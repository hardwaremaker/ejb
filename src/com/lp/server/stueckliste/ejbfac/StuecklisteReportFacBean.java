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
package com.lp.server.stueckliste.ejbfac;

import java.awt.Image;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelbestelltDto;
import com.lp.server.artikel.service.ArtikelbestelltFac;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ErsatztypenDto;
import com.lp.server.artikel.service.HerstellerDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerplatzDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VerschleissteilDto;
import com.lp.server.artikel.service.VerschleissteilwerkzeugDto;
import com.lp.server.artikel.service.VkPreisfindungEinzelverkaufspreisDto;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.artikel.service.WaffenausfuehrungDto;
import com.lp.server.artikel.service.WaffenkaliberDto;
import com.lp.server.artikel.service.WaffenkategorieDto;
import com.lp.server.artikel.service.WaffentypDto;
import com.lp.server.artikel.service.WaffentypFeinDto;
import com.lp.server.artikel.service.WaffenzusatzDto;
import com.lp.server.artikel.service.WerkzeugDto;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellpositionMitArtikelliste;
import com.lp.server.bestellung.fastlanereader.generated.FLRWareneingangspositionen;
import com.lp.server.fertigung.service.AnzahlInFertigungDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.personal.service.MaschinenStundensatzDto;
import com.lp.server.stueckliste.fastlanereader.generated.FLRPruefartspr;
import com.lp.server.stueckliste.fastlanereader.generated.FLRPruefkombination;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStklpruefplan;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklistearbeitsplan;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklisteeigenschaft;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklisteposition;
import com.lp.server.stueckliste.service.AlternativmaschineDto;
import com.lp.server.stueckliste.service.ApkommentarDto;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.stueckliste.service.PosersatzDto;
import com.lp.server.stueckliste.service.PruefartDto;
import com.lp.server.stueckliste.service.PruefkombinationDto;
import com.lp.server.stueckliste.service.StklagerentnahmeDto;
import com.lp.server.stueckliste.service.StklparameterDto;
import com.lp.server.stueckliste.service.StrukturDatenParamDto;
import com.lp.server.stueckliste.service.StrukturDatenParamDto.Sort;
import com.lp.server.stueckliste.service.StuecklisteAufgeloest;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklisteFacLocal;
import com.lp.server.stueckliste.service.StuecklisteMitStrukturDto;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklisteeigenschaftDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.service.EinheitDto;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.DatumsfilterVonBis;
import com.lp.server.util.HelperServer;
import com.lp.server.util.KundeId;
import com.lp.server.util.LPReport;
import com.lp.server.util.ReportSqlExecutor;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.server.util.report.TimingInterceptor;
import com.lp.service.plscript.DebuggingScriptRuntime;
import com.lp.service.plscript.ScriptRuntime;
import com.lp.util.ComparableImmutablePair;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.HvTreeMultimap;
import com.lp.util.ImmutablePair;
import com.lp.util.LPDatenSubreport;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

@Stateless
@Interceptors(TimingInterceptor.class)
public class StuecklisteReportFacBean extends LPReport implements StuecklisteReportFac {

	private Object[][] data = null;
	private String sAktuellerReport = null;

	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ARTIKEL = 0;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ZIELMENGENEINHEIT = 1;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ZIELMENGE = 2;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_MONTAGEART = 3;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_LAGERSTAND = 4;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_VERFUEGBAR = 5;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_BESTELLT = 6;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_LAGERORT = 7;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_BEZEICHNUNG = 8;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_KURZBEZEICHNUNG = 9;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ZUSATZBEZEICHNUNG = 10;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ZUSATZBEZEICHNUNG2 = 11;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_INDEX = 12;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_REVISION = 13;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_REFERENZNUMMER = 14;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_STUECKLISTEPOSITION_I_ID = 15;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ERSATZARTIKEL = 16;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ERSATZARTIKEL_BEZEICHNUNG = 17;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_I_EBENE = 18;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_IN_FERTIGUNG = 19;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_F_ARTIKELBILD = 20;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ARTIKEL_HERSTELLERNUMMER = 21;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ARTIKEL_HERSTELLERBEZEICHNUNG = 22;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ERSATZARTIKEL_HERSTELLERNUMMER = 23;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ERSATZARTIKEL_HERSTELLERBEZEICHNUNG = 24;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_FREIGABE_ZEITPUNKT = 25;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_FREIGABE_PERSON = 26;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_LAGERSTAND_ZIELLAGER = 27;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_KUNDENARTIKELNUMMER = 28;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_KUNDENARTIKELBEZEICHNUNG = 29;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_SUBREPORT_ARTIKELKOMMENTAR = 30;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_FREMDFERTIGUNG = 31;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ARTIKEL_FREIGABE_ZEITPUNKT = 32;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ARTIKEL_FREIGABE_PERSON = 33;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ARTIKEL_ANZAHL_ANGEFRAGT = 34;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ARTIKEL_ANZAHL_ANGEBOTEN = 35;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_SUBREPORT_GEPLANTE_LIEFERTERMINE = 36;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ANZAHL_SPALTEN = 37;

	private static int REPORT_MINDERVERFUEGBARKEIT_ARTIKEL = 0;
	private static int REPORT_MINDERVERFUEGBARKEIT_BEZEICHNUNG = 1;
	private static int REPORT_MINDERVERFUEGBARKEIT_ZUSATZBEZEICHNUNG = 2;
	private static int REPORT_MINDERVERFUEGBARKEIT_KURZBEZEICHNUNG = 3;
	private static int REPORT_MINDERVERFUEGBARKEIT_ZUSATZBEZEICHNUNG2 = 4;
	private static int REPORT_MINDERVERFUEGBARKEIT_MENGE = 5;
	private static int REPORT_MINDERVERFUEGBARKEIT_EINHEIT = 6;
	private static int REPORT_MINDERVERFUEGBARKEIT_REFERENZNUMMER = 7;
	private static int REPORT_MINDERVERFUEGBARKEIT_REVISION = 8;
	private static int REPORT_MINDERVERFUEGBARKEIT_INDEX = 9;
	private static int REPORT_MINDERVERFUEGBARKEIT_VERFUEGBAR = 10;
	private static int REPORT_MINDERVERFUEGBARKEIT_ERSATZARTIKEL = 11;
	private static int REPORT_MINDERVERFUEGBARKEIT_ERSATZARTIKEL_BEZEICHNUNG = 12;
	private static int REPORT_MINDERVERFUEGBARKEIT_BESTELLT = 13;
	private static int REPORT_MINDERVERFUEGBARKEIT_RAHMENBESTELLT = 14;
	private static int REPORT_MINDERVERFUEGBARKEIT_LAGERSTAND = 15;
	private static int REPORT_MINDERVERFUEGBARKEIT_SUBREPORT_OFFENE_BESTELLUNGEN = 16;
	private static int REPORT_MINDERVERFUEGBARKEIT_ANZAHL_SPALTEN = 17;

	private static int REPORT_ARBEITSPLAN_ARTIKEL = 0;
	private static int REPORT_ARBEITSPLAN_ARTIKELBEZEICHNUNG = 1;
	private static int REPORT_ARBEITSPLAN_ARBEITSGANG = 2;
	private static int REPORT_ARBEITSPLAN_STUECKZEIT = 3;
	private static int REPORT_ARBEITSPLAN_RUESTZEIT = 4;
	private static int REPORT_ARBEITSPLAN_GESAMTZEIT = 5;
	private static int REPORT_ARBEITSPLAN_KOMMENTAR = 6;
	private static int REPORT_ARBEITSPLAN_PREIS = 7;
	private static int REPORT_ARBEITSPLAN_AGART = 8;
	private static int REPORT_ARBEITSPLAN_AUFSPANNUNG = 9;
	private static int REPORT_ARBEITSPLAN_UNTERARBEITSGANG = 10;
	private static int REPORT_ARBEITSPLAN_MASCHINE = 11;
	private static int REPORT_ARBEITSPLAN_KOSTEN_MASCHINE = 12;
	private static int REPORT_ARBEITSPLAN_FREMDMATERIAL_ARTIKEL = 13;
	private static int REPORT_ARBEITSPLAN_FREMDMATERIAL_ARTIKELBEZEICHNUNG = 14;
	private static int REPORT_ARBEITSPLAN_FREMDMATERIAL_ARTIKELKURZBEZEICHNUNG = 15;
	private static int REPORT_ARBEITSPLAN_FREMDMATERIAL_ARTIKELZUSATZBEZEICHNUNG = 16;
	private static int REPORT_ARBEITSPLAN_FREMDMATERIAL_ARTIKELZUSATZBEZEICHNUNG2 = 17;
	private static int REPORT_ARBEITSPLAN_FREMDMATERIAL_SOLLMENGE = 18;
	private static int REPORT_ARBEITSPLAN_INDEX = 19;
	private static int REPORT_ARBEITSPLAN_REVISION = 20;
	private static int REPORT_ARBEITSPLAN_ARTIKELZUSATZBEZEICHNUNG = 21;
	private static int REPORT_ARBEITSPLAN_ARTIKELZUSATZBEZEICHNUNG2 = 22;
	private static int REPORT_ARBEITSPLAN_LANGTEXT = 23;
	private static int REPORT_ARBEITSPLAN_SUBREPORT_ALTERNATIVE_MASCHINEN = 24;
	private static int REPORT_ARBEITSPLAN_NUR_MASCHINENZEIT = 25;
	private static int REPORT_ARBEITSPLAN_INITIALKOSTEN = 26;
	private static int REPORT_ARBEITSPLAN_ANZAHL_SPALTEN = 27;

	private static int REPORT_PRUEFPLAN_STUECKLISTE_NUMMER = 0;
	private static int REPORT_PRUEFPLAN_STUECKLISTE_BEZEICHNUNG = 1;
	private static int REPORT_PRUEFPLAN_STUECKLISTE_KURZBEZEICHNUNG = 2;
	private static int REPORT_PRUEFPLAN_STUECKLISTE_ZUSATZBEZEICHNUNG = 3;
	private static int REPORT_PRUEFPLAN_STUECKLISTE_ZUSATZBEZEICHNUNG2 = 4;
	private static int REPORT_PRUEFPLAN_STUECKLISTE_KOMMENTAR = 5;
	private static int REPORT_PRUEFPLAN_STUECKLISTE_SUBREPORT_ZEICHNUNGSNUMMMERN = 6;
	private static int REPORT_PRUEFPLAN_STUECKLISTE_KUNDE = 7;
	private static int REPORT_PRUEFPLAN_STUECKLISTE_ERFASSUNGSFAKTOR = 8;
	private static int REPORT_PRUEFPLAN_STUECKLISTE_ANLEGEN = 9;
	private static int REPORT_PRUEFPLAN_STUECKLISTE_AENDERN = 10;
	private static int REPORT_PRUEFPLAN_STUECKLISTE_AENDERN_POSITION = 11;
	private static int REPORT_PRUEFPLAN_STUECKLISTE_AENDERN_ARBEITSPLAN = 12;
	private static int REPORT_PRUEFPLAN_STUECKLISTE_FREIGABE_ZEITPUNKT = 13;
	private static int REPORT_PRUEFPLAN_STUECKLISTE_FREIGABE_PERSON = 14;

	private static int REPORT_PRUEFPLAN_ARTIKEL_KONTAKT = 15;
	private static int REPORT_PRUEFPLAN_ARTIKELBEZEICHNUNG_KONTAKT = 16;
	private static int REPORT_PRUEFPLAN_POSITION_KONTAKT = 17;
	private static int REPORT_PRUEFPLAN_ARTIKEL_LITZE = 18;
	private static int REPORT_PRUEFPLAN_ARTIKELBEZEICHNUNG_LITZE = 19;
	private static int REPORT_PRUEFPLAN_POSITION_LITZE = 20;
	private static int REPORT_PRUEFPLAN_VERSCHLEISSTEILNUMMER = 21;
	private static int REPORT_PRUEFPLAN_VERSCHLEISSTEILBEZEICHNUNG = 22;
	private static int REPORT_PRUEFPLAN_CRIMPHOEHE_DRAHT = 23;
	private static int REPORT_PRUEFPLAN_CRIMPHOEHE_ISOLATION = 24;
	private static int REPORT_PRUEFPLAN_CRIMPBREITE_DRAHT = 25;
	private static int REPORT_PRUEFPLAN_CRIMPBREITE_ISOLATION = 26;
	private static int REPORT_PRUEFPLAN_PRUEFART = 27;
	private static int REPORT_PRUEFPLAN_PRUEFART_BEZEICHNUNG = 28;
	private static int REPORT_PRUEFPLAN_TOLERANZ_CRIMPHOEHE_DRAHT = 29;
	private static int REPORT_PRUEFPLAN_TOLERANZ_CRIMPHOEHE_ISOLATION = 30;
	private static int REPORT_PRUEFPLAN_TOLERANZ_CRIMPBREITE_DRAHT = 31;
	private static int REPORT_PRUEFPLAN_TOLERANZ_CRIMPBREITE_ISOLATION = 32;
	private static int REPORT_PRUEFPLAN_TOLERANZ_WERT = 33;
	private static int REPORT_PRUEFPLAN_WERT = 34;

	private static int REPORT_PRUEFPLAN_MENGE_LITZE = 35;
	private static int REPORT_PRUEFPLAN_EINHEIT_LITZE = 36;
	private static int REPORT_PRUEFPLAN_DIMENSION1_LITZE = 37;
	private static int REPORT_PRUEFPLAN_DIMENSION2_LITZE = 38;
	private static int REPORT_PRUEFPLAN_DIMENSION3_LITZE = 39;
	private static int REPORT_PRUEFPLAN_KOMMENTAR = 40;
	private static int REPORT_PRUEFPLAN_ARTIKEL_LITZE2 = 41;
	private static int REPORT_PRUEFPLAN_ARTIKELBEZEICHNUNG_LITZE2 = 42;
	private static int REPORT_PRUEFPLAN_POSITION_LITZE2 = 43;
	private static int REPORT_PRUEFPLAN_MENGE_LITZE2 = 44;
	private static int REPORT_PRUEFPLAN_EINHEIT_LITZE2 = 45;
	private static int REPORT_PRUEFPLAN_DIMENSION1_LITZE2 = 46;
	private static int REPORT_PRUEFPLAN_DIMENSION2_LITZE2 = 47;
	private static int REPORT_PRUEFPLAN_DIMENSION3_LITZE2 = 48;
	private static int REPORT_PRUEFPLAN_ABZUGSKAFT_LITZE = 49;
	private static int REPORT_PRUEFPLAN_ABZUGSKAFT_LITZE2 = 50;
	private static int REPORT_PRUEFPLAN_DOPPELANSCHLAG = 51;
	private static int REPORT_PRUEFPLAN_SUBREPORT_WERKZEUGE = 52;
	private static int REPORT_PRUEFPLAN_ANZAHL_SPALTEN = 53;

	private static int REPORT_VERSCHLEISSTEILVERWENDUNG_STUECKLISTE = 0;
	private static int REPORT_VERSCHLEISSTEILVERWENDUNG_STUECKLISTE_BEZEICHNUNG = 1;
	private static int REPORT_VERSCHLEISSTEILVERWENDUNG_VERSCHLEISSTEIL = 2;
	private static int REPORT_VERSCHLEISSTEILVERWENDUNG_VERSCHLEISSTEIL_BEZEICHNUNG = 3;
	private static int REPORT_VERSCHLEISSTEILVERWENDUNG_IN_FERTIGUNG = 4;
	private static int REPORT_VERSCHLEISSTEILVERWENDUNG_RESERVIERT = 5;
	private static int REPORT_VERSCHLEISSTEILVERWENDUNG_FEHLMENGEN = 6;
	private static int REPORT_VERSCHLEISSTEILVERWENDUNG_FREIGABEZEITPUNKT = 7;
	private static int REPORT_VERSCHLEISSTEILVERWENDUNG_PERSON_FREIGABE = 8;
	private static int REPORT_VERSCHLEISSTEILVERWENDUNG_ANZAHL_SPALTEN = 9;

	private static final int REPORT_VGL_ARTIKELNUMMER = 0;
	private static final int REPORT_VGL_BEZEICHNUNG = 1;
	private static final int REPORT_VGL_ZUSATZBEZEICHNUNG = 2;
	private static final int REPORT_VGL_MENGE = 3;
	private static final int REPORT_VGL_EINHEIT = 4;
	private static final int REPORT_VGL_DIMENSION1 = 5;
	private static final int REPORT_VGL_DIMENSION2 = 6;
	private static final int REPORT_VGL_DIMENSION3 = 7;
	private static final int REPORT_VGL_POSITION = 8;
	private static final int REPORT_VGL_KOMMENTAR = 9;
	private static final int REPORT_VGL_MONTAGEART = 10;
	private static final int REPORT_VGL_LFDNUMMER = 11;
	private static final int REPORT_VGL_MITDRUCKEN = 12;
	private static final int REPORT_VGL_KALKPREIS = 13;
	private static final int REPORT_VGL_BEGINNTERMINOFFSET = 14;
	private static final int REPORT_VGL_RUESTMENGE = 15;
	private static final int REPORT_VGL_FORMEL = 16;

	private static final int REPORT_VGL_STKL2_ARTIKELNUMMER = 17;
	private static final int REPORT_VGL_STKL2_BEZEICHNUNG = 18;
	private static final int REPORT_VGL_STKL2_ZUSATZBEZEICHNUNG = 19;
	private static final int REPORT_VGL_STKL2_MENGE = 20;
	private static final int REPORT_VGL_STKL2_EINHEIT = 21;
	private static final int REPORT_VGL_STKL2_DIMENSION1 = 22;
	private static final int REPORT_VGL_STKL2_DIMENSION2 = 23;
	private static final int REPORT_VGL_STKL2_DIMENSION3 = 24;
	private static final int REPORT_VGL_STKL2_POSITION = 25;
	private static final int REPORT_VGL_STKL2_KOMMENTAR = 26;
	private static final int REPORT_VGL_STKL2_MONTAGEART = 27;
	private static final int REPORT_VGL_STKL2_LFDNUMMER = 28;
	private static final int REPORT_VGL_STKL2_MITDRUCKEN = 29;
	private static final int REPORT_VGL_STKL2_KALKPREIS = 30;
	private static final int REPORT_VGL_STKL2_BEGINNTERMINOFFSET = 31;
	private static final int REPORT_VGL_STKL2_RUESTMENGE = 32;
	private static final int REPORT_VGL_STKL2_FORMEL = 33;

	private static final int REPORT_VGL_ARBEITSPLAN_ARBEITSGANG = 34;
	private static final int REPORT_VGL_ARBEITSPLAN_UNTERARBEITSGANG = 35;
	private static final int REPORT_VGL_ARBEITSPLAN_STUECKZEIT = 36;
	private static final int REPORT_VGL_ARBEITSPLAN_RUESTZEIT = 37;
	private static final int REPORT_VGL_ARBEITSPLAN_KOMMENTAR = 38;
	private static final int REPORT_VGL_ARBEITSPLAN_APKOMMENTAR = 39;
	private static final int REPORT_VGL_ARBEITSPLAN_LANGTEXT = 40;
	private static final int REPORT_VGL_ARBEITSPLAN_MASCHINE = 41;
	private static final int REPORT_VGL_ARBEITSPLAN_AGART = 42;
	private static final int REPORT_VGL_ARBEITSPLAN_AUFSPANNUNG = 43;
	private static final int REPORT_VGL_ARBEITSPLAN_NURMASCHINENZEIT = 44;
	private static final int REPORT_VGL_ARBEITSPLAN_MASCHINENVERSATZTAGE = 45;
	private static final int REPORT_VGL_ARBEITSPLAN_PPM = 46;
	private static final int REPORT_VGL_ARBEITSPLAN_FORMEL = 47;
	private static final int REPORT_VGL_ARBEITSPLAN_STKL2_ARBEITSGANG = 48;
	private static final int REPORT_VGL_ARBEITSPLAN_STKL2_UNTERARBEITSGANG = 49;
	private static final int REPORT_VGL_ARBEITSPLAN_STKL2_STUECKZEIT = 50;
	private static final int REPORT_VGL_ARBEITSPLAN_STKL2_RUESTZEIT = 51;
	private static final int REPORT_VGL_ARBEITSPLAN_STKL2_KOMMENTAR = 52;
	private static final int REPORT_VGL_ARBEITSPLAN_STKL2_APKOMMENTAR = 53;
	private static final int REPORT_VGL_ARBEITSPLAN_STKL2_LANGTEXT = 54;
	private static final int REPORT_VGL_ARBEITSPLAN_STKL2_MASCHINE = 55;
	private static final int REPORT_VGL_ARBEITSPLAN_STKL2_AGART = 56;
	private static final int REPORT_VGL_ARBEITSPLAN_STKL2_AUFSPANNUNG = 57;
	private static final int REPORT_VGL_ARBEITSPLAN_STKL2_NURMASCHINENZEIT = 58;
	private static final int REPORT_VGL_ARBEITSPLAN_STKL2_MASCHINENVERSATZTAGE = 59;
	private static final int REPORT_VGL_ARBEITSPLAN_STKL2_PPM = 60;
	private static final int REPORT_VGL_ARBEITSPLAN_STKL2_FORMEL = 61;

	private static final int REPORT_VGL_ARBEITSPLAN = 62;
	private static final int REPORT_VGL_POSITIONEN_UNTERSCHIEDLICH = 63;

	private static final int REPORT_VGL_AENDERUNGSDATUM = 64;
	private static final int REPORT_VGL_STKL2_AENDERUNGSDATUM = 65;

	private final static int REPORT_VGL_SPALTENANZAHL = 66;

	private static int REPORT_PRUEFKOMBINATION_ARTIKEL_KONTAKT = 0;
	private static int REPORT_PRUEFKOMBINATION_ARTIKELBEZEICHNUNG_KONTAKT = 1;
	private static int REPORT_PRUEFKOMBINATION_ARTIKEL_LITZE = 2;
	private static int REPORT_PRUEFKOMBINATION_ARTIKELBEZEICHNUNG_LITZE = 3;
	private static int REPORT_PRUEFKOMBINATION_VERSCHLEISSTEILNUMMER = 4;
	private static int REPORT_PRUEFKOMBINATION_STANDARD = 5;
	private static int REPORT_PRUEFKOMBINATION_CRIMPHOEHE_DRAHT = 6;
	private static int REPORT_PRUEFKOMBINATION_CRIMPHOEHE_ISOLATION = 7;
	private static int REPORT_PRUEFKOMBINATION_CRIMPBREITE_DRAHT = 8;
	private static int REPORT_PRUEFKOMBINATION_CRIMPBREITE_ISOLATION = 9;
	private static int REPORT_PRUEFKOMBINATION_VERSCHLEISSTEILBEZEICHNUNG = 10;

	private static int REPORT_PRUEFKOMBINATION_PRUEFART = 11;
	private static int REPORT_PRUEFKOMBINATION_TOLERANZ_CRIMPHOEHE_DRAHT = 12;
	private static int REPORT_PRUEFKOMBINATION_TOLERANZ_CRIMPHOEHE_ISOLATION = 13;
	private static int REPORT_PRUEFKOMBINATION_TOLERANZ_CRIMPBREITE_DRAHT = 14;
	private static int REPORT_PRUEFKOMBINATION_TOLERANZ_CRIMPBREITE_ISOLATION = 15;
	private static int REPORT_PRUEFKOMBINATION_TOLERANZ_WERT = 16;
	private static int REPORT_PRUEFKOMBINATION_WERT = 17;
	private static int REPORT_PRUEFKOMBINATION_PRUEFART_BEZEICHNUNG = 18;
	private static int REPORT_PRUEFKOMBINATION_KOMMENTAR = 19;
	private static int REPORT_PRUEFKOMBINATION_ARTIKEL_LITZE2 = 20;
	private static int REPORT_PRUEFKOMBINATION_ARTIKELBEZEICHNUNG_LITZE2 = 21;
	private static int REPORT_PRUEFKOMBINATION_DOPPELANSCHLAG = 22;
	private static int REPORT_PRUEFKOMBINATION_ABZUGSKRAFT1 = 23;
	private static int REPORT_PRUEFKOMBINATION_ABZUGSKRAFT2 = 24;
	private static int REPORT_PRUEFKOMBINATION_ANZAHL_SPALTEN = 25;

	private static int REPORT_GESAMTKALKULATION_ARTIKEL = 0;
	private static int REPORT_GESAMTKALKULATION_ARTIKELBEZEICHNUNG = 1;
	private static int REPORT_GESAMTKALKULATION_MENGE = 2;
	private static int REPORT_GESAMTKALKULATION_MENGENEINHEIT = 3;
	private static int REPORT_GESAMTKALKULATION_GESTPREIS = 4;
	private static int REPORT_GESAMTKALKULATION_GESTWERT = 5;
	private static int REPORT_GESAMTKALKULATION_LIEF1PREIS = 6;
	private static int REPORT_GESAMTKALKULATION_LIEF1PREISGESAMT = 7;
	private static int REPORT_GESAMTKALKULATION_STUECKZEIT = 8;
	private static int REPORT_GESAMTKALKULATION_RUESTZEIT = 9;
	private static int REPORT_GESAMTKALKULATION_GESTPREISWENNSTUECKLISTE = 10;
	private static int REPORT_GESAMTKALKULATION_SUMMEGESTPREISPOSITIONENWENNSTUECKLISTE = 11;
	private static int REPORT_GESAMTKALKULATION_GESTPREISEWEICHENAB = 12;
	private static int REPORT_GESAMTKALKULATION_DURCHLAUFZEIT = 13;
	private static int REPORT_GESAMTKALKULATION_KALKPREIS = 14;
	private static int REPORT_GESAMTKALKULATION_KALKWERT = 15;
	private static int REPORT_GESAMTKALKULATION_VKPREIS = 16;
	private static int REPORT_GESAMTKALKULATION_VKWERT = 17;
	private static int REPORT_GESAMTKALKULATION_MINDESTDECKUNGSBEITRAG = 18;
	private static int REPORT_GESAMTKALKULATION_STKLART = 19;
	private static int REPORT_GESAMTKALKULATION_GEWICHT = 20;
	private static int REPORT_GESAMTKALKULATION_MATERIALGEWICHT = 21;
	private static int REPORT_GESAMTKALKULATION_WIEDERBESCHAFFUNGSZEIT = 22;
	private static int REPORT_GESAMTKALKULATION_MATERIALZUSCHLAG = 23;
	private static int REPORT_GESAMTKALKULATION_FIXKOSTEN = 24;
	private static int REPORT_GESAMTKALKULATION_KLEINSTER_LIEF1PREIS_2JAHRE = 25;
	private static int REPORT_GESAMTKALKULATION_GROESSTER_LIEF1PREIS_2JAHRE = 26;
	private static int REPORT_GESAMTKALKULATION_ARTIKELZUSATZBEZEICHNUNG = 27;
	private static int REPORT_GESAMTKALKULATION_ARTIKELZUSATZBEZEICHNUNG2 = 28;
	private static int REPORT_GESAMTKALKULATION_KOMMENTAR = 29;
	private static int REPORT_GESAMTKALKULATION_AG = 30;
	private static int REPORT_GESAMTKALKULATION_UAG = 31;
	private static int REPORT_GESAMTKALKULATION_I_EBENE = 32;
	private static int REPORT_GESAMTKALKULATION_FREIGABE_ZEITPUNKT = 33;
	private static int REPORT_GESAMTKALKULATION_FREIGABE_PERSON = 34;
	private static int REPORT_GESAMTKALKULATION_ANZAHL_ARBEITSSCHRITTE = 35;
	private static int REPORT_GESAMTKALKULATION_MANDANT = 36;
	private static int REPORT_GESAMTKALKULATION_MANDANT_KBEZ = 37;
	private static int REPORT_GESAMTKALKULATION_SUMMELIEF1PREISPOSITIONENWENNSTUECKLISTE = 38;
	private static int REPORT_GESAMTKALKULATION_FERTIGUNGSSATZGROESSE = 39;
	private static int REPORT_GESAMTKALKULATION_MINDESTBESTELLMENGE = 40;
	private static int REPORT_GESAMTKALKULATION_ARTIKELGRUPPE = 41;
	private static int REPORT_GESAMTKALKULATION_MATERIALZUSCHLAG_EK = 42;
	private static int REPORT_GESAMTKALKULATION_FORMEL = 43;
	private static int REPORT_GESAMTKALKULATION_POS_MENGE = 44;
	private static int REPORT_GESAMTKALKULATION_POS_MENGENEINHEIT = 45;
	private static int REPORT_GESAMTKALKULATION_DIMENSION1 = 46;
	private static int REPORT_GESAMTKALKULATION_DIMENSION2 = 47;
	private static int REPORT_GESAMTKALKULATION_DIMENSION3 = 48;
	private static int REPORT_GESAMTKALKULATION_LOG_DEBUG = 49;
	private static int REPORT_GESAMTKALKULATION_LOG_INFO = 50;
	private static int REPORT_GESAMTKALKULATION_LOG_WARN = 51;
	private static int REPORT_GESAMTKALKULATION_LOG_ERROR = 52;
	private static int REPORT_GESAMTKALKULATION_VK_PREISBASIS_HEUTE = 53;
	private static int REPORT_GESAMTKALKULATION_HIERARCHIEMENGE = 54;
	private static int REPORT_GESAMTKALKULATION_ARTIKELART = 55;
	private static int REPORT_GESAMTKALKULATION_REIHENFOLGE = 56;
	private static int REPORT_GESAMTKALKULATION_EXTERNER_ARBEITSGANG = 57;
	private static int REPORT_GESAMTKALKULATION_LIEF1PREIS_GUELTIGAB = 58;
	private static int REPORT_GESAMTKALKULATION_ARTIKELREFERENZNUMMER = 59;
	private static int REPORT_GESAMTKALKULATION_ARTIKELKURZBEZEICHNUNG = 60;
	private static int REPORT_GESAMTKALKULATION_FREMDGEFERTIGT = 61;
	private static int REPORT_GESAMTKALKULATION_RUESTMENGE = 62;
	private static int REPORT_GESAMTKALKULATION_LAGERSTANDSDETAIL = 63;
	private static int REPORT_GESAMTKALKULATION_MENGE_OHNE_VPE = 64;
	private static int REPORT_GESAMTKALKULATION_MELDEPFLICHTIG = 65;
	private static int REPORT_GESAMTKALKULATION_BEWILLIGUNGSPFLICHTIG = 66;
	private static int REPORT_GESAMTKALKULATION_LIEF1_NAME = 67;
	private static int REPORT_GESAMTKALKULATION_LIEF1_KBEZ = 68;
	private static int REPORT_GESAMTKALKULATION_URSPRUNGSLAND_LKZ = 69;
	private static int REPORT_GESAMTKALKULATION_URSPRUNGSLAND_NAME = 70;
	private static int REPORT_GESAMTKALKULATION_WARENVERKEHRSNUMMER = 71;
	private static int REPORT_GESAMTKALKULATION_SUMMELIEF1PREISPOSITIONENWENNSTUECKLISTE_MATERIAL = 72;
	private static int REPORT_GESAMTKALKULATION_SUMMELIEF1PREISPOSITIONENWENNSTUECKLISTE_ARBEITSZEIT = 73;
	private static int REPORT_GESAMTKALKULATION_INITITALKOSTEN = 74;
	private static int REPORT_GESAMTKALKULATION_ANZAHL_SPALTEN = 75;

	private static int REPORT_REICHWEITE_ARTIKELNUMMER = 0;
	private static int REPORT_REICHWEITE_BEZEICHNUNG = 1;
	private static int REPORT_REICHWEITE_STUECKLISTENART = 2;
	private static int REPORT_REICHWEITE_LAGERSTAND = 3;
	private static int REPORT_REICHWEITE_JAHRESMENGE = 4;
	private static int REPORT_REICHWEITE_WIEDERBESCHAFFUNGSMORAL = 5;
	private static int REPORT_REICHWEITE_EBENE = 6;
	private static int REPORT_REICHWEITE_WIEDERBESCHAFFUNGSZEIT = 7;
	private static int REPORT_REICHWEITE_ARTIKELLIEFERANT = 8;
	private static int REPORT_REICHWEITE_LAGERSOLLSTAND = 9;
	private static int REPORT_REICHWEITE_LAGERMINDESTSTAND = 10;
	private static int REPORT_REICHWEITE_STUECKLISTE_DURCHLAUFZEIT = 11;
	private static int REPORT_REICHWEITE_STUECKLISTE_I_ID = 12;
	private static int REPORT_REICHWEITE_BESTELLT = 13;
	private static int REPORT_REICHWEITE_RAHMENBESTELLT = 14;
	private static int REPORT_REICHWEITE_NICHT_LAGERBEWIRTSCHAFTET = 15;
	private static int REPORT_REICHWEITE_ANZAHL_SPALTEN = 16;

	private static int REPORT_FREIGABE_ARTIKELNUMMER = 0;
	private static int REPORT_FREIGABE_BEZEICHNUNG = 1;
	private static int REPORT_FREIGABE_ZUSATZBEZEICHNUNG = 2;
	private static int REPORT_FREIGABE_ZUSATZBEZEICHNUNG2 = 3;
	private static int REPORT_FREIGABE_MENGE = 4;
	private static int REPORT_FREIGABE_LFDNR = 5;
	private static int REPORT_FREIGABE_POSITION = 6;
	private static int REPORT_FREIGABE_STUECKLISTEPOSITION_I_ID = 7;
	private static int REPORT_FREIGABE_FREIGABE_VON = 8;
	private static int REPORT_FREIGABE_FREIGABE_AM = 9;
	private static int REPORT_FREIGABE_SUBREPORT_ERSATZTYPEN_STKL = 10;
	private static int REPORT_FREIGABE_SUBREPORT_ERSATZTYPEN_ARTIKEL = 11;
	private static int REPORT_FREIGABE_FREIGEGEBEN = 12;
	private static int REPORT_FREIGABE_ANZAHL_SPALTEN = 13;

	@PersistenceContext
	private EntityManager em;

	@EJB
	private StuecklisteFacLocal stuecklisteFacLocalBean;

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printMinderverfuegbarkeit(Integer stuecklisteIId, boolean inFertigungBeruecksichtigen,
			TheClientDto theClientDto) {
		sAktuellerReport = StuecklisteReportFac.REPORT_STUECKLISTE_MINDERVERFUEGBARKEIT;

		HashMap<String, Object> reportParameter = new HashMap<String, Object>();
		StuecklisteDto dto = getStuecklisteFac().stuecklisteFindByPrimaryKey(stuecklisteIId, theClientDto);

		reportParameter.put("P_STUECKLISTE", dto.getArtikelDto().getCNr());

		reportParameter.put("P_STUECKLISTEBEZEICHNUNG", dto.getArtikelDto().formatBezeichnung());

		reportParameter.put("P_BERUECKSICHTIGE_IN_FERTIGUNG", inFertigungBeruecksichtigen);

		reportParameter.put("P_KOMMENTAR", Helper.formatStyledTextForJasper(dto.getXKommentar()));

		ArrayList alDaten = new ArrayList();

		TreeMap tmSubSperrlager = new TreeMap();
		String[] fieldnamesSubSperlager = new String[] { "Stueckliste", "Bezeichnung", "Lagerstand_Auf_Sperrlaegern" };

		try {
			StuecklisteAufgeloest stuecklisteInfoDto = stuecklisteFacLocalBean
					.getStrukturdatenEinesArtikelsStrukturiert(dto.getArtikelDto().getIId(), false,
							StuecklisteFacLocal.STUECKLISTENAUFLOESUNGSTIEFE_UNBEGRENZT, false, new BigDecimal(1),
							false, false, theClientDto);

			ArrayList<?> alStuecklisteAufgeloest = stuecklisteInfoDto
					.getAlStuecklisteAufgeloest_OhneLagerndeArtikelUndOhneStuecklisten(new BigDecimal(1),
							inFertigungBeruecksichtigen);

			Iterator it = alStuecklisteAufgeloest.iterator();

			while (it.hasNext()) {
				StuecklisteAufgeloest struktur = (StuecklisteAufgeloest) it.next();
				StuecklistepositionDto position = struktur.getStuecklistepositionDto();

				// Nur wenn eine Stkl
				if (struktur.getStuecklisteDto() != null) {
					if (struktur.getLagerstandSperrlaeger().doubleValue() > 0) {
						Object[] oZeileSubSperrlaeger = new Object[3];
						ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
								struktur.getStuecklisteDto().getArtikelIId(), theClientDto);

						oZeileSubSperrlaeger[0] = artikelDto.getCNr();
						oZeileSubSperrlaeger[1] = artikelDto.formatBezeichnung();
						oZeileSubSperrlaeger[2] = struktur.getLagerstandSperrlaeger();

						tmSubSperrlager.put(artikelDto.getCNr(), oZeileSubSperrlaeger);

					}
					continue;
				}

				Object[] oZeile = new Object[REPORT_MINDERVERFUEGBARKEIT_ANZAHL_SPALTEN];

				com.lp.server.artikel.service.ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(position.getArtikelIId(), theClientDto);

				if (artikelDto.getArtikelartCNr() != null && artikelDto.getArtikelartCNr()
						.equals(com.lp.server.artikel.service.ArtikelFac.ARTIKELART_HANDARTIKEL)) {

					oZeile[REPORT_MINDERVERFUEGBARKEIT_ARTIKEL] = "";

				} else {
					oZeile[REPORT_MINDERVERFUEGBARKEIT_ARTIKEL] = artikelDto.getCNr();
				}

				oZeile[REPORT_MINDERVERFUEGBARKEIT_REFERENZNUMMER] = artikelDto.getCReferenznr();

				if (artikelDto.getArtikelsprDto() != null) {
					oZeile[REPORT_MINDERVERFUEGBARKEIT_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
					oZeile[REPORT_MINDERVERFUEGBARKEIT_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCZbez();
					oZeile[REPORT_MINDERVERFUEGBARKEIT_ZUSATZBEZEICHNUNG2] = artikelDto.getArtikelsprDto().getCZbez2();
					oZeile[REPORT_MINDERVERFUEGBARKEIT_KURZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCKbez();
				}

				oZeile[REPORT_MINDERVERFUEGBARKEIT_EINHEIT] = artikelDto.getEinheitCNr();

				oZeile[REPORT_MINDERVERFUEGBARKEIT_INDEX] = artikelDto.getCIndex();
				oZeile[REPORT_MINDERVERFUEGBARKEIT_REVISION] = artikelDto.getCRevision();

				BigDecimal lagerstandSperrlager = getLagerFac()
						.getLagerstandAllerSperrlaegerEinesMandanten(artikelDto.getIId(), theClientDto);

				BigDecimal lagerstandOhneSperlaeger = getLagerFac()
						.getLagerstandAllerLagerEinesMandanten(artikelDto.getIId(), theClientDto)
						.subtract(lagerstandSperrlager);
				oZeile[REPORT_MINDERVERFUEGBARKEIT_LAGERSTAND] = lagerstandOhneSperlaeger;

				// Verfuegbar
				BigDecimal reservierungen = getReservierungFac().getAnzahlReservierungen(artikelDto.getIId(),
						theClientDto);
				BigDecimal fehlmengen = getFehlmengeFac().getAnzahlFehlmengeEinesArtikels(artikelDto.getIId(),
						theClientDto);

				BigDecimal verfuegbar = lagerstandOhneSperlaeger.subtract(fehlmengen).subtract(reservierungen);
				oZeile[REPORT_MINDERVERFUEGBARKEIT_VERFUEGBAR] = verfuegbar;

				oZeile[REPORT_MINDERVERFUEGBARKEIT_BESTELLT] = getArtikelbestelltFac()
						.getAnzahlBestellt(artikelDto.getIId());

				Hashtable htAnzahlRahmenbestellt = getArtikelbestelltFac().getAnzahlRahmenbestellt(artikelDto.getIId(),
						theClientDto);
				if (htAnzahlRahmenbestellt.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
					oZeile[REPORT_MINDERVERFUEGBARKEIT_RAHMENBESTELLT] = (BigDecimal) htAnzahlRahmenbestellt
							.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
				}

				BigDecimal bdZielmenge = position.getNZielmenge();

				oZeile[REPORT_MINDERVERFUEGBARKEIT_MENGE] = bdZielmenge;

				// Zusammenfassen
				boolean bGefunden = false;
				for (int i = 0; i < alDaten.size(); i++) {

					Object[] oZeileTemp = (Object[]) alDaten.get(i);

					if (oZeileTemp[REPORT_MINDERVERFUEGBARKEIT_ARTIKEL]
							.equals(oZeile[REPORT_MINDERVERFUEGBARKEIT_ARTIKEL])) {
						BigDecimal bdMengeVorhanden = (BigDecimal) oZeileTemp[REPORT_MINDERVERFUEGBARKEIT_MENGE];
						oZeileTemp[REPORT_MINDERVERFUEGBARKEIT_MENGE] = bdMengeVorhanden.add(bdZielmenge);
						alDaten.set(i, oZeileTemp);
						bGefunden = true;
					}

				}
				if (bGefunden == false) {

					ArrayList alDatenSub = new ArrayList();
					String sQuery = "SELECT position from FLRBestellpositionMitArtikelliste position WHERE position.flrbestellung.mandant_c_nr = '001' AND position.bestellpositionstatus_c_nr NOT IN ('Erledigt       ','Geliefert      ','Storniert      ') AND position.flrbestellung.bestellungstatus_c_nr IN ('Bestaetigt     ','Offen          ','Teilerledigt   ') AND position.position_i_id_artikelset IS NULL  AND position.flrbestellung.bestellungart_c_nr <> 'Rahmenbestellung    ' AND position.flrartikel.i_id ="
							+ artikelDto.getIId() + "  ORDER BY position.t_uebersteuerterliefertermin ASC";

					Session session = FLRSessionFactory.getFactory().openSession();

					Query query = session.createQuery(sQuery);

					List<?> resultList = query.list();
					Iterator<?> resultListIterator = resultList.iterator();

					while (resultListIterator.hasNext()) {
						FLRBestellpositionMitArtikelliste flrBestellpositionMitArtikelliste = (FLRBestellpositionMitArtikelliste) resultListIterator
								.next();

						Object[] oZeileSub = new Object[5];

						oZeileSub[0] = flrBestellpositionMitArtikelliste.getFlrbestellung().getC_nr();
						oZeileSub[1] = flrBestellpositionMitArtikelliste.getT_uebersteuerterliefertermin();
						oZeileSub[2] = flrBestellpositionMitArtikelliste.getT_auftragsbestaetigungstermin();
						oZeileSub[3] = HelperServer.formatNameAusFLRPartner(
								flrBestellpositionMitArtikelliste.getFlrbestellung().getFlrlieferant().getFlrpartner());
						oZeileSub[4] = flrBestellpositionMitArtikelliste.getN_offenemenge();

						alDatenSub.add(oZeileSub);
					}

					String[] fieldnames = new String[] { "Bestellnummer", "Positionstermin", "ABTermin", "Lieferant",
							"Menge" };

					Object[][] oSubData = new Object[alDatenSub.size()][fieldnames.length];
					oSubData = (Object[][]) alDatenSub.toArray(oSubData);

					oZeile[REPORT_MINDERVERFUEGBARKEIT_SUBREPORT_OFFENE_BESTELLUNGEN] = new LPDatenSubreport(oSubData,
							fieldnames);

					alDaten.add(oZeile);
				}
			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		for (int k = alDaten.size() - 1; k > 0; --k) {
			for (int j = 0; j < k; ++j) {
				Object[] a1 = (Object[]) alDaten.get(j);
				Object[] a2 = (Object[]) alDaten.get(j + 1);

				if (((String) a1[REPORT_MINDERVERFUEGBARKEIT_ARTIKEL])
						.compareTo((String) a2[REPORT_MINDERVERFUEGBARKEIT_ARTIKEL]) > 0) {
					alDaten.set(j, a2);
					alDaten.set(j + 1, a1);
				}

			}

		}

		data = new Object[alDaten.size()][REPORT_MINDERVERFUEGBARKEIT_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(data);

		Object[][] oSubDataSperrlaeger = new Object[tmSubSperrlager.size()][fieldnamesSubSperlager.length];
		oSubDataSperrlaeger = (Object[][]) tmSubSperrlager.values().toArray(oSubDataSperrlaeger);

		reportParameter.put("P_SUBREPORT_SPERRLAEGER",
				new LPDatenSubreport(oSubDataSperrlaeger, fieldnamesSubSperlager));

		initJRDS(reportParameter, StuecklisteReportFac.REPORT_MODUL,
				StuecklisteReportFac.REPORT_STUECKLISTE_MINDERVERFUEGBARKEIT, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAusgabestueckliste(Integer[] stuecklisteIId, Integer lagerIId,
			Boolean bMitStuecklistenkommentar, Boolean bUnterstuecklistenEinbinden,
			Boolean bGleichePositionenZusammenfassen, BigDecimal nLosgroesse, boolean bSortiertNachArtikelbezeichnung,
			int iOptionLager, boolean fremdfertigungAufloesen, TheClientDto theClientDto) {

		if (stuecklisteIId == null || lagerIId == null || bMitStuecklistenkommentar == null
				|| bUnterstuecklistenEinbinden == null || bGleichePositionenZusammenfassen == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"stuecklisteIId == null || lagerIId == null || bMitStuecklistenkommentar == null || bUnterstuecklistenEinbinden == null || bGleichePositionenZusammenfassen == null"));
		}

		// Losgreosse updaten
		StuecklisteDto dto = null;
		String stuecklisten = "";
		HashSet<Integer> slagerIIdsAbbuchungslager = new HashSet<Integer>();
		for (int i = 0; i < stuecklisteIId.length; i++) {

			StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(stuecklisteIId[i], theClientDto);
			stklDto.setNLosgroesse(nLosgroesse);

			stuecklisten += stklDto.getArtikelDto().getCNr();
			if (i != stuecklisteIId.length - 1) {
				stuecklisten += ", ";
			}

			if (i == 0) {
				dto = stklDto;
			}

			getStuecklisteFac().updateStuecklisteLosgroesse(stuecklisteIId[i], nLosgroesse);
			StklagerentnahmeDto[] laDtos = getStuecklisteFac().stklagerentnahmeFindByStuecklisteIId(stklDto.getIId());

			for (int j = 0; j < laDtos.length; j++) {
				slagerIIdsAbbuchungslager.add(laDtos[j].getLagerIId());
			}
		}

		KundeDto kdDto = null;

		try {
			if (dto.getPartnerIId() != null) {
				kdDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(dto.getPartnerIId(),
						theClientDto.getMandant(), theClientDto);

			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		// PJ18852
		String inLagerstaende = "(";

		Iterator itLagerstaende = slagerIIdsAbbuchungslager.iterator();
		while (itLagerstaende.hasNext()) {
			Integer lagerIIdIn = (Integer) itLagerstaende.next();

			inLagerstaende += lagerIIdIn;

			if (itLagerstaende.hasNext()) {
				inLagerstaende += ",";
			}

		}

		inLagerstaende += ")";

		index = -1;
		sAktuellerReport = StuecklisteReportFac.REPORT_STUECKLISTE_AUSGABESTUECKLSITE;

		List<?> m = getStuecklisteFac().getStrukturDatenEinerStueckliste(stuecklisteIId, theClientDto,
				StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_ARTIKELNR, 0, null,
				bUnterstuecklistenEinbinden.booleanValue(), bGleichePositionenZusammenfassen.booleanValue(),
				nLosgroesse, null, false, fremdfertigungAufloesen);

		Iterator<?> it = m.listIterator();
		data = new Object[m.size()][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ANZAHL_SPALTEN];
		int row = 0;
		while (it.hasNext()) {
			StuecklisteMitStrukturDto struktur = (StuecklisteMitStrukturDto) it.next();
			StuecklistepositionDto position = struktur.getStuecklistepositionDto();

			data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_I_EBENE] = struktur.getIEbene();
			data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_FREIGABE_PERSON] = struktur.getCKurzzeichenPersonFreigabe();
			data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_FREIGABE_ZEITPUNKT] = struktur.getTFreigabe();

			try {

				data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_STUECKLISTEPOSITION_I_ID] = position.getIId();

				com.lp.server.artikel.service.ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(position.getArtikelIId(), theClientDto);

				Long[] angefragtUndAngeboten = getAnfrageFac().getAngefragteUndAngeboteneMenge(artikelDto.getIId(),
						theClientDto);

				data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ARTIKEL_ANZAHL_ANGEFRAGT] = angefragtUndAngeboten[0];
				data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ARTIKEL_ANZAHL_ANGEBOTEN] = angefragtUndAngeboten[1];

				if (artikelDto.getArtikelartCNr() != null && artikelDto.getArtikelartCNr()
						.equals(com.lp.server.artikel.service.ArtikelFac.ARTIKELART_HANDARTIKEL)) {

					data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ARTIKEL] = "";

				} else {

					data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ARTIKEL_FREIGABE_ZEITPUNKT] = artikelDto
							.getTFreigabe();

					if (artikelDto.getPersonalIIdFreigabe() != null) {
						data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ARTIKEL_FREIGABE_PERSON] = getPersonalFac()
								.personalFindByPrimaryKeySmall(artikelDto.getPersonalIIdFreigabe()).getCKurzzeichen();
					}

					data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ARTIKEL] = artikelDto.getCNr();

					data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_SUBREPORT_ARTIKELKOMMENTAR] = getSubreportArtikelkommentar(
							artikelDto.getIId(), LocaleFac.BELEGART_STUECKLISTE, theClientDto);

					data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ARTIKEL_HERSTELLERNUMMER] = artikelDto
							.getCArtikelnrhersteller();
					data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ARTIKEL_HERSTELLERBEZEICHNUNG] = artikelDto
							.getCArtikelbezhersteller();

					if (kdDto != null) {
						// KundeArtikelnr gueltig zu Belegdatum
						KundesokoDto kundeSokoDto_gueltig = this.getKundesokoFac()
								.kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc(kdDto.getIId(),
										artikelDto.getIId(),
										Helper.cutDate(new java.sql.Date(System.currentTimeMillis())));
						if (kundeSokoDto_gueltig != null) {
							data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_KUNDENARTIKELNUMMER] = kundeSokoDto_gueltig
									.getCKundeartikelnummer();
							data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_KUNDENARTIKELBEZEICHNUNG] = kundeSokoDto_gueltig
									.getCKundeartikelbez();
						}
					}

					// Artikelkommentar Text und Bild
					Image imageKommentar = null;
					try {
						ArtikelkommentarDto[] aKommentarDto = getArtikelkommentarFac()
								.artikelkommentardruckFindByArtikelIIdBelegartCNr(artikelDto.getIId(),
										LocaleFac.BELEGART_LOS, theClientDto.getLocUiAsString(), theClientDto);

						// Artikelkommentar kann Text oder Bild sein
						if (aKommentarDto != null) {
							for (int k = 0; k < aKommentarDto.length; k++) {
								String cDatenformat = aKommentarDto[k].getDatenformatCNr().trim();
								if (cDatenformat.indexOf(MediaFac.DATENFORMAT_MIMETYPEART_IMAGE) != -1) {
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

					data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_F_ARTIKELBILD] = imageKommentar;

				}

				data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_REFERENZNUMMER] = artikelDto.getCReferenznr();

				StuecklisteDto stklDto = getStuecklisteFac()
						.stuecklisteFindByArtikelIIdMandantCNrOhneExc(artikelDto.getIId(), theClientDto.getMandant());
				if (stklDto != null) {
					data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_FREMDFERTIGUNG] = Helper
							.short2Boolean(stklDto.getBFremdfertigung());
				}

				if (artikelDto.getArtikelsprDto() != null) {
					data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_BEZEICHNUNG] = artikelDto.getArtikelsprDto()
							.getCBez();
					data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto()
							.getCZbez();
					data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ZUSATZBEZEICHNUNG2] = artikelDto.getArtikelsprDto()
							.getCZbez2();
					data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_KURZBEZEICHNUNG] = artikelDto.getArtikelsprDto()
							.getCKbez();
				}

				data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ZIELMENGENEINHEIT] = artikelDto.getEinheitCNr();

				data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_INDEX] = artikelDto.getCIndex();
				data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_REVISION] = artikelDto.getCRevision();

				BigDecimal lagerstand = null;

				if (iOptionLager == StuecklisteReportFac.REPORT_AUSGABESTUECKLISTE_OPTION_NUR_ABBUCHUNGSLAEGER) {
					// PJ18852 Der Lagerstand ist die Summe der
					// Quicklagerstaende
					// der Abbuchungslaeger

					if (inLagerstaende != null && inLagerstaende.length() > 2) {

						Session session = FLRSessionFactory.getFactory().openSession();
						String sQuery = "SELECT sum(al.n_lagerstand) FROM FLRArtikellager al WHERE al.compId.artikel_i_id="
								+ artikelDto.getIId() + " AND al.compId.lager_i_id IN " + inLagerstaende;

						org.hibernate.Query hquery = session.createQuery(sQuery);

						List<?> resultList = hquery.list();
						Iterator<?> resultListIterator = resultList.iterator();

						if (resultListIterator.hasNext()) {
							lagerstand = (BigDecimal) resultListIterator.next();
						}
					}
					if (lagerstand == null) {
						lagerstand = BigDecimal.ZERO;
					}

					data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_LAGERSTAND] = lagerstand;

				} else if (iOptionLager == StuecklisteReportFac.REPORT_AUSGABESTUECKLISTE_OPTION_LAGER_SELEKTIERT) {

					lagerstand = getLagerFac().getLagerstandOhneExc(artikelDto.getIId(), lagerIId, theClientDto);
					data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_LAGERSTAND] = lagerstand;
				} else {

					lagerstand = getLagerFac().getLagerstandAllerLagerEinesMandanten(artikelDto.getIId(), theClientDto);
					data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_LAGERSTAND] = lagerstand;
				}

				BigDecimal lagerstandZiellager = getLagerFac().getLagerstandOhneExc(artikelDto.getIId(),
						dto.getLagerIIdZiellager(), theClientDto);
				data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_LAGERSTAND_ZIELLAGER] = lagerstandZiellager;

				// Verfuegbar
				BigDecimal reservierungen = getReservierungFac().getAnzahlReservierungen(artikelDto.getIId(),
						theClientDto);
				BigDecimal fehlmengen = getFehlmengeFac().getAnzahlFehlmengeEinesArtikels(artikelDto.getIId(),
						theClientDto);

				BigDecimal verfuegbar = lagerstand.subtract(fehlmengen).subtract(reservierungen);
				data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_VERFUEGBAR] = verfuegbar;

				String[] fieldnames = new String[] { "Menge", "Termin" };

				ArrayList alDatenSubreport = new ArrayList();
				BigDecimal bdInFertigung = BigDecimal.ZERO;

				ArrayList<AnzahlInFertigungDto> infertigungDto = getFertigungFac()
						.getAnzahlInFertigungDtos(artikelDto.getIId(), theClientDto);

				for (int i = 0; i < infertigungDto.size(); i++) {
					bdInFertigung = bdInFertigung.add(infertigungDto.get(i).getBdMenge());

					Object[] oZeile = new Object[2];
					oZeile[0] = infertigungDto.get(i).getBdMenge();
					oZeile[1] = infertigungDto.get(i).getTProduktionsende();
					alDatenSubreport.add(oZeile);

				}

				data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_IN_FERTIGUNG] = bdInFertigung;

				ArrayList<ArtikelbestelltDto> bestelltDto = getArtikelbestelltFac()
						.getArtikelbestelltDtos(artikelDto.getIId());
				BigDecimal bdBestellt = BigDecimal.ZERO;

				for (int i = 0; i < bestelltDto.size(); i++) {
					bdBestellt = bdBestellt.add(bestelltDto.get(i).getNMenge());

					Object[] oZeile = new Object[2];
					oZeile[0] = bestelltDto.get(i).getNMenge();
					oZeile[1] = bestelltDto.get(i).getTLiefertermin();
					alDatenSubreport.add(oZeile);

				}

				data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_BESTELLT] = bdBestellt;

				Object[][] dataSub = new Object[alDatenSubreport.size()][fieldnames.length];
				dataSub = (Object[][]) alDatenSubreport.toArray(dataSub);

				data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_SUBREPORT_GEPLANTE_LIEFERTERMINE] = new LPDatenSubreport(
						dataSub, fieldnames);

				data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_MONTAGEART] = position.getMontageartDto().getCBez();

				if (dto.getNLosgroesse() != null && Helper.short2boolean(position.getBRuestmenge()) == false) {
					data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ZIELMENGE] = Helper
							.rundeKaufmaennisch(position.getNZielmenge(dto.getNLosgroesse()), 2);
				} else {
					data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ZIELMENGE] = position.getNZielmenge();
				}

				data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_MONTAGEART] = position.getMontageartDto().getCBez();

				try {

					if (iOptionLager == StuecklisteReportFac.REPORT_AUSGABESTUECKLISTE_OPTION_NUR_ABBUCHUNGSLAEGER) {

						String lagerorte = "";
						itLagerstaende = slagerIIdsAbbuchungslager.iterator();
						while (itLagerstaende.hasNext()) {
							Integer lagerIIdIn = (Integer) itLagerstaende.next();

							lagerorte += getLagerFac().getLagerplaezteEinesArtikels(artikelDto.getIId(), lagerIIdIn);

						}

						data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_LAGERORT] = lagerorte;
					} else {

						data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_LAGERORT] = getLagerFac()
								.getLagerplaezteEinesArtikels(artikelDto.getIId(), lagerIId);

					}

				} catch (javax.ejb.EJBException ex1) {
					// kein lagerort vorhanden
				} catch (EJBExceptionLP ex1) {
					// kein lagerort vorhanden
				}
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
			row++;
		}

		for (int k = data.length - 1; k > 0; --k) {
			for (int j = 0; j < k; ++j) {
				Object[] a1 = (Object[]) data[j];
				Object[] a2 = (Object[]) data[j + 1];

				if (bSortiertNachArtikelbezeichnung) {
					String bez1 = "";
					if (a1[REPORT_STUECKLISTE_AUSGABESTUECKLISTE_BEZEICHNUNG] != null) {
						bez1 = (String) a1[REPORT_STUECKLISTE_AUSGABESTUECKLISTE_BEZEICHNUNG];
					}
					String bez2 = "";
					if (a2[REPORT_STUECKLISTE_AUSGABESTUECKLISTE_BEZEICHNUNG] != null) {
						bez2 = (String) a2[REPORT_STUECKLISTE_AUSGABESTUECKLISTE_BEZEICHNUNG];
					}
					if (bez1.compareTo(bez2) > 0) {
						data[j] = a2;
						data[j + 1] = a1;

					}
				} else {
					if (((String) a1[REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ARTIKEL])
							.compareTo((String) a2[REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ARTIKEL]) > 0) {
						data[j] = a2;
						data[j + 1] = a1;

					}
				}

			}

		}

		// PJ15813 Ersatztypen hinzufuegen
		if (bGleichePositionenZusammenfassen == false) {

			ArrayList alDaten = new ArrayList();
			for (int i = 0; i < data.length; i++) {

				alDaten.add(data[i]);

				PosersatzDto[] posersatzDtos = getStuecklisteFac().posersatzFindByStuecklistepositionIId(
						(Integer) data[i][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_STUECKLISTEPOSITION_I_ID]);

				for (int j = 0; j < posersatzDtos.length; j++) {
					Object[] neueZeile = data[i].clone();

					com.lp.server.artikel.service.ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(posersatzDtos[j].getArtikelIIdErsatz(), theClientDto);

					neueZeile[REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ERSATZARTIKEL] = artikelDto.getCNr();

					neueZeile[REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ERSATZARTIKEL_BEZEICHNUNG] = artikelDto
							.getCBezAusSpr();

					neueZeile[REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ERSATZARTIKEL_HERSTELLERNUMMER] = artikelDto
							.getCArtikelnrhersteller();
					neueZeile[REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ERSATZARTIKEL_HERSTELLERBEZEICHNUNG] = artikelDto
							.getCArtikelbezhersteller();

					neueZeile[REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ZIELMENGE] = new BigDecimal(0);

					// SP2537

					try {
						BigDecimal lagerstand = null;
						lagerstand = getLagerFac().getLagerstandOhneExc(artikelDto.getIId(), lagerIId, theClientDto);
						neueZeile[REPORT_STUECKLISTE_AUSGABESTUECKLISTE_LAGERSTAND] = lagerstand;

						// Verfuegbar
						BigDecimal reservierungen = getReservierungFac().getAnzahlReservierungen(artikelDto.getIId(),
								theClientDto);
						BigDecimal fehlmengen = getFehlmengeFac().getAnzahlFehlmengeEinesArtikels(artikelDto.getIId(),
								theClientDto);

						BigDecimal verfuegbar = lagerstand.subtract(fehlmengen).subtract(reservierungen);
						neueZeile[REPORT_STUECKLISTE_AUSGABESTUECKLISTE_VERFUEGBAR] = verfuegbar;

						neueZeile[REPORT_STUECKLISTE_AUSGABESTUECKLISTE_IN_FERTIGUNG] = getFertigungFac()
								.getAnzahlInFertigung(artikelDto.getIId(), theClientDto);

						neueZeile[REPORT_STUECKLISTE_AUSGABESTUECKLISTE_BESTELLT] = getArtikelbestelltFac()
								.getAnzahlBestellt(artikelDto.getIId());
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					alDaten.add(neueZeile);

				}

			}

			data = new Object[alDaten.size()][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ANZAHL_SPALTEN];
			data = (Object[][]) alDaten.toArray(data);

		}

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		if (stuecklisteIId.length > 1) {
			parameter.put("P_STUECKLISTE", stuecklisten);
		} else {
			parameter.put("P_STUECKLISTE", dto.getArtikelDto().getCNr());

		}

		if (iOptionLager == StuecklisteReportFac.REPORT_AUSGABESTUECKLISTE_OPTION_NUR_ABBUCHUNGSLAEGER) {
			parameter.put("P_OPTION_LAGER", getTextRespectUISpr("stk.report.nurabuchungslaeger",
					theClientDto.getMandant(), theClientDto.getLocUi()));
		} else if (iOptionLager == StuecklisteReportFac.REPORT_AUSGABESTUECKLISTE_OPTION_ALLE_LAEGER) {
			parameter.put("P_OPTION_LAGER",
					getTextRespectUISpr("stk.report.allelaeger", theClientDto.getMandant(), theClientDto.getLocUi()));
		} else if (iOptionLager == StuecklisteReportFac.REPORT_AUSGABESTUECKLISTE_OPTION_LAGER_SELEKTIERT) {

			String s = getTextRespectUISpr("stk.report.selektierteslager", theClientDto.getMandant(),
					theClientDto.getLocUi());
			if (lagerIId != null) {
				try {
					s += ": " + getLagerFac().lagerFindByPrimaryKey(lagerIId).getCNr();
				} catch (RemoteException ex) {
					throwEJBExceptionLPRespectOld(ex);
				}
			}
			parameter.put("P_OPTION_LAGER", s);

		}

		try {
			// Zeichnungsnummer
			StuecklisteeigenschaftDto[] stuecklisteeigenschaftDtos = getStuecklisteFac()
					.stuecklisteeigenschaftFindByStuecklisteIId(dto.getIId());
			ArrayList<Object[]> al = new ArrayList<Object[]>();
			for (int i = 0; i < stuecklisteeigenschaftDtos.length; i++) {
				StuecklisteeigenschaftDto stuecklisteeigenschaftDto = stuecklisteeigenschaftDtos[i];

				Object[] o = new Object[2];
				o[0] = stuecklisteeigenschaftDto.getStuecklisteeigenschaftartDto().getCBez();
				o[1] = stuecklisteeigenschaftDto.getCBez();
				al.add(o);
			}

			if (stuecklisteeigenschaftDtos.length > 0) {
				String[] fieldnames = new String[] { "F_EIGENSCHAFTART", "F_BEZEICHNUNG" };
				Object[][] dataSub = new Object[al.size()][fieldnames.length];
				dataSub = (Object[][]) al.toArray(dataSub);

				parameter.put("P_EIGENSCHAFTEN", new LPDatenSubreport(dataSub, fieldnames));
			}

		} catch (RemoteException ex4) {
			throwEJBExceptionLPRespectOld(ex4);
		}

		parameter.put("P_STUECKLISTE", dto.getArtikelDto().getCNr());
		parameter.put("P_ERFASSUNGSFAKTOR", dto.getNErfassungsfaktor());
		parameter.put("P_STUECKLISTEBEZEICHNUNG", dto.getArtikelDto().formatBezeichnung());
		parameter.put("P_EINHEIT", dto.getArtikelDto().getEinheitCNr());
		parameter.put("P_ANLEGEN", dto.getTAnlegen());
		parameter.put("P_AENDERN", dto.getTAendern());
		parameter.put("P_AENDERN_POSITION", dto.getTAendernposition());
		parameter.put("P_AENDERN_ARBEITSPLAN", dto.getTAendernarbeitsplan());
		if (bMitStuecklistenkommentar.booleanValue() == true) {
			parameter.put("P_KOMMENTAR", Helper.formatStyledTextForJasper(dto.getXKommentar()));
		}

		parameter.put("P_FREMDFERTIGUNG_AUFLOESEN", fremdfertigungAufloesen);

		parameter.put("P_FREIGABE_ZEITPUNKT", dto.getTFreigabe());
		if (dto.getPersonalIIdFreigabe() != null) {
			parameter.put("P_FREIGABE_PERSON", getPersonalFac()
					.personalFindByPrimaryKey(dto.getPersonalIIdFreigabe(), theClientDto).getCKurzzeichen());
		}

		parameter.put("P_LOSGROESSE", dto.getNLosgroesse());
		initJRDS(parameter, StuecklisteReportFac.REPORT_MODUL,
				StuecklisteReportFac.REPORT_STUECKLISTE_AUSGABESTUECKLSITE, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	public BigDecimal[] getLief1PreisDerLetzten2JahreInMandantenwaehrung(Integer artikelIId, BigDecimal nMenge,
			TheClientDto theClientDto) {
		BigDecimal[] bdPreise = new BigDecimal[2];

		Timestamp tVor2Jahren = new Timestamp(System.currentTimeMillis());
		tVor2Jahren = Helper.addiereTageZuTimestamp(tVor2Jahren, -730);

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT wep FROM FLRWareneingangspositionen AS wep WHERE wep.flrbestellposition.flrartikel.i_id="
				+ artikelIId + " AND wep.flrwareneingang.t_wareneingansdatum<='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(System.currentTimeMillis()))
				+ "' AND  wep.flrwareneingang.t_wareneingansdatum>='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tVor2Jahren.getTime()))
				+ "' AND wep.n_gelieferterpreis IS NOT NULL ";

		org.hibernate.Query hquery = session.createQuery(sQuery);

		List<?> resultList = hquery.list();
		Iterator<?> resultListIterator = resultList.iterator();

		TreeSet<BigDecimal> ts = new TreeSet<BigDecimal>();

		while (resultListIterator.hasNext()) {
			FLRWareneingangspositionen wepos = (FLRWareneingangspositionen) resultListIterator.next();

			BigDecimal geliefertPreis = wepos.getN_gelieferterpreis();

			// Mit Welchselkurs zu Mandantenwaehrung multiplizieren
			if (wepos.getFlrwareneingang().getN_wechselkurs() != null
					&& wepos.getFlrwareneingang().getN_wechselkurs().doubleValue() != 0) {
				geliefertPreis = wepos.getN_gelieferterpreis().divide(wepos.getFlrwareneingang().getN_wechselkurs(), 4,
						BigDecimal.ROUND_HALF_EVEN);

			}

			ts.add(geliefertPreis);

		}

		session.close();

		if (ts.size() > 0) {
			bdPreise[0] = ts.first();
			bdPreise[1] = ts.last();

		}

		return bdPreise;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public JasperPrintLP printGesamtkalkulationKonfigurator(Integer stuecklisteIId, BigDecimal nLosgroesse,
			boolean lief1PreisInKalkpreisUebernehmen, boolean bMitPreisenDerLetzten2Jahre,
			boolean unterstuecklistenVerdichten, boolean bUeberAlleMandanten, boolean bFremdfertigungAufloesen,
			Map<String, Object> konfigurationsWerte, Integer kundeIId_Uebersteuert,
			boolean minBSMengeUndVPEBeruecksichtigen, Double dMaterialgemeinkostenfaktor,
			Double dArbeitszeitgemeinkostenfaktor, Double dFertigungsgemeinkostenfaktor, Date preisGueltig,
			TheClientDto theClientDto) {
		if (!getMandantFac().hatZusatzfunktionStuecklisteMitFormeln(theClientDto)) {
			konfigurationsWerte = null;
		}
		return printGesamtkalkulationImpl(stuecklisteIId, nLosgroesse, lief1PreisInKalkpreisUebernehmen,
				bMitPreisenDerLetzten2Jahre, unterstuecklistenVerdichten, bUeberAlleMandanten, false,
				bFremdfertigungAufloesen, konfigurationsWerte, kundeIId_Uebersteuert, minBSMengeUndVPEBeruecksichtigen,
				dMaterialgemeinkostenfaktor, dArbeitszeitgemeinkostenfaktor, dArbeitszeitgemeinkostenfaktor, false,
				preisGueltig, theClientDto);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printGesamtkalkulation(Integer stuecklisteIId, BigDecimal nLosgroesse,
			boolean lief1PreisInKalkpreisUebernehmen, boolean bMitPreisenDerLetzten2Jahre,
			boolean unterstuecklistenVerdichten, boolean bUeberAlleMandanten, boolean nachArtikelCnrSortieren,
			boolean bFremdfertigungAufloesen, boolean minBSMengeUndVPEBeruecksichtigen,
			Double dMaterialgemeinkostenfaktor, Double dArbeitszeitgemeinkostenfaktor,
			Double dFertigungsgemeinkostenfaktor, boolean gesamtmengeMaterialBeruecksichtigen, Date preisGueltig,
			TheClientDto theClientDto) {
		return printGesamtkalkulationImpl(stuecklisteIId, nLosgroesse, lief1PreisInKalkpreisUebernehmen,
				bMitPreisenDerLetzten2Jahre, unterstuecklistenVerdichten, bUeberAlleMandanten, nachArtikelCnrSortieren,
				bFremdfertigungAufloesen, null, null, minBSMengeUndVPEBeruecksichtigen, dMaterialgemeinkostenfaktor,
				dArbeitszeitgemeinkostenfaktor, dFertigungsgemeinkostenfaktor, gesamtmengeMaterialBeruecksichtigen,
				preisGueltig, theClientDto);
	}

	private JasperPrintLP printGesamtkalkulationImpl(Integer stuecklisteIId, BigDecimal nLosgroesse,
			boolean lief1PreisInKalkpreisUebernehmen, boolean bMitPreisenDerLetzten2Jahre,
			boolean unterstuecklistenVerdichten, boolean bUeberAlleMandanten, boolean nachArtikelcnrsortieren,
			boolean bFremdfertigungAufloesen, Map<String, Object> konfigurationsWerte, Integer kundeIId_Uebersteuert,
			boolean minBSMengeUndVPEBeruecksichtigen, Double dMaterialgemeinkostenfaktor,
			Double dArbeitszeitgemeinkostenfaktor, Double dFertigungsgemeinkostenfaktor,
			boolean gesamtmengeMaterialBeruecksichtigen, Date preisGueltig, TheClientDto theClientDto) {

		if (stuecklisteIId == null || nLosgroesse == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("stuecklisteIId == null || nLosgroesse == null"));
		}
		if (nLosgroesse.doubleValue() <= 0) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ZAHL_ZU_KLEIN,
					new Exception("nLosgroesse.doubleValue()<=0"));
		}

		// Wenn kein Preisgueltigkeitsdatum angegeben, verwende heute
		if (preisGueltig == null) {
			preisGueltig = new Date(System.currentTimeMillis());
		}

		String mandantOri = theClientDto.getMandant();

		index = -1;
		sAktuellerReport = StuecklisteReportFac.REPORT_STUECKLISTE_GESAMTKALKULATION;
		// Mandantenwaehrung holen
		String mandantenWaehrung = null;
		try {
			mandantenWaehrung = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto)
					.getWaehrungCNr();
		} catch (RemoteException ex2) {
			throwEJBExceptionLPRespectOld(ex2);

		}

		// Mandantparameter holen
		ParametermandantDto parameter = null;
		boolean bGestpreisberechnungHauptlager = false;
		Integer hauptlagerIId = null;
		int iBasisKalkulation = 0;
		try {
			parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_STUECKLISTE_ARBEITSPLAN_ZEITEINHEIT);

			ParametermandantDto parameterGestpreisBerechnung = null;
			parameterGestpreisBerechnung = (ParametermandantDto) getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_GESTPREISBERECHNUNG_HAUPTLAGER);

			bGestpreisberechnungHauptlager = ((Boolean) parameterGestpreisBerechnung.getCWertAsObject()).booleanValue();

			hauptlagerIId = getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId();

			ParametermandantDto parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_BASIS_GESAMTKALKULATION);
			iBasisKalkulation = (Integer) parameterDto.getCWertAsObject();

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		String sEinheit = parameter.getCWert().trim();
		StuecklisteDto stklDto = null;
		KundeDto kundeDto = null;

		try {
			stklDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(stuecklisteIId, theClientDto);
			if (stklDto.getPartnerIId() != null) {
				kundeDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(stklDto.getPartnerIId(),
						theClientDto.getMandant(), theClientDto);
				if (kundeDto != null) {
					kundeDto.setPartnerDto(
							getPartnerFac().partnerFindByPrimaryKey(stklDto.getPartnerIId(), theClientDto));
				}
			} else if (kundeIId_Uebersteuert != null) {
				kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId_Uebersteuert, theClientDto);
			}

		} catch (RemoteException ex2) {
			throwEJBExceptionLPRespectOld(ex2);
		}

		// Losgreosse updaten
		getStuecklisteFac().updateStuecklisteLosgroesse(stuecklisteIId, nLosgroesse);

		List<?> n = new ArrayList<StuecklisteMitStrukturDto>();
		if (konfigurationsWerte != null) {
			ScriptRuntime rt = prepareRuntime(new DebuggingScriptRuntime(), konfigurationsWerte);
			rt.beGesamtkalkulation();

			Connection c = getReportConnectionFacLocal().getConnectionWithEjbEx(theClientDto.getIDUser());
			ReportSqlExecutor sqlExecutor = new ReportSqlExecutor(c);
			rt.setSql(sqlExecutor);

			StrukturDatenParamDto paramDto = new StrukturDatenParamDto();
			paramDto.setSortierung(nachArtikelcnrsortieren ? Sort.Artikelnummer : Sort.Ohne).setLosgroesse(nLosgroesse)
					.setUeberAlleMandanten(bUeberAlleMandanten).beMitUnterstuecklisten()
					.setGleichePositionenZusammenfassen(false).setRuntime(rt);

			// paramDto = getStuecklisteFac()
			// .getStrukturDatenEinerStuecklisteMitArbeitsplanNew(
			// stuecklisteIId, paramDto, theClientDto);
			paramDto = stuecklisteFacLocalBean.getStrukturDatenEinerStuecklisteMitArbeitsplanNew(stuecklisteIId,
					paramDto, theClientDto);
			n = paramDto.getStrukturMap();
			sqlExecutor.close();
			try {
				getReportConnectionFacLocal().closeConnection(theClientDto.getIDUser(), null);
			} catch (SQLException e) {
				myLogger.error("Closing physical connection", e);
			}
		} else {
			try {
				n = getStuecklisteFac().getStrukturDatenEinerStuecklisteMitArbeitsplan(stuecklisteIId, theClientDto,
						nachArtikelcnrsortieren ? StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_ARTIKELNR
								: StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_OHNE,
						0, null, true, true, nLosgroesse, null, bUeberAlleMandanten, bFremdfertigungAufloesen,
						minBSMengeUndVPEBeruecksichtigen, null);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}

		List<StuecklisteMitStrukturDto> m = (List<StuecklisteMitStrukturDto>) n;
		Iterator<StuecklisteMitStrukturDto> it = m.listIterator();

		ArrayList alDaten = new ArrayList();

		data = new Object[m.size()][REPORT_GESAMTKALKULATION_ANZAHL_SPALTEN];

		BigDecimal bdArbeitszeitkosten = new BigDecimal(0);
		BigDecimal bdRuestzeitMannGesamt = new BigDecimal(0);
		BigDecimal bdZeitMannGesamt = new BigDecimal(0);
		BigDecimal bdRuestzeitMaschineGesamt = new BigDecimal(0);
		BigDecimal bdZeitMaschineGesamt = new BigDecimal(0);
		BigDecimal bdMaschinenzeitkosten = new BigDecimal(0);

		BigDecimal bdStueckZeitMannGesamt = new BigDecimal(0);
		BigDecimal bdStueckZeitMaschineGesamt = new BigDecimal(0);

		BigDecimal bdRuestzeitMannExterneAGGesamt = new BigDecimal(0);
		BigDecimal bdRuestzeitMaschineExterneAGGesamt = new BigDecimal(0);

		// POSITIONEN

		BigDecimal bdMaterialkosten = new BigDecimal(0);
		BigDecimal bdMaterialkostenLief = new BigDecimal(0);
		BigDecimal bdFixkostenLief = new BigDecimal(0);

		HashMap<Integer, BigDecimal> hmHoechsteDurchlaufzeitEinerEbene = new HashMap<Integer, BigDecimal>();

		int iMaxAnzahlArbeitsschritte = 0;

		HashMap<Integer, BigDecimal> hmMaterialVerdichtetNachArtikel = new HashMap<Integer, BigDecimal>();
		while (it.hasNext()) {
			StuecklisteMitStrukturDto struktur = it.next();
			if (!struktur.isBArbeitszeit()) {
				StuecklistepositionDto position = struktur.getStuecklistepositionDto();

				BigDecimal mengeZeile = position.getNZielmenge(nLosgroesse, minBSMengeUndVPEBeruecksichtigen);

				if (hmMaterialVerdichtetNachArtikel.containsKey(position.getArtikelIId())) {
					BigDecimal mengeGesamt = hmMaterialVerdichtetNachArtikel.get(position.getArtikelIId())
							.add(mengeZeile);
					hmMaterialVerdichtetNachArtikel.put(position.getArtikelIId(), mengeGesamt);
				} else {
					hmMaterialVerdichtetNachArtikel.put(position.getArtikelIId(), mengeZeile);
				}

			}
		}

		it = m.listIterator();

		while (it.hasNext()) {
			StuecklisteMitStrukturDto struktur = it.next();

			String einrueckung = "";
			for (int i = 0; i < struktur.getIEbene(); i++) {
				einrueckung = einrueckung + "   ";
			}

			Object[] zeile = new Object[REPORT_GESAMTKALKULATION_ANZAHL_SPALTEN];

			zeile[REPORT_GESAMTKALKULATION_I_EBENE] = struktur.getIEbene();

			zeile[REPORT_GESAMTKALKULATION_FREIGABE_PERSON] = struktur.getCKurzzeichenPersonFreigabe();

			zeile[REPORT_GESAMTKALKULATION_FREIGABE_ZEITPUNKT] = struktur.getTFreigabe();
			zeile[REPORT_GESAMTKALKULATION_ANZAHL_ARBEITSSCHRITTE] = struktur.getiAnzahlArbeitsschritte();
			if (struktur.getiAnzahlArbeitsschritte() != null
					&& struktur.getiAnzahlArbeitsschritte() > iMaxAnzahlArbeitsschritte) {
				iMaxAnzahlArbeitsschritte = struktur.getiAnzahlArbeitsschritte();
			}

			zeile[REPORT_GESAMTKALKULATION_MANDANT] = struktur.getMandantCNr();
			zeile[REPORT_GESAMTKALKULATION_MANDANT_KBEZ] = struktur.getMandantCKbez();
			// Wenn Position
			if (!struktur.isBArbeitszeit()) {
				StuecklistepositionDto position = struktur.getStuecklistepositionDto();

				zeile[REPORT_GESAMTKALKULATION_RUESTMENGE] = Helper.short2Boolean(position.getBRuestmenge());

				zeile[REPORT_GESAMTKALKULATION_INITITALKOSTEN] = Helper.short2Boolean(position.getBInitial());

				zeile[REPORT_GESAMTKALKULATION_FORMEL] = position.getXFormel();

				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(position.getArtikelIId(),
						theClientDto);

				// SP6052
				zeile[REPORT_GESAMTKALKULATION_ARTIKELART] = artikelDto.getArtikelartCNr();

				zeile[REPORT_GESAMTKALKULATION_MELDEPFLICHTIG] = Helper.short2Boolean(artikelDto.getBMeldepflichtig());
				zeile[REPORT_GESAMTKALKULATION_BEWILLIGUNGSPFLICHTIG] = Helper
						.short2Boolean(artikelDto.getBBewilligungspflichtig());

				// PJ21917
				if (artikelDto.getLandIIdUrsprungsland() != null) {
					LandDto landDto = getSystemFac().landFindByPrimaryKey(artikelDto.getLandIIdUrsprungsland());
					zeile[REPORT_GESAMTKALKULATION_URSPRUNGSLAND_LKZ] = landDto.getCLkz();
					zeile[REPORT_GESAMTKALKULATION_URSPRUNGSLAND_NAME] = landDto.getCName();
				}

				if (artikelDto.getIExternerArbeitsgang() > 0) {
					zeile[REPORT_GESAMTKALKULATION_EXTERNER_ARBEITSGANG] = Boolean.TRUE;
				} else {
					zeile[REPORT_GESAMTKALKULATION_EXTERNER_ARBEITSGANG] = Boolean.FALSE;
				}

				if (artikelDto.getArtikelartCNr() != null && artikelDto.getArtikelartCNr()
						.equals(com.lp.server.artikel.service.ArtikelFac.ARTIKELART_HANDARTIKEL)) {

					zeile[REPORT_GESAMTKALKULATION_ARTIKEL] = "";

				} else {
					zeile[REPORT_GESAMTKALKULATION_ARTIKEL] = einrueckung + artikelDto.getCNr();
					zeile[REPORT_GESAMTKALKULATION_ARTIKELREFERENZNUMMER] = artikelDto.getCReferenznr();

					if (artikelDto.getCWarenverkehrsnummer() != null) {
						int i = 0;
					}

					zeile[REPORT_GESAMTKALKULATION_WARENVERKEHRSNUMMER] = artikelDto.getCWarenverkehrsnummer();

					if (artikelDto.getArtgruIId() != null) {

						zeile[REPORT_GESAMTKALKULATION_ARTIKELGRUPPE] = getArtikelFac()
								.artgruFindByPrimaryKey(artikelDto.getArtgruIId(), theClientDto).getBezeichnung();

					}

				}

				zeile[REPORT_GESAMTKALKULATION_GEWICHT] = artikelDto.getFGewichtkg();
				zeile[REPORT_GESAMTKALKULATION_FERTIGUNGSSATZGROESSE] = artikelDto.getFFertigungssatzgroesse();
				zeile[REPORT_GESAMTKALKULATION_MATERIALGEWICHT] = artikelDto.getFMaterialgewicht();

				if (artikelDto.getArtikelsprDto() != null) {
					zeile[REPORT_GESAMTKALKULATION_ARTIKELBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
					zeile[REPORT_GESAMTKALKULATION_ARTIKELZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCZbez();
					zeile[REPORT_GESAMTKALKULATION_ARTIKELZUSATZBEZEICHNUNG2] = artikelDto.getArtikelsprDto()
							.getCZbez2();
					zeile[REPORT_GESAMTKALKULATION_ARTIKELKURZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCKbez();
				}

				if (Helper.short2boolean(position.getBRuestmenge())) {
					zeile[REPORT_GESAMTKALKULATION_MENGE] = Helper
							.rundeKaufmaennisch(position.getNZielmenge(minBSMengeUndVPEBeruecksichtigen), 4);

					zeile[REPORT_GESAMTKALKULATION_MENGE_OHNE_VPE] = Helper
							.rundeKaufmaennisch(position.getNZielmenge(false), 4);

				} else {
					zeile[REPORT_GESAMTKALKULATION_MENGE] = Helper.rundeKaufmaennisch(
							position.getNZielmenge(nLosgroesse, minBSMengeUndVPEBeruecksichtigen), 4);

					zeile[REPORT_GESAMTKALKULATION_MENGE_OHNE_VPE] = Helper
							.rundeKaufmaennisch(position.getNZielmenge(nLosgroesse), 4);

				}

				zeile[REPORT_GESAMTKALKULATION_KOMMENTAR] = position.getCKommentar();

				zeile[REPORT_GESAMTKALKULATION_HIERARCHIEMENGE] = position.getHierarchiemenge_NOT_IN_DB();

				zeile[REPORT_GESAMTKALKULATION_MENGENEINHEIT] = artikelDto.getEinheitCNr().trim();
				zeile[REPORT_GESAMTKALKULATION_MINDESTDECKUNGSBEITRAG] = artikelDto.getFMindestdeckungsbeitrag();
				zeile[REPORT_GESAMTKALKULATION_DURCHLAUFZEIT] = struktur.getDurchlaufzeit();

				// Hoechste durclaufzeit pro ebene ermitteln
				if (struktur.getDurchlaufzeit() != null) {
					if (hmHoechsteDurchlaufzeitEinerEbene.containsKey(struktur.getIEbene())) {
						BigDecimal value = (BigDecimal) hmHoechsteDurchlaufzeitEinerEbene.get(struktur.getIEbene());
						if (value.doubleValue() < struktur.getDurchlaufzeit().doubleValue()) {
							hmHoechsteDurchlaufzeitEinerEbene.put(struktur.getIEbene(), struktur.getDurchlaufzeit());
						}
					} else {
						hmHoechsteDurchlaufzeitEinerEbene.put(struktur.getIEbene(), struktur.getDurchlaufzeit());
					}
				}

				// Kalkpreis (fuer CNC-Rettenbacher)
				if (position.getNKalkpreis() != null) {
					zeile[REPORT_GESAMTKALKULATION_KALKPREIS] = position.getNKalkpreis();
					zeile[REPORT_GESAMTKALKULATION_KALKWERT] = position.getNKalkpreis()
							.multiply(position.getNZielmenge(nLosgroesse, minBSMengeUndVPEBeruecksichtigen));
				}

				// VKPreise
				if (stklDto.getPartnerIId() != null) {
					if (kundeDto != null) {

						if (kundeDto.getMwstsatzbezIId() != null) {
							Timestamp belegDatum = Helper.cutTimestamp(getTimestamp());
							MwstsatzDto mwstsatzKunde = getMandantFac()
									.mwstsatzZuDatumValidate(kundeDto.getMwstsatzbezIId(), belegDatum, theClientDto);

							BigDecimal mengeFuerVKPreis = position.getNZielmenge(nLosgroesse,
									minBSMengeUndVPEBeruecksichtigen);
							if (gesamtmengeMaterialBeruecksichtigen
									&& hmMaterialVerdichtetNachArtikel.containsKey(position.getArtikelIId())) {
								mengeFuerVKPreis = hmMaterialVerdichtetNachArtikel.get(position.getArtikelIId());
							}

							VkpreisfindungDto vkpreisDto = getVkPreisfindungFac().verkaufspreisfindung(
									artikelDto.getIId(), kundeDto.getIId(), mengeFuerVKPreis,
									new java.sql.Date(belegDatum.getTime()),
									kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
//										getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(
//												kundeDto.getMwstsatzbezIId(), theClientDto).getIId(),
									mwstsatzKunde.getIId(), theClientDto.getSMandantenwaehrung(), theClientDto);

							VerkaufspreisDto kundenVKPreisDto = Helper.getVkpreisBerechnet(vkpreisDto);

							if (kundenVKPreisDto != null && kundenVKPreisDto.nettopreis != null) {

								zeile[REPORT_GESAMTKALKULATION_VKPREIS] = kundenVKPreisDto.nettopreis;
								zeile[REPORT_GESAMTKALKULATION_MATERIALZUSCHLAG] = kundenVKPreisDto.bdMaterialzuschlag;

								// Lt. WH 15.4.2014 auf 2 Stellen runden
								zeile[REPORT_GESAMTKALKULATION_VKWERT] = Helper.rundeKaufmaennisch(
										position.getNZielmenge(nLosgroesse, minBSMengeUndVPEBeruecksichtigen)
												.multiply(kundenVKPreisDto.nettopreis),
										2);
							}

						}
					}
				}

				// SP4608
				zeile[REPORT_GESAMTKALKULATION_MATERIALZUSCHLAG_EK] = getMaterialFac()
						.getMaterialzuschlagEKInZielwaehrung(artikelDto.getIId(), null,
								new java.sql.Date(System.currentTimeMillis()), theClientDto.getSMandantenwaehrung(),
								theClientDto);

				if (zeile[REPORT_GESAMTKALKULATION_MATERIALZUSCHLAG] == null) {
					zeile[REPORT_GESAMTKALKULATION_MATERIALZUSCHLAG] = getMaterialFac()
							.getMaterialzuschlagVKInZielwaehrung(artikelDto.getIId(), null,
									new java.sql.Date(System.currentTimeMillis()), theClientDto.getSMandantenwaehrung(),
									theClientDto);
				}

				try {

					// PJ20515 Preisbasis
					zeile[REPORT_GESAMTKALKULATION_VK_PREISBASIS_HEUTE] = getVkPreisfindungFac().ermittlePreisbasis(
							artikelDto.getIId(), new java.sql.Date(System.currentTimeMillis()), null,
							theClientDto.getSMandantenwaehrung(), theClientDto);

					// PJ17371 bzw. PJ18978

					if (position.getArtikelIId() == 9233) {
						int z = 0;
					}

					BigDecimal bdMengeFuerLief1Preis = position.getNZielmenge(nLosgroesse,
							minBSMengeUndVPEBeruecksichtigen);

					if (gesamtmengeMaterialBeruecksichtigen
							&& hmMaterialVerdichtetNachArtikel.containsKey(position.getArtikelIId())) {
						bdMengeFuerLief1Preis = hmMaterialVerdichtetNachArtikel.get(position.getArtikelIId());
					}

					if (iBasisKalkulation == 1 && artikelDto.getFFertigungssatzgroesse() != null
							&& artikelDto.getFFertigungssatzgroesse().doubleValue() > 0) {
						bdMengeFuerLief1Preis = new BigDecimal(Math
								.ceil(bdMengeFuerLief1Preis.doubleValue()
										/ artikelDto.getFFertigungssatzgroesse().doubleValue())
								* artikelDto.getFFertigungssatzgroesse().doubleValue());

					}

					// TODO PJ22107 Gueltigkeitsdatum verwenden
					ArtikellieferantDto artikellieferantDto = getArtikelFac().getArtikelEinkaufspreis(
							artikelDto.getIId(), null, bdMengeFuerLief1Preis, mandantenWaehrung, preisGueltig,
							theClientDto);
					if (artikellieferantDto != null && artikellieferantDto.getLief1Preis() != null) {
						zeile[REPORT_GESAMTKALKULATION_LIEF1PREIS] = artikellieferantDto.getLief1Preis();
						zeile[REPORT_GESAMTKALKULATION_LIEF1PREIS_GUELTIGAB] = artikellieferantDto.getTPreisgueltigab();
						zeile[REPORT_GESAMTKALKULATION_LIEF1PREISGESAMT] = position
								.getNZielmenge(nLosgroesse, minBSMengeUndVPEBeruecksichtigen)
								.multiply(artikellieferantDto.getLief1Preis());
						zeile[REPORT_GESAMTKALKULATION_WIEDERBESCHAFFUNGSZEIT] = artikellieferantDto
								.getIWiederbeschaffungszeit();
						zeile[REPORT_GESAMTKALKULATION_MINDESTBESTELLMENGE] = artikellieferantDto
								.getFMindestbestelmenge();
						if (artikellieferantDto.getNFixkosten() != null) {
							zeile[REPORT_GESAMTKALKULATION_FIXKOSTEN] = artikellieferantDto.getNFixkosten();
						} else {
							zeile[REPORT_GESAMTKALKULATION_FIXKOSTEN] = new BigDecimal(0);
						}

						if (artikellieferantDto.getLieferantDto() != null
								&& artikellieferantDto.getLieferantDto().getPartnerDto() != null) {
							zeile[REPORT_GESAMTKALKULATION_LIEF1_KBEZ] = artikellieferantDto.getLieferantDto()
									.getPartnerDto().getCKbez();
							zeile[REPORT_GESAMTKALKULATION_LIEF1_NAME] = artikellieferantDto.getLieferantDto()
									.getPartnerDto().formatFixName1Name2();
						}

					} else if (artikellieferantDto == null
							&& artikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)
							&& position.getNKalkpreis() != null) {
						//SP9843 Wenn Handeingabe und Kalkpreis vorhanden, dann wird dieser als Lief1Preis verwendet
						zeile[REPORT_GESAMTKALKULATION_LIEF1PREIS] = position.getNKalkpreis();
						zeile[REPORT_GESAMTKALKULATION_LIEF1PREISGESAMT] = position.getNKalkpreis()
								.multiply(position.getNZielmenge(nLosgroesse, minBSMengeUndVPEBeruecksichtigen));
						zeile[REPORT_GESAMTKALKULATION_FIXKOSTEN] = new BigDecimal(0);
					} else {
						zeile[REPORT_GESAMTKALKULATION_LIEF1PREIS] = new BigDecimal(0);
						zeile[REPORT_GESAMTKALKULATION_LIEF1PREISGESAMT] = new BigDecimal(0);
						zeile[REPORT_GESAMTKALKULATION_FIXKOSTEN] = new BigDecimal(0);
					}

					// PJ18427 Lief1Preis zurueckschreiben

					if (lief1PreisInKalkpreisUebernehmen) {

						StuecklistepositionDto stklPosDtoTemp = getStuecklisteFac()
								.stuecklistepositionFindByPrimaryKey(position.getIId(), theClientDto);
						if (stklPosDtoTemp.getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)) {

							stklPosDtoTemp.setNKalkpreis((BigDecimal) zeile[REPORT_GESAMTKALKULATION_LIEF1PREIS]);
							getStuecklisteFac().updateStuecklisteposition(stklPosDtoTemp, theClientDto);
						}
					}

					if (bMitPreisenDerLetzten2Jahre == true) {

						BigDecimal[] geliefertpreise = getLief1PreisDerLetzten2JahreInMandantenwaehrung(
								artikelDto.getIId(),
								position.getNZielmenge(nLosgroesse, minBSMengeUndVPEBeruecksichtigen), theClientDto);

						zeile[REPORT_GESAMTKALKULATION_GROESSTER_LIEF1PREIS_2JAHRE] = geliefertpreise[1];
						zeile[REPORT_GESAMTKALKULATION_KLEINSTER_LIEF1PREIS_2JAHRE] = geliefertpreise[0];
					}

					BigDecimal bdGestpreis = null;

					theClientDto.setMandant(mandantOri);
					if (bUeberAlleMandanten == true && struktur.getMandantCNr() != null)
						theClientDto.setMandant(struktur.getMandantCNr());
					{
						hauptlagerIId = getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId();

					}

					if (bGestpreisberechnungHauptlager == true) {
						bdGestpreis = getLagerFac().getGemittelterGestehungspreisEinesLagers(artikelDto.getIId(),
								hauptlagerIId, theClientDto);

					} else {
						bdGestpreis = getLagerFac().getGemittelterGestehungspreisAllerLaegerEinesMandanten(
								artikelDto.getIId(), theClientDto);

					}

					// wieder zuruecksetzen
					theClientDto.setMandant(mandantOri);

					if (!struktur.isBStueckliste()) {
						zeile[REPORT_GESAMTKALKULATION_FREMDGEFERTIGT] = struktur.isBFremdfertigung();

						zeile[REPORT_GESAMTKALKULATION_GESTPREIS] = bdGestpreis;
						zeile[REPORT_GESAMTKALKULATION_GESTWERT] = bdGestpreis
								.multiply(position.getNZielmenge(nLosgroesse, minBSMengeUndVPEBeruecksichtigen));

					} else {

						if (struktur.getStuecklisteDto() != null) {
							zeile[REPORT_GESAMTKALKULATION_STKLART] = struktur.getStuecklisteDto()
									.getStuecklisteartCNr();

							zeile[REPORT_GESAMTKALKULATION_REIHENFOLGE] = struktur.getStuecklisteDto()
									.getIReihenfolge();

							zeile[REPORT_GESAMTKALKULATION_FREMDGEFERTIGT] = Helper
									.short2Boolean(struktur.getStuecklisteDto().getBFremdfertigung());
							zeile[REPORT_GESAMTKALKULATION_LAGERSTANDSDETAIL] = Helper
									.short2Boolean(struktur.getStuecklisteDto().getBDruckeinlagerstandsdetail());

						}

						zeile[REPORT_GESAMTKALKULATION_GESTPREISWENNSTUECKLISTE] = bdGestpreis;

					}
					if (artikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
						bdArbeitszeitkosten = bdArbeitszeitkosten
								.add((BigDecimal) zeile[REPORT_GESAMTKALKULATION_GESTWERT]);

						if (artikelDto.getIExternerArbeitsgang() == 0) {
							bdZeitMannGesamt = bdZeitMannGesamt
									.add(position.getNZielmenge(nLosgroesse, minBSMengeUndVPEBeruecksichtigen));
						}

						if (Helper.short2boolean(position.getBRuestmenge())) {
							if (artikelDto.getIExternerArbeitsgang() > 0) {
								bdRuestzeitMannExterneAGGesamt = bdRuestzeitMannExterneAGGesamt
										.add(position.getNZielmenge(nLosgroesse, minBSMengeUndVPEBeruecksichtigen));
							} else {
								bdRuestzeitMannGesamt = bdRuestzeitMannGesamt
										.add(position.getNZielmenge(nLosgroesse, minBSMengeUndVPEBeruecksichtigen));
							}
						} else {
							if (artikelDto.getIExternerArbeitsgang() == 0) {
								bdStueckZeitMannGesamt = bdStueckZeitMannGesamt
										.add(position.getNZielmenge(nLosgroesse, minBSMengeUndVPEBeruecksichtigen));
							}
						}

						if (sEinheit.equals(SystemFac.EINHEIT_STUNDE.trim())) {
							zeile[REPORT_GESAMTKALKULATION_STUECKZEIT] = position.getNMenge();
						} else if (sEinheit.equals(SystemFac.EINHEIT_MINUTE.trim())) {
							zeile[REPORT_GESAMTKALKULATION_STUECKZEIT] = position.getNMenge()
									.multiply(new BigDecimal(60));
						} else if (sEinheit.equals(SystemFac.EINHEIT_SEKUNDE.trim())) {
							zeile[REPORT_GESAMTKALKULATION_STUECKZEIT] = position.getNMenge()
									.multiply(new BigDecimal(3600));
						}
					} else {
						if (zeile[REPORT_GESAMTKALKULATION_GESTWERT] != null) {
							bdMaterialkosten = bdMaterialkosten
									.add((BigDecimal) zeile[REPORT_GESAMTKALKULATION_GESTWERT]);
						}
						if (zeile[REPORT_GESAMTKALKULATION_LIEF1PREISGESAMT] != null
								&& zeile[REPORT_GESAMTKALKULATION_GESTPREISWENNSTUECKLISTE] == null) {
							bdMaterialkostenLief = bdMaterialkostenLief
									.add((BigDecimal) zeile[REPORT_GESAMTKALKULATION_LIEF1PREISGESAMT]);
						}

						if (zeile[REPORT_GESAMTKALKULATION_FIXKOSTEN] != null
								&& zeile[REPORT_GESAMTKALKULATION_GESTPREISWENNSTUECKLISTE] == null) {

							// SP6514
							if (((BigDecimal) zeile[REPORT_GESAMTKALKULATION_MENGE]).doubleValue() != 0
									&& ((BigDecimal) zeile[REPORT_GESAMTKALKULATION_FIXKOSTEN]).doubleValue() != 0) {
								bdFixkostenLief = bdFixkostenLief
										.add((BigDecimal) zeile[REPORT_GESAMTKALKULATION_FIXKOSTEN]);
							}
						}
					}
				} catch (RemoteException ex1) {
					throwEJBExceptionLPRespectOld(ex1);
				}

				zeile[REPORT_GESAMTKALKULATION_POS_MENGE] = position.getNMenge();
				zeile[REPORT_GESAMTKALKULATION_POS_MENGENEINHEIT] = position.getEinheitCNr();
				zeile[REPORT_GESAMTKALKULATION_DIMENSION1] = position.getFDimension1();
				zeile[REPORT_GESAMTKALKULATION_DIMENSION2] = position.getFDimension2();
				zeile[REPORT_GESAMTKALKULATION_DIMENSION3] = position.getFDimension3();
				zeile[REPORT_GESAMTKALKULATION_LOG_DEBUG] = struktur.getReportDebug();
				zeile[REPORT_GESAMTKALKULATION_LOG_INFO] = struktur.getReportInfo();
				zeile[REPORT_GESAMTKALKULATION_LOG_WARN] = struktur.getReportWarn();
				zeile[REPORT_GESAMTKALKULATION_LOG_ERROR] = struktur.getReportError();

			} else {

				StuecklistearbeitsplanDto stuecklistearbeitsplanDto = struktur.getStuecklistearbeitsplanDto();

				zeile[REPORT_GESAMTKALKULATION_FORMEL] = stuecklistearbeitsplanDto.getXFormel();
				zeile[REPORT_GESAMTKALKULATION_LOG_DEBUG] = struktur.getReportDebug();
				zeile[REPORT_GESAMTKALKULATION_LOG_INFO] = struktur.getReportInfo();
				zeile[REPORT_GESAMTKALKULATION_LOG_WARN] = struktur.getReportWarn();
				zeile[REPORT_GESAMTKALKULATION_LOG_ERROR] = struktur.getReportError();

				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(stuecklistearbeitsplanDto.getArtikelIId(), theClientDto);

				// SP6052
				zeile[REPORT_GESAMTKALKULATION_ARTIKELART] = artikelDto.getArtikelartCNr();

				zeile[REPORT_GESAMTKALKULATION_MELDEPFLICHTIG] = Helper.short2Boolean(artikelDto.getBMeldepflichtig());
				zeile[REPORT_GESAMTKALKULATION_BEWILLIGUNGSPFLICHTIG] = Helper
						.short2Boolean(artikelDto.getBBewilligungspflichtig());

				// PJ21917
				if (artikelDto.getLandIIdUrsprungsland() != null) {
					LandDto landDto = getSystemFac().landFindByPrimaryKey(artikelDto.getLandIIdUrsprungsland());
					zeile[REPORT_GESAMTKALKULATION_URSPRUNGSLAND_LKZ] = landDto.getCLkz();
					zeile[REPORT_GESAMTKALKULATION_URSPRUNGSLAND_NAME] = landDto.getCName();
				}

				if (artikelDto.getIExternerArbeitsgang() > 0) {
					zeile[REPORT_GESAMTKALKULATION_EXTERNER_ARBEITSGANG] = Boolean.TRUE;
				} else {
					zeile[REPORT_GESAMTKALKULATION_EXTERNER_ARBEITSGANG] = Boolean.FALSE;
				}

				if (artikelDto.getArtikelartCNr() != null && artikelDto.getArtikelartCNr()
						.equals(com.lp.server.artikel.service.ArtikelFac.ARTIKELART_HANDARTIKEL)) {
					zeile[REPORT_GESAMTKALKULATION_ARTIKEL] = "";
				} else {
					zeile[REPORT_GESAMTKALKULATION_ARTIKEL] = einrueckung + artikelDto.getCNr();
					zeile[REPORT_GESAMTKALKULATION_ARTIKELREFERENZNUMMER] = artikelDto.getCReferenznr();

					if (artikelDto.getArtgruIId() != null) {
						zeile[REPORT_GESAMTKALKULATION_ARTIKELGRUPPE] = getArtikelFac()
								.artgruFindByPrimaryKey(artikelDto.getArtgruIId(), theClientDto).getBezeichnung();

					}
				}

				if (artikelDto.getArtikelsprDto() != null) {
					zeile[REPORT_GESAMTKALKULATION_ARTIKELBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
					zeile[REPORT_GESAMTKALKULATION_ARTIKELZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCZbez();
					zeile[REPORT_GESAMTKALKULATION_ARTIKELZUSATZBEZEICHNUNG2] = artikelDto.getArtikelsprDto()
							.getCZbez2();
				}

				zeile[REPORT_GESAMTKALKULATION_MINDESTDECKUNGSBEITRAG] = artikelDto.getFMindestdeckungsbeitrag();
				zeile[REPORT_GESAMTKALKULATION_MENGENEINHEIT] = artikelDto.getEinheitCNr().trim();

				BigDecimal bdGesamtzeit = Helper.berechneGesamtzeitInStunden(stuecklistearbeitsplanDto.getLRuestzeit(),
						stuecklistearbeitsplanDto.getLStueckzeit(), nLosgroesse, null,
						stuecklistearbeitsplanDto.getIAufspannung());

				zeile[REPORT_GESAMTKALKULATION_INITITALKOSTEN] = Helper
						.short2Boolean(stuecklistearbeitsplanDto.getBInitial());

				zeile[REPORT_GESAMTKALKULATION_MENGE] = bdGesamtzeit;

				try {

					// PJ20515 Preisbasis
					zeile[REPORT_GESAMTKALKULATION_VK_PREISBASIS_HEUTE] = getVkPreisfindungFac().ermittlePreisbasis(
							artikelDto.getIId(), new java.sql.Date(System.currentTimeMillis()), null,
							theClientDto.getSMandantenwaehrung(), theClientDto);

					// Wenn Maschinenzeit, dann statt Artikelnr und Bezeichnung,
					// Maschinenr und Bezeichnung einbauen

					if (!struktur.isBMaschinenzeit()) {

						ArtikellieferantDto artikellieferantDto = getArtikelFac()
								.getArtikelEinkaufspreisDesBevorzugtenLieferanten(artikelDto.getIId(), bdGesamtzeit,
										mandantenWaehrung, theClientDto);

						if (artikellieferantDto != null && artikellieferantDto.getLief1Preis() != null) {
							zeile[REPORT_GESAMTKALKULATION_GESTPREIS] = artikellieferantDto.getLief1Preis();
							zeile[REPORT_GESAMTKALKULATION_GESTWERT] = artikellieferantDto.getLief1Preis()
									.multiply(bdGesamtzeit);
							bdArbeitszeitkosten = bdArbeitszeitkosten
									.add(artikellieferantDto.getLief1Preis().multiply(bdGesamtzeit));
						} else {
							zeile[REPORT_GESAMTKALKULATION_GESTPREIS] = new BigDecimal(0);
							zeile[REPORT_GESAMTKALKULATION_GESTWERT] = new BigDecimal(0);
						}
					} else {
						com.lp.server.personal.service.MaschineDto maschineDto = getZeiterfassungFac()
								.maschineFindByPrimaryKey(stuecklistearbeitsplanDto.getMaschineIId());

						zeile[REPORT_GESAMTKALKULATION_ARTIKEL] = einrueckung + "M:"
								+ maschineDto.getCIdentifikationsnr();

						zeile[REPORT_GESAMTKALKULATION_ARTIKELBEZEICHNUNG] = maschineDto.getCBez();

						zeile[REPORT_GESAMTKALKULATION_MENGENEINHEIT] = SystemFac.EINHEIT_STUNDE.trim();

						BigDecimal maschinenkosten = getZeiterfassungFac()
								.getMaschinenKostenZumZeitpunkt(maschineDto.getIId(),
										new java.sql.Timestamp(System.currentTimeMillis()),
										LocaleFac.BELEGART_STUECKLISTE, stuecklistearbeitsplanDto.getIId())
								.getBdStundensatz();

						bdMaschinenzeitkosten = bdMaschinenzeitkosten.add(maschinenkosten.multiply(bdGesamtzeit));

						zeile[REPORT_GESAMTKALKULATION_GESTPREIS] = maschinenkosten;
						zeile[REPORT_GESAMTKALKULATION_GESTWERT] = maschinenkosten.multiply(bdGesamtzeit);
					}
				} catch (RemoteException ex1) {
					throwEJBExceptionLPRespectOld(ex1);
				}

				BigDecimal bdStueckzeit = null;
				if (nLosgroesse.doubleValue() != 0) {
					bdStueckzeit = bdGesamtzeit.divide(nLosgroesse, 8, BigDecimal.ROUND_HALF_EVEN);
				} else {
					bdStueckzeit = new BigDecimal(0);
				}

				double dRuestzeit = 0;
				double lRuestzeit = stuecklistearbeitsplanDto.getLRuestzeit().longValue();

				if (stuecklistearbeitsplanDto.getMaschineIId() == null) {
					if (artikelDto.getIExternerArbeitsgang() == 0) {
						bdZeitMannGesamt = bdZeitMannGesamt.add(bdStueckzeit);
						bdStueckZeitMannGesamt = bdStueckZeitMannGesamt.add(bdGesamtzeit);
					}

				} else {
					if (artikelDto.getIExternerArbeitsgang() == 0) {
						bdZeitMaschineGesamt = bdZeitMaschineGesamt.add(bdStueckzeit);
						bdStueckZeitMaschineGesamt = bdStueckZeitMaschineGesamt.add(bdGesamtzeit);
					}

				}
				if (sEinheit.equals(SystemFac.EINHEIT_STUNDE.trim())) {
					dRuestzeit = lRuestzeit / 3600000;
					if (stuecklistearbeitsplanDto.getMaschineIId() == null) {
						if (artikelDto.getIExternerArbeitsgang() > 0) {
							bdRuestzeitMannExterneAGGesamt = bdRuestzeitMannExterneAGGesamt
									.add(new BigDecimal(dRuestzeit));
						} else {
							bdRuestzeitMannGesamt = bdRuestzeitMannGesamt.add(new BigDecimal(dRuestzeit));
						}

					} else {
						if (artikelDto.getIExternerArbeitsgang() > 0) {
							bdRuestzeitMaschineExterneAGGesamt = bdRuestzeitMaschineExterneAGGesamt
									.add(new BigDecimal(dRuestzeit));
						} else {
							bdRuestzeitMaschineGesamt = bdRuestzeitMaschineGesamt.add(new BigDecimal(dRuestzeit));
						}

					}
				} else if (sEinheit.equals(SystemFac.EINHEIT_MINUTE.trim())) {
					bdStueckzeit = bdStueckzeit.multiply(new BigDecimal(60));
					dRuestzeit = lRuestzeit / 60000;
					if (stuecklistearbeitsplanDto.getMaschineIId() == null) {

						if (artikelDto.getIExternerArbeitsgang() > 0) {
							bdRuestzeitMannExterneAGGesamt = bdRuestzeitMannExterneAGGesamt
									.add(new BigDecimal(dRuestzeit / 60));
						} else {
							bdRuestzeitMannGesamt = bdRuestzeitMannGesamt.add(new BigDecimal(dRuestzeit / 60));
						}

					} else {
						if (artikelDto.getIExternerArbeitsgang() > 0) {
							bdRuestzeitMaschineExterneAGGesamt = bdRuestzeitMaschineExterneAGGesamt
									.add(new BigDecimal(dRuestzeit / 60));
						} else {
							bdRuestzeitMaschineGesamt = bdRuestzeitMaschineGesamt.add(new BigDecimal(dRuestzeit / 60));
						}
					}
				} else if (sEinheit.equals(SystemFac.EINHEIT_SEKUNDE.trim())) {
					bdStueckzeit = bdStueckzeit.multiply(new BigDecimal(3600));
					dRuestzeit = lRuestzeit / 100;
					if (stuecklistearbeitsplanDto.getMaschineIId() == null) {
						if (artikelDto.getIExternerArbeitsgang() > 0) {
							bdRuestzeitMannExterneAGGesamt = bdRuestzeitMannExterneAGGesamt
									.add(new BigDecimal(dRuestzeit / 3600));
						} else {
							bdRuestzeitMannGesamt = bdRuestzeitMannGesamt.add(new BigDecimal(dRuestzeit / 3600));
						}
					} else {

						if (artikelDto.getIExternerArbeitsgang() > 0) {
							bdRuestzeitMaschineExterneAGGesamt = bdRuestzeitMaschineExterneAGGesamt
									.add(new BigDecimal(dRuestzeit / 3600));
						} else {
							bdRuestzeitMaschineGesamt = bdRuestzeitMaschineGesamt
									.add(new BigDecimal(dRuestzeit / 3600));
						}
					}
				}
				zeile[REPORT_GESAMTKALKULATION_STUECKZEIT] = bdStueckzeit;
				zeile[REPORT_GESAMTKALKULATION_RUESTZEIT] = new BigDecimal(dRuestzeit);

				zeile[REPORT_GESAMTKALKULATION_AG] = stuecklistearbeitsplanDto.getIArbeitsgang();
				zeile[REPORT_GESAMTKALKULATION_UAG] = stuecklistearbeitsplanDto.getIUnterarbeitsgang();
				zeile[REPORT_GESAMTKALKULATION_KOMMENTAR] = stuecklistearbeitsplanDto.getCKommentar();

				if (!struktur.isBMaschinenzeit()) {

					// VKPreise
					if (stklDto.getPartnerIId() != null) {
						if (kundeDto != null) {

							if (kundeDto.getMwstsatzbezIId() != null) {
								Timestamp belegDatum = Helper.cutTimestamp(getTimestamp());
								MwstsatzDto mwstsatzKunde = getMandantFac()
										.mwstsatzZuDatumClient(kundeDto.getMwstsatzbezIId(), belegDatum, theClientDto);
								VkpreisfindungDto vkpreisDto = getVkPreisfindungFac().verkaufspreisfindung(
										artikelDto.getIId(), kundeDto.getIId(), bdGesamtzeit.multiply(nLosgroesse),
										new java.sql.Date(belegDatum.getTime()),
										kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
//											getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(
//													kundeDto.getMwstsatzbezIId(), theClientDto).getIId(),
										mwstsatzKunde.getIId(), theClientDto.getSMandantenwaehrung(), theClientDto);

								VerkaufspreisDto kundenVKPreisDto = Helper.getVkpreisBerechnet(vkpreisDto);

								if (kundenVKPreisDto != null && kundenVKPreisDto.nettopreis != null) {

									zeile[REPORT_GESAMTKALKULATION_VKPREIS] = kundenVKPreisDto.nettopreis;
									zeile[REPORT_GESAMTKALKULATION_VKWERT] = bdGesamtzeit
											.multiply(kundenVKPreisDto.nettopreis);
								}

							}
						}
					}
				} else {

					MaschinenStundensatzDto maschinenStundensatzDto = getZeiterfassungFac()
							.getMaschinenKostenZumZeitpunkt(stuecklistearbeitsplanDto.getMaschineIId(),
									new java.sql.Timestamp(System.currentTimeMillis()), LocaleFac.BELEGART_STUECKLISTE,
									stuecklistearbeitsplanDto.getIId());

					zeile[REPORT_GESAMTKALKULATION_VKPREIS] = maschinenStundensatzDto.getBdStundensatzVK();
					zeile[REPORT_GESAMTKALKULATION_VKWERT] = maschinenStundensatzDto.getBdStundensatzVK()
							.multiply(bdGesamtzeit);
				}

			}
			alDaten.add(zeile);
		}

		// SP3389 Summe der Positionen
		for (int i = 0; i < alDaten.size(); i++) {
			Object[] zeileUnterstkl = (Object[]) alDaten.get(i);

			if (zeileUnterstkl[REPORT_GESAMTKALKULATION_STKLART] != null) {
				// Ist eine Unterstkl

				// Nun die Gestpreise/Werte der Positionen in die Unterstkl
				// zurueckschreiben

				int iEbeneUnterstkl = (Integer) zeileUnterstkl[REPORT_GESAMTKALKULATION_I_EBENE];

				BigDecimal mengeUnterstkl = (BigDecimal) zeileUnterstkl[REPORT_GESAMTKALKULATION_MENGE];

				int iNaechsteEbene = iEbeneUnterstkl + 1;

				BigDecimal gestwertStklpositionen = BigDecimal.ZERO;
				BigDecimal lief1wertStklpositionen = BigDecimal.ZERO;

				BigDecimal summeMaterial = BigDecimal.ZERO;
				BigDecimal summeArbeitszeit = BigDecimal.ZERO;

				for (int j = i + 1; j < alDaten.size(); j++) {

					Object[] zeileNaechsteEbene = (Object[]) alDaten.get(j);

					if (((Integer) zeileNaechsteEbene[REPORT_GESAMTKALKULATION_I_EBENE]) >= iNaechsteEbene) {

						BigDecimal gestwertTemp = (BigDecimal) zeileNaechsteEbene[REPORT_GESAMTKALKULATION_GESTWERT];

						// Wenns eine Stkl ist, den gestpreis mal menge der
						// stkl

						if (gestwertTemp != null) {
							gestwertStklpositionen = gestwertStklpositionen.add(gestwertTemp);
						}

						// SP3888
						Integer arbeitsgang = (Integer) zeileNaechsteEbene[REPORT_GESAMTKALKULATION_AG];
						if (arbeitsgang != null) {

							BigDecimal gestpreisTemp = (BigDecimal) zeileNaechsteEbene[REPORT_GESAMTKALKULATION_GESTPREIS];

							if (gestpreisTemp != null) {

								BigDecimal menge = (BigDecimal) zeileNaechsteEbene[REPORT_GESAMTKALKULATION_MENGE];

								// Wenns eine Stkl ist, den lief1preis mal
								// menge
								// der
								// stkl

								BigDecimal gestWertTemp = gestpreisTemp.multiply(menge);

								if (gestWertTemp != null) {
									lief1wertStklpositionen = lief1wertStklpositionen.add(gestWertTemp);

									summeArbeitszeit = summeArbeitszeit.add(gestWertTemp);
								}
							}
						} else {

							BigDecimal lief1preisTemp = (BigDecimal) zeileNaechsteEbene[REPORT_GESAMTKALKULATION_LIEF1PREIS];

							if (lief1preisTemp != null) {

								BigDecimal menge = (BigDecimal) zeileNaechsteEbene[REPORT_GESAMTKALKULATION_MENGE];

								// Wenns eine Stkl ist, den lief1preis mal
								// menge
								// der
								// stkl

								BigDecimal lief1WertTemp = lief1preisTemp.multiply(menge);

								if (lief1WertTemp != null) {
									lief1wertStklpositionen = lief1wertStklpositionen.add(lief1WertTemp);
									summeMaterial = summeMaterial.add(lief1WertTemp);
								}

							}
						}

					}

					if (((Integer) zeileNaechsteEbene[REPORT_GESAMTKALKULATION_I_EBENE]) < iNaechsteEbene) {
						break;
					}

				}
				BigDecimal gestpreisUnterstkl = BigDecimal.ZERO;
				BigDecimal lief1preisUnterstkl = BigDecimal.ZERO;

				BigDecimal lief1preisUnterstklMaterial = BigDecimal.ZERO;
				BigDecimal lief1preisUnterstklArbeitszeit = BigDecimal.ZERO;

				if (mengeUnterstkl.doubleValue() != 0) {
					gestpreisUnterstkl = gestwertStklpositionen.divide(mengeUnterstkl, 3, BigDecimal.ROUND_HALF_EVEN);
					lief1preisUnterstkl = lief1wertStklpositionen.divide(mengeUnterstkl, 3, BigDecimal.ROUND_HALF_EVEN);

					lief1preisUnterstklMaterial = summeMaterial.divide(mengeUnterstkl, 3, BigDecimal.ROUND_HALF_EVEN);
					lief1preisUnterstklArbeitszeit = summeArbeitszeit.divide(mengeUnterstkl, 3,
							BigDecimal.ROUND_HALF_EVEN);
				}

				zeileUnterstkl[REPORT_GESAMTKALKULATION_SUMMEGESTPREISPOSITIONENWENNSTUECKLISTE] = gestpreisUnterstkl;
				zeileUnterstkl[REPORT_GESAMTKALKULATION_SUMMELIEF1PREISPOSITIONENWENNSTUECKLISTE] = lief1preisUnterstkl;
				zeileUnterstkl[REPORT_GESAMTKALKULATION_SUMMELIEF1PREISPOSITIONENWENNSTUECKLISTE_MATERIAL] = lief1preisUnterstklMaterial;
				zeileUnterstkl[REPORT_GESAMTKALKULATION_SUMMELIEF1PREISPOSITIONENWENNSTUECKLISTE_ARBEITSZEIT] = lief1preisUnterstklArbeitszeit;

				// Wenn mehr wie 10% Abweichung, dann wird Flag gesetzt

				BigDecimal bdGestpreis = BigDecimal.ZERO;

				if (zeileUnterstkl[REPORT_GESAMTKALKULATION_GESTPREISWENNSTUECKLISTE] != null) {
					bdGestpreis = (BigDecimal) zeileUnterstkl[REPORT_GESAMTKALKULATION_GESTPREISWENNSTUECKLISTE];
				}

				double a = bdGestpreis.doubleValue() / 100.0f; // Ein
																// Prozent
																// von A
				double p = gestpreisUnterstkl.doubleValue() / a; // Wieviel
																	// Prozent
																	// von A
																	// sind
																	// in B?
				if (p < 90 || p > 110) {
					zeileUnterstkl[REPORT_GESAMTKALKULATION_GESTPREISEWEICHENAB] = new Boolean(true);
				} else {
					zeileUnterstkl[REPORT_GESAMTKALKULATION_GESTPREISEWEICHENAB] = new Boolean(false);
				}

			}

		}

		if (unterstuecklistenVerdichten) {
			ArrayList<Object[]> alDatenVerdichtet = new ArrayList<Object[]>();

			for (int i = 0; i < alDaten.size(); i++) {
				Object[] zeile = (Object[]) alDaten.get(i);

				int iEbene = (Integer) zeile[REPORT_GESAMTKALKULATION_I_EBENE];

				if (iEbene == 0) {

					BigDecimal gestpreis = BigDecimal.ZERO;
					BigDecimal gestwert = BigDecimal.ZERO;
					BigDecimal lief1preis = BigDecimal.ZERO;
					BigDecimal lief1wert = BigDecimal.ZERO;

					BigDecimal kalkpreis = BigDecimal.ZERO;
					BigDecimal kalkwert = BigDecimal.ZERO;
					BigDecimal vkpreis = BigDecimal.ZERO;
					BigDecimal vkwert = BigDecimal.ZERO;

					BigDecimal lief1preisPositionen = BigDecimal.ZERO;
					BigDecimal lief1preisMaterial = BigDecimal.ZERO;
					BigDecimal lief1preisArbeitszeit = BigDecimal.ZERO;

					Double gewicht = 0D;

					if (zeile[REPORT_GESAMTKALKULATION_GEWICHT] != null) {
						gewicht = (Double) zeile[REPORT_GESAMTKALKULATION_GEWICHT];
					}
					if (zeile[REPORT_GESAMTKALKULATION_GESTPREIS] != null) {
						gestpreis = (BigDecimal) zeile[REPORT_GESAMTKALKULATION_GESTPREIS];
					}

					if (zeile[REPORT_GESAMTKALKULATION_GESTWERT] != null) {
						gestwert = (BigDecimal) zeile[REPORT_GESAMTKALKULATION_GESTWERT];
					}
					if (zeile[REPORT_GESAMTKALKULATION_LIEF1PREIS] != null) {
						lief1preis = (BigDecimal) zeile[REPORT_GESAMTKALKULATION_LIEF1PREIS];
					}
					if (zeile[REPORT_GESAMTKALKULATION_LIEF1PREISGESAMT] != null) {
						lief1wert = (BigDecimal) zeile[REPORT_GESAMTKALKULATION_LIEF1PREISGESAMT];
					}
					if (zeile[REPORT_GESAMTKALKULATION_KALKPREIS] != null) {
						kalkpreis = (BigDecimal) zeile[REPORT_GESAMTKALKULATION_KALKPREIS];
					}
					if (zeile[REPORT_GESAMTKALKULATION_KALKWERT] != null) {
						kalkwert = (BigDecimal) zeile[REPORT_GESAMTKALKULATION_KALKWERT];
					}
					if (zeile[REPORT_GESAMTKALKULATION_VKPREIS] != null) {
						vkpreis = (BigDecimal) zeile[REPORT_GESAMTKALKULATION_VKPREIS];
					}
					if (zeile[REPORT_GESAMTKALKULATION_VKWERT] != null) {
						vkwert = (BigDecimal) zeile[REPORT_GESAMTKALKULATION_VKWERT];
					}

					if (zeile[REPORT_GESAMTKALKULATION_SUMMELIEF1PREISPOSITIONENWENNSTUECKLISTE] != null) {
						lief1preisPositionen = (BigDecimal) zeile[REPORT_GESAMTKALKULATION_SUMMELIEF1PREISPOSITIONENWENNSTUECKLISTE];
					}
					if (zeile[REPORT_GESAMTKALKULATION_SUMMELIEF1PREISPOSITIONENWENNSTUECKLISTE_ARBEITSZEIT] != null) {
						lief1preisArbeitszeit = (BigDecimal) zeile[REPORT_GESAMTKALKULATION_SUMMELIEF1PREISPOSITIONENWENNSTUECKLISTE_ARBEITSZEIT];
					}
					if (zeile[REPORT_GESAMTKALKULATION_SUMMELIEF1PREISPOSITIONENWENNSTUECKLISTE_MATERIAL] != null) {
						lief1preisMaterial = (BigDecimal) zeile[REPORT_GESAMTKALKULATION_SUMMELIEF1PREISPOSITIONENWENNSTUECKLISTE_MATERIAL];
					}

					for (int j = i + 1; j < alDaten.size(); j++) {

						Object[] zeileTemp = (Object[]) alDaten.get(j);
						int iEbeneTemp = (Integer) zeileTemp[REPORT_GESAMTKALKULATION_I_EBENE];
						if (iEbeneTemp == 0) {
							break;
						}

						if (zeileTemp[REPORT_GESAMTKALKULATION_GEWICHT] != null) {
							gewicht = gewicht + ((Double) zeileTemp[REPORT_GESAMTKALKULATION_GEWICHT]);
						}

						if (zeileTemp[REPORT_GESAMTKALKULATION_GESTPREIS] != null) {
							gestpreis = gestpreis.add((BigDecimal) zeileTemp[REPORT_GESAMTKALKULATION_GESTPREIS]);
						}
						if (zeileTemp[REPORT_GESAMTKALKULATION_GESTWERT] != null) {
							gestwert = gestwert.add((BigDecimal) zeileTemp[REPORT_GESAMTKALKULATION_GESTWERT]);
						}

						// SP3888
						/*
						 * if (zeileTemp[REPORT_GESAMTKALKULATION_LIEF1PREIS] != null) { lief1preis =
						 * lief1preis .add((BigDecimal) zeileTemp[REPORT_GESAMTKALKULATION_LIEF1PREIS]);
						 * }
						 */
						if (zeileTemp[REPORT_GESAMTKALKULATION_LIEF1PREISGESAMT] != null) {
							lief1wert = lief1wert
									.add((BigDecimal) zeileTemp[REPORT_GESAMTKALKULATION_LIEF1PREISGESAMT]);
						}
						if (zeileTemp[REPORT_GESAMTKALKULATION_KALKPREIS] != null) {
							kalkpreis = kalkpreis.add((BigDecimal) zeileTemp[REPORT_GESAMTKALKULATION_KALKPREIS]);
						}
						if (zeileTemp[REPORT_GESAMTKALKULATION_KALKWERT] != null) {
							kalkwert = kalkwert.add((BigDecimal) zeileTemp[REPORT_GESAMTKALKULATION_KALKWERT]);
						}
						if (zeileTemp[REPORT_GESAMTKALKULATION_VKPREIS] != null) {
							vkpreis = vkpreis.add((BigDecimal) zeileTemp[REPORT_GESAMTKALKULATION_VKPREIS]);
						}
						if (zeileTemp[REPORT_GESAMTKALKULATION_VKWERT] != null) {
							vkwert = vkwert.add((BigDecimal) zeileTemp[REPORT_GESAMTKALKULATION_VKWERT]);
						}
					}

					zeile[REPORT_GESAMTKALKULATION_GEWICHT] = gewicht;

					zeile[REPORT_GESAMTKALKULATION_GESTPREIS] = gestpreis;
					zeile[REPORT_GESAMTKALKULATION_GESTWERT] = gestwert;
					zeile[REPORT_GESAMTKALKULATION_LIEF1PREIS] = lief1preis;
					zeile[REPORT_GESAMTKALKULATION_LIEF1PREISGESAMT] = lief1wert;
					zeile[REPORT_GESAMTKALKULATION_KALKPREIS] = kalkpreis;
					zeile[REPORT_GESAMTKALKULATION_KALKWERT] = kalkwert;
					zeile[REPORT_GESAMTKALKULATION_VKPREIS] = vkpreis;
					zeile[REPORT_GESAMTKALKULATION_VKWERT] = vkwert;
					zeile[REPORT_GESAMTKALKULATION_SUMMELIEF1PREISPOSITIONENWENNSTUECKLISTE] = lief1preisPositionen;
					zeile[REPORT_GESAMTKALKULATION_SUMMELIEF1PREISPOSITIONENWENNSTUECKLISTE_MATERIAL] = lief1preisMaterial;
					zeile[REPORT_GESAMTKALKULATION_SUMMELIEF1PREISPOSITIONENWENNSTUECKLISTE_ARBEITSZEIT] = lief1preisArbeitszeit;

					zeile[REPORT_GESAMTKALKULATION_DURCHLAUFZEIT] = null;

					alDatenVerdichtet.add(zeile);
				}
			}

			alDaten = alDatenVerdichtet;
		}

		data = new Object[alDaten.size()][REPORT_GESAMTKALKULATION_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(data);

		HashMap<String, Object> reportParameter = new HashMap<String, Object>();
		StuecklisteDto dto = getStuecklisteFac().stuecklisteFindByPrimaryKey(stuecklisteIId, theClientDto);

		if (konfigurationsWerte != null) {

			String[] fieldnames = new String[] { "Parameter", "Bezeichnung", "Typ", "WertAsObject" };

			Object[][] dataSub = new Object[konfigurationsWerte.size()][fieldnames.length];
			int i = 0;
			Iterator itk = konfigurationsWerte.keySet().iterator();
			while (itk.hasNext()) {
				String key = (String) itk.next();
				dataSub[i][0] = key;
				StklparameterDto stklparameterDto = getStuecklisteFac()
						.stklparameterFindByStuecklisteIIdCNr(stuecklisteIId, key, theClientDto);

				if (stklparameterDto != null) {
					stklparameterDto = getStuecklisteFac().stklparameterFindByPrimaryKey(stklparameterDto.getIId(),
							theClientDto);
					if (stklparameterDto.getStklparametersprDto() != null) {
						dataSub[i][1] = stklparameterDto.getStklparametersprDto().getCBez();
					}
					dataSub[i][2] = stklparameterDto.getCTyp();

					if (stklparameterDto.getCTyp().trim().equals(StuecklisteFac.STKLPARAMETER_TYP_ITEM_ID)
							&& konfigurationsWerte.get(key) instanceof com.lp.server.stueckliste.service.ItemId) {

						com.lp.server.stueckliste.service.ItemId itemid = (com.lp.server.stueckliste.service.ItemId) konfigurationsWerte
								.get(key);

						ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmallOhneExc(itemid.getId(),
								theClientDto);

						if (aDto != null) {
							dataSub[i][3] = getArtikelFac().artikelFindByPrimaryKey(aDto.getIId(), theClientDto);
						}

					} else if (stklparameterDto.getCTyp().trim().equals(StuecklisteFac.STKLPARAMETER_TYP_KUNDE_ID)
							&& konfigurationsWerte.get(key) instanceof KundeId) {

						KundeId kundeId = (KundeId) konfigurationsWerte.get(key);

						KundeDto kdDto = getKundeFac().kundeFindByPrimaryKeyOhneExc(kundeId.id(), theClientDto);

						if (kdDto != null) {
							dataSub[i][3] = kdDto;
						}

					} else {
						dataSub[i][3] = konfigurationsWerte.get(key);
					}

				} else {
					dataSub[i][3] = konfigurationsWerte.get(key);
				}

				i++;
			}

			reportParameter.put("P_SUBREPORT_KONFIGURATIONSWERTE", new LPDatenSubreport(dataSub, fieldnames));

		} else {
			String[] fieldnames = new String[] { "Parameter", "Bezeichnung", "Typ", "WertAsObject" };

			StklparameterDto[] stklparameterDtos = getStuecklisteFac().stklparameterFindByStuecklisteIId(stuecklisteIId,
					theClientDto);

			Object[][] dataSub = new Object[stklparameterDtos.length][fieldnames.length];

			for (int i = 0; i < stklparameterDtos.length; i++) {

				dataSub[i][0] = stklparameterDtos[i].getCNr();

				StklparameterDto stklparameterDto = getStuecklisteFac()
						.stklparameterFindByPrimaryKey(stklparameterDtos[i].getIId(), theClientDto);
				if (stklparameterDto.getStklparametersprDto() != null) {
					dataSub[i][1] = stklparameterDto.getStklparametersprDto().getCBez();
				}
				dataSub[i][2] = stklparameterDto.getCTyp();

				if (stklparameterDto.getCTyp() != null
						&& stklparameterDto.getCTyp().trim().equals(StuecklisteFac.STKLPARAMETER_TYP_KUNDE_ID)) {

					try {
						KundeDto kdDto = getKundeFac()
								.kundeFindByPrimaryKeyOhneExc(new Integer(stklparameterDto.getCWert()), theClientDto);

						if (kdDto != null) {
							dataSub[i][3] = kdDto;
						}
					} catch (Throwable e) {
						//
					}
				} else if (stklparameterDto.getCTyp() != null
						&& stklparameterDto.getCTyp().trim().equals(StuecklisteFac.STKLPARAMETER_TYP_ITEM_ID)) {
					try {
						ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmallOhneExc(
								new Integer(stklparameterDto.getCWert()), theClientDto);

						if (aDto != null) {
							dataSub[i][3] = getArtikelFac().artikelFindByPrimaryKey(aDto.getIId(), theClientDto);
						}
					} catch (Throwable e) {
						//
					}
				} else {
					dataSub[i][3] = stklparameterDto.getCWert();
				}

			}

			reportParameter.put("P_SUBREPORT_KONFIGURATIONSWERTE", new LPDatenSubreport(dataSub, fieldnames));
		}

		reportParameter.put("P_MAX_ANZAHL_ARBEITSSCHRITTE", new Integer(iMaxAnzahlArbeitsschritte));

		reportParameter.put("P_NACH_ARTIKEL_SORTIEREN", new Boolean(nachArtikelcnrsortieren));

		reportParameter.put("P_UEBERALLEMANDANTEN", new Boolean(bUeberAlleMandanten));

		reportParameter.put("P_FREMDFERTIGUNG_AUFLOESEN", new Boolean(bFremdfertigungAufloesen));
		reportParameter.put("P_MINBS_UND_VPE_BERUECKSICHTIGEN", new Boolean(minBSMengeUndVPEBeruecksichtigen));

		reportParameter.put("P_BASIS_KALKULATION", new Integer(iBasisKalkulation));

		reportParameter.put("P_STUECKLISTE", dto.getArtikelDto().getCNr());

		// PJ20944
		reportParameter.put("P_FREMDFERTIGUNG", Helper.short2Boolean(dto.getBFremdfertigung()));

		reportParameter.put("P_STUECKLISTE_REFERENZNUMMER", dto.getArtikelDto().getCReferenznr());
		reportParameter.put("P_STUECKLISTE_WARENVERKEHRSNUMMER", dto.getArtikelDto().getCWarenverkehrsnummer());

		reportParameter.put("P_STUECKLISTE_KURZBEZEICHNUNG", dto.getArtikelDto().getKbezAusSpr());

		reportParameter.put("P_STUECKLISTE_BEWILLIGUNGSPFLICHTIG",
				Helper.short2Boolean(dto.getArtikelDto().getBBewilligungspflichtig()));
		reportParameter.put("P_STUECKLISTE_MELDEPFLICHTIG",
				Helper.short2Boolean(dto.getArtikelDto().getBMeldepflichtig()));

		if (dto.getArtikelDto().getLandIIdUrsprungsland() != null) {
			LandDto landDto = getSystemFac().landFindByPrimaryKey(dto.getArtikelDto().getLandIIdUrsprungsland());

			reportParameter.put("P_STUECKLISTE_URSPRUNGSLAND_LKZ", landDto.getCLkz());
			reportParameter.put("P_STUECKLISTE_URSPRUNGSLAND_NAME", landDto.getCName());

		}

		if (dto.getArtikelDto().getSollverkaufDto() != null) {
			reportParameter.put("P_STUECKLISTE_AUFSCHLAG", dto.getArtikelDto().getSollverkaufDto().getFAufschlag());
			reportParameter.put("P_STUECKLISTE_SOLLVERKAUF", dto.getArtikelDto().getSollverkaufDto().getFSollverkauf());
		}

		reportParameter.put("P_STUECKLISTE_MINDESTDECKUNGSBEITRAG", dto.getArtikelDto().getFMindestdeckungsbeitrag());

		reportParameter.put("P_ERFASSUNGSFAKTOR", dto.getNErfassungsfaktor());
		reportParameter.put("P_STUECKLISTEBEZEICHNUNG", dto.getArtikelDto().formatBezeichnung());
		reportParameter.put("P_EINHEIT", dto.getArtikelDto().getEinheitCNr().trim());

		reportParameter.put("P_FREIGABE_ZEITPUNKT", dto.getTFreigabe());
		if (dto.getPersonalIIdFreigabe() != null) {
			reportParameter.put("P_FREIGABE_PERSON", getPersonalFac()
					.personalFindByPrimaryKey(dto.getPersonalIIdFreigabe(), theClientDto).getCKurzzeichen());
		}

		reportParameter.put("P_EINHEITZEIT", sEinheit);
		reportParameter.put("P_LOSGROESSE", dto.getNLosgroesse());
		reportParameter.put("P_DURCHLAUFZEIT", dto.getNDefaultdurchlaufzeit());
		reportParameter.put("P_WAEHRUNG", mandantenWaehrung);
		reportParameter.put("P_UNTERSTKL_VERDICHTEN", new Boolean(unterstuecklistenVerdichten));

		reportParameter.put("P_GESAMTMENGE_MATERIAL_BERUECKSICHTIGEN", gesamtmengeMaterialBeruecksichtigen);

		// PJ22131
		reportParameter.put("P_ANLEGEN_ZEITPUNKT", dto.getTAnlegen());
		reportParameter.put("P_ANLEGEN_PERSON",
				getPersonalFac().personalFindByPrimaryKey(dto.getPersonalIIdAnlegen(), theClientDto).getCKurzzeichen());
		reportParameter.put("P_AENDERN_ZEITPUNKT", dto.getTAendern());
		reportParameter.put("P_AENDERN_PERSON",
				getPersonalFac().personalFindByPrimaryKey(dto.getPersonalIIdAendern(), theClientDto).getCKurzzeichen());

		try {

			// PJ22395
			ParametermandantDto parameterDtoLief1Gueltigkeit = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_LIEF1PREIS_GUELTIG_IN_MONATEN_FUER_REPORT);

			if (parameterDtoLief1Gueltigkeit.getCWert() != null
					&& parameterDtoLief1Gueltigkeit.getCWert().length() > 0) {
				reportParameter.put("P_LIEF1PREIS_GUELTIG_IN_MONATEN",
						(Integer) parameterDtoLief1Gueltigkeit.getCWertAsObject());
			}

			ParametermandantDto parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT);

			reportParameter.put("P_EINHEIT_WIEDERBESCHAFFUNGSZEIT", parameterDto.getCWert());

			reportParameter.put("P_ARTIKELSPERREN",
					getArtikelFac().getArtikelsperrenText(dto.getArtikelDto().getIId()));

			if (dto.getArtikelDto().getVorzugIId() != null) {
				reportParameter.put("P_VORZUGSTEIL",
						getArtikelFac().vorzugFindByPrimaryKey(dto.getArtikelDto().getVorzugIId()).getCBez());

			}

			StklagerentnahmeDto[] stklagerentnahmeDtos = getStuecklisteFac()
					.stklagerentnahmeFindByStuecklisteIId(dto.getIId());

			String losLaegerMitLagerorten = "";

			LinkedHashSet lagerIIds = new LinkedHashSet();

			if (stklagerentnahmeDtos != null && stklagerentnahmeDtos.length > 0) {
				for (int i = 0; i < stklagerentnahmeDtos.length; i++) {

					lagerIIds.add(stklagerentnahmeDtos[i].getLagerIId());

				}
			}

			// default materialentnahme vom Hauptlager des Mandanten
			LagerDto lagerDto = getLagerFac().getHauptlagerDesMandanten(theClientDto);

			LagerDto[] lagerDtos = getLagerFac().lagerFindByMandantCNrOrderByILoslagersort(theClientDto.getMandant());

			if (lagerDtos != null && lagerDtos.length > 0) {
				for (int i = 0; i < lagerDtos.length; i++) {

					lagerIIds.add(lagerDtos[i].getIId());

				}
			} else {

				lagerIIds.add(lagerDto.getIId());

			}

			byte[] CRLFAscii = { 13, 10 };
			Iterator itLaeger = lagerIIds.iterator();
			while (itLaeger.hasNext()) {
				Integer lagerIId = (Integer) itLaeger.next();

				LagerDto lagerDtoTemp = getLagerFac().lagerFindByPrimaryKey(lagerIId);

				losLaegerMitLagerorten += lagerDtoTemp.getCNr() + ": ";

				List<LagerplatzDto> lagerplaetze = getLagerFac()
						.lagerplatzFindByArtikelIIdLagerIIdOhneExc(dto.getArtikelIId(), lagerDtoTemp.getIId());

				for (int i = 0; i < lagerplaetze.size(); i++) {
					LagerplatzDto lpDto = lagerplaetze.get(i);

					losLaegerMitLagerorten += lpDto.getCLagerplatz();

					if (i < lagerplaetze.size() - 1) {
						losLaegerMitLagerorten += ",";
					}
				}

				losLaegerMitLagerorten += new String(CRLFAscii);

			}

			reportParameter.put("P_ABBUCHUNGSLAEGER_MIT_LAGERORTEN", losLaegerMitLagerorten);

		} catch (

		RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		bdMaterialkosten = bdMaterialkosten.divide(nLosgroesse, BigDecimal.ROUND_HALF_EVEN);
		// bdMaterialkosten = Helper.rundeKaufmaennisch(bdMaterialkosten, 2);
		reportParameter.put("P_MATERIALKOSTEN", bdMaterialkosten);

		if (kundeDto != null && kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste() != null) {

			try {
				VkpfartikelpreislisteDto preislisteDto = getVkPreisfindungFac()
						.vkpfartikelpreislisteFindByPrimaryKey(kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste());

				reportParameter.put("P_PREISLISTE", preislisteDto.getCNr());
			} catch (RemoteException ex6) {
				throwEJBExceptionLPRespectOld(ex6);
			}
		}

		// PJ19483
		try {
			reportParameter.put("P_VKPREISBASIS",
					getVkPreisfindungFac().ermittlePreisbasis(stklDto.getArtikelIId(),
							new java.sql.Date(System.currentTimeMillis()), nLosgroesse, null,
							theClientDto.getSMandantenwaehrung(), theClientDto));

			if (kundeDto != null) {
				reportParameter.put("P_KUNDE", kundeDto.getPartnerDto().formatAnrede());

				reportParameter.put("P_KUNDE_ADRESSBLOCK",
						formatAdresseFuerAusdruck(kundeDto.getPartnerDto(), null,
								getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto),
								Helper.string2Locale(kundeDto.getPartnerDto().getLocaleCNrKommunikation())));

				if (kundeDto.getMwstsatzbezIId() != null) {
					Timestamp belegDatum = Helper.cutTimestamp(getTimestamp());
					MwstsatzDto mwstsatzKunde = getMandantFac().mwstsatzZuDatumValidate(kundeDto.getMwstsatzbezIId(),
							belegDatum, theClientDto);
					VkpreisfindungDto vkpreisDto = getVkPreisfindungFac().verkaufspreisfindung(stklDto.getArtikelIId(),
							kundeDto.getIId(), nLosgroesse, new java.sql.Date(belegDatum.getTime()),
							kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
//							getMandantFac()
//									.mwstsatzFindByMwstsatzbezIIdAktuellster(kundeDto.getMwstsatzbezIId(), theClientDto)
//									.getIId(),
							mwstsatzKunde.getIId(), theClientDto.getSMandantenwaehrung(), theClientDto);

					VerkaufspreisDto kundenVKPreisDto = Helper.getVkpreisBerechnet(vkpreisDto);

					if (kundenVKPreisDto != null && kundenVKPreisDto.nettopreis != null) {
						reportParameter.put("P_KUNDE_VKPREIS", kundenVKPreisDto.nettopreis);
					}
				}

			}
		} catch (RemoteException ex6) {
			throwEJBExceptionLPRespectOld(ex6);
		}
		bdMaterialkostenLief = bdMaterialkostenLief.divide(nLosgroesse, BigDecimal.ROUND_HALF_EVEN);
		// bdMaterialkostenLief =
		// Helper.rundeKaufmaennisch(bdMaterialkostenLief, 2);
		reportParameter.put("P_MATERIALKOSTENLIEF", bdMaterialkostenLief);
		reportParameter.put("P_FIXKOSTENLIEF", bdFixkostenLief);

		bdArbeitszeitkosten = bdArbeitszeitkosten.divide(nLosgroesse, BigDecimal.ROUND_HALF_EVEN);
		// bdArbeitszeitkosten = Helper.rundeKaufmaennisch(bdArbeitszeitkosten,
		// 4);

		reportParameter.put("P_ARBEITSZEITKOSTEN", bdArbeitszeitkosten);

		bdMaschinenzeitkosten = bdMaschinenzeitkosten.divide(nLosgroesse, BigDecimal.ROUND_HALF_EVEN);

		reportParameter.put("P_MASCHINENKOSTEN", bdMaschinenzeitkosten);
		reportParameter.put("P_RUESTZEITGESAMTMANN", bdRuestzeitMannGesamt);
		reportParameter.put("P_STUECKZEITGESAMTMANN", bdStueckZeitMannGesamt);
		reportParameter.put("P_STUECKZEITGESAMTMASCHINE", bdStueckZeitMaschineGesamt);
		reportParameter.put("P_ZEITGESAMTMANN", bdZeitMannGesamt);
		reportParameter.put("P_RUESTZEITGESAMTMASCHINE", bdRuestzeitMaschineGesamt);
		reportParameter.put("P_ZEITGESAMTMASCHINE", bdZeitMaschineGesamt);

		reportParameter.put("P_RUESTZEITGESAMTMANN_EXTERNEAG", bdRuestzeitMannExterneAGGesamt);
		reportParameter.put("P_RUESTZEITGESAMTMASCHINE_EXTERNEAG", bdRuestzeitMaschineExterneAGGesamt);

		// Mandantparameter holen

		if (dArbeitszeitgemeinkostenfaktor == null) {
			parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.ARBEITSZEITGEMEINKOSTENFAKTOR,
					new Timestamp(System.currentTimeMillis()));
			dArbeitszeitgemeinkostenfaktor = ((Double) parameter.getCWertAsObject()).doubleValue();

		}

		reportParameter.put("P_ARBEITSZEITGEMEINKOSTENPROZENT",
				Helper.formatZahl(dArbeitszeitgemeinkostenfaktor, 1, theClientDto.getLocUi()) + "%");
		reportParameter.put("P_ARBEITSZEITGEMEINKOSTENFAKTOR", dArbeitszeitgemeinkostenfaktor);

		dArbeitszeitgemeinkostenfaktor = (dArbeitszeitgemeinkostenfaktor / 100);
		BigDecimal bdArbeitszeitgemeinkosten = bdArbeitszeitkosten
				.multiply(new BigDecimal(dArbeitszeitgemeinkostenfaktor));

		reportParameter.put("P_ARBEITSZEITGEMEINKOSTEN", bdArbeitszeitgemeinkosten);

		if (dMaterialgemeinkostenfaktor == null) {

			parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.MATERIALGEMEINKOSTENFAKTOR,
					new Timestamp(System.currentTimeMillis()));

			dMaterialgemeinkostenfaktor = ((Double) parameter.getCWertAsObject()).doubleValue();
		}

		reportParameter.put("P_MATERIALGEMEINKOSTENPROZENT",
				Helper.formatZahl(dMaterialgemeinkostenfaktor, 1, theClientDto.getLocUi()) + "%");
		reportParameter.put("P_MATERIALGEMEINKOSTENFAKTOR", dMaterialgemeinkostenfaktor);

		dMaterialgemeinkostenfaktor = (dMaterialgemeinkostenfaktor / 100);

		BigDecimal bdMaterialgemeinskosten = bdMaterialkosten.multiply(new BigDecimal(dMaterialgemeinkostenfaktor));

		reportParameter.put("P_MATERIALGEMEINKOSTEN", bdMaterialgemeinskosten);

		BigDecimal bdMaterialgemeinskostenLief = bdMaterialkostenLief
				.multiply(new BigDecimal(dMaterialgemeinkostenfaktor));

		reportParameter.put("P_MATERIALGEMEINKOSTENLIEF", bdMaterialgemeinskostenLief);

		if (dFertigungsgemeinkostenfaktor == null) {

			parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.FERTIGUNGSGEMEINKOSTENFAKTOR,
					new Timestamp(System.currentTimeMillis()));

			dFertigungsgemeinkostenfaktor = ((Double) parameter.getCWertAsObject()).doubleValue();
		}
		reportParameter.put("P_FERTIGUNGSGEMEINKOSTENPROZENT",
				Helper.formatZahl(dFertigungsgemeinkostenfaktor, 1, theClientDto.getLocUi()) + "%");
		dFertigungsgemeinkostenfaktor = (dFertigungsgemeinkostenfaktor / 100);

		BigDecimal bdFertigungskosten = bdMaschinenzeitkosten.add(bdArbeitszeitkosten)
				.multiply(new BigDecimal(dFertigungsgemeinkostenfaktor));

		reportParameter.put("P_FERTIGUNGSGEMEINKOSTEN", bdFertigungskosten);

		BigDecimal herstellkosten = bdMaterialkosten.add(bdArbeitszeitkosten).add(bdArbeitszeitgemeinkosten)
				.add(bdMaterialgemeinskosten).add(bdFertigungskosten).add(bdMaschinenzeitkosten);
		reportParameter.put("P_HERSTELLKOSTEN", herstellkosten);

		BigDecimal herstellkostenLief = bdMaterialkostenLief.add(bdArbeitszeitkosten).add(bdArbeitszeitgemeinkosten)
				.add(bdMaterialgemeinskostenLief).add(bdFertigungskosten).add(bdMaschinenzeitkosten);
		reportParameter.put("P_HERSTELLKOSTENLIEF", herstellkostenLief);

		parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
				ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.ENTWICKLUNGSGEMEINKOSTENFAKTOR,
				new Timestamp(System.currentTimeMillis()));
		double dEntwicklungsgemeinkostenfaktor = ((Double) parameter.getCWertAsObject()).doubleValue();
		reportParameter.put("P_ENTWICKLUNGSGEMEINKOSTENPROZENT",
				Helper.formatZahl(dEntwicklungsgemeinkostenfaktor, 1, theClientDto.getLocUi()) + "%");

		dEntwicklungsgemeinkostenfaktor = (dEntwicklungsgemeinkostenfaktor / 100);

		BigDecimal bdEntwicklungsgemeinkosten = herstellkosten
				.multiply(new BigDecimal(dEntwicklungsgemeinkostenfaktor));

		reportParameter.put("P_ENTWICKLUNGSGEMEINKOSTEN", bdEntwicklungsgemeinkosten);
		BigDecimal bdEntwicklungsgemeinkostenLief = herstellkostenLief
				.multiply(new BigDecimal(dEntwicklungsgemeinkostenfaktor));

		reportParameter.put("P_ENTWICKLUNGSGEMEINKOSTENLIEF", bdEntwicklungsgemeinkostenLief);

		parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
				ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.VERWALTUNGSGEMEINKOSTENFAKTOR,
				new Timestamp(System.currentTimeMillis()));

		double dVerwaltungssgemeinkostenfaktor = ((Double) parameter.getCWertAsObject()).doubleValue();
		reportParameter.put("P_VERWALTUNGSGEMEINKOSTENPROZENT",
				Helper.formatZahl(dVerwaltungssgemeinkostenfaktor, 1, theClientDto.getLocUi()) + "%");
		dVerwaltungssgemeinkostenfaktor = (dVerwaltungssgemeinkostenfaktor / 100);

		BigDecimal bdVerwaltungsgemeinkosten = herstellkosten.multiply(new BigDecimal(dVerwaltungssgemeinkostenfaktor));

		reportParameter.put("P_VERWALTUNGSGEMEINKOSTEN", bdVerwaltungsgemeinkosten);

		BigDecimal bdVerwaltungsgemeinkostenLief = herstellkostenLief
				.multiply(new BigDecimal(dVerwaltungssgemeinkostenfaktor));

		reportParameter.put("P_VERWALTUNGSGEMEINKOSTENLIEF", bdVerwaltungsgemeinkostenLief);
		parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
				ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.VERTRIEBSGEMEINKOSTENFAKTOR,
				new Timestamp(System.currentTimeMillis()));

		double dVertriebsgemeinkostenfaktor = ((Double) parameter.getCWertAsObject()).doubleValue();
		reportParameter.put("P_VERTRIEBSGEMEINKOSTENPROZENT",
				Helper.formatZahl(dVertriebsgemeinkostenfaktor, 1, theClientDto.getLocUi()) + "%");

		dVertriebsgemeinkostenfaktor = (dVertriebsgemeinkostenfaktor / 100);

		BigDecimal bdVertriebsgemeinkosten = herstellkosten.multiply(new BigDecimal(dVertriebsgemeinkostenfaktor));

		reportParameter.put("P_VERTRIEBSGEMEINKOSTEN", bdVertriebsgemeinkosten);

		BigDecimal bdVertriebsgemeinkostenLief = herstellkostenLief
				.multiply(new BigDecimal(dVertriebsgemeinkostenfaktor));

		reportParameter.put("P_VERTRIEBSGEMEINKOSTENLIEF", bdVertriebsgemeinkostenLief);

		BigDecimal selbstkosten = herstellkosten.add(bdEntwicklungsgemeinkosten).add(bdVerwaltungsgemeinkosten)
				.add(bdVertriebsgemeinkosten);
		reportParameter.put("P_SELBSTKOSTEN", selbstkosten);

		BigDecimal selbstkostenLief = herstellkostenLief.add(bdEntwicklungsgemeinkostenLief)
				.add(bdVerwaltungsgemeinkostenLief).add(bdVertriebsgemeinkostenLief);
		reportParameter.put("P_SELBSTKOSTENLIEF", selbstkostenLief);

		BigDecimal durchlaufzeitGesamt = new BigDecimal(0);
		if (dto.getNDefaultdurchlaufzeit() != null) {
			durchlaufzeitGesamt = dto.getNDefaultdurchlaufzeit();
		}
		Iterator<Integer> i = hmHoechsteDurchlaufzeitEinerEbene.keySet().iterator();
		while (i.hasNext()) {
			Integer key = (Integer) i.next();
			BigDecimal wert = (BigDecimal) hmHoechsteDurchlaufzeitEinerEbene.get(key);
			durchlaufzeitGesamt = durchlaufzeitGesamt.add(wert);
		}

		reportParameter.put("P_DURCHLAUFZEITGESAMT", durchlaufzeitGesamt);

		reportParameter.put("P_MIT_KLEINSTEM_GROESSTEN_EK_PREIS_DER_LETZTEN_2_JAHRE",
				new Boolean(bMitPreisenDerLetzten2Jahre));

		reportParameter.put("P_DATUM_PREISGUELTIGKEIT", preisGueltig);

//		logDataSummed();

		initJRDS(reportParameter, StuecklisteReportFac.REPORT_MODUL,
				StuecklisteReportFac.REPORT_STUECKLISTE_GESAMTKALKULATION, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

//	private void logDataSummed() {
//		BigDecimal materialkostenLief = sum(REPORT_GESAMTKALKULATION_LIEF1PREISGESAMT);
//		BigDecimal arbeitszeitkosten = sum(REPORT_GESAMTKALKULATION_GESTWERT);
//		
//		myLogger.warn("Summe MaterialkostenLief: " + materialkostenLief + ", Arbeitszeitkosten: " + arbeitszeitkosten);
//	}
//	
//	private BigDecimal sum(int column) {
//		BigDecimal sum = BigDecimal.ZERO;
//		for (int i = 0; i < data.length; i++) {
//			BigDecimal d = (BigDecimal) data[i][column];
//			if (d != null) {
//				if (REPORT_GESAMTKALKULATION_GESTWERT == column) {
//					if (data[i][REPORT_GESAMTKALKULATION_AG] != null) {
//						sum = sum.add(d);						
//					}
//				} else {
//					sum = sum.add(d);
//				}
//			}
//		}
//		return sum;
//	}

	public JasperPrintLP printFreigabe(Integer stuecklisteIId, TheClientDto theClientDto) {
		index = -1;
		sAktuellerReport = StuecklisteReportFac.REPORT_STUECKLISTE_FREIGABE;

		HashMap<String, Object> reportParameter = new HashMap<String, Object>();
		StuecklisteDto dto = getStuecklisteFac().stuecklisteFindByPrimaryKey(stuecklisteIId, theClientDto);

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT pos FROM FLRStuecklisteposition pos WHERE pos.stueckliste_i_id=" + stuecklisteIId
				+ "  ORDER BY pos.i_sort ASC ";

		org.hibernate.Query hquery = session.createQuery(sQuery);

		List<?> resultList = hquery.list();
		Iterator<?> resultListIterator = resultList.iterator();

		ArrayList alDaten = new ArrayList();

		boolean bAllePositionenSindFreigegeben = true;

		while (resultListIterator.hasNext()) {
			FLRStuecklisteposition flrStuecklisteposition = (FLRStuecklisteposition) resultListIterator.next();

			Object[] oZeile = new Object[REPORT_FREIGABE_ANZAHL_SPALTEN];

			com.lp.server.artikel.service.ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKey(flrStuecklisteposition.getFlrartikel().getI_id(), theClientDto);
			boolean bFreigabe = true;
			if (artikelDto.getArtikelartCNr() != null && artikelDto.getArtikelartCNr()
					.equals(com.lp.server.artikel.service.ArtikelFac.ARTIKELART_HANDARTIKEL)) {
				bFreigabe = false;
			}

			StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByMandantCNrArtikelIIdOhneExc(
					flrStuecklisteposition.getFlrartikel().getI_id(), theClientDto);

			if (stklDto != null) {
				oZeile[REPORT_FREIGABE_FREIGABE_AM] = stklDto.getTFreigabe();

				if (stklDto.getTFreigabe() == null) {
					bFreigabe = false;
				}

				if (stklDto.getPersonalIIdFreigabe() != null) {
					oZeile[REPORT_FREIGABE_FREIGABE_VON] = getPersonalFac()
							.personalFindByPrimaryKeySmall(stklDto.getPersonalIIdFreigabe()).getCKurzzeichen();

				}

			} else {
				oZeile[REPORT_FREIGABE_FREIGABE_AM] = artikelDto.getTFreigabe();
				if (artikelDto.getTFreigabe() == null) {
					bFreigabe = false;
				}

				if (artikelDto.getPersonalIIdFreigabe() != null) {
					oZeile[REPORT_FREIGABE_FREIGABE_VON] = getPersonalFac()
							.personalFindByPrimaryKeySmall(artikelDto.getPersonalIIdFreigabe()).getCKurzzeichen();

				}

			}

			oZeile[REPORT_FREIGABE_ARTIKELNUMMER] = artikelDto.getCNr();

			oZeile[REPORT_FREIGABE_BEZEICHNUNG] = artikelDto.getCBezAusSpr();
			oZeile[REPORT_FREIGABE_ZUSATZBEZEICHNUNG] = artikelDto.getCZBezAusSpr();
			oZeile[REPORT_FREIGABE_ZUSATZBEZEICHNUNG2] = artikelDto.getCZBez2AusSpr();

			oZeile[REPORT_FREIGABE_MENGE] = flrStuecklisteposition.getN_menge();
			oZeile[REPORT_FREIGABE_LFDNR] = flrStuecklisteposition.getI_lfdnummer();
			oZeile[REPORT_FREIGABE_POSITION] = flrStuecklisteposition.getC_position();
			oZeile[REPORT_FREIGABE_STUECKLISTEPOSITION_I_ID] = flrStuecklisteposition.getI_id();

			String[] fieldnames = new String[] { "Artikelnummer", "Bezeichnung", "Zusatzbezeichnung",
					"Zusatzbezeichnung2", "FreigabeAm", "FreigabeVon" };

			PosersatzDto[] posersatzDtos = getStuecklisteFac()
					.posersatzFindByStuecklistepositionIId(flrStuecklisteposition.getI_id());

			Object[][] dataSub = new Object[posersatzDtos.length][fieldnames.length];

			for (int j = 0; j < posersatzDtos.length; j++) {

				com.lp.server.artikel.service.ArtikelDto artikelDtoErsatz = getArtikelFac()
						.artikelFindByPrimaryKeySmall(posersatzDtos[j].getArtikelIIdErsatz(), theClientDto);

				if (artikelDtoErsatz.getPersonalIIdFreigabe() == null) {
					bFreigabe = false;
				}

				dataSub[j][0] = artikelDtoErsatz.getCNr();
				dataSub[j][1] = artikelDtoErsatz.getCBezAusSpr();
				dataSub[j][2] = artikelDtoErsatz.getCZBezAusSpr();
				dataSub[j][3] = artikelDtoErsatz.getCZBez2AusSpr();
				dataSub[j][4] = artikelDtoErsatz.getTFreigabe();

				if (artikelDtoErsatz.getPersonalIIdFreigabe() != null) {
					dataSub[j][5] = getPersonalFac()
							.personalFindByPrimaryKeySmall(artikelDtoErsatz.getPersonalIIdFreigabe()).getCKurzzeichen();

				}

			}

			oZeile[REPORT_FREIGABE_SUBREPORT_ERSATZTYPEN_STKL] = new LPDatenSubreport(dataSub, fieldnames);

			ErsatztypenDto[] ersatztypenDtos = getArtikelFac().ersatztypenFindByArtikelIId(artikelDto.getIId());

			dataSub = new Object[ersatztypenDtos.length][fieldnames.length];

			for (int k = 0; k < ersatztypenDtos.length; k++) {
				com.lp.server.artikel.service.ArtikelDto artikelDtoErsatz = getArtikelFac()
						.artikelFindByPrimaryKeySmall(ersatztypenDtos[k].getArtikelIIdErsatz(), theClientDto);

				if (artikelDtoErsatz.getPersonalIIdFreigabe() == null) {
					bFreigabe = false;
				}

				dataSub[k][0] = artikelDtoErsatz.getCNr();
				dataSub[k][1] = artikelDtoErsatz.getCBezAusSpr();
				dataSub[k][2] = artikelDtoErsatz.getCZBezAusSpr();
				dataSub[k][3] = artikelDtoErsatz.getCZBez2AusSpr();
				dataSub[k][4] = artikelDtoErsatz.getTFreigabe();

				if (artikelDtoErsatz.getPersonalIIdFreigabe() != null) {
					dataSub[k][5] = getPersonalFac()
							.personalFindByPrimaryKeySmall(artikelDtoErsatz.getPersonalIIdFreigabe()).getCKurzzeichen();

				}

			}
			oZeile[REPORT_FREIGABE_SUBREPORT_ERSATZTYPEN_ARTIKEL] = new LPDatenSubreport(dataSub, fieldnames);

			oZeile[REPORT_FREIGABE_FREIGEGEBEN] = bFreigabe;

			if (bFreigabe == false) {
				bAllePositionenSindFreigegeben = false;
			}

			alDaten.add(oZeile);

		}

		data = new Object[alDaten.size()][REPORT_FREIGABE_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(data);

		reportParameter.put("P_FREIGEGEBEN", bAllePositionenSindFreigegeben);

		reportParameter.put("P_STUECKLISTE", dto.getArtikelDto().getCNr());

		reportParameter.put("P_FREIGABE_ZEITPUNKT", dto.getTFreigabe());

		if (dto.getPersonalIIdFreigabe() != null) {
			reportParameter.put("P_FREIGABE_PERSON", getPersonalFac()
					.personalFindByPrimaryKey(dto.getPersonalIIdFreigabe(), theClientDto).getCKurzzeichen());
		}

		if (dto.getArtikelDto().getArtikelsprDto() != null) {
			reportParameter.put("P_BEZEICHNUNG", dto.getArtikelDto().getArtikelsprDto().getCBez());
			reportParameter.put("P_ZUSATZBEZEICHNUNG", dto.getArtikelDto().getArtikelsprDto().getCZbez());
			reportParameter.put("P_ZUSATZBEZEICHNUNG2", dto.getArtikelDto().getArtikelsprDto().getCZbez2());
			reportParameter.put("P_KURZBEZEICHNUNG", dto.getArtikelDto().getArtikelsprDto().getCKbez());
		}

		initJRDS(reportParameter, StuecklisteReportFac.REPORT_MODUL, StuecklisteReportFac.REPORT_STUECKLISTE_FREIGABE,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	public JasperPrintLP printArbeitsplan(Integer stuecklisteIId, BigDecimal nLosgroesse, TheClientDto theClientDto) {

		index = -1;
		sAktuellerReport = StuecklisteReportFac.REPORT_STUECKLISTE_ARBEITSPLAN;

		FLRStuecklistearbeitsplan flrStuecklistearbeitsplan = new FLRStuecklistearbeitsplan();
		flrStuecklistearbeitsplan.setStueckliste_i_id(stuecklisteIId);

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session.createCriteria(FLRStuecklistearbeitsplan.class)
				.add(Example.create(flrStuecklistearbeitsplan));

		crit.addOrder(Order.asc(StuecklisteFac.FLR_STUECKLISTEARBEITSPLAN_I_ARBEITSGANG));
		crit.addOrder(Order.asc(StuecklisteFac.FLR_STUECKLISTEARBEITSPLAN_I_UNTERARBEITSGANG));
		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();
		data = new Object[results.size()][REPORT_ARBEITSPLAN_ANZAHL_SPALTEN];

		String mandantenWaehrung = null;
		try {
			mandantenWaehrung = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto)
					.getWaehrungCNr();
		} catch (RemoteException ex2) {
			throwEJBExceptionLPRespectOld(ex2);

		}
		// Losgreosse updaten
		getStuecklisteFac().updateStuecklisteLosgroesse(stuecklisteIId, nLosgroesse);

		ParametermandantDto parameter = null;
		try {
			parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_STUECKLISTE_ARBEITSPLAN_ZEITEINHEIT);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		String sEinheit = parameter.getCWert().trim();

		int row = 0;
		while (resultListIterator.hasNext()) {
			FLRStuecklistearbeitsplan stuecklistearbeitsplan = (FLRStuecklistearbeitsplan) resultListIterator.next();

			com.lp.server.artikel.service.ArtikelDto artikelDtoAP = getArtikelFac()
					.artikelFindByPrimaryKeySmall(stuecklistearbeitsplan.getFlrartikel().getI_id(), theClientDto);

			if (artikelDtoAP.getArtikelartCNr() != null && artikelDtoAP.getArtikelartCNr()
					.equals(com.lp.server.artikel.service.ArtikelFac.ARTIKELART_HANDARTIKEL)) {

				data[row][REPORT_ARBEITSPLAN_ARTIKEL] = "";

			} else {
				data[row][REPORT_ARBEITSPLAN_ARTIKEL] = artikelDtoAP.getCNr();

			}

			if (artikelDtoAP.getArtikelsprDto() != null) {
				data[row][REPORT_ARBEITSPLAN_ARTIKELBEZEICHNUNG] = artikelDtoAP.getArtikelsprDto().getCBez();
				data[row][REPORT_ARBEITSPLAN_ARTIKELZUSATZBEZEICHNUNG] = artikelDtoAP.getArtikelsprDto().getCZbez();
				data[row][REPORT_ARBEITSPLAN_ARTIKELZUSATZBEZEICHNUNG2] = artikelDtoAP.getArtikelsprDto().getCZbez2();
			}

			data[row][REPORT_ARBEITSPLAN_INDEX] = artikelDtoAP.getCIndex();
			data[row][REPORT_ARBEITSPLAN_REVISION] = artikelDtoAP.getCRevision();

			data[row][REPORT_ARBEITSPLAN_KOMMENTAR] = stuecklistearbeitsplan.getC_kommentar();
			data[row][REPORT_ARBEITSPLAN_ARBEITSGANG] = stuecklistearbeitsplan.getI_arbeitsgang();

			data[row][REPORT_ARBEITSPLAN_NUR_MASCHINENZEIT] = Helper
					.short2Boolean(stuecklistearbeitsplan.getB_nurmaschinenzeit());

			data[row][REPORT_ARBEITSPLAN_INITIALKOSTEN] = Helper.short2Boolean(stuecklistearbeitsplan.getB_initial());

			data[row][REPORT_ARBEITSPLAN_UNTERARBEITSGANG] = stuecklistearbeitsplan.getI_unterarbeitsgang();
			data[row][REPORT_ARBEITSPLAN_AUFSPANNUNG] = stuecklistearbeitsplan.getI_aufspannung();

			try {
				StuecklistearbeitsplanDto stuecklistearbeitsplanDto = getStuecklisteFac()
						.stuecklistearbeitsplanFindByPrimaryKey(stuecklistearbeitsplan.getI_id(), theClientDto);
				data[row][REPORT_ARBEITSPLAN_AGART] = stuecklistearbeitsplanDto.getAgartCNr();
				data[row][REPORT_ARBEITSPLAN_LANGTEXT] = stuecklistearbeitsplanDto.getXLangtext();

				if (stuecklistearbeitsplan.getFlrmaschine() != null) {

					String maschine = "";
					if (stuecklistearbeitsplan.getFlrmaschine().getC_identifikationsnr() != null) {
						maschine += stuecklistearbeitsplan.getFlrmaschine().getC_identifikationsnr() + " ";
					}
					if (stuecklistearbeitsplan.getFlrmaschine().getC_bez() != null) {
						maschine += stuecklistearbeitsplan.getFlrmaschine().getC_bez() + " ";
					}
					if (stuecklistearbeitsplan.getFlrmaschine().getC_inventarnummer() != null) {
						maschine += stuecklistearbeitsplan.getFlrmaschine().getC_inventarnummer();
					}

					data[row][REPORT_ARBEITSPLAN_MASCHINE] = maschine;
					data[row][REPORT_ARBEITSPLAN_KOSTEN_MASCHINE] = getZeiterfassungFac()
							.getMaschinenKostenZumZeitpunkt(stuecklistearbeitsplan.getMaschine_i_id(),
									new Timestamp(System.currentTimeMillis()), LocaleFac.BELEGART_STUECKLISTE,
									stuecklistearbeitsplanDto.getIId());
				}

				// PJ19960
				AlternativmaschineDto[] alternativmaschineDtos = getStuecklisteFac()
						.alternativmaschineFindByStuecklistearbeitsplanIId(stuecklistearbeitsplan.getI_id());

				if (alternativmaschineDtos != null && alternativmaschineDtos.length > 0) {

					String[] fieldnames = new String[] { "Inventarnummer", "Identifikationsnummer", "Bezeichnung",
							"Maschinengruppe", "Korrekturfaktor", "Maschinenkosten", "Seriennummer" };
					Object[][] dataSub = new Object[alternativmaschineDtos.length][fieldnames.length];

					for (int i = 0; i < alternativmaschineDtos.length; i++) {
						MaschineDto mDto = getZeiterfassungFac()
								.maschineFindByPrimaryKey(alternativmaschineDtos[i].getMaschineIId());
						dataSub[i][0] = mDto.getCInventarnummer();
						dataSub[i][1] = mDto.getCIdentifikationsnr();
						dataSub[i][2] = mDto.getCBez();

						if (mDto.getMaschinengruppeIId() != null) {
							dataSub[i][3] = getZeiterfassungFac()
									.maschinengruppeFindByPrimaryKey(mDto.getMaschinengruppeIId()).getCBez();
						}

						dataSub[i][4] = alternativmaschineDtos[i].getNKorrekturfaktor();

						dataSub[i][5] = getZeiterfassungFac().getMaschinenKostenZumZeitpunkt(mDto.getIId(),
								new Timestamp(System.currentTimeMillis()), LocaleFac.BELEGART_STUECKLISTE,
								stuecklistearbeitsplanDto.getIId());
						dataSub[i][6] = mDto.getCSeriennummer();

					}

					data[row][REPORT_ARBEITSPLAN_SUBREPORT_ALTERNATIVE_MASCHINEN] = new LPDatenSubreport(dataSub,
							fieldnames);

				}

				if (stuecklistearbeitsplanDto.getStuecklistepositionIId() != null) {
					StuecklistepositionDto posDto = getStuecklisteFac().stuecklistepositionFindByPrimaryKey(
							stuecklistearbeitsplanDto.getStuecklistepositionIId(), theClientDto);
					ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(posDto.getArtikelIId(),
							theClientDto);
					data[row][REPORT_ARBEITSPLAN_FREMDMATERIAL_ARTIKEL] = artikelDto.getCNr();
					if (artikelDto.getArtikelsprDto() != null) {
						data[row][REPORT_ARBEITSPLAN_FREMDMATERIAL_ARTIKELBEZEICHNUNG] = artikelDto.getArtikelsprDto()
								.getCBez();
						data[row][REPORT_ARBEITSPLAN_FREMDMATERIAL_ARTIKELKURZBEZEICHNUNG] = artikelDto
								.getArtikelsprDto().getCKbez();
						data[row][REPORT_ARBEITSPLAN_FREMDMATERIAL_ARTIKELZUSATZBEZEICHNUNG] = artikelDto
								.getArtikelsprDto().getCZbez();
						data[row][REPORT_ARBEITSPLAN_FREMDMATERIAL_ARTIKELZUSATZBEZEICHNUNG2] = artikelDto
								.getArtikelsprDto().getCZbez2();
					}
					data[row][REPORT_ARBEITSPLAN_FREMDMATERIAL_SOLLMENGE] = posDto.getNMenge();
				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			double lStueckzeit = stuecklistearbeitsplan.getL_stueckzeit().longValue();
			double lRuestzeit = stuecklistearbeitsplan.getL_ruestzeit().longValue();

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

			data[row][REPORT_ARBEITSPLAN_RUESTZEIT] = Helper.rundeKaufmaennisch(new BigDecimal(dRuestzeit), 5);
			data[row][REPORT_ARBEITSPLAN_STUECKZEIT] = Helper.rundeKaufmaennisch(new BigDecimal(dStueckzeit), 5);
			data[row][REPORT_ARBEITSPLAN_PREIS] = new BigDecimal(0);
			if (stuecklistearbeitsplan.getFlrstueckliste().getN_losgroesse() != null) {

				data[row][REPORT_ARBEITSPLAN_GESAMTZEIT] = Helper.berechneGesamtzeitInStunden((long) lRuestzeit,
						(long) lStueckzeit, stuecklistearbeitsplan.getFlrstueckliste().getN_losgroesse(),
						stuecklistearbeitsplan.getFlrstueckliste().getN_erfassungsfaktor(),
						stuecklistearbeitsplan.getI_aufspannung());

				try {
					ArtikellieferantDto artikellieferantDto = getArtikelFac()
							.getArtikelEinkaufspreisDesBevorzugtenLieferanten(
									stuecklistearbeitsplan.getFlrartikel().getI_id(),
									(BigDecimal) data[row][REPORT_ARBEITSPLAN_GESAMTZEIT], mandantenWaehrung,
									theClientDto);

					if (artikellieferantDto != null) {
						data[row][REPORT_ARBEITSPLAN_PREIS] = artikellieferantDto.getLief1Preis();
					}
				} catch (RemoteException ex1) {
					throwEJBExceptionLPRespectOld(ex1);
				}

			}

			row++;
		}
		session.close();
		HashMap<String, Object> reportParameter = new HashMap<String, Object>();
		StuecklisteDto dto = getStuecklisteFac().stuecklisteFindByPrimaryKey(stuecklisteIId, theClientDto);

		reportParameter.put("P_STUECKLISTE", dto.getArtikelDto().getCNr());
		reportParameter.put("P_EINHEIT", dto.getArtikelDto().getEinheitCNr());
		reportParameter.put("P_LOSGROESSE", dto.getNLosgroesse());
		reportParameter.put("P_WAEHRUNG", mandantenWaehrung);

		reportParameter.put("P_INDEX", dto.getArtikelDto().getCIndex());
		reportParameter.put("P_REVISION", dto.getArtikelDto().getCRevision());
		reportParameter.put("P_REFERENZNUMMER", dto.getArtikelDto().getCReferenznr());

		reportParameter.put("P_ARBEITSPLAN_ZEITEINHEIT", sEinheit);

		if (dto.getArtikelDto().getArtikelsprDto() != null) {
			reportParameter.put("P_BEZEICHNUNG", dto.getArtikelDto().getArtikelsprDto().getCBez());
			reportParameter.put("P_ZUSATZBEZEICHNUNG", dto.getArtikelDto().getArtikelsprDto().getCZbez());
			reportParameter.put("P_ZUSATZBEZEICHNUNG2", dto.getArtikelDto().getArtikelsprDto().getCZbez2());
			reportParameter.put("P_KURZBEZEICHNUNG", dto.getArtikelDto().getArtikelsprDto().getCKbez());
		}

		initJRDS(reportParameter, StuecklisteReportFac.REPORT_MODUL,
				StuecklisteReportFac.REPORT_STUECKLISTE_ARBEITSPLAN, theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printStuecklisteAllgemeinMitPreis(Integer stuecklisteIId, Integer iOptionPreis,
			Boolean bMitPositionskommentar, Boolean bMitStuecklistenkommentar, Boolean bUnterstuecklistenEinbinden,
			Boolean bGleichePositionenZusammenfassen, Integer iOptionSortierungUnterstuecklisten,
			boolean bUnterstklstrukurBelassen, TheClientDto theClientDto, Integer iOptionSortierungStuecklisteGesamt1,
			Integer iOptionSortierungStuecklisteGesamt2, Integer iOptionSortierungStuecklisteGesamt3,
			boolean fremdfertigungAufloesen) {

		if (stuecklisteIId == null || iOptionPreis == null || bMitPositionskommentar == null
				|| bMitStuecklistenkommentar == null || bUnterstuecklistenEinbinden == null
				|| bGleichePositionenZusammenfassen == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"stuecklisteIId == null || iOptionPreis == null || bMitPositionskommentar == null || bMitStuecklistenkommentar == null || bUnterstuecklistenEinbinden == null || bGleichePositionenZusammenfassen == null"));
		}
		if (bUnterstuecklistenEinbinden.booleanValue() == true && iOptionSortierungUnterstuecklisten == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"bUnterstuecklistenEinbinden.booleanValue()==true && iOptionSortierungUnterstuecklisten==null"));
		}
		StuecklisteDto dto = getStuecklisteFac().stuecklisteFindByPrimaryKey(stuecklisteIId, theClientDto);
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		KundeDto kdDto = null;
		MwstsatzDto mwstsatzDtoAktuell = null;
		try {
			if (dto.getPartnerIId() != null) {
				kdDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(dto.getPartnerIId(),
						theClientDto.getMandant(), theClientDto);
			}

			if (kdDto != null) {
//				mwstsatzDtoAktuell = getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(kdDto.getMwstsatzbezIId(),
//						theClientDto);
				// SP8308 Die Stueckliste wird "jetzt" (= belegdatum) ausgedruckt
				mwstsatzDtoAktuell = getMandantFac().mwstsatzZuDatumValidate(kdDto.getMwstsatzbezIId(),
						Helper.cutTimestamp(getTimestamp()), theClientDto);
				PartnerDto pDto = getPartnerFac().partnerFindByPrimaryKey(kdDto.getPartnerIId(), theClientDto);
				parameter.put("P_KUNDE", pDto.formatFixTitelName1Name2());
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		// Report und Datasoure initialisieren
		index = -1;
		sAktuellerReport = StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEIN_MITPREIS;
		// Strukurdaten holen
		List<?> m = null;
		try {
			m = getStuecklisteFac().getStrukturDatenEinerStueckliste(stuecklisteIId, theClientDto,
					iOptionSortierungUnterstuecklisten.intValue(), 0, null, bUnterstuecklistenEinbinden.booleanValue(),
					bGleichePositionenZusammenfassen.booleanValue(), new BigDecimal(1), null, bUnterstklstrukurBelassen,
					fremdfertigungAufloesen);
		} catch (RemoteException ex3) {
			throwEJBExceptionLPRespectOld(ex3);
		}

		String sPreis = null;
		// Mandantenwaehrung holen
		String sMandantenwaehrung = null;
		boolean bGestpreisberechnungHauptlager = true;
		Integer hauptlagerIId = null;
		try {
			sMandantenwaehrung = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto)
					.getWaehrungCNr();
			hauptlagerIId = getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId();
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

		try {
			ParametermandantDto parameterGestpreisBerechnung = null;
			parameterGestpreisBerechnung = (ParametermandantDto) getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_GESTPREISBERECHNUNG_HAUPTLAGER);

			bGestpreisberechnungHauptlager = ((Boolean) (parameterGestpreisBerechnung.getCWertAsObject()))
					.booleanValue();
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

		Iterator<?> it = m.listIterator();
		data = new Object[m.size()][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ANZAHL_SPALTEN];
		int row = 0;
		while (it.hasNext()) {
			StuecklisteMitStrukturDto struktur = (StuecklisteMitStrukturDto) it.next();
			StuecklistepositionDto position = struktur.getStuecklistepositionDto();

			// Artikelkommentar Text und Bild
			ArtikelkommentarDto[] aKommentarDto = null;
			try {
				aKommentarDto = getArtikelkommentarFac().artikelkommentardruckFindByArtikelIIdBelegartCNr(
						position.getArtikelIId(), LocaleFac.BELEGART_STUECKLISTE, theClientDto.getLocUiAsString(),
						theClientDto);
			} catch (RemoteException ex3) {
			} catch (EJBExceptionLP ex3) {
			}

			// Einrueckung fuer Unterstuecklisten
			String einrueckung = "";
			for (int i = 0; i < struktur.getIEbene(); i++) {
				einrueckung = einrueckung + "   ";
			}

			try {

				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_I_EBENE] = struktur.getIEbene();
				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_FREMDFERTIGUNG] = struktur.isBFremdfertigung();
				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_FREIGABE_PERSON] = struktur
						.getCKurzzeichenPersonFreigabe();
				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_FREIGABE_ZEITPUNKT] = struktur.getTFreigabe();
				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_STUECKLISTEPOSITION_I_ID] = position.getIId();
				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_INITIALKOSTEN] = Helper
						.short2Boolean(position.getBInitial());
				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_AUF_BELEG_MITDRUCKEN] = Helper
						.short2Boolean(position.getBMitdrucken());

				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KALK_PREIS] = position.getNKalkpreis();
				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KOMMENTAR_POSITION] = position.getCKommentar();

				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_SUBREPORT_ARTIKELKOMMENTAR] = getSubreportArtikelkommentar(
						position.getArtikelIId(), LocaleFac.BELEGART_STUECKLISTE, theClientDto);

				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_I_SORT] = Helper
						.fitString2LengthAlignRight(position.getISort() + "", 10, '0');

				// Artikel + Einheit holen
				com.lp.server.artikel.service.ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(position.getArtikelIId(), theClientDto);

				String sArtikelKommentar = "";
				if (artikelDto.getArtikelartCNr()
						.equals(com.lp.server.artikel.service.ArtikelFac.ARTIKELART_HANDARTIKEL)) {

					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKEL] = "";

				} else {
					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKEL] = einrueckung + artikelDto.getCNr();

					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKEL_FREIGABE_ZEITPUNKT] = artikelDto
							.getTFreigabe();

					if (artikelDto.getPersonalIIdFreigabe() != null) {
						data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKEL_FREIGABE_PERSON] = getPersonalFac()
								.personalFindByPrimaryKeySmall(artikelDto.getPersonalIIdFreigabe()).getCKurzzeichen();
					}

					if (kdDto != null) {
						// KundeArtikelnr gueltig zu jett
						KundesokoDto kundeSokoDto_gueltig = this.getKundesokoFac()
								.kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc(kdDto.getIId(),
										artikelDto.getIId(),
										Helper.cutDate(new java.sql.Date(System.currentTimeMillis())));
						if (kundeSokoDto_gueltig != null) {
							data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KUNDENARTIKELNUMMER] = kundeSokoDto_gueltig
									.getCKundeartikelnummer();
							data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KUNDENARTIKELBEZEICHNUNG] = kundeSokoDto_gueltig
									.getCKundeartikelbez();
						}
					}

					Image imageKommentar = null;
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
								data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_IMAGE] = imageKommentar;
							}
						}
					}

				}
				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_GEWICHT] = artikelDto.getFGewichtkg();
				String sBezeichnung = artikelDto.getArtikelsprDto().getCBez();
				if (sArtikelKommentar != "") {
					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KOMMENTAR] = sArtikelKommentar;
				}
				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKELBEZEICHNUNG] = sBezeichnung;

				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKELZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto()
						.getCZbez();
				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKELZUSATZBEZEICHNUNG2] = artikelDto
						.getArtikelsprDto().getCZbez2();
				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_REFERENZNUMMER] = artikelDto.getCReferenznr();

				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKEL_HERSTELLERNUMMER] = artikelDto
						.getCArtikelnrhersteller();
				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKEL_HERSTELLERBEZEICHNUNG] = artikelDto
						.getCArtikelbezhersteller();

				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_POSITION] = position.getCPosition();
				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_MONTAGEART] = position.getMontageartDto().getCBez();
				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_LFDNUMMER] = position.getILfdnummer();
				if (artikelDto.getArtikelsprDto() != null) {
					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KURZBEZEICHNUNG] = artikelDto.getArtikelsprDto()
							.getCKbez();
				}

				if (artikelDto.getMaterialIId() != null) {
					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKEL_MATERIAL] = getMaterialFac()
							.materialFindByPrimaryKey(artikelDto.getMaterialIId(), theClientDto).getBezeichnung();
				}

				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_BEWILLIGUNGSPFLICHTIG] = Helper
						.short2Boolean(artikelDto.getBBewilligungspflichtig());
				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_MELDEPFLICHTIG] = Helper
						.short2Boolean(artikelDto.getBMeldepflichtig());

				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_INDEX] = artikelDto.getCIndex();
				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_REVISION] = artikelDto.getCRevision();

				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ZIELMENGENEINHEIT] = artikelDto.getEinheitCNr();

				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_WARENVERKEHRSNUMMER] = artikelDto
						.getCWarenverkehrsnummer();

				if (artikelDto.getLandIIdUrsprungsland() != null) {

					LandDto landDto = getSystemFac().landFindByPrimaryKey(artikelDto.getLandIIdUrsprungsland());
					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_URSPRUNGSLAND_LKZ] = landDto.getCLkz();
					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_URSPRUNGSLAND_NAME] = landDto.getCName();
				}

				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_HOCHSTELLEN] = "N";
				if (artikelDto.getMontageDto() != null
						&& Helper.short2boolean(artikelDto.getMontageDto().getBHochstellen()) == true) {
					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_HOCHSTELLEN] = "J";
				}
				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_HOCHSETZEN] = "N";
				if (artikelDto.getMontageDto() != null
						&& Helper.short2boolean(artikelDto.getMontageDto().getBHochsetzen()) == true) {
					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_HOCHSETZEN] = "J";
				}

				if (artikelDto.getMontageDto() != null) {
					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_RASTERLIEGEND] = artikelDto.getMontageDto()
							.getFRasterliegend();
					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_RASTERSTEHEND] = artikelDto.getMontageDto()
							.getFRasterstehend();
				}

				if (artikelDto.getHerstellerIId() != null) {
					HerstellerDto herstellerDto = getArtikelFac()
							.herstellerFindByPrimaryKey(artikelDto.getHerstellerIId(), theClientDto);

					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_HERSTELLER] = herstellerDto.getCNr();
					PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(herstellerDto.getPartnerIId(),
							theClientDto);
					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_HERSTELLER_NAME] = partnerDto.formatFixName1Name2();
				}

				if (position.getFDimension1() == null) {
					position.setFDimension1(new Float(1));
				} else if (position.getFDimension2() == null) {
					position.setFDimension2(new Float(1));
				}
				if (position.getFDimension3() == null) {
					position.setFDimension3(new Float(1));
				}

				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_MENGE] = position.getNMenge();

				// Positionseinheit nach Zieleinheit umrechnen
				BigDecimal preis = new BigDecimal(0);
				BigDecimal kundenpreis = new BigDecimal(0);

				if (iOptionPreis.intValue() == StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_PREIS_EINKAUFSPREIS) {
					ArtikellieferantDto artikellieferantDto = getArtikelFac()
							.getArtikelEinkaufspreisDesBevorzugtenLieferanten(artikelDto.getIId(), position.getNMenge(),
									sMandantenwaehrung, theClientDto);
					sPreis = "Lief1";
					if (artikellieferantDto != null && artikellieferantDto.getLief1Preis() != null) {
						preis = artikellieferantDto.getLief1Preis();
					}

				} else if (iOptionPreis
						.intValue() == StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_PREIS_GESTEHUNGSSPREIS) {

					if (bGestpreisberechnungHauptlager == true) {

						preis = getLagerFac().getGemittelterGestehungspreisEinesLagers(artikelDto.getIId(),
								hauptlagerIId, theClientDto);
					} else {
						preis = getLagerFac().getGemittelterGestehungspreisAllerLaegerEinesMandanten(
								artikelDto.getIId(), theClientDto);

					}
					sPreis = "Gest";
				} else if (iOptionPreis
						.intValue() == StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_PREIS_VERKAUFSSPREIS) {
					VkPreisfindungEinzelverkaufspreisDto vkpreisDto = getVkPreisfindungFac()
							.getArtikeleinzelverkaufspreis(artikelDto.getIId(), null,
									theClientDto.getSMandantenwaehrung(), theClientDto);
					if (vkpreisDto != null && vkpreisDto.getNVerkaufspreisbasis() != null) {
						preis = vkpreisDto.getNVerkaufspreisbasis();
					}
					sPreis = "VK";

					if (kdDto != null && mwstsatzDtoAktuell != null) {
						VkpreisfindungDto vkpreisfindungDto = getVkPreisfindungFac().verkaufspreisfindung(
								artikelDto.getIId(), kdDto.getIId(), position.getNZielmenge(),
								new java.sql.Date(System.currentTimeMillis()),
								kdDto.getVkpfArtikelpreislisteIIdStdpreisliste(), mwstsatzDtoAktuell.getIId(),
								theClientDto.getSMandantenwaehrung(), theClientDto);

						VerkaufspreisDto kundenVKPreisDto = Helper.getVkpreisBerechnet(vkpreisfindungDto);
						if (kundenVKPreisDto != null) {
							kundenpreis = kundenVKPreisDto.nettopreis;
							data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_MATERIALZUSCHLAG] = kundenVKPreisDto.bdMaterialzuschlag;
						}

					}

				}

				// Wenn Stuecklsite, dann wird fuer den Kopf kein Preis
				// angedruckt, da ja sonst alles Doppelt gerechnet wird
				if (!struktur.isBStueckliste()) {
					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_PREIS] = preis;
					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KUNDENPREIS] = kundenpreis;
					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_WERT] = position.getNZielmenge().multiply(preis);

					// PJ22126
					Session session = FLRSessionFactory.getFactory().openSession();
					String sQuery = "SELECT wep FROM FLRWareneingangspositionen AS wep WHERE wep.flrbestellposition.flrartikel.i_id="
							+ artikelDto.getIId();
					sQuery += " ORDER BY wep.flrwareneingang.t_wareneingansdatum DESC ";
					org.hibernate.Query hquery = session.createQuery(sQuery);
					hquery.setMaxResults(1);
					List<?> resultList = hquery.list();
					Iterator<?> resultListIterator = resultList.iterator();
					if (resultListIterator.hasNext()) {
						FLRWareneingangspositionen wep = (FLRWareneingangspositionen) resultListIterator.next();
						BigDecimal kurs = new BigDecimal(wep.getFlrwareneingang().getFlrbestellung()
								.getF_wechselkursmandantwaehrungbestellungswaehrung());
						if (kurs.doubleValue() != 0) {
							data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_PREIS_LETZTE_WEP] = wep
									.getN_gelieferterpreis().divide(kurs, 4, BigDecimal.ROUND_HALF_EVEN);
						}
					}

				}
				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ZIELMENGE] = position.getNZielmenge();

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

			row++;
		}

		if (!bUnterstklstrukurBelassen) {

			// Nach Fertigungsgruppe sortieren
			for (int k = data.length - 1; k > 0; --k) {
				for (int j = 0; j < k; ++j) {
					Object[] a1 = (Object[]) data[j];
					Object[] a2 = (Object[]) data[j + 1];

					if (iOptionSortierungUnterstuecklisten == StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_ARTIKELNR) {
						if (((String) a1[REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKEL])
								.compareTo((String) a2[REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKEL]) > 0) {
							data[j] = a2;
							data[j + 1] = a1;

						}
					} else if (iOptionSortierungUnterstuecklisten == StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_POSITION) {

						String s1 = (String) a1[REPORT_STUECKLISTE_ALLGEMEINMITPREIS_POSITION];
						if (s1 == null) {
							s1 = "";
						}
						String s2 = (String) a2[REPORT_STUECKLISTE_ALLGEMEINMITPREIS_POSITION];
						if (s2 == null) {
							s2 = "";
						}

						if (s1.compareTo(s2) > 0) {
							data[j] = a2;
							data[j + 1] = a1;

						}

					}

				}

			}

		}

		parameter.put("P_WAEHRUNG", sMandantenwaehrung);
		parameter.put("P_PREIS", sPreis);
		if (bMitStuecklistenkommentar.booleanValue() == true) {
			parameter.put("P_KOMMENTAR", Helper.formatStyledTextForJasper(dto.getXKommentar()));
		}
		parameter.put("P_STUECKLISTE", dto.getArtikelDto().getCNr());

		parameter.put("P_STUECKLISTE_BEWILLIGUNGSPFLICHTIG",
				Helper.short2Boolean(dto.getArtikelDto().getBBewilligungspflichtig()));
		parameter.put("P_STUECKLISTE_MELDEPFLICHTIG", Helper.short2Boolean(dto.getArtikelDto().getBMeldepflichtig()));

		parameter.put("P_ERFASSUNGSFAKTOR", dto.getNErfassungsfaktor());
		parameter.put("P_STUECKLISTEBEZEICHNUNG", dto.getArtikelDto().formatBezeichnung());
		if (dto.getArtikelDto().getArtikelsprDto() != null) {
			parameter.put("P_STUECKLISTEKURZBEZEICHNUNG", dto.getArtikelDto().getArtikelsprDto().getCKbez());
			parameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG", dto.getArtikelDto().getArtikelsprDto().getCZbez());
			parameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG2", dto.getArtikelDto().getArtikelsprDto().getCZbez2());
		}
		parameter.put("P_ANLEGEN", dto.getTAnlegen());
		parameter.put("P_AENDERN", dto.getTAendern());
		parameter.put("P_AENDERN_POSITION", dto.getTAendernposition());
		parameter.put("P_AENDERN_ARBEITSPLAN", dto.getTAendernarbeitsplan());

		parameter.put("P_FREMDFERTIGUNG_AUFLOESEN", fremdfertigungAufloesen);

		parameter.put("P_FREIGABE_ZEITPUNKT", dto.getTFreigabe());
		if (dto.getPersonalIIdFreigabe() != null) {
			parameter.put("P_FREIGABE_PERSON", getPersonalFac()
					.personalFindByPrimaryKey(dto.getPersonalIIdFreigabe(), theClientDto).getCKurzzeichen());
		}

		try {
			if (dto.getArtikelDto().getLandIIdUrsprungsland() != null) {
				LandDto landDto = getSystemFac().landFindByPrimaryKey(dto.getArtikelDto().getLandIIdUrsprungsland());

				String s = landDto.getCName();
				if (getSystemFac().isEUMitglied(landDto)) {
					s += " (" + getTextRespectUISpr("lp.eumitglied", theClientDto.getMandant(), theClientDto.getLocUi())
							+ ")";
				}

				parameter.put("P_URSPRUNGSLAND", s);

			}
			// Zeichnungsnummer
			StuecklisteeigenschaftDto[] stuecklisteeigenschaftDtos = getStuecklisteFac()
					.stuecklisteeigenschaftFindByStuecklisteIId(dto.getIId());
			ArrayList<Object[]> al = new ArrayList<Object[]>();
			for (int i = 0; i < stuecklisteeigenschaftDtos.length; i++) {
				StuecklisteeigenschaftDto stuecklisteeigenschaftDto = stuecklisteeigenschaftDtos[i];

				Object[] o = new Object[2];
				o[0] = stuecklisteeigenschaftDto.getStuecklisteeigenschaftartDto().getCBez();
				o[1] = stuecklisteeigenschaftDto.getCBez();
				al.add(o);
			}

			if (stuecklisteeigenschaftDtos.length > 0) {
				String[] fieldnames = new String[] { "F_EIGENSCHAFTART", "F_BEZEICHNUNG" };
				Object[][] dataSub = new Object[al.size()][fieldnames.length];
				dataSub = (Object[][]) al.toArray(dataSub);

				parameter.put("DATENSUBREPORT", new LPDatenSubreport(dataSub, fieldnames));
			}

		} catch (RemoteException ex4) {
			throwEJBExceptionLPRespectOld(ex4);
		}

		// Nun nach Kriterium 1/2/3 sortieren PJ 06/2418
		if (!bUnterstuecklistenEinbinden) {
			data = sortiereStuecklistendruck(data, iOptionSortierungStuecklisteGesamt1,
					iOptionSortierungStuecklisteGesamt2, iOptionSortierungStuecklisteGesamt3, true);
		}

		// PJ15813 Ersatztypen hinzufuegen
		if (bGleichePositionenZusammenfassen == false) {

			ArrayList alDaten = new ArrayList();
			for (int i = 0; i < data.length; i++) {

				alDaten.add(data[i]);

				PosersatzDto[] posersatzDtos = getStuecklisteFac().posersatzFindByStuecklistepositionIId(
						(Integer) data[i][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_STUECKLISTEPOSITION_I_ID]);

				for (int j = 0; j < posersatzDtos.length; j++) {
					Object[] neueZeile = data[i].clone();

					String einrueckung = "";
					for (int k = 0; k < (Integer) data[i][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_I_EBENE]; k++) {
						einrueckung = einrueckung + "   ";
					}

					com.lp.server.artikel.service.ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(posersatzDtos[j].getArtikelIIdErsatz(), theClientDto);

					neueZeile[REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ERSATZARTIKEL] = einrueckung + artikelDto.getCNr();

					neueZeile[REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ERSATZARTIKEL_BEZEICHNUNG] = artikelDto
							.getCBezAusSpr();

					neueZeile[REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ERSATZARTIKEL_HERSTELLERNUMMER] = artikelDto
							.getCArtikelnrhersteller();
					neueZeile[REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ERSATZARTIKEL_HERSTELLERBEZEICHNUNG] = artikelDto
							.getCArtikelbezhersteller();

					if (artikelDto.getArtikelsprDto() != null) {
						neueZeile[REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ERSATZARTIKEL_ZUSATZBEZEICHNUNG] = artikelDto
								.getArtikelsprDto().getCZbez();
						neueZeile[REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ERSATZARTIKEL_ZUSATZBEZEICHNUNG2] = artikelDto
								.getArtikelsprDto().getCZbez2();
					}

					neueZeile[REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ZIELMENGE] = new BigDecimal(0);

					alDaten.add(neueZeile);

				}

			}

			data = new Object[alDaten.size()][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ANZAHL_SPALTEN];
			data = (Object[][]) alDaten.toArray(data);

		}

		initJRDS(parameter, StuecklisteReportFac.REPORT_MODUL,
				StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEIN_MITPREIS, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printPruefkombinationen(Integer artikelIIdKontakt, Integer artikelIIdLitze,
			String bezeichnungLitze, TheClientDto theClientDto) {

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		sAktuellerReport = StuecklisteReportFac.REPORT_STUECKLISTE_PRUEFKOMBINATION;

		SessionFactory factory = FLRSessionFactory.getFactory();
		org.hibernate.Session session = factory.openSession();

		String sLocUI;
		sLocUI = Helper.locale2String(theClientDto.getLocUi());
		session.enableFilter("filterLocale").setParameter("paramLocale", sLocUI);

		String queryString = "SELECT pk FROM FLRPruefkombination pk  LEFT OUTER JOIN pk.flrartikel_litze.artikelsprset AS litzespr WHERE 1=1 ";

		if (artikelIIdKontakt != null) {
			queryString += " AND pk.artikel_i_id_kontakt=" + artikelIIdKontakt;
			parameter.put("P_KONTAKT", getArtikelFac().artikelFindByPrimaryKeySmall(artikelIIdKontakt, theClientDto)
					.formatArtikelbezeichnung());

		}
		if (artikelIIdLitze != null) {
			queryString += " AND pk.artikel_i_id_litze=" + artikelIIdLitze;
			parameter.put("P_LITZE", getArtikelFac().artikelFindByPrimaryKeySmall(artikelIIdLitze, theClientDto)
					.formatArtikelbezeichnung());
		}

		if (bezeichnungLitze != null) {

			parameter.put("P_TEXTSUCHE_LITZE", bezeichnungLitze);

			StringBuffer where = new StringBuffer("");
			String suchstring = "lower(coalesce(litzespr.c_bez,'')||' '||coalesce(litzespr.c_kbez,'')||' '||coalesce(litzespr.c_zbez,'')||' '||coalesce(litzespr.c_zbez2,''))";

			String[] teile = bezeichnungLitze.toLowerCase().split(" ");
			where.append("(");

			for (int p = 0; p < teile.length; p++) {

				if (teile[p].startsWith("-")) {
					where.append(" NOT ");

					teile[p] = teile[p].substring(1);

				}

				where.append("lower(" + suchstring + ") like '%" + teile[p].toLowerCase() + "%'");
				if (p < teile.length - 1) {
					where.append(" AND ");
				}
			}

			where.append(") ");

			queryString += "AND " + where.toString();

		}

		queryString += " ORDER BY pk.flrpruefart.c_nr, pk.flrartikel_kontakt.c_nr, pk.flrartikel_litze.c_nr";
		org.hibernate.Query query = session.createQuery(queryString);

		List<?> resultList = query.list();
		Iterator it = resultList.iterator();

		ArrayList alDaten = new ArrayList();

		while (it.hasNext()) {
			FLRPruefkombination pk = (FLRPruefkombination) it.next();

			Object[] zeile = new Object[REPORT_PRUEFKOMBINATION_ANZAHL_SPALTEN];

			zeile[REPORT_PRUEFKOMBINATION_ARTIKEL_KONTAKT] = pk.getFlrartikel_kontakt().getC_nr();

			ArtikelDto aDtoKontakt = getArtikelFac().artikelFindByPrimaryKeySmall(pk.getFlrartikel_kontakt().getI_id(),
					theClientDto);

			if (aDtoKontakt.getArtikelsprDto() != null) {
				zeile[REPORT_PRUEFKOMBINATION_ARTIKELBEZEICHNUNG_KONTAKT] = aDtoKontakt.getArtikelsprDto().getCBez();
			}

			if (pk.getFlrartikel_litze() != null
					&& (pk.getFlrpruefart().getC_nr().equals(StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO)
							|| pk.getFlrpruefart().getC_nr().equals(StuecklisteFac.PRUEFART_CRIMPEN_OHNE_ISO))) {

				zeile[REPORT_PRUEFKOMBINATION_ARTIKEL_LITZE] = pk.getFlrartikel_litze().getC_nr();
				ArtikelDto aDtoLitze = getArtikelFac().artikelFindByPrimaryKeySmall(pk.getFlrartikel_litze().getI_id(),
						theClientDto);

				if (aDtoLitze.getArtikelsprDto() != null) {
					zeile[REPORT_PRUEFKOMBINATION_ARTIKELBEZEICHNUNG_LITZE] = aDtoLitze.getArtikelsprDto().getCBez();
				}
			}

			if (pk.getFlrartikel_litze2() != null
					&& (pk.getFlrpruefart().getC_nr().equals(StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO)
							|| pk.getFlrpruefart().getC_nr().equals(StuecklisteFac.PRUEFART_CRIMPEN_OHNE_ISO))) {

				zeile[REPORT_PRUEFKOMBINATION_ARTIKEL_LITZE2] = pk.getFlrartikel_litze2().getC_nr();
				ArtikelDto aDtoLitze = getArtikelFac().artikelFindByPrimaryKeySmall(pk.getFlrartikel_litze2().getI_id(),
						theClientDto);

				if (aDtoLitze.getArtikelsprDto() != null) {
					zeile[REPORT_PRUEFKOMBINATION_ARTIKELBEZEICHNUNG_LITZE2] = aDtoLitze.getArtikelsprDto().getCBez();
				}
			}

			if (pk.getFlrverschleissteil() != null
					&& (pk.getFlrpruefart().getC_nr().equals(StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO)
							|| pk.getFlrpruefart().getC_nr().equals(StuecklisteFac.PRUEFART_CRIMPEN_OHNE_ISO))) {
				zeile[REPORT_PRUEFKOMBINATION_VERSCHLEISSTEILNUMMER] = pk.getFlrverschleissteil().getC_nr();
				zeile[REPORT_PRUEFKOMBINATION_VERSCHLEISSTEILBEZEICHNUNG] = pk.getFlrverschleissteil().getC_bez();
			}

			zeile[REPORT_PRUEFKOMBINATION_CRIMPBREITE_DRAHT] = pk.getN_crimpbreite_draht();
			zeile[REPORT_PRUEFKOMBINATION_CRIMPBREITE_ISOLATION] = pk.getN_crimpbreite_isolation();
			zeile[REPORT_PRUEFKOMBINATION_CRIMPHOEHE_DRAHT] = pk.getN_crimphoehe_draht();
			zeile[REPORT_PRUEFKOMBINATION_CRIMPHOEHE_ISOLATION] = pk.getN_crimphoehe_isolation();

			zeile[REPORT_PRUEFKOMBINATION_TOLERANZ_CRIMPBREITE_DRAHT] = pk.getN_toleranz_crimpbreite_draht();
			zeile[REPORT_PRUEFKOMBINATION_TOLERANZ_CRIMPBREITE_ISOLATION] = pk.getN_toleranz_crimpbreite_isolation();
			zeile[REPORT_PRUEFKOMBINATION_TOLERANZ_CRIMPHOEHE_DRAHT] = pk.getN_toleranz_crimphoehe_draht();
			zeile[REPORT_PRUEFKOMBINATION_TOLERANZ_CRIMPHOEHE_ISOLATION] = pk.getN_toleranz_crimphoehe_isolation();

			zeile[REPORT_PRUEFKOMBINATION_TOLERANZ_WERT] = pk.getN_toleranz_wert();

			zeile[REPORT_PRUEFKOMBINATION_WERT] = pk.getN_wert();

			zeile[REPORT_PRUEFKOMBINATION_PRUEFART] = pk.getFlrpruefart().getC_nr();

			Iterator itSpr = pk.getFlrpruefart().getPruefartspr_set().iterator();
			while (itSpr.hasNext()) {
				FLRPruefartspr flrPruefartspr = (FLRPruefartspr) itSpr.next();
				if (flrPruefartspr.getLocale().getC_nr().equals(theClientDto.getLocUiAsString())) {
					zeile[REPORT_PRUEFKOMBINATION_PRUEFART_BEZEICHNUNG] = flrPruefartspr.getC_bez();
					break;
				}
			}

			PruefkombinationDto pkDto = getStuecklisteFac().pruefkombinationFindByPrimaryKey(pk.getI_id(),
					theClientDto);
			if (pkDto.getPruefkombinationsprDto() != null) {
				zeile[REPORT_PRUEFKOMBINATION_KOMMENTAR] = pkDto.getPruefkombinationsprDto().getCBez();
			}

			zeile[REPORT_PRUEFKOMBINATION_STANDARD] = Helper.short2Boolean(pk.getB_standard());

			zeile[REPORT_PRUEFKOMBINATION_DOPPELANSCHLAG] = Helper.short2Boolean(pk.getB_doppelanschlag());

			zeile[REPORT_PRUEFKOMBINATION_ABZUGSKRAFT1] = pk.getN_abzugskraft_litze();
			zeile[REPORT_PRUEFKOMBINATION_ABZUGSKRAFT2] = pk.getN_abzugskraft_litze2();

			zeile[REPORT_PRUEFKOMBINATION_STANDARD] = Helper.short2Boolean(pk.getB_standard());

			alDaten.add(zeile);
		}
		data = new Object[alDaten.size()][REPORT_PRUEFKOMBINATION_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(parameter, StuecklisteReportFac.REPORT_MODUL, StuecklisteReportFac.REPORT_STUECKLISTE_PRUEFKOMBINATION,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printPruefplan(Integer stuecklisteIId, TheClientDto theClientDto) {

		sAktuellerReport = StuecklisteReportFac.REPORT_STUECKLISTE_PRUEFPLAN;

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT pp FROM FLRStklpruefplan AS pp LEFT OUTER JOIN pp.flrstuecklisteposition_kontakt.flrartikel AS flrartikel WHERE 1=1 ";

		HashMap<Integer, Object[]> hmStklVorlage = new HashMap();

		if (stuecklisteIId != null) {

			sQuery += " AND pp.stueckliste_i_id=" + stuecklisteIId;
		}

		sQuery += " ORDER BY pp.flrstueckliste.flrartikel.c_nr ASC,  pp.i_sort ASC ";

		org.hibernate.Query hquery = session.createQuery(sQuery);

		List<?> resultList = hquery.list();
		Iterator<?> resultListIterator = resultList.iterator();

		ArrayList alDaten = new ArrayList();

		while (resultListIterator.hasNext()) {
			FLRStklpruefplan pp = (FLRStklpruefplan) resultListIterator.next();

			if (!hmStklVorlage.containsKey(pp.getStueckliste_i_id())) {
				Object[] oZeileVorlage = new Object[REPORT_PRUEFPLAN_ANZAHL_SPALTEN];

				StuecklisteDto dto = getStuecklisteFac().stuecklisteFindByPrimaryKey(pp.getStueckliste_i_id(),
						theClientDto);

				oZeileVorlage[REPORT_PRUEFPLAN_STUECKLISTE_KUNDE] = HelperServer
						.formatNameAusFLRPartner(pp.getFlrstueckliste().getFlrpartner());

				oZeileVorlage[REPORT_PRUEFPLAN_STUECKLISTE_NUMMER] = dto.getArtikelDto().getCNr();
				oZeileVorlage[REPORT_PRUEFPLAN_STUECKLISTE_ERFASSUNGSFAKTOR] = dto.getNErfassungsfaktor();
				oZeileVorlage[REPORT_PRUEFPLAN_STUECKLISTE_ANLEGEN] = dto.getTAnlegen();
				oZeileVorlage[REPORT_PRUEFPLAN_STUECKLISTE_AENDERN] = dto.getTAendern();
				oZeileVorlage[REPORT_PRUEFPLAN_STUECKLISTE_AENDERN_POSITION] = dto.getTAendernposition();
				oZeileVorlage[REPORT_PRUEFPLAN_STUECKLISTE_AENDERN_ARBEITSPLAN] = dto.getTAendernarbeitsplan();

				oZeileVorlage[REPORT_PRUEFPLAN_STUECKLISTE_FREIGABE_ZEITPUNKT] = dto.getTFreigabe();
				if (pp.getFlrstueckliste().getFlrpersonal_freigabe() != null) {

					oZeileVorlage[REPORT_PRUEFPLAN_STUECKLISTE_FREIGABE_PERSON] = pp.getFlrstueckliste()
							.getFlrpersonal_freigabe().getC_kurzzeichen();

				}

				oZeileVorlage[REPORT_PRUEFPLAN_STUECKLISTE_BEZEICHNUNG] = dto.getArtikelDto().formatBezeichnung();

				if (dto.getArtikelDto().getArtikelsprDto() != null) {

					oZeileVorlage[REPORT_PRUEFPLAN_STUECKLISTE_KURZBEZEICHNUNG] = dto.getArtikelDto().getArtikelsprDto()
							.getCKbez();
					oZeileVorlage[REPORT_PRUEFPLAN_STUECKLISTE_ZUSATZBEZEICHNUNG] = dto.getArtikelDto()
							.getArtikelsprDto().getCZbez();
					oZeileVorlage[REPORT_PRUEFPLAN_STUECKLISTE_ZUSATZBEZEICHNUNG2] = dto.getArtikelDto()
							.getArtikelsprDto().getCZbez2();
				}
				try {
					// Zeichnungsnummer
					StuecklisteeigenschaftDto[] stuecklisteeigenschaftDtos = getStuecklisteFac()
							.stuecklisteeigenschaftFindByStuecklisteIId(dto.getIId());
					ArrayList<Object[]> al = new ArrayList<Object[]>();
					for (int i = 0; i < stuecklisteeigenschaftDtos.length; i++) {
						StuecklisteeigenschaftDto stuecklisteeigenschaftDto = stuecklisteeigenschaftDtos[i];

						Object[] o = new Object[2];
						o[0] = stuecklisteeigenschaftDto.getStuecklisteeigenschaftartDto().getCBez();
						o[1] = stuecklisteeigenschaftDto.getCBez();
						al.add(o);
					}

					if (stuecklisteeigenschaftDtos.length > 0) {
						String[] fieldnames = new String[] { "F_EIGENSCHAFTART", "F_BEZEICHNUNG" };
						Object[][] dataSub = new Object[al.size()][fieldnames.length];
						dataSub = (Object[][]) al.toArray(dataSub);

						oZeileVorlage[REPORT_PRUEFPLAN_STUECKLISTE_SUBREPORT_ZEICHNUNGSNUMMMERN] = new LPDatenSubreport(
								dataSub, fieldnames);
					}

				} catch (RemoteException ex4) {
					throwEJBExceptionLPRespectOld(ex4);
				}

				oZeileVorlage[REPORT_PRUEFPLAN_STUECKLISTE_KOMMENTAR] = Helper
						.formatStyledTextForJasper(dto.getXKommentar());

				hmStklVorlage.put(pp.getStueckliste_i_id(), oZeileVorlage);
			}

			Object[] oZeile = hmStklVorlage.get(pp.getStueckliste_i_id()).clone();

			Integer artikelIIdKontakt = null;
			if (pp.getFlrstuecklisteposition_kontakt() != null) {
				artikelIIdKontakt = pp.getFlrstuecklisteposition_kontakt().getFlrartikel().getI_id();
				oZeile[REPORT_PRUEFPLAN_ARTIKEL_KONTAKT] = pp.getFlrstuecklisteposition_kontakt().getFlrartikel()
						.getC_nr();

				ArtikelDto aDtoKontakt = getArtikelFac().artikelFindByPrimaryKeySmall(
						pp.getFlrstuecklisteposition_kontakt().getFlrartikel().getI_id(), theClientDto);

				if (aDtoKontakt.getArtikelsprDto() != null) {
					oZeile[REPORT_PRUEFPLAN_ARTIKELBEZEICHNUNG_KONTAKT] = aDtoKontakt.getArtikelsprDto().getCBez();
				}
				oZeile[REPORT_PRUEFPLAN_POSITION_KONTAKT] = pp.getFlrstuecklisteposition_kontakt().getC_position();
			}

			Integer artikelIIdLitze = null;
			if (pp.getFlrstuecklisteposition_litze() != null) {
				artikelIIdLitze = pp.getFlrstuecklisteposition_litze().getFlrartikel().getI_id();
				oZeile[REPORT_PRUEFPLAN_ARTIKEL_LITZE] = pp.getFlrstuecklisteposition_litze().getFlrartikel().getC_nr();

				ArtikelDto aDtoLitze = getArtikelFac().artikelFindByPrimaryKeySmall(
						pp.getFlrstuecklisteposition_litze().getFlrartikel().getI_id(), theClientDto);

				if (aDtoLitze.getArtikelsprDto() != null) {
					oZeile[REPORT_PRUEFPLAN_ARTIKELBEZEICHNUNG_LITZE] = aDtoLitze.getArtikelsprDto().getCBez();
				}
				oZeile[REPORT_PRUEFPLAN_POSITION_LITZE] = pp.getFlrstuecklisteposition_litze().getC_position();

				oZeile[REPORT_PRUEFPLAN_MENGE_LITZE] = pp.getFlrstuecklisteposition_litze().getN_menge();
				oZeile[REPORT_PRUEFPLAN_EINHEIT_LITZE] = pp.getFlrstuecklisteposition_litze().getEinheit_c_nr();
				oZeile[REPORT_PRUEFPLAN_DIMENSION1_LITZE] = pp.getFlrstuecklisteposition_litze().getF_dimension1();
				oZeile[REPORT_PRUEFPLAN_DIMENSION2_LITZE] = pp.getFlrstuecklisteposition_litze().getF_dimension2();
				oZeile[REPORT_PRUEFPLAN_DIMENSION3_LITZE] = pp.getFlrstuecklisteposition_litze().getF_dimension3();

			}

			Integer artikelIIdLitze2 = null;
			if (pp.getFlrstuecklisteposition_litze2() != null) {
				artikelIIdLitze2 = pp.getFlrstuecklisteposition_litze2().getFlrartikel().getI_id();
				oZeile[REPORT_PRUEFPLAN_ARTIKEL_LITZE2] = pp.getFlrstuecklisteposition_litze2().getFlrartikel()
						.getC_nr();

				ArtikelDto aDtoLitze = getArtikelFac().artikelFindByPrimaryKeySmall(
						pp.getFlrstuecklisteposition_litze2().getFlrartikel().getI_id(), theClientDto);

				if (aDtoLitze.getArtikelsprDto() != null) {
					oZeile[REPORT_PRUEFPLAN_ARTIKELBEZEICHNUNG_LITZE2] = aDtoLitze.getArtikelsprDto().getCBez();
				}
				oZeile[REPORT_PRUEFPLAN_POSITION_LITZE2] = pp.getFlrstuecklisteposition_litze2().getC_position();

				oZeile[REPORT_PRUEFPLAN_MENGE_LITZE2] = pp.getFlrstuecklisteposition_litze2().getN_menge();
				oZeile[REPORT_PRUEFPLAN_EINHEIT_LITZE2] = pp.getFlrstuecklisteposition_litze2().getEinheit_c_nr();
				oZeile[REPORT_PRUEFPLAN_DIMENSION1_LITZE2] = pp.getFlrstuecklisteposition_litze2().getF_dimension1();
				oZeile[REPORT_PRUEFPLAN_DIMENSION2_LITZE2] = pp.getFlrstuecklisteposition_litze2().getF_dimension2();
				oZeile[REPORT_PRUEFPLAN_DIMENSION3_LITZE2] = pp.getFlrstuecklisteposition_litze2().getF_dimension3();

			}

			oZeile[REPORT_PRUEFPLAN_DOPPELANSCHLAG] = Helper.short2Boolean(pp.getB_doppelanschlag());

			if (pp.getFlrverschleissteil() != null) {
				oZeile[REPORT_PRUEFPLAN_VERSCHLEISSTEILNUMMER] = pp.getFlrverschleissteil().getC_nr();
				oZeile[REPORT_PRUEFPLAN_VERSCHLEISSTEILBEZEICHNUNG] = pp.getFlrverschleissteil().getC_bez();

				// Werkzeuge

				VerschleissteilwerkzeugDto[] vwDtos = getArtikelFac()
						.verschleissteilwerkzeugFindByVerschleissteilIId(pp.getVerschleissteil_i_id());
				String[] fieldnames = new String[] { "Werkzeug", "Bezeichnung", "WerkzeugMandant" };

				Object[][] dataSub = new Object[vwDtos.length][fieldnames.length];
				for (int i = 0; i < vwDtos.length; i++) {

					WerkzeugDto wDto = getArtikelFac().werkzeugFindByPrimaryKey(vwDtos[i].getWerkzeugIId());
					dataSub[i][0] = wDto.getCNr();
					dataSub[i][1] = wDto.getCBez();
					dataSub[i][2] = wDto.getMandantCNrStandort();

				}

				oZeile[REPORT_PRUEFPLAN_SUBREPORT_WERKZEUGE] = new LPDatenSubreport(dataSub, fieldnames);

			}
			PruefartDto paDto = getStuecklisteFac().pruefartFindByPrimaryKey(pp.getFlrpruefart().getI_id(),
					theClientDto);

			oZeile[REPORT_PRUEFPLAN_PRUEFART] = paDto.getCNr();

			oZeile[REPORT_PRUEFPLAN_PRUEFART_BEZEICHNUNG] = paDto.getBezeichnung();

			Integer pruefkombinationIId = getStuecklisteFac().pruefeObPruefplanInPruefkombinationVorhanden(
					stuecklisteIId, pp.getFlrpruefart().getI_id(), artikelIIdKontakt, artikelIIdLitze, artikelIIdLitze2,
					pp.getVerschleissteil_i_id(), pp.getPruefkombination_i_id(), false, theClientDto);
			if (pruefkombinationIId != null) {

				PruefkombinationDto pkDto = getStuecklisteFac().pruefkombinationFindByPrimaryKey(pruefkombinationIId,
						theClientDto);

				oZeile[REPORT_PRUEFPLAN_CRIMPBREITE_DRAHT] = pkDto.getNCrimpbreitDraht();
				oZeile[REPORT_PRUEFPLAN_CRIMPBREITE_ISOLATION] = pkDto.getNCrimpbreiteIsolation();
				oZeile[REPORT_PRUEFPLAN_CRIMPHOEHE_DRAHT] = pkDto.getNCrimphoeheDraht();
				oZeile[REPORT_PRUEFPLAN_CRIMPHOEHE_ISOLATION] = pkDto.getNCrimphoeheIsolation();

				oZeile[REPORT_PRUEFPLAN_TOLERANZ_CRIMPBREITE_DRAHT] = pkDto.getNToleranzCrimpbreitDraht();
				oZeile[REPORT_PRUEFPLAN_TOLERANZ_CRIMPBREITE_ISOLATION] = pkDto.getNToleranzCrimphoeheIsolation();
				oZeile[REPORT_PRUEFPLAN_TOLERANZ_CRIMPHOEHE_DRAHT] = pkDto.getNToleranzCrimphoeheDraht();
				oZeile[REPORT_PRUEFPLAN_TOLERANZ_CRIMPHOEHE_ISOLATION] = pkDto.getNToleranzCrimphoeheIsolation();

				oZeile[REPORT_PRUEFPLAN_TOLERANZ_WERT] = pkDto.getNToleranzWert();

				oZeile[REPORT_PRUEFPLAN_WERT] = pkDto.getNWert();

				oZeile[REPORT_PRUEFPLAN_ABZUGSKAFT_LITZE] = pkDto.getNAbzugskraftLitze();
				oZeile[REPORT_PRUEFPLAN_ABZUGSKAFT_LITZE2] = pkDto.getNAbzugskraftLitze2();

				if (pkDto.getPruefkombinationsprDto() != null) {
					oZeile[REPORT_PRUEFPLAN_KOMMENTAR] = pkDto.getPruefkombinationsprDto().getCBez();
				}

			}

			alDaten.add(oZeile);
		}

		data = new Object[alDaten.size()][REPORT_PRUEFPLAN_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(data);

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		if (stuecklisteIId == null) {
			parameter.put("P_ALLE_STUECKLISTEN", Boolean.TRUE);
		} else {
			parameter.put("P_ALLE_STUECKLISTEN", Boolean.FALSE);
		}

		initJRDS(parameter, StuecklisteReportFac.REPORT_MODUL, StuecklisteReportFac.REPORT_STUECKLISTE_PRUEFPLAN,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	private Object[] befuelleVergleichMitStuecklisteMitMaterialDaten(StuecklistepositionDto stklposDto,
			boolean position2, Object[] oZeile, TheClientDto theClientDto) {

		ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(stklposDto.getArtikelIId(), theClientDto);

		oZeile[position2 ? REPORT_VGL_STKL2_ARTIKELNUMMER : REPORT_VGL_ARTIKELNUMMER] = aDto.getCNr();

		if (aDto.getArtikelsprDto() != null) {

			oZeile[position2 ? REPORT_VGL_STKL2_BEZEICHNUNG : REPORT_VGL_BEZEICHNUNG] = aDto.getArtikelsprDto()
					.getCBez();

			oZeile[position2 ? REPORT_VGL_STKL2_ZUSATZBEZEICHNUNG : REPORT_VGL_ZUSATZBEZEICHNUNG] = aDto
					.getArtikelsprDto().getCZbez();

		}

		try {

			MontageartDto mDto = getStuecklisteFac().montageartFindByPrimaryKey(stklposDto.getMontageartIId(),
					theClientDto);

			oZeile[position2 ? REPORT_VGL_STKL2_MONTAGEART : REPORT_VGL_MONTAGEART] = mDto.getCBez();

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		oZeile[position2 ? REPORT_VGL_STKL2_MENGE : REPORT_VGL_MENGE] = stklposDto.getNMenge();
		oZeile[position2 ? REPORT_VGL_STKL2_EINHEIT : REPORT_VGL_EINHEIT] = stklposDto.getEinheitCNr();
		oZeile[position2 ? REPORT_VGL_STKL2_DIMENSION1 : REPORT_VGL_DIMENSION1] = stklposDto.getFDimension1();
		oZeile[position2 ? REPORT_VGL_STKL2_DIMENSION2 : REPORT_VGL_DIMENSION2] = stklposDto.getFDimension2();
		oZeile[position2 ? REPORT_VGL_STKL2_DIMENSION3 : REPORT_VGL_DIMENSION3] = stklposDto.getFDimension3();
		oZeile[position2 ? REPORT_VGL_STKL2_POSITION : REPORT_VGL_POSITION] = stklposDto.getCPosition();
		oZeile[position2 ? REPORT_VGL_STKL2_KOMMENTAR : REPORT_VGL_KOMMENTAR] = stklposDto.getXTextinhalt();
		oZeile[position2 ? REPORT_VGL_STKL2_LFDNUMMER : REPORT_VGL_LFDNUMMER] = stklposDto.getILfdnummer();
		oZeile[position2 ? REPORT_VGL_STKL2_MITDRUCKEN : REPORT_VGL_MITDRUCKEN] = Helper
				.short2boolean(stklposDto.getBMitdrucken());
		oZeile[position2 ? REPORT_VGL_STKL2_KALKPREIS : REPORT_VGL_KALKPREIS] = stklposDto.getNKalkpreis();
		oZeile[position2 ? REPORT_VGL_STKL2_BEGINNTERMINOFFSET : REPORT_VGL_BEGINNTERMINOFFSET] = stklposDto
				.getIBeginnterminoffset();
		oZeile[position2 ? REPORT_VGL_STKL2_RUESTMENGE : REPORT_VGL_RUESTMENGE] = Helper
				.short2boolean(stklposDto.getBRuestmenge());
		oZeile[position2 ? REPORT_VGL_STKL2_FORMEL : REPORT_VGL_FORMEL] = stklposDto.getXFormel();
		oZeile[position2 ? REPORT_VGL_STKL2_AENDERUNGSDATUM : REPORT_VGL_AENDERUNGSDATUM] = stklposDto.getTAendern();

		return oZeile;
	}

	private Object[] befuelleVergleichMitArbeitsplanDaten(StuecklistearbeitsplanDto apDto, boolean position2,
			Object[] oZeile, TheClientDto theClientDto) {

		ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(apDto.getArtikelIId(), theClientDto);

		oZeile[position2 ? REPORT_VGL_STKL2_ARTIKELNUMMER : REPORT_VGL_ARTIKELNUMMER] = aDto.getCNr();

		if (aDto.getArtikelsprDto() != null) {

			oZeile[position2 ? REPORT_VGL_STKL2_BEZEICHNUNG : REPORT_VGL_BEZEICHNUNG] = aDto.getArtikelsprDto()
					.getCBez();

			oZeile[position2 ? REPORT_VGL_STKL2_ZUSATZBEZEICHNUNG : REPORT_VGL_ZUSATZBEZEICHNUNG] = aDto
					.getArtikelsprDto().getCZbez();

		}

		if (apDto.getApkommentarIId() != null) {
			ApkommentarDto apkommDto = getStuecklisteFac().apkommentarFindByPrimaryKey(apDto.getApkommentarIId(),
					theClientDto);

			oZeile[position2 ? REPORT_VGL_ARBEITSPLAN_STKL2_APKOMMENTAR
					: REPORT_VGL_ARBEITSPLAN_APKOMMENTAR] = apkommDto.getBezeichnung();

		}

		oZeile[position2 ? REPORT_VGL_ARBEITSPLAN_STKL2_FORMEL : REPORT_VGL_ARBEITSPLAN_FORMEL] = apDto.getXFormel();

		oZeile[position2 ? REPORT_VGL_ARBEITSPLAN_STKL2_ARBEITSGANG : REPORT_VGL_ARBEITSPLAN_ARBEITSGANG] = apDto
				.getIArbeitsgang();
		oZeile[position2 ? REPORT_VGL_ARBEITSPLAN_STKL2_UNTERARBEITSGANG
				: REPORT_VGL_ARBEITSPLAN_UNTERARBEITSGANG] = apDto.getIUnterarbeitsgang();

//		new BigDecimal(apDto.getLStueckzeit()).divide(new BigDecimal(60 * 60 * 1000), BigDecimal.ROUND_HALF_EVEN, 4);

		oZeile[position2 ? REPORT_VGL_ARBEITSPLAN_STKL2_STUECKZEIT
				: REPORT_VGL_ARBEITSPLAN_STUECKZEIT] = new BigDecimal(apDto.getLStueckzeit())
						.divide(new BigDecimal(60 * 60 * 1000), BigDecimal.ROUND_HALF_EVEN, 4).doubleValue();
		oZeile[position2 ? REPORT_VGL_ARBEITSPLAN_STKL2_RUESTZEIT
				: REPORT_VGL_ARBEITSPLAN_RUESTZEIT] = new BigDecimal(apDto.getLRuestzeit())
						.divide(new BigDecimal(60 * 60 * 1000), BigDecimal.ROUND_HALF_EVEN, 4).doubleValue();
		oZeile[position2 ? REPORT_VGL_ARBEITSPLAN_STKL2_KOMMENTAR : REPORT_VGL_ARBEITSPLAN_KOMMENTAR] = apDto
				.getCKommentar();
		oZeile[position2 ? REPORT_VGL_ARBEITSPLAN_STKL2_LANGTEXT : REPORT_VGL_ARBEITSPLAN_LANGTEXT] = apDto
				.getXLangtext();
		if (apDto.getMaschineIId() != null) {
			oZeile[position2 ? REPORT_VGL_ARBEITSPLAN_STKL2_MASCHINE

					: REPORT_VGL_ARBEITSPLAN_MASCHINE] = getZeiterfassungFac()
							.maschineFindByPrimaryKey(apDto.getMaschineIId()).getBezeichnung();
		}
		oZeile[position2 ? REPORT_VGL_ARBEITSPLAN_STKL2_AGART : REPORT_VGL_ARBEITSPLAN_AGART] = apDto.getAgartCNr();
		oZeile[position2 ? REPORT_VGL_ARBEITSPLAN_STKL2_AUFSPANNUNG : REPORT_VGL_ARBEITSPLAN_AUFSPANNUNG] = apDto
				.getIAufspannung();
		oZeile[position2 ? REPORT_VGL_ARBEITSPLAN_STKL2_NURMASCHINENZEIT
				: REPORT_VGL_ARBEITSPLAN_NURMASCHINENZEIT] = Helper.short2Boolean(apDto.getBNurmaschinenzeit());
		oZeile[position2 ? REPORT_VGL_ARBEITSPLAN_STKL2_MASCHINENVERSATZTAGE
				: REPORT_VGL_ARBEITSPLAN_MASCHINENVERSATZTAGE] = apDto.getIMaschinenversatztage();
		oZeile[position2 ? REPORT_VGL_ARBEITSPLAN_STKL2_PPM : REPORT_VGL_ARBEITSPLAN_PPM] = apDto.getNPpm();

		StuecklisteDto stueckliste = getStuecklisteFac().stuecklisteFindByPrimaryKey(apDto.getStuecklisteIId(),
				theClientDto);
		oZeile[position2 ? REPORT_VGL_STKL2_AENDERUNGSDATUM : REPORT_VGL_AENDERUNGSDATUM] = stueckliste
				.getTAendernarbeitsplan();

		return oZeile;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printVergleichMitAndererStueckliste(Integer stuecklisteIId, Integer stuecklisteIId2,
			boolean bSortiertNachArtikelnummer, boolean bVerdichtetNachArtikelnummer, boolean bHerstellerunabhaengig,
			boolean bNurUnterschiede, TheClientDto theClientDto) {

		sAktuellerReport = StuecklisteReportFac.REPORT_STUECKLISTE_VERGLEICH_MIT_ANDERER_STUECKLISTE;

		try {

			StuecklisteDto stkDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(stuecklisteIId, theClientDto);

			StuecklisteDto stk2Dto = getStuecklisteFac().stuecklisteFindByPrimaryKey(stuecklisteIId2, theClientDto);

			StuecklistepositionDto[] stklPosDtos = getStuecklisteFac()
					.stuecklistepositionFindByStuecklisteIId(stuecklisteIId, theClientDto);

			StuecklistepositionDto[] stklPosDtos2 = getStuecklisteFac()
					.stuecklistepositionFindByStuecklisteIId(stuecklisteIId2, theClientDto);

			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_ARTIKELNUMMER);
			int iLaengeArtikelnummer = (Integer) parameter.getCWertAsObject();

			// Stuecklisteposition Dtos Artikel setzen
			for (StuecklistepositionDto posDto : stklPosDtos) {
				ArtikelDto artDto = getArtikelFac().artikelFindByPrimaryKeySmall(posDto.getArtikelIId(), theClientDto);
				posDto.setArtikelDto(artDto);
			}
			for (StuecklistepositionDto posDto : stklPosDtos2) {
				ArtikelDto artDto = getArtikelFac().artikelFindByPrimaryKeySmall(posDto.getArtikelIId(), theClientDto);
				posDto.setArtikelDto(artDto);
			}

			HvTreeMultimap<String, StuecklistepositionDto> stklPosDtoMap1 = stklPosZuMapfuerVergleich(
					bSortiertNachArtikelnummer, stklPosDtos);
			HvTreeMultimap<String, StuecklistepositionDto> stklPosDtoMap2 = stklPosZuMapfuerVergleich(
					bSortiertNachArtikelnummer, stklPosDtos2);

			if (bVerdichtetNachArtikelnummer) {
				stklPosDtoMap1 = verdichteNachArtikelnummer(stklPosDtoMap1, bHerstellerunabhaengig,
						iLaengeArtikelnummer);
				stklPosDtoMap2 = verdichteNachArtikelnummer(stklPosDtoMap2, bHerstellerunabhaengig,
						iLaengeArtikelnummer);
			}

			// Handartikel nicht nach Artikelnummer, sondern nach "~Bez" mappen, damit sind
			// die ganz hinten und gleichwertig wenn gleich benannt.
			BiFunction<String, StuecklistepositionDto, String> handartikelToBez = (k, v) -> {
				String artikelart = v.getArtikelDto().getArtikelartCNr();
				return artikelart.equals(ArtikelFac.ARTIKELART_HANDARTIKEL) ? "~" + v.getArtikelDto().getCBezAusSpr()
						: k;
			};
			stklPosDtoMap1 = stklPosDtoMap1.remapKey(handartikelToBez);
			stklPosDtoMap2 = stklPosDtoMap2.remapKey(handartikelToBez);

			HvTreeMultimap<String, ImmutablePair<StuecklistepositionDto>> pairMap = stklPosDtoMap1.merge(stklPosDtoMap2,
					HvTreeMultimap.mergeFunctionPairUp());

			ArrayList<Object[]> alDaten = new ArrayList<Object[]>();
			for (ImmutablePair<StuecklistepositionDto> pair : pairMap.values()) {

				Object[] oZeile = new Object[REPORT_VGL_SPALTENANZAHL];

				oZeile[REPORT_VGL_POSITIONEN_UNTERSCHIEDLICH] = Boolean.TRUE;
				oZeile[REPORT_VGL_ARBEITSPLAN] = Boolean.FALSE;

				// PJ21754 Handartikel anders vergleichen
				if (istHandartikel(pair)) {
					ArtikelDto art1 = pair.getFirst().getArtikelDto();
					ArtikelDto art2 = pair.getSecond().getArtikelDto();
					boolean bezGleich = art1.getCBezAusSpr().equals(art2.getCBezAusSpr());
					oZeile[REPORT_VGL_POSITIONEN_UNTERSCHIEDLICH] = Boolean.valueOf(!bezGleich);
				} else if (pair.getFirst() != null && pair.getSecond() != null
						&& pair.getFirst().istGleich(pair.getSecond())) {
					oZeile[REPORT_VGL_POSITIONEN_UNTERSCHIEDLICH] = Boolean.FALSE;
				}

				if (pair.getFirst() != null) {
					oZeile = befuelleVergleichMitStuecklisteMitMaterialDaten(pair.getFirst(), false, oZeile,
							theClientDto);
				}
				if (pair.getSecond() != null) {
					oZeile = befuelleVergleichMitStuecklisteMitMaterialDaten(pair.getSecond(), true, oZeile,
							theClientDto);
				}

				boolean nichtAnzeigen = bNurUnterschiede && !(Boolean) oZeile[REPORT_VGL_POSITIONEN_UNTERSCHIEDLICH];
				if (!nichtAnzeigen) {
					alDaten.add(oZeile);
				}

			}

			StuecklistearbeitsplanDto[] apDtos = getStuecklisteFac()
					.stuecklistearbeitsplanFindByStuecklisteIId(stuecklisteIId, theClientDto);

			StuecklistearbeitsplanDto[] apDtos2 = getStuecklisteFac()
					.stuecklistearbeitsplanFindByStuecklisteIId(stuecklisteIId2, theClientDto);

			int iZeilenAP = apDtos.length;
			if (apDtos2.length > apDtos.length) {
				iZeilenAP = apDtos2.length;
			}

			for (int i = 0; i < iZeilenAP; i++) {

				StuecklistearbeitsplanDto[] posDtos = new StuecklistearbeitsplanDto[2];

				if (apDtos.length > i) {
					posDtos[0] = apDtos[i];
				}

				if (apDtos2.length > i) {
					posDtos[1] = apDtos2[i];
				}

				Object[] oZeile = new Object[REPORT_VGL_SPALTENANZAHL];

				oZeile[REPORT_VGL_POSITIONEN_UNTERSCHIEDLICH] = Boolean.TRUE;
				oZeile[REPORT_VGL_ARBEITSPLAN] = Boolean.TRUE;

				if (posDtos[0] != null && posDtos[1] != null && posDtos[0].istGleich(posDtos[1])) {
					oZeile[REPORT_VGL_POSITIONEN_UNTERSCHIEDLICH] = Boolean.FALSE;
				}

				if (posDtos[0] != null) {
					oZeile = befuelleVergleichMitArbeitsplanDaten(posDtos[0], false, oZeile, theClientDto);
				}
				if (posDtos[1] != null) {
					oZeile = befuelleVergleichMitArbeitsplanDaten(posDtos[1], true, oZeile, theClientDto);
				}

				boolean nichtAnzeigen = bNurUnterschiede && !(Boolean) oZeile[REPORT_VGL_POSITIONEN_UNTERSCHIEDLICH];
				if (!nichtAnzeigen) {
					alDaten.add(oZeile);
				}

			}

			data = new Object[alDaten.size()][REPORT_VERSCHLEISSTEILVERWENDUNG_ANZAHL_SPALTEN];
			data = (Object[][]) alDaten.toArray(data);

			Map<String, Object> mapParameter = new TreeMap<String, Object>();

			// ----1
			mapParameter.put("P_STUECKLISTENUMMER", stkDto.getArtikelDto().getCNr());
			mapParameter.put("P_STUECKLISTEBEZEICHNUNG", stkDto.getArtikelDto().getArtikelsprDto().getCBez());

			mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG", stkDto.getArtikelDto().getArtikelsprDto().getCZbez());
			mapParameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG2", stkDto.getArtikelDto().getArtikelsprDto().getCZbez2());

			mapParameter.put("P_STUECKLISTEKURZBEZEICHNUNG", stkDto.getArtikelDto().getArtikelsprDto().getCKbez());

			mapParameter.put("P_STUECKLISTEKURZBEZEICHNUNG", stkDto.getArtikelDto().getArtikelsprDto().getCKbez());
			mapParameter.put("P_STUECKLISTEKURZBEZEICHNUNG", stkDto.getArtikelDto().getArtikelsprDto().getCKbez());
			mapParameter.put("P_STUECKLISTEKURZBEZEICHNUNG", stkDto.getArtikelDto().getArtikelsprDto().getCKbez());
			mapParameter.put("P_STUECKLISTEKURZBEZEICHNUNG", stkDto.getArtikelDto().getArtikelsprDto().getCKbez());

			mapParameter.put("P_INDEX", stkDto.getArtikelDto().getCIndex());
			mapParameter.put("P_REVISION", stkDto.getArtikelDto().getCRevision());
			mapParameter.put("P_MENGENEINHEIT", stkDto.getArtikelDto().getEinheitCNr());
			mapParameter.put("P_ANGELEGT", stkDto.getTAnlegen());

			mapParameter.put("P_GEAENDERT", stkDto.getTAendern());

			mapParameter.put("P_ERFASSUNGSFAKTOR", stkDto.getNErfassungsfaktor());

			// ----2

			mapParameter.put("P_STUECKLISTE2_NUMMER", stk2Dto.getArtikelDto().getCNr());

			mapParameter.put("P_STUECKLISTE2_BEZEICHNUNG", stk2Dto.getArtikelDto().getArtikelsprDto().getCBez());
			mapParameter.put("P_STUECKLISTE2_ZUSATZBEZEICHNUNG", stk2Dto.getArtikelDto().getArtikelsprDto().getCZbez());
			mapParameter.put("P_STUECKLISTE2_ZUSATZBEZEICHNUNG2",
					stk2Dto.getArtikelDto().getArtikelsprDto().getCZbez2());
			mapParameter.put("P_STUECKLISTE2_KURZBEZEICHNUNG", stk2Dto.getArtikelDto().getArtikelsprDto().getCKbez());

			mapParameter.put("P_STUECKLISTE2_INDEX", stk2Dto.getArtikelDto().getCIndex());
			mapParameter.put("P_STUECKLISTE2_REVISION", stk2Dto.getArtikelDto().getCRevision());
			mapParameter.put("P_STUECKLISTE2_MENGENEINHEIT", stk2Dto.getArtikelDto().getEinheitCNr());
			mapParameter.put("P_STUECKLISTE2_ANGELEGT", stk2Dto.getTAnlegen());

			mapParameter.put("P_STUECKLISTE2_GEAENDERT", stk2Dto.getTAendern());

			mapParameter.put("P_STUECKLISTE2_ERFASSUNGSFAKTOR", stk2Dto.getNErfassungsfaktor());

			mapParameter.put("P_SORTIERT_NACH_ARTIKELNUMMER", bSortiertNachArtikelnummer);
			mapParameter.put("P_VERDICHTET_NACH_ARTIKEL", bVerdichtetNachArtikelnummer);
			mapParameter.put("P_VERDICHTET_HERSTELLERUNABHAENGIG", bHerstellerunabhaengig);
			mapParameter.put("P_NUR_UNTERSCHIEDE", bNurUnterschiede);

			initJRDS(mapParameter, REPORT_MODUL, REPORT_STUECKLISTE_VERGLEICH_MIT_ANDERER_STUECKLISTE,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		} catch (Exception t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, t);

		}

		return getReportPrint();
	}

	private HvTreeMultimap<String, StuecklistepositionDto> verdichteNachArtikelnummer(
			HvTreeMultimap<String, StuecklistepositionDto> stklPosDtoMap1, boolean bHerstellerunabhaengig,
			int iLaengeArtikelnummer) {
		BinaryOperator<StuecklistepositionDto> stklVerdichtung = (stkl1, stkl2) -> {
			stkl1.setNMenge(stkl1.getNMenge().add(stkl2.getNMenge()));
			return stkl1;
		};
		// zuerst durch ruestmenge unterscheiden
		BiFunction<? super String, ? super StuecklistepositionDto, ComparableImmutablePair<String>> verdichtenKeyMapper = (
				k, v) -> new ComparableImmutablePair<String>(
						getArtNrFuerVerdichten(v.getArtikelDto(), bHerstellerunabhaengig, iLaengeArtikelnummer),
						v.getBRuestmenge().toString());
		HvTreeMultimap<ComparableImmutablePair<String>, StuecklistepositionDto> zwischenMap1 = stklPosDtoMap1
				.remapKey(verdichtenKeyMapper);
		// dann verdichten
		zwischenMap1.reduceMap(stklVerdichtung);
		// dann key wiederherstellen
		stklPosDtoMap1 = zwischenMap1.remapKey((k, v) -> k.getFirst());
		return stklPosDtoMap1;
	}

	private String getArtNrFuerVerdichten(ArtikelDto dto, boolean bHerstellerunabhaengig, int laengeArtikelnummer) {
		String artCNr = dto.getCNr();
		if (bHerstellerunabhaengig) {
			if (artCNr.length() > laengeArtikelnummer) {
				artCNr = artCNr.substring(0, laengeArtikelnummer);
			}
		}
		return artCNr;
	}

	private HvTreeMultimap<String, StuecklistepositionDto> stklPosZuMapfuerVergleich(boolean bSortiertNachArtikelnummer,
			StuecklistepositionDto[] stklPosDtos) {
		HvTreeMultimap<String, StuecklistepositionDto> map = HvTreeMultimap.create();
		for (int i = 0; i < stklPosDtos.length; i++) {
			StuecklistepositionDto posDto = stklPosDtos[i];
			String key;
			if (bSortiertNachArtikelnummer) {
				String artCNr = posDto.getArtikelDto().getCNr();
				key = artCNr;
			} else {
				key = String.valueOf(i);
			}
			map.put(key, posDto);
		}
		return map;
	}

	private boolean istHandartikel(ImmutablePair<StuecklistepositionDto> posDtos) {
		if (posDtos.getFirst() == null || posDtos.getSecond() == null) {
			return false;
		}
		String art1CNR = posDtos.getFirst().getArtikelDto().getArtikelartCNr();
		String art2CNR = posDtos.getSecond().getArtikelDto().getArtikelartCNr();
		if (art1CNR == null || art2CNR == null)
			return false;
		return art1CNR.equals(ArtikelFac.ARTIKELART_HANDARTIKEL) && art2CNR.equals(ArtikelFac.ARTIKELART_HANDARTIKEL);
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printVerschleissteilverwendung(Integer verschleissteilIId, TheClientDto theClientDto) {

		sAktuellerReport = StuecklisteReportFac.REPORT_STUECKLISTE_VERSCHLEISSTEILVERWENDUNG;

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT distinct pp.flrstueckliste.i_id,pp.flrstueckliste.flrartikel.c_nr,pp.flrverschleissteil.i_id,pp.flrverschleissteil.c_nr, pp.flrverschleissteil.c_bez FROM FLRStklpruefplan AS pp  WHERE 1=1";

		if (verschleissteilIId != null) {
			sQuery += "  AND pp.verschleissteil_i_id=" + verschleissteilIId;
			VerschleissteilDto dto = getArtikelFac().verschleissteilFindByPrimaryKey(verschleissteilIId);
			parameter.put("P_VERSCHLEISSTEIL", dto.getBezeichnung());

		}

		sQuery += "  ORDER BY pp.flrverschleissteil.c_nr, pp.flrstueckliste.flrartikel.c_nr ASC ";

		org.hibernate.Query hquery = session.createQuery(sQuery);

		List<?> resultList = hquery.list();
		Iterator<?> resultListIterator = resultList.iterator();

		ArrayList alDaten = new ArrayList();

		while (resultListIterator.hasNext()) {

			Object[] o = (Object[]) resultListIterator.next();

			Integer stuecklisteIId = (Integer) o[0];

			String werkzeugnummer = (String) o[3];
			String werkzeugBez = (String) o[4];

			Object[] oZeile = new Object[REPORT_VERSCHLEISSTEILVERWENDUNG_ANZAHL_SPALTEN];
			oZeile[REPORT_VERSCHLEISSTEILVERWENDUNG_VERSCHLEISSTEIL] = werkzeugnummer;
			oZeile[REPORT_VERSCHLEISSTEILVERWENDUNG_VERSCHLEISSTEIL_BEZEICHNUNG] = werkzeugBez;

			StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(stuecklisteIId, theClientDto);

			oZeile[REPORT_VERSCHLEISSTEILVERWENDUNG_FREIGABEZEITPUNKT] = stklDto.getTFreigabe();

			if (stklDto.getPersonalIIdFreigabe() != null) {
				oZeile[REPORT_VERSCHLEISSTEILVERWENDUNG_PERSON_FREIGABE] = getPersonalFac()
						.personalFindByPrimaryKey(stklDto.getPersonalIIdFreigabe(), theClientDto).formatFixName1Name2();
			}

			oZeile[REPORT_VERSCHLEISSTEILVERWENDUNG_STUECKLISTE] = stklDto.getArtikelDto().getCNr();
			if (stklDto.getArtikelDto().getArtikelsprDto() != null) {
				oZeile[REPORT_VERSCHLEISSTEILVERWENDUNG_STUECKLISTE_BEZEICHNUNG] = stklDto.getArtikelDto()
						.getArtikelsprDto().getCBez();
			}

			try {
				oZeile[REPORT_VERSCHLEISSTEILVERWENDUNG_IN_FERTIGUNG] = getFertigungFac()
						.getAnzahlInFertigung(stklDto.getArtikelIId(), theClientDto);
				oZeile[REPORT_VERSCHLEISSTEILVERWENDUNG_FEHLMENGEN] = getFehlmengeFac()
						.getAnzahlFehlmengeEinesArtikels(stklDto.getArtikelIId(), theClientDto);
				oZeile[REPORT_VERSCHLEISSTEILVERWENDUNG_RESERVIERT] = getReservierungFac()
						.getAnzahlReservierungen(stklDto.getArtikelIId(), theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			alDaten.add(oZeile);
		}

		data = new Object[alDaten.size()][REPORT_PRUEFPLAN_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(parameter, StuecklisteReportFac.REPORT_MODUL,
				StuecklisteReportFac.REPORT_STUECKLISTE_VERSCHLEISSTEILVERWENDUNG, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printReichweite(Integer stuecklisteIId, DatumsfilterVonBis vonBis, boolean bVerdichtet,
			TheClientDto theClientDto) {

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_VERDICHTET", new Boolean(bVerdichtet));
		parameter.put("P_VON", vonBis.getTimestampVon());
		parameter.put("P_BIS", vonBis.getTimestampBisUnveraendert());

		index = -1;
		sAktuellerReport = StuecklisteReportFac.REPORT_STUECKLISTE_REICHWEITE;

		int iEinheit = 1;
		try {
			ParametermandantDto parameterDto = (ParametermandantDto) getParameterFac().parametermandantFindByPrimaryKey(
					ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT, ParameterFac.KATEGORIE_ARTIKEL,
					theClientDto.getMandant());

			if (parameterDto.getCWert() != null) {
				if (parameterDto.getCWert().equals(ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_KW)) {
					iEinheit = 7;
				}

			}
			parameter.put("P_EINHEIT_WIEDERBESCHAFFUNGSMORAL", parameterDto.getCWert());
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		ArrayList alDaten = new ArrayList();

		Session session = FLRSessionFactory.getFactory().openSession();
		String queryString = null;
		if (stuecklisteIId == null) {
			queryString = "SELECT STKL.I_ID FROM WW_ARTIKEL A RIGHT OUTER JOIN STK_STUECKLISTE STKL ON A.I_ID = STKL.ARTIKEL_I_ID WHERE (SELECT count(*) FROM STK_STUECKLISTEPOSITION POS WHERE POS.ARTIKEL_I_ID=A.I_ID)=0 AND STKL.STUECKLISTEART_C_NR <>'"
					+ StuecklisteFac.STUECKLISTEART_SETARTIKEL + "'";

		} else {
			queryString = "SELECT I_ID FROM STK_STUECKLISTE WHERE I_ID=" + stuecklisteIId;
			StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(stuecklisteIId, theClientDto);
			parameter.put("P_STUECKLISTE", stklDto.getArtikelDto().getCNr());
			if (stklDto.getArtikelDto().getArtikelsprDto() != null) {
				parameter.put("P_STUECKLISTE_BEZEICHNUNG", stklDto.getArtikelDto().getArtikelsprDto().getCBez());
				parameter.put("P_STUECKLISTE_ZUSATZBEZEICHNUNG", stklDto.getArtikelDto().getArtikelsprDto().getCZbez());
			}
		}

		HashMap<Integer, Integer> hmStuecklistenWBZ = new HashMap<Integer, Integer>();

		org.hibernate.Query query = session.createSQLQuery(queryString);

		List<?> resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {
			Integer stklIId = (Integer) resultListIterator.next();
			StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(stklIId, theClientDto);

			HashMap<Integer, Integer> hmHoechsteWBZMoralEinerEbene = new HashMap<Integer, Integer>();

			Object[] oZeileKopf = new Object[REPORT_REICHWEITE_ANZAHL_SPALTEN];

			oZeileKopf[REPORT_REICHWEITE_ARTIKELNUMMER] = stklDto.getArtikelDto().getCNr();
			if (stklDto.getArtikelDto().getArtikelsprDto() != null) {
				oZeileKopf[REPORT_REICHWEITE_BEZEICHNUNG] = stklDto.getArtikelDto().getArtikelsprDto().getCBez();
			}
			oZeileKopf[REPORT_REICHWEITE_STUECKLISTENART] = stklDto.getStuecklisteartCNr();

			if (stklDto.getNDefaultdurchlaufzeit() != null) {
				oZeileKopf[REPORT_REICHWEITE_STUECKLISTE_DURCHLAUFZEIT] = (int) stklDto.getNDefaultdurchlaufzeit()
						.doubleValue() / iEinheit;
			}

			oZeileKopf[REPORT_REICHWEITE_JAHRESMENGE] = getLagerFac().getVerbrauchteMengeEinesArtikels(
					stklDto.getArtikelDto().getIId(), vonBis.getTimestampVon(), vonBis.getTimestampBisUnveraendert(),
					theClientDto);
			try {
				oZeileKopf[REPORT_REICHWEITE_LAGERSTAND] = getLagerFac()
						.getLagerstandAllerLagerEinesMandanten(stklDto.getArtikelDto().getIId(), theClientDto);

				oZeileKopf[REPORT_REICHWEITE_LAGERSOLLSTAND] = stklDto.getArtikelDto().getFLagersoll();
				oZeileKopf[REPORT_REICHWEITE_LAGERMINDESTSTAND] = stklDto.getArtikelDto().getFLagermindest();

				alDaten.add(oZeileKopf);

				ArrayList<?> stuecklisteAufegloest = getStuecklisteFac().getStrukturDatenEinerStueckliste(stklIId,
						theClientDto, StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_ARTIKELNR, 0, null,
						true, false, BigDecimal.ONE, null, true);

				Integer iDurchlaufzeitLetzteEbene = 0;
				Integer iHoechsteWBZMoralEbene = 0;

				for (int j = 0; j < stuecklisteAufegloest.size(); j++) {
					StuecklisteMitStrukturDto strukt = (StuecklisteMitStrukturDto) stuecklisteAufegloest.get(j);

					if (strukt.getStuecklistepositionDto() != null) {
						if (strukt.getStuecklistepositionDto().getArtikelIId() != null
								&& strukt.getStuecklistepositionDto().getNZielmenge() != null) {

							BigDecimal positionsmenge = strukt.getStuecklistepositionDto().getNZielmenge();

							String einrueckung = "  ";
							for (int i = 0; i < strukt.getIEbene(); i++) {
								einrueckung = einrueckung + "   ";
							}

							Object[] oZeile = new Object[REPORT_REICHWEITE_ANZAHL_SPALTEN];

							oZeile[REPORT_REICHWEITE_EBENE] = strukt.getIEbene();

							com.lp.server.artikel.service.ArtikelDto artikelDto = getArtikelFac()
									.artikelFindByPrimaryKey(strukt.getStuecklistepositionDto().getArtikelIId(),
											theClientDto);

							oZeile[REPORT_REICHWEITE_ARTIKELNUMMER] = einrueckung + artikelDto.getCNr();
							if (artikelDto.getArtikelsprDto() != null) {
								oZeile[REPORT_REICHWEITE_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
							}

							// PJ20022

							if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet())) {
								oZeile[REPORT_REICHWEITE_LAGERSTAND] = getLagerFac()
										.getLagerstandAllerLagerEinesMandanten(artikelDto.getIId(), theClientDto);
							}

							oZeile[REPORT_REICHWEITE_BESTELLT] = getArtikelbestelltFac()
									.getAnzahlBestellt(artikelDto.getIId());

							oZeile[REPORT_REICHWEITE_NICHT_LAGERBEWIRTSCHAFTET] = !Helper
									.short2boolean(artikelDto.getBLagerbewirtschaftet());

							Hashtable htAnzahlRahmenbestellt = getArtikelbestelltFac()
									.getAnzahlRahmenbestellt(artikelDto.getIId(), theClientDto);
							if (htAnzahlRahmenbestellt.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
								oZeile[REPORT_REICHWEITE_RAHMENBESTELLT] = (BigDecimal) htAnzahlRahmenbestellt
										.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
							}

							oZeile[REPORT_REICHWEITE_LAGERSOLLSTAND] = artikelDto.getFLagersoll();
							oZeile[REPORT_REICHWEITE_LAGERMINDESTSTAND] = artikelDto.getFLagermindest();

							if (strukt.isBStueckliste()) {
								iDurchlaufzeitLetzteEbene = 0;

								oZeile[REPORT_REICHWEITE_JAHRESMENGE] = getLagerFac().getVerbrauchteMengeEinesArtikels(
										artikelDto.getIId(), vonBis.getTimestampVon(),
										vonBis.getTimestampBisUnveraendert(), theClientDto);

								if (strukt.getStuecklisteDto() != null) {
									oZeile[REPORT_REICHWEITE_STUECKLISTENART] = strukt.getStuecklisteDto()
											.getStuecklisteartCNr();

									if (strukt.getStuecklisteDto().getNDefaultdurchlaufzeit() != null) {

										iDurchlaufzeitLetzteEbene = (int) strukt.getStuecklisteDto()
												.getNDefaultdurchlaufzeit().doubleValue() / iEinheit;
									}

									oZeile[REPORT_REICHWEITE_STUECKLISTE_DURCHLAUFZEIT] = iDurchlaufzeitLetzteEbene;
									oZeile[REPORT_REICHWEITE_STUECKLISTE_I_ID] = strukt.getStuecklisteDto().getIId();
								}

							} else {

								Integer wbMoral = 0;

								if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet())) {

									Integer iMoral = getBestellungFac()
											.getWiederbeschaffungsmoralEinesArtikels(artikelDto.getIId(), theClientDto);
									if (iMoral != null) {
										wbMoral = iMoral / iEinheit;
									}

								}

								if (strukt.getStuecklistepositionDto().getStuecklisteIId() != null) {

									if (hmStuecklistenWBZ
											.containsKey(strukt.getStuecklistepositionDto().getStuecklisteIId())) {
										Integer wbvorhanden = hmStuecklistenWBZ
												.get(strukt.getStuecklistepositionDto().getStuecklisteIId());

										if (wbvorhanden < wbMoral) {
											hmStuecklistenWBZ.put(
													strukt.getStuecklistepositionDto().getStuecklisteIId(), wbMoral);
										}
									} else {
										hmStuecklistenWBZ.put(strukt.getStuecklistepositionDto().getStuecklisteIId(),
												wbMoral);
									}

								}

								oZeile[REPORT_REICHWEITE_WIEDERBESCHAFFUNGSMORAL] = wbMoral;

								if (wbMoral > iHoechsteWBZMoralEbene) {
									iHoechsteWBZMoralEbene = wbMoral;
								}

								if (hmHoechsteWBZMoralEinerEbene.containsKey(strukt.getIEbene())) {
									Integer value = (Integer) hmHoechsteWBZMoralEinerEbene.get(strukt.getIEbene());
									if (value.doubleValue() < wbMoral + iDurchlaufzeitLetzteEbene) {
										hmHoechsteWBZMoralEinerEbene.put(strukt.getIEbene(),
												wbMoral + iDurchlaufzeitLetzteEbene);
									}
								} else {
									hmHoechsteWBZMoralEinerEbene.put(strukt.getIEbene(),
											wbMoral + iDurchlaufzeitLetzteEbene);
								}

								ArtikellieferantDto alDto = getArtikelFac()
										.getArtikelEinkaufspreisDesBevorzugtenLieferanten(artikelDto.getIId(),
												BigDecimal.ONE, theClientDto.getSMandantenwaehrung(), theClientDto);

								if (alDto != null) {

									oZeile[REPORT_REICHWEITE_WIEDERBESCHAFFUNGSZEIT] = alDto
											.getIWiederbeschaffungszeit();

									if (alDto.getLieferantDto() != null) {
										oZeile[REPORT_REICHWEITE_ARTIKELLIEFERANT] = alDto.getLieferantDto()
												.getPartnerDto().formatAnrede();
									}
								}

							}

							alDaten.add(oZeile);

						}

					}

				}

				Integer wbmoral = new Integer(0);
				if (stklDto.getNDefaultdurchlaufzeit() != null) {
					wbmoral = stklDto.getNDefaultdurchlaufzeit().intValue() / iEinheit;
				}
				Iterator<Integer> i = hmHoechsteWBZMoralEinerEbene.keySet().iterator();
				while (i.hasNext()) {
					Integer key = (Integer) i.next();
					Integer wert = (Integer) hmHoechsteWBZMoralEinerEbene.get(key);
					wbmoral += wert;
				}

				oZeileKopf[REPORT_REICHWEITE_WIEDERBESCHAFFUNGSMORAL] = wbmoral;

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}

		for (int i = 0; i < alDaten.size(); i++) {

			Object[] oZeile = (Object[]) alDaten.get(i);

			if (oZeile[REPORT_REICHWEITE_STUECKLISTE_I_ID] != null) {

				Integer dlz = 0;

				if (oZeile[REPORT_REICHWEITE_STUECKLISTE_DURCHLAUFZEIT] != null) {
					dlz = (Integer) oZeile[REPORT_REICHWEITE_STUECKLISTE_DURCHLAUFZEIT];
				}

				Integer wbmoral = 0;

				if (hmStuecklistenWBZ.containsKey(oZeile[REPORT_REICHWEITE_STUECKLISTE_I_ID])) {
					wbmoral = hmStuecklistenWBZ.get(oZeile[REPORT_REICHWEITE_STUECKLISTE_I_ID]);
				}

				oZeile[REPORT_REICHWEITE_WIEDERBESCHAFFUNGSMORAL] = wbmoral + dlz;

			}

		}

		data = new Object[alDaten.size()][REPORT_REICHWEITE_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(parameter, StuecklisteReportFac.REPORT_MODUL, StuecklisteReportFac.REPORT_STUECKLISTE_REICHWEITE,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printLoseAktualisiert(TreeMap<String, Object[]> tmAufgeloesteFehlmengen,
			Integer stuecklisteIId, boolean bInclAusgegebenUndInProduktion, TheClientDto theClientDto) {

		Iterator<?> it = tmAufgeloesteFehlmengen.keySet().iterator();

		data = new Object[tmAufgeloesteFehlmengen.size()][5];
		int i = 0;
		while (it.hasNext()) {
			Object key = it.next();

			data[i] = tmAufgeloesteFehlmengen.get(key);

			i++;
		}

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		if (stuecklisteIId != null) {
			StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(stuecklisteIId, theClientDto);
			parameter.put("P_STUECKLISTE", stklDto.getArtikelDto().getCNr());
		}

		parameter.put("P_INCL_AUSGEGBEN_UND_IN_PRODUKTION", bInclAusgegebenUndInProduktion);

		index = -1;
		sAktuellerReport = StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT;

		initJRDS(parameter, StuecklisteReportFac.REPORT_MODUL, StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printWaffenregister(Integer stuecklisteIId, TheClientDto theClientDto) {

		StuecklisteDto dto = getStuecklisteFac().stuecklisteFindByPrimaryKey(stuecklisteIId, theClientDto);

		KundeDto kdDto = null;

		try {
			if (dto.getPartnerIId() != null) {
				kdDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(dto.getPartnerIId(),
						theClientDto.getMandant(), theClientDto);

			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		index = -1;
		sAktuellerReport = StuecklisteReportFac.REPORT_STUECKLISTE_WAFFENREGISTER;

		List<?> m = null;
		try {
			m = getStuecklisteFac().getStrukturDatenEinerStueckliste(stuecklisteIId, theClientDto,
					StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_OHNE, 0, null, true, false,
					new BigDecimal(1), null, true, false);
		} catch (RemoteException ex2) {
			throwEJBExceptionLPRespectOld(ex2);
		}

		Iterator<?> it = m.listIterator();
		data = new Object[m.size()][REPORT_STUECKLISTE_WAFFENREGISTER_ANZAHL_SPALTEN];
		int row = 0;
		while (it.hasNext()) {
			StuecklisteMitStrukturDto struktur = (StuecklisteMitStrukturDto) it.next();
			StuecklistepositionDto position = struktur.getStuecklistepositionDto();

			// Artikelkommentar Text und Bild
			ArtikelkommentarDto[] aKommentarDto = null;
			try {
				aKommentarDto = getArtikelkommentarFac().artikelkommentardruckFindByArtikelIIdBelegartCNr(
						position.getArtikelIId(), LocaleFac.BELEGART_STUECKLISTE, theClientDto.getLocUiAsString(),
						theClientDto);
			} catch (RemoteException ex3) {
			} catch (EJBExceptionLP ex3) {
			}

			String einrueckung = "";
			for (int i = 0; i < struktur.getIEbene(); i++) {
				einrueckung = einrueckung + "   ";
			}
			try {

				data[row][REPORT_STUECKLISTE_WAFFENREGISTER_I_EBENE] = struktur.getIEbene();

				data[row][REPORT_STUECKLISTE_WAFFENREGISTER_FREMDFERTIGUNG] = struktur.isBFremdfertigung();

				data[row][REPORT_STUECKLISTE_WAFFENREGISTER_FREIGABE_PERSON] = struktur.getCKurzzeichenPersonFreigabe();
				data[row][REPORT_STUECKLISTE_WAFFENREGISTER_FREIGABE_ZEITPUNKT] = struktur.getTFreigabe();

				data[row][REPORT_STUECKLISTE_WAFFENREGISTER_STUECKLISTEPOSITION_I_ID] = position.getIId();

				data[row][REPORT_STUECKLISTE_WAFFENREGISTER_I_SORT] = Helper
						.fitString2LengthAlignRight(position.getISort() + "", 10, '0');

				com.lp.server.artikel.service.ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKey(position.getArtikelIId(), theClientDto);

				String sArtikelKommentar = "";
				if (artikelDto.getArtikelartCNr() != null && artikelDto.getArtikelartCNr()
						.equals(com.lp.server.artikel.service.ArtikelFac.ARTIKELART_HANDARTIKEL)) {

					data[row][REPORT_STUECKLISTE_WAFFENREGISTER_ARTIKEL] = "";

				} else {
					data[row][REPORT_STUECKLISTE_WAFFENREGISTER_ARTIKEL] = einrueckung + artikelDto.getCNr();

					data[row][REPORT_STUECKLISTE_WAFFENREGISTER_ARTIKEL_FREIGABE_ZEITPUNKT] = artikelDto.getTFreigabe();

					if (artikelDto.getPersonalIIdFreigabe() != null) {
						data[row][REPORT_STUECKLISTE_WAFFENREGISTER_ARTIKEL_FREIGABE_PERSON] = getPersonalFac()
								.personalFindByPrimaryKeySmall(artikelDto.getPersonalIIdFreigabe()).getCKurzzeichen();
					}

					if (kdDto != null) {
						// KundeArtikelnr gueltig zu Belegdatum
						KundesokoDto kundeSokoDto_gueltig = this.getKundesokoFac()
								.kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc(kdDto.getIId(),
										artikelDto.getIId(),
										Helper.cutDate(new java.sql.Date(System.currentTimeMillis())));
						if (kundeSokoDto_gueltig != null) {
							data[row][REPORT_STUECKLISTE_WAFFENREGISTER_KUNDENARTIKELNUMMER] = kundeSokoDto_gueltig
									.getCKundeartikelnummer();
							data[row][REPORT_STUECKLISTE_WAFFENREGISTER_KUNDENARTIKELBEZEICHNUNG] = kundeSokoDto_gueltig
									.getCKundeartikelbez();
						}
					}

					Image imageKommentar = null;
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
								data[row][REPORT_STUECKLISTE_WAFFENREGISTER_IMAGE] = imageKommentar;
							}
						}
					}

				}

				data[row][REPORT_STUECKLISTE_WAFFENREGISTER_ARTIKEL_HERSTELLERNUMMER] = artikelDto
						.getCArtikelnrhersteller();
				data[row][REPORT_STUECKLISTE_WAFFENREGISTER_ARTIKEL_HERSTELLERBEZEICHNUNG] = artikelDto
						.getCArtikelbezhersteller();

				data[row][REPORT_STUECKLISTE_WAFFENREGISTER_SUBREPORT_ARTIKELKOMMENTAR] = getSubreportArtikelkommentar(
						artikelDto.getIId(), LocaleFac.BELEGART_STUECKLISTE, theClientDto);

				data[row][REPORT_STUECKLISTE_WAFFENREGISTER_INDEX] = artikelDto.getCIndex();
				data[row][REPORT_STUECKLISTE_WAFFENREGISTER_REVISION] = artikelDto.getCRevision();
				data[row][REPORT_STUECKLISTE_WAFFENREGISTER_GEWICHT] = artikelDto.getFGewichtkg();
				data[row][REPORT_STUECKLISTE_WAFFENREGISTER_WARENVERKEHRSNUMMER] = artikelDto.getCWarenverkehrsnummer();

				if (artikelDto.getLandIIdUrsprungsland() != null) {

					LandDto landDto = getSystemFac().landFindByPrimaryKey(artikelDto.getLandIIdUrsprungsland());
					data[row][REPORT_STUECKLISTE_WAFFENREGISTER_URSPRUNGSLAND_LKZ] = landDto.getCLkz();
					data[row][REPORT_STUECKLISTE_WAFFENREGISTER_URSPRUNGSLAND_NAME] = landDto.getCName();
				}

				if (artikelDto.getWaffenkaliberIId() != null) {
					WaffenkaliberDto wDto = getArtikelServiceFac()
							.waffenkaliberFindByPrimaryKey(artikelDto.getWaffenkaliberIId());
					data[row][REPORT_STUECKLISTE_WAFFENREGISTER_WAFFENKALIBER] = wDto.getCBez();
				}
				if (artikelDto.getWaffenausfuehrungIId() != null) {
					WaffenausfuehrungDto wDto = getArtikelServiceFac()
							.waffenausfuehrungFindByPrimaryKey(artikelDto.getWaffenausfuehrungIId());
					data[row][REPORT_STUECKLISTE_WAFFENREGISTER_WAFFENAUSFUEHRUNG] = wDto.getCBez();
				}
				if (artikelDto.getWaffenkategorieIId() != null) {
					WaffenkategorieDto wDto = getArtikelServiceFac()
							.waffenkategorieFindByPrimaryKey(artikelDto.getWaffenkategorieIId());
					data[row][REPORT_STUECKLISTE_WAFFENREGISTER_WAFFENKATEGORIE] = wDto.getCBez();
				}
				if (artikelDto.getWaffentypIId() != null) {
					WaffentypDto wDto = getArtikelServiceFac().waffentypFindByPrimaryKey(artikelDto.getWaffentypIId());
					data[row][REPORT_STUECKLISTE_WAFFENREGISTER_WAFFENTYP] = wDto.getCBez();
				}
				if (artikelDto.getWaffentypFeinIId() != null) {
					WaffentypFeinDto wDto = getArtikelServiceFac()
							.waffentypFeinFindByPrimaryKey(artikelDto.getWaffentypFeinIId());
					data[row][REPORT_STUECKLISTE_WAFFENREGISTER_WAFFENTYPFEIN] = wDto.getCBez();
				}
				if (artikelDto.getWaffenzusatzIId() != null) {
					WaffenzusatzDto wDto = getArtikelServiceFac()
							.waffenzusatzFindByPrimaryKey(artikelDto.getWaffenzusatzIId());
					data[row][REPORT_STUECKLISTE_WAFFENREGISTER_WAFFENZUSATZ] = wDto.getCBez();
				}

				String sBezeichnung = artikelDto.getArtikelsprDto().getCBez();
				if (sArtikelKommentar != "") {
					data[row][REPORT_STUECKLISTE_WAFFENREGISTER_KOMMENTAR] = sArtikelKommentar;
				}
				data[row][REPORT_STUECKLISTE_WAFFENREGISTER_ARTIKELBEZEICHNUNG] = sBezeichnung;

				String sZusatzbez = artikelDto.getArtikelsprDto().getCZbez();
				data[row][REPORT_STUECKLISTE_WAFFENREGISTER_ARTIKELZUSATZBEZEICHNUNG] = sZusatzbez;
				data[row][REPORT_STUECKLISTE_WAFFENREGISTER_ARTIKELZUSATZBEZEICHNUNG2] = artikelDto.getArtikelsprDto()
						.getCZbez2();
				data[row][REPORT_STUECKLISTE_WAFFENREGISTER_REFERENZNUMMER] = artikelDto.getCReferenznr();

				if (artikelDto.getArtikelsprDto() != null) {
					data[row][REPORT_STUECKLISTE_WAFFENREGISTER_KURZBEZEICHNUNG] = artikelDto.getArtikelsprDto()
							.getCKbez();
				}
				EinheitDto einheitDto = getSystemFac().einheitFindByPrimaryKey(position.getEinheitCNr(), theClientDto);

				int dimension = einheitDto.getIDimension().intValue();

				if (position.getFDimension1() == null) {
					position.setFDimension1(new Float(1));
				} else if (position.getFDimension2() == null) {
					position.setFDimension2(new Float(1));
				}
				if (position.getFDimension3() == null) {
					position.setFDimension3(new Float(1));
				}

				data[row][REPORT_STUECKLISTE_WAFFENREGISTER_MENGE] = position.getNMenge();

				data[row][REPORT_STUECKLISTE_WAFFENREGISTER_FORMEL] = position.getXFormel();

				if (dimension == 1) {
					data[row][REPORT_STUECKLISTE_WAFFENREGISTER_DIMENSION] = "L"
							+ Helper.formatZahl(position.getFDimension1(), 2, theClientDto.getLocUi())
							+ einheitDto.getCNr();
				} else if (dimension == 2) {
					data[row][REPORT_STUECKLISTE_WAFFENREGISTER_DIMENSION] = "B"
							+ Helper.formatZahl(position.getFDimension1(), 2, theClientDto.getLocUi()) + "/T"
							+ Helper.formatZahl(position.getFDimension2(), 2, theClientDto.getLocUi())
							+ einheitDto.getCNr();

				} else if (dimension == 3) {
					data[row][REPORT_STUECKLISTE_WAFFENREGISTER_DIMENSION] = "B"
							+ Helper.formatZahl(position.getFDimension1(), 2, theClientDto.getLocUi()) + "/T"
							+ Helper.formatZahl(position.getFDimension2(), 2, theClientDto.getLocUi()) + "/H"
							+ Helper.formatZahl(position.getFDimension3(), 2, theClientDto.getLocUi())
							+ einheitDto.getCNr();

				}

				// PJ20923
				data[row][REPORT_STUECKLISTE_WAFFENREGISTER_DIMENSION1] = position.getFDimension1();
				data[row][REPORT_STUECKLISTE_WAFFENREGISTER_DIMENSION2] = position.getFDimension2();
				data[row][REPORT_STUECKLISTE_WAFFENREGISTER_DIMENSION3] = position.getFDimension3();
				data[row][REPORT_STUECKLISTE_WAFFENREGISTER_POSITIONSEINHEIT] = position.getEinheitCNr();

				data[row][REPORT_STUECKLISTE_WAFFENREGISTER_ZIELMENGE] = position.getNZielmenge();

				if (dimension != 0) {
					// Wenn Dimension der Einheit <> 0, dann wird Stk als
					// Ausgangseinheit eingetragen
					data[row][REPORT_STUECKLISTE_WAFFENREGISTER_STUECKLISTENEINHEIT] = SystemFac.EINHEIT_STUECK.trim();
				} else {
					data[row][REPORT_STUECKLISTE_WAFFENREGISTER_STUECKLISTENEINHEIT] = position.getEinheitCNr();
				}

				data[row][REPORT_STUECKLISTE_WAFFENREGISTER_ZIELMENGENEINHEIT] = artikelDto.getEinheitCNr();
				data[row][REPORT_STUECKLISTE_WAFFENREGISTER_POSITION] = position.getCPosition();
				data[row][REPORT_STUECKLISTE_WAFFENREGISTER_LFDNUMMER] = position.getILfdnummer();
				data[row][REPORT_STUECKLISTE_WAFFENREGISTER_MONTAGEART] = position.getMontageartDto().getCBez();
				data[row][REPORT_STUECKLISTE_WAFFENREGISTER_HOCHSTELLEN] = "N";
				if (artikelDto.getMontageDto() != null
						&& Helper.short2boolean(artikelDto.getMontageDto().getBHochstellen()) == true) {
					data[row][REPORT_STUECKLISTE_WAFFENREGISTER_HOCHSTELLEN] = "J";
				}
				data[row][REPORT_STUECKLISTE_WAFFENREGISTER_HOCHSETZEN] = "N";
				if (artikelDto.getMontageDto() != null
						&& Helper.short2boolean(artikelDto.getMontageDto().getBHochsetzen()) == true) {
					data[row][REPORT_STUECKLISTE_WAFFENREGISTER_HOCHSETZEN] = "J";
				}

				if (artikelDto.getMontageDto() != null) {
					data[row][REPORT_STUECKLISTE_WAFFENREGISTER_RASTERLIEGEND] = artikelDto.getMontageDto()
							.getFRasterliegend();
					data[row][REPORT_STUECKLISTE_WAFFENREGISTER_RASTERSTEHEND] = artikelDto.getMontageDto()
							.getFRasterstehend();
				}

				if (artikelDto.getHerstellerIId() != null) {
					HerstellerDto herstellerDto = getArtikelFac()
							.herstellerFindByPrimaryKey(artikelDto.getHerstellerIId(), theClientDto);

					data[row][REPORT_STUECKLISTE_WAFFENREGISTER_HERSTELLER] = herstellerDto.getCNr();
					PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(herstellerDto.getPartnerIId(),
							theClientDto);
					data[row][REPORT_STUECKLISTE_WAFFENREGISTER_HERSTELLER_NAME] = partnerDto.formatFixName1Name2();
				}

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

			row++;
		}

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		if (kdDto != null) {

			PartnerDto pDto = getPartnerFac().partnerFindByPrimaryKey(kdDto.getPartnerIId(), theClientDto);
			parameter.put("P_KUNDE", pDto.formatFixTitelName1Name2());
		}

		parameter.put("P_STUECKLISTE", dto.getArtikelDto().getCNr());
		parameter.put("P_ERFASSUNGSFAKTOR", dto.getNErfassungsfaktor());
		parameter.put("P_ANLEGEN", dto.getTAnlegen());
		parameter.put("P_AENDERN", dto.getTAendern());
		parameter.put("P_AENDERN_POSITION", dto.getTAendernposition());
		parameter.put("P_AENDERN_ARBEITSPLAN", dto.getTAendernarbeitsplan());

		if (dto.getArtikelDto().getWaffenkaliberIId() != null) {
			WaffenkaliberDto wDto = getArtikelServiceFac()
					.waffenkaliberFindByPrimaryKey(dto.getArtikelDto().getWaffenkaliberIId());
			parameter.put("P_WAFFENKALIBER", wDto.getCBez());
		}
		if (dto.getArtikelDto().getWaffenausfuehrungIId() != null) {
			WaffenausfuehrungDto wDto = getArtikelServiceFac()
					.waffenausfuehrungFindByPrimaryKey(dto.getArtikelDto().getWaffenausfuehrungIId());
			parameter.put("P_WAFFENAUSFUEHRUNG", wDto.getCBez());

		}
		if (dto.getArtikelDto().getWaffenkategorieIId() != null) {
			WaffenkategorieDto wDto = getArtikelServiceFac()
					.waffenkategorieFindByPrimaryKey(dto.getArtikelDto().getWaffenkategorieIId());
			parameter.put("P_WAFFENKATEGORIE", wDto.getCBez());

		}
		if (dto.getArtikelDto().getWaffentypIId() != null) {
			WaffentypDto wDto = getArtikelServiceFac().waffentypFindByPrimaryKey(dto.getArtikelDto().getWaffentypIId());
			parameter.put("P_WAFFENTYP", wDto.getCBez());

		}
		if (dto.getArtikelDto().getWaffentypFeinIId() != null) {
			WaffentypFeinDto wDto = getArtikelServiceFac()
					.waffentypFeinFindByPrimaryKey(dto.getArtikelDto().getWaffentypFeinIId());
			parameter.put("P_WAFFENTYPFEIN", wDto.getCBez());

		}
		if (dto.getArtikelDto().getWaffenzusatzIId() != null) {
			WaffenzusatzDto wDto = getArtikelServiceFac()
					.waffenzusatzFindByPrimaryKey(dto.getArtikelDto().getWaffenzusatzIId());
			parameter.put("P_WAFFENZUSATZ", wDto.getCBez());

		}

		parameter.put("P_FREIGABE_ZEITPUNKT", dto.getTFreigabe());
		if (dto.getPersonalIIdFreigabe() != null) {
			parameter.put("P_FREIGABE_PERSON", getPersonalFac()
					.personalFindByPrimaryKey(dto.getPersonalIIdFreigabe(), theClientDto).getCKurzzeichen());
		}

		if (Helper.short2boolean(dto.getBMitFormeln()) == true || dto.getStuecklisteIIdFormelstueckliste() != null) {

			String[] fieldnames = new String[] { "Parameter", "Bezeichnung", "Typ", "Wert" };

			StklparameterDto[] stklparameterDtos = getStuecklisteFac().stklparameterFindByStuecklisteIId(stuecklisteIId,
					theClientDto);

			Object[][] dataSub = new Object[stklparameterDtos.length][fieldnames.length];

			for (int i = 0; i < stklparameterDtos.length; i++) {

				dataSub[i][0] = stklparameterDtos[i].getCNr();

				StklparameterDto stklparameterDto = getStuecklisteFac()
						.stklparameterFindByPrimaryKey(stklparameterDtos[i].getIId(), theClientDto);
				if (stklparameterDto.getStklparametersprDto() != null) {
					dataSub[i][1] = stklparameterDto.getStklparametersprDto().getCBez();
				}
				dataSub[i][2] = stklparameterDto.getCTyp();
				dataSub[i][3] = stklparameterDto.getCWert();

			}

			parameter.put("P_SUBREPORT_KONFIGURATIONSPARAMETER", new LPDatenSubreport(dataSub, fieldnames));

		}

		parameter.put("P_STUECKLISTEBEZEICHNUNG", dto.getArtikelDto().formatBezeichnung());
		if (dto.getArtikelDto().getArtikelsprDto() != null) {
			parameter.put("P_STUECKLISTEKURZBEZEICHNUNG", dto.getArtikelDto().getArtikelsprDto().getCKbez());
			parameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG", dto.getArtikelDto().getArtikelsprDto().getCZbez());
			parameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG2", dto.getArtikelDto().getArtikelsprDto().getCZbez2());
		}
		try {
			// Zeichnungsnummer
			StuecklisteeigenschaftDto[] stuecklisteeigenschaftDtos = getStuecklisteFac()
					.stuecklisteeigenschaftFindByStuecklisteIId(dto.getIId());
			ArrayList<Object[]> al = new ArrayList<Object[]>();
			for (int i = 0; i < stuecklisteeigenschaftDtos.length; i++) {
				StuecklisteeigenschaftDto stuecklisteeigenschaftDto = stuecklisteeigenschaftDtos[i];

				Object[] o = new Object[2];
				o[0] = stuecklisteeigenschaftDto.getStuecklisteeigenschaftartDto().getCBez();
				o[1] = stuecklisteeigenschaftDto.getCBez();
				al.add(o);
			}

			if (stuecklisteeigenschaftDtos.length > 0) {
				String[] fieldnames = new String[] { "F_EIGENSCHAFTART", "F_BEZEICHNUNG" };
				Object[][] dataSub = new Object[al.size()][fieldnames.length];
				dataSub = (Object[][]) al.toArray(dataSub);

				parameter.put("DATENSUBREPORT", new LPDatenSubreport(dataSub, fieldnames));
			}

		} catch (RemoteException ex4) {
			throwEJBExceptionLPRespectOld(ex4);
		}

		initJRDS(parameter, StuecklisteReportFac.REPORT_MODUL, StuecklisteReportFac.REPORT_STUECKLISTE_WAFFENREGISTER,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printStuecklisteAllgemein(Integer stuecklisteIId, Boolean bMitPositionskommentar,
			Boolean bMitStuecklistenkommentar, Boolean bUnterstuecklistenEinbinden,
			Boolean bGleichePositionenZusammenfassen, Integer iOptionSortierungUnterstuecklisten,
			boolean bUnterstklstrukurBelassen, TheClientDto theClientDto, Integer iOptionSortierungStuecklisteGesamt1,
			Integer iOptionSortierungStuecklisteGesamt2, Integer iOptionSortierungStuecklisteGesamt3,
			String[] labelSortierungen, boolean fremdfertigungAufloesen) {

		if (stuecklisteIId == null || bMitPositionskommentar == null || bMitStuecklistenkommentar == null
				|| bUnterstuecklistenEinbinden == null || bGleichePositionenZusammenfassen == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"stuecklisteIId == null || bMitPositionskommentar == null || bMitStuecklistenkommentar == null || bUnterstuecklistenEinbinden == null || bGleichePositionenZusammenfassen == null"));
		}
		if (bUnterstuecklistenEinbinden.booleanValue() == true && iOptionSortierungUnterstuecklisten == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"bUnterstuecklistenEinbinden.booleanValue()==true && iOptionSortierungUnterstuecklisten==null"));
		}

		StuecklisteDto dto = getStuecklisteFac().stuecklisteFindByPrimaryKey(stuecklisteIId, theClientDto);

		KundeDto kdDto = null;

		try {
			if (dto.getPartnerIId() != null) {
				kdDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(dto.getPartnerIId(),
						theClientDto.getMandant(), theClientDto);

			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		index = -1;
		sAktuellerReport = StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEIN_OHNEPREIS;

		List<?> m = null;
		try {
			m = getStuecklisteFac().getStrukturDatenEinerStueckliste(stuecklisteIId, theClientDto,
					iOptionSortierungUnterstuecklisten.intValue(), 0, null, bUnterstuecklistenEinbinden.booleanValue(),
					bGleichePositionenZusammenfassen.booleanValue(), new BigDecimal(1), null, bUnterstklstrukurBelassen,
					fremdfertigungAufloesen);
		} catch (RemoteException ex2) {
			throwEJBExceptionLPRespectOld(ex2);
		}

		Iterator<?> it = m.listIterator();
		data = new Object[m.size()][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ANZAHL_SPALTEN];
		int row = 0;
		while (it.hasNext()) {
			StuecklisteMitStrukturDto struktur = (StuecklisteMitStrukturDto) it.next();
			StuecklistepositionDto position = struktur.getStuecklistepositionDto();

			// Artikelkommentar Text und Bild
			ArtikelkommentarDto[] aKommentarDto = null;
			try {
				aKommentarDto = getArtikelkommentarFac().artikelkommentardruckFindByArtikelIIdBelegartCNr(
						position.getArtikelIId(), LocaleFac.BELEGART_STUECKLISTE, theClientDto.getLocUiAsString(),
						theClientDto);
			} catch (RemoteException ex3) {
			} catch (EJBExceptionLP ex3) {
			}

			String einrueckung = "";
			for (int i = 0; i < struktur.getIEbene(); i++) {
				einrueckung = einrueckung + "   ";
			}
			try {

				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_I_EBENE] = struktur.getIEbene();

				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_FREMDFERTIGUNG] = struktur.isBFremdfertigung();

				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_FREIGABE_PERSON] = struktur
						.getCKurzzeichenPersonFreigabe();
				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_FREIGABE_ZEITPUNKT] = struktur.getTFreigabe();

				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_STUECKLISTEPOSITION_I_ID] = position.getIId();
				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_INITIALKOSTEN] = Helper
						.short2Boolean(position.getBInitial());

				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_I_SORT] = Helper
						.fitString2LengthAlignRight(position.getISort() + "", 10, '0');

				com.lp.server.artikel.service.ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKey(position.getArtikelIId(), theClientDto);

				String sArtikelKommentar = "";
				if (artikelDto.getArtikelartCNr() != null && artikelDto.getArtikelartCNr()
						.equals(com.lp.server.artikel.service.ArtikelFac.ARTIKELART_HANDARTIKEL)) {

					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKEL] = "";

				} else {
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKEL] = einrueckung + artikelDto.getCNr();

					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKEL_FREIGABE_ZEITPUNKT] = artikelDto
							.getTFreigabe();

					if (artikelDto.getPersonalIIdFreigabe() != null) {
						data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKEL_FREIGABE_PERSON] = getPersonalFac()
								.personalFindByPrimaryKeySmall(artikelDto.getPersonalIIdFreigabe()).getCKurzzeichen();
					}

					if (kdDto != null) {
						// KundeArtikelnr gueltig zu Belegdatum
						KundesokoDto kundeSokoDto_gueltig = this.getKundesokoFac()
								.kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc(kdDto.getIId(),
										artikelDto.getIId(),
										Helper.cutDate(new java.sql.Date(System.currentTimeMillis())));
						if (kundeSokoDto_gueltig != null) {
							data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_KUNDENARTIKELNUMMER] = kundeSokoDto_gueltig
									.getCKundeartikelnummer();
							data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_KUNDENARTIKELBEZEICHNUNG] = kundeSokoDto_gueltig
									.getCKundeartikelbez();
						}
					}

					Image imageKommentar = null;
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
								data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_IMAGE] = imageKommentar;
							}
						}
					}

				}

				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKEL_HERSTELLERNUMMER] = artikelDto
						.getCArtikelnrhersteller();
				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKEL_HERSTELLERBEZEICHNUNG] = artikelDto
						.getCArtikelbezhersteller();

				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_SUBREPORT_ARTIKELKOMMENTAR] = getSubreportArtikelkommentar(
						artikelDto.getIId(), LocaleFac.BELEGART_STUECKLISTE, theClientDto);

				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_INDEX] = artikelDto.getCIndex();
				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_REVISION] = artikelDto.getCRevision();
				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_GEWICHT] = artikelDto.getFGewichtkg();
				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_WARENVERKEHRSNUMMER] = artikelDto
						.getCWarenverkehrsnummer();

				if (artikelDto.getLandIIdUrsprungsland() != null) {

					LandDto landDto = getSystemFac().landFindByPrimaryKey(artikelDto.getLandIIdUrsprungsland());
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_URSPRUNGSLAND_LKZ] = landDto.getCLkz();
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_URSPRUNGSLAND_NAME] = landDto.getCName();
				}

				String sBezeichnung = artikelDto.getArtikelsprDto().getCBez();
				if (sArtikelKommentar != "") {
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_KOMMENTAR] = sArtikelKommentar;
				}
				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKELBEZEICHNUNG] = sBezeichnung;

				String sZusatzbez = artikelDto.getArtikelsprDto().getCZbez();
				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKELZUSATZBEZEICHNUNG] = sZusatzbez;
				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKELZUSATZBEZEICHNUNG2] = artikelDto
						.getArtikelsprDto().getCZbez2();
				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_REFERENZNUMMER] = artikelDto.getCReferenznr();

				if (artikelDto.getArtikelsprDto() != null) {
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_KURZBEZEICHNUNG] = artikelDto.getArtikelsprDto()
							.getCKbez();
				}
				EinheitDto einheitDto = getSystemFac().einheitFindByPrimaryKey(position.getEinheitCNr(), theClientDto);

				int dimension = einheitDto.getIDimension().intValue();

				if (position.getFDimension1() == null) {
					position.setFDimension1(new Float(1));
				} else if (position.getFDimension2() == null) {
					position.setFDimension2(new Float(1));
				}
				if (position.getFDimension3() == null) {
					position.setFDimension3(new Float(1));
				}

				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_MENGE] = position.getNMenge();

				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_FORMEL] = position.getXFormel();

				if (dimension == 1) {
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_DIMENSION] = "L"
							+ Helper.formatZahl(position.getFDimension1(), 2, theClientDto.getLocUi())
							+ einheitDto.getCNr();
				} else if (dimension == 2) {
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_DIMENSION] = "B"
							+ Helper.formatZahl(position.getFDimension1(), 2, theClientDto.getLocUi()) + "/T"
							+ Helper.formatZahl(position.getFDimension2(), 2, theClientDto.getLocUi())
							+ einheitDto.getCNr();

				} else if (dimension == 3) {
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_DIMENSION] = "B"
							+ Helper.formatZahl(position.getFDimension1(), 2, theClientDto.getLocUi()) + "/T"
							+ Helper.formatZahl(position.getFDimension2(), 2, theClientDto.getLocUi()) + "/H"
							+ Helper.formatZahl(position.getFDimension3(), 2, theClientDto.getLocUi())
							+ einheitDto.getCNr();

				}

				// PJ20923
				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_DIMENSION1] = position.getFDimension1();
				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_DIMENSION2] = position.getFDimension2();
				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_DIMENSION3] = position.getFDimension3();
				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_POSITIONSEINHEIT] = position.getEinheitCNr();

				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ZIELMENGE] = position.getNZielmenge();

				if (dimension != 0) {
					// Wenn Dimension der Einheit <> 0, dann wird Stk als
					// Ausgangseinheit eingetragen
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_STUECKLISTENEINHEIT] = SystemFac.EINHEIT_STUECK
							.trim();
				} else {
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_STUECKLISTENEINHEIT] = position.getEinheitCNr();
				}

				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ZIELMENGENEINHEIT] = artikelDto.getEinheitCNr();
				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_POSITION] = position.getCPosition();
				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_LFDNUMMER] = position.getILfdnummer();
				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_MONTAGEART] = position.getMontageartDto().getCBez();
				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_HOCHSTELLEN] = "N";
				if (artikelDto.getMontageDto() != null
						&& Helper.short2boolean(artikelDto.getMontageDto().getBHochstellen()) == true) {
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_HOCHSTELLEN] = "J";
				}
				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_HOCHSETZEN] = "N";
				if (artikelDto.getMontageDto() != null
						&& Helper.short2boolean(artikelDto.getMontageDto().getBHochsetzen()) == true) {
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_HOCHSETZEN] = "J";
				}

				if (artikelDto.getMontageDto() != null) {
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_RASTERLIEGEND] = artikelDto.getMontageDto()
							.getFRasterliegend();
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_RASTERSTEHEND] = artikelDto.getMontageDto()
							.getFRasterstehend();
				}

				if (artikelDto.getHerstellerIId() != null) {
					HerstellerDto herstellerDto = getArtikelFac()
							.herstellerFindByPrimaryKey(artikelDto.getHerstellerIId(), theClientDto);

					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_HERSTELLER] = herstellerDto.getCNr();
					PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(herstellerDto.getPartnerIId(),
							theClientDto);
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_HERSTELLER_NAME] = partnerDto.formatFixName1Name2();
				}

				if (bMitPositionskommentar.booleanValue() == true) {
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_POSITIONSKOMMENTAR] = position.getCKommentar();
				}

				if (artikelDto.getMaterialIId() != null) {
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKEL_MATERIAL] = getMaterialFac()
							.materialFindByPrimaryKey(artikelDto.getMaterialIId(), theClientDto).getBezeichnung();
				}

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

			row++;
		}

		if (!bUnterstklstrukurBelassen) {

			// Nach Fertigungsgruppe sortieren
			for (int k = data.length - 1; k > 0; --k) {
				for (int j = 0; j < k; ++j) {
					Object[] a1 = (Object[]) data[j];
					Object[] a2 = (Object[]) data[j + 1];

					if (iOptionSortierungUnterstuecklisten == StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_ARTIKELNR) {

						if (((String) a1[REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKEL])
								.compareTo((String) a2[REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKEL]) > 0) {
							data[j] = a2;
							data[j + 1] = a1;

						}
					} else if (iOptionSortierungUnterstuecklisten == StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_POSITION) {

						String s1 = (String) a1[REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_POSITION];
						if (s1 == null) {
							s1 = "";
						}
						String s2 = (String) a2[REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_POSITION];
						if (s2 == null) {
							s2 = "";
						}

						if (s1.compareTo(s2) > 0) {
							data[j] = a2;
							data[j + 1] = a1;

						}

					}
				}

			}

		}

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		if (kdDto != null) {

			PartnerDto pDto = getPartnerFac().partnerFindByPrimaryKey(kdDto.getPartnerIId(), theClientDto);
			parameter.put("P_KUNDE", pDto.formatFixTitelName1Name2());
		}

		parameter.put("P_STUECKLISTE", dto.getArtikelDto().getCNr());
		parameter.put("P_ERFASSUNGSFAKTOR", dto.getNErfassungsfaktor());
		parameter.put("P_ANLEGEN", dto.getTAnlegen());
		parameter.put("P_AENDERN", dto.getTAendern());
		parameter.put("P_AENDERN_POSITION", dto.getTAendernposition());
		parameter.put("P_AENDERN_ARBEITSPLAN", dto.getTAendernarbeitsplan());

		parameter.put("P_FREMDFERTIGUNG_AUFLOESEN", fremdfertigungAufloesen);

		parameter.put("P_MIT_FORMELN", Helper.short2Boolean(dto.getBMitFormeln()));

		parameter.put("P_FREIGABE_ZEITPUNKT", dto.getTFreigabe());
		if (dto.getPersonalIIdFreigabe() != null) {
			parameter.put("P_FREIGABE_PERSON", getPersonalFac()
					.personalFindByPrimaryKey(dto.getPersonalIIdFreigabe(), theClientDto).getCKurzzeichen());
		}

		if (Helper.short2boolean(dto.getBMitFormeln()) == true || dto.getStuecklisteIIdFormelstueckliste() != null) {

			String[] fieldnames = new String[] { "Parameter", "Bezeichnung", "Typ", "Wert" };

			StklparameterDto[] stklparameterDtos = getStuecklisteFac().stklparameterFindByStuecklisteIId(stuecklisteIId,
					theClientDto);

			Object[][] dataSub = new Object[stklparameterDtos.length][fieldnames.length];

			for (int i = 0; i < stklparameterDtos.length; i++) {

				dataSub[i][0] = stklparameterDtos[i].getCNr();

				StklparameterDto stklparameterDto = getStuecklisteFac()
						.stklparameterFindByPrimaryKey(stklparameterDtos[i].getIId(), theClientDto);
				if (stklparameterDto.getStklparametersprDto() != null) {
					dataSub[i][1] = stklparameterDto.getStklparametersprDto().getCBez();
				}
				dataSub[i][2] = stklparameterDto.getCTyp();
				dataSub[i][3] = stklparameterDto.getCWert();

			}

			parameter.put("P_SUBREPORT_KONFIGURATIONSPARAMETER", new LPDatenSubreport(dataSub, fieldnames));

		}

		parameter.put("P_STUECKLISTEBEZEICHNUNG", dto.getArtikelDto().formatBezeichnung());
		if (dto.getArtikelDto().getArtikelsprDto() != null) {
			parameter.put("P_STUECKLISTEKURZBEZEICHNUNG", dto.getArtikelDto().getArtikelsprDto().getCKbez());
			parameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG", dto.getArtikelDto().getArtikelsprDto().getCZbez());
			parameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG2", dto.getArtikelDto().getArtikelsprDto().getCZbez2());
		}
		try {
			// Zeichnungsnummer
			StuecklisteeigenschaftDto[] stuecklisteeigenschaftDtos = getStuecklisteFac()
					.stuecklisteeigenschaftFindByStuecklisteIId(dto.getIId());
			ArrayList<Object[]> al = new ArrayList<Object[]>();
			for (int i = 0; i < stuecklisteeigenschaftDtos.length; i++) {
				StuecklisteeigenschaftDto stuecklisteeigenschaftDto = stuecklisteeigenschaftDtos[i];

				Object[] o = new Object[2];
				o[0] = stuecklisteeigenschaftDto.getStuecklisteeigenschaftartDto().getCBez();
				o[1] = stuecklisteeigenschaftDto.getCBez();
				al.add(o);
			}

			if (stuecklisteeigenschaftDtos.length > 0) {
				String[] fieldnames = new String[] { "F_EIGENSCHAFTART", "F_BEZEICHNUNG" };
				Object[][] dataSub = new Object[al.size()][fieldnames.length];
				dataSub = (Object[][]) al.toArray(dataSub);

				parameter.put("DATENSUBREPORT", new LPDatenSubreport(dataSub, fieldnames));
			}

		} catch (RemoteException ex4) {
			throwEJBExceptionLPRespectOld(ex4);
		}
		if (bMitStuecklistenkommentar.booleanValue() == true) {
			parameter.put("P_KOMMENTAR", Helper.formatStyledTextForJasper(dto.getXKommentar()));
		}

		// Nun nach Kriterium 1/2/3 sortieren PJ 06/2418
		if (!bUnterstuecklistenEinbinden) {
			data = sortiereStuecklistendruck(data, iOptionSortierungStuecklisteGesamt1,
					iOptionSortierungStuecklisteGesamt2, iOptionSortierungStuecklisteGesamt3, false);

			if (labelSortierungen != null && labelSortierungen.length > 2) {
				parameter.put("P_SORTIERUNG1", labelSortierungen[0]);
				parameter.put("P_SORTIERUNG2", labelSortierungen[1]);
				parameter.put("P_SORTIERUNG3", labelSortierungen[2]);
			}

		}

		// PJ15813 Ersatztypen hinzufuegen
		if (bGleichePositionenZusammenfassen == false) {

			ArrayList alDaten = new ArrayList();
			for (int i = 0; i < data.length; i++) {

				alDaten.add(data[i]);

				PosersatzDto[] posersatzDtos = getStuecklisteFac().posersatzFindByStuecklistepositionIId(
						(Integer) data[i][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_STUECKLISTEPOSITION_I_ID]);

				for (int j = 0; j < posersatzDtos.length; j++) {
					Object[] neueZeile = data[i].clone();

					String einrueckung = "";
					for (int k = 0; k < (Integer) data[i][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_I_EBENE]; k++) {
						einrueckung = einrueckung + "   ";
					}

					com.lp.server.artikel.service.ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(posersatzDtos[j].getArtikelIIdErsatz(), theClientDto);

					neueZeile[REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ERSATZARTIKEL] = einrueckung + artikelDto.getCNr();

					neueZeile[REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ERSATZARTIKEL_BEZEICHNUNG] = artikelDto
							.getCBezAusSpr();

					neueZeile[REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ERSATZARTIKEL_HERSTELLERNUMMER] = artikelDto
							.getCArtikelnrhersteller();
					neueZeile[REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ERSATZARTIKEL_HERSTELLERBEZEICHNUNG] = artikelDto
							.getCArtikelbezhersteller();

					if (artikelDto.getArtikelsprDto() != null) {
						neueZeile[REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ERSATZARTIKEL_ZUSATZBEZEICHNUNG] = artikelDto
								.getArtikelsprDto().getCZbez();
						neueZeile[REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ERSATZARTIKEL_ZUSATZBEZEICHNUNG2] = artikelDto
								.getArtikelsprDto().getCZbez2();
					}

					neueZeile[REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ZIELMENGE] = new BigDecimal(0);
					neueZeile[REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_MENGE] = new BigDecimal(0);

					alDaten.add(neueZeile);

				}

			}

			data = new Object[alDaten.size()][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ANZAHL_SPALTEN];
			data = (Object[][]) alDaten.toArray(data);

		}

		initJRDS(parameter, StuecklisteReportFac.REPORT_MODUL,
				StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEIN_OHNEPREIS, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	/**
	 * Methode fuer JRDataSource
	 * 
	 * @return boolean
	 * @throws JRException
	 */
	public boolean next() throws JRException {

		index++;
		return (index < data.length);

	}

	public Object[][] sortiereStuecklistendruck(Object[][] dataTemp, int iSortierung1, int iSortierung2,
			int iSortierung3, boolean bAllgemeinMitPreis) {

		for (int k = dataTemp.length - 1; k > 0; --k) {
			for (int j = 0; j < k; ++j) {
				Object[] a1 = (Object[]) dataTemp[j];
				Object[] a2 = (Object[]) dataTemp[j + 1];

				a1 = befuelleFelder(a1, iSortierung1, bAllgemeinMitPreis);
				a1 = befuelleFelder(a1, iSortierung2, bAllgemeinMitPreis);
				a1 = befuelleFelder(a1, iSortierung3, bAllgemeinMitPreis);

				a2 = befuelleFelder(a2, iSortierung1, bAllgemeinMitPreis);
				a2 = befuelleFelder(a2, iSortierung2, bAllgemeinMitPreis);
				a2 = befuelleFelder(a2, iSortierung3, bAllgemeinMitPreis);

				int v = 0;
				if (a1[iSortierung1] instanceof Integer && a2[iSortierung1] instanceof Integer) {
					v = Integer.compare((Integer) a1[iSortierung1], (Integer) a2[iSortierung1]);
				} else {
					v = (a1[iSortierung1].toString().compareTo(a2[iSortierung1].toString()));
				}

				if (v == 0) {
					v = (a1[iSortierung2].toString().compareTo(a2[iSortierung2].toString()));

					if (v == 0) {
						v = (a1[iSortierung3].toString().compareTo(a2[iSortierung3].toString()));
					}

				}

				if (v > 0) {
					dataTemp[j] = a2;
					dataTemp[j + 1] = a1;

				}

			}

		}

		return dataTemp;

	}

	private Object[] befuelleFelder(Object[] zeile, int iSortierung, boolean bAllgemeinMitPreis) {

		if (zeile[iSortierung] == null) {
			if (bAllgemeinMitPreis) {
				if (iSortierung == StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEINMITPREIS_LFDNUMMER) {
					zeile[iSortierung] = new Integer(0);
					zeile[iSortierung] = new Integer(0);
				} else {
					zeile[iSortierung] = "";
					zeile[iSortierung] = "";
				}
			} else {
				if (iSortierung == StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_LFDNUMMER) {
					zeile[iSortierung] = new Integer(0);
					zeile[iSortierung] = new Integer(0);
				} else {
					zeile[iSortierung] = "";
					zeile[iSortierung] = "";
				}
			}
		}
		return zeile;

	}

	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;

		String fieldName = field.getName();

		if (sAktuellerReport.equals(StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEIN_OHNEPREIS)) {
			if ("Artikel".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKEL];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKELBEZEICHNUNG];
			} else if ("Artikelzusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKELZUSATZBEZEICHNUNG];
			} else if ("Artikelzusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKELZUSATZBEZEICHNUNG2];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_STUECKLISTENEINHEIT];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_MENGE];
			} else if ("Formel".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_FORMEL];
			} else if ("Fremdfertigung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_FREMDFERTIGUNG];
			} else if ("Position".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_POSITION];
			} else if ("Preis".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_PREIS];
			} else if ("Dimension".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_DIMENSION];
			} else if ("Zielmenge".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ZIELMENGE];
			} else if ("Zieleinheit".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ZIELMENGENEINHEIT];
			} else if ("Montageart".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_MONTAGEART];
			} else if ("Hochstellen".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_HOCHSTELLEN];
			} else if ("Hochsetzen".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_HOCHSETZEN];
			} else if ("F_IMAGE".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_IMAGE];
			} else if ("Kurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_KURZBEZEICHNUNG];
			} else if ("Referenznummer".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_REFERENZNUMMER];
			} else if ("Rasterliegend".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_RASTERLIEGEND];
			} else if ("Rasterstehend".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_RASTERSTEHEND];
			} else if ("Hersteller".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_HERSTELLER];
			} else if ("Herstellername".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_HERSTELLER_NAME];
			} else if ("Positionskommentar".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_POSITIONSKOMMENTAR];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_KOMMENTAR];
			} else if ("Lfdnummer".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_LFDNUMMER];
			} else if ("Index".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_INDEX];
			} else if ("Revision".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_REVISION];
			} else if ("Warenverkehrsnummer".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_WARENVERKEHRSNUMMER];
			} else if ("UrsprungslandLkz".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_URSPRUNGSLAND_LKZ];
			} else if ("UrsprungslandName".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_URSPRUNGSLAND_NAME];
			} else if ("Ersatzartikel".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ERSATZARTIKEL];
			} else if ("Ersatzartikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ERSATZARTIKEL_BEZEICHNUNG];
			} else if ("Ersatzartikelzusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ERSATZARTIKEL_ZUSATZBEZEICHNUNG];
			} else if ("Ersatzartikelzusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ERSATZARTIKEL_ZUSATZBEZEICHNUNG2];
			} else if ("Artikelgewicht".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_GEWICHT];
			} else if ("ArtikelHerstellernummer".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKEL_HERSTELLERNUMMER];
			} else if ("ArtikelHerstellerbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKEL_HERSTELLERBEZEICHNUNG];
			} else if ("ErsatzartikelHerstellernummer".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ERSATZARTIKEL_HERSTELLERNUMMER];
			} else if ("ErsatzartikelHerstellerbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ERSATZARTIKEL_HERSTELLERBEZEICHNUNG];
			} else if ("FreigabePerson".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_FREIGABE_PERSON];
			} else if ("FreigabeZeitpunkt".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_FREIGABE_ZEITPUNKT];
			} else if ("Kundenartikelnummer".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_KUNDENARTIKELNUMMER];
			} else if ("Kundenartikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_KUNDENARTIKELBEZEICHNUNG];
			} else if ("SubreportArtikelkommentar".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_SUBREPORT_ARTIKELKOMMENTAR];
			} else if ("Dimension1".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_DIMENSION1];
			} else if ("Dimension2".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_DIMENSION2];
			} else if ("Dimension3".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_DIMENSION3];
			} else if ("Positionseinheit".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_POSITIONSEINHEIT];
			} else if ("ArtikelFreigabePerson".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKEL_FREIGABE_PERSON];
			} else if ("ArtikelFreigabeZeitpunkt".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKEL_FREIGABE_ZEITPUNKT];
			} else if ("ArtikelMaterial".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKEL_MATERIAL];
			} else if ("Initialkosten".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_INITIALKOSTEN];
			}

		} else if (sAktuellerReport.equals(StuecklisteReportFac.REPORT_STUECKLISTE_WAFFENREGISTER)) {
			if ("Artikel".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_ARTIKEL];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_ARTIKELBEZEICHNUNG];
			} else if ("Artikelzusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_ARTIKELZUSATZBEZEICHNUNG];
			} else if ("Artikelzusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_ARTIKELZUSATZBEZEICHNUNG2];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_STUECKLISTENEINHEIT];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_MENGE];
			} else if ("Formel".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_FORMEL];
			} else if ("Fremdfertigung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_FREMDFERTIGUNG];
			} else if ("Position".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_POSITION];
			} else if ("Preis".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_PREIS];
			} else if ("Dimension".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_DIMENSION];
			} else if ("Zielmenge".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_ZIELMENGE];
			} else if ("Zieleinheit".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_ZIELMENGENEINHEIT];
			} else if ("Montageart".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_MONTAGEART];
			} else if ("Hochstellen".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_HOCHSTELLEN];
			} else if ("Hochsetzen".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_HOCHSETZEN];
			} else if ("F_IMAGE".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_IMAGE];
			} else if ("Kurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_KURZBEZEICHNUNG];
			} else if ("Referenznummer".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_REFERENZNUMMER];
			} else if ("Rasterliegend".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_RASTERLIEGEND];
			} else if ("Rasterstehend".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_RASTERSTEHEND];
			} else if ("Hersteller".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_HERSTELLER];
			} else if ("Herstellername".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_HERSTELLER_NAME];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_KOMMENTAR];
			} else if ("Lfdnummer".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_LFDNUMMER];
			} else if ("Index".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_INDEX];
			} else if ("Revision".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_REVISION];
			} else if ("Warenverkehrsnummer".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_WARENVERKEHRSNUMMER];
			} else if ("UrsprungslandLkz".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_URSPRUNGSLAND_LKZ];
			} else if ("UrsprungslandName".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_URSPRUNGSLAND_NAME];
			} else if ("Ersatzartikel".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_ERSATZARTIKEL];
			} else if ("Ersatzartikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_ERSATZARTIKEL_BEZEICHNUNG];
			} else if ("Ersatzartikelzusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_ERSATZARTIKEL_ZUSATZBEZEICHNUNG];
			} else if ("Ersatzartikelzusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_ERSATZARTIKEL_ZUSATZBEZEICHNUNG2];
			} else if ("Artikelgewicht".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_GEWICHT];
			} else if ("ArtikelHerstellernummer".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_ARTIKEL_HERSTELLERNUMMER];
			} else if ("ArtikelHerstellerbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_ARTIKEL_HERSTELLERBEZEICHNUNG];
			} else if ("ErsatzartikelHerstellernummer".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_ERSATZARTIKEL_HERSTELLERNUMMER];
			} else if ("ErsatzartikelHerstellerbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_ERSATZARTIKEL_HERSTELLERBEZEICHNUNG];
			} else if ("FreigabePerson".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_FREIGABE_PERSON];
			} else if ("FreigabeZeitpunkt".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_FREIGABE_ZEITPUNKT];
			} else if ("Kundenartikelnummer".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_KUNDENARTIKELNUMMER];
			} else if ("Kundenartikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_KUNDENARTIKELBEZEICHNUNG];
			} else if ("SubreportArtikelkommentar".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_SUBREPORT_ARTIKELKOMMENTAR];
			} else if ("Dimension1".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_DIMENSION1];
			} else if ("Dimension2".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_DIMENSION2];
			} else if ("Dimension3".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_DIMENSION3];
			} else if ("Positionseinheit".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_POSITIONSEINHEIT];
			} else if ("ArtikelFreigabePerson".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_ARTIKEL_FREIGABE_PERSON];
			} else if ("ArtikelFreigabeZeitpunkt".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_ARTIKEL_FREIGABE_ZEITPUNKT];
			}

			else if ("WaffenKaliber".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_WAFFENKALIBER];
			} else if ("WaffenAusfuehrung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_WAFFENAUSFUEHRUNG];
			} else if ("WaffenTyp".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_WAFFENTYP];
			} else if ("WaffenTypFein".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_WAFFENTYPFEIN];
			} else if ("WaffenKategorie".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_WAFFENKATEGORIE];
			} else if ("WaffenZusatz".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_WAFFENREGISTER_WAFFENZUSATZ];
			}

		} else if (sAktuellerReport.equals(StuecklisteReportFac.REPORT_STUECKLISTE_VERGLEICH_MIT_ANDERER_STUECKLISTE)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_VGL_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_VGL_BEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_VGL_ZUSATZBEZEICHNUNG];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][REPORT_VGL_EINHEIT];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_VGL_MENGE];
			} else if ("Dimension1".equals(fieldName)) {
				value = data[index][REPORT_VGL_DIMENSION1];
			} else if ("Dimension2".equals(fieldName)) {
				value = data[index][REPORT_VGL_DIMENSION2];
			} else if ("Dimension3".equals(fieldName)) {
				value = data[index][REPORT_VGL_DIMENSION3];
			} else if ("Position".equals(fieldName)) {
				value = data[index][REPORT_VGL_POSITION];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][REPORT_VGL_KOMMENTAR];
			} else if ("Montageart".equals(fieldName)) {
				value = data[index][REPORT_VGL_MONTAGEART];
			} else if ("Lfdnummer".equals(fieldName)) {
				value = data[index][REPORT_VGL_LFDNUMMER];
			} else if ("Mitdrucken".equals(fieldName)) {
				value = data[index][REPORT_VGL_MITDRUCKEN];
			} else if ("Kalkpreis".equals(fieldName)) {
				value = data[index][REPORT_VGL_KALKPREIS];
			} else if ("Beginnterminoffset".equals(fieldName)) {
				value = data[index][REPORT_VGL_BEGINNTERMINOFFSET];
			} else if ("Ruestmenge".equals(fieldName)) {
				value = data[index][REPORT_VGL_RUESTMENGE];
			} else if ("Formel".equals(fieldName)) {
				value = data[index][REPORT_VGL_FORMEL];
			} else if ("Stkl2_Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_VGL_STKL2_ARTIKELNUMMER];
			} else if ("Stkl2_Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_VGL_STKL2_BEZEICHNUNG];
			} else if ("Stkl2_Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_VGL_STKL2_ZUSATZBEZEICHNUNG];
			} else if ("Stkl2_Einheit".equals(fieldName)) {
				value = data[index][REPORT_VGL_STKL2_EINHEIT];
			} else if ("Stkl2_Menge".equals(fieldName)) {
				value = data[index][REPORT_VGL_STKL2_MENGE];
			} else if ("Stkl2_Dimension1".equals(fieldName)) {
				value = data[index][REPORT_VGL_STKL2_DIMENSION1];
			} else if ("Stkl2_Dimension2".equals(fieldName)) {
				value = data[index][REPORT_VGL_STKL2_DIMENSION2];
			} else if ("Stkl2_Dimension3".equals(fieldName)) {
				value = data[index][REPORT_VGL_STKL2_DIMENSION3];
			} else if ("Stkl2_Position".equals(fieldName)) {
				value = data[index][REPORT_VGL_STKL2_POSITION];
			} else if ("Stkl2_Kommentar".equals(fieldName)) {
				value = data[index][REPORT_VGL_STKL2_KOMMENTAR];
			} else if ("Stkl2_Montageart".equals(fieldName)) {
				value = data[index][REPORT_VGL_STKL2_MONTAGEART];
			} else if ("Stkl2_Lfdnummer".equals(fieldName)) {
				value = data[index][REPORT_VGL_STKL2_LFDNUMMER];
			} else if ("Stkl2_Mitdrucken".equals(fieldName)) {
				value = data[index][REPORT_VGL_STKL2_MITDRUCKEN];
			} else if ("Stkl2_Kalkpreis".equals(fieldName)) {
				value = data[index][REPORT_VGL_STKL2_KALKPREIS];
			} else if ("Stkl2_Beginnterminoffset".equals(fieldName)) {
				value = data[index][REPORT_VGL_STKL2_BEGINNTERMINOFFSET];
			} else if ("Stkl2_Ruestmenge".equals(fieldName)) {
				value = data[index][REPORT_VGL_STKL2_RUESTMENGE];
			} else if ("Stkl2_Formel".equals(fieldName)) {
				value = data[index][REPORT_VGL_STKL2_FORMEL];
			} else if ("PositionenUnterschiedlich".equals(fieldName)) {
				value = data[index][REPORT_VGL_POSITIONEN_UNTERSCHIEDLICH];
			} else if ("Arbeitsplan".equals(fieldName)) {
				value = data[index][REPORT_VGL_ARBEITSPLAN];
			}

			else if ("Arbeitsplan_Arbeitsgang".equals(fieldName)) {
				value = data[index][REPORT_VGL_ARBEITSPLAN_ARBEITSGANG];
			} else if ("Arbeitsplan_Unterarbeitsgang".equals(fieldName)) {
				value = data[index][REPORT_VGL_ARBEITSPLAN_UNTERARBEITSGANG];
			} else if ("Arbeitsplan_Stueckzeit".equals(fieldName)) {
				value = data[index][REPORT_VGL_ARBEITSPLAN_STUECKZEIT];
			} else if ("Arbeitsplan_Ruestzeit".equals(fieldName)) {
				value = data[index][REPORT_VGL_ARBEITSPLAN_RUESTZEIT];
			} else if ("Arbeitsplan_Kommentar".equals(fieldName)) {
				value = data[index][REPORT_VGL_ARBEITSPLAN_KOMMENTAR];
			} else if ("Arbeitsplan_Langtext".equals(fieldName)) {
				value = data[index][REPORT_VGL_ARBEITSPLAN_LANGTEXT];
			} else if ("Arbeitsplan_Maschine".equals(fieldName)) {
				value = data[index][REPORT_VGL_ARBEITSPLAN_MASCHINE];
			} else if ("Arbeitsplan_AgArt".equals(fieldName)) {
				value = data[index][REPORT_VGL_ARBEITSPLAN_AGART];
			} else if ("Arbeitsplan_Aufspannung".equals(fieldName)) {
				value = data[index][REPORT_VGL_ARBEITSPLAN_AUFSPANNUNG];
			} else if ("Arbeitsplan_Nurmaschinenzeit".equals(fieldName)) {
				value = data[index][REPORT_VGL_ARBEITSPLAN_NURMASCHINENZEIT];
			} else if ("Arbeitsplan_Maschinenversatztage".equals(fieldName)) {
				value = data[index][REPORT_VGL_ARBEITSPLAN_MASCHINENVERSATZTAGE];
			} else if ("Arbeitsplan_Ppm".equals(fieldName)) {
				value = data[index][REPORT_VGL_ARBEITSPLAN_PPM];
			} else if ("Arbeitsplan_Formel".equals(fieldName)) {
				value = data[index][REPORT_VGL_ARBEITSPLAN_FORMEL];
			} else if ("Arbeitsplan_Apkommentar".equals(fieldName)) {
				value = data[index][REPORT_VGL_ARBEITSPLAN_APKOMMENTAR];
			}

			else if ("Arbeitsplan_Stkl2_Arbeitsgang".equals(fieldName)) {
				value = data[index][REPORT_VGL_ARBEITSPLAN_STKL2_ARBEITSGANG];
			} else if ("Arbeitsplan_Stkl2_Unterarbeitsgang".equals(fieldName)) {
				value = data[index][REPORT_VGL_ARBEITSPLAN_STKL2_UNTERARBEITSGANG];
			} else if ("Arbeitsplan_Stkl2_Stueckzeit".equals(fieldName)) {
				value = data[index][REPORT_VGL_ARBEITSPLAN_STKL2_STUECKZEIT];
			} else if ("Arbeitsplan_Stkl2_Ruestzeit".equals(fieldName)) {
				value = data[index][REPORT_VGL_ARBEITSPLAN_STKL2_RUESTZEIT];
			} else if ("Arbeitsplan_Stkl2_Kommentar".equals(fieldName)) {
				value = data[index][REPORT_VGL_ARBEITSPLAN_STKL2_KOMMENTAR];
			} else if ("Arbeitsplan_Stkl2_Langtext".equals(fieldName)) {
				value = data[index][REPORT_VGL_ARBEITSPLAN_STKL2_LANGTEXT];
			} else if ("Arbeitsplan_Stkl2_Maschine".equals(fieldName)) {
				value = data[index][REPORT_VGL_ARBEITSPLAN_STKL2_MASCHINE];
			} else if ("Arbeitsplan_Stkl2_AgArt".equals(fieldName)) {
				value = data[index][REPORT_VGL_ARBEITSPLAN_STKL2_AGART];
			} else if ("Arbeitsplan_Stkl2_Aufspannung".equals(fieldName)) {
				value = data[index][REPORT_VGL_ARBEITSPLAN_STKL2_AUFSPANNUNG];
			} else if ("Arbeitsplan_Stkl2_Nurmaschinenzeit".equals(fieldName)) {
				value = data[index][REPORT_VGL_ARBEITSPLAN_STKL2_NURMASCHINENZEIT];
			} else if ("Arbeitsplan_Stkl2_Maschinenversatztage".equals(fieldName)) {
				value = data[index][REPORT_VGL_ARBEITSPLAN_STKL2_MASCHINENVERSATZTAGE];
			} else if ("Arbeitsplan_Stkl2_Ppm".equals(fieldName)) {
				value = data[index][REPORT_VGL_ARBEITSPLAN_STKL2_PPM];
			} else if ("Arbeitsplan_Stkl2_Formel".equals(fieldName)) {
				value = data[index][REPORT_VGL_ARBEITSPLAN_STKL2_FORMEL];
			} else if ("Arbeitsplan_Stkl2_Apkommentar".equals(fieldName)) {
				value = data[index][REPORT_VGL_ARBEITSPLAN_STKL2_APKOMMENTAR];
			} else if ("Aenderungsdatum".equals(fieldName)) {
				value = data[index][REPORT_VGL_AENDERUNGSDATUM];
			} else if ("Stkl2_Aenderungsdatum".equals(fieldName)) {
				value = data[index][REPORT_VGL_STKL2_AENDERUNGSDATUM];
			}

		} else if (sAktuellerReport.equals(StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEIN_MITPREIS)) {
			if ("Artikel".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKEL];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKELBEZEICHNUNG];
			} else if ("Artikelzusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKELZUSATZBEZEICHNUNG];
			} else if ("Artikelzusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKELZUSATZBEZEICHNUNG2];
			} else if ("Zieleinheit".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ZIELMENGENEINHEIT];
			} else if ("Zielmenge".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ZIELMENGE];
			} else if ("Wert".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_WERT];
			} else if ("Preis".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_PREIS];
			} else if ("Kundenpreis".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KUNDENPREIS];
			} else if ("F_IMAGE".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_IMAGE];
			} else if ("Kurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KURZBEZEICHNUNG];
			} else if ("Rasterliegend".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_RASTERLIEGEND];
			} else if ("Referenznummer".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_REFERENZNUMMER];
			} else if ("Rasterstehend".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_RASTERSTEHEND];
			} else if ("Hochstellen".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_HOCHSTELLEN];
			} else if ("Hochsetzen".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_HOCHSETZEN];
			} else if ("Hersteller".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_HERSTELLER];
			} else if ("Herstellername".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_HERSTELLER_NAME];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KOMMENTAR];
			} else if ("Montageart".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_MONTAGEART];
			} else if ("Position".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_POSITION];
			} else if ("Lfdnummer".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_LFDNUMMER];
			} else if ("Index".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_INDEX];
			} else if ("Revision".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_REVISION];
			} else if ("Warenverkehrsnummer".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_WARENVERKEHRSNUMMER];
			} else if ("UrsprungslandLkz".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_URSPRUNGSLAND_LKZ];
			} else if ("UrsprungslandName".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_URSPRUNGSLAND_NAME];
			} else if ("Ersatzartikel".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ERSATZARTIKEL];
			} else if ("Ersatzartikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ERSATZARTIKEL_BEZEICHNUNG];
			} else if ("Ersatzartikelzusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ERSATZARTIKEL_ZUSATZBEZEICHNUNG];
			} else if ("Ersatzartikelzusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ERSATZARTIKEL_ZUSATZBEZEICHNUNG2];
			} else if ("Artikelgewicht".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_GEWICHT];
			} else if ("Materialzuschlag".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_MATERIALZUSCHLAG];
			} else if ("AufBelegMitdrucken".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_AUF_BELEG_MITDRUCKEN];
			} else if ("KalkPreis".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KALK_PREIS];
			} else if ("KommentarPosition".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KOMMENTAR_POSITION];
			} else if ("SubreportArtikelkommentar".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_SUBREPORT_ARTIKELKOMMENTAR];
			} else if ("ArtikelHerstellernummer".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKEL_HERSTELLERNUMMER];
			} else if ("ArtikelHerstellerbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKEL_HERSTELLERBEZEICHNUNG];
			} else if ("ErsatzartikelHerstellernummer".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ERSATZARTIKEL_HERSTELLERNUMMER];
			} else if ("ErsatzartikelHerstellerbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ERSATZARTIKEL_HERSTELLERBEZEICHNUNG];
			} else if ("FreigabePerson".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_FREIGABE_PERSON];
			} else if ("FreigabeZeitpunkt".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_FREIGABE_ZEITPUNKT];
			} else if ("Kundenartikelnummer".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KUNDENARTIKELNUMMER];
			} else if ("Kundenartikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KUNDENARTIKELBEZEICHNUNG];
			} else if ("Fremdfertigung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_FREMDFERTIGUNG];
			} else if ("ArtikelFreigabePerson".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKEL_FREIGABE_PERSON];
			} else if ("ArtikelFreigabeZeitpunkt".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKEL_FREIGABE_ZEITPUNKT];
			} else if ("Bewilligungspflichtig".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_BEWILLIGUNGSPFLICHTIG];
			} else if ("Meldepflichtig".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_MELDEPFLICHTIG];
			} else if ("ArtikelMaterial".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKEL_MATERIAL];
			} else if ("PreisLetzteWEP".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_PREIS_LETZTE_WEP];
			} else if ("Initialkosten".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_INITIALKOSTEN];
			}

		} else if (sAktuellerReport.equals(StuecklisteReportFac.REPORT_STUECKLISTE_ARBEITSPLAN)) {
			if ("Artikel".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_ARTIKEL];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_ARTIKELBEZEICHNUNG];
			} else if ("Artikelzusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_ARTIKELZUSATZBEZEICHNUNG];
			} else if ("Artikelzusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_ARTIKELZUSATZBEZEICHNUNG2];
			} else if ("Arbeitsgang".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_ARBEITSGANG];
			} else if ("NurMaschinenzeit".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_NUR_MASCHINENZEIT];
			} else if ("Initialkosten".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_INITIALKOSTEN];
			} else if ("Stueckzeit".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_STUECKZEIT];
			} else if ("Ruestzeit".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_RUESTZEIT];
			} else if ("Gesamtzeit".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_GESAMTZEIT];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_KOMMENTAR];
			} else if ("Preis".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_PREIS];
			} else if ("Agart".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_AGART];
			} else if ("Aufspannung".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_AUFSPANNUNG];
			} else if ("Unterarbeitsgang".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_UNTERARBEITSGANG];
			} else if ("Maschine".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_MASCHINE];
			} else if ("Maschinenkosten".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_KOSTEN_MASCHINE];
			} else if ("FremdmaterialArtikel".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_FREMDMATERIAL_ARTIKEL];
			} else if ("FremdmaterialArtikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_FREMDMATERIAL_ARTIKELBEZEICHNUNG];
			} else if ("FremdmaterialSollmenge".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_FREMDMATERIAL_SOLLMENGE];
			} else if ("FremdmaterialArtikelkurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_FREMDMATERIAL_ARTIKELKURZBEZEICHNUNG];
			} else if ("FremdmaterialArtikelzusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_FREMDMATERIAL_ARTIKELZUSATZBEZEICHNUNG];
			} else if ("FremdmaterialArtikelzusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_FREMDMATERIAL_ARTIKELZUSATZBEZEICHNUNG2];
			} else if ("Revision".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_REVISION];
			} else if ("Index".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_INDEX];
			} else if ("Langtext".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_LANGTEXT];
			} else if ("SubreportAlternativeMaschinen".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_SUBREPORT_ALTERNATIVE_MASCHINEN];
			}
		} else if (sAktuellerReport.equals(StuecklisteReportFac.REPORT_STUECKLISTE_AUSGABESTUECKLSITE)) {
			if ("Artikel".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ARTIKEL];
			} else if ("Lagerort".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_LAGERORT];
			} else if ("F_ARTIKELBILD".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_F_ARTIKELBILD];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_BEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ZUSATZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ZUSATZBEZEICHNUNG2];
			} else if ("Kurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_KURZBEZEICHNUNG];
			} else if ("Montageart".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_MONTAGEART];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ZIELMENGE];
			} else if ("Mengeneinheit".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ZIELMENGENEINHEIT];
			} else if ("Lagerstand".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_LAGERSTAND];
			} else if ("LagerstandZiellager".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_LAGERSTAND_ZIELLAGER];
			} else if ("Verfuegbar".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_VERFUEGBAR];
			} else if ("Bestellt".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_BESTELLT];
			} else if ("Referenznummer".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_REFERENZNUMMER];
			} else if ("Revision".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_REVISION];
			} else if ("Index".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_INDEX];
			} else if ("Ersatzartikel".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ERSATZARTIKEL];
			} else if ("Ersatzartikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ERSATZARTIKEL_BEZEICHNUNG];
			} else if ("InFertigung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_IN_FERTIGUNG];
			} else if ("ArtikelHerstellernummer".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ARTIKEL_HERSTELLERNUMMER];
			} else if ("ArtikelHerstellerbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ARTIKEL_HERSTELLERBEZEICHNUNG];
			} else if ("ErsatzartikelHerstellernummer".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ERSATZARTIKEL_HERSTELLERNUMMER];
			} else if ("ErsatzartikelHerstellerbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ERSATZARTIKEL_HERSTELLERBEZEICHNUNG];
			} else if ("FreigabePerson".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_FREIGABE_PERSON];
			} else if ("FreigabeZeitpunkt".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_FREIGABE_ZEITPUNKT];
			} else if ("Kundenartikelnummer".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_KUNDENARTIKELNUMMER];
			} else if ("Kundenartikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_KUNDENARTIKELBEZEICHNUNG];
			} else if ("SubreportArtikelkommentar".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_SUBREPORT_ARTIKELKOMMENTAR];
			} else if ("Fremdfertigung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_FREMDFERTIGUNG];
			} else if ("ArtikelFreigabePerson".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ARTIKEL_FREIGABE_PERSON];
			} else if ("ArtikelFreigabeZeitpunkt".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ARTIKEL_FREIGABE_ZEITPUNKT];
			} else if ("ArtikelAnzahlAngeboten".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ARTIKEL_ANZAHL_ANGEBOTEN];
			} else if ("ArtikelAnzahlAngefragt".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ARTIKEL_ANZAHL_ANGEFRAGT];
			} else if ("SubreportGeplanteLiefertermine".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_SUBREPORT_GEPLANTE_LIEFERTERMINE];
			}

		} else if (sAktuellerReport.equals(StuecklisteReportFac.REPORT_STUECKLISTE_MINDERVERFUEGBARKEIT)) {
			if ("Artikel".equals(fieldName)) {
				value = data[index][REPORT_MINDERVERFUEGBARKEIT_ARTIKEL];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_MINDERVERFUEGBARKEIT_BEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_MINDERVERFUEGBARKEIT_ZUSATZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_MINDERVERFUEGBARKEIT_ZUSATZBEZEICHNUNG2];
			} else if ("Kurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_MINDERVERFUEGBARKEIT_KURZBEZEICHNUNG];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_MINDERVERFUEGBARKEIT_MENGE];
			} else if ("Mengeneinheit".equals(fieldName)) {
				value = data[index][REPORT_MINDERVERFUEGBARKEIT_EINHEIT];
			} else if ("Lagerstand".equals(fieldName)) {
				value = data[index][REPORT_MINDERVERFUEGBARKEIT_LAGERSTAND];
			} else if ("Verfuegbar".equals(fieldName)) {
				value = data[index][REPORT_MINDERVERFUEGBARKEIT_VERFUEGBAR];
			} else if ("Bestellt".equals(fieldName)) {
				value = data[index][REPORT_MINDERVERFUEGBARKEIT_BESTELLT];
			} else if ("Referenznummer".equals(fieldName)) {
				value = data[index][REPORT_MINDERVERFUEGBARKEIT_REFERENZNUMMER];
			} else if ("Revision".equals(fieldName)) {
				value = data[index][REPORT_MINDERVERFUEGBARKEIT_REVISION];
			} else if ("Index".equals(fieldName)) {
				value = data[index][REPORT_MINDERVERFUEGBARKEIT_INDEX];
			} else if ("Ersatzartikel".equals(fieldName)) {
				value = data[index][REPORT_MINDERVERFUEGBARKEIT_ERSATZARTIKEL];
			} else if ("Ersatzartikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_MINDERVERFUEGBARKEIT_ERSATZARTIKEL_BEZEICHNUNG];
			} else if ("Rahmenbestellt".equals(fieldName)) {
				value = data[index][REPORT_MINDERVERFUEGBARKEIT_RAHMENBESTELLT];
			} else if ("SubreportOffeneBestellungen".equals(fieldName)) {
				value = data[index][REPORT_MINDERVERFUEGBARKEIT_SUBREPORT_OFFENE_BESTELLUNGEN];
			}

		} else if (sAktuellerReport.equals(StuecklisteReportFac.REPORT_STUECKLISTE_GESAMTKALKULATION)) {
			if ("Artikel".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_ARTIKEL];
			} else if ("Artikelreferenznummer".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_ARTIKELREFERENZNUMMER];
			} else if ("Artikelkurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_ARTIKELKURZBEZEICHNUNG];
			} else if ("Artikelart".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_ARTIKELART];
			} else if ("Artikelgruppe".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_ARTIKELGRUPPE];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_ARTIKELBEZEICHNUNG];
			} else if ("Artikelzusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_ARTIKELZUSATZBEZEICHNUNG];
			} else if ("Artikelzusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_ARTIKELZUSATZBEZEICHNUNG2];
			} else if ("Gestpreis".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_GESTPREIS];
			} else if ("Gestwert".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_GESTWERT];
			} else if ("Lief1preis".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_LIEF1PREIS];
			} else if ("Lief1preisGueltigab".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_LIEF1PREIS_GUELTIGAB];
			} else if ("Lief1Name".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_LIEF1_NAME];
			} else if ("Lief1Kbez".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_LIEF1_KBEZ];
			} else if ("VKPreisbasisHeute".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_VK_PREISBASIS_HEUTE];
			} else if ("KleinsterLief1preis2Jahre".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_KLEINSTER_LIEF1PREIS_2JAHRE];
			} else if ("GroessterLief1preis2Jahre".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_GROESSTER_LIEF1PREIS_2JAHRE];
			} else if ("Fixkosten".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_FIXKOSTEN];
			} else if ("Liefwert".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_LIEF1PREISGESAMT];
			} else if ("Wiederbeschaffungszeit".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_WIEDERBESCHAFFUNGSZEIT];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_MENGE];
			} else if ("MengeOhneVpe".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_MENGE_OHNE_VPE];
			} else if ("Hierarchiemenge".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_HIERARCHIEMENGE];
			} else if ("Mengeneinheit".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_MENGENEINHEIT];
			} else if ("Stueckzeit".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_STUECKZEIT];
			} else if ("Ruestzeit".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_RUESTZEIT];
			} else if ("ExternerArbeitsgang".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_EXTERNER_ARBEITSGANG];
			} else if ("Gestpreiswennstueckliste".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_GESTPREISWENNSTUECKLISTE];
			} else if ("Summegestpreispositionen".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_SUMMEGESTPREISPOSITIONENWENNSTUECKLISTE];
			} else if ("Summelief1preispositionen".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_SUMMELIEF1PREISPOSITIONENWENNSTUECKLISTE];
//				myLogger.warn("VKPreis:" + data[index][REPORT_GESAMTKALKULATION_VKPREIS] + 
//						", Kalkwert:" + data[index][REPORT_GESAMTKALKULATION_KALKWERT] + 
//						", GestPreis:" + data[index][REPORT_GESAMTKALKULATION_GESTPREIS] +
//						", GestPreisStkl:" + data[index][REPORT_GESAMTKALKULATION_GESTPREISWENNSTUECKLISTE] +
//						", GestWert:" + data[index][REPORT_GESAMTKALKULATION_GESTWERT] +
//						", Lief1Preis:" + data[index][REPORT_GESAMTKALKULATION_LIEF1PREIS] +
//						", Lief1Gesamt:" + data[index][REPORT_GESAMTKALKULATION_LIEF1PREISGESAMT] +
//						", SummeGestPreisPosStkl:" + data[index][REPORT_GESAMTKALKULATION_SUMMEGESTPREISPOSITIONENWENNSTUECKLISTE] +
//						", SummeLief1PreisPos:" + data[index][REPORT_GESAMTKALKULATION_SUMMELIEF1PREISPOSITIONENWENNSTUECKLISTE] +
//						", SummeLief1Material:" + data[index][REPORT_GESAMTKALKULATION_SUMMELIEF1PREISPOSITIONENWENNSTUECKLISTE_MATERIAL] +
//						", SummeLief1Arbeitszeit:" + data[index][REPORT_GESAMTKALKULATION_SUMMELIEF1PREISPOSITIONENWENNSTUECKLISTE_ARBEITSZEIT] +
//						", Fixkosten:" + data[index][REPORT_GESAMTKALKULATION_FIXKOSTEN]);
//				myLogger.warn("Summelief1preisposition " + index + 
//						" [" + data[index][0] + "] = " + value);
			} else if ("Summelief1preispositionenArbeitszeit".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_SUMMELIEF1PREISPOSITIONENWENNSTUECKLISTE_ARBEITSZEIT];
			} else if ("Summelief1preispositionenMaterial".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_SUMMELIEF1PREISPOSITIONENWENNSTUECKLISTE_MATERIAL];
			} else if ("Stuecklistenart".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_STKLART];
			} else if ("Fremdgefertigt".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_FREMDGEFERTIGT];
			} else if ("DruckeInLagerstandsdetail".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_LAGERSTANDSDETAIL];
			} else if ("Ruestmenge".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_RUESTMENGE];
			} else if ("Initialkosten".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_INITITALKOSTEN];
			} else if ("Gestpreiseweichenab".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_GESTPREISEWEICHENAB];
			} else if ("Durchlaufzeit".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_DURCHLAUFZEIT];
			} else if ("Kalkpreis".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_KALKPREIS];
			} else if ("Kalkwert".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_KALKWERT];
			} else if ("Vkpreis".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_VKPREIS];
			} else if ("Materialzuschlag".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_MATERIALZUSCHLAG];
			} else if ("MaterialzuschlagEK".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_MATERIALZUSCHLAG_EK];
			} else if ("Vkwert".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_VKWERT];
			} else if ("Mindestdeckungsbeitrag".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_MINDESTDECKUNGSBEITRAG];
			} else if ("Artikelgewicht".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_GEWICHT];
			} else if ("Reihenfolge".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_REIHENFOLGE];
			} else if ("ArtikelMaterialgewicht".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_MATERIALGEWICHT];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_KOMMENTAR];
			} else if ("AG".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_AG];
			} else if ("UAG".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_UAG];
			} else if ("FreigabeZeitpunkt".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_FREIGABE_ZEITPUNKT];
			} else if ("FreigabePerson".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_FREIGABE_PERSON];
			} else if ("AnzahlArbeitsschritte".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_ANZAHL_ARBEITSSCHRITTE];
			} else if ("Mandant".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_MANDANT];
			} else if ("MandantKbez".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_MANDANT_KBEZ];
			} else if ("Fertigungssatzgroesse".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_FERTIGUNGSSATZGROESSE];
			} else if ("Mindestbestellmenge".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_MINDESTBESTELLMENGE];
			} else if ("Formel".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_FORMEL];
			} else if ("Positionsmenge".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_POS_MENGE];
			} else if ("Positionsmengeneinheit".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_POS_MENGENEINHEIT];
			} else if ("Dimension1".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_DIMENSION1];
			} else if ("Dimension2".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_DIMENSION2];
			} else if ("Dimension3".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_DIMENSION3];
			} else if ("ReportDebug".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_LOG_DEBUG];
			} else if ("ReportInfo".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_LOG_INFO];
			} else if ("ReportWarn".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_LOG_WARN];
			} else if ("ReportError".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_LOG_ERROR];
			} else if ("Meldepflichtig".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_MELDEPFLICHTIG];
			} else if ("Bewilligungspflichtig".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_BEWILLIGUNGSPFLICHTIG];
			} else if ("UrsprungslandLkz".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_URSPRUNGSLAND_LKZ];
			} else if ("UrsprungslandName".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_URSPRUNGSLAND_NAME];
			} else if ("Warenverkehrsnummer".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_WARENVERKEHRSNUMMER];
			}
		} else if (sAktuellerReport.equals(StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT)) {
			if ("Artikel".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_LOSEAKTUALISIERT_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_LOSEAKTUALISIERT_BEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_LOSEAKTUALISIERT_ZUSATZBEZEICHNUNG];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_LOSEAKTUALISIERT_EINHEIT];
			} else if ("KorrekturAusgabemenge".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_AUSGABEMENGE];
			} else if ("KorrekturSollmenge".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_SOLLMENGE];
			} else if ("Bemerkung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_LOSEAKTUALISIERT_BEMERKUNG];
			} else if ("Los".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_LOSEAKTUALISIERT_LOSNUMMER];
			}
		} else if (sAktuellerReport.equals(StuecklisteReportFac.REPORT_STUECKLISTE_VERSCHLEISSTEILVERWENDUNG)) {
			if ("Stueckliste".equals(fieldName)) {
				value = data[index][REPORT_VERSCHLEISSTEILVERWENDUNG_STUECKLISTE];
			} else if ("StuecklisteBezeichnung".equals(fieldName)) {
				value = data[index][REPORT_VERSCHLEISSTEILVERWENDUNG_STUECKLISTE_BEZEICHNUNG];
			} else if ("Verschleissteil".equals(fieldName)) {
				value = data[index][REPORT_VERSCHLEISSTEILVERWENDUNG_VERSCHLEISSTEIL];
			} else if ("VerschleissteilBezeichnung".equals(fieldName)) {
				value = data[index][REPORT_VERSCHLEISSTEILVERWENDUNG_VERSCHLEISSTEIL_BEZEICHNUNG];
			} else if ("Fehlmengen".equals(fieldName)) {
				value = data[index][REPORT_VERSCHLEISSTEILVERWENDUNG_FEHLMENGEN];
			} else if ("Reserviert".equals(fieldName)) {
				value = data[index][REPORT_VERSCHLEISSTEILVERWENDUNG_RESERVIERT];
			} else if ("InFertigung".equals(fieldName)) {
				value = data[index][REPORT_VERSCHLEISSTEILVERWENDUNG_IN_FERTIGUNG];
			} else if ("Freigabezeitpunkt".equals(fieldName)) {
				value = data[index][REPORT_VERSCHLEISSTEILVERWENDUNG_FREIGABEZEITPUNKT];
			} else if ("PersonFreigabe".equals(fieldName)) {
				value = data[index][REPORT_VERSCHLEISSTEILVERWENDUNG_PERSON_FREIGABE];
			}

		} else if (sAktuellerReport.equals(StuecklisteReportFac.REPORT_STUECKLISTE_PRUEFPLAN)) {
			if ("ArtikelKontakt".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_ARTIKEL_KONTAKT];
			} else if ("ArtikelbezeichnungKontakt".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_ARTIKELBEZEICHNUNG_KONTAKT];
			} else if ("ArtikelLitze".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_ARTIKEL_LITZE];
			} else if ("ArtikelbezeichnungLitze".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_ARTIKELBEZEICHNUNG_LITZE];
			} else if ("PositionKontakt".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_POSITION_KONTAKT];
			} else if ("PositionLitze".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_POSITION_LITZE];
			} else if ("Verschleissteilnummer".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_VERSCHLEISSTEILNUMMER];
			} else if ("Verschleissteilbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_VERSCHLEISSTEILBEZEICHNUNG];
			} else if ("CrimpbreiteDraht".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_CRIMPBREITE_DRAHT];
			} else if ("CrimpbreiteIsolation".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_CRIMPBREITE_ISOLATION];
			} else if ("CrimphoeheDraht".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_CRIMPHOEHE_DRAHT];
			} else if ("CrimphoeheIsolation".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_CRIMPHOEHE_ISOLATION];
			}

			else if ("ToleranzCrimpbreiteDraht".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_TOLERANZ_CRIMPBREITE_DRAHT];
			} else if ("ToleranzCrimpbreiteIsolation".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_TOLERANZ_CRIMPBREITE_ISOLATION];
			} else if ("ToleranzCrimphoeheDraht".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_TOLERANZ_CRIMPHOEHE_DRAHT];
			} else if ("ToleranzCrimphoeheIsolation".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_TOLERANZ_CRIMPHOEHE_ISOLATION];
			} else if ("ToleranzWert".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_TOLERANZ_WERT];
			} else if ("Wert".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_WERT];
			} else if ("Pruefart".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_PRUEFART];
			} else if ("PruefartBezeichnung".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_PRUEFART_BEZEICHNUNG];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_KOMMENTAR];
			} else if ("MengeLitze".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_MENGE_LITZE];
			} else if ("EinheitLitze".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_EINHEIT_LITZE];
			} else if ("Dimension1Litze".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_DIMENSION1_LITZE];
			} else if ("Dimension2Litze".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_DIMENSION2_LITZE];
			} else if ("Dimension3Litze".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_DIMENSION3_LITZE];
			} else if ("ArtikelLitze2".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_ARTIKEL_LITZE2];
			} else if ("ArtikelbezeichnungLitze2".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_ARTIKELBEZEICHNUNG_LITZE2];
			} else if ("MengeLitze2".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_MENGE_LITZE2];
			} else if ("EinheitLitze2".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_EINHEIT_LITZE2];
			} else if ("Dimension1Litze2".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_DIMENSION1_LITZE2];
			} else if ("Dimension2Litze2".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_DIMENSION2_LITZE2];
			} else if ("Dimension3Litze2".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_DIMENSION3_LITZE2];
			} else if ("PositionLitze2".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_POSITION_LITZE2];
			} else if ("AbzugskraftLitze".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_ABZUGSKAFT_LITZE];
			} else if ("AbzugskraftLitze2".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_ABZUGSKAFT_LITZE2];
			} else if ("Doppelanschlag".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_DOPPELANSCHLAG];
			} else if ("SubreportWerkzeuge".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_SUBREPORT_WERKZEUGE];
			}

			else if ("StuecklisteNummer".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_STUECKLISTE_NUMMER];
			} else if ("StuecklisteBezeichnung".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_STUECKLISTE_BEZEICHNUNG];
			} else if ("StuecklisteKurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_STUECKLISTE_KURZBEZEICHNUNG];
			} else if ("StuecklisteZusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_STUECKLISTE_ZUSATZBEZEICHNUNG];
			} else if ("StuecklisteZusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_STUECKLISTE_ZUSATZBEZEICHNUNG2];
			} else if ("StuecklisteKommentar".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_STUECKLISTE_KOMMENTAR];
			} else if ("StuecklisteSubreportZeichnungsnummern".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_STUECKLISTE_SUBREPORT_ZEICHNUNGSNUMMMERN];
			} else if ("StuecklisteKunde".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_STUECKLISTE_KUNDE];
			} else if ("StuecklisteErfassungsfaktor".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_STUECKLISTE_ERFASSUNGSFAKTOR];
			} else if ("StuecklisteAnlegen".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_STUECKLISTE_ANLEGEN];
			} else if ("StuecklisteAendern".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_STUECKLISTE_AENDERN];
			} else if ("StuecklisteAendernPosition".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_STUECKLISTE_AENDERN_POSITION];
			} else if ("StuecklisteAendernArbeitsplan".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_STUECKLISTE_AENDERN_ARBEITSPLAN];
			} else if ("StuecklisteFreigabeZeitpunkt".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_STUECKLISTE_FREIGABE_ZEITPUNKT];
			} else if ("StuecklisteFreigabePerson".equals(fieldName)) {
				value = data[index][REPORT_PRUEFPLAN_STUECKLISTE_FREIGABE_PERSON];
			}

		} else if (sAktuellerReport.equals(StuecklisteReportFac.REPORT_STUECKLISTE_FREIGABE)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_FREIGABE_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_FREIGABE_BEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_FREIGABE_ZUSATZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_FREIGABE_ZUSATZBEZEICHNUNG2];
			} else if ("FreigegebenAm".equals(fieldName)) {
				value = data[index][REPORT_FREIGABE_FREIGABE_AM];
			} else if ("FreigegebenVon".equals(fieldName)) {
				value = data[index][REPORT_FREIGABE_FREIGABE_VON];
			} else if ("Freigegeben".equals(fieldName)) {
				value = data[index][REPORT_FREIGABE_FREIGEGEBEN];
			} else if ("Lfdnr".equals(fieldName)) {
				value = data[index][REPORT_FREIGABE_LFDNR];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_FREIGABE_MENGE];
			} else if ("Position".equals(fieldName)) {
				value = data[index][REPORT_FREIGABE_POSITION];
			} else if ("STUECKLISTEPOSITION_I_ID".equals(fieldName)) {
				value = data[index][REPORT_FREIGABE_STUECKLISTEPOSITION_I_ID];
			} else if ("SubreportErsatztypenArtikel".equals(fieldName)) {
				value = data[index][REPORT_FREIGABE_SUBREPORT_ERSATZTYPEN_ARTIKEL];
			} else if ("SubreportErsatztypenStkl".equals(fieldName)) {
				value = data[index][REPORT_FREIGABE_SUBREPORT_ERSATZTYPEN_STKL];
			}
		} else if (sAktuellerReport.equals(StuecklisteReportFac.REPORT_STUECKLISTE_PRUEFKOMBINATION)) {
			if ("ArtikelKontakt".equals(fieldName)) {
				value = data[index][REPORT_PRUEFKOMBINATION_ARTIKEL_KONTAKT];
			} else if ("ArtikelbezeichnungKontakt".equals(fieldName)) {
				value = data[index][REPORT_PRUEFKOMBINATION_ARTIKELBEZEICHNUNG_KONTAKT];
			} else if ("ArtikelLitze".equals(fieldName)) {
				value = data[index][REPORT_PRUEFKOMBINATION_ARTIKEL_LITZE];
			} else if ("ArtikelbezeichnungLitze".equals(fieldName)) {
				value = data[index][REPORT_PRUEFKOMBINATION_ARTIKELBEZEICHNUNG_LITZE];
			} else if ("ArtikelLitze2".equals(fieldName)) {
				value = data[index][REPORT_PRUEFKOMBINATION_ARTIKEL_LITZE2];
			} else if ("ArtikelbezeichnungLitze2".equals(fieldName)) {
				value = data[index][REPORT_PRUEFKOMBINATION_ARTIKELBEZEICHNUNG_LITZE2];
			} else if ("Verschleissteilnummer".equals(fieldName)) {
				value = data[index][REPORT_PRUEFKOMBINATION_VERSCHLEISSTEILNUMMER];
			} else if ("Verschleissteilbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_PRUEFKOMBINATION_VERSCHLEISSTEILBEZEICHNUNG];
			} else if ("CrimpbreiteDraht".equals(fieldName)) {
				value = data[index][REPORT_PRUEFKOMBINATION_CRIMPBREITE_DRAHT];
			} else if ("CrimpbreiteIsolation".equals(fieldName)) {
				value = data[index][REPORT_PRUEFKOMBINATION_CRIMPBREITE_ISOLATION];
			} else if ("CrimphoeheDraht".equals(fieldName)) {
				value = data[index][REPORT_PRUEFKOMBINATION_CRIMPHOEHE_DRAHT];
			} else if ("CrimphoeheIsolation".equals(fieldName)) {
				value = data[index][REPORT_PRUEFKOMBINATION_CRIMPHOEHE_ISOLATION];
			} else if ("Standard".equals(fieldName)) {
				value = data[index][REPORT_PRUEFKOMBINATION_STANDARD];
			} else if ("ToleranzCrimpbreiteDraht".equals(fieldName)) {
				value = data[index][REPORT_PRUEFKOMBINATION_TOLERANZ_CRIMPBREITE_DRAHT];
			} else if ("ToleranzCrimpbreiteIsolation".equals(fieldName)) {
				value = data[index][REPORT_PRUEFKOMBINATION_TOLERANZ_CRIMPBREITE_ISOLATION];
			} else if ("ToleranzCrimphoeheDraht".equals(fieldName)) {
				value = data[index][REPORT_PRUEFKOMBINATION_TOLERANZ_CRIMPHOEHE_DRAHT];
			} else if ("ToleranzCrimphoeheIsolation".equals(fieldName)) {
				value = data[index][REPORT_PRUEFKOMBINATION_TOLERANZ_CRIMPHOEHE_ISOLATION];
			} else if ("ToleranzWert".equals(fieldName)) {
				value = data[index][REPORT_PRUEFKOMBINATION_TOLERANZ_WERT];
			} else if ("Wert".equals(fieldName)) {
				value = data[index][REPORT_PRUEFKOMBINATION_WERT];
			} else if ("Pruefart".equals(fieldName)) {
				value = data[index][REPORT_PRUEFKOMBINATION_PRUEFART];
			} else if ("PruefartBezeichnung".equals(fieldName)) {
				value = data[index][REPORT_PRUEFKOMBINATION_PRUEFART_BEZEICHNUNG];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][REPORT_PRUEFKOMBINATION_KOMMENTAR];
			} else if ("Doppelanschlag".equals(fieldName)) {
				value = data[index][REPORT_PRUEFKOMBINATION_DOPPELANSCHLAG];
			} else if ("Abzugskraft1".equals(fieldName)) {
				value = data[index][REPORT_PRUEFKOMBINATION_ABZUGSKRAFT1];
			} else if ("Abzugskraft2".equals(fieldName)) {
				value = data[index][REPORT_PRUEFKOMBINATION_ABZUGSKRAFT2];
			}

		} else if (sAktuellerReport.equals(StuecklisteReportFac.REPORT_STUECKLISTE_REICHWEITE)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_REICHWEITE_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_REICHWEITE_BEZEICHNUNG];
			} else if ("Ebene".equals(fieldName)) {
				value = data[index][REPORT_REICHWEITE_EBENE];
			} else if ("Jahresmenge".equals(fieldName)) {
				value = data[index][REPORT_REICHWEITE_JAHRESMENGE];
			} else if ("Lagerstand".equals(fieldName)) {
				value = data[index][REPORT_REICHWEITE_LAGERSTAND];
			} else if ("Bestellt".equals(fieldName)) {
				value = data[index][REPORT_REICHWEITE_BESTELLT];
			} else if ("Rahmenbestellt".equals(fieldName)) {
				value = data[index][REPORT_REICHWEITE_RAHMENBESTELLT];
			} else if ("NichtLagerbewirtschaftet".equals(fieldName)) {
				value = data[index][REPORT_REICHWEITE_NICHT_LAGERBEWIRTSCHAFTET];
			} else if ("Lagersollstand".equals(fieldName)) {
				value = data[index][REPORT_REICHWEITE_LAGERSOLLSTAND];
			} else if ("Lagermindeststand".equals(fieldName)) {
				value = data[index][REPORT_REICHWEITE_LAGERMINDESTSTAND];
			} else if ("Stuecklistenart".equals(fieldName)) {
				value = data[index][REPORT_REICHWEITE_STUECKLISTENART];
			} else if ("StuecklisteDurchlaufzeit".equals(fieldName)) {
				value = data[index][REPORT_REICHWEITE_STUECKLISTE_DURCHLAUFZEIT];
			} else if ("Wiederbeschaffungsmoral".equals(fieldName)) {
				value = data[index][REPORT_REICHWEITE_WIEDERBESCHAFFUNGSMORAL];
			} else if ("Wiederbeschaffungszeit".equals(fieldName)) {
				value = data[index][REPORT_REICHWEITE_WIEDERBESCHAFFUNGSZEIT];
			} else if ("Artikellieferant".equals(fieldName)) {
				value = data[index][REPORT_REICHWEITE_ARTIKELLIEFERANT];
			}
		}
		return value;
	}

	/**
	 * Hole Stuecklisteneigenschaften fuer einen Artikel. Keys in Hashtable sind
	 * StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_INDEX und
	 * StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_MATERIALPLATZ.
	 * 
	 * @param iIdStklIDI   Integer StuecklisteId
	 * @param sMandantCNr  String
	 * @param theClientDto der aktuelle Benutzer
	 * @return Hashtable
	 * @throws EJBExceptionLP
	 */
	public Hashtable getStuecklisteEigenschaften(Integer iIdStklIDI, String sMandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Hashtable<String, String> daten = new Hashtable<String, String>();
		;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			Criteria crit = session.createCriteria(FLRStuecklisteeigenschaft.class);
			Criteria cStueckliste = crit.createCriteria("flrstueckliste");
			Criteria cArt = crit.createCriteria("flrstuecklisteeigenschaftart");

			cStueckliste.add(Restrictions.eq("i_id", iIdStklIDI));
			cStueckliste.add(Restrictions.eq("mandant_c_nr", sMandantCNr));

			List<?> resultList = crit.list();
			Iterator<?> it = resultList.iterator();
			while (it.hasNext()) {
				FLRStuecklisteeigenschaft flr = (FLRStuecklisteeigenschaft) it.next();
				if (flr.getFlrstuecklisteeigenschaftart().getC_bez()
						.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_INDEX)) {
					daten.put(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_INDEX, flr.getC_bez());
				}
				if (flr.getFlrstuecklisteeigenschaftart().getC_bez()
						.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_MATERIALPLATZ)) {
					daten.put(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_MATERIALPLATZ, flr.getC_bez());
				}
			}

		} finally {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE, he);
			}
		}
		if (daten != null && daten.size() == 0) {
			return null;
		}
		return daten;

	}

}
