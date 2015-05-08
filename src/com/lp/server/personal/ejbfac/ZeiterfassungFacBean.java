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
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.BelegInfos;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.AuftragzeitenDto;
import com.lp.server.benutzer.service.BenutzerFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.fertigung.ejb.Losgutschlecht;
import com.lp.server.fertigung.ejb.Lossollarbeitsplan;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosgutschlechtDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.ejb.Bereitschaft;
import com.lp.server.personal.ejb.Bereitschafttag;
import com.lp.server.personal.ejb.Diaeten;
import com.lp.server.personal.ejb.Diaetentagessatz;
import com.lp.server.personal.ejb.Gleitzeitsaldo;
import com.lp.server.personal.ejb.Maschine;
import com.lp.server.personal.ejb.Maschinengruppe;
import com.lp.server.personal.ejb.Maschinenkosten;
import com.lp.server.personal.ejb.Maschinenzeitdaten;
import com.lp.server.personal.ejb.Personal;
import com.lp.server.personal.ejb.Personalzeitmodell;
import com.lp.server.personal.ejb.Reise;
import com.lp.server.personal.ejb.ReisekostenDiaetenScript;
import com.lp.server.personal.ejb.Reiselog;
import com.lp.server.personal.ejb.Schichtzeitmodell;
import com.lp.server.personal.ejb.Sonderzeiten;
import com.lp.server.personal.ejb.Taetigkeit;
import com.lp.server.personal.ejb.Taetigkeitart;
import com.lp.server.personal.ejb.Taetigkeitartspr;
import com.lp.server.personal.ejb.TaetigkeitartsprPK;
import com.lp.server.personal.ejb.Taetigkeitspr;
import com.lp.server.personal.ejb.TaetigkeitsprPK;
import com.lp.server.personal.ejb.Tagesart;
import com.lp.server.personal.ejb.Tagesartspr;
import com.lp.server.personal.ejb.TagesartsprPK;
import com.lp.server.personal.ejb.Telefonzeiten;
import com.lp.server.personal.ejb.Zeitdaten;
import com.lp.server.personal.ejb.Zeitmodell;
import com.lp.server.personal.ejb.Zeitmodellspr;
import com.lp.server.personal.ejb.ZeitmodellsprPK;
import com.lp.server.personal.ejb.Zeitmodelltag;
import com.lp.server.personal.ejb.Zeitmodelltagpause;
import com.lp.server.personal.ejb.Zeitstift;
import com.lp.server.personal.ejb.Zeitverteilung;
import com.lp.server.personal.fastlanereader.generated.FLRArtikelzulage;
import com.lp.server.personal.fastlanereader.generated.FLRBereitschaft;
import com.lp.server.personal.fastlanereader.generated.FLRLohnartstundenfaktor;
import com.lp.server.personal.fastlanereader.generated.FLRMaschine;
import com.lp.server.personal.fastlanereader.generated.FLRMaschinenzeitdaten;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import com.lp.server.personal.fastlanereader.generated.FLRPersonalverfuegbarkeit;
import com.lp.server.personal.fastlanereader.generated.FLRReise;
import com.lp.server.personal.fastlanereader.generated.FLRTaetigkeit;
import com.lp.server.personal.fastlanereader.generated.FLRTaetigkeitspr;
import com.lp.server.personal.fastlanereader.generated.FLRTelefonzeiten;
import com.lp.server.personal.fastlanereader.generated.FLRZeitabschluss;
import com.lp.server.personal.fastlanereader.generated.FLRZeitdaten;
import com.lp.server.personal.fastlanereader.generated.FLRZeitdatenLos;
import com.lp.server.personal.service.ArtikelzulageDto;
import com.lp.server.personal.service.BereitschaftDto;
import com.lp.server.personal.service.BereitschaftDtoAssembler;
import com.lp.server.personal.service.BereitschafttagDto;
import com.lp.server.personal.service.BereitschafttagDtoAssembler;
import com.lp.server.personal.service.BetriebskalenderDto;
import com.lp.server.personal.service.DiaetenDto;
import com.lp.server.personal.service.DiaetenDtoAssembler;
import com.lp.server.personal.service.DiaetentagessatzDto;
import com.lp.server.personal.service.DiaetentagessatzDtoAssembler;
import com.lp.server.personal.service.EintrittaustrittDto;
import com.lp.server.personal.service.GleitzeitsaldoDto;
import com.lp.server.personal.service.GleitzeitsaldoDtoAssembler;
import com.lp.server.personal.service.Kollektivuestd50Dto;
import com.lp.server.personal.service.KollektivuestdDto;
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.personal.service.MaschineDtoAssembler;
import com.lp.server.personal.service.MaschinengruppeDto;
import com.lp.server.personal.service.MaschinengruppeDtoAssembler;
import com.lp.server.personal.service.MaschinenkostenDto;
import com.lp.server.personal.service.MaschinenkostenDtoAssembler;
import com.lp.server.personal.service.MaschinenzeitdatenDto;
import com.lp.server.personal.service.MaschinenzeitdatenDtoAssembler;
import com.lp.server.personal.service.MonatsabrechnungBereitschaftDto;
import com.lp.server.personal.service.MonatsabrechnungDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.personal.service.PersonalgehaltDto;
import com.lp.server.personal.service.PersonalzeitmodellDto;
import com.lp.server.personal.service.PersonalzeitmodellDtoAssembler;
import com.lp.server.personal.service.ReiseDto;
import com.lp.server.personal.service.ReiseDtoAssembler;
import com.lp.server.personal.service.ReiseKomplettDto;
import com.lp.server.personal.service.ReiselogDto;
import com.lp.server.personal.service.ReiselogDtoAssembler;
import com.lp.server.personal.service.SollverfuegbarkeitDto;
import com.lp.server.personal.service.SonderzeitenDto;
import com.lp.server.personal.service.SonderzeitenDtoAssembler;
import com.lp.server.personal.service.SonderzeitenImportDto;
import com.lp.server.personal.service.StundenabrechnungDto;
import com.lp.server.personal.service.TaetigkeitDto;
import com.lp.server.personal.service.TaetigkeitDtoAssembler;
import com.lp.server.personal.service.TaetigkeitartDto;
import com.lp.server.personal.service.TaetigkeitartDtoAssembler;
import com.lp.server.personal.service.TaetigkeitartsprDto;
import com.lp.server.personal.service.TaetigkeitartsprDtoAssembler;
import com.lp.server.personal.service.TaetigkeitsprDto;
import com.lp.server.personal.service.TaetigkeitsprDtoAssembler;
import com.lp.server.personal.service.TagesartDto;
import com.lp.server.personal.service.TagesartDtoAssembler;
import com.lp.server.personal.service.TagesartsprDto;
import com.lp.server.personal.service.TagesartsprDtoAssembler;
import com.lp.server.personal.service.TelefonzeitenDto;
import com.lp.server.personal.service.TelefonzeitenDtoAssembler;
import com.lp.server.personal.service.UrlaubsabrechnungDto;
import com.lp.server.personal.service.UrlaubsanspruchDto;
import com.lp.server.personal.service.VonBisErfassungTagesdatenDto;
import com.lp.server.personal.service.WochenabschlussReportDto;
import com.lp.server.personal.service.ZeileMonatsabrechnungDto;
import com.lp.server.personal.service.ZeitabschlussDto;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.personal.service.ZeitdatenDtoAssembler;
import com.lp.server.personal.service.ZeitdatenDtoBelegzeiten;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeiterfassungFacAll;
import com.lp.server.personal.service.ZeiterfassungFacLocal;
import com.lp.server.personal.service.ZeiterfassungReportFac;
import com.lp.server.personal.service.ZeitmodellDto;
import com.lp.server.personal.service.ZeitmodellDtoAssembler;
import com.lp.server.personal.service.ZeitmodellsprDto;
import com.lp.server.personal.service.ZeitmodellsprDtoAssembler;
import com.lp.server.personal.service.ZeitmodelltagDto;
import com.lp.server.personal.service.ZeitmodelltagDtoAssembler;
import com.lp.server.personal.service.ZeitmodelltagpauseDto;
import com.lp.server.personal.service.ZeitmodelltagpauseDtoAssembler;
import com.lp.server.personal.service.ZeitstiftDto;
import com.lp.server.personal.service.ZeitstiftDtoAssembler;
import com.lp.server.personal.service.ZeitverteilungDto;
import com.lp.server.personal.service.ZeitverteilungDtoAssembler;
import com.lp.server.personal.service.ZulageDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.projekt.service.ProjektServiceFac;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeWochenabschlussBeleg;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.DatumsfilterVonBis;
import com.lp.server.util.HelperServer;
import com.lp.server.util.LPReport;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.logger.HvDtoLogger;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

@Stateless
@SuppressWarnings("unused")
@Local(ZeiterfassungFacLocal.class)
@Remote(ZeiterfassungFac.class)
public class ZeiterfassungFacBean extends LPReport implements JRDataSource,
		ZeiterfassungFac, ZeiterfassungFacLocal {
	@PersistenceContext
	private EntityManager em;

	private Object[][] data = null;
	private String sAktuellerReport = null;

	public static int REPORT_MONATSABRECHNUNG_KALENDERWOCHE = 0;
	public static int REPORT_MONATSABRECHNUNG_TAG = 1;
	public static int REPORT_MONATSABRECHNUNG_DATUM = 2;
	public static int REPORT_MONATSABRECHNUNG_VON = 3;
	public static int REPORT_MONATSABRECHNUNG_BIS = 4;
	public static int REPORT_MONATSABRECHNUNG_UNTER = 5;
	public static int REPORT_MONATSABRECHNUNG_SOLL = 6;
	public static int REPORT_MONATSABRECHNUNG_IST = 7;
	public static int REPORT_MONATSABRECHNUNG_DIFF = 8;
	public static int REPORT_MONATSABRECHNUNG_FEIERTAG = 9;
	public static int REPORT_MONATSABRECHNUNG_URLAUB = 10;
	public static int REPORT_MONATSABRECHNUNG_URLAUBTAGEWEISE = 11;
	public static int REPORT_MONATSABRECHNUNG_ARZT = 12;
	public static int REPORT_MONATSABRECHNUNG_BEHOERDE = 13;
	public static int REPORT_MONATSABRECHNUNG_ZEITAUSGLEICH = 14;
	public static int REPORT_MONATSABRECHNUNG_KRANK = 15;
	public static int REPORT_MONATSABRECHNUNG_MEHRZEIT = 16;
	public static int REPORT_MONATSABRECHNUNG_UESTD50 = 17;
	public static int REPORT_MONATSABRECHNUNG_UESTD100 = 18;
	public static int REPORT_MONATSABRECHNUNG_UESTD50TAGESWEISEFREI = 19;
	public static int REPORT_MONATSABRECHNUNG_UESTD100FREI = 20;
	public static int REPORT_MONATSABRECHNUNG_UESTD200 = 21;
	public static int REPORT_MONATSABRECHNUNG_BEMERKUNG = 22;
	public static int REPORT_MONATSABRECHNUNG_SONSTIGE_BEZAHLTETAETIGKEITEN = 23;
	public static int REPORT_MONATSABRECHNUNG_SONSTIGE_UNBEZAHLTETAETIGKEITEN = 24;
	public static int REPORT_MONATSABRECHNUNG_ZUSATZBEZEICHNUNG = 25;
	public static int REPORT_MONATSABRECHNUNG_REISE = 26;
	public static int REPORT_MONATSABRECHNUNG_UESTD50TAGEWEISE = 27;
	public static int REPORT_MONATSABRECHNUNG_JAHR = 28;
	public static int REPORT_MONATSABRECHNUNG_TAGESART = 29;
	public static int REPORT_MONATSABRECHNUNG_ZEITMODELL = 30;
	public static int REPORT_MONATSABRECHNUNG_KINDKRANK = 31;
	public static int REPORT_MONATSABRECHNUNG_SUBREPORT_ZULAGEN = 32;
	public static int REPORT_MONATSABRECHNUNG_PLATZHALTER_FUER_SUREPORTZEIDATENJOURNAL = 33;
	public static int REPORT_MONATSABRECHNUNG_MONAT = 34;
	public static int REPORT_MONATSABRECHNUNG_QUALIFIKATIONSFAKTOR = 35;
	public static int REPORT_MONATSABRECHNUNG_ANZAHL_SPALTEN = 36;

	private static int REPORT_ANWESENHEITSLISTE_ANWESEND = 0;
	private static int REPORT_ANWESENHEITSLISTE_NAME = 1;
	private static int REPORT_ANWESENHEITSLISTE_PERSONALNUMMER = 2;
	private static int REPORT_ANWESENHEITSLISTE_TEL_PRIVAT = 3;
	private static int REPORT_ANWESENHEITSLISTE_TAETIGKEIT = 4;
	private static int REPORT_ANWESENHEITSLISTE_ZEIT = 5;

	private static int REPORT_ANWESENHEITSLISTE_SONDERZEIT = 6;
	private static int REPORT_ANWESENHEITSLISTE_SONDERZEIT_STUNDEN = 7;
	private static int REPORT_ANWESENHEITSLISTE_SONDERZEIT_ART = 8;

	private static int REPORT_SONDERTAETIGKEITENLISTE_PERSONALNUMMER_GRUPPIERUNG = 0;
	private static int REPORT_SONDERTAETIGKEITENLISTE_PERSONALNUMMER = 1;
	private static int REPORT_SONDERTAETIGKEITENLISTE_NAME = 2;
	private static int REPORT_SONDERTAETIGKEITENLISTE_TAETIGKEIT = 3;
	private static int REPORT_SONDERTAETIGKEITENLISTE_ZEIT = 4;
	private static int REPORT_SONDERTAETIGKEITENLISTE_FAKTORBEZAHLT = 5;
	private static int REPORT_SONDERTAETIGKEITENLISTE_LFD_FEHLTAGE = 6;
	private static int REPORT_SONDERTAETIGKEITENLISTE_WARNMEDLUNG_IN_KALENDERTAGEN = 7;
	public static int REPORT_SONDERTAETIGKEITENLISTE_ANZAHL_SPALTEN = 8;

	private static int REPORT_PRODUKTIVITAETISSTATISTIK_PERSONAL_ID = 0;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_AUFTRAG = 1;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_LOS = 2;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_ANGEBOT = 3;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_PROJEKT = 4;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_PROJEKTBEZEICHNUNG = 5;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_KUNDE = 6;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_DAUER = 7;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_STKLNR = 8;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_STKLBEZ = 9;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_LOSKLASSEN = 10;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_TAETIGKEIT = 11;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_TAETIGKEIT_BEZEICHNUNG = 12;

	private static int REPORT_PRODUKTIVITAETISSTATISTIK_LOS_AGART = 13;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_LOS_RUESTZEIT = 14;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_LOS_STUECKZEIT = 15;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_LOS_GUTSTUECK = 16;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_LOS_SCHLECHTSTUECK = 17;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_LOS_INARBEIT = 18;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_LOS_LOSGROESSE = 19;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_LOS_ZEITANTEIL = 20;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_LOS_FERTIG = 21;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_LOS_GESAMTZEIT = 22;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_LOS_AGNUMMER = 23;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_LOS_UAGNUMMER = 24;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_LOS_RUESTEN_MITRECHNEN = 25;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_PROJEKT_PROJEKTKLAMMER = 26;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_PROJEKTKATEGORIE_PROJEKTKLAMMER = 27;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_TELEFONZEIT = 28;

	private static int REPORT_PRODUKTIVITAETISSTATISTIK_PERSON_NAME = 29;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_PERSON_IST_GESAMT = 30;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_PERSON_INTERN = 31;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_PERSON_EXTERN = 32;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_PERSON_PROZENTINTERN = 33;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_PERSON_PROZENTEXTERN = 34;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_PERSON_LEISTUNGSWERT = 35;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_PERSON_SUBREPORT_SONDERTAETIGKEITEN = 36;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_PERSON_KOSTENSTELLE = 37;
	private static int REPORT_PRODUKTIVITAETISSTATISTIK_PERSON_ABTEILUNG = 38;

	private static int REPORT_PRODUKTIVITAETISSTATISTIK_ANZAHL_SPALTEN = 39;

	private static int REPORT_SONDERTAETIGKEITEN_KENNUNG = 0;
	private static int REPORT_SONDERTAETIGKEITEN_BEZEICHNUNG = 1;

	private static int REPORT_ZEITSALDO_PERSON = 0;
	private static int REPORT_ZEITSALDO_PERSONALNUMMER = 1;
	private static int REPORT_ZEITSALDO_SOLL = 2;
	private static int REPORT_ZEITSALDO_IST = 3;
	private static int REPORT_ZEITSALDO_FTGSOLL = 4;
	private static int REPORT_ZEITSALDO_FTG = 5;
	private static int REPORT_ZEITSALDO_ARZT = 6;
	private static int REPORT_ZEITSALDO_KRANK = 7;
	private static int REPORT_ZEITSALDO_BEHOERDE = 8;
	private static int REPORT_ZEITSALDO_URLAUBSTD = 9;
	private static int REPORT_ZEITSALDO_SONSTIGEBEZAHLT = 10;
	private static int REPORT_ZEITSALDO_REISE = 11;
	private static int REPORT_ZEITSALDO_SONSTIGENICHTBEZAHLT = 12;
	private static int REPORT_ZEITSALDO_URLAUBTAGE_URLAUBREST = 13;
	private static int REPORT_ZEITSALDO_URLAUBTAGE_URLAUBRESTVERBRAUCHT = 14;
	private static int REPORT_ZEITSALDO_URLAUBTAGE_ALIQUOT = 15;
	private static int REPORT_ZEITSALDO_URLAUBTAGE_URLAUBAKTUELLVERBRAUCHT = 16;
	private static int REPORT_ZEITSALDO_URLAUBTAGE_URLAUBAKTUELL = 17;
	private static int REPORT_ZEITSALDO_URLAUBTAGE_URLAUB_GEPLANT = 18;
	private static int REPORT_ZEITSALDO_URLAUBTAGE_URLAUB_VERFUGBAR = 19;
	private static int REPORT_ZEITSALDO_URLAUBSTUNDEN_URLAUBREST = 20;
	private static int REPORT_ZEITSALDO_URLAUBSTUNDEN_URLAUBRESTVERBRAUCHT = 21;
	private static int REPORT_ZEITSALDO_URLAUBSTUNDEN_ALIQUOT = 22;
	private static int REPORT_ZEITSALDO_URLAUBSTUNDEN_URLAUBAKUTELLVERBRAUCHT = 23;
	private static int REPORT_ZEITSALDO_URLAUBSTUNDEN_URLAUBAKUTELL = 24;
	private static int REPORT_ZEITSALDO_URLAUBSTUNDEN_URLAUB_GEPLANT = 25;
	private static int REPORT_ZEITSALDO_URLAUBSTUNDEN_URLAUB_VERFUGBAR = 26;
	private static int REPORT_ZEITSALDO_UESTDSALDO100 = 27;
	private static int REPORT_ZEITSALDO_UESTDSALDO50 = 28;
	private static int REPORT_ZEITSALDO_UESTDSALDO100STF = 29;
	private static int REPORT_ZEITSALDO_UESTDSALDO50STF = 30;
	private static int REPORT_ZEITSALDO_UESTDSALDOMEHRSTD = 31;
	private static int REPORT_ZEITSALDO_GLEITZEITSALDOVORMONAT = 32;
	private static int REPORT_ZEITSALDO_GLEITZEITSALDOAKTUELLERMONAT = 33;
	private static int REPORT_ZEITSALDO_MONAT = 34;
	private static int REPORT_ZEITSALDO_URLAUBTAGE_URLAUBVORJAHR = 35;
	private static int REPORT_ZEITSALDO_UESTDSALDO200 = 36;
	private static int REPORT_ZEITSALDO_AUSBEZAHLTNORMALSTD = 37;
	private static int REPORT_ZEITSALDO_AUSBEZAHLTMEHRSTD = 38;
	private static int REPORT_ZEITSALDO_AUSBEZAHLT50 = 39;
	private static int REPORT_ZEITSALDO_AUSBEZAHLT50STF = 40;
	private static int REPORT_ZEITSALDO_AUSBEZAHLT100 = 41;
	private static int REPORT_ZEITSALDO_AUSBEZAHLT100STF = 42;
	private static int REPORT_ZEITSALDO_AUSBEZAHLT200 = 43;
	private static int REPORT_ZEITSALDO_AUSBEZAHLTGUTSTD = 44;
	private static int REPORT_ZEITSALDO_QUALIPRAEMIE = 45;
	private static int REPORT_ZEITSALDO_KIND_KRANK = 46;
	private static int REPORT_ZEITSALDO_ANZAHL_ZEILEN = 47;

	private static int REPORT_WOCHENABSCHLUSS_DATUM = 0;
	private static int REPORT_WOCHENABSCHLUSS_KOMMT = 1;
	private static int REPORT_WOCHENABSCHLUSS_GEHT = 2;
	private static int REPORT_WOCHENABSCHLUSS_SOLL = 3;
	private static int REPORT_WOCHENABSCHLUSS_IST = 4;
	private static int REPORT_WOCHENABSCHLUSS_SUBREPORT_BELEGZEITEN = 5;
	private static int REPORT_WOCHENABSCHLUSS_FEIERTAG = 6;
	private static int REPORT_WOCHENABSCHLUSS_ARZT = 7;
	private static int REPORT_WOCHENABSCHLUSS_KRANK = 8;
	private static int REPORT_WOCHENABSCHLUSS_KINDKRANK = 9;
	private static int REPORT_WOCHENABSCHLUSS_BEHOERDE = 10;
	private static int REPORT_WOCHENABSCHLUSS_URLAUB = 11;
	private static int REPORT_WOCHENABSCHLUSS_SONSTIGE_BEZAHLT = 12;
	private static int REPORT_WOCHENABSCHLUSS_SONSTIGE_UNBEZAHLT = 13;
	private static int REPORT_WOCHENABSCHLUSS_FEHLER = 14;
	private static int REPORT_WOCHENABSCHLUSS_WOCHENTAG = 15;
	private static int REPORT_WOCHENABSCHLUSS_ANZAHL_SPALTEN = 16;

	/**
	 * Berechne die TagesArbeitszeit eines Tages zu einer Person, ohne
	 * Paarweisen Sondertaetigkeiten meldet Fehler, wenn die Tagesarbeitszeit
	 * nicht berechnet werden konnte
	 *
	 * @param personalIId
	 *            Die Person
	 * @param d_datum
	 *            Das Datum
	 * @param theClientDto
	 *            User-ID
	 * @return String beinhaltet Tagesarbeitszeit
	 * @exception EJBExceptionLP
	 */

	public VonBisErfassungTagesdatenDto berechneTagesArbeitszeitVonBisZeiterfassungOhneKommtGeht(
			Integer personalIId, java.sql.Date d_datum,
			TheClientDto theClientDto) {

		VonBisErfassungTagesdatenDto vonBisDto = new VonBisErfassungTagesdatenDto();

		// Hole Dto der Unterbrechung
		TaetigkeitDto taetigkeitDto_Unter = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_UNTER, theClientDto);
		// Hole Dto des Arztbesuchs
		TaetigkeitDto taetigkeitDto_Arzt = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_ARZT, theClientDto);
		// Hole Dto der Behoerde
		TaetigkeitDto taetigkeitDto_Behoerde = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_BEHOERDE, theClientDto);
		// Hole Dto des Urlaub
		TaetigkeitDto taetigkeitDto_Urlaub = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_URLAUB, theClientDto);
		// Hole Dto des Zeitausgleichs
		TaetigkeitDto taetigkeitDto_ZA = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_ZEITAUSGLEICH, theClientDto);
		// Hole Dto der Behoerde
		TaetigkeitDto taetigkeitDto_Krank = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_KRANK, theClientDto);
		TaetigkeitDto taetigkeitDto_Kindkrank = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_KINDKRANK, theClientDto);

		// Berechnungsdatum abschneiden, damit nur mehr JJJJ.MM.TT ueberbleibt
		Calendar c = Calendar.getInstance();
		c.setTime(d_datum);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		d_datum = new java.sql.Date(c.getTime().getTime());
		// Heutiges Datum herausfinden und abschneiden, damit nur mehr
		// JJJJ.MM.TT ueberbleibt
		c.setTimeInMillis(System.currentTimeMillis());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		java.sql.Date d_heute = new java.sql.Date(c.getTime().getTime());

		ZeitdatenDto[] zeitdatenDtos = null;

		// try {
		// Hole Zeitdaten eines Tages
		Query query = em
				.createNamedQuery("ZeitdatenfindZeitdatenEinesTagesUndEinerPerson");
		query.setParameter(1, personalIId);
		query.setParameter(2, new java.sql.Timestamp(d_datum.getTime()));
		query.setParameter(3, new java.sql.Timestamp(
				d_datum.getTime() + 24 * 3600000));
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// zeitdatenDtos = null;
		// }
		// else {

		zeitdatenDtos = assembleZeitdatenDtosOhneEnde(cl);

		for (int i = 0; i < zeitdatenDtos.length; i++) {
			ZeitdatenDto zeitdatenDto_Aktuell = zeitdatenFindByPrimaryKey(
					zeitdatenDtos[i].getIId(), theClientDto);

			if (zeitdatenDto_Aktuell.getCBelegartnr() != null) {

				// eigentlich sollte nun eine Bis-Zeit vorhanden sein
				if (zeitdatenDto_Aktuell.gettZeit_Bis() != null) {

					java.sql.Time tDauer = new java.sql.Time(
							zeitdatenDto_Aktuell.gettZeit_Bis().getTime()
									- zeitdatenDto_Aktuell.getTZeit().getTime()
									- 3600000);

					double dIstZeile = Helper.time2Double(tDauer);
					vonBisDto.setdIst(vonBisDto.getdIst() + dIstZeile);
				}

			} else if (zeitdatenDto_Aktuell.getTaetigkeitIId() != null) {

				if (zeitdatenDto_Aktuell.gettZeit_Bis() != null) {

					java.sql.Time tDauer = new java.sql.Time(
							zeitdatenDto_Aktuell.gettZeit_Bis().getTime()
									- zeitdatenDto_Aktuell.getTZeit().getTime()
									- 3600000);

					double dIstZeile = Helper.time2Double(tDauer);

					if (zeitdatenDto_Aktuell.getTaetigkeitIId().equals(
							taetigkeitDto_Unter.getIId())) {
						vonBisDto.setdUnter(vonBisDto.getdUnter() + dIstZeile);
					} else if (zeitdatenDto_Aktuell.getTaetigkeitIId().equals(
							taetigkeitDto_Arzt.getIId())) {
						vonBisDto.setdArzt(vonBisDto.getdArzt() + dIstZeile);
					} else if (zeitdatenDto_Aktuell.getTaetigkeitIId().equals(
							taetigkeitDto_Behoerde.getIId())) {
						vonBisDto.setdBehoerde(vonBisDto.getdBehoerde()
								+ dIstZeile);
					} else if (zeitdatenDto_Aktuell.getTaetigkeitIId().equals(
							taetigkeitDto_Kindkrank.getIId())) {
						vonBisDto.setdKindkrank(vonBisDto.getdKindkrank()
								+ dIstZeile);
					} else if (zeitdatenDto_Aktuell.getTaetigkeitIId().equals(
							taetigkeitDto_Krank.getIId())) {
						vonBisDto.setdKrank(vonBisDto.getdKrank() + dIstZeile);
					} else if (zeitdatenDto_Aktuell.getTaetigkeitIId().equals(
							taetigkeitDto_Urlaub.getIId())) {
						vonBisDto
								.setdUrlaub(vonBisDto.getdUrlaub() + dIstZeile);
					} else if (zeitdatenDto_Aktuell.getTaetigkeitIId().equals(
							taetigkeitDto_ZA.getIId())) {
						vonBisDto.setdZeitausgleich(vonBisDto
								.getdZeitausgleich() + dIstZeile);
					} else {
						TaetigkeitDto tDto = taetigkeitFindByPrimaryKey(
								zeitdatenDto_Aktuell.getTaetigkeitIId(),
								theClientDto);

						if (tDto.getFBezahlt() == 0) {
							vonBisDto.setdSonstigeNichtBezahlt(vonBisDto
									.getdSonstigeNichtBezahlt() + dIstZeile);
						} else {

							double dSumme = Helper
									.rundeKaufmaennisch(
											new BigDecimal(dIstZeile)
													.multiply(new BigDecimal(
															tDto.getFBezahlt()
																	.doubleValue() / 100)),
											2).doubleValue();

							vonBisDto.setdSontigeBezahlt(vonBisDto
									.getdSontigeBezahlt() + dSumme);

						}

					}

				}

			}

		}

		return vonBisDto;

	}

	public Double berechneTagesArbeitszeit(Integer personalIId,
			java.sql.Date d_datum, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (personalIId == null || d_datum == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("personalIId == null || d_datum == null"));
		}

		// Hole id der Taetigkeit KOMMT
		Integer taetigkeitIId_Kommt = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_KOMMT, theClientDto).getIId();
		// Hole id der Taetigkeit GEHT
		Integer taetigkeitIId_Geht = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_GEHT, theClientDto).getIId();

		Integer taetigkeitIId_Telefon = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_TELEFON, theClientDto).getIId();

		// Berechnungsdatum abschneiden, damit nur mehr JJJJ.MM.TT ueberbleibt
		Calendar c = Calendar.getInstance();
		c.setTime(d_datum);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		d_datum = new java.sql.Date(c.getTime().getTime());
		// Heutiges Datum herausfinden und abschneiden, damit nur mehr
		// JJJJ.MM.TT ueberbleibt
		c.setTimeInMillis(System.currentTimeMillis());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		java.sql.Date d_heute = new java.sql.Date(c.getTime().getTime());

		ZeitdatenDto[] zeitdatenDtos = null;

		// try {
		// Hole Zeitdaten eines Tages
		Query query = em
				.createNamedQuery("ZeitdatenfindZeitdatenEinesTagesUndEinerPerson");
		query.setParameter(1, personalIId);
		query.setParameter(2, new java.sql.Timestamp(d_datum.getTime()));
		query.setParameter(3, new java.sql.Timestamp(
				d_datum.getTime() + 24 * 3600000));
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// zeitdatenDtos = null;
		// }
		// else {

		zeitdatenDtos = assembleZeitdatenDtosOhneBelegzeiten(cl);

		zeitdatenDtos = simuliereMitternachssprung(personalIId, d_datum,
				taetigkeitIId_Kommt, taetigkeitIId_Geht, zeitdatenDtos);

		// Wenn Heute und die letzte Taetigkeit ist kein GEHT, dann GEHT
		// simulieren, damit Tagesarbeitszeit bis JETZT berechnet werden kann
		if (d_datum.equals(d_heute)) {
			if (zeitdatenDtos != null && zeitdatenDtos.length > 0) {
				ZeitdatenDto zeitdatenDto_LetzterEintrag = zeitdatenDtos[zeitdatenDtos.length - 1];

				if (zeitdatenDto_LetzterEintrag.getTaetigkeitIId() == null
						|| zeitdatenDto_LetzterEintrag.getTaetigkeitIId()
								.intValue() != taetigkeitIId_Geht.intValue()) {
					ZeitdatenDto[] dtosTemp = new ZeitdatenDto[zeitdatenDtos.length + 1];

					for (int i = 0; i < zeitdatenDtos.length; i++) {
						dtosTemp[i] = zeitdatenDtos[i];
					}

					// Neue Zeile
					ZeitdatenDto dtoTemp = new ZeitdatenDto();
					dtoTemp.setPersonalIId(personalIId);
					dtoTemp.setTaetigkeitIId(taetigkeitIId_Geht);
					dtoTemp.setTZeit(new Timestamp(System.currentTimeMillis()));
					dtoTemp.setPersonalIId(personalIId);
					dtoTemp.setPersonalIId(personalIId);
					dtosTemp[zeitdatenDtos.length] = dtoTemp;
					zeitdatenDtos = dtosTemp;

				}

			}
		}

		// }
		// catch (FinderException ex) {
		// zeitdatenDtos = null;
		// }
		double dGesamt = 0;
		// Wenn Zeitdaten zum gewuenschten Tag vorhanden sind, dann
		// Tagesarbeitszeit berechnen
		if (!d_datum.equals(d_heute) && zeitdatenDtos != null
				&& zeitdatenDtos.length == 1) {
			if (d_datum.before(d_heute)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_ZEITERFASSUNG_FEHLER_ZEITDATEN,
						new Exception("FEHLER_ZEITERFASSUNG_FEHLER_ZEITDATEN"));
			}
		}
		if (zeitdatenDtos != null && zeitdatenDtos.length > 1) {
			ZeitdatenDto zeitdatenDto_Vorher = null;
			// Ersten und letzten Eintrag des Tages setzen
			long t1 = zeitdatenDtos[0].getTZeit().getTime();
			long t2 = zeitdatenDtos[zeitdatenDtos.length - 1].getTZeit()
					.getTime();
			;

			double differenzInMS = (t2 - t1);

			Double dIstUestd = differenzInMS / 3600000;

			if (!d_datum.equals(d_heute)
					&& !taetigkeitIId_Kommt.equals(zeitdatenDtos[0]
							.getTaetigkeitIId())) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_ZEITERFASSUNG_TAETIGKEIT_VOR_KOMMT,
						new Exception(
								"FEHLER_ZEITERFASSUNG_TAETIGKEIT_VOR_KOMMT"));
			}
			if (!d_datum.equals(d_heute)
					&& !taetigkeitIId_Geht
							.equals(zeitdatenDtos[zeitdatenDtos.length - 1]
									.getTaetigkeitIId())) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_ZEITERFASSUNG_GEHT_FEHLT,
						new Exception("FEHLER_ZEITERFASSUNG_GEHT_FEHLT"));
			}

			// Ueber jede Zeitbuchung iterieren
			dGesamt = dIstUestd;

			for (int i = 0; i < zeitdatenDtos.length; i++) {
				ZeitdatenDto zeitdatenDto_Aktuell = zeitdatenDtos[i];

				// Milliskunden auf 0 setzen
				Timestamp tsTemp = zeitdatenDto_Aktuell.getTZeit();
				Calendar c2 = Calendar.getInstance();
				c2.setTimeInMillis(tsTemp.getTime());
				c2.set(Calendar.MILLISECOND, 0);
				tsTemp = new Timestamp(c2.getTimeInMillis());
				zeitdatenDto_Aktuell.setTZeit(tsTemp);

				// Bei jeder Geraden Zahl mit dem Vorgaenger vergleichen
				if (i % 2 == 0 && i != 0) {
					if (zeitdatenDto_Aktuell.getTaetigkeitIId().equals(
							taetigkeitIId_Kommt)
							&& zeitdatenDto_Vorher.getTaetigkeitIId().equals(
									taetigkeitIId_Geht)) {
						// Wenn KOMMT - GEHT, dann nicht dazuzaehlen

						long d1 = zeitdatenDto_Vorher.getTZeit().getTime();
						long d2 = zeitdatenDto_Aktuell.getTZeit().getTime();
						double diffMS = (d2 - d1);

						double dSumme = diffMS / 3600000;
						dGesamt -= dSumme;
					} else if (zeitdatenDto_Aktuell.getTaetigkeitIId().equals(
							zeitdatenDto_Vorher.getTaetigkeitIId())) {

						if (!zeitdatenDto_Aktuell.getTaetigkeitIId().equals(
								taetigkeitIId_Telefon)) {
							long d1 = zeitdatenDto_Vorher.getTZeit().getTime();
							long d2 = zeitdatenDto_Aktuell.getTZeit().getTime();
							double diffMS = (d2 - d1);

							double dSumme = diffMS / 3600000;
							dGesamt -= dSumme;
						}

						// Wenn KOMMT, dann Mehrfaches KOMMT
						if (zeitdatenDto_Aktuell.getTaetigkeitIId().equals(
								taetigkeitIId_Kommt)) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_ZEITERFASSUNG_MEHRFACHES_KOMMT,
									new Exception(
											"FEHLER_ZEITERFASSUNG_MEHRFACHES_KOMMT"));

						}
						// Wenn GEHT, dann Mehrfaches GEHT
						else if (zeitdatenDto_Aktuell.getTaetigkeitIId()
								.equals(taetigkeitIId_Geht)) {

							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_ZEITERFASSUNG_MEHRFACHES_GEHT,
									new Exception(
											"FEHLER_ZEITERFASSUNG_MEHRFACHES_GEHT"));

						}

						// Ansonsten muss Taetigkeit bei sonstigen bezahlten
						// oder nicht bezahlten Taetigkeiten dabei sein

					} else {
						if (!d_datum.equals(d_heute)) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_ZEITERFASSUNG_SONDERTAETIGKEIT_MUSS_BEENDET_WERDEN,
									new Exception(
											"FEHLER_ZEITERFASSUNG_SONDERTAETIGKEIT_MUSS_BEENDET_WERDEN"));
						}
					}
				}

				zeitdatenDto_Vorher = zeitdatenDto_Aktuell;
			}
		}
		return dGesamt;
	}

	private ZeitdatenDto[] simuliereMitternachssprung(Integer personalIId,
			java.sql.Date d_datum, Integer taetigkeitIId_Kommt,
			Integer taetigkeitIId_Geht, ZeitdatenDto[] zeitdatenDtos) {

		// Wenn die erste Buchung des tages kein Kommt ist,
		if (zeitdatenDtos.length == 0
				|| zeitdatenDtos[0].getTaetigkeitIId() == null
				|| !zeitdatenDtos[0].getTaetigkeitIId().equals(
						taetigkeitIId_Kommt)) {

			// Wenn noch eine Sondertaetigkeit offen ist
			if (zeitdatenDtos.length > 0) {
				Integer letzteSondertaetigkeitIId = zeitdatenDtos[0]
						.getTaetigkeitIId();
				int iAnzahl = 1;
				for (int i = 1; i < zeitdatenDtos.length; i++) {
					if (!zeitdatenDtos[i].getTaetigkeitIId().equals(
							letzteSondertaetigkeitIId)
							&& iAnzahl % 2 == 1) {

						if (!zeitdatenDtos[i].getTaetigkeitIId().equals(
								taetigkeitIId_Geht)
								&& !zeitdatenDtos[i].getTaetigkeitIId().equals(
										taetigkeitIId_Kommt)) {

							ZeitdatenDto[] zeitdatenDtosTemp = new ZeitdatenDto[zeitdatenDtos.length + 1];
							// Neue Zeile
							ZeitdatenDto dtoTemp = new ZeitdatenDto();
							dtoTemp.setPersonalIId(personalIId);
							dtoTemp.setTaetigkeitIId(letzteSondertaetigkeitIId);
							dtoTemp.setTZeit(Helper
									.cutTimestamp(new java.sql.Timestamp(
											d_datum.getTime())));
							dtoTemp.setPersonalIId(personalIId);
							dtoTemp.setBAutomatikbuchung(Helper
									.boolean2Short(true));
							dtoTemp.setBTaetigkeitgeaendert(Helper
									.boolean2Short(false));

							zeitdatenDtosTemp[0] = dtoTemp;

							for (int j = 0; j < zeitdatenDtos.length; j++) {
								zeitdatenDtosTemp[j + 1] = zeitdatenDtos[j];

							}
							zeitdatenDtos = zeitdatenDtosTemp;
						}
						break;
					}
					iAnzahl++;
				}
			}
			String sQuery = "select zeitdaten FROM FLRZeitdaten zeitdaten WHERE zeitdaten.t_zeit<'"
					+ Helper.formatDateWithSlashes(d_datum)
					+ "' AND zeitdaten.personal_i_id="
					+ personalIId
					+ " AND (zeitdaten.taetigkeit_i_id="
					+ taetigkeitIId_Kommt
					+ " OR zeitdaten.taetigkeit_i_id="
					+ taetigkeitIId_Geht
					+ ") ORDER BY zeitdaten.t_zeit DESC";

			Session session = FLRSessionFactory.getFactory().openSession();

			org.hibernate.Query letzteKommtGeht = session.createQuery(sQuery);
			letzteKommtGeht.setMaxResults(1);

			List<?> resultList = letzteKommtGeht.list();

			Iterator<?> resultListIterator = resultList.iterator();

			if (resultListIterator.hasNext()) {
				FLRZeitdaten l = (FLRZeitdaten) resultListIterator.next();
				// Wenn zuletzt ein Kommt gebucht wurde, dann OK

				if (l.getTaetigkeit_i_id().equals(taetigkeitIId_Kommt)) {
					// KOMMT um 00:00 simulieren

					ZeitdatenDto[] zeitdatenDtosTemp = new ZeitdatenDto[zeitdatenDtos.length + 1];

					// Neue Zeile
					ZeitdatenDto dtoTemp = new ZeitdatenDto();
					dtoTemp.setPersonalIId(personalIId);
					dtoTemp.setTaetigkeitIId(taetigkeitIId_Kommt);
					dtoTemp.setTZeit(Helper
							.cutTimestamp(new java.sql.Timestamp(d_datum
									.getTime())));
					dtoTemp.setPersonalIId(personalIId);
					dtoTemp.setBAutomatikbuchung(Helper.boolean2Short(true));
					dtoTemp.setBTaetigkeitgeaendert(Helper.boolean2Short(false));

					zeitdatenDtosTemp[0] = dtoTemp;

					for (int i = 0; i < zeitdatenDtos.length; i++) {
						zeitdatenDtosTemp[i + 1] = zeitdatenDtos[i];

					}
					zeitdatenDtos = zeitdatenDtosTemp;
				}
			}
		}

		// Wenn die letzte Buchung des tages kein Geht ist,
		if (zeitdatenDtos.length == 0
				|| zeitdatenDtos[zeitdatenDtos.length - 1].getTaetigkeitIId() == null
				|| !zeitdatenDtos[zeitdatenDtos.length - 1].getTaetigkeitIId()
						.equals(taetigkeitIId_Geht)) {
			if (zeitdatenDtos.length > 0) {
				// Wenn noch eine Sondertaetigkeit offen ist
				if (!zeitdatenDtos[zeitdatenDtos.length - 1].getTaetigkeitIId()
						.equals(taetigkeitIId_Kommt)) {
					Integer letzteSondertaetigkeitIId = zeitdatenDtos[zeitdatenDtos.length - 1]
							.getTaetigkeitIId();
					int iAnzahl = 1;
					for (int i = zeitdatenDtos.length - 2; i >= 0; --i) {
						if (!zeitdatenDtos[i].getTaetigkeitIId().equals(
								letzteSondertaetigkeitIId)
								&& iAnzahl % 2 == 1) {

							if (!zeitdatenDtos[i].getTaetigkeitIId().equals(
									taetigkeitIId_Geht)
									&& !zeitdatenDtos[i].getTaetigkeitIId()
											.equals(taetigkeitIId_Kommt)) {

								ZeitdatenDto[] zeitdatenDtosTemp = new ZeitdatenDto[zeitdatenDtos.length + 1];
								// Neue Zeile
								ZeitdatenDto dtoTemp = new ZeitdatenDto();
								dtoTemp.setPersonalIId(personalIId);
								dtoTemp.setTaetigkeitIId(letzteSondertaetigkeitIId);
								dtoTemp.setTZeit(Helper
										.cutTimestamp(new java.sql.Timestamp(
												Helper.addiereTageZuDatum(
														d_datum, 1).getTime())));
								dtoTemp.setPersonalIId(personalIId);
								dtoTemp.setBAutomatikbuchung(Helper
										.boolean2Short(true));
								dtoTemp.setBTaetigkeitgeaendert(Helper
										.boolean2Short(false));
								for (int j = 0; j < zeitdatenDtos.length; j++) {
									zeitdatenDtosTemp[j] = zeitdatenDtos[j];
								}

								zeitdatenDtosTemp[zeitdatenDtos.length] = dtoTemp;

								zeitdatenDtos = zeitdatenDtosTemp;
							}
							break;
						}
						iAnzahl++;
					}

				}
			}
			String sQuery = "select zeitdaten FROM FLRZeitdaten zeitdaten WHERE zeitdaten.t_zeit>='"
					+ Helper.formatDateWithSlashes(Helper.addiereTageZuDatum(
							d_datum, 1))
					+ "' AND zeitdaten.personal_i_id="
					+ personalIId
					+ " AND (zeitdaten.taetigkeit_i_id="
					+ taetigkeitIId_Kommt
					+ " OR zeitdaten.taetigkeit_i_id="
					+ taetigkeitIId_Geht + ") ORDER BY zeitdaten.t_zeit ASC";

			Session session = FLRSessionFactory.getFactory().openSession();

			org.hibernate.Query letzteKommtGeht = session.createQuery(sQuery);
			letzteKommtGeht.setMaxResults(1);

			List<?> resultList = letzteKommtGeht.list();

			Iterator<?> resultListIterator = resultList.iterator();

			if (resultListIterator.hasNext()) {
				FLRZeitdaten l = (FLRZeitdaten) resultListIterator.next();
				// Wenn zuletzt ein GEHT gebucht wurde, dann OK

				if (l.getTaetigkeit_i_id().equals(taetigkeitIId_Geht)) {
					// GEHT um 23:59:59:999 simulieren

					ZeitdatenDto[] zeitdatenDtosTemp = new ZeitdatenDto[zeitdatenDtos.length + 1];

					// Neue Zeile
					ZeitdatenDto dtoTemp = new ZeitdatenDto();
					dtoTemp.setPersonalIId(personalIId);
					dtoTemp.setTaetigkeitIId(taetigkeitIId_Geht);
					dtoTemp.setTZeit(Helper
							.cutTimestamp(new java.sql.Timestamp(Helper
									.addiereTageZuDatum(d_datum, 1).getTime())));
					dtoTemp.setPersonalIId(personalIId);
					dtoTemp.setBAutomatikbuchung(Helper.boolean2Short(true));
					dtoTemp.setBTaetigkeitgeaendert(Helper.boolean2Short(false));
					for (int i = 0; i < zeitdatenDtos.length; i++) {
						zeitdatenDtosTemp[i] = zeitdatenDtos[i];
					}

					zeitdatenDtosTemp[zeitdatenDtos.length] = dtoTemp;

					zeitdatenDtos = zeitdatenDtosTemp;
				}
			}
		}

		return zeitdatenDtos;
	}

	public BigDecimal getSollzeitEinerPersonUndEinesTages(
			PersonalDto personalDto, Integer tagesartIId_Feiertag,
			Integer tagesartIId_Halbtag, Timestamp tDatum,
			TheClientDto theClientDto) {

		ZeitmodelltagDto zmtagDto = getZeitmodelltagZuDatum(
				personalDto.getIId(), tDatum, tagesartIId_Feiertag,
				tagesartIId_Halbtag, false, theClientDto);

		Double d = new Double(0);
		if (zmtagDto != null) {
			d = Helper.time2Double(zmtagDto.getUSollzeit());
		}

		if (d != null && d.doubleValue() > 0) {

			// TAGEWEISE SONDERZEITEN ABZIEHEN
			SonderzeitenDto[] sonderzeitenDtos = sonderzeitenFindByPersonalIIdDDatum(
					personalDto.getIId(), Helper.cutTimestamp(tDatum));
			double dHalbtag = d.doubleValue() / 2;
			for (int k = 0; k < sonderzeitenDtos.length; k++) {
				SonderzeitenDto sonderzeitenDto = sonderzeitenDtos[k];
				if (Helper.short2boolean(sonderzeitenDto.getBTag())) {
					d = new Double(0);
				} else if (Helper.short2boolean(sonderzeitenDto.getBHalbtag())) {
					d = new Double(d.doubleValue() - dHalbtag);
				} else {
					d = new Double(d.doubleValue()
							- Helper.time2Double(sonderzeitenDto.getUStunden())
									.doubleValue());
				}
			}
			// STUNDENWEISE SONDERZEITEN ABZIEHEN
			if (d.doubleValue() > 0) {
				// try {
				Query query1 = em
						.createNamedQuery("ZeitdatenfindZeitdatenEinesTagesUndEinerPerson");
				query1.setParameter(1, personalDto.getIId());
				query1.setParameter(2, Helper.cutTimestamp(tDatum));
				query1.setParameter(3, Helper.cutTimestamp(new Timestamp(tDatum
						.getTime() + 24 * 3600000)));

				ZeitdatenDto[] zeitdatenDtos = assembleZeitdatenDtosOhneBelegzeiten(query1
						.getResultList());
				if (zeitdatenDtos.length > 1) {
					Query query = em
							.createNamedQuery("TaetigkeitfindByBTagbuchbar");
					query.setParameter(1, Helper.boolean2Short(false));
					TaetigkeitDto[] dtos = assembleTaetigkeitDtos(query
							.getResultList());
					double dDauerGesamt = 0;
					for (int m = 0; m < dtos.length; m++) {
						TaetigkeitDto taetigkeit = dtos[m];
						if (taetigkeit.getCNr().equals(
								ZeiterfassungFac.TAETIGKEIT_ENDE)
								|| taetigkeit.getCNr().equals(
										ZeiterfassungFac.TAETIGKEIT_GEHT)
								|| taetigkeit.getCNr().equals(
										ZeiterfassungFac.TAETIGKEIT_KOMMT)
								|| taetigkeit.getCNr().equals(
										ZeiterfassungFac.TAETIGKEIT_KRANK)) {
						} else {
							try {
								dDauerGesamt += berechnePaarweiserSondertaetigkeiten(
										zeitdatenDtos, taetigkeit.getIId());
							} catch (Exception ex2) {
								// FEHLERHAFTE BUCHUNGEN
							}
						}
					}

					d = new Double(d.doubleValue() - dDauerGesamt);
					if (d.doubleValue() < 0) {
						d = new Double(0);
					}
				}
				// }
				// catch (FinderException ex1) {
				// }
			}

		}

		// BigDecimal ret = new BigDecimal(0);
		BigDecimal ret = BigDecimal.ZERO;
		if (d != null) {
			ret = new BigDecimal(d.doubleValue());
		}
		return ret;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	@SuppressWarnings("static-access")
	public SollverfuegbarkeitDto[] getVerfuegbareSollzeit(
			java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (tVon == null || tBis == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("tVon == null || tBis == null"));
		}

		tVon = Helper.cutTimestamp(tVon);
		tBis = Helper.cutTimestamp(tBis);

		Calendar c = Calendar.getInstance();
		c.setTime(tVon);
		int iAnzahlTage = Helper.getDifferenzInTagen(tVon, tBis);

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();
		// MASCHINENARBEITSZEITEN

		HashMap<Integer, BigDecimal> maschinengruppen = new HashMap<Integer, BigDecimal>();

		org.hibernate.Criteria crit = session
				.createCriteria(FLRMaschine.class)
				.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));
		List<?> resultList = crit.list();

		Iterator<?> resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {
			FLRMaschine flrmaschine = (FLRMaschine) resultListIterator.next();
			// Maschinen haben jeden Tag dieselbe Verfuegbarkeit

			double stunden = 0;
			for (int i = 0; i < iAnzahlTage; i++) {

				BigDecimal bdVerfuegbarkeitInStunden = getMaschineFac()
						.getVerfuegbarkeitInStundenZuDatum(
								flrmaschine.getI_id(),
								new java.sql.Date(c.getTimeInMillis()),
								theClientDto);

				if (bdVerfuegbarkeitInStunden != null) {
					stunden += bdVerfuegbarkeitInStunden.doubleValue();
				}

				c.set(c.DATE, c.get(c.DATE) + 1);

			}

			BigDecimal verfuegbareStungen = new BigDecimal(stunden);

			if (maschinengruppen.containsKey(flrmaschine
					.getMaschinengruppe_i_id())) {
				BigDecimal vorhandeneStunden = (BigDecimal) maschinengruppen
						.get(flrmaschine.getMaschinengruppe_i_id());
				maschinengruppen.put(flrmaschine.getMaschinengruppe_i_id(),
						vorhandeneStunden.add(verfuegbareStungen));
			} else {
				maschinengruppen.put(flrmaschine.getMaschinengruppe_i_id(),
						verfuegbareStungen);
			}

		}

		session.close();

		// MANNARBEITSZEITEN

		HashMap<Object, Number> artikelgruppen = new HashMap<Object, Number>();
		// BigDecimal artikelgruppeSonstiges = new BigDecimal(0);
		BigDecimal artikelgruppeSonstiges = BigDecimal.ZERO;

		Integer tagesartIId_Feiertag = tagesartFindByCNr(
				ZeiterfassungFac.TAGESART_FEIERTAG, theClientDto).getIId();
		Integer tagesartIId_Halbtag = tagesartFindByCNr(
				ZeiterfassungFac.TAGESART_HALBTAG, theClientDto).getIId();

		PersonalDto[] personalDtos = null;
		try {
			personalDtos = getPersonalFac().personalFindByMandantCNr(
					theClientDto.getMandant(), false);
		} catch (RemoteException ex) {
		}

		c.setTime(tVon);
		ArrayList<SollverfuegbarkeitDto> alArtikelgruppen = new ArrayList<SollverfuegbarkeitDto>();
		for (int i = 0; i < iAnzahlTage; i++) {
			// getsoll
			for (int j = 0; j < personalDtos.length; j++) {
				PersonalDto personalDto = personalDtos[j];

				ZeitmodelltagDto zmtagDto = getZeitmodelltagZuDatum(
						personalDto.getIId(),
						new Timestamp(c.getTimeInMillis()),
						tagesartIId_Feiertag, tagesartIId_Halbtag, false,
						theClientDto);

				Double d = new Double(0);
				if (zmtagDto != null) {
					d = Helper.time2Double(zmtagDto.getUSollzeit());
				}

				if (d != null && d.doubleValue() > 0) {

					// TAGEWEISE SONDERZEITEN ABZIEHEN
					SonderzeitenDto[] sonderzeitenDtos = sonderzeitenFindByPersonalIIdDDatum(
							personalDto.getIId(),
							Helper.cutTimestamp(new Timestamp(c
									.getTimeInMillis())));
					double dHalbtag = d.doubleValue() / 2;
					for (int k = 0; k < sonderzeitenDtos.length; k++) {
						SonderzeitenDto sonderzeitenDto = sonderzeitenDtos[k];
						if (Helper.short2boolean(sonderzeitenDto.getBTag())) {
							d = new Double(0);
						} else if (Helper.short2boolean(sonderzeitenDto
								.getBHalbtag())) {
							d = new Double(d.doubleValue() - dHalbtag);
						} else {
							d = new Double(d.doubleValue()
									- Helper.time2Double(
											sonderzeitenDto.getUStunden())
											.doubleValue());
						}
					}
					// STUNDENWEISE SONDERZEITEN ABZIEHEN
					if (d.doubleValue() > 0) {
						// try {
						Query query1 = em
								.createNamedQuery("ZeitdatenfindZeitdatenEinesTagesUndEinerPerson");
						query1.setParameter(1, personalDto.getIId());
						query1.setParameter(2,
								Helper.cutTimestamp(new Timestamp(c
										.getTimeInMillis())));
						query1.setParameter(3, Helper
								.cutTimestamp(new Timestamp(
										c.getTimeInMillis() + 24 * 3600000)));

						ZeitdatenDto[] zeitdatenDtos = assembleZeitdatenDtosOhneBelegzeiten(query1
								.getResultList());
						if (zeitdatenDtos.length > 1) {
							Query query = em
									.createNamedQuery("TaetigkeitfindByBTagbuchbar");
							query.setParameter(1, Helper.boolean2Short(false));
							TaetigkeitDto[] dtos = assembleTaetigkeitDtos(query
									.getResultList());
							double dDauerGesamt = 0;
							for (int m = 0; m < dtos.length; m++) {
								TaetigkeitDto taetigkeit = dtos[m];
								if (taetigkeit.getCNr().equals(
										ZeiterfassungFac.TAETIGKEIT_ENDE)
										|| taetigkeit
												.getCNr()
												.equals(ZeiterfassungFac.TAETIGKEIT_GEHT)
										|| taetigkeit
												.getCNr()
												.equals(ZeiterfassungFac.TAETIGKEIT_KOMMT)
										|| taetigkeit
												.getCNr()
												.equals(ZeiterfassungFac.TAETIGKEIT_KRANK)) {
								} else {
									try {
										dDauerGesamt += berechnePaarweiserSondertaetigkeiten(
												zeitdatenDtos,
												taetigkeit.getIId());
									} catch (Exception ex2) {
										// FEHLERHAFTE BUCHUNGEN
									}
								}
							}

							d = new Double(d.doubleValue() - dDauerGesamt);
							if (d.doubleValue() < 0) {
								d = new Double(0);
							}
						}
						// }
						// catch (FinderException ex1) {
						// }
					}

					session = factory.openSession();
					crit = session
							.createCriteria(FLRPersonalverfuegbarkeit.class)
							.createAlias("flrpersonal", "p")
							.add(Restrictions.eq("p.i_id", personalDto.getIId()));

					resultList = crit.list();

					resultList = crit.list();

					resultListIterator = resultList.iterator();
					while (resultListIterator.hasNext()) {
						FLRPersonalverfuegbarkeit flrPersonalverfuegbarkeit = (FLRPersonalverfuegbarkeit) resultListIterator
								.next();

						double dVerfuegbareZeit = d.doubleValue()
								* (flrPersonalverfuegbarkeit
										.getF_anteilprozent().doubleValue() / 100);

						// Sonderzeiten abziehen

						if (flrPersonalverfuegbarkeit.getFlrartikel()
								.getFlrartikelgruppe() != null) {
							Integer artikelgruppeIId = flrPersonalverfuegbarkeit
									.getFlrartikel().getFlrartikelgruppe()
									.getI_id();

							if (artikelgruppen.containsKey(artikelgruppeIId)) {
								BigDecimal dZeitvorhanden = (BigDecimal) artikelgruppen
										.get(artikelgruppeIId);
								artikelgruppen.put(artikelgruppeIId,
										dZeitvorhanden.add(new BigDecimal(
												dVerfuegbareZeit)));

							} else {
								artikelgruppen.put(artikelgruppeIId,
										new BigDecimal(dVerfuegbareZeit));
							}

						} else {

							artikelgruppeSonstiges = artikelgruppeSonstiges
									.add(new BigDecimal(dVerfuegbareZeit));
						}

					}

					session.close();
				}
			}

			Iterator<?> ag = artikelgruppen.keySet().iterator();
			while (ag.hasNext()) {
				Integer key = (Integer) ag.next();
				BigDecimal value = (BigDecimal) artikelgruppen.get(key);

				SollverfuegbarkeitDto dtoGruppe = new SollverfuegbarkeitDto();
				dtoGruppe.setBMannarbeitszeit(true);
				dtoGruppe.setIGruppeid(key);
				dtoGruppe.setNSollstunden(Helper.rundeKaufmaennisch(value, 2));
				dtoGruppe
						.setTDatum(new java.sql.Timestamp(c.getTimeInMillis()));
				if (value.doubleValue() > 0) {
					alArtikelgruppen.add(dtoGruppe);
				}
			}

			SollverfuegbarkeitDto dtoGruppeSonstiges = new SollverfuegbarkeitDto();
			dtoGruppeSonstiges.setBMannarbeitszeit(true);
			dtoGruppeSonstiges.setTDatum(new java.sql.Timestamp(c
					.getTimeInMillis()));
			dtoGruppeSonstiges.setNSollstunden(Helper.rundeKaufmaennisch(
					artikelgruppeSonstiges, 2));
			if (artikelgruppeSonstiges.doubleValue() > 0) {
				alArtikelgruppen.add(dtoGruppeSonstiges);
			}
			artikelgruppen = new HashMap<Object, Number>();
			artikelgruppeSonstiges = new BigDecimal(0);

			c.set(c.DATE, c.get(c.DATE) + 1);
		}

		int groesse = maschinengruppen.size() + alArtikelgruppen.size();

		SollverfuegbarkeitDto[] sollverfuegbarkeitDtos = new SollverfuegbarkeitDto[groesse];
		int iZeile = 0;

		Iterator<?> it = maschinengruppen.keySet().iterator();
		while (it.hasNext()) {
			Integer key = (Integer) it.next();
			BigDecimal stunden = (BigDecimal) maschinengruppen.get(key);
			SollverfuegbarkeitDto dto = new SollverfuegbarkeitDto();
			dto.setBMannarbeitszeit(false);
			dto.setIGruppeid(key);
			dto.setNSollstunden(Helper.rundeKaufmaennisch(stunden, 2));
			dto.setTDatum(null);
			sollverfuegbarkeitDtos[iZeile] = dto;
			iZeile++;
		}

		// sollverfuegbarkeitDtos[iZeile]=
		for (int i = 0; i < alArtikelgruppen.size(); i++) {

			sollverfuegbarkeitDtos[iZeile] = (SollverfuegbarkeitDto) alArtikelgruppen
					.get(i);
			iZeile++;
		}

		return sollverfuegbarkeitDtos;
	}

	/**
	 * Berechne die TagesArbeitszeit eines Zeitraumes zu einer Person, ohne
	 * Paarweisen Sondertaetigkeiten meldet Fehler, wenn die Tagesarbeitszeit
	 * nicht berechnet werden konnte
	 *
	 * @param personalIId
	 *            Die Person
	 * @param dDatumVon
	 *            Das Von
	 * @param dDatumBis
	 *            Das Bis
	 * @param theClientDto
	 *            User-ID
	 * @return String beinhaltet Tagesarbeitszeit
	 * @exception EJBExceptionLP
	 */
	@SuppressWarnings("static-access")
	public Double berechneArbeitszeitImZeitraum(Integer personalIId,
			java.sql.Date dDatumVon, java.sql.Date dDatumBis,
			boolean bAbzueglichTelefonzeiten, TheClientDto theClientDto) {
		if (personalIId == null || dDatumVon == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("personalIId == null || dDatumVon == null"));
		}
		Helper.cutDate(dDatumVon);
		Helper.cutDate(dDatumBis);

		// Hole id der Taetigkeit ENDE
		Integer taetigkeitIId_Ende = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_ENDE, theClientDto).getIId();
		// Hole id der Taetigkeit KOMMT
		Integer taetigkeitIId_Kommt = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_KOMMT, theClientDto).getIId();
		// Hole id der Taetigkeit GEHT
		Integer taetigkeitIId_Geht = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_GEHT, theClientDto).getIId();
		// Hole id der Taetigkeit GEHT
		Integer taetigkeitIId_Telefon = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_TELEFON, theClientDto).getIId();

		ArrayList<Object> daten = new ArrayList<Object>();
		// Alle Zeitdaten des Zeitraums holen
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();
		org.hibernate.Criteria zeitdatenEinesMonats = session
				.createCriteria(FLRZeitdaten.class);

		zeitdatenEinesMonats.add(Expression.eq(
				ZeiterfassungFac.FLR_ZEITDATEN_PERSONAL_I_ID, personalIId));
		zeitdatenEinesMonats.add(Expression.ge(
				ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT, new java.sql.Timestamp(
						dDatumVon.getTime())));
		zeitdatenEinesMonats.add(Expression.lt(
				ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT, new java.sql.Timestamp(
						dDatumBis.getTime())));
		zeitdatenEinesMonats.addOrder(Order
				.asc(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT));

		List<?> resultListZeitdaten = zeitdatenEinesMonats.list();

		Iterator<?> resultListIterator = resultListZeitdaten.iterator();

		List<ZeitdatenDto> tagZeitdaten = new ArrayList<ZeitdatenDto>();

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(dDatumVon.getTime());

		int iTagVorher = c.get(c.DAY_OF_MONTH);
		int rowZeile = 0;
		// Monatsdaten auf Tagesdaten aufteilen
		while (resultListIterator.hasNext()) {
			FLRZeitdaten flrzeitdaten = (FLRZeitdaten) resultListIterator
					.next();

			if (flrzeitdaten.getFlrtaetigkeit() != null
					&& (taetigkeitIId_Ende.equals(flrzeitdaten
							.getFlrtaetigkeit().getI_id()))) {

			} else {

				Calendar cTemp = Calendar.getInstance();
				cTemp.setTimeInMillis(flrzeitdaten.getT_zeit().getTime());

				ZeitdatenDto tempDto = new ZeitdatenDto();

				if (flrzeitdaten.getFlrtaetigkeit() != null) {
					tempDto.setTaetigkeitIId(flrzeitdaten.getFlrtaetigkeit()
							.getI_id());
				} else if (flrzeitdaten.getFlrartikel() != null) {
					tempDto.setArtikelIId(flrzeitdaten.getFlrartikel()
							.getI_id());
					tempDto.setCBelegartnr(flrzeitdaten.getC_belegartnr());
					tempDto.setIBelegartpositionid(flrzeitdaten
							.getI_belegartpositionid());
					tempDto.setIBelegartid(flrzeitdaten.getI_belegartid());
				}
				tempDto.setTZeit(new Timestamp(flrzeitdaten.getT_zeit()
						.getTime()));
				tempDto.setIId(flrzeitdaten.getI_id());

				int iTagAktuell = cTemp.get(cTemp.DAY_OF_MONTH);
				if (iTagVorher != iTagAktuell) {
					ZeitdatenDto[] returnArrayZeitdaten = new ZeitdatenDto[tagZeitdaten
							.size()];
					daten.add(tagZeitdaten.toArray(returnArrayZeitdaten));
					/*
					 * daten[iTagVorher - 1] = (ZeitdatenDto[])
					 * tagZeitdaten.toArray(returnArrayZeitdaten);
					 */
					tagZeitdaten = new ArrayList<ZeitdatenDto>();

					if (flrzeitdaten.getFlrtaetigkeit() != null) {
						tagZeitdaten.add(tempDto);
					}
				} else {

					if (flrzeitdaten.getFlrtaetigkeit() != null) {
						tagZeitdaten.add(tempDto);
					}

					if (resultListIterator.hasNext() == false) {
						ZeitdatenDto[] returnArrayZeitdaten = new ZeitdatenDto[tagZeitdaten
								.size()];
						if (flrzeitdaten.getFlrtaetigkeit() != null) {
							daten.add(tagZeitdaten
									.toArray(returnArrayZeitdaten));

							/*
							 * daten[iTagAktuell - 1] = (ZeitdatenDto[])
							 * tagZeitdaten.toArray(returnArrayZeitdaten);
							 */
						}
					}
				}
				iTagVorher = iTagAktuell;
			}

			rowZeile++;
		}
		session.close();
		double dGesamt = 0;

		try {
			for (int k = 0; k < daten.size(); k++) {
				ZeitdatenDto[] zeitdatenDtos = (ZeitdatenDto[]) daten.get(k);

				java.sql.Time u_gesamt = new java.sql.Time(-3600000);

				java.sql.Time u_before = null;

				java.sql.Time u_kommt = null;
				java.sql.Time u_geht = null;
				boolean b_kommt = false;
				boolean bEnde = false;
				Integer sTaetigkeit_before = null;

				// Wenn Zeitdaten zum gewuenschten Tag vorhanden sind, dann
				// Tagesarbeitszeit berechnen
				if (zeitdatenDtos != null && zeitdatenDtos.length > 0) {

					zeitdatenDtos = simuliereMitternachssprung(personalIId,
							new java.sql.Date(zeitdatenDtos[0].getTZeit()
									.getTime()), taetigkeitIId_Kommt,
							taetigkeitIId_Geht, zeitdatenDtos);

					for (int i = 0; i < zeitdatenDtos.length; i++) {
						ZeitdatenDto row = zeitdatenDtos[i];
						java.sql.Time u_aktuell = new java.sql.Time(row
								.getTZeit().getTime());

						Integer sTaetigkeit_aktuell = row.getTaetigkeitIId();

						if (!sTaetigkeit_aktuell.equals(taetigkeitIId_Kommt)
								&& i == 0) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_ZEITERFASSUNG_TAETIGKEIT_VOR_KOMMT,
									new Exception(
											"FEHLER_ZEITERFASSUNG_TAETIGKEIT_VOR_KOMMT"));
						}

						// Das KOMMT herausfinden
						if (sTaetigkeit_aktuell.equals(taetigkeitIId_Kommt)
								&& b_kommt == false) {
							u_kommt = new java.sql.Time(row.getTZeit()
									.getTime());
							b_kommt = true;

						}

						if (bEnde == false) {
							if (sTaetigkeit_aktuell.equals(sTaetigkeit_before)) {
								if (sTaetigkeit_aktuell
										.equals(taetigkeitIId_Kommt)) {
									throw new EJBExceptionLP(
											EJBExceptionLP.FEHLER_ZEITERFASSUNG_MEHRFACHES_KOMMT,
											new Exception(
													"FEHLER_ZEITERFASSUNG_MEHRFACHES_KOMMT"));
								}
								if (sTaetigkeit_aktuell
										.equals(taetigkeitIId_Geht)) {
									throw new EJBExceptionLP(
											EJBExceptionLP.FEHLER_ZEITERFASSUNG_MEHRFACHES_GEHT,
											new Exception(
													"FEHLER_ZEITERFASSUNG_MEHRFACHES_GEHT"));
								}

								java.sql.Time u_pause = new java.sql.Time(row
										.getTZeit().getTime());
								long l_pause = u_pause.getTime()
										- u_before.getTime();

								if (bAbzueglichTelefonzeiten == false) {
									if (!sTaetigkeit_aktuell
											.equals(taetigkeitIId_Telefon)) {
										u_gesamt.setTime(u_gesamt.getTime()
												- l_pause);
									}
								} else {
									u_gesamt.setTime(u_gesamt.getTime()
											- l_pause);
								}

								bEnde = true;

							}
						} else {
							bEnde = false;
						}

						if (sTaetigkeit_aktuell.equals(taetigkeitIId_Geht)) {
							if (u_kommt == null) {
								throw new EJBExceptionLP(
										EJBExceptionLP.FEHLER_ZEITERFASSUNG_GEHT_OHNE_KOMMT,
										new Exception(
												"FEHLER_ZEITERFASSUNG_GEHT_OHNE_KOMMT"));
							}
							u_geht = (java.sql.Time) new java.sql.Time(row
									.getTZeit().getTime());
							u_gesamt.setTime(u_gesamt.getTime()
									+ (u_geht.getTime() - u_kommt.getTime()));
							b_kommt = false;
						}

						sTaetigkeit_before = sTaetigkeit_aktuell;
						u_before = u_aktuell;
					}
				}
				dGesamt = dGesamt + Helper.time2Double(u_gesamt).doubleValue();
			}
		} catch (EJBExceptionLP ex) {
			// SP2936
			return -1D;
		}
		return new Double(dGesamt);
	}

	public double berechneDauerPaarweiserSondertaetigkeitenEinerPersonUndEinesZeitraumes(
			Integer personalIId, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, Integer iTaetigkeit) {
		tVon = Helper.cutTimestamp(tVon);
		tBis = Helper.cutTimestamp(tBis);

		double dDauerGesamt = 0;

		for (long l = tVon.getTime(); l <= tBis.getTime(); l = l
				+ (24 * 3600000)) {
			java.sql.Timestamp t_datum = new java.sql.Timestamp(l);
			try {
				Query query = em
						.createNamedQuery("ZeitdatenfindZeitdatenEinesTagesUndEinerPerson");
				query.setParameter(1, personalIId);
				query.setParameter(2, Helper.cutTimestamp(t_datum));
				query.setParameter(3, Helper.cutTimestamp(new Timestamp(t_datum
						.getTime() + 24 * 3600000)));

				ZeitdatenDto[] dtos = assembleZeitdatenDtos(query
						.getResultList());

				// Wenn Mehr als 2 buchungen sind
				if (dtos.length > 2) {
					dDauerGesamt = dDauerGesamt
							+ berechnePaarweiserSondertaetigkeiten(dtos,
									iTaetigkeit);
				}
			} catch (Exception e) {
				/** @todo Was machen wir hier PJ 4659 */
			}

		}
		return dDauerGesamt;
	}

	/**
	 * Ermittelt die Summe paarweiser Sondertaetigkeiten eines Tages (dezimal in
	 * Stunden)
	 *
	 * @param personalIId
	 * @param iMonat
	 * @param iJahr
	 * @return Summe der Unterbrechungen
	 * @exception RemoteException
	 * @exception Exception
	 */
	public BigDecimal berechneKalkJahresIstStunden(Integer personalIId,
			Integer iMonat, Integer iJahr, TheClientDto theClientDto) {

		try {
			ParametermandantDto parameter = (ParametermandantDto) getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_PERSONAL,
							ParameterFac.PARAMETER_LOHNSTUNDENSATZKALKULATION_KALENDERWOCHEN);

			Double dLohnstundensatzKalenderwochen = (Double) parameter
					.getCWertAsObject();
			parameter = (ParametermandantDto) getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_PERSONAL,
							ParameterFac.PARAMETER_LOHNSTUNDENSATZKALKULATION_KRANKWOCHEN);

			Double dLohnstundensatzKrankwochen = (Double) parameter
					.getCWertAsObject();
			parameter = (ParametermandantDto) getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_PERSONAL,
							ParameterFac.PARAMETER_LOHNSTUNDENSATZKALKULATION_FEIERTAGSWOCHEN);

			Double dLohnstundensatzFeiertagswochen = (Double) parameter
					.getCWertAsObject();

			if (dLohnstundensatzKalenderwochen > 0) {
				Calendar c = Calendar.getInstance();
				c.set(Calendar.YEAR, iJahr);
				c.set(Calendar.MONTH, iMonat);
				c.set(Calendar.DATE, 1);

				PersonalzeitmodellDto dto = getPersonalFac()
						.personalzeitmodellFindZeitmodellZuDatum(
								personalIId,
								Helper.cutTimestamp(new java.sql.Timestamp(c
										.getTimeInMillis())), theClientDto);
				if (dto != null) {

					double dJahresurlaubInWochen = 0;
					UrlaubsanspruchDto[] uDtos = getPersonalFac()
							.urlaubsanspruchFindByPersonalIIdIJahrKleiner(
									personalIId, iJahr);
					if (uDtos.length > 0) {
						dJahresurlaubInWochen = uDtos[0]
								.getFJahresurlaubinwochen();

					}

					return new BigDecimal(
							getSummeSollzeitMontagBisSonntag(dto
									.getZeitmodellIId())
									* (dLohnstundensatzKalenderwochen
											- dJahresurlaubInWochen
											- dLohnstundensatzKrankwochen - dLohnstundensatzFeiertagswochen));
				} else {
					return BigDecimal.ZERO;
				}
			}

		} catch (RemoteException ex5) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
		}

		return BigDecimal.ZERO;
	}

	public double berechnePaarweiserSondertaetigkeiten(
			ZeitdatenDto[] zeitdaten, Integer iTaetigkeit) throws Exception {

		java.sql.Time tUnterbrechungszeit = null;
		Float fUnterbrechungszeit = null;
		Integer iStelleLetzteUnterbrechungBeginn = null;
		boolean bUnterbrechungBeginn = false;
		Float fUnterbrechungBeginn = null;
		Float fUnterbrechungEnde = null;

		double fSummeUnterbrechungen = 0;

		int iAnzahlBuchungen = zeitdaten.length;
		ZeitdatenDto vEineZeile = null;

		try {
			for (int i = 0; i < iAnzahlBuchungen; i++) {
				vEineZeile = zeitdaten[i];

				if (iTaetigkeit.equals(vEineZeile.getTaetigkeitIId())) {
					tUnterbrechungszeit = null;
					// Konvertierung der Unterbrechungszeit in ein Float-Objekt
					tUnterbrechungszeit = new java.sql.Time(vEineZeile
							.getTZeit().getTime());
					Calendar c = Calendar.getInstance();
					c.setTimeInMillis(tUnterbrechungszeit.getTime());
					float hour = c.get(Calendar.HOUR_OF_DAY);
					float minuten = c.get(Calendar.MINUTE);
					float minutendecimal = minuten / 60;
					minutendecimal = Math.round(minutendecimal * 100) / 100f;
					fUnterbrechungszeit = new Float(hour + minutendecimal);

					// Es gibt noch keinen 'Unterbrechung Beginn'
					if (bUnterbrechungBeginn == false) {
						fUnterbrechungBeginn = fUnterbrechungszeit;
						bUnterbrechungBeginn = true;
						iStelleLetzteUnterbrechungBeginn = new Integer(i);
					}
					// Es gibt schon einen 'Beginn- Eintrag'
					// Demnach muss dies jetzt der 'Ende- Eintrag' sein
					// und zwar direkt folgend nach dem Beginn -> dh. zw.
					// Unterbrechung Beginn und
					// Unterbrechung Ende darf keine weitere Buchung vorhanden
					// sein
					else {
						// kein Buchung zw. Unterbrechung Beginn und Ende
						if ((i - 1) == iStelleLetzteUnterbrechungBeginn
								.intValue()) {
							fUnterbrechungEnde = fUnterbrechungszeit;
							bUnterbrechungBeginn = false;
							iStelleLetzteUnterbrechungBeginn = null;
						}
						// Buchung zw. Unterbrechung Beginn und Ende
						else {
							Taetigkeit taetigkeit = em.find(Taetigkeit.class,
									iTaetigkeit);
							throw new Exception("Buchung zw. "
									+ taetigkeit.getCNr().trim()
									+ " Beginn/Ende");
						}
					}

					if (fUnterbrechungBeginn != null
							&& fUnterbrechungEnde != null) {
						// Addieren der Pause zur Summe der Unterbrechungen
						fSummeUnterbrechungen = fSummeUnterbrechungen
								+ (fUnterbrechungEnde.floatValue() - fUnterbrechungBeginn
										.floatValue());
						fUnterbrechungBeginn = null;
						fUnterbrechungEnde = null;
					}
				}
			}
			if (fUnterbrechungBeginn != null && fUnterbrechungEnde == null) {
				// Nach Unterbrechung Beginn folgt kein Unterbrechung Ende
				Taetigkeit taetigkeit = em.find(Taetigkeit.class, iTaetigkeit);
				throw new Exception("kein " + taetigkeit.getCNr().trim()
						+ " Ende");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		// Runden auf 2-Nachkommastellen
		fSummeUnterbrechungen = Math.round(fSummeUnterbrechungen * 100) / 100f;

		return fSummeUnterbrechungen;
	}

	public Integer createZeitmodell(ZeitmodellDto zeitmodellDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (zeitmodellDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zeitmodellDto == null"));
		}
		if (zeitmodellDto.getCNr() == null
				|| zeitmodellDto.getFUrlaubstageprowoche() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"zeitmodellDto.getCNr() == null || zeitmodellDto.getIUrlaubstageprowoche() == null"));
		}
		try {
			Query query = em.createNamedQuery("ZeitmodellfindByCNrMandantCNr");
			query.setParameter(1, zeitmodellDto.getCNr());
			query.setParameter(2, theClientDto.getMandant());
			// @todo getSingleResult oder getResultList ?
			Zeitmodell doppelt = (Zeitmodell) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_ZEITMODELL.CNR"));
		} catch (NoResultException ex) {

		}

		if (zeitmodellDto.getIMinutenabzug() == null) {
			zeitmodellDto.setIMinutenabzug(0);
		}

		if (zeitmodellDto.getBFeiertagssollAddieren() == null) {
			zeitmodellDto.setBFeiertagssollAddieren(Helper.boolean2Short(true));
		}

		if (zeitmodellDto.getBFixepauseTrotzkommtgeht() == null) {
			zeitmodellDto.setBFixepauseTrotzkommtgeht(Helper
					.boolean2Short(false));
		}

		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ZEITMODELL);
		zeitmodellDto.setIId(pk);
		zeitmodellDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		zeitmodellDto.setTAendern(new java.sql.Timestamp(System
				.currentTimeMillis()));
		try {
			Zeitmodell zeitmodell = new Zeitmodell(zeitmodellDto.getIId(),
					theClientDto.getMandant(), zeitmodellDto.getCNr(),
					zeitmodellDto.getPersonalIIdAendern(),
					zeitmodellDto.getFUrlaubstageprowoche(),
					zeitmodellDto.getBDynamisch(),
					zeitmodellDto.getIMinutenabzug(),
					zeitmodellDto.getBFeiertagssollAddieren(),
					zeitmodellDto.getBFixepauseTrotzkommtgeht());
			em.persist(zeitmodell);
			em.flush();
			if (zeitmodellDto.getBTeilzeit() == null) {
				zeitmodellDto.setBTeilzeit(zeitmodell.getBTeilzeit());
			}
			if (zeitmodellDto.getBVersteckt() == null) {
				zeitmodellDto.setBVersteckt(zeitmodell.getBVersteckt());
			}
			setZeitmodellFromZeitmodellDto(zeitmodell, zeitmodellDto);
			if (zeitmodellDto.getZeitmodellsprDto() != null) {
				Zeitmodellspr zeitmodellspr = new Zeitmodellspr(
						zeitmodellDto.getIId(),
						theClientDto.getLocMandantAsString());
				em.persist(zeitmodellspr);
				em.flush();
				setZeitmodellsprFromZeitmodellsprDto(zeitmodellspr,
						zeitmodellDto.getZeitmodellsprDto());
			}
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return zeitmodellDto.getIId();
	}

	public void removeZeitmodell(ZeitmodellDto zeitmodellDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (zeitmodellDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zeitmodellDto == null"));
		}
		if (zeitmodellDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("zeitmodellDto.getIId() == null"));
		}
		try {
			// try {
			Query query = em
					.createNamedQuery("ZeitmodellsprfindByZeitmodellIId");
			query.setParameter(1, zeitmodellDto.getIId());
			Collection<?> allZeitmodellspr = query.getResultList();
			Iterator<?> iter = allZeitmodellspr.iterator();
			while (iter.hasNext()) {
				Zeitmodellspr artgrusprTemp = (Zeitmodellspr) iter.next();
				em.remove(artgrusprTemp);
			}
			// }
			// catch (RemoveException ex) {
			// throw new EJBExceptionLP(EJBExceptionLP.
			// FEHLER_BEIM_LOESCHEN, ex);
			// }
			Zeitmodell zeitmodell = em.find(Zeitmodell.class,
					zeitmodellDto.getIId());
			if (zeitmodell == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(zeitmodell);
			em.flush();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void updateZeitmodell(ZeitmodellDto zeitmodellDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (zeitmodellDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zeitmodellDto == null"));
		}
		if (zeitmodellDto.getIId() == null || zeitmodellDto.getCNr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"zeitmodellDto.getIId() == null || zeitmodellDto.getCNr() == null"));
		}
		if (zeitmodellDto.getBTeilzeit() == null
				|| zeitmodellDto.getBVersteckt() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"zeitmodellDto.getBTeilzeit() == null || zeitmodellDto.getBVersteckt() == null"));
		}
		Integer iId = zeitmodellDto.getIId();
		Zeitmodell zeitmodell = null;
		// try {
		zeitmodell = em.find(Zeitmodell.class, iId);
		if (zeitmodell == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		// }
		// catch (FinderException ex1) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex1);

		// }

		try {
			Query query = em.createNamedQuery("ZeitmodellfindByCNrMandantCNr");
			query.setParameter(1, zeitmodellDto.getCNr());
			query.setParameter(2, theClientDto.getMandant());
			Integer iIdVorhanden = ((Zeitmodell) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_ZEITMODELL.C_NR"));
			}

		} catch (NoResultException ex) {
			//
		}
		zeitmodell.setPersonalIIdAendern(theClientDto.getIDPersonal());
		zeitmodell.setTAendern(new java.sql.Timestamp(System
				.currentTimeMillis()));
		setZeitmodellFromZeitmodellDto(zeitmodell, zeitmodellDto);

		try {
			if (zeitmodellDto.getZeitmodellsprDto() != null) {
				Zeitmodellspr zeitmodellspr = em.find(
						Zeitmodellspr.class,
						new ZeitmodellsprPK(iId, theClientDto
								.getLocMandantAsString()));
				if (zeitmodellspr == null) {
					zeitmodellspr = new Zeitmodellspr(iId,
							theClientDto.getLocMandantAsString());
					em.persist(zeitmodellspr);
					em.flush();
				}
				setZeitmodellsprFromZeitmodellsprDto(zeitmodellspr,
						zeitmodellDto.getZeitmodellsprDto());
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
	}

	public void uebersteuereZeitmodellFuerEinenTag(Integer personalIId,
			Integer zeitmodellIId, java.sql.Date dDatum,
			TheClientDto theClientDto) {

		java.sql.Timestamp tDatum = Helper.cutTimestamp(new java.sql.Timestamp(
				dDatum.getTime()));
		java.sql.Timestamp tMorgen = Helper
				.cutTimestamp(new java.sql.Timestamp(
						dDatum.getTime() + 25 * 3600000));
		try {
			PersonalzeitmodellDto dto = getPersonalFac()
					.personalzeitmodellFindZeitmodellZuDatum(personalIId,
							tDatum, theClientDto);
			if (dto != null && zeitmodellIId != null
					&& dto.getZeitmodellIId().equals(zeitmodellIId)) {
				return;
			}

			PersonalzeitmodellDto dtoNeu = new PersonalzeitmodellDto();
			dtoNeu.setPersonalIId(personalIId);

			if (dto != null) {
				// Fuer heute anderes Zeitmodell eintragen, wenn heute keines
				// existiert, ansonsten updaten
				dtoNeu.setZeitmodellIId(zeitmodellIId);
				dtoNeu.setTGueltigab(tDatum);
				if (!tDatum.equals(dto.getTGueltigab())) {
					// Heute eintragen
					getPersonalFac().createPersonalzeitmodell(dtoNeu);
					// Morgen eintragen, wenn keines exitiert
					PersonalzeitmodellDto dtoExists = getPersonalFac()
							.personalzeitmodellFindByPersonalIIdTDatumOhneExc(
									personalIId, tMorgen);
					if (dtoExists == null) {
						dto.setTGueltigab(tMorgen);
						getPersonalFac().createPersonalzeitmodell(dto);
					}
				} else {
					dtoNeu.setIId(dto.getIId());
					getPersonalFac().updatePersonalzeitmodell(dtoNeu);
					PersonalzeitmodellDto dtoExists = getPersonalFac()
							.personalzeitmodellFindByPersonalIIdTDatumOhneExc(
									personalIId, tMorgen);
					if (dtoExists != null) {
						dtoExists.setZeitmodellIId(dto.getZeitmodellIId());
						getPersonalFac().updatePersonalzeitmodell(dtoExists);
					} else {
						dto.setTGueltigab(tMorgen);

						getPersonalFac().createPersonalzeitmodell(dto);

					}

				}
			} else {
				dtoNeu.setTGueltigab(tDatum);
				dtoNeu.setZeitmodellIId(zeitmodellIId);
				getPersonalFac().createPersonalzeitmodell(dtoNeu);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public ZeitmodellDto zeitmodellFindByCNr(String cNr,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			Query query = em.createNamedQuery("ZeitmodellfindByCNrMandantCNr");
			query.setParameter(1, cNr);
			query.setParameter(2, theClientDto.getMandant());
			Zeitmodell zeitmodell = (Zeitmodell) query.getSingleResult();
			return assembleZeitmodellDto(zeitmodell);
		} catch (NoResultException e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}

	}

	public ZeitmodellDto zeitmodellFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Zeitmodell zeitmodell = em.find(Zeitmodell.class, iId);
		if (zeitmodell == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		ZeitmodellDto artgruDto = assembleZeitmodellDto(zeitmodell);
		ZeitmodellsprDto zeitmodellsprDto = null;
		// try {
		Zeitmodellspr zeitmodellspr = em.find(Zeitmodellspr.class,
				new ZeitmodellsprPK(iId, theClientDto.getLocMandantAsString()));
		if (zeitmodellspr != null) {
			zeitmodellsprDto = assembleZeitmodellsprDto(zeitmodellspr);
			// if (zeitmodellsprDto == null) {
			// nothing here
		}
		// }
		// catch (NoResultException ex) {
		// nothing here
		// }
		if (zeitmodellsprDto == null) {
			// try {
			Zeitmodellspr temp = em.find(
					Zeitmodellspr.class,
					new ZeitmodellsprPK(iId, theClientDto
							.getLocKonzernAsString()));
			if (temp != null) {
				zeitmodellsprDto = assembleZeitmodellsprDto(temp);
			}
			// }
			// catch (NoResultException ex) {
			// nothing here
			// }
		}
		artgruDto.setZeitmodellsprDto(zeitmodellsprDto);
		return artgruDto;
		// }
		// catch (Exception e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setZeitmodellFromZeitmodellDto(Zeitmodell zeitmodell,
			ZeitmodellDto zeitmodellDto) {
		zeitmodell.setCNr(zeitmodellDto.getCNr());
		zeitmodell.setBTeilzeit(zeitmodellDto.getBTeilzeit());
		zeitmodell.setBVersteckt(zeitmodellDto.getBVersteckt());
		zeitmodell.setFUrlaubstageprowoche(zeitmodellDto
				.getFUrlaubstageprowoche());
		zeitmodell.setNSollstundenfix(zeitmodellDto.getNSollstundenfix());
		zeitmodell.setBDynamisch(zeitmodellDto.getBDynamisch());
		zeitmodell.setIMinutenabzug(zeitmodellDto.getIMinutenabzug());
		zeitmodell.setBFeiertagssollAddieren(zeitmodellDto
				.getBFeiertagssollAddieren());
		zeitmodell.setNMaximalesWochenist(zeitmodellDto
				.getNMaximalesWochenist());
		zeitmodell.setBFixepauseTrotzkommtgeht(zeitmodellDto
				.getBFixepauseTrotzkommtgeht());
		em.merge(zeitmodell);
		em.flush();
	}

	private ZeitmodellDto assembleZeitmodellDto(Zeitmodell zeitmodell) {
		return ZeitmodellDtoAssembler.createDto(zeitmodell);
	}

	private ZeitmodellDto[] assembleZeitmodellDtos(Collection<?> zeitmodells) {
		List<ZeitmodellDto> list = new ArrayList<ZeitmodellDto>();
		if (zeitmodells != null) {
			Iterator<?> iterator = zeitmodells.iterator();
			while (iterator.hasNext()) {
				Zeitmodell zeitmodell = (Zeitmodell) iterator.next();
				list.add(assembleZeitmodellDto(zeitmodell));
			}
		}
		ZeitmodellDto[] returnArray = new ZeitmodellDto[list.size()];
		return (ZeitmodellDto[]) list.toArray(returnArray);
	}

	private void setZeitmodellsprFromZeitmodellsprDto(
			Zeitmodellspr zeitmodellspr, ZeitmodellsprDto zeitmodellsprDto) {
		zeitmodellspr.setCBez(zeitmodellsprDto.getCBez());
		em.merge(zeitmodellspr);
		em.flush();
	}

	private ZeitmodellsprDto assembleZeitmodellsprDto(
			Zeitmodellspr zeitmodellspr) {
		return ZeitmodellsprDtoAssembler.createDto(zeitmodellspr);
	}

	private ZeitmodellsprDto[] assembleZeitmodellsprDtos(
			Collection<?> zeitmodellsprs) {
		List<ZeitmodellsprDto> list = new ArrayList<ZeitmodellsprDto>();
		if (zeitmodellsprs != null) {
			Iterator<?> iterator = zeitmodellsprs.iterator();
			while (iterator.hasNext()) {
				Zeitmodellspr zeitmodellspr = (Zeitmodellspr) iterator.next();
				list.add(assembleZeitmodellsprDto(zeitmodellspr));
			}
		}
		ZeitmodellsprDto[] returnArray = new ZeitmodellsprDto[list.size()];
		return (ZeitmodellsprDto[]) list.toArray(returnArray);
	}

	public Integer createZeitmodelltag(ZeitmodelltagDto zeitmodelltagDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (zeitmodelltagDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zeitmodelltagDto == null"));
		}
		if (zeitmodelltagDto.getUSollzeit() == null
				|| zeitmodelltagDto.getZeitmodellIId() == null
				|| zeitmodelltagDto.getTagesartIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zeitmodelltagDto.getUSollzeit() == null || zeitmodelltagDto.getZeitmodellIId() == null || zeitmodelltagDto.getTagesartIId() == null"));
		}
		if (zeitmodelltagDto.getIRundungbeginn() == null
				|| zeitmodelltagDto.getIRundungende() == null
				|| zeitmodelltagDto.getBRundesondertaetigkeiten() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zeitmodelltagDto.getIRundungbeginn() == null || zeitmodelltagDto.getIRundungende() == null || zeitmodelltagDto.getBRundesondertaetigkeiten()"));
		}
		try {
			Query query = em
					.createNamedQuery("ZeitmodelltagfindByZeitmodellIIdTagesartIId");
			query.setParameter(1, zeitmodelltagDto.getZeitmodellIId());
			query.setParameter(2, zeitmodelltagDto.getTagesartIId());
			Zeitmodelltag doppelt = (Zeitmodelltag) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_ZEITMODELLTAG.CNR"));
		} catch (NoResultException ex) {
			//
		}

		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ZEITMODELLTAG);
		zeitmodelltagDto.setIId(pk);
		zeitmodelltagDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		zeitmodelltagDto.setTAendern(new Timestamp(System.currentTimeMillis()));
		try {
			Zeitmodelltag zeitmodelltag = new Zeitmodelltag(
					zeitmodelltagDto.getIId(),
					zeitmodelltagDto.getZeitmodellIId(),
					zeitmodelltagDto.getTagesartIId(),
					zeitmodelltagDto.getPersonalIIdAendern(),
					zeitmodelltagDto.getUSollzeit(),
					zeitmodelltagDto.getIRundungbeginn(),
					zeitmodelltagDto.getIRundungende(),
					zeitmodelltagDto.getBRundesondertaetigkeiten());
			em.persist(zeitmodelltag);
			em.flush();
			setZeitmodelltagFromZeitmodelltagDto(zeitmodelltag,
					zeitmodelltagDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return zeitmodelltagDto.getIId();
	}

	public void removeZeitmodelltag(ZeitmodelltagDto zeitmodelltagDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (zeitmodelltagDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zeitmodelltagDto == null"));
		}
		if (zeitmodelltagDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("zeitmodelltagDto.getIId() == null"));
		}

		// try {
		Zeitmodelltag toRemove = em.find(Zeitmodelltag.class,
				zeitmodelltagDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN,
		// e);
		// }
	}

	public void updateZeitmodelltag(ZeitmodelltagDto zeitmodelltagDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (zeitmodelltagDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zeitmodelltagDto == null"));
		}
		if (zeitmodelltagDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("zeitmodelltagDto.getIId() == null"));
		}
		if (zeitmodelltagDto.getUSollzeit() == null
				|| zeitmodelltagDto.getZeitmodellIId() == null
				|| zeitmodelltagDto.getTagesartIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zeitmodelltagDto.getUSollzeit() == null || zeitmodelltagDto.getZeitmodellIId() == null || zeitmodelltagDto.getTagesartIId() == null"));
		}

		Integer iId = zeitmodelltagDto.getIId();
		// try {
		Zeitmodelltag zeitmodelltag = em.find(Zeitmodelltag.class, iId);
		if (zeitmodelltag == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		try {
			Query query = em
					.createNamedQuery("ZeitmodelltagfindByZeitmodellIIdTagesartIId");
			query.setParameter(1, zeitmodelltagDto.getZeitmodellIId());
			query.setParameter(2, zeitmodelltagDto.getTagesartIId());
			Integer iIdVorhanden = ((Zeitmodelltag) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_ZEITMODELLTAG.UK"));
			}
		} catch (NoResultException ex) {

		}

		zeitmodelltagDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		zeitmodelltagDto.setTAendern(new java.sql.Timestamp(System
				.currentTimeMillis()));

		setZeitmodelltagFromZeitmodelltagDto(zeitmodelltag, zeitmodelltagDto);
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }

	}

	public ZeitmodelltagDto zeitmodelltagFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		// try {
		Zeitmodelltag zeitmodelltag = em.find(Zeitmodelltag.class, iId);
		if (zeitmodelltag == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleZeitmodelltagDto(zeitmodelltag);
		// }
		// catch (NoResultException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public ZeitmodelltagDto[] zeitmodelltagFindByZeitmodellIId(Integer iId)
			throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("ZeitmodelltagfindByZeitmodellIId");
		query.setParameter(1, iId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty() ) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		return assembleZeitmodelltagDtos(cl);
		// }
		// catch (NoResultException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }

	}

	private void setZeitmodelltagFromZeitmodelltagDto(
			Zeitmodelltag zeitmodelltag, ZeitmodelltagDto zeitmodelltagDto) {
		zeitmodelltag.setZeitmodellIId(zeitmodelltagDto.getZeitmodellIId());
		zeitmodelltag.setTagesartIId(zeitmodelltagDto.getTagesartIId());
		zeitmodelltag.setUSollzeit(zeitmodelltagDto.getUSollzeit());
		zeitmodelltag.setUMindestpause(zeitmodelltagDto.getUMindestpause());
		zeitmodelltag.setUMindestpause2(zeitmodelltagDto.getUMindestpause2());
		zeitmodelltag.setUMindestpause3(zeitmodelltagDto.getUMindestpause3());
		zeitmodelltag.setIMindestpausenanzahl(zeitmodelltagDto
				.getIMindestpausenanzahl());
		zeitmodelltag.setIRundungbeginn(zeitmodelltagDto.getIRundungbeginn());
		zeitmodelltag.setIRundungende(zeitmodelltagDto.getIRundungende());
		zeitmodelltag.setUBeginn(zeitmodelltagDto.getUBeginn());
		zeitmodelltag.setUEnde(zeitmodelltagDto.getUEnde());
		zeitmodelltag.setUUeberstd(zeitmodelltagDto.getUUeberstd());
		zeitmodelltag.setUAutopauseab(zeitmodelltagDto.getUAutopauseab());
		zeitmodelltag.setUAutopauseab2(zeitmodelltagDto.getUAutopauseab2());
		zeitmodelltag.setUAutopauseab3(zeitmodelltagDto.getUAutopauseab3());
		zeitmodelltag.setUMehrstd(zeitmodelltagDto.getUMehrstd());
		zeitmodelltag.setBRundesondertaetigkeiten(zeitmodelltagDto
				.getBRundesondertaetigkeiten());
		zeitmodelltag.setUErlaubteanwesenheitszeit(zeitmodelltagDto
				.getUErlaubteanwesenheitszeit());
		zeitmodelltag.setPersonalIIdAendern(zeitmodelltagDto
				.getPersonalIIdAendern());
		zeitmodelltag.setTAendern(zeitmodelltagDto.getTAendern());
		em.merge(zeitmodelltag);
		em.flush();
	}

	private ZeitmodelltagDto assembleZeitmodelltagDto(
			Zeitmodelltag zeitmodelltag) {
		return ZeitmodelltagDtoAssembler.createDto(zeitmodelltag);
	}

	private ZeitmodelltagDto[] assembleZeitmodelltagDtos(
			Collection<?> zeitmodelltags) {
		List<ZeitmodelltagDto> list = new ArrayList<ZeitmodelltagDto>();
		if (zeitmodelltags != null) {
			Iterator<?> iterator = zeitmodelltags.iterator();
			while (iterator.hasNext()) {
				Zeitmodelltag zeitmodelltag = (Zeitmodelltag) iterator.next();
				list.add(assembleZeitmodelltagDto(zeitmodelltag));
			}
		}
		ZeitmodelltagDto[] returnArray = new ZeitmodelltagDto[list.size()];
		return (ZeitmodelltagDto[]) list.toArray(returnArray);
	}

	/**
	 * Hole alle Tagesarten nach Spr.
	 *
	 * @param cNrSpracheI
	 *            String
	 * @throws EJBExceptionLP
	 * @return Map
	 */
	public Map getAllSprTagesarten(String cNrSpracheI) throws EJBExceptionLP {

		Map tmArten = new LinkedHashMap<Integer, Object>();
		// try {
		Query query = em.createNamedQuery("TagesartfindAll");
		Collection<?> clArten = query.getResultList();
		Iterator<?> itArten = clArten.iterator();
		while (itArten.hasNext()) {
			Tagesart tagesartTemp = (Tagesart) itArten.next();
			Integer key = tagesartTemp.getIId();
			Object value = null;
			// try {
			Tagesartspr tagesartspr = em.find(Tagesartspr.class,
					new TagesartsprPK(cNrSpracheI, tagesartTemp.getIId()));
			if (tagesartspr == null || tagesartspr.getCBez() == null) {
				// fuer locale und C_NR keine Bezeichnug vorhanden ...
				value = tagesartTemp.getCNr();
			} else {
				value = tagesartspr.getCBez();
			}
			// }
			// catch (NoResultException ex1) {
			// fuer locale und C_NR keine Bezeichnug vorhanden ...
			// value = tagesartTemp.getCNr();
			// }
			tmArten.put(key, value);
		}
		// }
		// catch (NoResultException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		// }
		return tmArten;
	}

	/**
	 * Hole alle Tagesarten, die in einem Zeitmodell definiert sind nach Spr.
	 *
	 * @param cNrSpracheI
	 *            String
	 * @param zeitmodellIId
	 *            Zeitmodell
	 * @throws EJBExceptionLP
	 * @return Map
	 */
	public Map getAllSprTagesartenEinesZeitmodells(Integer zeitmodellIId,
			String cNrSpracheI) throws EJBExceptionLP {

		Map tmArten = new HashMap<Integer, Object>();
		// try {
		Query query = em.createNamedQuery("ZeitmodelltagfindByZeitmodellIId");
		query.setParameter(1, zeitmodellIId);
		Collection<?> clArten = query.getResultList();
		// if (clArten.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, null);
		// }
		Iterator<?> itArten = clArten.iterator();
		while (itArten.hasNext()) {
			Zeitmodelltag tagesartTemp = (Zeitmodelltag) itArten.next();
			Integer key = tagesartTemp.getTagesartIId();
			Object value = null;
			// try {
			Tagesartspr tagesartspr = em.find(Tagesartspr.class,
					new TagesartsprPK(cNrSpracheI, tagesartTemp.getIId()));

			if (tagesartspr == null) {
				// fuer locale und C_NR keine Bezeichnu g vorhanden ...
				Tagesart tagesart = em.find(Tagesart.class,
						tagesartTemp.getIId());
				if (tagesart == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
				} else {
					value = tagesart.getCNr();
				}
			} else {
				value = tagesartspr.getCBez();
			}
			// }
			// catch (NoResultException ex1) {
			// fuer locale und C_NR keine Bezeichnu g vorhanden ...
			// value = em.find(Tagesart.class, tagesartTemp.getIId()).getCNr();
			// }
			tmArten.put(key, value);
		}
		// }
		// catch (NoResultException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		// }

		return tmArten;
	}

	/**
	 * Hole alle Taetigkeitarten nach Spr.
	 *
	 * @param cNrSpracheI
	 *            String
	 * @throws EJBExceptionLP
	 * @return Map
	 */
	public Map getAllSprTaetigkeitarten(String cNrSpracheI)
			throws EJBExceptionLP {

		myLogger.entry();

		Map tmArten = new HashMap<String, Object>();
		// try {
		Query query = em.createNamedQuery("TaetigkeitartfindAll");
		Collection<?> clArten = query.getResultList();
		// if (clArten.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, null);
		// }
		Iterator<?> itArten = clArten.iterator();
		while (itArten.hasNext()) {
			Taetigkeitart taetigkeitartTemp = (Taetigkeitart) itArten.next();
			String key = taetigkeitartTemp.getCNr();
			Object value = null;
			// try {
			Taetigkeitartspr taetigkeitartspr = em.find(
					Taetigkeitartspr.class,
					new TaetigkeitartsprPK(cNrSpracheI, taetigkeitartTemp
							.getCNr()));
			if (taetigkeitartspr == null) {
				// fuer locale und C_NR keine Bezeichnug vorhanden ...
				value = taetigkeitartTemp.getCNr();
			} else {
				value = taetigkeitartspr.getCBez();
			}
			// }
			// catch (NoResultException ex1) {
			// fuer locale und C_NR keine Bezeichnug vorhanden ...
			// value = taetigkeitartTemp.getCNr();
			// }
			tmArten.put(key, value);
		}
		// }
		// catch (NoResultException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		// }

		return tmArten;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Double getSummeZeitenEinesBeleges(String belegartCNr,
			Integer belegartIId, Integer belegartpositionIId,
			Integer personalIId, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, TheClientDto theClientDto)
			throws EJBExceptionLP {
		AuftragzeitenDto[] dtos = getAllZeitenEinesBeleges(belegartCNr,
				belegartIId, belegartpositionIId, personalIId, tVon, tBis,
				false, false, theClientDto);

		double zeiten = 0;
		for (int i = 0; i < dtos.length; i++) {
			if (dtos[i] != null && dtos[i].getDdDauer() != null) {
				zeiten = zeiten + dtos[i].getDdDauer().doubleValue();
			}
		}
		return new Double(zeiten);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Double getSummeMaschinenZeitenEinesBeleges(Integer losIId,
			Integer lossollarbeitsplanIId, java.sql.Timestamp tZeitenBis,
			TheClientDto theClientDto) {
		AuftragzeitenDto[] dtos = getAllMaschinenzeitenEinesBeleges(losIId,
				lossollarbeitsplanIId, null, tZeitenBis, theClientDto);

		double zeiten = 0;
		for (int i = 0; i < dtos.length; i++) {
			if (dtos[i] != null && dtos[i].getDdDauer() != null) {
				zeiten = zeiten + dtos[i].getDdDauer().doubleValue();
			}
		}
		return new Double(zeiten);
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printLohndatenexport(Integer personalIId,
			Integer iJahr, Integer iMonat, boolean bisMonatsende,
			java.sql.Date d_datum_bis, TheClientDto theClientDto,
			Integer iOption, Integer iOptionSortierung,
			Boolean bPlusVersteckte, boolean bNurAnwesende) {

		try {

			int iVersatz = 0;
			try {
				ParametermandantDto parameter = (ParametermandantDto) getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_PERSONAL,
								ParameterFac.PARAMETER_PERSONALZEITDATENEXPORT_PERIODENVERSATZ);

				iVersatz = ((Integer) parameter.getCWertAsObject()).intValue();

			} catch (RemoteException ex5) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
			}

			PersonalDto[] personalDtos = null;

			if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_SELEKTIERTE_PERSON) {

				if (personalIId == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
							new Exception("personalIId == null"));
				}

				personalDtos = new PersonalDto[1];

				personalDtos[0] = getPersonalFac().personalFindByPrimaryKey(
						personalIId, theClientDto);

			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_PERSONEN) {
				personalDtos = getPersonalFac().personalFindByMandantCNr(
						theClientDto.getMandant(), bPlusVersteckte);
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_MEINE_ABTEILUNG) {
				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKey(personalIId, theClientDto);
				personalDtos = getPersonalFac()
						.personalFindAllPersonenMeinerAbteilung(
								personalDto.getKostenstelleIIdAbteilung(),
								theClientDto.getMandant(), bPlusVersteckte);
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_ARBEITER) {
				personalDtos = getPersonalFac()
						.personalFindAllArbeiterEinesMandanten(
								theClientDto.getMandant(), bPlusVersteckte);
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_ANGESTELLTE) {
				personalDtos = getPersonalFac()
						.personalFindAllAngestellteEinesMandanten(
								theClientDto.getMandant(), bPlusVersteckte);
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
						"OPTION NICHT VERFUEGBAR"));
			}

			if (bNurAnwesende) {
				personalDtos = getZeiterfassungFac()
						.entferneNichtAnwesendePersonen(iJahr, iMonat, iJahr,
								iMonat, personalDtos, theClientDto);
			}

			int iJahrMitVersatz = iJahr;
			int iMonatMitVersatz = iMonat;
			if (iVersatz > 0) {

				Calendar c = Calendar.getInstance();
				c.set(Calendar.YEAR, iJahr);
				c.set(Calendar.MONTH, iMonat);

				c.set(Calendar.MONTH, iMonat + iVersatz);

				iJahrMitVersatz = c.get(Calendar.YEAR);
				iMonatMitVersatz = c.get(Calendar.MONTH);

			}

			ArrayList alDaten = new ArrayList();
			for (int i = 0; i < personalDtos.length; i++) {

				try {
					MonatsabrechnungDto moaDto = erstelleMonatsAbrechnung(
							personalDtos[i].getIId(), iJahr, iMonat,
							bisMonatsende, d_datum_bis, theClientDto, false,
							iOptionSortierung);

					HashMap parameter = moaDto.getParameter();

					// Lohnarten holen

					String sQuery = "SELECT s FROM FLRLohnartstundenfaktor s WHERE s.flrlohnart.personalart_c_nr='"
							+ personalDtos[i].getPersonalartCNr()
							+ "' OR s.flrlohnart.personalart_c_nr IS NULL";

					SessionFactory factory = FLRSessionFactory.getFactory();
					Session session = factory.openSession();

					org.hibernate.Query inventurliste = session
							.createQuery(sQuery);

					List<?> resultList = inventurliste.list();

					Iterator<?> resultListIterator = resultList.iterator();

					double dExtern = 0;
					double dIntern = 0;
					data = new Object[resultList.size()][REPORT_PRODUKTIVITAETISSTATISTIK_ANZAHL_SPALTEN];

					while (resultListIterator.hasNext()) {
						FLRLohnartstundenfaktor f = (FLRLohnartstundenfaktor) resultListIterator
								.next();

						BigDecimal bdIstGesamt = new BigDecimal(0);
						BigDecimal bdSollGesamt = new BigDecimal(0);
						BigDecimal bdFtg = new BigDecimal(0);
						BigDecimal bdArzt = new BigDecimal(0);
						BigDecimal bdBehoerde = new BigDecimal(0);
						BigDecimal bdKrank = new BigDecimal(0);
						BigDecimal bdUrlaub = new BigDecimal(0);
						BigDecimal bdSonstBez = new BigDecimal(0);
						BigDecimal bdSonstNBez = new BigDecimal(0);

						BigDecimal bdUest50F = new BigDecimal(0);
						BigDecimal bdUest50P = new BigDecimal(0);
						BigDecimal bdUest100F = new BigDecimal(0);
						BigDecimal bdUest100P = new BigDecimal(0);
						BigDecimal bdMehrstd = new BigDecimal(0);

						BigDecimal bdGutstunden = new BigDecimal(0);

						StundenabrechnungDto[] stundeabrechnungDtos = getPersonalFac()
								.stundenabrechnungFindByPersonalIIdIJahrIMonat(
										personalIId, iJahr, iMonat);

						for (int j = 0; j < stundeabrechnungDtos.length; j++) {
							bdGutstunden = bdGutstunden
									.add(stundeabrechnungDtos[j]
											.getNGutstunden());
						}

						Date dEintrittsdatum = new Date();

						Object[][] datenMoa = moaDto.getData();
						for (int j = 0; j < datenMoa.length; j++) {
							Object[] zeileMoa = datenMoa[j];

							String tagesart = (String) zeileMoa[ZeiterfassungFacBean.REPORT_MONATSABRECHNUNG_TAGESART];

							if (f.getFlrtagesart() == null
									|| f.getFlrtagesart().getC_nr()
											.equals(tagesart)) {

								bdIstGesamt = bdIstGesamt
										.add((BigDecimal) zeileMoa[ZeiterfassungFacBean.REPORT_MONATSABRECHNUNG_IST]);
								bdSollGesamt = bdSollGesamt
										.add((BigDecimal) zeileMoa[ZeiterfassungFacBean.REPORT_MONATSABRECHNUNG_SOLL]);
								bdFtg = bdFtg
										.add((BigDecimal) zeileMoa[ZeiterfassungFacBean.REPORT_MONATSABRECHNUNG_FEIERTAG]);
								bdArzt = bdArzt
										.add((BigDecimal) zeileMoa[ZeiterfassungFacBean.REPORT_MONATSABRECHNUNG_ARZT]);
								bdBehoerde = bdBehoerde
										.add((BigDecimal) zeileMoa[ZeiterfassungFacBean.REPORT_MONATSABRECHNUNG_BEHOERDE]);
								bdKrank = bdKrank
										.add((BigDecimal) zeileMoa[ZeiterfassungFacBean.REPORT_MONATSABRECHNUNG_KRANK]);
								bdUrlaub = bdUrlaub
										.add((BigDecimal) zeileMoa[ZeiterfassungFacBean.REPORT_MONATSABRECHNUNG_URLAUB]);

								bdUest50P = bdUest50P
										.add((BigDecimal) zeileMoa[ZeiterfassungFacBean.REPORT_MONATSABRECHNUNG_UESTD50TAGEWEISE]);
								bdUest50F = bdUest50F
										.add((BigDecimal) zeileMoa[ZeiterfassungFacBean.REPORT_MONATSABRECHNUNG_UESTD50TAGESWEISEFREI]);
								bdUest100F = bdUest100F
										.add((BigDecimal) zeileMoa[ZeiterfassungFacBean.REPORT_MONATSABRECHNUNG_UESTD100FREI]);
								bdUest100P = bdUest100P
										.add((BigDecimal) zeileMoa[ZeiterfassungFacBean.REPORT_MONATSABRECHNUNG_UESTD100]);
								bdMehrstd = bdMehrstd
										.add((BigDecimal) zeileMoa[ZeiterfassungFacBean.REPORT_MONATSABRECHNUNG_MEHRZEIT]);

								bdSonstBez = bdSonstBez
										.add((BigDecimal) zeileMoa[ZeiterfassungFacBean.REPORT_MONATSABRECHNUNG_SONSTIGE_BEZAHLTETAETIGKEITEN]);
								bdSonstNBez = bdSonstNBez
										.add((BigDecimal) zeileMoa[ZeiterfassungFacBean.REPORT_MONATSABRECHNUNG_SONSTIGE_UNBEZAHLTETAETIGKEITEN]);
							}

						}

						BigDecimal bdNormalstunden = bdIstGesamt;

						bdNormalstunden = bdNormalstunden.subtract(bdFtg)
								.subtract(bdUest50P).subtract(bdUest50F)
								.subtract(bdUest100F).subtract(bdUest100P)
								.subtract(bdMehrstd);

						BigDecimal stunden = new BigDecimal(0);
						BigDecimal faktor = new BigDecimal(f.getF_faktor()
								.doubleValue());

						if (f.getFlrlohnstundenart().getC_nr()
								.equals(PersonalFac.LOHNSTUNDENART_SOLL)) {
							stunden = bdSollGesamt;
						} else if (f.getFlrlohnstundenart().getC_nr()
								.equals(PersonalFac.LOHNSTUNDENART_GST)) {
							stunden = bdIstGesamt;
						} else if (f.getFlrlohnstundenart().getC_nr()
								.equals(PersonalFac.LOHNSTUNDENART_FTG)) {
							stunden = bdFtg;
						} else if (f.getFlrlohnstundenart().getC_nr()
								.equals(PersonalFac.LOHNSTUNDENART_GUT)) {
							stunden = bdGutstunden;
						} else if (f.getFlrlohnstundenart().getC_nr()
								.equals(PersonalFac.LOHNSTUNDENART_MST)) {
							stunden = bdMehrstd;
						} else if (f.getFlrlohnstundenart().getC_nr()
								.equals(PersonalFac.LOHNSTUNDENART_NST)) {
							stunden = bdNormalstunden;
						} else if (f.getFlrlohnstundenart().getC_nr()
								.equals(PersonalFac.LOHNSTUNDENART_U100F)) {
							stunden = bdUest100F;
						} else if (f.getFlrlohnstundenart().getC_nr()
								.equals(PersonalFac.LOHNSTUNDENART_U100P)) {
							stunden = bdUest100P;
						} else if (f.getFlrlohnstundenart().getC_nr()
								.equals(PersonalFac.LOHNSTUNDENART_U50F)) {
							stunden = bdUest50F;
						} else if (f.getFlrlohnstundenart().getC_nr()
								.equals(PersonalFac.LOHNSTUNDENART_U50P)) {
							stunden = bdUest50P;
						}
						stunden = stunden.multiply(faktor);
						Object[] zeile = new Object[ZeiterfassungFac.REPORT_LOHNDATENEXPORT_ANZAHL_SPALTEN];
						zeile[ZeiterfassungFac.REPORT_LOHNDATENEXPORT_PERSONALNUMMER] = personalDtos[i]
								.getCPersonalnr();
						zeile[ZeiterfassungFac.REPORT_LOHNDATENEXPORT_JAHR] = iJahrMitVersatz
								+ "";
						zeile[ZeiterfassungFac.REPORT_LOHNDATENEXPORT_MONAT] = new DateFormatSymbols(
								theClientDto.getLocUi()).getMonths()[iMonatMitVersatz];
						zeile[ZeiterfassungFac.REPORT_LOHNDATENEXPORT_LOHNART] = f
								.getFlrlohnart().getC_bez();
						zeile[ZeiterfassungFac.REPORT_LOHNDATENEXPORT_LOHNART_NR] = f
								.getFlrlohnart().getI_lohnart();
						zeile[ZeiterfassungFac.REPORT_LOHNDATENEXPORT_LOHNSTUNDENART] = f
								.getFlrlohnstundenart().getC_bez();
						zeile[ZeiterfassungFac.REPORT_LOHNDATENEXPORT_STUNDEN] = stunden;

						PartnerDto pDto = getPartnerFac()
								.partnerFindByPrimaryKey(
										personalDtos[i].getPartnerIId(),
										theClientDto);

						zeile[ZeiterfassungFac.REPORT_LOHNDATENEXPORT_VORNAME] = pDto
								.getCName2vornamefirmazeile2();
						zeile[ZeiterfassungFac.REPORT_LOHNDATENEXPORT_NACHNAME] = pDto
								.getCName1nachnamefirmazeile1();

						alDaten.add(zeile);
					}

				} catch (EJBExceptionLP ex1) {
					if (ex1.getCode() == EJBExceptionLP.FEHLER_PERSONAL_FEHLER_BEI_EINTRITTSDATUM
							&& iOption.intValue() != ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_SELEKTIERTE_PERSON) {
						// NICHTS - PERSON WIRD AUSGELASSEN

					} else {
						throw ex1;
					}

				}

			}
			HashMap<String, Object> parameter = new HashMap<String, Object>();

			data = new Object[alDaten.size()][ZeiterfassungFac.REPORT_LOHNDATENEXPORT_ANZAHL_SPALTEN];
			data = (Object[][]) alDaten.toArray(data);

			index = -1;
			sAktuellerReport = ZeiterfassungFac.REPORT_LOHNDATENEXPORT;

			initJRDS(parameter, ZeiterfassungFac.REPORT_MODUL,
					ZeiterfassungFac.REPORT_LOHNDATENEXPORT,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
			return getReportPrint();

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

	}

	private Object[] befuelleProjektausauftragFuerProduktivitaetsstatistik(
			Integer auftragIId, Object[] oZeile, TheClientDto theClientDto) {

		if (auftragIId != null) {

			AuftragDto aDto = getAuftragFac().auftragFindByPrimaryKey(
					auftragIId);

			Integer projektIId = aDto.getProjektIId();

			try {
				if (projektIId == null) {
					// aus Angebot holen
					if (aDto.getAngebotIId() != null) {
						AngebotDto agDto = getAngebotFac()
								.angebotFindByPrimaryKey(aDto.getAngebotIId(),
										theClientDto);
						projektIId = agDto.getProjektIId();

					}

				}

				if (projektIId != null) {
					ProjektDto projektDto = null;

					projektDto = getProjektFac().projektFindByPrimaryKey(
							projektIId);
					oZeile[REPORT_PRODUKTIVITAETISSTATISTIK_PROJEKT_PROJEKTKLAMMER] = projektDto
							.getCNr();
					oZeile[REPORT_PRODUKTIVITAETISSTATISTIK_PROJEKTKATEGORIE_PROJEKTKLAMMER] = projektDto
							.getKategorieCNr();
				}

			} catch (RemoteException ex3) {
				throwEJBExceptionLPRespectOld(ex3);
			}

		}
		return oZeile;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printProduktivitaetsstatistik(Integer personalIId,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis, Integer iOption,
			Boolean bPlusVersteckte, boolean bNurAnwesende,
			boolean bVerdichtet, boolean bMonatsbetrachtung,
			TheClientDto theClientDto) {
		boolean bProjektklammer = false;
		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
				MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER, theClientDto)) {
			bProjektklammer = true;
		}

		if (tVon == null || tBis == null || personalIId == iOption) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"tVon == null || tBis == null || personalIId == iOption"));
		}
		tVon = Helper.cutTimestamp(tVon);
		tBis = Helper.cutTimestamp(tBis);

		if (tVon.after(tBis)) {
			java.sql.Timestamp h = tVon;
			tVon = tBis;
			tBis = h;
		}

		JasperPrintLP print = null;
		PersonalDto[] personalDtos = null;
		MandantDto mandantDto = null;
		try {
			mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
		} catch (RemoteException ex2) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex2);
		}
		try {
			if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_SELEKTIERTE_PERSON) {

				if (personalIId == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
							new Exception("personalIId == null"));
				}

				personalDtos = new PersonalDto[1];

				personalDtos[0] = getPersonalFac().personalFindByPrimaryKey(
						personalIId, theClientDto);

			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_PERSONEN) {
				personalDtos = getPersonalFac().personalFindByMandantCNr(
						theClientDto.getMandant(), bPlusVersteckte);
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_MEINE_ABTEILUNG) {
				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKey(personalIId, theClientDto);
				personalDtos = getPersonalFac()
						.personalFindAllPersonenMeinerAbteilung(
								personalDto.getKostenstelleIIdAbteilung(),
								theClientDto.getMandant(), bPlusVersteckte);
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_ARBEITER) {
				personalDtos = getPersonalFac()
						.personalFindAllArbeiterEinesMandanten(
								theClientDto.getMandant(), bPlusVersteckte);
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_ANGESTELLTE) {
				personalDtos = getPersonalFac()
						.personalFindAllAngestellteEinesMandanten(
								theClientDto.getMandant(), bPlusVersteckte);
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
						"OPTION NICHT VERFUEGBAR"));
			}

			if (bNurAnwesende) {
				personalDtos = getZeiterfassungFac()
						.entferneNichtAnwesendePersonen(tVon, tBis,
								personalDtos, theClientDto);
			}

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		HashMap parameter = new HashMap<Object, Object>();
		parameter.put("P_VERDICHTET", new Boolean(bVerdichtet));
		parameter.put("P_MONATSBETRACHTUNG", new Boolean(bMonatsbetrachtung));
		parameter.put("P_VON", tVon);
		parameter.put("P_NUR_ANWESENDE", new Boolean(bNurAnwesende));

		parameter.put("P_BIS", new java.sql.Timestamp(
				tBis.getTime() - 24 * 3600000));
		try {
			ParametermandantDto parameterVonBis = (ParametermandantDto) getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_PERSONAL,
							ParameterFac.PARAMETER_VON_BIS_ERFASSUNG);
			parameter.put("P_VON_BIS_ERFASSUNG",
					((Boolean) parameterVonBis.getCWertAsObject()));
		} catch (RemoteException e1) {
			throwEJBExceptionLPRespectOld(e1);
		}

		ArrayList alDatenGesamt = new ArrayList();

		for (int i = 0; i < personalDtos.length; i++) {
			ArrayList alDaten = new ArrayList();
			PersonalDto personalDto = personalDtos[i];

			personalDto.setPartnerDto(getPartnerFac().partnerFindByPrimaryKey(
					personalDto.getPartnerIId(), theClientDto));
			if (personalDto.getKostenstelleIIdAbteilung() != null) {
				personalDto.setKostenstelleDto_Abteilung(getSystemFac()
						.kostenstelleFindByPrimaryKey(
								personalDto.getKostenstelleIIdAbteilung()));
			}
			if (personalDto.getKostenstelleIIdStamm() != null) {
				personalDto.setKostenstelleDto_Stamm(getSystemFac()
						.kostenstelleFindByPrimaryKey(
								personalDto.getKostenstelleIIdStamm()));
			}

			// Anwesenheitszeit VON BIS berechnen
			double dIstGesamt = 0;
			Double dGesamt = berechneArbeitszeitImZeitraum(
					personalDto.getIId(), new java.sql.Date(tVon.getTime()),
					new java.sql.Date(tBis.getTime()), false, theClientDto);

			if (dGesamt != null) {
				dIstGesamt = dGesamt.doubleValue();
			}

			String sQuery = "select distinct zeitdaten.c_belegartnr,zeitdaten.i_belegartid";

			if (bVerdichtet == false) {
				sQuery += ",zeitdaten.i_belegartpositionid";
			}

			// PJ 16557

			// Hole Zeitdaten eines Tages
			Query query = em
					.createNamedQuery("ZeitdatenfindZeitdatenEinesTagesUndEinerPerson");
			query.setParameter(1, personalIId);
			query.setParameter(2, new java.sql.Timestamp(tVon.getTime()));
			query.setParameter(3, new java.sql.Timestamp(
					tVon.getTime() + 24 * 3600000));
			Collection<?> cl = query.getResultList();
			ZeitdatenDto[] zeitdatenDtos = assembleZeitdatenDtosOhneBelegzeiten(cl);

			Integer taetigkeitIId_Kommt = taetigkeitFindByCNr(
					ZeiterfassungFac.TAETIGKEIT_KOMMT, theClientDto).getIId();

			// Daten einen Tag vorher wg. Mitternachtssprung holen
			java.sql.Date dVon = new java.sql.Date(tVon.getTime()
					- (3600000 * 24));
			if (zeitdatenDtos.length > 0
					&& zeitdatenDtos[0].getTaetigkeitIId() != null
					&& zeitdatenDtos[0].getTaetigkeitIId().equals(
							taetigkeitIId_Kommt)) {
				// Ausser die erste Taetigkeit ist ein Kommt, dann nicht
				dVon = new java.sql.Date(tVon.getTime());
			}

			sQuery += " from FLRZeitdaten zeitdaten WHERE zeitdaten.flrpersonal.i_id="
					+ personalDto.getIId()
					+ "AND zeitdaten.c_belegartnr is not null AND zeitdaten.t_zeit>='"
					+ Helper.formatDateWithSlashes(dVon)
					+ "' AND zeitdaten.t_zeit<'"
					+ Helper.formatDateWithSlashes(new java.sql.Date(tBis
							.getTime())) + "'";

			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = factory.openSession();

			org.hibernate.Query inventurliste = session.createQuery(sQuery);

			List<?> resultList = inventurliste.list();

			Iterator<?> resultListIterator = resultList.iterator();

			double dExtern = 0;
			double dIntern = 0;
			data = new Object[resultList.size()][REPORT_PRODUKTIVITAETISSTATISTIK_ANZAHL_SPALTEN];

			// PJ18906
			if (resultList.size() == 0) {
				// Wenn keine Zeiten auf Auftraege, dann trotzdem in Statistik
				// anzeigen
				Object[] oZeile = new Object[REPORT_PRODUKTIVITAETISSTATISTIK_ANZAHL_SPALTEN];
				oZeile[REPORT_PRODUKTIVITAETISSTATISTIK_TELEFONZEIT] = Boolean.FALSE;
				alDaten.add(oZeile);
			}

			while (resultListIterator.hasNext()) {

				Object[] o = (Object[]) resultListIterator.next();

				Object[] oZeile = new Object[REPORT_PRODUKTIVITAETISSTATISTIK_ANZAHL_SPALTEN];

				oZeile[REPORT_PRODUKTIVITAETISSTATISTIK_TELEFONZEIT] = Boolean.FALSE;

				try {

					String sBezeichnung = "";
					com.lp.server.partner.service.PartnerDto partnerDto = null;
					if (((String) o[0]).equals(LocaleFac.BELEGART_AUFTRAG)) {
						AuftragDto auftragDto = getAuftragFac()
								.auftragFindByPrimaryKey((Integer) o[1]);
						oZeile[REPORT_PRODUKTIVITAETISSTATISTIK_AUFTRAG] = auftragDto
								.getCNr();

						sBezeichnung = auftragDto.getCBezProjektbezeichnung();
						KundeDto kundeDto = getKundeFac()
								.kundeFindByPrimaryKeyOhneExc(
										auftragDto.getKundeIIdAuftragsadresse(),
										theClientDto);
						if (kundeDto != null) {
							partnerDto = kundeDto.getPartnerDto();
						}

						if (bProjektklammer) {
							befuelleProjektausauftragFuerProduktivitaetsstatistik(
									auftragDto.getIId(), oZeile, theClientDto);
						}

					} else if (((String) o[0]).equals(LocaleFac.BELEGART_LOS)) {
						com.lp.server.fertigung.service.LosDto losDto = getFertigungFac()
								.losFindByPrimaryKey((Integer) o[1]);
						oZeile[REPORT_PRODUKTIVITAETISSTATISTIK_LOS] = "LO"
								+ losDto.getCNr();
						sBezeichnung = losDto.getCProjekt();

						Session sessionLosklasse = factory.openSession();
						String queryLosklasse = "FROM FLRLoslosklasse l where l.los_i_id="
								+ losDto.getIId();

						org.hibernate.Query loslosklasse = sessionLosklasse
								.createQuery(queryLosklasse);

						List resultListLosklasse = loslosklasse.list();

						Iterator resultListIteratorLosklasse = resultListLosklasse
								.iterator();

						String losklassen = "";
						while (resultListIteratorLosklasse.hasNext()) {
							com.lp.server.fertigung.fastlanereader.generated.FLRLoslosklasse lk = (com.lp.server.fertigung.fastlanereader.generated.FLRLoslosklasse) resultListIteratorLosklasse
									.next();

							losklassen += lk.getFlrlosklasse().getC_nr() + ",";

						}

						oZeile[REPORT_PRODUKTIVITAETISSTATISTIK_LOSKLASSEN] = losklassen;

						sessionLosklasse.close();

						if (losDto.getAuftragIId() != null
								|| losDto.getKundeIId() != null) {

							KundeDto kundeDto = null;
							if (losDto.getAuftragIId() != null) {
								AuftragDto auftragDto = getAuftragFac()
										.auftragFindByPrimaryKey(
												losDto.getAuftragIId());
								oZeile[REPORT_PRODUKTIVITAETISSTATISTIK_AUFTRAG] = auftragDto
										.getCNr();
								kundeDto = getKundeFac()
										.kundeFindByPrimaryKeyOhneExc(
												auftragDto
														.getKundeIIdAuftragsadresse(),
												theClientDto);

								if (bProjektklammer) {
									befuelleProjektausauftragFuerProduktivitaetsstatistik(
											auftragDto.getIId(), oZeile,
											theClientDto);
								}

							} else if (losDto.getKundeIId() != null) {
								kundeDto = getKundeFac()
										.kundeFindByPrimaryKeyOhneExc(
												losDto.getKundeIId(),
												theClientDto);
							}
							if (kundeDto != null) {
								partnerDto = kundeDto.getPartnerDto();
							}
						}

						if (losDto.getStuecklisteIId() != null) {
							ArtikelDto artikelDto = getStuecklisteFac()
									.stuecklisteFindByPrimaryKey(
											losDto.getStuecklisteIId(),
											theClientDto).getArtikelDto();
							oZeile[REPORT_PRODUKTIVITAETISSTATISTIK_STKLNR] = artikelDto
									.getCNr();
							oZeile[REPORT_PRODUKTIVITAETISSTATISTIK_STKLBEZ] = artikelDto
									.formatBezeichnung();
						}
					} else if (((String) o[0])
							.equals(LocaleFac.BELEGART_PROJEKT)) {
						ProjektDto projektDto = null;
						try {
							projektDto = getProjektFac()
									.projektFindByPrimaryKey((Integer) o[1]);
						} catch (EJBExceptionLP ex3) {
							throw new EJBExceptionLP(ex3);
						}
						oZeile[REPORT_PRODUKTIVITAETISSTATISTIK_PROJEKT] = "PJ"
								+ projektDto.getCNr();

						if (bProjektklammer) {

							// PJ18422
							oZeile[REPORT_PRODUKTIVITAETISSTATISTIK_PROJEKT_PROJEKTKLAMMER] = projektDto
									.getCNr();
							oZeile[REPORT_PRODUKTIVITAETISSTATISTIK_PROJEKTKATEGORIE_PROJEKTKLAMMER] = projektDto
									.getKategorieCNr();
						}

						sBezeichnung = projektDto.getCTitel();
						partnerDto = getPartnerFac().partnerFindByPrimaryKey(
								projektDto.getPartnerIId(), theClientDto);
					} else if (((String) o[0])
							.equals(LocaleFac.BELEGART_ANGEBOT)) {
						AngebotDto angebotDto = getAngebotFac()
								.angebotFindByPrimaryKey((Integer) o[1],
										theClientDto);
						oZeile[REPORT_PRODUKTIVITAETISSTATISTIK_ANGEBOT] = "AG"
								+ angebotDto.getCNr();

						sBezeichnung = angebotDto.getCBez();
						KundeDto kundeDto = getKundeFac()
								.kundeFindByPrimaryKeyOhneExc(
										angebotDto.getKundeIIdAngebotsadresse(),
										theClientDto);

						if (kundeDto != null) {
							partnerDto = kundeDto.getPartnerDto();
						}

						// PJ18422
						if (bProjektklammer) {
							if (angebotDto.getProjektIId() != null) {

								ProjektDto projektDto = null;
								try {
									projektDto = getProjektFac()
											.projektFindByPrimaryKey(
													angebotDto.getProjektIId());
								} catch (EJBExceptionLP ex3) {
									throw new EJBExceptionLP(ex3);
								}

								oZeile[REPORT_PRODUKTIVITAETISSTATISTIK_PROJEKT_PROJEKTKLAMMER] = projektDto
										.getCNr();
								oZeile[REPORT_PRODUKTIVITAETISSTATISTIK_PROJEKTKATEGORIE_PROJEKTKLAMMER] = projektDto
										.getKategorieCNr();
							}
						}

					}

					if (partnerDto != null) {
						oZeile[REPORT_PRODUKTIVITAETISSTATISTIK_KUNDE] = partnerDto
								.formatTitelAnrede();
					}
					oZeile[REPORT_PRODUKTIVITAETISSTATISTIK_PROJEKTBEZEICHNUNG] = sBezeichnung;
					oZeile[REPORT_PRODUKTIVITAETISSTATISTIK_PERSONAL_ID] = personalDto
							.getIId();

					Double dDauer = new Double(0);

					Integer belegpositionIId = null;
					if (bVerdichtet == false) {
						belegpositionIId = (Integer) o[2];
					}

					AuftragzeitenDto[] dtos = getAllZeitenEinesBeleges(
							(String) o[0], (Integer) o[1], belegpositionIId,
							personalDto.getIId(), tVon, tBis, true, false,
							false, theClientDto);

					for (int k = 0; k < dtos.length; k++) {
						if (dtos[k] != null && dtos[k].getDdDauer() != null) {
							dDauer = dDauer
									+ dtos[k].getDdDauer().doubleValue();
						}
					}

					if ((partnerDto != null && mandantDto.getPartnerIId()
							.equals(partnerDto.getIId())) || partnerDto == null) {
						dIntern = dIntern + dDauer.doubleValue();
					} else if (partnerDto != null) {
						dExtern = dExtern + dDauer.doubleValue();
					}

					if (bVerdichtet) {
						oZeile[REPORT_PRODUKTIVITAETISSTATISTIK_DAUER] = dDauer;

						alDaten.add(oZeile);

					} else {

						HashMap hmAGBereitsVorhanden = new HashMap();

						for (int k = 0; k < dtos.length; k++) {
							if (dtos[k] != null && dtos[k].getDdDauer() != null) {

								Object[] oZeileZusaetzlich = oZeile.clone();

								oZeileZusaetzlich[REPORT_PRODUKTIVITAETISSTATISTIK_TAETIGKEIT] = dtos[k]
										.getSArtikelcnr();
								oZeileZusaetzlich[REPORT_PRODUKTIVITAETISSTATISTIK_TAETIGKEIT_BEZEICHNUNG] = dtos[k]
										.getSArtikelbezeichnung();
								oZeileZusaetzlich[REPORT_PRODUKTIVITAETISSTATISTIK_DAUER] = dtos[k]
										.getDdDauer();

								if (((String) o[0])
										.equals(LocaleFac.BELEGART_LOS)) {
									com.lp.server.fertigung.service.LosDto losDto = getFertigungFac()
											.losFindByPrimaryKey((Integer) o[1]);

									if (o[2] != null) {

										LossollarbeitsplanDto saDto = null;
										try {
											saDto = getFertigungFac()
													.lossollarbeitsplanFindByPrimaryKey(
															(Integer) o[2]);
										} catch (EJBExceptionLP e) {
											// nicht mehr vorhanden
										}

										if (saDto != null) {
											AuftragzeitenDto[] dtosGesamt = getAllZeitenEinesBeleges(
													(String) o[0],
													(Integer) o[1],
													saDto.getIId(), null, null,
													null, true, false,
													theClientDto);

											BigDecimal zeitDerPersonAufArbeitsgang = new BigDecimal(
													0);
											BigDecimal zeitAllerPersonenAufArbeitsgang = new BigDecimal(
													0);
											for (int p = 0; p < dtosGesamt.length; p++) {
												if (dtosGesamt[p] != null
														&& dtosGesamt[p]
																.getDdDauer() != null) {

													BigDecimal zeitTemp = new BigDecimal(
															dtosGesamt[p]
																	.getDdDauer()
																	.doubleValue());
													zeitAllerPersonenAufArbeitsgang = zeitAllerPersonenAufArbeitsgang
															.add(zeitTemp);
													if (dtosGesamt[p]
															.getIPersonalMaschinenId()
															.equals(personalDto
																	.getIId())) {
														zeitDerPersonAufArbeitsgang = zeitDerPersonAufArbeitsgang
																.add(zeitTemp);
													}

												}
											}

											BigDecimal zeitanteil = new BigDecimal(
													100);
											if (zeitAllerPersonenAufArbeitsgang
													.doubleValue() != 0) {
												zeitanteil = zeitDerPersonAufArbeitsgang
														.divide(zeitAllerPersonenAufArbeitsgang,
																4,
																BigDecimal.ROUND_HALF_EVEN)
														.multiply(
																new BigDecimal(
																		100));
											}
											oZeileZusaetzlich[REPORT_PRODUKTIVITAETISSTATISTIK_LOS_ZEITANTEIL] = zeitanteil;

											oZeileZusaetzlich[REPORT_PRODUKTIVITAETISSTATISTIK_LOS_LOSGROESSE] = losDto
													.getNLosgroesse();
											oZeileZusaetzlich[REPORT_PRODUKTIVITAETISSTATISTIK_LOS_AGART] = saDto
													.getAgartCNr();
											oZeileZusaetzlich[REPORT_PRODUKTIVITAETISSTATISTIK_LOS_AGNUMMER] = saDto
													.getIArbeitsgangnummer();
											oZeileZusaetzlich[REPORT_PRODUKTIVITAETISSTATISTIK_LOS_UAGNUMMER] = saDto
													.getIUnterarbeitsgang();
											oZeileZusaetzlich[REPORT_PRODUKTIVITAETISSTATISTIK_LOS_FERTIG] = Helper
													.short2Boolean(saDto
															.getBFertig());

											oZeileZusaetzlich[REPORT_PRODUKTIVITAETISSTATISTIK_LOS_STUECKZEIT] = new BigDecimal(
													saDto.getLStueckzeit())
													.divide(new BigDecimal(
															3600000),
															4,
															BigDecimal.ROUND_HALF_EVEN);
											oZeileZusaetzlich[REPORT_PRODUKTIVITAETISSTATISTIK_LOS_RUESTZEIT] = new BigDecimal(
													saDto.getLRuestzeit())
													.divide(new BigDecimal(
															3600000),
															4,
															BigDecimal.ROUND_HALF_EVEN);

											oZeileZusaetzlich[REPORT_PRODUKTIVITAETISSTATISTIK_LOS_GESAMTZEIT] = saDto
													.getNGesamtzeit();

											// Alle Zeiten des Arbeitsganges
											// nach Zeit sortieren
											for (int p = dtosGesamt.length - 1; p > 0; --p) {
												for (int j = 0; j < p; ++j) {
													if (dtosGesamt[j]
															.getTsBeginn()
															.after(dtosGesamt[j + 1]
																	.getTsBeginn())) {
														AuftragzeitenDto tauschDto = dtosGesamt[j];
														dtosGesamt[j] = dtosGesamt[j + 1];
														dtosGesamt[j + 1] = tauschDto;
													}
												}
											}
											Boolean bRuestenMitrechnen = false;
											// Wenn ich nun der erste bin, dann
											// Ruesten mitrechnen
											if (dtosGesamt.length > 0) {
												if (dtosGesamt[0].getTsBeginn()
														.after(tVon)
														&& dtosGesamt[0]
																.getIPersonalMaschinenId()
																.equals(personalDto
																		.getIId())) {
													bRuestenMitrechnen = true;
												}
											}

											LosgutschlechtDto[] loguDtos = getFertigungFac()
													.losgutschlechtFindByLossollarbeitsplanIId(
															saDto.getIId());

											BigDecimal bdGut = new BigDecimal(0);
											BigDecimal bdSchlecht = new BigDecimal(
													0);
											BigDecimal bdInarbeit = new BigDecimal(
													0);

											for (int z = 0; z < loguDtos.length; z++) {

												if (loguDtos[z]
														.getZeitdatenIId() != null) {

													ZeitdatenDto zDto = zeitdatenFindByPrimaryKey(
															loguDtos[z]
																	.getZeitdatenIId(),
															theClientDto);

													if (personalDto
															.getIId()
															.equals(zDto
																	.getPersonalIId())
															&& zDto.getTZeit()
																	.after(tVon)
															&& zDto.getTZeit()
																	.before(tBis)) {
														bdGut = bdGut
																.add(loguDtos[z]
																		.getNGut());
														bdSchlecht = bdSchlecht
																.add(loguDtos[z]
																		.getNSchlecht());
														bdInarbeit = bdInarbeit
																.add(loguDtos[z]
																		.getNInarbeit());
													}
												}
											}

											if (!hmAGBereitsVorhanden
													.containsKey(belegpositionIId)) {

												oZeileZusaetzlich[REPORT_PRODUKTIVITAETISSTATISTIK_LOS_RUESTEN_MITRECHNEN] = bRuestenMitrechnen;
												oZeileZusaetzlich[REPORT_PRODUKTIVITAETISSTATISTIK_LOS_GUTSTUECK] = bdGut;
												oZeileZusaetzlich[REPORT_PRODUKTIVITAETISSTATISTIK_LOS_SCHLECHTSTUECK] = bdSchlecht;
												oZeileZusaetzlich[REPORT_PRODUKTIVITAETISSTATISTIK_LOS_INARBEIT] = bdInarbeit;
												hmAGBereitsVorhanden.put(
														belegpositionIId, "");
											}
										}
									}
								}

								alDaten.add(oZeileZusaetzlich);
							}
						}
					}

				} catch (RemoteException ex1) {
					/** @todo CK PJ 4659 */
				}
			}
			session.close();

			// Telefonzeiten

			ArtikelDto artikelDto_DefaultAZ = null;

			Session sessionTel = FLRSessionFactory.getFactory().openSession();

			String sQueryTel = "SELECT t FROM FLRTelefonzeiten as t "

					+ " WHERE t.t_von>='"
					+ Helper.formatDateWithSlashes(new java.sql.Date(tVon
							.getTime()))
					+ "' AND t.t_bis<'"
					+ Helper.formatDateWithSlashes(new java.sql.Date(tBis
							.getTime())) + "' ";

			sQueryTel += " AND t.personal_i_id=" + personalDto.getIId();

			org.hibernate.Query queryTelefon = sessionTel
					.createQuery(sQueryTel);

			List<?> resultListTelefon = queryTelefon.list();

			Iterator<?> resultListIteratorTelefon = resultListTelefon
					.iterator();

			TreeMap<String, Object[]> tmTelefonzeitenPJ = new TreeMap<String, Object[]>();
			TreeMap<String, Object[]> tmTelefonzeitenRest = new TreeMap<String, Object[]>();
			int k = 0;
			while (resultListIteratorTelefon.hasNext()) {
				FLRTelefonzeiten tel = (FLRTelefonzeiten) resultListIteratorTelefon
						.next();
				if (artikelDto_DefaultAZ == null) {
					artikelDto_DefaultAZ = getZeiterfassungFac()
							.getDefaultArbeitszeitartikel(theClientDto);
				}

				java.sql.Time tDauer = new java.sql.Time(tel.getT_bis()
						.getTime() - tel.getT_von().getTime());
				tDauer.setTime(tDauer.getTime() - 3600000);
				Double dDauer = Helper.time2Double(tDauer);

				k++;
				try {
					Object[] oZeile = new Object[REPORT_PRODUKTIVITAETISSTATISTIK_ANZAHL_SPALTEN];

					oZeile[REPORT_PRODUKTIVITAETISSTATISTIK_TELEFONZEIT] = Boolean.TRUE;

					oZeile[REPORT_PRODUKTIVITAETISSTATISTIK_DAUER] = dDauer;

					oZeile[REPORT_PRODUKTIVITAETISSTATISTIK_PERSONAL_ID] = personalDto
							.getIId();

					oZeile[REPORT_PRODUKTIVITAETISSTATISTIK_TAETIGKEIT] = artikelDto_DefaultAZ
							.getCNr();
					if (artikelDto_DefaultAZ.getArtikelsprDto() != null) {
						oZeile[REPORT_PRODUKTIVITAETISSTATISTIK_TAETIGKEIT_BEZEICHNUNG] = artikelDto_DefaultAZ
								.getArtikelsprDto().getCBez();
					}

					if (tel.getProjekt_i_id() != null) {

						ProjektDto pjDto = getProjektFac()
								.projektFindByPrimaryKey(tel.getProjekt_i_id());

						PartnerDto partnerDto = getPartnerFac()
								.partnerFindByPrimaryKey(pjDto.getPartnerIId(),
										theClientDto);

						if (mandantDto.getPartnerIId().equals(
								partnerDto.getIId())) {
							dIntern = dIntern + dDauer.doubleValue();
						} else {
							dExtern = dExtern + dDauer.doubleValue();
						}

						oZeile[REPORT_PRODUKTIVITAETISSTATISTIK_PROJEKT] = "PJ"
								+ pjDto.getCNr();
						oZeile[REPORT_PRODUKTIVITAETISSTATISTIK_PROJEKTBEZEICHNUNG] = pjDto
								.getCTitel();
						if (bVerdichtet) {

							if (tmTelefonzeitenPJ.containsKey(pjDto.getCNr())) {
								// Dauer addieren
								Object[] oZeileTemp = tmTelefonzeitenPJ
										.get(pjDto.getCNr());
								Double dauerVorhanden = ((Double) oZeileTemp[REPORT_PRODUKTIVITAETISSTATISTIK_DAUER])
										.doubleValue();
								oZeileTemp[REPORT_PRODUKTIVITAETISSTATISTIK_DAUER] = dauerVorhanden
										+ dDauer;

								oZeileTemp[REPORT_PRODUKTIVITAETISSTATISTIK_KUNDE] = partnerDto
										.formatTitelAnrede();

								tmTelefonzeitenPJ.put(pjDto.getCNr(),
										oZeileTemp);
							} else {
								tmTelefonzeitenPJ.put(pjDto.getCNr(), oZeile);
							}

						} else {
							tmTelefonzeitenPJ.put(pjDto.getCNr() + k, oZeile);
						}

					} else {

						String partner = "";
						if (tel.getFlrpartner() != null) {
							partner = HelperServer.formatNameAusFLRPartner(tel
									.getFlrpartner());

							if (mandantDto.getPartnerIId().equals(
									tel.getFlrpartner().getI_id())) {
								dIntern = dIntern + dDauer.doubleValue();
							} else {
								dExtern = dExtern + dDauer.doubleValue();
							}

						} else {
							dExtern = dExtern + dDauer.doubleValue();
						}

						oZeile[REPORT_PRODUKTIVITAETISSTATISTIK_KUNDE] = partner;

						if (bVerdichtet) {
							// Dauer addieren

							if (tmTelefonzeitenRest.containsKey(partner)) {

								Object[] oZeileTemp = tmTelefonzeitenRest
										.get(partner);
								Double dauerVorhanden = ((Double) oZeileTemp[REPORT_PRODUKTIVITAETISSTATISTIK_DAUER])
										.doubleValue();
								oZeileTemp[REPORT_PRODUKTIVITAETISSTATISTIK_DAUER] = dauerVorhanden
										+ dDauer;
							} else {

								tmTelefonzeitenRest.put(partner, oZeile);
							}
						} else {
							tmTelefonzeitenRest.put(partner + k, oZeile);
						}

					}

				} catch (RemoteException ex1) {
					throwEJBExceptionLPRespectOld(ex1);
				}

			}

			Iterator it = tmTelefonzeitenPJ.keySet().iterator();
			while (it.hasNext()) {
				alDaten.add(tmTelefonzeitenPJ.get(it.next()));
			}
			it = tmTelefonzeitenRest.keySet().iterator();
			while (it.hasNext()) {
				alDaten.add(tmTelefonzeitenRest.get(it.next()));
			}

			dIntern = Helper.rundeKaufmaennisch(
					new java.math.BigDecimal(dIntern), 2).doubleValue();
			dExtern = Helper.rundeKaufmaennisch(
					new java.math.BigDecimal(dExtern), 2).doubleValue();

			double dProzentExtern = 0;
			double dProzentIntern = 0;
			if (dIstGesamt > 0) {
				dProzentExtern = dExtern / dIstGesamt;
				dProzentExtern = Helper.rundeKaufmaennisch(
						new java.math.BigDecimal(dProzentExtern * 100), 2)
						.doubleValue();

				dProzentIntern = dIntern / dIstGesamt;
				dProzentIntern = Helper.rundeKaufmaennisch(
						new java.math.BigDecimal(dProzentIntern * 100), 2)
						.doubleValue();
			}

			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(tVon.getTime());

			Double leistungswert = null;
			try {
				PersonalgehaltDto pgDto = getPersonalFac()
						.personalgehaltFindLetztePersonalgehalt(personalIId,
								c.get(Calendar.YEAR), c.get(Calendar.MONTH));
				if (pgDto != null) {
					leistungswert = pgDto.getFLeistungswert();
				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			// Zeit Sondertaetigkeiten:
			TaetigkeitDto[] taetigkeitenDtos = getAllSondertaetigkeitenOhneKommtUndGeht(theClientDto);

			Integer tagesartIId_Feiertag = tagesartFindByCNr(
					ZeiterfassungFac.TAGESART_FEIERTAG, theClientDto).getIId();
			Integer tagesartIId_Halbtag = tagesartFindByCNr(
					ZeiterfassungFac.TAGESART_HALBTAG, theClientDto).getIId();

			// Datenquelle Subreport
			ArrayList<Object[]> al = new ArrayList<Object[]>();
			for (int j = 0; j < taetigkeitenDtos.length; j++) {
				BigDecimal bdDauer = new BigDecimal(
						getGesamtDauerEinerSondertaetigkeitImZeitraum(
								personalDto.getIId(), tVon, tBis,
								taetigkeitenDtos[j].getIId(), theClientDto,
								tagesartIId_Feiertag, tagesartIId_Halbtag));

				if (bdDauer.doubleValue() > 0) {
					Object[] o = new Object[2];
					o[0] = taetigkeitenDtos[j].getBezeichnung();
					o[1] = bdDauer;
					al.add(o);
				}
			}

			String[] fieldnames = new String[] { "F_SONDERTAETIGKEITEN",
					"F_DAUER" };

			Object[][] dataSub = new Object[al.size()][fieldnames.length];
			dataSub = (Object[][]) al.toArray(dataSub);

			for (int m = 0; m < alDaten.size(); m++) {
				Object[] zeile = (Object[]) alDaten.get(m);

				zeile[REPORT_PRODUKTIVITAETISSTATISTIK_PERSON_NAME] = personalDto
						.getCPersonalnr()
						+ " "
						+ personalDto.getPartnerDto().formatAnrede();
				if (personalDto.getKostenstelleDto_Stamm() != null) {
					zeile[REPORT_PRODUKTIVITAETISSTATISTIK_PERSON_KOSTENSTELLE] = personalDto
							.getKostenstelleDto_Stamm()
							.formatKostenstellenbezeichnung();
				}
				if (personalDto.getKostenstelleDto_Abteilung() != null) {
					zeile[REPORT_PRODUKTIVITAETISSTATISTIK_PERSON_ABTEILUNG] = personalDto
							.getKostenstelleDto_Abteilung()
							.formatKostenstellenbezeichnung();
				}
				zeile[REPORT_PRODUKTIVITAETISSTATISTIK_PERSON_IST_GESAMT] = new Double(
						dIstGesamt);
				zeile[REPORT_PRODUKTIVITAETISSTATISTIK_PERSON_INTERN] = new Double(
						dIntern);
				zeile[REPORT_PRODUKTIVITAETISSTATISTIK_PERSON_EXTERN] = new Double(
						dExtern);
				zeile[REPORT_PRODUKTIVITAETISSTATISTIK_PERSON_PROZENTINTERN] = new Double(
						dProzentIntern);
				zeile[REPORT_PRODUKTIVITAETISSTATISTIK_PERSON_PROZENTEXTERN] = new Double(
						dProzentExtern);
				zeile[REPORT_PRODUKTIVITAETISSTATISTIK_PERSON_LEISTUNGSWERT] = leistungswert;
				zeile[REPORT_PRODUKTIVITAETISSTATISTIK_PERSON_SUBREPORT_SONDERTAETIGKEITEN] = new LPDatenSubreport(
						dataSub, fieldnames);

				alDaten.set(m, zeile);

			}

			alDatenGesamt.addAll(alDaten);

		}

		Object[][] returnArray = new Object[alDatenGesamt.size()][REPORT_PRODUKTIVITAETISSTATISTIK_ANZAHL_SPALTEN];
		data = (Object[][]) alDatenGesamt.toArray(returnArray);

		index = -1;
		sAktuellerReport = ZeiterfassungFac.REPORT_PRODUKTIVITAETSSTATISTIK;

		initJRDS(parameter, ZeiterfassungFac.REPORT_MODUL,
				ZeiterfassungFac.REPORT_PRODUKTIVITAETSSTATISTIK,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();

	}

	private LPDatenSubreport getSubreportZeitdatenjournalFuerWochenabrechnung(
			Integer personalIId, int iKW, int iJahr, TheClientDto theClientDto) {

		Calendar cVon = Calendar.getInstance();
		cVon.set(Calendar.YEAR, iJahr);
		cVon.set(Calendar.WEEK_OF_YEAR, iKW);
		cVon.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

		Timestamp tVon = new Timestamp(cVon.getTimeInMillis());

		cVon.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

		Timestamp tBis = new Timestamp(cVon.getTimeInMillis());

		Object[][] oDaten = getZeiterfassungReportFac()
				.erstelleZeitdatenjournal(personalIId, tVon, tBis, theClientDto);

		Object[][] dataSub = new Object[oDaten.length][2];
		String[] fieldnames = new String[] { "Personalnummer", "Name", "Zeit",
				"Taetigkeit", "Auftrag", "Projektbezeichnung", "Tagessumme",
				"Maschine", "Position", "Buchungsart", "Bemerkung",
				"Gutstueck", "Schlechtstueck", "Kunde", "Dauer", "Kommentar",
				"Quelle", "TaetigkeitSonderzeit", "DauerSonderzeit",
				"DatumSonderzeit", "Artikelbezeichnung", "Sollzeit" };

		for (int i = 0; i < oDaten.length; i++) {
			Object[] zeileSub = oDaten[i];

			zeileSub[0] = oDaten[i][ZeiterfassungReportFac.REPORT_ZEITDATEN_PERSONALNR];
			zeileSub[1] = oDaten[i][ZeiterfassungReportFac.REPORT_ZEITDATEN_NAME];
			zeileSub[2] = oDaten[i][ZeiterfassungReportFac.REPORT_ZEITDATEN_ZEIT];
			zeileSub[3] = oDaten[i][ZeiterfassungReportFac.REPORT_ZEITDATEN_TAETIGKEIT];
			zeileSub[4] = oDaten[i][ZeiterfassungReportFac.REPORT_ZEITDATEN_AUFTRAG];
			zeileSub[5] = oDaten[i][ZeiterfassungReportFac.REPORT_ZEITDATEN_PROJEKTBEZEICHNUNG];
			zeileSub[6] = oDaten[i][ZeiterfassungReportFac.REPORT_ZEITDATEN_TAGESSUMME];
			zeileSub[7] = oDaten[i][ZeiterfassungReportFac.REPORT_ZEITDATEN_MASCHINE];
			zeileSub[8] = oDaten[i][ZeiterfassungReportFac.REPORT_ZEITDATEN_POSITION];
			zeileSub[9] = oDaten[i][ZeiterfassungReportFac.REPORT_ZEITDATEN_BUCHUNGSART];
			zeileSub[10] = oDaten[i][ZeiterfassungReportFac.REPORT_ZEITDATEN_BEMERKUNG];
			zeileSub[11] = oDaten[i][ZeiterfassungReportFac.REPORT_ZEITDATEN_GUTSTK];
			zeileSub[12] = oDaten[i][ZeiterfassungReportFac.REPORT_ZEITDATEN_SCHLECHTSTK];
			zeileSub[13] = oDaten[i][ZeiterfassungReportFac.REPORT_ZEITDATEN_KUNDE];
			zeileSub[14] = oDaten[i][ZeiterfassungReportFac.REPORT_ZEITDATEN_DAUER];
			zeileSub[15] = oDaten[i][ZeiterfassungReportFac.REPORT_ZEITDATEN_KOMMENTAR];
			zeileSub[16] = oDaten[i][ZeiterfassungReportFac.REPORT_ZEITDATEN_QUELLE];
			zeileSub[17] = oDaten[i][ZeiterfassungReportFac.REPORT_ZEITDATEN_TAETIGKEIT_SONDERZEIT];
			zeileSub[18] = oDaten[i][ZeiterfassungReportFac.REPORT_ZEITDATEN_DAUER_SONDERZEIT];
			zeileSub[19] = oDaten[i][ZeiterfassungReportFac.REPORT_ZEITDATEN_DATUM_SONDERZEIT];
			zeileSub[20] = oDaten[i][ZeiterfassungReportFac.REPORT_ZEITDATEN_ARTIKELBEZEICHNUNG];
			zeileSub[21] = oDaten[i][ZeiterfassungReportFac.REPORT_ZEITDATEN_SOLLZEIT];
			dataSub[i] = zeileSub;
		}

		return new LPDatenSubreport(dataSub, fieldnames);
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP erstelleWochenabrechnung(Integer personalIId,
			Timestamp tVon, Timestamp tBis,
			boolean bMitUrlaubZumAbrechnungsbeginn,
			boolean bMitSubreportZeitdatenjournal, TheClientDto theClientDto) {

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tVon.getTime());

		int iErsteKW = c.get(Calendar.WEEK_OF_YEAR);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
		int iErstesJahr = c.get(Calendar.YEAR);

		Calendar cBis = Calendar.getInstance();
		cBis.setTimeInMillis(tBis.getTime());
		int iLetzteKW = cBis.get(Calendar.WEEK_OF_YEAR);
		int iLetztesJahr = cBis.get(Calendar.YEAR);
		ArrayList alDaten = new ArrayList();

		c.set(Calendar.DAY_OF_MONTH, 1);
		while (c.getTimeInMillis() < tBis.getTime()) {

			try {
				MonatsabrechnungDto monatsabrechnungDto = erstelleMonatsAbrechnung(
						personalIId,
						c.get(Calendar.YEAR),
						c.get(Calendar.MONTH),
						true,
						null,
						theClientDto,
						false,
						ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_PERSONALNUMMER);

				for (int i = 0; i < monatsabrechnungDto.getData().length; i++) {
					alDaten.add(monatsabrechnungDto.getData()[i]);
				}
			} catch (EJBExceptionLP e) {
				if (e.getCode() == EJBExceptionLP.FEHLER_PERSONAL_FEHLER_BEI_EINTRITTSDATUM) {
					// auslassen, da noch nicht eingetreten
				} else {
					throw e;
				}
			}
			c.add(Calendar.MONTH, 1);
		}

		// Wochensummen bilden

		java.util.TreeMap wochensummen = new java.util.TreeMap();

		for (int i = 0; i < alDaten.size(); i++) {
			Object[] zeileMonatsabrechnung = (Object[]) alDaten.get(i);

			int iAktuelleKW = ((Integer) zeileMonatsabrechnung[0]).intValue();
			int iAktuellesJahr = ((Integer) zeileMonatsabrechnung[REPORT_MONATSABRECHNUNG_JAHR])
					.intValue();

			if (iErstesJahr == iAktuellesJahr && iAktuelleKW < iErsteKW) {
				continue;
			}
			if (iLetztesJahr == iAktuellesJahr && iAktuelleKW > iLetzteKW) {
				continue;
			}

			String key = (iAktuellesJahr * 100 + iAktuelleKW) + "";

			if (wochensummen.containsKey(key)) {
				Object[] oTemp = (Object[]) wochensummen.get(key);

				// Aufsummieren
				oTemp[REPORT_MONATSABRECHNUNG_SOLL] = ((BigDecimal) oTemp[REPORT_MONATSABRECHNUNG_SOLL])
						.add((BigDecimal) zeileMonatsabrechnung[REPORT_MONATSABRECHNUNG_SOLL]);
				oTemp[REPORT_MONATSABRECHNUNG_IST] = ((BigDecimal) oTemp[REPORT_MONATSABRECHNUNG_IST])
						.add((BigDecimal) zeileMonatsabrechnung[REPORT_MONATSABRECHNUNG_IST]);
				oTemp[REPORT_MONATSABRECHNUNG_DIFF] = ((BigDecimal) oTemp[REPORT_MONATSABRECHNUNG_DIFF])
						.add((BigDecimal) zeileMonatsabrechnung[REPORT_MONATSABRECHNUNG_DIFF]);
				oTemp[REPORT_MONATSABRECHNUNG_UNTER] = ((BigDecimal) oTemp[REPORT_MONATSABRECHNUNG_UNTER])
						.add((BigDecimal) zeileMonatsabrechnung[REPORT_MONATSABRECHNUNG_UNTER]);
				oTemp[REPORT_MONATSABRECHNUNG_FEIERTAG] = ((BigDecimal) oTemp[REPORT_MONATSABRECHNUNG_FEIERTAG])
						.add((BigDecimal) zeileMonatsabrechnung[REPORT_MONATSABRECHNUNG_FEIERTAG]);
				oTemp[REPORT_MONATSABRECHNUNG_URLAUB] = ((BigDecimal) oTemp[REPORT_MONATSABRECHNUNG_URLAUB])
						.add((BigDecimal) zeileMonatsabrechnung[REPORT_MONATSABRECHNUNG_URLAUB]);

				oTemp[REPORT_MONATSABRECHNUNG_ARZT] = ((BigDecimal) oTemp[REPORT_MONATSABRECHNUNG_ARZT])
						.add((BigDecimal) zeileMonatsabrechnung[REPORT_MONATSABRECHNUNG_ARZT]);

				oTemp[REPORT_MONATSABRECHNUNG_KRANK] = ((BigDecimal) oTemp[REPORT_MONATSABRECHNUNG_KRANK])
						.add((BigDecimal) zeileMonatsabrechnung[REPORT_MONATSABRECHNUNG_KRANK]);
				oTemp[REPORT_MONATSABRECHNUNG_KINDKRANK] = ((BigDecimal) oTemp[REPORT_MONATSABRECHNUNG_KINDKRANK])
						.add((BigDecimal) zeileMonatsabrechnung[REPORT_MONATSABRECHNUNG_KINDKRANK]);

				oTemp[REPORT_MONATSABRECHNUNG_BEHOERDE] = ((BigDecimal) oTemp[REPORT_MONATSABRECHNUNG_BEHOERDE])
						.add((BigDecimal) zeileMonatsabrechnung[REPORT_MONATSABRECHNUNG_BEHOERDE]);

				oTemp[REPORT_MONATSABRECHNUNG_SONSTIGE_BEZAHLTETAETIGKEITEN] = ((BigDecimal) oTemp[REPORT_MONATSABRECHNUNG_SONSTIGE_BEZAHLTETAETIGKEITEN])
						.add((BigDecimal) zeileMonatsabrechnung[REPORT_MONATSABRECHNUNG_SONSTIGE_BEZAHLTETAETIGKEITEN]);

				if (oTemp[REPORT_MONATSABRECHNUNG_UESTD100] == null) {
					oTemp[REPORT_MONATSABRECHNUNG_UESTD100] = new BigDecimal(0);
				}
				oTemp[REPORT_MONATSABRECHNUNG_UESTD100] = ((BigDecimal) oTemp[REPORT_MONATSABRECHNUNG_UESTD100])
						.add((BigDecimal) zeileMonatsabrechnung[REPORT_MONATSABRECHNUNG_UESTD100]);
				oTemp[REPORT_MONATSABRECHNUNG_UESTD200] = ((BigDecimal) oTemp[REPORT_MONATSABRECHNUNG_UESTD200])
						.add((BigDecimal) zeileMonatsabrechnung[REPORT_MONATSABRECHNUNG_UESTD200]);
				oTemp[REPORT_MONATSABRECHNUNG_UESTD100FREI] = ((BigDecimal) oTemp[REPORT_MONATSABRECHNUNG_UESTD100FREI])
						.add((BigDecimal) zeileMonatsabrechnung[REPORT_MONATSABRECHNUNG_UESTD100FREI]);
				oTemp[REPORT_MONATSABRECHNUNG_UESTD50] = ((BigDecimal) oTemp[REPORT_MONATSABRECHNUNG_UESTD50])
						.add((BigDecimal) zeileMonatsabrechnung[REPORT_MONATSABRECHNUNG_UESTD50]);
				oTemp[REPORT_MONATSABRECHNUNG_UESTD50TAGESWEISEFREI] = ((BigDecimal) oTemp[REPORT_MONATSABRECHNUNG_UESTD50TAGESWEISEFREI])
						.add((BigDecimal) zeileMonatsabrechnung[REPORT_MONATSABRECHNUNG_UESTD50TAGESWEISEFREI]);
				oTemp[REPORT_MONATSABRECHNUNG_UESTD50TAGEWEISE] = ((BigDecimal) oTemp[REPORT_MONATSABRECHNUNG_UESTD50TAGEWEISE])
						.add((BigDecimal) zeileMonatsabrechnung[REPORT_MONATSABRECHNUNG_UESTD50TAGEWEISE]);
				oTemp[REPORT_MONATSABRECHNUNG_MEHRZEIT] = ((BigDecimal) oTemp[REPORT_MONATSABRECHNUNG_MEHRZEIT])
						.add((BigDecimal) zeileMonatsabrechnung[REPORT_MONATSABRECHNUNG_MEHRZEIT]);

				wochensummen.put(key, oTemp);

			} else {

				if (bMitSubreportZeitdatenjournal) {
					zeileMonatsabrechnung[REPORT_MONATSABRECHNUNG_PLATZHALTER_FUER_SUREPORTZEIDATENJOURNAL] = getSubreportZeitdatenjournalFuerWochenabrechnung(
							personalIId, iAktuelleKW, iAktuellesJahr,
							theClientDto);
				}

				wochensummen.put(key, zeileMonatsabrechnung);

			}

		}

		data = new Object[wochensummen.size()][REPORT_MONATSABRECHNUNG_ANZAHL_SPALTEN];

		int iZeile = 0;
		Iterator it = wochensummen.keySet().iterator();

		while (it.hasNext()) {
			data[iZeile] = (Object[]) wochensummen.get(it.next());

			iZeile++;
		}

		HashMap parameter = new HashMap();

		parameter.put("P_VON", tVon);
		parameter.put("P_BIS", tBis);
		parameter.put("P_KWVON", iErsteKW + "");
		parameter.put("P_KWBIS", iLetzteKW + "");

		PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(
				personalIId, theClientDto);

		parameter.put("P_PERSON", personalDto.formatAnrede());
		parameter.put("P_PERSONALNUMMER", personalDto.getCPersonalnr());

		// PJ18264 Urlaubsanspruch zum Abrechnungsbeginn
		if (bMitUrlaubZumAbrechnungsbeginn) {
			UrlaubsabrechnungDto v = berechneUrlaubsAnspruch(
					personalDto.getIId(), new java.sql.Date(tVon.getTime()),
					theClientDto);

			parameter.put("P_URLAUBVERFUEGBAR_TAGE",
					v.getNVerfuegbarerUrlaubTage());
			parameter.put("P_URLAUBVERFUEGBAR_STUNDEN",
					v.getNVerfuegbarerUrlaubStunden());

		}

		if (bMitUrlaubZumAbrechnungsbeginn) {
			index = -1;
			sAktuellerReport = ZeiterfassungFac.REPORT_WOCHENABRECHNUNG;
			initJRDS(parameter, ZeiterfassungFac.REPORT_MODUL,
					ZeiterfassungFac.REPORT_WOCHENJOURNAL,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
			return getReportPrint();
		} else {
			index = -1;
			sAktuellerReport = ZeiterfassungFac.REPORT_WOCHENABRECHNUNG;
			initJRDS(parameter, ZeiterfassungFac.REPORT_MODUL,
					ZeiterfassungFac.REPORT_WOCHENABRECHNUNG,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
			return getReportPrint();
		}

	}

	private BigDecimal getSummeAusMonatsabrechnungsZeile(
			ArrayList<Object[]> alTag, int field) {
		BigDecimal bdSumme = BigDecimal.ZERO;

		for (int i = 0; i < alTag.size(); i++) {

			Object o = alTag.get(i)[field];
			if (o instanceof BigDecimal) {
				bdSumme = bdSumme.add((BigDecimal) o);
			}

		}

		return bdSumme;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public WochenabschlussReportDto printWochenabschluss(Integer personalIId,
			java.sql.Timestamp tKW, TheClientDto theClientDto) {

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(
				personalIId, theClientDto);
		parameter.put("P_PERSON", personalDto.getPartnerDto()
				.formatFixTitelName1Name2());
		WochenabschlussReportDto wDto = new WochenabschlussReportDto();
		// Vorher ev. 2 Monatsabrechnungen durchfuehren, wenn Woche ueber 2
		// Monate
		// geht

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tKW.getTime());

		int kwAktuell = c.get(Calendar.WEEK_OF_YEAR);

		parameter.put("P_KW", kwAktuell);

		Timestamp tKWVon = new Timestamp(tKW.getTime());
		Timestamp tKWBis = new Timestamp(tKW.getTime());

		Timestamp[] tVonBis = Helper.getTimestampVonBisEinerKW(tKW);
		tKWVon = tVonBis[0];
		tKWBis = tVonBis[1];

		parameter.put("P_VON", tKWVon);
		parameter.put("P_BIS", tKWBis);

		String sQueryZeitabschluss = "SELECT za  from FLRZeitabschluss za WHERE za.personal_i_id="
				+ personalIId + " ORDER BY za.t_abgeschlossen_bis DESC";

		Session sessionZeitabschluss = FLRSessionFactory.getFactory()
				.openSession();

		org.hibernate.Query queryZeitabschluss = sessionZeitabschluss
				.createQuery(sQueryZeitabschluss);
		queryZeitabschluss.setMaxResults(1);

		List<?> resultListZeitabschluss = queryZeitabschluss.list();

		Iterator<?> resultListIteratorZeitabschluss = resultListZeitabschluss
				.iterator();

		if (resultListIteratorZeitabschluss.hasNext()) {
			FLRZeitabschluss flr = (FLRZeitabschluss) resultListIteratorZeitabschluss
					.next();
			sessionZeitabschluss.close();

			parameter.put("P_ZEITENABGESCHLOSSEN", new Timestamp(flr
					.getT_abgeschlossen_bis().getTime()));
		}

		BigDecimal nMaxWochenanwesenheit = null;
		BigDecimal nKumuliertesWochenist = BigDecimal.ZERO;

		boolean bNurWarnung = false;
		ParametermandantDto parameterMand = null;
		try {
			parameterMand = (ParametermandantDto) getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_PERSONAL,
							ParameterFac.PARAMETER_AUTOMATISCHE_PAUSEN_NUR_WARNUNG);

			bNurWarnung = (Boolean) parameterMand.getCWertAsObject();

			PersonalzeitmodellDto dto = getPersonalFac()
					.personalzeitmodellFindZeitmodellZuDatum(personalIId,
							Helper.cutTimestamp(tKW), theClientDto);

			if (dto != null && dto.getZeitmodellIId() != null) {
				ZeitmodellDto zmDto = getZeiterfassungFac()
						.zeitmodellFindByPrimaryKey(dto.getZeitmodellIId(),
								theClientDto);
				nMaxWochenanwesenheit = zmDto.getNMaximalesWochenist();
			}

		} catch (RemoteException ex5) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
		}

		Calendar cVon = Calendar.getInstance();
		cVon.setTimeInMillis(tKWVon.getTime());
		int iJahrVon = cVon.get(Calendar.YEAR);
		int iMonatVon = cVon.get(Calendar.MONTH);

		Calendar cBis = Calendar.getInstance();
		cBis.setTimeInMillis(tKWBis.getTime());
		int iJahrBis = cBis.get(Calendar.YEAR);
		int iMonatBis = cBis.get(Calendar.MONTH);

		ArrayList<Object[]> alDatenMonatsabrechnung = new ArrayList<Object[]>();

		if (iMonatVon != iMonatBis) {
			MonatsabrechnungDto moaDtoVormonat = erstelleMonatsAbrechnung(
					personalIId,
					iJahrVon,
					iMonatVon,
					true,
					null,
					theClientDto,
					false,
					ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_PERSONALNUMMER,
					false, true);

			Object[][] oZeilen = moaDtoVormonat.getData();
			for (int i = 0; i < oZeilen.length; i++) {
				Object[] oZeile = oZeilen[i];

				int iKW = (Integer) oZeile[ZeiterfassungFacBean.REPORT_MONATSABRECHNUNG_KALENDERWOCHE];

				if (iKW == kwAktuell) {
					alDatenMonatsabrechnung.add(oZeile);
				}

			}

		}

		MonatsabrechnungDto report = erstelleMonatsAbrechnung(
				personalDto.getIId(),
				iJahrBis,
				iMonatBis,
				false,
				new java.sql.Date(tKWBis.getTime()),
				theClientDto,
				false,
				ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_PERSONALNUMMER,
				false, true);

		Object[][] oZeilen = report.getData();
		for (int i = 0; i < oZeilen.length; i++) {
			Object[] oZeile = oZeilen[i];

			int iKW = (Integer) oZeile[ZeiterfassungFacBean.REPORT_MONATSABRECHNUNG_KALENDERWOCHE];

			if (iKW == kwAktuell) {
				alDatenMonatsabrechnung.add(oZeile);
			}
		}

		
		//SP3403
		Calendar cEinenTagVorher=Calendar.getInstance();
		cEinenTagVorher.setTime(new java.sql.Date(tKWVon.getTime()));
		cEinenTagVorher.add(Calendar.DAY_OF_YEAR, -1);
		
		
		
		// PJ18816
		// Damit ich die Parameter zum Von-datum habe
		report = erstelleMonatsAbrechnung(
				personalDto.getIId(),
				cEinenTagVorher.get(Calendar.YEAR),
				cEinenTagVorher.get(Calendar.MONTH),
				false,
				new java.sql.Date(cEinenTagVorher.getTime().getTime()),
				theClientDto,
				false,
				ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_PERSONALNUMMER,
				false, false);
		HashMap hmParameterMonatsabrechnungVon = report.getParameter();

		parameter.put("P_VERFUEGBARERURLAUBTAGE_ZUM_VON_DATUM",
				hmParameterMonatsabrechnungVon.get("P_VERFUEGBARERURLAUBTAGE"));
		parameter.put("P_VERFUEGBARERURLAUBSTUNDEN_ZUM_VON_DATUM",
				hmParameterMonatsabrechnungVon
						.get("P_VERFUEGBARERURLAUBSTUNDEN"));

		parameter.put("P_VERFUEGBARERGLEITZEITSALDO_ZUM_VON_DATUM",
				hmParameterMonatsabrechnungVon
						.get("P_GLEITZEITSALDO_ABRECHNUNGSZEITPUNKT"));

		// Nochmal rechnen zum Bis-Datum
		report = erstelleMonatsAbrechnung(
				personalDto.getIId(),
				iJahrBis,
				iMonatBis,
				false,
				new java.sql.Date(tKWBis.getTime()),
				theClientDto,
				false,
				ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_PERSONALNUMMER,
				false, false);

		HashMap hmParameterMonatsabrechnung = report.getParameter();

		parameter.put("P_VERFUEGBARERURLAUBTAGE",
				hmParameterMonatsabrechnung.get("P_VERFUEGBARERURLAUBTAGE"));
		parameter.put("P_VERFUEGBARERURLAUBSTUNDEN",
				hmParameterMonatsabrechnung.get("P_VERFUEGBARERURLAUBSTUNDEN"));

		parameter.put("P_VERFUEGBARERGLEITZEITSALDO",
				hmParameterMonatsabrechnung
						.get("P_GLEITZEITSALDO_ABRECHNUNGSZEITPUNKT"));

		// Urlaubsanspruch zum Ende des Jahres

		BigDecimal bdAlterAspruchStunden = (BigDecimal) hmParameterMonatsabrechnung
				.get("P_ALTERURLAUBSANSPRUCHSTUNDEN");
		BigDecimal bdAlterVerbrauchtStunden = (BigDecimal) hmParameterMonatsabrechnung
				.get("P_ALTERURLAUBSVERBRAUCHTSTUNDEN");
		BigDecimal bdAktuellerAnspruchStunden = (BigDecimal) hmParameterMonatsabrechnung
				.get("P_AKTUELLERURLAUBSANSPRUCHSTUNDEN");
		BigDecimal bdAktuellerAnspruchVerbrauchtStunden = (BigDecimal) hmParameterMonatsabrechnung
				.get("P_AKTUELLERURLAUBSVERBRAUCHTSTUNDEN");

		parameter.put(
				"P_URLAUBSANSPRUCH_ZUM_ENDE_DES_JAHRES_STUNDEN",
				bdAlterAspruchStunden.subtract(bdAlterVerbrauchtStunden).add(
						bdAktuellerAnspruchStunden));
		parameter.put(
				"P_VERFUEGBARER_URLAUB_ZUM_ENDE_DES_JAHRES_STUNDEN",
				bdAlterAspruchStunden.subtract(bdAlterVerbrauchtStunden)
						.add(bdAktuellerAnspruchStunden)
						.subtract(bdAktuellerAnspruchVerbrauchtStunden));

		BigDecimal bdAlterAspruchTage = (BigDecimal) hmParameterMonatsabrechnung
				.get("P_ALTERURLAUBSANSPRUCHTAGE");
		BigDecimal bdAlterVerbrauchtTage = (BigDecimal) hmParameterMonatsabrechnung
				.get("P_ALTERURLAUBSVERBRAUCHTTAGE");
		BigDecimal bdAktuellerAnspruchTage = (BigDecimal) hmParameterMonatsabrechnung
				.get("P_AKTUELLERURLAUBSANSPRUCHTAGE");
		BigDecimal bdAktuellerAnspruchVerbrauchtTage = (BigDecimal) hmParameterMonatsabrechnung
				.get("P_AKTUELLERURLAUBSVERBRAUCHTTAGE");

		parameter.put(
				"P_URLAUBSANSPRUCH_ZUM_ENDE_DES_JAHRES_TAGE",
				bdAlterAspruchTage.subtract(bdAlterVerbrauchtTage).add(
						bdAktuellerAnspruchTage));
		parameter.put(
				"P_VERFUEGBARER_URLAUB_ZUM_ENDE_DES_JAHRES_TAGE",
				bdAlterAspruchTage.subtract(bdAlterVerbrauchtTage)
						.add(bdAktuellerAnspruchTage)
						.subtract(bdAktuellerAnspruchVerbrauchtTage));

		Integer tagesartIId_Feiertag = tagesartFindByCNr(
				ZeiterfassungFac.TAGESART_FEIERTAG, theClientDto).getIId();
		Integer tagesartIId_Halbtag = tagesartFindByCNr(
				ZeiterfassungFac.TAGESART_HALBTAG, theClientDto).getIId();

		// Nun nach Tage gruppieren, da wir ja ansonsten mit Kommt-Geht-Bloecken
		// rechnen

		LinkedHashMap<Integer, ArrayList<Object[]>> hmDatenTageweise = new LinkedHashMap<Integer, ArrayList<Object[]>>();
		for (int i = 0; i < alDatenMonatsabrechnung.size(); i++) {

			Object[] oZeile = alDatenMonatsabrechnung.get(i);

			Integer iTag = (Integer) oZeile[ZeiterfassungFacBean.REPORT_MONATSABRECHNUNG_DATUM];

			if (hmDatenTageweise.containsKey(iTag)) {

				ArrayList<Object[]> alTag = hmDatenTageweise.get(iTag);

				alTag.add(oZeile);
				hmDatenTageweise.put(iTag, alTag);

			} else {
				ArrayList<Object[]> alTag = new ArrayList<Object[]>();

				alTag.add(oZeile);
				hmDatenTageweise.put(iTag, alTag);

			}
		}

		ArrayList alDaten = new ArrayList();

		Iterator<Integer> it = hmDatenTageweise.keySet().iterator();
		while (it.hasNext()) {
			Integer iTag = it.next();
			ArrayList<Object[]> alTag = hmDatenTageweise.get(iTag);

			java.sql.Time kommt = (java.sql.Time) alTag.get(0)[REPORT_MONATSABRECHNUNG_VON];
			java.sql.Time geht = (java.sql.Time) alTag.get(alTag.size() - 1)[REPORT_MONATSABRECHNUNG_BIS];

			Object[] oZeileWochenabschluss = new Object[REPORT_WOCHENABSCHLUSS_ANZAHL_SPALTEN];

			oZeileWochenabschluss[REPORT_WOCHENABSCHLUSS_KOMMT] = kommt;
			oZeileWochenabschluss[REPORT_WOCHENABSCHLUSS_GEHT] = geht;

			oZeileWochenabschluss[REPORT_WOCHENABSCHLUSS_WOCHENTAG] = alTag
					.get(0)[REPORT_MONATSABRECHNUNG_TAG];

			oZeileWochenabschluss[REPORT_WOCHENABSCHLUSS_SOLL] = getSummeAusMonatsabrechnungsZeile(
					alTag, REPORT_MONATSABRECHNUNG_SOLL);
			oZeileWochenabschluss[REPORT_WOCHENABSCHLUSS_IST] = getSummeAusMonatsabrechnungsZeile(
					alTag, REPORT_MONATSABRECHNUNG_IST);

			oZeileWochenabschluss[REPORT_WOCHENABSCHLUSS_FEIERTAG] = getSummeAusMonatsabrechnungsZeile(
					alTag, REPORT_MONATSABRECHNUNG_FEIERTAG);
			oZeileWochenabschluss[REPORT_WOCHENABSCHLUSS_ARZT] = getSummeAusMonatsabrechnungsZeile(
					alTag, REPORT_MONATSABRECHNUNG_ARZT);
			oZeileWochenabschluss[REPORT_WOCHENABSCHLUSS_KINDKRANK] = getSummeAusMonatsabrechnungsZeile(
					alTag, REPORT_MONATSABRECHNUNG_KINDKRANK);
			oZeileWochenabschluss[REPORT_WOCHENABSCHLUSS_KRANK] = getSummeAusMonatsabrechnungsZeile(
					alTag, REPORT_MONATSABRECHNUNG_KRANK);
			oZeileWochenabschluss[REPORT_WOCHENABSCHLUSS_BEHOERDE] = getSummeAusMonatsabrechnungsZeile(
					alTag, REPORT_MONATSABRECHNUNG_BEHOERDE);

			oZeileWochenabschluss[REPORT_WOCHENABSCHLUSS_URLAUB] = getSummeAusMonatsabrechnungsZeile(
					alTag, REPORT_MONATSABRECHNUNG_URLAUB);
			oZeileWochenabschluss[REPORT_WOCHENABSCHLUSS_SONSTIGE_BEZAHLT] = getSummeAusMonatsabrechnungsZeile(
					alTag,
					REPORT_MONATSABRECHNUNG_SONSTIGE_BEZAHLTETAETIGKEITEN);
			oZeileWochenabschluss[REPORT_WOCHENABSCHLUSS_SONSTIGE_UNBEZAHLT] = getSummeAusMonatsabrechnungsZeile(
					alTag,
					REPORT_MONATSABRECHNUNG_SONSTIGE_UNBEZAHLTETAETIGKEITEN);

			Calendar cDatum = Calendar.getInstance();
			cDatum.set(Calendar.YEAR,
					(Integer) alTag.get(0)[REPORT_MONATSABRECHNUNG_JAHR]);
			cDatum.set(Calendar.MONTH,
					(Integer) alTag.get(0)[REPORT_MONATSABRECHNUNG_MONAT]);
			cDatum.set(Calendar.DAY_OF_MONTH,
					(Integer) alTag.get(0)[REPORT_MONATSABRECHNUNG_DATUM]);
			java.sql.Timestamp tDatum = Helper.cutTimestamp(new Timestamp(
					cDatum.getTimeInMillis()));

			oZeileWochenabschluss[REPORT_WOCHENABSCHLUSS_DATUM] = tDatum;

			try {
				Double dTagesanwesenheitszeit = getZeiterfassungFac()
						.berechneTagesArbeitszeit(personalDto.getIId(),
								new java.sql.Date(tDatum.getTime()),
								theClientDto);
				nKumuliertesWochenist = nKumuliertesWochenist
						.add(new BigDecimal(dTagesanwesenheitszeit));

				// Pruefen ob ueberschritten

				ZeitmodelltagDto zmTagDto = getZeitmodelltagZuDatum(
						personalIId, tDatum, tagesartIId_Feiertag,
						tagesartIId_Halbtag, false, theClientDto);
				if (zmTagDto != null
						&& zmTagDto.getUErlaubteanwesenheitszeit() != null
						&& zmTagDto.getUErlaubteanwesenheitszeit().getTime() != -3600000) {
					double dMaxAnwesenheit = Helper.time2Double(zmTagDto
							.getUErlaubteanwesenheitszeit());
					if (dTagesanwesenheitszeit != null
							&& dTagesanwesenheitszeit > dMaxAnwesenheit) {
						wDto.setBFehlerVorhanden(true);
						oZeileWochenabschluss[REPORT_WOCHENABSCHLUSS_FEHLER] = getTextRespectUISpr(
								"pers.wochenabschluss.tagesarbeitszeitueberschritten",
								theClientDto.getMandant(),
								theClientDto.getLocUi());
					}
				}

			} catch (javax.ejb.EJBException ex3) {

				oZeileWochenabschluss[REPORT_WOCHENABSCHLUSS_FEHLER] = "Fehler in Zeitdaten";

			} catch (EJBExceptionLP ex4) {

				oZeileWochenabschluss[REPORT_WOCHENABSCHLUSS_FEHLER] = "Fehler in Zeitdaten";
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);

			}

			if (nMaxWochenanwesenheit != null
					&& nKumuliertesWochenist.doubleValue() > nMaxWochenanwesenheit
							.doubleValue()) {
				wDto.setBFehlerVorhanden(true);
				String sMsgVorhanden = (String) oZeileWochenabschluss[REPORT_WOCHENABSCHLUSS_FEHLER];
				if (sMsgVorhanden != null) {
					sMsgVorhanden += ";"
							+ getTextRespectUISpr(
									"pers.wochenabschluss.wochenarbeitszeitueberschritten",
									theClientDto.getMandant(),
									theClientDto.getLocUi());
				} else {
					sMsgVorhanden = getTextRespectUISpr(
							"pers.wochenabschluss.wochenarbeitszeitueberschritten",
							theClientDto.getMandant(), theClientDto.getLocUi());
				}
				oZeileWochenabschluss[REPORT_WOCHENABSCHLUSS_FEHLER] = sMsgVorhanden;
			}

			if (geht != null && bNurWarnung == true) {

				Calendar cUhrzeit = Calendar.getInstance();
				cUhrzeit.setTimeInMillis(geht.getTime());

				Calendar cDatumMitUhrzeit = Calendar.getInstance();
				cDatumMitUhrzeit.setTimeInMillis(tDatum.getTime());
				cDatumMitUhrzeit.set(Calendar.HOUR_OF_DAY,
						cUhrzeit.get(Calendar.HOUR_OF_DAY));
				cDatumMitUhrzeit.set(Calendar.MINUTE,
						cUhrzeit.get(Calendar.MINUTE));
				cDatumMitUhrzeit.set(Calendar.SECOND,
						cUhrzeit.get(Calendar.SECOND));
				cDatumMitUhrzeit.set(Calendar.MILLISECOND,
						cUhrzeit.get(Calendar.MILLISECOND));

				String sMeldung = erstelleAutomatischeMindestpause(
						new Timestamp(cDatumMitUhrzeit.getTimeInMillis()),
						personalIId, theClientDto);

				if (sMeldung != null) {
					wDto.setBFehlerVorhanden(true);
					String sMsgVorhanden = (String) oZeileWochenabschluss[REPORT_WOCHENABSCHLUSS_FEHLER];
					if (sMsgVorhanden != null) {
						sMsgVorhanden += ";" + sMeldung;
					} else {
						sMsgVorhanden = sMeldung;
					}
					oZeileWochenabschluss[REPORT_WOCHENABSCHLUSS_FEHLER] = sMsgVorhanden;
				}

			}

			// Belegzeiten verdichtet

			String sQueryBelegzeiten = "SELECT distinct zeitdaten.c_belegartnr,zeitdaten.i_belegartid,zeitdaten.i_belegartpositionid  from FLRZeitdaten zeitdaten WHERE zeitdaten.flrpersonal.i_id="
					+ personalDto.getIId()
					+ "AND zeitdaten.c_belegartnr is not null AND zeitdaten.t_zeit>='"
					+ Helper.formatTimestampWithSlashes(Helper
							.cutTimestamp(tDatum))
					+ "' AND zeitdaten.t_zeit<'"
					+ Helper.formatTimestampWithSlashes(Helper
							.cutTimestamp(new Timestamp(
									tDatum.getTime() + 24 * 3600000))) + "'";

			String[] fieldnames = new String[] { "Belegart", "Belegnummer",
					"Bezeichnung", "Partner", "Dauer" };

			Session session = FLRSessionFactory.getFactory().openSession();

			org.hibernate.Query query = session.createQuery(sQueryBelegzeiten);

			List<?> resultList = query.list();

			Iterator<?> resultListIterator = resultList.iterator();

			ArrayList alDataSub = new ArrayList();

			while (resultListIterator.hasNext()) {
				Object[] o = (Object[]) resultListIterator.next();

				String belegartCNr = (String) o[0];
				Integer belegartId = (Integer) o[1];
				Integer belegartpositionId = (Integer) o[2];

				BelegInfos bi = null;
				try {
					bi = getLagerFac().getBelegInfos(belegartCNr, belegartId,
							null, theClientDto);
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				Object[] oZeileSub = new Object[5];
				oZeileSub[0] = belegartCNr;
				oZeileSub[1] = bi.getBelegnummer();
				oZeileSub[2] = bi.getBelegbezeichnung();
				oZeileSub[3] = bi.getKundeLieferant();
				if (belegartId != null) {

					oZeileSub[4] = getSummeZeitenEinesBeleges(belegartCNr,
							belegartId, null, personalIId,
							Helper.cutTimestamp(tDatum),
							Helper.cutTimestamp(new Timestamp(
									tDatum.getTime() + 24 * 3600000)),
							theClientDto);
				} else {
					int z = 0;
				}
				alDataSub.add(oZeileSub);
			}

			Object[][] dataSub = new Object[alDataSub.size()][fieldnames.length];
			dataSub = (Object[][]) alDataSub.toArray(dataSub);

			oZeileWochenabschluss[REPORT_WOCHENABSCHLUSS_SUBREPORT_BELEGZEITEN] = new LPDatenSubreport(
					dataSub, fieldnames);

			alDaten.add(oZeileWochenabschluss);

		}

		Object[][] returnArray = new Object[alDaten.size()][REPORT_WOCHENABSCHLUSS_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(returnArray);

		sAktuellerReport = ZeiterfassungFac.REPORT_WOCHENABSCHLUSS;

		initJRDS(parameter, ZeiterfassungReportFac.REPORT_MODUL,
				ZeiterfassungFac.REPORT_WOCHENABSCHLUSS,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		JasperPrintLP print = getReportPrint();

		if (print != null) {
			PrintInfoDto values = new PrintInfoDto();
			values.setDocPath(new DocPath(new DocNodeWochenabschlussBeleg(
					personalDto, personalDto.getPartnerDto(), tKWBis)));
			print.setOInfoForArchive(values);
		}

		wDto.setJasperPrintLP(getReportPrint());

		return wDto;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printWochenabrechnung(Integer personalIId,
			Timestamp tVon, Timestamp tBis, TheClientDto theClientDto,
			Integer iOption, Boolean bPlusVersteckte, boolean bNurAnwesende)
			throws EJBExceptionLP {
		JasperPrintLP print = null;
		try {

			PersonalDto[] personalDtos = null;

			if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_SELEKTIERTE_PERSON) {

				if (personalIId == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
							new Exception("personalIId == null"));
				}

				personalDtos = new PersonalDto[1];

				personalDtos[0] = getPersonalFac().personalFindByPrimaryKey(
						personalIId, theClientDto);

			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_PERSONEN) {
				personalDtos = getPersonalFac().personalFindByMandantCNr(
						theClientDto.getMandant(), bPlusVersteckte);
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_MEINE_ABTEILUNG) {
				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKey(personalIId, theClientDto);
				personalDtos = getPersonalFac()
						.personalFindAllPersonenMeinerAbteilung(
								personalDto.getKostenstelleIIdAbteilung(),
								theClientDto.getMandant(), bPlusVersteckte);
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_ARBEITER) {
				personalDtos = getPersonalFac()
						.personalFindAllArbeiterEinesMandanten(
								theClientDto.getMandant(), bPlusVersteckte);
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_ANGESTELLTE) {
				personalDtos = getPersonalFac()
						.personalFindAllAngestellteEinesMandanten(
								theClientDto.getMandant(), bPlusVersteckte);
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
						"OPTION NICHT VERFUEGBAR"));
			}

			if (bNurAnwesende) {
				personalDtos = getZeiterfassungFac()
						.entferneNichtAnwesendePersonen(tVon, tBis,
								personalDtos, theClientDto);
			}

			for (int i = 0; i < personalDtos.length; i++) {

				try {
					if (print != null) {

						print = Helper.addReport2Report(
								print,
								erstelleWochenabrechnung(
										personalDtos[i].getIId(), tVon, tBis,
										false, false, theClientDto).getPrint());
					} else {
						print = erstelleWochenabrechnung(
								personalDtos[i].getIId(), tVon, tBis, false,
								false, theClientDto);
					}
				} catch (javax.ejb.EJBException ex1) {
					if (ex1.getCause() instanceof EJBExceptionLP) {
						if (((EJBExceptionLP) ex1.getCause()).getCode() == EJBExceptionLP.FEHLER_PERSONAL_FEHLER_BEI_EINTRITTSDATUM
								&& iOption.intValue() != ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_SELEKTIERTE_PERSON) {
							// NICHTS - PERSON WIRD AUSGELASSEN
						} else {
							throw new EJBExceptionLP(ex1);
						}

					} else {
						throw new EJBExceptionLP(ex1);
					}

				}
			}
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		return print;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printWochenjournal(Integer personalIId,
			Timestamp tVon, Timestamp tBis, TheClientDto theClientDto,
			Integer iOption, Boolean bPlusVersteckte, boolean bNurAnwesende) {
		JasperPrintLP print = null;
		try {

			PersonalDto[] personalDtos = null;

			if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_SELEKTIERTE_PERSON) {

				if (personalIId == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
							new Exception("personalIId == null"));
				}

				personalDtos = new PersonalDto[1];

				personalDtos[0] = getPersonalFac().personalFindByPrimaryKey(
						personalIId, theClientDto);

			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_PERSONEN) {
				personalDtos = getPersonalFac().personalFindByMandantCNr(
						theClientDto.getMandant(), bPlusVersteckte);
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_MEINE_ABTEILUNG) {
				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKey(personalIId, theClientDto);
				personalDtos = getPersonalFac()
						.personalFindAllPersonenMeinerAbteilung(
								personalDto.getKostenstelleIIdAbteilung(),
								theClientDto.getMandant(), bPlusVersteckte);
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_ARBEITER) {
				personalDtos = getPersonalFac()
						.personalFindAllArbeiterEinesMandanten(
								theClientDto.getMandant(), bPlusVersteckte);
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_ANGESTELLTE) {
				personalDtos = getPersonalFac()
						.personalFindAllAngestellteEinesMandanten(
								theClientDto.getMandant(), bPlusVersteckte);
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
						"OPTION NICHT VERFUEGBAR"));
			}

			if (bNurAnwesende) {
				personalDtos = getZeiterfassungFac()
						.entferneNichtAnwesendePersonen(tVon, tBis,
								personalDtos, theClientDto);
			}

			for (int i = 0; i < personalDtos.length; i++) {

				try {
					if (print != null) {

						print = Helper.addReport2Report(
								print,
								erstelleWochenabrechnung(
										personalDtos[i].getIId(), tVon, tBis,
										true, true, theClientDto).getPrint());
					} else {
						print = erstelleWochenabrechnung(
								personalDtos[i].getIId(), tVon, tBis, true,
								true, theClientDto);
					}
				} catch (javax.ejb.EJBException ex1) {
					if (ex1.getCause() instanceof EJBExceptionLP) {
						if (((EJBExceptionLP) ex1.getCause()).getCode() == EJBExceptionLP.FEHLER_PERSONAL_FEHLER_BEI_EINTRITTSDATUM
								&& iOption.intValue() != ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_SELEKTIERTE_PERSON) {
							// NICHTS - PERSON WIRD AUSGELASSEN
						} else {
							throw new EJBExceptionLP(ex1);
						}

					} else {
						throw new EJBExceptionLP(ex1);
					}

				}
			}
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		return print;
	}

	public PersonalDto[] getPersonenDieZeitmodellVerwenden(
			Integer zeitmodellIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria personal = session
				.createCriteria(FLRPersonal.class);
		personal.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));

		List<?> resultListArtikel = personal.list();

		Iterator<?> resultListIterator = resultListArtikel.iterator();
		ArrayList<PersonalDto> al = new ArrayList<PersonalDto>();
		while (resultListIterator.hasNext()) {
			FLRPersonal person = (FLRPersonal) resultListIterator.next();
			try {
				PersonalzeitmodellDto zeitmodellDto = getPersonalFac()
						.personalzeitmodellFindZeitmodellZuDatum(
								person.getI_id(),
								new Timestamp(System.currentTimeMillis()),
								theClientDto);

				if (zeitmodellDto != null) {
					if (zeitmodellIId.equals(zeitmodellDto.getZeitmodellIId())) {
						al.add(getPersonalFac().personalFindByPrimaryKey(
								person.getI_id(), theClientDto));
					}
				}
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
		}
		PersonalDto[] returnArray = new PersonalDto[al.size()];
		return (PersonalDto[]) al.toArray(returnArray);
	}

	public MaschinenzeitdatenDto[] getZeitdatenEinerMaschine(
			Integer maschineIId, java.sql.Timestamp tZeitenVon,
			java.sql.Timestamp tZeitenBis,

			TheClientDto theClientDto) {
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		org.hibernate.Criteria crit = session
				.createCriteria(FLRMaschinenzeitdaten.class)
				.createAlias(ZeiterfassungFac.FLR_ZEITDATEN_FLRMASCHINE, "m")
				.add(Restrictions.eq("m.mandant_c_nr",
						theClientDto.getMandant()));

		if (maschineIId != null) {
			crit.add(Restrictions.eq("maschine_i_id", maschineIId));
		}

		crit.add(Restrictions.ge(ZeiterfassungFac.FLR_MASCHINENZEITDATEN_T_VON,
				tZeitenVon));
		crit.add(Restrictions.lt(ZeiterfassungFac.FLR_MASCHINENZEITDATEN_T_VON,
				tZeitenBis));

		crit.addOrder(Order.asc(ZeiterfassungFac.FLR_MASCHINENZEITDATEN_T_VON));

		List<?> resultList = crit.list();

		Iterator<?> resultListIterator = resultList.iterator();

		MaschinenzeitdatenDto[] dtos = new MaschinenzeitdatenDto[resultList
				.size()];
		int row = 0;
		while (resultListIterator.hasNext()) {
			FLRMaschinenzeitdaten flrzeitdaten = (FLRMaschinenzeitdaten) resultListIterator
					.next();
			MaschinenzeitdatenDto dto = new MaschinenzeitdatenDto();
			dto.setIId(flrzeitdaten.getI_id());
			dto.setLossollarbeitsplanIId(flrzeitdaten
					.getFlrlossollarbeitsplan().getI_id());
			dto.setTVon(new Timestamp(flrzeitdaten.getT_von().getTime()));
			if (flrzeitdaten.getT_bis() == null) {
				dto.setTBis(new Timestamp(System.currentTimeMillis()));
			} else {
				dto.setTBis(new Timestamp(flrzeitdaten.getT_bis().getTime()));
			}
			dto.setMaschineIId(flrzeitdaten.getMaschine_i_id());
			dto.setCBemerkung(flrzeitdaten.getC_bemerkung());

			dtos[row] = dto;
			row++;

		}

		return dtos;
	}

	public Map getAllMaschinen(TheClientDto theClientDto) {

		LinkedHashMap<String, String> tmArten = new LinkedHashMap<String, String>();

		String sQuery = "select m FROM FLRMaschine m WHERE m.mandant_c_nr = '"
				+ theClientDto.getMandant()
				+ "' ORDER BY m.flrmaschinengruppe.c_bez, m.c_bez ASC";

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Query query = session.createQuery(sQuery);

		List<?> resultList = query.list();

		Iterator<?> resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			FLRMaschine m = (FLRMaschine) resultListIterator.next();

			if (!tmArten.containsValue(m.getFlrmaschinengruppe().getC_bez())) {
				tmArten.put("G" + m.getFlrmaschinengruppe().getI_id(), m
						.getFlrmaschinengruppe().getC_bez());
			}

			String cBez = "";
			if (m.getC_bez() != null) {
				cBez = m.getC_bez();
			}

			cBez = "    " + cBez;

			tmArten.put("M" + m.getI_id(), cBez);

		}

		return tmArten;
	}

	public ArrayList<AuftragzeitenDto> getAllTelefonzeitenEinesProjekts(
			Integer projektIId, Integer personalIId, java.sql.Timestamp tVon,
			java.sql.Timestamp tZeitenBis, TheClientDto theClientDto) {

		ArrayList<AuftragzeitenDto> alDaten = new ArrayList<AuftragzeitenDto>();

		// PJ18444 Telefonzeiten hinzufuegen
		int iOption = 0;
		ParametermandantDto parameter = null;
		try {
			parameter = (ParametermandantDto) getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_PERSONAL,
							ParameterFac.PARAMETER_PERSONALKOSTEN_QUELLE);

			iOption = ((Integer) parameter.getCWertAsObject()).intValue();

		} catch (RemoteException ex5) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
		}

		HashMap hmGestpreise = new HashMap();
		ArtikelDto artikelDtoDefaultArbeiztszeit = getDefaultArbeitszeitartikel(theClientDto);

		Session sessionTelefon = FLRSessionFactory.getFactory().openSession();

		String sQuerytelefon = "SELECT tz FROM FLRTelefonzeiten tz WHERE tz.projekt_i_id="
				+ projektIId;

		if (tVon != null) {

			sQuerytelefon += " AND tz.t_von>='"
					+ Helper.formatTimestampWithSlashes(Helper
							.cutTimestamp(tVon)) + "'";

		}

		if (tZeitenBis != null) {
			sQuerytelefon += " AND tz.t_bis<='"
					+ Helper.formatTimestampWithSlashes(tZeitenBis) + "'";
		}

		if (personalIId != null) {

			sQuerytelefon += " AND tz.personal_i_id=" + personalIId;

		}

		org.hibernate.Query queryTelefon = sessionTelefon
				.createQuery(sQuerytelefon);

		List<?> resultListTelefon = queryTelefon.list();
		Iterator<?> resultListIteratorTelefon = resultListTelefon.iterator();
		while (resultListIteratorTelefon.hasNext()) {

			FLRTelefonzeiten flrTelefonzeiten = (FLRTelefonzeiten) resultListIteratorTelefon
					.next();

			// 2 Zeilen hinzufuegen

			AuftragzeitenDto dtoTemp = new AuftragzeitenDto();
			dtoTemp.setBTelefonzeit(true);
			dtoTemp.setSArtikelcnr(artikelDtoDefaultArbeiztszeit.getCNr());
			dtoTemp.setArtikelIId(artikelDtoDefaultArbeiztszeit.getIId());

			if (artikelDtoDefaultArbeiztszeit.getArtikelsprDto() != null) {
				dtoTemp.setSArtikelbezeichnung(artikelDtoDefaultArbeiztszeit
						.getArtikelsprDto().getCBez());
				dtoTemp.setSArtikelzusatzbezeichnung(artikelDtoDefaultArbeiztszeit
						.getArtikelsprDto().getCZbez());
			}
			dtoTemp.setTsBeginn(new Timestamp(flrTelefonzeiten.getT_von()
					.getTime()));
			dtoTemp.setTsEnde(new Timestamp(flrTelefonzeiten.getT_bis()
					.getTime()));

			java.sql.Time tDauer = new java.sql.Time(flrTelefonzeiten
					.getT_bis().getTime()
					- flrTelefonzeiten.getT_von().getTime());
			tDauer.setTime(tDauer.getTime() - 3600000);
			dtoTemp.setTDauer(tDauer);
			Double dDauer = Helper.time2Double(tDauer);
			dtoTemp.setDdDauer(dDauer);
			dtoTemp.setSBewegungsart(ZeiterfassungFac.TAETIGKEIT_TELEFON);

			String sName = flrTelefonzeiten.getFlrpersonal().getFlrpartner()
					.getC_name1nachnamefirmazeile1();
			if (flrTelefonzeiten.getFlrpersonal().getFlrpartner()
					.getC_name2vornamefirmazeile2() != null) {
				sName = flrTelefonzeiten.getFlrpersonal().getFlrpartner()
						.getC_name2vornamefirmazeile2()
						+ " " + sName;
			}

			dtoTemp.setSPersonalMaschinenname(sName);

			String sNameNachnameVorname = flrTelefonzeiten.getFlrpersonal()
					.getFlrpartner().getC_name1nachnamefirmazeile1();
			if (flrTelefonzeiten.getFlrpersonal().getFlrpartner()
					.getC_name2vornamefirmazeile2() != null) {
				sNameNachnameVorname = sNameNachnameVorname
						+ " "
						+ flrTelefonzeiten.getFlrpersonal().getFlrpartner()
								.getC_name2vornamefirmazeile2();
			}

			dtoTemp.setsPersonNachnameVorname(sNameNachnameVorname);

			dtoTemp.setSPersonalKurzzeichen(flrTelefonzeiten.getFlrpersonal()
					.getC_kurzzeichen());

			dtoTemp.setSPersonalnummer(flrTelefonzeiten.getFlrpersonal()
					.getC_personalnummer());
			dtoTemp.setSZeitbuchungtext(flrTelefonzeiten.getX_kommentarext());

			BigDecimal bdKostenProStunde = getPersonalKostenProStunde(
					theClientDto, hmGestpreise, iOption,
					artikelDtoDefaultArbeiztszeit.getIId(), flrTelefonzeiten
							.getFlrpersonal().getI_id(), new Timestamp(
							flrTelefonzeiten.getT_von().getTime()));
			dtoTemp.setBdKosten(bdKostenProStunde.multiply(new BigDecimal(
					dDauer)));

			alDaten.add(dtoTemp);

		}
		sessionTelefon.close();

		return alDaten;
	}

	public ArtikelDto getDefaultArbeitszeitartikel(TheClientDto theClientDto) {
		ArtikelDto artikelDtoDefaultArbeiztszeit = null;
		try {
			ParametermandantDto parameterDtoDefaultarbeitszeit = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_DEFAULT_ARBEITSZEITARTIKEL);

			if (parameterDtoDefaultarbeitszeit != null
					&& parameterDtoDefaultarbeitszeit.getCWert() != null
					&& !parameterDtoDefaultarbeitszeit.getCWert().trim()
							.equals("")) {
				try {

					artikelDtoDefaultArbeiztszeit = getArtikelFac()
							.artikelFindByCNr(
									parameterDtoDefaultarbeitszeit.getCWert(),
									theClientDto);

				} catch (EJBExceptionLP ex2) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DEFAULT_ARBEITSZEITARTIKEL_NICHT_DEFINIERT,
							new Exception(
									"FEHLER_DEFAULT_ARBEITSZEITARTIKEL_NICHT_DEFINIERT"));
				}
			} else {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DEFAULT_ARBEITSZEITARTIKEL_NICHT_DEFINIERT,
						new Exception(
								"FEHLER_DEFAULT_ARBEITSZEITARTIKEL_NICHT_DEFINIERT"));
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return artikelDtoDefaultArbeiztszeit;
	}

	public AuftragzeitenDto[] getAllMaschinenzeitenEinesBeleges(Integer losIId,
			Integer lossollarbeitsplanIId, java.sql.Timestamp tVon,
			java.sql.Timestamp tZeitenBis, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (losIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("losIId == null"));
		}

		boolean bTheoretischeIstZeit = false;

		try {
			ParametermandantDto parameterIstZeit = (ParametermandantDto) getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_PERSONAL,
							ParameterFac.PARAMETER_THEORETISCHE_IST_ZEIT_RECHNUNG);

			bTheoretischeIstZeit = ((Boolean) parameterIstZeit
					.getCWertAsObject());

		} catch (RemoteException ex5) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
		}

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		String s = "SELECT m, (SELECT spr.c_bez FROM FLRArtikellistespr as spr WHERE spr.Id.artikelliste=m.flrlossollarbeitsplan.flrartikel.i_id AND spr.Id.locale='"
				+ theClientDto.getLocUiAsString()
				+ "' ),(SELECT spr.c_zbez FROM FLRArtikellistespr as spr WHERE spr.Id.artikelliste=m.flrlossollarbeitsplan.flrartikel.i_id AND spr.Id.locale='"
				+ theClientDto.getLocUiAsString()
				+ "' ) FROM FLRMaschinenzeitdaten m WHERE 1=1 ";

		if (losIId != null) {
			s += " AND m.flrlossollarbeitsplan.flrlos.i_id=" + losIId;
		}

		if (lossollarbeitsplanIId != null) {
			s += " AND m.flrlossollarbeitsplan.i_id=" + lossollarbeitsplanIId;
		}

		if (bTheoretischeIstZeit == true) {
			// Es gibt dann fuer Umspannzeit/Laufzeit nur die Theoretische
			// IST-Zeit
			s += " AND m.flrlossollarbeitsplan.agart_c_nr IS NULL ";
		}
		if (tVon != null) {
			s += " AND m.t_von>='" + Helper.formatTimestampWithSlashes(tVon)
					+ "' ";
		}
		if (tZeitenBis != null) {
			s += " AND m.t_bis<'"
					+ Helper.formatTimestampWithSlashes(tZeitenBis) + "'";
		}

		s += " ORDER BY m.t_von";

		org.hibernate.Query inventurliste = session.createQuery(s);

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListIterator = resultList.iterator();
		ArrayList<AuftragzeitenDto> al = new ArrayList<AuftragzeitenDto>();
		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();
			FLRMaschinenzeitdaten flrBelege = (FLRMaschinenzeitdaten) o[0];

			AuftragzeitenDto dto = new AuftragzeitenDto();

			dto.setZeitdatenIIdBelegbuchung(flrBelege.getI_id());
			dto.setSZeitbuchungtext(flrBelege.getC_bemerkung());
			dto.setBelegpositionIId(flrBelege.getFlrlossollarbeitsplan()
					.getI_id());
			dto.setSPersonalnummer(flrBelege.getFlrmaschine()
					.getC_inventarnummer());
			dto.setMaschinengruppe(flrBelege.getFlrmaschine()
					.getFlrmaschinengruppe().getC_bez());
			dto.setIPersonalMaschinenId(flrBelege.getFlrmaschine().getI_id());

			dto.setSArtikelcnr(flrBelege.getFlrlossollarbeitsplan()
					.getFlrartikel().getC_nr());

			dto.setSArtikelbezeichnung((String) o[1]);
			dto.setSArtikelzusatzbezeichnung((String) o[2]);

			dto.setArtikelIId(flrBelege.getFlrlossollarbeitsplan()
					.getFlrartikel().getI_id());

			String maschinenname = "M:";
			if (flrBelege.getFlrmaschine().getC_identifikationsnr() != null) {
				maschinenname += flrBelege.getFlrmaschine()
						.getC_identifikationsnr() + " ";
			}

			dto.setiArbeitsgang(flrBelege.getFlrlossollarbeitsplan()
					.getI_arbeitsgangsnummer());
			dto.setiUnterarbeitsgang(flrBelege.getFlrlossollarbeitsplan()
					.getI_unterarbeitsgang());

			maschinenname += flrBelege.getFlrmaschine().getC_bez();
			dto.setSPersonalMaschinenname(maschinenname);
			dto.setsPersonNachnameVorname(maschinenname);
			dto.setTsBeginn(new Timestamp(flrBelege.getT_von().getTime()));

			Timestamp tBis = null;

			if (flrBelege.getT_bis() != null) {
				tBis = new Timestamp(flrBelege.getT_bis().getTime());
			} else {
				tBis = new Timestamp(System.currentTimeMillis());
			}

			dto.setTsEnde(tBis);

			java.sql.Time tDauer = new java.sql.Time(tBis.getTime()
					- flrBelege.getT_von().getTime());
			tDauer.setTime(tDauer.getTime() - 3600000);
			dto.setTDauer(tDauer);
			Double dDauer = ((double) tBis.getTime() - (double) flrBelege
					.getT_von().getTime()) / (double) 3600000;
			dto.setDdDauer(dDauer);

			BigDecimal stundensatz = getMaschinenKostenZumZeitpunkt(flrBelege
					.getFlrmaschine().getI_id(), new Timestamp(flrBelege
					.getT_von().getTime()));

			dto.setBdKosten(stundensatz.multiply(new BigDecimal(dDauer
					.doubleValue())));

			al.add(dto);

		}
		session.close();

		AuftragzeitenDto[] azDtos = new AuftragzeitenDto[al.size()];
		for (int i = 0; i < al.size(); i++) {
			azDtos[i] = (AuftragzeitenDto) al.get(i);
		}

		// PJ 15810 Wenn Los, dann Theoretische Ist-Zeit berechnen

		if (bTheoretischeIstZeit == true) {
			azDtos = theoretischeIstzeitHinzufuegen(azDtos, losIId,
					lossollarbeitsplanIId, null, null, null, true, theClientDto);
		}

		for (int i = azDtos.length - 1; i > 0; --i) {
			for (int j = 0; j < i; ++j) {
				if (azDtos[j].getSPersonalMaschinenname().compareTo(
						azDtos[j + 1].getSPersonalMaschinenname()) > 0) {
					AuftragzeitenDto tauschDto = azDtos[j];
					azDtos[j] = azDtos[j + 1];
					azDtos[j + 1] = tauschDto;
				}
			}
		}

		return azDtos;
	}

	public boolean sindBelegzeitenVorhanden(String cBelegartnr,
			Integer belegartIId) {
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();
		org.hibernate.Criteria zeitdatenEinesBelegs = session
				.createCriteria(FLRZeitdaten.class);

		zeitdatenEinesBelegs.add(Expression.eq(
				ZeiterfassungFac.FLR_ZEITDATEN_I_BELEGARTID, belegartIId));
		zeitdatenEinesBelegs.add(Expression.eq(
				ZeiterfassungFac.FLR_ZEITDATEN_C_BELEGARTNR, cBelegartnr));

		List<?> resultList = zeitdatenEinesBelegs.list();

		if (resultList.size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	public boolean sindZuvieleZeitdatenEinesBelegesVorhanden(
			String belegartCNr, Integer belegartIId, TheClientDto theClientDto) {

		int iAnzahl = 50;

		ParametermandantDto parameter = null;
		try {
			parameter = (ParametermandantDto) getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_PERSONAL,
							ParameterFac.PARAMETER_MAXIMALE_EINTRAEGE_SOLLZEITPRUEFUNG);

			iAnzahl = ((Integer) parameter.getCWertAsObject()).intValue();

		} catch (RemoteException ex5) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
		}

		String sQuery = "select count(*) FROM FLRZeitdaten zeitdaten WHERE zeitdaten.c_belegartnr='"
				+ belegartCNr + "' AND zeitdaten.i_belegartid=" + belegartIId;

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		org.hibernate.Query inventurliste = session.createQuery(sQuery);

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListIterator = resultList.iterator();

		if (resultListIterator.hasNext()) {
			Long l = (Long) resultListIterator.next();
			if (l > iAnzahl) {
				return true;
			}
		}

		return false;

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public AuftragzeitenDto[] getAllZeitenEinesBeleges(String belegartCNr,
			Integer belegartIId, Integer belegartpositionIId,
			Integer personalIId, java.sql.Timestamp tZeitenVon,
			java.sql.Timestamp tZeitenBis, boolean bOrderByArtikelCNr,
			boolean bOrberByPersonal, TheClientDto theClientDto) {
		return getAllZeitenEinesBeleges(belegartCNr, belegartIId,
				belegartpositionIId, personalIId, tZeitenVon, tZeitenBis,
				bOrderByArtikelCNr, bOrberByPersonal, true, theClientDto);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public AuftragzeitenDto[] getAllZeitenEinesBeleges(String belegartCNr,
			Integer belegartIId, Integer belegartpositionIId,
			Integer personalIId, java.sql.Timestamp tZeitenVon,
			java.sql.Timestamp tZeitenBis, boolean bOrderByArtikelCNr,
			boolean bOrberByPersonal, boolean bBeruecksichtigeLeistungsfaktor,
			TheClientDto theClientDto) {
		if (belegartCNr == null || belegartIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("belegartCNr == null || belegartIId == null"));
		}

		boolean bTheoretischeIstZeit = false;
		boolean bVonBisErfassung = false;

		try {
			ParametermandantDto parameterIstZeit = (ParametermandantDto) getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_PERSONAL,
							ParameterFac.PARAMETER_THEORETISCHE_IST_ZEIT_RECHNUNG);

			bTheoretischeIstZeit = ((Boolean) parameterIstZeit
					.getCWertAsObject());

			ParametermandantDto parameterVonBis = (ParametermandantDto) getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_PERSONAL,
							ParameterFac.PARAMETER_VON_BIS_ERFASSUNG);

			bVonBisErfassung = ((Boolean) parameterVonBis.getCWertAsObject());

		} catch (RemoteException ex5) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
		}

		// Hole id der Taetigkeit GEHT
		Integer taetigkeitIId_Geht = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_GEHT, theClientDto).getIId();
		// Hole id der Taetigkeit ENDE
		Integer taetigkeitIId_Ende = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_ENDE, theClientDto).getIId();
		// Hole id der Taetigkeit UNTER
		Integer taetigkeitIId_Unter = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_UNTER, theClientDto).getIId();
		// try {

		String sQueryAuftragzeiten = "SELECT z,(SELECT spr.c_bez FROM FLRArtikellistespr as spr WHERE spr.Id.artikelliste=z.flrartikel.i_id AND spr.Id.locale='"
				+ theClientDto.getLocUiAsString()
				+ "' ),(SELECT spr.c_zbez FROM FLRArtikellistespr as spr WHERE spr.Id.artikelliste=z.flrartikel.i_id AND spr.Id.locale='"
				+ theClientDto.getLocUiAsString()
				+ "' ) FROM FLRZeitdatenLos z WHERE z.c_belegartnr='"
				+ belegartCNr + "' AND z.i_belegartid=" + belegartIId;

		if (personalIId != null) {

			sQueryAuftragzeiten += " AND z.personal_i_id=" + personalIId;

		}
		if (belegartpositionIId != null) {
			sQueryAuftragzeiten += " AND z.i_belegartpositionid="
					+ belegartpositionIId;
		}

		if (bTheoretischeIstZeit == true
				&& belegartCNr.equals(LocaleFac.BELEGART_LOS)) {
			sQueryAuftragzeiten += " AND z.flrlossollarbeitsplan.agart_c_nr IS NULL ";
		}

		// WG MITTERNACHTSSPRUNG
		// Daten einen Tag vorher holen wg. Mitternachtssprung

		if (tZeitenVon != null) {

			sQueryAuftragzeiten += " AND z.t_zeit>='"
					+ Helper.formatTimestampWithSlashes(Helper
							.cutTimestamp(new Timestamp(tZeitenVon.getTime()
									- (3600000 * 24)))) + "'";

		}

		if (tZeitenBis != null) {
			sQueryAuftragzeiten += " AND z.t_zeit<'"
					+ Helper.formatTimestampWithSlashes(tZeitenBis) + "'";
		}

		sQueryAuftragzeiten += " ORDER BY z.t_zeit ASC";

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Query zeiten = session.createQuery(sQueryAuftragzeiten);

		List<?> resultList = zeiten.list();

		Iterator<?> resultListAuftraege = resultList.iterator();

		ArrayList<ZeitdatenDtoBelegzeiten[]> alAuftragszeit = new ArrayList<ZeitdatenDtoBelegzeiten[]>();

		HashMap hmGestpreise = new HashMap();

		while (resultListAuftraege.hasNext()) {
			Object[] o = (Object[]) resultListAuftraege.next();
			FLRZeitdatenLos auftragszeit = (FLRZeitdatenLos) o[0];

			Timestamp tBis = Helper.cutTimestamp(new Timestamp(auftragszeit
					.getT_zeit().getTime() + 24 * 3600000));

			String sQuery = "select zeitdaten FROM FLRZeitdaten zeitdaten WHERE zeitdaten.t_zeit>'"
					+ Helper.formatTimestampWithSlashes(new Timestamp(
							auftragszeit.getT_zeit().getTime()))
					+ "' AND zeitdaten.personal_i_id="
					+ auftragszeit.getPersonal_i_id()
					+ " AND zeitdaten.taetigkeit_i_id="
					+ taetigkeitIId_Geht
					+ " ORDER BY zeitdaten.t_zeit ASC";

			Session session2 = FLRSessionFactory.getFactory().openSession();

			org.hibernate.Query naechstesGeht = session2.createQuery(sQuery);
			naechstesGeht.setMaxResults(1);

			List<?> resultListNaechstesGeht = naechstesGeht.list();

			Iterator<?> resultListIterator = resultListNaechstesGeht.iterator();

			boolean bZeitIstOffen = false;

			if (resultListIterator.hasNext()) {
				FLRZeitdaten l = (FLRZeitdaten) resultListIterator.next();
				tBis = new Timestamp(l.getT_zeit().getTime() + 5);
				if (tZeitenVon != null && tBis.before(tZeitenVon)) {
					continue;
				}
			} else {
				bZeitIstOffen = true;
			}

			Query query = em
					.createNamedQuery("ZeitdatenfindZeitdatenEinesTagesUndEinerPerson");
			query.setParameter(1, auftragszeit.getPersonal_i_id());
			query.setParameter(2, auftragszeit.getT_zeit());
			query.setParameter(3, tBis);
			ZeitdatenDto[] tagesDaten = assembleZeitdatenDtos(query
					.getResultList());

			// Hier Mitternachtssprung simulieren, d.h. wenn die letzte Buchung
			// kein Geht ist, dann das naechste Geht suchen
			if (tZeitenVon != null) {
				for (int i = 0; i < tagesDaten.length; i++) {
					if (tagesDaten[i].getTZeit().before(tZeitenVon)) {
						tagesDaten[i].setTZeit(tZeitenVon);
					}
				}
			}
			if (tZeitenBis != null) {
				for (int i = 0; i < tagesDaten.length; i++) {
					if (tagesDaten[i].getTZeit().after(tZeitenBis)) {
						tagesDaten[i].setTZeit(tZeitenBis);
					}
				}
			}

			if (bZeitIstOffen == true) {
				// Einen ENDE Eintrag hinzufuegen

				ArrayList<ZeitdatenDto> alNeu = new ArrayList<ZeitdatenDto>();

				ZeitdatenDto zDtoOffen = ZeitdatenDto.clone(tagesDaten[0]);

				for (int i = 0; i < tagesDaten.length; i++) {
					alNeu.add(tagesDaten[i]);
				}

				// SP3044 Wenn vorher auch noch ein Pause-Beginn jedoch kein
				// Pause Ende gebucht wurde, dann muss die Pause ebenfalls
				// beendet werden
				ArrayList<ZeitdatenDto> alUnter = new ArrayList<ZeitdatenDto>();
				for (int i = 0; i < alNeu.size(); i++) {
					ZeitdatenDto zeitdatenDto = alNeu.get(i);
					if (zeitdatenDto.getTaetigkeitIId() != null
							&& zeitdatenDto.getTaetigkeitIId().equals(
									taetigkeitIId_Unter)) {
						alUnter.add(zeitdatenDto);
					}
				}

				zDtoOffen.setArtikelIId(null);
				zDtoOffen.setCBelegartnr(null);
				zDtoOffen.setIBelegartid(null);
				zDtoOffen.setIBelegartpositionid(null);

				if (alUnter.size() % 2 == 1) {
					zDtoOffen.setTaetigkeitIId(taetigkeitIId_Unter);
					zDtoOffen
							.setTZeit(new Timestamp(System.currentTimeMillis()));
					alNeu.add(zDtoOffen);
				}
				zDtoOffen = ZeitdatenDto.clone(zDtoOffen);
				zDtoOffen.setTaetigkeitIId(taetigkeitIId_Ende);
				zDtoOffen.setTZeit(new Timestamp(System.currentTimeMillis()));
				alNeu.add(zDtoOffen);

				tagesDaten = alNeu.toArray(new ZeitdatenDto[alNeu.size()]);

			}

			if (tagesDaten.length > 1) {
				int iZaehler = 0;
				for (int j = 0; j < tagesDaten.length; j++) {
					ZeitdatenDto bewegungsdatensatz = tagesDaten[j];

					iZaehler++;
					// Auftrag wird durch ENDE,GEHT oder neuen Beleg
					// abgeschlossen
					if ((bewegungsdatensatz.getTaetigkeitIId() != null && bewegungsdatensatz
							.getTaetigkeitIId().equals(taetigkeitIId_Ende))
							|| (bewegungsdatensatz.getTaetigkeitIId() != null && bewegungsdatensatz
									.getTaetigkeitIId().equals(
											taetigkeitIId_Geht))
							|| (bewegungsdatensatz.getCBelegartnr() != null
									&& bewegungsdatensatz.getIBelegartid() != null && j > 0)) {

						if (iZaehler % 2 == 1) {
							ArrayList<Object> al = new ArrayList<Object>();
							al.add(bewegungsdatensatz.getPersonalIId());
							al.add(tagesDaten[0].getTZeit());
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_IN_ZEITDATEN, al,
									new Exception());
						}

						ZeitdatenDtoBelegzeiten[] alleZeitdatenEinesAuftrages = new ZeitdatenDtoBelegzeiten[iZaehler];
						for (int l = 0; l < iZaehler; l++) {
							ZeitdatenDtoBelegzeiten dtoTemp = new ZeitdatenDtoBelegzeiten();

							if (auftragszeit.getFlrartikel() != null) {
								dtoTemp.setArtikel(auftragszeit.getFlrartikel()
										.getC_nr());
								if (auftragszeit.getFlrartikel()
										.getFlrartikelgruppe() != null) {
									dtoTemp.setArtgruIId(auftragszeit
											.getFlrartikel()
											.getFlrartikelgruppe().getI_id());
								}
								if (auftragszeit.getFlrartikel()
										.getFlrartikelklasse() != null) {
									dtoTemp.setArtklaIId(auftragszeit
											.getFlrartikel()
											.getFlrartikelklasse().getI_id());
								}
								dtoTemp.setBezeichnung((String) o[1]);
								dtoTemp.setZusatzbezeichnung((String) o[2]);
							}

							if (auftragszeit.getC_belegartnr() != null
									&& auftragszeit.getC_belegartnr().equals(
											LocaleFac.BELEGART_LOS)
									&& auftragszeit.getI_belegartpositionid() != null) {

								LossollarbeitsplanDto sapDto = getFertigungFac()
										.lossollarbeitsplanFindByPrimaryKeyOhneExc(
												auftragszeit
														.getI_belegartpositionid());

								if (sapDto != null) {
									dtoTemp.setArbeitsgang(sapDto
											.getIArbeitsgangnummer());
									dtoTemp.setUnterarbeitsgang(sapDto
											.getIUnterarbeitsgang());
								}
							}

							String sName = auftragszeit.getFlrpersonal()
									.getFlrpartner()
									.getC_name1nachnamefirmazeile1();
							if (auftragszeit.getFlrpersonal().getFlrpartner()
									.getC_name2vornamefirmazeile2() != null) {
								sName = auftragszeit.getFlrpersonal()
										.getFlrpartner()
										.getC_name2vornamefirmazeile2()
										+ " " + sName;
							}

							dtoTemp.setPerson(sName);

							String sNameNachnameVorname = auftragszeit
									.getFlrpersonal().getFlrpartner()
									.getC_name1nachnamefirmazeile1();
							if (auftragszeit.getFlrpersonal().getFlrpartner()
									.getC_name2vornamefirmazeile2() != null) {
								sNameNachnameVorname = sNameNachnameVorname
										+ " "
										+ auftragszeit.getFlrpersonal()
												.getFlrpartner()
												.getC_name2vornamefirmazeile2();
							}

							dtoTemp.setPersonNachnameVorname(sNameNachnameVorname);
							dtoTemp.setSPersonalKurzzeichen(auftragszeit
									.getFlrpersonal().getC_kurzzeichen());

							dtoTemp.setPersonalnummer(auftragszeit
									.getFlrpersonal().getC_personalnummer());

							dtoTemp.setZeitdatenDto(tagesDaten[l]);

							// Beim letzten OFFEN hinzufuegen
							// SP2596 Ausser bei Von-Bis-Erfassung

							if (bVonBisErfassung == false) {

								if (bZeitIstOffen == true && l == iZaehler - 1) {
									dtoTemp.setbOffen(true);
								}
							}

							alleZeitdatenEinesAuftrages[l] = dtoTemp;
						}

						alAuftragszeit.add(alleZeitdatenEinesAuftrages);

						break;
					}

				}
			}
		}
		// }
		// catch (NoResultException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
		// ex);
		// }

		int iOption = 0;
		ParametermandantDto parameter = null;
		try {
			parameter = (ParametermandantDto) getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_PERSONAL,
							ParameterFac.PARAMETER_PERSONALKOSTEN_QUELLE);

			iOption = ((Integer) parameter.getCWertAsObject()).intValue();

		} catch (RemoteException ex5) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
		}

		int iDatensaetze = 0;
		for (int i = 0; i < alAuftragszeit.size(); i++) {
			iDatensaetze = iDatensaetze + alAuftragszeit.get(i).length;
		}
		iDatensaetze = iDatensaetze / 2;
		AuftragzeitenDto[] azDtos = new AuftragzeitenDto[iDatensaetze];
		int iAktuellerDatensatz = 0;
		for (int i = 0; i < alAuftragszeit.size(); i++) {
			ZeitdatenDtoBelegzeiten[] einePositionDto = alAuftragszeit.get(i);
			if (einePositionDto.length > 1) {

				for (int x = 0; x < einePositionDto.length - 1; x++) {
					if (x % 2 == 0) {

						ZeitdatenDto dtoBeginn = einePositionDto[x]
								.getZeitdatenDto();
						ZeitdatenDto dtoEnde = einePositionDto[x + 1]
								.getZeitdatenDto();
						azDtos[iAktuellerDatensatz] = new AuftragzeitenDto();
						azDtos[iAktuellerDatensatz]
								.setBelegpositionIId(einePositionDto[0]
										.getZeitdatenDto()
										.getIBelegartpositionid());
						azDtos[iAktuellerDatensatz]
								.setZeitdatenIIdBelegbuchung(einePositionDto[0]
										.getZeitdatenDto().getIId());

						java.sql.Time tDauer = new java.sql.Time(dtoEnde
								.getTZeit().getTime()
								- dtoBeginn.getTZeit().getTime());
						tDauer.setTime(tDauer.getTime() - 3600000);
						azDtos[iAktuellerDatensatz].setTDauer(tDauer);
						Double dDauer = Helper.time2Double(tDauer);

						azDtos[iAktuellerDatensatz]
								.setSPersonalMaschinenname(einePositionDto[x]
										.getPerson());
						azDtos[iAktuellerDatensatz]
								.setSPersonalKurzzeichen(einePositionDto[x]
										.getSPersonalKurzzeichen());
						azDtos[iAktuellerDatensatz]
								.setsPersonNachnameVorname(einePositionDto[x]
										.getPersonNachnameVorname());
						azDtos[iAktuellerDatensatz]
								.setSPersonalnummer(einePositionDto[x]
										.getPersonalnummer().toString());
						azDtos[iAktuellerDatensatz]
								.setIPersonalMaschinenId(dtoBeginn
										.getPersonalIId());

						java.math.BigDecimal bdDauerGerundet = new BigDecimal(
								dDauer.doubleValue());
						// PJ 16661
						if (iOption == 0
								&& bBeruecksichtigeLeistungsfaktor == true) {
							// mit Leistungsfaktor multiplizieren
							try {
								Calendar c = Calendar.getInstance();
								c.setTimeInMillis(dtoBeginn.getTZeit()
										.getTime());
								PersonalgehaltDto pgDto = getPersonalFac()
										.personalgehaltFindLetztePersonalgehalt(
												dtoBeginn.getPersonalIId(),
												c.get(Calendar.YEAR),
												c.get(Calendar.MONTH));
								if (pgDto != null
										&& pgDto.getFLeistungswert() != null) {
									bdDauerGerundet = bdDauerGerundet
											.multiply(new BigDecimal(pgDto
													.getFLeistungswert())
													.divide(new BigDecimal(100),
															4,
															BigDecimal.ROUND_HALF_UP));
								}
							} catch (RemoteException ex1) {
								throwEJBExceptionLPRespectOld(ex1);
							}
						}

						bdDauerGerundet = Helper.rundeKaufmaennisch(
								bdDauerGerundet, 3);

						azDtos[iAktuellerDatensatz].setDdDauer(new Double(
								bdDauerGerundet.doubleValue()));

						if (einePositionDto[0].getZeitdatenDto()
								.getArtikelIId() != null) {

							azDtos[iAktuellerDatensatz]
									.setArtikelgruppeIId(einePositionDto[0]
											.getArtgruIId());
							azDtos[iAktuellerDatensatz]
									.setArtikelklasseIId(einePositionDto[0]
											.getArtklaIId());

							azDtos[iAktuellerDatensatz]
									.setSArtikelcnr(einePositionDto[0]
											.getArtikel());
							azDtos[iAktuellerDatensatz]
									.setArtikelIId(einePositionDto[0]
											.getZeitdatenDto().getArtikelIId());
							azDtos[iAktuellerDatensatz]
									.setSArtikelbezeichnung(einePositionDto[0]
											.getBezeichnung());

							azDtos[iAktuellerDatensatz]
									.setSArtikelzusatzbezeichnung(einePositionDto[0]
											.getZusatzbezeichnung());

							BigDecimal bdKostenProStunde = getPersonalKostenProStunde(
									theClientDto, hmGestpreise, iOption,
									einePositionDto[0].getZeitdatenDto()
											.getArtikelIId(),
									dtoBeginn.getPersonalIId(),
									einePositionDto[0].getZeitdatenDto()
											.getTZeit());
							azDtos[iAktuellerDatensatz]
									.setBdKosten(bdKostenProStunde
											.multiply(bdDauerGerundet));

							azDtos[iAktuellerDatensatz]
									.setiArbeitsgang(einePositionDto[0]
											.getArbeitsgang());
							azDtos[iAktuellerDatensatz]
									.setiUnterarbeitsgang(einePositionDto[0]
											.getUnterarbeitsgang());

						}

						String cBemerkung = dtoBeginn.getCBemerkungZuBelegart();

						if (einePositionDto[x + 1].isbOffen()) {

							if (cBemerkung != null) {
								cBemerkung = "OFFEN;" + cBemerkung;
							} else {
								cBemerkung = "OFFEN";
							}

						}

						azDtos[iAktuellerDatensatz]
								.setSZeitbuchungtext(cBemerkung);
						azDtos[iAktuellerDatensatz].setSKommentar(dtoBeginn
								.getXKommentar());
						azDtos[iAktuellerDatensatz].setTsBeginn(dtoBeginn
								.getTZeit());
						azDtos[iAktuellerDatensatz].setTsEnde(dtoEnde
								.getTZeit());

						if (dtoEnde.getTaetigkeitIId() != null) {
							azDtos[iAktuellerDatensatz]
									.setSBewegungsart(taetigkeitFindByPrimaryKey(
											dtoEnde.getTaetigkeitIId(),
											theClientDto).getCNr());
						}

						// CK: entfernt, siehe Projekt 7120
						/*
						 * else { try {
						 * azDtos[iAktuellerDatensatz].setSBewegungsart
						 * (getArtikelFac(). artikelFindByPrimaryKeySmall(
						 * dtoEnde.getArtikelIId(), idUser).getCNr()); } catch
						 * (RemoteException ex2) {
						 * throwEJBExceptionLPRespectOld(ex2); }
						 *
						 * }
						 */

						iAktuellerDatensatz++;
					}
				}
			}

		}

		if (bTheoretischeIstZeit == true) {

			// Zuerst verdichten
			TreeMap<Integer, AuftragzeitenDto> tm = new TreeMap<Integer, AuftragzeitenDto>();

			Integer letzteZeitdatenIId = null;
			BigDecimal bdDauer = new BigDecimal(0);
			for (int i = 0; i < azDtos.length; i++) {

				if (tm.containsKey(azDtos[i].getZeitdatenIIdBelegbuchung())) {
					AuftragzeitenDto azTmp = tm.get(azDtos[i]
							.getZeitdatenIIdBelegbuchung());

					azTmp.setDdDauer(azTmp.getDdDauer().doubleValue()
							+ azDtos[i].getDdDauer());
					azTmp.setTsEnde(azDtos[i].getTsEnde());
					azTmp.setSBewegungsart(azDtos[i].getSBewegungsart());
					azTmp.setSArtikelcnr(azDtos[i].getSArtikelcnr());
					azTmp.setSArtikelbezeichnung(azDtos[i]
							.getSArtikelbezeichnung());
					azTmp.setBdKosten(azTmp.getBdKosten().add(
							azDtos[i].getBdKosten()));

				} else {
					tm.put(azDtos[i].getZeitdatenIIdBelegbuchung(), azDtos[i]);
				}

			}

			azDtos = new AuftragzeitenDto[tm.size()];
			Iterator it = tm.keySet().iterator();
			int i = 0;
			while (it.hasNext()) {
				AuftragzeitenDto azDto = tm.get(it.next());
				azDtos[i] = azDto;
				i++;
			}

			for (int k = 0; k < azDtos.length; k++) {

				// Wenn Ruestzeit, dann Umspannzeiten abziehen
				// Im ende der Ruestzeit sind immer die Gut-Schlecht-Stueck der
				// anderen Lose
				// waehrend des Ruestens enthalten
				AuftragzeitenDto azDto = azDtos[k];
				Timestamp tBis = Helper.cutTimestamp(new Timestamp(azDto
						.getTsEnde().getTime() + 10));
				Query query = em
						.createNamedQuery("ZeitdatenfindZeitdatenEinesTagesUndEinerPerson");
				query.setParameter(1, azDto.getIPersonalMaschinenId());
				query.setParameter(2, azDto.getTsBeginn());
				query.setParameter(3, tBis);
				ZeitdatenDto[] tagesDaten = assembleZeitdatenDtos(query
						.getResultList());

				for (int j = 1; j < tagesDaten.length; j++) {
					LosgutschlechtDto[] lgsDtos = getFertigungFac()
							.losgutschlechtFindByZeitdatenIId(
									tagesDaten[j].getIId());

					if (lgsDtos.length > 0) {

						BigDecimal bdAbzuziehen = new BigDecimal(0);

						BigDecimal bdGesamt = new BigDecimal(0);
						for (int z = 0; z < lgsDtos.length; z++) {
							LosgutschlechtDto lgsDto = lgsDtos[z];

							if (lgsDto.getNGut() != null) {
								bdGesamt = bdGesamt.add(lgsDto.getNGut());
							}
							if (lgsDto.getNSchlecht() != null) {
								bdGesamt = bdGesamt.add(lgsDto.getNSchlecht());
							}

							LossollarbeitsplanDto sollarbeitsplanAbzuziehenDto = getFertigungFac()
									.lossollarbeitsplanFindByPrimaryKeyOhneExc(
											lgsDto.getLossollarbeitsplanIId());

							if (sollarbeitsplanAbzuziehenDto.getAgartCNr() != null) {
								bdAbzuziehen = bdAbzuziehen.add(bdGesamt
										.multiply(sollarbeitsplanAbzuziehenDto
												.getStueckzeit()));
							}

						}

						if (new BigDecimal(azDto.getDdDauer()).subtract(
								bdAbzuziehen).doubleValue() < 0) {
							azDto.setDdDauer((double) 0);
						} else {
							azDto.setDdDauer(new BigDecimal(azDto.getDdDauer())
									.subtract(bdAbzuziehen).doubleValue());
						}

						break;
					}
				}

			}
		}
		// PJ 15810 Wenn Los, dann Theoretische Ist-Zeit berechnen
		if (belegartCNr.equals(LocaleFac.BELEGART_LOS)) {
			if (bTheoretischeIstZeit == true) {
				azDtos = theoretischeIstzeitHinzufuegen(azDtos, belegartIId,
						belegartpositionIId, personalIId, tZeitenVon,
						tZeitenBis, false, theClientDto);
			}
		}

		try {
			if (bOrderByArtikelCNr == true) {

				for (int i = azDtos.length - 1; i > 0; --i) {
					for (int j = 0; j < i; ++j) {
						if (azDtos[j].getSArtikelcnr().compareTo(
								azDtos[j + 1].getSArtikelcnr()) > 0) {
							AuftragzeitenDto tauschDto = azDtos[j];
							azDtos[j] = azDtos[j + 1];
							azDtos[j + 1] = tauschDto;
						}
					}
				}
			}
			if (bOrberByPersonal == true) {
				for (int i = azDtos.length - 1; i > 0; --i) {
					for (int j = 0; j < i; ++j) {
						if (azDtos[j].getsPersonNachnameVorname().compareTo(
								azDtos[j + 1].getsPersonNachnameVorname()) > 0) {
							AuftragzeitenDto tauschDto = azDtos[j];
							azDtos[j] = azDtos[j + 1];
							azDtos[j + 1] = tauschDto;
						}
					}
				}
			}
		} catch (NullPointerException ex3) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_IN_ZEITDATEN, ex3);
		}

		return azDtos;
	}

	public BigDecimal getPersonalKostenProStunde(TheClientDto theClientDto,
			HashMap hmGestpreise, int iOption, Integer artikelIId,
			Integer personalIId, Timestamp tZeitpunkt) {

		BigDecimal bdKostenProStunde = BigDecimal.ZERO;
		try {
			if (iOption == 2) {

				// Stundensatz aus Personalgehalt holen
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(tZeitpunkt.getTime());

				PersonalgehaltDto pgDto = getPersonalFac()
						.personalgehaltFindLetztePersonalgehalt(personalIId,
								c.get(Calendar.YEAR), c.get(Calendar.MONTH));
				if (pgDto != null && pgDto.getNStundensatz() != null) {
					bdKostenProStunde = pgDto.getNStundensatz();
				}
			} else if (iOption == 1) {
				// Kosten aus Personalgruppe

				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKeySmall(personalIId);

				if (personalDto.getPersonalgruppeIId() != null) {

					BigDecimal bdKostenAusPersonalgruppe = getPersonalFac()
							.getPersonalgruppeKostenZumZeitpunkt(
									personalDto.getPersonalgruppeIId(),
									tZeitpunkt);

					if (bdKostenAusPersonalgruppe != null) {
						bdKostenProStunde = bdKostenAusPersonalgruppe;
					}
				}

			} else {

				// Tel. mit UW (05-15-05): Preise werden in
				// Mandantenwaehrung zurueckgeliefert.

				if (hmGestpreise != null) {
					if (!hmGestpreise.containsKey(artikelIId)) {
						BigDecimal preis = getLagerFac()
								.getGemittelterGestehungspreisAllerLaegerEinesMandanten(
										artikelIId, theClientDto);
						hmGestpreise.put(artikelIId, preis);

					}
					BigDecimal preis = (BigDecimal) hmGestpreise
							.get(artikelIId);
					bdKostenProStunde = preis;
				} else {

					BigDecimal preis = getLagerFac()
							.getGemittelterGestehungspreisAllerLaegerEinesMandanten(
									artikelIId, theClientDto);
					bdKostenProStunde = preis;

				}

			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return bdKostenProStunde;

	}

	private AuftragzeitenDto[] theoretischeIstzeitHinzufuegen(
			AuftragzeitenDto[] azDtos, Integer losIId,
			Integer lossollarbeitsplanIId, Integer personalId,
			java.sql.Timestamp tZeitenVon, java.sql.Timestamp tZeitenBis,
			boolean bMaschinenZeiten, TheClientDto theClientDto) {

		ArrayList<AuftragzeitenDto> alDaten = new ArrayList<AuftragzeitenDto>();

		for (int i = 0; i < azDtos.length; i++) {
			alDaten.add(azDtos[i]);
		}
		try {
			LossollarbeitsplanDto[] sollDtos = getFertigungFac()
					.lossollarbeitsplanFindByLosIId(losIId);

			for (int i = 0; i < sollDtos.length; i++) {
				LossollarbeitsplanDto sollarbeitsplanDto = sollDtos[i];
				if (sollarbeitsplanDto != null) {

					if (sollarbeitsplanDto.getAgartCNr() != null) {
						// Wenn Umspannzeit oder Laufzeit

						LosgutschlechtDto[] lgsDtos = getFertigungFac()
								.losgutschlechtFindByLossollarbeitsplanIId(
										sollarbeitsplanDto.getIId());

						if (lgsDtos.length > 0) {

							if (lossollarbeitsplanIId != null
									&& !sollarbeitsplanDto.getIId().equals(
											lossollarbeitsplanIId)) {
								continue;
							}

							if (bMaschinenZeiten == true) {
								if (sollarbeitsplanDto.getMaschineIId() != null) {
									HashMap<Integer, BigDecimal> hmMaschinen = new HashMap<Integer, BigDecimal>();

									for (int z = 0; z < lgsDtos.length; z++) {
										LosgutschlechtDto lgsDto = lgsDtos[z];

										if (lgsDto.getMaschinenzeitdatenIId() != null) {
											MaschinenzeitdatenDto zDto = maschinenzeitdatenFindByPrimaryKey(lgsDto
													.getMaschinenzeitdatenIId());
											if (zDto.getMaschineIId().equals(
													sollarbeitsplanDto
															.getMaschineIId())) {
												BigDecimal bdGesamtMaschine = new BigDecimal(
														0);
												if (lgsDto.getNGut() != null) {
													bdGesamtMaschine = bdGesamtMaschine
															.add(lgsDto
																	.getNGut());
												}
												if (lgsDto.getNSchlecht() != null) {
													bdGesamtMaschine = bdGesamtMaschine
															.add(lgsDto
																	.getNSchlecht());
												}

												if (bdGesamtMaschine
														.doubleValue() > 0) {
													AuftragzeitenDto azDto = new AuftragzeitenDto();

													ArtikelDto artikelDto = getArtikelFac()
															.artikelFindByPrimaryKeySmall(
																	sollarbeitsplanDto
																			.getArtikelIIdTaetigkeit(),
																	theClientDto);
													azDto.setArtikelIId(artikelDto
															.getIId());
													azDto.setArtikelgruppeIId(artikelDto
															.getArtgruIId());
													azDto.setTsBeginn(zDto
															.getTVon());
													azDto.setTsEnde(zDto
															.getTBis());
													azDto.setArtikelklasseIId(artikelDto
															.getArtklaIId());
													azDto.setSArtikelcnr(artikelDto
															.getCNr());
													azDto.setBelegpositionIId(sollarbeitsplanDto
															.getIId());
													String bezeichnung = "";
													if (artikelDto
															.getArtikelsprDto() != null) {
														bezeichnung = artikelDto
																.getArtikelsprDto()
																.getCBez();
														azDto.setSArtikelzusatzbezeichnung(artikelDto
																.getArtikelsprDto()
																.getCZbez());
													}
													azDto.setSArtikelbezeichnung("T:"
															+ bezeichnung);
													azDto.setIPersonalMaschinenId(sollarbeitsplanDto
															.getMaschineIId());

													String maschinenname = "M:";
													MaschineDto maschineDto = getZeiterfassungFac()
															.maschineFindByPrimaryKey(
																	sollarbeitsplanDto
																			.getMaschineIId());
													if (maschineDto
															.getCIdentifikationsnr() != null) {
														maschinenname += maschineDto
																.getCIdentifikationsnr()
																+ " ";
														azDto.setSPersonalnummer("M"
																+ maschineDto
																		.getCIdentifikationsnr());
													}
													maschinenname += maschineDto
															.getCBez();
													azDto.setSPersonalMaschinenname(maschinenname);
													azDto.setsPersonNachnameVorname(maschinenname);

													BigDecimal dauerNeu = bdGesamtMaschine
															.multiply(sollarbeitsplanDto
																	.getStueckzeit());

													// PJ 16119
													if (sollarbeitsplanDto
															.getIAufspannung() != null
															&& sollarbeitsplanDto
																	.getIAufspannung() >= 1) {
														dauerNeu = dauerNeu
																.divide(new BigDecimal(
																		sollarbeitsplanDto
																				.getIAufspannung()),
																		4,
																		BigDecimal.ROUND_HALF_EVEN);
													}

													azDto.setDdDauer(new Double(
															dauerNeu.doubleValue()));

													BigDecimal stundensatz = getMaschinenKostenZumZeitpunkt(
															sollarbeitsplanDto
																	.getMaschineIId(),
															azDto.getTsBeginn());

													azDto.setBdKosten(stundensatz
															.multiply(dauerNeu));
													alDaten.add(azDto);
												}
											}
										}

									}

								}
							} else {
								if (!Helper.short2boolean(sollarbeitsplanDto
										.getBNurmaschinenzeit())) {
									HashMap<Integer, BigDecimal> hmPersonen = new HashMap<Integer, BigDecimal>();

									for (int z = 0; z < lgsDtos.length; z++) {
										LosgutschlechtDto lgsDto = lgsDtos[z];

										if (lgsDto.getZeitdatenIId() != null) {
											ZeitdatenDto zDto = zeitdatenFindByPrimaryKey(
													lgsDto.getZeitdatenIId(),
													theClientDto);

											if (tZeitenVon != null
													&& tZeitenVon.after(zDto
															.getTZeit())) {
												continue;
											}
											if (tZeitenBis != null
													&& tZeitenBis.before(zDto
															.getTZeit())) {
												continue;
											}

											if (personalId == null
													|| personalId.equals(zDto
															.getPersonalIId())) {

												BigDecimal bdGesamtPerson = new BigDecimal(
														0);
												if (lgsDto.getNGut() != null) {
													bdGesamtPerson = bdGesamtPerson
															.add(lgsDto
																	.getNGut());
												}
												if (lgsDto.getNSchlecht() != null) {
													bdGesamtPerson = bdGesamtPerson
															.add(lgsDto
																	.getNSchlecht());
												}

												PersonalDto personalDto = getPersonalFac()
														.personalFindByPrimaryKey(
																zDto.getPersonalIId(),
																theClientDto);
												AuftragzeitenDto azDto = new AuftragzeitenDto();

												ArtikelDto artikelDto = getArtikelFac()
														.artikelFindByPrimaryKeySmall(
																sollarbeitsplanDto
																		.getArtikelIIdTaetigkeit(),
																theClientDto);
												azDto.setZeitdatenIIdBelegbuchung(zDto
														.getIId());
												azDto.setArtikelIId(artikelDto
														.getIId());
												azDto.setArtikelgruppeIId(artikelDto
														.getArtgruIId());
												azDto.setTsBeginn(zDto
														.getTZeit());
												azDto.setArtikelklasseIId(artikelDto
														.getArtklaIId());
												azDto.setSArtikelcnr(artikelDto
														.getCNr());
												azDto.setBelegpositionIId(sollarbeitsplanDto
														.getIId());
												String bezeichnung = "";
												if (artikelDto
														.getArtikelsprDto() != null) {
													bezeichnung = artikelDto
															.getArtikelsprDto()
															.getCBez();
												}
												azDto.setSArtikelbezeichnung("T:"
														+ bezeichnung);
												azDto.setSPersonalMaschinenname(personalDto
														.formatFixUFTitelName2Name1());
												azDto.setsPersonNachnameVorname(personalDto
														.formatFixName1Name2());

												azDto.setSPersonalKurzzeichen(personalDto
														.getCKurzzeichen());

												azDto.setSPersonalnummer(personalDto
														.getCPersonalnr() + "");

												azDto.setIPersonalMaschinenId(zDto
														.getPersonalIId());

												BigDecimal dauerNeu = bdGesamtPerson
														.multiply(sollarbeitsplanDto
																.getStueckzeit());
												// PJ 16119
												if (sollarbeitsplanDto
														.getIAufspannung() != null
														&& sollarbeitsplanDto
																.getIAufspannung() >= 1) {
													dauerNeu = dauerNeu
															.divide(new BigDecimal(
																	sollarbeitsplanDto
																			.getIAufspannung()),
																	4,
																	BigDecimal.ROUND_HALF_EVEN);
												}
												azDto.setDdDauer(new Double(
														dauerNeu.doubleValue()));

												int iOption = 0;
												ParametermandantDto parameter = null;
												try {
													parameter = (ParametermandantDto) getParameterFac()
															.getMandantparameter(
																	theClientDto
																			.getMandant(),
																	ParameterFac.KATEGORIE_PERSONAL,
																	ParameterFac.PARAMETER_PERSONALKOSTEN_QUELLE);

													iOption = ((Integer) parameter
															.getCWertAsObject())
															.intValue();

												} catch (RemoteException ex5) {
													throw new EJBExceptionLP(
															EJBExceptionLP.FEHLER,
															ex5);
												}

												if (iOption == 2) {
													BigDecimal bdPreis = new BigDecimal(
															0);
													// Stundensatz aus
													// Personalgehalt
													// holen
													Calendar c = Calendar
															.getInstance();
													c.setTimeInMillis(azDto
															.getTsBeginn()
															.getTime());

													PersonalgehaltDto pgDto = getPersonalFac()
															.personalgehaltFindLetztePersonalgehalt(
																	azDto.getIPersonalMaschinenId(),
																	c.get(Calendar.YEAR),
																	c.get(Calendar.MONTH));
													if (pgDto != null
															&& pgDto.getNStundensatz() != null) {
														bdPreis = pgDto
																.getNStundensatz()
																.multiply(
																		dauerNeu);

													}

													azDto.setBdKosten(bdPreis);

												} else if (iOption == 1) {
													// Kosten aus Personalgruppe
													BigDecimal bdPreis = new BigDecimal(
															0);

													if (personalDto
															.getPersonalgruppeIId() != null) {

														BigDecimal bdKostenAusPersonalgruppe = getPersonalFac()
																.getPersonalgruppeKostenZumZeitpunkt(
																		personalDto
																				.getPersonalgruppeIId(),
																		azDto.getTsBeginn());

														if (bdKostenAusPersonalgruppe != null) {
															bdPreis = bdKostenAusPersonalgruppe
																	.multiply(dauerNeu);
														}
													}

													azDto.setBdKosten(bdPreis);

												} else {

													// Tel. mit UW (05-15-05):
													// Preise
													// werden
													// in
													// Mandantenwaehrung
													// zurueckgeliefert.

													BigDecimal preis = getLagerFac()
															.getGemittelterGestehungspreisAllerLaegerEinesMandanten(
																	azDto.getArtikelIId(),
																	theClientDto);
													azDto.setBdKosten(preis
															.multiply(dauerNeu));

												}
												alDaten.add(azDto);
											}
										}

									}

								}
							}

						}
					}
				}
			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		azDtos = new AuftragzeitenDto[alDaten.size()];
		azDtos = alDaten.toArray(azDtos);

		return azDtos;
	}

	/**
	 * Hole alle Artikelarten ohne Montag bis Sonntag nach Spr.
	 *
	 * @param cNrSpracheI
	 *            String
	 * @throws EJBExceptionLP
	 * @return Map
	 */
	public Map getAllSprTagesartenOhneMontagBisSonntag(String cNrSpracheI)
			throws EJBExceptionLP {
		Map tmArten = new HashMap<Integer, Object>();
		// try {
		Query query = em.createNamedQuery("TagesartfindAll");
		Collection<?> clArten = query.getResultList();
		// if (clArten.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, null);
		// }
		Iterator<?> itArten = clArten.iterator();
		while (itArten.hasNext()) {
			Tagesart tagesartTemp = (Tagesart) itArten.next();
			Integer key = tagesartTemp.getIId();

			if (tagesartTemp.getCNr().equals(ZeiterfassungFac.TAGESART_MONTAG)) {
				key = null;
			} else if (tagesartTemp.getCNr().equals(
					ZeiterfassungFac.TAGESART_DIENSTAG)) {
				key = null;
			} else if (tagesartTemp.getCNr().equals(
					ZeiterfassungFac.TAGESART_MITTWOCH)) {
				key = null;
			} else if (tagesartTemp.getCNr().equals(
					ZeiterfassungFac.TAGESART_DONNERSTAG)) {
				key = null;
			} else if (tagesartTemp.getCNr().equals(
					ZeiterfassungFac.TAGESART_FREITAG)) {
				key = null;
			} else if (tagesartTemp.getCNr().equals(
					ZeiterfassungFac.TAGESART_SAMSTAG)) {
				key = null;
			} else if (tagesartTemp.getCNr().equals(
					ZeiterfassungFac.TAGESART_SONNTAG)) {
				key = null;
			}

			Object value = null;
			// try {
			Tagesartspr tagesartspr = em.find(Tagesartspr.class,
					new TagesartsprPK(cNrSpracheI, tagesartTemp.getIId()));
			if (tagesartspr == null) {
				// fuer locale und C_NR keine Bezeichnu g vorhanden ...
				value = tagesartTemp.getCNr();
			} else {
				value = tagesartspr.getCBez();
			}

			// }
			// catch (NoResultException ex1) {
			// fuer locale und C_NR keine Bezeichnu g vorhanden ...
			// value = tagesartTemp.getCNr();
			// }
			if (key != null) {
				tmArten.put(key, value);
			}
		}
		// }
		// catch (NoResultException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		// }

		return tmArten;
	}

	public Integer createTagesart(TagesartDto tagesartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (tagesartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("tagesartDto == null"));
		}
		if (tagesartDto.getCNr() == null || tagesartDto.getISort() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"tagesartDto.getCNr() == null || tagesartDto.getISort() == null"));
		}
		try {
			Query query = em.createNamedQuery("TagesartfindByCNr");
			query.setParameter(1, tagesartDto.getCNr());
			// @todo getSingleResult oder getResultList ?
			Tagesart doppelt = (Tagesart) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_TAGESART.CNR"));
		} catch (NoResultException ex) {
			//
		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_TAGESART);
			tagesartDto.setIId(pk);
			Tagesart tagesart = new Tagesart(tagesartDto.getIId(),
					tagesartDto.getCNr(), tagesartDto.getISort());
			em.persist(tagesart);
			em.flush();
			setTagesartFromTagesartDto(tagesart, tagesartDto);
			if (tagesartDto.getTagesartsprDto() != null) {
				Tagesartspr tagesartspr = new Tagesartspr(
						theClientDto.getLocMandantAsString(),
						tagesartDto.getIId());
				em.persist(tagesartspr);
				em.flush();
				setTagesartsprFromTagesartsprDto(tagesartspr,
						tagesartDto.getTagesartsprDto());
			}
			return tagesartDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Double getSummeSollzeitWochentagsUndSonntags(Integer zeitmodellIId) {
		// wg AD + OpenGolf eingebaut, da Hr. Neureiter nur Samstag und Sonntag
		// arbeitet
		Double d = getSummeSollzeitWochentags(zeitmodellIId);
		try {
			// SONNTAG
			Query query = em
					.createNamedQuery("ZeitmodelltagfindSollzeitZuTagesart");
			query.setParameter(1, zeitmodellIId);
			query.setParameter(2,
					((Tagesart) em.createNamedQuery("TagesartfindByCNr")
							.setParameter(1, ZeiterfassungFac.TAGESART_SONNTAG)
							.getSingleResult()).getIId());
			Zeitmodelltag zeitmodelltag = (Zeitmodelltag) query
					.getSingleResult();
			long l = zeitmodelltag.getUSollzeit().getTime();
			long lGesamt = l + 3600000;
			double d_zeitdec = lGesamt / 3600000;
			d = new Double(d.doubleValue() + d_zeitdec);
		} catch (NoResultException ex) {
			// nothig here
		}

		return d;
	}

	public Double getSummeSollzeitWochentags(Integer zeitmodellIId)
			throws EJBExceptionLP {
		Zeitmodelltag zeitmodelltag;
		long lGesamt = 0;
		Query query = em
				.createNamedQuery("ZeitmodelltagfindSollzeitZuTagesart");
		try {
			// MONTAG
			query.setParameter(1, zeitmodellIId);
			query.setParameter(2,
					((Tagesart) em.createNamedQuery("TagesartfindByCNr")
							.setParameter(1, ZeiterfassungFac.TAGESART_MONTAG)
							.getSingleResult()).getIId());
			zeitmodelltag = (Zeitmodelltag) query.getSingleResult();
			long l = zeitmodelltag.getUSollzeit().getTime();
			lGesamt += l + 3600000;
		} catch (NoResultException ex) {
			// nothig here
		}
		try {
			// DIENSTAG
			query.setParameter(
					2,
					((Tagesart) em
							.createNamedQuery("TagesartfindByCNr")
							.setParameter(1, ZeiterfassungFac.TAGESART_DIENSTAG)
							.getSingleResult()).getIId());
			zeitmodelltag = (Zeitmodelltag) query.getSingleResult();
			long l = zeitmodelltag.getUSollzeit().getTime();
			lGesamt += l + 3600000;
		} catch (NoResultException ex) {
			// nothig here
		}
		try {
			// MITTWOCH
			query.setParameter(
					2,
					((Tagesart) em
							.createNamedQuery("TagesartfindByCNr")
							.setParameter(1, ZeiterfassungFac.TAGESART_MITTWOCH)
							.getSingleResult()).getIId());
			zeitmodelltag = (Zeitmodelltag) query.getSingleResult();
			long l = zeitmodelltag.getUSollzeit().getTime();
			lGesamt += l + 3600000;
		} catch (NoResultException ex) {
			// nothig here
		}
		try {
			// DONNERSTAG
			query.setParameter(
					2,
					((Tagesart) em
							.createNamedQuery("TagesartfindByCNr")
							.setParameter(1,
									ZeiterfassungFac.TAGESART_DONNERSTAG)
							.getSingleResult()).getIId());
			zeitmodelltag = (Zeitmodelltag) query.getSingleResult();
			long l = zeitmodelltag.getUSollzeit().getTime();
			lGesamt += l + 3600000;
		} catch (NoResultException ex) {
			// nothig here
		}
		try {
			// FREITAG
			query.setParameter(2,
					((Tagesart) em.createNamedQuery("TagesartfindByCNr")
							.setParameter(1, ZeiterfassungFac.TAGESART_FREITAG)
							.getSingleResult()).getIId());
			zeitmodelltag = (Zeitmodelltag) query.getSingleResult();
			long l = zeitmodelltag.getUSollzeit().getTime();
			lGesamt += l + 3600000;
		} catch (NoResultException ex) {
			// nothig here
		}
		try {
			// SAMSTAG
			query.setParameter(2,
					((Tagesart) em.createNamedQuery("TagesartfindByCNr")
							.setParameter(1, ZeiterfassungFac.TAGESART_SAMSTAG)
							.getSingleResult()).getIId());
			zeitmodelltag = (Zeitmodelltag) query.getSingleResult();
			long l = zeitmodelltag.getUSollzeit().getTime();
			lGesamt += l + 3600000;
		} catch (NoResultException ex) {
			// nothig here
		}

		double d_zeitdec = lGesamt / 36000;
		return new Double(d_zeitdec / 100);

	}

	public Double getSummeSollzeitMontagBisSonntag(Integer zeitmodellIId) {
		Zeitmodelltag zeitmodelltag;
		long lGesamt = 0;
		Query query = em
				.createNamedQuery("ZeitmodelltagfindSollzeitZuTagesart");
		try {
			// MONTAG
			query.setParameter(1, zeitmodellIId);
			query.setParameter(2,
					((Tagesart) em.createNamedQuery("TagesartfindByCNr")
							.setParameter(1, ZeiterfassungFac.TAGESART_MONTAG)
							.getSingleResult()).getIId());
			zeitmodelltag = (Zeitmodelltag) query.getSingleResult();
			long l = zeitmodelltag.getUSollzeit().getTime();
			lGesamt += l + 3600000;
		} catch (NoResultException ex) {
			// nothig here
		}
		try {
			// DIENSTAG
			query.setParameter(
					2,
					((Tagesart) em
							.createNamedQuery("TagesartfindByCNr")
							.setParameter(1, ZeiterfassungFac.TAGESART_DIENSTAG)
							.getSingleResult()).getIId());
			zeitmodelltag = (Zeitmodelltag) query.getSingleResult();
			long l = zeitmodelltag.getUSollzeit().getTime();
			lGesamt += l + 3600000;
		} catch (NoResultException ex) {
			// nothig here
		}
		try {
			// MITTWOCH
			query.setParameter(
					2,
					((Tagesart) em
							.createNamedQuery("TagesartfindByCNr")
							.setParameter(1, ZeiterfassungFac.TAGESART_MITTWOCH)
							.getSingleResult()).getIId());
			zeitmodelltag = (Zeitmodelltag) query.getSingleResult();
			long l = zeitmodelltag.getUSollzeit().getTime();
			lGesamt += l + 3600000;
		} catch (NoResultException ex) {
			// nothig here
		}
		try {
			// DONNERSTAG
			query.setParameter(
					2,
					((Tagesart) em
							.createNamedQuery("TagesartfindByCNr")
							.setParameter(1,
									ZeiterfassungFac.TAGESART_DONNERSTAG)
							.getSingleResult()).getIId());
			zeitmodelltag = (Zeitmodelltag) query.getSingleResult();
			long l = zeitmodelltag.getUSollzeit().getTime();
			lGesamt += l + 3600000;
		} catch (NoResultException ex) {
			// nothig here
		}
		try {
			// FREITAG
			query.setParameter(2,
					((Tagesart) em.createNamedQuery("TagesartfindByCNr")
							.setParameter(1, ZeiterfassungFac.TAGESART_FREITAG)
							.getSingleResult()).getIId());
			zeitmodelltag = (Zeitmodelltag) query.getSingleResult();
			long l = zeitmodelltag.getUSollzeit().getTime();
			lGesamt += l + 3600000;
		} catch (NoResultException ex) {
			// nothig here
		}
		try {
			// SAMSTAG
			query.setParameter(2,
					((Tagesart) em.createNamedQuery("TagesartfindByCNr")
							.setParameter(1, ZeiterfassungFac.TAGESART_SAMSTAG)
							.getSingleResult()).getIId());
			zeitmodelltag = (Zeitmodelltag) query.getSingleResult();
			long l = zeitmodelltag.getUSollzeit().getTime();
			lGesamt += l + 3600000;
		} catch (NoResultException ex) {
			// nothig here
		}
		try {
			// SAMSTAG
			query.setParameter(2,
					((Tagesart) em.createNamedQuery("TagesartfindByCNr")
							.setParameter(1, ZeiterfassungFac.TAGESART_SONNTAG)
							.getSingleResult()).getIId());
			zeitmodelltag = (Zeitmodelltag) query.getSingleResult();
			long l = zeitmodelltag.getUSollzeit().getTime();
			lGesamt += l + 3600000;
		} catch (NoResultException ex) {
			// nothig here
		}
		double d_zeitdec = lGesamt / 36000;
		return new Double(d_zeitdec / 100);

	}

	/**
	 * Ermittlt den Schnitt der Sollzeit eines Tages eines Zeitmodells
	 *
	 * @param zeitmodellIId
	 *            ID des Zeitmodells
	 * @return String Ergebnis als String
	 * @exception RemoteException
	 * @exception EJBExceptionLP
	 */
	public Double getWochenschnittEinesZeitmodellsProTag(Integer zeitmodellIId)
			throws EJBExceptionLP {
		double dSumme = 0;
		ZeitmodelltagDto[] dtos = null;
		// try {
		Query query = em.createNamedQuery("ZeitmodelltagfindByZeitmodellIId");
		query.setParameter(1, zeitmodellIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// null);
		// }
		dtos = assembleZeitmodelltagDtos(cl);

		for (int i = 0; i < dtos.length; i++) {
			ZeitmodelltagDto dto = dtos[i];
			Query query2 = em
					.createNamedQuery("ZeitmodelltagfindSollzeitZuTagesart");
			query2.setParameter(1, zeitmodellIId);
			query2.setParameter(2, dto.getTagesartIId());
			Zeitmodelltag zeitmodelltag = (Zeitmodelltag) query2
					.getSingleResult();
			if (zeitmodelltag == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
			}
			long l = zeitmodelltag.getUSollzeit().getTime();

			double d_zeitdec = (l + 3600000) / 36000;
			dSumme = dSumme + (d_zeitdec / 100);

		}
		// }
		// catch (NoResultException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// ex);
		// }
		if (dtos.length > 0) {
			dSumme = dSumme / dtos.length;
		} else {
			dSumme = 0;
		}
		return new Double(dSumme);

	}

	/**
	 * Ermittlt Summe der Sollzeit einer fuer Feiertag, Sonntag und Halbtag
	 *
	 * @param zeitmodellIId
	 *            ID des Zeitmodells
	 * @return String Ergebnis als String
	 * @exception RemoteException
	 * @exception EJBExceptionLP
	 */
	public Double getSummeSollzeitSonnUndFeiertags(Integer zeitmodellIId)
			throws EJBExceptionLP {
		long lGesamt = 0;
		Query query = em
				.createNamedQuery("ZeitmodelltagfindSollzeitZuTagesart");
		try {
			// SONNTAG
			query.setParameter(1, zeitmodellIId);
			query.setParameter(2,
					((Tagesart) em.createNamedQuery("TagesartfindByCNr")
							.setParameter(1, ZeiterfassungFac.TAGESART_SONNTAG)
							.getSingleResult()).getIId());
			Zeitmodelltag zeitmodelltag = (Zeitmodelltag) query
					.getSingleResult();
			if (zeitmodelltag != null) {
				long l = zeitmodelltag.getUSollzeit().getTime();
				lGesamt += l + 3600000;
			}
		} catch (NoResultException ex) {
			// nothig here
		}
		try {
			// HALBTAG
			query.setParameter(2,
					((Tagesart) em.createNamedQuery("TagesartfindByCNr")
							.setParameter(1, ZeiterfassungFac.TAGESART_HALBTAG)
							.getSingleResult()).getIId());
			Zeitmodelltag zeitmodelltag = (Zeitmodelltag) query
					.getSingleResult();
			if (zeitmodelltag != null) {
				long l = zeitmodelltag.getUSollzeit().getTime();
				lGesamt += l + 3600000;
			}
		} catch (NoResultException ex) {
			// nothig here
		}
		try {
			// FEIERTAG
			query.setParameter(
					2,
					((Tagesart) em
							.createNamedQuery("TagesartfindByCNr")
							.setParameter(1, ZeiterfassungFac.TAGESART_FEIERTAG)
							.getSingleResult()).getIId());
			Zeitmodelltag zeitmodelltag = (Zeitmodelltag) query
					.getSingleResult();
			if (zeitmodelltag != null) {
				long l = zeitmodelltag.getUSollzeit().getTime();
				lGesamt += l + 3600000;
			}
		} catch (NoResultException ex) {
			// nothig here
		}

		double d_zeitdec = lGesamt / 36000;
		return new Double(d_zeitdec / 100);

	}

	public void removeTagesart(TagesartDto tagesartDto) throws EJBExceptionLP {
		myLogger.entry();
		if (tagesartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("tagesartDto == null"));
		}
		if (tagesartDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {

		try {
			Query query = em.createNamedQuery("TagesartsprfindByTagesartIId");
			query.setParameter(1, tagesartDto.getIId());
			Collection<?> allTagesartspr = query.getResultList();

			Iterator<?> iter = allTagesartspr.iterator();
			while (iter.hasNext()) {
				Tagesartspr artgrusprTemp = (Tagesartspr) iter.next();
				em.remove(artgrusprTemp);
			}
			// }
			// catch (RemoveException ex) {
			// throw new EJBExceptionLP(EJBExceptionLP.
			// FEHLER_BEIM_LOESCHEN, ex);
			// }
			Tagesart tagesart = em.find(Tagesart.class, tagesartDto.getIId());
			if (tagesart == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			if (tagesart.getCNr().equals(ZeiterfassungFac.TAGESART_MONTAG)
					|| tagesart.getCNr().equals(
							ZeiterfassungFac.TAGESART_DIENSTAG)
					|| tagesart.getCNr().equals(
							ZeiterfassungFac.TAGESART_MITTWOCH)
					|| tagesart.getCNr().equals(
							ZeiterfassungFac.TAGESART_DONNERSTAG)
					|| tagesart.getCNr().equals(
							ZeiterfassungFac.TAGESART_FREITAG)
					|| tagesart.getCNr().equals(
							ZeiterfassungFac.TAGESART_SAMSTAG)
					|| tagesart.getCNr().equals(
							ZeiterfassungFac.TAGESART_SONNTAG)
					|| tagesart.getCNr().equals(
							ZeiterfassungFac.TAGESART_HALBTAG)
					|| tagesart.getCNr().equals(
							ZeiterfassungFac.TAGESART_FEIERTAG)
					|| tagesart.getCNr().equals(
							ZeiterfassungFac.TAGESART_BETRIEBSURLAUB)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						new Exception("DARF_NICHT_GELOESCHT_WERDEN"));

			}

			em.remove(tagesart);
			em.flush();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void bringeFehlerWennZeitabschlussvorhanden(Integer personalIId,
			java.sql.Timestamp tZeitWelcheGeaendertWerdenSoll,
			TheClientDto theClientDto) {
		if (getMandantFac().hatZusatzfunktionberechtigung(
				MandantFac.ZUSATZFUNKTION_ZEITEN_ABSCHLIESSEN, theClientDto)) {
			java.sql.Timestamp t = gibtEsBereitseinenZeitabschlussBisZurKW(
					personalIId, tZeitWelcheGeaendertWerdenSoll, theClientDto);

			if (t != null) {
				ArrayList al = new ArrayList();
				al.add(t);

				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_ZEITEN_BEREITS_ABGESCHLOSSEN, al,
						new Exception("FEHLER_ZEITEN_BEREITS_ABGESCHLOSSEN"));

			}
		}

	}

	public java.sql.Timestamp gibtEsBereitseinenZeitabschlussBisZurKW(
			Integer personalIId, java.sql.Timestamp tKW,
			TheClientDto theClientDto) {

		Timestamp[] tVonBisEinerKW = Helper.getTimestampVonBisEinerKW(tKW);

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tKW.getTime());

		int kwAktuell = c.get(Calendar.WEEK_OF_YEAR);

		String sQueryBelegzeiten = "SELECT za  from FLRZeitabschluss za WHERE za.personal_i_id="
				+ personalIId
				+ " AND za.t_abgeschlossen_bis>='"
				+ Helper.formatTimestampWithSlashes(Helper
						.cutTimestamp(new Timestamp(tVonBisEinerKW[0].getTime())))
				+ "' ORDER BY za.t_abgeschlossen_bis DESC";

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Query query = session.createQuery(sQueryBelegzeiten);
		query.setMaxResults(1);

		List<?> resultList = query.list();

		Iterator<?> resultListIterator = resultList.iterator();

		ArrayList alDataSub = new ArrayList();

		if (resultListIterator.hasNext()) {
			FLRZeitabschluss flr = (FLRZeitabschluss) resultListIterator.next();
			session.close();
			return new Timestamp(flr.getT_abgeschlossen_bis().getTime());
		} else {
			session.close();
			return null;
		}

	}

	public void zeitenAbschliessen(Integer personalIId, java.sql.Timestamp tKW,
			TheClientDto theClientDto) {
		java.sql.Timestamp t = gibtEsBereitseinenZeitabschlussBisZurKW(
				personalIId, tKW, theClientDto);
		if (t == null) {
			ZeitabschlussDto zaDto = new ZeitabschlussDto();
			Timestamp[] tKWVonBis = Helper.getTimestampVonBisEinerKW(tKW);
			zaDto.setPersonalIId(personalIId);
			zaDto.setTAbgeschlossenBis(Helper.cutTimestamp(tKWVonBis[1]));
			getPersonalFac().createZeitabschluss(zaDto, theClientDto);
		}
	}

	public void updateTagesart(TagesartDto tagesartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (tagesartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("tagesartDto == null"));
		}
		if (tagesartDto.getIId() == null || tagesartDto.getCNr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"tagesartDto.getIId() == null || tagesartDto.getCNr() == null"));
		}
		Integer iId = tagesartDto.getIId();
		Tagesart tagesart = null;
		// try {
		tagesart = em.find(Tagesart.class, iId);
		if (tagesart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}

		// }
		// catch (NoResultException ex1) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex1);

		// }

		try {
			Query query = em.createNamedQuery("TagesartfindByCNr");
			query.setParameter(1, tagesartDto.getCNr());
			Integer iIdVorhanden = ((Tagesart) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_TAGESART.C_NR"));
			}

		} catch (NoResultException ex) {
			// nothing here
		}

		if ((tagesart.getCNr().equals(ZeiterfassungFac.TAGESART_MONTAG)
				|| tagesart.getCNr().equals(ZeiterfassungFac.TAGESART_DIENSTAG)
				|| tagesart.getCNr().equals(ZeiterfassungFac.TAGESART_MITTWOCH)
				|| tagesart.getCNr().equals(
						ZeiterfassungFac.TAGESART_DONNERSTAG)
				|| tagesart.getCNr().equals(ZeiterfassungFac.TAGESART_FREITAG)
				|| tagesart.getCNr().equals(ZeiterfassungFac.TAGESART_SAMSTAG)
				|| tagesart.getCNr().equals(ZeiterfassungFac.TAGESART_SONNTAG)
				|| tagesart.getCNr().equals(ZeiterfassungFac.TAGESART_HALBTAG)
				|| tagesart.getCNr().equals(ZeiterfassungFac.TAGESART_FEIERTAG) || tagesart
				.getCNr().equals(ZeiterfassungFac.TAGESART_BETRIEBSURLAUB))
				&& (!tagesart.getCNr().equals(tagesartDto.getCNr()))) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE,
					new Exception("DARF_NICHT_UPGEDATED_WERDEN"));

		}

		setTagesartFromTagesartDto(tagesart, tagesartDto);
		try {
			if (tagesartDto.getTagesartsprDto() != null) {
				// try {
				Tagesartspr tagesartspr = em
						.find(Tagesartspr.class,
								new TagesartsprPK(theClientDto
										.getLocUiAsString(), iId));
				if (tagesartspr == null) {
					tagesartspr = new Tagesartspr(
							theClientDto.getLocUiAsString(), iId);
					em.persist(tagesartspr);
					em.flush();
					setTagesartsprFromTagesartsprDto(tagesartspr,
							tagesartDto.getTagesartsprDto());
				}
				setTagesartsprFromTagesartsprDto(tagesartspr,
						tagesartDto.getTagesartsprDto());
				// }
				// catch (NoResultException ex) {
				// Tagesartspr tagesartspr = new
				// Tagesartspr(getTheClient(idUser).getLocUiAsString(), iId);
				// em.persist(tagesartspr);
				// setTagesartsprFromTagesartsprDto(tagesartspr,
				// tagesartDto.getTagesartsprDto());
				// }
			}
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
	}

	public TagesartDto tagesartFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Tagesart tagesart = em.find(Tagesart.class, iId);
		if (tagesart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		TagesartDto tagesartDto = assembleTagesartDto(tagesart);
		TagesartsprDto tagesartsprDto = null;
		// try {
		Tagesartspr tagesartspr = em.find(Tagesartspr.class, new TagesartsprPK(
				theClientDto.getLocUiAsString(), iId));
		if (tagesartspr != null) {
			tagesartsprDto = assembleTagesartsprDto(tagesartspr);
		}
		// }
		// catch (NoResultException ex) {
		// nothing here
		// }
		if (tagesartsprDto == null) {
			// try {
			tagesartspr = em.find(Tagesartspr.class, new TagesartsprPK(
					theClientDto.getLocKonzernAsString(), iId));
			if (tagesartspr != null) {
				tagesartsprDto = assembleTagesartsprDto(tagesartspr);
			}
			// catch (NoResultException ex) {
			// nothing here
			// }
		}
		tagesartDto.setTagesartsprDto(tagesartsprDto);
		return tagesartDto;
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public TagesartDto tagesartFindByCNr(String cNr, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (cNr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("cNr == null"));
		}

		try {
			Query query = em.createNamedQuery("TagesartfindByCNr");
			query.setParameter(1, cNr);
			return tagesartFindByPrimaryKey(
					((Tagesart) query.getSingleResult()).getIId(), theClientDto);
		} catch (NoResultException e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	public TagesartDto[] tagesartFindAll() throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("TagesartfindAll");
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		return assembleTagesartDtos(cl);
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);

		// }
	}

	public HashMap taetigkeitenMitImportkennzeichen() {

		Query query = em
				.createNamedQuery("TaetigkeitfindByCImportkennzeichenNotNull");
		Collection<?> cl = query.getResultList();

		HashMap hm = new HashMap();
		Iterator it = cl.iterator();
		while (it.hasNext()) {
			Taetigkeit t = (Taetigkeit) it.next();
			hm.put(t.getCImportkennzeichen(), t.getIId());
		}

		return hm;
	}

	private void setTagesartFromTagesartDto(Tagesart tagesart,
			TagesartDto tagesartDto) {
		tagesart.setCNr(tagesartDto.getCNr());
		tagesart.setISort(tagesartDto.getISort());
		em.merge(tagesart);
		em.flush();
	}

	private TagesartDto assembleTagesartDto(Tagesart tagesart) {
		return TagesartDtoAssembler.createDto(tagesart);
	}

	private TagesartDto[] assembleTagesartDtos(Collection<?> tagesarts) {
		List<TagesartDto> list = new ArrayList<TagesartDto>();
		if (tagesarts != null) {
			Iterator<?> iterator = tagesarts.iterator();
			while (iterator.hasNext()) {
				Tagesart tagesart = (Tagesart) iterator.next();
				list.add(assembleTagesartDto(tagesart));
			}
		}
		TagesartDto[] returnArray = new TagesartDto[list.size()];
		return (TagesartDto[]) list.toArray(returnArray);
	}

	private void setTagesartsprFromTagesartsprDto(Tagesartspr tagesartspr,
			TagesartsprDto tagesartsprDto) {
		tagesartspr.setCBez(tagesartsprDto.getCBez());
		em.merge(tagesartspr);
		em.flush();
	}

	private TagesartsprDto assembleTagesartsprDto(Tagesartspr tagesartspr) {
		return TagesartsprDtoAssembler.createDto(tagesartspr);
	}

	private TagesartsprDto[] assembleTagesartsprDtos(Collection<?> tagesartsprs) {
		List<TagesartsprDto> list = new ArrayList<TagesartsprDto>();
		if (tagesartsprs != null) {
			Iterator<?> iterator = tagesartsprs.iterator();
			while (iterator.hasNext()) {
				Tagesartspr tagesartspr = (Tagesartspr) iterator.next();
				list.add(assembleTagesartsprDto(tagesartspr));
			}
		}
		TagesartsprDto[] returnArray = new TagesartsprDto[list.size()];
		return (TagesartsprDto[]) list.toArray(returnArray);
	}

	private Timestamp sucheEndeZeitFuerRelativeZeitbuchung(Timestamp tsBeginn,
			Integer personalIId, long lZeitRelativ, boolean bIgnoriereFehler,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Timestamp tsEnde = null;
		try {
			Query query1 = em
					.createNamedQuery("ZeitdatenfindZeitdatenEinesTagesUndEinerPerson");
			query1.setParameter(1, personalIId);
			query1.setParameter(2, new Timestamp(tsBeginn.getTime() + 11));
			query1.setParameter(3, Helper.cutTimestamp(new Timestamp(tsBeginn
					.getTime() + 24 * 3600000)));

			ZeitdatenDto[] dtos = assembleZeitdatenDtosOhneBelegzeiten(query1
					.getResultList());

			if (dtos.length == 0) {
				return new Timestamp(tsBeginn.getTime() + lZeitRelativ);
			}

			int iGeht = dtos.length;

			Query query = em.createNamedQuery("TaetigkeitfindByCNr");
			query.setParameter(1, ZeiterfassungFac.TAETIGKEIT_GEHT);
			Integer taetigkeitIId_Geht = ((Taetigkeit) query.getSingleResult())
					.getIId();

			ZeitdatenDto dtoGeht = null;

			for (int i = 0; i < dtos.length; i++) {
				if (taetigkeitIId_Geht.equals(dtos[i].getTaetigkeitIId())) {
					iGeht = i;
					dtoGeht = dtos[i];
					break;
				}
			}

			ZeitdatenDto[] dtosAufbereitet = new ZeitdatenDto[iGeht];

			for (int i = 0; i < iGeht; i++) {
				dtosAufbereitet[i] = dtos[i];
			}

			if (dtosAufbereitet.length % 2 == 1) {
				// Wenn ungerade, Exception schmeissen -> Fehler in
				// Zeitbuchungen, ausser wenn Zeitvertileung auf Los, dann lt.
				// WH Fehler ingorieren

				if (bIgnoriereFehler == true) {
					return new Timestamp(tsBeginn.getTime() + lZeitRelativ);
				} else {

					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_ZEITERFASSUNG_RELATIVE_NICHT_MOEGLICH,
							new Exception(
									"FEHLER_ZEITERFASSUNG_RELATIVE_NICHT_MOEGLICH"));
				}
			}

			tsEnde = new Timestamp(tsBeginn.getTime() + lZeitRelativ);

			for (int i = 0; i < dtosAufbereitet.length - 1; i = i + 2) {
				ZeitdatenDto aktuellerDatensatz = dtosAufbereitet[i];
				ZeitdatenDto naechsterDatensatz = dtosAufbereitet[i + 1];

				if (tsEnde.before(aktuellerDatensatz.getTZeit())) {
					return tsEnde;
				} else {
					long diff = naechsterDatensatz.getTZeit().getTime()
							- aktuellerDatensatz.getTZeit().getTime();
					tsEnde = new Timestamp(tsEnde.getTime() + diff);
				}
			}
			// 20 Millisekunden abziehen, damit es zu keiner Unique constaraint
			// verletzung kommt
			tsEnde = new Timestamp(tsEnde.getTime() + 20);

			if (dtoGeht != null) {
				if (tsEnde.after(dtoGeht.getTZeit())) {
					// Soviel Zeit ist nicht ueber
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_GEHT_VOR_ENDE, new Exception(
									"FEHLER_GEHT_VOR_ENDE"));
				}
			}

		} catch (NoResultException ex) {
			// nichts
		}

		return tsEnde;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void automatikbuchungenAufrollen(java.sql.Date tVon,
			java.sql.Date tBis, Integer personalIId, TheClientDto theClientDto,
			boolean bLoeschen) throws EJBExceptionLP {

		Calendar c = Calendar.getInstance();
		c.setTime(tBis);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);

		tVon = Helper.cutDate(tVon);

		java.util.Date dBis = c.getTime();

		if (bLoeschen == true) {

			getZeiterfassungFac().automatikbuchungenLoeschen(tVon, personalIId,
					theClientDto, dBis);
		}

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();
		org.hibernate.Criteria autoPausen = session
				.createCriteria(FLRZeitdaten.class);
		autoPausen.createAlias("flrpersonal", "p");
		autoPausen.add(Restrictions.eq("p.mandant_c_nr",
				theClientDto.getMandant()));
		if (personalIId != null) {
			autoPausen.add(Restrictions.eq(
					ZeiterfassungFac.FLR_ZEITDATEN_PERSONAL_I_ID, personalIId));
		}
		autoPausen.add(Restrictions.ge(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT,
				tVon));

		autoPausen.add(Restrictions.le(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT,
				dBis));
		// Hole id der Taetigkeit GEHT
		Integer taetigkeitIId_Geht = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_GEHT, theClientDto).getIId();

		autoPausen.add(Restrictions.eq(
				ZeiterfassungFac.FLR_ZEITDATEN_TAETIGKEIT_I_ID,
				taetigkeitIId_Geht));
		List<?> resultListAlleGehtimZeitraum = autoPausen.list();

		Iterator<?> resultListIterator = resultListAlleGehtimZeitraum
				.iterator();
		while (resultListIterator.hasNext()) {
			FLRZeitdaten zeitdaten = (FLRZeitdaten) resultListIterator.next();

			boolean bNurWarnung = false;
			ParametermandantDto parameter = null;
			try {
				parameter = (ParametermandantDto) getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_PERSONAL,
								ParameterFac.PARAMETER_AUTOMATISCHE_PAUSEN_NUR_WARNUNG);

				bNurWarnung = (Boolean) parameter.getCWertAsObject();

			} catch (RemoteException ex5) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
			}
			if (bNurWarnung == false) {
				getZeiterfassungFac()
						.pruefeUndErstelleAutomatischePausen(
								new java.sql.Timestamp(zeitdaten.getT_zeit()
										.getTime()),
								zeitdaten.getPersonal_i_id(), theClientDto);
				getZeiterfassungFac()
						.erstelleAutomatischeMindestpause(
								new java.sql.Timestamp(zeitdaten.getT_zeit()
										.getTime()),
								zeitdaten.getPersonal_i_id(), theClientDto);
			}
			getZeiterfassungFac().pruefeUndErstelleAutomatischesEndeBeiGeht(
					new java.sql.Timestamp(zeitdaten.getT_zeit().getTime()),
					zeitdaten.getPersonal_i_id(), theClientDto);
		}
		session.close();
	}

	public void automatikbuchungenLoeschen(java.sql.Date tVon,
			Integer personalIId, TheClientDto theClientDto, java.util.Date dBis) {
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();
		org.hibernate.Criteria autoPausen = session
				.createCriteria(FLRZeitdaten.class);
		autoPausen.createAlias("flrpersonal", "p");
		autoPausen.add(Restrictions.eq("p.mandant_c_nr",
				theClientDto.getMandant()));
		if (personalIId != null) {
			autoPausen.add(Restrictions.eq(
					ZeiterfassungFac.FLR_ZEITDATEN_PERSONAL_I_ID, personalIId));
		}
		autoPausen.add(Restrictions.ge(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT,
				tVon));
		autoPausen.add(Restrictions.le(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT,
				dBis));
		autoPausen.add(Restrictions.eq(
				ZeiterfassungFac.FLR_ZEITDATEN_B_AUTOMATIKBUCHUNG,
				Helper.boolean2Short(true)));
		/*
		 * autoPausen.add(Restrictions
		 * .isNull(ZeiterfassungFac.FLR_ZEITDATEN_MASCHINE_I_ID));
		 */
		List<?> resultListAutopausen = autoPausen.list();

		Iterator<?> resultListIterator = resultListAutopausen.iterator();
		while (resultListIterator.hasNext()) {
			FLRZeitdaten zeitdaten = (FLRZeitdaten) resultListIterator.next();

			Zeitdaten toRemove = em.find(Zeitdaten.class, zeitdaten.getI_id());
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						er);
			}

		}
		session.close();
	}

	public void pruefeUndErstelleAutomatischePausen(
			java.sql.Timestamp tZeitpunkt, Integer personalIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			PersonalzeitmodellDto personalzeitmodellDto = getPersonalFac()
					.personalzeitmodellFindZeitmodellZuDatum(personalIId,
							Helper.cutTimestamp(tZeitpunkt), theClientDto);
			// Wenn kein Zeitmodell zugeordnet, dann keine Pausen
			if (personalzeitmodellDto == null) {
				return;
			}

			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(tZeitpunkt.getTime());
			Integer tagesartIId = null;
			try {
				Query query = em.createNamedQuery("TagesartfindByCNr");
				query.setParameter(1, Helper.holeTagbezeichnungLang(c
						.get(Calendar.DAY_OF_WEEK)));
				Tagesart tagesart = (Tagesart) query.getSingleResult();

				tagesartIId = tagesart.getIId();
			} catch (NoResultException ex2) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex2);
			}

			BetriebskalenderDto dto = getPersonalFac()
					.betriebskalenderFindByMandantCNrDDatum(
							Helper.cutTimestamp(tZeitpunkt),
							theClientDto.getMandant(), theClientDto);
			if (dto != null) {
				Integer tagesartIId_Feiertag = tagesartFindByCNr(
						ZeiterfassungFac.TAGESART_FEIERTAG, theClientDto)
						.getIId();
				Integer tagesartIId_Halbtag = tagesartFindByCNr(
						ZeiterfassungFac.TAGESART_HALBTAG, theClientDto)
						.getIId();
				if (dto.getTagesartIId().equals(tagesartIId_Feiertag)
						|| dto.getTagesartIId().equals(tagesartIId_Halbtag)) {
					tagesartIId = dto.getTagesartIId();
				} else {
					tagesartIId = dto.getTagesartIId();
				}
			}

			// Ist fuer die heutige Tagesart etwas definiert?
			Integer zmtagIId = null;
			// try {
			Query query1 = em
					.createNamedQuery("ZeitmodelltagfindByZeitmodellIIdTagesartIId");
			query1.setParameter(1, personalzeitmodellDto.getZeitmodellIId());
			query1.setParameter(2, tagesartIId);
			// @todo getSingleResult oder getResultList ?
			Zeitmodelltag zmtag = null;
			try {
				zmtag = (Zeitmodelltag) query1.getSingleResult();
			} catch (javax.persistence.NoResultException e) {
				// nix
			} catch (NonUniqueResultException e) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, e);
			}
			if (zmtag != null) {
				zmtagIId = zmtag.getIId();
				// }
				// catch (NoResultException ex3) {
				// //nix
				// }
				if (zmtagIId == null) {
					// fuer heute nichts definiert
					return;
				}
			}

			// Hole Pausen
			ZeitmodelltagpauseDto[] zeitmodelltagpauseDtos = null;
			// try {
			Query query2 = em
					.createNamedQuery("ZeitmodelltagpausefindByZeitmodelltagIId");
			query2.setParameter(1, zmtagIId);

			Collection<?> cl = query2.getResultList();
			// if (! cl.isEmpty()) {
			zeitmodelltagpauseDtos = assembleZeitmodelltagpauseDtos(cl);
			// }
			// catch (NoResultException ex3) {
			// //nix
			// }
			if (zeitmodelltagpauseDtos == null
					|| zeitmodelltagpauseDtos.length == 0) {
				// keine pausen definiert
				return;
			}

			// Hole Zeitdaten eines Tages
			ZeitdatenDto[] zeitdatenDtos = null;
			// try {
			Query query3 = em
					.createNamedQuery("ZeitdatenfindZeitdatenEinesTagesUndEinerPerson");
			query3.setParameter(1, personalIId);
			query3.setParameter(2, Helper.cutTimestamp(tZeitpunkt));
			query3.setParameter(3, tZeitpunkt);
			Collection<?> cl1 = query3.getResultList();
			// if (! cl1.isEmpty()) {
			zeitdatenDtos = assembleZeitdatenDtosOhneBelegzeiten(cl1);
			// }
			// catch (NoResultException ex4) {
			// //nix
			// }

			if (zeitdatenDtos == null || zeitdatenDtos.length == 0) {
				// keine daten fuer heute
				return;
			}

			// Hole id der Taetigkeit KOMMT
			Integer taetigkeitIId_Kommt = taetigkeitFindByCNr(
					ZeiterfassungFac.TAETIGKEIT_KOMMT, theClientDto).getIId();
			// Hole id der Taetigkeit GEHT
			Integer taetigkeitIId_Geht = taetigkeitFindByCNr(
					ZeiterfassungFac.TAETIGKEIT_GEHT, theClientDto).getIId();

			java.sql.Timestamp letztesKommt = null;

			for (int i = zeitdatenDtos.length - 1; i >= 0; i--) {
				if (zeitdatenDtos[i].getTaetigkeitIId().equals(
						taetigkeitIId_Kommt)) {
					letztesKommt = zeitdatenDtos[i].getTZeit();
					break;
				}
				if (zeitdatenDtos[i].getTaetigkeitIId().equals(
						taetigkeitIId_Geht)) {
					// wenn zuerst ein GEHT vor einem KOMMT gefunden wurde, dann
					// zurueck
					return;
				}
			}

			if (letztesKommt != null) {

				for (int k = 0; k < zeitmodelltagpauseDtos.length; k++) {
					ZeitmodelltagpauseDto zeitmodelltagpauseDto = zeitmodelltagpauseDtos[k];
					// Hole daten des tages
					Session session = FLRSessionFactory.getFactory()
							.openSession();

					Timestamp tVon = new Timestamp(Helper.cutTimestamp(
							tZeitpunkt).getTime()
							+ zeitmodelltagpauseDto.getUBeginn().getTime()
							+ 3600000);
					Timestamp tBis = new Timestamp(Helper.cutTimestamp(
							tZeitpunkt).getTime()
							+ zeitmodelltagpauseDto.getUEnde().getTime()
							+ 3600000);

					if (tVon.after(tZeitpunkt)) {
						continue;
					}

					if (Helper.short2boolean(personalzeitmodellDto
							.getZeitmodellDto().getBFixepauseTrotzkommtgeht())) {
						// PJ18888 Wenn Kommt oder Geht zwischen VON_BIS bis,
						// dann
						// muss Pause mitgemacht werden
						if (letztesKommt.after(tVon)
								&& letztesKommt.before(tBis)) {
							tVon = new Timestamp(letztesKommt.getTime() + 10);
						}

						if (tBis.after(tZeitpunkt)) {
							tBis = new Timestamp(tZeitpunkt.getTime() - 10);
						}
					} else {
						if (tBis.after(tZeitpunkt)) {
							continue;
						}
					}

					// Wenn KOMMT vor Automatischem UNTER, sonst Naechste
					// Buchung
					if (letztesKommt.before(tVon)) {

						if (istPersonAnwesend(zeitdatenDtos,
								taetigkeitIId_Kommt, tVon)) {

							boolean bHatPlatz = false;

							while (bHatPlatz == false) {

								bHatPlatz = true;

								org.hibernate.Criteria zeitdaten = session
										.createCriteria(FLRZeitdaten.class);
								zeitdaten.add(Restrictions.ge(
										ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT,
										tVon));
								zeitdaten.add(Restrictions.le(
										ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT,
										tBis));
								zeitdaten
										.add(Restrictions
												.eq(ZeiterfassungFac.FLR_ZEITDATEN_PERSONAL_I_ID,
														personalIId));
								zeitdaten
										.addOrder(Order
												.asc(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT));

								List resultListArtikel = zeitdaten.list();

								Iterator resultListIterator = resultListArtikel
										.iterator();

								boolean bVonAnlegen = true;
								boolean bBisAnlegen = true;

								while (resultListIterator.hasNext()) {
									FLRZeitdaten zeile = (FLRZeitdaten) resultListIterator
											.next();
									if (zeile.getT_zeit().getTime() == tVon
											.getTime()) {
										// es gibt schon einen Eintrag zu dieser
										// Zeit
										bVonAnlegen = false;
									}
									if (zeile.getT_zeit().getTime() == tBis
											.getTime()) {
										// es gibt schon einen Eintrag zu dieser
										// Zeit
										bBisAnlegen = false;
									}

									if (!Helper
											.short2boolean(personalzeitmodellDto
													.getZeitmodellDto()
													.getBFixepauseTrotzkommtgeht())) {

										if (zeile.getTaetigkeit_i_id() != null) {
											bVonAnlegen = false;
											bBisAnlegen = false;
										}

										if (zeile.getT_zeit().getTime() > tVon
												.getTime()
												&& zeile.getT_zeit().getTime() < tBis
														.getTime()) {
											// Es gibt einen eintrag dazwischen
											// ->
											// Verschieben
											bHatPlatz = false;
											long diff = zeile.getT_zeit()
													.getTime()
													- tVon.getTime()
													+ 1000;
											tVon = new Timestamp(tVon.getTime()
													+ diff);
											tBis = new Timestamp(tBis.getTime()
													+ diff);

										}
									}

								}

								if (bHatPlatz == false) {
									continue;
								}

								session.close();
								Integer taetigkeitIId_Unter = taetigkeitFindByCNr(
										ZeiterfassungFac.TAETIGKEIT_UNTER,
										theClientDto).getIId();

								// Automatische Pause eintragen
								ZeitdatenDto autoPause = new ZeitdatenDto();
								autoPause.setPersonalIId(personalIId);
								autoPause.setTZeit(tVon);
								autoPause.setBAutomatikbuchung(Helper
										.boolean2Short(true));
								autoPause.setTaetigkeitIId(taetigkeitIId_Unter);
								if (bVonAnlegen) {
									createZeitdaten(autoPause, false, false,
											false, false, theClientDto);
								}
								autoPause.setTZeit(tBis);
								if (bBisAnlegen) {
									createZeitdaten(autoPause, false, false,
											false, false, theClientDto);
								}
							}
						} else {
							if (Helper.short2boolean(personalzeitmodellDto
									.getZeitmodellDto()
									.getBFixepauseTrotzkommtgeht())) {
								if (istPersonAnwesend(zeitdatenDtos,
										taetigkeitIId_Kommt, tBis)) {

									// Nun Pause-Ende buchen
									// Automatische Pause eintragen

									Integer taetigkeitIId_Unter = taetigkeitFindByCNr(
											ZeiterfassungFac.TAETIGKEIT_UNTER,
											theClientDto).getIId();

									ZeitdatenDto autoPause = new ZeitdatenDto();
									autoPause.setPersonalIId(personalIId);
									autoPause.setTZeit(tBis);
									autoPause.setBAutomatikbuchung(Helper
											.boolean2Short(true));
									autoPause
											.setTaetigkeitIId(taetigkeitIId_Unter);

									try {
										Query query = em
												.createNamedQuery("ZeitdatenfindByPersonalIIdTZeit");
										query.setParameter(1,
												autoPause.getPersonalIId());
										query.setParameter(2,
												autoPause.getTZeit());
										Zeitdaten zeitdaten2 = (Zeitdaten) query
												.getSingleResult();
									} catch (NoResultException ex) {
										// Wenn zu dem Zeitpunkt noch keine
										// buchung
										createZeitdaten(autoPause, false,
												false, false, false,
												theClientDto);

										// Und Unter-Beginn zum Zeitpunkt der
										// lettzen Taetigkeit eintragen (+ 10ms)

										org.hibernate.Criteria zeitdaten = session
												.createCriteria(FLRZeitdaten.class);

										zeitdaten
												.add(Restrictions
														.lt(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT,
																autoPause
																		.getTZeit()));
										zeitdaten
												.add(Restrictions
														.eq(ZeiterfassungFac.FLR_ZEITDATEN_PERSONAL_I_ID,
																personalIId));
										zeitdaten
												.add(Restrictions
														.isNotNull(ZeiterfassungFac.FLR_ZEITDATEN_TAETIGKEIT_I_ID));
										zeitdaten
												.addOrder(Order
														.desc(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT));
										zeitdaten.setMaxResults(1);
										List resultListArtikel = zeitdaten
												.list();

										Iterator resultListIterator = resultListArtikel
												.iterator();

										boolean bVonAnlegen = true;
										boolean bBisAnlegen = true;

										if (resultListIterator.hasNext()) {
											FLRZeitdaten zeile = (FLRZeitdaten) resultListIterator
													.next();
											autoPause
													.setTZeit(new Timestamp(
															zeile.getT_zeit()
																	.getTime() + 10));
											createZeitdaten(autoPause, false,
													false, false, false,
													theClientDto);

										}

									}

								}
							}
						}
					}
				}
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

	}

	private boolean istPersonAnwesend(ZeitdatenDto[] zeitdatenDtos,
			Integer taetigkeitIId_Kommt, java.sql.Timestamp tsZeitpunkt) {
		Timestamp letztesKommt = null;

		boolean bAnwesend = true;
		for (int i = zeitdatenDtos.length - 1; i >= 0; i--) {
			if (zeitdatenDtos[i].getTaetigkeitIId().equals(taetigkeitIId_Kommt)) {
				letztesKommt = zeitdatenDtos[i].getTZeit();
				break;
			}
		}

		if (letztesKommt != null) {

			Integer iLetzteTaetigkeit = null;
			int iAnzahlGleicherTaetigkeiten = 0;

			for (int i = zeitdatenDtos.length - 1; i >= 0; i--) {

				if (zeitdatenDtos[i].getTZeit().after(letztesKommt)
						&& zeitdatenDtos[i].getTZeit().before(tsZeitpunkt)) {

					if (iLetzteTaetigkeit == null) {

						iAnzahlGleicherTaetigkeiten++;
					} else {
						if (iLetzteTaetigkeit.equals(zeitdatenDtos[i]
								.getTaetigkeitIId())) {
							iAnzahlGleicherTaetigkeiten++;
						} else {
							iAnzahlGleicherTaetigkeiten = 1;
						}
					}
					iLetzteTaetigkeit = zeitdatenDtos[i].getTaetigkeitIId();
				}

			}

			if (iAnzahlGleicherTaetigkeiten % 2 == 1) {
				bAnwesend = false;
			}
		}

		return bAnwesend;
	}

	public void pruefeUndErstelleAutomatischesEndeBeiGeht(
			java.sql.Timestamp tZeitpunktGeht, Integer personalIId,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// Hole id der Taetigkeit KOMMT
		Integer taetigkeitIId_Kommt = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_KOMMT, theClientDto).getIId();
		// Hole id der Taetigkeit GEHT
		Integer taetigkeitIId_Geht = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_GEHT, theClientDto).getIId();

		ZeitdatenDto[] zeitdatenDtos = null;

		// try {
		// Hole Zeitdaten eines Tages
		Query query = em
				.createNamedQuery("ZeitdatenfindZeitdatenEinesTagesUndEinerPerson");
		query.setParameter(1, personalIId);
		query.setParameter(2, Helper.cutTimestamp(tZeitpunktGeht));
		query.setParameter(3, tZeitpunktGeht);
		Collection<?> cl = query.getResultList();
		// if (! cl.isEmpty()) {
		zeitdatenDtos = assembleZeitdatenDtos(query.getResultList());
		// }
		// catch (NoResultException ex) {
		// zeitdatenDtos = null;
		// }
		// Letztes KOMMT suchen
		Timestamp tLetztesKommt = null;
		ArrayList<ZeitdatenDto> alZeitdaten = new ArrayList<ZeitdatenDto>();
		for (int i = zeitdatenDtos.length - 1; i >= 0; i--) {
			ZeitdatenDto zeitdatenDto = zeitdatenDtos[i];
			alZeitdaten.add(zeitdatenDto);
			if (zeitdatenDto.getTaetigkeitIId() != null) {
				if (zeitdatenDto.getTaetigkeitIId().equals(taetigkeitIId_Geht)) {
					break;
				}
				if (zeitdatenDto.getTaetigkeitIId().equals(taetigkeitIId_Kommt)) {
					tLetztesKommt = zeitdatenDto.getTZeit();
					// kommt wieder aus liste entfernen
					alZeitdaten.remove(alZeitdaten.size() - 1);

					break;
				}
			}
		}

		query = em
				.createNamedQuery("MaschinenzeitdatenfindByPersonalIIdGestartet");
		query.setParameter(1, personalIId);
		query.setParameter(2, tZeitpunktGeht);
		cl = query.getResultList();

		Iterator<?> iterator = cl.iterator();
		while (iterator.hasNext()) {
			Maschinenzeitdaten maschinenzeitdaten = (Maschinenzeitdaten) iterator
					.next();
			// SP1568

			LossollarbeitsplanDto lsapDto = getFertigungFac()
					.lossollarbeitsplanFindByPrimaryKey(
							maschinenzeitdaten.getLossollarbeitsplanIId());

			if (Helper.short2boolean(lsapDto.getBAutoendebeigeht()) == true
					&& maschinenzeitdaten.getTBis() == null) {
				maschinenzeitdaten.setTBis(tZeitpunktGeht);
				em.merge(maschinenzeitdaten);
				em.flush();
			}

		}

	}

	public java.sql.Time getRelativeZeitFuerRelativesAendernAmClient(
			Integer personalIId, java.sql.Timestamp tBelegbuchung) {

		java.sql.Time tReturn = new java.sql.Time(-3600000);
		if (tBelegbuchung != null) {

			try {
				// Hole Zeitdaten eines Tages
				Query query = em
						.createNamedQuery("ZeitdatenfindZeitdatenEinesTagesUndEinerPerson");
				query.setParameter(1, personalIId);
				query.setParameter(2, tBelegbuchung);
				query.setParameter(3, new java.sql.Timestamp(Helper
						.cutTimestamp(tBelegbuchung).getTime() + 24 * 3600000));
				Collection<?> cl = query.getResultList();
				ZeitdatenDto[] zeitdatenDtos = null;
				// if (! cl.isEmpty()) {
				zeitdatenDtos = assembleZeitdatenDtos(cl);
				// }
				if (zeitdatenDtos.length > 1) {
					Query query1 = em.createNamedQuery("TaetigkeitfindByCNr");
					query1.setParameter(1, ZeiterfassungFac.TAETIGKEIT_ENDE);
					Integer taetigkeitIId_Ende = ((Taetigkeit) query1
							.getSingleResult()).getIId();
					ZeitdatenDto naechster = zeitdatenDtos[1];

					if (naechster.getIBelegartid() != null
							|| (naechster.getTaetigkeitIId() != null && naechster
									.getTaetigkeitIId().equals(
											taetigkeitIId_Ende))) {

						long diff = naechster.getTZeit().getTime()
								- zeitdatenDtos[0].getTZeit().getTime();
						tReturn = new java.sql.Time(-3600000 + diff + 10);

					}

				}
			} catch (NoResultException ex) {
				// Keine Zeitdaten vorhanden
			}
		}

		return tReturn;
	}

	public void aendereZeitRelativ(ZeitdatenDto zeitdatenDto,
			java.sql.Time tZeitRelativ, TheClientDto theClientDto) {
		if (zeitdatenDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zeitdatenDto == null"));
		}

		try {
			Zeitdaten zeitdaten1 = em.find(Zeitdaten.class,
					zeitdatenDto.getIId());
			if (zeitdaten1 != null) {
				java.sql.Timestamp tSucheZeitdatenVon = zeitdaten1.getTZeit();

				// Hole Zeitdaten eines Tages
				Query query = em
						.createNamedQuery("ZeitdatenfindZeitdatenEinesTagesUndEinerPerson");
				query.setParameter(1, zeitdatenDto.getPersonalIId());
				query.setParameter(2, tSucheZeitdatenVon);
				query.setParameter(
						3,
						new java.sql.Timestamp(
								Helper.cutTimestamp(zeitdatenDto.getTZeit())
										.getTime() + 24 * 3600000));

				ZeitdatenDto[] zeitdatenDtos = assembleZeitdatenDtos(query
						.getResultList());

				Query query1 = em.createNamedQuery("TaetigkeitfindByCNr");
				query1.setParameter(1, ZeiterfassungFac.TAETIGKEIT_ENDE);
				Integer taetigkeitIId_Ende = ((Taetigkeit) query1
						.getSingleResult()).getIId();
				query1 = em.createNamedQuery("TaetigkeitfindByCNr");
				query1.setParameter(1, ZeiterfassungFac.TAETIGKEIT_KOMMT);
				Integer taetigkeitIId_Kommt = ((Taetigkeit) query1
						.getSingleResult()).getIId();
				query1 = em.createNamedQuery("TaetigkeitfindByCNr");
				query1.setParameter(1, ZeiterfassungFac.TAETIGKEIT_GEHT);
				Integer taetigkeitIId_Geht = ((Taetigkeit) query1
						.getSingleResult()).getIId();

				// Wenn Sondertaetigkeiten vorhanden, dann Fehler ausgeben
				for (int i = 0; i < zeitdatenDtos.length; i++) {
					ZeitdatenDto dto = zeitdatenDtos[i];
					if (dto.getTaetigkeitIId() != null) {
						if (!dto.getTaetigkeitIId().equals(taetigkeitIId_Kommt)
								&& !dto.getTaetigkeitIId().equals(
										taetigkeitIId_Geht)
								&& !dto.getTaetigkeitIId().equals(
										taetigkeitIId_Ende)) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_RELATIVES_AENDERN_MIT_SONDERTAETIGKEITEN_NICHT_MOEGLICH,
									new Exception(""));
						}
					}
				}

				// Eigentlich duerften ab hier nur mehr Auftraege/ENDE und GEHT
				// vorhanden sein

				if (zeitdatenDtos.length > 1) {
					// Naeachste Buchung ist das Auftragsende
					ZeitdatenDto endeDesAuftrages = zeitdatenDtos[1];

					java.sql.Time tAuftragVon = new java.sql.Time(
							tSucheZeitdatenVon.getTime());
					java.sql.Time tAuftragBis = new java.sql.Time(
							endeDesAuftrages.getTZeit().getTime());

					long lDifferenz = (tZeitRelativ.getTime() + 3600000)
							- ((tAuftragBis.getTime() + 3600000) - (tAuftragVon
									.getTime() + 3600000));

					for (int i = 1; i < zeitdatenDtos.length; i++) {
						ZeitdatenDto dto = zeitdatenDtos[i];

						if (dto.getTaetigkeitIId() == null
								|| !dto.getTaetigkeitIId().equals(
										taetigkeitIId_Geht)) {

							Zeitdaten zeitdaten = em.find(Zeitdaten.class,
									dto.getIId());

							Timestamp neueZeit = new Timestamp(zeitdaten
									.getTZeit().getTime() + lDifferenz);

							zeitdaten.setTZeit(neueZeit);

							// WENNS ein ENDE ist, dann dieses noch aendern und
							// aufhoeren
							if (dto.getTaetigkeitIId() != null
									&& dto.getTaetigkeitIId().equals(
											taetigkeitIId_Ende)) {
								// wenn nachher ein geht ist, pruefen ob es
								// nicth
								// spaeter als das geth wird
								if (zeitdatenDtos.length > i + 1) {
									ZeitdatenDto dtoNaechstes = zeitdatenDtos[i + 1];
									if (dtoNaechstes.getTaetigkeitIId() != null
											&& dtoNaechstes.getTaetigkeitIId()
													.equals(taetigkeitIId_Geht)) {
										if (neueZeit.after(dtoNaechstes
												.getTZeit())) {
											throw new EJBExceptionLP(
													EJBExceptionLP.FEHLER_RELATIVES_AENDERN_ZUWENIG_ZEIT,
													new Exception(""));
										} else {
											return;
										}
									} else {
										return;
									}
								} else {
									return;
								}

							}
						} else {
							if (lDifferenz > 0) {
								throw new EJBExceptionLP(
										EJBExceptionLP.FEHLER_RELATIVES_AENDERN_ZUWENIG_ZEIT,
										new Exception(""));
							} else {
								// ENDE einbuchen
								dto.setTZeit(new Timestamp(dto.getTZeit()
										.getTime() + lDifferenz));
								dto.setTaetigkeitIId(taetigkeitIId_Ende);
								createZeitdaten(dto, false, false, false,
										false, theClientDto);
							}
							return;
						}

						// Nun alle nachfolgenden Buchungen (ausser GEHT)
						// verschieben

					}
				}
			}
		} catch (NoResultException ex) {
			// Keine Zeitdaten vorhanden
		}

	}

	public Integer bucheZeitRelativ(ZeitdatenDto zeitdatenDto,
			Timestamp tsAbDieserZeit, Boolean bAuchWennZuWenigZeit,
			boolean bIgnoriereFehler, TheClientDto theClientDto) {

		if (zeitdatenDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zeitdatenDto == null"));
		}
		if (bAuchWennZuWenigZeit == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("bAuchWennZuWenigZeit == null"));
		}
		if (zeitdatenDto.getPersonalIId() == null
				|| zeitdatenDto.getTZeit() == null
				|| zeitdatenDto.getArtikelIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"zeitdatenDto.getPersonalIId() == null || zeitdatenDto.getTZeit() == null || zeitdatenDto.getArtikelIId() == null"));
		}
		Integer id = null;
		Integer iIdEnde = null;
		// try {
		// Hole Zeitdaten eines Tages
		Query query = em
				.createNamedQuery("ZeitdatenfindZeitdatenEinesTagesUndEinerPerson");
		query.setParameter(1, zeitdatenDto.getPersonalIId());
		if (tsAbDieserZeit != null) {
			query.setParameter(2, new Timestamp(tsAbDieserZeit.getTime() + 10));
		} else {
			query.setParameter(2, Helper.cutTimestamp(zeitdatenDto.getTZeit()));
		}

		Integer taetigkeitIId_Geht = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_GEHT, theClientDto).getIId();

		query.setParameter(
				3,
				new java.sql.Timestamp(Helper.cutTimestamp(
						zeitdatenDto.getTZeit()).getTime() + 24 * 3600000));
		Collection<?> cl = query.getResultList();
		// if (! cl.isEmpty()) {
		ZeitdatenDto[] zeitdatenDtos = assembleZeitdatenDtos(query
				.getResultList());

		Query query1 = em.createNamedQuery("TaetigkeitfindByCNr");
		query1.setParameter(1, ZeiterfassungFac.TAETIGKEIT_ENDE);
		Integer taetigkeitIId_Ende = ((Taetigkeit) query1.getSingleResult())
				.getIId();
		query1 = em.createNamedQuery("TaetigkeitfindByCNr");
		query1.setParameter(1, ZeiterfassungFac.TAETIGKEIT_KOMMT);
		Integer taetigkeitIId_Kommt = ((Taetigkeit) query1.getSingleResult())
				.getIId();

		Timestamp tsAbDaFrei = null;
		if (tsAbDieserZeit != null) {
			tsAbDaFrei = tsAbDieserZeit;
		}

		boolean bLetzterEintragIstEnde = false;

		if (bIgnoriereFehler == false) {
			if (zeitdatenDtos != null && zeitdatenDtos.length == 0) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_RELATIVE_BUCHUNG_OHNE_KOMMT,
						new Exception("FEHLER_RELATIVE_BUCHUNG_OHNE_KOMMT"));

			}

			if (zeitdatenDtos != null && zeitdatenDtos.length > 0) {
				ZeitdatenDto dto = zeitdatenDtos[0];
				if (dto.getTaetigkeitIId() == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_RELATIVE_BUCHUNG_OHNE_KOMMT,
							new Exception("FEHLER_RELATIVE_BUCHUNG_OHNE_KOMMT"));

				} else if (dto.getTaetigkeitIId() != null
						&& !dto.getTaetigkeitIId().equals(taetigkeitIId_Kommt)) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_RELATIVE_BUCHUNG_OHNE_KOMMT,
							new Exception("FEHLER_RELATIVE_BUCHUNG_OHNE_KOMMT"));
				}

			}
		}

		Timestamp tsLetzterAuftrag = null;
		for (int i = zeitdatenDtos.length; i > 0; i--) {
			ZeitdatenDto dto = zeitdatenDtos[i - 1];
			if (dto.getArtikelIId() != null) {
				tsLetzterAuftrag = dto.getTZeit();
				// Wenn nach dem letzten Auftrag ein Geht, jedoch kein Ende
				// gebucht wurde, dann ist die komplette zeit verbraucht worden
				for (int z = 0; z < zeitdatenDtos.length; z++) {
					if (zeitdatenDtos[z].getTZeit().after(tsLetzterAuftrag)) {
						if (zeitdatenDtos[z].getTaetigkeitIId() != null) {
							if (taetigkeitIId_Geht.equals(zeitdatenDtos[z]
									.getTaetigkeitIId())) {
								throw new EJBExceptionLP(
										EJBExceptionLP.FEHLER_RELATIVE_BUCHUNG_GESAMTE_ZEIT_VERBUCHT,
										new Exception(
												"FEHLER_RELATIVE_BUCHUNG_GESAMTE_ZEIT_VERBUCHT"));
							} else {
								break;
							}
						}
					}
				}
				break;
			}
		}

		for (int i = zeitdatenDtos.length; i > 0; i--) {
			ZeitdatenDto dto = zeitdatenDtos[i - 1];
			if (dto.getTaetigkeitIId() != null
					&& dto.getTaetigkeitIId().equals(taetigkeitIId_Kommt)) {
				tsAbDaFrei = new Timestamp(dto.getTZeit().getTime() + 10);
				break;
			} else if (dto.getTaetigkeitIId() != null
					&& dto.getTaetigkeitIId().equals(taetigkeitIId_Ende)) {
				bLetzterEintragIstEnde = true;
				tsAbDaFrei = new Timestamp(dto.getTZeit().getTime());
				iIdEnde = dto.getIId();
				break;
			}
		}

		if (tsAbDaFrei != null) {

			if (tsLetzterAuftrag != null
					&& (tsLetzterAuftrag.after(tsAbDaFrei) || tsLetzterAuftrag
							.equals(tsAbDaFrei))) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_RELATIVE_BUCHUNG_ENDE_FEHLT,
						new Exception("FEHLER_RELATIVE_BUCHUNG_ENDE_FEHLT"));
			}

			java.sql.Time tZeitRelativ = new java.sql.Time(zeitdatenDto
					.getTZeit().getTime());
			ZeitdatenDto dto = zeitdatenDto;

			if (bLetzterEintragIstEnde) {
				// Letzten Ende-Eintrag updaten

				dto.setIId(iIdEnde);
				dto.setTZeit(tsAbDaFrei);
				updateZeitdaten(dto, theClientDto);
				id = iIdEnde;
			} else {

				// Beginneintrag des Auftrages erstellen, wenn Kommt
				dto.setTZeit(tsAbDaFrei);
				id = createZeitdaten(dto, false, false, false, false,
						theClientDto);
			}
			// Ende-Eintrag erstellen
			dto = new ZeitdatenDto();
			dto.setPersonalIId(zeitdatenDto.getPersonalIId());
			dto.setTaetigkeitIId(taetigkeitFindByCNr(
					ZeiterfassungFac.TAETIGKEIT_ENDE, theClientDto).getIId());

			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(tZeitRelativ.getTime());
			int stunde = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);
			int sekunde = c.get(Calendar.SECOND);

			long tZeit = stunde * 3600000 + minute * 60000 + sekunde * 1000;

			try {
				dto.setTZeit(sucheEndeZeitFuerRelativeZeitbuchung(tsAbDaFrei,
						zeitdatenDto.getPersonalIId(), tZeit, bIgnoriereFehler,
						theClientDto));

				// Millisekunden abschneiden
				c.setTimeInMillis(dto.getTZeit().getTime());
				c.set(Calendar.MILLISECOND, 0);
				dto.setTZeit(new Timestamp(c.getTimeInMillis()));
				boolean bGespeichert = false;
				while (bGespeichert == false) {
					try {
						createZeitdaten(dto, false, false, false, false,
								theClientDto);
						bGespeichert = true;
					} catch (EJBExceptionLP ex1) {
						if (ex1.getCode() == EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE) {
							dto.setTZeit(new Timestamp(
									dto.getTZeit().getTime() + 10));
						}
					}
				}
			} catch (EJBExceptionLP ex2) {
				if (ex2.getCode() == EJBExceptionLP.FEHLER_GEHT_VOR_ENDE) {
					if (bAuchWennZuWenigZeit.booleanValue() == false) {
						throw new EJBExceptionLP(ex2.getCode(), ex2);
					} else {
						return id;
					}
				} else {
					throw new EJBExceptionLP(ex2.getCode(), ex2);
				}

			}

		}

		return id;
	}

	public void speichereZeidatenVonZEStift(ZeitdatenDto[] zeitdatenDtos,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (zeitdatenDtos == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zeitdatenDto == null"));
		}

		int abstand = 10000;
		ParametermandantDto parameter = null;
		try {
			parameter = (ParametermandantDto) getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_PERSONAL,
							ParameterFac.PARAMETER_ZESTIFT_ABSTAND_BUCHUNGEN);
		} catch (RemoteException ex5) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
		}

		java.sql.Timestamp tLetztesDatum = new Timestamp(
				System.currentTimeMillis());
		HashMap<Object, Object> hmPersonen = new HashMap<Object, Object>();

		abstand = ((Integer) parameter.getCWertAsObject()).intValue() * 1000;

		for (int i = 0; i < zeitdatenDtos.length; i++) {
			ZeitdatenDto zeitdatenDto = zeitdatenDtos[i];

			if (tLetztesDatum.after(zeitdatenDto.getTZeit())) {
				tLetztesDatum = zeitdatenDto.getTZeit();
			}
			if (!hmPersonen.containsKey(zeitdatenDto.getPersonalIId())) {
				hmPersonen.put(zeitdatenDto.getPersonalIId(), "");
			}

			boolean bGespeichert = false;

			while (bGespeichert == false) {
				try {

					if (zeitdatenDto.bFertigFuerLossollarbeitsplan == false) {

						createZeitdaten(zeitdatenDto, true, true, true, false,
								theClientDto);

					} else {
						try {
							com.lp.server.fertigung.service.LossollarbeitsplanDto[] dtos = getFertigungFac()
									.lossollarbeitsplanFindByLosIIdArtikelIIdTaetigkeit(
											zeitdatenDto.getIBelegartid(),
											zeitdatenDto.getArtikelIId());

							// PJ 15388

							if (dtos != null && dtos.length > 0) {

								LossollarbeitsplanDto dto = dtos[0];
								dto.setBFertig(Helper.boolean2Short(true));

								getFertigungFac().updateLossollarbeitsplan(dto,
										theClientDto);

							}

						} catch (RemoteException ex) {
							throwEJBExceptionLPRespectOld(ex);
						}

					}
					bGespeichert = true;
				} catch (EJBExceptionLP ex) {
					// Wenn 2 Buchungen in der selben Sekunde gemacht werden,
					// wird bei der naechsten 100 Millisekunden hinzugefuegt
					if (ex.getCode() == EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE) {
						bGespeichert = false;
						zeitdatenDto.setTZeit(new Timestamp(zeitdatenDto
								.getTZeit().getTime() + 100));
					} else {
						throw ex;
					}
				}
			}

			// Wenn 2 Taetigkeiten innerhalb eines best. Zeitraums sind und
			// gleich sind, dann werden sie verworfen
			// ausser es betrifft verschiedene Personen
			if (i < zeitdatenDtos.length - 1) {

				ZeitdatenDto zeitdatenDto_Next = zeitdatenDtos[i + 1];
				Timestamp tZeitNext = zeitdatenDto_Next.getTZeit();
				Timestamp tZeitAkt = zeitdatenDto.getTZeit();

				if (zeitdatenDto.getPersonalIId().equals(
						zeitdatenDto_Next.getPersonalIId())) {

					if (tZeitNext.getTime() - tZeitAkt.getTime() < abstand) {

						Integer artikelIdNext = zeitdatenDto_Next
								.getArtikelIId();
						Integer artikelIdAKt = zeitdatenDto.getArtikelIId();

						Integer taetigkeitIdNext = zeitdatenDto_Next
								.getTaetigkeitIId();
						Integer taetigkeitIdAKt = zeitdatenDto
								.getTaetigkeitIId();

						if (artikelIdNext != null && artikelIdAKt != null) {
							if (artikelIdNext.equals(artikelIdAKt)) {

								Integer belegartIdIdNext = zeitdatenDto_Next
										.getIBelegartid();
								Integer belegartIdAKt = zeitdatenDto
										.getIBelegartid();

								if (belegartIdIdNext.equals(belegartIdAKt)) {
									myLogger.logKritisch("Folgende Zeitdaten wurden aufgrund PARAMETER_ZESTIFT_ABSTAND_BUCHUNGEN verworfen: "
											+ zeitdatenDto_Next.toString());
									i++;
								}
							}
						} else if (taetigkeitIdNext != null
								&& taetigkeitIdAKt != null) {
							if (taetigkeitIdNext.equals(taetigkeitIdAKt)) {
								myLogger.logKritisch("Folgende Zeitdaten wurden aufgrund PARAMETER_ZESTIFT_ABSTAND_BUCHUNGEN verworfen: "
										+ zeitdatenDto_Next.toString());
								i++;
							}
						}
					}
				}
			}
		}

		// Zum Abschluss Automatikbuchungen ueberpruefen
		if (tLetztesDatum != null && hmPersonen.size() > 0) {

			Iterator<?> it = hmPersonen.keySet().iterator();
			while (it.hasNext()) {
				Integer key = (Integer) it.next();
				automatikbuchungenAufrollen(
						new java.sql.Date(tLetztesDatum.getTime()),
						new java.sql.Date(System.currentTimeMillis()), key,
						theClientDto, true);
			}
		}
	}

	public BigDecimal getMengeGutSchlechtEinesLosSollarbeitsplanes(
			Integer lossollarbeitsplanIId, TheClientDto theClientDto,
			Boolean bGut) {
		if (lossollarbeitsplanIId == null || bGut == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"lossollarbeitsplanIId == null || bGut == null"));
		}

		String sQuery = "";

		if (bGut.booleanValue() == true) {
			sQuery = "select sum(zeitdaten.n_gut) from FLRLosgutschlecht zeitdaten WHERE zeitdaten.lossollarbeitsplan_i_id="
					+ lossollarbeitsplanIId;
		} else {
			sQuery = "select sum(zeitdaten.n_schlecht) from FLRLosgutschlecht zeitdaten WHERE zeitdaten.lossollarbeitsplan_i_id="
					+ lossollarbeitsplanIId;

		}
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		org.hibernate.Query summe = session.createQuery(sQuery);
		List<?> resultList = summe.list();
		Iterator<?> resultListIterator = resultList.iterator();
		BigDecimal menge = (BigDecimal) resultListIterator.next();

		if (menge == null) {
			menge = new BigDecimal(0);
		}

		return menge;
	}

	public Integer createZeitdaten(ZeitdatenDto zeitdatenDto,
			boolean bBucheAutoPausen, boolean bBucheMitternachtssprung,
			boolean bZeitverteilen, boolean bLospruefungAufFertig,
			TheClientDto theClientDto) throws EJBExceptionLP {
		return createZeitdaten(zeitdatenDto, bBucheAutoPausen,
				bBucheMitternachtssprung, bZeitverteilen,
				bLospruefungAufFertig, theClientDto, false);
	}

	private boolean binIchZwischenEinerVonBisBuchung(Integer personalIId,
			Timestamp tZeit, TheClientDto theclientDto) {

		boolean b = false;

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		org.hibernate.Criteria letztesKommtOderGeht = session
				.createCriteria(FLRZeitdaten.class);

		letztesKommtOderGeht.add(Expression.eq(
				ZeiterfassungFac.FLR_ZEITDATEN_PERSONAL_I_ID, personalIId));
		letztesKommtOderGeht.add(Expression.lt(
				ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT, tZeit));
		letztesKommtOderGeht.setMaxResults(1);
		letztesKommtOderGeht.addOrder(Order
				.desc(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT));

		List<?> resultLetztesKommtOderGeht = letztesKommtOderGeht.list();
		Iterator it = resultLetztesKommtOderGeht.iterator();

		if (it.hasNext()) {
			FLRZeitdaten z = (FLRZeitdaten) it.next();
			// Wenn vorher beleg dann bin ich dazwischen
			if (z.getI_belegartid() != null) {
				b = true;
			}
		}
		session.close();
		return b;
	}

	private Integer createZeitdaten(ZeitdatenDto zeitdatenDto,
			boolean bBucheAutoPausen, boolean bBucheMitternachtssprung,
			boolean bZeitverteilen, boolean bLospruefungAufFertig,
			TheClientDto theClientDto, boolean bRekursiv) throws EJBExceptionLP {

		// PJ18356

		boolean bVonBisErfassung = false;

		try {
			ParametermandantDto parameterVonBis = (ParametermandantDto) getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_PERSONAL,
							ParameterFac.PARAMETER_VON_BIS_ERFASSUNG);

			bVonBisErfassung = ((Boolean) parameterVonBis.getCWertAsObject());

			// Wenn von-bis erfassung, dann darf zwischen von-bis keine
			// Zeitbuchung
			// vorhanden sein

			if (bVonBisErfassung && zeitdatenDto.getTaetigkeitIId() != null) {

				Integer tetigkeitIId_Kommt = taetigkeitFindByCNr(
						ZeiterfassungFac.TAETIGKEIT_KOMMT, theClientDto)
						.getIId();
				Integer tetigkeitIId_Geht = taetigkeitFindByCNr(
						ZeiterfassungFac.TAETIGKEIT_GEHT, theClientDto)
						.getIId();

				if (zeitdatenDto.getTaetigkeitIId().equals(tetigkeitIId_Kommt)
						|| zeitdatenDto.getTaetigkeitIId().equals(
								tetigkeitIId_Geht)) {

					zeitdatenDto.settZeit_Bis(null);
				}
			}

			if (bVonBisErfassung && zeitdatenDto.gettZeit_Bis() != null) {
				ZeitdatenDto[] dtos = getZeiterfassungFac()
						.zeitdatenFindZeitdatenEinesTagesUndEinerPerson(
								zeitdatenDto.getPersonalIId(),
								zeitdatenDto.getTZeit(),
								new Timestamp(zeitdatenDto.gettZeit_Bis()
										.getTime()));
				if (dtos.length > 0) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BUCHUNG_ZWISCHEN_VON_BIS,
							new Exception(
									"EJBExceptionLP.FEHLER_BUCHUNG_ZWISCHEN_VON_BIS"));
				}
			}

			if (bVonBisErfassung == true) {

				Integer tetigkeitIId_Ende = taetigkeitFindByCNr(
						ZeiterfassungFac.TAETIGKEIT_ENDE, theClientDto)
						.getIId();

				if (zeitdatenDto.getTaetigkeitIId() != null
						&& zeitdatenDto.getTaetigkeitIId().equals(
								tetigkeitIId_Ende)) {

				} else {
					boolean b = binIchZwischenEinerVonBisBuchung(
							zeitdatenDto.getPersonalIId(),
							zeitdatenDto.getTZeit(), theClientDto);
					if (b == true) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_BUCHUNG_EINFUEGEN_ZWISCHEN_VON_BIS_NICHT_ERLAUBT,
								new Exception(
										"EJBExceptionLP.FEHLER_BUCHUNG_EINFUEGEN_ZWISCHEN_VON_BIS_NICHT_ERLAUBT"));
					}
				}

			}

		} catch (RemoteException ex5) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
		}

		boolean bNurWarnung = false;
		ParametermandantDto parameterNurMarnung = null;
		try {
			parameterNurMarnung = (ParametermandantDto) getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_PERSONAL,
							ParameterFac.PARAMETER_AUTOMATISCHE_PAUSEN_NUR_WARNUNG);

			bNurWarnung = (Boolean) parameterNurMarnung.getCWertAsObject();

		} catch (RemoteException ex5) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
		}

		if (zeitdatenDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zeitdatenDto == null"));
		} else if (zeitdatenDto.getPersonalIId() == null
				|| zeitdatenDto.getTZeit() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"zeitdatenDto.getPersonalIId() == null || zeitdatenDto.getTZeit() == null"));
		} else if (zeitdatenDto.getTaetigkeitIId() == null
				&& zeitdatenDto.getArtikelIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"zeitdatenDto.getTaetigkeitIId() == null && zeitdatenDto.getArtikelIId() == null"));
		}

		if (zeitdatenDto.getTaetigkeitIId() != null) {
			zeitdatenDto.setCBelegartnr(null);
			zeitdatenDto.setArtikelIId(null);
			zeitdatenDto.setIBelegartid(null);
			zeitdatenDto.setIBelegartpositionid(null);
			zeitdatenDto.setIBelegartpositionid(null);

			if (zeitdatenDto.getCBemerkungZuBelegart() != null
					&& zeitdatenDto.getCBemerkungZuBelegart().length() > 79) {
				zeitdatenDto.setCBemerkungZuBelegart(zeitdatenDto
						.getCBemerkungZuBelegart().substring(0, 79));
			}

		}

		// SP3289
		bringeFehlerWennZeitabschlussvorhanden(zeitdatenDto.getPersonalIId(),
				zeitdatenDto.getTZeit(), theClientDto);

		if (zeitdatenDto.getBAutomatikbuchung() == null) {
			zeitdatenDto.setBAutomatikbuchung(Helper.boolean2Short(false));
		}

		if (zeitdatenDto.getCWowurdegebucht() != null
				&& zeitdatenDto.getCWowurdegebucht().length() > 40) {
			zeitdatenDto.setCWowurdegebucht(zeitdatenDto.getCWowurdegebucht()
					.substring(0, 39));
		}

		ParametermandantDto parameterNachher = null;
		try {
			parameterNachher = (ParametermandantDto) getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_PERSONAL,
							ParameterFac.PARAMETER_BUCHUNG_IMMER_NACHHER_EINFUEGEN);
		} catch (RemoteException ex5) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
		}

		if (bBucheAutoPausen) {
			if (bNurWarnung == false) {
				pruefeUndErstelleAutomatischePausen(zeitdatenDto.getTZeit(),
						zeitdatenDto.getPersonalIId(), theClientDto);
			}
		}

		// PJ 17048
		boolean bNachherEinfuegen = ((Boolean) parameterNachher
				.getCWertAsObject()).booleanValue();

		if (bNachherEinfuegen == true) {
			boolean bHatPlatz = false;

			while (bHatPlatz == false) {
				try {
					Query query = em
							.createNamedQuery("ZeitdatenfindByPersonalIIdTZeit");
					query.setParameter(1, zeitdatenDto.getPersonalIId());
					query.setParameter(2, zeitdatenDto.getTZeit());
					Zeitdaten doppelt = (Zeitdaten) query.getSingleResult();
					zeitdatenDto.setTZeit(new Timestamp(zeitdatenDto.getTZeit()
							.getTime() + 30));
				} catch (NoResultException ex) {
					bHatPlatz = true;
				}
			}

		} else {
			try {
				Query query = em
						.createNamedQuery("ZeitdatenfindByPersonalIIdTZeit");
				query.setParameter(1, zeitdatenDto.getPersonalIId());
				query.setParameter(2, zeitdatenDto.getTZeit());
				Zeitdaten doppelt = (Zeitdaten) query.getSingleResult();
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_ZEITDATEN.UK"));
			} catch (NoResultException ex) {
				// //
			}
		}

		ZeitdatenDto[] letzeBuchungen = null;
		Integer taetigkeitIId_Geht = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_GEHT, theClientDto).getIId();
		Integer taetigkeitIId_Kommt = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_KOMMT, theClientDto).getIId();
		// try {
		Query query3 = em
				.createNamedQuery("ZeitdatenfindZeitdatenEinesTagesUndEinerPerson");
		query3.setParameter(1, zeitdatenDto.getPersonalIId());
		query3.setParameter(2, Helper.cutTimestamp(zeitdatenDto.getTZeit()));
		query3.setParameter(3, zeitdatenDto.getTZeit());
		Collection<?> cl = query3.getResultList();
		// if (!cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		letzeBuchungen = assembleZeitdatenDtosOhneBelegzeiten(query3
				.getResultList());
		ZeitdatenDto[] letzeBuchungenMitBelegen = assembleZeitdatenDtos(query3
				.getResultList());

		ZeitdatenDto[] letzeBuchungenMitBelegzeiten = assembleZeitdatenDtos(query3
				.getResultList());

		// WH: 13.02.2008: Wenn zuletzt ein GEHT gebucht wurde, und wieder ein
		// GEHT gebucht wird, wir dieses 'verschmissen'
		// WH: 23.08.2011: Wieder auskommentieren, da wir den Sinn von nicht
		// mher wussten
		/*
		 * if (letzeBuchungen.length > 0) { if (taetigkeitIId_Geht
		 * .equals(letzeBuchungen[letzeBuchungen.length - 1]
		 * .getTaetigkeitIId()) && taetigkeitIId_Geht.equals(zeitdatenDto
		 * .getTaetigkeitIId())) { return letzeBuchungen[letzeBuchungen.length -
		 * 1].getIId(); } }
		 */

		// }
		// catch (NoResultException ex3) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex3);
		// }
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ZEITDATEN);
			zeitdatenDto.setIId(pk);

			zeitdatenDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			zeitdatenDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());

			zeitdatenDto.setTAnlegen(new java.sql.Timestamp(System
					.currentTimeMillis()));
			if (zeitdatenDto.getTAendern() == null) {
				zeitdatenDto.setTAendern(new java.sql.Timestamp(System
						.currentTimeMillis()));
			}
			zeitdatenDto.setBTaetigkeitgeaendert(Helper.boolean2Short(false));

			// WENN ENDE dann auch zeitverteilen jedoch darf dann das ENDE nich
			// gebucht werden
			Integer tetigkeitIId_Ende = taetigkeitFindByCNr(
					ZeiterfassungFac.TAETIGKEIT_ENDE, theClientDto).getIId();

			Integer tetigkeitIId_geht = taetigkeitFindByCNr(
					ZeiterfassungFac.TAETIGKEIT_GEHT, theClientDto).getIId();

			Integer tetigkeitIId_Kommt = taetigkeitFindByCNr(
					ZeiterfassungFac.TAETIGKEIT_KOMMT, theClientDto).getIId();

			// ZEITVERTILUNG AUF LOSE

			boolean bVorherEndeEinbuchen = false;
			if (letzeBuchungenMitBelegzeiten.length > 0) {
				if (letzeBuchungenMitBelegzeiten[letzeBuchungenMitBelegzeiten.length - 1]
						.getCBelegartnr() != null) {
					bVorherEndeEinbuchen = true;
				}
			}

			boolean bZeitenverteilt = false;
			if (bZeitverteilen == true
					&& zeitdatenDto.getTaetigkeitIId() != null
					&& zeitdatenDto.getTaetigkeitIId()
							.equals(tetigkeitIId_Ende)) {
				// NUN ZEITVERTEILEN PJ 5589
				bZeitenverteilt = zeitAufLoseVerteilen(
						zeitdatenDto.getPersonalIId(), zeitdatenDto.getTZeit(),
						bVorherEndeEinbuchen, theClientDto);
			}

			if (bZeitverteilen == true
					&& zeitdatenDto.getTaetigkeitIId() != null
					&& zeitdatenDto.getTaetigkeitIId()
							.equals(tetigkeitIId_geht)) {
				zeitAufLoseVerteilen(zeitdatenDto.getPersonalIId(),
						zeitdatenDto.getTZeit(), bVorherEndeEinbuchen,
						theClientDto);
			}

			if (bZeitverteilen == true && zeitdatenDto.getCBelegartnr() != null
					&& zeitdatenDto.getIBelegartid() != null) {
				// NUN ZEITVERTEILEN PJ 5589
				zeitAufLoseVerteilen(zeitdatenDto.getPersonalIId(),
						zeitdatenDto.getTZeit(), bVorherEndeEinbuchen,
						theClientDto);
			}

			if (bZeitenverteilt == false) {

				Zeitdaten zeitdaten = new Zeitdaten(zeitdatenDto.getIId(),
						zeitdatenDto.getPersonalIId(), zeitdatenDto.getTZeit(),
						zeitdatenDto.getBTaetigkeitgeaendert(),
						zeitdatenDto.getPersonalIIdAnlegen(),
						zeitdatenDto.getPersonalIIdAendern(),
						zeitdatenDto.getBAutomatikbuchung());
				em.persist(zeitdaten);
				em.flush();
				setZeitdatenFromZeitdatenDto(zeitdaten, zeitdatenDto);
			}

			// Auto-Kommt/Unter
			ParametermandantDto parameter = null;
			try {
				parameter = (ParametermandantDto) getParameterFac()
						.getMandantparameter(theClientDto.getMandant(),
								ParameterFac.KATEGORIE_PERSONAL,
								ParameterFac.PARAMETER_AUTOMATISCHES_KOMMT);
			} catch (RemoteException ex5) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
			}

			int bAutoKommt = ((Integer) parameter.getCWertAsObject());

			if (bAutoKommt > 0 && bRekursiv == false) {

				// Bei KOMMT nichts tun
				if (!taetigkeitIId_Kommt
						.equals(zeitdatenDto.getTaetigkeitIId())) {

					SessionFactory factory = FLRSessionFactory.getFactory();
					Session session = factory.openSession();

					org.hibernate.Criteria letztesKommtOderGeht = session
							.createCriteria(FLRZeitdaten.class);

					letztesKommtOderGeht.add(Expression.eq(
							ZeiterfassungFac.FLR_ZEITDATEN_PERSONAL_I_ID,
							zeitdatenDto.getPersonalIId()));
					letztesKommtOderGeht.add(Restrictions.or(Expression.eq(
							ZeiterfassungFac.FLR_ZEITDATEN_TAETIGKEIT_I_ID,
							taetigkeitIId_Geht), Expression.eq(
							ZeiterfassungFac.FLR_ZEITDATEN_TAETIGKEIT_I_ID,
							taetigkeitIId_Kommt)));
					letztesKommtOderGeht.add(Expression.lt(
							ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT,
							zeitdatenDto.getTZeit()));
					letztesKommtOderGeht.setMaxResults(1);
					letztesKommtOderGeht.addOrder(Order
							.desc(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT));

					List<?> resultLetztesKommtOderGeht = letztesKommtOderGeht
							.list();

					if (resultLetztesKommtOderGeht.size() == 0) {
						if (zeitdatenDto.getArtikelIId() != null
								|| (zeitdatenDto.getTaetigkeitIId() != null && !zeitdatenDto
										.getTaetigkeitIId().equals(
												taetigkeitIId_Kommt))) {

							ZeitdatenDto dtoKommt = new ZeitdatenDto();
							dtoKommt.setTaetigkeitIId(taetigkeitIId_Kommt);
							dtoKommt.setPersonalIId(zeitdatenDto
									.getPersonalIId());
							dtoKommt.setCWowurdegebucht(zeitdatenDto
									.getCWowurdegebucht());
							dtoKommt.setCBemerkungZuBelegart("Folgebuchung AUTOMATISCHES_KOMMT");
							// Zeit 140 MS vorher
							dtoKommt.setTZeit(new Timestamp(zeitdatenDto
									.getTZeit().getTime() - 140));
							createZeitdaten(dtoKommt, false, false, false,
									false, theClientDto, true);
						}
					} else {
						// Wenn zuletzt Geht, dann vorher Kommt buchen

						FLRZeitdaten flrZeitdaten = (FLRZeitdaten) resultLetztesKommtOderGeht
								.iterator().next();

						if (flrZeitdaten.getTaetigkeit_i_id().equals(
								taetigkeitIId_Kommt)) {
							Query queryAutoKommt = em
									.createNamedQuery("ZeitdatenfindZeitdatenEinesTagesUndEinerPerson");
							queryAutoKommt.setParameter(1,
									zeitdatenDto.getPersonalIId());
							queryAutoKommt.setParameter(2,
									flrZeitdaten.getT_zeit());
							queryAutoKommt.setParameter(3,
									zeitdatenDto.getTZeit());

							ZeitdatenDto[] letzeBuchungenFuerAutoKommt = assembleZeitdatenDtosOhneBelegzeiten(queryAutoKommt
									.getResultList());
							if (letzeBuchungenFuerAutoKommt.length == 0
									&& zeitdatenDto.getTaetigkeitIId() == null) {
								// Vorher Kommt buchen
								ZeitdatenDto dtoKommt = new ZeitdatenDto();
								dtoKommt.setTaetigkeitIId(taetigkeitFindByCNr(
										ZeiterfassungFac.TAETIGKEIT_KOMMT,
										theClientDto).getIId());
								dtoKommt.setPersonalIId(zeitdatenDto
										.getPersonalIId());
								dtoKommt.setCWowurdegebucht(zeitdatenDto
										.getCWowurdegebucht());
								dtoKommt.setCBemerkungZuBelegart("Folgebuchung AUTOMATISCHES_KOMMT");
								// Zeit 140 MS vorher
								dtoKommt.setTZeit(new Timestamp(zeitdatenDto
										.getTZeit().getTime() - 140));
								createZeitdaten(dtoKommt, false, false, false,
										false, theClientDto, true);

							} else {
								if (letzeBuchungenFuerAutoKommt.length >= 1) {

									// Wenn die letzte Taetigkeit ein Geht war,
									// dann
									// vorher
									// Kommt buchen
									if (taetigkeitIId_Geht
											.equals(letzeBuchungenFuerAutoKommt[letzeBuchungenFuerAutoKommt.length - 1]
													.getTaetigkeitIId())) {
										// Vorher Kommt buchen
										ZeitdatenDto dtoKommt = new ZeitdatenDto();
										dtoKommt.setTaetigkeitIId(taetigkeitFindByCNr(
												ZeiterfassungFac.TAETIGKEIT_KOMMT,
												theClientDto).getIId());
										dtoKommt.setPersonalIId(zeitdatenDto
												.getPersonalIId());
										dtoKommt.setCWowurdegebucht(zeitdatenDto
												.getCWowurdegebucht());
										dtoKommt.setCBemerkungZuBelegart("Folgebuchung AUTOMATISCHES_KOMMT");
										// Zeit 140 MS vorher
										dtoKommt.setTZeit(new Timestamp(
												zeitdatenDto.getTZeit()
														.getTime() - 140));
										createZeitdaten(dtoKommt, false, false,
												false, false, theClientDto,
												true);

									} else {

										Integer taetigkeitIId_Unter = taetigkeitFindByCNr(
												ZeiterfassungFac.TAETIGKEIT_UNTER,
												theClientDto).getIId();

										int iAnzahlUnterHintereinander = 0;
										for (int i = letzeBuchungenFuerAutoKommt.length - 1; i > 0; i--) {
											ZeitdatenDto dto = letzeBuchungenFuerAutoKommt[i];
											if (taetigkeitIId_Unter.equals(dto
													.getTaetigkeitIId())) {
												iAnzahlUnterHintereinander++;
											} else {
												break;
											}
										}

										if (iAnzahlUnterHintereinander % 2 == 1
												&& !taetigkeitIId_Unter
														.equals(zeitdatenDto
																.getTaetigkeitIId())) {
											// PJ17898
											if (bAutoKommt == 1) {
												// UNTER Ende Buchen
												ZeitdatenDto dtoUnter = new ZeitdatenDto();
												dtoUnter.setTaetigkeitIId(taetigkeitFindByCNr(
														ZeiterfassungFac.TAETIGKEIT_UNTER,
														theClientDto).getIId());
												dtoUnter.setPersonalIId(zeitdatenDto
														.getPersonalIId());
												dtoUnter.setCWowurdegebucht(zeitdatenDto
														.getCWowurdegebucht());
												dtoUnter.setCBemerkungZuBelegart("Folgebuchung AUTOMATISCHES_KOMMT");
												// Zeit 140 MS vorher
												dtoUnter.setTZeit(new Timestamp(
														zeitdatenDto.getTZeit()
																.getTime() - 140));
												createZeitdaten(dtoUnter,
														false, false, false,
														false, theClientDto,
														true);
											}
										}
									}
								}

							}
						} else {
							ZeitdatenDto dtoKommt = new ZeitdatenDto();
							dtoKommt.setTaetigkeitIId(taetigkeitIId_Kommt);
							dtoKommt.setPersonalIId(zeitdatenDto
									.getPersonalIId());
							dtoKommt.setCWowurdegebucht(zeitdatenDto
									.getCWowurdegebucht());
							dtoKommt.setCBemerkungZuBelegart("Folgebuchung AUTOMATISCHES_KOMMT");
							// Zeit 140 MS vorher
							dtoKommt.setTZeit(new Timestamp(zeitdatenDto
									.getTZeit().getTime() - 140));
							createZeitdaten(dtoKommt, false, false, false,
									false, theClientDto, true);
						}

					}
				}
			}

			if (zeitdatenDto.getTaetigkeitIId() != null
					&& zeitdatenDto.getTaetigkeitIId()
							.equals(tetigkeitIId_geht)) {

				if (Helper.short2boolean(zeitdatenDto.getBAutomatikbuchung()) == false) {
					pruefeUndErstelleAutomatischesEndeBeiGeht(
							zeitdatenDto.getTZeit(),
							zeitdatenDto.getPersonalIId(), theClientDto);
				}

				// automatische Mindestpause
				// PJ18736
				if (bNurWarnung == false) {

					erstelleAutomatischeMindestpause(zeitdatenDto.getTZeit(),
							zeitdatenDto.getPersonalIId(), theClientDto);
				}
			}

			// PJ 16849
			if (zeitdatenDto.getTaetigkeitIId() != null
					&& zeitdatenDto.getTaetigkeitIId().equals(
							tetigkeitIId_Kommt)) {

				Integer zeiltmodellIId = null;
				java.sql.Time tKommt = new java.sql.Time(zeitdatenDto
						.getTZeit().getTime() % (24 * 3600000) + 3600000);

				double dKommt = Helper.time2Double(tKommt);

				double dKleinsterAbstand = 999;

				Query query = em
						.createNamedQuery("SchichtzeitmodellfindByPersonalIId");
				query.setParameter(1, zeitdatenDto.getPersonalIId());
				Collection c = query.getResultList();
				Iterator it = c.iterator();
				while (it.hasNext()) {
					Schichtzeitmodell sz = (Schichtzeitmodell) it.next();

					ZeitmodellDto zmDto = zeitmodellFindByPrimaryKey(
							sz.getZeitmodellIId(), theClientDto);

					Calendar cKommt = Calendar.getInstance();
					cKommt.setTime(zeitdatenDto.getTZeit());
					Integer tagesartIId = null;

					Query queryTa = em.createNamedQuery("TagesartfindByCNr");
					queryTa.setParameter(1, Helper
							.holeTagbezeichnungLang(cKommt
									.get(Calendar.DAY_OF_WEEK)));
					Tagesart tagesart = (Tagesart) queryTa.getSingleResult();
					tagesartIId = tagesart.getIId();

					if (tagesartIId != null) {

						try {
							Query query2 = em
									.createNamedQuery("ZeitmodelltagfindSollzeitZuTagesart");
							query2.setParameter(1, zmDto.getIId());
							query2.setParameter(2, tagesartIId);
							Zeitmodelltag zmTag = (Zeitmodelltag) query2
									.getSingleResult();

							java.sql.Time tfruehestesKommt = zmTag.getUBeginn();

							if (tfruehestesKommt != null
									&& tfruehestesKommt.getTime() > -3600000) {

								double dFruehestesKommt = Helper
										.time2Double(tfruehestesKommt);

								double dDiff = Math.abs(dFruehestesKommt
										- dKommt);

								if (dDiff < dKleinsterAbstand) {
									zeiltmodellIId = zmDto.getIId();
									dKleinsterAbstand = dDiff;
								}

							}

						} catch (NoResultException e) {
							// dann nix
						}

					}

				}

				// Nun das Zeitmodell eintragen, wenn
				if (zeiltmodellIId != null) {

					PersonalzeitmodellDto personalzeitmodellDto = null;
					try {
						personalzeitmodellDto = getPersonalFac()
								.personalzeitmodellFindZeitmodellZuDatum(
										zeitdatenDto.getPersonalIId(),
										zeitdatenDto.getTZeit(), theClientDto);

						if (personalzeitmodellDto != null
								&& !personalzeitmodellDto.getIId().equals(
										zeiltmodellIId)) {
							// Zeitmodell ungleich, dann eintragen

							Timestamp tHeute = Helper.cutTimestamp(zeitdatenDto
									.getTZeit());

							Query query2 = em
									.createNamedQuery("PersonalzeitmodellfindByPersonalIIdTDatum");
							query2.setParameter(1,
									zeitdatenDto.getPersonalIId());
							query2.setParameter(2, tHeute);
							Personalzeitmodell personalzeitmodell = null;
							try {
								personalzeitmodell = (Personalzeitmodell) query2
										.getSingleResult();
								personalzeitmodell
										.setZeitmodellIId(zeiltmodellIId);
								em.merge(personalzeitmodell);
								em.flush();
							} catch (NoResultException e) {
								PKGeneratorObj pkPz = new PKGeneratorObj();
								Integer pkIPz = pkGen
										.getNextPrimaryKey(PKConst.PK_PERSONALZEITMODELL);

								personalzeitmodell = new Personalzeitmodell(
										pkIPz, zeitdatenDto.getPersonalIId(),
										zeiltmodellIId, tHeute);
								em.merge(personalzeitmodell);
								em.flush();
							}

						}

					} catch (RemoteException ex) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
					}
				}

			}

			boolean bAufAngelegteLoseBuchenMoeglich = false;
			boolean bAufErledigteLoseAuftraegeBuchenMoeglich = false;
			try {
				ParametermandantDto parameterint = (ParametermandantDto) getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_PERSONAL,
								ParameterFac.PARAMETER_ZEITBUCHUNG_AUF_ANGELEGTE_LOSE_MOEGLICH);

				bAufAngelegteLoseBuchenMoeglich = ((Boolean) parameterint
						.getCWertAsObject());
				parameterint = (ParametermandantDto) getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_PERSONAL,
								ParameterFac.PARAMETER_ZEITBUCHUNG_AUF_ERLEDIGTE_MOEGLICH);

				bAufErledigteLoseAuftraegeBuchenMoeglich = ((Boolean) parameterint
						.getCWertAsObject());

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			if (zeitdatenDto.getCBelegartnr() != null
					&& zeitdatenDto.getCBelegartnr().equals(
							LocaleFac.BELEGART_ANGEBOT)) {
				if (zeitdatenDto.getIBelegartid() != null) {
					AngebotDto angebotDto = getAngebotFac()
							.angebotFindByPrimaryKeyOhneExec(
									zeitdatenDto.getIBelegartid());
					if (angebotDto != null) {
						if (angebotDto.getStatusCNr().equals(
								LocaleFac.STATUS_ERLEDIGT)) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_ZEITBUCHUNG_ERLEDIGTES_ANGEBOT_NICHT_MOEGLICH,
									"");
						}
						if (angebotDto.getStatusCNr().equals(
								LocaleFac.STATUS_STORNIERT)) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_ZEITBUCHUNG_STORNIERTES_ANGEBOT_NICHT_MOEGLICH,
									"");
						}
					}

				}
			}

			if (zeitdatenDto.getCBelegartnr() != null
					&& zeitdatenDto.getCBelegartnr().equals(
							LocaleFac.BELEGART_AUFTRAG)) {
				if (zeitdatenDto.getIBelegartid() != null) {
					AuftragDto auftragDto = getAuftragFac()
							.auftragFindByPrimaryKeyOhneExc(
									zeitdatenDto.getIBelegartid());
					if (auftragDto.getStatusCNr().equals(
							LocaleFac.STATUS_ERLEDIGT)
							&& bAufErledigteLoseAuftraegeBuchenMoeglich == false) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_ZEITBUCHUNG_ERLEDIGTER_AUFTRAG_NICHT_MOEGLICH,
								"");
					}
					if (auftragDto.getStatusCNr().equals(
							LocaleFac.STATUS_STORNIERT)
							&& bAufErledigteLoseAuftraegeBuchenMoeglich == false) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_ZEITBUCHUNG_STORNIERTER_AUFTRAG_NICHT_MOEGLICH,
								"");
					}

				}
			}

			// Losablieferungen auf "Neu Berechnen" setzen
			if (zeitdatenDto.getCBelegartnr() != null
					&& zeitdatenDto.getCBelegartnr().equals(
							LocaleFac.BELEGART_LOS)) {

				try {
					if (zeitdatenDto.getIBelegartid() != null) {

						LosDto losDto = getFertigungFac()
								.losFindByPrimaryKeyOhneExc(
										zeitdatenDto.getIBelegartid());

						if (losDto.getStatusCNr().equals(
								LocaleFac.STATUS_ERLEDIGT)
								&& bAufErledigteLoseAuftraegeBuchenMoeglich == false) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_ZEITBUCHUNG_ERLEDIGTES_LOS_NICHT_MOEGLICH,
									"");
						}
						if (losDto.getStatusCNr().equals(
								LocaleFac.STATUS_ANGELEGT)
								&& bAufAngelegteLoseBuchenMoeglich == false) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_ZEITBUCHUNG_ANGELEGTES_LOS_NICHT_MOEGLICH,
									"");
						}

						if (losDto.getStatusCNr().equals(
								LocaleFac.STATUS_GESTOPPT)) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_ZEITBUCHUNG_GESTOPPTES_LOS_NICHT_MOEGLICH,
									"");
						}
						if (losDto.getStatusCNr().equals(
								LocaleFac.STATUS_STORNIERT)) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_ZEITBUCHUNG_STORNIERTES_LOS_NICHT_MOEGLICH,
									"");
						}

						getFertigungFac().setzeLosablieferungenAufNeuBerechnen(
								zeitdatenDto.getIBelegartid(), theClientDto);
					}

					if (zeitdatenDto.getIBelegartpositionid() != null
							&& bLospruefungAufFertig == true) {
						LossollarbeitsplanDto sollaDto = getFertigungFac()
								.lossollarbeitsplanFindByPrimaryKeyOhneExc(
										zeitdatenDto.getIBelegartpositionid());

						if (sollaDto != null
								&& Helper.short2boolean(sollaDto.getBFertig())) {
							// SP2410
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_ZEITBUCHUNG_AUF_FERTIGEN_ARBEITSGANG_NICHT_MOEGLICH,
									"");

						}

					}

				} catch (RemoteException ex2) {
					throwEJBExceptionLPRespectOld(ex2);
				}

				try {
					LossollarbeitsplanDto[] dtos = getFertigungFac()
							.lossollarbeitsplanFindByLosIIdArtikelIIdTaetigkeit(
									zeitdatenDto.getIBelegartid(),
									zeitdatenDto.getArtikelIId());

					if (dtos != null && dtos.length > 0) {
						LossollarbeitsplanDto[] zusDtos = getFertigungFac()
								.getAlleZusatzlichZuBuchuchendenArbeitsgaenge(
										dtos[0].getIId(), theClientDto);

						for (int i = 0; i < zusDtos.length; i++) {
							if (Helper.short2boolean(zusDtos[i]
									.getBNurmaschinenzeit()) == false) {
								pk = pkGen
										.getNextPrimaryKey(PKConst.PK_ZEITDATEN);
								zeitdatenDto.setIId(pk);
								zeitdatenDto.setArtikelIId(zusDtos[i]
										.getArtikelIIdTaetigkeit());
								zeitdatenDto.setIBelegartpositionid(zusDtos[i]
										.getIId());
								zeitdatenDto
										.setTZeit(new Timestamp(zeitdatenDto
												.getTZeit().getTime() + 200));
								Zeitdaten zeitdaten = new Zeitdaten(
										zeitdatenDto.getIId(),
										zeitdatenDto.getPersonalIId(),
										zeitdatenDto.getTZeit(),
										zeitdatenDto.getBTaetigkeitgeaendert(),
										zeitdatenDto.getPersonalIIdAnlegen(),
										zeitdatenDto.getPersonalIIdAendern(),
										zeitdatenDto.getBAutomatikbuchung());

								setZeitdatenFromZeitdatenDto(zeitdaten,
										zeitdatenDto);
							}
						}

					}

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}

			if (zeitdatenDto.getCBelegartnr() != null
					&& zeitdatenDto.getCBelegartnr().equals(
							LocaleFac.BELEGART_PROJEKT)) {
				// PJ18156

				ProjektDto pjDto = getProjektFac()
						.projektFindByPrimaryKeyOhneExc(
								zeitdatenDto.getIBelegartid());

				if (pjDto.getStatusCNr().equals(
						ProjektServiceFac.PROJEKT_STATUS_STORNIERT)) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_ZEITBUCHUNG_STORNIERTES_PROJEKT_NICHT_MOEGLICH,
							"");

				}
				if (pjDto.getStatusCNr().equals(
						ProjektServiceFac.PROJEKT_STATUS_ERLEDIGT)) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_ZEITBUCHUNG_ERLEDIGTES_PROJEKT_NICHT_MOEGLICH,
							"");

				}

				if (pjDto.getTInternerledigt() != null) {

					try {
						ParametermandantDto parameterint = (ParametermandantDto) getParameterFac()
								.getMandantparameter(
										theClientDto.getMandant(),
										ParameterFac.KATEGORIE_PROJEKT,
										ParameterFac.PARAMETER_INTERN_ERLEDIGT_BEBUCHBAR);

						boolean bInterErledigteBebuchbar = ((Boolean) parameterint
								.getCWertAsObject());

						if (bInterErledigteBebuchbar == false) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_ZEITBUCHUNG_INTERN_ERLEDIGTES_PROJEKT_NICHT_MOEGLICH,
									"");
						}

					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

				}

			}

			if ((zeitdatenDto.getTaetigkeitIId() != null && zeitdatenDto
					.getTaetigkeitIId().equals(tetigkeitIId_Ende))
					|| (zeitdatenDto.getTaetigkeitIId() != null && zeitdatenDto
							.getTaetigkeitIId().equals(tetigkeitIId_geht))
					|| zeitdatenDto.getCBelegartnr() != null) {

				// Hole letzte Los-Buchung und pruefe deren Sollzeit
				// Sollzeitueberschreitung pruefen

				for (int i = letzeBuchungenMitBelegen.length - 1; i >= 0; i--) {
					if (letzeBuchungenMitBelegen[i].getCBelegartnr() != null) {
						if (letzeBuchungenMitBelegen[i].getCBelegartnr()
								.equals(LocaleFac.BELEGART_LOS)) {
							boolean bKommtVonTerminal = false;
							if (zeitdatenDto.getCWowurdegebucht() != null) {

								if (zeitdatenDto.getCWowurdegebucht()
										.startsWith("ZT")
										|| zeitdatenDto.getCWowurdegebucht()
												.startsWith("F630")
										|| zeitdatenDto.getCWowurdegebucht()
												.startsWith("KDC100")) {
									bKommtVonTerminal = true;
								}
							}
							pruefeObSollzeitenUeberschritten(
									letzeBuchungenMitBelegen[i]
											.getIBelegartid(),
									letzeBuchungenMitBelegen[i].getArtikelIId(),
									letzeBuchungenMitBelegen[i]
											.getPersonalIId(),
									bKommtVonTerminal, theClientDto);
							break;
						}
					}
				}
			}

			// PJ 16393 Bei ENDE Maschine stoppen
			if (zeitdatenDto.getTaetigkeitIId() != null
					&& zeitdatenDto.getTaetigkeitIId()
							.equals(tetigkeitIId_Ende)) {
				boolean bTheoretischeIstZeit = false;

				try {
					ParametermandantDto parameterIstZeit = (ParametermandantDto) getParameterFac()
							.getMandantparameter(
									theClientDto.getMandant(),
									ParameterFac.KATEGORIE_PERSONAL,
									ParameterFac.PARAMETER_THEORETISCHE_IST_ZEIT_RECHNUNG);

					bTheoretischeIstZeit = ((Boolean) parameterIstZeit
							.getCWertAsObject());

					if (bTheoretischeIstZeit == true) {
						// Hole letzte offen Maschinenzeit deren Sollarbeitsplan
						// Ruesten (AGART=NULL) ist
						Session session = FLRSessionFactory.getFactory()
								.openSession();
						org.hibernate.Criteria letztesKommtOderGeht = session
								.createCriteria(FLRZeitdaten.class);

						letztesKommtOderGeht.add(Expression.eq(
								ZeiterfassungFac.FLR_ZEITDATEN_PERSONAL_I_ID,
								zeitdatenDto.getPersonalIId()));
						letztesKommtOderGeht.add(Expression.eq(
								ZeiterfassungFac.FLR_ZEITDATEN_TAETIGKEIT_I_ID,
								taetigkeitIId_Kommt));
						letztesKommtOderGeht.add(Expression.lt(
								ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT,
								zeitdatenDto.getTZeit()));
						letztesKommtOderGeht.setMaxResults(1);
						letztesKommtOderGeht.addOrder(Order
								.desc(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT));

						List<?> resultLetztesKommt = letztesKommtOderGeht
								.list();

						if (resultLetztesKommt.size() > 0) {

							FLRZeitdaten lKommt = (FLRZeitdaten) resultLetztesKommt
									.iterator().next();
							// auch alle Maschinen der Person mit autoende
							// stoppen
							String sQuery = "select zeitdaten FROM FLRMaschinenzeitdaten zeitdaten WHERE zeitdaten.t_von>'"
									+ Helper.formatTimestampWithSlashes(new Timestamp(
											lKommt.getT_zeit().getTime()))
									+ "' AND zeitdaten.personal_i_id_gestartet="
									+ zeitdatenDto.getPersonalIId()
									+ " AND zeitdaten.t_bis IS NULL "
									+ " AND (zeitdaten.flrlossollarbeitsplan.agart_c_nr IS NULL OR zeitdaten.flrlossollarbeitsplan.b_autoendebeigeht = 1)"
									+ " ORDER BY zeitdaten.t_von DESC";

							Session session2 = FLRSessionFactory.getFactory()
									.openSession();

							org.hibernate.Query letzteOffeneMaschine = session2
									.createQuery(sQuery);
							// jetzt alle stoppen!!
							// letzteOffeneMaschine.setMaxResults(1);

							List<?> resultList = letzteOffeneMaschine.list();

							Iterator<?> resultListIterator = resultList
									.iterator();

							while (resultListIterator.hasNext()) {

								FLRMaschinenzeitdaten mz = (FLRMaschinenzeitdaten) resultListIterator
										.next();
								Maschinenzeitdaten maschinenzeitdaten = em
										.find(Maschinenzeitdaten.class,
												mz.getI_id());
								maschinenzeitdaten.setTBis(zeitdatenDto
										.getTZeit());
								em.merge(maschinenzeitdaten);
								em.flush();

							}
							session.close();
							session2.close();

						}

					}

				} catch (RemoteException ex5) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
				}
			}

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);

		}

		if (bVonBisErfassung && zeitdatenDto.gettZeit_Bis() != null) {
			ZeitdatenDto zDtoBis = new ZeitdatenDto();

			zDtoBis.setPersonalIId(zeitdatenDto.getPersonalIId());
			zDtoBis.setTZeit(zeitdatenDto.gettZeit_Bis());

			if (zeitdatenDto.getCBelegartnr() != null) {
				zDtoBis.setTaetigkeitIId(taetigkeitFindByCNr(
						ZeiterfassungFac.TAETIGKEIT_ENDE, theClientDto)
						.getIId());
			} else {
				zDtoBis.setTaetigkeitIId(zeitdatenDto.getTaetigkeitIId());
			}

			zDtoBis.setCWowurdegebucht(zeitdatenDto.getCWowurdegebucht());
			zDtoBis.setPersonalIId(zeitdatenDto.getPersonalIId());
			createZeitdaten(zDtoBis, false, false, false, false, theClientDto,
					true);

		}

		HvDtoLogger<ZeitdatenDto> zeitdatenLogger = new HvDtoLogger<ZeitdatenDto>(
				em, zeitdatenDto.getPersonalIId(), theClientDto);
		zeitdatenLogger.logInsert(zeitdatenDto);

		return zeitdatenDto.getIId();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void pruefeObSollzeitenUeberschritten(Integer losIId,
			Integer arikelIId, Integer personalIId, boolean bKommtVonTerminal,
			TheClientDto theClientDto) {

		try {
			LossollarbeitsplanDto[] sollDtos = getFertigungFac()
					.lossollarbeitsplanFindByLosIIdArtikelIIdTaetigkeit(losIId,
							arikelIId);

			if (sollDtos.length > 0) {

				double dSoll = 0;

				{

					if (sollDtos[0].getLRuestzeit().doubleValue() > 0) {
						if (sollDtos[0].getNGesamtzeit() != null) {
							dSoll = dSoll
									+ sollDtos[0].getNGesamtzeit()
											.doubleValue();
						}
					} else {
						return;
					}

				}

				if (!sindZuvieleZeitdatenEinesBelegesVorhanden(
						LocaleFac.BELEGART_LOS, losIId, theClientDto)) {

					AuftragzeitenDto[] dtos = null;

					try {
						dtos = getAllZeitenEinesBeleges(LocaleFac.BELEGART_LOS,
								losIId, null, null, null, null, false, false,
								theClientDto);
					} catch (EJBExceptionLP e) {

						if (bKommtVonTerminal == true) {
							String nachricht = " Fehler in Zeitdaten bei R\u00FCstzeit\u00FCberschreitungspr\u00FCfung: ";

							ArrayList<?> al = e.getAlInfoForTheClient();
							String s = "";
							if (al != null && al.size() > 1) {
								if (al.get(0) instanceof Integer) {
									PersonalDto personalDto = getPersonalFac()
											.personalFindByPrimaryKey(
													(Integer) al.get(0),
													theClientDto);
									s += " ("
											+ personalDto.getCPersonalnr()
											+ " "
											+ personalDto.getPartnerDto()
													.formatFixName2Name1();
								}
								if (al.get(1) instanceof java.sql.Timestamp) {
									s += ", "
											+ Helper.formatDatum(
													(java.sql.Timestamp) al
															.get(1),
													theClientDto.getLocUi())
											+ ")";
								}

							}
							nachricht += s;

							getBenutzerFac().sendJmsMessageMitArchiveintrag(
									BenutzerFac.NA_RUESTZEIT_UEBERSCHRITTEN,
									nachricht, theClientDto);
							return;
						} else {
							throw e;
						}

					}

					double zeiten = 0;
					for (int i = 0; i < dtos.length; i++) {
						if (dtos[i] != null && dtos[i].getDdDauer() != null
								&& dtos[i].getArtikelIId().equals(arikelIId)) {
							zeiten = zeiten
									+ dtos[i].getDdDauer().doubleValue();
						}
					}

					if (zeiten > dSoll) {
						LosDto losDto = getFertigungFac().losFindByPrimaryKey(
								losIId);

						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(
										sollDtos[0].getArtikelIIdTaetigkeit(),
										theClientDto);

						PersonalDto personalDto = getPersonalFac()
								.personalFindByPrimaryKey(personalIId,
										theClientDto);

						String nachricht = " R\u00FCstzeit \u00FCberschritten bei Losnummer: "
								+ losDto.getCNr()
								+ " T\u00E4tigkeit: "
								+ artikelDto.formatArtikelbezeichnung()
								+ " Person: "
								+ personalDto.getPartnerDto().formatAnrede();
						getBenutzerFac().sendJmsMessageMitArchiveintrag(
								BenutzerFac.NA_RUESTZEIT_UEBERSCHRITTEN,
								nachricht, theClientDto);

					}

				}
			}

		} catch (RemoteException e) {
			return;
		}

	}

	private long getVerfuegbareZeitImZeitraumOhneSondertaetigkeiten(
			Integer personalIId, Timestamp tZeitVon, Timestamp tZeitBis) {
		// Hole Zeitdaten eines Tages
		Query query = em
				.createNamedQuery("ZeitdatenfindZeitdatenEinesTagesUndEinerPerson");
		query.setParameter(1, personalIId);
		query.setParameter(2, tZeitVon);
		query.setParameter(3, tZeitBis);
		Collection<?> cl = query.getResultList();

		ZeitdatenDto[] dtos = assembleZeitdatenDtosOhneBelegzeiten(query
				.getResultList());

		long lZeitgesamt = tZeitBis.getTime() - tZeitVon.getTime();

		// Paarweise Sondertaetigkeiten abziehen
		Query query2 = em.createNamedQuery("TaetigkeitfindAll");

		TaetigkeitDto[] tDtos = assembleTaetigkeitDtos(query2.getResultList());

		for (int i = 0; i < tDtos.length; i++) {
			try {
				double d = berechnePaarweiserSondertaetigkeiten(dtos,
						tDtos[i].getIId());

				lZeitgesamt -= d * 3600000;

			} catch (Exception e) {
				// Wenn Felher, dann wird das ignoriert
			}
		}

		return lZeitgesamt;

	}

	public boolean zeitAufLoseVerteilen(Integer personalIId,
			Timestamp tZeitBis, boolean bVorherEndeEinbuchen,
			TheClientDto theClientDto) {
		// Wenn Taetigkeit GEHT/ENDE oder eine Fortsetzung eines Loses gebucht
		// wird, dann Zeiten verteilen

		// Zuerst alle mit artikel_i_id=NULL loeschen, da hier anscheinend keine
		// Abschlussbuchung durchgefuehrt wurde
		ZeitverteilungDto[] zeitverteilungDtos = zeitverteilungFindByPersonalIIdUndTag(
				personalIId, tZeitBis);
		for (int i = 0; i < zeitverteilungDtos.length; i++) {
			if (zeitverteilungDtos[i].getArtikelIId() == null) {
				Zeitverteilung zeitverteilung = em.find(Zeitverteilung.class,
						zeitverteilungDtos[i].getIId());
				em.remove(zeitverteilung);
				em.flush();
			}
		}

		zeitverteilungDtos = zeitverteilungFindByPersonalIIdUndTag(personalIId,
				tZeitBis);
		if (zeitverteilungDtos.length > 0
				&& zeitverteilungDtos[0].getArtikelIId() != null) {

			ZeitdatenDto zeitdatenDto = new ZeitdatenDto();

			zeitdatenDto.setPersonalIId(personalIId);
			zeitdatenDto.setTaetigkeitIId(taetigkeitFindByCNr(
					ZeiterfassungFac.TAETIGKEIT_ENDE, theClientDto).getIId());
			zeitdatenDto.setTZeit(zeitverteilungDtos[0].getTZeit());

			// PJ18152 Wenn Beginnzeiten waehrend einer Pause gestempelt
			// wutrden, dann muss die naechste Sondertaetigkeit verwendet werden
			ZeitdatenDto[] zeitdatenDtos = null;
			// try {
			Query query3 = em
					.createNamedQuery("ZeitdatenfindZeitdatenEinesTagesUndEinerPerson");
			query3.setParameter(1, personalIId);
			query3.setParameter(2,
					Helper.cutTimestamp(zeitverteilungDtos[0].getTZeit()));
			query3.setParameter(3, zeitverteilungDtos[0].getTZeit());
			Collection<?> cl1 = query3.getResultList();
			// if (! cl1.isEmpty()) {
			zeitdatenDtos = assembleZeitdatenDtosOhneBelegzeiten(cl1);

			boolean b = istPersonAnwesend(
					zeitdatenDtos,
					taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_KOMMT,
							theClientDto).getIId(),
					zeitverteilungDtos[0].getTZeit());

			if (b == false) {

				String sQuery = "select zeitdaten FROM FLRZeitdaten zeitdaten WHERE zeitdaten.t_zeit>'"
						+ Helper.formatTimestampWithSlashes(zeitverteilungDtos[0]
								.getTZeit())
						+ "' AND zeitdaten.personal_i_id="
						+ personalIId
						+ " AND zeitdaten.taetigkeit_i_id is not null) ORDER BY zeitdaten.t_zeit ASC";

				Session session = FLRSessionFactory.getFactory().openSession();

				org.hibernate.Query naechsteSondertaetigkeit = session
						.createQuery(sQuery);
				naechsteSondertaetigkeit.setMaxResults(1);

				List<?> resultNaechsteSondertaetigkeit = naechsteSondertaetigkeit
						.list();

				if (resultNaechsteSondertaetigkeit.size() > 0) {
					FLRZeitdaten flr = (FLRZeitdaten) resultNaechsteSondertaetigkeit
							.iterator().next();
					zeitdatenDto.setTZeit(new Timestamp(flr.getT_zeit()
							.getTime() + 10));
				}
				session.close();

			}

			createZeitdaten(zeitdatenDto, false, false, false, false,
					theClientDto);

			// Hole alle Zeitdaten im Bereich

			long lVerfuegbar = getVerfuegbareZeitImZeitraumOhneSondertaetigkeiten(
					personalIId, zeitdatenDto.getTZeit(), tZeitBis);

			long lZeitProLos = lVerfuegbar / zeitverteilungDtos.length;

			Integer iId_Zeitdaten = null;

			for (int i = 0; i < zeitverteilungDtos.length; i++) {

				ZeitdatenDto dto = new ZeitdatenDto();
				dto.setPersonalIId(personalIId);
				dto.setCBelegartnr(LocaleFac.BELEGART_LOS);
				dto.setIBelegartid(zeitverteilungDtos[i].getLosIId());
				dto.setArtikelIId(zeitverteilungDtos[i].getArtikelIId());

				try {
					// Auf Taetigkeit buchen, wenn nicht vorhanden auf die
					// erste Position
					com.lp.server.fertigung.service.LossollarbeitsplanDto[] dtos = getFertigungFac()
							.lossollarbeitsplanFindByLosIIdArtikelIIdTaetigkeit(
									zeitverteilungDtos[i].getLosIId(),
									zeitverteilungDtos[i].getArtikelIId());

					if (dtos != null && dtos.length > 0) {
						dto.setIBelegartpositionid(dtos[0].getIId());
					} else {
						com.lp.server.fertigung.service.LossollarbeitsplanDto[] dtosErstePosition = getFertigungFac()
								.lossollarbeitsplanFindByLosIId(
										zeitverteilungDtos[i].getLosIId());
						if (dtosErstePosition != null
								&& dtosErstePosition.length > 0) {
							dto.setIBelegartpositionid(dtosErstePosition[0]
									.getIId());

						}
					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				dto.setTZeit(new Timestamp(Helper.cutTimestamp(tZeitBis)
						.getTime() + lZeitProLos));

				if (iId_Zeitdaten == null) {
					iId_Zeitdaten = bucheZeitRelativ(dto, null, true, true,
							theClientDto);
				} else {
					ZeitdatenDto temp = zeitdatenFindByPrimaryKey(
							iId_Zeitdaten, theClientDto);
					iId_Zeitdaten = bucheZeitRelativ(dto, temp.getTZeit(),
							true, true, theClientDto);
				}
			}

			removeZeitverteilungByPersonalIIdUndTag(personalIId, tZeitBis);
			return true;
		}
		return false;
	}

	public String pruefeObWiederholendePauseVorhanden(
			java.sql.Timestamp tDatum, Integer personalIId,
			TheClientDto theClientDto) {

		String sWarnung = null;

		Integer tagesartIId_Feiertag = tagesartFindByCNr(
				ZeiterfassungFac.TAGESART_FEIERTAG, theClientDto).getIId();
		Integer tagesartIId_Halbtag = tagesartFindByCNr(
				ZeiterfassungFac.TAGESART_HALBTAG, theClientDto).getIId();
		// Berechnungsdatum abschneiden, damit nur mehr JJJJ.MM.TT ueberbleibt

		// Heutiges Datum herausfinden und abschneiden, damit nur mehr
		// JJJJ.MM.TT ueberbleibt
		ZeitmodelltagDto zeitmodelltagDto = getZeitmodelltagZuDatum(
				personalIId, Helper.cutTimestamp(tDatum), tagesartIId_Feiertag,
				tagesartIId_Halbtag, false, theClientDto);

		if (zeitmodelltagDto != null
				&& zeitmodelltagDto.getZeitmodellIId() != null) {

			ZeitmodellDto zmDto = zeitmodellFindByPrimaryKey(
					zeitmodelltagDto.getZeitmodellIId(), theClientDto);

			// PAUSE1

			java.sql.Time pausenZeit = zeitmodelltagDto.getUMindestpause();

			Integer taetigkeitIId_Kommt = taetigkeitFindByCNr(
					ZeiterfassungFac.TAETIGKEIT_KOMMT, theClientDto).getIId();
			// Hole id der Taetigkeit GEHT
			Integer taetigkeitIId_Geht = taetigkeitFindByCNr(
					ZeiterfassungFac.TAETIGKEIT_GEHT, theClientDto).getIId();
			// Hole id der Taetigkeit UNTER
			Integer taetigkeitIId_Unter = taetigkeitFindByCNr(
					ZeiterfassungFac.TAETIGKEIT_UNTER, theClientDto).getIId();

			if (zeitmodelltagDto.getUAutopauseab() != null
					&& pausenZeit != null) {
				if ((zeitmodelltagDto.getUAutopauseab().getTime() != -3600000 && pausenZeit
						.getTime() != -3600000)) {
					try {

						Query query = em
								.createNamedQuery("ZeitdatenfindZeitdatenEinesTagesUndEinerPerson");
						query.setParameter(1, personalIId);
						query.setParameter(2, Helper.cutTimestamp(tDatum));
						query.setParameter(3, Helper.cutTimestamp(Helper
								.addiereTageZuTimestamp(tDatum, 1)));
						Collection<?> cl = query.getResultList();
						// if (! cl.isEmpty()) {
						ZeitdatenDto[] zeitdatenDtos = assembleZeitdatenDtosOhneBelegzeiten(cl);

						ArrayList<ZeitdatenDto> alZeitdaten = new ArrayList<ZeitdatenDto>();
						for (int i = 0; i < zeitdatenDtos.length; i++) {
							alZeitdaten.add(zeitdatenDtos[i]);
						}

						if (alZeitdaten.size() > 0) {

							ArrayList<ZeitdatenDto> alUnter = new ArrayList<ZeitdatenDto>();
							for (int i = 0; i < alZeitdaten.size(); i++) {
								ZeitdatenDto zeitdatenDto = alZeitdaten.get(i);
								if (zeitdatenDto.getTaetigkeitIId() != null
										&& zeitdatenDto.getTaetigkeitIId()
												.equals(taetigkeitIId_Unter)) {
									alUnter.add(zeitdatenDto);
								}
							}

							// Wenn Pausenanzahl gerade

							// SP3037 Wenn am heutigen Tag ungerade Pausen, dann
							// wird noch nicht fertig gebucht sein
							if (alUnter.size() % 2 != 0 && alUnter.size() > 0) {

								if (Helper.cutTimestamp(tDatum).getTime() == Helper
										.cutTimestamp(
												new Timestamp(System
														.currentTimeMillis()))
										.getTime()) {
									alUnter.remove(alUnter.size() - 1);
								}

							}

							if (zeitdatenDtos[zeitdatenDtos.length - 1]
									.getTaetigkeitIId() != null
									&& !zeitdatenDtos[zeitdatenDtos.length - 1]
											.getTaetigkeitIId().equals(
													taetigkeitIId_Geht)) {
								// Wenn Heute und der letzte eintrag ist kein
								// Geht,
								// dann GEHT=JETZT simulieren
								if (Helper.cutTimestamp(tDatum).getTime() == Helper
										.cutTimestamp(
												new Timestamp(System
														.currentTimeMillis()))
										.getTime()) {
									ZeitdatenDto zDto_geht = ZeitdatenDto
											.clone(zeitdatenDtos[0]);
									zDto_geht
											.setTaetigkeitIId(taetigkeitIId_Geht);
									zDto_geht.setTZeit(new Timestamp(System
											.currentTimeMillis()));
									alZeitdaten.add(zDto_geht);

								}

							}

							Timestamp tLetztePauseVollstaendig = alZeitdaten
									.get(0).getTZeit();

							if (alUnter.size() % 2 == 0) {
								for (int i = 0; i < alUnter.size(); i++) {
									if (i % 2 == 0) {
										ZeitdatenDto dto1 = (ZeitdatenDto) alUnter
												.get(i);
										ZeitdatenDto dto2 = (ZeitdatenDto) alUnter
												.get(i + 1);
										long lDauerPause = dto2.getTZeit()
												.getTime()
												- dto1.getTZeit().getTime();
										// Nur Pausen beruecksichtigen, die
										// groesser als die Mindestzeit sind

										if (lDauerPause >= (pausenZeit
												.getTime() + 3600000)) {

											// Ist die Differenz zwischend er
											// letzten Vollstaendigen Pause und
											// dem Pausenbginn klsiner als
											// vorgegeben, dann Fehler

											long ldiff = dto1.getTZeit()
													.getTime()
													- tLetztePauseVollstaendig
															.getTime();

											if (ldiff > zeitmodelltagDto
													.getUAutopauseab()
													.getTime() + 3600000) {
												// FEHLER zuwenig pause

												return meldungFuerFehlendeMindespauseErzeugen(
														pausenZeit.getTime() + 3600000,
														new Timestamp(
																tLetztePauseVollstaendig
																		.getTime()
																		+ (zeitmodelltagDto
																				.getUAutopauseab()
																				.getTime() + 3600000)),
														theClientDto);

											} else {
												tLetztePauseVollstaendig = dto2
														.getTZeit();
											}

										}

									}
								}
							}

							// Nun noch Zeit wzischen der letzen Pause und Geht
							// pruefen

							long diffDerLetztenPauseUndeGeht = alZeitdaten
									.get(alZeitdaten.size() - 1).getTZeit()
									.getTime()
									- tLetztePauseVollstaendig.getTime();

							if (diffDerLetztenPauseUndeGeht > (zeitmodelltagDto
									.getUAutopauseab().getTime() + 3600000)) {
								return meldungFuerFehlendeMindespauseErzeugen(
										pausenZeit.getTime() + 3600000,
										new Timestamp(tLetztePauseVollstaendig
												.getTime()
												+ (zeitmodelltagDto
														.getUAutopauseab()
														.getTime() + 3600000)),
										theClientDto);
							}

						}

					} catch (EJBExceptionLP ex1) {
						// Mindestpause eintragen nicht moeglich

					}

				}
			}

			if (sWarnung != null) {
				return sWarnung;
			}

		}
		return sWarnung;
	}

	public String erstelleAutomatischeMindestpause(java.sql.Timestamp tGeht,
			Integer personalIId, TheClientDto theClientDto) {

		ParametermandantDto parameter = null;
		try {
			parameter = (ParametermandantDto) getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_PERSONAL,
							ParameterFac.PARAMETER_AUTOMATISCHE_PAUSEN_NUR_WARNUNG);

			boolean bNurWarnung = (Boolean) parameter.getCWertAsObject();

			// PJ18774
			if (bNurWarnung == true) {
				return pruefeObWiederholendePauseVorhanden(tGeht, personalIId,
						theClientDto);
			}

		} catch (RemoteException ex5) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
		}

		Integer tagesartIId_Feiertag = tagesartFindByCNr(
				ZeiterfassungFac.TAGESART_FEIERTAG, theClientDto).getIId();
		Integer tagesartIId_Halbtag = tagesartFindByCNr(
				ZeiterfassungFac.TAGESART_HALBTAG, theClientDto).getIId();
		// Berechnungsdatum abschneiden, damit nur mehr JJJJ.MM.TT ueberbleibt

		// Heutiges Datum herausfinden und abschneiden, damit nur mehr
		// JJJJ.MM.TT ueberbleibt
		ZeitmodelltagDto zeitmodelltagDto = getZeitmodelltagZuDatum(
				personalIId, Helper.cutTimestamp(tGeht), tagesartIId_Feiertag,
				tagesartIId_Halbtag, false, theClientDto);

		if (zeitmodelltagDto != null
				&& zeitmodelltagDto.getZeitmodellIId() != null) {

			ZeitmodellDto zmDto = zeitmodellFindByPrimaryKey(
					zeitmodelltagDto.getZeitmodellIId(), theClientDto);

			// PAUSE1

			java.sql.Time pausenZeit = zeitmodelltagDto.getUMindestpause();
			mindestpauseBuchen(tGeht, personalIId,
					zeitmodelltagDto.getUAutopauseab(), pausenZeit,
					zeitmodelltagDto.getUBeginn(),
					Helper.short2boolean(zmDto.getBDynamisch()), theClientDto);

			// PAUSE2
			if (pausenZeit != null
					&& zeitmodelltagDto.getUMindestpause2() != null
					&& zeitmodelltagDto.getUAutopauseab2().getTime() != -3600000) {
				pausenZeit = new Time(zeitmodelltagDto.getUMindestpause2()
						.getTime());

				mindestpauseBuchen(tGeht, personalIId,
						zeitmodelltagDto.getUAutopauseab2(), pausenZeit,
						zeitmodelltagDto.getUBeginn(),
						Helper.short2boolean(zmDto.getBDynamisch()),
						theClientDto);

				// PAUSE3
				if (zeitmodelltagDto.getUMindestpause3() != null
						&& zeitmodelltagDto.getUAutopauseab3().getTime() != -3600000) {
					pausenZeit = new Time(zeitmodelltagDto.getUMindestpause3()
							.getTime());
					mindestpauseBuchen(tGeht, personalIId,
							zeitmodelltagDto.getUAutopauseab3(), pausenZeit,
							zeitmodelltagDto.getUBeginn(),
							Helper.short2boolean(zmDto.getBDynamisch()),
							theClientDto);

				}

			}
		}
		return null;
	}

	private String meldungFuerFehlendeMindespauseErzeugen(long lPausenzeit,
			Timestamp tBis, TheClientDto theClientDto) {

		MessageFormat mf = new MessageFormat(getTextRespectUISpr(
				"pers.warning.fehlende.mindestpause",
				theClientDto.getMandant(), theClientDto.getLocUi()));
		mf.setLocale(theClientDto.getLocUi());
		Object pattern[] = {

				Helper.formatZahl(lPausenzeit / (1000 * 60), 0,
						theClientDto.getLocUi()),

				Helper.formatTime(tBis, theClientDto.getLocUi()) };
		return mf.format(pattern);

	}

	private void mindestpauseBuchen(java.sql.Timestamp tGeht,
			Integer personalIId, java.sql.Time uAutopauseAb,
			java.sql.Time uMindestpause, java.sql.Time uBeginn,
			boolean bDynamisch, TheClientDto theClientDto) {
		// Hole id der Taetigkeit KOMMT
		Integer taetigkeitIId_Kommt = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_KOMMT, theClientDto).getIId();
		// Hole id der Taetigkeit GEHT
		Integer taetigkeitIId_Geht = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_GEHT, theClientDto).getIId();
		// Hole id der Taetigkeit UNTER
		Integer taetigkeitIId_Unter = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_UNTER, theClientDto).getIId();

		if (uAutopauseAb != null && uMindestpause != null) {
			if ((uAutopauseAb.getTime() != -3600000 && uMindestpause.getTime() != -3600000)) {
				try {

					java.sql.Timestamp letztesKommt = null;

					String sQuery = "select zeitdaten FROM FLRZeitdaten zeitdaten WHERE zeitdaten.t_zeit<'"
							+ Helper.formatTimestampWithSlashes(tGeht)
							+ "' AND zeitdaten.personal_i_id="
							+ personalIId
							+ " AND (zeitdaten.taetigkeit_i_id="
							+ taetigkeitIId_Kommt
							+ " OR zeitdaten.taetigkeit_i_id="
							+ taetigkeitIId_Geht
							+ ") ORDER BY zeitdaten.t_zeit DESC";

					Session session = FLRSessionFactory.getFactory()
							.openSession();

					org.hibernate.Query letzteKommtGeht = session
							.createQuery(sQuery);
					letzteKommtGeht.setMaxResults(1);

					List<?> resultList = letzteKommtGeht.list();

					Iterator<?> resultListIterator = resultList.iterator();

					if (resultListIterator.hasNext()) {
						FLRZeitdaten l = (FLRZeitdaten) resultListIterator
								.next();
						// Wenn zuletzt ein Kommt gebucht wurde, dann OK

						if (l.getTaetigkeit_i_id().equals(taetigkeitIId_Kommt)) {
							letztesKommt = new Timestamp(l.getT_zeit()
									.getTime());
						}
					}

					// PJ 17467
					ParametermandantDto parameter = null;
					try {
						parameter = (ParametermandantDto) getParameterFac()
								.getMandantparameter(
										theClientDto.getMandant(),
										ParameterFac.KATEGORIE_PERSONAL,
										ParameterFac.PARAMETER_AUTOMATISCHE_PAUSEN_AB_ERLAUBTEM_KOMMT);
					} catch (RemoteException ex5) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
					}

					if (letztesKommt == null) {
						return;
					}

					boolean bAutopausenAbErlaubtemKommt = ((Boolean) parameter
							.getCWertAsObject());

					if (bAutopausenAbErlaubtemKommt) {
						if (uBeginn != null && uBeginn.getTime() != -3600000) {

							// Das ist so eigentlich falsch. Die Zeitzone
							// (TimeZone) muss zum
							// Client passen der die Zeiten erfasst hat.
							Calendar c = GregorianCalendar.getInstance();
							c.setTimeInMillis(letztesKommt.getTime());

							long l = letztesKommt.getTime() % (24 * 3600000);

							// if (l < (zeitmodelltagDto.getUBeginn().getTime()
							// - 3600000)) {

							// TODO: Quickhack, damit Fr. Krautenbacher ihre
							// Abrechnungen machen kann.
							// Die Normalisierung des Kommt muss abhaengig
							// von der aktuellen
							// Sommerzeit/Normalzeitverschiebung erfolgen
							// Ich verstehe nicht, was die ganzen +/-
							// 3.600.000ms zu sagen haben?
							if (l < (uBeginn.getTime() - c
									.get(Calendar.DST_OFFSET))) {
								letztesKommt = new Timestamp(Helper
										.cutTimestamp(letztesKommt).getTime()
										+ (uBeginn.getTime() + 3600000));
							}

						}
					}

					// try {
					// Hole Zeitdaten eines Tages
					Query query = em
							.createNamedQuery("ZeitdatenfindZeitdatenEinesTagesUndEinerPerson");
					query.setParameter(1, personalIId);
					query.setParameter(2, letztesKommt);
					query.setParameter(3, tGeht);
					Collection<?> cl = query.getResultList();
					// if (! cl.isEmpty()) {
					ZeitdatenDto[] zeitdatenDtos = assembleZeitdatenDtosOhneBelegzeiten(cl);

					if (bDynamisch == true) {
						// ////////////

						// PJ 16676
						java.sql.Timestamp tAb = null;
						long lPausenzeit = 0;

						if (uAutopauseAb != null && uMindestpause != null
								&& uAutopauseAb.getTime() != -3600000
								&& uMindestpause.getTime() != -3600000) {
							long zeit = letztesKommt.getTime()
									+ uAutopauseAb.getTime() + 3600000;
							if (zeit >= tGeht.getTime()) {
								return;
							} else {
								tAb = new java.sql.Timestamp(zeit);
								lPausenzeit = uMindestpause.getTime() + 3600000;
							}
						} else {
							return;
						}

						// Hole Zeitdaten von Kommt bis Kommt + autopauseab
						Query query2 = em
								.createNamedQuery("ZeitdatenfindZeitdatenEinesTagesUndEinerPerson");
						query2.setParameter(1, personalIId);
						query2.setParameter(2, letztesKommt);
						query2.setParameter(3, tGeht);

						zeitdatenDtos = assembleZeitdatenDtosOhneBelegzeiten(query2
								.getResultList());

						ZeitdatenDto[] zeitdatenDtosMitBelegzeiten = assembleZeitdatenDtos(query2
								.getResultList());

						// Zeitdatenbuchung vorbereiten
						ZeitdatenDto zeitdatenDtoPause = new ZeitdatenDto();
						zeitdatenDtoPause.setPersonalIId(personalIId);
						zeitdatenDtoPause.setTaetigkeitIId(taetigkeitIId_Unter);
						zeitdatenDtoPause.setBAutomatikbuchung(Helper
								.boolean2Short(true));

						// UNTER MIT Autopausenzeit + GEHT oder Autopausenzeit +
						// Dauer einbuchen

						ArrayList<ZeitdatenDto> alUnter = new ArrayList<ZeitdatenDto>();
						for (int i = 0; i < zeitdatenDtos.length; i++) {
							ZeitdatenDto zeitdatenDto = zeitdatenDtos[i];
							if (zeitdatenDto.getTaetigkeitIId() != null
									&& zeitdatenDto.getTaetigkeitIId().equals(
											taetigkeitIId_Unter)) {
								alUnter.add(zeitdatenDto);
							}
						}

						long lRestPause = 0;
						// Wenn Pausenanzahl gerade

						// SP3037 Wenn am heutigen Tag ungerade Pausen, dann
						// wird noch nicht fertig gebucht sein
						if (alUnter.size() % 2 != 0 && alUnter.size() > 0) {

							if (Helper.cutTimestamp(tGeht).getTime() == Helper
									.cutTimestamp(
											new Timestamp(System
													.currentTimeMillis()))
									.getTime()) {
								alUnter.remove(alUnter.size() - 1);
							}

						}

						if (alUnter.size() % 2 == 0) {
							for (int i = 0; i < alUnter.size(); i++) {
								if (i % 2 == 0) {
									ZeitdatenDto dto1 = (ZeitdatenDto) alUnter
											.get(i);
									ZeitdatenDto dto2 = (ZeitdatenDto) alUnter
											.get(i + 1);
									lRestPause += dto2.getTZeit().getTime()
											- dto1.getTZeit().getTime();
								}
							}
						}

						long lNochZuVerbuchen = lPausenzeit - lRestPause;

						if (lNochZuVerbuchen < 1) {
							return;
						}

						// SP3171 Es muessen alle vorherigen Pausen
						// beruecksichtigt werden
						// Einkommentieren, wenn geklaert
						/*
						 * tAb = new Timestamp(tAb.getTime() + lRestPause); if
						 * (tAb.after(tGeht)) { return; }
						 */

						belegezeitenvorAutomatischeMindestpauseVerschieben(tAb,
								new java.sql.Timestamp(tAb.getTime()
										+ lNochZuVerbuchen),
								zeitdatenDtosMitBelegzeiten, theClientDto);

						zeitdatenDtoPause.setTZeit(new java.sql.Timestamp(tAb
								.getTime()));
						createZeitdaten(zeitdatenDtoPause, false, false, false,
								false, theClientDto);

						java.sql.Timestamp tBis = new java.sql.Timestamp(
								tAb.getTime() + lNochZuVerbuchen);

						if (tBis.after(tGeht)) {
							zeitdatenDtoPause.setTZeit(new java.sql.Timestamp(
									tGeht.getTime() - 5));
						} else {
							zeitdatenDtoPause.setTZeit(new java.sql.Timestamp(
									tAb.getTime() + lNochZuVerbuchen));
						}

						createZeitdaten(zeitdatenDtoPause, false, false, false,
								false, theClientDto);
						// erledigt
						return;

						// /////////
					} else {
						// PJ 16676
						java.sql.Timestamp tBis = null;
						long lPausenzeit = 0;

						if (uAutopauseAb != null && uMindestpause != null
								&& uAutopauseAb.getTime() != -3600000
								&& uMindestpause.getTime() != -3600000) {

							long zeit = letztesKommt.getTime()
									+ uAutopauseAb.getTime() + 3600000;

							if (zeit >= tGeht.getTime()) {
								// Wenn die Zeit zwischen Kommt und Geht
								// kleiner
								// als die Zeit der automatischen
								// Mindestpause
								// ist,
								// dann
								// ist nichts zu tun
								return;
							} else {
								tBis = new java.sql.Timestamp(zeit);
								lPausenzeit = uMindestpause.getTime() + 3600000;

							}

						}

						// Hole Zeitdaten von Kommt bis Kommt + autopauseab
						Query query2 = em
								.createNamedQuery("ZeitdatenfindZeitdatenEinesTagesUndEinerPerson");
						query2.setParameter(1, personalIId);
						query2.setParameter(2, letztesKommt);
						query2.setParameter(3, tBis);

						zeitdatenDtos = assembleZeitdatenDtosOhneBelegzeiten(query2
								.getResultList());

						ZeitdatenDto[] zeitdatenDtosMitBelegzeiten = assembleZeitdatenDtos(query2
								.getResultList());

						// Zeitdatenbuchung vorbereiten
						ZeitdatenDto zeitdatenDtoPause = new ZeitdatenDto();
						zeitdatenDtoPause.setPersonalIId(personalIId);
						zeitdatenDtoPause.setTaetigkeitIId(taetigkeitIId_Unter);
						zeitdatenDtoPause.setBAutomatikbuchung(Helper
								.boolean2Short(true));

						// Keine Buchung dazwischen, dann xx-minuten pause
						// buchen
						if (zeitdatenDtos.length == 1) {

							belegezeitenvorAutomatischeMindestpauseVerschieben(
									new java.sql.Timestamp(tBis.getTime()
											- lPausenzeit), tBis,
									zeitdatenDtosMitBelegzeiten, theClientDto);

							zeitdatenDtoPause.setTZeit(new java.sql.Timestamp(
									tBis.getTime() - lPausenzeit));
							createZeitdaten(zeitdatenDtoPause, false, false,
									false, false, theClientDto);
							zeitdatenDtoPause.setTZeit(tBis);
							createZeitdaten(zeitdatenDtoPause, false, false,
									false, false, theClientDto);
							// erledigt
							return;

						}

						// Wenn 2 Buchungen und nur KOMMT + GEHT, dann nichts
						if (zeitdatenDtos.length == 2) {
							ZeitdatenDto dto1 = zeitdatenDtos[0];
							ZeitdatenDto dto2 = zeitdatenDtos[1];
							if (dto1.getTaetigkeitIId() != null
									&& dto1.getTaetigkeitIId().equals(
											taetigkeitIId_Kommt)) {
								if (dto2.getTaetigkeitIId() != null
										&& dto2.getTaetigkeitIId().equals(
												taetigkeitIId_Geht)) {
									return;
								}
							}
						}

						ArrayList<ZeitdatenDto> alUnter = new ArrayList<ZeitdatenDto>();
						for (int i = 0; i < zeitdatenDtos.length; i++) {
							ZeitdatenDto zeitdatenDto = zeitdatenDtos[i];
							if (zeitdatenDto.getTaetigkeitIId() != null
									&& zeitdatenDto.getTaetigkeitIId().equals(
											taetigkeitIId_Unter)) {
								alUnter.add(zeitdatenDto);
							}
						}
						// Wenn mehr als eine Buchung, jedoch keine UNTER
						// Buchung, dann Pause Buchen
						if (alUnter.size() == 0) {

							zeitdatenDtoPause.setTZeit(new java.sql.Timestamp(
									tBis.getTime() - lPausenzeit));
							bucheAutomatischeMindestpause(personalIId,
									letztesKommt,
									new java.sql.Timestamp(tBis.getTime()
											- lPausenzeit), tBis, theClientDto);
							// erledigt
							return;

						}

						long lRestPause = 0;

						// Wenn Pausenazahl ungerade
						if (alUnter.size() % 2 == 1) {

							ZeitdatenDto dtoTemp = (ZeitdatenDto) alUnter
									.get(alUnter.size() - 1);
							lRestPause = tBis.getTime()
									- dtoTemp.getTZeit().getTime();
							tBis = new Timestamp(
									dtoTemp.getTZeit().getTime() - 10);
							alUnter.remove(alUnter.size() - 1);

						}

						if (lRestPause > lPausenzeit) {
							return;
						}

						// Nur eine Pausenbeginn vor Ablauf der Autopausenzeit
						if (alUnter.size() == 0) {

							belegezeitenvorAutomatischeMindestpauseVerschieben(
									new java.sql.Timestamp(tBis.getTime()
											- (lPausenzeit - lRestPause)),
									tBis, zeitdatenDtosMitBelegzeiten,
									theClientDto);

							zeitdatenDtoPause
									.setTZeit(new java.sql.Timestamp(tBis
											.getTime()
											- (lPausenzeit - lRestPause)));
							createZeitdaten(zeitdatenDtoPause, false, false,
									false, false, theClientDto);
							zeitdatenDtoPause.setTZeit(tBis);
							createZeitdaten(zeitdatenDtoPause, false, false,
									false, false, theClientDto);
							// erledigt
							return;

						}

						// Wenn Pausenanzahl gerade
						if (alUnter.size() % 2 == 0) {
							for (int i = 0; i < alUnter.size(); i++) {
								if (i % 2 == 0) {
									ZeitdatenDto dto1 = (ZeitdatenDto) alUnter
											.get(i);
									ZeitdatenDto dto2 = (ZeitdatenDto) alUnter
											.get(i + 1);
									lRestPause += dto2.getTZeit().getTime()
											- dto1.getTZeit().getTime();
								}
							}

							ZeitdatenDto dtoErstePauseEnde = (ZeitdatenDto) alUnter
									.get(alUnter.size() - 1);

							long lNochZuVerbuchen = lPausenzeit - lRestPause;

							if (lNochZuVerbuchen < 1) {
								return;
							}

							if (tBis.getTime()
									- dtoErstePauseEnde.getTZeit().getTime() > lNochZuVerbuchen) {

								belegezeitenvorAutomatischeMindestpauseVerschieben(
										new java.sql.Timestamp(tBis.getTime()
												- lNochZuVerbuchen), tBis,
										zeitdatenDtosMitBelegzeiten,
										theClientDto);

								bucheAutomatischeMindestpause(personalIId,
										letztesKommt, new java.sql.Timestamp(
												tBis.getTime()
														- lNochZuVerbuchen),
										tBis, theClientDto);

								// Komplette Pause verbucht ->Passt
								return;

							} else {
								// todo Naechten Freiraum suchen
							}

						}

						// }
						// catch (NoResultException ex) {
						// zeitdatenDtos = null;
						// }
					}

				} catch (EJBExceptionLP ex1) {
					// Mindestpause eintragen nicht moeglich

				}

			}
		}

	}

	private void belegezeitenvorAutomatischeMindestpauseVerschieben(
			java.sql.Timestamp tPauseVon, java.sql.Timestamp tPauseBis,
			ZeitdatenDto[] zeitdatenDtosMitBelegzeiten,
			TheClientDto theClientDto) {
		// Wenn Belegzeiten dazwischen sind, diese auf
		// "vor den Pausen-Beginn" verschieben

		// SP1792 + ENDE Buchungen auch verschieben
		Integer taetigkeitIId_Ende = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_ENDE, theClientDto).getIId();

		for (int i = 0; i < zeitdatenDtosMitBelegzeiten.length; i++) {
			ZeitdatenDto dtoTemp = zeitdatenDtosMitBelegzeiten[i];
			if ((dtoTemp.getArtikelIId() != null && dtoTemp.getCBelegartnr() != null)
					|| (dtoTemp.getTaetigkeitIId() != null && dtoTemp
							.getTaetigkeitIId().equals(taetigkeitIId_Ende))) {
				if (dtoTemp.getTZeit().getTime() >= tPauseVon.getTime()
						&& dtoTemp.getTZeit().getTime() <= tPauseBis.getTime()) {
					Zeitdaten z = em.find(Zeitdaten.class, dtoTemp.getIId());

					int iZusaetzlicheroffset = 0;
					boolean bHatPlatz = false;

					while (bHatPlatz == false) {
						try {
							Query query = em
									.createNamedQuery("ZeitdatenfindByPersonalIIdTZeit");
							query.setParameter(1, dtoTemp.getPersonalIId());
							query.setParameter(2,
									new Timestamp(
											(tPauseVon.getTime() - 10 * i)
													+ iZusaetzlicheroffset));
							Zeitdaten doppelt = (Zeitdaten) query
									.getSingleResult();
							iZusaetzlicheroffset = iZusaetzlicheroffset + 3;
						} catch (NoResultException ex) {
							bHatPlatz = true;
						}
					}

					z.setTZeit(new Timestamp((tPauseVon.getTime() - 10 * i)
							+ iZusaetzlicheroffset));
					z.setCBemerkungzubelegart("wg. automatischer Mindestpause verschoben.");
					em.merge(z);
				}
			}
		}
	}

	private boolean bucheAutomatischeMindestpause(Integer personalIId,
			Timestamp tLetztesKommt, Timestamp tPauseVon, Timestamp tPauseBis,
			TheClientDto theClientDto) {

		Integer taetigkeitIId_Unter = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_UNTER, theClientDto).getIId();

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session
				.createCriteria(FLRZeitdaten.class);
		crit.add(Restrictions.eq(ZeiterfassungFac.FLR_ZEITDATEN_PERSONAL_I_ID,
				personalIId));
		crit.add(Restrictions
				.isNotNull(ZeiterfassungFac.FLR_ZEITDATEN_TAETIGKEIT_I_ID));
		crit.add(Restrictions.gt(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT,
				tLetztesKommt));
		crit.add(Restrictions.lt(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT,
				tPauseVon));
		crit.addOrder(Order.desc(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT));
		// @todo ohne ENDE und STOP

		// wenn die Pause nicht vollstaendig Platz hat, dann wird die Taetigkeit
		// nicht unterbrochen.
		Session sessionPause = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria critPause = sessionPause
				.createCriteria(FLRZeitdaten.class);
		critPause.add(Restrictions.eq(
				ZeiterfassungFac.FLR_ZEITDATEN_PERSONAL_I_ID, personalIId));
		critPause.add(Restrictions
				.isNotNull(ZeiterfassungFac.FLR_ZEITDATEN_TAETIGKEIT_I_ID));
		critPause.add(Restrictions.ge(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT,
				tPauseVon));
		critPause.add(Restrictions.le(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT,
				tPauseBis));
		critPause.addOrder(Order.desc(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT));
		List<?> resultListPause = critPause.list();

		boolean bPauseHatVollstaendigPlatz = true;
		if (resultListPause.size() > 0) {
			bPauseHatVollstaendigPlatz = false;
		}

		// Zeitdatenbuchung vorbereiten
		ZeitdatenDto zeitdatenDtoUnterVon = new ZeitdatenDto();
		zeitdatenDtoUnterVon.setPersonalIId(personalIId);
		zeitdatenDtoUnterVon.setTaetigkeitIId(taetigkeitIId_Unter);
		zeitdatenDtoUnterVon.setBAutomatikbuchung(Helper.boolean2Short(true));
		zeitdatenDtoUnterVon.setTZeit(tPauseVon);

		ZeitdatenDto zeitdatenDtoUnterBis = new ZeitdatenDto();
		zeitdatenDtoUnterBis.setPersonalIId(personalIId);
		zeitdatenDtoUnterBis.setTaetigkeitIId(taetigkeitIId_Unter);
		zeitdatenDtoUnterBis.setBAutomatikbuchung(Helper.boolean2Short(true));
		zeitdatenDtoUnterBis.setTZeit(tPauseBis);

		List<?> resultList = crit.list();
		Iterator<?> resultListIterator = resultList.iterator();

		// Wenn zwischen geplanter Pause und Kommt Buchungen sind,
		// muessen diese unterbrochen werden, wenn die Pause vollstaendig darin
		// Platz hat

		if (resultListIterator.hasNext()) {
			FLRZeitdaten flrLetzteTaetigkeitVorPause = (FLRZeitdaten) resultListIterator
					.next();
			Integer iIdTaetigkeit = flrLetzteTaetigkeitVorPause
					.getTaetigkeit_i_id();
			int iAnzahl = 1;

			while (resultListIterator.hasNext()) {
				FLRZeitdaten flrZeitdaten = (FLRZeitdaten) resultListIterator
						.next();
				if (iIdTaetigkeit.equals(flrZeitdaten.getTaetigkeit_i_id())) {
					iAnzahl++;
				}
			}

			sessionPause.close();

			// Wenn eine ungerade Anzahl von Sondertaetigkeiten, dann muss diese
			// vorher beendet werden
			// aber nur wenn sie bezahlt ist
			if (taetigkeitFindByPrimaryKey(iIdTaetigkeit, theClientDto)
					.getFBezahlt() > 0) {

				if (iAnzahl % 2 == 1) {
					// Sondertaetigkeit vorher beenden
					ZeitdatenDto zeitdatenDtoVorherEnde = new ZeitdatenDto();
					zeitdatenDtoVorherEnde.setPersonalIId(personalIId);
					zeitdatenDtoVorherEnde.setTaetigkeitIId(iIdTaetigkeit);
					zeitdatenDtoVorherEnde.setBAutomatikbuchung(Helper
							.boolean2Short(true));
					zeitdatenDtoVorherEnde.setTZeit(new Timestamp(tPauseVon
							.getTime() - 100));
					createZeitdaten(zeitdatenDtoVorherEnde, false, false,
							false, false, theClientDto);
				}

			}
			// PauseBeginn buchen
			createZeitdaten(zeitdatenDtoUnterVon, false, false, false, false,
					theClientDto);

			// PauseEnde buchen
			createZeitdaten(zeitdatenDtoUnterBis, false, false, false, false,
					theClientDto);

			if (taetigkeitFindByPrimaryKey(iIdTaetigkeit, theClientDto)
					.getFBezahlt() > 0) {
				if (iAnzahl % 2 == 1) {
					// Sondertaetigkeit nachher wieder beginnen
					ZeitdatenDto zeitdatenDtoNachherBeginn = new ZeitdatenDto();
					zeitdatenDtoNachherBeginn.setPersonalIId(personalIId);
					zeitdatenDtoNachherBeginn.setTaetigkeitIId(iIdTaetigkeit);
					zeitdatenDtoNachherBeginn.setBAutomatikbuchung(Helper
							.boolean2Short(true));
					zeitdatenDtoNachherBeginn.setTZeit(new Timestamp(tPauseBis
							.getTime() + 100));
					createZeitdaten(zeitdatenDtoNachherBeginn, false, false,
							false, false, theClientDto);
				}
			}

		} else {
			// PauseBeginn buchen
			createZeitdaten(zeitdatenDtoUnterVon, false, false, false, false,
					theClientDto);
			// PauseEnde buchen
			createZeitdaten(zeitdatenDtoUnterBis, false, false, false, false,
					theClientDto);
		}
		session.close();
		return true;
	}

	public void removeZeitdaten(ZeitdatenDto zeitdatenDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.entry();
		if (zeitdatenDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zeitdatenDto == null"));
		}
		if (zeitdatenDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("zeitdatenDto.getIId() == null"));
		}

		// try {
		Zeitdaten zeitdaten = em.find(Zeitdaten.class, zeitdatenDto.getIId());
		if (zeitdaten == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		// SP3289
		bringeFehlerWennZeitabschlussvorhanden(zeitdatenDto.getPersonalIId(),
				zeitdatenDto.getTZeit(), theClientDto);

		HvDtoLogger<ZeitdatenDto> zeitdatenLogger = new HvDtoLogger<ZeitdatenDto>(
				em, zeitdatenDto.getPersonalIId(), theClientDto);
		zeitdatenLogger.logDelete(zeitdatenDto);

		byte[] CRLFAscii = { 13, 10 };
		Query q2 = em.createNamedQuery("LosgutschlechtFindByZeitdatenIId");
		q2.setParameter(1, zeitdatenDto.getIId());

		if (q2.getResultList().size() > 0) {

			Iterator it = q2.getResultList().iterator();

			String meldung = "";

			while (it.hasNext()) {
				Losgutschlecht lgs = (Losgutschlecht) it.next();

				LossollarbeitsplanDto soaDto = getFertigungFac()
						.lossollarbeitsplanFindByPrimaryKey(
								lgs.getLossollarbeitsplanIId());

				try {
					LosDto losDto = getFertigungFac().losFindByPrimaryKey(
							soaDto.getLosIId());

					meldung += losDto.getCNr() + " AG "
							+ soaDto.getIArbeitsgangnummer();

					if (soaDto.getIUnterarbeitsgang() != null) {
						meldung += "." + soaDto.getIUnterarbeitsgang();
					}

					meldung += ";" + new String(CRLFAscii);

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}

			ArrayList al = new ArrayList();
			al.add(meldung);

			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_LOSGUTSCHLECHT_VORHANDEN, al,
					new Exception("FEHLER_LOSGUTSCHLECHT_VORHANDEN"));
		}

		try {
			em.remove(zeitdaten);
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}

		// PJ18356
		if (zeitdatenDto.getZeitdatenIId_BisZeit() != null) {
			Zeitdaten zdBis = em.find(Zeitdaten.class,
					zeitdatenDto.getZeitdatenIId_BisZeit());
			em.remove(zdBis);
			em.flush();
		}

	}

	public void updateZeitdaten(ZeitdatenDto zeitdatenDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (zeitdatenDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zeitdatenDto == null"));
		} else if (zeitdatenDto.getIId() == null
				|| zeitdatenDto.getPersonalIId() == null
				|| zeitdatenDto.getTZeit() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"zeitdatenDto.getIId() == null || zeitdatenDto.getPersonalIId() == null || zeitdatenDto.getTZeit() == null"));
		} else if (zeitdatenDto.getTaetigkeitIId() == null
				&& zeitdatenDto.getArtikelIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"zeitdatenDto.getTaetigkeitIId() == null && zeitdatenDto.getArtikelIId() == null"));
		}

		boolean bVonBisErfassung = false;

		try {
			ParametermandantDto parameterVonBis = (ParametermandantDto) getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_PERSONAL,
							ParameterFac.PARAMETER_VON_BIS_ERFASSUNG);

			bVonBisErfassung = ((Boolean) parameterVonBis.getCWertAsObject());

			Integer tetigkeitIId_Kommt = taetigkeitFindByCNr(
					ZeiterfassungFac.TAETIGKEIT_KOMMT, theClientDto).getIId();
			Integer tetigkeitIId_Geht = taetigkeitFindByCNr(
					ZeiterfassungFac.TAETIGKEIT_GEHT, theClientDto).getIId();

			if (zeitdatenDto.getTaetigkeitIId() != null
					&& (zeitdatenDto.getTaetigkeitIId().equals(
							tetigkeitIId_Kommt) || zeitdatenDto
							.getTaetigkeitIId().equals(tetigkeitIId_Geht))) {
				zeitdatenDto.settZeit_Bis(null);
				zeitdatenDto.setZeitdatenIId_BisZeit(null);
			}

			// Wenn von-bis erfassung, dann darf zwischen von-bis keine
			// Zeitbuchung
			// vorhanden sein

			if (bVonBisErfassung && zeitdatenDto.gettZeit_Bis() != null
					&& zeitdatenDto.getZeitdatenIId_BisZeit() != null) {
				ZeitdatenDto[] dtos = getZeiterfassungFac()
						.zeitdatenFindZeitdatenEinesTagesUndEinerPerson(
								zeitdatenDto.getPersonalIId(),
								zeitdatenDto.getTZeit(),
								new Timestamp(zeitdatenDto.gettZeit_Bis()
										.getTime()));
				if (dtos.length > 0) {

					for (int i = 0; i < dtos.length; i++) {
						ZeitdatenDto zd = dtos[i];

						if (zd.getIId().equals(zeitdatenDto.getIId())
								|| zd.getIId().equals(
										zeitdatenDto.getZeitdatenIId_BisZeit())) {

						} else {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_BUCHUNG_ZWISCHEN_VON_BIS,
									new Exception(
											"EJBExceptionLP.FEHLER_BUCHUNG_ZWISCHEN_VON_BIS"));
						}

					}

				}
			}

			if (bVonBisErfassung == true && zeitdatenDto.gettZeit_Bis() == null) {
				boolean b = binIchZwischenEinerVonBisBuchung(
						zeitdatenDto.getPersonalIId(), zeitdatenDto.getTZeit(),
						theClientDto);
				if (b == true) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BUCHUNG_EINFUEGEN_ZWISCHEN_VON_BIS_NICHT_ERLAUBT,
							new Exception(
									"EJBExceptionLP.FEHLER_BUCHUNG_EINFUEGEN_ZWISCHEN_VON_BIS_NICHT_ERLAUBT"));
				}

			}

		} catch (RemoteException ex5) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
		}

		if (zeitdatenDto.getBAutomatikbuchung() == null) {
			zeitdatenDto.setBAutomatikbuchung(Helper.boolean2Short(false));
		}

		if (zeitdatenDto.getTaetigkeitIId() != null) {
			zeitdatenDto.setCBelegartnr(null);
			zeitdatenDto.setArtikelIId(null);
			zeitdatenDto.setIBelegartid(null);
			zeitdatenDto.setIBelegartpositionid(null);
		}

		// SP3289
		bringeFehlerWennZeitabschlussvorhanden(zeitdatenDto.getPersonalIId(),
				zeitdatenDto.getTZeit(), theClientDto);

		vergleicheZeitdatenDtoVorherNachherUndLoggeAenderungen(zeitdatenDto,
				theClientDto);

		Integer iId = zeitdatenDto.getIId();
		// try {
		Zeitdaten zeitdaten = em.find(Zeitdaten.class, zeitdatenDto.getIId());
		if (zeitdaten == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}

		try {
			Query query = em
					.createNamedQuery("ZeitdatenfindByPersonalIIdTZeit");
			query.setParameter(1, zeitdatenDto.getPersonalIId());
			query.setParameter(2, zeitdatenDto.getTZeit());
			Zeitdaten zeitdaten2 = (Zeitdaten) query.getSingleResult();
			Integer iIdVorhanden = zeitdaten2.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_ZEITDATEN.UK"));
			}

		} catch (NoResultException ex) {
			//
		}

		zeitdatenDto.setTAendern(new java.sql.Timestamp(System
				.currentTimeMillis()));
		// Aus Automatikbuchungen wir bei einem Update IMMER eine 'Handbuchung'
		zeitdatenDto.setBAutomatikbuchung(Helper.boolean2Short(false));

		// Bei Zeitdaten Sekunden abschneiden und dann vergleichen
		Calendar c1 = Calendar.getInstance();
		c1.setTimeInMillis(zeitdaten.getTZeit().getTime());
		c1.set(Calendar.SECOND, 0);

		Calendar c2 = Calendar.getInstance();
		c2.setTimeInMillis(zeitdatenDto.getTZeit().getTime());
		c2.set(Calendar.SECOND, 0);

		if (c1.equals(c2)) {
			zeitdatenDto.setTAendern(zeitdaten.getTAendern());
			if (zeitdaten.getTaetigkeitIId() != null
					&& !zeitdaten.getTaetigkeitIId().equals(
							zeitdatenDto.getTaetigkeitIId())) {
				zeitdatenDto
						.setBTaetigkeitgeaendert(Helper.boolean2Short(true));

			} else if (zeitdaten.getArtikelIId() != null
					&& !zeitdaten.getArtikelIId().equals(
							zeitdatenDto.getArtikelIId())) {
				zeitdatenDto
						.setBTaetigkeitgeaendert(Helper.boolean2Short(true));

			} else if (zeitdaten.getTaetigkeitIId() == null
					&& zeitdatenDto.getTaetigkeitIId() != null) {
				zeitdatenDto
						.setBTaetigkeitgeaendert(Helper.boolean2Short(true));
			} else if (zeitdaten.getArtikelIId() == null
					&& zeitdatenDto.getArtikelIId() != null) {
				zeitdatenDto
						.setBTaetigkeitgeaendert(Helper.boolean2Short(true));
			}
		}

		zeitdatenDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		setZeitdatenFromZeitdatenDto(zeitdaten, zeitdatenDto);

		// Losablieferungen auf "Neu Berechnen" setzen
		if (zeitdatenDto.getCBelegartnr() != null
				&& zeitdatenDto.getCBelegartnr().equals(LocaleFac.BELEGART_LOS)) {
			try {
				if (zeitdatenDto.getIBelegartid() != null) {
					getFertigungFac().setzeLosablieferungenAufNeuBerechnen(
							zeitdatenDto.getIBelegartid(), theClientDto);
				}
			} catch (RemoteException ex2) {
				throwEJBExceptionLPRespectOld(ex2);
			}
		}

		// PJ18356

		if (zeitdatenDto.getZeitdatenIId_BisZeit() != null
				&& zeitdatenDto.gettZeit_Bis() != null) {
			Zeitdaten zdBis = em.find(Zeitdaten.class,
					zeitdatenDto.getZeitdatenIId_BisZeit());
			zdBis.setTZeit(zeitdatenDto.gettZeit_Bis());
		}

	}

	public ZeitdatenDto zeitdatenFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Zeitdaten zeitdaten = em.find(Zeitdaten.class, iId);
		if (zeitdaten == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}
		ZeitdatenDto zDto = assembleZeitdatenDto(zeitdaten);

		ParametermandantDto parameter = null;
		try {
			parameter = (ParametermandantDto) getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_PERSONAL,
							ParameterFac.PARAMETER_VON_BIS_ERFASSUNG);
		} catch (RemoteException ex5) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
		}

		boolean bVonBisErfassung = ((Boolean) parameter.getCWertAsObject());
		if (bVonBisErfassung == true) {

			ZeitdatenDto zDtoEnde = getZeiterfassungFac()
					.getZugehoerigeEndeBuchung(zDto, theClientDto);
			if (zDtoEnde != null) {
				zDto.setZeitdatenIId_BisZeit(zDtoEnde.getIId());
				zDto.settZeit_Bis(zDtoEnde.getTZeit());
			}

		}

		return zDto;

	}

	public ZeitdatenDto zeitdatenFindByPrimaryKeyOhneExc(Integer id) {
		if (id == null)
			return null;
		Zeitdaten zeitdaten = em.find(Zeitdaten.class, id);
		return zeitdaten == null ? null : assembleZeitdatenDto(zeitdaten);
	}

	public ZeitdatenDto zeitdatenFindByPersonalIIdTZeit(Integer personalIId,
			Timestamp tZeit) throws EJBExceptionLP {
		if (personalIId == null || tZeit == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("personalIId == null || tZeit == null"));
		}
		// try {
		Query query = em.createNamedQuery("ZeitdatenfindByPersonalIIdTZeit");
		query.setParameter(1, personalIId);
		query.setParameter(2, tZeit);
		Zeitdaten zeitdaten = (Zeitdaten) query.getSingleResult();
		if (zeitdaten == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleZeitdatenDto(zeitdaten);
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public ZeitdatenDto[] zeitdatenFindZeitdatenEinesTagesUndEinerPerson(
			Integer personalIId, Timestamp tVon, Timestamp tBis)
			throws EJBExceptionLP {
		if (personalIId == null || tVon == null || tBis == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"personalIId == null || tVon == null || tBis == null"));
		}
		// try {
		Query query = em
				.createNamedQuery("ZeitdatenfindZeitdatenEinesTagesUndEinerPerson");
		query.setParameter(1, personalIId);
		query.setParameter(2, tVon);
		query.setParameter(3, tBis);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		return assembleZeitdatenDtos(cl);
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);

		// }
	}

	// @ToDo warum gibt es die gleiche unter anderem Namen!
	public ZeitdatenDto[] zeitdatenFindZeitdatenEinesTagesUndEinerPersonOnheBelegzeiten(
			Integer personalIId, Timestamp tVon, Timestamp tBis)
			throws EJBExceptionLP {
		if (personalIId == null || tVon == null || tBis == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"personalIId == null || tVon == null || tBis == null"));
		}
		// try {
		Query query = em
				.createNamedQuery("ZeitdatenfindZeitdatenEinesTagesUndEinerPerson");
		query.setParameter(1, personalIId);
		query.setParameter(2, tVon);
		query.setParameter(3, tBis);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		return assembleZeitdatenDtos(cl);
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);

		// }
	}

	private void setZeitdatenFromZeitdatenDto(Zeitdaten zeitdaten,
			ZeitdatenDto zeitdatenDto) {
		zeitdaten.setPersonalIId(zeitdatenDto.getPersonalIId());
		zeitdaten.setTZeit(zeitdatenDto.getTZeit());
		zeitdaten.setCBelegartnr(zeitdatenDto.getCBelegartnr());
		zeitdaten.setIBelegartpositionid(zeitdatenDto.getIBelegartpositionid());
		zeitdaten.setIBelegartid(zeitdatenDto.getIBelegartid());
		zeitdaten.setCBemerkungzubelegart(zeitdatenDto
				.getCBemerkungZuBelegart());
		zeitdaten.setTaetigkeitIId(zeitdatenDto.getTaetigkeitIId());
		zeitdaten.setArtikelIId(zeitdatenDto.getArtikelIId());
		zeitdaten.setBTaetigkeitgeaendert(zeitdatenDto
				.getBTaetigkeitgeaendert());
		zeitdaten.setBAutomatikbuchung(zeitdatenDto.getBAutomatikbuchung());
		zeitdaten.setPersonalIIdAendern(zeitdatenDto.getPersonalIIdAendern());
		zeitdaten.setTAendern(zeitdatenDto.getTAendern());
		zeitdaten.setXKommentar(zeitdatenDto.getXKommentar());
		zeitdaten.setCWowurdegebucht(zeitdatenDto.getCWowurdegebucht());
		em.merge(zeitdaten);
		em.flush();
	}

	private ZeitdatenDto assembleZeitdatenDto(Zeitdaten zeitdaten) {
		return ZeitdatenDtoAssembler.createDto(zeitdaten);
	}

	private ZeitdatenDto[] assembleZeitdatenDtos(Collection<?> zeitdatens) {
		List<ZeitdatenDto> list = new ArrayList<ZeitdatenDto>();
		if (zeitdatens != null) {
			Iterator<?> iterator = zeitdatens.iterator();
			while (iterator.hasNext()) {
				Zeitdaten zeitdaten = (Zeitdaten) iterator.next();
				list.add(assembleZeitdatenDto(zeitdaten));
			}
		}
		ZeitdatenDto[] returnArray = new ZeitdatenDto[list.size()];
		return (ZeitdatenDto[]) list.toArray(returnArray);
	}

	public ZeitdatenDto[] assembleZeitdatenDtosOhneBelegzeiten(
			Collection<?> zeitdatens) {
		List<ZeitdatenDto> list = new ArrayList<ZeitdatenDto>();

		Integer taetigkeitIId_Ende = null;

		try {
			Query query = em.createNamedQuery("TaetigkeitfindByCNr");
			query.setParameter(1, ZeiterfassungFac.TAETIGKEIT_ENDE);
			Taetigkeit taetigkeit = (Taetigkeit) query.getSingleResult();
			taetigkeitIId_Ende = taetigkeit.getIId();

		} catch (NoResultException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		}

		if (zeitdatens != null) {
			Iterator<?> iterator = zeitdatens.iterator();
			while (iterator.hasNext()) {
				Zeitdaten zeitdaten = (Zeitdaten) iterator.next();
				if (zeitdaten.getArtikelIId() == null) {
					if (!taetigkeitIId_Ende
							.equals(zeitdaten.getTaetigkeitIId())) {

						list.add(assembleZeitdatenDto(zeitdaten));

					}
				}
			}
		}
		ZeitdatenDto[] returnArray = new ZeitdatenDto[list.size()];
		return (ZeitdatenDto[]) list.toArray(returnArray);
	}

	private ZeitdatenDto[] assembleZeitdatenDtosOhneEnde(
			Collection<?> zeitdatens) {
		List<ZeitdatenDto> list = new ArrayList<ZeitdatenDto>();

		Integer taetigkeitIId_Ende = null;

		try {
			Query query = em.createNamedQuery("TaetigkeitfindByCNr");
			query.setParameter(1, ZeiterfassungFac.TAETIGKEIT_ENDE);
			Taetigkeit taetigkeit = (Taetigkeit) query.getSingleResult();
			taetigkeitIId_Ende = taetigkeit.getIId();

		} catch (NoResultException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		}

		if (zeitdatens != null) {
			Iterator<?> iterator = zeitdatens.iterator();
			while (iterator.hasNext()) {
				Zeitdaten zeitdaten = (Zeitdaten) iterator.next();

				if (!taetigkeitIId_Ende.equals(zeitdaten.getTaetigkeitIId())) {

					list.add(assembleZeitdatenDto(zeitdaten));

				}

			}
		}
		ZeitdatenDto[] returnArray = new ZeitdatenDto[list.size()];
		return (ZeitdatenDto[]) list.toArray(returnArray);
	}

	private ZeitdatenDto[] assembleZeitdatenOhneArbeitsUndMaschienzeitenDtos(
			Collection<?> zeitdatens) {
		List<ZeitdatenDto> list = new ArrayList<ZeitdatenDto>();
		if (zeitdatens != null) {
			Iterator<?> iterator = zeitdatens.iterator();
			while (iterator.hasNext()) {
				Zeitdaten zeitdaten = (Zeitdaten) iterator.next();
				if (zeitdaten.getTaetigkeitIId() != null) {
					list.add(assembleZeitdatenDto(zeitdaten));
				}
			}
		}
		ZeitdatenDto[] returnArray = new ZeitdatenDto[list.size()];
		return (ZeitdatenDto[]) list.toArray(returnArray);
	}

	public Timestamp pruefeObAmLetztenBuchungstagKommtUndGehtGebuchtWurde(
			Integer personalIId, TheClientDto theClientDto) {

		Date tKommt = null;
		Date tGeht = null;

		Timestamp tTag = null;

		TaetigkeitDto taetigkeitDto_Kommt = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_KOMMT, theClientDto);
		TaetigkeitDto taetigkeitDto_Geht = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_GEHT, theClientDto);

		// Meldung ob GEHT oder KOMMT ind den letzten Tagen fehlt
		// Hole letzte Buchung vor Heute
		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria liste = session
				.createCriteria(FLRZeitdaten.class);
		liste.add(Expression.eq(ZeiterfassungFac.FLR_ZEITDATEN_PERSONAL_I_ID,
				personalIId));

		liste.addOrder(Order.desc(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT));
		liste.setMaxResults(1);
		List<?> letzerAuftrag = liste.list();

		Iterator<?> it = letzerAuftrag.iterator();

		if (it.hasNext()) {
			FLRZeitdaten flrLetzteBuchung = (FLRZeitdaten) it.next();
			tTag = new Timestamp(flrLetzteBuchung.getT_zeit().getTime());

			// Wenn heute, dann nichts

			Timestamp tHeute = Helper.cutTimestamp(new Timestamp(System
					.currentTimeMillis()));

			if (tHeute.equals(Helper.cutTimestamp(tTag))) {
				return null;
			}

			if (flrLetzteBuchung.getTaetigkeit_i_id() != null
					&& flrLetzteBuchung.getTaetigkeit_i_id().equals(
							taetigkeitDto_Geht.getIId())) {
				tGeht = flrLetzteBuchung.getT_zeit();
			}

			String sQuery = "select zeitdaten FROM FLRZeitdaten zeitdaten WHERE zeitdaten.t_zeit<'"
					+ Helper.formatTimestampWithSlashes(new Timestamp(
							flrLetzteBuchung.getT_zeit().getTime()))
					+ "' AND zeitdaten.personal_i_id="
					+ personalIId
					+ " AND (zeitdaten.taetigkeit_i_id="
					+ taetigkeitDto_Kommt.getIId()
					+ " OR zeitdaten.taetigkeit_i_id="
					+ taetigkeitDto_Geht.getIId()
					+ ") ORDER BY zeitdaten.t_zeit DESC";

			Session session2 = FLRSessionFactory.getFactory().openSession();

			org.hibernate.Query letzteKommtGeht = session2.createQuery(sQuery);
			letzteKommtGeht.setMaxResults(1);

			List<?> resultList = letzteKommtGeht.list();

			Iterator<?> resultListIterator = resultList.iterator();

			if (resultListIterator.hasNext()) {
				FLRZeitdaten l = (FLRZeitdaten) resultListIterator.next();
				// Wenn zuletzt ein Kommt gebucht wurde, dann OK

				if (l.getTaetigkeit_i_id().equals(taetigkeitDto_Kommt.getIId())) {
					tKommt = l.getT_zeit();

				}
			}

		}

		if (tKommt == null || tGeht == null) {
			return tTag;
		}

		return null;
	}

	public Integer createTaetigkeit(TaetigkeitDto taetigkeitDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (taetigkeitDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("taetigkeitDto == null"));
		}
		if (taetigkeitDto.getCNr() == null
				|| taetigkeitDto.getTaetigkeitartCNr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"taetigkeitDto.getCNr() == null || taetigkeitDto.getTaetigkeitartCNr() == null"));
		}
		try {
			Query query = em.createNamedQuery("TaetigkeitfindByCNr");
			query.setParameter(1, taetigkeitDto.getCNr());
			Taetigkeit doppelt = (Taetigkeit) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_TAETIGKEIT.C_NR"));
		} catch (NoResultException ex1) {
			// nothing here
		}
		if (taetigkeitDto.getCNr().equals(ZeiterfassungFac.TAETIGKEIT_REISE)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(
							"Taetigkeit REISE is Reserviert und darf nicht angelegt werden"));

		}

		try {

			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_TAETIGKEIT);
			taetigkeitDto.setIId(pk);
			taetigkeitDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			taetigkeitDto.setTAendern(new java.sql.Timestamp(System
					.currentTimeMillis()));

			taetigkeitDto.setISort(new Integer(0));

			Taetigkeit taetigkeit = new Taetigkeit(taetigkeitDto.getIId(),
					taetigkeitDto.getCNr(),
					taetigkeitDto.getTaetigkeitartCNr(),
					taetigkeitDto.getISort(),
					taetigkeitDto.getPersonalIIdAendern());
			em.persist(taetigkeit);
			em.flush();

			if (taetigkeitDto.getBBdebuchbar() == null) {
				taetigkeitDto.setBBdebuchbar(taetigkeit.getBBdebuchbar());
			}
			if (taetigkeitDto.getFBezahlt() == null) {
				taetigkeitDto.setFBezahlt(taetigkeit.getFBezahlt());
			}
			if (taetigkeitDto.getBFeiertag() == null) {
				taetigkeitDto.setBFeiertag(taetigkeit.getBFeiertag());
			}

			if (taetigkeitDto.getBTagbuchbar() == null) {
				taetigkeitDto.setBTagbuchbar(taetigkeit.getBTagbuchbar());
			}
			if (taetigkeitDto.getBVersteckt() == null) {
				taetigkeitDto.setBVersteckt(taetigkeit.getbVersteckt());
			}
			setTaetigkeitFromTaetigkeitDto(taetigkeit, taetigkeitDto);
			if (taetigkeitDto.getTaetigkeitsprDto() != null) {
				String sprache = theClientDto.getLocUiAsString();
				Taetigkeitspr taetigkeitspr = new Taetigkeitspr(
						taetigkeitDto.getIId(), sprache);
				em.persist(taetigkeitspr);
				em.flush();
				setTaetigkeitsprFromTaetigkeitsprDto(taetigkeitspr,
						taetigkeitDto.getTaetigkeitsprDto());
			}
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return taetigkeitDto.getIId();
	}

	public Integer createMaschinenzeitdaten(
			MaschinenzeitdatenDto maschinenzeitdatenDto,
			TheClientDto theClientDto) {

		try {

			// Wenn bei der vorherigen Buchung das bis leer ist, wird das mit
			// dem jetzigen von beendet.

			Query query = em
					.createNamedQuery("MaschinenzeitdatenfindLetzeOffeneMaschinenzeitdaten");
			query.setParameter(1, maschinenzeitdatenDto.getMaschineIId());
			Collection<?> cl = query.getResultList();

			if (cl.size() > 0) {
				Maschinenzeitdaten m = (Maschinenzeitdaten) cl.iterator()
						.next();

				m.setTBis(maschinenzeitdatenDto.getTVon());
				em.merge(m);
				em.flush();

			}

			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_MASCHINENZEITDATEN);
			maschinenzeitdatenDto.setIId(pk);
			maschinenzeitdatenDto.setPersonalIIdAendern(theClientDto
					.getIDPersonal());
			maschinenzeitdatenDto.setTAendern(new java.sql.Timestamp(System
					.currentTimeMillis()));
			maschinenzeitdatenDto.setPersonalIIdAnlegen(theClientDto
					.getIDPersonal());
			maschinenzeitdatenDto.setTAnlegen(new java.sql.Timestamp(System
					.currentTimeMillis()));

			doCreateMaschinenzeitdaten(maschinenzeitdatenDto);

			Lossollarbeitsplan lsa = em.find(Lossollarbeitsplan.class,
					maschinenzeitdatenDto.getLossollarbeitsplanIId());

			try {
				getFertigungFac().setzeLosablieferungenAufNeuBerechnen(
						lsa.getLosIId(), theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			if (lsa.getMaschineIId() == null) {
				// keine Maschin im Arbeitgang definiert, daher kommt die
				// Maschine aus der createMaschinenzeit
				Query query1 = em
						.createNamedQuery("LossollarbeitsplanfindByLosIIdIArbeitsgangnummer");
				query1.setParameter(1, lsa.getLosIId());
				query1.setParameter(2, lsa.getIArbeitsgangnummer());
				Collection<Lossollarbeitsplan> lsaList = query1.getResultList();
				Iterator<Lossollarbeitsplan> it = lsaList.iterator();
				while (it.hasNext()) {
					if (it.next().getIId().equals(lsa.getIId()))
						// eigenen Arbeitsgang suchen
						if (it.hasNext()) {
							Lossollarbeitsplan lsa1 = it.next();
							// naechste Laufzeit des Arbeitsgangs suchen (muss
							// folgender sein!)
							if (lsa1.getAgartCNr() != null
									&& lsa1.getAgartCNr().equals(
											StuecklisteFac.AGART_LAUFZEIT)
									&& lsa1.getMaschineIId() == null) {
								// Maschinenzeit buchen
								letzteMaschinenzeitBuchungBeenden(
										maschinenzeitdatenDto.getMaschineIId(),
										maschinenzeitdatenDto.getTVon());
								maschinenzeitdatenDto
										.setIId(pkGen
												.getNextPrimaryKey(PKConst.PK_MASCHINENZEITDATEN));
								maschinenzeitdatenDto
										.setLossollarbeitsplanIId(lsa1.getIId());
								maschinenzeitdatenDto.setTVon(new Timestamp(
										maschinenzeitdatenDto.getTVon()
												.getTime() + 200));
								doCreateMaschinenzeitdaten(maschinenzeitdatenDto);
							}
							// wurde keiner gefunden oder ist der folgende keine
							// Laufzeit wird keine zusaetzliche Maschinenzeit
							// gebucht!
							break;
						}
				}
			} else {
				LossollarbeitsplanDto[] zusDtos = getFertigungFac()
						.getAlleZusatzlichZuBuchuchendenArbeitsgaenge(
								maschinenzeitdatenDto
										.getLossollarbeitsplanIId(),
								theClientDto);

				for (int i = 0; i < zusDtos.length; i++) {
					if (zusDtos[i].getMaschineIId() != null) {
						// Wenn bei der vorherigen Buchung das bis leer ist,
						// wird
						// das mit
						// dem jetzigen von beendet.
						letzteMaschinenzeitBuchungBeenden(
								maschinenzeitdatenDto.getMaschineIId(),
								maschinenzeitdatenDto.getTVon());

						pk = pkGen
								.getNextPrimaryKey(PKConst.PK_MASCHINENZEITDATEN);
						maschinenzeitdatenDto.setIId(pk);

						maschinenzeitdatenDto
								.setLossollarbeitsplanIId(zusDtos[i].getIId());
						maschinenzeitdatenDto
								.setTVon(new Timestamp(maschinenzeitdatenDto
										.getTVon().getTime() + 200));
						doCreateMaschinenzeitdaten(maschinenzeitdatenDto);
					}

				}
			}
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return maschinenzeitdatenDto.getIId();
	}

	private void doCreateMaschinenzeitdaten(
			MaschinenzeitdatenDto maschinenzeitdatenDto) {
		Maschinenzeitdaten taetigkeit = new Maschinenzeitdaten(
				maschinenzeitdatenDto.getIId(),
				maschinenzeitdatenDto.getMaschineIId(),
				maschinenzeitdatenDto.getTVon(),
				maschinenzeitdatenDto.getPersonalIIdAnlegen(),
				maschinenzeitdatenDto.getPersonalIIdAendern(),
				maschinenzeitdatenDto.getLossollarbeitsplanIId(),
				maschinenzeitdatenDto.getTAendern(),
				maschinenzeitdatenDto.getTAnlegen(),
				maschinenzeitdatenDto.getPersonalIIdGestartet());
		em.persist(taetigkeit);
		em.flush();

		setMaschinenzeitdatenFromMaschinenzeitdatenDto(taetigkeit,
				maschinenzeitdatenDto);
	}

	public void importiereSonderzeiten(java.sql.Date tLoescheVorhandenevon,
			java.sql.Date tLoescheVorhandenebis,
			HashMap<Integer, ArrayList<SonderzeitenImportDto>> daten,
			TheClientDto theClientDto) {

		Iterator<Integer> it = daten.keySet().iterator();
		while (it.hasNext()) {

			Integer personalIId = it.next();

			HashMap hm = taetigkeitenMitImportkennzeichen();

			Iterator itTaetigkeiten = hm.keySet().iterator();

			while (itTaetigkeiten.hasNext()) {
				Integer taetigkeitIId = (Integer) hm.get(itTaetigkeiten.next());
				// Zuerst Sonderzeiten im Zeitraum loechen
				Session session = FLRSessionFactory.getFactory().openSession();

				// Mit Fr. Krautenbacher besprochen:
				// Es werden ALLE Sonderzeiten in dem Zeitraom geloescht, da es
				// den Fall gab, das bei einer Person eine Stundenweise
				// Sonderzeit manuell
				// hinterlegt wurde und somit der UK verletzt wurde

				String hqlDelete = "delete FROM FLRSonderzeiten z WHERE z.taetigkeit_i_id="
						+ taetigkeitIId
						+ " AND  z.personal_i_id="
						+ personalIId
						+ " AND z.t_datum>=' "
						+ Helper.formatDateWithSlashes(tLoescheVorhandenevon)
						+ "' AND z.t_datum<' "
						+ Helper.formatDateWithSlashes(tLoescheVorhandenebis)
						+ "'";
				session.createQuery(hqlDelete).executeUpdate();

				session.close();
			}

			// Danach neu anlegen
			ArrayList<SonderzeitenImportDto> list = daten.get(personalIId);

			for (int i = 0; i < list.size(); i++) {
				SonderzeitenImportDto szDto = list.get(i);
				SonderzeitenDto sonderzeitenDto = new SonderzeitenDto();
				sonderzeitenDto.setBTag(Helper.boolean2Short(true));
				sonderzeitenDto.setBHalbtag(Helper.boolean2Short(false));
				sonderzeitenDto.setPersonalIId(personalIId);
				sonderzeitenDto.setTaetigkeitIId(szDto.getTaetigkeitIId());
				sonderzeitenDto.setTDatum(szDto.gettDatum());
				try {
					createSonderzeiten(sonderzeitenDto, theClientDto);
				} catch (EJBExceptionLP e) {
					if (e.getCode() == EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE) {
						ArrayList alDaten = new ArrayList();
						try {
							PersonalDto pDto = getPersonalFac()
									.personalFindByPrimaryKey(
											sonderzeitenDto.getPersonalIId(),
											theClientDto);

							TaetigkeitDto tDto = getZeiterfassungFac()
									.taetigkeitFindByPrimaryKey(
											sonderzeitenDto.getTaetigkeitIId(),
											theClientDto);

							alDaten.add("Pers.Nr.:"
									+ pDto.getCPersonalnr()
									+ ", Datum: "
									+ Helper.formatDatum(
											sonderzeitenDto.getTDatum(),
											theClientDto.getLocUi()) + " ("
									+ tDto.getCNr() + ")");
						} catch (RemoteException e1) {
							throwEJBExceptionLPRespectOld(e1);
						}

						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_SONDERZEITENIMPORT_DOPPELTER_EINTRAG,
								alDaten,
								new Exception(
										"FEHLER_SONDERZEITENIMPORT_DOPPELTER_EINTRAG"));

					} else {
						throw e;
					}
				}
			}

		}

	}

	private void letzteMaschinenzeitBuchungBeenden(Integer maschineIId,
			Timestamp t) {
		Query query;
		Collection<?> cl;

		query = em
				.createNamedQuery("MaschinenzeitdatenfindLetzeOffeneMaschinenzeitdaten");
		query.setParameter(1, maschineIId);
		cl = query.getResultList();

		if (cl.size() > 0) {
			Maschinenzeitdaten m = (Maschinenzeitdaten) cl.iterator().next();
			m.setTBis(t);
			em.merge(m);
			em.flush();
		}
	}

	public ZeitverteilungDto[] zeitverteilungFindByPersonalIId(
			Integer personalIId) {
		Query query = em.createNamedQuery("ZeitverteilungfindByPersonalIId");
		query.setParameter(1, personalIId);
		Collection<?> cl = query.getResultList();
		return assembleZeitverteilungDtos(cl);
	}

	public ZeitverteilungDto[] zeitverteilungFindByPersonalIIdUndTag(
			Integer personalIId, java.sql.Timestamp tTag) {
		Query query = em
				.createNamedQuery("ZeitverteilungfindByPersonalIIdTZeitVonTZeitBis");
		query.setParameter(1, personalIId);

		// Von
		tTag = Helper.cutTimestamp(tTag);
		query.setParameter(2, tTag);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tTag.getTime());
		c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);
		// Bis
		query.setParameter(3, new Timestamp(c.getTimeInMillis()));
		Collection<?> cl = query.getResultList();
		return assembleZeitverteilungDtos(cl);
	}

	public ZeitverteilungDto[] zeitverteilungFindByPersonalIIdLosIIdUndTag(
			Integer personalIId, Integer losIId, java.sql.Timestamp tTag) {
		Query query = em
				.createNamedQuery("ZeitverteilungfindByPersonalIIdLosIIdTZeitVonTZeitBis");
		query.setParameter(1, personalIId);
		query.setParameter(2, losIId);

		// Von
		tTag = Helper.cutTimestamp(tTag);
		query.setParameter(3, tTag);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tTag.getTime());
		c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);
		// Bis
		query.setParameter(4, new Timestamp(c.getTimeInMillis()));
		Collection<?> cl = query.getResultList();
		return assembleZeitverteilungDtos(cl);
	}

	public void removeZeitverteilungByPersonalIIdUndTag(Integer personalIId,
			java.sql.Timestamp tTag) {

		ZeitverteilungDto[] dtos = zeitverteilungFindByPersonalIIdUndTag(
				personalIId, tTag);

		for (int i = 0; i < dtos.length; i++) {
			Zeitverteilung zeitverteilung = em.find(Zeitverteilung.class,
					dtos[i].getIId());
			em.remove(zeitverteilung);
			em.flush();
		}
	}

	public void wandleUrlaubsantragInUrlaubUm(Integer[] sonderzeitenIIds,
			TheClientDto theClientDto) {
		Integer urlaubsantragIId = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_URLAUBSANTRAG, theClientDto)
				.getIId();
		Integer urlaubIId = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_URLAUB, theClientDto).getIId();
		for (int i = 0; i < sonderzeitenIIds.length; i++) {
			Sonderzeiten sonderzeiten = em.find(Sonderzeiten.class,
					sonderzeitenIIds[i]);
			if (sonderzeiten.getTaetigkeitIId().equals(urlaubsantragIId)) {
				sonderzeiten.setTaetigkeitIId(urlaubIId);
				em.flush();
			}
		}
	}

	public Integer createZeitverteilung(ZeitverteilungDto zeitverteilungDto,
			TheClientDto theClientDto) {
		if (zeitverteilungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zeitverteilungDto == null"));
		}
		if (zeitverteilungDto.getPersonalIId() == null
				|| zeitverteilungDto.getLosIId() == null
				|| zeitverteilungDto.getTZeit() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"taetigkeitDto.getPersonalIId() == null || taetigkeitDto.getLosIId() == null || taetigkeitDto.getTZeit() == null"));
		}
		try {
			ZeitverteilungDto[] zvDtos = zeitverteilungFindByPersonalIIdLosIIdUndTag(
					zeitverteilungDto.getPersonalIId(),
					zeitverteilungDto.getLosIId(), zeitverteilungDto.getTZeit());
			if (zvDtos.length > 0) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_ZEITVERTEILUNG.UK"));
			}
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {

			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ZEITVERTEILUNG);
			zeitverteilungDto.setIId(pk);

			Zeitverteilung zeitverteilung = new Zeitverteilung(
					zeitverteilungDto.getIId(),
					zeitverteilungDto.getPersonalIId(),
					zeitverteilungDto.getTZeit(), zeitverteilungDto.getLosIId());
			em.persist(zeitverteilung);
			em.flush();

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

		if (zeitverteilungDto.getArtikelIId() != null) {
			// Abschlussbuchung, jetzt wird ueberall die Taetigkeit hinterlegt
			Query query = em
					.createNamedQuery("ZeitverteilungfindByPersonalIId");
			query.setParameter(1, zeitverteilungDto.getPersonalIId());
			Collection<?> cl = query.getResultList();

			Iterator<?> iterator = cl.iterator();
			while (iterator.hasNext()) {
				Zeitverteilung zeitverteilung = (Zeitverteilung) iterator
						.next();

				zeitverteilung.setArtikelIId(zeitverteilungDto.getArtikelIId());
				em.merge(zeitverteilung);
				em.flush();
			}

		}

		return zeitverteilungDto.getIId();
	}

	public void pflegeUmstellungAufVonBisErfassung(TheClientDto theClientDto) {

		HashMap<Integer, TreeMap<java.util.Date, TreeMap<java.sql.Timestamp, FLRZeitdaten>>> hmPersonal = new HashMap<Integer, TreeMap<java.util.Date, TreeMap<java.sql.Timestamp, FLRZeitdaten>>>();

		String sQuery = "SELECT z FROM FLRZeitdaten z WHERE z.c_belegartnr IS NOT NULL AND z.i_belegartid IS NOT NULL ORDER BY z.personal_i_id, z.t_zeit";

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		org.hibernate.Query inventurliste = session.createQuery(sQuery);

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			FLRZeitdaten zeitdaten = (FLRZeitdaten) resultListIterator.next();

			TreeMap<java.util.Date, TreeMap<java.sql.Timestamp, FLRZeitdaten>> tmDaten = null;

			if (hmPersonal.containsKey(zeitdaten.getPersonal_i_id())) {
				tmDaten = hmPersonal.get(zeitdaten.getPersonal_i_id());
			} else {
				tmDaten = new TreeMap<java.util.Date, TreeMap<java.sql.Timestamp, FLRZeitdaten>>();
			}

			java.util.Date dTag = Helper.cutDate(zeitdaten.getT_zeit());

			TreeMap<java.sql.Timestamp, FLRZeitdaten> tmTage = null;
			if (tmDaten.containsKey(dTag)) {
				tmTage = tmDaten.get(dTag);
			} else {
				tmTage = new TreeMap<java.sql.Timestamp, FLRZeitdaten>();
			}

			tmTage.put(new java.sql.Timestamp(zeitdaten.getT_zeit().getTime()),
					zeitdaten);

			tmDaten.put(dTag, tmTage);
			hmPersonal.put(zeitdaten.getPersonal_i_id(), tmDaten);

		}

		// Daten sind nun nach Personal und Tagen getrennt

		// Auftraege muessen durch ENDE als bis abgeschlossen werden

		Integer taetigkeitIId_Ende = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_ENDE, theClientDto).getIId();

		Iterator<Integer> itPersonen = hmPersonal.keySet().iterator();
		while (itPersonen.hasNext()) {
			Integer personalIId = itPersonen.next();

			TreeMap<java.util.Date, TreeMap<java.sql.Timestamp, FLRZeitdaten>> tmDaten = hmPersonal
					.get(personalIId);

			Iterator<java.util.Date> itDatum = tmDaten.keySet().iterator();

			while (itDatum.hasNext()) {
				java.util.Date datum = itDatum.next();

				TreeMap<java.sql.Timestamp, FLRZeitdaten> tmTagesdaten = tmDaten
						.get(datum);

				Iterator<java.sql.Timestamp> itTagesdaten = tmTagesdaten
						.keySet().iterator();

				while (itTagesdaten.hasNext()) {

					Timestamp t = itTagesdaten.next();

					FLRZeitdaten zeiteintrag = tmTagesdaten.get(t);

					// Nun die naechste Zeitbuchung suchen

					// Wenn dies kein ende ist, dann nachtragen

					Timestamp tMorgen = Helper.cutTimestamp(new Timestamp(t
							.getTime() + 24 * 3600000));

					String sQuery2 = "SELECT z FROM FLRZeitdaten z WHERE z.personal_i_id="
							+ personalIId
							+ " AND z.t_zeit>'"
							+ Helper.formatTimestampWithSlashes(t)
							+ "' AND z.t_zeit <'"
							+ Helper.formatTimestampWithSlashes(tMorgen)
							+ "' ORDER BY z.t_zeit ASC";

					SessionFactory factory2 = FLRSessionFactory.getFactory();
					Session session2 = factory2.openSession();

					org.hibernate.Query naechsteZeitbuchung = session2
							.createQuery(sQuery2);
					naechsteZeitbuchung.setMaxResults(1);

					List<?> resultList2 = naechsteZeitbuchung.list();

					Iterator<?> resultListIterator2 = resultList2.iterator();

					// wenn die naechste Buchung kein Ende ist, bzw keine
					// Buchung vorhanden ist, dann kurz davor ein Ende
					// nachtragen

					java.sql.Timestamp tEnde = null;

					if (resultListIterator2.hasNext()) {
						FLRZeitdaten zeitdaten = (FLRZeitdaten) resultListIterator2
								.next();

						if (zeitdaten.getTaetigkeit_i_id() != null
								&& zeitdaten.getTaetigkeit_i_id().equals(
										taetigkeitIId_Ende)) {
							// Naechste Taetigkeit ist ein ENDE
						} else {
							// Ende 10 ms vorher nachtragen
							tEnde = new java.sql.Timestamp(zeitdaten
									.getT_zeit().getTime() - 10);
						}

					} else {
						// Nachtragen
						// ende am ende des Tages
						tEnde = new java.sql.Timestamp(tMorgen.getTime() - 10);
					}

					if (tEnde != null) {
						PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
						Integer pk = pkGen
								.getNextPrimaryKey(PKConst.PK_ZEITDATEN);

						Zeitdaten zEnde = new Zeitdaten(pk, personalIId, tEnde,
								Helper.boolean2Short(false),
								theClientDto.getIDPersonal(),
								theClientDto.getIDPersonal(),
								Helper.boolean2Short(false));
						zEnde.setTaetigkeitIId(taetigkeitIId_Ende);
						em.persist(zEnde);
						em.flush();

					}

					session2.close();

				}

			}

		}

	}

	public void removeTaetigkeit(TaetigkeitDto taetigkeitDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (taetigkeitDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("taetigkeitDto == null"));
		}
		if (taetigkeitDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("taetigkeitDto.getIId() == null"));
		}

		Taetigkeit taetigkeit = em.find(Taetigkeit.class,
				taetigkeitDto.getIId());

		if (taetigkeit.getCNr().equals(ZeiterfassungFac.TAETIGKEIT_ARZT)
				|| taetigkeit.getCNr().equals(
						ZeiterfassungFac.TAETIGKEIT_BEHOERDE)
				|| taetigkeit.getCNr().equals(ZeiterfassungFac.TAETIGKEIT_ENDE)
				|| taetigkeit.getCNr().equals(ZeiterfassungFac.TAETIGKEIT_GEHT)
				|| taetigkeit.getCNr()
						.equals(ZeiterfassungFac.TAETIGKEIT_KOMMT)
				|| taetigkeit.getCNr()
						.equals(ZeiterfassungFac.TAETIGKEIT_KRANK)
				|| taetigkeit.getCNr().equals(
						ZeiterfassungFac.TAETIGKEIT_URLAUBSANTRAG)
				|| taetigkeit.getCNr().equals(
						ZeiterfassungFac.TAETIGKEIT_KINDKRANK)
				|| taetigkeit.getCNr().equals(
						ZeiterfassungFac.TAETIGKEIT_TELEFON)
				|| taetigkeit.getCNr()
						.equals(ZeiterfassungFac.TAETIGKEIT_UNTER)
				|| taetigkeit.getCNr().equals(
						ZeiterfassungFac.TAETIGKEIT_URLAUB)
				|| taetigkeit.getCNr().equals(
						ZeiterfassungFac.TAETIGKEIT_ZEITAUSGLEICH)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
					new Exception("DARF_NICHT_GELOESCHT_WERDEN"));

		}
		// try {
		try {
			Query query = em
					.createNamedQuery("TaetigkeitsprfindByTaetigkeitIId");
			query.setParameter(1, taetigkeitDto.getIId());
			Collection<?> allTaetigkeitspr = query.getResultList();
			Iterator<?> iter = allTaetigkeitspr.iterator();
			while (iter.hasNext()) {
				Taetigkeitspr taetigkeitsprTemp = (Taetigkeitspr) iter.next();
				em.remove(taetigkeitsprTemp);
			}

			em.remove(taetigkeit);
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND, e);
		// }
	}

	public void updateTaetigkeit(TaetigkeitDto taetigkeitDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (taetigkeitDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("taetigkeitDto == null"));
		}
		if (taetigkeitDto.getIId() == null || taetigkeitDto.getCNr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"taetigkeitDto.getIId() == null || taetigkeitDto.getCNr() == null"));
		}
		if (taetigkeitDto.getBBdebuchbar() == null
				|| taetigkeitDto.getFBezahlt() == null
				|| taetigkeitDto.getBFeiertag() == null
				|| taetigkeitDto.getBTagbuchbar() == null
				|| taetigkeitDto.getBVersteckt() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"taetigkeitDto.getBBdebuchbar() == null || taetigkeitDto.getFBezahlt() == null || taetigkeitDto.getBFeiertag() == null || taetigkeitDto.getBTagbuchbar() == null"));
		}

		Integer iId = taetigkeitDto.getIId();
		// try {
		Taetigkeit taetigkeit = em.find(Taetigkeit.class, iId);
		if (taetigkeit == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		try {
			Query query = em.createNamedQuery("TaetigkeitfindByCNr");
			query.setParameter(1, taetigkeitDto.getCNr());
			Integer iIdVorhanden = ((Taetigkeit) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_TAETIGKEIT.CNR"));
			}

		} catch (NoResultException ex) {
			//
		}

		// Eigenschaften nur bei Taetigken AUSSER KOMMT, GEHT, UNTER, ENDE

		if (taetigkeitDto.getCNr().equals(ZeiterfassungFac.TAETIGKEIT_KOMMT)
				|| taetigkeitDto.getCNr().equals(
						ZeiterfassungFac.TAETIGKEIT_GEHT)
				|| taetigkeitDto.getCNr().equals(
						ZeiterfassungFac.TAETIGKEIT_UNTER)
				|| taetigkeitDto.getCNr().equals(
						ZeiterfassungFac.TAETIGKEIT_TELEFON)
				|| taetigkeitDto.getCNr().equals(
						ZeiterfassungFac.TAETIGKEIT_ENDE)) {

			taetigkeit.setBBdebuchbar(taetigkeitDto.getBBdebuchbar());
			taetigkeit.setBTagbuchbar(Helper.boolean2Short(false));

		} else {
			setTaetigkeitFromTaetigkeitDto(taetigkeit, taetigkeitDto);
		}
		if (taetigkeitDto.getTaetigkeitsprDto() != null) {
			// try {
			Taetigkeitspr taetigkeitspr = em.find(Taetigkeitspr.class,
					new TaetigkeitsprPK(iId, theClientDto.getLocUiAsString()));
			if (taetigkeitspr == null) {
				try {

					taetigkeitspr = new Taetigkeitspr(iId,
							theClientDto.getLocUiAsString());
					em.persist(taetigkeitspr);
					em.flush();
					setTaetigkeitsprFromTaetigkeitsprDto(taetigkeitspr,
							taetigkeitDto.getTaetigkeitsprDto());
				} catch (EntityExistsException e) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEIM_ANLEGEN, "");
				}
			}
			setTaetigkeitsprFromTaetigkeitsprDto(taetigkeitspr,
					taetigkeitDto.getTaetigkeitsprDto());
			// }
			// catch (NoResultException ex) {
			// try {

			// Taetigkeitspr taetigkeitspr = new
			// Taetigkeitspr(iId,getTheClient(idUser).getLocUiAsString());
			// em.persist(taetigkeitspr);
			// setTaetigkeitsprFromTaetigkeitsprDto(taetigkeitspr,
			// taetigkeitDto.getTaetigkeitsprDto());
			// }
			// catch (CreateException e) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
			// e);
			// }
			// }
		}

		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }

	}

	public TaetigkeitDto taetigkeitFindByCNr(String cNr,
			TheClientDto theClientDto) {
		// check2(idUser);
		if (cNr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("cNr == null"));
		}
		try {
			Query query = em.createNamedQuery("TaetigkeitfindByCNr");
			query.setParameter(1, cNr);
			Taetigkeit taetigkeit = (Taetigkeit) query.getSingleResult();
			TaetigkeitDto tDto = assembleTaetigkeitDto(taetigkeit);

			Taetigkeitspr taetigkeitspr = em.find(
					Taetigkeitspr.class,
					new TaetigkeitsprPK(taetigkeit.getIId(), theClientDto
							.getLocMandantAsString()));
			if (taetigkeitspr != null) {
				tDto.setTaetigkeitsprDto(assembleTaetigkeitsprDto(taetigkeitspr));
			}
			return tDto;
		} catch (NoResultException ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);

		}
	}

	public TaetigkeitDto taetigkeitFindByCNrSmallOhneExc(String cnr)
			throws EJBExceptionLP {
		Validator.notEmpty(cnr, "cnr");

		try {
			Query query = em.createNamedQuery("TaetigkeitfindByCNr");
			query.setParameter(1, cnr);
			Taetigkeit taetigkeit = (Taetigkeit) query.getSingleResult();
			TaetigkeitDto tDto = assembleTaetigkeitDto(taetigkeit);
			return tDto;
		} catch (NoResultException ex) {
		}

		return null;
	}

	public TaetigkeitDto taetigkeitFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Taetigkeit taetigkeit = em.find(Taetigkeit.class, iId);
		if (taetigkeit == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		TaetigkeitDto taetigkeitDto = assembleTaetigkeitDto(taetigkeit);
		TaetigkeitsprDto taetigkeitsprDto = null;
		// try {
		Taetigkeitspr taetigkeitspr = em.find(Taetigkeitspr.class,
				new TaetigkeitsprPK(iId, theClientDto.getLocMandantAsString()));
		if (taetigkeitspr != null) {
			taetigkeitsprDto = assembleTaetigkeitsprDto(taetigkeitspr);
		}
		// }
		// catch (NoResultException ex) {
		// nothing here
		// }
		if (taetigkeitsprDto == null) {
			// try {
			Taetigkeitspr temp = em.find(
					Taetigkeitspr.class,
					new TaetigkeitsprPK(iId, theClientDto
							.getLocKonzernAsString()));
			if (temp != null) {
				taetigkeitsprDto = assembleTaetigkeitsprDto(temp);
			}
			// }
			// catch (NoResultException ex) {
			// nothing here
			// }
		}
		taetigkeitDto.setTaetigkeitsprDto(taetigkeitsprDto);
		return taetigkeitDto;
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }

	}

	private void setMaschinenzeitdatenFromMaschinenzeitdatenDto(
			Maschinenzeitdaten maschinenzeitdaten,
			MaschinenzeitdatenDto maschinenzeitdatenDto) {
		maschinenzeitdaten.setCBemerkung(maschinenzeitdatenDto.getCBemerkung());
		maschinenzeitdaten.setLossollarbeitsplanIId(maschinenzeitdatenDto
				.getLossollarbeitsplanIId());
		maschinenzeitdaten.setMaschineIId(maschinenzeitdatenDto
				.getMaschineIId());
		maschinenzeitdaten.setPersonalIIdAendern(maschinenzeitdatenDto
				.getPersonalIIdAendern());
		maschinenzeitdaten.setPersonalIIdAnlegen(maschinenzeitdatenDto
				.getPersonalIIdAnlegen());
		maschinenzeitdaten.setPersonalIIdGestartet(maschinenzeitdatenDto
				.getPersonalIIdGestartet());
		maschinenzeitdaten.setTAendern(maschinenzeitdatenDto.getTAendern());
		maschinenzeitdaten.setTAnlegen(maschinenzeitdatenDto.getTAnlegen());
		maschinenzeitdaten.setTBis(maschinenzeitdatenDto.getTBis());
		maschinenzeitdaten.setTVon(maschinenzeitdatenDto.getTVon());
		em.merge(maschinenzeitdaten);
		em.flush();
	}

	private void setTaetigkeitFromTaetigkeitDto(Taetigkeit taetigkeit,
			TaetigkeitDto taetigkeitDto) {
		taetigkeit.setCNr(taetigkeitDto.getCNr());
		taetigkeit.setTaetigkeitartCNr(taetigkeitDto.getTaetigkeitartCNr());
		taetigkeit.setBBdebuchbar(taetigkeitDto.getBBdebuchbar());
		taetigkeit.setBFeiertag(taetigkeitDto.getBFeiertag());
		taetigkeit.setFBezahlt(taetigkeitDto.getFBezahlt());
		taetigkeit.setBTagbuchbar(taetigkeitDto.getBTagbuchbar());
		taetigkeit.setISort(taetigkeitDto.getISort());
		taetigkeit.setPersonalIIdAendern(taetigkeitDto.getPersonalIIdAendern());
		taetigkeit.setTAendern(taetigkeitDto.getTAendern());
		taetigkeit.setIWarnmeldunginkalendertagen(taetigkeitDto
				.getIWarnmeldunginkalendertagen());
		taetigkeit.setCImportkennzeichen(taetigkeitDto.getCImportkennzeichen());
		taetigkeit.setBUnterbrichtwarnmeldung(taetigkeitDto
				.getBUnterbrichtwarnmeldung());
		taetigkeit.setbVersteckt(taetigkeitDto.getBVersteckt());
		em.merge(taetigkeit);
		em.flush();
	}

	private TaetigkeitDto assembleTaetigkeitDto(Taetigkeit taetigkeit) {
		return TaetigkeitDtoAssembler.createDto(taetigkeit);
	}

	private TaetigkeitDto[] assembleTaetigkeitDtos(Collection<?> taetigkeits) {
		List<TaetigkeitDto> list = new ArrayList<TaetigkeitDto>();
		if (taetigkeits != null) {
			Iterator<?> iterator = taetigkeits.iterator();
			while (iterator.hasNext()) {
				Taetigkeit taetigkeit = (Taetigkeit) iterator.next();
				list.add(assembleTaetigkeitDto(taetigkeit));
			}
		}
		TaetigkeitDto[] returnArray = new TaetigkeitDto[list.size()];
		return (TaetigkeitDto[]) list.toArray(returnArray);
	}

	private void setTaetigkeitsprFromTaetigkeitsprDto(
			Taetigkeitspr taetigkeitspr, TaetigkeitsprDto taetigkeitsprDto) {
		taetigkeitspr.setCBez(taetigkeitsprDto.getCBez());
		em.merge(taetigkeitspr);
		em.flush();
	}

	private TaetigkeitsprDto assembleTaetigkeitsprDto(
			Taetigkeitspr taetigkeitspr) {
		return TaetigkeitsprDtoAssembler.createDto(taetigkeitspr);
	}

	private TaetigkeitsprDto[] assembleTaetigkeitsprDtos(
			Collection<?> taetigkeitsprs) {
		List<TaetigkeitsprDto> list = new ArrayList<TaetigkeitsprDto>();
		if (taetigkeitsprs != null) {
			Iterator<?> iterator = taetigkeitsprs.iterator();
			while (iterator.hasNext()) {
				Taetigkeitspr taetigkeitspr = (Taetigkeitspr) iterator.next();
				list.add(assembleTaetigkeitsprDto(taetigkeitspr));
			}
		}
		TaetigkeitsprDto[] returnArray = new TaetigkeitsprDto[list.size()];
		return (TaetigkeitsprDto[]) list.toArray(returnArray);
	}

	public void createTaetigkeitart(TaetigkeitartDto taetigkeitartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (taetigkeitartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("taetigkeitartDto == null"));
		}
		if (taetigkeitartDto.getCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("taetigkeitartDto.getCNr() == null"));
		}
		try {
			Taetigkeitart taetigkeitart = new Taetigkeitart(
					taetigkeitartDto.getCNr());
			em.persist(taetigkeitart);
			em.flush();
			setTaetigkeitartFromTaetigkeitartDto(taetigkeitart,
					taetigkeitartDto);
			if (taetigkeitartDto.getTaetigkeitartsprDto() != null) {
				Taetigkeitartspr taetigkeitartspr = new Taetigkeitartspr(
						theClientDto.getLocMandantAsString(),
						taetigkeitartDto.getCNr());
				em.persist(taetigkeitartspr);
				em.flush();
				setTaetigkeitartsprFromTaetigkeitartsprDto(taetigkeitartspr,
						taetigkeitartDto.getTaetigkeitartsprDto());
			}
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeTaetigkeitart(String cNr) throws EJBExceptionLP {
		// try {
		Taetigkeitart toRemove = em.find(Taetigkeitart.class, cNr);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }

	}

	public void removeTaetigkeitart(TaetigkeitartDto taetigkeitartDto)
			throws EJBExceptionLP {
		if (taetigkeitartDto != null) {
			String cNr = taetigkeitartDto.getCNr();
			removeTaetigkeitart(cNr);
		}
	}

	public void updateTaetigkeitart(TaetigkeitartDto taetigkeitartDto)
			throws EJBExceptionLP {
		if (taetigkeitartDto != null) {
			String cNr = taetigkeitartDto.getCNr();

			Taetigkeitart taetigkeitart = em.find(Taetigkeitart.class, cNr);
			if (taetigkeitart == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setTaetigkeitartFromTaetigkeitartDto(taetigkeitart,
					taetigkeitartDto);

		}
	}

	public void updateMaschinenzeitdaten(
			MaschinenzeitdatenDto maschinenzeitdatenDto,
			TheClientDto theClientDto) {

		maschinenzeitdatenDto.setPersonalIIdAendern(theClientDto
				.getIDPersonal());
		maschinenzeitdatenDto.setTAendern(new java.sql.Timestamp(System
				.currentTimeMillis()));

		Maschinenzeitdaten maschinenzeitdaten = em.find(
				Maschinenzeitdaten.class, maschinenzeitdatenDto.getIId());

		try {
			Lossollarbeitsplan lsa = em.find(Lossollarbeitsplan.class,
					maschinenzeitdatenDto.getLossollarbeitsplanIId());
			getFertigungFac().setzeLosablieferungenAufNeuBerechnen(
					lsa.getLosIId(), theClientDto);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		if (maschinenzeitdaten == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setMaschinenzeitdatenFromMaschinenzeitdatenDto(maschinenzeitdaten,
				maschinenzeitdatenDto);

	}

	public TaetigkeitartDto taetigkeitartFindByPrimaryKey(String cNr)
			throws EJBExceptionLP {
		// try {
		Taetigkeitart taetigkeitart = em.find(Taetigkeitart.class, cNr);
		if (taetigkeitart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleTaetigkeitartDto(taetigkeitart);
		// }
		// catch (NoResultException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	private void setTaetigkeitartFromTaetigkeitartDto(
			Taetigkeitart taetigkeitart, TaetigkeitartDto taetigkeitartDto) {
		em.merge(taetigkeitart);
		em.flush();
	}

	private TaetigkeitartDto assembleTaetigkeitartDto(
			Taetigkeitart taetigkeitart) {
		return TaetigkeitartDtoAssembler.createDto(taetigkeitart);
	}

	private TaetigkeitartDto[] assembleTaetigkeitartDtos(
			Collection<?> taetigkeitarts) {
		List<TaetigkeitartDto> list = new ArrayList<TaetigkeitartDto>();
		if (taetigkeitarts != null) {
			Iterator<?> iterator = taetigkeitarts.iterator();
			while (iterator.hasNext()) {
				Taetigkeitart taetigkeitart = (Taetigkeitart) iterator.next();
				list.add(assembleTaetigkeitartDto(taetigkeitart));
			}
		}
		TaetigkeitartDto[] returnArray = new TaetigkeitartDto[list.size()];
		return (TaetigkeitartDto[]) list.toArray(returnArray);
	}

	private MaschinenzeitdatenDto[] assembleMaschinenzeitdatenDtos(
			Collection<?> taetigkeitarts) {
		List<MaschinenzeitdatenDto> list = new ArrayList<MaschinenzeitdatenDto>();
		if (taetigkeitarts != null) {
			Iterator<?> iterator = taetigkeitarts.iterator();
			while (iterator.hasNext()) {
				Maschinenzeitdaten taetigkeitart = (Maschinenzeitdaten) iterator
						.next();
				list.add(assembleMaschinenzeitdatenDto(taetigkeitart));
			}
		}
		MaschinenzeitdatenDto[] returnArray = new MaschinenzeitdatenDto[list
				.size()];
		return (MaschinenzeitdatenDto[]) list.toArray(returnArray);
	}

	private void setTaetigkeitartsprFromTaetigkeitartsprDto(
			Taetigkeitartspr taetigkeitartspr,
			TaetigkeitartsprDto taetigkeitartsprDto) {
		taetigkeitartspr.setCBez(taetigkeitartsprDto.getCBez());
		em.merge(taetigkeitartspr);
		em.flush();
	}

	private TaetigkeitartsprDto assembleTaetigkeitartsprDto(
			Taetigkeitartspr taetigkeitartspr) {
		return TaetigkeitartsprDtoAssembler.createDto(taetigkeitartspr);
	}

	private TaetigkeitartsprDto[] assembleTaetigkeitartsprDtos(
			Collection<?> taetigkeitartsprs) {
		List<TaetigkeitartsprDto> list = new ArrayList<TaetigkeitartsprDto>();
		if (taetigkeitartsprs != null) {
			Iterator<?> iterator = taetigkeitartsprs.iterator();
			while (iterator.hasNext()) {
				Taetigkeitartspr taetigkeitartspr = (Taetigkeitartspr) iterator
						.next();
				list.add(assembleTaetigkeitartsprDto(taetigkeitartspr));
			}
		}
		TaetigkeitartsprDto[] returnArray = new TaetigkeitartsprDto[list.size()];
		return (TaetigkeitartsprDto[]) list.toArray(returnArray);
	}

	/**
	 * holt die Zeitmodell-Sollzeit zu einem best. Datum und einer bestimmten
	 * Person
	 *
	 * @deprecated use getZeitmodelltagZuDatum()...
	 * @param personalDto das Personal
	 * @param d_datum
	 *            Sprachkurzzeichen (zB DE oder EN)
	 * @param tagesartIId_Feiertag
	 *            Integer
	 * @param tagesartIId_Halbtag
	 *            Integer
	 * @param theClientDto
	 *            User-ID
	 * @param bSollzeit
	 *            boolean
	 * @return Die Partnerart
	 * @throws EJBExceptionLP
	 */
	protected java.sql.Time getSollzeitZuDatum(PersonalDto personalDto,
			java.sql.Timestamp d_datum, Integer tagesartIId_Feiertag,
			Integer tagesartIId_Halbtag, TheClientDto theClientDto,
			boolean bSollzeit) throws EJBExceptionLP {
		d_datum = Helper.cutTimestamp(d_datum);
		java.sql.Time u_sollzeit = null;

		PersonalzeitmodellDto personalzeitmodellDto = null;
		try {
			personalzeitmodellDto = getPersonalFac()
					.personalzeitmodellFindZeitmodellZuDatum(
							personalDto.getIId(), d_datum, theClientDto);
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		Calendar c = Calendar.getInstance();
		c.setTime(d_datum);
		Integer tagesartIId = null;
		u_sollzeit = null;
		try {
			Query query = em.createNamedQuery("TagesartfindByCNr");
			query.setParameter(1,
					Helper.holeTagbezeichnungLang(c.get(Calendar.DAY_OF_WEEK)));
			Tagesart tagesart = (Tagesart) query.getSingleResult();
			tagesartIId = tagesart.getIId();

			BetriebskalenderDto dto = getPersonalFac()
					.betriebskalenderFindByMandantCNrDDatum(d_datum,
							theClientDto.getMandant(), theClientDto);
			if (dto != null) {
				// Wenn Religion NULL oder Religion gleich Religion in
				// Betriebskalender
				if (dto.getReligionIId() == null
						|| dto.getReligionIId() == personalDto.getReligionIId()) {
					if (dto.getTagesartIId().equals(tagesartIId_Feiertag)
							|| dto.getTagesartIId().equals(tagesartIId_Halbtag)) {
						tagesartIId = dto.getTagesartIId();
					} else {
						tagesartIId = dto.getTagesartIId();
					}
				}
			}

			if (tagesartIId != null && personalzeitmodellDto != null) {
				if (bSollzeit == true) {
					Query query2 = em
							.createNamedQuery("ZeitmodelltagfindSollzeitZuTagesart");
					query2.setParameter(1,
							personalzeitmodellDto.getZeitmodellIId());
					query2.setParameter(2, tagesartIId);
					Zeitmodelltag zeitmodelltag = (Zeitmodelltag) query2
							.getSingleResult();
					u_sollzeit = zeitmodelltag.getUSollzeit();

				} else {
					Query query2 = em
							.createNamedQuery("ZeitmodelltagfindSollzeitZuTagesart");
					query2.setParameter(1,
							personalzeitmodellDto.getZeitmodellIId());
					query2.setParameter(2, tagesartIId);
					Zeitmodelltag zeitmodelltag = (Zeitmodelltag) query2
							.getSingleResult();
					u_sollzeit = zeitmodelltag.getUErlaubteanwesenheitszeit();

				}
			}
		} catch (NoResultException e) {
			u_sollzeit = null;
		}
		return u_sollzeit;
	}

	public boolean istUrlaubstagZuDatumNoetig(Integer personalIId,
			java.sql.Timestamp d_datum, TheClientDto theClientDto)
			throws EJBExceptionLP {
		d_datum = Helper.cutTimestamp(d_datum);
		boolean bTagNoetig = true;
		java.sql.Time u_sollzeit = null;

		PersonalzeitmodellDto personalzeitmodellDto = null;
		PersonalDto personalDto = null;
		try {
			personalzeitmodellDto = getPersonalFac()
					.personalzeitmodellFindZeitmodellZuDatum(personalIId,
							d_datum, theClientDto);
			personalDto = getPersonalFac().personalFindByPrimaryKey(
					personalIId, theClientDto);
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		Calendar c = Calendar.getInstance();
		c.setTime(d_datum);
		Integer tagesartIId = null;
		try {
			Query query = em.createNamedQuery("TagesartfindByCNr");
			query.setParameter(1,
					Helper.holeTagbezeichnungLang(c.get(Calendar.DAY_OF_WEEK)));
			Tagesart tagesart = (Tagesart) query.getSingleResult();
			tagesartIId = tagesart.getIId();

			BetriebskalenderDto dto = getPersonalFac()
					.betriebskalenderFindByMandantCNrDDatum(d_datum,
							theClientDto.getMandant(), theClientDto);
			if (dto != null) {
				if (personalDto.getReligionIId() == null
						&& dto.getReligionIId() == null) {
					tagesartIId = dto.getTagesartIId();
				} else if (personalDto.getReligionIId() != null
						&& dto.getReligionIId() == null) {
					tagesartIId = dto.getTagesartIId();
				} else if (personalDto.getReligionIId() != null
						&& personalDto.getReligionIId().equals(
								dto.getReligionIId())) {
					tagesartIId = dto.getTagesartIId();
				}
			}

			if (tagesartIId != null && personalzeitmodellDto != null) {
				Query query2 = em
						.createNamedQuery("ZeitmodelltagfindSollzeitZuTagesart");
				query2.setParameter(1, personalzeitmodellDto.getZeitmodellIId());
				query2.setParameter(2, tagesartIId);
				u_sollzeit = ((Zeitmodelltag) query2.getSingleResult())
						.getUSollzeit();
				if (u_sollzeit != null && u_sollzeit.getTime() > -3600000) {
					return true;
				} else {
					bTagNoetig = false;
				}
			}

		} catch (NoResultException e) {
			return false;
		}
		return bTagNoetig;
	}

	public ZeitmodelltagDto getZeitmodelltagZuDatum(Integer personalIId,
			java.sql.Timestamp d_datum, Integer tagesartIId_Feiertag,
			Integer tagesartIId_Halbtag,
			boolean bOriginaltagWennHalberFeiertag, TheClientDto theClientDto)
			throws EJBExceptionLP {
		d_datum = Helper.cutTimestamp(d_datum);
		ZeitmodelltagDto zeitmodelltagDto = new ZeitmodelltagDto();

		String sQuery = "select pzm.flrzeitmodell.i_id FROM FLRPersonalzeitmodell pzm WHERE pzm.personal_i_id="
				+ personalIId
				+ " AND pzm.t_gueltigab<='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(d_datum
						.getTime())) + "' ORDER BY pzm.t_gueltigab DESC";

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Query gueltigesZm = session.createQuery(sQuery);
		gueltigesZm.setMaxResults(1);

		List<?> resultList = gueltigesZm.list();

		Iterator<?> resultListIterator = resultList.iterator();

		Integer zeitmodellIId = null;

		if (resultListIterator.hasNext()) {
			zeitmodellIId = (Integer) resultListIterator.next();

		}

		Personal personal = em.find(Personal.class, personalIId);

		Calendar c = Calendar.getInstance();
		c.setTime(d_datum);
		Integer tagesartIId = null;
		try {
			Query query = em.createNamedQuery("TagesartfindByCNr");
			query.setParameter(1,
					Helper.holeTagbezeichnungLang(c.get(Calendar.DAY_OF_WEEK)));
			Tagesart tagesart = (Tagesart) query.getSingleResult();
			tagesartIId = tagesart.getIId();

			BetriebskalenderDto dto = getPersonalFac()
					.betriebskalenderFindByMandantCNrDDatum(d_datum,
							theClientDto.getMandant(), theClientDto);
			if (dto != null) {
				// Wenn Religion NULL oder Religion gleich Religion in
				// Betriebskalender
				if (dto.getReligionIId() == null
						|| dto.getReligionIId().equals(
								personal.getReligionIId())) {
					if (dto.getTagesartIId().equals(tagesartIId_Feiertag)
							|| dto.getTagesartIId().equals(tagesartIId_Halbtag)) {

						if (bOriginaltagWennHalberFeiertag == true
								&& dto.getTagesartIId().equals(
										tagesartIId_Halbtag)) {
							//
						} else {
							tagesartIId = dto.getTagesartIId();
						}

					} else {
						tagesartIId = dto.getTagesartIId();
					}
				}
			}

			if (tagesartIId != null && zeitmodellIId != null) {

				Query query2 = em
						.createNamedQuery("ZeitmodelltagfindSollzeitZuTagesart");
				query2.setParameter(1, zeitmodellIId);
				query2.setParameter(2, tagesartIId);
				zeitmodelltagDto = assembleZeitmodelltagDto((Zeitmodelltag) query2
						.getSingleResult());
			}
		} catch (NoResultException e) {
			zeitmodelltagDto = new ZeitmodelltagDto();
		}
		return zeitmodelltagDto;
	}

	private BereitschafttagDto getBereitschafttagZuDatum(Integer personalIId,
			java.sql.Timestamp d_datum, Integer tagesartIId_Feiertag,
			Integer tagesartIId_Halbtag,
			boolean bOriginaltagWennHalberFeiertag, Integer bereitschaftartIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		d_datum = Helper.cutTimestamp(d_datum);
		BereitschafttagDto zeitmodelltagDto = new BereitschafttagDto();

		PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(
				personalIId, theClientDto);

		Calendar c = Calendar.getInstance();
		c.setTime(d_datum);
		Integer tagesartIId = null;
		try {
			Query query = em.createNamedQuery("TagesartfindByCNr");
			query.setParameter(1,
					Helper.holeTagbezeichnungLang(c.get(Calendar.DAY_OF_WEEK)));
			Tagesart tagesart = (Tagesart) query.getSingleResult();
			tagesartIId = tagesart.getIId();

			BetriebskalenderDto dto = getPersonalFac()
					.betriebskalenderFindByMandantCNrDDatum(d_datum,
							theClientDto.getMandant(), theClientDto);
			if (dto != null) {
				// Wenn Religion NULL oder Religion gleich Religion in
				// Betriebskalender
				if (dto.getReligionIId() == null
						|| dto.getReligionIId().equals(
								personalDto.getReligionIId())) {
					if (dto.getTagesartIId().equals(tagesartIId_Feiertag)
							|| dto.getTagesartIId().equals(tagesartIId_Halbtag)) {

						if (bOriginaltagWennHalberFeiertag == true
								&& dto.getTagesartIId().equals(
										tagesartIId_Halbtag)) {
							//
						} else {
							tagesartIId = dto.getTagesartIId();
						}

					} else {
						tagesartIId = dto.getTagesartIId();
					}
				}
			}

			if (tagesartIId != null && bereitschaftartIId != null) {

				Query query2 = em
						.createNamedQuery("BereitschafttagfindByBereitschaftartIIdTagesartIId");
				query2.setParameter(1, bereitschaftartIId);
				query2.setParameter(2, tagesartIId);
				zeitmodelltagDto = BereitschafttagDtoAssembler
						.createDto((Bereitschafttag) query2.getSingleResult());
			}
		} catch (NoResultException e) {
			zeitmodelltagDto = new BereitschafttagDto();
		}
		return zeitmodelltagDto;
	}

	/**
	 * holt die Zeitmodell-Sollzeit zu einem best. Datum und einer bestimmten
	 * Person
	 *
	 * @param personalIId
	 *            Die sprachabh&auml;ngige Bezeichnung der Partnerart (zB Firma
	 *            oder Company)
	 * @param d_datum
	 *            Sprachkurzzeichen (zB DE oder EN)
	 * @param theClientDto
	 *            User-ID
	 * @return Die Partnerart
	 * @exception EJBExceptionLP
	 */
	protected java.sql.Time getSollzeitZuDatumWennFeiertag(Integer personalIId,
			java.sql.Timestamp d_datum, TheClientDto theClientDto)
			throws EJBExceptionLP {
		d_datum = Helper.cutTimestamp(d_datum);
		java.sql.Time u_sollzeit = null;

		PersonalzeitmodellDto personalzeitmodellDto = null;
		try {
			personalzeitmodellDto = getPersonalFac()
					.personalzeitmodellFindZeitmodellZuDatum(personalIId,
							d_datum, theClientDto);
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		Calendar c = Calendar.getInstance();
		c.setTime(d_datum);
		Integer tagesartIId = null;
		try {
			Query query = em.createNamedQuery("TagesartfindByCNr");
			query.setParameter(1,
					Helper.holeTagbezeichnungLang(c.get(Calendar.DAY_OF_WEEK)));
			Tagesart tagesart = (Tagesart) query.getSingleResult();
			tagesartIId = tagesart.getIId();
			Integer tagesartIId_Feiertag = tagesartFindByCNr(
					ZeiterfassungFac.TAGESART_FEIERTAG, theClientDto).getIId();
			Integer tagesartIId_Halbtag = tagesartFindByCNr(
					ZeiterfassungFac.TAGESART_HALBTAG, theClientDto).getIId();

			BetriebskalenderDto dto = getPersonalFac()
					.betriebskalenderFindByMandantCNrDDatum(d_datum,
							theClientDto.getMandant(), theClientDto);
			if (dto != null) {

				if (dto.getTagesartIId().equals(tagesartIId_Feiertag)
						|| dto.getTagesartIId().equals(tagesartIId_Halbtag)) {
					// tagesartIId=dto.getTagesartIId();
				} else {
					tagesartIId = dto.getTagesartIId();
				}
			}

			if (tagesartIId != null && personalzeitmodellDto != null) {
				Query query2 = em
						.createNamedQuery("ZeitmodelltagfindSollzeitZuTagesart");
				query2.setParameter(1, personalzeitmodellDto.getZeitmodellIId());
				query2.setParameter(2, tagesartIId);
				u_sollzeit = ((Zeitmodelltag) query2.getSingleResult())
						.getUSollzeit();
			}
		} catch (NoResultException e) {
			u_sollzeit = null;
		}
		return u_sollzeit;
	}

	/**
	 * Ermittelt ob dieser Tag ein Feiertag oder ein Sonntag ist
	 *
	 * @param d_datum
	 *            Datum
	 * @param partner_i_id
	 *            ID des Partners
	 * @param theClientDto
	 *            User-ID
	 * @return Feiertag/Halbtag/Sonntag/null(Wenn kein Feiertag/Sonntag)
	 * @exception EJBExceptionLP
	 */
	public Integer istFeiertagOderSonntag(java.sql.Timestamp d_datum,
			Integer partner_i_id, TheClientDto theClientDto)
			throws EJBExceptionLP

	{
		d_datum = Helper.cutTimestamp(d_datum);
		Integer tagesartIId = null;
		PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(
				partner_i_id, theClientDto);

		Integer religionIId = personalDto.getReligionIId();

		if (religionIId != null) {
			try {
				BetriebskalenderDto dtoTemp = getPersonalFac()
						.betriebskalenderFindByMandantCNrDDatumReligionIId(
								d_datum, personalDto.getMandantCNr(),
								religionIId);
				if (dtoTemp != null) {
					tagesartIId = dtoTemp.getTagesartIId();
				}
			} catch (RemoteException ex) {
				// keiner da
			}
		} else {

			BetriebskalenderDto dtoTemp = getPersonalFac()
					.betriebskalenderFindByMandantCNrDDatum(d_datum,
							personalDto.getMandantCNr(), theClientDto);
			if (dtoTemp != null) {
				tagesartIId = dtoTemp.getTagesartIId();
			}

		}

		if (tagesartIId == null) {
			Calendar cKalendar = Calendar.getInstance();
			cKalendar.setTime(d_datum);

			if (cKalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				try {
					Query query = em.createNamedQuery("TagesartfindByCNr");
					query.setParameter(1, ZeiterfassungFac.TAGESART_SONNTAG);
					Tagesart tagesart = (Tagesart) query.getSingleResult();
					tagesartIId = tagesart.getIId();
				} catch (NoResultException ex2) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex2);
				}
			}
		}

		return tagesartIId;
	}

	private Integer getTagesartZuDatum(Integer personalIId,
			java.sql.Timestamp d_datum, Integer tagesartIId_Feiertag,
			Integer tagesartIId_Halbtag, TheClientDto theClientDto)
			throws EJBExceptionLP {
		d_datum = Helper.cutTimestamp(d_datum);
		Personal personal = em.find(Personal.class, personalIId);

		Calendar c = Calendar.getInstance();
		c.setTime(d_datum);
		Integer tagesartIId = null;
		try {
			Query query = em.createNamedQuery("TagesartfindByCNr");
			query.setParameter(1,
					Helper.holeTagbezeichnungLang(c.get(Calendar.DAY_OF_WEEK)));
			Tagesart tagesart = (Tagesart) query.getSingleResult();
			tagesartIId = tagesart.getIId();

			BetriebskalenderDto dto = getPersonalFac()
					.betriebskalenderFindByMandantCNrDDatum(d_datum,
							theClientDto.getMandant(), theClientDto);
			if (dto != null) {
				// Wenn Religion NULL oder Religion gleich Religion in
				// Betriebskalender
				if (dto.getReligionIId() == null
						|| dto.getReligionIId().equals(
								personal.getReligionIId())) {
					if (dto.getTagesartIId().equals(tagesartIId_Feiertag)
							|| dto.getTagesartIId().equals(tagesartIId_Halbtag)) {
						tagesartIId = dto.getTagesartIId();
					} else {
						tagesartIId = dto.getTagesartIId();
					}
				}
			}

		} catch (NoResultException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}
		return tagesartIId;
	}

	/**
	 * Pr&uuml;ft, ob die Zeitbuchungen zur aktuellen Zeit gebucht worden sind
	 * und setzt bManipuliert auf true, wenn die Buchungszeit um mehr als 2 Min
	 * von der aktuellen Zeit abweicht
	 *
	 * @param i_id
	 *            ID der Zeitbewegung
	 * @return boolean ob die Buchung manipuliert ist oder nicht
	 * @exception RemoteException
	 * @exception EJBExceptionLP
	 */
	public boolean istBuchungManipuliert(Integer i_id) throws EJBExceptionLP {
		boolean bManipuliert = false;
		// try {

		Zeitdaten zeitdaten = em.find(Zeitdaten.class, i_id);
		if (zeitdaten == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}

		java.sql.Timestamp t_zeit = zeitdaten.getTZeit();
		java.sql.Timestamp t_aendern = zeitdaten.getTAendern();
		if ((t_aendern.getTime() - t_zeit.getTime()) > 180000
				|| (t_aendern.getTime() - t_zeit.getTime()) < -180000) {
			bManipuliert = true;
		}

		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);

		// }
		return bManipuliert;
	}

	private BigDecimal getAlteUrlaubStunden(PersonalDto personalDto,
			java.sql.Timestamp dEintrittsdatum,
			java.sql.Timestamp dAktuellerUrlaubsbeginn,
			Integer tagesartIId_Feiertag, Integer tagesartIId_Halbtag,
			TheClientDto theClientDto) {

		// VERBRAUCHTE TAGE+HALBTAGE+STUNDEN in Stunden umgerechnet ALT:
		Integer iIdUrlaub = null;
		try {
			Query query = em.createNamedQuery("TaetigkeitfindByCNr");
			query.setParameter(1, ZeiterfassungFac.TAETIGKEIT_URLAUB);
			Taetigkeit taetigkeit = (Taetigkeit) query.getSingleResult();
			iIdUrlaub = taetigkeit.getIId();
		} catch (NoResultException ex7) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex7);
		}
		double dStundenAlt = 0;

		// try {
		Query query2 = em
				.createNamedQuery("SonderzeitenfindTageweiseUndHalbtageweiseTaetigkeitenAlt");
		query2.setParameter(1, personalDto.getIId());
		query2.setParameter(2, iIdUrlaub);
		query2.setParameter(3, dEintrittsdatum);
		query2.setParameter(4, dAktuellerUrlaubsbeginn);

		SonderzeitenDto[] altTageDtos = assembleSonderzeitenDtos(query2
				.getResultList());

		for (int i = 0; i < altTageDtos.length; i++) {

			Double d = Helper.time2Double(getSollzeitZuDatum(personalDto,
					altTageDtos[i].getTDatum(), tagesartIId_Feiertag,
					tagesartIId_Halbtag, theClientDto, true));
			if (d != null) {
				dStundenAlt = dStundenAlt + d.doubleValue();
			}
		}
		// }
		// catch (NoResultException ex) {
		// Keiner da
		// }
		// try {
		query2 = em
				.createNamedQuery("SonderzeitenfindStundenweiseTaetigkeitenAlt");
		query2.setParameter(1, personalDto.getIId());
		query2.setParameter(2, iIdUrlaub);
		query2.setParameter(3, dEintrittsdatum);
		query2.setParameter(4, dAktuellerUrlaubsbeginn);

		SonderzeitenDto[] altStundenDtos = assembleSonderzeitenDtos(query2
				.getResultList());
		for (int i = 0; i < altStundenDtos.length; i++) {
			Double d = Helper.time2Double(altStundenDtos[i].getUStunden());
			if (d != null) {
				dStundenAlt = dStundenAlt + d.doubleValue();
			}
		}
		// }
		// catch (NoResultException ex) {
		// Keiner da
		// }

		return new BigDecimal(dStundenAlt);
	}

	private BigDecimal getGeplanteUrlaubStunden(PersonalDto personalDto,
			java.sql.Timestamp dAbrechnungzeitpunkt,
			Integer tagesartIId_Feiertag, Integer tagesartIId_Halbtag,
			TheClientDto theClientDto) {

		// GEPLANTE TAGE+HALBTAGE+STUNDEN in Stunden umgerechnet:
		Integer iIdUrlaub = null;
		// try {
		Query query = em.createNamedQuery("TaetigkeitfindByCNr");
		query.setParameter(1, ZeiterfassungFac.TAETIGKEIT_URLAUB);
		Taetigkeit taetigkeit = (Taetigkeit) query.getSingleResult();
		if (taetigkeit == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		iIdUrlaub = taetigkeit.getIId();
		// }
		// catch (NoResultException ex7) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex7);
		// }
		double dStundenAlt = 0;

		// try {
		Query query2 = em
				.createNamedQuery("SonderzeitenfindTageweiseUndHalbtageweiseTaetigkeitenGeplant");
		query2.setParameter(1, personalDto.getIId());
		query2.setParameter(2, iIdUrlaub);
		query2.setParameter(3, dAbrechnungzeitpunkt);

		SonderzeitenDto[] altTageDtos = assembleSonderzeitenDtos(query2
				.getResultList());

		for (int i = 0; i < altTageDtos.length; i++) {

			Double d = Helper.time2Double(getSollzeitZuDatum(personalDto,
					altTageDtos[i].getTDatum(), tagesartIId_Feiertag,
					tagesartIId_Halbtag, theClientDto, true));
			if (d != null) {

				if (Helper.short2boolean(altTageDtos[i].getBTag()) == true) {
					dStundenAlt = dStundenAlt + d.doubleValue();
				}
				if (Helper.short2boolean(altTageDtos[i].getBHalbtag()) == true) {
					double dTemp = d.doubleValue() / 2;
					dStundenAlt = dStundenAlt + dTemp;
				}
			}
		}
		// }
		// catch (NoResultException ex) {
		// Keiner da
		// }
		try {
			query2 = em
					.createNamedQuery("SonderzeitenfindStundenweiseTaetigkeitenGeplant");
			query2.setParameter(1, personalDto.getIId());
			query2.setParameter(2, iIdUrlaub);
			query2.setParameter(3, dAbrechnungzeitpunkt);

			SonderzeitenDto[] altStundenDtos = assembleSonderzeitenDtos(query2
					.getResultList());

			for (int i = 0; i < altStundenDtos.length; i++) {
				Double d = Helper.time2Double(altStundenDtos[i].getUStunden());
				if (d != null) {
					dStundenAlt = dStundenAlt + d.doubleValue();
				}
			}
		} catch (NoResultException ex) {
			// Keiner da
		}

		return new BigDecimal(dStundenAlt);
	}

	private BigDecimal getAktuelleUrlaubStunden(Integer personalIId,
			java.sql.Timestamp dAktuellerUrlaubsbeginn,
			java.sql.Timestamp dAbrechnungzeitpunkt,
			Integer tagesartIId_Feiertag, Integer tagesartIId_Halbtag,
			TheClientDto theClientDto) {
		// AKTUELLE TAGE+HALBTAGE+STUNDEN in Stunden umgerechnet:
		Integer iIdUrlaub = null;
		try {
			Query query = em.createNamedQuery("TaetigkeitfindByCNr");
			query.setParameter(1, ZeiterfassungFac.TAETIGKEIT_URLAUB);
			Taetigkeit taetigkeit = (Taetigkeit) query.getSingleResult();
			iIdUrlaub = taetigkeit.getIId();
		} catch (NoResultException ex7) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex7);
		}
		double dStundenAktuell = 0;

		// try {
		Query query2 = em
				.createNamedQuery("SonderzeitenfindTageweiseUndHalbtageweiseTaetigkeitenAktuell");
		query2.setParameter(1, personalIId);
		query2.setParameter(2, iIdUrlaub);
		query2.setParameter(3, dAktuellerUrlaubsbeginn);
		query2.setParameter(4, dAbrechnungzeitpunkt);

		SonderzeitenDto[] aktuellTageDtos = assembleSonderzeitenDtos(query2
				.getResultList());

		for (int i = 0; i < aktuellTageDtos.length; i++) {

			boolean bIstHalberFeiertag = false;

			BetriebskalenderDto dto = getPersonalFac()
					.betriebskalenderFindByMandantCNrDDatum(
							aktuellTageDtos[i].getTDatum(),
							theClientDto.getMandant(), theClientDto);

			if (dto != null) {

				if (dto.getTagesartIId().equals(tagesartIId_Halbtag)) {
					bIstHalberFeiertag = true;
				}
			}

			if (bIstHalberFeiertag) {
				ZeitmodelltagDto zeitmodelltagDtoOriginaltag = getZeitmodelltagZuDatum(
						personalIId, aktuellTageDtos[i].getTDatum(),
						tagesartIId_Feiertag, tagesartIId_Halbtag, true,
						theClientDto);

				if (zeitmodelltagDtoOriginaltag != null
						&& zeitmodelltagDtoOriginaltag.getUSollzeit() != null) {

					BigDecimal dbHalbtagsUrlaub = null;
					if (Helper.short2boolean(aktuellTageDtos[i].getBHalbtag())) {
						dbHalbtagsUrlaub = new BigDecimal(Helper.time2Double(
								zeitmodelltagDtoOriginaltag.getUSollzeit())
								.doubleValue() / 2);
					} else {
						dbHalbtagsUrlaub = new BigDecimal(Helper.time2Double(
								zeitmodelltagDtoOriginaltag.getUSollzeit())
								.doubleValue() / 2);
					}

					dbHalbtagsUrlaub = Helper.rundeKaufmaennisch(
							dbHalbtagsUrlaub, 2);
					dStundenAktuell = dStundenAktuell
							+ dbHalbtagsUrlaub.doubleValue();

				}
			} else {
				ZeitmodelltagDto zmTag = getZeitmodelltagZuDatum(personalIId,
						aktuellTageDtos[i].getTDatum(), tagesartIId_Feiertag,
						tagesartIId_Halbtag, false, theClientDto);

				Double d = Helper.time2Double(zmTag.getUSollzeit());
				if (d != null) {
					if (Helper.short2boolean(aktuellTageDtos[i].getBHalbtag())) {
						dStundenAktuell = dStundenAktuell + d.doubleValue() / 2;
					} else {
						dStundenAktuell = dStundenAktuell + d.doubleValue();
					}
				}
			}

		}
		// }
		// catch (NoResultException ex) {
		// Keiner da
		// }
		// try {
		query2 = em
				.createNamedQuery("SonderzeitenfindStundenweiseTaetigkeitenAktuell");
		query2.setParameter(1, personalIId);
		query2.setParameter(2, iIdUrlaub);
		query2.setParameter(3, dAktuellerUrlaubsbeginn);
		query2.setParameter(4, dAbrechnungzeitpunkt);

		SonderzeitenDto[] aktuellStundenDtos = assembleSonderzeitenDtos(query2
				.getResultList());

		for (int i = 0; i < aktuellStundenDtos.length; i++) {
			Double d = Helper.time2Double(aktuellStundenDtos[i].getUStunden());
			if (d != null) {
				dStundenAktuell = dStundenAktuell + d.doubleValue();
			}
		}
		// }
		// catch (NoResultException ex) {
		// Keiner da
		// }

		return new BigDecimal(dStundenAktuell);
	}

	private BigDecimal getAlterUrlaubTageweise(Integer personalIId,
			java.sql.Timestamp dEintrittsdatum,
			java.sql.Timestamp dAktuellerUrlaubsbeginn) {
		// VERBRAUCHTE TAGE+HALBTAGE ALT:
		Integer iIdUrlaub = null;
		try {
			Query query = em.createNamedQuery("TaetigkeitfindByCNr");
			query.setParameter(1, ZeiterfassungFac.TAETIGKEIT_URLAUB);
			Taetigkeit taetigkeit = (Taetigkeit) query.getSingleResult();
			// if (taetigkeit==null) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
			// / null);
			// }
			iIdUrlaub = taetigkeit.getIId();
		} catch (NoResultException ex7) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex7);
		}

		double dAlterUrlaubTagesweise = 0;
		// Ganze Urlaubstage ALT holen
		Query query2 = em
				.createNamedQuery("SonderzeitenejbSelectGanztaegigeTaetigkeitenAlt");
		query2.setParameter(1, personalIId);
		query2.setParameter(2, iIdUrlaub);
		query2.setParameter(3, dEintrittsdatum);
		query2.setParameter(4, dAktuellerUrlaubsbeginn);

		Long urlAltTage = (Long) query2.getSingleResult();

		if (urlAltTage != null) {
			dAlterUrlaubTagesweise = dAlterUrlaubTagesweise
					+ urlAltTage.doubleValue();
		}

		query2 = em
				.createNamedQuery("SonderzeitenejbSelectHalbtaegigeTaetigkeitenAlt");
		query2.setParameter(1, personalIId);
		query2.setParameter(2, iIdUrlaub);
		query2.setParameter(3, dEintrittsdatum);
		query2.setParameter(4, dAktuellerUrlaubsbeginn);
		Long urlAltHalbtage = (Long) query2.getSingleResult();

		if (urlAltHalbtage != null) {
			double dTemp = urlAltHalbtage.doubleValue() / 2;
			dAlterUrlaubTagesweise = dAlterUrlaubTagesweise + dTemp;

		}
		return new BigDecimal(dAlterUrlaubTagesweise);

	}

	public Map<String, String> getBebuchbareBelegarten(TheClientDto theClientDto) {

		boolean bHatAngebotszeiterfassung = false;
		boolean bHatProjektzeiterfassung = false;

		if (getMandantFac().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_PROJEKT, theClientDto)
				&& getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_PROJEKTZEITERFASSUNG,
						theClientDto)) {
			bHatProjektzeiterfassung = true;
		}
		if (getMandantFac().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_ANGEBOT, theClientDto)
				&& getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ANGEBOTSZEITERFASSUNG,
						theClientDto)) {
			bHatAngebotszeiterfassung = true;
		}

		Map<String, String> mBelegarten = new TreeMap<String, String>();
		if (getMandantFac().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_AUFTRAG, theClientDto)) {
			mBelegarten
					.put(LocaleFac.BELEGART_AUFTRAG,
							getTextRespectUISpr("auft.auftrag",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
		}
		if (getMandantFac().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_LOS, theClientDto)) {
			mBelegarten
					.put(LocaleFac.BELEGART_LOS,
							getTextRespectUISpr("fert.tab.unten.los.title",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
		}
		if (bHatAngebotszeiterfassung) {
			mBelegarten
					.put(LocaleFac.BELEGART_ANGEBOT,
							getTextRespectUISpr("angb.angebot",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
		}
		if (bHatProjektzeiterfassung == true) {
			mBelegarten
					.put(LocaleFac.BELEGART_PROJEKT,
							getTextRespectUISpr("lp.projekt.modulname",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
		}

		return Helper.sortByValue(mBelegarten);

	}

	private BigDecimal getAktuellerUrlaubTageweise(Integer personalIId,
			java.sql.Timestamp dAktuellerUrlaubsbeginn,
			java.sql.Timestamp dAbrechnungzeitpunkt) {
		// VERBRAUCHTE TAGE+HALBTAGE ALT:
		Integer iIdUrlaub = null;
		try {
			Query query = em.createNamedQuery("TaetigkeitfindByCNr");
			query.setParameter(1, ZeiterfassungFac.TAETIGKEIT_URLAUB);

			Taetigkeit taetigkeit = (Taetigkeit) query.getSingleResult();
			// if (taetigkeit==null) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
			// null);
			// }
			iIdUrlaub = taetigkeit.getIId();
		} catch (NoResultException ex7) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex7);
		}

		// VERBRAUCHTE TAGE+HALBTAGE AKTUELL:
		double dAktuellerUrlaubTagesweise = 0;
		// Ganze Urlaubstage ALT holen
		Query query2 = em
				.createNamedQuery("SonderzeitenejbSelectGanztaegigeTaetigkeitenAktuell");
		query2.setParameter(1, personalIId);
		query2.setParameter(2, iIdUrlaub);
		query2.setParameter(3, dAktuellerUrlaubsbeginn);
		query2.setParameter(4, dAbrechnungzeitpunkt);

		Long urlAktuellTage = (Long) query2.getSingleResult();

		if (urlAktuellTage != null) {
			dAktuellerUrlaubTagesweise = dAktuellerUrlaubTagesweise
					+ urlAktuellTage.doubleValue();
		}

		query2 = em
				.createNamedQuery("SonderzeitenejbSelectHalbtaegigeTaetigkeitenAktuell");
		query2.setParameter(1, personalIId);
		query2.setParameter(2, iIdUrlaub);
		query2.setParameter(3, dAktuellerUrlaubsbeginn);
		query2.setParameter(4, dAbrechnungzeitpunkt);

		Long urlAktuellHalbtage = (Long) query2.getSingleResult();

		if (urlAktuellHalbtage != null) {
			double dTemp = urlAktuellHalbtage.doubleValue() / 2;
			dAktuellerUrlaubTagesweise = dAktuellerUrlaubTagesweise + dTemp;

		}

		return new BigDecimal(dAktuellerUrlaubTagesweise);

	}

	private BigDecimal getGeplanterUrlaubTageweise(Integer personalIId,
			java.sql.Timestamp dAbrechnungzeitpunkt) {
		// GEPLANTE TAGE+HALBTAGE ALT:
		Integer iIdUrlaub = null;
		try {
			Query query = em.createNamedQuery("TaetigkeitfindByCNr");
			query.setParameter(1, ZeiterfassungFac.TAETIGKEIT_URLAUB);
			Taetigkeit taetigkeit = (Taetigkeit) query.getSingleResult();
			if (taetigkeit == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			iIdUrlaub = taetigkeit.getIId();
		} catch (NoResultException ex7) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex7);
		}

		// GEPLANTE TAGE+HALBTAGE AKTUELL:
		double dGeplanterUrlaubTagesweise = 0;
		// Ganze Urlaubstage ALT holen
		Query query2 = em
				.createNamedQuery("SonderzeitenejbSelectGanztaegigeTaetigkeitenGeplant");
		query2.setParameter(1, personalIId);
		query2.setParameter(2, iIdUrlaub);
		query2.setParameter(3, dAbrechnungzeitpunkt);

		Long urlGeplantTage = (Long) query2.getSingleResult();

		if (urlGeplantTage != null) {
			dGeplanterUrlaubTagesweise = dGeplanterUrlaubTagesweise
					+ urlGeplantTage.doubleValue();
		}
		query2 = em
				.createNamedQuery("SonderzeitenejbSelectHalbtaegigeTaetigkeitenGeplant");
		query2.setParameter(1, personalIId);
		query2.setParameter(2, iIdUrlaub);
		query2.setParameter(3, dAbrechnungzeitpunkt);

		Long urlGeplantHalbtage = (Long) query2.getSingleResult();

		if (urlGeplantHalbtage != null) {
			double dTemp = urlGeplantHalbtage.doubleValue() / 2;
			dGeplanterUrlaubTagesweise = dGeplanterUrlaubTagesweise + dTemp;

		}

		return new BigDecimal(dGeplanterUrlaubTagesweise);

	}

	private BigDecimal getAliquoterUrlaubsanspruchStunden(
			java.sql.Timestamp tStichtag, Double jahresStunden,
			TheClientDto theClientDto, Date dEintrittsdatum) {
		if (tStichtag == null || jahresStunden == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("tStichtag == null || jahresStunden == null"));
		}

		double dAliquoterAnspruch = 0;

		Calendar c = Calendar.getInstance();
		c.setTime(new Date(tStichtag.getTime()));

		Calendar cEintritt = Calendar.getInstance();
		cEintritt.setTime(dEintrittsdatum);

		if (c.get(Calendar.YEAR) != cEintritt.get(Calendar.YEAR)) {

			double iTagDesJahres = c.get(Calendar.DAY_OF_YEAR);
			double anzahlTageDesJahres = c
					.getActualMaximum(Calendar.DAY_OF_YEAR);

			dAliquoterAnspruch = (jahresStunden.doubleValue() / anzahlTageDesJahres)
					* iTagDesJahres;

			return new BigDecimal(dAliquoterAnspruch);
		} else {

			/*
			 * Bei w&auml;hrend des Jahres eingetretenen Mitarbeitern gibt es
			 * immer die Problematik wie wird der aliquoate Urlaub w&auml;hrend
			 * des Jahres richtig errechnet. Dies geht nur in der Form, dass a.)
			 * Der volle Jahres Urlaubsanspruch eingetragen wird b.) Es wird der
			 * Aliquote Urlaubsanspruch vom 1.1. bis zum Stichtag berechnet. c.)
			 * Davon wird der aliquote Anspruch vom 1.1. bis zum EIntrittsdatum
			 * abgezogen. Dies ergibt den Rest-Aliquoten Anspruch bis zum
			 * Stichtag.
			 */

			double iAktuelleTagDesJahres = c.get(Calendar.DAY_OF_YEAR);
			double anzahlTageDesJahres = c
					.getActualMaximum(Calendar.DAY_OF_YEAR);
			double urlaub = 0;

			double iAbTagUrlaubsansprung = cEintritt.get(Calendar.DAY_OF_YEAR);

			if (iAktuelleTagDesJahres > iAbTagUrlaubsansprung) {
				double urlaubsansprunhProTag = (jahresStunden.doubleValue() / (anzahlTageDesJahres));

				dAliquoterAnspruch = ((iAktuelleTagDesJahres - iAbTagUrlaubsansprung) * urlaubsansprunhProTag);
			} else {
				dAliquoterAnspruch = 0;
			}

			return new BigDecimal(dAliquoterAnspruch);
		}
	}

	private Double getAliquoterUrlaubsanspruchTageweise(
			java.sql.Timestamp tStichtag, Double urlaubstageDesJahres,
			Date dEintrittsdatum, TheClientDto theClientDto) {
		if (tStichtag == null || urlaubstageDesJahres == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"tStichtag == null || urlaubstageDesJahres == null"));
		}
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(tStichtag.getTime()));

		Calendar cEintritt = Calendar.getInstance();
		cEintritt.setTime(dEintrittsdatum);

		BigDecimal bdUrlaub = BigDecimal.ZERO;

		if (c.get(Calendar.YEAR) != cEintritt.get(Calendar.YEAR)) {
			double iTagDesJahres = c.get(Calendar.DAY_OF_YEAR);
			double anzahlTageDesJahres = c
					.getActualMaximum(Calendar.DAY_OF_YEAR);

			double urlaub = (iTagDesJahres / anzahlTageDesJahres)
					* urlaubstageDesJahres.doubleValue();
			bdUrlaub = new BigDecimal(urlaub);

		} else {

			/*
			 * Bei w&auml;hrend des Jahres eingetretenen Mitarbeitern gibt es
			 * immer die Problematik wie wird der aliquoate Urlaub w&auml;hrend
			 * des Jahres richtig errechnet. Dies geht nur in der Form, dass a.)
			 * Der volle Jahres Urlaubsanspruch eingetragen wird b.) Es wird der
			 * Aliquote Urlaubsanspruch vom 1.1. bis zum Stichtag berechnet. c.)
			 * Davon wird der aliquote Anspruch vom 1.1. bis zum EIntrittsdatum
			 * abgezogen. Dies ergibt den Rest-Aliquoten Anspruch bis zum
			 * Stichtag.
			 */

			double iAktuelleTagDesJahres = c.get(Calendar.DAY_OF_YEAR);
			double anzahlTageDesJahres = c
					.getActualMaximum(Calendar.DAY_OF_YEAR);
			double urlaub = 0;

			double iAbTagUrlaubsansprung = cEintritt.get(Calendar.DAY_OF_YEAR);

			if ((iAktuelleTagDesJahres - 1) > iAbTagUrlaubsansprung) {
				double urlaubsansprunhProTag = (urlaubstageDesJahres
						.doubleValue() / (anzahlTageDesJahres - (iAbTagUrlaubsansprung - 1)));

				urlaub = (iAktuelleTagDesJahres * urlaubsansprunhProTag)
						- ((iAbTagUrlaubsansprung - 1) * urlaubsansprunhProTag);
			}
			bdUrlaub = new BigDecimal(urlaub);

		}

		bdUrlaub = rundeUrlaubstageAnhandParameter(bdUrlaub, theClientDto);

		return bdUrlaub.doubleValue();
	}

	private BigDecimal rundeUrlaubstageAnhandParameter(
			BigDecimal bdUrlaubstage, TheClientDto theClientDto) {

		if (bdUrlaubstage == null) {
			return null;
		}

		try {
			ParametermandantDto parameter = (ParametermandantDto) getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_PERSONAL,
							ParameterFac.PARAMETER_URAUBSTAGE_RUNDUNG);

			int i = (Integer) parameter.getCWertAsObject();

			if (i == 0) {
				bdUrlaubstage = bdUrlaubstage.divide(BigDecimal.ONE, 2,
						BigDecimal.ROUND_HALF_UP);
			} else if (i == 1) {
				bdUrlaubstage = bdUrlaubstage.divide(BigDecimal.ONE, 0,
						BigDecimal.ROUND_HALF_UP);
			} else if (i == 2) {
				bdUrlaubstage = bdUrlaubstage.divide(BigDecimal.ONE, 0,
						BigDecimal.ROUND_DOWN);
			} else if (i == 3) {
				bdUrlaubstage = bdUrlaubstage.divide(BigDecimal.ONE, 0,
						BigDecimal.ROUND_UP);
			}

		} catch (RemoteException ex5) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
		}
		return bdUrlaubstage;
	}

	public BigDecimal berechneDiaetenAusScript(Integer diaetenIId,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			TheClientDto theClientDto, String personalart) {

		/////////////////////////////////////////////////////

		// Daten fuer JRuby Script

		ReisekostenScript script = new ReisekostenScript(getSystemFac(), theClientDto);

		DiaetentagessatzDto[] diaetentagessatzDtos = getDiaetenTagesSatzDtos(diaetenIId, tVon);

		String scriptName = null;
		if (diaetentagessatzDtos.length != 0) {
			scriptName = diaetentagessatzDtos[0].getCFilenameScript();
		}

		if(scriptName != null) {

			Integer landId = diaetenFindByPrimaryKey(diaetenIId).getLandIId();
			String lkz = getSystemFac().landFindByPrimaryKey(landId).getCLkz();

			BigDecimal diaetenGesamt = BigDecimal.ZERO.setScale(2);

			int bginnYear = getYear(tVon);
			int endYear = getYear(tBis);

			ReisekostenDiaetenScript reisekostenDiaetenScript = new ReisekostenDiaetenScript(
					tVon,
					tBis,
					lkz,
					personalart,
					bginnYear,
					endYear
					) ;

			BigDecimal bdDiaeten = script.getValue(reisekostenDiaetenScript, scriptName);
			return diaetenGesamt.add(bdDiaeten);
		} else {
			return berechneDiaeten(diaetenIId, tVon, tBis, theClientDto);
		}

		/////////////////////////////////////////////////////

	}

	private int getYear(Timestamp ts) {
		long time = ts.getTime();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		int year = cal.get(Calendar.YEAR);
		return year;
	}

	public BigDecimal berechneDiaeten(Integer diaetenIId,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			TheClientDto theClientDto
			) {

		// BigDecimal diaetenGesamt = new BigDecimal(0);
		// diaetenGesamt.setScale(2);
		BigDecimal diaetenGesamt = BigDecimal.ZERO.setScale(2);
		boolean bInland = true;

		if (tVon.before(tBis)) {
			Calendar cVon = Calendar.getInstance();
			cVon.setTimeInMillis(tVon.getTime());

			Calendar cBis = Calendar.getInstance();
			cBis.setTimeInMillis(tBis.getTime());

			while (cBis.after(cVon)) {
				java.sql.Time tDauer = null;
				if (cVon.get(Calendar.DAY_OF_YEAR) == cBis
						.get(Calendar.DAY_OF_YEAR)) {

					tDauer = new java.sql.Time(cBis.getTimeInMillis()
							- cVon.getTimeInMillis());

				} else {
					Calendar cTemp = Calendar.getInstance();
					cTemp.setTimeInMillis(cVon.getTimeInMillis());
					cTemp.set(Calendar.HOUR_OF_DAY, 23);
					cTemp.set(Calendar.MINUTE, 59);
					cTemp.set(Calendar.MILLISECOND, 999);
					cTemp.set(Calendar.SECOND, 59);

					tDauer = new java.sql.Time(cTemp.getTimeInMillis()
							- cVon.getTimeInMillis());

				}

				tDauer.setTime(tDauer.getTime() - 3600000);
				Double dDauer = Helper.time2Double(tDauer);

				// BigDecimal diaeten = new BigDecimal(0);
				// diaeten.setScale(2);
				BigDecimal diaeten = BigDecimal.ZERO.setScale(2);
				// try {
				Diaeten temp = em.find(Diaeten.class, diaetenIId);
				if (temp == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
				}
				Integer landIId = temp.getLandIId();

				try {
					PartnerDto mandant = getMandantFac()
							.mandantFindByPrimaryKey(theClientDto.getMandant(),
									theClientDto).getPartnerDto();
					if (mandant.getLandplzortDto() != null) {
						if (!landIId.equals(mandant.getLandplzortDto()
								.getIlandID())) {
							bInland = false;
						}
					}
				} catch (RemoteException ex) {
					throwEJBExceptionLPRespectOld(ex);
				}

				DiaetentagessatzDto[] dtos = getDiaetenTagesSatzDtos(diaetenIId, tVon);

				if (dtos.length > 0) {
					DiaetentagessatzDto dto = dtos[0];

					if (dDauer.doubleValue() > 12) {
						dDauer = new Double(12);
					}
					if (dDauer.doubleValue() % 1 > 0) {
						dDauer = new Double(dDauer.doubleValue() + 1);

					}

					if (Helper.short2boolean(dto.getBStundenweise()) == true) {
						if (dDauer.doubleValue() > dto.getIAbstunden()
								.doubleValue()) {
							diaeten = new BigDecimal(dDauer.intValue()
									* dto.getNStundensatz().doubleValue());
						}
					} else {
						if (dDauer.doubleValue() <= 3) {
							diaeten = new BigDecimal(0);
						} else {
							diaeten = dto.getNStundensatz().multiply(
									new BigDecimal(dDauer.intValue()));
						}
					}

				}

				// }
				// catch (NoResultException e) {
				// throw new
				// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
				// e);
				// }

				diaetenGesamt = diaetenGesamt.add(diaeten);

				cVon.set(Calendar.DAY_OF_MONTH,
						cVon.get(Calendar.DAY_OF_MONTH) + 1);
				cVon.set(Calendar.HOUR_OF_DAY, 0);
				cVon.set(Calendar.MINUTE, 0);
				cVon.set(Calendar.MILLISECOND, 0);
				cVon.set(Calendar.SECOND, 0);
			}

		}

		return diaetenGesamt;
	}

//	public BigDecimal berechneDiaeten(Integer diaetenIId,
//			java.sql.Timestamp tVon, java.sql.Timestamp tBis,
//			TheClientDto theClientDto) {
//
//		// BigDecimal diaetenGesamt = new BigDecimal(0);
//		// diaetenGesamt.setScale(2);
//		BigDecimal diaetenGesamt = BigDecimal.ZERO.setScale(2);
//		boolean bInland = true;
//
//		if (tVon.before(tBis)) {
//			Calendar cVon = Calendar.getInstance();
//			cVon.setTimeInMillis(tVon.getTime());
//
//			Calendar cBis = Calendar.getInstance();
//			cBis.setTimeInMillis(tBis.getTime());
//
//			while (cBis.after(cVon)) {
//				java.sql.Time tDauer = null;
//				if (cVon.get(Calendar.DAY_OF_YEAR) == cBis
//						.get(Calendar.DAY_OF_YEAR)) {
//
//					tDauer = new java.sql.Time(cBis.getTimeInMillis()
//							- cVon.getTimeInMillis());
//
//				} else {
//					Calendar cTemp = Calendar.getInstance();
//					cTemp.setTimeInMillis(cVon.getTimeInMillis());
//					cTemp.set(Calendar.HOUR_OF_DAY, 23);
//					cTemp.set(Calendar.MINUTE, 59);
//					cTemp.set(Calendar.MILLISECOND, 999);
//					cTemp.set(Calendar.SECOND, 59);
//
//					tDauer = new java.sql.Time(cTemp.getTimeInMillis()
//							- cVon.getTimeInMillis());
//
//				}
//
//				tDauer.setTime(tDauer.getTime() - 3600000);
//				Double dDauer = Helper.time2Double(tDauer);
//
//				// BigDecimal diaeten = new BigDecimal(0);
//				// diaeten.setScale(2);
//				BigDecimal diaeten = BigDecimal.ZERO.setScale(2);
//				// try {
//				Diaeten temp = em.find(Diaeten.class, diaetenIId);
//				if (temp == null) {
//					throw new EJBExceptionLP(
//							EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
//				}
//				Integer landIId = temp.getLandIId();
//
//				try {
//					PartnerDto mandant = getMandantFac()
//							.mandantFindByPrimaryKey(theClientDto.getMandant(),
//									theClientDto).getPartnerDto();
//					if (mandant.getLandplzortDto() != null) {
//						if (!landIId.equals(mandant.getLandplzortDto()
//								.getIlandID())) {
//							bInland = false;
//						}
//					}
//				} catch (RemoteException ex) {
//					throwEJBExceptionLPRespectOld(ex);
//				}
//
////				Query query2 = em
////						.createNamedQuery("DiaetentagessatzfindGueltigenTagessatzZuDatum");
////				query2.setParameter(1, diaetenIId);
////				query2.setParameter(2, tVon);
////
////				DiaetentagessatzDto[] dtos = assembleDiaetentagessatzDtos(query2
////						.getResultList());
//
//				DiaetentagessatzDto[] dtos = getDiaetenTagesSatzDtos(diaetenIId, tVon);
//
//				if (dtos.length > 0) {
//					DiaetentagessatzDto dto = dtos[0];
//
//					if (dDauer.doubleValue() > 12) {
//						dDauer = new Double(12);
//					}
//					if (dDauer.doubleValue() % 1 > 0) {
//						dDauer = new Double(dDauer.doubleValue() + 1);
//
//					}
//
//					if (Helper.short2boolean(dto.getBStundenweise()) == true) {
//						if (dDauer.doubleValue() > dto.getIAbstunden()
//								.doubleValue()) {
//							diaeten = new BigDecimal(dDauer.intValue()
//									* dto.getNStundensatz().doubleValue());
//						}
//					} else {
//						if (dDauer.doubleValue() <= 3) {
//							diaeten = new BigDecimal(0);
//						} else {
//							diaeten = dto.getNStundensatz().multiply(
//									new BigDecimal(dDauer.intValue()));
//						}
//					}
//
//				}
//
//				// }
//				// catch (NoResultException e) {
//				// throw new
//				// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
//				// e);
//				// }
//
//				diaetenGesamt = diaetenGesamt.add(diaeten);
//
//				cVon.set(Calendar.DAY_OF_MONTH,
//						cVon.get(Calendar.DAY_OF_MONTH) + 1);
//				cVon.set(Calendar.HOUR_OF_DAY, 0);
//				cVon.set(Calendar.MINUTE, 0);
//				cVon.set(Calendar.MILLISECOND, 0);
//				cVon.set(Calendar.SECOND, 0);
//			}
//
//		}
//
//		return diaetenGesamt;
//	}

	public DiaetentagessatzDto[] getDiaetenTagesSatzDtos(Integer diaetenIId, java.sql.Timestamp tVon) {
		Query query2 = em
				.createNamedQuery("DiaetentagessatzfindGueltigenTagessatzZuDatum");
		query2.setParameter(1, diaetenIId);
		query2.setParameter(2, tVon);

		DiaetentagessatzDto[] dtos = assembleDiaetentagessatzDtos(query2
				.getResultList());
		return dtos;
	}

	private BigDecimal berechneAliquotenUrlaubsanspruchsstundenAb2009(
			Integer personalIId, java.sql.Date tAbrechnungszeitpunkt,
			Date tLetzterEintritt, TheClientDto theClientDto) {

		// Anspruch Aliquot lt. Zeitmodellen berechnen

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tAbrechnungszeitpunkt.getTime());
		// In DB eintragen
		int iJahr = c.get(Calendar.YEAR);

		try {
			UrlaubsanspruchDto dto = getPersonalFac()
					.urlaubsanspruchFindByPersonalIIdIJahr(personalIId,
							new Integer(iJahr));

			UrlaubsanspruchDto[] dtosFrueher = getPersonalFac()
					.urlaubsanspruchFindByPersonalIIdIJahrKleiner(personalIId,
							new Integer(iJahr));

			if (dto != null || (dtosFrueher != null && dtosFrueher.length > 0)) {
				BigDecimal iWochenUrlaub = new BigDecimal(5);
				if (dto != null) {
					iWochenUrlaub = new BigDecimal(dto
							.getFJahresurlaubinwochen().doubleValue());
				} else {
					iWochenUrlaub = new BigDecimal(dtosFrueher[0]
							.getFJahresurlaubinwochen().doubleValue());
				}

				Query query = em
						.createNamedQuery("PersonalzeitmodellfindZeitmodellImZeitraum");
				query.setParameter(1, personalIId);

				Calendar cErsterJaenner = Calendar.getInstance();
				cErsterJaenner.set(Calendar.YEAR, iJahr);
				cErsterJaenner.set(Calendar.MONTH, Calendar.JANUARY);
				cErsterJaenner.set(Calendar.DATE, 1);
				cErsterJaenner.set(Calendar.MINUTE, 0);
				cErsterJaenner.set(Calendar.HOUR_OF_DAY, 0);
				cErsterJaenner.set(Calendar.SECOND, 0);
				cErsterJaenner.set(Calendar.MILLISECOND, 0);

				boolean bErstNachErstemJaennerEingetreten = false;

				if (cErsterJaenner.getTimeInMillis() < tLetzterEintritt
						.getTime()) {
					cErsterJaenner.setTimeInMillis(tLetzterEintritt.getTime());
					bErstNachErstemJaennerEingetreten = true;
				}

				query.setParameter(
						2,
						new java.sql.Timestamp(cErsterJaenner.getTimeInMillis()));
				PersonalzeitmodellDto zeitmodellZumErstenJaenner = getPersonalFac()
						.personalzeitmodellFindZeitmodellZuDatum(
								personalIId,
								new Timestamp(cErsterJaenner.getTimeInMillis()),
								theClientDto);
				Double dSollstundenZumErstenJaenner = new Double(0);
				if (zeitmodellZumErstenJaenner != null) {
					dSollstundenZumErstenJaenner = getSummeSollzeitWochentagsUndSonntags(zeitmodellZumErstenJaenner
							.getZeitmodellIId());

				}

				Calendar cAbrechnungszeitpunkt = Calendar.getInstance();
				c.setTimeInMillis(tAbrechnungszeitpunkt.getTime());

				cAbrechnungszeitpunkt.set(Calendar.YEAR, iJahr);
				cAbrechnungszeitpunkt
						.set(Calendar.MONTH, c.get(Calendar.MONTH));
				cAbrechnungszeitpunkt.set(Calendar.DATE,
						c.get(Calendar.DAY_OF_MONTH));
				cAbrechnungszeitpunkt.set(Calendar.MINUTE, 0);
				cAbrechnungszeitpunkt.set(Calendar.HOUR_OF_DAY, 0);
				cAbrechnungszeitpunkt.set(Calendar.SECOND, 0);
				cAbrechnungszeitpunkt.set(Calendar.MILLISECOND, 0);
				PersonalzeitmodellDto zeitmodellZumAbrechungszeitpunkt = getPersonalFac()
						.personalzeitmodellFindZeitmodellZuDatum(
								personalIId,
								new Timestamp(cAbrechnungszeitpunkt
										.getTimeInMillis()), theClientDto);

				query.setParameter(3, new java.sql.Timestamp(
						cAbrechnungszeitpunkt.getTimeInMillis()));

				Collection<?> cl = query.getResultList();

				double fStunden = 0;

				int ianzahlTageImJahr = cAbrechnungszeitpunkt
						.getActualMaximum(Calendar.DAY_OF_YEAR);
				if (cl.size() > 0) {

					List<PersonalzeitmodellDto> list = new ArrayList<PersonalzeitmodellDto>();
					if (cl != null) {
						Iterator<?> iterator = cl.iterator();
						while (iterator.hasNext()) {
							Personalzeitmodell personalzeitmodell = (Personalzeitmodell) iterator
									.next();
							list.add(PersonalzeitmodellDtoAssembler
									.createDto(personalzeitmodell));
						}
					}

					PersonalzeitmodellDto[] temp = new PersonalzeitmodellDto[list
							.size()];
					PersonalzeitmodellDto[] returnArray = (PersonalzeitmodellDto[]) list
							.toArray(temp);
					for (int i = 0; i < returnArray.length; i++) {

						if (i == 0) {
							// && bErstNachErstemJaennerEingetreten == false) {

							// bis zum naechstenZeitmodell
							PersonalzeitmodellDto dtoNaechstesZeitmodell = returnArray[0];

							int iAnzahlTage = Helper.getDifferenzInTagen(
									new java.sql.Timestamp(cErsterJaenner
											.getTimeInMillis()),
									dtoNaechstesZeitmodell.getTGueltigab());

							// PJ 15884
							ZeitmodellDto zeitmodellDto = zeitmodellFindByPrimaryKey(
									dtoNaechstesZeitmodell.getZeitmodellIId(),
									theClientDto);

							fStunden = ((iWochenUrlaub.doubleValue() * iAnzahlTage) / ianzahlTageImJahr)
									* dSollstundenZumErstenJaenner;

						}

						if (i > 0 && i != returnArray.length) {
							// Vom letzten bis zum aktuellen

							PersonalzeitmodellDto letztes = returnArray[i - 1];
							PersonalzeitmodellDto aktuelles = returnArray[i];

							Double d = getSummeSollzeitWochentagsUndSonntags(letztes
									.getZeitmodellIId());

							int iAnzahlTage = Helper.getDifferenzInTagen(
									letztes.getTGueltigab(),
									aktuelles.getTGueltigab());

							// PJ 15884
							ZeitmodellDto zeitmodellDto = zeitmodellFindByPrimaryKey(
									letztes.getZeitmodellIId(), theClientDto);

							fStunden += ((iWochenUrlaub.doubleValue() * iAnzahlTage) / ianzahlTageImJahr)
									* d;

						}

						if (i == returnArray.length - 1) {

							// bis zum naechstenZeitmodell
							PersonalzeitmodellDto dtoNaechstesZeitmodell = returnArray[i];

							Double d = getSummeSollzeitWochentagsUndSonntags(dtoNaechstesZeitmodell
									.getZeitmodellIId());
							int iAnzahlTage = Helper.getDifferenzInTagen(
									dtoNaechstesZeitmodell.getTGueltigab(),
									new java.sql.Timestamp(
											cAbrechnungszeitpunkt
													.getTimeInMillis())) + 1;
							// PJ 15884
							ZeitmodellDto zeitmodellDto = zeitmodellFindByPrimaryKey(
									dtoNaechstesZeitmodell.getZeitmodellIId(),
									theClientDto);

							fStunden += ((iWochenUrlaub.doubleValue() * iAnzahlTage) / ianzahlTageImJahr)
									* d;

						}

					}

				} else {
					// Einfach berechnen mit Zeitmodell zum ersten
					int iAnzahlTage = Helper.getDifferenzInTagen(
							new Timestamp(cErsterJaenner.getTimeInMillis()),
							new java.sql.Timestamp(cAbrechnungszeitpunkt
									.getTimeInMillis())) + 1;
					fStunden = dSollstundenZumErstenJaenner.doubleValue()
							* iWochenUrlaub.doubleValue() / ianzahlTageImJahr
							* iAnzahlTage;
				}
				/*
				 * if (dto != null && dto.getFStundenzusaetzlich() != null) {
				 * fStunden += dto.getFStundenzusaetzlich().doubleValue(); }
				 */

				return new BigDecimal(fStunden);

			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return new BigDecimal(0);
	}

	private BigDecimal berechneUrlaubsanspruchsstundenAb2009(
			Integer personalIId, int iJahr, Date tLetzterEintritt,
			TheClientDto theClientDto) {

		// Anspruch lt. Zeitmodellen berechnen
		try {
			UrlaubsanspruchDto dto = getPersonalFac()
					.urlaubsanspruchFindByPersonalIIdIJahr(personalIId,
							new Integer(iJahr));

			UrlaubsanspruchDto[] dtosFrueher = getPersonalFac()
					.urlaubsanspruchFindByPersonalIIdIJahrKleiner(personalIId,
							new Integer(iJahr));

			if (dto != null || (dtosFrueher != null && dtosFrueher.length > 0)) {

				BigDecimal iWochenUrlaub = new BigDecimal(5);
				if (dto != null) {
					iWochenUrlaub = new BigDecimal(dto
							.getFJahresurlaubinwochen().doubleValue());
				} else {
					iWochenUrlaub = new BigDecimal(dtosFrueher[0]
							.getFJahresurlaubinwochen().doubleValue());
				}

				Query query = em
						.createNamedQuery("PersonalzeitmodellfindZeitmodellImZeitraum");
				query.setParameter(1, personalIId);

				Calendar cErsterJaenner = Calendar.getInstance();
				cErsterJaenner.set(Calendar.YEAR, iJahr);
				cErsterJaenner.set(Calendar.MONTH, Calendar.JANUARY);
				cErsterJaenner.set(Calendar.DATE, 1);
				cErsterJaenner.set(Calendar.MINUTE, 0);
				cErsterJaenner.set(Calendar.HOUR_OF_DAY, 0);
				cErsterJaenner.set(Calendar.SECOND, 0);
				cErsterJaenner.set(Calendar.MILLISECOND, 0);

				boolean bErstNachErstemJaennerEingetreten = false;

				// PJ14891
				ParametermandantDto parameter = null;
				try {
					parameter = (ParametermandantDto) getParameterFac()
							.getMandantparameter(
									theClientDto.getMandant(),
									ParameterFac.KATEGORIE_PERSONAL,
									ParameterFac.PARAMETER_ERWEITERTER_URLAUBSANSPRUCH);
				} catch (RemoteException ex5) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
				}

				int bErweiterterUrlaubsanspruch = ((Integer) parameter
						.getCWertAsObject());

				Date tLetzterEintrittOriginal = new Date(
						tLetzterEintritt.getTime());

				if (bErweiterterUrlaubsanspruch >= 1) {
					// Wenn vor 30.6. eingetreten oder TVOeD, dann ist das wie
					// wenn am 1.1.
					// eingetreten
					Calendar cEintritt = Calendar.getInstance();
					cEintritt.setTimeInMillis(tLetzterEintritt.getTime());

					if (cEintritt.get(Calendar.MONTH) <= Calendar.JUNE
							|| bErweiterterUrlaubsanspruch == 2) {
						tLetzterEintritt = new Timestamp(
								cErsterJaenner.getTimeInMillis());
					}

				}

				PersonalzeitmodellDto zeitmodellZumErstenJaenner = null;
				if (cErsterJaenner.getTimeInMillis() < tLetzterEintritt
						.getTime()) {
					zeitmodellZumErstenJaenner = getPersonalFac()
							.personalzeitmodellFindZeitmodellZuDatum(
									personalIId,
									new Timestamp(tLetzterEintritt.getTime()),
									theClientDto);

					query.setParameter(2, tLetzterEintritt);

					cErsterJaenner.setTimeInMillis(tLetzterEintritt.getTime());

					bErstNachErstemJaennerEingetreten = true;
				} else {
					zeitmodellZumErstenJaenner = getPersonalFac()
							.personalzeitmodellFindZeitmodellZuDatum(
									personalIId,
									new Timestamp(cErsterJaenner
											.getTimeInMillis()), theClientDto);
					query.setParameter(
							2,
							new java.sql.Timestamp(cErsterJaenner
									.getTimeInMillis()));
				}

				Double dSollstundenZumErstenJaenner = new Double(0);
				if (zeitmodellZumErstenJaenner != null) {
					dSollstundenZumErstenJaenner = getSummeSollzeitWochentagsUndSonntags(zeitmodellZumErstenJaenner
							.getZeitmodellIId());

				}

				Calendar c31Dezember = Calendar.getInstance();

				c31Dezember.set(Calendar.YEAR, iJahr);
				c31Dezember.set(Calendar.MONTH, Calendar.DECEMBER);
				c31Dezember.set(Calendar.DATE, 31);
				c31Dezember.set(Calendar.MINUTE, 0);
				c31Dezember.set(Calendar.HOUR_OF_DAY, 0);
				c31Dezember.set(Calendar.SECOND, 0);
				c31Dezember.set(Calendar.MILLISECOND, 0);
				PersonalzeitmodellDto zeitmodellZumLetztenDezember = getPersonalFac()
						.personalzeitmodellFindZeitmodellZuDatum(personalIId,
								new Timestamp(c31Dezember.getTimeInMillis()),
								theClientDto);

				query.setParameter(3,
						new java.sql.Timestamp(c31Dezember.getTimeInMillis()));

				Collection<?> cl = query.getResultList();

				double fStunden = 0;
				double fTage = 0;

				int ianzahlTageImJahr = c31Dezember
						.getActualMaximum(Calendar.DAY_OF_YEAR);

				if (cl.size() > 0) {

					List<PersonalzeitmodellDto> list = new ArrayList<PersonalzeitmodellDto>();
					if (cl != null) {
						Iterator<?> iterator = cl.iterator();
						while (iterator.hasNext()) {
							Personalzeitmodell personalzeitmodell = (Personalzeitmodell) iterator
									.next();
							list.add(PersonalzeitmodellDtoAssembler
									.createDto(personalzeitmodell));
						}
					}

					PersonalzeitmodellDto[] temp = new PersonalzeitmodellDto[list
							.size()];
					PersonalzeitmodellDto[] returnArray = (PersonalzeitmodellDto[]) list
							.toArray(temp);
					for (int i = 0; i < returnArray.length; i++) {

						if (i == 0) {

							// bis zum naechstenZeitmodell
							PersonalzeitmodellDto dtoNaechstesZeitmodell = returnArray[0];

							int iAnzahlTage = Helper.getDifferenzInTagen(
									new java.sql.Timestamp(cErsterJaenner
											.getTimeInMillis()),
									dtoNaechstesZeitmodell.getTGueltigab());
							// +1 entfernt, da, wenn kein Zeitmodell
							// vorhanden
							// ist, 366 Tage im Jahr gerechnet werden

							// PJ 15884
							if (zeitmodellZumErstenJaenner != null) {
								ZeitmodellDto zeitmodellDto = zeitmodellFindByPrimaryKey(
										zeitmodellZumErstenJaenner
												.getZeitmodellIId(),
										theClientDto);

								fStunden = ((iWochenUrlaub.doubleValue() * iAnzahlTage) / ianzahlTageImJahr)
										* dSollstundenZumErstenJaenner;
								fTage = ((iWochenUrlaub.doubleValue() * iAnzahlTage) / ianzahlTageImJahr)
										* zeitmodellDto
												.getFUrlaubstageprowoche();
							}
						}

						if (i > 0 && i != returnArray.length) {
							// Vom letzten bis zum aktuellen

							PersonalzeitmodellDto letztes = returnArray[i - 1];
							PersonalzeitmodellDto aktuelles = returnArray[i];

							Double d = getSummeSollzeitWochentagsUndSonntags(letztes
									.getZeitmodellIId());

							int iAnzahlTage = Helper.getDifferenzInTagen(
									letztes.getTGueltigab(),
									aktuelles.getTGueltigab());

							// PJ 15884
							ZeitmodellDto zeitmodellDto = zeitmodellFindByPrimaryKey(
									letztes.getZeitmodellIId(), theClientDto);

							fStunden += ((iWochenUrlaub.doubleValue() * iAnzahlTage) / ianzahlTageImJahr)
									* d;

							fTage += ((iWochenUrlaub.doubleValue() * iAnzahlTage) / ianzahlTageImJahr)
									* zeitmodellDto.getFUrlaubstageprowoche();
						}

						if (i == returnArray.length - 1) {

							// bis zum naechstenZeitmodell
							PersonalzeitmodellDto dtoNaechstesZeitmodell = returnArray[i];

							Double d = getSummeSollzeitWochentagsUndSonntags(dtoNaechstesZeitmodell
									.getZeitmodellIId());
							int iAnzahlTage = Helper.getDifferenzInTagen(
									dtoNaechstesZeitmodell.getTGueltigab(),
									new java.sql.Timestamp(c31Dezember
											.getTimeInMillis())) + 1;

							// PJ 15884
							ZeitmodellDto zeitmodellDto = zeitmodellFindByPrimaryKey(
									dtoNaechstesZeitmodell.getZeitmodellIId(),
									theClientDto);

							fStunden += ((iWochenUrlaub.doubleValue() * iAnzahlTage) / ianzahlTageImJahr)
									* d;
							fTage += ((iWochenUrlaub.doubleValue() * iAnzahlTage) / ianzahlTageImJahr)
									* zeitmodellDto.getFUrlaubstageprowoche();
						}

					}

				} else {
					// Einfach berechnen mit Zeitmodell zum ersten
					if (zeitmodellZumErstenJaenner != null) {
						ZeitmodellDto zeitmodellDto = zeitmodellFindByPrimaryKey(
								zeitmodellZumErstenJaenner.getZeitmodellIId(),
								theClientDto);

						if (bErstNachErstemJaennerEingetreten == false) {
							fStunden = dSollstundenZumErstenJaenner
									.doubleValue()
									* iWochenUrlaub.doubleValue();

							if (zeitmodellZumErstenJaenner != null) {

								fTage = iWochenUrlaub.doubleValue()
										* zeitmodellDto
												.getFUrlaubstageprowoche();
							}
						} else {

							int iAnzahlTage = Helper.getDifferenzInTagen(
									tLetzterEintritt, new java.sql.Timestamp(
											c31Dezember.getTimeInMillis())) + 1;

							fStunden = ((iWochenUrlaub.doubleValue() * iAnzahlTage) / ianzahlTageImJahr)
									* dSollstundenZumErstenJaenner;
							fTage = ((iWochenUrlaub.doubleValue() * iAnzahlTage) / ianzahlTageImJahr)
									* zeitmodellDto.getFUrlaubstageprowoche();
						}
					}

				}

				// TVOeD: PJ 16677
				if (bErweiterterUrlaubsanspruch == 2) {

					Calendar cEintritt = Calendar.getInstance();
					cEintritt.setTimeInMillis(tLetzterEintrittOriginal
							.getTime());

					if (cEintritt.get(Calendar.YEAR) == iJahr) {

						double dMonate = 11 - cEintritt.get(Calendar.MONTH);
						if (cEintritt.get(Calendar.DAY_OF_MONTH) == 1) {
							dMonate = dMonate + 1;
						}

						fTage = fTage / 12 * dMonate;
						fStunden = fStunden / 12 * dMonate;
					}

					// Kaufmaennich auf ganze Tage runden
					fTage = Math.round(fTage);

				} else {
					// Tage auf 2 Nachkommastellen runden, da ansonsten bei 22
					// Zeitmodellen 29,999996 rauskommt und spaeter
					fTage = Math.round(fTage * 100.) / 100.;
				}

				if (dto == null) {
					UrlaubsanspruchDto[] dtos = getPersonalFac()
							.urlaubsanspruchFindByPersonalIIdIJahrKleiner(
									personalIId, new Integer(iJahr));

					if (dtos.length > 0) {
						dto = dtos[0];

						if (dto.getIJahr() >= 2009) {

							dto.setIId(null);

							dto.setIJahr(iJahr);
							dto.setFStundenzusaetzlich(0D);
							dto.setFTagezusaetzlich(0D);
							dto.setFStunden(fStunden);
							dto.setFTage(fTage);
							getPersonalFac().createUrlaubsanspruch(dto,
									theClientDto);
						}

					}

				} else {
					// Stunden heuer updaten
					dto.setFStunden(fStunden);
					dto.setFTage(fTage);
					getPersonalFac().updateUrlaubsanspruch(dto, theClientDto);
				}

			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return null;
	}

	/**
	 * Berechnen des Urlaubsanspruches einer Person Eintrittsdatum bis heute
	 * oder einem best. Datum
	 *
	 * Wenn Urlaubsabrechnung nach Gesch&auml;ftsjahr, dann muss f&uuml;r das
	 * Eintrittsjahr der entstprechende Urlaub eingegeben werden
	 *
	 * @param personalIId
	 *            des partners
	 * @param dAbrechnungzeitpunkt
	 *            Berechnung bis
	 * @param theClientDto
	 *            User-ID
	 * @return UrlaubsabrechnungDto
	 * @exception EJBExceptionLP
	 */
	public UrlaubsabrechnungDto berechneUrlaubsAnspruch(Integer personalIId,
			java.sql.Date dAbrechnungzeitpunkt, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (personalIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("personalIId == null"));
		}
		if (dAbrechnungzeitpunkt == null) {
			dAbrechnungzeitpunkt = new java.sql.Date(System.currentTimeMillis()); // heute
		}

		Date dEintrittsdatum = null;

		Date dAustrittsdatum = null;

		// Hole letztes Eintrittsdatum
		try {
			EintrittaustrittDto eaDto = getPersonalFac()
					.eintrittaustrittFindLetztenEintrittBisDatum(personalIId,
							new Timestamp(dAbrechnungzeitpunkt.getTime()));
			if (eaDto != null) {
				dEintrittsdatum = eaDto.getTEintritt();
				dAustrittsdatum = eaDto.getTAustritt();
			}
		} catch (RemoteException ex3) {
			throwEJBExceptionLPRespectOld(ex3);
		}

		// /-------------
		Calendar cTemp = Calendar.getInstance();
		cTemp.setTimeInMillis(dAbrechnungzeitpunkt.getTime());

		Calendar c = Calendar.getInstance();
		c.setTime(dEintrittsdatum);
		int iEintrittsjahr = c.get(Calendar.YEAR);

		c.setTime(dAbrechnungzeitpunkt);
		int iAkttuellesjahr = c.get(Calendar.YEAR);

		double i_stunden_resturlaub = 0;
		double i_tage_resturlaub = 0;

		// Resturlaub aus dem Vorjahr holen
		if (iAkttuellesjahr > iEintrittsjahr) {
			try {
				UrlaubsanspruchDto[] urlaubsanspruchDto = getPersonalFac()
						.urlaubsanspruchFindByPersonalIIdIJahrKleiner(
								personalIId, iAkttuellesjahr - 1);
				if (urlaubsanspruchDto != null
						&& urlaubsanspruchDto.length > 0
						&& urlaubsanspruchDto[0].getIJahr().intValue() == iAkttuellesjahr - 1
						&& urlaubsanspruchDto[0]
								.getFResturlaubjahresendestunden() != null
						&& urlaubsanspruchDto[0].getFResturlaubjahresendetage() != null) {
					// nix
					i_stunden_resturlaub = urlaubsanspruchDto[0]
							.getFResturlaubjahresendestunden();
					i_tage_resturlaub = urlaubsanspruchDto[0]
							.getFResturlaubjahresendetage();
				} else {

					// Vorjahr berechnen
					Calendar cVorjahr = Calendar.getInstance();
					cVorjahr.set(iAkttuellesjahr - 1, Calendar.DECEMBER, 31,
							23, 59);
					berechneUrlaubsAnspruch(personalIId, new java.sql.Date(
							cVorjahr.getTimeInMillis()), theClientDto);

					// Nun sollte es fuer das letzte jahr einen uebertrag geben
					urlaubsanspruchDto = getPersonalFac()
							.urlaubsanspruchFindByPersonalIIdIJahrKleiner(
									personalIId, iAkttuellesjahr - 1);
					if (urlaubsanspruchDto != null
							&& urlaubsanspruchDto.length > 0) {
						i_stunden_resturlaub = 0;
						if (urlaubsanspruchDto[0]
								.getFResturlaubjahresendestunden() != null) {
							i_stunden_resturlaub = urlaubsanspruchDto[0]
									.getFResturlaubjahresendestunden();
						}
						i_tage_resturlaub = 0;
						if (urlaubsanspruchDto[0]
								.getFResturlaubjahresendetage() != null) {
							i_tage_resturlaub = urlaubsanspruchDto[0]
									.getFResturlaubjahresendetage();
						}

					}
				}
			} catch (RemoteException ex4) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex4);
			}
		}
		// ----------------

		// Hole Personal
		PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(
				personalIId, theClientDto);

		java.sql.Timestamp dAktuellerUrlaubsbeginn = null;

		c.setTime(dAbrechnungzeitpunkt);
		c.set(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);
		dAktuellerUrlaubsbeginn = new java.sql.Timestamp(c.getTime().getTime()); // aktueller
		// Geschaeftsjahreswechsel
		// Fuer erstes Jahr muss ein separater Eintrag angelegt werden
		// darauffolgendes Jahr muss extra definiert werden.

		// 2 Neue Parameter: Geschaeftsjahresbeginn (Monat (1-12)) und
		// B_VOREILEND*/

		dAktuellerUrlaubsbeginn = Helper.cutTimestamp(dAktuellerUrlaubsbeginn);
		// PJ 14091
		if (dEintrittsdatum != null
				&& dEintrittsdatum.after(dAktuellerUrlaubsbeginn)) {
			dAktuellerUrlaubsbeginn = Helper.cutTimestamp(new Timestamp(
					dEintrittsdatum.getTime()));
		}

		UrlaubsabrechnungDto urlaubsabrechnungDto = new UrlaubsabrechnungDto();
		urlaubsabrechnungDto
				.setNAktuellerUrlaubsanspruchStunden(new BigDecimal(0));
		urlaubsabrechnungDto
				.setNAktuellerUrlaubVerbrauchtStunden(new BigDecimal(0));
		urlaubsabrechnungDto.setNAlterUrlaubsanspruchStunden(new BigDecimal(0));
		urlaubsabrechnungDto.setNGeplanterUrlaubStunden(new BigDecimal(0));
		urlaubsabrechnungDto.setNVerfuegbarerUrlaubStunden(new BigDecimal(0));

		urlaubsabrechnungDto
				.setNAktuellerUrlaubsanspruchTage(new BigDecimal(0));
		urlaubsabrechnungDto
				.setNAktuellerUrlaubVerbrauchtTage(new BigDecimal(0));
		urlaubsabrechnungDto.setNAlterUrlaubsanspruchTage(new BigDecimal(0));
		urlaubsabrechnungDto.setNGeplanterUrlaubTage(new BigDecimal(0));
		urlaubsabrechnungDto.setNVerfuegbarerUrlaubTage(new BigDecimal(0));
		urlaubsabrechnungDto.setDAbrechnungszeitpunkt(dAbrechnungzeitpunkt);

		// --- TAGESWEISE BETRACHTUNG ----
		// Ganze Urlaubstage ALT holen
		urlaubsabrechnungDto.setNAlterUrlaubsanspruchTage(new BigDecimal(
				i_tage_resturlaub));

		// Ganze Urlaubstage AKTUELL holen
		urlaubsabrechnungDto
				.setNAktuellerUrlaubVerbrauchtTage(getAktuellerUrlaubTageweise(
						personalIId, new java.sql.Timestamp(
								dAktuellerUrlaubsbeginn.getTime()),
						new java.sql.Timestamp(dAbrechnungzeitpunkt.getTime())));

		// Ganze Urlaubstage GEPLANT holen
		urlaubsabrechnungDto
				.setNGeplanterUrlaubTage(getGeplanterUrlaubTageweise(
						personalIId, new java.sql.Timestamp(
								dAbrechnungzeitpunkt.getTime())));

		// --- STUNDENWEISE BETRACHTUNG ----

		Integer tagesartIId_Feiertag = tagesartFindByCNr(
				ZeiterfassungFac.TAGESART_FEIERTAG, theClientDto).getIId();
		Integer tagesartIId_Halbtag = tagesartFindByCNr(
				ZeiterfassungFac.TAGESART_HALBTAG, theClientDto).getIId();

		// Urlaubsstunden ALT holen
		urlaubsabrechnungDto.setNAlterUrlaubsanspruchTage(new BigDecimal(
				i_stunden_resturlaub));
		// Urlaubsstunden AKTUELL holen
		urlaubsabrechnungDto
				.setNAktuellerUrlaubVerbrauchtStunden(getAktuelleUrlaubStunden(
						personalIId, dAktuellerUrlaubsbeginn,
						new java.sql.Timestamp(dAbrechnungzeitpunkt.getTime()),
						tagesartIId_Feiertag, tagesartIId_Halbtag, theClientDto));
		// Urlaubsstunden GEPLANT holen
		urlaubsabrechnungDto
				.setNGeplanterUrlaubStunden(getGeplanteUrlaubStunden(
						personalDto, new java.sql.Timestamp(
								dAbrechnungzeitpunkt.getTime()),
						tagesartIId_Feiertag, tagesartIId_Halbtag, theClientDto));

		// AB HIER BEGINNT BERECHNUNG DES URLAUBSANSPRUCHES

		// Beginn und Endjahr des alten Urlaubsanspruches setzen
		c.setTime(dEintrittsdatum);
		int i_jahr_rest_beginn = c.get(Calendar.YEAR);

		double dAnzahlDerTageDesJahres = c
				.getActualMaximum(Calendar.DAY_OF_YEAR);
		double dTagUrlaubsbeginn = c.get(Calendar.DAY_OF_YEAR) - 1;

		c.setTime(dAktuellerUrlaubsbeginn);
		c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 1);
		int i_jahr_rest_ende = c.get(Calendar.YEAR);

		if (iAkttuellesjahr >= 2009) {
			// Ab 2009 werden die Urlaubsanspruchsstunden lt. PJ 13282 gerechnet

			berechneUrlaubsanspruchsstundenAb2009(personalIId, iAkttuellesjahr,
					dEintrittsdatum, theClientDto);

		}

		// PJ14891
		ParametermandantDto parameter = null;
		try {
			parameter = (ParametermandantDto) getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_PERSONAL,
							ParameterFac.PARAMETER_ERWEITERTER_URLAUBSANSPRUCH);
		} catch (RemoteException ex5) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
		}

		int bErweiterterUrlaubsanspruch = ((Integer) parameter
				.getCWertAsObject());

		// Jahr des Aktuellen-Urlaubsanspruches
		c.setTime(dAktuellerUrlaubsbeginn);
		int iJahr = c.get(Calendar.YEAR);
		UrlaubsanspruchDto[] urlaubsanspruchDtos = null;
		try {
			urlaubsanspruchDtos = getPersonalFac()
					.urlaubsanspruchFindByPersonalIIdIJahrKleiner(personalIId,
							new Integer(iJahr));

			if (urlaubsanspruchDtos.length > 0) {

				if (iJahr == i_jahr_rest_beginn
						&& bErweiterterUrlaubsanspruch == 1) {

					Calendar cX = Calendar.getInstance();
					cX.setTimeInMillis(dEintrittsdatum.getTime());

					BigDecimal anspruchGesamt = new BigDecimal(
							(urlaubsanspruchDtos[0].getFTage().doubleValue() + urlaubsanspruchDtos[0]
									.getFTagezusaetzlich().doubleValue()));

					BigDecimal anspruchGesamtStunden = new BigDecimal(
							(urlaubsanspruchDtos[0].getFStunden().doubleValue() + urlaubsanspruchDtos[0]
									.getFStundenzusaetzlich().doubleValue()));

					BigDecimal fFaktorFuerErweitertenUrlaubsanspruch =

					anspruchGesamtStunden.divide(anspruchGesamt, 4,
							BigDecimal.ROUND_HALF_EVEN);

					if (cX.get(Calendar.MONTH) > Calendar.JUNE) {

						anspruchGesamt = anspruchGesamt
								.subtract(new BigDecimal(
										cX.get(Calendar.MONTH) * 2));

						anspruchGesamtStunden = anspruchGesamtStunden
								.subtract(new BigDecimal(cX.get(Calendar.MONTH)
										* 2
										* fFaktorFuerErweitertenUrlaubsanspruch
												.doubleValue()));
					}

					// Aliquote Urlaubsstunden
					double dAliquoterAnspruch = 0;

					Calendar cEintritt = Calendar.getInstance();
					cEintritt.setTime(dEintrittsdatum);

					double iAktuelleTagDesJahres = cTemp
							.get(Calendar.DAY_OF_YEAR);
					double anzahlTageDesJahres = c
							.getActualMaximum(Calendar.DAY_OF_YEAR);
					double urlaub = 0;

					double iAbTagUrlaubsansprung = cEintritt
							.get(Calendar.DAY_OF_YEAR);

					if (iAktuelleTagDesJahres > iAbTagUrlaubsansprung) {
						double urlaubsansprunhProTag = (anspruchGesamtStunden
								.doubleValue() / (anzahlTageDesJahres - iAbTagUrlaubsansprung));

						dAliquoterAnspruch = ((iAktuelleTagDesJahres - iAbTagUrlaubsansprung) * urlaubsansprunhProTag);
					} else {
						dAliquoterAnspruch = 0;
					}

					urlaubsabrechnungDto
							.setNAliquoterAnspruchStunden(new BigDecimal(
									dAliquoterAnspruch));

					urlaubsabrechnungDto
							.setNAktuellerUrlaubsanspruchTage(rundeUrlaubstageAnhandParameter(
									anspruchGesamt, theClientDto));

					urlaubsabrechnungDto
							.setNAktuellerUrlaubsanspruchStunden(anspruchGesamtStunden);

				} else {

					urlaubsabrechnungDto
							.setNAktuellerUrlaubsanspruchTage(rundeUrlaubstageAnhandParameter(
									new BigDecimal(urlaubsanspruchDtos[0]
											.getFTage().doubleValue()
											+ urlaubsanspruchDtos[0]
													.getFTagezusaetzlich()
													.doubleValue()),
									theClientDto));
					urlaubsabrechnungDto
							.setNAktuellerUrlaubsanspruchStunden(Helper
									.rundeKaufmaennisch(
											new BigDecimal(
													urlaubsanspruchDtos[0]
															.getFStunden()
															.doubleValue()
															+ urlaubsanspruchDtos[0]
																	.getFStundenzusaetzlich()
																	.doubleValue()),
											2));

					if (dAustrittsdatum != null) {
						// SP1042
						Calendar cAustritt = Calendar.getInstance();
						cAustritt.setTime(dAustrittsdatum);
						double anzahlTageDesJahres = cAustritt
								.getActualMaximum(Calendar.DAY_OF_YEAR);

						Calendar cEndeDesJahres = Calendar.getInstance();
						cEndeDesJahres.set(Calendar.MONTH, Calendar.DECEMBER);
						cEndeDesJahres.set(Calendar.DAY_OF_MONTH, 31);

						if (cAustritt.get(Calendar.YEAR) == iAkttuellesjahr) {

							int iTageUrlaub = 0;
							if (iEintrittsjahr == iAkttuellesjahr) {

								anzahlTageDesJahres = Helper
										.ermittleTageEinesZeitraumes(
												new java.sql.Date(
														dEintrittsdatum
																.getTime()),
												new java.sql.Date(
														cEndeDesJahres
																.getTime()
																.getTime()));

								iTageUrlaub = Helper
										.ermittleTageEinesZeitraumes(
												new java.sql.Date(
														dEintrittsdatum
																.getTime()),
												new java.sql.Date(
														dAustrittsdatum
																.getTime()));
							} else {

								cAustritt.set(Calendar.MONTH, Calendar.JANUARY);
								cAustritt.set(Calendar.DAY_OF_MONTH, 1);
								iTageUrlaub = Helper
										.ermittleTageEinesZeitraumes(
												new java.sql.Date(cAustritt
														.getTimeInMillis()),
												new java.sql.Date(
														dAustrittsdatum
																.getTime()));
							}
							anzahlTageDesJahres++;
							iTageUrlaub++;

							urlaubsabrechnungDto
									.setNAktuellerUrlaubsanspruchTage(rundeUrlaubstageAnhandParameter(
											urlaubsabrechnungDto
													.getNAktuellerUrlaubsanspruchTage()
													.multiply(
															new BigDecimal(
																	iTageUrlaub
																			/ anzahlTageDesJahres)),
											theClientDto));

							urlaubsabrechnungDto
									.setNAktuellerUrlaubsanspruchStunden(urlaubsabrechnungDto
											.getNAktuellerUrlaubsanspruchStunden()
											.multiply(
													new BigDecimal(
															iTageUrlaub
																	/ anzahlTageDesJahres)));

						}

					}

				}

				// Ganze Urlaubstage AKTUELL ALIQUOT holen
				Double dAliquotTageweise = getAliquoterUrlaubsanspruchTageweise(
						new java.sql.Timestamp(dAbrechnungzeitpunkt.getTime()),
						urlaubsanspruchDtos[0].getFTage(), dEintrittsdatum,
						theClientDto);
				urlaubsabrechnungDto.setNAliquoterAnspruchTage(new BigDecimal(
						dAliquotTageweise.doubleValue()
								+ urlaubsanspruchDtos[0].getFTagezusaetzlich()
										.doubleValue()));

				BigDecimal bdAliquotStunden = null;
				if (bErweiterterUrlaubsanspruch > 0
						&& iJahr == i_jahr_rest_beginn) {
					// Muesste schon vorher berechnet worden sein
					if (urlaubsabrechnungDto.getNAliquoterAnspruchStunden() == null) {
						urlaubsabrechnungDto
								.setNAliquoterAnspruchStunden(new BigDecimal(0));
					}

				} else {

					if (iAkttuellesjahr >= 2009) {
						bdAliquotStunden = berechneAliquotenUrlaubsanspruchsstundenAb2009(
								personalIId, dAbrechnungzeitpunkt,
								dEintrittsdatum, theClientDto);
					} else {
						// Urlaubsstunden AKTUELL ALIQUOT holen

						bdAliquotStunden = getAliquoterUrlaubsanspruchStunden(
								new java.sql.Timestamp(
										dAbrechnungzeitpunkt.getTime()),
								urlaubsanspruchDtos[0].getFStunden(),
								theClientDto, dEintrittsdatum);
					}

					// PJ 14082
					bdAliquotStunden = bdAliquotStunden.add(new BigDecimal(
							urlaubsanspruchDtos[0].getFStundenzusaetzlich()
									.doubleValue()));

					urlaubsabrechnungDto
							.setNAliquoterAnspruchStunden(bdAliquotStunden);

				}

			} else {
				urlaubsabrechnungDto
						.setNAliquoterAnspruchTage(new BigDecimal(0));
				urlaubsabrechnungDto
						.setNAliquoterAnspruchStunden(new BigDecimal(0));

			}

		} catch (RemoteException ex6) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex6);
		}

		// Resturlaub

		urlaubsabrechnungDto.setNAlterUrlaubsanspruchTage(Helper
				.rundeKaufmaennisch(new BigDecimal(i_tage_resturlaub), 2));
		urlaubsabrechnungDto.setNAlterUrlaubsanspruchStunden(Helper
				.rundeKaufmaennisch(new BigDecimal(i_stunden_resturlaub), 2));

		// Verfuegbaren Tageweisen Urlaub berechnen
		BigDecimal bdVerfuegbarAltTage = new BigDecimal(i_tage_resturlaub);
		BigDecimal bdVerfuegbarAktuellTage = urlaubsabrechnungDto
				.getNAliquoterAnspruchTage().subtract(
						urlaubsabrechnungDto
								.getNAktuellerUrlaubVerbrauchtTage());

		urlaubsabrechnungDto.setNVerfuegbarerUrlaubTage(bdVerfuegbarAltTage
				.add(bdVerfuegbarAktuellTage));

		// Verfuegbaren Stundenweisen Urlaub berechnen
		BigDecimal bdVerfuegbarAltStunden = new BigDecimal(i_stunden_resturlaub);
		BigDecimal bdVerfuegbarAktuellStunden = urlaubsabrechnungDto
				.getNAliquoterAnspruchStunden().subtract(
						urlaubsabrechnungDto
								.getNAktuellerUrlaubVerbrauchtStunden());

		urlaubsabrechnungDto
				.setNVerfuegbarerUrlaubStunden(bdVerfuegbarAltStunden
						.add(bdVerfuegbarAktuellStunden));

		// Wenn Dezember, dann fuers aktuelle Jahr niederschreiben

		Calendar cTempDezember = Calendar.getInstance();
		cTempDezember.setTimeInMillis(dAbrechnungzeitpunkt.getTime());

		if (cTempDezember.get(Calendar.MONTH) == Calendar.DECEMBER
				&& cTempDezember.get(Calendar.DAY_OF_MONTH) == 31) {
			try {
				UrlaubsanspruchDto[] urlaubsanspruchDto = getPersonalFac()
						.urlaubsanspruchFindByPersonalIIdIJahrKleiner(
								personalIId, iAkttuellesjahr);

				if (urlaubsanspruchDto != null && urlaubsanspruchDto.length > 0) {

					if (urlaubsanspruchDto[0].getIJahr().intValue() == iAkttuellesjahr) {

						if (Helper.short2boolean(urlaubsanspruchDto[0]
								.getBGesperrt()) == false) {
							urlaubsanspruchDto[0]
									.setFResturlaubjahresendestunden(urlaubsabrechnungDto
											.getNAliquoterAnspruchStunden()
											.doubleValue()
											+ urlaubsabrechnungDto
													.getNAlterUrlaubsanspruchStunden()
													.doubleValue()
											- urlaubsabrechnungDto
													.getNAktuellerUrlaubVerbrauchtStunden()
													.doubleValue());

							Double dUrlaubIntagen = urlaubsabrechnungDto
									.getNAliquoterAnspruchTage().doubleValue()
									+ urlaubsabrechnungDto
											.getNAlterUrlaubsanspruchTage()
											.doubleValue()
									- urlaubsabrechnungDto
											.getNAktuellerUrlaubVerbrauchtTage()
											.doubleValue();

							// SP1984

							urlaubsanspruchDto[0]
									.setFResturlaubjahresendetage(rundeUrlaubstageAnhandParameter(
											new BigDecimal(dUrlaubIntagen),
											theClientDto).doubleValue());
							getPersonalFac().updateUrlaubsanspruch(
									urlaubsanspruchDto[0], theClientDto);
						}

					} else {
						// Wenns fuer heuer keine gibt, den letzten kopieren
						urlaubsanspruchDto[0].setIJahr(iAkttuellesjahr);
						urlaubsanspruchDto[0].setBGesperrt(Helper
								.boolean2Short(false));
						urlaubsanspruchDto[0]
								.setFStundenzusaetzlich(new Double(0));
						urlaubsanspruchDto[0]
								.setFTagezusaetzlich(new Double(0));
						urlaubsanspruchDto[0]
								.setFResturlaubjahresendestunden(urlaubsabrechnungDto
										.getNAliquoterAnspruchStunden()
										.doubleValue()
										- urlaubsabrechnungDto
												.getNAktuellerUrlaubVerbrauchtStunden()
												.doubleValue());

						// SP1984

						Double dUrlaubIntagen = urlaubsabrechnungDto
								.getNAliquoterAnspruchTage().doubleValue()
								- urlaubsabrechnungDto
										.getNAktuellerUrlaubVerbrauchtTage()
										.doubleValue();
						urlaubsanspruchDto[0]
								.setFResturlaubjahresendetage(rundeUrlaubstageAnhandParameter(
										new BigDecimal(dUrlaubIntagen),
										theClientDto).doubleValue());
						getPersonalFac().createUrlaubsanspruch(
								urlaubsanspruchDto[0], theClientDto);
					}

				} else {
					// geht nicht, da ueberhaupt noch nie ein Anspruch
					// definiert wurde
				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}

		return urlaubsabrechnungDto;
	}

	private TaetigkeitDto[] getAllBenutzerdefinierteSondertaetigkeiten() {
		// try {

		Query query = em.createNamedQuery("TaetigkeitfindByTaetigkeitartCNr");
		query.setParameter(1, ZeiterfassungFac.TAETIGKEITART_SONDERTAETIGKEIT);
		Collection<?> c = query.getResultList();
		// if (c.isEmpty()) {
		// return new TaetigkeitDto[0];
		// }
		List<TaetigkeitDto> list = new ArrayList<TaetigkeitDto>();
		if (c != null) {
			Iterator<?> iterator = c.iterator();
			while (iterator.hasNext()) {
				Taetigkeit taetigkeit = (Taetigkeit) iterator.next();

				if (taetigkeit.getCNr()
						.equals(ZeiterfassungFac.TAETIGKEIT_ARZT)
						|| taetigkeit.getCNr().equals(
								ZeiterfassungFac.TAETIGKEIT_BEHOERDE)
						|| taetigkeit.getCNr().equals(
								ZeiterfassungFac.TAETIGKEIT_ENDE)
						|| taetigkeit.getCNr().equals(
								ZeiterfassungFac.TAETIGKEIT_GEHT)
						|| taetigkeit.getCNr().equals(
								ZeiterfassungFac.TAETIGKEIT_KOMMT)
						|| taetigkeit.getCNr().equals(
								ZeiterfassungFac.TAETIGKEIT_KRANK)
						|| taetigkeit.getCNr().equals(
								ZeiterfassungFac.TAETIGKEIT_UNTER)
						|| taetigkeit.getCNr().equals(
								ZeiterfassungFac.TAETIGKEIT_URLAUB)
						|| taetigkeit.getCNr().equals(
								ZeiterfassungFac.TAETIGKEIT_ZEITAUSGLEICH)) {

				} else {
					list.add(assembleTaetigkeitDto(taetigkeit));
				}

			}
		}

		TaetigkeitDto[] returnArray = new TaetigkeitDto[list.size()];
		return (TaetigkeitDto[]) list.toArray(returnArray);

	}

	public BigDecimal getStundenAllerBezahltenSondertaetigkeitenImZeitraum(
			Integer personalIId, Integer tagesartIId_Feiertag,
			Integer tagesartIId_Halbtag, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, TheClientDto theClientDto) {
		TaetigkeitDto[] taetigkeitenDtos = getAllSondertaetigkeitenOhneKommtUndGeht(theClientDto);

		double dGesamtdauer = 0;

		for (int j = 0; j < taetigkeitenDtos.length; j++) {

			if (taetigkeitenDtos[j].getFBezahlt() > 0) {
				double dDauer = getGesamtDauerEinerSondertaetigkeitImZeitraum(
						personalIId, tVon, tBis, taetigkeitenDtos[j].getIId(),
						theClientDto, tagesartIId_Feiertag, tagesartIId_Halbtag);

				dDauer = dDauer * (taetigkeitenDtos[j].getFBezahlt() / 100);

				dGesamtdauer += dDauer;
			}
		}

		return new BigDecimal(dGesamtdauer);
	}

	private TaetigkeitDto[] getAllSondertaetigkeitenOhneKommtUndGeht(
			TheClientDto theClientDto) {
		// try {

		Query query = em.createNamedQuery("TaetigkeitfindByTaetigkeitartCNr");
		query.setParameter(1, ZeiterfassungFac.TAETIGKEITART_SONDERTAETIGKEIT);
		Collection<?> c = query.getResultList();
		List<TaetigkeitDto> list = new ArrayList<TaetigkeitDto>();
		if (c != null) {
			Iterator<?> iterator = c.iterator();
			while (iterator.hasNext()) {
				Taetigkeit taetigkeit = (Taetigkeit) iterator.next();

				if (taetigkeit.getCNr()
						.equals(ZeiterfassungFac.TAETIGKEIT_ENDE)
						|| taetigkeit.getCNr().equals(
								ZeiterfassungFac.TAETIGKEIT_GEHT)
						|| taetigkeit.getCNr().equals(
								ZeiterfassungFac.TAETIGKEIT_KOMMT)) {

				} else {

					list.add(taetigkeitFindByPrimaryKey(taetigkeit.getIId(),
							theClientDto));
				}

			}
		}

		TaetigkeitDto[] returnArray = new TaetigkeitDto[list.size()];
		return (TaetigkeitDto[]) list.toArray(returnArray);

		// }
		// catch (NoResultException ex) {
		// return new TaetigkeitDto[0];
		// }
	}

	/**
	 * Ermittelt ob eine bestimmte Person an diesem Tag einen Sondertaetigkeit
	 * stundenweise eingetragen hat. Der R&uuml;ckgabewert ist ein Vector
	 * welcher die Bezeichnung der Sondertaetigkeit mit dazugeh&ouml;rige
	 * Stundenanzahl (dezimal) enth&auml;lt. Sind mehrere Sondert&auml;tigkeiten
	 * an diesem Tag eingetragen sieht der Vector zB folgenderma&szlig;en aus:
	 * Vector: Element 0: URLAUB Element 1: 5.5 (Stunden) Element 2: ARZT
	 * Element 3: 3.5 (Stunden) ... Vector enth&auml;lt also immer eine gerade
	 * Zahl von Elementen
	 *
	 * @param taetigkeitCNr
	 *            Taetigkeit
	 * @param tDatum
	 *            Datum
	 * @param personalIId
	 *            Personal-ID
	 * @return SonderzeitenDto
	 */
	private SonderzeitenDto sonderzeitenFindByPersonalIIdDDatumTaetigkeitIId(
			Integer personalIId, java.sql.Timestamp tDatum, String taetigkeitCNr) {
		try {
			Query query = em
					.createNamedQuery("SonderzeitenfindByPersonalIIdTDatumTaetigkeitIId");
			query.setParameter(1, personalIId);
			query.setParameter(2, tDatum);
			query.setParameter(3,
					((Taetigkeit) em.createNamedQuery("TaetigkeitfindByCNr")
							.setParameter(1, taetigkeitCNr).getSingleResult())
							.getIId());
			Sonderzeiten sonderzeiten = (Sonderzeiten) query.getSingleResult();
			return assembleSonderzeitenDto(sonderzeiten);
		} catch (NoResultException ex) {
			return null;
		}
	}

	public JasperPrintLP printSondertaetigkeiten(TheClientDto theClientDto) {

		index = -1;
		sAktuellerReport = ZeiterfassungFac.REPORT_SONDERTAETIKGEITEN;
		HashMap parameter = new HashMap<Object, Object>();

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria taetigkeiten = session
				.createCriteria(FLRTaetigkeit.class);

		taetigkeiten.add(Restrictions.ge(
				ZeiterfassungFac.FLR_TAETIGKEIT_B_BDEBUCHBAR,
				Helper.boolean2Short(true)));

		taetigkeiten.addOrder(Order.asc("c_nr"));

		List<?> resultListArtikel = taetigkeiten.list();

		Iterator<?> resultListIterator = resultListArtikel.iterator();

		String sLocUI = Helper.locale2String(theClientDto.getLocUi());
		data = new Object[resultListArtikel.size()][2];
		int row = 0;
		while (resultListIterator.hasNext()) {
			FLRTaetigkeit artikel = (FLRTaetigkeit) resultListIterator.next();

			data[row][REPORT_SONDERTAETIGKEITEN_KENNUNG] = artikel.getC_nr()
					.toUpperCase().trim();

			Iterator<?> sprsetIterator = artikel.getTaetigkeitsprset()
					.iterator();
			while (sprsetIterator.hasNext()) {
				FLRTaetigkeitspr taetigkeitspr = (FLRTaetigkeitspr) sprsetIterator
						.next();
				if (taetigkeitspr.getLocale().getC_nr().compareTo(sLocUI) == 0) {
					data[row][REPORT_SONDERTAETIGKEITEN_BEZEICHNUNG] = taetigkeitspr
							.getC_bez();
					break;
				}
			}

			row++;

		}

		initJRDS(parameter, ZeiterfassungFac.REPORT_MODUL,
				ZeiterfassungFac.REPORT_SONDERTAETIKGEITEN,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();
	}

	/**
	 * Erstellt eine Anwesenheitsliste zum Zeitpunkt JETZT
	 *
	 * @param theClientDto
	 *            String
	 * @return JasperPrint
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	public JasperPrintLP printAnwesenheitsliste(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		boolean bDarfAlleSehen = getTheJudgeFac().hatRecht(
				RechteFac.RECHT_PERS_ANWESENHEITSLISTE_R, theClientDto);

		if (bDarfAlleSehen == false) {
			bDarfAlleSehen = getTheJudgeFac().hatRecht(
					RechteFac.RECHT_PERS_SICHTBARKEIT_ALLE, theClientDto);
		}

		boolean bDarfAbteilungSehen = getTheJudgeFac().hatRecht(
				RechteFac.RECHT_PERS_SICHTBARKEIT_ABTEILUNG, theClientDto);
		return printAnwesenheitsliste2(theClientDto, bDarfAlleSehen,
				bDarfAbteilungSehen);
	}

	/***
	 * Anwesenheitsliste f&uuml;r Terminal (nur Local IF)
	 *
	 * @param theClientDto
	 * @param bDarfAlleSehen
	 * @param bDarfAbteilungSehen
	 * @return JasperPrint der Anwesenheitsliste
	 */
	public JasperPrintLP printAnwesenheitsliste2(TheClientDto theClientDto,
			boolean bDarfAlleSehen, boolean bDarfAbteilungSehen) {

		index = -1;
		sAktuellerReport = ZeiterfassungFac.REPORT_ANWESENHEITSLISTE;

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		session = factory.openSession();

		String sQery = "SELECT personal from FLRPersonal as personal WHERE personal.mandant_c_nr='"
				+ theClientDto.getMandant() + "' AND personal.b_versteckt=0";

		if (bDarfAlleSehen == false) {
			PersonalDto personalDto = getPersonalFac()
					.personalFindByPrimaryKey(theClientDto.getIDPersonal(),
							theClientDto);
			if (bDarfAbteilungSehen
					&& personalDto.getKostenstelleIIdAbteilung() != null) {
				sQery += " AND personal.flrkostenstelleabteilung.i_id="
						+ personalDto.getKostenstelleIIdAbteilung();
			} else {
				sQery += " AND personal.i_id=" + theClientDto.getIDPersonal();
			}
		}

		sQery += " ORDER BY personal.flrpartner.c_name1nachnamefirmazeile1 ASC";

		org.hibernate.Query query = session.createQuery(sQery);

		List<?> resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();
		int row = 0;
		Object[][] dataHelp = new Object[resultList.size()][10];

		java.sql.Timestamp tsJetzt = new java.sql.Timestamp(
				System.currentTimeMillis());

		while (resultListIterator.hasNext()) {

			FLRPersonal personal = (FLRPersonal) resultListIterator.next();
			ZeitdatenDto[] dtos = null;
			ZeitdatenDto[] dtosMitBelegzeiten = null;
			// try {
			Query query2 = em
					.createNamedQuery("ZeitdatenfindZeitdatenEinesTagesUndEinerPerson");
			query2.setParameter(1, personal.getI_id());
			query2.setParameter(2, Helper.cutTimestamp(tsJetzt));
			query2.setParameter(3, tsJetzt);
			dtos = assembleZeitdatenOhneArbeitsUndMaschienzeitenDtos(query2
					.getResultList());
			dtosMitBelegzeiten = assembleZeitdatenDtos(query2.getResultList());
			// }
			// catch (NoResultException ex) {
			// dtos = new ZeitdatenDto[0];
			// }
			boolean bAnzeigen = false;

			// Nachsehen, ob noch eingetreten oder in Personalliste anzeigen
			// aktiviert
			PersonalDto personalDto = getPersonalFac()
					.personalFindByPrimaryKey(personal.getI_id(), theClientDto);

			if (Helper.short2boolean(personalDto.getBAnwesenheitsliste()) == true) {
				bAnzeigen = true;
			}
			try {
				Boolean bAusgetreten = getPersonalFac().istPersonalAusgetreten(
						personal.getI_id(), Helper.cutTimestamp(tsJetzt),
						theClientDto);
				if (bAusgetreten.booleanValue() == false && bAnzeigen == true) {
					bAnzeigen = true;
				}
			} catch (RemoteException ex3) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex3);
			}

			// Wenn noch eingetreten und Hakerl in Personalliste anzeigen
			// aktiviert
			if (bAnzeigen == true) {
				dataHelp[row][REPORT_ANWESENHEITSLISTE_NAME] = personalDto
						.formatAnrede();
				dataHelp[row][REPORT_ANWESENHEITSLISTE_PERSONALNUMMER] = personalDto
						.getCPersonalnr();

				SonderzeitenDto[] sonderzeitenDtos = null;
				try {
					// Hier nun Sonderzeiten holen
					sonderzeitenDtos = getZeiterfassungFac()
							.sonderzeitenFindByPersonalIIdDDatum(
									personalDto.getIId(),
									Helper.cutTimestamp(tsJetzt));
					if (sonderzeitenDtos.length > 0) {
						TaetigkeitDto taeitgkeitDto = getZeiterfassungFac()
								.taetigkeitFindByPrimaryKey(
										sonderzeitenDtos[0].getTaetigkeitIId(),
										theClientDto);

						dataHelp[row][REPORT_ANWESENHEITSLISTE_SONDERZEIT] = taeitgkeitDto
								.getBezeichnung();

						dataHelp[row][REPORT_ANWESENHEITSLISTE_SONDERZEIT_STUNDEN] = sonderzeitenDtos[0]
								.getUStunden();
						if (sonderzeitenDtos[0].getUStunden() != null) {
							dataHelp[row][REPORT_ANWESENHEITSLISTE_SONDERZEIT_ART] = "Stunden";
						} else {
							if (Helper.short2boolean(sonderzeitenDtos[0]
									.getBTag()) == true) {
								dataHelp[row][REPORT_ANWESENHEITSLISTE_SONDERZEIT_ART] = "Ganzer Tag";
							} else {
								dataHelp[row][REPORT_ANWESENHEITSLISTE_SONDERZEIT_ART] = "Halber Tag";
							}
						}

					}
					ParametermandantDto parameter = getParameterFac()
							.getMandantparameter(
									theClientDto.getMandant(),
									ParameterFac.KATEGORIE_PERSONAL,
									ParameterFac.ANWESENHEITSLISTE_TELEFON_PRIVAT_ANZEIGEN);
					Boolean bTelPrivatAnzeigen = (Boolean) parameter
							.getCWertAsObject();

					if (personalDto.getPartnerDto().getCTelefon() != null
							&& bTelPrivatAnzeigen.booleanValue() == true) {
						dataHelp[row][REPORT_ANWESENHEITSLISTE_TEL_PRIVAT] = personalDto
								.getPartnerDto().getCTelefon();
					}
				} catch (RemoteException ex6) {
					throwEJBExceptionLPRespectOld(ex6);
				}

				// Reise holen
				// Wenn letzter Reiseeintrag Beginn, dann abwesend
				SessionFactory factoryReise = FLRSessionFactory.getFactory();
				Session sessionReise = factoryReise.openSession();

				org.hibernate.Criteria critReise = sessionReise
						.createCriteria(FLRReise.class);
				critReise.add(Restrictions.eq(
						ZeiterfassungFac.FLR_REISE_PERSONAL_I_ID,
						personalDto.getIId()));
				critReise.add(Restrictions.lt(
						ZeiterfassungFac.FLR_REISE_T_ZEIT, tsJetzt));
				critReise.addOrder(Order
						.desc(ZeiterfassungFac.FLR_REISE_T_ZEIT));

				List<?> resultListReise = critReise.list();

				Iterator<?> resultListIteratorReise = resultListReise
						.iterator();
				// Erster Eintrag ist beginn
				FLRReise letzterReiseEintrag = null;
				if (resultListIteratorReise.hasNext()) {
					letzterReiseEintrag = (FLRReise) resultListIteratorReise
							.next();
				}

				if (letzterReiseEintrag != null
						&& Helper.short2boolean(letzterReiseEintrag
								.getB_beginn()) == true) {
					dataHelp[row][REPORT_ANWESENHEITSLISTE_ZEIT] = new java.sql.Time(
							letzterReiseEintrag.getT_zeit().getTime())
							.toString().substring(0, 5);
					dataHelp[row][REPORT_ANWESENHEITSLISTE_TAETIGKEIT] = ZeiterfassungFac.TAETIGKEIT_REISE
							.trim();
					dataHelp[row][REPORT_ANWESENHEITSLISTE_ANWESEND] = new Integer(
							0);
				} else {

					if (dtos.length > 0) {
						// Wenn Zeitdaten vorhanden dann letzten Eintrag holen
						ZeitdatenDto letzterEintrag = dtos[dtos.length - 1];

						TaetigkeitDto letzteTaetigkeit = taetigkeitFindByPrimaryKey(
								letzterEintrag.getTaetigkeitIId(), theClientDto);
						dataHelp[row][REPORT_ANWESENHEITSLISTE_TAETIGKEIT] = letzteTaetigkeit
								.getBezeichnung();
						dataHelp[row][REPORT_ANWESENHEITSLISTE_ZEIT] = new java.sql.Time(
								letzterEintrag.getTZeit().getTime()).toString()
								.substring(0, 5);

						// Ist letzte Taetigkeit ein GEHT
						if (letzteTaetigkeit.getCNr().equals(
								ZeiterfassungFac.TAETIGKEIT_GEHT)) {
							// ABWESEND
							dataHelp[row][REPORT_ANWESENHEITSLISTE_ANWESEND] = new Integer(
									0);

						} else if (letzteTaetigkeit.getCNr().equals(
								ZeiterfassungFac.TAETIGKEIT_KOMMT)
								|| letzteTaetigkeit.getCNr().equals(
										ZeiterfassungFac.TAETIGKEIT_ENDE)) {
							// ANWESEND
							dataHelp[row][REPORT_ANWESENHEITSLISTE_ANWESEND] = new Integer(
									1);

							// Auftragsnummer anzeigen, wenn gerade auf Auftrag
							// gestempelt wird
							// try {
							Query query3 = em
									.createNamedQuery("ZeitdatenfindZeitdatenEinesTagesUndEinerPerson");
							query3.setParameter(1, personalDto.getIId());
							query3.setParameter(2, letzterEintrag.getTZeit());
							query3.setParameter(3,
									new Timestamp(System.currentTimeMillis()));

							ZeitdatenDto[] auftraege = assembleZeitdatenDtos(query3
									.getResultList());
							if (auftraege != null && auftraege.length > 1) {
								ZeitdatenDto dto = auftraege[auftraege.length - 1];
								if (dto.getCBelegartnr().equals(
										LocaleFac.BELEGART_AUFTRAG)) {
									try {
										AuftragDto auftragDto = getAuftragFac()
												.auftragFindByPrimaryKey(
														dto.getIBelegartid());
										dataHelp[row][REPORT_ANWESENHEITSLISTE_TAETIGKEIT] = "AB-"
												+ auftragDto.getCNr();
									} catch (Throwable ex5) {
										dataHelp[row][REPORT_ANWESENHEITSLISTE_TAETIGKEIT] = "AUFT-NOT-FOUND";
									}
								} else if (dto.getCBelegartnr().equals(
										LocaleFac.BELEGART_LOS)) {
									try {
										LosDto losDto = getFertigungFac()
												.losFindByPrimaryKey(
														dto.getIBelegartid());
										dataHelp[row][REPORT_ANWESENHEITSLISTE_TAETIGKEIT] = "LO-"
												+ losDto.getCNr();
									} catch (RemoteException ex5) {
										dataHelp[row][REPORT_ANWESENHEITSLISTE_TAETIGKEIT] = "LOS-NOT-FOUND";
									}
								} else if (dto.getCBelegartnr().equals(
										LocaleFac.BELEGART_PROJEKT)) {
									try {
										ProjektDto projektDto = getProjektFac()
												.projektFindByPrimaryKey(
														dto.getIBelegartid());
										dataHelp[row][REPORT_ANWESENHEITSLISTE_TAETIGKEIT] = "PJ-"
												+ projektDto.getCNr();
									} catch (RemoteException ex5) {
										dataHelp[row][REPORT_ANWESENHEITSLISTE_TAETIGKEIT] = "PJ-NOT-FOUND";
									}
								}

								dataHelp[row][REPORT_ANWESENHEITSLISTE_ZEIT] = new java.sql.Time(
										dto.getTZeit().getTime()).toString()
										.substring(0, 5);
							}
							// }
							// catch (NoResultException ex4) {
							// nothing here
							// }
						} else {

							ZeitdatenDto[] zeitdatenEinerTaetigkeit = null;
							// try {
							Query query5 = em
									.createNamedQuery("ZeitdatenfindByPersonalIIdTaetigkeitIIdTVonTBis");
							query5.setParameter(1, personal.getI_id());
							query5.setParameter(2,
									letzterEintrag.getTaetigkeitIId());
							query5.setParameter(3, Helper.cutTimestamp(tsJetzt));
							query5.setParameter(4, tsJetzt);
							zeitdatenEinerTaetigkeit = assembleZeitdatenDtos(query5
									.getResultList());
							// }
							// catch (NoResultException ex1) {
							// zeitdatenEinerTaetigkeit = new ZeitdatenDto[0];
							// }
							// Wenn Anzahl der Taetigkeiten gerade, dann
							// anwesend
							if (zeitdatenEinerTaetigkeit.length % 2 == 0) {
								// ANWESEND
								dataHelp[row][REPORT_ANWESENHEITSLISTE_ANWESEND] = new Integer(
										1);

								// Auftragsnummer anzeigen, wenn gerade auf
								// Auftrag gestempelt wird
								// try {
								Query query3 = em
										.createNamedQuery("ZeitdatenfindZeitdatenEinesTagesUndEinerPerson");
								query3.setParameter(1, personalDto.getIId());
								query3.setParameter(2,
										letzterEintrag.getTZeit());
								query3.setParameter(
										3,
										new Timestamp(System
												.currentTimeMillis()));

								ZeitdatenDto[] auftraege = assembleZeitdatenDtos(query3
										.getResultList());
								if (auftraege != null && auftraege.length > 1) {
									ZeitdatenDto dto = auftraege[auftraege.length - 1];
									if (dto.getCBelegartnr().equals(
											LocaleFac.BELEGART_AUFTRAG)) {
										try {
											AuftragDto auftragDto = getAuftragFac()
													.auftragFindByPrimaryKey(
															dto.getIBelegartid());
											dataHelp[row][REPORT_ANWESENHEITSLISTE_TAETIGKEIT] = "AB-"
													+ auftragDto.getCNr();
										} catch (Throwable ex5) {
											dataHelp[row][REPORT_ANWESENHEITSLISTE_TAETIGKEIT] = "AUFT-NOT-FOUND";
										}
									} else if (dto.getCBelegartnr().equals(
											LocaleFac.BELEGART_LOS)) {
										try {
											LosDto losDto = getFertigungFac()
													.losFindByPrimaryKey(
															dto.getIBelegartid());
											dataHelp[row][REPORT_ANWESENHEITSLISTE_TAETIGKEIT] = "LO-"
													+ losDto.getCNr();
										} catch (RemoteException ex5) {
											dataHelp[row][REPORT_ANWESENHEITSLISTE_TAETIGKEIT] = "LOS-NOT-FOUND";
										}
									} else if (dto.getCBelegartnr().equals(
											LocaleFac.BELEGART_PROJEKT)) {
										try {
											ProjektDto projektDto = getProjektFac()
													.projektFindByPrimaryKey(
															dto.getIBelegartid());
											dataHelp[row][REPORT_ANWESENHEITSLISTE_TAETIGKEIT] = "PJ-"
													+ projektDto.getCNr();
										} catch (RemoteException ex5) {
											dataHelp[row][REPORT_ANWESENHEITSLISTE_TAETIGKEIT] = "PJ-NOT-FOUND";
										}
									}
									dataHelp[row][REPORT_ANWESENHEITSLISTE_ZEIT] = new java.sql.Time(
											dto.getTZeit().getTime())
											.toString().substring(0, 5);
								} else {

									// PJ18768 Wenn nach der Pause auf keinen
									// Auftrag gestempelt wurde, dann den letzen
									// offenen suchen
									for (int k = dtosMitBelegzeiten.length - 1; k > 0; --k) {

										// Wenn zuletzt ENDE, dann kein Auftrag
										// angestempelt
										if (dtosMitBelegzeiten[k]
												.getTaetigkeitIId() != null) {

											TaetigkeitDto taetigkeit = taetigkeitFindByPrimaryKey(
													dtosMitBelegzeiten[k]
															.getTaetigkeitIId(),
													theClientDto);
											if (taetigkeit
													.getCNr()
													.equals(ZeiterfassungFac.TAETIGKEIT_ENDE)) {
												break;
											}

										}

										if (dtosMitBelegzeiten[k]
												.getCBelegartnr() != null
												&& dtosMitBelegzeiten[k]
														.getIBelegartid() != null) {
											ZeitdatenDto dto = dtosMitBelegzeiten[k];

											String belegartUndNummer = "";

											if (dto.getCBelegartnr().equals(
													LocaleFac.BELEGART_AUFTRAG)) {
												try {
													AuftragDto auftragDto = getAuftragFac()
															.auftragFindByPrimaryKey(
																	dto.getIBelegartid());
													belegartUndNummer = "AB-"
															+ auftragDto
																	.getCNr();
												} catch (Throwable ex5) {
													belegartUndNummer = "AUFT-NOT-FOUND";
												}
											} else if (dto
													.getCBelegartnr()
													.equals(LocaleFac.BELEGART_LOS)) {
												try {
													LosDto losDto = getFertigungFac()
															.losFindByPrimaryKey(
																	dto.getIBelegartid());
													belegartUndNummer = "LO-"
															+ losDto.getCNr();
												} catch (RemoteException ex5) {
													belegartUndNummer = "LOS-NOT-FOUND";
												}
											} else if (dto
													.getCBelegartnr()
													.equals(LocaleFac.BELEGART_PROJEKT)) {
												try {
													ProjektDto projektDto = getProjektFac()
															.projektFindByPrimaryKey(
																	dto.getIBelegartid());
													belegartUndNummer = "PJ-"
															+ projektDto
																	.getCNr();
												} catch (RemoteException ex5) {
													belegartUndNummer = "PJ-NOT-FOUND";
												}
											}

											String sVorhanden = (String) dataHelp[row][REPORT_ANWESENHEITSLISTE_TAETIGKEIT];

											sVorhanden += " ("
													+ belegartUndNummer + ")";
											dataHelp[row][REPORT_ANWESENHEITSLISTE_TAETIGKEIT] = sVorhanden;

											break;

										}

									}

								}
								// }
								// catch (NoResultException ex4) {
								// nothing here
								// }

							} else {
								// ABWESEND
								dataHelp[row][REPORT_ANWESENHEITSLISTE_ANWESEND] = new Integer(
										0);
							}

						}

						// Wenn vorhanden, vorletzten Eintrag Holen

					} else {
						// Wenn keine Zeitdaten vorhanden dann in Sonderzeiten
						// nachsehen; Auf jeden Fall abwesend
						dataHelp[row][REPORT_ANWESENHEITSLISTE_ANWESEND] = new Integer(
								0);
						dataHelp[row][REPORT_ANWESENHEITSLISTE_TAETIGKEIT] = "Kein Eintrag f\u00FCr Heute";
						SonderzeitenDto[] sonderzeiten = null;
						// try {
						Query query8 = em
								.createNamedQuery("SonderzeitenfindByPersonalIIdTDatum");
						query8.setParameter(1, personal.getI_id());
						query8.setParameter(2, Helper.cutTimestamp(tsJetzt));
						sonderzeiten = assembleSonderzeitenDtos(query8
								.getResultList());
						// }
						// catch (NoResultException ex2) {
						// sonderzeiten = new SonderzeitenDto[0];
						// }

						if (sonderzeiten.length > 0) {
							SonderzeitenDto sonderzeitenDto = sonderzeiten[0];
							TaetigkeitDto taetigkeitDto = taetigkeitFindByPrimaryKey(
									sonderzeitenDto.getTaetigkeitIId(),
									theClientDto);
							dataHelp[row][REPORT_ANWESENHEITSLISTE_TAETIGKEIT] = taetigkeitDto
									.getBezeichnung();
						}
					}
				}
				row++;

				sessionReise.close();
			}
		}

		session.close();

		for (int i = row - 1; i > 0; --i) {
			for (int j = 0; j < i; ++j) {
				if (((Integer) dataHelp[j][REPORT_ANWESENHEITSLISTE_ANWESEND])
						.intValue() < ((Integer) dataHelp[j + 1][REPORT_ANWESENHEITSLISTE_ANWESEND])
						.intValue()) {
					Object[] oTemp = dataHelp[j];
					dataHelp[j] = dataHelp[j + 1];
					dataHelp[j + 1] = oTemp;
				}
			}
		}

		data = new Object[row][9];
		for (int i = 0; i < row; i++) {
			data[i] = dataHelp[i];
		}
		HashMap parameter = new HashMap<Object, Object>();
		index = -1;
		sAktuellerReport = ZeiterfassungFac.REPORT_ANWESENHEITSLISTE;
		initJRDS(parameter, ZeiterfassungFac.REPORT_MODUL,
				ZeiterfassungFac.REPORT_ANWESENHEITSLISTE,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();
	}

	public double getGesamtDauerEinerSondertaetigkeitImZeitraum(
			Integer personalIId, Timestamp tVon, Timestamp tBis,
			Integer taetigkeitIId, TheClientDto theClientDto,
			Integer tagesartIId_Feiertag, Integer tagesartIId_Halbtag) {
		double dDauer = berechneDauerPaarweiserSondertaetigkeitenEinerPersonUndEinesZeitraumes(
				personalIId, tVon, tBis, taetigkeitIId);

		PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(
				personalIId, theClientDto);

		// try {
		Query query = em
				.createNamedQuery("SonderzeitenfindByPersonalIIdTVonTBisTaetigkeitIId");
		query.setParameter(1, personalIId);
		query.setParameter(2, tVon);
		query.setParameter(3, tBis);
		query.setParameter(4, taetigkeitIId);
		SonderzeitenDto[] sonderzeitenDtos = assembleSonderzeitenDtos(query
				.getResultList());
		for (int k = 0; k < sonderzeitenDtos.length; k++) {

			if (Helper.short2boolean(sonderzeitenDtos[k].getBTag()) == true) {

				Double dDauerTagesweise = Helper
						.time2Double(getSollzeitZuDatum(personalDto,
								sonderzeitenDtos[k].getTDatum(),
								tagesartIId_Feiertag, tagesartIId_Halbtag,
								theClientDto, true));
				if (dDauerTagesweise != null) {
					dDauer = dDauer + dDauerTagesweise.doubleValue();
				}
			} else if (Helper.short2boolean(sonderzeitenDtos[k].getBHalbtag()) == true) {
				Double dDauerHalbtagesweise = Helper
						.time2Double(getSollzeitZuDatum(personalDto,
								sonderzeitenDtos[k].getTDatum(),
								tagesartIId_Feiertag, tagesartIId_Halbtag,
								theClientDto, true));
				if (dDauerHalbtagesweise != null) {
					double dTemp = dDauerHalbtagesweise.doubleValue() / 2;
					dDauer = dDauer + dTemp;
				}

			} else if (sonderzeitenDtos[k].getUStunden() != null) {
				dDauer = dDauer
						+ Helper.time2Double(sonderzeitenDtos[k].getUStunden())
								.doubleValue();

			}

		}

		// }
		// catch (NoResultException ex2) {
		// nothing here
		// }

		return dDauer;

	}

	public boolean sindReisezeitenZueinemTagVorhanden(Integer personalIId,
			java.sql.Timestamp tDatum, TheClientDto theClientDto) {

		String sQuery = "select reise FROM FLRReise reise WHERE reise.t_zeit='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tDatum
						.getTime())) + "' AND reise.personal_i_id="
				+ personalIId;

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Query query = session.createQuery(sQuery);
		query.setMaxResults(1);

		List<?> resultList = query.list();

		Iterator<?> resultListIterator = resultList.iterator();

		if (resultListIterator.hasNext()) {
			return true;
		} else {
			return false;
		}

	}

	public Timestamp getErstesKommtEinesTages(Integer personalIId,
			java.sql.Timestamp tDatum, TheClientDto theClientDto) {

		Integer taetigkeitIId_Kommt = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_KOMMT, theClientDto).getIId();

		tDatum = Helper.cutTimestamp(tDatum);

		String sQuery = "select zeitdaten FROM FLRZeitdaten zeitdaten WHERE zeitdaten.t_zeit>='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tDatum
						.getTime()))
				+ "' AND zeitdaten.t_zeit<'"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tDatum
						.getTime() + (24 * 3600000)))
				+ "' AND zeitdaten.personal_i_id="
				+ personalIId
				+ " AND zeitdaten.taetigkeit_i_id="
				+ taetigkeitIId_Kommt
				+ " ORDER BY zeitdaten.t_zeit ASC";

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Query letzteKommtGeht = session.createQuery(sQuery);
		letzteKommtGeht.setMaxResults(1);

		List<?> resultList = letzteKommtGeht.list();

		Iterator<?> resultListIterator = resultList.iterator();

		if (resultListIterator.hasNext()) {
			FLRZeitdaten l = (FLRZeitdaten) resultListIterator.next();
			return new Timestamp(l.getT_zeit().getTime());
		}

		return null;
	}

	public Timestamp getLetzteGebuchteZeit(Integer personalIId,
			java.sql.Timestamp tDatum, TheClientDto theClientDto) {

		String sQuery = "select zeitdaten FROM FLRZeitdaten zeitdaten WHERE zeitdaten.t_zeit<'"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tDatum
						.getTime() + (24 * 3600000)))
				+ "' AND zeitdaten.t_zeit>='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tDatum
						.getTime()))
				+ "' AND zeitdaten.personal_i_id="
				+ personalIId + " ORDER BY zeitdaten.t_zeit DESC";

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Query letzteBuchung = session.createQuery(sQuery);
		letzteBuchung.setMaxResults(1);

		List<?> resultList = letzteBuchung.list();

		Iterator<?> resultListIterator = resultList.iterator();

		if (resultListIterator.hasNext()) {
			FLRZeitdaten l = (FLRZeitdaten) resultListIterator.next();
			return new Timestamp(l.getT_zeit().getTime());
		}

		return null;
	}

	public Timestamp getLetztesGehtEinesTages(Integer personalIId,
			java.sql.Timestamp tDatum, TheClientDto theClientDto) {

		Integer taetigkeitIId_Geht = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_GEHT, theClientDto).getIId();
		tDatum = Helper.cutTimestamp(tDatum);

		String sQuery = "select zeitdaten FROM FLRZeitdaten zeitdaten WHERE zeitdaten.t_zeit>='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tDatum
						.getTime()))
				+ "' AND zeitdaten.t_zeit<'"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tDatum
						.getTime() + (24 * 3600000)))
				+ "' AND zeitdaten.personal_i_id="
				+ personalIId
				+ " AND zeitdaten.taetigkeit_i_id="
				+ taetigkeitIId_Geht
				+ " ORDER BY zeitdaten.t_zeit DESC";

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Query letzteKommtGeht = session.createQuery(sQuery);
		letzteKommtGeht.setMaxResults(1);

		List<?> resultList = letzteKommtGeht.list();

		Iterator<?> resultListIterator = resultList.iterator();

		if (resultListIterator.hasNext()) {
			FLRZeitdaten l = (FLRZeitdaten) resultListIterator.next();
			return new Timestamp(l.getT_zeit().getTime());
		}

		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printSondertaetigkeitsliste(Integer personalIId,
			Integer taetigkeitIId, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, Integer iOption, Boolean bPlusVersteckte,
			boolean bNurAnwesende, TheClientDto theClientDto) {
		if (tVon == null || tBis == null || personalIId == iOption) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"tVon == null || tBis == null || personalIId == iOption"));
		}

		Integer taetigkeitIId_Kommt = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_KOMMT, theClientDto).getIId();

		Integer tagesartIId_Feiertag = tagesartFindByCNr(
				ZeiterfassungFac.TAGESART_FEIERTAG, theClientDto).getIId();
		Integer tagesartIId_Halbtag = tagesartFindByCNr(
				ZeiterfassungFac.TAGESART_HALBTAG, theClientDto).getIId();

		PersonalDto[] personalDtos = null;
		String sParameter_Personen = "";
		try {
			if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_SELEKTIERTE_PERSON) {

				if (personalIId == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
							new Exception("personalIId == null"));
				}

				personalDtos = new PersonalDto[1];

				personalDtos[0] = getPersonalFac().personalFindByPrimaryKey(
						personalIId, theClientDto);
				sParameter_Personen = personalDtos[0].getPartnerDto()
						.formatAnrede();
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_PERSONEN) {
				personalDtos = getPersonalFac().personalFindByMandantCNr(
						theClientDto.getMandant(), bPlusVersteckte);
				sParameter_Personen = "Alle Personen";
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_MEINE_ABTEILUNG) {
				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKey(personalIId, theClientDto);
				personalDtos = getPersonalFac()
						.personalFindAllPersonenMeinerAbteilung(
								personalDto.getKostenstelleIIdAbteilung(),
								theClientDto.getMandant(), bPlusVersteckte);
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_ARBEITER) {
				personalDtos = getPersonalFac()
						.personalFindAllArbeiterEinesMandanten(
								theClientDto.getMandant(), bPlusVersteckte);
				sParameter_Personen = "Alle Arbeiter";
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_ANGESTELLTE) {
				personalDtos = getPersonalFac()
						.personalFindAllAngestellteEinesMandanten(
								theClientDto.getMandant(), bPlusVersteckte);
				sParameter_Personen = "Alle Angestellten";
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
						"OPTION NICHT VERFUEGBAR"));
			}
			if (bNurAnwesende) {
				personalDtos = getZeiterfassungFac()
						.entferneNichtAnwesendePersonen(tVon, tBis,
								personalDtos, theClientDto);
			}
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		TaetigkeitDto[] taetigkeitenDtos = null;
		if (taetigkeitIId == null) {
			taetigkeitenDtos = getAllSondertaetigkeitenOhneKommtUndGeht(theClientDto);
		} else {
			taetigkeitenDtos = new TaetigkeitDto[1];
			taetigkeitenDtos[0] = taetigkeitFindByPrimaryKey(taetigkeitIId,
					theClientDto);
		}

		List<Object[]> list = new ArrayList<Object[]>();
		double[] dGesamtDauer = new double[taetigkeitenDtos.length];
		for (int i = 0; i < personalDtos.length; i++) {
			PersonalDto personalDto = personalDtos[i];

			personalDto.setPartnerDto(getPartnerFac().partnerFindByPrimaryKey(
					personalDto.getPartnerIId(), theClientDto));

			boolean nameGedruckt = false;
			for (int j = 0; j < taetigkeitenDtos.length; j++) {
				Object[] reihe = new Object[REPORT_SONDERTAETIGKEITENLISTE_ANZAHL_SPALTEN];

				double dDauer = getGesamtDauerEinerSondertaetigkeitImZeitraum(
						personalDto.getIId(), tVon, tBis,
						taetigkeitenDtos[j].getIId(), theClientDto,
						tagesartIId_Feiertag, tagesartIId_Halbtag);

				// SP310

				if (taetigkeitenDtos[j].getIId() == 19) {
					int z = 0;
				}

				Integer iTageHintereinander = 0;
				SonderzeitenDto[] sonderzeitenDtos = sonderzeitenFindByPersonalIIdDDatum(
						personalDto.getIId(), tBis);
				TaetigkeitDto t2Dto = null;
				if (sonderzeitenDtos.length > 0) {
					t2Dto = taetigkeitFindByPrimaryKey(
							sonderzeitenDtos[0].getTaetigkeitIId(),
							theClientDto);
				}

				if ((sonderzeitenDtos.length > 0
						&& Helper.short2boolean(sonderzeitenDtos[0].getBTag()) && sonderzeitenDtos[0]
						.getTaetigkeitIId()
						.equals(taetigkeitenDtos[j].getIId()))
						|| (sonderzeitenDtos.length > 0
								&& Helper.short2boolean(sonderzeitenDtos[0]
										.getBTag())
								&& !sonderzeitenDtos[0].getTaetigkeitIId()
										.equals(taetigkeitenDtos[j].getIId()) && Helper
								.short2Boolean(t2Dto
										.getBUnterbrichtwarnmeldung()) == false)) {

					iTageHintereinander++;
					SessionFactory factory = FLRSessionFactory.getFactory();
					Session session = factory.openSession();
					org.hibernate.Criteria letztesKommt = session
							.createCriteria(FLRZeitdaten.class);

					letztesKommt.add(Expression.eq(
							ZeiterfassungFac.FLR_ZEITDATEN_PERSONAL_I_ID,
							personalDto.getIId()));
					letztesKommt.add(Expression.eq(
							ZeiterfassungFac.FLR_ZEITDATEN_TAETIGKEIT_I_ID,
							taetigkeitIId_Kommt));

					letztesKommt.add(Expression.lt(
							ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT, tBis));
					letztesKommt.addOrder(Order
							.desc(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT));
					letztesKommt.setMaxResults(1);

					List<?> resultListLetztesKommt = letztesKommt.list();

					Date tLetztesKommt = new Timestamp(0);

					if (resultListLetztesKommt.size() > 0) {
						tLetztesKommt = ((FLRZeitdaten) resultListLetztesKommt
								.iterator().next()).getT_zeit();
					}

					Integer taetigkeitIIdWarnnmeldung = taetigkeitenDtos[j]
							.getIId();

					Timestamp tTag = tBis;
					tTag = new Timestamp(tTag.getTime() - 24 * 3600000);

					while (tTag.getTime() > tLetztesKommt.getTime()) {

						sonderzeitenDtos = sonderzeitenFindByPersonalIIdDDatum(
								personalDto.getIId(), tTag);

						TaetigkeitDto tDto = null;

						if (sonderzeitenDtos.length > 0) {
							tDto = taetigkeitFindByPrimaryKey(
									sonderzeitenDtos[0].getTaetigkeitIId(),
									theClientDto);
						} else {
							tDto = null;
						}

						if ((sonderzeitenDtos.length > 0
								&& Helper.short2boolean(sonderzeitenDtos[0]
										.getBTag()) && sonderzeitenDtos[0]
								.getTaetigkeitIId().equals(
										taetigkeitIIdWarnnmeldung))
								|| sonderzeitenDtos.length == 0
								|| (sonderzeitenDtos.length > 0
										&& Helper
												.short2boolean(sonderzeitenDtos[0]
														.getBTag())
										&& !sonderzeitenDtos[0]
												.getTaetigkeitIId()
												.equals(taetigkeitIIdWarnnmeldung) && Helper
										.short2Boolean(tDto
												.getBUnterbrichtwarnmeldung()) == false)) {
							iTageHintereinander++;
						} else {
							break;
						}
						tTag = new Timestamp(tTag.getTime() - 24 * 3600000);
					}
				}

				if (dDauer > 0) {

					reihe[REPORT_SONDERTAETIGKEITENLISTE_PERSONALNUMMER_GRUPPIERUNG] = personalDto
							.getCPersonalnr();
					if (nameGedruckt == false) {
						reihe[REPORT_SONDERTAETIGKEITENLISTE_NAME] = personalDto
								.formatAnrede();
						reihe[REPORT_SONDERTAETIGKEITENLISTE_PERSONALNUMMER] = personalDto
								.getCPersonalnr();
						nameGedruckt = true;
					}
					reihe[REPORT_SONDERTAETIGKEITENLISTE_TAETIGKEIT] = taetigkeitenDtos[j]
							.getBezeichnung();
					reihe[REPORT_SONDERTAETIGKEITENLISTE_WARNMEDLUNG_IN_KALENDERTAGEN] = taetigkeitenDtos[j]
							.getIWarnmeldunginkalendertagen();
					reihe[REPORT_SONDERTAETIGKEITENLISTE_LFD_FEHLTAGE] = iTageHintereinander;

					reihe[REPORT_SONDERTAETIGKEITENLISTE_ZEIT] = new Double(
							dDauer);
					reihe[REPORT_SONDERTAETIGKEITENLISTE_FAKTORBEZAHLT] = taetigkeitenDtos[j]
							.getFBezahlt();
					list.add(reihe);
				}
				dGesamtDauer[j] = dGesamtDauer[j] + dDauer;

			}

		}
		// Gesamtsummen
		boolean gesamtGeduckt = false;
		for (int j = 0; j < taetigkeitenDtos.length; j++) {
			Object[] reihe = new Object[REPORT_SONDERTAETIGKEITENLISTE_ANZAHL_SPALTEN];
			if (dGesamtDauer[j] > 0) {
				reihe[REPORT_SONDERTAETIGKEITENLISTE_PERSONALNUMMER_GRUPPIERUNG] = null;
				if (gesamtGeduckt == false) {
					reihe[REPORT_SONDERTAETIGKEITENLISTE_NAME] = "GESAMT";
					reihe[REPORT_SONDERTAETIGKEITENLISTE_PERSONALNUMMER] = null;
					gesamtGeduckt = true;
				}
				reihe[REPORT_SONDERTAETIGKEITENLISTE_TAETIGKEIT] = taetigkeitenDtos[j]
						.getBezeichnung();

				reihe[REPORT_SONDERTAETIGKEITENLISTE_ZEIT] = new Double(
						dGesamtDauer[j]);
				reihe[REPORT_SONDERTAETIGKEITENLISTE_FAKTORBEZAHLT] = taetigkeitenDtos[j]
						.getFBezahlt();
				list.add(reihe);
			}
		}
		data = new Object[list.size()][REPORT_SONDERTAETIGKEITENLISTE_ANZAHL_SPALTEN];
		for (int i = 0; i < list.size(); i++) {
			data[i] = (Object[]) list.get(i);
		}

		// Erstellung des Reports
		index = -1;
		sAktuellerReport = ZeiterfassungFac.REPORT_SONDERZEITENLISTE;

		HashMap parameter = new HashMap<Object, Object>();

		parameter.put("S_VON", new java.sql.Date(tVon.getTime()).toString());
		parameter.put("S_BIS", new java.sql.Date(tBis.getTime()).toString());
		parameter.put("T_VON", tVon);
		parameter.put("T_BIS", tBis);
		parameter.put("P_NUR_ANWESENDE", new Boolean(bNurAnwesende));
		parameter.put("P_PERSONEN", sParameter_Personen);

		initJRDS(parameter, ZeiterfassungFac.REPORT_MODUL,
				ZeiterfassungFac.REPORT_SONDERZEITENLISTE,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printMonatsAbrechnung(Integer personalIId,
			Integer iJahr, Integer iMonat, boolean bisMonatsende,
			java.sql.Date d_datum_bis, TheClientDto theClientDto,
			Integer iOption, Integer iOptionSortierung,
			Boolean bPlusVersteckte, boolean bNurAnwesende) {
		JasperPrintLP print = null;
		try {

			PersonalDto[] personalDtos = null;

			if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_SELEKTIERTE_PERSON) {

				if (personalIId == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
							new Exception("personalIId == null"));
				}

				personalDtos = new PersonalDto[1];

				personalDtos[0] = getPersonalFac().personalFindByPrimaryKey(
						personalIId, theClientDto);

			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_PERSONEN) {
				personalDtos = getPersonalFac().personalFindByMandantCNr(
						theClientDto.getMandant(), bPlusVersteckte);
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_MEINE_ABTEILUNG) {
				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKey(personalIId, theClientDto);
				personalDtos = getPersonalFac()
						.personalFindAllPersonenMeinerAbteilung(
								personalDto.getKostenstelleIIdAbteilung(),
								theClientDto.getMandant(), bPlusVersteckte);
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_ARBEITER) {
				personalDtos = getPersonalFac()
						.personalFindAllArbeiterEinesMandanten(
								theClientDto.getMandant(), bPlusVersteckte);
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_ANGESTELLTE) {
				personalDtos = getPersonalFac()
						.personalFindAllAngestellteEinesMandanten(
								theClientDto.getMandant(), bPlusVersteckte);
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
						"OPTION NICHT VERFUEGBAR"));
			}

			if (bNurAnwesende) {
				personalDtos = getZeiterfassungFac()
						.entferneNichtAnwesendePersonen(iJahr, iMonat, iJahr,
								iMonat, personalDtos, theClientDto);
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
										.kostenstelleFindByPrimaryKey(
												personalDtos[j]
														.getKostenstelleIIdStamm());
								kostenstelle1 = Helper.fitString2Length(
										kstDto.getCNr(), 15, ' ');

							}
							if (personalDtos[j + 1].getKostenstelleIIdStamm() != null) {
								KostenstelleDto kstDto = getSystemFac()
										.kostenstelleFindByPrimaryKey(
												personalDtos[j + 1]
														.getKostenstelleIIdStamm());
								kostenstelle2 = Helper.fitString2Length(
										kstDto.getCNr(), 15, ' ');

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
										.kostenstelleFindByPrimaryKey(
												personalDtos[j]
														.getKostenstelleIIdAbteilung());
								abteilung1 = Helper.fitString2Length(
										kstDto.getCNr(), 15, ' ');

							} else {
								abteilung1 = "               ";
							}
							if (personalDtos[j + 1]
									.getKostenstelleIIdAbteilung() != null) {
								KostenstelleDto kstDto = getSystemFac()
										.kostenstelleFindByPrimaryKey(
												personalDtos[j + 1]
														.getKostenstelleIIdAbteilung());
								abteilung2 = Helper.fitString2Length(
										kstDto.getCNr(), 15, ' ');

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

						PartnerDto p1Dto = getPartnerFac()
								.partnerFindByPrimaryKey(
										personalDtos[j].getPartnerIId(),
										theClientDto);
						personalDtos[j].setPartnerDto(p1Dto);
						PartnerDto p2Dto = getPartnerFac()
								.partnerFindByPrimaryKey(
										personalDtos[j + 1].getPartnerIId(),
										theClientDto);
						vergleich1 += Helper.fitString2Length(
								p1Dto.getCName1nachnamefirmazeile1(), 80, ' ');
						vergleich2 += Helper.fitString2Length(
								p2Dto.getCName1nachnamefirmazeile1(), 80, ' ');

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

			Integer uebersteuerterReport = theClientDto.getReportvarianteIId();

			for (int i = 0; i < personalDtos.length; i++) {

				try {

					theClientDto.setReportvarianteIId(uebersteuerterReport);
					if (print != null) {

						print = Helper.addReport2Report(
								print,
								erstelleMonatsAbrechnung(
										personalDtos[i].getIId(), iJahr,
										iMonat, bisMonatsende, d_datum_bis,
										theClientDto, true, iOptionSortierung)
										.getJasperPrint().getPrint());
					} else {
						print = erstelleMonatsAbrechnung(
								personalDtos[i].getIId(), iJahr, iMonat,
								bisMonatsende, d_datum_bis, theClientDto, true,
								iOptionSortierung).getJasperPrint();
					}

				} catch (EJBExceptionLP ex1) {
					if (ex1.getCode() == EJBExceptionLP.FEHLER_PERSONAL_FEHLER_BEI_EINTRITTSDATUM
							&& iOption.intValue() != ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_SELEKTIERTE_PERSON) {
						// NICHTS - PERSON WIRD AUSGELASSEN
					} else {
						throw new EJBExceptionLP(ex1);
					}
				}
			}
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		return print;
	}

	public void konvertiereAngebotszeitenNachAuftragzeiten(Integer angebotIId,
			Integer auftragIId, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (angebotIId == null && auftragIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("angebotIId == null && auftragIId == null"));
		}

		AuftragpositionDto[] auftragpositionDtos = null;
		try {
			auftragpositionDtos = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(auftragIId);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		if (auftragpositionDtos != null && auftragpositionDtos.length > 0) {
			Integer ersteAuftragsposition = auftragpositionDtos[0].getIId();
			Session session = FLRSessionFactory.getFactory().openSession();

			org.hibernate.Criteria critZeitdaten = session
					.createCriteria(FLRZeitdaten.class);

			critZeitdaten.add(Restrictions.eq(
					ZeiterfassungFac.FLR_ZEITDATEN_C_BELEGARTNR,
					LocaleFac.BELEGART_ANGEBOT));
			critZeitdaten.add(Restrictions.eq(
					ZeiterfassungFac.FLR_ZEITDATEN_I_BELEGARTID, angebotIId));

			List<?> resultListZeitdaten = critZeitdaten.list();

			Iterator<?> resultListIterator = resultListZeitdaten.iterator();

			int row = 0;
			while (resultListIterator.hasNext()) {
				FLRZeitdaten flrZeitdaten = (FLRZeitdaten) resultListIterator
						.next();

				// try {
				Zeitdaten zeitdaten = em.find(Zeitdaten.class,
						flrZeitdaten.getI_id());
				if (zeitdaten == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
				}
				zeitdaten.setCBelegartnr(LocaleFac.BELEGART_AUFTRAG);
				zeitdaten.setIBelegartid(auftragIId);
				zeitdaten.setIBelegartpositionid(ersteAuftragsposition);
				// }
				// catch (NoResultException e) {
				// throw new EJBExceptionLP(EJBExceptionLP.
				// FEHLER_BEI_FINDBYPRIMARYKEY,
				// e);
				// }
			}
			session.close();
		}
	}

	public void loszeitenVerschieben(Integer losIId_Quelle,
			Integer losIId_Ziel, TheClientDto theClientDto) {

		if (losIId_Quelle == null && losIId_Ziel == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"losIId_Quelle == null && losIId_Ziel == null"));
		}

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria critZeitdaten = session
				.createCriteria(FLRZeitdaten.class);

		critZeitdaten.add(Restrictions.eq(
				ZeiterfassungFac.FLR_ZEITDATEN_C_BELEGARTNR,
				LocaleFac.BELEGART_LOS));
		critZeitdaten.add(Restrictions.eq(
				ZeiterfassungFac.FLR_ZEITDATEN_I_BELEGARTID, losIId_Quelle));

		List<?> resultListZeitdaten = critZeitdaten.list();

		Iterator<?> resultListIterator = resultListZeitdaten.iterator();

		int row = 0;
		while (resultListIterator.hasNext()) {
			FLRZeitdaten flrZeitdaten = (FLRZeitdaten) resultListIterator
					.next();

			Zeitdaten zeitdaten = em.find(Zeitdaten.class,
					flrZeitdaten.getI_id());
			if (zeitdaten == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			zeitdaten.setIBelegartid(losIId_Ziel);

		}
		session.close();

	}

	public void projektzeitenVerschieben(Integer projektIId_Quelle,
			Integer projektIId_Ziel, TheClientDto theClientDto) {

		if (projektIId_Quelle == null && projektIId_Ziel == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"projektIId_Quelle == null && projektIId_Ziel == null"));
		}

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria critZeitdaten = session
				.createCriteria(FLRZeitdaten.class);

		critZeitdaten.add(Restrictions.eq(
				ZeiterfassungFac.FLR_ZEITDATEN_C_BELEGARTNR,
				LocaleFac.BELEGART_PROJEKT));
		critZeitdaten
				.add(Restrictions.eq(
						ZeiterfassungFac.FLR_ZEITDATEN_I_BELEGARTID,
						projektIId_Quelle));

		List<?> resultListZeitdaten = critZeitdaten.list();

		Iterator<?> resultListIterator = resultListZeitdaten.iterator();

		int row = 0;
		while (resultListIterator.hasNext()) {
			FLRZeitdaten flrZeitdaten = (FLRZeitdaten) resultListIterator
					.next();

			Zeitdaten zeitdaten = em.find(Zeitdaten.class,
					flrZeitdaten.getI_id());
			if (zeitdaten == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			zeitdaten.setIBelegartid(projektIId_Ziel);

		}
		session.close();

	}

	@SuppressWarnings("static-access")
	public double getBlockzeitenEinesTages(PersonalDto personalDto,
			ZeitdatenDto[] einblock, Integer taetigkeitIId_Kommt,
			Integer taetigkeitIId_Geht) {
		double dBlockzeit = 0;

		if (personalDto.getKollektivDto() != null && einblock.length > 1) {
			if (personalDto.getKollektivDto().getUBlockzeitab() != null
					&& personalDto.getKollektivDto().getUBlockzeitbis() != null) {

				if (personalDto.getKollektivDto().getUBlockzeitab().getTime() > -3600000
						|| personalDto.getKollektivDto().getUBlockzeitbis()
								.getTime() > -3600000) {

					java.sql.Timestamp tKommt = null;
					java.sql.Timestamp tGeht = null;

					// Kommt suchen
					Calendar c = Calendar.getInstance();
					c.setTime(einblock[0].getTZeit());
					// Wenn Kommt um 00:00, dann letztes Kommt holen
					if (c.get(Calendar.HOUR_OF_DAY) == 0
							&& c.get(Calendar.MINUTE) == 0
							&& taetigkeitIId_Kommt.equals(einblock[0]
									.getTaetigkeitIId())) {
						SessionFactory factory = FLRSessionFactory.getFactory();
						Session session = factory.openSession();

						org.hibernate.Criteria letztesKommt = session
								.createCriteria(FLRZeitdaten.class);

						letztesKommt.add(Expression.eq(
								ZeiterfassungFac.FLR_ZEITDATEN_PERSONAL_I_ID,
								personalDto.getIId()));
						letztesKommt.add(Expression.eq(
								ZeiterfassungFac.FLR_ZEITDATEN_TAETIGKEIT_I_ID,
								taetigkeitIId_Kommt));

						letztesKommt.add(Expression.lt(
								ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT,
								einblock[0].getTZeit()));
						letztesKommt.addOrder(Order
								.desc(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT));
						letztesKommt.setMaxResults(1);

						List<?> resultListLetztesKommt = letztesKommt.list();
						if (resultListLetztesKommt.size() > 0) {
							tKommt = new java.sql.Timestamp(
									((FLRZeitdaten) resultListLetztesKommt
											.get(0)).getT_zeit().getTime());
						}
						session.close();
						tGeht = einblock[einblock.length - 1].getTZeit();
					} else {
						tKommt = einblock[0].getTZeit();

						// Geht suchen
						c = Calendar.getInstance();
						c.setTime(einblock[einblock.length - 1].getTZeit());
						// Wenn Kommt um 00:00, dann naechstes GEHT holen
						if (c.get(Calendar.HOUR_OF_DAY) == 23
								&& c.get(Calendar.MINUTE) == 59
								&& taetigkeitIId_Geht
										.equals(einblock[einblock.length - 1]
												.getTaetigkeitIId())) {
							SessionFactory factory = FLRSessionFactory
									.getFactory();
							Session session = factory.openSession();

							org.hibernate.Criteria naechstesGeht = session
									.createCriteria(FLRZeitdaten.class);

							naechstesGeht
									.add(Expression
											.eq(ZeiterfassungFac.FLR_ZEITDATEN_PERSONAL_I_ID,
													personalDto.getIId()));
							naechstesGeht
									.add(Expression
											.eq(ZeiterfassungFac.FLR_ZEITDATEN_TAETIGKEIT_I_ID,
													taetigkeitIId_Geht));

							naechstesGeht.add(Expression.gt(
									ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT,
									einblock[einblock.length - 1].getTZeit()));
							naechstesGeht
									.addOrder(Order
											.asc(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT));
							naechstesGeht.setMaxResults(1);

							List<?> resultListLetztesGeht = naechstesGeht
									.list();
							if (resultListLetztesGeht.size() > 0) {
								tGeht = new java.sql.Timestamp(
										((FLRZeitdaten) resultListLetztesGeht
												.get(0)).getT_zeit().getTime());
							}
							session.close();
						} else {
							tGeht = einblock[einblock.length - 1].getTZeit();
						}
					}

					Calendar cBlockzeit = Calendar.getInstance();
					cBlockzeit.setTimeInMillis(Helper.cutTimestamp(
							einblock[0].getTZeit()).getTime());
					cBlockzeit.set(cBlockzeit.DAY_OF_MONTH,
							cBlockzeit.get(cBlockzeit.DAY_OF_MONTH) - 1);
					cBlockzeit.setTimeInMillis(cBlockzeit.getTimeInMillis()
							+ personalDto.getKollektivDto().getUBlockzeitab()
									.getTime() + 3600000);

					java.sql.Timestamp tBlockzeitAbVortag = new Timestamp(
							cBlockzeit.getTimeInMillis());

					java.sql.Timestamp tBlockzeitBisHeute = new Timestamp(
							Helper.cutTimestamp(einblock[0].getTZeit())
									.getTime()
									+ personalDto.getKollektivDto()
											.getUBlockzeitbis().getTime()
									+ 3600000);

					java.sql.Timestamp tBlockzeitAbHeute = new Timestamp(Helper
							.cutTimestamp(einblock[0].getTZeit()).getTime()
							+ personalDto.getKollektivDto().getUBlockzeitab()
									.getTime() + 3600000);

					cBlockzeit.setTimeInMillis(tBlockzeitBisHeute.getTime());
					cBlockzeit.set(cBlockzeit.DAY_OF_MONTH,
							cBlockzeit.get(cBlockzeit.DAY_OF_MONTH) + 1);

					java.sql.Timestamp tBlockzeitBisMorgen = new Timestamp(
							cBlockzeit.getTimeInMillis());

					// Block1
					if (tKommt.before(tBlockzeitBisHeute)
							|| tKommt.equals(tBlockzeitBisHeute)) {
						java.sql.Timestamp tBeginn = tKommt;
						if (tKommt.before(tBlockzeitAbVortag)) {
							tBeginn = tBlockzeitAbVortag;
						}

						java.sql.Timestamp tEnde = tGeht;

						if (tGeht.after(tBlockzeitBisHeute)) {
							tEnde = tBlockzeitBisHeute;
						}

						long l = tEnde.getTime() - tBeginn.getTime() - 3600000;

						// muss mindestens 3 Stunden sein
						if (l >= 3600000 * 2) {

							Calendar cTemp = Calendar.getInstance();
							cTemp.setTimeInMillis(Helper.cutTimestamp(
									einblock[0].getTZeit()).getTime());
							cTemp.set(cTemp.DAY_OF_MONTH,
									cTemp.get(cTemp.DAY_OF_MONTH));
							if (tBeginn.before(cTemp.getTime())) {
								tBeginn = new Timestamp(cTemp.getTime()
										.getTime());
							}
							l = tEnde.getTime() - tBeginn.getTime() - 3600000;

							dBlockzeit += Helper.time2Double(new Time(l));
						}

					}

					// Block2
					if (tKommt.after(tBlockzeitBisHeute)
							|| tKommt.equals(tBlockzeitBisHeute)) {
						java.sql.Timestamp tBeginn = tKommt;
						if (tKommt.before(tBlockzeitAbHeute)) {
							tBeginn = tBlockzeitAbHeute;
						}
						java.sql.Timestamp tEnde = tGeht;

						if (tGeht.after(tBlockzeitBisMorgen)) {
							tEnde = tBlockzeitBisMorgen;
						}

						long l = tEnde.getTime() - tBeginn.getTime() - 3600000;

						// muss mindestens 3 Stunden sein
						if (l >= 3600000 * 2) {

							Calendar cTemp = Calendar.getInstance();
							cTemp.setTimeInMillis(Helper.cutTimestamp(
									einblock[0].getTZeit()).getTime());
							cTemp.set(cTemp.DAY_OF_MONTH,
									cTemp.get(cTemp.DAY_OF_MONTH) + 1);
							if (tEnde.after(cTemp.getTime())) {
								tEnde = new Timestamp(cTemp.getTime().getTime());
							}
							l = tEnde.getTime() - tBeginn.getTime() - 3600000;

							dBlockzeit += Helper.time2Double(new Time(l));
						}
					}
				}
			}
		}

		return dBlockzeit;
	}

	public String erstelleMonatsAbrechnungFuerBDE(Integer personalIId,
			Integer iJahr, Integer iMonat, boolean bisMonatsende,
			java.sql.Date d_datum_bis, TheClientDto theClientDto,
			boolean bSaldozurueckschreiben, boolean returnVariableAlsCSV) {

		MonatsabrechnungDto monatsabrechnungDto = erstelleMonatsAbrechnung(
				personalIId,
				iJahr,
				iMonat,
				bisMonatsende,
				d_datum_bis,
				theClientDto,
				bSaldozurueckschreiben,
				ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_PERSONALNUMMER);

		if (returnVariableAlsCSV == false) {
			byte[] CRLFAscii = { 13, 10 };
			String s = Helper
					.fitString2Length(
							"Zeitsaldo:          "
									+ Helper.formatZahl(
											monatsabrechnungDto.getnSaldo(), 2,
											theClientDto.getLocUi()) + " Std.",
							40, ' ')
					+ new String(CRLFAscii);
			;
			s += Helper.fitString2Length(
					"Verf\u00FCgbarer Urlaub: "
							+ Helper.formatZahl(monatsabrechnungDto
									.getNVerfuegbarerurlaub(), 2, theClientDto
									.getLocUi())
							+ monatsabrechnungDto
									.getsEinheitVerfuegbarerUrlaub() + "", 40,
					' ')
					+ new String(CRLFAscii);
			;

			return s;

		} else {
			StringBuffer sb = new StringBuffer();

			sb.append("Zeitsaldo;Einheit;Urlaub;EinheitU\r\n");
			sb.append(monatsabrechnungDto.getnSaldo().toString());
			sb.append(";Std;");
			sb.append(monatsabrechnungDto.getNVerfuegbarerurlaub().toString());
			sb.append(";" + monatsabrechnungDto.getsEinheitVerfuegbarerUrlaub());
			return sb.toString();

		}

	}

	public ZeitdatenDto getZugehoerigeEndeBuchung(ZeitdatenDto zeitdatenDto,
			TheClientDto theClientDto) {
		// Wenn die naechste Buchung ein Ende ist, dann ist ist das die
		// Bis-Buchung
		ZeitdatenDto zDto = null;
		Integer taetigkeitIId_Ende = getZeiterfassungFac().taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_ENDE, theClientDto).getIId();
		Integer taetigkeitIId_Kommt = getZeiterfassungFac()
				.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_KOMMT,
						theClientDto).getIId();
		Integer taetigkeitIId_Geht = getZeiterfassungFac().taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_GEHT, theClientDto).getIId();

		if (zeitdatenDto.getTaetigkeitIId() != null
				&& (zeitdatenDto.getTaetigkeitIId().equals(taetigkeitIId_Kommt) || zeitdatenDto
						.getTaetigkeitIId().equals(taetigkeitIId_Geht))) {
			// Bei Kommt/Geht gibts kein BIS
			return null;
		}

		Session sessionEnde = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Query queryEnde = sessionEnde
				.createQuery("SELECT zeitdaten FROM FLRZeitdaten zeitdaten LEFT OUTER JOIN zeitdaten.flrartikel.artikelsprset AS aspr WHERE zeitdaten.personal_i_id="
						+ zeitdatenDto.getPersonalIId()
						+ " AND zeitdaten.t_zeit > '"
						+ Helper.formatTimestampWithSlashes(zeitdatenDto
								.getTZeit())
						+ "' ORDER BY zeitdaten.t_zeit ASC");
		queryEnde.setMaxResults(1);
		List<?> resultListEnde = queryEnde.list();
		Iterator<?> resultListIteratorEnde = resultListEnde.iterator();
		if (resultListIteratorEnde.hasNext()) {
			FLRZeitdaten z = (FLRZeitdaten) resultListIteratorEnde.next();

			if (z.getTaetigkeit_i_id() != null
					&& z.getTaetigkeit_i_id().equals(taetigkeitIId_Ende)) {
				zDto = zeitdatenFindByPrimaryKey(z.getI_id(), theClientDto);
			}

			if (z.getTaetigkeit_i_id() != null
					&& z.getTaetigkeit_i_id().equals(
							zeitdatenDto.getTaetigkeitIId())) {
				zDto = zeitdatenFindByPrimaryKey(z.getI_id(), theClientDto);
			}

		}
		sessionEnde.close();

		return zDto;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public MonatsabrechnungDto erstelleMonatsAbrechnung(Integer personalIId,
			Integer iJahr, Integer iMonat, boolean bisMonatsende,
			java.sql.Date d_datum_bis, TheClientDto theClientDto,
			boolean bSaldozurueckschreiben, Integer iOptionSortierung)
			throws EJBExceptionLP {

		return erstelleMonatsAbrechnung(personalIId, iJahr, iMonat,
				bisMonatsende, d_datum_bis, theClientDto,
				bSaldozurueckschreiben, iOptionSortierung, true, false);

	}

	/**
	 * Die Monatsabrechnung der Zeitdaten f&uuml;r ein bestimmtes Monat f&uuml;r
	 * eine bestimmte Person
	 *
	 * @param personalIId
	 *            Eindeutige ID des Partners
	 * @param iJahr
	 *            Das Jahr der Abrechnung (zB 2005)
	 * @param iMonat
	 *            Das Monat der Abrechnung (zB 10 f&uuml;r Oktober)
	 * @param bisMonatsende
	 *            Die Abrechnung kann bis zum Monatsende oder bis zum heutigen
	 *            Datum durchgef&uuml;hrt werden
	 * @param d_datum_bis
	 *            Datum, bis wohin Monatsabrechnung berechnet wird, wenn
	 *            bisMonatsende=false
	 * @param theClientDto
	 *            Das Monat der Abrechnung (zB 10 f&uuml;r Oktober)
	 * @return JasperPrint
	 * @throws EJBExceptionLP
	 */

	@TransactionAttribute(TransactionAttributeType.NEVER)
	private MonatsabrechnungDto erstelleMonatsAbrechnung(Integer personalIId,
			Integer iJahr, Integer iMonat, boolean bisMonatsende,
			java.sql.Date d_datum_bis, TheClientDto theClientDto,
			boolean bSaldozurueckschreiben, Integer iOptionSortierung,
			boolean vormonatFuerMaximaleAnwesenheitberechnen,
			boolean bTagesUndwochenmaximumIgnorieren) throws EJBExceptionLP {
		if (iJahr == null || iMonat == null || personalIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"iJahr == null || iMonat == null || personalIId == null"));
		}
		if (bisMonatsende == false && d_datum_bis == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"bisMonatsende == false && d_datum_bis == null"));

		}
		// Index zuruecksetzten und Report setzten
		index = -1;
		sAktuellerReport = ZeiterfassungFac.REPORT_MONATSABRECHNUNG;

		// Personaldaten holen
		PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(
				personalIId, theClientDto);

		// Ueberstundenberechnung
		BigDecimal nNormalstunden = null;
		BigDecimal nSollStundenFuerUestd50 = null;
		BigDecimal nSollStundenFIX = null;
		BigDecimal nMaximalesWochenIST = null;

		boolean bSollstundenMitrechnen = true;

		Calendar calErstesZeitmodell = Calendar.getInstance();
		calErstesZeitmodell.set(iJahr.intValue(), iMonat.intValue(), 1);

		boolean bVonBisZeiterfassungOhneKommtGeht = false;
		boolean bKommtGeht = true;
		boolean bVonBis = false;

		boolean bGutstundenZuUest50Addieren = true;

		try {
			ParametermandantDto parameter = (ParametermandantDto) getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_PERSONAL,
							ParameterFac.PARAMETER_VON_BIS_ERFASSUNG);

			bVonBis = (Boolean) parameter.getCWertAsObject();

			parameter = (ParametermandantDto) getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_PERSONAL,
							ParameterFac.PARAMETER_VON_BIS_ERFASSUNG_KOMMT_GEHT_BUCHEN);

			bKommtGeht = (Boolean) parameter.getCWertAsObject();

			if (bVonBis == true && bKommtGeht == false) {
				bVonBisZeiterfassungOhneKommtGeht = true;
			}

			parameter = (ParametermandantDto) getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_PERSONAL,
							ParameterFac.PARAMETER_GUTSTUNDEN_ZU_UESTD50_ADDIEREN);

			bGutstundenZuUest50Addieren = (Boolean) parameter
					.getCWertAsObject();

		} catch (RemoteException ex5) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
		}

		try {
			PersonalzeitmodellDto personalzeitmodellDto = getPersonalFac()
					.personalzeitmodellFindZeitmodellZuDatum(
							personalIId,
							new java.sql.Timestamp(calErstesZeitmodell
									.getTime().getTime()), theClientDto);
			if (personalzeitmodellDto != null) {
				nSollStundenFuerUestd50 = new BigDecimal(
						getSummeSollzeitWochentags(
								personalzeitmodellDto.getZeitmodellIId())
								.doubleValue());
				ZeitmodellDto zmDto = zeitmodellFindByPrimaryKey(
						personalzeitmodellDto.getZeitmodellIId(), theClientDto);
				nSollStundenFIX = zmDto.getNSollstundenfix();
				nMaximalesWochenIST = zmDto.getNMaximalesWochenist();
				bSollstundenMitrechnen = Helper.short2boolean(zmDto
						.getBFeiertagssollAddieren());
			} else {
				nSollStundenFuerUestd50 = new BigDecimal(0);
			}
		} catch (RemoteException ex11) {
			throwEJBExceptionLPRespectOld(ex11);
		}

		HashMap<Integer, KollektivuestdDto> hmKollektivUestd100 = new HashMap();
		HashMap<Integer, ArrayList> hmKollektivUestd50Tageweise = new HashMap();
		if (personalDto.getKollektivDto() != null) {
			if (personalDto.getKollektivDto() != null
					&& personalDto.getKollektivDto().getNNormalstunden() != null
					&& personalDto.getKollektivDto().getNNormalstunden()
							.doubleValue() > 0) {
				nNormalstunden = personalDto.getKollektivDto()
						.getNNormalstunden();

				try {
					KollektivuestdDto[] kollektivuestdDtos = getPersonalFac()
							.kollektivuestdFindByKollektivIId(
									personalDto.getKollektivDto().getIId());
					for (int i = 0; i < kollektivuestdDtos.length; i++) {
						hmKollektivUestd100.put(
								kollektivuestdDtos[i].getTagesartIId(),
								kollektivuestdDtos[i]);
					}
				} catch (RemoteException ex4) {
					throwEJBExceptionLPRespectOld(ex4);
				}

			}

			try {
				Kollektivuestd50Dto[] kollektivuestd50Dtos = getPersonalFac()
						.kollektivuestd50FindByKollektivIId(
								personalDto.getKollektivDto().getIId());
				for (int i = 0; i < kollektivuestd50Dtos.length; i++) {
					Kollektivuestd50Dto kollektivuestd50Dto = kollektivuestd50Dtos[i];

					// Nachsehen ob an dem Tag 100%ige definiert sind und sich
					// ueberschneiden

					ArrayList alVonBis = new ArrayList();

					if (hmKollektivUestd100.containsKey(kollektivuestd50Dto
							.getTagesartIId())) {

						KollektivuestdDto kollektivuestd100Dto = (KollektivuestdDto) hmKollektivUestd100
								.get(kollektivuestd50Dto.getTagesartIId());

						if (Helper.short2boolean(kollektivuestd100Dto
								.getBRestdestages()) == false
								&& Helper.short2boolean(kollektivuestd50Dto
										.getBRestdestages()) == true) {
							if (kollektivuestd100Dto.getUAb().getTime() > kollektivuestd50Dto
									.getUVon().getTime()) {

								alVonBis.add(new Object[] {
										kollektivuestd50Dto.getUVon(),
										kollektivuestd100Dto.getUAb(),
										kollektivuestd50Dto
												.getBUnterignorieren() });

							}

							if (kollektivuestd100Dto.getUBis().getTime() > kollektivuestd50Dto
									.getUVon().getTime()) {

								alVonBis.add(new Object[] {
										kollektivuestd100Dto.getUBis(),
										kollektivuestd100Dto.getUAb(),
										kollektivuestd100Dto
												.getBUnterignorieren() });
							}

						}

						if (Helper.short2boolean(kollektivuestd100Dto
								.getBRestdestages()) == false
								&& Helper.short2boolean(kollektivuestd50Dto
										.getBRestdestages()) == false) {
							if (kollektivuestd100Dto.getUAb().getTime() > kollektivuestd50Dto
									.getUVon().getTime()) {

								alVonBis.add(new Object[] {
										kollektivuestd50Dto.getUVon(),
										kollektivuestd100Dto.getUAb(),
										kollektivuestd50Dto
												.getBUnterignorieren() });
							}
							if (kollektivuestd100Dto.getUBis().getTime() < kollektivuestd50Dto
									.getUBis().getTime()) {

								alVonBis.add(new Object[] {
										kollektivuestd100Dto.getUBis(),
										kollektivuestd50Dto.getUBis(),
										kollektivuestd100Dto
												.getBUnterignorieren() });
							}

						}
						if (Helper.short2boolean(kollektivuestd100Dto
								.getBRestdestages()) == true
								&& Helper.short2boolean(kollektivuestd50Dto
										.getBRestdestages()) == true) {
							if (kollektivuestd100Dto.getUAb().getTime() > kollektivuestd50Dto
									.getUVon().getTime()) {

								alVonBis.add(new Object[] {
										kollektivuestd50Dto.getUVon(),
										kollektivuestd100Dto.getUAb(),
										kollektivuestd50Dto
												.getBUnterignorieren() });
							}

						}

						if (Helper.short2boolean(kollektivuestd100Dto
								.getBRestdestages()) == true
								&& Helper.short2boolean(kollektivuestd50Dto
										.getBRestdestages()) == false) {

							if (kollektivuestd100Dto.getUAb().getTime() > kollektivuestd50Dto
									.getUVon().getTime()) {

								alVonBis.add(new Object[] {
										kollektivuestd50Dto.getUVon(),
										kollektivuestd100Dto.getUAb(),
										kollektivuestd50Dto
												.getBUnterignorieren() });
							}

							if (kollektivuestd50Dto.getUBis().getTime() <= kollektivuestd100Dto
									.getUAb().getTime()) {
								alVonBis.add(new Object[] {
										new Time(-3600000),
										kollektivuestd50Dto.getUBis(),
										kollektivuestd100Dto
												.getBUnterignorieren() });
							}

						}

					} else {

						if (Helper.short2boolean(kollektivuestd50Dto
								.getBRestdestages()) == false) {
							alVonBis.add(new Object[] { new Time(-3600000),
									kollektivuestd50Dto.getUBis(),
									kollektivuestd50Dto.getBUnterignorieren() });
							alVonBis.add(new Object[] {
									kollektivuestd50Dto.getUVon(), null,
									kollektivuestd50Dto.getBUnterignorieren() });
						}
						if (Helper.short2boolean(kollektivuestd50Dto
								.getBRestdestages()) == true) {
							alVonBis.add(new Object[] {
									kollektivuestd50Dto.getUVon(), null,
									kollektivuestd50Dto.getBUnterignorieren() });
						}

					}

					hmKollektivUestd50Tageweise.put(
							kollektivuestd50Dto.getTagesartIId(), alVonBis);

				}
			} catch (RemoteException ex4) {
				throwEJBExceptionLPRespectOld(ex4);
			}

		}

		// Hat die ausgewaehlte Person eine Ueberstundenpauschale im aktuellen
		// Abrechnungsmonat
		double fUeberstundenpauschale = 0;
		boolean bUeberstundenAutomatischAuszahlen = false;
		BigDecimal bdUeberstundenPuffer = new BigDecimal(0);

		try {
			PersonalgehaltDto personalgehaltDto = getPersonalFac()
					.personalgehaltFindLetztePersonalgehalt(personalIId, iJahr,
							iMonat);

			if (personalgehaltDto != null) {
				fUeberstundenpauschale = personalgehaltDto.getFUestpauschale()
						.doubleValue();
				bdUeberstundenPuffer = personalgehaltDto.getNUestdpuffer();
				bUeberstundenAutomatischAuszahlen = Helper
						.short2boolean(personalgehaltDto.getBUestdauszahlen());
			}
		} catch (RemoteException ex) {
			// Keine Ueberstundenpauschale
		}

		// Hole Dto der Unterbrechung
		TaetigkeitDto taetigkeitDto_Unter = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_UNTER, theClientDto);
		// Hole Dto des Arztbesuchs
		TaetigkeitDto taetigkeitDto_Arzt = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_ARZT, theClientDto);
		// Hole Dto der Behoerde
		TaetigkeitDto taetigkeitDto_Behoerde = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_BEHOERDE, theClientDto);
		// Hole Dto des Urlaub
		TaetigkeitDto taetigkeitDto_Urlaub = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_URLAUB, theClientDto);
		// Hole Dto des Zeitausgleichs
		TaetigkeitDto taetigkeitDto_ZA = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_ZEITAUSGLEICH, theClientDto);
		// Hole Dto der Behoerde
		TaetigkeitDto taetigkeitDto_Krank = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_KRANK, theClientDto);
		TaetigkeitDto taetigkeitDto_Kindkrank = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_KINDKRANK, theClientDto);

		// Hole id der Taetigkeit KOMMT
		Integer taetigkeitIId_Kommt = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_KOMMT, theClientDto).getIId();
		// Hole id der Taetigkeit GEHT
		Integer taetigkeitIId_Geht = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_GEHT, theClientDto).getIId();
		// Hole id der Taetigkeit ENDE
		Integer taetigkeitIId_Ende = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_ENDE, theClientDto).getIId();
		// Hole id der Taetigkeit TELEFON
		Integer taetigkeitIId_telefon = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_TELEFON, theClientDto).getIId();
		// Hole die Sonstigen Taetigkeiten
		TaetigkeitDto[] sonstigeTaetigkeiten = getAllBenutzerdefinierteSondertaetigkeiten();

		Integer tagesartIId_Feiertag = tagesartFindByCNr(
				ZeiterfassungFac.TAGESART_FEIERTAG, theClientDto).getIId();
		Integer tagesartIId_Halbtag = tagesartFindByCNr(
				ZeiterfassungFac.TAGESART_HALBTAG, theClientDto).getIId();

		// Anzahl der Tage im aktuellen Monat ermitteln
		int lAnzahlTageImMonat = Helper.ermittleAnzahlTageEinesMonats(iJahr,
				iMonat);

		// Wenn bis Monatsende false ist, dann nur bis zum angegebenen
		// d_datum_bis berechnen
		Calendar cal = Calendar.getInstance();
		if (bisMonatsende == false) {
			cal.setTime(d_datum_bis);
			lAnzahlTageImMonat = cal.get(Calendar.DAY_OF_MONTH);
		} else {
			cal.set(iJahr.intValue(), iMonat.intValue(), lAnzahlTageImMonat);
			d_datum_bis = new java.sql.Date(cal.getTime().getTime());
		}

		// Zeitmodell
		Integer iZeitmodellId = null;
		Integer iZeitmodellIdVortag = null;

		Date dEintrittsdatum = null;

		Date dAustrittsdatum = null;

		// Hole letztes Eintrittsdatum
		try {

			EintrittaustrittDto eaDto = getPersonalFac()
					.eintrittaustrittFindLetztenEintrittBisDatum(personalIId,
							new Timestamp(d_datum_bis.getTime()));
			if (eaDto != null) {
				dEintrittsdatum = eaDto.getTEintritt();
			}

		} catch (RemoteException ex3) {
			throwEJBExceptionLPRespectOld(ex3);
		}

		// Austrittsdatum suchen, wenn NULL, dann ist Austritt 2099
		if (dEintrittsdatum != null) {
			try {
				dAustrittsdatum = getPersonalFac()
						.eintrittaustrittFindByPersonalIIdDEintritt(
								personalIId,
								new java.sql.Timestamp(dEintrittsdatum
										.getTime())).getTAustritt();
				if (dAustrittsdatum != null) {
					Calendar cAus = Calendar.getInstance();
					cAus.setTime(dAustrittsdatum);
					// Einen Tag dazuzaehlen, damit der Autrittstag auch noch
					// mitabgerechnet wird
					cAus.set(Calendar.DAY_OF_MONTH,
							cAus.get(Calendar.DAY_OF_MONTH) + 1);
					dAustrittsdatum = cAus.getTime();
				}
			} catch (RemoteException ex2) {
				// kein Austritt
			}
		}
		if (dAustrittsdatum == null) {
			Calendar x = Calendar.getInstance();
			x.set(Calendar.YEAR, 2099);
			dAustrittsdatum = x.getTime();
			// Einen Tag dazuzaehlen, damit der Autrittstag auch noch
			// mitabgerechnet wird
			dAustrittsdatum = new Date(dAustrittsdatum.getTime() + 24 * 3600000);
		}

		// Array initialisieren
		data = new Object[lAnzahlTageImMonat][23];

		ArrayList[] monatsDaten = new ArrayList[lAnzahlTageImMonat + 1];
		BigDecimal[] monatsDatenReisezeiten = new BigDecimal[lAnzahlTageImMonat + 1];
		for (int i = 0; i < lAnzahlTageImMonat + 1; i++) {
			monatsDaten[i] = new ArrayList<Object>();

		}

		// Kurze Tagebezeichnungen holen
		String[] kurzeWochentage = new DateFormatSymbols(
				theClientDto.getLocUi()).getShortWeekdays();

		Object[] auftragsDaten = new Object[lAnzahlTageImMonat + 1];

		// Monatserster
		cal.set(iJahr.intValue(), iMonat.intValue(), 1);
		Timestamp tsVon = new Timestamp(cal.getTime().getTime());
		tsVon = Helper.cutTimestamp(tsVon);
		// Bis Auswertungsdatum
		cal.set(iJahr.intValue(), iMonat.intValue() + 1, 1);
		Timestamp tsBis = new Timestamp(cal.getTime().getTime());
		tsBis = Helper.cutTimestamp(tsBis);

		// Alle ZeitdatenDesMonats holen
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();
		Session sessionZulagen = factory.openSession();

		org.hibernate.Criteria artikelzulagen = sessionZulagen
				.createCriteria(FLRArtikelzulage.class);
		artikelzulagen.add(Expression.lt("t_gueltigab", tsVon));

		artikelzulagen.addOrder(Order
				.asc(PersonalFac.FLR_ARTKELZULAGE_FLRARTIKEL + ".i_id"));
		artikelzulagen.addOrder(Order.desc("t_gueltigab"));

		List<?> resultListZulagen = artikelzulagen.list();

		Iterator<?> resultListIteratorZulagen = resultListZulagen.iterator();
		ArtikelzulageDto[] zulagenDtos = new ArtikelzulageDto[resultListZulagen
				.size()];
		Integer[] zulagenArtikelIId = new Integer[resultListZulagen.size()];

		int rowZulage = 0;
		while (resultListIteratorZulagen.hasNext()) {
			FLRArtikelzulage flrartikelzulagen = (FLRArtikelzulage) resultListIteratorZulagen
					.next();
			ArtikelzulageDto artikelzulageDto = new ArtikelzulageDto();
			artikelzulageDto.setIId(flrartikelzulagen.getI_id());
			artikelzulageDto.setArtikelIId(flrartikelzulagen.getFlrartikel()
					.getI_id());
			artikelzulageDto.setTGueltigab(new Timestamp(flrartikelzulagen
					.getT_gueltigab().getTime()));

			ZulageDto zulageDto = new ZulageDto();
			zulageDto.setIId(flrartikelzulagen.getFlrzulage().getI_id());
			zulageDto.setCBez(flrartikelzulagen.getFlrzulage().getC_bez());
			artikelzulageDto.setZulageDto(zulageDto);

			zulagenDtos[rowZulage] = artikelzulageDto;
			zulagenArtikelIId[rowZulage] = flrartikelzulagen.getFlrartikel()
					.getI_id();

			rowZulage++;
		}

		sessionZulagen.close();

		HashMap<Integer, HashMap<Integer, Double>> hmBelegeMitZulagen = new HashMap<Integer, HashMap<Integer, Double>>();

		if (zulagenArtikelIId.length > 0) {
			// Alle Belegbuchungen mit Zulagen-Artikeln holen
			Session sessionBelegeMitZulagen = factory.openSession();

			org.hibernate.Criteria belegeMitzulagen = sessionBelegeMitZulagen
					.createCriteria(FLRZeitdaten.class);
			belegeMitzulagen.add(Expression.eq(
					ZeiterfassungFac.FLR_ZEITDATEN_PERSONAL_I_ID, personalIId));
			belegeMitzulagen.add(Expression.ge(
					ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT, tsVon));

			if (bisMonatsende) {
				belegeMitzulagen.add(Expression.lt(
						ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT, tsBis));
			} else {
				belegeMitzulagen.add(Expression.lt(
						ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT, new Timestamp(
								d_datum_bis.getTime() + 24 * 3600000)));
			}

			belegeMitzulagen.add(Expression
					.isNotNull(ZeiterfassungFac.FLR_ZEITDATEN_ARTIKEL_I_ID));
			belegeMitzulagen.add(Expression.in(
					ZeiterfassungFac.FLR_ZEITDATEN_ARTIKEL_I_ID,
					zulagenArtikelIId));

			List<?> resultListBelegeMitZulagen = belegeMitzulagen.list();

			Iterator<?> resultListIteratorBelegeMitZulagen = resultListBelegeMitZulagen
					.iterator();

			while (resultListIteratorBelegeMitZulagen.hasNext()) {
				FLRZeitdaten belegzeit = (FLRZeitdaten) resultListIteratorBelegeMitZulagen
						.next();

				Calendar c = Calendar.getInstance();
				c.setTime(belegzeit.getT_zeit());

				Integer iTag = c.get(Calendar.DAY_OF_MONTH);

				Timestamp tBis = new Timestamp(
						belegzeit.getT_zeit().getTime() + 24 * 3600000);

				AuftragzeitenDto[] azDtos = getAllZeitenEinesBeleges(
						belegzeit.getC_belegartnr(),
						belegzeit.getI_belegartid(),
						belegzeit.getI_belegartpositionid(), personalIId,
						new Timestamp(belegzeit.getT_zeit().getTime()),
						Helper.cutTimestamp(tBis), true, false, false,
						theClientDto);

				for (int i = 0; i < azDtos.length; i++) {
					Integer artikelIId = azDtos[i].getArtikelIId();

					if (hmBelegeMitZulagen.containsKey(iTag)) {
						HashMap<Integer, Double> hmZulagen = hmBelegeMitZulagen
								.get(iTag);
						if (hmZulagen.containsKey(artikelIId)) {
							Double dDauer = hmZulagen.get(artikelIId);
							dDauer += azDtos[i].getDdDauer();
							hmZulagen.put(artikelIId, dDauer);
							hmBelegeMitZulagen.put(iTag, hmZulagen);
						} else {
							hmZulagen.put(artikelIId, azDtos[i].getDdDauer());
							hmBelegeMitZulagen.put(iTag, hmZulagen);
						}
					} else {
						HashMap<Integer, Double> hmZulagen = new HashMap<Integer, Double>();
						hmZulagen.put(artikelIId, azDtos[i].getDdDauer());
						hmBelegeMitZulagen.put(iTag, hmZulagen);
					}
				}

			}
		}

		// Alle Monatszeitdaten holen
		org.hibernate.Criteria zeitdatenEinesMonatsOhneAuftraege = session
				.createCriteria(FLRZeitdaten.class);

		zeitdatenEinesMonatsOhneAuftraege.add(Expression.eq(
				ZeiterfassungFac.FLR_ZEITDATEN_PERSONAL_I_ID, personalIId));
		zeitdatenEinesMonatsOhneAuftraege.add(Expression.ge(
				ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT, tsVon));

		if (bisMonatsende) {
			zeitdatenEinesMonatsOhneAuftraege.add(Expression.lt(
					ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT, tsBis));
		} else {
			zeitdatenEinesMonatsOhneAuftraege.add(Expression.lt(
					ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT, new Timestamp(
							d_datum_bis.getTime() + 24 * 3600000)));
		}

		zeitdatenEinesMonatsOhneAuftraege.add(Expression
				.isNull(ZeiterfassungFac.FLR_ZEITDATEN_ARTIKEL_I_ID));

		zeitdatenEinesMonatsOhneAuftraege.add(Expression
				.isNotNull(ZeiterfassungFac.FLR_ZEITDATEN_TAETIGKEIT_I_ID));

		Integer[] taetigkeiten = new Integer[2];
		taetigkeiten[0] = taetigkeitIId_Ende;
		// Und ohne Telefonzeiten
		taetigkeiten[1] = taetigkeitIId_telefon;

		zeitdatenEinesMonatsOhneAuftraege.add(Expression.not(Expression.in(
				ZeiterfassungFac.FLR_ZEITDATEN_TAETIGKEIT_I_ID, taetigkeiten)));
		zeitdatenEinesMonatsOhneAuftraege.addOrder(Order
				.asc(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT));

		List<?> resultListZeitdatenOhneAuftraege = zeitdatenEinesMonatsOhneAuftraege
				.list();

		Iterator<?> resultListIteratorOhneAuftraege = resultListZeitdatenOhneAuftraege
				.iterator();

		ArrayList<MonatsabrechnungBereitschaftDto> alBereitschaften = new ArrayList();

		// Monatsdaten auf Kommt-Geht-Bloecke aufteilen
		boolean bKommt = false;
		ArrayList<ZeitdatenDto> kommtGehtBlock = new ArrayList<ZeitdatenDto>();
		int iLetzterTag = -1;
		while (resultListIteratorOhneAuftraege.hasNext()) {
			FLRZeitdaten flrzeitdaten = (FLRZeitdaten) resultListIteratorOhneAuftraege
					.next();

			Calendar c = Calendar.getInstance();
			c.setTime(flrzeitdaten.getT_zeit());
			int iTag = c.get(Calendar.DAY_OF_MONTH);
			if (iLetzterTag != -1 && iTag != iLetzterTag
					&& kommtGehtBlock.size() > 0) {
				monatsDaten[iLetzterTag].add(kommtGehtBlock);
				kommtGehtBlock = new ArrayList<ZeitdatenDto>();
				bKommt = false;
			}

			ZeitdatenDto zeitdatenDto = new ZeitdatenDto();
			zeitdatenDto.setTaetigkeitIId(flrzeitdaten.getFlrtaetigkeit()
					.getI_id());
			zeitdatenDto.setTZeit(new Timestamp(flrzeitdaten.getT_zeit()
					.getTime()));
			zeitdatenDto.setIId(flrzeitdaten.getI_id());
			zeitdatenDto.setBAutomatikbuchung(flrzeitdaten
					.getB_automatikbuchung());
			kommtGehtBlock.add(zeitdatenDto);

			if (bKommt == false) {
				if (zeitdatenDto.getTaetigkeitIId() != null
						&& zeitdatenDto.getTaetigkeitIId().equals(
								taetigkeitIId_Kommt)) {
					bKommt = true;
				} else {
					// FEHLER: 2 KOMMT NACHEINANDER
				}
			} else if (bKommt == true) {
				if (zeitdatenDto.getTaetigkeitIId() != null
						&& zeitdatenDto.getTaetigkeitIId().equals(
								taetigkeitIId_Geht)) {
					bKommt = false;

					monatsDaten[iTag].add(kommtGehtBlock);
					kommtGehtBlock = new ArrayList<ZeitdatenDto>();
				}

			}

			if (resultListIteratorOhneAuftraege.hasNext() == false
					&& kommtGehtBlock.size() > 0) {
				monatsDaten[iTag].add(kommtGehtBlock);
			}

			iLetzterTag = iTag;
		}
		session.close();

		// fuer jeden KOMMT-GEHT-Block Mitternachtssprung simulieren

		for (int i = 1; i < lAnzahlTageImMonat + 1; i++) {
			ArrayList<ArrayList<ZeitdatenDto>> bloecke = monatsDaten[i];

			for (int j = 0; j < bloecke.size(); j++) {

				ArrayList<ZeitdatenDto> al = bloecke.get(j);

				ZeitdatenDto[] dtos = new ZeitdatenDto[al.size()];

				dtos = (ZeitdatenDto[]) al.toArray(dtos);

				dtos = simuliereMitternachssprung(personalIId,
						new java.sql.Date(al.get(0).getTZeit().getTime()),
						taetigkeitIId_Kommt, taetigkeitIId_Geht, dtos);

				al = new ArrayList();
				for (int k = 0; k < dtos.length; k++) {
					al.add(dtos[k]);
				}
				bloecke.set(j, al);
			}
			monatsDaten[i] = bloecke;
		}

		// nochmals auf KOMMT-GEHT Bloecke aufteilen
		for (int i = 1; i < lAnzahlTageImMonat + 1; i++) {
			ArrayList<ArrayList<ZeitdatenDto>> bloecke = monatsDaten[i];

			ArrayList<ArrayList<ZeitdatenDto>> alNeueBloecke = new ArrayList<ArrayList<ZeitdatenDto>>();
			for (int j = 0; j < bloecke.size(); j++) {

				ArrayList<ZeitdatenDto> al = bloecke.get(j);

				ZeitdatenDto[] dtos = new ZeitdatenDto[al.size()];
				dtos = (ZeitdatenDto[]) al.toArray(dtos);

				boolean bMehrereBloecke = false;

				if (dtos.length > 1) {
					for (int m = 2; m < dtos.length; m++) {
						ZeitdatenDto dto = dtos[m];
						if (dto.getTaetigkeitIId() != null
								&& dto.getTaetigkeitIId().equals(
										taetigkeitIId_Kommt)) {

							// Auf 2 Bloecke aufteilen
							bMehrereBloecke = true;
							ArrayList<ZeitdatenDto> alTemp = new ArrayList<ZeitdatenDto>();
							for (int l = 0; l < m; l++) {
								alTemp.add(dtos[l]);
							}
							alNeueBloecke.add(alTemp);

							alTemp = new ArrayList<ZeitdatenDto>();
							for (int l = m; l < dtos.length; l++) {
								alTemp.add(dtos[l]);
							}
							alNeueBloecke.add(alTemp);
							break;

						}
					}
				}

				if (bMehrereBloecke == false) {
					alNeueBloecke.add(bloecke.get(j));
				}

			}

			monatsDaten[i] = alNeueBloecke;

		}

		// Reisezeiten holen

		for (int i = 1; i < lAnzahlTageImMonat + 1; i++) {

			cal.set(iJahr.intValue(), iMonat.intValue(), i, 0, 0, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Timestamp tVon = new Timestamp(cal.getTimeInMillis());

			cal.set(iJahr.intValue(), iMonat.intValue(), i + 1, 0, 0, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Timestamp tBis = new Timestamp(cal.getTimeInMillis());

			Session sessReise = FLRSessionFactory.getFactory().openSession();
			org.hibernate.Criteria reisezeiten = sessReise
					.createCriteria(FLRReise.class);
			reisezeiten.add(Expression.eq(
					ZeiterfassungFac.FLR_REISE_PERSONAL_I_ID, personalIId));
			reisezeiten.add(Expression.ge(ZeiterfassungFac.FLR_REISE_T_ZEIT,
					tVon));
			reisezeiten.add(Expression.lt(ZeiterfassungFac.FLR_REISE_T_ZEIT,
					tBis));
			reisezeiten.addOrder(Order.asc(ZeiterfassungFac.FLR_REISE_T_ZEIT));

			List<?> lReisezeiten = reisezeiten.list();

			long reiseGesamt = 0;

			if (lReisezeiten.size() == 0) {
				// Hole letzten Eintrag
				Session sessReiseLetztesBeginn = FLRSessionFactory.getFactory()
						.openSession();
				org.hibernate.Criteria criteriaLetztesBeginn = sessReiseLetztesBeginn
						.createCriteria(FLRReise.class);
				criteriaLetztesBeginn.add(Expression.eq(
						ZeiterfassungFac.FLR_REISE_PERSONAL_I_ID, personalIId));
				criteriaLetztesBeginn.add(Expression.lt(
						ZeiterfassungFac.FLR_REISE_T_ZEIT, tVon));
				criteriaLetztesBeginn.addOrder(Order
						.desc(ZeiterfassungFac.FLR_REISE_T_ZEIT));
				criteriaLetztesBeginn.setMaxResults(1);
				List<?> listLetzttesBeginn = criteriaLetztesBeginn.list();
				if (listLetzttesBeginn.size() > 0) {
					FLRReise flrLetzterEintrag = (FLRReise) listLetzttesBeginn
							.get(0);
					if (Helper.short2boolean(flrLetzterEintrag.getB_beginn()) == true) {
						reiseGesamt = 24 * 3600000;
					}
				}

			}

			FLRReise letzterReiseeintrag = null;
			for (int j = 0; j < lReisezeiten.size(); j++) {
				FLRReise reise = (FLRReise) lReisezeiten.get(j);

				if (j == 0) {
					// Hole letzten Eintrag
					Session sessReiseLetztesBeginn = FLRSessionFactory
							.getFactory().openSession();
					org.hibernate.Criteria criteriaLetztesBeginn = sessReiseLetztesBeginn
							.createCriteria(FLRReise.class);
					criteriaLetztesBeginn.add(Expression.eq(
							ZeiterfassungFac.FLR_REISE_PERSONAL_I_ID,
							personalIId));
					criteriaLetztesBeginn.add(Expression.lt(
							ZeiterfassungFac.FLR_REISE_T_ZEIT,
							reise.getT_zeit()));
					criteriaLetztesBeginn.addOrder(Order
							.desc(ZeiterfassungFac.FLR_REISE_T_ZEIT));
					criteriaLetztesBeginn.setMaxResults(1);
					List<?> listLetzttesBeginn = criteriaLetztesBeginn.list();
					if (Helper.short2boolean(reise.getB_beginn()) == true) {
						if (listLetzttesBeginn.size() > 0) {

							FLRReise flrLetzterEintrag = (FLRReise) listLetzttesBeginn
									.get(0);

							if (Helper.short2boolean(flrLetzterEintrag
									.getB_beginn()) == true) {
								reiseGesamt += reise.getT_zeit().getTime()
										- tVon.getTime();
							}

						}
					} else {
						reiseGesamt += reise.getT_zeit().getTime()
								- tVon.getTime();
					}

					sessReiseLetztesBeginn.close();
				}
				if (j > 0 && j < lReisezeiten.size()) {

					if (Helper.short2boolean(reise.getB_beginn()) == true
							&& Helper.short2boolean(letzterReiseeintrag
									.getB_beginn()) == true) {
						reiseGesamt += reise.getT_zeit().getTime()
								- letzterReiseeintrag.getT_zeit().getTime();
					} else if (Helper.short2boolean(reise.getB_beginn()) == false
							&& Helper.short2boolean(letzterReiseeintrag
									.getB_beginn()) == true) {
						reiseGesamt += reise.getT_zeit().getTime()
								- letzterReiseeintrag.getT_zeit().getTime();
					}

				}

				if (j == 0 && lReisezeiten.size() == 1) {
					if (Helper.short2boolean(reise.getB_beginn()) == true) {
						reiseGesamt += tBis.getTime()
								- reise.getT_zeit().getTime();
					}
				}

				// Wenn letzer Eintrag Beginn ist
				if (j > 0 && j == lReisezeiten.size()) {
					if (Helper.short2boolean(reise.getB_beginn()) == true) {
						reiseGesamt += tBis.getTime()
								- letzterReiseeintrag.getT_zeit().getTime();
					}
				}

				letzterReiseeintrag = reise;
			}

			double dReiseDesamt = reiseGesamt;
			monatsDatenReisezeiten[i] = new BigDecimal(
					dReiseDesamt / 1000 / 60 / 60);
			sessReise.close();
		}

		boolean bUestdVerteilen = false;
		if (personalDto.getKollektivDto() != null
				&& Helper.short2boolean(personalDto.getKollektivDto()
						.getBUestdverteilen())) {

			bUestdVerteilen = true;
		}

		double dSummeMonatFeiertagSoll = 0;

		double dWochensummeUestd200 = 0;
		double dWochensummeUestd100 = 0;
		double dWochensummeUestd50Tageweise = 0;
		double dWochensummeIst = 0;
		double dWochensummeIstOhneSonderzeiten = 0;

		boolean bEintrittsmonatMonat = false;

		// SP3265 Wenn Eintrittsmonat, dann Vormonat nicht berechnen
		if (dEintrittsdatum != null) {
			Calendar cVergleich = Calendar.getInstance();
			cVergleich.setTimeInMillis(dEintrittsdatum.getTime());
			if (cVergleich.get(Calendar.MONTH) == iMonat
					&& cVergleich.get(Calendar.YEAR) == iJahr) {
				bEintrittsmonatMonat = true;
			}
		}

		if (vormonatFuerMaximaleAnwesenheitberechnen == true
				&& nMaximalesWochenIST != null
				&& nMaximalesWochenIST.doubleValue() > 0
				&& bEintrittsmonatMonat == false) {

			Calendar cVormonat = Calendar.getInstance();
			cVormonat.setTimeInMillis(calErstesZeitmodell.getTimeInMillis());
			cVormonat.add(Calendar.MONTH, -1);

			MonatsabrechnungDto monatsabrechnungVormonatDto = erstelleMonatsAbrechnung(
					personalIId, cVormonat.get(Calendar.YEAR),
					cVormonat.get(Calendar.MONTH), true, null, theClientDto,
					false, iOptionSortierung, false,
					bTagesUndwochenmaximumIgnorieren);

			Object[][] zeilenVormonat = monatsabrechnungVormonatDto.getData();

			int iKWErster = calErstesZeitmodell.get(Calendar.WEEK_OF_YEAR);

			for (int i = zeilenVormonat.length - 1; i > 0; --i) {
				Object[] zeileVormonat = zeilenVormonat[i];
				int iKWVormonatszeile = (Integer) zeileVormonat[REPORT_MONATSABRECHNUNG_KALENDERWOCHE];
				if (iKWErster == iKWVormonatszeile) {

					BigDecimal bdIstVormonat = (BigDecimal) zeileVormonat[REPORT_MONATSABRECHNUNG_IST];
					if (bdIstVormonat != null) {
						dWochensummeIstOhneSonderzeiten = dWochensummeIstOhneSonderzeiten
								+ bdIstVormonat.doubleValue();
					}

				}

			}

		}

		double dWochensummeSollFuerUestd = 0;
		double dWochensummeFtgSoll = 0;

		ArrayList<ZeileMonatsabrechnungDto> monatsabrechnungZeilen = new ArrayList<ZeileMonatsabrechnungDto>();
		// -------NEU

		// Fuer Wochesumme Uestd50
		cal.set(iJahr.intValue(), iMonat.intValue(), 1, 0, 0, 0);

		for (int i = 1; i < lAnzahlTageImMonat + 1; i++) {
			ArrayList<ArrayList<ZeitdatenDto>> bloecke = monatsDaten[i];
			if (bloecke.size() == 0) {
				bloecke.add(new ArrayList<ZeitdatenDto>());
			}
			// Datum setzen
			cal.set(iJahr.intValue(), iMonat.intValue(), i, 0, 0, 0);
			cal.set(Calendar.MILLISECOND, 0);

			// Sollzeit holen
			java.sql.Time tSollzeit = null;
			double dSollzeitGesamt = 0;
			ZeitmodelltagDto zeitodelltagDto = null;
			try {
				zeitodelltagDto = getZeitmodelltagZuDatum(personalIId,
						new java.sql.Timestamp(cal.getTimeInMillis()),
						tagesartIId_Feiertag, tagesartIId_Halbtag, false,
						theClientDto);

				if (zeitodelltagDto != null) {
					tSollzeit = zeitodelltagDto.getUSollzeit();

				}
			} catch (Exception ex3) {
				// keine Sollzeit
			}

			// PJ17884
			// Zuerst alle Bereitschaften des Tages holen
			Integer tagesartIId = getTagesartZuDatum(personalIId,
					new Timestamp(cal.getTimeInMillis()), tagesartIId_Feiertag,
					tagesartIId_Halbtag, theClientDto);

			if (tagesartIId != null) {
				String sQuery = "select ber FROM FLRBereitschaft ber WHERE ber.t_beginn<'"
						+ Helper.formatTimestampWithSlashes(new java.sql.Timestamp(
								cal.getTimeInMillis() + (24 * 3600000)))
						+ "' AND ber.flrpersonal.i_id="
						+ personalIId
						+ " AND ber.t_ende>='"
						+ Helper.formatTimestampWithSlashes(new java.sql.Timestamp(
								cal.getTimeInMillis())) + "'";

				Session sessionBereitschaft = FLRSessionFactory.getFactory()
						.openSession();

				org.hibernate.Query heutigebereitschaften = sessionBereitschaft
						.createQuery(sQuery);

				List<?> resultList = heutigebereitschaften.list();

				Iterator<?> resultListIterator = resultList.iterator();

				while (resultListIterator.hasNext()) {

					FLRBereitschaft bereitschaften = (FLRBereitschaft) resultListIterator
							.next();

					BereitschafttagDto bereitschafttagDto = getBereitschafttagZuDatum(
							personalIId,
							new java.sql.Timestamp(cal.getTimeInMillis()),
							tagesartIId_Feiertag, tagesartIId_Halbtag, false,
							bereitschaften.getFlrbereitschaftart().getI_id(),
							theClientDto);

					Query query = em
							.createNamedQuery("BereitschafttagfindByBereitschaftartIIdTagesartIId");
					query.setParameter(1, bereitschaften
							.getFlrbereitschaftart().getI_id());
					query.setParameter(2, bereitschafttagDto.getTagesartIId());
					// @todo getSingleResult oder
					// getResultList
					// ?
					Collection c = query.getResultList();
					Iterator it = c.iterator();
					while (it.hasNext()) {
						Bereitschafttag bereitschafttag = (Bereitschafttag) it
								.next();

						int z = 0;

						Timestamp tVon = new java.sql.Timestamp(
								cal.getTimeInMillis());

						if (bereitschaften.getT_beginn().after(tVon)) {
							tVon = new java.sql.Timestamp(bereitschaften
									.getT_beginn().getTime());
						}

						Timestamp bereitschafttagBeginn = new java.sql.Timestamp(
								cal.getTimeInMillis()
										+ (bereitschafttag.getUBeginn()
												.getTime() + 3600000));

						if (bereitschafttagBeginn.after(tVon)) {
							tVon = bereitschafttagBeginn;
						}

						if (bereitschaften.getT_ende().before(
								bereitschafttagBeginn)) {
							continue;
						}

						Timestamp tBis = new java.sql.Timestamp(
								cal.getTimeInMillis() + (24 * 3600000));

						if (bereitschaften.getT_ende().before(tBis)) {
							tBis = new java.sql.Timestamp(bereitschaften
									.getT_ende().getTime());
						}

						Timestamp bereitschafttagEnde = null;
						if (bereitschafttag.getUEnde() != null) {
							bereitschafttagEnde = new java.sql.Timestamp(
									cal.getTimeInMillis()
											+ bereitschafttag.getUEnde()
													.getTime() + 3600000);
						} else {
							bereitschafttagEnde = new java.sql.Timestamp(
									cal.getTimeInMillis() + (24 * 3600000));
						}

						if (bereitschaften.getT_beginn().after(
								bereitschafttagEnde)) {
							continue;
						}

						if (bereitschafttagEnde.before(tBis)) {
							tBis = bereitschafttagEnde;
						}

						Timestamp tVonFuerReport = new Timestamp(tVon.getTime());
						Timestamp tBisFuerReport = new Timestamp(tBis.getTime());

						MonatsabrechnungBereitschaftDto berDto = new MonatsabrechnungBereitschaftDto();
						berDto.setBereitschaftsart(bereitschaften
								.getFlrbereitschaftart().getC_bez());

						BetriebskalenderDto dto = getPersonalFac()
								.betriebskalenderFindByMandantCNrDDatum(
										Helper.cutTimestamp(new java.sql.Timestamp(
												cal.getTimeInMillis())),
										theClientDto.getMandant(), theClientDto);
						if (dto != null) {

							if (dto.getTagesartIId().equals(
									tagesartIId_Feiertag)
									|| dto.getTagesartIId().equals(
											tagesartIId_Halbtag)) {
								berDto.setFeiertag(dto.getCBez());
							}
						}

						berDto.setTagesartCNr(kurzeWochentage[cal
								.get(Calendar.DAY_OF_WEEK)]);

						berDto.setKw(new Integer(cal.get(Calendar.WEEK_OF_YEAR)));
						berDto.setBemerkung(bereitschaften.getC_bemerkung());

						// Nun KOMMT-GEHT Bloecke beruecksichtigen

						boolean bBereitschaftBereitsHinzugefuegt = false;

						for (int j = 0; j < bloecke.size(); j++) {

							ArrayList<ZeitdatenDto> einBlock = (ArrayList<ZeitdatenDto>) bloecke
									.get(j);
							if (einBlock.size() > 1) {

								// Erste Zeile muss Kommt sein
								ZeitdatenDto kommtDto = einBlock.get(0);

								// Letzt Zeile muss Geht sein
								ZeitdatenDto gehtDto = einBlock.get(einBlock
										.size() - 1);

								if (tBis.before(kommtDto.getTZeit())
										&& tVon.after(gehtDto.getTZeit())) {
									continue;
								}

								// 1ter Teil: KOMMT ist VOR dem
								// Bereitschaftsbeginn und GEHT nach dem
								// Bereitschaftsbeginn
								if (kommtDto.getTZeit().before(tVon)
										&& gehtDto.getTZeit().after(tVon)) {

									tVon = new Timestamp(gehtDto.getTZeit()
											.getTime());

									if (bloecke.size() == 1) {
										tVonFuerReport = tVon;
										continue;
									} else {
										if (j == 0) {

											ArrayList<ZeitdatenDto> naechsterBlock = (ArrayList<ZeitdatenDto>) bloecke
													.get(j + 1);
											if (naechsterBlock.size() > 1) {

												// Erste Zeile muss Kommt
												// sein
												ZeitdatenDto kommtDtoNaechstes = naechsterBlock
														.get(0);

												MonatsabrechnungBereitschaftDto berDtoTeil1 = berDto
														.clone();
												berDtoTeil1.settVon(tVon);
												berDtoTeil1
														.settBis(kommtDtoNaechstes
																.getTZeit());// naechstes
																				// Kommt
												alBereitschaften
														.add(berDtoTeil1);

												bBereitschaftBereitsHinzugefuegt = true;
											}
										}
									}
								}

								// Komm-Geht Block ist zwischen drin
								if (kommtDto.getTZeit().after(tVon)
										&& gehtDto.getTZeit().before(tBis)) {

									// 2 Neue Zeilen erstellen

									// Wenns der 1. ist ->
									MonatsabrechnungBereitschaftDto berDtoTeil = berDto
											.clone();
									berDtoTeil.settVon(tVon);
									berDtoTeil.settBis(kommtDto.getTZeit());
									alBereitschaften.add(berDtoTeil);

									bBereitschaftBereitsHinzugefuegt = true;

									// Nun noch bis zum naechsten Kommt oder
									// T_BIS
									if (j == bloecke.size() - 1) {
										// Wenns der letzte ist bis zum T_BIS
										MonatsabrechnungBereitschaftDto berDtoTeil1 = berDto
												.clone();
										berDtoTeil1.settVon(gehtDto.getTZeit());
										berDtoTeil1.settBis(tBis);
										alBereitschaften.add(berDtoTeil1);
										bBereitschaftBereitsHinzugefuegt = true;

									} else {
										// Wenns nicht der letzte ist, bis zum
										// naechsten Kommt
										ArrayList<ZeitdatenDto> naechsterBlock = (ArrayList<ZeitdatenDto>) bloecke
												.get(j + 1);
										if (naechsterBlock.size() > 1) {

											// Erste Zeile muss Kommt
											// sein
											ZeitdatenDto kommtDtoNaechstes = naechsterBlock
													.get(0);

											MonatsabrechnungBereitschaftDto berDtoTeil1 = berDto
													.clone();
											berDtoTeil1.settVon(gehtDto
													.getTZeit());

											// Ausser das bis_bereitschaft ist <
											// als das Kommt
											if (tBis.before(kommtDtoNaechstes
													.getTZeit())) {
												berDtoTeil1.settBis(tBis);
											} else {
												berDtoTeil1
														.settBis(kommtDtoNaechstes
																.getTZeit());
											}

											alBereitschaften.add(berDtoTeil1);
											bBereitschaftBereitsHinzugefuegt = true;

										}

									}

								}
								// 1ter Teil: KOMMT ist VOR dem
								// Bereitschaftsende und GEHT nach dem
								// Bereitschaftsende
								if (kommtDto.getTZeit().before(tBis)
										&& gehtDto.getTZeit().after(tBis)) {

									tBisFuerReport = kommtDto.getTZeit();

								}
							}
						}

						if (bBereitschaftBereitsHinzugefuegt == false) {

							berDto.settVon(tVonFuerReport);
							berDto.settBis(tBisFuerReport);

							alBereitschaften.add(berDto);
						}

					}

				}
				sessionBereitschaft.close();
			}

			if (bVonBisZeiterfassungOhneKommtGeht == true) {
				bloecke = new ArrayList<ArrayList<ZeitdatenDto>>();
				bloecke.add(new ArrayList<ZeitdatenDto>());
			}

			for (int j = 0; j < bloecke.size(); j++) {

				// EIN KOMMT-GEHT BLOCK
				ZeileMonatsabrechnungDto zeile = new ZeileMonatsabrechnungDto();

				// Bemerkung initialisieren
				zeile.setSBemerkung("");
				// Kalenderwoche setzten (danach wird im Report gruppiert)
				zeile.setIKw(new Integer(cal.get(Calendar.WEEK_OF_YEAR)));
				// Tagesbezeichnung setzen (Mo,Di,Mi) usw
				zeile.setSTag(kurzeWochentage[cal.get(Calendar.DAY_OF_WEEK)]);
				// Jahr setzen
				zeile.setIJahr(new Integer(cal.get(Calendar.YEAR)));
				zeile.setIMonat(new Integer(cal.get(Calendar.MONTH)));
				// Datum setzen
				zeile.setITag(i);
				zeile.setTDatum(new Timestamp(cal.getTimeInMillis()));

				ZeitmodelltagDto zeitmodelltagDto = getZeitmodelltagZuDatum(
						personalIId, new Timestamp(cal.getTime().getTime()),
						tagesartIId_Feiertag, tagesartIId_Halbtag, false,
						theClientDto);

				if (zeitmodelltagDto != null) {
					// Rundung zugunsten des Unternehmens
					ArrayList<ZeitdatenDto> einBlock_ZurZeitweiligenVerwendung = (ArrayList<ZeitdatenDto>) bloecke
							.get(j);

					einBlock_ZurZeitweiligenVerwendung = rundungZugunstenDesUnternehmens(
							einBlock_ZurZeitweiligenVerwendung,
							zeitmodelltagDto);

					// Fruehestes KOMMT und spaetestes GEHT verschieben, wenn
					// gesetzt
					if (bTagesUndwochenmaximumIgnorieren == false) {

						Double dMaximaleTagesanwesenheitszeitAufgrundWochenMaximum = null;
						if (nMaximalesWochenIST != null
								&& nMaximalesWochenIST.doubleValue() > 0) {

							dMaximaleTagesanwesenheitszeitAufgrundWochenMaximum = nMaximalesWochenIST
									.doubleValue()
									- dWochensummeIstOhneSonderzeiten;
						}

						if (i == 13) {
							int z = 0;
						}

						einBlock_ZurZeitweiligenVerwendung = fruehestesKommtUndSpaetestesGehtUndMaximaleTagesanwesenheitVerschieben(
								taetigkeitDto_Unter, taetigkeitDto_Arzt,
								taetigkeitDto_Behoerde,
								einBlock_ZurZeitweiligenVerwendung,
								zeitmodelltagDto,
								dMaximaleTagesanwesenheitszeitAufgrundWochenMaximum);
					}

					bloecke.set(j, einBlock_ZurZeitweiligenVerwendung);
				}

				if (zeile.getTDatum().compareTo(dEintrittsdatum) >= 0
						&& zeile.getTDatum().before(dAustrittsdatum)) {
					int iMinutenabzug = 0;
					ArrayList<?> einBlock = (ArrayList<?>) bloecke.get(j);
					ZeitdatenDto[] zeitdatenEinesTagesDtos = new ZeitdatenDto[einBlock
							.size()];
					zeitdatenEinesTagesDtos = (ZeitdatenDto[]) einBlock
							.toArray(zeitdatenEinesTagesDtos);

					if (zeitdatenEinesTagesDtos == null) {
						zeitdatenEinesTagesDtos = new ZeitdatenDto[0];
					}

					String sBemerkung = "";
					double dIst = 0;

					try {
						// Vergleichen, ob Zeitmodell gewechselt wurde
						PersonalzeitmodellDto personalzeitmodellDto = getPersonalFac()
								.personalzeitmodellFindZeitmodellZuDatum(
										personalIId,
										new Timestamp(cal.getTime().getTime()),
										theClientDto);
						if (personalzeitmodellDto != null) {
							// Wenn Zeitmodellwechsel, dann auf Ausdruck
							// anzeigen

							ZeitmodellDto zmDto = zeitmodellFindByPrimaryKey(
									personalzeitmodellDto.getZeitmodellIId(),
									theClientDto);

							iMinutenabzug = zmDto.getIMinutenabzug();

							zeile.setSZeitmodell(zmDto.getBezeichnung());
							iZeitmodellId = personalzeitmodellDto
									.getZeitmodellIId();
							if (iZeitmodellId != null) {
								if (cal.get(cal.DATE) > 1) {
									if (!iZeitmodellId
											.equals(iZeitmodellIdVortag)) {
										sBemerkung = sBemerkung
												+ getTextRespectUISpr(
														"pers.monatsabrechnung.zeitmodellwechsel",
														theClientDto
																.getMandant(),
														theClientDto.getLocUi())
												+ " "
												+ personalzeitmodellDto
														.getZeitmodellDto()
														.getCNr() + ",";
									}
								}
							}
							iZeitmodellIdVortag = personalzeitmodellDto
									.getZeitmodellIId();
						} else {
							iMinutenabzug = 0;
						}
					} catch (RemoteException ex1) {
						// nothing here
					}

					if (zeitdatenEinesTagesDtos.length > 0) {

						// SP1451
						if (iMinutenabzug > 0) {
							// Bei Kommt und Geht den Minutanbzug jeweils
							// zur
							// Haelfte durchfuhren
							if (bloecke.size() == 1) {

								zeitdatenEinesTagesDtos[0]
										.setTZeit(new Timestamp(
												zeitdatenEinesTagesDtos[0]
														.getTZeit().getTime()
														+ ((iMinutenabzug / 2) * 1000 * 60)));
								zeitdatenEinesTagesDtos[zeitdatenEinesTagesDtos.length - 1]
										.setTZeit(new Timestamp(
												zeitdatenEinesTagesDtos[zeitdatenEinesTagesDtos.length - 1]
														.getTZeit().getTime()
														- ((iMinutenabzug / 2) * 1000 * 60)));
							} else if (bloecke.size() > 1) {

								if (j == 0) {
									zeitdatenEinesTagesDtos[0]
											.setTZeit(new Timestamp(
													zeitdatenEinesTagesDtos[0]
															.getTZeit()
															.getTime()
															+ ((iMinutenabzug / 2) * 1000 * 60)));
								}
								if (j == bloecke.size() - 1) {
									zeitdatenEinesTagesDtos[zeitdatenEinesTagesDtos.length - 1]
											.setTZeit(new Timestamp(
													zeitdatenEinesTagesDtos[zeitdatenEinesTagesDtos.length - 1]
															.getTZeit()
															.getTime()
															- ((iMinutenabzug / 2) * 1000 * 60)));
								}

							}

						}
						if (!zeitdatenEinesTagesDtos[0].getTaetigkeitIId()
								.equals(taetigkeitIId_Kommt)) {
							sBemerkung = getTextRespectUISpr(
									"pers.zeiterfassung.kommmtmussderersteeintragsein",
									theClientDto.getMandant(),
									theClientDto.getLocUi())
									+ sBemerkung;
						}

						if (!zeitdatenEinesTagesDtos[zeitdatenEinesTagesDtos.length - 1]
								.getTaetigkeitIId().equals(taetigkeitIId_Geht)) {
							sBemerkung = getTextRespectUISpr(
									"pers.zeiterfassung.gehtmussderletzteeintragsein",
									theClientDto.getMandant(),
									theClientDto.getLocUi())
									+ sBemerkung;
						}

						// Ersten und letzten Eintrag des Tages setzen
						java.sql.Time t1 = new java.sql.Time(
								zeitdatenEinesTagesDtos[0].getTZeit().getTime());
						java.sql.Time t2 = new java.sql.Time(
								zeitdatenEinesTagesDtos[zeitdatenEinesTagesDtos.length - 1]
										.getTZeit().getTime());

						long l1 = zeitdatenEinesTagesDtos[0].getTZeit()
								.getTime();
						long l2 = zeitdatenEinesTagesDtos[zeitdatenEinesTagesDtos.length - 1]
								.getTZeit().getTime();

						double differenzInMS = (l2 - l1);
						dIst = differenzInMS / 3600000;

						zeile.setTVon(t1);
						zeile.setTBis(t2);
					}

					// Ueber jede Zeitbuchung iterieren
					double dSummeTagUNTER = 0;
					double dSummeTagARZT = 0;
					double dSummeTagBEHOERDE = 0;
					double dSummeTagZEITAUSGLEICH = 0;
					double dSummeTagKRANK = 0;
					double dSummeTagKINDKRANK = 0;
					double dSummeTagURLAUB_STUNDEN = 0;
					double dSummeTagSONSTIGE_BEZAHLT = 0;
					double dSummeTagSONSTIGE_BEZAHLT_OHNE_FAKTOR = 0;
					double dSummeTagSONSTIGE_NICHTBEZAHLT = 0;
					double dSummeTagKOMMTGEHT = 0;

					String sZusatzbezeichnung = "";
					ZeitdatenDto zeitdatenDto_Vorher = null;

					if (bVonBisZeiterfassungOhneKommtGeht == false) {

						for (int m = 0; m < zeitdatenEinesTagesDtos.length; m++) {

							ZeitdatenDto zeitdatenDto_Aktuell = zeitdatenEinesTagesDtos[m];

							// Milliskunden auf 0 setzen
							Timestamp tsTemp = zeitdatenDto_Aktuell.getTZeit();
							Calendar c2 = Calendar.getInstance();
							c2.setTimeInMillis(tsTemp.getTime());
							c2.set(Calendar.MILLISECOND, 0);
							tsTemp = new Timestamp(c2.getTimeInMillis());
							zeitdatenDto_Aktuell.setTZeit(tsTemp);
							// Bei jeder Geraden Zahl mit dem Vorgaenger
							// vergleichen
							if (m % 2 == 0 && m != 0) {
								if (zeitdatenDto_Aktuell.getTaetigkeitIId()
										.equals(taetigkeitIId_Kommt)
										&& zeitdatenDto_Vorher
												.getTaetigkeitIId().equals(
														taetigkeitIId_Geht)) {
									// Wenn KOMMT - GEHT, dann nicht dazuzaehlen

									Double dBeginn = Helper
											.time2Double(new Time(
													zeitdatenDto_Vorher
															.getTZeit()
															.getTime()));
									Double dEnde = Helper.time2Double(new Time(
											zeitdatenDto_Aktuell.getTZeit()
													.getTime()));
									double dSumme = dEnde.doubleValue()
											- dBeginn.doubleValue();
									dSummeTagKOMMTGEHT = dSummeTagKOMMTGEHT
											+ dSumme;
								} else if (zeitdatenDto_Aktuell
										.getTaetigkeitIId().equals(
												zeitdatenDto_Vorher
														.getTaetigkeitIId())) {

									Double dBeginn = Helper
											.time2Double(new Time(
													zeitdatenDto_Vorher
															.getTZeit()
															.getTime()));
									Double dEnde = Helper.time2Double(new Time(
											zeitdatenDto_Aktuell.getTZeit()
													.getTime()));

									if (dBeginn != null && dEnde != null) {
										double dSumme = dEnde.doubleValue()
												- dBeginn.doubleValue();

										// Wenn Taetigkeit UNTER dann bei UNTER
										// hinzufuegen
										if (zeitdatenDto_Aktuell
												.getTaetigkeitIId().equals(
														taetigkeitDto_Unter
																.getIId())) {
											dSummeTagUNTER = dSummeTagUNTER
													+ dSumme;
										}
										// Wenn Taetigkeit BEHOERDE dann bei
										// BEHOERDE hinzufuegen
										else if (zeitdatenDto_Aktuell
												.getTaetigkeitIId().equals(
														taetigkeitDto_Behoerde
																.getIId())) {
											dSummeTagBEHOERDE = dSummeTagBEHOERDE
													+ dSumme;
										}
										// Wenn Taetigkeit URLAUB dann bei
										// URLAUB
										// hinzufuegen
										else if (zeitdatenDto_Aktuell
												.getTaetigkeitIId().equals(
														taetigkeitDto_Urlaub
																.getIId())) {
											dSummeTagURLAUB_STUNDEN = dSummeTagURLAUB_STUNDEN
													+ dSumme;
										}
										// Wenn Taetigkeit ARZT dann bei ARZT
										// hinzufuegen
										else if (zeitdatenDto_Aktuell
												.getTaetigkeitIId().equals(
														taetigkeitDto_Arzt
																.getIId())) {
											dSummeTagARZT = dSummeTagARZT
													+ dSumme;
										}
										// Wenn Taetigkeit ZA dann bei ZA
										// hinzufuegen
										else if (zeitdatenDto_Aktuell
												.getTaetigkeitIId().equals(
														taetigkeitDto_ZA
																.getIId())) {
											dSummeTagZEITAUSGLEICH = dSummeTagZEITAUSGLEICH
													+ dSumme;
										}
										// Wenn Taetigkeit KRANK dann bei KRANK
										// hinzufuegen
										else if (zeitdatenDto_Aktuell
												.getTaetigkeitIId().equals(
														taetigkeitDto_Krank
																.getIId())) {
											dSummeTagKRANK = dSummeTagKRANK
													+ dSumme;
										}
										// Wenn Taetigkeit KINDKRANK dann bei
										// KINDKRANK
										// hinzufuegen
										else if (zeitdatenDto_Aktuell
												.getTaetigkeitIId().equals(
														taetigkeitDto_Kindkrank
																.getIId())) {
											dSummeTagKINDKRANK = dSummeTagKINDKRANK
													+ dSumme;
										}
										// Wenn KOMMT, dann Mehrfaches KOMMT
										else if (zeitdatenDto_Aktuell
												.getTaetigkeitIId().equals(
														taetigkeitIId_Kommt)) {
											sBemerkung = "Mehrfaches KOMMT"
													+ sBemerkung;
										}
										// Wenn GEHT, dann Mehrfaches GEHT
										else if (zeitdatenDto_Aktuell
												.getTaetigkeitIId().equals(
														taetigkeitIId_Geht)) {
											sBemerkung = "Mehrfaches GEHT"
													+ sBemerkung;
										}

										// Ansonsten muss Taetigkeit bei
										// sonstigen
										// bezahlten oder nicht bezahlten
										// Taetigkeiten dabei sein
										else {
											dSumme = Helper.rundeKaufmaennisch(
													new BigDecimal(dSumme), 2)
													.doubleValue();
											// Bezahlte Taetigkeiten
											for (int k = 0; k < sonstigeTaetigkeiten.length; k++) {
												if (zeitdatenDto_Aktuell
														.getTaetigkeitIId()
														.equals(sonstigeTaetigkeiten[k]
																.getIId())) {

													if (sonstigeTaetigkeiten[k]
															.getFBezahlt() > 0) {

														dSummeTagSONSTIGE_BEZAHLT_OHNE_FAKTOR += dSumme;
														dSumme = Helper
																.rundeKaufmaennisch(
																		new BigDecimal(
																				dSumme)
																				.multiply(new BigDecimal(
																						sonstigeTaetigkeiten[k]
																								.getFBezahlt()
																								.doubleValue() / 100)),
																		2)
																.doubleValue();

														dSummeTagSONSTIGE_BEZAHLT = dSummeTagSONSTIGE_BEZAHLT
																+ dSumme;
													} else {
														dSummeTagSONSTIGE_NICHTBEZAHLT = dSummeTagSONSTIGE_NICHTBEZAHLT
																+ dSumme;
													}

													sZusatzbezeichnung = sZusatzbezeichnung
															+ sonstigeTaetigkeiten[k]
																	.getCNr()
															+ " "
															+ dSumme
															+ ",";
												}
											}

										}
									}
								} else {
									sBemerkung = "Fehler in Buchungen"
											+ sBemerkung;
								}
							}

							zeitdatenDto_Vorher = zeitdatenDto_Aktuell;
						}

					} else {
						// PJ18440

						VonBisErfassungTagesdatenDto vbDto = berechneTagesArbeitszeitVonBisZeiterfassungOhneKommtGeht(
								personalIId, new java.sql.Date(cal.getTime()
										.getTime()), theClientDto);

						dSummeTagUNTER = vbDto.getdUnter();
						dSummeTagARZT = vbDto.getdArzt();
						dSummeTagBEHOERDE = vbDto.getdBehoerde();
						dSummeTagZEITAUSGLEICH = vbDto.getdZeitausgleich();
						dSummeTagKRANK = vbDto.getdKrank();
						dSummeTagKINDKRANK = vbDto.getdKindkrank();
						dSummeTagURLAUB_STUNDEN = vbDto.getdUrlaub();

						dIst = vbDto.getdIst();
					}

					// Zulagen in Bemerkung schreiben

					if (hmBelegeMitZulagen.containsKey(i)) {
						HashMap<Integer, Double> hmBelege = hmBelegeMitZulagen
								.get(i);
						Iterator belege = hmBelege.keySet().iterator();

						ArrayList al = new ArrayList();

						// Zulagen in Bemerkung schreiben
						while (belege.hasNext()) {
							Integer artikelIId = (Integer) belege.next();
							Double dDauer = hmBelege.get(artikelIId);

							String zulagenBezeichnung = null;

							for (int k = 0; k < zulagenDtos.length; k++) {
								ArtikelzulageDto dto = zulagenDtos[k];
								if (zeile.getTDatum().getTime() >= dto
										.getTGueltigab().getTime()
										&& artikelIId.equals(dto
												.getArtikelIId())) {

									zulagenBezeichnung = dto.getZulageDto()
											.getCBez();
								}
							}

							if (zulagenBezeichnung != null) {
								Object[] oZeile = new Object[2];
								oZeile[0] = zulagenBezeichnung;
								oZeile[1] = dDauer;
								al.add(oZeile);
							}

						}

						String[] fieldnames = new String[] { "F_ZULAGE",
								"F_DAUER" };

						Object[][] dataSub = new Object[al.size()][fieldnames.length];
						dataSub = (Object[][]) al.toArray(dataSub);

						zeile.setSubreportZulagen(new LPDatenSubreport(dataSub,
								fieldnames));
					}

					dIst = dIst - dSummeTagUNTER - dSummeTagARZT
							- dSummeTagKRANK - dSummeTagKINDKRANK
							- dSummeTagZEITAUSGLEICH - dSummeTagBEHOERDE
							- dSummeTagURLAUB_STUNDEN
							- dSummeTagSONSTIGE_BEZAHLT_OHNE_FAKTOR
							- dSummeTagSONSTIGE_NICHTBEZAHLT
							- dSummeTagKOMMTGEHT;

					// PJ 16676
					if (zeitmodelltagDto != null
							&& zeitmodelltagDto.getUErlaubteanwesenheitszeit() != null
							&& zeitmodelltagDto.getUErlaubteanwesenheitszeit()
									.getTime() != -3600000) {
						if (bTagesUndwochenmaximumIgnorieren == false) {
							double dMaxAnwesenheit = Helper
									.time2Double(zeitmodelltagDto
											.getUErlaubteanwesenheitszeit());
							if (dIst > dMaxAnwesenheit) {
								dIst = dMaxAnwesenheit;
							}
						}
					}

					zeile.setBdIst(Helper.rundeKaufmaennisch(new BigDecimal(
							dIst), 2));

					zeile.setBdUnter(Helper.rundeKaufmaennisch(new BigDecimal(
							dSummeTagUNTER), 2));
					zeile.setBdArzt(Helper.rundeKaufmaennisch(new BigDecimal(
							dSummeTagARZT), 2));
					zeile.setBdBehoerde(Helper.rundeKaufmaennisch(
							new BigDecimal(dSummeTagBEHOERDE), 2));
					zeile.setBdUrlaubStunden(Helper.rundeKaufmaennisch(
							new BigDecimal(dSummeTagURLAUB_STUNDEN), 2));
					zeile.setBdUrlaubTage(new BigDecimal(0));
					zeile.setBdZA(Helper.rundeKaufmaennisch(new BigDecimal(
							dSummeTagZEITAUSGLEICH), 2));
					zeile.setBdKrank(Helper.rundeKaufmaennisch(new BigDecimal(
							dSummeTagKRANK), 2));
					zeile.setBdKindkrank(Helper.rundeKaufmaennisch(
							new BigDecimal(dSummeTagKINDKRANK), 2));
					zeile.setBdSonstigeBezahlt(Helper.rundeKaufmaennisch(
							new BigDecimal(dSummeTagSONSTIGE_BEZAHLT), 2));
					zeile.setBdSonstigeNichtBezahlt(Helper.rundeKaufmaennisch(
							new BigDecimal(dSummeTagSONSTIGE_NICHTBEZAHLT), 2));

					// Nur beim ersten Block rechnen

					// Berechnen der Summen: Arzt, Unterbrechung und
					// Behoerde

					double d_zeitdec = 0;

					// Alle Tageswiesen Spalten nur einmal zaehlen
					if (j == 0) {

						zeile.setBdReise(monatsDatenReisezeiten[i]);

						if (tSollzeit != null) {
							zeile.setBdSoll(new BigDecimal(Helper.time2Double(
									tSollzeit).doubleValue())); // Sollzeit
																// zu
							// dem Datum
							d_zeitdec = Helper.time2Double(tSollzeit)
									.doubleValue();
							dSollzeitGesamt = d_zeitdec;
						} else {
							zeile.setBdSoll(new BigDecimal(0));
						}
						// --------- FEIERTAGBERECHNUNG
						// Feiertagsstunden berechnen
						boolean bIstHalberFeiertag = false;

						BetriebskalenderDto dto = getPersonalFac()
								.betriebskalenderFindByMandantCNrDDatum(
										Helper.cutTimestamp(new Timestamp(cal
												.getTime().getTime())),
										theClientDto.getMandant(), theClientDto);

						if (dto != null) {

							if (dto.getTagesartIId()
									.equals(tagesartIId_Halbtag)) {
								bIstHalberFeiertag = true;
							}

							boolean bHinzurechnenWeilReligionStimmt = false;
							if (personalDto.getReligionIId() == null
									&& dto.getReligionIId() == null) {
								bHinzurechnenWeilReligionStimmt = true;
							} else if (personalDto.getReligionIId() != null
									&& dto.getReligionIId() == null) {
								bHinzurechnenWeilReligionStimmt = true;
							} else if (personalDto.getReligionIId() != null
									&& personalDto.getReligionIId().equals(
											dto.getReligionIId())) {
								bHinzurechnenWeilReligionStimmt = true;
							}

							double dFtgSoll = 0;
							double dGutschriftFeiertag = 0;
							if (iZeitmodellId != null
									&& bHinzurechnenWeilReligionStimmt) {
								try {
									Query query = em
											.createNamedQuery("ZeitmodelltagfindByZeitmodellIIdTagesartIId");
									query.setParameter(1, iZeitmodellId);
									query.setParameter(2, dto.getTagesartIId());
									// @todo getSingleResult oder
									// getResultList
									// ?
									Zeitmodelltag zeimodelltag = (Zeitmodelltag) query
											.getSingleResult();
									dFtgSoll = Helper.time2Double(
											zeimodelltag.getUSollzeit())
											.doubleValue();

									if (dFtgSoll == 0 || bIstHalberFeiertag) {
										throw new NoResultException("");
									}
								} catch (NoResultException ex14) {
									// keine Sollzeit fuer Tagesart
									// definiert
									Time usoll = getSollzeitZuDatumWennFeiertag(
											personalIId, new Timestamp(cal
													.getTime().getTime()),
											theClientDto);
									if (usoll != null) {

										if (bIstHalberFeiertag) {
											dFtgSoll = Helper
													.time2Double(usoll)
													.doubleValue()
													- dFtgSoll;
											if (dFtgSoll > 0) {
												dSummeMonatFeiertagSoll += dFtgSoll;
												dGutschriftFeiertag = dFtgSoll;
											}

										} else {
											dFtgSoll = Helper
													.time2Double(usoll)
													.doubleValue();
											dSummeMonatFeiertagSoll += dFtgSoll;
											dGutschriftFeiertag = dFtgSoll;
										}
									}
								}

							}

							if (dto.getReligionIId() == null) {

								zeile.setBdFeiertag(new BigDecimal(
										dGutschriftFeiertag));
								String s = "";
								if (dto.getCBez() != null) {
									s = dto.getCBez();
								} else {
									s = "Unbek. Ftg.";
								}
								zeile.setSBemerkung(zeile.getSBemerkung() + s);
							} else {

								if (dto.getReligionIId().equals(
										personalDto.getReligionIId())) {

									zeile.setBdFeiertag(new BigDecimal(
											dGutschriftFeiertag));
									String s = "";
									if (dto.getCBez() != null) {
										s = dto.getCBez();
									} else {
										s = "Unbek. Ftg.";
									}
									zeile.setSBemerkung(zeile.getSBemerkung()
											+ s);

								} else {
									zeile.setBdFeiertag(new BigDecimal(0));
								}

							}

						} else {
							zeile.setBdFeiertag(new BigDecimal(0));
						}

						// ---------ENDE FEIERTAGBERECHNUNG

						// NEUE BERECHNUNG TAGEWEISE TAETIGKEITEN
						SonderzeitenDto[] sonderzeitenDtos = sonderzeitenFindByPersonalIIdDDatum(
								personalIId, Helper.cutTimestamp(new Timestamp(
										cal.getTime().getTime())));

						BigDecimal bdZeitDecimal = null;

						for (int m = 0; m < sonderzeitenDtos.length; m++) {
							SonderzeitenDto sonderzeitenDto = sonderzeitenDtos[m];

							if (Helper.short2boolean(sonderzeitenDto.getBTag())
									|| Helper.short2boolean(sonderzeitenDto
											.getBHalbtag())) {
								bdZeitDecimal = Helper.rundeKaufmaennisch(
										new BigDecimal(d_zeitdec), 4);
							} else {
								bdZeitDecimal = Helper.rundeKaufmaennisch(
										new BigDecimal(Helper.time2Double(
												sonderzeitenDto.getUStunden())
												.doubleValue()), 4);
							}

							// PJ 15914

							TaetigkeitDto taetigkeitDto = taetigkeitFindByPrimaryKey(
									sonderzeitenDto.getTaetigkeitIId(),
									theClientDto);
							if (taetigkeitDto.getFBezahlt() > 0) {
								bdZeitDecimal = Helper.rundeKaufmaennisch(
										bdZeitDecimal.multiply(new BigDecimal(
												taetigkeitDto.getFBezahlt()
														.doubleValue() / 100)),
										4);
							}

							// Wenn Taetigkeit URLAUB dann bei URLAUB
							// hinzufuegen
							if (sonderzeitenDto.getTaetigkeitIId().equals(
									taetigkeitDto_Urlaub.getIId())) {
								sBemerkung += taetigkeitDto_Urlaub
										.getBezeichnung();
								if (Helper.short2boolean(sonderzeitenDto
										.getBTag())) {
									zeile.setBdUrlaubTage(zeile
											.getBdUrlaubTage().add(
													new BigDecimal(1)));
									zeile.setBdUrlaubStunden(zeile
											.getBdUrlaubStunden().add(
													bdZeitDecimal));
									// 11339: Wenn ein Halber Feiertag ist,
									// dann
									// wird bei den Urlaubsstunden die
									// Sollzeit
									// des
									// Originaltags gutgeschrieben
									if (bIstHalberFeiertag) {
										ZeitmodelltagDto zeitmodelltagDtoOriginaltag = getZeitmodelltagZuDatum(
												personalIId, new Timestamp(cal
														.getTime().getTime()),
												tagesartIId_Feiertag,
												tagesartIId_Halbtag, true,
												theClientDto);

										if (zeitmodelltagDtoOriginaltag != null
												&& zeitmodelltagDtoOriginaltag
														.getUSollzeit() != null) {

											BigDecimal dbHalbtagsUrlaub = new BigDecimal(
													Helper.time2Double(
															zeitmodelltagDtoOriginaltag
																	.getUSollzeit())
															.doubleValue());
											dbHalbtagsUrlaub = Helper
													.rundeKaufmaennisch(
															dbHalbtagsUrlaub, 2);
											zeile.setBdUrlaubStunden(dbHalbtagsUrlaub);

										}
									}

								} else if (Helper.short2boolean(sonderzeitenDto
										.getBHalbtag())) {
									zeile.setBdUrlaubTage(zeile
											.getBdUrlaubTage().add(
													new BigDecimal(0.5)));
									zeile.setBdUrlaubStunden(zeile
											.getBdUrlaubStunden()
											.add(bdZeitDecimal.divide(
													new BigDecimal(2),
													BigDecimal.ROUND_HALF_EVEN)));

									// 11339: Wenn ein Halber Feiertag ist,
									// dann
									// wird bei den Urlaubsstunden die
									// Haelfte
									// der Sollzeit des
									// Originaltags gutgeschrieben
									if (bIstHalberFeiertag) {
										ZeitmodelltagDto zeitmodelltagDtoOriginaltag = getZeitmodelltagZuDatum(
												personalIId, new Timestamp(cal
														.getTime().getTime()),
												tagesartIId_Feiertag,
												tagesartIId_Halbtag, true,
												theClientDto);

										if (zeitmodelltagDtoOriginaltag != null
												&& zeitmodelltagDtoOriginaltag
														.getUSollzeit() != null) {

											BigDecimal dbHalbtagsUrlaub = new BigDecimal(
													Helper.time2Double(
															zeitmodelltagDtoOriginaltag
																	.getUSollzeit())
															.doubleValue() / 2);

											dbHalbtagsUrlaub = Helper
													.rundeKaufmaennisch(
															dbHalbtagsUrlaub, 2);
											zeile.setBdUrlaubStunden(dbHalbtagsUrlaub);

										}
									}
								} else {
									zeile.setBdUrlaubStunden(zeile
											.getBdUrlaubStunden().add(
													bdZeitDecimal));
								}
							}
							// Wenn Taetigkeit KRANK dann bei KRANK
							// hinzufuegen
							else if (sonderzeitenDto.getTaetigkeitIId().equals(
									taetigkeitDto_Krank.getIId())) {
								zeile.setBdKrank(zeile.getBdKrank().add(
										bdZeitDecimal));
								sBemerkung += taetigkeitDto_Krank
										.getBezeichnung();

							}
							// Wenn Taetigkeit KINDKRANK dann bei KINDKRANK
							// hinzufuegen
							else if (sonderzeitenDto.getTaetigkeitIId().equals(
									taetigkeitDto_Kindkrank.getIId())) {
								zeile.setBdKindkrank(zeile.getBdKindkrank()
										.add(bdZeitDecimal));
								sBemerkung += taetigkeitDto_Kindkrank
										.getBezeichnung();

							}
							// Wenn Taetigkeit ZA dann bei ZA hinzufuegen
							else if (sonderzeitenDto.getTaetigkeitIId().equals(
									taetigkeitDto_ZA.getIId())) {
								zeile.setBdZA(zeile.getBdZA()
										.add(bdZeitDecimal));
								sBemerkung += taetigkeitDto_ZA.getBezeichnung();

							}
							// Wenn Taetigkeit BEHOERDE dann bei BEHOERDE
							// hinzufuegen
							else if (sonderzeitenDto.getTaetigkeitIId().equals(
									taetigkeitDto_Behoerde.getIId())) {
								zeile.setBdBehoerde(zeile.getBdBehoerde().add(
										bdZeitDecimal));
								sBemerkung += taetigkeitDto_Behoerde
										.getBezeichnung();

							}
							// Wenn Taetigkeit ARZT dann bei ARZT
							// hinzufuegen
							else if (sonderzeitenDto.getTaetigkeitIId().equals(
									taetigkeitDto_Arzt.getIId())) {
								zeile.setBdArzt(zeile.getBdArzt().add(
										bdZeitDecimal));
								sBemerkung += taetigkeitDto_Arzt
										.getBezeichnung();

							} else {

								// Sonstige Taetigkeiten
								for (int k = 0; k < sonstigeTaetigkeiten.length; k++) {
									if (sonderzeitenDto.getTaetigkeitIId()
											.equals(sonstigeTaetigkeiten[k]
													.getIId())) {

										if (taetigkeitDto.getFBezahlt() > 0) {
											zeile.setBdSonstigeBezahlt(zeile
													.getBdSonstigeBezahlt()
													.add(bdZeitDecimal));
										} else {
											zeile.setBdSonstigeNichtBezahlt(zeile
													.getBdSonstigeNichtBezahlt()
													.add(bdZeitDecimal));
										}

										sZusatzbezeichnung = sZusatzbezeichnung
												+ sonstigeTaetigkeiten[k]
														.getBezeichnung() + " "
												+ bdZeitDecimal.doubleValue()
												+ ",";
									}
								}

							}
						}

						// NEUE BERECHNUNG TAGEWEISE TAETIGKEITEN ENDE
					}

					zeile.setSZusatzbezeichnung(sZusatzbezeichnung);

					BigDecimal dIstZeit = new BigDecimal(dIst);
					dIstZeit = Helper.rundeKaufmaennisch(dIstZeit, 2);

					// Sollzeit aufteilen auf die einzelnen Tage;

					if (j + 1 != bloecke.size()) {
						if (dIst < dSollzeitGesamt) {
							// auf 2 stellen runden
							// myDouble = Math.round( myDouble * 100. ) /
							// 100.;

							zeile.setBdSoll(Helper.rundeKaufmaennisch(
									new BigDecimal(dIst), 2));
							dSollzeitGesamt = dSollzeitGesamt - dIst;
						} else {
							zeile.setBdSoll(new BigDecimal(dSollzeitGesamt));
							dSollzeitGesamt = 0;
						}

					} else {
						// Restliche Sollzeit am letzten Tag eintragen
						zeile.setBdSoll(new BigDecimal(dSollzeitGesamt));
						dSollzeitGesamt = 0;
					}

					if (dIstZeit != null) {
						zeile.setBdIst(dIstZeit);
						double differenz = 0;
						if (dIstZeit.doubleValue() != 0) {

							differenz = dIstZeit.doubleValue()
									- zeile.getBdSoll().doubleValue();
							zeile.setBdDiff(new BigDecimal(differenz)); // Differenz
							// Soll
							// -Ist
						}
						if (dIstZeit.doubleValue() != 0
								&& zeile.getBdSoll() == null) {
							differenz = dIstZeit.doubleValue() - d_zeitdec;
							zeile.setBdDiff(new BigDecimal(differenz)); // Differenz
							// Soll
							// -Ist
						}
						if (zeile.getBdSoll() != null
								&& dIstZeit.doubleValue() == 0) {
							differenz = dIstZeit.doubleValue()
									- zeile.getBdSoll().doubleValue();
							zeile.setBdDiff(new BigDecimal(differenz)); // Differenz
							// Soll
							// -Ist
						}

						if (zeile.getBdDiff() == null) {
							zeile.setBdDiff(new BigDecimal(0));
						}
					}

					if (zeile.getSBemerkung() != null) {
						zeile.setSBemerkung(zeile.getSBemerkung() + " "
								+ sBemerkung);
					} else {
						zeile.setSBemerkung(sBemerkung);
					}

					// Ueberstundenberechnung
					double dSummeTagUESTD200 = 0;
					double dSummeTagUESTD100 = 0;
					double dSummeTagUESTD100Steuerfrei = 0;
					double dSummeTagUESTD50 = 0;
					double dSummeTagUESTD50Steuerfrei = 0;

					if (tagesartIId != null) {
						zeile.setSTagesart(tagesartFindByPrimaryKey(
								tagesartIId, theClientDto).getCNr());
					}

					if (nNormalstunden != null
							&& zeitdatenEinesTagesDtos.length > 1) {
						// Was ist Heute fuer eine Tagesart?

						boolean bUestdZaehlenErstWennSollzeitErbracht = false;

						double dBlockzeit = getBlockzeitenEinesTages(
								personalDto, zeitdatenEinesTagesDtos,
								taetigkeitIId_Kommt, taetigkeitIId_Geht);

						if (personalDto.getKollektivDto() != null
								&& Helper.short2boolean(personalDto
										.getKollektivDto()
										.getBUestdabsollstderbracht())) {

							bUestdZaehlenErstWennSollzeitErbracht = true;

							if (zeile.getBdIst().doubleValue() >= zeile
									.getBdSoll().doubleValue()) {

								// Zeit um Sollzeit verschieben
								// Wenn Ja, dann Zeitbuchungen neu setzen
								long l = zeitdatenEinesTagesDtos[0].getTZeit()
										.getTime()
										+ (long) (zeile.getBdSoll()
												.doubleValue() * 3600000)
										+ (long) (zeile.getBdUnter()
												.doubleValue() * 3600000);
								dBlockzeit = getBlockzeitenEinesTages(
										personalDto,
										ZeitdatenDto
												.kopiereArrayUndVerschiebeAnfangsZeitNachSpaeter(
														zeitdatenEinesTagesDtos,
														l),
										taetigkeitIId_Kommt, taetigkeitIId_Geht);
							}

						}

						// Wenn Tagesart Sonntag und Feiertag, DANN ist den
						// ganzen Tag Blockzeit, d.h. es sind immer
						// Steuerfreie
						// Ueberstunden
						if (tagesartIId
								.equals(tagesartFindByCNr(
										ZeiterfassungFac.TAGESART_SONNTAG,
										theClientDto).getIId())
								|| tagesartIId.equals(tagesartIId_Feiertag)) {
							dBlockzeit = 24;
						}

						if (bUestdZaehlenErstWennSollzeitErbracht == false
								|| bUestdZaehlenErstWennSollzeitErbracht == true
								&& zeile.getBdIst().doubleValue() > zeile
										.getBdSoll().doubleValue()) {

							if ((zeile.getBdFeiertag() != null && zeile
									.getBdFeiertag().doubleValue() > 0)
									|| cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
								if (personalDto.getKollektivDto() != null
										&& personalDto.getKollektivDto()
												.getN200prozentigeab() != null) {
									if (zeile.getBdIst().doubleValue() > personalDto
											.getKollektivDto()
											.getN200prozentigeab()
											.doubleValue()) {

										dSummeTagUESTD200 = zeile
												.getBdIst()
												.subtract(
														personalDto
																.getKollektivDto()
																.getN200prozentigeab())
												.doubleValue();

										dWochensummeUestd200 += dSummeTagUESTD200;

									}
								}
							}

							// Gibt es fuer diese Tagesart einen Eintrag in
							// den
							// Kollektivuestd 100 ?
							if (hmKollektivUestd100.containsKey(tagesartIId)) {
								KollektivuestdDto kollektivuestdDto = (KollektivuestdDto) hmKollektivUestd100
										.get(tagesartIId);

								ZeitdatenDto[] zeitdatenFuerUestdAbrechnung = ZeitdatenDto
										.kopiereArray(zeitdatenEinesTagesDtos);

								if (bUestdZaehlenErstWennSollzeitErbracht == true) {

									// SP2710
									long l = zeitdatenFuerUestdAbrechnung[0]
											.getTZeit().getTime()
											+ (long) (zeile.getBdSoll()
													.doubleValue() * 3600000)
											+ (long) (zeile.getBdUnter()
													.doubleValue() * 3600000);

									zeitdatenFuerUestdAbrechnung = ZeitdatenDto
											.kopiereArrayUndVerschiebeAnfangsZeitNachSpaeter(
													zeitdatenFuerUestdAbrechnung,
													l);

								}

								// --AB setzen

								Calendar cUestdTemp = Calendar.getInstance();
								cUestdTemp.setTimeInMillis(kollektivuestdDto
										.getUAb().getTime());

								// Wenn Ja, dann Zeitbuchungen neu setzen
								Calendar cUestdAb = Calendar.getInstance();
								cUestdAb.setTimeInMillis(zeitdatenEinesTagesDtos[0]
										.getTZeit().getTime());
								cUestdAb.set(Calendar.HOUR_OF_DAY,
										cUestdTemp.get(Calendar.HOUR_OF_DAY));
								cUestdAb.set(Calendar.MINUTE,
										cUestdTemp.get(Calendar.MINUTE));
								cUestdAb.set(Calendar.SECOND, 0);
								cUestdAb.set(Calendar.MILLISECOND, 0);

								zeitdatenFuerUestdAbrechnung = ZeitdatenDto
										.kopiereArrayUndVerschiebeAnfangsZeitNachSpaeter(
												zeitdatenFuerUestdAbrechnung,
												cUestdAb.getTime().getTime());

								// Ersten und letzten Eintrag des Tages
								// setzen

								long lDifferenz = zeitdatenFuerUestdAbrechnung[zeitdatenEinesTagesDtos.length - 1]
										.getTZeit().getTime()
										- zeitdatenFuerUestdAbrechnung[0]
												.getTZeit().getTime();

								Double dIstUestd = (double) lDifferenz / 3600000;

								// Ueber jede Zeitbuchung iterieren
								double dGesamt = dIstUestd;
								zeitdatenDto_Vorher = null;

								for (int m = 0; m < zeitdatenFuerUestdAbrechnung.length; m++) {
									ZeitdatenDto zeitdatenDto_Aktuell = zeitdatenFuerUestdAbrechnung[m];
									// Milliskunden auf 0 setzen
									Timestamp tsTemp = zeitdatenDto_Aktuell
											.getTZeit();
									Calendar c2 = Calendar.getInstance();
									c2.setTimeInMillis(tsTemp.getTime());
									c2.set(Calendar.MILLISECOND, 0);
									tsTemp = new Timestamp(c2.getTimeInMillis());
									zeitdatenDto_Aktuell.setTZeit(tsTemp);
									// Bei jeder Geraden Zahl mit dem
									// Vorgaenger
									// vergleichen
									if (m % 2 == 0 && m != 0) {
										if (zeitdatenDto_Aktuell
												.getTaetigkeitIId().equals(
														taetigkeitIId_Kommt)
												&& zeitdatenDto_Vorher
														.getTaetigkeitIId()
														.equals(taetigkeitIId_Geht)) {
											// Wenn KOMMT - GEHT, dann nicht
											// dazuzaehlen

											Double dBeginn = Helper
													.time2Double(new Time(
															zeitdatenDto_Vorher
																	.getTZeit()
																	.getTime()));
											Double dEnde = Helper
													.time2Double(new Time(
															zeitdatenDto_Aktuell
																	.getTZeit()
																	.getTime()));
											double dSumme = dEnde.doubleValue()
													- dBeginn.doubleValue();
											dGesamt += dSumme;
										} else if (zeitdatenDto_Aktuell
												.getTaetigkeitIId()
												.equals(zeitdatenDto_Vorher
														.getTaetigkeitIId())) {
											if (Helper
													.short2boolean(kollektivuestdDto
															.getBUnterignorieren()) == false
													|| (Helper
															.short2boolean(kollektivuestdDto
																	.getBUnterignorieren()) == true && !zeitdatenDto_Aktuell
															.getTaetigkeitIId()
															.equals(taetigkeitDto_Unter
																	.getIId()))) {
												Double dBeginn = Helper
														.time2Double(new Time(
																zeitdatenDto_Vorher
																		.getTZeit()
																		.getTime()));
												Double dEnde = Helper
														.time2Double(new Time(
																zeitdatenDto_Aktuell
																		.getTZeit()
																		.getTime()));

												if (dBeginn != null
														&& dEnde != null) {
													double dSumme = dEnde
															.doubleValue()
															- dBeginn
																	.doubleValue();
													// Wenn Taetigkeit ARZT
													// dann
													// bei
													// ARZT hinzufuegen
													if (zeitdatenDto_Aktuell
															.getTaetigkeitIId()
															.equals(taetigkeitDto_Arzt
																	.getIId())) {
														dGesamt += dSumme;
													}
													// Wenn Taetigkeit KRANK
													// dann
													// bei
													// KRANK hinzufuegen
													else if (zeitdatenDto_Aktuell
															.getTaetigkeitIId()
															.equals(taetigkeitDto_Krank
																	.getIId())) {
														dGesamt += dSumme;
													}

													// Ansonsten muss
													// Taetigkeit
													// bei
													// sonstigen bezahlten
													// oder
													// nicht
													// bezahlten
													// Taetigkeiten
													// dabei
													// sein
													else {
														dSumme = Helper
																.rundeKaufmaennisch(
																		new BigDecimal(
																				dSumme),
																		2)
																.doubleValue();
														// Bezahlte
														// Taetigkeiten
														boolean bGefunden = false;
														for (int k = 0; k < sonstigeTaetigkeiten.length; k++) {
															if (zeitdatenDto_Aktuell
																	.getTaetigkeitIId()
																	.equals(sonstigeTaetigkeiten[k]
																			.getIId())
																	&& sonstigeTaetigkeiten[k]
																			.getFBezahlt()
																			.doubleValue() > 0) {

																dSumme = Helper
																		.rundeKaufmaennisch(
																				new BigDecimal(
																						dSumme)
																						.multiply(new BigDecimal(
																								sonstigeTaetigkeiten[k]
																										.getFBezahlt()
																										.doubleValue() / 100)),
																				2)
																		.doubleValue();

																dGesamt += dSumme;
																bGefunden = true;
															}
														}

														if (bGefunden == false) {
															dGesamt -= dSumme;
														}
													}
												}
											}
										}
									}
									zeitdatenDto_Vorher = zeitdatenDto_Aktuell;
								}

								dGesamt -= dSummeTagUESTD200;

								if (dGesamt > 0) {

									if (dBlockzeit > dGesamt) {
										dSummeTagUESTD100Steuerfrei = dGesamt;
										dSummeTagUESTD100 = 0;
									} else {
										dSummeTagUESTD100Steuerfrei = dBlockzeit;
										dSummeTagUESTD100 = dGesamt
												- dBlockzeit;
									}
									dBlockzeit = dBlockzeit - dGesamt;
								}
								dWochensummeUestd100 += dGesamt;
								// --ENDE AB

								// --BIS setzen
								if (kollektivuestdDto.getUBis() != null) {
									cUestdTemp = Calendar.getInstance();
									cUestdTemp
											.setTimeInMillis(kollektivuestdDto
													.getUBis().getTime());

									// Wenn Ja, dann Zeitbuchungen neu
									// setzen
									Calendar cUestdBis = Calendar.getInstance();
									cUestdBis
											.setTimeInMillis(zeitdatenEinesTagesDtos[0]
													.getTZeit().getTime());
									cUestdBis.set(Calendar.HOUR_OF_DAY,
											cUestdTemp
													.get(Calendar.HOUR_OF_DAY));
									cUestdBis.set(Calendar.MINUTE,
											cUestdTemp.get(Calendar.MINUTE));
									cUestdBis.set(Calendar.SECOND, 0);
									cUestdBis.set(Calendar.MILLISECOND, 0);

									zeitdatenFuerUestdAbrechnung = ZeitdatenDto
											.kopiereArrayUndVerschiebeEndZeitNachFrueher(
													zeitdatenEinesTagesDtos,
													cUestdBis.getTime()
															.getTime());

									// Ersten und letzten Eintrag des Tages
									// setzen
									lDifferenz = zeitdatenFuerUestdAbrechnung[zeitdatenEinesTagesDtos.length - 1]
											.getTZeit().getTime()
											- zeitdatenFuerUestdAbrechnung[0]
													.getTZeit().getTime();

									dIstUestd = (double) lDifferenz / 3600000;
									// Ueber jede Zeitbuchung iterieren
									dGesamt = dIstUestd;
									zeitdatenDto_Vorher = null;

									for (int m = 0; m < zeitdatenFuerUestdAbrechnung.length; m++) {
										ZeitdatenDto zeitdatenDto_Aktuell = zeitdatenFuerUestdAbrechnung[m];
										// Milliskunden auf 0 setzen
										Timestamp tsTemp = zeitdatenDto_Aktuell
												.getTZeit();
										Calendar c2 = Calendar.getInstance();
										c2.setTimeInMillis(tsTemp.getTime());
										c2.set(Calendar.MILLISECOND, 0);
										tsTemp = new Timestamp(
												c2.getTimeInMillis());
										zeitdatenDto_Aktuell.setTZeit(tsTemp);
										// Bei jeder Geraden Zahl mit dem
										// Vorgaenger
										// vergleichen
										if (m % 2 == 0 && m != 0) {
											if (zeitdatenDto_Aktuell
													.getTaetigkeitIId()
													.equals(taetigkeitIId_Kommt)
													&& zeitdatenDto_Vorher
															.getTaetigkeitIId()
															.equals(taetigkeitIId_Geht)) {
												// Wenn KOMMT - GEHT, dann
												// nicht
												// dazuzaehlen

												Double dBeginn = Helper
														.time2Double(new Time(
																zeitdatenDto_Vorher
																		.getTZeit()
																		.getTime()));
												Double dEnde = Helper
														.time2Double(new Time(
																zeitdatenDto_Aktuell
																		.getTZeit()
																		.getTime()));
												double dSumme = dEnde
														.doubleValue()
														- dBeginn.doubleValue();
												dGesamt += dSumme;
											} else if (zeitdatenDto_Aktuell
													.getTaetigkeitIId()
													.equals(zeitdatenDto_Vorher
															.getTaetigkeitIId())) {

												Double dBeginn = Helper
														.time2Double(new Time(
																zeitdatenDto_Vorher
																		.getTZeit()
																		.getTime()));
												Double dEnde = Helper
														.time2Double(new Time(
																zeitdatenDto_Aktuell
																		.getTZeit()
																		.getTime()));

												if (dBeginn != null
														&& dEnde != null) {
													double dSumme = dEnde
															.doubleValue()
															- dBeginn
																	.doubleValue();
													// Wenn Taetigkeit ARZT
													// dann
													// bei
													// ARZT hinzufuegen
													if (zeitdatenDto_Aktuell
															.getTaetigkeitIId()
															.equals(taetigkeitDto_Arzt
																	.getIId())) {
														dGesamt += dSumme;
													}
													// Wenn Taetigkeit KRANK
													// dann
													// bei KRANK hinzufuegen
													else if (zeitdatenDto_Aktuell
															.getTaetigkeitIId()
															.equals(taetigkeitDto_Krank
																	.getIId())) {
														dGesamt += dSumme;
													}

													// Ansonsten muss
													// Taetigkeit
													// bei
													// sonstigen bezahlten
													// oder
													// nicht bezahlten
													// Taetigkeiten
													// dabei sein
													else {
														dSumme = Helper
																.rundeKaufmaennisch(
																		new BigDecimal(
																				dSumme),
																		2)
																.doubleValue();
														// Bezahlte
														// Taetigkeiten
														boolean bGefunden = false;
														for (int k = 0; k < sonstigeTaetigkeiten.length; k++) {
															if (zeitdatenDto_Aktuell
																	.getTaetigkeitIId()
																	.equals(sonstigeTaetigkeiten[k]
																			.getIId())
																	&& sonstigeTaetigkeiten[k]
																			.getFBezahlt()
																			.doubleValue() > 0) {

																dSumme = Helper
																		.rundeKaufmaennisch(
																				new BigDecimal(
																						dSumme)
																						.multiply(new BigDecimal(
																								sonstigeTaetigkeiten[k]
																										.getFBezahlt()
																										.doubleValue() / 100)),
																				2)
																		.doubleValue();

																dGesamt += dSumme;
																bGefunden = true;
															}
														}

														if (bGefunden == false) {
															dGesamt -= dSumme;
														}

													}
												}
											}
										}
										zeitdatenDto_Vorher = zeitdatenDto_Aktuell;
									}

									dGesamt -= dSummeTagUESTD200;

									if (dGesamt > 0) {
										if (dBlockzeit < 0) {
											dBlockzeit = 0;
										}

										if (dBlockzeit > dGesamt) {
											dSummeTagUESTD100Steuerfrei += dGesamt;
										} else {
											dSummeTagUESTD100Steuerfrei += dBlockzeit;
											dSummeTagUESTD100 += dGesamt
													- dBlockzeit;
										}
										dBlockzeit = dBlockzeit - dGesamt;
									}
									dWochensummeUestd100 += dGesamt;
								}
								// --ENDE BIS

							}

							// 50% tageweise
							// Gibt es fuer diese Tagesart einen Eintrag in
							// den
							// Kollektivuestd 50 ?
							if (hmKollektivUestd50Tageweise
									.containsKey(tagesartIId)) {

								ArrayList alVonBis = (ArrayList) hmKollektivUestd50Tageweise
										.get(tagesartIId);

								for (int alz = 0; alz < alVonBis.size(); alz++) {
									Object[] oZeile = (Object[]) alVonBis
											.get(alz);
									boolean bPausenIgnorieren = Helper
											.short2boolean((Short) oZeile[2]);
									Time ab = (Time) oZeile[0];
									Time bis = (Time) oZeile[1];

									// --AB setzen

									Calendar cUestdTemp = Calendar
											.getInstance();
									cUestdTemp.setTimeInMillis(ab.getTime());

									// Wenn Ja, dann Zeitbuchungen neu
									// setzen
									Calendar cUestdAb = Calendar.getInstance();
									cUestdAb.setTimeInMillis(zeitdatenEinesTagesDtos[0]
											.getTZeit().getTime());
									cUestdAb.set(Calendar.HOUR_OF_DAY,
											cUestdTemp
													.get(Calendar.HOUR_OF_DAY));
									cUestdAb.set(Calendar.MINUTE,
											cUestdTemp.get(Calendar.MINUTE));
									cUestdAb.set(Calendar.SECOND, 0);
									cUestdAb.set(Calendar.MILLISECOND, 0);

									ZeitdatenDto[] zeitdatenFuerUestdAbrechnung = ZeitdatenDto
											.kopiereArray(zeitdatenEinesTagesDtos);

									for (int o = 0; o < zeitdatenFuerUestdAbrechnung.length; o++) {
										ZeitdatenDto zeitdatenDto_Aktuell = zeitdatenFuerUestdAbrechnung[o];
										if (zeitdatenDto_Aktuell.getTZeit()
												.before(new Timestamp(cUestdAb
														.getTime().getTime()))) {
											zeitdatenDto_Aktuell
													.setTZeit(new Timestamp(
															cUestdAb.getTime()
																	.getTime()));
											zeitdatenFuerUestdAbrechnung[o] = zeitdatenDto_Aktuell;
										}
									}
									// --BIS setzen
									if (bis != null) {
										cUestdTemp.setTimeInMillis(bis
												.getTime());

										// Wenn Ja, dann Zeitbuchungen neu
										// setzen
										Calendar cUestdBis = Calendar
												.getInstance();
										cUestdBis
												.setTimeInMillis(zeitdatenFuerUestdAbrechnung[0]
														.getTZeit().getTime());
										cUestdBis
												.set(Calendar.HOUR_OF_DAY,
														cUestdTemp
																.get(Calendar.HOUR_OF_DAY));
										cUestdBis
												.set(Calendar.MINUTE,
														cUestdTemp
																.get(Calendar.MINUTE));

										cUestdBis.set(Calendar.SECOND, 0);
										cUestdBis.set(Calendar.MILLISECOND, 0);

										for (int o = 0; o < zeitdatenFuerUestdAbrechnung.length; o++) {
											ZeitdatenDto zeitdatenDto_Aktuell = zeitdatenFuerUestdAbrechnung[o];
											if (zeitdatenDto_Aktuell
													.getTZeit()
													.after(new Timestamp(
															cUestdBis.getTime()
																	.getTime()))) {
												zeitdatenDto_Aktuell
														.setTZeit(new Timestamp(
																cUestdBis
																		.getTime()
																		.getTime()));
												zeitdatenFuerUestdAbrechnung[o] = zeitdatenDto_Aktuell;
											}
										}
									}
									// Ersten und letzten Eintrag des Tages
									// setzen
									java.sql.Time t1Uesdt = new java.sql.Time(
											zeitdatenFuerUestdAbrechnung[0]
													.getTZeit().getTime());
									java.sql.Time t2Uestd = new java.sql.Time(
											zeitdatenFuerUestdAbrechnung[zeitdatenEinesTagesDtos.length - 1]
													.getTZeit().getTime());

									Double dIstUestd = Helper.time2Double(
											t2Uestd).doubleValue()
											- Helper.time2Double(t1Uesdt)
													.doubleValue();

									// Ueber jede Zeitbuchung iterieren
									double dGesamt = dIstUestd;
									zeitdatenDto_Vorher = null;

									for (int m = 0; m < zeitdatenFuerUestdAbrechnung.length; m++) {
										ZeitdatenDto zeitdatenDto_Aktuell = zeitdatenFuerUestdAbrechnung[m];
										// Milliskunden auf 0 setzen
										Timestamp tsTemp = zeitdatenDto_Aktuell
												.getTZeit();
										Calendar c2 = Calendar.getInstance();
										c2.setTimeInMillis(tsTemp.getTime());
										c2.set(c2.MILLISECOND, 0);
										tsTemp = new Timestamp(
												c2.getTimeInMillis());
										zeitdatenDto_Aktuell.setTZeit(tsTemp);
										// Bei jeder Geraden Zahl mit dem
										// Vorgaenger
										// vergleichen
										if (m % 2 == 0 && m != 0) {
											if (zeitdatenDto_Aktuell
													.getTaetigkeitIId()
													.equals(taetigkeitIId_Kommt)
													&& zeitdatenDto_Vorher
															.getTaetigkeitIId()
															.equals(taetigkeitIId_Geht)) {
												// Wenn KOMMT - GEHT, dann
												// nicht
												// dazuzaehlen

												Double dBeginn = Helper
														.time2Double(new Time(
																zeitdatenDto_Vorher
																		.getTZeit()
																		.getTime()));
												Double dEnde = Helper
														.time2Double(new Time(
																zeitdatenDto_Aktuell
																		.getTZeit()
																		.getTime()));
												double dSumme = dEnde
														.doubleValue()
														- dBeginn.doubleValue();
												dGesamt += dSumme;
											} else if (zeitdatenDto_Aktuell
													.getTaetigkeitIId()
													.equals(zeitdatenDto_Vorher
															.getTaetigkeitIId())) {

												if (bPausenIgnorieren == false
														|| (bPausenIgnorieren == true && !zeitdatenDto_Aktuell
																.getTaetigkeitIId()
																.equals(taetigkeitDto_Unter
																		.getIId()))) {

													Double dBeginn = Helper
															.time2Double(new Time(
																	zeitdatenDto_Vorher
																			.getTZeit()
																			.getTime()));
													Double dEnde = Helper
															.time2Double(new Time(
																	zeitdatenDto_Aktuell
																			.getTZeit()
																			.getTime()));

													if (dBeginn != null
															&& dEnde != null) {
														boolean bBezahlt = false;
														double dSumme = dEnde
																.doubleValue()
																- dBeginn
																		.doubleValue();
														// Wenn Taetigkeit
														// ARZT
														// dann
														// bei
														// ARZT hinzufuegen
														if (zeitdatenDto_Aktuell
																.getTaetigkeitIId()
																.equals(taetigkeitDto_Arzt
																		.getIId())) {
															bBezahlt = true;
														}
														// Wenn Taetigkeit
														// KRANK
														// dann
														// bei KRANK
														// hinzufuegen
														else if (zeitdatenDto_Aktuell
																.getTaetigkeitIId()
																.equals(taetigkeitDto_Krank
																		.getIId())) {
															bBezahlt = true;
														}

														// Ansonsten muss
														// Taetigkeit
														// bei
														// sonstigen
														// bezahlten
														// oder
														// nicht bezahlten
														// Taetigkeiten
														// dabei sein
														else {
															dSumme = Helper
																	.rundeKaufmaennisch(
																			new BigDecimal(
																					dSumme),
																			2)
																	.doubleValue();
															// Bezahlte
															// Taetigkeiten
															for (int k = 0; k < sonstigeTaetigkeiten.length; k++) {
																if (zeitdatenDto_Aktuell
																		.getTaetigkeitIId()
																		.equals(sonstigeTaetigkeiten[k]
																				.getIId())
																		&& sonstigeTaetigkeiten[k]
																				.getFBezahlt() > 0) {
																	bBezahlt = true;
																}
															}

														}

														if (!bBezahlt) {
															dGesamt -= dSumme;
														}
													}
												}
											}
										}
										zeitdatenDto_Vorher = zeitdatenDto_Aktuell;
									}

									dGesamt -= dSummeTagUESTD200;

									if (dGesamt > 0) {
										if (dBlockzeit < 0) {
											dBlockzeit = 0;
										}

										if (dBlockzeit > dGesamt) {
											dSummeTagUESTD50Steuerfrei += dGesamt;
											dSummeTagUESTD50 += 0;
										} else {
											dSummeTagUESTD50Steuerfrei += dBlockzeit;
											dSummeTagUESTD50 += dGesamt
													- dBlockzeit;
										}
										dBlockzeit = dBlockzeit - dGesamt;
									}
									dWochensummeUestd50Tageweise += dGesamt;
									// --ENDE 50%Tageweise
									// ------------------------------------------

								}
							}
						}

					}

					zeile.setBdUestd200(new BigDecimal(dSummeTagUESTD200));

					zeile.setBdUestd100(new BigDecimal(dSummeTagUESTD100));
					zeile.setBdUestd100Steuerfrei(new BigDecimal(
							dSummeTagUESTD100Steuerfrei));

					zeile.setBdUestd50Tageweise(new BigDecimal(dSummeTagUESTD50));
					zeile.setBdUestd50TageweiseSteuerfrei(new BigDecimal(
							dSummeTagUESTD50Steuerfrei));
					zeile.setBdUestd50(new BigDecimal(0));

					zeile.setBdMehrstunden(new BigDecimal(0));
					// -- ENDE Ueberstundenberechnung

				} else {
					zeile.setBdArzt(new BigDecimal(0));
					zeile.setBdBehoerde(new BigDecimal(0));
					zeile.setBdDiff(new BigDecimal(0));
					zeile.setBdFeiertag(new BigDecimal(0));
					zeile.setBdIst(new BigDecimal(0));
					zeile.setBdKrank(new BigDecimal(0));
					zeile.setBdKindkrank(new BigDecimal(0));
					zeile.setBdSoll(new BigDecimal(0));
					zeile.setBdSonstigeBezahlt(new BigDecimal(0));
					zeile.setBdSonstigeNichtBezahlt(new BigDecimal(0));
					zeile.setBdUnter(new BigDecimal(0));
					zeile.setBdUrlaubStunden(new BigDecimal(0));
					zeile.setBdUrlaubTage(new BigDecimal(0));
					zeile.setBdZA(new BigDecimal(0));
					zeile.setBdUestd100(new BigDecimal(0));
					zeile.setBdUestd50(new BigDecimal(0));
					zeile.setBdUestd100Steuerfrei(new BigDecimal(0));
					zeile.setBdUestd50Tageweise(new BigDecimal(0));
					zeile.setBdUestd50TageweiseSteuerfrei(new BigDecimal(0));
					zeile.setBdReise(new BigDecimal(0));
					zeile.setBdUestd200(new BigDecimal(0));
					zeile.setBdMehrstunden(new BigDecimal(0));
				}

				monatsabrechnungZeilen.add(zeile);

				dWochensummeIst += zeile.getBdIst().doubleValue();

				// PJ18628
				dWochensummeIstOhneSonderzeiten += zeile.getBdIst()
						.doubleValue();
				// Projekt 13205: BezahlteSonmdertaetigkeiten gehoeren dazu
				dWochensummeIst += zeile.getBdSonstigeBezahlt().doubleValue();
				if (zeile.getBdFeiertag() != null) {
					dWochensummeIst += zeile.getBdFeiertag().doubleValue();
				}
				dWochensummeIst += zeile.getBdKrank().doubleValue();
				dWochensummeIst += zeile.getBdBehoerde().doubleValue();
				dWochensummeIst += zeile.getBdUrlaubStunden().doubleValue();
				dWochensummeIst += zeile.getBdArzt().doubleValue();

				if (zeile.getBdSoll() == null) {
					zeile.setBdSoll(new BigDecimal(0));
				}
				if (zeile.getBdFeiertag() == null) {
					zeile.setBdFeiertag(new BigDecimal(0));
				}

				dWochensummeFtgSoll += zeile.getBdFeiertag().doubleValue();

				dWochensummeSollFuerUestd += zeile.getBdSoll()
						.add(zeile.getBdFeiertag()).doubleValue();

				// Wenn naechster Tag in anderer Woche, dann Wochesumme
				// ausgeben
				// und bei 0 anfangen

				Calendar cKwNachsterTag = Calendar.getInstance();
				cKwNachsterTag.set(iJahr.intValue(), iMonat.intValue(), i + 1);

				int kwNachsterTag = cKwNachsterTag.get(Calendar.WEEK_OF_YEAR);

				if (zeile.getIKw() != kwNachsterTag || i == lAnzahlTageImMonat) {
					// Hier erfolget die eigentliche 50%
					// Ueberstundenberechnung

					zeile.setBdUestd50(new BigDecimal(0));
					zeile.setBdMehrstunden(new BigDecimal(0));

					System.out.println("WochensollFuerUestd fuer KW "
							+ zeile.getIKw() + " " + dWochensummeSollFuerUestd);

					if (nNormalstunden != null
							&& nNormalstunden.doubleValue() != 0) {
						BigDecimal nFaktorUestd50 = nSollStundenFuerUestd50
								.divide(nNormalstunden, 4,
										BigDecimal.ROUND_HALF_EVEN);

						BigDecimal nNormalstundenWoche = null;

						if (nFaktorUestd50.doubleValue() != 0) {
							nNormalstundenWoche = new BigDecimal(
									dWochensummeSollFuerUestd).divide(
									nFaktorUestd50, 4,
									BigDecimal.ROUND_HALF_EVEN);
						} else {
							nNormalstundenWoche = new BigDecimal(0);
						}

						BigDecimal nMehrstunden = nNormalstundenWoche
								.subtract(new BigDecimal(
										dWochensummeSollFuerUestd));

						if (nMehrstunden.doubleValue() < 0) {
							nMehrstunden = new BigDecimal(0);
						}

						double dWochenDiff = dWochensummeIst
								- dWochensummeSollFuerUestd;

						if (bUestdVerteilen == false) {

							// Berechnungsgrundlage fuer 50%ige Ueberstunden
							BigDecimal bdBerechnungsgrundlageFuer50Prozentige = new BigDecimal(
									dWochensummeIst)
									.subtract(
											Helper.rundeKaufmaennisch(
													new BigDecimal(
															dWochensummeUestd100),
													2))
									.subtract(
											Helper.rundeKaufmaennisch(
													new BigDecimal(
															dWochensummeUestd200),
													2));

							bdBerechnungsgrundlageFuer50Prozentige = bdBerechnungsgrundlageFuer50Prozentige
									.subtract(Helper
											.rundeKaufmaennisch(
													new BigDecimal(
															dWochensummeUestd50Tageweise),
													2));

							bdBerechnungsgrundlageFuer50Prozentige = bdBerechnungsgrundlageFuer50Prozentige
									.subtract(Helper.rundeKaufmaennisch(
											new BigDecimal(
													dWochensummeSollFuerUestd),
											2));
							bdBerechnungsgrundlageFuer50Prozentige = Helper
									.rundeKaufmaennisch(
											bdBerechnungsgrundlageFuer50Prozentige,
											2);

							if (bdBerechnungsgrundlageFuer50Prozentige
									.doubleValue() > nMehrstunden.doubleValue()) {
								zeile.setBdMehrstunden(Helper
										.rundeKaufmaennisch(nMehrstunden, 2));
								bdBerechnungsgrundlageFuer50Prozentige = bdBerechnungsgrundlageFuer50Prozentige
										.subtract(nMehrstunden);
								zeile.setBdUestd50(Helper.rundeKaufmaennisch(
										bdBerechnungsgrundlageFuer50Prozentige,
										2));
							} else if (bdBerechnungsgrundlageFuer50Prozentige
									.doubleValue() < 0) {
								zeile.setBdUestd50(new BigDecimal(0));
								zeile.setBdMehrstunden(new BigDecimal(0));
							} else {
								zeile.setBdMehrstunden(Helper
										.rundeKaufmaennisch(
												bdBerechnungsgrundlageFuer50Prozentige,
												2));
							}
						} else {

							double zuVerteilendeUestd = dWochenDiff;

							if (dWochenDiff > 0) {
								if (dWochenDiff > nMehrstunden.doubleValue()) {
									zeile.setBdMehrstunden(nMehrstunden);
									zuVerteilendeUestd = dWochenDiff
											- nMehrstunden.doubleValue();
								} else {
									zeile.setBdMehrstunden(new BigDecimal(
											dWochenDiff));
									zuVerteilendeUestd = 0;
								}
							}
							for (int f = 0; f < 4; f++) {
								for (int u = 0; u < monatsabrechnungZeilen
										.size(); u++) {
									ZeileMonatsabrechnungDto dto = (ZeileMonatsabrechnungDto) monatsabrechnungZeilen
											.get(u);
									if (dto.getIKw().intValue() == zeile
											.getIKw().intValue()) {

										// Wenn B_UESTDVERTEILEN und diff
										// <0,
										// dann gibt es keine Ueberstunden
										// Mehrstunden bzw. ueberstunden
										if (dWochenDiff <= 0) {
											dto.setBdUestd50(new BigDecimal(0));
											dto.setBdUestd100(new BigDecimal(0));
											dto.setBdUestd100Steuerfrei(new BigDecimal(
													0));
											dto.setBdUestd50Tageweise(new BigDecimal(
													0));
											dto.setBdUestd50TageweiseSteuerfrei(new BigDecimal(
													0));
											dto.setBdMehrstunden(new BigDecimal(
													0));
										} else {
											// nun verteilen
											if (zuVerteilendeUestd > 0) {

												if (f == 0) {

													if (dto.getBdUestd50Tageweise() != null
															&& dto.getBdUestd50Tageweise()
																	.doubleValue() > 0) {

														if (dto.getBdUestd50Tageweise()
																.doubleValue() > zuVerteilendeUestd) {
															dto.setBdUestd50Tageweise(new BigDecimal(
																	zuVerteilendeUestd));
															zuVerteilendeUestd = 0;
														} else {
															zuVerteilendeUestd -= dto
																	.getBdUestd50Tageweise()
																	.doubleValue();
														}

													}

												}
												if (f == 1) {
													if (dto.getBdUestd100() != null
															&& dto.getBdUestd100()
																	.doubleValue() > 0) {

														if (dto.getBdUestd100()
																.doubleValue() > zuVerteilendeUestd) {
															dto.setBdUestd100(new BigDecimal(
																	zuVerteilendeUestd));
															zuVerteilendeUestd = 0;
														} else {
															zuVerteilendeUestd -= dto
																	.getBdUestd100()
																	.doubleValue();
														}

													}

												}
												if (f == 2) {
													if (dto.getBdUestd50TageweiseSteuerfrei() != null
															&& dto.getBdUestd50TageweiseSteuerfrei()
																	.doubleValue() > 0) {

														if (dto.getBdUestd50TageweiseSteuerfrei()
																.doubleValue() > zuVerteilendeUestd) {
															dto.setBdUestd50TageweiseSteuerfrei(new BigDecimal(
																	zuVerteilendeUestd));
															zuVerteilendeUestd = 0;
														} else {
															zuVerteilendeUestd -= dto
																	.getBdUestd50TageweiseSteuerfrei()
																	.doubleValue();
														}

													}
												}
												if (f == 3) {
													if (dto.getBdUestd100Steuerfrei() != null
															&& dto.getBdUestd100Steuerfrei()
																	.doubleValue() > 0) {

														if (dto.getBdUestd100Steuerfrei()
																.doubleValue() > zuVerteilendeUestd) {
															dto.setBdUestd100Steuerfrei(new BigDecimal(
																	zuVerteilendeUestd));
															zuVerteilendeUestd = 0;
														} else {
															zuVerteilendeUestd -= dto
																	.getBdUestd100Steuerfrei()
																	.doubleValue();
														}

													}

												}

												// Wenn noch verbeibende,
												// die
												// sind dann Wochenweise
												// 50%ig

												if (f == 3
														&& zuVerteilendeUestd > 0) {
													zeile.setBdUestd50(new BigDecimal(
															zuVerteilendeUestd));
												}

											}

										}
										monatsabrechnungZeilen.set(u, dto);
									}
								}
							}

						}
					}

					dWochensummeIst = 0;
					dWochensummeIstOhneSonderzeiten = 0;
					dWochensummeSollFuerUestd = 0;
					dWochensummeUestd50Tageweise = 0;
					dWochensummeUestd100 = 0;
					dWochensummeFtgSoll = 0;
				}
			}

		}

		// Faktor
		BigDecimal faktor50 = new BigDecimal(0.00);
		BigDecimal faktor100 = new BigDecimal(0.00);
		BigDecimal faktormehrstd = new BigDecimal(0.00);
		BigDecimal faktor200 = new BigDecimal(0.00);
		if (personalDto.getKollektivDto() != null) {
			faktor50 = personalDto.getKollektivDto().getNFaktoruestd50();
			faktor100 = personalDto.getKollektivDto().getNFaktoruestd100();
			faktormehrstd = personalDto.getKollektivDto().getNFaktormehrstd();
			faktor200 = personalDto.getKollektivDto().getNFaktoruestd200();
		}

		// Holen des Gleitzeitsaldos des Vormonates
		GleitzeitsaldoDto gleitzeitsaldoDto_Vormonat = getGleitzeitsaldoVormonat(
				personalIId, iJahr, iMonat, d_datum_bis, dEintrittsdatum);

		// Summe der Ausbezahlten Stunden zusammenzaehlen
		BigDecimal bdSummeStundenabrechnungUestdFrei50 = new BigDecimal(0);
		BigDecimal bdSummeStundenabrechnungUestdFrei100 = new BigDecimal(0);
		BigDecimal bdSummeStundenabrechnungUestdPflichtig50 = new BigDecimal(0);
		BigDecimal bdSummeStundenabrechnungUestdPflichtig100 = new BigDecimal(0);
		BigDecimal bdSummeStundenabrechnungUestd200 = new BigDecimal(0);
		BigDecimal bdSummeStundenabrechnungUestdMehrstunden = new BigDecimal(0);
		BigDecimal bdSummeStundenabrechnungUestdNormalstunden = new BigDecimal(
				0);
		BigDecimal bdSummeStundenabrechnungGutstunden = new BigDecimal(0);
		BigDecimal bdQualifikationspraemie = new BigDecimal(0);

		try {
			StundenabrechnungDto[] stundeabrechnungDtos = getPersonalFac()
					.stundenabrechnungFindByPersonalIIdIJahrIMonat(personalIId,
							iJahr, iMonat);

			for (int i = 0; i < stundeabrechnungDtos.length; i++) {

				Calendar c = Calendar.getInstance();
				c.setTime(stundeabrechnungDtos[i].getTDatum());

				// PJ18647
				for (int k = 0; k < monatsabrechnungZeilen.size(); k++) {
					ZeileMonatsabrechnungDto z = (ZeileMonatsabrechnungDto) monatsabrechnungZeilen
							.get(k);

					if (z.getITag() == c.get(Calendar.DATE)) {
						z.setBdQualifikationsfaktor(stundeabrechnungDtos[i]
								.getNQualifikationsfaktor());
					}

				}

				// Fuer Uebertrag in Monatsabrechnun

				bdSummeStundenabrechnungUestdNormalstunden = bdSummeStundenabrechnungUestdNormalstunden
						.subtract(stundeabrechnungDtos[i].getNGutstunden());

				bdSummeStundenabrechnungUestdNormalstunden = bdSummeStundenabrechnungUestdNormalstunden
						.add(stundeabrechnungDtos[i].getNNormalstunden());

				if (stundeabrechnungDtos[i].getNQualifikationspraemie() != null) {
					bdQualifikationspraemie = bdQualifikationspraemie
							.add(stundeabrechnungDtos[i]
									.getNQualifikationspraemie());
				}

				// WH: 08-12-09: Die Ueberstunden muessen bei
				// den Ueberstunden und beim Gleitzeitsaldo abgezogen werden.
				bdSummeStundenabrechnungUestdNormalstunden = bdSummeStundenabrechnungUestdNormalstunden
						.add(stundeabrechnungDtos[i].getNMehrstunden())
						.add(stundeabrechnungDtos[i].getNUest200())
						.add(stundeabrechnungDtos[i].getNUestfrei100())
						.add(stundeabrechnungDtos[i].getNUestfrei50())
						.add(stundeabrechnungDtos[i].getNUestpflichtig100())
						.add(stundeabrechnungDtos[i].getNUestpflichtig50());

				// Fuer Uebertrag in Gleitzeitsaldo
				bdSummeStundenabrechnungUestdMehrstunden = bdSummeStundenabrechnungUestdMehrstunden
						.add(stundeabrechnungDtos[i].getNMehrstunden());
				bdSummeStundenabrechnungUestdFrei100 = bdSummeStundenabrechnungUestdFrei100
						.add(stundeabrechnungDtos[i].getNUestfrei100());
				bdSummeStundenabrechnungUestdFrei50 = bdSummeStundenabrechnungUestdFrei50
						.add(stundeabrechnungDtos[i].getNUestfrei50());
				bdSummeStundenabrechnungGutstunden = bdSummeStundenabrechnungGutstunden
						.add(stundeabrechnungDtos[i].getNGutstunden());
				bdSummeStundenabrechnungUestdPflichtig100 = bdSummeStundenabrechnungUestdPflichtig100
						.add(stundeabrechnungDtos[i].getNUestpflichtig100());
				bdSummeStundenabrechnungUestdPflichtig50 = bdSummeStundenabrechnungUestdPflichtig50
						.add(stundeabrechnungDtos[i].getNUestpflichtig50());
				bdSummeStundenabrechnungUestd200 = bdSummeStundenabrechnungUestd200
						.add(stundeabrechnungDtos[i].getNUest200());

			}

		} catch (RemoteException ex10) {
			throwEJBExceptionLPRespectOld(ex10);
		}

		// In Report uebergeben
		HashMap parameter = new HashMap();

		parameter.put("P_VON_BIS_ERFASSUNG", new Boolean(bVonBis));
		parameter
				.put("P_VON_BIS_ERFASSUNG_KOMMT_GEHT", new Boolean(bKommtGeht));

		parameter.put("P_AUSBEZAHLTMEHRSTD", Helper.rundeKaufmaennisch(
				bdSummeStundenabrechnungUestdMehrstunden, 2));

		parameter.put("P_EINTRITTSDATUM", dEintrittsdatum);

		parameter.put("P_AUSBEZAHLTUESTD50", Helper.rundeKaufmaennisch(
				bdSummeStundenabrechnungUestdPflichtig50, 2));
		parameter.put("P_AUSBEZAHLTUESTD50STF", Helper.rundeKaufmaennisch(
				bdSummeStundenabrechnungUestdFrei50, 2));
		parameter.put("P_AUSBEZAHLTUESTD100", Helper.rundeKaufmaennisch(
				bdSummeStundenabrechnungUestdPflichtig100, 2));
		parameter.put("P_AUSBEZAHLTUESTD100STF", Helper.rundeKaufmaennisch(
				bdSummeStundenabrechnungUestdFrei100, 2));
		parameter.put("P_AUSBEZAHLTNORMALSTD", Helper.rundeKaufmaennisch(
				bdSummeStundenabrechnungUestdNormalstunden, 2));
		parameter.put("P_AUSBEZAHLTUESTD200",
				Helper.rundeKaufmaennisch(bdSummeStundenabrechnungUestd200, 2));
		parameter.put("P_AUSBEZAHLTGUTSTD", Helper.rundeKaufmaennisch(
				bdSummeStundenabrechnungGutstunden, 2));
		parameter.put("P_QUALIPRAEMIE",
				Helper.rundeKaufmaennisch(bdQualifikationspraemie, 2));

		// Abgerechnete Ueberstunden aus Gleitzeitsaldo-Vormonat abziehen
		gleitzeitsaldoDto_Vormonat.setNSaldouest200(gleitzeitsaldoDto_Vormonat
				.getNSaldouest200().subtract(bdSummeStundenabrechnungUestd200));
		gleitzeitsaldoDto_Vormonat
				.setNSaldouestpflichtig100(gleitzeitsaldoDto_Vormonat
						.getNSaldouestpflichtig100().subtract(
								bdSummeStundenabrechnungUestdPflichtig100));
		gleitzeitsaldoDto_Vormonat
				.setNSaldouestfrei100(gleitzeitsaldoDto_Vormonat
						.getNSaldouestfrei100().subtract(
								bdSummeStundenabrechnungUestdFrei100));
		gleitzeitsaldoDto_Vormonat
				.setNSaldouestpflichtig50(gleitzeitsaldoDto_Vormonat
						.getNSaldouestpflichtig50().subtract(
								bdSummeStundenabrechnungUestdPflichtig50));
		gleitzeitsaldoDto_Vormonat
				.setNSaldouestfrei50(gleitzeitsaldoDto_Vormonat
						.getNSaldouestfrei50().subtract(
								bdSummeStundenabrechnungUestdFrei50));
		gleitzeitsaldoDto_Vormonat
				.setNSaldomehrstunden(gleitzeitsaldoDto_Vormonat
						.getNSaldomehrstunden().subtract(
								bdSummeStundenabrechnungUestdMehrstunden));

		// Berechnung der Gesamtsummen fuer Gleitzeitsaldo

		BigDecimal bdSummeUrlaub = new BigDecimal(0);
		BigDecimal bdSummeArzt = new BigDecimal(0);
		BigDecimal bdSummeBehoerde = new BigDecimal(0);
		BigDecimal bdSummeFeiertag = new BigDecimal(0);
		BigDecimal bdSummeKrank = new BigDecimal(0);
		BigDecimal bdSummeKindkrank = new BigDecimal(0);
		BigDecimal bdSummeSonstigeBezahlt = new BigDecimal(0);
		BigDecimal bdSummeUEST50FREI = new BigDecimal(0);
		BigDecimal bdSummeUEST50PFLICHTIG = new BigDecimal(0);
		BigDecimal bdSummeUEST100FREI = new BigDecimal(0);
		BigDecimal bdSummeUEST100PFLICHTIG = new BigDecimal(0);
		BigDecimal bdSummeMEHRSTUNDEN = new BigDecimal(0);
		BigDecimal bdSummeUEST200 = new BigDecimal(0);

		BigDecimal bdSummeSoll = new BigDecimal(0);
		BigDecimal bdSummeIst = new BigDecimal(0);

		for (int i = 0; i < monatsabrechnungZeilen.size(); i++) {
			ZeileMonatsabrechnungDto z = (ZeileMonatsabrechnungDto) monatsabrechnungZeilen
					.get(i);
			if (z.getBdUrlaubStunden() != null) {
				bdSummeUrlaub = bdSummeUrlaub.add(z.getBdUrlaubStunden());
			}
			if (z.getBdArzt() != null) {
				bdSummeArzt = bdSummeArzt.add(z.getBdArzt());
			}
			if (z.getBdBehoerde() != null) {
				bdSummeBehoerde = bdSummeBehoerde.add(z.getBdBehoerde());
			}
			if (z.getBdFeiertag() != null) {
				bdSummeFeiertag = bdSummeFeiertag.add(z.getBdFeiertag());
			}
			if (z.getBdSonstigeBezahlt() != null) {
				bdSummeSonstigeBezahlt = bdSummeSonstigeBezahlt.add(z
						.getBdSonstigeBezahlt());
			}
			if (z.getBdKrank() != null) {
				bdSummeKrank = bdSummeKrank.add(z.getBdKrank());
			}
			if (z.getBdKindkrank() != null) {
				bdSummeKindkrank = bdSummeKindkrank.add(z.getBdKindkrank());
			}
			if (z.getBdSoll() != null) {
				bdSummeSoll = bdSummeSoll.add(z.getBdSoll());
			}
			if (z.getBdIst() != null) {
				bdSummeIst = bdSummeIst.add(z.getBdIst());
			}
			if (z.getBdUestd200() != null) {
				bdSummeUEST200 = bdSummeUEST200.add(z.getBdUestd200());
			}
			if (z.getBdUestd100() != null) {
				bdSummeUEST100PFLICHTIG = bdSummeUEST100PFLICHTIG.add(z
						.getBdUestd100());
			}
			if (z.getBdUestd100Steuerfrei() != null) {
				bdSummeUEST100FREI = bdSummeUEST100FREI.add(z
						.getBdUestd100Steuerfrei());
			}
			if (z.getBdUestd50() != null) {
				bdSummeUEST50PFLICHTIG = bdSummeUEST50PFLICHTIG.add(z
						.getBdUestd50());
			}
			if (z.getBdUestd50Tageweise() != null) {
				bdSummeUEST50PFLICHTIG = bdSummeUEST50PFLICHTIG.add(z
						.getBdUestd50Tageweise());
			}
			if (z.getBdUestd50TageweiseSteuerfrei() != null) {
				bdSummeUEST50FREI = bdSummeUEST50FREI.add(z
						.getBdUestd50TageweiseSteuerfrei());
			}
			if (z.getBdMehrstunden() != null) {
				bdSummeMEHRSTUNDEN = bdSummeMEHRSTUNDEN.add(z
						.getBdMehrstunden());
			}
		}

		// PJ 16194
		if (nSollStundenFIX != null) {
			bdSummeSoll = nSollStundenFIX;
			// 2865
			if (bSollstundenMitrechnen == false) {
				dSummeMonatFeiertagSoll = 0;
			}

		}

		parameter.put("P_SOLLGESAMT", bdSummeSoll);

		bdSummeSoll = bdSummeSoll.add(new BigDecimal(dSummeMonatFeiertagSoll));

		bdSummeIst = bdSummeIst.add(bdSummeUrlaub);
		bdSummeIst = bdSummeIst.add(bdSummeArzt);
		bdSummeIst = bdSummeIst.add(bdSummeBehoerde);
		bdSummeIst = bdSummeIst.add(bdSummeFeiertag);
		bdSummeIst = bdSummeIst.add(bdSummeKrank);
		bdSummeIst = bdSummeIst.add(bdSummeSonstigeBezahlt);
		bdSummeIst = bdSummeIst.add(bdSummeKindkrank);

		// PJ 16204
		BigDecimal bdSummeIstFuerSaldenabfrage = bdSummeIst;

		bdSummeIst = bdSummeIst.subtract(bdSummeSoll);

		BigDecimal bdSummeSonstigerSondetaetigkeiten = bdSummeUrlaub
				.add(bdSummeArzt).add(bdSummeBehoerde).add(bdSummeFeiertag)
				.add(bdSummeKrank).add(bdSummeKindkrank)
				.add(bdSummeSonstigeBezahlt);
		parameter.put("P_SUMMEZUSSONDERTAETIGKEITEN",
				bdSummeSonstigerSondetaetigkeiten);
		// Vormonatsgleitzeitsaldo berechnen
		GleitzeitsaldoDto gleitzeitsaldoDtoVormonat = getGleitzeitsaldoVormonat(
				personalIId, iJahr, iMonat, d_datum_bis, dEintrittsdatum);

		parameter.put(
				"P_GLEITZEITSALDOVORMONAT",
				Helper.rundeKaufmaennisch(
						gleitzeitsaldoDto_Vormonat.getNSaldo(), 2));

		parameter.put("P_ABGERECHNETESTUNDEN",
				bdSummeStundenabrechnungUestdNormalstunden);

		// Abgerechnete Ueberstunden

		BigDecimal abAbgerechneteUeberstunden = bdSummeStundenabrechnungUestdPflichtig100
				.multiply(faktor100);

		abAbgerechneteUeberstunden = abAbgerechneteUeberstunden
				.add(bdSummeStundenabrechnungUestd200.multiply(faktor200));
		abAbgerechneteUeberstunden = abAbgerechneteUeberstunden
				.add(bdSummeStundenabrechnungUestdFrei100.multiply(faktor100));
		abAbgerechneteUeberstunden = abAbgerechneteUeberstunden
				.add(bdSummeStundenabrechnungUestdPflichtig50
						.multiply(faktor50));
		abAbgerechneteUeberstunden = abAbgerechneteUeberstunden
				.add(bdSummeStundenabrechnungUestdFrei50.multiply(faktor50));
		abAbgerechneteUeberstunden = abAbgerechneteUeberstunden
				.add(bdSummeStundenabrechnungUestdMehrstunden
						.multiply(faktormehrstd));

		parameter.put("P_ABGERECHNETEUEBERSTUNDEN", abAbgerechneteUeberstunden);

		// Ueberstundenpauschale abziehen:
		double fUeberstundenpauschale_abzuziehen = 0;
		// Wenn PlusStunden zwichen 0 und Ueberstundenpauschale, dann ist
		// Ueberstundenpauschale gleich den PlusStunden
		if (bdSummeIst.doubleValue() > 0
				&& bdSummeIst.doubleValue() <= fUeberstundenpauschale) {
			fUeberstundenpauschale_abzuziehen = bdSummeIst.doubleValue();
		} else if (bdSummeIst.doubleValue() > fUeberstundenpauschale) {
			fUeberstundenpauschale_abzuziehen = fUeberstundenpauschale;
		}
		// Wenn MinusStunden, dann keine Ueberstundenpauschale

		// Stundenausgleich
		BigDecimal bdVorhandeneMehrstunden = bdSummeMEHRSTUNDEN;
		BigDecimal bdVorhandeneUESTD50FREI = bdSummeUEST50FREI;
		BigDecimal bdVorhandeneUESTD50PFLICHTIG = bdSummeUEST50PFLICHTIG;
		BigDecimal bdVorhandeneUESTD100FREI = bdSummeUEST100FREI;
		BigDecimal bdVorhandeneUESTD100PFLICHTIG = bdSummeUEST100PFLICHTIG;
		BigDecimal bdVorhandeneUESTD200 = bdSummeUEST200;

		BigDecimal bdZuverbrauchendeMehrstunden = new BigDecimal(0.00);
		BigDecimal bdZuverbrauchendeUESTD50FREI = new BigDecimal(0.00);
		BigDecimal bdZuverbrauchendeUESTD50PFLICHTIG = new BigDecimal(0.00);
		BigDecimal bdZuverbrauchendeUESTD100FREI = new BigDecimal(0.00);
		BigDecimal bdZuverbrauchendeUESTD100PFLICHTIG = new BigDecimal(0.00);
		BigDecimal bdZuverbrauchendeUESTD200 = new BigDecimal(0.00);

		bdVorhandeneMehrstunden = bdVorhandeneMehrstunden
				.add(gleitzeitsaldoDto_Vormonat.getNSaldomehrstunden());
		bdVorhandeneUESTD50FREI = bdVorhandeneUESTD50FREI
				.add(gleitzeitsaldoDto_Vormonat.getNSaldouestfrei50());
		bdVorhandeneUESTD50PFLICHTIG = bdVorhandeneUESTD50PFLICHTIG
				.add(gleitzeitsaldoDto_Vormonat.getNSaldouestpflichtig50());
		bdVorhandeneUESTD100FREI = bdVorhandeneUESTD100FREI
				.add(gleitzeitsaldoDto_Vormonat.getNSaldouestfrei100());
		bdVorhandeneUESTD100PFLICHTIG = bdVorhandeneUESTD100PFLICHTIG
				.add(gleitzeitsaldoDto_Vormonat.getNSaldouestpflichtig100());
		bdVorhandeneUESTD200 = bdVorhandeneUESTD200
				.add(gleitzeitsaldoDto_Vormonat.getNSaldouest200());

		// An Report uebergeben
		parameter.put("P_UESTD200VORHANDEN", bdVorhandeneUESTD200);
		parameter.put("P_UESTD100PFLICHTIGVORHANDEN",
				bdVorhandeneUESTD100PFLICHTIG);
		parameter.put("P_UESTD100FREIVORHANDEN", bdVorhandeneUESTD100FREI);

		// SP2688
		if (bGutstundenZuUest50Addieren) {
			parameter.put("P_UESTD50PFLICHTIGVORHANDEN",
					bdVorhandeneUESTD50PFLICHTIG
							.add(bdSummeStundenabrechnungGutstunden));
		} else {
			parameter.put("P_UESTD50PFLICHTIGVORHANDEN",
					bdVorhandeneUESTD50PFLICHTIG);
		}

		parameter.put("P_UESTD50FREIVORHANDEN", bdVorhandeneUESTD50FREI);
		parameter.put("P_MEHRSTUNDENVORHANDEN", bdVorhandeneMehrstunden);

		boolean bStundenWerdenVerbraucht = true;
		if (personalDto.getKollektivDto() != null
				&& Helper.short2boolean(personalDto.getKollektivDto()
						.getBVerbraucheuestd()) == false) {
			bStundenWerdenVerbraucht = false;
		}

		parameter.put("P_FAKTORUESTD50", faktor50);
		parameter.put("P_FAKTORUESTD100", faktor100);
		parameter.put("P_FAKTORUESTD200", faktor200);
		parameter.put("P_FAKTORMEHRSTD", faktormehrstd);

		if (bStundenWerdenVerbraucht) {

			bdVorhandeneUESTD200 = bdVorhandeneUESTD200.multiply(faktor200);

			bdVorhandeneUESTD100PFLICHTIG = bdVorhandeneUESTD100PFLICHTIG
					.multiply(faktor100);
			bdVorhandeneUESTD100FREI = bdVorhandeneUESTD100FREI
					.multiply(faktor100);

			bdVorhandeneUESTD50PFLICHTIG = bdVorhandeneUESTD50PFLICHTIG
					.multiply(faktor50);
			bdVorhandeneUESTD50FREI = bdVorhandeneUESTD50FREI
					.multiply(faktor50);

			bdVorhandeneMehrstunden = bdVorhandeneMehrstunden
					.multiply(faktormehrstd);

			// Nur wenn Gleitzeitsaldo < 0, dann muessen Ueberstunden verbraucht
			// werden
			parameter.put("P_VERBRAUCHTEUESTD", new BigDecimal(0));
			if (personalDto.getKollektivIId() != null) {
				BigDecimal bdZeitsaldoGesamt = bdSummeIst
						.add(gleitzeitsaldoDto_Vormonat.getNSaldo())
						.subtract(bdSummeStundenabrechnungUestdNormalstunden)
						.subtract(
								new BigDecimal(
										fUeberstundenpauschale_abzuziehen));

				if (bdZeitsaldoGesamt.doubleValue() < 0) {

					BigDecimal bdZuVerbrauchen = bdZeitsaldoGesamt.abs();

					// CK: 20090123 lt. Kunde werden die Stunden in
					// folgender Reihenfolge verbraucht:
					// zuerst 50P, dann 100P, dann50F und dann 100F
					if (bdZuVerbrauchen.doubleValue() > 0) {
						if (bdVorhandeneUESTD50PFLICHTIG.doubleValue() >= bdZuVerbrauchen
								.doubleValue()) {
							bdZuverbrauchendeUESTD50PFLICHTIG = bdZuVerbrauchen;
							bdZuVerbrauchen = bdZuVerbrauchen
									.subtract(bdZuVerbrauchen);
						} else {
							bdZuverbrauchendeUESTD50PFLICHTIG = bdVorhandeneUESTD50PFLICHTIG;
							bdZuVerbrauchen = bdZuVerbrauchen
									.subtract(bdVorhandeneUESTD50PFLICHTIG);
						}

					}
					if (bdVorhandeneUESTD100PFLICHTIG.doubleValue() >= bdZuVerbrauchen
							.doubleValue()) {
						bdZuverbrauchendeUESTD100PFLICHTIG = bdZuVerbrauchen;
						bdZuVerbrauchen = bdZuVerbrauchen
								.subtract(bdZuVerbrauchen);
					} else {
						bdZuverbrauchendeUESTD100PFLICHTIG = bdVorhandeneUESTD100PFLICHTIG;
						bdZuVerbrauchen = bdZuVerbrauchen
								.subtract(bdVorhandeneUESTD100PFLICHTIG);
					}

					if (bdZuVerbrauchen.doubleValue() > 0) {
						if (bdVorhandeneUESTD50FREI.doubleValue() >= bdZuVerbrauchen
								.doubleValue()) {
							bdZuverbrauchendeUESTD50FREI = bdZuVerbrauchen;
							bdZuVerbrauchen = bdZuVerbrauchen
									.subtract(bdZuVerbrauchen);
						} else {
							bdZuverbrauchendeUESTD50FREI = bdVorhandeneUESTD50FREI;
							bdZuVerbrauchen = bdZuVerbrauchen
									.subtract(bdVorhandeneUESTD50FREI);
						}

					}
					if (bdZuVerbrauchen.doubleValue() > 0) {
						if (bdVorhandeneUESTD100FREI.doubleValue() >= bdZuVerbrauchen
								.doubleValue()) {
							bdZuverbrauchendeUESTD100FREI = bdZuVerbrauchen;
							bdZuVerbrauchen = bdZuVerbrauchen
									.subtract(bdZuVerbrauchen);
						} else {
							bdZuverbrauchendeUESTD100FREI = bdVorhandeneUESTD100FREI;
							bdZuVerbrauchen = bdZuVerbrauchen
									.subtract(bdVorhandeneUESTD100FREI);
						}

					}
					if (bdZuVerbrauchen.doubleValue() > 0) {
						if (bdVorhandeneUESTD200.doubleValue() >= bdZuVerbrauchen
								.doubleValue()) {
							bdZuverbrauchendeUESTD200 = bdZuVerbrauchen;
							bdZuVerbrauchen = bdZuVerbrauchen
									.subtract(bdZuVerbrauchen);
						} else {
							bdZuverbrauchendeUESTD200 = bdVorhandeneUESTD200;
							bdZuVerbrauchen = bdZuVerbrauchen
									.subtract(bdVorhandeneUESTD200);
						}

					}
					// Mehrstunden koennen nur verbraucht werden, wenn Faktor >0
					// sie werden
					// mit
					// dem Gleitzeitsaldo verbraucht

					if (bdZuVerbrauchen.doubleValue() > 0) {
						if (bdVorhandeneMehrstunden.doubleValue() >= bdZuVerbrauchen
								.doubleValue()) {
							bdZuverbrauchendeMehrstunden = bdZuVerbrauchen;
							bdZuVerbrauchen = bdZuVerbrauchen
									.subtract(bdZuVerbrauchen);
						} else {
							bdZuverbrauchendeMehrstunden = bdVorhandeneMehrstunden;
							bdZuVerbrauchen = bdZuVerbrauchen
									.subtract(bdVorhandeneMehrstunden);
						}

					}

					BigDecimal bdVerbrauchteUESTD = bdZuverbrauchendeUESTD100PFLICHTIG
							.add(bdZuverbrauchendeUESTD100FREI)
							.add(bdZuverbrauchendeUESTD50PFLICHTIG)
							.add(bdZuverbrauchendeUESTD50FREI)
							.add(bdZuverbrauchendeUESTD200)
							.add(bdZuverbrauchendeMehrstunden);

					parameter.put("P_VERBRAUCHTEUESTD",
							Helper.rundeKaufmaennisch(bdVerbrauchteUESTD, 2));

					bdSummeIst = bdSummeIst.add(bdVerbrauchteUESTD);

				}

			}

			// Verbrauchte abziehen
			bdVorhandeneUESTD100PFLICHTIG = bdVorhandeneUESTD100PFLICHTIG
					.subtract(bdZuverbrauchendeUESTD100PFLICHTIG);
			bdVorhandeneUESTD200 = bdVorhandeneUESTD200
					.subtract(bdZuverbrauchendeUESTD200);
			bdVorhandeneUESTD100FREI = bdVorhandeneUESTD100FREI
					.subtract(bdZuverbrauchendeUESTD100FREI);
			bdVorhandeneUESTD50PFLICHTIG = bdVorhandeneUESTD50PFLICHTIG
					.subtract(bdZuverbrauchendeUESTD50PFLICHTIG);
			bdVorhandeneUESTD50FREI = bdVorhandeneUESTD50FREI
					.subtract(bdZuverbrauchendeUESTD50FREI);
			bdVorhandeneMehrstunden = bdVorhandeneMehrstunden
					.subtract(bdZuverbrauchendeMehrstunden);

			// Wieder zurueckdividieren

			if (faktor200.doubleValue() != 0) {
				bdVorhandeneUESTD200 = bdVorhandeneUESTD200.divide(faktor200,
						4, BigDecimal.ROUND_HALF_EVEN);

			}
			if (faktor100.doubleValue() != 0) {
				bdVorhandeneUESTD100PFLICHTIG = bdVorhandeneUESTD100PFLICHTIG
						.divide(faktor100, 4, BigDecimal.ROUND_HALF_EVEN);
				bdVorhandeneUESTD100FREI = bdVorhandeneUESTD100FREI.divide(
						faktor100, 4, BigDecimal.ROUND_HALF_EVEN);
			}
			if (faktor50.doubleValue() != 0) {
				bdVorhandeneUESTD50PFLICHTIG = bdVorhandeneUESTD50PFLICHTIG
						.divide(faktor50, 4, BigDecimal.ROUND_HALF_EVEN);
				bdVorhandeneUESTD50FREI = bdVorhandeneUESTD50FREI.divide(
						faktor50, 4, BigDecimal.ROUND_HALF_EVEN);
			}
			if (faktormehrstd.doubleValue() != 0) {
				bdVorhandeneMehrstunden = bdVorhandeneMehrstunden.divide(
						faktormehrstd, 4, BigDecimal.ROUND_HALF_EVEN);

			}

		} else {
			parameter.put("P_VERBRAUCHTEUESTD", new BigDecimal(0));
		}

		parameter.put("P_UESTD100PFLICHTIGVERBRAUCHT",
				bdZuverbrauchendeUESTD100PFLICHTIG);
		parameter.put("P_UESTD200VERBRAUCHT", bdZuverbrauchendeUESTD200);
		parameter
				.put("P_UESTD100FREIVERBRAUCHT", bdZuverbrauchendeUESTD100FREI);
		parameter.put("P_UESTD50PFLICHTIGVERBRAUCHT",
				bdZuverbrauchendeUESTD50PFLICHTIG);
		parameter.put("P_UESTD50FREIVERBRAUCHT", bdZuverbrauchendeUESTD50FREI);
		parameter.put("P_MEHRSTUNDENVERBRAUCHT", bdZuverbrauchendeMehrstunden);

		MonatsabrechnungDto monatsabrechnungDto = new MonatsabrechnungDto();

		boolean bSaldenabfrageNurIstAktuellesMonat = false;

		try {
			ParametermandantDto parameterIstZeit = (ParametermandantDto) getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_PERSONAL,
							ParameterFac.PARAMETER_SALDENABFRAGE_NUR_IST_STUNDEN_DES_AKTUELLEN_MONATS);

			bSaldenabfrageNurIstAktuellesMonat = ((Boolean) parameterIstZeit
					.getCWertAsObject());

		} catch (RemoteException ex5) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
		}

		// PJ18621
		BigDecimal gleizeitsaldoZumAbrechnungszeitpunkt = Helper
				.rundeKaufmaennisch(
						gleitzeitsaldoDto_Vormonat
								.getNSaldo()
								.add(bdSummeIst)
								.subtract(
										bdSummeStundenabrechnungUestdNormalstunden),
						4);
		parameter.put("P_GLEITZEITSALDO_ABRECHNUNGSZEITPUNKT",
				gleizeitsaldoZumAbrechnungszeitpunkt);

		if (bSaldozurueckschreiben == true) {
			GleitzeitsaldoDto dto = null;
			try {
				Query query = em
						.createNamedQuery("GleitzeitsaldofindByPersonalIIdIJahrIMonat");
				query.setParameter(1, personalIId);
				query.setParameter(2, iJahr);
				query.setParameter(3, iMonat);
				Gleitzeitsaldo gleitzeitsaldo = (Gleitzeitsaldo) query
						.getSingleResult();
				// if (gleitzeitsaldo==null) {
				// throw new EJBExceptionLP(EJBExceptionLP.
				// FEHLER_BEI_FIND,
				// null);
				// }
				dto = GleitzeitsaldoDtoAssembler.createDto(gleitzeitsaldo);

				if (Helper.short2boolean(dto.getBGesperrt()) == false) {

					if (bdVorhandeneMehrstunden.doubleValue() >= 0) {
						dto.setNSaldomehrstunden(bdVorhandeneMehrstunden);
					} else {
						dto.setNSaldomehrstunden(new BigDecimal(0));
					}

					if (bdVorhandeneUESTD100FREI.doubleValue() >= 0) {

						dto.setNSaldouestfrei100(bdVorhandeneUESTD100FREI);// .
					} else {
						dto.setNSaldouestfrei100(new BigDecimal(0));
					}

					if (bdVorhandeneUESTD200.doubleValue() >= 0) {

						dto.setNSaldouest200(bdVorhandeneUESTD200);// .
					} else {
						dto.setNSaldouest200(new BigDecimal(0));
					}

					if (bdVorhandeneUESTD50FREI.doubleValue() >= 0) {

						dto.setNSaldouestfrei50(bdVorhandeneUESTD50FREI);// .
					} else {
						dto.setNSaldouestfrei50(new BigDecimal(0));
					}

					if (bdVorhandeneUESTD100PFLICHTIG.doubleValue() >= 0) {

						dto.setNSaldouestpflichtig100(bdVorhandeneUESTD100PFLICHTIG);
					} else {
						dto.setNSaldouestpflichtig100(new BigDecimal(0));
					}

					// PJ15624
					// SP2688
					if (bGutstundenZuUest50Addieren) {

						if (bdVorhandeneUESTD50PFLICHTIG.add(
								bdSummeStundenabrechnungGutstunden)
								.doubleValue() >= 0) {
							dto.setNSaldouestpflichtig50(bdVorhandeneUESTD50PFLICHTIG
									.add(bdSummeStundenabrechnungGutstunden));
						} else {
							dto.setNSaldouestpflichtig50(new BigDecimal(0));
						}

					} else {
						if (bdVorhandeneUESTD50PFLICHTIG.doubleValue() >= 0) {
							dto.setNSaldouestpflichtig50(bdVorhandeneUESTD50PFLICHTIG);
						} else {
							dto.setNSaldouestpflichtig50(new BigDecimal(0));
						}
					}

					// Ueberstundenpauschale und Stundenabrechnung abziehen
					BigDecimal bdSaldo = gleitzeitsaldoDto_Vormonat
							.getNSaldo()
							.add(bdSummeIst
									.subtract(
											new BigDecimal(
													fUeberstundenpauschale_abzuziehen))
									.subtract(
											bdSummeStundenabrechnungUestdNormalstunden));

					dto.setNSaldo(Helper.rundeKaufmaennisch(bdSaldo, 4));
					// P 15964
					if (bSaldenabfrageNurIstAktuellesMonat == false) {
						monatsabrechnungDto.setnSaldo(dto.getNSaldo());
					} else {
						monatsabrechnungDto
								.setnSaldo(bdSummeIstFuerSaldenabfrage);
					}

					try {
						getPersonalFac()
								.updateGleitzeitsaldo(dto, theClientDto);
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

				}
			} catch (NoResultException e) {

				dto = new GleitzeitsaldoDto();
				dto.setPersonalIId(personalIId);
				dto.setIJahr(iJahr);
				dto.setIMonat(iMonat);

				if (bdVorhandeneMehrstunden.doubleValue() >= 0) {

					dto.setNSaldomehrstunden(bdVorhandeneMehrstunden
							.subtract(bdSummeStundenabrechnungUestdMehrstunden));
				} else {
					dto.setNSaldomehrstunden(new BigDecimal(0));
				}

				if (bdVorhandeneUESTD100FREI.doubleValue() >= 0) {
					dto.setNSaldouestfrei100(bdVorhandeneUESTD100FREI
							.subtract(bdSummeStundenabrechnungUestdFrei100));
				} else {
					dto.setNSaldouestfrei100(new BigDecimal(0));
				}

				if (bdVorhandeneUESTD50FREI.doubleValue() >= 0) {
					dto.setNSaldouestfrei50(bdVorhandeneUESTD50FREI);
				} else {
					dto.setNSaldouestfrei50(new BigDecimal(0));
				}

				if (bdVorhandeneUESTD100PFLICHTIG.doubleValue() >= 0) {
					dto.setNSaldouestpflichtig100(bdVorhandeneUESTD100PFLICHTIG);
				} else {
					dto.setNSaldouestpflichtig100(new BigDecimal(0));
				}

				// SP2688
				if (bGutstundenZuUest50Addieren) {

					if (bdVorhandeneUESTD50PFLICHTIG.add(
							bdSummeStundenabrechnungGutstunden).doubleValue() >= 0) {
						dto.setNSaldouestpflichtig50(bdVorhandeneUESTD50PFLICHTIG
								.add(bdSummeStundenabrechnungGutstunden));
					} else {
						dto.setNSaldouestpflichtig50(new BigDecimal(0));
					}

				} else {
					if (bdVorhandeneUESTD50PFLICHTIG.doubleValue() >= 0) {
						dto.setNSaldouestpflichtig50(bdVorhandeneUESTD50PFLICHTIG);
					} else {
						dto.setNSaldouestpflichtig50(new BigDecimal(0));
					}
				}

				dto.setNSaldo(Helper
						.rundeKaufmaennisch(
								gleitzeitsaldoDto_Vormonat
										.getNSaldo()
										.add(bdSummeIst)
										.subtract(
												bdSummeStundenabrechnungUestdNormalstunden),
								4));

				// P 15964
				if (bSaldenabfrageNurIstAktuellesMonat == false) {
					monatsabrechnungDto.setnSaldo(dto.getNSaldo());
				} else {
					monatsabrechnungDto.setnSaldo(bdSummeIstFuerSaldenabfrage);
				}

				// Ueberstundenpauschale abziehen:
				dto.setNSaldouestpflichtig50(dto.getNSaldouestpflichtig50()
						.subtract(
								new BigDecimal(
										fUeberstundenpauschale_abzuziehen)));

				try {
					getPersonalFac().createGleitzeitsaldo(dto, theClientDto);
				} catch (RemoteException ex9) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex9);
				}
			}

			// Verbleibende Stunden berechnen
			BigDecimal bdUebrigeNormalStunden = dto.getNSaldouestfrei100()
					.multiply(faktor100);
			bdUebrigeNormalStunden = bdUebrigeNormalStunden.add(dto
					.getNSaldouestpflichtig100().multiply(faktor100));
			bdUebrigeNormalStunden = bdUebrigeNormalStunden.add(dto
					.getNSaldouestfrei50().multiply(faktor50));
			bdUebrigeNormalStunden = bdUebrigeNormalStunden.add(dto
					.getNSaldouestpflichtig50().multiply(faktor50));
			bdUebrigeNormalStunden = bdUebrigeNormalStunden.add(dto
					.getNSaldomehrstunden().multiply(faktormehrstd));

			// Automatische Auszahlung, wenn Flag gesetzt und Ist groesser als
			// Puffer
			if (bUeberstundenAutomatischAuszahlen
					&& bdUebrigeNormalStunden.add(bdSummeIst).doubleValue() > bdUeberstundenPuffer
							.doubleValue()) {
				Calendar cStundenabrechnung = Calendar.getInstance();
				cStundenabrechnung.set(Calendar.YEAR, iJahr);
				cStundenabrechnung.set(Calendar.MONTH, iMonat);
				cStundenabrechnung.set(Calendar.DAY_OF_MONTH, 1);
				cStundenabrechnung.set(Calendar.MONTH,
						cStundenabrechnung.get(Calendar.MONTH) + 1);

				StundenabrechnungDto stundenabrechnungDto = new StundenabrechnungDto();
				stundenabrechnungDto.setPersonalIId(personalIId);
				stundenabrechnungDto.setTDatum(Helper
						.cutTimestamp(new Timestamp(cStundenabrechnung
								.getTimeInMillis())));

				stundenabrechnungDto.setCKommentar("AUTOMATISCHE-AUSZAHLUNG");
				stundenabrechnungDto.setNGutstunden(new BigDecimal(0));

				stundenabrechnungDto
						.setNQualifikationspraemie(new BigDecimal(0));

				BigDecimal auszuzahlendeStunden = bdUebrigeNormalStunden.add(
						bdSummeIst).subtract(bdUeberstundenPuffer);

				//

				if (auszuzahlendeStunden.doubleValue() > 0) {

					if (bdSummeIst.doubleValue() > 0) {
						stundenabrechnungDto.setNNormalstunden(bdSummeIst);
						auszuzahlendeStunden = auszuzahlendeStunden
								.subtract(bdSummeIst);

					} else {
						stundenabrechnungDto
								.setNNormalstunden(new BigDecimal(0));
					}

					// 200% Steuerpflichtig vorhanden
					if (auszuzahlendeStunden.doubleValue() > 0) {
						BigDecimal bdVorh = dto.getNSaldouest200().multiply(
								faktor200);
						if (auszuzahlendeStunden.doubleValue() >= bdVorh
								.doubleValue()) {
							stundenabrechnungDto.setNUest200(dto
									.getNSaldouest200());
							auszuzahlendeStunden = auszuzahlendeStunden
									.subtract(bdVorh);
						} else {
							stundenabrechnungDto
									.setNUest200(auszuzahlendeStunden.divide(
											faktor200,
											BigDecimal.ROUND_HALF_EVEN));
							auszuzahlendeStunden = new BigDecimal(0);
						}
					} // 100% Steuerpflichtig vorhanden
					if (auszuzahlendeStunden.doubleValue() > 0) {
						BigDecimal bdVorh = dto.getNSaldouestpflichtig100()
								.multiply(faktor100);
						if (auszuzahlendeStunden.doubleValue() >= bdVorh
								.doubleValue()) {
							stundenabrechnungDto.setNUestpflichtig100(dto
									.getNSaldouestpflichtig100());
							auszuzahlendeStunden = auszuzahlendeStunden
									.subtract(bdVorh);
						} else {
							stundenabrechnungDto
									.setNUestpflichtig100(auszuzahlendeStunden
											.divide(faktor100,
													BigDecimal.ROUND_HALF_EVEN));
							auszuzahlendeStunden = new BigDecimal(0);
						}
					}
					// 100% Steuerfrei vorhanden
					if (auszuzahlendeStunden.doubleValue() > 0) {
						BigDecimal bdVorh = dto.getNSaldouestfrei100()
								.multiply(faktor100);
						if (auszuzahlendeStunden.doubleValue() >= bdVorh
								.doubleValue()) {
							stundenabrechnungDto.setNUestfrei100(dto
									.getNSaldouestfrei100());
							auszuzahlendeStunden = auszuzahlendeStunden
									.subtract(bdVorh);
						} else {
							stundenabrechnungDto
									.setNUestfrei100(auszuzahlendeStunden
											.divide(faktor100,
													BigDecimal.ROUND_HALF_EVEN));
							auszuzahlendeStunden = new BigDecimal(0);
						}
					}
					// 50% Steuerpflichtig vorhanden
					if (auszuzahlendeStunden.doubleValue() > 0) {
						BigDecimal bdVorh = dto.getNSaldouestpflichtig50()
								.multiply(faktor50);
						if (auszuzahlendeStunden.doubleValue() >= bdVorh
								.doubleValue()) {
							stundenabrechnungDto.setNUestpflichtig50(dto
									.getNSaldouestpflichtig50());
							auszuzahlendeStunden = auszuzahlendeStunden
									.subtract(bdVorh);
						} else {
							stundenabrechnungDto
									.setNUestpflichtig50(auszuzahlendeStunden
											.divide(faktor50,
													BigDecimal.ROUND_HALF_EVEN));
							auszuzahlendeStunden = new BigDecimal(0);
						}
					}
					// 50% Steuerfrei vorhanden
					if (auszuzahlendeStunden.doubleValue() > 0) {
						BigDecimal bdVorh = dto.getNSaldouestfrei50().multiply(
								faktor50);
						if (auszuzahlendeStunden.doubleValue() >= bdVorh
								.doubleValue()) {
							stundenabrechnungDto.setNUestfrei50(dto
									.getNSaldouestfrei50());
							auszuzahlendeStunden = auszuzahlendeStunden
									.subtract(bdVorh);
						} else {
							stundenabrechnungDto
									.setNUestfrei50(auszuzahlendeStunden
											.divide(faktor50,
													BigDecimal.ROUND_HALF_EVEN));
							auszuzahlendeStunden = new BigDecimal(0);
						}
					}
					// Mehrstunden vorhanden
					if (auszuzahlendeStunden.doubleValue() > 0) {
						BigDecimal bdVorh = dto.getNSaldomehrstunden()
								.multiply(faktormehrstd);
						if (auszuzahlendeStunden.doubleValue() >= bdVorh
								.doubleValue()) {
							stundenabrechnungDto.setNMehrstunden(dto
									.getNSaldomehrstunden());
							auszuzahlendeStunden = auszuzahlendeStunden
									.subtract(bdVorh);
						} else {
							stundenabrechnungDto
									.setNMehrstunden(auszuzahlendeStunden
											.divide(faktormehrstd,
													BigDecimal.ROUND_HALF_EVEN));
							auszuzahlendeStunden = new BigDecimal(0);
						}
					}

					try {
						StundenabrechnungDto stundenabrechnungDtoTemp = getPersonalFac()
								.stundenabrechnungFindByPersonalIIdDDatum(
										personalIId,
										Helper.cutTimestamp(new Timestamp(
												cStundenabrechnung
														.getTimeInMillis())));

						if (stundenabrechnungDtoTemp != null) {
							stundenabrechnungDto
									.setIId(stundenabrechnungDtoTemp.getIId());
							getPersonalFac().updateStundenabrechnung(
									stundenabrechnungDto, theClientDto);
						} else {
							try {
								getPersonalFac().createStundenabrechnung(
										stundenabrechnungDto, theClientDto);
							} catch (RemoteException ex7) {
								throwEJBExceptionLPRespectOld(ex7);
							}

						}
					}

					catch (RemoteException ex3) {
						throwEJBExceptionLPRespectOld(ex3);
					}

				}

			} else {
				if (bUeberstundenAutomatischAuszahlen) {
					// SP961 Wenn nicht mehr zutrifft, dann die Zeile loeschen

					Calendar cStundenabrechnung = Calendar.getInstance();
					cStundenabrechnung.set(Calendar.YEAR, iJahr);
					cStundenabrechnung.set(Calendar.MONTH, iMonat);
					cStundenabrechnung.set(Calendar.DAY_OF_MONTH, 1);
					cStundenabrechnung.set(Calendar.MONTH,
							cStundenabrechnung.get(Calendar.MONTH) + 1);

					try {
						StundenabrechnungDto stundenabrechnungDtoTemp = getPersonalFac()
								.stundenabrechnungFindByPersonalIIdDDatum(
										personalIId,
										Helper.cutTimestamp(new Timestamp(
												cStundenabrechnung
														.getTimeInMillis())));
						if (stundenabrechnungDtoTemp != null
								&& stundenabrechnungDtoTemp.getCKommentar()
										.equals("AUTOMATISCHE-AUSZAHLUNG")) {
							getPersonalFac().removeStundenabrechnung(
									stundenabrechnungDtoTemp);
						}
					} catch (RemoteException ex3) {
						throwEJBExceptionLPRespectOld(ex3);
					}
				}
			}

		}
		// Erstellung des Reports
		index = -1;
		sAktuellerReport = ZeiterfassungFac.REPORT_MONATSABRECHNUNG;

		parameter.put("P_PERSONAL", personalDto.formatAnrede());
		parameter.put("P_PERSONALNUMMER", personalDto.getCPersonalnr());
		if (personalDto.getKollektivDto() != null) {
			parameter.put("P_KOLLEKTIV", personalDto.getKollektivDto()
					.getCBez());
		}

		parameter.put(
				"P_SORTIERUNG",
				getZeiterfassungFac().getParameterSortierungZeitauswertungen(
						iOptionSortierung, theClientDto));

		parameter
				.put("P_FEIERTAGSOLL", new BigDecimal(dSummeMonatFeiertagSoll));

		boolean bTeilzeit = false;

		try {

			parameter.put(
					"P_PERSONALART",
					getPersonalFac().personalartFindByPrimaryKey(
							personalDto.getPersonalartCNr(), theClientDto)
							.getBezeichnung());

			PersonalzeitmodellDto personalzeitmodellDto = getPersonalFac()
					.personalzeitmodellFindZeitmodellZuDatum(personalIId,
							new java.sql.Timestamp(cal.getTime().getTime()),
							theClientDto);
			if (personalzeitmodellDto != null) {
				parameter.put("P_ZEITMODELL", personalzeitmodellDto
						.getZeitmodellDto().getCNr());

				parameter.put("P_TEILZEIT", Helper
						.short2Boolean(personalzeitmodellDto.getZeitmodellDto()
								.getBTeilzeit()));

				bTeilzeit = Helper.short2Boolean(personalzeitmodellDto
						.getZeitmodellDto().getBTeilzeit());

			} else {
				parameter.put("P_ZEITMODELL", "Kein Zeitmodell zugeordnet");
				parameter.put("P_TEILZEIT", new Boolean(false));
			}
		} catch (RemoteException ex11) {
			parameter.put("P_ZEITMODELL", "Kein Zeitmodell zugeordnet");
		}

		try {

			// Urlaubsberechnung des Vorjahres
			try {
				Calendar c = Calendar.getInstance();

				// set Date auf den letzten Tag des Vorjahres
				c.set(c.YEAR, iJahr.intValue() - 1);
				c.set(c.MONTH, c.DECEMBER);
				c.set(c.DATE, 31);

				c.set(c.HOUR_OF_DAY, 23);
				c.set(c.MINUTE, 59);
				c.set(c.SECOND, 59);

				java.sql.Date d_vorjahr = new java.sql.Date(c.getTime()
						.getTime());

				if (dEintrittsdatum.before(d_vorjahr)) {

					UrlaubsabrechnungDto urlaubsabrechnungVorjahrDto = berechneUrlaubsAnspruch(
							personalIId, d_vorjahr, theClientDto);

					parameter.put("P_VORJAHRURLAUBSANSPRUCHSTUNDEN",
							urlaubsabrechnungVorjahrDto
									.getNAktuellerUrlaubsanspruchStunden());

					parameter.put("P_VORJAHRVERFUEGBARERURLAUBSTUNDEN",
							urlaubsabrechnungVorjahrDto
									.getNVerfuegbarerUrlaubStunden());

					parameter.put("P_VORJAHRURLAUBSANSPRUCHTAGE",
							urlaubsabrechnungVorjahrDto
									.getNAktuellerUrlaubsanspruchTage());

					parameter.put("P_VORJAHRVERFUEGBARERURLAUBTAGE",
							urlaubsabrechnungVorjahrDto
									.getNVerfuegbarerUrlaubTage());

				}

			} catch (EJBExceptionLP ex6) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex6);
			}
			java.sql.Date dDatumUrlaubsabrechnung = new java.sql.Date(
					d_datum_bis.getTime());
			if (dAustrittsdatum.before(d_datum_bis)) {
				dDatumUrlaubsabrechnung = new java.sql.Date(
						dAustrittsdatum.getTime());
			}

			UrlaubsabrechnungDto urlaubsabrechnungDto = berechneUrlaubsAnspruch(
					personalIId, dDatumUrlaubsabrechnung, theClientDto);

			parameter.put("P_AKTUELLERURLAUBSANSPRUCHSTUNDEN",
					urlaubsabrechnungDto.getNAktuellerUrlaubsanspruchStunden());

			parameter
					.put("P_AKTUELLERURLAUBSVERBRAUCHTSTUNDEN",
							urlaubsabrechnungDto
									.getNAktuellerUrlaubVerbrauchtStunden());

			parameter.put("P_ALTERURLAUBSANSPRUCHSTUNDEN",
					urlaubsabrechnungDto.getNAlterUrlaubsanspruchStunden());

			parameter.put("P_GEPLANTERURLAUBSTUNDEN",
					urlaubsabrechnungDto.getNGeplanterUrlaubStunden());

			parameter.put("P_VERFUEGBARERURLAUBSTUNDEN",
					urlaubsabrechnungDto.getNVerfuegbarerUrlaubStunden());

			parameter.put("P_ALIQUOTERURLAUBSTUNDEN",
					urlaubsabrechnungDto.getNAliquoterAnspruchStunden());

			parameter.put("P_AKTUELLERURLAUBSANSPRUCHTAGE",
					urlaubsabrechnungDto.getNAktuellerUrlaubsanspruchTage());

			parameter.put("P_AKTUELLERURLAUBSVERBRAUCHTTAGE",
					urlaubsabrechnungDto.getNAktuellerUrlaubVerbrauchtTage());

			parameter.put("P_ALTERURLAUBSANSPRUCHTAGE",
					urlaubsabrechnungDto.getNAlterUrlaubsanspruchTage());

			parameter.put("P_GEPLANTERURLAUBTAGE",
					urlaubsabrechnungDto.getNGeplanterUrlaubTage());

			parameter.put("P_VERFUEGBARERURLAUBTAGE",
					urlaubsabrechnungDto.getNVerfuegbarerUrlaubTage());

			if (bTeilzeit == false) {
				monatsabrechnungDto.setNVerfuegbarerurlaub(urlaubsabrechnungDto
						.getNAlterUrlaubsanspruchTage()
						.add(urlaubsabrechnungDto
								.getNAktuellerUrlaubsanspruchTage())
						.subtract(
								urlaubsabrechnungDto
										.getNAktuellerUrlaubVerbrauchtTage()));
				monatsabrechnungDto.setsEinheitVerfuegbarerUrlaub("Tage");
			} else {
				monatsabrechnungDto
						.setNVerfuegbarerurlaub(urlaubsabrechnungDto
								.getNAlterUrlaubsanspruchStunden()
								.add(urlaubsabrechnungDto
										.getNAktuellerUrlaubsanspruchStunden())
								.subtract(
										urlaubsabrechnungDto
												.getNAktuellerUrlaubVerbrauchtStunden()));
				monatsabrechnungDto.setsEinheitVerfuegbarerUrlaub("Stunden");
			}

			parameter.put("P_ALIQUOTERURLAUBTAGE",
					urlaubsabrechnungDto.getNAliquoterAnspruchTage());
		} catch (EJBExceptionLP ex6) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex6);
		}

		// Bereitschaften
		String[] fieldnames = new String[] { "F_ART", "F_VON", "F_BIS",
				"F_BEMERKUNG", "F_KW", "F_TAG", "F_DAUER", "F_FEIERTAG" };
		Object[][] zeilenBereitschaften = new Object[alBereitschaften.size()][fieldnames.length];
		for (int i = 0; i < alBereitschaften.size(); i++) {
			zeilenBereitschaften[i][0] = alBereitschaften.get(i)
					.getBereitschaftsart();
			zeilenBereitschaften[i][1] = alBereitschaften.get(i).gettVon();
			zeilenBereitschaften[i][2] = alBereitschaften.get(i).gettBis();
			zeilenBereitschaften[i][3] = alBereitschaften.get(i).getBemerkung();

			zeilenBereitschaften[i][4] = alBereitschaften.get(i).getKw();
			zeilenBereitschaften[i][5] = alBereitschaften.get(i)
					.getTagesartCNr();
			zeilenBereitschaften[i][6] = alBereitschaften.get(i).getdDauer();
			zeilenBereitschaften[i][7] = alBereitschaften.get(i).getFeiertag();
		}

		parameter.put("P_SUBREPORT_BEREITSCHAFTEN", new LPDatenSubreport(
				zeilenBereitschaften, fieldnames));

		// Ueberstundenpauschale
		parameter.put("P_UEBERSTUNDENPAUSCHALE", Helper.rundeKaufmaennisch(
				new BigDecimal(fUeberstundenpauschale_abzuziehen), 2));

		parameter
				.put("P_MONAT",
						new DateFormatSymbols(theClientDto.getLocUi())
								.getMonths()[iMonat.intValue()] + " " + iJahr);

		cal.set(iJahr.intValue(), iMonat.intValue(), 1);

		data = new Object[monatsabrechnungZeilen.size()][REPORT_MONATSABRECHNUNG_ANZAHL_SPALTEN];

		for (int i = 0; i < monatsabrechnungZeilen.size(); i++) {
			ZeileMonatsabrechnungDto z = (ZeileMonatsabrechnungDto) monatsabrechnungZeilen
					.get(i);
			data[i][REPORT_MONATSABRECHNUNG_ARZT] = z.getBdArzt();
			data[i][REPORT_MONATSABRECHNUNG_BEHOERDE] = z.getBdBehoerde();
			data[i][REPORT_MONATSABRECHNUNG_BEMERKUNG] = z.getSBemerkung();
			data[i][REPORT_MONATSABRECHNUNG_BIS] = z.getTBis();
			data[i][REPORT_MONATSABRECHNUNG_TAGESART] = z.getSTagesart();
			data[i][REPORT_MONATSABRECHNUNG_SUBREPORT_ZULAGEN] = z
					.getSubreportZulagen();

			Calendar cCal = Calendar.getInstance();
			cCal.setTimeInMillis(z.getTDatum().getTime());
			Integer iTag = cCal.get(Calendar.DATE);
			data[i][REPORT_MONATSABRECHNUNG_DATUM] = iTag;

			data[i][REPORT_MONATSABRECHNUNG_DIFF] = z.getBdDiff();
			data[i][REPORT_MONATSABRECHNUNG_FEIERTAG] = z.getBdFeiertag();
			data[i][REPORT_MONATSABRECHNUNG_IST] = z.getBdIst();
			data[i][REPORT_MONATSABRECHNUNG_KALENDERWOCHE] = z.getIKw();
			data[i][REPORT_MONATSABRECHNUNG_KRANK] = z.getBdKrank();
			data[i][REPORT_MONATSABRECHNUNG_KINDKRANK] = z.getBdKindkrank();
			data[i][REPORT_MONATSABRECHNUNG_SOLL] = z.getBdSoll();
			data[i][REPORT_MONATSABRECHNUNG_SONSTIGE_BEZAHLTETAETIGKEITEN] = z
					.getBdSonstigeBezahlt();
			data[i][REPORT_MONATSABRECHNUNG_ZEITMODELL] = z.getSZeitmodell();
			data[i][REPORT_MONATSABRECHNUNG_SONSTIGE_UNBEZAHLTETAETIGKEITEN] = z
					.getBdSonstigeNichtBezahlt();
			data[i][REPORT_MONATSABRECHNUNG_TAG] = z.getSTag();
			data[i][REPORT_MONATSABRECHNUNG_UESTD100] = z.getBdUestd100();
			data[i][REPORT_MONATSABRECHNUNG_UESTD200] = z.getBdUestd200();
			data[i][REPORT_MONATSABRECHNUNG_UESTD100FREI] = z
					.getBdUestd100Steuerfrei();
			data[i][REPORT_MONATSABRECHNUNG_UESTD50] = z.getBdUestd50();
			data[i][REPORT_MONATSABRECHNUNG_UESTD50TAGEWEISE] = z
					.getBdUestd50Tageweise();
			data[i][REPORT_MONATSABRECHNUNG_UESTD50TAGESWEISEFREI] = z
					.getBdUestd50TageweiseSteuerfrei();
			data[i][REPORT_MONATSABRECHNUNG_MEHRZEIT] = z.getBdMehrstunden();
			data[i][REPORT_MONATSABRECHNUNG_UNTER] = z.getBdUnter();
			data[i][REPORT_MONATSABRECHNUNG_URLAUB] = z.getBdUrlaubStunden();
			data[i][REPORT_MONATSABRECHNUNG_URLAUBTAGEWEISE] = z
					.getBdUrlaubTage();
			data[i][REPORT_MONATSABRECHNUNG_VON] = z.getTVon();
			data[i][REPORT_MONATSABRECHNUNG_ZEITAUSGLEICH] = z.getBdZA();
			data[i][REPORT_MONATSABRECHNUNG_ZUSATZBEZEICHNUNG] = z
					.getSZusatzbezeichnung();
			data[i][REPORT_MONATSABRECHNUNG_REISE] = z.getBdReise();
			data[i][REPORT_MONATSABRECHNUNG_JAHR] = z.getIJahr();
			data[i][REPORT_MONATSABRECHNUNG_MONAT] = z.getIMonat();
			data[i][REPORT_MONATSABRECHNUNG_QUALIFIKATIONSFAKTOR] = z
					.getBdQualifikationsfaktor();
		}

		initJRDS(parameter, ZeiterfassungFac.REPORT_MODUL,
				ZeiterfassungFac.REPORT_MONATSABRECHNUNG,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		monatsabrechnungDto.setJasperPrint(this.getReportPrint());
		monatsabrechnungDto.setData(data.clone());
		monatsabrechnungDto.setParameter(parameter);
		return monatsabrechnungDto;
	}

	private ArrayList<ZeitdatenDto> rundungZugunstenDesUnternehmens(
			ArrayList<ZeitdatenDto> einBlock, ZeitmodelltagDto zeitmodelltagDto) {

		int iRundungBeginn = 0;
		if (zeitmodelltagDto.getIRundungbeginn() != null) {
			iRundungBeginn = zeitmodelltagDto.getIRundungbeginn();
		}

		int iRundungEnde = 0;
		if (zeitmodelltagDto.getIRundungende() != null) {
			iRundungEnde = zeitmodelltagDto.getIRundungende();
		}

		long lRundungBeginn = 60000 * iRundungBeginn;
		long lRundungEnde = 60000 * iRundungEnde;

		if (iRundungBeginn > 0 || iRundungEnde > 0) {

			boolean bRundeSondertaetigkeiten = Helper
					.short2boolean(zeitmodelltagDto
							.getBRundesondertaetigkeiten());

			ZeitdatenDto[] zeitdatenEinesTagesDtos = new ZeitdatenDto[einBlock
					.size()];
			zeitdatenEinesTagesDtos = (ZeitdatenDto[]) einBlock
					.toArray(zeitdatenEinesTagesDtos);
			for (int z = 0; z < zeitdatenEinesTagesDtos.length; z++) {
				ZeitdatenDto eineZeile = zeitdatenEinesTagesDtos[z];
				// Automatikbuchungen auslassen (z.b. das bei
				// Mitternachtssprung nicht gerundet wird)
				if (Helper.short2boolean(eineZeile.getBAutomatikbuchung()) == false) {

					if (lRundungBeginn != 0) {
						if (z == 0) {
							long lZuviel = eineZeile.getTZeit().getTime()
									% lRundungBeginn;
							if (lZuviel != 0) {
								lZuviel = lRundungBeginn - lZuviel;
								eineZeile.setTZeit(new Timestamp(eineZeile
										.getTZeit().getTime() + lZuviel));
							}
						}
					}

					if (z > 0 && z < zeitdatenEinesTagesDtos.length - 1) {
						if (eineZeile.getTaetigkeitIId() != null) {

							if (bRundeSondertaetigkeiten == true) {

								if (z % 2 == 1) {
									if (lRundungEnde != 0) {
										long lZuviel = eineZeile.getTZeit()
												.getTime() % lRundungEnde;
										eineZeile.setTZeit(new Timestamp(
												eineZeile.getTZeit().getTime()
														- lZuviel));
									}
								} else if (z % 2 == 0) {
									if (lRundungBeginn != 0) {
										long lZuviel = eineZeile.getTZeit()
												.getTime() % lRundungBeginn;
										if (lZuviel != 0) {
											lZuviel = lRundungBeginn - lZuviel;
											eineZeile.setTZeit(new Timestamp(
													eineZeile.getTZeit()
															.getTime()
															+ lZuviel));
										}
									}
								}
							}
						}

					}

					if (z == zeitdatenEinesTagesDtos.length - 1) {
						if (lRundungEnde != 0) {
							long lZuviel = eineZeile.getTZeit().getTime()
									% lRundungEnde;
							eineZeile.setTZeit(new Timestamp(eineZeile
									.getTZeit().getTime() - lZuviel));
						}
					}
					einBlock.set(z, eineZeile);
				}
			}

		}
		return einBlock;
	}

	private ArrayList<ZeitdatenDto> fruehestesKommtUndSpaetestesGehtUndMaximaleTagesanwesenheitVerschieben(
			TaetigkeitDto taetigkeitDto_Unter,
			TaetigkeitDto taetigkeitDto_Arzt,
			TaetigkeitDto taetigkeitDto_Behoerde,
			ArrayList<ZeitdatenDto> einBlock,
			ZeitmodelltagDto zeitmodelltagDto,
			Double dMaximaleTagesanwesenheitszeitAufgrundWochenMaximum) {

		java.sql.Time tFruehestesKommt = zeitmodelltagDto.getUBeginn();

		java.sql.Time tSpaetestesGeht = zeitmodelltagDto.getUEnde();

		boolean bVerkehrt = false;

		if (tFruehestesKommt != null && tSpaetestesGeht != null
				&& tFruehestesKommt.after(tSpaetestesGeht)) {
			bVerkehrt = true;
		}

		boolean bFrueh = true;

		ZeitdatenDto[] zeitdatenEinesTagesDtos = new ZeitdatenDto[einBlock
				.size()];
		zeitdatenEinesTagesDtos = (ZeitdatenDto[]) einBlock
				.toArray(zeitdatenEinesTagesDtos);

		// PJ16913
		Timestamp tMaxGeht = null;
		if ((zeitmodelltagDto != null
				&& zeitmodelltagDto.getUErlaubteanwesenheitszeit() != null && zeitmodelltagDto
				.getUErlaubteanwesenheitszeit().getTime() != -3600000)
				|| dMaximaleTagesanwesenheitszeitAufgrundWochenMaximum != null) {
			if (zeitdatenEinesTagesDtos.length > 0) {

				if (tFruehestesKommt != null
						&& tFruehestesKommt.getTime() > -3600000) {
					Calendar tZeit = Calendar.getInstance();
					tZeit.setTimeInMillis(zeitdatenEinesTagesDtos[0].getTZeit()
							.getTime());
					Calendar tKommt = Calendar.getInstance();
					tKommt.setTimeInMillis(tFruehestesKommt.getTime());
					tZeit.set(Calendar.HOUR_OF_DAY,
							tKommt.get(Calendar.HOUR_OF_DAY));
					tZeit.set(Calendar.MINUTE, tKommt.get(Calendar.MINUTE));
					tZeit.set(Calendar.SECOND, 0);
					tZeit.set(Calendar.MILLISECOND, 0);
					java.sql.Timestamp tNeueZeitKommt = new java.sql.Timestamp(
							tZeit.getTimeInMillis());
					if (zeitdatenEinesTagesDtos[0].getTZeit().before(
							tNeueZeitKommt)) {
						zeitdatenEinesTagesDtos[0].setTZeit(tNeueZeitKommt);

					}
				}

				// PJ18724 Wenn Ein Wochenmaximum definiert kann es sein, muss
				// dies hier beruecksichtigt werden
				double dMaxAnwesenheit = 24;
				if (zeitmodelltagDto.getUErlaubteanwesenheitszeit() != null
						&& zeitmodelltagDto.getUErlaubteanwesenheitszeit()
								.getTime() != -3600000) {
					dMaxAnwesenheit = Helper.time2Double(zeitmodelltagDto
							.getUErlaubteanwesenheitszeit());
				}

				if (dMaximaleTagesanwesenheitszeitAufgrundWochenMaximum != null) {

					if (dMaximaleTagesanwesenheitszeitAufgrundWochenMaximum
							.doubleValue() < dMaxAnwesenheit) {
						dMaxAnwesenheit = dMaximaleTagesanwesenheitszeitAufgrundWochenMaximum;
					}

				}

				tMaxGeht = new Timestamp((long) (zeitdatenEinesTagesDtos[0]
						.getTZeit().getTime() + (dMaxAnwesenheit * 3600000)));

				ZeitdatenDto[] zeitdatenDtoFuerVorlaeufigeMaxGehtBerechnung = ZeitdatenDto
						.kopiereArray(zeitdatenEinesTagesDtos);

				// wg. SP3135 auskommentiert
				/*
				 * for (int z = 0; z <
				 * zeitdatenDtoFuerVorlaeufigeMaxGehtBerechnung.length; z++) {
				 * ZeitdatenDto eineZeile =
				 * zeitdatenDtoFuerVorlaeufigeMaxGehtBerechnung[z]; if
				 * (eineZeile.getTZeit().after(tMaxGeht)) {
				 * eineZeile.setTZeit(tMaxGeht); } }
				 */

				double dDauerPaarweiseSondertaetigkeiten = 0;
				try {
					dDauerPaarweiseSondertaetigkeiten = berechnePaarweiserSondertaetigkeiten(
							zeitdatenDtoFuerVorlaeufigeMaxGehtBerechnung,
							taetigkeitDto_Unter.getIId());
					dDauerPaarweiseSondertaetigkeiten += berechnePaarweiserSondertaetigkeiten(
							zeitdatenDtoFuerVorlaeufigeMaxGehtBerechnung,
							taetigkeitDto_Arzt.getIId());
					dDauerPaarweiseSondertaetigkeiten += berechnePaarweiserSondertaetigkeiten(
							zeitdatenDtoFuerVorlaeufigeMaxGehtBerechnung,
							taetigkeitDto_Behoerde.getIId());
				} catch (Exception e) {
					//
				}

				tMaxGeht = new Timestamp(
						(long) (tMaxGeht.getTime() + (dDauerPaarweiseSondertaetigkeiten * 3600000)));
			}

		}

		for (int z = 0; z < zeitdatenEinesTagesDtos.length; z++) {
			ZeitdatenDto eineZeile = zeitdatenEinesTagesDtos[z];

			if (!Helper.short2boolean(eineZeile.getBAutomatikbuchung())) {

				Calendar tZeit = Calendar.getInstance();
				tZeit.setTimeInMillis(eineZeile.getTZeit().getTime());

				if (bVerkehrt == false) {

					if (tFruehestesKommt != null
							&& tFruehestesKommt.getTime() > -3600000) {

						Calendar tKommt = Calendar.getInstance();
						tKommt.setTimeInMillis(tFruehestesKommt.getTime());
						tZeit.set(Calendar.HOUR_OF_DAY,
								tKommt.get(Calendar.HOUR_OF_DAY));
						tZeit.set(Calendar.MINUTE, tKommt.get(Calendar.MINUTE));
						tZeit.set(Calendar.SECOND, 0);
						tZeit.set(Calendar.MILLISECOND, 0);
						java.sql.Timestamp tNeueZeitKommt = new java.sql.Timestamp(
								tZeit.getTimeInMillis());
						if (eineZeile.getTZeit().before(tNeueZeitKommt)) {
							eineZeile.setTZeit(tNeueZeitKommt);
							zeitdatenEinesTagesDtos[z] = eineZeile;
							einBlock.set(z, eineZeile);

						}
					}

					if (tSpaetestesGeht != null
							&& tSpaetestesGeht.getTime() > -3600000) {
						Calendar tGeht = Calendar.getInstance();
						tGeht.setTimeInMillis(tSpaetestesGeht.getTime());
						tZeit.set(Calendar.HOUR_OF_DAY,
								tGeht.get(Calendar.HOUR_OF_DAY));
						tZeit.set(Calendar.MINUTE, tGeht.get(Calendar.MINUTE));
						tZeit.set(Calendar.SECOND, 0);
						tZeit.set(Calendar.MILLISECOND, 0);
						java.sql.Timestamp tNeueZeitGht = new java.sql.Timestamp(
								tZeit.getTimeInMillis());

						if (tMaxGeht != null && tNeueZeitGht.after(tMaxGeht)) {
							tNeueZeitGht = tMaxGeht;
						}

						if (eineZeile.getTZeit().after(tNeueZeitGht)) {
							eineZeile.setTZeit(tNeueZeitGht);
							zeitdatenEinesTagesDtos[z] = eineZeile;
							einBlock.set(z, eineZeile);

						}
					}
				} else {

					// Wenn der erste eintrag nach dem
					// Spaetestem
					// GEHT
					// ist, dann wird alles nach Vorne
					// verschoben
					Calendar tGeht = Calendar.getInstance();
					tGeht.setTimeInMillis(tSpaetestesGeht.getTime());
					tZeit.set(Calendar.HOUR_OF_DAY,
							tGeht.get(Calendar.HOUR_OF_DAY));
					tZeit.set(Calendar.MINUTE, tGeht.get(Calendar.MINUTE));
					tZeit.set(Calendar.SECOND, 0);
					tZeit.set(Calendar.MILLISECOND, 0);
					java.sql.Timestamp tNeueZeitGht = new java.sql.Timestamp(
							tZeit.getTimeInMillis());
					if (z == 0 && eineZeile.getTZeit().after(tNeueZeitGht)) {
						bFrueh = false;
					}

					if (bFrueh) {
						if (tFruehestesKommt != null
								&& tFruehestesKommt.getTime() > -3600000) {
							if (eineZeile.getTZeit().after(tNeueZeitGht)) {
								eineZeile.setTZeit(tNeueZeitGht);
								zeitdatenEinesTagesDtos[z] = eineZeile;
								einBlock.set(z, eineZeile);
							}

						}
					} else {
						if (tFruehestesKommt != null
								&& tFruehestesKommt.getTime() > -3600000) {

							Calendar tKommt = Calendar.getInstance();
							tKommt.setTimeInMillis(tFruehestesKommt.getTime());
							tZeit.set(Calendar.HOUR_OF_DAY,
									tKommt.get(Calendar.HOUR_OF_DAY));
							tZeit.set(Calendar.MINUTE,
									tKommt.get(Calendar.MINUTE));
							tZeit.set(Calendar.SECOND, 0);
							tZeit.set(Calendar.MILLISECOND, 0);
							java.sql.Timestamp tNeueZeitKommt = new java.sql.Timestamp(
									tZeit.getTimeInMillis());
							if (eineZeile.getTZeit().before(tNeueZeitKommt)) {
								eineZeile.setTZeit(tNeueZeitKommt);
								zeitdatenEinesTagesDtos[z] = eineZeile;
								einBlock.set(z, eineZeile);

							}
						}

					}
				}
			}
		}

		return einBlock;
	}

	private GleitzeitsaldoDto getGleitzeitsaldoVormonat(Integer personalIId,
			Integer iJahr, Integer iMonat, java.sql.Date d_datum_bis,
			Date dEintrittsdatum) {
		GleitzeitsaldoDto gleitzeitsaldoDto_Vormonat = new GleitzeitsaldoDto();
		try {

			Calendar tempVormonat = Calendar.getInstance();

			tempVormonat.set(Calendar.YEAR, iJahr.intValue());
			tempVormonat.set(Calendar.MONTH, iMonat.intValue());
			tempVormonat.set(Calendar.DAY_OF_MONTH, 1);

			tempVormonat.set(Calendar.MONTH,
					tempVormonat.get(Calendar.MONTH) - 1);

			gleitzeitsaldoDto_Vormonat = getPersonalFac()
					.gleitzeitsaldoFindByPersonalIIdIJahrIMonat(personalIId,
							new Integer(tempVormonat.get(Calendar.YEAR)),
							new Integer(tempVormonat.get(Calendar.MONTH)));
			Calendar temp = Calendar.getInstance();
			temp.setTimeInMillis(d_datum_bis.getTime());
			int akt_jahr = temp.get(Calendar.YEAR);
			int akt_monat = temp.get(Calendar.MONTH);
			temp.setTimeInMillis(dEintrittsdatum.getTime());

			int eintr_jahr = temp.get(Calendar.YEAR);
			int eintr_monat = temp.get(Calendar.MONTH);
			if (akt_jahr == eintr_jahr && akt_monat == eintr_monat) {
				gleitzeitsaldoDto_Vormonat.setNSaldomehrstunden(new BigDecimal(
						0));
				gleitzeitsaldoDto_Vormonat.setNSaldouestfrei100(new BigDecimal(
						0));
				gleitzeitsaldoDto_Vormonat
						.setNSaldouestfrei50(new BigDecimal(0));
				gleitzeitsaldoDto_Vormonat
						.setNSaldouestpflichtig100(new BigDecimal(0));
				gleitzeitsaldoDto_Vormonat.setNSaldouest200(new BigDecimal(0));
				gleitzeitsaldoDto_Vormonat
						.setNSaldouestpflichtig50(new BigDecimal(0));
				gleitzeitsaldoDto_Vormonat.setNSaldo(new BigDecimal(0));
			}
		} catch (Throwable ex8) {
			gleitzeitsaldoDto_Vormonat.setNSaldomehrstunden(new BigDecimal(0));
			gleitzeitsaldoDto_Vormonat.setNSaldouestfrei100(new BigDecimal(0));
			gleitzeitsaldoDto_Vormonat.setNSaldouestfrei50(new BigDecimal(0));
			gleitzeitsaldoDto_Vormonat
					.setNSaldouestpflichtig100(new BigDecimal(0));
			gleitzeitsaldoDto_Vormonat.setNSaldouest200(new BigDecimal(0));
			gleitzeitsaldoDto_Vormonat.setNSaldouestpflichtig50(new BigDecimal(
					0));
			gleitzeitsaldoDto_Vormonat.setNSaldo(new BigDecimal(0));
		}
		return gleitzeitsaldoDto_Vormonat;
	}

	public PersonalDto[] entferneNichtAnwesendePersonen(Integer iJahrVon,
			Integer iMonatVon, Integer iJahrBis, Integer iMonatBis,
			PersonalDto[] personalDtos, TheClientDto theClientDto) {

		Timestamp tVon = null;
		Timestamp tBis = null;

		if (iJahrVon != null && iMonatVon != null) {
			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR, iJahrVon);
			c.set(Calendar.MONTH, iMonatVon);
			c.set(Calendar.DAY_OF_MONTH, 1);
			tVon = new Timestamp(c.getTimeInMillis());
		}
		if (iJahrBis != null && iMonatBis != null) {
			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR, iJahrBis);
			c.set(Calendar.MONTH, iMonatBis);
			c.set(Calendar.DAY_OF_MONTH,
					c.getActualMaximum(Calendar.DAY_OF_MONTH));
			tBis = new Timestamp(c.getTimeInMillis());
		}

		return entferneNichtAnwesendePersonen(tVon, tBis, personalDtos,
				theClientDto);
	}

	public PersonalDto[] entferneNichtAnwesendePersonen(
			java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			PersonalDto[] personalDtos, TheClientDto theClientDto) {
		ArrayList<PersonalDto> anwesendePersonen = new ArrayList<PersonalDto>();

		DatumsfilterVonBis df = new DatumsfilterVonBis(tVon, tBis);

		for (int i = 0; i < personalDtos.length; i++) {

			String sQuery = "select zeitdaten FROM FLRZeitdaten zeitdaten WHERE zeitdaten.personal_i_id="
					+ personalDtos[i].getIId();

			if (tVon != null) {
				sQuery += " AND zeitdaten.t_zeit>='"
						+ Helper.formatTimestampWithSlashes(df
								.getTimestampVon()) + "'";
			}

			if (df.getTimestampBis() != null) {
				sQuery += " AND zeitdaten.t_zeit<'"
						+ Helper.formatTimestampWithSlashes(df
								.getTimestampBis()) + "'";
			}

			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = factory.openSession();

			org.hibernate.Query inventurliste = session.createQuery(sQuery);
			inventurliste.setMaxResults(1);

			List<?> resultList = inventurliste.list();

			if (resultList.size() > 0) {
				// anwesend
				anwesendePersonen.add(personalDtos[i]);

			} else {
				// abwesend
			}
			session.close();

		}
		return (PersonalDto[]) anwesendePersonen
				.toArray(new PersonalDto[anwesendePersonen.size()]);
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printZeitsaldo(Integer personalIId, Integer iJahrVon,
			Integer iMonatVon, Integer iJahrBis, Integer iMonatBis,
			boolean bisMonatsende, java.sql.Date d_datum_bis,
			TheClientDto theClientDto, Integer iOption,
			Integer iOptionSortierung, Boolean bPlusVersteckte,
			boolean bNurAnwesende) {

		try {

			PersonalDto[] personalDtos = null;

			if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_SELEKTIERTE_PERSON) {

				if (personalIId == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
							new Exception("personalIId == null"));
				}

				personalDtos = new PersonalDto[1];

				personalDtos[0] = getPersonalFac().personalFindByPrimaryKey(
						personalIId, theClientDto);

			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_PERSONEN) {
				personalDtos = getPersonalFac().personalFindByMandantCNr(
						theClientDto.getMandant(), bPlusVersteckte);
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_MEINE_ABTEILUNG) {
				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKey(personalIId, theClientDto);
				personalDtos = getPersonalFac()
						.personalFindAllPersonenMeinerAbteilung(
								personalDto.getKostenstelleIIdAbteilung(),
								theClientDto.getMandant(), bPlusVersteckte);
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_ARBEITER) {
				personalDtos = getPersonalFac()
						.personalFindAllArbeiterEinesMandanten(
								theClientDto.getMandant(), bPlusVersteckte);
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_ANGESTELLTE) {
				personalDtos = getPersonalFac()
						.personalFindAllAngestellteEinesMandanten(
								theClientDto.getMandant(), bPlusVersteckte);
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
						"OPTION NICHT VERFUEGBAR"));
			}

			if (bNurAnwesende) {
				personalDtos = entferneNichtAnwesendePersonen(iJahrVon,
						iMonatVon, iJahrBis, iMonatBis, personalDtos,
						theClientDto);
			}

			// PJ 17420
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
										.kostenstelleFindByPrimaryKey(
												personalDtos[j]
														.getKostenstelleIIdStamm());
								kostenstelle1 = Helper.fitString2Length(
										kstDto.getCNr(), 15, ' ');

							}
							if (personalDtos[j + 1].getKostenstelleIIdStamm() != null) {
								KostenstelleDto kstDto = getSystemFac()
										.kostenstelleFindByPrimaryKey(
												personalDtos[j + 1]
														.getKostenstelleIIdStamm());
								kostenstelle2 = Helper.fitString2Length(
										kstDto.getCNr(), 15, ' ');

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
										.kostenstelleFindByPrimaryKey(
												personalDtos[j]
														.getKostenstelleIIdAbteilung());
								abteilung1 = Helper.fitString2Length(
										kstDto.getCNr(), 15, ' ');

							} else {
								abteilung1 = "               ";
							}
							if (personalDtos[j + 1]
									.getKostenstelleIIdAbteilung() != null) {
								KostenstelleDto kstDto = getSystemFac()
										.kostenstelleFindByPrimaryKey(
												personalDtos[j + 1]
														.getKostenstelleIIdAbteilung());
								abteilung2 = Helper.fitString2Length(
										kstDto.getCNr(), 15, ' ');

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

						PartnerDto p1Dto = getPartnerFac()
								.partnerFindByPrimaryKey(
										personalDtos[j].getPartnerIId(),
										theClientDto);
						personalDtos[j].setPartnerDto(p1Dto);
						PartnerDto p2Dto = getPartnerFac()
								.partnerFindByPrimaryKey(
										personalDtos[j + 1].getPartnerIId(),
										theClientDto);
						vergleich1 += Helper.fitString2Length(
								p1Dto.getCName1nachnamefirmazeile1(), 80, ' ');
						vergleich2 += Helper.fitString2Length(
								p2Dto.getCName1nachnamefirmazeile1(), 80, ' ');

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

			ArrayList alDaten = new ArrayList();
			for (int i = 0; i < personalDtos.length; i++) {
				Calendar cAktuell = Calendar.getInstance();
				cAktuell.set(Calendar.DAY_OF_MONTH, 1);
				cAktuell.set(Calendar.MONTH, iMonatVon);
				cAktuell.set(Calendar.YEAR, iJahrVon);
				cAktuell.set(Calendar.HOUR_OF_DAY, 0);
				cAktuell.set(Calendar.MINUTE, 0);
				cAktuell.set(Calendar.SECOND, 0);
				cAktuell.set(Calendar.MILLISECOND, 0);

				Calendar cEnde = Calendar.getInstance();

				if (iMonatBis == null) {
					cEnde = (Calendar) cAktuell.clone();

				} else {
					cEnde.set(Calendar.MONTH, iMonatBis);
					cEnde.set(Calendar.YEAR, iJahrBis);
					cEnde.set(Calendar.DAY_OF_MONTH,
							cEnde.getActualMaximum(Calendar.DAY_OF_MONTH));
				}

				while (cAktuell.before(cEnde) || cAktuell.equals(cEnde)) {

					int iJahr = cAktuell.get(Calendar.YEAR);
					int iMonat = cAktuell.get(Calendar.MONTH);

					try {
						MonatsabrechnungDto moaDto = erstelleMonatsAbrechnung(
								personalDtos[i].getIId(), iJahr, iMonat,
								bisMonatsende, d_datum_bis, theClientDto,
								false, iOptionSortierung);

						HashMap parameter = moaDto.getParameter();

						Object[] zeile = new Object[ZeiterfassungFacBean.REPORT_ZEITSALDO_ANZAHL_ZEILEN];

						BigDecimal bdIstGesamt = new BigDecimal(0);
						BigDecimal bdSollGesamt = new BigDecimal(0);
						BigDecimal bdFtg = new BigDecimal(0);
						BigDecimal bdArzt = new BigDecimal(0);
						BigDecimal bdBehoerde = new BigDecimal(0);
						BigDecimal bdKrank = new BigDecimal(0);
						BigDecimal bdKindKrank = new BigDecimal(0);
						BigDecimal bdUrlaub = new BigDecimal(0);
						BigDecimal bdReise = new BigDecimal(0);
						BigDecimal bdSonstBez = new BigDecimal(0);
						BigDecimal bdSonstNBez = new BigDecimal(0);
						Date dEintrittsdatum = new Date();
						BigDecimal bdUrlaubVorjahr = new BigDecimal(0);

						Object[][] datenMoa = moaDto.getData();
						for (int j = 0; j < datenMoa.length; j++) {
							Object[] zeileMoa = datenMoa[j];
							bdIstGesamt = bdIstGesamt
									.add((BigDecimal) zeileMoa[ZeiterfassungFacBean.REPORT_MONATSABRECHNUNG_IST]);
							bdSollGesamt = bdSollGesamt
									.add((BigDecimal) zeileMoa[ZeiterfassungFacBean.REPORT_MONATSABRECHNUNG_SOLL]);
							bdFtg = bdFtg
									.add((BigDecimal) zeileMoa[ZeiterfassungFacBean.REPORT_MONATSABRECHNUNG_FEIERTAG]);
							bdArzt = bdArzt
									.add((BigDecimal) zeileMoa[ZeiterfassungFacBean.REPORT_MONATSABRECHNUNG_ARZT]);
							bdBehoerde = bdBehoerde
									.add((BigDecimal) zeileMoa[ZeiterfassungFacBean.REPORT_MONATSABRECHNUNG_BEHOERDE]);
							bdKrank = bdKrank
									.add((BigDecimal) zeileMoa[ZeiterfassungFacBean.REPORT_MONATSABRECHNUNG_KRANK]);
							bdKindKrank = bdKindKrank
									.add((BigDecimal) zeileMoa[ZeiterfassungFacBean.REPORT_MONATSABRECHNUNG_KINDKRANK]);
							bdUrlaub = bdUrlaub
									.add((BigDecimal) zeileMoa[ZeiterfassungFacBean.REPORT_MONATSABRECHNUNG_URLAUB]);

							if (zeileMoa[ZeiterfassungFacBean.REPORT_MONATSABRECHNUNG_REISE] != null) {
								bdReise = bdReise
										.add((BigDecimal) zeileMoa[ZeiterfassungFacBean.REPORT_MONATSABRECHNUNG_REISE]);
							}

							bdSonstBez = bdSonstBez
									.add((BigDecimal) zeileMoa[ZeiterfassungFacBean.REPORT_MONATSABRECHNUNG_SONSTIGE_BEZAHLTETAETIGKEITEN]);
							bdSonstNBez = bdSonstNBez
									.add((BigDecimal) zeileMoa[ZeiterfassungFacBean.REPORT_MONATSABRECHNUNG_SONSTIGE_UNBEZAHLTETAETIGKEITEN]);

						}
						if (parameter.containsKey("P_PERSONAL")) {
							zeile[REPORT_ZEITSALDO_PERSON] = parameter
									.get("P_PERSONAL");
							zeile[REPORT_ZEITSALDO_PERSONALNUMMER] = personalDtos[i]
									.getCPersonalnr();
						}

						zeile[REPORT_ZEITSALDO_IST] = bdIstGesamt;
						zeile[REPORT_ZEITSALDO_SOLL] = bdSollGesamt;
						zeile[REPORT_ZEITSALDO_FTGSOLL] = parameter
								.get("P_FEIERTAGSOLL");
						zeile[REPORT_ZEITSALDO_FTG] = bdFtg;
						zeile[REPORT_ZEITSALDO_ARZT] = bdArzt;
						zeile[REPORT_ZEITSALDO_BEHOERDE] = bdBehoerde;
						zeile[REPORT_ZEITSALDO_KRANK] = bdKrank;
						zeile[REPORT_ZEITSALDO_KIND_KRANK] = bdKindKrank;
						zeile[REPORT_ZEITSALDO_URLAUBSTD] = bdUrlaub;
						zeile[REPORT_ZEITSALDO_REISE] = bdReise;
						zeile[REPORT_ZEITSALDO_SONSTIGEBEZAHLT] = bdSonstBez;
						zeile[REPORT_ZEITSALDO_SONSTIGENICHTBEZAHLT] = bdSonstNBez;

						// Urlaubstunden
						if (parameter
								.containsKey("P_AKTUELLERURLAUBSANSPRUCHSTUNDEN")) {
							zeile[REPORT_ZEITSALDO_URLAUBSTUNDEN_URLAUBAKUTELL] = parameter
									.get("P_AKTUELLERURLAUBSANSPRUCHSTUNDEN");
						}
						if (parameter
								.containsKey("P_AKTUELLERURLAUBSVERBRAUCHTSTUNDEN")) {
							zeile[REPORT_ZEITSALDO_URLAUBSTUNDEN_URLAUBAKUTELLVERBRAUCHT] = parameter
									.get("P_AKTUELLERURLAUBSVERBRAUCHTSTUNDEN");
						}
						if (parameter
								.containsKey("P_ALTERURLAUBSANSPRUCHSTUNDEN")) {
							zeile[REPORT_ZEITSALDO_URLAUBSTUNDEN_URLAUBREST] = parameter
									.get("P_ALTERURLAUBSANSPRUCHSTUNDEN");
						}
						if (parameter
								.containsKey("P_ALTERURLAUBSVERBRAUCHTSTUNDEN")) {
							zeile[REPORT_ZEITSALDO_URLAUBSTUNDEN_URLAUBRESTVERBRAUCHT] = parameter
									.get("P_ALTERURLAUBSVERBRAUCHTSTUNDEN");
						}
						if (parameter.containsKey("P_GEPLANTERURLAUBSTUNDEN")) {
							zeile[REPORT_ZEITSALDO_URLAUBSTUNDEN_URLAUB_GEPLANT] = parameter
									.get("P_GEPLANTERURLAUBSTUNDEN");
						}
						if (parameter
								.containsKey("P_VERFUEGBARERURLAUBSTUNDEN")) {
							zeile[REPORT_ZEITSALDO_URLAUBSTUNDEN_URLAUB_VERFUGBAR] = parameter
									.get("P_VERFUEGBARERURLAUBSTUNDEN");
						}
						if (parameter.containsKey("P_ALIQUOTERURLAUBSTUNDEN")) {
							zeile[REPORT_ZEITSALDO_URLAUBSTUNDEN_ALIQUOT] = parameter
									.get("P_ALIQUOTERURLAUBSTUNDEN");
						}
						// Urlaubtage
						if (parameter
								.containsKey("P_AKTUELLERURLAUBSANSPRUCHTAGE")) {
							zeile[REPORT_ZEITSALDO_URLAUBTAGE_URLAUBAKTUELL] = parameter
									.get("P_AKTUELLERURLAUBSANSPRUCHTAGE");
						}
						// Urlaubtage Vorjahr
						if (parameter
								.containsKey("P_VORJAHRVERFUEGBARERURLAUBTAGE")) {
							zeile[REPORT_ZEITSALDO_URLAUBTAGE_URLAUBVORJAHR] = parameter
									.get("P_VORJAHRVERFUEGBARERURLAUBTAGE");
						}

						if (parameter
								.containsKey("P_AKTUELLERURLAUBSVERBRAUCHTTAGE")) {
							zeile[REPORT_ZEITSALDO_URLAUBTAGE_URLAUBAKTUELLVERBRAUCHT] = parameter
									.get("P_AKTUELLERURLAUBSVERBRAUCHTTAGE");
						}
						if (parameter.containsKey("P_ALTERURLAUBSANSPRUCHTAGE")) {
							zeile[REPORT_ZEITSALDO_URLAUBTAGE_URLAUBREST] = parameter
									.get("P_ALTERURLAUBSANSPRUCHTAGE");
						}
						if (parameter
								.containsKey("P_ALTERURLAUBSVERBRAUCHTTAGE")) {
							zeile[REPORT_ZEITSALDO_URLAUBTAGE_URLAUBRESTVERBRAUCHT] = parameter
									.get("P_ALTERURLAUBSVERBRAUCHTTAGE");
						}
						if (parameter.containsKey("P_GEPLANTERURLAUBTAGE")) {
							zeile[REPORT_ZEITSALDO_URLAUBTAGE_URLAUB_GEPLANT] = parameter
									.get("P_GEPLANTERURLAUBTAGE");
						}
						if (parameter.containsKey("P_VERFUEGBARERURLAUBTAGE")) {
							zeile[REPORT_ZEITSALDO_URLAUBTAGE_URLAUB_VERFUGBAR] = parameter
									.get("P_VERFUEGBARERURLAUBTAGE");
						}
						if (parameter.containsKey("P_ALIQUOTERURLAUBTAGE")) {
							zeile[REPORT_ZEITSALDO_URLAUBTAGE_ALIQUOT] = parameter
									.get("P_ALIQUOTERURLAUBTAGE");
						}

						// UESTD
						if (parameter
								.containsKey("P_UESTD100PFLICHTIGVORHANDEN")) {
							zeile[REPORT_ZEITSALDO_UESTDSALDO100] = parameter
									.get("P_UESTD100PFLICHTIGVORHANDEN");
						}
						if (parameter.containsKey("P_UESTD100FREIVORHANDEN")) {
							zeile[REPORT_ZEITSALDO_UESTDSALDO100STF] = parameter
									.get("P_UESTD100FREIVORHANDEN");
						}

						if (parameter.containsKey("P_UESTD200VORHANDEN")) {
							zeile[REPORT_ZEITSALDO_UESTDSALDO200] = parameter
									.get("P_UESTD200VORHANDEN");
						}

						if (parameter
								.containsKey("P_UESTD50PFLICHTIGVORHANDEN")) {
							zeile[REPORT_ZEITSALDO_UESTDSALDO50] = parameter
									.get("P_UESTD50PFLICHTIGVORHANDEN");
						}
						if (parameter.containsKey("P_UESTD50FREIVORHANDEN")) {
							zeile[REPORT_ZEITSALDO_UESTDSALDO50STF] = parameter
									.get("P_UESTD50FREIVORHANDEN");
						}
						if (parameter.containsKey("P_MEHRSTUNDENVORHANDEN")) {
							zeile[REPORT_ZEITSALDO_UESTDSALDOMEHRSTD] = parameter
									.get("P_MEHRSTUNDENVORHANDEN");
						}

						// Ausbezahlt

						if (parameter.containsKey("P_AUSBEZAHLTMEHRSTD")) {
							zeile[REPORT_ZEITSALDO_AUSBEZAHLTMEHRSTD] = parameter
									.get("P_AUSBEZAHLTMEHRSTD");
						}
						if (parameter.containsKey("P_AUSBEZAHLTUESTD50")) {
							zeile[REPORT_ZEITSALDO_AUSBEZAHLT50] = parameter
									.get("P_AUSBEZAHLTUESTD50");
						}
						if (parameter.containsKey("P_AUSBEZAHLTUESTD50STF")) {
							zeile[REPORT_ZEITSALDO_AUSBEZAHLT50STF] = parameter
									.get("P_AUSBEZAHLTUESTD50STF");
						}
						if (parameter.containsKey("P_AUSBEZAHLTUESTD100")) {
							zeile[REPORT_ZEITSALDO_AUSBEZAHLT100] = parameter
									.get("P_AUSBEZAHLTUESTD100");
						}

						if (parameter.containsKey("P_AUSBEZAHLTUESTD100STF")) {
							zeile[REPORT_ZEITSALDO_AUSBEZAHLT100STF] = parameter
									.get("P_AUSBEZAHLTUESTD100STF");
						}
						if (parameter.containsKey("P_AUSBEZAHLTNORMALSTD")) {
							zeile[REPORT_ZEITSALDO_AUSBEZAHLTNORMALSTD] = parameter
									.get("P_AUSBEZAHLTNORMALSTD");
						}
						if (parameter.containsKey("P_AUSBEZAHLTGUTSTD")) {
							zeile[REPORT_ZEITSALDO_AUSBEZAHLTGUTSTD] = parameter
									.get("P_AUSBEZAHLTGUTSTD");
						}
						if (parameter.containsKey("P_AUSBEZAHLTUESTD200")) {
							zeile[REPORT_ZEITSALDO_AUSBEZAHLT200] = parameter
									.get("P_AUSBEZAHLTUESTD200");
						}
						if (parameter.containsKey("P_QUALIPRAEMIE")) {
							zeile[REPORT_ZEITSALDO_QUALIPRAEMIE] = parameter
									.get("P_QUALIPRAEMIE");
						}

						Calendar c = Calendar.getInstance();
						java.sql.Date d_heute = new java.sql.Date(c.getTime()
								.getTime());

						// Hole letztes Eintrittsdatum
						try {

							EintrittaustrittDto eaDto = getPersonalFac()
									.eintrittaustrittFindLetztenEintrittBisDatum(
											personalDtos[i].getIId(),
											new Timestamp(d_heute.getTime()));

							if (eaDto != null) {
								dEintrittsdatum = eaDto.getTEintritt();
							}

						} catch (RemoteException ex3) {
							throwEJBExceptionLPRespectOld(ex3);
						}

						// Vormonatsgleitzeitsaldo berechnen
						GleitzeitsaldoDto gleitzeitsaldoDto_Vormonat = getGleitzeitsaldoVormonat(
								personalDtos[i].getIId(), iJahr, iMonat,
								d_heute, dEintrittsdatum);
						zeile[REPORT_ZEITSALDO_GLEITZEITSALDOVORMONAT] = Helper
								.rundeKaufmaennisch(
										gleitzeitsaldoDto_Vormonat.getNSaldo(),
										2);

						// Gleitzeitsaldo des aktuellen Monats holen

						GleitzeitsaldoDto gleitzeitsaldoDto_Aktuell;
						try {
							gleitzeitsaldoDto_Aktuell = getPersonalFac()
									.gleitzeitsaldoFindByPersonalIIdIJahrIMonat(
											personalDtos[i].getIId(), iJahr,
											iMonat);
							if (gleitzeitsaldoDto_Aktuell != null) {
								zeile[REPORT_ZEITSALDO_GLEITZEITSALDOAKTUELLERMONAT] = gleitzeitsaldoDto_Aktuell
										.getNSaldo();
							}

						} catch (EJBExceptionLP e) {
							// Keinen gefunden;
						}

						zeile[REPORT_ZEITSALDO_MONAT] = new DateFormatSymbols(
								theClientDto.getLocUi()).getMonths()[iMonat]
								+ " " + iJahr;

						alDaten.add(zeile);

					} catch (EJBExceptionLP ex1) {

						if (ex1.getCode() == EJBExceptionLP.FEHLER_PERSONAL_FEHLER_BEI_EINTRITTSDATUM
								&& iOption.intValue() != ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_SELEKTIERTE_PERSON) {
							// NICHTS - PERSON WIRD AUSGELASSEN
						} else {
							throw new EJBExceptionLP(ex1);
						}

					}

					cAktuell.set(Calendar.DAY_OF_MONTH, 1);
					cAktuell.set(Calendar.MONTH,
							cAktuell.get(Calendar.MONTH) + 1);

				}
			}
			HashMap<String, Object> parameter = new HashMap<String, Object>();
			parameter
					.put("P_MONATVON",
							new DateFormatSymbols(theClientDto.getLocUi())
									.getMonths()[iMonatVon] + " " + iJahrVon);

			if (iMonatBis != null && iJahrBis != null) {
				parameter.put(
						"P_MONATBIS",
						new DateFormatSymbols(theClientDto.getLocUi())
								.getMonths()[iMonatBis] + " " + iJahrBis);

			}

			parameter.put(
					"P_SORTIERUNG",
					getParameterSortierungZeitauswertungen(iOptionSortierung,
							theClientDto));

			parameter.put("P_NUR_ANWESENDE", new Boolean(bNurAnwesende));

			data = new Object[alDaten.size()][32];
			data = (Object[][]) alDaten.toArray(data);

			index = -1;
			sAktuellerReport = ZeiterfassungFac.REPORT_ZEITSALDO;

			initJRDS(parameter, ZeiterfassungFac.REPORT_MODUL,
					ZeiterfassungFac.REPORT_ZEITSALDO,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
			return getReportPrint();

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
	}

	public String istBelegGeradeInBearbeitung(String belegartCNr,
			Integer belegartIId, TheClientDto theClientDto) {
		String s = null;

		Integer taetigkeitIId_Ende = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_ENDE, theClientDto).getIId();
		// Hole id der Taetigkeit GEHT
		Integer taetigkeitIId_Geht = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_GEHT, theClientDto).getIId();

		String sQueryBelegzeiten = "SELECT z FROM FLRZeitdaten z WHERE z.c_belegartnr='"
				+ belegartCNr + "' AND z.i_belegartid=" + belegartIId;

		sQueryBelegzeiten += " AND z.t_zeit>='"
				+ Helper.formatTimestampWithSlashes(Helper
						.cutTimestamp(new Timestamp(System.currentTimeMillis())))
				+ "'";

		sQueryBelegzeiten += " AND z.t_zeit<='"
				+ Helper.formatTimestampWithSlashes(new Timestamp(System
						.currentTimeMillis())) + "'";

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Query zeiten = session.createQuery(sQueryBelegzeiten);

		List<?> resultList = zeiten.list();

		Iterator<?> resultListAuftraege = resultList.iterator();

		ArrayList<ZeitdatenDtoBelegzeiten[]> alAuftragszeit = new ArrayList<ZeitdatenDtoBelegzeiten[]>();

		HashMap hmPersonalIIds = new HashMap();

		belegdaten: while (resultListAuftraege.hasNext()) {
			FLRZeitdaten auftragszeit = (FLRZeitdaten) resultListAuftraege
					.next();

			if (!hmPersonalIIds.containsKey(auftragszeit.getPersonal_i_id())) {
				// Pruefen, ob danach noch ein ENDE GEHT oder eine andere
				// Belegbuchung ist
				ZeitdatenDto[] dtos = zeitdatenFindZeitdatenEinesTagesUndEinerPerson(
						auftragszeit.getPersonal_i_id(), new Timestamp(
								auftragszeit.getT_zeit().getTime()),
						new Timestamp(System.currentTimeMillis()));

				for (int i = 1; i < dtos.length; i++) {
					ZeitdatenDto dto = dtos[i];

					if (dto.getTaetigkeitIId() != null) {
						if (dto.getTaetigkeitIId().equals(taetigkeitIId_Ende)
								|| dto.getTaetigkeitIId().equals(
										taetigkeitIId_Geht)) {
							continue belegdaten;
						}

					} else if (dto.getIBelegartid() != null) {
						continue belegdaten;
					}

				}
				hmPersonalIIds.put(auftragszeit.getPersonal_i_id(), "");
			}

		}

		Iterator it = hmPersonalIIds.keySet().iterator();

		byte[] CRLFAscii = { 13, 10 };

		while (it.hasNext()) {
			if (s == null) {
				s = new String(CRLFAscii);
			}
			Integer personalIId = (Integer) it.next();
			PersonalDto personalDto = getPersonalFac()
					.personalFindByPrimaryKey(personalIId, theClientDto);
			s += personalDto.formatAnrede() + new String(CRLFAscii);
		}

		return s;
	}

	public java.sql.Timestamp[] sindIstZeitenVorhandenWennUrlaubGebuchtWird(
			SonderzeitenDto sonderzeitenDto, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, TheClientDto theClientDto) {
		if (sonderzeitenDto == null || tVon == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("sonderzeitenDto == null || tVon == null"));
		}

		ArrayList auszulassen = new ArrayList();

		tVon = Helper.cutTimestamp(tVon);
		tBis = Helper.cutTimestamp(tBis);

		java.sql.Timestamp d_datum = null;

		Integer iPk = null;

		if (tBis != null) {
			if (tVon.equals(tBis)) {
				d_datum = tVon;
			}
		} else {
			tBis = tVon;
		}

		Calendar cTag = Calendar.getInstance();

		cTag.setTimeInMillis(tVon.getTime());

		while (cTag.getTimeInMillis() <= tBis.getTime()) {
			d_datum = new java.sql.Timestamp(cTag.getTimeInMillis());
			sonderzeitenDto.setTDatum(d_datum);
			try {
				if (istUrlaubstagZuDatumNoetig(
						sonderzeitenDto.getPersonalIId(), d_datum, theClientDto)) {

					// Naechster Tag fuer suche

					Calendar cTemp = Calendar.getInstance();
					cTemp.setTimeInMillis(cTag.getTimeInMillis());
					cTemp.set(Calendar.DAY_OF_MONTH,
							cTemp.get(Calendar.DAY_OF_MONTH) + 1);

					// Sind Buchungen vorhanden
					ZeitdatenDto[] dtos = zeitdatenFindZeitdatenEinesTagesUndEinerPerson(
							sonderzeitenDto.getPersonalIId(), d_datum,
							new java.sql.Timestamp(cTemp.getTimeInMillis()));
					if (dtos != null && dtos.length > 0) {
						auszulassen.add(d_datum);
					}
				}

			} catch (Exception e) {
				// Naechster Tag
			}
			cTag.set(Calendar.DAY_OF_MONTH, cTag.get(Calendar.DAY_OF_MONTH) + 1);
		}

		java.sql.Timestamp[] ta = new java.sql.Timestamp[auszulassen.size()];

		return (java.sql.Timestamp[]) auszulassen.toArray(ta);
	}

	public Integer createSonderzeitenVonBis(SonderzeitenDto sonderzeitenDto,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			java.sql.Timestamp[] auslassen, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (sonderzeitenDto == null || tVon == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("sonderzeitenDto == null || tVon == null"));
		}

		tVon = Helper.cutTimestamp(tVon);
		tBis = Helper.cutTimestamp(tBis);

		java.sql.Timestamp d_datum = null;

		Integer iPk = null;

		if (tBis != null) {
			if (tVon.equals(tBis)) {
				d_datum = tVon;
			}
		} else {
			tBis = tVon;
		}

		Calendar cTag = Calendar.getInstance();

		cTag.setTimeInMillis(tVon.getTime());

		while (cTag.getTimeInMillis() <= tBis.getTime()) {
			d_datum = new java.sql.Timestamp(cTag.getTimeInMillis());
			sonderzeitenDto.setTDatum(d_datum);
			try {
				if (istUrlaubstagZuDatumNoetig(
						sonderzeitenDto.getPersonalIId(), d_datum, theClientDto)) {
					boolean bAuslassen = false;
					if (auslassen != null) {
						for (int i = 0; i < auslassen.length; i++) {
							if (auslassen[i].equals(d_datum)) {
								bAuslassen = true;
							}
						}
					}
					if (bAuslassen == false) {
						iPk = createSonderzeiten(sonderzeitenDto, theClientDto);
					}
				}

			} catch (Exception e) {
				// Naechster Tag
			}
			cTag.set(Calendar.DAY_OF_MONTH, cTag.get(Calendar.DAY_OF_MONTH) + 1);
		}

		return iPk;
	}

	public Integer createSonderzeiten(SonderzeitenDto sonderzeitenDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (sonderzeitenDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("sonderzeitenDto == null"));
		}

		if (sonderzeitenDto.getBTag() == null
				|| sonderzeitenDto.getTDatum() == null
				|| sonderzeitenDto.getPersonalIId() == null
				|| sonderzeitenDto.getTaetigkeitIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"sonderzeitenDto.getBTag() == null || sonderzeitenDto.getDDatum() == null || sonderzeitenDto.getPersonalIId() == null || sonderzeitenDto.getTaetigkeitIId() == null"));
		}
		// try {
		Query query = em
				.createNamedQuery("SonderzeitenfindByPersonalIIdTDatumTaetigkeitIId");
		query.setParameter(1, sonderzeitenDto.getPersonalIId());
		query.setParameter(2, sonderzeitenDto.getTDatum());
		query.setParameter(3, sonderzeitenDto.getTaetigkeitIId());
		// @todo getSingleResult oder getResultList ?
		try {
			Sonderzeiten dopelt = (Sonderzeiten) query.getSingleResult();

			PersonalDto pDto = getPersonalFac().personalFindByPrimaryKey(
					sonderzeitenDto.getPersonalIId(), theClientDto);

			System.out.println("PERS_SONDERZEITEN.UK: pers:"
					+ sonderzeitenDto.getPersonalIId() + " datum: "
					+ sonderzeitenDto.getTDatum().toString() + " taetigkeit: "
					+ sonderzeitenDto.getTaetigkeitIId());
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_SONDERZEITEN.UK PersonalNr.:"
							+ pDto.getCPersonalnr() + " " + pDto.formatAnrede()
							+ " datum: "
							+ sonderzeitenDto.getTDatum().toString()
							+ " taetigkeit: "
							+ sonderzeitenDto.getTaetigkeitIId()));
		} catch (NoResultException e1) {
			// nix
		}
		// SP3289
		bringeFehlerWennZeitabschlussvorhanden(
				sonderzeitenDto.getPersonalIId(), sonderzeitenDto.getTDatum(),
				theClientDto);

		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_SONDERZEITEN);
		sonderzeitenDto.setIId(pk);
		sonderzeitenDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		sonderzeitenDto.setTAendern(new java.sql.Timestamp(System
				.currentTimeMillis()));
		try {
			Sonderzeiten sonderzeiten = new Sonderzeiten(
					sonderzeitenDto.getIId(), sonderzeitenDto.getPersonalIId(),
					sonderzeitenDto.getTDatum(),
					sonderzeitenDto.getTaetigkeitIId(),
					sonderzeitenDto.getBTag(), sonderzeitenDto.getBHalbtag(),
					sonderzeitenDto.getPersonalIIdAendern());
			em.persist(sonderzeiten);
			em.flush();

			if (Helper.short2boolean(sonderzeitenDto.getBTag()) == true
					|| Helper.short2boolean(sonderzeitenDto.getBHalbtag()) == true) {
				sonderzeitenDto.setUStunden(null);
			}

			setSonderzeitenFromSonderzeitenDto(sonderzeiten, sonderzeitenDto);
			return sonderzeitenDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

	}

	public void removeSonderzeiten(SonderzeitenDto sonderzeitenDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (sonderzeitenDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("sonderzeitenDto == null"));
		}
		if (sonderzeitenDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception("sonderzeitenDto.getIId() == null"));
		}
		try {
			Sonderzeiten sonderzeiten = em.find(Sonderzeiten.class,
					sonderzeitenDto.getIId());
			if (sonderzeiten == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(sonderzeiten);
			em.flush();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void removeMaschinenzeitdaten(
			MaschinenzeitdatenDto maschinenzeitdatenDto) {

		try {
			Maschinenzeitdaten maschinenzeitdaten = em.find(
					Maschinenzeitdaten.class, maschinenzeitdatenDto.getIId());
			if (maschinenzeitdaten == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(maschinenzeitdaten);
			em.flush();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void updateSonderzeiten(SonderzeitenDto sonderzeitenDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (sonderzeitenDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("sonderzeitenDto == null"));
		}
		if (sonderzeitenDto.getIId() == null
				|| sonderzeitenDto.getPersonalIId() == null
				|| sonderzeitenDto.getTaetigkeitIId() == null
				|| sonderzeitenDto.getBTag() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"sonderzeitenDto.getIId() == null || sonderzeitenDto.getPersonalIId() == null || sonderzeitenDto.getTaetigkeitIId() == null || sonderzeitenDto.getBTag() == null"));
		}
		Integer iId = sonderzeitenDto.getIId();
		// try {
		Sonderzeiten sonderzeiten = em.find(Sonderzeiten.class, iId);
		if (sonderzeiten == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		// SP3289
		bringeFehlerWennZeitabschlussvorhanden(
				sonderzeitenDto.getPersonalIId(), sonderzeitenDto.getTDatum(),
				theClientDto);
		try {
			Query query = em
					.createNamedQuery("SonderzeitenfindByPersonalIIdTDatumTaetigkeitIId");
			query.setParameter(1, sonderzeitenDto.getPersonalIId());
			query.setParameter(2, sonderzeitenDto.getTDatum());
			query.setParameter(3, sonderzeitenDto.getTaetigkeitIId());
			Integer iIdVorhanden = ((Sonderzeiten) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_SONDERZEITEN.UK"));
			}
		} catch (NoResultException ex) {
			// nix
		}
		sonderzeitenDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		sonderzeitenDto.setTAendern(new java.sql.Timestamp(System
				.currentTimeMillis()));
		if (Helper.short2boolean(sonderzeitenDto.getBTag()) == true
				|| Helper.short2boolean(sonderzeitenDto.getBHalbtag()) == true) {
			sonderzeitenDto.setUStunden(null);
		}

		setSonderzeitenFromSonderzeitenDto(sonderzeiten, sonderzeitenDto);
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);

		// }

	}

	public SonderzeitenDto sonderzeitenFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Sonderzeiten sonderzeiten = em.find(Sonderzeiten.class, iId);
		if (sonderzeiten == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		SonderzeitenDto dto = assembleSonderzeitenDto(sonderzeiten);
		dto.setTaetigkeitDto(taetigkeitFindByPrimaryKey(dto.getTaetigkeitIId(),
				theClientDto));

		return dto;
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public SonderzeitenDto[] sonderzeitenFindByPersonalIIdDDatum(
			Integer personalIId, Timestamp dDatum) throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("SonderzeitenfindByPersonalIIdTDatum");
		query.setParameter(1, personalIId);
		query.setParameter(2, Helper.cutTimestamp(dDatum));
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		return assembleSonderzeitenDtos(cl);
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setSonderzeitenFromSonderzeitenDto(Sonderzeiten sonderzeiten,
			SonderzeitenDto sonderzeitenDto) {
		sonderzeiten.setPersonalIId(sonderzeitenDto.getPersonalIId());
		sonderzeiten.setTDatum(sonderzeitenDto.getTDatum());
		sonderzeiten.setTaetigkeitIId(sonderzeitenDto.getTaetigkeitIId());
		sonderzeiten.setBTag(sonderzeitenDto.getBTag());
		sonderzeiten.setBHalbtag(sonderzeitenDto.getBHalbtag());
		sonderzeiten.setUStunden(sonderzeitenDto.getUStunden());
		sonderzeiten.setPersonalIIdAendern(sonderzeitenDto
				.getPersonalIIdAendern());
		sonderzeiten.setTAendern(sonderzeitenDto.getTAendern());
		em.merge(sonderzeiten);
		em.flush();
	}

	private SonderzeitenDto assembleSonderzeitenDto(Sonderzeiten sonderzeiten) {
		return SonderzeitenDtoAssembler.createDto(sonderzeiten);
	}

	private SonderzeitenDto[] assembleSonderzeitenDtos(
			Collection<?> sonderzeitens) {
		List<SonderzeitenDto> list = new ArrayList<SonderzeitenDto>();
		if (sonderzeitens != null) {
			Iterator<?> iterator = sonderzeitens.iterator();
			while (iterator.hasNext()) {
				Sonderzeiten sonderzeiten = (Sonderzeiten) iterator.next();
				list.add(assembleSonderzeitenDto(sonderzeiten));
			}
		}
		SonderzeitenDto[] returnArray = new SonderzeitenDto[list.size()];
		return (SonderzeitenDto[]) list.toArray(returnArray);
	}

	public Integer createZeitmodelltagpause(
			ZeitmodelltagpauseDto zeitmodelltagpauseDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (zeitmodelltagpauseDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zeitmodelltagpauseDto == null"));
		}
		if (zeitmodelltagpauseDto.getZeitmodelltagIId() == null
				|| zeitmodelltagpauseDto.getUBeginn() == null
				|| zeitmodelltagpauseDto.getUEnde() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zeitmodelltagpauseDto.getZeitmodelltagIId() == null || zeitmodelltagpauseDto.getUBeginn() == null || zeitmodelltagpauseDto.getUEnde() == null"));
		}

		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ZEITMODELLTAGPAUSE);
		zeitmodelltagpauseDto.setIId(pk);
		zeitmodelltagpauseDto.setPersonalIIdAendern(theClientDto
				.getIDPersonal());
		zeitmodelltagpauseDto.setTAendern(new Timestamp(System
				.currentTimeMillis()));
		try {
			Zeitmodelltagpause zeitmodelltagpause = new Zeitmodelltagpause(
					zeitmodelltagpauseDto.getIId(),
					zeitmodelltagpauseDto.getZeitmodelltagIId(),
					zeitmodelltagpauseDto.getPersonalIIdAendern(),
					zeitmodelltagpauseDto.getUBeginn(),
					zeitmodelltagpauseDto.getUEnde());
			em.persist(zeitmodelltagpause);
			em.flush();
			setZeitmodelltagpauseFromZeitmodelltagpauseDto(zeitmodelltagpause,
					zeitmodelltagpauseDto);
			return zeitmodelltagpauseDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeZeitmodelltagpause(
			ZeitmodelltagpauseDto zeitmodelltagpauseDto) throws EJBExceptionLP {
		myLogger.entry();
		if (zeitmodelltagpauseDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zeitmodelltagpauseDto == null"));
		}
		if (zeitmodelltagpauseDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("zeitmodelltagpauseDto.getIId() == null"));
		}

		// try {
		Zeitmodelltagpause toRemove = em.find(Zeitmodelltagpause.class,
				zeitmodelltagpauseDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN,
		// e);
		// }
	}

	public void updateZeitmodelltagpause(
			ZeitmodelltagpauseDto zeitmodelltagpauseDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (zeitmodelltagpauseDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zeitmodelltagpauseDto == null"));
		}
		if (zeitmodelltagpauseDto.getIId() == null
				|| zeitmodelltagpauseDto.getZeitmodelltagIId() == null
				|| zeitmodelltagpauseDto.getUBeginn() == null
				|| zeitmodelltagpauseDto.getUEnde() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zeitmodelltagpauseDto.getIId() == null || zeitmodelltagpauseDto.getZeitmodelltagIId() == null || zeitmodelltagpauseDto.getUBeginn() == null || zeitmodelltagpauseDto.getUEnde() == null"));
		}

		Integer iId = zeitmodelltagpauseDto.getIId();
		// try {
		Zeitmodelltagpause zeitmodelltagpause = em.find(
				Zeitmodelltagpause.class, iId);
		if (zeitmodelltagpause == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		zeitmodelltagpauseDto.setPersonalIIdAendern(theClientDto
				.getIDPersonal());
		zeitmodelltagpauseDto.setTAendern(new java.sql.Timestamp(System
				.currentTimeMillis()));

		setZeitmodelltagpauseFromZeitmodelltagpauseDto(zeitmodelltagpause,
				zeitmodelltagpauseDto);
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }

	}

	public ZeitmodelltagpauseDto zeitmodelltagpauseFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Zeitmodelltagpause zeitmodelltagpause = em.find(
				Zeitmodelltagpause.class, iId);
		if (zeitmodelltagpause == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleZeitmodelltagpauseDto(zeitmodelltagpause);
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setZeitmodelltagpauseFromZeitmodelltagpauseDto(
			Zeitmodelltagpause zeitmodelltagpause,
			ZeitmodelltagpauseDto zeitmodelltagpauseDto) {
		zeitmodelltagpause.setZeitmodelltagIId(zeitmodelltagpauseDto
				.getZeitmodelltagIId());
		zeitmodelltagpause.setUBeginn(zeitmodelltagpauseDto.getUBeginn());
		zeitmodelltagpause.setUEnde(zeitmodelltagpauseDto.getUEnde());
		zeitmodelltagpause.setPersonalIIdAendern(zeitmodelltagpauseDto
				.getPersonalIIdAendern());
		zeitmodelltagpause.setTAendern(zeitmodelltagpauseDto.getTAendern());
		em.merge(zeitmodelltagpause);
		em.flush();
	}

	private ZeitmodelltagpauseDto assembleZeitmodelltagpauseDto(
			Zeitmodelltagpause zeitmodelltagpause) {
		return ZeitmodelltagpauseDtoAssembler.createDto(zeitmodelltagpause);
	}

	private ZeitmodelltagpauseDto[] assembleZeitmodelltagpauseDtos(
			Collection<?> zeitmodelltagpauses) {
		List<ZeitmodelltagpauseDto> list = new ArrayList<ZeitmodelltagpauseDto>();
		if (zeitmodelltagpauses != null) {
			Iterator<?> iterator = zeitmodelltagpauses.iterator();
			while (iterator.hasNext()) {
				Zeitmodelltagpause zeitmodelltagpause = (Zeitmodelltagpause) iterator
						.next();
				list.add(assembleZeitmodelltagpauseDto(zeitmodelltagpause));
			}
		}
		ZeitmodelltagpauseDto[] returnArray = new ZeitmodelltagpauseDto[list
				.size()];
		return (ZeitmodelltagpauseDto[]) list.toArray(returnArray);
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

	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;

		String fieldName = field.getName();

		if (sAktuellerReport.equals(ZeiterfassungFac.REPORT_MONATSABRECHNUNG)) {
			if ("Woche".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_KALENDERWOCHE];
			} else if ("Wochentag".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_TAG];
			} else if ("Tag".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_DATUM];
			} else if ("Tagesart".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_TAGESART];
			} else if ("Zeitmodell".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_ZEITMODELL];
			} else if ("Von".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_VON];
			} else if ("Bis".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_BIS];
			} else if ("Unter".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_UNTER];
			} else if ("Soll".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_SOLL];
			} else if ("Ist".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_IST];
			} else if ("Diff".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_DIFF];
			} else if ("SubreportZulagen".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_SUBREPORT_ZULAGEN];
			} else if ("Feiertag".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_FEIERTAG];
			} else if ("Urlaub".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_URLAUB];
			} else if ("UrlaubTag".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_URLAUBTAGEWEISE];
			} else if ("Krank".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_KRANK];
			} else if ("Kindkrank".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_KINDKRANK];
			} else if ("Arzt".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_ARZT];
			} else if ("Behoerde".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_BEHOERDE];
			} else if ("Zeitausgleich".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_ZEITAUSGLEICH];
			} else if ("Bemerkung".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_BEMERKUNG];
			} else if ("Sonstigebezahlt".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_SONSTIGE_BEZAHLTETAETIGKEITEN];
			} else if ("Sonstigeunbezahlt".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_SONSTIGE_UNBEZAHLTETAETIGKEITEN];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_ZUSATZBEZEICHNUNG];
			} else if ("Reise".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_REISE];
			} else if ("Uestd100".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_UESTD100];
			} else if ("Uestd100Steuerfrei".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_UESTD100FREI];
			} else if ("Uestd50".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_UESTD50];
			} else if ("Uestd200".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_UESTD200];
			} else if ("Uestd50Tageweise".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_UESTD50TAGEWEISE];
			} else if ("Uestd50TageweiseSteuerfrei".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_UESTD50TAGESWEISEFREI];
			} else if ("Jahr".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_JAHR];
			} else if ("Mehrstunden".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_MEHRZEIT];
			} else if ("Qualifikationsfaktor".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_QUALIFIKATIONSFAKTOR];
			}
		} else if (sAktuellerReport
				.equals(ZeiterfassungFac.REPORT_WOCHENABRECHNUNG)
				|| sAktuellerReport
						.equals(ZeiterfassungFac.REPORT_WOCHENJOURNAL)) {
			if ("Kw".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_KALENDERWOCHE];
			} else if ("Soll".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_SOLL];
			} else if ("Ist".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_IST];
			} else if ("Diff".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_DIFF];
			} else if ("Unter".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_UNTER];
			} else if ("Ftg".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_FEIERTAG];
			} else if ("Urlaub".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_URLAUB];
			} else if ("Krank".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_KRANK];
			} else if ("Kindkrank".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_KINDKRANK];
			} else if ("Behoerde".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_BEHOERDE];
			} else if ("Arzt".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_ARZT];
			} else if ("Sonstbez".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_SONSTIGE_BEZAHLTETAETIGKEITEN];
			} else if ("Uestd100".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_UESTD100];
			} else if ("Uestd200".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_UESTD200];
			} else if ("Uestd100Frei".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_UESTD100FREI];
			} else if ("Uestd50Tageweise".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_UESTD50TAGEWEISE];
			} else if ("Uestd50TageweiseFrei".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_UESTD50TAGESWEISEFREI];
			} else if ("Uestd50".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_UESTD50];
			} else if ("Mehrzeit".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_MEHRZEIT];
			} else if ("SubreportZeitdatenjournal".equals(fieldName)) {
				value = data[index][REPORT_MONATSABRECHNUNG_PLATZHALTER_FUER_SUREPORTZEIDATENJOURNAL];
			}

		} else if (sAktuellerReport
				.equals(ZeiterfassungFac.REPORT_ANWESENHEITSLISTE)) {
			if ("Anwesend".equals(fieldName)) {
				value = data[index][REPORT_ANWESENHEITSLISTE_ANWESEND];
			} else if ("Personalnummer".equals(fieldName)) {
				value = data[index][REPORT_ANWESENHEITSLISTE_PERSONALNUMMER];
			} else if ("Name".equals(fieldName)) {
				value = data[index][REPORT_ANWESENHEITSLISTE_NAME];
			} else if ("Telefonprivat".equals(fieldName)) {
				value = data[index][REPORT_ANWESENHEITSLISTE_TEL_PRIVAT];
			} else if ("Taetigkeit".equals(fieldName)) {
				value = data[index][REPORT_ANWESENHEITSLISTE_TAETIGKEIT];
			} else if ("Zeit".equals(fieldName)) {
				value = data[index][REPORT_ANWESENHEITSLISTE_ZEIT];
			} else if ("Sonderzeit".equals(fieldName)) {
				value = data[index][REPORT_ANWESENHEITSLISTE_SONDERZEIT];
			} else if ("SonderzeitArt".equals(fieldName)) {
				value = data[index][REPORT_ANWESENHEITSLISTE_SONDERZEIT_ART];
			} else if ("SonderzeitStunden".equals(fieldName)) {
				value = data[index][REPORT_ANWESENHEITSLISTE_SONDERZEIT_STUNDEN];
			}

		} else if (sAktuellerReport
				.equals(ZeiterfassungFac.REPORT_SONDERZEITENLISTE)) {
			if ("Personalnummergruppierung".equals(fieldName)) {
				value = data[index][REPORT_SONDERTAETIGKEITENLISTE_PERSONALNUMMER_GRUPPIERUNG];
			} else if ("Personalnummer".equals(fieldName)) {
				value = data[index][REPORT_SONDERTAETIGKEITENLISTE_PERSONALNUMMER];
			} else if ("Name".equals(fieldName)) {
				value = data[index][REPORT_SONDERTAETIGKEITENLISTE_NAME];
			} else if ("Taetigkeit".equals(fieldName)) {
				value = data[index][REPORT_SONDERTAETIGKEITENLISTE_TAETIGKEIT];
			} else if ("Zeit".equals(fieldName)) {
				value = data[index][REPORT_SONDERTAETIGKEITENLISTE_ZEIT];
			} else if ("Faktorbezahlt".equals(fieldName)) {
				value = data[index][REPORT_SONDERTAETIGKEITENLISTE_FAKTORBEZAHLT];
			} else if ("LfdFehltage".equals(fieldName)) {
				value = data[index][REPORT_SONDERTAETIGKEITENLISTE_LFD_FEHLTAGE];
			} else if ("WarnmeldungInKalendertagen".equals(fieldName)) {
				value = data[index][REPORT_SONDERTAETIGKEITENLISTE_WARNMEDLUNG_IN_KALENDERTAGEN];
			}
		} else if (sAktuellerReport
				.equals(ZeiterfassungFac.REPORT_LOHNDATENEXPORT)) {
			if ("Personalnummer".equals(fieldName)) {
				value = data[index][ZeiterfassungFac.REPORT_LOHNDATENEXPORT_PERSONALNUMMER];
			} else if ("Monat".equals(fieldName)) {
				value = data[index][ZeiterfassungFac.REPORT_LOHNDATENEXPORT_MONAT];
			} else if ("Jahr".equals(fieldName)) {
				value = data[index][ZeiterfassungFac.REPORT_LOHNDATENEXPORT_JAHR];
			} else if ("Lohnart".equals(fieldName)) {
				value = data[index][ZeiterfassungFac.REPORT_LOHNDATENEXPORT_LOHNART];
			} else if ("Stunden".equals(fieldName)) {
				value = data[index][ZeiterfassungFac.REPORT_LOHNDATENEXPORT_STUNDEN];
			} else if ("Vorname".equals(fieldName)) {
				value = data[index][ZeiterfassungFac.REPORT_LOHNDATENEXPORT_VORNAME];
			} else if ("Name".equals(fieldName)) {
				value = data[index][ZeiterfassungFac.REPORT_LOHNDATENEXPORT_NACHNAME];
			} else if ("Lohnart_Nr".equals(fieldName)) {
				value = data[index][ZeiterfassungFac.REPORT_LOHNDATENEXPORT_LOHNART_NR];
			} else if ("Lohnstundenart".equals(fieldName)) {
				value = data[index][ZeiterfassungFac.REPORT_LOHNDATENEXPORT_LOHNSTUNDENART];
			}
		} else if (sAktuellerReport
				.equals(ZeiterfassungFac.REPORT_PRODUKTIVITAETSSTATISTIK)) {
			if ("PersonalID".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_PERSONAL_ID];
			} else if ("Auftrag".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_AUFTRAG];
			} else if ("Projekt".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_PROJEKT];
			} else if ("Angebot".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_ANGEBOT];
			} else if ("Projektbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_PROJEKTBEZEICHNUNG];
			} else if ("Kunde".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_KUNDE];
			} else if ("Dauer".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_DAUER];
			} else if ("Stklnr".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_STKLNR];
			} else if ("Stklbez".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_STKLBEZ];
			} else if ("Los".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_LOS];
			} else if ("Losklassen".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_LOSKLASSEN];
			} else if ("Artikel".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_TAETIGKEIT];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_TAETIGKEIT_BEZEICHNUNG];
			}

			else if ("LosAgart".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_LOS_AGART];
			} else if ("LosFertig".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_LOS_FERTIG];
			} else if ("LosGutstueck".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_LOS_GUTSTUECK];
			} else if ("LosRuestenMitrechnen".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_LOS_RUESTEN_MITRECHNEN];
			} else if ("LosInarbeit".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_LOS_INARBEIT];
			} else if ("LosLosgroesse".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_LOS_LOSGROESSE];
			} else if ("LosRuestzeit".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_LOS_RUESTZEIT];
			} else if ("LosSchlechtstueck".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_LOS_SCHLECHTSTUECK];
			} else if ("LosStueckzeit".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_LOS_STUECKZEIT];
			} else if ("LosZeitanteil".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_LOS_ZEITANTEIL];
			} else if ("LosGesamtzeit".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_LOS_GESAMTZEIT];
			} else if ("LosAgnummer".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_LOS_AGNUMMER];
			} else if ("LosUagnummer".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_LOS_UAGNUMMER];
			} else if ("Projekt_Projektklammer".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_PROJEKT_PROJEKTKLAMMER];
			} else if ("Projektkategorie_Projektklammer".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_PROJEKTKATEGORIE_PROJEKTKLAMMER];
			} else if ("Telefonzeit".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_TELEFONZEIT];
			} else if ("PersonName".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_PERSON_NAME];
			} else if ("PersonIstGesamt".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_PERSON_IST_GESAMT];
			} else if ("PersonIntern".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_PERSON_INTERN];
			} else if ("PersonExtern".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_PERSON_EXTERN];
			} else if ("PersonProzentintern".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_PERSON_PROZENTINTERN];
			} else if ("PersonProzentextern".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_PERSON_PROZENTEXTERN];
			} else if ("PersonLeistungswert".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_PERSON_LEISTUNGSWERT];
			} else if ("PersonSubreportSondertaetigkeiten".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_PERSON_SUBREPORT_SONDERTAETIGKEITEN];
			} else if ("PersonKostenstelle".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_PERSON_KOSTENSTELLE];
			} else if ("PersonAbteilung".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETISSTATISTIK_PERSON_ABTEILUNG];
			}

		} else if (sAktuellerReport
				.equals(ZeiterfassungFac.REPORT_SONDERTAETIKGEITEN)) {
			if ("Kennung".equals(fieldName)) {
				value = data[index][REPORT_SONDERTAETIGKEITEN_KENNUNG];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_SONDERTAETIGKEITEN_BEZEICHNUNG];
			}
		} else if (sAktuellerReport.equals(ZeiterfassungFac.REPORT_ZEITSALDO)) {
			if ("Arzt".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_ARZT];
			} else if ("Behoerde".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_BEHOERDE];
			} else if ("Feiertag".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_FTG];
			} else if ("Feiertagsoll".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_FTGSOLL];
			} else if ("Ist".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_IST];
			} else if ("Krank".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_KRANK];
			} else if ("KindKrank".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_KIND_KRANK];
			} else if ("Person".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_PERSON];
			} else if ("Personalnummer".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_PERSONALNUMMER];
			} else if ("Reise".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_REISE];
			} else if ("Soll".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_SOLL];
			} else if ("Sonstigebezahlt".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_SONSTIGEBEZAHLT];
			} else if ("Sonstigenichtbezahlt".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_SONSTIGENICHTBEZAHLT];
			} else if ("Uestdsaldo100".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_UESTDSALDO100];
			} else if ("Uestdsaldo100Steuerfrei".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_UESTDSALDO100STF];
			} else if ("Uestdsaldo200".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_UESTDSALDO200];
			} else if ("Uestdsaldo50".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_UESTDSALDO50];
			} else if ("Ausbezahlt100".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_AUSBEZAHLT100];
			} else if ("Ausbezahlt100Steuerfrei".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_AUSBEZAHLT100STF];
			} else if ("Ausbezahlt50".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_AUSBEZAHLT50];
			} else if ("Ausbezahlt50Steuerfrei".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_AUSBEZAHLT50STF];
			} else if ("AusbezahltGutstunden".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_AUSBEZAHLTGUTSTD];
			} else if ("Qualipraemie".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_QUALIPRAEMIE];
			} else if ("AusbezahltMehrstunden".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_AUSBEZAHLTMEHRSTD];
			} else if ("AusbezahltNormalstunden".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_AUSBEZAHLTNORMALSTD];
			} else if ("Ausbezahlt200".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_AUSBEZAHLT200];
			} else if ("Uestdsaldo50Steuerfrei".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_UESTDSALDO50STF];
			} else if ("UestdsaldoMehrstunden".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_UESTDSALDOMEHRSTD];
			} else if ("Urlaub".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_URLAUBSTD];
			} else if ("UrlaubStundenAliquot".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_URLAUBSTUNDEN_ALIQUOT];
			} else if ("UrlaubStundenGeplant".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_URLAUBSTUNDEN_URLAUB_GEPLANT];
			} else if ("UrlaubStundenAktuell".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_URLAUBSTUNDEN_URLAUBAKUTELL];
			} else if ("UrlaubStundenAktuellVerbraucht".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_URLAUBSTUNDEN_URLAUBAKUTELLVERBRAUCHT];
			} else if ("UrlaubStundenRest".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_URLAUBSTUNDEN_URLAUBREST];
			} else if ("UrlaubStundenRestVerbraucht".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_URLAUBSTUNDEN_URLAUBRESTVERBRAUCHT];
			} else if ("UrlaubTageAliquot".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_URLAUBTAGE_ALIQUOT];
			} else if ("UrlaubTageGeplant".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_URLAUBTAGE_URLAUB_GEPLANT];
			} else if ("UrlaubTageAktuell".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_URLAUBTAGE_URLAUBAKTUELL];
			} else if ("UrlaubTageVorjahr".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_URLAUBTAGE_URLAUBVORJAHR];
			} else if ("UrlaubTageAktuellVerbraucht".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_URLAUBTAGE_URLAUBAKTUELLVERBRAUCHT];
			} else if ("UrlaubTageRest".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_URLAUBTAGE_URLAUBREST];
			} else if ("UrlaubTageRestVerbraucht".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_URLAUBTAGE_URLAUBRESTVERBRAUCHT];
			} else if ("GleitzeitsaldoVormonat".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_GLEITZEITSALDOVORMONAT];
			} else if ("GleitzeitsaldoAktuellerMonat".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_GLEITZEITSALDOAKTUELLERMONAT];
			} else if ("MonatJahr".equals(fieldName)) {
				value = data[index][REPORT_ZEITSALDO_MONAT];
			}
		} else if (sAktuellerReport
				.equals(ZeiterfassungFac.REPORT_WOCHENABSCHLUSS)) {
			if ("Datum".equals(fieldName)) {
				value = data[index][REPORT_WOCHENABSCHLUSS_DATUM];
			} else if ("Geht".equals(fieldName)) {
				value = data[index][REPORT_WOCHENABSCHLUSS_GEHT];
			} else if ("Kommt".equals(fieldName)) {
				value = data[index][REPORT_WOCHENABSCHLUSS_KOMMT];
			} else if ("SubreportBelegzeiten".equals(fieldName)) {
				value = data[index][REPORT_WOCHENABSCHLUSS_SUBREPORT_BELEGZEITEN];
			} else if ("Soll".equals(fieldName)) {
				value = data[index][REPORT_WOCHENABSCHLUSS_SOLL];
			} else if ("Ist".equals(fieldName)) {
				value = data[index][REPORT_WOCHENABSCHLUSS_IST];
			} else if ("Feiertag".equals(fieldName)) {
				value = data[index][REPORT_WOCHENABSCHLUSS_FEIERTAG];
			} else if ("Arzt".equals(fieldName)) {
				value = data[index][REPORT_WOCHENABSCHLUSS_ARZT];
			} else if ("Krank".equals(fieldName)) {
				value = data[index][REPORT_WOCHENABSCHLUSS_KRANK];
			} else if ("KindKrank".equals(fieldName)) {
				value = data[index][REPORT_WOCHENABSCHLUSS_KINDKRANK];
			} else if ("Behoerde".equals(fieldName)) {
				value = data[index][REPORT_WOCHENABSCHLUSS_BEHOERDE];
			} else if ("Urlaub".equals(fieldName)) {
				value = data[index][REPORT_WOCHENABSCHLUSS_URLAUB];
			} else if ("SonstigeBezahlt".equals(fieldName)) {
				value = data[index][REPORT_WOCHENABSCHLUSS_SONSTIGE_BEZAHLT];
			} else if ("SonstigeUnbezahlt".equals(fieldName)) {
				value = data[index][REPORT_WOCHENABSCHLUSS_SONSTIGE_UNBEZAHLT];
			} else if ("Fehler".equals(fieldName)) {
				value = data[index][REPORT_WOCHENABSCHLUSS_FEHLER];
			} else if ("Wochentag".equals(fieldName)) {
				value = data[index][REPORT_WOCHENABSCHLUSS_WOCHENTAG];
			}

		}
		return value;
	}

	public Integer createMaschine(MaschineDto maschineDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (maschineDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("maschineDto == null"));
		}
		if (maschineDto.getCInventarnummer() == null
				|| maschineDto.getBAutoendebeigeht() == null

				|| maschineDto.getMaschinengruppeIId() == null
				|| maschineDto.getBVersteckt() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"maschineDto.getCInventarnummer() == null || maschineDto.getBAutoendebeigeht() == null || maschineDto.getFVerfuegbarkeitinprozent() == null || maschineDto.getMaschinengruppeIId() == null"));
		}
		try {
			Query query = em
					.createNamedQuery("MaschinefindByMandantCNrCInventarnummer");
			query.setParameter(1, theClientDto.getMandant());
			query.setParameter(2, maschineDto.getCInventarnummer());
			// @todo getSingleResult oder getResultList ?
			Maschine doppelt = (Maschine) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_MASCHINE.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}
		if (maschineDto.getCIdentifikationsnr() != null) {
			try {
				Query query = em
						.createNamedQuery("MaschinefindByCIdentifikationsnr");
				query.setParameter(1, maschineDto.getCIdentifikationsnr());
				Maschine doppelt = (Maschine) query.getSingleResult();
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_MASCHINE.C_IDENTIFIKATIONSNR"));
			} catch (NoResultException ex1) {
				// nothing here
			}
		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_MASCHINE);
			maschineDto.setIId(pk);
			maschineDto.setMandantCNr(theClientDto.getMandant());

			Maschine maschine = new Maschine(maschineDto.getIId(),
					maschineDto.getMandantCNr(),
					maschineDto.getCInventarnummer(),
					maschineDto.getBAutoendebeigeht(),
					maschineDto.getMaschinengruppeIId(),
					maschineDto.getBVersteckt());
			em.persist(maschine);
			em.flush();
			setMaschineFromMaschineDto(maschine, maschineDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return maschineDto.getIId();
	}

	public void removeMaschine(MaschineDto maschineDto) throws EJBExceptionLP {
		myLogger.entry();
		if (maschineDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("maschineDto == null"));
		}
		if (maschineDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("maschineDto.getIId() == null"));
		}

		// try {
		Maschine toRemove = em.find(Maschine.class, maschineDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN,
		// e);
		// }

	}

	public void updateMaschine(MaschineDto maschineDto) throws EJBExceptionLP {
		if (maschineDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("maschineDto == null"));
		}
		if (maschineDto.getIId() == null
				|| maschineDto.getCInventarnummer() == null
				|| maschineDto.getBAutoendebeigeht() == null
				|| maschineDto.getMaschinengruppeIId() == null
				|| maschineDto.getBVersteckt() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"maschineDto.getIId() == null || maschineDto.getCInventarnummer() == null || maschineDto.getBAutoendebeigeht() == null || maschineDto.getFVerfuegbarkeitinprozent() == null || maschineDto.getMaschinengruppeIId() == null"));
		}
		Integer iId = maschineDto.getIId();

		try {
			Query query = em
					.createNamedQuery("MaschinefindByMandantCNrCInventarnummer");
			query.setParameter(1, maschineDto.getMandantCNr());
			query.setParameter(2, maschineDto.getCInventarnummer());
			Integer iIdVorhanden = ((Maschine) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_MASCHINE.UK"));
			}

		} catch (NoResultException ex) {
			// NIX
		}

		if (maschineDto.getCIdentifikationsnr() != null) {
			try {
				Query query = em
						.createNamedQuery("MaschinefindByCIdentifikationsnr");
				query.setParameter(1, maschineDto.getCIdentifikationsnr());
				Integer iIdVorhanden = ((Maschine) query.getSingleResult())
						.getIId();
				if (iId.equals(iIdVorhanden) == false) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("PERS_MASCHINE.C_IDENTIFIKATIONSNR"));
				}

			} catch (NoResultException ex) {

			}

		}

		// try {
		Maschine maschine = em.find(Maschine.class, iId);
		if (maschine == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}
		setMaschineFromMaschineDto(maschine, maschineDto);
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);

		// }

	}

	public MaschineDto maschineFindByPrimaryKey(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Maschine maschine = em.find(Maschine.class, iId);
		if (maschine == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleMaschineDto(maschine);
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public MaschinenzeitdatenDto maschinenzeitdatenFindByPrimaryKey(Integer iId) {

		Maschinenzeitdaten maschinenzeitdaten = em.find(
				Maschinenzeitdaten.class, iId);
		if (maschinenzeitdaten == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleMaschinenzeitdatenDto(maschinenzeitdaten);

	}

	public MaschineDto maschineFindByMandantCNrCInventarnummer(
			String cInventarnummer, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (cInventarnummer == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("cInventarnummer == null"));
		}
		// try {
		Query query = em
				.createNamedQuery("MaschinefindByMandantCNrCInventarnummer");
		query.setParameter(1, cInventarnummer);
		query.setParameter(2, theClientDto.getMandant());
		Maschine maschine = (Maschine) query.getSingleResult();
		if (maschine == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleMaschineDto(maschine);
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
		// e);
		// }
	}

	public MaschineDto[] maschineFindByMandantCNr(String mandantCNr)
			throws EJBExceptionLP {
		if (mandantCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("mandantCNr == null"));
		}
		// try {
		Query query = em.createNamedQuery("MaschinefindByByMandantCNr");
		query.setParameter(1, mandantCNr);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
		// null);
		// }
		return assembleMaschineDtos(cl);
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
		// e);
		// }
	}

	public MaschineDto maschineFindByCIdentifikationsnr(
			String cIdentifikationsnr) throws EJBExceptionLP {
		if (cIdentifikationsnr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("cIdentifikationsnr == null"));
		}
		try {
			Query query = em
					.createNamedQuery("MaschinefindByCIdentifikationsnr");
			query.setParameter(1, cIdentifikationsnr);
			Maschine maschine = (Maschine) query.getSingleResult();
			return assembleMaschineDto(maschine);
		} catch (NoResultException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}
	}

	private void setMaschineFromMaschineDto(Maschine maschine,
			MaschineDto maschineDto) {
		maschine.setMandantCNr(maschineDto.getMandantCNr());
		maschine.setCInventarnummer(maschineDto.getCInventarnummer());
		maschine.setCIdentifikationsnr(maschineDto.getCIdentifikationsnr());
		maschine.setCBez(maschineDto.getCBez());
		maschine.setBAutoendebeigeht(maschineDto.getBAutoendebeigeht());
		maschine.setTKaufdatum(maschineDto.getTKaufdatum());
		maschine.setMaschinengruppeIId(maschineDto.getMaschinengruppeIId());
		maschine.setBVersteckt(maschineDto.getBVersteckt());
		em.merge(maschine);
		em.flush();
	}

	private MaschineDto assembleMaschineDto(Maschine maschine) {
		return MaschineDtoAssembler.createDto(maschine);
	}

	private MaschinenzeitdatenDto assembleMaschinenzeitdatenDto(
			Maschinenzeitdaten maschinenzeitdaten) {
		return MaschinenzeitdatenDtoAssembler.createDto(maschinenzeitdaten);
	}

	private MaschineDto[] assembleMaschineDtos(Collection<?> maschines) {
		List<MaschineDto> list = new ArrayList<MaschineDto>();
		if (maschines != null) {
			Iterator<?> iterator = maschines.iterator();
			while (iterator.hasNext()) {
				Maschine maschine = (Maschine) iterator.next();
				list.add(assembleMaschineDto(maschine));
			}
		}
		MaschineDto[] returnArray = new MaschineDto[list.size()];
		return (MaschineDto[]) list.toArray(returnArray);
	}

	public Integer createZeitstift(ZeitstiftDto zeitstiftDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (zeitstiftDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zeitstiftDto == null"));
		}
		if (zeitstiftDto.getCNr() == null
				|| zeitstiftDto.getBMehrfachstift() == null
				|| zeitstiftDto.getCTyp() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zeitstiftDto.getCNr() == null || zeitstiftDto.getBMehrfachstift() == null || zeitstiftDto.getCTyp() == null"));
		}
		if (Helper.short2boolean(zeitstiftDto.getBPersonenzuordnung()) == false) {
			if (Helper.short2boolean(zeitstiftDto.getBMehrfachstift()) == false
					&& zeitstiftDto.getPersonalIId() == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
						new Exception("zeitstiftDto.getPersonalIId() == null"));

			}
		} else {
			zeitstiftDto.setPersonalIId(null);
		}
		if (Helper.short2boolean(zeitstiftDto.getBMehrfachstift()) == true) {
			zeitstiftDto.setPersonalIId(null);
		}

		try {
			Query query = em.createNamedQuery("ZeitstiftfindByCNr");
			query.setParameter(1, zeitstiftDto.getCNr());
			Zeitstift doppelt = (Zeitstift) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_ZEITSTIFT.C_NR"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {

			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ZEITSTIFT);
			zeitstiftDto.setIId(pk);
			zeitstiftDto.setMandantCNr(theClientDto.getMandant());
			Zeitstift zeitstift = new Zeitstift(zeitstiftDto.getIId(),
					zeitstiftDto.getCNr(), zeitstiftDto.getMandantCNr(),
					zeitstiftDto.getBMehrfachstift(),
					zeitstiftDto.getBPersonenzuordnung(),
					zeitstiftDto.getCTyp());
			em.persist(zeitstift);
			em.flush();
			setZeitstiftFromZeitstiftDto(zeitstift, zeitstiftDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return zeitstiftDto.getIId();
	}

	public void removeZeitstift(ZeitstiftDto zeitstiftDto)
			throws EJBExceptionLP {

		myLogger.entry();
		if (zeitstiftDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zeitstiftDto == null"));
		}
		if (zeitstiftDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("zeitstiftDto.getIId() == null"));
		}

		// try {
		Zeitstift toRemove = em.find(Zeitstift.class, zeitstiftDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN,
		// e);
		// }
	}

	public void updateZeitstift(ZeitstiftDto zeitstiftDto)
			throws EJBExceptionLP {
		if (zeitstiftDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zeitstiftDto == null"));
		}
		if (zeitstiftDto.getIId() == null || zeitstiftDto.getCNr() == null
				|| zeitstiftDto.getBMehrfachstift() == null
				|| zeitstiftDto.getCTyp() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zeitstiftDto.getIId() == null || zeitstiftDto.getCNr() == null || zeitstiftDto.getBMehrfachstift() == null || zeitstiftDto.getCTyp() == null"));
		}

		if (Helper.short2boolean(zeitstiftDto.getBMehrfachstift()) == true) {
			zeitstiftDto.setPersonalIId(null);
		}

		Integer iId = zeitstiftDto.getIId();

		try {
			Query query = em.createNamedQuery("ZeitstiftfindByCNr");
			query.setParameter(1, zeitstiftDto.getCNr());
			Integer iIdVorhanden = ((Zeitstift) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_ZEITSTIFT.CNR"));
			}

		} catch (NoResultException ex) {
			//
		}

		// try {
		Zeitstift zeitstift = em.find(Zeitstift.class, iId);
		if (zeitstift == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}
		setZeitstiftFromZeitstiftDto(zeitstift, zeitstiftDto);
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);

		// }

	}

	public ZeitstiftDto zeitstiftFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Zeitstift zeitstift = em.find(Zeitstift.class, iId);
		if (zeitstift == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		return assembleZeitstiftDto(zeitstift);
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public ZeitstiftDto[] zeitstiftFindByPersonalIId(Integer personalIId)
			throws EJBExceptionLP {
		if (personalIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("personalIId == null"));
		}
		// try {
		Query query = em.createNamedQuery("ZeitstiftfindByPersonalIId");
		query.setParameter(1, personalIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
		// null);
		// }
		return assembleZeitstiftDtos(cl);
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
		// e);
		// }
	}

	public ZeitstiftDto[] zeitstiftFindByMandantCNr(TheClientDto theClientDto)
			throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("ZeitstiftfindByMandantCNr");
		query.setParameter(1, theClientDto.getMandant());
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
		// null);
		// }
		return assembleZeitstiftDtos(cl);
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
		// e);
		// }
	}

	private void setZeitstiftFromZeitstiftDto(Zeitstift zeitstift,
			ZeitstiftDto zeitstiftDto) {
		zeitstift.setCNr(zeitstiftDto.getCNr());
		zeitstift.setBMehrfachstift(zeitstiftDto.getBMehrfachstift());
		zeitstift.setBPersonenzuordnung(zeitstiftDto.getBPersonenzuordnung());
		zeitstift.setPersonalIId(zeitstiftDto.getPersonalIId());
		zeitstift.setMandantCNr(zeitstiftDto.getMandantCNr());
		zeitstift.setCTyp(zeitstiftDto.getCTyp());
		em.merge(zeitstift);
		em.flush();
	}

	private ZeitstiftDto assembleZeitstiftDto(Zeitstift zeitstift) {
		return ZeitstiftDtoAssembler.createDto(zeitstift);
	}

	private ZeitstiftDto[] assembleZeitstiftDtos(Collection<?> zeitstifts) {
		List<ZeitstiftDto> list = new ArrayList<ZeitstiftDto>();
		if (zeitstifts != null) {
			Iterator<?> iterator = zeitstifts.iterator();
			while (iterator.hasNext()) {
				Zeitstift zeitstift = (Zeitstift) iterator.next();
				list.add(assembleZeitstiftDto(zeitstift));
			}
		}
		ZeitstiftDto[] returnArray = new ZeitstiftDto[list.size()];
		return (ZeitstiftDto[]) list.toArray(returnArray);
	}

	private ZeitverteilungDto assembleZeitverteilungDto(
			Zeitverteilung zeitverteilung) {
		return ZeitverteilungDtoAssembler.createDto(zeitverteilung);
	}

	private ZeitverteilungDto[] assembleZeitverteilungDtos(
			Collection<?> zeitverteilungs) {
		List<ZeitverteilungDto> list = new ArrayList<ZeitverteilungDto>();
		if (zeitverteilungs != null) {
			Iterator<?> iterator = zeitverteilungs.iterator();
			while (iterator.hasNext()) {
				Zeitverteilung zeitverteilung = (Zeitverteilung) iterator
						.next();
				list.add(assembleZeitverteilungDto(zeitverteilung));
			}
		}
		ZeitverteilungDto[] returnArray = new ZeitverteilungDto[list.size()];
		return (ZeitverteilungDto[]) list.toArray(returnArray);
	}

	public Integer createMaschinenkosten(MaschinenkostenDto maschinenkostenDto)
			throws EJBExceptionLP {

		try {
			Query query = em
					.createNamedQuery("MaschinenkostenfindByMaschineIIdTGueltigab");
			query.setParameter(1, maschinenkostenDto.getMaschineIId());
			query.setParameter(2, maschinenkostenDto.getTGueltigab());
			Maschinenkosten doppelt = (Maschinenkosten) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_MASCHINENKOSTEN.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_MASCHINENKOSTEN);
			maschinenkostenDto.setIId(pk);

			Maschinenkosten maschinenkosten = new Maschinenkosten(
					maschinenkostenDto.getIId(),
					maschinenkostenDto.getMaschineIId(),
					maschinenkostenDto.getTGueltigab(),
					maschinenkostenDto.getNStundensatz(),
					maschinenkostenDto.getNVkstundensatz());
			em.persist(maschinenkosten);
			em.flush();
			setMaschinenkostenFromMaschinenkostenDto(maschinenkosten,
					maschinenkostenDto);
			return maschinenkostenDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeMaschinenkosten(MaschinenkostenDto maschinenkostenDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (maschinenkostenDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("maschinenkostenDto == null"));
		}
		if (maschinenkostenDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("maschinenkostenDto.getIId() == null"));
		}

		// try {
		Maschinenkosten toRemove = em.find(Maschinenkosten.class,
				maschinenkostenDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN,
		// e);
		// }
	}

	public void updateMaschinenkosten(MaschinenkostenDto maschinenkostenDto)
			throws EJBExceptionLP {
		if (maschinenkostenDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("maschinenkostenDto == null"));
		}
		if (maschinenkostenDto.getIId() == null
				|| maschinenkostenDto.getMaschineIId() == null
				|| maschinenkostenDto.getNStundensatz() == null
				|| maschinenkostenDto.getTGueltigab() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"maschinenkostenDto.getIId() == null || maschinenkostenDto.getMaschineIId() == null || maschinenkostenDto.getNStundensatz() == null || maschinenkostenDto.getTGueltigab() == null"));
		}

		Integer iId = maschinenkostenDto.getIId();

		try {
			Query query = em
					.createNamedQuery("MaschinenkostenfindByMaschineIIdTGueltigab");
			query.setParameter(1, maschinenkostenDto.getMaschineIId());
			query.setParameter(2, maschinenkostenDto.getTGueltigab());
			Integer iIdVorhanden = ((Maschinenkosten) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_MASCHINENKOSTEN.UK"));
			}
		} catch (NoResultException ex) {
			//
		}

		try {
			Maschinenkosten maschinenkosten = em.find(Maschinenkosten.class,
					iId);
			setMaschinenkostenFromMaschinenkostenDto(maschinenkosten,
					maschinenkostenDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}

	}

	public void maschineStop(Integer maschineIId,
			Integer lossollarbeitsplanIId, java.sql.Timestamp tStop,
			TheClientDto theClientDto) {

		String sQuery = "select zeitdaten FROM FLRMaschinenzeitdaten zeitdaten WHERE zeitdaten.t_von<'"
				+ Helper.formatTimestampWithSlashes(tStop)
				+ "' AND zeitdaten.maschine_i_id="
				+ maschineIId
				+ " AND zeitdaten.lossollarbeitsplan_i_id="
				+ lossollarbeitsplanIId
				+ "  AND zeitdaten.t_bis IS NULL ORDER BY zeitdaten.t_von DESC";

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Query query = session.createQuery(sQuery);
		query.setMaxResults(1);

		List<?> resultList = query.list();

		Iterator<?> resultListIterator = resultList.iterator();

		if (resultListIterator.hasNext()) {
			FLRMaschinenzeitdaten flrMaschinenzeitdaten = (FLRMaschinenzeitdaten) resultListIterator
					.next();

			Maschinenzeitdaten maschinenzeitdaten = em.find(
					Maschinenzeitdaten.class, flrMaschinenzeitdaten.getI_id());
			maschinenzeitdaten.setTBis(tStop);
			em.merge(maschinenzeitdaten);
			em.flush();
		}
	}

	public MaschinenkostenDto maschinenkostenFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Maschinenkosten maschinenkosten = em.find(Maschinenkosten.class, iId);
		if (maschinenkosten == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleMaschinenkostenDto(maschinenkosten);
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public BigDecimal getMaschinenKostenZumZeitpunkt(Integer maschineIId,
			java.sql.Timestamp tDatum) {
		// try {
		Query query = em
				.createNamedQuery("MaschinenkostenfindLetzeKostenByMaschineIIdTGueltigab");
		query.setParameter(1, maschineIId);
		query.setParameter(2, tDatum);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// return new BigDecimal(0);
		// } else {
		MaschinenkostenDto[] maschienkostenDtos = assembleMaschinenkostenDtos(cl);

		if (maschienkostenDtos != null && maschienkostenDtos.length > 0) {
			return maschienkostenDtos[0].getNStundensatz();
		} else {
			return new BigDecimal(0);
		}
		// }
		// catch (NoResultException ex) {
		// return new BigDecimal(0);
		// }
	}

	public BigDecimal getMaschinenKostenVKZumZeitpunkt(Integer maschineIId,
			java.sql.Timestamp tDatum) {
		// try {
		Query query = em
				.createNamedQuery("MaschinenkostenfindLetzeKostenByMaschineIIdTGueltigab");
		query.setParameter(1, maschineIId);
		query.setParameter(2, tDatum);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// return new BigDecimal(0);
		// } else {
		MaschinenkostenDto[] maschienkostenDtos = assembleMaschinenkostenDtos(cl);

		if (maschienkostenDtos != null && maschienkostenDtos.length > 0) {
			return maschienkostenDtos[0].getNVkstundensatz();
		} else {
			return new BigDecimal(0);
		}
		// }
		// catch (NoResultException ex) {
		// return new BigDecimal(0);
		// }
	}

	public MaschinenkostenDto maschinenkostenFindByMaschineIIdTGueltigab(
			Integer maschineIId, Timestamp tGueltigab) throws EJBExceptionLP {
		try {
			Query query = em
					.createNamedQuery("MaschinenkostenfindByMaschineIIdTGueltigab");
			query.setParameter(1, maschineIId);
			query.setParameter(2, tGueltigab);
			return assembleMaschinenkostenDto((Maschinenkosten) query
					.getSingleResult());
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}
	}

	private void setMaschinenkostenFromMaschinenkostenDto(
			Maschinenkosten maschinenkosten,
			MaschinenkostenDto maschinenkostenDto) {
		maschinenkosten.setMaschineIId(maschinenkostenDto.getMaschineIId());
		maschinenkosten.setTGueltigab(maschinenkostenDto.getTGueltigab());
		maschinenkosten.setNStundensatz(maschinenkostenDto.getNStundensatz());
		maschinenkosten.setNVkstundensatz(maschinenkostenDto
				.getNVkstundensatz());
		em.merge(maschinenkosten);
		em.flush();
	}

	private MaschinenkostenDto assembleMaschinenkostenDto(
			Maschinenkosten maschinenkosten) {
		return MaschinenkostenDtoAssembler.createDto(maschinenkosten);
	}

	private MaschinenkostenDto[] assembleMaschinenkostenDtos(
			Collection<?> maschinenkostens) {
		List<MaschinenkostenDto> list = new ArrayList<MaschinenkostenDto>();
		if (maschinenkostens != null) {
			Iterator<?> iterator = maschinenkostens.iterator();
			while (iterator.hasNext()) {
				Maschinenkosten maschinenkosten = (Maschinenkosten) iterator
						.next();
				list.add(assembleMaschinenkostenDto(maschinenkosten));
			}
		}
		MaschinenkostenDto[] returnArray = new MaschinenkostenDto[list.size()];
		return (MaschinenkostenDto[]) list.toArray(returnArray);
	}

	public Integer createMaschinengruppe(MaschinengruppeDto maschinengruppeDto)
			throws EJBExceptionLP {
		if (maschinengruppeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("maschinengruppeDto == null"));
		}
		if (maschinengruppeDto.getCBez() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception("maschinengruppeDto.getCBez() == null"));
		}
		try {
			Query query = em.createNamedQuery("MaschinengruppefindByCBez");
			query.setParameter(1, maschinengruppeDto.getCBez());
			Maschinengruppe doppelt = (Maschinengruppe) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_MASCHINENGRUPPE.C_BEZ"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_MASCHINENGRUPPE);
			maschinengruppeDto.setIId(pk);

			Maschinengruppe maschinengruppe = new Maschinengruppe(
					maschinengruppeDto.getIId(), maschinengruppeDto.getCBez());
			em.persist(maschinengruppe);
			em.flush();
			setMaschinengruppeFromMaschinengruppeDto(maschinengruppe,
					maschinengruppeDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return maschinengruppeDto.getIId();
	}

	public void removeMaschinengruppe(MaschinengruppeDto maschinengruppeDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (maschinengruppeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("maschinengruppeDto == null"));
		}
		if (maschinengruppeDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("maschinengruppeDto.getIId() == null"));
		}

		// try {
		Maschinengruppe toRemove = em.find(Maschinengruppe.class,
				maschinengruppeDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN,
		// e);
		// }
	}

	public void updateMaschinengruppe(MaschinengruppeDto maschinengruppeDto)
			throws EJBExceptionLP {
		if (maschinengruppeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("maschinengruppeDto == null"));
		}
		if (maschinengruppeDto.getIId() == null
				|| maschinengruppeDto.getCBez() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"maschinengruppeDto.getIId() == null || maschinengruppeDto.getCBez() == null"));
		}
		Integer iId = maschinengruppeDto.getIId();
		// try {
		Maschinengruppe maschinengruppe = em.find(Maschinengruppe.class, iId);
		if (maschinengruppe == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		try {
			Query query = em.createNamedQuery("MaschinengruppefindByCBez");
			query.setParameter(1, maschinengruppeDto.getCBez());
			Integer iIdVorhanden = ((Maschinengruppe) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_MASCHINENGRUPPE.C_BEZ"));
			}

		} catch (NoResultException ex) {
			//
		}

		setMaschinengruppeFromMaschinengruppeDto(maschinengruppe,
				maschinengruppeDto);
		// }
		// catch (NoResultException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public MaschinengruppeDto maschinengruppeFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Maschinengruppe maschinengruppe = em.find(Maschinengruppe.class, iId);
		if (maschinengruppe == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleMaschinengruppeDto(maschinengruppe);
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setMaschinengruppeFromMaschinengruppeDto(
			Maschinengruppe maschinengruppe,
			MaschinengruppeDto maschinengruppeDto) {
		maschinengruppe.setCBez(maschinengruppeDto.getCBez());
		em.merge(maschinengruppe);
		em.flush();
	}

	private MaschinengruppeDto assembleMaschinengruppeDto(
			Maschinengruppe maschinengruppe) {
		return MaschinengruppeDtoAssembler.createDto(maschinengruppe);
	}

	private MaschinengruppeDto[] assembleMaschinengruppeDtos(
			Collection<?> maschinengruppes) {
		List<MaschinengruppeDto> list = new ArrayList<MaschinengruppeDto>();
		if (maschinengruppes != null) {
			Iterator<?> iterator = maschinengruppes.iterator();
			while (iterator.hasNext()) {
				Maschinengruppe maschinengruppe = (Maschinengruppe) iterator
						.next();
				list.add(assembleMaschinengruppeDto(maschinengruppe));
			}
		}
		MaschinengruppeDto[] returnArray = new MaschinengruppeDto[list.size()];
		return (MaschinengruppeDto[]) list.toArray(returnArray);
	}

	public Integer createReise(ReiseDto reiseDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (!getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
				MandantFac.ZUSATZFUNKTION_REISEZEITEN, theClientDto)) {
			return null;
		}

		if (reiseDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("reiseDto == null"));
		}
		if (reiseDto.getPersonalIId() == null || reiseDto.getBBeginn() == null
				|| reiseDto.getTZeit() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"reiseDto.getPersonalIId() == null || reiseDto.getBBeginn() == null || reiseDto.getTZeit() == null"));
		}

		if (Helper.short2Boolean(reiseDto.getBBeginn()) == true) {
			reiseDto.setCFahrzeug(null);
			reiseDto.setIKmbeginn(null);
			reiseDto.setIKmende(null);
			reiseDto.setNSpesen(null);
			if (reiseDto.getDiaetenIId() == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
						new Exception("reiseDto.getDiaetenIId() == null"));
			}
		} else {
			reiseDto.setPartnerIId(null);
			reiseDto.setAnsprechpartnerIId(null);
			reiseDto.setDiaetenIId(null);
		}
		try {
			Query query = em.createNamedQuery("ReisefindByPersonalIIdTZeit");
			query.setParameter(1, reiseDto.getPersonalIId());
			query.setParameter(2, reiseDto.getTZeit());
			Reise doppelt = (Reise) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_REISE.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_REISE);
			reiseDto.setIId(pk);
			reiseDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			reiseDto.setTAendern(new java.sql.Timestamp(System
					.currentTimeMillis()));

			Reise reise = new Reise(reiseDto.getIId(),
					reiseDto.getPersonalIId(), reiseDto.getTZeit(),
					reiseDto.getBBeginn(), reiseDto.getPersonalIIdAendern());
			em.persist(reise);
			em.flush();
			setReiseFromReiseDto(reise, reiseDto);
			// Mitprotokollieren
			protokolliereReiselog(reiseDto,
					ZeiterfassungFac.REISELOG_ART_CREATE);

			return pk;
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

	}

	public void removeReise(ReiseDto reiseDto) throws EJBExceptionLP {
		if (reiseDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("reiseDto == null"));
		}
		if (reiseDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception("reiseDto.getIId() == null"));
		}

		try {
			Reise reise = em.find(Reise.class, reiseDto.getIId());
			if (reise == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(reise);
			em.flush();

			// Mitprotokollieren
			protokolliereReiselog(reiseDto,
					ZeiterfassungFac.REISELOG_ART_DELETE);

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}

	}

	public void updateReise(ReiseDto reiseDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (reiseDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("reiseDto == null"));
		}
		if (reiseDto.getIId() == null || reiseDto.getPersonalIId() == null
				|| reiseDto.getBBeginn() == null || reiseDto.getTZeit() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"reiseDto.getIId() == null || reiseDto.getPersonalIId() == null || reiseDto.getBBeginn() == null || reiseDto.getTZeit() == null"));
		}
		if (reiseDto.getPartnerIId() == null) {
			reiseDto.setAnsprechpartnerIId(null);
		}

		if (Helper.short2Boolean(reiseDto.getBBeginn()) == true) {
			reiseDto.setCFahrzeug(null);
			reiseDto.setIKmbeginn(null);
			reiseDto.setIKmende(null);
			reiseDto.setNSpesen(null);
			if (reiseDto.getDiaetenIId() == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
						new Exception("reiseDto.getDiaetenIId() == null"));
			}
		} else {
			reiseDto.setPartnerIId(null);
			reiseDto.setAnsprechpartnerIId(null);
			reiseDto.setDiaetenIId(null);
		}
		Integer iId = reiseDto.getIId();

		try {
			Query query = em.createNamedQuery("ReisefindByPersonalIIdTZeit");
			query.setParameter(1, reiseDto.getPersonalIId());
			query.setParameter(2, reiseDto.getTZeit());
			Integer iIdVorhanden = ((Reise) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_REISE.UK"));
			}
		} catch (NoResultException ex) {
			//
		}

		try {
			Reise reise = em.find(Reise.class, iId);

			reiseDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			reiseDto.setTAendern(new java.sql.Timestamp(System
					.currentTimeMillis()));

			setReiseFromReiseDto(reise, reiseDto);

			// Mitprotokollieren
			protokolliereReiselog(reiseDto,
					ZeiterfassungFac.REISELOG_ART_UPDATE);

		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}

	}

	public void updateReiselog(ReiselogDto reiselogDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (reiselogDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("reiselogDto == null"));
		}
		if (reiselogDto.getIId() == null
				|| reiselogDto.getPersonalIId() == null
				|| reiselogDto.getReiseIId() == null
				|| reiselogDto.getTZeit() == null
				|| reiselogDto.getBBeginn() == null
				|| reiselogDto.getCArt() == null
				|| reiselogDto.getTAendern() == null
				|| reiselogDto.getPersonalIIdAendern() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"reiselogDto.getIId() == null || reiselogDto.getPersonalIId() == null || reiselogDto.getReiseIId() == null || reiselogDto.getTZeit() == null || reiselogDto.getBBeginn() == null || reiselogDto.getCArt() == null || reiselogDto.getTAendern() == null || reiselogDto.getPersonalIIdAendern() == null"));
		}

		Integer iId = reiselogDto.getIId();

		// try {
		Reiselog rl = em.find(Reiselog.class, iId);
		if (rl != null) {
			Integer iIdVorhanden = rl.getIId();

			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_REISELOG.UK"));
			}
		}
		// catch (NoResultException ex) {
		//
		// }

		try {
			Reiselog reiselog = em.find(Reiselog.class, iId);
			setReiselogFromReiselogDto(reiselog, reiselogDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}

	}

	public String getParameterSortierungZeitauswertungen(
			Integer iOptionSortierung, TheClientDto theClientDto) {
		String parameter = "";
		if (iOptionSortierung != null) {
			if (iOptionSortierung == ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_PERSONALNUMMER) {
				parameter = getTextRespectUISpr(
						"pers.zeitauswertungen.sortierung.personalnummer",
						theClientDto.getMandant(), theClientDto.getLocUi());
			} else if (iOptionSortierung == ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_ABTEILUNG_NAME_VORNAME) {
				parameter = getTextRespectUISpr(
						"pers.zeitauswertungen.sortierung.abteilungnamevorname",
						theClientDto.getMandant(), theClientDto.getLocUi());
			} else if (iOptionSortierung == ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_NAME_VORNAME) {
				parameter = getTextRespectUISpr(
						"pers.zeitauswertungen.sortierung.namevorname",
						theClientDto.getMandant(), theClientDto.getLocUi());
			} else if (iOptionSortierung == ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_KOSTENSTELLE_NAME_VORNAME) {
				parameter = getTextRespectUISpr(
						"pers.zeitauswertungen.sortierung.kostenstellenamevorname",
						theClientDto.getMandant(), theClientDto.getLocUi());
			} else if (iOptionSortierung == ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_ABTEILUNG_KOSTENSTELLE_NAME_VORNAME) {
				parameter = getTextRespectUISpr(
						"pers.zeitauswertungen.sortierung.abteilungkostenstellenamevorname",
						theClientDto.getMandant(), theClientDto.getLocUi());
			}
		}
		return parameter;
	}

	public ReiseDto reiseFindByPrimaryKey(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Reise reise = em.find(Reise.class, iId);
		if (reise == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleReiseDto(reise);
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public ReiseDto[] reiseFindByPartnerIId(Integer iPartnerId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		ReiseDto[] reiseDtos = null;
		// try {
		Query query = em.createNamedQuery("ReisefindByPartnerIId");
		query.setParameter(1, iPartnerId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, null);
		// }
		reiseDtos = assembleReiseDtos(cl);
		// }
		// catch (ObjectNotFoundException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		// catch (NoResultException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		return reiseDtos;
	}

	public ReiseDto[] reiseFindByPartnerIIdOhneExc(Integer iPartnerId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		ReiseDto[] reiseDtos = null;
		// try {
		Query query = em.createNamedQuery("ReisefindByPartnerIId");
		query.setParameter(1, iPartnerId);
		// @todo getSingleResult oder getResultList ?
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// myLogger.warn("partnerIId=" + iPartnerId);
		// return null;
		// }
		reiseDtos = assembleReiseDtos(cl);
		// }
		// catch (ObjectNotFoundException ex) {
		// myLogger.warn("partnerIId=" + iPartnerId, ex);
		// return null;
		// }
		// catch (NoResultException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		return reiseDtos;
	}

	public ReiselogDto[] reiselogFindByPartnerIId(Integer iPartnerId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		ReiselogDto[] reiselogDtos = null;
		// try {
		Query query = em.createNamedQuery("ReiselogfindByPartnerIId");
		query.setParameter(1, iPartnerId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		reiselogDtos = assembleReiselogDtos(cl);
		// }
		// catch (ObjectNotFoundException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		// catch (NoResultException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		return reiselogDtos;
	}

	public ReiselogDto[] reiselogFindByPartnerIIdOhneExc(Integer iPartnerId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		ReiselogDto[] reiselogDtos = null;
		// try {
		Query query = em.createNamedQuery("ReiselogfindByPartnerIId");
		query.setParameter(1, iPartnerId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// myLogger.warn("partnerIId=" + iPartnerId);
		// return null;
		// }
		reiselogDtos = assembleReiselogDtos(cl);
		// }
		// catch (ObjectNotFoundException ex) {
		// myLogger.warn("partnerIId=" + iPartnerId, ex);
		// return null;
		// }
		// catch (NoResultException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		return reiselogDtos;
	}

	private void setReiseFromReiseDto(Reise reise, ReiseDto reiseDto) {
		reise.setPersonalIId(reiseDto.getPersonalIId());
		reise.setTZeit(reiseDto.getTZeit());
		reise.setBBeginn(reiseDto.getBBeginn());
		reise.setDiaetenIId(reiseDto.getDiaetenIId());
		reise.setPartnerIId(reiseDto.getPartnerIId());
		reise.setAnsprechpartnerIId(reiseDto.getAnsprechpartnerIId());
		reise.setIKmbeginn(reiseDto.getIKmbeginn());
		reise.setIKmende(reiseDto.getIKmende());
		reise.setNSpesen(reiseDto.getNSpesen());
		reise.setCFahrzeug(reiseDto.getCFahrzeug());
		reise.setCKommentar(reiseDto.getCKommentar());
		reise.setTAendern(reiseDto.getTAendern());
		reise.setPersonalIIdAendern(reiseDto.getPersonalIIdAendern());

		reise.setBelegartCNr(reiseDto.getBelegartCNr());
		reise.setIBelegartid(reiseDto.getIBelegartid());
		reise.setFahrzeugIId(reiseDto.getFahrzeugIId());
		reise.setFFaktor(reiseDto.getFFaktor());
		em.merge(reise);
		em.flush();
	}

	private ReiseDto assembleReiseDto(Reise reise) {
		return ReiseDtoAssembler.createDto(reise);
	}

	private ReiseDto[] assembleReiseDtos(Collection<?> reises) {
		List<ReiseDto> list = new ArrayList<ReiseDto>();
		if (reises != null) {
			Iterator<?> iterator = reises.iterator();
			while (iterator.hasNext()) {
				Reise reise = (Reise) iterator.next();
				list.add(assembleReiseDto(reise));
			}
		}
		ReiseDto[] returnArray = new ReiseDto[list.size()];
		return (ReiseDto[]) list.toArray(returnArray);
	}

	private void setReiselogFromReiselogDto(Reiselog reiselog,
			ReiselogDto reiselogDto) {
		reiselog.setReiseIId(reiselogDto.getReiseIId());
		reiselog.setPersonalIId(reiselogDto.getPersonalIId());
		reiselog.setTZeit(reiselogDto.getTZeit());
		reiselog.setBBeginn(reiselogDto.getBBeginn());
		reiselog.setDiaetenIId(reiselogDto.getDiaetenIId());
		reiselog.setPartnerIId(reiselogDto.getPartnerIId());
		reiselog.setAnsprechpartnerIId(reiselogDto.getAnsprechpartnerIId());
		reiselog.setIKmbeginn(reiselogDto.getIKmbeginn());
		reiselog.setIKmende(reiselogDto.getIKmende());
		reiselog.setNSpesen(reiselogDto.getNSpesen());
		reiselog.setCFahrzeug(reiselogDto.getCFahrzeug());
		reiselog.setCKommentar(reiselogDto.getCKommentar());
		reiselog.setTAendern(reiselogDto.getTAendern());
		reiselog.setPersonalIIdAendern(reiselogDto.getPersonalIIdAendern());
		reiselog.setCArt(reiselogDto.getCArt());
		reiselog.setBelegartCNr(reiselogDto.getBelegartCNr());
		reiselog.setIBelegartid(reiselogDto.getIBelegartid());
		reiselog.setFahrzeugIId(reiselogDto.getFahrzeugIId());
		reiselog.setFFaktor(reiselogDto.getFFaktor());
		em.merge(reiselog);
		em.flush();
	}

	private void protokolliereReiselog(ReiseDto reiseDto, String cArt)
			throws EJBExceptionLP {
		try {

			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_REISELOG);
			reiseDto.setIId(pk);

			Reiselog reiselog = new Reiselog(pk, reiseDto.getIId(),
					reiseDto.getPersonalIId(), reiseDto.getTZeit(),
					reiseDto.getBBeginn(), reiseDto.getPersonalIIdAendern(),
					cArt);
			em.persist(reiselog);
			em.flush();

			reiselog.setAnsprechpartnerIId(reiseDto.getAnsprechpartnerIId());
			reiselog.setCFahrzeug(reiseDto.getCFahrzeug());
			reiselog.setCKommentar(reiseDto.getCKommentar());
			reiselog.setIKmbeginn(reiseDto.getIKmbeginn());
			reiselog.setIKmende(reiseDto.getIKmende());
			reiselog.setDiaetenIId(reiseDto.getDiaetenIId());
			reiselog.setNSpesen(reiseDto.getNSpesen());
			reiselog.setPartnerIId(reiseDto.getPartnerIId());
			reiselog.setBelegartCNr(reiseDto.getBelegartCNr());
			reiselog.setIBelegartid(reiseDto.getIBelegartid());
			reiselog.setFahrzeugIId(reiseDto.getFahrzeugIId());
			reiselog.setFFaktor(reiseDto.getFFaktor());

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

	}

	private ReiselogDto assembleReiselogDto(Reiselog reiselog) {
		return ReiselogDtoAssembler.createDto(reiselog);
	}

	private ReiselogDto[] assembleReiselogDtos(Collection<?> reiselogs) {
		List<ReiselogDto> list = new ArrayList<ReiselogDto>();
		if (reiselogs != null) {
			Iterator<?> iterator = reiselogs.iterator();
			while (iterator.hasNext()) {
				Reiselog reiselog = (Reiselog) iterator.next();
				list.add(assembleReiselogDto(reiselog));
			}
		}
		ReiselogDto[] returnArray = new ReiselogDto[list.size()];
		return (ReiselogDto[]) list.toArray(returnArray);
	}

	public Integer createTelefonzeiten(TelefonzeitenDto telefonzeitenDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (telefonzeitenDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("telefonzeitenDto == null"));
		}
		if (telefonzeitenDto.getPersonalIId() == null
				|| telefonzeitenDto.getTVon() == null
				|| telefonzeitenDto.getXKommentarext() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"telefonzeitenDto.getPersonalIId() == null || telefonzeitenDto.getTVon() == null || telefonzeitenDto.getXKommentarext() == null"));
		}
		try {
			Query query = em
					.createNamedQuery("TelefonzeitenfindByPersonalIIdTVon");
			query.setParameter(1, telefonzeitenDto.getPersonalIId());
			query.setParameter(2, telefonzeitenDto.getTVon());
			Telefonzeiten doppelt = (Telefonzeiten) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_TELEFONZEITEN.UK"));
		} catch (NoResultException ex) {

		}

		// SP3289
		bringeFehlerWennZeitabschlussvorhanden(
				telefonzeitenDto.getPersonalIId(), telefonzeitenDto.getTVon(),
				theClientDto);

		try {
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_TELEFONZEITEN);
			telefonzeitenDto.setIId(pk);

			Telefonzeiten telefonzeiten = new Telefonzeiten(
					telefonzeitenDto.getIId(),
					telefonzeitenDto.getPersonalIId(),
					telefonzeitenDto.getTVon(),
					telefonzeitenDto.getXKommentarext());
			em.persist(telefonzeiten);
			em.flush();
			setTelefonzeitenFromTelefonzeitenDto(telefonzeiten,
					telefonzeitenDto);

			Integer taetigeitIId_telefon = taetigkeitFindByCNr(
					ZeiterfassungFac.TAETIGKEIT_TELEFON, theClientDto).getIId();

			ZeitdatenDto zeitdatenDto = new ZeitdatenDto();
			zeitdatenDto.setTaetigkeitIId(taetigeitIId_telefon);
			zeitdatenDto.setPersonalIId(telefonzeitenDto.getPersonalIId());
			zeitdatenDto.setTZeit(telefonzeitenDto.getTVon());

			try {
				createZeitdaten(zeitdatenDto, false, false, false, false,
						theClientDto);
			} catch (EJBExceptionLP e) {
				if (e.getCode() == EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_ZEITBUCHUNGEN_VORHANDEN,
							new ArrayList<Object>(Arrays
									.asList(telefonzeitenDto.getTVon())),
							null);
				}
			}
			if (telefonzeitenDto.getTBis() != null) {
				zeitdatenDto.setTZeit(telefonzeitenDto.getTBis());
				try {
					createZeitdaten(zeitdatenDto, false, false, false, false,
							theClientDto);
				} catch (EJBExceptionLP e) {
					if (e.getCode() == EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_ZEITBUCHUNGEN_VORHANDEN,
								new ArrayList<Object>(
										Arrays.asList(telefonzeitenDto
												.getTBis())), e);
					}
				}
			}

			return telefonzeitenDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeTelefonzeiten(TelefonzeitenDto telefonzeitenDto)
			throws EJBExceptionLP {
		if (telefonzeitenDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("telefonzeitenDto == null"));
		}
		if (telefonzeitenDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception("telefonzeitenDto.getIId() == null"));
		}

		try {
			Telefonzeiten telefonzeiten = em.find(Telefonzeiten.class,
					telefonzeitenDto.getIId());
			if (telefonzeiten != null) {
				try {
					Query query = em
							.createNamedQuery("ZeitdatenfindByPersonalIIdTZeit");
					query.setParameter(1, telefonzeiten.getPersonalIId());
					query.setParameter(2, telefonzeiten.getTVon());
					Zeitdaten zeitdatenVon = (Zeitdaten) query
							.getSingleResult();
					if (zeitdatenVon != null) {
						em.remove(zeitdatenVon);
						em.flush();
					}
				} catch (NoResultException ex) {
					// Dann wurde sie geaendert
				}
				if (telefonzeiten.getTBis() != null) {
					try {
						Query query = em
								.createNamedQuery("ZeitdatenfindByPersonalIIdTZeit");
						query.setParameter(1, telefonzeiten.getPersonalIId());
						query.setParameter(2, telefonzeiten.getTBis());
						Zeitdaten zeitdatenBis = (Zeitdaten) query
								.getSingleResult();
						if (zeitdatenBis != null) {
							em.remove(zeitdatenBis);
						}
					} catch (NoResultException ex) {
						// Dann wurde sie geaendert
					}
				}

				em.remove(telefonzeiten);
				em.flush();
			}
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void updateTelefonzeiten(TelefonzeitenDto telefonzeitenDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (telefonzeitenDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("telefonzeitenDto == null"));
		}
		if (telefonzeitenDto.getIId() == null
				|| telefonzeitenDto.getPersonalIId() == null
				|| telefonzeitenDto.getTVon() == null
				|| telefonzeitenDto.getXKommentarext() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"telefonzeitenDto.getIId() == null || telefonzeitenDto.getPersonalIId() == null || telefonzeitenDto.getTVon() == null || telefonzeitenDto.getXKommentarext() == null"));
		}

		Integer iId = telefonzeitenDto.getIId();
		// try {
		Telefonzeiten telefonzeiten = em.find(Telefonzeiten.class, iId);
		if (telefonzeiten == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		try {
			Query query = em
					.createNamedQuery("TelefonzeitenfindByPersonalIIdTVon");
			query.setParameter(1, telefonzeitenDto.getPersonalIId());
			query.setParameter(2, telefonzeitenDto.getTVon());
			Integer iIdVorhanden = ((Telefonzeiten) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_TELEFONZEITEN.UK"));
			}
		} catch (NoResultException ex) {

		}

		Integer taetigeitIId_telefon = taetigkeitFindByCNr(
				ZeiterfassungFac.TAETIGKEIT_TELEFON, theClientDto).getIId();

		ZeitdatenDto zeitdatenDto = new ZeitdatenDto();
		zeitdatenDto.setTaetigkeitIId(taetigeitIId_telefon);
		zeitdatenDto.setPersonalIId(telefonzeitenDto.getPersonalIId());
		zeitdatenDto.setTZeit(telefonzeitenDto.getTVon());

		try {
			// Von-Zeitdaten in PERS_ZEITDATEN updaten
			Query query = em
					.createNamedQuery("ZeitdatenfindByPersonalIIdTZeit");
			query.setParameter(1, telefonzeiten.getPersonalIId());
			query.setParameter(2, telefonzeiten.getTVon());
			Zeitdaten zeitdaten = (Zeitdaten) query.getSingleResult();
			zeitdaten.setTZeit(telefonzeitenDto.getTVon());
			em.flush();
		} catch (NoResultException ex1) {
			// Dann hat es wer veraendert - neu anlegen
			createZeitdaten(zeitdatenDto, false, false, false, false,
					theClientDto);
		} catch (EntityExistsException ex) {
			EJBExceptionLP e = new EJBExceptionLP(
					EJBExceptionLP.FEHLER_ZEITBUCHUNGEN_VORHANDEN,
					new Exception("PERS_ZEITDATEN.UK"));
			e.setAlInfoForTheClient(new ArrayList<Object>(Arrays
					.asList(telefonzeitenDto.getTVon())));
			throw e;
		}

		zeitdatenDto.setTZeit(telefonzeitenDto.getTBis());
		try {
			// Bis-Zeitdaten in PERS_ZEITDATEN updaten
			Query query = em
					.createNamedQuery("ZeitdatenfindByPersonalIIdTZeit");
			query.setParameter(1, telefonzeiten.getPersonalIId());
			query.setParameter(2, telefonzeiten.getTBis());
			Zeitdaten zeitdaten = (Zeitdaten) query.getSingleResult();
			zeitdaten.setTZeit(telefonzeitenDto.getTBis());
			em.flush();
		} catch (NoResultException ex1) {
			// Dann hat es wer veraendert - neu anlegen
			createZeitdaten(zeitdatenDto, false, false, false, false,
					theClientDto);
		} catch (EntityExistsException ex) {
			EJBExceptionLP e = new EJBExceptionLP(
					EJBExceptionLP.FEHLER_ZEITBUCHUNGEN_VORHANDEN,
					new Exception("PERS_ZEITDATEN.UK"));
			e.setAlInfoForTheClient(new ArrayList(Arrays
					.asList(telefonzeitenDto.getTBis())));
			throw e;
		}
		// if (telefonzeiten.getTBis() != null) {
		// try {
		// // Zeitdaten in PERS_ZEITDATEN updaten
		// Query query = em
		// .createNamedQuery("ZeitdatenfindByPersonalIIdTZeit");
		// query.setParameter(1, telefonzeiten.getPersonalIId());
		// query.setParameter(2, telefonzeiten.getTBis());
		// Zeitdaten zeitdaten = (Zeitdaten) query.getSingleResult();
		// em.remove(zeitdaten);
		// em.flush();
		// } catch (NoResultException ex1) {
		// // Dann hat es wer veraendert - neu anlegen
		// } catch (EntityExistsException ex) {
		// throw new EJBExceptionLP(
		// EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
		// }

		setTelefonzeitenFromTelefonzeitenDto(telefonzeiten, telefonzeitenDto);
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }

	}

	public TelefonzeitenDto telefonzeitenFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Telefonzeiten telefonzeiten = em.find(Telefonzeiten.class, iId);
		if (telefonzeiten == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleTelefonzeitenDto(telefonzeiten);
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public TelefonzeitenDto[] telefonzeitenFindByPartnerIId(Integer iPartnerIId)
			throws EJBExceptionLP {
		if (iPartnerIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iPartnerIId == null"));
		}
		// try {
		Query query = em.createNamedQuery("TelefonzeitenfindByPartnerIId");
		query.setParameter(1, iPartnerIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		return assembleTelefonzeitenDtos(cl);
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public TelefonzeitenDto[] telefonzeitenFindByPartnerIIdOhneExc(
			Integer iPartnerIId) throws EJBExceptionLP {
		if (iPartnerIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iPartnerIId == null"));
		}
		// try {
		Query query = em.createNamedQuery("TelefonzeitenfindByPartnerIId");
		query.setParameter(1, iPartnerIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// myLogger.warn("partnerIId=" + iPartnerIId);
		// return null;
		// }
		return assembleTelefonzeitenDtos(cl);
		// }
		// catch (ObjectNotFoundException e) {
		// myLogger.warn("partnerIId=" + iPartnerIId, e);
		// return null;
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setTelefonzeitenFromTelefonzeitenDto(
			Telefonzeiten telefonzeiten, TelefonzeitenDto telefonzeitenDto) {
		telefonzeiten.setPersonalIId(telefonzeitenDto.getPersonalIId());
		telefonzeiten.setPartnerIId(telefonzeitenDto.getPartnerIId());
		telefonzeiten.setAnsprechpartnerIId(telefonzeitenDto
				.getAnsprechpartnerIId());
		telefonzeiten.setTVon(telefonzeitenDto.getTVon());
		telefonzeiten.setTBis(telefonzeitenDto.getTBis());
		telefonzeiten.setXKommentarext(telefonzeitenDto.getXKommentarext());
		telefonzeiten.setXKommentarint(telefonzeitenDto.getXKommentarint());
		telefonzeiten.setProjektIId(telefonzeitenDto.getProjektIId());
		em.merge(telefonzeiten);
		em.flush();
	}

	private TelefonzeitenDto assembleTelefonzeitenDto(
			Telefonzeiten telefonzeiten) {
		return TelefonzeitenDtoAssembler.createDto(telefonzeiten);
	}

	private TelefonzeitenDto[] assembleTelefonzeitenDtos(
			Collection<?> telefonzeitens) {
		List<TelefonzeitenDto> list = new ArrayList<TelefonzeitenDto>();
		if (telefonzeitens != null) {
			Iterator<?> iterator = telefonzeitens.iterator();
			while (iterator.hasNext()) {
				Telefonzeiten telefonzeiten = (Telefonzeiten) iterator.next();
				list.add(assembleTelefonzeitenDto(telefonzeiten));
			}
		}
		TelefonzeitenDto[] returnArray = new TelefonzeitenDto[list.size()];
		return (TelefonzeitenDto[]) list.toArray(returnArray);
	}

	public Integer createDiaeten(DiaetenDto diaetenDto) throws EJBExceptionLP {
		if (diaetenDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("tagesartDto == null"));
		}
		if (diaetenDto.getCBez() == null || diaetenDto.getLandIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"diaetenDto.getCBez() == null || diaetenDto.getLandIId() == null"));
		}
		try {
			Query query = em.createNamedQuery("DiaetenfindByCBez");
			query.setParameter(1, diaetenDto.getCBez());
			Diaeten doppelt = (Diaeten) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_DIAETEN.C_BEZ"));
		} catch (NoResultException ex) {

		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_DIAETEN);
			diaetenDto.setIId(pk);

			Diaeten diaeten = new Diaeten(diaetenDto.getIId(),
					diaetenDto.getCBez(), diaetenDto.getLandIId());
			em.persist(diaeten);
			em.flush();
			setDiaetenFromDiaetenDto(diaeten, diaetenDto);
			return diaetenDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeDiaeten(DiaetenDto diaetenDto) throws EJBExceptionLP {
		if (diaetenDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("diaetenDto == null"));
		}
		if (diaetenDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Diaeten toRemove = em.find(Diaeten.class, diaetenDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN,
		// e);
		// }

	}

	public void updateDiaeten(DiaetenDto diaetenDto) throws EJBExceptionLP {
		if (diaetenDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("tagesartDto == null"));
		}
		if (diaetenDto.getIId() == null || diaetenDto.getCBez() == null
				|| diaetenDto.getLandIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"diaetenDto.getIId() == null || diaetenDto.getCBez() == null || diaetenDto.getLandIId() == null"));
		}

		Integer iId = diaetenDto.getIId();
		// try {
		Diaeten diaeten = em.find(Diaeten.class, iId);
		if (diaeten == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}

		try {
			Query query = em.createNamedQuery("DiaetenfindByCBez");
			query.setParameter(1, diaetenDto.getCBez());
			Integer iIdVorhanden = ((Diaeten) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_DIAETEN.CBEZ"));
			}
		} catch (NoResultException ex) {

		}

		setDiaetenFromDiaetenDto(diaeten, diaetenDto);
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);

		// }
	}

	public DiaetenDto[] diaetenFindByLandIId(Integer landIId)
			throws EJBExceptionLP {
		if (landIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("landIId == null"));
		}

		// try {
		Query query = em.createNamedQuery("DiaetenfindByLandIId");
		query.setParameter(1, landIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		return assembleDiaetenDtos(cl);
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public DiaetenDto diaetenFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Diaeten diaeten = em.find(Diaeten.class, iId);
		if (diaeten == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleDiaetenDto(diaeten);
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setDiaetenFromDiaetenDto(Diaeten diaeten, DiaetenDto diaetenDto) {
		diaeten.setCBez(diaetenDto.getCBez());
		diaeten.setLandIId(diaetenDto.getLandIId());
		em.merge(diaeten);
		em.flush();
	}

	private DiaetenDto assembleDiaetenDto(Diaeten diaeten) {
		return DiaetenDtoAssembler.createDto(diaeten);
	}

	private DiaetenDto[] assembleDiaetenDtos(Collection<?> diaetens) {
		List<DiaetenDto> list = new ArrayList<DiaetenDto>();
		if (diaetens != null) {
			Iterator<?> iterator = diaetens.iterator();
			while (iterator.hasNext()) {
				Diaeten diaeten = (Diaeten) iterator.next();
				list.add(assembleDiaetenDto(diaeten));
			}
		}
		DiaetenDto[] returnArray = new DiaetenDto[list.size()];
		return (DiaetenDto[]) list.toArray(returnArray);
	}

	public Integer createDiaetentagessatz(
			DiaetentagessatzDto diaetentagessatzDto) throws EJBExceptionLP {
		if (diaetentagessatzDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("diaetentagessatzDto == null"));
		}
		if (diaetentagessatzDto.getDiaetenIId() == null
				|| diaetentagessatzDto.getBStundenweise() == null
				|| diaetentagessatzDto.getIAbstunden() == null
				|| diaetentagessatzDto.getNMindestsatz() == null
				|| diaetentagessatzDto.getNStundensatz() == null
				|| diaetentagessatzDto.getNTagessatz() == null
				|| diaetentagessatzDto.getTGueltigab() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"diaetentagessatzDto.getDiaetenIId() == null || diaetentagessatzDto.getBStundenweise() == null || diaetentagessatzDto.getIAbstunden() == null || diaetentagessatzDto.getNMindestsatz() == null || diaetentagessatzDto.getNStundensatz() == null || diaetentagessatzDto.getNTagessatz() == null || diaetentagessatzDto.getTGueltigab() == null"));
		}

		diaetentagessatzDto.setTGueltigab(Helper
				.cutTimestamp(diaetentagessatzDto.getTGueltigab()));

		diaetentagessatzDto.setNMindestsatz(Helper.rundeKaufmaennisch(
				diaetentagessatzDto.getNMindestsatz(), 6));
		diaetentagessatzDto.setNStundensatz(Helper.rundeKaufmaennisch(
				diaetentagessatzDto.getNStundensatz(), 6));
		diaetentagessatzDto.setNTagessatz(Helper.rundeKaufmaennisch(
				diaetentagessatzDto.getNTagessatz(), 6));

		try {
			Query query = em
					.createNamedQuery("DiaetentagessatzfindByDiatenIIdTGueltigab");
			query.setParameter(1, diaetentagessatzDto.getDiaetenIId());
			query.setParameter(2, diaetentagessatzDto.getTGueltigab());
			Diaetentagessatz doppelt = (Diaetentagessatz) query
					.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_DIAETENTAGESSATZ.UK"));
		} catch (NoResultException ex) {

		}
		if (Helper.short2Boolean(diaetentagessatzDto.getBStundenweise()) == false) {
			diaetentagessatzDto.setIAbstunden(0);
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_DIAETENTAGESSATZ);
			diaetentagessatzDto.setIId(pk);

			Diaetentagessatz diaetentagessatz = new Diaetentagessatz(
					diaetentagessatzDto.getIId(),
					diaetentagessatzDto.getDiaetenIId(),
					diaetentagessatzDto.getTGueltigab(),
					diaetentagessatzDto.getIAbstunden(),
					diaetentagessatzDto.getBStundenweise(),
					diaetentagessatzDto.getNStundensatz(),
					diaetentagessatzDto.getNTagessatz(),
					diaetentagessatzDto.getNMindestsatz());
			em.persist(diaetentagessatz);
			em.flush();
			setDiaetentagessatzFromDiaetentagessatzDto(diaetentagessatz,
					diaetentagessatzDto);
			return diaetentagessatzDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeDiaetentagessatz(DiaetentagessatzDto diaetentagessatzDto)
			throws EJBExceptionLP {
		if (diaetentagessatzDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("diaetentagessatzDto == null"));
		}
		if (diaetentagessatzDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Diaetentagessatz toRemove = em.find(Diaetentagessatz.class,
				diaetentagessatzDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN,
		// e);
		// }
	}

	public void updateDiaetentagessatz(DiaetentagessatzDto diaetentagessatzDto)
			throws EJBExceptionLP {
		if (diaetentagessatzDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("diaetentagessatzDto == null"));
		}
		if (diaetentagessatzDto.getIId() == null
				|| diaetentagessatzDto.getDiaetenIId() == null
				|| diaetentagessatzDto.getBStundenweise() == null
				|| diaetentagessatzDto.getIAbstunden() == null
				|| diaetentagessatzDto.getNMindestsatz() == null
				|| diaetentagessatzDto.getNStundensatz() == null
				|| diaetentagessatzDto.getNTagessatz() == null
				|| diaetentagessatzDto.getTGueltigab() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"diaetentagessatzDto.getIId() == null || diaetentagessatzDto.getDiaetenIId() == null || diaetentagessatzDto.getBStundenweise() == null || diaetentagessatzDto.getIAbstunden() == null || diaetentagessatzDto.getNMindestsatz() == null || diaetentagessatzDto.getNStundensatz() == null || diaetentagessatzDto.getNTagessatz() == null || diaetentagessatzDto.getTGueltigab() == null"));
		}
		if (Helper.short2Boolean(diaetentagessatzDto.getBStundenweise()) == false) {
			diaetentagessatzDto.setIAbstunden(0);
		}
		diaetentagessatzDto.setTGueltigab(Helper
				.cutTimestamp(diaetentagessatzDto.getTGueltigab()));

		diaetentagessatzDto.setNMindestsatz(Helper.rundeKaufmaennisch(
				diaetentagessatzDto.getNMindestsatz(), 6));
		diaetentagessatzDto.setNStundensatz(Helper.rundeKaufmaennisch(
				diaetentagessatzDto.getNStundensatz(), 6));
		diaetentagessatzDto.setNTagessatz(Helper.rundeKaufmaennisch(
				diaetentagessatzDto.getNTagessatz(), 6));

		Integer iId = diaetentagessatzDto.getIId();
		// try {
		Diaetentagessatz diaetentagessatz = em
				.find(Diaetentagessatz.class, iId);
		if (diaetentagessatz == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}
		try {
			Query query = em
					.createNamedQuery("DiaetentagessatzfindByDiatenIIdTGueltigab");
			query.setParameter(1, diaetentagessatzDto.getDiaetenIId());
			query.setParameter(2, diaetentagessatzDto.getTGueltigab());
			Integer iIdVorhanden = ((Diaetentagessatz) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"PERS_DIAETENTAGESSATZ.UK"));
			}

		} catch (NoResultException ex) {

		}

		setDiaetentagessatzFromDiaetentagessatzDto(diaetentagessatz,
				diaetentagessatzDto);
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);

		// }

	}

	public DiaetentagessatzDto diaetentagessatzFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		// try {
		Diaetentagessatz diaetentagessatz = em
				.find(Diaetentagessatz.class, iId);
		if (diaetentagessatz == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleDiaetentagessatzDto(diaetentagessatz);
		// }
		// catch (NoResultException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public DiaetentagessatzDto[] diaetentagessatzFindGueltigenTagessatzZuDatum(
			Integer diaetenIId, Timestamp tVon) {

		Query query2 = em
				.createNamedQuery("DiaetentagessatzfindGueltigenTagessatzZuDatum");
		query2.setParameter(1, diaetenIId);
		query2.setParameter(2, tVon);

		return assembleDiaetentagessatzDtos(query2.getResultList());
	}

	private void setDiaetentagessatzFromDiaetentagessatzDto(
			Diaetentagessatz diaetentagessatz,
			DiaetentagessatzDto diaetentagessatzDto) {
		diaetentagessatz.setDiaetenIId(diaetentagessatzDto.getDiaetenIId());
		diaetentagessatz.setTGueltigab(diaetentagessatzDto.getTGueltigab());
		diaetentagessatz.setIAbstunden(diaetentagessatzDto.getIAbstunden());
		diaetentagessatz.setBStundenweise(diaetentagessatzDto
				.getBStundenweise());
		diaetentagessatz.setNStundensatz(diaetentagessatzDto.getNStundensatz());
		diaetentagessatz.setNTagessatz(diaetentagessatzDto.getNTagessatz());
		diaetentagessatz.setNMindestsatz(diaetentagessatzDto.getNMindestsatz());
		diaetentagessatz.setCFilenameScript(diaetentagessatzDto
				.getCFilenameScript());
		em.merge(diaetentagessatz);
		em.flush();
	}

	private DiaetentagessatzDto assembleDiaetentagessatzDto(
			Diaetentagessatz diaetentagessatz) {
		return DiaetentagessatzDtoAssembler.createDto(diaetentagessatz);
	}

	private DiaetentagessatzDto[] assembleDiaetentagessatzDtos(
			Collection<?> diaetentagessatzs) {
		List<DiaetentagessatzDto> list = new ArrayList<DiaetentagessatzDto>();
		if (diaetentagessatzs != null) {
			Iterator<?> iterator = diaetentagessatzs.iterator();
			while (iterator.hasNext()) {
				Diaetentagessatz diaetentagessatz = (Diaetentagessatz) iterator
						.next();
				list.add(assembleDiaetentagessatzDto(diaetentagessatz));
			}
		}
		DiaetentagessatzDto[] returnArray = new DiaetentagessatzDto[list.size()];
		return (DiaetentagessatzDto[]) list.toArray(returnArray);
	}

	public void removeReise(Integer iId) throws EJBExceptionLP {
		// try {
		Reise toRemove = em.find(Reise.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (Exception e) {
		// throw new EJBException(e.getMessage());
		// }
	}

	public BigDecimal getKmKostenEinerReise(ReiseKomplettDto rkDto,
			TheClientDto theClientDto) {
		TreeMap tmBeginn = rkDto.getTmReiseBeginn();
		Iterator it = tmBeginn.keySet().iterator();

		BigDecimal kmKosten = new BigDecimal(0);

		while (it.hasNext()) {
			ReiseDto rDto = (ReiseDto) tmBeginn.get(it.next());

			if (rkDto.getReiseEnde().getIKmbeginn() != null
					&& rkDto.getReiseEnde().getIKmende() != null) {
				Integer iKm = rkDto.getReiseEnde().getIKmende()
						- rkDto.getReiseEnde().getIKmbeginn();

				if (rkDto.getReiseEnde().getCFahrzeug() != null) {
					// KM-Kosten aus Personalgehalt

					Calendar c = Calendar.getInstance();
					c.setTimeInMillis(rDto.getTZeit().getTime());

					try {
						PersonalgehaltDto pgDto = getPersonalFac()
								.personalgehaltFindLetztePersonalgehalt(
										rDto.getPersonalIId(),
										c.get(Calendar.YEAR),
										c.get(Calendar.MONTH));

						if (pgDto != null) {
							BigDecimal kmGeld1 = pgDto.getNKmgeld1();
							if (kmGeld1 != null) {

								if (pgDto.getFBiskilometer() != null) {

									if (iKm <= pgDto.getFBiskilometer()
											|| pgDto.getFBiskilometer() == 0) {
										kmKosten = kmKosten.add(kmGeld1
												.multiply(new BigDecimal(iKm
														.doubleValue())));
									} else {

										kmKosten = kmKosten.add(kmGeld1
												.multiply(new BigDecimal(pgDto
														.getFBiskilometer())));
										if (pgDto.getNKmgeld2() != null) {
											Integer km2 = iKm
													- pgDto.getFBiskilometer()
															.intValue();
											kmKosten = kmKosten
													.add(pgDto
															.getNKmgeld2()
															.multiply(
																	new BigDecimal(
																			km2)));
										}
									}

								} else {
									kmKosten = kmKosten
											.multiply(new BigDecimal(iKm
													.doubleValue()));
								}

							}
						}
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				} else if (rkDto.getReiseEnde().getFahrzeugIId() != null) {
					// KM-Kosten aus Fahrzeug

					BigDecimal bdKMKostenFahrzeug = getPersonalFac()
							.getKMKostenInZielwaehrung(
									rkDto.getReiseEnde().getFahrzeugIId(),
									rDto.getTZeit(),
									theClientDto.getSMandantenwaehrung(),
									theClientDto);

					if (bdKMKostenFahrzeug != null) {
						kmKosten = bdKMKostenFahrzeug.multiply(new BigDecimal(
								iKm));
					}

				}

			}

		}
		return kmKosten;
	}

	public void updateReise(ReiseDto reiseDto) throws EJBExceptionLP {
		if (reiseDto != null) {
			Integer iId = reiseDto.getIId();
			try {
				Reise reise = em.find(Reise.class, iId);
				if (reise == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
				}
				setReiseFromReiseDto(reise, reiseDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public void updateReises(ReiseDto[] reiseDtos) throws EJBExceptionLP {
		if (reiseDtos != null) {
			for (int i = 0; i < reiseDtos.length; i++) {
				updateReise(reiseDtos[i]);
			}
		}
	}

	public ReiseDto reiseFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		// try {
		Reise reise = em.find(Reise.class, iId);
		if (reise == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleReiseDto(reise);
		// }
		// catch (NoResultException fe) {
		// throw fe;
		// }
		// catch (Exception e) {
		// throw new EJBException(e.getMessage());
		// }
	}

	public ReiseDto reiseFindByPersonalIIdTZeit(Integer personalIId,
			Timestamp tZeit) throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("ReisefindByPersonalIIdTZeit");
		query.setParameter(1, personalIId);
		query.setParameter(2, tZeit);
		Reise reise = (Reise) query.getSingleResult();
		if (reise == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
		}

		return assembleReiseDto(reise);
		// }
		// catch (NoResultException fe) {
		// throw fe;
		// }
		// catch (Exception e) {
		// throw new EJBException(e.getMessage());
		// }
	}

	public ReiseDto[] reiseFindByPartnerIId(Integer partnerIIId)
			throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("ReisefindByPartnerIId");
		query.setParameter(1, partnerIIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,null);
		// }
		return assembleReiseDtos(cl);
		// }
		// catch (NoResultException fe) {
		// throw fe;
		// }
		// catch (Exception e) {
		// throw new EJBException(e.getMessage());
		// }
	}

	public ReiseDto[] reiseFindByAnsprechpartnerIId(Integer IansprechpartnerIId)
			throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("ReisefindByAnsprechpartnerIId");
		query.setParameter(1, IansprechpartnerIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,null);
		// }
		return assembleReiseDtos(cl);
		// }
		// catch (NoResultException fe) {
		// throw fe;
		// }
		// catch (Exception e) {
		// throw new EJBException(e.getMessage());
		// }
	}

	public void createReiselog(ReiselogDto reiselogDto) throws EJBExceptionLP {
		if (reiselogDto == null) {
			return;
		}
		try {
			Reiselog reiselog = new Reiselog(reiselogDto.getIId(),
					reiselogDto.getReiseIId(), reiselogDto.getPersonalIId(),
					reiselogDto.getTZeit(), reiselogDto.getBBeginn(),
					reiselogDto.getPersonalIIdAendern(), reiselogDto.getCArt());
			em.persist(reiselog);
			em.flush();
			setReiselogFromReiselogDto(reiselog, reiselogDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeReiselog(Integer iId) throws EJBExceptionLP {
		// try {
		Reiselog toRemove = em.find(Reiselog.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (Exception e) {
		// throw new EJBException(e.getMessage());
		// }
	}

	public void removeReiselog(ReiselogDto reiselogDto) throws EJBExceptionLP {
		if (reiselogDto != null) {
			Integer iId = reiselogDto.getIId();
			removeReiselog(iId);
		}
	}

	public void updateReiselog(ReiselogDto reiselogDto) throws EJBExceptionLP {
		if (reiselogDto != null) {
			Integer iId = reiselogDto.getIId();
			try {
				Reiselog reiselog = em.find(Reiselog.class, iId);
				setReiselogFromReiselogDto(reiselog, reiselogDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public void updateReiselogs(ReiselogDto[] reiselogDtos)
			throws EJBExceptionLP {
		if (reiselogDtos != null) {
			for (int i = 0; i < reiselogDtos.length; i++) {
				updateReiselog(reiselogDtos[i]);
			}
		}
	}

	public ReiselogDto reiselogFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		// try {
		Reiselog reiselog = em.find(Reiselog.class, iId);
		if (reiselog == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleReiselogDto(reiselog);
		// }
		// catch (NoResultException fe) {
		// throw fe;
		// }
		// catch (Exception e) {
		// throw new EJBException(e.getMessage());
		// }
	}

	public ReiselogDto[] reiselogFindByPartnerIId(Integer partnerIId)
			throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("ReiselogfindByPartnerIId");
		query.setParameter(1, partnerIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,null);
		// }
		return assembleReiselogDtos(cl);
		// }
		// catch (NoResultException fe) {
		// throw fe;
		// }
		// catch (Exception e) {
		// throw new EJBException(e.getMessage());
		// }
	}

	public ReiselogDto[] reiselogFindByAnsprechpartnerIId(
			Integer iAnsprechpartnerIId) throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("ReiselogfindByAnsprechpartnerIId");
		query.setParameter(1, iAnsprechpartnerIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,null);
		// }
		return assembleReiselogDtos(cl);
		// }
		// catch (NoResultException fe) {
		// throw fe;
		// }
		// catch (Exception e) {
		// throw new EJBException(e.getMessage());
		// }
	}

	public void createTelefonzeiten(TelefonzeitenDto telefonzeitenDto)
			throws EJBExceptionLP {
		if (telefonzeitenDto == null) {
			return;
		}
		try {
			Telefonzeiten telefonzeiten = new Telefonzeiten(
					telefonzeitenDto.getIId(),
					telefonzeitenDto.getPersonalIId(),
					telefonzeitenDto.getTVon(),
					telefonzeitenDto.getXKommentarext());
			em.persist(telefonzeiten);
			em.flush();
			setTelefonzeitenFromTelefonzeitenDto(telefonzeiten,
					telefonzeitenDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeTelefonzeiten(Integer iId) throws EJBExceptionLP {
		// try {
		Telefonzeiten toRemove = em.find(Telefonzeiten.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (Exception e) {
		// throw new EJBException(e.getMessage());
		// }
	}

	public void updateTelefonzeiten(TelefonzeitenDto telefonzeitenDto)
			throws EJBExceptionLP {
		if (telefonzeitenDto != null) {
			Integer iId = telefonzeitenDto.getIId();
			try {
				Telefonzeiten telefonzeiten = em.find(Telefonzeiten.class, iId);
				setTelefonzeitenFromTelefonzeitenDto(telefonzeiten,
						telefonzeitenDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public void updateTelefonzeitens(TelefonzeitenDto[] telefonzeitenDtos)
			throws EJBExceptionLP {
		if (telefonzeitenDtos != null) {
			for (int i = 0; i < telefonzeitenDtos.length; i++) {
				updateTelefonzeiten(telefonzeitenDtos[i]);
			}
		}
	}

	public TelefonzeitenDto telefonzeitenFindByPersonalIIdTVon(
			Integer personalIId, Timestamp tVon) throws EJBExceptionLP {
		try {
			Query query = em
					.createNamedQuery("TelefonzeitenfindByPersonalIIdTVon");
			query.setParameter(1, personalIId);
			query.setParameter(2, tVon);
			Telefonzeiten telefonzeiten = (Telefonzeiten) query
					.getSingleResult();
			if (telefonzeiten == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
			}
			return assembleTelefonzeitenDto(telefonzeiten);
		} catch (NoResultException fe) {
			throw fe;
		}
		// catch (Exception e) {
		// throw new EJBException(e.getMessage());
		// }
	}

	public TelefonzeitenDto[] telefonzeitenFindByAnsprechpartnerIId(
			Integer iAnsprechpartnerIId) throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("TelefonzeitenfindByAnsprechpartnerIId");
		query.setParameter(1, iAnsprechpartnerIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,null);
		// }
		return assembleTelefonzeitenDtos(cl);
		// }
		// catch (NoResultException fe) {
		// throw fe;
		// }
		// catch (Exception e) {
		// throw new EJBException(e.getMessage());
		// }
	}

	public Integer createBereitschaft(BereitschaftDto dto) {

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_BEREITSCHAFT);
			dto.setIId(pk);

			Bereitschaft bean = new Bereitschaft(dto.getIId(),
					dto.getBereitschaftartIId(), dto.getPersonalIId(),
					dto.getTBeginn(), dto.getTEnde());
			em.persist(bean);
			em.flush();
			setBereitschaftFromIsBereitschaftDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public BereitschaftDto bereitschaftFindByPrimaryKey(Integer iId) {
		Bereitschaft ialle = em.find(Bereitschaft.class, iId);
		return BereitschaftDtoAssembler.createDto(ialle);
	}

	public void updateBereitschaft(BereitschaftDto dto) {
		Bereitschaft ialle = em.find(Bereitschaft.class, dto.getIId());

		setBereitschaftFromIsBereitschaftDto(ialle, dto);
	}

	public void removeBereitschaft(BereitschaftDto dto) {
		Bereitschaft toRemove = em.find(Bereitschaft.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	private void setBereitschaftFromIsBereitschaftDto(Bereitschaft bean,
			BereitschaftDto dto) {
		bean.setBereitschaftartIId(dto.getBereitschaftartIId());
		bean.setPersonalIId(dto.getPersonalIId());
		bean.setTBeginn(dto.getTBeginn());
		bean.setTEnde(dto.getTEnde());
		bean.setCBemerkung(dto.getCBemerkung());
		em.merge(bean);
		em.flush();
	}

	private void vergleicheZeitdatenDtoVorherNachherUndLoggeAenderungen(
			ZeitdatenDto zeitdatenDto, TheClientDto theClientDto) {
		ZeitdatenDto stuecklisteDto_vorher = zeitdatenFindByPrimaryKey(
				zeitdatenDto.getIId(), theClientDto);

		HvDtoLogger<ZeitdatenDto> zeitdatenLogger = new HvDtoLogger<ZeitdatenDto>(
				em, zeitdatenDto.getPersonalIId(), theClientDto);
		zeitdatenLogger.log(stuecklisteDto_vorher, zeitdatenDto);
	}

	/**
	 * Hole alle Sondertaetigkeiten nach Spr.
	 *
	 * @param cNrSpracheI
	 *            String
	 * @throws EJBExceptionLP
	 * @return Map
	 */
	public Map getAllSprSondertaetigkeiten(String cNrSpracheI) {
		return buildSprMap(getAllSondertaetigkeitenRaw(), cNrSpracheI);
	}

	public Map getAllSprSondertaetigkeitenNurBDEBuchbar(String cNrSpracheI) {
		SondertaetigkeitFilter filter = new SondertaetigkeitFilter();
		return buildSprMap(
				filter.filterNurBdeBebuchbar(getAllSondertaetigkeitenRaw()),
				cNrSpracheI);
	}

	public Map<Integer, String> getAllSprSondertaetigkeitenOhneVersteckt(
			String cNrSpracheI) {
		SondertaetigkeitFilter filter = new SondertaetigkeitFilter();
		return buildSprMap(
				filter.filterOhneVersteckte(getAllSondertaetigkeitenRaw()),
				cNrSpracheI);
	}

	public Map<Integer, String> getAllSprSondertaetigkeitenNurBDEBuchbarOhneVersteckt(
			String cNrSpracheI) {
		SondertaetigkeitFilter filter = new SondertaetigkeitFilter();
		return buildSprMap(filter.filterOhneVersteckte(filter
				.filterNurBdeBebuchbar(getAllSondertaetigkeitenRaw())),
				cNrSpracheI);
	}

	private List<Taetigkeit> getAllSondertaetigkeitenRaw() {
		List<Taetigkeit> bebuchbare = new ArrayList<Taetigkeit>();
		HvTypedQuery<Taetigkeit> query = new HvTypedQuery<Taetigkeit>(
				em.createNamedQuery("TaetigkeitfindAll"));
		for (Taetigkeit taetigkeit : query.getResultList()) {
			if (!ZeiterfassungFac.TAETIGKEITART_SONDERTAETIGKEIT
					.equals(taetigkeit.getTaetigkeitartCNr()))
				continue;
			if (Helper.short2boolean(taetigkeit.getBTagbuchbar()))
				continue;

			bebuchbare.add(taetigkeit);
		}

		return bebuchbare;
	}

	private Map<Integer, String> buildSprMap(List<Taetigkeit> entries,
			String cNrSprache) {
		Map<Integer, String> tmArten = new LinkedHashMap<Integer, String>();
		for (Taetigkeit taetigkeit : entries) {
			String value = taetigkeit.getCNr();
			Taetigkeitspr taetigkeitspr = em.find(Taetigkeitspr.class,
					new TaetigkeitsprPK(taetigkeit.getIId(), cNrSprache));
			if (taetigkeitspr != null && taetigkeitspr.getCBez() != null) {
				value = taetigkeitspr.getCBez();
			}
			tmArten.put(taetigkeit.getIId(), value.trim());
		}
		return tmArten;
	}

	public ArrayList<ReiseKomplettDto> holeReisenKomplett(Integer fahrzeugIId,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			TheClientDto theClientDto) {
		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session.createCriteria(FLRReise.class);

		crit.add(Restrictions.eq(ZeiterfassungFac.FLR_REISE_B_BEGINN,
				Helper.boolean2Short(false)));

		if (fahrzeugIId != null) {
			crit.add(Restrictions.eq(ZeiterfassungFac.FLR_REISE_FAHRZEUG_I_ID,
					fahrzeugIId));
		} else {
			crit.add(Restrictions
					.isNotNull(ZeiterfassungFac.FLR_REISE_FAHRZEUG_I_ID));
		}

		crit.add(Restrictions.ge(ZeiterfassungFac.FLR_REISE_T_ZEIT, tVon));
		crit.add(Restrictions.lt(ZeiterfassungFac.FLR_REISE_T_ZEIT, tBis));

		crit.createAlias("flrfahrzeug", "f").addOrder(Order.asc("f.c_bez"));

		List<?> resultList = crit.list();
		Iterator<?> resultListIterator = resultList.iterator();

		ArrayList<ReiseKomplettDto> alReisen = new ArrayList<ReiseKomplettDto>();

		while (resultListIterator.hasNext()) {
			FLRReise flrReise = (FLRReise) resultListIterator.next();
			ArrayList<ReiseKomplettDto> alReisenTemp = holeReiseKomplettAnhandEndeBuchung(
					tVon, flrReise, theClientDto);
			alReisen.addAll(alReisenTemp);
		}

		session.close();
		return alReisen;
	}

	public ArrayList<ReiseKomplettDto> holeReisenKomplett(String belegartCNr,
			Integer belegartIId, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, TheClientDto theClientDto) {
		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session.createCriteria(FLRReise.class);

		crit.add(Restrictions.eq(ZeiterfassungFac.FLR_REISE_B_BEGINN,
				Helper.boolean2Short(true)));

		crit.add(Restrictions.eq(ZeiterfassungFac.FLR_REISE_BELEGART_C_NR,
				belegartCNr));

		crit.add(Restrictions.eq(ZeiterfassungFac.FLR_REISE_I_BELEGARTID,
				belegartIId));

		if (tVon != null) {
			crit.add(Restrictions.ge(ZeiterfassungFac.FLR_REISE_T_ZEIT, tVon));
		}
		if (tBis != null) {
			crit.add(Restrictions.lt(ZeiterfassungFac.FLR_REISE_T_ZEIT, tBis));
		}
		crit.addOrder(Order.asc(ZeiterfassungFac.FLR_REISE_T_ZEIT));

		List<?> resultList = crit.list();
		Iterator<?> resultListIterator = resultList.iterator();

		ArrayList<ReiseKomplettDto> alReisen = new ArrayList<ReiseKomplettDto>();

		HashMap<Integer, String> hmReiseBereitsGefunden = new HashMap<Integer, String>();

		while (resultListIterator.hasNext()) {
			FLRReise flrReise = (FLRReise) resultListIterator.next();
			// Nun das zugehoerige Ende suchen
			Session session2 = FLRSessionFactory.getFactory().openSession();
			org.hibernate.Criteria critNaechstesEnde = session2
					.createCriteria(FLRReise.class);
			critNaechstesEnde.add(Restrictions.gt(
					ZeiterfassungFac.FLR_REISE_T_ZEIT, flrReise.getT_zeit()));
			critNaechstesEnde.add(Restrictions.eq(
					ZeiterfassungFac.FLR_REISE_PERSONAL_I_ID,
					flrReise.getPersonal_i_id()));
			critNaechstesEnde.add(Restrictions.eq(
					ZeiterfassungFac.FLR_REISE_B_BEGINN,
					Helper.boolean2Short(false)));
			critNaechstesEnde.addOrder(Order
					.asc(ZeiterfassungFac.FLR_REISE_T_ZEIT));
			critNaechstesEnde.setMaxResults(1);
			List<?> resultListNaechstesEnde = critNaechstesEnde.list();
			Iterator<?> resultListIteratortNaechstesEnde = resultListNaechstesEnde
					.iterator();
			if (resultListIteratortNaechstesEnde.hasNext()) {
				FLRReise flrReiseEnde = (FLRReise) resultListIteratortNaechstesEnde
						.next();

				if (!hmReiseBereitsGefunden.containsKey(flrReiseEnde.getI_id())) {
					ArrayList<ReiseKomplettDto> alReisenTemp = holeReiseKomplettAnhandEndeBuchung(
							tVon, flrReiseEnde, theClientDto);
					alReisen.addAll(alReisenTemp);

					hmReiseBereitsGefunden.put(flrReiseEnde.getI_id(), "");
				}

			}
			session2.close();

		}

		session.close();
		return alReisen;
	}

	private ArrayList<ReiseKomplettDto> holeReiseKomplettAnhandEndeBuchung(
			java.sql.Timestamp tVon, FLRReise flrReise,
			TheClientDto theClientDto) {

		ArrayList<ReiseKomplettDto> alReisen = new ArrayList<ReiseKomplettDto>();

		// Das muss ein Ende-eintrag sein

		// Nun den naechsten Ende eintrag der Person suchen
		Session session2 = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria critVorherigesEnde = session2
				.createCriteria(FLRReise.class);

		critVorherigesEnde.add(Restrictions.eq(
				ZeiterfassungFac.FLR_REISE_B_BEGINN,
				Helper.boolean2Short(false)));
		critVorherigesEnde.add(Restrictions.eq(
				ZeiterfassungFac.FLR_REISE_PERSONAL_I_ID,
				flrReise.getPersonal_i_id()));
		critVorherigesEnde.add(Restrictions.lt(
				ZeiterfassungFac.FLR_REISE_T_ZEIT, flrReise.getT_zeit()));
		critVorherigesEnde.addOrder(Order
				.desc(ZeiterfassungFac.FLR_REISE_T_ZEIT));
		critVorherigesEnde.setMaxResults(1);
		List<?> resultListVorherigesEnde = critVorherigesEnde.list();

		// Wenns keinen gibt, dann den allerersten suchen
		boolean bEsGibtKeinVorherigesEnde = false;
		if (resultListVorherigesEnde.size() == 0) {
			bEsGibtKeinVorherigesEnde = true;
			critVorherigesEnde = session2.createCriteria(FLRReise.class);

			critVorherigesEnde.add(Restrictions.eq(
					ZeiterfassungFac.FLR_REISE_B_BEGINN,
					Helper.boolean2Short(true)));
			critVorherigesEnde.add(Restrictions.eq(
					ZeiterfassungFac.FLR_REISE_PERSONAL_I_ID,
					flrReise.getPersonal_i_id()));
			critVorherigesEnde.add(Restrictions.lt(
					ZeiterfassungFac.FLR_REISE_T_ZEIT, flrReise.getT_zeit()));
			critVorherigesEnde.addOrder(Order
					.asc(ZeiterfassungFac.FLR_REISE_T_ZEIT));
			critVorherigesEnde.setMaxResults(1);
			resultListVorherigesEnde = critVorherigesEnde.list();

		}

		Iterator<?> resultListIteratorVorherigesEnde = resultListVorherigesEnde
				.iterator();
		while (resultListIteratorVorherigesEnde.hasNext()) {
			FLRReise flrReiseVorherigesEnde = (FLRReise) resultListIteratorVorherigesEnde
					.next();
			// Alle Reisezeiten dazwischen sind gesucht

			Session session3 = FLRSessionFactory.getFactory().openSession();
			org.hibernate.Criteria critReiseKomplett = session3
					.createCriteria(FLRReise.class);

			critReiseKomplett.add(Restrictions.eq(
					ZeiterfassungFac.FLR_REISE_PERSONAL_I_ID,
					flrReise.getPersonal_i_id()));

			if (bEsGibtKeinVorherigesEnde) {
				critReiseKomplett.add(Restrictions.ge(
						ZeiterfassungFac.FLR_REISE_T_ZEIT,
						flrReiseVorherigesEnde.getT_zeit()));
			} else {
				critReiseKomplett.add(Restrictions.gt(
						ZeiterfassungFac.FLR_REISE_T_ZEIT,
						flrReiseVorherigesEnde.getT_zeit()));
			}

			critReiseKomplett.add(Restrictions.le(
					ZeiterfassungFac.FLR_REISE_T_ZEIT, flrReise.getT_zeit()));
			critReiseKomplett.addOrder(Order
					.asc(ZeiterfassungFac.FLR_REISE_T_ZEIT));

			List<?> resultListReiseKomplett = critReiseKomplett.list();
			Iterator<?> resultListIteratorReiseKomplett = resultListReiseKomplett
					.iterator();

			ReiseKomplettDto rkDto = new ReiseKomplettDto();

			while (resultListIteratorReiseKomplett.hasNext()) {
				FLRReise flrReiseKomplett = (FLRReise) resultListIteratorReiseKomplett
						.next();
				try {
					if (flrReiseKomplett.getB_beginn().equals(
							Helper.boolean2Short(true))) {

						ReiseDto reiseBeginnDto = getZeiterfassungFac()
								.reiseFindByPrimaryKey(
										flrReiseKomplett.getI_id(),
										theClientDto);

						if (tVon != null
								&& reiseBeginnDto.getTZeit().before(tVon)) {
							reiseBeginnDto.setTZeit(tVon);
						}

						rkDto.addBeginn(reiseBeginnDto);
					} else {
						rkDto.setReiseEnde(getZeiterfassungFac()
								.reiseFindByPrimaryKey(
										flrReiseKomplett.getI_id(),
										theClientDto));
					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}
			session3.close();
			alReisen.add(rkDto);

		}

		session2.close();

		return alReisen;
	}

	@Override
	public boolean zeitAufLoseVerteilen(Integer personalIId,
			Timestamp tZeitBis, TheClientDto theClientDto)
			throws RemoteException {
		// TODO Diese Methode gab es in der ZeiterfassungFacAll aber nicht hier.
		return false;
	}
}
