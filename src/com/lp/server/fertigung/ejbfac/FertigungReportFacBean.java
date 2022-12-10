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
package com.lp.server.fertigung.ejbfac;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Predicate;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikelbestellt;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikellistespr;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelreservierung;
import com.lp.server.artikel.fastlanereader.generated.FLRFehlmenge;
import com.lp.server.artikel.fastlanereader.generated.FLRVerschleissteilwerkzeug;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelbestelltFac;
import com.lp.server.artikel.service.ArtikelfehlmengeDto;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.ArtikelkommentarartDto;
import com.lp.server.artikel.service.ArtikellagerplaetzeDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.artikel.service.ErsatztypenDto;
import com.lp.server.artikel.service.GeometrieDto;
import com.lp.server.artikel.service.GeraetesnrDto;
import com.lp.server.artikel.service.HerstellerDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerbewegungDto;
import com.lp.server.artikel.service.MaterialDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VerpackungDto;
import com.lp.server.artikel.service.VerpackungsmittelDto;
import com.lp.server.artikel.service.VerschleissteilDto;
import com.lp.server.artikel.service.VkPreisfindungEinzelverkaufspreisDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.artikel.service.WarenabgangsreferenzDto;
import com.lp.server.artikel.service.WarenzugangsreferenzDto;
import com.lp.server.artikel.service.WerkzeugDto;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragposition;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.AuftragzeitenDto;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellposition;
import com.lp.server.bestellung.service.BSMahnungDto;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.fertigung.ejb.Bedarfsuebernahme;
import com.lp.server.fertigung.fastlanereader.generated.FLRBedarfsuebernahme;
import com.lp.server.fertigung.fastlanereader.generated.FLRLos;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosReport;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosablieferung;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosgutschlecht;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosistmaterial;
import com.lp.server.fertigung.fastlanereader.generated.FLRLospruefplan;
import com.lp.server.fertigung.fastlanereader.generated.FLRLossollarbeitsplan;
import com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial;
import com.lp.server.fertigung.fastlanereader.generated.FLROffeneags;
import com.lp.server.fertigung.fastlanereader.generated.FLRPruefergebnis;
import com.lp.server.fertigung.service.AblieferstatistikJournalKriterienDto;
import com.lp.server.fertigung.service.BedarfsuebernahmeDto;
import com.lp.server.fertigung.service.BucheSerienChnrAufLosDto;
import com.lp.server.fertigung.service.FehlmengenBeiAusgabeMehrererLoseDto;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.fertigung.service.GesamtkalkulationDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosMonatsauswertungDto;
import com.lp.server.fertigung.service.LosStatistikDto;
import com.lp.server.fertigung.service.LosablieferungDto;
import com.lp.server.fertigung.service.LosgutschlechtDto;
import com.lp.server.fertigung.service.LosistmaterialDto;
import com.lp.server.fertigung.service.LoslagerentnahmeDto;
import com.lp.server.fertigung.service.LospruefplanDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.fertigung.service.PruefergebnisDto;
import com.lp.server.fertigung.service.ReportAufloesbareFehlmengenDto;
import com.lp.server.fertigung.service.ReportLosAusgabelisteDto;
import com.lp.server.fertigung.service.ReportLosnachkalkulationDto;
import com.lp.server.fertigung.service.ReportTheoretischeFehlmengenDto;
import com.lp.server.fertigung.service.ScriptStuecklisteGraphicServiceDto;
import com.lp.server.fertigung.service.StklPosDtoSearchParams;
import com.lp.server.fertigung.service.StklPosDtoSearchResult;
import com.lp.server.fertigung.service.TraceImportDto;
import com.lp.server.fertigung.service.ZusatzstatusDto;
import com.lp.server.forecast.fastlanereader.generated.FLRLinienabruf;
import com.lp.server.forecast.service.FclieferadresseDto;
import com.lp.server.forecast.service.ForecastDto;
import com.lp.server.forecast.service.ForecastauftragDto;
import com.lp.server.forecast.service.ForecastpositionDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.fastlanereader.generated.FLRZeitdatenLos;
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.personal.service.MaschinenzeitdatenDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.SollverfuegbarkeitDto;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.projekt.service.BereichDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.reklamation.fastlanereader.generated.FLRFehlerspr;
import com.lp.server.reklamation.service.FehlerDto;
import com.lp.server.stueckliste.fastlanereader.generated.FLRApkommentarspr;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste;
import com.lp.server.stueckliste.service.ApkommentarDto;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.stueckliste.service.PosersatzDto;
import com.lp.server.stueckliste.service.PruefartDto;
import com.lp.server.stueckliste.service.PruefkombinationDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.stueckliste.service.StuecklisteeigenschaftDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.system.ejbfac.HvPrinter;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeBedarfszusammenschau;
import com.lp.server.system.jcr.service.docnode.DocNodeLos;
import com.lp.server.system.jcr.service.docnode.DocNodeLosAblieferung;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.system.service.ReportvarianteDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.DatumsfilterVonBis;
import com.lp.server.util.HelperServer;
import com.lp.server.util.HvOptional;
import com.lp.server.util.LPReport;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.server.util.report.TimingInterceptor;
import com.lp.util.EJBExceptionLP;
import com.lp.util.HVPdfReport;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.renderers.BatikRenderer;

@Stateless
@Interceptors(TimingInterceptor.class)
public class FertigungReportFacBean extends LPReport implements FertigungReportFac, JRDataSource {

	@PersistenceContext
	private EntityManager em;

	private int useCase;
	private Object[][] data = null;

	private static final int UC_THEORETISCHE_FEHLMENGEN = 0;
	private static final int UC_FERTIGUNGSBEGLEITSCHEIN = 1;
	private static final int UC_AUSGABELISTE = 2;
	private static final int UC_ALLE = 3;
	private static final int UC_OFFENE = 4;
	private static final int UC_HALBFERTIGFABRIKATSINVENTUR = 5;
	private static final int UC_NACHKALKULATION = 6;
	private static final int UC_ABLIEFERUNGSSTATISTIK = 7;
	private static final int UC_AUFLOESBARE_FEHLMENGEN = 8;
	private static final int UC_STUECKRUECKMELDUNG = 9;
	private static final int UC_GANZSEITIGESBILD = 10;
	private static final int UC_MONATSAUSWERTUNG = 11;
	private static final int UC_RANKINGLISTE = 12;
	private static final int UC_ETIKETT = 13;
	private static final int UC_FEHLMENGEN_ALLER_LOSE = 14;
	private static final int UC_PRODUKTIONSINFORMATION = 15;
	private static final int UC_ZEITENTWICKLUNG = 16;
	private static final int UC_AUSLASTUNGSVORSCHAU = 17;
	private static final int UC_AUSLIEFERLISTE = 18;
	private static final int UC_LOSSTATISTIK = 19;
	private static final int UC_FEHLTEILE = 20;
	private static final int UC_OFFENE_AG = 21;
	private static final int UC_ABLIEFERETIKETT = 22;
	private static final int UC_LOSZEITEN = 23;
	private static final int UC_ETIKETTA4 = 24;
	private static final int UC_FEHLERSTATISTIK = 25;
	private static final int UC_MATERIALLISTE = 26;
	private static final int UC_AUSLASTUNGSVORSCHAU_DETAILIERT = 27;
	private static final int UC_GESAMTKALKULATION = 28;
	private static final int UC_MASCHINEUNDMATERIAL = 29;
	private static final int UC_BEDARFSZUSAMMENSCHAU = 30;
	private static final int UC_TAETIGKEITAGBEGINN = 31;
	private static final int UC_LOSPRUEFPLAN = 32;
	private static final int UC_PRUEFERGEBNIS = 33;
	private static final int UC_NICHT_ERFASSTE_PRUEFERGEBNISSE = 34;
	private static final int UC_VERGLEICH_MIT_STUECKLISTE = 35;
	private static final int UC_VERSANDETIKETT_ABLIEFERUNG = 36;
	private static final int UC_VERSANDETIKETT_VORBEREITUNG = 37;
	private static final int UC_ARBEITSZEITSTATUS = 38;
	private static final int UC_BEDARFSUEBERNAHME_SYNCHRONISIERUNG = 39;
	private static final int UC_BEDARFSUEBERNAHME_BUCHUNGSLISTE = 40;
	private static final int UC_BEDARFSUEBERNAHME_OFFENE = 41;
	private static final int UC_TRACEIMPORT = 42;
	private static final int UC_SONDERETIKETT = 43;
	private static final int UC_LADELISTE = 44;

	private static int NEP_LOSNUMMER = 0;
	private static int NEP_LOSGROESSE = 1;
	private static int NEP_STATUS = 2;
	private static int NEP_PRODUKTIONSBEGINN = 3;
	private static int NEP_STUECKLISTEBEZEICHNUNG = 4;
	private static int NEP_STUECKLISTEZUSATZBEZEICHNUNG = 5;
	private static int NEP_STUECKLISTENUMMER = 6;
	private static int NEP_ARTIKEL_KONTAKT = 7;
	private static int NEP_ARTIKELBEZEICHNUNG_KONTAKT = 8;
	private static int NEP_POSITION_KONTAKT = 9;
	private static int NEP_ARTIKEL_LITZE = 10;
	private static int NEP_ARTIKELBEZEICHNUNG_LITZE = 11;
	private static int NEP_POSITION_LITZE = 12;
	private static int NEP_WERKZEUGNUMMER = 13;
	private static int NEP_WERKZEUGBEZEICHNUNG = 14;
	private static int NEP_VERSCHLEISSTEILNUMMER = 15;
	private static int NEP_VERSCHLEISSTEILBEZEICHNUNG = 16;
	private static int NEP_PRUEFART = 17;
	private static int NEP_PRUEFART_BEZEICHNUNG = 18;
	private static int NEP_ABLIEFERDATUM = 19;

	private final static int NEP_SPALTENANZAHL = 20;

	private static final int TF_IDENT = 0;
	private static final int TF_BEZEICHNUNG = 1;

	private static final int TF_SOLLMENGE = 2;
	private static final int TF_VERFUEGBAR = 3;
	private static final int TF_FEHLMENGE = 4;
	private static final int TF_BESTELLT = 5;
	private static final int TF_MOEGLICH = 6;
	private static final int TF_LAGERSTAND = 7;
	private static final int TF_ZUSATZBEZEICHNUNG = 8;
	private static final int TF_ISTMENGE = 9;
	private static final int TF_VERFUEGBARZUMPRODUKTIONSSTART = 10;
	private static final int TF_ARTIKELSPERREN = 11;
	private static final int TF_ERSATZIDENT = 12;
	private static final int TF_ERSATZBEZEICHNUNG = 13;

	private static final int TF_ANGELEGT = 14;
	private static final int TF_AUFTRAGNUMMER = 15;
	private static final int TF_KOSTENSTELLENUMMER = 16;
	private static final int TF_KUNDE = 17;
	private static final int TF_LOSGROESSE = 18;
	private static final int TF_LOSNUMMER = 19;
	private static final int TF_PRODUKTIONSBEGINN = 20;
	private static final int TF_STUECKLISTEBEZEICHNUNG = 21;
	private static final int TF_STUECKLISTEZUSATZBEZEICHNUNG = 22;
	private static final int TF_STUECKLISTENUMMER = 23;
	private static final int TF_MENGENEINHEIT = 24;
	private static final int TF_STUECKLISTEARTIKELKOMMENTAR = 25;
	private static final int TF_STATUS = 26;
	private static final int TF_SUBREPORT_BILDER = 27;
	private static final int TF_IN_FERTIGUNG = 28;
	private static final int TF_LAGERBEWIRTSCHAFTET = 29;
	private static final int TF_I_SORT_AUS_STUECKLISTE = 30;
	private static final int TF_BESTELLTZUMPRODUKTIONSSTART = 31;
	private static final int TF_KURZBEZEICHNUNG = 32;
	private static final int TF_REFERENZNUMMER = 33;
	private static final int TF_STUECKLISTEKURZBEZEICHNUNG = 34;
	private static final int TF_STUECKLISTEREFERENZNUMMER = 35;
	private static final int TF_ARTIKEL_ANZAHL_ANGEFRAGT = 36;
	private static final int TF_ARTIKEL_ANZAHL_ANGEBOTEN = 37;

	private static final int TF_SPALTENANZAHL = 38;

	private static final int BEDARFSUEBERNAHME_SYNCHRONISIERUNG_PERSON = 0;
	private static final int BEDARFSUEBERNAHME_SYNCHRONISIERUNG_LOSNUMMER_MANUELL = 1;
	private static final int BEDARFSUEBERNAHME_SYNCHRONISIERUNG_LOS_PROJEKT = 2;
	private static final int BEDARFSUEBERNAHME_SYNCHRONISIERUNG_AUFTRAG = 3;
	private static final int BEDARFSUEBERNAHME_SYNCHRONISIERUNG_WUNSCHMENGE = 4;
	private static final int BEDARFSUEBERNAHME_SYNCHRONISIERUNG_WUNSCHTERMIN = 5;
	private static final int BEDARFSUEBERNAHME_SYNCHRONISIERUNG_ARTIKELNUMMER_MANUELL = 6;
	private static final int BEDARFSUEBERNAHME_SYNCHRONISIERUNG_ARTIKELBEZEICHNUNG_MANUELL = 7;
	private static final int BEDARFSUEBERNAHME_SYNCHRONISIERUNG_ABGANG = 8;
	private static final int BEDARFSUEBERNAHME_SYNCHRONISIERUNG_ZUSAETZLICH = 9;
	private static final int BEDARFSUEBERNAHME_SYNCHRONISIERUNG_ARTIKELNUMMER_HV = 10;
	private static final int BEDARFSUEBERNAHME_SYNCHRONISIERUNG_ARTIKELBEZEICHNUNG_HV = 11;
	private static final int BEDARFSUEBERNAHME_SYNCHRONISIERUNG_LOSNUMMER_HV = 12;
	private static final int BEDARFSUEBERNAHME_SYNCHRONISIERUNG_STUECKLISTENNUMMER = 13;
	private static final int BEDARFSUEBERNAHME_SYNCHRONISIERUNG_STUECKLISTENBEZEICHNUNG = 14;
	private static final int BEDARFSUEBERNAHME_SYNCHRONISIERUNG_KUNDE = 15;
	private static final int BEDARFSUEBERNAHME_SYNCHRONISIERUNG_BILD = 16;
	private static final int BEDARFSUEBERNAHME_SYNCHRONISIERUNG_KOMMENTAR = 17;
	private static final int BEDARFSUEBERNAHME_SYNCHRONISIERUNG_LOS_STATUS = 18;
	private static final int BEDARFSUEBERNAHME_SYNCHRONISIERUNG_SPALTENANZAHL = 19;

	private static final int BEDARFSUEBERNAHME_BUCHUNGSLISTE_PERSON = 0;
	private static final int BEDARFSUEBERNAHME_BUCHUNGSLISTE_LOSNUMMER_MANUELL = 1;
	private static final int BEDARFSUEBERNAHME_BUCHUNGSLISTE_LOS_PROJEKT = 2;
	private static final int BEDARFSUEBERNAHME_BUCHUNGSLISTE_AUFTRAG = 3;
	private static final int BEDARFSUEBERNAHME_BUCHUNGSLISTE_WUNSCHMENGE = 4;
	private static final int BEDARFSUEBERNAHME_BUCHUNGSLISTE_WUNSCHTERMIN = 5;
	private static final int BEDARFSUEBERNAHME_BUCHUNGSLISTE_ARTIKELNUMMER_MANUELL = 6;
	private static final int BEDARFSUEBERNAHME_BUCHUNGSLISTE_ARTIKELBEZEICHNUNG_MANUELL = 7;
	private static final int BEDARFSUEBERNAHME_BUCHUNGSLISTE_ABGANG = 8;
	private static final int BEDARFSUEBERNAHME_BUCHUNGSLISTE_ZUSAETZLICH = 9;
	private static final int BEDARFSUEBERNAHME_BUCHUNGSLISTE_ARTIKELNUMMER_HV = 10;
	private static final int BEDARFSUEBERNAHME_BUCHUNGSLISTE_ARTIKELBEZEICHNUNG_HV = 11;
	private static final int BEDARFSUEBERNAHME_BUCHUNGSLISTE_LOSNUMMER_HV = 12;
	private static final int BEDARFSUEBERNAHME_BUCHUNGSLISTE_STUECKLISTENNUMMER = 13;
	private static final int BEDARFSUEBERNAHME_BUCHUNGSLISTE_STUECKLISTENBEZEICHNUNG = 14;
	private static final int BEDARFSUEBERNAHME_BUCHUNGSLISTE_KUNDE = 15;
	private static final int BEDARFSUEBERNAHME_BUCHUNGSLISTE_BILD = 16;
	private static final int BEDARFSUEBERNAHME_BUCHUNGSLISTE_KOMMENTAR = 17;
	private static final int BEDARFSUEBERNAHME_BUCHUNGSLISTE_LOS_STATUS = 18;
	private static final int BEDARFSUEBERNAHME_BUCHUNGSLISTE_MENGE_GENEHMIGT = 19;
	private static final int BEDARFSUEBERNAHME_BUCHUNGSLISTE_MENGE_GEBUCHT = 20;
	private static final int BEDARFSUEBERNAHME_BUCHUNGSLISTE_LAGER = 21;
	private static final int BEDARFSUEBERNAHME_BUCHUNGSLISTE_ARTIKELEINHEIT = 22;
	private static final int BEDARFSUEBERNAHME_BUCHUNGSLISTE_SPALTENANZAHL = 23;

	private static final int BEDARFSUEBERNAHME_OFFENE_PERSON = 0;
	private static final int BEDARFSUEBERNAHME_OFFENE_LOSNUMMER_MANUELL = 1;
	private static final int BEDARFSUEBERNAHME_OFFENE_LOS_PROJEKT = 2;
	private static final int BEDARFSUEBERNAHME_OFFENE_AUFTRAG = 3;
	private static final int BEDARFSUEBERNAHME_OFFENE_WUNSCHMENGE = 4;
	private static final int BEDARFSUEBERNAHME_OFFENE_WUNSCHTERMIN = 5;
	private static final int BEDARFSUEBERNAHME_OFFENE_ARTIKELNUMMER_MANUELL = 6;
	private static final int BEDARFSUEBERNAHME_OFFENE_ARTIKELBEZEICHNUNG_MANUELL = 7;
	private static final int BEDARFSUEBERNAHME_OFFENE_ABGANG = 8;
	private static final int BEDARFSUEBERNAHME_OFFENE_ZUSAETZLICH = 9;
	private static final int BEDARFSUEBERNAHME_OFFENE_ARTIKELNUMMER_HV = 10;
	private static final int BEDARFSUEBERNAHME_OFFENE_ARTIKELBEZEICHNUNG_HV = 11;
	private static final int BEDARFSUEBERNAHME_OFFENE_LOSNUMMER_HV = 12;
	private static final int BEDARFSUEBERNAHME_OFFENE_STUECKLISTENNUMMER = 13;
	private static final int BEDARFSUEBERNAHME_OFFENE_STUECKLISTENBEZEICHNUNG = 14;
	private static final int BEDARFSUEBERNAHME_OFFENE_KUNDE = 15;
	private static final int BEDARFSUEBERNAHME_OFFENE_BILD = 16;
	private static final int BEDARFSUEBERNAHME_OFFENE_KOMMENTAR = 17;
	private static final int BEDARFSUEBERNAHME_OFFENE_LOS_STATUS = 18;
	private static final int BEDARFSUEBERNAHME_OFFENE_MENGE_GENEHMIGT = 19;
	private static final int BEDARFSUEBERNAHME_OFFENE_MENGE_GEBUCHT = 20;
	private static final int BEDARFSUEBERNAHME_OFFENE_LAGER = 21;
	private static final int BEDARFSUEBERNAHME_OFFENE_LAGERSTAND_LOSLAEGER = 22;
	private static final int BEDARFSUEBERNAHME_OFFENE_ARTIKELEINHEIT = 23;
	private static final int BEDARFSUEBERNAHME_OFFENE_SPALTENANZAHL = 24;

	private static final int AUSG_IDENT = 0;
	private static final int AUSG_MENGE = 1;
	private static final int AUSG_EINHEIT = 2;
	private static final int AUSG_AUSGABE = 3;
	private static final int AUSG_BEZEICHNUNG = 4;
	private static final int AUSG_LAGER = 5;
	private static final int AUSG_LAGERORT = 6;
	private static final int AUSG_MONTAGEART = 7;
	private static final int AUSG_SCHALE = 8;
	private static final int AUSG_ARTIKELKLASSE = 9;
	private static final int AUSG_ZUSATZBEZEICHNUNG = 10;
	private static final int AUSG_ZUSATZBEZEICHNUNG2 = 11;
	private static final int AUSG_FARBCODE = 12;
	private static final int AUSG_MATERIAL = 13;
	private static final int AUSG_HOEHE = 14;
	private static final int AUSG_BREITE = 15;
	private static final int AUSG_TIEFE = 16;
	private static final int AUSG_BAUFORM = 17;
	private static final int AUSG_VERPACKUNGSART = 18;
	private static final int AUSG_GEWICHTKG = 19;
	private static final int AUSG_RASTERSTEHEND = 20;
	private static final int AUSG_STUECKLISTE_KOMMENTAR = 21;
	private static final int AUSG_BESTELLT = 22;
	private static final int AUSG_SNRCHNR = 23;
	private static final int AUSG_KOMMENTAR = 24;
	private static final int AUSG_ARTIKELBILD = 25;
	private final static int AUSG_LAGERSTAND = 26;
	private final static int AUSG_LAGERSTAND_SPERRLAGER = 27;
	private final static int AUSG_INDEX = 28;
	private final static int AUSG_REVISION = 29;
	private final static int AUSG_IN_FERTIGUNG = 30;
	private final static int AUSG_NUR_ZUR_INFO = 31;
	private static final int AUSG_STUECKLISTE_POSITION = 32;
	private static final int AUSG_STANDORT = 33;
	private static final int AUSG_STUECKLISTE_SOLLMENGE = 34;
	private static final int AUSG_STUECKLISTE_EINHEIT = 35;
	private static final int AUSG_MATERIAL_POSITION = 36;
	private static final int AUSG_MATERIAL_KOMMENTAR = 37;
	private static final int AUSG_MATERIAL_DIMENSION1 = 38;
	private static final int AUSG_MATERIAL_DIMENSION2 = 39;
	private static final int AUSG_MATERIAL_DIMENSION3 = 40;
	private static final int AUSG_KURZBEZEICHNUNG = 41;
	private static final int AUSG_REFERENZNUMMER = 42;
	private static final int AUSG_LOSNUMMER = 43;
	private static final int AUSG_LOSSOLLMATERIAL_IIDS = 44;

	private static final int AUSG_LOS_LOSNUMMER = 45;
	private static final int AUSG_LOS_ANGELEGT = 46;
	private static final int AUSG_LOS_AUFTRAGNUMMER = 47;
	private static final int AUSG_LOS_KOSTENSTELLENUMMER = 48;
	private static final int AUSG_LOS_PRODUKTIONSBEGINN = 49;
	private static final int AUSG_LOS_PRODUKTIONSENDE = 50;
	private static final int AUSG_LOS_KUNDE = 51;
	private static final int AUSG_LOS_STUECKLISTENUMMER = 52;
	private static final int AUSG_LOS_STUECKLISTEBEZEICHNUNG = 53;
	private static final int AUSG_LOS_STUECKLISTEZUSATZBEZEICHNUNG = 54;
	private static final int AUSG_LOS_STUECKLISTE_ERFASSUNGSFAKTOR = 55;
	private static final int AUSG_LOS_LOSGROESSE = 56;

	private static final int AUSG_FEHLMENGE = 57;

	private final static int AUSG_SPALTENANZAHL = 58;

	private static final int BEGL_IDENT = 0;
	private static final int BEGL_BEZEICHNUNG = 1;
	private static final int BEGL_RUESTZEIT = 2;
	private static final int BEGL_STUECKZEIT = 3;
	private static final int BEGL_GESAMTZEIT = 4;
	private static final int BEGL_KOMMENTAR = 5;
	private static final int BEGL_ZUSATZBEZEICHNUNG = 6;
	private static final int BEGL_ZUSATZBEZEICHNUNG2 = 7;
	private static final int BEGL_MASCHINE = 8;
	private static final int BEGL_MASCHINE_BEZEICHNUNG = 9;
	private static final int BEGL_ARBEITSGANG = 10;
	private static final int BEGL_IST_MATERIAL = 11;
	private static final int BEGL_MATERIAL_IDENT = 12;
	private static final int BEGL_MATERIAL_MENGE = 13;
	private static final int BEGL_MATERIAL_EINHEIT = 14;
	private static final int BEGL_MATERIAL_AUSGABE = 15;
	private static final int BEGL_MATERIAL_BEZEICHNUNG = 16;
	private static final int BEGL_MATERIAL_LAGER = 17;
	private static final int BEGL_MATERIAL_LAGERORT = 18;
	private static final int BEGL_MATERIAL_MONTAGEART = 19;
	private static final int BEGL_MATERIAL_SCHALE = 20;
	private static final int BEGL_MATERIAL_ARTIKELKLASSE = 21;
	private static final int BEGL_MATERIAL_ZUSATZBEZEICHNUNG = 22;
	private static final int BEGL_MATERIAL_ZUSATZBEZEICHNUNG2 = 23;
	private static final int BEGL_MATERIAL_FARBCODE = 24;
	private static final int BEGL_MATERIAL_MATERIAL = 25;
	private static final int BEGL_MATERIAL_HOEHE = 26;
	private static final int BEGL_MATERIAL_BREITE = 27;
	private static final int BEGL_MATERIAL_TIEFE = 28;
	private final static int BEGL_MATERIAL_INDEX = 29;
	private final static int BEGL_MATERIAL_REVISION = 30;
	private static final int BEGL_BAUFORM = 31;
	private static final int BEGL_VERPACKUNGSART = 32;
	private static final int BEGL_GEWICHTKG = 33;
	private static final int BEGL_AGART = 34;
	private static final int BEGL_AUFSPANNUNG = 35;
	private static final int BEGL_UNTERARBEITSGANG = 36;
	private static final int BEGL_FREMDMATERIAL_ARTIKEL = 37;
	private static final int BEGL_FREMDMATERIAL_ARTIKELBEZEICHNUNG = 38;
	private static final int BEGL_FREMDMATERIAL_ARTIKELKURZBEZEICHNUNG = 39;
	private static final int BEGL_FREMDMATERIAL_ARTIKELZUSATZBEZEICHNUNG = 40;
	private static final int BEGL_FREMDMATERIAL_ARTIKELZUSATZBEZEICHNUNG2 = 41;
	private static final int BEGL_FREMDMATERIAL_SOLLMENGE = 42;
	private static final int BEGL_FREMDMATERIAL_LAGERORT = 43;
	private static final int BEGL_FERTIG = 44;
	private static final int BEGL_NURZURINFORMATION = 45;
	private static final int BEGL_MATERIAL_SNRCHNR = 46;
	private static final int BEGL_MATERIAL_STUECKLISTE_POSITION = 47;

	private static final int BEGL_MATERIAL_STUECKLISTE_SOLLMENGE = 48;
	private static final int BEGL_MATERIAL_STUECKLISTE_EINHEIT = 49;
	private static final int BEGL_MATERIAL_DIMENSION1 = 50;
	private static final int BEGL_MATERIAL_DIMENSION2 = 51;
	private static final int BEGL_MATERIAL_DIMENSION3 = 52;

	private static final int BEGL_ARBEITSPLANKOMMENTAR = 54;

	private static final int BEGL_NEXT_AG_IDENT = 49;
	private static final int BEGL_NEXT_AG_BEZEICHNUNG = 50;
	private static final int BEGL_NEXT_AG_RUESTZEIT = 51;
	private static final int BEGL_NEXT_AG_STUECKZEIT = 52;
	private static final int BEGL_NEXT_AG_GESAMTZEIT = 53;
	private static final int BEGL_NEXT_AG_KOMMENTAR = 54;
	private static final int BEGL_NEXT_AG_ZUSATZBEZEICHNUNG = 55;
	private static final int BEGL_NEXT_AG_ZUSATZBEZEICHNUNG2 = 56;
	private static final int BEGL_NEXT_AG_MASCHINE = 57;
	private static final int BEGL_NEXT_AG_MASCHINE_BEZEICHNUNG = 58;
	private static final int BEGL_NEXT_AG_AGART = 59;
	private static final int BEGL_NEXT_AG_AUFSPANNUNG = 60;
	private static final int BEGL_NEXT_AG_UNTERARBEITSGANG = 61;
	private static final int BEGL_NEXT_AG_ARBEITSGANG = 62;
	private static final int BEGL_NEXT_AG_ARBEITSPLANKOMMENTAR = 63;
	private final static int BEGL_NEXT_AG_BEGINN = 64;
	private final static int BEGL_NEXT_AG_MASCHINENVERSATZ_MS = 65;
	private static final int BEGL_NEXT_AG_FREMDMATERIAL_ARTIKEL = 66;
	private static final int BEGL_NEXT_AG_FREMDMATERIAL_ARTIKELBEZEICHNUNG = 67;
	private static final int BEGL_SUBREPORT_ALTERNATIV_MASCHINEN = 68;
	private final static int BEGL_BEGINN = 69;
	private final static int BEGL_MASCHINENVERSATZ_MS = 70;
	private static final int BEGL_NEXT_AG_MASCHINE_MANUELLE_BEDIENUNG = 71;
	private static final int BEGL_MASCHINE_MANUELLE_BEDIENUNG = 72;
	private static final int BEGL_MATERIAL_KURZBEZEICHNUNG = 73;
	private static final int BEGL_MATERIAL_REFERENZNUMMER = 74;
	private final static int BEGL_SPALTENANZAHL = 75;

	private static final int MATERIAL_ART = 0;
	private static final int MATERIAL_ARTIKELNUMMER = 1;
	private static final int MATERIAL_BEZEICHNUNG = 2;
	private static final int MATERIAL_ZUSATZBEZEICHNUNG = 3;
	private static final int MATERIAL_ZUSATZBEZEICHNUNG2 = 4;
	private static final int MATERIAL_KURZBEZEICHNUNG = 5;
	private static final int MATERIAL_SOLLMENGE = 6;
	private static final int MATERIAL_ISTMENGE = 7;
	private static final int MATERIAL_SOLLPREIS = 8;
	private static final int MATERIAL_LIEF1PREIS = 9;
	private static final int MATERIAL_ISTPREIS = 10;
	private static final int MATERIAL_WE_REFERENZ = 11;
	private static final int MATERIAL_SNRCHNR = 12;
	private static final int MATERIAL_EINHEIT = 13;
	private static final int MATERIAL_SUBREPORT_SNR_GSNR = 14;
	private static final int MATERIAL_LAGERORT = 15;
	private static final int MATERIAL_REFERENZNUMMER = 16;
	private static final int MATERIAL_VKPREIS = 17;
	private static final int MATERIAL_ERSATZTYP = 19;
	private static final int MATERIAL_ORIGINALARTIKELNUMMER = 20;
	private static final int MATERIAL_KOMMENTAR = 21;
	private final static int MATERIAL_SPALTENANZAHL = 22;

	private static final int VMS_ARTIKELNUMMER = 0;
	private static final int VMS_BEZEICHNUNG = 1;
	private static final int VMS_ZUSATZBEZEICHNUNG = 2;
	private static final int VMS_ZUSATZBEZEICHNUNG2 = 3;
	private static final int VMS_KURZBEZEICHNUNG = 4;
	private static final int VMS_SOLLMENGE_LOS = 5;
	private static final int VMS_SOLLMENGE_STUECKLISTE = 6;
	private static final int VMS_SOLLSATZGROESSE = 7;
	private static final int VMS_EINHEIT = 8;
	private static final int VMS_MONTAGEART = 9;
	private static final int VMS_IN_LOS = 10;
	private static final int VMS_IN_STUECKLISTE = 11;
	private static final int VMS_AUSGABEMENGE = 12;
	private static final int VMS_AUSGABEPREIS = 13;
	private static final int VMS_IS_ERSATZARTIKEL = 14;
	private static final int VMS_SOLLSATZGROESSE_GESAMT = 15;
	private final static int VMS_SPALTENANZAHL = 16;

	private static final int ALLE_LOSNUMMER = 0;
	private static final int ALLE_IDENT = 1;
	private static final int ALLE_BEZEICHNUNG = 2;
	private static final int ALLE_MATERIALSOLLMENGE = 3;
	private static final int ALLE_MATERIALISTMENGE = 4;
	private static final int ALLE_MATERIALSOLLPREIS = 5;
	private static final int ALLE_MATERIALISTPREIS = 6;
	private static final int ALLE_ARBEITSZEITSOLLMENGE = 7;
	private static final int ALLE_ARBEITSZEITISTMENGE = 8;
	private static final int ALLE_ARBEITSZEITSOLLPREIS = 9;
	private static final int ALLE_ARBEITSZEITISTPREIS = 10;
	private static final int ALLE_ZUSATZBEZEICHNUNG = 11;
	private static final int ALLE_BEGINNTERMIN = 12;
	private static final int ALLE_AUFTRAGLIEFERTERMIN = 13;
	private static final int ALLE_LOSGROESSE = 14;
	private static final int ALLE_OFFENERAHMENRESERVIERUNGEN = 15;
	private static final int ALLE_AUFTRAGSNUMMER = 16;
	private static final int ALLE_KUNDE = 17;
	private static final int ALLE_FERTIGUNGSGRUPPE = 18;
	private static final int ALLE_STATUS = 19;
	private static final int ALLE_ENDETERMIN = 20;
	private static final int ALLE_ABGELIEFERT = 21;
	private static final int ALLE_FELDANZAHL = 22;

	private static final int OFFENE_LOSNUMMER = 0;
	private static final int OFFENE_ARTIKELNUMMER = 1;
	private static final int OFFENE_BEZEICHNUNG = 2;
	private static final int OFFENE_KALENDERWOCHE = 3;
	private static final int OFFENE_AUFTRAGSNUMMER = 4;
	private static final int OFFENE_LOSGROESSE = 5;
	private static final int OFFENE_GELIEFERT = 6;
	private static final int OFFENE_BEGINN = 7;
	private static final int OFFENE_ENDE = 8;
	private static final int OFFENE_MATERIAL = 9;
	private static final int OFFENE_ZUSATZBEZEICHNUNG = 10;
	private static final int OFFENE_KUNDE = 11;
	private static final int OFFENE_PROJEKT = 12;
	private static final int OFFENE_FERTIGUNGSGRUPPE = 13;
	private static final int OFFENE_FEHLMENGE = 14;
	private static final int OFFENE_RESERVIERUNGEN = 15;
	private static final int OFFENE_RAHMEN_BESTELLT = 16;
	private static final int OFFENE_AUFTRAGSPOENALE = 17;
	private static final int OFFENE_FERTIGUNGSZEIT = 18;
	private static final int OFFENE_LOSSTATUS = 19;
	private static final int OFFENE_LIEFERTERMIN = 20;
	private static final int OFFENE_RAHMENRESERVIERUNGEN = 21;
	private static final int OFFENE_DETAILBEDARF = 22;
	private static final int OFFENE_LOSHATFEHLMENGE = 23;
	private static final int OFFENE_AUFTRAGSSTATUS = 24;
	private static final int OFFENE_KUNDE_KBEZ = 25;

	private static final int OFFENE_FORECAST_KUNDE_LIEFERADRESSE_KBEZ = 25;
	private static final int OFFENE_FORECAST_NUMMER = 26;
	private static final int OFFENE_FORECASTAUFTRAG_ANLAGEDATUM = 27;
	private static final int OFFENE_FORECASTPOSITION_TERMIN = 28;

	private static final int OFFENE_IN_FERTIGUNG = 29;
	private static final int OFFENE_BESTELLT = 30;
	private static final int OFFENE_LAGERSTAND_SPERRLAEGER = 31;
	private static final int OFFENE_LAGERSTAND_MITKONSIGNATIONSLAGER = 32;
	private static final int OFFENE_LAGERSTAND_OHNEKONSIGNATIONSLAGER = 33;
	private static final int OFFENE_LOSKLASSEN = 34;
	private final static int OFFENE_SPALTENANZAHL = 35;

	private final static int MASCHINEUNDMATERIAL_LOSNUMMER = 0;
	private final static int MASCHINEUNDMATERIAL_ARTIKELNUMMER_TAETIGKEIT = 1;
	private final static int MASCHINEUNDMATERIAL_ARTIKELBEZEICHNUNG_TAETIGKEIT = 2;
	private final static int MASCHINEUNDMATERIAL_ARBEITSGANG = 3;
	private final static int MASCHINEUNDMATERIAL_UNTERARBEITSGANG = 4;
	private final static int MASCHINEUNDMATERIAL_AG_ART = 5;
	private final static int MASCHINEUNDMATERIAL_AG_BEGINN = 6;
	private final static int MASCHINEUNDMATERIAL_AUFSPANNUNG = 7;
	private final static int MASCHINEUNDMATERIAL_PERSON = 8;
	private static final int MASCHINEUNDMATERIAL_MASCHINE_IDENTIFIKATIONSNUMMMER = 9;
	private static final int MASCHINEUNDMATERIAL_MASCHINE_BEZEICHNUNG = 10;
	private static final int MASCHINEUNDMATERIAL_MASCHINE_INVENTARNUMMMER = 11;
	private final static int MASCHINEUNDMATERIAL_NUR_MASCHINENZEIT = 12;
	private final static int MASCHINEUNDMATERIAL_AUTO_STOP_BEI_GEHT = 13;
	private final static int MASCHINEUNDMATERIAL_KOMMENTAR = 14;
	private final static int MASCHINEUNDMATERIAL_KOMMENTAR_LANG = 15;
	private final static int MASCHINEUNDMATERIAL_STUECKZEIT = 16;
	private final static int MASCHINEUNDMATERIAL_RUESTZEIT = 17;
	private final static int MASCHINEUNDMATERIAL_ZEITEINHEIT_ARBEITSPLAN = 18;
	private final static int MASCHINEUNDMATERIAL_ARTIKELNUMMER_MATERIAL = 19;
	private final static int MASCHINEUNDMATERIAL_ARTIKELBEZEICHNUNG_MATERIAL = 20;
	private final static int MASCHINEUNDMATERIAL_GESAMTZEIT_IN_STUNDEN_PERSONAL = 21;

	private final static int MASCHINEUNDMATERIAL_KUNDE_KBEZ = 22;
	private final static int MASCHINEUNDMATERIAL_MASCHINENVERSATZ_MS = 23;

	private final static int MASCHINEUNDMATERIAL_NEXT_AG_ARTIKELNUMMER_TAETIGKEIT = 24;
	private final static int MASCHINEUNDMATERIAL_NEXT_AG_ARTIKELBEZEICHNUNG_TAETIGKEIT = 25;
	private final static int MASCHINEUNDMATERIAL_NEXT_AG_ARBEITSGANG = 26;
	private final static int MASCHINEUNDMATERIAL_NEXT_AG_UNTERARBEITSGANG = 27;
	private final static int MASCHINEUNDMATERIAL_NEXT_AG_AG_ART = 28;
	private final static int MASCHINEUNDMATERIAL_NEXT_AG_AG_BEGINN = 29;
	private final static int MASCHINEUNDMATERIAL_NEXT_AG_AUFSPANNUNG = 30;
	private final static int MASCHINEUNDMATERIAL_NEXT_AG_PERSON = 31;
	private static final int MASCHINEUNDMATERIAL_NEXT_AG_MASCHINE_IDENTIFIKATIONSNUMMMER = 32;
	private static final int MASCHINEUNDMATERIAL_NEXT_AG_MASCHINE_BEZEICHNUNG = 33;
	private static final int MASCHINEUNDMATERIAL_NEXT_AG_MASCHINE_INVENTARNUMMMER = 34;
	private final static int MASCHINEUNDMATERIAL_NEXT_AG_NUR_MASCHINENZEIT = 35;
	private final static int MASCHINEUNDMATERIAL_NEXT_AG_AUTO_STOP_BEI_GEHT = 36;
	private final static int MASCHINEUNDMATERIAL_NEXT_AG_KOMMENTAR = 37;
	private final static int MASCHINEUNDMATERIAL_NEXT_AG_KOMMENTAR_LANG = 38;
	private final static int MASCHINEUNDMATERIAL_NEXT_AG_STUECKZEIT = 39;
	private final static int MASCHINEUNDMATERIAL_NEXT_AG_RUESTZEIT = 40;
	private final static int MASCHINEUNDMATERIAL_NEXT_AG_ARTIKELNUMMER_MATERIAL = 41;
	private final static int MASCHINEUNDMATERIAL_NEXT_AG_ARTIKELBEZEICHNUNG_MATERIAL = 42;
	private final static int MASCHINEUNDMATERIAL_NEXT_AG_GESAMTZEIT_IN_STUNDEN_PERSONAL = 43;
	private final static int MASCHINEUNDMATERIAL_NEXT_AG_MASCHINENVERSATZ_MS = 44;
	private final static int MASCHINEUNDMATERIAL_FORTSCHRITT = 45;
	private final static int MASCHINEUNDMATERIAL_NEXT_AG_FORTSCHRITT = 46;
	private static final int MASCHINEUNDMATERIAL_MASCHINENGRUPPE = 47;
	private static final int MASCHINEUNDMATERIAL_NEXT_AG_MASCHINENGRUPPE = 48;
	private final static int MASCHINEUNDMATERIAL_ISTZEIT_IN_STUNDEN_PERSONAL = 49;
	private final static int MASCHINEUNDMATERIAL_LOSGROESSE = 50;
	private final static int MASCHINEUNDMATERIAL_ZUSATZSTATUS_P1 = 51;
	private final static int MASCHINEUNDMATERIAL_OFFENE_MENGE_MATERIAL = 52;
	private final static int MASCHINEUNDMATERIAL_NEXT_AG_OFFENE_MENGE_MATERIAL = 53;
	private final static int MASCHINEUNDMATERIAL_GESAMTZEIT_IN_STUNDEN_MASCHINE = 54;
	private final static int MASCHINEUNDMATERIAL_ISTZEIT_IN_STUNDEN_MASCHINE = 55;
	private final static int MASCHINEUNDMATERIAL_NEXT_AG_GESAMTZEIT_IN_STUNDEN_MASCHINE = 56;
	private final static int MASCHINEUNDMATERIAL_SPALTENANZAHL = 57;

	private final static int TAETIGKEITAGBEGINN_LOSNUMMER = 0;
	private final static int TAETIGKEITAGBEGINN_ARTIKELNUMMER_TAETIGKEIT = 1;
	private final static int TAETIGKEITAGBEGINN_ARTIKELBEZEICHNUNG_TAETIGKEIT = 2;
	private final static int TAETIGKEITAGBEGINN_ARBEITSGANG = 3;
	private final static int TAETIGKEITAGBEGINN_UNTERARBEITSGANG = 4;
	private final static int TAETIGKEITAGBEGINN_AG_ART = 5;
	private final static int TAETIGKEITAGBEGINN_AG_BEGINN = 6;
	private final static int TAETIGKEITAGBEGINN_AUFSPANNUNG = 7;
	private final static int TAETIGKEITAGBEGINN_PERSON = 8;
	private static final int TAETIGKEITAGBEGINN_MASCHINE_IDENTIFIKATIONSNUMMMER = 9;
	private static final int TAETIGKEITAGBEGINN_MASCHINE_BEZEICHNUNG = 10;
	private static final int TAETIGKEITAGBEGINN_MASCHINE_INVENTARNUMMMER = 11;
	private final static int TAETIGKEITAGBEGINN_NUR_MASCHINENZEIT = 12;
	private final static int TAETIGKEITAGBEGINN_AUTO_STOP_BEI_GEHT = 13;
	private final static int TAETIGKEITAGBEGINN_KOMMENTAR = 14;
	private final static int TAETIGKEITAGBEGINN_KOMMENTAR_LANG = 15;
	private final static int TAETIGKEITAGBEGINN_STUECKZEIT = 16;
	private final static int TAETIGKEITAGBEGINN_RUESTZEIT = 17;
	private final static int TAETIGKEITAGBEGINN_ZEITEINHEIT_ARBEITSPLAN = 18;
	private final static int TAETIGKEITAGBEGINN_ARTIKELNUMMER_MATERIAL = 19;
	private final static int TAETIGKEITAGBEGINN_ARTIKELBEZEICHNUNG_MATERIAL = 20;
	private final static int TAETIGKEITAGBEGINN_GESAMTZEIT_IN_STUNDEN_PERSONAL = 21;
	private final static int TAETIGKEITAGBEGINN_KUNDE_KBEZ = 22;
	private final static int TAETIGKEITAGBEGINN_MASCHINENVERSATZ_MS = 23;
	private final static int TAETIGKEITAGBEGINN_FORTSCHRITT = 24;
	private final static int TAETIGKEITAGBEGINN_NEXT_AG_BEGINN = 25;
	private final static int TAETIGKEITAGBEGINN_NEXT_MASCHINENVERSATZ_MS = 26;
	private final static int TAETIGKEITAGBEGINN_IN_ARBEIT = 27;
	private final static int TAETIGKEITAGBEGINN_LOSGROESSE = 28;
	private final static int TAETIGKEITAGBEGINN_ISTZEIT_IN_STUNDEN_PERSONAL = 29;
	private final static int TAETIGKEITAGBEGINN_ARTIKELNUMMER_STKL = 30;
	private final static int TAETIGKEITAGBEGINN_ARTIKELBEZEICHNUNG_STKL = 31;
	private final static int TAETIGKEITAGBEGINN_ARTIKELKURZBEZEICHNUNG_STKL = 32;
	private final static int TAETIGKEITAGBEGINN_ARTIKELZUSATZBEZEICHNUNG_STKL = 33;
	private final static int TAETIGKEITAGBEGINN_ARTIKELZUSATZBEZEICHNUNG2_STKL = 34;
	private final static int TAETIGKEITAGBEGINN_GESAMTZEIT_IN_STUNDEN_MASCHINE = 35;
	private final static int TAETIGKEITAGBEGINN_ISTZEIT_IN_STUNDEN_MASCHINE = 36;
	private final static int TAETIGKEITAGBEGINN_FEHLER_IN_ZEITDATEN = 37;
	private final static int TAETIGKEITAGBEGINN_SPALTENANZAHL = 38;

	private final static int LADELISTE_LOSNUMMER = 0;
	private final static int LADELISTE_LOSSTATUS = 1;
	private final static int LADELISTE_ZEITEINHEIT_ARBEITSPLAN = 2;
	private final static int LADELISTE_KUNDE_KBEZ = 3;
	private final static int LADELISTE_IN_ARBEIT = 4;
	private final static int LADELISTE_LOSGROESSE = 5;
	private final static int LADELISTE_ARTIKELNUMMER_STKL = 6;
	private final static int LADELISTE_ARTIKELBEZEICHNUNG_STKL = 7;
	private final static int LADELISTE_ARTIKELKURZBEZEICHNUNG_STKL = 8;
	private final static int LADELISTE_ARTIKELZUSATZBEZEICHNUNG_STKL = 9;
	private final static int LADELISTE_ARTIKELZUSATZBEZEICHNUNG2_STKL = 10;

	private final static int LADELISTE_AKTUELLER_ARBEITGANG_ARTIKELNUMMER_TAETIGKEIT = 11;
	private final static int LADELISTE_AKTUELLER_ARBEITGANG_BEZEICHNUNG_TAETIGKEIT = 12;
	private final static int LADELISTE_AKTUELLER_ARBEITGANG_ZUSATZBEZEICHNUNG_TAETIGKEIT = 13;
	private final static int LADELISTE_AKTUELLER_ARBEITGANG_FERTIG = 14;
	private final static int LADELISTE_AKTUELLER_ARBEITGANG_FORTSCHRITT = 15;
	private final static int LADELISTE_AKTUELLER_ARBEITGANG_ARBEITGANGNUMMER = 16;
	private final static int LADELISTE_AKTUELLER_ARBEITGANG_AG_BEGINN = 17;
	private final static int LADELISTE_AKTUELLER_ARBEITGANG_UNTERARBEITSGANGNUMMER = 18;
	private final static int LADELISTE_AKTUELLER_ARBEITGANG_SUBREPORT_GUT_SCHLECHT = 19;
	private final static int LADELISTE_AKTUELLER_ARBEITGANG_SUMME_GUTSTUECK = 20;
	private final static int LADELISTE_AKTUELLER_ARBEITGANG_SUMME_SCHLECHTSTUECK = 21;
	private final static int LADELISTE_AKTUELLER_ARBEITGANG_DATUM_JUENGSTE_STUECKRUECKMELDUNG = 22;
	private final static int LADELISTE_AKTUELLER_ARBEITGANG_KOMMENTAR_JUENGSTE_STUECKRUECKMELDUNG = 23;
	private final static int LADELISTE_AKTUELLER_ARBEITGANG_KOMMENTAR = 24;
	private final static int LADELISTE_AKTUELLER_ARBEITGANG_LANGTEXT = 25;

	private final static int LADELISTE_NAECHSTER_ARBEITGANG_ARTIKELNUMMER_TAETIGKEIT = 26;
	private final static int LADELISTE_NAECHSTER_ARBEITGANG_BEZEICHNUNG_TAETIGKEIT = 27;
	private final static int LADELISTE_NAECHSTER_ARBEITGANG_ZUSATZBEZEICHNUNG_TAETIGKEIT = 28;
	private final static int LADELISTE_NAECHSTER_ARBEITGANG_FERTIG = 29;
	private final static int LADELISTE_NAECHSTER_ARBEITGANG_FORTSCHRITT = 30;
	private final static int LADELISTE_NAECHSTER_ARBEITGANG_ARBEITGANGNUMMER = 31;
	private final static int LADELISTE_NAECHSTER_ARBEITGANG_AG_BEGINN = 32;
	private final static int LADELISTE_NAECHSTER_ARBEITGANG_UNTERARBEITSGANGNUMMER = 33;
	private final static int LADELISTE_NAECHSTER_ARBEITGANG_SUBREPORT_GUT_SCHLECHT = 34;
	private final static int LADELISTE_NAECHSTER_ARBEITGANG_SUMME_GUTSTUECK = 35;
	private final static int LADELISTE_NAECHSTER_ARBEITGANG_SUMME_SCHLECHTSTUECK = 36;
	private final static int LADELISTE_NAECHSTER_ARBEITGANG_DATUM_JUENGSTE_STUECKRUECKMELDUNG = 37;
	private final static int LADELISTE_NAECHSTER_ARBEITGANG_KOMMENTAR_JUENGSTE_STUECKRUECKMELDUNG = 38;
	private final static int LADELISTE_NAECHSTER_ARBEITGANG_KOMMENTAR = 39;
	private final static int LADELISTE_NAECHSTER_ARBEITGANG_LANGTEXT = 40;

	private final static int LADELISTE_VORHERIGER_ARBEITGANG_ARTIKELNUMMER_TAETIGKEIT = 41;
	private final static int LADELISTE_VORHERIGER_ARBEITGANG_BEZEICHNUNG_TAETIGKEIT = 42;
	private final static int LADELISTE_VORHERIGER_ARBEITGANG_ZUSATZBEZEICHNUNG_TAETIGKEIT = 43;
	private final static int LADELISTE_VORHERIGER_ARBEITGANG_FERTIG = 44;
	private final static int LADELISTE_VORHERIGER_ARBEITGANG_FORTSCHRITT = 45;
	private final static int LADELISTE_VORHERIGER_ARBEITGANG_ARBEITGANGNUMMER = 46;
	private final static int LADELISTE_VORHERIGER_ARBEITGANG_AG_BEGINN = 47;
	private final static int LADELISTE_VORHERIGER_ARBEITGANG_UNTERARBEITSGANGNUMMER = 48;
	private final static int LADELISTE_VORHERIGER_ARBEITGANG_SUBREPORT_GUT_SCHLECHT = 49;
	private final static int LADELISTE_VORHERIGER_ARBEITGANG_SUMME_GUTSTUECK = 50;
	private final static int LADELISTE_VORHERIGER_ARBEITGANG_SUMME_SCHLECHTSTUECK = 51;
	private final static int LADELISTE_VORHERIGER_ARBEITGANG_DATUM_JUENGSTE_STUECKRUECKMELDUNG = 52;
	private final static int LADELISTE_VORHERIGER_ARBEITGANG_KOMMENTAR_JUENGSTE_STUECKRUECKMELDUNG = 53;
	private final static int LADELISTE_VORHERIGER_ARBEITGANG_KOMMENTAR = 54;
	private final static int LADELISTE_VORHERIGER_ARBEITGANG_LANGTEXT = 55;

	private final static int LADELISTE_SPALTENANZAHL = 56;

	private static final int OFFENE_AG_LOSNUMMER = 0;
	private static final int OFFENE_AG_ARTIKELNUMMER = 1;
	private static final int OFFENE_AG_BEZEICHNUNG = 2;
	private static final int OFFENE_AG_KALENDERWOCHE = 3;
	private static final int OFFENE_AG_AUFTRAGSNUMMER = 4;
	private static final int OFFENE_AG_LOSGROESSE = 5;
	private static final int OFFENE_AG_GELIEFERT = 6;
	private static final int OFFENE_AG_BEGINN = 7;
	private static final int OFFENE_AG_ENDE = 8;
	private static final int OFFENE_AG_MATERIAL = 9;
	private static final int OFFENE_AG_ZUSATZBEZEICHNUNG = 10;
	private static final int OFFENE_AG_KUNDE = 11;
	private static final int OFFENE_AG_PROJEKT = 12;
	private static final int OFFENE_AG_FERTIGUNGSGRUPPE = 13;
	private static final int OFFENE_AG_FEHLMENGE = 14;
	private static final int OFFENE_AG_RESERVIERUNGEN = 15;
	private static final int OFFENE_AG_RAHMEN_BESTELLT = 16;
	private static final int OFFENE_AG_AUFTRAGSPOENALE = 17;
	private static final int OFFENE_AG_FERTIGUNGSZEIT = 18;
	private static final int OFFENE_AG_LOSSTATUS = 19;
	private static final int OFFENE_AG_LIEFERTERMIN = 20;
	private static final int OFFENE_AG_RAHMENRESERVIERUNGEN = 21;
	private static final int OFFENE_AG_DETAILBEDARF = 22;
	private static final int OFFENE_AG_LOSHATFEHLMENGE = 23;
	private static final int OFFENE_AG_AGNUMMER = 24;
	private static final int OFFENE_AG_UAGNUMMER = 25;
	private static final int OFFENE_AG_AG_ARTIKEL = 26;
	private static final int OFFENE_AG_AG_ARTIKELGRUPPPE = 27;
	private static final int OFFENE_AG_AG_BEGINN = 28;

	private static final int OFFENE_AG_AG_ARTIKELBEZEICHNUNG = 29;
	private static final int OFFENE_AG_AG_GESAMTZEIT = 30;
	private static final int OFFENE_AG_AG_MASCHINE_IDENTIFIKATIONSNUMMMER = 31;
	private static final int OFFENE_AG_AG_MASCHINE_BEZEICHNUNG = 32;
	private static final int OFFENE_AG_AG_MASCHINE_INVENTARNUMMMER = 33;
	private static final int OFFENE_AG_AG_ISTZEIT = 34;
	private static final int OFFENE_AG_FORTSCHRITT = 35;
	private static final int OFFENE_AG_LOSZUSATZSTATUS_P1 = 36;
	private final static int OFFENE_AG_SPALTENANZAHL = 37;

	private static final int AUSLIEFERLISTE_LOSNUMMER = 0;
	private static final int AUSLIEFERLISTE_ARTIKELNUMMER = 1;
	private static final int AUSLIEFERLISTE_BEZEICHNUNG = 2;
	private static final int AUSLIEFERLISTE_KALENDERWOCHE = 3;
	private static final int AUSLIEFERLISTE_AUFTRAGSNUMMER = 4;
	private static final int AUSLIEFERLISTE_LOSGROESSE = 5;
	private static final int AUSLIEFERLISTE_GELIEFERT = 6;
	private static final int AUSLIEFERLISTE_BEGINN = 7;
	private static final int AUSLIEFERLISTE_ENDE = 8;
	private static final int AUSLIEFERLISTE_MATERIAL_SUBREPORT = 9;
	private static final int AUSLIEFERLISTE_ZUSATZBEZEICHNUNG = 10;
	private static final int AUSLIEFERLISTE_KUNDE = 11;
	private static final int AUSLIEFERLISTE_PROJEKT = 12;
	private static final int AUSLIEFERLISTE_FERTIGUNGSGRUPPE = 13;
	private static final int AUSLIEFERLISTE_FEHLMENGE = 14;
	private static final int AUSLIEFERLISTE_RESERVIERUNGEN = 15;
	private static final int AUSLIEFERLISTE_RAHMEN_BESTELLT = 16;
	private static final int AUSLIEFERLISTE_AUFTRAGSPOENALE = 17;
	private static final int AUSLIEFERLISTE_FERTIGUNGSZEIT = 18;
	private static final int AUSLIEFERLISTE_LOSSTATUS = 19;
	private static final int AUSLIEFERLISTE_LIEFERTERMIN = 20;
	private static final int AUSLIEFERLISTE_RAHMENRESERVIERUNGEN = 21;
	private static final int AUSLIEFERLISTE_LOSHATFEHLMENGE = 22;
	private static final int AUSLIEFERLISTE_ARBEITSGAENGE = 23;
	private static final int AUSLIEFERLISTE_AUFTRAGSPOSITIONNUMMER = 24;
	private static final int AUSLIEFERLISTE_AUFTRAGARTIKEL = 25;
	private static final int AUSLIEFERLISTE_AUFTRAGARTIKELBEZEICHNUNG = 26;
	private static final int AUSLIEFERLISTE_AUFTRAGARTIKELGRUPPE = 27;
	private static final int AUSLIEFERLISTE_AUFTRAGARTIKELKLASSE = 28;
	private static final int AUSLIEFERLISTE_AUFTRAGPOSITIONSMENGE = 29;
	private static final int AUSLIEFERLISTE_AUFTRAGART = 30;
	private static final int AUSLIEFERLISTE_AUFTRAGSKUNDE_NAME1 = 31;
	private static final int AUSLIEFERLISTE_AUFTRAGSKUNDE_NAME2 = 32;
	private static final int AUSLIEFERLISTE_AUFTRAGSKUNDE_STRASSE = 33;
	private static final int AUSLIEFERLISTE_AUFTRAGSKUNDE_ORT = 34;
	private static final int AUSLIEFERLISTE_AUFTRAGSKUNDE_LKZ = 35;
	private static final int AUSLIEFERLISTE_AUFTRAGSKUNDE_PLZ = 36;
	private static final int AUSLIEFERLISTE_AUFTRAGSKUNDE_TELEFON = 37;
	private static final int AUSLIEFERLISTE_AUFTRAGSKUNDE_ANSPRECHPARTNER = 38;
	private static final int AUSLIEFERLISTE_AUFTRAGSKUNDE_ANSPRECHPARTNER_TELEFONDW = 39;
	private static final int AUSLIEFERLISTE_AUFTRAG_POSITIONSTERMIN = 40;
	private static final int AUSLIEFERLISTE_MATERIALLISTE = 41;
	private static final int AUSLIEFERLISTE_PROJEKTKOMMENTAR = 42;
	private static final int AUSLIEFERLISTE_LOSARTIKELGRUPPE = 43;
	private static final int AUSLIEFERLISTE_LOSARTIKELKLASSE = 44;
	private static final int AUSLIEFERLISTE_AUFTRAG_LIEFERADRESSE = 45;
	private static final int AUSLIEFERLISTE_AUFTRAG_LIEFERART = 46;
	private static final int AUSLIEFERLISTE_AUFTRAG_SPEDITEUR = 47;
	private static final int AUSLIEFERLISTE_ABLIEFERDATUM = 48;
	private static final int AUSLIEFERLISTE_AUFTRAG_OFFENERAHMENMENGE = 49;
	private static final int AUSLIEFERLISTE_LOSKLASSEN = 50;
	private static final int AUSLIEFERLISTE_ZUWENIG_AUF_LAGER_ZUM_ZEITPUNKT = 51;
	private static final int AUSLIEFERLISTE_AUFTRAG_OFFENEMENGE = 52;
	private static final int AUSLIEFERLISTE_LAGERSTAND = 53;
	private static final int AUSLIEFERLISTE_AUFTRAGARTIKEL_LAGERSTAND = 54;
	private static final int AUSLIEFERLISTE_ZUSATZBEZEICHNUNG2 = 55;
	private final static int AUSLIEFERLISTE_SPALTENANZAHL = 56;

	private static final int ABLIEF_LOSNUMMER = 0;
	private static final int ABLIEF_IDENT = 1;
	private static final int ABLIEF_BEZEICHNUNG = 2;
	private static final int ABLIEF_DATUM = 3;
	private static final int ABLIEF_MENGE = 4;
	private static final int ABLIEF_ABLIEFERPREIS = 5;
	private static final int ABLIEF_MATERIALWERT = 6;
	private static final int ABLIEF_ARBEITSZEITWERT = 7;
	private static final int ABLIEF_WERT = 8;
	private static final int ABLIEF_ZUSATZBEZEICHNUNG = 9;
	private static final int ABLIEF_AUFRAGSNUMMER = 10;
	private static final int ABLIEF_ABGANG_BELEGART = 11;
	private static final int ABLIEF_ABGANG_BELEGNUMMER = 12;
	private static final int ABLIEF_ANZAHL_SPALTEN = 13;

	private static final int ZE_SOLLZEIT = 0;
	private static final int ZE_ISTZEIT = 1;
	private static final int ZE_DATUM = 2;
	private static final int ZE_ARTIKELGRUPPE = 3;

	private static final int AV_SOLLZEITBEGINN = 0;
	private static final int AV_SOLLZEITENDE = 1;
	private static final int AV_DATUM = 2;
	private static final int AV_ARTIKELGRUPPE = 3;
	private static final int AV_PERSONALVERFUEGBARKEIT = 4;

	private static final int AV_DETAIL_ARTIKELNUMMER = 0;
	private static final int AV_DETAIL_BEZEICHNUNG = 1;
	private static final int AV_DETAIL_ARTIKELGRUPPE = 2;
	private static final int AV_DETAIL_ARBEITSGANG = 3;
	private static final int AV_DETAIL_UNTERARBEITSGANG = 4;
	private static final int AV_DETAIL_BEGINN_DATUM = 5;
	private static final int AV_DETAIL_DAUER = 6;
	private static final int AV_DETAIL_VERFUEGBARKEIT = 7;

	private static final int AV_DETAIL_LOSNUMMER = 8;
	private static final int AV_DETAIL_KUNDE = 9;
	private static final int AV_DETAIL_AUFTRAG = 10;
	private static final int AV_DETAIL_PROJEKT = 11;
	private static final int AV_DETAIL_KOMMENTAR = 12;
	private static final int AV_DETAIL_SORT_VERFUEGBARKEIT = 13;
	private static final int AV_DETAIL_ANZAHL_SPALTEN = 14;

	private final static int AFM_BELEGNUMMER = 0;
	private final static int AFM_PROJEKT = 1;
	private final static int AFM_STUECKLISTENUMMER = 2;
	private final static int AFM_STUECKLISTEBEZEICHNUNG = 3;
	private final static int AFM_ARTIKELNUMMER = 4;
	private final static int AFM_ARTIKELBEZEICHNUNG = 5;
	private final static int AFM_FEHLMENGE = 6;
	private final static int AFM_LAGERSTAND = 7;
	private final static int AFM_TERMIN = 8;
	private final static int AFM_EINHEIT = 9;
	private final static int AFM_BESTELLT = 10;
	private final static int AFM_STUECKLISTEZUSATZBEZEICHNUNG = 11;
	private final static int AFM_ARTIKELZUSATZBEZEICHNUNG = 12;
	private final static int AFM_RESERVIERT = 14;
	private final static int AFM_OFFEN = 15;
	private final static int AFM_ABNUMMER = 16;
	private final static int AFM_MAHNSTUFE = 17;
	private final static int AFM_FIKTIVERLAGERSTAND = 18;
	private final static int AFM_SPALTENANZAHL = 19;

	private final static int FMAL_BELEGNUMMER = 0;
	private final static int FMAL_BELEGART = 1;
	private final static int FMAL_STUECKLISTENUMMER = 2;
	private final static int FMAL_STUECKLISTEBEZEICHNUNG = 3;
	private final static int FMAL_ARTIKELNUMMER = 4;
	private final static int FMAL_ARTIKELBEZEICHNUNG = 5;
	private final static int FMAL_FEHLMENGE = 6;
	private final static int FMAL_LAGERSTAND = 7;
	private final static int FMAL_TERMIN = 8;
	private final static int FMAL_PRODUKTIONSBEGINN = 9;
	private final static int FMAL_PRODUKTIONSENDE = 10;
	private final static int FMAL_EINHEIT = 11;
	private final static int FMAL_BESTELLT = 12;
	private final static int FMAL_STUECKLISTEZUSATZBEZEICHNUNG = 13;
	private final static int FMAL_ARTIKELZUSATZBEZEICHNUNG = 14;
	private final static int FMAL_RESERVIERT = 15;
	private final static int FMAL_OFFEN = 16;
	private final static int FMAL_ABNUMMER = 17;
	private final static int FMAL_MAHNSTUFE = 18;
	private final static int FMAL_FIKTIVERLAGERSTAND = 19;
	private final static int FMAL_ARTIKEL_I_ID = 20;
	private final static int FMAL_ABTERMIN = 21;
	private final static int FMAL_LIEFERANT = 22;
	private final static int FMAL_ARTIKELSPERREN = 23;
	private final static int FMAL_POENALE_AUS_AUFTRAG = 24;
	private final static int FMAL_DRINGEND = 25;
	private final static int FMAL_STUECKLISTEREFERENZNUMMER = 26;
	private final static int FMAL_ARTIKELREFERENZNUMMER = 27;
	private static final int FMAL_ARTIKEL_ANZAHL_ANGEFRAGT = 28;
	private static final int FMAL_ARTIKEL_ANZAHL_ANGEBOTEN = 29;
	private final static int FMAL_SPALTENANZAHL = 30;

	private final static int SRM_ARBEITSGANG = 0;
	private final static int SRM_IDENT = 1;
	private final static int SRM_BEZEICHNUNG = 2;
	private final static int SRM_ZUSATZBEZEICHNUNG = 3;
	private final static int SRM_GEBUCHT = 4;
	private final static int SRM_PERSONAL = 5;
	private final static int SRM_GUT = 6;
	private final static int SRM_SCHLECHT = 7;
	private final static int SRM_INARBEIT = 8;
	private final static int SRM_OFFEN = 8;
	private final static int SRM_MASCHINE = 10;
	private final static int SRM_SPALTENANZAHL = 11;

	private final static int LOSZEITEN_ARTIKELNUMMER = 0;
	private final static int LOSZEITEN_BEZEICHNUNG = 1;
	private final static int LOSZEITEN_ZUSATZBEZEICHNUNG = 2;
	private final static int LOSZEITEN_PERSON_MASCHINE = 3;
	private final static int LOSZEITEN_PERSONALNUMMER_MASCHINENINVENARNUMMER = 4;
	private final static int LOSZEITEN_BEGINN = 5;
	private final static int LOSZEITEN_ENDE = 6;
	private final static int LOSZEITEN_DAUER = 7;
	private final static int LOSZEITEN_BEMERKUNG = 8;
	private final static int LOSZEITEN_BEMERKUNG_LANG = 9;
	private final static int LOSZEITEN_MASCHINENGRUPPE = 10;
	private final static int LOSZEITEN_ARBEITSGANG = 11;
	private final static int LOSZEITEN_UNTERARBEITSGANG = 12;
	private final static int LOSZEITEN_ZEITDATEN_I_ID = 13;
	private final static int LOSZEITEN_SPALTENANZAHL = 14;

	private final static int BZ_ARTIKELNUMMER = 0;
	private final static int BZ_BEZEICHNUNG = 1;
	private final static int BZ_ZUSATZBEZEICHNUNG = 2;
	private final static int BZ_KURZBEZEICHNUNG = 3;
	private final static int BZ_REVISION = 4;
	private final static int BZ_INDEX = 5;
	private final static int BZ_ARTIKELGRUPPE = 6;
	private final static int BZ_ARTIKELKLASSE = 7;
	private final static int BZ_LAGERSOLL = 8;
	private final static int BZ_LAGERMINDEST = 9;
	private final static int BZ_JAHRESMENGE_GEPLANT = 10;
	private final static int BZ_FERTIGUNGSSATZGROESSE = 11;
	private final static int BZ_MONAT_VERGANGENHEIT_1 = 12;
	private final static int BZ_MONAT_VERGANGENHEIT_2 = 13;
	private final static int BZ_MONAT_VERGANGENHEIT_3 = 14;
	private final static int BZ_MONAT_VERGANGENHEIT_6 = 15;
	private final static int BZ_MONAT_VERGANGENHEIT_9 = 16;
	private final static int BZ_MONAT_VERGANGENHEIT_12 = 17;
	private final static int BZ_MONAT_ZUKUNFT_1 = 18;
	private final static int BZ_MONAT_ZUKUNFT_2 = 19;
	private final static int BZ_MONAT_ZUKUNFT_3 = 20;
	private final static int BZ_MONAT_ZUKUNFT_6 = 21;
	private final static int BZ_MONAT_ZUKUNFT_9 = 22;
	private final static int BZ_MONAT_ZUKUNFT_12 = 23;
	private final static int BZ_MONAT_ZUKUNFT_DANACH = 24;
	private final static int BZ_IN_FERTIGUNG = 25;
	private final static int BZ_RAHMENRESERVIERT = 26;
	private final static int BZ_BESTELLT = 27;
	private final static int BZ_RAHMENBESTELLT = 28;
	private final static int BZ_KUNDE = 29;
	private final static int BZ_VKPREIS = 30;
	private final static int BZ_GESTPREIS = 31;
	private final static int BZ_SUBREPORT_ARTIKELKOMMENTAR = 32;
	private final static int BZ_SUMME_MENGE_INTERNEBESTELLUNG = 33;
	private final static int BZ_LAGERSTAND = 34;
	private final static int BZ_SPALTENANZAHL = 35;

	private final static int FEHLERSTATISTIK_LOSNUMMER = 0;
	private final static int FEHLERSTATISTIK_STUECKLISTE = 1;
	private final static int FEHLERSTATISTIK_STUECKLISTEBEZEICHNUNG = 2;
	private final static int FEHLERSTATISTIK_PROJEKT = 3;
	private final static int FEHLERSTATISTIK_LOSGROESSE = 4;
	private final static int FEHLERSTATISTIK_MENGE_ERLEDIGT = 5;
	private final static int FEHLERSTATISTIK_ERLEDIGTDATUM = 6;
	private final static int FEHLERSTATISTIK_AG_AGNUMMER = 7;
	private final static int FEHLERSTATISTIK_AG_UAGNUMMER = 8;
	private final static int FEHLERSTATISTIK_AG_ARTIKELNUMMER = 9;
	private final static int FEHLERSTATISTIK_AG_ARTIKELBEZEICHNUNG = 10;
	private final static int FEHLERSTATISTIK_GUT = 11;
	private final static int FEHLERSTATISTIK_SCHLECHT = 12;
	private final static int FEHLERSTATISTIK_INARBEIT = 13;
	private final static int FEHLERSTATISTIK_FEHLER = 14;
	private final static int FEHLERSTATISTIK_FEHLERSPR = 15;
	private final static int FEHLERSTATISTIK_FEHLER_KOMMENTAR = 16;
	private final static int FEHLERSTATISTIK_PERSONAL = 17;
	private final static int FEHLERSTATISTIK_ZEITPUNKT = 18;
	private final static int FEHLERSTATISTIK_SPALTENANZAHL = 19;

	private final static int LOSSTATISTIK_ARTIKELNUMMER = 0;
	private final static int LOSSTATISTIK_ARTIKELBEZEICHNUNG = 1;
	private final static int LOSSTATISTIK_PERSONALMASCHINE = 2;
	private final static int LOSSTATISTIK_SOLLMENGE = 3;
	private final static int LOSSTATISTIK_SOLLPREIS = 4;
	private final static int LOSSTATISTIK_ISTMENGE = 5;
	private final static int LOSSTATISTIK_ISTPREIS = 6;
	private final static int LOSSTATISTIK_GRUPPIERUNG = 7;
	private final static int LOSSTATISTIK_GRUPPIERUNGBEZEICHNUNG = 8;
	private final static int LOSSTATISTIK_UNTERGRUPPEMATERIAL = 9;
	private final static int LOSSTATISTIK_GRUPPIERUNGSTKLARTIKEL = 10;
	private final static int LOSSTATISTIK_GRUPPIERUNGKUNDE = 11;
	private final static int LOSSTATISTIK_GRUPPIERUNGAUSGABE = 12;
	private final static int LOSSTATISTIK_GRUPPIERUNGERLEDIGT = 13;
	private final static int LOSSTATISTIK_ISTPERSON = 14;
	private final static int LOSSTATISTIK_GRUPPIERUNVKPREIS = 15;
	private final static int LOSSTATISTIK_GRUPPIERUNABGELIEFERTEMENGE = 16;
	private final static int LOSSTATISTIK_GRUPPIERUNGLOSGROESSE = 17;
	private final static int LOSSTATISTIK_BUCHUNGSZEIT = 18;
	private final static int LOSSTATISTIK_GEPLANTESSOLLMATERIAL = 19;
	private final static int LOSSTATISTIK_BEWERTUNG = 20;
	private final static int LOSSTATISTIK_GRUPPIERUNGSTKLARTIKELREFERENZNUMMER = 21;
	private final static int LOSSTATISTIK_ERLEDIGUNGSDATUM = 22;

	private static final int MONATSAUSWERTUNG_KUNDE = 0;
	private static final int MONATSAUSWERTUNG_ARTIKELNUMMER = 1;
	private static final int MONATSAUSWERTUNG_BEZEICHNUNG = 2;
	private static final int MONATSAUSWERTUNG_SOLLZEIT = 3;
	private static final int MONATSAUSWERTUNG_ISTZEIT = 4;
	private static final int MONATSAUSWERTUNG_LOSGROESSE = 5;
	private static final int MONATSAUSWERTUNG_ABWEICHUNG = 6;
	private static final int MONATSAUSWERTUNG_FERTIGUNGSGRUPPE = 7;
	private static final int MONATSAUSWERTUNG_LOSNUMMER = 8;
	private static final int MONATSAUSWERTUNG_BEWERTUNG = 9;
	private static final int MONATSAUSWERTUNG_ANZAHL_FELDER = 10;

	private static final int RANKINGLISTE_FERTIGUNGSGRUPPE = 0;
	private static final int RANKINGLISTE_INFO_U = 1;
	private static final int RANKINGLISTE_INFO_L = 2;
	private static final int RANKINGLISTE_INFO_K = 3;
	private static final int RANKINGLISTE_ARTIKELNUMMER = 4;
	private static final int RANKINGLISTE_ARTIKELBEZEICHNUNG = 5;
	private static final int RANKINGLISTE_LOSNR = 6;
	private static final int RANKINGLISTE_KUNDE = 7;
	private static final int RANKINGLISTE_SOLLZEIT = 8;
	private static final int RANKINGLISTE_OFFENELOSMENGE = 9;
	private static final int RANKINGLISTE_LOSBEGINNTERMIN = 10;

	private final static int NACHKALKULATION_ARTIKELNUMMER = 0;
	private final static int NACHKALKULATION_ARTIKELBEZEICHNUNG = 1;
	private final static int NACHKALKULATION_PERSONALSOLL = 2;
	private final static int NACHKALKULATION_PERSONALIST = 3;
	private final static int NACHKALKULATION_PERSONALDIFF = 4;
	private final static int NACHKALKULATION_MASCHINESOLL = 5;
	private final static int NACHKALKULATION_MASCHINEIST = 6;
	private final static int NACHKALKULATION_MASCHINEDIFF = 7;
	private final static int NACHKALKULATION_GESAMTSOLL = 8;
	private final static int NACHKALKULATION_GESAMTIST = 9;
	private final static int NACHKALKULATION_FERTIG = 10;
	private final static int NACHKALKULATION_HOECHSTER_ARBEITSGANG = 11;
	private final static int NACHKALKULATION_ANZAHL_SPALTEN = 12;

	private static final int ETI_ARTIKELNUMMER = 0;
	private static final int ETI_BEZEICHNUNG = 1;
	private static final int ETI_KURZBEZEICHNUNG = 2;
	private static final int ETI_ZUSATZBEZEICHNUNG = 3;
	private static final int ETI_ZUSATZBEZEICHNUNG2 = 4;
	private static final int ETI_REFERENZNUMMER = 5;
	private static final int ETI_SOLLMENGE = 6;
	private static final int ETI_ISTMENGE = 7;
	private static final int ETI_EINHEIT = 8;
	private final static int ETI_ANZAHL_FELDER = 9;

	private static final int ETI_A4_SOLLMATERIAL_ARTIKELNUMMER = 0;
	private static final int ETI_A4_SOLLMATERIAL_BEZEICHNUNG = 1;
	private static final int ETI_A4_SOLLMATERIAL_KURZBEZEICHNUNG = 2;
	private static final int ETI_A4_SOLLMATERIAL_ZUSATZBEZEICHNUNG = 3;
	private static final int ETI_A4_SOLLMATERIAL_ZUSATZBEZEICHNUNG2 = 4;
	private static final int ETI_A4_SOLLMATERIAL_REFERENZNUMMER = 5;
	private static final int ETI_A4_SOLLMATERIAL_SOLLMENGE = 6;
	private static final int ETI_A4_SOLLMATERIAL_ISTMENGE = 7;
	private static final int ETI_A4_SOLLMATERIAL_EINHEIT = 8;
	private static final int ETI_A4_LOSNUMMER = 9;
	private static final int ETI_A4_AUFTRAG = 10;
	private static final int ETI_A4_ROHS = 11;
	private static final int ETI_A4_KUNDE_ANREDE = 12;
	private static final int ETI_A4_KUNDE_ABTEILUNG = 13;
	private static final int ETI_A4_KUNDE_LANDPLZORT = 14;
	private static final int ETI_A4_AUFTRAGSPOSITION = 15;
	private static final int ETI_A4_KOSTENSTELLENNUMMER = 16;
	private static final int ETI_A4_KOSTENSTELLENBEZEICHNUNG = 17;
	private static final int ETI_A4_LOSART = 18;
	private static final int ETI_A4_STUECKLISTENNUMMER = 19;
	private static final int ETI_A4_STUECKLISTENBEZEICHNUNG = 20;
	private static final int ETI_A4_STUECKLISTEZUSATZBEZEICHNUNG = 21;
	private static final int ETI_A4_STUECKLISTEKURZBEZEICHNUNG = 22;
	private static final int ETI_A4_STUECKLISTEREFERENZNUMMER = 23;
	private static final int ETI_A4_STUECKLISTEMENGENEINHEIT = 24;
	private static final int ETI_A4_LOSGROESSE = 25;
	private static final int ETI_A4_LAGER = 26;
	private static final int ETI_A4_BEGINN = 27;
	private static final int ETI_A4_ENDE = 28;
	private static final int ETI_A4_DAUER = 29;
	private static final int ETI_A4_TECHNIKERNUMMER = 30;
	private static final int ETI_A4_TECHNIKERNAME = 31;
	private static final int ETI_A4_PROJEKT = 32;
	private static final int ETI_A4_KOMMENTAR = 33;
	private static final int ETI_A4_FERTIGUNGSGRUPPE = 34;
	private static final int ETI_A4_FERTIGUNGSORT = 35;
	private static final int ETI_A4_TEXT = 36;
	private static final int ETI_A4_EXEMPLAR = 37;
	private static final int ETI_A4_AUFTRAGPOSITION_LIEFERTERMIN = 38;
	private static final int ETI_A4_AUFTRAG_LIEFERTERMIN = 39;
	private static final int ETI_A4_STUECKLISTEZUSATZBEZEICHNUNG2 = 40;
	private final static int ETI_A4_ANZAHL_FELDER = 41;

	private static int PRUEFPLAN_ARTIKEL_KONTAKT = 0;
	private static int PRUEFPLAN_ARTIKELBEZEICHNUNG_KONTAKT = 1;
	private static int PRUEFPLAN_POSITION_KONTAKT = 2;
	private static int PRUEFPLAN_ARTIKEL_LITZE = 3;
	private static int PRUEFPLAN_ARTIKELBEZEICHNUNG_LITZE = 4;
	private static int PRUEFPLAN_POSITION_LITZE = 5;
	private static int PRUEFPLAN_WERKZEUGNUMMER = 6;
	private static int PRUEFPLAN_WERKZEUGBEZEICHNUNG = 7;
	private static int PRUEFPLAN_VERSCHLEISSTEILNUMMER = 8;
	private static int PRUEFPLAN_VERSCHLEISSTEILBEZEICHNUNG = 9;
	private static int PRUEFPLAN_CRIMPHOEHE_DRAHT = 10;
	private static int PRUEFPLAN_CRIMPHOEHE_ISOLATION = 11;
	private static int PRUEFPLAN_CRIMPBREITE_DRAHT = 12;
	private static int PRUEFPLAN_CRIMPBREITE_ISOLATION = 13;
	private static int PRUEFPLAN_ANZAHL_VERWENDUNGEN_VERSCHLEISSTEIL = 14;

	private static int PRUEFPLAN_PRUEFART = 15;
	private static int PRUEFPLAN_PRUEFART_BEZEICHNUNG = 16;
	private static int PRUEFPLAN_TOLERANZ_CRIMPHOEHE_DRAHT = 17;
	private static int PRUEFPLAN_TOLERANZ_CRIMPHOEHE_ISOLATION = 18;
	private static int PRUEFPLAN_TOLERANZ_CRIMPBREITE_DRAHT = 19;
	private static int PRUEFPLAN_TOLERANZ_CRIMPBREITE_ISOLATION = 20;
	private static int PRUEFPLAN_TOLERANZ_WERT = 21;
	private static int PRUEFPLAN_WERT = 22;
	private static int PRUEFPLAN_MENGE_LITZE = 23;
	private static int PRUEFPLAN_EINHEIT_LITZE = 24;
	private static int PRUEFPLAN_DIMENSION1_LITZE = 25;
	private static int PRUEFPLAN_DIMENSION2_LITZE = 26;
	private static int PRUEFPLAN_DIMENSION3_LITZE = 27;

	private static int PRUEFPLAN_ARTIKEL_LITZE2 = 28;
	private static int PRUEFPLAN_ARTIKELBEZEICHNUNG_LITZE2 = 29;
	private static int PRUEFPLAN_POSITION_LITZE2 = 30;
	private static int PRUEFPLAN_MENGE_LITZE2 = 31;
	private static int PRUEFPLAN_EINHEIT_LITZE2 = 32;
	private static int PRUEFPLAN_DIMENSION1_LITZE2 = 33;
	private static int PRUEFPLAN_DIMENSION2_LITZE2 = 34;
	private static int PRUEFPLAN_DIMENSION3_LITZE2 = 35;
	private static int PRUEFPLAN_ABZUGSKRAFT_LITZE = 36;
	private static int PRUEFPLAN_ABZUGSKRAFT_LITZE2 = 37;
	private static int PRUEFPLAN_DOPPELANSCHLAG = 38;
	private static int PRUEFPLAN_KOMMENTAR = 39;
	private static int PRUEFPLAN_SUBREPORT_MOEGLICHE_WERKZEUGE = 40;
	private static int PRUEFPLAN_ANZAHL_SPALTEN = 41;

	private static int TRACEIMPORT_STUECKLISTE = 0;
	private static int TRACEIMPORT_STUECKLISTE_BEZEICHNUNG = 1;
	private static int TRACEIMPORT_ARTIKEL_MATERIAL = 2;
	private static int TRACEIMPORT_CHARGENNUMMER = 3;
	private static int TRACEIMPORT_MENGE_AUS_IMPORTDATEI = 4;
	private static int TRACEIMPORT_FEHLER = 5;
	private static int TRACEIMPORT_KBEZ_STKL_AUS_IMPORTDATEI = 6;
	private static int TRACEIMPORT_SUBREPORT_ZEILEN_AUS_DATEI = 7;
	private static int TRACEIMPORT_LOSNUMMER = 8;
	private static int TRACEIMPORT_LOSGROESSE = 9;
	private static int TRACEIMPORT_LOSABGELIEFERT = 10;
	private static int TRACEIMPORT_LAGERSTAND = 11;
	private static int TRACEIMPORT_ANZAHL_SPALTEN = 12;

	private static int PRUEFERGEBNIS_LOSNUMMMER = 0;
	private static int PRUEFERGEBNIS_BEGINN = 1;
	private static int PRUEFERGEBNIS_ENDE = 2;
	private static int PRUEFERGEBNIS_ABLIEFERZEITPUNKT = 3;
	private static int PRUEFERGEBNIS_STUECKLISTENUMMER = 4;
	private static int PRUEFERGEBNIS_STUECKLISTEBEZEICHNUNG = 5;
	private static int PRUEFERGEBNIS_ARTIKEL_KONTAKT = 6;
	private static int PRUEFERGEBNIS_ARTIKELBEZEICHNUNG_KONTAKT = 7;
	private static int PRUEFERGEBNIS_ARTIKEL_LITZE = 8;
	private static int PRUEFERGEBNIS_ARTIKELBEZEICHNUNG_LITZE = 9;
	private static int PRUEFERGEBNIS_WERKZEUGNUMMER = 10;
	private static int PRUEFERGEBNIS_WERKZEUGBEZEICHNUNG = 11;
	private static int PRUEFERGEBNIS_VERSCHLEISSTEILNUMMER = 12;
	private static int PRUEFERGEBNIS_VERSCHLEISSTEILBEZEICHNUNG = 13;
	private static int PRUEFERGEBNIS_SOLL_CRIMPHOEHE_DRAHT = 14;
	private static int PRUEFERGEBNIS_SOLL_CRIMPHOEHE_ISOLATION = 15;
	private static int PRUEFERGEBNIS_SOLL_CRIMPBREITE_DRAHT = 16;
	private static int PRUEFERGEBNIS_SOLL_CRIMPBREITE_ISOLATION = 17;
	private static int PRUEFERGEBNIS_IST_CRIMPHOEHE_DRAHT = 18;
	private static int PRUEFERGEBNIS_IST_CRIMPHOEHE_ISOLATION = 19;
	private static int PRUEFERGEBNIS_IST_CRIMPBREITE_DRAHT = 20;
	private static int PRUEFERGEBNIS_IST_CRIMPBREITE_ISOLATION = 21;
	private static int PRUEFERGEBNIS_ABLIEFERMENGE = 22;

	private static int PRUEFERGEBNIS_PRUEFART = 23;
	private static int PRUEFERGEBNIS_PRUEFART_BEZEICHNUNG = 24;
	private static int PRUEFERGEBNIS_SOLL_TOLERANZ_CRIMPHOEHE_DRAHT = 25;
	private static int PRUEFERGEBNIS_SOLL_TOLERANZ_CRIMPHOEHE_ISOLATION = 26;
	private static int PRUEFERGEBNIS_SOLL_TOLERANZ_CRIMPBREITE_DRAHT = 27;
	private static int PRUEFERGEBNIS_SOLL_TOLERANZ_CRIMPBREITE_ISOLATION = 28;
	private static int PRUEFERGEBNIS_SOLL_TOLERANZ_WERT = 29;
	private static int PRUEFERGEBNIS_SOLL_WERT = 30;
	private static int PRUEFERGEBNIS_IST_WERT = 31;
	private static int PRUEFERGEBNIS_IST_WERT_BOOLEAN = 32;

	private static int PRUEFERGEBNIS_KOMMENTAR_PRUEFKOMBINATION = 33;
	private static int PRUEFERGEBNIS_POSITION_KONTAT = 34;
	private static int PRUEFERGEBNIS_POSITION_LITZE = 35;

	private static int PRUEFERGEBNIS_ARTIKEL_LITZE2 = 36;
	private static int PRUEFERGEBNIS_ARTIKELBEZEICHNUNG_LITZE2 = 37;
	private static int PRUEFERGEBNIS_POSITION_LITZE2 = 38;

	private static int PRUEFERGEBNIS_ABZUGSKRAFT_LITZE = 39;
	private static int PRUEFERGEBNIS_ABZUGSKRAFT_LITZE2 = 40;

	private static int PRUEFERGEBNIS_ANZAHL_SPALTEN = 41;

	private static final int PI_BILD = 0;
	private static final int PI_SCHLAGWORTE = 1;
	private static final int PI_FILENAME = 2;
	private static final int PI_NAME = 3;
	private final static int PI_ANZAHL_FELDER = 4;

	private static final int FT_STUECKLISTENART = 0;
	private static final int FT_ARTIKELNUMMER = 1;
	private static final int FT_BEZEICHNUNG = 2;
	private static final int FT_SOLLMENGE = 3;
	private static final int FT_AUSGEGEBEN = 4;
	private static final int FT_LAGERSTAND = 5;
	private static final int FT_EBENE = 6;
	private static final int FT_LOSNUMMER = 7;
	private static final int FT_FEHLMENGE = 8;
	private static final int FT_LOSGROESSE = 9;
	private static final int FT_ABGELIEFERT = 10;
	private static final int FT_LOSSTATUS = 11;
	private static final int FT_FRUEHESTER_EINTREFFTERMIN = 12;
	private static final int FT_LAGERBEWIRTSCHAFTET = 13;
	private static final int FT_ERSATZTYP = 14;
	private static final int FT_KURZBEZEICHNUNG = 15;
	private static final int FT_REFERENZNUMMER = 16;
	private static final int FT_SUBREPORT_FRUEHESTER_EINTREFFTERMIN_BS = 17;
	private static final int FT_SUBREPORT_FRUEHESTER_EINTREFFTERMIN_LO = 18;
	private final static int FT_ANZAHL_SPALTEN = 19;

	private static final int AZSTATUS_LOSNUMMER = 0;
	private static final int AZSTATUS_STUECKLISTE = 1;
	private static final int AZSTATUS_STUECKLISTEBEZEICHNUNG = 2;
	private static final int AZSTATUS_LOSGROESSE = 3;
	private static final int AZSTATUS_TECHNIKER = 4;
	private static final int AZSTATUS_PROJEKT = 5;
	private static final int AZSTATUS_ARBEITSGANG = 6;
	private static final int AZSTATUS_UNTERARBEITSGANG = 7;
	private static final int AZSTATUS_TAETIGKEIT = 8;
	private static final int AZSTATUS_TAETIGKEIT_BEZEICHNUNG = 9;
	private static final int AZSTATUS_SOLL_PERSON = 10;
	private static final int AZSTATUS_IST_PERSON = 11;
	private static final int AZSTATUS_FORTSCHRITT = 12;
	private static final int AZSTATUS_FERTIG = 13;
	private final static int AZSTATUS_ANZAHL_SPALTEN = 14;

	private static final int ABLIEFERETIKETT_SERIENNUMMER = 0;
	private static final int ABLIEFERETIKETT_SUBREPORT_GERAETESNR = 1;
	private static final int ABLIEFERETIKETT_VERSION = 2;
	private static final int ABLIEFERETIKETT_I_ID_BUCHUNG = 3;
	private static final int ABLIEFERETIKETT_MENGE = 4;
	private static final int ABLIEFERETIKETT_EXEMPLAR = 5;
	private final static int ABLIEFERETIKETT_ANZAHL_SPALTEN = 6;

	public boolean next() throws JRException {
		index++;
		return (index < data.length);
	}

	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;
		String fieldName = field.getName();
		switch (useCase) {
		case UC_THEORETISCHE_FEHLMENGEN: {
			if ("F_IDENT".equals(fieldName)) {
				value = data[index][TF_IDENT];
			} else if ("F_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][TF_BEZEICHNUNG];
			} else if ("F_SOLLMENGE".equals(fieldName)) {
				value = data[index][TF_SOLLMENGE];
			} else if ("F_ISTMENGE".equals(fieldName)) {
				value = data[index][TF_ISTMENGE];
			} else if ("F_VERFUEGBAR".equals(fieldName)) {
				value = data[index][TF_VERFUEGBAR];
			} else if ("F_FEHLMENGE".equals(fieldName)) {
				value = data[index][TF_FEHLMENGE];
			} else if ("F_BESTELLT".equals(fieldName)) {
				value = data[index][TF_BESTELLT];
			} else if ("F_BESTELLTZUMTERMIN".equals(fieldName)) {
				value = data[index][TF_BESTELLTZUMPRODUKTIONSSTART];
			} else if ("F_IN_FERTIGUNG".equals(fieldName)) {
				value = data[index][TF_IN_FERTIGUNG];
			} else if ("F_MOEGLICH".equals(fieldName)) {
				value = data[index][TF_MOEGLICH];
			} else if ("F_LAGERSTAND".equals(fieldName)) {
				value = data[index][TF_LAGERSTAND];
			} else if (F_ZUSATZBEZEICHNUNG.equals(fieldName)) {
				value = data[index][TF_ZUSATZBEZEICHNUNG];
			} else if ("F_VERFUEGBARZUMTERMIN".equals(fieldName)) {
				value = data[index][TF_VERFUEGBARZUMPRODUKTIONSSTART];
			} else if ("F_ARTIKELSPERREN".equals(fieldName)) {
				value = data[index][TF_ARTIKELSPERREN];
			} else if ("F_ERSATZIDENT".equals(fieldName)) {
				value = data[index][TF_ERSATZIDENT];
			} else if ("F_ERSATZBEZEICHNUNG".equals(fieldName)) {
				value = data[index][TF_ERSATZBEZEICHNUNG];
			} else if ("F_LAGERBEWIRTSCHAFTET".equals(fieldName)) {
				value = data[index][TF_LAGERBEWIRTSCHAFTET];
			}

			else if ("F_ANGELEGT".equals(fieldName)) {
				value = data[index][TF_ANGELEGT];
			} else if ("F_AUFTRAGNUMMER".equals(fieldName)) {
				value = data[index][TF_AUFTRAGNUMMER];
			} else if ("F_KOSTENSTELLENUMMER".equals(fieldName)) {
				value = data[index][TF_KOSTENSTELLENUMMER];
			} else if ("F_KUNDE".equals(fieldName)) {
				value = data[index][TF_KUNDE];
			} else if ("F_LOSGROESSE".equals(fieldName)) {
				value = data[index][TF_LOSGROESSE];
			} else if ("F_LOSNUMMER".equals(fieldName)) {
				value = data[index][TF_LOSNUMMER];
			} else if ("F_PRODUKTIONSBEGINN".equals(fieldName)) {
				value = data[index][TF_PRODUKTIONSBEGINN];
			} else if ("F_STUECKLISTEBEZEICHNUNG".equals(fieldName)) {
				value = data[index][TF_STUECKLISTEBEZEICHNUNG];
			} else if ("F_STUECKLISTEZUSATZBEZEICHNUNG".equals(fieldName)) {
				value = data[index][TF_STUECKLISTEZUSATZBEZEICHNUNG];
			} else if ("F_STUECKLISTENUMMER".equals(fieldName)) {
				value = data[index][TF_STUECKLISTENUMMER];
			} else if ("F_MENGENEINHEIT".equals(fieldName)) {
				value = data[index][TF_MENGENEINHEIT];
			} else if ("F_STUECKLISTEARTIKELKOMMENTAR".equals(fieldName)) {
				value = data[index][TF_STUECKLISTEARTIKELKOMMENTAR];
			} else if ("F_STATUS".equals(fieldName)) {
				value = data[index][TF_STATUS];
			} else if ("F_SUBREPORT_BILDER".equals(fieldName)) {
				value = data[index][TF_SUBREPORT_BILDER];
			} else if ("F_I_SORT_AUS_STUECKLISTE".equals(fieldName)) {
				value = data[index][TF_I_SORT_AUS_STUECKLISTE];
			} else if ("F_KURZBEZEICHNUNG".equals(fieldName)) {
				value = data[index][TF_KURZBEZEICHNUNG];
			} else if ("F_REFERENZNUMMER".equals(fieldName)) {
				value = data[index][TF_REFERENZNUMMER];
			} else if ("F_STUECKLISTEKURZBEZEICHNUNG".equals(fieldName)) {
				value = data[index][TF_STUECKLISTEKURZBEZEICHNUNG];
			} else if ("F_STUECKLISTEREFERENZNUMMER".equals(fieldName)) {
				value = data[index][TF_STUECKLISTEREFERENZNUMMER];
			} else if ("ArtikelAnzahlAngeboten".equals(fieldName)) {
				value = data[index][TF_ARTIKEL_ANZAHL_ANGEBOTEN];
			} else if ("ArtikelAnzahlAngefragt".equals(fieldName)) {
				value = data[index][TF_ARTIKEL_ANZAHL_ANGEFRAGT];
			}

		}
			break;
		case UC_GESAMTKALKULATION: {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_ARTIKELBEZEICHNUNG];
			} else if ("ChnrBehaftet".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_CHNRBEHAFTET];
			} else if ("SnrBehaftet".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_SNRBEHAFTET];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_ZUSATZBEZEICHNUNG];
			} else if ("Sollmenge".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_SOLLMENGE];
			} else if ("Istmenge".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_ISTMENGE];
			} else if ("Lief1Preis".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_LIEF1PREIS];
			} else if ("SnrChnr".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_SNR_CHNR];
			} else if ("Sollpreis".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_SOLLPREIS];
			} else if ("Istpreis".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_ISTPREIS];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_EINHEIT];
			} else if ("Ebene".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_EBENE];
			} else if ("VerbrauchteMenge".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_VERBRAUCHTE_MENGE];
			} else if ("BelegartZugang".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_BELEGART_ZUGANG];
			} else if ("BelegnummerZugang".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_BELEGNUMMER_ZUGANG];
			} else if ("BelegstatusZugang".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_BELEGSTATUS_ZUGANG];
			} else if ("Losgroesse".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_LOSGROESSE];
			} else if ("Arbeitszeit".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_ARBEITSZEIT];
			} else if ("Mengenfaktor".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_MENGENFAKTOR];
			} else if ("Einstandspreis".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_EINSTANDSPREIS];
			} else if ("Maschine".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_MASCHINE];
			} else if ("BelegartpositionIIdZugang".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_BELEGARTPOSITION_I_ID_ZUGANG];
			}

		}
			break;
		case UC_MATERIALLISTE: {
			if ("F_ART".equals(fieldName)) {
				value = data[index][MATERIAL_ART];
			} else if ("F_ARTIKELNUMMER".equals(fieldName)) {
				value = data[index][MATERIAL_ARTIKELNUMMER];
			} else if ("F_ORIGINALARTIKELNUMMER".equals(fieldName)) {
				value = data[index][MATERIAL_ORIGINALARTIKELNUMMER];
			} else if ("F_REFERENZNUMMER".equals(fieldName)) {
				value = data[index][MATERIAL_REFERENZNUMMER];
			} else if ("F_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][MATERIAL_BEZEICHNUNG];
			} else if ("F_ZUSATZBEZEICHNUNG".equals(fieldName)) {
				value = data[index][MATERIAL_ZUSATZBEZEICHNUNG];
			} else if ("F_ZUSATZBEZEICHNUNG2".equals(fieldName)) {
				value = data[index][MATERIAL_ZUSATZBEZEICHNUNG2];
			} else if ("F_KURZBEZEICHNUNG".equals(fieldName)) {
				value = data[index][MATERIAL_KURZBEZEICHNUNG];
			} else if ("F_SOLLMENGE".equals(fieldName)) {
				value = data[index][MATERIAL_SOLLMENGE];
			} else if ("F_KOMMENTAR".equals(fieldName)) {
				value = data[index][MATERIAL_KOMMENTAR];
			} else if ("F_EINHEIT".equals(fieldName)) {
				value = data[index][MATERIAL_EINHEIT];
			} else if ("F_LAGERORT".equals(fieldName)) {
				value = data[index][MATERIAL_LAGERORT];
			} else if ("F_ISTMENGE".equals(fieldName)) {
				value = data[index][MATERIAL_ISTMENGE];
			} else if ("F_SOLLPREIS".equals(fieldName)) {
				value = data[index][MATERIAL_SOLLPREIS];
			} else if ("F_LIEF1PREIS".equals(fieldName)) {
				value = data[index][MATERIAL_LIEF1PREIS];
			} else if ("F_VKPREIS".equals(fieldName)) {
				value = data[index][MATERIAL_VKPREIS];
			} else if ("F_ISTPREIS".equals(fieldName)) {
				value = data[index][MATERIAL_ISTPREIS];
			} else if ("F_WE_REFERENZ".equals(fieldName)) {
				value = data[index][MATERIAL_WE_REFERENZ];
			} else if ("F_SNRCHNR".equals(fieldName)) {
				value = data[index][MATERIAL_SNRCHNR];
			} else if ("F_SUBREPORT_SNR_GSNR".equals(fieldName)) {
				value = data[index][MATERIAL_SUBREPORT_SNR_GSNR];
			} else if ("F_ERSATZTYP".equals(fieldName)) {
				value = data[index][MATERIAL_ERSATZTYP];
			}

		}
			break;
		case UC_VERGLEICH_MIT_STUECKLISTE: {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][VMS_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][VMS_BEZEICHNUNG];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][VMS_EINHEIT];
			} else if ("InLos".equals(fieldName)) {
				value = data[index][VMS_IN_LOS];
			} else if ("InStueckliste".equals(fieldName)) {
				value = data[index][VMS_IN_STUECKLISTE];
			} else if ("Kurzbezeichnung".equals(fieldName)) {
				value = data[index][VMS_KURZBEZEICHNUNG];
			} else if ("Montageart".equals(fieldName)) {
				value = data[index][VMS_MONTAGEART];
			} else if ("SollmengeLos".equals(fieldName)) {
				value = data[index][VMS_SOLLMENGE_LOS];
			} else if ("SollmengeStkl".equals(fieldName)) {
				value = data[index][VMS_SOLLMENGE_STUECKLISTE];
			} else if ("Sollsatzgroesse".equals(fieldName)) {
				value = data[index][VMS_SOLLSATZGROESSE];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][VMS_ZUSATZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung2".equals(fieldName)) {
				value = data[index][VMS_ZUSATZBEZEICHNUNG2];
			} else if ("Ausgabemenge".equals(fieldName)) {
				value = data[index][VMS_AUSGABEMENGE];
			} else if ("Ausgabepreis".equals(fieldName)) {
				value = data[index][VMS_AUSGABEPREIS];
			} else if ("IsErsatzartikel".equals(fieldName)) {
				value = data[index][VMS_IS_ERSATZARTIKEL];
			} else if ("SollsatzgroesseGes".equals(fieldName)) {
				value = data[index][VMS_SOLLSATZGROESSE_GESAMT];
			}
		}
			break;
		case UC_ARBEITSZEITSTATUS: {
			if ("Losnummer".equals(fieldName)) {
				value = data[index][AZSTATUS_LOSNUMMER];
			} else if ("Stueckliste".equals(fieldName)) {
				value = data[index][AZSTATUS_STUECKLISTE];
			} else if ("StuecklisteBezeichnung".equals(fieldName)) {
				value = data[index][AZSTATUS_STUECKLISTEBEZEICHNUNG];
			} else if ("Arbeitsgang".equals(fieldName)) {
				value = data[index][AZSTATUS_ARBEITSGANG];
			} else if ("Unterabeitsgang".equals(fieldName)) {
				value = data[index][AZSTATUS_UNTERARBEITSGANG];
			} else if ("Techniker".equals(fieldName)) {
				value = data[index][AZSTATUS_TECHNIKER];
			} else if ("Losgroesse".equals(fieldName)) {
				value = data[index][AZSTATUS_LOSGROESSE];
			} else if ("Projekt".equals(fieldName)) {
				value = data[index][AZSTATUS_PROJEKT];
			} else if ("Taetigkeit".equals(fieldName)) {
				value = data[index][AZSTATUS_TAETIGKEIT];
			} else if ("TaetigkeitBezeichnung".equals(fieldName)) {
				value = data[index][AZSTATUS_TAETIGKEIT_BEZEICHNUNG];
			} else if ("SollPerson".equals(fieldName)) {
				value = data[index][AZSTATUS_SOLL_PERSON];
			} else if ("IstPerson".equals(fieldName)) {
				value = data[index][AZSTATUS_IST_PERSON];
			} else if ("Fortschritt".equals(fieldName)) {
				value = data[index][AZSTATUS_FORTSCHRITT];
			} else if ("Fertig".equals(fieldName)) {
				value = data[index][AZSTATUS_FERTIG];
			}
		}
			break;
		case UC_AUSGABELISTE: {
			if ("F_IDENT".equals(fieldName)) {
				value = data[index][AUSG_IDENT];
			} else if ("F_KURZBEZEICHNUNG".equals(fieldName)) {
				value = data[index][AUSG_KURZBEZEICHNUNG];
			} else if ("F_REFERENZNUMMER".equals(fieldName)) {
				value = data[index][AUSG_REFERENZNUMMER];
			} else if ("F_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][AUSG_BEZEICHNUNG];
			} else if ("F_ARTIKELKLASSE".equals(fieldName)) {
				value = data[index][AUSG_ARTIKELKLASSE];
			} else if ("F_AUSGABE".equals(fieldName)) {
				value = data[index][AUSG_AUSGABE];
			} else if ("F_EINHEIT".equals(fieldName)) {
				value = data[index][AUSG_EINHEIT];
			} else if ("F_LAGER".equals(fieldName)) {
				value = data[index][AUSG_LAGER];
			} else if ("F_STANDORT".equals(fieldName)) {
				value = data[index][AUSG_STANDORT];
			} else if ("F_LAGERORT".equals(fieldName)) {
				value = data[index][AUSG_LAGERORT];
			} else if ("F_MENGE".equals(fieldName)) {
				value = data[index][AUSG_MENGE];
			} else if ("F_MONTAGEART".equals(fieldName)) {
				value = data[index][AUSG_MONTAGEART];
			} else if ("F_SCHALE".equals(fieldName)) {
				value = data[index][AUSG_SCHALE];
			} else if (F_ZUSATZBEZEICHNUNG.equals(fieldName)) {
				value = data[index][AUSG_ZUSATZBEZEICHNUNG];
			} else if ("F_ZUSATZBEZEICHNUNG2".equals(fieldName)) {
				value = data[index][AUSG_ZUSATZBEZEICHNUNG2];
			} else if ("F_FARBCODE".equals(fieldName)) {
				value = data[index][AUSG_FARBCODE];
			} else if ("F_HOEHE".equals(fieldName)) {
				value = data[index][AUSG_HOEHE];
			} else if (F_ARTIKEL_BREITE.equals(fieldName)) {
				value = data[index][AUSG_BREITE];
			} else if (F_ARTIKEL_TIEFE.equals(fieldName)) {
				value = data[index][AUSG_TIEFE];
			} else if ("F_MATERIAL".equals(fieldName)) {
				value = data[index][AUSG_MATERIAL];
			} else if (F_ARTIKEL_BAUFORM.equals(fieldName)) {
				value = data[index][AUSG_BAUFORM];
			} else if (F_ARTIKEL_VERPACKUNGSART.equals(fieldName)) {
				value = data[index][AUSG_VERPACKUNGSART];
			} else if (F_ARTIKEL_MONTAGE_RASTERSTEHEND.equals(fieldName)) {
				value = data[index][AUSG_RASTERSTEHEND];
			} else if ("F_BESTELLT".equals(fieldName)) {
				value = data[index][AUSG_BESTELLT];
			} else if ("F_STKL_KOMMENTAR".equals(fieldName)) {
				value = Helper.formatStyledTextForJasper(data[index][AUSG_STUECKLISTE_KOMMENTAR]);
			} else if ("F_STKL_POSITION".equals(fieldName)) {
				value = data[index][AUSG_STUECKLISTE_POSITION];
			} else if ("F_SNRCHNR".equals(fieldName)) {
				value = data[index][AUSG_SNRCHNR];
			} else if ("F_ARTIKELKOMMENTAR".equals(fieldName)) {
				value = Helper.formatStyledTextForJasper(data[index][AUSG_KOMMENTAR]);
			} else if ("F_ARTIKELBILD".equals(fieldName)) {
				value = data[index][AUSG_ARTIKELBILD];
			} else if ("F_LAGERSTAND".equals(fieldName)) {
				value = data[index][AUSG_LAGERSTAND];
			} else if ("F_IN_FERTIGUNG".equals(fieldName)) {
				value = data[index][AUSG_IN_FERTIGUNG];
			} else if ("F_LAGERSTAND_SPERRLAGER".equals(fieldName)) {
				value = data[index][AUSG_LAGERSTAND_SPERRLAGER];
			} else if ("F_INDEX".equals(fieldName)) {
				value = data[index][AUSG_INDEX];
			} else if ("F_REVISION".equals(fieldName)) {
				value = data[index][AUSG_REVISION];
			} else if ("F_STKL_SOLLMENGE".equals(fieldName)) {
				value = data[index][AUSG_STUECKLISTE_SOLLMENGE];
			} else if ("F_STKL_EINHEIT".equals(fieldName)) {
				value = data[index][AUSG_STUECKLISTE_EINHEIT];

			} else if ("F_POSITION".equals(fieldName)) {
				value = data[index][AUSG_MATERIAL_POSITION];
			} else if ("F_KOMMENTAR".equals(fieldName)) {
				value = data[index][AUSG_MATERIAL_KOMMENTAR];
			} else if ("F_DIMENSION1".equals(fieldName)) {
				value = data[index][AUSG_MATERIAL_DIMENSION1];
			} else if ("F_DIMENSION2".equals(fieldName)) {
				value = data[index][AUSG_MATERIAL_DIMENSION2];
			} else if ("F_DIMENSION3".equals(fieldName)) {
				value = data[index][AUSG_MATERIAL_DIMENSION3];
			} else if ("F_LOSNUMMER".equals(fieldName)) {
				value = data[index][AUSG_LOSNUMMER];
			} else if ("F_LOSSOLLMATERIAL_IIDS".equals(fieldName)) {
				value = data[index][AUSG_LOSSOLLMATERIAL_IIDS];
			}

			else if ("F_LOS_ANGELEGT".equals(fieldName)) {
				value = data[index][AUSG_LOS_ANGELEGT];
			} else if ("F_LOS_LOSNUMMER".equals(fieldName)) {
				value = data[index][AUSG_LOS_LOSNUMMER];
			} else if ("F_LOS_AUFTRAGNUMMER".equals(fieldName)) {
				value = data[index][AUSG_LOS_AUFTRAGNUMMER];
			} else if ("F_LOS_KOSTENSTELLENUMMER".equals(fieldName)) {
				value = data[index][AUSG_LOS_KOSTENSTELLENUMMER];
			} else if ("F_LOS_PRODUKTIONSBEGINN".equals(fieldName)) {
				value = data[index][AUSG_LOS_PRODUKTIONSBEGINN];
			} else if ("F_LOS_PRODUKTIONSENDE".equals(fieldName)) {
				value = data[index][AUSG_LOS_PRODUKTIONSENDE];
			} else if ("F_LOS_KUNDE".equals(fieldName)) {
				value = data[index][AUSG_LOS_KUNDE];
			} else if ("F_LOS_STUECKLISTENUMMER".equals(fieldName)) {
				value = data[index][AUSG_LOS_STUECKLISTENUMMER];
			} else if ("F_LOS_STUECKLISTEBEZEICHNUNG".equals(fieldName)) {
				value = data[index][AUSG_LOS_STUECKLISTEBEZEICHNUNG];
			} else if ("F_LOS_STUECKLISTEZUSATZBEZEICHNUNG".equals(fieldName)) {
				value = data[index][AUSG_LOS_STUECKLISTEZUSATZBEZEICHNUNG];
			} else if ("F_LOS_STUECKLISTE_ERFASSUNGSFAKTOR".equals(fieldName)) {
				value = data[index][AUSG_LOS_STUECKLISTE_ERFASSUNGSFAKTOR];
			} else if ("F_LOS_LOSGROESSE".equals(fieldName)) {
				value = data[index][AUSG_LOS_LOSGROESSE];
			} else if ("F_FEHLMENGE".equals(fieldName)) {
				value = data[index][AUSG_FEHLMENGE];
			}

		}
			break;
		case UC_FERTIGUNGSBEGLEITSCHEIN: {
			if ("F_IDENT".equals(fieldName)) {
				value = data[index][BEGL_IDENT];
			}
			if ("F_IST_MATERIAL".equals(fieldName)) {
				value = data[index][BEGL_IST_MATERIAL];
			} else if ("F_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][BEGL_BEZEICHNUNG];
			} else if ("F_RUESTZEIT".equals(fieldName)) {
				value = data[index][BEGL_RUESTZEIT];
			} else if ("F_STUECKZEIT".equals(fieldName)) {
				value = data[index][BEGL_STUECKZEIT];
			} else if ("F_GESAMTZEIT".equals(fieldName)) {
				value = data[index][BEGL_GESAMTZEIT];
			} else if ("F_KOMMENTAR".equals(fieldName)) {
				value = Helper.formatStyledTextForJasper(data[index][BEGL_KOMMENTAR]);
			} else if (F_ZUSATZBEZEICHNUNG.equals(fieldName)) {
				value = data[index][BEGL_ZUSATZBEZEICHNUNG];
			} else if ("F_ZUSATZBEZEICHNUNG2".equals(fieldName)) {
				value = data[index][BEGL_ZUSATZBEZEICHNUNG2];
			} else if ("F_MASCHINE".equals(fieldName)) {
				value = data[index][BEGL_MASCHINE];
			} else if ("F_MASCHINE_MANUELLE_BEDIENUNG".equals(fieldName)) {
				value = data[index][BEGL_MASCHINE_MANUELLE_BEDIENUNG];
			} else if ("F_MASCHINEBEZEICHNUNG".equals(fieldName)) {
				value = data[index][BEGL_MASCHINE_BEZEICHNUNG];
			} else if ("F_SUBREPORT_ALTERNATIV_MASCHINEN".equals(fieldName)) {
				value = data[index][BEGL_SUBREPORT_ALTERNATIV_MASCHINEN];
			} else if ("F_ARBEITSGANG".equals(fieldName)) {
				value = data[index][BEGL_ARBEITSGANG];
			} else if ("F_ARBEITSPLANKOMMENTAR".equals(fieldName)) {
				value = data[index][BEGL_ARBEITSPLANKOMMENTAR];
			} else if ("F_UNTERARBEITSGANG".equals(fieldName)) {
				value = data[index][BEGL_UNTERARBEITSGANG];
			} else if ("F_AGART".equals(fieldName)) {
				value = data[index][BEGL_AGART];
			} else if ("F_AUFSPANNUNG".equals(fieldName)) {
				value = data[index][BEGL_AUFSPANNUNG];
			} else if ("F_FERTIG".equals(fieldName)) {
				value = data[index][BEGL_FERTIG];
			} else if ("F_NURZURINFORMATION".equals(fieldName)) {
				value = data[index][BEGL_NURZURINFORMATION];
			}
			// Material
			else if ("F_MATERIAL_ARTIKELKLASSE".equals(fieldName)) {
				value = data[index][BEGL_MATERIAL_ARTIKELKLASSE];
			} else if ("F_MATERIAL_AUSGABE".equals(fieldName)) {
				value = data[index][BEGL_MATERIAL_AUSGABE];
			} else if ("F_MATERIAL_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][BEGL_MATERIAL_BEZEICHNUNG];
			} else if ("F_MATERIAL_KURZBEZEICHNUNG".equals(fieldName)) {
				value = data[index][BEGL_MATERIAL_KURZBEZEICHNUNG];
			} else if ("F_MATERIAL_REFERENZNUMMER".equals(fieldName)) {
				value = data[index][BEGL_MATERIAL_REFERENZNUMMER];
			} else if ("F_MATERIAL_EINHEIT".equals(fieldName)) {
				value = data[index][BEGL_MATERIAL_EINHEIT];
			} else if ("F_MATERIAL_FARBCODE".equals(fieldName)) {
				value = data[index][BEGL_MATERIAL_FARBCODE];
			} else if ("F_MATERIAL_IDENT".equals(fieldName)) {
				value = data[index][BEGL_MATERIAL_IDENT];
			} else if ("F_MATERIAL_LAGER".equals(fieldName)) {
				value = data[index][BEGL_MATERIAL_LAGER];
			} else if ("F_MATERIAL_LAGERORT".equals(fieldName)) {
				value = data[index][BEGL_MATERIAL_LAGERORT];
			} else if ("F_MATERIAL_MENGE".equals(fieldName)) {
				value = data[index][BEGL_MATERIAL_MENGE];
			} else if ("F_MATERIAL_MONTAGEART".equals(fieldName)) {
				value = data[index][BEGL_MATERIAL_MONTAGEART];
			} else if ("F_MATERIAL_SCHALE".equals(fieldName)) {
				value = data[index][BEGL_MATERIAL_SCHALE];
			} else if ("F_MATERIAL_ZUSATZBEZEICHNUNG".equals(fieldName)) {
				value = data[index][BEGL_MATERIAL_ZUSATZBEZEICHNUNG];
			} else if ("F_MATERIAL_ZUSATZBEZEICHNUNG2".equals(fieldName)) {
				value = data[index][BEGL_MATERIAL_ZUSATZBEZEICHNUNG2];
			} else if ("F_MATERIAL_HOEHE".equals(fieldName)) {
				value = data[index][BEGL_MATERIAL_HOEHE];
			} else if (F_ARTIKEL_BREITE.equals(fieldName)) {
				value = data[index][BEGL_MATERIAL_BREITE];
			} else if (F_ARTIKEL_TIEFE.equals(fieldName)) {
				value = data[index][BEGL_MATERIAL_TIEFE];
			} else if ("F_MATERIAL_MATERIAL".equals(fieldName)) {
				value = data[index][BEGL_MATERIAL_MATERIAL];
			} else if ("F_MATERIAL_INDEX".equals(fieldName)) {
				value = data[index][BEGL_MATERIAL_INDEX];
			} else if ("F_MATERIAL_REVISION".equals(fieldName)) {
				value = data[index][BEGL_MATERIAL_REVISION];
			} else if (F_ARTIKEL_BAUFORM.equals(fieldName)) {
				value = data[index][BEGL_BAUFORM];
			} else if (F_ARTIKEL_VERPACKUNGSART.equals(fieldName)) {
				value = data[index][BEGL_VERPACKUNGSART];
			} else if (F_ARTIKEL_GEWICHTKG.equals(fieldName)) {
				value = data[index][BEGL_GEWICHTKG];
			} else if ("FremdmaterialArtikel".equals(fieldName)) {
				value = data[index][BEGL_FREMDMATERIAL_ARTIKEL];
			} else if ("FremdmaterialArtikelbezeichnung".equals(fieldName)) {
				value = data[index][BEGL_FREMDMATERIAL_ARTIKELBEZEICHNUNG];
			} else if ("FremdmaterialSollmenge".equals(fieldName)) {
				value = data[index][BEGL_FREMDMATERIAL_SOLLMENGE];
			} else if ("FremdmaterialLagerort".equals(fieldName)) {
				value = data[index][BEGL_FREMDMATERIAL_LAGERORT];
			} else if ("FremdmaterialArtikelkurzbezeichnung".equals(fieldName)) {
				value = data[index][BEGL_FREMDMATERIAL_ARTIKELKURZBEZEICHNUNG];
			} else if ("FremdmaterialArtikelzusatzbezeichnung".equals(fieldName)) {
				value = data[index][BEGL_FREMDMATERIAL_ARTIKELZUSATZBEZEICHNUNG];
			} else if ("FremdmaterialArtikelzusatzbezeichnung2".equals(fieldName)) {
				value = data[index][BEGL_FREMDMATERIAL_ARTIKELZUSATZBEZEICHNUNG2];
			} else if ("F_MATERIAL_SNRCHNR".equals(fieldName)) {
				value = data[index][BEGL_MATERIAL_SNRCHNR];
			} else if ("F_MATERIAL_STUECKLISTE_POSITION".equals(fieldName)) {
				value = data[index][BEGL_MATERIAL_STUECKLISTE_POSITION];
			}

			else if ("F_NEXT_AG_IDENT".equals(fieldName)) {
				value = data[index][BEGL_NEXT_AG_IDENT];
			} else if ("F_NEXT_AG_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][BEGL_NEXT_AG_BEZEICHNUNG];
			} else if ("F_NEXT_AG_RUESTZEIT".equals(fieldName)) {
				value = data[index][BEGL_NEXT_AG_RUESTZEIT];
			} else if ("F_NEXT_AG_STUECKZEIT".equals(fieldName)) {
				value = data[index][BEGL_NEXT_AG_STUECKZEIT];
			} else if ("F_NEXT_AG_GESAMTZEIT".equals(fieldName)) {
				value = data[index][BEGL_NEXT_AG_GESAMTZEIT];
			} else if ("F_NEXT_AG_KOMMENTAR".equals(fieldName)) {
				value = data[index][BEGL_NEXT_AG_KOMMENTAR];
			} else if ("F_NEXT_AG_ZUSATZBEZEICHNUNG".equals(fieldName)) {
				value = data[index][BEGL_NEXT_AG_ZUSATZBEZEICHNUNG];
			} else if ("F_NEXT_AG_ZUSATZBEZEICHNUNG2".equals(fieldName)) {
				value = data[index][BEGL_NEXT_AG_ZUSATZBEZEICHNUNG2];
			} else if ("F_NEXT_AG_MASCHINE".equals(fieldName)) {
				value = data[index][BEGL_NEXT_AG_MASCHINE];
			} else if ("F_NEXT_AG_MASCHINE_MANUELLE_BEDIENUNG".equals(fieldName)) {
				value = data[index][BEGL_NEXT_AG_MASCHINE_MANUELLE_BEDIENUNG];
			} else if ("F_NEXT_AG_MASCHINE_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][BEGL_NEXT_AG_MASCHINE_BEZEICHNUNG];
			} else if ("F_NEXT_AG_AGART".equals(fieldName)) {
				value = data[index][BEGL_NEXT_AG_AGART];
			} else if ("F_NEXT_AG_AUFSPANNUNG".equals(fieldName)) {
				value = data[index][BEGL_NEXT_AG_AUFSPANNUNG];
			} else if ("F_NEXT_AG_UNTERARBEITSGANG".equals(fieldName)) {
				value = data[index][BEGL_NEXT_AG_UNTERARBEITSGANG];
			} else if ("F_NEXT_AG_ARBEITSGANG".equals(fieldName)) {
				value = data[index][BEGL_NEXT_AG_ARBEITSGANG];
			} else if ("F_NEXT_AG_ARBEITSPLANKOMMENTAR".equals(fieldName)) {
				value = data[index][BEGL_NEXT_AG_ARBEITSPLANKOMMENTAR];
			} else if ("F_NEXT_AG_BEGINN".equals(fieldName)) {
				value = data[index][BEGL_NEXT_AG_BEGINN];
			} else if ("F_NEXT_AG_MASCHINENVERSATZ_MS".equals(fieldName)) {
				value = data[index][BEGL_NEXT_AG_MASCHINENVERSATZ_MS];
			} else if ("F_NEXT_AG_FREMDMATERIAL_ARTIKEL".equals(fieldName)) {
				value = data[index][BEGL_NEXT_AG_FREMDMATERIAL_ARTIKEL];
			} else if ("F_NEXT_AG_FREMDMATERIAL_ARTIKELBEZEICHNUNG".equals(fieldName)) {
				value = data[index][BEGL_NEXT_AG_FREMDMATERIAL_ARTIKELBEZEICHNUNG];
			} else if ("F_BEGINN".equals(fieldName)) {
				value = data[index][BEGL_BEGINN];
			} else if ("F_MASCHINENVERSATZ_MS".equals(fieldName)) {
				value = data[index][BEGL_MASCHINENVERSATZ_MS];
			}

			else if ("F_MATERIAL_STUECKLISTE_SOLLMENGE".equals(fieldName)) {
				value = data[index][BEGL_MATERIAL_STUECKLISTE_SOLLMENGE];
			} else if ("F_MATERIAL_STUECKLISTE_EINHEIT".equals(fieldName)) {
				value = data[index][BEGL_MATERIAL_STUECKLISTE_EINHEIT];
			} else if ("F_MATERIAL_DIMENSION1".equals(fieldName)) {
				value = data[index][BEGL_MATERIAL_DIMENSION1];
			} else if ("F_MATERIAL_DIMENSION2".equals(fieldName)) {
				value = data[index][BEGL_MATERIAL_DIMENSION2];
			} else if ("F_MATERIAL_DIMENSION3".equals(fieldName)) {
				value = data[index][BEGL_MATERIAL_DIMENSION3];
			}

		}
			break;
		case UC_ABLIEFERUNGSSTATISTIK: {
			if ("F_LOSNUMMER".equals(fieldName)) {
				value = data[index][ABLIEF_LOSNUMMER];
			} else if ("F_IDENT".equals(fieldName)) {
				value = data[index][ABLIEF_IDENT];
			} else if ("F_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][ABLIEF_BEZEICHNUNG];
			} else if ("F_DATUM".equals(fieldName)) {
				value = data[index][ABLIEF_DATUM];
			} else if ("F_MENGE".equals(fieldName)) {
				value = data[index][ABLIEF_MENGE];
			} else if ("F_ABLIEFERPREIS".equals(fieldName)) {
				value = data[index][ABLIEF_ABLIEFERPREIS];
			} else if ("F_MATERIALWERT".equals(fieldName)) {
				value = data[index][ABLIEF_MATERIALWERT];
			} else if ("F_ARBEITSZEITWERT".equals(fieldName)) {
				value = data[index][ABLIEF_ARBEITSZEITWERT];
			} else if ("F_WERT".equals(fieldName)) {
				value = data[index][ABLIEF_WERT];
			} else if ("F_AUFTRAGSNUMMER".equals(fieldName)) {
				value = data[index][ABLIEF_AUFRAGSNUMMER];
			} else if (F_ZUSATZBEZEICHNUNG.equals(fieldName)) {
				value = data[index][ABLIEF_ZUSATZBEZEICHNUNG];
			} else if ("F_ABGANG_BELEGART".equals(fieldName)) {
				value = data[index][ABLIEF_ABGANG_BELEGART];
			} else if ("F_ABGANG_BELEGNUMMER".equals(fieldName)) {
				value = data[index][ABLIEF_ABGANG_BELEGNUMMER];
			}
		}
			break;
		case UC_LOSPRUEFPLAN: {
			if ("ArtikelKontakt".equals(fieldName)) {
				value = data[index][PRUEFPLAN_ARTIKEL_KONTAKT];
			} else if ("ArtikelbezeichnungKontakt".equals(fieldName)) {
				value = data[index][PRUEFPLAN_ARTIKELBEZEICHNUNG_KONTAKT];
			} else if ("ArtikelLitze".equals(fieldName)) {
				value = data[index][PRUEFPLAN_ARTIKEL_LITZE];
			} else if ("ArtikelbezeichnungLitze".equals(fieldName)) {
				value = data[index][PRUEFPLAN_ARTIKELBEZEICHNUNG_LITZE];
			} else if ("PositionKontakt".equals(fieldName)) {
				value = data[index][PRUEFPLAN_POSITION_KONTAKT];
			} else if ("PositionLitze".equals(fieldName)) {
				value = data[index][PRUEFPLAN_POSITION_LITZE];
			} else if ("Verschleissteilnummer".equals(fieldName)) {
				value = data[index][PRUEFPLAN_VERSCHLEISSTEILNUMMER];
			} else if ("Verschleissteilbezeichnung".equals(fieldName)) {
				value = data[index][PRUEFPLAN_VERSCHLEISSTEILBEZEICHNUNG];
			} else if ("Werkzeugnummer".equals(fieldName)) {
				value = data[index][PRUEFPLAN_WERKZEUGNUMMER];
			} else if ("Werkzeugbezeichnung".equals(fieldName)) {
				value = data[index][PRUEFPLAN_WERKZEUGBEZEICHNUNG];
			} else if ("CrimpbreiteDraht".equals(fieldName)) {
				value = data[index][PRUEFPLAN_CRIMPBREITE_DRAHT];
			} else if ("CrimpbreiteIsolation".equals(fieldName)) {
				value = data[index][PRUEFPLAN_CRIMPBREITE_ISOLATION];
			} else if ("CrimphoeheDraht".equals(fieldName)) {
				value = data[index][PRUEFPLAN_CRIMPHOEHE_DRAHT];
			} else if ("CrimphoeheIsolation".equals(fieldName)) {
				value = data[index][PRUEFPLAN_CRIMPHOEHE_ISOLATION];
			} else if ("AnzahlVerwendungenVerschleissteil".equals(fieldName)) {
				value = data[index][PRUEFPLAN_ANZAHL_VERWENDUNGEN_VERSCHLEISSTEIL];
			}

			else if ("ToleranzCrimpbreiteDraht".equals(fieldName)) {
				value = data[index][PRUEFPLAN_TOLERANZ_CRIMPBREITE_DRAHT];
			} else if ("ToleranzCrimpbreiteIsolation".equals(fieldName)) {
				value = data[index][PRUEFPLAN_TOLERANZ_CRIMPBREITE_ISOLATION];
			} else if ("ToleranzCrimphoeheDraht".equals(fieldName)) {
				value = data[index][PRUEFPLAN_TOLERANZ_CRIMPHOEHE_DRAHT];
			} else if ("ToleranzCrimphoeheIsolation".equals(fieldName)) {
				value = data[index][PRUEFPLAN_TOLERANZ_CRIMPHOEHE_ISOLATION];
			} else if ("ToleranzWert".equals(fieldName)) {
				value = data[index][PRUEFPLAN_TOLERANZ_WERT];
			} else if ("Wert".equals(fieldName)) {
				value = data[index][PRUEFPLAN_WERT];
			} else if ("Pruefart".equals(fieldName)) {
				value = data[index][PRUEFPLAN_PRUEFART];
			} else if ("PruefartBezeichnung".equals(fieldName)) {
				value = data[index][PRUEFPLAN_PRUEFART_BEZEICHNUNG];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][PRUEFPLAN_KOMMENTAR];
			} else if ("MengeLitze".equals(fieldName)) {
				value = data[index][PRUEFPLAN_MENGE_LITZE];
			} else if ("EinheitLitze".equals(fieldName)) {
				value = data[index][PRUEFPLAN_EINHEIT_LITZE];
			} else if ("Dimension1Litze".equals(fieldName)) {
				value = data[index][PRUEFPLAN_DIMENSION1_LITZE];
			} else if ("Dimension2Litze".equals(fieldName)) {
				value = data[index][PRUEFPLAN_DIMENSION2_LITZE];
			} else if ("Dimension3Litze".equals(fieldName)) {
				value = data[index][PRUEFPLAN_DIMENSION3_LITZE];
			}

			else if ("ArtikelLitze2".equals(fieldName)) {
				value = data[index][PRUEFPLAN_ARTIKEL_LITZE2];
			} else if ("ArtikelbezeichnungLitze2".equals(fieldName)) {
				value = data[index][PRUEFPLAN_ARTIKELBEZEICHNUNG_LITZE2];
			} else if ("MengeLitze2".equals(fieldName)) {
				value = data[index][PRUEFPLAN_MENGE_LITZE2];
			} else if ("EinheitLitze2".equals(fieldName)) {
				value = data[index][PRUEFPLAN_EINHEIT_LITZE2];
			} else if ("Dimension1Litze2".equals(fieldName)) {
				value = data[index][PRUEFPLAN_DIMENSION1_LITZE2];
			} else if ("Dimension2Litze2".equals(fieldName)) {
				value = data[index][PRUEFPLAN_DIMENSION2_LITZE2];
			} else if ("Dimension3Litze2".equals(fieldName)) {
				value = data[index][PRUEFPLAN_DIMENSION3_LITZE2];
			} else if ("PositionLitze2".equals(fieldName)) {
				value = data[index][PRUEFPLAN_POSITION_LITZE2];
			} else if ("AbzugskraftLitze".equals(fieldName)) {
				value = data[index][PRUEFPLAN_ABZUGSKRAFT_LITZE];
			} else if ("AbzugskraftLitze2".equals(fieldName)) {
				value = data[index][PRUEFPLAN_ABZUGSKRAFT_LITZE2];
			} else if ("Doppelanschlag".equals(fieldName)) {
				value = data[index][PRUEFPLAN_DOPPELANSCHLAG];
			} else if ("SubreportMoeglicheWerkzeuge".equals(fieldName)) {
				value = data[index][PRUEFPLAN_SUBREPORT_MOEGLICHE_WERKZEUGE];
			}

		}
			break;
		case UC_NICHT_ERFASSTE_PRUEFERGEBNISSE: {
			if ("ArtikelKontakt".equals(fieldName)) {
				value = data[index][NEP_ARTIKEL_KONTAKT];
			} else if ("ArtikelbezeichnungKontakt".equals(fieldName)) {
				value = data[index][NEP_ARTIKELBEZEICHNUNG_KONTAKT];
			} else if ("ArtikelLitze".equals(fieldName)) {
				value = data[index][NEP_ARTIKEL_LITZE];
			} else if ("ArtikelbezeichnungLitze".equals(fieldName)) {
				value = data[index][NEP_ARTIKELBEZEICHNUNG_LITZE];
			} else if ("PositionKontakt".equals(fieldName)) {
				value = data[index][NEP_POSITION_KONTAKT];
			} else if ("PositionLitze".equals(fieldName)) {
				value = data[index][NEP_POSITION_LITZE];
			} else if ("Verschleissteil".equals(fieldName)) {
				value = data[index][NEP_VERSCHLEISSTEILNUMMER];
			} else if ("Verschleissteilbezeichnung".equals(fieldName)) {
				value = data[index][NEP_VERSCHLEISSTEILBEZEICHNUNG];
			} else if ("Werkzeugnummer".equals(fieldName)) {
				value = data[index][NEP_WERKZEUGNUMMER];
			} else if ("Werkzeugbezeichnung".equals(fieldName)) {
				value = data[index][NEP_WERKZEUGBEZEICHNUNG];
			} else if ("Pruefart".equals(fieldName)) {
				value = data[index][NEP_PRUEFART];
			} else if ("PruefartBezeichnung".equals(fieldName)) {
				value = data[index][NEP_PRUEFART_BEZEICHNUNG];
			} else if ("Ablieferdatum".equals(fieldName)) {
				value = data[index][NEP_ABLIEFERDATUM];
			} else if ("Losnummer".equals(fieldName)) {
				value = data[index][NEP_LOSNUMMER];
			} else if ("Losgroesse".equals(fieldName)) {
				value = data[index][NEP_LOSGROESSE];
			} else if ("Status".equals(fieldName)) {
				value = data[index][NEP_STATUS];
			} else if ("Produktionsbeginn".equals(fieldName)) {
				value = data[index][NEP_PRODUKTIONSBEGINN];
			} else if ("StuecklisteBezeichnung".equals(fieldName)) {
				value = data[index][NEP_STUECKLISTEBEZEICHNUNG];
			} else if ("StuecklisteZusatzbezeichnung".equals(fieldName)) {
				value = data[index][NEP_STUECKLISTEZUSATZBEZEICHNUNG];
			} else if ("StuecklisteNummer".equals(fieldName)) {
				value = data[index][NEP_STUECKLISTENUMMER];
			}

		}
			break;
		case UC_PRUEFERGEBNIS: {
			if ("ArtikelKontakt".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_ARTIKEL_KONTAKT];
			} else if ("ArtikelbezeichnungKontakt".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_ARTIKELBEZEICHNUNG_KONTAKT];
			} else if ("ArtikelLitze".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_ARTIKEL_LITZE];
			} else if ("ArtikelbezeichnungLitze".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_ARTIKELBEZEICHNUNG_LITZE];
			} else if ("Verschleissteil".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_VERSCHLEISSTEILNUMMER];
			} else if ("Verschleissteilbezeichnung".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_VERSCHLEISSTEILBEZEICHNUNG];
			} else if ("Werkzeugnummer".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_WERKZEUGNUMMER];
			} else if ("Werkzeugbezeichnung".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_WERKZEUGBEZEICHNUNG];
			} else if ("SollCrimpbreiteDraht".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_SOLL_CRIMPBREITE_DRAHT];
			} else if ("SollCrimpbreiteIsolation".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_SOLL_CRIMPBREITE_ISOLATION];
			} else if ("SollCrimphoeheDraht".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_SOLL_CRIMPHOEHE_DRAHT];
			} else if ("SollCrimphoeheIsolation".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_SOLL_CRIMPHOEHE_ISOLATION];
			} else if ("IstCrimpbreiteDraht".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_IST_CRIMPBREITE_DRAHT];
			} else if ("IstCrimpbreiteIsolation".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_IST_CRIMPBREITE_ISOLATION];
			} else if ("IstCrimphoeheDraht".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_IST_CRIMPHOEHE_DRAHT];
			} else if ("IstCrimphoeheIsolation".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_IST_CRIMPHOEHE_ISOLATION];
			} else if ("Abliefermenge".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_ABLIEFERMENGE];
			} else if ("Ablieferzeitpunkt".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_ABLIEFERZEITPUNKT];
			} else if ("Beginn".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_BEGINN];
			} else if ("Ende".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_ENDE];
			} else if ("StuecklisteNummer".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_STUECKLISTENUMMER];
			} else if ("StuecklisteBezeichnung".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_STUECKLISTEBEZEICHNUNG];
			} else if ("Losnummer".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_LOSNUMMMER];
			}

			else if ("SollToleranzCrimpbreiteDraht".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_SOLL_TOLERANZ_CRIMPBREITE_DRAHT];
			} else if ("SollToleranzCrimpbreiteIsolation".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_SOLL_TOLERANZ_CRIMPBREITE_ISOLATION];
			} else if ("SollToleranzCrimphoeheDraht".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_SOLL_TOLERANZ_CRIMPHOEHE_DRAHT];
			} else if ("SollToleranzCrimphoeheIsolation".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_SOLL_TOLERANZ_CRIMPHOEHE_ISOLATION];
			}

			else if ("SollToleranzWert".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_SOLL_TOLERANZ_WERT];
			} else if ("SollWert".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_SOLL_WERT];
			}

			else if ("IstWert".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_IST_WERT];
			} else if ("IstWertBoolean".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_IST_WERT_BOOLEAN];
			}

			else if ("Pruefart".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_PRUEFART];
			} else if ("PruefartBezeichnung".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_PRUEFART_BEZEICHNUNG];
			} else if ("KommentarPruefkombination".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_KOMMENTAR_PRUEFKOMBINATION];
			} else if ("PositionKontakt".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_POSITION_KONTAT];
			} else if ("PositionLitze".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_POSITION_LITZE];
			}

			else if ("ArtikelLitze2".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_ARTIKEL_LITZE2];
			} else if ("ArtikelbezeichnungLitze2".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_ARTIKELBEZEICHNUNG_LITZE2];
			} else if ("PositionLitze2".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_POSITION_LITZE2];
			} else if ("AbzugskraftLitze".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_ABZUGSKRAFT_LITZE];
			} else if ("AbzugskraftLitze2".equals(fieldName)) {
				value = data[index][PRUEFERGEBNIS_ABZUGSKRAFT_LITZE2];
			}

		}
			break;
		case UC_ABLIEFERETIKETT: {
			if ("F_SNRCHNR".equals(fieldName)) {
				value = data[index][ABLIEFERETIKETT_SERIENNUMMER];
			} else if ("F_MENGE".equals(fieldName)) {
				value = data[index][ABLIEFERETIKETT_MENGE];
			} else if ("F_SUBREPORT_GERAETESNR".equals(fieldName)) {
				value = data[index][ABLIEFERETIKETT_SUBREPORT_GERAETESNR];
			} else if ("F_VERSION".equals(fieldName)) {
				value = data[index][ABLIEFERETIKETT_VERSION];
			} else if ("F_I_ID_BUCHUNG".equals(fieldName)) {
				value = data[index][ABLIEFERETIKETT_I_ID_BUCHUNG];
			} else if ("F_EXEMPLAR".equals(fieldName)) {
				value = data[index][ABLIEFERETIKETT_EXEMPLAR];
			}
		}
			break;
		case UC_FEHLERSTATISTIK: {
			if ("F_AG_AGNUMMER".equals(fieldName)) {
				value = data[index][FEHLERSTATISTIK_AG_AGNUMMER];
			} else if ("F_AG_ARTIKELBEZEICHNUNG".equals(fieldName)) {
				value = data[index][FEHLERSTATISTIK_AG_ARTIKELBEZEICHNUNG];
			} else if ("F_AG_ARTIKELNUMMER".equals(fieldName)) {
				value = data[index][FEHLERSTATISTIK_AG_ARTIKELNUMMER];
			} else if ("F_AG_UAGNUMMER".equals(fieldName)) {
				value = data[index][FEHLERSTATISTIK_AG_UAGNUMMER];
			} else if ("F_ERLEDIGTDATUM".equals(fieldName)) {
				value = data[index][FEHLERSTATISTIK_ERLEDIGTDATUM];
			} else if ("F_FEHLER".equals(fieldName)) {
				value = data[index][FEHLERSTATISTIK_FEHLER];
			} else if ("F_FEHLERSPR".equals(fieldName)) {
				value = data[index][FEHLERSTATISTIK_FEHLERSPR];
			} else if ("F_FEHLER_KOMMENTAR".equals(fieldName)) {
				value = data[index][FEHLERSTATISTIK_FEHLER_KOMMENTAR];
			} else if ("F_GUT".equals(fieldName)) {
				value = data[index][FEHLERSTATISTIK_GUT];
			} else if ("F_SCHLECHT".equals(fieldName)) {
				value = data[index][FEHLERSTATISTIK_SCHLECHT];
			} else if ("F_INARBEIT".equals(fieldName)) {
				value = data[index][FEHLERSTATISTIK_INARBEIT];
			} else if ("F_LOSGROESSE".equals(fieldName)) {
				value = data[index][FEHLERSTATISTIK_LOSGROESSE];
			} else if ("F_LOSNUMMER".equals(fieldName)) {
				value = data[index][FEHLERSTATISTIK_LOSNUMMER];
			} else if ("F_MENGE_ERLEDIGT".equals(fieldName)) {
				value = data[index][FEHLERSTATISTIK_MENGE_ERLEDIGT];
			} else if ("F_PERSONAL".equals(fieldName)) {
				value = data[index][FEHLERSTATISTIK_PERSONAL];
			} else if ("F_PROJEKT".equals(fieldName)) {
				value = data[index][FEHLERSTATISTIK_PROJEKT];
			} else if ("F_STUECKLISTE".equals(fieldName)) {
				value = data[index][FEHLERSTATISTIK_STUECKLISTE];
			} else if ("F_STUECKLISTEBEZEICHNUNG".equals(fieldName)) {
				value = data[index][FEHLERSTATISTIK_STUECKLISTEBEZEICHNUNG];
			} else if ("F_ZEITPUNKT".equals(fieldName)) {
				value = data[index][FEHLERSTATISTIK_ZEITPUNKT];
			}
		}
			break;
		case UC_LOSZEITEN: {
			if ("F_ARTIKELNUMMER".equals(fieldName)) {
				value = data[index][LOSZEITEN_ARTIKELNUMMER];
			} else if ("F_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][LOSZEITEN_BEZEICHNUNG];
			} else if ("F_ZEITDATEN_I_ID".equals(fieldName)) {
				value = data[index][LOSZEITEN_ZEITDATEN_I_ID];
			} else if ("F_ZUSATZBEZEICHNUNG".equals(fieldName)) {
				value = data[index][LOSZEITEN_ZUSATZBEZEICHNUNG];
			} else if ("F_BEGINN".equals(fieldName)) {
				value = data[index][LOSZEITEN_BEGINN];
			} else if ("F_BEMERKUNG".equals(fieldName)) {
				value = data[index][LOSZEITEN_BEMERKUNG];
			} else if ("F_BEMERKUNG_LANG".equals(fieldName)) {
				value = data[index][LOSZEITEN_BEMERKUNG_LANG];
			} else if ("F_MASCHINENGRUPPE".equals(fieldName)) {
				value = data[index][LOSZEITEN_MASCHINENGRUPPE];
			} else if ("F_ARBEITSGANG".equals(fieldName)) {
				value = data[index][LOSZEITEN_ARBEITSGANG];
			} else if ("F_UNTERARBEITSGANG".equals(fieldName)) {
				value = data[index][LOSZEITEN_UNTERARBEITSGANG];
			} else if ("F_DAUER".equals(fieldName)) {
				value = data[index][LOSZEITEN_DAUER];
			} else if ("F_ENDE".equals(fieldName)) {
				value = data[index][LOSZEITEN_ENDE];
			} else if ("F_PERSON_MASCHINE".equals(fieldName)) {
				value = data[index][LOSZEITEN_PERSON_MASCHINE];
			} else if ("F_PERSONALNUMMER_MASCHINENINVENARNUMMER".equals(fieldName)) {
				value = data[index][LOSZEITEN_PERSONALNUMMER_MASCHINENINVENARNUMMER];
			}
		}
			break;
		case UC_FEHLTEILE: {
			if ("F_ARTIKELNUMMER".equals(fieldName)) {
				value = data[index][FT_ARTIKELNUMMER];
			} else if ("F_LAGERBEWIRTSCHAFTET".equals(fieldName)) {
				value = data[index][FT_LAGERBEWIRTSCHAFTET];
			} else if ("F_ERSATZTYP".equals(fieldName)) {
				value = data[index][FT_ERSATZTYP];
			} else if ("F_AUSGEGEBEN".equals(fieldName)) {
				value = data[index][FT_AUSGEGEBEN];
			} else if ("F_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][FT_BEZEICHNUNG];
			} else if ("F_KURZBEZEICHNUNG".equals(fieldName)) {
				value = data[index][FT_KURZBEZEICHNUNG];
			} else if ("F_REFERENZNUMMER".equals(fieldName)) {
				value = data[index][FT_REFERENZNUMMER];
			} else if ("F_EBENE".equals(fieldName)) {
				value = data[index][FT_EBENE];
			} else if ("F_FEHLMENGE".equals(fieldName)) {
				value = data[index][FT_FEHLMENGE];
			} else if ("F_LAGERSTAND".equals(fieldName)) {
				value = data[index][FT_LAGERSTAND];
			} else if ("F_LOSNUMMER".equals(fieldName)) {
				value = data[index][FT_LOSNUMMER];
			} else if ("F_SOLLMENGE".equals(fieldName)) {
				value = data[index][FT_SOLLMENGE];
			} else if ("F_STUECKLISTENART".equals(fieldName)) {
				value = data[index][FT_STUECKLISTENART];
			} else if ("F_LOSGROESSE".equals(fieldName)) {
				value = data[index][FT_LOSGROESSE];
			} else if ("F_LOSSTATUS".equals(fieldName)) {
				value = data[index][FT_LOSSTATUS];
			} else if ("F_ABGELIEFERT".equals(fieldName)) {
				value = data[index][FT_ABGELIEFERT];
			} else if ("F_FRUEHESTER_EINTREFFTERMIN".equals(fieldName)) {
				value = data[index][FT_FRUEHESTER_EINTREFFTERMIN];
			} else if ("F_SUBREPORT_FRUEHESTER_EINTREFFTERMIN_BS".equals(fieldName)) {
				value = data[index][FT_SUBREPORT_FRUEHESTER_EINTREFFTERMIN_BS];
			} else if ("F_SUBREPORT_FRUEHESTER_EINTREFFTERMIN_LO".equals(fieldName)) {
				value = data[index][FT_SUBREPORT_FRUEHESTER_EINTREFFTERMIN_LO];
			}

		}
			break;

		case UC_ZEITENTWICKLUNG: {
			if ("Sollzeit".equals(fieldName)) {
				value = data[index][ZE_SOLLZEIT];
			} else if ("Istzeit".equals(fieldName)) {
				value = data[index][ZE_ISTZEIT];
			} else if ("Artikelgruppe".equals(fieldName)) {
				value = data[index][ZE_ARTIKELGRUPPE];
			} else if ("Datum".equals(fieldName)) {
				value = data[index][ZE_DATUM];
			}
		}
			break;
		case UC_BEDARFSUEBERNAHME_SYNCHRONISIERUNG: {
			if ("Abgang".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_SYNCHRONISIERUNG_ABGANG];
			} else if ("ArtikelbezeichnungHV".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_SYNCHRONISIERUNG_ARTIKELBEZEICHNUNG_HV];
			} else if ("ArtikelbezeichnungManuell".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_SYNCHRONISIERUNG_ARTIKELBEZEICHNUNG_MANUELL];
			} else if ("ArtikelnummerHV".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_SYNCHRONISIERUNG_ARTIKELNUMMER_HV];
			} else if ("ArtikelnummerManuell".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_SYNCHRONISIERUNG_ARTIKELNUMMER_MANUELL];
			} else if ("Auftrag".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_SYNCHRONISIERUNG_AUFTRAG];
			} else if ("Kunde".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_SYNCHRONISIERUNG_KUNDE];
			} else if ("LosProjekt".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_SYNCHRONISIERUNG_LOS_PROJEKT];
			} else if ("LosnummerHV".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_SYNCHRONISIERUNG_LOSNUMMER_HV];
			} else if ("LosnummerManuell".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_SYNCHRONISIERUNG_LOSNUMMER_MANUELL];
			} else if ("Person".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_SYNCHRONISIERUNG_PERSON];
			} else if ("StuecklisteBezeichnung".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_SYNCHRONISIERUNG_STUECKLISTENBEZEICHNUNG];
			} else if ("Stueckliste".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_SYNCHRONISIERUNG_STUECKLISTENNUMMER];
			} else if ("Wunschmenge".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_SYNCHRONISIERUNG_WUNSCHMENGE];
			} else if ("Wunschtermin".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_SYNCHRONISIERUNG_WUNSCHTERMIN];
			} else if ("Zusaetzlich".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_SYNCHRONISIERUNG_ZUSAETZLICH];
			} else if ("Bild".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_SYNCHRONISIERUNG_BILD];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_SYNCHRONISIERUNG_KOMMENTAR];
			} else if ("LosStatus".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_SYNCHRONISIERUNG_LOS_STATUS];
			}

		}
			break;
		case UC_BEDARFSUEBERNAHME_BUCHUNGSLISTE: {
			if ("Abgang".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_BUCHUNGSLISTE_ABGANG];
			} else if ("ArtikelbezeichnungHV".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_BUCHUNGSLISTE_ARTIKELBEZEICHNUNG_HV];
			} else if ("ArtikelbezeichnungManuell".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_BUCHUNGSLISTE_ARTIKELBEZEICHNUNG_MANUELL];
			} else if ("ArtikelnummerHV".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_BUCHUNGSLISTE_ARTIKELNUMMER_HV];
			} else if ("ArtikelnummerManuell".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_BUCHUNGSLISTE_ARTIKELNUMMER_MANUELL];
			} else if ("ArtikelEinheit".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_BUCHUNGSLISTE_ARTIKELEINHEIT];
			} else if ("Auftrag".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_BUCHUNGSLISTE_AUFTRAG];
			} else if ("Kunde".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_BUCHUNGSLISTE_KUNDE];
			} else if ("LosProjekt".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_BUCHUNGSLISTE_LOS_PROJEKT];
			} else if ("LosnummerHV".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_BUCHUNGSLISTE_LOSNUMMER_HV];
			} else if ("LosnummerManuell".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_BUCHUNGSLISTE_LOSNUMMER_MANUELL];
			} else if ("Person".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_BUCHUNGSLISTE_PERSON];
			} else if ("StuecklisteBezeichnung".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_BUCHUNGSLISTE_STUECKLISTENBEZEICHNUNG];
			} else if ("Stueckliste".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_BUCHUNGSLISTE_STUECKLISTENNUMMER];
			} else if ("Wunschmenge".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_BUCHUNGSLISTE_WUNSCHMENGE];
			} else if ("Wunschtermin".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_BUCHUNGSLISTE_WUNSCHTERMIN];
			} else if ("Zusaetzlich".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_BUCHUNGSLISTE_ZUSAETZLICH];
			} else if ("Bild".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_BUCHUNGSLISTE_BILD];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_BUCHUNGSLISTE_KOMMENTAR];
			} else if ("LosStatus".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_BUCHUNGSLISTE_LOS_STATUS];
			} else if ("MengeGenehmigt".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_BUCHUNGSLISTE_MENGE_GENEHMIGT];
			} else if ("MengeGebucht".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_BUCHUNGSLISTE_MENGE_GEBUCHT];
			} else if ("Lager".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_BUCHUNGSLISTE_LAGER];
			}

		}
			break;
		case UC_BEDARFSUEBERNAHME_OFFENE: {
			if ("Abgang".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_OFFENE_ABGANG];
			} else if ("ArtikelbezeichnungHV".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_OFFENE_ARTIKELBEZEICHNUNG_HV];
			} else if ("ArtikelbezeichnungManuell".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_OFFENE_ARTIKELBEZEICHNUNG_MANUELL];
			} else if ("ArtikelnummerHV".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_OFFENE_ARTIKELNUMMER_HV];
			} else if ("ArtikelnummerManuell".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_OFFENE_ARTIKELNUMMER_MANUELL];
			} else if ("Auftrag".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_OFFENE_AUFTRAG];
			} else if ("Kunde".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_OFFENE_KUNDE];
			} else if ("LosProjekt".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_OFFENE_LOS_PROJEKT];
			} else if ("LosnummerHV".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_OFFENE_LOSNUMMER_HV];
			} else if ("LosnummerManuell".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_OFFENE_LOSNUMMER_MANUELL];
			} else if ("Person".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_OFFENE_PERSON];
			} else if ("StuecklisteBezeichnung".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_OFFENE_STUECKLISTENBEZEICHNUNG];
			} else if ("Stueckliste".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_OFFENE_STUECKLISTENNUMMER];
			} else if ("Wunschmenge".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_OFFENE_WUNSCHMENGE];
			} else if ("Wunschtermin".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_OFFENE_WUNSCHTERMIN];
			} else if ("Zusaetzlich".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_OFFENE_ZUSAETZLICH];
			} else if ("Bild".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_OFFENE_BILD];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_OFFENE_KOMMENTAR];
			} else if ("LosStatus".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_OFFENE_LOS_STATUS];
			} else if ("MengeGenehmigt".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_OFFENE_MENGE_GENEHMIGT];
			} else if ("MengeGebucht".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_OFFENE_MENGE_GEBUCHT];
			} else if ("Lager".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_OFFENE_LAGER];
			} else if ("ArtikelEinheit".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_OFFENE_ARTIKELEINHEIT];
			} else if ("LagerstandLoslaeger".equals(fieldName)) {
				value = data[index][BEDARFSUEBERNAHME_OFFENE_LAGERSTAND_LOSLAEGER];
			}

		}
			break;
		case UC_AUSLASTUNGSVORSCHAU: {
			if ("SollzeitBeginn".equals(fieldName)) {
				value = data[index][AV_SOLLZEITBEGINN];
			} else if ("SollzeitEnde".equals(fieldName)) {
				value = data[index][AV_SOLLZEITENDE];
			} else if ("Verfuegbarkeit".equals(fieldName)) {
				value = data[index][AV_PERSONALVERFUEGBARKEIT];
			} else if ("Datum".equals(fieldName)) {
				value = data[index][AV_DATUM];
			} else if ("Artikelgruppe".equals(fieldName)) {
				value = data[index][AV_ARTIKELGRUPPE];
			}
		}
			break;
		case UC_AUSLASTUNGSVORSCHAU_DETAILIERT: {
			if ("Arbeitsgang".equals(fieldName)) {
				value = data[index][AV_DETAIL_ARBEITSGANG];
			} else if ("Artikelgruppe".equals(fieldName)) {
				value = data[index][AV_DETAIL_ARTIKELGRUPPE];
			} else if ("Artikelnummer".equals(fieldName)) {
				value = data[index][AV_DETAIL_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][AV_DETAIL_BEZEICHNUNG];
			} else if ("BeginnDatum".equals(fieldName)) {
				value = data[index][AV_DETAIL_BEGINN_DATUM];
			} else if ("Dauer".equals(fieldName)) {
				value = data[index][AV_DETAIL_DAUER];
			} else if ("Unterarbeitsgang".equals(fieldName)) {
				value = data[index][AV_DETAIL_UNTERARBEITSGANG];
			} else if ("Verfuegbarkeit".equals(fieldName)) {
				value = data[index][AV_DETAIL_VERFUEGBARKEIT];
			} else if ("Losnummer".equals(fieldName)) {
				value = data[index][AV_DETAIL_LOSNUMMER];
			} else if ("Kunde".equals(fieldName)) {
				value = data[index][AV_DETAIL_KUNDE];
			} else if ("Auftrag".equals(fieldName)) {
				value = data[index][AV_DETAIL_AUFTRAG];
			} else if ("Projekt".equals(fieldName)) {
				value = data[index][AV_DETAIL_PROJEKT];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][AV_DETAIL_KOMMENTAR];
			}

		}
			break;
		case UC_HALBFERTIGFABRIKATSINVENTUR: {
			if ("Losnummer".equals(fieldName)) {
				value = data[index][HF_LOSNUMMER];
			} else if ("Artikelnummer".equals(fieldName)) {
				value = data[index][HF_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][HF_BEZEICHNUNG];
			} else if ("Kurzbezeichnung".equals(fieldName)) {
				value = data[index][HF_KURZBEZEICHNUNG];
			} else if ("Referenznummer".equals(fieldName)) {
				value = data[index][HF_REFERENZNUMMER];
			} else if ("Losgroesse".equals(fieldName)) {
				value = data[index][HF_LOSGROESSE];
			} else if ("Erledigt".equals(fieldName)) {
				value = data[index][HF_ERLEDIGT];
			} else if ("PositionArtikelnummer".equals(fieldName)) {
				value = data[index][HF_POSITION_ARTIKELNUMMMER];
			} else if ("PositionReferenznummer".equals(fieldName)) {
				value = data[index][HF_POSITION_REFERENZNUMMER];
			} else if ("PositionBezeichnung".equals(fieldName)) {
				value = data[index][HF_POSITION_BEZEICHNUNG];
			} else if ("PositionAbgeliefert".equals(fieldName)) {
				value = data[index][HF_POSITION_ABGELIFERT];
			} else if ("PositionAusgegeben".equals(fieldName)) {
				value = data[index][HF_POSITION_AUSGEGEBEN];
			} else if ("PositionOffen".equals(fieldName)) {
				value = data[index][HF_POSITION_OFFEN];
			} else if ("PositionPreis".equals(fieldName)) {
				value = data[index][HF_POSITION_PREIS];
			} else if ("PositionAbgeliefertMaschine".equals(fieldName)) {
				value = data[index][HF_POSITION_ABGELIFERT_MASCHINE];
			} else if ("PositionAusgegebenMaschine".equals(fieldName)) {
				value = data[index][HF_POSITION_AUSGEGEBEN_MASCHINE];
			} else if ("PositionOffenMaschine".equals(fieldName)) {
				value = data[index][HF_POSITION_OFFEN_MASCHINE];
			} else if ("PositionPreisMaschine".equals(fieldName)) {
				value = data[index][HF_POSITION_PREIS_MASCHINE];
			} else if ("PositionEinkaufspreis".equals(fieldName)) {
				value = data[index][HF_POSITION_EKPREIS];
			} else if ("PositionArtikelverwendung".equals(fieldName)) {
				value = data[index][HF_POSITION_ARTIKELVERWENDUNG];
			} else if ("Auftragsnummer".equals(fieldName)) {
				value = data[index][HF_AUFTRAGSNUMMER];
			} else if ("Fertigungsgruppe".equals(fieldName)) {
				value = data[index][HF_FERTIGUNGSGRUPPE];
			}

			else if ("Auftragsstatus".equals(fieldName)) {
				value = data[index][HF_AUFTRAGSSTATUS];
			} else if ("Auftragspositionsstatus".equals(fieldName)) {
				value = data[index][HF_AUFTRAGSPOSITIONSSTATUS];
			} else if ("Auftragskunde".equals(fieldName)) {
				value = data[index][HF_AUFTRAGSKUNDE];
			} else if ("Auftragskundekurzbezeichnung".equals(fieldName)) {
				value = data[index][HF_AUFTRAGSKUNDEKURZBEZEICHNUNG];
			} else if ("PositionNurMaschinenzeit".equals(fieldName)) {
				value = data[index][HF_POSITION_NUR_MASCHINENZEIT];
			} else if ("PositionArbeitsgang".equals(fieldName)) {
				value = data[index][HF_POSITION_ARBEITSGANG];
			} else if ("PositionMaschinenbezeichnung".equals(fieldName)) {
				value = data[index][HF_POSITION_MASCHINENBEZEICHNUNG];
			} else if ("PositionMaschinenidentifikationsnummer".equals(fieldName)) {
				value = data[index][HF_POSITION_MASCHINENIDENTIFIKATIONSNUMMER];
			} else if ("PositionMaschinenSeriennummer".equals(fieldName)) {
				value = data[index][HF_POSITION_MASCHINENSERIENNUMMER];
			} else if ("PositionMaschineninventarnummer".equals(fieldName)) {
				value = data[index][HF_POSITION_MASCHINENINVENTARNUMMER];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][HF_EINHEIT];
			} else if ("PositionEinheit".equals(fieldName)) {
				value = data[index][HF_POSITION_EINHEIT];
			} else if ("PositionUmrechnungsfaktor".equals(fieldName)) {
				value = data[index][HF_POSITION_UMRECHNUNGSFAKTOR];
			} else if ("PositionBestellmengeneinheit".equals(fieldName)) {
				value = data[index][HF_POSITION_BESTELLMENGENEINHEIT];
			} else if ("Umrechnungsfaktor".equals(fieldName)) {
				value = data[index][HF_UMRECHNUNGSFAKTOR];
			} else if ("Bestellmengeneinheit".equals(fieldName)) {
				value = data[index][HF_BESTELLMENGENEINHEIT];
			}

		}
			break;
		case UC_OFFENE: {
			if ("F_AUFTRAGNUMMER".equals(fieldName)) {
				value = data[index][OFFENE_AUFTRAGSNUMMER];
			} else if ("F_AUFTRAGSSTATUS".equals(fieldName)) {
				value = data[index][OFFENE_AUFTRAGSSTATUS];
			} else if ("F_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][OFFENE_BEZEICHNUNG];
			} else if ("F_GELIEFERT".equals(fieldName)) {
				value = data[index][OFFENE_GELIEFERT];
			} else if ("F_ARTIKELNUMMMER".equals(fieldName)) {
				value = data[index][OFFENE_ARTIKELNUMMER];
			} else if ("F_DETAILBEDARF".equals(fieldName)) {
				value = data[index][OFFENE_DETAILBEDARF];
			} else if ("F_KALENDERWOCHE".equals(fieldName)) {
				value = data[index][OFFENE_KALENDERWOCHE];
			} else if ("F_LOSGROESSE".equals(fieldName)) {
				value = data[index][OFFENE_LOSGROESSE];
			} else if ("F_LOSNUMMER".equals(fieldName)) {
				value = data[index][OFFENE_LOSNUMMER];
			} else if ("F_LOSKLASSEN".equals(fieldName)) {
				value = data[index][OFFENE_LOSKLASSEN];
			} else if ("F_MATERIAL".equals(fieldName)) {
				value = data[index][OFFENE_MATERIAL];
			} else if ("F_BEGINN".equals(fieldName)) {
				value = data[index][OFFENE_BEGINN];
			} else if ("F_ENDE".equals(fieldName)) {
				value = data[index][OFFENE_ENDE];
			} else if (F_ZUSATZBEZEICHNUNG.equals(fieldName)) {
				value = data[index][OFFENE_ZUSATZBEZEICHNUNG];
			} else if ("F_KUNDE".equals(fieldName)) {
				value = data[index][OFFENE_KUNDE];
			} else if ("F_KUNDE_KBEZ".equals(fieldName)) {
				value = data[index][OFFENE_KUNDE_KBEZ];
			} else if ("F_PROJEKT".equals(fieldName)) {
				value = data[index][OFFENE_PROJEKT];
			} else if ("F_FERTIGUNGSGRUPPE".equals(fieldName)) {
				value = data[index][OFFENE_FERTIGUNGSGRUPPE];
			} else if ("F_FEHLMENGE".equals(fieldName)) {
				value = data[index][OFFENE_FEHLMENGE];
			} else if ("F_RESERVIERUNGEN".equals(fieldName)) {
				value = data[index][OFFENE_RESERVIERUNGEN];
			} else if ("F_RAHMENBESTELLT".equals(fieldName)) {
				value = data[index][OFFENE_RAHMEN_BESTELLT];
			} else if ("F_AUFTRAGSPOENALE".equals(fieldName)) {
				value = data[index][OFFENE_AUFTRAGSPOENALE];
			} else if ("F_FERTIGUNGSZEIT".equals(fieldName)) {
				value = data[index][OFFENE_FERTIGUNGSZEIT];
			} else if ("F_LOSSTATUS".equals(fieldName)) {
				value = data[index][OFFENE_LOSSTATUS];
			} else if ("F_LIEFERTERMIN".equals(fieldName)) {
				value = data[index][OFFENE_LIEFERTERMIN];
			} else if ("F_RAHMENRESERVIERUNGEN".equals(fieldName)) {
				value = data[index][OFFENE_RAHMENRESERVIERUNGEN];
			} else if ("F_LOSHATFEHLMENGE".equals(fieldName)) {
				value = data[index][OFFENE_LOSHATFEHLMENGE];
			} else if ("F_FORECAST_KUNDE_LIEFERADRESSE_KBEZ".equals(fieldName)) {
				value = data[index][OFFENE_FORECAST_KUNDE_LIEFERADRESSE_KBEZ];
			} else if ("F_FORECAST_NUMMER".equals(fieldName)) {
				value = data[index][OFFENE_FORECAST_NUMMER];
			} else if ("F_FORECASTAUFTRAG_ANLAGEDATUM".equals(fieldName)) {
				value = data[index][OFFENE_FORECASTAUFTRAG_ANLAGEDATUM];
			} else if ("F_FORECASTPOSITION_TERMIN".equals(fieldName)) {
				value = data[index][OFFENE_FORECASTPOSITION_TERMIN];
			} else if ("F_IN_FERTIGUNG".equals(fieldName)) {
				value = data[index][OFFENE_IN_FERTIGUNG];
			} else if ("F_BESTELLT".equals(fieldName)) {
				value = data[index][OFFENE_BESTELLT];
			} else if ("F_LAGERSTAND_SPERRLAEGER".equals(fieldName)) {
				value = data[index][OFFENE_LAGERSTAND_SPERRLAEGER];
			} else if ("F_LAGERSTAND_MITKONSIGNATIONSLAGER".equals(fieldName)) {
				value = data[index][OFFENE_LAGERSTAND_MITKONSIGNATIONSLAGER];
			} else if ("F_LAGERSTAND_OHNEKONSIGNATIONSLAGER".equals(fieldName)) {
				value = data[index][OFFENE_LAGERSTAND_OHNEKONSIGNATIONSLAGER];
			}

		}
			break;
		case UC_OFFENE_AG: {
			if ("F_AUFTRAGNUMMER".equals(fieldName)) {
				value = data[index][OFFENE_AG_AUFTRAGSNUMMER];
			} else if ("F_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][OFFENE_AG_BEZEICHNUNG];
			} else if ("F_GELIEFERT".equals(fieldName)) {
				value = data[index][OFFENE_AG_GELIEFERT];
			} else if ("F_ARTIKELNUMMMER".equals(fieldName)) {
				value = data[index][OFFENE_AG_ARTIKELNUMMER];
			} else if ("F_DETAILBEDARF".equals(fieldName)) {
				value = data[index][OFFENE_AG_DETAILBEDARF];
			} else if ("F_KALENDERWOCHE".equals(fieldName)) {
				value = data[index][OFFENE_AG_KALENDERWOCHE];
			} else if ("F_LOSGROESSE".equals(fieldName)) {
				value = data[index][OFFENE_AG_LOSGROESSE];
			} else if ("F_LOSNUMMER".equals(fieldName)) {
				value = data[index][OFFENE_AG_LOSNUMMER];
			} else if ("F_MATERIAL".equals(fieldName)) {
				value = data[index][OFFENE_AG_MATERIAL];
			} else if ("F_BEGINN".equals(fieldName)) {
				value = data[index][OFFENE_AG_BEGINN];
			} else if ("F_ENDE".equals(fieldName)) {
				value = data[index][OFFENE_AG_ENDE];
			} else if (F_ZUSATZBEZEICHNUNG.equals(fieldName)) {
				value = data[index][OFFENE_AG_ZUSATZBEZEICHNUNG];
			} else if ("F_KUNDE".equals(fieldName)) {
				value = data[index][OFFENE_AG_KUNDE];
			} else if ("F_PROJEKT".equals(fieldName)) {
				value = data[index][OFFENE_AG_PROJEKT];
			} else if ("F_FERTIGUNGSGRUPPE".equals(fieldName)) {
				value = data[index][OFFENE_AG_FERTIGUNGSGRUPPE];
			} else if ("F_FEHLMENGE".equals(fieldName)) {
				value = data[index][OFFENE_AG_FEHLMENGE];
			} else if ("F_RESERVIERUNGEN".equals(fieldName)) {
				value = data[index][OFFENE_AG_RESERVIERUNGEN];
			} else if (F_ARTIKELRAHMENMENGE_OFFEN.equals(fieldName)) {
				value = data[index][OFFENE_AG_RAHMEN_BESTELLT];
			} else if ("F_AUFTRAGSPOENALE".equals(fieldName)) {
				value = data[index][OFFENE_AG_AUFTRAGSPOENALE];
			} else if ("F_FERTIGUNGSZEIT".equals(fieldName)) {
				value = data[index][OFFENE_AG_FERTIGUNGSZEIT];
			} else if ("F_LOSSTATUS".equals(fieldName)) {
				value = data[index][OFFENE_AG_LOSSTATUS];
			} else if ("F_LOSZUSATZSTATUS_P1".equals(fieldName)) {
				value = data[index][OFFENE_AG_LOSZUSATZSTATUS_P1];
			} else if ("F_LIEFERTERMIN".equals(fieldName)) {
				value = data[index][OFFENE_AG_LIEFERTERMIN];
			} else if ("F_RAHMENRESERVIERUNGEN".equals(fieldName)) {
				value = data[index][OFFENE_AG_RAHMENRESERVIERUNGEN];
			} else if ("F_LOSHATFEHLMENGE".equals(fieldName)) {
				value = data[index][OFFENE_AG_LOSHATFEHLMENGE];
			} else if ("F_AG_ARTIKELNUMMER".equals(fieldName)) {
				value = data[index][OFFENE_AG_AG_ARTIKEL];
			} else if ("F_AG_ARTIKELGRUPPE".equals(fieldName)) {
				value = data[index][OFFENE_AG_AG_ARTIKELGRUPPPE];
			} else if ("F_AG_AG_BEGINN".equals(fieldName)) {
				value = data[index][OFFENE_AG_AG_BEGINN];
			} else if ("F_AG_AGNUMMER".equals(fieldName)) {
				value = data[index][OFFENE_AG_AGNUMMER];
			} else if ("F_AG_UAGNUMMER".equals(fieldName)) {
				value = data[index][OFFENE_AG_UAGNUMMER];
			}

			else if ("F_AG_ARTIKELBEZEICHNUNG".equals(fieldName)) {
				value = data[index][OFFENE_AG_AG_ARTIKELBEZEICHNUNG];
			} else if ("F_AG_GESAMTZEIT".equals(fieldName)) {
				value = data[index][OFFENE_AG_AG_GESAMTZEIT];
			} else if ("F_AG_ISTTZEIT".equals(fieldName)) {
				value = data[index][OFFENE_AG_AG_ISTZEIT];
			} else if ("F_AG_MASCHINE_IDENTIFIKATIONSNUMMMER".equals(fieldName)) {
				value = data[index][OFFENE_AG_AG_MASCHINE_IDENTIFIKATIONSNUMMMER];
			} else if ("F_AG_MASCHINE_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][OFFENE_AG_AG_MASCHINE_BEZEICHNUNG];
			} else if ("F_AG_MASCHINE_INVENTARNUMMMER".equals(fieldName)) {
				value = data[index][OFFENE_AG_AG_MASCHINE_INVENTARNUMMMER];
			} else if ("F_FORTSCHRITT".equals(fieldName)) {
				value = data[index][OFFENE_AG_FORTSCHRITT];
			}

		}
			break;
		case UC_AUSLIEFERLISTE: {
			if ("F_AUFTRAGNUMMER".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_AUFTRAGSNUMMER];
			} else if ("F_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_BEZEICHNUNG];
			} else if ("F_GELIEFERT".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_GELIEFERT];
			} else if ("F_ARTIKELNUMMMER".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_ARTIKELNUMMER];
			} else if ("F_LAGERSTAND".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_LAGERSTAND];
			} else if ("F_KALENDERWOCHE".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_KALENDERWOCHE];
			} else if ("F_LOSGROESSE".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_LOSGROESSE];
			} else if ("F_LOSNUMMER".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_LOSNUMMER];
			} else if ("F_MATERIAL_SUBREPORT".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_MATERIAL_SUBREPORT];
			} else if ("F_BEGINN".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_BEGINN];
			} else if ("F_ENDE".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_ENDE];
			} else if (F_ZUSATZBEZEICHNUNG.equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_ZUSATZBEZEICHNUNG];
			} else if ("F_ZUSATZBEZEICHNUNG2".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_ZUSATZBEZEICHNUNG2];
			} else if ("F_KUNDE".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_KUNDE];
			} else if ("F_PROJEKT".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_PROJEKT];
			} else if ("F_FERTIGUNGSGRUPPE".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_FERTIGUNGSGRUPPE];
			} else if ("F_FEHLMENGE".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_FEHLMENGE];
			} else if ("F_RESERVIERUNGEN".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_RESERVIERUNGEN];
			} else if (F_ARTIKELRAHMENMENGE_OFFEN.equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_RAHMEN_BESTELLT];
			} else if ("F_AUFTRAGSPOENALE".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_AUFTRAGSPOENALE];
			} else if ("F_FERTIGUNGSZEIT".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_FERTIGUNGSZEIT];
			} else if ("F_LOSSTATUS".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_LOSSTATUS];
			} else if ("F_LIEFERTERMIN".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_LIEFERTERMIN];
			} else if ("F_RAHMENRESERVIERUNGEN".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_RAHMENRESERVIERUNGEN];
			} else if ("F_LOSHATFEHLMENGE".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_LOSHATFEHLMENGE];
			} else if ("F_ARBEITSGAENGE".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_ARBEITSGAENGE];
			} else if ("F_AUFTRAGSPOSITIONNUMMER".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_AUFTRAGSPOSITIONNUMMER];
			} else if ("F_AUFTRAGARTIKEL".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_AUFTRAGARTIKEL];
			} else if ("F_AUFTRAGARTIKEL_LAGERSTAND".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_AUFTRAGARTIKEL_LAGERSTAND];
			} else if ("F_AUFTRAGARTIKELBEZEICHNUNG".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_AUFTRAGARTIKELBEZEICHNUNG];
			} else if ("F_AUFTRAGARTIKELGRUPPE".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_AUFTRAGARTIKELGRUPPE];
			} else if ("F_AUFTRAGARTIKELKLASSE".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_AUFTRAGARTIKELKLASSE];
			} else if ("F_AUFTRAGPOSITIONSMENGE".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_AUFTRAGPOSITIONSMENGE];
			} else if ("F_AUFTRAGART".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_AUFTRAGART];
			} else if ("F_AUFTRAGSKUNDE_NAME1".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_AUFTRAGSKUNDE_NAME1];
			} else if ("F_AUFTRAGSKUNDE_NAME2".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_AUFTRAGSKUNDE_NAME2];
			} else if ("F_AUFTRAGSKUNDE_STRASSE".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_AUFTRAGSKUNDE_STRASSE];
			} else if ("F_AUFTRAGSKUNDE_ORT".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_AUFTRAGSKUNDE_ORT];
			} else if ("F_AUFTRAGSKUNDE_LKZ".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_AUFTRAGSKUNDE_LKZ];
			} else if ("F_AUFTRAGSKUNDE_PLZ".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_AUFTRAGSKUNDE_PLZ];
			} else if ("F_AUFTRAGSKUNDE_TELEFON".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_AUFTRAGSKUNDE_TELEFON];
			} else if ("F_AUFTRAGSKUNDE_ANSPRECHPARTNER".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_AUFTRAGSKUNDE_ANSPRECHPARTNER];
			} else if ("F_AUFTRAGSKUNDE_ANSPRECHPARTNER_TELEFONDW".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_AUFTRAGSKUNDE_ANSPRECHPARTNER_TELEFONDW];
			} else if ("F_AUFTRAGSKUNDE_AUFTRAG_POSITIONSTERMIN".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_AUFTRAG_POSITIONSTERMIN];
			} else if ("F_MATERIALLISTE".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_MATERIALLISTE];
			} else if ("F_PROJEKTKOMMENTAR".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_PROJEKTKOMMENTAR];
			} else if ("F_LOSARTIKELGRUPPE".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_LOSARTIKELGRUPPE];
			} else if ("F_LOSARTIKELKLASSE".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_LOSARTIKELKLASSE];
			} else if ("F_AUFTRAG_LIEFERADRESSE".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_AUFTRAG_LIEFERADRESSE];
			} else if ("F_AUFTRAG_LIEFERART".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_AUFTRAG_LIEFERART];
			} else if ("F_AUFTRAG_SPEDITEUR".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_AUFTRAG_SPEDITEUR];
			} else if ("F_ABLIEFERDATUM".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_ABLIEFERDATUM];
			} else if ("F_AUFTRAG_OFFENERAHMENMENGE".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_AUFTRAG_OFFENERAHMENMENGE];
			} else if ("F_LOSKLASSEN".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_LOSKLASSEN];
			} else if ("F_ZUWENIG_AUF_LAGER_ZUM_ZEITPUNKT".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_ZUWENIG_AUF_LAGER_ZUM_ZEITPUNKT];
			} else if ("F_AUFTRAG_OFFENEMENGE".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_AUFTRAG_OFFENEMENGE];
			}
		}
			break;
		case UC_ALLE: {
			if ("F_ARBEITSZEITISTMENGE".equals(fieldName)) {
				value = data[index][ALLE_ARBEITSZEITISTMENGE];
			} else if ("F_ARBEITSZEITISTPREIS".equals(fieldName)) {
				value = data[index][ALLE_ARBEITSZEITISTPREIS];
			} else if ("F_ARBEITSZEITSOLLMENGE".equals(fieldName)) {
				value = data[index][ALLE_ARBEITSZEITSOLLMENGE];
			} else if ("F_ARBEITSZEITSOLLPREIS".equals(fieldName)) {
				value = data[index][ALLE_ARBEITSZEITSOLLPREIS];
			} else if ("F_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][ALLE_BEZEICHNUNG];
			} else if ("F_IDENT".equals(fieldName)) {
				value = data[index][ALLE_IDENT];
			} else if ("F_LOSNUMMER".equals(fieldName)) {
				value = data[index][ALLE_LOSNUMMER];
			} else if ("F_MATERIALISTMENGE".equals(fieldName)) {
				value = data[index][ALLE_MATERIALISTMENGE];
			} else if ("F_MATERIALISTPREIS".equals(fieldName)) {
				value = data[index][ALLE_MATERIALISTPREIS];
			} else if ("F_MATERIALSOLLMENGE".equals(fieldName)) {
				value = data[index][ALLE_MATERIALSOLLMENGE];
			} else if ("F_MATERIALSOLLPREIS".equals(fieldName)) {
				value = data[index][ALLE_MATERIALSOLLPREIS];
			} else if (F_ZUSATZBEZEICHNUNG.equals(fieldName)) {
				value = data[index][ALLE_ZUSATZBEZEICHNUNG];
			} else if ("F_BEGINNTERMIN".equals(fieldName)) {
				value = data[index][ALLE_BEGINNTERMIN];
			} else if ("F_AUFTRAGLIEFERTERMIN".equals(fieldName)) {
				value = data[index][ALLE_AUFTRAGLIEFERTERMIN];
			} else if ("F_LOSGROESSE".equals(fieldName)) {
				value = data[index][ALLE_LOSGROESSE];
			} else if ("F_OFFENERAHMENRESERVIERUNGEN".equals(fieldName)) {
				value = data[index][ALLE_OFFENERAHMENRESERVIERUNGEN];
			} else if ("F_AUFTRAGSNUMMER".equals(fieldName)) {
				value = data[index][ALLE_AUFTRAGSNUMMER];
			} else if ("F_KUNDE".equals(fieldName)) {
				value = data[index][ALLE_KUNDE];
			} else if ("F_FERTIGUNGSGRUPPE".equals(fieldName)) {
				value = data[index][ALLE_FERTIGUNGSGRUPPE];
			} else if ("F_ENDETERMIN".equals(fieldName)) {
				value = data[index][ALLE_ENDETERMIN];
			} else if ("F_STATUS".equals(fieldName)) {
				value = data[index][ALLE_STATUS];
			} else if ("F_ABGELIEFERT".equals(fieldName)) {
				value = data[index][ALLE_ABGELIEFERT];
			}
		}
			break;
		case UC_MONATSAUSWERTUNG: {
			if ("Kunde".equals(fieldName)) {
				value = data[index][MONATSAUSWERTUNG_KUNDE];
			} else if ("Sollzeit".equals(fieldName)) {
				value = data[index][MONATSAUSWERTUNG_SOLLZEIT];
			} else if ("Istzeit".equals(fieldName)) {
				value = data[index][MONATSAUSWERTUNG_ISTZEIT];
			} else if ("Losgroesse".equals(fieldName)) {
				value = data[index][MONATSAUSWERTUNG_LOSGROESSE];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][MONATSAUSWERTUNG_BEZEICHNUNG];
			} else if ("Abweichung".equals(fieldName)) {
				value = data[index][MONATSAUSWERTUNG_ABWEICHUNG];
			} else if ("Losnummer".equals(fieldName)) {
				value = data[index][MONATSAUSWERTUNG_LOSNUMMER];
			} else if ("Fertigungsgruppe".equals(fieldName)) {
				value = data[index][MONATSAUSWERTUNG_FERTIGUNGSGRUPPE];
			} else if ("Artikelnummer".equals(fieldName)) {
				value = data[index][MONATSAUSWERTUNG_ARTIKELNUMMER];
			} else if ("Bewertung".equals(fieldName)) {
				value = data[index][MONATSAUSWERTUNG_BEWERTUNG];
			}

		}
			break;

		case UC_AUFLOESBARE_FEHLMENGEN: {
			if ("F_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][AFM_ARTIKELBEZEICHNUNG];
			} else if ("F_IDENT".equals(fieldName)) {
				value = data[index][AFM_ARTIKELNUMMER];
			} else if ("F_EINHEIT".equals(fieldName)) {
				value = data[index][AFM_EINHEIT];
			} else if ("F_FEHLMENGE".equals(fieldName)) {
				value = data[index][AFM_FEHLMENGE];
			} else if ("F_LAGERSTAND".equals(fieldName)) {
				value = data[index][AFM_LAGERSTAND];
			} else if ("F_BELEGNUMMER".equals(fieldName)) {
				value = data[index][AFM_BELEGNUMMER];
			} else if ("F_PROJEKT".equals(fieldName)) {
				value = data[index][AFM_PROJEKT];
			} else if ("F_STUECKLISTEBEZEICHNUNG".equals(fieldName)) {
				value = data[index][AFM_STUECKLISTEBEZEICHNUNG];
			} else if ("F_STUECKLISTEIDENT".equals(fieldName)) {
				value = data[index][AFM_STUECKLISTENUMMER];
			} else if ("F_TERMIN".equals(fieldName)) {
				value = data[index][AFM_TERMIN];
			} else if ("F_RESERVIERT".equals(fieldName)) {
				value = data[index][AFM_RESERVIERT];
			} else if ("F_BESTELLT".equals(fieldName)) {
				value = data[index][AFM_BESTELLT];
			} else if ("F_OFFEN".equals(fieldName)) {
				value = data[index][AFM_OFFEN];
			} else if ("F_ABNUMMER".equals(fieldName)) {
				value = data[index][AFM_ABNUMMER];
			} else if ("F_MAHNSTUFE".equals(fieldName)) {
				value = data[index][AFM_MAHNSTUFE];
			} else if ("F_STUECKLISTEZUSATZBEZEICHNUNG".equals(fieldName)) {
				value = data[index][AFM_STUECKLISTEZUSATZBEZEICHNUNG];
			} else if (F_ZUSATZBEZEICHNUNG.equals(fieldName)) {
				value = data[index][AFM_ARTIKELZUSATZBEZEICHNUNG];
			} else if ("F_FIKTIVERLAGERSTAND".equals(fieldName)) {
				value = data[index][AFM_FIKTIVERLAGERSTAND];
			}
		}
			break;
		case UC_FEHLMENGEN_ALLER_LOSE: {
			if ("F_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][FMAL_ARTIKELBEZEICHNUNG];
			} else if ("F_IDENT".equals(fieldName)) {
				value = data[index][FMAL_ARTIKELNUMMER];
			} else if ("F_ARTIKELREFERENZNUMMER".equals(fieldName)) {
				value = data[index][FMAL_ARTIKELREFERENZNUMMER];
			} else if ("F_EINHEIT".equals(fieldName)) {
				value = data[index][FMAL_EINHEIT];
			} else if ("F_FEHLMENGE".equals(fieldName)) {
				value = data[index][FMAL_FEHLMENGE];
			} else if ("F_LAGERSTAND".equals(fieldName)) {
				value = data[index][FMAL_LAGERSTAND];
			} else if ("F_BELEGNUMMER".equals(fieldName)) {
				value = data[index][FMAL_BELEGNUMMER];
			} else if ("F_POENALE_AUS_AUFTRAG".equals(fieldName)) {
				value = data[index][FMAL_POENALE_AUS_AUFTRAG];
			} else if ("F_DRINGEND".equals(fieldName)) {
				value = data[index][FMAL_DRINGEND];
			} else if ("F_BELEGART".equals(fieldName)) {
				value = data[index][FMAL_BELEGART];
			} else if ("F_STUECKLISTEBEZEICHNUNG".equals(fieldName)) {
				value = data[index][FMAL_STUECKLISTEBEZEICHNUNG];
			} else if ("F_STUECKLISTEIDENT".equals(fieldName)) {
				value = data[index][FMAL_STUECKLISTENUMMER];
			} else if ("F_STUECKLISTEREFERENZNUMMER".equals(fieldName)) {
				value = data[index][FMAL_STUECKLISTEREFERENZNUMMER];
			} else if ("F_TERMIN".equals(fieldName)) {
				value = data[index][FMAL_TERMIN];
			} else if ("F_PRODUKTIONSBEGINN".equals(fieldName)) {
				value = data[index][FMAL_PRODUKTIONSBEGINN];
			} else if ("F_PRODUKTIONSENDE".equals(fieldName)) {
				value = data[index][FMAL_PRODUKTIONSENDE];
			} else if ("F_TERMIN".equals(fieldName)) {
				value = data[index][FMAL_TERMIN];
			} else if ("F_ABTERMIN".equals(fieldName)) {
				value = data[index][FMAL_ABTERMIN];
			} else if ("F_FIKTIVERLAGERSTAND".equals(fieldName)) {
				value = data[index][FMAL_FIKTIVERLAGERSTAND];
			} else if ("F_RESERVIERT".equals(fieldName)) {
				value = data[index][FMAL_RESERVIERT];
			} else if ("F_BESTELLT".equals(fieldName)) {
				value = data[index][FMAL_BESTELLT];
			} else if ("F_OFFEN".equals(fieldName)) {
				value = data[index][FMAL_OFFEN];
			} else if ("F_ABNUMMER".equals(fieldName)) {
				value = data[index][FMAL_ABNUMMER];
			} else if ("F_MAHNSTUFE".equals(fieldName)) {
				value = data[index][FMAL_MAHNSTUFE];
			} else if ("F_STUECKLISTEZUSATZBEZEICHNUNG".equals(fieldName)) {
				value = data[index][FMAL_STUECKLISTEZUSATZBEZEICHNUNG];
			} else if (F_ZUSATZBEZEICHNUNG.equals(fieldName)) {
				value = data[index][FMAL_ARTIKELZUSATZBEZEICHNUNG];
			} else if ("F_FIKTIVERLAGERSTAND".equals(fieldName)) {
				value = data[index][FMAL_FIKTIVERLAGERSTAND];
			} else if ("F_LIEFERANT".equals(fieldName)) {
				value = data[index][FMAL_LIEFERANT];
			} else if ("F_ARTIKELSPERREN".equals(fieldName)) {
				value = data[index][FMAL_ARTIKELSPERREN];
			} else if ("ArtikelAnzahlAngeboten".equals(fieldName)) {
				value = data[index][FMAL_ARTIKEL_ANZAHL_ANGEBOTEN];
			} else if ("ArtikelAnzahlAngefragt".equals(fieldName)) {
				value = data[index][FMAL_ARTIKEL_ANZAHL_ANGEFRAGT];
			}
		}
			break;
		case UC_STUECKRUECKMELDUNG: {
			if ("F_ARBEITSGANG".equals(fieldName)) {
				value = data[index][SRM_ARBEITSGANG];
			}
			if ("F_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][SRM_BEZEICHNUNG];
			} else if ("F_GEBUCHT".equals(fieldName)) {
				value = data[index][SRM_GEBUCHT];
			} else if ("F_GUT".equals(fieldName)) {
				value = data[index][SRM_GUT];
			} else if ("F_INARBEIT".equals(fieldName)) {
				value = data[index][SRM_INARBEIT];
			} else if ("F_IDENT".equals(fieldName)) {
				value = data[index][SRM_IDENT];
			} else if ("F_MASCHINE".equals(fieldName)) {
				value = data[index][SRM_MASCHINE];
			} else if ("F_OFFEN".equals(fieldName)) {
				value = data[index][SRM_OFFEN];
			} else if ("F_PERSONAL".equals(fieldName)) {
				value = data[index][SRM_PERSONAL];
			} else if ("F_SCHLECHT".equals(fieldName)) {
				value = data[index][SRM_SCHLECHT];
			} else if (F_ZUSATZBEZEICHNUNG.equals(fieldName)) {
				value = data[index][SRM_ZUSATZBEZEICHNUNG];
			}
		}
			break;
		case UC_LOSSTATISTIK: {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][LOSSTATISTIK_ARTIKELNUMMER];
			}
			if ("Bezeichnung".equals(fieldName)) {
				value = data[index][LOSSTATISTIK_ARTIKELBEZEICHNUNG];
			} else if ("PersonalMaschine".equals(fieldName)) {
				value = data[index][LOSSTATISTIK_PERSONALMASCHINE];
			} else if ("IstPerson".equals(fieldName)) {
				value = data[index][LOSSTATISTIK_ISTPERSON];
			} else if ("Sollpreis".equals(fieldName)) {
				value = data[index][LOSSTATISTIK_SOLLPREIS];
			} else if ("Sollmenge".equals(fieldName)) {
				value = data[index][LOSSTATISTIK_SOLLMENGE];
			} else if ("Istpreis".equals(fieldName)) {
				value = data[index][LOSSTATISTIK_ISTPREIS];
			} else if ("Istmenge".equals(fieldName)) {
				value = data[index][LOSSTATISTIK_ISTMENGE];
			} else if ("Gruppierung".equals(fieldName)) {
				value = data[index][LOSSTATISTIK_GRUPPIERUNG];
			} else if ("Gruppierungbezeichnung".equals(fieldName)) {
				value = data[index][LOSSTATISTIK_GRUPPIERUNGBEZEICHNUNG];
			} else if ("Gruppierungausgabe".equals(fieldName)) {
				value = data[index][LOSSTATISTIK_GRUPPIERUNGAUSGABE];
			} else if ("Gruppierungerledigt".equals(fieldName)) {
				value = data[index][LOSSTATISTIK_GRUPPIERUNGERLEDIGT];
			} else if ("Gruppierungartikel".equals(fieldName)) {
				value = data[index][LOSSTATISTIK_GRUPPIERUNGSTKLARTIKEL];
			} else if ("GruppierungartikelReferenznummer".equals(fieldName)) {
				value = data[index][LOSSTATISTIK_GRUPPIERUNGSTKLARTIKELREFERENZNUMMER];
			} else if ("Gruppierungkunde".equals(fieldName)) {
				value = data[index][LOSSTATISTIK_GRUPPIERUNGKUNDE];
			} else if ("Untergruppematerial".equals(fieldName)) {
				value = data[index][LOSSTATISTIK_UNTERGRUPPEMATERIAL];
			} else if ("Gruppierunglosgroesse".equals(fieldName)) {
				value = data[index][LOSSTATISTIK_GRUPPIERUNGLOSGROESSE];
			} else if ("Gruppierungabgeliefertemenge".equals(fieldName)) {
				value = data[index][LOSSTATISTIK_GRUPPIERUNABGELIEFERTEMENGE];
			} else if ("Gruppierungvkpreis".equals(fieldName)) {
				value = data[index][LOSSTATISTIK_GRUPPIERUNVKPREIS];
			} else if ("Buchungszeit".equals(fieldName)) {
				value = data[index][LOSSTATISTIK_BUCHUNGSZEIT];
			} else if ("GeplantesSollmaterial".equals(fieldName)) {
				value = data[index][LOSSTATISTIK_GEPLANTESSOLLMATERIAL];
			} else if ("Erledigungsdatum".equals(fieldName)) {
				value = data[index][LOSSTATISTIK_ERLEDIGUNGSDATUM];
			} else if ("Bewertung".equals(fieldName)) {
				value = data[index][LOSSTATISTIK_BEWERTUNG];
			}
		}
			break;
		case UC_NACHKALKULATION: {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][NACHKALKULATION_ARTIKELNUMMER];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][NACHKALKULATION_ARTIKELBEZEICHNUNG];
			} else if ("Gesamtist".equals(fieldName)) {
				value = data[index][NACHKALKULATION_GESAMTIST];
			} else if ("Gesamtsoll".equals(fieldName)) {
				value = data[index][NACHKALKULATION_GESAMTSOLL];
			} else if ("Maschinediff".equals(fieldName)) {
				value = data[index][NACHKALKULATION_MASCHINEDIFF];
			} else if ("Maschineist".equals(fieldName)) {
				value = data[index][NACHKALKULATION_MASCHINEIST];
			} else if ("Maschinesoll".equals(fieldName)) {
				value = data[index][NACHKALKULATION_MASCHINESOLL];
			} else if ("Personaldiff".equals(fieldName)) {
				value = data[index][NACHKALKULATION_PERSONALDIFF];
			} else if ("Personalist".equals(fieldName)) {
				value = data[index][NACHKALKULATION_PERSONALIST];
			} else if ("Personalsoll".equals(fieldName)) {
				value = data[index][NACHKALKULATION_PERSONALSOLL];
			} else if ("Fertig".equals(fieldName)) {
				value = data[index][NACHKALKULATION_FERTIG];
			} else if ("HoechsterArbeitsgang".equals(fieldName)) {
				value = data[index][NACHKALKULATION_HOECHSTER_ARBEITSGANG];
			}
		}
			break;
		case UC_GANZSEITIGESBILD: {
			if ("F_BILD".equals(fieldName)) {
				value = data[index][0];
			}

		}
			break;
		case UC_PRODUKTIONSINFORMATION: {
			if ("F_SCHLAGWORTE".equals(fieldName)) {
				value = data[index][PI_SCHLAGWORTE];
			} else if ("F_DATEINAME".equals(fieldName)) {
				value = data[index][PI_FILENAME];
			} else if ("F_NAME".equals(fieldName)) {
				value = data[index][PI_NAME];
			} else if ("F_BILD".equals(fieldName)) {
				value = data[index][PI_BILD];
			}
		}
			break;
		case UC_RANKINGLISTE: {
			if ("Fertigungsgruppe".equals(fieldName)) {
				value = data[index][RANKINGLISTE_FERTIGUNGSGRUPPE];
			} else if ("InfoU".equals(fieldName)) {
				value = data[index][RANKINGLISTE_INFO_U];
			} else if ("InfoL".equals(fieldName)) {
				value = data[index][RANKINGLISTE_INFO_L];
			} else if ("InfoK".equals(fieldName)) {
				value = data[index][RANKINGLISTE_INFO_K];
			} else if ("Artikelnummer".equals(fieldName)) {
				value = data[index][RANKINGLISTE_ARTIKELNUMMER];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][RANKINGLISTE_ARTIKELBEZEICHNUNG];
			} else if ("Losnummer".equals(fieldName)) {
				value = data[index][RANKINGLISTE_LOSNR];
			} else if ("Beginntermin".equals(fieldName)) {
				value = data[index][RANKINGLISTE_LOSBEGINNTERMIN];
			} else if ("Kunde".equals(fieldName)) {
				value = data[index][RANKINGLISTE_KUNDE];
			} else if ("Sollzeit".equals(fieldName)) {
				value = data[index][RANKINGLISTE_SOLLZEIT];
			} else if ("Offenelosmenge".equals(fieldName)) {
				value = data[index][RANKINGLISTE_OFFENELOSMENGE];
			}
		}
			break;
		case UC_ETIKETT: {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][ETI_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][ETI_BEZEICHNUNG];
			} else if ("Kurzbezeichnung".equals(fieldName)) {
				value = data[index][ETI_KURZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][ETI_ZUSATZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung2".equals(fieldName)) {
				value = data[index][ETI_ZUSATZBEZEICHNUNG2];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][ETI_EINHEIT];
			} else if ("Referenznummer".equals(fieldName)) {
				value = data[index][ETI_REFERENZNUMMER];
			} else if ("Istmenge".equals(fieldName)) {
				value = data[index][ETI_ISTMENGE];
			} else if ("Sollmenge".equals(fieldName)) {
				value = data[index][ETI_SOLLMENGE];
			}
		}
			break;
		case UC_TRACEIMPORT: {
			if ("F_ARTIKEL_MATERIAL".equals(fieldName)) {
				value = data[index][TRACEIMPORT_ARTIKEL_MATERIAL];
			} else if ("F_CHARGENNUMMER".equals(fieldName)) {
				value = data[index][TRACEIMPORT_CHARGENNUMMER];
			} else if ("F_FEHLER".equals(fieldName)) {
				value = data[index][TRACEIMPORT_FEHLER];
			} else if ("F_MENGE_AUS_IMPORTDATEI".equals(fieldName)) {
				value = data[index][TRACEIMPORT_MENGE_AUS_IMPORTDATEI];
			} else if ("F_STUECKLISTE".equals(fieldName)) {
				value = data[index][TRACEIMPORT_STUECKLISTE];
			} else if ("F_STUECKLISTE_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][TRACEIMPORT_STUECKLISTE_BEZEICHNUNG];
			} else if ("F_KBEZ_STKL_AUS_IMPORTDATEI".equals(fieldName)) {
				value = data[index][TRACEIMPORT_KBEZ_STKL_AUS_IMPORTDATEI];
			} else if ("F_SUBREPORT_ZEILEN_AUS_DATEI".equals(fieldName)) {
				value = data[index][TRACEIMPORT_SUBREPORT_ZEILEN_AUS_DATEI];
			} else if ("F_LAGERSTAND".equals(fieldName)) {
				value = data[index][TRACEIMPORT_LAGERSTAND];
			} else if ("F_LOSGROESSE".equals(fieldName)) {
				value = data[index][TRACEIMPORT_LOSGROESSE];
			} else if ("F_LOSNUMMER".equals(fieldName)) {
				value = data[index][TRACEIMPORT_LOSNUMMER];
			} else if ("F_LOSABGELIEFERT".equals(fieldName)) {
				value = data[index][TRACEIMPORT_LOSABGELIEFERT];
			}
		}
			break;
		case UC_MASCHINEUNDMATERIAL: {
			if ("Losnummer".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_LOSNUMMER];
			} else if ("Losgroesse".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_LOSGROESSE];
			} else if ("LoszusatzstatusP1".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_ZUSATZSTATUS_P1];
			} else if ("ArtikelnummerTaetigkeit".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_ARTIKELNUMMER_TAETIGKEIT];
			} else if ("ArtikelbezeichnungTaetigkeit".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_ARTIKELBEZEICHNUNG_TAETIGKEIT];
			} else if ("Arbeitsgang".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_ARBEITSGANG];
			} else if ("Unterarbeitsgang".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_UNTERARBEITSGANG];
			} else if ("AgArt".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_AG_ART];
			} else if ("AgBeginn".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_AG_BEGINN];
			} else if ("Aufspannung".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_AUFSPANNUNG];
			} else if ("Person".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_PERSON];
			} else if ("MaschineIdentifikationsnummer".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_MASCHINE_IDENTIFIKATIONSNUMMMER];
			} else if ("Maschinengruppe".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_MASCHINENGRUPPE];
			} else if ("MaschineBezeichnung".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_MASCHINE_BEZEICHNUNG];
			} else if ("MaschineInventarnummer".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_MASCHINE_INVENTARNUMMMER];
			} else if ("NurMaschinenzeit".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_NUR_MASCHINENZEIT];
			} else if ("AutoStopBeiGeht".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_AUTO_STOP_BEI_GEHT];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_KOMMENTAR];
			} else if ("KommentarLang".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_KOMMENTAR_LANG];
			} else if ("Stueckzeit".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_STUECKZEIT];
			} else if ("Ruestzeit".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_RUESTZEIT];
			} else if ("ZeiteinheitArbeitsplan".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_ZEITEINHEIT_ARBEITSPLAN];
			} else if ("ArtikelnummerMaterial".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_ARTIKELNUMMER_MATERIAL];
			} else if ("Next_OffeneMengeMaterial".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_NEXT_AG_OFFENE_MENGE_MATERIAL];
			} else if ("OffeneMengeMaterial".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_OFFENE_MENGE_MATERIAL];
			} else if ("ArtikelbezeichnungMaterial".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_ARTIKELBEZEICHNUNG_MATERIAL];
			} else if ("GesamtzeitInStundenPersonal".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_GESAMTZEIT_IN_STUNDEN_PERSONAL];
			} else if ("GesamtzeitInStundenMaschine".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_GESAMTZEIT_IN_STUNDEN_MASCHINE];
			} else if ("MaschinenversatzMS".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_MASCHINENVERSATZ_MS];
			} else if ("Next_ArtikelnummerTaetigkeit".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_NEXT_AG_ARTIKELNUMMER_TAETIGKEIT];
			} else if ("Next_ArtikelbezeichnungTaetigkeit".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_NEXT_AG_ARTIKELBEZEICHNUNG_TAETIGKEIT];
			} else if ("Next_Arbeitsgang".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_NEXT_AG_ARBEITSGANG];
			} else if ("Next_Unterarbeitsgang".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_NEXT_AG_UNTERARBEITSGANG];
			} else if ("Next_AgArt".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_NEXT_AG_AG_ART];
			} else if ("Next_AgBeginn".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_NEXT_AG_AG_BEGINN];
			} else if ("Next_Aufspannung".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_NEXT_AG_AUFSPANNUNG];
			} else if ("Next_Person".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_NEXT_AG_PERSON];
			} else if ("Next_MaschineIdentifikationsnummer".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_NEXT_AG_MASCHINE_IDENTIFIKATIONSNUMMMER];
			} else if ("Next_MaschineBezeichnung".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_NEXT_AG_MASCHINE_BEZEICHNUNG];
			} else if ("Next_MaschineInventarnummer".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_NEXT_AG_MASCHINE_INVENTARNUMMMER];
			} else if ("Next_NurMaschinenzeit".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_NEXT_AG_NUR_MASCHINENZEIT];
			} else if ("Next_AutoStopBeiGeht".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_NEXT_AG_AUTO_STOP_BEI_GEHT];
			} else if ("Next_Kommentar".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_NEXT_AG_KOMMENTAR];
			} else if ("Next_KommentarLang".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_NEXT_AG_KOMMENTAR_LANG];
			} else if ("Next_Stueckzeit".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_NEXT_AG_STUECKZEIT];
			} else if ("Next_Ruestzeit".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_NEXT_AG_RUESTZEIT];
			} else if ("Next_ArtikelnummerMaterial".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_NEXT_AG_ARTIKELNUMMER_MATERIAL];
			} else if ("Next_ArtikelbezeichnungMaterial".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_NEXT_AG_ARTIKELBEZEICHNUNG_MATERIAL];
			} else if ("Next_GesamtzeitInStundenPersonal".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_NEXT_AG_GESAMTZEIT_IN_STUNDEN_PERSONAL];
			} else if ("Next_GesamtzeitInStundenMaschine".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_NEXT_AG_GESAMTZEIT_IN_STUNDEN_MASCHINE];
			} else if ("Next_MaschinenversatzMS".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_NEXT_AG_MASCHINENVERSATZ_MS];
			} else if ("KundeKbez".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_KUNDE_KBEZ];
			} else if ("Fortschritt".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_FORTSCHRITT];
			} else if ("Next_Fortschritt".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_NEXT_AG_FORTSCHRITT];
			} else if ("Next_Maschinengruppe".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_NEXT_AG_MASCHINENGRUPPE];
			} else if ("IstzeitInStundenPersonal".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_ISTZEIT_IN_STUNDEN_PERSONAL];
			} else if ("IstzeitInStundenMaschine".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_ISTZEIT_IN_STUNDEN_MASCHINE];
			}

		}
			break;
		case UC_TAETIGKEITAGBEGINN: {
			if ("Losnummer".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_LOSNUMMER];
			} else if ("Losgroesse".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_LOSGROESSE];
			} else if ("ArtikelnummerTaetigkeit".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_ARTIKELNUMMER_TAETIGKEIT];
			} else if ("ArtikelbezeichnungTaetigkeit".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_ARTIKELBEZEICHNUNG_TAETIGKEIT];
			} else if ("ArtikelnummerStkl".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_ARTIKELNUMMER_STKL];
			} else if ("ArtikelbezeichnungStkl".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_ARTIKELBEZEICHNUNG_STKL];
			} else if ("ArtikelkurzbezeichnungStkl".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_ARTIKELKURZBEZEICHNUNG_STKL];
			} else if ("ArtikelzusatzbezeichnungStkl".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_ARTIKELZUSATZBEZEICHNUNG_STKL];
			} else if ("Artikelzusatzbezeichnung2Stkl".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_ARTIKELZUSATZBEZEICHNUNG2_STKL];
			} else if ("Arbeitsgang".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_ARBEITSGANG];
			} else if ("Unterarbeitsgang".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_UNTERARBEITSGANG];
			} else if ("AgArt".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_AG_ART];
			} else if ("InArbeit".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_IN_ARBEIT];
			} else if ("AgBeginn".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_AG_BEGINN];
			} else if ("Aufspannung".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_AUFSPANNUNG];
			} else if ("Person".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_PERSON];
			} else if ("MaschineIdentifikationsnummer".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_MASCHINE_IDENTIFIKATIONSNUMMMER];
			} else if ("MaschineBezeichnung".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_MASCHINE_BEZEICHNUNG];
			} else if ("MaschineInventarnummer".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_MASCHINE_INVENTARNUMMMER];
			} else if ("NurMaschinenzeit".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_NUR_MASCHINENZEIT];
			} else if ("AutoStopBeiGeht".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_AUTO_STOP_BEI_GEHT];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_KOMMENTAR];
			} else if ("KommentarLang".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_KOMMENTAR_LANG];
			} else if ("Stueckzeit".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_STUECKZEIT];
			} else if ("Ruestzeit".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_RUESTZEIT];
			} else if ("ZeiteinheitArbeitsplan".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_ZEITEINHEIT_ARBEITSPLAN];
			} else if ("ArtikelnummerMaterial".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_ARTIKELNUMMER_MATERIAL];
			} else if ("ArtikelbezeichnungMaterial".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_ARTIKELBEZEICHNUNG_MATERIAL];
			} else if ("GesamtzeitInStundenMaschine".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_GESAMTZEIT_IN_STUNDEN_MASCHINE];
			} else if ("IstzeitInStundenMaschine".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_ISTZEIT_IN_STUNDEN_MASCHINE];
			} else if ("GesamtzeitInStundenPersonal".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_GESAMTZEIT_IN_STUNDEN_PERSONAL];
			} else if ("IstzeitInStundenPersonal".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_ISTZEIT_IN_STUNDEN_PERSONAL];
			} else if ("MaschinenversatzMS".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_MASCHINENVERSATZ_MS];
			} else if ("Next_AgBeginn".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_NEXT_AG_BEGINN];
			} else if ("Next_MaschinenversatzMS".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_NEXT_MASCHINENVERSATZ_MS];
			} else if ("KundeKbez".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_KUNDE_KBEZ];
			} else if ("Fortschritt".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_FORTSCHRITT];
			} else if ("FehlerInZeitdaten".equals(fieldName)) {
				value = data[index][TAETIGKEITAGBEGINN_FEHLER_IN_ZEITDATEN];
			}

		}
			break;

		case UC_LADELISTE: {
			if ("Losnummer".equals(fieldName)) {
				value = data[index][LADELISTE_LOSNUMMER];
			}
			if ("Losstatus".equals(fieldName)) {
				value = data[index][LADELISTE_LOSSTATUS];
			} else if ("Losgroesse".equals(fieldName)) {
				value = data[index][LADELISTE_LOSGROESSE];
			} else if ("ArtikelnummerStkl".equals(fieldName)) {
				value = data[index][LADELISTE_ARTIKELNUMMER_STKL];
			} else if ("ArtikelbezeichnungStkl".equals(fieldName)) {
				value = data[index][LADELISTE_ARTIKELBEZEICHNUNG_STKL];
			} else if ("ArtikelkurzbezeichnungStkl".equals(fieldName)) {
				value = data[index][LADELISTE_ARTIKELKURZBEZEICHNUNG_STKL];
			} else if ("ArtikelzusatzbezeichnungStkl".equals(fieldName)) {
				value = data[index][LADELISTE_ARTIKELZUSATZBEZEICHNUNG_STKL];
			} else if ("Artikelzusatzbezeichnung2Stkl".equals(fieldName)) {
				value = data[index][LADELISTE_ARTIKELZUSATZBEZEICHNUNG2_STKL];
			} else if ("InArbeit".equals(fieldName)) {
				value = data[index][LADELISTE_IN_ARBEIT];
			} else if ("ZeiteinheitArbeitsplan".equals(fieldName)) {
				value = data[index][LADELISTE_ZEITEINHEIT_ARBEITSPLAN];
			} else if ("KundeKbez".equals(fieldName)) {
				value = data[index][LADELISTE_KUNDE_KBEZ];
			}

			else if ("AktuellerAG_ArtikelnummerTaetigkeit".equals(fieldName)) {
				value = data[index][LADELISTE_AKTUELLER_ARBEITGANG_ARTIKELNUMMER_TAETIGKEIT];
			} else if ("AktuellerAG_BezeichnungTaetigkeit".equals(fieldName)) {
				value = data[index][LADELISTE_AKTUELLER_ARBEITGANG_BEZEICHNUNG_TAETIGKEIT];
			} else if ("AktuellerAG_ZusatzbezeichnungTaetigkeit".equals(fieldName)) {
				value = data[index][LADELISTE_AKTUELLER_ARBEITGANG_ZUSATZBEZEICHNUNG_TAETIGKEIT];
			} else if ("AktuellerAG_Fertig".equals(fieldName)) {
				value = data[index][LADELISTE_AKTUELLER_ARBEITGANG_FERTIG];
			} else if ("AktuellerAG_Fortschritt".equals(fieldName)) {
				value = data[index][LADELISTE_AKTUELLER_ARBEITGANG_FORTSCHRITT];
			} else if ("AktuellerAG_Arbeitsgangsnummer".equals(fieldName)) {
				value = data[index][LADELISTE_AKTUELLER_ARBEITGANG_ARBEITGANGNUMMER];
			} else if ("AktuellerAG_AGBeginn".equals(fieldName)) {
				value = data[index][LADELISTE_AKTUELLER_ARBEITGANG_AG_BEGINN];
			} else if ("AktuellerAG_Unterarbeitsgangsnummer".equals(fieldName)) {
				value = data[index][LADELISTE_AKTUELLER_ARBEITGANG_UNTERARBEITSGANGNUMMER];
			} else if ("AktuellerAG_SubreportGutSchlecht".equals(fieldName)) {
				value = data[index][LADELISTE_AKTUELLER_ARBEITGANG_SUBREPORT_GUT_SCHLECHT];
			} else if ("AktuellerAG_SummeGut".equals(fieldName)) {
				value = data[index][LADELISTE_AKTUELLER_ARBEITGANG_SUMME_GUTSTUECK];
			} else if ("AktuellerAG_SummeSchlecht".equals(fieldName)) {
				value = data[index][LADELISTE_AKTUELLER_ARBEITGANG_SUMME_SCHLECHTSTUECK];
			} else if ("AktuellerAG_DatumJuengsteStueckrueckeldung".equals(fieldName)) {
				value = data[index][LADELISTE_AKTUELLER_ARBEITGANG_DATUM_JUENGSTE_STUECKRUECKMELDUNG];
			} else if ("AktuellerAG_KommentarJuengsteStueckrueckeldung".equals(fieldName)) {
				value = data[index][LADELISTE_AKTUELLER_ARBEITGANG_KOMMENTAR_JUENGSTE_STUECKRUECKMELDUNG];
			} else if ("AktuellerAG_Kommentar".equals(fieldName)) {
				value = data[index][LADELISTE_AKTUELLER_ARBEITGANG_KOMMENTAR];
			} else if ("AktuellerAG_Langtext".equals(fieldName)) {
				value = data[index][LADELISTE_AKTUELLER_ARBEITGANG_LANGTEXT];
			}

			else if ("NaechsterAG_ArtikelnummerTaetigkeit".equals(fieldName)) {
				value = data[index][LADELISTE_NAECHSTER_ARBEITGANG_ARTIKELNUMMER_TAETIGKEIT];
			} else if ("NaechsterAG_BezeichnungTaetigkeit".equals(fieldName)) {
				value = data[index][LADELISTE_NAECHSTER_ARBEITGANG_BEZEICHNUNG_TAETIGKEIT];
			} else if ("NaechsterAG_ZusatzbezeichnungTaetigkeit".equals(fieldName)) {
				value = data[index][LADELISTE_NAECHSTER_ARBEITGANG_ZUSATZBEZEICHNUNG_TAETIGKEIT];
			} else if ("NaechsterAG_Fertig".equals(fieldName)) {
				value = data[index][LADELISTE_NAECHSTER_ARBEITGANG_FERTIG];
			} else if ("NaechsterAG_Fortschritt".equals(fieldName)) {
				value = data[index][LADELISTE_NAECHSTER_ARBEITGANG_FORTSCHRITT];
			} else if ("NaechsterAG_Arbeitsgangsnummer".equals(fieldName)) {
				value = data[index][LADELISTE_NAECHSTER_ARBEITGANG_ARBEITGANGNUMMER];
			} else if ("NaechsterAG_AGBeginn".equals(fieldName)) {
				value = data[index][LADELISTE_NAECHSTER_ARBEITGANG_AG_BEGINN];
			} else if ("NaechsterAG_Unterarbeitsgangsnummer".equals(fieldName)) {
				value = data[index][LADELISTE_NAECHSTER_ARBEITGANG_UNTERARBEITSGANGNUMMER];
			} else if ("NaechsterAG_SubreportGutSchlecht".equals(fieldName)) {
				value = data[index][LADELISTE_NAECHSTER_ARBEITGANG_SUBREPORT_GUT_SCHLECHT];
			} else if ("NaechsterAG_SummeGut".equals(fieldName)) {
				value = data[index][LADELISTE_NAECHSTER_ARBEITGANG_SUMME_GUTSTUECK];
			} else if ("NaechsterAG_SummeSchlecht".equals(fieldName)) {
				value = data[index][LADELISTE_NAECHSTER_ARBEITGANG_SUMME_SCHLECHTSTUECK];
			} else if ("NaechsterAG_DatumJuengsteStueckrueckeldung".equals(fieldName)) {
				value = data[index][LADELISTE_NAECHSTER_ARBEITGANG_DATUM_JUENGSTE_STUECKRUECKMELDUNG];
			} else if ("NaechsterAG_KommentarJuengsteStueckrueckeldung".equals(fieldName)) {
				value = data[index][LADELISTE_NAECHSTER_ARBEITGANG_KOMMENTAR_JUENGSTE_STUECKRUECKMELDUNG];
			} else if ("NaechsterAG_Kommentar".equals(fieldName)) {
				value = data[index][LADELISTE_NAECHSTER_ARBEITGANG_KOMMENTAR];
			} else if ("NaechsterAG_Langtext".equals(fieldName)) {
				value = data[index][LADELISTE_NAECHSTER_ARBEITGANG_LANGTEXT];
			}

			else if ("VorherigerAG_ArtikelnummerTaetigkeit".equals(fieldName)) {
				value = data[index][LADELISTE_VORHERIGER_ARBEITGANG_ARTIKELNUMMER_TAETIGKEIT];
			} else if ("VorherigerAG_BezeichnungTaetigkeit".equals(fieldName)) {
				value = data[index][LADELISTE_VORHERIGER_ARBEITGANG_BEZEICHNUNG_TAETIGKEIT];
			} else if ("VorherigerAG_ZusatzbezeichnungTaetigkeit".equals(fieldName)) {
				value = data[index][LADELISTE_VORHERIGER_ARBEITGANG_ZUSATZBEZEICHNUNG_TAETIGKEIT];
			} else if ("VorherigerAG_Fertig".equals(fieldName)) {
				value = data[index][LADELISTE_VORHERIGER_ARBEITGANG_FERTIG];
			} else if ("VorherigerAG_Fortschritt".equals(fieldName)) {
				value = data[index][LADELISTE_VORHERIGER_ARBEITGANG_FORTSCHRITT];
			} else if ("VorherigerAG_Arbeitsgangsnummer".equals(fieldName)) {
				value = data[index][LADELISTE_VORHERIGER_ARBEITGANG_ARBEITGANGNUMMER];
			} else if ("VorherigerAG_AGBeginn".equals(fieldName)) {
				value = data[index][LADELISTE_VORHERIGER_ARBEITGANG_AG_BEGINN];
			} else if ("VorherigerAG_Unterarbeitsgangsnummer".equals(fieldName)) {
				value = data[index][LADELISTE_VORHERIGER_ARBEITGANG_UNTERARBEITSGANGNUMMER];
			} else if ("VorherigerAG_SubreportGutSchlecht".equals(fieldName)) {
				value = data[index][LADELISTE_VORHERIGER_ARBEITGANG_SUBREPORT_GUT_SCHLECHT];
			} else if ("VorherigerAG_SummeGut".equals(fieldName)) {
				value = data[index][LADELISTE_VORHERIGER_ARBEITGANG_SUMME_GUTSTUECK];
			} else if ("VorherigerAG_SummeSchlecht".equals(fieldName)) {
				value = data[index][LADELISTE_VORHERIGER_ARBEITGANG_SUMME_SCHLECHTSTUECK];
			} else if ("VorherigerAG_DatumJuengsteStueckrueckeldung".equals(fieldName)) {
				value = data[index][LADELISTE_VORHERIGER_ARBEITGANG_DATUM_JUENGSTE_STUECKRUECKMELDUNG];
			} else if ("VorherigerAG_KommentarJuengsteStueckrueckeldung".equals(fieldName)) {
				value = data[index][LADELISTE_VORHERIGER_ARBEITGANG_KOMMENTAR_JUENGSTE_STUECKRUECKMELDUNG];
			} else if ("VorherigerAG_Kommentar".equals(fieldName)) {
				value = data[index][LADELISTE_VORHERIGER_ARBEITGANG_KOMMENTAR];
			} else if ("VorherigerAG_Langtext".equals(fieldName)) {
				value = data[index][LADELISTE_VORHERIGER_ARBEITGANG_LANGTEXT];
			}

		}
			break;

		case UC_BEDARFSZUSAMMENSCHAU: {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][BZ_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][BZ_BEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][BZ_ZUSATZBEZEICHNUNG];
			} else if ("Kurzbezeichnung".equals(fieldName)) {
				value = data[index][BZ_KURZBEZEICHNUNG];
			} else if ("Revision".equals(fieldName)) {
				value = data[index][BZ_REVISION];
			} else if ("Index".equals(fieldName)) {
				value = data[index][BZ_INDEX];
			} else if ("Artikelgruppe".equals(fieldName)) {
				value = data[index][BZ_ARTIKELGRUPPE];
			} else if ("Artikelklasse".equals(fieldName)) {
				value = data[index][BZ_ARTIKELKLASSE];
			} else if ("Lagersoll".equals(fieldName)) {
				value = data[index][BZ_LAGERSOLL];
			} else if ("Lagermindest".equals(fieldName)) {
				value = data[index][BZ_LAGERMINDEST];
			} else if ("JahresmengeGeplant".equals(fieldName)) {
				value = data[index][BZ_JAHRESMENGE_GEPLANT];
			} else if ("Fertigungssatzgroesse".equals(fieldName)) {
				value = data[index][BZ_FERTIGUNGSSATZGROESSE];
			} else if ("MonatVergangenheit1".equals(fieldName)) {
				value = data[index][BZ_MONAT_VERGANGENHEIT_1];
			} else if ("MonatVergangenheit2".equals(fieldName)) {
				value = data[index][BZ_MONAT_VERGANGENHEIT_2];
			} else if ("MonatVergangenheit3".equals(fieldName)) {
				value = data[index][BZ_MONAT_VERGANGENHEIT_3];
			} else if ("MonatVergangenheit6".equals(fieldName)) {
				value = data[index][BZ_MONAT_VERGANGENHEIT_6];
			} else if ("MonatVergangenheit9".equals(fieldName)) {
				value = data[index][BZ_MONAT_VERGANGENHEIT_9];
			} else if ("MonatVergangenheit12".equals(fieldName)) {
				value = data[index][BZ_MONAT_VERGANGENHEIT_12];
			} else if ("MonatZukunft1".equals(fieldName)) {
				value = data[index][BZ_MONAT_ZUKUNFT_1];
			} else if ("MonatZukunft2".equals(fieldName)) {
				value = data[index][BZ_MONAT_ZUKUNFT_2];
			} else if ("MonatZukunft3".equals(fieldName)) {
				value = data[index][BZ_MONAT_ZUKUNFT_3];
			} else if ("MonatZukunft6".equals(fieldName)) {
				value = data[index][BZ_MONAT_ZUKUNFT_6];
			} else if ("MonatZukunft9".equals(fieldName)) {
				value = data[index][BZ_MONAT_ZUKUNFT_9];
			} else if ("MonatZukunft12".equals(fieldName)) {
				value = data[index][BZ_MONAT_ZUKUNFT_12];
			} else if ("MonatZukunftDanach".equals(fieldName)) {
				value = data[index][BZ_MONAT_ZUKUNFT_DANACH];
			} else if ("InFertigung".equals(fieldName)) {
				value = data[index][BZ_IN_FERTIGUNG];
			} else if ("Rahmenreserviert".equals(fieldName)) {
				value = data[index][BZ_RAHMENRESERVIERT];
			} else if ("Bestellt".equals(fieldName)) {
				value = data[index][BZ_BESTELLT];
			} else if ("Rahmenbestellt".equals(fieldName)) {
				value = data[index][BZ_RAHMENBESTELLT];
			} else if ("Kunde".equals(fieldName)) {
				value = data[index][BZ_KUNDE];
			} else if ("VKPreis".equals(fieldName)) {
				value = data[index][BZ_VKPREIS];
			} else if ("Gestpreis".equals(fieldName)) {
				value = data[index][BZ_GESTPREIS];
			} else if ("SubreportArtikelkommentar".equals(fieldName)) {
				value = data[index][BZ_SUBREPORT_ARTIKELKOMMENTAR];
			} else if ("SummeMengeInternebestellung".equals(fieldName)) {
				value = data[index][BZ_SUMME_MENGE_INTERNEBESTELLUNG];
			} else if ("Lagerstand".equals(fieldName)) {
				value = data[index][BZ_LAGERSTAND];
			}

		}
			break;
		case UC_ETIKETTA4: {
			if ("Sollmaterial_Artikelnummer".equals(fieldName)) {
				value = data[index][ETI_A4_SOLLMATERIAL_ARTIKELNUMMER];
			} else if ("Sollmaterial_Bezeichnung".equals(fieldName)) {
				value = data[index][ETI_A4_SOLLMATERIAL_BEZEICHNUNG];
			} else if ("Sollmaterial_Kurzbezeichnung".equals(fieldName)) {
				value = data[index][ETI_A4_SOLLMATERIAL_KURZBEZEICHNUNG];
			} else if ("Sollmaterial_Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][ETI_A4_SOLLMATERIAL_ZUSATZBEZEICHNUNG];
			} else if ("Sollmaterial_Zusatzbezeichnung2".equals(fieldName)) {
				value = data[index][ETI_A4_SOLLMATERIAL_ZUSATZBEZEICHNUNG2];
			} else if ("Sollmaterial_Einheit".equals(fieldName)) {
				value = data[index][ETI_A4_SOLLMATERIAL_EINHEIT];
			} else if ("Sollmaterial_Referenznummer".equals(fieldName)) {
				value = data[index][ETI_A4_SOLLMATERIAL_REFERENZNUMMER];
			} else if ("Sollmaterial_Istmenge".equals(fieldName)) {
				value = data[index][ETI_A4_SOLLMATERIAL_ISTMENGE];
			} else if ("Sollmaterial_Sollmenge".equals(fieldName)) {
				value = data[index][ETI_A4_SOLLMATERIAL_SOLLMENGE];
			} else if ("Losnummer".equals(fieldName)) {
				value = data[index][ETI_A4_LOSNUMMER];
			} else if ("Auftrag".equals(fieldName)) {
				value = data[index][ETI_A4_AUFTRAG];
			} else if ("Rohs".equals(fieldName)) {
				value = data[index][ETI_A4_ROHS];
			} else if ("Kunde_Anrede".equals(fieldName)) {
				value = data[index][ETI_A4_KUNDE_ANREDE];
			} else if ("Kunde_Abteilung".equals(fieldName)) {
				value = data[index][ETI_A4_KUNDE_ABTEILUNG];
			} else if ("Kunde_LandPlzOrt".equals(fieldName)) {
				value = data[index][ETI_A4_KUNDE_LANDPLZORT];
			} else if ("Auftragsposition".equals(fieldName)) {
				value = data[index][ETI_A4_AUFTRAGSPOSITION];
			} else if ("Kostenstellennummer".equals(fieldName)) {
				value = data[index][ETI_A4_KOSTENSTELLENNUMMER];
			} else if ("Kostenstellenbezeichnung".equals(fieldName)) {
				value = data[index][ETI_A4_KOSTENSTELLENBEZEICHNUNG];
			} else if ("Losart".equals(fieldName)) {
				value = data[index][ETI_A4_LOSART];
			} else if ("Stuecklistennummer".equals(fieldName)) {
				value = data[index][ETI_A4_STUECKLISTENNUMMER];
			} else if ("Stuecklistenbezeichnung".equals(fieldName)) {
				value = data[index][ETI_A4_STUECKLISTENBEZEICHNUNG];
			} else if ("Stuecklistenzusatzbezeichnung".equals(fieldName)) {
				value = data[index][ETI_A4_STUECKLISTEZUSATZBEZEICHNUNG];
			} else if ("Stuecklistenzusatzbezeichnung2".equals(fieldName)) {
				value = data[index][ETI_A4_STUECKLISTEZUSATZBEZEICHNUNG2];
			} else if ("Stuecklistenkurzbezeichnung".equals(fieldName)) {
				value = data[index][ETI_A4_STUECKLISTEKURZBEZEICHNUNG];
			} else if ("Stuecklistenreferenznummer".equals(fieldName)) {
				value = data[index][ETI_A4_STUECKLISTEREFERENZNUMMER];
			} else if ("Stuecklisteneinheit".equals(fieldName)) {
				value = data[index][ETI_A4_STUECKLISTEMENGENEINHEIT];
			} else if ("Losgroesse".equals(fieldName)) {
				value = data[index][ETI_A4_LOSGROESSE];
			} else if ("Lager".equals(fieldName)) {
				value = data[index][ETI_A4_LAGER];
			} else if ("Beginn".equals(fieldName)) {
				value = data[index][ETI_A4_BEGINN];
			} else if ("Ende".equals(fieldName)) {
				value = data[index][ETI_A4_ENDE];
			} else if ("Dauer".equals(fieldName)) {
				value = data[index][ETI_A4_DAUER];
			} else if ("Technikernummer".equals(fieldName)) {
				value = data[index][ETI_A4_TECHNIKERNUMMER];
			} else if ("Technikername".equals(fieldName)) {
				value = data[index][ETI_A4_TECHNIKERNAME];
			} else if ("Projekt".equals(fieldName)) {
				value = data[index][ETI_A4_PROJEKT];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][ETI_A4_KOMMENTAR];
			} else if ("Fertigungsgruppe".equals(fieldName)) {
				value = data[index][ETI_A4_FERTIGUNGSGRUPPE];
			} else if ("Fertigungsort".equals(fieldName)) {
				value = data[index][ETI_A4_FERTIGUNGSORT];
			} else if ("Text".equals(fieldName)) {
				value = data[index][ETI_A4_TEXT];
			} else if ("Exemplar".equals(fieldName)) {
				value = data[index][ETI_A4_EXEMPLAR];
			} else if ("AuftragLiefertermin".equals(fieldName)) {
				value = data[index][ETI_A4_AUFTRAG_LIEFERTERMIN];
			} else if ("AuftragpositionLiefertermin".equals(fieldName)) {
				value = data[index][ETI_A4_AUFTRAGPOSITION_LIEFERTERMIN];
			}
		}
			break;
		}

		return value;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printTheoretischeFehlmengen(Integer losIId, Integer auftragIId, Integer projektIId,
			boolean sortierungWieInStklErfasst, TheClientDto theClientDto) throws EJBExceptionLP {
		Session sessionEinesLoses = null;
		try {
			this.useCase = UC_THEORETISCHE_FEHLMENGEN;
			this.index = -1;
			SessionFactory factory = FLRSessionFactory.getFactory();

			HashMap mapParameter = new HashMap();

			Session session = factory.openSession();

			String sQuery = "SELECT f FROM FLRLosReport f WHERE f.mandant_c_nr='" + theClientDto.getMandant()
					+ "' AND f.status_c_nr NOT IN ('" + FertigungFac.STATUS_ERLEDIGT + "','"
					+ FertigungFac.STATUS_STORNIERT + "')";

			if (losIId != null) {
				sQuery += " AND f.i_id=" + losIId;

				LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);
				mapParameter.put("P_LOSNUMMER", losDto.getCNr());

			}

			if (auftragIId != null) {
				sQuery += " AND f.flrauftrag.i_id=" + auftragIId;
				AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(auftragIId);
				mapParameter.put("P_AUFTRAG", auftragDto.getCNr());
			}

			if (projektIId != null) {

				sQuery += " AND ( f.flrprojekt.i_id=" + projektIId + " OR f.flrauftrag.flrprojekt.i_id= " + projektIId
						+ " )";

				ProjektDto pjDto = getProjektFac().projektFindByPrimaryKey(projektIId);

				mapParameter.put("P_PROJEKT", pjDto.getCNr());
				BereichDto bereichDto = getProjektServiceFac().bereichFindByPrimaryKey(pjDto.getBereichIId());
				mapParameter.put("P_PROJEKTBEREICH", bereichDto.getCBez());

			}

			mapParameter.put("P_SORTIERUNG_WIE_IN_STKL_ERFASST", sortierungWieInStklErfasst);

			boolean bErsatztypen = false;
			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ERSATZTYPENVERWALTUNG,
					theClientDto)) {
				bErsatztypen = true;
			}

			org.hibernate.Query queryLose = session.createQuery(sQuery);

			List l = queryLose.list();

			Iterator it = l.iterator();

			ArrayList alDaten = new ArrayList();

			HashMap<Integer, ArrayList<StuecklistepositionDto>> hmLoseMitStuecklistePositionen = new HashMap();

			HashMap<Integer, BigDecimal> hmArtikelReserviert = new HashMap<Integer, BigDecimal>();

			HashMap<Integer, LosDto> hmLose = new HashMap<Integer, LosDto>();
			HashMap<Integer, LossollmaterialDto> hmLossollamterial = new HashMap<Integer, LossollmaterialDto>();

			while (it.hasNext()) {
				FLRLosReport flrLos = (FLRLosReport) it.next();

				Object[] oZeileVorlageLos = new Object[TF_SPALTENANZAHL];

				// --------

				oZeileVorlageLos[TF_ANGELEGT] = new java.util.Date(flrLos.getT_anlegen().getTime());

				String sAuftragsnummer;
				String sKunde;
				if (flrLos.getFlrauftrag() != null) {
					AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(flrLos.getFlrauftrag().getI_id());
					sAuftragsnummer = auftragDto.getCNr();
					KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(),
							theClientDto);
					sKunde = kundeDto.getPartnerDto().getCName1nachnamefirmazeile1();
				} else {
					sAuftragsnummer = "";
					sKunde = "";
				}
				oZeileVorlageLos[TF_AUFTRAGNUMMER] = sAuftragsnummer;
				KostenstelleDto kstDto = getSystemFac().kostenstelleFindByPrimaryKey(flrLos.getKostenstelle_i_id());

				oZeileVorlageLos[TF_KOSTENSTELLENUMMER] = kstDto.getCNr();
				oZeileVorlageLos[TF_KUNDE] = sKunde;
				oZeileVorlageLos[TF_LOSGROESSE] = flrLos.getN_losgroesse();
				oZeileVorlageLos[TF_LOSNUMMER] = flrLos.getC_nr();
				oZeileVorlageLos[TF_STATUS] = flrLos.getStatus_c_nr();
				oZeileVorlageLos[TF_PRODUKTIONSBEGINN] = new java.sql.Date(flrLos.getT_produktionsbeginn().getTime());

				String sMengenEinheit = null;

				oZeileVorlageLos[TF_MENGENEINHEIT] = sMengenEinheit;
				ArrayList<Object> images = new ArrayList<Object>();
				String sLosStuecklisteArtikelKommentar = "";
				// Bild einfuegen
				if (flrLos.getStueckliste_i_id() != null) {

					StuecklisteDto stkDto = getStuecklisteFac()
							.stuecklisteFindByPrimaryKey(flrLos.getStueckliste_i_id(), theClientDto);
					// Einheit
					if (stkDto.getArtikelDto() != null) {
						if (stkDto.getArtikelDto().getEinheitCNr() != null) {
							sMengenEinheit = stkDto.getArtikelDto().getEinheitCNr();
						}
					}

					StuecklistepositionDto[] stklPosDtos = getStuecklisteFac()
							.stuecklistepositionFindByStuecklisteIId(flrLos.getStueckliste_i_id(), theClientDto);
					ArrayList<StuecklistepositionDto> alStuecklstepositionen = new ArrayList<StuecklistepositionDto>();
					for (int j = 0; j < stklPosDtos.length; j++) {
						alStuecklstepositionen.add(stklPosDtos[j]);
					}

					hmLoseMitStuecklistePositionen.put(flrLos.getI_id(), alStuecklstepositionen);

					oZeileVorlageLos[TF_STUECKLISTEBEZEICHNUNG] = stkDto.getArtikelDto().getArtikelsprDto().getCBez();
					oZeileVorlageLos[TF_STUECKLISTEZUSATZBEZEICHNUNG] = stkDto.getArtikelDto().getArtikelsprDto()
							.getCZbez();
					oZeileVorlageLos[TF_STUECKLISTEKURZBEZEICHNUNG] = stkDto.getArtikelDto().getArtikelsprDto()
							.getCKbez();
					oZeileVorlageLos[TF_STUECKLISTENUMMER] = stkDto.getArtikelDto().getCNr();
					oZeileVorlageLos[TF_STUECKLISTEREFERENZNUMMER] = stkDto.getArtikelDto().getCReferenznr();

					ArtikelkommentarDto[] artikelkommentarDto = getArtikelkommentarFac()
							.artikelkommentardruckFindByArtikelIIdBelegartCNr(stkDto.getArtikelIId(),
									LocaleFac.BELEGART_LOS, theClientDto.getLocUiAsString(), theClientDto);

					if (artikelkommentarDto != null && artikelkommentarDto.length > 0) {

						for (int j = 0; j < artikelkommentarDto.length; j++) {
							if (artikelkommentarDto[j].getArtikelkommentarsprDto() != null) {
								// Text Kommentar
								if (artikelkommentarDto[j].getDatenformatCNr().trim()
										.indexOf(MediaFac.DATENFORMAT_MIMETYPEART_TEXT) != -1) {
									if (sLosStuecklisteArtikelKommentar == "") {
										sLosStuecklisteArtikelKommentar += artikelkommentarDto[j]
												.getArtikelkommentarsprDto().getXKommentar();
									} else {
										sLosStuecklisteArtikelKommentar += "\n"
												+ artikelkommentarDto[j].getArtikelkommentarsprDto().getXKommentar();
									}
								} else if (artikelkommentarDto[j].getDatenformatCNr()
										.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
										|| artikelkommentarDto[j].getDatenformatCNr()
												.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)
										|| artikelkommentarDto[j].getDatenformatCNr()
												.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)) {
									byte[] bild = artikelkommentarDto[j].getArtikelkommentarsprDto().getOMedia();
									if (bild != null) {
										java.awt.Image myImage = Helper.byteArrayToImage(bild);
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
				} else {
					oZeileVorlageLos[TF_STUECKLISTEBEZEICHNUNG] = flrLos.getC_projekt();

					oZeileVorlageLos[TF_STUECKLISTENUMMER] = getTextRespectUISpr("fert.materialliste",
							theClientDto.getMandant(), theClientDto.getLocUi());

				}
				if (sLosStuecklisteArtikelKommentar != "") {
					oZeileVorlageLos[TF_STUECKLISTEARTIKELKOMMENTAR] = Helper
							.formatStyledTextForJasper(sLosStuecklisteArtikelKommentar);
				}

				if (images != null && images.size() > 0) {

					String[] fieldnamesEigenschaften = new String[] { "F_BILD" };
					Object[][] dataSub = new Object[images.size()][fieldnamesEigenschaften.length];

					for (int i = 0; i < images.size(); i++) {
						dataSub[i][0] = images.get(i);
					}

					oZeileVorlageLos[TF_SUBREPORT_BILDER] = new LPDatenSubreport(dataSub, fieldnamesEigenschaften);
				}

				// ------
				sessionEinesLoses = factory.openSession();
				Criteria c = sessionEinesLoses.createCriteria(FLRLossollmaterial.class);
				c.add(Restrictions.eq(FertigungFac.FLR_LOSSOLLMATERIAL_LOS_I_ID, flrLos.getI_id()));
				// Sortierung nach Artikelnummer

				c.createAlias(FertigungFac.FLR_LOSSOLLMATERIAL_FLRARTIKEL, "a").addOrder(Order.asc("a.c_nr"));
				List<?> list = c.list();
				// Laeger holen
				LoslagerentnahmeDto[] laeger = getFertigungFac().loslagerentnahmeFindByLosIId(flrLos.getI_id());
				// positionen verdichten
				ArrayList<ReportTheoretischeFehlmengenDto> listVerdichtet = new ArrayList<ReportTheoretischeFehlmengenDto>();
				for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
					FLRLossollmaterial mat = (FLRLossollmaterial) iter.next();

					LossollmaterialDto[] lossollmaterialDtoErsatztypen = null;

					if (bErsatztypen) {

						lossollmaterialDtoErsatztypen = getFertigungFac()
								.lossollmaterialFindByLossollmaterialIIdOriginal(mat.getI_id());
					}

					java.sql.Date dTerminZeile = Helper
							.cutDate(getFertigungFac().getProduktionsbeginnAnhandZugehoerigemArbeitsgang(
									new java.sql.Date(flrLos.getT_produktionsbeginn().getTime()), mat.getI_id(),
									theClientDto));

					boolean bBereitsZugeordnet = false;
					for (int i = 0; i < listVerdichtet.size(); i++) {
						ReportTheoretischeFehlmengenDto temp = listVerdichtet.get(i);

						if (temp.getArtikelIId().equals(mat.getFlrartikel().getI_id())) {
							if (temp.getErsatztypen() != null && temp.getErsatztypen().length > 0) {
								// nicht verdichten
								break;
							} else {
								if (lossollmaterialDtoErsatztypen != null && lossollmaterialDtoErsatztypen.length > 0) {
									// nicht verdichten
									break;
								} else if (mat.getLossollmaterial_i_id_original() != null
										|| temp.getArtikelIIdOriginal() != null) {
									// nicht verdichten
									break;
								} else if (!Helper.cutDate(dTerminZeile).equals(temp.getDTermin())) {
									// nicht verdichten
									break;
								} else {
									// verdichten
									temp.addiereZuMenge(mat.getN_menge());
									temp.addiereZuAusgegebenerMenge(
											getFertigungFac().getAusgegebeneMenge(mat.getI_id(), null, theClientDto));
									bBereitsZugeordnet = true;
									break;
								}
							}
						}

					}
					if (bBereitsZugeordnet == false) {
						ReportTheoretischeFehlmengenDto dto = new ReportTheoretischeFehlmengenDto(
								mat.getFlrartikel().getI_id());
						dto.setErsatztypen(lossollmaterialDtoErsatztypen);
						dto.addiereZuMenge(mat.getN_menge());
						dto.setDTermin(dTerminZeile);
						dto.addiereZuAusgegebenerMenge(
								getFertigungFac().getAusgegebeneMenge(mat.getI_id(), null, theClientDto));
						if (mat.getLossollmaterial_i_id_original() != null) {
							LossollmaterialDto sollmatDto = getFertigungFac()
									.lossollmaterialFindByPrimaryKey(mat.getLossollmaterial_i_id_original());
							dto.setArtikelIIdOriginal(sollmatDto.getArtikelIId());
						}

						ArrayList<StuecklistepositionDto> alStuecklstepositionen = hmLoseMitStuecklistePositionen
								.get(mat.getLos_i_id());

						if (alStuecklstepositionen != null) {

							for (int u = 0; u < alStuecklstepositionen.size(); u++) {

								if (mat.getFlrartikel().getI_id().equals(alStuecklstepositionen.get(u).getArtikelIId())

										&& mat.getMontageart_i_id()
												.equals(alStuecklstepositionen.get(u).getMontageartIId())) {
									// wenn Menge und Artikel und Montageart gleich

									if (mat.getN_menge()
											.divide(mat.getFlrlos().getN_losgroesse(), 2, BigDecimal.ROUND_HALF_EVEN)
											.equals(Helper.rundeKaufmaennisch(alStuecklstepositionen.get(u).getNMenge(),
													2))) {

										dto.setISortAusStueckliste(alStuecklstepositionen.get(u).getISort());
										alStuecklstepositionen.remove(u);
										break;
									}

								}

							}
						}
						listVerdichtet.add(dto);
					}

				}

				for (int i = 0; i < listVerdichtet.size(); i++) {
					ReportTheoretischeFehlmengenDto item = (ReportTheoretischeFehlmengenDto) listVerdichtet.get(i);

					Object[] oZeile = oZeileVorlageLos.clone();

					Integer artikelIId = item.getArtikelIId();

					Long[] angebotenUndAngefragt = getAnfrageFac().getAngefragteUndAngeboteneMenge(artikelIId,
							theClientDto);
					oZeile[TF_ARTIKEL_ANZAHL_ANGEFRAGT] = angebotenUndAngefragt[0];
					oZeile[TF_ARTIKEL_ANZAHL_ANGEBOTEN] = angebotenUndAngefragt[1];

					if (item.getArtikelIIdOriginal() != null) {
						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(item.getArtikelIIdOriginal(), theClientDto);
						oZeile[TF_IDENT] = artikelDto.getCNr();
						oZeile[TF_REFERENZNUMMER] = artikelDto.getCReferenznr();
						oZeile[TF_LAGERBEWIRTSCHAFTET] = artikelDto.isLagerbewirtschaftet();
						oZeile[TF_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
						oZeile[TF_KURZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCKbez();

						ArtikelDto artikelDtoErsatz = getArtikelFac().artikelFindByPrimaryKeySmall(item.getArtikelIId(),
								theClientDto);
						oZeile[TF_ERSATZIDENT] = artikelDtoErsatz.getCNr();
						oZeile[TF_ERSATZBEZEICHNUNG] = artikelDtoErsatz.getArtikelsprDto().getCBez();
						oZeile[TF_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCZbez();
						artikelIId = item.getArtikelIId();
					} else {
						ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(item.getArtikelIId(),
								theClientDto);
						oZeile[TF_IDENT] = artikelDto.getCNr();
						oZeile[TF_REFERENZNUMMER] = artikelDto.getCReferenznr();
						oZeile[TF_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
						oZeile[TF_KURZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCKbez();
						oZeile[TF_ERSATZIDENT] = "";
						oZeile[TF_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCZbez();
						oZeile[TF_LAGERBEWIRTSCHAFTET] = artikelDto.isLagerbewirtschaftet();
					}

					oZeile[TF_PRODUKTIONSBEGINN] = item.getDTermin();

					oZeile[TF_SOLLMENGE] = item.getMenge();

					oZeile[TF_I_SORT_AUS_STUECKLISTE] = item.getISortAusStueckliste();

					oZeile[TF_ISTMENGE] = item.getBdAusgegebeneMenge();

					try {
						oZeile[TF_ARTIKELSPERREN] = getArtikelFac().getArtikelsperrenText(artikelIId);
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

					BigDecimal bdLagerstand = new BigDecimal(0);
					for (int j = 0; j < laeger.length; j++) {
						bdLagerstand = bdLagerstand.add(
								getLagerFac().getLagerstandOhneExc(artikelIId, laeger[j].getLagerIId(), theClientDto));
					}
					BigDecimal bdFehlmenge = getFehlmengeFac().getAnzahlFehlmengeEinesArtikels(artikelIId,
							theClientDto);

					BigDecimal bdReserviert = null;
					if (!hmArtikelReserviert.containsKey(artikelIId)) {
						hmArtikelReserviert.put(artikelIId,
								getReservierungFac().getAnzahlReservierungen(artikelIId, theClientDto));
					}
					bdReserviert = hmArtikelReserviert.get(artikelIId);

					BigDecimal bdVerfuegbar = bdLagerstand.subtract(bdFehlmenge).subtract(bdReserviert);
					// die Reservierungen auf diesem Los darf ich nicht
					// beruecksichtigen
					BigDecimal bdFehlmengeTheoretisch;
					Integer iMoeglich = 0;
					// wenn der lagerstand >= als die sollmenge ->
					// th.fehlmenge=0
					if (bdLagerstand.add(item.getBdAusgegebeneMenge()).compareTo(item.getMenge()) >= 0) {
						bdFehlmengeTheoretisch = new BigDecimal(0);
						iMoeglich = new Integer(flrLos.getN_losgroesse().intValue());
					} else {
						bdFehlmengeTheoretisch = item.getMenge()
								.subtract(bdLagerstand.add(item.getBdAusgegebeneMenge()));

						BigDecimal bdMoeglich = BigDecimal.ZERO;
						if (item.getMenge().doubleValue() != 0) {
							bdMoeglich = bdLagerstand.add(item.getBdAusgegebeneMenge())
									.multiply(flrLos.getN_losgroesse())
									.divide(item.getMenge(), 0, BigDecimal.ROUND_DOWN);
							iMoeglich = new Integer(bdMoeglich.intValue());
						}
					}
					oZeile[TF_VERFUEGBAR] = bdVerfuegbar;
					oZeile[TF_LAGERSTAND] = bdLagerstand; // zum testen
					oZeile[TF_FEHLMENGE] = bdFehlmengeTheoretisch;

					// SP6704
					Session sessionBestellt = FLRSessionFactory.getFactory().openSession();

					String query = "SELECT b FROM FLRArtikelbestellt AS b WHERE b.flrartikel.i_id=" + artikelIId;
					Query qResult = sessionBestellt.createQuery(query);
					List<?> results = qResult.list();
					BigDecimal bdBestelltZumTermin = new BigDecimal(0);
					BigDecimal bdBestelltGesamt = new BigDecimal(0);
					Iterator<?> resultListIterator = results.iterator();
					while (resultListIterator.hasNext()) {
						FLRArtikelbestellt flrArtikelbestellt = (FLRArtikelbestellt) resultListIterator.next();

						bdBestelltGesamt = bdBestelltGesamt.add(flrArtikelbestellt.getN_menge());

						java.util.Date dTermin = flrArtikelbestellt.getT_liefertermin();
						if (flrArtikelbestellt.getFlrbestellposition().getT_auftragsbestaetigungstermin() != null) {
							dTermin = flrArtikelbestellt.getFlrbestellposition().getT_auftragsbestaetigungstermin();
						}

						if (dTermin.getTime() <= item.getDTermin().getTime()) {
							bdBestelltZumTermin = bdBestelltZumTermin.add(flrArtikelbestellt.getN_menge());
						}

					}
					sessionBestellt.close();

					oZeile[TF_BESTELLT] = bdBestelltGesamt;
					oZeile[TF_BESTELLTZUMPRODUKTIONSSTART] = bdBestelltZumTermin;
					// SP4389
					oZeile[TF_IN_FERTIGUNG] = getFertigungFac().getAnzahlInFertigung(artikelIId, theClientDto);
					oZeile[TF_MOEGLICH] = iMoeglich;

					// Verfuegbarkeit bis zum Produktionsstart
					// PJ 14249
					// Fehlmengen
					Session sessionFehlmengen = FLRSessionFactory.getFactory().openSession();

					query = "SELECT SUM(f.n_menge) FROM FLRFehlmenge AS f WHERE f.artikel_i_id=" + artikelIId
							+ " AND f.t_liefertermin<='"
							+ Helper.formatDateWithSlashes(new java.sql.Date(flrLos.getT_produktionsbeginn().getTime()))
							+ "'" + " AND f.flrlossollmaterial.flrlos.c_nr<'" + flrLos.getC_nr() + "'";
					qResult = sessionFehlmengen.createQuery(query);
					results = qResult.list();
					BigDecimal bdfehlmengenZumTermin = new BigDecimal(0);
					resultListIterator = results.iterator();
					if (resultListIterator.hasNext()) {
						BigDecimal bd = (BigDecimal) resultListIterator.next();
						if (bd != null) {
							bdfehlmengenZumTermin = bd;
						}
					}

					sessionFehlmengen.close();
					// Reservierungen

					Session sessionReservierungen = FLRSessionFactory.getFactory().openSession();
					BigDecimal reservierungen = new BigDecimal(0);
					query = "SELECT r FROM FLRArtikelreservierung AS r WHERE r.flrartikel.i_id=" + artikelIId;
					qResult = sessionReservierungen.createQuery(query);

					results = qResult.list();

					resultListIterator = results.iterator();
					while (resultListIterator.hasNext()) {
						FLRArtikelreservierung flrArtikelreservierung = (FLRArtikelreservierung) resultListIterator
								.next();

						if (Helper.cutTimestamp(flrArtikelreservierung.getT_liefertermin()).getTime() <= item
								.getDTermin().getTime()) {
							{

							}

							if (flrArtikelreservierung.getC_belegartnr().equals(LocaleFac.BELEGART_LOS)) {

								LossollmaterialDto sollmatDto = null;
								if (!hmLossollamterial.containsKey(flrArtikelreservierung.getI_belegartpositionid())) {

									hmLossollamterial.put(flrArtikelreservierung.getI_belegartpositionid(),
											getFertigungFac().lossollmaterialFindByPrimaryKeyOhneExc(
													flrArtikelreservierung.getI_belegartpositionid()));

								}
								sollmatDto = hmLossollamterial.get(flrArtikelreservierung.getI_belegartpositionid());

								if (sollmatDto != null) {
									LosDto losvonreservierungDto = null;

									if (!hmLose.containsKey(sollmatDto.getLosIId())) {
										hmLose.put(sollmatDto.getLosIId(),
												getFertigungFac().losFindByPrimaryKey(sollmatDto.getLosIId()));
									}

									losvonreservierungDto = hmLose.get(sollmatDto.getLosIId());

									if (losvonreservierungDto.getCNr().compareTo(flrLos.getC_nr()) < 0) {
										reservierungen = reservierungen.add(flrArtikelreservierung.getN_menge());
									} else {
										continue;
									}

								}

							}
						}

					}

					sessionReservierungen.close();

					oZeile[TF_VERFUEGBARZUMPRODUKTIONSSTART] = bdLagerstand.subtract(bdfehlmengenZumTermin)
							.subtract(reservierungen);

					alDaten.add(oZeile);

				}

			}

			// Sortieren nach Artikel und dann nach ersatzartikel

			for (int k = alDaten.size() - 1; k > 0; --k) {
				for (int j = 0; j < k; ++j) {
					Object[] a1 = (Object[]) alDaten.get(j);
					Object[] a2 = (Object[]) alDaten.get(j + 1);

					if (sortierungWieInStklErfasst) {

						if (((String) a1[TF_LOSNUMMER]).compareTo((String) a2[TF_LOSNUMMER]) > 0) {
							alDaten.set(j, a2);
							alDaten.set(j + 1, a1);
						} else if (((String) a1[TF_LOSNUMMER]).compareTo((String) a2[TF_LOSNUMMER]) == 0) {
							Integer k1 = (Integer) a1[TF_I_SORT_AUS_STUECKLISTE];
							Integer k2 = (Integer) a2[TF_I_SORT_AUS_STUECKLISTE];

							if (k1 > k2) {
								alDaten.set(j, a2);
								alDaten.set(j + 1, a1);
							}
						}
					} else {
						if (((String) a1[TF_IDENT]).compareTo((String) a2[TF_IDENT]) > 0) {
							alDaten.set(j, a2);
							alDaten.set(j + 1, a1);
						} else if (((String) a1[TF_IDENT]).compareTo((String) a2[TF_IDENT]) == 0) {
							Date k1 = (Date) a1[TF_PRODUKTIONSBEGINN];
							Date k2 = (Date) a2[TF_PRODUKTIONSBEGINN];

							if (k1.after(k2)) {
								alDaten.set(j, a2);
								alDaten.set(j + 1, a1);
							}
						}
					}
				}
			}

			data = new Object[alDaten.size()][TF_SPALTENANZAHL];

			data = (Object[][]) alDaten.toArray(data);

			super.initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL,
					FertigungReportFac.REPORT_THEORETISCHE_FEHLMENGEN, theClientDto.getMandant(),
					theClientDto.getLocUi(), theClientDto);
			JasperPrintLP print = getReportPrint();

			return print;
		} catch (Exception t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, t);
		} finally {
			closeSession(sessionEinesLoses);
		}
	}

	public JasperPrintLP printLosEtikett(Integer losIId, BigDecimal bdMenge, String sKommentar, boolean bMitInhalten,
			Integer iExemplare, TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
		// TODO SK
		// Je nach bMitInhalten Inhalte mitdrucken oder nicht.
		// Inhalte drucken = getDataAusgabeliste(lodDto[0])
		// Inhalte nicht drucken = getDataAusgabeliste(losDto)
		// Paramerter mit gerParameter holen
		Map<String, Object> parameter = new TreeMap<String, Object>();
		this.useCase = UC_ETIKETT;
		this.index = -1;
		data = new Object[1][1];
		data[0][0] = "";
		parameter.put("P_ZUSATZKOMMENTAR", sKommentar);
		parameter.put("P_MITINHALT", new Boolean(bMitInhalten));
		parameter.put("P_MENGE", bdMenge);
		LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);
		// parameter = getParameterAusgabeliste(losDto,"",theClientDto);
		if (losDto != null) {
			parameter.put("P_CNR", losDto.getCNr());
			if (losDto.getAuftragIId() != null) {
				AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(losDto.getAuftragIId());
				if (auftragDto != null) {
					parameter.put("P_AUFTRAG", auftragDto.getCNr());

					parameter.put("P_AUFTRAG_LIEFERTERMIN", auftragDto.getDLiefertermin());

					parameter.put("P_ROHS", Helper.short2Boolean(auftragDto.getBRoHs()));
					if (auftragDto.getKundeIIdAuftragsadresse() != null) {
						KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(),
								theClientDto);
						if (kundeDto != null) {
							if (kundeDto.getPartnerDto() != null) {
								parameter.put("P_KUNDE_ANREDE", kundeDto.getPartnerDto().formatFixTitelName1Name2());
								parameter.put("P_KUNDE_ABTEILUNG",
										kundeDto.getPartnerDto().getCName3vorname2abteilung());
							}
							if (kundeDto.getPartnerDto().getLandplzortDto() == null) {
								if (kundeDto.getPartnerDto().getLandplzortIId() != null) {
									LandplzortDto lpoDto = getSystemFac()
											.landplzortFindByPrimaryKey(kundeDto.getPartnerDto().getLandplzortIId());
									kundeDto.getPartnerDto().setLandplzortDto(lpoDto);
								}
							}
							if (kundeDto.getPartnerDto().getLandplzortDto() != null) {
								parameter.put("P_KUNDE_LANDPLZORT",
										kundeDto.getPartnerDto().getLandplzortDto().formatLandPlzOrt());
							}
						}
					}
				}
			}
			if (losDto.getAuftragpositionIId() != null) {
				AuftragpositionDto auftragposDto = getAuftragpositionFac()
						.auftragpositionFindByPrimaryKey(losDto.getAuftragpositionIId());
				if (auftragposDto != null) {
					parameter.put("P_AUFTRAGPOSITION", auftragposDto.getISort());
					parameter.put("P_AUFTRAGPOSITION_LIEFERTERMIN", auftragposDto.getTUebersteuerbarerLiefertermin());
				}
			}
			if (losDto.getKostenstelleIId() != null) {
				KostenstelleDto kostDto = getSystemFac().kostenstelleFindByPrimaryKey(losDto.getKostenstelleIId());
				if (kostDto != null) {
					parameter.put("P_KOSTENSTELLENNUMMER", kostDto.getCNr());
					parameter.put("P_KOSTENSTELLENBEZEICHNUNG", kostDto.getCBez());
				}
			}
			if (losDto.getStuecklisteIId() != null) {
				parameter.put("P_LOSART", FertigungFac.LOSART_IDENT);
				StuecklisteDto stuecklisteDto = getStuecklisteFac()
						.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(), theClientDto);
				if (stuecklisteDto != null) {
					if (stuecklisteDto.getArtikelDto() == null) {
						ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(stuecklisteDto.getArtikelIId(),
								theClientDto);
						stuecklisteDto.setArtikelDto(artikelDto);
					}
					// Nochmal abfragen falls Artikel nicht gefunden wurde
					if (stuecklisteDto.getArtikelDto() != null) {
						parameter.put("P_STUECKLISTENNUMMER", stuecklisteDto.getArtikelDto().getCNr());
						if (stuecklisteDto.getArtikelDto().getArtikelsprDto() == null) {
							ArtikelsprDto artikelsprDto = getArtikelFac().artikelsprFindByArtikelIIdLocaleCNrOhneExc(
									stuecklisteDto.getArtikelIId(), theClientDto.getLocUiAsString(), theClientDto);
							stuecklisteDto.getArtikelDto().setArtikelsprDto(artikelsprDto);
						}
						if (stuecklisteDto.getArtikelDto().getArtikelsprDto() != null) {
							parameter.put("P_STUECKLISTENBEZEICHNUNG",
									stuecklisteDto.getArtikelDto().getArtikelsprDto().getCBez());
							parameter.put("P_ARTIKELZUSATZBEZEICHNUNG",
									stuecklisteDto.getArtikelDto().getArtikelsprDto().getCZbez());
							parameter.put("P_ARTIKELZUSATZBEZEICHNUNG2",
									stuecklisteDto.getArtikelDto().getArtikelsprDto().getCZbez2());
							parameter.put("P_ARTIKELKURZBEZEICHNUNG",
									stuecklisteDto.getArtikelDto().getArtikelsprDto().getCKbez());
						}

						parameter.put("P_STUECKLISTEREFERENZNUMMER", stuecklisteDto.getArtikelDto().getCReferenznr());
						parameter.put("P_STUECKLISTEMENGENEINHEIT", stuecklisteDto.getArtikelDto().getEinheitCNr());
					}
				}
			} else {
				parameter.put("P_LOSART", FertigungFac.LOSART_MATERIALLISTE);
			}
			parameter.put("P_LOSGROESSE", losDto.getNLosgroesse());
			if (losDto.getLagerIIdZiel() != null) {
				LagerDto lagerDto = getLagerFac().lagerFindByPrimaryKey(losDto.getLagerIIdZiel());
				if (lagerDto != null) {
					parameter.put("P_LAGER", lagerDto.getCNr());
				}
			}
			Date dBegin = losDto.getTProduktionsbeginn();
			Date dEnde = losDto.getTProduktionsende();
			parameter.put("P_BEGINN", dBegin);
			parameter.put("P_ENDE", dEnde);
			if ((dEnde != null) && (dBegin != null)) {
				parameter.put("P_DAUER", new Integer(Helper.getDifferenzInTagen(dBegin, dEnde)));
			}
			if (losDto.getPersonalIIdTechniker() != null) {
				PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(losDto.getPersonalIIdTechniker(),
						theClientDto);
				if (personalDto != null) {
					parameter.put("P_TECHNIKERNUMMER", personalDto.getCPersonalnr());
					parameter.put("P_TECHNIKERNAME", personalDto.formatFixUFTitelName2Name1());
				}
			}
			parameter.put("P_PROJEKT", losDto.getCProjekt());
			parameter.put("P_KOMMENTAR", losDto.getCKommentar());
			if (losDto.getFertigungsgruppeIId() != null) {
				FertigungsgruppeDto fertDto = getStuecklisteFac()
						.fertigungsgruppeFindByPrimaryKey(losDto.getFertigungsgruppeIId());
				if (fertDto != null) {
					parameter.put("P_FERTIGUNGSGRUPPE", fertDto.getCBez());
				}
			}
			if (losDto.getPartnerIIdFertigungsort() != null) {
				PartnerDto partnerDto = getPartnerFac()
						.partnerFindByPrimaryKeyOhneExc(losDto.getPartnerIIdFertigungsort(), theClientDto);
				if (partnerDto != null) {
					parameter.put("P_FERTIGUNGSORT", partnerDto.formatTitelAnrede());
				}
			}
			parameter.put("P_TEXT", losDto.getXText());

			// Inhalt
			LossollmaterialDto[] sollmaterialDtos = getFertigungFac().lossollmaterialFindByLosIIdOrderByISort(losIId);

			data = new Object[sollmaterialDtos.length][ETI_ANZAHL_FELDER];

			for (int i = 0; i < sollmaterialDtos.length; i++) {
				data[i][ETI_SOLLMENGE] = sollmaterialDtos[i].getNMenge();
				data[i][ETI_ISTMENGE] = getFertigungFac().getAusgegebeneMenge(sollmaterialDtos[i].getIId(), null,
						theClientDto);

				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(sollmaterialDtos[i].getArtikelIId(), theClientDto);

				data[i][ETI_ARTIKELNUMMER] = artikelDto.getCNr();
				if (artikelDto.getArtikelsprDto() != null) {
					data[i][ETI_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
					data[i][ETI_KURZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCKbez();
					data[i][ETI_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCZbez();
					data[i][ETI_ZUSATZBEZEICHNUNG2] = artikelDto.getArtikelsprDto().getCZbez2();
				}

				data[i][ETI_EINHEIT] = artikelDto.getEinheitCNr();
				data[i][ETI_REFERENZNUMMER] = artikelDto.getCReferenznr();

			}

		}

		JasperPrintLP print = null;
		Integer varianteIId = theClientDto.getReportvarianteIId();
		for (int i = 0; i < iExemplare; i++) {

			parameter.put("P_EXEMPLAR", new Integer(i + 1));
			parameter.put("P_EXEMPLAREGESAMT", iExemplare);
			theClientDto.setReportvarianteIId(varianteIId);
			if (print != null) {
				initJRDS(parameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_LOSETIKETT1,
						theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

				print = Helper.addReport2Report(print, getReportPrint().getPrint());
			} else {
				initJRDS(parameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_LOSETIKETT1,
						theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

				print = getReportPrint();
			}

		}

		return print;

	}

	public JasperPrintLP printLosVerpackungsetiketten(Integer losIId, String handKommentar, Integer iAnzahl,
			TheClientDto theClientDto) throws RemoteException {

		Map<String, Object> parameter = new TreeMap<String, Object>();
		this.useCase = UC_ETIKETT;
		this.index = -1;
		data = new Object[1][1];
		data[0][0] = "";

		LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);

		BigDecimal bdVerpackungsmenge = losDto.getNLosgroesse();

		parameter.put("P_CNR", losDto.getCNr());

		parameter.put("P_HAND_KOMMENTAR", handKommentar);

		// PJ19681
		Integer kundeIIdZugehoerig = getFertigungFac().getZugehoerigenKunden(losDto.getIId(), theClientDto);

		if (kundeIIdZugehoerig != null) {
			parameter.put("P_ZUGEHOERIGER_KUNDE", getKundeFac().kundeFindByPrimaryKey(kundeIIdZugehoerig, theClientDto)
					.getPartnerDto().formatFixName1Name2());
		}

		if (losDto.getAuftragIId() != null) {
			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(losDto.getAuftragIId());
			if (auftragDto != null) {
				parameter.put("P_AUFTRAG", auftragDto.getCNr());
				parameter.put("P_ROHS", Helper.short2Boolean(auftragDto.getBRoHs()));
				if (auftragDto.getKundeIIdAuftragsadresse() != null) {
					KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(),
							theClientDto);
					if (kundeDto != null) {
						if (kundeDto.getPartnerDto() != null) {
							parameter.put("P_KUNDE_ANREDE", kundeDto.getPartnerDto().formatFixTitelName1Name2());
							parameter.put("P_KUNDE_ABTEILUNG", kundeDto.getPartnerDto().getCName3vorname2abteilung());
						}
						if (kundeDto.getPartnerDto().getLandplzortDto() == null) {
							if (kundeDto.getPartnerDto().getLandplzortIId() != null) {
								LandplzortDto lpoDto = getSystemFac()
										.landplzortFindByPrimaryKey(kundeDto.getPartnerDto().getLandplzortIId());
								kundeDto.getPartnerDto().setLandplzortDto(lpoDto);
							}
						}
						if (kundeDto.getPartnerDto().getLandplzortDto() != null) {
							parameter.put("P_KUNDE_LANDPLZORT",
									kundeDto.getPartnerDto().getLandplzortDto().formatLandPlzOrt());
						}
					}
				}
			}
		}
		if (losDto.getAuftragpositionIId() != null) {
			AuftragpositionDto auftragposDto = getAuftragpositionFac()
					.auftragpositionFindByPrimaryKey(losDto.getAuftragpositionIId());
			if (auftragposDto != null) {
				parameter.put("P_AUFTRAGPOSITION", auftragposDto.getISort());
			}
		}
		if (losDto.getKostenstelleIId() != null) {
			KostenstelleDto kostDto = getSystemFac().kostenstelleFindByPrimaryKey(losDto.getKostenstelleIId());
			if (kostDto != null) {
				parameter.put("P_KOSTENSTELLENNUMMER", kostDto.getCNr());
				parameter.put("P_KOSTENSTELLENBEZEICHNUNG", kostDto.getCBez());
			}
		}
		if (losDto.getStuecklisteIId() != null) {
			parameter.put("P_LOSART", FertigungFac.LOSART_IDENT);
			StuecklisteDto stuecklisteDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(),
					theClientDto);
			if (stuecklisteDto != null) {
				if (stuecklisteDto.getArtikelDto() == null) {
					ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(stuecklisteDto.getArtikelIId(),
							theClientDto);
					stuecklisteDto.setArtikelDto(artikelDto);
				}
				// Nochmal abfragen falls Artikel nicht gefunden wurde
				if (stuecklisteDto.getArtikelDto() != null) {

					if (stuecklisteDto.getArtikelDto().getVerpackungsmittelIId() != null) {
						VerpackungsmittelDto verpackungsmittelDto = getArtikelFac().verpackungsmittelFindByPrimaryKey(
								stuecklisteDto.getArtikelDto().getVerpackungsmittelIId(), theClientDto);
						parameter.put("P_VERPACKUNGSMITTEL_KENNUNG", verpackungsmittelDto.getCNr());
						parameter.put("P_VERPACKUNGSMITTEL_BEZEICHNUNG", verpackungsmittelDto.getBezeichnung());

						parameter.put("P_VERPACKUNGSMITTEL_GEWICHT_IN_KG", verpackungsmittelDto.getNGewichtInKG());
					}

					parameter.put("P_VERPACKUNGSMITTELMENGE",
							stuecklisteDto.getArtikelDto().getNVerpackungsmittelmenge());

					parameter.put("P_FERTIGUNGSSATZGROESSE",
							stuecklisteDto.getArtikelDto().getFFertigungssatzgroesse());

					if (stuecklisteDto.getArtikelDto().getNVerpackungsmittelmenge() != null) {
						bdVerpackungsmenge = stuecklisteDto.getArtikelDto().getNVerpackungsmittelmenge();
					}

					parameter.put("P_STUECKLISTENNUMMER", stuecklisteDto.getArtikelDto().getCNr());
					if (stuecklisteDto.getArtikelDto().getArtikelsprDto() == null) {
						ArtikelsprDto artikelsprDto = getArtikelFac().artikelsprFindByArtikelIIdLocaleCNrOhneExc(
								stuecklisteDto.getArtikelIId(), theClientDto.getLocUiAsString(), theClientDto);
						stuecklisteDto.getArtikelDto().setArtikelsprDto(artikelsprDto);
					}
					if (stuecklisteDto.getArtikelDto().getArtikelsprDto() != null) {
						parameter.put("P_STUECKLISTENBEZEICHNUNG",
								stuecklisteDto.getArtikelDto().getArtikelsprDto().getCBez());
						parameter.put("P_ARTIKELZUSATZBEZEICHNUNG",
								stuecklisteDto.getArtikelDto().getArtikelsprDto().getCZbez());
						parameter.put("P_ARTIKELZUSATZBEZEICHNUNG2",
								stuecklisteDto.getArtikelDto().getArtikelsprDto().getCZbez2());
						parameter.put("P_ARTIKELKURZBEZEICHNUNG",
								stuecklisteDto.getArtikelDto().getArtikelsprDto().getCKbez());
					}

					parameter.put("P_STUECKLISTEREFERENZNUMMER", stuecklisteDto.getArtikelDto().getCReferenznr());
					parameter.put("P_STUECKLISTEMENGENEINHEIT", stuecklisteDto.getArtikelDto().getEinheitCNr());
				}
			}
		} else {
			parameter.put("P_LOSART", FertigungFac.LOSART_MATERIALLISTE);
		}
		parameter.put("P_LOSGROESSE", losDto.getNLosgroesse());
		if (losDto.getLagerIIdZiel() != null) {
			LagerDto lagerDto = getLagerFac().lagerFindByPrimaryKey(losDto.getLagerIIdZiel());
			if (lagerDto != null) {
				parameter.put("P_LAGER", lagerDto.getCNr());
			}
		}
		Date dBegin = losDto.getTProduktionsbeginn();
		Date dEnde = losDto.getTProduktionsende();
		parameter.put("P_BEGINN", dBegin);
		parameter.put("P_ENDE", dEnde);
		if ((dEnde != null) && (dBegin != null)) {
			parameter.put("P_DAUER", new Integer(Helper.getDifferenzInTagen(dBegin, dEnde)));
		}
		if (losDto.getPersonalIIdTechniker() != null) {
			PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(losDto.getPersonalIIdTechniker(),
					theClientDto);
			if (personalDto != null) {
				parameter.put("P_TECHNIKERNUMMER", personalDto.getCPersonalnr());
				parameter.put("P_TECHNIKERNAME", personalDto.formatFixUFTitelName2Name1());
			}
		}
		parameter.put("P_PROJEKT", losDto.getCProjekt());
		parameter.put("P_KOMMENTAR", losDto.getCKommentar());
		if (losDto.getFertigungsgruppeIId() != null) {
			FertigungsgruppeDto fertDto = getStuecklisteFac()
					.fertigungsgruppeFindByPrimaryKey(losDto.getFertigungsgruppeIId());
			if (fertDto != null) {
				parameter.put("P_FERTIGUNGSGRUPPE", fertDto.getCBez());
			}
		}
		if (losDto.getPartnerIIdFertigungsort() != null) {
			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKeyOhneExc(losDto.getPartnerIIdFertigungsort(),
					theClientDto);
			if (partnerDto != null) {
				parameter.put("P_FERTIGUNGSORT", partnerDto.formatTitelAnrede());
			}
		}
		parameter.put("P_TEXT", losDto.getXText());

		// Inhalt
		LossollmaterialDto[] sollmaterialDtos = getFertigungFac().lossollmaterialFindByLosIIdOrderByISort(losIId);

		data = new Object[sollmaterialDtos.length][ETI_ANZAHL_FELDER];

		for (int i = 0; i < sollmaterialDtos.length; i++) {
			data[i][ETI_SOLLMENGE] = sollmaterialDtos[i].getNMenge();
			data[i][ETI_ISTMENGE] = getFertigungFac().getAusgegebeneMenge(sollmaterialDtos[i].getIId(), null,
					theClientDto);

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(sollmaterialDtos[i].getArtikelIId(),
					theClientDto);

			data[i][ETI_ARTIKELNUMMER] = artikelDto.getCNr();
			if (artikelDto.getArtikelsprDto() != null) {
				data[i][ETI_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
				data[i][ETI_KURZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCKbez();
				data[i][ETI_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCZbez();
				data[i][ETI_ZUSATZBEZEICHNUNG2] = artikelDto.getArtikelsprDto().getCZbez2();
			}

			data[i][ETI_EINHEIT] = artikelDto.getEinheitCNr();
			data[i][ETI_REFERENZNUMMER] = artikelDto.getCReferenznr();

		}

		BigDecimal bdVerbleibendeMenge = losDto.getNLosgroesse();

		JasperPrintLP print = null;
		Integer varianteIId = theClientDto.getReportvarianteIId();
		if (losDto.getTVpEtikettengedruckt() != null) {
			iAnzahl = 1;
		}

		for (int i = 0; i < iAnzahl; i++) {

			if (losDto.getTVpEtikettengedruckt() == null) {

				parameter.put("P_EXEMPLAR", new Integer(i + 1));
				parameter.put("P_EXEMPLAREGESAMT", iAnzahl);

				if (i == iAnzahl - 1) {
					parameter.put("P_INHALTSMENGE", bdVerbleibendeMenge);
				} else {
					parameter.put("P_INHALTSMENGE", bdVerpackungsmenge);
				}
			}

			theClientDto.setReportvarianteIId(varianteIId);
			if (print != null) {
				initJRDS(parameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_VERPACKUNGSETIKETT,
						theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

				print = Helper.addReport2Report(print, getReportPrint().getPrint());
			} else {
				initJRDS(parameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_VERPACKUNGSETIKETT,
						theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

				print = getReportPrint();

			}

			bdVerbleibendeMenge = bdVerbleibendeMenge.subtract(bdVerpackungsmenge);

		}

		return print;

	}

	public JasperPrintLP printLosEtikettA4(Integer losIId, BigDecimal bdMenge, String sKommentar, boolean bMitInhalten,
			Integer iExemplare, TheClientDto theClientDto) throws RemoteException {
		// TODO SK
		// Je nach bMitInhalten Inhalte mitdrucken oder nicht.
		// Inhalte drucken = getDataAusgabeliste(lodDto[0])
		// Inhalte nicht drucken = getDataAusgabeliste(losDto)
		// Paramerter mit gerParameter holen
		Map<String, Object> parameter = new TreeMap<String, Object>();
		this.useCase = UC_ETIKETTA4;
		this.index = -1;

		ArrayList alDaten = new ArrayList();

		parameter.put("P_ZUSATZKOMMENTAR", sKommentar);
		parameter.put("P_MITINHALT", new Boolean(bMitInhalten));
		parameter.put("P_MENGE", bdMenge);
		parameter.put("P_EXEMPLAREGESAMT", iExemplare);

		LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);
		// parameter = getParameterAusgabeliste(losDto,"",theClientDto);
		if (losDto != null) {
			for (int ex = 0; ex < iExemplare; ex++) {

				Object[] zeile = new Object[ETI_A4_ANZAHL_FELDER];

				zeile[ETI_A4_LOSNUMMER] = losDto.getCNr();
				zeile[ETI_A4_EXEMPLAR] = ex + 1;

				if (losDto.getAuftragIId() != null) {

					AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(losDto.getAuftragIId());
					if (auftragDto != null) {

						zeile[ETI_A4_AUFTRAG] = auftragDto.getCNr();

						zeile[ETI_A4_AUFTRAG_LIEFERTERMIN] = auftragDto.getDLiefertermin();
						zeile[ETI_A4_ROHS] = Helper.short2Boolean(auftragDto.getBRoHs());

						if (auftragDto.getKundeIIdAuftragsadresse() != null) {
							KundeDto kundeDto = getKundeFac()
									.kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(), theClientDto);
							if (kundeDto != null) {
								if (kundeDto.getPartnerDto() != null) {

									zeile[ETI_A4_KUNDE_ABTEILUNG] = kundeDto.getPartnerDto()
											.getCName3vorname2abteilung();
									zeile[ETI_A4_KUNDE_ANREDE] = kundeDto.getPartnerDto().formatFixTitelName1Name2();

								}
								if (kundeDto.getPartnerDto().getLandplzortDto() == null) {
									if (kundeDto.getPartnerDto().getLandplzortIId() != null) {
										LandplzortDto lpoDto = getSystemFac().landplzortFindByPrimaryKey(
												kundeDto.getPartnerDto().getLandplzortIId());
										kundeDto.getPartnerDto().setLandplzortDto(lpoDto);
									}
								}
								if (kundeDto.getPartnerDto().getLandplzortDto() != null) {
									zeile[ETI_A4_KUNDE_LANDPLZORT] = kundeDto.getPartnerDto().getLandplzortDto()
											.formatLandPlzOrt();

								}
							}
						}
					}
				}
				if (losDto.getAuftragpositionIId() != null) {
					AuftragpositionDto auftragposDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(losDto.getAuftragpositionIId());
					if (auftragposDto != null) {

						zeile[ETI_A4_AUFTRAGSPOSITION] = auftragposDto.getISort();
						zeile[ETI_A4_AUFTRAGPOSITION_LIEFERTERMIN] = auftragposDto.getTUebersteuerbarerLiefertermin();

					}
				}
				if (losDto.getKostenstelleIId() != null) {
					KostenstelleDto kostDto = getSystemFac().kostenstelleFindByPrimaryKey(losDto.getKostenstelleIId());
					if (kostDto != null) {

						zeile[ETI_A4_KOSTENSTELLENNUMMER] = kostDto.getCNr();
						zeile[ETI_A4_KOSTENSTELLENBEZEICHNUNG] = kostDto.getCBez();

					}
				}
				if (losDto.getStuecklisteIId() != null) {

					zeile[ETI_A4_LOSART] = FertigungFac.LOSART_IDENT;

					StuecklisteDto stuecklisteDto = getStuecklisteFac()
							.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(), theClientDto);
					if (stuecklisteDto != null) {
						if (stuecklisteDto.getArtikelDto() == null) {
							ArtikelDto artikelDto = getArtikelFac()
									.artikelFindByPrimaryKey(stuecklisteDto.getArtikelIId(), theClientDto);
							stuecklisteDto.setArtikelDto(artikelDto);
						}
						// Nochmal abfragen falls Artikel nicht gefunden wurde
						if (stuecklisteDto.getArtikelDto() != null) {

							zeile[ETI_A4_STUECKLISTENNUMMER] = stuecklisteDto.getArtikelDto().getCNr();
							if (stuecklisteDto.getArtikelDto().getArtikelsprDto() == null) {
								ArtikelsprDto artikelsprDto = getArtikelFac()
										.artikelsprFindByArtikelIIdLocaleCNrOhneExc(stuecklisteDto.getArtikelIId(),
												theClientDto.getLocUiAsString(), theClientDto);
								stuecklisteDto.getArtikelDto().setArtikelsprDto(artikelsprDto);
							}
							if (stuecklisteDto.getArtikelDto().getArtikelsprDto() != null) {

								zeile[ETI_A4_STUECKLISTENBEZEICHNUNG] = stuecklisteDto.getArtikelDto()
										.getArtikelsprDto().getCBez();
								zeile[ETI_A4_STUECKLISTEKURZBEZEICHNUNG] = stuecklisteDto.getArtikelDto()
										.getArtikelsprDto().getCKbez();
								zeile[ETI_A4_STUECKLISTEZUSATZBEZEICHNUNG] = stuecklisteDto.getArtikelDto()
										.getArtikelsprDto().getCZbez();
								zeile[ETI_A4_STUECKLISTEZUSATZBEZEICHNUNG2] = stuecklisteDto.getArtikelDto()
										.getArtikelsprDto().getCZbez2();

							}
							zeile[ETI_A4_STUECKLISTEMENGENEINHEIT] = stuecklisteDto.getArtikelDto().getEinheitCNr();
							zeile[ETI_A4_STUECKLISTEREFERENZNUMMER] = stuecklisteDto.getArtikelDto().getCReferenznr();
						}
					}
				} else {
					zeile[ETI_A4_LOSART] = FertigungFac.LOSART_MATERIALLISTE;
				}

				zeile[ETI_A4_LOSGROESSE] = losDto.getNLosgroesse();

				if (losDto.getLagerIIdZiel() != null) {
					LagerDto lagerDto = getLagerFac().lagerFindByPrimaryKey(losDto.getLagerIIdZiel());
					if (lagerDto != null) {

						zeile[ETI_A4_LAGER] = lagerDto.getCNr();
					}
				}
				Date dBegin = losDto.getTProduktionsbeginn();
				Date dEnde = losDto.getTProduktionsende();

				zeile[ETI_A4_BEGINN] = dBegin;
				zeile[ETI_A4_ENDE] = dEnde;

				if ((dEnde != null) && (dBegin != null)) {
					zeile[ETI_A4_DAUER] = new Integer(Helper.getDifferenzInTagen(dBegin, dEnde));
				}
				if (losDto.getPersonalIIdTechniker() != null) {
					PersonalDto personalDto = getPersonalFac()
							.personalFindByPrimaryKey(losDto.getPersonalIIdTechniker(), theClientDto);
					if (personalDto != null) {

						zeile[ETI_A4_TECHNIKERNAME] = personalDto.formatFixUFTitelName2Name1();
						zeile[ETI_A4_TECHNIKERNUMMER] = personalDto.getCPersonalnr();

					}
				}

				zeile[ETI_A4_PROJEKT] = losDto.getCProjekt();
				zeile[ETI_A4_KOMMENTAR] = losDto.getCKommentar();

				if (losDto.getFertigungsgruppeIId() != null) {
					FertigungsgruppeDto fertDto = getStuecklisteFac()
							.fertigungsgruppeFindByPrimaryKey(losDto.getFertigungsgruppeIId());
					if (fertDto != null) {
						zeile[ETI_A4_FERTIGUNGSGRUPPE] = fertDto.getCBez();

					}
				}
				if (losDto.getPartnerIIdFertigungsort() != null) {
					PartnerDto partnerDto = getPartnerFac()
							.partnerFindByPrimaryKeyOhneExc(losDto.getPartnerIIdFertigungsort(), theClientDto);
					if (partnerDto != null) {
						zeile[ETI_A4_FERTIGUNGSORT] = partnerDto.formatTitelAnrede();

					}
				}

				zeile[ETI_A4_TEXT] = losDto.getXText();

				if (bMitInhalten == true) {

					// Inhalt
					LossollmaterialDto[] sollmaterialDtos = getFertigungFac()
							.lossollmaterialFindByLosIIdOrderByISort(losIId);

					for (int i = 0; i < sollmaterialDtos.length; i++) {

						Object[] zeileSoll = zeile.clone();
						zeileSoll[ETI_A4_SOLLMATERIAL_SOLLMENGE] = sollmaterialDtos[i].getNMenge();
						zeileSoll[ETI_A4_SOLLMATERIAL_ISTMENGE] = getFertigungFac()
								.getAusgegebeneMenge(sollmaterialDtos[i].getIId(), null, theClientDto);

						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(sollmaterialDtos[i].getArtikelIId(), theClientDto);

						zeileSoll[ETI_A4_SOLLMATERIAL_ARTIKELNUMMER] = artikelDto.getCNr();
						if (artikelDto.getArtikelsprDto() != null) {
							zeileSoll[ETI_A4_SOLLMATERIAL_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
							zeileSoll[ETI_A4_SOLLMATERIAL_KURZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCKbez();
							zeileSoll[ETI_A4_SOLLMATERIAL_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCZbez();
							zeileSoll[ETI_A4_SOLLMATERIAL_ZUSATZBEZEICHNUNG2] = artikelDto.getArtikelsprDto()
									.getCZbez2();
						}

						zeileSoll[ETI_A4_SOLLMATERIAL_EINHEIT] = artikelDto.getEinheitCNr();
						zeileSoll[ETI_A4_SOLLMATERIAL_REFERENZNUMMER] = artikelDto.getCReferenznr();
						alDaten.add(zeileSoll);
					}
				} else {
					alDaten.add(zeile);
				}
			}
		}
		data = new Object[alDaten.size()][ETI_A4_ANZAHL_FELDER];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(parameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_LOSETIKETTA4,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	public ArrayList<FehlmengenBeiAusgabeMehrererLoseDto> getFehlmengenBeiAusgabeMehrerLoseEinesStuecklistenbaumes(
			Integer losIId, TheClientDto theClientDto) {

		TreeSet<Integer> iids = getFertigungFac().getLoseEinesStuecklistenbaums(losIId, theClientDto);

		Object[][] zeilen = getDataAusgabeListe((Integer[]) iids.toArray(new Integer[iids.size()]),
				Helper.SORTIERUNG_NACH_IDENT, true, false, null, theClientDto);

		ArrayList<FehlmengenBeiAusgabeMehrererLoseDto> al = new ArrayList<FehlmengenBeiAusgabeMehrererLoseDto>();

		for (int i = 0; i < zeilen.length; i++) {

			Object[] zeile = zeilen[i];

			BigDecimal sollmenge = (BigDecimal) zeile[AUSG_MENGE];
			BigDecimal lagerstand = (BigDecimal) zeile[AUSG_LAGERSTAND];
			Integer artikelIId = null;
			try {
				ArtikelDto aDto = getArtikelFac().artikelFindByCNrOhneExc((String) zeile[AUSG_IDENT], theClientDto);

				if (aDto != null) {
					artikelIId = aDto.getIId();
				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			if (sollmenge.doubleValue() >= lagerstand.doubleValue()) {

				FehlmengenBeiAusgabeMehrererLoseDto fm = new FehlmengenBeiAusgabeMehrererLoseDto(
						(String) zeile[AUSG_LAGER], (String) zeile[AUSG_IDENT], (String) zeile[AUSG_BEZEICHNUNG],
						sollmenge, lagerstand, artikelIId);

				al.add(fm);

			}

		}

		return al;
	}

	public JasperPrintLP printAusgabeListe(Integer[] losIId, Integer iSortierung, boolean bVerdichtetNachIdent,
			boolean bVorrangigNachFarbcodeSortiert, Integer artikelklasseIId, String alternativerReport,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Session session = null;
		try {
			this.useCase = UC_AUSGABELISTE;
			this.index = -1;
			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			String lose = "";
			for (int i = 0; i < losIId.length; i++) {

				// Los holen
				LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId[i]);
				lose += losDto.getCNr() + ", ";

				ArrayList<Object> al = new ArrayList<Object>();
				al.add(losDto.getCNr());

				if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN, al,
							null);
				} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT, al, null);
				}
			}
			data = getDataAusgabeListe(losIId, iSortierung, bVerdichtetNachIdent, bVorrangigNachFarbcodeSortiert,
					artikelklasseIId, theClientDto);
			LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId[0]);
			mapParameter = getParameterAusgabeliste(losDto, lose, iSortierung, theClientDto);

			String sLosStuecklisteArtikelKommentar = "";
			// Bild einfuegen
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stuecklisteDto = getStuecklisteFac()
						.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(), theClientDto);
				ArtikelkommentarDto[] artikelkommentarDto = getArtikelkommentarFac()
						.artikelkommentardruckFindByArtikelIIdBelegartCNr(stuecklisteDto.getArtikelIId(),
								LocaleFac.BELEGART_LOS, theClientDto.getLocUiAsString(), theClientDto);

				// Sperren
				String sperren = getArtikelFac().getArtikelsperrenText(stuecklisteDto.getArtikelIId());

				mapParameter.put("P_STUECKLISTESPERRSTATUS", sperren);

				if (artikelkommentarDto != null && artikelkommentarDto.length > 0) {

					ArrayList<Object[]> subreportArtikelkommentare = new ArrayList<Object[]>();

					String[] fieldnames = new String[] { "F_KOMMENTARART", "F_MIMETYPE", "F_BILD", "F_KOMMENTAR", };

					int iFeld_Subreport_Kommentarart = 0;
					int iFeld_Subreport_Mimetype = 1;
					int iFeld_Subreport_Bild = 2;
					int iFeld_Subreport_Kommentar = 3;
					int iFeld_Subreport_iAnzahlSpalten = 4;

					for (int j = 0; j < artikelkommentarDto.length; j++) {
						if (artikelkommentarDto[j].getArtikelkommentarsprDto() != null) {

							Object[] oZeileVorlage = new Object[iFeld_Subreport_iAnzahlSpalten];

							ArtikelkommentarartDto artikelkommentarartDto = getArtikelkommentarFac()
									.artikelkommentarartFindByPrimaryKey(
											artikelkommentarDto[j].getArtikelkommentarartIId(), theClientDto);

							oZeileVorlage[iFeld_Subreport_Kommentarart] = artikelkommentarartDto.getCNr();

							oZeileVorlage[iFeld_Subreport_Mimetype] = artikelkommentarDto[j].getDatenformatCNr();
							oZeileVorlage[iFeld_Subreport_Kommentar] = artikelkommentarDto[j]
									.getArtikelkommentarsprDto().getXKommentar();

							// Text Kommentar
							if (artikelkommentarDto[j].getDatenformatCNr().trim()
									.indexOf(MediaFac.DATENFORMAT_MIMETYPEART_TEXT) != -1) {
								if (sLosStuecklisteArtikelKommentar == "") {
									sLosStuecklisteArtikelKommentar += artikelkommentarDto[j]
											.getArtikelkommentarsprDto().getXKommentar();
								} else {
									sLosStuecklisteArtikelKommentar += "\n"
											+ artikelkommentarDto[j].getArtikelkommentarsprDto().getXKommentar();
								}

								Object[] oZeile = oZeileVorlage.clone();
								subreportArtikelkommentare.add(oZeile);

							} else if (artikelkommentarDto[j].getDatenformatCNr()
									.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
									|| artikelkommentarDto[j].getDatenformatCNr()
											.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)
									|| artikelkommentarDto[j].getDatenformatCNr()
											.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)) {
								byte[] bild = artikelkommentarDto[j].getArtikelkommentarsprDto().getOMedia();
								if (bild != null) {
									java.awt.Image myImage = Helper.byteArrayToImage(bild);

									Object[] oZeile = oZeileVorlage.clone();
									oZeile[iFeld_Subreport_Bild] = myImage;

									subreportArtikelkommentare.add(oZeile);

								}
							} else if (artikelkommentarDto[j].getDatenformatCNr()
									.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF)) {

								byte[] bild = artikelkommentarDto[j].getArtikelkommentarsprDto().getOMedia();

								java.awt.Image[] tiffs = Helper.tiffToImageArray(bild);
								if (tiffs != null) {
									for (int k = 0; k < tiffs.length; k++) {
										Object[] oZeile = oZeileVorlage.clone();
										oZeile[iFeld_Subreport_Bild] = tiffs[k];

										subreportArtikelkommentare.add(oZeile);
									}
								}

							} else if (artikelkommentarDto[j].getDatenformatCNr()
									.equals(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF)) {

								byte[] pdf = artikelkommentarDto[j].getArtikelkommentarsprDto().getOMedia();

								PDDocument document = null;

								try {

									InputStream myInputStream = new ByteArrayInputStream(pdf);

									document = PDDocument.load(myInputStream);
									int numPages = document.getNumberOfPages();
									PDFRenderer renderer = new PDFRenderer(document);

									for (int p = 0; p < numPages; p++) {

										BufferedImage image = renderer.renderImageWithDPI(p, 150); // Windows
										Object[] oZeile = oZeileVorlage.clone();
										oZeile[iFeld_Subreport_Bild] = image;

										subreportArtikelkommentare.add(oZeile);

									}
								} catch (IOException e) {
									e.printStackTrace();
									throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e.getMessage());

								} finally {
									if (document != null) {

										try {
											document.close();
										} catch (IOException e) {
											e.printStackTrace();
											throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e.getMessage());

										}
									}

								}

							}
						}
					}

					Object[][] dataSub = new Object[subreportArtikelkommentare.size()][fieldnames.length];
					dataSub = (Object[][]) subreportArtikelkommentare.toArray(dataSub);

					// SP2801
					mapParameter.put("P_SUBREPORT_ARTIKELKOMMENTAR", new LPDatenSubreport(dataSub, fieldnames));

				}
			}
			if (sLosStuecklisteArtikelKommentar != "") {
				mapParameter.put("P_STUECKLISTEARTIKELKOMMENTAR",
						Helper.formatStyledTextForJasper(sLosStuecklisteArtikelKommentar));
			}
			mapParameter.put("P_VERDICHTET_NACH_IDENT", bVerdichtetNachIdent);

			if (alternativerReport != null) {
				initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, alternativerReport, theClientDto.getMandant(),
						theClientDto.getLocUi(), theClientDto);
			} else {

				String report = FertigungReportFac.REPORT_AUSGABELISTE;

				// PJ 17672
				FertigungsgruppeDto fertGruppeDto = getStuecklisteFac()
						.fertigungsgruppeFindByPrimaryKey(losDto.getFertigungsgruppeIId());
				if (fertGruppeDto.getIFormularnummer() != null) {
					report = report.replace(".", fertGruppeDto.getIFormularnummer() + ".");
				}

				initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, report, theClientDto.getMandant(),
						theClientDto.getLocUi(), theClientDto);
			}

			JasperPrintLP print = getReportPrint();
			if (print != null) {
				PrintInfoDto values = new PrintInfoDto();
				values.setDocPath(new DocPath(new DocNodeLos(losDto)));
				print.setOInfoForArchive(values);
			}

			return print;

		} catch (RemoteException t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, t);
		} finally {
			closeSession(session);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printArbeitszeitstatus(DatumsfilterVonBis datumsfilter, TheClientDto theClientDto) {

		this.useCase = UC_ARBEITSZEITSTATUS;

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		parameter.put("P_VON", datumsfilter.getTimestampVon());
		parameter.put("P_BIS", datumsfilter.getTimestampBisUnveraendert());

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		TreeMap<String, Object[]> tmDaten = new TreeMap<String, Object[]>();
		String sQuery = "select zeitdaten from FLRZeitdatenLos zeitdaten WHERE zeitdaten.i_belegartpositionid IS NOT NULL AND  zeitdaten.c_belegartnr ='"
				+ LocaleFac.BELEGART_LOS + "' AND zeitdaten.t_zeit>='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(datumsfilter.getTimestampVon().getTime()))
				+ "' AND zeitdaten.t_zeit<='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(datumsfilter.getTimestampBis().getTime()))
				+ "' AND zeitdaten.flrpersonal.mandant_c_nr='" + theClientDto.getMandant() + "'";

		org.hibernate.Query inventurliste = session.createQuery(sQuery);

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {

			FLRZeitdatenLos flrZeitdatenLos = (FLRZeitdatenLos) resultListIterator.next();

			if (flrZeitdatenLos.getFlrlossollarbeitsplan() != null) {

				try {
					flrZeitdatenLos.getFlrlossollarbeitsplan().getFlrlos();
				} catch (Throwable e) {
					// Dann wurde die Los-Position geloescht
					// SP6584
					try {
						ZeitdatenDto zDto = getZeiterfassungFac().zeitdatenFindByPrimaryKey(flrZeitdatenLos.getI_id(),
								theClientDto);
						if (zDto.getIBelegartid() != null) {
							LosDto losDto = getFertigungFac().losFindByPrimaryKeyOhneExc(zDto.getIBelegartid());
							if (losDto != null) {
								ArrayList alDaten = new ArrayList();
								alDaten.add(HelperServer
										.formatPersonAusFLRPartner(flrZeitdatenLos.getFlrpersonal().getFlrpartner()));
								alDaten.add(flrZeitdatenLos.getT_zeit());
								throw new EJBExceptionLP(EJBExceptionLP.FEHLER_LOSARBEITSPLAN_GELOESCHT, alDaten,
										new Exception("FEHLER_LOSARBEITSPLAN_GELOESCHT"));

							}
						}
					} catch (RemoteException e1) {
						throwEJBExceptionLPRespectOld(e1);
					}

					continue;
				}

				String key = flrZeitdatenLos.getFlrlossollarbeitsplan().getFlrlos().getC_nr() + ""
						+ Helper.fitString2LengthAlignRight(
								flrZeitdatenLos.getFlrlossollarbeitsplan().getI_arbeitsgangsnummer() + "", 5, ' ')
						+ Helper.fitString2LengthAlignRight(
								flrZeitdatenLos.getFlrlossollarbeitsplan().getI_unterarbeitsgang() + "", 5, ' ');

				if (!tmDaten.containsKey(key)) {
					Object[] oZeile = new Object[AZSTATUS_ANZAHL_SPALTEN];
					oZeile[AZSTATUS_LOSNUMMER] = flrZeitdatenLos.getFlrlossollarbeitsplan().getFlrlos().getC_nr();
					oZeile[AZSTATUS_PROJEKT] = flrZeitdatenLos.getFlrlossollarbeitsplan().getFlrlos().getC_projekt();
					oZeile[AZSTATUS_LOSGROESSE] = flrZeitdatenLos.getFlrlossollarbeitsplan().getFlrlos()
							.getN_losgroesse();
					oZeile[AZSTATUS_ARBEITSGANG] = flrZeitdatenLos.getFlrlossollarbeitsplan().getI_arbeitsgangsnummer();
					oZeile[AZSTATUS_FERTIG] = Helper
							.short2Boolean(flrZeitdatenLos.getFlrlossollarbeitsplan().getB_fertig());
					oZeile[AZSTATUS_FORTSCHRITT] = flrZeitdatenLos.getFlrlossollarbeitsplan().getF_fortschritt();
					if (flrZeitdatenLos.getFlrlossollarbeitsplan().getFlrlos().getPersonal_i_id_techniker() != null) {
						oZeile[AZSTATUS_TECHNIKER] = getPersonalFac().personalFindByPrimaryKey(
								flrZeitdatenLos.getFlrlossollarbeitsplan().getFlrlos().getPersonal_i_id_techniker(),
								theClientDto).formatFixName1Name2();
					}
					oZeile[AZSTATUS_UNTERARBEITSGANG] = flrZeitdatenLos.getFlrlossollarbeitsplan()
							.getI_unterarbeitsgang();
					oZeile[AZSTATUS_SOLL_PERSON] = flrZeitdatenLos.getFlrlossollarbeitsplan().getN_gesamtzeit();

					AuftragzeitenDto[] mannZeiten = getZeiterfassungFac().getAllZeitenEinesBeleges(
							LocaleFac.BELEGART_LOS, flrZeitdatenLos.getFlrlossollarbeitsplan().getFlrlos().getI_id(),
							flrZeitdatenLos.getFlrlossollarbeitsplan().getI_id(), null, null, null,
							ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ARTIKEL, theClientDto);

					BigDecimal bdZeitPerson = new BigDecimal(0);

					for (int i = 0; i < mannZeiten.length; i++) {

						bdZeitPerson = bdZeitPerson.add(new BigDecimal(mannZeiten[i].getDdDauer().doubleValue()));

					}

					oZeile[AZSTATUS_IST_PERSON] = bdZeitPerson;

					if (flrZeitdatenLos.getFlrartikel() != null) {
						oZeile[AZSTATUS_TAETIGKEIT] = flrZeitdatenLos.getFlrartikel().getC_nr();

						oZeile[AZSTATUS_TAETIGKEIT_BEZEICHNUNG] = getArtikelFac()
								.artikelFindByPrimaryKeySmall(flrZeitdatenLos.getFlrartikel().getI_id(), theClientDto)
								.formatBezeichnung();
					}

					if (flrZeitdatenLos.getFlrlossollarbeitsplan().getFlrlos().getFlrstueckliste() != null) {

						oZeile[AZSTATUS_STUECKLISTE] = flrZeitdatenLos.getFlrlossollarbeitsplan().getFlrlos()
								.getFlrstueckliste().getFlrartikel().getC_nr();

						oZeile[AZSTATUS_STUECKLISTEBEZEICHNUNG] = getArtikelFac()
								.artikelFindByPrimaryKeySmall(flrZeitdatenLos.getFlrlossollarbeitsplan().getFlrlos()
										.getFlrstueckliste().getFlrartikel().getI_id(), theClientDto)
								.formatBezeichnung();
					}

					tmDaten.put(key, oZeile);
				}

			}

		}
		session.close();

		Iterator itArtikel = tmDaten.keySet().iterator();
		ArrayList alDaten = new ArrayList();
		while (itArtikel.hasNext()) {

			Object[] o = tmDaten.get(itArtikel.next());
			alDaten.add(o);
		}

		data = new Object[alDaten.size()][AZSTATUS_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(parameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_ARBEITSZEITSTATUS,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	public JasperPrintLP printProduktionsinformation(Integer losIId, TheClientDto theClientDto) {
		this.useCase = UC_PRODUKTIONSINFORMATION;

		Map<String, Object> mapParameter = new TreeMap<String, Object>();

		try {
			LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);
			KostenstelleDto kstDto = getSystemFac().kostenstelleFindByPrimaryKey(losDto.getKostenstelleIId());

			String sAuftragsnummer = "";
			String sInternerKommentar = "";
			String sKunde = "";
			Timestamp dLiefertermin = null;
			String sLieferart = "";
			String sAbteilung = null;
			String sSpediteur = null;
			Boolean bPoenale = Boolean.FALSE;
			Boolean bRoHs = Boolean.FALSE;
			String sKundenbestellnummer = null;

			if (losDto.getAuftragIId() != null) {
				// Auftrag holen
				AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(losDto.getAuftragIId());
				sAuftragsnummer = auftragDto.getCNr();
				sLieferart = getLocaleFac().lieferartFindByIIdLocaleOhneExc(auftragDto.getLieferartIId(),
						theClientDto.getLocUi(), theClientDto);

				if (auftragDto.getSpediteurIId() != null) {
					sSpediteur = getMandantFac().spediteurFindByPrimaryKey(auftragDto.getSpediteurIId())
							.getCNamedesspediteurs();
				}

				bPoenale = Helper.short2Boolean(auftragDto.getBPoenale());
				bRoHs = Helper.short2Boolean(auftragDto.getBRoHs());

				sKundenbestellnummer = auftragDto.getCBestellnummer();

				// Internen Kommentar aus dem Auftrag, abhaengig von einem
				// Mandantenparameter
				ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_KOMMENTAR_AM_FERTIGUNGSBEGLEITSCHEIN);
				short iValue = Short.parseShort(parameter.getCWert());
				boolean bDruckeKommentar = Helper.short2boolean(iValue);
				if (bDruckeKommentar) {
					sInternerKommentar = auftragDto.getXInternerkommentar();
				}
				// Kunde holen
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(),
						theClientDto);
				sKunde = kundeDto.getPartnerDto().getCName1nachnamefirmazeile1();

				sAbteilung = kundeDto.getPartnerDto().getCName3vorname2abteilung();

				if (losDto.getAuftragpositionIId() != null) {
					AuftragpositionDto aufposDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(losDto.getAuftragpositionIId());
					if (aufposDto.getTUebersteuerbarerLiefertermin() != null) {
						dLiefertermin = aufposDto.getTUebersteuerbarerLiefertermin();
					} else {
						dLiefertermin = auftragDto.getDLiefertermin();
					}
				} else {
					dLiefertermin = auftragDto.getDLiefertermin();
				}
			} else if (losDto.getKundeIId() != null) {
				// Kunde holen
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(losDto.getKundeIId(), theClientDto);
				sKunde = kundeDto.getPartnerDto().getCName1nachnamefirmazeile1();

				sAbteilung = kundeDto.getPartnerDto().getCName3vorname2abteilung();
			}

			mapParameter.put("P_AUFTRAGNUMMER", sAuftragsnummer);
			mapParameter.put("P_BESTELLNUMMER", sKundenbestellnummer);
			mapParameter.put("P_AUFTRAG_INTERNERKOMMENTAR", sInternerKommentar);
			mapParameter.put("P_AUFTRAG_KUNDE_ABTEILUNG", sAbteilung);
			mapParameter.put("P_LIEFERART", sLieferart);
			mapParameter.put("P_SPEDITEUR", sSpediteur);
			mapParameter.put("P_POENALE", bPoenale);
			mapParameter.put("P_ROHS", bRoHs);

			PersonalDto personalBenutzer = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(),
					theClientDto);
			mapParameter.put("P_BENUTZER", personalBenutzer.getCKurzzeichen());

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			mapParameter.put("Mandantenadresse", Helper.formatMandantAdresse(mandantDto));

			mapParameter.put("P_ANGELEGT", new java.util.Date(losDto.getTAnlegen().getTime()));

			if (losDto.getTErledigt() != null) {
				mapParameter.put("P_ERLEDIGT", new java.util.Date(losDto.getTErledigt().getTime()));
			}

			mapParameter.put("P_KOSTENSTELLENUMMER", kstDto.getCNr());
			mapParameter.put("P_KUNDE", sKunde);
			mapParameter.put("P_LOSGROESSE", losDto.getNLosgroesse());
			mapParameter.put("P_LOSNUMMER", losDto.getCNr());
			mapParameter.put("P_PRODUKTIONSBEGINN", losDto.getTProduktionsbeginn());
			mapParameter.put("P_PRODUKTIONSENDE", losDto.getTProduktionsende());
			mapParameter.put("P_PROJEKT", losDto.getCProjekt());
			mapParameter.put("P_LOSKOMMENTAR", losDto.getCKommentar());
			mapParameter.put("P_LOSLANGTEXT", losDto.getXText());

			mapParameter.put("P_PRODUKTIONSINFORMATION", losDto.getXProduktionsinformation());

			mapParameter.put("P_LIEFERTERMIN", dLiefertermin);
			// Materialliste?
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stkDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(),
						theClientDto);

				mapParameter.put("P_STUECKLISTEBEZEICHNUNG", stkDto.getArtikelDto().getArtikelsprDto().getCBez());

				mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG",
						stkDto.getArtikelDto().getArtikelsprDto().getCZbez());
				mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG2",
						stkDto.getArtikelDto().getArtikelsprDto().getCZbez2());
				mapParameter.put("P_STUECKLISTENUMMER", stkDto.getArtikelDto().getCNr());

				mapParameter.put("P_STUECKLISTE_GEWICHTKG", stkDto.getArtikelDto().getFGewichtkg());

				if (stkDto.getArtikelDto().getVerpackungDto() != null) {
					mapParameter.put("P_STUECKLISTE_BAUFORM", stkDto.getArtikelDto().getVerpackungDto().getCBauform());
					mapParameter.put("P_STUECKLISTE_VERPACKUNGSART",
							stkDto.getArtikelDto().getVerpackungDto().getCVerpackungsart());
				}

				if (stkDto.getArtikelDto().getGeometrieDto() != null) {
					mapParameter.put("P_STUECKLISTE_BREITETEXT",
							stkDto.getArtikelDto().getGeometrieDto().getCBreitetext());
					mapParameter.put("P_STUECKLISTE_BREITE", stkDto.getArtikelDto().getGeometrieDto().getFBreite());
					mapParameter.put("P_STUECKLISTE_HOEHE", stkDto.getArtikelDto().getGeometrieDto().getFHoehe());
					mapParameter.put("P_STUECKLISTE_TIEFE", stkDto.getArtikelDto().getGeometrieDto().getFTiefe());
				}

			} else {
				mapParameter.put("P_STUECKLISTEBEZEICHNUNG", losDto.getCProjekt());
				mapParameter.put("P_STUECKLISTENUMMER",
						getTextRespectUISpr("fert.materialliste", theClientDto.getMandant(), theClientDto.getLocUi()));
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		ArrayList alData = new ArrayList();

		ArrayList<JCRDocDto> dokumente = getJCRDocFac().holeDokumenteZuUseCase(losIId, QueryParameters.UC_ID_LOS,
				theClientDto);

		for (int i = 0; i < dokumente.size(); i++) {

			if (((JCRDocDto) dokumente.get(i)).getsMIME() != null) {

				String sMime = ((JCRDocDto) dokumente.get(i)).getsMIME();

				if (".JPG".equals(sMime.toUpperCase()) || ".JPEG".equals(sMime.toUpperCase())
						|| ".GIF".equals(sMime.toUpperCase()) || ".PNG".equals(sMime.toUpperCase())
						|| ".TIFF".equals(sMime.toUpperCase()) || ".BMP".equals(sMime.toUpperCase())) {
					Object[] zeile = new Object[PI_ANZAHL_FELDER];

					zeile[PI_BILD] = Helper.byteArrayToImage(((JCRDocDto) dokumente.get(i)).getbData());
					zeile[PI_FILENAME] = ((JCRDocDto) dokumente.get(i)).getsFilename();
					zeile[PI_NAME] = ((JCRDocDto) dokumente.get(i)).getsName();
					zeile[PI_SCHLAGWORTE] = ((JCRDocDto) dokumente.get(i)).getsSchlagworte();

					alData.add(zeile);
				}

			}

		}

		data = new Object[alData.size()][PI_ANZAHL_FELDER];
		data = (Object[][]) alData.toArray(data);

		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_PRODUKTIONSINFORMATION,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();

	}

	public void traceImportBuchen(ArrayList<BucheSerienChnrAufLosDto> zuBuchen, TheClientDto theClientDto) {

		for (int i = 0; i < zuBuchen.size(); i++) {

			try {
				LossollmaterialDto lossollmaterialDto = getFertigungFac()
						.lossollmaterialFindByPrimaryKey(zuBuchen.get(i).getLossollmaterialIId());

				ArrayList<BucheSerienChnrAufLosDto> bucheSerienChnrAufLosDtos = new ArrayList<BucheSerienChnrAufLosDto>();
				bucheSerienChnrAufLosDtos.add(zuBuchen.get(i));

				getFertigungFac().bucheMaterialAufLos(
						getFertigungFac().losFindByPrimaryKey(lossollmaterialDto.getLosIId()),
						zuBuchen.get(i).getNMenge(), false, false, true, theClientDto, bucheSerienChnrAufLosDtos,
						false);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printTraceImport(String filename, ArrayList<String[]> alDatenCSV, TheClientDto theClientDto) {

		this.useCase = UC_TRACEIMPORT;

		TreeMap<String, ArrayList<String[]>> lhmGruppiertNachSatz = new TreeMap<String, ArrayList<String[]>>();

		HashMap<String, Integer> lhmAnzahlPCB = new HashMap<String, Integer>();

		ArrayList<TraceImportDto> alZuBuchen = new ArrayList<TraceImportDto>();

		String fehlerAllgemein = "";

		int csvBatchIndex = 9;
		int csvItemIndex = 0;
		int csvPCBIndex = 4;

		if (!alDatenCSV.isEmpty() && alDatenCSV.get(0).length > 13) {
			csvBatchIndex = 10;
		}

		String lastPCB = "-1";
		for (int i = 1; i < alDatenCSV.size(); i++) {

			String[] sZeile = alDatenCSV.get(i);

			if (sZeile.length > 12) {

				String artikelKbez = sZeile[csvItemIndex];
				String chargennummer = sZeile[csvBatchIndex];
				String pcb = Helper.fitString2LengthAlignRight(sZeile[csvPCBIndex], 5, '0');

				Integer anzahlPCB = null;
				if (!lhmAnzahlPCB.containsKey(artikelKbez)) {
					anzahlPCB = 0;
				} else {
					anzahlPCB = lhmAnzahlPCB.get(artikelKbez);
				}

				if (!lastPCB.equals(pcb)) {
					anzahlPCB++;
				}
				lastPCB = pcb;

				lhmAnzahlPCB.put(artikelKbez, anzahlPCB);

				pcb = "";

				String sKey = artikelKbez + ";" + chargennummer + ";" + pcb;

				ArrayList<String[]> alTemp = null;

				if (lhmGruppiertNachSatz.containsKey(sKey)) {
					alTemp = lhmGruppiertNachSatz.get(sKey);
				} else {
					alTemp = new ArrayList<String[]>();
				}

				alTemp.add(sZeile);

				lhmGruppiertNachSatz.put(sKey, alTemp);
			} else {
				fehlerAllgemein += "Zeile " + i + " muss mindestens 12 Spalten haben \n";
			}

		}

		ArrayList<Object[]> alDaten = new ArrayList<Object[]>();

		HashMap<Integer, BigDecimal> hmBereitsVerbraucht = new HashMap<Integer, BigDecimal>();

		Iterator it = lhmGruppiertNachSatz.keySet().iterator();
		while (it.hasNext()) {

			String key = (String) it.next();

			ArrayList<String[]> alZeilen = lhmGruppiertNachSatz.get(key);

			Object[] oZeile = new Object[TRACEIMPORT_ANZAHL_SPALTEN];

			BigDecimal anzahlPCB = new BigDecimal(alZeilen.size());

			oZeile[TRACEIMPORT_MENGE_AUS_IMPORTDATEI] = anzahlPCB;

			String stklKbezRAW = alZeilen.get(0)[csvItemIndex];
			String stklKbez = alZeilen.get(0)[csvItemIndex];

			String batchChargennummer = alZeilen.get(0)[csvBatchIndex];

			String pcbNummer = alZeilen.get(0)[csvPCBIndex];

			oZeile[TRACEIMPORT_KBEZ_STKL_AUS_IMPORTDATEI] = stklKbez;

			oZeile[TRACEIMPORT_CHARGENNUMMER] = batchChargennummer;

			String fehlerZeile = "";

			if (stklKbez.contains("/")) {
				stklKbez = stklKbez.substring(0, stklKbez.lastIndexOf("/"));
			}

			StuecklisteDto stklDto = null;
			List<ArtikelDto> gefundeneArtikel = getArtikelFac().artikelFindByCKBezOhneExc(stklKbez, theClientDto);
			if (gefundeneArtikel != null && gefundeneArtikel.size() > 0) {

				ArtikelDto aDto = gefundeneArtikel.get(0);
				stklDto = getStuecklisteFac().stuecklisteFindByMandantCNrArtikelIIdOhneExc(aDto.getIId(),
						aDto.getMandantCNr());
				if (stklDto != null) {
					stklDto.setArtikelDto(aDto);
					oZeile[TRACEIMPORT_STUECKLISTE] = aDto.getCNr();
					oZeile[TRACEIMPORT_STUECKLISTE_BEZEICHNUNG] = aDto.formatBezeichnung();
				}
			}

			if (oZeile[TRACEIMPORT_STUECKLISTE] == null) {
				fehlerZeile += "Es konnte keine Stueckliste mit der KBez '" + stklKbez + "' gefunden werden \n";
			}

			// Materialartikel anhand Chargennummer suchen
			String queryString = "SELECT L.ARTIKEL_I_ID FROM WW_LAGERBEWEGUNG L "
					+ "LEFT OUTER JOIN WW_ARTIKEL A ON L.ARTIKEL_I_ID= A.I_ID " + "WHERE A.B_CHARGENNRTRAGEND=1 "
					+ "AND L.N_MENGE>0 " + "AND L.B_HISTORIE=0 "
					+ "AND L.B_ABGANG=0 AND L.B_VOLLSTAENDIGVERBRAUCHT=0 AND L.C_SERIENNRCHARGENNR IS NOT NULL AND L.C_SERIENNRCHARGENNR ='"
					+ batchChargennummer + "'";

			Session session = FLRSessionFactory.getFactory().openSession();
			org.hibernate.Query query = session.createSQLQuery(queryString);
			query.setMaxResults(1);
			List<?> results = query.list();
			Iterator<?> resultListIterator = results.iterator();

			if (resultListIterator.hasNext()) {
				Integer artikelIId = (Integer) resultListIterator.next();
				ArtikelDto aDtoMaterial = getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);
				oZeile[TRACEIMPORT_ARTIKEL_MATERIAL] = aDtoMaterial.getCNr();

				// aeltestes Los anhand Stueckliste
				if (stklDto != null) {

					Session sessionSub = FLRSessionFactory.getFactory().openSession();
					String sQuerySub = "FROM FLRLosReport AS l WHERE l.stueckliste_i_id=" + stklDto.getIId()
							+ " AND l.status_c_nr IN('" + FertigungFac.STATUS_AUSGEGEBEN + "','"
							+ FertigungFac.STATUS_IN_PRODUKTION + "','" + FertigungFac.STATUS_TEILERLEDIGT
							+ "') ORDER BY l.c_nr ASC ";

					org.hibernate.Query hquerySub = sessionSub.createQuery(sQuerySub);

					List<?> resultListSub = hquerySub.list();
					Iterator<?> resultListIteratorSub = resultListSub.iterator();

					TraceImportDto traceImportDto = new TraceImportDto();

					if (resultListIteratorSub.hasNext()) {

						traceImportDto.setStuecklisteIId(stklDto.getIId());
						traceImportDto.setStuecklisteDto(stklDto);

						BigDecimal bdZuVerbrauchen = new BigDecimal(alZeilen.size());

						traceImportDto.setZuBuchendeMenge(bdZuVerbrauchen);

						traceImportDto.setChargennummer(batchChargennummer);
						traceImportDto.setPcbNummer(pcbNummer);
						traceImportDto.setAnzahlPcb(new BigDecimal(lhmAnzahlPCB.get(stklKbezRAW)));
						traceImportDto.setArtikelDtoMaterial(aDtoMaterial);
						try {
							Integer lagerIIdHauptlager = getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId();

							BigDecimal bdLagerstandGesamtAufHauptlager = getLagerFac().getMengeAufLager(
									aDtoMaterial.getIId(), lagerIIdHauptlager, batchChargennummer, theClientDto);

							traceImportDto.setMengeAufHauptlager(bdLagerstandGesamtAufHauptlager);
							oZeile[TRACEIMPORT_LAGERSTAND] = bdLagerstandGesamtAufHauptlager;

							if (bdLagerstandGesamtAufHauptlager.doubleValue() < alZeilen.size()) {
								fehlerZeile += "Es sind nur "
										+ Helper.formatZahl(bdLagerstandGesamtAufHauptlager, 2, theClientDto.getLocUi())
										+ "  der benoetigten Charge " + batchChargennummer + " "
										+ Helper.formatZahl(alZeilen.size(), 2, theClientDto.getLocUi()) + " "
										+ aDtoMaterial.getEinheitCNr().trim() + " auf dem Hauptlager\n";
							}

						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}

						while (resultListIteratorSub.hasNext()) {
							FLRLosReport flrLosReport = (FLRLosReport) resultListIteratorSub.next();

							oZeile[TRACEIMPORT_LOSNUMMER] = flrLosReport.getC_nr();
							oZeile[TRACEIMPORT_LOSGROESSE] = flrLosReport.getN_losgroesse();
							try {
								oZeile[TRACEIMPORT_LOSABGELIEFERT] = getFertigungFac()
										.getErledigteMenge(flrLosReport.getI_id(), theClientDto);

								LossollmaterialDto[] sollmatDtos = getFertigungFac()
										.lossollmaterialFindyByLosIIdArtikelIId(flrLosReport.getI_id(), artikelIId,
												theClientDto);

								for (LossollmaterialDto sollmatDto : sollmatDtos) {

									sollmatDto.setLosDto_NOT_IN_DB(
											getFertigungFac().losFindByPrimaryKey(flrLosReport.getI_id()));

									// SP9608
									Set<Integer> posIIds = new HashSet<Integer>();

									posIIds.add(sollmatDto.getIId());

									Integer sollmatIIdOriginal = sollmatDto.getIId();
									if (sollmatDto.getLossollmaterialIIdOriginal() != null) {
										sollmatIIdOriginal = sollmatDto.getLossollmaterialIIdOriginal();

									}

									BigDecimal bdBereitsVerbraucht = BigDecimal.ZERO;
									if (hmBereitsVerbraucht.containsKey(sollmatIIdOriginal)) {
										bdBereitsVerbraucht = hmBereitsVerbraucht.get(sollmatIIdOriginal);
									}

									LossollmaterialDto[] ersatzartikel = null;
									if (sollmatDto.getLossollmaterialIIdOriginal() != null) {
										posIIds.add(sollmatDto.getLossollmaterialIIdOriginal());
										ersatzartikel = getFertigungFac()
												.lossollmaterialFindByLossollmaterialIIdOriginal(
														sollmatDto.getLossollmaterialIIdOriginal());
									} else {
										ersatzartikel = getFertigungFac()
												.lossollmaterialFindByLossollmaterialIIdOriginal(sollmatDto.getIId());
									}

									for (LossollmaterialDto ersatzartikelZeile : ersatzartikel) {
										posIIds.add(ersatzartikelZeile.getIId());
									}

									// SUMME SOLL - SUMME IST

									BigDecimal bdSummeSoll = BigDecimal.ZERO;
									BigDecimal bdSummeAusgegeben = BigDecimal.ZERO;
									Iterator itSollmat = posIIds.iterator();

									while (itSollmat.hasNext()) {
										LossollmaterialDto sollmat = getFertigungFac()
												.lossollmaterialFindByPrimaryKey((Integer) itSollmat.next());

										bdSummeSoll = bdSummeSoll.add(sollmat.getNMenge());

										BigDecimal bdBereitsausgegeben = getFertigungFac()
												.getAusgegebeneMenge(sollmat.getIId(), null, theClientDto);
										bdSummeAusgegeben = bdSummeAusgegeben.add(bdBereitsausgegeben);

									}

									if (bdSummeSoll.subtract(bdSummeAusgegeben).subtract(bdBereitsVerbraucht)
											.doubleValue() < 0) {
										sollmatDto.setNOffeneMenge_NOT_IN_DB(BigDecimal.ZERO);
									} else {
										sollmatDto.setNOffeneMenge_NOT_IN_DB(
												bdSummeSoll.subtract(bdSummeAusgegeben).subtract(bdBereitsVerbraucht));
									}

									if (sollmatDto.getNOffeneMenge_NOT_IN_DB().doubleValue() > bdZuVerbrauchen
											.doubleValue()) {
										sollmatDto.setNZuVerbrauchendenMenge_NOT_IN_DB(bdZuVerbrauchen);

										hmBereitsVerbraucht.put(sollmatIIdOriginal,
												bdBereitsVerbraucht.add(bdZuVerbrauchen));

										bdZuVerbrauchen = BigDecimal.ZERO;
									} else {
										sollmatDto.setNZuVerbrauchendenMenge_NOT_IN_DB(
												sollmatDto.getNOffeneMenge_NOT_IN_DB());

										hmBereitsVerbraucht.put(sollmatIIdOriginal,
												bdBereitsVerbraucht.add(sollmatDto.getNOffeneMenge_NOT_IN_DB()));

										bdZuVerbrauchen = bdZuVerbrauchen
												.subtract(sollmatDto.getNOffeneMenge_NOT_IN_DB());
									}

									sollmatDto.setSnrChnr_NOT_IN_DB(batchChargennummer);

									traceImportDto.getSollmaterialoffeneLose().put(flrLosReport.getC_nr(), sollmatDto);

								}

								if (sollmatDtos.length == 0) {
									fehlerZeile += "Es konnte kein Artikel mit der Artikelnummer '"
											+ aDtoMaterial.getCNr() + "' im Los '" + flrLosReport.getC_nr()
											+ "' gefunden werden.\n";
								}

							} catch (RemoteException e) {
								throwEJBExceptionLPRespectOld(e);
							}

						}

						alZuBuchen.add(traceImportDto);
					} else {

						fehlerZeile += "Es konnte kein Los im Status Ausgegeben/InProduktion/Telerledigt gefunden werden.\n";
					}
				}

			}

			if (oZeile[TRACEIMPORT_ARTIKEL_MATERIAL] == null) {
				fehlerZeile += "Es konnte kein Artikel ueber die Chargennummer '" + batchChargennummer
						+ "' gefunden werden \n";

			}

			ArrayList alSub = new ArrayList();

			String[] fieldnames = new String[] { "F_ZEILE" };

			for (int i = 0; i < alZeilen.size(); i++) {

				Object[] oZeileSub = new Object[1];
				oZeileSub[0] = Helper.erzeugeStringAusStringArray(alZeilen.get(i));
				alSub.add(oZeileSub);
			}

			Object[][] dataSub = new Object[alSub.size()][fieldnames.length];
			dataSub = (Object[][]) alSub.toArray(dataSub);

			oZeile[TRACEIMPORT_SUBREPORT_ZEILEN_AUS_DATEI] = new LPDatenSubreport(dataSub, fieldnames);

			oZeile[TRACEIMPORT_FEHLER] = fehlerZeile;

			if (fehlerZeile.length() > 0) {

				alDaten.add(oZeile);
			}
		}

		Map<String, Object> mapParameter = new TreeMap<String, Object>();
		mapParameter.put("P_FEHLER_ALLGEMEIN", fehlerAllgemein);
		mapParameter.put("P_BATCH_SPALTENNR", Integer.valueOf(csvBatchIndex));
		mapParameter.put("P_DATEINAME", filename);

		mapParameter.put("P_DATENSAETZE_ZU_BUCHEN", alZuBuchen);

		data = new Object[alDaten.size()][TRACEIMPORT_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(data);
		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, REPORT_TRACEIMPORT, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printPruefplan(Integer losIId, TheClientDto theClientDto) {

		this.useCase = UC_LOSPRUEFPLAN;

		LosDto losDto = null;
		try {
			losDto = getFertigungFac().losFindByPrimaryKey(losIId);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		Map<String, Object> mapParameter = new TreeMap<String, Object>();
		try {
			mapParameter.put("P_ANGELEGT", new java.util.Date(losDto.getTAnlegen().getTime()));

			mapParameter.put("P_SUBREPORT_PRUEFPLAN", getSubreportPruefplan(losDto, theClientDto));

			String sAuftragsnummer;
			String sInternerKommentar = null;
			String sKunde;

			Timestamp dLiefertermin;

			String sAbteilung = null;

			Boolean bPoenale = Boolean.FALSE;
			Boolean bRoHs = Boolean.FALSE;
			if (losDto.getAuftragIId() != null) {
				// Auftrag holen
				AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(losDto.getAuftragIId());
				sAuftragsnummer = auftragDto.getCNr();

				bPoenale = Helper.short2Boolean(auftragDto.getBPoenale());
				bRoHs = Helper.short2Boolean(auftragDto.getBRoHs());

				// Kunde holen
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(),
						theClientDto);

				sKunde = kundeDto.getPartnerDto().getCName1nachnamefirmazeile1();

				sAbteilung = kundeDto.getPartnerDto().getCName3vorname2abteilung();

				if (losDto.getAuftragpositionIId() != null) {
					AuftragpositionDto aufposDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(losDto.getAuftragpositionIId());
					if (aufposDto.getTUebersteuerbarerLiefertermin() != null) {
						dLiefertermin = aufposDto.getTUebersteuerbarerLiefertermin();
					} else {
						dLiefertermin = auftragDto.getDLiefertermin();
					}
				} else {
					dLiefertermin = auftragDto.getDLiefertermin();
				}
			} else {
				sAuftragsnummer = "";

				if (losDto.getKundeIId() != null) {
					KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(losDto.getKundeIId(), theClientDto);

					sKunde = kundeDto.getPartnerDto().getCName1nachnamefirmazeile1();
				} else {
					sKunde = "";
				}

				dLiefertermin = null;

			}

			mapParameter.put("P_AUFTRAGNUMMER", sAuftragsnummer);
			mapParameter.put("P_AUFTRAG_INTERNERKOMMENTAR", sInternerKommentar);
			mapParameter.put("P_AUFTRAG_KUNDE_ABTEILUNG", sAbteilung);

			mapParameter.put("P_POENALE", bPoenale);
			mapParameter.put("P_ROHS", bRoHs);
			KostenstelleDto kstDto = getSystemFac().kostenstelleFindByPrimaryKey(losDto.getKostenstelleIId());
			mapParameter.put("P_KOSTENSTELLENUMMER", kstDto.getCNr());
			mapParameter.put("P_KUNDE", sKunde);
			mapParameter.put("P_LOSGROESSE", losDto.getNLosgroesse());

			mapParameter.put("P_LOSKLASSEN", getLosLosKlassenAlsString(losDto.getIId()));

			mapParameter.put("P_LOSNUMMER", losDto.getCNr());
			mapParameter.put("P_PRODUKTIONSBEGINN", losDto.getTProduktionsbeginn());
			mapParameter.put("P_PRODUKTIONSENDE", losDto.getTProduktionsende());
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(losDto.getTProduktionsende());

			mapParameter.put("P_LIEFERTERMIN", dLiefertermin);

			// Fertigungsort
			if (losDto.getPartnerIIdFertigungsort() != null) {
				PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(losDto.getPartnerIIdFertigungsort(),
						theClientDto);
				mapParameter.put("P_FERTIGUNGSORT", partnerDto.formatTitelAnrede());
			}

			ArrayList<Object[]> subreportArtikelkommentare = new ArrayList<Object[]>();

			String[] fieldnames = new String[] { "F_KOMMENTARART", "F_MIMETYPE", "F_BILD", "F_KOMMENTAR",
					"F_PDF_OBJECT" };

			int iFeld_Subreport_Kommentarart = 0;
			int iFeld_Subreport_Mimetype = 1;
			int iFeld_Subreport_Bild = 2;
			int iFeld_Subreport_Kommentar = 3;
			int iFeld_Subreport_Pdf = 4;
			int iFeld_Subreport_iAnzahlSpalten = 5;

			String sLosStuecklisteArtikelKommentar = "";
			// Bild einfuegen
			String sMengenEinheit = "";
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stuecklisteDto = getStuecklisteFac()
						.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(), theClientDto);
				// Einheit
				if (stuecklisteDto.getArtikelDto() != null) {
					if (stuecklisteDto.getArtikelDto().getEinheitCNr() != null) {
						sMengenEinheit = stuecklisteDto.getArtikelDto().getEinheitCNr();
					}
				}

				// Sperren
				String sperren = getArtikelFac().getArtikelsperrenText(stuecklisteDto.getArtikelIId());

				mapParameter.put("P_STUECKLISTESPERRSTATUS", sperren);

				mapParameter.put("P_ANZAHL_VERWENDET", getFertigungFac()
						.getAnzahlBisherVerwendet(losDto.getStuecklisteIId(), losDto.getIId(), theClientDto));

				// Abmessungen
				GeometrieDto geometrieDto = stuecklisteDto.getArtikelDto().getGeometrieDto();
				if (geometrieDto != null) {
					mapParameter.put(P_ARTIKEL_BREITE, geometrieDto.getFBreite());
					mapParameter.put(P_ARTIKEL_HOEHE, geometrieDto.getFHoehe());
					mapParameter.put(P_ARTIKEL_TIEFE, geometrieDto.getFTiefe());
				}
				// Bauform
				VerpackungDto verpackungDto = stuecklisteDto.getArtikelDto().getVerpackungDto();
				if (verpackungDto != null) {
					mapParameter.put("P_ARTIKEL_BAUFORM", verpackungDto.getCBauform());
					mapParameter.put("P_ARTIKEL_VERPACKUNGSART", verpackungDto.getCVerpackungsart());
				}

				mapParameter.put("P_INDEX", stuecklisteDto.getArtikelDto().getCIndex());
				mapParameter.put("P_REVISION", stuecklisteDto.getArtikelDto().getCRevision());

				ArtikelkommentarDto[] artikelkommentarDto = getArtikelkommentarFac()
						.artikelkommentardruckFindByArtikelIIdBelegartCNr(stuecklisteDto.getArtikelIId(),
								LocaleFac.BELEGART_LOS, theClientDto.getLocUiAsString(), theClientDto);

				if (artikelkommentarDto != null && artikelkommentarDto.length > 0) {

					for (int j = 0; j < artikelkommentarDto.length; j++) {
						if (artikelkommentarDto[j].getArtikelkommentarsprDto() != null) {

							ArtikelkommentarartDto artikelkommentarartDto = getArtikelkommentarFac()
									.artikelkommentarartFindByPrimaryKey(
											artikelkommentarDto[j].getArtikelkommentarartIId(), theClientDto);

							Object[] oZeileVorlage = new Object[iFeld_Subreport_iAnzahlSpalten];
							oZeileVorlage[iFeld_Subreport_Kommentarart] = artikelkommentarartDto.getCNr();

							oZeileVorlage[iFeld_Subreport_Mimetype] = artikelkommentarDto[j].getDatenformatCNr();
							oZeileVorlage[iFeld_Subreport_Kommentar] = artikelkommentarDto[j]
									.getArtikelkommentarsprDto().getXKommentar();

							// Text Kommentar
							if (artikelkommentarDto[j].getDatenformatCNr().trim()
									.indexOf(MediaFac.DATENFORMAT_MIMETYPEART_TEXT) != -1) {
								if (sLosStuecklisteArtikelKommentar == "") {
									sLosStuecklisteArtikelKommentar += artikelkommentarDto[j]
											.getArtikelkommentarsprDto().getXKommentar();
								} else {
									sLosStuecklisteArtikelKommentar += "\n"
											+ artikelkommentarDto[j].getArtikelkommentarsprDto().getXKommentar();
								}

								Object[] oZeile = oZeileVorlage.clone();
								subreportArtikelkommentare.add(oZeile);

							} else if (artikelkommentarDto[j].getDatenformatCNr()
									.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
									|| artikelkommentarDto[j].getDatenformatCNr()
											.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)
									|| artikelkommentarDto[j].getDatenformatCNr()
											.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)) {
								byte[] bild = artikelkommentarDto[j].getArtikelkommentarsprDto().getOMedia();
								if (bild != null) {
									java.awt.image.BufferedImage myImage = Helper.byteArrayToImage(bild);

									Object[] oZeile = oZeileVorlage.clone();
									oZeile[iFeld_Subreport_Bild] = myImage;

									subreportArtikelkommentare.add(oZeile);

								}
							} else if (artikelkommentarDto[j].getDatenformatCNr()
									.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF)) {

								byte[] bild = artikelkommentarDto[j].getArtikelkommentarsprDto().getOMedia();

								java.awt.image.BufferedImage[] tiffs = Helper.tiffToImageArray(bild);
								if (tiffs != null) {
									for (int k = 0; k < tiffs.length; k++) {

										Object[] oZeile = oZeileVorlage.clone();
										oZeile[iFeld_Subreport_Bild] = tiffs[k];

										subreportArtikelkommentare.add(oZeile);

									}
								}

							} else if (artikelkommentarDto[j].getDatenformatCNr()
									.equals(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF)) {
								byte[] pdf = artikelkommentarDto[j].getArtikelkommentarsprDto().getOMedia();
								if (pdf != null) {
									HVPdfReport pdfObject = new HVPdfReport(pdf);
									Object[] oZeile = oZeileVorlage.clone();
									oZeile[iFeld_Subreport_Pdf] = pdfObject;
									subreportArtikelkommentare.add(oZeile);
								}
							}
						}
					}
				}
			}
			mapParameter.put("P_MENGENEINHEIT", sMengenEinheit);
			mapParameter.put("P_PROJEKT", losDto.getCProjekt());
			mapParameter.put("P_LOSKOMMENTAR", losDto.getCKommentar());
			mapParameter.put("P_LOSLANGTEXT", losDto.getXText());

			FertigungsgruppeDto fertGruppeDto = getStuecklisteFac()
					.fertigungsgruppeFindByPrimaryKey(losDto.getFertigungsgruppeIId());
			mapParameter.put("P_FERTIGUNGSGRUPPE", fertGruppeDto.getCBez());

			// Materialliste?
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stkDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(),
						theClientDto);

				if (sLosStuecklisteArtikelKommentar != "") {
					mapParameter.put("P_STUECKLISTEARTIKELKOMMENTAR",
							Helper.formatStyledTextForJasper(sLosStuecklisteArtikelKommentar));
				}

				mapParameter.put("P_STUECKLISTEBEZEICHNUNG", stkDto.getArtikelDto().getArtikelsprDto().getCBez());

				mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG",
						stkDto.getArtikelDto().getArtikelsprDto().getCZbez());
				mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG2",
						stkDto.getArtikelDto().getArtikelsprDto().getCZbez2());

				mapParameter.put("P_STUECKLISTEKURZBEZEICHNUNG", stkDto.getArtikelDto().getArtikelsprDto().getCKbez());
				mapParameter.put("P_STUECKLISTEREFERENZNUMMER", stkDto.getArtikelDto().getCReferenznr());

				mapParameter.put("P_STUECKLISTENUMMER", stkDto.getArtikelDto().getCNr());

				mapParameter.put("P_STUECKLISTE_GEWICHTKG", stkDto.getArtikelDto().getFGewichtkg());

				if (stkDto.getArtikelDto().getVerpackungDto() != null) {
					mapParameter.put("P_STUECKLISTE_BAUFORM", stkDto.getArtikelDto().getVerpackungDto().getCBauform());
					mapParameter.put("P_STUECKLISTE_VERPACKUNGSART",
							stkDto.getArtikelDto().getVerpackungDto().getCVerpackungsart());
				}

				if (stkDto.getArtikelDto().getGeometrieDto() != null) {
					mapParameter.put("P_STUECKLISTE_BREITETEXT",
							stkDto.getArtikelDto().getGeometrieDto().getCBreitetext());
					mapParameter.put("P_STUECKLISTE_BREITE", stkDto.getArtikelDto().getGeometrieDto().getFBreite());
					mapParameter.put("P_STUECKLISTE_HOEHE", stkDto.getArtikelDto().getGeometrieDto().getFHoehe());
					mapParameter.put("P_STUECKLISTE_TIEFE", stkDto.getArtikelDto().getGeometrieDto().getFTiefe());
				}

			} else {
				mapParameter.put("P_STUECKLISTEBEZEICHNUNG", losDto.getCProjekt());
				mapParameter.put("P_STUECKLISTENUMMER",
						getTextRespectUISpr("fert.materialliste", theClientDto.getMandant(), theClientDto.getLocUi()));
			}

			Object[][] dataSub = new Object[subreportArtikelkommentare.size()][fieldnames.length];
			dataSub = (Object[][]) subreportArtikelkommentare.toArray(dataSub);

			// SP2699
			mapParameter.put("P_SUBREPORT_ARTIKELKOMMENTAR", new LPDatenSubreport(dataSub, fieldnames));

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		data = new Object[0][PRUEFPLAN_ANZAHL_SPALTEN];
		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, REPORT_PRUEFPLAN, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		JasperPrintLP print = getReportPrint();
		if (print != null) {
			PrintInfoDto values = new PrintInfoDto();
			values.setDocPath(new DocPath(new DocNodeLos(losDto)));
			print.setOInfoForArchive(values);
		}

		return print;
	}

	public LPDatenSubreport getSubreportPruefplan(LosDto losDto, TheClientDto theClientDto) {
		StuecklistepositionDto[] stklPosDtos = getStuecklisteFac()
				.stuecklistepositionFindByStuecklisteIId(losDto.getStuecklisteIId(), theClientDto);

		ArrayList<StuecklistepositionDto> alPositionenKontakt = new ArrayList<StuecklistepositionDto>();
		ArrayList<StuecklistepositionDto> alPositionenLitze = new ArrayList<StuecklistepositionDto>();
		ArrayList<StuecklistepositionDto> alPositionenLitze2 = new ArrayList<StuecklistepositionDto>();

		for (int j = 0; j < stklPosDtos.length; j++) {
			alPositionenKontakt.add(stklPosDtos[j]);
			alPositionenLitze.add(stklPosDtos[j]);
			alPositionenLitze2.add(stklPosDtos[j]);

		}

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT pp FROM FLRLospruefplan AS pp LEFT OUTER JOIN pp.flrlossollmaterial_kontakt.flrartikel as kontakt WHERE pp.los_i_id="
				+ losDto.getIId() + " ORDER BY pp.i_sort ASC ";

		org.hibernate.Query hquery = session.createQuery(sQuery);

		List<?> resultList = hquery.list();
		Iterator<?> resultListIterator = resultList.iterator();

		ArrayList alDaten = new ArrayList();

		while (resultListIterator.hasNext()) {
			FLRLospruefplan pp = (FLRLospruefplan) resultListIterator.next();

			Object[] oZeile = new Object[PRUEFPLAN_ANZAHL_SPALTEN];

			Integer artikelIIdKontakt = null;

			if (pp.getFlrlossollmaterial_kontakt() != null) {

				oZeile[PRUEFPLAN_ARTIKEL_KONTAKT] = pp.getFlrlossollmaterial_kontakt().getFlrartikel().getC_nr();

				ArtikelDto aDtoKontakt = getArtikelFac().artikelFindByPrimaryKeySmall(
						pp.getFlrlossollmaterial_kontakt().getFlrartikel().getI_id(), theClientDto);

				artikelIIdKontakt = pp.getFlrlossollmaterial_kontakt().getFlrartikel().getI_id();

				if (aDtoKontakt.getArtikelsprDto() != null) {
					oZeile[PRUEFPLAN_ARTIKELBEZEICHNUNG_KONTAKT] = aDtoKontakt.getArtikelsprDto().getCBez();
				}

			}

			StuecklistepositionDto positionAusStuecklisteLitze = null;
			Integer artikelIIdLitze = null;
			if (pp.getFlrlossollmaterial_litze() != null) {

				oZeile[PRUEFPLAN_ARTIKEL_LITZE] = pp.getFlrlossollmaterial_litze().getFlrartikel().getC_nr();

				ArtikelDto aDtoLitze = getArtikelFac().artikelFindByPrimaryKeySmall(
						pp.getFlrlossollmaterial_litze().getFlrartikel().getI_id(), theClientDto);
				artikelIIdLitze = aDtoLitze.getIId();

				if (aDtoLitze.getArtikelsprDto() != null) {
					oZeile[PRUEFPLAN_ARTIKELBEZEICHNUNG_LITZE] = aDtoLitze.getArtikelsprDto().getCBez();
				}

				for (int u = 0; u < alPositionenLitze.size(); u++) {

					if (pp.getFlrlossollmaterial_litze().getFlrartikel().getI_id()
							.equals(alPositionenLitze.get(u).getArtikelIId())
							&& pp.getFlrlossollmaterial_litze().getN_menge()
									.divide(pp.getFlrlos().getN_losgroesse(), BigDecimal.ROUND_HALF_EVEN)
									.equals(alPositionenLitze.get(u).getNMenge())
							&& pp.getFlrlossollmaterial_litze().getMontageart_i_id()
									.equals(alPositionenLitze.get(u).getMontageartIId())) {
						// wenn Menge und Artikel und Montageart gleich

						positionAusStuecklisteLitze = alPositionenLitze.get(u);
						alPositionenLitze.remove(u);
						break;

					}

				}

				if (positionAusStuecklisteLitze != null) {
					oZeile[PRUEFPLAN_POSITION_LITZE] = positionAusStuecklisteLitze.getCPosition();

					oZeile[PRUEFPLAN_MENGE_LITZE] = positionAusStuecklisteLitze.getNMenge();
					oZeile[PRUEFPLAN_EINHEIT_LITZE] = positionAusStuecklisteLitze.getEinheitCNr();
					oZeile[PRUEFPLAN_DIMENSION1_LITZE] = positionAusStuecklisteLitze.getFDimension1();
					oZeile[PRUEFPLAN_DIMENSION2_LITZE] = positionAusStuecklisteLitze.getFDimension2();
					oZeile[PRUEFPLAN_DIMENSION3_LITZE] = positionAusStuecklisteLitze.getFDimension3();
				}

			}

			StuecklistepositionDto positionAusStuecklisteLitze2 = null;
			Integer artikelIIdLitze2 = null;
			if (pp.getFlrlossollmaterial_litze2() != null) {

				oZeile[PRUEFPLAN_ARTIKEL_LITZE2] = pp.getFlrlossollmaterial_litze2().getFlrartikel().getC_nr();

				ArtikelDto aDtoLitze = getArtikelFac().artikelFindByPrimaryKeySmall(
						pp.getFlrlossollmaterial_litze2().getFlrartikel().getI_id(), theClientDto);
				artikelIIdLitze2 = aDtoLitze.getIId();

				if (aDtoLitze.getArtikelsprDto() != null) {
					oZeile[PRUEFPLAN_ARTIKELBEZEICHNUNG_LITZE2] = aDtoLitze.getArtikelsprDto().getCBez();
				}

				for (int u = 0; u < alPositionenLitze2.size(); u++) {

					if (pp.getFlrlossollmaterial_litze2().getFlrartikel().getI_id()
							.equals(alPositionenLitze2.get(u).getArtikelIId())
							&& pp.getFlrlossollmaterial_litze2().getN_menge()
									.divide(pp.getFlrlos().getN_losgroesse(), BigDecimal.ROUND_HALF_EVEN)
									.equals(alPositionenLitze2.get(u).getNMenge())
							&& pp.getFlrlossollmaterial_litze2().getMontageart_i_id()
									.equals(alPositionenLitze2.get(u).getMontageartIId())) {
						// wenn Menge und Artikel und Montageart gleich

						positionAusStuecklisteLitze2 = alPositionenLitze2.get(u);
						alPositionenLitze2.remove(u);
						break;

					}

				}

				if (positionAusStuecklisteLitze2 != null) {
					oZeile[PRUEFPLAN_POSITION_LITZE2] = positionAusStuecklisteLitze2.getCPosition();

					oZeile[PRUEFPLAN_MENGE_LITZE2] = positionAusStuecklisteLitze2.getNMenge();
					oZeile[PRUEFPLAN_EINHEIT_LITZE2] = positionAusStuecklisteLitze2.getEinheitCNr();
					oZeile[PRUEFPLAN_DIMENSION1_LITZE2] = positionAusStuecklisteLitze2.getFDimension1();
					oZeile[PRUEFPLAN_DIMENSION2_LITZE2] = positionAusStuecklisteLitze2.getFDimension2();
					oZeile[PRUEFPLAN_DIMENSION3_LITZE2] = positionAusStuecklisteLitze2.getFDimension3();
				}

			}

			String positionAusStuecklisteKontakt = null;

			if (pp.getFlrlossollmaterial_kontakt() != null) {

				for (int u = 0; u < alPositionenKontakt.size(); u++) {

					if (pp.getFlrlossollmaterial_kontakt().getFlrartikel().getI_id()
							.equals(alPositionenKontakt.get(u).getArtikelIId())
							&& pp.getFlrlossollmaterial_kontakt().getN_menge()
									.divide(pp.getFlrlos().getN_losgroesse(), BigDecimal.ROUND_HALF_EVEN)
									.equals(alPositionenKontakt.get(u).getNMenge())
							&& pp.getFlrlossollmaterial_kontakt().getMontageart_i_id()
									.equals(alPositionenKontakt.get(u).getMontageartIId())) {
						// wenn Menge und Artikel und Montageart gleich

						positionAusStuecklisteKontakt = alPositionenKontakt.get(u).getCPosition();
						alPositionenKontakt.remove(u);
						break;

					}

				}
			}
			PruefartDto paDto = getStuecklisteFac().pruefartFindByPrimaryKey(pp.getFlrpruefart().getI_id(),
					theClientDto);

			oZeile[PRUEFPLAN_PRUEFART] = paDto.getCNr();

			oZeile[PRUEFPLAN_DOPPELANSCHLAG] = Helper.short2Boolean(pp.getB_doppelanschlag());

			oZeile[PRUEFPLAN_PRUEFART_BEZEICHNUNG] = paDto.getBezeichnung();

			oZeile[PRUEFPLAN_POSITION_KONTAKT] = positionAusStuecklisteKontakt;
			if (pp.getFlrwerkzeug() != null) {
				oZeile[PRUEFPLAN_WERKZEUGNUMMER] = pp.getFlrwerkzeug().getC_nr();
				oZeile[PRUEFPLAN_WERKZEUGBEZEICHNUNG] = pp.getFlrwerkzeug().getC_bez();
			}
			if (pp.getFlrverschleissteil() != null) {
				oZeile[PRUEFPLAN_VERSCHLEISSTEILNUMMER] = pp.getFlrverschleissteil().getC_nr();
				oZeile[PRUEFPLAN_VERSCHLEISSTEILBEZEICHNUNG] = pp.getFlrverschleissteil().getC_bez();
			}

			Integer pruefkombinationIId = getStuecklisteFac().pruefeObPruefplanInPruefkombinationVorhanden(null,
					pp.getFlrpruefart().getI_id(), artikelIIdKontakt, artikelIIdLitze, artikelIIdLitze2,
					pp.getVerschleissteil_i_id(), pp.getPruefkombination_i_id(), false, theClientDto);
			if (pruefkombinationIId != null) {

				PruefkombinationDto pkDto = getStuecklisteFac().pruefkombinationFindByPrimaryKey(pruefkombinationIId,
						theClientDto);
				oZeile[PRUEFPLAN_CRIMPBREITE_DRAHT] = pkDto.getNCrimpbreitDraht();
				oZeile[PRUEFPLAN_CRIMPBREITE_ISOLATION] = pkDto.getNCrimpbreiteIsolation();
				oZeile[PRUEFPLAN_CRIMPHOEHE_DRAHT] = pkDto.getNCrimphoeheDraht();
				oZeile[PRUEFPLAN_CRIMPHOEHE_ISOLATION] = pkDto.getNCrimphoeheIsolation();

				oZeile[PRUEFPLAN_TOLERANZ_CRIMPBREITE_DRAHT] = pkDto.getNToleranzCrimpbreitDraht();
				oZeile[PRUEFPLAN_TOLERANZ_CRIMPBREITE_ISOLATION] = pkDto.getNToleranzCrimpbreiteIsolation();
				oZeile[PRUEFPLAN_TOLERANZ_CRIMPHOEHE_DRAHT] = pkDto.getNToleranzCrimphoeheDraht();
				oZeile[PRUEFPLAN_TOLERANZ_CRIMPHOEHE_ISOLATION] = pkDto.getNToleranzCrimphoeheIsolation();
				oZeile[PRUEFPLAN_ABZUGSKRAFT_LITZE] = pkDto.getNAbzugskraftLitze();
				oZeile[PRUEFPLAN_ABZUGSKRAFT_LITZE2] = pkDto.getNAbzugskraftLitze2();

				oZeile[PRUEFPLAN_TOLERANZ_WERT] = pkDto.getNToleranzWert();

				oZeile[PRUEFPLAN_WERT] = pkDto.getNWert();

				if (pkDto.getPruefkombinationsprDto() != null) {
					oZeile[PRUEFPLAN_KOMMENTAR] = pkDto.getPruefkombinationsprDto().getCBez();
				}

			}

			// SP9212
			Session sessionWerzeuge = FLRSessionFactory.getFactory().openSession();

			String sQueryWerzeuge = "SELECT vw " + " FROM FLRVerschleissteilwerkzeug AS vw WHERE 1=1 ";
			sQueryWerzeuge += " AND vw.flrwerkzeug.mandant_c_nr_standort='" + theClientDto.getMandant()
					+ "' AND vw.verschleissteil_i_id= " + pp.getVerschleissteil_i_id();
			sQueryWerzeuge += " ORDER BY vw.flrverschleissteil.c_nr ASC, vw.flrwerkzeug.c_nr ASC";

			Query qWerzeuge = sessionWerzeuge.createQuery(sQueryWerzeuge);

			List<?> resultListWerzeuge = qWerzeuge.list();

			Iterator<?> resultListIteratorWerzeuge = resultListWerzeuge.iterator();

			ArrayList alDatenSubreportWerkzeuge = new ArrayList();

			String[] fieldnamesWerkzeuge = new String[] { "Werkzeug", "WerkzeugBezeichnung" };

			while (resultListIteratorWerzeuge.hasNext()) {
				FLRVerschleissteilwerkzeug vw = (FLRVerschleissteilwerkzeug) resultListIteratorWerzeuge.next();

				Object[] oZeileWerzeuge = new Object[2];

				oZeileWerzeuge[0] = vw.getFlrwerkzeug().getC_nr();
				oZeileWerzeuge[1] = vw.getFlrwerkzeug().getC_bez();

				alDatenSubreportWerkzeuge.add(oZeileWerzeuge);

			}
			sessionWerzeuge.close();

			oZeile[PRUEFPLAN_SUBREPORT_MOEGLICHE_WERKZEUGE] = new LPDatenSubreport(alDatenSubreportWerkzeuge,
					fieldnamesWerkzeuge);

			// Anzahl verwendungen

			if (pp.getFlrwerkzeug() != null) {
				Session sessionVW = FLRSessionFactory.getFactory().openSession();

				String sQueryVW = "SELECT pp  FROM FLRLospruefplan AS pp WHERE pp.werkzeug_i_id="
						+ pp.getFlrwerkzeug().getI_id() + " AND pp.verschleissteil_i_id="
						+ pp.getFlrverschleissteil().getI_id() + " AND pp.flrlos.c_nr < '" + losDto.getCNr() + "'";

				org.hibernate.Query hqueryVW = sessionVW.createQuery(sQueryVW);

				BigDecimal bdAnzahlVerwendungen = BigDecimal.ZERO;

				List<?> resultListVW = hqueryVW.list();
				Iterator<?> resultListIteratorVW = resultListVW.iterator();
				while (resultListIteratorVW.hasNext()) {
					FLRLospruefplan verw = (FLRLospruefplan) resultListIteratorVW.next();

					BigDecimal bdAbgeliefert = BigDecimal.ZERO;

					Iterator itAbl = verw.getFlrlos().getAblieferungset().iterator();

					while (itAbl.hasNext()) {
						bdAbgeliefert = bdAbgeliefert.add(((FLRLosablieferung) itAbl.next()).getN_menge());
					}

					double dVerwendung = verw.getFlrlossollmaterial_kontakt().getN_menge().doubleValue()
							/ verw.getFlrlossollmaterial_kontakt().getFlrlos().getN_losgroesse().doubleValue()
							* bdAbgeliefert.doubleValue();

					bdAnzahlVerwendungen = bdAnzahlVerwendungen.add(new BigDecimal(dVerwendung));

				}
				oZeile[PRUEFPLAN_ANZAHL_VERWENDUNGEN_VERSCHLEISSTEIL] = bdAnzahlVerwendungen;
			}
			alDaten.add(oZeile);

		}

		String[] fieldnamesSub = new String[PRUEFPLAN_ANZAHL_SPALTEN];
		fieldnamesSub[PRUEFPLAN_ARTIKEL_KONTAKT] = "ArtikelKontakt";
		fieldnamesSub[PRUEFPLAN_ARTIKELBEZEICHNUNG_KONTAKT] = "ArtikelbezeichnungKontakt";
		fieldnamesSub[PRUEFPLAN_ARTIKEL_LITZE] = "ArtikelLitze";
		fieldnamesSub[PRUEFPLAN_ARTIKELBEZEICHNUNG_LITZE] = "ArtikelbezeichnungLitze";
		fieldnamesSub[PRUEFPLAN_POSITION_KONTAKT] = "PositionKontakt";
		fieldnamesSub[PRUEFPLAN_POSITION_LITZE] = "PositionLitze";
		fieldnamesSub[PRUEFPLAN_VERSCHLEISSTEILNUMMER] = "Verschleissteilnummer";
		fieldnamesSub[PRUEFPLAN_VERSCHLEISSTEILBEZEICHNUNG] = "Verschleissteilbezeichnung";
		fieldnamesSub[PRUEFPLAN_WERKZEUGNUMMER] = "Werkzeugnummer";
		fieldnamesSub[PRUEFPLAN_WERKZEUGBEZEICHNUNG] = "Werkzeugbezeichnung";
		fieldnamesSub[PRUEFPLAN_CRIMPBREITE_DRAHT] = "CrimpbreiteDraht";
		fieldnamesSub[PRUEFPLAN_CRIMPBREITE_ISOLATION] = "CrimpbreiteIsolation";
		fieldnamesSub[PRUEFPLAN_CRIMPHOEHE_DRAHT] = "CrimphoeheDraht";
		fieldnamesSub[PRUEFPLAN_CRIMPHOEHE_ISOLATION] = "CrimphoeheIsolation";
		fieldnamesSub[PRUEFPLAN_ANZAHL_VERWENDUNGEN_VERSCHLEISSTEIL] = "AnzahlVerwendungenVerschleissteil";
		fieldnamesSub[PRUEFPLAN_TOLERANZ_CRIMPBREITE_DRAHT] = "ToleranzCrimpbreiteDraht";
		fieldnamesSub[PRUEFPLAN_TOLERANZ_CRIMPBREITE_ISOLATION] = "ToleranzCrimpbreiteIsolation";
		fieldnamesSub[PRUEFPLAN_TOLERANZ_CRIMPHOEHE_DRAHT] = "ToleranzCrimphoeheDraht";
		fieldnamesSub[PRUEFPLAN_TOLERANZ_CRIMPHOEHE_ISOLATION] = "ToleranzCrimphoeheIsolation";
		fieldnamesSub[PRUEFPLAN_TOLERANZ_WERT] = "ToleranzWert";
		fieldnamesSub[PRUEFPLAN_WERT] = "Wert";
		fieldnamesSub[PRUEFPLAN_PRUEFART] = "Pruefart";
		fieldnamesSub[PRUEFPLAN_PRUEFART_BEZEICHNUNG] = "PruefartBezeichnung";
		fieldnamesSub[PRUEFPLAN_KOMMENTAR] = "Kommentar";
		fieldnamesSub[PRUEFPLAN_MENGE_LITZE] = "MengeLitze";
		fieldnamesSub[PRUEFPLAN_EINHEIT_LITZE] = "EinheitLitze";
		fieldnamesSub[PRUEFPLAN_DIMENSION1_LITZE] = "Dimension1Litze";
		fieldnamesSub[PRUEFPLAN_DIMENSION2_LITZE] = "Dimension2Litze";
		fieldnamesSub[PRUEFPLAN_DIMENSION3_LITZE] = "Dimension3Litze";
		fieldnamesSub[PRUEFPLAN_ARTIKEL_LITZE2] = "ArtikelLitze2";
		fieldnamesSub[PRUEFPLAN_ARTIKELBEZEICHNUNG_LITZE2] = "ArtikelbezeichnungLitze2";
		fieldnamesSub[PRUEFPLAN_MENGE_LITZE2] = "MengeLitze2";
		fieldnamesSub[PRUEFPLAN_EINHEIT_LITZE2] = "EinheitLitze2";
		fieldnamesSub[PRUEFPLAN_DIMENSION1_LITZE2] = "Dimension1Litze2";
		fieldnamesSub[PRUEFPLAN_DIMENSION2_LITZE2] = "Dimension2Litze2";
		fieldnamesSub[PRUEFPLAN_DIMENSION3_LITZE2] = "Dimension3Litze2";
		fieldnamesSub[PRUEFPLAN_POSITION_LITZE2] = "PositionLitze2";
		fieldnamesSub[PRUEFPLAN_ABZUGSKRAFT_LITZE] = "AbzugskraftLitze";
		fieldnamesSub[PRUEFPLAN_ABZUGSKRAFT_LITZE2] = "AbzugskraftLitze2";
		fieldnamesSub[PRUEFPLAN_DOPPELANSCHLAG] = "Doppelanschlag";
		fieldnamesSub[PRUEFPLAN_SUBREPORT_MOEGLICHE_WERKZEUGE] = "SubreportMoeglicheWerkzeuge";

		Object[][] dataSub = new Object[alDaten.size()][fieldnamesSub.length];
		dataSub = (Object[][]) alDaten.toArray(dataSub);

		return new LPDatenSubreport(dataSub, fieldnamesSub);
	}

	public boolean istErstlos(LosDto losDto, TheClientDto theClientDto) {

		if (losDto.getStuecklisteIId() != null) {

			SessionFactory factory = FLRSessionFactory.getFactory();
			org.hibernate.Session session = factory.openSession();
			Criteria c = session.createCriteria(FLRLosReport.class);
			c.add(Restrictions.eq(FertigungFac.FLR_LOS_MANDANT_C_NR, theClientDto.getMandant()));

			c.add(Restrictions.eq(FertigungFac.FLR_LOS_STUECKLISTE_I_ID, losDto.getStuecklisteIId()));
			c.add(Restrictions.lt(FertigungFac.FLR_LOS_C_NR, losDto.getCNr()));
			c.add(Restrictions.not(Restrictions.in(FertigungFac.FLR_LOS_STATUS_C_NR,
					new String[] { FertigungFac.STATUS_STORNIERT, FertigungFac.STATUS_GESTOPPT })));

			// Sortierung nach Losnummer
			c.addOrder(Order.asc(FertigungFac.FLR_LOS_C_NR));
			c.setMaxResults(1);
			List<?> list = c.list();

			if (list.size() > 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	public JasperPrintLP printAblieferEtikett(Integer losablieferungIId, Integer iExemplare, BigDecimal bdHandmenge,
			String snrVonScannerRAW, TheClientDto theClientDto) {
		this.useCase = UC_ABLIEFERETIKETT;
		try {

			LosablieferungDto losablieferungDto = getFertigungFac().losablieferungFindByPrimaryKey(losablieferungIId,
					false, theClientDto);
			LosDto losDto = getFertigungFac().losFindByPrimaryKey(losablieferungDto.getLosIId());

			Map<String, Object> parameter = new TreeMap<String, Object>();

			parameter.put("P_EXEMPLAREGESAMT", iExemplare);

			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(),
						theClientDto);
				ArtikelDto artikelDto = stklDto.getArtikelDto();

				parameter.put("P_ARTIKELNUMMER", artikelDto.getCNr());

				if (artikelDto.getArtikelsprDto() != null) {
					parameter.put("P_BEZEICHNUNG", artikelDto.getArtikelsprDto().getCBez());
					parameter.put("P_KURZBEZEICHNUNG", artikelDto.getArtikelsprDto().getCKbez());
					parameter.put("P_ZUSATZBEZEICHNUNG", artikelDto.getArtikelsprDto().getCZbez());
					parameter.put("P_ZUSATZBEZEICHNUNG2", artikelDto.getArtikelsprDto().getCZbez2());
				}

				// Erste gefundene Version anzeigen
				LagerbewegungDto[] lDto = getLagerFac().lagerbewegungFindByBelegartCNrBelegartPositionIId(
						LocaleFac.BELEGART_LOSABLIEFERUNG, losablieferungDto.getIId());
				if (lDto != null && lDto.length > 0) {
					parameter.put("P_VERSION", lDto[0].getCVersion());
				}

				MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(),
						theClientDto);

				parameter.put("P_EINHEIT", artikelDto.getEinheitCNr());
				parameter.put("P_REFERENZNUMMER", artikelDto.getCReferenznr());
				parameter.put("P_REVISION", artikelDto.getCRevision());
				parameter.put("P_INDEX", artikelDto.getCIndex());
				parameter.put("P_VERPACKUNGSMENGE", artikelDto.getFVerpackungsmenge());

				parameter.put("P_MANDANTADRESSE", Helper.formatMandantAdresse(mandantDto));

				if (artikelDto.getMaterialIId() != null) {
					MaterialDto materialDto = getMaterialFac().materialFindByPrimaryKey(artikelDto.getMaterialIId(),
							theClientDto);
					parameter.put("P_MATERIAL", materialDto.getBezeichnung());
				}

				if (artikelDto.getGeometrieDto() != null) {
					parameter.put("P_BREITE", artikelDto.getGeometrieDto().getFBreite());
					parameter.put("P_HOEHE", artikelDto.getGeometrieDto().getFHoehe());
					parameter.put("P_TIEFE", artikelDto.getGeometrieDto().getFTiefe());
				}

				if (artikelDto.getVerpackungDto() != null) {
					parameter.put("P_BAUFORM", artikelDto.getVerpackungDto().getCBauform());
					parameter.put("P_VERPACKUNGSART", artikelDto.getVerpackungDto().getCVerpackungsart());
				}

				parameter.put("P_VERPACKUNGS_EAN", artikelDto.getCVerpackungseannr());
				parameter.put("P_VERKAUFS_EAN", artikelDto.getCVerkaufseannr());

				if (artikelDto.getHerstellerIId() != null) {
					HerstellerDto herstellerDto = getArtikelFac()
							.herstellerFindByPrimaryKey(artikelDto.getHerstellerIId(), theClientDto);
					parameter.put("P_HERSTELLER", herstellerDto.getCNr());
					parameter.put("P_HERSTELLER_NAME1", herstellerDto.getPartnerDto().getCName1nachnamefirmazeile1());
					parameter.put("P_HERSTELLER_NAME2", herstellerDto.getPartnerDto().getCName2vornamefirmazeile2());
				}

				ArtikellieferantDto[] artikellieferantDtos = getArtikelFac()
						.artikellieferantFindByArtikelIId(artikelDto.getIId(), theClientDto);

				if (artikellieferantDtos != null && artikellieferantDtos.length > 0) {
					parameter.put("P_LIEFERANT",
							getLieferantFac()
									.lieferantFindByPrimaryKey(artikellieferantDtos[0].getLieferantIId(), theClientDto)
									.getPartnerDto().formatAnrede());
					parameter.put("P_LIEFERANT_ARTIKELNUMMER", artikellieferantDtos[0].getCArtikelnrlieferant());
					parameter.put("P_LIEFERANT_ARTIKELBEZEICHNUNG", artikellieferantDtos[0].getCBezbeilieferant());
				}
				Integer iIdHauplpager = getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId();
				if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet())) {
					parameter.put("P_LAGERSTAND",
							getLagerFac().getLagerstand(artikelDto.getIId(), iIdHauplpager, theClientDto));
				}

				parameter.put("P_LAGERORT",
						getLagerFac().getLagerplaezteEinesArtikels(artikelDto.getIId(), iIdHauplpager));

			}

			if (bdHandmenge != null) {
				parameter.put("P_MENGE", bdHandmenge);
			} else {
				parameter.put("P_MENGE", losablieferungDto.getNMenge());
			}

			parameter.put("P_LOSNUMMER", losDto.getCNr());
			parameter.put("P_ABLIEFERDATUM", losablieferungDto.getTAendern());
			parameter.put("P_PROJEKT", losDto.getCProjekt());
			parameter.put("P_KOMMENTAR", losDto.getCKommentar());

			parameter.put("P_BARCODE_RAW", snrVonScannerRAW);

			// PJ18617
			PersonalDto personalDto = getPersonalFac()
					.personalFindByPrimaryKey(losablieferungDto.getPersonalIIdAendern(), theClientDto);
			parameter.put("P_PERSON_ABGELIEFERT_KURZZEICHEN", personalDto.getCKurzzeichen());
			parameter.put("P_PERSON_ABGELIEFERT", personalDto.formatFixUFTitelName2Name1());

			parameter.put("P_SNRCHNR", SeriennrChargennrMitMengeDto
					.erstelleStringAusMehrerenSeriennummern(losablieferungDto.getSeriennrChargennrMitMenge()));
			data = new Object[0][ABLIEFERETIKETT_ANZAHL_SPALTEN];

			ArrayList alDaten = new ArrayList();

			if (losablieferungDto.getSeriennrChargennrMitMenge() != null) {

				for (int i = 0; i < losablieferungDto.getSeriennrChargennrMitMenge().size(); i++) {

					String snr = losablieferungDto.getSeriennrChargennrMitMenge().get(i).getCSeriennrChargennr();

					Object[] oZeile = new Object[ABLIEFERETIKETT_ANZAHL_SPALTEN];

					oZeile[ABLIEFERETIKETT_SERIENNUMMER] = snr;
					oZeile[ABLIEFERETIKETT_MENGE] = losablieferungDto.getSeriennrChargennrMitMenge().get(i).getNMenge();

					// PJ18632

					LagerbewegungDto[] lagerbewDtos = getLagerFac()
							.lagerbewegungFindByBelegartCNrBelegartPositionIIdCSeriennrchargennr(
									LocaleFac.BELEGART_LOSABLIEFERUNG, losablieferungDto.getIId(), snr);
					if (lagerbewDtos != null && lagerbewDtos.length > 0) {
						oZeile[ABLIEFERETIKETT_I_ID_BUCHUNG] = lagerbewDtos[0].getIIdBuchung();
					}

					oZeile[ABLIEFERETIKETT_VERSION] = losablieferungDto.getSeriennrChargennrMitMenge().get(i)
							.getCVersion();

					oZeile[ABLIEFERETIKETT_SUBREPORT_GERAETESNR] = getLagerFac()
							.getSubreportGeraeteseriennummernEinerLagerbewegung(LocaleFac.BELEGART_LOSABLIEFERUNG,
									losablieferungIId, snr, theClientDto);

					for (int ex = 0; ex < iExemplare; ex++) {

						Object[] oZeileClone = oZeile.clone();

						oZeileClone[ABLIEFERETIKETT_EXEMPLAR] = ex + 1;

						alDaten.add(oZeileClone);

					}

				}
			}

			data = (Object[][]) alDaten.toArray(new Object[alDaten.size()][ABLIEFERETIKETT_ANZAHL_SPALTEN]);

			initJRDS(parameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_ABLIEFERETIKETT,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

			JasperPrintLP print = getReportPrint();

			if (print != null) {
				PrintInfoDto values = new PrintInfoDto();
				values.setDocPath(new DocPath(new DocNodeLosAblieferung(losablieferungDto, losDto)));
				print.setOInfoForArchive(values);
			}

			return print;
		}

		catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, ex);
		}

	}

	/**
	 * 
	 * @param forecastpositionId ist nie null
	 * @param theClientDto
	 * @return
	 */
	private JasperPrintLP printVersandetikettVorbereitungImpl(Integer forecastpositionId, TheClientDto theClientDto) {
		this.useCase = UC_VERSANDETIKETT_VORBEREITUNG;
		try {
			Session sessionSub = FLRSessionFactory.getFactory().openSession();
			String sQuerySub = "FROM FLRLinienabruf AS l WHERE l.forecastposition_i_id=" + forecastpositionId
					+ "ORDER BY l.c_bereich_nr, l.c_linie DESC, l.flrforecastposition.flrartikel.c_nr DESC ";
			// Sortierung muss gleich wie in Los/Versandetikett sein

			org.hibernate.Query hquerySub = sessionSub.createQuery(sQuerySub);
			List<?> resultListSub = hquerySub.list();
			Iterator<?> resultListIteratorSub = resultListSub.iterator();

			JasperPrintLP print = null;
			Integer cachedReportVarianteId = theClientDto.getReportvarianteIId();

			ForecastpositionDto fpDto = getForecastFac().forecastpositionFindByPrimaryKey(forecastpositionId);
			ForecastauftragDto faDto = getForecastFac().forecastauftragFindByPrimaryKey(fpDto.getForecastauftragIId());
			FclieferadresseDto fclDto = getForecastFac().fclieferadresseFindByPrimaryKey(faDto.getFclieferadresseIId());
			ForecastDto forecastDto = getForecastFac().forecastFindByPrimaryKey(fclDto.getForecastIId());

			KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(forecastDto.getKundeIId(), theClientDto);
			KundeDto lieferadresseDto = getKundeFac().kundeFindByPrimaryKey(fclDto.getKundeIIdLieferadresse(),
					theClientDto);
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(fpDto.getArtikelIId(), theClientDto);
			LosDto losDto = getFertigungFac().losFindByForecastpositionIIdOhneExc(forecastpositionId);

			while (resultListIteratorSub.hasNext() || resultListSub.size() == 0) {
				String linie = null;
				String bereichNr = null;
				String bereichBez = null;
				String bestellnummer = fpDto.getCBestellnummer();
				java.util.Date produktionstermin = null;
				BigDecimal bdMenge = losDto != null ? losDto.getNLosgroesse() : fpDto.getNMenge();

				if (resultListIteratorSub.hasNext()) {
					FLRLinienabruf l = (FLRLinienabruf) resultListIteratorSub.next();

					linie = l.getC_linie();
					bereichNr = l.getC_bereich_nr();
					bereichBez = l.getC_bereich_bez();
					bestellnummer = l.getC_bestellnummer();
					produktionstermin = l.getT_produktionstermin();
					bdMenge = l.getN_menge();
				}

				int iAnzahlPakete = 1;

				BigDecimal bdPaketmenge = bdMenge;
				BigDecimal bdVerpackungsmittelmenge = BigDecimal.ZERO;
				if (artikelDto != null && artikelDto.getNVerpackungsmittelmenge() != null
						&& artikelDto.getNVerpackungsmittelmenge().signum() != 0) {

					double d = bdMenge.doubleValue() / artikelDto.getNVerpackungsmittelmenge().doubleValue();
					iAnzahlPakete = (int) Math.ceil(d);

					bdVerpackungsmittelmenge = artikelDto.getNVerpackungsmittelmenge();
				}

				for (int i = 0; i < iAnzahlPakete; i++) {
					Map<String, Object> parameter = new TreeMap<String, Object>();

					parameter.put("P_LIEFERANTENNUMMER_KUNDE", kdDto.getCLieferantennr());
					parameter.put("P_LIEFERANTENNUMMER_LIEFERADRESSE", lieferadresseDto.getCLieferantennr());

					parameter.put("P_LINIE", linie);
					parameter.put("P_BEREICH_NR", bereichNr);
					parameter.put("P_BEREICH_BEZ", bereichBez);
					parameter.put("P_BESTELLNUMMER", bestellnummer);
					parameter.put("P_PRODUKTIONSTERMIN", produktionstermin);
					parameter.put("P_ABRUFMENGE", bdMenge);

					parameter.put("P_FORECASTPOSITION_I_ID", forecastpositionId);

					parameter.put("P_MANDANTADRESSE", Helper.formatMandantAdresse(mandantDto));

					parameter.put("P_PACKSTUECKNR", getLieferscheinFac().getNextPackstuecknummerForecast(
							forecastpositionId, losDto != null ? losDto.getIId() : null, theClientDto));

					if (artikelDto != null) {
						fillArtikelParameter(parameter, artikelDto, theClientDto);
					}

					parameter.put("P_LOSNUMMER", null);
					parameter.put("P_PROJEKT", null);
					parameter.put("P_KOMMENTAR", null);

					if (losDto != null) {
						parameter.put("P_LOSNUMMER", losDto.getCNr());
						parameter.put("P_PROJEKT", losDto.getCProjekt());
						parameter.put("P_KOMMENTAR", losDto.getCKommentar());
					}

					parameter.put("P_EXEMPLAR", new Integer(i + 1));
					parameter.put("P_PAKETEGESAMT", iAnzahlPakete);

					if (bdPaketmenge.doubleValue() > bdVerpackungsmittelmenge.doubleValue()) {
						parameter.put("P_PAKETMENGE", bdVerpackungsmittelmenge);
					} else {
						parameter.put("P_PAKETMENGE", bdPaketmenge);
					}

					bdPaketmenge = bdPaketmenge.subtract(bdVerpackungsmittelmenge);

					theClientDto.setReportvarianteIId(cachedReportVarianteId);
					data = new Object[0][0];
					initJRDS(parameter, FertigungReportFac.REPORT_MODUL,
							FertigungReportFac.REPORT_VERSANDETIKETT_VORBEREITUNG, theClientDto.getMandant(),
							theClientDto.getLocUi(), theClientDto);
					print = print == null ? getReportPrint()
							: Helper.addReport2Report(print, getReportPrint().getPrint());
				}

				if (resultListSub.size() == 0) {
					break;
				}

			}

			if (print != null && losDto != null) {
				PrintInfoDto values = new PrintInfoDto();
				values.setDocPath(new DocPath(new DocNodeLos(losDto)));
				print.setOInfoForArchive(values);
			}

			return print;
		}

		catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, ex);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printVersandetikettVorbereitung(Integer losIId, TheClientDto theClientDto) {
		try {
			LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);
			return printVersandetikettVorbereitungImpl(losDto.getForecastpositionIId(), theClientDto);
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, ex);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printVersandetikettVorbereitungForecast(Integer forecastpositionId,
			TheClientDto theClientDto) {
		return printVersandetikettVorbereitungImpl(forecastpositionId, theClientDto);
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printVersandetikettVorbereitung0(Integer losIId, TheClientDto theClientDto) {
		this.useCase = UC_VERSANDETIKETT_VORBEREITUNG;
		try {

			LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);
			ArtikelDto artikelDto = null;
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(),
						theClientDto);
				artikelDto = stklDto.getArtikelDto();
			}

			Session sessionSub = FLRSessionFactory.getFactory().openSession();
			String sQuerySub = "FROM FLRLinienabruf AS l WHERE l.forecastposition_i_id="
					+ losDto.getForecastpositionIId()
					+ "ORDER BY l.c_bereich_nr, l.c_linie DESC, l.flrforecastposition.flrartikel.c_nr DESC ";
			// Sortierung muss gleich wie in Los/Versandetikett sein

			org.hibernate.Query hquerySub = sessionSub.createQuery(sQuerySub);
			List<?> resultListSub = hquerySub.list();
			Iterator<?> resultListIteratorSub = resultListSub.iterator();

			JasperPrintLP print = null;
			Integer cachedReportVarianteId = theClientDto.getReportvarianteIId();

			ForecastpositionDto fpDto = getForecastFac()
					.forecastpositionFindByPrimaryKey(losDto.getForecastpositionIId());
			ForecastauftragDto faDto = getForecastFac().forecastauftragFindByPrimaryKey(fpDto.getForecastauftragIId());
			FclieferadresseDto fclDto = getForecastFac().fclieferadresseFindByPrimaryKey(faDto.getFclieferadresseIId());
			ForecastDto forecastDto = getForecastFac().forecastFindByPrimaryKey(fclDto.getForecastIId());

			KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(forecastDto.getKundeIId(), theClientDto);
			KundeDto lieferadresseDto = getKundeFac().kundeFindByPrimaryKey(fclDto.getKundeIIdLieferadresse(),
					theClientDto);
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);

			while (resultListIteratorSub.hasNext() || resultListSub.size() == 0) {

				String linie = null;
				String bereichNr = null;
				String bereichBez = null;
				String bestellnummer = null;
				java.util.Date produktionstermin = null;
				BigDecimal bdMenge = losDto.getNLosgroesse();

				if (resultListIteratorSub.hasNext()) {
					FLRLinienabruf l = (FLRLinienabruf) resultListIteratorSub.next();

					linie = l.getC_linie();
					bereichNr = l.getC_bereich_nr();
					bereichBez = l.getC_bereich_bez();
					bestellnummer = l.getC_bestellnummer();
					produktionstermin = l.getT_produktionstermin();
					bdMenge = l.getN_menge();
				}

				int iAnzahlPakete = 1;

				BigDecimal bdPaketmenge = bdMenge;
				BigDecimal bdVerpackungsmittelmenge = BigDecimal.ZERO;
				if (artikelDto != null && artikelDto.getNVerpackungsmittelmenge() != null
						&& artikelDto.getNVerpackungsmittelmenge().signum() != 0) {

					double d = bdMenge.doubleValue() / artikelDto.getNVerpackungsmittelmenge().doubleValue();
					iAnzahlPakete = (int) Math.ceil(d);

					bdVerpackungsmittelmenge = artikelDto.getNVerpackungsmittelmenge();
				}

				for (int i = 0; i < iAnzahlPakete; i++) {

					Map<String, Object> parameter = new TreeMap<String, Object>();

					parameter.put("P_LIEFERANTENNUMMER_KUNDE", kdDto.getCLieferantennr());
					parameter.put("P_LIEFERANTENNUMMER_LIEFERADRESSE", lieferadresseDto.getCLieferantennr());

					parameter.put("P_LINIE", linie);
					parameter.put("P_BEREICH_NR", bereichNr);
					parameter.put("P_BEREICH_BEZ", bereichBez);
					parameter.put("P_BESTELLNUMMER", bestellnummer);
					parameter.put("P_PRODUKTIONSTERMIN", produktionstermin);
					parameter.put("P_ABRUFMENGE", bdMenge);

					parameter.put("P_FORECASTPOSITION_I_ID", losDto.getForecastpositionIId());

					parameter.put("P_MANDANTADRESSE", Helper.formatMandantAdresse(mandantDto));

					parameter.put("P_PACKSTUECKNR", getLieferscheinFac().getNextPackstuecknummer(null, null,
							losDto.getIId(), null, theClientDto));

					if (artikelDto != null) {
						fillArtikelParameter(parameter, artikelDto, theClientDto);
					}

					parameter.put("P_LOSNUMMER", losDto.getCNr());

					parameter.put("P_PROJEKT", losDto.getCProjekt());
					parameter.put("P_KOMMENTAR", losDto.getCKommentar());

					parameter.put("P_EXEMPLAR", new Integer(i + 1));
					parameter.put("P_PAKETEGESAMT", iAnzahlPakete);

					if (bdPaketmenge.doubleValue() > bdVerpackungsmittelmenge.doubleValue()) {
						parameter.put("P_PAKETMENGE", bdVerpackungsmittelmenge);
					} else {
						parameter.put("P_PAKETMENGE", bdPaketmenge);
					}

					bdPaketmenge = bdPaketmenge.subtract(bdVerpackungsmittelmenge);

					theClientDto.setReportvarianteIId(cachedReportVarianteId);
					data = new Object[0][0];
					initJRDS(parameter, FertigungReportFac.REPORT_MODUL,
							FertigungReportFac.REPORT_VERSANDETIKETT_VORBEREITUNG, theClientDto.getMandant(),
							theClientDto.getLocUi(), theClientDto);
					print = print == null ? getReportPrint()
							: Helper.addReport2Report(print, getReportPrint().getPrint());
				}

				if (resultListSub.size() == 0) {
					break;
				}

			}

			if (print != null) {
				PrintInfoDto values = new PrintInfoDto();
				values.setDocPath(new DocPath(new DocNodeLos(losDto)));
				print.setOInfoForArchive(values);
			}

			return print;
		}

		catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, ex);
		}

	}

	private void fillArtikelParameter(Map<String, Object> parameter, ArtikelDto artikelDto, TheClientDto theClientDto)
			throws RemoteException {
		parameter.put("P_ARTIKELNUMMER", artikelDto.getCNr());

		if (artikelDto.getArtikelsprDto() != null) {
			parameter.put("P_BEZEICHNUNG", artikelDto.getArtikelsprDto().getCBez());
			parameter.put("P_KURZBEZEICHNUNG", artikelDto.getArtikelsprDto().getCKbez());
			parameter.put("P_ZUSATZBEZEICHNUNG", artikelDto.getArtikelsprDto().getCZbez());
			parameter.put("P_ZUSATZBEZEICHNUNG2", artikelDto.getArtikelsprDto().getCZbez2());
		}

		parameter.put("P_EINHEIT", artikelDto.getEinheitCNr());
		parameter.put("P_REFERENZNUMMER", artikelDto.getCReferenznr());
		parameter.put("P_REVISION", artikelDto.getCRevision());
		parameter.put("P_INDEX", artikelDto.getCIndex());
		parameter.put("P_VERPACKUNGSMENGE", artikelDto.getFVerpackungsmenge());
		parameter.put("P_VERPACKUNGSMITTELMENGE", artikelDto.getNVerpackungsmittelmenge());

		if (artikelDto.getVerpackungsmittelIId() != null) {
			VerpackungsmittelDto verpackungsmittelDto = getArtikelFac().verpackungsmittelFindByPrimaryKey(
					artikelDto.getVerpackungsmittelIId(),

					theClientDto);

			parameter.put("P_VERPACKUNGSMITTEL_KENNUNG", verpackungsmittelDto.getCNr());
			parameter.put("P_VERPACKUNGSMITTEL_BEZEICHNUNG", verpackungsmittelDto.getBezeichnung());
			parameter.put("P_VERPACKUNGSMITTEL_GEWICHT_IN_KG", verpackungsmittelDto.getNGewichtInKG());
		}

		if (artikelDto.getMaterialIId() != null) {
			MaterialDto materialDto = getMaterialFac().materialFindByPrimaryKey(artikelDto.getMaterialIId(),
					theClientDto);
			parameter.put("P_MATERIAL", materialDto.getBezeichnung());
		}

		parameter.put("P_ARTIKELGEWICHT_IN_KG", artikelDto.getFGewichtkg());

		if (artikelDto.getGeometrieDto() != null) {
			parameter.put("P_BREITE", artikelDto.getGeometrieDto().getFBreite());
			parameter.put("P_HOEHE", artikelDto.getGeometrieDto().getFHoehe());
			parameter.put("P_TIEFE", artikelDto.getGeometrieDto().getFTiefe());
		}

		if (artikelDto.getVerpackungDto() != null) {
			parameter.put("P_BAUFORM", artikelDto.getVerpackungDto().getCBauform());
			parameter.put("P_VERPACKUNGSART", artikelDto.getVerpackungDto().getCVerpackungsart());
		}

		parameter.put("P_VERPACKUNGS_EAN", artikelDto.getCVerpackungseannr());
		parameter.put("P_VERKAUFS_EAN", artikelDto.getCVerkaufseannr());

		if (artikelDto.getHerstellerIId() != null) {
			HerstellerDto herstellerDto = getArtikelFac().herstellerFindByPrimaryKey(artikelDto.getHerstellerIId(),
					theClientDto);
			parameter.put("P_HERSTELLER", herstellerDto.getCNr());
			parameter.put("P_HERSTELLER_NAME1", herstellerDto.getPartnerDto().getCName1nachnamefirmazeile1());
			parameter.put("P_HERSTELLER_NAME2", herstellerDto.getPartnerDto().getCName2vornamefirmazeile2());
		}

		ArtikellieferantDto[] artikellieferantDtos = getArtikelFac()
				.artikellieferantFindByArtikelIId(artikelDto.getIId(), theClientDto);

		if (artikellieferantDtos != null && artikellieferantDtos.length > 0) {
			parameter.put("P_LIEFERANT",
					getLieferantFac().lieferantFindByPrimaryKey(artikellieferantDtos[0].getLieferantIId(), theClientDto)
							.getPartnerDto().formatAnrede());
			parameter.put("P_LIEFERANT_ARTIKELNUMMER", artikellieferantDtos[0].getCArtikelnrlieferant());
			parameter.put("P_LIEFERANT_ARTIKELBEZEICHNUNG", artikellieferantDtos[0].getCBezbeilieferant());
		}
		Integer iIdHauplpager = getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId();
		if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet())) {
			parameter.put("P_LAGERSTAND",
					getLagerFac().getLagerstand(artikelDto.getIId(), iIdHauplpager, theClientDto));
		}

		parameter.put("P_LAGERORT", getLagerFac().getLagerplaezteEinesArtikels(artikelDto.getIId(), iIdHauplpager));
	}

	public JasperPrintLP printVersandetikettAblieferung(Integer losablieferungIId, Integer iKopien,
			TheClientDto theClientDto) {
		this.useCase = UC_VERSANDETIKETT_ABLIEFERUNG;
		try {

			int iGesamt = 1;
			if (iKopien != null) {
				iGesamt = iKopien + 1;
			}

			LosablieferungDto losablieferungDto = getFertigungFac().losablieferungFindByPrimaryKey(losablieferungIId,
					false, theClientDto);
			LosDto losDto = getFertigungFac().losFindByPrimaryKey(losablieferungDto.getLosIId());

			int iAnzahlPakete = 1;

			StuecklisteDto stklDto = null;
			BigDecimal bdVPMenge = losablieferungDto.getNMenge();
			if (losDto.getStuecklisteIId() != null) {
				stklDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(), theClientDto);
				ArtikelDto artikelDto = stklDto.getArtikelDto();

				if (artikelDto.getNVerpackungsmittelmenge() != null
						&& artikelDto.getNVerpackungsmittelmenge().doubleValue() != 0) {

					bdVPMenge = artikelDto.getNVerpackungsmittelmenge();

					double d = losablieferungDto.getNMenge().doubleValue()
							/ artikelDto.getNVerpackungsmittelmenge().doubleValue();
					iAnzahlPakete = (int) Math.ceil(d);
				}
			}

			JasperPrintLP print = null;
			Integer cachedReportVarianteId = theClientDto.getReportvarianteIId();

			BigDecimal bdPaketmenge = losablieferungDto.getNMenge();

			for (int i = 0; i < iAnzahlPakete; i++) {

				Long packstuecknr = getLieferscheinFac().getNextPackstuecknummer(null, null,
						losablieferungDto.getLosIId(), losablieferungIId, theClientDto);

				BigDecimal paketmengeTemp = BigDecimal.ZERO;
				if (bdPaketmenge.doubleValue() > bdVPMenge.doubleValue()) {

					paketmengeTemp = bdVPMenge;
				} else {

					paketmengeTemp = bdPaketmenge;
				}

				bdPaketmenge = bdPaketmenge.subtract(bdVPMenge);

				for (int j = 0; j < iGesamt; j++) {

					Map<String, Object> parameter = new TreeMap<String, Object>();

					parameter.put("P_PAKETMENGE", paketmengeTemp);

					if (stklDto != null) {

						ArtikelDto artikelDto = stklDto.getArtikelDto();

						parameter.put("P_ARTIKELNUMMER", artikelDto.getCNr());

						if (artikelDto.getArtikelsprDto() != null) {
							parameter.put("P_BEZEICHNUNG", artikelDto.getArtikelsprDto().getCBez());
							parameter.put("P_KURZBEZEICHNUNG", artikelDto.getArtikelsprDto().getCKbez());
							parameter.put("P_ZUSATZBEZEICHNUNG", artikelDto.getArtikelsprDto().getCZbez());
							parameter.put("P_ZUSATZBEZEICHNUNG2", artikelDto.getArtikelsprDto().getCZbez2());
						}

						// Erste gefundene Version anzeigen
						LagerbewegungDto[] lDto = getLagerFac().lagerbewegungFindByBelegartCNrBelegartPositionIId(
								LocaleFac.BELEGART_LOSABLIEFERUNG, losablieferungDto.getIId());
						if (lDto != null && lDto.length > 0) {
							parameter.put("P_VERSION", lDto[0].getCVersion());
						}

						MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(),
								theClientDto);

						parameter.put("P_EINHEIT", artikelDto.getEinheitCNr());
						parameter.put("P_REFERENZNUMMER", artikelDto.getCReferenznr());
						parameter.put("P_REVISION", artikelDto.getCRevision());
						parameter.put("P_INDEX", artikelDto.getCIndex());
						parameter.put("P_VERPACKUNGSMENGE", artikelDto.getFVerpackungsmenge());
						parameter.put("P_VERPACKUNGSMITTELMENGE", artikelDto.getNVerpackungsmittelmenge());

						if (artikelDto.getVerpackungsmittelIId() != null) {
							VerpackungsmittelDto verpackungsmittelDto = getArtikelFac()
									.verpackungsmittelFindByPrimaryKey(artikelDto.getVerpackungsmittelIId(),

											theClientDto);

							parameter.put("P_VERPACKUNGSMITTEL_KENNUNG", verpackungsmittelDto.getCNr());
							parameter.put("P_VERPACKUNGSMITTEL_BEZEICHNUNG", verpackungsmittelDto.getBezeichnung());
							parameter.put("P_VERPACKUNGSMITTEL_GEWICHT_IN_KG", verpackungsmittelDto.getNGewichtInKG());
						}

						parameter.put("P_PACKSTUECKNR", packstuecknr);

						parameter.put("P_MANDANTADRESSE", Helper.formatMandantAdresse(mandantDto));

						if (artikelDto.getMaterialIId() != null) {
							MaterialDto materialDto = getMaterialFac()
									.materialFindByPrimaryKey(artikelDto.getMaterialIId(), theClientDto);
							parameter.put("P_MATERIAL", materialDto.getBezeichnung());
						}

						if (artikelDto.getGeometrieDto() != null) {
							parameter.put("P_BREITE", artikelDto.getGeometrieDto().getFBreite());
							parameter.put("P_HOEHE", artikelDto.getGeometrieDto().getFHoehe());
							parameter.put("P_TIEFE", artikelDto.getGeometrieDto().getFTiefe());
						}

						if (artikelDto.getVerpackungDto() != null) {
							parameter.put("P_BAUFORM", artikelDto.getVerpackungDto().getCBauform());
							parameter.put("P_VERPACKUNGSART", artikelDto.getVerpackungDto().getCVerpackungsart());
						}

						parameter.put("P_VERPACKUNGS_EAN", artikelDto.getCVerpackungseannr());
						parameter.put("P_VERKAUFS_EAN", artikelDto.getCVerkaufseannr());

						if (artikelDto.getHerstellerIId() != null) {
							HerstellerDto herstellerDto = getArtikelFac()
									.herstellerFindByPrimaryKey(artikelDto.getHerstellerIId(), theClientDto);
							parameter.put("P_HERSTELLER", herstellerDto.getCNr());
							parameter.put("P_HERSTELLER_NAME1",
									herstellerDto.getPartnerDto().getCName1nachnamefirmazeile1());
							parameter.put("P_HERSTELLER_NAME2",
									herstellerDto.getPartnerDto().getCName2vornamefirmazeile2());
						}

						ArtikellieferantDto[] artikellieferantDtos = getArtikelFac()
								.artikellieferantFindByArtikelIId(artikelDto.getIId(), theClientDto);

						if (artikellieferantDtos != null && artikellieferantDtos.length > 0) {
							parameter.put("P_LIEFERANT", getLieferantFac()
									.lieferantFindByPrimaryKey(artikellieferantDtos[0].getLieferantIId(), theClientDto)
									.getPartnerDto().formatAnrede());
							parameter.put("P_LIEFERANT_ARTIKELNUMMER",
									artikellieferantDtos[0].getCArtikelnrlieferant());
							parameter.put("P_LIEFERANT_ARTIKELBEZEICHNUNG",
									artikellieferantDtos[0].getCBezbeilieferant());
						}
						Integer iIdHauplpager = getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId();
						if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet())) {
							parameter.put("P_LAGERSTAND",
									getLagerFac().getLagerstand(artikelDto.getIId(), iIdHauplpager, theClientDto));
						}

						parameter.put("P_LAGERORT",
								getLagerFac().getLagerplaezteEinesArtikels(artikelDto.getIId(), iIdHauplpager));

					}

					parameter.put("P_LOSNUMMER", losDto.getCNr());
					parameter.put("P_ABLIEFERDATUM", losablieferungDto.getTAendern());
					parameter.put("P_PROJEKT", losDto.getCProjekt());
					parameter.put("P_KOMMENTAR", losDto.getCKommentar());

					// PJ18617
					PersonalDto personalDto = getPersonalFac()
							.personalFindByPrimaryKey(losablieferungDto.getPersonalIIdAendern(), theClientDto);
					parameter.put("P_PERSON_ABGELIEFERT_KURZZEICHEN", personalDto.getCKurzzeichen());
					parameter.put("P_PERSON_ABGELIEFERT", personalDto.formatFixUFTitelName2Name1());

					parameter.put("P_SNRCHNR", SeriennrChargennrMitMengeDto
							.erstelleStringAusMehrerenSeriennummern(losablieferungDto.getSeriennrChargennrMitMenge()));

					parameter.put("P_MENGE", losablieferungDto.getNMenge());

					parameter.put("P_EXEMPLAR", new Integer(i + 1));
					parameter.put("P_PAKETEGESAMT", iAnzahlPakete);

					parameter.put("P_KOPIE", j);

					theClientDto.setReportvarianteIId(cachedReportVarianteId);
					data = new Object[0][0];
					initJRDS(parameter, FertigungReportFac.REPORT_MODUL,
							FertigungReportFac.REPORT_VERSANDETIKETT_ABLIEFERUNG, theClientDto.getMandant(),
							theClientDto.getLocUi(), theClientDto);
					print = print == null ? getReportPrint()
							: Helper.addReport2Report(print, getReportPrint().getPrint());

				}

			}

			if (print != null) {
				PrintInfoDto values = new PrintInfoDto();
				values.setDocPath(new DocPath(new DocNodeLosAblieferung(losablieferungDto, losDto)));
				print.setOInfoForArchive(values);
			}

			return print;
		}

		catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, ex);
		}

	}

	public JasperPrintLP printMehrereFertigungsbegleitscheine(ArrayList<Integer> losIIds, TheClientDto theClientDto) {

		JasperPrintLP print = null;

		for (int i = 0; i < losIIds.size(); i++) {

			JasperPrintLP printEinzeln = printFertigungsbegleitschein(losIIds.get(i), false, theClientDto);

			if (print == null) {
				print = printEinzeln;
			} else {
				print = Helper.addReport2Report(print, printEinzeln);
			}

		}
		return print;
	}

	public JasperPrintLP printFertigungsbegleitschein(Integer losIId, Boolean bStammtVonSchnellanlage,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Session session = null;
		try {
			this.useCase = UC_FERTIGUNGSBEGLEITSCHEIN;
			this.index = -1;
			// Los holen
			LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);

			String sEinheit = null;

			try {
				ParametermandantDto parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
						theClientDto.getMandant(), ParameterFac.KATEGORIE_STUECKLISTE,
						ParameterFac.PARAMETER_STUECKLISTE_ARBEITSPLAN_ZEITEINHEIT);
				sEinheit = parameter.getCWert().trim();
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

			if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN, "");
			} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT, "");
			} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT, "");
			}

			// Hole Parameter UNTERDRUECKE_ARTIKELNR_NULLTAETIGKEIT
			String artikelnrZuUnterdruecken = null;
			ParametermandantDto parameterNulltaetigkeit = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_UNTERDRUECKE_ARTIKELNR_NULLTAETIGKEIT);
			if (parameterNulltaetigkeit.getCWert() != null && !parameterNulltaetigkeit.getCWert().equals("")
					&& !parameterNulltaetigkeit.getCWert().equals(" ")) {
				artikelnrZuUnterdruecken = parameterNulltaetigkeit.getCWert();
			}

			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRLossollarbeitsplan.class);
			if (artikelnrZuUnterdruecken != null) {
				c.createAlias(FertigungFac.FLR_LOSSOLLARBEITSPLAN_FLRARTIKEL, "a");
				c.add(Restrictions
						.not(Restrictions.and(Restrictions.isNotNull(FertigungFac.FLR_LOSSOLLARBEITSPLAN_MASCHINE_I_ID),
								Restrictions.eq("a.c_nr", artikelnrZuUnterdruecken))));
			}
			c.add(Restrictions.eq(FertigungFac.FLR_LOSSOLLARBEITSPLAN_LOS_I_ID, losIId));
			// Sortierung nach Arbeitsgang
			c.addOrder(Order.asc(FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_ARBEITSGANGNUMMER));
			c.addOrder(Order.asc(FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_UNTERARBEITSGANG));
			List<?> list = c.list();

			int iSizeMaterial = 0;
			ParametermandantDto parameterMaterial = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_FERTIGUNGSBEGLEITSCHEIN_MIT_MATERIAL);
			short iMaterial = Short.parseShort(parameterMaterial.getCWert());
			boolean bMitMaterial = Helper.short2boolean(iMaterial);
			if (bMitMaterial) {
				// Hier Material einfuegen
				Object[][] material = getDataAusgabeListe(new Integer[] { losIId }, Helper.SORTIERUNG_NACH_IDENT, true,
						false, null, theClientDto);

				if (material == null) {
					iSizeMaterial = 0;
				} else {
					iSizeMaterial = material.length;
				}

				data = new Object[list.size() + material.length][BEGL_SPALTENANZAHL];
				for (int i = 0; i < iSizeMaterial; i++) {
					data[i][BEGL_IST_MATERIAL] = new Boolean(true);
					data[i][BEGL_MATERIAL_ARTIKELKLASSE] = material[i][AUSG_ARTIKELKLASSE];
					data[i][BEGL_MATERIAL_AUSGABE] = material[i][AUSG_AUSGABE];
					data[i][BEGL_MATERIAL_BEZEICHNUNG] = material[i][AUSG_BEZEICHNUNG];
					data[i][BEGL_MATERIAL_KURZBEZEICHNUNG] = material[i][AUSG_KURZBEZEICHNUNG];
					data[i][BEGL_MATERIAL_REFERENZNUMMER] = material[i][AUSG_REFERENZNUMMER];
					data[i][BEGL_MATERIAL_EINHEIT] = material[i][AUSG_EINHEIT];
					data[i][BEGL_MATERIAL_FARBCODE] = material[i][AUSG_FARBCODE];
					data[i][BEGL_MATERIAL_IDENT] = material[i][AUSG_IDENT];
					data[i][BEGL_MATERIAL_LAGER] = material[i][AUSG_LAGER];
					data[i][BEGL_MATERIAL_LAGERORT] = material[i][AUSG_LAGERORT];
					data[i][BEGL_MATERIAL_MENGE] = material[i][AUSG_MENGE];
					data[i][BEGL_MATERIAL_MONTAGEART] = material[i][AUSG_MONTAGEART];
					data[i][BEGL_MATERIAL_SCHALE] = material[i][AUSG_SCHALE];
					data[i][BEGL_MATERIAL_ZUSATZBEZEICHNUNG] = material[i][AUSG_ZUSATZBEZEICHNUNG];
					data[i][BEGL_MATERIAL_ZUSATZBEZEICHNUNG2] = material[i][AUSG_ZUSATZBEZEICHNUNG2];
					data[i][BEGL_MATERIAL_HOEHE] = material[i][AUSG_HOEHE];
					data[i][BEGL_MATERIAL_BREITE] = material[i][AUSG_BREITE];
					data[i][BEGL_MATERIAL_TIEFE] = material[i][AUSG_TIEFE];
					data[i][BEGL_MATERIAL_MATERIAL] = material[i][AUSG_MATERIAL];
					data[i][BEGL_BAUFORM] = material[i][AUSG_BAUFORM];
					data[i][BEGL_VERPACKUNGSART] = material[i][AUSG_VERPACKUNGSART];
					data[i][BEGL_GEWICHTKG] = material[i][AUSG_GEWICHTKG];
					data[i][BEGL_MATERIAL_REVISION] = material[i][AUSG_REVISION];
					data[i][BEGL_MATERIAL_INDEX] = material[i][AUSG_INDEX];
					data[i][BEGL_NURZURINFORMATION] = material[i][AUSG_NUR_ZUR_INFO];
					data[i][BEGL_MATERIAL_SNRCHNR] = material[i][AUSG_SNRCHNR];
					data[i][BEGL_MATERIAL_STUECKLISTE_POSITION] = material[i][AUSG_STUECKLISTE_POSITION];

					data[i][BEGL_MATERIAL_STUECKLISTE_SOLLMENGE] = material[i][AUSG_STUECKLISTE_SOLLMENGE];
					data[i][BEGL_MATERIAL_STUECKLISTE_EINHEIT] = material[i][AUSG_STUECKLISTE_EINHEIT];
					data[i][BEGL_MATERIAL_DIMENSION1] = material[i][AUSG_MATERIAL_DIMENSION1];
					data[i][BEGL_MATERIAL_DIMENSION2] = material[i][AUSG_MATERIAL_DIMENSION2];
					data[i][BEGL_MATERIAL_DIMENSION3] = material[i][AUSG_MATERIAL_DIMENSION3];

				}

			} else {
				data = new Object[list.size()][BEGL_SPALTENANZAHL];
			}

			int i = 0;
			for (Iterator<?> iter = list.iterator(); iter.hasNext(); i++) {
				FLRLossollarbeitsplan item = (FLRLossollarbeitsplan) iter.next();
				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(item.getFlrartikel().getI_id(),
						theClientDto);
				data[i + iSizeMaterial][BEGL_IST_MATERIAL] = new Boolean(false);
				data[i + iSizeMaterial][BEGL_IDENT] = artikelDto.getCNr();
				data[i + iSizeMaterial][BEGL_NURZURINFORMATION] = Helper.short2Boolean(artikelDto.getbNurzurinfo());
				data[i + iSizeMaterial][BEGL_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
				data[i + iSizeMaterial][BEGL_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCZbez();
				data[i + iSizeMaterial][BEGL_ZUSATZBEZEICHNUNG2] = artikelDto.getArtikelsprDto().getCZbez2();
				data[i + iSizeMaterial][BEGL_ARBEITSGANG] = item.getI_arbeitsgangsnummer();
				data[i + iSizeMaterial][BEGL_UNTERARBEITSGANG] = item.getI_unterarbeitsgang();
				data[i + iSizeMaterial][BEGL_AGART] = item.getAgart_c_nr();
				data[i + iSizeMaterial][BEGL_AUFSPANNUNG] = item.getI_aufspannung();
				data[i + iSizeMaterial][BEGL_MATERIAL_REVISION] = artikelDto.getCRevision();
				data[i + iSizeMaterial][BEGL_MATERIAL_INDEX] = artikelDto.getCIndex();
				data[i + iSizeMaterial][BEGL_FERTIG] = Helper.short2Boolean(item.getB_fertig());

				// PJ19263
				if (item.getFlrapkommentar() != null) {

					Iterator<?> sprsetIterator = item.getFlrapkommentar().getApkommentarspr_set().iterator();
					String kommentar = null;
					while (sprsetIterator.hasNext()) {
						FLRApkommentarspr spr = (FLRApkommentarspr) sprsetIterator.next();
						if (spr.getLocale().getC_nr().compareTo(theClientDto.getLocUiAsString()) == 0) {
							kommentar = spr.getC_bez();
							break;
						}
					}

					if (kommentar == null) {
						kommentar = getTextRespectUISpr("fert.apkommentar.uebersetzungfehlt", theClientDto.getMandant(),
								theClientDto.getLocUi());
					}

					data[i + iSizeMaterial][BEGL_ARBEITSPLANKOMMENTAR] = kommentar;
				}

				// Ein Mandantenparameter entscheidet, ob auch die Sollzeiten
				// gedruckt werden
				ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.FERTIGUNG_FERTIGUNGSBEGLEITSCHEIN_MIT_SOLLDATEN);
				short iValue = Short.parseShort(parameter.getCWert());
				boolean bDruckeSolldaten = Helper.short2boolean(iValue);
				if (bDruckeSolldaten) {
					data[i + iSizeMaterial][BEGL_RUESTZEIT] = item.getL_ruestzeit().divide(new BigDecimal(1000 * 60), 4,
							BigDecimal.ROUND_HALF_EVEN);
					data[i + iSizeMaterial][BEGL_STUECKZEIT] = item.getL_stueckzeit().divide(new BigDecimal(1000 * 60),
							4, BigDecimal.ROUND_HALF_EVEN);
					data[i + iSizeMaterial][BEGL_GESAMTZEIT] = item.getN_gesamtzeit();
				}
				LossollarbeitsplanDto l = getFertigungFac().lossollarbeitsplanFindByPrimaryKey(item.getI_id());
				StringBuffer sKommentar = new StringBuffer();
				if (l.getCKomentar() != null) {
					sKommentar.append(l.getCKomentar() + "\n");
				}
				if (l.getXText() != null) {
					sKommentar.append(l.getXText());
				}
				data[i + iSizeMaterial][BEGL_KOMMENTAR] = sKommentar.toString();
				// Maschinenzeiterfassung
				if (l.getMaschineIId() != null) {
					MaschineDto maschineDto = getZeiterfassungFac().maschineFindByPrimaryKey(l.getMaschineIId());
					data[i + iSizeMaterial][BEGL_MASCHINE] = maschineDto.getCIdentifikationsnr();
					data[i + iSizeMaterial][BEGL_MASCHINE_BEZEICHNUNG] = maschineDto.getCBez();
					data[i + iSizeMaterial][BEGL_MASCHINE_MANUELLE_BEDIENUNG] = Helper
							.short2Boolean(maschineDto.getBManuelleBedienung());
				}

				if (item.getI_maschinenversatztage() == null) {
					data[i + iSizeMaterial][BEGL_BEGINN] = item.getFlrlos().getT_produktionsbeginn();
				} else {
					data[i + iSizeMaterial][BEGL_BEGINN] = Helper.addiereTageZuDatum(
							item.getFlrlos().getT_produktionsbeginn(), item.getI_maschinenversatztage());
				}

				data[i + iSizeMaterial][BEGL_MASCHINENVERSATZ_MS] = item.getI_maschinenversatz_ms();

				// PJ19746 Alternativmaschinen einfuegen

				if (getMandantFac().hatZusatzfunktionberechtigung(MandantFac.ZUSATZFUNKTION_ALTERNATIV_MASCHINEN,
						theClientDto)) {

					List<Integer> alAlternativMaschinen = getStuecklisteFac().getMoeglicheMaschinen(l.getIId(),
							theClientDto);

					if (l.getMaschineIId() != null) {

						for (int k = 0; k < alAlternativMaschinen.size(); k++) {
							Integer maschineIId = alAlternativMaschinen.get(k);

							if (l.getMaschineIId().equals(maschineIId)) {
								alAlternativMaschinen.remove(k);
								break;
							}
						}
					}

					String[] fieldnamesEigenschaften = new String[] { "F_MASCHINE", "F_MASCHINE_BEZEICHNUNG", };
					Object[][] dataSub = new Object[alAlternativMaschinen.size()][fieldnamesEigenschaften.length];

					for (int k = 0; k < alAlternativMaschinen.size(); k++) {
						Integer maschineIId = alAlternativMaschinen.get(k);

						MaschineDto mDto = getZeiterfassungFac().maschineFindByPrimaryKey(maschineIId);
						dataSub[k][0] = mDto.getCIdentifikationsnr();
						dataSub[k][1] = mDto.getCBez();
					}

					data[i + iSizeMaterial][BEGL_SUBREPORT_ALTERNATIV_MASCHINEN] = new LPDatenSubreport(dataSub,
							fieldnamesEigenschaften);

				}

				if (l.getLossollmaterialIId() != null) {
					LossollmaterialDto posDto = getFertigungFac()
							.lossollmaterialFindByPrimaryKey(l.getLossollmaterialIId());
					ArtikelDto artikelDtoFremdmaterial = getArtikelFac()
							.artikelFindByPrimaryKeySmall(posDto.getArtikelIId(), theClientDto);
					data[i + iSizeMaterial][BEGL_FREMDMATERIAL_ARTIKEL] = artikelDtoFremdmaterial.getCNr();
					if (artikelDtoFremdmaterial.getArtikelsprDto() != null) {
						data[i + iSizeMaterial][BEGL_FREMDMATERIAL_ARTIKELBEZEICHNUNG] = artikelDtoFremdmaterial
								.getArtikelsprDto().getCBez();
						data[i + iSizeMaterial][BEGL_FREMDMATERIAL_ARTIKELKURZBEZEICHNUNG] = artikelDtoFremdmaterial
								.getArtikelsprDto().getCKbez();
						data[i + iSizeMaterial][BEGL_FREMDMATERIAL_ARTIKELZUSATZBEZEICHNUNG] = artikelDtoFremdmaterial
								.getArtikelsprDto().getCZbez();
						data[i + iSizeMaterial][BEGL_FREMDMATERIAL_ARTIKELZUSATZBEZEICHNUNG2] = artikelDtoFremdmaterial
								.getArtikelsprDto().getCZbez2();
					}
					data[i + iSizeMaterial][BEGL_FREMDMATERIAL_SOLLMENGE] = posDto.getNMenge();

					LoslagerentnahmeDto[] laeger = getFertigungFac().loslagerentnahmeFindByLosIId(posDto.getLosIId());
					if (laeger.length > 0) {
						ArtikellagerplaetzeDto artikellagerplaetzeDto = getLagerFac()
								.artikellagerplaetzeFindByArtikelIIdLagerIId(artikelDtoFremdmaterial.getIId(),
										laeger[0].getLagerIId());
						if (artikellagerplaetzeDto != null && artikellagerplaetzeDto.getLagerplatzDto() != null) {
							data[i + iSizeMaterial][BEGL_FREMDMATERIAL_LAGERORT] = artikellagerplaetzeDto
									.getLagerplatzDto().getCLagerplatz();
						}
					}

				}

				// PJ19374
				// Naechster AG
				Integer sollarbeitsplanIId_NaechterAG = null;

				try {
					LossollarbeitsplanDto[] sollDtos = getFertigungFac()
							.lossollarbeitsplanFindByLosIId(item.getLos_i_id());

					for (int j = 0; j < sollDtos.length; j++) {

						if (item.getI_id().equals(sollDtos[j].getIId())) {

							// Der naechste ists dann
							if (j < sollDtos.length - 1) {
								sollarbeitsplanIId_NaechterAG = sollDtos[j + 1].getIId();
							}

						}

					}

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				if (sollarbeitsplanIId_NaechterAG != null) {

					LossollarbeitsplanDto saDto_Next = getFertigungFac()
							.lossollarbeitsplanFindByPrimaryKey(sollarbeitsplanIId_NaechterAG);

					data[i + iSizeMaterial][BEGL_NEXT_AG_AGART] = saDto_Next.getAgartCNr();

					if (saDto_Next.getIMaschinenversatztage() == null) {
						data[i + iSizeMaterial][BEGL_NEXT_AG_BEGINN] = item.getFlrlos().getT_produktionsbeginn();
					} else {
						data[i + iSizeMaterial][BEGL_NEXT_AG_BEGINN] = Helper.addiereTageZuDatum(
								item.getFlrlos().getT_produktionsbeginn(), saDto_Next.getIMaschinenversatztage());
					}

					data[i + iSizeMaterial][BEGL_NEXT_AG_MASCHINENVERSATZ_MS] = saDto_Next.getIMaschinenversatzMs();

					data[i + iSizeMaterial][BEGL_NEXT_AG_ARBEITSGANG] = saDto_Next.getIArbeitsgangnummer();

					try {
						if (saDto_Next.getLossollmaterialIId() != null) {
							LossollmaterialDto sollmatDto = getFertigungFac()
									.lossollmaterialFindByPrimaryKey(saDto_Next.getLossollmaterialIId());

							ArtikelDto aDtoSollmat = getArtikelFac()
									.artikelFindByPrimaryKeySmall(sollmatDto.getArtikelIId(), theClientDto);
							data[i + iSizeMaterial][BEGL_NEXT_AG_FREMDMATERIAL_ARTIKEL] = aDtoSollmat.getCNr();
							data[i + iSizeMaterial][BEGL_NEXT_AG_FREMDMATERIAL_ARTIKELBEZEICHNUNG] = aDtoSollmat
									.formatBezeichnung();

						}
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

					ArtikelDto aDtoSollarbeitsplan = getArtikelFac()
							.artikelFindByPrimaryKeySmall(saDto_Next.getArtikelIIdTaetigkeit(), theClientDto);
					data[i + iSizeMaterial][BEGL_NEXT_AG_IDENT] = aDtoSollarbeitsplan.getCNr();

					data[i + iSizeMaterial][BEGL_NEXT_AG_BEZEICHNUNG] = aDtoSollarbeitsplan.formatBezeichnung();

					data[i + iSizeMaterial][BEGL_NEXT_AG_AUFSPANNUNG] = saDto_Next.getIAufspannung();

					data[i + iSizeMaterial][BEGL_NEXT_AG_KOMMENTAR] = saDto_Next.getCKomentar();

					if (saDto_Next.getMaschineIId() != null) {
						MaschineDto mDto = getZeiterfassungFac().maschineFindByPrimaryKey(saDto_Next.getMaschineIId());
						data[i + iSizeMaterial][BEGL_NEXT_AG_MASCHINE_BEZEICHNUNG] = mDto.getCBez();
						data[i + iSizeMaterial][BEGL_NEXT_AG_MASCHINE] = mDto.getCInventarnummer();
						data[i + iSizeMaterial][BEGL_NEXT_AG_MASCHINE_MANUELLE_BEDIENUNG] = Helper
								.short2Boolean(mDto.getBManuelleBedienung());
					}

					data[i + iSizeMaterial][BEGL_NEXT_AG_UNTERARBEITSGANG] = saDto_Next.getIUnterarbeitsgang();

					double lStueckzeit = saDto_Next.getLStueckzeit();
					double lRuestzeit = saDto_Next.getLRuestzeit();
					double dRuestzeit = 0;
					double dStueckzeit = 0;

					if (sEinheit.equals(SystemFac.EINHEIT_STUNDE.trim())) {
						dStueckzeit = lStueckzeit / 3600000;
						dRuestzeit = lRuestzeit / 3600000;
					} else if (sEinheit.equals(SystemFac.EINHEIT_MINUTE.trim())) {
						dStueckzeit = lStueckzeit / 60000;
						dRuestzeit = lRuestzeit / 60000;
					} else if (sEinheit.equals(SystemFac.EINHEIT_SEKUNDE.trim())) {
						dStueckzeit = lStueckzeit / 1000;
						dRuestzeit = lRuestzeit / 1000;
					}

					data[i + iSizeMaterial][BEGL_NEXT_AG_RUESTZEIT] = Helper
							.rundeKaufmaennisch(new BigDecimal(dRuestzeit), 5);
					data[i + iSizeMaterial][BEGL_NEXT_AG_STUECKZEIT] = Helper
							.rundeKaufmaennisch(new BigDecimal(dStueckzeit), 5);
					data[i + iSizeMaterial][BEGL_NEXT_AG_GESAMTZEIT] = Helper.berechneGesamtzeitInStunden(
							saDto_Next.getLRuestzeit(), saDto_Next.getLStueckzeit(), item.getFlrlos().getN_losgroesse(),
							null, saDto_Next.getIAufspannung());

					if (saDto_Next.getApkommentarIId() != null) {

						ApkommentarDto apkommentar = getStuecklisteFac()
								.apkommentarFindByPrimaryKey(saDto_Next.getApkommentarIId(), theClientDto);

						data[i + iSizeMaterial][BEGL_NEXT_AG_ARBEITSPLANKOMMENTAR] = apkommentar.getBezeichnung();
					}

				}

			}
			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			mapParameter.put("P_ANGELEGT", new java.util.Date(losDto.getTAnlegen().getTime()));
			String sAuftragsnummer;
			String sInternerKommentar = null;
			String sKunde;
			String sKundeLieferadresse;
			Timestamp dLiefertermin;
			String sLieferart;
			String sAbteilung = null;
			String sSpediteur = null;
			Boolean bPoenale = Boolean.FALSE;
			Boolean bRoHs = Boolean.FALSE;
			if (losDto.getAuftragIId() != null) {
				// Auftrag holen
				AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(losDto.getAuftragIId());
				sAuftragsnummer = auftragDto.getCNr();
				sLieferart = getLocaleFac().lieferartFindByIIdLocaleOhneExc(auftragDto.getLieferartIId(),
						theClientDto.getLocUi(), theClientDto);

				if (auftragDto.getSpediteurIId() != null) {
					sSpediteur = getMandantFac().spediteurFindByPrimaryKey(auftragDto.getSpediteurIId())
							.getCNamedesspediteurs();
				}

				bPoenale = Helper.short2Boolean(auftragDto.getBPoenale());
				bRoHs = Helper.short2Boolean(auftragDto.getBRoHs());
				// Internen Kommentar aus dem Auftrag, abhaengig von einem
				// Mandantenparameter
				ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_KOMMENTAR_AM_FERTIGUNGSBEGLEITSCHEIN);
				short iValue = Short.parseShort(parameter.getCWert());
				boolean bDruckeKommentar = Helper.short2boolean(iValue);
				if (bDruckeKommentar) {
					sInternerKommentar = auftragDto.getXInternerkommentar();
				}
				// Kunde holen
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(),
						theClientDto);

				sKunde = kundeDto.getPartnerDto().getCName1nachnamefirmazeile1();

				KundeDto kundeDtoLieferadresse = getKundeFac()
						.kundeFindByPrimaryKey(auftragDto.getKundeIIdLieferadresse(), theClientDto);
				sKundeLieferadresse = kundeDtoLieferadresse.getPartnerDto().getCName1nachnamefirmazeile1();

				sAbteilung = kundeDto.getPartnerDto().getCName3vorname2abteilung();

				if (losDto.getAuftragpositionIId() != null) {
					AuftragpositionDto aufposDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(losDto.getAuftragpositionIId());
					if (aufposDto.getTUebersteuerbarerLiefertermin() != null) {
						dLiefertermin = aufposDto.getTUebersteuerbarerLiefertermin();
					} else {
						dLiefertermin = auftragDto.getDLiefertermin();
					}
				} else {
					dLiefertermin = auftragDto.getDLiefertermin();
				}
			} else {
				sAuftragsnummer = "";

				if (losDto.getKundeIId() != null) {
					KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(losDto.getKundeIId(), theClientDto);

					sKunde = kundeDto.getPartnerDto().getCName1nachnamefirmazeile1();
				} else {
					sKunde = "";
				}

				sKundeLieferadresse = "";
				dLiefertermin = null;
				sLieferart = "";
				sSpediteur = "";
			}

			mapParameter.put("P_AUFTRAGNUMMER", sAuftragsnummer);
			mapParameter.put("P_AUFTRAG_INTERNERKOMMENTAR", sInternerKommentar);
			mapParameter.put("P_AUFTRAG_KUNDE_ABTEILUNG", sAbteilung);
			mapParameter.put("P_LIEFERART", sLieferart);
			mapParameter.put("P_SPEDITEUR", sSpediteur);
			mapParameter.put("P_POENALE", bPoenale);
			mapParameter.put("P_ROHS", bRoHs);
			KostenstelleDto kstDto = getSystemFac().kostenstelleFindByPrimaryKey(losDto.getKostenstelleIId());
			mapParameter.put("P_KOSTENSTELLENUMMER", kstDto.getCNr());
			mapParameter.put("P_KUNDE", sKunde);
			mapParameter.put("P_KUNDE_LIEFERADRESSE", sKundeLieferadresse);
			// PJ20772
			mapParameter.put("P_ABPOSNR", losDto.getCAbposnr());

			// PJ19688
			Integer kundeIIdZugehoerig = getFertigungFac().getZugehoerigenKunden(losDto.getIId(), theClientDto);

			if (kundeIIdZugehoerig != null) {
				mapParameter.put("P_ZUGEHOERIGER_KUNDE", getKundeFac()
						.kundeFindByPrimaryKey(kundeIIdZugehoerig, theClientDto).getPartnerDto().formatFixName1Name2());
			}

			mapParameter.put("P_LOSGROESSE", losDto.getNLosgroesse());

			mapParameter.put("P_LOSKLASSEN", getLosLosKlassenAlsString(losDto.getIId()));

			mapParameter.put("P_LOSNUMMER", losDto.getCNr());
			mapParameter.put("P_PRODUKTIONSBEGINN", losDto.getTProduktionsbeginn());
			mapParameter.put("P_PRODUKTIONSENDE", losDto.getTProduktionsende());
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(losDto.getTProduktionsende());
			mapParameter.put("P_PRODUKTIONSENDE_KW", new Integer(gc.get(Calendar.WEEK_OF_YEAR)));

			mapParameter.put("P_LIEFERTERMIN", dLiefertermin);

			// Fertigungsort
			if (losDto.getPartnerIIdFertigungsort() != null) {
				PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(losDto.getPartnerIIdFertigungsort(),
						theClientDto);
				mapParameter.put("P_FERTIGUNGSORT", partnerDto.formatTitelAnrede());
			}

			// Erstlos
			mapParameter.put("P_ERSTLOS", new Boolean(istErstlos(losDto, theClientDto)));

			boolean bDruckeUeberschriftMaterial = false;
			if (bMitMaterial == true && iSizeMaterial > 0) {
				bDruckeUeberschriftMaterial = true;
			}

			mapParameter.put("P_DRUCKEUEBERSCHRIFTMATERIAL", new Boolean(bDruckeUeberschriftMaterial));

			ArrayList<Object[]> subreportArtikelkommentare = new ArrayList<Object[]>();

			String[] fieldnames = new String[] { "F_KOMMENTARART", "F_MIMETYPE", "F_BILD", "F_KOMMENTAR",
					"F_PDF_OBJECT" };

			int iFeld_Subreport_Kommentarart = 0;
			int iFeld_Subreport_Mimetype = 1;
			int iFeld_Subreport_Bild = 2;
			int iFeld_Subreport_Kommentar = 3;
			int iFeld_Subreport_Pdf = 4;
			int iFeld_Subreport_iAnzahlSpalten = 5;

			String sLosStuecklisteArtikelKommentar = "";
			// Bild einfuegen
			String sMengenEinheit = "";
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stuecklisteDto = getStuecklisteFac()
						.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(), theClientDto);
				// Einheit
				if (stuecklisteDto.getArtikelDto() != null) {
					if (stuecklisteDto.getArtikelDto().getEinheitCNr() != null) {
						sMengenEinheit = stuecklisteDto.getArtikelDto().getEinheitCNr();
					}
				}

				// Sperren
				String sperren = getArtikelFac().getArtikelsperrenText(stuecklisteDto.getArtikelIId());

				mapParameter.put("P_STUECKLISTESPERRSTATUS", sperren);

				// Abmessungen
				GeometrieDto geometrieDto = stuecklisteDto.getArtikelDto().getGeometrieDto();
				if (geometrieDto != null) {
					mapParameter.put(P_ARTIKEL_BREITE, geometrieDto.getFBreite());
					mapParameter.put(P_ARTIKEL_HOEHE, geometrieDto.getFHoehe());
					mapParameter.put(P_ARTIKEL_TIEFE, geometrieDto.getFTiefe());
				}
				// Bauform
				VerpackungDto verpackungDto = stuecklisteDto.getArtikelDto().getVerpackungDto();
				if (verpackungDto != null) {
					mapParameter.put("P_ARTIKEL_BAUFORM", verpackungDto.getCBauform());
					mapParameter.put("P_ARTIKEL_VERPACKUNGSART", verpackungDto.getCVerpackungsart());
				}

				mapParameter.put("P_INDEX", stuecklisteDto.getArtikelDto().getCIndex());
				mapParameter.put("P_REVISION", stuecklisteDto.getArtikelDto().getCRevision());

				ArtikelkommentarDto[] artikelkommentarDto = getArtikelkommentarFac()
						.artikelkommentardruckFindByArtikelIIdBelegartCNr(stuecklisteDto.getArtikelIId(),
								LocaleFac.BELEGART_LOS, theClientDto.getLocUiAsString(), theClientDto);

				if (artikelkommentarDto != null && artikelkommentarDto.length > 0) {

					for (int j = 0; j < artikelkommentarDto.length; j++) {
						if (artikelkommentarDto[j].getArtikelkommentarsprDto() != null) {

							ArtikelkommentarartDto artikelkommentarartDto = getArtikelkommentarFac()
									.artikelkommentarartFindByPrimaryKey(
											artikelkommentarDto[j].getArtikelkommentarartIId(), theClientDto);

							Object[] oZeileVorlage = new Object[iFeld_Subreport_iAnzahlSpalten];
							oZeileVorlage[iFeld_Subreport_Kommentarart] = artikelkommentarartDto.getCNr();

							oZeileVorlage[iFeld_Subreport_Mimetype] = artikelkommentarDto[j].getDatenformatCNr();
							oZeileVorlage[iFeld_Subreport_Kommentar] = artikelkommentarDto[j]
									.getArtikelkommentarsprDto().getXKommentar();

							// Text Kommentar
							if (artikelkommentarDto[j].getDatenformatCNr().trim()
									.indexOf(MediaFac.DATENFORMAT_MIMETYPEART_TEXT) != -1) {
								if (sLosStuecklisteArtikelKommentar == "") {
									sLosStuecklisteArtikelKommentar += artikelkommentarDto[j]
											.getArtikelkommentarsprDto().getXKommentar();
								} else {
									sLosStuecklisteArtikelKommentar += "\n"
											+ artikelkommentarDto[j].getArtikelkommentarsprDto().getXKommentar();
								}

								Object[] oZeile = oZeileVorlage.clone();
								subreportArtikelkommentare.add(oZeile);

							} else if (artikelkommentarDto[j].getDatenformatCNr()
									.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
									|| artikelkommentarDto[j].getDatenformatCNr()
											.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)
									|| artikelkommentarDto[j].getDatenformatCNr()
											.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)) {
								byte[] bild = artikelkommentarDto[j].getArtikelkommentarsprDto().getOMedia();
								if (bild != null) {
									java.awt.image.BufferedImage myImage = Helper.byteArrayToImage(bild);

									Object[] oZeile = oZeileVorlage.clone();
									oZeile[iFeld_Subreport_Bild] = myImage;

									subreportArtikelkommentare.add(oZeile);

								}
							} else if (artikelkommentarDto[j].getDatenformatCNr()
									.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF)) {

								byte[] bild = artikelkommentarDto[j].getArtikelkommentarsprDto().getOMedia();

								java.awt.image.BufferedImage[] tiffs = Helper.tiffToImageArray(bild);
								if (tiffs != null) {
									for (int k = 0; k < tiffs.length; k++) {

										Object[] oZeile = oZeileVorlage.clone();
										oZeile[iFeld_Subreport_Bild] = tiffs[k];

										subreportArtikelkommentare.add(oZeile);

									}
								}

							} else if (artikelkommentarDto[j].getDatenformatCNr()
									.equals(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF)) {
								byte[] pdf = artikelkommentarDto[j].getArtikelkommentarsprDto().getOMedia();
								if (pdf != null) {
									HVPdfReport pdfObject = new HVPdfReport(pdf);
									Object[] oZeile = oZeileVorlage.clone();
									oZeile[iFeld_Subreport_Pdf] = pdfObject;
									subreportArtikelkommentare.add(oZeile);
								}
							}
						}
					}
				}
			}
			mapParameter.put("P_MENGENEINHEIT", sMengenEinheit);
			mapParameter.put("P_PROJEKT", losDto.getCProjekt());
			mapParameter.put("P_LOSKOMMENTAR", losDto.getCKommentar());
			mapParameter.put("P_LOSLANGTEXT", losDto.getXText());

			// SP7124
			if (losDto.getProjektIId() != null || (losDto.getProjektIId() == null && losDto.getAuftragIId() != null)) {
				ProjektDto pjDto = null;
				if (losDto.getProjektIId() != null) {
					pjDto = getProjektFac().projektFindByPrimaryKey(losDto.getProjektIId());
				} else if (losDto.getAuftragIId() != null) {
					AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(losDto.getAuftragIId());
					if (auftragDto.getProjektIId() != null) {
						pjDto = getProjektFac().projektFindByPrimaryKey(auftragDto.getProjektIId());
					}
				}

				if (pjDto != null) {
					mapParameter.put("P_PROJEKTKLAMMER_PROJEKT", pjDto.getCNr());
					BereichDto bereichDto = getProjektServiceFac().bereichFindByPrimaryKey(pjDto.getBereichIId());
					mapParameter.put("P_PROJEKTKLAMMER_BEREICH", bereichDto.getCBez());

					mapParameter.put("P_PROJEKTKLAMMER_TITEL", pjDto.getCTitel());
				}
			}

			FertigungsgruppeDto fertGruppeDto = getStuecklisteFac()
					.fertigungsgruppeFindByPrimaryKey(losDto.getFertigungsgruppeIId());
			mapParameter.put("P_FERTIGUNGSGRUPPE", fertGruppeDto.getCBez());

			// Materialliste?
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stkDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(),
						theClientDto);

				if (sLosStuecklisteArtikelKommentar != "") {
					mapParameter.put("P_STUECKLISTEARTIKELKOMMENTAR",
							Helper.formatStyledTextForJasper(sLosStuecklisteArtikelKommentar));
				}

				mapParameter.put("P_ANZAHL_VERWENDET", getFertigungFac()
						.getAnzahlBisherVerwendet(losDto.getStuecklisteIId(), losDto.getIId(), theClientDto));

				mapParameter.put("P_STUECKLISTEBEZEICHNUNG", stkDto.getArtikelDto().getArtikelsprDto().getCBez());

				mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG",
						stkDto.getArtikelDto().getArtikelsprDto().getCZbez());
				mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG2",
						stkDto.getArtikelDto().getArtikelsprDto().getCZbez2());

				mapParameter.put("P_STUECKLISTEKURZBEZEICHNUNG", stkDto.getArtikelDto().getArtikelsprDto().getCKbez());
				mapParameter.put("P_STUECKLISTEREFERENZNUMMER", stkDto.getArtikelDto().getCReferenznr());

				mapParameter.put("P_STUECKLISTENUMMER", stkDto.getArtikelDto().getCNr());

				mapParameter.put("P_STUECKLISTE_GEWICHTKG", stkDto.getArtikelDto().getFGewichtkg());

				if (stkDto.getArtikelDto().getVerpackungDto() != null) {
					mapParameter.put("P_STUECKLISTE_BAUFORM", stkDto.getArtikelDto().getVerpackungDto().getCBauform());
					mapParameter.put("P_STUECKLISTE_VERPACKUNGSART",
							stkDto.getArtikelDto().getVerpackungDto().getCVerpackungsart());
				}

				if (stkDto.getArtikelDto().getGeometrieDto() != null) {
					mapParameter.put("P_STUECKLISTE_BREITETEXT",
							stkDto.getArtikelDto().getGeometrieDto().getCBreitetext());
					mapParameter.put("P_STUECKLISTE_BREITE", stkDto.getArtikelDto().getGeometrieDto().getFBreite());
					mapParameter.put("P_STUECKLISTE_HOEHE", stkDto.getArtikelDto().getGeometrieDto().getFHoehe());
					mapParameter.put("P_STUECKLISTE_TIEFE", stkDto.getArtikelDto().getGeometrieDto().getFTiefe());
				}

				// Stuecklisteneigenschaften
				StuecklisteeigenschaftDto[] stuecklisteeigenschaftDtos = getStuecklisteFac()
						.stuecklisteeigenschaftFindByStuecklisteIId(losDto.getStuecklisteIId());
				ArrayList<Object[]> al = new ArrayList<Object[]>();
				for (int j = 0; j < stuecklisteeigenschaftDtos.length; j++) {
					StuecklisteeigenschaftDto dto = stuecklisteeigenschaftDtos[j];

					Object[] o = new Object[2];
					String sStklEigenschaftArt = dto.getStuecklisteeigenschaftartDto().getCBez();
					o[0] = sStklEigenschaftArt;
					o[1] = dto.getCBez();
					al.add(o);

					// Index und Materialplatz auch einzeln an Report uebergeben
					if (sStklEigenschaftArt.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_INDEX)) {
						mapParameter.put(P_STUECKLISTENEIGENSCHAFT_INDEX, dto.getCBez());
					}
					if (sStklEigenschaftArt
							.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_MATERIALPLATZ)) {
						mapParameter.put(P_STUECKLISTENEIGENSCHAFT_MATERIALPLATZ, dto.getCBez());
					}

				}

				if (stuecklisteeigenschaftDtos.length > 0) {
					String[] fieldnamesEigenschaften = new String[] { "F_EIGENSCHAFTART", "F_BEZEICHNUNG" };
					Object[][] dataSub = new Object[al.size()][fieldnamesEigenschaften.length];
					dataSub = (Object[][]) al.toArray(dataSub);

					mapParameter.put("DATENSUBREPORT", new LPDatenSubreport(dataSub, fieldnamesEigenschaften));
				}

			} else {
				mapParameter.put("P_STUECKLISTEBEZEICHNUNG", losDto.getCProjekt());
				mapParameter.put("P_STUECKLISTENUMMER",
						getTextRespectUISpr("fert.materialliste", theClientDto.getMandant(), theClientDto.getLocUi()));
			}

			mapParameter.put("P_SCHNELLANLAGE", bStammtVonSchnellanlage);

			Object[][] dataSub = new Object[subreportArtikelkommentare.size()][fieldnames.length];
			dataSub = (Object[][]) subreportArtikelkommentare.toArray(dataSub);

			// SP2699
			mapParameter.put("P_SUBREPORT_ARTIKELKOMMENTAR", new LPDatenSubreport(dataSub, fieldnames));

			// Stueckliste mit Script
			mapParameter.put("P_STUECKLISTESVG", null);
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stkDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(),
						theClientDto);
				if (stkDto.getStuecklisteScriptartIId() != null) {
					String scriptName = stkDto.getStuecklisteScriptartDto().getCScript();
					scriptName = scriptName.replace(".rb", "_graphic.rb");
					ScriptStuecklisteGraphicServiceDto serviceDto = new ScriptStuecklisteGraphicServiceDto();
					serviceDto.setStuecklistepositionenIn(getStuecklisteFac()
							.stuecklistepositionFindByStuecklisteIIdAllData(stkDto.getIId(), theClientDto));
					serviceDto.setStuecklistearbeitsplanIn(getStuecklisteFac()
							.stuecklistearbeitsplanFindByStuecklisteIId(stkDto.getIId(), theClientDto));
					StuecklisteCreateGraphicScript script = new StuecklisteCreateGraphicScript(serviceDto, scriptName,
							getSystemFac(), theClientDto);

					try {
						// String s =
						// "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
						// "<svg xmlns=\"http://www.w3.org/2000/svg\" " +
						// "version=\"1.1\" baseProfile=\"full\" " +
						// "width=\"700px\" height=\"400px\" viewBox=\"0 0 700 400\">"
						// +
						// "<line x1=\"0\" y1=\"200\" x2=\"700\" y2=\"200\" stroke=\"black\"
						// stroke-width=\"20px\"/>"
						// +
						// "<rect x=\"100\" y=\"100\" width=\"500\" height=\"200\" fill=\"white\"
						// stroke=\"black\" stroke-width=\"20px\"/>"
						// +
						// "<line x1=\"180\" y1=\"370\" x2=\"500\" y2=\"50\" stroke=\"black\"
						// stroke-width=\"15px\"/>"
						// +
						// "<polygon points=\"585 0 525 25 585 50\" transform=\"rotate(135 525 25)\"/>"
						// +
						// "</svg>\n";
						String s = script.getSvg();
						if (s != null) {
							mapParameter.put("P_STUECKLISTESVG",
									BatikRenderer.getInstance(new ByteArrayInputStream(s.getBytes())));
						}
					} catch (JRException e) {
						myLogger.error("Ausfuehren von Script '" + scriptName + "'", e);
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_SCRIPT_NICHT_AUSFUEHRBAR, e);
					}
				}
			}

			// Subreport Pruefplan

			if (losDto.getStuecklisteIId() != null) {

				mapParameter.put("P_SUBREPORT_PRUEFPLAN", getSubreportPruefplan(losDto, theClientDto));

			}
			// Formularnummer anhaengen, wenn vorhanden
			String report = FertigungReportFac.REPORT_FERTIGUNGSBEGLEITSCHEIN;

			if (fertGruppeDto.getIFormularnummer() != null) {
				report = report.replace(".", fertGruppeDto.getIFormularnummer() + ".");
			}

			initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, report, theClientDto.getMandant(),
					theClientDto.getLocUi(), theClientDto);

			JasperPrintLP print = getReportPrint();

			if (print != null) {
				PrintInfoDto values = new PrintInfoDto();
				values.setDocPath(new DocPath(new DocNodeLos(losDto)));
				print.setOInfoForArchive(values);
			}
			return print;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} finally {
			closeSession(session);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAlle(ReportJournalKriterienDto krit, boolean bNurAngelegte, Integer fertigungsgruppeIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Session session = null;
		try {
			this.useCase = UC_ALLE;
			this.index = -1;
			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();

			String sQuery = " SELECT l FROM FLRLosReport l " + " LEFT OUTER JOIN l.flrkunde as kunde "
					+ " LEFT OUTER JOIN l.flrauftrag as auftrag "
					+ " LEFT OUTER JOIN l.flrauftragposition as auftragposition "
					+ " LEFT OUTER JOIN l.flrauftragposition.flrauftrag as auftragauspos";

			sQuery += " WHERE l.mandant_c_nr='" + theClientDto.getMandant() + "' ";

			if (krit.kundeIId != null) {
				sQuery += " AND (kunde.i_id=" + krit.kundeIId + " OR auftrag.kunde_i_id_auftragsadresse="
						+ krit.kundeIId + " OR auftragauspos.kunde_i_id_auftragsadresse=" + krit.kundeIId + ")";
			}

			if (krit.kostenstelleIId != null) {

				sQuery += " AND l." + FertigungFac.FLR_LOSREPORT_KOSTENSTELLE_I_ID + "=" + krit.kostenstelleIId;
			}
			if (bNurAngelegte) {
				sQuery += " AND l." + FertigungFac.FLR_LOS_STATUS_C_NR + "='" + LocaleFac.STATUS_ANGELEGT + "' ";
			}
			if (fertigungsgruppeIId != null) {
				sQuery += " AND l." + FertigungFac.FLR_LOSREPORT_FERTIGUNGSGRUPPE_I_ID + "=" + fertigungsgruppeIId;

				FertigungsgruppeDto fertigungsgruppeDto = getStuecklisteFac()
						.fertigungsgruppeFindByPrimaryKey(fertigungsgruppeIId);
				mapParameter.put("P_FERTIGUNGSGRUPPE", fertigungsgruppeDto.getCBez());
			}

			String sVon = null;
			String sBis = null;
			if (krit.dVon != null) {

				sQuery += " AND l." + FertigungFac.FLR_LOS_T_PRODUKTIONSBEGINN + ">='"
						+ Helper.formatDateWithSlashes(krit.dVon) + "' ";

				sVon = Helper.formatDatum(krit.dVon, theClientDto.getLocUi());
			}
			if (krit.dBis != null) {

				sQuery += " AND l." + FertigungFac.FLR_LOS_T_PRODUKTIONSBEGINN + "<='"
						+ Helper.formatDateWithSlashes(krit.dBis) + "' ";

				sBis = Helper.formatDatum(krit.dBis, theClientDto.getLocUi());
			}
			LpBelegnummerFormat f = getBelegnummerGeneratorObj().getBelegnummernFormat(theClientDto.getMandant());
			Integer iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(theClientDto.getMandant());
			String sMandantKuerzel = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_BELEGNUMMER_MANDANTKENNUNG).getCWert();
			if (krit.sBelegnummerVon != null) {
				sVon = HelperServer.getBelegnummernFilterForHibernateCriterias(f, iGeschaeftsjahr, sMandantKuerzel,
						krit.sBelegnummerVon);

				sQuery += " AND l." + FertigungFac.FLR_LOS_C_NR + ">='" + sVon + "' ";

			}
			if (krit.sBelegnummerBis != null) {
				sBis = HelperServer.getBelegnummernFilterForHibernateCriterias(f, iGeschaeftsjahr, sMandantKuerzel,
						krit.sBelegnummerBis);

				sQuery += " AND l." + FertigungFac.FLR_LOS_C_NR + "<='" + sBis + "' ";

			}

			if (krit.bSortiereNachKostenstelle) {
				sQuery += " ORDER BY l." + FertigungFac.FLR_LOSREPORT_FLRKOSTENSTELLE + ".c_nr ASC ";
				if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_BELEGNUMMER) {

					sQuery += " , l.c_nr ASC";

				}
			} else {
				sQuery += " ORDER BY l.c_nr ASC";
			}

			org.hibernate.Query q = session.createQuery(sQuery);

			List<Object> list = q.list();

			data = new Object[list.size()][ALLE_FELDANZAHL];
			int i = 0;
			for (Iterator<?> iter = list.iterator(); iter.hasNext(); i++) {
				FLRLosReport los = (FLRLosReport) iter.next();

				// SP4279
				String kunde = "";

				if (los.getFlrkunde() == null) {

					if (los.getFlrauftrag() != null) {
						kunde = los.getFlrauftrag().getFlrkunde().getFlrpartner().getC_name1nachnamefirmazeile1();

					} else if (los.getFlrauftragposition() != null) {

						kunde = los.getFlrauftragposition().getFlrauftrag().getFlrkunde().getFlrpartner()
								.getC_name1nachnamefirmazeile1();

					}

				} else {

					kunde = los.getFlrkunde().getFlrpartner().getC_name1nachnamefirmazeile1();

				}

				data[i][ALLE_KUNDE] = kunde;

				if (los.getFlrauftrag() != null) {
					data[i][ALLE_AUFTRAGLIEFERTERMIN] = los.getFlrauftrag().getT_liefertermin();
					data[i][ALLE_AUFTRAGSNUMMER] = los.getFlrauftrag().getC_nr();

				} else if (los.getFlrauftragposition() != null) {
					data[i][ALLE_AUFTRAGLIEFERTERMIN] = los.getFlrauftragposition().getFlrauftrag().getT_liefertermin();
					data[i][ALLE_AUFTRAGSNUMMER] = los.getFlrauftragposition().getFlrauftrag().getC_nr();

				} else {
					data[i][ALLE_AUFTRAGSNUMMER] = "";
					data[i][ALLE_AUFTRAGLIEFERTERMIN] = null;
				}

				data[i][ALLE_STATUS] = los.getStatus_c_nr();
				data[i][ALLE_ENDETERMIN] = los.getT_produktionsende();

				BigDecimal bdAbgeliefert = new BigDecimal(0);
				for (Iterator<?> iter2 = los.getAblieferungset().iterator(); iter2.hasNext();) {
					FLRLosablieferung item = (FLRLosablieferung) iter2.next();
					bdAbgeliefert = bdAbgeliefert.add(item.getN_menge());
				}

				data[i][ALLE_ABGELIEFERT] = bdAbgeliefert;

				LossollarbeitsplanDto[] sollaz = getFertigungFac().lossollarbeitsplanFindByLosIId(los.getI_id());
				BigDecimal bdArbeitszeitIstmenge = new BigDecimal(0);
				BigDecimal bdArbeitszeitSollmenge = new BigDecimal(0);
				BigDecimal bdArbeitszeitIstpreis = new BigDecimal(0);
				BigDecimal bdArbeitszeitSollpreis = new BigDecimal(0);
				for (int j = 0; j < sollaz.length; j++) {
					bdArbeitszeitSollmenge = bdArbeitszeitSollmenge.add(sollaz[j].getNGesamtzeit());
					ArtikellieferantDto artikellieferantDto = getArtikelFac()
							.getArtikelEinkaufspreisDesBevorzugtenLieferanten(sollaz[j].getArtikelIIdTaetigkeit(),
									new BigDecimal(1), theClientDto.getSMandantenwaehrung(), theClientDto);
					BigDecimal bdEinzelpreis;
					if (artikellieferantDto != null) {
						bdEinzelpreis = artikellieferantDto.getNEinzelpreis();
					} else {
						bdEinzelpreis = new BigDecimal(0);
					}
					bdArbeitszeitSollpreis = bdArbeitszeitSollpreis
							.add(sollaz[j].getNGesamtzeit().multiply(bdEinzelpreis));
				}
				AuftragzeitenDto[] zeiten = getZeiterfassungFac().getAllZeitenEinesBeleges(LocaleFac.BELEGART_LOS,
						los.getI_id(), null, null, null, null, ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ARTIKEL,
						theClientDto);
				for (int j = 0; j < zeiten.length; j++) {
					bdArbeitszeitIstmenge = bdArbeitszeitIstmenge
							.add(new BigDecimal(zeiten[j].getDdDauer().doubleValue()));
					bdArbeitszeitIstpreis = bdArbeitszeitIstpreis.add(zeiten[j].getBdKosten());
				}
				data[i][ALLE_ARBEITSZEITISTMENGE] = bdArbeitszeitIstmenge;
				data[i][ALLE_ARBEITSZEITISTPREIS] = bdArbeitszeitIstpreis;
				data[i][ALLE_ARBEITSZEITSOLLMENGE] = bdArbeitszeitSollmenge;
				data[i][ALLE_ARBEITSZEITSOLLPREIS] = bdArbeitszeitSollpreis;
				if (los.getFlrstueckliste() != null) {
					ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
							los.getFlrstueckliste().getFlrartikel().getI_id(), theClientDto);
					data[i][ALLE_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
					if (artikelDto.getArtikelsprDto().getCZbez() != null) {
						data[i][ALLE_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCZbez();
					} else {
						data[i][ALLE_ZUSATZBEZEICHNUNG] = "";
					}
					data[i][ALLE_IDENT] = los.getFlrstueckliste().getFlrartikel().getC_nr();
					data[i][ALLE_OFFENERAHMENRESERVIERUNGEN] = getReservierungFac()
							.getAnzahlRahmenreservierungen(artikelDto.getIId(), theClientDto);
				} else {
					data[i][ALLE_BEZEICHNUNG] = los.getC_projekt();
					data[i][ALLE_BEZEICHNUNG] = null;
					data[i][ALLE_IDENT] = getTextRespectUISpr("fert.materialliste", theClientDto.getMandant(),
							theClientDto.getLocUi());
					data[i][ALLE_OFFENERAHMENRESERVIERUNGEN] = new BigDecimal(0);
				}
				data[i][ALLE_LOSNUMMER] = los.getC_nr();
				LossollmaterialDto[] sollmat = getFertigungFac().lossollmaterialFindByLosIId(los.getI_id());
				BigDecimal bdMaterialIstmenge = new BigDecimal(0);
				BigDecimal bdMaterialSollmenge = new BigDecimal(0);
				BigDecimal bdMaterialIstpreis = new BigDecimal(0);
				BigDecimal bdMaterialSollpreis = new BigDecimal(0);
				for (int j = 0; j < sollmat.length; j++) {
					BigDecimal bdAusgegebeneMenge = getFertigungFac().getAusgegebeneMenge(sollmat[j].getIId(), null,
							theClientDto);
					bdMaterialIstmenge = bdMaterialIstmenge.add(bdAusgegebeneMenge);
					bdMaterialSollmenge = bdMaterialSollmenge.add(sollmat[j].getNMenge());
					bdMaterialIstpreis = bdMaterialIstpreis.add(bdAusgegebeneMenge.multiply(
							getFertigungFac().getAusgegebeneMengePreis(sollmat[j].getIId(), null, theClientDto)));
					bdMaterialSollpreis = bdMaterialSollpreis
							.add(sollmat[j].getNMenge().multiply(sollmat[j].getNSollpreis()));
				}
				data[i][ALLE_MATERIALISTMENGE] = bdMaterialIstmenge;
				data[i][ALLE_MATERIALISTPREIS] = bdMaterialIstpreis;
				data[i][ALLE_MATERIALSOLLMENGE] = bdMaterialSollmenge;
				data[i][ALLE_MATERIALSOLLPREIS] = bdMaterialSollpreis;
				data[i][ALLE_BEGINNTERMIN] = los.getT_produktionsbeginn();
				data[i][ALLE_ENDETERMIN] = los.getT_produktionsende();

				data[i][ALLE_LOSGROESSE] = los.getN_losgroesse();

				if (los.getFlrfertigungsgruppe() != null) {
					data[i][ALLE_FERTIGUNGSGRUPPE] = los.getFlrfertigungsgruppe().getC_bez();
				} else {
					data[i][ALLE_FERTIGUNGSGRUPPE] = "";
				}
			}

			// Sortieren
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				for (int m = data.length - 1; m > 0; --m) {
					for (int j = 0; j < m; ++j) {
						Object[] o = data[j];
						Object[] o1 = data[j + 1];

						String s1 = (String) o[ALLE_KUNDE];
						String s2 = (String) o1[ALLE_KUNDE];

						if (s1.compareTo(s2) > 0) {
							data[j] = o1;
							data[j + 1] = o;
						}
					}
				}
			}

			StringBuffer sSortierung = new StringBuffer();
			// Sortierung nach Kostenstelle
			sSortierung
					.append(getTextRespectUISpr("lp.sortierungnach", theClientDto.getMandant(), theClientDto.getLocUi())
							+ ": ");
			mapParameter.put(LPReport.P_SORTIERENACHKOSTENSTELLE, new Boolean(krit.bSortiereNachKostenstelle));
			if (krit.bSortiereNachKostenstelle) {
				sSortierung.append(
						getTextRespectUISpr("lp.kostenstelle", theClientDto.getMandant(), theClientDto.getLocUi()));
				sSortierung.append(", ");
			}
			// Sortieung nach Kunde
			mapParameter.put(LPReport.P_SORTIERENACHKUNDE,
					new Boolean(krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER));
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				sSortierung.append(getTextRespectUISpr("lp.kunde", theClientDto.getMandant(), theClientDto.getLocUi()));
			} else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_BELEGNUMMER) {
				sSortierung.append(getTextRespectUISpr("lp.losnr", theClientDto.getMandant(), theClientDto.getLocUi()));
			}
			StringBuffer sFilter = new StringBuffer();
			sFilter.append(getTextRespectUISpr("lp.filter", theClientDto.getMandant(), theClientDto.getLocUi()) + ": ");
			if (bNurAngelegte) {
				sFilter.append(
						getTextRespectUISpr("lp.nichtausgegebene", theClientDto.getMandant(), theClientDto.getLocUi())
								+ ", ");
			}
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
			mapParameter.put(LPReport.P_SORTIERUNG, sSortierung.toString());
			mapParameter.put("P_VON", krit.dVon);
			mapParameter.put("P_BIS", krit.dBis);
			mapParameter.put(LPReport.P_FILTER, sFilter.toString());
			initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_ALLE,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
			return getReportPrint();
		} catch (RemoteException t) {
			throwEJBExceptionLPRespectOld(t);
			return null;
		} finally {
			closeSession(session);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printOffene(java.sql.Date dStichtag, int iOptionStichtag, String belegNrVon, String belegNrBis,
			Integer kundeIId, Integer kostenstelleIId, Integer fertigungsgruppeIId, int iSortierung,
			boolean bNurForecast, TheClientDto theClientDto) throws EJBExceptionLP {
		Session session = null;

		try {
			this.useCase = UC_OFFENE;
			this.index = -1;
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRLosReport.class);
			c.add(Restrictions.eq(FertigungFac.FLR_LOS_MANDANT_C_NR, theClientDto.getMandant()));
			Map<String, Object> mapParameter = new TreeMap<String, Object>();

			mapParameter.put("P_NUR_FORECAST", new Boolean(bNurForecast));
			if (bNurForecast == true) {
				c.add(Restrictions.isNotNull("forecastposition_i_id"));
			}

			if (kostenstelleIId != null) {
				KostenstelleDto kostenstelleDto = getSystemFac().kostenstelleFindByPrimaryKey(kostenstelleIId);
				mapParameter.put("P_KOSTENSTELLE", kostenstelleDto.formatKostenstellenbezeichnung());

				c.add(Restrictions.eq(FertigungFac.FLR_LOSREPORT_KOSTENSTELLE_I_ID, kostenstelleIId));
			}
			boolean flrAuftragSchonVerwendet = false;
			if (kundeIId != null) {
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto);
				mapParameter.put("P_KUNDE", kundeDto.getPartnerDto().formatFixTitelName1Name2());
				c.createAlias(FertigungFac.FLR_LOSREPORT_FLRAUFTRAG, "a", CriteriaSpecification.LEFT_JOIN);
				flrAuftragSchonVerwendet = true;
				c.add(Restrictions.eq("a." + AuftragFac.FLR_AUFTRAG_KUNDE_I_ID_AUFTRAGSADRESSE, kundeIId));
			}
			if (fertigungsgruppeIId != null) {
				c.add(Restrictions.eq(FertigungFac.FLR_LOSREPORT_FERTIGUNGSGRUPPE_I_ID, fertigungsgruppeIId));

				FertigungsgruppeDto fertDto = getStuecklisteFac().fertigungsgruppeFindByPrimaryKey(fertigungsgruppeIId);
				mapParameter.put("P_FERTIGUNGSGRUPPE", fertDto.getCBez());
			}

			String[] stati = new String[4];
			stati[0] = LocaleFac.STATUS_AUSGEGEBEN;
			stati[1] = LocaleFac.STATUS_IN_PRODUKTION;
			stati[2] = LocaleFac.STATUS_TEILERLEDIGT;
			stati[3] = LocaleFac.STATUS_ANGELEGT;

			c.add(Restrictions.in(FertigungFac.FLR_LOS_STATUS_C_NR, stati));

			if (dStichtag != null) {

				dStichtag = Helper.cutDate(dStichtag);

				// PJ 14420
				mapParameter.put("P_STICHTAG", dStichtag);

				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(dStichtag.getTime());
				cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 1);

				dStichtag = new java.sql.Date(cal.getTimeInMillis());

				String datumsart = "";

				if (iOptionStichtag == FertigungReportFac.OFFENE_OPTION_STICHTAG_BEGINNDATUM) {
					c.add(Restrictions.lt(FertigungFac.FLR_LOS_T_PRODUKTIONSBEGINN, dStichtag));

					datumsart = getTextRespectUISpr("lp.begintermin", theClientDto.getMandant(),
							theClientDto.getLocUi());

				} else if (iOptionStichtag == FertigungReportFac.OFFENE_OPTION_STICHTAG_ENDEDATUM) {
					c.add(Restrictions.lt(FertigungFac.FLR_LOS_T_PRODUKTIONSENDE, dStichtag));
					datumsart = getTextRespectUISpr("lp.endetermin", theClientDto.getMandant(),
							theClientDto.getLocUi());

				} else if (iOptionStichtag == FertigungReportFac.OFFENE_OPTION_STICHTAG_LIEFERTERMIN) {
					if (flrAuftragSchonVerwendet == false) {
						c.createAlias(FertigungFac.FLR_LOSREPORT_FLRAUFTRAG, "a", CriteriaSpecification.LEFT_JOIN);
						flrAuftragSchonVerwendet = true;
						c.add(Restrictions.lt("a." + AuftragFac.FLR_AUFTRAG_T_LIEFERTERMIN, dStichtag));

					}
					datumsart = getTextRespectUISpr("bes.liefertermin", theClientDto.getMandant(),
							theClientDto.getLocUi());

				}

				mapParameter.put("P_DATUMSART", datumsart);

			}
			LpBelegnummerFormat f = getBelegnummerGeneratorObj().getBelegnummernFormat(theClientDto.getMandant());
			Integer iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(theClientDto.getMandant());
			String sMandantKuerzel = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_BELEGNUMMER_MANDANTKENNUNG).getCWert();
			if (belegNrVon != null) {
				String sVon = HelperServer.getBelegnummernFilterForHibernateCriterias(f, iGeschaeftsjahr,
						sMandantKuerzel, belegNrVon);
				c.add(Restrictions.ge(FertigungFac.FLR_LOS_C_NR, sVon));
				mapParameter.put("P_LOSNRVON", sVon);
			}
			if (belegNrBis != null) {
				String sBis = HelperServer.getBelegnummernFilterForHibernateCriterias(f, iGeschaeftsjahr,
						sMandantKuerzel, belegNrBis);
				c.add(Restrictions.le(FertigungFac.FLR_LOS_C_NR, sBis));
				mapParameter.put("P_LOSNRBIS", sBis);
			}

			// Sortierung
			if (iSortierung == FertigungReportFac.OFFENE_OPTION_SORTIERUNG_KOSTENSTELLE) {
				c.createCriteria(FertigungFac.FLR_LOSREPORT_FLRKOSTENSTELLE).addOrder(Order.asc("c_nr"));
				mapParameter.put(P_SORTIERUNG,
						getTextRespectUISpr("lp.kostenstelle", theClientDto.getMandant(), theClientDto.getLocUi()));
			} else if (iSortierung == FertigungReportFac.OFFENE_OPTION_SORTIERUNG_KUNDE) {
				if (flrAuftragSchonVerwendet == false) {
					c.createAlias(FertigungFac.FLR_LOSREPORT_FLRAUFTRAG, "a", CriteriaSpecification.LEFT_JOIN);
					flrAuftragSchonVerwendet = true;
				}
				c.createAlias("a." + AuftragFac.FLR_AUFTRAG_FLRKUNDE, "k", CriteriaSpecification.LEFT_JOIN);
				c.createAlias("k." + KundeFac.FLR_PARTNER, "p", CriteriaSpecification.LEFT_JOIN);

				c.addOrder(Order.asc("p." + PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1));
				c.addOrder(Order.asc("k.i_id"));

				mapParameter.put(P_SORTIERUNG,
						getTextRespectUISpr("lp.kunde", theClientDto.getMandant(), theClientDto.getLocUi()));
			} else if (iSortierung == FertigungReportFac.OFFENE_OPTION_SORTIERUNG_KUNDEUNDGEWAEHLTERTERMIN) {
				if (flrAuftragSchonVerwendet == false) {
					c.createAlias(FertigungFac.FLR_LOSREPORT_FLRAUFTRAG, "a", CriteriaSpecification.LEFT_JOIN);
					flrAuftragSchonVerwendet = true;
				}
				c.createAlias("a." + AuftragFac.FLR_AUFTRAG_FLRKUNDE, "k", CriteriaSpecification.LEFT_JOIN);
				c.createAlias("k." + KundeFac.FLR_PARTNER, "p", CriteriaSpecification.LEFT_JOIN);

				c.addOrder(Order.asc("p." + PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1));

				if (iOptionStichtag == FertigungReportFac.OFFENE_OPTION_STICHTAG_BEGINNDATUM) {
					c.addOrder(Order.asc(FertigungFac.FLR_LOS_T_PRODUKTIONSBEGINN));
				} else if (iOptionStichtag == FertigungReportFac.OFFENE_OPTION_STICHTAG_ENDEDATUM) {
					c.addOrder(Order.asc(FertigungFac.FLR_LOS_T_PRODUKTIONSENDE));
				} else if (iOptionStichtag == FertigungReportFac.OFFENE_OPTION_STICHTAG_LIEFERTERMIN) {
					c.addOrder(Order.asc("a." + AuftragFac.FLR_AUFTRAG_T_LIEFERTERMIN));
				}

				mapParameter.put(P_SORTIERUNG,
						getTextRespectUISpr("fert.kundeundtermin", theClientDto.getMandant(), theClientDto.getLocUi()));
			} else if (iSortierung == FertigungReportFac.OFFENE_OPTION_SORTIERUNG_LIEFERTERMIN) {
				if (flrAuftragSchonVerwendet == false) {
					c.createAlias(FertigungFac.FLR_LOSREPORT_FLRAUFTRAG, "a", CriteriaSpecification.LEFT_JOIN);
					flrAuftragSchonVerwendet = true;
				}
				c.createAlias("a." + AuftragFac.FLR_AUFTRAG_FLRKUNDE, "k", CriteriaSpecification.LEFT_JOIN);
				c.createAlias("k." + KundeFac.FLR_PARTNER, "p", CriteriaSpecification.LEFT_JOIN);

				c.addOrder(Order.asc("a." + AuftragFac.FLR_AUFTRAG_T_LIEFERTERMIN));
				c.addOrder(Order.asc("p." + PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1));

				mapParameter.put(P_SORTIERUNG,
						getTextRespectUISpr("fert.liefertermin", theClientDto.getMandant(), theClientDto.getLocUi()));
			} else if (iSortierung == FertigungReportFac.OFFENE_OPTION_SORTIERUNG_ARTIKEL) {
				c.createAlias(FertigungFac.FLR_LOS_FLRSTUECKLISTE, "s");
				c.createAlias("s." + StuecklisteFac.FLR_STUECKLISTE_FLRARTIKEL, "art");
				c.addOrder(Order.asc("art.c_nr"));
				mapParameter.put(P_SORTIERUNG, getTextRespectUISpr("artikel.artikelnummerlang",
						theClientDto.getMandant(), theClientDto.getLocUi()));
			} else if (iSortierung == FertigungReportFac.OFFENE_OPTION_SORTIERUNG_FERTIGUNGSGRUPPE) {
				c.createAlias(FertigungFac.FLR_LOSREPORT_FLRFERTIGUNGSGRUPPE, "f");
				c.addOrder(Order.asc("f.c_bez"));
				mapParameter.put(P_SORTIERUNG,
						getTextRespectUISpr("lp.fertigungsgruppe", theClientDto.getMandant(), theClientDto.getLocUi()));
			} else if (iSortierung == FertigungReportFac.OFFENE_OPTION_SORTIERUNG_BEGINN) {
				c.addOrder(Order.asc(FertigungFac.FLR_LOS_T_PRODUKTIONSBEGINN));
				mapParameter.put(P_SORTIERUNG,
						getTextRespectUISpr("lp.beginn", theClientDto.getMandant(), theClientDto.getLocUi()));
			} else if (iSortierung == FertigungReportFac.OFFENE_OPTION_SORTIERUNG_ENDE) {
				c.addOrder(Order.asc(FertigungFac.FLR_LOS_T_PRODUKTIONSENDE));
				mapParameter.put(P_SORTIERUNG,
						getTextRespectUISpr("lp.ende", theClientDto.getMandant(), theClientDto.getLocUi()));
			} else {
				mapParameter.put(P_SORTIERUNG,
						getTextRespectUISpr("lp.losnr", theClientDto.getMandant(), theClientDto.getLocUi()));
			}

			// 2tes sortierkriterium immer Losnr

			if (iSortierung == FertigungReportFac.OFFENE_OPTION_SORTIERUNG_BEGINN
					|| iSortierung == FertigungReportFac.OFFENE_OPTION_SORTIERUNG_ENDE
					|| iSortierung == FertigungReportFac.OFFENE_OPTION_SORTIERUNG_LIEFERTERMIN
					|| iSortierung == FertigungReportFac.OFFENE_OPTION_SORTIERUNG_KUNDEUNDGEWAEHLTERTERMIN) {
				if (flrAuftragSchonVerwendet == false) {
					c.createAlias(FertigungFac.FLR_LOSREPORT_FLRAUFTRAG, "a", CriteriaSpecification.LEFT_JOIN);
					flrAuftragSchonVerwendet = true;
				}
				c.addOrder(Order.asc("a.c_nr"));
			} else {

				c.addOrder(Order.asc(FertigungFac.FLR_LOS_C_NR));
			}

			List<?> list = c.list();
			data = new Object[list.size()][OFFENE_SPALTENANZAHL];
			int i = 0;
			for (Iterator<?> iter = list.iterator(); iter.hasNext(); i++) {
				FLRLosReport los = (FLRLosReport) iter.next();

				if (los.getFlrauftrag() != null) {

					Integer partnerIId = los.getFlrauftrag().getFlrkunde().getFlrpartner().getI_id();
					PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(partnerIId, theClientDto);
					data[i][OFFENE_KUNDE] = partnerDto.formatFixTitelName1Name2();
					data[i][OFFENE_KUNDE_KBEZ] = partnerDto.getCKbez();
					data[i][OFFENE_AUFTRAGSPOENALE] = los.getFlrauftrag().getB_poenale();
				}

				if (los.getFlrauftrag() != null) {
					data[i][OFFENE_AUFTRAGSNUMMER] = los.getFlrauftrag().getC_nr();
					data[i][OFFENE_AUFTRAGSSTATUS] = los.getFlrauftrag().getAuftragstatus_c_nr();
					data[i][OFFENE_PROJEKT] = los.getFlrauftrag().getC_bez();
					data[i][OFFENE_LIEFERTERMIN] = los.getFlrauftrag().getT_liefertermin();
				}
				BigDecimal bdGeliefert = new BigDecimal(0);
				for (Iterator<?> iter2 = los.getAblieferungset().iterator(); iter2.hasNext();) {
					FLRLosablieferung item = (FLRLosablieferung) iter2.next();
					bdGeliefert = bdGeliefert.add(item.getN_menge());
				}
				data[i][OFFENE_GELIEFERT] = bdGeliefert;
				if (los.getFlrstueckliste() != null) {
					ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
							los.getFlrstueckliste().getFlrartikel().getI_id(), theClientDto);
					data[i][OFFENE_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
					data[i][OFFENE_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCZbez();
					data[i][OFFENE_ARTIKELNUMMER] = los.getFlrstueckliste().getFlrartikel().getC_nr();

					data[i][OFFENE_DETAILBEDARF] = getRahmenbedarfeFac()
							.getSummeAllerRahmenbedarfeEinesArtikels(artikelDto.getIId());

					// Offene Fehlmengen
					data[i][OFFENE_FEHLMENGE] = getFehlmengeFac().getAnzahlFehlmengeEinesArtikels(artikelDto.getIId(),
							theClientDto);

					LossollarbeitsplanDto[] lossollarbeitsplanDto = getFertigungFac()
							.lossollarbeitsplanFindByLosIId(los.getI_id());

					BigDecimal bdFertigungszeit = new BigDecimal(0);
					for (int j = 0; lossollarbeitsplanDto.length > j; j++) {
						bdFertigungszeit = bdFertigungszeit.add(lossollarbeitsplanDto[j].getNGesamtzeit());
					}
					data[i][OFFENE_FERTIGUNGSZEIT] = bdFertigungszeit;

					// Rahmenbestellt
					Hashtable<?, ?> htRahmenbestellt = getArtikelbestelltFac()
							.getAnzahlRahmenbestellt(artikelDto.getIId(), theClientDto);
					if (htRahmenbestellt.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
						BigDecimal bdAnzahlRahmenbestellt = (BigDecimal) htRahmenbestellt
								.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
						data[i][OFFENE_RAHMEN_BESTELLT] = bdAnzahlRahmenbestellt;
					}
					data[i][OFFENE_RESERVIERUNGEN] = getReservierungFac().getAnzahlReservierungen(artikelDto.getIId(),
							theClientDto);

					data[i][OFFENE_RAHMENRESERVIERUNGEN] = getReservierungFac()
							.getAnzahlRahmenreservierungen(artikelDto.getIId(), theClientDto);

					// PJ19893
					data[i][OFFENE_IN_FERTIGUNG] = getFertigungFac().getAnzahlInFertigung(artikelDto.getIId(),
							theClientDto);
					data[i][OFFENE_BESTELLT] = getArtikelbestelltFac().getAnzahlBestellt(artikelDto.getIId());
					data[i][OFFENE_LAGERSTAND_SPERRLAEGER] = getLagerFac()
							.getLagerstandAllerSperrlaegerEinesMandanten(artikelDto.getIId(), theClientDto);
					data[i][OFFENE_LAGERSTAND_MITKONSIGNATIONSLAGER] = getLagerFac()
							.getLagerstandAllerLagerEinesMandanten(artikelDto.getIId(), true, theClientDto);
					data[i][OFFENE_LAGERSTAND_OHNEKONSIGNATIONSLAGER] = getLagerFac()
							.getLagerstandAllerLagerEinesMandanten(artikelDto.getIId(), false, theClientDto);

				} else {
					data[i][OFFENE_BEZEICHNUNG] = los.getC_projekt();
					data[i][OFFENE_ZUSATZBEZEICHNUNG] = null;
					data[i][OFFENE_ARTIKELNUMMER] = getTextRespectUISpr("fert.materialliste", theClientDto.getMandant(),
							theClientDto.getLocUi());
				}

				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(los.getT_produktionsbeginn());
				data[i][OFFENE_KALENDERWOCHE] = new Integer(gc.get(GregorianCalendar.WEEK_OF_YEAR));
				data[i][OFFENE_LOSGROESSE] = los.getN_losgroesse();
				data[i][OFFENE_LOSNUMMER] = los.getC_nr();
				data[i][OFFENE_FERTIGUNGSGRUPPE] = los.getFlrfertigungsgruppe().getC_bez();

				// SP4784
				if (los.getFlrforecastposition() != null) {
					data[i][OFFENE_FORECAST_NUMMER] = los.getFlrforecastposition().getFlrforecastauftrag()
							.getFlrfclieferadresse().getFlrforecast().getC_nr();
					data[i][OFFENE_FORECAST_KUNDE_LIEFERADRESSE_KBEZ] = los.getFlrforecastposition()
							.getFlrforecastauftrag().getFlrfclieferadresse().getFlrkunde_lieferadresse().getFlrpartner()
							.getC_kbez();
					data[i][OFFENE_FORECASTAUFTRAG_ANLAGEDATUM] = los.getFlrforecastposition().getFlrforecastauftrag()
							.getT_anlegen();
					data[i][OFFENE_FORECASTPOSITION_TERMIN] = los.getFlrforecastposition().getT_termin();
				}

				data[i][OFFENE_MATERIAL] = null;
				data[i][OFFENE_BEGINN] = los.getT_produktionsbeginn();
				data[i][OFFENE_ENDE] = los.getT_produktionsende();
				data[i][OFFENE_LOSSTATUS] = los.getStatus_c_nr();

				// PJ 15009

				String queryf = "select sum(flrfehlmenge.n_menge) FROM FLRFehlmenge flrfehlmenge WHERE flrfehlmenge.flrlossollmaterial.flrlos = "
						+ los.getI_id() + " AND flrfehlmenge.n_menge>0";
				Session session2 = FLRSessionFactory.getFactory().openSession();
				Query query = session2.createQuery(queryf);
				List<?> results = query.list();

				data[i][OFFENE_LOSHATFEHLMENGE] = new Boolean(false);
				if (results.size() > 0) {
					BigDecimal bd = (BigDecimal) results.iterator().next();

					if (bd != null && bd.doubleValue() > 0) {
						data[i][OFFENE_LOSHATFEHLMENGE] = new Boolean(true);
					}
				}
				session2.close();

				data[i][OFFENE_LOSKLASSEN] = getLosLosKlassenAlsString(los.getI_id());

			}

			initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_OFFENE,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
			return getReportPrint();
		} catch (Exception t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, t);
		} finally {
			closeSession(session);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printMaschineUndMaterial(Integer maschineIId, Integer maschinengruppeIId,
			DatumsfilterVonBis vonBis, TheClientDto theClientDto) {
		this.useCase = UC_MASCHINEUNDMATERIAL;
		this.index = -1;

		Map<String, Object> mapParameter = new TreeMap<String, Object>();

		mapParameter.put("P_VON", vonBis.getTimestampVon());
		mapParameter.put("P_BIS", vonBis.getTimestampBisUnveraendert());

		ParametermandantDto parameter = null;
		try {
			parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_STUECKLISTE_ARBEITSPLAN_ZEITEINHEIT);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		String sEinheit = parameter.getCWert().trim();

		String sQuery = " SELECT flroffeneags from FLROffeneags flroffeneags  "
				+ "LEFT OUTER JOIN flroffeneags.flrartikel_taetigkeit.artikelsprset AS aspr_taetigkeit   "
				+ "LEFT OUTER JOIN flroffeneags.flrartikel_stueckliste.artikelsprset AS aspr_stueckliste   "
				+ "LEFT OUTER JOIN flroffeneags.flrartikel_sollmaterial.artikelsprset AS aspr_sollmaterial   "
				+ "LEFT OUTER JOIN flroffeneags.flrmaschine AS flrmaschine  "
				+ "LEFT OUTER JOIN flroffeneags.flrartikel_taetigkeit AS taetigkeit "
				+ "LEFT OUTER JOIN flroffeneags.flrartikel_sollmaterial AS sollmaterial  "
				+ "LEFT OUTER JOIN flroffeneags.flrartikel_stueckliste AS stueckliste "
				+ "LEFT OUTER JOIN flroffeneags.flrlossollarbeitsplan AS sollarbeitsplan " + ""
				+ "WHERE flroffeneags.mandant_c_nr = '" + theClientDto.getMandant() + "'";
		if (vonBis.getTimestampVon() != null) {
			sQuery += " AND flroffeneags.t_agbeginn>='" + Helper.formatTimestampWithSlashes(vonBis.getTimestampVon())
					+ "'";
		}

		if (vonBis.getTimestampBis() != null) {
			sQuery += " AND flroffeneags.t_agbeginn<'" + Helper.formatTimestampWithSlashes(vonBis.getTimestampBis())
					+ "'";
		}

		if (maschineIId != null) {
			sQuery += " AND flrmaschine=" + maschineIId + " ";
			mapParameter.put("P_MASCHINE",
					getZeiterfassungFac().maschineFindByPrimaryKey(maschineIId).getBezeichnung());

		}

		if (maschinengruppeIId != null) {
			try {
				sQuery += " AND flrmaschine.maschinengruppe_i_id=" + maschinengruppeIId + " ";
				mapParameter.put("P_MASCHINENGRUPPE",
						getZeiterfassungFac().maschinengruppeFindByPrimaryKey(maschinengruppeIId).getCBez());
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}

		sQuery += " ORDER BY flrmaschine.c_identifikationsnr ASC, sollmaterial.c_nr ASC,"
				+ " flroffeneags.t_agbeginn ASC, flroffeneags.i_maschinenversatz_ms ASC";

		Session session = FLRSessionFactory.getFactory().openSession();

		// SP5549
		session.enableFilter("filterLocale").setParameter("paramLocale", theClientDto.getLocUiAsString());

		org.hibernate.Query hquery = session.createQuery(sQuery);
		List<?> resultList = hquery.list();
		Iterator<?> resultListIterator = resultList.iterator();
		int i = 0;
		ArrayList alDaten = new ArrayList();

		while (resultListIterator.hasNext()) {
			FLROffeneags flrOffeneags = (FLROffeneags) resultListIterator.next();

			Object[] oZeile = new Object[MASCHINEUNDMATERIAL_SPALTENANZAHL];

			// AG

			LossollarbeitsplanDto saDtoOriginal = getFertigungFac()
					.lossollarbeitsplanFindByPrimaryKey(flrOffeneags.getI_id());

			oZeile[MASCHINEUNDMATERIAL_AG_ART] = flrOffeneags.getFlrlossollarbeitsplan().getAgart_c_nr();
			oZeile[MASCHINEUNDMATERIAL_FORTSCHRITT] = flrOffeneags.getFlrlossollarbeitsplan().getF_fortschritt();
			oZeile[MASCHINEUNDMATERIAL_AG_BEGINN] = flrOffeneags.getT_agbeginn();
			oZeile[MASCHINEUNDMATERIAL_ARBEITSGANG] = flrOffeneags.getFlrlossollarbeitsplan().getI_arbeitsgangsnummer();

			if (flrOffeneags.getFlrlossollarbeitsplan().getFlrsollmaterial() != null) {
				oZeile[MASCHINEUNDMATERIAL_ARTIKELNUMMER_MATERIAL] = flrOffeneags.getFlrlossollarbeitsplan()
						.getFlrsollmaterial().getFlrartikel().getC_nr();

				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
						flrOffeneags.getFlrlossollarbeitsplan().getFlrsollmaterial().getFlrartikel().getI_id(),
						theClientDto);

				oZeile[MASCHINEUNDMATERIAL_ARTIKELBEZEICHNUNG_MATERIAL] = aDto.formatBezeichnung();

				try {
					oZeile[MASCHINEUNDMATERIAL_OFFENE_MENGE_MATERIAL] = flrOffeneags.getFlrlossollarbeitsplan()
							.getFlrsollmaterial().getN_menge()
							.subtract(getFertigungFac().getAusgegebeneMenge(
									flrOffeneags.getFlrlossollarbeitsplan().getFlrsollmaterial().getI_id(), null,
									theClientDto));

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}

			oZeile[MASCHINEUNDMATERIAL_ARTIKELNUMMER_TAETIGKEIT] = flrOffeneags.getFlrlossollarbeitsplan()
					.getFlrartikel().getC_nr();

			ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
					flrOffeneags.getFlrlossollarbeitsplan().getFlrartikel().getI_id(), theClientDto);

			oZeile[MASCHINEUNDMATERIAL_ARTIKELBEZEICHNUNG_TAETIGKEIT] = aDto.formatBezeichnung();

			oZeile[MASCHINEUNDMATERIAL_AUFSPANNUNG] = flrOffeneags.getFlrlossollarbeitsplan().getI_aufspannung();
			oZeile[MASCHINEUNDMATERIAL_AUTO_STOP_BEI_GEHT] = Helper.short2Boolean(saDtoOriginal.getBAutoendebeigeht());

			oZeile[MASCHINEUNDMATERIAL_KOMMENTAR] = saDtoOriginal.getCKomentar();
			oZeile[MASCHINEUNDMATERIAL_KOMMENTAR_LANG] = saDtoOriginal.getXText();
			oZeile[MASCHINEUNDMATERIAL_LOSNUMMER] = flrOffeneags.getFlrlos().getC_nr();
			oZeile[MASCHINEUNDMATERIAL_LOSGROESSE] = flrOffeneags.getFlrlos().getN_losgroesse();

			oZeile[MASCHINEUNDMATERIAL_ZUSATZSTATUS_P1] = getFertigungFac()
					.hatLosZusatzstatusP1(flrOffeneags.getFlrlos().getI_id());

			if (flrOffeneags.getFlrkunde() != null) {

				oZeile[MASCHINEUNDMATERIAL_KUNDE_KBEZ] = flrOffeneags.getFlrkunde().getFlrpartner().getC_kbez();
			}

			if (flrOffeneags.getFlrlossollarbeitsplan().getFlrmaschine() != null) {
				oZeile[MASCHINEUNDMATERIAL_MASCHINE_BEZEICHNUNG] = flrOffeneags.getFlrlossollarbeitsplan()
						.getFlrmaschine().getC_bez();
				oZeile[MASCHINEUNDMATERIAL_MASCHINE_INVENTARNUMMMER] = flrOffeneags.getFlrlossollarbeitsplan()
						.getFlrmaschine().getC_inventarnummer();
				oZeile[MASCHINEUNDMATERIAL_MASCHINE_IDENTIFIKATIONSNUMMMER] = flrOffeneags.getFlrlossollarbeitsplan()
						.getFlrmaschine().getC_identifikationsnr();
				oZeile[MASCHINEUNDMATERIAL_MASCHINENGRUPPE] = flrOffeneags.getFlrlossollarbeitsplan().getFlrmaschine()
						.getFlrmaschinengruppe().getC_bez();
			}

			oZeile[MASCHINEUNDMATERIAL_MASCHINENVERSATZ_MS] = saDtoOriginal.getIMaschinenversatzMs();
			oZeile[MASCHINEUNDMATERIAL_NUR_MASCHINENZEIT] = Helper.short2Boolean(saDtoOriginal.getBNurmaschinenzeit());

			if (flrOffeneags.getFlrlossollarbeitsplan().getFlrpersonal_zugeordneter() != null) {

				oZeile[MASCHINEUNDMATERIAL_PERSON] = HelperServer.formatPersonAusFLRPartner(
						flrOffeneags.getFlrlossollarbeitsplan().getFlrpersonal_zugeordneter().getFlrpartner());
			}

			oZeile[MASCHINEUNDMATERIAL_UNTERARBEITSGANG] = saDtoOriginal.getIUnterarbeitsgang();
			oZeile[MASCHINEUNDMATERIAL_ZEITEINHEIT_ARBEITSPLAN] = sEinheit;

			double lStueckzeitOriginal = saDtoOriginal.getLStueckzeit();
			double lRuestzeitOriginal = saDtoOriginal.getLRuestzeit();
			double dRuestzeitOriginal = 0;
			double dStueckzeitOriginal = 0;

			if (sEinheit.equals(SystemFac.EINHEIT_STUNDE.trim())) {
				dStueckzeitOriginal = lStueckzeitOriginal / 3600000;
				dRuestzeitOriginal = lRuestzeitOriginal / 3600000;
			} else if (sEinheit.equals(SystemFac.EINHEIT_MINUTE.trim())) {
				dStueckzeitOriginal = lStueckzeitOriginal / 60000;
				dRuestzeitOriginal = lRuestzeitOriginal / 60000;
			} else if (sEinheit.equals(SystemFac.EINHEIT_SEKUNDE.trim())) {
				dStueckzeitOriginal = lStueckzeitOriginal / 1000;
				dRuestzeitOriginal = lRuestzeitOriginal / 1000;
			}

			oZeile[MASCHINEUNDMATERIAL_RUESTZEIT] = Helper.rundeKaufmaennisch(new BigDecimal(dRuestzeitOriginal), 5);
			oZeile[MASCHINEUNDMATERIAL_STUECKZEIT] = Helper.rundeKaufmaennisch(new BigDecimal(dStueckzeitOriginal), 5);

			if (Helper.short2boolean(saDtoOriginal.getBNurmaschinenzeit()) == false) {
				oZeile[MASCHINEUNDMATERIAL_GESAMTZEIT_IN_STUNDEN_PERSONAL] = saDtoOriginal.getNGesamtzeit();
			}
			if (saDtoOriginal.getMaschineIId() != null) {
				oZeile[MASCHINEUNDMATERIAL_GESAMTZEIT_IN_STUNDEN_MASCHINE] = saDtoOriginal.getNGesamtzeit();
			}

			try {

				AuftragzeitenDto[] mannZeiten = getZeiterfassungFac().getAllZeitenEinesBeleges(LocaleFac.BELEGART_LOS,
						flrOffeneags.getLos_i_id(), null, null, null, null,
						ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ARTIKEL, theClientDto);

				AuftragzeitenDto[] maschinenZeiten = getZeiterfassungFac()
						.getAllMaschinenzeitenEinesBeleges(flrOffeneags.getLos_i_id(), null, null, null, theClientDto);

				Double bdZeitPerson = new Double(0);

				Double bdZeitMaschine = new Double(0);

				for (int j = 0; j < mannZeiten.length; j++) {
					if (saDtoOriginal.getIId().equals(mannZeiten[j].getBelegpositionIId())) {
						bdZeitPerson = bdZeitPerson + mannZeiten[j].getDdDauer();
					}

				}

				for (int j = 0; j < maschinenZeiten.length; j++) {
					if (saDtoOriginal.getIId().equals(maschinenZeiten[j].getBelegpositionIId())) {
						bdZeitMaschine = bdZeitMaschine + maschinenZeiten[j].getDdDauer();
					}

				}

				oZeile[MASCHINEUNDMATERIAL_ISTZEIT_IN_STUNDEN_PERSONAL] = bdZeitPerson;
				oZeile[MASCHINEUNDMATERIAL_ISTZEIT_IN_STUNDEN_MASCHINE] = bdZeitMaschine;
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			// Naechster AG
			Integer sollarbeitsplanIId_NaechterAG = null;

			try {
				LossollarbeitsplanDto[] sollDtos = getFertigungFac()
						.lossollarbeitsplanFindByLosIId(flrOffeneags.getFlrlos().getI_id());

				for (int j = 0; j < sollDtos.length; j++) {

					if (saDtoOriginal.getIId().equals(sollDtos[j].getIId())) {

						// Der naechste ists dann
						if (j < sollDtos.length - 1) {
							sollarbeitsplanIId_NaechterAG = sollDtos[j + 1].getIId();
						}

					}

				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			if (sollarbeitsplanIId_NaechterAG != null) {

				LossollarbeitsplanDto saDto_Next = getFertigungFac()
						.lossollarbeitsplanFindByPrimaryKey(sollarbeitsplanIId_NaechterAG);

				oZeile[MASCHINEUNDMATERIAL_NEXT_AG_AG_ART] = saDto_Next.getAgartCNr();

				oZeile[MASCHINEUNDMATERIAL_NEXT_AG_FORTSCHRITT] = saDto_Next.getFFortschritt();

				if (saDto_Next.getIMaschinenversatztage() == null) {
					oZeile[MASCHINEUNDMATERIAL_NEXT_AG_AG_BEGINN] = flrOffeneags.getFlrlos().getT_produktionsbeginn();
				} else {
					oZeile[MASCHINEUNDMATERIAL_NEXT_AG_AG_BEGINN] = Helper.addiereTageZuDatum(
							flrOffeneags.getFlrlos().getT_produktionsbeginn(), saDto_Next.getIMaschinenversatztage());
				}

				oZeile[MASCHINEUNDMATERIAL_NEXT_AG_ARBEITSGANG] = saDto_Next.getIArbeitsgangnummer();

				try {
					if (saDto_Next.getLossollmaterialIId() != null) {
						LossollmaterialDto sollmatDto = getFertigungFac()
								.lossollmaterialFindByPrimaryKey(saDto_Next.getLossollmaterialIId());

						ArtikelDto aDtoSollmat = getArtikelFac()
								.artikelFindByPrimaryKeySmall(sollmatDto.getArtikelIId(), theClientDto);
						oZeile[MASCHINEUNDMATERIAL_NEXT_AG_ARTIKELNUMMER_MATERIAL] = aDtoSollmat.getCNr();
						oZeile[MASCHINEUNDMATERIAL_NEXT_AG_ARTIKELBEZEICHNUNG_MATERIAL] = aDtoSollmat
								.formatBezeichnung();

						oZeile[MASCHINEUNDMATERIAL_NEXT_AG_OFFENE_MENGE_MATERIAL] = sollmatDto.getNMenge()
								.subtract(getFertigungFac().getAusgegebeneMenge(saDto_Next.getLossollmaterialIId(),
										null, theClientDto));

					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				ArtikelDto aDtoSollarbeitsplan = getArtikelFac()
						.artikelFindByPrimaryKeySmall(saDto_Next.getArtikelIIdTaetigkeit(), theClientDto);
				oZeile[MASCHINEUNDMATERIAL_NEXT_AG_ARTIKELNUMMER_TAETIGKEIT] = aDtoSollarbeitsplan.getCNr();

				oZeile[MASCHINEUNDMATERIAL_NEXT_AG_ARTIKELBEZEICHNUNG_TAETIGKEIT] = aDtoSollarbeitsplan
						.formatBezeichnung();

				oZeile[MASCHINEUNDMATERIAL_NEXT_AG_AUFSPANNUNG] = saDto_Next.getIAufspannung();
				oZeile[MASCHINEUNDMATERIAL_NEXT_AG_AUTO_STOP_BEI_GEHT] = Helper
						.short2Boolean(saDto_Next.getBAutoendebeigeht());

				oZeile[MASCHINEUNDMATERIAL_NEXT_AG_KOMMENTAR] = saDto_Next.getCKomentar();
				oZeile[MASCHINEUNDMATERIAL_NEXT_AG_KOMMENTAR_LANG] = saDto_Next.getXText();

				if (saDto_Next.getMaschineIId() != null) {
					MaschineDto mDto = getZeiterfassungFac().maschineFindByPrimaryKey(saDto_Next.getMaschineIId());
					oZeile[MASCHINEUNDMATERIAL_NEXT_AG_MASCHINE_BEZEICHNUNG] = mDto.getCBez();
					oZeile[MASCHINEUNDMATERIAL_NEXT_AG_MASCHINE_INVENTARNUMMMER] = mDto.getCInventarnummer();
					oZeile[MASCHINEUNDMATERIAL_NEXT_AG_MASCHINE_IDENTIFIKATIONSNUMMMER] = mDto.getCIdentifikationsnr();
					try {
						oZeile[MASCHINEUNDMATERIAL_NEXT_AG_MASCHINENGRUPPE] = getZeiterfassungFac()
								.maschinengruppeFindByPrimaryKey(mDto.getMaschinengruppeIId()).getCBez();
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}

				oZeile[MASCHINEUNDMATERIAL_NEXT_AG_MASCHINENVERSATZ_MS] = saDto_Next.getIMaschinenversatzMs();
				oZeile[MASCHINEUNDMATERIAL_NEXT_AG_NUR_MASCHINENZEIT] = Helper
						.short2Boolean(saDto_Next.getBNurmaschinenzeit());

				if (saDto_Next.getPersonalIIdZugeordneter() != null) {

					PersonalDto personalDto = getPersonalFac()
							.personalFindByPrimaryKey(saDto_Next.getPersonalIIdZugeordneter(), theClientDto);
					oZeile[MASCHINEUNDMATERIAL_NEXT_AG_PERSON] = personalDto.formatFixUFTitelName2Name1();
				}

				oZeile[MASCHINEUNDMATERIAL_NEXT_AG_UNTERARBEITSGANG] = saDto_Next.getIUnterarbeitsgang();

				double lStueckzeit = saDto_Next.getLStueckzeit();
				double lRuestzeit = saDto_Next.getLRuestzeit();
				double dRuestzeit = 0;
				double dStueckzeit = 0;

				if (sEinheit.equals(SystemFac.EINHEIT_STUNDE.trim())) {
					dStueckzeit = lStueckzeit / 3600000;
					dRuestzeit = lRuestzeit / 3600000;
				} else if (sEinheit.equals(SystemFac.EINHEIT_MINUTE.trim())) {
					dStueckzeit = lStueckzeit / 60000;
					dRuestzeit = lRuestzeit / 60000;
				} else if (sEinheit.equals(SystemFac.EINHEIT_SEKUNDE.trim())) {
					dStueckzeit = lStueckzeit / 1000;
					dRuestzeit = lRuestzeit / 1000;
				}

				oZeile[MASCHINEUNDMATERIAL_NEXT_AG_RUESTZEIT] = Helper.rundeKaufmaennisch(new BigDecimal(dRuestzeit),
						5);
				oZeile[MASCHINEUNDMATERIAL_NEXT_AG_STUECKZEIT] = Helper.rundeKaufmaennisch(new BigDecimal(dStueckzeit),
						5);

				if (Helper.short2boolean(saDto_Next.getBNurmaschinenzeit()) == false) {
					oZeile[MASCHINEUNDMATERIAL_NEXT_AG_GESAMTZEIT_IN_STUNDEN_PERSONAL] = saDto_Next.getNGesamtzeit();
				}
				if (saDto_Next.getMaschineIId() != null) {
					oZeile[MASCHINEUNDMATERIAL_NEXT_AG_GESAMTZEIT_IN_STUNDEN_MASCHINE] = saDto_Next.getNGesamtzeit();
				}
			}
			alDaten.add(oZeile);

		}
		session.close();

		data = new Object[alDaten.size()][MASCHINEUNDMATERIAL_SPALTENANZAHL];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_MASCHINEUNDMATERIAL,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printTaetigkeitAGBeginn(Integer artikelIId_Taetigkeit, DatumsfilterVonBis vonBis,
			TheClientDto theClientDto) {
		this.useCase = UC_TAETIGKEITAGBEGINN;
		this.index = -1;

		Map<String, Object> mapParameter = new TreeMap<String, Object>();

		mapParameter.put("P_VON", vonBis.getTimestampVon());
		mapParameter.put("P_BIS", vonBis.getTimestampBisUnveraendert());

		ParametermandantDto parameter = null;
		try {
			parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_STUECKLISTE_ARBEITSPLAN_ZEITEINHEIT);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		String sEinheit = parameter.getCWert().trim();

		String sQuery = " SELECT flroffeneags, (select count(z.i_id) FROM FLRZeitdaten z WHERE z.i_belegartpositionid=flroffeneags.i_id AND z.c_belegartnr='"
				+ LocaleFac.BELEGART_LOS + "'), aspr_stueckliste from FLROffeneags flroffeneags  "
				+ "LEFT OUTER JOIN flroffeneags.flrartikel_taetigkeit.artikelsprset AS aspr_taetigkeit   "
				+ "LEFT OUTER JOIN flroffeneags.flrartikel_stueckliste.artikelsprset AS aspr_stueckliste   "
				+ "LEFT OUTER JOIN flroffeneags.flrartikel_sollmaterial.artikelsprset AS aspr_sollmaterial   "
				+ "LEFT OUTER JOIN flroffeneags.flrmaschine AS flrmaschine  "
				+ "LEFT OUTER JOIN flroffeneags.flrartikel_taetigkeit AS taetigkeit "
				+ "LEFT OUTER JOIN flroffeneags.flrartikel_sollmaterial AS sollmaterial  "
				+ "LEFT OUTER JOIN flroffeneags.flrartikel_stueckliste AS stueckliste "
				+ "LEFT OUTER JOIN flroffeneags.flrlossollarbeitsplan AS sollarbeitsplan " + ""
				+ "WHERE flroffeneags.mandant_c_nr = '" + theClientDto.getMandant() + "'";
		if (vonBis.getTimestampVon() != null) {
			sQuery += " AND flroffeneags.t_agbeginn>='" + Helper.formatTimestampWithSlashes(vonBis.getTimestampVon())
					+ "'";
		}

		if (vonBis.getTimestampBis() != null) {
			sQuery += " AND flroffeneags.t_agbeginn<'" + Helper.formatTimestampWithSlashes(vonBis.getTimestampBis())
					+ "'";
		}

		if (artikelIId_Taetigkeit != null) {

			sQuery += " AND flroffeneags.flrartikel_taetigkeit.i_id=" + artikelIId_Taetigkeit + " ";
			mapParameter.put("P_ARTIKEL_TAETIGKEIT", getArtikelFac()
					.artikelFindByPrimaryKeySmall(artikelIId_Taetigkeit, theClientDto).formatArtikelbezeichnung());

		}

		sQuery += " ORDER BY flroffeneags.flrartikel_taetigkeit.c_nr ASC, flroffeneags.t_agbeginn ASC, flroffeneags.flrlossollarbeitsplan.i_maschinenversatz_ms ASC, flroffeneags.flrlossollarbeitsplan.i_arbeitsgangsnummer ASC";

		Session session = FLRSessionFactory.getFactory().openSession();
		// SP5549
		session.enableFilter("filterLocale").setParameter("paramLocale", theClientDto.getLocUiAsString());
		org.hibernate.Query hquery = session.createQuery(sQuery);
		List<?> resultList = hquery.list();
		Iterator<?> resultListIterator = resultList.iterator();
		int i = 0;
		ArrayList alDaten = new ArrayList();

		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();

			FLROffeneags flrOffeneags = (FLROffeneags) o[0];

			Object[] oZeile = new Object[TAETIGKEITAGBEGINN_SPALTENANZAHL];

			// AG

			long lAnzahlZeitbuchungen = 0;
			if (o[1] != null) {
				lAnzahlZeitbuchungen = (Long) o[1];
			}

			if (lAnzahlZeitbuchungen > 0) {

				oZeile[TAETIGKEITAGBEGINN_IN_ARBEIT] = Boolean.TRUE;

				try {

					Double bdZeitPerson = new Double(0);
					try {
						AuftragzeitenDto[] mannZeiten = getZeiterfassungFac().getAllZeitenEinesBeleges(
								LocaleFac.BELEGART_LOS, flrOffeneags.getLos_i_id(), null, null, null, null,
								ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ARTIKEL, theClientDto);

						for (int j = 0; j < mannZeiten.length; j++) {
							if (flrOffeneags.getI_id().equals(mannZeiten[j].getBelegpositionIId())) {
								bdZeitPerson = bdZeitPerson + mannZeiten[j].getDdDauer();
							}

						}

						oZeile[TAETIGKEITAGBEGINN_ISTZEIT_IN_STUNDEN_PERSONAL] = bdZeitPerson;

					} catch (EJBExceptionLP e) {

						if (e.getCode() == EJBExceptionLP.FEHLER_IN_ZEITDATEN) {

							List<?> al = e.getAlInfoForTheClient();

							if (al != null && al.size() > 1) {
								String s = "";

								if (al.get(0) instanceof Integer) {
									PersonalDto personalDto = getPersonalFac()
											.personalFindByPrimaryKey((Integer) al.get(0), theClientDto);
									s += " " + personalDto.getCPersonalnr() + " "
											+ personalDto.getPartnerDto().formatFixName2Name1();
								}
								if (al.get(1) instanceof java.sql.Timestamp) {
									s += ", " + Helper.formatDatum((java.sql.Timestamp) al.get(1),
											theClientDto.getLocUi()) + "";
								}
								oZeile[TAETIGKEITAGBEGINN_FEHLER_IN_ZEITDATEN] = "Fehler in Zeitdaten " + s;
							} else {
								oZeile[TAETIGKEITAGBEGINN_FEHLER_IN_ZEITDATEN] = "Fehler in Zeitdaten";
							}

						} else {
							throw e;
						}

					}

					AuftragzeitenDto[] maschinenZeiten = getZeiterfassungFac().getAllMaschinenzeitenEinesBeleges(
							flrOffeneags.getLos_i_id(), null, null, null, theClientDto);

					Double bdZeitMaschine = new Double(0);

					for (int j = 0; j < maschinenZeiten.length; j++) {
						if (flrOffeneags.getI_id().equals(maschinenZeiten[j].getBelegpositionIId())) {
							bdZeitMaschine = bdZeitMaschine + maschinenZeiten[j].getDdDauer();
						}

					}

					oZeile[TAETIGKEITAGBEGINN_ISTZEIT_IN_STUNDEN_MASCHINE] = bdZeitMaschine;

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			} else {
				oZeile[TAETIGKEITAGBEGINN_IN_ARBEIT] = Boolean.FALSE;
			}

			LossollarbeitsplanDto saDtoOriginal = getFertigungFac()
					.lossollarbeitsplanFindByPrimaryKey(flrOffeneags.getI_id());

			oZeile[TAETIGKEITAGBEGINN_AG_ART] = flrOffeneags.getFlrlossollarbeitsplan().getAgart_c_nr();
			oZeile[TAETIGKEITAGBEGINN_FORTSCHRITT] = flrOffeneags.getFlrlossollarbeitsplan().getF_fortschritt();
			oZeile[TAETIGKEITAGBEGINN_AG_BEGINN] = flrOffeneags.getT_agbeginn();
			oZeile[TAETIGKEITAGBEGINN_ARBEITSGANG] = flrOffeneags.getFlrlossollarbeitsplan().getI_arbeitsgangsnummer();

			if (flrOffeneags.getFlrlossollarbeitsplan().getFlrsollmaterial() != null) {
				oZeile[TAETIGKEITAGBEGINN_ARTIKELNUMMER_MATERIAL] = flrOffeneags.getFlrlossollarbeitsplan()
						.getFlrsollmaterial().getFlrartikel().getC_nr();

				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
						flrOffeneags.getFlrlossollarbeitsplan().getFlrsollmaterial().getFlrartikel().getI_id(),
						theClientDto);

				oZeile[TAETIGKEITAGBEGINN_ARTIKELBEZEICHNUNG_MATERIAL] = aDto.formatBezeichnung();

			}

			oZeile[TAETIGKEITAGBEGINN_ARTIKELNUMMER_TAETIGKEIT] = flrOffeneags.getFlrlossollarbeitsplan()
					.getFlrartikel().getC_nr();

			ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
					flrOffeneags.getFlrlossollarbeitsplan().getFlrartikel().getI_id(), theClientDto);

			oZeile[TAETIGKEITAGBEGINN_ARTIKELBEZEICHNUNG_TAETIGKEIT] = aDto.formatBezeichnung();

			if (flrOffeneags.getFlrartikel_stueckliste() != null) {
				oZeile[TAETIGKEITAGBEGINN_ARTIKELNUMMER_STKL] = flrOffeneags.getFlrartikel_stueckliste().getC_nr();
				FLRArtikellistespr artikellistespr_stueckliste = (FLRArtikellistespr) o[2];
				if (artikellistespr_stueckliste != null) {
					oZeile[TAETIGKEITAGBEGINN_ARTIKELBEZEICHNUNG_STKL] = artikellistespr_stueckliste.getC_bez();
					oZeile[TAETIGKEITAGBEGINN_ARTIKELKURZBEZEICHNUNG_STKL] = artikellistespr_stueckliste.getC_kbez();
					oZeile[TAETIGKEITAGBEGINN_ARTIKELZUSATZBEZEICHNUNG_STKL] = artikellistespr_stueckliste.getC_zbez();
					oZeile[TAETIGKEITAGBEGINN_ARTIKELZUSATZBEZEICHNUNG2_STKL] = artikellistespr_stueckliste
							.getC_zbez2();

				}
			}

			oZeile[TAETIGKEITAGBEGINN_ARTIKELBEZEICHNUNG_TAETIGKEIT] = aDto.formatBezeichnung();

			oZeile[TAETIGKEITAGBEGINN_AUFSPANNUNG] = flrOffeneags.getFlrlossollarbeitsplan().getI_aufspannung();
			oZeile[TAETIGKEITAGBEGINN_AUTO_STOP_BEI_GEHT] = Helper.short2Boolean(saDtoOriginal.getBAutoendebeigeht());

			oZeile[TAETIGKEITAGBEGINN_KOMMENTAR] = saDtoOriginal.getCKomentar();
			oZeile[TAETIGKEITAGBEGINN_KOMMENTAR_LANG] = saDtoOriginal.getXText();
			oZeile[TAETIGKEITAGBEGINN_LOSNUMMER] = flrOffeneags.getFlrlos().getC_nr();
			oZeile[TAETIGKEITAGBEGINN_LOSGROESSE] = flrOffeneags.getFlrlos().getN_losgroesse();

			if (flrOffeneags.getFlrkunde() != null) {

				oZeile[TAETIGKEITAGBEGINN_KUNDE_KBEZ] = flrOffeneags.getFlrkunde().getFlrpartner().getC_kbez();
			}

			if (flrOffeneags.getFlrlossollarbeitsplan().getFlrmaschine() != null) {
				oZeile[TAETIGKEITAGBEGINN_MASCHINE_BEZEICHNUNG] = flrOffeneags.getFlrlossollarbeitsplan()
						.getFlrmaschine().getC_bez();
				oZeile[TAETIGKEITAGBEGINN_MASCHINE_INVENTARNUMMMER] = flrOffeneags.getFlrlossollarbeitsplan()
						.getFlrmaschine().getC_inventarnummer();
				oZeile[TAETIGKEITAGBEGINN_MASCHINE_IDENTIFIKATIONSNUMMMER] = flrOffeneags.getFlrlossollarbeitsplan()
						.getFlrmaschine().getC_identifikationsnr();

			}

			oZeile[TAETIGKEITAGBEGINN_MASCHINENVERSATZ_MS] = saDtoOriginal.getIMaschinenversatzMs();
			oZeile[TAETIGKEITAGBEGINN_NUR_MASCHINENZEIT] = Helper.short2Boolean(saDtoOriginal.getBNurmaschinenzeit());

			if (flrOffeneags.getFlrlossollarbeitsplan().getFlrpersonal_zugeordneter() != null) {

				oZeile[TAETIGKEITAGBEGINN_PERSON] = HelperServer.formatPersonAusFLRPartner(
						flrOffeneags.getFlrlossollarbeitsplan().getFlrpersonal_zugeordneter().getFlrpartner());
			}

			oZeile[TAETIGKEITAGBEGINN_UNTERARBEITSGANG] = saDtoOriginal.getIUnterarbeitsgang();
			oZeile[TAETIGKEITAGBEGINN_ZEITEINHEIT_ARBEITSPLAN] = sEinheit;

			double lStueckzeitOriginal = saDtoOriginal.getLStueckzeit();
			double lRuestzeitOriginal = saDtoOriginal.getLRuestzeit();
			double dRuestzeitOriginal = 0;
			double dStueckzeitOriginal = 0;

			if (sEinheit.equals(SystemFac.EINHEIT_STUNDE.trim())) {
				dStueckzeitOriginal = lStueckzeitOriginal / 3600000;
				dRuestzeitOriginal = lRuestzeitOriginal / 3600000;
			} else if (sEinheit.equals(SystemFac.EINHEIT_MINUTE.trim())) {
				dStueckzeitOriginal = lStueckzeitOriginal / 60000;
				dRuestzeitOriginal = lRuestzeitOriginal / 60000;
			} else if (sEinheit.equals(SystemFac.EINHEIT_SEKUNDE.trim())) {
				dStueckzeitOriginal = lStueckzeitOriginal / 1000;
				dRuestzeitOriginal = lRuestzeitOriginal / 1000;
			}

			oZeile[TAETIGKEITAGBEGINN_RUESTZEIT] = Helper.rundeKaufmaennisch(new BigDecimal(dRuestzeitOriginal), 5);
			oZeile[TAETIGKEITAGBEGINN_STUECKZEIT] = Helper.rundeKaufmaennisch(new BigDecimal(dStueckzeitOriginal), 5);

			if (Helper.short2boolean(saDtoOriginal.getBNurmaschinenzeit()) == false) {
				oZeile[TAETIGKEITAGBEGINN_GESAMTZEIT_IN_STUNDEN_PERSONAL] = saDtoOriginal.getNGesamtzeit();
			}

			if (saDtoOriginal.getMaschineIId() != null) {
				oZeile[TAETIGKEITAGBEGINN_GESAMTZEIT_IN_STUNDEN_MASCHINE] = saDtoOriginal.getNGesamtzeit();
			}

			// Naechster AG
			Integer sollarbeitsplanIId_NaechterAG = null;

			try {
				LossollarbeitsplanDto[] sollDtos = getFertigungFac()
						.lossollarbeitsplanFindByLosIId(flrOffeneags.getFlrlos().getI_id());

				for (int j = 0; j < sollDtos.length; j++) {

					if (saDtoOriginal.getIId().equals(sollDtos[j].getIId())) {

						// Der naechste ists dann
						if (j < sollDtos.length - 1) {
							sollarbeitsplanIId_NaechterAG = sollDtos[j + 1].getIId();
						}

					}

				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			if (sollarbeitsplanIId_NaechterAG != null) {

				LossollarbeitsplanDto saDto_Next = getFertigungFac()
						.lossollarbeitsplanFindByPrimaryKey(sollarbeitsplanIId_NaechterAG);

				if (saDto_Next.getIMaschinenversatztage() != null) {
					oZeile[TAETIGKEITAGBEGINN_NEXT_AG_BEGINN] = Helper.addiereTageZuDatum(
							flrOffeneags.getFlrlos().getT_produktionsbeginn(), saDto_Next.getIMaschinenversatztage());
				} else {
					oZeile[TAETIGKEITAGBEGINN_NEXT_AG_BEGINN] = flrOffeneags.getFlrlos().getT_produktionsbeginn();
				}

				oZeile[TAETIGKEITAGBEGINN_NEXT_MASCHINENVERSATZ_MS] = saDto_Next.getIMaschinenversatzMs();
			} else {
				oZeile[TAETIGKEITAGBEGINN_NEXT_AG_BEGINN] = flrOffeneags.getFlrlos().getT_produktionsende();
			}

			alDaten.add(oZeile);

		}
		session.close();

		data = new Object[alDaten.size()][TAETIGKEITAGBEGINN_SPALTENANZAHL];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_TAETIGKEITAGBEGINN,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printLadeliste(Integer artikelIId_Taetigkeit, Integer artikelgruppeIId,
			DatumsfilterVonBis vonBis, TheClientDto theClientDto) {
		this.useCase = UC_LADELISTE;
		this.index = -1;

		Map<String, Object> mapParameter = new TreeMap<String, Object>();

		mapParameter.put("P_VON", vonBis.getTimestampVon());
		mapParameter.put("P_BIS", vonBis.getTimestampBisUnveraendert());

		ParametermandantDto parameter = null;
		try {
			parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_STUECKLISTE_ARBEITSPLAN_ZEITEINHEIT);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		String sEinheit = parameter.getCWert().trim();

		String sQuery = " SELECT flroffeneags, (select count(z.i_id) FROM FLRZeitdaten z WHERE z.i_belegartpositionid=flroffeneags.i_id AND z.c_belegartnr='"
				+ LocaleFac.BELEGART_LOS + "'), aspr_stueckliste from FLROffeneags flroffeneags  "
				+ "LEFT OUTER JOIN flroffeneags.flrartikel_taetigkeit.artikelsprset AS aspr_taetigkeit   "
				+ "LEFT OUTER JOIN flroffeneags.flrartikel_stueckliste.artikelsprset AS aspr_stueckliste   "
				+ "LEFT OUTER JOIN flroffeneags.flrartikel_sollmaterial.artikelsprset AS aspr_sollmaterial   "
				+ "LEFT OUTER JOIN flroffeneags.flrmaschine AS flrmaschine  "
				+ "LEFT OUTER JOIN flroffeneags.flrartikel_taetigkeit AS taetigkeit "
				+ "LEFT OUTER JOIN flroffeneags.flrartikel_sollmaterial AS sollmaterial  "
				+ "LEFT OUTER JOIN flroffeneags.flrartikel_stueckliste AS stueckliste "
				+ "LEFT OUTER JOIN flroffeneags.flrlossollarbeitsplan AS sollarbeitsplan " + ""
				+ "WHERE flroffeneags.mandant_c_nr = '" + theClientDto.getMandant() + "'";
		if (vonBis.getTimestampVon() != null) {
			sQuery += " AND flroffeneags.t_agbeginn>='" + Helper.formatTimestampWithSlashes(vonBis.getTimestampVon())
					+ "'";
		}

		if (vonBis.getTimestampBis() != null) {
			sQuery += " AND flroffeneags.t_agbeginn<'" + Helper.formatTimestampWithSlashes(vonBis.getTimestampBis())
					+ "'";
		}

		if (artikelIId_Taetigkeit != null) {

			sQuery += " AND flroffeneags.flrartikel_taetigkeit.i_id=" + artikelIId_Taetigkeit + " ";
			mapParameter.put("P_ARTIKEL_TAETIGKEIT", getArtikelFac()
					.artikelFindByPrimaryKeySmall(artikelIId_Taetigkeit, theClientDto).formatArtikelbezeichnung());

		}

		if (artikelgruppeIId != null) {

			sQuery += " AND flroffeneags.flrartikel_taetigkeit.flrartikelgruppe.i_id=" + artikelgruppeIId + " ";
			mapParameter.put("P_ARTIKELGRUPPE_TAETIGKEIT",
					getArtikelFac().artgruFindByPrimaryKey(artikelgruppeIId, theClientDto).getBezeichnung());

		}

		sQuery += " ORDER BY flroffeneags.flrartikel_taetigkeit.c_nr ASC, flroffeneags.t_agbeginn ASC, flroffeneags.flrlossollarbeitsplan.i_maschinenversatz_ms ASC, flroffeneags.flrlossollarbeitsplan.i_arbeitsgangsnummer ASC";

		Session session = FLRSessionFactory.getFactory().openSession();
		// SP5549
		session.enableFilter("filterLocale").setParameter("paramLocale", theClientDto.getLocUiAsString());
		org.hibernate.Query hquery = session.createQuery(sQuery);
		List<?> resultList = hquery.list();
		Iterator<?> resultListIterator = resultList.iterator();
		int i = 0;
		ArrayList alDaten = new ArrayList();

		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();

			FLROffeneags flrOffeneags = (FLROffeneags) o[0];

			Object[] oZeile = new Object[LADELISTE_SPALTENANZAHL];

			// AG

			long lAnzahlZeitbuchungen = 0;
			if (o[1] != null) {
				lAnzahlZeitbuchungen = (Long) o[1];
			}

			if (lAnzahlZeitbuchungen > 0) {

				oZeile[LADELISTE_IN_ARBEIT] = Boolean.TRUE;

			} else {
				oZeile[LADELISTE_IN_ARBEIT] = Boolean.FALSE;
			}

			LossollarbeitsplanDto saDtoOriginal = getFertigungFac()
					.lossollarbeitsplanFindByPrimaryKey(flrOffeneags.getI_id());

			if (flrOffeneags.getFlrartikel_stueckliste() != null) {
				oZeile[LADELISTE_ARTIKELNUMMER_STKL] = flrOffeneags.getFlrartikel_stueckliste().getC_nr();
				FLRArtikellistespr artikellistespr_stueckliste = (FLRArtikellistespr) o[2];
				if (artikellistespr_stueckliste != null) {
					oZeile[LADELISTE_ARTIKELBEZEICHNUNG_STKL] = artikellistespr_stueckliste.getC_bez();
					oZeile[LADELISTE_ARTIKELKURZBEZEICHNUNG_STKL] = artikellistespr_stueckliste.getC_kbez();
					oZeile[LADELISTE_ARTIKELZUSATZBEZEICHNUNG_STKL] = artikellistespr_stueckliste.getC_zbez();
					oZeile[LADELISTE_ARTIKELZUSATZBEZEICHNUNG2_STKL] = artikellistespr_stueckliste.getC_zbez2();

				}
			}

			oZeile[LADELISTE_LOSNUMMER] = flrOffeneags.getFlrlos().getC_nr();
			oZeile[LADELISTE_LOSGROESSE] = flrOffeneags.getFlrlos().getN_losgroesse();
			oZeile[LADELISTE_LOSSTATUS] = flrOffeneags.getFlrlos().getStatus_c_nr();

			if (flrOffeneags.getFlrkunde() != null) {

				oZeile[LADELISTE_KUNDE_KBEZ] = flrOffeneags.getFlrkunde().getFlrpartner().getC_kbez();
			}

			oZeile[LADELISTE_ZEITEINHEIT_ARBEITSPLAN] = sEinheit;

			double lStueckzeitOriginal = saDtoOriginal.getLStueckzeit();
			double lRuestzeitOriginal = saDtoOriginal.getLRuestzeit();
			double dRuestzeitOriginal = 0;
			double dStueckzeitOriginal = 0;

			if (sEinheit.equals(SystemFac.EINHEIT_STUNDE.trim())) {
				dStueckzeitOriginal = lStueckzeitOriginal / 3600000;
				dRuestzeitOriginal = lRuestzeitOriginal / 3600000;
			} else if (sEinheit.equals(SystemFac.EINHEIT_MINUTE.trim())) {
				dStueckzeitOriginal = lStueckzeitOriginal / 60000;
				dRuestzeitOriginal = lRuestzeitOriginal / 60000;
			} else if (sEinheit.equals(SystemFac.EINHEIT_SEKUNDE.trim())) {
				dStueckzeitOriginal = lStueckzeitOriginal / 1000;
				dRuestzeitOriginal = lRuestzeitOriginal / 1000;
			}

			// Naechster AG
			Integer sollarbeitsplanIId_NaechterAG = null;
			Integer sollarbeitsplanIId_VorherigerAG = null;

			try {
				LossollarbeitsplanDto[] sollDtos = getFertigungFac()
						.lossollarbeitsplanFindByLosIId(flrOffeneags.getFlrlos().getI_id());

				for (int j = 0; j < sollDtos.length; j++) {

					if (saDtoOriginal.getIId().equals(sollDtos[j].getIId())) {

						// Der naechste ists dann
						if (j < sollDtos.length - 1) {
							sollarbeitsplanIId_NaechterAG = sollDtos[j + 1].getIId();
						}

						// Der vorherige ists dann
						if (j > 0) {
							sollarbeitsplanIId_VorherigerAG = sollDtos[j - 1].getIId();
						}

					}

				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			oZeile = befuelleArbeitsgaengeFuerLadeliste(saDtoOriginal, null, null, oZeile, flrOffeneags, theClientDto);

			if (sollarbeitsplanIId_NaechterAG != null) {

				LossollarbeitsplanDto saDto_Next = getFertigungFac()
						.lossollarbeitsplanFindByPrimaryKey(sollarbeitsplanIId_NaechterAG);

				oZeile = befuelleArbeitsgaengeFuerLadeliste(null, saDto_Next, null, oZeile, flrOffeneags, theClientDto);
			}

			if (sollarbeitsplanIId_VorherigerAG != null) {

				LossollarbeitsplanDto saDto_Vorherig = getFertigungFac()
						.lossollarbeitsplanFindByPrimaryKey(sollarbeitsplanIId_VorherigerAG);

				oZeile = befuelleArbeitsgaengeFuerLadeliste(null, null, saDto_Vorherig, oZeile, flrOffeneags,
						theClientDto);
			}

			alDaten.add(oZeile);

		}
		session.close();

		data = new Object[alDaten.size()][LADELISTE_SPALTENANZAHL];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_LADELISTE,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printOffeneArbeitsgaenge(java.sql.Date dStichtag, int iOptionStichtag, String belegNrVon,
			String belegNrBis, Integer kundeIId, Integer kostenstelleIId, Integer fertigungsgruppeIId,
			Integer artikelgruppeIId, Integer maschineIId, boolean bSollstundenbetrachtung, TheClientDto theClientDto) {
		Session session = null;

		try {
			this.useCase = UC_OFFENE_AG;
			this.index = -1;
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRLossollarbeitsplan.class);

			c.createAlias(FertigungFac.FLR_LOSSOLLARBEITSPLAN_FLRLOS, "l", CriteriaSpecification.LEFT_JOIN);

			c.add(Restrictions.eq("l." + FertigungFac.FLR_LOS_MANDANT_C_NR, theClientDto.getMandant()));

			c.add(Restrictions.eq(FertigungFac.FLR_LOSSOLLARBEITSPLAN_B_FERTIG, Helper.boolean2Short(false)));

			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			mapParameter.put("P_SOLLSTUNDENBETRACHTUNG", new Boolean(bSollstundenbetrachtung));
			if (artikelgruppeIId != null) {
				c.createAlias(FertigungFac.FLR_LOSSOLLARBEITSPLAN_FLRARTIKEL, "artikel",
						CriteriaSpecification.LEFT_JOIN);
				c.createAlias("artikel." + ArtikelFac.FLR_ARTIKEL_FLRARTIKELGRUPPE, "artikelgruppe",
						CriteriaSpecification.LEFT_JOIN);
				c.add(Restrictions.eq("artikelgruppe.i_id", artikelgruppeIId));

				ArtgruDto artgruDto = getArtikelFac().artgruFindByPrimaryKey(artikelgruppeIId, theClientDto);

				mapParameter.put("P_ARTIKELGRUPPE", artgruDto.getBezeichnung());
			}

			/*
			 * if (kostenstelleIId != null) { KostenstelleDto kostenstelleDto =
			 * getSystemFac() .kostenstelleFindByPrimaryKey(kostenstelleIId);
			 * mapParameter.put("P_KOSTENSTELLE",
			 * kostenstelleDto.formatKostenstellenbezeichnung());
			 * 
			 * c.add(Restrictions.eq("l." + FertigungFac.FLR_LOSREPORT_KOSTENSTELLE_I_ID,
			 * kostenstelleIId)); }
			 */
			boolean flrAuftragSchonVerwendet = false;
			if (kundeIId != null) {
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto);
				mapParameter.put("P_KUNDE", kundeDto.getPartnerDto().formatFixTitelName1Name2());
				c.createAlias("l." + FertigungFac.FLR_LOSREPORT_FLRAUFTRAG, "a", CriteriaSpecification.LEFT_JOIN);
				flrAuftragSchonVerwendet = true;
				c.add(Restrictions.eq("a." + AuftragFac.FLR_AUFTRAG_KUNDE_I_ID_AUFTRAGSADRESSE, kundeIId));
			}

			String[] stati = new String[4];
			stati[0] = LocaleFac.STATUS_AUSGEGEBEN;
			stati[1] = LocaleFac.STATUS_IN_PRODUKTION;
			stati[2] = LocaleFac.STATUS_TEILERLEDIGT;
			stati[3] = LocaleFac.STATUS_ANGELEGT;

			c.add(Restrictions.in("l." + FertigungFac.FLR_LOS_STATUS_C_NR, stati));

			if (dStichtag != null) {

				dStichtag = Helper.cutDate(dStichtag);

				// PJ 14420
				mapParameter.put("P_STICHTAG", dStichtag);

				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(dStichtag.getTime());
				cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 1);

				dStichtag = new java.sql.Date(cal.getTimeInMillis());

				String datumsart = "";

				if (iOptionStichtag == FertigungReportFac.OFFENE_OPTION_STICHTAG_BEGINNDATUM) {
					c.add(Restrictions.lt("l." + FertigungFac.FLR_LOS_T_PRODUKTIONSBEGINN, dStichtag));

					datumsart = getTextRespectUISpr("lp.begintermin", theClientDto.getMandant(),
							theClientDto.getLocUi());

				} else if (iOptionStichtag == FertigungReportFac.OFFENE_OPTION_STICHTAG_ENDEDATUM) {
					c.add(Restrictions.lt("l." + FertigungFac.FLR_LOS_T_PRODUKTIONSENDE, dStichtag));
					datumsart = getTextRespectUISpr("lp.endetermin", theClientDto.getMandant(),
							theClientDto.getLocUi());

				} else if (iOptionStichtag == FertigungReportFac.OFFENE_OPTION_STICHTAG_LIEFERTERMIN) {
					if (flrAuftragSchonVerwendet == false) {
						c.createAlias("l." + FertigungFac.FLR_LOSREPORT_FLRAUFTRAG, "a",
								CriteriaSpecification.LEFT_JOIN);
						flrAuftragSchonVerwendet = true;
						c.add(Restrictions.lt("a." + AuftragFac.FLR_AUFTRAG_T_LIEFERTERMIN, dStichtag));

					}
					datumsart = getTextRespectUISpr("bes.liefertermin", theClientDto.getMandant(),
							theClientDto.getLocUi());

				}

				mapParameter.put("P_DATUMSART", datumsart);

			}
			LpBelegnummerFormat f = getBelegnummerGeneratorObj().getBelegnummernFormat(theClientDto.getMandant());
			Integer iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(theClientDto.getMandant());
			String sMandantKuerzel = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_BELEGNUMMER_MANDANTKENNUNG).getCWert();
			if (belegNrVon != null) {
				String sVon = HelperServer.getBelegnummernFilterForHibernateCriterias(f, iGeschaeftsjahr,
						sMandantKuerzel, belegNrVon);
				c.add(Restrictions.ge("l." + FertigungFac.FLR_LOS_C_NR, sVon));
				mapParameter.put("P_LOSNRVON", sVon);
			}
			if (belegNrBis != null) {
				String sBis = HelperServer.getBelegnummernFilterForHibernateCriterias(f, iGeschaeftsjahr,
						sMandantKuerzel, belegNrBis);
				c.add(Restrictions.le("l." + FertigungFac.FLR_LOS_C_NR, sBis));
				mapParameter.put("P_LOSNRBIS", sBis);
			}

			if (maschineIId != null) {
				MaschineDto mDto = getZeiterfassungFac().maschineFindByPrimaryKey(maschineIId);
				c.add(Restrictions.eq("maschine_i_id", maschineIId));
				mapParameter.put("P_MASCHINE", mDto.getBezeichnung());
			}

			List<?> list = c.list();
			data = new Object[list.size()][OFFENE_AG_SPALTENANZAHL];
			int i = 0;
			for (Iterator<?> iter = list.iterator(); iter.hasNext(); i++) {
				FLRLossollarbeitsplan a = (FLRLossollarbeitsplan) iter.next();
				FLRLos los = a.getFlrlos();
				if (fertigungsgruppeIId != null && kostenstelleIId != null) {
					if (los.getFertigungsgruppe_i_id().intValue() != fertigungsgruppeIId.intValue()
							&& los.getKostenstelle_i_id().intValue() != kostenstelleIId.intValue()) {
						i--;
					} else {
						// skip
					}
				} else if (fertigungsgruppeIId != null && kostenstelleIId == null) {
					if (los.getFertigungsgruppe_i_id().intValue() != fertigungsgruppeIId.intValue()) {
						i--;
					} else {
						// skip
					}
				} else if (fertigungsgruppeIId == null && kostenstelleIId != null) {
					if (los.getKostenstelle_i_id().intValue() != kostenstelleIId.intValue()) {
						i--;
					} else {
						// skip
					}
				}
			}
			if (fertigungsgruppeIId != null || kostenstelleIId != null) {
				data = new Object[i][OFFENE_AG_SPALTENANZAHL];
			}
			i = 0;
			for (Iterator<?> iter = list.iterator(); iter.hasNext(); i++) {
				FLRLossollarbeitsplan a = (FLRLossollarbeitsplan) iter.next();
				FLRLos los = a.getFlrlos();
				if (fertigungsgruppeIId != null && kostenstelleIId != null) {
					if (los.getFertigungsgruppe_i_id().intValue() != fertigungsgruppeIId.intValue()
							&& los.getKostenstelle_i_id().intValue() != kostenstelleIId.intValue()) {
						i--;
					} else {
						data[i][OFFENE_AG_AGNUMMER] = a.getI_arbeitsgangsnummer();
						data[i][OFFENE_AG_UAGNUMMER] = a.getI_unterarbeitsgang();
						data[i][OFFENE_AG_AG_ARTIKEL] = a.getFlrartikel().getC_nr();
						ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(a.getFlrartikel().getI_id(),
								theClientDto);
						data[i][OFFENE_AG_AG_ARTIKELBEZEICHNUNG] = aDto.formatBezeichnung();

						data[i][OFFENE_AG_FORTSCHRITT] = a.getF_fortschritt();

						if (Helper.short2boolean(a.getB_fertig())) {
							data[i][OFFENE_AG_AG_GESAMTZEIT] = new BigDecimal(0);
						} else {

							data[i][OFFENE_AG_AG_GESAMTZEIT] = a.getN_gesamtzeit();
						}

						if (bSollstundenbetrachtung == true) {
							data[i][OFFENE_AG_AG_ISTZEIT] = getZeiterfassungFac().getSummeZeitenEinesBeleges(
									LocaleFac.BELEGART_LOS, los.getI_id(), a.getI_id(), null, null, null, theClientDto);

						}

						if (a.getFlrmaschine() != null) {
							data[i][OFFENE_AG_AG_MASCHINE_BEZEICHNUNG] = a.getFlrmaschine().getC_bez();
							data[i][OFFENE_AG_AG_MASCHINE_IDENTIFIKATIONSNUMMMER] = a.getFlrmaschine()
									.getC_identifikationsnr();
							data[i][OFFENE_AG_AG_MASCHINE_INVENTARNUMMMER] = a.getFlrmaschine().getC_inventarnummer();
						}

						if (a.getFlrartikel().getFlrartikelgruppe() != null) {
							data[i][OFFENE_AG_AG_ARTIKELGRUPPPE] = a.getFlrartikel().getFlrartikelgruppe().getC_nr();
						} else {
							data[i][OFFENE_AG_AG_ARTIKELGRUPPPE] = "";
						}

						if (a.getI_maschinenversatztage() != null) {
							data[i][OFFENE_AG_AG_BEGINN] = Helper.addiereTageZuTimestamp(
									(Timestamp) los.getT_produktionsbeginn(), a.getI_maschinenversatztage());

						} else {
							data[i][OFFENE_AG_AG_BEGINN] = los.getT_produktionsbeginn();

						}

						if (los.getFlrauftrag() != null) {

							Integer partnerIId = los.getFlrauftrag().getFlrkunde().getFlrpartner().getI_id();
							PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(partnerIId, theClientDto);
							data[i][OFFENE_AG_KUNDE] = partnerDto.formatFixTitelName1Name2();
							data[i][OFFENE_AG_AUFTRAGSPOENALE] = los.getFlrauftrag().getB_poenale();
						}

						if (los.getFlrauftrag() != null) {
							data[i][OFFENE_AG_AUFTRAGSNUMMER] = los.getFlrauftrag().getC_nr();
							data[i][OFFENE_AG_PROJEKT] = los.getFlrauftrag().getC_bez();
							data[i][OFFENE_AG_LIEFERTERMIN] = los.getFlrauftrag().getT_liefertermin();
						}
						BigDecimal bdGeliefert = new BigDecimal(0);
						for (Iterator<?> iter2 = los.getAblieferungset().iterator(); iter2.hasNext();) {
							FLRLosablieferung item = (FLRLosablieferung) iter2.next();
							bdGeliefert = bdGeliefert.add(item.getN_menge());
						}
						data[i][OFFENE_AG_GELIEFERT] = bdGeliefert;
						if (los.getFlrstueckliste() != null) {
							ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
									los.getFlrstueckliste().getFlrartikel().getI_id(), theClientDto);
							data[i][OFFENE_AG_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
							data[i][OFFENE_AG_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCZbez();
							data[i][OFFENE_AG_ARTIKELNUMMER] = los.getFlrstueckliste().getFlrartikel().getC_nr();

							data[i][OFFENE_AG_DETAILBEDARF] = getRahmenbedarfeFac()
									.getSummeAllerRahmenbedarfeEinesArtikels(artikelDto.getIId());

							// Offene Fehlmengen
							data[i][OFFENE_AG_FEHLMENGE] = getFehlmengeFac()
									.getAnzahlFehlmengeEinesArtikels(artikelDto.getIId(), theClientDto);

							LossollarbeitsplanDto[] lossollarbeitsplanDto = getFertigungFac()
									.lossollarbeitsplanFindByLosIId(los.getI_id());

							BigDecimal bdFertigungszeit = new BigDecimal(0);
							for (int j = 0; lossollarbeitsplanDto.length > j; j++) {
								bdFertigungszeit = bdFertigungszeit.add(lossollarbeitsplanDto[j].getNGesamtzeit());
							}
							data[i][OFFENE_AG_FERTIGUNGSZEIT] = bdFertigungszeit;

							// Rahmenbestellt
							Hashtable<?, ?> htRahmenbestellt = getArtikelbestelltFac()
									.getAnzahlRahmenbestellt(artikelDto.getIId(), theClientDto);
							if (htRahmenbestellt.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
								BigDecimal bdAnzahlRahmenbestellt = (BigDecimal) htRahmenbestellt
										.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
								data[i][OFFENE_AG_RAHMEN_BESTELLT] = bdAnzahlRahmenbestellt;
							}
							data[i][OFFENE_AG_RESERVIERUNGEN] = getReservierungFac()
									.getAnzahlReservierungen(artikelDto.getIId(), theClientDto);

							data[i][OFFENE_AG_RAHMENRESERVIERUNGEN] = getReservierungFac()
									.getAnzahlRahmenreservierungen(artikelDto.getIId(), theClientDto);

						} else {
							data[i][OFFENE_AG_BEZEICHNUNG] = los.getC_projekt();
							data[i][OFFENE_AG_ZUSATZBEZEICHNUNG] = null;
							data[i][OFFENE_AG_ARTIKELNUMMER] = getTextRespectUISpr("fert.materialliste",
									theClientDto.getMandant(), theClientDto.getLocUi());
						}

						GregorianCalendar gc = new GregorianCalendar();
						gc.setTime(los.getT_produktionsbeginn());
						data[i][OFFENE_AG_KALENDERWOCHE] = new Integer(gc.get(GregorianCalendar.WEEK_OF_YEAR));
						data[i][OFFENE_AG_LOSGROESSE] = los.getN_losgroesse();
						data[i][OFFENE_AG_LOSNUMMER] = los.getC_nr();
						data[i][OFFENE_AG_FERTIGUNGSGRUPPE] = los.getFlrfertigungsgruppe().getC_bez();
						/**
						 * @todo material PJ 4239
						 */
						data[i][OFFENE_AG_MATERIAL] = null;
						data[i][OFFENE_AG_BEGINN] = los.getT_produktionsbeginn();
						data[i][OFFENE_AG_ENDE] = los.getT_produktionsende();
						data[i][OFFENE_AG_LOSSTATUS] = los.getStatus_c_nr();
						data[i][OFFENE_AG_LOSZUSATZSTATUS_P1] = getFertigungFac().hatLosZusatzstatusP1(los.getI_id());
						// PJ 15009

						String queryf = "select sum(flrfehlmenge.n_menge) FROM FLRFehlmenge flrfehlmenge WHERE flrfehlmenge.flrlossollmaterial.flrlos = "
								+ los.getI_id() + " AND flrfehlmenge.n_menge>0";
						Session session2 = FLRSessionFactory.getFactory().openSession();
						Query query = session2.createQuery(queryf);
						List<?> results = query.list();

						data[i][OFFENE_AG_LOSHATFEHLMENGE] = new Boolean(false);
						if (results.size() > 0) {
							BigDecimal bd = (BigDecimal) results.iterator().next();

							if (bd != null && bd.doubleValue() > 0) {
								data[i][OFFENE_AG_LOSHATFEHLMENGE] = new Boolean(true);
							}
						}
						session2.close();
					}
				} else if (fertigungsgruppeIId != null || kostenstelleIId != null) {
					if (fertigungsgruppeIId != null) {
						if (los.getFertigungsgruppe_i_id().intValue() != fertigungsgruppeIId.intValue()) {
							i--;
						} else {
							data[i][OFFENE_AG_AGNUMMER] = a.getI_arbeitsgangsnummer();
							data[i][OFFENE_AG_UAGNUMMER] = a.getI_unterarbeitsgang();
							data[i][OFFENE_AG_AG_ARTIKEL] = a.getFlrartikel().getC_nr();
							ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(a.getFlrartikel().getI_id(),
									theClientDto);
							data[i][OFFENE_AG_AG_ARTIKELBEZEICHNUNG] = aDto.formatBezeichnung();

							data[i][OFFENE_AG_FORTSCHRITT] = a.getF_fortschritt();

							if (Helper.short2boolean(a.getB_fertig())) {
								data[i][OFFENE_AG_AG_GESAMTZEIT] = new BigDecimal(0);
							} else {
								data[i][OFFENE_AG_AG_GESAMTZEIT] = a.getN_gesamtzeit();
							}

							if (bSollstundenbetrachtung == true) {
								data[i][OFFENE_AG_AG_ISTZEIT] = getZeiterfassungFac().getSummeZeitenEinesBeleges(
										LocaleFac.BELEGART_LOS, los.getI_id(), a.getI_id(), null, null, null,
										theClientDto);

							}

							if (a.getFlrmaschine() != null) {
								data[i][OFFENE_AG_AG_MASCHINE_BEZEICHNUNG] = a.getFlrmaschine().getC_bez();
								data[i][OFFENE_AG_AG_MASCHINE_IDENTIFIKATIONSNUMMMER] = a.getFlrmaschine()
										.getC_identifikationsnr();
								data[i][OFFENE_AG_AG_MASCHINE_INVENTARNUMMMER] = a.getFlrmaschine()
										.getC_inventarnummer();
							}

							if (a.getFlrartikel().getFlrartikelgruppe() != null) {
								data[i][OFFENE_AG_AG_ARTIKELGRUPPPE] = a.getFlrartikel().getFlrartikelgruppe()
										.getC_nr();
							} else {
								data[i][OFFENE_AG_AG_ARTIKELGRUPPPE] = "";
							}

							if (a.getI_maschinenversatztage() != null) {
								data[i][OFFENE_AG_AG_BEGINN] = Helper.addiereTageZuTimestamp(
										(Timestamp) los.getT_produktionsbeginn(), a.getI_maschinenversatztage());

							} else {
								data[i][OFFENE_AG_AG_BEGINN] = los.getT_produktionsbeginn();

							}

							if (los.getFlrauftrag() != null) {

								Integer partnerIId = los.getFlrauftrag().getFlrkunde().getFlrpartner().getI_id();
								PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(partnerIId,
										theClientDto);
								data[i][OFFENE_AG_KUNDE] = partnerDto.formatFixTitelName1Name2();
								data[i][OFFENE_AG_AUFTRAGSPOENALE] = los.getFlrauftrag().getB_poenale();
							}

							if (los.getFlrauftrag() != null) {
								data[i][OFFENE_AG_AUFTRAGSNUMMER] = los.getFlrauftrag().getC_nr();
								data[i][OFFENE_AG_PROJEKT] = los.getFlrauftrag().getC_bez();
								data[i][OFFENE_AG_LIEFERTERMIN] = los.getFlrauftrag().getT_liefertermin();
							}
							BigDecimal bdGeliefert = new BigDecimal(0);
							for (Iterator<?> iter2 = los.getAblieferungset().iterator(); iter2.hasNext();) {
								FLRLosablieferung item = (FLRLosablieferung) iter2.next();
								bdGeliefert = bdGeliefert.add(item.getN_menge());
							}
							data[i][OFFENE_AG_GELIEFERT] = bdGeliefert;
							if (los.getFlrstueckliste() != null) {
								ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
										los.getFlrstueckliste().getFlrartikel().getI_id(), theClientDto);
								data[i][OFFENE_AG_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
								data[i][OFFENE_AG_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCZbez();
								data[i][OFFENE_AG_ARTIKELNUMMER] = los.getFlrstueckliste().getFlrartikel().getC_nr();

								data[i][OFFENE_AG_DETAILBEDARF] = getRahmenbedarfeFac()
										.getSummeAllerRahmenbedarfeEinesArtikels(artikelDto.getIId());

								// Offene Fehlmengen
								data[i][OFFENE_AG_FEHLMENGE] = getFehlmengeFac()
										.getAnzahlFehlmengeEinesArtikels(artikelDto.getIId(), theClientDto);

								LossollarbeitsplanDto[] lossollarbeitsplanDto = getFertigungFac()
										.lossollarbeitsplanFindByLosIId(los.getI_id());

								BigDecimal bdFertigungszeit = new BigDecimal(0);
								for (int j = 0; lossollarbeitsplanDto.length > j; j++) {
									bdFertigungszeit = bdFertigungszeit.add(lossollarbeitsplanDto[j].getNGesamtzeit());
								}
								data[i][OFFENE_AG_FERTIGUNGSZEIT] = bdFertigungszeit;

								// Rahmenbestellt
								Hashtable<?, ?> htRahmenbestellt = getArtikelbestelltFac()
										.getAnzahlRahmenbestellt(artikelDto.getIId(), theClientDto);
								if (htRahmenbestellt.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
									BigDecimal bdAnzahlRahmenbestellt = (BigDecimal) htRahmenbestellt
											.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
									data[i][OFFENE_AG_RAHMEN_BESTELLT] = bdAnzahlRahmenbestellt;
								}
								data[i][OFFENE_AG_RESERVIERUNGEN] = getReservierungFac()
										.getAnzahlReservierungen(artikelDto.getIId(), theClientDto);

								data[i][OFFENE_AG_RAHMENRESERVIERUNGEN] = getReservierungFac()
										.getAnzahlRahmenreservierungen(artikelDto.getIId(), theClientDto);

							} else {
								data[i][OFFENE_AG_BEZEICHNUNG] = los.getC_projekt();
								data[i][OFFENE_AG_ZUSATZBEZEICHNUNG] = null;
								data[i][OFFENE_AG_ARTIKELNUMMER] = getTextRespectUISpr("fert.materialliste",
										theClientDto.getMandant(), theClientDto.getLocUi());
							}

							GregorianCalendar gc = new GregorianCalendar();
							gc.setTime(los.getT_produktionsbeginn());
							data[i][OFFENE_AG_KALENDERWOCHE] = new Integer(gc.get(GregorianCalendar.WEEK_OF_YEAR));
							data[i][OFFENE_AG_LOSGROESSE] = los.getN_losgroesse();
							data[i][OFFENE_AG_LOSNUMMER] = los.getC_nr();
							data[i][OFFENE_AG_FERTIGUNGSGRUPPE] = los.getFlrfertigungsgruppe().getC_bez();
							/**
							 * @todo material PJ 4239
							 */
							data[i][OFFENE_AG_MATERIAL] = null;
							data[i][OFFENE_AG_BEGINN] = los.getT_produktionsbeginn();
							data[i][OFFENE_AG_ENDE] = los.getT_produktionsende();
							data[i][OFFENE_AG_LOSSTATUS] = los.getStatus_c_nr();
							data[i][OFFENE_AG_LOSZUSATZSTATUS_P1] = getFertigungFac()
									.hatLosZusatzstatusP1(los.getI_id());
							// PJ 15009

							String queryf = "select sum(flrfehlmenge.n_menge) FROM FLRFehlmenge flrfehlmenge WHERE flrfehlmenge.flrlossollmaterial.flrlos = "
									+ los.getI_id() + " AND flrfehlmenge.n_menge>0";
							Session session2 = FLRSessionFactory.getFactory().openSession();
							Query query = session2.createQuery(queryf);
							List<?> results = query.list();

							data[i][OFFENE_AG_LOSHATFEHLMENGE] = new Boolean(false);
							if (results.size() > 0) {
								BigDecimal bd = (BigDecimal) results.iterator().next();

								if (bd != null && bd.doubleValue() > 0) {
									data[i][OFFENE_AG_LOSHATFEHLMENGE] = new Boolean(true);
								}
							}
							session2.close();
						}
					} else if (kostenstelleIId != null) {
						if (los.getKostenstelle_i_id().intValue() != kostenstelleIId.intValue()) {
							i--;
						} else {
							data[i][OFFENE_AG_AGNUMMER] = a.getI_arbeitsgangsnummer();
							data[i][OFFENE_AG_UAGNUMMER] = a.getI_unterarbeitsgang();
							data[i][OFFENE_AG_AG_ARTIKEL] = a.getFlrartikel().getC_nr();
							ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(a.getFlrartikel().getI_id(),
									theClientDto);
							data[i][OFFENE_AG_AG_ARTIKELBEZEICHNUNG] = aDto.formatBezeichnung();

							data[i][OFFENE_AG_FORTSCHRITT] = a.getF_fortschritt();

							if (Helper.short2boolean(a.getB_fertig())) {
								data[i][OFFENE_AG_AG_GESAMTZEIT] = new BigDecimal(0);
							} else {
								data[i][OFFENE_AG_AG_GESAMTZEIT] = a.getN_gesamtzeit();
							}
							if (bSollstundenbetrachtung == true) {
								data[i][OFFENE_AG_AG_ISTZEIT] = getZeiterfassungFac().getSummeZeitenEinesBeleges(
										LocaleFac.BELEGART_LOS, los.getI_id(), a.getI_id(), null, null, null,
										theClientDto);

							}
							if (a.getFlrmaschine() != null) {
								data[i][OFFENE_AG_AG_MASCHINE_BEZEICHNUNG] = a.getFlrmaschine().getC_bez();
								data[i][OFFENE_AG_AG_MASCHINE_IDENTIFIKATIONSNUMMMER] = a.getFlrmaschine()
										.getC_identifikationsnr();
								data[i][OFFENE_AG_AG_MASCHINE_INVENTARNUMMMER] = a.getFlrmaschine()
										.getC_inventarnummer();
							}
							if (a.getFlrartikel().getFlrartikelgruppe() != null) {
								data[i][OFFENE_AG_AG_ARTIKELGRUPPPE] = a.getFlrartikel().getFlrartikelgruppe()
										.getC_nr();
							} else {
								data[i][OFFENE_AG_AG_ARTIKELGRUPPPE] = "";
							}

							if (a.getI_maschinenversatztage() != null) {
								data[i][OFFENE_AG_AG_BEGINN] = Helper.addiereTageZuTimestamp(
										(Timestamp) los.getT_produktionsbeginn(), a.getI_maschinenversatztage());

							} else {
								data[i][OFFENE_AG_AG_BEGINN] = los.getT_produktionsbeginn();

							}

							if (los.getFlrauftrag() != null) {

								Integer partnerIId = los.getFlrauftrag().getFlrkunde().getFlrpartner().getI_id();
								PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(partnerIId,
										theClientDto);
								data[i][OFFENE_AG_KUNDE] = partnerDto.formatFixTitelName1Name2();
								data[i][OFFENE_AG_AUFTRAGSPOENALE] = los.getFlrauftrag().getB_poenale();
							}

							if (los.getFlrauftrag() != null) {
								data[i][OFFENE_AG_AUFTRAGSNUMMER] = los.getFlrauftrag().getC_nr();
								data[i][OFFENE_AG_PROJEKT] = los.getFlrauftrag().getC_bez();
								data[i][OFFENE_AG_LIEFERTERMIN] = los.getFlrauftrag().getT_liefertermin();
							}
							BigDecimal bdGeliefert = new BigDecimal(0);
							for (Iterator<?> iter2 = los.getAblieferungset().iterator(); iter2.hasNext();) {
								FLRLosablieferung item = (FLRLosablieferung) iter2.next();
								bdGeliefert = bdGeliefert.add(item.getN_menge());
							}
							data[i][OFFENE_AG_GELIEFERT] = bdGeliefert;
							if (los.getFlrstueckliste() != null) {
								ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
										los.getFlrstueckliste().getFlrartikel().getI_id(), theClientDto);
								data[i][OFFENE_AG_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
								data[i][OFFENE_AG_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCZbez();
								data[i][OFFENE_AG_ARTIKELNUMMER] = los.getFlrstueckliste().getFlrartikel().getC_nr();

								data[i][OFFENE_AG_DETAILBEDARF] = getRahmenbedarfeFac()
										.getSummeAllerRahmenbedarfeEinesArtikels(artikelDto.getIId());

								// Offene Fehlmengen
								data[i][OFFENE_AG_FEHLMENGE] = getFehlmengeFac()
										.getAnzahlFehlmengeEinesArtikels(artikelDto.getIId(), theClientDto);

								LossollarbeitsplanDto[] lossollarbeitsplanDto = getFertigungFac()
										.lossollarbeitsplanFindByLosIId(los.getI_id());

								BigDecimal bdFertigungszeit = new BigDecimal(0);
								for (int j = 0; lossollarbeitsplanDto.length > j; j++) {
									bdFertigungszeit = bdFertigungszeit.add(lossollarbeitsplanDto[j].getNGesamtzeit());
								}
								data[i][OFFENE_AG_FERTIGUNGSZEIT] = bdFertigungszeit;

								// Rahmenbestellt
								Hashtable<?, ?> htRahmenbestellt = getArtikelbestelltFac()
										.getAnzahlRahmenbestellt(artikelDto.getIId(), theClientDto);
								if (htRahmenbestellt.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
									BigDecimal bdAnzahlRahmenbestellt = (BigDecimal) htRahmenbestellt
											.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
									data[i][OFFENE_AG_RAHMEN_BESTELLT] = bdAnzahlRahmenbestellt;
								}
								data[i][OFFENE_AG_RESERVIERUNGEN] = getReservierungFac()
										.getAnzahlReservierungen(artikelDto.getIId(), theClientDto);

								data[i][OFFENE_AG_RAHMENRESERVIERUNGEN] = getReservierungFac()
										.getAnzahlRahmenreservierungen(artikelDto.getIId(), theClientDto);

							} else {
								data[i][OFFENE_AG_BEZEICHNUNG] = los.getC_projekt();
								data[i][OFFENE_AG_ZUSATZBEZEICHNUNG] = null;
								data[i][OFFENE_AG_ARTIKELNUMMER] = getTextRespectUISpr("fert.materialliste",
										theClientDto.getMandant(), theClientDto.getLocUi());
							}

							GregorianCalendar gc = new GregorianCalendar();
							gc.setTime(los.getT_produktionsbeginn());
							data[i][OFFENE_AG_KALENDERWOCHE] = new Integer(gc.get(GregorianCalendar.WEEK_OF_YEAR));
							data[i][OFFENE_AG_LOSGROESSE] = los.getN_losgroesse();
							data[i][OFFENE_AG_LOSNUMMER] = los.getC_nr();
							data[i][OFFENE_AG_FERTIGUNGSGRUPPE] = los.getFlrfertigungsgruppe().getC_bez();
							/**
							 * @todo material PJ 4239
							 */
							data[i][OFFENE_AG_MATERIAL] = null;
							data[i][OFFENE_AG_BEGINN] = los.getT_produktionsbeginn();
							data[i][OFFENE_AG_ENDE] = los.getT_produktionsende();
							data[i][OFFENE_AG_LOSSTATUS] = los.getStatus_c_nr();
							data[i][OFFENE_AG_LOSZUSATZSTATUS_P1] = getFertigungFac()
									.hatLosZusatzstatusP1(los.getI_id());
							// PJ 15009

							String queryf = "select sum(flrfehlmenge.n_menge) FROM FLRFehlmenge flrfehlmenge WHERE flrfehlmenge.flrlossollmaterial.flrlos = "
									+ los.getI_id() + " AND flrfehlmenge.n_menge>0";
							Session session2 = FLRSessionFactory.getFactory().openSession();
							Query query = session2.createQuery(queryf);
							List<?> results = query.list();

							data[i][OFFENE_AG_LOSHATFEHLMENGE] = new Boolean(false);
							if (results.size() > 0) {
								BigDecimal bd = (BigDecimal) results.iterator().next();

								if (bd != null && bd.doubleValue() > 0) {
									data[i][OFFENE_AG_LOSHATFEHLMENGE] = new Boolean(true);
								}
							}
							session2.close();
						}
					}
				} else {

					data[i][OFFENE_AG_AGNUMMER] = a.getI_arbeitsgangsnummer();
					data[i][OFFENE_AG_UAGNUMMER] = a.getI_unterarbeitsgang();
					data[i][OFFENE_AG_AG_ARTIKEL] = a.getFlrartikel().getC_nr();
					ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(a.getFlrartikel().getI_id(),
							theClientDto);
					data[i][OFFENE_AG_AG_ARTIKELBEZEICHNUNG] = aDto.formatBezeichnung();

					data[i][OFFENE_AG_FORTSCHRITT] = a.getF_fortschritt();

					if (Helper.short2boolean(a.getB_fertig())) {
						data[i][OFFENE_AG_AG_GESAMTZEIT] = new BigDecimal(0);
					} else {
						data[i][OFFENE_AG_AG_GESAMTZEIT] = a.getN_gesamtzeit();
					}
					if (bSollstundenbetrachtung == true) {
						data[i][OFFENE_AG_AG_ISTZEIT] = getZeiterfassungFac().getSummeZeitenEinesBeleges(
								LocaleFac.BELEGART_LOS, los.getI_id(), a.getI_id(), null, null, null, theClientDto);

					}
					if (a.getFlrmaschine() != null) {
						data[i][OFFENE_AG_AG_MASCHINE_BEZEICHNUNG] = a.getFlrmaschine().getC_bez();
						data[i][OFFENE_AG_AG_MASCHINE_IDENTIFIKATIONSNUMMMER] = a.getFlrmaschine()
								.getC_identifikationsnr();
						data[i][OFFENE_AG_AG_MASCHINE_INVENTARNUMMMER] = a.getFlrmaschine().getC_inventarnummer();
					}
					if (a.getFlrartikel().getFlrartikelgruppe() != null) {
						data[i][OFFENE_AG_AG_ARTIKELGRUPPPE] = a.getFlrartikel().getFlrartikelgruppe().getC_nr();
					} else {
						data[i][OFFENE_AG_AG_ARTIKELGRUPPPE] = "";
					}

					if (a.getI_maschinenversatztage() != null) {
						data[i][OFFENE_AG_AG_BEGINN] = Helper.addiereTageZuTimestamp(
								(Timestamp) los.getT_produktionsbeginn(), a.getI_maschinenversatztage());

					} else {
						data[i][OFFENE_AG_AG_BEGINN] = los.getT_produktionsbeginn();

					}

					if (los.getFlrauftrag() != null) {

						Integer partnerIId = los.getFlrauftrag().getFlrkunde().getFlrpartner().getI_id();
						PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(partnerIId, theClientDto);
						data[i][OFFENE_AG_KUNDE] = partnerDto.formatFixTitelName1Name2();
						data[i][OFFENE_AG_AUFTRAGSPOENALE] = los.getFlrauftrag().getB_poenale();
					}

					if (los.getFlrauftrag() != null) {
						data[i][OFFENE_AG_AUFTRAGSNUMMER] = los.getFlrauftrag().getC_nr();
						data[i][OFFENE_AG_PROJEKT] = los.getFlrauftrag().getC_bez();
						data[i][OFFENE_AG_LIEFERTERMIN] = los.getFlrauftrag().getT_liefertermin();
					}
					BigDecimal bdGeliefert = new BigDecimal(0);
					for (Iterator<?> iter2 = los.getAblieferungset().iterator(); iter2.hasNext();) {
						FLRLosablieferung item = (FLRLosablieferung) iter2.next();
						bdGeliefert = bdGeliefert.add(item.getN_menge());
					}
					data[i][OFFENE_AG_GELIEFERT] = bdGeliefert;
					if (los.getFlrstueckliste() != null) {
						ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
								los.getFlrstueckliste().getFlrartikel().getI_id(), theClientDto);

						if (artikelDto.getArtikelsprDto() != null) {
							data[i][OFFENE_AG_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
							data[i][OFFENE_AG_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCZbez();
						}
						data[i][OFFENE_AG_ARTIKELNUMMER] = los.getFlrstueckliste().getFlrartikel().getC_nr();

						data[i][OFFENE_AG_DETAILBEDARF] = getRahmenbedarfeFac()
								.getSummeAllerRahmenbedarfeEinesArtikels(artikelDto.getIId());

						// Offene Fehlmengen
						data[i][OFFENE_AG_FEHLMENGE] = getFehlmengeFac()
								.getAnzahlFehlmengeEinesArtikels(artikelDto.getIId(), theClientDto);

						LossollarbeitsplanDto[] lossollarbeitsplanDto = getFertigungFac()
								.lossollarbeitsplanFindByLosIId(los.getI_id());

						BigDecimal bdFertigungszeit = new BigDecimal(0);
						for (int j = 0; lossollarbeitsplanDto.length > j; j++) {
							bdFertigungszeit = bdFertigungszeit.add(lossollarbeitsplanDto[j].getNGesamtzeit());
						}
						data[i][OFFENE_AG_FERTIGUNGSZEIT] = bdFertigungszeit;

						// Rahmenbestellt
						Hashtable<?, ?> htRahmenbestellt = getArtikelbestelltFac()
								.getAnzahlRahmenbestellt(artikelDto.getIId(), theClientDto);
						if (htRahmenbestellt.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
							BigDecimal bdAnzahlRahmenbestellt = (BigDecimal) htRahmenbestellt
									.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
							data[i][OFFENE_AG_RAHMEN_BESTELLT] = bdAnzahlRahmenbestellt;
						}
						data[i][OFFENE_AG_RESERVIERUNGEN] = getReservierungFac()
								.getAnzahlReservierungen(artikelDto.getIId(), theClientDto);

						data[i][OFFENE_AG_RAHMENRESERVIERUNGEN] = getReservierungFac()
								.getAnzahlRahmenreservierungen(artikelDto.getIId(), theClientDto);

					} else {
						data[i][OFFENE_AG_BEZEICHNUNG] = los.getC_projekt();
						data[i][OFFENE_AG_ZUSATZBEZEICHNUNG] = null;
						data[i][OFFENE_AG_ARTIKELNUMMER] = getTextRespectUISpr("fert.materialliste",
								theClientDto.getMandant(), theClientDto.getLocUi());
					}

					GregorianCalendar gc = new GregorianCalendar();
					gc.setTime(los.getT_produktionsbeginn());
					data[i][OFFENE_AG_KALENDERWOCHE] = new Integer(gc.get(GregorianCalendar.WEEK_OF_YEAR));
					data[i][OFFENE_AG_LOSGROESSE] = los.getN_losgroesse();
					data[i][OFFENE_AG_LOSNUMMER] = los.getC_nr();
					data[i][OFFENE_AG_FERTIGUNGSGRUPPE] = los.getFlrfertigungsgruppe().getC_bez();
					/**
					 * @todo material PJ 4239
					 */
					data[i][OFFENE_AG_MATERIAL] = null;
					data[i][OFFENE_AG_BEGINN] = los.getT_produktionsbeginn();
					data[i][OFFENE_AG_ENDE] = los.getT_produktionsende();
					data[i][OFFENE_AG_LOSSTATUS] = los.getStatus_c_nr();
					data[i][OFFENE_AG_LOSZUSATZSTATUS_P1] = getFertigungFac().hatLosZusatzstatusP1(los.getI_id());
					// PJ 15009

					String queryf = "select sum(flrfehlmenge.n_menge) FROM FLRFehlmenge flrfehlmenge WHERE flrfehlmenge.flrlossollmaterial.flrlos = "
							+ los.getI_id() + " AND flrfehlmenge.n_menge>0";
					Session session2 = FLRSessionFactory.getFactory().openSession();
					Query query = session2.createQuery(queryf);
					List<?> results = query.list();

					data[i][OFFENE_AG_LOSHATFEHLMENGE] = new Boolean(false);
					if (results.size() > 0) {
						BigDecimal bd = (BigDecimal) results.iterator().next();

						if (bd != null && bd.doubleValue() > 0) {
							data[i][OFFENE_AG_LOSHATFEHLMENGE] = new Boolean(true);
						}
					}
					session2.close();
				}
			}
			// Sortieren nach Artikelgruppe + AG-Beginn

			for (int k = data.length - 1; k > 0; --k) {
				for (int j = 0; j < k; ++j) {
					Object[] a1 = (Object[]) data[j];
					Object[] a2 = (Object[]) data[j + 1];

					if (((String) a1[OFFENE_AG_AG_ARTIKELGRUPPPE])
							.compareTo((String) a2[OFFENE_AG_AG_ARTIKELGRUPPPE]) > 0) {
						data[j] = a2;
						data[j + 1] = a1;
					} else if (((String) a1[OFFENE_AG_AG_ARTIKELGRUPPPE])
							.compareTo((String) a2[OFFENE_AG_AG_ARTIKELGRUPPPE]) == 0) {
						java.util.Date k1 = (java.util.Date) a1[OFFENE_AG_AG_BEGINN];
						java.util.Date k2 = (java.util.Date) a2[OFFENE_AG_AG_BEGINN];

						if (iOptionStichtag == FertigungReportFac.OFFENE_OPTION_STICHTAG_ENDEDATUM) {
							k1 = (java.util.Date) a1[OFFENE_AG_ENDE];
							k2 = (java.util.Date) a2[OFFENE_AG_ENDE];
						} else if (iOptionStichtag == FertigungReportFac.OFFENE_OPTION_STICHTAG_LIEFERTERMIN) {
							k1 = (java.util.Date) a1[OFFENE_AG_LIEFERTERMIN];

							if (k1 == null) {
								k1 = new Date(0);
							}

							k2 = (java.util.Date) a2[OFFENE_AG_LIEFERTERMIN];

							if (k2 == null) {
								k2 = new Date(0);
							}

						}

						k1 = Helper.cutDate(k1);
						k2 = Helper.cutDate(k2);

						if (k1.after(k2)) {
							data[j] = a2;
							data[j + 1] = a1;
						} else if (k1.equals(k2)) {

							String l1 = (String) a1[OFFENE_AG_LOSNUMMER];
							String l2 = (String) a1[OFFENE_AG_LOSNUMMER];
							if (l1.compareTo(l2) > 0) {
								data[j] = a2;
								data[j + 1] = a1;
							}

						}
					}
				}
			}

			initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_OFFENE_AG,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
			return getReportPrint();
		} catch (Exception t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, t);
		} finally {
			closeSession(session);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printRankingliste(TheClientDto theClientDto) {

		this.useCase = UC_RANKINGLISTE;
		this.index = -1;

		Map<String, Object> mapParameter = new TreeMap<String, Object>();
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();
		session.enableFilter("filterMandant").setParameter("paramMandant", theClientDto.getMandant());
		session.enableFilter("filterLocale").setParameter("paramLocale", Helper.locale2String(theClientDto.getLocUi()));

		ZusatzstatusDto zusatzstatusDto = null;
		try {
			ParametermandantDto parametermandantDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_RANKINGLISTE_ZUSATZSTATUS);
			if (((java.lang.Integer) parametermandantDto.getCWertAsObject()) != null) {
				zusatzstatusDto = getFertigungFac()
						.zusatzstatusFindByPrimaryKey((java.lang.Integer) parametermandantDto.getCWertAsObject());

			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

		String query = "SELECT a.flrartikel.i_id,a.flrartikel.c_nr, sum(a.n_menge), (SELECT s.i_id FROM FLRStueckliste s WHERE s.artikel_i_id=a.flrartikel.i_id AND a.flrartikel.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "') AS stkl_i_id ,  (SELECT COUNT(*) FROM FLRStuecklisteposition sp WHERE sp.flrartikel.i_id=a.flrartikel.i_id ),  (SELECT SUM(fm.n_menge) FROM FLRFehlmenge fm WHERE fm.flrartikel.i_id=a.flrartikel.i_id ),  (SELECT SUM(al.n_lagerstand) FROM FLRArtikellager al WHERE al.compId.artikel_i_id=a.flrartikel.i_id ), (SELECT spr.c_bez FROM FLRArtikellistespr spr WHERE spr.Id.artikelliste=a.flrartikel.i_id AND spr.Id.locale='"
				+ theClientDto.getLocUiAsString()
				+ "')  FROM FLRArtikelreservierung a WHERE a.flrartikel.mandant_c_nr='" + theClientDto.getMandant()
				+ "'  GROUP BY a.flrartikel.i_id,a.flrartikel.mandant_c_nr,a.flrartikel.c_nr ORDER BY a.flrartikel.c_nr ASC";

		Query qResult = session.createQuery(query);

		List<?> results = qResult.list();

		ArrayList<Object[]> alDaten = new ArrayList<Object[]>();

		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();
			Object[] oZeile = new Object[11];

			BigDecimal bdReserviert = (BigDecimal) o[2];
			if (bdReserviert == null) {
				bdReserviert = new BigDecimal(0.00);
			}
			BigDecimal bdLagerstand = (BigDecimal) o[6];
			if (bdLagerstand == null) {
				bdLagerstand = new BigDecimal(0.00);
			}
			BigDecimal bdFehlmengen = (BigDecimal) o[5];
			if (bdFehlmengen == null) {
				bdFehlmengen = new BigDecimal(0.00);
			}

			if (o[3] != null) {

				Session session2 = factory.openSession();

				String subQuery = "SELECT l.c_nr,partner.c_name1nachnamefirmazeile1, l.t_produktionsbeginn, l.n_losgroesse, "
						+ " (SELECT SUM(abl.n_menge) FROM FLRLosablieferung abl WHERE abl.los_i_id=l.i_id), "
						+ " (SELECT SUM(sa.n_gesamtzeit) FROM FLRLossollarbeitsplan sa WHERE sa.los_i_id=l.i_id), "
						+ "(SELECT ls.i_id FROM FLRLoszusatzstatus ls WHERE ls.los_i_id=l.i_id AND ls.zusatzstatus_i_id="
						+ zusatzstatusDto.getIId() + ") , fg.c_bez, l.flrstueckliste.flrartikel.c_nr "
						+ " FROM FLRLosReport l LEFT JOIN l.flrauftrag.flrkunde.flrpartner AS partner LEFT JOIN l.flrfertigungsgruppe AS fg  WHERE l.flrstueckliste.i_id="
						+ o[3] + " AND l.status_c_nr IN ('" + FertigungFac.STATUS_AUSGEGEBEN + "','"
						+ FertigungFac.STATUS_IN_PRODUKTION + "') ORDER BY l.t_produktionsbeginn ASC ";
				Query subResult = session2.createQuery(subQuery);

				List<?> subResults = subResult.list();
				Iterator<?> subResultListIterator = subResults.iterator();
				while (subResultListIterator.hasNext()) {
					Object[] flrLosReport = (Object[]) subResultListIterator.next();

					oZeile = new Object[11];

					oZeile[RANKINGLISTE_LOSNR] = flrLosReport[0];
					oZeile[RANKINGLISTE_LOSBEGINNTERMIN] = flrLosReport[2];

					BigDecimal offeneMenge = new BigDecimal(0.00);
					BigDecimal losgroesse = (BigDecimal) flrLosReport[3];

					BigDecimal summeAblieferungen = (BigDecimal) flrLosReport[4];
					if (summeAblieferungen == null) {
						summeAblieferungen = new BigDecimal(0.00);
					}

					offeneMenge = losgroesse.subtract(summeAblieferungen);

					oZeile[RANKINGLISTE_OFFENELOSMENGE] = offeneMenge;

					if (offeneMenge.doubleValue() > 0) {

						BigDecimal gesamt = (BigDecimal) flrLosReport[5];
						if (gesamt == null) {
							gesamt = new BigDecimal(0.00);
						}

						oZeile[RANKINGLISTE_SOLLZEIT] = gesamt.divide(losgroesse, 4, BigDecimal.ROUND_HALF_EVEN)
								.multiply(offeneMenge);
						// Unterstueckliste?
						if (o[4] != null && ((Long) o[4]).intValue() > 0) {
							oZeile[RANKINGLISTE_INFO_U] = new Boolean(true);
						} else {
							oZeile[RANKINGLISTE_INFO_U] = new Boolean(false);
						}

						if (bdReserviert.add(bdFehlmengen).doubleValue() > bdLagerstand.doubleValue()) {
							oZeile[RANKINGLISTE_INFO_L] = new Boolean(true);
						} else {
							oZeile[RANKINGLISTE_INFO_L] = new Boolean(false);
						}

						if (flrLosReport[7] != null) {
							oZeile[RANKINGLISTE_FERTIGUNGSGRUPPE] = flrLosReport[7];
						} else {
							oZeile[RANKINGLISTE_FERTIGUNGSGRUPPE] = "";
						}

						oZeile[RANKINGLISTE_ARTIKELNUMMER] = flrLosReport[8];
						oZeile[RANKINGLISTE_ARTIKELBEZEICHNUNG] = o[7];

						oZeile[RANKINGLISTE_KUNDE] = flrLosReport[1];

						if (flrLosReport[6] != null) {

							oZeile[RANKINGLISTE_INFO_K] = new Boolean(true);
						} else {
							// nix gefunden
							oZeile[RANKINGLISTE_INFO_K] = new Boolean(false);
						}

						alDaten.add(oZeile);

						oZeile = new Object[11];
					}

				}

				session2.close();
			}
		}

		session.close();

		// Nach Fertigungsgruppe sortieren
		for (int k = alDaten.size() - 1; k > 0; --k) {
			for (int j = 0; j < k; ++j) {
				Object[] a1 = (Object[]) alDaten.get(j);
				Object[] a2 = (Object[]) alDaten.get(j + 1);

				String s1 = Helper.fitString2Length((String) a1[RANKINGLISTE_FERTIGUNGSGRUPPE], 40, ' ')
						+ a1[RANKINGLISTE_LOSBEGINNTERMIN];
				String s2 = Helper.fitString2Length((String) a2[RANKINGLISTE_FERTIGUNGSGRUPPE], 40, ' ')
						+ a2[RANKINGLISTE_LOSBEGINNTERMIN];

				if (s1.compareTo(s2) > 0) {
					alDaten.set(j, a2);
					alDaten.set(j + 1, a1);
				}
			}

		}

		data = new Object[alDaten.size()][12];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_RANKINGLISTE,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printHalbfertigfabrikatsinventur(java.sql.Timestamp tsStichtag, int iSortierung,
			boolean bVerdichtet, Integer partnerIIdFertigungsort, boolean bSortiertNachFertigungsgruppe,
			boolean bNurMaterialwerte, boolean referenznummerStattArtikelnummer, TheClientDto theClientDto)
			throws EJBExceptionLP {

		try {
			Map<String, Object> mapParameter = new TreeMap<String, Object>();

			if (tsStichtag != null) {
				mapParameter.put("P_STICHTAG", tsStichtag);
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(tsStichtag.getTime());
				c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);
				tsStichtag = new java.sql.Timestamp(c.getTimeInMillis());
				tsStichtag = Helper.cutTimestamp(tsStichtag);
			}

			data = getDataHalbfertigfabrikatsinventur(tsStichtag, iSortierung, bVerdichtet, partnerIIdFertigungsort,
					bSortiertNachFertigungsgruppe, bNurMaterialwerte, referenznummerStattArtikelnummer, theClientDto);

			mapParameter.put(LPReport.P_WAEHRUNG, theClientDto.getSMandantenwaehrung());
			mapParameter.put("P_VERDICHTET", new Boolean(bVerdichtet));
			mapParameter.put("P_SORTIERT_NACH_FERTIGUNGSGRUPPE", new Boolean(bSortiertNachFertigungsgruppe));

			mapParameter.put("P_REFERENZNUMMER_STATT_ARTIKELNUMMER", new Boolean(referenznummerStattArtikelnummer));

			mapParameter.put("P_NURMATERIALWERTE", new Boolean(bNurMaterialwerte));

			if (iSortierung == FertigungReportFac.HF_OPTION_SORTIERUNG_LOSNR) {
				mapParameter.put("P_SORTIERUNG", "Losnummer");
			} else if (iSortierung == FertigungReportFac.HF_OPTION_SORTIERUNG_ARTIKELNR) {
				mapParameter.put("P_SORTIERUNG", "Artikelnummer");
			} else if (iSortierung == FertigungReportFac.HF_OPTION_SORTIERUNG_AUFTRAGNR) {
				mapParameter.put("P_SORTIERUNG", "Auftragsnummer");
			}

			initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL,
					FertigungReportFac.REPORT_HALBFERTIGFABRIKATSINVENTUR, theClientDto.getMandant(),
					theClientDto.getLocUi(), theClientDto);

			return getReportPrint();
		} catch (RemoteException t) {
			throwEJBExceptionLPRespectOld(t);
		}
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public Object[][] getDataHalbfertigfabrikatsinventur(java.sql.Timestamp tsStichtag, int iSortierung,
			boolean bVerdichtet, Integer partnerIIdFertigungsort, boolean bSortiertNachFertigungsgruppe,
			boolean bNurMaterialwerte, boolean referenznummerStattArtikelnummer, TheClientDto theClientDto)
			throws RemoteException {

		this.useCase = UC_HALBFERTIGFABRIKATSINVENTUR;
		this.index = -1;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();
		Criteria c = session.createCriteria(FLRLosReport.class);
		c.add(Restrictions.eq(FertigungFac.FLR_LOS_MANDANT_C_NR, theClientDto.getMandant()));

		// c.add(Restrictions.eq("c_nr", "19/1730"));

		if (partnerIIdFertigungsort != null) {
			c.add(Restrictions.eq(FertigungFac.FLR_LOSREPORT_PARTNER_I_ID_FERTIGUNGSORT, partnerIIdFertigungsort));
		}

		if (tsStichtag == null) {
			c.add(Restrictions.not(Restrictions.in(FertigungFac.FLR_LOS_STATUS_C_NR,
					new String[] { FertigungFac.STATUS_STORNIERT, FertigungFac.STATUS_ERLEDIGT,
							FertigungFac.STATUS_ANGELEGT, FertigungFac.STATUS_GESTOPPT })));
		} else {
			c.add(Restrictions.not(Restrictions.in(FertigungFac.FLR_LOS_STATUS_C_NR,
					new String[] { FertigungFac.STATUS_STORNIERT, FertigungFac.STATUS_GESTOPPT })));

			c.add(Restrictions.or(Restrictions.gt(FertigungFac.FLR_LOS_T_ERLEDIGT, tsStichtag),
					Restrictions.isNull(FertigungFac.FLR_LOS_T_ERLEDIGT)));

			c.add(Restrictions.or(Restrictions.gt(FertigungFac.FLR_LOSREPORT_T_MANUELLERLEDIGT, tsStichtag),
					Restrictions.isNull(FertigungFac.FLR_LOSREPORT_T_MANUELLERLEDIGT)));

			c.add(Restrictions.le(FertigungFac.FLR_LOS_T_AUSGABE, tsStichtag));
		}
		// Sortierung nach Losnummer

		c.addOrder(Order.asc(FertigungFac.FLR_LOS_C_NR));

		MaschineDto[] maschineDtos = getZeiterfassungFac().maschineFindByMandantCNr(theClientDto.getMandant());

		List<?> list = c.list();

		ArrayList<Object[]> alDaten = new ArrayList<Object[]>();
		int i = 0;
		for (Iterator<?> iter = list.iterator(); iter.hasNext(); i++) {
			FLRLosReport item = (FLRLosReport) iter.next();

			Object[] zeileVorlage = new Object[HF_ANZAHL_SPALTEN];

			zeileVorlage[HF_LOSNUMMER] = item.getC_nr();
			zeileVorlage[HF_LOSGROESSE] = item.getN_losgroesse();
			// zeile[HF_AUFTRAGSNUMMER] = "";
			// PJ18443
			if (item.getFlrauftrag() != null) {
				zeileVorlage[HF_AUFTRAGSNUMMER] = item.getFlrauftrag().getC_nr();
				zeileVorlage[HF_AUFTRAGSSTATUS] = item.getFlrauftrag().getAuftragstatus_c_nr();

				zeileVorlage[HF_AUFTRAGSKUNDE] = HelperServer
						.formatNameAusFLRPartner(item.getFlrauftrag().getFlrkunde().getFlrpartner());
				zeileVorlage[HF_AUFTRAGSKUNDEKURZBEZEICHNUNG] = item.getFlrauftrag().getFlrkunde().getFlrpartner()
						.getC_kbez();
				if (item.getFlrauftragposition() != null) {
					zeileVorlage[HF_AUFTRAGSPOSITIONSSTATUS] = item.getFlrauftragposition()
							.getAuftragpositionstatus_c_nr();
				}

			} else {
				if (item.getFlrauftragposition() != null) {
					zeileVorlage[HF_AUFTRAGSNUMMER] = item.getFlrauftragposition().getFlrauftrag().getC_nr();
					zeileVorlage[HF_AUFTRAGSPOSITIONSSTATUS] = item.getFlrauftragposition()
							.getAuftragpositionstatus_c_nr();
					zeileVorlage[HF_AUFTRAGSSTATUS] = item.getFlrauftragposition().getFlrauftrag()
							.getAuftragstatus_c_nr();
					zeileVorlage[HF_AUFTRAGSKUNDE] = HelperServer.formatNameAusFLRPartner(
							item.getFlrauftragposition().getFlrauftrag().getFlrkunde().getFlrpartner());
					zeileVorlage[HF_AUFTRAGSKUNDEKURZBEZEICHNUNG] = item.getFlrauftragposition().getFlrauftrag()
							.getFlrkunde().getFlrpartner().getC_kbez();
				}
			}

			if (item.getFlrfertigungsgruppe() != null) {
				zeileVorlage[HF_FERTIGUNGSGRUPPE] = item.getFlrfertigungsgruppe().getC_bez();
			} else {
				zeileVorlage[HF_FERTIGUNGSGRUPPE] = "";
			}

			if (item.getFlrstueckliste() != null) {
				zeileVorlage[HF_ARTIKELNUMMER] = item.getFlrstueckliste().getFlrartikel().getC_nr();
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(item.getFlrstueckliste().getFlrartikel().getI_id(), theClientDto);
				zeileVorlage[HF_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
				zeileVorlage[HF_KURZBEZEICHNUNG] = artikelDto.getKbezAusSpr();
				zeileVorlage[HF_REFERENZNUMMER] = artikelDto.getCReferenznr();
				zeileVorlage[HF_EINHEIT] = artikelDto.getEinheitCNr();
				zeileVorlage[HF_UMRECHNUNGSFAKTOR] = artikelDto.getNUmrechnungsfaktor();
				zeileVorlage[HF_BESTELLMENGENEINHEIT] = artikelDto.getEinheitCNrBestellung();
			} else {
				zeileVorlage[HF_ARTIKELNUMMER] = getTextRespectUISpr("fert.materialliste", theClientDto.getMandant(),
						theClientDto.getLocUi());
				zeileVorlage[HF_BEZEICHNUNG] = item.getC_projekt();
			}

			LosablieferungDto[] losablieferungDtos = getFertigungFac().losablieferungFindByLosIId(item.getI_id(), true,
					theClientDto);
			BigDecimal bdAbgeliefert = new BigDecimal(0.0000);
			for (int j = 0; j < losablieferungDtos.length; j++) {

				if (tsStichtag == null) {
					bdAbgeliefert = bdAbgeliefert.add(losablieferungDtos[j].getNMenge());
				} else {
					if (tsStichtag.after(losablieferungDtos[j].getTAendern())) {
						bdAbgeliefert = bdAbgeliefert.add(losablieferungDtos[j].getNMenge());
					}
				}

			}
			zeileVorlage[HF_ERLEDIGT] = bdAbgeliefert;

			// Nun eine Zeile pro Position

			// Ausgegebenes Material
			LossollmaterialDto[] sollmat = getFertigungFac().lossollmaterialFindByLosIId(item.getI_id());

			for (int j = 0; j < sollmat.length; j++) {
				BigDecimal bdMenge = getFertigungFac().getAusgegebeneMenge(sollmat[j].getIId(), tsStichtag,
						theClientDto);

				Object[] zeile = zeileVorlage.clone();

				zeile[HF_POSITION_AUSGEGEBEN] = bdMenge;

				// Einkaufspreis des ersten Lieferanten hinzufuegen
				ArtikellieferantDto dto = getArtikelFac().getArtikelEinkaufspreis(sollmat[j].getArtikelIId(), null,
						new BigDecimal(1), theClientDto.getSMandantenwaehrung(),
						new java.sql.Date(sollmat[j].getTAendern().getTime()), theClientDto);
				if (dto != null) {
					zeile[HF_POSITION_EKPREIS] = dto.getLief1Preis();
				}

				zeile[HF_POSITION_ARTIKELVERWENDUNG] = "Material";

				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(sollmat[j].getArtikelIId(),
						theClientDto);
				zeile[HF_POSITION_ARTIKELNUMMMER] = artikelDto.getCNr();
				zeile[HF_POSITION_REFERENZNUMMER] = artikelDto.getCReferenznr();
				zeile[HF_POSITION_BEZEICHNUNG] = artikelDto.formatBezeichnung();
				zeile[HF_POSITION_EINHEIT] = artikelDto.getEinheitCNr();

				zeile[HF_POSITION_UMRECHNUNGSFAKTOR] = artikelDto.getNUmrechnungsfaktor();
				zeile[HF_POSITION_BESTELLMENGENEINHEIT] = artikelDto.getEinheitCNrBestellung();

				// CK:2008-12-23 Wegen Beistellteilen ist die Verfaelschung
				// des Gestehungspreises falsch
				// es muss immer der Gestehungspreis zum ausgabezeitpunkt
				// verwendet werden.
				BigDecimal bdPreis = getFertigungFac().getAusgegebeneMengePreis(sollmat[j].getIId(), tsStichtag,
						theClientDto);

				if (bNurMaterialwerte == true) {

					BigDecimal bdGesamtwert = BigDecimal.ZERO;

					Session sessionIstMat = factory.openSession();

					String query = "SELECT li.i_id,li.b_abgang FROM FLRLosistmaterial li WHERE li.lossollmaterial_i_id="
							+ sollmat[j].getIId();

					org.hibernate.Query qResult = sessionIstMat.createQuery(query);
					List<?> results = qResult.list();

					Iterator<?> resultListIterator = results.iterator();
					while (resultListIterator.hasNext()) {

						Object[] o = (Object[]) resultListIterator.next();

						if (Helper.short2boolean((Short) o[1]) == true) {

							LosistmaterialDto liDto = getFertigungFac().losistmaterialFindByPrimaryKey((Integer) o[0]);

							ArrayList<WarenzugangsreferenzDto> weReferenz = getLagerFac().getWareneingangsreferenz(
									LocaleFac.BELEGART_LOS, liDto.getIId(),
									getLagerFac().getAllSeriennrchargennrEinerBelegartpositionOhneChargeneigenschaften(
											LocaleFac.BELEGART_LOS, liDto.getIId()),
									false, theClientDto);

							for (int m = 0; m < weReferenz.size(); m++) {
								WarenzugangsreferenzDto wa = weReferenz.get(m);

								if (wa.getBelegart() != null
										&& wa.getBelegart().equals(LocaleFac.BELEGART_LOSABLIEFERUNG)
										&& wa.getMenge().doubleValue() > 0) {

									GesamtkalkulationDto gkDto = new GesamtkalkulationDto();
									gkDto = getFertigungReportFac().getDatenGesamtkalkulation(wa.getBelegartIId(), null,
											99, theClientDto);

									/*
									 * Wenn ich mehrere Losistmaterialdatensaetze habe, dann ist die folgende
									 * Vorgangsweise falsch. Der Gesamtwert wird dann letztendlich nur von der
									 * letzten Ablieferung bestimmt.
									 * 
									 * bdGesamtwert = gkDto.getDurchschnittlicherMaterialpreisProStueck()
									 * .multiply(liDto.getNMenge());
									 */

									bdGesamtwert = bdGesamtwert.add(gkDto.getDurchschnittlicherMaterialpreisProStueck()
											.multiply(wa.getMenge()));
								} else {
//									bdGesamtwert = bdGesamtwert.add(bdPreis.multiply(liDto.getNMenge()));
									bdGesamtwert = bdGesamtwert.add(bdPreis.multiply(wa.getMenge()));
								}
							}
						}
					}

					sessionIstMat.close();

					if (bdMenge.doubleValue() != 0) {
						bdPreis = bdGesamtwert.divide(bdMenge, 4, BigDecimal.ROUND_HALF_EVEN);
					}

				}

				zeile[HF_POSITION_PREIS] = bdPreis;

				BigDecimal sollsatzmenge = new BigDecimal(0);

				if (item.getN_losgroesse().doubleValue() != 0) {
					sollsatzmenge = sollmat[j].getNMenge().divide(item.getN_losgroesse(), BigDecimal.ROUND_HALF_EVEN);
				}

				BigDecimal theoretischabgeliefert = sollsatzmenge.multiply(bdAbgeliefert);

				if (theoretischabgeliefert.doubleValue() > bdMenge.doubleValue()) {
					theoretischabgeliefert = bdMenge;
				}

				zeile[HF_POSITION_ABGELIFERT] = theoretischabgeliefert;
				zeile[HF_POSITION_OFFEN] = bdMenge.subtract(theoretischabgeliefert);

				// Verdichtet gibts bei Sortierung nach Auftrag nicht
				if (bVerdichtet && iSortierung != FertigungReportFac.HF_OPTION_SORTIERUNG_AUFTRAGNR) {
					if (iSortierung == FertigungReportFac.HF_OPTION_SORTIERUNG_ARTIKELNR) {
						boolean bGefunden = false;
						for (int k = 0; k < alDaten.size(); k++) {
							Object[] zeileTemp = (Object[]) alDaten.get(k);

							String suchstring1 = (String) zeileTemp[HF_POSITION_ARTIKELNUMMMER];
							String suchstring2 = (String) zeile[HF_POSITION_ARTIKELNUMMMER];

							if (referenznummerStattArtikelnummer) {
								suchstring1 = (String) zeileTemp[HF_POSITION_REFERENZNUMMER];
								if (suchstring1 == null) {
									suchstring1 = "";
								}
								suchstring2 = (String) zeile[HF_POSITION_REFERENZNUMMER];
								if (suchstring2 == null) {
									suchstring2 = "";
								}
							}

							if (bSortiertNachFertigungsgruppe == true) {
								suchstring1 = Helper.fitString2Length((String) zeileTemp[HF_FERTIGUNGSGRUPPE], 40, ' ')
										+ suchstring1;

								suchstring2 = Helper.fitString2Length((String) zeile[HF_FERTIGUNGSGRUPPE], 40, ' ')
										+ suchstring2;

							}

							if (suchstring1.equals(suchstring2)) {
								bGefunden = true;

								zeileTemp[HF_LOSNUMMER] = null;
								zeileTemp[HF_AUFTRAGSNUMMER] = null;
								zeileTemp[HF_POSITION_AUSGEGEBEN] = ((BigDecimal) zeileTemp[HF_POSITION_AUSGEGEBEN])
										.add((BigDecimal) zeile[HF_POSITION_AUSGEGEBEN]);
								zeileTemp[HF_POSITION_ABGELIFERT] = ((BigDecimal) zeileTemp[HF_POSITION_ABGELIFERT])
										.add((BigDecimal) zeile[HF_POSITION_ABGELIFERT]);

								// Neuen Preis berechnen

								BigDecimal alterPreis = (BigDecimal) zeileTemp[HF_POSITION_PREIS];
								BigDecimal alteOffen = (BigDecimal) zeileTemp[HF_POSITION_OFFEN];

								BigDecimal neuerPreis = (BigDecimal) zeile[HF_POSITION_PREIS];
								BigDecimal neueOffen = (BigDecimal) zeile[HF_POSITION_OFFEN];

								BigDecimal wertNeu = alterPreis.multiply(alteOffen).add(neuerPreis.multiply(neueOffen));

								if (neueOffen.add(alteOffen).doubleValue() != 0) {
									zeileTemp[HF_POSITION_PREIS] = wertNeu.divide(neueOffen.add(alteOffen), 4,
											BigDecimal.ROUND_HALF_EVEN);
								} else {
									zeileTemp[HF_POSITION_PREIS] = new BigDecimal(0);
								}

								zeileTemp[HF_POSITION_OFFEN] = neueOffen.add(alteOffen);

								// Neuen Preis/Maschine berechnen

								BigDecimal alterPreisMaschine = (BigDecimal) zeileTemp[HF_POSITION_PREIS_MASCHINE];
								if (alterPreisMaschine == null) {
									alterPreisMaschine = BigDecimal.ZERO;
								}
								BigDecimal alteOffenMaschine = (BigDecimal) zeileTemp[HF_POSITION_OFFEN_MASCHINE];
								if (alteOffenMaschine == null) {
									alteOffenMaschine = BigDecimal.ZERO;
								}
								BigDecimal neuerPreisMaschine = (BigDecimal) zeile[HF_POSITION_PREIS_MASCHINE];
								if (neuerPreisMaschine == null) {
									neuerPreisMaschine = BigDecimal.ZERO;
								}
								BigDecimal neueOffenMaschine = (BigDecimal) zeile[HF_POSITION_OFFEN_MASCHINE];
								if (neueOffenMaschine == null) {
									neueOffenMaschine = BigDecimal.ZERO;
								}

								BigDecimal wertNeuMaschine = alterPreisMaschine.multiply(alteOffenMaschine)
										.add(neuerPreisMaschine.multiply(neueOffenMaschine));

								if (neueOffenMaschine.add(alteOffenMaschine).doubleValue() != 0) {
									zeileTemp[HF_POSITION_PREIS_MASCHINE] = wertNeuMaschine.divide(
											neueOffenMaschine.add(alteOffenMaschine), 4, BigDecimal.ROUND_HALF_EVEN);
								} else {
									zeileTemp[HF_POSITION_PREIS_MASCHINE] = new BigDecimal(0);
								}

								zeileTemp[HF_POSITION_OFFEN_MASCHINE] = neueOffenMaschine.add(alteOffenMaschine);

								alDaten.set(k, zeileTemp);
							}

						}

						if (bGefunden == false) {
							alDaten.add(zeile);
						}

					} else if (iSortierung == FertigungReportFac.HF_OPTION_SORTIERUNG_LOSNR) {
						boolean bGefunden = false;
						for (int k = 0; k < alDaten.size(); k++) {
							Object[] zeileTemp = (Object[]) alDaten.get(k);

							String suchstring1 = (String) zeileTemp[HF_LOSNUMMER];
							String suchstring2 = (String) zeile[HF_LOSNUMMER];

							if (bSortiertNachFertigungsgruppe == true) {
								suchstring1 = Helper.fitString2Length((String) zeileTemp[HF_FERTIGUNGSGRUPPE], 40, ' ')
										+ suchstring1;

								suchstring2 = Helper.fitString2Length((String) zeile[HF_FERTIGUNGSGRUPPE], 40, ' ')
										+ suchstring2;

							}

							if (suchstring1.equals(suchstring2)) {

								bGefunden = true;

								zeileTemp[HF_POSITION_ARTIKELNUMMMER] = "";
								zeileTemp[HF_POSITION_REFERENZNUMMER] = "";
								zeileTemp[HF_POSITION_BEZEICHNUNG] = "";

								zeileTemp[HF_POSITION_AUSGEGEBEN] = ((BigDecimal) zeileTemp[HF_POSITION_AUSGEGEBEN])
										.add((BigDecimal) zeile[HF_POSITION_AUSGEGEBEN]);
								zeileTemp[HF_POSITION_ABGELIFERT] = ((BigDecimal) zeileTemp[HF_POSITION_ABGELIFERT])
										.add((BigDecimal) zeile[HF_POSITION_ABGELIFERT]);

								// Neuen Preis berechnen

								BigDecimal alterPreis = (BigDecimal) zeileTemp[HF_POSITION_PREIS];
								BigDecimal alteOffen = (BigDecimal) zeileTemp[HF_POSITION_OFFEN];

								BigDecimal neuerPreis = (BigDecimal) zeile[HF_POSITION_PREIS];
								BigDecimal neueOffen = (BigDecimal) zeile[HF_POSITION_OFFEN];

								BigDecimal wertNeu = alterPreis.multiply(alteOffen).add(neuerPreis.multiply(neueOffen));

								if (neueOffen.add(alteOffen).doubleValue() != 0) {
									zeileTemp[HF_POSITION_PREIS] = wertNeu.divide(neueOffen.add(alteOffen), 4,
											BigDecimal.ROUND_HALF_EVEN);
								} else {
									zeileTemp[HF_POSITION_PREIS] = new BigDecimal(0);
								}

								zeileTemp[HF_POSITION_OFFEN] = neueOffen.add(alteOffen);

								// Neuen Preis/Maschine berechnen

								BigDecimal alterPreisMaschine = (BigDecimal) zeileTemp[HF_POSITION_PREIS_MASCHINE];
								if (alterPreisMaschine == null) {
									alterPreisMaschine = BigDecimal.ZERO;
								}
								BigDecimal alteOffenMaschine = (BigDecimal) zeileTemp[HF_POSITION_OFFEN_MASCHINE];
								if (alteOffenMaschine == null) {
									alteOffenMaschine = BigDecimal.ZERO;
								}
								BigDecimal neuerPreisMaschine = (BigDecimal) zeile[HF_POSITION_PREIS_MASCHINE];
								if (neuerPreisMaschine == null) {
									neuerPreisMaschine = BigDecimal.ZERO;
								}
								BigDecimal neueOffenMaschine = (BigDecimal) zeile[HF_POSITION_OFFEN_MASCHINE];
								if (neueOffenMaschine == null) {
									neueOffenMaschine = BigDecimal.ZERO;
								}

								BigDecimal wertNeuMaschine = alterPreisMaschine.multiply(alteOffenMaschine)
										.add(neuerPreisMaschine.multiply(neueOffenMaschine));

								if (neueOffenMaschine.add(alteOffenMaschine).doubleValue() != 0) {
									zeileTemp[HF_POSITION_PREIS_MASCHINE] = wertNeuMaschine.divide(
											neueOffenMaschine.add(alteOffenMaschine), 4, BigDecimal.ROUND_HALF_EVEN);
								} else {
									zeileTemp[HF_POSITION_PREIS_MASCHINE] = new BigDecimal(0);
								}

								zeileTemp[HF_POSITION_OFFEN_MASCHINE] = neueOffenMaschine.add(alteOffenMaschine);

								alDaten.set(k, zeileTemp);
							}

						}

						if (bGefunden == false) {
							alDaten.add(zeile);
						}

					}
				} else {

					alDaten.add(zeile);
				}

			}
			if (bNurMaterialwerte == false) {
				// Verbrauchte Arbeitszeit
				LossollarbeitsplanDto[] lossollarbeitsplanDto = getFertigungFac()
						.lossollarbeitsplanFindByLosIId(item.getI_id());
				for (int j = 0; j < lossollarbeitsplanDto.length; j++) {
					AuftragzeitenDto[] zeiten = getZeiterfassungFac().getAllZeitenEinesBeleges(LocaleFac.BELEGART_LOS,
							item.getI_id(), null, null, null, tsStichtag, ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ARTIKEL,
							theClientDto);

					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(lossollarbeitsplanDto[j].getArtikelIIdTaetigkeit(), theClientDto);

					Object[] zeile = zeileVorlage.clone();

					zeile[HF_POSITION_ARTIKELVERWENDUNG] = "Arbeitszeit";

					zeile[HF_POSITION_ARTIKELNUMMMER] = artikelDto.getCNr();
					zeile[HF_POSITION_REFERENZNUMMER] = artikelDto.getCReferenznr();
					zeile[HF_POSITION_BEZEICHNUNG] = artikelDto.formatBezeichnung();
					zeile[HF_POSITION_EINHEIT] = artikelDto.getEinheitCNr();

					zeile[HF_POSITION_UMRECHNUNGSFAKTOR] = artikelDto.getNUmrechnungsfaktor();
					zeile[HF_POSITION_BESTELLMENGENEINHEIT] = artikelDto.getEinheitCNrBestellung();

					zeile[HF_POSITION_NUR_MASCHINENZEIT] = Helper
							.short2Boolean(lossollarbeitsplanDto[j].getBNurmaschinenzeit());
					zeile[HF_POSITION_ARBEITSGANG] = lossollarbeitsplanDto[j].getIArbeitsgangnummer();

					if (lossollarbeitsplanDto[j].getMaschineIId() != null) {

						for (int k = 0; k < maschineDtos.length; k++) {
							if (maschineDtos[k].getIId().equals(lossollarbeitsplanDto[j].getMaschineIId())) {
								zeile[HF_POSITION_MASCHINENBEZEICHNUNG] = maschineDtos[k].getCBez();
								zeile[HF_POSITION_MASCHINENIDENTIFIKATIONSNUMMER] = maschineDtos[k]
										.getCIdentifikationsnr();
								zeile[HF_POSITION_MASCHINENINVENTARNUMMER] = maschineDtos[k].getCInventarnummer();
								zeile[HF_POSITION_MASCHINENSERIENNUMMER] = maschineDtos[k].getCSeriennummer();

							}
						}

					}

					BigDecimal bdArbeitszeitwert = new BigDecimal(0);
					BigDecimal bdIstZeit = new BigDecimal(0);

					for (int k = 0; k < zeiten.length; k++) {
						if (lossollarbeitsplanDto[j].getIId().equals(zeiten[k].getBelegpositionIId())) {

							bdArbeitszeitwert = bdArbeitszeitwert.add(zeiten[k].getBdKosten());
							bdIstZeit = bdIstZeit.add(new BigDecimal(zeiten[k].getDdDauer().doubleValue()));
						}
					}

					BigDecimal bdSollGesamt = lossollarbeitsplanDto[j].getNGesamtzeit();

					if (bdIstZeit.doubleValue() > bdSollGesamt.doubleValue()) {
						bdSollGesamt = bdIstZeit;
					}

					BigDecimal sollsatzmenge = new BigDecimal(0);
					if (item.getN_losgroesse().doubleValue() != 0) {

						sollsatzmenge = bdSollGesamt.divide(item.getN_losgroesse(), BigDecimal.ROUND_HALF_EVEN);
					}

					if (Helper.short2boolean(lossollarbeitsplanDto[j].getBNurmaschinenzeit())) {
						zeile[HF_POSITION_AUSGEGEBEN] = new BigDecimal(0);
					} else {
						zeile[HF_POSITION_AUSGEGEBEN] = bdSollGesamt;
					}

					// SP8121
					BigDecimal temp = bdIstZeit.subtract(sollsatzmenge.multiply(bdAbgeliefert));

					if (temp.doubleValue() < 0) {
						zeile[HF_POSITION_OFFEN] = BigDecimal.ZERO;
					} else {
						zeile[HF_POSITION_OFFEN] = temp;
					}

					BigDecimal theoretischabgeliefert = sollsatzmenge.multiply(bdAbgeliefert);
					zeile[HF_POSITION_ABGELIFERT] = theoretischabgeliefert;

					if (bdIstZeit.doubleValue() != 0) {
						zeile[HF_POSITION_PREIS] = bdArbeitszeitwert.divide(bdIstZeit, BigDecimal.ROUND_HALF_EVEN);
					} else {
						zeile[HF_POSITION_PREIS] = new BigDecimal(0);
					}

					AuftragzeitenDto[] maschinenzeiten = getZeiterfassungFac().getAllMaschinenzeitenEinesBeleges(
							item.getI_id(), lossollarbeitsplanDto[j].getIId(), null, tsStichtag, theClientDto);

					BigDecimal bdArbeitszeitwertMaschine = new BigDecimal(0);
					BigDecimal bdIstZeitMaschine = new BigDecimal(0);

					for (int k = 0; k < maschinenzeiten.length; k++) {

						bdArbeitszeitwertMaschine = bdArbeitszeitwertMaschine.add(maschinenzeiten[k].getBdKosten());
						bdIstZeitMaschine = bdIstZeitMaschine
								.add(new BigDecimal(maschinenzeiten[k].getDdDauer().doubleValue()));

					}

					if (bdIstZeitMaschine.doubleValue() > lossollarbeitsplanDto[j].getNGesamtzeit().doubleValue()) {
						zeile[HF_POSITION_AUSGEGEBEN_MASCHINE] = bdIstZeitMaschine;
					} else {
						zeile[HF_POSITION_AUSGEGEBEN_MASCHINE] = lossollarbeitsplanDto[j].getNGesamtzeit();
					}

					BigDecimal bdSollGesamtMaschine = BigDecimal.ZERO;

					if (lossollarbeitsplanDto[j].getMaschineIId() != null) {
						lossollarbeitsplanDto[j].getNGesamtzeit();
					}

					if (bdIstZeitMaschine.doubleValue() > bdSollGesamtMaschine.doubleValue()) {
						bdSollGesamtMaschine = bdIstZeitMaschine;
					}

					BigDecimal sollsatzmengeMaschine = new BigDecimal(0);
					if (item.getN_losgroesse().doubleValue() != 0) {

						sollsatzmengeMaschine = bdSollGesamtMaschine.divide(item.getN_losgroesse(),
								BigDecimal.ROUND_HALF_EVEN);
					}

					// SP8121
					BigDecimal tempMaschine = bdIstZeitMaschine.subtract(sollsatzmengeMaschine.multiply(bdAbgeliefert));

					if (tempMaschine.doubleValue() < 0) {
						zeile[HF_POSITION_OFFEN_MASCHINE] = BigDecimal.ZERO;
					} else {
						zeile[HF_POSITION_OFFEN_MASCHINE] = tempMaschine;
					}

					theoretischabgeliefert = sollsatzmengeMaschine.multiply(bdAbgeliefert);

					if (theoretischabgeliefert.doubleValue() > bdIstZeitMaschine.doubleValue()) {
						theoretischabgeliefert = bdIstZeitMaschine;
					}
					zeile[HF_POSITION_ABGELIFERT_MASCHINE] = theoretischabgeliefert;

					if (bdIstZeitMaschine.doubleValue() != 0) {
						zeile[HF_POSITION_PREIS_MASCHINE] = bdArbeitszeitwertMaschine.divide(bdIstZeitMaschine,
								BigDecimal.ROUND_HALF_EVEN);
					} else {
						zeile[HF_POSITION_PREIS_MASCHINE] = new BigDecimal(0);
					}

					if (bVerdichtet) {
						if (iSortierung == FertigungReportFac.HF_OPTION_SORTIERUNG_ARTIKELNR) {
							boolean bGefunden = false;
							for (int k = 0; k < alDaten.size(); k++) {
								Object[] zeileTemp = (Object[]) alDaten.get(k);

								String suchstring1 = (String) zeileTemp[HF_POSITION_ARTIKELNUMMMER];
								String suchstring2 = (String) zeile[HF_POSITION_ARTIKELNUMMMER];

								if (referenznummerStattArtikelnummer) {
									suchstring1 = (String) zeileTemp[HF_POSITION_REFERENZNUMMER];
									if (suchstring1 == null) {
										suchstring1 = "";
									}
									suchstring2 = (String) zeile[HF_POSITION_REFERENZNUMMER];
									if (suchstring2 == null) {
										suchstring2 = "";
									}
								}

								if (bSortiertNachFertigungsgruppe == true) {
									suchstring1 = Helper.fitString2Length((String) zeileTemp[HF_FERTIGUNGSGRUPPE], 40,
											' ') + suchstring1;

									suchstring2 = Helper.fitString2Length((String) zeile[HF_FERTIGUNGSGRUPPE], 40, ' ')
											+ suchstring2;

								}

								if (suchstring1.equals(suchstring2)) {

									bGefunden = true;

									zeileTemp[HF_LOSNUMMER] = null;
									zeileTemp[HF_AUFTRAGSNUMMER] = null;
									zeileTemp[HF_POSITION_AUSGEGEBEN] = ((BigDecimal) zeileTemp[HF_POSITION_AUSGEGEBEN])
											.add((BigDecimal) zeile[HF_POSITION_AUSGEGEBEN]);
									zeileTemp[HF_POSITION_ABGELIFERT] = ((BigDecimal) zeileTemp[HF_POSITION_ABGELIFERT])
											.add((BigDecimal) zeile[HF_POSITION_ABGELIFERT]);

									// Neuen Preis berechnen

									BigDecimal alterPreis = (BigDecimal) zeileTemp[HF_POSITION_PREIS];
									BigDecimal alteOffen = (BigDecimal) zeileTemp[HF_POSITION_OFFEN];

									BigDecimal neuerPreis = (BigDecimal) zeile[HF_POSITION_PREIS];
									BigDecimal neueOffen = (BigDecimal) zeile[HF_POSITION_OFFEN];

									BigDecimal wertNeu = alterPreis.multiply(alteOffen)
											.add(neuerPreis.multiply(neueOffen));

									if (neueOffen.add(alteOffen).doubleValue() != 0) {
										zeileTemp[HF_POSITION_PREIS] = wertNeu.divide(neueOffen.add(alteOffen), 4,
												BigDecimal.ROUND_HALF_EVEN);
									} else {
										zeileTemp[HF_POSITION_PREIS] = new BigDecimal(0);
									}

									zeileTemp[HF_POSITION_OFFEN] = neueOffen.add(alteOffen);

									// Neuen Preis/Maschine berechnen

									BigDecimal alterPreisMaschine = (BigDecimal) zeileTemp[HF_POSITION_PREIS_MASCHINE];
									if (alterPreisMaschine == null) {
										alterPreisMaschine = BigDecimal.ZERO;
									}
									BigDecimal alteOffenMaschine = (BigDecimal) zeileTemp[HF_POSITION_OFFEN_MASCHINE];
									if (alteOffenMaschine == null) {
										alteOffenMaschine = BigDecimal.ZERO;
									}
									BigDecimal neuerPreisMaschine = (BigDecimal) zeile[HF_POSITION_PREIS_MASCHINE];
									if (neuerPreisMaschine == null) {
										neuerPreisMaschine = BigDecimal.ZERO;
									}
									BigDecimal neueOffenMaschine = (BigDecimal) zeile[HF_POSITION_OFFEN_MASCHINE];
									if (neueOffenMaschine == null) {
										neueOffenMaschine = BigDecimal.ZERO;
									}

									BigDecimal wertNeuMaschine = alterPreisMaschine.multiply(alteOffenMaschine)
											.add(neuerPreisMaschine.multiply(neueOffenMaschine));

									if (neueOffenMaschine.add(alteOffenMaschine).doubleValue() != 0) {
										zeileTemp[HF_POSITION_PREIS_MASCHINE] = wertNeuMaschine.divide(
												neueOffenMaschine.add(alteOffenMaschine), 4,
												BigDecimal.ROUND_HALF_EVEN);
									} else {
										zeileTemp[HF_POSITION_PREIS_MASCHINE] = new BigDecimal(0);
									}

									zeileTemp[HF_POSITION_OFFEN_MASCHINE] = neueOffenMaschine.add(alteOffenMaschine);

									alDaten.set(k, zeileTemp);
								}

							}

							if (bGefunden == false) {
								alDaten.add(zeile);
							}

						} else if (iSortierung == FertigungReportFac.HF_OPTION_SORTIERUNG_LOSNR) {
							boolean bGefunden = false;
							for (int k = 0; k < alDaten.size(); k++) {
								Object[] zeileTemp = (Object[]) alDaten.get(k);

								String suchstring1 = (String) zeileTemp[HF_LOSNUMMER];
								String suchstring2 = (String) zeile[HF_LOSNUMMER];

								if (bSortiertNachFertigungsgruppe == true) {
									suchstring1 = Helper.fitString2Length((String) zeileTemp[HF_FERTIGUNGSGRUPPE], 40,
											' ') + suchstring1;

									suchstring2 = Helper.fitString2Length((String) zeile[HF_FERTIGUNGSGRUPPE], 40, ' ')
											+ suchstring2;

								}

								if (suchstring1.equals(suchstring2)) {
									bGefunden = true;

									zeileTemp[HF_POSITION_ARTIKELNUMMMER] = "";
									zeileTemp[HF_POSITION_BEZEICHNUNG] = "";
									zeileTemp[HF_POSITION_AUSGEGEBEN] = ((BigDecimal) zeileTemp[HF_POSITION_AUSGEGEBEN])
											.add((BigDecimal) zeile[HF_POSITION_AUSGEGEBEN]);
									zeileTemp[HF_POSITION_ABGELIFERT] = ((BigDecimal) zeileTemp[HF_POSITION_ABGELIFERT])
											.add((BigDecimal) zeile[HF_POSITION_ABGELIFERT]);

									// Neuen Preis berechnen

									BigDecimal alterPreis = (BigDecimal) zeileTemp[HF_POSITION_PREIS];
									BigDecimal alteOffen = (BigDecimal) zeileTemp[HF_POSITION_OFFEN];

									BigDecimal neuerPreis = (BigDecimal) zeile[HF_POSITION_PREIS];
									BigDecimal neueOffen = (BigDecimal) zeile[HF_POSITION_OFFEN];

									BigDecimal wertNeu = alterPreis.multiply(alteOffen)
											.add(neuerPreis.multiply(neueOffen));

									if (neueOffen.add(alteOffen).doubleValue() != 0) {
										zeileTemp[HF_POSITION_PREIS] = wertNeu.divide(neueOffen.add(alteOffen), 4,
												BigDecimal.ROUND_HALF_EVEN);
									} else {
										zeileTemp[HF_POSITION_PREIS] = new BigDecimal(0);
									}

									zeileTemp[HF_POSITION_OFFEN] = neueOffen.add(alteOffen);
									// Neuen Preis/Maschine berechnen

									BigDecimal alterPreisMaschine = (BigDecimal) zeileTemp[HF_POSITION_PREIS_MASCHINE];
									if (alterPreisMaschine == null) {
										alterPreisMaschine = BigDecimal.ZERO;
									}
									BigDecimal alteOffenMaschine = (BigDecimal) zeileTemp[HF_POSITION_OFFEN_MASCHINE];
									if (alteOffenMaschine == null) {
										alteOffenMaschine = BigDecimal.ZERO;
									}
									BigDecimal neuerPreisMaschine = (BigDecimal) zeile[HF_POSITION_PREIS_MASCHINE];
									if (neuerPreisMaschine == null) {
										neuerPreisMaschine = BigDecimal.ZERO;
									}
									BigDecimal neueOffenMaschine = (BigDecimal) zeile[HF_POSITION_OFFEN_MASCHINE];
									if (neueOffenMaschine == null) {
										neueOffenMaschine = BigDecimal.ZERO;
									}

									BigDecimal wertNeuMaschine = alterPreisMaschine.multiply(alteOffenMaschine)
											.add(neuerPreisMaschine.multiply(neueOffenMaschine));

									if (neueOffenMaschine.add(alteOffenMaschine).doubleValue() != 0) {
										zeileTemp[HF_POSITION_PREIS_MASCHINE] = wertNeuMaschine.divide(
												neueOffenMaschine.add(alteOffenMaschine), 4,
												BigDecimal.ROUND_HALF_EVEN);
									} else {
										zeileTemp[HF_POSITION_PREIS_MASCHINE] = new BigDecimal(0);
									}

									zeileTemp[HF_POSITION_OFFEN_MASCHINE] = neueOffenMaschine.add(alteOffenMaschine);

									alDaten.set(k, zeileTemp);
								}

							}

							if (bGefunden == false) {
								alDaten.add(zeile);
							}

						}
					} else {

						alDaten.add(zeile);
					}

				}

			}
		}

		for (int o = 0; o < alDaten.size(); o++) {
			Object[] a1 = (Object[]) alDaten.get(o);

			String fertigungsgruppe1 = Helper.fitString2Length((String) a1[HF_FERTIGUNGSGRUPPE], 40, ' ');

			String losnummer1 = (String) a1[HF_LOSNUMMER];

			if (losnummer1 == null) {
				losnummer1 = "";
			}

			losnummer1 = Helper.fitString2Length(losnummer1, 30, ' ');

			String auftragsnummer1 = (String) a1[HF_AUFTRAGSNUMMER];

			if (auftragsnummer1 == null) {
				auftragsnummer1 = "";
			}

			auftragsnummer1 = Helper.fitString2Length(auftragsnummer1, 30, ' ');

			String artikelnummer1 = (String) a1[HF_POSITION_ARTIKELNUMMMER];

			if (referenznummerStattArtikelnummer) {
				artikelnummer1 = (String) a1[HF_POSITION_REFERENZNUMMER];
			}

			if (artikelnummer1 == null) {
				artikelnummer1 = "";
			}

			artikelnummer1 = Helper.fitString2Length(artikelnummer1, 40, ' ');

			String suchstring1 = null;

			if (iSortierung == FertigungReportFac.HF_OPTION_SORTIERUNG_LOSNR) {

				suchstring1 = losnummer1 + artikelnummer1;

			} else if (iSortierung == FertigungReportFac.HF_OPTION_SORTIERUNG_ARTIKELNR) {

				suchstring1 = artikelnummer1 + losnummer1;

			} else if (iSortierung == FertigungReportFac.HF_OPTION_SORTIERUNG_AUFTRAGNR) {

				suchstring1 = auftragsnummer1 + losnummer1 + artikelnummer1;

			}

			if (bSortiertNachFertigungsgruppe == true) {
				suchstring1 = fertigungsgruppe1 + suchstring1;

			}

			a1[HF_SORTIERSTRING] = suchstring1;
			alDaten.set(o, a1);
		}

		for (int k = alDaten.size() - 1; k > 0; --k) {
			for (int j = 0; j < k; ++j) {
				Object[] a1 = (Object[]) alDaten.get(j);
				Object[] a2 = (Object[]) alDaten.get(j + 1);

				String s1 = (String) a1[HF_SORTIERSTRING];
				String s2 = (String) a2[HF_SORTIERSTRING];

				if (s1.compareTo(s2) > 0) {
					alDaten.set(j, a2);
					alDaten.set(j + 1, a1);
				}

			}
		}
		Object[][] data = new Object[alDaten.size()][HF_ANZAHL_SPALTEN];
		return (Object[][]) alDaten.toArray(data);
	}

	public ReportLosnachkalkulationDto[] getDataNachkalkulationZeitdaten(Integer losIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			String sMandantWaehrung = theClientDto.getSMandantenwaehrung();

			ParametermandantDto parametermandantDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_ISTZEITEN_GLEICH_SOLLZEITEN);
			boolean bSollGleichIstzeiten = false;

			if (((java.lang.Boolean) parametermandantDto.getCWertAsObject()).booleanValue() == true) {
				bSollGleichIstzeiten = true;
			}

			LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);
			BigDecimal erlMenge = getFertigungFac().getErledigteMenge(losIId, theClientDto);
			BigDecimal faktorFuerIstGleichSoll = new BigDecimal(0);

			if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
				faktorFuerIstGleichSoll = new BigDecimal(1);
			} else {

				if (losDto.getNLosgroesse().doubleValue() > 0) {
					faktorFuerIstGleichSoll = erlMenge.divide(losDto.getNLosgroesse(), 10, BigDecimal.ROUND_HALF_EVEN);
				}
			}
			// Sollzeiten holen
			LossollarbeitsplanDto[] soll = getFertigungFac().lossollarbeitsplanFindByLosIId(losIId);
			// Eine Verdichtete Liste anlegen
			LinkedHashMap<Integer, ReportLosnachkalkulationDto> listVerdichtet = new LinkedHashMap<Integer, ReportLosnachkalkulationDto>();
			// fuer jede Ident eine Zeile anlegen

			for (int i = 0; i < soll.length; i++) {
				ReportLosnachkalkulationDto dto = (ReportLosnachkalkulationDto) listVerdichtet
						.get(soll[i].getArtikelIIdTaetigkeit());
				if (dto == null) {
					dto = new ReportLosnachkalkulationDto();
					listVerdichtet.put(soll[i].getArtikelIIdTaetigkeit(), dto);
					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(soll[i].getArtikelIIdTaetigkeit(), theClientDto);
					dto.setSArtikelnummer(artikelDto.getCNr());
					dto.setSBezeichnung(artikelDto.formatBezeichnung());
					dto.setBFertig(true);
				}

				// SP7738
				dto.setIHoechsterArbeitsgang(soll[i].getIArbeitsgangnummer());

				// SP7674
				if (Helper.short2boolean(soll[i].getBFertig()) == false) {
					dto.setBFertig(false);
				}

				if (soll[i].getMaschineIId() != null && Helper.short2boolean(soll[i].getBNurmaschinenzeit())) {
					// Dann keine Personalzeit
				} else {
					dto.addiereZuSollmenge(soll[i].getNGesamtzeit());
				}

				// CK:Neu
				if (bSollGleichIstzeiten == true) {
					dto.addiereZuIstmenge(soll[i].getNGesamtzeit().multiply(faktorFuerIstGleichSoll));
				}

				if (soll[i].getMaschineIId() != null) {
					dto.addiereZuSollmengeMaschine(soll[i].getNGesamtzeit());
					if (bSollGleichIstzeiten == true) {
						dto.addiereZuIstmengeMaschine(soll[i].getNGesamtzeit().multiply(faktorFuerIstGleichSoll));
					}

					// Soll hinzufuegen
					BigDecimal sollpreis = getZeiterfassungFac().getMaschinenKostenZumZeitpunkt(
							soll[i].getMaschineIId(), Helper.cutTimestamp(new Timestamp(System.currentTimeMillis())),
							LocaleFac.BELEGART_LOS, soll[i].getIId()).getBdStundensatz();

					dto.addiereZuSollpreis(soll[i].getNGesamtzeit().multiply(sollpreis));
					if (bSollGleichIstzeiten == true) {
						dto.addiereZuIstpreis(
								soll[i].getNGesamtzeit().multiply(sollpreis).multiply(faktorFuerIstGleichSoll));
					}

				}

				if (Helper.short2boolean(soll[i].getBNurmaschinenzeit()) == false) {
					ArtikellieferantDto artlief = getArtikelFac().getArtikelEinkaufspreisDesBevorzugtenLieferanten(
							soll[i].getArtikelIIdTaetigkeit(), new BigDecimal(1), sMandantWaehrung, theClientDto);
					if (artlief != null && artlief.getLief1Preis() != null) {
						BigDecimal bdSollpreis = artlief.getLief1Preis();
						dto.addiereZuSollpreis(soll[i].getNGesamtzeit().multiply(bdSollpreis));
						if (bSollGleichIstzeiten == true) {
							dto.addiereZuIstpreis(
									soll[i].getNGesamtzeit().multiply(bdSollpreis).multiply(faktorFuerIstGleichSoll));
						}
					}
				}
			}

			// gebuchte Zeiten holen

			// Eine zusaetzliche Zeile fuer gebuchte Taetigkeiten, die im Soll
			// nicht vorkamen
			ReportLosnachkalkulationDto nkSonstige = new ReportLosnachkalkulationDto();
			nkSonstige.setSBezeichnung(
					getTextRespectUISpr("lp.sonstige", theClientDto.getMandant(), theClientDto.getLocUi()));

			if (bSollGleichIstzeiten == false) {

				// Maschinenzeiten
				AuftragzeitenDto[] zeitenMaschine = getZeiterfassungFac().getAllMaschinenzeitenEinesBeleges(losIId,
						null, null, null, theClientDto);
				for (int i = 0; i < zeitenMaschine.length; i++) {
					zeitenMaschine[i].getDdDauer();
					ReportLosnachkalkulationDto nk = (ReportLosnachkalkulationDto) listVerdichtet
							.get(zeitenMaschine[i].getArtikelIId());
					if (nk != null) {
						nk.addiereZuIstmengeMaschine(new BigDecimal(zeitenMaschine[i].getDdDauer().doubleValue()));
						nk.addiereZuIstpreis(zeitenMaschine[i].getBdKosten());
					} else {
						nkSonstige.addiereZuIstmengeMaschine(
								new BigDecimal(zeitenMaschine[i].getDdDauer().doubleValue()));
						nkSonstige.addiereZuIstpreis(zeitenMaschine[i].getBdKosten());
					}
				}
				// Istzeiten (ohne Maschinenzeiten)
				AuftragzeitenDto[] zeiten = getZeiterfassungFac().getAllZeitenEinesBeleges(LocaleFac.BELEGART_LOS,
						losIId, null, null, null, null, ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ARTIKEL, theClientDto);
				for (int i = 0; i < zeiten.length; i++) {
					zeiten[i].getDdDauer();
					ReportLosnachkalkulationDto nk = (ReportLosnachkalkulationDto) listVerdichtet
							.get(zeiten[i].getArtikelIId());
					if (nk != null) {
						nk.addiereZuIstmenge(new BigDecimal(zeiten[i].getDdDauer().doubleValue()));
						nk.addiereZuIstpreis(zeiten[i].getBdKosten());
					} else {
						nkSonstige.addiereZuIstmenge(new BigDecimal(zeiten[i].getDdDauer().doubleValue()));
						nkSonstige.addiereZuIstpreis(zeiten[i].getBdKosten());
					}
				}
			}
			// Das ganze in ein Array umwandeln
			ReportLosnachkalkulationDto[] result = new ReportLosnachkalkulationDto[listVerdichtet.size() + 1];
			int i = 0;
			for (Iterator<Integer> iter = listVerdichtet.keySet().iterator(); iter.hasNext(); i++) {
				ReportLosnachkalkulationDto item = (ReportLosnachkalkulationDto) listVerdichtet.get(iter.next());
				result[i] = item;
			}
			// jetzt kommen auch die sonstigen dazu
			result[listVerdichtet.size()] = nkSonstige;
			return result;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	private Object[][] getDataAusgabeListe(Integer[] losIId, Integer iSortierung, boolean bVerdichtetNachIdent,
			boolean bVorrangigNachFarbcodeSortiert, Integer artikelklasseIId, TheClientDto theClientDto) {
		Session session = null;

		Object[][] dataLokal = null;

		try {

			// hole alle Stkl-Positionen, wg. Kommentar und

			HashMap<Integer, ArrayList> hmStklPositionenAllerLose = new HashMap<Integer, ArrayList>();

			for (int i = 0; i < losIId.length; i++) {

				LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId[i]);
				if (losDto.getStuecklisteIId() != null) {
					StuecklistepositionDto[] stklPosDtos = getStuecklisteFac()
							.stuecklistepositionFindByStuecklisteIId(losDto.getStuecklisteIId(), theClientDto);

					ArrayList<StuecklistepositionDto> hmPositionen = new ArrayList<StuecklistepositionDto>();

					for (int j = 0; j < stklPosDtos.length; j++) {
						hmPositionen.add(stklPosDtos[j]);

					}

					hmStklPositionenAllerLose.put(losIId[i], hmPositionen);

				}
			}

			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRLossollmaterial.class);

			c.add(Restrictions.in(FertigungFac.FLR_LOSSOLLMATERIAL_LOS_I_ID, losIId));

			// SP5855
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_AUSGABELISTE_MATERIAL_NULLMENGEN_ANDRUCKEN);
			boolean bNullmengenDrucken = (Boolean) parameter.getCWertAsObject();
			if (!bNullmengenDrucken) {
				// SP5709
				c.add(Restrictions.ne("n_menge", BigDecimal.ZERO));
			}

			if (artikelklasseIId != null) {

				c.createAlias("flrartikel", "a").createAlias("a.flrartikelklasse", "kl")
						.add(Restrictions.eq("kl.i_id", artikelklasseIId));
			}

			List<?> list = c.list();
			// positionen verdichten
			LinkedHashMap<Integer, ReportLosAusgabelisteDto> listVerdichtet = new LinkedHashMap<Integer, ReportLosAusgabelisteDto>();
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRLossollmaterial sollmat = (FLRLossollmaterial) iter.next();

				LosistmaterialDto[] mat = getFertigungFac().losistmaterialFindByLossollmaterialIId(sollmat.getI_id());
				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(sollmat.getFlrartikel().getI_id(),
						theClientDto);

				ArtikelDto aDtoStueckliste = null;
				if (sollmat.getFlrlos().getStueckliste_i_id() != null) {

					aDtoStueckliste = getArtikelFac().artikelFindByPrimaryKeySmall(
							sollmat.getFlrlos().getFlrstueckliste().getArtikel_i_id(), theClientDto);

				}

				KostenstelleDto kstDto = null;
				if (sollmat.getFlrlos().getKostenstelle_i_id() != null) {
					kstDto = getSystemFac().kostenstelleFindByPrimaryKey(sollmat.getFlrlos().getKostenstelle_i_id());

				}

				ArtikelfehlmengeDto artikelfehlemngeDto = getFehlmengeFac()
						.artikelfehlmengeFindByBelegartCNrBelegartPositionIIdOhneExc(LocaleFac.BELEGART_LOS,
								sollmat.getI_id());

				BigDecimal lagerstand = new BigDecimal(0);
				if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet()) == true) {
					LoslagerentnahmeDto[] laeger = getFertigungFac()
							.loslagerentnahmeFindByLosIId(sollmat.getLos_i_id());
					for (int i = 0; i < laeger.length; i++) {
						lagerstand = lagerstand.add(getLagerFac().getLagerstandOhneExc(artikelDto.getIId(),
								laeger[i].getLagerIId(), theClientDto));
					}

				}
				BigDecimal inFertigung = getFertigungFac().getAnzahlInFertigung(artikelDto.getIId(), theClientDto);
				BigDecimal lagerstandSperrlager = getLagerFac()
						.getLagerstandAllerSperrlaegerEinesMandanten(artikelDto.getIId(), theClientDto);

				// Artikelkommentar Text und Bild
				Image imageKommentar = null;
				String sArtikelKommentar = "";
				try {
					ArtikelkommentarDto[] aKommentarDto = getArtikelkommentarFac()
							.artikelkommentardruckFindByArtikelIIdBelegartCNr(artikelDto.getIId(),
									LocaleFac.BELEGART_LOS, theClientDto.getLocUiAsString(), theClientDto);

					// Artikelkommentar kann Text oder Bild sein
					if (aKommentarDto != null) {
						for (int k = 0; k < aKommentarDto.length; k++) {
							String cDatenformat = aKommentarDto[k].getDatenformatCNr().trim();
							if (cDatenformat.indexOf(MediaFac.DATENFORMAT_MIMETYPEART_TEXT) != -1) {
								sArtikelKommentar += "\n"
										+ aKommentarDto[k].getArtikelkommentarsprDto().getXKommentar();
							} else if (cDatenformat.indexOf(MediaFac.DATENFORMAT_MIMETYPEART_IMAGE) != -1) {
								// es wird hoechstens 1 Bild pro Belegart
								// gedruckt
								imageKommentar = Helper
										.byteArrayToImage(aKommentarDto[k].getArtikelkommentarsprDto().getOMedia());
							}
						}
					}

				} catch (RemoteException ex3) {
					throwEJBExceptionLPRespectOld(ex3);
				}

				// Stuecklistepositionskommentar + Position aus Stueckliste
				String positionAusStueckliste = null;
				String kommentarAusStueckliste = null;
				String sortierungAusStueckliste = "99999999999999999999";

				BigDecimal bdSollmengeAusStueckliste = null;

				String einheitAusStueckliste = null;

				if (sollmat.getFlrlos().getStueckliste_i_id() != null) {

					ArrayList<StuecklistepositionDto> alPositionen = hmStklPositionenAllerLose
							.get(sollmat.getFlrlos().getI_id());

					// SP5058 Wenn Artikel und Montageart genau einmal vorhanden
					// sind, dann verwenden wird den Eintrag
					StuecklistepositionDto stuecklistepositionDto_Zueghoerig = null;
					for (int u = 0; u < alPositionen.size(); u++) {

						if (sollmat.getFlrartikel().getI_id().equals(alPositionen.get(u).getArtikelIId())
								&& sollmat.getMontageart_i_id().equals(alPositionen.get(u).getMontageartIId())) {

							if (stuecklistepositionDto_Zueghoerig == null) {

								stuecklistepositionDto_Zueghoerig = alPositionen.get(u);
							} else {
								stuecklistepositionDto_Zueghoerig = null;
								break;
							}

						}

					}

					if (stuecklistepositionDto_Zueghoerig == null) {
						// SP5709
						BigDecimal sollsatzmenge = sollmat.getN_menge().divide(sollmat.getFlrlos().getN_losgroesse(), 3,
								BigDecimal.ROUND_HALF_EVEN);
						StklPosDtoSearchResult result = findStklPositionByLossollmaterial(alPositionen,
								new StklPosDtoSearchParams(sollmat.getFlrartikel().getI_id(),
										sollmat.getMontageart_i_id(), sollsatzmenge, sollmat.getN_menge_stklpos()),
								theClientDto);
						stuecklistepositionDto_Zueghoerig = result.getStklPosDto();
					}
					if (stuecklistepositionDto_Zueghoerig != null) {
						kommentarAusStueckliste = stuecklistepositionDto_Zueghoerig.getCKommentar();
						positionAusStueckliste = stuecklistepositionDto_Zueghoerig.getCPosition();
						// SP5709 Wenn vorhanden urspruengliche Menge und
						// Einheit aus Stklposition
						bdSollmengeAusStueckliste = sollmat.getN_menge_stklpos() != null ? sollmat.getN_menge_stklpos()
								: stuecklistepositionDto_Zueghoerig.getNMenge();
						einheitAusStueckliste = sollmat.getEinheit_c_nr_stklpos() != null
								? sollmat.getEinheit_c_nr_stklpos()
								: stuecklistepositionDto_Zueghoerig.getEinheitCNr();

						sortierungAusStueckliste = Helper.fitString2Length(sollmat.getFlrlos().getC_nr(), 20, ' ') + ""
								+ Helper.fitString2LengthAlignRight(stuecklistepositionDto_Zueghoerig.getISort() + "",
										10, ' ');

						alPositionen.remove(stuecklistepositionDto_Zueghoerig);
					}

				}

				if (mat.length == 0) {
					ReportLosAusgabelisteDto dto = new ReportLosAusgabelisteDto();
					dto.setISchale(sollmat.getI_lfdnummer());
					dto.setNAusgabe(new BigDecimal(0));
					dto.setNMenge(sollmat.getN_menge());

					dto.setNLagerstand(lagerstand);
					dto.setNLagerstandSperrlager(lagerstandSperrlager);
					dto.setNInFertigung(inFertigung);
					dto.setArtikelbild(imageKommentar);
					dto.setSKommentar(sArtikelKommentar);
					dto.setBNurZurInfo(Helper.short2Boolean(artikelDto.getbNurzurinfo()));
					dto.setSKommentarMaterial(sollmat.getC_kommentar());
					dto.setSPositionMaterial(sollmat.getC_position());
					dto.setFDimension1Material(sollmat.getF_dimension1());
					dto.setFDimension2Material(sollmat.getF_dimension2());
					dto.setFDimension3Material(sollmat.getF_dimension3());

					if (artikelDto.getFarbcodeIId() != null) {
						dto.setSFarbcode(
								getArtikelFac().farbcodeFindByPrimaryKey(artikelDto.getFarbcodeIId()).getCNr());
					}

					dto.setSBezeichnung(artikelDto.getArtikelsprDto().getCBez());
					dto.setSZusatzBezeichnung(artikelDto.getArtikelsprDto().getCZbez());
					dto.setSZusatzBezeichnung2(artikelDto.getArtikelsprDto().getCZbez2());
					dto.setKurzbezeichnung(artikelDto.getArtikelsprDto().getCKbez());
					dto.setSEinheit(sollmat.getFlrartikel().getEinheit_c_nr());

					// Material
					if (artikelDto.getMaterialIId() != null) {
						MaterialDto materialDto = getMaterialFac().materialFindByPrimaryKey(artikelDto.getMaterialIId(),
								theClientDto);
						dto.setSMaterial(materialDto.getBezeichnung());
					}
					// Staerke/Hoehe
					if (artikelDto.getGeometrieDto() != null) {
						if (artikelDto.getGeometrieDto().getFHoehe() != null) {
							dto.setDHoehe(artikelDto.getGeometrieDto().getFHoehe());
						}
						if (artikelDto.getGeometrieDto().getFBreite() != null) {
							dto.setDBreite(artikelDto.getGeometrieDto().getFBreite());
						}
						if (artikelDto.getGeometrieDto().getFTiefe() != null) {
							dto.setDTiefe(artikelDto.getGeometrieDto().getFTiefe());
						}
					}

					dto.setSRevision(artikelDto.getCRevision());
					dto.setSIndex(artikelDto.getCIndex());

					// Verpackung
					if (artikelDto.getVerpackungDto() != null) {
						dto.setSBauform(artikelDto.getVerpackungDto().getCBauform());
						dto.setSVerpackungsart(artikelDto.getVerpackungDto().getCVerpackungsart());
					}

					// Gewicht
					dto.setDGewichtkg(artikelDto.getFGewichtkg());
					// Montage Rasterstehend
					if (artikelDto.getMontageDto() != null && artikelDto.getMontageDto().getFRasterstehend() != null) {
						dto.setDRasterstehend(artikelDto.getMontageDto().getFRasterstehend().doubleValue());
					}
					String sIdent;
					if (sollmat.getFlrartikel().getArtikelart_c_nr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
						sIdent = "";
					} else {
						sIdent = sollmat.getFlrartikel().getC_nr();
						dto.setReferenznummer(sollmat.getFlrartikel().getC_referenznr());
					}
					dto.setSIdent(sIdent);
					if (sollmat.getFlrartikel().getFlrartikelklasse() != null) {
						dto.setSArtikelklasse(sollmat.getFlrartikel().getFlrartikelklasse().getC_nr());
					}
					if (sollmat.getMontageart_i_id() != null) {
						MontageartDto montageartDto = getStuecklisteFac()
								.montageartFindByPrimaryKey(sollmat.getMontageart_i_id(), theClientDto);
						dto.setSMontageart(montageartDto.getCBez());
					}

					LosDto losDto = getFertigungFac().losFindByPrimaryKey(sollmat.getLos_i_id());
					LagerDto lagerDto = getLagerFac().lagerFindByPrimaryKey(losDto.getLagerIIdZiel());
					dto.setSLager(lagerDto.getCNr());

					if (lagerDto.getPartnerIIdStandort() != null) {
						dto.setSStandort(getPartnerFac()
								.partnerFindByPrimaryKey(lagerDto.getPartnerIIdStandort(), theClientDto).getCKbez());
					}

					String sLagerort = getLagerFac().getLagerplaezteEinesArtikels(sollmat.getFlrartikel().getI_id(),
							losDto.getLagerIIdZiel());

					dto.setSLagerort(sLagerort);
					// offene Bestellmenge
					dto.setBdAnzahlBestellt(getArtikelbestelltFac().getAnzahlBestellt(artikelDto.getIId()));

					if (artikelfehlemngeDto != null && artikelfehlemngeDto.getNMenge() != null) {
						dto.setBdFehlmenge(artikelfehlemngeDto.getNMenge());
					}

					// Stuecklistepositionskommentar + Position aus Stueckliste

					dto.setSKommentarStueckliste(kommentarAusStueckliste);
					dto.setSPositionStueckliste(positionAusStueckliste);
					dto.setSSortAusStueckliste(sortierungAusStueckliste);
					dto.setSollmengeStueckliste(bdSollmengeAusStueckliste);
					dto.setEinheitStueckliste(einheitAusStueckliste);

					dto.setLosnummer(losDto.getCNr());
					dto.addLosSollmaterialIId(sollmat.getI_id());

					// PJ22180
					if (bVerdichtetNachIdent == false) {

						dto.setsLosLosnummer(losDto.getCNr());
						dto.setdLosProduktionsbeginn(losDto.getTProduktionsbeginn());
						dto.setdLosProduktionsende(losDto.getTProduktionsende());

						dto.settLosAngelegt(losDto.getTAnlegen());

						dto.setBdLosLosgroesse(losDto.getNLosgroesse());

						if (aDtoStueckliste != null) {
							dto.setBdLosStuecklisteErfassungsfaktor(
									sollmat.getFlrlos().getFlrstueckliste().getN_erfassungsfaktor());
							dto.setsLosStuecklistenummer(aDtoStueckliste.getCNr());
							dto.setsLosStuecklistebezeichnung(aDtoStueckliste.getCBezAusSpr());
							dto.setsLosStuecklistezusatzbezeichnung(aDtoStueckliste.getCZBezAusSpr());

						}

						if (kstDto != null) {
							dto.setsLosKostenstellenummer(kstDto.getCNr());
						}

						if (sollmat.getFlrlos().getFlrauftrag() != null) {
							dto.setsLosAuftragsnummer(sollmat.getFlrlos().getFlrauftrag().getC_nr());
							dto.setsLosKunde(HelperServer.formatNameAusFLRPartner(
									sollmat.getFlrlos().getFlrauftrag().getFlrkunde().getFlrpartner()));
						}
					}

					// key ist -i_id, damit ich keine vorhandenen eintraege
					// ueberschreibe

					// verdichten nach ident

					Iterator<Integer> it = listVerdichtet.keySet().iterator();
					boolean bGefunden = false;

					if (bVerdichtetNachIdent) {
						while (it.hasNext()) {
							Object key = it.next();

							ReportLosAusgabelisteDto temp = (ReportLosAusgabelisteDto) listVerdichtet.get(key);
							if ((temp.getSLager() == null && lagerDto.getCNr() == null)
									|| (temp.getSLager() != null && lagerDto.getCNr() != null)) {
								if ((temp.getSLager() == null && lagerDto.getCNr() == null)
										|| (temp.getSLager().equals(lagerDto.getCNr()))) {

									if (temp.getSIdent().equals(sIdent) && !temp.getSIdent().equals("")) {

										bGefunden = true;
										dto.setNMenge(dto.getNMenge().add(temp.getNMenge()));
										dto.setNAusgabe(dto.getNAusgabe().add(temp.getNAusgabe()));
										dto.setSKommentarStueckliste(addToVerdichtetenString(
												dto.getSKommentarStueckliste(), temp.getSKommentarStueckliste()));
										dto.setSPositionStueckliste(addToVerdichtetenString(
												dto.getSPositionStueckliste(), temp.getSPositionStueckliste()));
										dto.setSKommentarMaterial(addToVerdichtetenString(dto.getSKommentarMaterial(),
												temp.getSKommentarMaterial()));
										dto.setSPositionMaterial(addToVerdichtetenString(dto.getSPositionMaterial(),
												temp.getSPositionMaterial()));
										dto.addLosnummer(temp.getLosnummer());
										dto.addLosSollmaterialIIds(temp.getLosSollmaterialIIds());

										dto.setBdFehlmenge(dto.getBdFehlmenge().add(temp.getBdFehlmenge()));

										listVerdichtet.put((Integer) key, dto);

										continue;
									}
								}
							}

						}
					}
					if (bGefunden == false) {
						listVerdichtet.put(new Integer(-sollmat.getI_id().intValue()), dto);
					}

				} else {

					HashMap<Integer, ArrayList> hmStklPositionenAllerLoseKopie = (HashMap<Integer, ArrayList>) hmStklPositionenAllerLose
							.clone();

					for (int i = 0; i < mat.length; i++) {
						BigDecimal ausgabemenge = new BigDecimal(0);
						Integer lagerIId = null;

						if (Helper.short2boolean(mat[i].getBAbgang()) == true) {
							ausgabemenge = ausgabemenge.add(mat[i].getNMenge());
						} else {
							ausgabemenge = ausgabemenge.subtract(mat[i].getNMenge());
						}
						lagerIId = mat[i].getLagerIId();

						ReportLosAusgabelisteDto dto = new ReportLosAusgabelisteDto();
						dto.setISchale(sollmat.getI_lfdnummer());
						dto.setNAusgabe(ausgabemenge);
						dto.setArtikelbild(imageKommentar);
						dto.setSKommentar(sArtikelKommentar);
						dto.setNLagerstand(lagerstand);
						dto.setNLagerstandSperrlager(lagerstandSperrlager);
						dto.setNInFertigung(inFertigung);
						dto.setSKommentarMaterial(sollmat.getC_kommentar());
						dto.setSPositionMaterial(sollmat.getC_position());
						dto.setFDimension1Material(sollmat.getF_dimension1());
						dto.setFDimension2Material(sollmat.getF_dimension2());
						dto.setFDimension3Material(sollmat.getF_dimension3());

						// Sollmenge zaehlt nur beim ersten eintrag
						if (i == 0) {
							dto.setNMenge(sollmat.getN_menge());
						} else {
							dto.setNMenge(BigDecimal.ZERO);
						}

						if (sollmat.getFlrartikel().getFlrartikelklasse() != null) {
							dto.setSArtikelklasse(sollmat.getFlrartikel().getFlrartikelklasse().getC_nr());
						} else {
							dto.setSArtikelklasse("");
						}

						if (artikelDto.getFarbcodeIId() != null) {
							dto.setSFarbcode(
									getArtikelFac().farbcodeFindByPrimaryKey(artikelDto.getFarbcodeIId()).getCNr());
						}
						dto.setSBezeichnung(artikelDto.getArtikelsprDto().getCBez());
						dto.setSZusatzBezeichnung(artikelDto.getArtikelsprDto().getCZbez());
						dto.setSZusatzBezeichnung2(artikelDto.getArtikelsprDto().getCZbez2());
						dto.setKurzbezeichnung(artikelDto.getArtikelsprDto().getCKbez());
						dto.setSEinheit(sollmat.getFlrartikel().getEinheit_c_nr());

						dto.setBNurZurInfo(Helper.short2Boolean(artikelDto.getbNurzurinfo()));

						// Material
						if (artikelDto.getMaterialIId() != null) {
							MaterialDto materialDto = getMaterialFac()
									.materialFindByPrimaryKey(artikelDto.getMaterialIId(), theClientDto);
							dto.setSMaterial(materialDto.getBezeichnung());
						}
						// Staerke/Hoehe
						if (artikelDto.getGeometrieDto() != null) {
							if (artikelDto.getGeometrieDto().getFHoehe() != null) {
								dto.setDHoehe(artikelDto.getGeometrieDto().getFHoehe());
							}
							if (artikelDto.getGeometrieDto().getFBreite() != null) {
								dto.setDBreite(artikelDto.getGeometrieDto().getFBreite());
							}
							if (artikelDto.getGeometrieDto().getFTiefe() != null) {
								dto.setDTiefe(artikelDto.getGeometrieDto().getFTiefe());
							}
						}

						// Verpackung
						if (artikelDto.getVerpackungDto() != null) {
							dto.setSBauform(artikelDto.getVerpackungDto().getCBauform());
							dto.setSVerpackungsart(artikelDto.getVerpackungDto().getCVerpackungsart());
						}
						// Gewicht
						dto.setDGewichtkg(artikelDto.getFGewichtkg());

						dto.setSRevision(artikelDto.getCRevision());
						dto.setSIndex(artikelDto.getCIndex());

						// Montage Rasterstehend
						if (artikelDto.getMontageDto() != null
								&& artikelDto.getMontageDto().getFRasterstehend() != null) {
							dto.setDRasterstehend(artikelDto.getMontageDto().getFRasterstehend());
						}

						if (Helper.short2boolean(artikelDto.getBSeriennrtragend())
								|| Helper.short2boolean(artikelDto.getBChargennrtragend())) {
							List<SeriennrChargennrMitMengeDto> snrChnrDtos = getLagerFac()
									.getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(LocaleFac.BELEGART_LOS,
											mat[i].getIId());
							if (snrChnrDtos != null) {
								String snrchnr = "";
								for (int s = 0; s < snrChnrDtos.size(); s++) {

									SeriennrChargennrMitMengeDto snrChnrDto = snrChnrDtos.get(s);

									if (dto.getNAusgabe().doubleValue() < 0) {
										snrchnr += "-";
									}

									snrchnr += Helper.formatZahl(snrChnrDto.getNMenge(), 4, theClientDto.getLocUi())
											+ " ";
									snrchnr += snrChnrDto.getCSeriennrChargennr() + "; ";

								}
								dto.setSSnrChnr(snrchnr);
							}
						}

						// offene Bestellmenge
						dto.setBdAnzahlBestellt(getArtikelbestelltFac().getAnzahlBestellt(artikelDto.getIId()));

						if (artikelfehlemngeDto != null && artikelfehlemngeDto.getNMenge() != null) {
							dto.setBdFehlmenge(artikelfehlemngeDto.getNMenge());
						}

						String sIdent;
						if (sollmat.getFlrartikel().getArtikelart_c_nr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
							sIdent = "";
						} else {
							sIdent = sollmat.getFlrartikel().getC_nr();
							dto.setReferenznummer(sollmat.getFlrartikel().getC_referenznr());
						}
						dto.setSIdent(sIdent);

						LagerDto lagerDto = getLagerFac().lagerFindByPrimaryKey(lagerIId);

						dto.setSLager(lagerDto.getCNr());

						if (lagerDto.getPartnerIIdStandort() != null) {
							dto.setSStandort(getPartnerFac()
									.partnerFindByPrimaryKey(lagerDto.getPartnerIIdStandort(), theClientDto)
									.getCKbez());
						}

						String sLagerort = getLagerFac().getLagerplaezteEinesArtikels(sollmat.getFlrartikel().getI_id(),
								lagerDto.getIId());

						dto.setSLagerort(sLagerort);

						if (sollmat.getMontageart_i_id() != null) {
							MontageartDto montageartDto = getStuecklisteFac()
									.montageartFindByPrimaryKey(sollmat.getMontageart_i_id(), theClientDto);
							dto.setSMontageart(montageartDto.getCBez());
						}

						dto.setSKommentarStueckliste(kommentarAusStueckliste);
						dto.setSSortAusStueckliste(sortierungAusStueckliste);
						dto.setSPositionStueckliste(positionAusStueckliste);
						dto.setSollmengeStueckliste(bdSollmengeAusStueckliste);
						dto.setEinheitStueckliste(einheitAusStueckliste);

						LosDto losDto = getFertigungFac().losFindByPrimaryKey(sollmat.getLos_i_id());
						dto.setLosnummer(losDto.getCNr());
						dto.addLosSollmaterialIId(sollmat.getI_id());

						// verdichten nach ident
						Iterator<Integer> it = listVerdichtet.keySet().iterator();
						boolean bGefunden = false;

						// PJ22180
						if (bVerdichtetNachIdent == false) {

							dto.setsLosLosnummer(losDto.getCNr());
							dto.setdLosProduktionsbeginn(losDto.getTProduktionsbeginn());
							dto.setdLosProduktionsende(losDto.getTProduktionsende());

							dto.settLosAngelegt(losDto.getTAnlegen());

							dto.setBdLosLosgroesse(losDto.getNLosgroesse());

							if (aDtoStueckliste != null) {
								dto.setBdLosStuecklisteErfassungsfaktor(
										sollmat.getFlrlos().getFlrstueckliste().getN_erfassungsfaktor());
								dto.setsLosStuecklistenummer(aDtoStueckliste.getCNr());
								dto.setsLosStuecklistebezeichnung(aDtoStueckliste.getCBezAusSpr());
								dto.setsLosStuecklistezusatzbezeichnung(aDtoStueckliste.getCZBezAusSpr());

							}

							if (kstDto != null) {
								dto.setsLosKostenstellenummer(kstDto.getCNr());
							}

							if (sollmat.getFlrlos().getFlrauftrag() != null) {
								dto.setsLosAuftragsnummer(sollmat.getFlrlos().getFlrauftrag().getC_nr());
								dto.setsLosKunde(HelperServer.formatNameAusFLRPartner(
										sollmat.getFlrlos().getFlrauftrag().getFlrkunde().getFlrpartner()));
							}
						}

						if (bVerdichtetNachIdent) {

							while (it.hasNext()) {
								Object key = it.next();

								ReportLosAusgabelisteDto temp = (ReportLosAusgabelisteDto) listVerdichtet.get(key);
								if ((temp.getSLager() == null && lagerDto.getCNr() == null)
										|| (temp.getSLager() != null && lagerDto.getCNr() != null)) {
									if ((temp.getSLager() == null && lagerDto.getCNr() == null)
											|| (temp.getSLager().equals(lagerDto.getCNr()))) {
										if (temp.getSIdent().equals(sIdent) && !temp.getSIdent().equals("")) {
											bGefunden = true;
											dto.setNMenge(dto.getNMenge().add(temp.getNMenge()));
											dto.setNAusgabe(dto.getNAusgabe().add(temp.getNAusgabe()));
											dto.addLosnummer(temp.getLosnummer());
											dto.addLosSollmaterialIIds(temp.getLosSollmaterialIIds());
											// PJ 14410
											if (temp.getSSnrChnr() != null) {

												dto.setSSnrChnr(dto.getSSnrChnr() + (temp.getSSnrChnr()));

											}

											dto.setBdFehlmenge(dto.getBdFehlmenge().add(temp.getBdFehlmenge()));

											listVerdichtet.put((Integer) key, dto);

											continue;
										}
									}
								}
							}
						}
						if (bGefunden == false) {
							listVerdichtet.put(mat[i].getIId(), dto);
						}

					}

				}
			}
			// Sortieren, dazu brauch ich einen anderen container-typ
			List<ReportLosAusgabelisteDto> listVerdichtetUndSortiert = new ArrayList<ReportLosAusgabelisteDto>();
			for (Iterator<Integer> iter = listVerdichtet.keySet().iterator(); iter.hasNext();) {
				listVerdichtetUndSortiert.add(listVerdichtet.get(iter.next()));
			}
			Collections.sort(listVerdichtetUndSortiert,
					new ComparatorAusgabeListe(iSortierung.intValue(), bVorrangigNachFarbcodeSortiert));

			dataLokal = new Object[listVerdichtet.size()][AUSG_SPALTENANZAHL];
			int i = 0;
			for (Iterator<ReportLosAusgabelisteDto> iter = listVerdichtetUndSortiert.iterator(); iter.hasNext(); i++) {
				ReportLosAusgabelisteDto item = (ReportLosAusgabelisteDto) iter.next();
				dataLokal[i][AUSG_ARTIKELKLASSE] = item.getSArtikelklasse();
				dataLokal[i][AUSG_NUR_ZUR_INFO] = item.getBNurZurInfo();
				dataLokal[i][AUSG_AUSGABE] = item.getNAusgabe();
				dataLokal[i][AUSG_BEZEICHNUNG] = item.getSBezeichnung();

				dataLokal[i][AUSG_KURZBEZEICHNUNG] = item.getKurzbezeichnung();
				dataLokal[i][AUSG_REFERENZNUMMER] = item.getReferenznummer();

				dataLokal[i][AUSG_ZUSATZBEZEICHNUNG] = item.getSZusatzBezeichnung();
				dataLokal[i][AUSG_ZUSATZBEZEICHNUNG2] = item.getSZusatzBezeichnung2();
				dataLokal[i][AUSG_EINHEIT] = item.getSEinheit();
				dataLokal[i][AUSG_IDENT] = item.getSIdent();
				dataLokal[i][AUSG_LAGER] = item.getSLager();
				dataLokal[i][AUSG_STANDORT] = item.getSStandort();
				dataLokal[i][AUSG_LAGERORT] = item.getSLagerort();
				dataLokal[i][AUSG_MENGE] = item.getNMenge();
				dataLokal[i][AUSG_MONTAGEART] = item.getSMontageart();
				dataLokal[i][AUSG_SCHALE] = item.getISchale();
				dataLokal[i][AUSG_SNRCHNR] = item.getSSnrChnr();
				dataLokal[i][AUSG_ARTIKELBILD] = item.getArtikelbild();
				dataLokal[i][AUSG_KOMMENTAR] = item.getSKommentar();
				dataLokal[i][AUSG_LAGERSTAND] = item.getNLagerstand();
				dataLokal[i][AUSG_LAGERSTAND_SPERRLAGER] = item.getNLagerstandSperrlager();

				dataLokal[i][AUSG_REVISION] = item.getSRevision();
				dataLokal[i][AUSG_INDEX] = item.getSIndex();

				// CK: Farbcode einfuegen
				dataLokal[i][AUSG_FARBCODE] = item.getSFarbcode();
				if (item.getDHoehe() != null && item.getDHoehe() != 0) {
					dataLokal[i][AUSG_HOEHE] = new Double(item.getDHoehe());
				}
				if (item.getDBreite() != null && item.getDBreite() != 0) {
					dataLokal[i][AUSG_BREITE] = new Double(item.getDBreite());
				}
				if (item.getDTiefe() != null && item.getDTiefe() != 0) {
					dataLokal[i][AUSG_TIEFE] = new Double(item.getDTiefe());
				}
				dataLokal[i][AUSG_MATERIAL] = item.getSMaterial();

				// Verpackung
				if (item.getSBauform() != null) {
					dataLokal[i][AUSG_BAUFORM] = item.getSBauform();
				}
				if (item.getSVerpackungsart() != null) {
					dataLokal[i][AUSG_VERPACKUNGSART] = item.getSVerpackungsart();
				}
				// Gewicht
				if (item.getDGewichtkg() != null && item.getDGewichtkg() != 0) {
					dataLokal[i][AUSG_GEWICHTKG] = item.getDGewichtkg();
				}
				// Montage Rasterstehend
				if (item.getDRasterstehend() != null && item.getDRasterstehend() != 0) {
					dataLokal[i][AUSG_RASTERSTEHEND] = item.getDRasterstehend();
				}
				dataLokal[i][AUSG_BESTELLT] = item.getBdAnzahlBestellt();
				dataLokal[i][AUSG_FEHLMENGE] = item.getBdFehlmenge();
				dataLokal[i][AUSG_IN_FERTIGUNG] = item.getNInFertigung();
				dataLokal[i][AUSG_STUECKLISTE_KOMMENTAR] = item.getSKommentarStueckliste();
				dataLokal[i][AUSG_STUECKLISTE_POSITION] = item.getSPositionStueckliste();
				dataLokal[i][AUSG_STUECKLISTE_SOLLMENGE] = item.getSollmengeStueckliste();
				dataLokal[i][AUSG_STUECKLISTE_EINHEIT] = item.getEinheitStueckliste();
				dataLokal[i][AUSG_MATERIAL_POSITION] = item.getSPositionMaterial();
				dataLokal[i][AUSG_MATERIAL_KOMMENTAR] = item.getSKommentarMaterial();
				dataLokal[i][AUSG_MATERIAL_DIMENSION1] = item.getFDimension1Material();
				dataLokal[i][AUSG_MATERIAL_DIMENSION2] = item.getFDimension2Material();
				dataLokal[i][AUSG_MATERIAL_DIMENSION3] = item.getFDimension3Material();
				dataLokal[i][AUSG_LOSNUMMER] = item.getLosnummer();
				dataLokal[i][AUSG_LOSSOLLMATERIAL_IIDS] = item.getLosSollmaterialIIds();

				dataLokal[i][AUSG_LOS_ANGELEGT] = item.gettLosAngelegt();
				dataLokal[i][AUSG_LOS_LOSNUMMER] = item.getsLosLosnummer();
				dataLokal[i][AUSG_LOS_AUFTRAGNUMMER] = item.getsLosAuftragsnummer();
				dataLokal[i][AUSG_LOS_KOSTENSTELLENUMMER] = item.getsLosKostenstellenummer();
				dataLokal[i][AUSG_LOS_PRODUKTIONSBEGINN] = item.getdLosProduktionsbeginn();
				dataLokal[i][AUSG_LOS_PRODUKTIONSENDE] = item.getdLosProduktionsende();
				dataLokal[i][AUSG_LOS_KUNDE] = item.getsLosKunde();
				dataLokal[i][AUSG_LOS_STUECKLISTENUMMER] = item.getsLosStuecklistenummer();
				dataLokal[i][AUSG_LOS_STUECKLISTEBEZEICHNUNG] = item.getsLosStuecklistebezeichnung();
				dataLokal[i][AUSG_LOS_STUECKLISTEZUSATZBEZEICHNUNG] = item.getsLosStuecklistezusatzbezeichnung();
				dataLokal[i][AUSG_LOS_STUECKLISTE_ERFASSUNGSFAKTOR] = item.getBdLosStuecklisteErfassungsfaktor();
				dataLokal[i][AUSG_LOS_LOSGROESSE] = item.getBdLosLosgroesse();

			}

		} catch (Exception t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, t);
		} finally {
			closeSession(session);
		}
		return dataLokal;
	}

	private String addToVerdichtetenString(String verdichtet, String value) {
		if (Helper.isStringEmpty(value))
			return verdichtet;

		if (verdichtet == null)
			return value;

		return value + "," + verdichtet;
	}

	public StklPosDtoSearchResult findStklPositionByLossollmaterial(List<StuecklistepositionDto> stklPositionen,
			StklPosDtoSearchParams params, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		StklPosDtoSearchResult result = new StklPosDtoSearchResult();

		for (StuecklistepositionDto stklPosDto : stklPositionen) {
			if (!params.getArtikelIId().equals(stklPosDto.getArtikelIId())
					|| !params.getMontageartIId().equals(stklPosDto.getMontageartIId()))
				continue;

			// SP4979
			BigDecimal bdZielmenge = getStuecklisteFac().berechneZielmenge(stklPosDto.getIId(), theClientDto);
			bdZielmenge = bdZielmenge != null ? bdZielmenge : BigDecimal.ZERO;
			BigDecimal bdSollmenge;

			if (params.getMengeStklPos() == null) {
				// SP5160
				if (Helper.isBetween(bdZielmenge, new BigDecimal("0.000001"), new BigDecimal("0.001"))) {
					bdZielmenge = new BigDecimal("0.001");
				}

				bdZielmenge = Helper.rundeKaufmaennisch(bdZielmenge, 3);
				bdSollmenge = params.getSollsatzmenge();
				if (bdSollmenge.compareTo(bdZielmenge) == 0) {
					result.setStklPosDto(stklPosDto);
					result.setZielmenge(bdZielmenge);
					return result;
				}
			} else {
				// SP5709
				bdSollmenge = params.getMengeStklPos();
				if (bdSollmenge.compareTo(stklPosDto.getNMenge()) == 0) {
					result.setStklPosDto(stklPosDto);
					result.setZielmenge(bdZielmenge);
					return result;
				}
			}

		}
		return result;
	}

	private Map<String, Object> getParameterAusgabeliste(LosDto losDto, String lose, Integer iSortierung,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Map<String, Object> mapParameter = new TreeMap<String, Object>();
		mapParameter.put("P_ANGELEGT", new java.util.Date(losDto.getTAnlegen().getTime()));
		mapParameter.put("P_LOSNUMMER", lose);
		mapParameter.put("P_PROJEKT", losDto.getCProjekt());
		mapParameter.put("P_LOSKOMMENTAR", losDto.getCKommentar());
		mapParameter.put("P_LOSLANGTEXT", losDto.getXText());

		// PJ20772
		mapParameter.put("P_ABPOSNR", losDto.getCAbposnr());

		String sortierung = "";

		if (iSortierung == Helper.SORTIERUNG_NACH_IDENT) {
			sortierung = getTextRespectUISpr("fert.ausgabeliste.sort.artikel", theClientDto.getMandant(),
					theClientDto.getLocUi());
		} else if (iSortierung == Helper.SORTIERUNG_NACH_ARTIKELBEZEICHNUNG) {
			sortierung = getTextRespectUISpr("fert.ausgabeliste.sort.artikelbezeichnung", theClientDto.getMandant(),
					theClientDto.getLocUi());
		} else if (iSortierung == Helper.SORTIERUNG_NACH_LAGER_UND_LAGERORT) {
			sortierung = getTextRespectUISpr("fert.ausgabeliste.sort.lagerlagerplatz", theClientDto.getMandant(),
					theClientDto.getLocUi());
		} else if (iSortierung == Helper.SORTIERUNG_NACH_LAGERORT_UND_LAGER) {
			sortierung = getTextRespectUISpr("fert.ausgabeliste.sort.lagerplatzlager", theClientDto.getMandant(),
					theClientDto.getLocUi());
		} else if (iSortierung == Helper.SORTIERUNG_NACH_MONTAGEART_UND_SCHALE) {
			sortierung = getTextRespectUISpr("fert.ausgabeliste.sort.montageartschale", theClientDto.getMandant(),
					theClientDto.getLocUi());
		} else if (iSortierung == Helper.SORTIERUNG_NACH_WIE_ERFASST) {
			sortierung = getTextRespectUISpr("fert.ausgabeliste.sort.wieerfasst", theClientDto.getMandant(),
					theClientDto.getLocUi());
		} else if (iSortierung == Helper.SORTIERUNG_NACH_ARTIKELKLASSE) {
			sortierung = getTextRespectUISpr("fert.ausgabeliste.sort.artikelklasse", theClientDto.getMandant(),
					theClientDto.getLocUi());
		} else if (iSortierung == Helper.SORTIERUNG_NACH_ARTIKELKLASSE_UND_MATERIAL) {
			sortierung = getTextRespectUISpr("fert.ausgabeliste.sort.artikelklassematerial", theClientDto.getMandant(),
					theClientDto.getLocUi());
		}

		mapParameter.put("P_SORTIERUNG", sortierung);

		String sAuftragsnummer;
		String sKunde;
		String sLieferart;
		String sSpediteur = null;
		if (losDto.getAuftragIId() != null) {
			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(losDto.getAuftragIId());
			sAuftragsnummer = auftragDto.getCNr();
			mapParameter.put("P_AUFTRAGNUMMER", sAuftragsnummer);

			mapParameter.put("P_AUFTRAGLIEFERTERMIN", auftragDto.getDLiefertermin());
			mapParameter.put("P_ROHS", Helper.short2Boolean(auftragDto.getBRoHs()));

			sLieferart = getLocaleFac().lieferartFindByIIdLocaleOhneExc(auftragDto.getLieferartIId(),
					theClientDto.getLocUi(), theClientDto);

			if (auftragDto.getSpediteurIId() != null) {
				sSpediteur = getMandantFac().spediteurFindByPrimaryKey(auftragDto.getSpediteurIId())
						.getCNamedesspediteurs();
			}
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(),
					theClientDto);
			sKunde = kundeDto.getPartnerDto().getCName1nachnamefirmazeile1();
		} else {

			// SP9528
			if (losDto.getKundeIId() != null) {
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(losDto.getKundeIId(), theClientDto);

				sKunde = kundeDto.getPartnerDto().getCName1nachnamefirmazeile1();
			} else {
				sKunde = "";
			}

			sLieferart = "";
			sSpediteur = "";
			sAuftragsnummer = "";
		}

		// PJ18849 Ziellager + Abbuchungslaeger

		LagerDto lagerDto_Ziellgaer = getLagerFac().lagerFindByPrimaryKey(losDto.getLagerIIdZiel());

		mapParameter.put("P_ZIELLAGER", lagerDto_Ziellgaer.getCNr());

		if (lagerDto_Ziellgaer.getPartnerIIdStandort() != null) {

			mapParameter.put("P_ZIELSTANDORT", getPartnerFac()
					.partnerFindByPrimaryKey(lagerDto_Ziellgaer.getPartnerIIdStandort(), theClientDto).getCKbez());

		}

		// Abbuchungslaeger
		String[] fieldnamesLola = new String[] { "F_LAGER", "F_STANDORT" };
		LoslagerentnahmeDto[] loslagerDtos = getFertigungFac().loslagerentnahmeFindByLosIId(losDto.getIId());
		ArrayList<Object[]> alLola = new ArrayList<Object[]>();
		for (int i = 0; i < loslagerDtos.length; i++) {
			LoslagerentnahmeDto dto = loslagerDtos[i];

			LagerDto lolaDto = getLagerFac().lagerFindByPrimaryKey(dto.getLagerIId());

			Object[] o = new Object[2];

			o[0] = lolaDto.getCNr();

			if (lolaDto.getPartnerIIdStandort() != null) {

				o[1] = getPartnerFac().partnerFindByPrimaryKey(lolaDto.getPartnerIIdStandort(), theClientDto)
						.getCKbez();

			}
			alLola.add(o);

		}

		Object[][] dataSubLola = new Object[alLola.size()][fieldnamesLola.length];
		dataSubLola = (Object[][]) alLola.toArray(dataSubLola);

		mapParameter.put("P_SUBREPORT_ABBUCHUNGSLAEGER", new LPDatenSubreport(dataSubLola, fieldnamesLola));

		KostenstelleDto kstDto = getSystemFac().kostenstelleFindByPrimaryKey(losDto.getKostenstelleIId());
		mapParameter.put("P_LIEFERART", sLieferart);
		mapParameter.put("P_SPEDITEUR", sSpediteur);
		mapParameter.put("P_KOSTENSTELLENUMMER", kstDto.getCNr());
		mapParameter.put("P_KUNDE", sKunde);
		mapParameter.put("P_LOSGROESSE", losDto.getNLosgroesse());

		mapParameter.put("P_PRODUKTIONSBEGINN", losDto.getTProduktionsbeginn());
		mapParameter.put("P_PRODUKTIONSENDE", losDto.getTProduktionsende());

		FertigungsgruppeDto fertGruppeDto = getStuecklisteFac()
				.fertigungsgruppeFindByPrimaryKey(losDto.getFertigungsgruppeIId());
		mapParameter.put("P_FERTIGUNGSGRUPPE", fertGruppeDto.getCBez());

		// Materialliste?
		String sMengenEinheit = "";
		if (losDto.getStuecklisteIId() != null) {
			StuecklisteDto stkDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(),
					theClientDto);
			// Einheit
			if (stkDto.getArtikelDto() != null) {
				if (stkDto.getArtikelDto().getEinheitCNr() != null) {
					sMengenEinheit = stkDto.getArtikelDto().getEinheitCNr();
				}
			}
			mapParameter.put("P_STUECKLISTEBEZEICHNUNG", stkDto.getArtikelDto().getArtikelsprDto().getCBez());
			mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG", stkDto.getArtikelDto().getArtikelsprDto().getCZbez());
			mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG2", stkDto.getArtikelDto().getArtikelsprDto().getCZbez2());
			mapParameter.put("P_STUECKLISTEKURZBEZEICHNUNG", stkDto.getArtikelDto().getArtikelsprDto().getCKbez());
			mapParameter.put("P_STUECKLISTENUMMER", stkDto.getArtikelDto().getCNr());
			mapParameter.put("P_STUECKLISTEREFERENZNUMMER", stkDto.getArtikelDto().getCReferenznr());
			mapParameter.put("P_INDEX", stkDto.getArtikelDto().getCIndex());
			mapParameter.put("P_REVISION", stkDto.getArtikelDto().getCRevision());
			mapParameter.put("P_REVISION", stkDto.getArtikelDto().getCRevision());

			mapParameter.put("P_STUECKLISTE_ERFASSUNGSFAKTOR", stkDto.getNErfassungsfaktor());

			if (stkDto.getArtikelDto().getVerpackungDto() != null) {
				mapParameter.put("P_STUECKLISTE_BAUFORM", stkDto.getArtikelDto().getVerpackungDto().getCBauform());
				mapParameter.put("P_STUECKLISTE_VERPACKUNGSART",
						stkDto.getArtikelDto().getVerpackungDto().getCVerpackungsart());
			}

			if (stkDto.getArtikelDto().getGeometrieDto() != null) {
				mapParameter.put("P_STUECKLISTE_BREITETEXT", stkDto.getArtikelDto().getGeometrieDto().getCBreitetext());
				mapParameter.put("P_STUECKLISTE_BREITE", stkDto.getArtikelDto().getGeometrieDto().getFBreite());
				mapParameter.put("P_STUECKLISTE_HOEHE", stkDto.getArtikelDto().getGeometrieDto().getFHoehe());
				mapParameter.put("P_STUECKLISTE_TIEFE", stkDto.getArtikelDto().getGeometrieDto().getFTiefe());
			}

			if (stkDto.getArtikelDto().getMontageDto() != null) {
				mapParameter.put("P_STUECKLISTE_RASTERSTEHEND",
						stkDto.getArtikelDto().getMontageDto().getFRasterstehend());
				mapParameter.put("P_STUECKLISTE_RASTERLIEGEND",
						stkDto.getArtikelDto().getMontageDto().getFRasterliegend());

			}

			// Zeichnungsnummer
			StuecklisteeigenschaftDto[] stuecklisteeigenschaftDtos = getStuecklisteFac()
					.stuecklisteeigenschaftFindByStuecklisteIId(losDto.getStuecklisteIId());
			ArrayList<Object[]> al = new ArrayList<Object[]>();
			for (int i = 0; i < stuecklisteeigenschaftDtos.length; i++) {
				StuecklisteeigenschaftDto dto = stuecklisteeigenschaftDtos[i];

				Object[] o = new Object[2];
				String sStklEigenschaftArt = dto.getStuecklisteeigenschaftartDto().getCBez();
				o[0] = sStklEigenschaftArt;
				o[1] = dto.getCBez();
				al.add(o);

				// Index und Materialplatz auch einzeln an Report uebergeben
				if (sStklEigenschaftArt.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_INDEX)) {
					mapParameter.put(P_STUECKLISTENEIGENSCHAFT_INDEX, dto.getCBez());
				}
				if (sStklEigenschaftArt.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_MATERIALPLATZ)) {
					mapParameter.put(P_STUECKLISTENEIGENSCHAFT_MATERIALPLATZ, dto.getCBez());
				}
			}

			// Stuecklisteeigenschaft fuer Subreport
			if (stuecklisteeigenschaftDtos.length > 0) {
				String[] fieldnames = new String[] { "F_EIGENSCHAFTART", "F_BEZEICHNUNG" };
				Object[][] dataSub = new Object[al.size()][fieldnames.length];
				dataSub = (Object[][]) al.toArray(dataSub);

				mapParameter.put("DATENSUBREPORT", new LPDatenSubreport(dataSub, fieldnames));
			}

			// Stuecklisteeigenschaften als einzelne Parameter fuer Index
			// und Materialplatz
			Hashtable<?, ?> htStklEigenschaften = getStuecklisteReportFac()
					.getStuecklisteEigenschaften(losDto.getStuecklisteIId(), theClientDto.getMandant(), theClientDto);
			if (htStklEigenschaften != null) {
				if (htStklEigenschaften.containsKey(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_INDEX)) {
					mapParameter.put(P_STUECKLISTENEIGENSCHAFT_INDEX,
							htStklEigenschaften.get(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_INDEX));
				}
				if (htStklEigenschaften
						.containsKey(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_MATERIALPLATZ)) {
					mapParameter.put(P_STUECKLISTENEIGENSCHAFT_MATERIALPLATZ, htStklEigenschaften
							.get(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_MATERIALPLATZ));
				}
			}

		} else {
			mapParameter.put("P_STUECKLISTEBEZEICHNUNG", losDto.getCProjekt());
			mapParameter.put("P_STUECKLISTENUMMER",
					getTextRespectUISpr("fert.materialliste", theClientDto.getMandant(), theClientDto.getLocUi()));
		}
		mapParameter.put("P_MENGENEINHEIT", sMengenEinheit);
		return mapParameter;
	}

	public ReportLosnachkalkulationDto getDataNachkalkulationMaterial(Integer losIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			ReportLosnachkalkulationDto nk = new ReportLosnachkalkulationDto();
			nk.setSBezeichnung(getTextRespectUISpr("lp.material", theClientDto.getMandant(), theClientDto.getLocUi()));
			LossollmaterialDto[] soll = getFertigungFac().lossollmaterialFindByLosIId(losIId);
			for (int i = 0; i < soll.length; i++) {
				nk.addiereZuSollpreis(soll[i].getNSollpreis().multiply(soll[i].getNMenge()));
				BigDecimal bdPreis = getFertigungFac().getAusgegebeneMengePreis(soll[i].getIId(), null, theClientDto);
				BigDecimal bdMenge = getFertigungFac().getAusgegebeneMenge(soll[i].getIId(), null, theClientDto);
				nk.addiereZuIstpreis(bdPreis.multiply(bdMenge));
			}
			return nk;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	private HashMap hmAddToAuslastungsvorschau(HashMap hm, Timestamp t, Integer artikelgruppeIId, double dSollZumbeginn,
			double dSollZumEnde, double bdPersonalverfuegbarkeit) {
		if (hm == null) {
			hm = new HashMap();
		}

		t = Helper.cutTimestamp(t);

		if (t.before(Helper.cutTimestamp(new Timestamp(System.currentTimeMillis())))) {
			t = Helper.cutTimestamp(new Timestamp(System.currentTimeMillis()));
		}

		if (hm.containsKey(t)) {

			HashMap hmSub = (HashMap) hm.get(t);

			if (hmSub.containsKey(artikelgruppeIId)) {
				double[] d = (double[]) hmSub.get(artikelgruppeIId);
				d[0] = d[0] + dSollZumbeginn;
				d[1] = d[1] + dSollZumEnde;
				d[2] = d[2] + bdPersonalverfuegbarkeit;
				hmSub.put(artikelgruppeIId, d);
				hm.put(t, hmSub);
			} else {
				double[] d = new double[3];
				d[0] = dSollZumbeginn;
				d[1] = dSollZumEnde;
				d[2] = bdPersonalverfuegbarkeit;
				hmSub.put(artikelgruppeIId, d);
				hm.put(t, hmSub);
			}

		} else {

			double[] d = new double[3];
			d[0] = dSollZumbeginn;
			d[1] = dSollZumEnde;
			d[2] = bdPersonalverfuegbarkeit;

			HashMap hmSub = new HashMap();

			hmSub.put(artikelgruppeIId, d);

			hm.put(t, hmSub);

		}

		return hm;
	}

	private HashMap hmAddToZeitentwicklung(HashMap hm, Timestamp t, java.sql.Timestamp tVon, Integer artikelgruppeIId,
			double dSoll, double dIst) {
		if (hm == null) {
			hm = new HashMap();
		}

		t = Helper.cutTimestamp(t);

		if (t.before(Helper.cutTimestamp(tVon))) {
			t = Helper.cutTimestamp(tVon);
		}

		if (hm.containsKey(Helper.cutTimestamp(t))) {

			HashMap hmSub = (HashMap) hm.get(t);

			if (hmSub.containsKey(artikelgruppeIId)) {
				double[] d = (double[]) hmSub.get(artikelgruppeIId);
				d[0] = d[0] + dSoll;
				d[1] = d[1] + dIst;
				hmSub.put(artikelgruppeIId, d);
				hm.put(t, hmSub);
			} else {
				double[] d = new double[2];
				d[0] = dSoll;
				d[1] = dIst;
				hmSub.put(artikelgruppeIId, d);
				hm.put(t, hmSub);
			}

		} else {

			double[] d = new double[2];
			d[0] = dSoll;
			d[1] = dIst;

			HashMap hmSub = new HashMap();

			hmSub.put(artikelgruppeIId, d);

			hm.put(t, hmSub);
		}

		return hm;
	}

	public LPDatenSubreport getSubreportSollmaterial(Integer losIId, Integer partnerIIdStandort,
			java.sql.Timestamp t_produktionsbeginn, LoslagerentnahmeDto[] lolaeDtos, TheClientDto theClientDto) {
		String[] fieldnames = new String[] { "Artikelnummer", "Bezeichnung", "Sollmenge", "AusgegebeneMenge",
				"FiktiverLagerstand", "Lagerstand", "Artikelklasse" };

		LinkedHashMap hmDaten = new LinkedHashMap();

		ArrayList alDatenSub = null;
		try {
			LossollmaterialDto[] sollmat = getFertigungFac().lossollmaterialFindByLosIId(losIId);

			alDatenSub = new ArrayList();

			for (int j = 0; j < sollmat.length; j++) {

				LossollmaterialDto sollmatDto = sollmat[j];

				Object[] oZeileSub = new Object[fieldnames.length];

				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(sollmatDto.getArtikelIId(),
						theClientDto);

				BigDecimal bdMenge = getFertigungFac().getAusgegebeneMenge(sollmat[j].getIId(), null, theClientDto);

				oZeileSub[0] = aDto.getCNr();
				oZeileSub[1] = aDto.getCBezAusSpr();
				oZeileSub[2] = sollmatDto.getNMenge();
				oZeileSub[3] = bdMenge;
				oZeileSub[4] = getInternebestellungFac().getFiktivenLagerstandZuZeitpunkt(aDto, theClientDto,
						t_produktionsbeginn, partnerIIdStandort);

				oZeileSub[5] = aDto.getCNr();

				// Lagerstand
				BigDecimal lagerstand = new BigDecimal(0);

				if (Helper.short2boolean(aDto.getBLagerbewirtschaftet())) {

					for (int i = 0; i < lolaeDtos.length; i++) {
						BigDecimal lagerstandEisneLagers = getLagerFac().getLagerstandOhneExc(aDto.getIId(),
								lolaeDtos[i].getLagerIId(), theClientDto);
						if (lagerstandEisneLagers != null) {
							lagerstand = lagerstand.add(lagerstandEisneLagers);
						}
					}
				} else {
					lagerstand = new BigDecimal(99999999);
				}
				oZeileSub[5] = lagerstand;

				if (aDto.getArtklaIId() != null) {
					oZeileSub[6] = getArtikelFac().artklaFindByPrimaryKey(aDto.getArtklaIId(), theClientDto)
							.getBezeichnung();
				}

				alDatenSub.add(oZeileSub);

			}

			Object[][] dataSub = new Object[alDatenSub.size()][fieldnames.length];
			dataSub = (Object[][]) alDatenSub.toArray(dataSub);

			return new LPDatenSubreport(dataSub, fieldnames);

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return null;
	}

	private Object[] befuelleArbeitsgaengeFuerLadeliste(LossollarbeitsplanDto lossollarbeitsplanDto,
			LossollarbeitsplanDto lossollarbeitsplanDto_NextAG,
			LossollarbeitsplanDto lossollarbeitsplanDto_VorherigerAG, Object[] oZeile, FLROffeneags flrOffeneags,
			TheClientDto theClientDto) {

		String queryString = "SELECT lgs FROM FLRLosgutschlecht lgs WHERE 1=1";

		ArrayList alDatenSub = new ArrayList();

		if (lossollarbeitsplanDto != null) {
			queryString += " AND lgs.lossollarbeitsplan_i_id=" + lossollarbeitsplanDto.getIId();
		} else if (lossollarbeitsplanDto_NextAG != null) {
			queryString += " AND lgs.lossollarbeitsplan_i_id=" + lossollarbeitsplanDto_NextAG.getIId();
		} else if (lossollarbeitsplanDto_VorherigerAG != null) {
			queryString += " AND lgs.lossollarbeitsplan_i_id=" + lossollarbeitsplanDto_VorherigerAG.getIId();
		}

		queryString += " ORDER BY lgs.i_id DESC";

		org.hibernate.Query query = FLRSessionFactory.getFactory().openSession().createQuery(queryString);

		List<?> resultList = query.list();
		Iterator it = resultList.iterator();

		BigDecimal bdSummeGut = BigDecimal.ZERO;
		BigDecimal bdSummeSchlecht = BigDecimal.ZERO;
		String kommentarJuengsteStueckrueckmeldung = null;
		java.util.Date datumJuengsteStueckrueckmeldung = null;

		String[] fieldnames = new String[] { "Gut", "Schlecht", "InArbeit", "Kommentar", "Zeitpunkt", "Personal",
				"PersonalErfasst", "PersonalAnlegen", "AnlageZeitpunkt" };

		int i = 0;
		while (it.hasNext()) {
			FLRLosgutschlecht lgs = (FLRLosgutschlecht) it.next();

			Object[] oZeileSub = new Object[fieldnames.length];

			oZeileSub[0] = lgs.getN_gut();
			oZeileSub[1] = lgs.getN_schlecht();
			oZeileSub[2] = lgs.getN_inarbeit();
			oZeileSub[3] = lgs.getC_kommentar();

			if (lgs.getFlrzeitdaten() != null) {
				oZeileSub[4] = lgs.getFlrzeitdaten().getT_zeit();
				oZeileSub[5] = HelperServer.formatPersonAusFLRPErsonal(lgs.getFlrzeitdaten().getFlrpersonal());
			} else if (lgs.getFlrmaschinenzeitdaten() != null) {
				oZeileSub[4] = lgs.getFlrmaschinenzeitdaten().getT_von();
				if (lgs.getFlrmaschinenzeitdaten().getFlrpersonal_gestartet() != null) {
					oZeileSub[5] = HelperServer
							.formatPersonAusFLRPErsonal(lgs.getFlrmaschinenzeitdaten().getFlrpersonal_gestartet());
				}
			} else {
				oZeileSub[4] = lgs.getT_zeitpunkt();
			}

			if (lgs.getFlrpersonal_erfasst() != null) {
				oZeileSub[6] = lgs.getFlrpersonal_erfasst().getC_kurzzeichen();
			}

			if (lgs.getFlrpersonal_anlegen() != null) {
				oZeileSub[7] = lgs.getFlrpersonal_anlegen().getC_kurzzeichen();
			}

			oZeileSub[8] = lgs.getT_anlegen();

			alDatenSub.add(oZeileSub);

			bdSummeGut = bdSummeGut.add(lgs.getN_gut());
			bdSummeSchlecht = bdSummeSchlecht.add(lgs.getN_schlecht());

			if (i == 0) {
				kommentarJuengsteStueckrueckmeldung = lgs.getC_kommentar();
				datumJuengsteStueckrueckmeldung = (java.util.Date) oZeileSub[4];
			}

			i++;
		}

		Object[][] dataSub = new Object[alDatenSub.size()][fieldnames.length];
		dataSub = (Object[][]) alDatenSub.toArray(dataSub);

		LPDatenSubreport subreport = new LPDatenSubreport(dataSub, fieldnames);

		if (lossollarbeitsplanDto != null) {
			oZeile[LADELISTE_AKTUELLER_ARBEITGANG_SUBREPORT_GUT_SCHLECHT] = subreport;

			oZeile[LADELISTE_AKTUELLER_ARBEITGANG_ARBEITGANGNUMMER] = lossollarbeitsplanDto.getIArbeitsgangnummer();
			if (lossollarbeitsplanDto.getIMaschinenversatztage() != null) {
				oZeile[LADELISTE_AKTUELLER_ARBEITGANG_AG_BEGINN] = Helper.addiereTageZuDatum(
						flrOffeneags.getFlrlos().getT_produktionsbeginn(),
						lossollarbeitsplanDto.getIMaschinenversatztage());
			}
			oZeile[LADELISTE_AKTUELLER_ARBEITGANG_UNTERARBEITSGANGNUMMER] = lossollarbeitsplanDto
					.getIUnterarbeitsgang();
			oZeile[LADELISTE_AKTUELLER_ARBEITGANG_SUMME_GUTSTUECK] = bdSummeGut;
			oZeile[LADELISTE_AKTUELLER_ARBEITGANG_SUMME_SCHLECHTSTUECK] = bdSummeSchlecht;
			oZeile[LADELISTE_AKTUELLER_ARBEITGANG_DATUM_JUENGSTE_STUECKRUECKMELDUNG] = datumJuengsteStueckrueckmeldung;
			oZeile[LADELISTE_AKTUELLER_ARBEITGANG_KOMMENTAR_JUENGSTE_STUECKRUECKMELDUNG] = kommentarJuengsteStueckrueckmeldung;
			oZeile[LADELISTE_AKTUELLER_ARBEITGANG_KOMMENTAR] = lossollarbeitsplanDto.getCKomentar();
			oZeile[LADELISTE_AKTUELLER_ARBEITGANG_LANGTEXT] = lossollarbeitsplanDto.getXText();

			oZeile[LADELISTE_AKTUELLER_ARBEITGANG_FERTIG] = Helper.short2Boolean(lossollarbeitsplanDto.getBFertig());
			oZeile[LADELISTE_AKTUELLER_ARBEITGANG_FORTSCHRITT] = lossollarbeitsplanDto.getFFortschritt();

			ArtikelDto aDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(lossollarbeitsplanDto.getArtikelIIdTaetigkeit(), theClientDto);

			oZeile[LADELISTE_AKTUELLER_ARBEITGANG_ARTIKELNUMMER_TAETIGKEIT] = aDto.getCNr();
			oZeile[LADELISTE_AKTUELLER_ARBEITGANG_BEZEICHNUNG_TAETIGKEIT] = aDto.getCBezAusSpr();
			oZeile[LADELISTE_AKTUELLER_ARBEITGANG_ZUSATZBEZEICHNUNG_TAETIGKEIT] = aDto.getCZBezAusSpr();

		}

		if (lossollarbeitsplanDto_NextAG != null) {
			oZeile[LADELISTE_NAECHSTER_ARBEITGANG_SUBREPORT_GUT_SCHLECHT] = subreport;

			oZeile[LADELISTE_NAECHSTER_ARBEITGANG_ARBEITGANGNUMMER] = lossollarbeitsplanDto_NextAG
					.getIArbeitsgangnummer();
			if (lossollarbeitsplanDto_NextAG.getIMaschinenversatztage() != null) {
				oZeile[LADELISTE_NAECHSTER_ARBEITGANG_AG_BEGINN] = Helper.addiereTageZuDatum(
						flrOffeneags.getFlrlos().getT_produktionsbeginn(),
						lossollarbeitsplanDto_NextAG.getIMaschinenversatztage());
			}
			oZeile[LADELISTE_NAECHSTER_ARBEITGANG_UNTERARBEITSGANGNUMMER] = lossollarbeitsplanDto_NextAG
					.getIUnterarbeitsgang();
			oZeile[LADELISTE_NAECHSTER_ARBEITGANG_SUMME_GUTSTUECK] = bdSummeGut;
			oZeile[LADELISTE_NAECHSTER_ARBEITGANG_SUMME_SCHLECHTSTUECK] = bdSummeSchlecht;
			oZeile[LADELISTE_NAECHSTER_ARBEITGANG_DATUM_JUENGSTE_STUECKRUECKMELDUNG] = datumJuengsteStueckrueckmeldung;
			oZeile[LADELISTE_NAECHSTER_ARBEITGANG_KOMMENTAR_JUENGSTE_STUECKRUECKMELDUNG] = kommentarJuengsteStueckrueckmeldung;
			oZeile[LADELISTE_NAECHSTER_ARBEITGANG_KOMMENTAR] = lossollarbeitsplanDto_NextAG.getCKomentar();
			oZeile[LADELISTE_NAECHSTER_ARBEITGANG_LANGTEXT] = lossollarbeitsplanDto_NextAG.getXText();

			oZeile[LADELISTE_NAECHSTER_ARBEITGANG_FERTIG] = Helper
					.short2Boolean(lossollarbeitsplanDto_NextAG.getBFertig());
			oZeile[LADELISTE_NAECHSTER_ARBEITGANG_FORTSCHRITT] = lossollarbeitsplanDto_NextAG.getFFortschritt();

			ArtikelDto aDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(lossollarbeitsplanDto_NextAG.getArtikelIIdTaetigkeit(), theClientDto);

			oZeile[LADELISTE_NAECHSTER_ARBEITGANG_ARTIKELNUMMER_TAETIGKEIT] = aDto.getCNr();
			oZeile[LADELISTE_NAECHSTER_ARBEITGANG_BEZEICHNUNG_TAETIGKEIT] = aDto.getCBezAusSpr();
			oZeile[LADELISTE_NAECHSTER_ARBEITGANG_ZUSATZBEZEICHNUNG_TAETIGKEIT] = aDto.getCZBezAusSpr();

		}

		if (lossollarbeitsplanDto_VorherigerAG != null) {
			oZeile[LADELISTE_VORHERIGER_ARBEITGANG_SUBREPORT_GUT_SCHLECHT] = subreport;

			oZeile[LADELISTE_VORHERIGER_ARBEITGANG_ARBEITGANGNUMMER] = lossollarbeitsplanDto_VorherigerAG
					.getIArbeitsgangnummer();
			if (lossollarbeitsplanDto_VorherigerAG.getIMaschinenversatztage() != null) {
				oZeile[LADELISTE_VORHERIGER_ARBEITGANG_AG_BEGINN] = Helper.addiereTageZuDatum(
						flrOffeneags.getFlrlos().getT_produktionsbeginn(),
						lossollarbeitsplanDto_VorherigerAG.getIMaschinenversatztage());
			}
			oZeile[LADELISTE_VORHERIGER_ARBEITGANG_UNTERARBEITSGANGNUMMER] = lossollarbeitsplanDto_VorherigerAG
					.getIUnterarbeitsgang();
			oZeile[LADELISTE_VORHERIGER_ARBEITGANG_SUMME_GUTSTUECK] = bdSummeGut;
			oZeile[LADELISTE_VORHERIGER_ARBEITGANG_SUMME_SCHLECHTSTUECK] = bdSummeSchlecht;
			oZeile[LADELISTE_VORHERIGER_ARBEITGANG_DATUM_JUENGSTE_STUECKRUECKMELDUNG] = datumJuengsteStueckrueckmeldung;
			oZeile[LADELISTE_VORHERIGER_ARBEITGANG_KOMMENTAR_JUENGSTE_STUECKRUECKMELDUNG] = kommentarJuengsteStueckrueckmeldung;
			oZeile[LADELISTE_VORHERIGER_ARBEITGANG_KOMMENTAR] = lossollarbeitsplanDto_VorherigerAG.getCKomentar();
			oZeile[LADELISTE_VORHERIGER_ARBEITGANG_LANGTEXT] = lossollarbeitsplanDto_VorherigerAG.getXText();

			oZeile[LADELISTE_VORHERIGER_ARBEITGANG_FERTIG] = Helper
					.short2Boolean(lossollarbeitsplanDto_VorherigerAG.getBFertig());
			oZeile[LADELISTE_VORHERIGER_ARBEITGANG_FORTSCHRITT] = lossollarbeitsplanDto_VorherigerAG.getFFortschritt();

			ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
					lossollarbeitsplanDto_VorherigerAG.getArtikelIIdTaetigkeit(), theClientDto);

			oZeile[LADELISTE_VORHERIGER_ARBEITGANG_ARTIKELNUMMER_TAETIGKEIT] = aDto.getCNr();
			oZeile[LADELISTE_VORHERIGER_ARBEITGANG_BEZEICHNUNG_TAETIGKEIT] = aDto.getCBezAusSpr();
			oZeile[LADELISTE_VORHERIGER_ARBEITGANG_ZUSATZBEZEICHNUNG_TAETIGKEIT] = aDto.getCZBezAusSpr();

		}
		return oZeile;

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAuslieferliste(java.sql.Timestamp tStichtag, Integer kundeIId,
			boolean bNurNachLosEndeTerminSortiert, TheClientDto theClientDto) {
		this.useCase = UC_AUSLIEFERLISTE;

		HashMap hmPositionen = new HashMap();

		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT lr,(SELECT sum(la.n_menge) FROM FLRLosablieferung la WHERE la.los_i_id=lr.i_id   ) FROM FLRLosReport lr WHERE lr.t_produktionsbeginn <='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tStichtag.getTime())) + "' AND lr.status_c_nr IN ('"
				+ LocaleFac.STATUS_ANGELEGT + "','" + LocaleFac.STATUS_IN_PRODUKTION + "','"
				+ LocaleFac.STATUS_TEILERLEDIGT + "') AND lr.mandant_c_nr='" + theClientDto.getMandant()
				+ "' ORDER BY lr.t_produktionsende ASC , lr.c_nr ASC";

		Query qResult = session.createQuery(sQuery);
		List<?> results = qResult.list();
		ArrayList alDaten = new ArrayList();
		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();

			FLRLosReport los = (FLRLosReport) o[0];

			// PJ19668
			// Kunde
			Integer kundeIIdZeile = null;
			if (los.getFlrkunde() != null) {
				kundeIIdZeile = los.getFlrkunde().getI_id();
			} else {
				if (los.getFlrauftragposition() != null) {
					kundeIIdZeile = los.getFlrauftragposition().getFlrauftrag().getKunde_i_id_auftragsadresse();
				} else {
					if (los.getFlrauftrag() != null) {
						kundeIIdZeile = los.getFlrauftrag().getKunde_i_id_auftragsadresse();
					}
				}
			}

			if (kundeIId != null) {

				if (!kundeIId.equals(kundeIIdZeile)) {
					continue;
				}

			}

			BigDecimal bdAbgeliefert = new BigDecimal(0);

			if (o[1] != null) {
				bdAbgeliefert = (BigDecimal) o[1];
			}

			if (bdAbgeliefert.doubleValue() < los.getN_losgroesse().doubleValue()) {
				if (los.getFlrauftragposition() != null && !hmPositionen.containsKey(los.getFlrauftragposition())) {
					hmPositionen.put(los.getFlrauftragposition().getI_id(), "");
				}

				Object[] oZeile = new Object[AUSLIEFERLISTE_SPALTENANZAHL];
				oZeile[AUSLIEFERLISTE_AUFTRAGSNUMMER] = "";
				oZeile[AUSLIEFERLISTE_LOSNUMMER] = "";
				oZeile[AUSLIEFERLISTE_KUNDE] = "";
				try {
					// PJ18849
					LoslagerentnahmeDto[] lolaeDtos = getFertigungFac().loslagerentnahmeFindByLosIId(los.getI_id());
					Integer partnerIIdStandort = null;
					if (lolaeDtos != null && lolaeDtos.length > 0) {
						partnerIIdStandort = getLagerFac().getPartnerIIdStandortEinesLagers(lolaeDtos[0].getLagerIId());

					}

					// SP590
					if (los.getFlrkunde() != null) {
						KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(los.getFlrkunde().getI_id(),
								theClientDto);
						oZeile[AUSLIEFERLISTE_KUNDE] = kundeDto.getPartnerDto().formatAnrede();
					}

					BigDecimal bdGeliefert = new BigDecimal(0);
					for (Iterator<?> iter2 = los.getAblieferungset().iterator(); iter2.hasNext();) {
						FLRLosablieferung item = (FLRLosablieferung) iter2.next();
						bdGeliefert = bdGeliefert.add(item.getN_menge());
					}
					oZeile[AUSLIEFERLISTE_GELIEFERT] = bdGeliefert;
					if (los.getFlrstueckliste() != null) {
						ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
								los.getFlrstueckliste().getFlrartikel().getI_id(), theClientDto);
						oZeile[AUSLIEFERLISTE_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
						oZeile[AUSLIEFERLISTE_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCZbez();
						oZeile[AUSLIEFERLISTE_ZUSATZBEZEICHNUNG2] = artikelDto.getArtikelsprDto().getCZbez2();
						oZeile[AUSLIEFERLISTE_ARTIKELNUMMER] = los.getFlrstueckliste().getFlrartikel().getC_nr();
						oZeile[AUSLIEFERLISTE_LAGERSTAND] = getLagerFac().getLagerstandAllerLagerEinesMandanten(
								los.getFlrstueckliste().getFlrartikel().getI_id(), theClientDto);
						// Offene Fehlmengen
						oZeile[AUSLIEFERLISTE_FEHLMENGE] = getFehlmengeFac()
								.getAnzahlFehlmengeEinesArtikels(artikelDto.getIId(), theClientDto);

						LossollarbeitsplanDto[] lossollarbeitsplanDto = getFertigungFac()
								.lossollarbeitsplanFindByLosIId(los.getI_id());

						BigDecimal bdFertigungszeit = new BigDecimal(0);
						for (int j = 0; lossollarbeitsplanDto.length > j; j++) {
							bdFertigungszeit = bdFertigungszeit.add(lossollarbeitsplanDto[j].getNGesamtzeit());
						}
						oZeile[AUSLIEFERLISTE_FERTIGUNGSZEIT] = bdFertigungszeit;

						// Rahmenbestellt
						Hashtable<?, ?> htRahmenbestellt = getArtikelbestelltFac()
								.getAnzahlRahmenbestellt(artikelDto.getIId(), theClientDto);
						if (htRahmenbestellt.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
							BigDecimal bdAnzahlRahmenbestellt = (BigDecimal) htRahmenbestellt
									.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
							oZeile[AUSLIEFERLISTE_RAHMEN_BESTELLT] = bdAnzahlRahmenbestellt;
						}
						oZeile[AUSLIEFERLISTE_RESERVIERUNGEN] = getReservierungFac()
								.getAnzahlReservierungen(artikelDto.getIId(), theClientDto);

						oZeile[AUSLIEFERLISTE_RAHMENRESERVIERUNGEN] = getReservierungFac()
								.getAnzahlRahmenreservierungen(artikelDto.getIId(), theClientDto);

					} else {
						oZeile[AUSLIEFERLISTE_BEZEICHNUNG] = los.getC_projekt();
						oZeile[AUSLIEFERLISTE_ZUSATZBEZEICHNUNG] = null;
						oZeile[AUSLIEFERLISTE_ZUSATZBEZEICHNUNG2] = null;
						oZeile[AUSLIEFERLISTE_ARTIKELNUMMER] = getTextRespectUISpr("fert.materialliste",
								theClientDto.getMandant(), theClientDto.getLocUi());
					}

					oZeile[AUSLIEFERLISTE_LOSKLASSEN] = getLosLosKlassenAlsString(los.getI_id());

					LossollmaterialDto[] sollmatDtos = getFertigungFac().lossollmaterialFindByLosIId(los.getI_id());

					Boolean bZuwenigauflagerzumbeginn = new Boolean(false);

					for (int i = 0; i < sollmatDtos.length; i++) {
						BigDecimal bd = getFertigungFac().getAusgegebeneMenge(sollmatDtos[i].getIId(), null,
								theClientDto);

						BigDecimal nochbenoetigt = sollmatDtos[i].getNMenge().subtract(bd);

						if (nochbenoetigt.doubleValue() > 0) {
							ArtikelDto aDtoSoll = getArtikelFac()
									.artikelFindByPrimaryKeySmall(sollmatDtos[i].getArtikelIId(), theClientDto);
							BigDecimal bdfiktlag = getInternebestellungFac().getFiktivenLagerstandZuZeitpunkt(aDtoSoll,
									theClientDto, new Timestamp(los.getT_produktionsbeginn().getTime()),
									partnerIIdStandort);

							if (bdfiktlag.doubleValue() < nochbenoetigt.doubleValue()) {
								bZuwenigauflagerzumbeginn = true;
								break;
							}

						}

					}
					oZeile[AUSLIEFERLISTE_ZUWENIG_AUF_LAGER_ZUM_ZEITPUNKT] = bZuwenigauflagerzumbeginn;

					Calendar gc = Calendar.getInstance(theClientDto.getLocUi());
					gc.setTime(los.getT_produktionsbeginn());
					oZeile[AUSLIEFERLISTE_KALENDERWOCHE] = new Integer(gc.get(Calendar.WEEK_OF_YEAR));
					oZeile[AUSLIEFERLISTE_LOSGROESSE] = los.getN_losgroesse();
					oZeile[AUSLIEFERLISTE_LOSNUMMER] = los.getC_nr();
					oZeile[AUSLIEFERLISTE_FERTIGUNGSGRUPPE] = los.getFlrfertigungsgruppe().getC_bez();

					oZeile[AUSLIEFERLISTE_MATERIAL_SUBREPORT] = getSubreportSollmaterial(los.getI_id(),
							partnerIIdStandort, new Timestamp(los.getT_produktionsbeginn().getTime()), lolaeDtos,
							theClientDto);

					oZeile[AUSLIEFERLISTE_BEGINN] = los.getT_produktionsbeginn();
					oZeile[AUSLIEFERLISTE_ENDE] = los.getT_produktionsende();
					oZeile[AUSLIEFERLISTE_LOSSTATUS] = los.getStatus_c_nr();

					LossollarbeitsplanDto[] sollDtos = getFertigungFac().lossollarbeitsplanFindByLosIId(los.getI_id());

					if (sollDtos.length > 0) {

						AuftragzeitenDto[] zeiten = getZeiterfassungFac().getAllZeitenEinesBeleges(
								LocaleFac.BELEGART_LOS, los.getI_id(), null, null, null, null,
								ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ARTIKEL, theClientDto);

						Object[][] oSubData = new Object[sollDtos.length][13];

						for (int i = 0; i < sollDtos.length; i++) {

							oSubData[i][0] = sollDtos[i].getIArbeitsgangnummer();

							ArtikelDto artikelDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(sollDtos[i].getArtikelIIdTaetigkeit(), theClientDto);
							oSubData[i][1] = artikelDto.getCNr();
							oSubData[i][2] = artikelDto.formatBezeichnung();
							oSubData[i][3] = sollDtos[i].getNGesamtzeit();

							// Istzeitdaten
							BigDecimal bdZeit = new BigDecimal(0);
							for (int j = 0; j < zeiten.length; j++) {
								if (sollDtos[i].getArtikelIIdTaetigkeit().equals(zeiten[j].getArtikelIId())) {
									bdZeit = bdZeit.add(new BigDecimal(zeiten[j].getDdDauer().doubleValue()));
								}
							}

							oSubData[i][4] = bdZeit;
							oSubData[i][5] = Helper.short2Boolean(sollDtos[i].getBFertig());

							if (artikelDto.getArtgruIId() != null) {
								oSubData[i][6] = getArtikelFac()
										.artgruFindByPrimaryKey(artikelDto.getArtgruIId(), theClientDto).getCNr();
							}

							oSubData[i][7] = sollDtos[i].getIUnterarbeitsgang();

							if (sollDtos[i].getMaschineIId() != null) {
								MaschineDto maschineDto = getZeiterfassungFac()
										.maschineFindByPrimaryKey(sollDtos[i].getMaschineIId());
								oSubData[i][8] = maschineDto.getCIdentifikationsnr();
								oSubData[i][9] = maschineDto.getCBez();
							}
							if (sollDtos[i].getIMaschinenversatztage() != null) {
								oSubData[i][10] = Helper.addiereTageZuTimestamp(
										new Timestamp(los.getT_produktionsbeginn().getTime()),
										sollDtos[i].getIMaschinenversatztage());
							}

							BigDecimal gut = new BigDecimal(0);
							BigDecimal schlecht = new BigDecimal(0);
							LosgutschlechtDto[] losgutschlechDtos = getFertigungFac()
									.losgutschlechtFindByLossollarbeitsplanIId(sollDtos[i].getIId());

							for (int j = 0; j < losgutschlechDtos.length; j++) {
								gut = gut.add(losgutschlechDtos[j].getNGut());
								schlecht = schlecht.add(losgutschlechDtos[j].getNSchlecht());
							}
							oSubData[i][11] = gut;
							oSubData[i][12] = schlecht;
						}

						String[] fieldnames = new String[] { "F_AGNUMMER", "F_ARTIKEL", "F_BEZEICHNUNG", "F_SOLLZEIT",
								"F_ISTZEIT", "F_FERTIG", "F_ARTIKELGRUPPE", "F_UAGNUMMER", "F_MASCHINENIDENTIFIKATION",
								"F_MASCHINENBEZEICHNUNG", "F_AGBEGINN", "F_GUTSTUECK", "F_SCHLECHTSTUECK" };
						oZeile[AUSLIEFERLISTE_ARBEITSGAENGE] = new LPDatenSubreport(oSubData, fieldnames);
					}

				} catch (RemoteException ex) {
					throwEJBExceptionLPRespectOld(ex);
				}

				// PJ 15009

				String queryf = "select sum(flrfehlmenge.n_menge) FROM FLRFehlmenge flrfehlmenge WHERE flrfehlmenge.flrlossollmaterial.flrlos = "
						+ los.getI_id() + " AND flrfehlmenge.n_menge>0";
				Session session2 = FLRSessionFactory.getFactory().openSession();
				Query query = session2.createQuery(queryf);
				List<?> results2 = query.list();

				oZeile[AUSLIEFERLISTE_LOSHATFEHLMENGE] = new Boolean(false);
				if (results.size() > 0) {
					BigDecimal bd = (BigDecimal) results2.iterator().next();

					if (bd != null && bd.doubleValue() > 0) {
						oZeile[AUSLIEFERLISTE_LOSHATFEHLMENGE] = new Boolean(true);
					}
				}
				session2.close();

				oZeile[AUSLIEFERLISTE_PROJEKTKOMMENTAR] = los.getC_projekt();
				if (los.getFlrstueckliste() == null) {
					oZeile[AUSLIEFERLISTE_MATERIALLISTE] = new Boolean(true);

				} else {
					oZeile[AUSLIEFERLISTE_MATERIALLISTE] = new Boolean(false);

					if (los.getFlrstueckliste().getFlrartikel().getFlrartikelgruppe() != null) {
						oZeile[AUSLIEFERLISTE_LOSARTIKELGRUPPE] = los.getFlrstueckliste().getFlrartikel()
								.getFlrartikelgruppe().getC_nr();
					}
					if (los.getFlrstueckliste().getFlrartikel().getFlrartikelklasse() != null) {
						oZeile[AUSLIEFERLISTE_LOSARTIKELKLASSE] = los.getFlrstueckliste().getFlrartikel()
								.getFlrartikelklasse().getC_nr();
					}

				}
				if (los.getFlrauftragposition() != null || los.getFlrauftrag() != null) {

					if (los.getFlrauftragposition() != null) {
						oZeile = befuelleMitAuftragsdaten(oZeile, los.getFlrauftragposition().getFlrauftrag(),
								los.getFlrauftragposition(), theClientDto);
					} else {
						oZeile = befuelleMitAuftragsdaten(oZeile, los.getFlrauftrag(), null, theClientDto);

					}

				}

				if (oZeile[AUSLIEFERLISTE_ABLIEFERDATUM] == null) {
					oZeile[AUSLIEFERLISTE_ABLIEFERDATUM] = new java.sql.Date(los.getT_produktionsende().getTime());

				}

				// PJ20205
				if (((java.sql.Date) oZeile[AUSLIEFERLISTE_ABLIEFERDATUM]).after(tStichtag)) {

				} else {

					alDaten.add(oZeile);
				}
			}

		}

		session.close();

		if (!bNurNachLosEndeTerminSortiert) {

			session = FLRSessionFactory.getFactory().openSession();
			sQuery = "SELECT ap FROM FLRAuftragposition ap WHERE ap.flrauftrag.auftragstatus_c_nr IN ('"
					+ LocaleFac.STATUS_OFFEN + "','" + LocaleFac.STATUS_TEILERLEDIGT
					+ "') AND ap.flrauftrag.mandant_c_nr='" + theClientDto.getMandant()
					+ "' AND ap.n_menge IS NOT NULL";

			if (kundeIId != null) {
				sQuery += " AND ap.flrauftrag.kunde_i_id_auftragsadresse=" + kundeIId;
			}

			qResult = session.createQuery(sQuery);
			results = qResult.list();

			resultListIterator = results.iterator();
			while (resultListIterator.hasNext()) {
				FLRAuftragposition ap = (FLRAuftragposition) resultListIterator.next();

				Object[] oZeile = new Object[AUSLIEFERLISTE_SPALTENANZAHL];

				oZeile[AUSLIEFERLISTE_AUFTRAGSNUMMER] = "";
				oZeile[AUSLIEFERLISTE_LOSNUMMER] = "";
				oZeile = befuelleMitAuftragsdaten(oZeile, ap.getFlrauftrag(), ap, theClientDto);

				if (!hmPositionen.containsKey(ap.getI_id())) {

					if (((java.sql.Date) oZeile[AUSLIEFERLISTE_ABLIEFERDATUM]).after(tStichtag)) {

					} else {
						alDaten.add(oZeile);

					}
				}

			}

			// sortieren

			boolean bSortiertNachEndeBeiRahmen = false;
			try {
				ParametermandantDto parametermandantDto = getParameterFac().getMandantparameter(
						theClientDto.getMandant(), ParameterFac.KATEGORIE_FERTIGUNG,
						ParameterFac.PARAMETER_AUSLIEFERLISTE_BEI_RAHMEN_NACH_ENDETERMIN);
				bSortiertNachEndeBeiRahmen = (Boolean) parametermandantDto.getCWertAsObject();
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			for (int k = alDaten.size() - 1; k > 0; --k) {
				for (int j = 0; j < k; ++j) {
					Object[] a1 = (Object[]) alDaten.get(j);
					Object[] a2 = (Object[]) alDaten.get(j + 1);

					java.sql.Date s1 = (java.sql.Date) a1[AUSLIEFERLISTE_ABLIEFERDATUM];
					java.sql.Date s2 = (java.sql.Date) a2[AUSLIEFERLISTE_ABLIEFERDATUM];

					// PJ20549
					if (bSortiertNachEndeBeiRahmen == true) {
						if (a1[AUSLIEFERLISTE_AUFTRAGART] != null
								&& a1[AUSLIEFERLISTE_AUFTRAGART].equals(AuftragServiceFac.AUFTRAGART_RAHMEN)
								&& a1[AUSLIEFERLISTE_ENDE] != null) {
							s1 = new java.sql.Date(((java.util.Date) a1[AUSLIEFERLISTE_ENDE]).getTime());
						}
						if (a2[AUSLIEFERLISTE_AUFTRAGART] != null
								&& a2[AUSLIEFERLISTE_AUFTRAGART].equals(AuftragServiceFac.AUFTRAGART_RAHMEN)
								&& a2[AUSLIEFERLISTE_ENDE] != null) {
							s2 = new java.sql.Date(((java.util.Date) a2[AUSLIEFERLISTE_ENDE]).getTime());
						}
					}

					s1 = Helper.cutDate(s1);
					s2 = Helper.cutDate(s2);

					if (s1.getTime() > s2.getTime()) {
						alDaten.set(j, a2);
						alDaten.set(j + 1, a1);
					} else if (s1.getTime() == s2.getTime()) {
						String k1 = (String) a1[AUSLIEFERLISTE_KUNDE];
						String k2 = (String) a2[AUSLIEFERLISTE_KUNDE];

						if (k1.compareTo(k2) > 0) {
							alDaten.set(j, a2);
							alDaten.set(j + 1, a1);
						} else if (k1.compareTo(k2) == 0) {
							String t1 = (String) a1[AUSLIEFERLISTE_AUFTRAGSNUMMER];
							String t2 = (String) a2[AUSLIEFERLISTE_AUFTRAGSNUMMER];

							if (t1.compareTo(t2) > 0) {
								alDaten.set(j, a2);
								alDaten.set(j + 1, a1);
							} else if (t1.compareTo(t2) == 0) {
								String l1 = (String) a1[AUSLIEFERLISTE_LOSNUMMER];
								String l2 = (String) a2[AUSLIEFERLISTE_LOSNUMMER];

								if (l1.compareTo(l2) > 0) {
									alDaten.set(j, a2);
									alDaten.set(j + 1, a1);
								}
							}
						}
					}
				}
			}
		}
		data = new Object[alDaten.size()][AUSLIEFERLISTE_SPALTENANZAHL];
		data = (Object[][]) alDaten.toArray(data);

		Map<String, Object> mapParameter = new TreeMap<String, Object>();
		mapParameter.put("P_STICHTAG", new Timestamp(tStichtag.getTime() - 3600000 * 24));
		mapParameter.put("P_NUR_LOSE_NACH_ENDETERMIN_SORTIERT", bNurNachLosEndeTerminSortiert);

		if (kundeIId != null) {
			mapParameter.put("P_KUNDE",
					getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto).getPartnerDto().formatFixName1Name2());
		}

		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_AUSLIEFERLISTE,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	private String getLosLosKlassenAlsString(Integer losIId) {

		Session sessionLosklasse = FLRSessionFactory.getFactory().openSession();
		String queryLosklasse = "FROM FLRLoslosklasse l where l.los_i_id=" + losIId;

		org.hibernate.Query loslosklasse = sessionLosklasse.createQuery(queryLosklasse);

		List resultListLosklasse = loslosklasse.list();

		Iterator resultListIteratorLosklasse = resultListLosklasse.iterator();

		String losklassen = "";
		while (resultListIteratorLosklasse.hasNext()) {
			com.lp.server.fertigung.fastlanereader.generated.FLRLoslosklasse lk = (com.lp.server.fertigung.fastlanereader.generated.FLRLoslosklasse) resultListIteratorLosklasse
					.next();

			losklassen += lk.getFlrlosklasse().getC_nr() + ",";

		}

		sessionLosklasse.close();

		return losklassen;
	}

	private Object[] befuelleMitAuftragsdaten(Object[] oZeile, FLRAuftragReport flrAuftrag, FLRAuftragposition position,
			TheClientDto theClientDto) {

		try {
			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(flrAuftrag.getI_id());

			if (position != null) {
				oZeile[AUSLIEFERLISTE_AUFTRAGSPOSITIONNUMMER] = position.getI_sort();

				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(flrAuftrag.getKunde_i_id_auftragsadresse(),
						theClientDto);
				oZeile[AUSLIEFERLISTE_KUNDE] = kundeDto.getPartnerDto().formatAnrede();

				oZeile[AUSLIEFERLISTE_AUFTRAGSPOENALE] = Helper.short2Boolean(auftragDto.getBPoenale());

				if (position.getFlrartikel() != null) {

					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(position.getFlrartikel().getI_id(), theClientDto);

					oZeile[AUSLIEFERLISTE_AUFTRAGARTIKEL] = artikelDto.getCNr();
					oZeile[AUSLIEFERLISTE_AUFTRAGARTIKEL_LAGERSTAND] = getLagerFac()
							.getLagerstandAllerLagerEinesMandanten(artikelDto.getIId(), theClientDto);

					oZeile[AUSLIEFERLISTE_AUFTRAGARTIKELBEZEICHNUNG] = artikelDto.formatBezeichnung();
					if (artikelDto.getArtgruIId() != null) {
						oZeile[AUSLIEFERLISTE_AUFTRAGARTIKELGRUPPE] = getArtikelFac()
								.artgruFindByPrimaryKey(artikelDto.getArtgruIId(), theClientDto).getCNr();
					}
					if (artikelDto.getArtklaIId() != null) {
						oZeile[AUSLIEFERLISTE_AUFTRAGARTIKELKLASSE] = getArtikelFac()
								.artklaFindByPrimaryKey(artikelDto.getArtklaIId(), theClientDto).getCNr();
					}

				} else {
					oZeile[AUSLIEFERLISTE_AUFTRAGARTIKELBEZEICHNUNG] = position.getC_bez();
				}

				oZeile[AUSLIEFERLISTE_AUFTRAGPOSITIONSMENGE] = position.getN_menge();

				oZeile[AUSLIEFERLISTE_AUFTRAG_OFFENEMENGE] = position.getN_offenemenge();

				if (auftragDto.getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_RAHMEN)) {
					oZeile[AUSLIEFERLISTE_AUFTRAG_OFFENERAHMENMENGE] = position.getN_offenerahmenmenge();
				}

				oZeile[AUSLIEFERLISTE_AUFTRAG_POSITIONSTERMIN] = new Timestamp(
						position.getT_uebersteuerterliefertermin().getTime());

			}

			if (auftragDto != null) {

				oZeile[AUSLIEFERLISTE_AUFTRAGSPOENALE] = Helper.short2Boolean(auftragDto.getBPoenale());

				oZeile[AUSLIEFERLISTE_AUFTRAGSNUMMER] = auftragDto.getCNr();
				oZeile[AUSLIEFERLISTE_PROJEKT] = auftragDto.getCBezProjektbezeichnung();
				oZeile[AUSLIEFERLISTE_LIEFERTERMIN] = auftragDto.getDLiefertermin();

				oZeile[AUSLIEFERLISTE_AUFTRAGART] = auftragDto.getAuftragartCNr();
				oZeile[AUSLIEFERLISTE_AUFTRAGSKUNDE_NAME1] = flrAuftrag.getFlrkunde().getFlrpartner()
						.getC_name1nachnamefirmazeile1();
				oZeile[AUSLIEFERLISTE_AUFTRAGSKUNDE_NAME2] = flrAuftrag.getFlrkunde().getFlrpartner()
						.getC_name2vornamefirmazeile2();
				oZeile[AUSLIEFERLISTE_AUFTRAGSKUNDE_STRASSE] = flrAuftrag.getFlrkunde().getFlrpartner().getC_strasse();

				if (flrAuftrag.getFlrkunde().getFlrpartner().getFlrlandplzort() != null) {
					oZeile[AUSLIEFERLISTE_AUFTRAGSKUNDE_ORT] = flrAuftrag.getFlrkunde().getFlrpartner()
							.getFlrlandplzort().getFlrort().getC_name();
					oZeile[AUSLIEFERLISTE_AUFTRAGSKUNDE_LKZ] = flrAuftrag.getFlrkunde().getFlrpartner()
							.getFlrlandplzort().getFlrland().getC_lkz();
					oZeile[AUSLIEFERLISTE_AUFTRAGSKUNDE_PLZ] = flrAuftrag.getFlrkunde().getFlrpartner()
							.getFlrlandplzort().getC_plz();

				}

				oZeile[AUSLIEFERLISTE_AUFTRAGSKUNDE_TELEFON] = flrAuftrag.getFlrkunde().getFlrpartner().getC_telefon();

				if (flrAuftrag.getFlrkundeansprechpartner() != null) {
					AnsprechpartnerDto anspDto = getAnsprechpartnerFac().ansprechpartnerFindByPrimaryKey(
							flrAuftrag.getFlrkundeansprechpartner().getI_id(), theClientDto);
					oZeile[AUSLIEFERLISTE_AUFTRAGSKUNDE_ANSPRECHPARTNER] = anspDto.getPartnerDto().formatAnrede();

					oZeile[AUSLIEFERLISTE_AUFTRAGSKUNDE_ANSPRECHPARTNER_TELEFONDW] = flrAuftrag
							.getFlrkundeansprechpartner().getC_telefon();

				}

				// Ablieferdatum

				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(flrAuftrag.getKunde_i_id_lieferadresse(),
						theClientDto);
				oZeile[AUSLIEFERLISTE_AUFTRAG_LIEFERADRESSE] = kundeDto.getPartnerDto().formatAnrede();

				oZeile[AUSLIEFERLISTE_AUFTRAG_LIEFERART] = getLocaleFac()
						.lieferartFindByPrimaryKey(auftragDto.getLieferartIId(), theClientDto).formatBez();
				oZeile[AUSLIEFERLISTE_AUFTRAG_SPEDITEUR] = getMandantFac()
						.spediteurFindByPrimaryKey(auftragDto.getSpediteurIId()).getCNamedesspediteurs();

				java.sql.Date dTermin = null;

				if (position != null) {
					dTermin = Helper.addiereTageZuDatum(new Date(position.getT_uebersteuerterliefertermin().getTime()),
							-kundeDto.getILieferdauer().intValue());
				} else {
					dTermin = Helper.addiereTageZuDatum(new Date(flrAuftrag.getT_liefertermin().getTime()),
							-kundeDto.getILieferdauer().intValue());
				}

				oZeile[AUSLIEFERLISTE_ABLIEFERDATUM] = dTermin;

			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return oZeile;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAuslastungsvorschauDetailliert(java.sql.Timestamp tStichtag, TheClientDto theClientDto) {

		this.useCase = UC_AUSLASTUNGSVORSCHAU_DETAILIERT;

		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT lr,(SELECT sum(la.n_menge) FROM FLRLosablieferung la WHERE la.los_i_id=lr.i_id   ) FROM FLRLosReport lr WHERE lr.t_produktionsbeginn <='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tStichtag.getTime())) + "' AND lr.status_c_nr IN ('"
				+ LocaleFac.STATUS_ANGELEGT + "','" + LocaleFac.STATUS_IN_PRODUKTION + "','"
				+ LocaleFac.STATUS_TEILERLEDIGT + "') AND lr.mandant_c_nr='" + theClientDto.getMandant() + "'";

		java.sql.Timestamp tSpaetestesDatum = new Timestamp(System.currentTimeMillis());

		ArrayList alDaten = new ArrayList();

		HashMap<Integer, String> hmArtikelgruppen = new HashMap<Integer, String>();

		Query qResult = session.createQuery(sQuery);
		List<?> results = qResult.list();
		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();

			FLRLosReport flrLosReport = (FLRLosReport) o[0];
			BigDecimal bdAbgeliefert = new BigDecimal(0);

			if (o[1] != null) {
				bdAbgeliefert = (BigDecimal) o[1];
			}

			if (bdAbgeliefert.doubleValue() < flrLosReport.getN_losgroesse().doubleValue()) {
				BigDecimal sollsatzfaktor = new BigDecimal(1);
				if (flrLosReport.getT_produktionsende().after(tSpaetestesDatum)) {
					tSpaetestesDatum = new Timestamp(flrLosReport.getT_produktionsende().getTime());
				}

				if (bdAbgeliefert.doubleValue() > 0) {
					sollsatzfaktor = bdAbgeliefert.divide(flrLosReport.getN_losgroesse(), 4,
							BigDecimal.ROUND_HALF_EVEN);
				}

				try {

					LossollarbeitsplanDto[] sollDtos = getFertigungFac()
							.lossollarbeitsplanFindByLosIId(flrLosReport.getI_id());

					if (sollDtos.length > 0) {

						for (int i = 0; i < sollDtos.length; i++) {

							LossollarbeitsplanDto saDto = sollDtos[i];

							if (Helper.short2boolean(saDto.getBFertig()) == false) {
								if (Helper.short2boolean(saDto.getBNurmaschinenzeit()) == false) {
									ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
											sollDtos[i].getArtikelIIdTaetigkeit(), theClientDto);
									// sollzeit berechnen
									double dSollzeit = sollDtos[i].getNGesamtzeit().doubleValue()
											* sollsatzfaktor.doubleValue();

									java.util.Date beginnDatum = flrLosReport.getT_produktionsbeginn();
									if (saDto.getIMaschinenversatztage() != null) {
										beginnDatum = Helper.addiereTageZuDatum(flrLosReport.getT_produktionsbeginn(),
												saDto.getIMaschinenversatztage());
									}

									Timestamp tsBginn = new Timestamp(beginnDatum.getTime());

									Object[] oZeile = new Object[AV_DETAIL_ANZAHL_SPALTEN];

									oZeile[AV_DETAIL_DAUER] = new Double(dSollzeit);

									if (tsBginn
											.before(Helper.cutTimestamp(new Timestamp(System.currentTimeMillis())))) {
										tsBginn = Helper.cutTimestamp(new Timestamp(System.currentTimeMillis()));
									}

									oZeile[AV_DETAIL_BEGINN_DATUM] = tsBginn;

									oZeile[AV_DETAIL_ARTIKELNUMMER] = artikelDto.getCNr();

									if (artikelDto.getArtikelsprDto() != null) {
										oZeile[AV_DETAIL_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
									}

									if (artikelDto.getArtgruIId() != null) {

										if (!hmArtikelgruppen.containsKey(artikelDto.getArtgruIId())) {

											ArtgruDto agDto = getArtikelFac()
													.artgruFindByPrimaryKey(artikelDto.getArtgruIId(), theClientDto);
											hmArtikelgruppen.put(artikelDto.getArtgruIId(), agDto.getBezeichnung());
										}
										oZeile[AV_DETAIL_ARTIKELGRUPPE] = hmArtikelgruppen
												.get(artikelDto.getArtgruIId());

									} else {
										oZeile[AV_DETAIL_ARTIKELGRUPPE] = "";
										hmArtikelgruppen.put(null, "");

									}
									oZeile[AV_DETAIL_LOSNUMMER] = flrLosReport.getC_nr();
									oZeile[AV_DETAIL_PROJEKT] = flrLosReport.getC_projekt();
									oZeile[AV_DETAIL_ARBEITSGANG] = saDto.getIArbeitsgangnummer();
									oZeile[AV_DETAIL_UNTERARBEITSGANG] = saDto.getIUnterarbeitsgang();

									oZeile[AV_DETAIL_KOMMENTAR] = flrLosReport.getC_kommentar();

									oZeile[AV_DETAIL_SORT_VERFUEGBARKEIT] = "1";

									if (flrLosReport.getFlrauftrag() != null) {

										oZeile[AV_DETAIL_AUFTRAG] = flrLosReport.getFlrauftrag().getC_nr();

										oZeile[AV_DETAIL_KUNDE] = flrLosReport.getFlrauftrag().getFlrkunde()
												.getFlrpartner().getC_name1nachnamefirmazeile1();

									}

									alDaten.add(oZeile);

								}
							}

						}

					}

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}
		}

		try {
			SollverfuegbarkeitDto[] dtos = getZeiterfassungFac().getVerfuegbareSollzeit(
					Helper.cutTimestamp(new Timestamp(System.currentTimeMillis())), tSpaetestesDatum, theClientDto);
			for (int i = 0; i < dtos.length; i++) {
				if (dtos[i].isBMannarbeitszeit()) {

					Object[] oZeile = new Object[AV_DETAIL_ANZAHL_SPALTEN];

					if (dtos[i].getIGruppeid() != null) {

						if (!hmArtikelgruppen.containsKey(dtos[i].getIGruppeid())) {

							ArtgruDto agDto = getArtikelFac().artgruFindByPrimaryKey(dtos[i].getIGruppeid(),
									theClientDto);
							hmArtikelgruppen.put(dtos[i].getIGruppeid(), agDto.getBezeichnung());
						}
						oZeile[AV_DETAIL_ARTIKELGRUPPE] = hmArtikelgruppen.get(dtos[i].getIGruppeid());

					} else {
						oZeile[AV_DETAIL_ARTIKELGRUPPE] = "";
					}

					oZeile[AV_DETAIL_BEGINN_DATUM] = dtos[i].getTDatum();
					oZeile[AV_DETAIL_VERFUEGBARKEIT] = dtos[i].getNSollstunden();

					oZeile[AV_DETAIL_SORT_VERFUEGBARKEIT] = "0";

					alDaten.add(oZeile);

				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		// Fehlende Tage auffuellen

		Timestamp tHeute = Helper.cutTimestamp(new Timestamp(System.currentTimeMillis()));
		while (tHeute.before(tSpaetestesDatum)) {

			Iterator it = hmArtikelgruppen.keySet().iterator();

			while (it.hasNext()) {

				Integer artikelgruppeIId = (Integer) it.next();
				String artikelgruppe = hmArtikelgruppen.get(artikelgruppeIId);

				boolean gefunden = false;
				for (int i = 0; i < alDaten.size(); i++) {
					Object[] oZeile = (Object[]) alDaten.get(i);

					Timestamp tBeginnDatum = (Timestamp) oZeile[AV_DETAIL_BEGINN_DATUM];

					String artikelgruppeVorhanden = (String) oZeile[AV_DETAIL_ARTIKELGRUPPE];

					if (artikelgruppeVorhanden == null) {
						artikelgruppeVorhanden = "";
					}

					if (tBeginnDatum.equals(tHeute) && artikelgruppeVorhanden.equals(artikelgruppe)) {
						gefunden = true;
						break;
					}

				}
				if (gefunden == false) {
					Object[] oZeile = new Object[AV_DETAIL_ANZAHL_SPALTEN];

					oZeile[AV_DETAIL_BEGINN_DATUM] = tHeute;
					oZeile[AV_DETAIL_SORT_VERFUEGBARKEIT] = "0";
					oZeile[AV_DETAIL_ARTIKELGRUPPE] = artikelgruppe;
					alDaten.add(oZeile);
				}
			}
			tHeute = Helper.cutTimestamp(Helper.addiereTageZuTimestamp(tHeute, 1));

		}

		// Nun zuerst nach Artikelgruppe, dann nach Datum und dann nach
		// SortVerfuegbarkeit sortieren
		for (int k = alDaten.size() - 1; k > 0; --k) {
			for (int j = 0; j < k; ++j) {
				Object[] a1 = (Object[]) alDaten.get(j);
				Object[] a2 = (Object[]) alDaten.get(j + 1);

				String s1 = (String) a1[AV_DETAIL_ARTIKELGRUPPE];
				String s2 = (String) a2[AV_DETAIL_ARTIKELGRUPPE];

				if (s1 == null) {
					s1 = "";
				}
				if (s2 == null) {
					s2 = "";
				}

				if (s1.compareTo(s2) > 0) {
					alDaten.set(j, a2);
					alDaten.set(j + 1, a1);
				} else if (s1.compareTo(s2) == 0) {
					Timestamp t1 = (Timestamp) a1[AV_DETAIL_BEGINN_DATUM];
					Timestamp t2 = (Timestamp) a2[AV_DETAIL_BEGINN_DATUM];

					if (t1.after(t2)) {
						alDaten.set(j, a2);
						alDaten.set(j + 1, a1);
					} else if (t1.compareTo(t2) == 0) {
						String v1 = (String) a1[AV_DETAIL_SORT_VERFUEGBARKEIT];
						String v2 = (String) a2[AV_DETAIL_SORT_VERFUEGBARKEIT];

						if (v1.compareTo(v2) > 0) {
							alDaten.set(j, a2);
							alDaten.set(j + 1, a1);
						}
					}
				}

			}

		}

		Map<String, Object> mapParameter = new TreeMap<String, Object>();
		mapParameter.put("P_STICHTAG", new Timestamp(tStichtag.getTime() - 3600000 * 24));

		data = new Object[alDaten.size()][AV_DETAIL_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL,
				FertigungReportFac.REPORT_AUSLASTUNGSVORSCHAU_DETAILIERT, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAuslastungsvorschau(java.sql.Timestamp tStichtag, boolean bSortiertNachArtikelgruppe,
			TheClientDto theClientDto) {
		this.useCase = UC_AUSLASTUNGSVORSCHAU;

		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT lr,(SELECT sum(la.n_menge) FROM FLRLosablieferung la WHERE la.los_i_id=lr.i_id   ) FROM FLRLosReport lr WHERE lr.t_produktionsbeginn <='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tStichtag.getTime())) + "' AND lr.status_c_nr IN ('"
				+ LocaleFac.STATUS_ANGELEGT + "','" + LocaleFac.STATUS_IN_PRODUKTION + "','"
				+ LocaleFac.STATUS_TEILERLEDIGT + "') AND lr.mandant_c_nr='" + theClientDto.getMandant() + "'";

		HashMap hmGesamt = new HashMap();
		HashMap hmArtikelgruppen = new HashMap();
		java.sql.Timestamp tSpaetestesDatum = new Timestamp(System.currentTimeMillis());

		Query qResult = session.createQuery(sQuery);
		List<?> results = qResult.list();
		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();

			FLRLosReport flrLosReport = (FLRLosReport) o[0];
			BigDecimal bdAbgeliefert = new BigDecimal(0);

			if (o[1] != null) {
				bdAbgeliefert = (BigDecimal) o[1];
			}

			if (bdAbgeliefert.doubleValue() < flrLosReport.getN_losgroesse().doubleValue()) {
				BigDecimal sollsatzfaktor = new BigDecimal(1);

				if (flrLosReport.getT_produktionsende().after(tSpaetestesDatum)) {
					tSpaetestesDatum = new Timestamp(flrLosReport.getT_produktionsende().getTime());
				}

				if (bdAbgeliefert.doubleValue() > 0) {
					sollsatzfaktor = bdAbgeliefert.divide(flrLosReport.getN_losgroesse(), 4,
							BigDecimal.ROUND_HALF_EVEN);
				}

				try {

					LossollarbeitsplanDto[] sollDtos = getFertigungFac()
							.lossollarbeitsplanFindByLosIId(flrLosReport.getI_id());

					if (sollDtos.length > 0) {

						for (int i = 0; i < sollDtos.length; i++) {

							ArtikelDto artikelDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(sollDtos[i].getArtikelIIdTaetigkeit(), theClientDto);
							// sollzeit berechnen
							double dSollzeit = sollDtos[i].getNGesamtzeit().doubleValue();

							hmGesamt = hmAddToAuslastungsvorschau(hmGesamt,
									new java.sql.Timestamp(flrLosReport.getT_produktionsbeginn().getTime()),
									artikelDto.getArtgruIId(), dSollzeit * sollsatzfaktor.doubleValue(), 0, 0);

							hmGesamt = hmAddToAuslastungsvorschau(hmGesamt,
									new java.sql.Timestamp(flrLosReport.getT_produktionsende().getTime()),
									artikelDto.getArtgruIId(), 0, dSollzeit * sollsatzfaktor.doubleValue(), 0);

							if (!hmArtikelgruppen.containsKey(artikelDto.getArtgruIId())) {
								hmArtikelgruppen.put(artikelDto.getArtgruIId(), null);
							}

						}

					}

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}
		}
		try {
			SollverfuegbarkeitDto[] dtos = getZeiterfassungFac().getVerfuegbareSollzeit(
					Helper.cutTimestamp(new Timestamp(System.currentTimeMillis())), tSpaetestesDatum, theClientDto);
			for (int i = 0; i < dtos.length; i++) {
				if (dtos[i].isBMannarbeitszeit()) {
					hmGesamt = hmAddToAuslastungsvorschau(hmGesamt, dtos[i].getTDatum(), dtos[i].getIGruppeid(), 0, 0,
							dtos[i].getNSollstunden().doubleValue());
				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		// Fehlende Tage mit 0 nachtragen
		int iAnzahlTage = Helper.getDifferenzInTagen(new Timestamp(System.currentTimeMillis()), tSpaetestesDatum);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		for (int i = 0; i < iAnzahlTage; i++) {

			Iterator itGru = hmArtikelgruppen.keySet().iterator();
			while (itGru.hasNext()) {
				hmGesamt = hmAddToAuslastungsvorschau(hmGesamt, new Timestamp(c.getTimeInMillis()),
						(Integer) itGru.next(), 0, 0, 0);
			}

			c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);
		}

		Map<String, Object> mapParameter = new TreeMap<String, Object>();
		mapParameter.put("P_SORTIERTNACHARTIKELGRUPPE", new Boolean(bSortiertNachArtikelgruppe));
		mapParameter.put("P_STICHTAG", new Timestamp(tStichtag.getTime() - 3600000 * 24));

		ArrayList alDaten = new ArrayList();
		Iterator itGesamt = hmGesamt.keySet().iterator();
		while (itGesamt.hasNext()) {
			Timestamp t = (Timestamp) itGesamt.next();

			HashMap hmSub = (HashMap) hmGesamt.get(t);

			Iterator itSub = hmSub.keySet().iterator();

			while (itSub.hasNext()) {
				Integer artikelgruppeIId = (Integer) itSub.next();

				double[] d = (double[]) hmSub.get(artikelgruppeIId);

				Object[] zeile = new Object[5];

				if (artikelgruppeIId == null) {
					zeile[ZE_ARTIKELGRUPPE] = "";
				} else {
					zeile[ZE_ARTIKELGRUPPE] = getArtikelFac().artgruFindByPrimaryKey(artikelgruppeIId, theClientDto)
							.getCNr();
				}

				zeile[AV_DATUM] = t;
				zeile[AV_SOLLZEITBEGINN] = new BigDecimal(d[0]);
				zeile[AV_SOLLZEITENDE] = new BigDecimal(d[1]);
				zeile[AV_PERSONALVERFUEGBARKEIT] = new BigDecimal(d[2]);
				alDaten.add(zeile);

			}

		}

		// sortieren

		for (int k = alDaten.size() - 1; k > 0; --k) {
			for (int j = 0; j < k; ++j) {
				Object[] a1 = (Object[]) alDaten.get(j);
				Object[] a2 = (Object[]) alDaten.get(j + 1);

				if (bSortiertNachArtikelgruppe == false) {
					Timestamp t1 = (Timestamp) a1[ZE_DATUM];
					Timestamp t2 = (Timestamp) a2[ZE_DATUM];

					if (t1.after(t2)) {
						alDaten.set(j, a2);
						alDaten.set(j + 1, a1);
					}
				} else {
					String s1 = (String) a1[ZE_ARTIKELGRUPPE];
					String s2 = (String) a2[ZE_ARTIKELGRUPPE];

					if (s1.compareTo(s2) > 0) {
						alDaten.set(j, a2);
						alDaten.set(j + 1, a1);
					} else if (s1.compareTo(s2) == 0) {
						Timestamp t1 = (Timestamp) a1[ZE_DATUM];
						Timestamp t2 = (Timestamp) a2[ZE_DATUM];

						if (t1.after(t2)) {
							alDaten.set(j, a2);
							alDaten.set(j + 1, a1);
						}
					}
				}

			}

		}

		data = new Object[alDaten.size()][4];
		data = (Object[][]) alDaten.toArray(data);
		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_AUSLASTUNGSVORSCHAU,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printZeitentwicklung(java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			boolean bSortiertNachArtikelgruppe, TheClientDto theClientDto) {
		this.useCase = UC_ZEITENTWICKLUNG;

		Map<String, Object> mapParameter = new TreeMap<String, Object>();
		mapParameter.put("P_VON", Helper.cutTimestamp(tVon));
		mapParameter.put("P_BIS", new Timestamp(tBis.getTime() - 3600000 * 24));
		mapParameter.put("P_SORTIERTNACHARTIKELGRUPPE", new Boolean(bSortiertNachArtikelgruppe));

		HashMap mhLosIIds = new HashMap();

		java.sql.Timestamp tFruehestesDatum = new Timestamp(System.currentTimeMillis());

		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT lr FROM FLRLosReport lr WHERE lr.t_manuellerledigt <='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tBis.getTime())) + "' AND lr.t_manuellerledigt >='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tVon.getTime())) + "' AND lr.mandant_c_nr='"
				+ theClientDto.getMandant() + "'";

		Query qResult = session.createQuery(sQuery);
		List<?> results = qResult.list();
		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			FLRLosReport flrLosReport = (FLRLosReport) resultListIterator.next();
			mhLosIIds.put(flrLosReport.getI_id(), null);
		}

		Session session2 = FLRSessionFactory.getFactory().openSession();
		sQuery = "SELECT la.flrlos.i_id,sum(la.n_menge) FROM FLRLosablieferung la WHERE la.t_aendern <='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tBis.getTime())) + "' AND la.t_aendern >='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tVon.getTime())) + "' AND la.flrlos.mandant_c_nr='"
				+ theClientDto.getMandant() + "' GROUP BY la.los_i_id";

		qResult = session2.createQuery(sQuery);
		results = qResult.list();
		resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {

			Object[] o = (Object[]) resultListIterator.next();
			Integer losIId = (Integer) o[0];

			BigDecimal bdAbgeliefert = (BigDecimal) o[1];

			LosDto losDto = null;
			try {
				losDto = getFertigungFac().losFindByPrimaryKey(losIId);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			if (bdAbgeliefert.doubleValue() >= losDto.getNLosgroesse().doubleValue()) {

				if (!mhLosIIds.containsKey(losIId)) {
					mhLosIIds.put(losIId, null);
				}
			}

		}
		session.close();
		session2.close();

		HashMap hmGesamt = new HashMap();

		HashMap hmArtikelgruppen = new HashMap();

		Iterator it = mhLosIIds.keySet().iterator();
		while (it.hasNext()) {
			Integer losIId = (Integer) it.next();
			try {

				LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);

				AuftragzeitenDto[] dtos = getZeiterfassungFac().getAllZeitenEinesBeleges(LocaleFac.BELEGART_LOS, losIId,
						null, null, null, null, ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ARTIKEL, theClientDto);

				if (dtos.length > 0) {

					for (int i = 0; i < dtos.length; i++) {

						if (dtos[i] != null && dtos[i].getDdDauer() != null) {
							if (dtos[dtos.length - 1].getTsBeginn().before(tFruehestesDatum)) {
								tFruehestesDatum = dtos[0].getTsBeginn();
							}
							hmGesamt = hmAddToZeitentwicklung(hmGesamt,
									Helper.cutTimestamp(dtos[dtos.length - 1].getTsBeginn()), tVon,
									dtos[i].getArtikelgruppeIId(), 0, dtos[i].getDdDauer());
						}
					}

					LossollarbeitsplanDto[] sollDtos = getFertigungFac().lossollarbeitsplanFindByLosIId(losIId);

					if (sollDtos.length > 0) {

						for (int i = 0; i < sollDtos.length; i++) {

							ArtikelDto artikelDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(sollDtos[i].getArtikelIIdTaetigkeit(), theClientDto);

							double dSollzeit = sollDtos[i].getNGesamtzeit().doubleValue();
							hmGesamt = hmAddToZeitentwicklung(hmGesamt, Helper.cutTimestamp(dtos[0].getTsBeginn()),
									tVon, artikelDto.getArtgruIId(), dSollzeit, 0);

							if (!hmArtikelgruppen.containsKey(artikelDto.getArtgruIId())) {
								hmArtikelgruppen.put(artikelDto.getArtgruIId(), null);
							}

						}
					}

				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}

		// Fehlende Tage mit 0 nachtragen
		int iAnzahlTage = Helper.getDifferenzInTagen(tFruehestesDatum, tBis);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tFruehestesDatum.getTime());
		for (int i = 0; i < iAnzahlTage; i++) {
			Iterator itGru = hmArtikelgruppen.keySet().iterator();
			while (itGru.hasNext()) {
				hmGesamt = hmAddToZeitentwicklung(hmGesamt, new Timestamp(c.getTimeInMillis()), tVon,
						(Integer) itGru.next(), 0, 0);
			}
			c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);
		}

		ArrayList alDaten = new ArrayList();

		Iterator itGesamt = hmGesamt.keySet().iterator();
		while (itGesamt.hasNext()) {
			Timestamp t = (Timestamp) itGesamt.next();

			HashMap hmSub = (HashMap) hmGesamt.get(t);

			Iterator itSub = hmSub.keySet().iterator();

			while (itSub.hasNext()) {
				Integer artikelgruppeIId = (Integer) itSub.next();

				double[] d = (double[]) hmSub.get(artikelgruppeIId);

				Object[] zeile = new Object[4];

				if (artikelgruppeIId == null) {
					zeile[ZE_ARTIKELGRUPPE] = "";
				} else {
					zeile[ZE_ARTIKELGRUPPE] = getArtikelFac().artgruFindByPrimaryKey(artikelgruppeIId, theClientDto)
							.getCNr();
				}

				zeile[ZE_DATUM] = t;
				zeile[ZE_SOLLZEIT] = new BigDecimal(d[0]);
				zeile[ZE_ISTZEIT] = new BigDecimal(d[1]);
				alDaten.add(zeile);

			}

		}

		// sortieren

		for (int k = alDaten.size() - 1; k > 0; --k) {
			for (int j = 0; j < k; ++j) {
				Object[] a1 = (Object[]) alDaten.get(j);
				Object[] a2 = (Object[]) alDaten.get(j + 1);

				if (bSortiertNachArtikelgruppe == false) {
					Timestamp t1 = (Timestamp) a1[ZE_DATUM];
					Timestamp t2 = (Timestamp) a2[ZE_DATUM];

					if (t1.after(t2)) {
						alDaten.set(j, a2);
						alDaten.set(j + 1, a1);
					}
				} else {
					String s1 = (String) a1[ZE_ARTIKELGRUPPE];
					String s2 = (String) a2[ZE_ARTIKELGRUPPE];

					if (s1.compareTo(s2) > 0) {
						alDaten.set(j, a2);
						alDaten.set(j + 1, a1);
					} else if (s1.compareTo(s2) == 0) {
						Timestamp t1 = (Timestamp) a1[ZE_DATUM];
						Timestamp t2 = (Timestamp) a2[ZE_DATUM];

						if (t1.after(t2)) {
							alDaten.set(j, a2);
							alDaten.set(j + 1, a1);
						}
					}
				}

			}

		}

		data = new Object[alDaten.size()][4];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_ZEITENTWICKLUNG,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printMonatsauswertung(java.sql.Timestamp tVon, java.sql.Timestamp tBis, boolean bVerdichtet,
			TheClientDto theClientDto) {
		this.useCase = UC_MONATSAUSWERTUNG;
		this.index = -1;

		Map<String, Object> mapParameter = new TreeMap<String, Object>();

		// Zuerst Alle Lose Ohne Kunden
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();
		session.enableFilter("filterMandant").setParameter("paramMandant", theClientDto.getMandant());
		session.enableFilter("filterLocale").setParameter("paramLocale", Helper.locale2String(theClientDto.getLocUi()));
		String query = "SELECT l, (SELECT SUM(s.n_gesamtzeit) FROM FLRLossollarbeitsplan s  WHERE s.los_i_id=l.i_id)  FROM FLRLosReport l LEFT OUTER JOIN l.flrauftrag a LEFT OUTER JOIN l.flrauftrag.flrkunde.flrpartner p WHERE l.mandant_c_nr='"
				+ theClientDto.getMandant() + "' " + " AND l.status_c_nr ='" + LocaleFac.STATUS_ERLEDIGT
				+ "' AND (l.t_manuellerledigt>='" + Helper.formatDateWithSlashes(new java.sql.Date(tVon.getTime()))
				+ "'   OR l.t_erledigt>='" + Helper.formatDateWithSlashes(new java.sql.Date(tVon.getTime())) + "')"
				+ " AND (l.t_manuellerledigt<='" + Helper.formatDateWithSlashes(new java.sql.Date(tBis.getTime()))
				+ "'   OR l.t_erledigt<='" + Helper.formatDateWithSlashes(new java.sql.Date(tBis.getTime()))
				+ "') ORDER BY l.flrfertigungsgruppe.c_bez,p.c_name1nachnamefirmazeile1, a.c_nr, l.c_nr";

		mapParameter.put("P_VERDICHTET", new Boolean(bVerdichtet));
		mapParameter.put("P_VON", Helper.cutTimestamp(tVon));
		mapParameter.put("P_BIS", new Timestamp(tBis.getTime() - 3600000 * 24));

		Query qResult = session.createQuery(query);
		// qResult.setMaxResults(50);

		List<?> results = qResult.list();

		ArrayList<LosMonatsauswertungDto> alDaten = new ArrayList<LosMonatsauswertungDto>();
		int k = 0;

		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();

			FLRLosReport flrLosReport = (FLRLosReport) o[0];

			k++;
			LosMonatsauswertungDto losMonatsauswertungDto = new LosMonatsauswertungDto();

			losMonatsauswertungDto.setSFertigungsgruppe(flrLosReport.getFlrfertigungsgruppe().getC_bez());

			losMonatsauswertungDto.setSLosnummer(flrLosReport.getC_nr());
			// Sollzeit/Istzeit
			BigDecimal sollzeit = new BigDecimal(0.0000);
			if (o[1] != null) {
				sollzeit = (BigDecimal) o[1];
			}
			losMonatsauswertungDto.setNSollzeit(sollzeit);

			Double zeiten = new Double(0);
			try {
				zeiten = getZeiterfassungFac().getSummeZeitenEinesBeleges(LocaleFac.BELEGART_LOS,
						flrLosReport.getI_id(), null, null, null, null, theClientDto);
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

			BigDecimal istzeit = new BigDecimal(zeiten.doubleValue());

			losMonatsauswertungDto.setNIstZeit(istzeit);
			losMonatsauswertungDto.setNLosgroesse(flrLosReport.getN_losgroesse());

			if (flrLosReport.getF_bewertung() != null) {
				BigDecimal abw = sollzeit.subtract(istzeit)
						.multiply(new BigDecimal(flrLosReport.getF_bewertung().doubleValue()));
				abw = abw.divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_EVEN);
				losMonatsauswertungDto.setNAbweichung(abw);
				losMonatsauswertungDto.setFBewertung(flrLosReport.getF_bewertung());
			} else {
				losMonatsauswertungDto.setNAbweichung(sollzeit.subtract(istzeit));
			}

			if (flrLosReport.getFlrstueckliste() == null) {
				if (flrLosReport.getC_projekt() != null) {
					losMonatsauswertungDto.setArtikelnummer(flrLosReport.getC_projekt());
				} else {
					losMonatsauswertungDto.setArtikelnummer("");
				}
			} else {
				losMonatsauswertungDto.setArtikelnummer(flrLosReport.getFlrstueckliste().getFlrartikel().getC_nr());

				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
						flrLosReport.getFlrstueckliste().getFlrartikel().getI_id(), theClientDto);

				losMonatsauswertungDto.setArtikelbezeichnung(artikelDto.formatBezeichnung());

			}

			if (flrLosReport.getFlrauftrag() == null) {

				if (flrLosReport.getFlrkunde() != null) {
					String kunde = flrLosReport.getFlrkunde().getFlrpartner().getC_name1nachnamefirmazeile1();
					losMonatsauswertungDto.setSKunde(kunde);
				} else {
					losMonatsauswertungDto.setSKunde("");
				}

			} else {
				String kunde = flrLosReport.getFlrauftrag().getFlrkunde().getFlrpartner()
						.getC_name1nachnamefirmazeile1();
				losMonatsauswertungDto.setSKunde(kunde);
			}

			alDaten.add(losMonatsauswertungDto);
		}

		session.close();

		data = new Object[alDaten.size()][MONATSAUSWERTUNG_ANZAHL_FELDER];

		int i = 0;

		Iterator<LosMonatsauswertungDto> it = alDaten.iterator();
		while (it.hasNext()) {
			LosMonatsauswertungDto losMonatsauswertungDto = (LosMonatsauswertungDto) it.next();

			data[i][MONATSAUSWERTUNG_ARTIKELNUMMER] = losMonatsauswertungDto.getArtikelnummer();
			data[i][MONATSAUSWERTUNG_BEZEICHNUNG] = losMonatsauswertungDto.getArtikelbezeichnung();
			data[i][MONATSAUSWERTUNG_KUNDE] = losMonatsauswertungDto.getSKunde();
			data[i][MONATSAUSWERTUNG_ISTZEIT] = losMonatsauswertungDto.getNIstZeit();
			data[i][MONATSAUSWERTUNG_SOLLZEIT] = losMonatsauswertungDto.getNSollzeit();
			data[i][MONATSAUSWERTUNG_ABWEICHUNG] = losMonatsauswertungDto.getNAbweichung();
			data[i][MONATSAUSWERTUNG_LOSGROESSE] = losMonatsauswertungDto.getNLosgroesse();
			data[i][MONATSAUSWERTUNG_FERTIGUNGSGRUPPE] = losMonatsauswertungDto.getSFertigungsgruppe();
			data[i][MONATSAUSWERTUNG_LOSNUMMER] = losMonatsauswertungDto.getSLosnummer();
			data[i][MONATSAUSWERTUNG_BEWERTUNG] = losMonatsauswertungDto.getFBewertung();
			i++;
		}

		// Nochmals sortieren

		for (int m = data.length - 1; m > 0; --m) {
			for (int j = 0; j < m; ++j) {
				Object[] a1 = (Object[]) data[j];
				Object[] a2 = (Object[]) data[j + 1];

				if (((String) a1[MONATSAUSWERTUNG_FERTIGUNGSGRUPPE])
						.compareTo((String) a2[MONATSAUSWERTUNG_FERTIGUNGSGRUPPE]) > 0) {
					data[j] = a2;
					data[j + 1] = a1;
				} else if (((String) a1[MONATSAUSWERTUNG_FERTIGUNGSGRUPPE])
						.compareTo((String) a2[MONATSAUSWERTUNG_FERTIGUNGSGRUPPE]) == 0) {
					String k1 = (String) a1[MONATSAUSWERTUNG_KUNDE];
					String k2 = (String) a2[MONATSAUSWERTUNG_KUNDE];

					if (k1.compareTo(k2) > 0) {
						data[j] = a2;
						data[j + 1] = a1;
					} else if (((String) a1[MONATSAUSWERTUNG_KUNDE])
							.compareTo((String) a2[MONATSAUSWERTUNG_KUNDE]) == 0) {
						String b1 = (String) a1[MONATSAUSWERTUNG_ARTIKELNUMMER];
						String b2 = (String) a2[MONATSAUSWERTUNG_ARTIKELNUMMER];

						if (b1.compareTo(b2) > 0) {
							data[j] = a2;
							data[j + 1] = a1;
						} else if (((String) a1[MONATSAUSWERTUNG_ARTIKELNUMMER])
								.compareTo((String) a2[MONATSAUSWERTUNG_ARTIKELNUMMER]) == 0) {
							String l1 = (String) a1[MONATSAUSWERTUNG_LOSNUMMER];
							String l2 = (String) a2[MONATSAUSWERTUNG_LOSNUMMER];

							if (l1.compareTo(l2) > 0) {
								data[j] = a2;
								data[j + 1] = a1;
							}
						}
					}
				}
			}
		}

		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_MONATSAUSWERTUNG,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	public JasperPrintLP printLoszeiten(Integer losIId, int iSortierung, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, TheClientDto theClientDto) {
		Map<String, Object> mapParameter = new TreeMap<String, Object>();
		try {
			this.useCase = UC_LOSZEITEN;
			this.index = -1;
			LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);
			AuftragzeitenDto[] personalZeitenDtos = null;
			AuftragzeitenDto[] maschinenZeitenDtos = null;

			mapParameter.put("P_VON", tVon);
			mapParameter.put("P_BIS", tBis);

			if (iSortierung == LOSZEITEN_OPTION_SORTIERUNG_PERSON) {
				mapParameter.put("P_SORTIERUNG",
						getTextRespectUISpr("lp.person", theClientDto.getMandant(), theClientDto.getLocUi()));
			} else if (iSortierung == LOSZEITEN_OPTION_SORTIERUNG_ARTIKEL) {
				mapParameter.put("P_SORTIERUNG",
						getTextRespectUISpr("lp.artikel", theClientDto.getMandant(), theClientDto.getLocUi()));
			} else if (iSortierung == LOSZEITEN_OPTION_SORTIERUNG_AG_UAG) {
				mapParameter.put("P_SORTIERUNG",
						getTextRespectUISpr("fert.ag", theClientDto.getMandant(), theClientDto.getLocUi()) + "+"
								+ getTextRespectUISpr("fert.uag", theClientDto.getMandant(), theClientDto.getLocUi()));
			}

			if (tBis != null) {

				tBis = Helper.cutTimestamp(Helper.addiereTageZuTimestamp(tBis, 1));
			}

			if (iSortierung == LOSZEITEN_OPTION_SORTIERUNG_PERSON) {
				personalZeitenDtos = getZeiterfassungFac().getAllZeitenEinesBeleges(LocaleFac.BELEGART_LOS, losIId,
						null, null, tVon, tBis, ZeiterfassungFac.SORTIERUNG_ZEITDATEN_PERSONAL, theClientDto);
			} else {
				personalZeitenDtos = getZeiterfassungFac().getAllZeitenEinesBeleges(LocaleFac.BELEGART_LOS, losIId,
						null, null, tVon, tBis, ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ARTIKEL, theClientDto);
			}

			if (iSortierung == LOSZEITEN_OPTION_SORTIERUNG_PERSON) {
				maschinenZeitenDtos = getZeiterfassungFac().getAllMaschinenzeitenEinesBeleges(losIId, null, tVon, tBis,
						theClientDto);
			} else {
				maschinenZeitenDtos = getZeiterfassungFac().getAllMaschinenzeitenEinesBeleges(losIId, null, tVon, tBis,
						theClientDto);
			}

			AuftragzeitenDto[] aLoszeitenDtos = new AuftragzeitenDto[personalZeitenDtos.length
					+ maschinenZeitenDtos.length];

			int row = 0;

			for (int i = 0; i < personalZeitenDtos.length; i++) {
				aLoszeitenDtos[row] = personalZeitenDtos[i];
				row++;
			}
			for (int i = 0; i < maschinenZeitenDtos.length; i++) {
				aLoszeitenDtos[row] = maschinenZeitenDtos[i];
				row++;
			}

			data = new Object[aLoszeitenDtos.length][LOSZEITEN_SPALTENANZAHL];

			for (int i = 0; i < aLoszeitenDtos.length; i++) {
				data[i][LOSZEITEN_PERSON_MASCHINE] = aLoszeitenDtos[i].getSPersonalMaschinenname();
				data[i][LOSZEITEN_PERSONALNUMMER_MASCHINENINVENARNUMMER] = aLoszeitenDtos[i].getSPersonalnummer();
				data[i][LOSZEITEN_ARTIKELNUMMER] = aLoszeitenDtos[i].getSArtikelcnr();
				data[i][LOSZEITEN_BEMERKUNG] = aLoszeitenDtos[i].getSZeitbuchungtext();
				data[i][LOSZEITEN_BEMERKUNG_LANG] = aLoszeitenDtos[i].getSKommentar();
				data[i][LOSZEITEN_BEMERKUNG_LANG] = aLoszeitenDtos[i].getSKommentar();
				data[i][LOSZEITEN_MASCHINENGRUPPE] = aLoszeitenDtos[i].getMaschinengruppe();
				data[i][LOSZEITEN_ARBEITSGANG] = aLoszeitenDtos[i].getiArbeitsgang();
				data[i][LOSZEITEN_UNTERARBEITSGANG] = aLoszeitenDtos[i].getiUnterarbeitsgang();
				data[i][LOSZEITEN_BEZEICHNUNG] = aLoszeitenDtos[i].getSArtikelbezeichnung();
				data[i][LOSZEITEN_ZUSATZBEZEICHNUNG] = aLoszeitenDtos[i].getSArtikelzusatzbezeichnung();
				data[i][LOSZEITEN_DAUER] = aLoszeitenDtos[i].getDdDauer();
				data[i][LOSZEITEN_BEGINN] = aLoszeitenDtos[i].getTsBeginn();
				data[i][LOSZEITEN_ENDE] = aLoszeitenDtos[i].getTsEnde();
				data[i][LOSZEITEN_ZEITDATEN_I_ID] = aLoszeitenDtos[i].getZeitdatenIIdBelegbuchung();

			}

			// SP1296
			if (iSortierung == LOSZEITEN_OPTION_SORTIERUNG_AG_UAG) {
				// Umsortieren
				for (int k = data.length - 1; k > 0; --k) {
					for (int j = 0; j < k; ++j) {
						Object[] a1 = (Object[]) data[j];
						Object[] a2 = (Object[]) data[j + 1];

						Integer iArbeitsgang = (Integer) a1[LOSZEITEN_ARBEITSGANG];
						Integer iUnterArbeitsgang = (Integer) a1[LOSZEITEN_UNTERARBEITSGANG];
						if (iArbeitsgang == null) {
							iArbeitsgang = 0;
						}
						if (iUnterArbeitsgang == null) {
							iUnterArbeitsgang = 0;
						}
						Integer iArbeitsgang2 = (Integer) a2[LOSZEITEN_ARBEITSGANG];
						Integer iUnterArbeitsgang2 = (Integer) a2[LOSZEITEN_UNTERARBEITSGANG];
						if (iArbeitsgang2 == null) {
							iArbeitsgang2 = 0;
						}
						if (iUnterArbeitsgang2 == null) {
							iUnterArbeitsgang2 = 0;
						}

						String s1 = Helper.fitString2LengthAlignRight(iArbeitsgang + "", 5, '0')
								+ Helper.fitString2LengthAlignRight(iUnterArbeitsgang + "", 5, '0');
						String s2 = Helper.fitString2LengthAlignRight(iArbeitsgang2 + "", 5, '0')
								+ Helper.fitString2LengthAlignRight(iUnterArbeitsgang2 + "", 5, '0');

						if (s1.compareTo(s2) > 0) {
							data[j] = a2;
							data[j + 1] = a1;
						}
					}
				}
			}

			mapParameter.put("P_ANGELEGT", new java.util.Date(losDto.getTAnlegen().getTime()));
			String sAuftragsnummer;
			String sInternerKommentar = null;
			String sKunde;
			String sKundeLieferadresse;
			Timestamp dLiefertermin;
			String sLieferart;
			String sAbteilung = null;
			String sSpediteur = null;
			Boolean bPoenale = Boolean.FALSE;
			Boolean bRoHs = Boolean.FALSE;
			if (losDto.getAuftragIId() != null) {
				// Auftrag holen
				AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(losDto.getAuftragIId());
				sAuftragsnummer = auftragDto.getCNr();
				sLieferart = getLocaleFac().lieferartFindByIIdLocaleOhneExc(auftragDto.getLieferartIId(),
						theClientDto.getLocUi(), theClientDto);

				if (auftragDto.getSpediteurIId() != null) {
					sSpediteur = getMandantFac().spediteurFindByPrimaryKey(auftragDto.getSpediteurIId())
							.getCNamedesspediteurs();
				}

				bPoenale = Helper.short2Boolean(auftragDto.getBPoenale());
				bRoHs = Helper.short2Boolean(auftragDto.getBRoHs());
				// Internen Kommentar aus dem Auftrag, abhaengig von einem
				// Mandantenparameter
				ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_KOMMENTAR_AM_FERTIGUNGSBEGLEITSCHEIN);
				short iValue = Short.parseShort(parameter.getCWert());
				boolean bDruckeKommentar = Helper.short2boolean(iValue);
				if (bDruckeKommentar) {
					sInternerKommentar = auftragDto.getXInternerkommentar();
				}
				// Kunde holen
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(),
						theClientDto);

				sKunde = kundeDto.getPartnerDto().getCName1nachnamefirmazeile1();

				KundeDto kundeDtoLieferadresse = getKundeFac()
						.kundeFindByPrimaryKey(auftragDto.getKundeIIdLieferadresse(), theClientDto);
				sKundeLieferadresse = kundeDtoLieferadresse.getPartnerDto().getCName1nachnamefirmazeile1();

				sAbteilung = kundeDto.getPartnerDto().getCName3vorname2abteilung();

				if (losDto.getAuftragpositionIId() != null) {
					AuftragpositionDto aufposDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(losDto.getAuftragpositionIId());
					if (aufposDto.getTUebersteuerbarerLiefertermin() != null) {
						dLiefertermin = aufposDto.getTUebersteuerbarerLiefertermin();
					} else {
						dLiefertermin = auftragDto.getDLiefertermin();
					}
				} else {
					dLiefertermin = auftragDto.getDLiefertermin();
				}
			} else {
				sAuftragsnummer = "";

				if (losDto.getKundeIId() != null) {
					KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(losDto.getKundeIId(), theClientDto);

					sKunde = kundeDto.getPartnerDto().getCName1nachnamefirmazeile1();
				} else {
					sKunde = "";
				}

				sKundeLieferadresse = "";
				dLiefertermin = null;
				sLieferart = "";
				sSpediteur = "";
			}

			mapParameter.put("P_AUFTRAGNUMMER", sAuftragsnummer);
			mapParameter.put("P_AUFTRAG_INTERNERKOMMENTAR", sInternerKommentar);
			mapParameter.put("P_AUFTRAG_KUNDE_ABTEILUNG", sAbteilung);
			mapParameter.put("P_LIEFERART", sLieferart);
			mapParameter.put("P_SPEDITEUR", sSpediteur);
			mapParameter.put("P_POENALE", bPoenale);
			mapParameter.put("P_ROHS", bRoHs);
			KostenstelleDto kstDto = getSystemFac().kostenstelleFindByPrimaryKey(losDto.getKostenstelleIId());
			mapParameter.put("P_KOSTENSTELLENUMMER", kstDto.getCNr());
			mapParameter.put("P_KUNDE", sKunde);
			mapParameter.put("P_KUNDE_LIEFERADRESSE", sKundeLieferadresse);
			mapParameter.put("P_LOSGROESSE", losDto.getNLosgroesse());

			mapParameter.put("P_LOSKLASSEN", getLosLosKlassenAlsString(losDto.getIId()));

			mapParameter.put("P_LOSNUMMER", losDto.getCNr());
			mapParameter.put("P_PRODUKTIONSBEGINN", losDto.getTProduktionsbeginn());
			mapParameter.put("P_PRODUKTIONSENDE", losDto.getTProduktionsende());
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(losDto.getTProduktionsende());
			mapParameter.put("P_PRODUKTIONSENDE_KW", new Integer(gc.get(Calendar.WEEK_OF_YEAR)));

			mapParameter.put("P_LIEFERTERMIN", dLiefertermin);

			// Fertigungsort
			if (losDto.getPartnerIIdFertigungsort() != null) {
				PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(losDto.getPartnerIIdFertigungsort(),
						theClientDto);
				mapParameter.put("P_FERTIGUNGSORT", partnerDto.formatTitelAnrede());
			}

			// Erstlos
			mapParameter.put("P_ERSTLOS", new Boolean(istErstlos(losDto, theClientDto)));

			ArrayList<Object> images = new ArrayList<Object>();
			String sLosStuecklisteArtikelKommentar = "";
			// Bild einfuegen
			String sMengenEinheit = "";
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stuecklisteDto = getStuecklisteFac()
						.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(), theClientDto);
				// Einheit
				if (stuecklisteDto.getArtikelDto() != null) {
					if (stuecklisteDto.getArtikelDto().getEinheitCNr() != null) {
						sMengenEinheit = stuecklisteDto.getArtikelDto().getEinheitCNr();
					}
				}

				// Sperren
				String sperren = getArtikelFac().getArtikelsperrenText(stuecklisteDto.getArtikelIId());

				mapParameter.put("P_STUECKLISTESPERRSTATUS", sperren);

				// Abmessungen
				GeometrieDto geometrieDto = stuecklisteDto.getArtikelDto().getGeometrieDto();
				if (geometrieDto != null) {
					mapParameter.put(P_ARTIKEL_BREITE, geometrieDto.getFBreite());
					mapParameter.put(P_ARTIKEL_HOEHE, geometrieDto.getFHoehe());
					mapParameter.put(P_ARTIKEL_TIEFE, geometrieDto.getFTiefe());
				}
				// Bauform
				VerpackungDto verpackungDto = stuecklisteDto.getArtikelDto().getVerpackungDto();
				if (verpackungDto != null) {
					mapParameter.put("P_ARTIKEL_BAUFORM", verpackungDto.getCBauform());
					mapParameter.put("P_ARTIKEL_VERPACKUNGSART", verpackungDto.getCVerpackungsart());
				}

				mapParameter.put("P_INDEX", stuecklisteDto.getArtikelDto().getCIndex());
				mapParameter.put("P_REVISION", stuecklisteDto.getArtikelDto().getCRevision());

				ArtikelkommentarDto[] artikelkommentarDto = getArtikelkommentarFac()
						.artikelkommentardruckFindByArtikelIIdBelegartCNr(stuecklisteDto.getArtikelIId(),
								LocaleFac.BELEGART_LOS, theClientDto.getLocUiAsString(), theClientDto);

				if (artikelkommentarDto != null && artikelkommentarDto.length > 0) {

					for (int j = 0; j < artikelkommentarDto.length; j++) {
						if (artikelkommentarDto[j].getArtikelkommentarsprDto() != null) {
							// Text Kommentar
							if (artikelkommentarDto[j].getDatenformatCNr().trim()
									.indexOf(MediaFac.DATENFORMAT_MIMETYPEART_TEXT) != -1) {
								if (sLosStuecklisteArtikelKommentar == "") {
									sLosStuecklisteArtikelKommentar += artikelkommentarDto[j]
											.getArtikelkommentarsprDto().getXKommentar();
								} else {
									sLosStuecklisteArtikelKommentar += "\n"
											+ artikelkommentarDto[j].getArtikelkommentarsprDto().getXKommentar();
								}
							} else if (artikelkommentarDto[j].getDatenformatCNr()
									.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
									|| artikelkommentarDto[j].getDatenformatCNr()
											.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)
									|| artikelkommentarDto[j].getDatenformatCNr()
											.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)) {
								byte[] bild = artikelkommentarDto[j].getArtikelkommentarsprDto().getOMedia();
								if (bild != null) {
									java.awt.Image myImage = Helper.byteArrayToImage(bild);
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
			mapParameter.put("P_MENGENEINHEIT", sMengenEinheit);
			mapParameter.put("P_PROJEKT", losDto.getCProjekt());
			mapParameter.put("P_LOSKOMMENTAR", losDto.getCKommentar());
			mapParameter.put("P_LOSLANGTEXT", losDto.getXText());

			FertigungsgruppeDto fertGruppeDto = getStuecklisteFac()
					.fertigungsgruppeFindByPrimaryKey(losDto.getFertigungsgruppeIId());
			mapParameter.put("P_FERTIGUNGSGRUPPE", fertGruppeDto.getCBez());

			// PJ18776
			ParametermandantDto parametermandantDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_ISTZEITEN_GLEICH_SOLLZEITEN);
			mapParameter.put("P_ISTZEITEN_GLEICH_SOLLZEITEN",
					(java.lang.Boolean) parametermandantDto.getCWertAsObject());

			// Materialliste?
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stkDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(),
						theClientDto);

				if (sLosStuecklisteArtikelKommentar != "") {
					mapParameter.put("P_STUECKLISTEARTIKELKOMMENTAR",
							Helper.formatStyledTextForJasper(sLosStuecklisteArtikelKommentar));
				}

				mapParameter.put("P_STUECKLISTEBEZEICHNUNG", stkDto.getArtikelDto().getArtikelsprDto().getCBez());

				mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG",
						stkDto.getArtikelDto().getArtikelsprDto().getCZbez());
				mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG2",
						stkDto.getArtikelDto().getArtikelsprDto().getCZbez2());

				mapParameter.put("P_STUECKLISTEKURZBEZEICHNUNG", stkDto.getArtikelDto().getArtikelsprDto().getCKbez());
				mapParameter.put("P_STUECKLISTEREFERENZNUMMER", stkDto.getArtikelDto().getCReferenznr());

				mapParameter.put("P_STUECKLISTENUMMER", stkDto.getArtikelDto().getCNr());

				mapParameter.put("P_STUECKLISTE_GEWICHTKG", stkDto.getArtikelDto().getFGewichtkg());

				if (stkDto.getArtikelDto().getVerpackungDto() != null) {
					mapParameter.put("P_STUECKLISTE_BAUFORM", stkDto.getArtikelDto().getVerpackungDto().getCBauform());
					mapParameter.put("P_STUECKLISTE_VERPACKUNGSART",
							stkDto.getArtikelDto().getVerpackungDto().getCVerpackungsart());
				}

				if (stkDto.getArtikelDto().getGeometrieDto() != null) {
					mapParameter.put("P_STUECKLISTE_BREITETEXT",
							stkDto.getArtikelDto().getGeometrieDto().getCBreitetext());
					mapParameter.put("P_STUECKLISTE_BREITE", stkDto.getArtikelDto().getGeometrieDto().getFBreite());
					mapParameter.put("P_STUECKLISTE_HOEHE", stkDto.getArtikelDto().getGeometrieDto().getFHoehe());
					mapParameter.put("P_STUECKLISTE_TIEFE", stkDto.getArtikelDto().getGeometrieDto().getFTiefe());
				}

				// Stuecklisteneigenschaften
				StuecklisteeigenschaftDto[] stuecklisteeigenschaftDtos = getStuecklisteFac()
						.stuecklisteeigenschaftFindByStuecklisteIId(losDto.getStuecklisteIId());
				ArrayList<Object[]> al = new ArrayList<Object[]>();
				for (int j = 0; j < stuecklisteeigenschaftDtos.length; j++) {
					StuecklisteeigenschaftDto dto = stuecklisteeigenschaftDtos[j];

					Object[] o = new Object[2];
					String sStklEigenschaftArt = dto.getStuecklisteeigenschaftartDto().getCBez();
					o[0] = sStklEigenschaftArt;
					o[1] = dto.getCBez();
					al.add(o);

					// Index und Materialplatz auch einzeln an Report uebergeben
					if (sStklEigenschaftArt.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_INDEX)) {
						mapParameter.put(P_STUECKLISTENEIGENSCHAFT_INDEX, dto.getCBez());
					}
					if (sStklEigenschaftArt
							.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_MATERIALPLATZ)) {
						mapParameter.put(P_STUECKLISTENEIGENSCHAFT_MATERIALPLATZ, dto.getCBez());
					}

				}

				if (stuecklisteeigenschaftDtos.length > 0) {
					String[] fieldnames = new String[] { "F_EIGENSCHAFTART", "F_BEZEICHNUNG" };
					Object[][] dataSub = new Object[al.size()][fieldnames.length];
					dataSub = (Object[][]) al.toArray(dataSub);

					mapParameter.put("DATENSUBREPORT", new LPDatenSubreport(dataSub, fieldnames));
				}

			} else {
				mapParameter.put("P_STUECKLISTEBEZEICHNUNG", losDto.getCProjekt());
				mapParameter.put("P_STUECKLISTENUMMER",
						getTextRespectUISpr("fert.materialliste", theClientDto.getMandant(), theClientDto.getLocUi()));
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_LOSZEITEN,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public GesamtkalkulationDto getDatenGesamtkalkulation(Integer losIId, Integer losablieferungIId, int bisEbene,
			TheClientDto theClientDto) {

		GesamtkalkulationDto gkDto = getDatenGesamtkalkulation(new GesamtkalkulationDto(), losIId, losablieferungIId, 0,
				BigDecimal.ONE, bisEbene, theClientDto);

		BigDecimal bdAbgeliefert = null;
		try {
			bdAbgeliefert = getFertigungFac().getErledigteMenge(losIId, theClientDto);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		// Preise pro Stueck berechnen

		ArrayList alGK = gkDto.getAlDaten();

		BigDecimal bdGesamtwertAZ = BigDecimal.ZERO;
		BigDecimal bdGesamtwertMaterial = BigDecimal.ZERO;

		BigDecimal bdGesamtwertAZMann = BigDecimal.ZERO;
		BigDecimal bdGesamtwertAZMaschine = BigDecimal.ZERO;

		BigDecimal bdArbeitszeitGesamt = BigDecimal.ZERO;

		BigDecimal bdArbeitszeitGesamtMann = BigDecimal.ZERO;
		BigDecimal bdArbeitszeitGesamtMaschine = BigDecimal.ZERO;

		BigDecimal bdLief1WertMaterial = BigDecimal.ZERO;

		BigDecimal bdEinstandsWertMaterialAusKundenkonsignationslager = BigDecimal.ZERO;

		for (int n = 0; n < alGK.size(); n++) {
			Object[] oZeileGK = (Object[]) alGK.get(n);

			BigDecimal menge = (BigDecimal) oZeileGK[GESAMTKALKULATION_ISTMENGE];
			BigDecimal preis = (BigDecimal) oZeileGK[GESAMTKALKULATION_ISTPREIS];

			BigDecimal lief1preis = (BigDecimal) oZeileGK[GESAMTKALKULATION_LIEF1PREIS];

			BigDecimal bdMengenfaktor = BigDecimal.ONE;
			if (oZeileGK[GESAMTKALKULATION_MENGENFAKTOR] != null) {
				bdMengenfaktor = (BigDecimal) oZeileGK[GESAMTKALKULATION_MENGENFAKTOR];
			}

			Boolean bAZ = (Boolean) oZeileGK[GESAMTKALKULATION_ARBEITSZEIT];

			if (oZeileGK[GESAMTKALKULATION_BELEGART_ZUGANG] != null
					&& oZeileGK[GESAMTKALKULATION_BELEGART_ZUGANG].equals(LocaleFac.BELEGART_LOSABLIEFERUNG)) {
				continue;
			}

			if (bAZ == false && menge != null && preis != null) {
				bdGesamtwertMaterial = bdGesamtwertMaterial.add(menge.multiply(preis).multiply(bdMengenfaktor));
			}

			if (bAZ == false && menge != null && lief1preis != null) {
				bdLief1WertMaterial = bdLief1WertMaterial.add(menge.multiply(lief1preis).multiply(bdMengenfaktor));
			}

			if (bAZ == true && menge != null && preis != null) {

				bdGesamtwertAZ = bdGesamtwertAZ.add(menge.multiply(preis).multiply(bdMengenfaktor));

				bdArbeitszeitGesamt = bdArbeitszeitGesamt.add(menge.multiply(bdMengenfaktor));

				if (oZeileGK[FertigungReportFac.GESAMTKALKULATION_MASCHINE] == null) {
					bdGesamtwertAZMann = bdGesamtwertAZMann.add(menge.multiply(preis).multiply(bdMengenfaktor));

					bdArbeitszeitGesamtMann = bdArbeitszeitGesamtMann.add(menge.multiply(bdMengenfaktor));

				} else {
					bdGesamtwertAZMaschine = bdGesamtwertAZMaschine.add(menge.multiply(preis).multiply(bdMengenfaktor));

					bdArbeitszeitGesamtMaschine = bdArbeitszeitGesamtMaschine.add(menge.multiply(bdMengenfaktor));
				}

			}

		}

		if (bdAbgeliefert != null && bdAbgeliefert.doubleValue() != 0) {

			gkDto.setDurchschnittlicherMaterialpreisProStueck(
					bdGesamtwertMaterial.divide(bdAbgeliefert, 4, BigDecimal.ROUND_HALF_EVEN));
			gkDto.setDurchschnittlicherAZPreisProStueck(
					bdGesamtwertAZ.divide(bdAbgeliefert, 4, BigDecimal.ROUND_HALF_EVEN));
			gkDto.setDurchschnittlicherLief1WertProStueck(
					bdLief1WertMaterial.divide(bdAbgeliefert, 4, BigDecimal.ROUND_HALF_EVEN));
		} else {
			gkDto.setDurchschnittlicherMaterialpreisProStueck(BigDecimal.ZERO);
			gkDto.setDurchschnittlicherAZPreisProStueck(BigDecimal.ZERO);
			gkDto.setDurchschnittlicherLief1WertProStueck(BigDecimal.ZERO);
		}

		gkDto.setAZwertGesamt(bdGesamtwertAZ);
		gkDto.setMaterialwertGesamt(bdGesamtwertMaterial);

		gkDto.setBdLief1WertMaterial(bdLief1WertMaterial);

		gkDto.setBdGesamtwertAZMann(bdGesamtwertAZMann);
		gkDto.setBdGesamtwertAZMaschine(bdGesamtwertAZMaschine);

		gkDto.setBdArbeitszeitGesamt(bdArbeitszeitGesamt);
		gkDto.setBdArbeitszeitGesamtMann(bdArbeitszeitGesamtMann);
		gkDto.setBdArbeitszeitGesamtMaschine(bdArbeitszeitGesamtMaschine);

		return gkDto;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public GesamtkalkulationDto getDatenGesamtkalkulation(GesamtkalkulationDto gkDto, Integer losIId,
			Integer losablieferungIId, int iEbene, BigDecimal mengenfaktor, int bisEbene, TheClientDto theClientDto) {

		if (iEbene >= bisEbene) {
			return gkDto;
		}

		LosDto losDto = null;
		LosablieferungDto losablieferungDto = null;
		boolean bSollGleichIstzeiten = false;
		try {
			losDto = getFertigungFac().losFindByPrimaryKey(losIId);
			if (losablieferungIId != null) {
				losablieferungDto = getFertigungFac().losablieferungFindByPrimaryKey(losablieferungIId, false,
						theClientDto);
			}

			ParametermandantDto parametermandantDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_ISTZEITEN_GLEICH_SOLLZEITEN);
			bSollGleichIstzeiten = (java.lang.Boolean) parametermandantDto.getCWertAsObject();

		} catch (RemoteException e1) {
			throwEJBExceptionLPRespectOld(e1);
		}

//		if (losDto.getCNr().equals("2016/00108-999")) {
//			int i = 0;
//		}

		iEbene = iEbene + 1;
		// Zuerst Arbeit andrucken

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQueryArbeitsplan = "FROM FLRLossollarbeitsplan AS s WHERE s.flrlos.i_id=" + losIId
				+ "ORDER BY s.flrartikel.c_nr ";

		org.hibernate.Query hqueryArbeitsplan = session.createQuery(sQueryArbeitsplan);

		List<?> resultListArbeitsplan = hqueryArbeitsplan.list();
		Iterator<?> resultListIteratorArbeitsplan = resultListArbeitsplan.iterator();

		class MaschineArtikel {

			MaschineArtikel(Integer artikelIId, Integer maschineIId) {
				this.artikelIId = artikelIId;
				this.maschineIId = maschineIId;
			}

			public Integer artikelIId;
			public Integer maschineIId;

			public int hashCode() {
				int result = 17;
				result = 37 * result + this.artikelIId.hashCode();
				result = 37 * result + this.maschineIId.hashCode();
				return result;
			}

		}

		LinkedHashMap<Integer, Object[]> hmArbeitsplanEinesLoses = new LinkedHashMap();
		LinkedHashMap<Integer, Object[]> hmArbeitsplanEinesLosesMaschine = new LinkedHashMap();

		while (resultListIteratorArbeitsplan.hasNext()) {
			FLRLossollarbeitsplan arbeitsplan = (FLRLossollarbeitsplan) resultListIteratorArbeitsplan.next();

			Object[] zeile = new Object[GESAMTKALKULATION_ANZAHL_SPALTEN];

			ArtikelDto aDto = null;
			if (gkDto.hmArtikelDto.containsKey(arbeitsplan.getFlrartikel().getI_id())) {
				aDto = gkDto.hmArtikelDto.get(arbeitsplan.getFlrartikel().getI_id());
			} else {
				aDto = getArtikelFac().artikelFindByPrimaryKeySmall(arbeitsplan.getFlrartikel().getI_id(),
						theClientDto);
				gkDto.hmArtikelDto.put(arbeitsplan.getFlrartikel().getI_id(), aDto);
			}

			zeile[GESAMTKALKULATION_ARTIKELNUMMER] = aDto.getCNr();

			if (aDto.getArtikelsprDto() != null) {
				zeile[GESAMTKALKULATION_ARTIKELBEZEICHNUNG] = aDto.getArtikelsprDto().getCBez();
				zeile[GESAMTKALKULATION_ZUSATZBEZEICHNUNG] = aDto.getArtikelsprDto().getCZbez();
			}

			zeile[GESAMTKALKULATION_EINHEIT] = aDto.getEinheitCNr();
			zeile[GESAMTKALKULATION_SOLLMENGE] = arbeitsplan.getN_gesamtzeit();

			zeile[GESAMTKALKULATION_ISTMENGE] = BigDecimal.ZERO;
			zeile[GESAMTKALKULATION_ISTPREIS] = BigDecimal.ZERO;

			if (bSollGleichIstzeiten) {
				zeile[GESAMTKALKULATION_ISTMENGE] = arbeitsplan.getN_gesamtzeit();

				try {

					ArtikellieferantDto artlief = getArtikelFac().getArtikelEinkaufspreisDesBevorzugtenLieferanten(
							arbeitsplan.getFlrartikel().getI_id(), new BigDecimal(1),
							theClientDto.getSMandantenwaehrung(), theClientDto);
					if (artlief != null && artlief.getLief1Preis() != null) {
						BigDecimal bdSollpreis = artlief.getLief1Preis();

						zeile[GESAMTKALKULATION_ISTPREIS] = bdSollpreis;

						zeile[GESAMTKALKULATION_ARBEITSZEIT_KOSTEN] = bdSollpreis
								.multiply(arbeitsplan.getN_gesamtzeit());
					}

					BigDecimal preis = getLagerFac().getGemittelterGestehungspreisAllerLaegerEinesMandanten(
							arbeitsplan.getFlrartikel().getI_id(), theClientDto);
					zeile[GESAMTKALKULATION_ISTPREIS] = preis;
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}

			zeile[GESAMTKALKULATION_LOSGROESSE] = arbeitsplan.getFlrlos().getN_losgroesse();
			zeile[GESAMTKALKULATION_LOSNUMMER] = arbeitsplan.getFlrlos().getC_nr();
			zeile[GESAMTKALKULATION_EBENE] = new Integer(iEbene);
			zeile[GESAMTKALKULATION_ARBEITSZEIT] = Boolean.TRUE;
			zeile[GESAMTKALKULATION_MENGENFAKTOR] = mengenfaktor;

			if (!Helper.short2boolean(arbeitsplan.getB_nurmaschinenzeit())) {

				if (!hmArbeitsplanEinesLoses.containsKey(arbeitsplan.getFlrartikel().getI_id())) {
					hmArbeitsplanEinesLoses.put(arbeitsplan.getFlrartikel().getI_id(), zeile);

				}
			}

			if (arbeitsplan.getFlrmaschine() != null) {

				Object[] zeileMaschinenzeit = zeile.clone();

				zeileMaschinenzeit[GESAMTKALKULATION_MASCHINE] = "M:" + arbeitsplan.getFlrmaschine().getC_bez();

				if (bSollGleichIstzeiten) {
					BigDecimal kosten = getZeiterfassungFac()
							.getMaschinenKostenZumZeitpunkt(arbeitsplan.getFlrmaschine().getI_id(),
									Helper.cutTimestamp(new Timestamp(System.currentTimeMillis())),LocaleFac.BELEGART_LOS,arbeitsplan.getI_id())
							.getBdStundensatz();

					// TODO ghp, 2021-08-13: Warum "zeile" und nicht "zeileMaschinenzeit"?
					zeile[GESAMTKALKULATION_ARBEITSZEIT_KOSTEN] = kosten.multiply(arbeitsplan.getN_gesamtzeit());
				}

				MaschineArtikel key = new MaschineArtikel(arbeitsplan.getFlrartikel().getI_id(),
						arbeitsplan.getFlrmaschine().getI_id());

				if (!hmArbeitsplanEinesLosesMaschine.containsKey(key.hashCode())) {
					hmArbeitsplanEinesLosesMaschine.put(key.hashCode(), zeileMaschinenzeit);

				}
			}

		}
		session.close();

		if (bSollGleichIstzeiten == false) {
			// Nun Zeiten addieren

			AuftragzeitenDto[] belegzeitenDtos = getZeiterfassungFac().getAllZeitenEinesBeleges(LocaleFac.BELEGART_LOS,
					losIId, null, null, null, null, ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ARTIKEL, false, theClientDto);

			for (int i = 0; i < belegzeitenDtos.length; i++) {

				AuftragzeitenDto azDto = belegzeitenDtos[i];

				Object[] zeile = null;

				if (hmArbeitsplanEinesLoses.containsKey(azDto.getArtikelIId())) {

					zeile = hmArbeitsplanEinesLoses.get(azDto.getArtikelIId());

				} else {

					ArtikelDto aDto = null;
					if (gkDto.hmArtikelDto.containsKey(azDto.getArtikelIId())) {
						aDto = gkDto.hmArtikelDto.get(azDto.getArtikelIId());
					} else {
						aDto = getArtikelFac().artikelFindByPrimaryKeySmall(azDto.getArtikelIId(), theClientDto);
						gkDto.hmArtikelDto.put(azDto.getArtikelIId(), aDto);
					}

					zeile = new Object[GESAMTKALKULATION_ANZAHL_SPALTEN];
					zeile[GESAMTKALKULATION_ARTIKELNUMMER] = aDto.getCNr();

					if (aDto.getArtikelsprDto() != null) {
						zeile[GESAMTKALKULATION_ARTIKELBEZEICHNUNG] = aDto.getArtikelsprDto().getCBez();
						zeile[GESAMTKALKULATION_ZUSATZBEZEICHNUNG] = aDto.getArtikelsprDto().getCZbez();
					}

					zeile[GESAMTKALKULATION_EINHEIT] = aDto.getEinheitCNr();
					zeile[GESAMTKALKULATION_SOLLMENGE] = BigDecimal.ZERO;
					zeile[GESAMTKALKULATION_ISTMENGE] = BigDecimal.ZERO;
					zeile[GESAMTKALKULATION_ISTPREIS] = BigDecimal.ZERO;
					zeile[GESAMTKALKULATION_LOSGROESSE] = losDto.getNLosgroesse();
					zeile[GESAMTKALKULATION_LOSNUMMER] = losDto.getCNr();
					zeile[GESAMTKALKULATION_EBENE] = new Integer(iEbene);
					zeile[GESAMTKALKULATION_ARBEITSZEIT] = Boolean.TRUE;
					zeile[GESAMTKALKULATION_MENGENFAKTOR] = mengenfaktor;
				}

				BigDecimal bIst = (BigDecimal) zeile[GESAMTKALKULATION_ISTMENGE];
				if (bIst == null) {
					bIst = BigDecimal.ZERO;
				}
				bIst = bIst.add(new BigDecimal(azDto.getDdDauer().doubleValue()));

				zeile[GESAMTKALKULATION_ISTMENGE] = bIst;

				BigDecimal bKosten = (BigDecimal) zeile[GESAMTKALKULATION_ARBEITSZEIT_KOSTEN];
				if (bKosten == null) {
					bKosten = BigDecimal.ZERO;
				}

				bKosten = bKosten.add(new BigDecimal(azDto.getBdKosten().doubleValue()));
				zeile[GESAMTKALKULATION_ARBEITSZEIT_KOSTEN] = bKosten;

				// TODO ghp, 2021-08-13 Warum hier ein genereller put?
				// Nur wenn der Artikel nicht existiert hat notwendig, gleich
				// dort hinverlagern, wo die Zeile neu erzeugt wird
				hmArbeitsplanEinesLoses.put(azDto.getArtikelIId(), zeile);

			}
		}

		Iterator it = hmArbeitsplanEinesLoses.keySet().iterator();
		while (it.hasNext()) {
			Object[] zeile = hmArbeitsplanEinesLoses.get(it.next());

			BigDecimal bIst = (BigDecimal) zeile[GESAMTKALKULATION_ISTMENGE];
			if (bIst == null) {
				bIst = BigDecimal.ZERO;
			}

			BigDecimal bKosten = (BigDecimal) zeile[GESAMTKALKULATION_ARBEITSZEIT_KOSTEN];
			if (bKosten == null) {
				bKosten = BigDecimal.ZERO;
			}

			if (bIst.doubleValue() != 0) {
				zeile[GESAMTKALKULATION_ISTPREIS] = bKosten.divide(bIst, 4, BigDecimal.ROUND_HALF_EVEN);
				zeile[GESAMTKALKULATION_EINSTANDSPREIS] = bKosten.divide(bIst, 4, BigDecimal.ROUND_HALF_EVEN)
						.multiply(mengenfaktor);
			}
			gkDto.getAlDaten().add(zeile);

		}

		if (bSollGleichIstzeiten == false) {

			// Maschinenzeiten

			AuftragzeitenDto[] maschinenzeitenDtos = null;
			try {
				maschinenzeitenDtos = getZeiterfassungFac().getAllMaschinenzeitenEinesBeleges(losIId, null, null, null,
						theClientDto);

			} catch (RemoteException e1) {
				throwEJBExceptionLPRespectOld(e1);
			}

			// Nun MaschinenZeiten addieren
			for (int i = 0; i < maschinenzeitenDtos.length; i++) {

				AuftragzeitenDto azDto = maschinenzeitenDtos[i];

				Object[] zeile = null;

				MaschineArtikel key = new MaschineArtikel(azDto.getArtikelIId(), azDto.getIPersonalMaschinenId());

				if (hmArbeitsplanEinesLosesMaschine.containsKey(key.hashCode())) {

					zeile = hmArbeitsplanEinesLosesMaschine.get(key.hashCode());

				} else {

					ArtikelDto aDto = null;
					if (gkDto.hmArtikelDto.containsKey(azDto.getArtikelIId())) {
						aDto = gkDto.hmArtikelDto.get(azDto.getArtikelIId());
					} else {
						aDto = getArtikelFac().artikelFindByPrimaryKeySmall(azDto.getArtikelIId(), theClientDto);
						gkDto.hmArtikelDto.put(azDto.getArtikelIId(), aDto);
					}

					zeile = new Object[GESAMTKALKULATION_ANZAHL_SPALTEN];
					zeile[GESAMTKALKULATION_ARTIKELNUMMER] = aDto.getCNr();

					if (aDto.getArtikelsprDto() != null) {
						zeile[GESAMTKALKULATION_ARTIKELBEZEICHNUNG] = aDto.getArtikelsprDto().getCBez();
						zeile[GESAMTKALKULATION_ZUSATZBEZEICHNUNG] = aDto.getArtikelsprDto().getCZbez();
					}

					zeile[GESAMTKALKULATION_EINHEIT] = aDto.getEinheitCNr();
					zeile[GESAMTKALKULATION_SOLLMENGE] = BigDecimal.ZERO;
					zeile[GESAMTKALKULATION_ISTMENGE] = BigDecimal.ZERO;
					zeile[GESAMTKALKULATION_ISTPREIS] = BigDecimal.ZERO;
					zeile[GESAMTKALKULATION_LOSGROESSE] = losDto.getNLosgroesse();
					zeile[GESAMTKALKULATION_LOSNUMMER] = losDto.getCNr();
					zeile[GESAMTKALKULATION_EBENE] = new Integer(iEbene);
					zeile[GESAMTKALKULATION_ARBEITSZEIT] = Boolean.TRUE;
					zeile[GESAMTKALKULATION_MENGENFAKTOR] = mengenfaktor;

					zeile[GESAMTKALKULATION_MASCHINE] = maschinenzeitenDtos[i].getSPersonalMaschinenname();

				}

				BigDecimal bIst = (BigDecimal) zeile[GESAMTKALKULATION_ISTMENGE];
				if (bIst == null) {
					bIst = BigDecimal.ZERO;
				}
				bIst = bIst.add(new BigDecimal(azDto.getDdDauer().doubleValue()));

				zeile[GESAMTKALKULATION_ISTMENGE] = bIst;

				BigDecimal bKosten = (BigDecimal) zeile[GESAMTKALKULATION_ARBEITSZEIT_KOSTEN];
				if (bKosten == null) {
					bKosten = BigDecimal.ZERO;
				}

				bKosten = bKosten.add(new BigDecimal(azDto.getBdKosten().doubleValue()));
				zeile[GESAMTKALKULATION_ARBEITSZEIT_KOSTEN] = bKosten;

				hmArbeitsplanEinesLosesMaschine.put(key.hashCode(), zeile);

			}

		}

		Iterator itMaschine = hmArbeitsplanEinesLosesMaschine.keySet().iterator();
		while (itMaschine.hasNext()) {
			Object[] zeile = hmArbeitsplanEinesLosesMaschine.get(itMaschine.next());

			BigDecimal bIst = (BigDecimal) zeile[GESAMTKALKULATION_ISTMENGE];
			if (bIst == null) {
				bIst = BigDecimal.ZERO;
			}

			BigDecimal bKosten = (BigDecimal) zeile[GESAMTKALKULATION_ARBEITSZEIT_KOSTEN];
			if (bKosten == null) {
				bKosten = BigDecimal.ZERO;
			}

			if (bIst.doubleValue() != 0) {
				zeile[GESAMTKALKULATION_ISTPREIS] = bKosten.divide(bIst, 4, BigDecimal.ROUND_HALF_EVEN);
				zeile[GESAMTKALKULATION_EINSTANDSPREIS] = bKosten.divide(bIst, 4, BigDecimal.ROUND_HALF_EVEN)
						.multiply(mengenfaktor);
			}
			gkDto.getAlDaten().add(zeile);

		}

		GeraetesnrDto[] gsnrDtos = null;

		if (losablieferungIId != null && losablieferungDto.getSeriennrChargennrMitMenge() != null
				&& losablieferungDto.getSeriennrChargennrMitMenge().size() == 1) {
			gsnrDtos = getLagerFac().getGeraeteseriennummerEinerLagerbewegung(LocaleFac.BELEGART_LOSABLIEFERUNG,
					losablieferungIId, losablieferungDto.getSeriennrChargennrMitMenge().get(0).getCSeriennrChargennr());
		}

		ArrayList<LossollmaterialDto> sollmatDtos = new ArrayList<LossollmaterialDto>();
		if (gkDto.hmLossollmaterialDto.containsKey(losIId)) {
			sollmatDtos = gkDto.hmLossollmaterialDto.get(losIId);
		} else {

			Session sessionSub = FLRSessionFactory.getFactory().openSession();
			String sQuerySub = "FROM FLRLossollmaterial AS s WHERE s.flrlos.i_id=" + losIId
					+ "ORDER BY s.flrartikel.c_nr, s.t_aendern ";

			org.hibernate.Query hquerySub = sessionSub.createQuery(sQuerySub);
			List<?> resultListSub = hquerySub.list();
			Iterator<?> resultListIteratorSub = resultListSub.iterator();

			while (resultListIteratorSub.hasNext()) {
				FLRLossollmaterial sollmaterial = (FLRLossollmaterial) resultListIteratorSub.next();
				try {
					sollmatDtos.add(getFertigungFac().lossollmaterialFindByPrimaryKey(sollmaterial.getI_id()));
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}
			gkDto.hmLossollmaterialDto.put(losIId, sollmatDtos);

		}

		Iterator<?> resultListIteratorSub = sollmatDtos.iterator();

		while (resultListIteratorSub.hasNext()) {
			LossollmaterialDto sollmaterial = (LossollmaterialDto) resultListIteratorSub.next();

			Object[] zeileVorlage = new Object[GESAMTKALKULATION_ANZAHL_SPALTEN];

			ArtikelDto aDto = null;
			if (gkDto.hmArtikelDto.containsKey(sollmaterial.getArtikelIId())) {
				aDto = gkDto.hmArtikelDto.get(sollmaterial.getArtikelIId());
			} else {
				aDto = getArtikelFac().artikelFindByPrimaryKeySmall(sollmaterial.getArtikelIId(), theClientDto);
				gkDto.hmArtikelDto.put(sollmaterial.getArtikelIId(), aDto);
			}

			zeileVorlage[GESAMTKALKULATION_CHNRBEHAFTET] = Helper.short2Boolean(aDto.getBChargennrtragend());
			zeileVorlage[GESAMTKALKULATION_SNRBEHAFTET] = Helper.short2Boolean(aDto.getBSeriennrtragend());

			zeileVorlage[GESAMTKALKULATION_ARTIKELNUMMER] = aDto.getCNr();
			if (aDto.getArtikelsprDto() != null) {
				zeileVorlage[GESAMTKALKULATION_ARTIKELBEZEICHNUNG] = aDto.getArtikelsprDto().getCBez();
				zeileVorlage[GESAMTKALKULATION_ZUSATZBEZEICHNUNG] = aDto.getArtikelsprDto().getCZbez();
			}

			zeileVorlage[GESAMTKALKULATION_EINHEIT] = aDto.getEinheitCNr();
			zeileVorlage[GESAMTKALKULATION_SOLLMENGE] = sollmaterial.getNMenge();
			zeileVorlage[GESAMTKALKULATION_SOLLPREIS] = sollmaterial.getNSollpreis();
			zeileVorlage[GESAMTKALKULATION_ISTMENGE] = BigDecimal.ZERO;
			zeileVorlage[GESAMTKALKULATION_ISTPREIS] = BigDecimal.ZERO;
			zeileVorlage[GESAMTKALKULATION_LOSGROESSE] = losDto.getNLosgroesse();
			zeileVorlage[GESAMTKALKULATION_LOSNUMMER] = losDto.getCNr();
			zeileVorlage[GESAMTKALKULATION_EBENE] = new Integer(iEbene);
			zeileVorlage[GESAMTKALKULATION_ARBEITSZEIT] = Boolean.FALSE;
			zeileVorlage[GESAMTKALKULATION_MENGENFAKTOR] = mengenfaktor;

			boolean bZeileHinzugefuegt = false;

			if (sollmaterial.getIstmaterialDtos() == null) {
				try {
					sollmaterial.setIstmaterialDtos(
							getFertigungFac().losistmaterialFindByLossollmaterialIId(sollmaterial.getIId()));
					gkDto.hmLossollmaterialDto.put(losIId, sollmatDtos);
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

			for (int x = 0; x < sollmaterial.getIstmaterialDtos().length; x++) {
				LosistmaterialDto item = sollmaterial.getIstmaterialDtos()[x];

				LagerDto lagerDto = null;

				if (gkDto.hmLagerDto.containsKey(item.getLagerIId())) {
					lagerDto = gkDto.hmLagerDto.get(item.getLagerIId());
				} else {
					try {
						lagerDto = getLagerFac().lagerFindByPrimaryKey(item.getLagerIId());
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
					gkDto.hmLagerDto.put(item.getLagerIId(), lagerDto);
				}

				boolean bKundenkonsignatonslager = Helper.short2boolean(lagerDto.getBKonsignationslager());

				if (Helper.short2boolean(aDto.getBSeriennrtragend()) && losablieferungIId != null) {
					// Nur anzeigen, wenn Geraetesnr zu der Ablieferung passt
					boolean bAnzeigen = false;

					List<SeriennrChargennrMitMengeDto> bebucht = gkDto.getFromHmSnrChnrLosIstMaterial(item.getIId());
					if (bebucht == null) {
						bebucht = getLagerFac().getAllSeriennrchargennrEinerBelegartpositionOhneChargeneigenschaften(
								LocaleFac.BELEGART_LOS, item.getIId());
						gkDto.add2HmSnrChnrLosIstMaterial(item.getIId(), bebucht);
					}

					if (gsnrDtos != null) {
						for (int i = 0; i < gsnrDtos.length; i++) {
							if (gsnrDtos[i].getArtikelIId().equals(aDto.getIId()) && bebucht.size() > 0
									&& gsnrDtos[i].getCSnr().equals(bebucht.get(0).getCSeriennrChargennr())) {

								bAnzeigen = true;

							}
						}
					}
					if (bAnzeigen == false) {
						continue;
					}
				}

				// SP3613
				if (item.getNMenge().doubleValue() == 0) {
					continue;
				}

				Object[] zeile = zeileVorlage.clone();

				if (Helper.short2boolean(item.getBAbgang())) {
					zeile[GESAMTKALKULATION_ISTMENGE] = item.getNMenge();
				} else {
					// SP8736
					zeile[GESAMTKALKULATION_ISTMENGE] = item.getNMenge().negate();
				}

				try {

					ArtikellieferantDto alDto = null;
					if (gkDto.preisVorhanden(aDto.getIId(), item.getNMenge())) {
						alDto = gkDto.getLief1Preis(aDto.getIId(), item.getNMenge());
					} else {
						// PJ20404
						alDto = getArtikelFac().getArtikelEinkaufspreis(aDto.getIId(), null, item.getNMenge(),
								theClientDto.getSMandantenwaehrung(),
								new java.sql.Date(losDto.getTProduktionsbeginn().getTime()),

								theClientDto);

						gkDto.add2HmLief1Preise(aDto.getIId(), item.getNMenge(), alDto);
					}

					if (alDto != null) {
						zeile[GESAMTKALKULATION_LIEF1PREIS] = alDto.getNNettopreis();

					}

					// Ist der Ursprung aus einer Stueckliste

					BigDecimal bdPreis = gkDto.getFromHmPreisLosIstMaterial(item.getIId());

					if (bdPreis == null) {
						if (Helper.short2boolean(item.getBAbgang())) {

							bdPreis = getLagerFac().getGemittelterGestehungspreisEinerAbgangsposition(
									LocaleFac.BELEGART_LOS, item.getIId());

						} else {
							bdPreis = getLagerFac().getGemittelterEinstandspreisEinerZugangsposition(
									LocaleFac.BELEGART_LOS, item.getIId());

						}

						gkDto.add2HmPreisLosIstMaterial(item.getIId(), bdPreis);
					}

					zeile[GESAMTKALKULATION_ISTPREIS] = bdPreis;

					// CACHING
					ArrayList<WarenzugangsreferenzDto> alZu = gkDto
							.getFromHmWareneingangsreferenzLosIstMaterial(item.getIId());
					if (alZu == null) {

						List<SeriennrChargennrMitMengeDto> snrChnrs = gkDto
								.getFromHmSnrChnrLosIstMaterial(item.getIId());
						if (snrChnrs == null) {

							snrChnrs = getLagerFac()
									.getAllSeriennrchargennrEinerBelegartpositionOhneChargeneigenschaften(
											LocaleFac.BELEGART_LOS, item.getIId());

							gkDto.add2HmSnrChnrLosIstMaterial(item.getIId(), snrChnrs);
						}

						// System.out.println("Hole WE-Referenz fuer Los :" +
						// item.getFlrlossollmaterial().getFlrlos().getC_nr() + " Sollmat:"
						// +item.getFlrlossollmaterial().getFlrartikel().getC_nr() + " SNRCHNR: "
						// +
						// SeriennrChargennrMitMengeDto.erstelleStringAusMehrerenSeriennummern(snrChnrs));

						alZu = getLagerFac().getWareneingangsreferenz(LocaleFac.BELEGART_LOS, item.getIId(), snrChnrs,
								false, theClientDto);
						gkDto.add2HmWareneingangsreferenzLosIstMaterial(item.getIId(), alZu);
					}

					BigDecimal einstandswert = gkDto.getFromHmEinstandswertLosIstMaterial(item.getIId());
					List<SeriennrChargennrMitMengeDto> snrs = gkDto.getFromHmSnrChnrLosIstMaterial(item.getIId());

					if (einstandswert == null) {

						einstandswert = BigDecimal.ZERO;

						for (int m = 0; m < snrs.size(); m++) {

							try {

								BigDecimal bdEinstandwert = getLagerFac().getEinstandspreis(LocaleFac.BELEGART_LOS,
										item.getIId(), snrs.get(m).getCSeriennrChargennr());
								if (bdEinstandwert != null) {
									einstandswert = einstandswert.add(bdEinstandwert);

								}
							} catch (RemoteException e) {
								throwEJBExceptionLPRespectOld(e);
							}
						}

						gkDto.add2HmEinstandswertLosIstMaterial(item.getIId(), einstandswert);

					}

					zeile[GESAMTKALKULATION_SNR_CHNR] = SeriennrChargennrMitMengeDto
							.erstelleStringAusMehrerenSeriennummern(snrs);

					// lt. WH muss hier durch durch ist-Menge anstatt der
					// abliefermenge dividiert werden
					if (item.getNMenge().doubleValue() != 0 && mengenfaktor != null) {
						zeile[GESAMTKALKULATION_EINSTANDSPREIS] = new BigDecimal(einstandswert.doubleValue()
								/ item.getNMenge().doubleValue() * mengenfaktor.doubleValue());
					}

					// TODO, ghp, 2021-08-13 Wieso nicht:
					// einstandswert.multiply(mengenfaktor).divide(item.getNMenge(),6,
					// RoundingMode.HALF_EVEN);
					// einstandswert.divide(item.getNMenge()).multiply(mengenfaktor);

					for (int i = 0; i < alZu.size(); i++) {

						WarenzugangsreferenzDto wzuDto = alZu.get(i);
						// System.out.println(wzuDto.toString());

						Object[] zeileWarenzugang = zeile.clone();

						zeileWarenzugang[GESAMTKALKULATION_BELEGART_ZUGANG] = wzuDto.getBelegart();
						zeileWarenzugang[GESAMTKALKULATION_BELEGNUMMER_ZUGANG] = wzuDto.getBelegnummer();
						zeileWarenzugang[GESAMTKALKULATION_BELEGSTATUS_ZUGANG] = wzuDto.getBelegstatus();
						zeileWarenzugang[GESAMTKALKULATION_BELEGARTPOSITION_I_ID_ZUGANG] = wzuDto
								.getBelegartpositionIId();

						zeileWarenzugang[GESAMTKALKULATION_VERBRAUCHTE_MENGE] = wzuDto.getMenge();
						zeileWarenzugang[GESAMTKALKULATION_ISTMENGE] = wzuDto.getMenge();
						zeileWarenzugang[GESAMTKALKULATION_ISTPREIS] = bdPreis;

						gkDto.getAlDaten().add(zeileWarenzugang);
						// Wenn Ursprung aus LosAblieferung, dann rekursiv
						bZeileHinzugefuegt = true;
						if (wzuDto.getBelegart().equals(LocaleFac.BELEGART_LOSABLIEFERUNG)) {

							// Losablieferung holen
							LosablieferungDto laDto = getFertigungFac().losablieferungFindByPrimaryKeyOhneExc(
									wzuDto.getBelegartpositionIId(), false, theClientDto);

							LosDto losDtoLA = getFertigungFac().losFindByPrimaryKey(laDto.getLosIId());

							if (Helper.rundeKaufmaennisch(laDto.getNGestehungspreis(), 2).doubleValue() != Helper
									.rundeKaufmaennisch(bdPreis, 2).doubleValue()) {

								System.out.println(losDtoLA.getCNr() + "  "
										+ Helper.formatZahl(laDto.getNGestehungspreis(), 4, theClientDto.getLocUi())
										+ " -- " + Helper.formatZahl(bdPreis, 4, theClientDto.getLocUi()));
							}

							BigDecimal mngfaktor = null;
							if (laDto != null && wzuDto.getMenge().doubleValue() != 0
									&& losDtoLA.getNLosgroesse().doubleValue() != 0) {
								mngfaktor = wzuDto.getMenge()
										.divide(losDtoLA.getNLosgroesse(), 10, BigDecimal.ROUND_HALF_EVEN)
										.multiply(mengenfaktor);
							}

							Integer loablieferungIIdFuerNaechsteEbene = null;

							if (losablieferungIId != null) {
								loablieferungIIdFuerNaechsteEbene = laDto.getIId();
							}

							gkDto = getDatenGesamtkalkulation(gkDto, wzuDto.getBelegartIId(),
									loablieferungIIdFuerNaechsteEbene, iEbene, mngfaktor, bisEbene, theClientDto);
							if (laDto != null) {
								gkDto.addBetroffenesLos(iEbene, laDto.getLosIId());
							}

						}

					}

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				if (bZeileHinzugefuegt == false) {
					gkDto.getAlDaten().add(zeile);
				}

			}

		}

		return gkDto;
	}

	public JasperPrintLP printBedarfsuebernahmeSynchronisierung(Integer personalIId, boolean bStatusAufOffenSetzen,
			TheClientDto theClientDto) {

		this.useCase = UC_BEDARFSUEBERNAHME_SYNCHRONISIERUNG;

		Map<String, Object> mapParameter = new TreeMap<String, Object>();

		ArrayList alDaten = new ArrayList();

		SessionFactory factory = FLRSessionFactory.getFactory();
		org.hibernate.Session session = factory.openSession();

		String queryString = "SELECT b FROM FLRBedarfsuebernahme b WHERE b.flrpersonal_anlegen.mandant_c_nr='"
				+ theClientDto.getMandant() + "' AND b.status_c_nr='" + LocaleFac.STATUS_ANGELEGT + "'";

		if (personalIId != null) {
			PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(personalIId, theClientDto);
			mapParameter.put("P_PERSON", personalDto.formatFixUFTitelName2Name1());

			queryString += " AND b.personal_i_id_anlegen=" + personalIId;
		}

		queryString += " ORDER BY b.flrpersonal_anlegen.c_kurzzeichen, b.t_anlegen ASC";

		org.hibernate.Query query = session.createQuery(queryString);

		List<?> resultList = query.list();
		Iterator it = resultList.iterator();
		while (it.hasNext()) {
			FLRBedarfsuebernahme b = (FLRBedarfsuebernahme) it.next();

			Object[] oZeile = new Object[BEDARFSUEBERNAHME_SYNCHRONISIERUNG_SPALTENANZAHL];

			BedarfsuebernahmeDto bedarfsuebernahmeDto = getFertigungFac()
					.bedarfsuebernahmeFindByPrimaryKey(b.getI_id());
			if (bedarfsuebernahmeDto.getOMedia() != null) {
				java.awt.Image myImage = Helper.byteArrayToImage(bedarfsuebernahmeDto.getOMedia());
				oZeile[BEDARFSUEBERNAHME_SYNCHRONISIERUNG_BILD] = myImage;
			}

			oZeile[BEDARFSUEBERNAHME_SYNCHRONISIERUNG_KOMMENTAR] = b.getC_kommentar();

			oZeile[BEDARFSUEBERNAHME_SYNCHRONISIERUNG_PERSON] = HelperServer
					.formatPersonAusFLRPartner(b.getFlrpersonal_anlegen().getFlrpartner());
			oZeile[BEDARFSUEBERNAHME_SYNCHRONISIERUNG_ABGANG] = Helper.short2boolean(b.getB_abgang());
			oZeile[BEDARFSUEBERNAHME_SYNCHRONISIERUNG_ZUSAETZLICH] = Helper.short2boolean(b.getB_zusaetzlich());
			oZeile[BEDARFSUEBERNAHME_SYNCHRONISIERUNG_ARTIKELNUMMER_MANUELL] = b.getC_artikelnummer();
			oZeile[BEDARFSUEBERNAHME_SYNCHRONISIERUNG_ARTIKELBEZEICHNUNG_MANUELL] = b.getC_artikelbezeichnung();
			if (b.getArtikel_i_id() != null) {
				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(b.getArtikel_i_id(), theClientDto);

				oZeile[BEDARFSUEBERNAHME_SYNCHRONISIERUNG_ARTIKELNUMMER_HV] = aDto.getCNr();
				oZeile[BEDARFSUEBERNAHME_SYNCHRONISIERUNG_ARTIKELBEZEICHNUNG_HV] = aDto.getCBezAusSpr();
			}
			oZeile[BEDARFSUEBERNAHME_SYNCHRONISIERUNG_LOSNUMMER_MANUELL] = b.getC_losnummer();

			if (b.getLos_i_id() != null) {
				oZeile[BEDARFSUEBERNAHME_SYNCHRONISIERUNG_LOSNUMMER_HV] = b.getFlrlos().getC_nr();

				oZeile[BEDARFSUEBERNAHME_SYNCHRONISIERUNG_LOS_PROJEKT] = b.getFlrlos().getC_projekt();
				oZeile[BEDARFSUEBERNAHME_SYNCHRONISIERUNG_LOS_STATUS] = b.getFlrlos().getStatus_c_nr();

				if (b.getFlrlos().getFlrauftrag() != null) {

					oZeile[BEDARFSUEBERNAHME_SYNCHRONISIERUNG_AUFTRAG] = b.getFlrlos().getFlrauftrag().getC_nr();

					oZeile[BEDARFSUEBERNAHME_SYNCHRONISIERUNG_KUNDE] = HelperServer
							.formatNameAusFLRPartner(b.getFlrlos().getFlrauftrag().getFlrkunde().getFlrpartner());
				} else {
					if (b.getFlrlos().getFlrkunde() != null) {
						oZeile[BEDARFSUEBERNAHME_SYNCHRONISIERUNG_KUNDE] = HelperServer
								.formatNameAusFLRPartner(b.getFlrlos().getFlrkunde().getFlrpartner());
					}
				}

				if (b.getFlrlos().getFlrstueckliste() != null) {
					oZeile[BEDARFSUEBERNAHME_SYNCHRONISIERUNG_STUECKLISTENNUMMER] = b.getFlrlos().getFlrstueckliste()
							.getFlrartikel().getC_nr();

					ArtikelDto aDtoStkl = getArtikelFac().artikelFindByPrimaryKeySmall(
							b.getFlrlos().getFlrstueckliste().getArtikel_i_id(), theClientDto);

					oZeile[BEDARFSUEBERNAHME_SYNCHRONISIERUNG_STUECKLISTENBEZEICHNUNG] = aDtoStkl.getCBezAusSpr();
				}

			}

			oZeile[BEDARFSUEBERNAHME_SYNCHRONISIERUNG_WUNSCHMENGE] = b.getN_wunschmenge();
			oZeile[BEDARFSUEBERNAHME_SYNCHRONISIERUNG_WUNSCHTERMIN] = b.getT_wunschtermin();

			alDaten.add(oZeile);

			if (bStatusAufOffenSetzen) {
				Bedarfsuebernahme bedarf = em.find(Bedarfsuebernahme.class, b.getI_id());
				bedarf.setStatusCNr(LocaleFac.STATUS_OFFEN);
				em.merge(bedarf);
				// em.flush();

			}

		}

		data = new Object[alDaten.size()][BEDARFSUEBERNAHME_SYNCHRONISIERUNG_SPALTENANZAHL];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL,
				FertigungReportFac.REPORT_BEDARFSUEBERNAHME_SYNCHRONISIERUNG, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	public JasperPrintLP printBedarfsuebernahmeBuchungsliste(boolean bStatusAufVerbuchtUndGedrucktSetzen,
			TheClientDto theClientDto) {

		this.useCase = UC_BEDARFSUEBERNAHME_BUCHUNGSLISTE;

		Map<String, Object> mapParameter = new TreeMap<String, Object>();

		ArrayList alDaten = new ArrayList();

		SessionFactory factory = FLRSessionFactory.getFactory();
		org.hibernate.Session session = factory.openSession();

		String queryString = "SELECT b FROM FLRBedarfsuebernahme b WHERE b.flrpersonal_anlegen.mandant_c_nr='"
				+ theClientDto.getMandant() + "' AND b.status_c_nr='" + LocaleFac.STATUS_VERBUCHT
				+ "' AND b.t_verbucht_gedruckt IS NULL";

		queryString += " ORDER BY b.flrpersonal_anlegen.c_kurzzeichen, b.t_anlegen ASC";

		org.hibernate.Query query = session.createQuery(queryString);

		List<?> resultList = query.list();
		Iterator it = resultList.iterator();
		while (it.hasNext()) {
			FLRBedarfsuebernahme b = (FLRBedarfsuebernahme) it.next();

			Object[] oZeile = new Object[BEDARFSUEBERNAHME_BUCHUNGSLISTE_SPALTENANZAHL];

			BedarfsuebernahmeDto bedarfsuebernahmeDto = getFertigungFac()
					.bedarfsuebernahmeFindByPrimaryKey(b.getI_id());
			if (bedarfsuebernahmeDto.getOMedia() != null) {
				java.awt.Image myImage = Helper.byteArrayToImage(bedarfsuebernahmeDto.getOMedia());
				oZeile[BEDARFSUEBERNAHME_BUCHUNGSLISTE_BILD] = myImage;
			}

			oZeile[BEDARFSUEBERNAHME_BUCHUNGSLISTE_KOMMENTAR] = b.getC_kommentar();

			oZeile[BEDARFSUEBERNAHME_BUCHUNGSLISTE_PERSON] = HelperServer
					.formatPersonAusFLRPartner(b.getFlrpersonal_anlegen().getFlrpartner());
			oZeile[BEDARFSUEBERNAHME_BUCHUNGSLISTE_ABGANG] = Helper.short2boolean(b.getB_abgang());
			oZeile[BEDARFSUEBERNAHME_BUCHUNGSLISTE_ZUSAETZLICH] = Helper.short2boolean(b.getB_zusaetzlich());
			oZeile[BEDARFSUEBERNAHME_BUCHUNGSLISTE_ARTIKELNUMMER_MANUELL] = b.getC_artikelnummer();
			oZeile[BEDARFSUEBERNAHME_BUCHUNGSLISTE_ARTIKELBEZEICHNUNG_MANUELL] = b.getC_artikelbezeichnung();
			if (b.getArtikel_i_id() != null) {
				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(b.getArtikel_i_id(), theClientDto);

				oZeile[BEDARFSUEBERNAHME_BUCHUNGSLISTE_ARTIKELNUMMER_HV] = aDto.getCNr();
				oZeile[BEDARFSUEBERNAHME_BUCHUNGSLISTE_ARTIKELBEZEICHNUNG_HV] = aDto.getCBezAusSpr();

				oZeile[BEDARFSUEBERNAHME_BUCHUNGSLISTE_ARTIKELEINHEIT] = aDto.getEinheitCNr();

			}
			oZeile[BEDARFSUEBERNAHME_BUCHUNGSLISTE_LOSNUMMER_MANUELL] = b.getC_losnummer();

			if (b.getLos_i_id() != null) {
				oZeile[BEDARFSUEBERNAHME_BUCHUNGSLISTE_LOSNUMMER_HV] = b.getFlrlos().getC_nr();

				oZeile[BEDARFSUEBERNAHME_BUCHUNGSLISTE_LOS_PROJEKT] = b.getFlrlos().getC_projekt();
				oZeile[BEDARFSUEBERNAHME_BUCHUNGSLISTE_LOS_STATUS] = b.getFlrlos().getStatus_c_nr();

				if (b.getFlrlos().getFlrauftrag() != null) {

					oZeile[BEDARFSUEBERNAHME_BUCHUNGSLISTE_AUFTRAG] = b.getFlrlos().getFlrauftrag().getC_nr();

					oZeile[BEDARFSUEBERNAHME_BUCHUNGSLISTE_KUNDE] = HelperServer
							.formatNameAusFLRPartner(b.getFlrlos().getFlrauftrag().getFlrkunde().getFlrpartner());
				} else {
					if (b.getFlrlos().getFlrkunde() != null) {
						oZeile[BEDARFSUEBERNAHME_BUCHUNGSLISTE_KUNDE] = HelperServer
								.formatNameAusFLRPartner(b.getFlrlos().getFlrkunde().getFlrpartner());
					}
				}

				if (b.getFlrlos().getFlrstueckliste() != null) {
					oZeile[BEDARFSUEBERNAHME_BUCHUNGSLISTE_STUECKLISTENNUMMER] = b.getFlrlos().getFlrstueckliste()
							.getFlrartikel().getC_nr();

					ArtikelDto aDtoStkl = getArtikelFac().artikelFindByPrimaryKeySmall(
							b.getFlrlos().getFlrstueckliste().getArtikel_i_id(), theClientDto);

					oZeile[BEDARFSUEBERNAHME_BUCHUNGSLISTE_STUECKLISTENBEZEICHNUNG] = aDtoStkl.getCBezAusSpr();
				}

			}

			oZeile[BEDARFSUEBERNAHME_BUCHUNGSLISTE_WUNSCHMENGE] = b.getN_wunschmenge();
			oZeile[BEDARFSUEBERNAHME_BUCHUNGSLISTE_WUNSCHTERMIN] = b.getT_wunschtermin();

			if (b.getFlrlossollmaterial() != null) {
				oZeile[BEDARFSUEBERNAHME_BUCHUNGSLISTE_MENGE_GENEHMIGT] = b.getFlrlossollmaterial().getN_menge();

				try {
					LosistmaterialDto[] istMatDtos = getFertigungFac()
							.losistmaterialFindByLossollmaterialIId(b.getFlrlossollmaterial().getI_id());
					if (istMatDtos != null && istMatDtos.length > 0) {
						BigDecimal bdMengeGebucht = BigDecimal.ZERO;
						for (int i = 0; i < istMatDtos.length; i++) {
							bdMengeGebucht = bdMengeGebucht.add(istMatDtos[i].getNMenge());
						}
						oZeile[BEDARFSUEBERNAHME_BUCHUNGSLISTE_MENGE_GEBUCHT] = bdMengeGebucht;
						oZeile[BEDARFSUEBERNAHME_BUCHUNGSLISTE_LAGER] = getLagerFac()
								.lagerFindByPrimaryKey(istMatDtos[0].getLagerIId()).getCNr();

					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}

			alDaten.add(oZeile);

			if (bStatusAufVerbuchtUndGedrucktSetzen) {
				Bedarfsuebernahme bedarf = em.find(Bedarfsuebernahme.class, b.getI_id());
				bedarf.setTVerbuchtGedruckt(getTimestamp());
				bedarf.setPersonalIIdVerbuchtGedruckt(theClientDto.getIDPersonal());
				em.merge(bedarf);
				em.flush();

			}

		}

		data = new Object[alDaten.size()][BEDARFSUEBERNAHME_BUCHUNGSLISTE_SPALTENANZAHL];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL,
				FertigungReportFac.REPORT_BEDARFSUEBERNAHME_BUCHUNGSLISTE, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	public JasperPrintLP printBedarfsuebernahmeOffene(Integer personalIId, Integer losIId, boolean bNurRueckgaben,
			TheClientDto theClientDto) {

		this.useCase = UC_BEDARFSUEBERNAHME_OFFENE;

		Map<String, Object> mapParameter = new TreeMap<String, Object>();

		ArrayList alDaten = new ArrayList();

		SessionFactory factory = FLRSessionFactory.getFactory();
		org.hibernate.Session session = factory.openSession();

		String queryString = "SELECT b FROM FLRBedarfsuebernahme b WHERE b.flrpersonal_anlegen.mandant_c_nr='"
				+ theClientDto.getMandant() + "' AND b.status_c_nr='" + LocaleFac.STATUS_OFFEN + "' ";

		if (personalIId != null) {
			PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(personalIId, theClientDto);
			mapParameter.put("P_PERSON", personalDto.formatFixUFTitelName2Name1());

			queryString += " AND b.personal_i_id_anlegen=" + personalIId;
		}

		if (losIId != null) {
			try {
				LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);
				mapParameter.put("P_LOS", losDto.getCNr());

				queryString += " AND b.los_i_id=" + losIId;

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}
		mapParameter.put("P_NUR_RUECKGABEN", Boolean.FALSE);
		if (bNurRueckgaben) {
			queryString += " AND b.b_abgang=0";
			mapParameter.put("P_NUR_RUECKGABEN", Boolean.TRUE);
		}

		queryString += " ORDER BY b.flrpersonal_anlegen.c_kurzzeichen, b.t_anlegen ASC";

		org.hibernate.Query query = session.createQuery(queryString);

		List<?> resultList = query.list();
		Iterator it = resultList.iterator();
		while (it.hasNext()) {
			FLRBedarfsuebernahme b = (FLRBedarfsuebernahme) it.next();

			Object[] oZeile = new Object[BEDARFSUEBERNAHME_OFFENE_SPALTENANZAHL];

			BedarfsuebernahmeDto bedarfsuebernahmeDto = getFertigungFac()
					.bedarfsuebernahmeFindByPrimaryKey(b.getI_id());
			if (bedarfsuebernahmeDto.getOMedia() != null) {
				java.awt.Image myImage = Helper.byteArrayToImage(bedarfsuebernahmeDto.getOMedia());
				oZeile[BEDARFSUEBERNAHME_OFFENE_BILD] = myImage;
			}

			oZeile[BEDARFSUEBERNAHME_OFFENE_KOMMENTAR] = b.getC_kommentar();

			oZeile[BEDARFSUEBERNAHME_OFFENE_PERSON] = HelperServer
					.formatPersonAusFLRPartner(b.getFlrpersonal_anlegen().getFlrpartner());
			oZeile[BEDARFSUEBERNAHME_OFFENE_ABGANG] = Helper.short2boolean(b.getB_abgang());
			oZeile[BEDARFSUEBERNAHME_OFFENE_ZUSAETZLICH] = Helper.short2boolean(b.getB_zusaetzlich());
			oZeile[BEDARFSUEBERNAHME_OFFENE_ARTIKELNUMMER_MANUELL] = b.getC_artikelnummer();
			oZeile[BEDARFSUEBERNAHME_OFFENE_ARTIKELBEZEICHNUNG_MANUELL] = b.getC_artikelbezeichnung();
			if (b.getArtikel_i_id() != null) {
				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(b.getArtikel_i_id(), theClientDto);

				oZeile[BEDARFSUEBERNAHME_OFFENE_ARTIKELNUMMER_HV] = aDto.getCNr();
				oZeile[BEDARFSUEBERNAHME_OFFENE_ARTIKELBEZEICHNUNG_HV] = aDto.getCBezAusSpr();
				oZeile[BEDARFSUEBERNAHME_OFFENE_ARTIKELEINHEIT] = aDto.getEinheitCNr();
			}
			oZeile[BEDARFSUEBERNAHME_OFFENE_LOSNUMMER_MANUELL] = b.getC_losnummer();

			if (b.getLos_i_id() != null) {
				oZeile[BEDARFSUEBERNAHME_OFFENE_LOSNUMMER_HV] = b.getFlrlos().getC_nr();

				oZeile[BEDARFSUEBERNAHME_OFFENE_LOS_PROJEKT] = b.getFlrlos().getC_projekt();
				oZeile[BEDARFSUEBERNAHME_OFFENE_LOS_STATUS] = b.getFlrlos().getStatus_c_nr();

				if (b.getFlrlos().getFlrauftrag() != null) {

					oZeile[BEDARFSUEBERNAHME_OFFENE_AUFTRAG] = b.getFlrlos().getFlrauftrag().getC_nr();

					oZeile[BEDARFSUEBERNAHME_OFFENE_KUNDE] = HelperServer
							.formatNameAusFLRPartner(b.getFlrlos().getFlrauftrag().getFlrkunde().getFlrpartner());
				} else {
					if (b.getFlrlos().getFlrkunde() != null) {
						oZeile[BEDARFSUEBERNAHME_OFFENE_KUNDE] = HelperServer
								.formatNameAusFLRPartner(b.getFlrlos().getFlrkunde().getFlrpartner());
					}
				}

				if (b.getFlrlos().getFlrstueckliste() != null) {
					oZeile[BEDARFSUEBERNAHME_OFFENE_STUECKLISTENNUMMER] = b.getFlrlos().getFlrstueckliste()
							.getFlrartikel().getC_nr();

					ArtikelDto aDtoStkl = getArtikelFac().artikelFindByPrimaryKeySmall(
							b.getFlrlos().getFlrstueckliste().getArtikel_i_id(), theClientDto);

					oZeile[BEDARFSUEBERNAHME_OFFENE_STUECKLISTENBEZEICHNUNG] = aDtoStkl.getCBezAusSpr();
				}

				try {
					if (b.getFlrartikel() != null
							&& Helper.short2Boolean(b.getFlrartikel().getB_lagerbewirtschaftet())) {
						LoslagerentnahmeDto[] laeger = getFertigungFac().loslagerentnahmeFindByLosIId(b.getLos_i_id());

						BigDecimal bdLagerstandGesamtAufLosLaegern = BigDecimal.ZERO;
						for (int j = 0; j < laeger.length; j++) {

							bdLagerstandGesamtAufLosLaegern = bdLagerstandGesamtAufLosLaegern.add(getLagerFac()
									.getLagerstand(b.getFlrartikel().getI_id(), laeger[j].getLagerIId(), theClientDto));
						}
						oZeile[BEDARFSUEBERNAHME_OFFENE_LAGERSTAND_LOSLAEGER] = bdLagerstandGesamtAufLosLaegern;
					}

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

			oZeile[BEDARFSUEBERNAHME_OFFENE_WUNSCHMENGE] = b.getN_wunschmenge();
			oZeile[BEDARFSUEBERNAHME_OFFENE_WUNSCHTERMIN] = b.getT_wunschtermin();

			if (b.getFlrlossollmaterial() != null) {
				oZeile[BEDARFSUEBERNAHME_OFFENE_MENGE_GENEHMIGT] = b.getFlrlossollmaterial().getN_menge();
				try {
					LosistmaterialDto[] istMatDtos = getFertigungFac()
							.losistmaterialFindByLossollmaterialIId(b.getFlrlossollmaterial().getI_id());
					if (istMatDtos != null && istMatDtos.length > 0) {
						oZeile[BEDARFSUEBERNAHME_OFFENE_MENGE_GEBUCHT] = istMatDtos[0].getNMenge();
						oZeile[BEDARFSUEBERNAHME_OFFENE_LAGER] = getLagerFac()
								.lagerFindByPrimaryKey(istMatDtos[0].getLagerIId()).getCNr();

					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}

			alDaten.add(oZeile);

		}

		data = new Object[alDaten.size()][BEDARFSUEBERNAHME_OFFENE_SPALTENANZAHL];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_BEDARFSUEBERNAHME_OFFENE,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printGesamtkalkulation(Integer losIId, int iBisEbene, TheClientDto theClientDto) {
		try {

			this.useCase = UC_GESAMTKALKULATION;
			LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);

			Map<String, Object> mapParameter = new TreeMap<String, Object>();

			mapParameter.put("P_BIS_EBENE", iBisEbene);

			mapParameter.put("P_LOSNUMMER", losDto.getCNr());

			mapParameter.put("P_LOSGROESSE", losDto.getNLosgroesse());
			BigDecimal bdAbgeliefert = getFertigungFac().getErledigteMenge(losIId, theClientDto);

			mapParameter.put("P_ABGELIEFERT", bdAbgeliefert);

			mapParameter.put("P_PRODUKTIONSBEGINN", losDto.getTProduktionsbeginn());
			mapParameter.put("P_PRODUKTIONSENDE", losDto.getTProduktionsende());

			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stkDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(),
						theClientDto);
				mapParameter.put("P_STUECKLISTENUMMER", stkDto.getArtikelDto().getCNr());
				mapParameter.put("P_STUECKLISTEBEZEICHNUNG", stkDto.getArtikelDto().getArtikelsprDto().getCBez());

			}

			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_ARTIKELNUMMER);

			Integer iMaxLaenge = (Integer) parameter.getCWertAsObject();
			mapParameter.put("P_MAX_LAENGE_ARTIKELNUMMER_OHNE_HERSTELLER", iMaxLaenge);

			String sAuftragsnummer = null;
			String sKunde = null;
			if (losDto.getAuftragIId() != null) {
				// Auftrag holen
				AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(losDto.getAuftragIId());
				sAuftragsnummer = auftragDto.getCNr();

				// Kunde holen
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(),
						theClientDto);

				sKunde = kundeDto.getPartnerDto().getCName1nachnamefirmazeile1();

				if (auftragDto.getProjektIId() != null) {
					mapParameter.put("P_PROJEKTNUMMER",
							getProjektFac().projektFindByPrimaryKey(auftragDto.getProjektIId()).getCNr());
				}

			} else {
				sAuftragsnummer = "";

				if (losDto.getKundeIId() != null) {
					KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(losDto.getKundeIId(), theClientDto);

					sKunde = kundeDto.getPartnerDto().getCName1nachnamefirmazeile1();
				} else {
					sKunde = "";
				}

			}

			mapParameter.put("P_AUFTRAGSNUMMER", sAuftragsnummer);
			mapParameter.put("P_KUNDE", sKunde);

			mapParameter.put("P_PROJEKT", losDto.getCProjekt());
			mapParameter.put("P_KOMMENTAR", losDto.getCKommentar());
			mapParameter.put("P_TEXT", losDto.getXText());

			GesamtkalkulationDto gkDto = new GesamtkalkulationDto();

			gkDto = getDatenGesamtkalkulation(losIId, null, iBisEbene, theClientDto);

			TreeMap<Integer, HashSet<Integer>> lose = getFertigungFac().getBetroffeneLoseEinesLoses(losIId, false,
					theClientDto);

			data = new Object[gkDto.getAlDaten().size()][GESAMTKALKULATION_ANZAHL_SPALTEN];
			data = (Object[][]) gkDto.getAlDaten().toArray(data);

			initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_GESAMTALKULATION,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return getReportPrint();

	}

	public JasperPrintLP printNachkalkulation(Integer losIId, TheClientDto theClientDto) {

		this.useCase = UC_NACHKALKULATION;

		try {
			int iAnzahlSpalten = NACHKALKULATION_ANZAHL_SPALTEN;
			int iAnzahlZeilen = 50;
			ReportLosnachkalkulationDto mat = getFertigungReportFac().getDataNachkalkulationMaterial(losIId,
					theClientDto);

			ReportLosnachkalkulationDto[] zeit = getFertigungReportFac().getDataNachkalkulationZeitdaten(losIId,
					theClientDto);
			data = new Object[iAnzahlZeilen][iAnzahlSpalten];

			int row = 0;
			ReportLosnachkalkulationDto nkZeitSumme = new ReportLosnachkalkulationDto();
			nkZeitSumme.setSBezeichnung(
					getTextRespectUISpr("lp.summe", theClientDto.getMandant(), theClientDto.getLocUi()));

			for (int i = 0; i < zeit.length; i++) {
				data[row][NACHKALKULATION_ARTIKELNUMMER] = zeit[i].getSArtikelnummer();
				data[row][NACHKALKULATION_ARTIKELBEZEICHNUNG] = zeit[i].getSBezeichnung();
				data[row][NACHKALKULATION_PERSONALSOLL] = zeit[i].getBdSollmenge();
				data[row][NACHKALKULATION_PERSONALIST] = zeit[i].getBdIstmenge();
				data[row][NACHKALKULATION_PERSONALDIFF] = zeit[i].getBdSollmenge().subtract(zeit[i].getBdIstmenge());
				data[row][NACHKALKULATION_MASCHINESOLL] = zeit[i].getBdSollmengeMaschine();
				data[row][NACHKALKULATION_MASCHINEIST] = zeit[i].getBdIstmengeMaschine();
				data[row][NACHKALKULATION_MASCHINEDIFF] = zeit[i].getBdSollmengeMaschine()
						.subtract(zeit[i].getBdIstmengeMaschine());
				data[row][NACHKALKULATION_GESAMTSOLL] = zeit[i].getBdSollpreis();
				data[row][NACHKALKULATION_GESAMTIST] = zeit[i].getBdIstpreis();
				data[row][NACHKALKULATION_FERTIG] = zeit[i].getBFertig();
				data[row][NACHKALKULATION_HOECHSTER_ARBEITSGANG] = zeit[i].getIHoechsterArbeitsgang();
				// Summe
				nkZeitSumme.addiereZuSollmenge(zeit[i].getBdSollmenge());
				nkZeitSumme.addiereZuIstmenge(zeit[i].getBdIstmenge());
				nkZeitSumme.addiereZuIstmengeMaschine(zeit[i].getBdIstmengeMaschine());
				nkZeitSumme.addiereZuSollpreis(zeit[i].getBdSollpreis());
				nkZeitSumme.addiereZuIstpreis(zeit[i].getBdIstpreis());
				nkZeitSumme.addiereZuSollmengeMaschine(zeit[i].getBdSollmengeMaschine());
				row++;
			}
			data[row][NACHKALKULATION_ARTIKELBEZEICHNUNG] = nkZeitSumme.getSBezeichnung();
			data[row][NACHKALKULATION_PERSONALSOLL] = nkZeitSumme.getBdSollmenge();
			data[row][NACHKALKULATION_PERSONALIST] = nkZeitSumme.getBdIstmenge();
			data[row][NACHKALKULATION_PERSONALDIFF] = nkZeitSumme.getBdSollmenge()
					.subtract(nkZeitSumme.getBdIstmenge());
			data[row][NACHKALKULATION_MASCHINESOLL] = nkZeitSumme.getBdSollmengeMaschine();
			data[row][NACHKALKULATION_MASCHINEIST] = nkZeitSumme.getBdIstmengeMaschine();
			data[row][NACHKALKULATION_MASCHINEDIFF] = nkZeitSumme.getBdSollmengeMaschine()
					.subtract(nkZeitSumme.getBdIstmengeMaschine());
			data[row][NACHKALKULATION_GESAMTSOLL] = nkZeitSumme.getBdSollpreis();
			data[row][NACHKALKULATION_GESAMTIST] = nkZeitSumme.getBdIstpreis();
			row++;
			row++;
			ReportLosnachkalkulationDto nkGesamtSumme = new ReportLosnachkalkulationDto();
			nkGesamtSumme.setSBezeichnung(
					getTextRespectUISpr("lp.gesamtsumme", theClientDto.getMandant(), theClientDto.getLocUi()));
			nkGesamtSumme.addiereZuSollpreis(nkZeitSumme.getBdSollpreis());
			nkGesamtSumme.addiereZuIstpreis(nkZeitSumme.getBdIstpreis());
			nkGesamtSumme.addiereZuSollpreis(mat.getBdSollpreis());
			nkGesamtSumme.addiereZuIstpreis(mat.getBdIstpreis());
			data[row][NACHKALKULATION_ARTIKELBEZEICHNUNG] = mat.getSBezeichnung();
			data[row][NACHKALKULATION_PERSONALSOLL] = mat.getBdSollmenge();
			data[row][NACHKALKULATION_PERSONALIST] = mat.getBdIstmenge();
			data[row][NACHKALKULATION_PERSONALDIFF] = mat.getBdSollmenge().subtract(mat.getBdIstmenge());
			data[row][NACHKALKULATION_MASCHINESOLL] = mat.getBdSollmengeMaschine();
			data[row][NACHKALKULATION_MASCHINEIST] = mat.getBdIstmengeMaschine();
			data[row][NACHKALKULATION_MASCHINEDIFF] = mat.getBdSollmengeMaschine()
					.subtract(mat.getBdIstmengeMaschine());
			data[row][NACHKALKULATION_GESAMTSOLL] = mat.getBdSollpreis();
			data[row][NACHKALKULATION_GESAMTIST] = mat.getBdIstpreis();
			row++;
			row++;
			data[row][NACHKALKULATION_ARTIKELBEZEICHNUNG] = nkGesamtSumme.getSBezeichnung();
			data[row][NACHKALKULATION_PERSONALSOLL] = nkGesamtSumme.getBdSollmenge();
			data[row][NACHKALKULATION_PERSONALIST] = nkGesamtSumme.getBdIstmenge();
			data[row][NACHKALKULATION_PERSONALDIFF] = nkGesamtSumme.getBdSollmengeMaschine()
					.subtract(nkGesamtSumme.getBdIstmengeMaschine());
			data[row][NACHKALKULATION_MASCHINESOLL] = nkGesamtSumme.getBdSollmengeMaschine();
			data[row][NACHKALKULATION_MASCHINEIST] = nkGesamtSumme.getBdIstmengeMaschine();
			data[row][NACHKALKULATION_MASCHINEDIFF] = nkGesamtSumme.getBdSollmenge()
					.subtract(nkGesamtSumme.getBdIstmenge());
			data[row][NACHKALKULATION_GESAMTSOLL] = nkGesamtSumme.getBdSollpreis();
			data[row][NACHKALKULATION_GESAMTIST] = nkGesamtSumme.getBdIstpreis();
			row++;

			LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);

			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			mapParameter.put("P_ANGELEGT", new java.util.Date(losDto.getTAnlegen().getTime()));
			String sAuftragsnummer;
			String sInternerKommentar = null;
			String sKunde;
			String sKundeLieferadresse;
			Timestamp dLiefertermin;
			String sLieferart;
			String sAbteilung = null;
			String sSpediteur = null;
			Boolean bPoenale = Boolean.FALSE;
			Boolean bRoHs = Boolean.FALSE;

			Integer kundeIId = null;

			if (losDto.getAuftragIId() != null) {
				// Auftrag holen
				AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(losDto.getAuftragIId());
				kundeIId = auftragDto.getKundeIIdAuftragsadresse();
				sAuftragsnummer = auftragDto.getCNr();
				sLieferart = getLocaleFac().lieferartFindByIIdLocaleOhneExc(auftragDto.getLieferartIId(),
						theClientDto.getLocUi(), theClientDto);

				if (auftragDto.getSpediteurIId() != null) {
					sSpediteur = getMandantFac().spediteurFindByPrimaryKey(auftragDto.getSpediteurIId())
							.getCNamedesspediteurs();
				}

				bPoenale = Helper.short2Boolean(auftragDto.getBPoenale());
				bRoHs = Helper.short2Boolean(auftragDto.getBRoHs());
				// Internen Kommentar aus dem Auftrag, abhaengig von einem
				// Mandantenparameter
				ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_KOMMENTAR_AM_FERTIGUNGSBEGLEITSCHEIN);
				short iValue = Short.parseShort(parameter.getCWert());
				boolean bDruckeKommentar = Helper.short2boolean(iValue);
				if (bDruckeKommentar) {
					sInternerKommentar = auftragDto.getXInternerkommentar();
				}
				// Kunde holen
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(),
						theClientDto);

				sKunde = kundeDto.getPartnerDto().getCName1nachnamefirmazeile1();

				KundeDto kundeDtoLieferadresse = getKundeFac()
						.kundeFindByPrimaryKey(auftragDto.getKundeIIdLieferadresse(), theClientDto);
				sKundeLieferadresse = kundeDtoLieferadresse.getPartnerDto().getCName1nachnamefirmazeile1();

				sAbteilung = kundeDto.getPartnerDto().getCName3vorname2abteilung();

				if (losDto.getAuftragpositionIId() != null) {
					AuftragpositionDto aufposDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(losDto.getAuftragpositionIId());
					if (aufposDto.getTUebersteuerbarerLiefertermin() != null) {
						dLiefertermin = aufposDto.getTUebersteuerbarerLiefertermin();
					} else {
						dLiefertermin = auftragDto.getDLiefertermin();
					}
				} else {
					dLiefertermin = auftragDto.getDLiefertermin();
				}
			} else {
				sAuftragsnummer = "";
				sKunde = "";
				sKundeLieferadresse = "";
				dLiefertermin = null;
				sLieferart = "";
				sSpediteur = "";
			}

			BigDecimal vkPreisAuftragsposition = null;
			BigDecimal vkPreisbasisStueckliste = null;
			BigDecimal vkPreisStueckliste = null;
			if (kundeIId == null) {
				kundeIId = losDto.getKundeIId();
			}

			if (losDto.getAuftragpositionIId() != null) {
				AuftragpositionDto auftragpositionDto = getAuftragpositionFac()
						.auftragpositionFindByPrimaryKey(losDto.getAuftragpositionIId());
				vkPreisAuftragsposition = auftragpositionDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte();
			}

			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stkDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(),
						theClientDto);

				VkPreisfindungEinzelverkaufspreisDto vkpreisDto = getVkPreisfindungFac().getArtikeleinzelverkaufspreis(
						stkDto.getArtikelIId(), losDto.getTProduktionsende(), theClientDto.getSMandantenwaehrung(),
						theClientDto);
				if (vkpreisDto != null && vkpreisDto.getNVerkaufspreisbasis() != null) {
					vkPreisbasisStueckliste = vkpreisDto.getNVerkaufspreisbasis();
				}

				if (kundeIId != null) {

					KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto);

//					MwstsatzDto mwstsatzDtoAktuell = getMandantFac()
//							.mwstsatzFindByMwstsatzbezIIdAktuellster(kundeDto.getMwstsatzbezIId(), theClientDto);
					Timestamp belegDatum = Helper.cutTimestamp(new Timestamp(losDto.getTProduktionsende().getTime()));
					MwstsatzDto mwstsatzDtoAktuell = getMandantFac()
							.mwstsatzZuDatumValidate(kundeDto.getMwstsatzbezIId(), belegDatum, theClientDto);

					VkpreisfindungDto vkpreisfindungDto = getVkPreisfindungFac().verkaufspreisfindung(
							stkDto.getArtikelIId(), kundeIId, losDto.getNLosgroesse(), losDto.getTProduktionsende(),
							kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(), mwstsatzDtoAktuell.getIId(),
							theClientDto.getSMandantenwaehrung(), theClientDto);

					VerkaufspreisDto kundenVKPreisDto = Helper.getVkpreisBerechnet(vkpreisfindungDto);
					if (kundenVKPreisDto != null) {
						vkPreisStueckliste = kundenVKPreisDto.nettopreis;
					}
				}

			}

			mapParameter.put("P_VKPREIS_AUFTRAGSPOSITION", vkPreisAuftragsposition);
			mapParameter.put("P_VKPREISBASIS_STUECKLISTE", vkPreisbasisStueckliste);
			mapParameter.put("P_VKPREIS_STUECKLISTE", vkPreisStueckliste);

			mapParameter.put("P_AUFTRAGNUMMER", sAuftragsnummer);
			mapParameter.put("P_AUFTRAG_INTERNERKOMMENTAR", sInternerKommentar);
			mapParameter.put("P_AUFTRAG_KUNDE_ABTEILUNG", sAbteilung);
			mapParameter.put("P_LIEFERART", sLieferart);
			mapParameter.put("P_SPEDITEUR", sSpediteur);
			mapParameter.put("P_POENALE", bPoenale);
			mapParameter.put("P_ROHS", bRoHs);
			KostenstelleDto kstDto = getSystemFac().kostenstelleFindByPrimaryKey(losDto.getKostenstelleIId());
			mapParameter.put("P_KOSTENSTELLENUMMER", kstDto.getCNr());
			mapParameter.put("P_KUNDE", sKunde);
			mapParameter.put("P_KUNDE_LIEFERADRESSE", sKundeLieferadresse);
			mapParameter.put("P_LOSGROESSE", losDto.getNLosgroesse());

			BigDecimal abgeliefert = BigDecimal.ZERO;

			LosablieferungDto[] losablieferungDtos = getFertigungFac().losablieferungFindByLosIId(losDto.getIId(),
					false, theClientDto);
			BigDecimal bdAbgeliefert = new BigDecimal(0.0000);
			for (int j = 0; j < losablieferungDtos.length; j++) {
				abgeliefert = abgeliefert.add(losablieferungDtos[j].getNMenge());
			}

			mapParameter.put("P_ABGELIEFERTEMENGE", abgeliefert);

			mapParameter.put("P_LOSNUMMER", losDto.getCNr());
			mapParameter.put("P_PRODUKTIONSBEGINN", losDto.getTProduktionsbeginn());
			mapParameter.put("P_PRODUKTIONSENDE", losDto.getTProduktionsende());
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(losDto.getTProduktionsende());
			mapParameter.put("P_PRODUKTIONSENDE_KW", new Integer(gc.get(Calendar.WEEK_OF_YEAR)));

			mapParameter.put("P_LIEFERTERMIN", dLiefertermin);

			mapParameter.put("P_PROJEKT", losDto.getCProjekt());
			mapParameter.put("P_LOSKOMMENTAR", losDto.getCKommentar());
			mapParameter.put("P_LOSLANGTEXT", losDto.getXText());

			FertigungsgruppeDto fertGruppeDto = getStuecklisteFac()
					.fertigungsgruppeFindByPrimaryKey(losDto.getFertigungsgruppeIId());
			mapParameter.put("P_FERTIGUNGSGRUPPE", fertGruppeDto.getCBez());

			// Materialliste?
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stkDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(),
						theClientDto);

				mapParameter.put("P_MENGENEINHEIT", stkDto.getArtikelDto().getEinheitCNr());
				mapParameter.put("P_MINDESTDECKUNGSBEITRAG", stkDto.getArtikelDto().getFMindestdeckungsbeitrag());
				mapParameter.put("P_STUECKLISTEBEZEICHNUNG", stkDto.getArtikelDto().getArtikelsprDto().getCBez());

				mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG",
						stkDto.getArtikelDto().getArtikelsprDto().getCZbez());
				mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG2",
						stkDto.getArtikelDto().getArtikelsprDto().getCZbez2());

				mapParameter.put("P_STUECKLISTEKURZBEZEICHNUNG", stkDto.getArtikelDto().getArtikelsprDto().getCKbez());
				mapParameter.put("P_STUECKLISTEREFERENZNUMMER", stkDto.getArtikelDto().getCReferenznr());

				mapParameter.put("P_STUECKLISTENUMMER", stkDto.getArtikelDto().getCNr());

				mapParameter.put("P_STUECKLISTE_GEWICHTKG", stkDto.getArtikelDto().getFGewichtkg());

				if (stkDto.getArtikelDto().getVerpackungDto() != null) {
					mapParameter.put("P_STUECKLISTE_BAUFORM", stkDto.getArtikelDto().getVerpackungDto().getCBauform());
					mapParameter.put("P_STUECKLISTE_VERPACKUNGSART",
							stkDto.getArtikelDto().getVerpackungDto().getCVerpackungsart());
				}

				if (stkDto.getArtikelDto().getGeometrieDto() != null) {
					mapParameter.put("P_STUECKLISTE_BREITETEXT",
							stkDto.getArtikelDto().getGeometrieDto().getCBreitetext());
					mapParameter.put("P_STUECKLISTE_BREITE", stkDto.getArtikelDto().getGeometrieDto().getFBreite());
					mapParameter.put("P_STUECKLISTE_HOEHE", stkDto.getArtikelDto().getGeometrieDto().getFHoehe());
					mapParameter.put("P_STUECKLISTE_TIEFE", stkDto.getArtikelDto().getGeometrieDto().getFTiefe());
				}

				// Stuecklisteneigenschaften
				StuecklisteeigenschaftDto[] stuecklisteeigenschaftDtos = getStuecklisteFac()
						.stuecklisteeigenschaftFindByStuecklisteIId(losDto.getStuecklisteIId());
				ArrayList<Object[]> al = new ArrayList<Object[]>();
				for (int j = 0; j < stuecklisteeigenschaftDtos.length; j++) {
					StuecklisteeigenschaftDto dto = stuecklisteeigenschaftDtos[j];

					Object[] o = new Object[2];
					String sStklEigenschaftArt = dto.getStuecklisteeigenschaftartDto().getCBez();
					o[0] = sStklEigenschaftArt;
					o[1] = dto.getCBez();
					al.add(o);

					// Index und Materialplatz auch einzeln an Report uebergeben
					if (sStklEigenschaftArt.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_INDEX)) {
						mapParameter.put(P_STUECKLISTENEIGENSCHAFT_INDEX, dto.getCBez());
					}
					if (sStklEigenschaftArt
							.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_MATERIALPLATZ)) {
						mapParameter.put(P_STUECKLISTENEIGENSCHAFT_MATERIALPLATZ, dto.getCBez());
					}

				}

				if (stuecklisteeigenschaftDtos.length > 0) {
					String[] fieldnames = new String[] { "F_EIGENSCHAFTART", "F_BEZEICHNUNG" };
					Object[][] dataSub = new Object[al.size()][fieldnames.length];
					dataSub = (Object[][]) al.toArray(dataSub);

					mapParameter.put("DATENSUBREPORT", new LPDatenSubreport(dataSub, fieldnames));
				}

			} else {
				mapParameter.put("P_STUECKLISTEBEZEICHNUNG", losDto.getCProjekt());
				mapParameter.put("P_STUECKLISTENUMMER",
						getTextRespectUISpr("fert.materialliste", theClientDto.getMandant(), theClientDto.getLocUi()));
			}

			// PJ21733

			Timestamp tZeitpunkt = new Timestamp(System.currentTimeMillis());
			if (losablieferungDtos.length > 0) {
				tZeitpunkt = losablieferungDtos[losablieferungDtos.length - 1].getTAendern();
			}

			ParametermandantDto parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.MATERIALGEMEINKOSTENFAKTOR, tZeitpunkt);

			mapParameter.put("P_MATERIALGEMEINKOSTENPROZENT", parameter.getCWertAsObject());

			parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.FERTIGUNGSGEMEINKOSTENFAKTOR, tZeitpunkt);

			mapParameter.put("P_FERTIGUNGSGEMEINKOSTENPROZENT", parameter.getCWertAsObject());

			parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.VERWALTUNGSGEMEINKOSTENFAKTOR, tZeitpunkt);

			mapParameter.put("P_VERWALTUNGSGEMEINKOSTENPROZENT", parameter.getCWertAsObject());

			parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.ENTWICKLUNGSGEMEINKOSTENFAKTOR, tZeitpunkt);
			mapParameter.put("P_ENTWICKLUNGSGEMEINKOSTENPROZENT", parameter.getCWertAsObject());

			parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.VERTRIEBSGEMEINKOSTENFAKTOR, tZeitpunkt);
			mapParameter.put("P_VERTRIEBSGEMEINKOSTENPROZENT", parameter.getCWertAsObject());

			parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.ARBEITSZEITGEMEINKOSTENFAKTOR, tZeitpunkt);
			mapParameter.put("P_ARBEITSZEITGEMEINKOSTENPROZENT", parameter.getCWertAsObject());

			// PJ17759
			LosgutschlechtDto[] lgsDtos = getFertigungFac().losgutschlechtFindAllFehler(losIId);
			Object[][] oSubData = new Object[lgsDtos.length][12];

			for (int i = 0; i < lgsDtos.length; i++) {
				LossollarbeitsplanDto sollDto = getFertigungFac()
						.lossollarbeitsplanFindByPrimaryKey(lgsDtos[i].getLossollarbeitsplanIId());

				oSubData[i][0] = sollDto.getIArbeitsgangnummer();
				oSubData[i][1] = sollDto.getIUnterarbeitsgang();
				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(sollDto.getArtikelIIdTaetigkeit(),
						theClientDto);
				oSubData[i][2] = artikelDto.getCNr();
				oSubData[i][3] = artikelDto.formatBezeichnung();

				String person = "";
				Timestamp zeitpunkt = null;
				if (lgsDtos[i].getZeitdatenIId() != null) {
					ZeitdatenDto zDto = getZeiterfassungFac().zeitdatenFindByPrimaryKey(lgsDtos[i].getZeitdatenIId(),
							theClientDto);
					zeitpunkt = zDto.getTZeit();

					PersonalDto pDto = getPersonalFac().personalFindByPrimaryKey(zDto.getPersonalIId(), theClientDto);

					person = pDto.formatFixName1Name2();

				} else if (lgsDtos[i].getMaschinenzeitdatenIId() != null) {
					MaschinenzeitdatenDto mzDto = getZeiterfassungFac()
							.maschinenzeitdatenFindByPrimaryKey(lgsDtos[i].getMaschinenzeitdatenIId());
					person = getZeiterfassungFac().maschineFindByPrimaryKey(mzDto.getMaschineIId()).getCBez();
					zeitpunkt = mzDto.getTVon();
				}

				FehlerDto fDto = getReklamationFac().fehlerFindByPrimaryKey(lgsDtos[i].getFehlerIId(), theClientDto);

				oSubData[i][4] = fDto.getCBez();

				if (fDto.getFehlersprDto() != null) {
					oSubData[i][5] = fDto.getFehlersprDto().getCBez();
				}

				oSubData[i][6] = lgsDtos[i].getCKommentar();

				oSubData[i][7] = person;
				oSubData[i][8] = zeitpunkt;
				oSubData[i][9] = lgsDtos[i].getNGut();
				oSubData[i][10] = lgsDtos[i].getNSchlecht();
				oSubData[i][11] = lgsDtos[i].getNInarbeit();

			}

			String[] fieldnames = new String[] { "F_AGNUMMER", "F_UAGNUMMER", "F_ARTIKEL", "F_BEZEICHNUNG", "F_FEHLER",
					"F_FEHLERSPR", "F_FEHLER_KOMMENTAR", "F_PERSONMASCHINE", "F_ZEITPUNKT", "F_GUT", "F_SCHLECHT",
					"F_INARBEIT" };

			mapParameter.put("P_SUBREPORT_FEHLER", new LPDatenSubreport(oSubData, fieldnames));
			initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_NACHKALKULATION,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return getReportPrint();
	}

	private BigDecimal getSummeAbbuchungenInDerVergangenheit(Integer artikelIId, int iVonMonat, int iBisMonat,
			boolean bMitHandlagerbewegungen) {

		Calendar cVon = Calendar.getInstance();
		cVon.add(Calendar.MONTH, -iVonMonat);

		java.sql.Date dBis = null;

		Calendar cBis = Calendar.getInstance();
		cBis.add(Calendar.MONTH, -iBisMonat);

		dBis = new java.sql.Date(cBis.getTime().getTime());

		BigDecimal bdAnzahl = BigDecimal.ZERO;

		SessionFactory factory = FLRSessionFactory.getFactory();
		org.hibernate.Session session = factory.openSession();

		String queryString = "SELECT sum(l.n_menge) FROM FLRLagerbewegung l WHERE l.flrartikel.i_id=" + artikelIId
				+ " AND l.b_abgang=1 AND l.b_historie=0 AND l.t_belegdatum>='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(cVon.getTime().getTime())) + "' AND l.t_belegdatum<'"
				+ Helper.formatDateWithSlashes(dBis) + "'";

		if (bMitHandlagerbewegungen == false) {
			queryString += " AND l.c_belegartnr NOT IN ('" + LocaleFac.BELEGART_HAND + "')";
		}

		org.hibernate.Query query = session.createQuery(queryString);

		List<?> resultList = query.list();
		Iterator it = resultList.iterator();
		if (it.hasNext()) {
			BigDecimal summe = (BigDecimal) it.next();

			if (summe != null) {
				bdAnzahl = summe;
			}

		}

		session.close();

		return bdAnzahl;

	}

	private BigDecimal getSummeReservierungenUndFehlmengenFuerZeitraum(Integer artikelIId, int iVonMonat,
			Integer iBisMonat) {

		Calendar cVon = Calendar.getInstance();
		cVon.add(Calendar.MONTH, iVonMonat);

		java.sql.Date dBis = null;
		if (iBisMonat != null) {

			Calendar cBis = Calendar.getInstance();
			cBis.add(Calendar.MONTH, iBisMonat);

			dBis = new java.sql.Date(cBis.getTime().getTime());
		}

		BigDecimal bdAnzahl = BigDecimal.ZERO;

		SessionFactory factory = FLRSessionFactory.getFactory();
		org.hibernate.Session session = factory.openSession();

		String queryString = "SELECT sum (a.n_menge) FROM FLRArtikelreservierung a WHERE a.flrartikel.i_id="
				+ artikelIId + " AND a.t_liefertermin>='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(cVon.getTime().getTime())) + "'";

		if (dBis != null) {
			queryString += " AND a.t_liefertermin<'" + Helper.formatDateWithSlashes(dBis) + "'";
		}

		org.hibernate.Query query = session.createQuery(queryString);

		List<?> resultList = query.list();
		Iterator it = resultList.iterator();
		if (it.hasNext()) {
			BigDecimal bdRes = (BigDecimal) it.next();
			if (bdRes != null) {
				bdAnzahl = bdRes;
			}

		}

		session.close();

		session = factory.openSession();

		queryString = "SELECT sum (f.n_menge) FROM FLRFehlmenge f WHERE f.artikel_i_id=" + artikelIId
				+ " AND f.t_liefertermin>='" + Helper.formatDateWithSlashes(new java.sql.Date(cVon.getTime().getTime()))
				+ "'";
		if (dBis != null) {
			queryString += " AND f.t_liefertermin<'" + Helper.formatDateWithSlashes(dBis) + "'";
		}
		query = session.createQuery(queryString);

		resultList = query.list();
		it = resultList.iterator();
		if (it.hasNext()) {
			BigDecimal bdFM = (BigDecimal) it.next();
			if (bdFM != null) {
				bdAnzahl = bdAnzahl.add(bdFM);
			}

		}

		return bdAnzahl;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printBedarfszusammenschau(boolean bMitHandlagerbewegungen, boolean bMitArtikelkommentar,
			TheClientDto theClientDto) {
		this.useCase = UC_BEDARFSZUSAMMENSCHAU;
		Map<String, Object> mapParameter = new TreeMap<String, Object>();

		mapParameter.put("P_MITHANDLAGERBEWEGUNGEN", new Boolean(bMitHandlagerbewegungen));
		mapParameter.put("P_MITARTIKELKOMMENTAR", new Boolean(bMitArtikelkommentar));

		TreeMap<String, Object[]> tmDaten = new TreeMap();

		// Basis sind die Artikel der Internen Bestellung und die offenen Lose

		SessionFactory factory = FLRSessionFactory.getFactory();
		org.hibernate.Session session = factory.openSession();

		String queryString = "SELECT distinct ig.flrstueckliste.artikel_i_id, ig.flrstueckliste.partner_i_id FROM FLRInternebestellung ig WHERE ig.mandant_c_nr='"
				+ theClientDto.getMandant() + "'";

		org.hibernate.Query query = session.createQuery(queryString);

		List<?> resultList = query.list();
		Iterator it = resultList.iterator();
		while (it.hasNext()) {
			Object[] o = (Object[]) it.next();

			Integer artikelIId = (Integer) o[0];

			ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);

			Integer partnerIId = (Integer) o[1];

			String partner = "";
			PartnerDto partnerDto = null;
			if (partnerIId != null) {

				partnerDto = getPartnerFac().partnerFindByPrimaryKey(partnerIId, theClientDto);
				partner = partnerDto.formatFixName1Name2();

			}

			tmDaten.put(Helper.fitString2Length(partner, PartnerFac.MAX_NAME, ' ') + aDto.getCNr(),
					new Object[] { aDto, partnerDto });

		}

		session.close();

		session = factory.openSession();

		queryString = "SELECT distinct los.flrstueckliste.artikel_i_id, los.flrstueckliste.partner_i_id FROM FLRLos los WHERE los.mandant_c_nr='"
				+ theClientDto.getMandant() + "' AND los.status_c_nr IN ('" + FertigungFac.STATUS_AUSGEGEBEN + "','"
				+ FertigungFac.STATUS_ANGELEGT + "','" + FertigungFac.STATUS_IN_PRODUKTION + "','"
				+ FertigungFac.STATUS_TEILERLEDIGT + "') ";

		query = session.createQuery(queryString);

		resultList = query.list();
		it = resultList.iterator();
		while (it.hasNext()) {

			Object[] o = (Object[]) it.next();

			Integer artikelIId = (Integer) o[0];

			ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);

			Integer partnerIId = (Integer) o[1];

			String partner = "";
			PartnerDto partnerDto = null;
			if (partnerIId != null) {

				partnerDto = getPartnerFac().partnerFindByPrimaryKey(partnerIId, theClientDto);
				partner = partnerDto.formatFixName1Name2();

			}

			tmDaten.put(Helper.fitString2Length(partner, PartnerFac.MAX_NAME, ' ') + aDto.getCNr(),
					new Object[] { aDto, partnerDto });

		}

		session.close();

		ArrayList alDaten = new ArrayList();

		Iterator itArtikel = tmDaten.keySet().iterator();

		while (itArtikel.hasNext()) {

			Object[] o = tmDaten.get(itArtikel.next());

			ArtikelDto aDto = (ArtikelDto) o[0];
			PartnerDto pDto = (PartnerDto) o[1];
			Object[] oZeile = new Object[BZ_SPALTENANZAHL];

			oZeile[BZ_ARTIKELNUMMER] = aDto.getCNr();
			if (aDto.getArtikelsprDto() != null) {
				oZeile[BZ_BEZEICHNUNG] = aDto.getArtikelsprDto().getCBez();
				oZeile[BZ_ZUSATZBEZEICHNUNG] = aDto.getArtikelsprDto().getCZbez();
				oZeile[BZ_KURZBEZEICHNUNG] = aDto.getArtikelsprDto().getCKbez();
			}

			KundeDto kdDto = null;
			if (pDto != null) {
				oZeile[BZ_KUNDE] = pDto.formatFixName1Name2();

				try {
					kdDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(pDto.getIId(),
							theClientDto.getMandant(), theClientDto);
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}

			oZeile[BZ_REVISION] = aDto.getCRevision();
			oZeile[BZ_INDEX] = aDto.getCIndex();
			oZeile[BZ_LAGERSOLL] = aDto.getFLagersoll();
			oZeile[BZ_LAGERMINDEST] = aDto.getFLagermindest();
			oZeile[BZ_JAHRESMENGE_GEPLANT] = aDto.getFJahresmenge();
			oZeile[BZ_FERTIGUNGSSATZGROESSE] = aDto.getFFertigungssatzgroesse();
			try {
				if (aDto.getArtgruIId() != null) {

					oZeile[BZ_ARTIKELGRUPPE] = getArtikelFac().artgruFindByPrimaryKey(aDto.getArtgruIId(), theClientDto)
							.getBezeichnung();

				}
				if (aDto.getArtklaIId() != null) {

					oZeile[BZ_ARTIKELKLASSE] = getArtikelFac().artklaFindByPrimaryKey(aDto.getArtklaIId(), theClientDto)
							.getBezeichnung();

				}

				oZeile[BZ_IN_FERTIGUNG] = getFertigungFac().getAnzahlInFertigung(aDto.getIId(), theClientDto);

				oZeile[BZ_LAGERSTAND] = getLagerFac().getLagerstandAllerLagerEinesMandanten(aDto.getIId(),
						theClientDto);

				oZeile[BZ_RAHMENRESERVIERT] = getReservierungFac().getAnzahlRahmenreservierungen(aDto.getIId(),
						theClientDto);
				oZeile[BZ_BESTELLT] = getArtikelbestelltFac().getAnzahlBestellt(aDto.getIId());
				BigDecimal rahmenbestellt = null;
				Hashtable htAnzahlRahmenbestellt = getArtikelbestelltFac().getAnzahlRahmenbestellt(aDto.getIId(),
						theClientDto);
				if (htAnzahlRahmenbestellt.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
					rahmenbestellt = (BigDecimal) htAnzahlRahmenbestellt
							.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);

				}

				oZeile[BZ_RAHMENBESTELLT] = rahmenbestellt;

				oZeile[BZ_GESTPREIS] = getLagerFac()
						.getGemittelterGestehungspreisAllerLaegerEinesMandanten(aDto.getIId(), theClientDto);

				if (bMitArtikelkommentar == true) {

					oZeile[BZ_SUBREPORT_ARTIKELKOMMENTAR] = getSubreportArtikelkommentar(aDto.getIId(),
							LocaleFac.BELEGART_LOS, theClientDto);
				}

				if (kdDto != null) {

					VkpreisfindungDto vkpreisfindungDto = getVkPreisfindungFac().verkaufspreisfindungWeb(aDto.getIId(),
							kdDto.getIId(), BigDecimal.ONE,
							Helper.cutDate(new java.sql.Date(System.currentTimeMillis())),
							kdDto.getVkpfArtikelpreislisteIIdStdpreisliste(), null,
							theClientDto.getSMandantenwaehrung(), theClientDto);
					VerkaufspreisDto kundenVKPreisDto = Helper.getVkpreisBerechnet(vkpreisfindungDto);

					if (kundenVKPreisDto != null) {
						oZeile[BZ_VKPREIS] = kundenVKPreisDto.nettopreis;
					}

				} else {
					oZeile[BZ_VKPREIS] = getVkPreisfindungFac().ermittlePreisbasis(aDto.getIId(),

							Helper.cutDate(new java.sql.Date(System.currentTimeMillis())), null,
							theClientDto.getSMandantenwaehrung(), theClientDto);
				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			org.hibernate.Session sessionIB = factory.openSession();

			String queryStringIB = "SELECT sum(ig.n_menge) FROM FLRInternebestellung ig WHERE ig.flrstueckliste.artikel_i_id="
					+ aDto.getIId();

			org.hibernate.Query queryIB = sessionIB.createQuery(queryStringIB);

			List<?> resultListIB = queryIB.list();
			Iterator itIB = resultListIB.iterator();
			if (itIB.hasNext()) {

				BigDecimal bdSumme = (BigDecimal) itIB.next();
				oZeile[BZ_SUMME_MENGE_INTERNEBESTELLUNG] = bdSumme;
			}
			sessionIB.close();

			oZeile[BZ_MONAT_ZUKUNFT_1] = getSummeReservierungenUndFehlmengenFuerZeitraum(aDto.getIId(), 0, 1);
			oZeile[BZ_MONAT_ZUKUNFT_2] = getSummeReservierungenUndFehlmengenFuerZeitraum(aDto.getIId(), 1, 2);
			oZeile[BZ_MONAT_ZUKUNFT_3] = getSummeReservierungenUndFehlmengenFuerZeitraum(aDto.getIId(), 2, 3);
			oZeile[BZ_MONAT_ZUKUNFT_6] = getSummeReservierungenUndFehlmengenFuerZeitraum(aDto.getIId(), 3, 6);
			oZeile[BZ_MONAT_ZUKUNFT_9] = getSummeReservierungenUndFehlmengenFuerZeitraum(aDto.getIId(), 6, 9);
			oZeile[BZ_MONAT_ZUKUNFT_12] = getSummeReservierungenUndFehlmengenFuerZeitraum(aDto.getIId(), 9, 12);
			oZeile[BZ_MONAT_ZUKUNFT_DANACH] = getSummeReservierungenUndFehlmengenFuerZeitraum(aDto.getIId(), 12, null);

			oZeile[BZ_MONAT_VERGANGENHEIT_1] = getSummeAbbuchungenInDerVergangenheit(aDto.getIId(), 1, 0,
					bMitHandlagerbewegungen);

			oZeile[BZ_MONAT_VERGANGENHEIT_2] = getSummeAbbuchungenInDerVergangenheit(aDto.getIId(), 2, 1,
					bMitHandlagerbewegungen);

			oZeile[BZ_MONAT_VERGANGENHEIT_3] = getSummeAbbuchungenInDerVergangenheit(aDto.getIId(), 3, 2,
					bMitHandlagerbewegungen);

			oZeile[BZ_MONAT_VERGANGENHEIT_6] = getSummeAbbuchungenInDerVergangenheit(aDto.getIId(), 6, 3,
					bMitHandlagerbewegungen);

			oZeile[BZ_MONAT_VERGANGENHEIT_9] = getSummeAbbuchungenInDerVergangenheit(aDto.getIId(), 9, 6,
					bMitHandlagerbewegungen);

			oZeile[BZ_MONAT_VERGANGENHEIT_12] = getSummeAbbuchungenInDerVergangenheit(aDto.getIId(), 12, 9,
					bMitHandlagerbewegungen);

			alDaten.add(oZeile);

		}

		data = new Object[alDaten.size()][BZ_SPALTENANZAHL];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_BEDARFSZUSAMMENSCHAU,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		JasperPrintLP print = getReportPrint();
		PrintInfoDto values = new PrintInfoDto();
		values.setDocPath(new DocPath(new DocNodeBedarfszusammenschau(theClientDto.getMandant())));

		values.setiId(theClientDto.getIDPersonal());
		values.setTable("");

		print.setOInfoForArchive(values);

		return print;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printFehlerstatistik(java.sql.Timestamp tVon, java.sql.Timestamp tBis, Integer iSortierung,
			boolean bAlleAnzeigen, TheClientDto theClientDto) {
		this.useCase = UC_FEHLERSTATISTIK;
		Map<String, Object> mapParameter = new TreeMap<String, Object>();

		SessionFactory factory = FLRSessionFactory.getFactory();
		org.hibernate.Session session = factory.openSession();

		String queryString = "SELECT lgs FROM FLRLosgutschlecht lgs LEFT OUTER JOIN lgs.flrfehler fehler LEFT OUTER JOIN lgs.flrlossollarbeitsplan.flrlos.flrstueckliste.flrartikel flra WHERE lgs.flrlossollarbeitsplan.flrlos.status_c_nr<>'"
				+ FertigungFac.STATUS_STORNIERT + "'";

		if (bAlleAnzeigen == false) {
			queryString += " AND ((lgs.flrlossollarbeitsplan.flrlos.t_erledigt>=' "
					+ Helper.formatTimestampWithSlashes(tVon) + "' AND lgs.flrlossollarbeitsplan.flrlos.t_erledigt<' "
					+ Helper.formatTimestampWithSlashes(tBis)
					+ "') OR (lgs.flrlossollarbeitsplan.flrlos.t_manuellerledigt>=' "
					+ Helper.formatTimestampWithSlashes(tVon)
					+ "' AND lgs.flrlossollarbeitsplan.flrlos.t_manuellerledigt<' "
					+ Helper.formatTimestampWithSlashes(tBis) + "')) ";
		} else {
			queryString += " AND lgs.flrlossollarbeitsplan.flrlos.t_produktionsbeginn>=' "
					+ Helper.formatTimestampWithSlashes(tVon)
					+ "' AND lgs.flrlossollarbeitsplan.flrlos.t_produktionsbeginn<' "
					+ Helper.formatTimestampWithSlashes(tBis) + "' ";
		}

		String sortierung = "";
		if (iSortierung == FertigungReportFac.SORTIERUNG_FEHLERSTATISTIK_ARTIKELNUMMER) {
			sortierung = getTextRespectUISpr("lp.artikelnummer", theClientDto.getMandant(), theClientDto.getLocUi());
			queryString += " ORDER BY flra.c_nr ";
		} else if (iSortierung == FertigungReportFac.SORTIERUNG_FEHLERSTATISTIK_FEHLER) {
			sortierung = getTextRespectUISpr("lp.fehler", theClientDto.getMandant(), theClientDto.getLocUi());
			queryString += " ORDER BY fehler.c_bez ";
		} else if (iSortierung == FertigungReportFac.SORTIERUNG_FEHLERSTATISTIK_LOSNUMMER) {
			sortierung = getTextRespectUISpr("fert.tab.unten.los.title", theClientDto.getMandant(),
					theClientDto.getLocUi());
			queryString += " ORDER BY lgs.flrlossollarbeitsplan.flrlos.c_nr ";
		}
		mapParameter.put("P_SORTIERUNG", sortierung);

		String sLocUI = Helper.locale2String(theClientDto.getLocUi());

		org.hibernate.Query query = session.createQuery(queryString);

		List<?> resultList = query.list();
		Iterator it = resultList.iterator();

		ArrayList alDaten = new ArrayList();

		while (it.hasNext()) {
			FLRLosgutschlecht lgs = (FLRLosgutschlecht) it.next();

			Object[] zeile = new Object[FEHLERSTATISTIK_SPALTENANZAHL];

			zeile[FEHLERSTATISTIK_AG_AGNUMMER] = lgs.getFlrlossollarbeitsplan().getI_arbeitsgangsnummer();
			zeile[FEHLERSTATISTIK_AG_UAGNUMMER] = lgs.getFlrlossollarbeitsplan().getI_unterarbeitsgang();
			zeile[FEHLERSTATISTIK_AG_ARTIKELNUMMER] = lgs.getFlrlossollarbeitsplan().getFlrartikel().getC_nr();
			zeile[FEHLERSTATISTIK_AG_ARTIKELBEZEICHNUNG] = getArtikelFac().artikelFindByPrimaryKeySmall(
					lgs.getFlrlossollarbeitsplan().getFlrartikel().getI_id(), theClientDto).formatBezeichnung();
			if (lgs.getFlrlossollarbeitsplan().getFlrlos().getT_erledigt() != null) {
				zeile[FEHLERSTATISTIK_ERLEDIGTDATUM] = lgs.getFlrlossollarbeitsplan().getFlrlos().getT_erledigt();
			} else {
				zeile[FEHLERSTATISTIK_ERLEDIGTDATUM] = lgs.getFlrlossollarbeitsplan().getFlrlos()
						.getT_manuellerledigt();
			}

			if (lgs.getFlrfehler() != null) {
				zeile[FEHLERSTATISTIK_FEHLER] = lgs.getFlrfehler().getC_bez();

				if (lgs.getFlrfehler().getSprset() != null && lgs.getFlrfehler().getSprset().size() > 0) {

					Iterator<?> sprsetIterator = lgs.getFlrfehler().getSprset().iterator();

					while (sprsetIterator.hasNext()) {
						FLRFehlerspr fehlerspr = (FLRFehlerspr) sprsetIterator.next();
						if (fehlerspr.getLocale().getC_nr().compareTo(sLocUI) == 0) {
							zeile[FEHLERSTATISTIK_FEHLERSPR] = fehlerspr.getC_bez();
							break;
						}
					}

				}

			}
			zeile[FEHLERSTATISTIK_FEHLER_KOMMENTAR] = lgs.getC_kommentar();
			zeile[FEHLERSTATISTIK_GUT] = lgs.getN_gut();
			zeile[FEHLERSTATISTIK_INARBEIT] = lgs.getN_inarbeit();
			zeile[FEHLERSTATISTIK_LOSGROESSE] = lgs.getFlrlossollarbeitsplan().getFlrlos().getN_losgroesse();
			zeile[FEHLERSTATISTIK_LOSNUMMER] = lgs.getFlrlossollarbeitsplan().getFlrlos().getC_nr();
			try {
				zeile[FEHLERSTATISTIK_MENGE_ERLEDIGT] = getFertigungFac()
						.getErledigteMenge(lgs.getFlrlossollarbeitsplan().getFlrlos().getI_id(), theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			zeile[FEHLERSTATISTIK_PROJEKT] = lgs.getFlrlossollarbeitsplan().getFlrlos().getC_projekt();
			zeile[FEHLERSTATISTIK_SCHLECHT] = lgs.getN_schlecht();
			if (lgs.getFlrlossollarbeitsplan().getFlrlos().getFlrstueckliste() != null) {
				zeile[FEHLERSTATISTIK_STUECKLISTE] = lgs.getFlrlossollarbeitsplan().getFlrlos().getFlrstueckliste()
						.getFlrartikel().getC_nr();
				zeile[FEHLERSTATISTIK_STUECKLISTEBEZEICHNUNG] = getArtikelFac().artikelFindByPrimaryKeySmall(
						lgs.getFlrlossollarbeitsplan().getFlrlos().getFlrstueckliste().getFlrartikel().getI_id(),
						theClientDto).formatBezeichnung();

			}

			if (lgs.getFlrzeitdaten() != null) {
				zeile[FEHLERSTATISTIK_PERSONAL] = HelperServer
						.formatAdresseEinesFLRPartner(lgs.getFlrzeitdaten().getFlrpersonal().getFlrpartner());
				zeile[FEHLERSTATISTIK_ZEITPUNKT] = lgs.getFlrzeitdaten().getT_zeit();
			}

			alDaten.add(zeile);
		}

		session.close();

		mapParameter.put("P_VON", tVon);
		mapParameter.put("P_BIS", new Timestamp(tBis.getTime() - 3600000));
		mapParameter.put("P_ALLE_LOSE_ANZEIGEN", new Boolean(bAlleAnzeigen));

		data = new Object[alDaten.size()][FEHLERSTATISTIK_SPALTENANZAHL];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_FEHLERSTATISTIK,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printLosstatistik(java.sql.Timestamp tVon, java.sql.Timestamp tBis, Integer losIId,
			Integer stuecklisteIId, Integer auftragIId, boolean bArbeitsplanSortiertNachAG, boolean bVerdichtet,
			java.sql.Timestamp tStichtag, TheClientDto theClientDto) {

		this.useCase = UC_LOSSTATISTIK;
		this.index = -1;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		PersonalDto[] personalDtos = null;
		MaschineDto[] maschineDtos = null;
		try {
			personalDtos = getPersonalFac().personalFindByMandantCNr(theClientDto.getMandant(), true);
			maschineDtos = getZeiterfassungFac().maschineFindByMandantCNr(theClientDto.getMandant());
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

		Map<String, Object> mapParameter = new TreeMap<String, Object>();

		Criteria c = session.createCriteria(FLRLosReport.class);
		c.add(Restrictions.eq(FertigungFac.FLR_LOS_MANDANT_C_NR, theClientDto.getMandant()));
		c.add(Restrictions.not(Restrictions.in(FertigungFac.FLR_LOS_STATUS_C_NR,
				new String[] { LocaleFac.STATUS_ANGELEGT, LocaleFac.STATUS_STORNIERT })));
		if (stuecklisteIId != null) {
			c.add(Restrictions.eq(FertigungFac.FLR_LOS_STUECKLISTE_I_ID, stuecklisteIId));

			StuecklisteDto stuecklisteDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(stuecklisteIId,
					theClientDto);
			mapParameter.put("P_STUECKLISTE", stuecklisteDto.getArtikelDto().formatArtikelbezeichnung());

		}
		if (losIId != null) {
			c.add(Restrictions.eq("i_id", losIId));
		}
		if (auftragIId != null) {
			c.createAlias("flrauftrag", "a").add(Restrictions.eq("a.i_id", auftragIId));

			try {
				AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(auftragIId);
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(),
						theClientDto);
				mapParameter.put("P_AUFTRAG", auftragDto.getCNr());
				mapParameter.put("P_AUFTRAGPROJEKT", auftragDto.getCBezProjektbezeichnung());
				mapParameter.put("P_AUFTRAGKUNDE", kundeDto.getPartnerDto().formatAnrede());

				mapParameter.put("P_BEARBEITER",
						getPersonalFac().getPersonRpt(auftragDto.getPersonalIIdAnlegen(), theClientDto));
				if (auftragDto.getPersonalIIdVertreter() != null) {
					mapParameter.put("P_VERTRETER",
							getPersonalFac().getPersonRpt(auftragDto.getPersonalIIdVertreter(), theClientDto));
				}

			} catch (RemoteException ex2) {
				throwEJBExceptionLPRespectOld(ex2);
			}

		}
		if (tStichtag != null) {
			mapParameter.put("P_STICHTAG", tStichtag);// new
			// Timestamp(tStichtag.getTime()));
			tStichtag = Helper.cutTimestamp(Helper.addiereTageZuTimestamp(tStichtag, 1));
		}

		if (losIId == null && auftragIId == null) {

			if (tVon != null) {
				c.add(Restrictions.or(Restrictions.ge(FertigungFac.FLR_LOSREPORT_T_MANUELLERLEDIGT, tVon),
						Restrictions.ge(FertigungFac.FLR_LOS_T_ERLEDIGT, tVon)));
				mapParameter.put("P_VON", tVon);
			}
			if (tBis != null) {
				c.add(Restrictions.or(Restrictions.lt(FertigungFac.FLR_LOSREPORT_T_MANUELLERLEDIGT, tBis),
						Restrictions.lt(FertigungFac.FLR_LOS_T_ERLEDIGT, tBis)));
				mapParameter.put("P_BIS", new Timestamp(tBis.getTime() - 3600000));
			}

			if (tStichtag != null) {
				c.add(Restrictions.or(Restrictions.lt(FertigungFac.FLR_LOSREPORT_T_ANLEGEN, tStichtag),
						Restrictions.lt(FertigungFac.FLR_LOSREPORT_T_ANLEGEN, tStichtag)));
				c.add(Restrictions.or(Restrictions.ge(FertigungFac.FLR_LOSREPORT_T_MANUELLERLEDIGT, tStichtag),
						Restrictions.ge(FertigungFac.FLR_LOS_T_ERLEDIGT, tStichtag)));
			}

		}
		c.addOrder(Order.asc("c_nr"));

		List<?> results = c.list();
		ArrayList<LosStatistikDto> al = new ArrayList<LosStatistikDto>();

		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			FLRLosReport los = (FLRLosReport) resultListIterator.next();

			try {

				LosDto losDto = getFertigungFac().losFindByPrimaryKey(los.getI_id());

				BigDecimal abgeliefert = new BigDecimal(0);
				for (Iterator<?> iter2 = los.getAblieferungset().iterator(); iter2.hasNext();) {
					FLRLosablieferung item = (FLRLosablieferung) iter2.next();
					abgeliefert = abgeliefert.add(item.getN_menge());
				}

				// VKpreis
				BigDecimal vkPreis = null;

				if (los.getFlrauftragposition() != null) {
					vkPreis = los.getFlrauftragposition().getN_nettogesamtpreisplusversteckteraufschlagminusrabatte();
				} else {

					if (los.getFlrstueckliste() != null) {

						Integer kundeIId = null;

						if (los.getFlrauftrag() != null) {
							kundeIId = los.getFlrauftrag().getFlrkunde().getI_id();
						}
						if (kundeIId == null && los.getFlrkunde() != null) {
							kundeIId = los.getFlrkunde().getI_id();
						}

						if (kundeIId != null) {
							KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto);

//							MwstsatzDto mwstsatzDtoAktuell = getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(
//									kundeDto.getMwstsatzbezIId(), theClientDto);

							Timestamp belegDatum = Helper
									.cutTimestamp(new Timestamp(losDto.getTProduktionsende().getTime()));
							MwstsatzDto mwstsatzDtoAktuell = getMandantFac()
									.mwstsatzZuDatumValidate(kundeDto.getMwstsatzbezIId(), belegDatum, theClientDto);
							VkpreisfindungDto vkpreisfindungDto = getVkPreisfindungFac().verkaufspreisfindung(
									los.getFlrstueckliste().getArtikel_i_id(), kundeIId, losDto.getNLosgroesse(),
									losDto.getTProduktionsende(), kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
									mwstsatzDtoAktuell.getIId(), theClientDto.getSMandantenwaehrung(), theClientDto);

							VerkaufspreisDto kundenVKPreisDto = Helper.getVkpreisBerechnet(vkpreisfindungDto);
							if (kundenVKPreisDto != null) {
								vkPreis = kundenVKPreisDto.nettopreis;
							}
						} else {
							VkPreisfindungEinzelverkaufspreisDto vkpreisDto = getVkPreisfindungFac()
									.getArtikeleinzelverkaufspreis(los.getFlrstueckliste().getArtikel_i_id(), null,
											theClientDto.getSMandantenwaehrung(), theClientDto);
							if (vkpreisDto != null && vkpreisDto.getNVerkaufspreisbasis() != null) {
								vkPreis = vkpreisDto.getNVerkaufspreisbasis();
							}
						}

					}
				}

				// Zuerst Material
				Session session2 = factory.openSession();
				Criteria cSoll = session.createCriteria(FLRLossollmaterial.class);
				cSoll.add(Restrictions.eq(FertigungFac.FLR_LOSSOLLMATERIAL_LOS_I_ID, los.getI_id()));
				cSoll.createAlias("flrartikel", "a");
				cSoll.addOrder(Order.asc("a.c_nr"));
				List<?> resultsSoll = cSoll.list();
				Iterator<?> resultListIteratorSoll = resultsSoll.iterator();
				while (resultListIteratorSoll.hasNext()) {
					FLRLossollmaterial sollmat = (FLRLossollmaterial) resultListIteratorSoll.next();
					if (bVerdichtet == false && sollmat.getIstmaterialset().size() > 0) {
						int i = 0;
						for (Iterator<?> iter = sollmat.getIstmaterialset().iterator(); iter.hasNext();) {
							FLRLosistmaterial item = (FLRLosistmaterial) iter.next();
							LosStatistikDto losStatistikDto = new LosStatistikDto(losDto);
							losStatistikDto.setArtikelnummer(sollmat.getFlrartikel().getC_nr());
							ArtikelDto artikelDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(sollmat.getFlrartikel().getI_id(), theClientDto);
							losStatistikDto.setArtikelbezeichnung(artikelDto.formatBezeichnung());

							Timestamp tsBelegdatum = null;

							List<SeriennrChargennrMitMengeDto> snrDtos = getLagerFac()
									.getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(LocaleFac.BELEGART_LOS,
											item.getI_id());

							for (int k = 0; k < snrDtos.size(); k++) {

								LagerbewegungDto bewDto = getLagerFac().getLetzteintrag(LocaleFac.BELEGART_LOS,
										item.getI_id(), snrDtos.get(k).getCSeriennrChargennr());
								tsBelegdatum = bewDto.getTBelegdatum();
								break;
							}

							losStatistikDto.setBuchungszeit(tsBelegdatum);

							if (i == 0) {
								losStatistikDto.setSollmenge(sollmat.getN_menge());
								losStatistikDto.setSollpreis(sollmat.getN_sollpreis());
							} else {
								losStatistikDto.setSollmenge(new BigDecimal(0));
								losStatistikDto.setSollpreis(new BigDecimal(0));
							}

							losStatistikDto.setBMaterial(true);
							losStatistikDto.setIstpreis(
									getFertigungFac().getAusgegebeneMengePreis(sollmat.getI_id(), null, theClientDto));
							losStatistikDto.setAbgelieferteMenge(abgeliefert);
							losStatistikDto.setVkpreisStueckliste(vkPreis);

							BigDecimal istmenge = new BigDecimal(0);

							if (Helper.short2boolean(item.getB_abgang()) == true) {
								istmenge = istmenge.add(item.getN_menge());
							} else {
								istmenge = istmenge.subtract(item.getN_menge());
							}

							losStatistikDto.setIstmenge(istmenge);

							if (tStichtag == null) {

								al.add(losStatistikDto);
								i++;
							} else {
								if (tsBelegdatum == null) {
									al.add(losStatistikDto);
									i++;
								} else {
									if (tsBelegdatum.before(tStichtag)) {
										al.add(losStatistikDto);
										i++;
									}
								}

							}

						}
					} else {

						LosStatistikDto losStatistikDto = new LosStatistikDto(losDto);
						losStatistikDto.setArtikelnummer(sollmat.getFlrartikel().getC_nr());
						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(sollmat.getFlrartikel().getI_id(), theClientDto);
						losStatistikDto.setArtikelbezeichnung(artikelDto.formatBezeichnung());
						losStatistikDto.setSollmenge(sollmat.getN_menge());
						losStatistikDto.setSollpreis(sollmat.getN_sollpreis());
						losStatistikDto.setBMaterial(true);
						losStatistikDto.setIstpreis(
								getFertigungFac().getAusgegebeneMengePreis(sollmat.getI_id(), null, theClientDto));
						losStatistikDto.setAbgelieferteMenge(abgeliefert);
						losStatistikDto.setVkpreisStueckliste(vkPreis);

						BigDecimal istmenge = new BigDecimal(0);
						for (Iterator<?> iter = sollmat.getIstmaterialset().iterator(); iter.hasNext();) {
							FLRLosistmaterial item = (FLRLosistmaterial) iter.next();

							// Menge 0 auslassen
							if (item.getN_menge().doubleValue() != 0) {
								if (tStichtag != null) {

									Timestamp tsBelegdatum = null;

									List<SeriennrChargennrMitMengeDto> snrDtos = getLagerFac()
											.getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(
													LocaleFac.BELEGART_LOS, item.getI_id());

									for (int k = 0; k < snrDtos.size(); k++) {

										LagerbewegungDto bewDto = getLagerFac().getLetzteintrag(LocaleFac.BELEGART_LOS,
												item.getI_id(), snrDtos.get(k).getCSeriennrChargennr());
										tsBelegdatum = bewDto.getTBelegdatum();
										break;
									}

									if (tsBelegdatum.before(tStichtag)) {

									} else {
										continue;
									}

								}

								if (Helper.short2boolean(item.getB_abgang()) == true) {
									istmenge = istmenge.add(item.getN_menge());
								} else {
									istmenge = istmenge.subtract(item.getN_menge());
								}
							}
						}
						losStatistikDto.setIstmenge(istmenge);

						al.add(losStatistikDto);
					}
				}
				session2.close();

				LossollarbeitsplanDto[] lossollarbeitsplanDtos = getFertigungFac()
						.lossollarbeitsplanFindByLosIId(los.getI_id());
				// PJ14351 Nach AG sortieren
				if (bArbeitsplanSortiertNachAG == true) {

					// Nach Fertigungsgruppe sortieren
					for (int k = lossollarbeitsplanDtos.length - 1; k > 0; --k) {
						for (int j = 0; j < k; ++j) {
							Integer a1 = lossollarbeitsplanDtos[j].getIArbeitsgangnummer();
							Integer a2 = lossollarbeitsplanDtos[j + 1].getIArbeitsgangnummer();
							if (a1.intValue() > a2.intValue()) {
								LossollarbeitsplanDto temp = lossollarbeitsplanDtos[j];
								lossollarbeitsplanDtos[j] = lossollarbeitsplanDtos[j + 1];
								lossollarbeitsplanDtos[j + 1] = temp;
							}
						}
					}
				}

				ArrayList<LosStatistikDto> hmSoll = new ArrayList<LosStatistikDto>();

				// Zuerst Maschinenzeiten
				for (int i = 0; i < lossollarbeitsplanDtos.length; i++) {
					if (lossollarbeitsplanDtos[i].getMaschineIId() != null) {
						Integer artikelIId = lossollarbeitsplanDtos[i].getArtikelIIdTaetigkeit();
						BigDecimal sollzeit = lossollarbeitsplanDtos[i].getNGesamtzeit();
						ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(artikelIId, theClientDto);

						// Schon vorhanden?
						boolean bGefunden = false;
						for (int j = 0; j < hmSoll.size(); j++) {
							LosStatistikDto losStatistikDto = (LosStatistikDto) hmSoll.get(j);
							if (losStatistikDto.getArtikelnummer().equals(artikelDto.getCNr())) {
								if (lossollarbeitsplanDtos[i].getMaschineIId()
										.equals(losStatistikDto.getMaschineIId())) {
									losStatistikDto.setSollmenge(losStatistikDto.getSollmenge().add(sollzeit));
									bGefunden = true;
									hmSoll.set(j, losStatistikDto);
								}
							}
						}
						if (!bGefunden) {
							LosStatistikDto losStatistikMaschineDto = new LosStatistikDto(losDto);
							losStatistikMaschineDto.setArtikelnummer(artikelDto.getCNr());
							losStatistikMaschineDto.setArtikelbezeichnung(artikelDto.formatBezeichnung());
							losStatistikMaschineDto.setSollmenge(sollzeit);
							losStatistikMaschineDto.setBMaterial(false);
							losStatistikMaschineDto.setBIstPerson(false);

							losStatistikMaschineDto.setAbgelieferteMenge(abgeliefert);
							losStatistikMaschineDto.setVkpreisStueckliste(vkPreis);

							losStatistikMaschineDto
									.setSollpreis(
											getZeiterfassungFac()
													.getMaschinenKostenZumZeitpunkt(
															lossollarbeitsplanDtos[i].getMaschineIId(),
															Helper.cutTimestamp(
																	new Timestamp(System.currentTimeMillis())),LocaleFac.BELEGART_LOS,lossollarbeitsplanDtos[i].getIId())
													.getBdStundensatz());
							MaschineDto maschineDto = getZeiterfassungFac()
									.maschineFindByPrimaryKey(lossollarbeitsplanDtos[i].getMaschineIId());

							String maschinenname = "M:";
							if (maschineDto.getCIdentifikationsnr() != null) {
								maschinenname += maschineDto.getCIdentifikationsnr() + " ";
							}
							maschinenname += maschineDto.getCBez();

							losStatistikMaschineDto.setPersonMaschine(maschinenname);
							losStatistikMaschineDto.setMaschineIId(maschineDto.getIId());

							hmSoll.add(losStatistikMaschineDto);
						}
					}
				}
				// Dann Personalzeiten
				for (int i = 0; i < lossollarbeitsplanDtos.length; i++) {

					if (!Helper.short2boolean(lossollarbeitsplanDtos[i].getBNurmaschinenzeit())) {

						Integer artikelIId = lossollarbeitsplanDtos[i].getArtikelIIdTaetigkeit();
						BigDecimal sollzeit = lossollarbeitsplanDtos[i].getNGesamtzeit();
						ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(artikelIId, theClientDto);

						// Schon vorhanden?
						boolean bGefunden = false;
						for (int j = 0; j < hmSoll.size(); j++) {
							LosStatistikDto losStatistikDto = (LosStatistikDto) hmSoll.get(j);
							if (losStatistikDto.getArtikelnummer().equals(artikelDto.getCNr())) {

								if (losStatistikDto.getMaschineIId() == null) {
									losStatistikDto.setSollmenge(losStatistikDto.getSollmenge().add(sollzeit));
									hmSoll.set(j, losStatistikDto);
									bGefunden = true;
								}

							}

						}

						if (!bGefunden) {
							LosStatistikDto losStatistikDto = new LosStatistikDto(losDto);
							losStatistikDto.setArtikelnummer(artikelDto.getCNr());
							losStatistikDto.setArtikelbezeichnung(artikelDto.formatBezeichnung());
							losStatistikDto.setSollmenge(sollzeit);
							losStatistikDto.setBMaterial(false);
							losStatistikDto.setBIstPerson(true);
							losStatistikDto.setAbgelieferteMenge(abgeliefert);
							losStatistikDto.setVkpreisStueckliste(vkPreis);
							ArtikellieferantDto artikellieferantDto = getArtikelFac()
									.getArtikelEinkaufspreisDesBevorzugtenLieferanten(artikelDto.getIId(),
											new BigDecimal(1), theClientDto.getSMandantenwaehrung(), theClientDto);
							if (artikellieferantDto != null && artikellieferantDto.getLief1Preis() != null) {
								losStatistikDto.setSollpreis(artikellieferantDto.getLief1Preis());
							} else {
								losStatistikDto.setSollpreis(new BigDecimal(0));
							}
							hmSoll.add(losStatistikDto);

						}
					}
				}
				// Dann Zeiten

				AuftragzeitenDto[] maschinenzeitenDtos = getZeiterfassungFac()
						.getAllMaschinenzeitenEinesBeleges(los.getI_id(), null, null, tStichtag, theClientDto);

				AuftragzeitenDto[] personalzeitenDtos = getZeiterfassungFac().getAllZeitenEinesBeleges(
						LocaleFac.BELEGART_LOS, los.getI_id(), null, null, null, tStichtag,
						ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ARTIKEL, theClientDto);

				for (int i = 0; i < personalzeitenDtos.length; i++) {

					boolean bGefunden = false;
					for (int j = 0; j < hmSoll.size(); j++) {
						LosStatistikDto losStatistikDto = (LosStatistikDto) hmSoll.get(j);

						if (losStatistikDto.getMaschineIId() == null) {

							if (losStatistikDto.getArtikelnummer().equals(personalzeitenDtos[i].getSArtikelcnr())) {
								bGefunden = true;

							}
						}
					}

					if (bGefunden == false) {
						LosStatistikDto losStatistikDto = new LosStatistikDto(losDto);
						losStatistikDto.setArtikelnummer(personalzeitenDtos[i].getSArtikelcnr());
						losStatistikDto.setArtikelbezeichnung(personalzeitenDtos[i].getSArtikelbezeichnung());
						losStatistikDto.setSollmenge(new BigDecimal(0));
						losStatistikDto.setSollpreis(new BigDecimal(0));
						losStatistikDto.setAbgelieferteMenge(abgeliefert);
						losStatistikDto.setVkpreisStueckliste(vkPreis);
						hmSoll.add(losStatistikDto);

					}
				}

				for (int i = 0; i < maschinenzeitenDtos.length; i++) {
					boolean bGefunden = false;

					if (tStichtag != null && maschinenzeitenDtos[i].getTsEnde() != null
							&& tStichtag.before(maschinenzeitenDtos[i].getTsEnde())) {
						continue;
					}

					for (int j = 0; j < hmSoll.size(); j++) {
						LosStatistikDto losStatistikDto = (LosStatistikDto) hmSoll.get(j);

						if (losStatistikDto.getMaschineIId() != null && losStatistikDto.getMaschineIId()
								.equals(maschinenzeitenDtos[i].getIPersonalMaschinenId())) {

							if (losStatistikDto.getArtikelnummer().equals(maschinenzeitenDtos[i].getSArtikelcnr())) {
								bGefunden = true;

							}
						}
					}

					if (bGefunden == false) {
						LosStatistikDto losStatistikDto = new LosStatistikDto(losDto);
						losStatistikDto.setArtikelnummer(maschinenzeitenDtos[i].getSArtikelcnr());
						losStatistikDto.setArtikelbezeichnung(maschinenzeitenDtos[i].getSArtikelbezeichnung());
						losStatistikDto.setMaschineIId(maschinenzeitenDtos[i].getIPersonalMaschinenId());
						losStatistikDto.setPersonMaschine(maschinenzeitenDtos[i].getSPersonalMaschinenname());

						losStatistikDto.setBMaterial(false);
						losStatistikDto.setBIstPerson(false);

						losStatistikDto.setSollmenge(new BigDecimal(0));
						losStatistikDto.setSollpreis(new BigDecimal(0));
						losStatistikDto.setAbgelieferteMenge(abgeliefert);
						losStatistikDto.setVkpreisStueckliste(vkPreis);

						hmSoll.add(losStatistikDto);

					}

				}

				for (int k = 0; k < hmSoll.size(); k++) {
					LosStatistikDto losStatistikDto = (LosStatistikDto) hmSoll.get(k);

					losStatistikDto.setAbgelieferteMenge(abgeliefert);

					int iPos = al.size();
					al.add(losStatistikDto);
					// Personen
					if (losStatistikDto.getMaschineIId() == null) {
						for (int i = 0; i < personalDtos.length; i++) {
							PersonalDto personalDto = personalDtos[i];

							BigDecimal bdgesamtzeit = new BigDecimal(0);
							BigDecimal bdgesamtkosten = new BigDecimal(0);
							String person = "";
							for (int j = 0; j < personalzeitenDtos.length; j++) {
								AuftragzeitenDto azDto = personalzeitenDtos[j];
								if (azDto != null) {
									if (personalDto.getIId().equals(azDto.getIPersonalMaschinenId())
											&& azDto.getSArtikelcnr().equals(losStatistikDto.getArtikelnummer())) {
										bdgesamtzeit = bdgesamtzeit.add(new BigDecimal(azDto.getDdDauer()));
										bdgesamtkosten = bdgesamtkosten.add(azDto.getBdKosten());
										person = azDto.getSPersonalMaschinenname();
										personalzeitenDtos[j] = null;
									}
								}
							}
							if (bdgesamtzeit.doubleValue() != 0 || bdgesamtkosten.doubleValue() != 0) {

								LosStatistikDto losStatistikPersonDto = new LosStatistikDto(losDto);
								losStatistikPersonDto.setArtikelnummer(losStatistikDto.getArtikelnummer());
								losStatistikPersonDto.setArtikelbezeichnung(losStatistikDto.getArtikelbezeichnung());
								losStatistikPersonDto.setPersonMaschine(person);
								losStatistikPersonDto.setBMaterial(false);
								losStatistikPersonDto.setAbgelieferteMenge(abgeliefert);
								losStatistikPersonDto.setVkpreisStueckliste(vkPreis);

								losStatistikPersonDto.setIstmenge(bdgesamtzeit);
								if (bdgesamtzeit.doubleValue() != 0) {
									losStatistikPersonDto.setIstpreis(
											bdgesamtkosten.divide(bdgesamtzeit, 4, BigDecimal.ROUND_HALF_EVEN));
								} else {
									losStatistikPersonDto.setIstpreis(new BigDecimal(0));
								}
								al.add(losStatistikPersonDto);

							}

						}
					}
					// Maschinen
					if (losStatistikDto.getMaschineIId() != null) {
						for (int i = 0; i < maschineDtos.length; i++) {
							MaschineDto maschineDto = maschineDtos[i];

							BigDecimal bdgesamtzeit = new BigDecimal(0);
							BigDecimal bdgesamtkosten = new BigDecimal(0);
							for (int j = 0; j < maschinenzeitenDtos.length; j++) {

								if (tStichtag != null && maschinenzeitenDtos[j].getTsEnde() != null
										&& tStichtag.before(maschinenzeitenDtos[j].getTsEnde())) {
									continue;
								}

								AuftragzeitenDto azDto = maschinenzeitenDtos[j];
								if (maschineDto.getIId().equals(azDto.getIPersonalMaschinenId())
										&& azDto.getSArtikelcnr().equals(losStatistikDto.getArtikelnummer())) {
									if (losStatistikDto.getMaschineIId().equals(azDto.getIPersonalMaschinenId())) {
										bdgesamtzeit = bdgesamtzeit.add(new BigDecimal(azDto.getDdDauer()));
										bdgesamtkosten = bdgesamtkosten.add(azDto.getBdKosten());
									}

								}
							}
							if (bdgesamtzeit.doubleValue() != 0 || bdgesamtkosten.doubleValue() != 0) {

								BigDecimal kosten = bdgesamtkosten.divide(bdgesamtzeit, 4, BigDecimal.ROUND_HALF_EVEN);

								if (losStatistikDto.getIstmenge() != null) {
									losStatistikDto.setIstmenge(losStatistikDto.getIstmenge().add(bdgesamtzeit));
								} else {
									losStatistikDto.setIstmenge(bdgesamtzeit);
								}

								if (losStatistikDto.getIstpreis() != null) {
									losStatistikDto.setIstpreis(losStatistikDto.getIstpreis().add(kosten));
								} else {
									losStatistikDto.setIstpreis(kosten);
								}
								al.set(iPos, losStatistikDto);
							}

						}
					}
				}

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

		}
		session.close();

		data = new Object[al.size()][23];
		for (int i = 0; i < al.size(); i++) {
			LosStatistikDto losStatistikDto = (LosStatistikDto) al.get(i);
			data[i][LOSSTATISTIK_GRUPPIERUNG] = losStatistikDto.getLosDto().getCNr();

			if (losStatistikDto.getLosDto().getTManuellerledigt() != null) {
				data[i][LOSSTATISTIK_ERLEDIGUNGSDATUM] = losStatistikDto.getLosDto().getTManuellerledigt();
			} else {
				data[i][LOSSTATISTIK_ERLEDIGUNGSDATUM] = losStatistikDto.getLosDto().getTErledigt();
			}

			data[i][LOSSTATISTIK_BEWERTUNG] = losStatistikDto.getLosDto().getFBewertung();
			data[i][LOSSTATISTIK_GEPLANTESSOLLMATERIAL] = losStatistikDto.getLosDto().getNSollmaterial();
			data[i][LOSSTATISTIK_GRUPPIERUNGBEZEICHNUNG] = losStatistikDto.getLosDto().getCProjekt();
			data[i][LOSSTATISTIK_GRUPPIERUNGERLEDIGT] = losStatistikDto.getLosDto().getTErledigt();
			data[i][LOSSTATISTIK_GRUPPIERUNGAUSGABE] = losStatistikDto.getLosDto().getTAusgabe();
			data[i][LOSSTATISTIK_GRUPPIERUNABGELIEFERTEMENGE] = losStatistikDto.getAbgelieferteMenge();
			data[i][LOSSTATISTIK_GRUPPIERUNGLOSGROESSE] = losStatistikDto.getLosDto().getNLosgroesse();
			data[i][LOSSTATISTIK_GRUPPIERUNVKPREIS] = losStatistikDto.getVkpreisStueckliste();
			data[i][LOSSTATISTIK_BUCHUNGSZEIT] = losStatistikDto.getBuchungszeit();

			if (losStatistikDto.getLosDto().getStuecklisteIId() != null) {

				ArtikelDto aDto = getStuecklisteFac()
						.stuecklisteFindByPrimaryKey(losStatistikDto.getLosDto().getStuecklisteIId(), theClientDto)
						.getArtikelDto();

				data[i][LOSSTATISTIK_GRUPPIERUNGSTKLARTIKEL] = aDto.formatArtikelbezeichnung();
				data[i][LOSSTATISTIK_GRUPPIERUNGSTKLARTIKELREFERENZNUMMER] = aDto.getCReferenznr();
			}

			if (losStatistikDto.getLosDto().getAuftragIId() != null) {
				AuftragDto auftragDto = getAuftragFac()
						.auftragFindByPrimaryKey(losStatistikDto.getLosDto().getAuftragIId());
				if (auftragDto.getKundeIIdAuftragsadresse() != null) {
					KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(),
							theClientDto);
					data[i][LOSSTATISTIK_GRUPPIERUNGKUNDE] = kundeDto.getPartnerDto().formatAnrede();
				}
			} else if (losStatistikDto.getLosDto().getKundeIId() != null) {
				// SP9742
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(losStatistikDto.getLosDto().getKundeIId(),
						theClientDto);
				data[i][LOSSTATISTIK_GRUPPIERUNGKUNDE] = kundeDto.getPartnerDto().formatAnrede();
			}

			data[i][LOSSTATISTIK_ISTMENGE] = losStatistikDto.getIstmenge();
			data[i][LOSSTATISTIK_ISTPREIS] = losStatistikDto.getIstpreis();
			data[i][LOSSTATISTIK_PERSONALMASCHINE] = losStatistikDto.getPersonMaschine();
			data[i][LOSSTATISTIK_ISTPERSON] = new Boolean(losStatistikDto.isBIstPerson());
			data[i][LOSSTATISTIK_SOLLMENGE] = losStatistikDto.getSollmenge();
			data[i][LOSSTATISTIK_SOLLPREIS] = losStatistikDto.getSollpreis();
			data[i][LOSSTATISTIK_ARTIKELNUMMER] = losStatistikDto.getArtikelnummer();
			data[i][LOSSTATISTIK_ARTIKELBEZEICHNUNG] = losStatistikDto.getArtikelbezeichnung();
			data[i][LOSSTATISTIK_UNTERGRUPPEMATERIAL] = new Boolean(losStatistikDto.isBMaterial());
		}

		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_LOSSTATISTIK,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printPruefergebnis(java.sql.Timestamp tVon, java.sql.Timestamp tBis, Integer losIId,
			Integer stuecklisteIId, Integer auftragIId, TheClientDto theClientDto) {

		this.useCase = UC_PRUEFERGEBNIS;
		this.index = -1;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		Map<String, Object> mapParameter = new TreeMap<String, Object>();

		Criteria c = session.createCriteria(FLRLosReport.class);
		c.add(Restrictions.eq(FertigungFac.FLR_LOS_MANDANT_C_NR, theClientDto.getMandant()));
		c.add(Restrictions.not(Restrictions.in(FertigungFac.FLR_LOS_STATUS_C_NR,
				new String[] { LocaleFac.STATUS_ANGELEGT, LocaleFac.STATUS_STORNIERT })));
		if (stuecklisteIId != null) {
			c.add(Restrictions.eq(FertigungFac.FLR_LOS_STUECKLISTE_I_ID, stuecklisteIId));

			StuecklisteDto stuecklisteDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(stuecklisteIId,
					theClientDto);
			mapParameter.put("P_STUECKLISTE", stuecklisteDto.getArtikelDto().formatArtikelbezeichnung());

		}
		if (losIId != null) {
			c.add(Restrictions.eq("i_id", losIId));

			try {
				mapParameter.put("P_LOSNUMMER", getFertigungFac().losFindByPrimaryKey(losIId).getCNr());
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}
		if (auftragIId != null) {
			c.createAlias("flrauftrag", "a").add(Restrictions.eq("a.i_id", auftragIId));

			try {
				AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(auftragIId);
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(),
						theClientDto);
				mapParameter.put("P_AUFTRAG", auftragDto.getCNr());
				mapParameter.put("P_AUFTRAGPROJEKT", auftragDto.getCBezProjektbezeichnung());
				mapParameter.put("P_AUFTRAGKUNDE", kundeDto.getPartnerDto().formatAnrede());

				mapParameter.put("P_BEARBEITER",
						getPersonalFac().getPersonRpt(auftragDto.getPersonalIIdAnlegen(), theClientDto));
				if (auftragDto.getPersonalIIdVertreter() != null) {
					mapParameter.put("P_VERTRETER",
							getPersonalFac().getPersonRpt(auftragDto.getPersonalIIdVertreter(), theClientDto));
				}

			} catch (RemoteException ex2) {
				throwEJBExceptionLPRespectOld(ex2);
			}

		}

		if (losIId == null && auftragIId == null) {

			if (tVon != null) {
				c.add(Restrictions.or(Restrictions.ge(FertigungFac.FLR_LOSREPORT_T_MANUELLERLEDIGT, tVon),
						Restrictions.ge(FertigungFac.FLR_LOS_T_ERLEDIGT, tVon)));
				mapParameter.put("P_VON", tVon);
			}
			if (tBis != null) {
				c.add(Restrictions.or(Restrictions.lt(FertigungFac.FLR_LOSREPORT_T_MANUELLERLEDIGT, tBis),
						Restrictions.lt(FertigungFac.FLR_LOS_T_ERLEDIGT, tBis)));
				mapParameter.put("P_BIS", new Timestamp(tBis.getTime() - 3600000));
			}

		}
		c.addOrder(Order.asc("c_nr"));

		List<?> results = c.list();
		ArrayList alDaten = new ArrayList();

		String ablieferungenOhnePruefergebnisse = "";

		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			FLRLosReport los = (FLRLosReport) resultListIterator.next();

			try {

				LosDto losDto = getFertigungFac().losFindByPrimaryKey(los.getI_id());

				ArrayList<StuecklistepositionDto> alPositionenKontakt = new ArrayList<StuecklistepositionDto>();
				ArrayList<StuecklistepositionDto> alPositionenLitze = new ArrayList<StuecklistepositionDto>();
				ArrayList<StuecklistepositionDto> alPositionenLitze2 = new ArrayList<StuecklistepositionDto>();

				StuecklisteDto stuecklisteDto = null;
				if (losDto.getStuecklisteIId() != null) {
					stuecklisteDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(),
							theClientDto);

					StuecklistepositionDto[] stklPosDtos = getStuecklisteFac()
							.stuecklistepositionFindByStuecklisteIId(losDto.getStuecklisteIId(), theClientDto);
					for (int j = 0; j < stklPosDtos.length; j++) {
						alPositionenKontakt.add(stklPosDtos[j]);
						alPositionenLitze.add(stklPosDtos[j]);
						alPositionenLitze2.add(stklPosDtos[j]);

					}
				}

				for (Iterator<?> iter2 = los.getAblieferungset().iterator(); iter2.hasNext();) {
					FLRLosablieferung item = (FLRLosablieferung) iter2.next();

					// Pruefergebnisse holen

					Session session2 = factory.openSession();
					Criteria cErgebnisse = session2.createCriteria(FLRPruefergebnis.class);
					cErgebnisse.add(Restrictions.eq("losablieferung_i_id", item.getI_id()));

					List<?> resultsErgebnisse = cErgebnisse.list();

					if (resultsErgebnisse.size() == 0) {

						ablieferungenOhnePruefergebnisse += losDto.getCNr() + " " + Helper.formatTimestamp(
								new Timestamp(item.getT_aendern().getTime()), theClientDto.getLocUi()) + ";";
						// Zu Subreport hinzufuegen
					} else {
						Iterator<?> resultListIteratorSoll = resultsErgebnisse.iterator();
						while (resultListIteratorSoll.hasNext()) {
							FLRPruefergebnis pe = (FLRPruefergebnis) resultListIteratorSoll.next();

							Object[] oZeile = new Object[PRUEFERGEBNIS_ANZAHL_SPALTEN];

							if (stuecklisteDto != null) {
								oZeile[PRUEFERGEBNIS_STUECKLISTENUMMER] = stuecklisteDto.getArtikelDto().getCNr();
								if (stuecklisteDto.getArtikelDto().getArtikelsprDto() != null) {

									oZeile[PRUEFERGEBNIS_STUECKLISTEBEZEICHNUNG] = stuecklisteDto.getArtikelDto()
											.getArtikelsprDto().getCBez();
								}
							}

							oZeile[PRUEFERGEBNIS_BEGINN] = losDto.getTProduktionsbeginn();
							oZeile[PRUEFERGEBNIS_ENDE] = losDto.getTProduktionsende();
							oZeile[PRUEFERGEBNIS_LOSNUMMMER] = losDto.getCNr();
							oZeile[PRUEFERGEBNIS_ABLIEFERZEITPUNKT] = item.getT_aendern();
							oZeile[PRUEFERGEBNIS_ABLIEFERMENGE] = item.getN_menge();

							Integer artikelIIdKontakt = null;
							if (pe.getFlrlospruefplan().getFlrlossollmaterial_kontakt() != null) {
								oZeile[PRUEFERGEBNIS_ARTIKEL_KONTAKT] = pe.getFlrlospruefplan()
										.getFlrlossollmaterial_kontakt().getFlrartikel().getC_nr();
								artikelIIdKontakt = pe.getFlrlospruefplan().getFlrlossollmaterial_kontakt()
										.getFlrartikel().getI_id();
								ArtikelDto aDtoKontakt = getArtikelFac().artikelFindByPrimaryKeySmall(pe
										.getFlrlospruefplan().getFlrlossollmaterial_kontakt().getFlrartikel().getI_id(),
										theClientDto);

								String positionAusStuecklisteKontakt = null;
								for (int u = 0; u < alPositionenKontakt.size(); u++) {

									if (pe.getFlrlospruefplan().getFlrlossollmaterial_kontakt().getFlrartikel()
											.getI_id().equals(alPositionenKontakt.get(u).getArtikelIId())
											&& pe.getFlrlospruefplan().getFlrlossollmaterial_kontakt().getN_menge()
													.divide(pe.getFlrlospruefplan().getFlrlos().getN_losgroesse(),
															BigDecimal.ROUND_HALF_EVEN)
													.equals(alPositionenKontakt.get(u).getNMenge())
											&& pe.getFlrlospruefplan().getFlrlossollmaterial_kontakt()
													.getMontageart_i_id()
													.equals(alPositionenKontakt.get(u).getMontageartIId())) {
										// wenn Menge und Artikel und Montageart
										// gleich

										positionAusStuecklisteKontakt = alPositionenKontakt.get(u).getCPosition();
										alPositionenKontakt.remove(u);
										break;

									}

								}
								oZeile[PRUEFERGEBNIS_POSITION_KONTAT] = positionAusStuecklisteKontakt;

								if (aDtoKontakt.getArtikelsprDto() != null) {
									oZeile[PRUEFERGEBNIS_ARTIKELBEZEICHNUNG_KONTAKT] = aDtoKontakt.getArtikelsprDto()
											.getCBez();
								}
							}

							Integer artikelIIdLitze = null;
							if (pe.getFlrlospruefplan().getFlrlossollmaterial_litze() != null) {
								oZeile[PRUEFERGEBNIS_ARTIKEL_LITZE] = pe.getFlrlospruefplan()
										.getFlrlossollmaterial_litze().getFlrartikel().getC_nr();

								ArtikelDto aDtoLitze = getArtikelFac().artikelFindByPrimaryKeySmall(
										pe.getFlrlospruefplan().getFlrlossollmaterial_litze().getFlrartikel().getI_id(),
										theClientDto);

								artikelIIdLitze = aDtoLitze.getIId();

								if (aDtoLitze.getArtikelsprDto() != null) {
									oZeile[PRUEFERGEBNIS_ARTIKELBEZEICHNUNG_LITZE] = aDtoLitze.getArtikelsprDto()
											.getCBez();
								}

								String positionAusStuecklisteLitze = null;
								for (int u = 0; u < alPositionenLitze.size(); u++) {

									if (pe.getFlrlospruefplan().getFlrlossollmaterial_litze().getFlrartikel().getI_id()
											.equals(alPositionenLitze.get(u).getArtikelIId())
											&& pe.getFlrlospruefplan().getFlrlossollmaterial_litze().getN_menge()
													.divide(pe.getFlrlospruefplan().getFlrlos().getN_losgroesse(),
															BigDecimal.ROUND_HALF_EVEN)
													.equals(alPositionenLitze.get(u).getNMenge())
											&& pe.getFlrlospruefplan().getFlrlossollmaterial_litze()
													.getMontageart_i_id()
													.equals(alPositionenLitze.get(u).getMontageartIId())) {
										// wenn Menge und Artikel und Montageart
										// gleich

										positionAusStuecklisteLitze = alPositionenLitze.get(u).getCPosition();
										alPositionenLitze.remove(u);
										break;

									}

								}

								oZeile[PRUEFERGEBNIS_POSITION_LITZE] = positionAusStuecklisteLitze;

							}

							Integer artikelIIdLitze2 = null;
							if (pe.getFlrlospruefplan().getFlrlossollmaterial_litze2() != null) {
								oZeile[PRUEFERGEBNIS_ARTIKEL_LITZE2] = pe.getFlrlospruefplan()
										.getFlrlossollmaterial_litze2().getFlrartikel().getC_nr();

								ArtikelDto aDtoLitze = getArtikelFac().artikelFindByPrimaryKeySmall(pe
										.getFlrlospruefplan().getFlrlossollmaterial_litze2().getFlrartikel().getI_id(),
										theClientDto);

								artikelIIdLitze = aDtoLitze.getIId();

								if (aDtoLitze.getArtikelsprDto() != null) {
									oZeile[PRUEFERGEBNIS_ARTIKELBEZEICHNUNG_LITZE2] = aDtoLitze.getArtikelsprDto()
											.getCBez();
								}

								String positionAusStuecklisteLitze2 = null;
								for (int u = 0; u < alPositionenLitze2.size(); u++) {

									if (pe.getFlrlospruefplan().getFlrlossollmaterial_litze2().getFlrartikel().getI_id()
											.equals(alPositionenLitze2.get(u).getArtikelIId())
											&& pe.getFlrlospruefplan().getFlrlossollmaterial_litze2().getN_menge()
													.divide(pe.getFlrlospruefplan().getFlrlos().getN_losgroesse(),
															BigDecimal.ROUND_HALF_EVEN)
													.equals(alPositionenLitze2.get(u).getNMenge())
											&& pe.getFlrlospruefplan().getFlrlossollmaterial_litze2()
													.getMontageart_i_id()
													.equals(alPositionenLitze2.get(u).getMontageartIId())) {
										// wenn Menge und Artikel und Montageart
										// gleich

										positionAusStuecklisteLitze2 = alPositionenLitze2.get(u).getCPosition();
										alPositionenLitze2.remove(u);
										break;

									}

								}

								oZeile[PRUEFERGEBNIS_POSITION_LITZE2] = positionAusStuecklisteLitze2;

							}
							PruefartDto paDto = getStuecklisteFac().pruefartFindByPrimaryKey(
									pe.getFlrlospruefplan().getFlrpruefart().getI_id(), theClientDto);

							oZeile[PRUEFERGEBNIS_PRUEFART] = paDto.getCNr();

							oZeile[PRUEFERGEBNIS_PRUEFART_BEZEICHNUNG] = paDto.getBezeichnung();
							Integer werkzeugIId = null;
							if (pe.getFlrlospruefplan().getFlrwerkzeug() != null) {
								oZeile[PRUEFERGEBNIS_WERKZEUGNUMMER] = pe.getFlrlospruefplan().getFlrwerkzeug()
										.getC_nr();
								oZeile[PRUEFERGEBNIS_WERKZEUGBEZEICHNUNG] = pe.getFlrlospruefplan().getFlrwerkzeug()
										.getC_bez();

								werkzeugIId = pe.getFlrlospruefplan().getFlrwerkzeug().getI_id();

							}
							if (pe.getFlrlospruefplan().getFlrverschleissteil() != null) {
								oZeile[PRUEFERGEBNIS_VERSCHLEISSTEILNUMMER] = pe.getFlrlospruefplan()
										.getFlrverschleissteil().getC_nr();
								oZeile[PRUEFERGEBNIS_VERSCHLEISSTEILBEZEICHNUNG] = pe.getFlrlospruefplan()
										.getFlrverschleissteil().getC_bez();
							}

							Integer pkIId = getStuecklisteFac().pruefeObPruefplanInPruefkombinationVorhanden(null,
									pe.getFlrlospruefplan().getFlrpruefart().getI_id(), artikelIIdKontakt,
									artikelIIdLitze, artikelIIdLitze2,
									pe.getFlrlospruefplan().getVerschleissteil_i_id(),
									pe.getFlrlospruefplan().getPruefkombination_i_id(), false, theClientDto);

							if (pkIId != null) {
								PruefkombinationDto pkDto = getStuecklisteFac().pruefkombinationFindByPrimaryKey(pkIId,
										theClientDto);
								oZeile[PRUEFERGEBNIS_SOLL_CRIMPBREITE_DRAHT] = pkDto.getNCrimpbreitDraht();
								oZeile[PRUEFERGEBNIS_SOLL_CRIMPBREITE_ISOLATION] = pkDto.getNCrimpbreiteIsolation();
								oZeile[PRUEFERGEBNIS_SOLL_CRIMPHOEHE_DRAHT] = pkDto.getNCrimphoeheDraht();
								oZeile[PRUEFERGEBNIS_SOLL_CRIMPHOEHE_ISOLATION] = pkDto.getNCrimphoeheIsolation();
								oZeile[PRUEFERGEBNIS_SOLL_WERT] = pkDto.getNWert();

								oZeile[PRUEFERGEBNIS_SOLL_TOLERANZ_CRIMPBREITE_DRAHT] = pkDto
										.getNToleranzCrimpbreitDraht();
								oZeile[PRUEFERGEBNIS_SOLL_TOLERANZ_CRIMPBREITE_ISOLATION] = pkDto
										.getNToleranzCrimpbreiteIsolation();
								oZeile[PRUEFERGEBNIS_SOLL_TOLERANZ_CRIMPHOEHE_DRAHT] = pkDto
										.getNToleranzCrimphoeheDraht();
								oZeile[PRUEFERGEBNIS_SOLL_TOLERANZ_CRIMPHOEHE_ISOLATION] = pkDto
										.getNToleranzCrimphoeheIsolation();
								oZeile[PRUEFERGEBNIS_SOLL_TOLERANZ_WERT] = pkDto.getNToleranzWert();

								oZeile[PRUEFERGEBNIS_ABZUGSKRAFT_LITZE] = pkDto.getNAbzugskraftLitze();
								oZeile[PRUEFERGEBNIS_ABZUGSKRAFT_LITZE2] = pkDto.getNAbzugskraftLitze2();

								if (pkDto.getPruefkombinationsprDto() != null) {
									oZeile[PRUEFERGEBNIS_KOMMENTAR_PRUEFKOMBINATION] = pkDto.getPruefkombinationsprDto()
											.getCBez();
								}

							}

							oZeile[PRUEFERGEBNIS_IST_CRIMPBREITE_DRAHT] = pe.getN_crimpbreite_draht();
							oZeile[PRUEFERGEBNIS_IST_CRIMPBREITE_ISOLATION] = pe.getN_crimpbreite_isolation();
							oZeile[PRUEFERGEBNIS_IST_CRIMPHOEHE_DRAHT] = pe.getN_crimphoehe_draht();
							oZeile[PRUEFERGEBNIS_IST_CRIMPHOEHE_ISOLATION] = pe.getN_crimphoehe_isolation();
							oZeile[PRUEFERGEBNIS_IST_WERT] = pe.getN_wert();

							if (pe.getB_wert() != null) {
								oZeile[PRUEFERGEBNIS_IST_WERT_BOOLEAN] = Helper.short2Boolean(pe.getB_wert());
							}

							alDaten.add(oZeile);

						}

					}

					session2.close();

				}

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
				return null;
			}
		}

		for (int k = alDaten.size() - 1; k > 0; --k) {
			for (int j = 0; j < k; ++j) {
				Object[] a1 = (Object[]) alDaten.get(j);
				Object[] a2 = (Object[]) alDaten.get(j + 1);

				if (((String) a1[PRUEFERGEBNIS_LOSNUMMMER]).compareTo((String) a2[PRUEFERGEBNIS_LOSNUMMMER]) > 0) {
					alDaten.set(j, a2);
					alDaten.set(j + 1, a1);

				} else if (((String) a1[PRUEFERGEBNIS_LOSNUMMMER])
						.compareTo((String) a2[PRUEFERGEBNIS_LOSNUMMMER]) == 0) {
					Timestamp k1 = (Timestamp) a1[PRUEFERGEBNIS_ABLIEFERZEITPUNKT];
					Timestamp k2 = (Timestamp) a2[PRUEFERGEBNIS_ABLIEFERZEITPUNKT];

					if (k1.after(k2)) {

						alDaten.set(j, a2);
						alDaten.set(j + 1, a1);

					}
				}
			}
		}

		mapParameter.put("P_ABLIEFERUNGEN_OHNE_PRUEFERGEBNISSE", ablieferungenOhnePruefergebnisse);

		data = new Object[alDaten.size()][PRUEFERGEBNIS_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_PRUEFERGEBNIS,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printNichtErfasstePruefergebnisse(java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			TheClientDto theClientDto) {

		this.useCase = UC_NICHT_ERFASSTE_PRUEFERGEBNISSE;
		this.index = -1;
		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT la FROM FLRLosablieferung la WHERE la.t_aendern >='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tVon.getTime())) + "' AND la.t_aendern <='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tBis.getTime())) + "' ORDER BY la.t_aendern ASC";

		Query qResult = session.createQuery(sQuery);
		List<?> results = qResult.list();
		ArrayList alDaten = new ArrayList();
		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			FLRLosablieferung la = (FLRLosablieferung) resultListIterator.next();

			ArrayList<StuecklistepositionDto> alPositionenKontakt = new ArrayList<StuecklistepositionDto>();
			ArrayList<StuecklistepositionDto> alPositionenLitze = new ArrayList<StuecklistepositionDto>();

			StuecklisteDto stuecklisteDto = null;
			if (la.getFlrlos().getStueckliste_i_id() != null) {
				stuecklisteDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(la.getFlrlos().getStueckliste_i_id(),
						theClientDto);

				StuecklistepositionDto[] stklPosDtos = getStuecklisteFac()
						.stuecklistepositionFindByStuecklisteIId(la.getFlrlos().getStueckliste_i_id(), theClientDto);
				for (int j = 0; j < stklPosDtos.length; j++) {
					alPositionenKontakt.add(stklPosDtos[j]);
					alPositionenLitze.add(stklPosDtos[j]);

				}
			}

			ArrayList<PruefergebnisDto> alErgebnisse = getFertigungServiceFac()
					.pruefergebnisFindByLosablieferungIId(la.getI_id());

			ArrayList<LospruefplanDto> alLospruefplan = getFertigungServiceFac()
					.lospruefplanFindyByLosIId(la.getLos_i_id());

			Iterator it = alErgebnisse.iterator();
			while (it.hasNext()) {
				PruefergebnisDto peDto = (PruefergebnisDto) it.next();

				for (int i = 0; i < alLospruefplan.size(); i++) {

					if (alLospruefplan.get(i).getIId().equals(peDto.getLospruefplanIId())) {
						alLospruefplan.remove(i);
						break;
					}

				}

			}

			// Die hier uebrigen fehlen
			Iterator itPP = alLospruefplan.iterator();
			while (itPP.hasNext()) {

				LospruefplanDto ppDto = (LospruefplanDto) itPP.next();

				Object[] oZeile = new Object[NEP_SPALTENANZAHL];

				oZeile[NEP_STATUS] = la.getFlrlos().getStatus_c_nr();
				oZeile[NEP_LOSNUMMER] = la.getFlrlos().getC_nr();
				oZeile[NEP_LOSGROESSE] = la.getFlrlos().getN_losgroesse();

				oZeile[NEP_ABLIEFERDATUM] = la.getT_aendern();

				oZeile[NEP_PRODUKTIONSBEGINN] = la.getFlrlos().getT_produktionsbeginn();

				if (la.getFlrlos().getStueckliste_i_id() != null) {
					StuecklisteDto stklDto = getStuecklisteFac()
							.stuecklisteFindByPrimaryKey(la.getFlrlos().getStueckliste_i_id(), theClientDto);

					if (stklDto != null && stklDto.getArtikelDto() != null) {

						oZeile[NEP_STUECKLISTENUMMER] = stklDto.getArtikelDto().getCNr();
						if (stklDto.getArtikelDto().getArtikelsprDto() != null) {
							oZeile[NEP_STUECKLISTEBEZEICHNUNG] = stklDto.getArtikelDto().getArtikelsprDto().getCBez();
							oZeile[NEP_STUECKLISTEZUSATZBEZEICHNUNG] = stklDto.getArtikelDto().getArtikelsprDto()
									.getCZbez();
						}
					}
				}

				PruefartDto paDto = getStuecklisteFac().pruefartFindByPrimaryKey(ppDto.getPruefartIId(), theClientDto);

				oZeile[NEP_PRUEFART] = paDto.getCNr();

				oZeile[NEP_PRUEFART_BEZEICHNUNG] = paDto.getBezeichnung();

				if (ppDto.getWerkzeugIId() != null && ppDto.getVerschleissteilIId() != null) {

					WerkzeugDto wDto = getArtikelFac().werkzeugFindByPrimaryKey(ppDto.getWerkzeugIId());

					VerschleissteilDto vDto = getArtikelFac()
							.verschleissteilFindByPrimaryKey(ppDto.getVerschleissteilIId());
					oZeile[NEP_WERKZEUGNUMMER] = wDto.getCNr();
					oZeile[NEP_WERKZEUGBEZEICHNUNG] = wDto.getBezeichnung();

					oZeile[NEP_VERSCHLEISSTEILNUMMER] = vDto.getCNr();
					oZeile[NEP_VERSCHLEISSTEILBEZEICHNUNG] = vDto.getBezeichnung();

				}

				try {

					if (ppDto.getLossollmaterialIIdKontakt() != null) {
						LossollmaterialDto sollmatKontaktDto = getFertigungFac()
								.lossollmaterialFindByPrimaryKey(ppDto.getLossollmaterialIIdKontakt());
						ArtikelDto aDtoKontakt = getArtikelFac()
								.artikelFindByPrimaryKey(sollmatKontaktDto.getArtikelIId(), theClientDto);

						String positionAusStuecklisteKontakt = null;
						for (int u = 0; u < alPositionenKontakt.size(); u++) {

							if (aDtoKontakt.getIId().equals(alPositionenKontakt.get(u).getArtikelIId())
									&& sollmatKontaktDto.getNMenge().equals(alPositionenKontakt.get(u).getNMenge())
									&& sollmatKontaktDto.getMontageartIId()
											.equals(alPositionenKontakt.get(u).getMontageartIId())) {
								// wenn Menge und Artikel und Montageart
								// gleich

								positionAusStuecklisteKontakt = alPositionenKontakt.get(u).getCPosition();
								alPositionenKontakt.remove(u);
								break;

							}

						}

						oZeile[NEP_ARTIKEL_KONTAKT] = aDtoKontakt.getCNr();
						oZeile[NEP_POSITION_KONTAKT] = positionAusStuecklisteKontakt;

						if (aDtoKontakt.getArtikelsprDto() != null) {
							oZeile[NEP_ARTIKELBEZEICHNUNG_KONTAKT] = aDtoKontakt.getArtikelsprDto().getCBez();
						}
					}
					if (ppDto.getLossollmaterialIIdLitze() != null) {

						LossollmaterialDto sollmatLitzetDto = getFertigungFac()
								.lossollmaterialFindByPrimaryKey(ppDto.getLossollmaterialIIdLitze());
						ArtikelDto aDtoLitze = getArtikelFac().artikelFindByPrimaryKey(sollmatLitzetDto.getArtikelIId(),
								theClientDto);

						String positionAusStuecklisteLitze = null;
						for (int u = 0; u < alPositionenLitze.size(); u++) {

							if (aDtoLitze.getIId().equals(alPositionenLitze.get(u).getArtikelIId())
									&& sollmatLitzetDto.getNMenge().equals(alPositionenLitze.get(u).getNMenge())
									&& sollmatLitzetDto.getMontageartIId()
											.equals(alPositionenLitze.get(u).getMontageartIId())) {
								// wenn Menge und Artikel und Montageart
								// gleich

								positionAusStuecklisteLitze = alPositionenLitze.get(u).getCPosition();
								alPositionenLitze.remove(u);
								break;

							}

						}

						oZeile[NEP_ARTIKEL_LITZE] = aDtoLitze.getCNr();
						oZeile[NEP_POSITION_LITZE] = positionAusStuecklisteLitze;

						if (aDtoLitze.getArtikelsprDto() != null) {
							oZeile[NEP_ARTIKELBEZEICHNUNG_LITZE] = aDtoLitze.getArtikelsprDto().getCBez();
						}

					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				alDaten.add(oZeile);

			}

		}
		Map<String, Object> mapParameter = new TreeMap<String, Object>();

		mapParameter.put("P_VON", tVon);

		mapParameter.put("P_BIS", tBis);

		data = new Object[alDaten.size()][NEP_SPALTENANZAHL];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL,
				FertigungReportFac.REPORT_NICHT_ERFASSTE_PRUEFERGEBNISSE, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAblieferungsstatistik(java.sql.Date dVon, java.sql.Date dBis, Integer artikelIId,
			int iOptionArtikel, String optionArtikel, int iSortierungAblieferungsstatistik, boolean bNurMaterialwerte,
			TheClientDto theClientDto) throws EJBExceptionLP {
		AblieferstatistikJournalKriterienDto kritDto = new AblieferstatistikJournalKriterienDto();
		kritDto.dVon = dVon;
		kritDto.dBis = dBis;
		kritDto.artikelIId = artikelIId;
		kritDto.optionArtikel = iOptionArtikel;
		kritDto.optionArtikelText = optionArtikel;
		kritDto.sort = iSortierungAblieferungsstatistik;
		kritDto.nurMaterialwerte = bNurMaterialwerte;
		return printAblieferungsstatistik(kritDto, theClientDto);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAblieferungsstatistik(AblieferstatistikJournalKriterienDto kritDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Session session = null;
		try {

			Map<String, Object> mapParameter = new TreeMap<String, Object>();

			this.useCase = UC_ABLIEFERUNGSSTATISTIK;
			this.index = -1;
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();

			String sQuery = "SELECT l,(SELECT COUNT(*) FROM FLRStuecklisteposition s WHERE s.flrartikel=l.flrlos.flrstueckliste.artikel_i_id) FROM FLRLosablieferung l WHERE l.flrlos.mandant_c_nr='"
					+ theClientDto.getMandant() + "'";

			if (kritDto.dVon != null) {
				kritDto.dVon = Helper.cutDate(kritDto.dVon);

				sQuery += " AND l.t_aendern>='" + Helper.formatDateWithSlashes(kritDto.dVon) + "'";

				mapParameter.put("P_VON", new Timestamp(kritDto.dVon.getTime()));

			}
			if (kritDto.dBis != null) {

				sQuery += " AND l.t_aendern<='" + Helper.formatDateWithSlashes(new java.sql.Date(
						Helper.cutTimestamp(Helper.addiereTageZuTimestamp(new Timestamp(kritDto.dBis.getTime()), 1))
								.getTime()))
						+ "'";

				mapParameter.put("P_BIS", new Timestamp(kritDto.dBis.getTime()));

			}

			if (kritDto.artikelIId != null) {
				sQuery += " AND l.flrlos.flrstueckliste.artikel_i_id=" + kritDto.artikelIId;
			}

			mapParameter.put("P_NURMATERIALWERTE", new Boolean(kritDto.nurMaterialwerte));

			mapParameter.put("P_OPTION_ARTIKEL", kritDto.optionArtikelText);
			mapParameter.put("P_OPTION_ARTIKEL_ID", Integer.valueOf(kritDto.optionArtikel));

			if (kritDto.sort == ABLIEFERSTATISTIK_OPTION_SORTIERUNG_ARTIKEL) {

				sQuery += " ORDER BY l.flrlos.flrstueckliste.flrartikel.c_nr, l.t_aendern";

				mapParameter.put(LPReport.P_SORTIERUNG, getTextRespectUISpr("artikel.artikelnummerlang",
						theClientDto.getMandant(), theClientDto.getLocUi()));
			} else if (kritDto.sort == ABLIEFERSTATISTIK_OPTION_SORTIERUNG_ABLIEFERDATUM) {

				mapParameter.put(LPReport.P_SORTIERUNG,
						getTextRespectUISpr("fert.ablieferdatum", theClientDto.getMandant(), theClientDto.getLocUi()));
				sQuery += " ORDER BY l.t_aendern";
			} else if (kritDto.sort == ABLIEFERSTATISTIK_OPTION_SORTIERUNG_AUFTRAG) {
				mapParameter.put(LPReport.P_SORTIERUNG,
						getTextRespectUISpr("fert.ablieferstatistik.sortierung.auftragsnummer",
								theClientDto.getMandant(), theClientDto.getLocUi()));
				sQuery += " ORDER BY l.flrlos.flrstueckliste.flrartikel.c_nr, l.t_aendern";
			}

			org.hibernate.Query hquery = session.createQuery(sQuery);

			List<?> list = hquery.list();
			ArrayList alDaten = new ArrayList();

			Iterator<?> iter = list.iterator();

			while (iter.hasNext()) {
				Object[] o = (Object[]) iter.next();
				FLRLosablieferung losab = (FLRLosablieferung) o[0];

				Long verwendungsanzahlInstuecklisten = (Long) o[1];

				if (kritDto.optionArtikel == FertigungReportFac.ABLIEFERSTATISTIK_OPTION_ARTIKEL_NUR_KOPFSTUECKLISTEN) {

					if (verwendungsanzahlInstuecklisten != null && verwendungsanzahlInstuecklisten.longValue() > 0) {
						continue;
					}

				}

				if (losab.getFlrlos().getFlrstueckliste() != null) {

					ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
							losab.getFlrlos().getFlrstueckliste().getFlrartikel().getI_id(), theClientDto);

					Object[] oZeile = new Object[ABLIEF_ANZAHL_SPALTEN];

					oZeile[ABLIEF_LOSNUMMER] = losab.getFlrlos().getC_nr();
					oZeile[ABLIEF_IDENT] = artikelDto.getCNr();
					oZeile[ABLIEF_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
					if (artikelDto.getArtikelsprDto().getCZbez() != null) {
						oZeile[ABLIEF_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCZbez();
					} else {
						oZeile[ABLIEF_ZUSATZBEZEICHNUNG] = "";
					}
					oZeile[ABLIEF_DATUM] = losab.getT_aendern();
					oZeile[ABLIEF_MENGE] = losab.getN_menge();
					oZeile[ABLIEF_ABLIEFERPREIS] = losab.getN_gestehungspreis();
					oZeile[ABLIEF_MATERIALWERT] = losab.getN_materialwert();
					oZeile[ABLIEF_ARBEITSZEITWERT] = losab.getN_arbeitszeitwert();
					oZeile[ABLIEF_WERT] = losab.getN_gestehungspreis().multiply(losab.getN_menge());
					myLogger.info("Ablieferstatistik '" + artikelDto.getCNr() + "': "
							+ ((BigDecimal) (oZeile[ABLIEF_WERT])).toPlainString());

					// Auftrag
					String auftragsnummer = null;

					if (losab.getFlrlosreport().getFlrauftrag() != null) {
						auftragsnummer = losab.getFlrlosreport().getFlrauftrag().getC_nr();
					} else if (losab.getFlrlosreport().getFlrauftragposition() != null) {
						auftragsnummer = losab.getFlrlosreport().getFlrauftragposition().getFlrauftrag().getC_nr();
					}

					oZeile[ABLIEF_AUFRAGSNUMMER] = auftragsnummer;

					if (kritDto.optionArtikel == FertigungReportFac.ABLIEFERSTATISTIK_OPTION_ARTIKEL_NUR_KOPFLOSE) {

						LosablieferungDto laDto = getFertigungFac().losablieferungFindByPrimaryKey(losab.getI_id(),
								false, theClientDto);
						ArrayList<WarenabgangsreferenzDto> waReferenz = getLagerFac().getWarenausgangsreferenz(
								LocaleFac.BELEGART_LOSABLIEFERUNG, laDto.getIId(), laDto.getSeriennrChargennrMitMenge(),
								theClientDto);

						BigDecimal bdMaterialwertGesamt = null;

						if (kritDto.nurMaterialwerte == true) {

							bdMaterialwertGesamt = BigDecimal.ZERO;

							GesamtkalkulationDto gkDto = new GesamtkalkulationDto();
							gkDto = getFertigungReportFac().getDatenGesamtkalkulation(gkDto, laDto.getLosIId(), null, 0,
									BigDecimal.ONE, 99, theClientDto);

							ArrayList alGK = gkDto.getAlDaten();

							for (int i = 0; i < alGK.size(); i++) {
								Object[] oZeileGK = (Object[]) alGK.get(i);

								BigDecimal menge = (BigDecimal) oZeileGK[GESAMTKALKULATION_ISTMENGE];
								BigDecimal preis = (BigDecimal) oZeileGK[GESAMTKALKULATION_ISTPREIS];

								BigDecimal bdMengenfaktor = BigDecimal.ONE;
								if (oZeileGK[GESAMTKALKULATION_MENGENFAKTOR] != null) {
									bdMengenfaktor = (BigDecimal) oZeileGK[GESAMTKALKULATION_MENGENFAKTOR];
								}

								Boolean bAZ = (Boolean) oZeileGK[GESAMTKALKULATION_ARBEITSZEIT];

								if (bAZ == false && menge != null && preis != null) {
									bdMaterialwertGesamt = bdMaterialwertGesamt
											.add(menge.multiply(preis).multiply(bdMengenfaktor));
								}

							}

						}

						for (int i = 0; i < waReferenz.size(); i++) {
							WarenabgangsreferenzDto wa = waReferenz.get(i);

							if (!wa.getBelegart().equals(LocaleFac.BELEGART_LOS.trim())
									&& wa.getMenge().doubleValue() > 0) {

								Object[] oZeileKopie = oZeile.clone();

								oZeileKopie[ABLIEF_ABGANG_BELEGART] = wa.getBelegart();
								oZeileKopie[ABLIEF_ABGANG_BELEGNUMMER] = wa.getBelegnummer();
								oZeileKopie[ABLIEF_MENGE] = wa.getMenge();

								if (bdMaterialwertGesamt != null) {

									oZeileKopie[ABLIEF_MATERIALWERT] = bdMaterialwertGesamt.divide(wa.getMenge(), 4,
											BigDecimal.ROUND_HALF_EVEN);

									oZeileKopie[ABLIEF_ARBEITSZEITWERT] = null;
								}

								alDaten.add(oZeileKopie);
							}

						}

					} else {
						alDaten.add(oZeile);
					}

				}
			}

			// PJ18868
			if (kritDto.sort == ABLIEFERSTATISTIK_OPTION_SORTIERUNG_AUFTRAG) {
				for (int k = alDaten.size() - 1; k > 0; --k) {
					for (int j = 0; j < k; ++j) {
						String a1 = (String) ((Object[]) alDaten.get(j))[ABLIEF_AUFRAGSNUMMER];
						if (a1 == null) {
							a1 = "";
						}
						String a2 = (String) ((Object[]) alDaten.get(j + 1))[ABLIEF_AUFRAGSNUMMER];
						if (a2 == null) {
							a2 = "";
						}
						if (a1.compareTo(a2) > 0) {
							Object[] zeileTemp = (Object[]) alDaten.get(j);
							alDaten.set(j, alDaten.get(j + 1));
							alDaten.set(j + 1, zeileTemp);

						}
					}
				}
			}

			data = new Object[alDaten.size()][ABLIEF_ANZAHL_SPALTEN];
			data = (Object[][]) alDaten.toArray(data);

			mapParameter.put(P_KPI_VARIABLEN, kritDto.getKpiReportStorage());
			initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_ABLIEFERUNGSSTATISTIK,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
			return getReportPrint();
		} catch (Exception t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, t);
		} finally {
			closeSession(session);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAufloesbareFehlmengen(Integer iSortierung, Boolean bNurArtikelMitLagerstand,
			Boolean bOhneEigengefertigteArtikel, TheClientDto theClientDto) {
		Session session = null;
		try {

			this.useCase = UC_AUFLOESBARE_FEHLMENGEN;
			this.index = -1;
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRFehlmenge.class);
			c.createCriteria(ArtikelFac.FLR_FEHLMENGE_FLRLOSSOLLMATERIAL)
					.createCriteria(FertigungFac.FLR_LOSSOLLMATERIAL_FLRLOS)
					.add(Restrictions.eq(FertigungFac.FLR_LOS_MANDANT_C_NR, theClientDto.getMandant()));
			// c.add(Restrictions.eq("artikel_i_id",9223));
			List<?> list = c.list();
			int i = 0;
			LinkedList<ReportAufloesbareFehlmengenDto> cData = new LinkedList<ReportAufloesbareFehlmengenDto>();

			HashMap<Integer, String> hmVorhandeneBestelltEintraege = new HashMap<Integer, String>();

			for (Iterator<?> iter = list.iterator(); iter.hasNext(); i++) {
				FLRFehlmenge fm = (FLRFehlmenge) iter.next();

				boolean bDatensatzVerwenden = true;
				// gleich den Lagerstand holen
				BigDecimal bdLagerstand = getLagerFac().getLagerstandAllerLagerEinesMandanten(fm.getArtikel_i_id(),
						theClientDto);

				if (bNurArtikelMitLagerstand.booleanValue()) {
					// vorher Lagerstand pruefen
					if (bdLagerstand.compareTo(new BigDecimal(0)) <= 0) {
						bDatensatzVerwenden = false;
					}
				}

				if (bDatensatzVerwenden) {

					if (bOhneEigengefertigteArtikel == true) {
						StuecklisteDto stklDto = getStuecklisteFac()
								.stuecklisteFindByMandantCNrArtikelIIdOhneExc(fm.getArtikel_i_id(), theClientDto);

						if (stklDto != null) {
							if (!Helper.short2boolean(stklDto.getBFremdfertigung())) {
								continue;
							}
						}
					}
					ReportAufloesbareFehlmengenDto rep = new ReportAufloesbareFehlmengenDto();
					rep.setBdFehlmenge(fm.getN_menge());
					rep.setBdLagerstand(bdLagerstand);
					rep.setBdBestellt(getArtikelbestelltFac().getAnzahlBestellt(fm.getArtikel_i_id()));
					rep.setBLos(true);
					rep.setBdReserviert(
							getReservierungFac().getAnzahlReservierungen(fm.getArtikel_i_id(), theClientDto));
					rep.setDTermin(fm.getT_liefertermin());
					ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(fm.getArtikel_i_id(),
							theClientDto);
					rep.setSArtikelBezeichnung(artikelDto.getArtikelsprDto().getCBez());
					rep.setSArtikelZusatzBezeichnung(artikelDto.getArtikelsprDto().getCZbez());
					rep.setSArtikelNummer(artikelDto.getCNr());
					rep.setIArtikelIId(artikelDto.getIId());
					rep.setSEinheit(artikelDto.getEinheitCNr());
					rep.setSBelegnummer("ALO" + fm.getFlrlossollmaterial().getFlrlos().getC_nr());
					rep.setSProjekt(fm.getFlrlossollmaterial().getFlrlos().getC_projekt());
					if (fm.getFlrlossollmaterial().getFlrlos().getFlrstueckliste() != null) {
						ArtikelDto artikelDtoStueckliste = getArtikelFac().artikelFindByPrimaryKeySmall(
								fm.getFlrlossollmaterial().getFlrlos().getFlrstueckliste().getFlrartikel().getI_id(),
								theClientDto);
						rep.setSStuecklisteBezeichnung(artikelDtoStueckliste.getArtikelsprDto().getCBez());
						rep.setSStuecklisteZusatzBezeichnung(artikelDtoStueckliste.getArtikelsprDto().getCZbez());
						rep.setSStuecklisteNummer(artikelDtoStueckliste.getCNr());
					}
					cData.add(rep);

					if (!hmVorhandeneBestelltEintraege.containsKey(fm.getArtikel_i_id())) {

						// HQL
						Session session2 = factory.openSession();

						String query = "SELECT a.n_menge, a.c_belegartnr, a.i_belegartpositionid FROM FLRArtikelbestellt a WHERE a.flrartikel.i_id="
								+ fm.getArtikel_i_id();

						Query qResult = session.createQuery(query);
						// qResult.setMaxResults(50);

						List<?> results = qResult.list();
						Iterator<?> it2 = results.iterator();
						while (it2.hasNext()) {
							Object[] o = (Object[]) it2.next();

							rep = new ReportAufloesbareFehlmengenDto();
							rep.setSArtikelBezeichnung(artikelDto.getArtikelsprDto().getCBez());
							rep.setSArtikelZusatzBezeichnung(artikelDto.getArtikelsprDto().getCZbez());
							rep.setSArtikelNummer(artikelDto.getCNr());
							rep.setIArtikelIId(artikelDto.getIId());
							rep.setSEinheit(artikelDto.getEinheitCNr());
							rep.setBLos(false);

							BestellpositionDto dto = getBestellpositionFac()
									.bestellpositionFindByPrimaryKeyOhneExc((Integer) o[2]);
							if (dto != null) {
								BestellungDto bestellungDto = getBestellungFac()
										.bestellungFindByPrimaryKey(dto.getBelegIId());
								if (dto.getTAuftragsbestaetigungstermin() != null) {
									rep.setDTermin(dto.getTAuftragsbestaetigungstermin());
								} else if (dto.getTUebersteuerterLiefertermin() != null) {
									rep.setDTermin(dto.getTUebersteuerterLiefertermin());
								} else if (bestellungDto.getDLiefertermin() != null) {
									rep.setDTermin(bestellungDto.getDLiefertermin());
								}
								rep.setSBelegnummer("BBS" + bestellungDto.getCNr());
								rep.setBdOffen((BigDecimal) o[0]);
								rep.setSABNummer(dto.getCABNummer());

								BSMahnungDto[] dtos = getBSMahnwesenFac().bsmahnungFindByBestellungIId(dto.getIId(),
										theClientDto);
								if (dtos.length > 0) {
									rep.setIMahnstufe(dtos[0].getMahnstufeIId());
								}

							} else {
								rep.setSBelegnummer("BS not found");
							}

							cData.add(rep);

						}
						hmVorhandeneBestelltEintraege.put(fm.getArtikel_i_id(), "");
						closeSession(session2);
					}
				}
			}
			Collections.sort(cData, new ComparatorAufloesbareFehlmengen(iSortierung.intValue()));
			data = new Object[cData.size()][AFM_SPALTENANZAHL];
			i = 0;
			for (Iterator<ReportAufloesbareFehlmengenDto> iter = cData.iterator(); iter.hasNext(); i++) {
				ReportAufloesbareFehlmengenDto rep = (ReportAufloesbareFehlmengenDto) iter.next();
				data[i][AFM_ARTIKELBEZEICHNUNG] = rep.getSArtikelBezeichnung();
				data[i][AFM_ARTIKELZUSATZBEZEICHNUNG] = rep.getSArtikelZusatzBezeichnung();
				data[i][AFM_ARTIKELNUMMER] = rep.getSArtikelNummer();
				data[i][AFM_EINHEIT] = rep.getSEinheit();
				data[i][AFM_FEHLMENGE] = rep.getBdFehlmenge();
				data[i][AFM_LAGERSTAND] = rep.getBdLagerstand();
				data[i][AFM_BELEGNUMMER] = rep.getSBelegnummer().substring(1);
				data[i][AFM_BESTELLT] = rep.getBdBestellt();
				data[i][AFM_RESERVIERT] = rep.getBdReserviert();
				data[i][AFM_PROJEKT] = rep.getSProjekt();
				data[i][AFM_OFFEN] = rep.getBdOffen();
				data[i][AFM_MAHNSTUFE] = rep.getIMahnstufe();
				data[i][AFM_ABNUMMER] = rep.getSABNummer();
				data[i][AFM_STUECKLISTEBEZEICHNUNG] = rep.getSStuecklisteBezeichnung();
				data[i][AFM_STUECKLISTEZUSATZBEZEICHNUNG] = rep.getSStuecklisteZusatzBezeichnung();
				data[i][AFM_STUECKLISTENUMMER] = rep.getSStuecklisteNummer();
				data[i][AFM_TERMIN] = rep.getDTermin();
				BigDecimal bdFiktiverLagerstand = null;
				if (rep.getBdLagerstand() == null) {
					bdFiktiverLagerstand = new BigDecimal(0);
				} else {
					bdFiktiverLagerstand = rep.getBdLagerstand();
				}
				for (int y = 0; y < cData.size(); y++) {
					ReportAufloesbareFehlmengenDto rafHelper = cData.get(y);
					if (rafHelper.isBLos() && rep.isBLos()) {
						if (rafHelper.getIArtikelIId().equals(rep.getIArtikelIId())
								&& (rafHelper.getDTermin().before(rep.getDTermin())
										|| rafHelper.getDTermin().equals(rep.getDTermin()))) {
							if (rafHelper.getDTermin().equals(rep.getDTermin())) {
								if (i >= y) {
									bdFiktiverLagerstand = bdFiktiverLagerstand
											.subtract(rafHelper.getBdFehlmenge() == null ? new BigDecimal(0)
													: rafHelper.getBdFehlmenge());
								}
							} else {
								bdFiktiverLagerstand = bdFiktiverLagerstand
										.subtract(rafHelper.getBdFehlmenge() == null ? new BigDecimal(0)
												: rafHelper.getBdFehlmenge());
							}
						}
					}
				}
				data[i][AFM_FIKTIVERLAGERSTAND] = bdFiktiverLagerstand;
			}
			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			String sSortierung = null;
			boolean bSortiertNachArtikelnummer = false;
			if (iSortierung.intValue() == Helper.SORTIERUNG_NACH_IDENT) {
				sSortierung = "Sortiert nach Artikelnummer";
				bSortiertNachArtikelnummer = true;

			} else if (iSortierung.intValue() == Helper.SORTIERUNG_NACH_LOSNUMMER) {
				sSortierung = "Sortiert nach Losnummer";
			}
			mapParameter.put("P_SORTIERTNACHARTIKELNUMMER", bSortiertNachArtikelnummer);
			mapParameter.put(P_SORTIERUNG, sSortierung);
			String sFilter = null;
			if (bNurArtikelMitLagerstand.booleanValue()) {
				sFilter = "Nur Artikel mit Lagerstand";
			}
			mapParameter.put(P_FILTER, sFilter);

			mapParameter.put("P_OHNEEIGENGEFERTIGTEARTIKEL", bOhneEigengefertigteArtikel);

			initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_AUFLOESBARE_FEHLMENGEN,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
			return getReportPrint();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} finally {
			closeSession(session);
		}
	}

	private ArrayList befuelleFehlteillisteMitLossollmaterialDto(int iEbene, LosDto losDto,
			LoslagerentnahmeDto[] lolaeDtos, LossollmaterialDto sollmaterialDto, ArrayList alDaten,
			boolean bNurPositionenMitFehlmengen, TheClientDto theClientDto) {

		try {
			Object[] oZeile = new Object[FT_ANZAHL_SPALTEN];
			oZeile[FT_AUSGEGEBEN] = getFertigungFac().getAusgegebeneMenge(sollmaterialDto.getIId(), null, theClientDto);

			oZeile[FT_SOLLMENGE] = sollmaterialDto.getNMenge();

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(sollmaterialDto.getArtikelIId(),
					theClientDto);

			// Lagerstand
			BigDecimal lagerstand = new BigDecimal(0);

			if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet())) {

				for (int i = 0; i < lolaeDtos.length; i++) {
					BigDecimal lagerstandEisneLagers = getLagerFac().getLagerstandOhneExc(
							sollmaterialDto.getArtikelIId(), lolaeDtos[i].getLagerIId(), theClientDto);
					if (lagerstandEisneLagers != null) {
						lagerstand = lagerstand.add(lagerstandEisneLagers);
					}
				}
			} else {
				lagerstand = new BigDecimal(99999999);
			}
			oZeile[FT_LAGERSTAND] = lagerstand;

			oZeile[FT_FRUEHESTER_EINTREFFTERMIN] = getFertigungFac().getFruehesterEintrefftermin(artikelDto.getIId(),
					theClientDto);

			String einrueckung = "";
			for (int i = 0; i < iEbene; i++) {
				einrueckung = einrueckung + "   ";
			}

			if (sollmaterialDto.getLossollmaterialIIdOriginal() != null) {
				oZeile[FT_ERSATZTYP] = Boolean.TRUE;
			} else {
				oZeile[FT_ERSATZTYP] = Boolean.FALSE;
			}

			oZeile[FT_ARTIKELNUMMER] = einrueckung + artikelDto.getCNr();
			oZeile[FT_REFERENZNUMMER] = artikelDto.getCReferenznr();

			oZeile[FT_LAGERBEWIRTSCHAFTET] = Helper.short2Boolean(artikelDto.getBLagerbewirtschaftet());

			if (artikelDto.getArtikelsprDto() != null) {
				oZeile[FT_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
				oZeile[FT_KURZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCKbez();
			}

			BigDecimal fehlmenge = new BigDecimal(0);

			ArtikelfehlmengeDto fmDto = getFehlmengeFac().artikelfehlmengeFindByBelegartCNrBelegartPositionIIdOhneExc(
					LocaleFac.BELEGART_LOS, sollmaterialDto.getIId());
			if (fmDto != null && fmDto.getNMenge() != null) {
				fehlmenge = fmDto.getNMenge();
			}
			oZeile[FT_FEHLMENGE] = fehlmenge;

			if (iEbene > 0) {
				oZeile[FT_LOSNUMMER] = losDto.getCNr();
				oZeile[FT_LOSGROESSE] = losDto.getNLosgroesse();
				oZeile[FT_LOSSTATUS] = losDto.getStatusCNr();
				oZeile[FT_ABGELIEFERT] = getFertigungFac().getErledigteMenge(losDto.getIId(), theClientDto);
				oZeile[FT_EBENE] = new Integer(iEbene);
			}

			if (bNurPositionenMitFehlmengen == false || ((BigDecimal) oZeile[FT_SOLLMENGE])
					.subtract((BigDecimal) oZeile[FT_AUSGEGEBEN]).doubleValue() > 0) {

				// Nachsehen,ob Stueckliste
				StuecklisteDto stklDto = getStuecklisteFac()
						.stuecklisteFindByMandantCNrArtikelIIdOhneExc(sollmaterialDto.getArtikelIId(), theClientDto);

				if (stklDto != null) {
					oZeile[FT_STUECKLISTENART] = new Boolean(true);
				} else {
					oZeile[FT_STUECKLISTENART] = new Boolean(false);
				}

				alDaten.add(oZeile);

				if (stklDto != null) {
					// Offene Lose holen
					ArrayList<LosDto> lose = getFertigungFac().getLoseInFertigung(sollmaterialDto.getArtikelIId(),
							theClientDto);
					iEbene++;

					BigDecimal bdOffenenLosablieferungen = fehlmenge;

					for (int i = 0; i < lose.size(); i++) {

						BigDecimal bdErledigteMenge = getFertigungFac().getErledigteMenge(lose.get(i).getIId(),
								theClientDto);
						BigDecimal bdOffenEinesLoses = lose.get(i).getNLosgroesse().subtract(bdErledigteMenge);
						LossollmaterialDto[] sollmaterialDtos = getFertigungFac()
								.lossollmaterialFindByLosIIdOrderByISort(lose.get(i).getIId());

						if (bNurPositionenMitFehlmengen == false || (bNurPositionenMitFehlmengen == true
								&& bdOffenenLosablieferungen.doubleValue() > 0)) {
							bdOffenenLosablieferungen = bdOffenenLosablieferungen.subtract(bdOffenEinesLoses);
							for (int j = 0; j < sollmaterialDtos.length; j++) {

								alDaten = befuelleFehlteillisteMitLossollmaterialDto(iEbene, lose.get(i), lolaeDtos,
										sollmaterialDtos[j], alDaten, bNurPositionenMitFehlmengen, theClientDto);

							}

						}
					}

				}
			}

			// PJ21013
			BestellpositionDto[] bestellpositionDtos = getBestellpositionFac()
					.bestellpositionfindByArtikelOrderByTAuftragsbestaetigungstermin2(sollmaterialDto.getArtikelIId(),
							theClientDto);
			if (bestellpositionDtos != null) {

				ArrayList<Object[]> al = new ArrayList<Object[]>();

				for (int i = 0; i < bestellpositionDtos.length; i++) {
					if (!bestellpositionDtos[i].getBestellpositionstatusCNr()
							.equals(BestellpositionFac.BESTELLPOSITIONSTATUS_ERLEDIGT)) {

						if (bestellpositionDtos[i].getNOffeneMenge() == null
								|| bestellpositionDtos[i].getNOffeneMenge().doubleValue() > 0) {

							BestellungDto bestellungDto = getBestellungFac()
									.bestellungFindByPrimaryKey(bestellpositionDtos[i].getBestellungIId());

							if (!bestellungDto.getStatusCNr().equals(BestellungFac.BESTELLSTATUS_ERLEDIGT)) {

								if (!bestellungDto.getBestellungartCNr()
										.equals(BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR)) {

									Object[] oZeileSub = new Object[5];
									oZeileSub[0] = bestellungDto.getCNr();
									oZeileSub[1] = bestellpositionDtos[i].getNOffeneMenge();
									oZeileSub[2] = bestellpositionDtos[i].getTUebersteuerterLiefertermin();
									oZeileSub[3] = bestellpositionDtos[i].getTAuftragsbestaetigungstermin();
									oZeileSub[4] = bestellpositionDtos[i].getCABNummer();

									al.add(oZeileSub);

								}
							}

						}
					}
				}

				String[] fieldnames = new String[] { "Bestellnummer", "offeneMenge", "PosTermin", "ABTermin",
						"ABNummer" };
				Object[][] dataSub = new Object[al.size()][fieldnames.length];
				dataSub = (Object[][]) al.toArray(dataSub);

				oZeile[FT_SUBREPORT_FRUEHESTER_EINTREFFTERMIN_BS] = new LPDatenSubreport(dataSub, fieldnames);

			}

			ArrayList<LosDto> alLose = getFertigungFac().getLoseInFertigung(sollmaterialDto.getArtikelIId(),
					theClientDto);

			ArrayList<Object[]> al = new ArrayList<Object[]>();

			for (int i = 0; i < alLose.size(); i++) {

				BigDecimal bdErledigtMenge = getFertigungFac().getErledigteMenge(alLose.get(i).getIId(), theClientDto);

				BigDecimal bdOffenMenge = alLose.get(i).getNLosgroesse().subtract(bdErledigtMenge);

				if (bdOffenMenge.doubleValue() > 0) {
					Object[] oZeileSub = new Object[5];
					oZeileSub[0] = alLose.get(i).getCNr();
					oZeileSub[1] = alLose.get(i).getTProduktionsende();

					oZeileSub[2] = bdOffenMenge;

					al.add(oZeileSub);
				}
			}
			String[] fieldnames = new String[] { "Losnummer", "Produktionsende", "offeneMenge" };
			Object[][] dataSub = new Object[al.size()][fieldnames.length];
			dataSub = (Object[][]) al.toArray(dataSub);

			oZeile[FT_SUBREPORT_FRUEHESTER_EINTREFFTERMIN_LO] = new LPDatenSubreport(dataSub, fieldnames);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return alDaten;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printFehlteile(Integer losIId, boolean bNurPositionenMitFehlmengen,
			TheClientDto theClientDto) {
		this.useCase = UC_FEHLTEILE;

		ArrayList alDaten = new ArrayList();
		try {

			LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);

			LoslagerentnahmeDto[] lolaeDtos = getFertigungFac().loslagerentnahmeFindByLosIId(losIId);

			LossollmaterialDto[] sollmaterialDtos = getFertigungFac().lossollmaterialFindByLosIId(losIId);

			for (int i = 0; i < sollmaterialDtos.length; i++) {
				alDaten = befuelleFehlteillisteMitLossollmaterialDto(0, losDto, lolaeDtos, sollmaterialDtos[i], alDaten,
						bNurPositionenMitFehlmengen, theClientDto);
			}

			data = new Object[alDaten.size()][FT_ANZAHL_SPALTEN];
			data = (Object[][]) alDaten.toArray(data);

			Map<String, Object> mapParameter = new TreeMap<String, Object>();

			mapParameter = parameterMitLosinformationenFuellen(losDto, mapParameter, theClientDto);

			mapParameter.put("P_NURPOSITIONENMITFEHLMENGEN", new Boolean(bNurPositionenMitFehlmengen));

			initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_FEHLTEILE,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return getReportPrint();
	}

	private Map parameterMitLosinformationenFuellen(LosDto losDto, Map mapParameter, TheClientDto theClientDto) {
		try {
			mapParameter.put("P_ANGELEGT", new java.util.Date(losDto.getTAnlegen().getTime()));
			String sAuftragsnummer;
			String sInternerKommentar = null;
			String sKunde;
			String sKundeLieferadresse;
			Timestamp dLiefertermin;
			String sLieferart;
			String sAbteilung = null;
			String sSpediteur = null;
			Boolean bPoenale = Boolean.FALSE;
			Boolean bRoHs = Boolean.FALSE;
			if (losDto.getAuftragIId() != null) {
				// Auftrag holen
				AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(losDto.getAuftragIId());
				sAuftragsnummer = auftragDto.getCNr();
				sLieferart = getLocaleFac().lieferartFindByIIdLocaleOhneExc(auftragDto.getLieferartIId(),
						theClientDto.getLocUi(), theClientDto);

				if (auftragDto.getSpediteurIId() != null) {
					sSpediteur = getMandantFac().spediteurFindByPrimaryKey(auftragDto.getSpediteurIId())
							.getCNamedesspediteurs();
				}

				bPoenale = Helper.short2Boolean(auftragDto.getBPoenale());
				bRoHs = Helper.short2Boolean(auftragDto.getBRoHs());
				// Internen Kommentar aus dem Auftrag, abhaengig von einem
				// Mandantenparameter
				ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_KOMMENTAR_AM_FERTIGUNGSBEGLEITSCHEIN);
				short iValue = Short.parseShort(parameter.getCWert());
				boolean bDruckeKommentar = Helper.short2boolean(iValue);
				if (bDruckeKommentar) {
					sInternerKommentar = auftragDto.getXInternerkommentar();
				}
				// Kunde holen
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(),
						theClientDto);

				sKunde = kundeDto.getPartnerDto().getCName1nachnamefirmazeile1();

				KundeDto kundeDtoLieferadresse = getKundeFac()
						.kundeFindByPrimaryKey(auftragDto.getKundeIIdLieferadresse(), theClientDto);
				sKundeLieferadresse = kundeDtoLieferadresse.getPartnerDto().getCName1nachnamefirmazeile1();

				sAbteilung = kundeDto.getPartnerDto().getCName3vorname2abteilung();

				if (losDto.getAuftragpositionIId() != null) {
					AuftragpositionDto aufposDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(losDto.getAuftragpositionIId());
					if (aufposDto.getTUebersteuerbarerLiefertermin() != null) {
						dLiefertermin = aufposDto.getTUebersteuerbarerLiefertermin();
					} else {
						dLiefertermin = auftragDto.getDLiefertermin();
					}
				} else {
					dLiefertermin = auftragDto.getDLiefertermin();
				}
			} else {
				sAuftragsnummer = "";
				sKunde = "";
				sKundeLieferadresse = "";
				dLiefertermin = null;
				sLieferart = "";
				sSpediteur = "";
			}

			mapParameter.put("P_AUFTRAGNUMMER", sAuftragsnummer);
			mapParameter.put("P_AUFTRAG_INTERNERKOMMENTAR", sInternerKommentar);
			mapParameter.put("P_AUFTRAG_KUNDE_ABTEILUNG", sAbteilung);
			mapParameter.put("P_LIEFERART", sLieferart);
			mapParameter.put("P_SPEDITEUR", sSpediteur);
			mapParameter.put("P_POENALE", bPoenale);
			mapParameter.put("P_ROHS", bRoHs);
			KostenstelleDto kstDto = getSystemFac().kostenstelleFindByPrimaryKey(losDto.getKostenstelleIId());
			mapParameter.put("P_KOSTENSTELLENUMMER", kstDto.getCNr());
			mapParameter.put("P_KUNDE", sKunde);
			mapParameter.put("P_KUNDE_LIEFERADRESSE", sKundeLieferadresse);
			mapParameter.put("P_LOSGROESSE", losDto.getNLosgroesse());

			mapParameter.put("P_LOSNUMMER", losDto.getCNr());
			mapParameter.put("P_PRODUKTIONSBEGINN", losDto.getTProduktionsbeginn());
			mapParameter.put("P_PRODUKTIONSENDE", losDto.getTProduktionsende());
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(losDto.getTProduktionsende());
			mapParameter.put("P_PRODUKTIONSENDE_KW", new Integer(gc.get(Calendar.WEEK_OF_YEAR)));

			mapParameter.put("P_LIEFERTERMIN", dLiefertermin);

			// Fertigungsort
			if (losDto.getPartnerIIdFertigungsort() != null) {
				PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(losDto.getPartnerIIdFertigungsort(),
						theClientDto);
				mapParameter.put("P_FERTIGUNGSORT", partnerDto.formatTitelAnrede());
			}

			// Erstlos
			mapParameter.put("P_ERSTLOS", new Boolean(istErstlos(losDto, theClientDto)));

			ArrayList<Object> images = new ArrayList<Object>();
			String sLosStuecklisteArtikelKommentar = "";
			// Bild einfuegen
			String sMengenEinheit = "";
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stuecklisteDto = getStuecklisteFac()
						.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(), theClientDto);
				// Einheit
				if (stuecklisteDto.getArtikelDto() != null) {
					if (stuecklisteDto.getArtikelDto().getEinheitCNr() != null) {
						sMengenEinheit = stuecklisteDto.getArtikelDto().getEinheitCNr();
					}
				}
				// Abmessungen
				GeometrieDto geometrieDto = stuecklisteDto.getArtikelDto().getGeometrieDto();
				if (geometrieDto != null) {
					mapParameter.put(P_ARTIKEL_BREITE, geometrieDto.getFBreite());
					mapParameter.put(P_ARTIKEL_HOEHE, geometrieDto.getFHoehe());
					mapParameter.put(P_ARTIKEL_TIEFE, geometrieDto.getFTiefe());
				}
				// Bauform
				VerpackungDto verpackungDto = stuecklisteDto.getArtikelDto().getVerpackungDto();
				if (verpackungDto != null) {
					mapParameter.put("P_ARTIKEL_BAUFORM", verpackungDto.getCBauform());
					mapParameter.put("P_ARTIKEL_VERPACKUNGSART", verpackungDto.getCVerpackungsart());
				}

				ArtikelkommentarDto[] artikelkommentarDto = getArtikelkommentarFac()
						.artikelkommentardruckFindByArtikelIIdBelegartCNr(stuecklisteDto.getArtikelIId(),
								LocaleFac.BELEGART_LOS, theClientDto.getLocUiAsString(), theClientDto);

				if (artikelkommentarDto != null && artikelkommentarDto.length > 0) {

					for (int j = 0; j < artikelkommentarDto.length; j++) {
						if (artikelkommentarDto[j].getArtikelkommentarsprDto() != null) {
							// Text Kommentar
							if (artikelkommentarDto[j].getDatenformatCNr().trim()
									.indexOf(MediaFac.DATENFORMAT_MIMETYPEART_TEXT) != -1) {
								if (sLosStuecklisteArtikelKommentar == "") {
									sLosStuecklisteArtikelKommentar += artikelkommentarDto[j]
											.getArtikelkommentarsprDto().getXKommentar();
								} else {
									sLosStuecklisteArtikelKommentar += "\n"
											+ artikelkommentarDto[j].getArtikelkommentarsprDto().getXKommentar();
								}
							} else if (artikelkommentarDto[j].getDatenformatCNr()
									.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
									|| artikelkommentarDto[j].getDatenformatCNr()
											.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)
									|| artikelkommentarDto[j].getDatenformatCNr()
											.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)) {
								byte[] bild = artikelkommentarDto[j].getArtikelkommentarsprDto().getOMedia();
								if (bild != null) {
									java.awt.Image myImage = Helper.byteArrayToImage(bild);
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
			mapParameter.put("P_MENGENEINHEIT", sMengenEinheit);
			mapParameter.put("P_PROJEKT", losDto.getCProjekt());
			mapParameter.put("P_LOSKOMMENTAR", losDto.getCKommentar());
			mapParameter.put("P_LOSLANGTEXT", losDto.getXText());

			FertigungsgruppeDto fertGruppeDto = getStuecklisteFac()
					.fertigungsgruppeFindByPrimaryKey(losDto.getFertigungsgruppeIId());
			mapParameter.put("P_FERTIGUNGSGRUPPE", fertGruppeDto.getCBez());

			// Materialliste?
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stkDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(),
						theClientDto);

				if (sLosStuecklisteArtikelKommentar != "") {
					mapParameter.put("P_STUECKLISTEARTIKELKOMMENTAR",
							Helper.formatStyledTextForJasper(sLosStuecklisteArtikelKommentar));
				}

				mapParameter.put("P_STUECKLISTEBEZEICHNUNG", stkDto.getArtikelDto().getArtikelsprDto().getCBez());

				mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG",
						stkDto.getArtikelDto().getArtikelsprDto().getCZbez());
				mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG2",
						stkDto.getArtikelDto().getArtikelsprDto().getCZbez2());

				mapParameter.put("P_STUECKLISTEKURZBEZEICHNUNG", stkDto.getArtikelDto().getArtikelsprDto().getCKbez());
				mapParameter.put("P_STUECKLISTEREFERENZNUMMER", stkDto.getArtikelDto().getCReferenznr());

				mapParameter.put("P_STUECKLISTENUMMER", stkDto.getArtikelDto().getCNr());

				mapParameter.put("P_STUECKLISTE_GEWICHTKG", stkDto.getArtikelDto().getFGewichtkg());

				if (stkDto.getArtikelDto().getVerpackungDto() != null) {
					mapParameter.put("P_STUECKLISTE_BAUFORM", stkDto.getArtikelDto().getVerpackungDto().getCBauform());
					mapParameter.put("P_STUECKLISTE_VERPACKUNGSART",
							stkDto.getArtikelDto().getVerpackungDto().getCVerpackungsart());
				}

				if (stkDto.getArtikelDto().getGeometrieDto() != null) {
					mapParameter.put("P_STUECKLISTE_BREITETEXT",
							stkDto.getArtikelDto().getGeometrieDto().getCBreitetext());
					mapParameter.put("P_STUECKLISTE_BREITE", stkDto.getArtikelDto().getGeometrieDto().getFBreite());
					mapParameter.put("P_STUECKLISTE_HOEHE", stkDto.getArtikelDto().getGeometrieDto().getFHoehe());
					mapParameter.put("P_STUECKLISTE_TIEFE", stkDto.getArtikelDto().getGeometrieDto().getFTiefe());
				}

				// Stuecklisteneigenschaften
				StuecklisteeigenschaftDto[] stuecklisteeigenschaftDtos = getStuecklisteFac()
						.stuecklisteeigenschaftFindByStuecklisteIId(losDto.getStuecklisteIId());
				ArrayList<Object[]> al = new ArrayList<Object[]>();
				for (int j = 0; j < stuecklisteeigenschaftDtos.length; j++) {
					StuecklisteeigenschaftDto dto = stuecklisteeigenschaftDtos[j];

					Object[] o = new Object[2];
					String sStklEigenschaftArt = dto.getStuecklisteeigenschaftartDto().getCBez();
					o[0] = sStklEigenschaftArt;
					o[1] = dto.getCBez();
					al.add(o);

					// Index und Materialplatz auch einzeln an Report uebergeben
					if (sStklEigenschaftArt.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_INDEX)) {
						mapParameter.put(P_STUECKLISTENEIGENSCHAFT_INDEX, dto.getCBez());
					}
					if (sStklEigenschaftArt
							.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_MATERIALPLATZ)) {
						mapParameter.put(P_STUECKLISTENEIGENSCHAFT_MATERIALPLATZ, dto.getCBez());
					}

				}

				if (stuecklisteeigenschaftDtos.length > 0) {
					String[] fieldnames = new String[] { "F_EIGENSCHAFTART", "F_BEZEICHNUNG" };
					Object[][] dataSub = new Object[al.size()][fieldnames.length];
					dataSub = (Object[][]) al.toArray(dataSub);

					mapParameter.put("DATENSUBREPORT", new LPDatenSubreport(dataSub, fieldnames));
				}

			} else {
				mapParameter.put("P_STUECKLISTEBEZEICHNUNG", losDto.getCProjekt());
				mapParameter.put("P_STUECKLISTENUMMER",
						getTextRespectUISpr("fert.materialliste", theClientDto.getMandant(), theClientDto.getLocUi()));
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return mapParameter;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printFehlmengenAllerLose(boolean bAlleOhneEigengefertigteArtikel, int iOptionStueckliste,
			boolean bOhneBestellteArtikel, ArrayList<Integer> arLosIId, int iSortierung, boolean bNurDringende,
			Integer fertigungsgruppeIId, TheClientDto theClientDto) {
		Session session = null;
		// try {

		Map<String, Object> mapParameter = new TreeMap<String, Object>();

		this.useCase = UC_FEHLMENGEN_ALLER_LOSE;
		this.index = -1;
		SessionFactory factory = FLRSessionFactory.getFactory();
		session = factory.openSession();

		String queryString = "SELECT f, (SELECT SUM(l.n_lagerstand) FROM FLRArtikellager l WHERE l.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' AND l.flrartikel.i_id=f.flrartikel.i_id AND l.flrlager.i_loslagersort IS NOT NULL), (SELECT SUM(r.n_menge) FROM FLRArtikelreservierung r WHERE r.flrartikel.i_id=f.flrartikel.i_id),(SELECT SUM(bes.n_menge) FROM FLRArtikelbestellt bes WHERE bes.flrartikel.i_id=f.flrartikel.i_id),(SELECT spr.c_bez FROM FLRArtikellistespr as spr WHERE spr.Id.artikelliste=f.flrartikel.i_id AND spr.Id.locale='"
				+ theClientDto.getLocUiAsString()
				+ "' ),(SELECT spr.c_bez FROM FLRArtikellistespr as spr WHERE spr.Id.artikelliste=f.flrlossollmaterial.flrlos.flrstueckliste.flrartikel.i_id AND spr.Id.locale='"
				+ theClientDto.getLocUiAsString()
				+ "' ),(SELECT stkl FROM FLRStueckliste stkl WHERE stkl.artikel_i_id=f.flrartikel.i_id AND stkl.mandant_c_nr=f.flrartikel.mandant_c_nr),(SELECT SUM(fm.n_menge) FROM FLRFehlmenge fm WHERE fm.flrartikel.i_id=f.flrartikel.i_id) FROM FLRFehlmenge f WHERE 1=1 ";

		if (arLosIId != null && arLosIId.size() > 0) {

			String in = null;
			String filter = "";
			for (int i = 0; i < arLosIId.size(); i++) {
				try {

					LosDto losDto = getFertigungFac().losFindByPrimaryKey(arLosIId.get(i));
					filter += losDto.getCNr() + ", ";

					LossollmaterialDto[] sollDtos = getFertigungFac().lossollmaterialFindByLosIId(arLosIId.get(i));
					for (int j = 0; j < sollDtos.length; j++) {
						if (sollDtos[j].getArtikelIId() != null) {

							if (in == null) {
								in = sollDtos[j].getArtikelIId() + "";
							} else {
								in += "," + sollDtos[j].getArtikelIId();
							}

						}

					}

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

			if (in != null) {
				queryString += " AND f.flrartikel.i_id in (" + in + ")";
			} else {
				queryString += " AND f.flrartikel.i_id = -1";
			}
			mapParameter.put("P_FILTER", filter);

		}

		if (bNurDringende) {
			queryString += " AND f.flrlossollmaterial.b_dringend = 1";
		}

		if (iSortierung == FEHLMENGEN_ALLER_LOSE_OPTION_SORTIERUNG_BELEGNUMMER) {
			queryString += " ORDER BY f.flrlossollmaterial.flrlos.c_nr, f.flrartikel.c_nr, f.flrlossollmaterial.flrlos.t_produktionsbeginn ";

			mapParameter.put("P_SORTIERUNG", getTextRespectUISpr("fert.fehlmengenallerlose.sort.belegnummer",
					theClientDto.getMandant(), theClientDto.getLocUi()));

		} else if (iSortierung == FEHLMENGEN_ALLER_LOSE_OPTION_SORTIERUNG_ARTIKELNUMMER) {
			queryString += " ORDER BY f.flrartikel.c_nr, f.flrlossollmaterial.flrlos.t_produktionsbeginn,f.flrlossollmaterial.flrlos.c_nr ";
			mapParameter.put("P_SORTIERUNG", getTextRespectUISpr("fert.fehlmengenallerlose.sort.artikel",
					theClientDto.getMandant(), theClientDto.getLocUi()));
		} else if (iSortierung == FEHLMENGEN_ALLER_LOSE_OPTION_SORTIERUNG_BEGINNTERMIN) {
			queryString += " ORDER BY  f.flrlossollmaterial.flrlos.t_produktionsbeginn, f.flrartikel.c_nr, f.flrlossollmaterial.flrlos.c_nr ";
			mapParameter.put("P_SORTIERUNG", getTextRespectUISpr("fert.fehlmengenallerlose.sort.beginntermin",
					theClientDto.getMandant(), theClientDto.getLocUi()));
		}

		org.hibernate.Query query = session.createQuery(queryString);

		List<?> resultList = query.list();

		int i = 0;

		data = new Object[resultList.size()][FMAL_SPALTENANZAHL];
		ArrayList alListe = new ArrayList();

		Integer letzteArtikelId = null;

		BigDecimal fiktiverLagerstand = new BigDecimal(0);

		for (Iterator<?> iter = resultList.iterator(); iter.hasNext(); i++) {
			Object[] f = (Object[]) iter.next();

			BigDecimal lagerstand = (BigDecimal) f[1];
			FLRFehlmenge al = (FLRFehlmenge) f[0];
			BigDecimal reservierungen = (BigDecimal) f[2];

			BigDecimal bestellt = (BigDecimal) f[3];
			String artikel_bez = (String) f[4];
			String stkl_bez = (String) f[5];
			FLRStueckliste stkl = (FLRStueckliste) f[6];

			BigDecimal summeFehlmengen = (BigDecimal) f[7];

			if (bAlleOhneEigengefertigteArtikel == true) {
				if (stkl != null && Helper.short2boolean(stkl.getB_fremdfertigung()) == false) {
					continue;
				}
			}

			// PJ19228 Wenne es mehr Bestellungen als Fehlmengen gibt, dann
			// auslassen
			if (bOhneBestellteArtikel == true) {
				if (summeFehlmengen == null) {
					summeFehlmengen = BigDecimal.ZERO;
				}
				if (bestellt == null) {
					bestellt = BigDecimal.ZERO;
				}

				if (bestellt.doubleValue() >= summeFehlmengen.doubleValue()) {
					continue;
				}

			}

			Object[] zeile = new Object[FMAL_SPALTENANZAHL];

			zeile[FMAL_ARTIKEL_I_ID] = al.getArtikel_i_id();
			zeile[FMAL_BESTELLT] = bestellt;
			if (bNurDringende == false) {
				if (fiktiverLagerstand.doubleValue() >= 0 && letzteArtikelId != null
						&& !letzteArtikelId.equals(al.getFlrartikel().getI_id())) {
					while (alListe.size() > 0 && ((Object[]) alListe.get(alListe.size() - 1))[FMAL_ARTIKEL_I_ID]
							.equals(letzteArtikelId)) {
						alListe.remove(alListe.size() - 1);
					}
				}

				if (letzteArtikelId == null || !letzteArtikelId.equals(al.getFlrartikel().getI_id())) {
					// Offenen Bestelungen + Reservierungen
					fiktiverLagerstand = lagerstand;

				}

				if (fiktiverLagerstand == null) {
					fiktiverLagerstand = new BigDecimal(0);
				}
			}

			zeile[FMAL_ARTIKELNUMMER] = al.getFlrartikel().getC_nr();

			Long[] angebotenUndAngefragt = getAnfrageFac().getAngefragteUndAngeboteneMenge(al.getArtikel_i_id(),
					theClientDto);
			zeile[FMAL_ARTIKEL_ANZAHL_ANGEFRAGT] = angebotenUndAngefragt[0];
			zeile[FMAL_ARTIKEL_ANZAHL_ANGEBOTEN] = angebotenUndAngefragt[1];

			zeile[FMAL_ARTIKELREFERENZNUMMER] = al.getFlrartikel().getC_referenznr();
			zeile[FMAL_ARTIKELBEZEICHNUNG] = artikel_bez;

			try {
				zeile[FMAL_ARTIKELSPERREN] = getArtikelFac().getArtikelsperrenText(al.getFlrartikel().getI_id());
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			zeile[FMAL_RESERVIERT] = reservierungen;
			if (al.getFlrlossollmaterial().getFlrlos().getFlrstueckliste() != null) {

				FLRStueckliste flrstkl = al.getFlrlossollmaterial().getFlrlos().getFlrstueckliste();

				if (iOptionStueckliste == FertigungReportFac.FEHLMENGEN_ALLER_LOSE_OPTION_STUECKLISTEN_NUR_EIGENGEFERTIGTE) {
					if (flrstkl != null && Helper.short2boolean(flrstkl.getB_fremdfertigung()) == true) {
						continue;
					}
				}
				if (iOptionStueckliste == FertigungReportFac.FEHLMENGEN_ALLER_LOSE_OPTION_STUECKLISTEN_NUR_FREMDGEFERTIGTE) {
					if (flrstkl != null && Helper.short2boolean(flrstkl.getB_fremdfertigung()) == false) {
						continue;
					}
				}
				if (fertigungsgruppeIId != null) {
					if (flrstkl.getFertigungsgruppe_i_id() == null
							|| !flrstkl.getFertigungsgruppe_i_id().equals(fertigungsgruppeIId)) {
						continue;
					}
				}

				zeile[FMAL_STUECKLISTENUMMER] = al.getFlrlossollmaterial().getFlrlos().getFlrstueckliste()
						.getFlrartikel().getC_nr();
				zeile[FMAL_STUECKLISTEREFERENZNUMMER] = al.getFlrlossollmaterial().getFlrlos().getFlrstueckliste()
						.getFlrartikel().getC_referenznr();
				zeile[FMAL_STUECKLISTEBEZEICHNUNG] = stkl_bez;
			}

			zeile[FMAL_DRINGEND] = Helper.short2Boolean(al.getFlrlossollmaterial().getB_dringend());

			zeile[FMAL_BELEGNUMMER] = al.getFlrlossollmaterial().getFlrlos().getC_nr();

			if (al.getFlrlossollmaterial().getFlrlos().getFlrauftrag() != null) {
				zeile[FMAL_POENALE_AUS_AUFTRAG] = Helper
						.short2Boolean(al.getFlrlossollmaterial().getFlrlos().getFlrauftrag().getB_poenale());
			}

			zeile[FMAL_BELEGART] = LocaleFac.BELEGART_LOS;

			zeile[FMAL_PRODUKTIONSBEGINN] = al.getFlrlossollmaterial().getFlrlos().getT_produktionsbeginn();
			zeile[FMAL_PRODUKTIONSENDE] = al.getFlrlossollmaterial().getFlrlos().getT_produktionsende();

			zeile[FMAL_FEHLMENGE] = al.getN_menge();

			if (bNurDringende == false) {
				fiktiverLagerstand = fiktiverLagerstand.subtract(al.getN_menge());

				zeile[FMAL_FIKTIVERLAGERSTAND] = fiktiverLagerstand;
			}

			zeile[FMAL_LAGERSTAND] = lagerstand;

			alListe.add(zeile);
			letzteArtikelId = al.getFlrartikel().getI_id();

		}
		closeSession(session);

		ArrayList alFertig = new ArrayList();

		letzteArtikelId = null;
		for (int j = 0; j < alListe.size(); j++) {
			Object[] zeile = (Object[]) alListe.get(j);
			Integer artikelIDNext = null;

			alFertig.add(zeile);

			if (alListe.size() > j + 1) {
				Object[] zeileNext = (Object[]) alListe.get(j + 1);
				artikelIDNext = (Integer) zeileNext[FMAL_ARTIKEL_I_ID];
			}

			if (artikelIDNext == null || !artikelIDNext.equals((Integer) zeile[FMAL_ARTIKEL_I_ID])) {
				if (iSortierung == FEHLMENGEN_ALLER_LOSE_OPTION_SORTIERUNG_ARTIKELNUMMER) {
					Session session2 = FLRSessionFactory.getFactory().openSession();

					String queryStringOb = "SELECT b,(SELECT spr.c_bez FROM FLRArtikellistespr as spr WHERE spr.Id.artikelliste=b.flrartikel.i_id AND spr.Id.locale='"
							+ theClientDto.getLocUiAsString()
							+ "' ) FROM FLRBestellposition b WHERE b.flrbestellung.bestellungstatus_c_nr NOT IN('"
							+ BestellungFac.BESTELLSTATUS_STORNIERT + "','" + BestellungFac.BESTELLSTATUS_ERLEDIGT
							+ "') AND ( b.flrbestellung.bestellungart_c_nr='"
							+ BestellungFac.BESTELLUNGART_FREIE_BESTELLUNG_C_NR
							+ "' OR b.flrbestellung.bestellungart_c_nr='"
							+ BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR + "' ) AND b.flrartikel.i_id="
							+ (Integer) zeile[FMAL_ARTIKEL_I_ID] + " ORDER BY b.flrbestellung.c_nr";

					org.hibernate.Query queryOb = session2.createQuery(queryStringOb);

					List<?> resultListOb = queryOb.list();
					Iterator it2 = resultListOb.iterator();
					while (it2.hasNext()) {

						Object[] bp = (Object[]) it2.next();

						String artikel_bez_bestpos = (String) bp[1];

						FLRBestellposition bespos = (FLRBestellposition) bp[0];
						Object[] zeileBestpos = new Object[FMAL_SPALTENANZAHL];

						zeileBestpos[FMAL_BELEGNUMMER] = bespos.getFlrbestellung().getC_nr();
						zeileBestpos[FMAL_ARTIKELNUMMER] = bespos.getFlrartikel().getC_nr();

						Long[] angebotenUndAngefragt = getAnfrageFac()
								.getAngefragteUndAngeboteneMenge(bespos.getFlrartikel().getI_id(), theClientDto);
						zeileBestpos[FMAL_ARTIKEL_ANZAHL_ANGEFRAGT] = angebotenUndAngefragt[0];
						zeileBestpos[FMAL_ARTIKEL_ANZAHL_ANGEBOTEN] = angebotenUndAngefragt[1];

						zeileBestpos[FMAL_ARTIKELREFERENZNUMMER] = bespos.getFlrartikel().getC_referenznr();
						zeileBestpos[FMAL_ARTIKELBEZEICHNUNG] = artikel_bez_bestpos;

						if (bespos.getN_offenemenge() == null) {
							zeileBestpos[FMAL_OFFEN] = bespos.getN_menge();
						} else if (bespos.getN_offenemenge().doubleValue() > 0) {
							zeileBestpos[FMAL_OFFEN] = bespos.getN_offenemenge();
						} else {
							continue;
						}

						zeileBestpos[FMAL_MAHNSTUFE] = bespos.getFlrbestellung().getMahnstufe_i_id();

						zeileBestpos[FMAL_ABNUMMER] = bespos.getC_abnummer();

						zeileBestpos[FMAL_ABTERMIN] = bespos.getT_auftragsbestaetigungstermin();

						if (bespos.getT_uebersteuerterliefertermin() != null) {
							zeileBestpos[FMAL_TERMIN] = bespos.getT_uebersteuerterliefertermin();
						} else {
							zeileBestpos[FMAL_TERMIN] = bespos.getFlrbestellung().getT_liefertermin();
						}

						String lieferant = bespos.getFlrbestellung().getFlrlieferant().getFlrpartner()
								.getC_name1nachnamefirmazeile1();
						if (bespos.getFlrbestellung().getFlrlieferant().getFlrpartner()
								.getC_name2vornamefirmazeile2() != null) {
							lieferant = lieferant + " " + bespos.getFlrbestellung().getFlrlieferant().getFlrpartner()
									.getC_name2vornamefirmazeile2();
						}
						zeileBestpos[FMAL_LIEFERANT] = lieferant;

						zeileBestpos[FMAL_ARTIKEL_I_ID] = (Integer) zeile[FMAL_ARTIKEL_I_ID];
						zeileBestpos[FMAL_BELEGART] = LocaleFac.BELEGART_BESTELLUNG;
						alFertig.add(zeileBestpos);

					}

					session2.close();
				}
			}
			letzteArtikelId = (Integer) zeile[FMAL_ARTIKEL_I_ID];
		}

		Object[][] returnArray = new Object[alFertig.size()][FMAL_SPALTENANZAHL];
		data = (Object[][]) alFertig.toArray(returnArray);

		String optionStueckliste = getTextRespectUISpr("fert.report.fehlmengenallerlose.art.option.alle",
				theClientDto.getMandant(), theClientDto.getLocUi());

		if (iOptionStueckliste == FEHLMENGEN_ALLER_LOSE_OPTION_STUECKLISTEN_NUR_EIGENGEFERTIGTE) {
			optionStueckliste = getTextRespectUISpr("fert.report.fehlmengenallerlose.art.option.nureigengefertigte",
					theClientDto.getMandant(), theClientDto.getLocUi());
		} else if (iOptionStueckliste == FEHLMENGEN_ALLER_LOSE_OPTION_STUECKLISTEN_NUR_FREMDGEFERTIGTE) {
			optionStueckliste = getTextRespectUISpr("fert.report.fehlmengenallerlose.art.option.nurfremdgefertigte",
					theClientDto.getMandant(), theClientDto.getLocUi());
		}

		mapParameter.put("P_OPTION_STUECKLISTE", optionStueckliste);

		mapParameter.put("P_ALLEOHNEEIGENGEFERTIGTEARTIKEL", new Boolean(bAlleOhneEigengefertigteArtikel));

		mapParameter.put("P_OHNEBESTELLTEARTIKEL", new Boolean(bOhneBestellteArtikel));

		mapParameter.put("P_NUR_DRINGENDE", new Boolean(bNurDringende));

		try {
			if (fertigungsgruppeIId != null) {
				mapParameter.put("P_FERTIGUNGSGRUPPE",
						getStuecklisteFac().fertigungsgruppeFindByPrimaryKey(fertigungsgruppeIId).getCBez());
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_FEHLMENGEN_ALLER_LOSE,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();

	}

	private Object[] befuelleVergleichMitStuecklisteMitArtikelDaten(Integer artikelIId, Integer montageartIId,
			TheClientDto theClientDto) {
		ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(artikelIId, theClientDto);
		return befuelleVergleichMitStuecklisteMitArtikelDaten(aDto, montageartIId, theClientDto);
	}

	private Object[] befuelleVergleichMitStuecklisteMitArtikelDaten(ArtikelDto aDto, Integer montageartIId,
			TheClientDto theClientDto) {
		Object[] oZeile = new Object[VMS_SPALTENANZAHL];
		oZeile[VMS_ARTIKELNUMMER] = aDto.getCNr();
		if (aDto.getArtikelsprDto() != null) {
			oZeile[VMS_BEZEICHNUNG] = aDto.getArtikelsprDto().getCBez();
			oZeile[VMS_KURZBEZEICHNUNG] = aDto.getArtikelsprDto().getCKbez();
			oZeile[VMS_ZUSATZBEZEICHNUNG] = aDto.getArtikelsprDto().getCZbez();
			oZeile[VMS_ZUSATZBEZEICHNUNG2] = aDto.getArtikelsprDto().getCZbez2();

		}

		try {
			oZeile[VMS_MONTAGEART] = getStuecklisteFac().montageartFindByPrimaryKey(montageartIId, theClientDto)
					.getCBez();
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return oZeile;
	}

	private ArrayList<StuecklistepositionDto> addHilfsstuecklisten(StuecklisteDto stklDtoI,
			StuecklistepositionDto stklPosDto, ArrayList<StuecklistepositionDto> alPositionenAusStueckliste,
			TheClientDto theClientDto) {

		StuecklistepositionDto[] stklPosDtos = getStuecklisteFac()
				.stuecklistepositionFindByStuecklisteIId(stklDtoI.getIId(), theClientDto);
		for (int j = 0; j < stklPosDtos.length; j++) {

			try {
				BigDecimal zielmenge = Helper.rundeKaufmaennisch(getStuecklisteFac()
						.berechneZielmenge(stklPosDtos[j].getIId(), theClientDto).multiply(stklPosDto.getNZielmenge()),
						5);

				stklPosDtos[j].setNZielmenge(zielmenge);

				// Wenn eine Position eine Hilfsstueckliste ist, dann muss auch
				// diese ausfegloest werden
				StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByMandantCNrArtikelIIdOhneExc(
						stklPosDtos[j].getArtikelIId(), theClientDto.getMandant());
				if (stklDto != null
						&& stklDto.getStuecklisteartCNr().equals(StuecklisteFac.STUECKLISTEART_HILFSSTUECKLISTE)) {
					alPositionenAusStueckliste = addHilfsstuecklisten(stklDto, stklPosDtos[j],
							alPositionenAusStueckliste, theClientDto);
				} else {

					ArtikelDto artikel = getArtikelFac().artikelFindByPrimaryKeySmall(stklPosDtos[j].getArtikelIId(),
							theClientDto);
					stklPosDtos[j].setArtikelDto(artikel);

					alPositionenAusStueckliste.add(stklPosDtos[j]);
				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}

		return alPositionenAusStueckliste;
	}

	private static class ArtikelPosKey implements Comparable<ArtikelPosKey> {
		public final String origArtNr;
		public final int origArtIId;
		public final int order;
		// may be null
		public final String ersatzArtNr;
		public final int ersatzArtIId;

		private ArtikelPosKey(String origArtNr, int origArtIId, int order, String ersatzArtNr, int ersatzArtIId) {
			this.origArtNr = origArtNr;
			this.origArtIId = origArtIId;
			this.order = order;
			this.ersatzArtNr = ersatzArtNr;
			this.ersatzArtIId = ersatzArtIId;
		}

		public boolean isOriginal() {
			return ersatzArtNr == null;
		}

		public ArtikelPosKey(String origArtNr, int origArtIId) {
			this(origArtNr, origArtIId, 0, null, -1);
		}

		public ArtikelPosKey getErsatz(String ersatzArtNr, int ersatzArtIId) {
			return new ArtikelPosKey(origArtNr, origArtIId, order, ersatzArtNr, ersatzArtIId);
		}

		public ArtikelPosKey next() {
			return new ArtikelPosKey(origArtNr, origArtIId, order + 1, ersatzArtNr, ersatzArtIId);
		}

		@Override
		public int compareTo(ArtikelPosKey o) {
			int origCmp = origArtNr.compareTo(o.origArtNr);
			if (origCmp != 0)
				return origCmp;
			int orderCmp = Integer.compare(order, o.order);
			if (orderCmp != 0)
				return orderCmp;
			if (ersatzArtNr == null)
				if (o.ersatzArtNr == null)
					return 0;
				else
					return -1;
			if (o.ersatzArtNr == null)
				return 1;
			int ersatzCmp = ersatzArtNr.compareTo(o.ersatzArtNr);
			return ersatzCmp;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ArtikelPosKey other = (ArtikelPosKey) obj;
			if (ersatzArtNr == null) {
				if (other.ersatzArtNr != null)
					return false;
			} else if (!ersatzArtNr.equals(other.ersatzArtNr))
				return false;
			if (order != other.order)
				return false;
			if (origArtNr == null) {
				if (other.origArtNr != null)
					return false;
			} else if (!origArtNr.equals(other.origArtNr))
				return false;
			return true;
		}

	}

	/**
	 * Helper Klasse fuer Daten Array plus Lossollmaterial
	 *
	 */
	private static class VergleichMitStuecklisteDaten {
		public final Object[] daten;
		public final LossollmaterialDto dto;

		public VergleichMitStuecklisteDaten(Object[] daten, LossollmaterialDto dto) {
			this.daten = daten;
			this.dto = dto;
		}
	}

	private SortedMap<Integer, List<LossollmaterialDto>> getLosSollmaterialGroupedByOriginalIID(Integer losIId)
			throws RemoteException {
		SortedMap<Integer, List<LossollmaterialDto>> sollmaterialGroupedErsatztypen = new TreeMap<Integer, List<LossollmaterialDto>>();
		LossollmaterialDto[] losPosDto = getFertigungFac().lossollmaterialFindByLosIId(losIId);
		// Gruppiere LosPositionen
		for (LossollmaterialDto losPos : losPosDto) {
			Integer origID = losPos.getLossollmaterialIIdOriginal();
			boolean orig = false;
			// Falls origID == null dann ist Originaltyp
			if (origID == null) {
				origID = losPos.getIId();
				orig = true;
			}
			LinkedList<LossollmaterialDto> lis = (LinkedList<LossollmaterialDto>) sollmaterialGroupedErsatztypen
					.get(origID);
			if (lis == null) {
				lis = new LinkedList<LossollmaterialDto>();
				sollmaterialGroupedErsatztypen.put(origID, lis);
			}
			if (orig)
				lis.addFirst(losPos);
			else
				lis.addLast(losPos);

		}
		return sollmaterialGroupedErsatztypen;
	}

	private boolean stklPosEqualLosPos(StuecklistepositionDto stklPos, LossollmaterialDto losPos,
			BigDecimal gesSatzgroesse) {
		if (stklPos.getCPosition() != null && losPos.getCPosition() != null) {
			// Falls positionen verwendet sind, dann muessen diese gleich sein bei gleichen
			// positionen
			if (!stklPos.getCPosition().equals(losPos.getCPosition())) {
				return false;
			}
		}
		BigDecimal bdZielmenge = stklPos.getNZielmenge();
		// SP5160
		if (bdZielmenge.doubleValue() < 0.001 && bdZielmenge.doubleValue() > 0.000001) {
			bdZielmenge = new BigDecimal("0.001");
		}
		// 3 Stellen werden angezeigt, muss nur auf 3 Stellen gleich sein.
		bdZielmenge = Helper.rundeKaufmaennisch(bdZielmenge, 3);
		gesSatzgroesse = Helper.rundeKaufmaennisch(gesSatzgroesse, 3);
		boolean isEqual = losPos.getArtikelIId().equals(stklPos.getArtikelIId())
				&& bdZielmenge.compareTo(gesSatzgroesse) == 0
				&& losPos.getMontageartIId().equals(stklPos.getMontageartIId());
		return isEqual;
	}

	private HvOptional<ArtikelPosKey> findLosPosPredicate(String artikelCNr,
			TreeMap<ArtikelPosKey, VergleichMitStuecklisteDaten> mapLosPos,
			Predicate<Entry<ArtikelPosKey, VergleichMitStuecklisteDaten>> predicate) {
		ArtikelPosKey lowerBound = new ArtikelPosKey(artikelCNr, -1);
		NavigableMap<ArtikelPosKey, VergleichMitStuecklisteDaten> tailMap = mapLosPos.tailMap(lowerBound, true);
		for (Entry<ArtikelPosKey, VergleichMitStuecklisteDaten> entry : tailMap.entrySet()) {
			if (predicate.test(entry)) {
				return HvOptional.of(entry.getKey());
			}
			if (entry.getKey().origArtNr.compareTo(artikelCNr) > 0) {
				// Artikelnummer zu hoch, nicht gefunden
				break;
			}
		}
		return HvOptional.empty();
	}

	/**
	 * Finde eine LosPos bei der Artikel und Montageart gleich sind wie bei
	 * Stueckliste
	 */
	private HvOptional<ArtikelPosKey> findLosPosUngefaehrStklPos(final StuecklistepositionDto stklPos,
			String artikelCNr, TreeMap<ArtikelPosKey, VergleichMitStuecklisteDaten> mapLosPos) {
		Predicate<Entry<ArtikelPosKey, VergleichMitStuecklisteDaten>> predicate = (entry) -> {
			LossollmaterialDto losPos = entry.getValue().dto;
			return entry.getKey().isOriginal() && losPos.getArtikelIId().equals(stklPos.getArtikelIId())
					&& losPos.getMontageartIId().equals(stklPos.getMontageartIId());
		};
		return findLosPosPredicate(artikelCNr, mapLosPos, predicate);
	}

	private HvOptional<ArtikelPosKey> findLosPosGenau(final StuecklistepositionDto stklPos, String artikelCNr,
			final TreeMap<ArtikelPosKey, VergleichMitStuecklisteDaten> smDatenByArtikelID) {
		Predicate<Entry<ArtikelPosKey, VergleichMitStuecklisteDaten>> predicate = (entry) -> {
			Object[] data = entry.getValue().daten;
			BigDecimal gesSatzgroesse = (BigDecimal) data[VMS_SOLLSATZGROESSE_GESAMT];
			return entry.getKey().isOriginal() && stklPosEqualLosPos(stklPos, entry.getValue().dto, gesSatzgroesse);
		};
		return findLosPosPredicate(artikelCNr, smDatenByArtikelID, predicate);
	}

	/**
	 * Sucht ob Ersatztypen fuer einen Artikel auch in der Stueckliste enthalten
	 * sind und setzt VMS_IN_STUECKLISTE und VMS_SOLLMENGE_STUECKLISTE
	 * 
	 * @param stklPos
	 * @param smDatenByArtikelID
	 * @param originalKey
	 */
	private void setzeErsatzLosPosInStueckliste(StuecklistepositionDto stklPos,
			NavigableMap<ArtikelPosKey, VergleichMitStuecklisteDaten> smDatenByArtikelID, ArtikelPosKey originalKey) {
		// Alle ersatztypen fuer eine Los Position sind zwischen dem Originalartikel und
		// dem n�chsten Originalartikel
		NavigableMap<ArtikelPosKey, VergleichMitStuecklisteDaten> ersatzLosPos = smDatenByArtikelID.subMap(originalKey,
				false, originalKey.next(), false);
		if (ersatzLosPos.isEmpty())
			return;
		PosersatzDto[] stklErsatzPos = getStuecklisteFac().posersatzFindByStuecklistepositionIId(stklPos.getIId());
		ErsatztypenDto[] artikelErsatztypen = getArtikelFac().ersatztypenFindByArtikelIId(originalKey.origArtIId);
		for (Map.Entry<ArtikelPosKey, VergleichMitStuecklisteDaten> ersatzTyp : ersatzLosPos.entrySet()) {
			boolean found = findErsatzTypInArtikelersatz(artikelErsatztypen, ersatzTyp.getKey().ersatzArtIId);
			if (!found) {
				// Kein genereller Artikelersatz, vielleicht nur in Stueckliste
				found = findErsatztypInStuecklisteersatz(stklErsatzPos, ersatzTyp.getKey().ersatzArtIId);
			}

			if (found) {
				Object[] oZeile = ersatzTyp.getValue().daten;
				oZeile[VMS_IN_STUECKLISTE] = Boolean.TRUE;
				oZeile[VMS_SOLLMENGE_STUECKLISTE] = BigDecimal.ZERO;
			}
		}
	}

	private boolean findErsatztypInStuecklisteersatz(PosersatzDto[] stklErsatzPos, int ersatzArtIId) {
		for (PosersatzDto ersatzDto : stklErsatzPos) {
			if (ersatzDto.getArtikelIIdErsatz() != null && ersatzDto.getArtikelIIdErsatz().intValue() == ersatzArtIId) {
				return true;
			}
		}
		return false;
	}

	private boolean findErsatzTypInArtikelersatz(ErsatztypenDto[] artikelErsatztypen, int ersatzArtIId) {
		for (ErsatztypenDto ersatz : artikelErsatztypen) {
			if (ersatz.getArtikelIIdErsatz() != null && ersatz.getArtikelIIdErsatz().intValue() == ersatzArtIId) {
				return true;
			}
		}
		return false;
	}

	/**
	 * L&ouml;st Hilfsst&uuml;cklisten auf und gibt eine Liste mit allen
	 * Stuecklistenpositionen zur&uuml;ck
	 * 
	 * @param theClientDto
	 * @param stklPosDtos
	 * @return
	 * @throws RemoteException
	 */
	private ArrayList<StuecklistepositionDto> getAllPositionenMitHilfsstuecklisten(TheClientDto theClientDto,
			StuecklistepositionDto[] stklPosDtos) throws RemoteException {
		ArrayList<StuecklistepositionDto> alPositionenAusStueckliste = new ArrayList<StuecklistepositionDto>();

		for (int j = 0; j < stklPosDtos.length; j++) {

			BigDecimal zielmenge = Helper.rundeKaufmaennisch(
					getStuecklisteFac().berechneZielmenge(stklPosDtos[j].getIId(), theClientDto), 5);

			stklPosDtos[j].setNZielmenge(zielmenge);
			// SP5089
			// Wenn eine Position eine Hilfsstueckliste ist, dann muss auch
			// diese aufgeloest werden
			StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByMandantCNrArtikelIIdOhneExc(
					stklPosDtos[j].getArtikelIId(), theClientDto.getMandant());
			if (stklDto != null
					&& stklDto.getStuecklisteartCNr().equals(StuecklisteFac.STUECKLISTEART_HILFSSTUECKLISTE)) {
				alPositionenAusStueckliste = addHilfsstuecklisten(stklDto, stklPosDtos[j], alPositionenAusStueckliste,
						theClientDto);
			} else {
				ArtikelDto artikel = getArtikelFac().artikelFindByPrimaryKeySmall(stklPosDtos[j].getArtikelIId(),
						theClientDto);
				stklPosDtos[j].setArtikelDto(artikel);
				alPositionenAusStueckliste.add(stklPosDtos[j]);
			}

		}
		return alPositionenAusStueckliste;
	}

	private TreeMap<ArtikelPosKey, VergleichMitStuecklisteDaten> vglMitStklBuildLosdaten(TheClientDto theClientDto,
			LosDto losDto, Iterator<Entry<Integer, List<LossollmaterialDto>>> losSollMatIterator)
			throws RemoteException {
		TreeMap<ArtikelPosKey, VergleichMitStuecklisteDaten> smDatenByArtikelID = new TreeMap<ArtikelPosKey, VergleichMitStuecklisteDaten>();
		while (losSollMatIterator.hasNext()) {
			Entry<Integer, List<LossollmaterialDto>> entry = losSollMatIterator.next();
			Iterator<LossollmaterialDto> losPosIterator = entry.getValue().iterator();
			LossollmaterialDto originalLosPos = null;
			BigDecimal gesMenge = BigDecimal.ZERO;
			ArtikelPosKey originalKey = null;
			while (losPosIterator.hasNext()) {
				LossollmaterialDto losPos = losPosIterator.next();
				Object[] oZeile = befuelleVergleichMitStuecklisteMitArtikelDaten(losPos.getArtikelIId(),
						losPos.getMontageartIId(), theClientDto);
				if (originalLosPos == null) {
					originalLosPos = losPos;
					originalKey = new ArtikelPosKey(oZeile[VMS_ARTIKELNUMMER].toString(), losPos.getArtikelIId());
					while (smDatenByArtikelID.containsKey(originalKey)) {
						originalKey = originalKey.next();
					}
				}
				gesMenge = gesMenge.add(losPos.getNMenge());

				oZeile[VMS_EINHEIT] = losPos.getEinheitCNr();
				oZeile[VMS_IN_LOS] = Boolean.TRUE;

				oZeile[VMS_SOLLMENGE_LOS] = losPos.getNMenge();

				oZeile[VMS_SOLLSATZGROESSE] = losPos.getNMenge().divide(losDto.getNLosgroesse(),
						BigDecimal.ROUND_HALF_EVEN);

				oZeile[VMS_AUSGABEMENGE] = getFertigungFac().getAusgegebeneMenge(losPos.getIId(), null, theClientDto);
				oZeile[VMS_AUSGABEPREIS] = getFertigungFac().getAusgegebeneMengePreis(losPos.getIId(), null,
						theClientDto);

				// kann auf != pruefen, weil nur interessant ist ob gleiche Referenz
				oZeile[VMS_IS_ERSATZARTIKEL] = losPos != originalLosPos;
				oZeile[VMS_IN_STUECKLISTE] = Boolean.FALSE; // wird spaeter auf true gesetzt falls gefunden

				if (losPos != originalLosPos) {
					ArtikelPosKey ersatzPosKey = originalKey.getErsatz(oZeile[VMS_ARTIKELNUMMER].toString(),
							losPos.getArtikelIId());
					oZeile[VMS_ARTIKELNUMMER] = " " + oZeile[VMS_ARTIKELNUMMER];
					smDatenByArtikelID.put(ersatzPosKey, new VergleichMitStuecklisteDaten(oZeile, losPos));
				} else {
					smDatenByArtikelID.put(originalKey, new VergleichMitStuecklisteDaten(oZeile, losPos));
				}
			}

			BigDecimal satzGroesse = gesMenge.divide(losDto.getNLosgroesse(), 5, BigDecimal.ROUND_HALF_EVEN);

			smDatenByArtikelID.get(originalKey).daten[VMS_SOLLSATZGROESSE_GESAMT] = satzGroesse;
		}
		return smDatenByArtikelID;
	}

	/**
	 * Fuegt alle im Los nicht gefunendenen Positionen zum Vergleich hinzu
	 * 
	 * @param theClientDto
	 * @param alPositionenAusStueckliste
	 * @param smDatenByArtikelID
	 * @param smDatenByArtikelID2
	 */
	private void vglMitStklPruefeNichtGefundenePositionen(TheClientDto theClientDto,
			ArrayList<StuecklistepositionDto> alPositionenAusStueckliste,
			TreeMap<ArtikelPosKey, VergleichMitStuecklisteDaten> smDatenByArtikelID,
			TreeMap<ArtikelPosKey, VergleichMitStuecklisteDaten> verbleibendeLosPos) {
		Iterator<StuecklistepositionDto> it = alPositionenAusStueckliste.iterator();

		while (it.hasNext()) {

			StuecklistepositionDto stklPos = it.next();

			VergleichMitStuecklisteDaten daten = null;
			Object[] oZeile = befuelleVergleichMitStuecklisteMitArtikelDaten(stklPos.getArtikelDto(),
					stklPos.getMontageartIId(), theClientDto);
			oZeile[VMS_EINHEIT] = stklPos.getEinheitCNr();
			oZeile[VMS_IN_LOS] = Boolean.FALSE;
			boolean inLos = false;
			// Kann sein, dass nur Sollmenge unterschiedlich ist, versuche passende
			// LosPosition zu finden
			HvOptional<ArtikelPosKey> keyOfLosPos = findLosPosUngefaehrStklPos(stklPos,
					oZeile[VMS_ARTIKELNUMMER].toString(), verbleibendeLosPos);
			if (keyOfLosPos.isPresent()) {
				setzeErsatzLosPosInStueckliste(stklPos, smDatenByArtikelID, keyOfLosPos.get());
				daten = verbleibendeLosPos.get(keyOfLosPos.get());
				oZeile = daten.daten;
				inLos = true;
				verbleibendeLosPos.remove(keyOfLosPos.get());
			}

			oZeile[VMS_IN_STUECKLISTE] = Boolean.TRUE;
			oZeile[VMS_IS_ERSATZARTIKEL] = Boolean.FALSE;
			oZeile[VMS_SOLLMENGE_STUECKLISTE] = stklPos.getNZielmenge();

			// Falls doch Losposition dazu gefunden, nicht doppelt hinzufuegen
			if (!inLos) {
				ArtikelPosKey key = new ArtikelPosKey(oZeile[VMS_ARTIKELNUMMER].toString(), stklPos.getArtikelIId());
				while (smDatenByArtikelID.containsKey(key)) {
					key = key.next();
				}
				if (daten == null) {
					daten = new VergleichMitStuecklisteDaten(oZeile, null);
				}
				smDatenByArtikelID.put(key, daten);
			}

		}
	}

	private TreeMap<ArtikelPosKey, VergleichMitStuecklisteDaten> vglMitStklPruefeStuecklistePositionen(
			TreeMap<ArtikelPosKey, VergleichMitStuecklisteDaten> smDatenByArtikelID,
			List<StuecklistepositionDto> stklPositionen) {

		TreeMap<ArtikelPosKey, VergleichMitStuecklisteDaten> mapVerbleibendeLosDaten = new TreeMap<ArtikelPosKey, VergleichMitStuecklisteDaten>(
				smDatenByArtikelID);

		Iterator<StuecklistepositionDto> iter = stklPositionen.iterator();
		while (iter.hasNext()) {

			StuecklistepositionDto stuecklistepositionDto = iter.next();

			HvOptional<ArtikelPosKey> found = findLosPosGenau(stuecklistepositionDto,
					stuecklistepositionDto.getArtikelDto().getCNr(), smDatenByArtikelID);
			if (found.isPresent()) {
				mapVerbleibendeLosDaten.remove(found.get());
				// wenn Menge und Artikel und Montageart gleich oder Position gleich

				// d.h. diese Position ist Sowohl in STKL als auch in Los
				VergleichMitStuecklisteDaten origZeile = smDatenByArtikelID.get(found.get());
				origZeile.daten[VMS_IN_STUECKLISTE] = Boolean.TRUE;
				origZeile.daten[VMS_SOLLMENGE_STUECKLISTE] = stuecklistepositionDto.getNZielmenge();
				smDatenByArtikelID.put(found.get(), origZeile);

				setzeErsatzLosPosInStueckliste(stuecklistepositionDto, smDatenByArtikelID, found.get());
				iter.remove();
			}

		}
		return mapVerbleibendeLosDaten;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printVergleichMitStueckliste(Integer losIId, TheClientDto theClientDto) {
		this.useCase = UC_VERGLEICH_MIT_STUECKLISTE;
		this.index = -1;
		try {
			LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);

			StuecklistepositionDto[] stklPosDtos = getStuecklisteFac()
					.stuecklistepositionFindByStuecklisteIId(losDto.getStuecklisteIId(), theClientDto);

			ArrayList<StuecklistepositionDto> alPositionenAusStueckliste = getAllPositionenMitHilfsstuecklisten(
					theClientDto, stklPosDtos);

			TreeMap<ArtikelPosKey, VergleichMitStuecklisteDaten> smDatenByArtikelID;

			SortedMap<Integer, List<LossollmaterialDto>> sollmaterialGroupedErsatztypen = getLosSollmaterialGroupedByOriginalIID(
					losIId);

			Iterator<Entry<Integer, List<LossollmaterialDto>>> losSollMatIterator = sollmaterialGroupedErsatztypen
					.entrySet().iterator();
			smDatenByArtikelID = vglMitStklBuildLosdaten(theClientDto, losDto, losSollMatIterator);

			// Hier werden alle gefundenen aus origLosMap geloescht
			TreeMap<ArtikelPosKey, VergleichMitStuecklisteDaten> uebrigeLosDaten = vglMitStklPruefeStuecklistePositionen(
					smDatenByArtikelID, alPositionenAusStueckliste);

			// Positionen nur in Stueckliste
			vglMitStklPruefeNichtGefundenePositionen(theClientDto, alPositionenAusStueckliste, smDatenByArtikelID,
					uebrigeLosDaten);

			// Nach Artikel sortieren
			// Ist schon sortiert

			data = new Object[smDatenByArtikelID.size()][VMS_SPALTENANZAHL];
			int zeile = 0;
			for (VergleichMitStuecklisteDaten vms : smDatenByArtikelID.values()) {
				data[zeile] = vms.daten;
				zeile++;
			}

			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			mapParameter.put("P_ANGELEGT", new java.util.Date(losDto.getTAnlegen().getTime()));
			String sAuftragsnummer;
			String sInternerKommentar = null;
			String sKunde;
			String sKundeLieferadresse;
			Timestamp dLiefertermin;
			String sLieferart;
			String sAbteilung = null;
			String sSpediteur = null;
			Boolean bPoenale = Boolean.FALSE;
			Boolean bRoHs = Boolean.FALSE;
			if (losDto.getAuftragIId() != null) {
				// Auftrag holen
				AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(losDto.getAuftragIId());
				sAuftragsnummer = auftragDto.getCNr();
				sLieferart = getLocaleFac().lieferartFindByIIdLocaleOhneExc(auftragDto.getLieferartIId(),
						theClientDto.getLocUi(), theClientDto);

				if (auftragDto.getSpediteurIId() != null) {
					sSpediteur = getMandantFac().spediteurFindByPrimaryKey(auftragDto.getSpediteurIId())
							.getCNamedesspediteurs();
				}

				bPoenale = Helper.short2Boolean(auftragDto.getBPoenale());
				bRoHs = Helper.short2Boolean(auftragDto.getBRoHs());
				// Internen Kommentar aus dem Auftrag, abhaengig von einem
				// Mandantenparameter
				ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_KOMMENTAR_AM_FERTIGUNGSBEGLEITSCHEIN);
				short iValue = Short.parseShort(parameter.getCWert());
				boolean bDruckeKommentar = Helper.short2boolean(iValue);
				if (bDruckeKommentar) {
					sInternerKommentar = auftragDto.getXInternerkommentar();
				}
				// Kunde holen
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(),
						theClientDto);

				sKunde = kundeDto.getPartnerDto().getCName1nachnamefirmazeile1();

				KundeDto kundeDtoLieferadresse = getKundeFac()
						.kundeFindByPrimaryKey(auftragDto.getKundeIIdLieferadresse(), theClientDto);
				sKundeLieferadresse = kundeDtoLieferadresse.getPartnerDto().getCName1nachnamefirmazeile1();

				sAbteilung = kundeDto.getPartnerDto().getCName3vorname2abteilung();

				if (losDto.getAuftragpositionIId() != null) {
					AuftragpositionDto aufposDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(losDto.getAuftragpositionIId());
					if (aufposDto.getTUebersteuerbarerLiefertermin() != null) {
						dLiefertermin = aufposDto.getTUebersteuerbarerLiefertermin();
					} else {
						dLiefertermin = auftragDto.getDLiefertermin();
					}
				} else {
					dLiefertermin = auftragDto.getDLiefertermin();
				}
			} else {
				sAuftragsnummer = "";

				if (losDto.getKundeIId() != null) {
					KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(losDto.getKundeIId(), theClientDto);

					sKunde = kundeDto.getPartnerDto().getCName1nachnamefirmazeile1();
				} else {
					sKunde = "";
				}

				sKundeLieferadresse = "";
				dLiefertermin = null;
				sLieferart = "";
				sSpediteur = "";
			}

			mapParameter.put("P_AUFTRAGNUMMER", sAuftragsnummer);
			mapParameter.put("P_AUFTRAG_INTERNERKOMMENTAR", sInternerKommentar);
			mapParameter.put("P_AUFTRAG_KUNDE_ABTEILUNG", sAbteilung);
			mapParameter.put("P_LIEFERART", sLieferart);
			mapParameter.put("P_SPEDITEUR", sSpediteur);
			mapParameter.put("P_POENALE", bPoenale);
			mapParameter.put("P_ROHS", bRoHs);
			KostenstelleDto kstDto = getSystemFac().kostenstelleFindByPrimaryKey(losDto.getKostenstelleIId());
			mapParameter.put("P_KOSTENSTELLENUMMER", kstDto.getCNr());
			mapParameter.put("P_KUNDE", sKunde);
			mapParameter.put("P_KUNDE_LIEFERADRESSE", sKundeLieferadresse);
			mapParameter.put("P_LOSGROESSE", losDto.getNLosgroesse());

			mapParameter.put("P_LOSKLASSEN", getLosLosKlassenAlsString(losDto.getIId()));

			mapParameter.put("P_LOSNUMMER", losDto.getCNr());
			mapParameter.put("P_PRODUKTIONSBEGINN", losDto.getTProduktionsbeginn());
			mapParameter.put("P_PRODUKTIONSENDE", losDto.getTProduktionsende());
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(losDto.getTProduktionsende());
			mapParameter.put("P_PRODUKTIONSENDE_KW", new Integer(gc.get(Calendar.WEEK_OF_YEAR)));

			mapParameter.put("P_LIEFERTERMIN", dLiefertermin);

			// Fertigungsort
			if (losDto.getPartnerIIdFertigungsort() != null) {
				PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(losDto.getPartnerIIdFertigungsort(),
						theClientDto);
				mapParameter.put("P_FERTIGUNGSORT", partnerDto.formatTitelAnrede());
			}

			// Erstlos
			mapParameter.put("P_ERSTLOS", new Boolean(istErstlos(losDto, theClientDto)));

			ArrayList<Object> images = new ArrayList<Object>();
			String sLosStuecklisteArtikelKommentar = "";
			// Bild einfuegen
			String sMengenEinheit = "";
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stuecklisteDto = getStuecklisteFac()
						.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(), theClientDto);
				// Einheit
				if (stuecklisteDto.getArtikelDto() != null) {
					if (stuecklisteDto.getArtikelDto().getEinheitCNr() != null) {
						sMengenEinheit = stuecklisteDto.getArtikelDto().getEinheitCNr();
					}
				}

				// Sperren
				String sperren = getArtikelFac().getArtikelsperrenText(stuecklisteDto.getArtikelIId());

				mapParameter.put("P_STUECKLISTESPERRSTATUS", sperren);

				// Abmessungen
				GeometrieDto geometrieDto = stuecklisteDto.getArtikelDto().getGeometrieDto();
				if (geometrieDto != null) {
					mapParameter.put(P_ARTIKEL_BREITE, geometrieDto.getFBreite());
					mapParameter.put(P_ARTIKEL_HOEHE, geometrieDto.getFHoehe());
					mapParameter.put(P_ARTIKEL_TIEFE, geometrieDto.getFTiefe());
				}
				// Bauform
				VerpackungDto verpackungDto = stuecklisteDto.getArtikelDto().getVerpackungDto();
				if (verpackungDto != null) {
					mapParameter.put("P_ARTIKEL_BAUFORM", verpackungDto.getCBauform());
					mapParameter.put("P_ARTIKEL_VERPACKUNGSART", verpackungDto.getCVerpackungsart());
				}

				mapParameter.put("P_INDEX", stuecklisteDto.getArtikelDto().getCIndex());
				mapParameter.put("P_REVISION", stuecklisteDto.getArtikelDto().getCRevision());

				ArtikelkommentarDto[] artikelkommentarDto = getArtikelkommentarFac()
						.artikelkommentardruckFindByArtikelIIdBelegartCNr(stuecklisteDto.getArtikelIId(),
								LocaleFac.BELEGART_LOS, theClientDto.getLocUiAsString(), theClientDto);

				if (artikelkommentarDto != null && artikelkommentarDto.length > 0) {

					for (int j = 0; j < artikelkommentarDto.length; j++) {
						if (artikelkommentarDto[j].getArtikelkommentarsprDto() != null) {
							// Text Kommentar
							if (artikelkommentarDto[j].getDatenformatCNr().trim()
									.indexOf(MediaFac.DATENFORMAT_MIMETYPEART_TEXT) != -1) {
								if (sLosStuecklisteArtikelKommentar == "") {
									sLosStuecklisteArtikelKommentar += artikelkommentarDto[j]
											.getArtikelkommentarsprDto().getXKommentar();
								} else {
									sLosStuecklisteArtikelKommentar += "\n"
											+ artikelkommentarDto[j].getArtikelkommentarsprDto().getXKommentar();
								}
							} else if (artikelkommentarDto[j].getDatenformatCNr()
									.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
									|| artikelkommentarDto[j].getDatenformatCNr()
											.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)
									|| artikelkommentarDto[j].getDatenformatCNr()
											.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)) {
								byte[] bild = artikelkommentarDto[j].getArtikelkommentarsprDto().getOMedia();
								if (bild != null) {
									java.awt.Image myImage = Helper.byteArrayToImage(bild);
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
			mapParameter.put("P_MENGENEINHEIT", sMengenEinheit);
			mapParameter.put("P_PROJEKT", losDto.getCProjekt());
			mapParameter.put("P_LOSKOMMENTAR", losDto.getCKommentar());
			mapParameter.put("P_LOSLANGTEXT", losDto.getXText());

			FertigungsgruppeDto fertGruppeDto = getStuecklisteFac()
					.fertigungsgruppeFindByPrimaryKey(losDto.getFertigungsgruppeIId());
			mapParameter.put("P_FERTIGUNGSGRUPPE", fertGruppeDto.getCBez());

			// Materialliste?

			StuecklisteDto stkDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(),
					theClientDto);

			if (sLosStuecklisteArtikelKommentar != "") {
				mapParameter.put("P_STUECKLISTEARTIKELKOMMENTAR",
						Helper.formatStyledTextForJasper(sLosStuecklisteArtikelKommentar));
			}

			mapParameter.put("P_STUECKLISTEBEZEICHNUNG", stkDto.getArtikelDto().getArtikelsprDto().getCBez());

			mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG", stkDto.getArtikelDto().getArtikelsprDto().getCZbez());
			mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG2", stkDto.getArtikelDto().getArtikelsprDto().getCZbez2());

			mapParameter.put("P_STUECKLISTEKURZBEZEICHNUNG", stkDto.getArtikelDto().getArtikelsprDto().getCKbez());
			mapParameter.put("P_STUECKLISTEREFERENZNUMMER", stkDto.getArtikelDto().getCReferenznr());

			mapParameter.put("P_STUECKLISTENUMMER", stkDto.getArtikelDto().getCNr());

			mapParameter.put("P_STUECKLISTE_GEWICHTKG", stkDto.getArtikelDto().getFGewichtkg());

			if (stkDto.getArtikelDto().getVerpackungDto() != null) {
				mapParameter.put("P_STUECKLISTE_BAUFORM", stkDto.getArtikelDto().getVerpackungDto().getCBauform());
				mapParameter.put("P_STUECKLISTE_VERPACKUNGSART",
						stkDto.getArtikelDto().getVerpackungDto().getCVerpackungsart());
			}

			if (stkDto.getArtikelDto().getGeometrieDto() != null) {
				mapParameter.put("P_STUECKLISTE_BREITETEXT", stkDto.getArtikelDto().getGeometrieDto().getCBreitetext());
				mapParameter.put("P_STUECKLISTE_BREITE", stkDto.getArtikelDto().getGeometrieDto().getFBreite());
				mapParameter.put("P_STUECKLISTE_HOEHE", stkDto.getArtikelDto().getGeometrieDto().getFHoehe());
				mapParameter.put("P_STUECKLISTE_TIEFE", stkDto.getArtikelDto().getGeometrieDto().getFTiefe());
			}

			// Stuecklisteneigenschaften
			StuecklisteeigenschaftDto[] stuecklisteeigenschaftDtos = getStuecklisteFac()
					.stuecklisteeigenschaftFindByStuecklisteIId(losDto.getStuecklisteIId());
			ArrayList<Object[]> al = new ArrayList<Object[]>();
			for (int j = 0; j < stuecklisteeigenschaftDtos.length; j++) {
				StuecklisteeigenschaftDto dto = stuecklisteeigenschaftDtos[j];

				Object[] o = new Object[2];
				String sStklEigenschaftArt = dto.getStuecklisteeigenschaftartDto().getCBez();
				o[0] = sStklEigenschaftArt;
				o[1] = dto.getCBez();
				al.add(o);

				// Index und Materialplatz auch einzeln an Report uebergeben
				if (sStklEigenschaftArt.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_INDEX)) {
					mapParameter.put(P_STUECKLISTENEIGENSCHAFT_INDEX, dto.getCBez());
				}
				if (sStklEigenschaftArt.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_MATERIALPLATZ)) {
					mapParameter.put(P_STUECKLISTENEIGENSCHAFT_MATERIALPLATZ, dto.getCBez());
				}

			}

			if (stuecklisteeigenschaftDtos.length > 0) {
				String[] fieldnames = new String[] { "F_EIGENSCHAFTART", "F_BEZEICHNUNG" };
				Object[][] dataSub = new Object[al.size()][fieldnames.length];
				dataSub = (Object[][]) al.toArray(dataSub);

				mapParameter.put("DATENSUBREPORT", new LPDatenSubreport(dataSub, fieldnames));
			}

			initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_VERGLEICH_MIT_STUECKLISTE,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		} catch (Exception t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, t);

		}

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printMaterialliste(Integer losIId, ArrayList<Integer> selektiertePositionen,
			boolean bSortiertNachOrginialArtikelnummer, TheClientDto theClientDto) {
		this.useCase = UC_MATERIALLISTE;
		this.index = -1;

		Map<String, Object> mapParameter = new TreeMap<String, Object>();

		try {
			LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);

			LoslagerentnahmeDto[] loslagerDtos = getFertigungFac().loslagerentnahmeFindByLosIId(losDto.getIId());

			mapParameter.put("P_ANGELEGT", new java.util.Date(losDto.getTAnlegen().getTime()));
			String sAuftragsnummer;
			String sInternerKommentar = null;
			String sKunde;
			String sKundeLieferadresse;
			Timestamp dLiefertermin;
			String sLieferart;
			String sAbteilung = null;
			String sSpediteur = null;
			Boolean bPoenale = Boolean.FALSE;
			Boolean bRoHs = Boolean.FALSE;
			KundeDto kundeDtoKopf = null;
			if (losDto.getAuftragIId() != null) {
				// Auftrag holen
				AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(losDto.getAuftragIId());
				sAuftragsnummer = auftragDto.getCNr();
				sLieferart = getLocaleFac().lieferartFindByIIdLocaleOhneExc(auftragDto.getLieferartIId(),
						theClientDto.getLocUi(), theClientDto);

				if (auftragDto.getSpediteurIId() != null) {
					sSpediteur = getMandantFac().spediteurFindByPrimaryKey(auftragDto.getSpediteurIId())
							.getCNamedesspediteurs();
				}

				bPoenale = Helper.short2Boolean(auftragDto.getBPoenale());
				bRoHs = Helper.short2Boolean(auftragDto.getBRoHs());
				// Internen Kommentar aus dem Auftrag, abhaengig von einem
				// Mandantenparameter
				ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_KOMMENTAR_AM_FERTIGUNGSBEGLEITSCHEIN);
				short iValue = Short.parseShort(parameter.getCWert());
				boolean bDruckeKommentar = Helper.short2boolean(iValue);
				if (bDruckeKommentar) {
					sInternerKommentar = auftragDto.getXInternerkommentar();
				}
				// Kunde holen
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(),
						theClientDto);

				kundeDtoKopf = kundeDto;

				sKunde = kundeDto.getPartnerDto().getCName1nachnamefirmazeile1();

				KundeDto kundeDtoLieferadresse = getKundeFac()
						.kundeFindByPrimaryKey(auftragDto.getKundeIIdLieferadresse(), theClientDto);
				sKundeLieferadresse = kundeDtoLieferadresse.getPartnerDto().getCName1nachnamefirmazeile1();

				sAbteilung = kundeDto.getPartnerDto().getCName3vorname2abteilung();

				if (losDto.getAuftragpositionIId() != null) {
					AuftragpositionDto aufposDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(losDto.getAuftragpositionIId());
					if (aufposDto.getTUebersteuerbarerLiefertermin() != null) {
						dLiefertermin = aufposDto.getTUebersteuerbarerLiefertermin();
					} else {
						dLiefertermin = auftragDto.getDLiefertermin();
					}
				} else {
					dLiefertermin = auftragDto.getDLiefertermin();
				}
			} else {
				sAuftragsnummer = "";

				if (losDto.getKundeIId() != null) {
					KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(losDto.getKundeIId(), theClientDto);
					kundeDtoKopf = kundeDto;
					sKunde = kundeDto.getPartnerDto().getCName1nachnamefirmazeile1();
				} else {
					sKunde = "";
				}

				sKundeLieferadresse = "";
				dLiefertermin = null;
				sLieferart = "";
				sSpediteur = "";
			}

			Session sessionSub = FLRSessionFactory.getFactory().openSession();
			String sQuerySub = "FROM FLRLossollmaterial AS s WHERE s.flrlos.i_id=" + losIId;

			if (selektiertePositionen != null && selektiertePositionen.size() > 0) {
				String in = null;

				for (int j = 0; j < selektiertePositionen.size(); j++) {

					if (in == null) {
						in = selektiertePositionen.get(j) + "";
					} else {
						in += "," + selektiertePositionen.get(j);
					}

				}
				sQuerySub += " AND  s.i_id IN (" + in + ") ";

				mapParameter.put("P_NUR_SELEKTIERTE_POSITIONEN", Boolean.TRUE);
			} else {
				mapParameter.put("P_NUR_SELEKTIERTE_POSITIONEN", Boolean.FALSE);
			}

			sQuerySub += " ORDER BY s.flrartikel.c_nr, s.t_aendern ";

			org.hibernate.Query hquerySub = sessionSub.createQuery(sQuerySub);
			List<?> resultListSub = hquerySub.list();
			Iterator<?> resultListIteratorSub = resultListSub.iterator();
			int i = 0;
			ArrayList alDaten = new ArrayList();

			while (resultListIteratorSub.hasNext()) {
				FLRLossollmaterial sollmaterial = (FLRLossollmaterial) resultListIteratorSub.next();

				ArrayList<WarenzugangsreferenzDto> alWeReferenzen = new ArrayList();

				// SNR-CHNR
				List<SeriennrChargennrMitMengeDto> snrDtosKomplett = new ArrayList<SeriennrChargennrMitMengeDto>();

				BigDecimal bdAusgegeben = new BigDecimal(0);
				for (Iterator<?> iter = sollmaterial.getIstmaterialset().iterator(); iter.hasNext();) {
					FLRLosistmaterial item = (FLRLosistmaterial) iter.next();

					List<SeriennrChargennrMitMengeDto> snrDtos = getLagerFac()
							.getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(LocaleFac.BELEGART_LOS,
									item.getI_id());

					for (int k = 0; k < snrDtos.size(); k++) {
						snrDtosKomplett.add(snrDtos.get(k));
					}

					if (Helper.short2boolean(item.getB_abgang()) == true) {
						bdAusgegeben = bdAusgegeben.add(item.getN_menge());

						ArrayList<WarenzugangsreferenzDto> alZeile = getLagerFac().getWareneingangsreferenz(
								LocaleFac.BELEGART_LOS, item.getI_id(), snrDtos, false, theClientDto);
						for (int h = 0; h < alZeile.size(); h++) {
							alWeReferenzen.add(alZeile.get(h));
						}
					} else {
						bdAusgegeben = bdAusgegeben.subtract(item.getN_menge());
					}
				}
				Object[] zeile = new Object[MATERIAL_SPALTENANZAHL];

				String[] fieldnames = new String[] { "F_BELEGART", "F_BELEGNUMMER", "F_ZUSATZ", "F_BELEGDATUM",
						"F_POSITION1", "F_POSITION2", "F_URSPRUNGSLAND", "F_HERSTELLER", "F_MENGE", "F_LOS_AZANTEIL",
						"F_LOS_MATERIALANTEIL", "F_SNRCHNR", "F_EINSTANDSPREIS", "F_GESTEHUNGSPREIS" };

				Object[][] dataSub = new Object[alWeReferenzen.size()][fieldnames.length];

				for (int h = 0; h < alWeReferenzen.size(); h++) {
					WarenzugangsreferenzDto dto = (WarenzugangsreferenzDto) alWeReferenzen.get(h);
					dataSub[h][0] = dto.getBelegart();
					dataSub[h][1] = dto.getBelegnummer();
					dataSub[h][2] = dto.getZusatz();
					dataSub[h][3] = dto.getTBelegdatum();
					dataSub[h][4] = dto.getPosition1();
					dataSub[h][5] = dto.getPosition2();
					dataSub[h][6] = dto.getLand();
					dataSub[h][7] = dto.getHersteller();
					dataSub[h][8] = dto.getMenge();
					dataSub[h][9] = dto.getLosAZAnteil();
					dataSub[h][10] = dto.getLosMaterialanteil();
					dataSub[h][11] = dto.getSnrChnr();
					dataSub[h][12] = dto.getnEinstandspreis();
					dataSub[h][13] = dto.getNGestehungspreis();

				}

				zeile[MATERIAL_WE_REFERENZ] = new LPDatenSubreport(dataSub, fieldnames);

				zeile[MATERIAL_SNRCHNR] = SeriennrChargennrMitMengeDto
						.erstelleStringAusMehrerenSeriennummern(snrDtosKomplett);

				// PJ19885

				String[] fieldnamesSnrs = new String[] { "F_SNRCHNR", "F_SNR_VERBRAUCHT_IN" };

				Object[][] dataSubSnrs = new Object[snrDtosKomplett.size()][fieldnamesSnrs.length];
				for (int x = 0; x < snrDtosKomplett.size(); x++) {

					Object[] oZeileSubreport = new Object[2];
					dataSubSnrs[x][0] = snrDtosKomplett.get(x).getCSeriennrChargennr();

					if (getMandantFac().hatZusatzfunktionberechtigung(MandantFac.ZUSATZFUNKTION_GERAETESERIENNUMMERN,
							theClientDto)) {

						dataSubSnrs[x][1] = getLagerFac().getZugeordneteGeraetesnr(
								sollmaterial.getFlrartikel().getI_id(), snrDtosKomplett.get(x).getCSeriennrChargennr());
					}

				}

				zeile[MATERIAL_SUBREPORT_SNR_GSNR] = new LPDatenSubreport(dataSubSnrs, fieldnamesSnrs);

				BigDecimal preis = new BigDecimal(0);

				if (bdAusgegeben.doubleValue() != 0) {
					try {
						preis = getFertigungFac().getAusgegebeneMengePreis(sollmaterial.getI_id(), null, theClientDto);
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}

				if (Helper.short2boolean(sollmaterial.getB_nachtraeglich())) {
					zeile[MATERIAL_ART] = "N";
				} else {
					zeile[MATERIAL_ART] = "S";
				}
				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(sollmaterial.getFlrartikel().getI_id(),
						theClientDto);
				zeile[MATERIAL_ARTIKELNUMMER] = aDto.getCNr();
				zeile[MATERIAL_ORIGINALARTIKELNUMMER] = aDto.getCNr();

				zeile[MATERIAL_REFERENZNUMMER] = aDto.getCReferenznr();

				if (aDto.getArtikelsprDto() != null) {
					zeile[MATERIAL_BEZEICHNUNG] = aDto.getArtikelsprDto().getCBez();
					zeile[MATERIAL_ZUSATZBEZEICHNUNG] = aDto.getArtikelsprDto().getCZbez();
					zeile[MATERIAL_ZUSATZBEZEICHNUNG2] = aDto.getArtikelsprDto().getCZbez2();
					zeile[MATERIAL_KURZBEZEICHNUNG] = aDto.getArtikelsprDto().getCKbez();
				}

				zeile[MATERIAL_SOLLMENGE] = sollmaterial.getN_menge();
				zeile[MATERIAL_KOMMENTAR] = sollmaterial.getC_kommentar();

				if (sollmaterial.getLossollmaterial_i_id_original() != null) {
					zeile[MATERIAL_ERSATZTYP] = Boolean.TRUE;
					zeile[MATERIAL_ORIGINALARTIKELNUMMER] = sollmaterial.getFlrlossollmaterial_original()
							.getFlrartikel().getC_nr();

				} else {
					zeile[MATERIAL_ERSATZTYP] = Boolean.FALSE;
				}

				zeile[MATERIAL_ISTMENGE] = bdAusgegeben;
				zeile[MATERIAL_SOLLPREIS] = sollmaterial.getN_sollpreis();
				zeile[MATERIAL_ISTPREIS] = preis;

				LossollmaterialDto sollMatDto = getFertigungFac()
						.lossollmaterialFindByPrimaryKey(sollmaterial.getI_id());

				zeile[MATERIAL_EINHEIT] = sollMatDto.getEinheitCNr();

				ArtikellieferantDto dto = getArtikelFac().getArtikelEinkaufspreis(
						sollmaterial.getFlrartikel().getI_id(), null, sollmaterial.getN_menge(),
						theClientDto.getSMandantenwaehrung(), new java.sql.Date(sollMatDto.getTAendern().getTime()),
						theClientDto);
				if (dto != null) {
					zeile[MATERIAL_LIEF1PREIS] = dto.getLief1Preis();
				}

				if (loslagerDtos.length > 0) {
					ArtikellagerplaetzeDto artikellagerplaetzeDto = getLagerFac()
							.artikellagerplaetzeFindByArtikelIIdLagerIId(sollmaterial.getFlrartikel().getI_id(),
									loslagerDtos[0].getLagerIId());
					if (artikellagerplaetzeDto != null && artikellagerplaetzeDto.getLagerplatzDto() != null) {
						zeile[MATERIAL_LAGERORT] = artikellagerplaetzeDto.getLagerplatzDto().getCLagerplatz();
					}
				}

				// PJ21048

				if (kundeDtoKopf != null) {

//					MwstsatzDto mwstsatzDtoAktuell = getMandantFac()
//							.mwstsatzFindByMwstsatzbezIIdAktuellster(kundeDtoKopf.getMwstsatzbezIId(), theClientDto);
					Timestamp belegDatum = Helper.cutTimestamp(new Timestamp(losDto.getTProduktionsende().getTime()));
					MwstsatzDto mwstsatzDtoAktuell = getMandantFac()
							.mwstsatzZuDatumValidate(kundeDtoKopf.getMwstsatzbezIId(), belegDatum, theClientDto);

					VkpreisfindungDto vkpreisfindungDto = getVkPreisfindungFac().verkaufspreisfindung(
							sollMatDto.getArtikelIId(), kundeDtoKopf.getIId(), losDto.getNLosgroesse(),
							losDto.getTProduktionsende(), kundeDtoKopf.getVkpfArtikelpreislisteIIdStdpreisliste(),
							mwstsatzDtoAktuell.getIId(), theClientDto.getSMandantenwaehrung(), theClientDto);

					VerkaufspreisDto kundenVKPreisDto = Helper.getVkpreisBerechnet(vkpreisfindungDto);
					if (kundenVKPreisDto != null) {
						zeile[MATERIAL_VKPREIS] = kundenVKPreisDto.nettopreis;
					}
				}

				alDaten.add(zeile);
				i++;
			}

			sessionSub.close();

			if (bSortiertNachOrginialArtikelnummer) {

				// Sortieren nach Artikel und dann nach ersatzartikel

				for (int k = alDaten.size() - 1; k > 0; --k) {
					for (int j = 0; j < k; ++j) {
						Object[] a1 = (Object[]) alDaten.get(j);
						Object[] a2 = (Object[]) alDaten.get(j + 1);

						if (((String) a1[MATERIAL_ORIGINALARTIKELNUMMER])
								.compareTo((String) a2[MATERIAL_ORIGINALARTIKELNUMMER]) > 0) {

							alDaten.set(j, a2);
							alDaten.set(j + 1, a1);
						} else if (((String) a1[MATERIAL_ORIGINALARTIKELNUMMER])
								.compareTo((String) a2[MATERIAL_ORIGINALARTIKELNUMMER]) == 0) {

							if (((String) a1[MATERIAL_ARTIKELNUMMER])
									.compareTo((String) a2[MATERIAL_ARTIKELNUMMER]) > 0) {

								alDaten.set(j, a2);
								alDaten.set(j + 1, a1);
							}

						}
					}
				}

			}

			data = new Object[alDaten.size()][MATERIAL_SPALTENANZAHL];
			data = (Object[][]) alDaten.toArray(data);

			// Abbuchungslaeger
			String[] fieldnamesLola = new String[] { "F_LAGER", "F_STANDORT" };

			ArrayList<Object[]> alLola = new ArrayList<Object[]>();
			for (int j = 0; j < loslagerDtos.length; j++) {
				LoslagerentnahmeDto dto = loslagerDtos[j];

				LagerDto lolaDto = getLagerFac().lagerFindByPrimaryKey(dto.getLagerIId());

				Object[] o = new Object[2];

				o[0] = lolaDto.getCNr();

				if (lolaDto.getPartnerIIdStandort() != null) {

					o[1] = getPartnerFac().partnerFindByPrimaryKey(lolaDto.getPartnerIIdStandort(), theClientDto)
							.getCKbez();

				}
				alLola.add(o);

			}

			Object[][] dataSubLola = new Object[alLola.size()][fieldnamesLola.length];
			dataSubLola = (Object[][]) alLola.toArray(dataSubLola);

			mapParameter.put("P_SUBREPORT_ABBUCHUNGSLAEGER", new LPDatenSubreport(dataSubLola, fieldnamesLola));

			boolean bErsatztypen = false;
			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ERSATZTYPENVERWALTUNG,
					theClientDto)) {
				bErsatztypen = true;
			}

			mapParameter.put("P_ERSATZTYPENVERWALTUNG", bErsatztypen);

			mapParameter.put("P_SORTIERT_NACH_ORIGINALARTIKELNUMMER", bSortiertNachOrginialArtikelnummer);

			mapParameter.put("P_AUFTRAGNUMMER", sAuftragsnummer);
			mapParameter.put("P_AUFTRAG_INTERNERKOMMENTAR", sInternerKommentar);
			mapParameter.put("P_AUFTRAG_KUNDE_ABTEILUNG", sAbteilung);
			mapParameter.put("P_LIEFERART", sLieferart);
			mapParameter.put("P_SPEDITEUR", sSpediteur);
			mapParameter.put("P_POENALE", bPoenale);
			mapParameter.put("P_ROHS", bRoHs);
			KostenstelleDto kstDto = getSystemFac().kostenstelleFindByPrimaryKey(losDto.getKostenstelleIId());
			mapParameter.put("P_KOSTENSTELLENUMMER", kstDto.getCNr());
			mapParameter.put("P_KUNDE", sKunde);
			mapParameter.put("P_KUNDE_LIEFERADRESSE", sKundeLieferadresse);
			mapParameter.put("P_LOSGROESSE", losDto.getNLosgroesse());

			mapParameter.put("P_LOSKLASSEN", getLosLosKlassenAlsString(losDto.getIId()));

			LosablieferungDto[] losablieferungDtos = getFertigungFac().losablieferungFindByLosIId(losDto.getIId(),
					false, theClientDto);
			BigDecimal bdAbgeliefert = new BigDecimal(0.0000);
			for (int j = 0; j < losablieferungDtos.length; j++) {
				bdAbgeliefert = bdAbgeliefert.add(losablieferungDtos[j].getNMenge());
			}

			mapParameter.put("P_ABGELIEFERTEMENGE", bdAbgeliefert);

			mapParameter.put("P_LOSNUMMER", losDto.getCNr());
			mapParameter.put("P_PRODUKTIONSBEGINN", losDto.getTProduktionsbeginn());
			mapParameter.put("P_PRODUKTIONSENDE", losDto.getTProduktionsende());
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(losDto.getTProduktionsende());
			mapParameter.put("P_PRODUKTIONSENDE_KW", new Integer(gc.get(Calendar.WEEK_OF_YEAR)));

			mapParameter.put("P_LIEFERTERMIN", dLiefertermin);

			// Fertigungsort
			if (losDto.getPartnerIIdFertigungsort() != null) {
				PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(losDto.getPartnerIIdFertigungsort(),
						theClientDto);
				mapParameter.put("P_FERTIGUNGSORT", partnerDto.formatTitelAnrede());
			}

			// Erstlos
			mapParameter.put("P_ERSTLOS", new Boolean(istErstlos(losDto, theClientDto)));

			ArrayList<Object> images = new ArrayList<Object>();
			String sLosStuecklisteArtikelKommentar = "";
			// Bild einfuegen
			String sMengenEinheit = "";
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stuecklisteDto = getStuecklisteFac()
						.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(), theClientDto);
				// Einheit
				if (stuecklisteDto.getArtikelDto() != null) {
					if (stuecklisteDto.getArtikelDto().getEinheitCNr() != null) {
						sMengenEinheit = stuecklisteDto.getArtikelDto().getEinheitCNr();
					}
				}

				// Sperren
				String sperren = getArtikelFac().getArtikelsperrenText(stuecklisteDto.getArtikelIId());

				mapParameter.put("P_STUECKLISTESPERRSTATUS", sperren);

				// Abmessungen
				GeometrieDto geometrieDto = stuecklisteDto.getArtikelDto().getGeometrieDto();
				if (geometrieDto != null) {
					mapParameter.put(P_ARTIKEL_BREITE, geometrieDto.getFBreite());
					mapParameter.put(P_ARTIKEL_HOEHE, geometrieDto.getFHoehe());
					mapParameter.put(P_ARTIKEL_TIEFE, geometrieDto.getFTiefe());
				}
				// Bauform
				VerpackungDto verpackungDto = stuecklisteDto.getArtikelDto().getVerpackungDto();
				if (verpackungDto != null) {
					mapParameter.put("P_ARTIKEL_BAUFORM", verpackungDto.getCBauform());
					mapParameter.put("P_ARTIKEL_VERPACKUNGSART", verpackungDto.getCVerpackungsart());
				}

				mapParameter.put("P_INDEX", stuecklisteDto.getArtikelDto().getCIndex());
				mapParameter.put("P_REVISION", stuecklisteDto.getArtikelDto().getCRevision());

				ArtikelkommentarDto[] artikelkommentarDto = getArtikelkommentarFac()
						.artikelkommentardruckFindByArtikelIIdBelegartCNr(stuecklisteDto.getArtikelIId(),
								LocaleFac.BELEGART_LOS, theClientDto.getLocUiAsString(), theClientDto);

				if (artikelkommentarDto != null && artikelkommentarDto.length > 0) {

					for (int j = 0; j < artikelkommentarDto.length; j++) {
						if (artikelkommentarDto[j].getArtikelkommentarsprDto() != null) {
							// Text Kommentar
							if (artikelkommentarDto[j].getDatenformatCNr().trim()
									.indexOf(MediaFac.DATENFORMAT_MIMETYPEART_TEXT) != -1) {
								if (sLosStuecklisteArtikelKommentar == "") {
									sLosStuecklisteArtikelKommentar += artikelkommentarDto[j]
											.getArtikelkommentarsprDto().getXKommentar();
								} else {
									sLosStuecklisteArtikelKommentar += "\n"
											+ artikelkommentarDto[j].getArtikelkommentarsprDto().getXKommentar();
								}
							} else if (artikelkommentarDto[j].getDatenformatCNr()
									.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
									|| artikelkommentarDto[j].getDatenformatCNr()
											.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)
									|| artikelkommentarDto[j].getDatenformatCNr()
											.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)) {
								byte[] bild = artikelkommentarDto[j].getArtikelkommentarsprDto().getOMedia();
								if (bild != null) {
									java.awt.Image myImage = Helper.byteArrayToImage(bild);
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
			mapParameter.put("P_MENGENEINHEIT", sMengenEinheit);
			mapParameter.put("P_PROJEKT", losDto.getCProjekt());
			mapParameter.put("P_LOSKOMMENTAR", losDto.getCKommentar());
			mapParameter.put("P_LOSLANGTEXT", losDto.getXText());

			FertigungsgruppeDto fertGruppeDto = getStuecklisteFac()
					.fertigungsgruppeFindByPrimaryKey(losDto.getFertigungsgruppeIId());
			mapParameter.put("P_FERTIGUNGSGRUPPE", fertGruppeDto.getCBez());

			// Materialliste?
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stkDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(),
						theClientDto);

				if (sLosStuecklisteArtikelKommentar != "") {
					mapParameter.put("P_STUECKLISTEARTIKELKOMMENTAR",
							Helper.formatStyledTextForJasper(sLosStuecklisteArtikelKommentar));
				}

				mapParameter.put("P_STUECKLISTEBEZEICHNUNG", stkDto.getArtikelDto().getArtikelsprDto().getCBez());

				mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG",
						stkDto.getArtikelDto().getArtikelsprDto().getCZbez());
				mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG2",
						stkDto.getArtikelDto().getArtikelsprDto().getCZbez2());

				mapParameter.put("P_STUECKLISTEKURZBEZEICHNUNG", stkDto.getArtikelDto().getArtikelsprDto().getCKbez());
				mapParameter.put("P_STUECKLISTEREFERENZNUMMER", stkDto.getArtikelDto().getCReferenznr());

				mapParameter.put("P_STUECKLISTENUMMER", stkDto.getArtikelDto().getCNr());

				mapParameter.put("P_STUECKLISTE_GEWICHTKG", stkDto.getArtikelDto().getFGewichtkg());

				if (stkDto.getArtikelDto().getVerpackungDto() != null) {
					mapParameter.put("P_STUECKLISTE_BAUFORM", stkDto.getArtikelDto().getVerpackungDto().getCBauform());
					mapParameter.put("P_STUECKLISTE_VERPACKUNGSART",
							stkDto.getArtikelDto().getVerpackungDto().getCVerpackungsart());
				}

				if (stkDto.getArtikelDto().getGeometrieDto() != null) {
					mapParameter.put("P_STUECKLISTE_BREITETEXT",
							stkDto.getArtikelDto().getGeometrieDto().getCBreitetext());
					mapParameter.put("P_STUECKLISTE_BREITE", stkDto.getArtikelDto().getGeometrieDto().getFBreite());
					mapParameter.put("P_STUECKLISTE_HOEHE", stkDto.getArtikelDto().getGeometrieDto().getFHoehe());
					mapParameter.put("P_STUECKLISTE_TIEFE", stkDto.getArtikelDto().getGeometrieDto().getFTiefe());
				}

				// Stuecklisteneigenschaften
				StuecklisteeigenschaftDto[] stuecklisteeigenschaftDtos = getStuecklisteFac()
						.stuecklisteeigenschaftFindByStuecklisteIId(losDto.getStuecklisteIId());
				ArrayList<Object[]> al = new ArrayList<Object[]>();
				for (int j = 0; j < stuecklisteeigenschaftDtos.length; j++) {
					StuecklisteeigenschaftDto dto = stuecklisteeigenschaftDtos[j];

					Object[] o = new Object[2];
					String sStklEigenschaftArt = dto.getStuecklisteeigenschaftartDto().getCBez();
					o[0] = sStklEigenschaftArt;
					o[1] = dto.getCBez();
					al.add(o);

					// Index und Materialplatz auch einzeln an Report uebergeben
					if (sStklEigenschaftArt.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_INDEX)) {
						mapParameter.put(P_STUECKLISTENEIGENSCHAFT_INDEX, dto.getCBez());
					}
					if (sStklEigenschaftArt
							.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_MATERIALPLATZ)) {
						mapParameter.put(P_STUECKLISTENEIGENSCHAFT_MATERIALPLATZ, dto.getCBez());
					}

				}

				if (stuecklisteeigenschaftDtos.length > 0) {
					String[] fieldnames = new String[] { "F_EIGENSCHAFTART", "F_BEZEICHNUNG" };
					Object[][] dataSub = new Object[al.size()][fieldnames.length];
					dataSub = (Object[][]) al.toArray(dataSub);

					mapParameter.put("DATENSUBREPORT", new LPDatenSubreport(dataSub, fieldnames));
				}

			} else {
				mapParameter.put("P_STUECKLISTEBEZEICHNUNG", losDto.getCProjekt());
				mapParameter.put("P_STUECKLISTENUMMER",
						getTextRespectUISpr("fert.materialliste", theClientDto.getMandant(), theClientDto.getLocUi()));
			}

			initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_MATERIALLISTE,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		} catch (Exception t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, t);

		}

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printStueckrueckmeldung(Integer losIId, int iSortierung, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Session session = null;
		try {
			this.useCase = UC_STUECKRUECKMELDUNG;
			this.index = -1;
			// Los holen
			LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);
			if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN, "");
			} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT, "");
			}
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRLossollarbeitsplan.class);
			// Nur Buchungen auf dieses Los
			c.add(Restrictions.eq(FertigungFac.FLR_LOSSOLLARBEITSPLAN_LOS_I_ID, losIId));

			// Sortierung
			if (iSortierung == Helper.SORTIERUNG_NACH_IDENT) {
				c.createCriteria(FertigungFac.FLR_LOSSOLLARBEITSPLAN_FLRARTIKEL)
						.addOrder(Order.asc(ArtikelFac.FLR_ARTIKEL_C_NR));
			} else if (iSortierung == Helper.SORTIERUNG_NACH_ARBEITSGANG) {
				c.addOrder(Order.asc(FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_ARBEITSGANGNUMMER));
			}
			List<?> list = c.list();

			ArrayList alDaten = new ArrayList();
			Iterator<?> iter = list.iterator();
			while (iter.hasNext()) {
				FLRLossollarbeitsplan item = (FLRLossollarbeitsplan) iter.next();

				LosgutschlechtDto[] losgutschlechDtos = getFertigungFac()
						.losgutschlechtFindByLossollarbeitsplanIId(item.getI_id());

				for (int i = 0; i < losgutschlechDtos.length; i++) {

					Object[] oZeile = new Object[SRM_SPALTENANZAHL];
					ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(item.getFlrartikel().getI_id(),
							theClientDto);
					oZeile[SRM_IDENT] = artikelDto.getCNr();
					oZeile[SRM_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
					oZeile[SRM_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCZbez();

					oZeile[SRM_ARBEITSGANG] = item.getI_arbeitsgangsnummer();
					if (losgutschlechDtos[i].getZeitdatenIId() != null) {
						ZeitdatenDto zd = getZeiterfassungFac()
								.zeitdatenFindByPrimaryKey(losgutschlechDtos[i].getZeitdatenIId(), theClientDto);
						oZeile[SRM_GEBUCHT] = zd.getTZeit();
						PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(zd.getPersonalIId(),
								theClientDto);

						oZeile[SRM_PERSONAL] = personalDto.formatAnrede();
					}

					oZeile[SRM_GUT] = losgutschlechDtos[i].getNGut();
					oZeile[SRM_SCHLECHT] = losgutschlechDtos[i].getNSchlecht();
					oZeile[SRM_INARBEIT] = losgutschlechDtos[i].getNInarbeit();
					oZeile[SRM_OFFEN] = losDto.getNLosgroesse().subtract(losgutschlechDtos[i].getNGut())
							.subtract(losgutschlechDtos[i].getNSchlecht())
							.subtract(losgutschlechDtos[i].getNInarbeit());
					if (item.getFlrmaschine() != null) {
						oZeile[SRM_MASCHINE] = item.getFlrmaschine().getC_identifikationsnr();
					}

					alDaten.add(oZeile);
				}

			}

			data = new Object[alDaten.size()][SRM_SPALTENANZAHL];
			data = (Object[][]) alDaten.toArray(data);

			// Parameter
			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			mapParameter.put("P_ANGELEGT", new java.util.Date(losDto.getTAnlegen().getTime()));
			String sAuftragsnummer;
			String sInternerKommentar = null;
			String sKunde;
			Timestamp dLiefertermin;
			if (losDto.getAuftragpositionIId() != null) {
				// Auftrag holen
				AuftragpositionDto aufposDto = getAuftragpositionFac()
						.auftragpositionFindByPrimaryKey(losDto.getAuftragpositionIId());
				AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(aufposDto.getBelegIId());
				sAuftragsnummer = auftragDto.getCNr();
				// Internen Kommentar aus dem Auftrag, anhaengig von einem
				// Mandantenparameter
				ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_KOMMENTAR_AM_FERTIGUNGSBEGLEITSCHEIN);
				short iValue = Short.parseShort(parameter.getCWert());
				boolean bDruckeKommentar = Helper.short2boolean(iValue);
				if (bDruckeKommentar) {
					sInternerKommentar = auftragDto.getXInternerkommentar();
				}
				// Kunde holen
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(),
						theClientDto);
				sKunde = kundeDto.getPartnerDto().getCName1nachnamefirmazeile1();
				if (aufposDto.getTUebersteuerbarerLiefertermin() != null) {
					dLiefertermin = aufposDto.getTUebersteuerbarerLiefertermin();
				} else {
					dLiefertermin = auftragDto.getDLiefertermin();
				}
			} else {
				sAuftragsnummer = "";
				sKunde = "";
				dLiefertermin = null;
			}
			mapParameter.put("P_AUFTRAGNUMMER", sAuftragsnummer);
			mapParameter.put("P_AUFTRAG_INTERNERKOMMENTAR", sInternerKommentar);

			KostenstelleDto kstDto = getSystemFac().kostenstelleFindByPrimaryKey(losDto.getKostenstelleIId());
			mapParameter.put("P_KOSTENSTELLENUMMER", kstDto.getCNr());
			mapParameter.put("P_KUNDE", sKunde);
			mapParameter.put("P_LOSGROESSE", losDto.getNLosgroesse());
			mapParameter.put("P_LOSNUMMER", losDto.getCNr());
			mapParameter.put("P_PRODUKTIONSBEGINN", losDto.getTProduktionsbeginn());
			mapParameter.put("P_LIEFERTERMIN", dLiefertermin);
			// Materialliste?
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stkDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(),
						theClientDto);
				mapParameter.put("P_STUECKLISTEBEZEICHNUNG", stkDto.getArtikelDto().getArtikelsprDto().getCBez());
				mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG",
						stkDto.getArtikelDto().getArtikelsprDto().getCZbez());
				mapParameter.put("P_STUECKLISTENUMMER", stkDto.getArtikelDto().getCNr());
			} else {
				mapParameter.put("P_STUECKLISTEBEZEICHNUNG", losDto.getCProjekt());
				mapParameter.put("P_STUECKLISTENUMMER",
						getTextRespectUISpr("fert.materialliste", theClientDto.getMandant(), theClientDto.getLocUi()));
			}
			initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_STUECKRUECKMELDUNG,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
			return getReportPrint();
		} catch (Exception t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, t);
		} finally {
			closeSession(session);
		}
	}

	public JasperPrintLP printSonderetikett(String s, TheClientDto theClientDto) {
		this.useCase = UC_SONDERETIKETT;
		this.index = -1;

		Map<String, Object> parameter = new TreeMap<String, Object>();

		parameter.put("P_STRING", s);
		data = new Object[0][0];
		initJRDS(parameter, FertigungReportFac.REPORT_MODUL, FertigungReportFac.REPORT_SONDERETIKETT,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	@Override
	public JasperPrintLP printLosEtikett(Integer losIId, BigDecimal bdMenge, String sKommentar, boolean bMitInhalten,
			Integer iExemplare, String cReportnamevariante, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Validator.notEmpty(cReportnamevariante, "cReportnamevariante");

		ReportvarianteDto reportvarianteDto = getDruckerFac()
				.reportvarianteFindByCReportnameCReportnameVariante(REPORT_LOSETIKETT1, cReportnamevariante);
		theClientDto.setReportvarianteIId(reportvarianteDto.getIId());

		return printLosEtikett(losIId, bdMenge, sKommentar, bMitInhalten, iExemplare, theClientDto);
	}

	@Override
	public JasperPrintLP printAblieferEtikettOnServer(Integer losablieferungIId, BigDecimal bdHandmenge,
			TheClientDto theClientDto) {
		String printer = getServerDruckerFacLocal().getPrinterNameByArbeitsplatzparameterOhneExc(
				ParameterFac.ARBEITSPLATZPARAMETER_DRUCKERNAME_MOBILES_LOSABLIEFER_ETIKETT, theClientDto);

		if (printer == null)
			return null;

		JasperPrintLP print = printAblieferEtikett(losablieferungIId, 1, bdHandmenge, null, theClientDto);

		HvPrinter hvPrinter = getServerDruckerFacLocal().createHvPrinter(printer);
		if (getServerDruckerFacLocal().exists(hvPrinter))
			getServerDruckerFacLocal().print(print, hvPrinter);

		return print;
	}

	@Override
	public JasperPrintLP printFertigungsbegleitscheinOnServer(Integer losId, String printerName,
			TheClientDto theClientDto) {
		JasperPrintLP print = printFertigungsbegleitschein(losId, false, theClientDto);
		HvPrinter hvPrinter = getServerDruckerFacLocal().createMobileDefaultPagePrinter(printerName, theClientDto);
		getServerDruckerFacLocal().printMobile(print, hvPrinter);
		return print;
	}

	@Override
	public JasperPrintLP printAusgabeListeOnServer(Integer losId, Integer iSortierung, boolean bVerdichtetNachIdent,
			boolean bVorrangigNachFarbcodeSortiert, Integer artikelklasseIId, String alternativerReport,
			String printerName, TheClientDto theClientDto) throws EJBExceptionLP {
		JasperPrintLP print = printAusgabeListe(new Integer[] { losId }, iSortierung, bVerdichtetNachIdent,
				bVorrangigNachFarbcodeSortiert, artikelklasseIId, alternativerReport, theClientDto);
		HvPrinter hvPrinter = getServerDruckerFacLocal().createMobileDefaultPagePrinter(printerName, theClientDto);
		getServerDruckerFacLocal().printMobile(print, hvPrinter);
		return print;
	}

	@Override
	public JasperPrintLP printBedarfsuebernahmeSynchronisierungOnServer(Integer personalIId,
			boolean bStatusAufOffenSetzen, TheClientDto theClientDto) {
		String printerName = getParameterFac().getMobilDruckernameBedarfsuebernahme(theClientDto.getMandant());
		if (Helper.isStringEmpty(printerName) || printerName.equals(".")) {
			printerName = null;
//			throw EJBExcFactory.keinDruckernameHinterlegtMandantenparameter(
//					ParameterFac.PARAMETER_MOBIL_DRUCKERNAME_BEDARFSUEBERNAHME);
		}

		HvPrinter hvPrinter = getServerDruckerFacLocal().createMobileDefaultPagePrinter(printerName, theClientDto);
		if (!hvPrinter.exists()) {
			throw EJBExcFactory.unbekannterDruckernameHinterlegtMandantenparameter(
					ParameterFac.PARAMETER_MOBIL_DRUCKERNAME_BEDARFSUEBERNAHME, printerName);
		}

		JasperPrintLP print = printBedarfsuebernahmeSynchronisierung(personalIId, bStatusAufOffenSetzen, theClientDto);
		getServerDruckerFacLocal().printMobile(print, hvPrinter);

		return print;
	}

	@Override
	public JasperPrintLP printLosEtikettOnServer(Integer losIId, BigDecimal bdMenge, String sKommentar,
			boolean bMitInhalten, Integer iExemplare, String printerName, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP {
		JasperPrintLP print = printLosEtikett(losIId, bdMenge, sKommentar, bMitInhalten, iExemplare, theClientDto);

		HvPrinter hvPrinter = getServerDruckerFacLocal().createMobileDefaultLabelPrinter(printerName, theClientDto);
		getServerDruckerFacLocal().printMobile(print, hvPrinter);

		return print;
	}
}
