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
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.NoResultException;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.angebot.service.AngebotReportFac;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelreservierung;
import com.lp.server.artikel.fastlanereader.generated.FLRFehlmenge;
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
import com.lp.server.artikel.service.GeometrieDto;
import com.lp.server.artikel.service.GeraetesnrDto;
import com.lp.server.artikel.service.HerstellerDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerbewegungDto;
import com.lp.server.artikel.service.MaterialDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VerpackungDto;
import com.lp.server.artikel.service.VkPreisfindungEinzelverkaufspreisDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.artikel.service.WarenzugangsreferenzDto;
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
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.fertigung.ejb.Lossollarbeitsplan;
import com.lp.server.fertigung.fastlanereader.generated.FLRLos;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosReport;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosablieferung;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosgutschlecht;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosistmaterial;
import com.lp.server.fertigung.fastlanereader.generated.FLRLossollarbeitsplan;
import com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial;
import com.lp.server.fertigung.fastlanereader.generated.FLROffeneags;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosMonatsauswertungDto;
import com.lp.server.fertigung.service.LosStatistikDto;
import com.lp.server.fertigung.service.LosablieferungDto;
import com.lp.server.fertigung.service.LosgutschlechtDto;
import com.lp.server.fertigung.service.LosistmaterialDto;
import com.lp.server.fertigung.service.LoslagerentnahmeDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.fertigung.service.ReportAufloesbareFehlmengenDto;
import com.lp.server.fertigung.service.ReportLosAusgabelisteDto;
import com.lp.server.fertigung.service.ReportLosnachkalkulationDto;
import com.lp.server.fertigung.service.ReportTheoretischeFehlmengenDto;
import com.lp.server.fertigung.service.ZusatzstatusDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.personal.service.MaschinenzeitdatenDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.SollverfuegbarkeitDto;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.projekt.service.ProjektReportFac;
import com.lp.server.reklamation.fastlanereader.generated.FLRFehlerspr;
import com.lp.server.reklamation.service.FehlerDto;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.stueckliste.service.StuecklisteeigenschaftDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeLosAblieferung;
import com.lp.server.system.jcr.service.docnode.DocNodeReklamation;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.DatumsfilterVonBis;
import com.lp.server.util.HelperServer;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.server.util.report.TimingInterceptor;
import com.lp.util.EJBExceptionLP;
import com.lp.util.HVPdfReport;
import com.lp.util.Helper;
import com.lp.util.HelperReport;
import com.lp.util.LPDatenSubreport;

@Stateless
@Interceptors(TimingInterceptor.class)
public class FertigungReportFacBean extends LPReport implements
		FertigungReportFac, JRDataSource {

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
	private final static int TF_SPALTENANZAHL = 14;

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

	private final static int AUSG_SPALTENANZAHL = 33;

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
	private final static int BEGL_SPALTENANZAHL = 47;

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
	private final static int MATERIAL_SPALTENANZAHL = 14;

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

	private static final int ALLE_FELDANZAHL = 19;

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
	private final static int OFFENE_SPALTENANZAHL = 24;

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
	private final static int MASCHINEUNDMATERIAL_GESAMTZEIT_IN_STUNDEN = 21;
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
	private final static int MASCHINEUNDMATERIAL_NEXT_AG_GESAMTZEIT_IN_STUNDEN = 43;
	private final static int MASCHINEUNDMATERIAL_NEXT_AG_MASCHINENVERSATZ_MS = 44;

	private final static int MASCHINEUNDMATERIAL_SPALTENANZAHL = 45;

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

	private final static int OFFENE_AG_SPALTENANZAHL = 35;

	private static final int AUSLIEFERLISTE_LOSNUMMER = 0;
	private static final int AUSLIEFERLISTE_ARTIKELNUMMER = 1;
	private static final int AUSLIEFERLISTE_BEZEICHNUNG = 2;
	private static final int AUSLIEFERLISTE_KALENDERWOCHE = 3;
	private static final int AUSLIEFERLISTE_AUFTRAGSNUMMER = 4;
	private static final int AUSLIEFERLISTE_LOSGROESSE = 5;
	private static final int AUSLIEFERLISTE_GELIEFERT = 6;
	private static final int AUSLIEFERLISTE_BEGINN = 7;
	private static final int AUSLIEFERLISTE_ENDE = 8;
	private static final int AUSLIEFERLISTE_MATERIAL = 9;
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
	private final static int AUSLIEFERLISTE_SPALTENANZAHL = 55;

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
	private static final int ABLIEF_ANZAHL_SPALTEN = 11;

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

	private static final int HF_ARTIKELNUMMER = 0;
	private static final int HF_LOSGROESSE = 1;
	private static final int HF_LOSNUMMER = 2;
	private static final int HF_BEZEICHNUNG = 3;
	private static final int HF_ERLEDIGT = 4;
	private static final int HF_POSITION_ARTIKELNUMMMER = 5;
	private static final int HF_POSITION_BEZEICHNUNG = 6;
	private static final int HF_POSITION_AUSGEGEBEN = 7;
	private static final int HF_POSITION_ABGELIFERT = 8;
	private static final int HF_POSITION_OFFEN = 9;
	private static final int HF_POSITION_PREIS = 10;
	private static final int HF_POSITION_EKPREIS = 11;
	private static final int HF_POSITION_MAT_ODER_AZ = 12;
	private static final int HF_POSITION_AUSGEGEBEN_MASCHINE = 13;
	private static final int HF_POSITION_ABGELIFERT_MASCHINE = 14;
	private static final int HF_POSITION_OFFEN_MASCHINE = 15;
	private static final int HF_POSITION_PREIS_MASCHINE = 16;
	private static final int HF_AUFTRAGSNUMMER = 17;
	private static final int HF_FERTIGUNGSGRUPPE = 18;
	private static final int HF_ANZAHL_SPALTEN = 19;

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
	private final static int FMAL_SPALTENANZAHL = 25;

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
	private final static int LOSZEITEN_SPALTENANZAHL = 13;

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
	private final static int LOSSTATISTIK_ERLEDIGUNGSDATUM = 21;

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

	private final static int GESAMTKALKULATION_ARTIKELNUMMER = 0;
	private final static int GESAMTKALKULATION_ARTIKELBEZEICHNUNG = 1;
	private final static int GESAMTKALKULATION_ZUSATZBEZEICHNUNG = 2;
	private final static int GESAMTKALKULATION_EINHEIT = 3;
	private final static int GESAMTKALKULATION_LOSGROESSE = 4;
	private final static int GESAMTKALKULATION_EBENE = 5;
	private final static int GESAMTKALKULATION_SOLLMENGE = 6;
	private final static int GESAMTKALKULATION_SOLLPREIS = 7;
	private final static int GESAMTKALKULATION_ISTMENGE = 8;
	private final static int GESAMTKALKULATION_ISTPREIS = 9;
	private final static int GESAMTKALKULATION_ARBEITSZEIT_KOSTEN = 10;
	private final static int GESAMTKALKULATION_VERBRAUCHTE_MENGE = 11;
	private final static int GESAMTKALKULATION_BELEGART_ZUGANG = 12;
	private final static int GESAMTKALKULATION_BELEGNUMMER_ZUGANG = 13;
	private final static int GESAMTKALKULATION_ARBEITSZEIT = 14;
	private final static int GESAMTKALKULATION_MENGENFAKTOR = 15;
	private final static int GESAMTKALKULATION_EINSTANDSPREIS = 16;
	private final static int GESAMTKALKULATION_ANZAHL_SPALTEN = 17;

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
	private final static int ETI_A4_ANZAHL_FELDER = 38;

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
	private final static int FT_ANZAHL_SPALTEN = 13;

	private static final int ABLIEFERETIKETT_SERIENNUMMER = 0;
	private static final int ABLIEFERETIKETT_SUBREPORT_GERAETESNR = 1;
	private static final int ABLIEFERETIKETT_VERSION = 2;
	private static final int ABLIEFERETIKETT_I_ID_BUCHUNG = 3;
	private final static int ABLIEFERETIKETT_ANZAHL_SPALTEN = 4;

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
			}

		}
			break;
		case UC_GESAMTKALKULATION: {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_ARTIKELBEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_ZUSATZBEZEICHNUNG];
			} else if ("Sollmenge".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_SOLLMENGE];
			} else if ("Istmenge".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_ISTMENGE];
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
			} else if ("Losgroesse".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_LOSGROESSE];
			} else if ("Arbeitszeit".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_ARBEITSZEIT];
			} else if ("Mengenfaktor".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_MENGENFAKTOR];
			} else if ("Einstandspreis".equals(fieldName)) {
				value = data[index][GESAMTKALKULATION_EINSTANDSPREIS];
			}

		}
			break;
		case UC_MATERIALLISTE: {
			if ("F_ART".equals(fieldName)) {
				value = data[index][MATERIAL_ART];
			} else if ("F_ARTIKELNUMMER".equals(fieldName)) {
				value = data[index][MATERIAL_ARTIKELNUMMER];
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
			} else if ("F_EINHEIT".equals(fieldName)) {
				value = data[index][MATERIAL_EINHEIT];
			} else if ("F_ISTMENGE".equals(fieldName)) {
				value = data[index][MATERIAL_ISTMENGE];
			} else if ("F_SOLLPREIS".equals(fieldName)) {
				value = data[index][MATERIAL_SOLLPREIS];
			} else if ("F_LIEF1PREIS".equals(fieldName)) {
				value = data[index][MATERIAL_LIEF1PREIS];
			} else if ("F_ISTPREIS".equals(fieldName)) {
				value = data[index][MATERIAL_ISTPREIS];
			} else if ("F_WE_REFERENZ".equals(fieldName)) {
				value = data[index][MATERIAL_WE_REFERENZ];
			} else if ("F_SNRCHNR".equals(fieldName)) {
				value = data[index][MATERIAL_SNRCHNR];
			}

		}
			break;
		case UC_AUSGABELISTE: {
			if ("F_IDENT".equals(fieldName)) {
				value = data[index][AUSG_IDENT];
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
				value = Helper
						.formatStyledTextForJasper(data[index][AUSG_STUECKLISTE_KOMMENTAR]);
			} else if ("F_STKL_POSITION".equals(fieldName)) {
				value = data[index][AUSG_STUECKLISTE_POSITION];
			} else if ("F_SNRCHNR".equals(fieldName)) {
				value = data[index][AUSG_SNRCHNR];
			} else if ("F_ARTIKELKOMMENTAR".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][AUSG_KOMMENTAR]);
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
				value = Helper
						.formatStyledTextForJasper(data[index][BEGL_KOMMENTAR]);
			} else if (F_ZUSATZBEZEICHNUNG.equals(fieldName)) {
				value = data[index][BEGL_ZUSATZBEZEICHNUNG];
			} else if ("F_ZUSATZBEZEICHNUNG2".equals(fieldName)) {
				value = data[index][BEGL_ZUSATZBEZEICHNUNG2];
			} else if ("F_MASCHINE".equals(fieldName)) {
				value = data[index][BEGL_MASCHINE];
			} else if ("F_MASCHINEBEZEICHNUNG".equals(fieldName)) {
				value = data[index][BEGL_MASCHINE_BEZEICHNUNG];
			} else if ("F_ARBEITSGANG".equals(fieldName)) {
				value = data[index][BEGL_ARBEITSGANG];
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
			} else if ("FremdmaterialArtikelzusatzbezeichnung"
					.equals(fieldName)) {
				value = data[index][BEGL_FREMDMATERIAL_ARTIKELZUSATZBEZEICHNUNG];
			} else if ("FremdmaterialArtikelzusatzbezeichnung2"
					.equals(fieldName)) {
				value = data[index][BEGL_FREMDMATERIAL_ARTIKELZUSATZBEZEICHNUNG2];
			} else if ("F_MATERIAL_SNRCHNR".equals(fieldName)) {
				value = data[index][BEGL_MATERIAL_SNRCHNR];
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
			}
		}
			break;
		case UC_ABLIEFERETIKETT: {
			if ("F_SNRCHNR".equals(fieldName)) {
				value = data[index][ABLIEFERETIKETT_SERIENNUMMER];
			} else if ("F_SUBREPORT_GERAETESNR".equals(fieldName)) {
				value = data[index][ABLIEFERETIKETT_SUBREPORT_GERAETESNR];
			} else if ("F_VERSION".equals(fieldName)) {
				value = data[index][ABLIEFERETIKETT_VERSION];
			} else if ("F_I_ID_BUCHUNG".equals(fieldName)) {
				value = data[index][ABLIEFERETIKETT_I_ID_BUCHUNG];
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
			} else if ("F_PERSONALNUMMER_MASCHINENINVENARNUMMER"
					.equals(fieldName)) {
				value = data[index][LOSZEITEN_PERSONALNUMMER_MASCHINENINVENARNUMMER];
			}
		}
			break;
		case UC_FEHLTEILE: {
			if ("F_ARTIKELNUMMER".equals(fieldName)) {
				value = data[index][FT_ARTIKELNUMMER];
			} else if ("F_AUSGEGEBEN".equals(fieldName)) {
				value = data[index][FT_AUSGEGEBEN];
			} else if ("F_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][FT_BEZEICHNUNG];
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
			} else if ("Losgroesse".equals(fieldName)) {
				value = data[index][HF_LOSGROESSE];
			} else if ("Erledigt".equals(fieldName)) {
				value = data[index][HF_ERLEDIGT];
			} else if ("PositionArtikelnummer".equals(fieldName)) {
				value = data[index][HF_POSITION_ARTIKELNUMMMER];
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
			} else if ("Material".equals(fieldName)) {
				value = data[index][HF_POSITION_MAT_ODER_AZ];
			} else if ("Auftragsnummer".equals(fieldName)) {
				value = data[index][HF_AUFTRAGSNUMMER];
			} else if ("Fertigungsgruppe".equals(fieldName)) {
				value = data[index][HF_FERTIGUNGSGRUPPE];
			}
		}
			break;
		case UC_OFFENE: {
			if ("F_AUFTRAGNUMMER".equals(fieldName)) {
				value = data[index][OFFENE_AUFTRAGSNUMMER];
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
			} else if ("F_PROJEKT".equals(fieldName)) {
				value = data[index][OFFENE_PROJEKT];
			} else if ("F_FERTIGUNGSGRUPPE".equals(fieldName)) {
				value = data[index][OFFENE_FERTIGUNGSGRUPPE];
			} else if ("F_FEHLMENGE".equals(fieldName)) {
				value = data[index][OFFENE_FEHLMENGE];
			} else if ("F_RESERVIERUNGEN".equals(fieldName)) {
				value = data[index][OFFENE_RESERVIERUNGEN];
			} else if (F_ARTIKELRAHMENMENGE_OFFEN.equals(fieldName)) {
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
			} else if ("F_MATERIAL".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_MATERIAL];
			} else if ("F_BEGINN".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_BEGINN];
			} else if ("F_ENDE".equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_ENDE];
			} else if (F_ZUSATZBEZEICHNUNG.equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_ZUSATZBEZEICHNUNG];
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
			} else if ("F_AUFTRAGSKUNDE_ANSPRECHPARTNER_TELEFONDW"
					.equals(fieldName)) {
				value = data[index][AUSLIEFERLISTE_AUFTRAGSKUNDE_ANSPRECHPARTNER_TELEFONDW];
			} else if ("F_AUFTRAGSKUNDE_AUFTRAG_POSITIONSTERMIN"
					.equals(fieldName)) {
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
			} else if ("F_BELEGART".equals(fieldName)) {
				value = data[index][FMAL_BELEGART];
			} else if ("F_STUECKLISTEBEZEICHNUNG".equals(fieldName)) {
				value = data[index][FMAL_STUECKLISTEBEZEICHNUNG];
			} else if ("F_STUECKLISTEIDENT".equals(fieldName)) {
				value = data[index][FMAL_STUECKLISTENUMMER];
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
		case UC_MASCHINEUNDMATERIAL: {
			if ("Losnummer".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_LOSNUMMER];
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
			} else if ("ArtikelbezeichnungMaterial".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_ARTIKELBEZEICHNUNG_MATERIAL];
			} else if ("GesamtzeitInStunden".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_GESAMTZEIT_IN_STUNDEN];
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
			} else if ("Next_GesamtzeitInStunden".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_NEXT_AG_GESAMTZEIT_IN_STUNDEN];
			} else if ("Next_MaschinenversatzMS".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_NEXT_AG_MASCHINENVERSATZ_MS];
			} else if ("KundeKbez".equals(fieldName)) {
				value = data[index][MASCHINEUNDMATERIAL_KUNDE_KBEZ];
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
			}
		}
			break;
		}

		return value;
	}

	public JasperPrintLP printTheoretischeFehlmengen(Integer losIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Session session = null;
		try {
			this.useCase = UC_THEORETISCHE_FEHLMENGEN;
			this.index = -1;
			// Los holen und status pruefen
			LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);
			if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
						"");
			}
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRLossollmaterial.class);
			c.add(Restrictions.eq(FertigungFac.FLR_LOSSOLLMATERIAL_LOS_I_ID,
					losIId));
			// Sortierung nach Artikelnummer

			c.createAlias(FertigungFac.FLR_LOSSOLLMATERIAL_FLRARTIKEL, "a")
					.addOrder(Order.asc("a.c_nr"));
			List<?> list = c.list();
			// Laeger holen
			LoslagerentnahmeDto[] laeger = getFertigungFac()
					.loslagerentnahmeFindByLosIId(losIId);
			// positionen verdichten
			ArrayList<ReportTheoretischeFehlmengenDto> listVerdichtet = new ArrayList<ReportTheoretischeFehlmengenDto>();
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRLossollmaterial mat = (FLRLossollmaterial) iter.next();

				LossollmaterialDto[] lossollmaterialDtoErsatztypen = getFertigungFac()
						.lossollmaterialFindByLossollmaterialIIdOriginal(
								mat.getI_id());

				boolean bBereitsZugeordnet = false;
				for (int i = 0; i < listVerdichtet.size(); i++) {
					ReportTheoretischeFehlmengenDto temp = listVerdichtet
							.get(i);

					if (temp.getArtikelIId().equals(
							mat.getFlrartikel().getI_id())) {
						if (temp.getErsatztypen() != null
								&& temp.getErsatztypen().length > 0) {
							// nicht verdichten
							break;
						} else {
							if (lossollmaterialDtoErsatztypen != null
									&& lossollmaterialDtoErsatztypen.length > 0) {
								// nicht verdichten
								break;
							} else if (mat.getLossollmaterial_i_id_original() != null
									|| temp.getArtikelIIdOriginal() != null) {
								// nicht verdichten
								break;
							} else {
								// verdichten
								temp.addiereZuMenge(mat.getN_menge());
								temp.addiereZuAusgegebenerMenge(getFertigungFac()
										.getAusgegebeneMenge(mat.getI_id(),
												null, theClientDto));
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
					dto.addiereZuAusgegebenerMenge(getFertigungFac()
							.getAusgegebeneMenge(mat.getI_id(), null,
									theClientDto));
					if (mat.getLossollmaterial_i_id_original() != null) {
						LossollmaterialDto sollmatDto = getFertigungFac()
								.lossollmaterialFindByPrimaryKey(
										mat.getLossollmaterial_i_id_original());
						dto.setArtikelIIdOriginal(sollmatDto.getArtikelIId());
					}
					listVerdichtet.add(dto);
				}

			}
			data = new Object[listVerdichtet.size()][TF_SPALTENANZAHL];
			for (int i = 0; i < listVerdichtet.size(); i++) {
				ReportTheoretischeFehlmengenDto item = (ReportTheoretischeFehlmengenDto) listVerdichtet
						.get(i);

				Integer artikelIId = item.getArtikelIId();

				if (item.getArtikelIIdOriginal() != null) {
					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(
									item.getArtikelIIdOriginal(), theClientDto);
					data[i][TF_IDENT] = artikelDto.getCNr();
					data[i][TF_BEZEICHNUNG] = artikelDto.getArtikelsprDto()
							.getCBez();
					ArtikelDto artikelDtoErsatz = getArtikelFac()
							.artikelFindByPrimaryKeySmall(item.getArtikelIId(),
									theClientDto);
					data[i][TF_ERSATZIDENT] = artikelDtoErsatz.getCNr();
					data[i][TF_ERSATZBEZEICHNUNG] = artikelDtoErsatz
							.getArtikelsprDto().getCBez();
					data[i][TF_ZUSATZBEZEICHNUNG] = artikelDto
							.getArtikelsprDto().getCZbez();
					artikelIId = item.getArtikelIId();
				} else {
					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(item.getArtikelIId(),
									theClientDto);
					data[i][TF_IDENT] = artikelDto.getCNr();
					data[i][TF_BEZEICHNUNG] = artikelDto.getArtikelsprDto()
							.getCBez();
					data[i][TF_ERSATZIDENT] = "";
					data[i][TF_ZUSATZBEZEICHNUNG] = artikelDto
							.getArtikelsprDto().getCZbez();
				}

				data[i][TF_SOLLMENGE] = item.getMenge();

				data[i][TF_ISTMENGE] = item.getBdAusgegebeneMenge();

				try {
					data[i][TF_ARTIKELSPERREN] = getArtikelFac()
							.getArtikelsperrenText(artikelIId);
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				BigDecimal bdLagerstand = new BigDecimal(0);
				for (int j = 0; j < laeger.length; j++) {
					bdLagerstand = bdLagerstand.add(getLagerFac()
							.getLagerstandOhneExc(artikelIId,
									laeger[j].getLagerIId(), theClientDto));
				}
				BigDecimal bdFehlmenge = getFehlmengeFac()
						.getAnzahlFehlmengeEinesArtikels(artikelIId,
								theClientDto);
				BigDecimal bdReserviert = getReservierungFac()
						.getAnzahlReservierungen(artikelIId, theClientDto);
				BigDecimal bdVerfuegbar = bdLagerstand.subtract(bdFehlmenge)
						.subtract(bdReserviert);
				// die Reservierungen auf diesem Los darf ich nicht
				// beruecksichtigen
				BigDecimal bdFehlmengeTheoretisch;
				Integer iMoeglich;
				// wenn der lagerstand >= als die sollmenge -> th.fehlmenge=0
				if (bdLagerstand.add(item.getBdAusgegebeneMenge()).compareTo(
						item.getMenge()) >= 0) {
					bdFehlmengeTheoretisch = new BigDecimal(0);
					iMoeglich = new Integer(losDto.getNLosgroesse().intValue());
				} else {
					bdFehlmengeTheoretisch = item.getMenge().subtract(
							bdLagerstand.add(item.getBdAusgegebeneMenge()));
					BigDecimal bdMoeglich = bdLagerstand
							.add(item.getBdAusgegebeneMenge())
							.multiply(losDto.getNLosgroesse())
							.divide(item.getMenge(), 0, BigDecimal.ROUND_DOWN);
					iMoeglich = new Integer(bdMoeglich.intValue());
				}
				data[i][TF_VERFUEGBAR] = bdVerfuegbar;
				data[i][TF_LAGERSTAND] = bdLagerstand; // zum testen
				data[i][TF_FEHLMENGE] = bdFehlmengeTheoretisch;
				data[i][TF_BESTELLT] = getArtikelbestelltFac()
						.getAnzahlBestellt(artikelIId);
				data[i][TF_MOEGLICH] = iMoeglich;

				// Verfuegbarkeit bis zum Produktionsstart
				// PJ 14249
				// Fehlmengen
				Session sessionFehlmengen = FLRSessionFactory.getFactory()
						.openSession();

				String query = "SELECT SUM(f.n_menge) FROM FLRFehlmenge AS f WHERE f.artikel_i_id="
						+ artikelIId
						+ " AND f.t_liefertermin<='"
						+ Helper.formatDateWithSlashes(losDto
								.getTProduktionsbeginn())
						+ "'"
						+ " AND f.flrlossollmaterial.flrlos.c_nr<'"
						+ losDto.getCNr() + "'";
				Query qResult = sessionFehlmengen.createQuery(query);
				List<?> results = qResult.list();
				BigDecimal bdfehlmengenZumTermin = new BigDecimal(0);
				Iterator<?> resultListIterator = results.iterator();
				if (resultListIterator.hasNext()) {
					BigDecimal bd = (BigDecimal) resultListIterator.next();
					if (bd != null) {
						bdfehlmengenZumTermin = bd;
					}
				}

				sessionFehlmengen.close();
				// Reservierungen

				Session sessionReservierungen = FLRSessionFactory.getFactory()
						.openSession();
				BigDecimal reservierungen = new BigDecimal(0);
				query = "SELECT r FROM FLRArtikelreservierung AS r WHERE r.flrartikel.i_id="
						+ artikelIId;
				qResult = sessionReservierungen.createQuery(query);

				results = qResult.list();

				resultListIterator = results.iterator();
				while (resultListIterator.hasNext()) {
					FLRArtikelreservierung flrArtikelreservierung = (FLRArtikelreservierung) resultListIterator
							.next();

					if (Helper.cutTimestamp(
							flrArtikelreservierung.getT_liefertermin())
							.getTime() <= losDto.getTProduktionsbeginn()
							.getTime()) {
						{

						}

						if (flrArtikelreservierung.getC_belegartnr().equals(
								LocaleFac.BELEGART_LOS)) {

							LossollmaterialDto sollmatDto = getFertigungFac()
									.lossollmaterialFindByPrimaryKeyOhneExc(
											flrArtikelreservierung
													.getI_belegartpositionid());

							if (sollmatDto != null) {
								LosDto losvonreservierungDto = getFertigungFac()
										.losFindByPrimaryKey(
												sollmatDto.getLosIId());

								if (losvonreservierungDto.getCNr().compareTo(
										losDto.getCNr()) < 0) {
									reservierungen = reservierungen
											.add(flrArtikelreservierung
													.getN_menge());
								} else {
									continue;
								}

							}

						}
					}

				}

				sessionReservierungen.close();

				data[i][TF_VERFUEGBARZUMPRODUKTIONSSTART] = bdLagerstand
						.subtract(bdfehlmengenZumTermin).subtract(
								reservierungen);

			}

			// Sortieren nach Artikel und dann nach ersatzartikel

			for (int k = data.length - 1; k > 0; --k) {
				for (int j = 0; j < k; ++j) {
					Object[] a1 = (Object[]) data[j];
					Object[] a2 = (Object[]) data[j + 1];

					if (((String) a1[TF_IDENT])
							.compareTo((String) a2[TF_IDENT]) > 0) {
						data[j] = a2;
						data[j + 1] = a1;
					} else if (((String) a1[TF_IDENT])
							.compareTo((String) a2[TF_IDENT]) == 0) {
						String k1 = (String) a1[TF_ERSATZIDENT];
						String k2 = (String) a2[TF_ERSATZIDENT];

						if (k1.compareTo(k2) > 0) {
							data[j] = a2;
							data[j + 1] = a1;
						}
					}
				}
			}

			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			mapParameter.put("P_ANGELEGT", new java.util.Date(losDto
					.getTAnlegen().getTime()));
			String sAuftragsnummer;
			String sKunde;
			if (losDto.getAuftragIId() != null) {
				AuftragDto auftragDto = getAuftragFac()
						.auftragFindByPrimaryKey(losDto.getAuftragIId());
				sAuftragsnummer = auftragDto.getCNr();
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						auftragDto.getKundeIIdAuftragsadresse(), theClientDto);
				sKunde = kundeDto.getPartnerDto()
						.getCName1nachnamefirmazeile1();
			} else {
				sAuftragsnummer = "";
				sKunde = "";
			}
			mapParameter.put("P_AUFTRAGNUMMER", sAuftragsnummer);
			KostenstelleDto kstDto = getSystemFac()
					.kostenstelleFindByPrimaryKey(losDto.getKostenstelleIId());
			mapParameter.put("P_KOSTENSTELLENUMMER", kstDto.getCNr());
			mapParameter.put("P_KUNDE", sKunde);
			mapParameter.put("P_LOSGROESSE", losDto.getNLosgroesse());
			mapParameter.put("P_LOSNUMMER", losDto.getCNr());
			mapParameter.put("P_PRODUKTIONSBEGINN",
					losDto.getTProduktionsbeginn());
			String sMengenEinheit = null;
			// Materialliste?
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stkDto = getStuecklisteFac()
						.stuecklisteFindByPrimaryKey(
								losDto.getStuecklisteIId(), theClientDto);
				// Einheit
				if (stkDto.getArtikelDto() != null) {
					if (stkDto.getArtikelDto().getEinheitCNr() != null) {
						sMengenEinheit = stkDto.getArtikelDto().getEinheitCNr();
					}
				}
				mapParameter.put("P_STUECKLISTEBEZEICHNUNG", stkDto
						.getArtikelDto().getArtikelsprDto().getCBez());
				mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG", stkDto
						.getArtikelDto().getArtikelsprDto().getCZbez());
				mapParameter.put("P_STUECKLISTENUMMER", stkDto.getArtikelDto()
						.getCNr());
			} else {
				mapParameter.put("P_STUECKLISTEBEZEICHNUNG",
						losDto.getCProjekt());
				mapParameter.put(
						"P_STUECKLISTENUMMER",
						getTextRespectUISpr("fert.materialliste",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			}
			mapParameter.put("P_MENGENEINHEIT", sMengenEinheit);
			ArrayList<Object> images = new ArrayList<Object>();
			String sLosStuecklisteArtikelKommentar = "";
			// Bild einfuegen
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stuecklisteDto = getStuecklisteFac()
						.stuecklisteFindByPrimaryKey(
								losDto.getStuecklisteIId(), theClientDto);
				ArtikelkommentarDto[] artikelkommentarDto = getArtikelkommentarFac()
						.artikelkommentardruckFindByArtikelIIdBelegartCNr(
								stuecklisteDto.getArtikelIId(),
								LocaleFac.BELEGART_LOS,
								theClientDto.getLocUiAsString(), theClientDto);

				if (artikelkommentarDto != null
						&& artikelkommentarDto.length > 0) {

					for (int j = 0; j < artikelkommentarDto.length; j++) {
						if (artikelkommentarDto[j].getArtikelkommentarsprDto() != null) {
							// Text Kommentar
							if (artikelkommentarDto[j]
									.getDatenformatCNr()
									.trim()
									.indexOf(
											MediaFac.DATENFORMAT_MIMETYPEART_TEXT) != -1) {
								if (sLosStuecklisteArtikelKommentar == "") {
									sLosStuecklisteArtikelKommentar += artikelkommentarDto[j]
											.getArtikelkommentarsprDto()
											.getXKommentar();
								} else {
									sLosStuecklisteArtikelKommentar += "\n"
											+ artikelkommentarDto[j]
													.getArtikelkommentarsprDto()
													.getXKommentar();
								}
							} else if (artikelkommentarDto[j]
									.getDatenformatCNr()
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
			if (sLosStuecklisteArtikelKommentar != "") {
				mapParameter
						.put("P_STUECKLISTEARTIKELKOMMENTAR",
								Helper.formatStyledTextForJasper(sLosStuecklisteArtikelKommentar));
			}

			super.initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL,
					FertigungReportFac.REPORT_THEORETISCHE_FEHLMENGEN,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
			JasperPrintLP print = getReportPrint();
			Integer cachedReportvariante = theClientDto.getReportvarianteIId();
			if (images != null) {
				for (int k = 0; k < images.size(); k++) {
					mapParameter = new HashMap<String, Object>();
					mapParameter.put("P_BILD", images.get(k));
					this.useCase = UC_GANZSEITIGESBILD;
					this.index = -1;
					data = new Object[1][1];
					data[0][0] = images.get(k);
					theClientDto.setReportvarianteIId(cachedReportvariante);
					initJRDS(mapParameter, REPORT_MODUL_ALLGEMEIN,
							REPORT_GANZSEITIGESBILD, theClientDto.getMandant(),
							theClientDto.getLocUi(), theClientDto);
					print = Helper.addReport2Report(print, getReportPrint()
							.getPrint());
				}
			}
			return print;
		} catch (Exception t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, t);
		} finally {
			closeSession(session);
		}
	}

	public JasperPrintLP printLosEtikett(Integer losIId, BigDecimal bdMenge,
			String sKommentar, boolean bMitInhalten, Integer iExemplare,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
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
				AuftragDto auftragDto = getAuftragFac()
						.auftragFindByPrimaryKey(losDto.getAuftragIId());
				if (auftragDto != null) {
					parameter.put("P_AUFTRAG", auftragDto.getCNr());
					parameter.put("P_ROHS",
							Helper.short2Boolean(auftragDto.getBRoHs()));
					if (auftragDto.getKundeIIdAuftragsadresse() != null) {
						KundeDto kundeDto = getKundeFac()
								.kundeFindByPrimaryKey(
										auftragDto.getKundeIIdAuftragsadresse(),
										theClientDto);
						if (kundeDto != null) {
							if (kundeDto.getPartnerDto() != null) {
								parameter.put("P_KUNDE_ANREDE", kundeDto
										.getPartnerDto()
										.formatFixTitelName1Name2());
								parameter.put("P_KUNDE_ABTEILUNG", kundeDto
										.getPartnerDto()
										.getCName3vorname2abteilung());
							}
							if (kundeDto.getPartnerDto().getLandplzortDto() == null) {
								if (kundeDto.getPartnerDto().getLandplzortIId() != null) {
									LandplzortDto lpoDto = getSystemFac()
											.landplzortFindByPrimaryKey(
													kundeDto.getPartnerDto()
															.getLandplzortIId());
									kundeDto.getPartnerDto().setLandplzortDto(
											lpoDto);
								}
							}
							if (kundeDto.getPartnerDto().getLandplzortDto() != null) {
								parameter.put("P_KUNDE_LANDPLZORT", kundeDto
										.getPartnerDto().getLandplzortDto()
										.formatLandPlzOrt());
							}
						}
					}
				}
			}
			if (losDto.getAuftragpositionIId() != null) {
				AuftragpositionDto auftragposDto = getAuftragpositionFac()
						.auftragpositionFindByPrimaryKey(
								losDto.getAuftragpositionIId());
				if (auftragposDto != null) {
					parameter
							.put("P_AUFTRAGPOSITION", auftragposDto.getISort());
				}
			}
			if (losDto.getKostenstelleIId() != null) {
				KostenstelleDto kostDto = getSystemFac()
						.kostenstelleFindByPrimaryKey(
								losDto.getKostenstelleIId());
				if (kostDto != null) {
					parameter.put("P_KOSTENSTELLENNUMMER", kostDto.getCNr());
					parameter.put("P_KOSTENSTELLENBEZEICHNUNG",
							kostDto.getCBez());
				}
			}
			if (losDto.getStuecklisteIId() != null) {
				parameter.put("P_LOSART", FertigungFac.LOSART_IDENT);
				StuecklisteDto stuecklisteDto = getStuecklisteFac()
						.stuecklisteFindByPrimaryKey(
								losDto.getStuecklisteIId(), theClientDto);
				if (stuecklisteDto != null) {
					if (stuecklisteDto.getArtikelDto() == null) {
						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKey(
										stuecklisteDto.getArtikelIId(),
										theClientDto);
						stuecklisteDto.setArtikelDto(artikelDto);
					}
					// Nochmal abfragen falls Artikel nicht gefunden wurde
					if (stuecklisteDto.getArtikelDto() != null) {
						parameter.put("P_STUECKLISTENNUMMER", stuecklisteDto
								.getArtikelDto().getCNr());
						if (stuecklisteDto.getArtikelDto().getArtikelsprDto() == null) {
							ArtikelsprDto artikelsprDto = getArtikelFac()
									.artikelsprFindByArtikelIIdLocaleCNrOhneExc(
											stuecklisteDto.getArtikelIId(),
											theClientDto.getLocUiAsString(),
											theClientDto);
							stuecklisteDto.getArtikelDto().setArtikelsprDto(
									artikelsprDto);
						}
						if (stuecklisteDto.getArtikelDto().getArtikelsprDto() != null) {
							parameter.put("P_STUECKLISTENBEZEICHNUNG",
									stuecklisteDto.getArtikelDto()
											.getArtikelsprDto().getCBez());
							parameter.put("P_ARTIKELZUSATZBEZEICHNUNG",
									stuecklisteDto.getArtikelDto()
											.getArtikelsprDto().getCZbez());
							parameter.put("P_ARTIKELKURZBEZEICHNUNG",
									stuecklisteDto.getArtikelDto()
											.getArtikelsprDto().getCKbez());
						}

						parameter
								.put("P_STUECKLISTEREFERENZNUMMER",
										stuecklisteDto.getArtikelDto()
												.getCReferenznr());
						parameter.put("P_STUECKLISTEMENGENEINHEIT",
								stuecklisteDto.getArtikelDto().getEinheitCNr());
					}
				}
			} else {
				parameter.put("P_LOSART", FertigungFac.LOSART_MATERIALLISTE);
			}
			parameter.put("P_LOSGROESSE", losDto.getNLosgroesse());
			if (losDto.getLagerIIdZiel() != null) {
				LagerDto lagerDto = getLagerFac().lagerFindByPrimaryKey(
						losDto.getLagerIIdZiel());
				if (lagerDto != null) {
					parameter.put("P_LAGER", lagerDto.getCNr());
				}
			}
			Date dBegin = losDto.getTProduktionsbeginn();
			Date dEnde = losDto.getTProduktionsende();
			parameter.put("P_BEGINN", dBegin);
			parameter.put("P_ENDE", dEnde);
			if ((dEnde != null) && (dBegin != null)) {
				parameter.put("P_DAUER",
						new Integer(Helper.getDifferenzInTagen(dBegin, dEnde)));
			}
			if (losDto.getPersonalIIdTechniker() != null) {
				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKey(
								losDto.getPersonalIIdTechniker(), theClientDto);
				if (personalDto != null) {
					parameter.put("P_TECHNIKERNUMMER",
							personalDto.getCPersonalnr());
					parameter.put("P_TECHNIKERNAME",
							personalDto.formatFixUFTitelName2Name1());
				}
			}
			parameter.put("P_PROJEKT", losDto.getCProjekt());
			parameter.put("P_KOMMENTAR", losDto.getCKommentar());
			if (losDto.getFertigungsgruppeIId() != null) {
				FertigungsgruppeDto fertDto = getStuecklisteFac()
						.fertigungsgruppeFindByPrimaryKey(
								losDto.getFertigungsgruppeIId());
				if (fertDto != null) {
					parameter.put("P_FERTIGUNGSGRUPPE", fertDto.getCBez());
				}
			}
			if (losDto.getPartnerIIdFertigungsort() != null) {
				PartnerDto partnerDto = getPartnerFac()
						.partnerFindByPrimaryKeyOhneExc(
								losDto.getPartnerIIdFertigungsort(),
								theClientDto);
				if (partnerDto != null) {
					parameter.put("P_FERTIGUNGSORT",
							partnerDto.formatTitelAnrede());
				}
			}
			parameter.put("P_TEXT", losDto.getXText());

			// Inhalt
			LossollmaterialDto[] sollmaterialDtos = getFertigungFac()
					.lossollmaterialFindByLosIIdOrderByISort(losIId);

			data = new Object[sollmaterialDtos.length][ETI_ANZAHL_FELDER];

			for (int i = 0; i < sollmaterialDtos.length; i++) {
				data[i][ETI_SOLLMENGE] = sollmaterialDtos[i].getNMenge();
				data[i][ETI_ISTMENGE] = getFertigungFac().getAusgegebeneMenge(
						sollmaterialDtos[i].getIId(), null, theClientDto);

				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(
								sollmaterialDtos[i].getArtikelIId(),
								theClientDto);

				data[i][ETI_ARTIKELNUMMER] = artikelDto.getCNr();
				if (artikelDto.getArtikelsprDto() != null) {
					data[i][ETI_BEZEICHNUNG] = artikelDto.getArtikelsprDto()
							.getCBez();
					data[i][ETI_KURZBEZEICHNUNG] = artikelDto
							.getArtikelsprDto().getCKbez();
					data[i][ETI_ZUSATZBEZEICHNUNG] = artikelDto
							.getArtikelsprDto().getCZbez();
					data[i][ETI_ZUSATZBEZEICHNUNG2] = artikelDto
							.getArtikelsprDto().getCZbez2();
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
				initJRDS(parameter, FertigungReportFac.REPORT_MODUL,
						FertigungReportFac.REPORT_LOSETIKETT1,
						theClientDto.getMandant(), theClientDto.getLocUi(),
						theClientDto);

				print = Helper.addReport2Report(print, getReportPrint()
						.getPrint());
			} else {
				initJRDS(parameter, FertigungReportFac.REPORT_MODUL,
						FertigungReportFac.REPORT_LOSETIKETT1,
						theClientDto.getMandant(), theClientDto.getLocUi(),
						theClientDto);

				print = getReportPrint();
			}

		}

		return print;

	}

	public JasperPrintLP printLosEtikettA4(Integer losIId, BigDecimal bdMenge,
			String sKommentar, boolean bMitInhalten, Integer iExemplare,
			TheClientDto theClientDto) throws RemoteException {
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

					AuftragDto auftragDto = getAuftragFac()
							.auftragFindByPrimaryKey(losDto.getAuftragIId());
					if (auftragDto != null) {

						zeile[ETI_A4_AUFTRAG] = auftragDto.getCNr();
						zeile[ETI_A4_ROHS] = Helper.short2Boolean(auftragDto
								.getBRoHs());

						if (auftragDto.getKundeIIdAuftragsadresse() != null) {
							KundeDto kundeDto = getKundeFac()
									.kundeFindByPrimaryKey(
											auftragDto
													.getKundeIIdAuftragsadresse(),
											theClientDto);
							if (kundeDto != null) {
								if (kundeDto.getPartnerDto() != null) {

									zeile[ETI_A4_KUNDE_ABTEILUNG] = kundeDto
											.getPartnerDto()
											.getCName3vorname2abteilung();
									zeile[ETI_A4_KUNDE_ANREDE] = kundeDto
											.getPartnerDto()
											.formatFixTitelName1Name2();

								}
								if (kundeDto.getPartnerDto().getLandplzortDto() == null) {
									if (kundeDto.getPartnerDto()
											.getLandplzortIId() != null) {
										LandplzortDto lpoDto = getSystemFac()
												.landplzortFindByPrimaryKey(
														kundeDto.getPartnerDto()
																.getLandplzortIId());
										kundeDto.getPartnerDto()
												.setLandplzortDto(lpoDto);
									}
								}
								if (kundeDto.getPartnerDto().getLandplzortDto() != null) {
									zeile[ETI_A4_KUNDE_LANDPLZORT] = kundeDto
											.getPartnerDto().getLandplzortDto()
											.formatLandPlzOrt();

								}
							}
						}
					}
				}
				if (losDto.getAuftragpositionIId() != null) {
					AuftragpositionDto auftragposDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(
									losDto.getAuftragpositionIId());
					if (auftragposDto != null) {

						zeile[ETI_A4_AUFTRAGSPOSITION] = auftragposDto
								.getISort();

					}
				}
				if (losDto.getKostenstelleIId() != null) {
					KostenstelleDto kostDto = getSystemFac()
							.kostenstelleFindByPrimaryKey(
									losDto.getKostenstelleIId());
					if (kostDto != null) {

						zeile[ETI_A4_KOSTENSTELLENNUMMER] = kostDto.getCNr();
						zeile[ETI_A4_KOSTENSTELLENBEZEICHNUNG] = kostDto
								.getCBez();

					}
				}
				if (losDto.getStuecklisteIId() != null) {

					zeile[ETI_A4_LOSART] = FertigungFac.LOSART_IDENT;

					StuecklisteDto stuecklisteDto = getStuecklisteFac()
							.stuecklisteFindByPrimaryKey(
									losDto.getStuecklisteIId(), theClientDto);
					if (stuecklisteDto != null) {
						if (stuecklisteDto.getArtikelDto() == null) {
							ArtikelDto artikelDto = getArtikelFac()
									.artikelFindByPrimaryKey(
											stuecklisteDto.getArtikelIId(),
											theClientDto);
							stuecklisteDto.setArtikelDto(artikelDto);
						}
						// Nochmal abfragen falls Artikel nicht gefunden wurde
						if (stuecklisteDto.getArtikelDto() != null) {

							zeile[ETI_A4_STUECKLISTENNUMMER] = stuecklisteDto
									.getArtikelDto().getCNr();
							if (stuecklisteDto.getArtikelDto()
									.getArtikelsprDto() == null) {
								ArtikelsprDto artikelsprDto = getArtikelFac()
										.artikelsprFindByArtikelIIdLocaleCNrOhneExc(
												stuecklisteDto.getArtikelIId(),
												theClientDto.getLocUiAsString(),
												theClientDto);
								stuecklisteDto.getArtikelDto()
										.setArtikelsprDto(artikelsprDto);
							}
							if (stuecklisteDto.getArtikelDto()
									.getArtikelsprDto() != null) {

								zeile[ETI_A4_STUECKLISTENBEZEICHNUNG] = stuecklisteDto
										.getArtikelDto().getArtikelsprDto()
										.getCBez();
								zeile[ETI_A4_STUECKLISTEKURZBEZEICHNUNG] = stuecklisteDto
										.getArtikelDto().getArtikelsprDto()
										.getCKbez();
								zeile[ETI_A4_STUECKLISTEZUSATZBEZEICHNUNG] = stuecklisteDto
										.getArtikelDto().getArtikelsprDto()
										.getCZbez();

							}
							zeile[ETI_A4_STUECKLISTEMENGENEINHEIT] = stuecklisteDto
									.getArtikelDto().getEinheitCNr();
							zeile[ETI_A4_STUECKLISTEREFERENZNUMMER] = stuecklisteDto
									.getArtikelDto().getCReferenznr();
						}
					}
				} else {
					zeile[ETI_A4_LOSART] = FertigungFac.LOSART_MATERIALLISTE;
				}

				zeile[ETI_A4_LOSGROESSE] = losDto.getNLosgroesse();

				if (losDto.getLagerIIdZiel() != null) {
					LagerDto lagerDto = getLagerFac().lagerFindByPrimaryKey(
							losDto.getLagerIIdZiel());
					if (lagerDto != null) {

						zeile[ETI_A4_LAGER] = lagerDto.getCNr();
					}
				}
				Date dBegin = losDto.getTProduktionsbeginn();
				Date dEnde = losDto.getTProduktionsende();

				zeile[ETI_A4_BEGINN] = dBegin;
				zeile[ETI_A4_ENDE] = dEnde;

				if ((dEnde != null) && (dBegin != null)) {
					zeile[ETI_A4_DAUER] = new Integer(
							Helper.getDifferenzInTagen(dBegin, dEnde));
				}
				if (losDto.getPersonalIIdTechniker() != null) {
					PersonalDto personalDto = getPersonalFac()
							.personalFindByPrimaryKey(
									losDto.getPersonalIIdTechniker(),
									theClientDto);
					if (personalDto != null) {

						zeile[ETI_A4_TECHNIKERNAME] = personalDto
								.formatFixUFTitelName2Name1();
						zeile[ETI_A4_TECHNIKERNUMMER] = personalDto
								.getCPersonalnr();

					}
				}

				zeile[ETI_A4_PROJEKT] = losDto.getCProjekt();
				zeile[ETI_A4_KOMMENTAR] = losDto.getCKommentar();

				if (losDto.getFertigungsgruppeIId() != null) {
					FertigungsgruppeDto fertDto = getStuecklisteFac()
							.fertigungsgruppeFindByPrimaryKey(
									losDto.getFertigungsgruppeIId());
					if (fertDto != null) {
						zeile[ETI_A4_FERTIGUNGSGRUPPE] = fertDto.getCBez();

					}
				}
				if (losDto.getPartnerIIdFertigungsort() != null) {
					PartnerDto partnerDto = getPartnerFac()
							.partnerFindByPrimaryKeyOhneExc(
									losDto.getPartnerIIdFertigungsort(),
									theClientDto);
					if (partnerDto != null) {
						zeile[ETI_A4_FERTIGUNGSORT] = partnerDto
								.formatTitelAnrede();

					}
				}

				zeile[ETI_A4_TEXT] = losDto.getXText();

				if (bMitInhalten == true) {

					// Inhalt
					LossollmaterialDto[] sollmaterialDtos = getFertigungFac()
							.lossollmaterialFindByLosIIdOrderByISort(losIId);

					for (int i = 0; i < sollmaterialDtos.length; i++) {

						Object[] zeileSoll = zeile.clone();
						zeileSoll[ETI_A4_SOLLMATERIAL_SOLLMENGE] = sollmaterialDtos[i]
								.getNMenge();
						zeileSoll[ETI_A4_SOLLMATERIAL_ISTMENGE] = getFertigungFac()
								.getAusgegebeneMenge(
										sollmaterialDtos[i].getIId(), null,
										theClientDto);

						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(
										sollmaterialDtos[i].getArtikelIId(),
										theClientDto);

						zeileSoll[ETI_A4_SOLLMATERIAL_ARTIKELNUMMER] = artikelDto
								.getCNr();
						if (artikelDto.getArtikelsprDto() != null) {
							zeileSoll[ETI_A4_SOLLMATERIAL_BEZEICHNUNG] = artikelDto
									.getArtikelsprDto().getCBez();
							zeileSoll[ETI_A4_SOLLMATERIAL_KURZBEZEICHNUNG] = artikelDto
									.getArtikelsprDto().getCKbez();
							zeileSoll[ETI_A4_SOLLMATERIAL_ZUSATZBEZEICHNUNG] = artikelDto
									.getArtikelsprDto().getCZbez();
							zeileSoll[ETI_A4_SOLLMATERIAL_ZUSATZBEZEICHNUNG2] = artikelDto
									.getArtikelsprDto().getCZbez2();
						}

						zeileSoll[ETI_A4_SOLLMATERIAL_EINHEIT] = artikelDto
								.getEinheitCNr();
						zeileSoll[ETI_A4_SOLLMATERIAL_REFERENZNUMMER] = artikelDto
								.getCReferenznr();
						alDaten.add(zeileSoll);
					}
				} else {
					alDaten.add(zeile);
				}
			}
		}
		data = new Object[alDaten.size()][ETI_A4_ANZAHL_FELDER];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(parameter, FertigungReportFac.REPORT_MODUL,
				FertigungReportFac.REPORT_LOSETIKETTA4,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();

	}

	public JasperPrintLP printAusgabeListe(Integer[] losIId,
			Integer iSortierung, boolean bVerdichtetNachIdent,
			boolean bVorrangigNachFarbcodeSortiert, Integer artikelklasseIId,
			String alternativerReport, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Session session = null;
		try {
			this.useCase = UC_AUSGABELISTE;
			this.index = -1;
			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			String lose = "";
			for (int i = 0; i < losIId.length; i++) {

				// Los holen
				LosDto losDto = getFertigungFac()
						.losFindByPrimaryKey(losIId[i]);
				lose += losDto.getCNr() + ", ";

				ArrayList<Object> al = new ArrayList<Object>();
				al.add(losDto.getCNr());

				if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN,
							al, null);
				} else if (losDto.getStatusCNr().equals(
						FertigungFac.STATUS_STORNIERT)) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT,
							al, null);
				}
			}
			data = getDataAusgabeListe(losIId, iSortierung,
					bVerdichtetNachIdent, bVorrangigNachFarbcodeSortiert,
					artikelklasseIId, theClientDto);
			LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId[0]);
			mapParameter = getParameterAusgabeliste(losDto, lose, theClientDto);

			String sLosStuecklisteArtikelKommentar = "";
			// Bild einfuegen
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stuecklisteDto = getStuecklisteFac()
						.stuecklisteFindByPrimaryKey(
								losDto.getStuecklisteIId(), theClientDto);
				ArtikelkommentarDto[] artikelkommentarDto = getArtikelkommentarFac()
						.artikelkommentardruckFindByArtikelIIdBelegartCNr(
								stuecklisteDto.getArtikelIId(),
								LocaleFac.BELEGART_LOS,
								theClientDto.getLocUiAsString(), theClientDto);

				// Sperren
				String sperren = getArtikelFac().getArtikelsperrenText(
						stuecklisteDto.getArtikelIId());

				mapParameter.put("P_STUECKLISTESPERRSTATUS", sperren);

				if (artikelkommentarDto != null
						&& artikelkommentarDto.length > 0) {

					ArrayList<Object[]> subreportArtikelkommentare = new ArrayList<Object[]>();

					String[] fieldnames = new String[] { "F_KOMMENTARART",
							"F_MIMETYPE", "F_BILD", "F_KOMMENTAR", };

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
											artikelkommentarDto[j]
													.getArtikelkommentarartIId(),
											theClientDto);

							oZeileVorlage[iFeld_Subreport_Kommentarart] = artikelkommentarartDto
									.getCNr();

							oZeileVorlage[iFeld_Subreport_Mimetype] = artikelkommentarDto[j]
									.getDatenformatCNr();
							oZeileVorlage[iFeld_Subreport_Kommentar] = artikelkommentarDto[j]
									.getArtikelkommentarsprDto()
									.getXKommentar();

							// Text Kommentar
							if (artikelkommentarDto[j]
									.getDatenformatCNr()
									.trim()
									.indexOf(
											MediaFac.DATENFORMAT_MIMETYPEART_TEXT) != -1) {
								if (sLosStuecklisteArtikelKommentar == "") {
									sLosStuecklisteArtikelKommentar += artikelkommentarDto[j]
											.getArtikelkommentarsprDto()
											.getXKommentar();
								} else {
									sLosStuecklisteArtikelKommentar += "\n"
											+ artikelkommentarDto[j]
													.getArtikelkommentarsprDto()
													.getXKommentar();
								}

								Object[] oZeile = oZeileVorlage.clone();
								subreportArtikelkommentare.add(oZeile);

							} else if (artikelkommentarDto[j]
									.getDatenformatCNr()
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

									Object[] oZeile = oZeileVorlage.clone();
									oZeile[iFeld_Subreport_Bild] = myImage;

									subreportArtikelkommentare.add(oZeile);

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
										Object[] oZeile = oZeileVorlage.clone();
										oZeile[iFeld_Subreport_Bild] = tiffs[k];

										subreportArtikelkommentare.add(oZeile);
									}
								}

							} else if (artikelkommentarDto[j]
									.getDatenformatCNr()
									.equals(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF)) {

								byte[] pdf = artikelkommentarDto[j]
										.getArtikelkommentarsprDto()
										.getOMedia();

								PDDocument document = null;

								try {

									InputStream myInputStream = new ByteArrayInputStream(
											pdf);

									document = PDDocument.load(myInputStream);
									int numPages = document.getNumberOfPages();
									PDFRenderer renderer = new PDFRenderer(
											document);

									for (int p = 0; p < numPages; p++) {

										BufferedImage image = renderer
												.renderImageWithDPI(p, 150); // Windows
										Object[] oZeile = oZeileVorlage.clone();
										oZeile[iFeld_Subreport_Bild] = image;

										subreportArtikelkommentare.add(oZeile);

									}
								} catch (IOException e) {
									e.printStackTrace();
									throw new EJBExceptionLP(
											EJBExceptionLP.FEHLER,
											e.getMessage());

								} finally {
									if (document != null) {

										try {
											document.close();
										} catch (IOException e) {
											e.printStackTrace();
											throw new EJBExceptionLP(
													EJBExceptionLP.FEHLER,
													e.getMessage());

										}
									}

								}

							}
						}
					}

					Object[][] dataSub = new Object[subreportArtikelkommentare
							.size()][fieldnames.length];
					dataSub = (Object[][]) subreportArtikelkommentare
							.toArray(dataSub);

					// SP2801
					mapParameter.put("P_SUBREPORT_ARTIKELKOMMENTAR",
							new LPDatenSubreport(dataSub, fieldnames));

				}
			}
			if (sLosStuecklisteArtikelKommentar != "") {
				mapParameter
						.put("P_STUECKLISTEARTIKELKOMMENTAR",
								Helper.formatStyledTextForJasper(sLosStuecklisteArtikelKommentar));
			}

			if (alternativerReport != null) {
				initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL,
						alternativerReport, theClientDto.getMandant(),
						theClientDto.getLocUi(), theClientDto);
			} else {

				String report = FertigungReportFac.REPORT_AUSGABELISTE;

				// PJ 17672
				FertigungsgruppeDto fertGruppeDto = getStuecklisteFac()
						.fertigungsgruppeFindByPrimaryKey(
								losDto.getFertigungsgruppeIId());
				if (fertGruppeDto.getIFormularnummer() != null) {
					report = report.replace(".",
							fertGruppeDto.getIFormularnummer() + ".");
				}

				initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, report,
						theClientDto.getMandant(), theClientDto.getLocUi(),
						theClientDto);
			}

			return getReportPrint();
		} catch (RemoteException t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, t);
		} finally {
			closeSession(session);
		}
	}

	public JasperPrintLP printProduktionsinformation(Integer losIId,
			TheClientDto theClientDto) {
		this.useCase = UC_PRODUKTIONSINFORMATION;

		Map<String, Object> mapParameter = new TreeMap<String, Object>();

		try {
			LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);
			KostenstelleDto kstDto = getSystemFac()
					.kostenstelleFindByPrimaryKey(losDto.getKostenstelleIId());

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
				AuftragDto auftragDto = getAuftragFac()
						.auftragFindByPrimaryKey(losDto.getAuftragIId());
				sAuftragsnummer = auftragDto.getCNr();
				sLieferart = getLocaleFac().lieferartFindByIIdLocaleOhneExc(
						auftragDto.getLieferartIId(), theClientDto.getLocUi(),
						theClientDto);

				if (auftragDto.getSpediteurIId() != null) {
					sSpediteur = getMandantFac().spediteurFindByPrimaryKey(
							auftragDto.getSpediteurIId())
							.getCNamedesspediteurs();
				}

				bPoenale = Helper.short2Boolean(auftragDto.getBPoenale());
				bRoHs = Helper.short2Boolean(auftragDto.getBRoHs());

				sKundenbestellnummer = auftragDto.getCBestellnummer();

				// Internen Kommentar aus dem Auftrag, abhaengig von einem
				// Mandantenparameter
				ParametermandantDto parameter = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_FERTIGUNG,
								ParameterFac.PARAMETER_KOMMENTAR_AM_FERTIGUNGSBEGLEITSCHEIN);
				short iValue = Short.parseShort(parameter.getCWert());
				boolean bDruckeKommentar = Helper.short2boolean(iValue);
				if (bDruckeKommentar) {
					sInternerKommentar = auftragDto.getXInternerkommentar();
				}
				// Kunde holen
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						auftragDto.getKundeIIdAuftragsadresse(), theClientDto);
				sKunde = kundeDto.getPartnerDto()
						.getCName1nachnamefirmazeile1();

				sAbteilung = kundeDto.getPartnerDto()
						.getCName3vorname2abteilung();

				if (losDto.getAuftragpositionIId() != null) {
					AuftragpositionDto aufposDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(
									losDto.getAuftragpositionIId());
					if (aufposDto.getTUebersteuerbarerLiefertermin() != null) {
						dLiefertermin = aufposDto
								.getTUebersteuerbarerLiefertermin();
					} else {
						dLiefertermin = auftragDto.getDLiefertermin();
					}
				} else {
					dLiefertermin = auftragDto.getDLiefertermin();
				}
			} else if (losDto.getKundeIId() != null) {
				// Kunde holen
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						losDto.getKundeIId(), theClientDto);
				sKunde = kundeDto.getPartnerDto()
						.getCName1nachnamefirmazeile1();

				sAbteilung = kundeDto.getPartnerDto()
						.getCName3vorname2abteilung();
			}

			mapParameter.put("P_AUFTRAGNUMMER", sAuftragsnummer);
			mapParameter.put("P_BESTELLNUMMER", sKundenbestellnummer);
			mapParameter.put("P_AUFTRAG_INTERNERKOMMENTAR", sInternerKommentar);
			mapParameter.put("P_AUFTRAG_KUNDE_ABTEILUNG", sAbteilung);
			mapParameter.put("P_LIEFERART", sLieferart);
			mapParameter.put("P_SPEDITEUR", sSpediteur);
			mapParameter.put("P_POENALE", bPoenale);
			mapParameter.put("P_ROHS", bRoHs);

			PersonalDto personalBenutzer = getPersonalFac()
					.personalFindByPrimaryKey(theClientDto.getIDPersonal(),
							theClientDto);
			mapParameter.put("P_BENUTZER", personalBenutzer.getCKurzzeichen());

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
			mapParameter.put("Mandantenadresse",
					Helper.formatMandantAdresse(mandantDto));

			mapParameter.put("P_ANGELEGT", new java.util.Date(losDto
					.getTAnlegen().getTime()));

			if (losDto.getTErledigt() != null) {
				mapParameter.put("P_ERLEDIGT", new java.util.Date(losDto
						.getTErledigt().getTime()));
			}

			mapParameter.put("P_KOSTENSTELLENUMMER", kstDto.getCNr());
			mapParameter.put("P_KUNDE", sKunde);
			mapParameter.put("P_LOSGROESSE", losDto.getNLosgroesse());
			mapParameter.put("P_LOSNUMMER", losDto.getCNr());
			mapParameter.put("P_PRODUKTIONSBEGINN",
					losDto.getTProduktionsbeginn());
			mapParameter.put("P_PRODUKTIONSENDE", losDto.getTProduktionsende());
			mapParameter.put("P_PROJEKT", losDto.getCProjekt());
			mapParameter.put("P_LOSKOMMENTAR", losDto.getCKommentar());
			mapParameter.put("P_LOSLANGTEXT", losDto.getXText());

			mapParameter.put("P_PRODUKTIONSINFORMATION",
					losDto.getXProduktionsinformation());

			mapParameter.put("P_LIEFERTERMIN", dLiefertermin);
			// Materialliste?
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stkDto = getStuecklisteFac()
						.stuecklisteFindByPrimaryKey(
								losDto.getStuecklisteIId(), theClientDto);

				mapParameter.put("P_STUECKLISTEBEZEICHNUNG", stkDto
						.getArtikelDto().getArtikelsprDto().getCBez());

				mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG", stkDto
						.getArtikelDto().getArtikelsprDto().getCZbez());
				mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG2", stkDto
						.getArtikelDto().getArtikelsprDto().getCZbez2());
				mapParameter.put("P_STUECKLISTENUMMER", stkDto.getArtikelDto()
						.getCNr());

				mapParameter.put("P_STUECKLISTE_GEWICHTKG", stkDto
						.getArtikelDto().getFGewichtkg());

				if (stkDto.getArtikelDto().getVerpackungDto() != null) {
					mapParameter.put("P_STUECKLISTE_BAUFORM", stkDto
							.getArtikelDto().getVerpackungDto().getCBauform());
					mapParameter.put("P_STUECKLISTE_VERPACKUNGSART", stkDto
							.getArtikelDto().getVerpackungDto()
							.getCVerpackungsart());
				}

				if (stkDto.getArtikelDto().getGeometrieDto() != null) {
					mapParameter
							.put("P_STUECKLISTE_BREITETEXT", stkDto
									.getArtikelDto().getGeometrieDto()
									.getCBreitetext());
					mapParameter.put("P_STUECKLISTE_BREITE", stkDto
							.getArtikelDto().getGeometrieDto().getFBreite());
					mapParameter.put("P_STUECKLISTE_HOEHE", stkDto
							.getArtikelDto().getGeometrieDto().getFHoehe());
					mapParameter.put("P_STUECKLISTE_TIEFE", stkDto
							.getArtikelDto().getGeometrieDto().getFTiefe());
				}

			} else {
				mapParameter.put("P_STUECKLISTEBEZEICHNUNG",
						losDto.getCProjekt());
				mapParameter.put(
						"P_STUECKLISTENUMMER",
						getTextRespectUISpr("fert.materialliste",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		ArrayList alData = new ArrayList();

		ArrayList<JCRDocDto> dokumente = getJCRDocFac().holeDokumenteZuUseCase(
				losIId, QueryParameters.UC_ID_LOS, theClientDto);

		for (int i = 0; i < dokumente.size(); i++) {

			if (((JCRDocDto) dokumente.get(i)).getsMIME() != null) {

				String sMime = ((JCRDocDto) dokumente.get(i)).getsMIME();

				if (".JPG".equals(sMime.toUpperCase())
						|| ".JPEG".equals(sMime.toUpperCase())
						|| ".GIF".equals(sMime.toUpperCase())
						|| ".PNG".equals(sMime.toUpperCase())
						|| ".TIFF".equals(sMime.toUpperCase())
						|| ".BMP".equals(sMime.toUpperCase())) {
					Object[] zeile = new Object[PI_ANZAHL_FELDER];

					zeile[PI_BILD] = Helper
							.byteArrayToImage(((JCRDocDto) dokumente.get(i))
									.getbData());
					zeile[PI_FILENAME] = ((JCRDocDto) dokumente.get(i))
							.getsFilename();
					zeile[PI_NAME] = ((JCRDocDto) dokumente.get(i)).getsName();
					zeile[PI_SCHLAGWORTE] = ((JCRDocDto) dokumente.get(i))
							.getsSchlagworte();

					alData.add(zeile);
				}

			}

		}

		data = new Object[alData.size()][PI_ANZAHL_FELDER];
		data = (Object[][]) alData.toArray(data);

		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL,
				FertigungReportFac.REPORT_PRODUKTIONSINFORMATION,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();

	}

	public boolean istErstlos(LosDto losDto, TheClientDto theClientDto) {

		if (losDto.getStuecklisteIId() != null) {

			SessionFactory factory = FLRSessionFactory.getFactory();
			org.hibernate.Session session = factory.openSession();
			Criteria c = session.createCriteria(FLRLosReport.class);
			c.add(Restrictions.eq(FertigungFac.FLR_LOS_MANDANT_C_NR,
					theClientDto.getMandant()));

			c.add(Restrictions.eq(FertigungFac.FLR_LOS_STUECKLISTE_I_ID,
					losDto.getStuecklisteIId()));
			c.add(Restrictions.lt(FertigungFac.FLR_LOS_C_NR, losDto.getCNr()));
			c.add(Restrictions.not(Restrictions.in(
					FertigungFac.FLR_LOS_STATUS_C_NR, new String[] {
							FertigungFac.STATUS_STORNIERT,
							FertigungFac.STATUS_GESTOPPT })));

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

	public JasperPrintLP printAblieferEtikett(Integer losablieferungIId,
			Integer iExemplare, BigDecimal bdHandmenge,
			TheClientDto theClientDto) {
		this.useCase = UC_ABLIEFERETIKETT;
		try {

			LosablieferungDto losablieferungDto = getFertigungFac()
					.losablieferungFindByPrimaryKey(losablieferungIId, false,
							theClientDto);
			LosDto losDto = getFertigungFac().losFindByPrimaryKey(
					losablieferungDto.getLosIId());

			Map<String, Object> parameter = new TreeMap<String, Object>();

			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stklDto = getStuecklisteFac()
						.stuecklisteFindByPrimaryKey(
								losDto.getStuecklisteIId(), theClientDto);
				ArtikelDto artikelDto = stklDto.getArtikelDto();

				parameter.put("P_ARTIKELNUMMER", artikelDto.getCNr());

				if (artikelDto.getArtikelsprDto() != null) {
					parameter.put("P_BEZEICHNUNG", artikelDto
							.getArtikelsprDto().getCBez());
					parameter.put("P_KURZBEZEICHNUNG", artikelDto
							.getArtikelsprDto().getCKbez());
					parameter.put("P_ZUSATZBEZEICHNUNG", artikelDto
							.getArtikelsprDto().getCZbez());
					parameter.put("P_ZUSATZBEZEICHNUNG2", artikelDto
							.getArtikelsprDto().getCZbez2());
				}

				// Erste gefundene Version anzeigen
				LagerbewegungDto[] lDto = getLagerFac()
						.lagerbewegungFindByBelegartCNrBelegartPositionIId(
								LocaleFac.BELEGART_LOSABLIEFERUNG,
								losablieferungDto.getIId());
				if (lDto != null && lDto.length > 0) {
					parameter.put("P_VERSION", lDto[0].getCVersion());
				}

				MandantDto mandantDto = getMandantFac()
						.mandantFindByPrimaryKey(theClientDto.getMandant(),
								theClientDto);

				parameter.put("P_EINHEIT", artikelDto.getEinheitCNr());
				parameter.put("P_REFERENZNUMMER", artikelDto.getCReferenznr());
				parameter.put("P_REVISION", artikelDto.getCRevision());
				parameter.put("P_INDEX", artikelDto.getCIndex());
				parameter.put("P_VERPACKUNGSMENGE",
						artikelDto.getFVerpackungsmenge());

				parameter.put("P_MANDANTADRESSE",
						Helper.formatMandantAdresse(mandantDto));

				if (artikelDto.getMaterialIId() != null) {
					MaterialDto materialDto = getMaterialFac()
							.materialFindByPrimaryKey(
									artikelDto.getMaterialIId(), theClientDto);
					parameter.put("P_MATERIAL", materialDto.getBezeichnung());
				}

				if (artikelDto.getGeometrieDto() != null) {
					parameter.put("P_BREITE", artikelDto.getGeometrieDto()
							.getFBreite());
					parameter.put("P_HOEHE", artikelDto.getGeometrieDto()
							.getFHoehe());
					parameter.put("P_TIEFE", artikelDto.getGeometrieDto()
							.getFTiefe());
				}

				if (artikelDto.getVerpackungDto() != null) {
					parameter.put("P_BAUFORM", artikelDto.getVerpackungDto()
							.getCBauform());
					parameter.put("P_VERPACKUNGSART", artikelDto
							.getVerpackungDto().getCVerpackungsart());
				}

				parameter.put("P_VERPACKUNGS_EAN",
						artikelDto.getCVerpackungseannr());
				parameter.put("P_VERKAUFS_EAN", artikelDto.getCVerkaufseannr());

				if (artikelDto.getHerstellerIId() != null) {
					HerstellerDto herstellerDto = getArtikelFac()
							.herstellerFindByPrimaryKey(
									artikelDto.getHerstellerIId(), theClientDto);
					parameter.put("P_HERSTELLER", herstellerDto.getCNr());
					parameter.put("P_HERSTELLER_NAME1", herstellerDto
							.getPartnerDto().getCName1nachnamefirmazeile1());
					parameter.put("P_HERSTELLER_NAME2", herstellerDto
							.getPartnerDto().getCName2vornamefirmazeile2());
				}

				ArtikellieferantDto[] artikellieferantDtos = getArtikelFac()
						.artikellieferantFindByArtikelIId(artikelDto.getIId(),
								theClientDto);

				if (artikellieferantDtos != null
						&& artikellieferantDtos.length > 0) {
					parameter.put(
							"P_LIEFERANT",
							getLieferantFac()
									.lieferantFindByPrimaryKey(
											artikellieferantDtos[0]
													.getLieferantIId(),
											theClientDto).getPartnerDto()
									.formatAnrede());
					parameter.put("P_LIEFERANT_ARTIKELNUMMER",
							artikellieferantDtos[0].getCArtikelnrlieferant());
					parameter.put("P_LIEFERANT_ARTIKELBEZEICHNUNG",
							artikellieferantDtos[0].getCBezbeilieferant());
				}
				Integer iIdHauplpager = getLagerFac()
						.getHauptlagerDesMandanten(theClientDto).getIId();
				if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet())) {
					parameter.put(
							"P_LAGERSTAND",
							getLagerFac().getLagerstand(artikelDto.getIId(),
									iIdHauplpager, theClientDto));
				}

				parameter.put(
						"P_LAGERORT",
						getLagerFac().getLagerplaezteEinesArtikels(
								artikelDto.getIId(), iIdHauplpager));

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

			// PJ18617
			PersonalDto personalDto = getPersonalFac()
					.personalFindByPrimaryKey(
							losablieferungDto.getPersonalIIdAendern(),
							theClientDto);
			parameter.put("P_PERSON_ABGELIEFERT_KURZZEICHEN",
					personalDto.getCKurzzeichen());
			parameter.put("P_PERSON_ABGELIEFERT",
					personalDto.formatFixUFTitelName2Name1());

			parameter.put("P_SNRCHNR", SeriennrChargennrMitMengeDto
					.erstelleStringAusMehrerenSeriennummern(losablieferungDto
							.getSeriennrChargennrMitMenge()));
			data = new Object[0][ABLIEFERETIKETT_ANZAHL_SPALTEN];
			if (losablieferungDto.getSeriennrChargennrMitMenge() != null) {

				data = new Object[losablieferungDto
						.getSeriennrChargennrMitMenge().size()][ABLIEFERETIKETT_ANZAHL_SPALTEN];

				for (int i = 0; i < losablieferungDto
						.getSeriennrChargennrMitMenge().size(); i++) {

					String snr = losablieferungDto
							.getSeriennrChargennrMitMenge().get(i)
							.getCSeriennrChargennr();

					data[i][ABLIEFERETIKETT_SERIENNUMMER] = snr;

					// PJ18632

					LagerbewegungDto[] lagerbewDtos = getLagerFac()
							.lagerbewegungFindByBelegartCNrBelegartPositionIIdCSeriennrchargennr(
									LocaleFac.BELEGART_LOSABLIEFERUNG,
									losablieferungDto.getIId(), snr);
					if (lagerbewDtos != null && lagerbewDtos.length > 0) {
						data[i][ABLIEFERETIKETT_I_ID_BUCHUNG] = lagerbewDtos[0]
								.getIIdBuchung();
					}

					data[i][ABLIEFERETIKETT_VERSION] = losablieferungDto
							.getSeriennrChargennrMitMenge().get(i)
							.getCVersion();

					GeraetesnrDto[] gsnrDto = getLagerFac()
							.getGeraeteseriennummerEinerLagerbewegung(
									LocaleFac.BELEGART_LOSABLIEFERUNG,
									losablieferungIId, snr);

					String[] fieldnames = new String[] { "F_ARTIKELNUMMER",
							"F_ARTIKELBEZEICHNUNG", "F_GERAETESNR" };
					Object[][] dataSub = new Object[gsnrDto.length][fieldnames.length];

					for (int k = 0; k < gsnrDto.length; k++) {
						ArtikelDto aDto = getArtikelFac()
								.artikelFindByPrimaryKey(
										gsnrDto[k].getArtikelIId(),
										theClientDto);
						dataSub[k][0] = aDto.getCNr();
						if (aDto.getArtikelsprDto() != null) {
							dataSub[k][1] = aDto.getArtikelsprDto().getCBez();
						}
						dataSub[k][2] = gsnrDto[k].getCSnr();
					}

					data[i][ABLIEFERETIKETT_SUBREPORT_GERAETESNR] = new LPDatenSubreport(
							dataSub, fieldnames);

				}
			}

			JasperPrintLP print = null;
			Integer cachedReportVarianteId = theClientDto
					.getReportvarianteIId();
			for (int i = 0; i < iExemplare; i++) {

				parameter.put("P_EXEMPLAR", new Integer(i + 1));
				parameter.put("P_EXEMPLAREGESAMT", iExemplare);

				theClientDto.setReportvarianteIId(cachedReportVarianteId);

				initJRDS(parameter, FertigungReportFac.REPORT_MODUL,
						FertigungReportFac.REPORT_ABLIEFERETIKETT,
						theClientDto.getMandant(), theClientDto.getLocUi(),
						theClientDto);
				print = print == null ? getReportPrint() : Helper
						.addReport2Report(print, getReportPrint().getPrint());
			}

			if (print != null) {
				PrintInfoDto values = new PrintInfoDto();
				values.setDocPath(new DocPath(new DocNodeLosAblieferung(
						losablieferungDto, losDto)));
				print.setOInfoForArchive(values);
			}

			return print;
		}

		catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, ex);
		}

	}

	public JasperPrintLP printFertigungsbegleitschein(Integer losIId,
			Boolean bStammtVonSchnellanlage, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Session session = null;
		try {
			this.useCase = UC_FERTIGUNGSBEGLEITSCHEIN;
			this.index = -1;
			// Los holen
			LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);
			if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN,
						"");
			} else if (losDto.getStatusCNr().equals(
					FertigungFac.STATUS_STORNIERT)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT,
						"");
			} else if (losDto.getStatusCNr().equals(
					FertigungFac.STATUS_ERLEDIGT)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
						"");
			}

			// Hole Parameter UNTERDRUECKE_ARTIKELNR_NULLTAETIGKEIT
			String artikelnrZuUnterdruecken = null;
			ParametermandantDto parameterNulltaetigkeit = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_UNTERDRUECKE_ARTIKELNR_NULLTAETIGKEIT);
			if (parameterNulltaetigkeit.getCWert() != null
					&& !parameterNulltaetigkeit.getCWert().equals("")
					&& !parameterNulltaetigkeit.getCWert().equals(" ")) {
				artikelnrZuUnterdruecken = parameterNulltaetigkeit.getCWert();
			}

			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRLossollarbeitsplan.class);
			if (artikelnrZuUnterdruecken != null) {
				c.createAlias(FertigungFac.FLR_LOSSOLLARBEITSPLAN_FLRARTIKEL,
						"a");
				c.add(Restrictions.not(Restrictions.and(
						Restrictions
								.isNotNull(FertigungFac.FLR_LOSSOLLARBEITSPLAN_MASCHINE_I_ID),
						Restrictions.eq("a.c_nr", artikelnrZuUnterdruecken))));
			}
			c.add(Restrictions.eq(FertigungFac.FLR_LOSSOLLARBEITSPLAN_LOS_I_ID,
					losIId));
			// Sortierung nach Arbeitsgang
			c.addOrder(Order
					.asc(FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_ARBEITSGANGNUMMER));
			c.addOrder(Order
					.asc(FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_UNTERARBEITSGANG));
			List<?> list = c.list();

			int iSizeMaterial = 0;
			ParametermandantDto parameterMaterial = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FERTIGUNG,
							ParameterFac.PARAMETER_FERTIGUNGSBEGLEITSCHEIN_MIT_MATERIAL);
			short iMaterial = Short.parseShort(parameterMaterial.getCWert());
			boolean bMitMaterial = Helper.short2boolean(iMaterial);
			if (bMitMaterial) {
				// Hier Material einfuegen
				Object[][] material = getDataAusgabeListe(
						new Integer[] { losIId }, Helper.SORTIERUNG_NACH_IDENT,
						true, false, null, theClientDto);

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

				}

			} else {
				data = new Object[list.size()][BEGL_SPALTENANZAHL];
			}

			int i = 0;
			for (Iterator<?> iter = list.iterator(); iter.hasNext(); i++) {
				FLRLossollarbeitsplan item = (FLRLossollarbeitsplan) iter
						.next();
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKey(
								item.getFlrartikel().getI_id(), theClientDto);
				data[i + iSizeMaterial][BEGL_IST_MATERIAL] = new Boolean(false);
				data[i + iSizeMaterial][BEGL_IDENT] = artikelDto.getCNr();
				data[i + iSizeMaterial][BEGL_NURZURINFORMATION] = Helper
						.short2Boolean(artikelDto.getbNurzurinfo());
				data[i + iSizeMaterial][BEGL_BEZEICHNUNG] = artikelDto
						.getArtikelsprDto().getCBez();
				data[i + iSizeMaterial][BEGL_ZUSATZBEZEICHNUNG] = artikelDto
						.getArtikelsprDto().getCZbez();
				data[i + iSizeMaterial][BEGL_ZUSATZBEZEICHNUNG2] = artikelDto
						.getArtikelsprDto().getCZbez2();
				data[i + iSizeMaterial][BEGL_ARBEITSGANG] = item
						.getI_arbeitsgangsnummer();
				data[i + iSizeMaterial][BEGL_UNTERARBEITSGANG] = item
						.getI_unterarbeitsgang();
				data[i + iSizeMaterial][BEGL_AGART] = item.getAgart_c_nr();
				data[i + iSizeMaterial][BEGL_AUFSPANNUNG] = item
						.getI_aufspannung();
				data[i + iSizeMaterial][BEGL_MATERIAL_REVISION] = artikelDto
						.getCRevision();
				data[i + iSizeMaterial][BEGL_MATERIAL_INDEX] = artikelDto
						.getCIndex();
				data[i + iSizeMaterial][BEGL_FERTIG] = Helper
						.short2Boolean(item.getB_fertig());

				// Ein Mandantenparameter entscheidet, ob auch die Sollzeiten
				// gedruckt werden
				ParametermandantDto parameter = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_FERTIGUNG,
								ParameterFac.FERTIGUNG_FERTIGUNGSBEGLEITSCHEIN_MIT_SOLLDATEN);
				short iValue = Short.parseShort(parameter.getCWert());
				boolean bDruckeSolldaten = Helper.short2boolean(iValue);
				if (bDruckeSolldaten) {
					data[i + iSizeMaterial][BEGL_RUESTZEIT] = item
							.getL_ruestzeit().divide(new BigDecimal(1000 * 60),
									4, BigDecimal.ROUND_HALF_EVEN);
					data[i + iSizeMaterial][BEGL_STUECKZEIT] = item
							.getL_stueckzeit().divide(
									new BigDecimal(1000 * 60), 4,
									BigDecimal.ROUND_HALF_EVEN);
					data[i + iSizeMaterial][BEGL_GESAMTZEIT] = item
							.getN_gesamtzeit();
				}
				LossollarbeitsplanDto l = getFertigungFac()
						.lossollarbeitsplanFindByPrimaryKey(item.getI_id());
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
					MaschineDto maschineDto = getZeiterfassungFac()
							.maschineFindByPrimaryKey(l.getMaschineIId());
					data[i + iSizeMaterial][BEGL_MASCHINE] = maschineDto
							.getCIdentifikationsnr();
					data[i + iSizeMaterial][BEGL_MASCHINE_BEZEICHNUNG] = maschineDto
							.getCBez();
				}

				if (l.getLossollmaterialIId() != null) {
					LossollmaterialDto posDto = getFertigungFac()
							.lossollmaterialFindByPrimaryKey(
									l.getLossollmaterialIId());
					ArtikelDto artikelDtoFremdmaterial = getArtikelFac()
							.artikelFindByPrimaryKeySmall(
									posDto.getArtikelIId(), theClientDto);
					data[i + iSizeMaterial][BEGL_FREMDMATERIAL_ARTIKEL] = artikelDtoFremdmaterial
							.getCNr();
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
					data[i + iSizeMaterial][BEGL_FREMDMATERIAL_SOLLMENGE] = posDto
							.getNMenge();

					LoslagerentnahmeDto[] laeger = getFertigungFac()
							.loslagerentnahmeFindByLosIId(posDto.getLosIId());
					if (laeger.length > 0) {
						ArtikellagerplaetzeDto artikellagerplaetzeDto = getLagerFac()
								.artikellagerplaetzeFindByArtikelIIdLagerIId(
										artikelDtoFremdmaterial.getIId(),
										laeger[0].getLagerIId());
						if (artikellagerplaetzeDto != null
								&& artikellagerplaetzeDto.getLagerplatzDto() != null) {
							data[i + iSizeMaterial][BEGL_FREMDMATERIAL_LAGERORT] = artikellagerplaetzeDto
									.getLagerplatzDto().getCLagerplatz();
						}
					}

				}

			}
			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			mapParameter.put("P_ANGELEGT", new java.util.Date(losDto
					.getTAnlegen().getTime()));
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
				AuftragDto auftragDto = getAuftragFac()
						.auftragFindByPrimaryKey(losDto.getAuftragIId());
				sAuftragsnummer = auftragDto.getCNr();
				sLieferart = getLocaleFac().lieferartFindByIIdLocaleOhneExc(
						auftragDto.getLieferartIId(), theClientDto.getLocUi(),
						theClientDto);

				if (auftragDto.getSpediteurIId() != null) {
					sSpediteur = getMandantFac().spediteurFindByPrimaryKey(
							auftragDto.getSpediteurIId())
							.getCNamedesspediteurs();
				}

				bPoenale = Helper.short2Boolean(auftragDto.getBPoenale());
				bRoHs = Helper.short2Boolean(auftragDto.getBRoHs());
				// Internen Kommentar aus dem Auftrag, abhaengig von einem
				// Mandantenparameter
				ParametermandantDto parameter = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_FERTIGUNG,
								ParameterFac.PARAMETER_KOMMENTAR_AM_FERTIGUNGSBEGLEITSCHEIN);
				short iValue = Short.parseShort(parameter.getCWert());
				boolean bDruckeKommentar = Helper.short2boolean(iValue);
				if (bDruckeKommentar) {
					sInternerKommentar = auftragDto.getXInternerkommentar();
				}
				// Kunde holen
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						auftragDto.getKundeIIdAuftragsadresse(), theClientDto);

				sKunde = kundeDto.getPartnerDto()
						.getCName1nachnamefirmazeile1();

				KundeDto kundeDtoLieferadresse = getKundeFac()
						.kundeFindByPrimaryKey(
								auftragDto.getKundeIIdLieferadresse(),
								theClientDto);
				sKundeLieferadresse = kundeDtoLieferadresse.getPartnerDto()
						.getCName1nachnamefirmazeile1();

				sAbteilung = kundeDto.getPartnerDto()
						.getCName3vorname2abteilung();

				if (losDto.getAuftragpositionIId() != null) {
					AuftragpositionDto aufposDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(
									losDto.getAuftragpositionIId());
					if (aufposDto.getTUebersteuerbarerLiefertermin() != null) {
						dLiefertermin = aufposDto
								.getTUebersteuerbarerLiefertermin();
					} else {
						dLiefertermin = auftragDto.getDLiefertermin();
					}
				} else {
					dLiefertermin = auftragDto.getDLiefertermin();
				}
			} else {
				sAuftragsnummer = "";

				if (losDto.getKundeIId() != null) {
					KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
							losDto.getKundeIId(), theClientDto);

					sKunde = kundeDto.getPartnerDto()
							.getCName1nachnamefirmazeile1();
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
			KostenstelleDto kstDto = getSystemFac()
					.kostenstelleFindByPrimaryKey(losDto.getKostenstelleIId());
			mapParameter.put("P_KOSTENSTELLENUMMER", kstDto.getCNr());
			mapParameter.put("P_KUNDE", sKunde);
			mapParameter.put("P_KUNDE_LIEFERADRESSE", sKundeLieferadresse);
			mapParameter.put("P_LOSGROESSE", losDto.getNLosgroesse());

			mapParameter.put("P_LOSKLASSEN",
					getLosLosKlassenAlsString(losDto.getIId()));

			mapParameter.put("P_LOSNUMMER", losDto.getCNr());
			mapParameter.put("P_PRODUKTIONSBEGINN",
					losDto.getTProduktionsbeginn());
			mapParameter.put("P_PRODUKTIONSENDE", losDto.getTProduktionsende());
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(losDto.getTProduktionsende());
			mapParameter.put("P_PRODUKTIONSENDE_KW",
					new Integer(gc.get(Calendar.WEEK_OF_YEAR)));

			mapParameter.put("P_LIEFERTERMIN", dLiefertermin);

			// Fertigungsort
			if (losDto.getPartnerIIdFertigungsort() != null) {
				PartnerDto partnerDto = getPartnerFac()
						.partnerFindByPrimaryKey(
								losDto.getPartnerIIdFertigungsort(),
								theClientDto);
				mapParameter.put("P_FERTIGUNGSORT",
						partnerDto.formatTitelAnrede());
			}

			// Erstlos
			mapParameter.put("P_ERSTLOS",
					new Boolean(istErstlos(losDto, theClientDto)));

			boolean bDruckeUeberschriftMaterial = false;
			if (bMitMaterial == true && iSizeMaterial > 0) {
				bDruckeUeberschriftMaterial = true;
			}

			mapParameter.put("P_DRUCKEUEBERSCHRIFTMATERIAL", new Boolean(
					bDruckeUeberschriftMaterial));

			ArrayList<Object[]> subreportArtikelkommentare = new ArrayList<Object[]>();

			String[] fieldnames = new String[] { "F_KOMMENTARART",
					"F_MIMETYPE", "F_BILD", "F_KOMMENTAR", "F_PDF_OBJECT" };

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
						.stuecklisteFindByPrimaryKey(
								losDto.getStuecklisteIId(), theClientDto);
				// Einheit
				if (stuecklisteDto.getArtikelDto() != null) {
					if (stuecklisteDto.getArtikelDto().getEinheitCNr() != null) {
						sMengenEinheit = stuecklisteDto.getArtikelDto()
								.getEinheitCNr();
					}
				}

				// Sperren
				String sperren = getArtikelFac().getArtikelsperrenText(
						stuecklisteDto.getArtikelIId());

				mapParameter.put("P_STUECKLISTESPERRSTATUS", sperren);

				// Abmessungen
				GeometrieDto geometrieDto = stuecklisteDto.getArtikelDto()
						.getGeometrieDto();
				if (geometrieDto != null) {
					mapParameter.put(P_ARTIKEL_BREITE,
							geometrieDto.getFBreite());
					mapParameter.put(P_ARTIKEL_HOEHE, geometrieDto.getFHoehe());
					mapParameter.put(P_ARTIKEL_TIEFE, geometrieDto.getFTiefe());
				}
				// Bauform
				VerpackungDto verpackungDto = stuecklisteDto.getArtikelDto()
						.getVerpackungDto();
				if (verpackungDto != null) {
					mapParameter.put("P_ARTIKEL_BAUFORM",
							verpackungDto.getCBauform());
					mapParameter.put("P_ARTIKEL_VERPACKUNGSART",
							verpackungDto.getCVerpackungsart());
				}

				mapParameter.put("P_INDEX", stuecklisteDto.getArtikelDto()
						.getCIndex());
				mapParameter.put("P_REVISION", stuecklisteDto.getArtikelDto()
						.getCRevision());

				ArtikelkommentarDto[] artikelkommentarDto = getArtikelkommentarFac()
						.artikelkommentardruckFindByArtikelIIdBelegartCNr(
								stuecklisteDto.getArtikelIId(),
								LocaleFac.BELEGART_LOS,
								theClientDto.getLocUiAsString(), theClientDto);

				if (artikelkommentarDto != null
						&& artikelkommentarDto.length > 0) {

					for (int j = 0; j < artikelkommentarDto.length; j++) {
						if (artikelkommentarDto[j].getArtikelkommentarsprDto() != null) {

							ArtikelkommentarartDto artikelkommentarartDto = getArtikelkommentarFac()
									.artikelkommentarartFindByPrimaryKey(
											artikelkommentarDto[j]
													.getArtikelkommentarartIId(),
											theClientDto);

							Object[] oZeileVorlage = new Object[iFeld_Subreport_iAnzahlSpalten];
							oZeileVorlage[iFeld_Subreport_Kommentarart] = artikelkommentarartDto
									.getCNr();

							oZeileVorlage[iFeld_Subreport_Mimetype] = artikelkommentarDto[j]
									.getDatenformatCNr();
							oZeileVorlage[iFeld_Subreport_Kommentar] = artikelkommentarDto[j]
									.getArtikelkommentarsprDto()
									.getXKommentar();

							// Text Kommentar
							if (artikelkommentarDto[j]
									.getDatenformatCNr()
									.trim()
									.indexOf(
											MediaFac.DATENFORMAT_MIMETYPEART_TEXT) != -1) {
								if (sLosStuecklisteArtikelKommentar == "") {
									sLosStuecklisteArtikelKommentar += artikelkommentarDto[j]
											.getArtikelkommentarsprDto()
											.getXKommentar();
								} else {
									sLosStuecklisteArtikelKommentar += "\n"
											+ artikelkommentarDto[j]
													.getArtikelkommentarsprDto()
													.getXKommentar();
								}

								Object[] oZeile = oZeileVorlage.clone();
								subreportArtikelkommentare.add(oZeile);

							} else if (artikelkommentarDto[j]
									.getDatenformatCNr()
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
									java.awt.image.BufferedImage myImage = Helper
											.byteArrayToImage(bild);

									Object[] oZeile = oZeileVorlage.clone();
									oZeile[iFeld_Subreport_Bild] = myImage;

									subreportArtikelkommentare.add(oZeile);

								}
							} else if (artikelkommentarDto[j]
									.getDatenformatCNr()
									.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF)) {

								byte[] bild = artikelkommentarDto[j]
										.getArtikelkommentarsprDto()
										.getOMedia();

								java.awt.image.BufferedImage[] tiffs = Helper
										.tiffToImageArray(bild);
								if (tiffs != null) {
									for (int k = 0; k < tiffs.length; k++) {

										Object[] oZeile = oZeileVorlage.clone();
										oZeile[iFeld_Subreport_Bild] = tiffs[k];

										subreportArtikelkommentare.add(oZeile);

									}
								}

							} else if (artikelkommentarDto[j]
									.getDatenformatCNr()
									.equals(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF)) {
								byte[] pdf = artikelkommentarDto[j]
										.getArtikelkommentarsprDto()
										.getOMedia();
								HVPdfReport pdfObject = new HVPdfReport(pdf);
								Object[] oZeile = oZeileVorlage.clone();
								oZeile[iFeld_Subreport_Pdf] = pdfObject;
								subreportArtikelkommentare.add(oZeile);
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
					.fertigungsgruppeFindByPrimaryKey(
							losDto.getFertigungsgruppeIId());
			mapParameter.put("P_FERTIGUNGSGRUPPE", fertGruppeDto.getCBez());

			// Materialliste?
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stkDto = getStuecklisteFac()
						.stuecklisteFindByPrimaryKey(
								losDto.getStuecklisteIId(), theClientDto);

				if (sLosStuecklisteArtikelKommentar != "") {
					mapParameter
							.put("P_STUECKLISTEARTIKELKOMMENTAR",
									Helper.formatStyledTextForJasper(sLosStuecklisteArtikelKommentar));
				}

				mapParameter.put("P_STUECKLISTEBEZEICHNUNG", stkDto
						.getArtikelDto().getArtikelsprDto().getCBez());

				mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG", stkDto
						.getArtikelDto().getArtikelsprDto().getCZbez());
				mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG2", stkDto
						.getArtikelDto().getArtikelsprDto().getCZbez2());

				mapParameter.put("P_STUECKLISTEKURZBEZEICHNUNG", stkDto
						.getArtikelDto().getArtikelsprDto().getCKbez());
				mapParameter.put("P_STUECKLISTEREFERENZNUMMER", stkDto
						.getArtikelDto().getCReferenznr());

				mapParameter.put("P_STUECKLISTENUMMER", stkDto.getArtikelDto()
						.getCNr());

				mapParameter.put("P_STUECKLISTE_GEWICHTKG", stkDto
						.getArtikelDto().getFGewichtkg());

				if (stkDto.getArtikelDto().getVerpackungDto() != null) {
					mapParameter.put("P_STUECKLISTE_BAUFORM", stkDto
							.getArtikelDto().getVerpackungDto().getCBauform());
					mapParameter.put("P_STUECKLISTE_VERPACKUNGSART", stkDto
							.getArtikelDto().getVerpackungDto()
							.getCVerpackungsart());
				}

				if (stkDto.getArtikelDto().getGeometrieDto() != null) {
					mapParameter
							.put("P_STUECKLISTE_BREITETEXT", stkDto
									.getArtikelDto().getGeometrieDto()
									.getCBreitetext());
					mapParameter.put("P_STUECKLISTE_BREITE", stkDto
							.getArtikelDto().getGeometrieDto().getFBreite());
					mapParameter.put("P_STUECKLISTE_HOEHE", stkDto
							.getArtikelDto().getGeometrieDto().getFHoehe());
					mapParameter.put("P_STUECKLISTE_TIEFE", stkDto
							.getArtikelDto().getGeometrieDto().getFTiefe());
				}

				// Stuecklisteneigenschaften
				StuecklisteeigenschaftDto[] stuecklisteeigenschaftDtos = getStuecklisteFac()
						.stuecklisteeigenschaftFindByStuecklisteIId(
								losDto.getStuecklisteIId());
				ArrayList<Object[]> al = new ArrayList<Object[]>();
				for (int j = 0; j < stuecklisteeigenschaftDtos.length; j++) {
					StuecklisteeigenschaftDto dto = stuecklisteeigenschaftDtos[j];

					Object[] o = new Object[2];
					String sStklEigenschaftArt = dto
							.getStuecklisteeigenschaftartDto().getCBez();
					o[0] = sStklEigenschaftArt;
					o[1] = dto.getCBez();
					al.add(o);

					// Index und Materialplatz auch einzeln an Report uebergeben
					if (sStklEigenschaftArt
							.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_INDEX)) {
						mapParameter.put(P_STUECKLISTENEIGENSCHAFT_INDEX,
								dto.getCBez());
					}
					if (sStklEigenschaftArt
							.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_MATERIALPLATZ)) {
						mapParameter.put(
								P_STUECKLISTENEIGENSCHAFT_MATERIALPLATZ,
								dto.getCBez());
					}

				}

				if (stuecklisteeigenschaftDtos.length > 0) {
					String[] fieldnamesEigenschaften = new String[] {
							"F_EIGENSCHAFTART", "F_BEZEICHNUNG" };
					Object[][] dataSub = new Object[al.size()][fieldnamesEigenschaften.length];
					dataSub = (Object[][]) al.toArray(dataSub);

					mapParameter.put("DATENSUBREPORT", new LPDatenSubreport(
							dataSub, fieldnamesEigenschaften));
				}

			} else {
				mapParameter.put("P_STUECKLISTEBEZEICHNUNG",
						losDto.getCProjekt());
				mapParameter.put(
						"P_STUECKLISTENUMMER",
						getTextRespectUISpr("fert.materialliste",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			}

			mapParameter.put("P_SCHNELLANLAGE", bStammtVonSchnellanlage);

			Object[][] dataSub = new Object[subreportArtikelkommentare.size()][fieldnames.length];
			dataSub = (Object[][]) subreportArtikelkommentare.toArray(dataSub);

			// SP2699
			mapParameter.put("P_SUBREPORT_ARTIKELKOMMENTAR",
					new LPDatenSubreport(dataSub, fieldnames));

			// Formularnummer anhaengen, wenn vorhanden
			String report = FertigungReportFac.REPORT_FERTIGUNGSBEGLEITSCHEIN;

			if (fertGruppeDto.getIFormularnummer() != null) {
				report = report.replace(".", fertGruppeDto.getIFormularnummer()
						+ ".");
			}

			initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL, report,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);

			JasperPrintLP print = getReportPrint();

			return print;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} finally {
			closeSession(session);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAlle(ReportJournalKriterienDto krit,
			boolean bNurAngelegte, Integer fertigungsgruppeIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Session session = null;
		try {
			this.useCase = UC_ALLE;
			this.index = -1;
			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRLosReport.class);
			c.add(Restrictions.eq(FertigungFac.FLR_LOS_MANDANT_C_NR,
					theClientDto.getMandant()));
			if (krit.kostenstelleIId != null) {
				c.add(Restrictions.eq(
						FertigungFac.FLR_LOSREPORT_KOSTENSTELLE_I_ID,
						krit.kostenstelleIId));
			}
			if (bNurAngelegte) {
				c.add(Restrictions.eq(FertigungFac.FLR_LOS_STATUS_C_NR,
						LocaleFac.STATUS_ANGELEGT));
			}
			if (fertigungsgruppeIId != null) {
				c.add(Restrictions.eq(
						FertigungFac.FLR_LOSREPORT_FERTIGUNGSGRUPPE_I_ID,
						fertigungsgruppeIId));

				FertigungsgruppeDto fertigungsgruppeDto = getStuecklisteFac()
						.fertigungsgruppeFindByPrimaryKey(fertigungsgruppeIId);
				mapParameter.put("P_FERTIGUNGSGRUPPE",
						fertigungsgruppeDto.getCBez());
			}

			String sVon = null;
			String sBis = null;
			if (krit.dVon != null) {
				c.add(Restrictions.ge(FertigungFac.FLR_LOS_T_PRODUKTIONSBEGINN,
						krit.dVon));
				sVon = Helper.formatDatum(krit.dVon, theClientDto.getLocUi());
			}
			if (krit.dBis != null) {
				c.add(Restrictions.le(FertigungFac.FLR_LOS_T_PRODUKTIONSBEGINN,
						krit.dBis));
				sBis = Helper.formatDatum(krit.dBis, theClientDto.getLocUi());
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
				sVon = HelperServer.getBelegnummernFilterForHibernateCriterias(
						f, iGeschaeftsjahr, sMandantKuerzel,
						krit.sBelegnummerVon);
				c.add(Restrictions.ge(FertigungFac.FLR_LOS_C_NR, sVon));
			}
			if (krit.sBelegnummerBis != null) {
				sBis = HelperServer.getBelegnummernFilterForHibernateCriterias(
						f, iGeschaeftsjahr, sMandantKuerzel,
						krit.sBelegnummerBis);
				c.add(Restrictions.le(FertigungFac.FLR_LOS_C_NR, sBis));
			}
			if (krit.bSortiereNachKostenstelle) {
				c.createCriteria(FertigungFac.FLR_LOSREPORT_FLRKOSTENSTELLE)
						.addOrder(Order.asc("c_nr"));
			}
			// if (krit.iSortierung ==
			// ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
			// c.createCriteria(FertigungFac.FLR_ER_FLRLIEFERANT).createCriteria(
			// LieferantFac.
			// FLR_PARTNER).addOrder(Order.asc(PartnerFac.
			// FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
			// }
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				c.createCriteria(FertigungFac.FLR_LOSREPORT_FLRAUFTRAG)
						.createCriteria(AuftragFac.FLR_AUFTRAG_FLRKUNDE)
						.createCriteria(KundeFac.FLR_PARTNER)
						.addOrder(
								Order.asc(PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1));
			}
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_BELEGNUMMER) {
				c.addOrder(Order.asc(FertigungFac.FLR_LOS_C_NR));
			}
			List<Object> list = c.list();
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				Criteria c2 = session.createCriteria(FLRLosReport.class);
				c2.add(Restrictions
						.isNull(FertigungFac.FLR_LOSREPORT_FLRAUFTRAG));
				c2.add(Restrictions.eq(FertigungFac.FLR_LOS_MANDANT_C_NR,
						theClientDto.getMandant()));
				if (krit.kostenstelleIId != null) {
					c2.add(Restrictions.eq(
							FertigungFac.FLR_LOSREPORT_KOSTENSTELLE_I_ID,
							krit.kostenstelleIId));
				}
				if (bNurAngelegte) {
					c2.add(Restrictions.eq(FertigungFac.FLR_LOS_STATUS_C_NR,
							LocaleFac.STATUS_ANGELEGT));
				}
				if (fertigungsgruppeIId != null) {
					c2.add(Restrictions.eq(
							FertigungFac.FLR_LOSREPORT_FERTIGUNGSGRUPPE_I_ID,
							fertigungsgruppeIId));
				}
				if (krit.dVon != null) {
					c2.add(Restrictions
							.ge(FertigungFac.FLR_LOS_T_PRODUKTIONSBEGINN,
									krit.dVon));
				}
				if (krit.dBis != null) {
					c2.add(Restrictions
							.le(FertigungFac.FLR_LOS_T_PRODUKTIONSBEGINN,
									krit.dBis));
				}
				List<?> list2 = c2.list();
				Iterator<?> iter2 = list2.iterator();
				while (iter2.hasNext()) {
					list.add(iter2.next());
				}
			}

			data = new Object[list.size()][ALLE_FELDANZAHL];
			int i = 0;
			for (Iterator<?> iter = list.iterator(); iter.hasNext(); i++) {
				FLRLosReport los = (FLRLosReport) iter.next();
				LossollarbeitsplanDto[] sollaz = getFertigungFac()
						.lossollarbeitsplanFindByLosIId(los.getI_id());
				BigDecimal bdArbeitszeitIstmenge = new BigDecimal(0);
				BigDecimal bdArbeitszeitSollmenge = new BigDecimal(0);
				BigDecimal bdArbeitszeitIstpreis = new BigDecimal(0);
				BigDecimal bdArbeitszeitSollpreis = new BigDecimal(0);
				for (int j = 0; j < sollaz.length; j++) {
					bdArbeitszeitSollmenge = bdArbeitszeitSollmenge
							.add(sollaz[j].getNGesamtzeit());
					ArtikellieferantDto artikellieferantDto = getArtikelFac()
							.getArtikelEinkaufspreis(
									sollaz[j].getArtikelIIdTaetigkeit(),
									new BigDecimal(1),
									theClientDto.getSMandantenwaehrung(),
									theClientDto);
					BigDecimal bdEinzelpreis;
					if (artikellieferantDto != null) {
						bdEinzelpreis = artikellieferantDto.getNEinzelpreis();
					} else {
						bdEinzelpreis = new BigDecimal(0);
					}
					bdArbeitszeitSollpreis = bdArbeitszeitSollpreis
							.add(sollaz[j].getNGesamtzeit().multiply(
									bdEinzelpreis));
				}
				AuftragzeitenDto[] zeiten = getZeiterfassungFac()
						.getAllZeitenEinesBeleges(LocaleFac.BELEGART_LOS,
								los.getI_id(), null, null, null, null, false,
								false, theClientDto);
				for (int j = 0; j < zeiten.length; j++) {
					bdArbeitszeitIstmenge = bdArbeitszeitIstmenge
							.add(new BigDecimal(zeiten[j].getDdDauer()
									.doubleValue()));
					bdArbeitszeitIstpreis = bdArbeitszeitIstpreis.add(zeiten[j]
							.getBdKosten());
				}
				data[i][ALLE_ARBEITSZEITISTMENGE] = bdArbeitszeitIstmenge;
				data[i][ALLE_ARBEITSZEITISTPREIS] = bdArbeitszeitIstpreis;
				data[i][ALLE_ARBEITSZEITSOLLMENGE] = bdArbeitszeitSollmenge;
				data[i][ALLE_ARBEITSZEITSOLLPREIS] = bdArbeitszeitSollpreis;
				if (los.getFlrstueckliste() != null) {
					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(
									los.getFlrstueckliste().getFlrartikel()
											.getI_id(), theClientDto);
					data[i][ALLE_BEZEICHNUNG] = artikelDto.getArtikelsprDto()
							.getCBez();
					if (artikelDto.getArtikelsprDto().getCZbez() != null) {
						data[i][ALLE_ZUSATZBEZEICHNUNG] = artikelDto
								.getArtikelsprDto().getCZbez();
					} else {
						data[i][ALLE_ZUSATZBEZEICHNUNG] = "";
					}
					data[i][ALLE_IDENT] = los.getFlrstueckliste()
							.getFlrartikel().getC_nr();
					data[i][ALLE_OFFENERAHMENRESERVIERUNGEN] = getReservierungFac()
							.getAnzahlRahmenreservierungen(artikelDto.getIId(),
									theClientDto);
				} else {
					data[i][ALLE_BEZEICHNUNG] = los.getC_projekt();
					data[i][ALLE_BEZEICHNUNG] = null;
					data[i][ALLE_IDENT] = getTextRespectUISpr(
							"fert.materialliste", theClientDto.getMandant(),
							theClientDto.getLocUi());
					data[i][ALLE_OFFENERAHMENRESERVIERUNGEN] = new BigDecimal(0);
				}
				data[i][ALLE_LOSNUMMER] = los.getC_nr();
				LossollmaterialDto[] sollmat = getFertigungFac()
						.lossollmaterialFindByLosIId(los.getI_id());
				BigDecimal bdMaterialIstmenge = new BigDecimal(0);
				BigDecimal bdMaterialSollmenge = new BigDecimal(0);
				BigDecimal bdMaterialIstpreis = new BigDecimal(0);
				BigDecimal bdMaterialSollpreis = new BigDecimal(0);
				for (int j = 0; j < sollmat.length; j++) {
					BigDecimal bdAusgegebeneMenge = getFertigungFac()
							.getAusgegebeneMenge(sollmat[j].getIId(), null,
									theClientDto);
					bdMaterialIstmenge = bdMaterialIstmenge
							.add(bdAusgegebeneMenge);
					bdMaterialSollmenge = bdMaterialSollmenge.add(sollmat[j]
							.getNMenge());
					bdMaterialIstpreis = bdMaterialIstpreis
							.add(bdAusgegebeneMenge.multiply(getFertigungFac()
									.getAusgegebeneMengePreis(
											sollmat[j].getIId(), null,
											theClientDto)));
					bdMaterialSollpreis = bdMaterialSollpreis.add(sollmat[j]
							.getNMenge().multiply(sollmat[j].getNSollpreis()));
				}
				data[i][ALLE_MATERIALISTMENGE] = bdMaterialIstmenge;
				data[i][ALLE_MATERIALISTPREIS] = bdMaterialIstpreis;
				data[i][ALLE_MATERIALSOLLMENGE] = bdMaterialSollmenge;
				data[i][ALLE_MATERIALSOLLPREIS] = bdMaterialSollpreis;
				data[i][ALLE_BEGINNTERMIN] = los.getT_produktionsbeginn();
				data[i][ALLE_LOSGROESSE] = los.getN_losgroesse();
				if (los.getFlrauftrag() != null) {
					data[i][ALLE_AUFTRAGLIEFERTERMIN] = los.getFlrauftrag()
							.getT_liefertermin();
					data[i][ALLE_AUFTRAGSNUMMER] = los.getFlrauftrag()
							.getC_nr();
					if (los.getFlrauftrag().getFlrkunde() != null) {
						if (los.getFlrauftrag().getFlrkunde().getFlrpartner() != null) {
							data[i][ALLE_KUNDE] = los.getFlrauftrag()
									.getFlrkunde().getFlrpartner()
									.getC_name1nachnamefirmazeile1();
						} else {
							data[i][ALLE_KUNDE] = "";
						}
					} else {
						data[i][ALLE_KUNDE] = "";
					}
				} else {
					data[i][ALLE_AUFTRAGSNUMMER] = "";
					data[i][ALLE_AUFTRAGLIEFERTERMIN] = null;
				}
				if (los.getFlrfertigungsgruppe() != null) {
					data[i][ALLE_FERTIGUNGSGRUPPE] = los
							.getFlrfertigungsgruppe().getC_bez();
				} else {
					data[i][ALLE_FERTIGUNGSGRUPPE] = "";
				}
			}

			StringBuffer sSortierung = new StringBuffer();
			// Sortierung nach Kostenstelle
			sSortierung.append(getTextRespectUISpr("lp.sortierungnach",
					theClientDto.getMandant(), theClientDto.getLocUi()) + ": ");
			mapParameter.put(LPReport.P_SORTIERENACHKOSTENSTELLE, new Boolean(
					krit.bSortiereNachKostenstelle));
			if (krit.bSortiereNachKostenstelle) {
				sSortierung.append(getTextRespectUISpr("lp.kostenstelle",
						theClientDto.getMandant(), theClientDto.getLocUi()));
				sSortierung.append(", ");
			}
			// Sortieung nach Kunde
			mapParameter
					.put(LPReport.P_SORTIERENACHKUNDE,
							new Boolean(
									krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER));
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				sSortierung.append(getTextRespectUISpr("lp.kunde",
						theClientDto.getMandant(), theClientDto.getLocUi()));
			} else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_BELEGNUMMER) {
				sSortierung.append(getTextRespectUISpr("lp.losnr",
						theClientDto.getMandant(), theClientDto.getLocUi()));
			}
			StringBuffer sFilter = new StringBuffer();
			sFilter.append(getTextRespectUISpr("lp.filter",
					theClientDto.getMandant(), theClientDto.getLocUi())
					+ ": ");
			if (bNurAngelegte) {
				sFilter.append(getTextRespectUISpr("lp.nichtausgegebene",
						theClientDto.getMandant(), theClientDto.getLocUi())
						+ ", ");
			}
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
			mapParameter.put(LPReport.P_SORTIERUNG, sSortierung.toString());
			mapParameter.put("P_VON", krit.dVon);
			mapParameter.put("P_BIS", krit.dBis);
			mapParameter.put(LPReport.P_FILTER, sFilter.toString());
			initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL,
					FertigungReportFac.REPORT_ALLE, theClientDto.getMandant(),
					theClientDto.getLocUi(), theClientDto);
			return getReportPrint();
		} catch (RemoteException t) {
			throwEJBExceptionLPRespectOld(t);
			return null;
		} finally {
			closeSession(session);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printOffene(java.sql.Date dStichtag,
			int iOptionStichtag, String belegNrVon, String belegNrBis,
			Integer kundeIId, Integer kostenstelleIId,
			Integer fertigungsgruppeIId, int iSortierung,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Session session = null;

		try {
			this.useCase = UC_OFFENE;
			this.index = -1;
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRLosReport.class);
			c.add(Restrictions.eq(FertigungFac.FLR_LOS_MANDANT_C_NR,
					theClientDto.getMandant()));
			Map<String, Object> mapParameter = new TreeMap<String, Object>();

			if (kostenstelleIId != null) {
				KostenstelleDto kostenstelleDto = getSystemFac()
						.kostenstelleFindByPrimaryKey(kostenstelleIId);
				mapParameter.put("P_KOSTENSTELLE",
						kostenstelleDto.formatKostenstellenbezeichnung());

				c.add(Restrictions.eq(
						FertigungFac.FLR_LOSREPORT_KOSTENSTELLE_I_ID,
						kostenstelleIId));
			}
			boolean flrAuftragSchonVerwendet = false;
			if (kundeIId != null) {
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						kundeIId, theClientDto);
				mapParameter.put("P_KUNDE", kundeDto.getPartnerDto()
						.formatFixTitelName1Name2());
				c.createAlias(FertigungFac.FLR_LOSREPORT_FLRAUFTRAG, "a",
						CriteriaSpecification.LEFT_JOIN);
				flrAuftragSchonVerwendet = true;
				c.add(Restrictions.eq("a."
						+ AuftragFac.FLR_AUFTRAG_KUNDE_I_ID_AUFTRAGSADRESSE,
						kundeIId));
			}
			if (fertigungsgruppeIId != null) {
				c.add(Restrictions.eq(
						FertigungFac.FLR_LOSREPORT_FERTIGUNGSGRUPPE_I_ID,
						fertigungsgruppeIId));
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
					c.add(Restrictions
							.lt(FertigungFac.FLR_LOS_T_PRODUKTIONSBEGINN,
									dStichtag));

					datumsart = getTextRespectUISpr("lp.begintermin",
							theClientDto.getMandant(), theClientDto.getLocUi());

				} else if (iOptionStichtag == FertigungReportFac.OFFENE_OPTION_STICHTAG_ENDEDATUM) {
					c.add(Restrictions.lt(
							FertigungFac.FLR_LOS_T_PRODUKTIONSENDE, dStichtag));
					datumsart = getTextRespectUISpr("lp.endetermin",
							theClientDto.getMandant(), theClientDto.getLocUi());

				} else if (iOptionStichtag == FertigungReportFac.OFFENE_OPTION_STICHTAG_LIEFERTERMIN) {
					if (flrAuftragSchonVerwendet == false) {
						c.createAlias(FertigungFac.FLR_LOSREPORT_FLRAUFTRAG,
								"a", CriteriaSpecification.LEFT_JOIN);
						flrAuftragSchonVerwendet = true;
						c.add(Restrictions.lt("a."
								+ AuftragFac.FLR_AUFTRAG_T_LIEFERTERMIN,
								dStichtag));

					}
					datumsart = getTextRespectUISpr("bes.liefertermin",
							theClientDto.getMandant(), theClientDto.getLocUi());

				}

				mapParameter.put("P_DATUMSART", datumsart);

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
			if (belegNrVon != null) {
				String sVon = HelperServer
						.getBelegnummernFilterForHibernateCriterias(f,
								iGeschaeftsjahr, sMandantKuerzel, belegNrVon);
				c.add(Restrictions.ge(FertigungFac.FLR_LOS_C_NR, sVon));
				mapParameter.put("P_LOSNRVON", sVon);
			}
			if (belegNrBis != null) {
				String sBis = HelperServer
						.getBelegnummernFilterForHibernateCriterias(f,
								iGeschaeftsjahr, sMandantKuerzel, belegNrBis);
				c.add(Restrictions.le(FertigungFac.FLR_LOS_C_NR, sBis));
				mapParameter.put("P_LOSNRBIS", sBis);
			}

			// Sortierung
			if (iSortierung == FertigungReportFac.OFFENE_OPTION_SORTIERUNG_KOSTENSTELLE) {
				c.createCriteria(FertigungFac.FLR_LOSREPORT_FLRKOSTENSTELLE)
						.addOrder(Order.asc("c_nr"));
				mapParameter.put(
						P_SORTIERUNG,
						getTextRespectUISpr("lp.kostenstelle",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			} else if (iSortierung == FertigungReportFac.OFFENE_OPTION_SORTIERUNG_KUNDE) {
				if (flrAuftragSchonVerwendet == false) {
					c.createAlias(FertigungFac.FLR_LOSREPORT_FLRAUFTRAG, "a",
							CriteriaSpecification.LEFT_JOIN);
					flrAuftragSchonVerwendet = true;
				}
				c.createAlias("a." + AuftragFac.FLR_AUFTRAG_FLRKUNDE, "k",
						CriteriaSpecification.LEFT_JOIN);
				c.createAlias("k." + KundeFac.FLR_PARTNER, "p",
						CriteriaSpecification.LEFT_JOIN);

				c.addOrder(Order.asc("p."
						+ PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1));
				c.addOrder(Order.asc("k.i_id"));

				mapParameter.put(
						P_SORTIERUNG,
						getTextRespectUISpr("lp.kunde",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			} else if (iSortierung == FertigungReportFac.OFFENE_OPTION_SORTIERUNG_KUNDEUNDGEWAEHLTERTERMIN) {
				if (flrAuftragSchonVerwendet == false) {
					c.createAlias(FertigungFac.FLR_LOSREPORT_FLRAUFTRAG, "a",
							CriteriaSpecification.LEFT_JOIN);
					flrAuftragSchonVerwendet = true;
				}
				c.createAlias("a." + AuftragFac.FLR_AUFTRAG_FLRKUNDE, "k",
						CriteriaSpecification.LEFT_JOIN);
				c.createAlias("k." + KundeFac.FLR_PARTNER, "p",
						CriteriaSpecification.LEFT_JOIN);

				c.addOrder(Order.asc("p."
						+ PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1));

				if (iOptionStichtag == FertigungReportFac.OFFENE_OPTION_STICHTAG_BEGINNDATUM) {
					c.addOrder(Order
							.asc(FertigungFac.FLR_LOS_T_PRODUKTIONSBEGINN));
				} else if (iOptionStichtag == FertigungReportFac.OFFENE_OPTION_STICHTAG_ENDEDATUM) {
					c.addOrder(Order
							.asc(FertigungFac.FLR_LOS_T_PRODUKTIONSENDE));
				} else if (iOptionStichtag == FertigungReportFac.OFFENE_OPTION_STICHTAG_LIEFERTERMIN) {
					c.addOrder(Order.asc("a."
							+ AuftragFac.FLR_AUFTRAG_T_LIEFERTERMIN));
				}

				mapParameter.put(
						P_SORTIERUNG,
						getTextRespectUISpr("fert.kundeundtermin",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			} else if (iSortierung == FertigungReportFac.OFFENE_OPTION_SORTIERUNG_LIEFERTERMIN) {
				if (flrAuftragSchonVerwendet == false) {
					c.createAlias(FertigungFac.FLR_LOSREPORT_FLRAUFTRAG, "a",
							CriteriaSpecification.LEFT_JOIN);
					flrAuftragSchonVerwendet = true;
				}
				c.createAlias("a." + AuftragFac.FLR_AUFTRAG_FLRKUNDE, "k",
						CriteriaSpecification.LEFT_JOIN);
				c.createAlias("k." + KundeFac.FLR_PARTNER, "p",
						CriteriaSpecification.LEFT_JOIN);

				c.addOrder(Order.asc("a."
						+ AuftragFac.FLR_AUFTRAG_T_LIEFERTERMIN));
				c.addOrder(Order.asc("p."
						+ PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1));

				mapParameter.put(
						P_SORTIERUNG,
						getTextRespectUISpr("fert.liefertermin",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			} else if (iSortierung == FertigungReportFac.OFFENE_OPTION_SORTIERUNG_ARTIKEL) {
				c.createAlias(FertigungFac.FLR_LOS_FLRSTUECKLISTE, "s");
				c.createAlias("s." + StuecklisteFac.FLR_STUECKLISTE_FLRARTIKEL,
						"art");
				c.addOrder(Order.asc("art.c_nr"));
				mapParameter.put(
						P_SORTIERUNG,
						getTextRespectUISpr("lp.kunde",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			} else if (iSortierung == FertigungReportFac.OFFENE_OPTION_SORTIERUNG_FERTIGUNGSGRUPPE) {
				c.createAlias(FertigungFac.FLR_LOSREPORT_FLRFERTIGUNGSGRUPPE,
						"f");
				c.addOrder(Order.asc("f.c_bez"));
				mapParameter.put(
						P_SORTIERUNG,
						getTextRespectUISpr("lp.fertigungsgruppe",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			} else if (iSortierung == FertigungReportFac.OFFENE_OPTION_SORTIERUNG_BEGINN) {
				c.addOrder(Order.asc(FertigungFac.FLR_LOS_T_PRODUKTIONSBEGINN));
				mapParameter.put(
						P_SORTIERUNG,
						getTextRespectUISpr("lp.beginn",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			} else if (iSortierung == FertigungReportFac.OFFENE_OPTION_SORTIERUNG_ENDE) {
				c.addOrder(Order.asc(FertigungFac.FLR_LOS_T_PRODUKTIONSENDE));
				mapParameter.put(
						P_SORTIERUNG,
						getTextRespectUISpr("lp.ende",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			} else {
				mapParameter.put(
						P_SORTIERUNG,
						getTextRespectUISpr("lp.losnr",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			}

			// 2tes sortierkriterium immer Losnr

			if (iSortierung == FertigungReportFac.OFFENE_OPTION_SORTIERUNG_BEGINN
					|| iSortierung == FertigungReportFac.OFFENE_OPTION_SORTIERUNG_ENDE
					|| iSortierung == FertigungReportFac.OFFENE_OPTION_SORTIERUNG_LIEFERTERMIN
					|| iSortierung == FertigungReportFac.OFFENE_OPTION_SORTIERUNG_KUNDEUNDGEWAEHLTERTERMIN) {
				if (flrAuftragSchonVerwendet == false) {
					c.createAlias(FertigungFac.FLR_LOSREPORT_FLRAUFTRAG, "a",
							CriteriaSpecification.LEFT_JOIN);
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

					Integer partnerIId = los.getFlrauftrag().getFlrkunde()
							.getFlrpartner().getI_id();
					PartnerDto partnerDto = getPartnerFac()
							.partnerFindByPrimaryKey(partnerIId, theClientDto);
					data[i][OFFENE_KUNDE] = partnerDto
							.formatFixTitelName1Name2();
					data[i][OFFENE_AUFTRAGSPOENALE] = los.getFlrauftrag()
							.getB_poenale();
				}

				if (los.getFlrauftrag() != null) {
					data[i][OFFENE_AUFTRAGSNUMMER] = los.getFlrauftrag()
							.getC_nr();
					data[i][OFFENE_PROJEKT] = los.getFlrauftrag().getC_bez();
					data[i][OFFENE_LIEFERTERMIN] = los.getFlrauftrag()
							.getT_liefertermin();
				}
				BigDecimal bdGeliefert = new BigDecimal(0);
				for (Iterator<?> iter2 = los.getAblieferungset().iterator(); iter2
						.hasNext();) {
					FLRLosablieferung item = (FLRLosablieferung) iter2.next();
					bdGeliefert = bdGeliefert.add(item.getN_menge());
				}
				data[i][OFFENE_GELIEFERT] = bdGeliefert;
				if (los.getFlrstueckliste() != null) {
					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(
									los.getFlrstueckliste().getFlrartikel()
											.getI_id(), theClientDto);
					data[i][OFFENE_BEZEICHNUNG] = artikelDto.getArtikelsprDto()
							.getCBez();
					data[i][OFFENE_ZUSATZBEZEICHNUNG] = artikelDto
							.getArtikelsprDto().getCZbez();
					data[i][OFFENE_ARTIKELNUMMER] = los.getFlrstueckliste()
							.getFlrartikel().getC_nr();

					data[i][OFFENE_DETAILBEDARF] = getRahmenbedarfeFac()
							.getSummeAllerRahmenbedarfeEinesArtikels(
									artikelDto.getIId());

					// Offene Fehlmengen
					data[i][OFFENE_FEHLMENGE] = getFehlmengeFac()
							.getAnzahlFehlmengeEinesArtikels(
									artikelDto.getIId(), theClientDto);

					LossollarbeitsplanDto[] lossollarbeitsplanDto = getFertigungFac()
							.lossollarbeitsplanFindByLosIId(los.getI_id());

					BigDecimal bdFertigungszeit = new BigDecimal(0);
					for (int j = 0; lossollarbeitsplanDto.length > j; j++) {
						bdFertigungszeit = bdFertigungszeit
								.add(lossollarbeitsplanDto[j].getNGesamtzeit());
					}
					data[i][OFFENE_FERTIGUNGSZEIT] = bdFertigungszeit;

					// Rahmenbestellt
					Hashtable<?, ?> htRahmenbestellt = getArtikelbestelltFac()
							.getAnzahlRahmenbestellt(artikelDto.getIId(),
									theClientDto);
					if (htRahmenbestellt
							.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
						BigDecimal bdAnzahlRahmenbestellt = (BigDecimal) htRahmenbestellt
								.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
						data[i][OFFENE_RAHMEN_BESTELLT] = bdAnzahlRahmenbestellt;
					}
					data[i][OFFENE_RESERVIERUNGEN] = getReservierungFac()
							.getAnzahlReservierungen(artikelDto.getIId(),
									theClientDto);

					data[i][OFFENE_RAHMENRESERVIERUNGEN] = getReservierungFac()
							.getAnzahlRahmenreservierungen(artikelDto.getIId(),
									theClientDto);

				} else {
					data[i][OFFENE_BEZEICHNUNG] = los.getC_projekt();
					data[i][OFFENE_ZUSATZBEZEICHNUNG] = null;
					data[i][OFFENE_ARTIKELNUMMER] = getTextRespectUISpr(
							"fert.materialliste", theClientDto.getMandant(),
							theClientDto.getLocUi());
				}

				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(los.getT_produktionsbeginn());
				data[i][OFFENE_KALENDERWOCHE] = new Integer(
						gc.get(GregorianCalendar.WEEK_OF_YEAR));
				data[i][OFFENE_LOSGROESSE] = los.getN_losgroesse();
				data[i][OFFENE_LOSNUMMER] = los.getC_nr();
				data[i][OFFENE_FERTIGUNGSGRUPPE] = los.getFlrfertigungsgruppe()
						.getC_bez();
				/**
				 * @todo material PJ 4239
				 */
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
			}

			initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL,
					FertigungReportFac.REPORT_OFFENE,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
			return getReportPrint();
		} catch (Exception t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, t);
		} finally {
			closeSession(session);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printMaschineUndMaterial(Integer maschineIId,
			DatumsfilterVonBis vonBis, TheClientDto theClientDto) {
		this.useCase = UC_MASCHINEUNDMATERIAL;
		this.index = -1;

		Map<String, Object> mapParameter = new TreeMap<String, Object>();

		mapParameter.put("P_VON", vonBis.getTimestampVon());
		mapParameter.put("P_BIS", vonBis.getTimestampBisUnveraendert());

		ParametermandantDto parameter = null;
		try {
			parameter = (ParametermandantDto) getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_STUECKLISTE,
							ParameterFac.PARAMETER_STUECKLISTE_ARBEITSPLAN_ZEITEINHEIT);
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
				+ "LEFT OUTER JOIN flroffeneags.flrlossollarbeitsplan AS sollarbeitsplan "
				+ "" + "WHERE flroffeneags.mandant_c_nr = '"
				+ theClientDto.getMandant() + "'";
		if (vonBis.getTimestampVon() != null) {
			sQuery += " AND flroffeneags.t_agbeginn>='"
					+ Helper.formatTimestampWithSlashes(vonBis
							.getTimestampVon()) + "'";
		}

		if (vonBis.getTimestampBis() != null) {
			sQuery += " AND flroffeneags.t_agbeginn<'"
					+ Helper.formatTimestampWithSlashes(vonBis
							.getTimestampBis()) + "'";
		}

		if (maschineIId != null) {
			sQuery += " AND flrmaschine=" + maschineIId + " ";
			mapParameter.put("P_MASCHINE", getZeiterfassungFac()
					.maschineFindByPrimaryKey(maschineIId).getBezeichnung());

		}
		sQuery += " ORDER BY flrmaschine.c_identifikationsnr ASC, sollmaterial.c_nr ASC,"
				+ " flroffeneags.t_agbeginn ASC, flroffeneags.i_maschinenversatz_ms ASC";

		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Query hquery = session.createQuery(sQuery);
		List<?> resultList = hquery.list();
		Iterator<?> resultListIterator = resultList.iterator();
		int i = 0;
		ArrayList alDaten = new ArrayList();

		while (resultListIterator.hasNext()) {
			FLROffeneags flrOffeneags = (FLROffeneags) resultListIterator
					.next();

			Object[] oZeile = new Object[MASCHINEUNDMATERIAL_SPALTENANZAHL];

			// AG

			LossollarbeitsplanDto saDtoOriginal = getFertigungFac()
					.lossollarbeitsplanFindByPrimaryKey(flrOffeneags.getI_id());

			oZeile[MASCHINEUNDMATERIAL_AG_ART] = flrOffeneags
					.getFlrlossollarbeitsplan().getAgart_c_nr();
			oZeile[MASCHINEUNDMATERIAL_AG_BEGINN] = flrOffeneags
					.getT_agbeginn();
			oZeile[MASCHINEUNDMATERIAL_ARBEITSGANG] = flrOffeneags
					.getFlrlossollarbeitsplan().getI_arbeitsgangsnummer();

			if (flrOffeneags.getFlrlossollarbeitsplan().getFlrsollmaterial() != null) {
				oZeile[MASCHINEUNDMATERIAL_ARTIKELNUMMER_MATERIAL] = flrOffeneags
						.getFlrlossollarbeitsplan().getFlrsollmaterial()
						.getFlrartikel().getC_nr();

				ArtikelDto aDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(
								flrOffeneags.getFlrlossollarbeitsplan()
										.getFlrsollmaterial().getFlrartikel()
										.getI_id(), theClientDto);

				oZeile[MASCHINEUNDMATERIAL_ARTIKELBEZEICHNUNG_MATERIAL] = aDto
						.formatBezeichnung();

			}

			oZeile[MASCHINEUNDMATERIAL_ARTIKELNUMMER_TAETIGKEIT] = flrOffeneags
					.getFlrlossollarbeitsplan().getFlrartikel().getC_nr();

			ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
					flrOffeneags.getFlrlossollarbeitsplan().getFlrartikel()
							.getI_id(), theClientDto);

			oZeile[MASCHINEUNDMATERIAL_ARTIKELBEZEICHNUNG_TAETIGKEIT] = aDto
					.formatBezeichnung();

			oZeile[MASCHINEUNDMATERIAL_AUFSPANNUNG] = flrOffeneags
					.getFlrlossollarbeitsplan().getI_aufspannung();
			oZeile[MASCHINEUNDMATERIAL_AUTO_STOP_BEI_GEHT] = Helper
					.short2Boolean(saDtoOriginal.getBAutoendebeigeht());

			oZeile[MASCHINEUNDMATERIAL_KOMMENTAR] = saDtoOriginal
					.getCKomentar();
			oZeile[MASCHINEUNDMATERIAL_KOMMENTAR_LANG] = saDtoOriginal
					.getXText();
			oZeile[MASCHINEUNDMATERIAL_LOSNUMMER] = flrOffeneags.getFlrlos()
					.getC_nr();

			if (flrOffeneags.getFlrkunde() != null) {

				oZeile[MASCHINEUNDMATERIAL_KUNDE_KBEZ] = flrOffeneags
						.getFlrkunde().getFlrpartner().getC_kbez();
			}

			if (flrOffeneags.getFlrlossollarbeitsplan().getFlrmaschine() != null) {
				oZeile[MASCHINEUNDMATERIAL_MASCHINE_BEZEICHNUNG] = flrOffeneags
						.getFlrlossollarbeitsplan().getFlrmaschine().getC_bez();
				oZeile[MASCHINEUNDMATERIAL_MASCHINE_INVENTARNUMMMER] = flrOffeneags
						.getFlrlossollarbeitsplan().getFlrmaschine()
						.getC_inventarnummer();
				oZeile[MASCHINEUNDMATERIAL_MASCHINE_IDENTIFIKATIONSNUMMMER] = flrOffeneags
						.getFlrlossollarbeitsplan().getFlrmaschine()
						.getC_identifikationsnr();
			}

			oZeile[MASCHINEUNDMATERIAL_MASCHINENVERSATZ_MS] = saDtoOriginal
					.getIMaschinenversatzMs();
			oZeile[MASCHINEUNDMATERIAL_NUR_MASCHINENZEIT] = Helper
					.short2Boolean(saDtoOriginal.getBNurmaschinenzeit());

			if (flrOffeneags.getFlrlossollarbeitsplan()
					.getFlrpersonal_zugeordneter() != null) {

				oZeile[MASCHINEUNDMATERIAL_PERSON] = HelperServer
						.formatPersonAusFLRPartner(flrOffeneags
								.getFlrlossollarbeitsplan()
								.getFlrpersonal_zugeordneter().getFlrpartner());
			}

			oZeile[MASCHINEUNDMATERIAL_UNTERARBEITSGANG] = saDtoOriginal
					.getIUnterarbeitsgang();
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

			oZeile[MASCHINEUNDMATERIAL_RUESTZEIT] = Helper.rundeKaufmaennisch(
					new BigDecimal(dRuestzeitOriginal), 5);
			oZeile[MASCHINEUNDMATERIAL_STUECKZEIT] = Helper.rundeKaufmaennisch(
					new BigDecimal(dStueckzeitOriginal), 5);
			oZeile[MASCHINEUNDMATERIAL_GESAMTZEIT_IN_STUNDEN] = Helper
					.berechneGesamtzeitInStunden(saDtoOriginal.getLRuestzeit(),
							saDtoOriginal.getLStueckzeit(), flrOffeneags
									.getFlrlos().getN_losgroesse(), null,
							saDtoOriginal.getIAufspannung());

			// Naechster AG
			Integer sollarbeitsplanIId_NaechterAG = null;

			try {
				LossollarbeitsplanDto[] sollDtos = getFertigungFac()
						.lossollarbeitsplanFindByLosIId(
								flrOffeneags.getFlrlos().getI_id());

				for (int j = 0; j < sollDtos.length; j++) {

					if (saDtoOriginal.getIId().equals(sollDtos[j].getIId())) {

						// Der naechste ists dann
						if (j < sollDtos.length - 1) {
							sollarbeitsplanIId_NaechterAG = sollDtos[j + 1]
									.getIId();
						}

					}

				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			if (sollarbeitsplanIId_NaechterAG != null) {

				LossollarbeitsplanDto saDto_Next = getFertigungFac()
						.lossollarbeitsplanFindByPrimaryKey(
								sollarbeitsplanIId_NaechterAG);

				oZeile[MASCHINEUNDMATERIAL_NEXT_AG_AG_ART] = saDto_Next
						.getAgartCNr();

				if (saDto_Next.getIMaschinenversatztage() == null) {
					oZeile[MASCHINEUNDMATERIAL_NEXT_AG_AG_BEGINN] = flrOffeneags
							.getFlrlos().getT_produktionsbeginn();
				} else {
					oZeile[MASCHINEUNDMATERIAL_NEXT_AG_AG_BEGINN] = Helper
							.addiereTageZuDatum(flrOffeneags.getFlrlos()
									.getT_produktionsbeginn(), saDto_Next
									.getIMaschinenversatztage());
				}

				oZeile[MASCHINEUNDMATERIAL_NEXT_AG_ARBEITSGANG] = saDto_Next
						.getIArbeitsgangnummer();

				try {
					if (saDto_Next.getLossollmaterialIId() != null) {
						LossollmaterialDto sollmatDto = getFertigungFac()
								.lossollmaterialFindByPrimaryKey(
										saDto_Next.getLossollmaterialIId());

						ArtikelDto aDtoSollmat = getArtikelFac()
								.artikelFindByPrimaryKeySmall(
										sollmatDto.getArtikelIId(),
										theClientDto);
						oZeile[MASCHINEUNDMATERIAL_NEXT_AG_ARTIKELNUMMER_MATERIAL] = aDtoSollmat
								.getCNr();
						oZeile[MASCHINEUNDMATERIAL_NEXT_AG_ARTIKELBEZEICHNUNG_MATERIAL] = aDtoSollmat
								.formatBezeichnung();

					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				ArtikelDto aDtoSollarbeitsplan = getArtikelFac()
						.artikelFindByPrimaryKeySmall(
								saDto_Next.getArtikelIIdTaetigkeit(),
								theClientDto);
				oZeile[MASCHINEUNDMATERIAL_NEXT_AG_ARTIKELNUMMER_TAETIGKEIT] = aDtoSollarbeitsplan
						.getCNr();

				oZeile[MASCHINEUNDMATERIAL_NEXT_AG_ARTIKELBEZEICHNUNG_TAETIGKEIT] = aDtoSollarbeitsplan
						.formatBezeichnung();

				oZeile[MASCHINEUNDMATERIAL_NEXT_AG_AUFSPANNUNG] = saDto_Next
						.getIAufspannung();
				oZeile[MASCHINEUNDMATERIAL_NEXT_AG_AUTO_STOP_BEI_GEHT] = Helper
						.short2Boolean(saDto_Next.getBAutoendebeigeht());

				oZeile[MASCHINEUNDMATERIAL_NEXT_AG_KOMMENTAR] = saDto_Next
						.getCKomentar();
				oZeile[MASCHINEUNDMATERIAL_NEXT_AG_KOMMENTAR_LANG] = saDto_Next
						.getXText();

				if (saDto_Next.getMaschineIId() != null) {
					MaschineDto mDto = getZeiterfassungFac()
							.maschineFindByPrimaryKey(
									saDto_Next.getMaschineIId());
					oZeile[MASCHINEUNDMATERIAL_NEXT_AG_MASCHINE_BEZEICHNUNG] = mDto
							.getCBez();
					oZeile[MASCHINEUNDMATERIAL_NEXT_AG_MASCHINE_INVENTARNUMMMER] = mDto
							.getCInventarnummer();
					oZeile[MASCHINEUNDMATERIAL_NEXT_AG_MASCHINE_IDENTIFIKATIONSNUMMMER] = mDto
							.getCIdentifikationsnr();
				}

				oZeile[MASCHINEUNDMATERIAL_NEXT_AG_MASCHINENVERSATZ_MS] = saDto_Next
						.getIMaschinenversatzMs();
				oZeile[MASCHINEUNDMATERIAL_NEXT_AG_NUR_MASCHINENZEIT] = Helper
						.short2Boolean(saDto_Next.getBNurmaschinenzeit());

				if (saDto_Next.getPersonalIIdZugeordneter() != null) {

					PersonalDto personalDto = getPersonalFac()
							.personalFindByPrimaryKey(
									saDto_Next.getPersonalIIdZugeordneter(),
									theClientDto);
					oZeile[MASCHINEUNDMATERIAL_NEXT_AG_PERSON] = personalDto
							.formatFixUFTitelName2Name1();
				}

				oZeile[MASCHINEUNDMATERIAL_NEXT_AG_UNTERARBEITSGANG] = saDto_Next
						.getIUnterarbeitsgang();

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

				oZeile[MASCHINEUNDMATERIAL_NEXT_AG_RUESTZEIT] = Helper
						.rundeKaufmaennisch(new BigDecimal(dRuestzeit), 5);
				oZeile[MASCHINEUNDMATERIAL_NEXT_AG_STUECKZEIT] = Helper
						.rundeKaufmaennisch(new BigDecimal(dStueckzeit), 5);
				oZeile[MASCHINEUNDMATERIAL_NEXT_AG_GESAMTZEIT_IN_STUNDEN] = Helper
						.berechneGesamtzeitInStunden(
								saDto_Next.getLRuestzeit(), saDto_Next
										.getLStueckzeit(), flrOffeneags
										.getFlrlos().getN_losgroesse(), null,
								saDto_Next.getIAufspannung());
			}
			alDaten.add(oZeile);

		}
		session.close();

		data = new Object[alDaten.size()][MASCHINEUNDMATERIAL_SPALTENANZAHL];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL,
				FertigungReportFac.REPORT_MASCHINEUNDMATERIAL,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printOffeneArbeitsgaenge(java.sql.Date dStichtag,
			int iOptionStichtag, String belegNrVon, String belegNrBis,
			Integer kundeIId, Integer kostenstelleIId,
			Integer fertigungsgruppeIId, Integer artikelgruppeIId,
			Integer maschineIId, boolean bSollstundenbetrachtung,
			TheClientDto theClientDto) {
		Session session = null;

		try {
			this.useCase = UC_OFFENE_AG;
			this.index = -1;
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRLossollarbeitsplan.class);

			c.createAlias(FertigungFac.FLR_LOSSOLLARBEITSPLAN_FLRLOS, "l",
					CriteriaSpecification.LEFT_JOIN);

			c.add(Restrictions.eq("l." + FertigungFac.FLR_LOS_MANDANT_C_NR,
					theClientDto.getMandant()));

			c.add(Restrictions.eq(FertigungFac.FLR_LOSSOLLARBEITSPLAN_B_FERTIG,
					Helper.boolean2Short(false)));

			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			mapParameter.put("P_SOLLSTUNDENBETRACHTUNG", new Boolean(
					bSollstundenbetrachtung));
			if (artikelgruppeIId != null) {
				c.createAlias(FertigungFac.FLR_LOSSOLLARBEITSPLAN_FLRARTIKEL,
						"artikel", CriteriaSpecification.LEFT_JOIN);
				c.createAlias("artikel."
						+ ArtikelFac.FLR_ARTIKEL_FLRARTIKELGRUPPE,
						"artikelgruppe", CriteriaSpecification.LEFT_JOIN);
				c.add(Restrictions.eq("artikelgruppe.i_id", artikelgruppeIId));

				ArtgruDto artgruDto = getArtikelFac().artgruFindByPrimaryKey(
						artikelgruppeIId, theClientDto);

				mapParameter.put("P_ARTIKELGRUPPE", artgruDto.getBezeichnung());
			}

			/*
			 * if (kostenstelleIId != null) { KostenstelleDto kostenstelleDto =
			 * getSystemFac() .kostenstelleFindByPrimaryKey(kostenstelleIId);
			 * mapParameter.put("P_KOSTENSTELLE",
			 * kostenstelleDto.formatKostenstellenbezeichnung());
			 * 
			 * c.add(Restrictions.eq("l." +
			 * FertigungFac.FLR_LOSREPORT_KOSTENSTELLE_I_ID, kostenstelleIId));
			 * }
			 */
			boolean flrAuftragSchonVerwendet = false;
			if (kundeIId != null) {
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						kundeIId, theClientDto);
				mapParameter.put("P_KUNDE", kundeDto.getPartnerDto()
						.formatFixTitelName1Name2());
				c.createAlias("l." + FertigungFac.FLR_LOSREPORT_FLRAUFTRAG,
						"a", CriteriaSpecification.LEFT_JOIN);
				flrAuftragSchonVerwendet = true;
				c.add(Restrictions.eq("a."
						+ AuftragFac.FLR_AUFTRAG_KUNDE_I_ID_AUFTRAGSADRESSE,
						kundeIId));
			}

			String[] stati = new String[4];
			stati[0] = LocaleFac.STATUS_AUSGEGEBEN;
			stati[1] = LocaleFac.STATUS_IN_PRODUKTION;
			stati[2] = LocaleFac.STATUS_TEILERLEDIGT;
			stati[3] = LocaleFac.STATUS_ANGELEGT;

			c.add(Restrictions.in("l." + FertigungFac.FLR_LOS_STATUS_C_NR,
					stati));

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
					c.add(Restrictions.lt("l."
							+ FertigungFac.FLR_LOS_T_PRODUKTIONSBEGINN,
							dStichtag));

					datumsart = getTextRespectUISpr("lp.begintermin",
							theClientDto.getMandant(), theClientDto.getLocUi());

				} else if (iOptionStichtag == FertigungReportFac.OFFENE_OPTION_STICHTAG_ENDEDATUM) {
					c.add(Restrictions
							.lt("l." + FertigungFac.FLR_LOS_T_PRODUKTIONSENDE,
									dStichtag));
					datumsart = getTextRespectUISpr("lp.endetermin",
							theClientDto.getMandant(), theClientDto.getLocUi());

				} else if (iOptionStichtag == FertigungReportFac.OFFENE_OPTION_STICHTAG_LIEFERTERMIN) {
					if (flrAuftragSchonVerwendet == false) {
						c.createAlias("l."
								+ FertigungFac.FLR_LOSREPORT_FLRAUFTRAG, "a",
								CriteriaSpecification.LEFT_JOIN);
						flrAuftragSchonVerwendet = true;
						c.add(Restrictions.lt("a."
								+ AuftragFac.FLR_AUFTRAG_T_LIEFERTERMIN,
								dStichtag));

					}
					datumsart = getTextRespectUISpr("bes.liefertermin",
							theClientDto.getMandant(), theClientDto.getLocUi());

				}

				mapParameter.put("P_DATUMSART", datumsart);

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
			if (belegNrVon != null) {
				String sVon = HelperServer
						.getBelegnummernFilterForHibernateCriterias(f,
								iGeschaeftsjahr, sMandantKuerzel, belegNrVon);
				c.add(Restrictions.ge("l." + FertigungFac.FLR_LOS_C_NR, sVon));
				mapParameter.put("P_LOSNRVON", sVon);
			}
			if (belegNrBis != null) {
				String sBis = HelperServer
						.getBelegnummernFilterForHibernateCriterias(f,
								iGeschaeftsjahr, sMandantKuerzel, belegNrBis);
				c.add(Restrictions.le("l." + FertigungFac.FLR_LOS_C_NR, sBis));
				mapParameter.put("P_LOSNRBIS", sBis);
			}

			if (maschineIId != null) {
				MaschineDto mDto = getZeiterfassungFac()
						.maschineFindByPrimaryKey(maschineIId);
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
					if (los.getFertigungsgruppe_i_id().intValue() != fertigungsgruppeIId
							.intValue()
							&& los.getKostenstelle_i_id().intValue() != kostenstelleIId
									.intValue()) {
						i--;
					} else {
						// skip
					}
				} else if (fertigungsgruppeIId != null
						&& kostenstelleIId == null) {
					if (los.getFertigungsgruppe_i_id().intValue() != fertigungsgruppeIId
							.intValue()) {
						i--;
					} else {
						// skip
					}
				} else if (fertigungsgruppeIId == null
						&& kostenstelleIId != null) {
					if (los.getKostenstelle_i_id().intValue() != kostenstelleIId
							.intValue()) {
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
					if (los.getFertigungsgruppe_i_id().intValue() != fertigungsgruppeIId
							.intValue()
							&& los.getKostenstelle_i_id().intValue() != kostenstelleIId
									.intValue()) {
						i--;
					} else {
						data[i][OFFENE_AG_AGNUMMER] = a
								.getI_arbeitsgangsnummer();
						data[i][OFFENE_AG_UAGNUMMER] = a
								.getI_unterarbeitsgang();
						data[i][OFFENE_AG_AG_ARTIKEL] = a.getFlrartikel()
								.getC_nr();
						ArtikelDto aDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(
										a.getFlrartikel().getI_id(),
										theClientDto);
						data[i][OFFENE_AG_AG_ARTIKELBEZEICHNUNG] = aDto
								.formatBezeichnung();

						if (Helper.short2boolean(a.getB_fertig())) {
							data[i][OFFENE_AG_AG_GESAMTZEIT] = new BigDecimal(0);
						} else {

							data[i][OFFENE_AG_AG_GESAMTZEIT] = a
									.getN_gesamtzeit();
						}

						if (bSollstundenbetrachtung == true) {
							data[i][OFFENE_AG_AG_ISTZEIT] = getZeiterfassungFac()
									.getSummeZeitenEinesBeleges(
											LocaleFac.BELEGART_LOS,
											los.getI_id(), a.getI_id(), null,
											null, null, theClientDto);

						}

						if (a.getFlrmaschine() != null) {
							data[i][OFFENE_AG_AG_MASCHINE_BEZEICHNUNG] = a
									.getFlrmaschine().getC_bez();
							data[i][OFFENE_AG_AG_MASCHINE_IDENTIFIKATIONSNUMMMER] = a
									.getFlrmaschine().getC_identifikationsnr();
							data[i][OFFENE_AG_AG_MASCHINE_INVENTARNUMMMER] = a
									.getFlrmaschine().getC_inventarnummer();
						}

						if (a.getFlrartikel().getFlrartikelgruppe() != null) {
							data[i][OFFENE_AG_AG_ARTIKELGRUPPPE] = a
									.getFlrartikel().getFlrartikelgruppe()
									.getC_nr();
						} else {
							data[i][OFFENE_AG_AG_ARTIKELGRUPPPE] = "";
						}

						if (a.getI_maschinenversatztage() != null) {
							data[i][OFFENE_AG_AG_BEGINN] = Helper
									.addiereTageZuTimestamp((Timestamp) los
											.getT_produktionsbeginn(), a
											.getI_maschinenversatztage());

						} else {
							data[i][OFFENE_AG_AG_BEGINN] = los
									.getT_produktionsbeginn();

						}

						if (los.getFlrauftrag() != null) {

							Integer partnerIId = los.getFlrauftrag()
									.getFlrkunde().getFlrpartner().getI_id();
							PartnerDto partnerDto = getPartnerFac()
									.partnerFindByPrimaryKey(partnerIId,
											theClientDto);
							data[i][OFFENE_AG_KUNDE] = partnerDto
									.formatFixTitelName1Name2();
							data[i][OFFENE_AG_AUFTRAGSPOENALE] = los
									.getFlrauftrag().getB_poenale();
						}

						if (los.getFlrauftrag() != null) {
							data[i][OFFENE_AG_AUFTRAGSNUMMER] = los
									.getFlrauftrag().getC_nr();
							data[i][OFFENE_AG_PROJEKT] = los.getFlrauftrag()
									.getC_bez();
							data[i][OFFENE_AG_LIEFERTERMIN] = los
									.getFlrauftrag().getT_liefertermin();
						}
						BigDecimal bdGeliefert = new BigDecimal(0);
						for (Iterator<?> iter2 = los.getAblieferungset()
								.iterator(); iter2.hasNext();) {
							FLRLosablieferung item = (FLRLosablieferung) iter2
									.next();
							bdGeliefert = bdGeliefert.add(item.getN_menge());
						}
						data[i][OFFENE_AG_GELIEFERT] = bdGeliefert;
						if (los.getFlrstueckliste() != null) {
							ArtikelDto artikelDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(
											los.getFlrstueckliste()
													.getFlrartikel().getI_id(),
											theClientDto);
							data[i][OFFENE_AG_BEZEICHNUNG] = artikelDto
									.getArtikelsprDto().getCBez();
							data[i][OFFENE_AG_ZUSATZBEZEICHNUNG] = artikelDto
									.getArtikelsprDto().getCZbez();
							data[i][OFFENE_AG_ARTIKELNUMMER] = los
									.getFlrstueckliste().getFlrartikel()
									.getC_nr();

							data[i][OFFENE_AG_DETAILBEDARF] = getRahmenbedarfeFac()
									.getSummeAllerRahmenbedarfeEinesArtikels(
											artikelDto.getIId());

							// Offene Fehlmengen
							data[i][OFFENE_AG_FEHLMENGE] = getFehlmengeFac()
									.getAnzahlFehlmengeEinesArtikels(
											artikelDto.getIId(), theClientDto);

							LossollarbeitsplanDto[] lossollarbeitsplanDto = getFertigungFac()
									.lossollarbeitsplanFindByLosIId(
											los.getI_id());

							BigDecimal bdFertigungszeit = new BigDecimal(0);
							for (int j = 0; lossollarbeitsplanDto.length > j; j++) {
								bdFertigungszeit = bdFertigungszeit
										.add(lossollarbeitsplanDto[j]
												.getNGesamtzeit());
							}
							data[i][OFFENE_AG_FERTIGUNGSZEIT] = bdFertigungszeit;

							// Rahmenbestellt
							Hashtable<?, ?> htRahmenbestellt = getArtikelbestelltFac()
									.getAnzahlRahmenbestellt(
											artikelDto.getIId(), theClientDto);
							if (htRahmenbestellt
									.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
								BigDecimal bdAnzahlRahmenbestellt = (BigDecimal) htRahmenbestellt
										.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
								data[i][OFFENE_AG_RAHMEN_BESTELLT] = bdAnzahlRahmenbestellt;
							}
							data[i][OFFENE_AG_RESERVIERUNGEN] = getReservierungFac()
									.getAnzahlReservierungen(
											artikelDto.getIId(), theClientDto);

							data[i][OFFENE_AG_RAHMENRESERVIERUNGEN] = getReservierungFac()
									.getAnzahlRahmenreservierungen(
											artikelDto.getIId(), theClientDto);

						} else {
							data[i][OFFENE_AG_BEZEICHNUNG] = los.getC_projekt();
							data[i][OFFENE_AG_ZUSATZBEZEICHNUNG] = null;
							data[i][OFFENE_AG_ARTIKELNUMMER] = getTextRespectUISpr(
									"fert.materialliste",
									theClientDto.getMandant(),
									theClientDto.getLocUi());
						}

						GregorianCalendar gc = new GregorianCalendar();
						gc.setTime(los.getT_produktionsbeginn());
						data[i][OFFENE_AG_KALENDERWOCHE] = new Integer(
								gc.get(GregorianCalendar.WEEK_OF_YEAR));
						data[i][OFFENE_AG_LOSGROESSE] = los.getN_losgroesse();
						data[i][OFFENE_AG_LOSNUMMER] = los.getC_nr();
						data[i][OFFENE_AG_FERTIGUNGSGRUPPE] = los
								.getFlrfertigungsgruppe().getC_bez();
						/**
						 * @todo material PJ 4239
						 */
						data[i][OFFENE_AG_MATERIAL] = null;
						data[i][OFFENE_AG_BEGINN] = los
								.getT_produktionsbeginn();
						data[i][OFFENE_AG_ENDE] = los.getT_produktionsende();
						data[i][OFFENE_AG_LOSSTATUS] = los.getStatus_c_nr();

						// PJ 15009

						String queryf = "select sum(flrfehlmenge.n_menge) FROM FLRFehlmenge flrfehlmenge WHERE flrfehlmenge.flrlossollmaterial.flrlos = "
								+ los.getI_id() + " AND flrfehlmenge.n_menge>0";
						Session session2 = FLRSessionFactory.getFactory()
								.openSession();
						Query query = session2.createQuery(queryf);
						List<?> results = query.list();

						data[i][OFFENE_AG_LOSHATFEHLMENGE] = new Boolean(false);
						if (results.size() > 0) {
							BigDecimal bd = (BigDecimal) results.iterator()
									.next();

							if (bd != null && bd.doubleValue() > 0) {
								data[i][OFFENE_AG_LOSHATFEHLMENGE] = new Boolean(
										true);
							}
						}
						session2.close();
					}
				} else if (fertigungsgruppeIId != null
						|| kostenstelleIId != null) {
					if (fertigungsgruppeIId != null) {
						if (los.getFertigungsgruppe_i_id().intValue() != fertigungsgruppeIId
								.intValue()) {
							i--;
						} else {
							data[i][OFFENE_AG_AGNUMMER] = a
									.getI_arbeitsgangsnummer();
							data[i][OFFENE_AG_UAGNUMMER] = a
									.getI_unterarbeitsgang();
							data[i][OFFENE_AG_AG_ARTIKEL] = a.getFlrartikel()
									.getC_nr();
							ArtikelDto aDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(
											a.getFlrartikel().getI_id(),
											theClientDto);
							data[i][OFFENE_AG_AG_ARTIKELBEZEICHNUNG] = aDto
									.formatBezeichnung();

							if (Helper.short2boolean(a.getB_fertig())) {
								data[i][OFFENE_AG_AG_GESAMTZEIT] = new BigDecimal(
										0);
							} else {
								data[i][OFFENE_AG_AG_GESAMTZEIT] = a
										.getN_gesamtzeit();
							}

							if (bSollstundenbetrachtung == true) {
								data[i][OFFENE_AG_AG_ISTZEIT] = getZeiterfassungFac()
										.getSummeZeitenEinesBeleges(
												LocaleFac.BELEGART_LOS,
												los.getI_id(), a.getI_id(),
												null, null, null, theClientDto);

							}

							if (a.getFlrmaschine() != null) {
								data[i][OFFENE_AG_AG_MASCHINE_BEZEICHNUNG] = a
										.getFlrmaschine().getC_bez();
								data[i][OFFENE_AG_AG_MASCHINE_IDENTIFIKATIONSNUMMMER] = a
										.getFlrmaschine()
										.getC_identifikationsnr();
								data[i][OFFENE_AG_AG_MASCHINE_INVENTARNUMMMER] = a
										.getFlrmaschine().getC_inventarnummer();
							}

							if (a.getFlrartikel().getFlrartikelgruppe() != null) {
								data[i][OFFENE_AG_AG_ARTIKELGRUPPPE] = a
										.getFlrartikel().getFlrartikelgruppe()
										.getC_nr();
							} else {
								data[i][OFFENE_AG_AG_ARTIKELGRUPPPE] = "";
							}

							if (a.getI_maschinenversatztage() != null) {
								data[i][OFFENE_AG_AG_BEGINN] = Helper
										.addiereTageZuTimestamp((Timestamp) los
												.getT_produktionsbeginn(), a
												.getI_maschinenversatztage());

							} else {
								data[i][OFFENE_AG_AG_BEGINN] = los
										.getT_produktionsbeginn();

							}

							if (los.getFlrauftrag() != null) {

								Integer partnerIId = los.getFlrauftrag()
										.getFlrkunde().getFlrpartner()
										.getI_id();
								PartnerDto partnerDto = getPartnerFac()
										.partnerFindByPrimaryKey(partnerIId,
												theClientDto);
								data[i][OFFENE_AG_KUNDE] = partnerDto
										.formatFixTitelName1Name2();
								data[i][OFFENE_AG_AUFTRAGSPOENALE] = los
										.getFlrauftrag().getB_poenale();
							}

							if (los.getFlrauftrag() != null) {
								data[i][OFFENE_AG_AUFTRAGSNUMMER] = los
										.getFlrauftrag().getC_nr();
								data[i][OFFENE_AG_PROJEKT] = los
										.getFlrauftrag().getC_bez();
								data[i][OFFENE_AG_LIEFERTERMIN] = los
										.getFlrauftrag().getT_liefertermin();
							}
							BigDecimal bdGeliefert = new BigDecimal(0);
							for (Iterator<?> iter2 = los.getAblieferungset()
									.iterator(); iter2.hasNext();) {
								FLRLosablieferung item = (FLRLosablieferung) iter2
										.next();
								bdGeliefert = bdGeliefert
										.add(item.getN_menge());
							}
							data[i][OFFENE_AG_GELIEFERT] = bdGeliefert;
							if (los.getFlrstueckliste() != null) {
								ArtikelDto artikelDto = getArtikelFac()
										.artikelFindByPrimaryKeySmall(
												los.getFlrstueckliste()
														.getFlrartikel()
														.getI_id(),
												theClientDto);
								data[i][OFFENE_AG_BEZEICHNUNG] = artikelDto
										.getArtikelsprDto().getCBez();
								data[i][OFFENE_AG_ZUSATZBEZEICHNUNG] = artikelDto
										.getArtikelsprDto().getCZbez();
								data[i][OFFENE_AG_ARTIKELNUMMER] = los
										.getFlrstueckliste().getFlrartikel()
										.getC_nr();

								data[i][OFFENE_AG_DETAILBEDARF] = getRahmenbedarfeFac()
										.getSummeAllerRahmenbedarfeEinesArtikels(
												artikelDto.getIId());

								// Offene Fehlmengen
								data[i][OFFENE_AG_FEHLMENGE] = getFehlmengeFac()
										.getAnzahlFehlmengeEinesArtikels(
												artikelDto.getIId(),
												theClientDto);

								LossollarbeitsplanDto[] lossollarbeitsplanDto = getFertigungFac()
										.lossollarbeitsplanFindByLosIId(
												los.getI_id());

								BigDecimal bdFertigungszeit = new BigDecimal(0);
								for (int j = 0; lossollarbeitsplanDto.length > j; j++) {
									bdFertigungszeit = bdFertigungszeit
											.add(lossollarbeitsplanDto[j]
													.getNGesamtzeit());
								}
								data[i][OFFENE_AG_FERTIGUNGSZEIT] = bdFertigungszeit;

								// Rahmenbestellt
								Hashtable<?, ?> htRahmenbestellt = getArtikelbestelltFac()
										.getAnzahlRahmenbestellt(
												artikelDto.getIId(),
												theClientDto);
								if (htRahmenbestellt
										.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
									BigDecimal bdAnzahlRahmenbestellt = (BigDecimal) htRahmenbestellt
											.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
									data[i][OFFENE_AG_RAHMEN_BESTELLT] = bdAnzahlRahmenbestellt;
								}
								data[i][OFFENE_AG_RESERVIERUNGEN] = getReservierungFac()
										.getAnzahlReservierungen(
												artikelDto.getIId(),
												theClientDto);

								data[i][OFFENE_AG_RAHMENRESERVIERUNGEN] = getReservierungFac()
										.getAnzahlRahmenreservierungen(
												artikelDto.getIId(),
												theClientDto);

							} else {
								data[i][OFFENE_AG_BEZEICHNUNG] = los
										.getC_projekt();
								data[i][OFFENE_AG_ZUSATZBEZEICHNUNG] = null;
								data[i][OFFENE_AG_ARTIKELNUMMER] = getTextRespectUISpr(
										"fert.materialliste",
										theClientDto.getMandant(),
										theClientDto.getLocUi());
							}

							GregorianCalendar gc = new GregorianCalendar();
							gc.setTime(los.getT_produktionsbeginn());
							data[i][OFFENE_AG_KALENDERWOCHE] = new Integer(
									gc.get(GregorianCalendar.WEEK_OF_YEAR));
							data[i][OFFENE_AG_LOSGROESSE] = los
									.getN_losgroesse();
							data[i][OFFENE_AG_LOSNUMMER] = los.getC_nr();
							data[i][OFFENE_AG_FERTIGUNGSGRUPPE] = los
									.getFlrfertigungsgruppe().getC_bez();
							/**
							 * @todo material PJ 4239
							 */
							data[i][OFFENE_AG_MATERIAL] = null;
							data[i][OFFENE_AG_BEGINN] = los
									.getT_produktionsbeginn();
							data[i][OFFENE_AG_ENDE] = los
									.getT_produktionsende();
							data[i][OFFENE_AG_LOSSTATUS] = los.getStatus_c_nr();

							// PJ 15009

							String queryf = "select sum(flrfehlmenge.n_menge) FROM FLRFehlmenge flrfehlmenge WHERE flrfehlmenge.flrlossollmaterial.flrlos = "
									+ los.getI_id()
									+ " AND flrfehlmenge.n_menge>0";
							Session session2 = FLRSessionFactory.getFactory()
									.openSession();
							Query query = session2.createQuery(queryf);
							List<?> results = query.list();

							data[i][OFFENE_AG_LOSHATFEHLMENGE] = new Boolean(
									false);
							if (results.size() > 0) {
								BigDecimal bd = (BigDecimal) results.iterator()
										.next();

								if (bd != null && bd.doubleValue() > 0) {
									data[i][OFFENE_AG_LOSHATFEHLMENGE] = new Boolean(
											true);
								}
							}
							session2.close();
						}
					} else if (kostenstelleIId != null) {
						if (los.getKostenstelle_i_id().intValue() != kostenstelleIId
								.intValue()) {
							i--;
						} else {
							data[i][OFFENE_AG_AGNUMMER] = a
									.getI_arbeitsgangsnummer();
							data[i][OFFENE_AG_UAGNUMMER] = a
									.getI_unterarbeitsgang();
							data[i][OFFENE_AG_AG_ARTIKEL] = a.getFlrartikel()
									.getC_nr();
							ArtikelDto aDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(
											a.getFlrartikel().getI_id(),
											theClientDto);
							data[i][OFFENE_AG_AG_ARTIKELBEZEICHNUNG] = aDto
									.formatBezeichnung();

							if (Helper.short2boolean(a.getB_fertig())) {
								data[i][OFFENE_AG_AG_GESAMTZEIT] = new BigDecimal(
										0);
							} else {
								data[i][OFFENE_AG_AG_GESAMTZEIT] = a
										.getN_gesamtzeit();
							}
							if (bSollstundenbetrachtung == true) {
								data[i][OFFENE_AG_AG_ISTZEIT] = getZeiterfassungFac()
										.getSummeZeitenEinesBeleges(
												LocaleFac.BELEGART_LOS,
												los.getI_id(), a.getI_id(),
												null, null, null, theClientDto);

							}
							if (a.getFlrmaschine() != null) {
								data[i][OFFENE_AG_AG_MASCHINE_BEZEICHNUNG] = a
										.getFlrmaschine().getC_bez();
								data[i][OFFENE_AG_AG_MASCHINE_IDENTIFIKATIONSNUMMMER] = a
										.getFlrmaschine()
										.getC_identifikationsnr();
								data[i][OFFENE_AG_AG_MASCHINE_INVENTARNUMMMER] = a
										.getFlrmaschine().getC_inventarnummer();
							}
							if (a.getFlrartikel().getFlrartikelgruppe() != null) {
								data[i][OFFENE_AG_AG_ARTIKELGRUPPPE] = a
										.getFlrartikel().getFlrartikelgruppe()
										.getC_nr();
							} else {
								data[i][OFFENE_AG_AG_ARTIKELGRUPPPE] = "";
							}

							if (a.getI_maschinenversatztage() != null) {
								data[i][OFFENE_AG_AG_BEGINN] = Helper
										.addiereTageZuTimestamp((Timestamp) los
												.getT_produktionsbeginn(), a
												.getI_maschinenversatztage());

							} else {
								data[i][OFFENE_AG_AG_BEGINN] = los
										.getT_produktionsbeginn();

							}

							if (los.getFlrauftrag() != null) {

								Integer partnerIId = los.getFlrauftrag()
										.getFlrkunde().getFlrpartner()
										.getI_id();
								PartnerDto partnerDto = getPartnerFac()
										.partnerFindByPrimaryKey(partnerIId,
												theClientDto);
								data[i][OFFENE_AG_KUNDE] = partnerDto
										.formatFixTitelName1Name2();
								data[i][OFFENE_AG_AUFTRAGSPOENALE] = los
										.getFlrauftrag().getB_poenale();
							}

							if (los.getFlrauftrag() != null) {
								data[i][OFFENE_AG_AUFTRAGSNUMMER] = los
										.getFlrauftrag().getC_nr();
								data[i][OFFENE_AG_PROJEKT] = los
										.getFlrauftrag().getC_bez();
								data[i][OFFENE_AG_LIEFERTERMIN] = los
										.getFlrauftrag().getT_liefertermin();
							}
							BigDecimal bdGeliefert = new BigDecimal(0);
							for (Iterator<?> iter2 = los.getAblieferungset()
									.iterator(); iter2.hasNext();) {
								FLRLosablieferung item = (FLRLosablieferung) iter2
										.next();
								bdGeliefert = bdGeliefert
										.add(item.getN_menge());
							}
							data[i][OFFENE_AG_GELIEFERT] = bdGeliefert;
							if (los.getFlrstueckliste() != null) {
								ArtikelDto artikelDto = getArtikelFac()
										.artikelFindByPrimaryKeySmall(
												los.getFlrstueckliste()
														.getFlrartikel()
														.getI_id(),
												theClientDto);
								data[i][OFFENE_AG_BEZEICHNUNG] = artikelDto
										.getArtikelsprDto().getCBez();
								data[i][OFFENE_AG_ZUSATZBEZEICHNUNG] = artikelDto
										.getArtikelsprDto().getCZbez();
								data[i][OFFENE_AG_ARTIKELNUMMER] = los
										.getFlrstueckliste().getFlrartikel()
										.getC_nr();

								data[i][OFFENE_AG_DETAILBEDARF] = getRahmenbedarfeFac()
										.getSummeAllerRahmenbedarfeEinesArtikels(
												artikelDto.getIId());

								// Offene Fehlmengen
								data[i][OFFENE_AG_FEHLMENGE] = getFehlmengeFac()
										.getAnzahlFehlmengeEinesArtikels(
												artikelDto.getIId(),
												theClientDto);

								LossollarbeitsplanDto[] lossollarbeitsplanDto = getFertigungFac()
										.lossollarbeitsplanFindByLosIId(
												los.getI_id());

								BigDecimal bdFertigungszeit = new BigDecimal(0);
								for (int j = 0; lossollarbeitsplanDto.length > j; j++) {
									bdFertigungszeit = bdFertigungszeit
											.add(lossollarbeitsplanDto[j]
													.getNGesamtzeit());
								}
								data[i][OFFENE_AG_FERTIGUNGSZEIT] = bdFertigungszeit;

								// Rahmenbestellt
								Hashtable<?, ?> htRahmenbestellt = getArtikelbestelltFac()
										.getAnzahlRahmenbestellt(
												artikelDto.getIId(),
												theClientDto);
								if (htRahmenbestellt
										.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
									BigDecimal bdAnzahlRahmenbestellt = (BigDecimal) htRahmenbestellt
											.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
									data[i][OFFENE_AG_RAHMEN_BESTELLT] = bdAnzahlRahmenbestellt;
								}
								data[i][OFFENE_AG_RESERVIERUNGEN] = getReservierungFac()
										.getAnzahlReservierungen(
												artikelDto.getIId(),
												theClientDto);

								data[i][OFFENE_AG_RAHMENRESERVIERUNGEN] = getReservierungFac()
										.getAnzahlRahmenreservierungen(
												artikelDto.getIId(),
												theClientDto);

							} else {
								data[i][OFFENE_AG_BEZEICHNUNG] = los
										.getC_projekt();
								data[i][OFFENE_AG_ZUSATZBEZEICHNUNG] = null;
								data[i][OFFENE_AG_ARTIKELNUMMER] = getTextRespectUISpr(
										"fert.materialliste",
										theClientDto.getMandant(),
										theClientDto.getLocUi());
							}

							GregorianCalendar gc = new GregorianCalendar();
							gc.setTime(los.getT_produktionsbeginn());
							data[i][OFFENE_AG_KALENDERWOCHE] = new Integer(
									gc.get(GregorianCalendar.WEEK_OF_YEAR));
							data[i][OFFENE_AG_LOSGROESSE] = los
									.getN_losgroesse();
							data[i][OFFENE_AG_LOSNUMMER] = los.getC_nr();
							data[i][OFFENE_AG_FERTIGUNGSGRUPPE] = los
									.getFlrfertigungsgruppe().getC_bez();
							/**
							 * @todo material PJ 4239
							 */
							data[i][OFFENE_AG_MATERIAL] = null;
							data[i][OFFENE_AG_BEGINN] = los
									.getT_produktionsbeginn();
							data[i][OFFENE_AG_ENDE] = los
									.getT_produktionsende();
							data[i][OFFENE_AG_LOSSTATUS] = los.getStatus_c_nr();

							// PJ 15009

							String queryf = "select sum(flrfehlmenge.n_menge) FROM FLRFehlmenge flrfehlmenge WHERE flrfehlmenge.flrlossollmaterial.flrlos = "
									+ los.getI_id()
									+ " AND flrfehlmenge.n_menge>0";
							Session session2 = FLRSessionFactory.getFactory()
									.openSession();
							Query query = session2.createQuery(queryf);
							List<?> results = query.list();

							data[i][OFFENE_AG_LOSHATFEHLMENGE] = new Boolean(
									false);
							if (results.size() > 0) {
								BigDecimal bd = (BigDecimal) results.iterator()
										.next();

								if (bd != null && bd.doubleValue() > 0) {
									data[i][OFFENE_AG_LOSHATFEHLMENGE] = new Boolean(
											true);
								}
							}
							session2.close();
						}
					}
				} else {

					data[i][OFFENE_AG_AGNUMMER] = a.getI_arbeitsgangsnummer();
					data[i][OFFENE_AG_UAGNUMMER] = a.getI_unterarbeitsgang();
					data[i][OFFENE_AG_AG_ARTIKEL] = a.getFlrartikel().getC_nr();
					ArtikelDto aDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(
									a.getFlrartikel().getI_id(), theClientDto);
					data[i][OFFENE_AG_AG_ARTIKELBEZEICHNUNG] = aDto
							.formatBezeichnung();

					if (Helper.short2boolean(a.getB_fertig())) {
						data[i][OFFENE_AG_AG_GESAMTZEIT] = new BigDecimal(0);
					} else {
						data[i][OFFENE_AG_AG_GESAMTZEIT] = a.getN_gesamtzeit();
					}
					if (bSollstundenbetrachtung == true) {
						data[i][OFFENE_AG_AG_ISTZEIT] = getZeiterfassungFac()
								.getSummeZeitenEinesBeleges(
										LocaleFac.BELEGART_LOS, los.getI_id(),
										a.getI_id(), null, null, null,
										theClientDto);

					}
					if (a.getFlrmaschine() != null) {
						data[i][OFFENE_AG_AG_MASCHINE_BEZEICHNUNG] = a
								.getFlrmaschine().getC_bez();
						data[i][OFFENE_AG_AG_MASCHINE_IDENTIFIKATIONSNUMMMER] = a
								.getFlrmaschine().getC_identifikationsnr();
						data[i][OFFENE_AG_AG_MASCHINE_INVENTARNUMMMER] = a
								.getFlrmaschine().getC_inventarnummer();
					}
					if (a.getFlrartikel().getFlrartikelgruppe() != null) {
						data[i][OFFENE_AG_AG_ARTIKELGRUPPPE] = a
								.getFlrartikel().getFlrartikelgruppe()
								.getC_nr();
					} else {
						data[i][OFFENE_AG_AG_ARTIKELGRUPPPE] = "";
					}

					if (a.getI_maschinenversatztage() != null) {
						data[i][OFFENE_AG_AG_BEGINN] = Helper
								.addiereTageZuTimestamp((Timestamp) los
										.getT_produktionsbeginn(), a
										.getI_maschinenversatztage());

					} else {
						data[i][OFFENE_AG_AG_BEGINN] = los
								.getT_produktionsbeginn();

					}

					if (los.getFlrauftrag() != null) {

						Integer partnerIId = los.getFlrauftrag().getFlrkunde()
								.getFlrpartner().getI_id();
						PartnerDto partnerDto = getPartnerFac()
								.partnerFindByPrimaryKey(partnerIId,
										theClientDto);
						data[i][OFFENE_AG_KUNDE] = partnerDto
								.formatFixTitelName1Name2();
						data[i][OFFENE_AG_AUFTRAGSPOENALE] = los
								.getFlrauftrag().getB_poenale();
					}

					if (los.getFlrauftrag() != null) {
						data[i][OFFENE_AG_AUFTRAGSNUMMER] = los.getFlrauftrag()
								.getC_nr();
						data[i][OFFENE_AG_PROJEKT] = los.getFlrauftrag()
								.getC_bez();
						data[i][OFFENE_AG_LIEFERTERMIN] = los.getFlrauftrag()
								.getT_liefertermin();
					}
					BigDecimal bdGeliefert = new BigDecimal(0);
					for (Iterator<?> iter2 = los.getAblieferungset().iterator(); iter2
							.hasNext();) {
						FLRLosablieferung item = (FLRLosablieferung) iter2
								.next();
						bdGeliefert = bdGeliefert.add(item.getN_menge());
					}
					data[i][OFFENE_AG_GELIEFERT] = bdGeliefert;
					if (los.getFlrstueckliste() != null) {
						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(
										los.getFlrstueckliste().getFlrartikel()
												.getI_id(), theClientDto);
						data[i][OFFENE_AG_BEZEICHNUNG] = artikelDto
								.getArtikelsprDto().getCBez();
						data[i][OFFENE_AG_ZUSATZBEZEICHNUNG] = artikelDto
								.getArtikelsprDto().getCZbez();
						data[i][OFFENE_AG_ARTIKELNUMMER] = los
								.getFlrstueckliste().getFlrartikel().getC_nr();

						data[i][OFFENE_AG_DETAILBEDARF] = getRahmenbedarfeFac()
								.getSummeAllerRahmenbedarfeEinesArtikels(
										artikelDto.getIId());

						// Offene Fehlmengen
						data[i][OFFENE_AG_FEHLMENGE] = getFehlmengeFac()
								.getAnzahlFehlmengeEinesArtikels(
										artikelDto.getIId(), theClientDto);

						LossollarbeitsplanDto[] lossollarbeitsplanDto = getFertigungFac()
								.lossollarbeitsplanFindByLosIId(los.getI_id());

						BigDecimal bdFertigungszeit = new BigDecimal(0);
						for (int j = 0; lossollarbeitsplanDto.length > j; j++) {
							bdFertigungszeit = bdFertigungszeit
									.add(lossollarbeitsplanDto[j]
											.getNGesamtzeit());
						}
						data[i][OFFENE_AG_FERTIGUNGSZEIT] = bdFertigungszeit;

						// Rahmenbestellt
						Hashtable<?, ?> htRahmenbestellt = getArtikelbestelltFac()
								.getAnzahlRahmenbestellt(artikelDto.getIId(),
										theClientDto);
						if (htRahmenbestellt
								.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
							BigDecimal bdAnzahlRahmenbestellt = (BigDecimal) htRahmenbestellt
									.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
							data[i][OFFENE_AG_RAHMEN_BESTELLT] = bdAnzahlRahmenbestellt;
						}
						data[i][OFFENE_AG_RESERVIERUNGEN] = getReservierungFac()
								.getAnzahlReservierungen(artikelDto.getIId(),
										theClientDto);

						data[i][OFFENE_AG_RAHMENRESERVIERUNGEN] = getReservierungFac()
								.getAnzahlRahmenreservierungen(
										artikelDto.getIId(), theClientDto);

					} else {
						data[i][OFFENE_AG_BEZEICHNUNG] = los.getC_projekt();
						data[i][OFFENE_AG_ZUSATZBEZEICHNUNG] = null;
						data[i][OFFENE_AG_ARTIKELNUMMER] = getTextRespectUISpr(
								"fert.materialliste",
								theClientDto.getMandant(),
								theClientDto.getLocUi());
					}

					GregorianCalendar gc = new GregorianCalendar();
					gc.setTime(los.getT_produktionsbeginn());
					data[i][OFFENE_AG_KALENDERWOCHE] = new Integer(
							gc.get(GregorianCalendar.WEEK_OF_YEAR));
					data[i][OFFENE_AG_LOSGROESSE] = los.getN_losgroesse();
					data[i][OFFENE_AG_LOSNUMMER] = los.getC_nr();
					data[i][OFFENE_AG_FERTIGUNGSGRUPPE] = los
							.getFlrfertigungsgruppe().getC_bez();
					/**
					 * @todo material PJ 4239
					 */
					data[i][OFFENE_AG_MATERIAL] = null;
					data[i][OFFENE_AG_BEGINN] = los.getT_produktionsbeginn();
					data[i][OFFENE_AG_ENDE] = los.getT_produktionsende();
					data[i][OFFENE_AG_LOSSTATUS] = los.getStatus_c_nr();

					// PJ 15009

					String queryf = "select sum(flrfehlmenge.n_menge) FROM FLRFehlmenge flrfehlmenge WHERE flrfehlmenge.flrlossollmaterial.flrlos = "
							+ los.getI_id() + " AND flrfehlmenge.n_menge>0";
					Session session2 = FLRSessionFactory.getFactory()
							.openSession();
					Query query = session2.createQuery(queryf);
					List<?> results = query.list();

					data[i][OFFENE_AG_LOSHATFEHLMENGE] = new Boolean(false);
					if (results.size() > 0) {
						BigDecimal bd = (BigDecimal) results.iterator().next();

						if (bd != null && bd.doubleValue() > 0) {
							data[i][OFFENE_AG_LOSHATFEHLMENGE] = new Boolean(
									true);
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

			initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL,
					FertigungReportFac.REPORT_OFFENE_AG,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
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
		session.enableFilter("filterMandant").setParameter("paramMandant",
				theClientDto.getMandant());
		session.enableFilter("filterLocale").setParameter("paramLocale",
				Helper.locale2String(theClientDto.getLocUi()));

		ZusatzstatusDto zusatzstatusDto = null;
		try {
			ParametermandantDto parametermandantDto = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FERTIGUNG,
							ParameterFac.PARAMETER_RANKINGLISTE_ZUSATZSTATUS);
			if (((java.lang.Integer) parametermandantDto.getCWertAsObject()) != null) {
				zusatzstatusDto = getFertigungFac()
						.zusatzstatusFindByPrimaryKey(
								(java.lang.Integer) parametermandantDto
										.getCWertAsObject());

			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

		String query = "SELECT a.flrartikel.i_id,a.flrartikel.c_nr, sum(a.n_menge), (SELECT s.i_id FROM FLRStueckliste s WHERE s.artikel_i_id=a.flrartikel.i_id AND a.flrartikel.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "') AS stkl_i_id ,  (SELECT COUNT(*) FROM FLRStuecklisteposition sp WHERE sp.flrartikel.i_id=a.flrartikel.i_id ),  (SELECT SUM(fm.n_menge) FROM FLRFehlmenge fm WHERE fm.flrartikel.i_id=a.flrartikel.i_id ),  (SELECT SUM(al.n_lagerstand) FROM FLRArtikellager al WHERE al.compId.artikel_i_id=a.flrartikel.i_id ), (SELECT spr.c_bez FROM FLRArtikellistespr spr WHERE spr.Id.artikelliste=a.flrartikel.i_id AND spr.Id.locale='"
				+ theClientDto.getLocUiAsString()
				+ "')  FROM FLRArtikelreservierung a WHERE a.flrartikel.mandant_c_nr='"
				+ theClientDto.getMandant()
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
						+ zusatzstatusDto.getIId()
						+ ") , fg.c_bez, l.flrstueckliste.flrartikel.c_nr "
						+ " FROM FLRLosReport l LEFT JOIN l.flrauftrag.flrkunde.flrpartner AS partner LEFT JOIN l.flrfertigungsgruppe AS fg  WHERE l.flrstueckliste.i_id="
						+ o[3]
						+ " AND l.status_c_nr IN ('"
						+ FertigungFac.STATUS_AUSGEGEBEN
						+ "','"
						+ FertigungFac.STATUS_IN_PRODUKTION
						+ "') ORDER BY l.t_produktionsbeginn ASC ";
				Query subResult = session2.createQuery(subQuery);

				List<?> subResults = subResult.list();
				Iterator<?> subResultListIterator = subResults.iterator();
				while (subResultListIterator.hasNext()) {
					Object[] flrLosReport = (Object[]) subResultListIterator
							.next();

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

						oZeile[RANKINGLISTE_SOLLZEIT] = gesamt.divide(
								losgroesse, 4, BigDecimal.ROUND_HALF_EVEN)
								.multiply(offeneMenge);
						// Unterstueckliste?
						if (o[4] != null && ((Long) o[4]).intValue() > 0) {
							oZeile[RANKINGLISTE_INFO_U] = new Boolean(true);
						} else {
							oZeile[RANKINGLISTE_INFO_U] = new Boolean(false);
						}

						if (bdReserviert.add(bdFehlmengen).doubleValue() > bdLagerstand
								.doubleValue()) {
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

				String s1 = Helper.fitString2Length(
						(String) a1[RANKINGLISTE_FERTIGUNGSGRUPPE], 40, ' ')
						+ a1[RANKINGLISTE_LOSBEGINNTERMIN];
				String s2 = Helper.fitString2Length(
						(String) a2[RANKINGLISTE_FERTIGUNGSGRUPPE], 40, ' ')
						+ a2[RANKINGLISTE_LOSBEGINNTERMIN];

				if (s1.compareTo(s2) > 0) {
					alDaten.set(j, a2);
					alDaten.set(j + 1, a1);
				}
			}

		}

		data = new Object[alDaten.size()][12];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL,
				FertigungReportFac.REPORT_RANKINGLISTE,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printHalbfertigfabrikatsinventur(
			java.sql.Timestamp tsStichtag, int iSortierung,
			boolean bVerdichtet, Integer partnerIIdFertigungsort,
			boolean bSortiertNachFertigungsgruppe, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Session session = null;
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

			this.useCase = UC_HALBFERTIGFABRIKATSINVENTUR;
			this.index = -1;
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRLosReport.class);
			c.add(Restrictions.eq(FertigungFac.FLR_LOS_MANDANT_C_NR,
					theClientDto.getMandant()));
			// c.add(Restrictions.eq("i_id", 99579));

			if (partnerIIdFertigungsort != null) {
				c.add(Restrictions.eq(
						FertigungFac.FLR_LOSREPORT_PARTNER_I_ID_FERTIGUNGSORT,
						partnerIIdFertigungsort));
			}

			if (tsStichtag == null) {
				c.add(Restrictions.not(Restrictions.in(
						FertigungFac.FLR_LOS_STATUS_C_NR, new String[] {
								FertigungFac.STATUS_STORNIERT,
								FertigungFac.STATUS_ERLEDIGT,
								FertigungFac.STATUS_ANGELEGT,
								FertigungFac.STATUS_GESTOPPT })));
			} else {
				c.add(Restrictions.not(Restrictions.in(
						FertigungFac.FLR_LOS_STATUS_C_NR, new String[] {
								FertigungFac.STATUS_STORNIERT,
								FertigungFac.STATUS_GESTOPPT })));

				c.add(Restrictions.or(Restrictions.gt(
						FertigungFac.FLR_LOS_T_ERLEDIGT, tsStichtag),
						Restrictions.isNull(FertigungFac.FLR_LOS_T_ERLEDIGT)));

				c.add(Restrictions.or(Restrictions.gt(
						FertigungFac.FLR_LOSREPORT_T_MANUELLERLEDIGT,
						tsStichtag), Restrictions
						.isNull(FertigungFac.FLR_LOSREPORT_T_MANUELLERLEDIGT)));

				c.add(Restrictions.le(FertigungFac.FLR_LOS_T_AUSGABE,
						tsStichtag));
			}
			// Sortierung nach Losnummer
			c.addOrder(Order.asc(FertigungFac.FLR_LOS_C_NR));
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
					zeileVorlage[HF_AUFTRAGSNUMMER] = item.getFlrauftrag()
							.getC_nr();
				} else {
					if (item.getFlrauftragposition() != null) {
						zeileVorlage[HF_AUFTRAGSNUMMER] = item
								.getFlrauftragposition().getFlrauftrag()
								.getC_nr();
					}
				}

				if (item.getFlrfertigungsgruppe() != null) {
					zeileVorlage[HF_FERTIGUNGSGRUPPE] = item
							.getFlrfertigungsgruppe().getC_bez();
				} else {
					zeileVorlage[HF_FERTIGUNGSGRUPPE] = "";
				}

				if (item.getFlrstueckliste() != null) {
					zeileVorlage[HF_ARTIKELNUMMER] = item.getFlrstueckliste()
							.getFlrartikel().getC_nr();
					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(
									item.getFlrstueckliste().getFlrartikel()
											.getI_id(), theClientDto);
					zeileVorlage[HF_BEZEICHNUNG] = artikelDto
							.getArtikelsprDto().getCBez();
					;
				} else {
					zeileVorlage[HF_ARTIKELNUMMER] = getTextRespectUISpr(
							"fert.materialliste", theClientDto.getMandant(),
							theClientDto.getLocUi());
					zeileVorlage[HF_BEZEICHNUNG] = item.getC_projekt();
				}

				LosablieferungDto[] losablieferungDtos = getFertigungFac()
						.losablieferungFindByLosIId(item.getI_id(), true,
								theClientDto);
				BigDecimal bdAbgeliefert = new BigDecimal(0.0000);
				for (int j = 0; j < losablieferungDtos.length; j++) {

					if (tsStichtag == null) {
						bdAbgeliefert = bdAbgeliefert.add(losablieferungDtos[j]
								.getNMenge());
					} else {
						if (tsStichtag.after(losablieferungDtos[j]
								.getTAendern())) {
							bdAbgeliefert = bdAbgeliefert
									.add(losablieferungDtos[j].getNMenge());
						}
					}

				}
				zeileVorlage[HF_ERLEDIGT] = bdAbgeliefert;

				// Nun eine Zeile pro Position

				// Ausgegebenes Material
				LossollmaterialDto[] sollmat = getFertigungFac()
						.lossollmaterialFindByLosIId(item.getI_id());

				for (int j = 0; j < sollmat.length; j++) {
					BigDecimal bdMenge = getFertigungFac().getAusgegebeneMenge(
							sollmat[j].getIId(), tsStichtag, theClientDto);

					Object[] zeile = zeileVorlage.clone();

					zeile[HF_POSITION_AUSGEGEBEN] = bdMenge;

					// Einkaufspreis des ersten Lieferanten hinzufuegen
					ArtikellieferantDto dto = getArtikelFac()
							.getArtikelEinkaufspreis(
									sollmat[j].getArtikelIId(),
									null,
									new BigDecimal(1),
									theClientDto.getSMandantenwaehrung(),
									new java.sql.Date(sollmat[j].getTAendern()
											.getTime()), theClientDto);
					if (dto != null) {
						zeile[HF_POSITION_EKPREIS] = dto.getLief1Preis();
					}

					// CK:2008-12-23 Wegen Beistellteilen ist die Verfaelschung
					// des Gestehungspreises falsch
					// es muss immer der Gestehungspreis zum ausgabezeitpunkt
					// verwendet werden.
					BigDecimal bdPreis = getFertigungFac()
							.getAusgegebeneMengePreis(sollmat[j].getIId(),
									tsStichtag, theClientDto);
					zeile[HF_POSITION_PREIS] = bdPreis;

					BigDecimal sollsatzmenge = new BigDecimal(0);

					if (item.getN_losgroesse().doubleValue() != 0) {
						sollsatzmenge = sollmat[j].getNMenge().divide(
								item.getN_losgroesse(),
								BigDecimal.ROUND_HALF_EVEN);
					}

					BigDecimal theoretischabgeliefert = sollsatzmenge
							.multiply(bdAbgeliefert);

					if (theoretischabgeliefert.doubleValue() > bdMenge
							.doubleValue()) {
						theoretischabgeliefert = bdMenge;
					}

					zeile[HF_POSITION_ABGELIFERT] = theoretischabgeliefert;
					zeile[HF_POSITION_OFFEN] = bdMenge
							.subtract(theoretischabgeliefert);

					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(
									sollmat[j].getArtikelIId(), theClientDto);
					zeile[HF_POSITION_ARTIKELNUMMMER] = artikelDto.getCNr();
					zeile[HF_POSITION_BEZEICHNUNG] = artikelDto
							.formatBezeichnung();

					// Verdichtet gibts bei Sortierung nach Auftrag nicht
					if (bVerdichtet
							&& iSortierung != FertigungReportFac.HF_OPTION_SORTIERUNG_AUFTRAGNR) {
						if (iSortierung == FertigungReportFac.HF_OPTION_SORTIERUNG_ARTIKELNR) {
							boolean bGefunden = false;
							for (int k = 0; k < alDaten.size(); k++) {
								Object[] zeileTemp = (Object[]) alDaten.get(k);

								String suchstring1 = (String) zeileTemp[HF_POSITION_ARTIKELNUMMMER];
								String suchstring2 = (String) zeile[HF_POSITION_ARTIKELNUMMMER];

								if (bSortiertNachFertigungsgruppe == true) {
									suchstring1 = Helper
											.fitString2Length(
													(String) zeileTemp[HF_FERTIGUNGSGRUPPE],
													40, ' ')
											+ suchstring1;

									suchstring2 = Helper
											.fitString2Length(
													(String) zeile[HF_FERTIGUNGSGRUPPE],
													40, ' ')
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

									BigDecimal wertNeu = alterPreis.multiply(
											alteOffen).add(
											neuerPreis.multiply(neueOffen));

									if (neueOffen.add(alteOffen).doubleValue() != 0) {
										zeileTemp[HF_POSITION_PREIS] = wertNeu
												.divide(neueOffen
														.add(alteOffen),
														4,
														BigDecimal.ROUND_HALF_EVEN);
									} else {
										zeileTemp[HF_POSITION_PREIS] = new BigDecimal(
												0);
									}

									zeileTemp[HF_POSITION_OFFEN] = neueOffen
											.add(alteOffen);

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
									suchstring1 = Helper
											.fitString2Length(
													(String) zeileTemp[HF_FERTIGUNGSGRUPPE],
													40, ' ')
											+ suchstring1;

									suchstring2 = Helper
											.fitString2Length(
													(String) zeile[HF_FERTIGUNGSGRUPPE],
													40, ' ')
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

									BigDecimal wertNeu = alterPreis.multiply(
											alteOffen).add(
											neuerPreis.multiply(neueOffen));

									if (neueOffen.add(alteOffen).doubleValue() != 0) {
										zeileTemp[HF_POSITION_PREIS] = wertNeu
												.divide(neueOffen
														.add(alteOffen),
														4,
														BigDecimal.ROUND_HALF_EVEN);
									} else {
										zeileTemp[HF_POSITION_PREIS] = new BigDecimal(
												0);
									}

									zeileTemp[HF_POSITION_OFFEN] = neueOffen
											.add(alteOffen);

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

				// Verbrauchte Arbeitszeit
				LossollarbeitsplanDto[] lossollarbeitsplanDto = getFertigungFac()
						.lossollarbeitsplanFindByLosIId(item.getI_id());
				for (int j = 0; j < lossollarbeitsplanDto.length; j++) {
					AuftragzeitenDto[] zeiten = getZeiterfassungFac()
							.getAllZeitenEinesBeleges(LocaleFac.BELEGART_LOS,
									item.getI_id(), null, null, null,
									tsStichtag, false, false, theClientDto);

					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(
									lossollarbeitsplanDto[j]
											.getArtikelIIdTaetigkeit(),
									theClientDto);

					Object[] zeile = zeileVorlage.clone();

					zeile[HF_POSITION_ARTIKELNUMMMER] = artikelDto.getCNr();
					zeile[HF_POSITION_BEZEICHNUNG] = artikelDto
							.formatBezeichnung();

					BigDecimal bdArbeitszeitwert = new BigDecimal(0);
					BigDecimal bdIstZeit = new BigDecimal(0);

					for (int k = 0; k < zeiten.length; k++) {
						if (artikelDto.getIId().equals(
								zeiten[k].getArtikelIId())) {

							bdArbeitszeitwert = bdArbeitszeitwert.add(zeiten[k]
									.getBdKosten());
							bdIstZeit = bdIstZeit.add(new BigDecimal(zeiten[k]
									.getDdDauer().doubleValue()));
						}
					}
					BigDecimal sollsatzmenge = new BigDecimal(0);

					if (item.getN_losgroesse().doubleValue() != 0) {

						sollsatzmenge = lossollarbeitsplanDto[j]
								.getNGesamtzeit().divide(
										item.getN_losgroesse(),
										BigDecimal.ROUND_HALF_EVEN);
					}

					if (Helper.short2boolean(lossollarbeitsplanDto[j]
							.getBNurmaschinenzeit())) {
						zeile[HF_POSITION_AUSGEGEBEN] = new BigDecimal(0);
					} else {
						zeile[HF_POSITION_AUSGEGEBEN] = lossollarbeitsplanDto[j]
								.getNGesamtzeit();
					}

					BigDecimal theoretischabgeliefert = sollsatzmenge
							.multiply(bdAbgeliefert);

					BigDecimal restist = bdIstZeit
							.subtract(theoretischabgeliefert);

					if (restist.doubleValue() <= 0) {
						restist = new BigDecimal(0);
					}

					if (theoretischabgeliefert.doubleValue() > bdIstZeit
							.doubleValue()) {
						theoretischabgeliefert = bdIstZeit;
					}
					zeile[HF_POSITION_ABGELIFERT] = theoretischabgeliefert;
					zeile[HF_POSITION_OFFEN] = restist;

					if (bdIstZeit.doubleValue() != 0) {
						zeile[HF_POSITION_PREIS] = bdArbeitszeitwert.divide(
								bdIstZeit, BigDecimal.ROUND_HALF_EVEN);
					} else {
						zeile[HF_POSITION_PREIS] = new BigDecimal(0);
					}

					AuftragzeitenDto[] maschinenzeiten = getZeiterfassungFac()
							.getAllMaschinenzeitenEinesBeleges(item.getI_id(),
									lossollarbeitsplanDto[j].getIId(), null,
									tsStichtag, theClientDto);

					BigDecimal bdArbeitszeitwertMaschine = new BigDecimal(0);
					BigDecimal bdIstZeitMaschine = new BigDecimal(0);

					for (int k = 0; k < maschinenzeiten.length; k++) {

						bdArbeitszeitwertMaschine = bdArbeitszeitwertMaschine
								.add(maschinenzeiten[k].getBdKosten());
						bdIstZeitMaschine = bdIstZeitMaschine
								.add(new BigDecimal(maschinenzeiten[k]
										.getDdDauer().doubleValue()));

					}

					if (lossollarbeitsplanDto[j].getMaschineIId() == null) {
						zeile[HF_POSITION_AUSGEGEBEN_MASCHINE] = new BigDecimal(
								0);
					} else {
						zeile[HF_POSITION_AUSGEGEBEN_MASCHINE] = lossollarbeitsplanDto[j]
								.getNGesamtzeit();
					}

					theoretischabgeliefert = sollsatzmenge
							.multiply(bdAbgeliefert);
					BigDecimal restistMaschine = bdIstZeitMaschine
							.subtract(theoretischabgeliefert);

					if (restistMaschine.doubleValue() <= 0) {
						restistMaschine = new BigDecimal(0);
					}

					if (theoretischabgeliefert.doubleValue() > bdIstZeitMaschine
							.doubleValue()) {
						theoretischabgeliefert = bdIstZeitMaschine;
					}
					zeile[HF_POSITION_ABGELIFERT_MASCHINE] = theoretischabgeliefert;
					zeile[HF_POSITION_OFFEN_MASCHINE] = restistMaschine;

					if (bdIstZeitMaschine.doubleValue() != 0) {
						zeile[HF_POSITION_PREIS_MASCHINE] = bdArbeitszeitwertMaschine
								.divide(bdIstZeitMaschine,
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

								if (bSortiertNachFertigungsgruppe == true) {
									suchstring1 = Helper
											.fitString2Length(
													(String) zeileTemp[HF_FERTIGUNGSGRUPPE],
													40, ' ')
											+ suchstring1;

									suchstring2 = Helper
											.fitString2Length(
													(String) zeile[HF_FERTIGUNGSGRUPPE],
													40, ' ')
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

									BigDecimal wertNeu = alterPreis.multiply(
											alteOffen).add(
											neuerPreis.multiply(neueOffen));

									if (neueOffen.add(alteOffen).doubleValue() != 0) {
										zeileTemp[HF_POSITION_PREIS] = wertNeu
												.divide(neueOffen
														.add(alteOffen),
														4,
														BigDecimal.ROUND_HALF_EVEN);
									} else {
										zeileTemp[HF_POSITION_PREIS] = new BigDecimal(
												0);
									}

									zeileTemp[HF_POSITION_OFFEN] = neueOffen
											.add(alteOffen);

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
									suchstring1 = Helper
											.fitString2Length(
													(String) zeileTemp[HF_FERTIGUNGSGRUPPE],
													40, ' ')
											+ suchstring1;

									suchstring2 = Helper
											.fitString2Length(
													(String) zeile[HF_FERTIGUNGSGRUPPE],
													40, ' ')
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

									BigDecimal wertNeu = alterPreis.multiply(
											alteOffen).add(
											neuerPreis.multiply(neueOffen));

									if (neueOffen.add(alteOffen).doubleValue() != 0) {
										zeileTemp[HF_POSITION_PREIS] = wertNeu
												.divide(neueOffen
														.add(alteOffen),
														4,
														BigDecimal.ROUND_HALF_EVEN);
									} else {
										zeileTemp[HF_POSITION_PREIS] = new BigDecimal(
												0);
									}

									zeileTemp[HF_POSITION_OFFEN] = neueOffen
											.add(alteOffen);

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
			mapParameter.put(LPReport.P_WAEHRUNG,
					theClientDto.getSMandantenwaehrung());
			mapParameter.put("P_VERDICHTET", new Boolean(bVerdichtet));
			mapParameter.put("P_SORTIERT_NACH_FERTIGUNGSGRUPPE", new Boolean(
					bSortiertNachFertigungsgruppe));

			if (iSortierung == FertigungReportFac.HF_OPTION_SORTIERUNG_LOSNR) {
				mapParameter.put("P_SORTIERUNG", "Losnummer");
			} else if (iSortierung == FertigungReportFac.HF_OPTION_SORTIERUNG_ARTIKELNR) {
				mapParameter.put("P_SORTIERUNG", "Artikelnummer");
			} else if (iSortierung == FertigungReportFac.HF_OPTION_SORTIERUNG_AUFTRAGNR) {
				mapParameter.put("P_SORTIERUNG", "Auftragsnummer");
			}

			for (int k = alDaten.size() - 1; k > 0; --k) {
				for (int j = 0; j < k; ++j) {
					Object[] a1 = (Object[]) alDaten.get(j);
					Object[] a2 = (Object[]) alDaten.get(j + 1);
					String fertigungsgruppe1 = Helper.fitString2Length(
							(String) a1[HF_FERTIGUNGSGRUPPE], 40, ' ');
					String fertigungsgruppe2 = Helper.fitString2Length(
							(String) a2[HF_FERTIGUNGSGRUPPE], 40, ' ');

					String losnummer1 = (String) a1[HF_LOSNUMMER];
					String losnummer2 = (String) a2[HF_LOSNUMMER];

					if (losnummer1 == null) {
						losnummer1 = "";
					}
					if (losnummer2 == null) {
						losnummer2 = "";
					}

					losnummer1 = Helper.fitString2Length(losnummer1, 30, ' ');
					losnummer2 = Helper.fitString2Length(losnummer2, 30, ' ');

					String auftragsnummer1 = (String) a1[HF_AUFTRAGSNUMMER];
					String auftragsnummer2 = (String) a2[HF_AUFTRAGSNUMMER];

					if (auftragsnummer1 == null) {
						auftragsnummer1 = "";
					}
					if (auftragsnummer2 == null) {
						auftragsnummer2 = "";
					}

					auftragsnummer1 = Helper.fitString2Length(auftragsnummer1,
							30, ' ');
					auftragsnummer2 = Helper.fitString2Length(auftragsnummer2,
							30, ' ');

					String artikelnummer1 = (String) a1[HF_POSITION_ARTIKELNUMMMER];
					String artikelnummer2 = (String) a2[HF_POSITION_ARTIKELNUMMMER];

					if (artikelnummer1 == null) {
						artikelnummer1 = "";
					}
					if (artikelnummer2 == null) {
						artikelnummer2 = "";
					}

					artikelnummer1 = Helper.fitString2Length(artikelnummer1,
							40, ' ');
					artikelnummer2 = Helper.fitString2Length(artikelnummer2,
							40, ' ');

					String suchstring1 = null;
					String suchstring2 = null;

					if (iSortierung == FertigungReportFac.HF_OPTION_SORTIERUNG_LOSNR) {

						suchstring1 = losnummer1 + artikelnummer1;
						suchstring2 = losnummer2 + artikelnummer2;

					} else if (iSortierung == FertigungReportFac.HF_OPTION_SORTIERUNG_ARTIKELNR) {

						suchstring1 = artikelnummer1 + losnummer1;
						suchstring2 = artikelnummer2 + losnummer2;

					} else if (iSortierung == FertigungReportFac.HF_OPTION_SORTIERUNG_AUFTRAGNR) {

						suchstring1 = auftragsnummer1 + losnummer1
								+ artikelnummer1;
						suchstring2 = auftragsnummer2 + losnummer2
								+ artikelnummer2;

					}

					if (bSortiertNachFertigungsgruppe == true) {
						suchstring1 = fertigungsgruppe1 + suchstring1;
						suchstring2 = fertigungsgruppe2 + suchstring2;
					}

					if (suchstring1.compareTo(suchstring2) > 0) {
						alDaten.set(j, a2);
						alDaten.set(j + 1, a1);
					}

				}
			}
			data = new Object[alDaten.size()][HF_ANZAHL_SPALTEN];
			data = (Object[][]) alDaten.toArray(data);

			initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL,
					FertigungReportFac.REPORT_HALBFERTIGFABRIKATSINVENTUR,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);

			return getReportPrint();
		} catch (RemoteException t) {
			throwEJBExceptionLPRespectOld(t);
		} finally {
			closeSession(session);
		}
		return getReportPrint();
	}

	public ReportLosnachkalkulationDto[] getDataNachkalkulationZeitdaten(
			Integer losIId, TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			String sMandantWaehrung = theClientDto.getSMandantenwaehrung();

			ParametermandantDto parametermandantDto = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FERTIGUNG,
							ParameterFac.PARAMETER_ISTZEITEN_GLEICH_SOLLZEITEN);
			boolean bSollGleichIstzeiten = false;

			if (((java.lang.Boolean) parametermandantDto.getCWertAsObject())
					.booleanValue() == true) {
				bSollGleichIstzeiten = true;
			}

			LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);
			BigDecimal erlMenge = getFertigungFac().getErledigteMenge(losIId,
					theClientDto);
			BigDecimal faktorFuerIstGleichSoll = new BigDecimal(0);

			if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
				faktorFuerIstGleichSoll = new BigDecimal(1);
			} else {

				if (losDto.getNLosgroesse().doubleValue() > 0) {
					faktorFuerIstGleichSoll = erlMenge.divide(
							losDto.getNLosgroesse(), 10,
							BigDecimal.ROUND_HALF_EVEN);
				}
			}
			// Sollzeiten holen
			LossollarbeitsplanDto[] soll = getFertigungFac()
					.lossollarbeitsplanFindByLosIId(losIId);
			// Eine Verdichtete Liste anlegen
			HashMap<Integer, ReportLosnachkalkulationDto> listVerdichtet = new HashMap<Integer, ReportLosnachkalkulationDto>();
			// fuer jede Ident eine Zeile anlegen

			for (int i = 0; i < soll.length; i++) {
				ReportLosnachkalkulationDto dto = (ReportLosnachkalkulationDto) listVerdichtet
						.get(soll[i].getArtikelIIdTaetigkeit());
				if (dto == null) {
					dto = new ReportLosnachkalkulationDto();
					listVerdichtet.put(soll[i].getArtikelIIdTaetigkeit(), dto);
					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(
									soll[i].getArtikelIIdTaetigkeit(),
									theClientDto);
					dto.setSArtikelnummer(artikelDto.getCNr());
					dto.setSBezeichnung(artikelDto.formatBezeichnung());
				}

				if (soll[i].getMaschineIId() != null
						&& Helper.short2boolean(soll[i].getBNurmaschinenzeit())) {
					// Dann keine Personalzeit
				} else {
					dto.addiereZuSollmenge(soll[i].getNGesamtzeit());
				}

				// CK:Neu
				if (bSollGleichIstzeiten == true) {
					dto.addiereZuIstmenge(soll[i].getNGesamtzeit().multiply(
							faktorFuerIstGleichSoll));
				}

				if (soll[i].getMaschineIId() != null) {
					dto.addiereZuSollmengeMaschine(soll[i].getNGesamtzeit());
					if (bSollGleichIstzeiten == true) {
						dto.addiereZuIstmengeMaschine(soll[i].getNGesamtzeit()
								.multiply(faktorFuerIstGleichSoll));
					}

					// Soll hinzufuegen
					BigDecimal sollpreis = getZeiterfassungFac()
							.getMaschinenKostenZumZeitpunkt(
									soll[i].getMaschineIId(),
									Helper.cutTimestamp(new Timestamp(System
											.currentTimeMillis())));

					dto.addiereZuSollpreis(soll[i].getNGesamtzeit().multiply(
							sollpreis));
					if (bSollGleichIstzeiten == true) {
						dto.addiereZuIstpreis(soll[i].getNGesamtzeit()
								.multiply(sollpreis)
								.multiply(faktorFuerIstGleichSoll));
					}

				}

				if (Helper.short2boolean(soll[i].getBNurmaschinenzeit()) == false) {
					ArtikellieferantDto artlief = getArtikelFac()
							.getArtikelEinkaufspreis(
									soll[i].getArtikelIIdTaetigkeit(),
									new BigDecimal(1), sMandantWaehrung,
									theClientDto);
					if (artlief != null && artlief.getLief1Preis() != null) {
						BigDecimal bdSollpreis = artlief.getLief1Preis();
						dto.addiereZuSollpreis(soll[i].getNGesamtzeit()
								.multiply(bdSollpreis));
						if (bSollGleichIstzeiten == true) {
							dto.addiereZuIstpreis(soll[i].getNGesamtzeit()
									.multiply(bdSollpreis)
									.multiply(faktorFuerIstGleichSoll));
						}
					}
				}
			}

			// gebuchte Zeiten holen

			// Eine zusaetzliche Zeile fuer gebuchte Taetigkeiten, die im Soll
			// nicht vorkamen
			ReportLosnachkalkulationDto nkSonstige = new ReportLosnachkalkulationDto();
			nkSonstige.setSBezeichnung(getTextRespectUISpr("lp.sonstige",
					theClientDto.getMandant(), theClientDto.getLocUi()));

			if (bSollGleichIstzeiten == false) {

				// Maschinenzeiten
				AuftragzeitenDto[] zeitenMaschine = getZeiterfassungFac()
						.getAllMaschinenzeitenEinesBeleges(losIId, null, null,
								null, theClientDto);
				for (int i = 0; i < zeitenMaschine.length; i++) {
					zeitenMaschine[i].getDdDauer();
					ReportLosnachkalkulationDto nk = (ReportLosnachkalkulationDto) listVerdichtet
							.get(zeitenMaschine[i].getArtikelIId());
					if (nk != null) {
						nk.addiereZuIstmengeMaschine(new BigDecimal(
								zeitenMaschine[i].getDdDauer().doubleValue()));
						nk.addiereZuIstpreis(zeitenMaschine[i].getBdKosten());
					} else {
						nkSonstige.addiereZuIstmengeMaschine(new BigDecimal(
								zeitenMaschine[i].getDdDauer().doubleValue()));
						nkSonstige.addiereZuIstpreis(zeitenMaschine[i]
								.getBdKosten());
					}
				}
				// Istzeiten (ohne Maschinenzeiten)
				AuftragzeitenDto[] zeiten = getZeiterfassungFac()
						.getAllZeitenEinesBeleges(LocaleFac.BELEGART_LOS,
								losIId, null, null, null, null, false, false,
								theClientDto);
				for (int i = 0; i < zeiten.length; i++) {
					zeiten[i].getDdDauer();
					ReportLosnachkalkulationDto nk = (ReportLosnachkalkulationDto) listVerdichtet
							.get(zeiten[i].getArtikelIId());
					if (nk != null) {
						nk.addiereZuIstmenge(new BigDecimal(zeiten[i]
								.getDdDauer().doubleValue()));
						nk.addiereZuIstpreis(zeiten[i].getBdKosten());
					} else {
						nkSonstige.addiereZuIstmenge(new BigDecimal(zeiten[i]
								.getDdDauer().doubleValue()));
						nkSonstige.addiereZuIstpreis(zeiten[i].getBdKosten());
					}
				}
			}
			// Das ganze in ein Array umwandeln
			ReportLosnachkalkulationDto[] result = new ReportLosnachkalkulationDto[listVerdichtet
					.size() + 1];
			int i = 0;
			for (Iterator<Integer> iter = listVerdichtet.keySet().iterator(); iter
					.hasNext(); i++) {
				ReportLosnachkalkulationDto item = (ReportLosnachkalkulationDto) listVerdichtet
						.get(iter.next());
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

	private Object[][] getDataAusgabeListe(Integer[] losIId,
			Integer iSortierung, boolean bVerdichtetNachIdent,
			boolean bVorrangigNachFarbcodeSortiert, Integer artikelklasseIId,
			TheClientDto theClientDto) {
		Session session = null;

		Object[][] dataLokal = null;

		try {

			// hole alle Stkl-Positionen, wg. Kommentar und

			HashMap<Integer, ArrayList> hmStklPositionenAllerLose = new HashMap<Integer, ArrayList>();

			for (int i = 0; i < losIId.length; i++) {

				LosDto losDto = getFertigungFac()
						.losFindByPrimaryKey(losIId[i]);
				if (losDto.getStuecklisteIId() != null) {
					StuecklistepositionDto[] stklPosDtos = getStuecklisteFac()
							.stuecklistepositionFindByStuecklisteIId(
									losDto.getStuecklisteIId(), theClientDto);

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

			c.add(Restrictions.in(FertigungFac.FLR_LOSSOLLMATERIAL_LOS_I_ID,
					losIId));

			if (artikelklasseIId != null) {

				c.createAlias("flrartikel", "a")
						.createAlias("a.flrartikelklasse", "kl")
						.add(Restrictions.eq("kl.i_id", artikelklasseIId));
			}

			List<?> list = c.list();
			// positionen verdichten
			LinkedHashMap<Integer, ReportLosAusgabelisteDto> listVerdichtet = new LinkedHashMap<Integer, ReportLosAusgabelisteDto>();
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRLossollmaterial sollmat = (FLRLossollmaterial) iter.next();

				LosistmaterialDto[] mat = getFertigungFac()
						.losistmaterialFindByLossollmaterialIId(
								sollmat.getI_id());
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKey(
								sollmat.getFlrartikel().getI_id(), theClientDto);

				LoslagerentnahmeDto[] laeger = getFertigungFac()
						.loslagerentnahmeFindByLosIId(sollmat.getLos_i_id());
				BigDecimal lagerstand = new BigDecimal(0);
				if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet()) == true) {
					for (int i = 0; i < laeger.length; i++) {
						lagerstand = lagerstand.add(getLagerFac()
								.getLagerstandOhneExc(artikelDto.getIId(),
										laeger[i].getLagerIId(), theClientDto));
					}

				}
				BigDecimal inFertigung = getFertigungFac()
						.getAnzahlInFertigung(artikelDto.getIId(), theClientDto);
				BigDecimal lagerstandSperrlager = getLagerFac()
						.getLagerstandAllerSperrlaegerEinesMandanten(
								artikelDto.getIId(), theClientDto);

				// Artikelkommentar Text und Bild
				Image imageKommentar = null;
				String sArtikelKommentar = "";
				try {
					ArtikelkommentarDto[] aKommentarDto = getArtikelkommentarFac()
							.artikelkommentardruckFindByArtikelIIdBelegartCNr(
									artikelDto.getIId(),
									LocaleFac.BELEGART_LOS,
									theClientDto.getLocUiAsString(),
									theClientDto);

					// Artikelkommentar kann Text oder Bild sein
					if (aKommentarDto != null) {
						for (int k = 0; k < aKommentarDto.length; k++) {
							String cDatenformat = aKommentarDto[k]
									.getDatenformatCNr().trim();
							if (cDatenformat
									.indexOf(MediaFac.DATENFORMAT_MIMETYPEART_TEXT) != -1) {
								sArtikelKommentar += "\n"
										+ aKommentarDto[k]
												.getArtikelkommentarsprDto()
												.getXKommentar();
							} else if (cDatenformat
									.indexOf(MediaFac.DATENFORMAT_MIMETYPEART_IMAGE) != -1) {
								// es wird hoechstens 1 Bild pro Belegart
								// gedruckt
								imageKommentar = Helper
										.byteArrayToImage(aKommentarDto[k]
												.getArtikelkommentarsprDto()
												.getOMedia());
							}
						}
					}

				} catch (RemoteException ex3) {
					throwEJBExceptionLPRespectOld(ex3);
				}

				// Stuecklistepositionskommentar + Position aus Stueckliste
				String positionAusStueckliste = null;
				String kommentarAusStueckliste = null;
				if (sollmat.getFlrlos().getStueckliste_i_id() != null) {

					ArrayList<StuecklistepositionDto> alPositionen = hmStklPositionenAllerLose
							.get(sollmat.getFlrlos().getI_id());

					for (int u = 0; u < alPositionen.size(); u++) {

						if (sollmat.getFlrartikel().getI_id()
								.equals(alPositionen.get(u).getArtikelIId())
								&& sollmat.getN_menge().equals(
										alPositionen.get(u).getNMenge())
								&& sollmat.getMontageart_i_id().equals(
										alPositionen.get(u).getMontageartIId())) {
							// wenn Menge und Artikel und Montageart gleich

							kommentarAusStueckliste = alPositionen.get(u)
									.getCKommentar();
							positionAusStueckliste = alPositionen.get(u)
									.getCPosition();
							alPositionen.remove(u);
							break;

						}

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
					dto.setBNurZurInfo(Helper.short2Boolean(artikelDto
							.getbNurzurinfo()));

					if (artikelDto.getFarbcodeIId() != null) {
						dto.setSFarbcode(getArtikelFac()
								.farbcodeFindByPrimaryKey(
										artikelDto.getFarbcodeIId()).getCNr());
					}

					dto.setSBezeichnung(artikelDto.getArtikelsprDto().getCBez());
					dto.setSZusatzBezeichnung(artikelDto.getArtikelsprDto()
							.getCZbez());
					dto.setSZusatzBezeichnung2(artikelDto.getArtikelsprDto()
							.getCZbez2());
					dto.setSEinheit(sollmat.getFlrartikel().getEinheit_c_nr());

					// Material
					if (artikelDto.getMaterialIId() != null) {
						MaterialDto materialDto = getMaterialFac()
								.materialFindByPrimaryKey(
										artikelDto.getMaterialIId(),
										theClientDto);
						dto.setSMaterial(materialDto.getBezeichnung());
					}
					// Staerke/Hoehe
					if (artikelDto.getGeometrieDto() != null) {
						if (artikelDto.getGeometrieDto().getFHoehe() != null) {
							dto.setDHoehe(artikelDto.getGeometrieDto()
									.getFHoehe());
						}
						if (artikelDto.getGeometrieDto().getFBreite() != null) {
							dto.setDBreite(artikelDto.getGeometrieDto()
									.getFBreite());
						}
						if (artikelDto.getGeometrieDto().getFTiefe() != null) {
							dto.setDTiefe(artikelDto.getGeometrieDto()
									.getFTiefe());
						}
					}

					dto.setSRevision(artikelDto.getCRevision());
					dto.setSIndex(artikelDto.getCIndex());

					// Verpackung
					if (artikelDto.getVerpackungDto() != null) {
						dto.setSBauform(artikelDto.getVerpackungDto()
								.getCBauform());
						dto.setSVerpackungsart(artikelDto.getVerpackungDto()
								.getCVerpackungsart());
					}

					// Gewicht
					dto.setDGewichtkg(artikelDto.getFGewichtkg());
					// Montage Rasterstehend
					if (artikelDto.getMontageDto() != null
							&& artikelDto.getMontageDto().getFRasterstehend() != null) {
						dto.setDRasterstehend(artikelDto.getMontageDto()
								.getFRasterstehend().doubleValue());
					}
					String sIdent;
					if (sollmat.getFlrartikel().getArtikelart_c_nr()
							.equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
						sIdent = "";
					} else {
						sIdent = sollmat.getFlrartikel().getC_nr();
					}
					dto.setSIdent(sIdent);
					if (sollmat.getFlrartikel().getFlrartikelklasse() != null) {
						dto.setSArtikelklasse(sollmat.getFlrartikel()
								.getFlrartikelklasse().getC_nr());
					}
					if (sollmat.getMontageart_i_id() != null) {
						MontageartDto montageartDto = getStuecklisteFac()
								.montageartFindByPrimaryKey(
										sollmat.getMontageart_i_id(),
										theClientDto);
						dto.setSMontageart(montageartDto.getCBez());
					}

					LosDto losDto = getFertigungFac().losFindByPrimaryKey(
							sollmat.getLos_i_id());
					LagerDto lagerDto = getLagerFac().lagerFindByPrimaryKey(
							losDto.getLagerIIdZiel());
					dto.setSLager(lagerDto.getCNr());

					String sLagerort = getLagerFac()
							.getLagerplaezteEinesArtikels(
									sollmat.getFlrartikel().getI_id(),
									losDto.getLagerIIdZiel());

					dto.setSLagerort(sLagerort);
					// offene Bestellmenge
					dto.setBdAnzahlBestellt(getArtikelbestelltFac()
							.getAnzahlBestellt(artikelDto.getIId()));

					// Stuecklistepositionskommentar + Position aus Stueckliste

					dto.setSKommentarStueckliste(kommentarAusStueckliste);
					dto.setSPositionStueckliste(positionAusStueckliste);

					// key ist -i_id, damit ich keine vorhandenen eintraege
					// ueberschreibe

					// verdichten nach ident

					Iterator<Integer> it = listVerdichtet.keySet().iterator();
					boolean bGefunden = false;

					if (bVerdichtetNachIdent) {
						while (it.hasNext()) {
							Object key = it.next();

							ReportLosAusgabelisteDto temp = (ReportLosAusgabelisteDto) listVerdichtet
									.get(key);
							if ((temp.getSLager() == null && lagerDto.getCNr() == null)
									|| (temp.getSLager() != null && lagerDto
											.getCNr() != null)) {
								if ((temp.getSLager() == null && lagerDto
										.getCNr() == null)
										|| (temp.getSLager().equals(lagerDto
												.getCNr()))) {

									if (temp.getSIdent().equals(sIdent)
											&& !temp.getSIdent().equals("")) {

										bGefunden = true;
										dto.setNMenge(dto.getNMenge().add(
												temp.getNMenge()));
										dto.setNAusgabe(dto.getNAusgabe().add(
												temp.getNAusgabe()));

										String skommenarStuecklisteVorhanden = temp
												.getSKommentarStueckliste();

										if (skommenarStuecklisteVorhanden == null) {
											skommenarStuecklisteVorhanden = "";
										}

										if (dto.getSKommentarStueckliste() != null) {
											if (skommenarStuecklisteVorhanden
													.length() > 0) {
												skommenarStuecklisteVorhanden = skommenarStuecklisteVorhanden
														+ ","
														+ dto.getSKommentarStueckliste();
											}
										}
										dto.setSKommentarStueckliste(skommenarStuecklisteVorhanden);

										String sPositionStuecklisteVorhanden = temp
												.getSPositionStueckliste();

										if (sPositionStuecklisteVorhanden == null) {
											sPositionStuecklisteVorhanden = "";
										}

										if (dto.getSPositionStueckliste() != null) {
											if (sPositionStuecklisteVorhanden
													.length() > 0) {
												sPositionStuecklisteVorhanden = sPositionStuecklisteVorhanden
														+ ","
														+ dto.getSPositionStueckliste();
											}
										}
										dto.setSPositionStueckliste(sPositionStuecklisteVorhanden);

										listVerdichtet.put((Integer) key, dto);

										continue;
									}
								}
							}

						}
					}
					if (bGefunden == false) {
						listVerdichtet.put(new Integer(-sollmat.getI_id()
								.intValue()), dto);
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
							ausgabemenge = ausgabemenge.add(mat[i].getNMenge()
									.multiply(new BigDecimal(-1)));
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

						// Sollmenge zaehlt nur beim ersten eintrag
						if (i == 0) {
							dto.setNMenge(sollmat.getN_menge());
						} else {
							dto.setNMenge(new BigDecimal(0));
						}

						if (sollmat.getFlrartikel().getFlrartikelklasse() != null) {
							dto.setSArtikelklasse(sollmat.getFlrartikel()
									.getFlrartikelklasse().getC_nr());
						} else {
							dto.setSArtikelklasse("");
						}

						if (artikelDto.getFarbcodeIId() != null) {
							dto.setSFarbcode(getArtikelFac()
									.farbcodeFindByPrimaryKey(
											artikelDto.getFarbcodeIId())
									.getCNr());
						}
						dto.setSBezeichnung(artikelDto.getArtikelsprDto()
								.getCBez());
						dto.setSZusatzBezeichnung(artikelDto.getArtikelsprDto()
								.getCZbez());
						dto.setSZusatzBezeichnung2(artikelDto
								.getArtikelsprDto().getCZbez2());
						dto.setSEinheit(sollmat.getFlrartikel()
								.getEinheit_c_nr());

						dto.setBNurZurInfo(Helper.short2Boolean(artikelDto
								.getbNurzurinfo()));

						// Material
						if (artikelDto.getMaterialIId() != null) {
							MaterialDto materialDto = getMaterialFac()
									.materialFindByPrimaryKey(
											artikelDto.getMaterialIId(),
											theClientDto);
							dto.setSMaterial(materialDto.getBezeichnung());
						}
						// Staerke/Hoehe
						if (artikelDto.getGeometrieDto() != null) {
							if (artikelDto.getGeometrieDto().getFHoehe() != null) {
								dto.setDHoehe(artikelDto.getGeometrieDto()
										.getFHoehe());
							}
							if (artikelDto.getGeometrieDto().getFBreite() != null) {
								dto.setDBreite(artikelDto.getGeometrieDto()
										.getFBreite());
							}
							if (artikelDto.getGeometrieDto().getFTiefe() != null) {
								dto.setDTiefe(artikelDto.getGeometrieDto()
										.getFTiefe());
							}
						}

						// Verpackung
						if (artikelDto.getVerpackungDto() != null) {
							dto.setSBauform(artikelDto.getVerpackungDto()
									.getCBauform());
							dto.setSVerpackungsart(artikelDto
									.getVerpackungDto().getCVerpackungsart());
						}
						// Gewicht
						dto.setDGewichtkg(artikelDto.getFGewichtkg());

						dto.setSRevision(artikelDto.getCRevision());
						dto.setSIndex(artikelDto.getCIndex());

						// Montage Rasterstehend
						if (artikelDto.getMontageDto() != null
								&& artikelDto.getMontageDto()
										.getFRasterstehend() != null) {
							dto.setDRasterstehend(artikelDto.getMontageDto()
									.getFRasterstehend().doubleValue());
						}

						if (Helper.short2boolean(artikelDto
								.getBSeriennrtragend())
								|| Helper.short2boolean(artikelDto
										.getBChargennrtragend())) {
							List<SeriennrChargennrMitMengeDto> snrChnrDtos = getLagerFac()
									.getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(
											LocaleFac.BELEGART_LOS,
											mat[i].getIId());
							if (snrChnrDtos != null) {
								String snrchnr = "";
								for (int s = 0; s < snrChnrDtos.size(); s++) {

									SeriennrChargennrMitMengeDto snrChnrDto = snrChnrDtos
											.get(s);

									if (dto.getNAusgabe().doubleValue() < 0) {
										snrchnr += "-";
									}

									snrchnr += Helper.formatZahl(
											snrChnrDto.getNMenge(), 4,
											theClientDto.getLocUi()) + " ";
									snrchnr += snrChnrDto
											.getCSeriennrChargennr() + "; ";

								}
								dto.setSSnrChnr(snrchnr);
							}
						}

						// offene Bestellmenge
						dto.setBdAnzahlBestellt(getArtikelbestelltFac()
								.getAnzahlBestellt(artikelDto.getIId()));

						String sIdent;
						if (sollmat.getFlrartikel().getArtikelart_c_nr()
								.equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
							sIdent = "";
						} else {
							sIdent = sollmat.getFlrartikel().getC_nr();
						}
						dto.setSIdent(sIdent);

						LagerDto lagerDto = getLagerFac()
								.lagerFindByPrimaryKey(lagerIId);

						dto.setSLager(lagerDto.getCNr());

						String sLagerort = getLagerFac()
								.getLagerplaezteEinesArtikels(
										sollmat.getFlrartikel().getI_id(),
										lagerDto.getIId());

						dto.setSLagerort(sLagerort);

						if (sollmat.getMontageart_i_id() != null) {
							MontageartDto montageartDto = getStuecklisteFac()
									.montageartFindByPrimaryKey(
											sollmat.getMontageart_i_id(),
											theClientDto);
							dto.setSMontageart(montageartDto.getCBez());
						}

						dto.setSKommentarStueckliste(kommentarAusStueckliste);
						dto.setSPositionStueckliste(positionAusStueckliste);

						// verdichten nach ident
						Iterator<Integer> it = listVerdichtet.keySet()
								.iterator();
						boolean bGefunden = false;

						if (bVerdichtetNachIdent) {

							while (it.hasNext()) {
								Object key = it.next();

								ReportLosAusgabelisteDto temp = (ReportLosAusgabelisteDto) listVerdichtet
										.get(key);
								if ((temp.getSLager() == null && lagerDto
										.getCNr() == null)
										|| (temp.getSLager() != null && lagerDto
												.getCNr() != null)) {
									if ((temp.getSLager() == null && lagerDto
											.getCNr() == null)
											|| (temp.getSLager()
													.equals(lagerDto.getCNr()))) {
										if (temp.getSIdent().equals(sIdent)
												&& !temp.getSIdent().equals("")) {
											bGefunden = true;
											dto.setNMenge(dto.getNMenge().add(
													temp.getNMenge()));
											dto.setNAusgabe(dto.getNAusgabe()
													.add(temp.getNAusgabe()));
											// PJ 14410
											if (temp.getSSnrChnr() != null) {

												dto.setSSnrChnr(dto
														.getSSnrChnr()
														+ (temp.getSSnrChnr()));

											}
											listVerdichtet.put((Integer) key,
													dto);

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
			LinkedList<ReportLosAusgabelisteDto> listVerdichtetUndSortiert = new LinkedList<ReportLosAusgabelisteDto>();
			for (Iterator<Integer> iter = listVerdichtet.keySet().iterator(); iter
					.hasNext();) {
				listVerdichtetUndSortiert.add(listVerdichtet.get(iter.next()));
			}
			Collections.sort(listVerdichtetUndSortiert,
					new ComparatorAusgabeListe(iSortierung.intValue(),
							bVorrangigNachFarbcodeSortiert));

			dataLokal = new Object[listVerdichtet.size()][AUSG_SPALTENANZAHL];
			int i = 0;
			for (Iterator<ReportLosAusgabelisteDto> iter = listVerdichtetUndSortiert
					.iterator(); iter.hasNext(); i++) {
				ReportLosAusgabelisteDto item = (ReportLosAusgabelisteDto) iter
						.next();
				dataLokal[i][AUSG_ARTIKELKLASSE] = item.getSArtikelklasse();
				dataLokal[i][AUSG_NUR_ZUR_INFO] = item.getBNurZurInfo();
				dataLokal[i][AUSG_AUSGABE] = item.getNAusgabe();
				dataLokal[i][AUSG_BEZEICHNUNG] = item.getSBezeichnung();
				dataLokal[i][AUSG_ZUSATZBEZEICHNUNG] = item
						.getSZusatzBezeichnung();
				dataLokal[i][AUSG_ZUSATZBEZEICHNUNG2] = item
						.getSZusatzBezeichnung2();
				dataLokal[i][AUSG_EINHEIT] = item.getSEinheit();
				dataLokal[i][AUSG_IDENT] = item.getSIdent();
				dataLokal[i][AUSG_LAGER] = item.getSLager();
				dataLokal[i][AUSG_LAGERORT] = item.getSLagerort();
				dataLokal[i][AUSG_MENGE] = item.getNMenge();
				dataLokal[i][AUSG_MONTAGEART] = item.getSMontageart();
				dataLokal[i][AUSG_SCHALE] = item.getISchale();
				dataLokal[i][AUSG_SNRCHNR] = item.getSSnrChnr();
				dataLokal[i][AUSG_ARTIKELBILD] = item.getArtikelbild();
				dataLokal[i][AUSG_KOMMENTAR] = item.getSKommentar();
				dataLokal[i][AUSG_LAGERSTAND] = item.getNLagerstand();
				dataLokal[i][AUSG_LAGERSTAND_SPERRLAGER] = item
						.getNLagerstandSperrlager();

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
					dataLokal[i][AUSG_VERPACKUNGSART] = item
							.getSVerpackungsart();
				}
				// Gewicht
				if (item.getDGewichtkg() != null && item.getDGewichtkg() != 0) {
					dataLokal[i][AUSG_GEWICHTKG] = item.getDGewichtkg();
				}
				// Montage Rasterstehend
				if (item.getDRasterstehend() != null
						&& item.getDRasterstehend() != 0) {
					dataLokal[i][AUSG_RASTERSTEHEND] = item.getDRasterstehend();
				}
				dataLokal[i][AUSG_BESTELLT] = item.getBdAnzahlBestellt();
				dataLokal[i][AUSG_IN_FERTIGUNG] = item.getNInFertigung();
				dataLokal[i][AUSG_STUECKLISTE_KOMMENTAR] = item
						.getSKommentarStueckliste();
				dataLokal[i][AUSG_STUECKLISTE_POSITION] = item
						.getSPositionStueckliste();
			}

		} catch (Exception t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, t);
		} finally {
			closeSession(session);
		}
		return dataLokal;
	}

	private Map<String, Object> getParameterAusgabeliste(LosDto losDto,
			String lose, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException {
		Map<String, Object> mapParameter = new TreeMap<String, Object>();
		mapParameter.put("P_ANGELEGT", new java.util.Date(losDto.getTAnlegen()
				.getTime()));
		mapParameter.put("P_LOSNUMMER", lose);
		mapParameter.put("P_PROJEKT", losDto.getCProjekt());
		mapParameter.put("P_LOSKOMMENTAR", losDto.getCKommentar());
		mapParameter.put("P_LOSLANGTEXT", losDto.getXText());

		String sAuftragsnummer;
		String sKunde;
		String sLieferart;
		String sSpediteur = null;
		if (losDto.getAuftragIId() != null) {
			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(
					losDto.getAuftragIId());
			sAuftragsnummer = auftragDto.getCNr();
			mapParameter.put("P_AUFTRAGNUMMER", sAuftragsnummer);

			mapParameter.put("P_AUFTRAGLIEFERTERMIN",
					auftragDto.getDLiefertermin());
			mapParameter.put("P_ROHS",
					Helper.short2Boolean(auftragDto.getBRoHs()));

			sLieferart = getLocaleFac().lieferartFindByIIdLocaleOhneExc(
					auftragDto.getLieferartIId(), theClientDto.getLocUi(),
					theClientDto);

			if (auftragDto.getSpediteurIId() != null) {
				sSpediteur = getMandantFac().spediteurFindByPrimaryKey(
						auftragDto.getSpediteurIId()).getCNamedesspediteurs();
			}
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					auftragDto.getKundeIIdAuftragsadresse(), theClientDto);
			sKunde = kundeDto.getPartnerDto().getCName1nachnamefirmazeile1();
		} else {
			sKunde = "";
			sLieferart = "";
			sSpediteur = "";
			sAuftragsnummer = "";
		}

		KostenstelleDto kstDto = getSystemFac().kostenstelleFindByPrimaryKey(
				losDto.getKostenstelleIId());
		mapParameter.put("P_LIEFERART", sLieferart);
		mapParameter.put("P_SPEDITEUR", sSpediteur);
		mapParameter.put("P_KOSTENSTELLENUMMER", kstDto.getCNr());
		mapParameter.put("P_KUNDE", sKunde);
		mapParameter.put("P_LOSGROESSE", losDto.getNLosgroesse());

		mapParameter.put("P_PRODUKTIONSBEGINN", losDto.getTProduktionsbeginn());
		mapParameter.put("P_PRODUKTIONSENDE", losDto.getTProduktionsende());

		FertigungsgruppeDto fertGruppeDto = getStuecklisteFac()
				.fertigungsgruppeFindByPrimaryKey(
						losDto.getFertigungsgruppeIId());
		mapParameter.put("P_FERTIGUNGSGRUPPE", fertGruppeDto.getCBez());

		// Materialliste?
		String sMengenEinheit = "";
		if (losDto.getStuecklisteIId() != null) {
			StuecklisteDto stkDto = getStuecklisteFac()
					.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(),
							theClientDto);
			// Einheit
			if (stkDto.getArtikelDto() != null) {
				if (stkDto.getArtikelDto().getEinheitCNr() != null) {
					sMengenEinheit = stkDto.getArtikelDto().getEinheitCNr();
				}
			}
			mapParameter.put("P_STUECKLISTEBEZEICHNUNG", stkDto.getArtikelDto()
					.getArtikelsprDto().getCBez());
			mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG", stkDto
					.getArtikelDto().getArtikelsprDto().getCZbez());
			mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG2", stkDto
					.getArtikelDto().getArtikelsprDto().getCZbez2());
			mapParameter.put("P_STUECKLISTENUMMER", stkDto.getArtikelDto()
					.getCNr());
			mapParameter.put("P_INDEX", stkDto.getArtikelDto().getCIndex());
			mapParameter.put("P_REVISION", stkDto.getArtikelDto()
					.getCRevision());

			mapParameter.put("P_STUECKLISTE_GEWICHTKG", stkDto.getArtikelDto()
					.getFGewichtkg());

			if (stkDto.getArtikelDto().getVerpackungDto() != null) {
				mapParameter.put("P_STUECKLISTE_BAUFORM", stkDto
						.getArtikelDto().getVerpackungDto().getCBauform());
				mapParameter.put("P_STUECKLISTE_VERPACKUNGSART", stkDto
						.getArtikelDto().getVerpackungDto()
						.getCVerpackungsart());
			}

			if (stkDto.getArtikelDto().getGeometrieDto() != null) {
				mapParameter.put("P_STUECKLISTE_BREITETEXT", stkDto
						.getArtikelDto().getGeometrieDto().getCBreitetext());
				mapParameter.put("P_STUECKLISTE_BREITE", stkDto.getArtikelDto()
						.getGeometrieDto().getFBreite());
				mapParameter.put("P_STUECKLISTE_HOEHE", stkDto.getArtikelDto()
						.getGeometrieDto().getFHoehe());
				mapParameter.put("P_STUECKLISTE_TIEFE", stkDto.getArtikelDto()
						.getGeometrieDto().getFTiefe());
			}

			if (stkDto.getArtikelDto().getMontageDto() != null) {
				mapParameter.put("P_STUECKLISTE_RASTERSTEHEND", stkDto
						.getArtikelDto().getMontageDto().getFRasterstehend());
				mapParameter.put("P_STUECKLISTE_RASTERLIEGEND", stkDto
						.getArtikelDto().getMontageDto().getFRasterliegend());

			}

			// Zeichnungsnummer
			StuecklisteeigenschaftDto[] stuecklisteeigenschaftDtos = getStuecklisteFac()
					.stuecklisteeigenschaftFindByStuecklisteIId(
							losDto.getStuecklisteIId());
			ArrayList<Object[]> al = new ArrayList<Object[]>();
			for (int i = 0; i < stuecklisteeigenschaftDtos.length; i++) {
				StuecklisteeigenschaftDto dto = stuecklisteeigenschaftDtos[i];

				Object[] o = new Object[2];
				String sStklEigenschaftArt = dto
						.getStuecklisteeigenschaftartDto().getCBez();
				o[0] = sStklEigenschaftArt;
				o[1] = dto.getCBez();
				al.add(o);

				// Index und Materialplatz auch einzeln an Report uebergeben
				if (sStklEigenschaftArt
						.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_INDEX)) {
					mapParameter.put(P_STUECKLISTENEIGENSCHAFT_INDEX,
							dto.getCBez());
				}
				if (sStklEigenschaftArt
						.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_MATERIALPLATZ)) {
					mapParameter.put(P_STUECKLISTENEIGENSCHAFT_MATERIALPLATZ,
							dto.getCBez());
				}
			}

			// Stuecklisteeigenschaft fuer Subreport
			if (stuecklisteeigenschaftDtos.length > 0) {
				String[] fieldnames = new String[] { "F_EIGENSCHAFTART",
						"F_BEZEICHNUNG" };
				Object[][] dataSub = new Object[al.size()][fieldnames.length];
				dataSub = (Object[][]) al.toArray(dataSub);

				mapParameter.put("DATENSUBREPORT", new LPDatenSubreport(
						dataSub, fieldnames));
			}

			// Stuecklisteeigenschaften als einzelne Parameter fuer Index
			// und Materialplatz
			Hashtable<?, ?> htStklEigenschaften = getStuecklisteReportFac()
					.getStuecklisteEigenschaften(losDto.getStuecklisteIId(),
							theClientDto.getMandant(), theClientDto);
			if (htStklEigenschaften != null) {
				if (htStklEigenschaften
						.containsKey(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_INDEX)) {
					mapParameter
							.put(P_STUECKLISTENEIGENSCHAFT_INDEX,
									htStklEigenschaften
											.get(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_INDEX));
				}
				if (htStklEigenschaften
						.containsKey(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_MATERIALPLATZ)) {
					mapParameter
							.put(P_STUECKLISTENEIGENSCHAFT_MATERIALPLATZ,
									htStklEigenschaften
											.get(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_MATERIALPLATZ));
				}
			}

		} else {
			mapParameter.put("P_STUECKLISTEBEZEICHNUNG", losDto.getCProjekt());
			mapParameter
					.put("P_STUECKLISTENUMMER",
							getTextRespectUISpr("fert.materialliste",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
		}
		mapParameter.put("P_MENGENEINHEIT", sMengenEinheit);
		return mapParameter;
	}

	public ReportLosnachkalkulationDto getDataNachkalkulationMaterial(
			Integer losIId, TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			ReportLosnachkalkulationDto nk = new ReportLosnachkalkulationDto();
			nk.setSBezeichnung(getTextRespectUISpr("lp.material",
					theClientDto.getMandant(), theClientDto.getLocUi()));
			LossollmaterialDto[] soll = getFertigungFac()
					.lossollmaterialFindByLosIId(losIId);
			for (int i = 0; i < soll.length; i++) {
				nk.addiereZuSollpreis(soll[i].getNSollpreis().multiply(
						soll[i].getNMenge()));
				BigDecimal bdPreis = getFertigungFac()
						.getAusgegebeneMengePreis(soll[i].getIId(), null,
								theClientDto);
				BigDecimal bdMenge = getFertigungFac().getAusgegebeneMenge(
						soll[i].getIId(), null, theClientDto);
				nk.addiereZuIstpreis(bdPreis.multiply(bdMenge));
			}
			return nk;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	private HashMap hmAddToAuslastungsvorschau(HashMap hm, Timestamp t,
			Integer artikelgruppeIId, double dSollZumbeginn,
			double dSollZumEnde, double bdPersonalverfuegbarkeit) {
		if (hm == null) {
			hm = new HashMap();
		}

		t = Helper.cutTimestamp(t);

		if (t.before(Helper.cutTimestamp(new Timestamp(System
				.currentTimeMillis())))) {
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

	private HashMap hmAddToZeitentwicklung(HashMap hm, Timestamp t,
			java.sql.Timestamp tVon, Integer artikelgruppeIId, double dSoll,
			double dIst) {
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

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAuslieferliste(java.sql.Timestamp tStichtag,
			TheClientDto theClientDto) {
		this.useCase = UC_AUSLIEFERLISTE;

		HashMap hmPositionen = new HashMap();

		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT lr,(SELECT sum(la.n_menge) FROM FLRLosablieferung la WHERE la.los_i_id=lr.i_id   ) FROM FLRLosReport lr WHERE lr.t_produktionsbeginn <='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tStichtag
						.getTime()))
				+ "' AND lr.status_c_nr IN ('"
				+ LocaleFac.STATUS_ANGELEGT
				+ "','"
				+ LocaleFac.STATUS_IN_PRODUKTION
				+ "','"
				+ LocaleFac.STATUS_TEILERLEDIGT
				+ "') AND lr.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' ORDER BY lr.t_produktionsende ASC , lr.c_nr ASC";

		Query qResult = session.createQuery(sQuery);
		List<?> results = qResult.list();
		ArrayList alDaten = new ArrayList();
		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();

			FLRLosReport los = (FLRLosReport) o[0];
			BigDecimal bdAbgeliefert = new BigDecimal(0);

			if (o[1] != null) {
				bdAbgeliefert = (BigDecimal) o[1];
			}

			if (bdAbgeliefert.doubleValue() < los.getN_losgroesse()
					.doubleValue()) {
				if (los.getFlrauftragposition() != null
						&& !hmPositionen.containsKey(los
								.getFlrauftragposition())) {
					hmPositionen.put(los.getFlrauftragposition().getI_id(), "");
				}

				Object[] oZeile = new Object[AUSLIEFERLISTE_SPALTENANZAHL];
				oZeile[AUSLIEFERLISTE_AUFTRAGSNUMMER] = "";
				oZeile[AUSLIEFERLISTE_LOSNUMMER] = "";
				oZeile[AUSLIEFERLISTE_KUNDE] = "";
				try {
					// SP590
					if (los.getFlrkunde() != null) {
						KundeDto kundeDto = getKundeFac()
								.kundeFindByPrimaryKey(
										los.getFlrkunde().getI_id(),
										theClientDto);
						oZeile[AUSLIEFERLISTE_KUNDE] = kundeDto.getPartnerDto()
								.formatAnrede();
					}

					BigDecimal bdGeliefert = new BigDecimal(0);
					for (Iterator<?> iter2 = los.getAblieferungset().iterator(); iter2
							.hasNext();) {
						FLRLosablieferung item = (FLRLosablieferung) iter2
								.next();
						bdGeliefert = bdGeliefert.add(item.getN_menge());
					}
					oZeile[AUSLIEFERLISTE_GELIEFERT] = bdGeliefert;
					if (los.getFlrstueckliste() != null) {
						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(
										los.getFlrstueckliste().getFlrartikel()
												.getI_id(), theClientDto);
						oZeile[AUSLIEFERLISTE_BEZEICHNUNG] = artikelDto
								.getArtikelsprDto().getCBez();
						oZeile[AUSLIEFERLISTE_ZUSATZBEZEICHNUNG] = artikelDto
								.getArtikelsprDto().getCZbez();
						oZeile[AUSLIEFERLISTE_ARTIKELNUMMER] = los
								.getFlrstueckliste().getFlrartikel().getC_nr();
						oZeile[AUSLIEFERLISTE_LAGERSTAND] = getLagerFac()
								.getLagerstandAllerLagerEinesMandanten(
										los.getFlrstueckliste().getFlrartikel()
												.getI_id(), theClientDto);
						// Offene Fehlmengen
						oZeile[AUSLIEFERLISTE_FEHLMENGE] = getFehlmengeFac()
								.getAnzahlFehlmengeEinesArtikels(
										artikelDto.getIId(), theClientDto);

						LossollarbeitsplanDto[] lossollarbeitsplanDto = getFertigungFac()
								.lossollarbeitsplanFindByLosIId(los.getI_id());

						BigDecimal bdFertigungszeit = new BigDecimal(0);
						for (int j = 0; lossollarbeitsplanDto.length > j; j++) {
							bdFertigungszeit = bdFertigungszeit
									.add(lossollarbeitsplanDto[j]
											.getNGesamtzeit());
						}
						oZeile[AUSLIEFERLISTE_FERTIGUNGSZEIT] = bdFertigungszeit;

						// Rahmenbestellt
						Hashtable<?, ?> htRahmenbestellt = getArtikelbestelltFac()
								.getAnzahlRahmenbestellt(artikelDto.getIId(),
										theClientDto);
						if (htRahmenbestellt
								.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
							BigDecimal bdAnzahlRahmenbestellt = (BigDecimal) htRahmenbestellt
									.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
							oZeile[AUSLIEFERLISTE_RAHMEN_BESTELLT] = bdAnzahlRahmenbestellt;
						}
						oZeile[AUSLIEFERLISTE_RESERVIERUNGEN] = getReservierungFac()
								.getAnzahlReservierungen(artikelDto.getIId(),
										theClientDto);

						oZeile[AUSLIEFERLISTE_RAHMENRESERVIERUNGEN] = getReservierungFac()
								.getAnzahlRahmenreservierungen(
										artikelDto.getIId(), theClientDto);

					} else {
						oZeile[AUSLIEFERLISTE_BEZEICHNUNG] = los.getC_projekt();
						oZeile[AUSLIEFERLISTE_ZUSATZBEZEICHNUNG] = null;
						oZeile[AUSLIEFERLISTE_ARTIKELNUMMER] = getTextRespectUISpr(
								"fert.materialliste",
								theClientDto.getMandant(),
								theClientDto.getLocUi());
					}

					oZeile[AUSLIEFERLISTE_LOSKLASSEN] = getLosLosKlassenAlsString(los
							.getI_id());

					LossollmaterialDto[] sollmatDtos = getFertigungFac()
							.lossollmaterialFindByLosIId(los.getI_id());

					Boolean bZuwenigauflagerzumbeginn = new Boolean(false);

					for (int i = 0; i < sollmatDtos.length; i++) {
						BigDecimal bd = getFertigungFac().getAusgegebeneMenge(
								sollmatDtos[i].getIId(), null, theClientDto);

						BigDecimal nochbenoetigt = sollmatDtos[i].getNMenge()
								.subtract(bd);

						if (nochbenoetigt.doubleValue() > 0) {
							ArtikelDto aDtoSoll = getArtikelFac()
									.artikelFindByPrimaryKeySmall(
											sollmatDtos[i].getArtikelIId(),
											theClientDto);
							BigDecimal bdfiktlag = getInternebestellungFac()
									.getFiktivenLagerstandZuZeitpunkt(
											aDtoSoll,
											theClientDto,
											new Timestamp(los
													.getT_produktionsbeginn()
													.getTime()));

							if (bdfiktlag.doubleValue() < nochbenoetigt
									.doubleValue()) {
								bZuwenigauflagerzumbeginn = true;
								break;
							}

						}

					}

					oZeile[AUSLIEFERLISTE_ZUWENIG_AUF_LAGER_ZUM_ZEITPUNKT] = bZuwenigauflagerzumbeginn;

					Calendar gc = Calendar.getInstance(theClientDto.getLocUi());
					gc.setTime(los.getT_produktionsbeginn());
					oZeile[AUSLIEFERLISTE_KALENDERWOCHE] = new Integer(
							gc.get(Calendar.WEEK_OF_YEAR));
					oZeile[AUSLIEFERLISTE_LOSGROESSE] = los.getN_losgroesse();
					oZeile[AUSLIEFERLISTE_LOSNUMMER] = los.getC_nr();
					oZeile[AUSLIEFERLISTE_FERTIGUNGSGRUPPE] = los
							.getFlrfertigungsgruppe().getC_bez();
					/**
					 * @todo material PJ 4239
					 */
					oZeile[AUSLIEFERLISTE_MATERIAL] = null;
					oZeile[AUSLIEFERLISTE_BEGINN] = los
							.getT_produktionsbeginn();
					oZeile[AUSLIEFERLISTE_ENDE] = los.getT_produktionsende();
					oZeile[AUSLIEFERLISTE_LOSSTATUS] = los.getStatus_c_nr();

					LossollarbeitsplanDto[] sollDtos = getFertigungFac()
							.lossollarbeitsplanFindByLosIId(los.getI_id());

					if (sollDtos.length > 0) {

						AuftragzeitenDto[] zeiten = getZeiterfassungFac()
								.getAllZeitenEinesBeleges(
										LocaleFac.BELEGART_LOS, los.getI_id(),
										null, null, null, null, false, false,
										theClientDto);

						Object[][] oSubData = new Object[sollDtos.length][13];

						for (int i = 0; i < sollDtos.length; i++) {

							oSubData[i][0] = sollDtos[i]
									.getIArbeitsgangnummer();

							ArtikelDto artikelDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(
											sollDtos[i]
													.getArtikelIIdTaetigkeit(),
											theClientDto);
							oSubData[i][1] = artikelDto.getCNr();
							oSubData[i][2] = artikelDto.formatBezeichnung();
							oSubData[i][3] = sollDtos[i].getNGesamtzeit();

							// Istzeitdaten
							BigDecimal bdZeit = new BigDecimal(0);
							for (int j = 0; j < zeiten.length; j++) {
								if (sollDtos[i].getArtikelIIdTaetigkeit()
										.equals(zeiten[j].getArtikelIId())) {
									bdZeit = bdZeit.add(new BigDecimal(
											zeiten[j].getDdDauer()
													.doubleValue()));
								}
							}

							oSubData[i][4] = bdZeit;
							oSubData[i][5] = Helper.short2Boolean(sollDtos[i]
									.getBFertig());

							if (artikelDto.getArtgruIId() != null) {
								oSubData[i][6] = getArtikelFac()
										.artgruFindByPrimaryKey(
												artikelDto.getArtgruIId(),
												theClientDto).getCNr();
							}

							oSubData[i][7] = sollDtos[i].getIUnterarbeitsgang();

							if (sollDtos[i].getMaschineIId() != null) {
								MaschineDto maschineDto = getZeiterfassungFac()
										.maschineFindByPrimaryKey(
												sollDtos[i].getMaschineIId());
								oSubData[i][8] = maschineDto
										.getCIdentifikationsnr();
								oSubData[i][9] = maschineDto.getCBez();
							}
							if (sollDtos[i].getIMaschinenversatztage() != null) {
								oSubData[i][10] = Helper
										.addiereTageZuTimestamp(
												new Timestamp(
														los.getT_produktionsbeginn()
																.getTime()),
												sollDtos[i]
														.getIMaschinenversatztage());
							}

							BigDecimal gut = new BigDecimal(0);
							BigDecimal schlecht = new BigDecimal(0);
							LosgutschlechtDto[] losgutschlechDtos = getFertigungFac()
									.losgutschlechtFindByLossollarbeitsplanIId(
											sollDtos[i].getIId());

							for (int j = 0; j < losgutschlechDtos.length; j++) {
								gut = gut.add(losgutschlechDtos[j].getNGut());
								schlecht = schlecht.add(losgutschlechDtos[j]
										.getNSchlecht());
							}
							oSubData[i][11] = gut;
							oSubData[i][12] = schlecht;
						}

						String[] fieldnames = new String[] { "F_AGNUMMER",
								"F_ARTIKEL", "F_BEZEICHNUNG", "F_SOLLZEIT",
								"F_ISTZEIT", "F_FERTIG", "F_ARTIKELGRUPPE",
								"F_UAGNUMMER", "F_MASCHINENIDENTIFIKATION",
								"F_MASCHINENBEZEICHNUNG", "F_AGBEGINN",
								"F_GUTSTUECK", "F_SCHLECHTSTUECK" };
						oZeile[AUSLIEFERLISTE_ARBEITSGAENGE] = new LPDatenSubreport(
								oSubData, fieldnames);
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
						oZeile[AUSLIEFERLISTE_LOSHATFEHLMENGE] = new Boolean(
								true);
					}
				}
				session2.close();

				oZeile[AUSLIEFERLISTE_PROJEKTKOMMENTAR] = los.getC_projekt();
				if (los.getFlrstueckliste() == null) {
					oZeile[AUSLIEFERLISTE_MATERIALLISTE] = new Boolean(true);

				} else {
					oZeile[AUSLIEFERLISTE_MATERIALLISTE] = new Boolean(false);

					if (los.getFlrstueckliste().getFlrartikel()
							.getFlrartikelgruppe() != null) {
						oZeile[AUSLIEFERLISTE_LOSARTIKELGRUPPE] = los
								.getFlrstueckliste().getFlrartikel()
								.getFlrartikelgruppe().getC_nr();
					}
					if (los.getFlrstueckliste().getFlrartikel()
							.getFlrartikelklasse() != null) {
						oZeile[AUSLIEFERLISTE_LOSARTIKELKLASSE] = los
								.getFlrstueckliste().getFlrartikel()
								.getFlrartikelklasse().getC_nr();
					}

				}
				if (los.getFlrauftragposition() != null
						|| los.getFlrauftrag() != null) {

					if (los.getFlrauftragposition() != null) {
						oZeile = befuelleMitAuftragsdaten(oZeile, los
								.getFlrauftragposition().getFlrauftrag(),
								los.getFlrauftragposition(), theClientDto);
					} else {
						oZeile = befuelleMitAuftragsdaten(oZeile,
								los.getFlrauftrag(), null, theClientDto);

					}

				}

				if (oZeile[AUSLIEFERLISTE_ABLIEFERDATUM] == null) {
					oZeile[AUSLIEFERLISTE_ABLIEFERDATUM] = new java.sql.Date(
							los.getT_produktionsende().getTime());

				}

				alDaten.add(oZeile);
			}

		}

		session.close();

		session = FLRSessionFactory.getFactory().openSession();
		sQuery = "SELECT ap FROM FLRAuftragposition ap WHERE ap.flrauftrag.auftragstatus_c_nr IN ('"
				+ LocaleFac.STATUS_OFFEN
				+ "','"
				+ LocaleFac.STATUS_TEILERLEDIGT
				+ "') AND ap.flrauftrag.mandant_c_nr='"
				+ theClientDto.getMandant() + "' AND ap.n_menge IS NOT NULL";

		qResult = session.createQuery(sQuery);
		results = qResult.list();

		resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			FLRAuftragposition ap = (FLRAuftragposition) resultListIterator
					.next();

			Object[] oZeile = new Object[AUSLIEFERLISTE_SPALTENANZAHL];

			oZeile[AUSLIEFERLISTE_AUFTRAGSNUMMER] = "";
			oZeile[AUSLIEFERLISTE_LOSNUMMER] = "";
			oZeile = befuelleMitAuftragsdaten(oZeile, ap.getFlrauftrag(), ap,
					theClientDto);

			if (!hmPositionen.containsKey(ap.getI_id())) {

				if (((java.sql.Date) oZeile[AUSLIEFERLISTE_ABLIEFERDATUM])
						.after(tStichtag)) {

				} else {
					alDaten.add(oZeile);

				}
			}

		}

		// sortieren

		for (int k = alDaten.size() - 1; k > 0; --k) {
			for (int j = 0; j < k; ++j) {
				Object[] a1 = (Object[]) alDaten.get(j);
				Object[] a2 = (Object[]) alDaten.get(j + 1);

				java.sql.Date s1 = (java.sql.Date) a1[AUSLIEFERLISTE_ABLIEFERDATUM];
				java.sql.Date s2 = (java.sql.Date) a2[AUSLIEFERLISTE_ABLIEFERDATUM];

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

		data = new Object[alDaten.size()][AUSLIEFERLISTE_SPALTENANZAHL];
		data = (Object[][]) alDaten.toArray(data);

		Map<String, Object> mapParameter = new TreeMap<String, Object>();
		mapParameter.put("P_STICHTAG", new Timestamp(
				tStichtag.getTime() - 3600000 * 24));
		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL,
				FertigungReportFac.REPORT_AUSLIEFERLISTE,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();
	}

	private String getLosLosKlassenAlsString(Integer losIId) {

		Session sessionLosklasse = FLRSessionFactory.getFactory().openSession();
		String queryLosklasse = "FROM FLRLoslosklasse l where l.los_i_id="
				+ losIId;

		org.hibernate.Query loslosklasse = sessionLosklasse
				.createQuery(queryLosklasse);

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

	private Object[] befuelleMitAuftragsdaten(Object[] oZeile,
			FLRAuftragReport flrAuftrag, FLRAuftragposition position,
			TheClientDto theClientDto) {

		try {
			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(
					flrAuftrag.getI_id());

			if (position != null) {
				oZeile[AUSLIEFERLISTE_AUFTRAGSPOSITIONNUMMER] = position
						.getI_sort();

				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						flrAuftrag.getKunde_i_id_auftragsadresse(),
						theClientDto);
				oZeile[AUSLIEFERLISTE_KUNDE] = kundeDto.getPartnerDto()
						.formatAnrede();

				oZeile[AUSLIEFERLISTE_AUFTRAGSPOENALE] = Helper
						.short2Boolean(auftragDto.getBPoenale());

				if (position.getFlrartikel() != null) {

					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(
									position.getFlrartikel().getI_id(),
									theClientDto);

					oZeile[AUSLIEFERLISTE_AUFTRAGARTIKEL] = artikelDto.getCNr();
					oZeile[AUSLIEFERLISTE_AUFTRAGARTIKEL_LAGERSTAND] = getLagerFac()
							.getLagerstandAllerLagerEinesMandanten(
									artikelDto.getIId(), theClientDto);

					oZeile[AUSLIEFERLISTE_AUFTRAGARTIKELBEZEICHNUNG] = artikelDto
							.formatArtikelbezeichnung();
					if (artikelDto.getArtgruIId() != null) {
						oZeile[AUSLIEFERLISTE_AUFTRAGARTIKELGRUPPE] = getArtikelFac()
								.artgruFindByPrimaryKey(
										artikelDto.getArtgruIId(), theClientDto)
								.getCNr();
					}
					if (artikelDto.getArtklaIId() != null) {
						oZeile[AUSLIEFERLISTE_AUFTRAGARTIKELKLASSE] = getArtikelFac()
								.artklaFindByPrimaryKey(
										artikelDto.getArtklaIId(), theClientDto)
								.getCNr();
					}

				} else {
					oZeile[AUSLIEFERLISTE_AUFTRAGARTIKELBEZEICHNUNG] = position
							.getC_bez();
				}

				oZeile[AUSLIEFERLISTE_AUFTRAGPOSITIONSMENGE] = position
						.getN_menge();

				oZeile[AUSLIEFERLISTE_AUFTRAG_OFFENEMENGE] = position
						.getN_offenemenge();

				if (auftragDto.getAuftragartCNr().equals(
						AuftragServiceFac.AUFTRAGART_RAHMEN)) {
					oZeile[AUSLIEFERLISTE_AUFTRAG_OFFENERAHMENMENGE] = position
							.getN_offenerahmenmenge();
				}

				oZeile[AUSLIEFERLISTE_AUFTRAG_POSITIONSTERMIN] = new Timestamp(
						position.getT_uebersteuerterliefertermin().getTime());

			}

			if (auftragDto != null) {

				oZeile[AUSLIEFERLISTE_AUFTRAGSPOENALE] = Helper
						.short2Boolean(auftragDto.getBPoenale());

				oZeile[AUSLIEFERLISTE_AUFTRAGSNUMMER] = auftragDto.getCNr();
				oZeile[AUSLIEFERLISTE_PROJEKT] = auftragDto
						.getCBezProjektbezeichnung();
				oZeile[AUSLIEFERLISTE_LIEFERTERMIN] = auftragDto
						.getDLiefertermin();

				oZeile[AUSLIEFERLISTE_AUFTRAGART] = auftragDto
						.getAuftragartCNr();
				oZeile[AUSLIEFERLISTE_AUFTRAGSKUNDE_NAME1] = flrAuftrag
						.getFlrkunde().getFlrpartner()
						.getC_name1nachnamefirmazeile1();
				oZeile[AUSLIEFERLISTE_AUFTRAGSKUNDE_NAME2] = flrAuftrag
						.getFlrkunde().getFlrpartner()
						.getC_name2vornamefirmazeile2();
				oZeile[AUSLIEFERLISTE_AUFTRAGSKUNDE_STRASSE] = flrAuftrag
						.getFlrkunde().getFlrpartner().getC_strasse();

				if (flrAuftrag.getFlrkunde().getFlrpartner().getFlrlandplzort() != null) {
					oZeile[AUSLIEFERLISTE_AUFTRAGSKUNDE_ORT] = flrAuftrag
							.getFlrkunde().getFlrpartner().getFlrlandplzort()
							.getFlrort().getC_name();
					oZeile[AUSLIEFERLISTE_AUFTRAGSKUNDE_LKZ] = flrAuftrag
							.getFlrkunde().getFlrpartner().getFlrlandplzort()
							.getFlrland().getC_lkz();
					oZeile[AUSLIEFERLISTE_AUFTRAGSKUNDE_PLZ] = flrAuftrag
							.getFlrkunde().getFlrpartner().getFlrlandplzort()
							.getC_plz();

				}

				oZeile[AUSLIEFERLISTE_AUFTRAGSKUNDE_TELEFON] = flrAuftrag
						.getFlrkunde().getFlrpartner().getC_telefon();

				if (flrAuftrag.getFlrkundeansprechpartner() != null) {
					AnsprechpartnerDto anspDto = getAnsprechpartnerFac()
							.ansprechpartnerFindByPrimaryKey(
									flrAuftrag.getFlrkundeansprechpartner()
											.getI_id(), theClientDto);
					oZeile[AUSLIEFERLISTE_AUFTRAGSKUNDE_ANSPRECHPARTNER] = anspDto
							.getPartnerDto().formatAnrede();

					oZeile[AUSLIEFERLISTE_AUFTRAGSKUNDE_ANSPRECHPARTNER_TELEFONDW] = flrAuftrag
							.getFlrkundeansprechpartner().getC_telefon();

				}

				// Ablieferdatum

				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						flrAuftrag.getKunde_i_id_lieferadresse(), theClientDto);
				oZeile[AUSLIEFERLISTE_AUFTRAG_LIEFERADRESSE] = kundeDto
						.getPartnerDto().formatAnrede();

				oZeile[AUSLIEFERLISTE_AUFTRAG_LIEFERART] = getLocaleFac()
						.lieferartFindByPrimaryKey(
								auftragDto.getLieferartIId(), theClientDto)
						.formatBez();
				oZeile[AUSLIEFERLISTE_AUFTRAG_SPEDITEUR] = getMandantFac()
						.spediteurFindByPrimaryKey(auftragDto.getSpediteurIId())
						.getCNamedesspediteurs();

				java.sql.Date dTermin = null;

				if (position != null) {
					dTermin = Helper.addiereTageZuDatum(new Date(position
							.getT_uebersteuerterliefertermin().getTime()),
							-kundeDto.getILieferdauer().intValue());
				} else {
					dTermin = Helper.addiereTageZuDatum(new Date(flrAuftrag
							.getT_liefertermin().getTime()), -kundeDto
							.getILieferdauer().intValue());
				}

				oZeile[AUSLIEFERLISTE_ABLIEFERDATUM] = dTermin;

			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return oZeile;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAuslastungsvorschauDetailliert(
			java.sql.Timestamp tStichtag, TheClientDto theClientDto) {

		this.useCase = UC_AUSLASTUNGSVORSCHAU_DETAILIERT;

		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT lr,(SELECT sum(la.n_menge) FROM FLRLosablieferung la WHERE la.los_i_id=lr.i_id   ) FROM FLRLosReport lr WHERE lr.t_produktionsbeginn <='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tStichtag
						.getTime()))
				+ "' AND lr.status_c_nr IN ('"
				+ LocaleFac.STATUS_ANGELEGT
				+ "','"
				+ LocaleFac.STATUS_IN_PRODUKTION
				+ "','"
				+ LocaleFac.STATUS_TEILERLEDIGT
				+ "') AND lr.mandant_c_nr='"
				+ theClientDto.getMandant() + "'";

		java.sql.Timestamp tSpaetestesDatum = new Timestamp(
				System.currentTimeMillis());

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

			if (bdAbgeliefert.doubleValue() < flrLosReport.getN_losgroesse()
					.doubleValue()) {
				BigDecimal sollsatzfaktor = new BigDecimal(1);
				if (flrLosReport.getT_produktionsende().after(tSpaetestesDatum)) {
					tSpaetestesDatum = new Timestamp(flrLosReport
							.getT_produktionsende().getTime());
				}

				if (bdAbgeliefert.doubleValue() > 0) {
					sollsatzfaktor = bdAbgeliefert.divide(
							flrLosReport.getN_losgroesse(), 4,
							BigDecimal.ROUND_HALF_EVEN);
				}

				try {

					LossollarbeitsplanDto[] sollDtos = getFertigungFac()
							.lossollarbeitsplanFindByLosIId(
									flrLosReport.getI_id());

					if (sollDtos.length > 0) {

						for (int i = 0; i < sollDtos.length; i++) {

							LossollarbeitsplanDto saDto = sollDtos[i];

							if (Helper.short2boolean(saDto.getBFertig()) == false) {
								if (Helper.short2boolean(saDto
										.getBNurmaschinenzeit()) == false) {
									ArtikelDto artikelDto = getArtikelFac()
											.artikelFindByPrimaryKeySmall(
													sollDtos[i]
															.getArtikelIIdTaetigkeit(),
													theClientDto);
									// sollzeit berechnen
									double dSollzeit = sollDtos[i]
											.getNGesamtzeit().doubleValue()
											* sollsatzfaktor.doubleValue();

									java.util.Date beginnDatum = flrLosReport
											.getT_produktionsbeginn();
									if (saDto.getIMaschinenversatztage() != null) {
										beginnDatum = Helper
												.addiereTageZuDatum(
														flrLosReport
																.getT_produktionsbeginn(),
														saDto.getIMaschinenversatztage());
									}

									Timestamp tsBginn = new Timestamp(
											beginnDatum.getTime());

									Object[] oZeile = new Object[AV_DETAIL_ANZAHL_SPALTEN];

									oZeile[AV_DETAIL_DAUER] = new Double(
											dSollzeit);

									if (tsBginn.before(Helper
											.cutTimestamp(new Timestamp(System
													.currentTimeMillis())))) {
										tsBginn = Helper
												.cutTimestamp(new Timestamp(
														System.currentTimeMillis()));
									}

									oZeile[AV_DETAIL_BEGINN_DATUM] = tsBginn;

									oZeile[AV_DETAIL_ARTIKELNUMMER] = artikelDto
											.getCNr();

									if (artikelDto.getArtikelsprDto() != null) {
										oZeile[AV_DETAIL_BEZEICHNUNG] = artikelDto
												.getArtikelsprDto().getCBez();
									}

									if (artikelDto.getArtgruIId() != null) {

										if (!hmArtikelgruppen
												.containsKey(artikelDto
														.getArtgruIId())) {

											ArtgruDto agDto = getArtikelFac()
													.artgruFindByPrimaryKey(
															artikelDto
																	.getArtgruIId(),
															theClientDto);
											hmArtikelgruppen.put(
													artikelDto.getArtgruIId(),
													agDto.getBezeichnung());
										}
										oZeile[AV_DETAIL_ARTIKELGRUPPE] = hmArtikelgruppen
												.get(artikelDto.getArtgruIId());

									} else {
										oZeile[AV_DETAIL_ARTIKELGRUPPE] = "";
										hmArtikelgruppen.put(null, "");

									}
									oZeile[AV_DETAIL_LOSNUMMER] = flrLosReport
											.getC_nr();
									oZeile[AV_DETAIL_PROJEKT] = flrLosReport
											.getC_projekt();
									oZeile[AV_DETAIL_ARBEITSGANG] = saDto
											.getIArbeitsgangnummer();
									oZeile[AV_DETAIL_UNTERARBEITSGANG] = saDto
											.getIUnterarbeitsgang();

									oZeile[AV_DETAIL_KOMMENTAR] = flrLosReport
											.getC_kommentar();

									oZeile[AV_DETAIL_SORT_VERFUEGBARKEIT] = "1";

									if (flrLosReport.getFlrauftrag() != null) {

										oZeile[AV_DETAIL_AUFTRAG] = flrLosReport
												.getFlrauftrag().getC_nr();

										oZeile[AV_DETAIL_KUNDE] = flrLosReport
												.getFlrauftrag()
												.getFlrkunde()
												.getFlrpartner()
												.getC_name1nachnamefirmazeile1();

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
			SollverfuegbarkeitDto[] dtos = getZeiterfassungFac()
					.getVerfuegbareSollzeit(
							Helper.cutTimestamp(new Timestamp(System
									.currentTimeMillis())), tSpaetestesDatum,
							theClientDto);
			for (int i = 0; i < dtos.length; i++) {
				if (dtos[i].isBMannarbeitszeit()) {

					Object[] oZeile = new Object[AV_DETAIL_ANZAHL_SPALTEN];

					if (dtos[i].getIGruppeid() != null) {

						if (!hmArtikelgruppen.containsKey(dtos[i]
								.getIGruppeid())) {

							ArtgruDto agDto = getArtikelFac()
									.artgruFindByPrimaryKey(
											dtos[i].getIGruppeid(),
											theClientDto);
							hmArtikelgruppen.put(dtos[i].getIGruppeid(),
									agDto.getBezeichnung());
						}
						oZeile[AV_DETAIL_ARTIKELGRUPPE] = hmArtikelgruppen
								.get(dtos[i].getIGruppeid());

					} else {
						oZeile[AV_DETAIL_ARTIKELGRUPPE] = "";
					}

					oZeile[AV_DETAIL_BEGINN_DATUM] = dtos[i].getTDatum();
					oZeile[AV_DETAIL_VERFUEGBARKEIT] = dtos[i]
							.getNSollstunden();

					oZeile[AV_DETAIL_SORT_VERFUEGBARKEIT] = "0";

					alDaten.add(oZeile);

				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		// Fehlende Tage auffuellen

		Timestamp tHeute = Helper.cutTimestamp(new Timestamp(System
				.currentTimeMillis()));
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

					if (tBeginnDatum.equals(tHeute)
							&& artikelgruppeVorhanden.equals(artikelgruppe)) {
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
			tHeute = Helper.cutTimestamp(Helper.addiereTageZuTimestamp(tHeute,
					1));

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
		mapParameter.put("P_STICHTAG", new Timestamp(
				tStichtag.getTime() - 3600000 * 24));

		data = new Object[alDaten.size()][AV_DETAIL_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL,
				FertigungReportFac.REPORT_AUSLASTUNGSVORSCHAU_DETAILIERT,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAuslastungsvorschau(java.sql.Timestamp tStichtag,
			boolean bSortiertNachArtikelgruppe, TheClientDto theClientDto) {
		this.useCase = UC_AUSLASTUNGSVORSCHAU;

		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT lr,(SELECT sum(la.n_menge) FROM FLRLosablieferung la WHERE la.los_i_id=lr.i_id   ) FROM FLRLosReport lr WHERE lr.t_produktionsbeginn <='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tStichtag
						.getTime()))
				+ "' AND lr.status_c_nr IN ('"
				+ LocaleFac.STATUS_ANGELEGT
				+ "','"
				+ LocaleFac.STATUS_IN_PRODUKTION
				+ "','"
				+ LocaleFac.STATUS_TEILERLEDIGT
				+ "') AND lr.mandant_c_nr='"
				+ theClientDto.getMandant() + "'";

		HashMap hmGesamt = new HashMap();
		HashMap hmArtikelgruppen = new HashMap();
		java.sql.Timestamp tSpaetestesDatum = new Timestamp(
				System.currentTimeMillis());

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

			if (bdAbgeliefert.doubleValue() < flrLosReport.getN_losgroesse()
					.doubleValue()) {
				BigDecimal sollsatzfaktor = new BigDecimal(1);

				if (flrLosReport.getT_produktionsende().after(tSpaetestesDatum)) {
					tSpaetestesDatum = new Timestamp(flrLosReport
							.getT_produktionsende().getTime());
				}

				if (bdAbgeliefert.doubleValue() > 0) {
					sollsatzfaktor = bdAbgeliefert.divide(
							flrLosReport.getN_losgroesse(), 4,
							BigDecimal.ROUND_HALF_EVEN);
				}

				try {

					LossollarbeitsplanDto[] sollDtos = getFertigungFac()
							.lossollarbeitsplanFindByLosIId(
									flrLosReport.getI_id());

					if (sollDtos.length > 0) {

						for (int i = 0; i < sollDtos.length; i++) {

							ArtikelDto artikelDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(
											sollDtos[i]
													.getArtikelIIdTaetigkeit(),
											theClientDto);
							// sollzeit berechnen
							double dSollzeit = sollDtos[i].getNGesamtzeit()
									.doubleValue();

							hmGesamt = hmAddToAuslastungsvorschau(
									hmGesamt,
									new java.sql.Timestamp(flrLosReport
											.getT_produktionsbeginn().getTime()),
									artikelDto.getArtgruIId(), dSollzeit
											* sollsatzfaktor.doubleValue(), 0,
									0);

							hmGesamt = hmAddToAuslastungsvorschau(hmGesamt,
									new java.sql.Timestamp(flrLosReport
											.getT_produktionsende().getTime()),
									artikelDto.getArtgruIId(), 0, dSollzeit
											* sollsatzfaktor.doubleValue(), 0);

							if (!hmArtikelgruppen.containsKey(artikelDto
									.getArtgruIId())) {
								hmArtikelgruppen.put(artikelDto.getArtgruIId(),
										null);
							}

						}

					}

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}
		}
		try {
			SollverfuegbarkeitDto[] dtos = getZeiterfassungFac()
					.getVerfuegbareSollzeit(
							Helper.cutTimestamp(new Timestamp(System
									.currentTimeMillis())), tSpaetestesDatum,
							theClientDto);
			for (int i = 0; i < dtos.length; i++) {
				if (dtos[i].isBMannarbeitszeit()) {
					hmGesamt = hmAddToAuslastungsvorschau(hmGesamt,
							dtos[i].getTDatum(), dtos[i].getIGruppeid(), 0, 0,
							dtos[i].getNSollstunden().doubleValue());
				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		// Fehlende Tage mit 0 nachtragen
		int iAnzahlTage = Helper.getDifferenzInTagen(
				new Timestamp(System.currentTimeMillis()), tSpaetestesDatum);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		for (int i = 0; i < iAnzahlTage; i++) {

			Iterator itGru = hmArtikelgruppen.keySet().iterator();
			while (itGru.hasNext()) {
				hmGesamt = hmAddToAuslastungsvorschau(hmGesamt,
						new Timestamp(c.getTimeInMillis()),
						(Integer) itGru.next(), 0, 0, 0);
			}

			c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);
		}

		Map<String, Object> mapParameter = new TreeMap<String, Object>();
		mapParameter.put("P_SORTIERTNACHARTIKELGRUPPE", new Boolean(
				bSortiertNachArtikelgruppe));
		mapParameter.put("P_STICHTAG", new Timestamp(
				tStichtag.getTime() - 3600000 * 24));

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
				try {
					if (artikelgruppeIId == null) {
						zeile[ZE_ARTIKELGRUPPE] = "";
					} else {
						zeile[ZE_ARTIKELGRUPPE] = getArtikelFac()
								.artgruFindByPrimaryKey(artikelgruppeIId,
										theClientDto).getCNr();
					}

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
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
		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL,
				FertigungReportFac.REPORT_AUSLASTUNGSVORSCHAU,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printZeitentwicklung(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, boolean bSortiertNachArtikelgruppe,
			TheClientDto theClientDto) {
		this.useCase = UC_ZEITENTWICKLUNG;

		Map<String, Object> mapParameter = new TreeMap<String, Object>();
		mapParameter.put("P_VON", Helper.cutTimestamp(tVon));
		mapParameter.put("P_BIS", new Timestamp(tBis.getTime() - 3600000 * 24));
		mapParameter.put("P_SORTIERTNACHARTIKELGRUPPE", new Boolean(
				bSortiertNachArtikelgruppe));

		HashMap mhLosIIds = new HashMap();

		java.sql.Timestamp tFruehestesDatum = new Timestamp(
				System.currentTimeMillis());

		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT lr FROM FLRLosReport lr WHERE lr.t_manuellerledigt <='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tBis.getTime()))
				+ "' AND lr.t_manuellerledigt >='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tVon.getTime()))
				+ "' AND lr.mandant_c_nr='" + theClientDto.getMandant() + "'";

		Query qResult = session.createQuery(sQuery);
		List<?> results = qResult.list();
		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			FLRLosReport flrLosReport = (FLRLosReport) resultListIterator
					.next();
			mhLosIIds.put(flrLosReport.getI_id(), null);
		}

		Session session2 = FLRSessionFactory.getFactory().openSession();
		sQuery = "SELECT la.flrlos.i_id,sum(la.n_menge) FROM FLRLosablieferung la WHERE la.t_aendern <='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tBis.getTime()))
				+ "' AND la.t_aendern >='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tVon.getTime()))
				+ "' AND la.flrlos.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' GROUP BY la.los_i_id";

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

			if (bdAbgeliefert.doubleValue() >= losDto.getNLosgroesse()
					.doubleValue()) {

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

				AuftragzeitenDto[] dtos = getZeiterfassungFac()
						.getAllZeitenEinesBeleges(LocaleFac.BELEGART_LOS,
								losIId, null, null, null, null, false, false,
								theClientDto);

				if (dtos.length > 0) {

					for (int i = 0; i < dtos.length; i++) {

						if (dtos[i] != null && dtos[i].getDdDauer() != null) {
							if (dtos[dtos.length - 1].getTsBeginn().before(
									tFruehestesDatum)) {
								tFruehestesDatum = dtos[0].getTsBeginn();
							}
							hmGesamt = hmAddToZeitentwicklung(hmGesamt,
									Helper.cutTimestamp(dtos[dtos.length - 1]
											.getTsBeginn()), tVon,
									dtos[i].getArtikelgruppeIId(), 0,
									dtos[i].getDdDauer());
						}
					}

					LossollarbeitsplanDto[] sollDtos = getFertigungFac()
							.lossollarbeitsplanFindByLosIId(losIId);

					if (sollDtos.length > 0) {

						for (int i = 0; i < sollDtos.length; i++) {

							ArtikelDto artikelDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(
											sollDtos[i]
													.getArtikelIIdTaetigkeit(),
											theClientDto);

							double dSollzeit = sollDtos[i].getNGesamtzeit()
									.doubleValue();
							hmGesamt = hmAddToZeitentwicklung(hmGesamt,
									Helper.cutTimestamp(dtos[0].getTsBeginn()),
									tVon, artikelDto.getArtgruIId(), dSollzeit,
									0);

							if (!hmArtikelgruppen.containsKey(artikelDto
									.getArtgruIId())) {
								hmArtikelgruppen.put(artikelDto.getArtgruIId(),
										null);
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
				hmGesamt = hmAddToZeitentwicklung(hmGesamt,
						new Timestamp(c.getTimeInMillis()), tVon,
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
				try {
					if (artikelgruppeIId == null) {
						zeile[ZE_ARTIKELGRUPPE] = "";
					} else {
						zeile[ZE_ARTIKELGRUPPE] = getArtikelFac()
								.artgruFindByPrimaryKey(artikelgruppeIId,
										theClientDto).getCNr();
					}

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
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

		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL,
				FertigungReportFac.REPORT_ZEITENTWICKLUNG,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printMonatsauswertung(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, boolean bVerdichtet,
			TheClientDto theClientDto) {
		this.useCase = UC_MONATSAUSWERTUNG;
		this.index = -1;

		Map<String, Object> mapParameter = new TreeMap<String, Object>();

		// Zuerst Alle Lose Ohne Kunden
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();
		session.enableFilter("filterMandant").setParameter("paramMandant",
				theClientDto.getMandant());
		session.enableFilter("filterLocale").setParameter("paramLocale",
				Helper.locale2String(theClientDto.getLocUi()));
		String query = "SELECT l, (SELECT SUM(s.n_gesamtzeit) FROM FLRLossollarbeitsplan s  WHERE s.los_i_id=l.i_id)  FROM FLRLosReport l LEFT OUTER JOIN l.flrauftrag a LEFT OUTER JOIN l.flrauftrag.flrkunde.flrpartner p WHERE l.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' "
				+ " AND l.status_c_nr ='"
				+ LocaleFac.STATUS_ERLEDIGT
				+ "' AND (l.t_manuellerledigt>='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tVon.getTime()))
				+ "'   OR l.t_erledigt>='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tVon.getTime()))
				+ "')"
				+ " AND (l.t_manuellerledigt<='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tBis.getTime()))
				+ "'   OR l.t_erledigt<='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tBis.getTime()))
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

			losMonatsauswertungDto.setSFertigungsgruppe(flrLosReport
					.getFlrfertigungsgruppe().getC_bez());

			losMonatsauswertungDto.setSLosnummer(flrLosReport.getC_nr());
			// Sollzeit/Istzeit
			BigDecimal sollzeit = new BigDecimal(0.0000);
			if (o[1] != null) {
				sollzeit = (BigDecimal) o[1];
			}
			losMonatsauswertungDto.setNSollzeit(sollzeit);

			Double zeiten = new Double(0);
			try {
				zeiten = getZeiterfassungFac().getSummeZeitenEinesBeleges(
						LocaleFac.BELEGART_LOS, flrLosReport.getI_id(), null,
						null, null, null, theClientDto);
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

			BigDecimal istzeit = new BigDecimal(zeiten.doubleValue());

			losMonatsauswertungDto.setNIstZeit(istzeit);
			losMonatsauswertungDto.setNLosgroesse(flrLosReport
					.getN_losgroesse());

			if (flrLosReport.getF_bewertung() != null) {
				BigDecimal abw = sollzeit.subtract(istzeit).multiply(
						new BigDecimal(flrLosReport.getF_bewertung()
								.doubleValue()));
				abw = abw.divide(new BigDecimal(100), 4,
						BigDecimal.ROUND_HALF_EVEN);
				losMonatsauswertungDto.setNAbweichung(abw);
				losMonatsauswertungDto.setFBewertung(flrLosReport
						.getF_bewertung());
			} else {
				losMonatsauswertungDto.setNAbweichung(sollzeit
						.subtract(istzeit));
			}

			if (flrLosReport.getFlrstueckliste() == null) {
				if (flrLosReport.getC_projekt() != null) {
					losMonatsauswertungDto.setArtikelnummer(flrLosReport
							.getC_projekt());
				} else {
					losMonatsauswertungDto.setArtikelnummer("");
				}
			} else {
				losMonatsauswertungDto.setArtikelnummer(flrLosReport
						.getFlrstueckliste().getFlrartikel().getC_nr());

				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(
								flrLosReport.getFlrstueckliste()
										.getFlrartikel().getI_id(),
								theClientDto);

				losMonatsauswertungDto.setArtikelbezeichnung(artikelDto
						.formatBezeichnung());

			}

			if (flrLosReport.getFlrauftrag() == null) {

				if (flrLosReport.getFlrkunde() != null) {
					String kunde = flrLosReport.getFlrkunde().getFlrpartner()
							.getC_name1nachnamefirmazeile1();
					losMonatsauswertungDto.setSKunde(kunde);
				} else {
					losMonatsauswertungDto.setSKunde("");
				}

			} else {
				String kunde = flrLosReport.getFlrauftrag().getFlrkunde()
						.getFlrpartner().getC_name1nachnamefirmazeile1();
				losMonatsauswertungDto.setSKunde(kunde);
			}

			alDaten.add(losMonatsauswertungDto);
		}

		session.close();

		data = new Object[alDaten.size()][MONATSAUSWERTUNG_ANZAHL_FELDER];

		int i = 0;

		Iterator<LosMonatsauswertungDto> it = alDaten.iterator();
		while (it.hasNext()) {
			LosMonatsauswertungDto losMonatsauswertungDto = (LosMonatsauswertungDto) it
					.next();

			data[i][MONATSAUSWERTUNG_ARTIKELNUMMER] = losMonatsauswertungDto
					.getArtikelnummer();
			data[i][MONATSAUSWERTUNG_BEZEICHNUNG] = losMonatsauswertungDto
					.getArtikelbezeichnung();
			data[i][MONATSAUSWERTUNG_KUNDE] = losMonatsauswertungDto
					.getSKunde();
			data[i][MONATSAUSWERTUNG_ISTZEIT] = losMonatsauswertungDto
					.getNIstZeit();
			data[i][MONATSAUSWERTUNG_SOLLZEIT] = losMonatsauswertungDto
					.getNSollzeit();
			data[i][MONATSAUSWERTUNG_ABWEICHUNG] = losMonatsauswertungDto
					.getNAbweichung();
			data[i][MONATSAUSWERTUNG_LOSGROESSE] = losMonatsauswertungDto
					.getNLosgroesse();
			data[i][MONATSAUSWERTUNG_FERTIGUNGSGRUPPE] = losMonatsauswertungDto
					.getSFertigungsgruppe();
			data[i][MONATSAUSWERTUNG_LOSNUMMER] = losMonatsauswertungDto
					.getSLosnummer();
			data[i][MONATSAUSWERTUNG_BEWERTUNG] = losMonatsauswertungDto
					.getFBewertung();
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

		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL,
				FertigungReportFac.REPORT_MONATSAUSWERTUNG,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();
	}

	public JasperPrintLP printLoszeiten(Integer losIId, int iSortierung,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			TheClientDto theClientDto) {
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
				mapParameter.put(
						"P_SORTIERUNG",
						getTextRespectUISpr("lp.person",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			} else if (iSortierung == LOSZEITEN_OPTION_SORTIERUNG_ARTIKEL) {
				mapParameter.put(
						"P_SORTIERUNG",
						getTextRespectUISpr("lp.artikel",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			} else if (iSortierung == LOSZEITEN_OPTION_SORTIERUNG_AG_UAG) {
				mapParameter.put(
						"P_SORTIERUNG",
						getTextRespectUISpr("fert.ag",
								theClientDto.getMandant(),
								theClientDto.getLocUi())
								+ "+"
								+ getTextRespectUISpr("fert.uag",
										theClientDto.getMandant(),
										theClientDto.getLocUi()));
			}

			if (tBis != null) {

				tBis = Helper.cutTimestamp(Helper.addiereTageZuTimestamp(tBis,
						1));
			}

			if (iSortierung == LOSZEITEN_OPTION_SORTIERUNG_PERSON) {
				personalZeitenDtos = getZeiterfassungFac()
						.getAllZeitenEinesBeleges(LocaleFac.BELEGART_LOS,
								losIId, null, null, tVon, tBis, false, true,
								theClientDto);
			} else {
				personalZeitenDtos = getZeiterfassungFac()
						.getAllZeitenEinesBeleges(LocaleFac.BELEGART_LOS,
								losIId, null, null, tVon, tBis, true, false,
								theClientDto);
			}

			if (iSortierung == LOSZEITEN_OPTION_SORTIERUNG_PERSON) {
				maschinenZeitenDtos = getZeiterfassungFac()
						.getAllMaschinenzeitenEinesBeleges(losIId, null, tVon,
								tBis, theClientDto);
			} else {
				maschinenZeitenDtos = getZeiterfassungFac()
						.getAllMaschinenzeitenEinesBeleges(losIId, null, tVon,
								tBis, theClientDto);
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
				data[i][LOSZEITEN_PERSON_MASCHINE] = aLoszeitenDtos[i]
						.getSPersonalMaschinenname();
				data[i][LOSZEITEN_PERSONALNUMMER_MASCHINENINVENARNUMMER] = aLoszeitenDtos[i]
						.getSPersonalnummer();
				data[i][LOSZEITEN_ARTIKELNUMMER] = aLoszeitenDtos[i]
						.getSArtikelcnr();
				data[i][LOSZEITEN_BEMERKUNG] = aLoszeitenDtos[i]
						.getSZeitbuchungtext();
				data[i][LOSZEITEN_BEMERKUNG_LANG] = aLoszeitenDtos[i]
						.getSKommentar();
				data[i][LOSZEITEN_BEMERKUNG_LANG] = aLoszeitenDtos[i]
						.getSKommentar();
				data[i][LOSZEITEN_MASCHINENGRUPPE] = aLoszeitenDtos[i]
						.getMaschinengruppe();
				data[i][LOSZEITEN_ARBEITSGANG] = aLoszeitenDtos[i]
						.getiArbeitsgang();
				data[i][LOSZEITEN_UNTERARBEITSGANG] = aLoszeitenDtos[i]
						.getiUnterarbeitsgang();
				data[i][LOSZEITEN_BEZEICHNUNG] = aLoszeitenDtos[i]
						.getSArtikelbezeichnung();
				data[i][LOSZEITEN_ZUSATZBEZEICHNUNG] = aLoszeitenDtos[i]
						.getSArtikelzusatzbezeichnung();
				data[i][LOSZEITEN_DAUER] = aLoszeitenDtos[i].getDdDauer();
				data[i][LOSZEITEN_BEGINN] = aLoszeitenDtos[i].getTsBeginn();
				data[i][LOSZEITEN_ENDE] = aLoszeitenDtos[i].getTsEnde();

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

						String s1 = Helper.fitString2LengthAlignRight(
								iArbeitsgang + "", 5, '0')
								+ Helper.fitString2LengthAlignRight(
										iUnterArbeitsgang + "", 5, '0');
						String s2 = Helper.fitString2LengthAlignRight(
								iArbeitsgang2 + "", 5, '0')
								+ Helper.fitString2LengthAlignRight(
										iUnterArbeitsgang2 + "", 5, '0');

						if (s1.compareTo(s2) > 0) {
							data[j] = a2;
							data[j + 1] = a1;
						}
					}
				}
			}

			mapParameter.put("P_ANGELEGT", new java.util.Date(losDto
					.getTAnlegen().getTime()));
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
				AuftragDto auftragDto = getAuftragFac()
						.auftragFindByPrimaryKey(losDto.getAuftragIId());
				sAuftragsnummer = auftragDto.getCNr();
				sLieferart = getLocaleFac().lieferartFindByIIdLocaleOhneExc(
						auftragDto.getLieferartIId(), theClientDto.getLocUi(),
						theClientDto);

				if (auftragDto.getSpediteurIId() != null) {
					sSpediteur = getMandantFac().spediteurFindByPrimaryKey(
							auftragDto.getSpediteurIId())
							.getCNamedesspediteurs();
				}

				bPoenale = Helper.short2Boolean(auftragDto.getBPoenale());
				bRoHs = Helper.short2Boolean(auftragDto.getBRoHs());
				// Internen Kommentar aus dem Auftrag, abhaengig von einem
				// Mandantenparameter
				ParametermandantDto parameter = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_FERTIGUNG,
								ParameterFac.PARAMETER_KOMMENTAR_AM_FERTIGUNGSBEGLEITSCHEIN);
				short iValue = Short.parseShort(parameter.getCWert());
				boolean bDruckeKommentar = Helper.short2boolean(iValue);
				if (bDruckeKommentar) {
					sInternerKommentar = auftragDto.getXInternerkommentar();
				}
				// Kunde holen
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						auftragDto.getKundeIIdAuftragsadresse(), theClientDto);

				sKunde = kundeDto.getPartnerDto()
						.getCName1nachnamefirmazeile1();

				KundeDto kundeDtoLieferadresse = getKundeFac()
						.kundeFindByPrimaryKey(
								auftragDto.getKundeIIdLieferadresse(),
								theClientDto);
				sKundeLieferadresse = kundeDtoLieferadresse.getPartnerDto()
						.getCName1nachnamefirmazeile1();

				sAbteilung = kundeDto.getPartnerDto()
						.getCName3vorname2abteilung();

				if (losDto.getAuftragpositionIId() != null) {
					AuftragpositionDto aufposDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(
									losDto.getAuftragpositionIId());
					if (aufposDto.getTUebersteuerbarerLiefertermin() != null) {
						dLiefertermin = aufposDto
								.getTUebersteuerbarerLiefertermin();
					} else {
						dLiefertermin = auftragDto.getDLiefertermin();
					}
				} else {
					dLiefertermin = auftragDto.getDLiefertermin();
				}
			} else {
				sAuftragsnummer = "";

				if (losDto.getKundeIId() != null) {
					KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
							losDto.getKundeIId(), theClientDto);

					sKunde = kundeDto.getPartnerDto()
							.getCName1nachnamefirmazeile1();
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
			KostenstelleDto kstDto = getSystemFac()
					.kostenstelleFindByPrimaryKey(losDto.getKostenstelleIId());
			mapParameter.put("P_KOSTENSTELLENUMMER", kstDto.getCNr());
			mapParameter.put("P_KUNDE", sKunde);
			mapParameter.put("P_KUNDE_LIEFERADRESSE", sKundeLieferadresse);
			mapParameter.put("P_LOSGROESSE", losDto.getNLosgroesse());

			mapParameter.put("P_LOSKLASSEN",
					getLosLosKlassenAlsString(losDto.getIId()));

			mapParameter.put("P_LOSNUMMER", losDto.getCNr());
			mapParameter.put("P_PRODUKTIONSBEGINN",
					losDto.getTProduktionsbeginn());
			mapParameter.put("P_PRODUKTIONSENDE", losDto.getTProduktionsende());
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(losDto.getTProduktionsende());
			mapParameter.put("P_PRODUKTIONSENDE_KW",
					new Integer(gc.get(Calendar.WEEK_OF_YEAR)));

			mapParameter.put("P_LIEFERTERMIN", dLiefertermin);

			// Fertigungsort
			if (losDto.getPartnerIIdFertigungsort() != null) {
				PartnerDto partnerDto = getPartnerFac()
						.partnerFindByPrimaryKey(
								losDto.getPartnerIIdFertigungsort(),
								theClientDto);
				mapParameter.put("P_FERTIGUNGSORT",
						partnerDto.formatTitelAnrede());
			}

			// Erstlos
			mapParameter.put("P_ERSTLOS",
					new Boolean(istErstlos(losDto, theClientDto)));

			ArrayList<Object> images = new ArrayList<Object>();
			String sLosStuecklisteArtikelKommentar = "";
			// Bild einfuegen
			String sMengenEinheit = "";
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stuecklisteDto = getStuecklisteFac()
						.stuecklisteFindByPrimaryKey(
								losDto.getStuecklisteIId(), theClientDto);
				// Einheit
				if (stuecklisteDto.getArtikelDto() != null) {
					if (stuecklisteDto.getArtikelDto().getEinheitCNr() != null) {
						sMengenEinheit = stuecklisteDto.getArtikelDto()
								.getEinheitCNr();
					}
				}

				// Sperren
				String sperren = getArtikelFac().getArtikelsperrenText(
						stuecklisteDto.getArtikelIId());

				mapParameter.put("P_STUECKLISTESPERRSTATUS", sperren);

				// Abmessungen
				GeometrieDto geometrieDto = stuecklisteDto.getArtikelDto()
						.getGeometrieDto();
				if (geometrieDto != null) {
					mapParameter.put(P_ARTIKEL_BREITE,
							geometrieDto.getFBreite());
					mapParameter.put(P_ARTIKEL_HOEHE, geometrieDto.getFHoehe());
					mapParameter.put(P_ARTIKEL_TIEFE, geometrieDto.getFTiefe());
				}
				// Bauform
				VerpackungDto verpackungDto = stuecklisteDto.getArtikelDto()
						.getVerpackungDto();
				if (verpackungDto != null) {
					mapParameter.put("P_ARTIKEL_BAUFORM",
							verpackungDto.getCBauform());
					mapParameter.put("P_ARTIKEL_VERPACKUNGSART",
							verpackungDto.getCVerpackungsart());
				}

				mapParameter.put("P_INDEX", stuecklisteDto.getArtikelDto()
						.getCIndex());
				mapParameter.put("P_REVISION", stuecklisteDto.getArtikelDto()
						.getCRevision());

				ArtikelkommentarDto[] artikelkommentarDto = getArtikelkommentarFac()
						.artikelkommentardruckFindByArtikelIIdBelegartCNr(
								stuecklisteDto.getArtikelIId(),
								LocaleFac.BELEGART_LOS,
								theClientDto.getLocUiAsString(), theClientDto);

				if (artikelkommentarDto != null
						&& artikelkommentarDto.length > 0) {

					for (int j = 0; j < artikelkommentarDto.length; j++) {
						if (artikelkommentarDto[j].getArtikelkommentarsprDto() != null) {
							// Text Kommentar
							if (artikelkommentarDto[j]
									.getDatenformatCNr()
									.trim()
									.indexOf(
											MediaFac.DATENFORMAT_MIMETYPEART_TEXT) != -1) {
								if (sLosStuecklisteArtikelKommentar == "") {
									sLosStuecklisteArtikelKommentar += artikelkommentarDto[j]
											.getArtikelkommentarsprDto()
											.getXKommentar();
								} else {
									sLosStuecklisteArtikelKommentar += "\n"
											+ artikelkommentarDto[j]
													.getArtikelkommentarsprDto()
													.getXKommentar();
								}
							} else if (artikelkommentarDto[j]
									.getDatenformatCNr()
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
			mapParameter.put("P_MENGENEINHEIT", sMengenEinheit);
			mapParameter.put("P_PROJEKT", losDto.getCProjekt());
			mapParameter.put("P_LOSKOMMENTAR", losDto.getCKommentar());
			mapParameter.put("P_LOSLANGTEXT", losDto.getXText());

			FertigungsgruppeDto fertGruppeDto = getStuecklisteFac()
					.fertigungsgruppeFindByPrimaryKey(
							losDto.getFertigungsgruppeIId());
			mapParameter.put("P_FERTIGUNGSGRUPPE", fertGruppeDto.getCBez());

			// PJ18776
			ParametermandantDto parametermandantDto = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FERTIGUNG,
							ParameterFac.PARAMETER_ISTZEITEN_GLEICH_SOLLZEITEN);
			mapParameter.put("P_ISTZEITEN_GLEICH_SOLLZEITEN",
					(java.lang.Boolean) parametermandantDto.getCWertAsObject());

			// Materialliste?
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stkDto = getStuecklisteFac()
						.stuecklisteFindByPrimaryKey(
								losDto.getStuecklisteIId(), theClientDto);

				if (sLosStuecklisteArtikelKommentar != "") {
					mapParameter
							.put("P_STUECKLISTEARTIKELKOMMENTAR",
									Helper.formatStyledTextForJasper(sLosStuecklisteArtikelKommentar));
				}

				mapParameter.put("P_STUECKLISTEBEZEICHNUNG", stkDto
						.getArtikelDto().getArtikelsprDto().getCBez());

				mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG", stkDto
						.getArtikelDto().getArtikelsprDto().getCZbez());
				mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG2", stkDto
						.getArtikelDto().getArtikelsprDto().getCZbez2());

				mapParameter.put("P_STUECKLISTEKURZBEZEICHNUNG", stkDto
						.getArtikelDto().getArtikelsprDto().getCKbez());
				mapParameter.put("P_STUECKLISTEREFERENZNUMMER", stkDto
						.getArtikelDto().getCReferenznr());

				mapParameter.put("P_STUECKLISTENUMMER", stkDto.getArtikelDto()
						.getCNr());

				mapParameter.put("P_STUECKLISTE_GEWICHTKG", stkDto
						.getArtikelDto().getFGewichtkg());

				if (stkDto.getArtikelDto().getVerpackungDto() != null) {
					mapParameter.put("P_STUECKLISTE_BAUFORM", stkDto
							.getArtikelDto().getVerpackungDto().getCBauform());
					mapParameter.put("P_STUECKLISTE_VERPACKUNGSART", stkDto
							.getArtikelDto().getVerpackungDto()
							.getCVerpackungsart());
				}

				if (stkDto.getArtikelDto().getGeometrieDto() != null) {
					mapParameter
							.put("P_STUECKLISTE_BREITETEXT", stkDto
									.getArtikelDto().getGeometrieDto()
									.getCBreitetext());
					mapParameter.put("P_STUECKLISTE_BREITE", stkDto
							.getArtikelDto().getGeometrieDto().getFBreite());
					mapParameter.put("P_STUECKLISTE_HOEHE", stkDto
							.getArtikelDto().getGeometrieDto().getFHoehe());
					mapParameter.put("P_STUECKLISTE_TIEFE", stkDto
							.getArtikelDto().getGeometrieDto().getFTiefe());
				}

				// Stuecklisteneigenschaften
				StuecklisteeigenschaftDto[] stuecklisteeigenschaftDtos = getStuecklisteFac()
						.stuecklisteeigenschaftFindByStuecklisteIId(
								losDto.getStuecklisteIId());
				ArrayList<Object[]> al = new ArrayList<Object[]>();
				for (int j = 0; j < stuecklisteeigenschaftDtos.length; j++) {
					StuecklisteeigenschaftDto dto = stuecklisteeigenschaftDtos[j];

					Object[] o = new Object[2];
					String sStklEigenschaftArt = dto
							.getStuecklisteeigenschaftartDto().getCBez();
					o[0] = sStklEigenschaftArt;
					o[1] = dto.getCBez();
					al.add(o);

					// Index und Materialplatz auch einzeln an Report uebergeben
					if (sStklEigenschaftArt
							.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_INDEX)) {
						mapParameter.put(P_STUECKLISTENEIGENSCHAFT_INDEX,
								dto.getCBez());
					}
					if (sStklEigenschaftArt
							.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_MATERIALPLATZ)) {
						mapParameter.put(
								P_STUECKLISTENEIGENSCHAFT_MATERIALPLATZ,
								dto.getCBez());
					}

				}

				if (stuecklisteeigenschaftDtos.length > 0) {
					String[] fieldnames = new String[] { "F_EIGENSCHAFTART",
							"F_BEZEICHNUNG" };
					Object[][] dataSub = new Object[al.size()][fieldnames.length];
					dataSub = (Object[][]) al.toArray(dataSub);

					mapParameter.put("DATENSUBREPORT", new LPDatenSubreport(
							dataSub, fieldnames));
				}

			} else {
				mapParameter.put("P_STUECKLISTEBEZEICHNUNG",
						losDto.getCProjekt());
				mapParameter.put(
						"P_STUECKLISTENUMMER",
						getTextRespectUISpr("fert.materialliste",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL,
				FertigungReportFac.REPORT_LOSZEITEN, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	private ArrayList getDatenGesamtkalkulation(ArrayList alDaten,
			Integer losIId, int iEbene, BigDecimal mengenfaktor,
			TheClientDto theClientDto) {

		LosDto losDto = null;
		BigDecimal gesamtAbgeliefert = null;
		try {
			losDto = getFertigungFac().losFindByPrimaryKey(losIId);

			gesamtAbgeliefert = getFertigungFac().getErledigteMenge(
					losDto.getIId(), theClientDto);

		} catch (RemoteException e1) {
			throwEJBExceptionLPRespectOld(e1);
		}
		iEbene = iEbene + 1;
		// Zuerst Arbeit andrucken

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQueryArbeitsplan = "FROM FLRLossollarbeitsplan AS s WHERE s.flrlos.i_id="
				+ losIId + "ORDER BY s.flrartikel.c_nr ";

		org.hibernate.Query hqueryArbeitsplan = session
				.createQuery(sQueryArbeitsplan);

		AuftragzeitenDto[] belegzeitenDtos = getZeiterfassungFac()
				.getAllZeitenEinesBeleges(LocaleFac.BELEGART_LOS, losIId, null,
						null, null, null, true, false, false, theClientDto);

		List<?> resultListArbeitsplan = hqueryArbeitsplan.list();
		Iterator<?> resultListIteratorArbeitsplan = resultListArbeitsplan
				.iterator();

		LinkedHashMap<Integer, Object[]> hmArbeitsplanEinesLoses = new LinkedHashMap();

		while (resultListIteratorArbeitsplan.hasNext()) {
			FLRLossollarbeitsplan arbeitsplan = (FLRLossollarbeitsplan) resultListIteratorArbeitsplan
					.next();

			Object[] zeile = new Object[GESAMTKALKULATION_ANZAHL_SPALTEN];
			zeile[GESAMTKALKULATION_ARTIKELNUMMER] = arbeitsplan
					.getFlrartikel().getC_nr();

			ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
					arbeitsplan.getFlrartikel().getI_id(), theClientDto);

			if (aDto.getArtikelsprDto() != null) {
				zeile[GESAMTKALKULATION_ARTIKELBEZEICHNUNG] = aDto
						.getArtikelsprDto().getCBez();
				zeile[GESAMTKALKULATION_ZUSATZBEZEICHNUNG] = aDto
						.getArtikelsprDto().getCZbez();
			}

			zeile[GESAMTKALKULATION_EINHEIT] = arbeitsplan.getFlrartikel()
					.getEinheit_c_nr();
			zeile[GESAMTKALKULATION_SOLLMENGE] = arbeitsplan.getN_gesamtzeit();
			zeile[GESAMTKALKULATION_ISTMENGE] = BigDecimal.ZERO;
			zeile[GESAMTKALKULATION_ISTPREIS] = BigDecimal.ZERO;
			zeile[GESAMTKALKULATION_LOSGROESSE] = arbeitsplan.getFlrlos()
					.getN_losgroesse();
			zeile[GESAMTKALKULATION_EBENE] = new Integer(iEbene);
			zeile[GESAMTKALKULATION_ARBEITSZEIT] = Boolean.TRUE;
			zeile[GESAMTKALKULATION_MENGENFAKTOR] = mengenfaktor;

			if (!hmArbeitsplanEinesLoses.containsKey(arbeitsplan
					.getFlrartikel().getI_id())) {
				hmArbeitsplanEinesLoses.put(arbeitsplan.getFlrartikel()
						.getI_id(), zeile);
			}
		}
		session.close();

		// Nun Zeiten addieren
		for (int i = 0; i < belegzeitenDtos.length; i++) {

			AuftragzeitenDto azDto = belegzeitenDtos[i];

			Object[] zeile = null;

			if (hmArbeitsplanEinesLoses.containsKey(azDto.getArtikelIId())) {

				zeile = hmArbeitsplanEinesLoses.get(azDto.getArtikelIId());

			} else {
				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
						azDto.getArtikelIId(), theClientDto);

				zeile = new Object[GESAMTKALKULATION_ANZAHL_SPALTEN];
				zeile[GESAMTKALKULATION_ARTIKELNUMMER] = aDto.getCNr();

				if (aDto.getArtikelsprDto() != null) {
					zeile[GESAMTKALKULATION_ARTIKELBEZEICHNUNG] = aDto
							.getArtikelsprDto().getCBez();
					zeile[GESAMTKALKULATION_ZUSATZBEZEICHNUNG] = aDto
							.getArtikelsprDto().getCZbez();
				}

				zeile[GESAMTKALKULATION_EINHEIT] = aDto.getEinheitCNr();
				zeile[GESAMTKALKULATION_SOLLMENGE] = BigDecimal.ZERO;
				zeile[GESAMTKALKULATION_ISTMENGE] = BigDecimal.ZERO;
				zeile[GESAMTKALKULATION_ISTPREIS] = BigDecimal.ZERO;
				zeile[GESAMTKALKULATION_LOSGROESSE] = losDto.getNLosgroesse();
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

			bKosten = bKosten.add(new BigDecimal(azDto.getBdKosten()
					.doubleValue()));
			zeile[GESAMTKALKULATION_ARBEITSZEIT_KOSTEN] = bKosten;

			hmArbeitsplanEinesLoses.put(azDto.getArtikelIId(), zeile);

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
				zeile[GESAMTKALKULATION_ISTPREIS] = bKosten.divide(bIst, 4,
						BigDecimal.ROUND_HALF_EVEN);
				zeile[GESAMTKALKULATION_EINSTANDSPREIS] = bKosten.divide(bIst,
						4, BigDecimal.ROUND_HALF_EVEN);
			}

			alDaten.add(zeile);

		}

		Session sessionSub = FLRSessionFactory.getFactory().openSession();
		String sQuerySub = "FROM FLRLossollmaterial AS s WHERE s.flrlos.i_id="
				+ losIId + "ORDER BY s.flrartikel.c_nr, s.t_aendern ";

		org.hibernate.Query hquerySub = sessionSub.createQuery(sQuerySub);
		List<?> resultListSub = hquerySub.list();
		Iterator<?> resultListIteratorSub = resultListSub.iterator();

		while (resultListIteratorSub.hasNext()) {
			FLRLossollmaterial sollmaterial = (FLRLossollmaterial) resultListIteratorSub
					.next();

			Object[] zeile = new Object[GESAMTKALKULATION_ANZAHL_SPALTEN];
			zeile[GESAMTKALKULATION_ARTIKELNUMMER] = sollmaterial
					.getFlrartikel().getC_nr();

			ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
					sollmaterial.getFlrartikel().getI_id(), theClientDto);

			if (aDto.getArtikelsprDto() != null) {
				zeile[GESAMTKALKULATION_ARTIKELBEZEICHNUNG] = aDto
						.getArtikelsprDto().getCBez();
				zeile[GESAMTKALKULATION_ZUSATZBEZEICHNUNG] = aDto
						.getArtikelsprDto().getCZbez();
			}

			zeile[GESAMTKALKULATION_EINHEIT] = sollmaterial.getFlrartikel()
					.getEinheit_c_nr();
			zeile[GESAMTKALKULATION_SOLLMENGE] = sollmaterial.getN_menge();
			zeile[GESAMTKALKULATION_SOLLPREIS] = sollmaterial.getN_sollpreis();
			zeile[GESAMTKALKULATION_ISTMENGE] = BigDecimal.ZERO;
			zeile[GESAMTKALKULATION_ISTPREIS] = BigDecimal.ZERO;
			zeile[GESAMTKALKULATION_LOSGROESSE] = sollmaterial.getFlrlos()
					.getN_losgroesse();
			zeile[GESAMTKALKULATION_EBENE] = new Integer(iEbene);
			zeile[GESAMTKALKULATION_ARBEITSZEIT] = Boolean.FALSE;
			zeile[GESAMTKALKULATION_MENGENFAKTOR] = mengenfaktor;

			boolean bZeileHinzugefuegt = false;

			for (Iterator<?> iter = sollmaterial.getIstmaterialset().iterator(); iter
					.hasNext();) {
				FLRLosistmaterial item = (FLRLosistmaterial) iter.next();
				zeile[GESAMTKALKULATION_ISTMENGE] = item.getN_menge();

				BigDecimal einstandswert = BigDecimal.ZERO;

				List<SeriennrChargennrMitMengeDto> snrs = getLagerFac()
						.getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(
								LocaleFac.BELEGART_LOS, item.getI_id());

				for (int m = 0; m < snrs.size(); m++) {

					try {

						BigDecimal bdEinstandwert = getLagerFac()
								.getEinstandspreis(LocaleFac.BELEGART_LOS,
										item.getI_id(),
										snrs.get(m).getCSeriennrChargennr());
						if (bdEinstandwert != null) {
							einstandswert = einstandswert.add(bdEinstandwert);
						}
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}

				// lt. WH muss hier durch durch ist-Menge anstatt der
				// abliefermenge dividiert werden
				if (item.getN_menge().doubleValue() != 0
						&& mengenfaktor != null) {
					zeile[GESAMTKALKULATION_EINSTANDSPREIS] = new BigDecimal(
							einstandswert.doubleValue()
									/ item.getN_menge().doubleValue()
									* mengenfaktor.doubleValue());
				}

				try {
					LosistmaterialDto istmatDto = getFertigungFac()
							.losistmaterialFindByPrimaryKey(item.getI_id());

					// Ist der Ursprung aus einer Stueckliste

					BigDecimal bdPreis = null;
					if (Helper.short2boolean(istmatDto.getBAbgang())) {

						bdPreis = getLagerFac()
								.getGemittelterGestehungspreisEinerAbgangsposition(
										LocaleFac.BELEGART_LOS,
										istmatDto.getIId());

					} else {
						bdPreis = getLagerFac()
								.getGemittelterEinstandspreisEinerZugangsposition(
										LocaleFac.BELEGART_LOS,
										istmatDto.getIId());

					}

					ArrayList<WarenzugangsreferenzDto> alZu = getLagerFac()
							.getWareneingangsreferenz(
									LocaleFac.BELEGART_LOS,
									item.getI_id(),
									getLagerFac()
											.getAllSeriennrchargennrEinerBelegartpositionOhneChargeneigenschaften(
													LocaleFac.BELEGART_LOS,
													item.getI_id()), false,
									theClientDto);

					for (int i = 0; i < alZu.size(); i++) {

						WarenzugangsreferenzDto wzuDto = alZu.get(i);

						Object[] zeileWarenzugang = zeile.clone();

						zeileWarenzugang[GESAMTKALKULATION_BELEGART_ZUGANG] = wzuDto
								.getBelegart();
						zeileWarenzugang[GESAMTKALKULATION_BELEGNUMMER_ZUGANG] = wzuDto
								.getBelegnummer();

						zeileWarenzugang[GESAMTKALKULATION_VERBRAUCHTE_MENGE] = wzuDto
								.getMenge();
						zeileWarenzugang[GESAMTKALKULATION_ISTMENGE] = wzuDto
								.getMenge();
						zeileWarenzugang[GESAMTKALKULATION_ISTPREIS] = bdPreis;

						alDaten.add(zeileWarenzugang);
						// Wenn Ursprung aus LosAblieferung, dann rekursiv

						if (wzuDto.getBelegart().equals(
								LocaleFac.BELEGART_LOSABLIEFERUNG)) {

							// Losablieferung holen
							LosablieferungDto laDto = getFertigungFac()
									.losablieferungFindByPrimaryKeyOhneExc(
											wzuDto.getBelegartpositionIId(),
											false, theClientDto);

							BigDecimal mngfaktor = null;
							if (laDto != null
									&& wzuDto.getMenge().doubleValue() != 0
									&& item.getN_menge().doubleValue() != 0) {
								mngfaktor = wzuDto.getMenge().divide(
										item.getN_menge(), 4,
										BigDecimal.ROUND_HALF_EVEN);
							}

							alDaten = getDatenGesamtkalkulation(alDaten,
									wzuDto.getBelegartIId(), iEbene, mngfaktor,
									theClientDto);
						}

					}

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				bZeileHinzugefuegt = true;

			}

			if (bZeileHinzugefuegt == false) {
				alDaten.add(zeile);
			}

		}

		return alDaten;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printGesamtkalkulation(Integer losIId,
			TheClientDto theClientDto) {
		try {
			this.useCase = UC_GESAMTKALKULATION;
			LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);

			Map<String, Object> mapParameter = new TreeMap<String, Object>();

			mapParameter.put("P_LOSNUMMER", losDto.getCNr());
			mapParameter.put("P_PRODUKTIONSBEGINN",
					losDto.getTProduktionsbeginn());
			mapParameter.put("P_PRODUKTIONSENDE", losDto.getTProduktionsende());

			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stkDto = getStuecklisteFac()
						.stuecklisteFindByPrimaryKey(
								losDto.getStuecklisteIId(), theClientDto);
				mapParameter.put("P_STUECKLISTENUMMER", stkDto.getArtikelDto()
						.getCNr());
				mapParameter.put("P_STUECKLISTEBEZEICHNUNG", stkDto
						.getArtikelDto().getArtikelsprDto().getCBez());

			}

			String sAuftragsnummer = null;
			String sKunde = null;
			if (losDto.getAuftragIId() != null) {
				// Auftrag holen
				AuftragDto auftragDto = getAuftragFac()
						.auftragFindByPrimaryKey(losDto.getAuftragIId());
				sAuftragsnummer = auftragDto.getCNr();

				// Kunde holen
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						auftragDto.getKundeIIdAuftragsadresse(), theClientDto);

				sKunde = kundeDto.getPartnerDto()
						.getCName1nachnamefirmazeile1();

				if (auftragDto.getProjektIId() != null) {
					mapParameter.put(
							"P_PROJEKTNUMMER",
							getProjektFac().projektFindByPrimaryKey(
									auftragDto.getProjektIId()).getCNr());
				}

			} else {
				sAuftragsnummer = "";

				if (losDto.getKundeIId() != null) {
					KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
							losDto.getKundeIId(), theClientDto);

					sKunde = kundeDto.getPartnerDto()
							.getCName1nachnamefirmazeile1();
				} else {
					sKunde = "";
				}

			}

			mapParameter.put("P_AUFTRAGSNUMMER", sAuftragsnummer);
			mapParameter.put("P_KUNDE", sKunde);

			mapParameter.put("P_PROJEKT", losDto.getCProjekt());
			mapParameter.put("P_KOMMENTAR", losDto.getCKommentar());
			mapParameter.put("P_TEXT", losDto.getXText());

			ArrayList alDaten = new ArrayList();

			alDaten = getDatenGesamtkalkulation(alDaten, losIId, 0,
					BigDecimal.ONE, theClientDto);

			data = new Object[alDaten.size()][GESAMTKALKULATION_ANZAHL_SPALTEN];
			data = (Object[][]) alDaten.toArray(data);

			initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL,
					FertigungReportFac.REPORT_GESAMTALKULATION,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return getReportPrint();

	}

	public JasperPrintLP printNachkalkulation(Integer losIId,
			TheClientDto theClientDto) {

		this.useCase = UC_NACHKALKULATION;

		try {
			int iAnzahlSpalten = 10;
			int iAnzahlZeilen = 50;
			ReportLosnachkalkulationDto mat = getFertigungReportFac()
					.getDataNachkalkulationMaterial(losIId, theClientDto);

			ReportLosnachkalkulationDto[] zeit = getFertigungReportFac()
					.getDataNachkalkulationZeitdaten(losIId, theClientDto);
			data = new Object[iAnzahlZeilen][iAnzahlSpalten];

			int row = 0;
			ReportLosnachkalkulationDto nkZeitSumme = new ReportLosnachkalkulationDto();
			nkZeitSumme.setSBezeichnung(getTextRespectUISpr("lp.summe",
					theClientDto.getMandant(), theClientDto.getLocUi()));

			for (int i = 0; i < zeit.length; i++) {
				data[row][NACHKALKULATION_ARTIKELNUMMER] = zeit[i]
						.getSArtikelnummer();
				data[row][NACHKALKULATION_ARTIKELBEZEICHNUNG] = zeit[i]
						.getSBezeichnung();
				data[row][NACHKALKULATION_PERSONALSOLL] = zeit[i]
						.getBdSollmenge();
				data[row][NACHKALKULATION_PERSONALIST] = zeit[i]
						.getBdIstmenge();
				data[row][NACHKALKULATION_PERSONALDIFF] = zeit[i]
						.getBdSollmenge().subtract(zeit[i].getBdIstmenge());
				data[row][NACHKALKULATION_MASCHINESOLL] = zeit[i]
						.getBdSollmengeMaschine();
				data[row][NACHKALKULATION_MASCHINEIST] = zeit[i]
						.getBdIstmengeMaschine();
				data[row][NACHKALKULATION_MASCHINEDIFF] = zeit[i]
						.getBdSollmengeMaschine().subtract(
								zeit[i].getBdIstmengeMaschine());
				data[row][NACHKALKULATION_GESAMTSOLL] = zeit[i]
						.getBdSollpreis();
				data[row][NACHKALKULATION_GESAMTIST] = zeit[i].getBdIstpreis();
				// Summe
				nkZeitSumme.addiereZuSollmenge(zeit[i].getBdSollmenge());
				nkZeitSumme.addiereZuIstmenge(zeit[i].getBdIstmenge());
				nkZeitSumme.addiereZuIstmengeMaschine(zeit[i]
						.getBdIstmengeMaschine());
				nkZeitSumme.addiereZuSollpreis(zeit[i].getBdSollpreis());
				nkZeitSumme.addiereZuIstpreis(zeit[i].getBdIstpreis());
				nkZeitSumme.addiereZuSollmengeMaschine(zeit[i]
						.getBdSollmengeMaschine());
				row++;
			}
			data[row][NACHKALKULATION_ARTIKELBEZEICHNUNG] = nkZeitSumme
					.getSBezeichnung();
			data[row][NACHKALKULATION_PERSONALSOLL] = nkZeitSumme
					.getBdSollmenge();
			data[row][NACHKALKULATION_PERSONALIST] = nkZeitSumme
					.getBdIstmenge();
			data[row][NACHKALKULATION_PERSONALDIFF] = nkZeitSumme
					.getBdSollmenge().subtract(nkZeitSumme.getBdIstmenge());
			data[row][NACHKALKULATION_MASCHINESOLL] = nkZeitSumme
					.getBdSollmengeMaschine();
			data[row][NACHKALKULATION_MASCHINEIST] = nkZeitSumme
					.getBdIstmengeMaschine();
			data[row][NACHKALKULATION_MASCHINEDIFF] = nkZeitSumme
					.getBdSollmengeMaschine().subtract(
							nkZeitSumme.getBdIstmengeMaschine());
			data[row][NACHKALKULATION_GESAMTSOLL] = nkZeitSumme
					.getBdSollpreis();
			data[row][NACHKALKULATION_GESAMTIST] = nkZeitSumme.getBdIstpreis();
			row++;
			row++;
			ReportLosnachkalkulationDto nkGesamtSumme = new ReportLosnachkalkulationDto();
			nkGesamtSumme.setSBezeichnung(getTextRespectUISpr("lp.gesamtsumme",
					theClientDto.getMandant(), theClientDto.getLocUi()));
			nkGesamtSumme.addiereZuSollpreis(nkZeitSumme.getBdSollpreis());
			nkGesamtSumme.addiereZuIstpreis(nkZeitSumme.getBdIstpreis());
			nkGesamtSumme.addiereZuSollpreis(mat.getBdSollpreis());
			nkGesamtSumme.addiereZuIstpreis(mat.getBdIstpreis());
			data[row][NACHKALKULATION_ARTIKELBEZEICHNUNG] = mat
					.getSBezeichnung();
			data[row][NACHKALKULATION_PERSONALSOLL] = mat.getBdSollmenge();
			data[row][NACHKALKULATION_PERSONALIST] = mat.getBdIstmenge();
			data[row][NACHKALKULATION_PERSONALDIFF] = mat.getBdSollmenge()
					.subtract(mat.getBdIstmenge());
			data[row][NACHKALKULATION_MASCHINESOLL] = mat
					.getBdSollmengeMaschine();
			data[row][NACHKALKULATION_MASCHINEIST] = mat
					.getBdIstmengeMaschine();
			data[row][NACHKALKULATION_MASCHINEDIFF] = mat
					.getBdSollmengeMaschine().subtract(
							mat.getBdIstmengeMaschine());
			data[row][NACHKALKULATION_GESAMTSOLL] = mat.getBdSollpreis();
			data[row][NACHKALKULATION_GESAMTIST] = mat.getBdIstpreis();
			row++;
			row++;
			data[row][NACHKALKULATION_ARTIKELBEZEICHNUNG] = nkGesamtSumme
					.getSBezeichnung();
			data[row][NACHKALKULATION_PERSONALSOLL] = nkGesamtSumme
					.getBdSollmenge();
			data[row][NACHKALKULATION_PERSONALIST] = nkGesamtSumme
					.getBdIstmenge();
			data[row][NACHKALKULATION_PERSONALDIFF] = nkGesamtSumme
					.getBdSollmengeMaschine().subtract(
							nkGesamtSumme.getBdIstmengeMaschine());
			data[row][NACHKALKULATION_MASCHINESOLL] = nkGesamtSumme
					.getBdSollmengeMaschine();
			data[row][NACHKALKULATION_MASCHINEIST] = nkGesamtSumme
					.getBdIstmengeMaschine();
			data[row][NACHKALKULATION_MASCHINEDIFF] = nkGesamtSumme
					.getBdSollmenge().subtract(nkGesamtSumme.getBdIstmenge());
			data[row][NACHKALKULATION_GESAMTSOLL] = nkGesamtSumme
					.getBdSollpreis();
			data[row][NACHKALKULATION_GESAMTIST] = nkGesamtSumme
					.getBdIstpreis();
			row++;

			LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);

			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			mapParameter.put("P_ANGELEGT", new java.util.Date(losDto
					.getTAnlegen().getTime()));
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
				AuftragDto auftragDto = getAuftragFac()
						.auftragFindByPrimaryKey(losDto.getAuftragIId());
				kundeIId = auftragDto.getKundeIIdAuftragsadresse();
				sAuftragsnummer = auftragDto.getCNr();
				sLieferart = getLocaleFac().lieferartFindByIIdLocaleOhneExc(
						auftragDto.getLieferartIId(), theClientDto.getLocUi(),
						theClientDto);

				if (auftragDto.getSpediteurIId() != null) {
					sSpediteur = getMandantFac().spediteurFindByPrimaryKey(
							auftragDto.getSpediteurIId())
							.getCNamedesspediteurs();
				}

				bPoenale = Helper.short2Boolean(auftragDto.getBPoenale());
				bRoHs = Helper.short2Boolean(auftragDto.getBRoHs());
				// Internen Kommentar aus dem Auftrag, abhaengig von einem
				// Mandantenparameter
				ParametermandantDto parameter = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_FERTIGUNG,
								ParameterFac.PARAMETER_KOMMENTAR_AM_FERTIGUNGSBEGLEITSCHEIN);
				short iValue = Short.parseShort(parameter.getCWert());
				boolean bDruckeKommentar = Helper.short2boolean(iValue);
				if (bDruckeKommentar) {
					sInternerKommentar = auftragDto.getXInternerkommentar();
				}
				// Kunde holen
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						auftragDto.getKundeIIdAuftragsadresse(), theClientDto);

				sKunde = kundeDto.getPartnerDto()
						.getCName1nachnamefirmazeile1();

				KundeDto kundeDtoLieferadresse = getKundeFac()
						.kundeFindByPrimaryKey(
								auftragDto.getKundeIIdLieferadresse(),
								theClientDto);
				sKundeLieferadresse = kundeDtoLieferadresse.getPartnerDto()
						.getCName1nachnamefirmazeile1();

				sAbteilung = kundeDto.getPartnerDto()
						.getCName3vorname2abteilung();

				if (losDto.getAuftragpositionIId() != null) {
					AuftragpositionDto aufposDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(
									losDto.getAuftragpositionIId());
					if (aufposDto.getTUebersteuerbarerLiefertermin() != null) {
						dLiefertermin = aufposDto
								.getTUebersteuerbarerLiefertermin();
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
						.auftragpositionFindByPrimaryKey(
								losDto.getAuftragpositionIId());
				vkPreisAuftragsposition = auftragpositionDto
						.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte();
			}

			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stkDto = getStuecklisteFac()
						.stuecklisteFindByPrimaryKey(
								losDto.getStuecklisteIId(), theClientDto);

				VkPreisfindungEinzelverkaufspreisDto vkpreisDto = getVkPreisfindungFac()
						.getArtikeleinzelverkaufspreis(stkDto.getArtikelIId(),
								losDto.getTProduktionsende(),
								theClientDto.getSMandantenwaehrung(),
								theClientDto);
				if (vkpreisDto != null
						&& vkpreisDto.getNVerkaufspreisbasis() != null) {
					vkPreisbasisStueckliste = vkpreisDto
							.getNVerkaufspreisbasis();
				}

				if (kundeIId != null) {

					KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
							kundeIId, theClientDto);

					MwstsatzDto mwstsatzDtoAktuell = getMandantFac()
							.mwstsatzFindByMwstsatzbezIIdAktuellster(
									kundeDto.getMwstsatzbezIId(), theClientDto);

					VkpreisfindungDto vkpreisfindungDto = getVkPreisfindungFac()
							.verkaufspreisfindung(
									stkDto.getArtikelIId(),
									kundeIId,
									losDto.getNLosgroesse(),
									losDto.getTProduktionsende(),
									kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
									mwstsatzDtoAktuell.getIId(),
									theClientDto.getSMandantenwaehrung(),
									theClientDto);

					VerkaufspreisDto kundenVKPreisDto = Helper
							.getVkpreisBerechnet(vkpreisfindungDto);
					if (kundenVKPreisDto != null) {
						vkPreisStueckliste = kundenVKPreisDto.nettopreis;
					}
				}

			}

			mapParameter.put("P_VKPREIS_AUFTRAGSPOSITION",
					vkPreisAuftragsposition);
			mapParameter.put("P_VKPREISBASIS_STUECKLISTE",
					vkPreisbasisStueckliste);
			mapParameter.put("P_VKPREIS_STUECKLISTE", vkPreisStueckliste);

			mapParameter.put("P_AUFTRAGNUMMER", sAuftragsnummer);
			mapParameter.put("P_AUFTRAG_INTERNERKOMMENTAR", sInternerKommentar);
			mapParameter.put("P_AUFTRAG_KUNDE_ABTEILUNG", sAbteilung);
			mapParameter.put("P_LIEFERART", sLieferart);
			mapParameter.put("P_SPEDITEUR", sSpediteur);
			mapParameter.put("P_POENALE", bPoenale);
			mapParameter.put("P_ROHS", bRoHs);
			KostenstelleDto kstDto = getSystemFac()
					.kostenstelleFindByPrimaryKey(losDto.getKostenstelleIId());
			mapParameter.put("P_KOSTENSTELLENUMMER", kstDto.getCNr());
			mapParameter.put("P_KUNDE", sKunde);
			mapParameter.put("P_KUNDE_LIEFERADRESSE", sKundeLieferadresse);
			mapParameter.put("P_LOSGROESSE", losDto.getNLosgroesse());

			BigDecimal abgeliefert = BigDecimal.ZERO;

			LosablieferungDto[] losablieferungDtos = getFertigungFac()
					.losablieferungFindByLosIId(losDto.getIId(), false,
							theClientDto);
			BigDecimal bdAbgeliefert = new BigDecimal(0.0000);
			for (int j = 0; j < losablieferungDtos.length; j++) {
				abgeliefert = abgeliefert
						.add(losablieferungDtos[j].getNMenge());
			}

			mapParameter.put("P_ABGELIEFERTEMENGE", abgeliefert);

			mapParameter.put("P_LOSNUMMER", losDto.getCNr());
			mapParameter.put("P_PRODUKTIONSBEGINN",
					losDto.getTProduktionsbeginn());
			mapParameter.put("P_PRODUKTIONSENDE", losDto.getTProduktionsende());
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(losDto.getTProduktionsende());
			mapParameter.put("P_PRODUKTIONSENDE_KW",
					new Integer(gc.get(Calendar.WEEK_OF_YEAR)));

			mapParameter.put("P_LIEFERTERMIN", dLiefertermin);

			mapParameter.put("P_PROJEKT", losDto.getCProjekt());
			mapParameter.put("P_LOSKOMMENTAR", losDto.getCKommentar());
			mapParameter.put("P_LOSLANGTEXT", losDto.getXText());

			FertigungsgruppeDto fertGruppeDto = getStuecklisteFac()
					.fertigungsgruppeFindByPrimaryKey(
							losDto.getFertigungsgruppeIId());
			mapParameter.put("P_FERTIGUNGSGRUPPE", fertGruppeDto.getCBez());

			// Materialliste?
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stkDto = getStuecklisteFac()
						.stuecklisteFindByPrimaryKey(
								losDto.getStuecklisteIId(), theClientDto);

				mapParameter.put("P_MENGENEINHEIT", stkDto.getArtikelDto()
						.getEinheitCNr());
				mapParameter.put("P_MINDESTDECKUNGSBEITRAG", stkDto
						.getArtikelDto().getFMindestdeckungsbeitrag());
				mapParameter.put("P_STUECKLISTEBEZEICHNUNG", stkDto
						.getArtikelDto().getArtikelsprDto().getCBez());

				mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG", stkDto
						.getArtikelDto().getArtikelsprDto().getCZbez());
				mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG2", stkDto
						.getArtikelDto().getArtikelsprDto().getCZbez2());

				mapParameter.put("P_STUECKLISTEKURZBEZEICHNUNG", stkDto
						.getArtikelDto().getArtikelsprDto().getCKbez());
				mapParameter.put("P_STUECKLISTEREFERENZNUMMER", stkDto
						.getArtikelDto().getCReferenznr());

				mapParameter.put("P_STUECKLISTENUMMER", stkDto.getArtikelDto()
						.getCNr());

				mapParameter.put("P_STUECKLISTE_GEWICHTKG", stkDto
						.getArtikelDto().getFGewichtkg());

				if (stkDto.getArtikelDto().getVerpackungDto() != null) {
					mapParameter.put("P_STUECKLISTE_BAUFORM", stkDto
							.getArtikelDto().getVerpackungDto().getCBauform());
					mapParameter.put("P_STUECKLISTE_VERPACKUNGSART", stkDto
							.getArtikelDto().getVerpackungDto()
							.getCVerpackungsart());
				}

				if (stkDto.getArtikelDto().getGeometrieDto() != null) {
					mapParameter
							.put("P_STUECKLISTE_BREITETEXT", stkDto
									.getArtikelDto().getGeometrieDto()
									.getCBreitetext());
					mapParameter.put("P_STUECKLISTE_BREITE", stkDto
							.getArtikelDto().getGeometrieDto().getFBreite());
					mapParameter.put("P_STUECKLISTE_HOEHE", stkDto
							.getArtikelDto().getGeometrieDto().getFHoehe());
					mapParameter.put("P_STUECKLISTE_TIEFE", stkDto
							.getArtikelDto().getGeometrieDto().getFTiefe());
				}

				// Stuecklisteneigenschaften
				StuecklisteeigenschaftDto[] stuecklisteeigenschaftDtos = getStuecklisteFac()
						.stuecklisteeigenschaftFindByStuecklisteIId(
								losDto.getStuecklisteIId());
				ArrayList<Object[]> al = new ArrayList<Object[]>();
				for (int j = 0; j < stuecklisteeigenschaftDtos.length; j++) {
					StuecklisteeigenschaftDto dto = stuecklisteeigenschaftDtos[j];

					Object[] o = new Object[2];
					String sStklEigenschaftArt = dto
							.getStuecklisteeigenschaftartDto().getCBez();
					o[0] = sStklEigenschaftArt;
					o[1] = dto.getCBez();
					al.add(o);

					// Index und Materialplatz auch einzeln an Report uebergeben
					if (sStklEigenschaftArt
							.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_INDEX)) {
						mapParameter.put(P_STUECKLISTENEIGENSCHAFT_INDEX,
								dto.getCBez());
					}
					if (sStklEigenschaftArt
							.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_MATERIALPLATZ)) {
						mapParameter.put(
								P_STUECKLISTENEIGENSCHAFT_MATERIALPLATZ,
								dto.getCBez());
					}

				}

				if (stuecklisteeigenschaftDtos.length > 0) {
					String[] fieldnames = new String[] { "F_EIGENSCHAFTART",
							"F_BEZEICHNUNG" };
					Object[][] dataSub = new Object[al.size()][fieldnames.length];
					dataSub = (Object[][]) al.toArray(dataSub);

					mapParameter.put("DATENSUBREPORT", new LPDatenSubreport(
							dataSub, fieldnames));
				}

			} else {
				mapParameter.put("P_STUECKLISTEBEZEICHNUNG",
						losDto.getCProjekt());
				mapParameter.put(
						"P_STUECKLISTENUMMER",
						getTextRespectUISpr("fert.materialliste",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			}

			// PJ17759
			LosgutschlechtDto[] lgsDtos = getFertigungFac()
					.losgutschlechtFindAllFehler(losIId);
			Object[][] oSubData = new Object[lgsDtos.length][12];

			for (int i = 0; i < lgsDtos.length; i++) {
				LossollarbeitsplanDto sollDto = getFertigungFac()
						.lossollarbeitsplanFindByPrimaryKey(
								lgsDtos[i].getLossollarbeitsplanIId());

				oSubData[i][0] = sollDto.getIArbeitsgangnummer();
				oSubData[i][1] = sollDto.getIUnterarbeitsgang();
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(
								sollDto.getArtikelIIdTaetigkeit(), theClientDto);
				oSubData[i][2] = artikelDto.getCNr();
				oSubData[i][3] = artikelDto.formatBezeichnung();

				String person = "";
				Timestamp zeitpunkt = null;
				if (lgsDtos[i].getZeitdatenIId() != null) {
					ZeitdatenDto zDto = getZeiterfassungFac()
							.zeitdatenFindByPrimaryKey(
									lgsDtos[i].getZeitdatenIId(), theClientDto);
					zeitpunkt = zDto.getTZeit();

					PersonalDto pDto = getPersonalFac()
							.personalFindByPrimaryKey(zDto.getPersonalIId(),
									theClientDto);

					person = pDto.formatFixName1Name2();

				} else if (lgsDtos[i].getMaschinenzeitdatenIId() != null) {
					MaschinenzeitdatenDto mzDto = getZeiterfassungFac()
							.maschinenzeitdatenFindByPrimaryKey(
									lgsDtos[i].getMaschinenzeitdatenIId());
					person = getZeiterfassungFac().maschineFindByPrimaryKey(
							mzDto.getMaschineIId()).getCBez();
					zeitpunkt = mzDto.getTVon();
				}

				FehlerDto fDto = getReklamationFac().fehlerFindByPrimaryKey(
						lgsDtos[i].getFehlerIId(), theClientDto);

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

			String[] fieldnames = new String[] { "F_AGNUMMER", "F_UAGNUMMER",
					"F_ARTIKEL", "F_BEZEICHNUNG", "F_FEHLER", "F_FEHLERSPR",
					"F_FEHLER_KOMMENTAR", "F_PERSONMASCHINE", "F_ZEITPUNKT",
					"F_GUT", "F_SCHLECHT", "F_INARBEIT" };

			mapParameter.put("P_SUBREPORT_FEHLER", new LPDatenSubreport(
					oSubData, fieldnames));
			initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL,
					FertigungReportFac.REPORT_NACHKALKULATION,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printFehlerstatistik(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, Integer iSortierung,
			boolean bAlleAnzeigen, TheClientDto theClientDto) {
		this.useCase = UC_FEHLERSTATISTIK;
		Map<String, Object> mapParameter = new TreeMap<String, Object>();

		SessionFactory factory = FLRSessionFactory.getFactory();
		org.hibernate.Session session = factory.openSession();

		String queryString = "SELECT lgs FROM FLRLosgutschlecht lgs LEFT OUTER JOIN lgs.flrfehler fehler LEFT OUTER JOIN lgs.flrlossollarbeitsplan.flrlos.flrstueckliste.flrartikel flra WHERE lgs.flrlossollarbeitsplan.flrlos.status_c_nr<>'"
				+ FertigungFac.STATUS_STORNIERT + "'";

		if (bAlleAnzeigen == false) {
			queryString += " AND ((lgs.flrlossollarbeitsplan.flrlos.t_erledigt>=' "
					+ Helper.formatTimestampWithSlashes(tVon)
					+ "' AND lgs.flrlossollarbeitsplan.flrlos.t_erledigt<' "
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
			sortierung = getTextRespectUISpr("lp.artikelnummer",
					theClientDto.getMandant(), theClientDto.getLocUi());
			queryString += " ORDER BY flra.c_nr ";
		} else if (iSortierung == FertigungReportFac.SORTIERUNG_FEHLERSTATISTIK_FEHLER) {
			sortierung = getTextRespectUISpr("lp.fehler",
					theClientDto.getMandant(), theClientDto.getLocUi());
			queryString += " ORDER BY fehler.c_bez ";
		} else if (iSortierung == FertigungReportFac.SORTIERUNG_FEHLERSTATISTIK_LOSNUMMER) {
			sortierung = getTextRespectUISpr("fert.tab.unten.los.title",
					theClientDto.getMandant(), theClientDto.getLocUi());
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

			zeile[FEHLERSTATISTIK_AG_AGNUMMER] = lgs.getFlrlossollarbeitsplan()
					.getI_arbeitsgangsnummer();
			zeile[FEHLERSTATISTIK_AG_UAGNUMMER] = lgs
					.getFlrlossollarbeitsplan().getI_unterarbeitsgang();
			zeile[FEHLERSTATISTIK_AG_ARTIKELNUMMER] = lgs
					.getFlrlossollarbeitsplan().getFlrartikel().getC_nr();
			zeile[FEHLERSTATISTIK_AG_ARTIKELBEZEICHNUNG] = getArtikelFac()
					.artikelFindByPrimaryKeySmall(
							lgs.getFlrlossollarbeitsplan().getFlrartikel()
									.getI_id(), theClientDto)
					.formatBezeichnung();
			if (lgs.getFlrlossollarbeitsplan().getFlrlos().getT_erledigt() != null) {
				zeile[FEHLERSTATISTIK_ERLEDIGTDATUM] = lgs
						.getFlrlossollarbeitsplan().getFlrlos().getT_erledigt();
			} else {
				zeile[FEHLERSTATISTIK_ERLEDIGTDATUM] = lgs
						.getFlrlossollarbeitsplan().getFlrlos()
						.getT_manuellerledigt();
			}

			if (lgs.getFlrfehler() != null) {
				zeile[FEHLERSTATISTIK_FEHLER] = lgs.getFlrfehler().getC_bez();

				if (lgs.getFlrfehler().getSprset() != null
						&& lgs.getFlrfehler().getSprset().size() > 0) {

					Iterator<?> sprsetIterator = lgs.getFlrfehler().getSprset()
							.iterator();

					while (sprsetIterator.hasNext()) {
						FLRFehlerspr fehlerspr = (FLRFehlerspr) sprsetIterator
								.next();
						if (fehlerspr.getLocale().getC_nr().compareTo(sLocUI) == 0) {
							zeile[FEHLERSTATISTIK_FEHLERSPR] = fehlerspr
									.getC_bez();
							break;
						}
					}

				}

			}
			zeile[FEHLERSTATISTIK_FEHLER_KOMMENTAR] = lgs.getC_kommentar();
			zeile[FEHLERSTATISTIK_GUT] = lgs.getN_gut();
			zeile[FEHLERSTATISTIK_INARBEIT] = lgs.getN_inarbeit();
			zeile[FEHLERSTATISTIK_LOSGROESSE] = lgs.getFlrlossollarbeitsplan()
					.getFlrlos().getN_losgroesse();
			zeile[FEHLERSTATISTIK_LOSNUMMER] = lgs.getFlrlossollarbeitsplan()
					.getFlrlos().getC_nr();
			try {
				zeile[FEHLERSTATISTIK_MENGE_ERLEDIGT] = getFertigungFac()
						.getErledigteMenge(
								lgs.getFlrlossollarbeitsplan().getFlrlos()
										.getI_id(), theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			zeile[FEHLERSTATISTIK_PROJEKT] = lgs.getFlrlossollarbeitsplan()
					.getFlrlos().getC_projekt();
			zeile[FEHLERSTATISTIK_SCHLECHT] = lgs.getN_schlecht();
			if (lgs.getFlrlossollarbeitsplan().getFlrlos().getFlrstueckliste() != null) {
				zeile[FEHLERSTATISTIK_STUECKLISTE] = lgs
						.getFlrlossollarbeitsplan().getFlrlos()
						.getFlrstueckliste().getFlrartikel().getC_nr();
				zeile[FEHLERSTATISTIK_STUECKLISTEBEZEICHNUNG] = getArtikelFac()
						.artikelFindByPrimaryKeySmall(
								lgs.getFlrlossollarbeitsplan().getFlrlos()
										.getFlrstueckliste().getFlrartikel()
										.getI_id(), theClientDto)
						.formatBezeichnung();

			}

			if (lgs.getFlrzeitdaten() != null) {
				zeile[FEHLERSTATISTIK_PERSONAL] = HelperServer
						.formatAdresseEinesFLRPartner(lgs.getFlrzeitdaten()
								.getFlrpersonal().getFlrpartner());
				zeile[FEHLERSTATISTIK_ZEITPUNKT] = lgs.getFlrzeitdaten()
						.getT_zeit();
			}

			alDaten.add(zeile);
		}

		session.close();

		mapParameter.put("P_VON", tVon);
		mapParameter.put("P_BIS", new Timestamp(tBis.getTime() - 3600000));
		mapParameter.put("P_ALLE_LOSE_ANZEIGEN", new Boolean(bAlleAnzeigen));

		data = new Object[alDaten.size()][FEHLERSTATISTIK_SPALTENANZAHL];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL,
				FertigungReportFac.REPORT_FEHLERSTATISTIK,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printLosstatistik(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, Integer losIId, Integer stuecklisteIId,
			Integer auftragIId, boolean bArbeitsplanSortiertNachAG,
			boolean bVerdichtet, java.sql.Timestamp tStichtag,
			TheClientDto theClientDto) {

		this.useCase = UC_LOSSTATISTIK;
		this.index = -1;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		PersonalDto[] personalDtos = null;
		MaschineDto[] maschineDtos = null;
		try {
			personalDtos = getPersonalFac().personalFindByMandantCNr(
					theClientDto.getMandant(), true);
			maschineDtos = getZeiterfassungFac().maschineFindByMandantCNr(
					theClientDto.getMandant());
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

		Map<String, Object> mapParameter = new TreeMap<String, Object>();

		Criteria c = session.createCriteria(FLRLosReport.class);
		c.add(Restrictions.eq(FertigungFac.FLR_LOS_MANDANT_C_NR,
				theClientDto.getMandant()));
		c.add(Restrictions.not(Restrictions
				.in(FertigungFac.FLR_LOS_STATUS_C_NR, new String[] {
						LocaleFac.STATUS_ANGELEGT, LocaleFac.STATUS_STORNIERT })));
		if (stuecklisteIId != null) {
			c.add(Restrictions.eq(FertigungFac.FLR_LOS_STUECKLISTE_I_ID,
					stuecklisteIId));

			StuecklisteDto stuecklisteDto = getStuecklisteFac()
					.stuecklisteFindByPrimaryKey(stuecklisteIId, theClientDto);
			mapParameter.put("P_STUECKLISTE", stuecklisteDto.getArtikelDto()
					.formatArtikelbezeichnung());

		}
		if (losIId != null) {
			c.add(Restrictions.eq("i_id", losIId));
		}
		if (auftragIId != null) {
			c.createAlias("flrauftrag", "a").add(
					Restrictions.eq("a.i_id", auftragIId));

			try {
				AuftragDto auftragDto = getAuftragFac()
						.auftragFindByPrimaryKey(auftragIId);
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						auftragDto.getKundeIIdAuftragsadresse(), theClientDto);
				mapParameter.put("P_AUFTRAG", auftragDto.getCNr());
				mapParameter.put("P_AUFTRAGPROJEKT",
						auftragDto.getCBezProjektbezeichnung());
				mapParameter.put("P_AUFTRAGKUNDE", kundeDto.getPartnerDto()
						.formatAnrede());

				mapParameter.put(
						"P_BEARBEITER",
						getPersonalFac().getPersonRpt(
								auftragDto.getPersonalIIdAnlegen(),
								theClientDto));
				if (auftragDto.getPersonalIIdVertreter() != null) {
					mapParameter.put(
							"P_VERTRETER",
							getPersonalFac().getPersonRpt(
									auftragDto.getPersonalIIdVertreter(),
									theClientDto));
				}

			} catch (RemoteException ex2) {
				throwEJBExceptionLPRespectOld(ex2);
			}

		}
		if (tStichtag != null) {
			mapParameter.put("P_STICHTAG", tStichtag);// new
			// Timestamp(tStichtag.getTime()));
			tStichtag = Helper.cutTimestamp(new Timestamp(
					tStichtag.getTime() + 24 * 3600000));
		}

		if (losIId == null && auftragIId == null) {

			if (tVon != null) {
				c.add(Restrictions.or(Restrictions.ge(
						FertigungFac.FLR_LOSREPORT_T_MANUELLERLEDIGT, tVon),
						Restrictions.ge(FertigungFac.FLR_LOS_T_ERLEDIGT, tVon)));
				mapParameter.put("P_VON", tVon);
			}
			if (tBis != null) {
				c.add(Restrictions.or(Restrictions.lt(
						FertigungFac.FLR_LOSREPORT_T_MANUELLERLEDIGT, tBis),
						Restrictions.lt(FertigungFac.FLR_LOS_T_ERLEDIGT, tBis)));
				mapParameter.put("P_BIS", new Timestamp(
						tBis.getTime() - 3600000));
			}

			if (tStichtag != null) {
				c.add(Restrictions.or(Restrictions.lt(
						FertigungFac.FLR_LOSREPORT_T_ANLEGEN, tStichtag),
						Restrictions.lt(FertigungFac.FLR_LOSREPORT_T_ANLEGEN,
								tStichtag)));
				c.add(Restrictions.or(Restrictions
						.ge(FertigungFac.FLR_LOSREPORT_T_MANUELLERLEDIGT,
								tStichtag), Restrictions.ge(
						FertigungFac.FLR_LOS_T_ERLEDIGT, tStichtag)));
			}

		}
		c.addOrder(Order.asc("c_nr"));

		List<?> results = c.list();
		ArrayList<LosStatistikDto> al = new ArrayList<LosStatistikDto>();

		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			FLRLosReport los = (FLRLosReport) resultListIterator.next();

			try {

				LosDto losDto = getFertigungFac().losFindByPrimaryKey(
						los.getI_id());

				BigDecimal abgeliefert = new BigDecimal(0);
				for (Iterator<?> iter2 = los.getAblieferungset().iterator(); iter2
						.hasNext();) {
					FLRLosablieferung item = (FLRLosablieferung) iter2.next();
					abgeliefert = abgeliefert.add(item.getN_menge());
				}

				// VKpreis
				BigDecimal vkPreis = null;

				if (los.getFlrauftragposition() != null) {
					vkPreis = los
							.getFlrauftragposition()
							.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte();
				} else {

					if (los.getFlrstueckliste() != null) {

						Integer kundeIId = null;

						if (los.getFlrauftrag() != null) {
							kundeIId = los.getFlrauftrag().getFlrkunde()
									.getI_id();
						}
						if (kundeIId == null && los.getFlrkunde() != null) {
							kundeIId = los.getFlrkunde().getI_id();
						}

						if (kundeIId != null) {
							KundeDto kundeDto = getKundeFac()
									.kundeFindByPrimaryKey(kundeIId,
											theClientDto);

							MwstsatzDto mwstsatzDtoAktuell = getMandantFac()
									.mwstsatzFindByMwstsatzbezIIdAktuellster(
											kundeDto.getMwstsatzbezIId(),
											theClientDto);

							VkpreisfindungDto vkpreisfindungDto = getVkPreisfindungFac()
									.verkaufspreisfindung(
											los.getFlrstueckliste()
													.getArtikel_i_id(),
											kundeIId,
											losDto.getNLosgroesse(),
											losDto.getTProduktionsende(),
											kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
											mwstsatzDtoAktuell.getIId(),
											theClientDto
													.getSMandantenwaehrung(),
											theClientDto);

							VerkaufspreisDto kundenVKPreisDto = Helper
									.getVkpreisBerechnet(vkpreisfindungDto);
							if (kundenVKPreisDto != null) {
								vkPreis = kundenVKPreisDto.nettopreis;
							}
						} else {
							VkPreisfindungEinzelverkaufspreisDto vkpreisDto = getVkPreisfindungFac()
									.getArtikeleinzelverkaufspreis(
											los.getFlrstueckliste()
													.getArtikel_i_id(),
											null,
											theClientDto
													.getSMandantenwaehrung(),
											theClientDto);
							if (vkpreisDto != null
									&& vkpreisDto.getNVerkaufspreisbasis() != null) {
								vkPreis = vkpreisDto.getNVerkaufspreisbasis();
							}
						}

					}
				}

				// Zuerst Material
				Session session2 = factory.openSession();
				Criteria cSoll = session
						.createCriteria(FLRLossollmaterial.class);
				cSoll.add(Restrictions.eq(
						FertigungFac.FLR_LOSSOLLMATERIAL_LOS_I_ID,
						los.getI_id()));
				cSoll.createAlias("flrartikel", "a");
				cSoll.addOrder(Order.asc("a.c_nr"));
				List<?> resultsSoll = cSoll.list();
				Iterator<?> resultListIteratorSoll = resultsSoll.iterator();
				while (resultListIteratorSoll.hasNext()) {
					FLRLossollmaterial sollmat = (FLRLossollmaterial) resultListIteratorSoll
							.next();
					if (bVerdichtet == false
							&& sollmat.getIstmaterialset().size() > 0) {
						int i = 0;
						for (Iterator<?> iter = sollmat.getIstmaterialset()
								.iterator(); iter.hasNext();) {
							FLRLosistmaterial item = (FLRLosistmaterial) iter
									.next();
							LosStatistikDto losStatistikDto = new LosStatistikDto(
									losDto);
							losStatistikDto.setArtikelnummer(sollmat
									.getFlrartikel().getC_nr());
							ArtikelDto artikelDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(
											sollmat.getFlrartikel().getI_id(),
											theClientDto);
							losStatistikDto.setArtikelbezeichnung(artikelDto
									.formatBezeichnung());

							Timestamp tsBuchungszeit = null;

							List<SeriennrChargennrMitMengeDto> snrDtos = getLagerFac()
									.getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(
											LocaleFac.BELEGART_LOS,
											item.getI_id());

							for (int k = 0; k < snrDtos.size(); k++) {

								LagerbewegungDto bewDto = getLagerFac()
										.getLetzteintrag(
												LocaleFac.BELEGART_LOS,
												item.getI_id(),
												snrDtos.get(k)
														.getCSeriennrChargennr());
								tsBuchungszeit = bewDto.getTBuchungszeit();
								break;
							}

							losStatistikDto.setBuchungszeit(tsBuchungszeit);

							if (i == 0) {
								losStatistikDto.setSollmenge(sollmat
										.getN_menge());
								losStatistikDto.setSollpreis(sollmat
										.getN_sollpreis());
							} else {
								losStatistikDto.setSollmenge(new BigDecimal(0));
								losStatistikDto.setSollpreis(new BigDecimal(0));
							}

							losStatistikDto.setBMaterial(true);
							losStatistikDto.setIstpreis(getFertigungFac()
									.getAusgegebeneMengePreis(
											sollmat.getI_id(), null,
											theClientDto));
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
								if (tsBuchungszeit == null) {
									al.add(losStatistikDto);
									i++;
								} else {
									if (tsBuchungszeit.before(tStichtag)) {
										al.add(losStatistikDto);
										i++;
									}
								}

							}

						}
					} else {

						LosStatistikDto losStatistikDto = new LosStatistikDto(
								losDto);
						losStatistikDto.setArtikelnummer(sollmat
								.getFlrartikel().getC_nr());
						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(
										sollmat.getFlrartikel().getI_id(),
										theClientDto);
						losStatistikDto.setArtikelbezeichnung(artikelDto
								.formatBezeichnung());
						losStatistikDto.setSollmenge(sollmat.getN_menge());
						losStatistikDto.setSollpreis(sollmat.getN_sollpreis());
						losStatistikDto.setBMaterial(true);
						losStatistikDto.setIstpreis(getFertigungFac()
								.getAusgegebeneMengePreis(sollmat.getI_id(),
										null, theClientDto));
						losStatistikDto.setAbgelieferteMenge(abgeliefert);
						losStatistikDto.setVkpreisStueckliste(vkPreis);

						BigDecimal istmenge = new BigDecimal(0);
						for (Iterator<?> iter = sollmat.getIstmaterialset()
								.iterator(); iter.hasNext();) {
							FLRLosistmaterial item = (FLRLosistmaterial) iter
									.next();

							if (tStichtag != null) {

								Timestamp tsBuchungszeit = null;

								List<SeriennrChargennrMitMengeDto> snrDtos = getLagerFac()
										.getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(
												LocaleFac.BELEGART_LOS,
												item.getI_id());

								for (int k = 0; k < snrDtos.size(); k++) {

									LagerbewegungDto bewDto = getLagerFac()
											.getLetzteintrag(
													LocaleFac.BELEGART_LOS,
													item.getI_id(),
													snrDtos.get(k)
															.getCSeriennrChargennr());
									tsBuchungszeit = bewDto.getTBuchungszeit();
									break;
								}

								if (tsBuchungszeit.after(tStichtag)) {
									continue;
								}

							}

							if (Helper.short2boolean(item.getB_abgang()) == true) {
								istmenge = istmenge.add(item.getN_menge());
							} else {
								istmenge = istmenge.subtract(item.getN_menge());
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
							Integer a1 = lossollarbeitsplanDtos[j]
									.getIArbeitsgangnummer();
							Integer a2 = lossollarbeitsplanDtos[j + 1]
									.getIArbeitsgangnummer();
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
						Integer artikelIId = lossollarbeitsplanDtos[i]
								.getArtikelIIdTaetigkeit();
						BigDecimal sollzeit = lossollarbeitsplanDtos[i]
								.getNGesamtzeit();
						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(artikelIId,
										theClientDto);

						// Schon vorhanden?
						boolean bGefunden = false;
						for (int j = 0; j < hmSoll.size(); j++) {
							LosStatistikDto losStatistikDto = (LosStatistikDto) hmSoll
									.get(j);
							if (losStatistikDto.getArtikelnummer().equals(
									artikelDto.getCNr())) {
								if (lossollarbeitsplanDtos[i].getMaschineIId()
										.equals(losStatistikDto
												.getMaschineIId())) {
									losStatistikDto
											.setSollmenge(losStatistikDto
													.getSollmenge().add(
															sollzeit));
									bGefunden = true;
									hmSoll.set(j, losStatistikDto);
								}
							}
						}
						if (!bGefunden) {
							LosStatistikDto losStatistikMaschineDto = new LosStatistikDto(
									losDto);
							losStatistikMaschineDto.setArtikelnummer(artikelDto
									.getCNr());
							losStatistikMaschineDto
									.setArtikelbezeichnung(artikelDto
											.formatBezeichnung());
							losStatistikMaschineDto.setSollmenge(sollzeit);
							losStatistikMaschineDto.setBMaterial(false);
							losStatistikMaschineDto.setBIstPerson(false);

							losStatistikMaschineDto
									.setAbgelieferteMenge(abgeliefert);
							losStatistikMaschineDto
									.setVkpreisStueckliste(vkPreis);

							losStatistikMaschineDto
									.setSollpreis(getZeiterfassungFac()
											.getMaschinenKostenZumZeitpunkt(
													lossollarbeitsplanDtos[i]
															.getMaschineIId(),
													Helper.cutTimestamp(new Timestamp(
															System.currentTimeMillis()))));
							MaschineDto maschineDto = getZeiterfassungFac()
									.maschineFindByPrimaryKey(
											lossollarbeitsplanDtos[i]
													.getMaschineIId());

							String maschinenname = "M:";
							if (maschineDto.getCIdentifikationsnr() != null) {
								maschinenname += maschineDto
										.getCIdentifikationsnr() + " ";
							}
							maschinenname += maschineDto.getCBez();

							losStatistikMaschineDto
									.setPersonMaschine(maschinenname);
							losStatistikMaschineDto.setMaschineIId(maschineDto
									.getIId());

							hmSoll.add(losStatistikMaschineDto);
						}
					}
				}
				// Dann Personalzeiten
				for (int i = 0; i < lossollarbeitsplanDtos.length; i++) {

					if (!Helper.short2boolean(lossollarbeitsplanDtos[i]
							.getBNurmaschinenzeit())) {

						Integer artikelIId = lossollarbeitsplanDtos[i]
								.getArtikelIIdTaetigkeit();
						BigDecimal sollzeit = lossollarbeitsplanDtos[i]
								.getNGesamtzeit();
						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(artikelIId,
										theClientDto);

						// Schon vorhanden?
						boolean bGefunden = false;
						for (int j = 0; j < hmSoll.size(); j++) {
							LosStatistikDto losStatistikDto = (LosStatistikDto) hmSoll
									.get(j);
							if (losStatistikDto.getArtikelnummer().equals(
									artikelDto.getCNr())) {

								if (losStatistikDto.getMaschineIId() == null) {
									losStatistikDto
											.setSollmenge(losStatistikDto
													.getSollmenge().add(
															sollzeit));
									hmSoll.set(j, losStatistikDto);
									bGefunden = true;
								}

							}

						}

						if (!bGefunden) {
							LosStatistikDto losStatistikDto = new LosStatistikDto(
									losDto);
							losStatistikDto.setArtikelnummer(artikelDto
									.getCNr());
							losStatistikDto.setArtikelbezeichnung(artikelDto
									.formatBezeichnung());
							losStatistikDto.setSollmenge(sollzeit);
							losStatistikDto.setBMaterial(false);
							losStatistikDto.setBIstPerson(true);
							losStatistikDto.setAbgelieferteMenge(abgeliefert);
							losStatistikDto.setVkpreisStueckliste(vkPreis);
							ArtikellieferantDto artikellieferantDto = getArtikelFac()
									.getArtikelEinkaufspreis(
											artikelDto.getIId(),
											new BigDecimal(1),
											theClientDto
													.getSMandantenwaehrung(),
											theClientDto);
							if (artikellieferantDto != null
									&& artikellieferantDto.getLief1Preis() != null) {
								losStatistikDto
										.setSollpreis(artikellieferantDto
												.getLief1Preis());
							} else {
								losStatistikDto.setSollpreis(new BigDecimal(0));
							}
							hmSoll.add(losStatistikDto);

						}
					}
				}
				// Dann Zeiten

				AuftragzeitenDto[] maschinenzeitenDtos = getZeiterfassungFac()
						.getAllMaschinenzeitenEinesBeleges(los.getI_id(), null,
								null, tStichtag, theClientDto);

				AuftragzeitenDto[] personalzeitenDtos = getZeiterfassungFac()
						.getAllZeitenEinesBeleges(LocaleFac.BELEGART_LOS,
								los.getI_id(), null, null, null, tStichtag,
								true, false, theClientDto);

				for (int i = 0; i < personalzeitenDtos.length; i++) {

					boolean bGefunden = false;
					for (int j = 0; j < hmSoll.size(); j++) {
						LosStatistikDto losStatistikDto = (LosStatistikDto) hmSoll
								.get(j);

						if (losStatistikDto.getMaschineIId() == null) {

							if (losStatistikDto.getArtikelnummer().equals(
									personalzeitenDtos[i].getSArtikelcnr())) {
								bGefunden = true;

							}
						}
					}

					if (bGefunden == false) {
						LosStatistikDto losStatistikDto = new LosStatistikDto(
								losDto);
						losStatistikDto.setArtikelnummer(personalzeitenDtos[i]
								.getSArtikelcnr());
						losStatistikDto
								.setArtikelbezeichnung(personalzeitenDtos[i]
										.getSArtikelbezeichnung());
						losStatistikDto.setSollmenge(new BigDecimal(0));
						losStatistikDto.setSollpreis(new BigDecimal(0));
						losStatistikDto.setAbgelieferteMenge(abgeliefert);
						losStatistikDto.setVkpreisStueckliste(vkPreis);
						hmSoll.add(losStatistikDto);

					}
				}

				for (int i = 0; i < maschinenzeitenDtos.length; i++) {
					boolean bGefunden = false;

					if (tStichtag != null
							&& maschinenzeitenDtos[i].getTsEnde() != null
							&& tStichtag.before(maschinenzeitenDtos[i]
									.getTsEnde())) {
						continue;
					}

					for (int j = 0; j < hmSoll.size(); j++) {
						LosStatistikDto losStatistikDto = (LosStatistikDto) hmSoll
								.get(j);

						if (losStatistikDto.getMaschineIId() != null
								&& losStatistikDto.getMaschineIId().equals(
										maschinenzeitenDtos[i]
												.getIPersonalMaschinenId())) {

							if (losStatistikDto.getArtikelnummer().equals(
									maschinenzeitenDtos[i].getSArtikelcnr())) {
								bGefunden = true;

							}
						}
					}

					if (bGefunden == false) {
						LosStatistikDto losStatistikDto = new LosStatistikDto(
								losDto);
						losStatistikDto.setArtikelnummer(maschinenzeitenDtos[i]
								.getSArtikelcnr());
						losStatistikDto
								.setArtikelbezeichnung(maschinenzeitenDtos[i]
										.getSArtikelbezeichnung());
						losStatistikDto.setMaschineIId(maschinenzeitenDtos[i]
								.getIPersonalMaschinenId());
						losStatistikDto.setSollmenge(new BigDecimal(0));
						losStatistikDto.setSollpreis(new BigDecimal(0));
						losStatistikDto.setAbgelieferteMenge(abgeliefert);
						losStatistikDto.setVkpreisStueckliste(vkPreis);

						hmSoll.add(losStatistikDto);

					}

				}

				for (int k = 0; k < hmSoll.size(); k++) {
					LosStatistikDto losStatistikDto = (LosStatistikDto) hmSoll
							.get(k);

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
									if (personalDto.getIId().equals(
											azDto.getIPersonalMaschinenId())
											&& azDto.getSArtikelcnr()
													.equals(losStatistikDto
															.getArtikelnummer())) {
										bdgesamtzeit = bdgesamtzeit
												.add(new BigDecimal(azDto
														.getDdDauer()));
										bdgesamtkosten = bdgesamtkosten
												.add(azDto.getBdKosten());
										person = azDto
												.getSPersonalMaschinenname();
										personalzeitenDtos[j] = null;
									}
								}
							}
							if (bdgesamtzeit.doubleValue() != 0
									|| bdgesamtkosten.doubleValue() != 0) {

								LosStatistikDto losStatistikPersonDto = new LosStatistikDto(
										losDto);
								losStatistikPersonDto.setPersonMaschine(person);
								losStatistikPersonDto.setBMaterial(false);
								losStatistikPersonDto
										.setAbgelieferteMenge(abgeliefert);
								losStatistikPersonDto
										.setVkpreisStueckliste(vkPreis);

								losStatistikPersonDto.setIstmenge(bdgesamtzeit);
								if (bdgesamtzeit.doubleValue() != 0) {
									losStatistikPersonDto
											.setIstpreis(bdgesamtkosten.divide(
													bdgesamtzeit, 4,
													BigDecimal.ROUND_HALF_EVEN));
								} else {
									losStatistikPersonDto
											.setIstpreis(new BigDecimal(0));
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

								if (tStichtag != null
										&& maschinenzeitenDtos[j].getTsEnde() != null
										&& tStichtag
												.before(maschinenzeitenDtos[j]
														.getTsEnde())) {
									continue;
								}

								AuftragzeitenDto azDto = maschinenzeitenDtos[j];
								if (maschineDto.getIId().equals(
										azDto.getIPersonalMaschinenId())
										&& azDto.getSArtikelcnr().equals(
												losStatistikDto
														.getArtikelnummer())) {
									if (losStatistikDto.getMaschineIId()
											.equals(azDto
													.getIPersonalMaschinenId())) {
										bdgesamtzeit = bdgesamtzeit
												.add(new BigDecimal(azDto
														.getDdDauer()));
										bdgesamtkosten = bdgesamtkosten
												.add(azDto.getBdKosten());
									}

								}
							}
							if (bdgesamtzeit.doubleValue() != 0
									|| bdgesamtkosten.doubleValue() != 0) {

								BigDecimal kosten = bdgesamtkosten.divide(
										bdgesamtzeit, 4,
										BigDecimal.ROUND_HALF_EVEN);

								if (losStatistikDto.getIstmenge() != null) {
									losStatistikDto.setIstmenge(losStatistikDto
											.getIstmenge().add(bdgesamtzeit));
								} else {
									losStatistikDto.setIstmenge(bdgesamtzeit);
								}

								if (losStatistikDto.getIstpreis() != null) {
									losStatistikDto.setIstpreis(losStatistikDto
											.getIstpreis().add(kosten));
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
			data[i][LOSSTATISTIK_GRUPPIERUNG] = losStatistikDto.getLosDto()
					.getCNr();

			if (losStatistikDto.getLosDto().getTManuellerledigt() != null) {
				data[i][LOSSTATISTIK_ERLEDIGUNGSDATUM] = losStatistikDto
						.getLosDto().getTManuellerledigt();
			} else {
				data[i][LOSSTATISTIK_ERLEDIGUNGSDATUM] = losStatistikDto
						.getLosDto().getTErledigt();
			}

			data[i][LOSSTATISTIK_BEWERTUNG] = losStatistikDto.getLosDto()
					.getFBewertung();
			data[i][LOSSTATISTIK_GEPLANTESSOLLMATERIAL] = losStatistikDto
					.getLosDto().getNSollmaterial();
			data[i][LOSSTATISTIK_GRUPPIERUNGBEZEICHNUNG] = losStatistikDto
					.getLosDto().getCProjekt();
			data[i][LOSSTATISTIK_GRUPPIERUNGERLEDIGT] = losStatistikDto
					.getLosDto().getTErledigt();
			data[i][LOSSTATISTIK_GRUPPIERUNGAUSGABE] = losStatistikDto
					.getLosDto().getTAusgabe();
			data[i][LOSSTATISTIK_GRUPPIERUNABGELIEFERTEMENGE] = losStatistikDto
					.getAbgelieferteMenge();
			data[i][LOSSTATISTIK_GRUPPIERUNGLOSGROESSE] = losStatistikDto
					.getLosDto().getNLosgroesse();
			data[i][LOSSTATISTIK_GRUPPIERUNVKPREIS] = losStatistikDto
					.getVkpreisStueckliste();
			data[i][LOSSTATISTIK_BUCHUNGSZEIT] = losStatistikDto
					.getBuchungszeit();

			if (losStatistikDto.getLosDto().getStuecklisteIId() != null) {
				data[i][LOSSTATISTIK_GRUPPIERUNGSTKLARTIKEL] = getStuecklisteFac()
						.stuecklisteFindByPrimaryKey(
								losStatistikDto.getLosDto().getStuecklisteIId(),
								theClientDto).getArtikelDto()
						.formatArtikelbezeichnung();
			}

			if (losStatistikDto.getLosDto().getAuftragIId() != null) {
				AuftragDto auftragDto = getAuftragFac()
						.auftragFindByPrimaryKey(
								losStatistikDto.getLosDto().getAuftragIId());
				if (auftragDto.getKundeIIdAuftragsadresse() != null) {
					KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
							auftragDto.getKundeIIdAuftragsadresse(),
							theClientDto);
					data[i][LOSSTATISTIK_GRUPPIERUNGKUNDE] = kundeDto
							.getPartnerDto().formatAnrede();
				}
			}

			data[i][LOSSTATISTIK_ISTMENGE] = losStatistikDto.getIstmenge();
			data[i][LOSSTATISTIK_ISTPREIS] = losStatistikDto.getIstpreis();
			data[i][LOSSTATISTIK_PERSONALMASCHINE] = losStatistikDto
					.getPersonMaschine();
			data[i][LOSSTATISTIK_ISTPERSON] = new Boolean(
					losStatistikDto.isBIstPerson());
			data[i][LOSSTATISTIK_SOLLMENGE] = losStatistikDto.getSollmenge();
			data[i][LOSSTATISTIK_SOLLPREIS] = losStatistikDto.getSollpreis();
			data[i][LOSSTATISTIK_ARTIKELNUMMER] = losStatistikDto
					.getArtikelnummer();
			data[i][LOSSTATISTIK_ARTIKELBEZEICHNUNG] = losStatistikDto
					.getArtikelbezeichnung();
			data[i][LOSSTATISTIK_UNTERGRUPPEMATERIAL] = new Boolean(
					losStatistikDto.isBMaterial());
		}

		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL,
				FertigungReportFac.REPORT_LOSSTATISTIK,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAblieferungsstatistik(java.sql.Date dVon,
			java.sql.Date dBis, Integer artikelIId,
			int iSortierungAblieferungsstatistik,
			boolean bVerdichtetNachArtikel,
			boolean bNurKopfloseanhandStueckliste, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Session session = null;
		try {

			Map<String, Object> mapParameter = new TreeMap<String, Object>();

			this.useCase = UC_ABLIEFERUNGSSTATISTIK;
			this.index = -1;
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();

			String sQuery = "SELECT l,(SELECT COUNT(*) FROM FLRStuecklisteposition s WHERE s.flrartikel=l.flrlos.flrstueckliste.artikel_i_id) FROM FLRLosablieferung l WHERE l.flrlos.mandant_c_nr='"
					+ theClientDto.getMandant() + "'";

			if (dVon != null) {
				dVon = Helper.cutDate(dVon);

				sQuery += " AND l.t_aendern>='"
						+ Helper.formatDateWithSlashes(dVon) + "'";

				mapParameter.put("P_VON", new Timestamp(dVon.getTime()));

			}
			if (dBis != null) {

				sQuery += " AND l.t_aendern<='"
						+ Helper.formatDateWithSlashes(new java.sql.Date(Helper
								.cutTimestamp(
										new Timestamp(
												dBis.getTime() + 24 * 3600000))
								.getTime())) + "'";

				mapParameter.put("P_BIS", new Timestamp(dBis.getTime()));

			}

			if (artikelIId != null) {
				sQuery += " AND l.flrlos.flrstueckliste.artikel_i_id="
						+ artikelIId;

			}

			mapParameter.put("P_VERDICHTET",
					new Boolean(bVerdichtetNachArtikel));

			mapParameter.put("P_NURKOPFLOSE_ANHAND_STUECKLISTE", new Boolean(
					bNurKopfloseanhandStueckliste));

			if (iSortierungAblieferungsstatistik == ABLIEFERSTATISTIK_OPTION_SORTIERUNG_ARTIKEL) {

				sQuery += " ORDER BY l.flrlos.flrstueckliste.flrartikel.c_nr, l.t_aendern";

				mapParameter.put(
						LPReport.P_SORTIERUNG,
						getTextRespectUISpr("artikel.artikelnummerlang",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			} else if (iSortierungAblieferungsstatistik == ABLIEFERSTATISTIK_OPTION_SORTIERUNG_ABLIEFERDATUM) {

				mapParameter.put(
						LPReport.P_SORTIERUNG,
						getTextRespectUISpr("fert.ablieferdatum",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
				sQuery += " ORDER BY l.t_aendern";
			} else if (iSortierungAblieferungsstatistik == ABLIEFERSTATISTIK_OPTION_SORTIERUNG_AUFTRAG) {

				mapParameter
						.put(LPReport.P_SORTIERUNG,
								getTextRespectUISpr(
										"fert.ablieferstatistik.sortierung.auftragsnummer",
										theClientDto.getMandant(),
										theClientDto.getLocUi()));
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

				if (bNurKopfloseanhandStueckliste) {

					if (verwendungsanzahlInstuecklisten != null
							&& verwendungsanzahlInstuecklisten.longValue() > 0) {
						continue;
					}

				}

				if (losab.getFlrlos().getFlrstueckliste() != null) {

					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(
									losab.getFlrlos().getFlrstueckliste()
											.getFlrartikel().getI_id(),
									theClientDto);

					Object[] oZeile = new Object[ABLIEF_ANZAHL_SPALTEN];

					oZeile[ABLIEF_LOSNUMMER] = losab.getFlrlos().getC_nr();
					oZeile[ABLIEF_IDENT] = artikelDto.getCNr();
					oZeile[ABLIEF_BEZEICHNUNG] = artikelDto.getArtikelsprDto()
							.getCBez();
					if (artikelDto.getArtikelsprDto().getCZbez() != null) {
						oZeile[ABLIEF_ZUSATZBEZEICHNUNG] = artikelDto
								.getArtikelsprDto().getCZbez();
					} else {
						oZeile[ABLIEF_ZUSATZBEZEICHNUNG] = "";
					}
					oZeile[ABLIEF_DATUM] = losab.getT_aendern();
					oZeile[ABLIEF_MENGE] = losab.getN_menge();
					oZeile[ABLIEF_ABLIEFERPREIS] = losab.getN_gestehungspreis();
					oZeile[ABLIEF_MATERIALWERT] = losab.getN_materialwert();
					oZeile[ABLIEF_ARBEITSZEITWERT] = losab
							.getN_arbeitszeitwert();
					oZeile[ABLIEF_WERT] = losab.getN_gestehungspreis()
							.multiply(losab.getN_menge());

					// Auftrag
					String auftragsnummer = null;

					if (losab.getFlrlosreport().getFlrauftrag() != null) {
						auftragsnummer = losab.getFlrlosreport()
								.getFlrauftrag().getC_nr();
					} else if (losab.getFlrlosreport().getFlrauftragposition() != null) {
						auftragsnummer = losab.getFlrlosreport()
								.getFlrauftragposition().getFlrauftrag()
								.getC_nr();
					}

					oZeile[ABLIEF_AUFRAGSNUMMER] = auftragsnummer;
					alDaten.add(oZeile);
				}
			}

			// PJ18868
			if (iSortierungAblieferungsstatistik == ABLIEFERSTATISTIK_OPTION_SORTIERUNG_AUFTRAG) {
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

			initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL,
					FertigungReportFac.REPORT_ABLIEFERUNGSSTATISTIK,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
			return getReportPrint();
		} catch (Exception t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, t);
		} finally {
			closeSession(session);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAufloesbareFehlmengen(Integer iSortierung,
			Boolean bNurArtikelMitLagerstand,
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
					.add(Restrictions.eq(FertigungFac.FLR_LOS_MANDANT_C_NR,
							theClientDto.getMandant()));
			// c.add(Restrictions.eq("artikel_i_id",9223));
			List<?> list = c.list();
			int i = 0;
			LinkedList<ReportAufloesbareFehlmengenDto> cData = new LinkedList<ReportAufloesbareFehlmengenDto>();

			HashMap<Integer, String> hmVorhandeneBestelltEintraege = new HashMap<Integer, String>();

			for (Iterator<?> iter = list.iterator(); iter.hasNext(); i++) {
				FLRFehlmenge fm = (FLRFehlmenge) iter.next();

				boolean bDatensatzVerwenden = true;
				// gleich den Lagerstand holen
				BigDecimal bdLagerstand = getLagerFac()
						.getLagerstandAllerLagerEinesMandanten(
								fm.getArtikel_i_id(), theClientDto);

				if (bNurArtikelMitLagerstand.booleanValue()) {
					// vorher Lagerstand pruefen
					if (bdLagerstand.compareTo(new BigDecimal(0)) <= 0) {
						bDatensatzVerwenden = false;
					}
				}

				if (bDatensatzVerwenden) {

					if (bOhneEigengefertigteArtikel == true) {
						StuecklisteDto stklDto = getStuecklisteFac()
								.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
										fm.getArtikel_i_id(), theClientDto);

						if (stklDto != null) {
							if (!Helper.short2boolean(stklDto
									.getBFremdfertigung())) {
								continue;
							}
						}
					}
					ReportAufloesbareFehlmengenDto rep = new ReportAufloesbareFehlmengenDto();
					rep.setBdFehlmenge(fm.getN_menge());
					rep.setBdLagerstand(bdLagerstand);
					rep.setBdBestellt(getArtikelbestelltFac()
							.getAnzahlBestellt(fm.getArtikel_i_id()));
					rep.setBLos(true);
					rep.setBdReserviert(getReservierungFac()
							.getAnzahlReservierungen(fm.getArtikel_i_id(),
									theClientDto));
					rep.setDTermin(fm.getT_liefertermin());
					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(fm.getArtikel_i_id(),
									theClientDto);
					rep.setSArtikelBezeichnung(artikelDto.getArtikelsprDto()
							.getCBez());
					rep.setSArtikelZusatzBezeichnung(artikelDto
							.getArtikelsprDto().getCZbez());
					rep.setSArtikelNummer(artikelDto.getCNr());
					rep.setIArtikelIId(artikelDto.getIId());
					rep.setSEinheit(artikelDto.getEinheitCNr());
					rep.setSBelegnummer("ALO"
							+ fm.getFlrlossollmaterial().getFlrlos().getC_nr());
					rep.setSProjekt(fm.getFlrlossollmaterial().getFlrlos()
							.getC_projekt());
					if (fm.getFlrlossollmaterial().getFlrlos()
							.getFlrstueckliste() != null) {
						ArtikelDto artikelDtoStueckliste = getArtikelFac()
								.artikelFindByPrimaryKeySmall(
										fm.getFlrlossollmaterial().getFlrlos()
												.getFlrstueckliste()
												.getFlrartikel().getI_id(),
										theClientDto);
						rep.setSStuecklisteBezeichnung(artikelDtoStueckliste
								.getArtikelsprDto().getCBez());
						rep.setSStuecklisteZusatzBezeichnung(artikelDtoStueckliste
								.getArtikelsprDto().getCZbez());
						rep.setSStuecklisteNummer(artikelDtoStueckliste
								.getCNr());
					}
					cData.add(rep);

					if (!hmVorhandeneBestelltEintraege.containsKey(fm
							.getArtikel_i_id())) {

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
							rep.setSArtikelBezeichnung(artikelDto
									.getArtikelsprDto().getCBez());
							rep.setSArtikelZusatzBezeichnung(artikelDto
									.getArtikelsprDto().getCZbez());
							rep.setSArtikelNummer(artikelDto.getCNr());
							rep.setIArtikelIId(artikelDto.getIId());
							rep.setSEinheit(artikelDto.getEinheitCNr());
							rep.setBLos(false);

							BestellpositionDto dto = getBestellpositionFac()
									.bestellpositionFindByPrimaryKeyOhneExc(
											(Integer) o[2]);
							if (dto != null) {
								BestellungDto bestellungDto = getBestellungFac()
										.bestellungFindByPrimaryKey(
												dto.getBelegIId());
								if (dto.getTAuftragsbestaetigungstermin() != null) {
									rep.setDTermin(dto
											.getTAuftragsbestaetigungstermin());
								} else if (dto.getTUebersteuerterLiefertermin() != null) {
									rep.setDTermin(dto
											.getTUebersteuerterLiefertermin());
								} else if (bestellungDto.getDLiefertermin() != null) {
									rep.setDTermin(bestellungDto
											.getDLiefertermin());
								}
								rep.setSBelegnummer("BBS"
										+ bestellungDto.getCNr());
								rep.setBdOffen((BigDecimal) o[0]);
								rep.setSABNummer(dto.getCABNummer());

								BSMahnungDto[] dtos = getBSMahnwesenFac()
										.bsmahnungFindByBestellungIId(
												dto.getIId(), theClientDto);
								if (dtos.length > 0) {
									rep.setIMahnstufe(dtos[0].getMahnstufeIId());
								}

							} else {
								rep.setSBelegnummer("BS not found");
							}

							cData.add(rep);

						}
						hmVorhandeneBestelltEintraege.put(fm.getArtikel_i_id(),
								"");
						closeSession(session2);
					}
				}
			}
			Collections.sort(cData, new ComparatorAufloesbareFehlmengen(
					iSortierung.intValue()));
			data = new Object[cData.size()][AFM_SPALTENANZAHL];
			i = 0;
			for (Iterator<ReportAufloesbareFehlmengenDto> iter = cData
					.iterator(); iter.hasNext(); i++) {
				ReportAufloesbareFehlmengenDto rep = (ReportAufloesbareFehlmengenDto) iter
						.next();
				data[i][AFM_ARTIKELBEZEICHNUNG] = rep.getSArtikelBezeichnung();
				data[i][AFM_ARTIKELZUSATZBEZEICHNUNG] = rep
						.getSArtikelZusatzBezeichnung();
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
				data[i][AFM_STUECKLISTEBEZEICHNUNG] = rep
						.getSStuecklisteBezeichnung();
				data[i][AFM_STUECKLISTEZUSATZBEZEICHNUNG] = rep
						.getSStuecklisteZusatzBezeichnung();
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
						if (rafHelper.getIArtikelIId().equals(
								rep.getIArtikelIId())
								&& (rafHelper.getDTermin().before(
										rep.getDTermin()) || rafHelper
										.getDTermin().equals(rep.getDTermin()))) {
							if (rafHelper.getDTermin().equals(rep.getDTermin())) {
								if (i >= y) {
									bdFiktiverLagerstand = bdFiktiverLagerstand
											.subtract(rafHelper
													.getBdFehlmenge() == null ? new BigDecimal(
													0) : rafHelper
													.getBdFehlmenge());
								}
							} else {
								bdFiktiverLagerstand = bdFiktiverLagerstand
										.subtract(rafHelper.getBdFehlmenge() == null ? new BigDecimal(
												0) : rafHelper.getBdFehlmenge());
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
			mapParameter.put("P_SORTIERTNACHARTIKELNUMMER",
					bSortiertNachArtikelnummer);
			mapParameter.put(P_SORTIERUNG, sSortierung);
			String sFilter = null;
			if (bNurArtikelMitLagerstand.booleanValue()) {
				sFilter = "Nur Artikel mit Lagerstand";
			}
			mapParameter.put(P_FILTER, sFilter);

			mapParameter.put("P_OHNEEIGENGEFERTIGTEARTIKEL",
					bOhneEigengefertigteArtikel);

			initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL,
					FertigungReportFac.REPORT_AUFLOESBARE_FEHLMENGEN,
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

	private ArrayList befuelleFehlteillisteMitLossollmaterialDto(int iEbene,
			LosDto losDto, LoslagerentnahmeDto[] lolaeDtos,
			LossollmaterialDto sollmaterialDto, ArrayList alDaten,
			TheClientDto theClientDto) {

		try {
			Object[] oZeile = new Object[FT_ANZAHL_SPALTEN];
			oZeile[FT_AUSGEGEBEN] = getFertigungFac().getAusgegebeneMenge(
					sollmaterialDto.getIId(), null, theClientDto);

			oZeile[FT_SOLLMENGE] = sollmaterialDto.getNMenge();

			ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(
							sollmaterialDto.getArtikelIId(), theClientDto);

			// Lagerstand
			BigDecimal lagerstand = new BigDecimal(0);

			if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet())) {

				for (int i = 0; i < lolaeDtos.length; i++) {
					BigDecimal lagerstandEisneLagers = getLagerFac()
							.getLagerstandOhneExc(
									sollmaterialDto.getArtikelIId(),
									lolaeDtos[i].getLagerIId(), theClientDto);
					if (lagerstandEisneLagers != null) {
						lagerstand = lagerstand.add(lagerstandEisneLagers);
					}
				}
			} else {
				lagerstand = new BigDecimal(99999999);
			}
			oZeile[FT_LAGERSTAND] = lagerstand;

			oZeile[FT_FRUEHESTER_EINTREFFTERMIN] = getFertigungFac()
					.getFruehesterEintrefftermin(artikelDto.getIId(),
							theClientDto);

			String einrueckung = "";
			for (int i = 0; i < iEbene; i++) {
				einrueckung = einrueckung + "   ";
			}

			oZeile[FT_ARTIKELNUMMER] = einrueckung + artikelDto.getCNr();

			if (artikelDto.getArtikelsprDto() != null) {
				oZeile[FT_BEZEICHNUNG] = artikelDto.getArtikelsprDto()
						.getCBez();
			}

			BigDecimal fehlmenge = new BigDecimal(0);

			ArtikelfehlmengeDto fmDto = getFehlmengeFac()
					.artikelfehlmengeFindByBelegartCNrBelegartPositionIIdOhneExc(
							LocaleFac.BELEGART_LOS, sollmaterialDto.getIId());
			if (fmDto != null && fmDto.getNMenge() != null) {
				fehlmenge = fmDto.getNMenge();
			}
			oZeile[FT_FEHLMENGE] = fehlmenge;

			if (iEbene > 0) {
				oZeile[FT_LOSNUMMER] = losDto.getCNr();
				oZeile[FT_LOSGROESSE] = losDto.getNLosgroesse();
				oZeile[FT_LOSSTATUS] = losDto.getStatusCNr();
				oZeile[FT_ABGELIEFERT] = getFertigungFac().getErledigteMenge(
						losDto.getIId(), theClientDto);
				oZeile[FT_EBENE] = new Integer(iEbene);
			}

			// Nachsehen,ob Stueckliste
			StuecklisteDto stklDto = getStuecklisteFac()
					.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
							sollmaterialDto.getArtikelIId(), theClientDto);

			if (stklDto != null) {
				oZeile[FT_STUECKLISTENART] = new Boolean(true);
			} else {
				oZeile[FT_STUECKLISTENART] = new Boolean(false);
			}

			alDaten.add(oZeile);

			if (stklDto != null) {
				// Offene Lose holen
				ArrayList<LosDto> lose = getFertigungFac().getLoseInFertigung(
						sollmaterialDto.getArtikelIId(), theClientDto);
				iEbene++;
				for (int i = 0; i < lose.size(); i++) {
					LossollmaterialDto[] sollmaterialDtos = getFertigungFac()
							.lossollmaterialFindByLosIIdOrderByISort(
									lose.get(i).getIId());

					for (int j = 0; j < sollmaterialDtos.length; j++) {
						alDaten = befuelleFehlteillisteMitLossollmaterialDto(
								iEbene, lose.get(i), lolaeDtos,
								sollmaterialDtos[j], alDaten, theClientDto);
					}
				}

			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return alDaten;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printFehlteile(Integer losIId,
			boolean bNurPositionenMitFehlmengen, TheClientDto theClientDto) {
		this.useCase = UC_FEHLTEILE;

		ArrayList alDaten = new ArrayList();
		try {

			LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);

			LoslagerentnahmeDto[] lolaeDtos = getFertigungFac()
					.loslagerentnahmeFindByLosIId(losIId);

			LossollmaterialDto[] sollmaterialDtos = getFertigungFac()
					.lossollmaterialFindByLosIId(losIId);

			for (int i = 0; i < sollmaterialDtos.length; i++) {
				alDaten = befuelleFehlteillisteMitLossollmaterialDto(0, losDto,
						lolaeDtos, sollmaterialDtos[i], alDaten, theClientDto);
			}

			data = new Object[alDaten.size()][FT_ANZAHL_SPALTEN];
			data = (Object[][]) alDaten.toArray(data);

			Map<String, Object> mapParameter = new TreeMap<String, Object>();

			mapParameter = parameterMitLosinformationenFuellen(losDto,
					mapParameter, theClientDto);

			mapParameter.put("P_NURPOSITIONENMITFEHLMENGEN", new Boolean(
					bNurPositionenMitFehlmengen));

			initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL,
					FertigungReportFac.REPORT_FEHLTEILE,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return getReportPrint();
	}

	private Map parameterMitLosinformationenFuellen(LosDto losDto,
			Map mapParameter, TheClientDto theClientDto) {
		try {
			mapParameter.put("P_ANGELEGT", new java.util.Date(losDto
					.getTAnlegen().getTime()));
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
				AuftragDto auftragDto = getAuftragFac()
						.auftragFindByPrimaryKey(losDto.getAuftragIId());
				sAuftragsnummer = auftragDto.getCNr();
				sLieferart = getLocaleFac().lieferartFindByIIdLocaleOhneExc(
						auftragDto.getLieferartIId(), theClientDto.getLocUi(),
						theClientDto);

				if (auftragDto.getSpediteurIId() != null) {
					sSpediteur = getMandantFac().spediteurFindByPrimaryKey(
							auftragDto.getSpediteurIId())
							.getCNamedesspediteurs();
				}

				bPoenale = Helper.short2Boolean(auftragDto.getBPoenale());
				bRoHs = Helper.short2Boolean(auftragDto.getBRoHs());
				// Internen Kommentar aus dem Auftrag, abhaengig von einem
				// Mandantenparameter
				ParametermandantDto parameter = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_FERTIGUNG,
								ParameterFac.PARAMETER_KOMMENTAR_AM_FERTIGUNGSBEGLEITSCHEIN);
				short iValue = Short.parseShort(parameter.getCWert());
				boolean bDruckeKommentar = Helper.short2boolean(iValue);
				if (bDruckeKommentar) {
					sInternerKommentar = auftragDto.getXInternerkommentar();
				}
				// Kunde holen
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						auftragDto.getKundeIIdAuftragsadresse(), theClientDto);

				sKunde = kundeDto.getPartnerDto()
						.getCName1nachnamefirmazeile1();

				KundeDto kundeDtoLieferadresse = getKundeFac()
						.kundeFindByPrimaryKey(
								auftragDto.getKundeIIdLieferadresse(),
								theClientDto);
				sKundeLieferadresse = kundeDtoLieferadresse.getPartnerDto()
						.getCName1nachnamefirmazeile1();

				sAbteilung = kundeDto.getPartnerDto()
						.getCName3vorname2abteilung();

				if (losDto.getAuftragpositionIId() != null) {
					AuftragpositionDto aufposDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(
									losDto.getAuftragpositionIId());
					if (aufposDto.getTUebersteuerbarerLiefertermin() != null) {
						dLiefertermin = aufposDto
								.getTUebersteuerbarerLiefertermin();
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
			KostenstelleDto kstDto = getSystemFac()
					.kostenstelleFindByPrimaryKey(losDto.getKostenstelleIId());
			mapParameter.put("P_KOSTENSTELLENUMMER", kstDto.getCNr());
			mapParameter.put("P_KUNDE", sKunde);
			mapParameter.put("P_KUNDE_LIEFERADRESSE", sKundeLieferadresse);
			mapParameter.put("P_LOSGROESSE", losDto.getNLosgroesse());

			mapParameter.put("P_LOSNUMMER", losDto.getCNr());
			mapParameter.put("P_PRODUKTIONSBEGINN",
					losDto.getTProduktionsbeginn());
			mapParameter.put("P_PRODUKTIONSENDE", losDto.getTProduktionsende());
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(losDto.getTProduktionsende());
			mapParameter.put("P_PRODUKTIONSENDE_KW",
					new Integer(gc.get(Calendar.WEEK_OF_YEAR)));

			mapParameter.put("P_LIEFERTERMIN", dLiefertermin);

			// Fertigungsort
			if (losDto.getPartnerIIdFertigungsort() != null) {
				PartnerDto partnerDto = getPartnerFac()
						.partnerFindByPrimaryKey(
								losDto.getPartnerIIdFertigungsort(),
								theClientDto);
				mapParameter.put("P_FERTIGUNGSORT",
						partnerDto.formatTitelAnrede());
			}

			// Erstlos
			mapParameter.put("P_ERSTLOS",
					new Boolean(istErstlos(losDto, theClientDto)));

			ArrayList<Object> images = new ArrayList<Object>();
			String sLosStuecklisteArtikelKommentar = "";
			// Bild einfuegen
			String sMengenEinheit = "";
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stuecklisteDto = getStuecklisteFac()
						.stuecklisteFindByPrimaryKey(
								losDto.getStuecklisteIId(), theClientDto);
				// Einheit
				if (stuecklisteDto.getArtikelDto() != null) {
					if (stuecklisteDto.getArtikelDto().getEinheitCNr() != null) {
						sMengenEinheit = stuecklisteDto.getArtikelDto()
								.getEinheitCNr();
					}
				}
				// Abmessungen
				GeometrieDto geometrieDto = stuecklisteDto.getArtikelDto()
						.getGeometrieDto();
				if (geometrieDto != null) {
					mapParameter.put(P_ARTIKEL_BREITE,
							geometrieDto.getFBreite());
					mapParameter.put(P_ARTIKEL_HOEHE, geometrieDto.getFHoehe());
					mapParameter.put(P_ARTIKEL_TIEFE, geometrieDto.getFTiefe());
				}
				// Bauform
				VerpackungDto verpackungDto = stuecklisteDto.getArtikelDto()
						.getVerpackungDto();
				if (verpackungDto != null) {
					mapParameter.put("P_ARTIKEL_BAUFORM",
							verpackungDto.getCBauform());
					mapParameter.put("P_ARTIKEL_VERPACKUNGSART",
							verpackungDto.getCVerpackungsart());
				}

				ArtikelkommentarDto[] artikelkommentarDto = getArtikelkommentarFac()
						.artikelkommentardruckFindByArtikelIIdBelegartCNr(
								stuecklisteDto.getArtikelIId(),
								LocaleFac.BELEGART_LOS,
								theClientDto.getLocUiAsString(), theClientDto);

				if (artikelkommentarDto != null
						&& artikelkommentarDto.length > 0) {

					for (int j = 0; j < artikelkommentarDto.length; j++) {
						if (artikelkommentarDto[j].getArtikelkommentarsprDto() != null) {
							// Text Kommentar
							if (artikelkommentarDto[j]
									.getDatenformatCNr()
									.trim()
									.indexOf(
											MediaFac.DATENFORMAT_MIMETYPEART_TEXT) != -1) {
								if (sLosStuecklisteArtikelKommentar == "") {
									sLosStuecklisteArtikelKommentar += artikelkommentarDto[j]
											.getArtikelkommentarsprDto()
											.getXKommentar();
								} else {
									sLosStuecklisteArtikelKommentar += "\n"
											+ artikelkommentarDto[j]
													.getArtikelkommentarsprDto()
													.getXKommentar();
								}
							} else if (artikelkommentarDto[j]
									.getDatenformatCNr()
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
			mapParameter.put("P_MENGENEINHEIT", sMengenEinheit);
			mapParameter.put("P_PROJEKT", losDto.getCProjekt());
			mapParameter.put("P_LOSKOMMENTAR", losDto.getCKommentar());
			mapParameter.put("P_LOSLANGTEXT", losDto.getXText());

			FertigungsgruppeDto fertGruppeDto = getStuecklisteFac()
					.fertigungsgruppeFindByPrimaryKey(
							losDto.getFertigungsgruppeIId());
			mapParameter.put("P_FERTIGUNGSGRUPPE", fertGruppeDto.getCBez());

			// Materialliste?
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stkDto = getStuecklisteFac()
						.stuecklisteFindByPrimaryKey(
								losDto.getStuecklisteIId(), theClientDto);

				if (sLosStuecklisteArtikelKommentar != "") {
					mapParameter
							.put("P_STUECKLISTEARTIKELKOMMENTAR",
									Helper.formatStyledTextForJasper(sLosStuecklisteArtikelKommentar));
				}

				mapParameter.put("P_STUECKLISTEBEZEICHNUNG", stkDto
						.getArtikelDto().getArtikelsprDto().getCBez());

				mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG", stkDto
						.getArtikelDto().getArtikelsprDto().getCZbez());
				mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG2", stkDto
						.getArtikelDto().getArtikelsprDto().getCZbez2());

				mapParameter.put("P_STUECKLISTEKURZBEZEICHNUNG", stkDto
						.getArtikelDto().getArtikelsprDto().getCKbez());
				mapParameter.put("P_STUECKLISTEREFERENZNUMMER", stkDto
						.getArtikelDto().getCReferenznr());

				mapParameter.put("P_STUECKLISTENUMMER", stkDto.getArtikelDto()
						.getCNr());

				mapParameter.put("P_STUECKLISTE_GEWICHTKG", stkDto
						.getArtikelDto().getFGewichtkg());

				if (stkDto.getArtikelDto().getVerpackungDto() != null) {
					mapParameter.put("P_STUECKLISTE_BAUFORM", stkDto
							.getArtikelDto().getVerpackungDto().getCBauform());
					mapParameter.put("P_STUECKLISTE_VERPACKUNGSART", stkDto
							.getArtikelDto().getVerpackungDto()
							.getCVerpackungsart());
				}

				if (stkDto.getArtikelDto().getGeometrieDto() != null) {
					mapParameter
							.put("P_STUECKLISTE_BREITETEXT", stkDto
									.getArtikelDto().getGeometrieDto()
									.getCBreitetext());
					mapParameter.put("P_STUECKLISTE_BREITE", stkDto
							.getArtikelDto().getGeometrieDto().getFBreite());
					mapParameter.put("P_STUECKLISTE_HOEHE", stkDto
							.getArtikelDto().getGeometrieDto().getFHoehe());
					mapParameter.put("P_STUECKLISTE_TIEFE", stkDto
							.getArtikelDto().getGeometrieDto().getFTiefe());
				}

				// Stuecklisteneigenschaften
				StuecklisteeigenschaftDto[] stuecklisteeigenschaftDtos = getStuecklisteFac()
						.stuecklisteeigenschaftFindByStuecklisteIId(
								losDto.getStuecklisteIId());
				ArrayList<Object[]> al = new ArrayList<Object[]>();
				for (int j = 0; j < stuecklisteeigenschaftDtos.length; j++) {
					StuecklisteeigenschaftDto dto = stuecklisteeigenschaftDtos[j];

					Object[] o = new Object[2];
					String sStklEigenschaftArt = dto
							.getStuecklisteeigenschaftartDto().getCBez();
					o[0] = sStklEigenschaftArt;
					o[1] = dto.getCBez();
					al.add(o);

					// Index und Materialplatz auch einzeln an Report uebergeben
					if (sStklEigenschaftArt
							.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_INDEX)) {
						mapParameter.put(P_STUECKLISTENEIGENSCHAFT_INDEX,
								dto.getCBez());
					}
					if (sStklEigenschaftArt
							.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_MATERIALPLATZ)) {
						mapParameter.put(
								P_STUECKLISTENEIGENSCHAFT_MATERIALPLATZ,
								dto.getCBez());
					}

				}

				if (stuecklisteeigenschaftDtos.length > 0) {
					String[] fieldnames = new String[] { "F_EIGENSCHAFTART",
							"F_BEZEICHNUNG" };
					Object[][] dataSub = new Object[al.size()][fieldnames.length];
					dataSub = (Object[][]) al.toArray(dataSub);

					mapParameter.put("DATENSUBREPORT", new LPDatenSubreport(
							dataSub, fieldnames));
				}

			} else {
				mapParameter.put("P_STUECKLISTEBEZEICHNUNG",
						losDto.getCProjekt());
				mapParameter.put(
						"P_STUECKLISTENUMMER",
						getTextRespectUISpr("fert.materialliste",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return mapParameter;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printFehlmengenAllerLose(
			boolean bNurEigengefertigteArtikel,
			boolean bAlleOhneEigengefertigteArtikel, TheClientDto theClientDto) {
		Session session = null;
		// try {

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
				+ "' ),(SELECT stkl FROM FLRStueckliste stkl WHERE stkl.artikel_i_id=f.flrartikel.i_id AND stkl.mandant_c_nr=f.flrartikel.mandant_c_nr) FROM FLRFehlmenge f WHERE 1=1 ";

		queryString += " ORDER BY f.flrartikel.c_nr, f.flrlossollmaterial.flrlos.t_produktionsbeginn,f.flrlossollmaterial.flrlos.c_nr ";

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

			if (bNurEigengefertigteArtikel == true) {
				if (stkl == null
						|| Helper.short2boolean(stkl.getB_fremdfertigung()) == true) {
					continue;
				}
			}
			if (bAlleOhneEigengefertigteArtikel == true) {
				if (stkl != null
						&& Helper.short2boolean(stkl.getB_fremdfertigung()) == false) {
					continue;
				}
			}

			Object[] zeile = new Object[FMAL_SPALTENANZAHL];

			zeile[FMAL_ARTIKEL_I_ID] = al.getArtikel_i_id();
			zeile[FMAL_BESTELLT] = bestellt;

			if (fiktiverLagerstand.doubleValue() >= 0
					&& letzteArtikelId != null
					&& !letzteArtikelId.equals(al.getFlrartikel().getI_id())) {
				while (alListe.size() > 0
						&& ((Object[]) alListe.get(alListe.size() - 1))[FMAL_ARTIKEL_I_ID]
								.equals(letzteArtikelId)) {
					alListe.remove(alListe.size() - 1);
				}
			}

			if (letzteArtikelId == null
					|| !letzteArtikelId.equals(al.getFlrartikel().getI_id())) {
				// Offenen Bestelungen + Reservierungen
				fiktiverLagerstand = lagerstand;

			}

			if (fiktiverLagerstand == null) {
				fiktiverLagerstand = new BigDecimal(0);
			}

			zeile[FMAL_ARTIKELNUMMER] = al.getFlrartikel().getC_nr();
			zeile[FMAL_ARTIKELBEZEICHNUNG] = artikel_bez;

			try {
				zeile[FMAL_ARTIKELSPERREN] = getArtikelFac()
						.getArtikelsperrenText(al.getFlrartikel().getI_id());
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			zeile[FMAL_RESERVIERT] = reservierungen;
			if (al.getFlrlossollmaterial().getFlrlos().getFlrstueckliste() != null) {
				zeile[FMAL_STUECKLISTENUMMER] = al.getFlrlossollmaterial()
						.getFlrlos().getFlrstueckliste().getFlrartikel()
						.getC_nr();
				zeile[FMAL_STUECKLISTEBEZEICHNUNG] = stkl_bez;
			}

			zeile[FMAL_BELEGNUMMER] = al.getFlrlossollmaterial().getFlrlos()
					.getC_nr();

			if (al.getFlrlossollmaterial().getFlrlos().getFlrauftrag() != null) {
				zeile[FMAL_POENALE_AUS_AUFTRAG] = Helper.short2Boolean(al
						.getFlrlossollmaterial().getFlrlos().getFlrauftrag()
						.getB_poenale());
			}

			zeile[FMAL_BELEGART] = LocaleFac.BELEGART_LOS;

			zeile[FMAL_PRODUKTIONSBEGINN] = al.getFlrlossollmaterial()
					.getFlrlos().getT_produktionsbeginn();
			zeile[FMAL_PRODUKTIONSENDE] = al.getFlrlossollmaterial()
					.getFlrlos().getT_produktionsende();

			zeile[FMAL_FEHLMENGE] = al.getN_menge();

			fiktiverLagerstand = fiktiverLagerstand.subtract(al.getN_menge());

			zeile[FMAL_FIKTIVERLAGERSTAND] = fiktiverLagerstand;

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

			if (artikelIDNext == null
					|| !artikelIDNext
							.equals((Integer) zeile[FMAL_ARTIKEL_I_ID])) {

				Session session2 = FLRSessionFactory.getFactory().openSession();

				String queryStringOb = "SELECT b,(SELECT spr.c_bez FROM FLRArtikellistespr as spr WHERE spr.Id.artikelliste=b.flrartikel.i_id AND spr.Id.locale='"
						+ theClientDto.getLocUiAsString()
						+ "' ) FROM FLRBestellposition b WHERE b.flrbestellung.bestellungstatus_c_nr NOT IN('"
						+ BestellungFac.BESTELLSTATUS_STORNIERT
						+ "','"
						+ BestellungFac.BESTELLSTATUS_ERLEDIGT
						+ "') AND ( b.flrbestellung.bestellungart_c_nr='"
						+ BestellungFac.BESTELLUNGART_FREIE_BESTELLUNG_C_NR
						+ "' OR b.flrbestellung.bestellungart_c_nr='"
						+ BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR
						+ "' ) AND b.flrartikel.i_id="
						+ (Integer) zeile[FMAL_ARTIKEL_I_ID]
						+ " ORDER BY b.flrbestellung.c_nr";

				org.hibernate.Query queryOb = session2
						.createQuery(queryStringOb);

				List<?> resultListOb = queryOb.list();
				Iterator it2 = resultListOb.iterator();
				while (it2.hasNext()) {

					Object[] bp = (Object[]) it2.next();

					String artikel_bez_bestpos = (String) bp[1];

					FLRBestellposition bespos = (FLRBestellposition) bp[0];
					Object[] zeileBestpos = new Object[FMAL_SPALTENANZAHL];

					zeileBestpos[FMAL_BELEGNUMMER] = bespos.getFlrbestellung()
							.getC_nr();
					zeileBestpos[FMAL_ARTIKELNUMMER] = bespos.getFlrartikel()
							.getC_nr();
					zeileBestpos[FMAL_ARTIKELBEZEICHNUNG] = artikel_bez_bestpos;

					if (bespos.getN_offenemenge() == null) {
						zeileBestpos[FMAL_OFFEN] = bespos.getN_menge();
					} else if (bespos.getN_offenemenge().doubleValue() > 0) {
						zeileBestpos[FMAL_OFFEN] = bespos.getN_offenemenge();
					} else {
						continue;
					}

					zeileBestpos[FMAL_MAHNSTUFE] = bespos.getFlrbestellung()
							.getMahnstufe_i_id();

					zeileBestpos[FMAL_ABNUMMER] = bespos.getC_abnummer();

					zeileBestpos[FMAL_ABTERMIN] = bespos
							.getT_auftragsbestaetigungstermin();

					if (bespos.getT_uebersteuerterliefertermin() != null) {
						zeileBestpos[FMAL_TERMIN] = bespos
								.getT_uebersteuerterliefertermin();
					} else {
						zeileBestpos[FMAL_TERMIN] = bespos.getFlrbestellung()
								.getT_liefertermin();
					}

					String lieferant = bespos.getFlrbestellung()
							.getFlrlieferant().getFlrpartner()
							.getC_name1nachnamefirmazeile1();
					if (bespos.getFlrbestellung().getFlrlieferant()
							.getFlrpartner().getC_name2vornamefirmazeile2() != null) {
						lieferant = lieferant
								+ " "
								+ bespos.getFlrbestellung().getFlrlieferant()
										.getFlrpartner()
										.getC_name2vornamefirmazeile2();
					}
					zeileBestpos[FMAL_LIEFERANT] = lieferant;

					zeileBestpos[FMAL_ARTIKEL_I_ID] = (Integer) zeile[FMAL_ARTIKEL_I_ID];
					zeileBestpos[FMAL_BELEGART] = LocaleFac.BELEGART_BESTELLUNG;
					alFertig.add(zeileBestpos);

				}

				session2.close();
			}

			letzteArtikelId = (Integer) zeile[FMAL_ARTIKEL_I_ID];
		}

		Object[][] returnArray = new Object[alFertig.size()][FMAL_SPALTENANZAHL];
		data = (Object[][]) alFertig.toArray(returnArray);

		Map<String, Object> mapParameter = new TreeMap<String, Object>();

		mapParameter.put("P_NUREIGENGEFERTIGTEARTIKEL", new Boolean(
				bNurEigengefertigteArtikel));
		mapParameter.put("P_ALLEOHNEEIGENGEFERTIGTEARTIKEL", new Boolean(
				bAlleOhneEigengefertigteArtikel));

		initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL,
				FertigungReportFac.REPORT_FEHLMENGEN_ALLER_LOSE,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printMaterialliste(Integer losIId,
			TheClientDto theClientDto) {
		this.useCase = UC_MATERIALLISTE;
		this.index = -1;
		try {
			LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);

			Session sessionSub = FLRSessionFactory.getFactory().openSession();
			String sQuerySub = "FROM FLRLossollmaterial AS s WHERE s.flrlos.i_id="
					+ losIId + "ORDER BY s.flrartikel.c_nr, s.t_aendern ";

			org.hibernate.Query hquerySub = sessionSub.createQuery(sQuerySub);
			List<?> resultListSub = hquerySub.list();
			Iterator<?> resultListIteratorSub = resultListSub.iterator();
			int i = 0;
			ArrayList alDaten = new ArrayList();

			while (resultListIteratorSub.hasNext()) {
				FLRLossollmaterial sollmaterial = (FLRLossollmaterial) resultListIteratorSub
						.next();

				ArrayList<WarenzugangsreferenzDto> alWeReferenzen = new ArrayList();

				// SNR-CHNR
				List<SeriennrChargennrMitMengeDto> snrDtosKomplett = new ArrayList<SeriennrChargennrMitMengeDto>();

				BigDecimal bdAusgegeben = new BigDecimal(0);
				for (Iterator<?> iter = sollmaterial.getIstmaterialset()
						.iterator(); iter.hasNext();) {
					FLRLosistmaterial item = (FLRLosistmaterial) iter.next();

					List<SeriennrChargennrMitMengeDto> snrDtos = getLagerFac()
							.getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(
									LocaleFac.BELEGART_LOS, item.getI_id());

					for (int k = 0; k < snrDtos.size(); k++) {
						snrDtosKomplett.add(snrDtos.get(k));
					}

					if (Helper.short2boolean(item.getB_abgang()) == true) {
						bdAusgegeben = bdAusgegeben.add(item.getN_menge());

						ArrayList<WarenzugangsreferenzDto> alZeile = getLagerFac()
								.getWareneingangsreferenz(
										LocaleFac.BELEGART_LOS, item.getI_id(),
										(List) null, false, theClientDto);
						for (int h = 0; h < alZeile.size(); h++) {
							alWeReferenzen.add(alZeile.get(h));
						}
					} else {
						bdAusgegeben = bdAusgegeben.subtract(item.getN_menge());
					}
				}
				Object[] zeile = new Object[MATERIAL_SPALTENANZAHL];

				String[] fieldnames = new String[] { "F_BELEGART",
						"F_BELEGNUMMER", "F_ZUSATZ", "F_BELEGDATUM",
						"F_POSITION1", "F_POSITION2", "F_URSPRUNGSLAND",
						"F_HERSTELLER", "F_MENGE", "F_LOS_AZANTEIL",
						"F_LOS_MATERIALANTEIL" };

				Object[][] dataSub = new Object[alWeReferenzen.size()][fieldnames.length];

				for (int h = 0; h < alWeReferenzen.size(); h++) {
					WarenzugangsreferenzDto dto = (WarenzugangsreferenzDto) alWeReferenzen
							.get(h);
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

				}

				zeile[MATERIAL_WE_REFERENZ] = new LPDatenSubreport(dataSub,
						fieldnames);

				zeile[MATERIAL_SNRCHNR] = SeriennrChargennrMitMengeDto
						.erstelleStringAusMehrerenSeriennummern(snrDtosKomplett);

				BigDecimal preis = new BigDecimal(0);

				if (bdAusgegeben.doubleValue() != 0) {
					try {
						preis = getFertigungFac().getAusgegebeneMengePreis(
								sollmaterial.getI_id(), null, theClientDto);
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}

				if (Helper.short2boolean(sollmaterial.getB_nachtraeglich())) {
					zeile[MATERIAL_ART] = "N";
				} else {
					zeile[MATERIAL_ART] = "S";
				}
				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
						sollmaterial.getFlrartikel().getI_id(), theClientDto);
				zeile[MATERIAL_ARTIKELNUMMER] = aDto.getCNr();

				if (aDto.getArtikelsprDto() != null) {
					zeile[MATERIAL_BEZEICHNUNG] = aDto.getArtikelsprDto()
							.getCBez();
					zeile[MATERIAL_ZUSATZBEZEICHNUNG] = aDto.getArtikelsprDto()
							.getCZbez();
					zeile[MATERIAL_ZUSATZBEZEICHNUNG2] = aDto
							.getArtikelsprDto().getCZbez2();
					zeile[MATERIAL_KURZBEZEICHNUNG] = aDto.getArtikelsprDto()
							.getCKbez();
				}

				zeile[MATERIAL_SOLLMENGE] = sollmaterial.getN_menge();

				zeile[MATERIAL_ISTMENGE] = bdAusgegeben;
				zeile[MATERIAL_SOLLPREIS] = sollmaterial.getN_sollpreis();
				zeile[MATERIAL_ISTPREIS] = preis;

				LossollmaterialDto sollMatDto = getFertigungFac()
						.lossollmaterialFindByPrimaryKey(sollmaterial.getI_id());

				zeile[MATERIAL_EINHEIT] = sollMatDto.getEinheitCNr();

				ArtikellieferantDto dto = getArtikelFac()
						.getArtikelEinkaufspreis(
								sollmaterial.getFlrartikel().getI_id(),
								null,
								sollmaterial.getN_menge(),
								theClientDto.getSMandantenwaehrung(),
								new java.sql.Date(sollMatDto.getTAendern()
										.getTime()), theClientDto);
				if (dto != null) {
					zeile[MATERIAL_LIEF1PREIS] = dto.getLief1Preis();
				}

				alDaten.add(zeile);
				i++;
			}
			sessionSub.close();

			data = new Object[alDaten.size()][MATERIAL_SPALTENANZAHL];
			data = (Object[][]) alDaten.toArray(data);

			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			mapParameter.put("P_ANGELEGT", new java.util.Date(losDto
					.getTAnlegen().getTime()));
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
				AuftragDto auftragDto = getAuftragFac()
						.auftragFindByPrimaryKey(losDto.getAuftragIId());
				sAuftragsnummer = auftragDto.getCNr();
				sLieferart = getLocaleFac().lieferartFindByIIdLocaleOhneExc(
						auftragDto.getLieferartIId(), theClientDto.getLocUi(),
						theClientDto);

				if (auftragDto.getSpediteurIId() != null) {
					sSpediteur = getMandantFac().spediteurFindByPrimaryKey(
							auftragDto.getSpediteurIId())
							.getCNamedesspediteurs();
				}

				bPoenale = Helper.short2Boolean(auftragDto.getBPoenale());
				bRoHs = Helper.short2Boolean(auftragDto.getBRoHs());
				// Internen Kommentar aus dem Auftrag, abhaengig von einem
				// Mandantenparameter
				ParametermandantDto parameter = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_FERTIGUNG,
								ParameterFac.PARAMETER_KOMMENTAR_AM_FERTIGUNGSBEGLEITSCHEIN);
				short iValue = Short.parseShort(parameter.getCWert());
				boolean bDruckeKommentar = Helper.short2boolean(iValue);
				if (bDruckeKommentar) {
					sInternerKommentar = auftragDto.getXInternerkommentar();
				}
				// Kunde holen
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						auftragDto.getKundeIIdAuftragsadresse(), theClientDto);

				sKunde = kundeDto.getPartnerDto()
						.getCName1nachnamefirmazeile1();

				KundeDto kundeDtoLieferadresse = getKundeFac()
						.kundeFindByPrimaryKey(
								auftragDto.getKundeIIdLieferadresse(),
								theClientDto);
				sKundeLieferadresse = kundeDtoLieferadresse.getPartnerDto()
						.getCName1nachnamefirmazeile1();

				sAbteilung = kundeDto.getPartnerDto()
						.getCName3vorname2abteilung();

				if (losDto.getAuftragpositionIId() != null) {
					AuftragpositionDto aufposDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(
									losDto.getAuftragpositionIId());
					if (aufposDto.getTUebersteuerbarerLiefertermin() != null) {
						dLiefertermin = aufposDto
								.getTUebersteuerbarerLiefertermin();
					} else {
						dLiefertermin = auftragDto.getDLiefertermin();
					}
				} else {
					dLiefertermin = auftragDto.getDLiefertermin();
				}
			} else {
				sAuftragsnummer = "";

				if (losDto.getKundeIId() != null) {
					KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
							losDto.getKundeIId(), theClientDto);

					sKunde = kundeDto.getPartnerDto()
							.getCName1nachnamefirmazeile1();
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
			KostenstelleDto kstDto = getSystemFac()
					.kostenstelleFindByPrimaryKey(losDto.getKostenstelleIId());
			mapParameter.put("P_KOSTENSTELLENUMMER", kstDto.getCNr());
			mapParameter.put("P_KUNDE", sKunde);
			mapParameter.put("P_KUNDE_LIEFERADRESSE", sKundeLieferadresse);
			mapParameter.put("P_LOSGROESSE", losDto.getNLosgroesse());

			mapParameter.put("P_LOSKLASSEN",
					getLosLosKlassenAlsString(losDto.getIId()));

			mapParameter.put("P_LOSNUMMER", losDto.getCNr());
			mapParameter.put("P_PRODUKTIONSBEGINN",
					losDto.getTProduktionsbeginn());
			mapParameter.put("P_PRODUKTIONSENDE", losDto.getTProduktionsende());
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(losDto.getTProduktionsende());
			mapParameter.put("P_PRODUKTIONSENDE_KW",
					new Integer(gc.get(Calendar.WEEK_OF_YEAR)));

			mapParameter.put("P_LIEFERTERMIN", dLiefertermin);

			// Fertigungsort
			if (losDto.getPartnerIIdFertigungsort() != null) {
				PartnerDto partnerDto = getPartnerFac()
						.partnerFindByPrimaryKey(
								losDto.getPartnerIIdFertigungsort(),
								theClientDto);
				mapParameter.put("P_FERTIGUNGSORT",
						partnerDto.formatTitelAnrede());
			}

			// Erstlos
			mapParameter.put("P_ERSTLOS",
					new Boolean(istErstlos(losDto, theClientDto)));

			ArrayList<Object> images = new ArrayList<Object>();
			String sLosStuecklisteArtikelKommentar = "";
			// Bild einfuegen
			String sMengenEinheit = "";
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stuecklisteDto = getStuecklisteFac()
						.stuecklisteFindByPrimaryKey(
								losDto.getStuecklisteIId(), theClientDto);
				// Einheit
				if (stuecklisteDto.getArtikelDto() != null) {
					if (stuecklisteDto.getArtikelDto().getEinheitCNr() != null) {
						sMengenEinheit = stuecklisteDto.getArtikelDto()
								.getEinheitCNr();
					}
				}

				// Sperren
				String sperren = getArtikelFac().getArtikelsperrenText(
						stuecklisteDto.getArtikelIId());

				mapParameter.put("P_STUECKLISTESPERRSTATUS", sperren);

				// Abmessungen
				GeometrieDto geometrieDto = stuecklisteDto.getArtikelDto()
						.getGeometrieDto();
				if (geometrieDto != null) {
					mapParameter.put(P_ARTIKEL_BREITE,
							geometrieDto.getFBreite());
					mapParameter.put(P_ARTIKEL_HOEHE, geometrieDto.getFHoehe());
					mapParameter.put(P_ARTIKEL_TIEFE, geometrieDto.getFTiefe());
				}
				// Bauform
				VerpackungDto verpackungDto = stuecklisteDto.getArtikelDto()
						.getVerpackungDto();
				if (verpackungDto != null) {
					mapParameter.put("P_ARTIKEL_BAUFORM",
							verpackungDto.getCBauform());
					mapParameter.put("P_ARTIKEL_VERPACKUNGSART",
							verpackungDto.getCVerpackungsart());
				}

				mapParameter.put("P_INDEX", stuecklisteDto.getArtikelDto()
						.getCIndex());
				mapParameter.put("P_REVISION", stuecklisteDto.getArtikelDto()
						.getCRevision());

				ArtikelkommentarDto[] artikelkommentarDto = getArtikelkommentarFac()
						.artikelkommentardruckFindByArtikelIIdBelegartCNr(
								stuecklisteDto.getArtikelIId(),
								LocaleFac.BELEGART_LOS,
								theClientDto.getLocUiAsString(), theClientDto);

				if (artikelkommentarDto != null
						&& artikelkommentarDto.length > 0) {

					for (int j = 0; j < artikelkommentarDto.length; j++) {
						if (artikelkommentarDto[j].getArtikelkommentarsprDto() != null) {
							// Text Kommentar
							if (artikelkommentarDto[j]
									.getDatenformatCNr()
									.trim()
									.indexOf(
											MediaFac.DATENFORMAT_MIMETYPEART_TEXT) != -1) {
								if (sLosStuecklisteArtikelKommentar == "") {
									sLosStuecklisteArtikelKommentar += artikelkommentarDto[j]
											.getArtikelkommentarsprDto()
											.getXKommentar();
								} else {
									sLosStuecklisteArtikelKommentar += "\n"
											+ artikelkommentarDto[j]
													.getArtikelkommentarsprDto()
													.getXKommentar();
								}
							} else if (artikelkommentarDto[j]
									.getDatenformatCNr()
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
			mapParameter.put("P_MENGENEINHEIT", sMengenEinheit);
			mapParameter.put("P_PROJEKT", losDto.getCProjekt());
			mapParameter.put("P_LOSKOMMENTAR", losDto.getCKommentar());
			mapParameter.put("P_LOSLANGTEXT", losDto.getXText());

			FertigungsgruppeDto fertGruppeDto = getStuecklisteFac()
					.fertigungsgruppeFindByPrimaryKey(
							losDto.getFertigungsgruppeIId());
			mapParameter.put("P_FERTIGUNGSGRUPPE", fertGruppeDto.getCBez());

			// Materialliste?
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stkDto = getStuecklisteFac()
						.stuecklisteFindByPrimaryKey(
								losDto.getStuecklisteIId(), theClientDto);

				if (sLosStuecklisteArtikelKommentar != "") {
					mapParameter
							.put("P_STUECKLISTEARTIKELKOMMENTAR",
									Helper.formatStyledTextForJasper(sLosStuecklisteArtikelKommentar));
				}

				mapParameter.put("P_STUECKLISTEBEZEICHNUNG", stkDto
						.getArtikelDto().getArtikelsprDto().getCBez());

				mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG", stkDto
						.getArtikelDto().getArtikelsprDto().getCZbez());
				mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG2", stkDto
						.getArtikelDto().getArtikelsprDto().getCZbez2());

				mapParameter.put("P_STUECKLISTEKURZBEZEICHNUNG", stkDto
						.getArtikelDto().getArtikelsprDto().getCKbez());
				mapParameter.put("P_STUECKLISTEREFERENZNUMMER", stkDto
						.getArtikelDto().getCReferenznr());

				mapParameter.put("P_STUECKLISTENUMMER", stkDto.getArtikelDto()
						.getCNr());

				mapParameter.put("P_STUECKLISTE_GEWICHTKG", stkDto
						.getArtikelDto().getFGewichtkg());

				if (stkDto.getArtikelDto().getVerpackungDto() != null) {
					mapParameter.put("P_STUECKLISTE_BAUFORM", stkDto
							.getArtikelDto().getVerpackungDto().getCBauform());
					mapParameter.put("P_STUECKLISTE_VERPACKUNGSART", stkDto
							.getArtikelDto().getVerpackungDto()
							.getCVerpackungsart());
				}

				if (stkDto.getArtikelDto().getGeometrieDto() != null) {
					mapParameter
							.put("P_STUECKLISTE_BREITETEXT", stkDto
									.getArtikelDto().getGeometrieDto()
									.getCBreitetext());
					mapParameter.put("P_STUECKLISTE_BREITE", stkDto
							.getArtikelDto().getGeometrieDto().getFBreite());
					mapParameter.put("P_STUECKLISTE_HOEHE", stkDto
							.getArtikelDto().getGeometrieDto().getFHoehe());
					mapParameter.put("P_STUECKLISTE_TIEFE", stkDto
							.getArtikelDto().getGeometrieDto().getFTiefe());
				}

				// Stuecklisteneigenschaften
				StuecklisteeigenschaftDto[] stuecklisteeigenschaftDtos = getStuecklisteFac()
						.stuecklisteeigenschaftFindByStuecklisteIId(
								losDto.getStuecklisteIId());
				ArrayList<Object[]> al = new ArrayList<Object[]>();
				for (int j = 0; j < stuecklisteeigenschaftDtos.length; j++) {
					StuecklisteeigenschaftDto dto = stuecklisteeigenschaftDtos[j];

					Object[] o = new Object[2];
					String sStklEigenschaftArt = dto
							.getStuecklisteeigenschaftartDto().getCBez();
					o[0] = sStklEigenschaftArt;
					o[1] = dto.getCBez();
					al.add(o);

					// Index und Materialplatz auch einzeln an Report uebergeben
					if (sStklEigenschaftArt
							.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_INDEX)) {
						mapParameter.put(P_STUECKLISTENEIGENSCHAFT_INDEX,
								dto.getCBez());
					}
					if (sStklEigenschaftArt
							.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_MATERIALPLATZ)) {
						mapParameter.put(
								P_STUECKLISTENEIGENSCHAFT_MATERIALPLATZ,
								dto.getCBez());
					}

				}

				if (stuecklisteeigenschaftDtos.length > 0) {
					String[] fieldnames = new String[] { "F_EIGENSCHAFTART",
							"F_BEZEICHNUNG" };
					Object[][] dataSub = new Object[al.size()][fieldnames.length];
					dataSub = (Object[][]) al.toArray(dataSub);

					mapParameter.put("DATENSUBREPORT", new LPDatenSubreport(
							dataSub, fieldnames));
				}

			} else {
				mapParameter.put("P_STUECKLISTEBEZEICHNUNG",
						losDto.getCProjekt());
				mapParameter.put(
						"P_STUECKLISTENUMMER",
						getTextRespectUISpr("fert.materialliste",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			}

			initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL,
					FertigungReportFac.REPORT_MATERIALLISTE,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
		} catch (Exception t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, t);

		}

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printStueckrueckmeldung(Integer losIId,
			int iSortierung, TheClientDto theClientDto) throws EJBExceptionLP {
		Session session = null;
		try {
			this.useCase = UC_STUECKRUECKMELDUNG;
			this.index = -1;
			// Los holen
			LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);
			if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN,
						"");
			} else if (losDto.getStatusCNr().equals(
					FertigungFac.STATUS_STORNIERT)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT,
						"");
			}
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRLossollarbeitsplan.class);
			// Nur Buchungen auf dieses Los
			c.add(Restrictions.eq(FertigungFac.FLR_LOSSOLLARBEITSPLAN_LOS_I_ID,
					losIId));

			// Sortierung
			if (iSortierung == Helper.SORTIERUNG_NACH_IDENT) {
				c.createCriteria(FertigungFac.FLR_LOSSOLLARBEITSPLAN_FLRARTIKEL)
						.addOrder(Order.asc(ArtikelFac.FLR_ARTIKEL_C_NR));
			} else if (iSortierung == Helper.SORTIERUNG_NACH_ARBEITSGANG) {
				c.addOrder(Order
						.asc(FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_ARBEITSGANGNUMMER));
			}
			List<?> list = c.list();

			ArrayList alDaten = new ArrayList();
			Iterator<?> iter = list.iterator();
			while (iter.hasNext()) {
				FLRLossollarbeitsplan item = (FLRLossollarbeitsplan) iter
						.next();

				LosgutschlechtDto[] losgutschlechDtos = getFertigungFac()
						.losgutschlechtFindByLossollarbeitsplanIId(
								item.getI_id());

				for (int i = 0; i < losgutschlechDtos.length; i++) {

					Object[] oZeile = new Object[SRM_SPALTENANZAHL];
					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(
									item.getFlrartikel().getI_id(),
									theClientDto);
					oZeile[SRM_IDENT] = artikelDto.getCNr();
					oZeile[SRM_BEZEICHNUNG] = artikelDto.getArtikelsprDto()
							.getCBez();
					oZeile[SRM_ZUSATZBEZEICHNUNG] = artikelDto
							.getArtikelsprDto().getCZbez();

					oZeile[SRM_ARBEITSGANG] = item.getI_arbeitsgangsnummer();
					ZeitdatenDto zd = getZeiterfassungFac()
							.zeitdatenFindByPrimaryKey(
									losgutschlechDtos[i].getZeitdatenIId(),
									theClientDto);
					oZeile[SRM_GEBUCHT] = zd.getTZeit();

					oZeile[SRM_GUT] = losgutschlechDtos[i].getNGut();
					oZeile[SRM_SCHLECHT] = losgutschlechDtos[i].getNSchlecht();
					oZeile[SRM_INARBEIT] = losgutschlechDtos[i].getNInarbeit();
					oZeile[SRM_OFFEN] = losDto.getNLosgroesse()
							.subtract(losgutschlechDtos[i].getNGut())
							.subtract(losgutschlechDtos[i].getNSchlecht())
							.subtract(losgutschlechDtos[i].getNInarbeit());
					if (item.getFlrmaschine() != null) {
						oZeile[SRM_MASCHINE] = item.getFlrmaschine()
								.getC_identifikationsnr();
					}

					PersonalDto personalDto = getPersonalFac()
							.personalFindByPrimaryKey(zd.getPersonalIId(),
									theClientDto);

					oZeile[SRM_PERSONAL] = personalDto.formatAnrede();
					alDaten.add(oZeile);
				}

			}

			data = new Object[alDaten.size()][SRM_SPALTENANZAHL];
			data = (Object[][]) alDaten.toArray(data);

			// Parameter
			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			mapParameter.put("P_ANGELEGT", new java.util.Date(losDto
					.getTAnlegen().getTime()));
			String sAuftragsnummer;
			String sInternerKommentar = null;
			String sKunde;
			Timestamp dLiefertermin;
			if (losDto.getAuftragpositionIId() != null) {
				// Auftrag holen
				AuftragpositionDto aufposDto = getAuftragpositionFac()
						.auftragpositionFindByPrimaryKey(
								losDto.getAuftragpositionIId());
				AuftragDto auftragDto = getAuftragFac()
						.auftragFindByPrimaryKey(aufposDto.getBelegIId());
				sAuftragsnummer = auftragDto.getCNr();
				// Internen Kommentar aus dem Auftrag, anhaengig von einem
				// Mandantenparameter
				ParametermandantDto parameter = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_FERTIGUNG,
								ParameterFac.PARAMETER_KOMMENTAR_AM_FERTIGUNGSBEGLEITSCHEIN);
				short iValue = Short.parseShort(parameter.getCWert());
				boolean bDruckeKommentar = Helper.short2boolean(iValue);
				if (bDruckeKommentar) {
					sInternerKommentar = auftragDto.getXInternerkommentar();
				}
				// Kunde holen
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						auftragDto.getKundeIIdAuftragsadresse(), theClientDto);
				sKunde = kundeDto.getPartnerDto()
						.getCName1nachnamefirmazeile1();
				if (aufposDto.getTUebersteuerbarerLiefertermin() != null) {
					dLiefertermin = aufposDto
							.getTUebersteuerbarerLiefertermin();
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

			KostenstelleDto kstDto = getSystemFac()
					.kostenstelleFindByPrimaryKey(losDto.getKostenstelleIId());
			mapParameter.put("P_KOSTENSTELLENUMMER", kstDto.getCNr());
			mapParameter.put("P_KUNDE", sKunde);
			mapParameter.put("P_LOSGROESSE", losDto.getNLosgroesse());
			mapParameter.put("P_LOSNUMMER", losDto.getCNr());
			mapParameter.put("P_PRODUKTIONSBEGINN",
					losDto.getTProduktionsbeginn());
			mapParameter.put("P_LIEFERTERMIN", dLiefertermin);
			// Materialliste?
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stkDto = getStuecklisteFac()
						.stuecklisteFindByPrimaryKey(
								losDto.getStuecklisteIId(), theClientDto);
				mapParameter.put("P_STUECKLISTEBEZEICHNUNG", stkDto
						.getArtikelDto().getArtikelsprDto().getCBez());
				mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG", stkDto
						.getArtikelDto().getArtikelsprDto().getCZbez());
				mapParameter.put("P_STUECKLISTENUMMER", stkDto.getArtikelDto()
						.getCNr());
			} else {
				mapParameter.put("P_STUECKLISTEBEZEICHNUNG",
						losDto.getCProjekt());
				mapParameter.put(
						"P_STUECKLISTENUMMER",
						getTextRespectUISpr("fert.materialliste",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			}
			initJRDS(mapParameter, FertigungReportFac.REPORT_MODUL,
					FertigungReportFac.REPORT_STUECKRUECKMELDUNG,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
			return getReportPrint();
		} catch (Exception t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, t);
		} finally {
			closeSession(session);
		}
	}
}
