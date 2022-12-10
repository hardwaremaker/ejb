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
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.angebot.fastlanereader.generated.FLRAngebotposition;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.artikel.ejb.Artgru;
import com.lp.server.artikel.ejb.Artkla;
import com.lp.server.artikel.ejb.Handlagerbewegung;
import com.lp.server.artikel.ejb.Lager;
import com.lp.server.artikel.ejb.Shopgruppe;
import com.lp.server.artikel.fastlanereader.WarenbewegungenHandler;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelgruppe;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikellistespr;
import com.lp.server.artikel.fastlanereader.generated.FLRHandlagerbewegung;
import com.lp.server.artikel.fastlanereader.generated.FLRLagerabgangursprung;
import com.lp.server.artikel.fastlanereader.generated.FLRLagerbewegung;
import com.lp.server.artikel.fastlanereader.generated.FLRShopgruppe;
import com.lp.server.artikel.fastlanereader.generated.FLRWareneingangsreferez;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelGestehungspreisCalc;
import com.lp.server.artikel.service.ArtikelbestelltFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtklaDto;
import com.lp.server.artikel.service.BelegInfos;
import com.lp.server.artikel.service.KundeUmsatzstatistikDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.artikel.service.LagerReportFac;
import com.lp.server.artikel.service.LagerabgangursprungDto;
import com.lp.server.artikel.service.LagerbewegungDto;
import com.lp.server.artikel.service.LagerplatzDto;
import com.lp.server.artikel.service.ReportArtikelgruppeDto;
import com.lp.server.artikel.service.ReportMaterialbedarfsvorschauDto;
import com.lp.server.artikel.service.ReportMaterialbedarfsvorschauSubreportDto;
import com.lp.server.artikel.service.SeriennrChargennrAufLagerDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkPreisfindungEinzelverkaufspreisDto;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragpositionReport;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.fastlanereader.generated.FLRWareneingangspositionen;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.WareneingangFac;
import com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnungReport;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosistmaterialDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferscheinposition;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.partner.fastlanereader.generated.FLRLieferant;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnung;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungPosition;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungReport;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.stueckliste.fastlanereader.generated.FLRFertigungsgruppe;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteMitStrukturDto;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.ejbfac.HvCreatingCachingProvider;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeBase;
import com.lp.server.system.jcr.service.docnode.DocNodeFolder;
import com.lp.server.system.jcr.service.docnode.DocNodeLiteral;
import com.lp.server.system.jcr.service.docnode.HeliumDocPath;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.DatumsfilterVonBis;
import com.lp.server.util.HelperServer;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.HelperReport;
import com.lp.util.LPDatenSubreport;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

@Stateless
public class LagerReportFacBean extends LPReport implements LagerReportFac, JRDataSource {
	@PersistenceContext
	private EntityManager em;
	private Object[][] data = null;
	private String sAktuellerReport = null;

	private static int REPORT_WARENBEWEGUNGSJOURNAL_ZUGANGSBELEG = 0;
	private static int REPORT_WARENBEWEGUNGSJOURNAL_ZUGANGSDATUM = 1;
	private static int REPORT_WARENBEWEGUNGSJOURNAL_ZUGANGFIRMAKOMMENTAR = 2;
	private static int REPORT_WARENBEWEGUNGSJOURNAL_ZUBUCHUNGSZEITPUNKT = 3;
	private static int REPORT_WARENBEWEGUNGSJOURNAL_ZUGANGSMENGE = 4;
	private static int REPORT_WARENBEWEGUNGSJOURNAL_LAGER = 5;
	private static int REPORT_WARENBEWEGUNGSJOURNAL_SNR = 6;
	private static int REPORT_WARENBEWEGUNGSJOURNAL_EINSTANDSPREIS = 7;
	private static int REPORT_WARENBEWEGUNGSJOURNAL_GESTPREIS = 8;
	private static int REPORT_WARENBEWEGUNGSJOURNAL_ABGANGSBELEG = 9;
	private static int REPORT_WARENBEWEGUNGSJOURNAL_ABGANGSDATUM = 10;
	private static int REPORT_WARENBEWEGUNGSJOURNAL_ABGANGFIRMAKOMMENTAR = 11;
	private static int REPORT_WARENBEWEGUNGSJOURNAL_ABBUCHUNGSZEITPUNKT = 12;
	private static int REPORT_WARENBEWEGUNGSJOURNAL_VKPREIS = 13;
	private static int REPORT_WARENBEWEGUNGSJOURNAL_ABGANGSMENGE = 14;
	private static int REPORT_WARENBEWEGUNGSJOURNAL_HERSTELLER = 15;
	private static int REPORT_WARENBEWEGUNGSJOURNAL_URSPRUNGSLAND = 16;
	private static int REPORT_WARENBEWEGUNGSJOURNAL_WE_REFERENZ = 17;
	private static int REPORT_WARENBEWEGUNGSJOURNAL_ZUGANG_WER_HAT_GEBUCHT = 18;
	private static int REPORT_WARENBEWEGUNGSJOURNAL_ABGANG_WER_HAT_GEBUCHT = 19;
	private static int REPORT_WARENBEWEGUNGSJOURNAL_MANDANT_ZUGANGSBELEG = 20;
	private static int REPORT_WARENBEWEGUNGSJOURNAL_MANDANT_ABGANGSBELEG = 21;
	private static int REPORT_WARENBEWEGUNGSJOURNAL_VERSION = 22;

	private static int REPORT_KUNDEUMSATZSTATISTIK_KUNDENGRUPPIERUNG = 0;
	private static int REPORT_KUNDEUMSATZSTATISTIK_KUNDE = 1;
	private static int REPORT_KUNDEUMSATZSTATISTIK_UMSATZ = 2;
	private static int REPORT_KUNDEUMSATZSTATISTIK_DECKUNGSBEITRAG = 3;
	private static int REPORT_KUNDEUMSATZSTATISTIK_ERSTUMSATZ = 4;
	private static int REPORT_KUNDEUMSATZSTATISTIK_ABCKLASSE = 5;
	private static int REPORT_KUNDEUMSATZSTATISTIK_SPALTE1 = 6;
	private static int REPORT_KUNDEUMSATZSTATISTIK_SPALTE2 = 7;
	private static int REPORT_KUNDEUMSATZSTATISTIK_SPALTE3 = 8;
	private static int REPORT_KUNDEUMSATZSTATISTIK_SPALTE4 = 9;
	private static int REPORT_KUNDEUMSATZSTATISTIK_SPALTE5 = 10;
	private static int REPORT_KUNDEUMSATZSTATISTIK_SPALTE6 = 11;
	private static int REPORT_KUNDEUMSATZSTATISTIK_SPALTE7 = 12;
	private static int REPORT_KUNDEUMSATZSTATISTIK_SPALTE8 = 13;
	private static int REPORT_KUNDEUMSATZSTATISTIK_SPALTE1DB = 14;
	private static int REPORT_KUNDEUMSATZSTATISTIK_SPALTE2DB = 15;
	private static int REPORT_KUNDEUMSATZSTATISTIK_SPALTE3DB = 16;
	private static int REPORT_KUNDEUMSATZSTATISTIK_SPALTE4DB = 17;
	private static int REPORT_KUNDEUMSATZSTATISTIK_SPALTE5DB = 18;
	private static int REPORT_KUNDEUMSATZSTATISTIK_SPALTE6DB = 19;
	private static int REPORT_KUNDEUMSATZSTATISTIK_SPALTE7DB = 20;
	private static int REPORT_KUNDEUMSATZSTATISTIK_SPALTE8DB = 21;
	private static int REPORT_KUNDEUMSATZSTATISTIK_ZAHLUNGSZIEL = 22;
	private static int REPORT_KUNDEUMSATZSTATISTIK_LIEFERART = 23;
	private static int REPORT_KUNDEUMSATZSTATISTIK_SPEDITEUR = 24;
	private static int REPORT_KUNDEUMSATZSTATISTIK_LKZ = 25;
	private static int REPORT_KUNDEUMSATZSTATISTIK_PLZ = 26;
	private static int REPORT_KUNDEUMSATZSTATISTIK_KUNDENNUMMER = 27;
	private static int REPORT_KUNDEUMSATZSTATISTIK_KREDITLIMIT = 28;
	private static int REPORT_KUNDEUMSATZSTATISTIK_DATUM_ERSTUMSATZ = 29;
	private static int REPORT_KUNDEUMSATZSTATISTIK_DATUM_LETZTUMSATZ = 30;
	private static int REPORT_KUNDEUMSATZSTATISTIK_ILN = 31;
	private static int REPORT_KUNDEUMSATZSTATISTIK_KUNDE_I_ID = 32;
	private static int REPORT_KUNDEUMSATZSTATISTIK_PARTNER_I_ID = 33;

	private static int REPORT_KUNDEUMSATZSTATISTIK_PROVISIONSEMPFAENGER_KURZZEICHEN = 34;
	private static int REPORT_KUNDEUMSATZSTATISTIK_PROVISIONSEMPFAENGER_NAME = 35;

	private static int REPORT_KUNDEUMSATZSTATISTIK_ANZAHL_SPALTEN = 36;

	private static int REPORT_LIEFERANTUMSATZSTATISTIK_LIEFERANTGRUPPIERUNG = 0;
	private static int REPORT_LIEFERANTUMSATZSTATISTIK_LIEFERANT = 1;
	private static int REPORT_LIEFERANTUMSATZSTATISTIK_UMSATZ = 2;
	private static int REPORT_LIEFERANTUMSATZSTATISTIK_ABCKLASSE = 3;
	private static int REPORT_LIEFERANTUMSATZSTATISTIK_SPALTE1 = 4;
	private static int REPORT_LIEFERANTUMSATZSTATISTIK_SPALTE2 = 5;
	private static int REPORT_LIEFERANTUMSATZSTATISTIK_SPALTE3 = 6;
	private static int REPORT_LIEFERANTUMSATZSTATISTIK_SPALTE4 = 7;
	private static int REPORT_LIEFERANTUMSATZSTATISTIK_SPALTE5 = 8;
	private static int REPORT_LIEFERANTUMSATZSTATISTIK_SPALTE6 = 9;
	private static int REPORT_LIEFERANTUMSATZSTATISTIK_SPALTE7 = 10;
	private static int REPORT_LIEFERANTUMSATZSTATISTIK_SPALTE8 = 11;
	private static int REPORT_LIEFERANTUMSATZSTATISTIK_ZAHLUNGSZIEL = 12;
	private static int REPORT_LIEFERANTUMSATZSTATISTIK_LIEFERART = 13;
	private static int REPORT_LIEFERANTUMSATZSTATISTIK_SPEDITEUR = 14;
	private static int REPORT_LIEFERANTUMSATZSTATISTIK_LKZ = 15;
	private static int REPORT_LIEFERANTUMSATZSTATISTIK_PLZ = 16;
	private static int REPORT_LIEFERANTUMSATZSTATISTIK_ORT = 17;
	private static int REPORT_LIEFERANTUMSATZSTATISTIK_LIEFERANT_I_ID = 18;
	private static int REPORT_LIEFERANTUMSATZSTATISTIK_ANZAHL_SPALTEN = 19;

	private static int REPORT_LADENHUETER_ARTIKELNUMMER = 0;
	private static int REPORT_LADENHUETER_BEZEICHNUNG = 1;
	private static int REPORT_LADENHUETER_LAGERSTAND_HEUTE = 2;
	private static int REPORT_LADENHUETER_LAGERSTAND_ZUMSTICHTAG = 3;
	private static int REPORT_LADENHUETER_GESTPREIS = 4;
	private static int REPORT_LADENHUETER_ARTIKELGRUPPE = 5;
	private static int REPORT_LADENHUETER_ARTIKELKLASSE = 6;
	private static int REPORT_LADENHUETER_RESERVIERUNGEN = 7;
	private static int REPORT_LADENHUETER_FEHLMENGEN = 8;
	private static int REPORT_LADENHUETER_EINSTANDSPREIS = 9;
	private static int REPORT_LADENHUETER_VERSTECHT = 10;
	private static int REPORT_LADENHUETER_ARTIKELART = 11;
	private static int REPORT_LADENHUETER_RAHMENRESERVIERUNGEN = 12;
	private static int REPORT_LADENHUETER_STUECKLISTE = 13;
	private static int REPORT_LADENHUETER_HILFSSTUECKLISTE = 14;
	private static int REPORT_LADENHUETER_RAHMENDETAILBEDARFE = 15;
	private static int REPORT_LADENHUETER_EINKAUFSPREIS = 16;
	private static int REPORT_LADENHUETER_LIEFERANTARTIKELNUMMER = 17;
	private static int REPORT_LADENHUETER_ARTIKELREFERENZNUMMER = 18;
	private static int REPORT_LADENHUETER_BEVORZUGTERLIEFERANT = 19;

	private static int REPORT_HILISTE_ARTIKELNUMMER = 0;
	private static int REPORT_HILISTE_BEZEICHNUNG = 1;
	private static int REPORT_HILISTE_DURCHSCHNITTLICHERVKPREIS = 2;
	private static int REPORT_HILISTE_VERKAUFTEMENGE = 3;
	private static int REPORT_HILISTE_ARTIKELGRUPPE = 4;
	private static int REPORT_HILISTE_SHOPGRUPPE = 5;
	private static int REPORT_HILISTE_ARTIKELKLASSE = 6;
	private static int REPORT_HILISTE_DURCHSCHNITTLICHERGESTPREIS = 7;
	private static int REPORT_HILISTE_DBWERT = 8;
	private static int REPORT_HILISTE_VERSTECKT = 9;

	private static int REPORT_HILISTE_LS_NICHT_VERRECHENBAR_MENGE = 10;
	private static int REPORT_HILISTE_LS_NICHT_VERRECHENBAR_GESTWERT = 11;
	private static int REPORT_HILISTE_LS_NICHT_VERRECHENBAR_DBWERT = 12;

	private static int REPORT_HILISTE_LS_VERRECHENBAR_OHNE_ZIELLAGER_MENGE = 13;
	private static int REPORT_HILISTE_LS_VERRECHENBAR_OHNE_ZIELLAGER_GESTWERT = 14;
	private static int REPORT_HILISTE_LS_VERRECHENBAR_OHNE_ZIELLAGER_DBWERT = 15;

	private static int REPORT_HILISTE_LS_VERRECHENBAR_MIT_ZIELLAGER_UND_KONSI_MENGE = 16;
	private static int REPORT_HILISTE_LS_VERRECHENBAR_MIT_ZIELLAGER_UND_KONSI_GESTWERT = 17;
	private static int REPORT_HILISTE_LS_VERRECHENBAR_MIT_ZIELLAGER_UND_KONSI_DBWERT = 18;

	private static int REPORT_HILISTE_LS_VERRECHENBAR_MIT_ZIELLAGER_OHNE_KONSI_MENGE = 19;
	private static int REPORT_HILISTE_LS_VERRECHENBAR_MIT_ZIELLAGER_OHNE_KONSI_GESTWERT = 20;
	private static int REPORT_HILISTE_LS_VERRECHENBAR_MIT_ZIELLAGER_OHNE_KONSI_DBWERT = 21;

	private static int REPORT_HILISTE_JAHRESMENGE = 22;
	private static int REPORT_HILISTE_LAGERMINDESTSTAND = 23;
	private static int REPORT_HILISTE_LAGERSOLLSTAND = 24;
	private static int REPORT_HILISTE_LAGERSTAND = 25;
	private static int REPORT_HILISTE_DURCHSCHNITTLICHE_WBZ = 26;
	private static int REPORT_HILISTE_WBZ_LIEF1 = 27;
	private static int REPORT_HILISTE_ANZAHL_TEILERLEDIGTER_LOSE = 28;
	private static int REPORT_HILISTE_ARTIKELREFERENZNUMMER = 29;
	private static int REPORT_HILISTE_ANZAHL_SPALTEN = 30;

	private static int REPORT_ARTIKELGRUPPE_ARTIKELGRUPPE = 0;
	private static int REPORT_ARTIKELGRUPPE_VKWERT = 1;
	private static int REPORT_ARTIKELGRUPPE_GESTWERT = 2;
	private static int REPORT_ARTIKELGRUPPE_MENGE = 3;
	private static int REPORT_ARTIKELGRUPPE_KONTO = 4;
	private static int REPORT_ARTIKELGRUPPE_KONTOBEZEICHNUNG = 5;
	private static int REPORT_ARTIKELGRUPPE_ERGEBNISGRUPPE = 6;
	private static int REPORT_ARTIKELGRUPPE_ANZAHL_SPALTEN = 7;

	private static int REPORT_SHOPGRUPPE_SHOPGRUPPE = 0;
	private static int REPORT_SHOPGRUPPE_VKWERT = 1;
	private static int REPORT_SHOPGRUPPE_GESTWERT = 2;
	private static int REPORT_SHOPGRUPPE_MENGE = 3;

	private static int REPORT_MATERIALBEDARFSVORSCHAU_ARTIKELGRUPPE = 0;
	private static int REPORT_MATERIALBEDARFSVORSCHAU_MENGE_OFFENE_RAHMENAUFTRAEGE = 1;
	private static int REPORT_MATERIALBEDARFSVORSCHAU_MENGE_OFFENE_ANGEBOTE = 2;
	private static int REPORT_MATERIALBEDARFSVORSCHAU_MENGE_VERGANGENHEIT = 3;
	private static int REPORT_MATERIALBEDARFSVORSCHAU_MENGE_ANGELEGTE_AUFTRAEGE = 4;
	private static int REPORT_MATERIALBEDARFSVORSCHAU_ANZAHL_SPALTEN = 5;

	private static int REPORT_UMBUCHUNGSBELEG_ARTIKELNUMMER = 0;
	private static int REPORT_UMBUCHUNGSBELEG_BEZEICHNUNG = 1;
	private static int REPORT_UMBUCHUNGSBELEG_DATUM = 2;
	private static int REPORT_UMBUCHUNGSBELEG_VONLAGER = 3;
	private static int REPORT_UMBUCHUNGSBELEG_NACHLAGER = 4;
	private static int REPORT_UMBUCHUNGSBELEG_MENGE = 5;
	private static int REPORT_UMBUCHUNGSBELEG_SERIENNR = 6;
	private static int REPORT_UMBUCHUNGSBELEG_EINHEIT = 7;
	private static int REPORT_UMBUCHUNGSBELEG_KURZBEZEICHNUNG = 8;
	private static int REPORT_UMBUCHUNGSBELEG_ZUSATZBEZEICHNUNG = 9;
	private static int REPORT_UMBUCHUNGSBELEG_ZUSATZBEZEICHNUNG2 = 10;
	private static int REPORT_UMBUCHUNGSBELEG_LAGERORT = 11;
	private static int REPORT_UMBUCHUNGSBELEG_ANZAHL_SPALTEN = 12;

	private static int REPORT_LAGERBUCHUNGSBELEG_ARTIKELNUMMER = 0;
	private static int REPORT_LAGERBUCHUNGSBELEG_BEZEICHNUNG = 1;
	private static int REPORT_LAGERBUCHUNGSBELEG_DATUM = 2;
	private static int REPORT_LAGERBUCHUNGSBELEG_LAGER = 3;
	private static int REPORT_LAGERBUCHUNGSBELEG_MENGE = 4;
	private static int REPORT_LAGERBUCHUNGSBELEG_SERIENNR = 5;
	private static int REPORT_LAGERBUCHUNGSBELEG_EINHEIT = 6;
	private static int REPORT_LAGERBUCHUNGSBELEG_KURZBEZEICHNUNG = 7;
	private static int REPORT_LAGERBUCHUNGSBELEG_ZUSATZBEZEICHNUNG = 8;
	private static int REPORT_LAGERBUCHUNGSBELEG_ZUSATZBEZEICHNUNG2 = 9;
	private static int REPORT_LAGERBUCHUNGSBELEG_LAGERORT = 10;
	private static int REPORT_LAGERBUCHUNGSBELEG_REFERENZNUMMER = 11;
	private static int REPORT_LAGERBUCHUNGSBELEG_INDEX = 12;
	private static int REPORT_LAGERBUCHUNGSBELEG_I_ID_BUCHUNG = 13;
	private static int REPORT_LAGERBUCHUNGSBELEG_KOMMENTAR = 14;
	private static int REPORT_LAGERBUCHUNGSBELEG_EXEMPLAR = 15;
	private static int REPORT_LAGERBUCHUNGSBELEG_EXEMPLAREGESAMT = 16;
	private static int REPORT_LAGERBUCHUNGSBELEG_ANZAHL_SPALTEN = 17;

	private static int REPORT_CHARGENEIGENSCHAFTEN_ARTIKELNUMMER = 0;
	private static int REPORT_CHARGENEIGENSCHAFTEN_BEZEICHNUNG = 1;
	private static int REPORT_CHARGENEIGENSCHAFTEN_DATUM = 2;
	private static int REPORT_CHARGENEIGENSCHAFTEN_LAGER = 3;
	private static int REPORT_CHARGENEIGENSCHAFTEN_MENGE = 4;
	private static int REPORT_CHARGENEIGENSCHAFTEN_SERIENNR = 5;
	private static int REPORT_CHARGENEIGENSCHAFTEN_EINHEIT = 6;
	private static int REPORT_CHARGENEIGENSCHAFTEN_KURZBEZEICHNUNG = 7;
	private static int REPORT_CHARGENEIGENSCHAFTEN_ZUSATZBEZEICHNUNG = 8;
	private static int REPORT_CHARGENEIGENSCHAFTEN_ZUSATZBEZEICHNUNG2 = 9;
	private static int REPORT_CHARGENEIGENSCHAFTEN_LAGERORT = 10;
	private static int REPORT_CHARGENEIGENSCHAFTEN_REFERENZNUMMER = 11;
	private static int REPORT_CHARGENEIGENSCHAFTEN_INDEX = 12;
	private static int REPORT_CHARGENEIGENSCHAFTEN_I_ID_BUCHUNG = 13;
	private static int REPORT_CHARGENEIGENSCHAFTEN_ARTIKELSNRCHNR_I_ID = 14;
	private static int REPORT_CHARGENEIGENSCHAFTEN_ARTGRU_I_ID = 15;
	private static int REPORT_CHARGENEIGENSCHAFTEN_ANZAHL_SPALTEN = 16;

	private static int REPORT_MINDESTHALTBARKEIT_ARTIKELNUMMER = 0;
	private static int REPORT_MINDESTHALTBARKEIT_BEZEICHNUNG = 1;
	private static int REPORT_MINDESTHALTBARKEIT_MENGE = 2;
	private static int REPORT_MINDESTHALTBARKEIT_CHARGENNUMMER = 3;
	private static int REPORT_MINDESTHALTBARKEIT_HALTBARMONATE = 4;
	private static int REPORT_MINDESTHALTBARKEIT_HALTBARTAGE = 5;

	private static int REPORT_LAGERSTANDLISTE_IDENTNUMMER = 0;
	private static int REPORT_LAGERSTANDLISTE_BEZEICHNUNG = 1;
	private static int REPORT_LAGERSTANDLISTE_LAGERSTAND = 2;
	private static int REPORT_LAGERSTANDLISTE_EINHEIT = 3;
	private static int REPORT_LAGERSTANDLISTE_GESTEHUNGSPREIS = 4;
	private static int REPORT_LAGERSTANDLISTE_LAGERWERT = 5;
	private static int REPORT_LAGERSTANDLISTE_RESERVIERTE_MENGE = 6;
	private static int REPORT_LAGERSTANDLISTE_RESERVIERTER_WERT = 7;
	private static int REPORT_LAGERSTANDLISTE_FREIER_WERT = 8;
	private static int REPORT_LAGERSTANDLISTE_SNRCHNRAUFLAGER = 9;
	private static int REPORT_LAGERSTANDLISTE_ARTIKELGRUPPE = 10;
	private static int REPORT_LAGERSTANDLISTE_ARTIKELKLASSE = 11;
	private static int REPORT_LAGERSTANDLISTE_ARTIKELART = 12;
	private static int REPORT_LAGERSTANDLISTE_STUECKLISTE = 13;
	private static int REPORT_LAGERSTANDLISTE_HILFSSTUECKLISTE = 14;
	private static int REPORT_LAGERSTANDLISTE_GESTEHUNGSPREIS_ABGEWERTET = 15;
	private static int REPORT_LAGERSTANDLISTE_LIEF1PREIS = 16;
	private static int REPORT_LAGERSTANDLISTE_VERSTECKT = 17;
	private static int REPORT_LAGERSTANDLISTE_KURZBEZEICHNUNG = 18;
	private static int REPORT_LAGERSTANDLISTE_ZUSATZBEZEICHNUNG = 19;
	private static int REPORT_LAGERSTANDLISTE_ZUSATZBEZEICHNUNG2 = 20;
	private static int REPORT_LAGERSTANDLISTE_REFERENZNUMMER = 21;
	private static int REPORT_LAGERSTANDLISTE_BESTELLMENGENEINHEIT = 22;
	private static int REPORT_LAGERSTANDLISTE_LAGERORT = 23;
	private static int REPORT_LAGERSTANDLISTE_UMRECHNUNGFAKTOR = 24;
	private static int REPORT_LAGERSTANDLISTE_SHOPGRUPPE = 25;
	private static int REPORT_LAGERSTANDLISTE_LAGERWERT_MATERIALZUSCHLAG = 26;
	private static int REPORT_LAGERSTANDLISTE_VERPACKUNGSMENGE = 27;
	private static int REPORT_LAGERSTANDLISTE_LAGER = 28;
	private static int REPORT_LAGERSTANDLISTE_LAGERART = 29;
	private static int REPORT_LAGERSTANDLISTE_KONSIGNATIONSLAGER = 30;
	private static int REPORT_LAGERSTANDLISTE_FEHLMENGE = 31;
	private static int REPORT_LAGERSTANDLISTE_FEHLMENGEN_WERT = 32;
	private static int REPORT_LAGERSTANDLISTE_FREIER_WERT_INKL_FEHLMENGE = 33;
	private static int REPORT_LAGERSTANDLISTE_SUBREPORT_SNRCHNRAUFLAGER = 34;
	private static int REPORT_LAGERSTANDLISTE_SUBREPORT_BELEGE = 35;
	private static int REPORT_LAGERSTANDLISTE_ANZAHL_SPALTEN = 36;

	private static int REPORT_GESTPREISUNTERMINVK_ARTIKELNUMMER = 0;
	private static int REPORT_GESTPREISUNTERMINVK_BEZEICHNUNG = 1;
	private static int REPORT_GESTPREISUNTERMINVK_VKPREIS = 2;
	private static int REPORT_GESTPREISUNTERMINVK_GESTPREIS = 3;
	private static int REPORT_GESTPREISUNTERMINVK_VERSTECKT = 4;
	private static int REPORT_GESTPREISUNTERMINVK_ARTIKELART = 5;
	private static int REPORT_GESTPREISUNTERMINVK_STUECKLISTENART = 6;
	private static int REPORT_GESTPREISUNTERMINVK_MATERIALZUSCHLAG = 7;
	private static int REPORT_GESTPREISUNTERMINVK_LAGERSTAND = 8;
	private static int REPORT_GESTPREISUNTERMINVK_ARTIKELREFERENZNUMMER = 9;
	private static int REPORT_GESTPREISUNTERMINVK_ANZAHL_SPALTEN = 26;

	private static int REPORT_WARENENTNAHMESTATISTIK_ARTIKELNUMMER = 0;
	private static int REPORT_WARENENTNAHMESTATISTIK_BEZEICHNUNG = 1;
	private static int REPORT_WARENENTNAHMESTATISTIK_KURZBEZEICHNUNG = 2;
	private static int REPORT_WARENENTNAHMESTATISTIK_ZUSATZBEZEICHNUNG = 3;
	private static int REPORT_WARENENTNAHMESTATISTIK_ZUSATZBEZEICHNUNG2 = 4;
	private static int REPORT_WARENENTNAHMESTATISTIK_LAGERSTAND_BEGINN = 5;
	private static int REPORT_WARENENTNAHMESTATISTIK_LAGERSTAND_ENDE = 6;
	private static int REPORT_WARENENTNAHMESTATISTIK_MENGE_ZUGANG = 7;
	private static int REPORT_WARENENTNAHMESTATISTIK_MENGE_ABGANG = 8;
	private static int REPORT_WARENENTNAHMESTATISTIK_LAGERWERT_BEGINN = 9;
	private static int REPORT_WARENENTNAHMESTATISTIK_LAGERWERT_ENDE = 10;
	private static int REPORT_WARENENTNAHMESTATISTIK_LAGER = 11;
	private static int REPORT_WARENENTNAHMESTATISTIK_VERSTECKT = 12;
	private static int REPORT_WARENENTNAHMESTATISTIK_EKPREIS = 13;
	private static int REPORT_WARENENTNAHMESTATISTIK_ANZAHL_BESTELLUNGEN = 14;
	private static int REPORT_WARENENTNAHMESTATISTIK_ARTIKELGRUPPE = 15;
	private static int REPORT_WARENENTNAHMESTATISTIK_ARTIKELKLASSE = 16;
	private static int REPORT_WARENENTNAHMESTATISTIK_SACHKONTO = 17;
	private static int REPORT_WARENENTNAHMESTATISTIK_WERT_ZUGAENGE = 18;
	private static int REPORT_WARENENTNAHMESTATISTIK_WERT_ABGAENGE = 19;
	private static int REPORT_WARENENTNAHMESTATISTIK_ARTIKELART = 20;
	private static int REPORT_WARENENTNAHMESTATISTIK_ARTIKELREFERENZNUMMER = 21;
	private static int REPORT_WARENENTNAHMESTATISTIK_BELEGART = 22;
	private static int REPORT_WARENENTNAHMESTATISTIK_BELEGNUMMER = 23;
	private static int REPORT_WARENENTNAHMESTATISTIK_BELEGDATUM = 24;
	private static int REPORT_WARENENTNAHMESTATISTIK_BUCHUNGSZEIT = 25;
	private static int REPORT_WARENENTNAHMESTATISTIK_EINSTANDSPREIS_PRO_STUECK = 26;
	private static int REPORT_WARENENTNAHMESTATISTIK_ANZAHL_SPALTEN = 27;

	public static int REPORT_ZAEHLLISTE_ARTIKELNUMMER = 0;
	public static int REPORT_ZAEHLLISTE_ARTIKELBEZEICHNUNG = 1;
	public static int REPORT_ZAEHLLISTE_ARTIELZUSATZBEZEICHNUNG = 2;
	public static int REPORT_ZAEHLLISTE_ARTIELZUSATZBEZEICHNUNG2 = 3;
	public static int REPORT_ZAEHLLISTE_MENGENART = 4;
	public static int REPORT_ZAEHLLISTE_LAGERORT = 5;
	public static int REPORT_ZAEHLLISTE_SNRTRAGEND = 6;
	public static int REPORT_ZAEHLLISTE_CHNRTRAGEND = 7;
	public static int REPORT_ZAEHLLISTE_SERIENNRCHARGENNR = 8;
	public static int REPORT_ZAEHLLISTE_BARCODE_DRUCKBAR = 9;
	public static int REPORT_ZAEHLLISTE_BREITE = 10;
	public static int REPORT_ZAEHLLISTE_HOEHE = 11;
	public static int REPORT_ZAEHLLISTE_TIEFE = 12;
	public static int REPORT_ZAEHLLISTE_BREITETEXT = 13;
	public static int REPORT_ZAEHLLISTE_ARTIKELBAUFORM = 14;
	public static int REPORT_ZAEHLLISTE_VERPACKUNGSART = 15;
	public static int REPORT_ZAEHLLISTE_LAGERSTAND = 16;
	public static int REPORT_ZAEHLLISTE_VERPACKUNGMENGE = 17;
	public static int REPORT_ZAEHLLISTE_REFERENZNUMMER = 18;
	public static int REPORT_ZAEHLLISTE_FELDANZAHL = 19;

	public static int REPORT_HANDLAGERBEWEGUNGEN_ARTIKELNUMMER = 0;
	public static int REPORT_HANDLAGERBEWEGUNGEN_ARTIKELBEZEICHNUNG = 1;
	public static int REPORT_HANDLAGERBEWEGUNGEN_ARTIKELZUSATZBEZEICHNUNG = 2;
	public static int REPORT_HANDLAGERBEWEGUNGEN_ARTIKELZUSATZBEZEICHNUNG2 = 3;
	public static int REPORT_HANDLAGERBEWEGUNGEN_MENGE = 4;
	public static int REPORT_HANDLAGERBEWEGUNGEN_ABGANG = 5;
	public static int REPORT_HANDLAGERBEWEGUNGEN_VKPREIS = 6;
	public static int REPORT_HANDLAGERBEWEGUNGEN_EINSTANDSPREIS = 7;
	public static int REPORT_HANDLAGERBEWEGUNGEN_BUCHUNGSZEIT = 8;
	public static int REPORT_HANDLAGERBEWEGUNGEN_PERSON_GEBUCHT = 9;
	public static int REPORT_HANDLAGERBEWEGUNGEN_KOMMENTAR = 10;
	public static int REPORT_HANDLAGERBEWEGUNGEN_LAGER = 11;
	public static int REPORT_HANDLAGERBEWEGUNGEN_SUBREPORT_SNRCHNR = 12;
	public static int REPORT_HANDLAGERBEWEGUNGEN_ANZAHL_SPALTEN = 13;

	private static int REPORT_INDIREKTE_WE_STATISTIK_ARTIKELNUMMER = 0;
	private static int REPORT_INDIREKTE_WE_STATISTIK_BEZEICHNUNG = 1;
	private static int REPORT_INDIREKTE_WE_STATISTIK_ZUSATZBEZEICHNUNG = 2;
	private static int REPORT_INDIREKTE_WE_STATISTIK_LIEFERBELEG = 3;
	private static int REPORT_INDIREKTE_WE_STATISTIK_VKPREIS_LIEFERBELEG = 4;
	private static int REPORT_INDIREKTE_WE_STATISTIK_KUNDE = 5;
	private static int REPORT_INDIREKTE_WE_STATISTIK_WARENEINGANGSBELEG = 6;
	private static int REPORT_INDIREKTE_WE_STATISTIK_LIEFERANT = 7;
	private static int REPORT_INDIREKTE_WE_STATISTIK_VERBRAUCHSBELEG = 8;
	private static int REPORT_INDIREKTE_WE_STATISTIK_EBENE = 9;
	private static int REPORT_INDIREKTE_WE_STATISTIK_LIEFERDATUM = 10;
	private static int REPORT_INDIREKTE_WE_STATISTIK_MENGE = 11;
	private static int REPORT_INDIREKTE_WE_STATISTIK_EINSTANDSPREIS = 12;
	private static int REPORT_INDIREKTE_WE_STATISTIK_GESTEHUNGSPREIS = 13;
	private static int REPORT_INDIREKTE_WE_STATISTIK_VKPREIS = 14;
	private static int REPORT_INDIREKTE_WE_STATISTIK_KUNDENARTIKELNUMMER = 15;
	private static int REPORT_INDIREKTE_WE_STATISTIK_VERBRAUCHSDATUM = 16;
	private static int REPORT_INDIREKTE_WE_STATISTIK_WARENEINGANGSDATUM = 17;
	private static int REPORT_INDIREKTE_WE_STATISTIK_ARTIKELREFERENZNUMMER = 18;
	private static int REPORT_INDIREKTE_WE_STATISTIK_ANZAHL_SPALTEN = 19;

	public JasperPrintLP printLieferantumsatzstatistik(Timestamp tVon, Timestamp tBis, boolean bWareneingangspositionen,
			Integer iOptionKundengruppierung, Integer iOptionGruppierung, Integer iOptionSortierung,
			boolean bNurHauptgruppeKlasse, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		List<String> gruppierung = new ArrayList<String>();

		String[] monate = new DateFormatSymbols(theClientDto.getLocUi()).getMonths();

		HashMap<String, String> zuordnungVatergruppenKlassen = null;

		if (iOptionGruppierung
				.intValue() == LagerReportFac.REPORT_LIEFERANTUMSATZSTATISTIK_OPTION_GRUPPIERUNG_ARTIKELKLASSE) {

			ArtklaDto[] artklDtos = getArtikelFac().artklaFindByMandantCNr(theClientDto);
			for (int i = 0; i < artklDtos.length; i++) {
				gruppierung.add(artklDtos[i].getCNr());
			}

			if (bNurHauptgruppeKlasse == true) {
				zuordnungVatergruppenKlassen = getArtikelklasseVaterklasse(theClientDto);
			}

		} else if (iOptionGruppierung
				.intValue() == LagerReportFac.REPORT_LIEFERANTUMSATZSTATISTIK_OPTION_GRUPPIERUNG_ARTIKELGRUPPE) {

			ArtgruDto[] artgruDtos = getArtikelFac().artgruFindByMandantCNr(theClientDto);
			for (int i = 0; i < artgruDtos.length; i++) {
				gruppierung.add(artgruDtos[i].getCNr());
			}

			if (bNurHauptgruppeKlasse == true) {
				zuordnungVatergruppenKlassen = getArtikelgruppeVatergruppe(theClientDto);
			}
		} else if (iOptionGruppierung
				.intValue() == LagerReportFac.REPORT_LIEFERANTUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHR) {

			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(tVon.getTime());
			c.set(Calendar.DAY_OF_YEAR, 1);
			while (c.getTime().before(tBis)) {
				gruppierung.add(c.get(Calendar.YEAR) + "");
				c.set(Calendar.YEAR, c.get(Calendar.YEAR) + 1);

			}

		} else if (iOptionGruppierung
				.intValue() == LagerReportFac.REPORT_LIEFERANTUMSATZSTATISTIK_OPTION_GRUPPIERUNG_MONAT) {

			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(tVon.getTime());
			c.set(Calendar.MONTH, 1);
			while (c.getTime().before(tBis)) {

				gruppierung.add(monate[c.get(Calendar.MONTH)] + " " + c.get(Calendar.YEAR));
				c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);

			}

		}

		tVon = Helper.cutTimestamp(tVon);
		tBis = Helper.cutTimestamp(tBis);

		BigDecimal gesamtUmsatz = new BigDecimal(0);
		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session.createCriteria(FLRLieferant.class);
		crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));

		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();

		ArrayList<Object> gesamtListe = new ArrayList<Object>();

		// Leere Spalten entfernen

		boolean[] bSpalteAndrucken = new boolean[gruppierung.size()];

		while (resultListIterator.hasNext()) {
			FLRLieferant lieferant = (FLRLieferant) resultListIterator.next();

			LieferantDto lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(lieferant.getI_id(), theClientDto);

			KundeUmsatzstatistikDto kdums = new KundeUmsatzstatistikDto(gruppierung.size() + 1);
			kdums.setSKunde(lieferant.getFlrpartner().getC_name1nachnamefirmazeile1());
			kdums.setLieferantIId(lieferant.getI_id());
			kdums.setIZahlungsziel(lieferantDto.getZahlungszielIId());
			kdums.setILieferart(lieferantDto.getLieferartIId());
			kdums.setISpediteur(lieferantDto.getIdSpediteur());

			if (lieferantDto.getPartnerDto().getLandplzortDto() != null) {
				kdums.setSLkz(lieferantDto.getPartnerDto().getLandplzortDto().getLandDto().getCLkz());
				kdums.setSPlz(lieferantDto.getPartnerDto().getLandplzortDto().getCPlz());
				kdums.setSOrt(lieferantDto.getPartnerDto().getLandplzortDto().getOrtDto().getCName());
			}

			kdums.setSKundengruppierung("");
			// Branche/Partnerklasse hinzufuegen

			if (iOptionKundengruppierung
					.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_KUNDENGRUPPIERUNG_BRANCHE
					&& lieferant.getFlrpartner().getBranche_i_id() != null) {
				kdums.setSKundengruppierung(getPartnerServicesFac()
						.brancheFindByPrimaryKey(lieferant.getFlrpartner().getBranche_i_id(), theClientDto)
						.getBezeichnung());
			} else if (iOptionKundengruppierung
					.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_KUNDENGRUPPIERUNG_PARTNERKLASSE
					&& lieferant.getFlrpartner().getPartnerklasse_i_id() != null) {
				kdums.setSKundengruppierung(getPartnerFac()
						.partnerklasseFindByPrimaryKey(lieferant.getFlrpartner().getPartnerklasse_i_id(), theClientDto)
						.getBezeichnung());
			}

			if (bWareneingangspositionen) {
				Session session2 = FLRSessionFactory.getFactory().openSession();

				org.hibernate.Criteria crit2 = session2.createCriteria(FLRWareneingangspositionen.class)
						.createAlias("flrwareneingang", "w").createAlias("w.flrbestellung", "b")
						.createAlias("b.flrlieferant", "l").add(Restrictions.eq("l.i_id", lieferant.getI_id()))
						.createAlias("l.flrpartner", "p");
				crit2.add(Restrictions.isNotNull(WareneingangFac.FLR_WEPOS_N_EINSTANDSPREIS));

				// SP1115
				crit2.createAlias("flrbestellposition", "bp").add(Restrictions.isNull("bp.position_i_id_artikelset"));

				String[] stati = new String[1];
				// auskommentiert wg. SP5795
				// stati[0] = BestellungFac.BESTELLSTATUS_ANGELEGT;
				stati[0] = BestellungFac.BESTELLSTATUS_STORNIERT;

				crit2.add(Restrictions
						.not(Restrictions.in("b." + BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR, stati)));

				Calendar c = Calendar.getInstance();
				c.setTime(tVon);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				c.set(Calendar.MILLISECOND, 0);
				crit2.add(Restrictions.ge("w." + WareneingangFac.FLR_WE_T_WARENEINGANGSDATUM, c.getTime()));

				c.setTime(tBis);
				c.set(Calendar.HOUR_OF_DAY, 23);
				c.set(Calendar.MINUTE, 59);
				c.set(Calendar.SECOND, 59);
				c.set(Calendar.MILLISECOND, 999);
				crit2.add(Restrictions.le("w." + WareneingangFac.FLR_WE_T_WARENEINGANGSDATUM, c.getTime()));
				crit2.addOrder(Order.asc("p.c_name1nachnamefirmazeile1"));

				List<?> results2 = crit2.list();
				Iterator<?> resultListIterator2 = results2.iterator();
				while (resultListIterator2.hasNext()) {
					FLRWareneingangspositionen wepos = (FLRWareneingangspositionen) resultListIterator2.next();

					BigDecimal umsatz = wepos.getN_einstandspreis();

					// Mit Welchselkurs zu Mandantenwaehrung multiplizieren
					if (wepos.getFlrwareneingang().getN_wechselkurs() != null
							&& wepos.getFlrwareneingang().getN_wechselkurs().doubleValue() != 0) {
						umsatz = umsatz.divide(wepos.getFlrwareneingang().getN_wechselkurs(), 4,
								BigDecimal.ROUND_HALF_EVEN);

					}

					// Bei Handeingabe ist Umsatz gleich Deckungsbeitrag und
					// kommt in die Kategorie Unbekannt
					if (wepos.getFlrbestellposition().getBestellpositionart_c_nr()
							.equals(LocaleFac.POSITIONSART_HANDEINGABE)
							&& iOptionGruppierung
									.intValue() != LagerReportFac.REPORT_LIEFERANTUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHR
							&& iOptionGruppierung
									.intValue() != LagerReportFac.REPORT_LIEFERANTUMSATZSTATISTIK_OPTION_GRUPPIERUNG_MONAT) {
						umsatz = umsatz.multiply(wepos.getN_geliefertemenge());
						kdums.getSubSummeUmsatz()[0] = kdums.getSubSummeUmsatz()[0].add(umsatz);
						// GESAMTSUMME
						kdums.setBdUmsatz(kdums.getBdUmsatz().add(umsatz));
					} else if (wepos.getFlrbestellposition().getBestellpositionart_c_nr()
							.equals(LocaleFac.POSITIONSART_IDENT)
							|| (wepos.getFlrbestellposition().getBestellpositionart_c_nr()
									.equals(LocaleFac.POSITIONSART_HANDEINGABE)
									&& ((iOptionGruppierung
											.intValue() == LagerReportFac.REPORT_LIEFERANTUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHR)
											|| iOptionGruppierung
													.intValue() == LagerReportFac.REPORT_LIEFERANTUMSATZSTATISTIK_OPTION_GRUPPIERUNG_MONAT))) {
						umsatz = umsatz.multiply(wepos.getN_geliefertemenge());

						// GESAMTSUMME
						kdums.setBdUmsatz(kdums.getBdUmsatz().add(umsatz));

						// Aufteilen auf Gruppe
						if (iOptionGruppierung
								.intValue() == LagerReportFac.REPORT_LIEFERANTUMSATZSTATISTIK_OPTION_GRUPPIERUNG_ARTIKELKLASSE) {
							if (wepos.getFlrbestellposition().getFlrartikel().getFlrartikelklasse() != null) {
								// Bei richtiger Artikelklasse einfuegen

								String klasse = wepos.getFlrbestellposition().getFlrartikel().getFlrartikelklasse()
										.getC_nr();

								if (bNurHauptgruppeKlasse == true && zuordnungVatergruppenKlassen != null) {
									klasse = zuordnungVatergruppenKlassen.get(klasse);
								}

								for (int j = 0; j < gruppierung.size(); j++) {
									if (gruppierung.get(j).equals(klasse)) {
										kdums.getSubSummeUmsatz()[j + 1] = kdums.getSubSummeUmsatz()[j + 1].add(umsatz);
										bSpalteAndrucken[j] = true;
									}
								}
							} else {
								kdums.getSubSummeUmsatz()[0] = kdums.getSubSummeUmsatz()[0].add(umsatz);
							}
						} else if (iOptionGruppierung
								.intValue() == LagerReportFac.REPORT_LIEFERANTUMSATZSTATISTIK_OPTION_GRUPPIERUNG_ARTIKELGRUPPE) {
							if (wepos.getFlrbestellposition().getFlrartikel().getFlrartikelgruppe() != null) {
								// Bei richtiger Gruppe einfuegen

								String gruppe = wepos.getFlrbestellposition().getFlrartikel().getFlrartikelgruppe()
										.getC_nr();

								if (bNurHauptgruppeKlasse == true && zuordnungVatergruppenKlassen != null) {
									gruppe = zuordnungVatergruppenKlassen.get(gruppe);
								}

								for (int j = 0; j < gruppierung.size(); j++) {
									if (gruppierung.get(j).equals(gruppe)) {

										kdums.getSubSummeUmsatz()[j + 1] = kdums.getSubSummeUmsatz()[j + 1].add(umsatz);
										bSpalteAndrucken[j] = true;
									}
								}
							} else {
								kdums.getSubSummeUmsatz()[0] = kdums.getSubSummeUmsatz()[0].add(umsatz);
							}
						} else if (iOptionGruppierung
								.intValue() == LagerReportFac.REPORT_LIEFERANTUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHR) {

							Calendar cZeile = Calendar.getInstance();
							cZeile.setTimeInMillis(wepos.getFlrwareneingang().getT_wareneingansdatum().getTime());
							String jahr = cZeile.get(Calendar.YEAR) + "";

							for (int j = 0; j < gruppierung.size(); j++) {
								if (gruppierung.get(j).equals(jahr)) {

									kdums.getSubSummeUmsatz()[j + 1] = kdums.getSubSummeUmsatz()[j + 1].add(umsatz);
									bSpalteAndrucken[j] = true;
								}
							}

						} else if (iOptionGruppierung
								.intValue() == LagerReportFac.REPORT_LIEFERANTUMSATZSTATISTIK_OPTION_GRUPPIERUNG_MONAT) {
							Calendar cZeile = Calendar.getInstance();
							cZeile.setTimeInMillis(wepos.getFlrwareneingang().getT_wareneingansdatum().getTime());
							String jahrMonat = monate[cZeile.get(Calendar.MONTH)] + " " + cZeile.get(Calendar.YEAR);

							for (int j = 0; j < gruppierung.size(); j++) {
								if (gruppierung.get(j).equals(jahrMonat)) {

									kdums.getSubSummeUmsatz()[j + 1] = kdums.getSubSummeUmsatz()[j + 1].add(umsatz);
									bSpalteAndrucken[j] = true;
								}
							}
						}
					}
				}
				session2.close();

				if (results2.size() > 0) {
					gesamtListe.add(kdums);

					gesamtUmsatz = gesamtUmsatz.add(kdums.getBdUmsatz());
				}
			} else {
				// Eingangsrechnung
				Session session2 = FLRSessionFactory.getFactory().openSession();

				org.hibernate.Criteria crit2 = session2.createCriteria(FLREingangsrechnungReport.class)
						.createAlias("flrlieferant", "l");
				crit2.add(Restrictions.eq("l.i_id", lieferant.getI_id()));
				crit2.add(Restrictions.ge(EingangsrechnungFac.FLR_ER_D_BELEGDATUM, Helper.cutTimestamp(tVon)));

				Calendar c = Calendar.getInstance();
				c.setTime(tBis);
				c.set(Calendar.HOUR_OF_DAY, 23);
				c.set(Calendar.MINUTE, 59);
				c.set(Calendar.SECOND, 59);
				c.set(Calendar.MILLISECOND, 999);
				crit2.add(Restrictions.le(EingangsrechnungFac.FLR_ER_D_BELEGDATUM, c.getTime()));

				List<?> results2 = crit2.list();
				Iterator<?> resultListIterator2 = results2.iterator();

				while (resultListIterator2.hasNext()) {
					FLREingangsrechnungReport er = (FLREingangsrechnungReport) resultListIterator2.next();

					if (iOptionGruppierung
							.intValue() == LagerReportFac.REPORT_LIEFERANTUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHR) {

						Calendar cZeile = Calendar.getInstance();
						cZeile.setTimeInMillis(er.getT_belegdatum().getTime());
						String jahr = cZeile.get(Calendar.YEAR) + "";

						for (int j = 0; j < gruppierung.size(); j++) {
							if (gruppierung.get(j).equals(jahr)) {

								kdums.getSubSummeUmsatz()[j + 1] = kdums.getSubSummeUmsatz()[j + 1]
										.add(er.getN_betrag().subtract(er.getN_ustbetrag()));

								kdums.setBdUmsatz(
										kdums.getBdUmsatz().add(er.getN_betrag().subtract(er.getN_ustbetrag())));

								bSpalteAndrucken[j] = true;
							}
						}

					} else if (iOptionGruppierung
							.intValue() == LagerReportFac.REPORT_LIEFERANTUMSATZSTATISTIK_OPTION_GRUPPIERUNG_MONAT) {

						Calendar cZeile = Calendar.getInstance();
						cZeile.setTimeInMillis(er.getT_belegdatum().getTime());

						String jahrMonat = monate[cZeile.get(Calendar.MONTH)] + " " + cZeile.get(Calendar.YEAR);

						for (int j = 0; j < gruppierung.size(); j++) {
							if (gruppierung.get(j).equals(jahrMonat)) {

								kdums.getSubSummeUmsatz()[j + 1] = kdums.getSubSummeUmsatz()[j + 1]
										.add(er.getN_betrag().subtract(er.getN_ustbetrag()));

								kdums.setBdUmsatz(
										kdums.getBdUmsatz().add(er.getN_betrag().subtract(er.getN_ustbetrag())));

								bSpalteAndrucken[j] = true;
							}
						}

					} else {

						kdums.getSubSummeUmsatz()[0] = kdums.getSubSummeUmsatz()[0]
								.add(er.getN_betrag().subtract(er.getN_ustbetrag()));
						kdums.setBdUmsatz(kdums.getBdUmsatz().add(er.getN_betrag().subtract(er.getN_ustbetrag())));
					}
				}

				if (results2.size() > 0) {
					gesamtListe.add(kdums);

					gesamtUmsatz = gesamtUmsatz.add(kdums.getBdUmsatz());
				}

			}
		}
		session.close();

		JasperPrintLP print = null;

		// NACH UMSATZ SORTIEREN
		int n = gesamtListe.size();
		Object temp;
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < n - 1 - i; j++) {
				double a = ((KundeUmsatzstatistikDto) gesamtListe.get(j)).getBdUmsatz().doubleValue();
				double b = ((KundeUmsatzstatistikDto) gesamtListe.get(j + 1)).getBdUmsatz().doubleValue();
				if (a < b) {
					temp = gesamtListe.get(j);
					gesamtListe.set(j, gesamtListe.get(j + 1));
					gesamtListe.set(j + 1, temp);
				}

			}
		}

		// ABC-Klassifizierung
		// A=80%
		// B=15%
		// C=5%

		double dTempUmsatz = gesamtUmsatz.doubleValue();

		ParametermandantDto mandantparameterA = null;
		ParametermandantDto mandantparameterB = null;
		try {
			mandantparameterA = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_KUNDENBEWERTUNG_WERT_A);
			mandantparameterB = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_KUNDENBEWERTUNG_WERT_B);

		} catch (RemoteException ex3) {
			throwEJBExceptionLPRespectOld(ex3);
		}

		BigDecimal paramBUmsatz = gesamtUmsatz.multiply((new BigDecimal(mandantparameterB.getCWert())
				.divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_EVEN)));

		BigDecimal paramAUmsatz = gesamtUmsatz.multiply((new BigDecimal(mandantparameterA.getCWert())
				.divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_EVEN)));

		for (int i = 0; i < gesamtListe.size(); i++) {
			KundeUmsatzstatistikDto dto = (KundeUmsatzstatistikDto) gesamtListe.get(i);

			if (dTempUmsatz < gesamtUmsatz.doubleValue() - (paramAUmsatz.doubleValue() + paramBUmsatz.doubleValue())) {
				dto.setAbcKlassifizierung("C");
			} else if (dTempUmsatz < paramBUmsatz.doubleValue()) {
				dto.setAbcKlassifizierung("B");
			} else {
				dto.setAbcKlassifizierung("A");
			}

			dTempUmsatz = dTempUmsatz - dto.getBdUmsatz().doubleValue();

			gesamtListe.set(i, dto);
		}

		// Nach Name sortieren

		// NACH NAME SORTIEREN, Wenn angegeben

		if (iOptionSortierung.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_SORTIERUNG_FIRMANNAME) {
			n = gesamtListe.size();
			for (int i = 0; i < n - 1; i++) {
				for (int j = 0; j < n - 1 - i; j++) {
					String a = ((KundeUmsatzstatistikDto) gesamtListe.get(j)).getSKunde();
					String b = ((KundeUmsatzstatistikDto) gesamtListe.get(j + 1)).getSKunde();
					if (a.compareTo(b) > 0) {
						temp = gesamtListe.get(j);
						gesamtListe.set(j, gesamtListe.get(j + 1));
						gesamtListe.set(j + 1, temp);
					}
				}
			}
		}

		// Nach Branche/Partnerklasse Gruppieren

		if (iOptionKundengruppierung
				.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_KUNDENGRUPPIERUNG_BRANCHE
				|| iOptionKundengruppierung
						.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_KUNDENGRUPPIERUNG_PARTNERKLASSE) {
			n = gesamtListe.size();
			for (int i = 0; i < n - 1; i++) {
				for (int j = 0; j < n - 1 - i; j++) {
					String a = ((KundeUmsatzstatistikDto) gesamtListe.get(j)).getSKundengruppierung();
					String b = ((KundeUmsatzstatistikDto) gesamtListe.get(j + 1)).getSKundengruppierung();
					if (a.compareTo(b) > 0) {
						temp = gesamtListe.get(j);
						gesamtListe.set(j, gesamtListe.get(j + 1));
						gesamtListe.set(j + 1, temp);
					}
				}
			}

		}

		// SPALTENHEADER entfernen, wenn Spalte leer
		for (int i = bSpalteAndrucken.length; i > 0; i--) {
			if (bSpalteAndrucken[i - 1] == false) {
				gruppierung.remove(i - 1);
			}
		}

		int[] spaltenDrucken = new int[gruppierung.size() + 1];
		spaltenDrucken[0] = 0;
		int x = 1;
		for (int i = 1; i < bSpalteAndrucken.length + 1; i++) {
			if (bSpalteAndrucken[i - 1] == true) {
				spaltenDrucken[x] = i;
				x++;
			}
		}

		int seiten_breite = (gruppierung.size() / 8) + 1;

		for (int i = 0; i < seiten_breite; i++) {
			index = -1;

			sAktuellerReport = LagerReportFac.REPORT_LIEFERANTUMSATZSTATISTIK;

			data = new Object[gesamtListe.size()][REPORT_LIEFERANTUMSATZSTATISTIK_ANZAHL_SPALTEN];
			for (int j = 0; j < gesamtListe.size(); j++) {
				KundeUmsatzstatistikDto dto = (KundeUmsatzstatistikDto) gesamtListe.get(j);
				data[j][REPORT_LIEFERANTUMSATZSTATISTIK_LIEFERANTGRUPPIERUNG] = dto.getSKundengruppierung();
				data[j][REPORT_LIEFERANTUMSATZSTATISTIK_LIEFERANT] = dto.getSKunde();
				data[j][REPORT_LIEFERANTUMSATZSTATISTIK_UMSATZ] = dto.getBdUmsatz();
				data[j][REPORT_LIEFERANTUMSATZSTATISTIK_ABCKLASSE] = dto.getAbcKlassifizierung();
				data[j][REPORT_LIEFERANTUMSATZSTATISTIK_ZAHLUNGSZIEL] = getMandantFac()
						.zahlungszielFindByIIdLocaleOhneExc(dto.getIZahlungsziel(), theClientDto.getLocMandant(),
								theClientDto);
				data[j][REPORT_LIEFERANTUMSATZSTATISTIK_LIEFERART] = getLocaleFac().lieferartFindByIIdLocaleOhneExc(
						dto.getILieferart(), theClientDto.getLocMandant(), theClientDto);
				data[j][REPORT_LIEFERANTUMSATZSTATISTIK_SPEDITEUR] = getMandantFac()
						.spediteurFindByPrimaryKey(dto.getISpediteur()).getCNamedesspediteurs();

				data[j][REPORT_LIEFERANTUMSATZSTATISTIK_LKZ] = dto.getSLkz();
				data[j][REPORT_LIEFERANTUMSATZSTATISTIK_PLZ] = dto.getSPlz();
				data[j][REPORT_LIEFERANTUMSATZSTATISTIK_ORT] = dto.getSOrt();
				data[j][REPORT_LIEFERANTUMSATZSTATISTIK_LIEFERANT_I_ID] = dto.getLieferantIId();

				try {
					if (i == 0) {
						data[j][REPORT_LIEFERANTUMSATZSTATISTIK_SPALTE1] = dto.getSubSummeUmsatz()[0];
					} else {
						data[j][REPORT_LIEFERANTUMSATZSTATISTIK_SPALTE1] = dto
								.getSubSummeUmsatz()[spaltenDrucken[0 + (i * 8)]];
					}
					data[j][REPORT_LIEFERANTUMSATZSTATISTIK_SPALTE2] = dto
							.getSubSummeUmsatz()[spaltenDrucken[1 + (i * 8)]];
					data[j][REPORT_LIEFERANTUMSATZSTATISTIK_SPALTE3] = dto
							.getSubSummeUmsatz()[spaltenDrucken[2 + (i * 8)]];
					data[j][REPORT_LIEFERANTUMSATZSTATISTIK_SPALTE4] = dto
							.getSubSummeUmsatz()[spaltenDrucken[3 + (i * 8)]];
					data[j][REPORT_LIEFERANTUMSATZSTATISTIK_SPALTE5] = dto
							.getSubSummeUmsatz()[spaltenDrucken[4 + (i * 8)]];
					data[j][REPORT_LIEFERANTUMSATZSTATISTIK_SPALTE6] = dto
							.getSubSummeUmsatz()[spaltenDrucken[5 + (i * 8)]];
					data[j][REPORT_LIEFERANTUMSATZSTATISTIK_SPALTE7] = dto
							.getSubSummeUmsatz()[spaltenDrucken[6 + (i * 8)]];
					data[j][REPORT_LIEFERANTUMSATZSTATISTIK_SPALTE8] = dto
							.getSubSummeUmsatz()[spaltenDrucken[7 + (i * 8)]];
				} catch (ArrayIndexOutOfBoundsException ex1) {
					// nothing
				}

			}

			HashMap<String, Object> parameter = new HashMap<String, Object>();
			parameter.put("P_VON", tVon);
			parameter.put("P_BIS", tBis);

			parameter.put("P_NUR_HAUPTGRUPPEKLASSE", new Boolean(bNurHauptgruppeKlasse));

			if (bWareneingangspositionen) {
				parameter.put("P_BASIS", getTextRespectUISpr("bes.wareneingangsposition", theClientDto.getMandant(),
						theClientDto.getLocUi()));
			} else {
				parameter.put("P_BASIS",
						getTextRespectUISpr("er.eingangsrechnung", theClientDto.getMandant(), theClientDto.getLocUi()));

			}

			if (iOptionKundengruppierung
					.intValue() == LagerReportFac.REPORT_LIEFERANTUMSATZSTATISTIK_OPTION_KUNDENGRUPPIERUNG_BRANCHE) {
				parameter.put("P_GRUPPIERUNGLIEFERANT",
						getTextRespectUISpr("lp.branche", theClientDto.getMandant(), theClientDto.getLocUi()));
			} else if (iOptionKundengruppierung
					.intValue() == LagerReportFac.REPORT_LIEFERANTUMSATZSTATISTIK_OPTION_KUNDENGRUPPIERUNG_PARTNERKLASSE) {
				parameter.put("P_GRUPPIERUNGLIEFERANT", "Partnerklasse");
			}

			if (iOptionGruppierung
					.intValue() == LagerReportFac.REPORT_LIEFERANTUMSATZSTATISTIK_OPTION_GRUPPIERUNG_ARTIKELKLASSE) {
				parameter.put("P_GRUPPIERUNG",
						getTextRespectUISpr("lp.artikelklasse", theClientDto.getMandant(), theClientDto.getLocUi()));
			} else if (iOptionGruppierung
					.intValue() == LagerReportFac.REPORT_LIEFERANTUMSATZSTATISTIK_OPTION_GRUPPIERUNG_ARTIKELGRUPPE) {
				parameter.put("P_GRUPPIERUNG",
						getTextRespectUISpr("lp.artikelgruppe", theClientDto.getMandant(), theClientDto.getLocUi()));

			} else if (iOptionGruppierung
					.intValue() == LagerReportFac.REPORT_LIEFERANTUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHR) {
				parameter.put("P_GRUPPIERUNG",
						getTextRespectUISpr("lp.jahr", theClientDto.getMandant(), theClientDto.getLocUi()));

			} else if (iOptionGruppierung
					.intValue() == LagerReportFac.REPORT_LIEFERANTUMSATZSTATISTIK_OPTION_GRUPPIERUNG_MONAT) {
				parameter.put("P_GRUPPIERUNG",
						getTextRespectUISpr("lp.monat", theClientDto.getMandant(), theClientDto.getLocUi()));

			}
			if (iOptionSortierung
					.intValue() == LagerReportFac.REPORT_LIEFERANTUMSATZSTATISTIK_OPTION_SORTIERUNG_FIRMANNAME) {
				parameter.put("P_SORTIERUNG", "Firmenname");
			} else if (iOptionSortierung
					.intValue() == LagerReportFac.REPORT_LIEFERANTUMSATZSTATISTIK_OPTION_SORTIERUNG_UMSATZ) {
				parameter.put("P_SORTIERUNG",
						getTextRespectUISpr("er.umsatz", theClientDto.getMandant(), theClientDto.getLocUi()));

			}

			try {

				parameter.put("P_GRUPPEA", mandantparameterA.getCWertAsObject());
				parameter.put("P_GRUPPEB", mandantparameterB.getCWertAsObject());

				if (i == 0) {
					parameter.put("Spalte1", "Unbekannt");
					parameter.put("Spalte2", gruppierung.get(0 + (i * 8)));
					parameter.put("Spalte3", gruppierung.get(1 + (i * 8)));
					parameter.put("Spalte4", gruppierung.get(2 + (i * 8)));
					parameter.put("Spalte5", gruppierung.get(3 + (i * 8)));
					parameter.put("Spalte6", gruppierung.get(4 + (i * 8)));
					parameter.put("Spalte7", gruppierung.get(5 + (i * 8)));
					parameter.put("Spalte8", gruppierung.get(6 + (i * 8)));

				} else {

					parameter.put("Spalte1", gruppierung.get(7 + (i * 8) - 8));
					parameter.put("Spalte2", gruppierung.get(8 + (i * 8) - 8));
					parameter.put("Spalte3", gruppierung.get(1 + (i * 8)));
					parameter.put("Spalte4", gruppierung.get(2 + (i * 8)));
					parameter.put("Spalte5", gruppierung.get(3 + (i * 8)));
					parameter.put("Spalte6", gruppierung.get(4 + (i * 8)));
					parameter.put("Spalte7", gruppierung.get(5 + (i * 8)));
					parameter.put("Spalte8", gruppierung.get(6 + (i * 80)));

				}

			} catch (IndexOutOfBoundsException ex1) {
				// nothing
			}
			initJRDS(parameter, LagerReportFac.REPORT_MODUL, sAktuellerReport, theClientDto.getMandant(),
					theClientDto.getLocUi(), theClientDto);

			if (print == null) {
				print = getReportPrint();
			} else {
				print = Helper.addReport2Report(print, getReportPrint().getPrint());
			}
		}

		return print;

	}

	private String getVatergruppe(Integer artgruIId) {

		Artgru artgru1 = em.find(Artgru.class, artgruIId);

		if (artgru1.getArtgruIId() == null) {
			return artgru1.getCNr();
		} else {

			Artgru artgruVater = em.find(Artgru.class, artgru1.getArtgruIId());

			if (artgruVater.getArtgruIId() != null) {
				return getVatergruppe(artgruVater.getArtgruIId());
			} else {
				return artgruVater.getCNr();
			}
		}

	}

	public HashMap<String, String> getArtikelgruppeVatergruppe(TheClientDto theClientDto) {

		ArtgruDto[] artgruDtos = getArtikelFac().artgruFindByMandantCNr(theClientDto);

		HashMap<String, String> zuordnungen = new HashMap<String, String>();

		for (int i = 0; i < artgruDtos.length; i++) {

			if (artgruDtos[i].getArtgruIId() == null) {
				zuordnungen.put(artgruDtos[i].getCNr(), artgruDtos[i].getCNr());
			} else {
				zuordnungen.put(artgruDtos[i].getCNr(), getVatergruppe(artgruDtos[i].getArtgruIId()));
			}
		}

		return zuordnungen;

	}

	private String getVaterklasse(Integer artgruIId) {

		Artkla artkla1 = em.find(Artkla.class, artgruIId);

		if (artkla1.getArtklaIId() == null) {
			return artkla1.getCNr();
		} else {

			Artkla artgruVater = em.find(Artkla.class, artkla1.getArtklaIId());

			if (artgruVater.getArtklaIId() != null) {
				return getVatergruppe(artgruVater.getArtklaIId());
			} else {
				return artgruVater.getCNr();
			}
		}

	}

	public HashMap<String, String> getArtikelklasseVaterklasse(TheClientDto theClientDto) {

		ArtklaDto[] artklDtos = getArtikelFac().artklaFindByMandantCNr(theClientDto);
		HashMap<String, String> zuordnungen = new HashMap<String, String>();

		for (int i = 0; i < artklDtos.length; i++) {

			if (artklDtos[i].getArtklaIId() == null) {
				zuordnungen.put(artklDtos[i].getCNr(), artklDtos[i].getCNr());
			} else {
				zuordnungen.put(artklDtos[i].getCNr(), getVaterklasse(artklDtos[i].getArtklaIId()));
			}
		}

		return zuordnungen;

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printKundeumsatzstatistik(Timestamp tVon, Timestamp tBis, Integer iOptionKundengruppierung,
			boolean bUmsatz, Integer iOptionGruppierung, boolean bNurHauptgruppeKlasse, Integer iOptionSortierung,
			Integer iSortierbasisJahre, boolean bVerwendeAuftragsadresse, boolean bVerwendeStatistikadresse,
			boolean bMitNichtLagerbewertetenArtikeln, boolean ohneDBBetrachtung,
			boolean bSortiertNachProvisionsempfaenger, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		List<String> gruppierung = new ArrayList<String>();

		tVon = Helper.cutTimestamp(tVon);
		tBis = Helper.cutTimestamp(tBis);
		String[] monate = new DateFormatSymbols(theClientDto.getLocUi()).getMonths();

		HashMap<String, String> zuordnungVatergruppenKlassen = null;

		try {
			if (iOptionGruppierung
					.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_ARTIKELKLASSE) {

				ArtklaDto[] artklDtos = getArtikelFac().artklaFindByMandantCNr(theClientDto);
				for (int i = 0; i < artklDtos.length; i++) {
					gruppierung.add(artklDtos[i].getCNr());
				}

				if (bNurHauptgruppeKlasse == true) {
					zuordnungVatergruppenKlassen = getArtikelklasseVaterklasse(theClientDto);
				}

			} else if (iOptionGruppierung
					.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_ARTIKELGRUPPE) {

				ArtgruDto[] artgruDtos = getArtikelFac().artgruFindByMandantCNr(theClientDto);
				for (int i = 0; i < artgruDtos.length; i++) {
					gruppierung.add(artgruDtos[i].getCNr());
				}

				if (bNurHauptgruppeKlasse == true) {
					zuordnungVatergruppenKlassen = getArtikelgruppeVatergruppe(theClientDto);
				}

			} else if (iOptionGruppierung
					.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_FERTIGUNGSGRUPPE) {

				FertigungsgruppeDto[] ftgruDtos = getStuecklisteFac()
						.fertigungsgruppeFindByMandantCNr(theClientDto.getMandant(), theClientDto);
				for (int i = 0; i < ftgruDtos.length; i++) {
					gruppierung.add(ftgruDtos[i].getCBez());
				}
			} else if (iOptionGruppierung
					.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHR) {

				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(tVon.getTime());
				c.set(Calendar.DAY_OF_YEAR, 1);
				while (c.getTime().before(tBis)) {
					gruppierung.add(c.get(Calendar.YEAR) + "");
					c.set(Calendar.YEAR, c.get(Calendar.YEAR) + 1);

				}

			} else if (iOptionGruppierung
					.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_MONAT) {

				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(tVon.getTime());
				c.set(Calendar.DAY_OF_MONTH, 1);
				while (c.getTime().before(tBis)) {

					gruppierung.add(monate[c.get(Calendar.MONTH)] + " " + c.get(Calendar.YEAR));
					c.add(Calendar.MONTH, 1);

				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		BigDecimal gesamtUmsatz = new BigDecimal(0);
		BigDecimal gesamtDeckungsbeitrag = new BigDecimal(0);

		ArrayList<Object> gesamtListe = new ArrayList<Object>();

		HashMap<Integer, KundeUmsatzstatistikDto> hmKunden = new HashMap<Integer, KundeUmsatzstatistikDto>();

		// Leere Spalten entfernen

		boolean[] bSpalteAndrucken = new boolean[gruppierung.size()];
		int iKunde = 0;

		Session session2 = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit2 = session2.createCriteria(FLRRechnungPosition.class).createAlias("flrrechnung",
				"r");

		// SP5885
		crit2.add(Restrictions.eq("r.mandant_c_nr", theClientDto.getMandant()));

		crit2.createAlias("r." + RechnungFac.FLR_RECHNUNG_FLRRECHNUNGART, "ra");

		// PJ 14808
		crit2.add(Restrictions.or(
				Restrictions.isNotNull(RechnungFac.FLR_RECHNUNGPOSITION_N_NETTOEINZELPREIS_PLUS_AUFSCHLAG_MINUS_RABATT),
				Restrictions.eq(RechnungFac.FLR_RECHNUNGPOSITIONSART_POSITIONSART_C_NR,
						RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN)));

		// projekt 3568
		String[] arten = new String[2];
		arten[0] = RechnungFac.RECHNUNGART_ANZAHLUNG;
		arten[1] = RechnungFac.RECHNUNGART_PROFORMARECHNUNG;

		crit2.add(Restrictions.not(Restrictions.in("ra.c_nr", arten)));

		String[] stati = new String[2];
		stati[0] = RechnungFac.STATUS_ANGELEGT;
		stati[1] = RechnungFac.STATUS_STORNIERT;

		crit2.add(Restrictions.not(Restrictions.in("r." + RechnungFac.FLR_RECHNUNG_STATUS_C_NR, stati)));

		Calendar c = Calendar.getInstance();
		c.setTime(tVon);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		crit2.add(Restrictions.ge("r." + RechnungFac.FLR_RECHNUNG_D_BELEGDATUM, c.getTime()));

		c.setTime(tBis);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		crit2.add(Restrictions.le("r." + RechnungFac.FLR_RECHNUNG_D_BELEGDATUM, c.getTime()));

		// SP903
		crit2.add(Restrictions.isNull("position_i_id_artikelset"));

		List<?> results2 = crit2.list();
		Iterator<?> resultListIterator2 = results2.iterator();

		int iPos = 0;
		while (resultListIterator2.hasNext()) {
			FLRRechnungPosition rechpos = (FLRRechnungPosition) resultListIterator2.next();

			iPos++;

			// 16882 Ohne Lagerbewertete Artikel
			if (rechpos.getPositionsart_c_nr().equals(LocaleFac.POSITIONSART_IDENT) && rechpos.getFlrartikel() != null
					&& !Helper.short2boolean(rechpos.getFlrartikel().getB_lagerbewertet())) {

				if (bMitNichtLagerbewertetenArtikeln == false) {
					continue;
				}
			}

			if (iPos % 100 == 0) {
				System.out.println(iPos + " von " + results2.size());
			}

			Integer kundeIId = rechpos.getFlrrechnung().getFlrkunde().getI_id();

			if (bVerwendeStatistikadresse) {
				kundeIId = rechpos.getFlrrechnung().getFlrstatistikadresse().getI_id();
			}

			if (bVerwendeAuftragsadresse && rechpos.getAuftragposition_i_id() != null) {
				kundeIId = rechpos.getFlrpositionensichtauftrag().getFlrauftrag().getKunde_i_id_auftragsadresse();
			}

			KundeUmsatzstatistikDto kdums = null;

			if (rechpos.getPositionsart_c_nr().equals(LocaleFac.POSITIONSART_HANDEINGABE)
					|| rechpos.getPositionsart_c_nr().equals(LocaleFac.POSITIONSART_IDENT)) {

				if (!hmKunden.containsKey(kundeIId)) {
					kdums = addKunde2HashMap(tVon, iOptionKundengruppierung, bVerwendeStatistikadresse, theClientDto,
							gruppierung, kundeIId);

					hmKunden.put(kundeIId, kdums);
				} else {
					kdums = hmKunden.get(kundeIId);
				}
			}
			boolean bGutschrift = false;

			BigDecimal umsatz = rechpos.getN_nettoeinzelpreis_plus_aufschlag_minus_rabatt();
			// Gutschrift oder Wertgutschrift
			if (rechpos.getFlrrechnung().getFlrrechnungart().getC_nr().equals(RechnungFac.RECHNUNGART_GUTSCHRIFT)
					|| rechpos.getFlrrechnung().getFlrrechnungart().getC_nr()
							.equals(RechnungFac.RECHNUNGART_WERTGUTSCHRIFT)) {
				bGutschrift = true;
				umsatz = new BigDecimal(0).subtract(umsatz);

			}

			// Mit Welchselkurs zu Mandantenwaehrung dividieren
			if (rechpos.getFlrrechnung().getN_kurs() != null && rechpos.getFlrrechnung().getN_kurs().doubleValue() != 0
					&& !rechpos.getPositionsart_c_nr().equals(RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN)) {
				umsatz = umsatz.divide(rechpos.getFlrrechnung().getN_kurs(), 6, BigDecimal.ROUND_HALF_EVEN);

			}

			// Bei Handeingabe ist Umsatz gleich Deckungsbeitrag und kommt
			// in die Kategorie Unbekannt
			if (rechpos.getPositionsart_c_nr().equals(LocaleFac.POSITIONSART_HANDEINGABE)
					&& iOptionGruppierung
							.intValue() != LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHR
					&& iOptionGruppierung
							.intValue() != LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_MONAT) {
				umsatz = umsatz.multiply(rechpos.getN_menge());
				kdums.getSubSummeUmsatz()[0] = kdums.getSubSummeUmsatz()[0].add(umsatz);
				kdums.getSubSummeDeckungsbeitrag()[0] = kdums.getSubSummeDeckungsbeitrag()[0].add(umsatz);
				// GESAMTSUMME
				kdums.setBdUmsatz(kdums.getBdUmsatz().add(umsatz));
				kdums.setBdDeckungsbeitrag(kdums.getBdDeckungsbeitrag().add(umsatz));

			} else if (rechpos.getPositionsart_c_nr().equals(LocaleFac.POSITIONSART_IDENT)
					|| (rechpos.getPositionsart_c_nr().equals(LocaleFac.POSITIONSART_HANDEINGABE) && iOptionGruppierung
							.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHR)
					|| (rechpos.getPositionsart_c_nr().equals(LocaleFac.POSITIONSART_HANDEINGABE) && iOptionGruppierung
							.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_MONAT)) {

				if (rechpos.getPosition_i_id_artikelset() == null) {

					umsatz = umsatz.multiply(rechpos.getN_menge());
					BigDecimal gestwert = new BigDecimal(0);

					BigDecimal deckungsbeitrag = new BigDecimal(0);
					if (ohneDBBetrachtung == false) {
						if (!rechpos.getPositionsart_c_nr().equals(LocaleFac.POSITIONSART_HANDEINGABE)) {

							if (rechpos.getSetartikel_set().size() == 0) {
								gestwert = getGestpreisFuerUmsatzStatistik(rechpos, bGutschrift);
							} else {
								Iterator it = rechpos.getSetartikel_set().iterator();
								while (it.hasNext()) {
									gestwert = gestwert.add(getGestpreisFuerUmsatzStatistik(
											(FLRRechnungPosition) it.next(), bGutschrift));
								}
							}
						}

						// Mit Welchselkurs zu Mandantenwaehrung dividieren
						if (rechpos.getFlrrechnung().getN_kurs() != null
								&& rechpos.getFlrrechnung().getN_kurs().doubleValue() != 0) {
							gestwert = gestwert.divide(rechpos.getFlrrechnung().getN_kurs(), 6,
									BigDecimal.ROUND_HALF_EVEN);
						}

						deckungsbeitrag = umsatz.subtract(gestwert);
					}

					// GESAMTSUMME
					if (rechpos.getPositionsart_c_nr().equals(LocaleFac.POSITIONSART_HANDEINGABE)) {
						deckungsbeitrag = umsatz;
					}

					kdums.setBdDeckungsbeitrag(kdums.getBdDeckungsbeitrag().add(deckungsbeitrag));

					kdums.setBdUmsatz(kdums.getBdUmsatz().add(umsatz));

					// Aufteilen auf Gruppe
					if (iOptionGruppierung
							.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_ARTIKELKLASSE) {
						if (rechpos.getFlrartikel().getFlrartikelklasse() != null) {
							// Bei richtiger Artikelklasse einfuegen

							String klasse = rechpos.getFlrartikel().getFlrartikelklasse().getC_nr();

							if (klasse.startsWith("ZK")) {
								int z = 0;
							}

							if (bNurHauptgruppeKlasse == true && zuordnungVatergruppenKlassen != null) {
								klasse = zuordnungVatergruppenKlassen.get(klasse);
							}

							for (int j = 0; j < gruppierung.size(); j++) {
								if (gruppierung.get(j).equals(klasse)) {
									kdums.getSubSummeUmsatz()[j + 1] = kdums.getSubSummeUmsatz()[j + 1].add(umsatz);
									kdums.getSubSummeDeckungsbeitrag()[j
											+ 1] = kdums.getSubSummeDeckungsbeitrag()[j + 1].add(deckungsbeitrag);
									bSpalteAndrucken[j] = true;
								}
							}
						} else {
							kdums.getSubSummeUmsatz()[0] = kdums.getSubSummeUmsatz()[0].add(umsatz);
							kdums.getSubSummeDeckungsbeitrag()[0] = kdums.getSubSummeDeckungsbeitrag()[0]
									.add(deckungsbeitrag);
						}
					} else if (iOptionGruppierung
							.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_ARTIKELGRUPPE) {
						if (rechpos.getFlrartikel().getFlrartikelgruppe() != null) {
							// Bei richtiger Gruppe einfuegen

							String gruppe = rechpos.getFlrartikel().getFlrartikelgruppe().getC_nr();
							if (bNurHauptgruppeKlasse == true && zuordnungVatergruppenKlassen != null) {
								gruppe = zuordnungVatergruppenKlassen.get(gruppe);
							}

							for (int j = 0; j < gruppierung.size(); j++) {
								if (gruppierung.get(j).equals(gruppe)) {
									kdums.getSubSummeUmsatz()[j + 1] = kdums.getSubSummeUmsatz()[j + 1].add(umsatz);
									kdums.getSubSummeDeckungsbeitrag()[j
											+ 1] = kdums.getSubSummeDeckungsbeitrag()[j + 1].add(deckungsbeitrag);
									bSpalteAndrucken[j] = true;
								}
							}
						} else {
							kdums.getSubSummeUmsatz()[0] = kdums.getSubSummeUmsatz()[0].add(umsatz);
							kdums.getSubSummeDeckungsbeitrag()[0] = kdums.getSubSummeDeckungsbeitrag()[0]
									.add(deckungsbeitrag);
						}
					} else if (iOptionGruppierung
							.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_FERTIGUNGSGRUPPE) {

						Session sessionFTGruppe = FLRSessionFactory.getFactory().openSession();

						String queryString = "SELECT stkl.flrfertigungsgruppe FROM FLRStueckliste AS stkl"
								+ " WHERE stkl.artikel_i_id= " + rechpos.getFlrartikel().getI_id();

						Query query = sessionFTGruppe.createQuery(queryString);
						List<?> resultList = query.list();
						Iterator<?> resultListIteratorFTGruppe = resultList.iterator();

						String fertigungsgruppe_c_bez = null;

						if (resultListIteratorFTGruppe.hasNext()) {
							FLRFertigungsgruppe gru = (FLRFertigungsgruppe) resultListIteratorFTGruppe.next();
							fertigungsgruppe_c_bez = gru.getC_bez();
						}

						if (fertigungsgruppe_c_bez != null) {
							// Bei richtiger Gruppe einfuegen
							for (int j = 0; j < gruppierung.size(); j++) {
								if (gruppierung.get(j).equals(fertigungsgruppe_c_bez)) {
									kdums.getSubSummeUmsatz()[j + 1] = kdums.getSubSummeUmsatz()[j + 1].add(umsatz);
									kdums.getSubSummeDeckungsbeitrag()[j
											+ 1] = kdums.getSubSummeDeckungsbeitrag()[j + 1].add(deckungsbeitrag);
									bSpalteAndrucken[j] = true;
								}
							}
						} else {
							kdums.getSubSummeUmsatz()[0] = kdums.getSubSummeUmsatz()[0].add(umsatz);
							kdums.getSubSummeDeckungsbeitrag()[0] = kdums.getSubSummeDeckungsbeitrag()[0]
									.add(deckungsbeitrag);
						}

						sessionFTGruppe.close();

					} else if (iOptionGruppierung
							.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHR) {

						Calendar cZeile = Calendar.getInstance();
						cZeile.setTimeInMillis(rechpos.getFlrrechnung().getD_belegdatum().getTime());
						String jahr = cZeile.get(Calendar.YEAR) + "";

						// Bei richtiger Gruppe einfuegen
						for (int j = 0; j < gruppierung.size(); j++) {
							if (gruppierung.get(j).equals(jahr)) {
								kdums.getSubSummeUmsatz()[j + 1] = kdums.getSubSummeUmsatz()[j + 1].add(umsatz);
								kdums.getSubSummeDeckungsbeitrag()[j + 1] = kdums.getSubSummeDeckungsbeitrag()[j + 1]
										.add(deckungsbeitrag);
								bSpalteAndrucken[j] = true;
							}
						}

					} else if (iOptionGruppierung
							.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_MONAT) {

						Calendar cZeile = Calendar.getInstance();
						cZeile.setTimeInMillis(rechpos.getFlrrechnung().getD_belegdatum().getTime());
						String jahrMonat = monate[cZeile.get(Calendar.MONTH)] + " " + cZeile.get(Calendar.YEAR);

						// Bei richtiger Gruppe einfuegen
						for (int j = 0; j < gruppierung.size(); j++) {
							if (gruppierung.get(j).equals(jahrMonat)) {
								kdums.getSubSummeUmsatz()[j + 1] = kdums.getSubSummeUmsatz()[j + 1].add(umsatz);
								kdums.getSubSummeDeckungsbeitrag()[j + 1] = kdums.getSubSummeDeckungsbeitrag()[j + 1]
										.add(deckungsbeitrag);
								bSpalteAndrucken[j] = true;
							}
						}

					}
				}
			}
			// Wenn Rechnungsposition ein LS ist, dann dessen Positionen
			// verwenden
			else if (rechpos.getPositionsart_c_nr().equals(LocaleFac.POSITIONSART_LIEFERSCHEIN)) {
				Integer lieferscheinIId = rechpos.getFlrlieferschein().getI_id();

				Session session3 = FLRSessionFactory.getFactory().openSession();

				org.hibernate.Criteria crit3 = session3.createCriteria(FLRLieferscheinposition.class);
				crit3.createAlias("flrlieferschein", "l");
				crit3.add(Restrictions.eq("l.i_id", lieferscheinIId));
				// SP903
				crit3.add(Restrictions.isNull("position_i_id_artikelset"));
				crit3.add(Restrictions.isNotNull(LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_N_NETTOGESAMTPREIS));
				crit3.add(Restrictions.not(
						Restrictions.eq(LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_N_MENGE, new BigDecimal(0))));

				List<?> results3 = crit3.list();
				Iterator<?> resultListIterator3 = results3.iterator();
				while (resultListIterator3.hasNext()) {
					FLRLieferscheinposition lieferscheinpos = (FLRLieferscheinposition) resultListIterator3.next();

					// 16882 Ohne Lagerbewertete Artikel
					if (lieferscheinpos.getPositionsart_c_nr().equals(LocaleFac.POSITIONSART_IDENT)
							&& lieferscheinpos.getFlrartikel() != null
							&& !Helper.short2boolean(lieferscheinpos.getFlrartikel().getB_lagerbewertet())) {
						if (bMitNichtLagerbewertetenArtikeln == false) {
							continue;
						}
					}

					if (bVerwendeAuftragsadresse && lieferscheinpos.getAuftragposition_i_id() != null
							&& !lieferscheinpos.getFlrpositionensichtauftrag().getFlrauftrag()
									.getKunde_i_id_auftragsadresse().equals(kundeIId)) {
						kundeIId = lieferscheinpos.getFlrpositionensichtauftrag().getFlrauftrag()
								.getKunde_i_id_auftragsadresse();

					}

					if (!hmKunden.containsKey(kundeIId)) {
						kdums = addKunde2HashMap(tVon, iOptionKundengruppierung, bVerwendeStatistikadresse,
								theClientDto, gruppierung, kundeIId);

						hmKunden.put(kundeIId, kdums);
					} else {
						kdums = hmKunden.get(kundeIId);
					}

					// PJ 14525
					BigDecimal umsatzLs = lieferscheinpos.getN_nettogesamtpreisplusversteckteraufschlagminusrabatt()
							.divide(rechpos.getFlrrechnung().getN_kurs(), 6, BigDecimal.ROUND_HALF_EVEN);
					umsatzLs = umsatzLs.multiply(lieferscheinpos.getN_menge());

					if (lieferscheinpos.getPositionsart_c_nr().equals(LocaleFac.POSITIONSART_HANDEINGABE)
							&& iOptionGruppierung
									.intValue() != LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHR
							&& iOptionGruppierung
									.intValue() != LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_MONAT) {
						kdums.getSubSummeUmsatz()[0] = kdums.getSubSummeUmsatz()[0].add(umsatzLs);
						kdums.getSubSummeDeckungsbeitrag()[0] = kdums.getSubSummeDeckungsbeitrag()[0].add(umsatzLs);
						// GESAMTSUMME
						kdums.setBdUmsatz(kdums.getBdUmsatz().add(umsatzLs));
						kdums.setBdDeckungsbeitrag(kdums.getBdDeckungsbeitrag().add(umsatzLs));

					} else if (lieferscheinpos.getPositionsart_c_nr().equals(LocaleFac.POSITIONSART_IDENT)
							|| (lieferscheinpos.getPositionsart_c_nr().equals(LocaleFac.POSITIONSART_HANDEINGABE)
									&& iOptionGruppierung
											.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHR)
							|| (lieferscheinpos.getPositionsart_c_nr().equals(LocaleFac.POSITIONSART_HANDEINGABE)
									&& iOptionGruppierung
											.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_MONAT)) {
						BigDecimal gestpreis = new BigDecimal(0);
						BigDecimal deckungsbeitrag = new BigDecimal(0);
						if (ohneDBBetrachtung == false) {
							try {

								/**
								 * @todo Berechnung fuer negative Lieferscheinpositionen einbauen
								 */
								if (lieferscheinpos.getN_menge().doubleValue() < 0) {
									if (umsatz != null) {
										gestpreis = umsatz;
									}
								} else {

									if (!rechpos.getPositionsart_c_nr().equals(LocaleFac.POSITIONSART_HANDEINGABE)) {

										// PJ 16882
										if (lieferscheinpos.getFlrartikel() != null
												&& Helper.short2boolean(lieferscheinpos.getFlrartikel()
														.getB_lagerbewirtschaftet()) == false
												&& !lieferscheinpos.getFlrartikel().getArtikelart_c_nr()
														.equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
											gestpreis = new BigDecimal(0);
										} else {
											gestpreis = getLagerFac().getGemittelterGestehungspreisEinerAbgangsposition(
													LocaleFac.BELEGART_LIEFERSCHEIN, lieferscheinpos.getI_id());
											gestpreis = gestpreis.multiply(lieferscheinpos.getN_menge());
										}

									}
								}
							} catch (RemoteException ex2) {
								if (ex2.getCause() instanceof EJBExceptionLP) {
									if (((EJBExceptionLP) ex2.getCause())
											.getCode() == EJBExceptionLP.ARTIKEL_KEINE_LAGERBUCHUNG_VORHANDEN) {
										// DANN ALLES OK
									} else {
										throwEJBExceptionLPRespectOld(ex2);
									}
								} else {
									throwEJBExceptionLPRespectOld(ex2);
								}
							}
							deckungsbeitrag = umsatzLs.subtract(gestpreis);
						}

						// GESAMTSUMME

						if (rechpos.getPositionsart_c_nr().equals(LocaleFac.POSITIONSART_HANDEINGABE)) {
							deckungsbeitrag = umsatzLs;
						}

						try {
							kdums.setBdUmsatz(kdums.getBdUmsatz().add(umsatzLs));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						kdums.setBdDeckungsbeitrag(kdums.getBdDeckungsbeitrag().add(deckungsbeitrag));

						if (iOptionGruppierung
								.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_ARTIKELKLASSE) {
							if (lieferscheinpos.getFlrartikel().getFlrartikelklasse() != null) {
								// Bei richtiger Artikelklasse einfuegen

								String klasse = lieferscheinpos.getFlrartikel().getFlrartikelklasse().getC_nr();

								if (bNurHauptgruppeKlasse == true && zuordnungVatergruppenKlassen != null) {
									klasse = zuordnungVatergruppenKlassen.get(klasse);
								}

								for (int j = 0; j < gruppierung.size(); j++) {
									if (gruppierung.get(j).equals(klasse)) {
										kdums.getSubSummeUmsatz()[j + 1] = kdums.getSubSummeUmsatz()[j + 1]
												.add(umsatzLs);
										kdums.getSubSummeDeckungsbeitrag()[j
												+ 1] = kdums.getSubSummeDeckungsbeitrag()[j + 1].add(deckungsbeitrag);
										bSpalteAndrucken[j] = true;

									}
								}
							} else {
								kdums.getSubSummeUmsatz()[0] = kdums.getSubSummeUmsatz()[0].add(umsatzLs);
								kdums.getSubSummeDeckungsbeitrag()[0] = kdums.getSubSummeDeckungsbeitrag()[0]
										.add(deckungsbeitrag);
							}
						} else if (iOptionGruppierung
								.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_ARTIKELGRUPPE) {
							if (lieferscheinpos.getFlrartikel().getFlrartikelgruppe() != null) {
								// Bei richtiger Gruppe einfuegen

								String gruppe = lieferscheinpos.getFlrartikel().getFlrartikelgruppe().getC_nr();

								if (bNurHauptgruppeKlasse == true && zuordnungVatergruppenKlassen != null) {
									gruppe = zuordnungVatergruppenKlassen.get(gruppe);
								}

								for (int j = 0; j < gruppierung.size(); j++) {
									if (gruppierung.get(j).equals(gruppe)) {
										kdums.getSubSummeUmsatz()[j + 1] = kdums.getSubSummeUmsatz()[j + 1]
												.add(umsatzLs);
										kdums.getSubSummeDeckungsbeitrag()[j
												+ 1] = kdums.getSubSummeDeckungsbeitrag()[j + 1].add(deckungsbeitrag);
										bSpalteAndrucken[j] = true;

									}
								}
							} else {
								kdums.getSubSummeUmsatz()[0] = kdums.getSubSummeUmsatz()[0].add(umsatzLs);
								kdums.getSubSummeDeckungsbeitrag()[0] = kdums.getSubSummeDeckungsbeitrag()[0]
										.add(deckungsbeitrag);
							}
						} else if (iOptionGruppierung
								.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_FERTIGUNGSGRUPPE) {

							Session sessionFTGruppe = FLRSessionFactory.getFactory().openSession();

							String queryString = "SELECT stkl.flrfertigungsgruppe FROM FLRStueckliste AS stkl"
									+ " WHERE stkl.artikel_i_id= " + lieferscheinpos.getFlrartikel().getI_id();

							Query query = sessionFTGruppe.createQuery(queryString);
							List<?> resultList = query.list();
							Iterator<?> resultListIteratorFTGruppe = resultList.iterator();

							String fertigungsgruppe_c_bez = null;

							if (resultListIteratorFTGruppe.hasNext()) {
								FLRFertigungsgruppe gru = (FLRFertigungsgruppe) resultListIteratorFTGruppe.next();
								fertigungsgruppe_c_bez = gru.getC_bez();
							}

							if (fertigungsgruppe_c_bez != null) {
								// Bei richtiger Gruppe einfuegen
								for (int j = 0; j < gruppierung.size(); j++) {
									if (gruppierung.get(j).equals(fertigungsgruppe_c_bez)) {
										kdums.getSubSummeUmsatz()[j + 1] = kdums.getSubSummeUmsatz()[j + 1]
												.add(umsatzLs);
										kdums.getSubSummeDeckungsbeitrag()[j
												+ 1] = kdums.getSubSummeDeckungsbeitrag()[j + 1].add(deckungsbeitrag);
										bSpalteAndrucken[j] = true;
									}
								}
							} else {
								kdums.getSubSummeUmsatz()[0] = kdums.getSubSummeUmsatz()[0].add(umsatzLs);
								kdums.getSubSummeDeckungsbeitrag()[0] = kdums.getSubSummeDeckungsbeitrag()[0]
										.add(deckungsbeitrag);
							}
							sessionFTGruppe.close();

						} else if (iOptionGruppierung
								.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHR) {

							Calendar cZeile = Calendar.getInstance();
							cZeile.setTimeInMillis(rechpos.getFlrrechnung().getD_belegdatum().getTime());
							String jahr = cZeile.get(Calendar.YEAR) + "";

							for (int j = 0; j < gruppierung.size(); j++) {
								if (gruppierung.get(j).equals(jahr)) {
									kdums.getSubSummeUmsatz()[j + 1] = kdums.getSubSummeUmsatz()[j + 1].add(umsatzLs);
									kdums.getSubSummeDeckungsbeitrag()[j
											+ 1] = kdums.getSubSummeDeckungsbeitrag()[j + 1].add(deckungsbeitrag);
									bSpalteAndrucken[j] = true;
								}
							}
						} else if (iOptionGruppierung
								.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_MONAT) {

							Calendar cZeile = Calendar.getInstance();
							cZeile.setTimeInMillis(rechpos.getFlrrechnung().getD_belegdatum().getTime());
							String jahrMonat = monate[cZeile.get(Calendar.MONTH)] + " " + cZeile.get(Calendar.YEAR);

							for (int j = 0; j < gruppierung.size(); j++) {
								if (gruppierung.get(j).equals(jahrMonat)) {
									kdums.getSubSummeUmsatz()[j + 1] = kdums.getSubSummeUmsatz()[j + 1].add(umsatzLs);
									kdums.getSubSummeDeckungsbeitrag()[j
											+ 1] = kdums.getSubSummeDeckungsbeitrag()[j + 1].add(deckungsbeitrag);
									bSpalteAndrucken[j] = true;
								}
							}
						}
					}

				}
				session3.close();

			}

		}
		session2.close();

		Iterator it = hmKunden.keySet().iterator();
		while (it.hasNext()) {
			Integer kundeIId = (Integer) it.next();
			KundeUmsatzstatistikDto kdums = hmKunden.get(kundeIId);

			gesamtListe.add(kdums);

			gesamtUmsatz = gesamtUmsatz.add(kdums.getBdUmsatz());
			gesamtDeckungsbeitrag = gesamtDeckungsbeitrag.add(kdums.getBdDeckungsbeitrag());
		}

		JasperPrintLP print = null;

		// NACH UMSATZ SORTIEREN
		int n = gesamtListe.size();
		Object temp;
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < n - 1 - i; j++) {

				if (bUmsatz == true) {

					double a = ((KundeUmsatzstatistikDto) gesamtListe.get(j)).getBdUmsatz().doubleValue();
					double b = ((KundeUmsatzstatistikDto) gesamtListe.get(j + 1)).getBdUmsatz().doubleValue();

					if (iOptionGruppierung == REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHR
							&& iOptionSortierung == REPORT_KUNDEUMSATZSTATISTIK_OPTION_SORTIERUNG_UMSATZ) {
						if (iSortierbasisJahre == REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHRE_SORTIERBASIS_UMSATZ_AKTUELLESJAHR) {
							a = ((KundeUmsatzstatistikDto) gesamtListe.get(j))
									.getSubSummeUmsatz()[((KundeUmsatzstatistikDto) gesamtListe.get(j))
											.getSubSummeUmsatz().length - 1].doubleValue();
							b = ((KundeUmsatzstatistikDto) gesamtListe.get(j + 1))
									.getSubSummeUmsatz()[((KundeUmsatzstatistikDto) gesamtListe.get(j))
											.getSubSummeUmsatz().length - 1].doubleValue();
						} else if (iSortierbasisJahre == REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHRE_SORTIERBASIS_UMSATZ_VORJAHR
								&& ((KundeUmsatzstatistikDto) gesamtListe.get(j)).getSubSummeUmsatz().length > 1) {
							a = ((KundeUmsatzstatistikDto) gesamtListe.get(j))
									.getSubSummeUmsatz()[((KundeUmsatzstatistikDto) gesamtListe.get(j))
											.getSubSummeUmsatz().length - 2].doubleValue();
							b = ((KundeUmsatzstatistikDto) gesamtListe.get(j + 1))
									.getSubSummeUmsatz()[((KundeUmsatzstatistikDto) gesamtListe.get(j))
											.getSubSummeUmsatz().length - 2].doubleValue();
						}
					}

					if (bSortiertNachProvisionsempfaenger) {

						String sA = ((KundeUmsatzstatistikDto) gesamtListe.get(j)).getProvisionsempfaengerUndUmsatz(a);
						String sB = ((KundeUmsatzstatistikDto) gesamtListe.get(j + 1))
								.getProvisionsempfaengerUndUmsatz(b);

						if (sA.compareTo(sB) > 0) {
							temp = gesamtListe.get(j);
							gesamtListe.set(j, gesamtListe.get(j + 1));
							gesamtListe.set(j + 1, temp);
						}

					} else {

						if (a < b) {
							temp = gesamtListe.get(j);
							gesamtListe.set(j, gesamtListe.get(j + 1));
							gesamtListe.set(j + 1, temp);
						}
					}

				} else {
					double a = ((KundeUmsatzstatistikDto) gesamtListe.get(j)).getBdDeckungsbeitrag().doubleValue();
					double b = ((KundeUmsatzstatistikDto) gesamtListe.get(j + 1)).getBdDeckungsbeitrag().doubleValue();

					if (iOptionGruppierung == REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHR
							&& iOptionSortierung == REPORT_KUNDEUMSATZSTATISTIK_OPTION_SORTIERUNG_UMSATZ) {
						if (iSortierbasisJahre == REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHRE_SORTIERBASIS_UMSATZ_AKTUELLESJAHR) {
							a = ((KundeUmsatzstatistikDto) gesamtListe.get(j))
									.getSubSummeDeckungsbeitrag()[((KundeUmsatzstatistikDto) gesamtListe.get(j))
											.getSubSummeDeckungsbeitrag().length - 1].doubleValue();
							b = ((KundeUmsatzstatistikDto) gesamtListe.get(j + 1))
									.getSubSummeDeckungsbeitrag()[((KundeUmsatzstatistikDto) gesamtListe.get(j))
											.getSubSummeDeckungsbeitrag().length - 1].doubleValue();
						} else if (iSortierbasisJahre == REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHRE_SORTIERBASIS_UMSATZ_VORJAHR
								&& ((KundeUmsatzstatistikDto) gesamtListe.get(j))
										.getSubSummeDeckungsbeitrag().length > 1) {
							a = ((KundeUmsatzstatistikDto) gesamtListe.get(j))
									.getSubSummeDeckungsbeitrag()[((KundeUmsatzstatistikDto) gesamtListe.get(j))
											.getSubSummeDeckungsbeitrag().length - 2].doubleValue();
							b = ((KundeUmsatzstatistikDto) gesamtListe.get(j + 1))
									.getSubSummeDeckungsbeitrag()[((KundeUmsatzstatistikDto) gesamtListe.get(j))
											.getSubSummeDeckungsbeitrag().length - 2].doubleValue();
						}
					}
					if (bSortiertNachProvisionsempfaenger) {

						String sA = ((KundeUmsatzstatistikDto) gesamtListe.get(j)).getProvisionsempfaengerUndDB(a);
						String sB = ((KundeUmsatzstatistikDto) gesamtListe.get(j + 1)).getProvisionsempfaengerUndDB(b);

						if (sA.compareTo(sB) > 0) {
							temp = gesamtListe.get(j);
							gesamtListe.set(j, gesamtListe.get(j + 1));
							gesamtListe.set(j + 1, temp);
						}

					} else {

						if (a < b) {
							temp = gesamtListe.get(j);
							gesamtListe.set(j, gesamtListe.get(j + 1));
							gesamtListe.set(j + 1, temp);
						}
					}

				}
			}
		}

		// ABC-Klassifizierung
		// A=80%
		// B=15%
		// C=5%

		BigDecimal bdTempUmsatz = BigDecimal.ZERO;
		BigDecimal bdTempDB = BigDecimal.ZERO;

		ParametermandantDto mandantparameterA = null;
		ParametermandantDto mandantparameterB = null;
		try {
			mandantparameterA = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_KUNDENBEWERTUNG_WERT_A);
			mandantparameterB = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_KUNDENBEWERTUNG_WERT_B);

		} catch (RemoteException ex3) {
			throwEJBExceptionLPRespectOld(ex3);
		}

		BigDecimal paramAUmsatz = gesamtUmsatz.multiply((new BigDecimal(mandantparameterA.getCWert())
				.divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_EVEN)));
		BigDecimal paramBUmsatz = gesamtUmsatz.multiply((new BigDecimal(mandantparameterB.getCWert())
				.divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_EVEN))).add(paramAUmsatz);

		BigDecimal paramADeckungsbeitrag = gesamtDeckungsbeitrag.multiply((new BigDecimal(mandantparameterA.getCWert())
				.divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_EVEN)));
		BigDecimal paramBDeckungsbeitrag = gesamtDeckungsbeitrag.multiply((new BigDecimal(mandantparameterB.getCWert())
				.divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_EVEN))).add(paramADeckungsbeitrag);

		for (int i = 0; i < gesamtListe.size(); i++) {
			KundeUmsatzstatistikDto dto = (KundeUmsatzstatistikDto) gesamtListe.get(i);

			if (bUmsatz == true) {
				if (bdTempUmsatz.compareTo(paramAUmsatz) <= 0) {
					dto.setAbcKlassifizierung("A");
				} else if (bdTempUmsatz.compareTo(paramBUmsatz) <= 0) {
					dto.setAbcKlassifizierung("B");
				} else {
					dto.setAbcKlassifizierung("C");
				}
				bdTempUmsatz = bdTempUmsatz.add(dto.getBdUmsatz());

			} else {
				if (bdTempDB.compareTo(paramADeckungsbeitrag) <= 0) {
					dto.setAbcKlassifizierung("A");
				} else if (bdTempDB.compareTo(paramBDeckungsbeitrag) <= 0) {
					dto.setAbcKlassifizierung("B");
				} else {
					dto.setAbcKlassifizierung("C");
				}
				bdTempDB = bdTempDB.add(dto.getBdDeckungsbeitrag());

			}
			gesamtListe.set(i, dto);
		}

		// Nach Name sortieren

		// NACH NAME SORTIEREN, Wenn angegeben

		if (iOptionSortierung.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_SORTIERUNG_FIRMANNAME) {
			n = gesamtListe.size();
			for (int i = 0; i < n - 1; i++) {
				for (int j = 0; j < n - 1 - i; j++) {
					String a = ((KundeUmsatzstatistikDto) gesamtListe.get(j)).getSKunde();
					String b = ((KundeUmsatzstatistikDto) gesamtListe.get(j + 1)).getSKunde();

					if (bSortiertNachProvisionsempfaenger) {
						a = ((KundeUmsatzstatistikDto) gesamtListe.get(j)).getProvisionsempfaengerUndKunde();
						b = ((KundeUmsatzstatistikDto) gesamtListe.get(j + 1)).getProvisionsempfaengerUndKunde();
					}

					if (a.compareTo(b) > 0) {
						temp = gesamtListe.get(j);
						gesamtListe.set(j, gesamtListe.get(j + 1));
						gesamtListe.set(j + 1, temp);
					}
				}
			}
		}

		else if (iOptionSortierung.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_SORTIERUNG_LKZ) {
			n = gesamtListe.size();
			for (int i = 0; i < n - 1; i++) {
				for (int j = 0; j < n - 1 - i; j++) {
					String a = ((KundeUmsatzstatistikDto) gesamtListe.get(j)).getSLkz();
					String b = ((KundeUmsatzstatistikDto) gesamtListe.get(j + 1)).getSLkz();

					if (bSortiertNachProvisionsempfaenger) {
						a = ((KundeUmsatzstatistikDto) gesamtListe.get(j)).getProvisionsempfaengerUndLkz();
						b = ((KundeUmsatzstatistikDto) gesamtListe.get(j + 1)).getProvisionsempfaengerUndLkz();
						if (a.compareTo(b) > 0) {
							temp = gesamtListe.get(j);
							gesamtListe.set(j, gesamtListe.get(j + 1));
							gesamtListe.set(j + 1, temp);
						}
					} else {

						if (a.compareTo(b) > 0) {
							temp = gesamtListe.get(j);
							gesamtListe.set(j, gesamtListe.get(j + 1));
							gesamtListe.set(j + 1, temp);
						} else if (a.compareTo(b) == 0) {
							String plz1 = ((KundeUmsatzstatistikDto) gesamtListe.get(j)).getSPlz();
							String plz2 = ((KundeUmsatzstatistikDto) gesamtListe.get(j + 1)).getSPlz();

							if (plz1.compareTo(plz2) > 0) {
								temp = gesamtListe.get(j);
								gesamtListe.set(j, gesamtListe.get(j + 1));
								gesamtListe.set(j + 1, temp);
							}

						}
					}
				}
			}
		}

		// Nach Branche/Partnerklasse Gruppieren

		if (iOptionKundengruppierung
				.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_KUNDENGRUPPIERUNG_BRANCHE
				|| iOptionKundengruppierung
						.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_KUNDENGRUPPIERUNG_PARTNERKLASSE) {
			n = gesamtListe.size();
			for (int i = 0; i < n - 1; i++) {
				for (int j = 0; j < n - 1 - i; j++) {
					String a = ((KundeUmsatzstatistikDto) gesamtListe.get(j)).getSKundengruppierung();
					String b = ((KundeUmsatzstatistikDto) gesamtListe.get(j + 1)).getSKundengruppierung();
					if (a.compareTo(b) > 0) {
						temp = gesamtListe.get(j);
						gesamtListe.set(j, gesamtListe.get(j + 1));
						gesamtListe.set(j + 1, temp);
					}
				}
			}

		}

		// SPALTENHEADER entfernen, wenn Spalte leer
		for (int i = bSpalteAndrucken.length; i > 0; i--) {
			if (bSpalteAndrucken[i - 1] == false) {
				gruppierung.remove(i - 1);
			}
		}

		int[] spaltenDrucken = new int[gruppierung.size() + 1];
		spaltenDrucken[0] = 0;
		int x = 1;
		for (int i = 1; i < bSpalteAndrucken.length + 1; i++) {
			if (bSpalteAndrucken[i - 1] == true) {
				spaltenDrucken[x] = i;
				x++;
			}
		}

		int seiten_breite = (gruppierung.size() / 8) + 1;

		// PJ20822

		BigDecimal bdGroessteSummeSpalte = BigDecimal.ZERO;

		for (int i = 0; i < seiten_breite; i++) {
			index = -1;
			BigDecimal bdSummeSpalte0 = BigDecimal.ZERO;
			BigDecimal bdSummeSpalte1 = BigDecimal.ZERO;
			BigDecimal bdSummeSpalte2 = BigDecimal.ZERO;
			BigDecimal bdSummeSpalte3 = BigDecimal.ZERO;
			BigDecimal bdSummeSpalte4 = BigDecimal.ZERO;
			BigDecimal bdSummeSpalte5 = BigDecimal.ZERO;
			BigDecimal bdSummeSpalte6 = BigDecimal.ZERO;
			BigDecimal bdSummeSpalte7 = BigDecimal.ZERO;
			BigDecimal bdSummeSpalte8 = BigDecimal.ZERO;

			for (int j = 0; j < gesamtListe.size(); j++) {
				KundeUmsatzstatistikDto dto = (KundeUmsatzstatistikDto) gesamtListe.get(j);

				try {
					if (i == 0) {
						// Gesamt
					} else {
						bdSummeSpalte0 = bdSummeSpalte0.add(dto.getSubSummeUmsatz()[spaltenDrucken[0 + (i * 8)]]);

					}

					bdSummeSpalte1 = bdSummeSpalte1.add(dto.getSubSummeUmsatz()[spaltenDrucken[1 + (i * 8)]]);
					bdSummeSpalte2 = bdSummeSpalte2.add(dto.getSubSummeUmsatz()[spaltenDrucken[2 + (i * 8)]]);
					bdSummeSpalte3 = bdSummeSpalte3.add(dto.getSubSummeUmsatz()[spaltenDrucken[3 + (i * 8)]]);
					bdSummeSpalte4 = bdSummeSpalte4.add(dto.getSubSummeUmsatz()[spaltenDrucken[4 + (i * 8)]]);
					bdSummeSpalte5 = bdSummeSpalte5.add(dto.getSubSummeUmsatz()[spaltenDrucken[5 + (i * 8)]]);
					bdSummeSpalte6 = bdSummeSpalte6.add(dto.getSubSummeUmsatz()[spaltenDrucken[6 + (i * 8)]]);
					bdSummeSpalte7 = bdSummeSpalte7.add(dto.getSubSummeUmsatz()[spaltenDrucken[7 + (i * 8)]]);
					bdSummeSpalte8 = bdSummeSpalte8.add(dto.getSubSummeUmsatz()[spaltenDrucken[8 + (i * 8)]]);

				} catch (ArrayIndexOutOfBoundsException ex1) {
					// nothing
				}
			}

			if (bdSummeSpalte0.doubleValue() > bdGroessteSummeSpalte.doubleValue()) {
				bdGroessteSummeSpalte = bdSummeSpalte0;
			}
			if (bdSummeSpalte1.doubleValue() > bdGroessteSummeSpalte.doubleValue()) {
				bdGroessteSummeSpalte = bdSummeSpalte1;
			}
			if (bdSummeSpalte2.doubleValue() > bdGroessteSummeSpalte.doubleValue()) {
				bdGroessteSummeSpalte = bdSummeSpalte2;
			}
			if (bdSummeSpalte3.doubleValue() > bdGroessteSummeSpalte.doubleValue()) {
				bdGroessteSummeSpalte = bdSummeSpalte3;
			}
			if (bdSummeSpalte4.doubleValue() > bdGroessteSummeSpalte.doubleValue()) {
				bdGroessteSummeSpalte = bdSummeSpalte4;
			}
			if (bdSummeSpalte5.doubleValue() > bdGroessteSummeSpalte.doubleValue()) {
				bdGroessteSummeSpalte = bdSummeSpalte5;
			}
			if (bdSummeSpalte6.doubleValue() > bdGroessteSummeSpalte.doubleValue()) {
				bdGroessteSummeSpalte = bdSummeSpalte6;
			}
			if (bdSummeSpalte7.doubleValue() > bdGroessteSummeSpalte.doubleValue()) {
				bdGroessteSummeSpalte = bdSummeSpalte7;
			}
			if (bdSummeSpalte8.doubleValue() > bdGroessteSummeSpalte.doubleValue()) {
				bdGroessteSummeSpalte = bdSummeSpalte8;
			}

		}

		for (int i = 0; i < seiten_breite; i++) {
			index = -1;
			if (bUmsatz == true) {
				sAktuellerReport = LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK;
			} else {
				sAktuellerReport = LagerReportFac.REPORT_KUNDEDBSTATISTIK;
			}
			data = new Object[gesamtListe.size()][REPORT_KUNDEUMSATZSTATISTIK_ANZAHL_SPALTEN];

			for (int j = 0; j < gesamtListe.size(); j++) {
				KundeUmsatzstatistikDto dto = (KundeUmsatzstatistikDto) gesamtListe.get(j);
				data[j][REPORT_KUNDEUMSATZSTATISTIK_KUNDENGRUPPIERUNG] = dto.getSKundengruppierung();
				data[j][REPORT_KUNDEUMSATZSTATISTIK_KUNDE] = dto.getSKunde();
				data[j][REPORT_KUNDEUMSATZSTATISTIK_PROVISIONSEMPFAENGER_KURZZEICHEN] = dto
						.getSProvisionsempfaengerKurzzeichen();
				data[j][REPORT_KUNDEUMSATZSTATISTIK_PROVISIONSEMPFAENGER_NAME] = dto.getSProvisionsempfaengerName();

				data[j][REPORT_KUNDEUMSATZSTATISTIK_KREDITLIMIT] = dto.getBdKreditlimit();
				data[j][REPORT_KUNDEUMSATZSTATISTIK_UMSATZ] = dto.getBdUmsatz();
				data[j][REPORT_KUNDEUMSATZSTATISTIK_PLZ] = dto.getSPlz();
				data[j][REPORT_KUNDEUMSATZSTATISTIK_LKZ] = dto.getSLkz();
				data[j][REPORT_KUNDEUMSATZSTATISTIK_KUNDENNUMMER] = dto.getIKundennummer();

				// PJ20550
				data[j][REPORT_KUNDEUMSATZSTATISTIK_ILN] = dto.getKundeDto().getPartnerDto().getCIln();
				data[j][REPORT_KUNDEUMSATZSTATISTIK_KUNDE_I_ID] = dto.getKundeDto().getIId();
				data[j][REPORT_KUNDEUMSATZSTATISTIK_PARTNER_I_ID] = dto.getKundeDto().getPartnerIId();

				data[j][REPORT_KUNDEUMSATZSTATISTIK_DECKUNGSBEITRAG] = dto.getBdDeckungsbeitrag();
				data[j][REPORT_KUNDEUMSATZSTATISTIK_ERSTUMSATZ] = new Boolean(dto.isBErstumsatz());
				data[j][REPORT_KUNDEUMSATZSTATISTIK_DATUM_ERSTUMSATZ] = dto.getDErstumsatz();
				data[j][REPORT_KUNDEUMSATZSTATISTIK_DATUM_LETZTUMSATZ] = dto.getDLetztumsatz();

				data[j][REPORT_KUNDEUMSATZSTATISTIK_ABCKLASSE] = dto.getAbcKlassifizierung();
				data[j][REPORT_KUNDEUMSATZSTATISTIK_ZAHLUNGSZIEL] = getMandantFac().zahlungszielFindByIIdLocaleOhneExc(
						dto.getIZahlungsziel(), theClientDto.getLocMandant(), theClientDto);
				data[j][REPORT_KUNDEUMSATZSTATISTIK_LIEFERART] = getLocaleFac().lieferartFindByIIdLocaleOhneExc(
						dto.getILieferart(), theClientDto.getLocMandant(), theClientDto);
				data[j][REPORT_KUNDEUMSATZSTATISTIK_SPEDITEUR] = getMandantFac()
						.spediteurFindByPrimaryKey(dto.getISpediteur()).getCNamedesspediteurs();

				try {
					if (i == 0) {
						data[j][REPORT_KUNDEUMSATZSTATISTIK_SPALTE1] = dto.getSubSummeUmsatz()[0];
						data[j][REPORT_KUNDEUMSATZSTATISTIK_SPALTE1DB] = dto.getSubSummeDeckungsbeitrag()[0];
					} else {
						data[j][REPORT_KUNDEUMSATZSTATISTIK_SPALTE1] = dto
								.getSubSummeUmsatz()[spaltenDrucken[0 + (i * 8)]];
						data[j][REPORT_KUNDEUMSATZSTATISTIK_SPALTE1DB] = dto
								.getSubSummeDeckungsbeitrag()[spaltenDrucken[0 + (i * 8)]];
					}
					data[j][REPORT_KUNDEUMSATZSTATISTIK_SPALTE2] = dto.getSubSummeUmsatz()[spaltenDrucken[1 + (i * 8)]];
					data[j][REPORT_KUNDEUMSATZSTATISTIK_SPALTE2DB] = dto
							.getSubSummeDeckungsbeitrag()[spaltenDrucken[1 + (i * 8)]];
					data[j][REPORT_KUNDEUMSATZSTATISTIK_SPALTE3] = dto.getSubSummeUmsatz()[spaltenDrucken[2 + (i * 8)]];
					data[j][REPORT_KUNDEUMSATZSTATISTIK_SPALTE3DB] = dto
							.getSubSummeDeckungsbeitrag()[spaltenDrucken[2 + (i * 8)]];
					data[j][REPORT_KUNDEUMSATZSTATISTIK_SPALTE4] = dto.getSubSummeUmsatz()[spaltenDrucken[3 + (i * 8)]];
					data[j][REPORT_KUNDEUMSATZSTATISTIK_SPALTE4DB] = dto
							.getSubSummeDeckungsbeitrag()[spaltenDrucken[3 + (i * 8)]];
					data[j][REPORT_KUNDEUMSATZSTATISTIK_SPALTE5] = dto.getSubSummeUmsatz()[spaltenDrucken[4 + (i * 8)]];
					data[j][REPORT_KUNDEUMSATZSTATISTIK_SPALTE5DB] = dto
							.getSubSummeDeckungsbeitrag()[spaltenDrucken[4 + (i * 8)]];
					data[j][REPORT_KUNDEUMSATZSTATISTIK_SPALTE6] = dto.getSubSummeUmsatz()[spaltenDrucken[5 + (i * 8)]];
					data[j][REPORT_KUNDEUMSATZSTATISTIK_SPALTE6DB] = dto
							.getSubSummeDeckungsbeitrag()[spaltenDrucken[5 + (i * 8)]];
					data[j][REPORT_KUNDEUMSATZSTATISTIK_SPALTE7] = dto.getSubSummeUmsatz()[spaltenDrucken[6 + (i * 8)]];
					data[j][REPORT_KUNDEUMSATZSTATISTIK_SPALTE7DB] = dto
							.getSubSummeDeckungsbeitrag()[spaltenDrucken[6 + (i * 8)]];
					data[j][REPORT_KUNDEUMSATZSTATISTIK_SPALTE8] = dto.getSubSummeUmsatz()[spaltenDrucken[7 + (i * 8)]];
					data[j][REPORT_KUNDEUMSATZSTATISTIK_SPALTE8DB] = dto
							.getSubSummeDeckungsbeitrag()[spaltenDrucken[7 + (i * 8)]];
				} catch (ArrayIndexOutOfBoundsException ex1) {
					// nothing
				}

			}

			HashMap<String, Object> parameter = new HashMap<String, Object>();
			parameter.put("P_VON", tVon);
			parameter.put("P_BIS", tBis);

			if (bUmsatz == true) {
				parameter.put("P_UMSATZ", new Boolean(true));
			} else {
				parameter.put("P_UMSATZ", new Boolean(false));
			}

			parameter.put("P_MAX_UMSATZ_SPALTE", bdGroessteSummeSpalte);

			parameter.put("P_NUR_HAUPTGRUPPEKLASSE", new Boolean(bNurHauptgruppeKlasse));

			if (iOptionKundengruppierung
					.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_KUNDENGRUPPIERUNG_BRANCHE) {
				parameter.put("P_GRUPPIERUNGKUNDE",
						getTextRespectUISpr("lp.branche", theClientDto.getMandant(), theClientDto.getLocUi()));
			} else if (iOptionKundengruppierung
					.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_KUNDENGRUPPIERUNG_PARTNERKLASSE) {
				parameter.put("P_GRUPPIERUNGKUNDE", "Partnerklasse");
			}

			if (iOptionGruppierung
					.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_ARTIKELKLASSE) {
				parameter.put("P_GRUPPIERUNG",
						getTextRespectUISpr("lp.artikelklasse", theClientDto.getMandant(), theClientDto.getLocUi()));
			} else if (iOptionGruppierung
					.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_ARTIKELGRUPPE) {
				parameter.put("P_GRUPPIERUNG",
						getTextRespectUISpr("lp.artikelgruppe", theClientDto.getMandant(), theClientDto.getLocUi()));

			} else if (iOptionGruppierung
					.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_FERTIGUNGSGRUPPE) {
				parameter.put("P_GRUPPIERUNG",
						getTextRespectUISpr("lp.fertigungsgruppe", theClientDto.getMandant(), theClientDto.getLocUi()));

			} else if (iOptionGruppierung
					.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHR) {
				parameter.put("P_GRUPPIERUNG",
						getTextRespectUISpr("lp.jahr", theClientDto.getMandant(), theClientDto.getLocUi()));

			} else if (iOptionGruppierung
					.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHR) {
				parameter.put("P_GRUPPIERUNG",
						getTextRespectUISpr("lp.monat", theClientDto.getMandant(), theClientDto.getLocUi()));

			}
			if (iOptionSortierung
					.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_SORTIERUNG_FIRMANNAME) {
				parameter.put("P_SORTIERUNG", "Firmenname");
			} else if (iOptionSortierung
					.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_SORTIERUNG_UMSATZ) {
				parameter.put("P_SORTIERUNG",
						getTextRespectUISpr("er.umsatz", theClientDto.getMandant(), theClientDto.getLocUi()));

			} else if (iOptionSortierung
					.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_SORTIERUNG_LKZ) {
				parameter.put("P_SORTIERUNG",
						getTextRespectUISpr("lp.lkzplz", theClientDto.getMandant(), theClientDto.getLocUi()));

			}
			parameter.put("P_STATISTIKADRESSE", new Boolean(bVerwendeStatistikadresse));

			parameter.put("P_AUFTRAGSADRESSE", new Boolean(bVerwendeAuftragsadresse));

			parameter.put("P_MITNICHTLAGERBEWERTETENARTIKELN", new Boolean(bMitNichtLagerbewertetenArtikeln));
			parameter.put("P_OHNE_DBBETRACHTUNG", new Boolean(ohneDBBetrachtung));

			parameter.put("P_SORTIERT_NACH_PROVISIONSEMPFAENGER", new Boolean(bSortiertNachProvisionsempfaenger));

			try {

				parameter.put("P_GRUPPEA", mandantparameterA.getCWertAsObject());
				parameter.put("P_GRUPPEB", mandantparameterB.getCWertAsObject());

				if (i == 0) {
					parameter.put("Spalte1", "Unbekannt");
					parameter.put("Spalte2", gruppierung.get(0 + (i * 8)));
					parameter.put("Spalte3", gruppierung.get(1 + (i * 8)));
					parameter.put("Spalte4", gruppierung.get(2 + (i * 8)));
					parameter.put("Spalte5", gruppierung.get(3 + (i * 8)));
					parameter.put("Spalte6", gruppierung.get(4 + (i * 8)));
					parameter.put("Spalte7", gruppierung.get(5 + (i * 8)));
					parameter.put("Spalte8", gruppierung.get(6 + (i * 8)));

				} else {

					parameter.put("Spalte1", gruppierung.get(7 + (i * 8) - 8));
					parameter.put("Spalte2", gruppierung.get(8 + (i * 8) - 8));
					parameter.put("Spalte3", gruppierung.get(1 + (i * 8)));
					parameter.put("Spalte4", gruppierung.get(2 + (i * 8)));
					parameter.put("Spalte5", gruppierung.get(3 + (i * 8)));
					parameter.put("Spalte6", gruppierung.get(4 + (i * 8)));
					parameter.put("Spalte7", gruppierung.get(5 + (i * 8)));
					parameter.put("Spalte8", gruppierung.get(6 + (i * 80)));

				}

			} catch (IndexOutOfBoundsException ex1) {
				// nothing
			}
			initJRDS(parameter, LagerReportFac.REPORT_MODUL, sAktuellerReport, theClientDto.getMandant(),
					theClientDto.getLocUi(), theClientDto);

			if (print == null) {
				print = getReportPrint();
			} else {
				print = Helper.addReport2Report(print, getReportPrint().getPrint());
			}
		}

		return print;

	}

	private KundeUmsatzstatistikDto addKunde2HashMap(Timestamp tVon, Integer iOptionKundengruppierung,
			boolean bVerwendeStatistikadresse, TheClientDto theClientDto, List<String> gruppierung, Integer kundeIId) {
		KundeUmsatzstatistikDto kdums;
		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto);

		kdums = new KundeUmsatzstatistikDto(gruppierung.size() + 1);
		kdums.setSKunde(kundeDto.getPartnerDto().getCName1nachnamefirmazeile1());
		kdums.setIZahlungsziel(kundeDto.getZahlungszielIId());
		kdums.setILieferart(kundeDto.getLieferartIId());
		kdums.setISpediteur(kundeDto.getSpediteurIId());
		kdums.setIKundennummer(kundeDto.getIKundennummer());
		kdums.setBdKreditlimit(kundeDto.getNKreditlimit());
		kdums.setKundeDto(kundeDto);

		if (kundeDto.getPartnerDto().getLandplzortDto() != null) {
			kdums.setSLkz(kundeDto.getPartnerDto().getLandplzortDto().getLandDto().getCLkz());
			kdums.setSPlz(kundeDto.getPartnerDto().getLandplzortDto().getCPlz());
		}

		if (kundeDto.getPersonaliIdProvisionsempfaenger() != null) {

			PersonalDto personalDto = getPersonalFac()
					.personalFindByPrimaryKey(kundeDto.getPersonaliIdProvisionsempfaenger(), theClientDto);
			kdums.setSProvisionsempfaengerKurzzeichen(personalDto.getCKurzzeichen());
			kdums.setSProvisionsempfaengerName(personalDto.formatFixName1Name2());
		}

		kdums.setSKundengruppierung("");
		// Branche/Partnerklasse hinzufuegen

		if (iOptionKundengruppierung
				.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_KUNDENGRUPPIERUNG_BRANCHE
				&& kundeDto.getPartnerDto().getBrancheIId() != null) {
			kdums.setSKundengruppierung(getPartnerServicesFac()
					.brancheFindByPrimaryKey(kundeDto.getPartnerDto().getBrancheIId(), theClientDto).getBezeichnung());
		} else if (iOptionKundengruppierung
				.intValue() == LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_KUNDENGRUPPIERUNG_PARTNERKLASSE
				&& kundeDto.getPartnerDto().getPartnerklasseIId() != null) {
			kdums.setSKundengruppierung(getPartnerFac()
					.partnerklasseFindByPrimaryKey(kundeDto.getPartnerDto().getPartnerklasseIId(), theClientDto)
					.getBezeichnung());
		}

		// Erstumsatz?
		Session sessionEU = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria critEU = sessionEU.createCriteria(FLRRechnung.class);

		if (bVerwendeStatistikadresse) {
			critEU = critEU.createAlias("flrstatistikadresse", "k").add(Restrictions.eq("k.i_id", kundeIId));
		} else {
			critEU = critEU.createAlias("flrkunde", "k").add(Restrictions.eq("k.i_id", kundeIId));
		}

		critEU.add(Restrictions.lt(RechnungFac.FLR_RECHNUNG_D_BELEGDATUM, tVon));

		String[] statiErstumsatz = new String[1];
		statiErstumsatz[0] = RechnungFac.STATUS_STORNIERT;
		critEU.add(Restrictions.not(Restrictions.in(RechnungFac.FLR_RECHNUNG_STATUS_C_NR, statiErstumsatz)));
		critEU.setFetchSize(1);
		List<?> resultsEU = critEU.list();
		if (resultsEU.size() < 1) {
			kdums.setBErstumsatz(true);
		}
		sessionEU.close();

		// ENDE Erstumsatz

		// Erstumsatz-Datum
		sessionEU = FLRSessionFactory.getFactory().openSession();

		critEU = sessionEU.createCriteria(FLRRechnung.class);

		if (bVerwendeStatistikadresse) {
			critEU = critEU.createAlias("flrstatistikadresse", "kd").add(Restrictions.eq("kd.i_id", kundeIId));
		} else {
			critEU = critEU.createAlias("flrkunde", "kd").add(Restrictions.eq("kd.i_id", kundeIId));

		}

		statiErstumsatz = new String[1];
		statiErstumsatz[0] = RechnungFac.STATUS_STORNIERT;
		critEU.add(Restrictions.not(Restrictions.in(RechnungFac.FLR_RECHNUNG_STATUS_C_NR, statiErstumsatz)));
		critEU.setFetchSize(1);
		critEU.addOrder(Order.asc(RechnungFac.FLR_RECHNUNG_D_BELEGDATUM));
		resultsEU = critEU.list();
		if (resultsEU.size() > 0) {
			FLRRechnung flrr = (FLRRechnung) resultsEU.iterator().next();

			kdums.setDErstumsatz(flrr.getD_belegdatum());
		}
		sessionEU.close();

		// ENDE Erstumsatz-Datum

		// Letztumsatz-Datum PJ19866
		sessionEU = FLRSessionFactory.getFactory().openSession();

		critEU = sessionEU.createCriteria(FLRRechnung.class);

		if (bVerwendeStatistikadresse) {
			critEU = critEU.createAlias("flrstatistikadresse", "kd").add(Restrictions.eq("kd.i_id", kundeIId));
		} else {
			critEU = critEU.createAlias("flrkunde", "kd").add(Restrictions.eq("kd.i_id", kundeIId));

		}

		statiErstumsatz = new String[1];
		statiErstumsatz[0] = RechnungFac.STATUS_STORNIERT;
		critEU.createAlias("flrrechnungart", "art")
				.add(Restrictions.eq("art.rechnungtyp_c_nr", RechnungFac.RECHNUNGTYP_RECHNUNG));
		critEU.add(Restrictions.not(Restrictions.in(RechnungFac.FLR_RECHNUNG_STATUS_C_NR, statiErstumsatz)));
		critEU.setFetchSize(1);
		critEU.addOrder(Order.desc(RechnungFac.FLR_RECHNUNG_D_BELEGDATUM));
		resultsEU = critEU.list();
		if (resultsEU.size() > 0) {
			FLRRechnung flrr = (FLRRechnung) resultsEU.iterator().next();

			kdums.setDLetztumsatz(flrr.getD_belegdatum());
		}
		sessionEU.close();

		// ENDE Letztumsatz-Datum
		return kdums;
	}

	public FLRRechnungReport[] rechnungFindByBelegdatumVonBis(String mandantCNr, Date dVon, Date dBis,
			Collection<String> cRechnungstyp, Collection<String> cStati, Integer kundeIId, boolean bStatistikadresse,
			boolean bOhneAndereMandanten, TheClientDto theClientDto) {
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRRechnungReport.class);
			c.add(Restrictions.eq(RechnungFac.FLR_RECHNUNG_MANDANT_C_NR, mandantCNr));
			c.add(Restrictions.in(RechnungFac.FLR_RECHNUNG_STATUS_C_NR, cStati));
			if (dVon != null) {
				c.add(Restrictions.ge(RechnungFac.FLR_RECHNUNG_D_BELEGDATUM, dVon));
			}
			if (dBis != null) {
				c.add(Restrictions.lt(RechnungFac.FLR_RECHNUNG_D_BELEGDATUM, dBis));
			}

			if (kundeIId != null) {
				if (bStatistikadresse) {

					c.add(Restrictions.eq(RechnungFac.FLR_RECHNUNG_KUNDE_I_ID_STATISTIKADRESSE, kundeIId));
				} else {
					c.add(Restrictions.eq(RechnungFac.FLR_RECHNUNG_KUNDE_I_ID, kundeIId));
				}
			}

			c.createCriteria(RechnungFac.FLR_RECHNUNG_FLRRECHNUNGART)
					.add(Restrictions.in(RechnungFac.FLR_RECHNUNGART_RECHNUNGTYP_C_NR, cRechnungstyp));

			if (bOhneAndereMandanten) {
				ArrayList alKundeIId = new ArrayList();

				MandantDto[] mandantenDtos = getMandantFac().mandantFindAll(theClientDto);

				for (int i = 0; i < mandantenDtos.length; i++) {
					try {
						if (!mandantenDtos[i].getCNr().equals(mandantCNr)) {
							KundeDto kdDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(
									mandantenDtos[i].getPartnerIId(), mandantCNr, theClientDto);
							if (kdDto != null) {
								alKundeIId.add(kdDto.getIId());

							}
						}
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}
				c.add(Restrictions.not(Restrictions.in(RechnungFac.FLR_RECHNUNG_KUNDE_I_ID, alKundeIId)));
			}

			List<?> list = c.list();
			if (list.isEmpty()) {
				return new FLRRechnungReport[0];
			} else {
				int i = 0;
				FLRRechnungReport[] re = new FLRRechnungReport[list.size()];
				for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
					re[i++] = (FLRRechnungReport) iter.next();
				}
				return re;
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public BigDecimal berechneSummeDeckungsbeitrag(String mandantCNr, String sKriterium, GregorianCalendar gcVon,
			GregorianCalendar gcBis, boolean bOhneAndereMandanten, TheClientDto theClientDto) {
		BigDecimal deckungsbeitrag = new BigDecimal(0);
		Collection<String> cRechnungstyp = new LinkedList<String>();
		cRechnungstyp.add(RechnungFac.RECHNUNGTYP_RECHNUNG);
		if (sKriterium.equals(RechnungFac.KRIT_MIT_GUTSCHRIFTEN))
			cRechnungstyp.add(RechnungFac.RECHNUNGTYP_GUTSCHRIFT);
		Collection<String> cStati = new LinkedList<String>();
		cStati.add(RechnungFac.STATUS_BEZAHLT);
		cStati.add(RechnungFac.STATUS_OFFEN);
		cStati.add(RechnungFac.STATUS_TEILBEZAHLT);
		cStati.add(RechnungFac.STATUS_VERBUCHT);

		FLRRechnungReport[] r = rechnungFindByBelegdatumVonBis(mandantCNr, new java.sql.Date(gcVon.getTimeInMillis()),
				new java.sql.Date(gcBis.getTimeInMillis()), cRechnungstyp, cStati, null, false, bOhneAndereMandanten,
				theClientDto);
		for (int i = 0; i < r.length; i++) {

			Session session2 = FLRSessionFactory.getFactory().openSession();

			org.hibernate.Criteria crit2 = session2.createCriteria(FLRRechnungPosition.class).createAlias("flrrechnung",
					"r");

			// SP5885
			crit2.add(Restrictions.eq("r.i_id", r[i].getI_id()));

			crit2.createAlias("r." + RechnungFac.FLR_RECHNUNG_FLRRECHNUNGART, "ra");

			// PJ 14808
			crit2.add(Restrictions.or(
					Restrictions
							.isNotNull(RechnungFac.FLR_RECHNUNGPOSITION_N_NETTOEINZELPREIS_PLUS_AUFSCHLAG_MINUS_RABATT),
					Restrictions.eq(RechnungFac.FLR_RECHNUNGPOSITIONSART_POSITIONSART_C_NR,
							RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN)));

			// projekt 3568
			String[] arten = new String[2];
			arten[0] = RechnungFac.RECHNUNGART_ANZAHLUNG;
			arten[1] = RechnungFac.RECHNUNGART_PROFORMARECHNUNG;

			crit2.add(Restrictions.not(Restrictions.in("ra.c_nr", arten)));

			String[] stati = new String[2];
			stati[0] = RechnungFac.STATUS_ANGELEGT;
			stati[1] = RechnungFac.STATUS_STORNIERT;

			crit2.add(Restrictions.not(Restrictions.in("r." + RechnungFac.FLR_RECHNUNG_STATUS_C_NR, stati)));

			// SP903
			crit2.add(Restrictions.isNull("position_i_id_artikelset"));

			List<?> results2 = crit2.list();
			Iterator<?> resultListIterator2 = results2.iterator();

			int iPos = 0;
			while (resultListIterator2.hasNext()) {
				FLRRechnungPosition rechpos = (FLRRechnungPosition) resultListIterator2.next();

				if (rechpos.getPositionsart_c_nr().equals(LocaleFac.POSITIONSART_IDENT)
						|| rechpos.getPositionsart_c_nr().equals(LocaleFac.POSITIONSART_HANDEINGABE)) {

					boolean bGutschrift = false;

					BigDecimal umsatz = rechpos.getN_nettoeinzelpreis_plus_aufschlag_minus_rabatt();
					// Gutschrift oder Wertgutschrift
					if (rechpos.getFlrrechnung().getFlrrechnungart().getC_nr()
							.equals(RechnungFac.RECHNUNGART_GUTSCHRIFT)
							|| rechpos.getFlrrechnung().getFlrrechnungart().getC_nr()
									.equals(RechnungFac.RECHNUNGART_WERTGUTSCHRIFT)) {
						bGutschrift = true;
						umsatz = new BigDecimal(0).subtract(umsatz);

					}

					// Mit Welchselkurs zu Mandantenwaehrung dividieren
					if (rechpos.getFlrrechnung().getN_kurs() != null
							&& rechpos.getFlrrechnung().getN_kurs().doubleValue() != 0
							&& !rechpos.getPositionsart_c_nr().equals(RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN)) {
						umsatz = umsatz.divide(rechpos.getFlrrechnung().getN_kurs(), 6, BigDecimal.ROUND_HALF_EVEN);

					}

					if (rechpos.getPositionsart_c_nr().equals(LocaleFac.POSITIONSART_IDENT)
							|| (rechpos.getPositionsart_c_nr().equals(LocaleFac.POSITIONSART_HANDEINGABE))) {

						if (rechpos.getPosition_i_id_artikelset() == null) {

							umsatz = umsatz.multiply(rechpos.getN_menge());
							BigDecimal gestwert = new BigDecimal(0);

							BigDecimal deckungsbeitragPos = new BigDecimal(0);

							if (!rechpos.getPositionsart_c_nr().equals(LocaleFac.POSITIONSART_HANDEINGABE)) {

								if (rechpos.getSetartikel_set().size() == 0) {
									gestwert = getGestpreisFuerUmsatzStatistik(rechpos.getI_id(), bGutschrift);
								} else {
									Iterator it = rechpos.getSetartikel_set().iterator();
									while (it.hasNext()) {
										gestwert = gestwert.add(getGestpreisFuerUmsatzStatistik(
												(FLRRechnungPosition) it.next(), bGutschrift));
									}
								}
							}

							// Mit Welchselkurs zu Mandantenwaehrung dividieren
							if (rechpos.getFlrrechnung().getN_kurs() != null
									&& rechpos.getFlrrechnung().getN_kurs().doubleValue() != 0) {
								gestwert = gestwert.divide(rechpos.getFlrrechnung().getN_kurs(), 6,
										BigDecimal.ROUND_HALF_EVEN);
							}

							deckungsbeitragPos = umsatz.subtract(gestwert);

							// GESAMTSUMME
							if (rechpos.getPositionsart_c_nr().equals(LocaleFac.POSITIONSART_HANDEINGABE)) {
								deckungsbeitragPos = umsatz;
							}

							deckungsbeitrag = deckungsbeitrag.add(deckungsbeitragPos);

						}

					}

				} else if (rechpos.getPositionsart_c_nr().equals(LocaleFac.POSITIONSART_LIEFERSCHEIN)) {
					Integer lieferscheinIId = rechpos.getFlrlieferschein().getI_id();

					Session session3 = FLRSessionFactory.getFactory().openSession();

					org.hibernate.Criteria crit3 = session3.createCriteria(FLRLieferscheinposition.class);
					crit3.createAlias("flrlieferschein", "l");
					crit3.add(Restrictions.eq("l.i_id", lieferscheinIId));
					// SP903
					crit3.add(Restrictions.isNull("position_i_id_artikelset"));
					crit3.add(Restrictions
							.isNotNull(LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_N_NETTOGESAMTPREIS));
					crit3.add(Restrictions.not(Restrictions.eq(LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_N_MENGE,
							new BigDecimal(0))));

					List<?> results3 = crit3.list();
					Iterator<?> resultListIterator3 = results3.iterator();
					while (resultListIterator3.hasNext()) {
						FLRLieferscheinposition lieferscheinpos = (FLRLieferscheinposition) resultListIterator3.next();

						// PJ 14525
						BigDecimal umsatzLs = lieferscheinpos.getN_nettogesamtpreisplusversteckteraufschlagminusrabatt()
								.divide(rechpos.getFlrrechnung().getN_kurs(), 6, BigDecimal.ROUND_HALF_EVEN);
						umsatzLs = umsatzLs.multiply(lieferscheinpos.getN_menge());

						if (lieferscheinpos.getPositionsart_c_nr().equals(LocaleFac.POSITIONSART_HANDEINGABE)) {

							deckungsbeitrag = deckungsbeitrag.add(umsatzLs);

						} else if (lieferscheinpos.getPositionsart_c_nr().equals(LocaleFac.POSITIONSART_IDENT)) {
							BigDecimal gestpreis = new BigDecimal(0);

							try {

								/**
								 * @todo Berechnung fuer negative Lieferscheinpositionen einbauen
								 */
								if (lieferscheinpos.getN_menge().doubleValue() < 0) {
									if (umsatzLs != null) {
										gestpreis = umsatzLs;
									}
								} else {

									if (!rechpos.getPositionsart_c_nr().equals(LocaleFac.POSITIONSART_HANDEINGABE)) {

										// PJ 16882
										if (lieferscheinpos.getFlrartikel() != null
												&& Helper.short2boolean(lieferscheinpos.getFlrartikel()
														.getB_lagerbewirtschaftet()) == false
												&& !lieferscheinpos.getFlrartikel().getArtikelart_c_nr()
														.equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
											gestpreis = new BigDecimal(0);
										} else {
											gestpreis = getLagerFac().getGemittelterGestehungspreisEinerAbgangsposition(
													LocaleFac.BELEGART_LIEFERSCHEIN, lieferscheinpos.getI_id());
											gestpreis = gestpreis.multiply(lieferscheinpos.getN_menge());
										}

									}
								}
							} catch (RemoteException ex2) {
								if (ex2.getCause() instanceof EJBExceptionLP) {
									if (((EJBExceptionLP) ex2.getCause())
											.getCode() == EJBExceptionLP.ARTIKEL_KEINE_LAGERBUCHUNG_VORHANDEN) {
										// DANN ALLES OK
									} else {
										throwEJBExceptionLPRespectOld(ex2);
									}
								} else {
									throwEJBExceptionLPRespectOld(ex2);
								}
							}
							BigDecimal deckungsbeitragLS = umsatzLs.subtract(gestpreis);

							deckungsbeitrag = deckungsbeitrag.add(deckungsbeitragLS);

						}

					}
					session3.close();

				}

			}

		}
		return deckungsbeitrag;
	}

	public BigDecimal getGestpreisFuerUmsatzStatistik(Integer rechnungpositionIId, boolean bGutschrift) {

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT p FROM FLRRechnungPosition AS p WHERE p.i_id=" + rechnungpositionIId;

		Query query = session.createQuery(queryString);
		List<?> resultList = query.list();
		if (resultList.size() > 0) {
			return getGestpreisFuerUmsatzStatistik((FLRRechnungPosition) resultList.iterator().next(), bGutschrift);
		} else {
			return BigDecimal.ZERO;
		}

	}

	private BigDecimal getGestpreisFuerUmsatzStatistik(FLRRechnungPosition rechpos, boolean bGutschrift) {

		BigDecimal gestpreis = new BigDecimal(0);
		// 16882 -> Gestpreis fuer Umsatz/DB-Statistik: Wenn NICHT
		// LAGERBEWIRTSCHAFTET, dann ist der Gestpreis=0
		if (rechpos.getFlrartikel() != null
				&& Helper.short2boolean(rechpos.getFlrartikel().getB_lagerbewirtschaftet()) == false
				&& !rechpos.getFlrartikel().getArtikelart_c_nr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
			return gestpreis;
		}

		try {

			if (bGutschrift == false) {
				gestpreis = getLagerFac().getGemittelterGestehungspreisEinerAbgangsposition(LocaleFac.BELEGART_RECHNUNG,
						rechpos.getI_id());
				gestpreis = gestpreis.multiply(rechpos.getN_menge());
			} else {
				// PJ16955 Der Gestehungswert bei Gutschriften wurde bisher
				// nicht mitgerechnet (immer 0)
				// Mit dieser Erweiterung wird der Gutschrifts-Gestwert, der bei
				// der Zubuchung aus der Rechnung ermittelt wird,
				// beruecksichtigt
				gestpreis = getLagerFac().getGemittelterEinstandspreisEinerZugangsposition(
						LocaleFac.BELEGART_GUTSCHRIFT, rechpos.getI_id());
				gestpreis = gestpreis.multiply(rechpos.getN_menge()).multiply(new BigDecimal(-1));
			}

		} catch (RemoteException ex2) {
			if (ex2.getCause() instanceof EJBExceptionLP) {
				if (((EJBExceptionLP) ex2.getCause())
						.getCode() == EJBExceptionLP.ARTIKEL_KEINE_LAGERBUCHUNG_VORHANDEN) {
					// DANN ALLES OK
				} else {
					throwEJBExceptionLPRespectOld(ex2);
				}
			} else {
				throwEJBExceptionLPRespectOld(ex2);
			}
		}
		return gestpreis;
	}

	public JasperPrintLP printMindesthaltbarkeit(String artikelnrVon, String artikelnrBis,
			Boolean bSortiertNachChargennr, Integer mhAlter, TheClientDto theClientDto) {
		Session session = FLRSessionFactory.getFactory().openSession();
		session.enableFilter("filterMandant").setParameter("paramMandant", theClientDto.getMandant());
		session.enableFilter("filterLocale").setParameter("paramLocale", Helper.locale2String(theClientDto.getLocUi()));

		String queryString = "SELECT artikelliste.i_id, artikelliste.c_nr, aspr.c_bez, sum(alager.n_lagerstand),artikelliste.i_garantiezeit "
				+ " FROM FLRArtikelliste AS artikelliste" + " LEFT OUTER JOIN artikelliste.artikellagerset AS alager "
				+ " LEFT OUTER JOIN artikelliste.artikelsprset AS aspr ";
		queryString = queryString + " WHERE artikelliste.mandant_c_nr='" + theClientDto.getMandant()
				+ "' AND artikelliste.artikelart_c_nr not in('" + ArtikelFac.ARTIKELART_HANDARTIKEL
				+ "') AND artikelliste.b_lagerbewirtschaftet=1 AND artikelliste.b_chargennrtragend=1 ";

		if (artikelnrVon != null) {
			queryString = queryString + " AND artikelliste.c_nr >='" + artikelnrVon + "'";
		}
		if (artikelnrBis != null) {
			queryString = queryString + " AND artikelliste.c_nr <='" + artikelnrBis + "'";
		}

		queryString = queryString
				+ " GROUP BY artikelliste.i_id,artikelliste.c_nr, aspr.c_bez, artikelliste.i_garantiezeit ";

		queryString = queryString + " ORDER BY artikelliste.c_nr ";
		Query query = session.createQuery(queryString);
		List<?> resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();
		Calendar cHeute = Calendar.getInstance();

		ArrayList<Object[]> al = new ArrayList<Object[]>();

		while (resultListIterator.hasNext()) {
			Object o[] = (Object[]) resultListIterator.next();

			// Lagerstand holen
			Double lagerstand = null;
			if (o[3] != null) {
				lagerstand = new Double(((BigDecimal) o[3]).doubleValue());
			} else {
				lagerstand = new Double(0);
			}

			// Alle Artikel mit Lagerstand groesser 0 kommen in die
			// Lagerstandsliste
			if (lagerstand.doubleValue() > 0) {

				try {
					SeriennrChargennrAufLagerDto[] dtos = getLagerFac().getAllSerienChargennrAufLager((Integer) o[0],
							null, theClientDto, Boolean.TRUE, false);

					for (int i = 0; i < dtos.length; i++) {
						Object[] oZeile = new Object[6];

						oZeile[REPORT_MINDESTHALTBARKEIT_ARTIKELNUMMER] = o[1];
						oZeile[REPORT_MINDESTHALTBARKEIT_BEZEICHNUNG] = o[2];
						oZeile[REPORT_MINDESTHALTBARKEIT_MENGE] = dtos[i].getNMenge();
						oZeile[REPORT_MINDESTHALTBARKEIT_CHARGENNUMMER] = dtos[i].getCSeriennrChargennr();

						// Haltbarkeit berechnen
						String chargennummer = dtos[i].getCSeriennrChargennr();
						if (chargennummer != null && chargennummer.length() > 7) {
							try {
								int iJahr = Integer.parseInt(chargennummer.substring(0, 4));
								int iMonat = Integer.parseInt(chargennummer.substring(4, 6)) - 1;
								int iTag = Integer.parseInt(chargennummer.substring(6, 8));

								Calendar cHaltbarkeitsdatum = Calendar.getInstance();
								cHaltbarkeitsdatum.set(iJahr, iMonat, iTag, 0, 0, 0);

								if (cHaltbarkeitsdatum.getTimeInMillis() > cHeute.getTimeInMillis()) {

									int diffMonate = cHaltbarkeitsdatum.get(Calendar.MONTH)
											- cHeute.get(Calendar.MONTH);
									int diffJahre = cHaltbarkeitsdatum.get(Calendar.YEAR) - cHeute.get(Calendar.YEAR);
									int diffTage = cHaltbarkeitsdatum.get(Calendar.DAY_OF_MONTH)
											- cHeute.get(Calendar.DAY_OF_MONTH);

									diffMonate = diffMonate + diffJahre * 12;

									if (diffTage < 0) {
										diffMonate = diffMonate - 1;
										diffTage = 31 + diffTage;
									}

									oZeile[REPORT_MINDESTHALTBARKEIT_HALTBARTAGE] = diffTage;
									oZeile[REPORT_MINDESTHALTBARKEIT_HALTBARMONATE] = diffMonate;

								}
								al.add(oZeile);
							} catch (NumberFormatException ex1) {
								//
							}
						}

					}

				} catch (RemoteException ex) {
					throwEJBExceptionLPRespectOld(ex);
				}

			}

		}
		session.close();

		// Umsortieren
		if (bSortiertNachChargennr == true) {
			for (int i = 1; i < al.size(); i++) {
				// innere For-Schleife (laeuft bis schon platzierten Elementen)
				for (int j = 0; j < al.size() - i; j++) {

					Object[] o1 = (Object[]) al.get(j);
					Object[] o2 = (Object[]) al.get(j + 1);
					String chnr1 = (String) o1[REPORT_MINDESTHALTBARKEIT_CHARGENNUMMER];
					String chnr2 = (String) o2[REPORT_MINDESTHALTBARKEIT_CHARGENNUMMER];

					// falls in falscher Reihenfolge, dann vertauschen
					if (chnr1.compareTo(chnr2) > 0) {
						Object[] temp = o1;
						al.set(j, o2);
						al.set(j + 1, temp);
					}

				}
			}

		}

		Object[][] returnArray = new Object[al.size()][6];
		data = (Object[][]) al.toArray(returnArray);

		// Erstellung des Reports
		JasperPrintLP print = null;
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_MHALTER", mhAlter);
		parameter.put("P_VON", artikelnrVon);
		parameter.put("P_BIS", artikelnrBis);
		if (bSortiertNachChargennr == true) {
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.chargennummer", theClientDto.getMandant(), theClientDto.getLocUi()));
		} else {
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.artikelnummer", theClientDto.getMandant(), theClientDto.getLocUi()));
		}
		index = -1;
		sAktuellerReport = LagerReportFac.REPORT_MINDESTHALTBARKEIT;
		initJRDS(parameter, LagerReportFac.REPORT_MODUL, LagerReportFac.REPORT_MINDESTHALTBARKEIT,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		print = getReportPrint();
		return print;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printWarenentnahmestatistik(Timestamp dVon, Timestamp dBis, Integer lagerIId,
			boolean bMitVersteckten, String artikelNrVon, String artikelNrBis, Integer artikelgruppeIId,
			Integer artikelklasseIId, Integer iSortierung, boolean bMitNichtLagerbewirtschaftetenArtikeln,
			boolean bDetailliert, TheClientDto theClientDto) {
		sAktuellerReport = LagerReportFac.REPORT_WARENENTNAHMESTATISTIK;

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_VON", dVon);
		parameter.put("P_BIS", dBis);
		parameter.put("P_ARTIKELNRVON", artikelNrVon);
		parameter.put("P_ARTIKELNRBIS", artikelNrBis);
		parameter.put("P_MITVERSTECKTEN", new Boolean(bMitVersteckten));
		parameter.put("P_MITNICHTLAGERBEWIRTSCHAFTETENARTIKELN", new Boolean(bMitNichtLagerbewirtschaftetenArtikeln));

		parameter.put("P_DETAILLIERT", bDetailliert);

		Session session = FLRSessionFactory.getFactory().openSession();
		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			session.enableFilter("filterMandant").setParameter("paramMandant", getSystemFac().getHauptmandant());
		} else {
			session.enableFilter("filterMandant").setParameter("paramMandant", theClientDto.getMandant());
		}
		String mandantCNr = theClientDto.getMandant();

		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			mandantCNr = getSystemFac().getHauptmandant();
		}

		String queryString = "SELECT l,stuecklisten.stuecklisteart_c_nr FROM FLRLagerbewegung as l LEFT OUTER JOIN l.flrartikel.flrartikelgruppe AS gruppe LEFT OUTER JOIN l.flrartikel.stuecklisten AS stuecklisten LEFT OUTER JOIN l.flrartikel.flrartikelklasse AS klasse "
				+ " WHERE l.flrartikel.mandant_c_nr='" + mandantCNr
				+ "' AND l.b_historie=0 AND l.flrartikel.artikelart_c_nr NOT IN ('" + ArtikelFac.ARTIKELART_HANDARTIKEL
				+ "') ";

		queryString += " AND l.t_belegdatum >='" + Helper.formatTimestampWithSlashes(dVon) + "'";
		parameter.put("P_VON", dVon);

		queryString += " AND l.t_belegdatum <'" + Helper.formatTimestampWithSlashes(
				Helper.cutTimestamp(Helper.addiereTageZuTimestamp(new Timestamp(dBis.getTime()), 1))) + "'";

		if (artikelgruppeIId != null) {
			queryString += " AND l.flrartikel.flrartikelgruppe.i_id=" + artikelgruppeIId.intValue();

			parameter.put("P_ARTIKELGRUPPE",
					getArtikelFac().artgruFindByPrimaryKey(artikelgruppeIId, theClientDto).getCNr());

		}

		if (artikelklasseIId != null) {
			queryString += " AND l.flrartikel.flrartikelklasse.i_id=" + artikelklasseIId.intValue();

			parameter.put("P_ARTIKELKLASSE",
					getArtikelFac().artklaFindByPrimaryKey(artikelklasseIId, theClientDto).getCNr());

		}

		if (bMitVersteckten == false) {
			queryString += " AND l.flrartikel.b_versteckt = 0 ";
		}

		if (artikelNrVon != null) {
			queryString += " AND l.flrartikel.c_nr >='" + artikelNrVon + "'";
		}
		if (artikelNrBis != null) {

			String artikelNrBis_Gefuellt = Helper.fitString2Length(artikelNrBis, 25, '_');
			queryString += " AND l.flrartikel.c_nr <='" + artikelNrBis_Gefuellt + "'";
		}

		if (lagerIId != null) {
			try {
				LagerDto lagerDto = getLagerFac().lagerFindByPrimaryKey(lagerIId);
				parameter.put("P_LAGER", lagerDto.getCNr());
				queryString += " AND l.flrlager.i_id=" + lagerIId + " ";

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
		} else {
			if (bMitNichtLagerbewirtschaftetenArtikeln) {
				queryString += " AND l.flrlager.c_nr NOT IN('" + LagerFac.LAGER_WERTGUTSCHRIFT + "')";
			} else {
				queryString += " AND l.flrlager.c_nr NOT IN('" + LagerFac.LAGER_KEINLAGER + "','"
						+ LagerFac.LAGER_WERTGUTSCHRIFT + "')";
			}

		}

		queryString += " ORDER BY ";

		if (iSortierung == LagerReportFac.REPORT_WARENENTNAHMESTATISTIK_SORTIERUNG_ARTIKELGRUPPE) {
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.artikelgruppe", theClientDto.getMandant(), theClientDto.getLocUi()));
			queryString += " gruppe.c_nr ASC, ";
		} else if (iSortierung == LagerReportFac.REPORT_WARENENTNAHMESTATISTIK_SORTIERUNG_ARTIKELKLASSE) {
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.artikelklasse", theClientDto.getMandant(), theClientDto.getLocUi()));
			queryString += " klasse.c_nr ASC, ";
		} else {
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.artikelnummer", theClientDto.getMandant(), theClientDto.getLocUi()));
		}

		queryString += " l.flrartikel.c_nr ASC, l.t_buchungszeit DESC";

		Query query = session.createQuery(queryString);

		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();
		// --------------
		LinkedHashMap<String, Object[]> lhmArtikel = new LinkedHashMap<String, Object[]>();
		while (resultListIterator.hasNext()) {

			Object[] oZeile = (Object[]) resultListIterator.next();

			FLRLagerbewegung lagerbewegung = (FLRLagerbewegung) oZeile[0];
			String stklArt = (String) oZeile[1];

			if (lagerbewegung.getN_menge().doubleValue() != 0) {

				String key = lagerbewegung.getArtikel_i_id() + " " + lagerbewegung.getLager_i_id();

				if (bDetailliert) {
					key = lagerbewegung.getI_id() + "";
				}

				if (!lhmArtikel.containsKey(key)) {
					Object[] zeile = new Object[REPORT_WARENENTNAHMESTATISTIK_ANZAHL_SPALTEN];

					try {

						if (bDetailliert) {
							BelegInfos bi = getLagerFac().getBelegInfos(lagerbewegung.getC_belegartnr(),
									lagerbewegung.getI_belegartid(), null, theClientDto);
							zeile[REPORT_WARENENTNAHMESTATISTIK_BELEGART] = lagerbewegung.getC_belegartnr();
							zeile[REPORT_WARENENTNAHMESTATISTIK_BELEGNUMMER] = bi.getBelegnummer();

							zeile[REPORT_WARENENTNAHMESTATISTIK_BELEGDATUM] = new Timestamp(
									lagerbewegung.getT_belegdatum().getTime());
							zeile[REPORT_WARENENTNAHMESTATISTIK_BUCHUNGSZEIT] = new Timestamp(
									lagerbewegung.getT_buchungszeit().getTime());

						}

						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(lagerbewegung.getArtikel_i_id(), theClientDto);

						zeile[REPORT_WARENENTNAHMESTATISTIK_ARTIKELNUMMER] = artikelDto.getCNr();
						zeile[REPORT_WARENENTNAHMESTATISTIK_ARTIKELREFERENZNUMMER] = artikelDto.getCReferenznr();
						if (stklArt != null) {
							zeile[REPORT_WARENENTNAHMESTATISTIK_ARTIKELART] = stklArt;
						} else {
							zeile[REPORT_WARENENTNAHMESTATISTIK_ARTIKELART] = artikelDto.getArtikelartCNr();
						}

						if (artikelDto.getArtikelsprDto() != null) {
							zeile[REPORT_WARENENTNAHMESTATISTIK_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
							zeile[REPORT_WARENENTNAHMESTATISTIK_KURZBEZEICHNUNG] = artikelDto.getArtikelsprDto()
									.getCKbez();
							zeile[REPORT_WARENENTNAHMESTATISTIK_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto()
									.getCZbez();
							zeile[REPORT_WARENENTNAHMESTATISTIK_ZUSATZBEZEICHNUNG2] = artikelDto.getArtikelsprDto()
									.getCZbez2();
						}

						if (lagerbewegung.getFlrartikel().getFlrartikelgruppe() != null) {
							zeile[REPORT_WARENENTNAHMESTATISTIK_ARTIKELGRUPPE] = lagerbewegung.getFlrartikel()
									.getFlrartikelgruppe().getC_nr();
							if (lagerbewegung.getFlrartikel().getFlrartikelgruppe()
									.getFlrkonto(theClientDto.getMandant()) != null) {
								zeile[REPORT_WARENENTNAHMESTATISTIK_SACHKONTO] = lagerbewegung.getFlrartikel()
										.getFlrartikelgruppe().getFlrkonto(theClientDto.getMandant()).getC_nr();
							}
						}
						if (lagerbewegung.getFlrartikel().getFlrartikelklasse() != null) {
							zeile[REPORT_WARENENTNAHMESTATISTIK_ARTIKELKLASSE] = lagerbewegung.getFlrartikel()
									.getFlrartikelklasse().getC_nr();
						}

						zeile[REPORT_WARENENTNAHMESTATISTIK_LAGER] = lagerbewegung.getFlrlager().getC_nr();

						zeile[REPORT_WARENENTNAHMESTATISTIK_VERSTECKT] = Helper
								.short2Boolean(lagerbewegung.getFlrartikel().getB_versteckt());

						BigDecimal lsBeginn = getLagerFac().getLagerstandZumZeitpunkt(lagerbewegung.getArtikel_i_id(),
								lagerbewegung.getLager_i_id(), dVon, theClientDto);

						if (lsBeginn != null) {
							zeile[REPORT_WARENENTNAHMESTATISTIK_LAGERSTAND_BEGINN] = lsBeginn;
						} else {
							zeile[REPORT_WARENENTNAHMESTATISTIK_LAGERSTAND_BEGINN] = new BigDecimal(0);
						}

						BigDecimal lsEnde = getLagerFac().getLagerstandZumZeitpunkt(lagerbewegung.getArtikel_i_id(),
								lagerbewegung.getLager_i_id(), dBis,

								theClientDto);

						zeile[REPORT_WARENENTNAHMESTATISTIK_LAGERSTAND_ENDE] = lsEnde;

						zeile[REPORT_WARENENTNAHMESTATISTIK_ANZAHL_BESTELLUNGEN] = getWareneingangFac()
								.getAnzahlWEImZeitraum(lagerbewegung.getArtikel_i_id(), dVon, dBis, theClientDto);

						System.out.println("WE: " + zeile[REPORT_WARENENTNAHMESTATISTIK_ANZAHL_BESTELLUNGEN]);

						BigDecimal preisBeginn = getLagerFac().getGestehungspreisZumZeitpunkt(
								lagerbewegung.getArtikel_i_id(), lagerbewegung.getLager_i_id(), dVon,

								theClientDto);

						if (preisBeginn == null) {
							zeile[REPORT_WARENENTNAHMESTATISTIK_LAGERWERT_BEGINN] = new BigDecimal(0);
						} else {
							zeile[REPORT_WARENENTNAHMESTATISTIK_LAGERWERT_BEGINN] = preisBeginn
									.multiply((BigDecimal) zeile[REPORT_WARENENTNAHMESTATISTIK_LAGERSTAND_BEGINN]);

						}

						BigDecimal bdGestpreisEnde = getLagerFac().getGestehungspreisZumZeitpunkt(
								lagerbewegung.getArtikel_i_id(), lagerbewegung.getLager_i_id(), dBis,

								theClientDto);

						if (bdGestpreisEnde == null) {
							bdGestpreisEnde = new BigDecimal(0);
						}

						zeile[REPORT_WARENENTNAHMESTATISTIK_LAGERWERT_ENDE] = bdGestpreisEnde.multiply(lsEnde);

						zeile[REPORT_WARENENTNAHMESTATISTIK_MENGE_ZUGANG] = new BigDecimal(0);
						zeile[REPORT_WARENENTNAHMESTATISTIK_MENGE_ABGANG] = new BigDecimal(0);

						zeile[REPORT_WARENENTNAHMESTATISTIK_WERT_ABGAENGE] = new BigDecimal(0);
						zeile[REPORT_WARENENTNAHMESTATISTIK_WERT_ZUGAENGE] = new BigDecimal(0);

						BigDecimal nMenge = lagerbewegung.getN_menge();

						if (Helper.short2boolean(lagerbewegung.getB_abgang())) {
							zeile[REPORT_WARENENTNAHMESTATISTIK_MENGE_ABGANG] = nMenge;

							zeile[REPORT_WARENENTNAHMESTATISTIK_WERT_ABGAENGE] = nMenge
									.multiply(lagerbewegung.getN_gestehungspreis());

						} else {
							zeile[REPORT_WARENENTNAHMESTATISTIK_MENGE_ZUGANG] = nMenge;
							zeile[REPORT_WARENENTNAHMESTATISTIK_WERT_ZUGAENGE] = nMenge
									.multiply(lagerbewegung.getN_einstandspreis());
						}

						// EK-Preis hinzufuegen
						// Einkaufspreis des ersten Lieferanten hinzufuegen
						ArtikellieferantDto dto = getArtikelFac().getArtikelEinkaufspreis(
								lagerbewegung.getArtikel_i_id(), null, new BigDecimal(1),
								theClientDto.getSMandantenwaehrung(),
								new java.sql.Date(lagerbewegung.getT_belegdatum().getTime()), theClientDto);
						if (dto != null) {
							zeile[REPORT_WARENENTNAHMESTATISTIK_EKPREIS] = dto.getLief1Preis();
						}

					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

					lhmArtikel.put(key, zeile);

				} else {
					Object[] zeile = lhmArtikel.get(key);

					BigDecimal nMenge = lagerbewegung.getN_menge();

					if (Helper.short2boolean(lagerbewegung.getB_abgang())) {

						zeile[REPORT_WARENENTNAHMESTATISTIK_MENGE_ABGANG] = ((BigDecimal) zeile[REPORT_WARENENTNAHMESTATISTIK_MENGE_ABGANG])
								.add(nMenge);

						zeile[REPORT_WARENENTNAHMESTATISTIK_WERT_ABGAENGE] = ((BigDecimal) zeile[REPORT_WARENENTNAHMESTATISTIK_WERT_ABGAENGE])
								.subtract(nMenge.multiply(lagerbewegung.getN_gestehungspreis()));

					} else {
						zeile[REPORT_WARENENTNAHMESTATISTIK_MENGE_ZUGANG] = ((BigDecimal) zeile[REPORT_WARENENTNAHMESTATISTIK_MENGE_ZUGANG])
								.add(nMenge);
						zeile[REPORT_WARENENTNAHMESTATISTIK_WERT_ZUGAENGE] = ((BigDecimal) zeile[REPORT_WARENENTNAHMESTATISTIK_WERT_ZUGAENGE])
								.add(nMenge.multiply(lagerbewegung.getN_einstandspreis()));
					}

					lhmArtikel.put(key, zeile);

				}

			}

		}
		session.close();

		Iterator it = lhmArtikel.keySet().iterator();

		data = new Object[lhmArtikel.size()][13];

		int iRow = 0;
		while (it.hasNext()) {

			// PJ22307
			Object[] oZeile = lhmArtikel.get(it.next());

			BigDecimal bdWertZugaenge = (BigDecimal) oZeile[REPORT_WARENENTNAHMESTATISTIK_WERT_ZUGAENGE];
			BigDecimal bdMengeZugaenge = (BigDecimal) oZeile[REPORT_WARENENTNAHMESTATISTIK_MENGE_ZUGANG];

			if (bdWertZugaenge != null && bdMengeZugaenge != null && bdMengeZugaenge.doubleValue() != 0) {
				oZeile[REPORT_WARENENTNAHMESTATISTIK_EINSTANDSPREIS_PRO_STUECK] = bdWertZugaenge.divide(bdMengeZugaenge,
						4, BigDecimal.ROUND_HALF_EVEN);
			}

			data[iRow] = oZeile;

			iRow++;
		}

		// PJ18017

		// Sortieren
		if (iSortierung.intValue() == LagerReportFac.REPORT_WARENENTNAHMESTATISTIK_SORTIERUNG_ARTIKELGRUPPE
				|| iSortierung.intValue() == LagerReportFac.REPORT_WARENENTNAHMESTATISTIK_SORTIERUNG_ARTIKELKLASSE) {
			for (int i = data.length - 1; i > 0; --i) {
				for (int j = 0; j < i; ++j) {
					Object[] o = data[j];
					Object[] o1 = data[j + 1];

					String s1 = "";
					String s2 = "";

					if (iSortierung
							.intValue() == LagerReportFac.REPORT_WARENENTNAHMESTATISTIK_SORTIERUNG_ARTIKELGRUPPE) {
						if (o[REPORT_WARENENTNAHMESTATISTIK_ARTIKELGRUPPE] != null) {
							s1 = (String) o[REPORT_WARENENTNAHMESTATISTIK_ARTIKELGRUPPE];
						}
						if (o[REPORT_WARENENTNAHMESTATISTIK_ARTIKELGRUPPE] != null) {
							s2 = (String) o1[REPORT_WARENENTNAHMESTATISTIK_ARTIKELGRUPPE];
						}

						s1 = Helper.fitString2Length(s1, 40, ' ')
								+ (String) o[REPORT_WARENENTNAHMESTATISTIK_ARTIKELNUMMER];
						s2 = Helper.fitString2Length(s2, 40, ' ')
								+ (String) o1[REPORT_WARENENTNAHMESTATISTIK_ARTIKELNUMMER];

					} else {
						if (o[REPORT_WARENENTNAHMESTATISTIK_ARTIKELKLASSE] != null) {
							s1 = (String) o[REPORT_WARENENTNAHMESTATISTIK_ARTIKELKLASSE];
						}
						if (o[REPORT_WARENENTNAHMESTATISTIK_ARTIKELKLASSE] != null) {
							s2 = (String) o1[REPORT_WARENENTNAHMESTATISTIK_ARTIKELKLASSE];
						}

						s1 = Helper.fitString2Length(s1, 40, ' ')
								+ (String) o[REPORT_WARENENTNAHMESTATISTIK_ARTIKELNUMMER];
						s2 = Helper.fitString2Length(s2, 40, ' ')
								+ (String) o1[REPORT_WARENENTNAHMESTATISTIK_ARTIKELNUMMER];
					}

					if (s1.compareTo(s2) > 0) {
						data[j] = o1;
						data[j + 1] = o;
					}
				}
			}
		}

		initJRDS(parameter, LagerReportFac.REPORT_MODUL, LagerReportFac.REPORT_WARENENTNAHMESTATISTIK,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printHitliste(Timestamp dVon, Timestamp dBis, Integer lagerIId, Integer iSortierung,
			String artikelNrVon, String artikelNrBis, boolean bMitHandlagerbewegungen, boolean bMitFertigung,
			boolean bMitVersteckten, boolean bNurVerrechnetePositionen, TheClientDto theClientDto) {

		if (iSortierung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("iSortierung == null"));
		}
		index = -1;
		sAktuellerReport = LagerReportFac.REPORT_HITLISTE;

		int iFaktor;
		ParametermandantDto parameterDtoWBZ = null;
		try {
			parameterDtoWBZ = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT);
		} catch (RemoteException e1) {
			throwEJBExceptionLPRespectOld(e1);
		}

		if (parameterDtoWBZ.getCWert().equals(ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_KW)) {
			iFaktor = 7;

		} else if (parameterDtoWBZ.getCWert().equals(ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_TAGE)) {
			iFaktor = 1;
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_MANDANTPARAMETER_NICHT_ANGELEGT, new Exception(
					ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT + " ist nicht richtig definiert"));
		}

		HashMap hmKonsiLager = new HashMap();

		javax.persistence.Query queryLager = em.createNamedQuery("LagerfindByMandantCNr");
		queryLager.setParameter(1, theClientDto.getMandant());
		Collection<?> cl = queryLager.getResultList();

		Iterator it = cl.iterator();
		while (it.hasNext()) {

			Lager lager = (Lager) it.next();

			if (Helper.short2boolean(lager.getBKonsignationslager())) {
				hmKonsiLager.put(lager.getIId(), Helper.short2boolean(lager.getBKonsignationslager()));
			}
		}

		// SP6794
		String eingeloggterMandant = theClientDto.getMandant();
		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			eingeloggterMandant = getSystemFac().getHauptmandant();
		}

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT l.i_id_buchung,l.n_menge,l.artikel_i_id,gruppe.c_nr,klasse.c_nr, l.n_verkaufspreis, l.n_gestehungspreis , l.flrartikel.b_versteckt, shopgruppe.c_nr ,(SELECT count(los.i_id) FROM FLRLos los WHERE los.mandant_c_nr='"
				+ eingeloggterMandant + "' AND los.flrstueckliste.artikel_i_id=l.artikel_i_id AND los.status_c_nr='"
				+ FertigungFac.STATUS_TEILERLEDIGT + "' ),l FROM FLRLagerbewegung as l "
				+ " LEFT OUTER JOIN l.flrartikel.flrartikelgruppe AS gruppe "
				+ " LEFT OUTER JOIN l.flrartikel.flrshopgruppe AS shopgruppe "
				+ " LEFT OUTER JOIN l.flrartikel.flrartikelklasse AS klasse WHERE l.flrartikel.mandant_c_nr='"
				+ eingeloggterMandant
				+ "' AND l.b_abgang=1 AND l.b_historie=0 AND l.n_menge>0 AND l.flrartikel.artikelart_c_nr NOT IN ('"
				+ ArtikelFac.ARTIKELART_HANDARTIKEL + "') AND l.c_belegartnr NOT IN ('" + LocaleFac.BELEGART_INVENTUR
				+ "') ";

		if (artikelNrVon != null) {
			queryString += " AND l.flrartikel.c_nr>='" + artikelNrVon + "'";
		}
		if (artikelNrBis != null) {
			queryString += " AND l.flrartikel.c_nr<='" + Helper.fitString2Length(artikelNrBis, 25, '_') + "'";
		}

		if (bMitVersteckten == false) {
			queryString += " AND l.flrartikel.b_versteckt = 0 ";
		}

		if (!bMitHandlagerbewegungen) {

			queryString += " AND l.c_belegartnr NOT IN ('" + LocaleFac.BELEGART_HAND + "')";
		}
		if (!bMitFertigung) {

			queryString += " AND l.c_belegartnr NOT IN ('" + LocaleFac.BELEGART_LOS + "','"
					+ LocaleFac.BELEGART_LOSABLIEFERUNG + "')";
		}

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_NUR_VERRECHNETE_POSITIONEN", bNurVerrechnetePositionen);

		parameter.put("P_BERUECKSICHTIGEFERTIGUNG", new Boolean(bMitFertigung));
		parameter.put("P_BERUECKSICHTIGEHANDBUCHUNG", new Boolean(bMitHandlagerbewegungen));
		parameter.put("P_MITVERSTECKTEN", new Boolean(bMitVersteckten));
		if (dVon != null) {
			queryString += " AND l.t_belegdatum >='" + Helper.formatDateWithSlashes(new java.sql.Date(dVon.getTime()))
					+ "'";
			parameter.put("P_VON", dVon);
		}
		if (dBis != null) {

			parameter.put("P_BIS", dBis);

			queryString += " AND l.t_belegdatum <'"
					+ Helper.formatDateWithSlashes(Helper.addiereTageZuDatum(new java.sql.Date(dBis.getTime()), 1))
					+ "'";

		}

		queryString += " ORDER BY ";

		if (iSortierung.intValue() == LagerReportFac.REPORT_HITLISTE_SORTIERUNG_ARTIKELGRUPPE) {
			queryString += " gruppe.c_nr ";
		} else if (iSortierung.intValue() == LagerReportFac.REPORT_HITLISTE_SORTIERUNG_ARTIKELKLASSE) {
			queryString += " klasse.c_nr ";
		} else if (iSortierung.intValue() == LagerReportFac.REPORT_HITLISTE_SORTIERUNG_SHOPGRUPPE) {
			queryString += " shopgruppe.c_nr ";
		} else {
			queryString += " l.flrartikel.c_nr ";
		}
		queryString += ",l.i_id_buchung ASC, l.t_buchungszeit DESC";

		Query query = session.createQuery(queryString);

		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		int SPALTE_INTERN_MENGE = 0;
		int SPALTE_INTERN_VKWERT = 1;
		int SPALTE_INTERN_ARTGRU = 2;
		int SPALTE_INTERN_ARTIKELKLASSE = 3;
		int SPALTE_INTERN_GESTWERT = 4;
		int SPALTE_INTERN_DBWERT = 5;
		int SPALTE_INTERN_VERSTECKT = 6;
		int SPALTE_INTERN_SHOPGRUPPE = 7;

		int SPALTE_INTERN_LS_NICHT_VERRECHENBAR_MENGE = 8;
		int SPALTE_INTERN_LS_NICHT_VERRECHENBAR_GESTWERT = 9;
		int SPALTE_INTERN_LS_NICHT_VERRECHENBAR_DBWERT = 10;

		int SPALTE_INTERN_LS_VERRECHENBAR_OHNE_ZIELLAGER_MENGE = 11;
		int SPALTE_INTERN_LS_VERRECHENBAR_OHNE_ZIELLAGER_GESTWERT = 12;
		int SPALTE_INTERN_LS_VERRECHENBAR_OHNE_ZIELLAGER_DBWERT = 13;

		int SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_UND_KONSI_MENGE = 14;
		int SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_UND_KONSI_GESTWERT = 15;
		int SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_UND_KONSI_DBWERT = 16;

		int SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_OHNE_KONSI_MENGE = 17;
		int SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_OHNE_KONSI_GESTWERT = 18;
		int SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_OHNE_KONSI_DBWERT = 19;

		int SPALTE_INTERN_ANZAHL_TEILERLEDIGTER_LOSE = 20;

		// --------------
		LinkedHashMap<Integer, Object[]> lhmArtikel = new LinkedHashMap<Integer, Object[]>();
		while (resultListIterator.hasNext()) {
			Object[] lagerbewegung = (Object[]) resultListIterator.next();

			BigDecimal menge = (BigDecimal) lagerbewegung[1];
			Integer artikelIId = (Integer) lagerbewegung[2];
			BigDecimal vkpreis = (BigDecimal) lagerbewegung[5];

			Long lanzahlTeilerleigteLose = (Long) lagerbewegung[9];

			FLRLagerbewegung l = (FLRLagerbewegung) lagerbewegung[10];

			if (vkpreis == null) {
				vkpreis = new BigDecimal(0);
			}

			BigDecimal gestpreis = (BigDecimal) lagerbewegung[6];

			BigDecimal dbwert = vkpreis.subtract(gestpreis);

			Boolean bVersteckt = Helper.short2Boolean((Short) lagerbewegung[7]);

			if (menge.doubleValue() != 0) {

				Boolean bLieferscheinVerrechenbar = null;
				boolean bMitZiellager = false;
				boolean bZiellagerIstKonsiLager = false;

				if (l.getC_belegartnr().equals(LocaleFac.BELEGART_LIEFERSCHEIN)) {

					try {
						LieferscheinDto lsDto = getLieferscheinFac().lieferscheinFindByPrimaryKey(l.getI_belegartid());

						// lt. WH Lieferantenlieferscheine auslassen
						if (lsDto.getLieferscheinartCNr() != null
								&& lsDto.getLieferscheinartCNr().equals(LieferscheinFac.LSART_LIEFERANT)) {
							continue;
						}

						// PJ20332
						if (bNurVerrechnetePositionen == true
								&& !lsDto.getStatusCNr().equals(LieferscheinFac.LSSTATUS_VERRECHNET)) {
							continue;
						}

						if (lsDto.getZiellagerIId() != null) {
							bMitZiellager = true;

							if (hmKonsiLager.containsKey(lsDto.getZiellagerIId())) {
								bZiellagerIstKonsiLager = true;
							}

						}

						if (Helper.short2boolean(lsDto.getBVerrechenbar())) {
							bLieferscheinVerrechenbar = Boolean.TRUE;

						} else {
							bLieferscheinVerrechenbar = Boolean.FALSE;
						}
					} catch (EJBExceptionLP e) {
						// Lieferschein nicht vorhanden
					}

				}

				if (lhmArtikel.get(artikelIId) == null) {

					Object[] help = new Object[REPORT_HILISTE_ANZAHL_SPALTEN];
					// VK-Menge
					help[SPALTE_INTERN_MENGE] = menge;
					// VK-Wert
					help[SPALTE_INTERN_VKWERT] = vkpreis.multiply(menge);
					help[SPALTE_INTERN_ARTGRU] = lagerbewegung[3];
					help[SPALTE_INTERN_ARTIKELKLASSE] = lagerbewegung[4];
					// Gest-Wert
					help[SPALTE_INTERN_GESTWERT] = gestpreis.multiply(menge);
					// DB-Wert
					help[SPALTE_INTERN_DBWERT] = dbwert.multiply(menge);
					help[SPALTE_INTERN_VERSTECKT] = bVersteckt;
					help[SPALTE_INTERN_SHOPGRUPPE] = lagerbewegung[8];

					help[SPALTE_INTERN_ANZAHL_TEILERLEDIGTER_LOSE] = lanzahlTeilerleigteLose;

					help[SPALTE_INTERN_LS_NICHT_VERRECHENBAR_MENGE] = new BigDecimal(0);
					help[SPALTE_INTERN_LS_NICHT_VERRECHENBAR_GESTWERT] = new BigDecimal(0);
					help[SPALTE_INTERN_LS_NICHT_VERRECHENBAR_DBWERT] = new BigDecimal(0);

					help[SPALTE_INTERN_LS_VERRECHENBAR_OHNE_ZIELLAGER_MENGE] = new BigDecimal(0);
					help[SPALTE_INTERN_LS_VERRECHENBAR_OHNE_ZIELLAGER_GESTWERT] = new BigDecimal(0);
					help[SPALTE_INTERN_LS_VERRECHENBAR_OHNE_ZIELLAGER_DBWERT] = new BigDecimal(0);

					help[SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_UND_KONSI_MENGE] = new BigDecimal(0);
					help[SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_UND_KONSI_GESTWERT] = new BigDecimal(0);
					help[SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_UND_KONSI_DBWERT] = new BigDecimal(0);

					help[SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_OHNE_KONSI_MENGE] = new BigDecimal(0);
					help[SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_OHNE_KONSI_GESTWERT] = new BigDecimal(0);
					help[SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_OHNE_KONSI_DBWERT] = new BigDecimal(0);

					if (bLieferscheinVerrechenbar != null) {

						if (bLieferscheinVerrechenbar == false) {
							help[SPALTE_INTERN_LS_NICHT_VERRECHENBAR_MENGE] = menge;
							help[SPALTE_INTERN_LS_NICHT_VERRECHENBAR_GESTWERT] = gestpreis.multiply(menge);
							help[SPALTE_INTERN_LS_NICHT_VERRECHENBAR_DBWERT] = dbwert.multiply(menge);
						} else {
							if (bMitZiellager == true) {

								if (bZiellagerIstKonsiLager) {

									help[SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_UND_KONSI_MENGE] = menge;
									help[SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_UND_KONSI_GESTWERT] = gestpreis
											.multiply(menge);
									help[SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_UND_KONSI_DBWERT] = dbwert
											.multiply(menge);
								} else {

									help[SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_OHNE_KONSI_MENGE] = menge;
									help[SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_OHNE_KONSI_GESTWERT] = gestpreis
											.multiply(menge);
									help[SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_OHNE_KONSI_DBWERT] = dbwert
											.multiply(menge);
								}

							} else {

								help[SPALTE_INTERN_LS_VERRECHENBAR_OHNE_ZIELLAGER_MENGE] = menge;
								help[SPALTE_INTERN_LS_VERRECHENBAR_OHNE_ZIELLAGER_GESTWERT] = gestpreis.multiply(menge);
								help[SPALTE_INTERN_LS_VERRECHENBAR_OHNE_ZIELLAGER_DBWERT] = dbwert.multiply(menge);
							}
						}
					}

					lhmArtikel.put(artikelIId, help);
				} else {

					// Menge erhoehen
					Object[] help = (Object[]) lhmArtikel.get(artikelIId);

					BigDecimal gesamtmenge = (BigDecimal) help[SPALTE_INTERN_MENGE];
					BigDecimal gesamtvkwert = (BigDecimal) help[SPALTE_INTERN_VKWERT];
					BigDecimal gesamtgestwert = (BigDecimal) help[SPALTE_INTERN_GESTWERT];
					BigDecimal gesamtdbwert = (BigDecimal) help[SPALTE_INTERN_DBWERT];

					help[SPALTE_INTERN_MENGE] = gesamtmenge.add(menge);

					help[1] = gesamtvkwert.add(vkpreis.multiply(menge));
					help[SPALTE_INTERN_GESTWERT] = gesamtgestwert.add(gestpreis.multiply(menge));

					help[SPALTE_INTERN_DBWERT] = gesamtdbwert.add(dbwert.multiply(menge));
					help[SPALTE_INTERN_SHOPGRUPPE] = lagerbewegung[8];
					help[SPALTE_INTERN_ANZAHL_TEILERLEDIGTER_LOSE] = lanzahlTeilerleigteLose;

					if (bLieferscheinVerrechenbar != null) {

						if (bLieferscheinVerrechenbar == false) {
							help[SPALTE_INTERN_LS_NICHT_VERRECHENBAR_MENGE] = ((BigDecimal) help[SPALTE_INTERN_LS_NICHT_VERRECHENBAR_MENGE])
									.add(menge);
							help[SPALTE_INTERN_LS_NICHT_VERRECHENBAR_GESTWERT] = ((BigDecimal) help[SPALTE_INTERN_LS_NICHT_VERRECHENBAR_GESTWERT])
									.add(gestpreis.multiply(menge));
							help[SPALTE_INTERN_LS_NICHT_VERRECHENBAR_DBWERT] = ((BigDecimal) help[SPALTE_INTERN_LS_NICHT_VERRECHENBAR_DBWERT])
									.add(dbwert.multiply(menge));
						} else {
							if (bMitZiellager == true) {

								if (bZiellagerIstKonsiLager) {

									help[SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_UND_KONSI_MENGE] = ((BigDecimal) help[SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_UND_KONSI_MENGE])
											.add(menge);
									help[SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_UND_KONSI_GESTWERT] = ((BigDecimal) help[SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_UND_KONSI_GESTWERT])
											.add(gestpreis.multiply(menge));
									help[SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_UND_KONSI_DBWERT] = ((BigDecimal) help[SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_UND_KONSI_DBWERT])
											.add(dbwert.multiply(menge));
								} else {

									help[SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_OHNE_KONSI_MENGE] = ((BigDecimal) help[SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_OHNE_KONSI_MENGE])
											.add(menge);
									help[SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_OHNE_KONSI_GESTWERT] = ((BigDecimal) help[SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_OHNE_KONSI_GESTWERT])
											.add(gestpreis.multiply(menge));
									help[SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_OHNE_KONSI_DBWERT] = ((BigDecimal) help[SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_OHNE_KONSI_DBWERT])
											.add(dbwert.multiply(menge));
								}

							} else {

								help[SPALTE_INTERN_LS_VERRECHENBAR_OHNE_ZIELLAGER_MENGE] = ((BigDecimal) help[SPALTE_INTERN_LS_VERRECHENBAR_OHNE_ZIELLAGER_MENGE])
										.add(menge);
								help[SPALTE_INTERN_LS_VERRECHENBAR_OHNE_ZIELLAGER_GESTWERT] = ((BigDecimal) help[SPALTE_INTERN_LS_VERRECHENBAR_OHNE_ZIELLAGER_GESTWERT])
										.add(gestpreis.multiply(menge));
								help[SPALTE_INTERN_LS_VERRECHENBAR_OHNE_ZIELLAGER_DBWERT] = ((BigDecimal) help[SPALTE_INTERN_LS_VERRECHENBAR_OHNE_ZIELLAGER_DBWERT])
										.add(dbwert.multiply(menge));
							}
						}
					}

					lhmArtikel.put(artikelIId, help);

				}
			}

		}
		session.close();

		if (iSortierung.intValue() == LagerReportFac.REPORT_HITLISTE_SORTIERUNG_ARTIKELKLASSE) {
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.artikelklasse", theClientDto.getMandant(), theClientDto.getLocUi()));
		} else if (iSortierung.intValue() == LagerReportFac.REPORT_HITLISTE_SORTIERUNG_ARTIKELGRUPPE) {
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.artikelgruppe", theClientDto.getMandant(), theClientDto.getLocUi()));

		} else if (iSortierung.intValue() == LagerReportFac.REPORT_HITLISTE_SORTIERUNG_ARTIKELNR) {
			parameter.put("P_SORTIERUNG", getTextRespectUISpr("artikel.artikelnummerlang", theClientDto.getMandant(),
					theClientDto.getLocUi()));

		} else if (iSortierung.intValue() == LagerReportFac.REPORT_HITLISTE_SORTIERUNG_VKWERT) {
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.wert", theClientDto.getMandant(), theClientDto.getLocUi()));

		} else if (iSortierung.intValue() == LagerReportFac.REPORT_HITLISTE_SORTIERUNG_DBWERT) {
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.dbwert", theClientDto.getMandant(), theClientDto.getLocUi()));

		} else if (iSortierung.intValue() == LagerReportFac.REPORT_HITLISTE_SORTIERUNG_SHOPGRUPPE) {
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.shopgruppe", theClientDto.getMandant(), theClientDto.getLocUi()));

		}

		boolean bLagerMinJeLager = lagerMinJeLager(theClientDto);

		data = new Object[lhmArtikel.size()][REPORT_HILISTE_ANZAHL_SPALTEN];

		Iterator<Integer> ergebnis = lhmArtikel.keySet().iterator();
		int row = 0;
		while (ergebnis.hasNext()) {
			Object key = ergebnis.next();

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall((Integer) key, theClientDto);

			data[row][REPORT_HILISTE_ARTIKELNUMMER] = artikelDto.getCNr();
			data[row][REPORT_HILISTE_BEZEICHNUNG] = artikelDto.formatBezeichnung();
			data[row][REPORT_HILISTE_ARTIKELREFERENZNUMMER] = artikelDto.getCReferenznr();

			if (bLagerMinJeLager == false) {
				data[row][REPORT_HILISTE_LAGERMINDESTSTAND] = artikelDto.getFLagermindest();
				data[row][REPORT_HILISTE_LAGERSOLLSTAND] = artikelDto.getFLagersoll();
			}
			data[row][REPORT_HILISTE_JAHRESMENGE] = artikelDto.getFJahresmenge();

			String sQueryLagerstand = "SELECT sum(artikellager.n_lagerstand),sum(artikellager.f_lagermindest),sum(artikellager.f_lagersoll) FROM FLRArtikellager AS artikellager WHERE artikellager.compId.artikel_i_id="
					+ artikelDto.getIId() + " AND artikellager.flrlager.mandant_c_nr='" + theClientDto.getMandant()
					+ "'  AND artikellager.flrlager.b_konsignationslager=0 AND artikellager.flrlager.lagerart_c_nr NOT IN('"
					+ LagerFac.LAGERART_WERTGUTSCHRIFT + "')";
			Session session2 = FLRSessionFactory.getFactory().openSession();
			Query queryLagerstand = session2.createQuery(sQueryLagerstand);

			List<?> resultsLagerstand = queryLagerstand.list();
			Iterator<?> resultListIteratorLagerstand = resultsLagerstand.iterator();
			if (resultListIteratorLagerstand.hasNext()) {
				Object[] o = (Object[]) resultListIteratorLagerstand.next();

				data[row][REPORT_HILISTE_LAGERSTAND] = (BigDecimal) o[0];

				if (bLagerMinJeLager == true) {
					data[row][REPORT_HILISTE_LAGERMINDESTSTAND] = o[1];
					data[row][REPORT_HILISTE_LAGERSOLLSTAND] = o[2];
				}
			}

			session2.close();

			data[row][REPORT_HILISTE_DURCHSCHNITTLICHE_WBZ] = getBestellungFac()
					.getWiederbeschaffungsmoralEinesArtikels(artikelDto.getIId(), theClientDto);

			try {
				ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreisDesBevorzugtenLieferanten(
						artikelDto.getIId(), BigDecimal.ONE, theClientDto.getSMandantenwaehrung(), theClientDto);
				if (alDto != null && alDto.getIWiederbeschaffungszeit() != null) {
					data[row][REPORT_HILISTE_WBZ_LIEF1] = alDto.getIWiederbeschaffungszeit() * iFaktor;

				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			Object[] help = (Object[]) lhmArtikel.get(key);
			BigDecimal menge = (BigDecimal) help[SPALTE_INTERN_MENGE];
			BigDecimal vkwert = (BigDecimal) help[SPALTE_INTERN_VKWERT];
			BigDecimal gestwert = (BigDecimal) help[SPALTE_INTERN_GESTWERT];
			data[row][REPORT_HILISTE_VERKAUFTEMENGE] = menge;
			data[row][REPORT_HILISTE_DURCHSCHNITTLICHERVKPREIS] = vkwert.divide(menge, BigDecimal.ROUND_HALF_EVEN);
			data[row][REPORT_HILISTE_ARTIKELGRUPPE] = help[SPALTE_INTERN_ARTGRU];
			data[row][REPORT_HILISTE_ARTIKELKLASSE] = help[SPALTE_INTERN_ARTIKELKLASSE];
			data[row][REPORT_HILISTE_DURCHSCHNITTLICHERGESTPREIS] = gestwert.divide(menge, BigDecimal.ROUND_HALF_EVEN);
			data[row][REPORT_HILISTE_DBWERT] = help[SPALTE_INTERN_DBWERT];
			data[row][REPORT_HILISTE_VERSTECKT] = help[SPALTE_INTERN_VERSTECKT];
			data[row][REPORT_HILISTE_SHOPGRUPPE] = help[SPALTE_INTERN_SHOPGRUPPE];
			data[row][REPORT_HILISTE_ANZAHL_TEILERLEDIGTER_LOSE] = help[SPALTE_INTERN_ANZAHL_TEILERLEDIGTER_LOSE];

			data[row][REPORT_HILISTE_LS_NICHT_VERRECHENBAR_DBWERT] = help[SPALTE_INTERN_LS_NICHT_VERRECHENBAR_DBWERT];
			data[row][REPORT_HILISTE_LS_NICHT_VERRECHENBAR_GESTWERT] = help[SPALTE_INTERN_LS_NICHT_VERRECHENBAR_GESTWERT];
			data[row][REPORT_HILISTE_LS_NICHT_VERRECHENBAR_MENGE] = help[SPALTE_INTERN_LS_NICHT_VERRECHENBAR_MENGE];

			data[row][REPORT_HILISTE_LS_VERRECHENBAR_MIT_ZIELLAGER_OHNE_KONSI_DBWERT] = help[SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_OHNE_KONSI_DBWERT];
			data[row][REPORT_HILISTE_LS_VERRECHENBAR_MIT_ZIELLAGER_OHNE_KONSI_GESTWERT] = help[SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_OHNE_KONSI_GESTWERT];
			data[row][REPORT_HILISTE_LS_VERRECHENBAR_MIT_ZIELLAGER_OHNE_KONSI_MENGE] = help[SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_OHNE_KONSI_MENGE];

			data[row][REPORT_HILISTE_LS_VERRECHENBAR_MIT_ZIELLAGER_UND_KONSI_DBWERT] = help[SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_UND_KONSI_DBWERT];
			data[row][REPORT_HILISTE_LS_VERRECHENBAR_MIT_ZIELLAGER_UND_KONSI_GESTWERT] = help[SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_UND_KONSI_GESTWERT];
			data[row][REPORT_HILISTE_LS_VERRECHENBAR_MIT_ZIELLAGER_UND_KONSI_MENGE] = help[SPALTE_INTERN_LS_VERRECHENBAR_MIT_ZIELLAGER_UND_KONSI_MENGE];

			data[row][REPORT_HILISTE_LS_VERRECHENBAR_OHNE_ZIELLAGER_DBWERT] = help[SPALTE_INTERN_LS_VERRECHENBAR_OHNE_ZIELLAGER_DBWERT];
			data[row][REPORT_HILISTE_LS_VERRECHENBAR_OHNE_ZIELLAGER_GESTWERT] = help[SPALTE_INTERN_LS_VERRECHENBAR_OHNE_ZIELLAGER_GESTWERT];
			data[row][REPORT_HILISTE_LS_VERRECHENBAR_OHNE_ZIELLAGER_MENGE] = help[SPALTE_INTERN_LS_VERRECHENBAR_OHNE_ZIELLAGER_MENGE];

			row++;
		}

		if (iSortierung.intValue() == LagerReportFac.REPORT_HITLISTE_SORTIERUNG_VKWERT) {
			for (int i = data.length - 1; i > 0; --i) {
				for (int j = 0; j < i; ++j) {
					Object[] o = data[j];
					Object[] o1 = data[j + 1];

					BigDecimal wert = ((BigDecimal) o[REPORT_HILISTE_DURCHSCHNITTLICHERVKPREIS])
							.multiply((BigDecimal) o[REPORT_HILISTE_VERKAUFTEMENGE]);
					BigDecimal wert1 = ((BigDecimal) o1[REPORT_HILISTE_DURCHSCHNITTLICHERVKPREIS])
							.multiply((BigDecimal) o1[REPORT_HILISTE_VERKAUFTEMENGE]);
					if (wert.compareTo(wert1) < 0) {
						data[j] = o1;
						data[j + 1] = o;
					}
				}
			}
		}

		if (iSortierung.intValue() == LagerReportFac.REPORT_HITLISTE_SORTIERUNG_DBWERT) {
			for (int i = data.length - 1; i > 0; --i) {
				for (int j = 0; j < i; ++j) {
					Object[] o = data[j];
					Object[] o1 = data[j + 1];

					BigDecimal wert = ((BigDecimal) o[REPORT_HILISTE_DBWERT]);
					BigDecimal wert1 = ((BigDecimal) o1[REPORT_HILISTE_DBWERT]);
					if (wert.compareTo(wert1) < 0) {
						data[j] = o1;
						data[j + 1] = o;
					}
				}
			}
		}

		initJRDS(parameter, LagerReportFac.REPORT_MODUL, LagerReportFac.REPORT_HITLISTE, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	private void sucheUndFuegeBetragHinzu(FLRLagerbewegung flrLagerbewegung, HashMap hm,
			Integer artikelgruppeIIdZuFinden) {
		Iterator it = hm.keySet().iterator();

		while (it.hasNext()) {

			Integer artikelgruppeId = (Integer) it.next();
			ReportArtikelgruppeDto r = (ReportArtikelgruppeDto) hm.get(artikelgruppeId);

			if (artikelgruppeId.equals(artikelgruppeIIdZuFinden)) {

				if (Helper.short2boolean(flrLagerbewegung.getB_abgang())) {
					r.add2Gestwert(flrLagerbewegung.getN_menge().multiply(flrLagerbewegung.getN_gestehungspreis()));
					r.add2Umsatz(flrLagerbewegung.getN_menge().multiply(flrLagerbewegung.getN_verkaufspreis()));
					r.add2Menge(flrLagerbewegung.getN_menge());
				} else if (flrLagerbewegung.getC_belegartnr().equals(LocaleFac.BELEGART_GUTSCHRIFT)) {

					BigDecimal vkPreis = BigDecimal.ZERO;

					try {
						RechnungPositionDto rechposDto = getRechnungFac()
								.rechnungPositionFindByPrimaryKeyOhneExc(flrLagerbewegung.getI_belegartpositionid());

						if (rechposDto != null) {
							RechnungDto rechnungDto = getRechnungFac()
									.rechnungFindByPrimaryKey(rechposDto.getRechnungIId());

							vkPreis = rechposDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()
									.divide(rechnungDto.getNKurs(), 5, BigDecimal.ROUND_HALF_EVEN);

							System.out.println(rechnungDto.getCNr() + " " + vkPreis);

						} else {

							vkPreis = flrLagerbewegung.getN_einstandspreis();
						}
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

					r.add2Gestwert(
							flrLagerbewegung.getN_menge().negate().multiply(flrLagerbewegung.getN_gestehungspreis()));
					r.add2Umsatz(flrLagerbewegung.getN_menge().negate().multiply(vkPreis));
					r.add2Menge(flrLagerbewegung.getN_menge().negate());
				}

				return;
			} else {
				if (r.getSubGruppen() != null) {
					sucheUndFuegeBetragHinzu(flrLagerbewegung, r.getSubGruppen(), artikelgruppeIIdZuFinden);
				}
			}

		}
	}

	private void sucheUndFuegeBetragMaterialbedarfsvorschauHinzu(FLRLagerbewegung flrLagerbewegung,
			FLRAngebotposition flrAngebotposition, FLRAuftragpositionReport flrAuftragpositionReportRahmen,
			FLRAuftragpositionReport flrAuftragpositionReportAngelegte, HashMap hm, Integer artikelgruppeIIdZuFinden) {
		Iterator it = hm.keySet().iterator();

		while (it.hasNext()) {

			Integer artikelgruppeId = (Integer) it.next();
			ReportMaterialbedarfsvorschauDto r = (ReportMaterialbedarfsvorschauDto) hm.get(artikelgruppeId);

			if ((artikelgruppeIIdZuFinden == null && artikelgruppeId == null)
					|| (artikelgruppeId != null && artikelgruppeId.equals(artikelgruppeIIdZuFinden))) {

				if (flrAngebotposition != null) {
					r.add2MengeOffeneAngebote(flrAngebotposition.getFlrartikel().getI_id(),
							flrAngebotposition.getN_menge().multiply(
									new BigDecimal(flrAngebotposition.getFlrangebot().getF_auftragswahrscheinlichkeit())
											.movePointLeft(2)));
				}

				if (flrAuftragpositionReportRahmen != null) {
					r.add2MengeOffeneRahmen(flrAuftragpositionReportRahmen.getFlrartikel().getI_id(),
							flrAuftragpositionReportRahmen.getN_menge());
				}

				if (flrAuftragpositionReportAngelegte != null) {
					r.add2MengeAngelegteAuftraege(flrAuftragpositionReportAngelegte.getArtikel_i_id(),
							flrAuftragpositionReportAngelegte.getN_menge());
				}

				if (flrLagerbewegung != null) {

					if (Helper.short2boolean(flrLagerbewegung.getB_abgang())) {

						r.add2MengeVergangenheit(flrLagerbewegung.getArtikel_i_id(), flrLagerbewegung.getN_menge());
					} else if (flrLagerbewegung.getC_belegartnr().equals(LocaleFac.BELEGART_GUTSCHRIFT)) {

						BigDecimal vkPreis = BigDecimal.ZERO;

						try {
							RechnungPositionDto rechposDto = getRechnungFac().rechnungPositionFindByPrimaryKeyOhneExc(
									flrLagerbewegung.getI_belegartpositionid());

							if (rechposDto != null) {
								RechnungDto rechnungDto = getRechnungFac()
										.rechnungFindByPrimaryKey(rechposDto.getRechnungIId());

								vkPreis = rechposDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()
										.divide(rechnungDto.getNKurs(), 5, BigDecimal.ROUND_HALF_EVEN);

								System.out.println(rechnungDto.getCNr() + " " + vkPreis);

							} else {

								vkPreis = flrLagerbewegung.getN_einstandspreis();
							}
						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}

						r.add2MengeVergangenheit(flrLagerbewegung.getArtikel_i_id(),
								flrLagerbewegung.getN_menge().negate());
					}
				}
				return;
			} else {
				if (artikelgruppeIIdZuFinden != null && r.getSubGruppen() != null) {
					sucheUndFuegeBetragMaterialbedarfsvorschauHinzu(flrLagerbewegung, flrAngebotposition,
							flrAuftragpositionReportRahmen, flrAuftragpositionReportAngelegte, r.getSubGruppen(),
							artikelgruppeIIdZuFinden);
				}
			}

		}
	}

	private void add2ArtikelgruppenDaten(FLRLagerbewegung flrLagerbewegung, FLRArtikelgruppe flrartikelgruppe,
			HashMap hmBaum) {
		sucheUndFuegeBetragHinzu(flrLagerbewegung, hmBaum, flrartikelgruppe.getI_id());

		if (flrartikelgruppe.getFlrartikelgruppe() != null) {
			add2ArtikelgruppenDaten(flrLagerbewegung, flrartikelgruppe.getFlrartikelgruppe(), hmBaum);

		}

	}

	private void add2MaterialbedarfsvorschauDaten(FLRLagerbewegung flrLagerbewegung, FLRArtikelgruppe flrartikelgruppe,
			FLRAngebotposition flrAngebotposition, FLRAuftragpositionReport flrAuftragpositionReportRahmen,
			FLRAuftragpositionReport flrAuftragpositionReportAngelegte, HashMap hmBaum) {

		if (flrartikelgruppe == null) {
			// Unbekannt
			sucheUndFuegeBetragMaterialbedarfsvorschauHinzu(flrLagerbewegung, flrAngebotposition,
					flrAuftragpositionReportRahmen, flrAuftragpositionReportAngelegte, hmBaum, null);

		} else {
			// Unbekannt
			sucheUndFuegeBetragMaterialbedarfsvorschauHinzu(flrLagerbewegung, flrAngebotposition,
					flrAuftragpositionReportRahmen, flrAuftragpositionReportAngelegte, hmBaum,
					flrartikelgruppe.getI_id());
		}

		if (flrartikelgruppe != null && flrartikelgruppe.getFlrartikelgruppe() != null) {
			add2MaterialbedarfsvorschauDaten(flrLagerbewegung, flrartikelgruppe.getFlrartikelgruppe(),
					flrAngebotposition, flrAuftragpositionReportRahmen, flrAuftragpositionReportAngelegte, hmBaum);

		}

	}

	private void add2ShopgruppenDaten(FLRLagerbewegung flrLagerbewegung, FLRShopgruppe flrshopgruppe, HashMap hmBaum) {
		sucheUndFuegeBetragHinzu(flrLagerbewegung, hmBaum, flrshopgruppe.getI_id());

		if (flrshopgruppe.getFlrshopgruppe() != null) {
			add2ShopgruppenDaten(flrLagerbewegung, flrshopgruppe.getFlrshopgruppe(), hmBaum);

		}

	}

	public HashMap baumErstellenArtikelgruppe(TheClientDto theClientDto) {
		HashMap hmRoot = new HashMap();

		String sMandant = theClientDto.getMandant();

		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			sMandant = getSystemFac().getHauptmandant();
		}

		javax.persistence.Query queryLagerort = em.createNamedQuery("ArtgrufindAllRoot");
		queryLagerort.setParameter(1, sMandant);
		Collection<?> cl = queryLagerort.getResultList();
		Iterator it = cl.iterator();

		// --------------
		while (it.hasNext()) {

			Artgru flrArtikelgruppe = (Artgru) it.next();
			ReportArtikelgruppeDto r = new ReportArtikelgruppeDto();

			hmRoot.put(flrArtikelgruppe.getIId(), r);

			addUnterArtikelgruppen(hmRoot, flrArtikelgruppe.getIId());

		}

		return hmRoot;

	}

	public HashMap baumErstellenMaterialbedarfsvorschau(TheClientDto theClientDto) {
		HashMap hmRoot = new HashMap();

		String sMandant = theClientDto.getMandant();

		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			sMandant = getSystemFac().getHauptmandant();
		}

		javax.persistence.Query queryLagerort = em.createNamedQuery("ArtgrufindAllRoot");
		queryLagerort.setParameter(1, sMandant);
		Collection<?> cl = queryLagerort.getResultList();
		Iterator it = cl.iterator();

		// --------------
		while (it.hasNext()) {

			Artgru flrArtikelgruppe = (Artgru) it.next();
			ReportMaterialbedarfsvorschauDto r = new ReportMaterialbedarfsvorschauDto();

			hmRoot.put(flrArtikelgruppe.getIId(), r);

			addUnterArtikelgruppenMaterialbedarfsvorschau(hmRoot, flrArtikelgruppe.getIId());

		}

		// Unbekannt
		hmRoot.put(null, new ReportMaterialbedarfsvorschauDto());

		return hmRoot;

	}

	private HashMap addUnterArtikelgruppen(HashMap hmParent, Integer vatergruppeIId) {

		javax.persistence.Query queryLagerort = em.createNamedQuery("ArtgrufindByArtgruIId");
		queryLagerort.setParameter(1, vatergruppeIId);

		String lagerplatz = "";

		Collection<?> cl = queryLagerort.getResultList();
		if (cl.size() > 0) {

			ReportArtikelgruppeDto r = new ReportArtikelgruppeDto();
			Iterator it = cl.iterator();
			while (it.hasNext()) {
				Artgru pl = (Artgru) it.next();

				r.getSubGruppen().put(pl.getIId(), new ReportArtikelgruppeDto());

				addUnterArtikelgruppen(r.getSubGruppen(), pl.getIId());

			}

			hmParent.put(vatergruppeIId, r);

		}
		return null;
	}

	private HashMap addUnterArtikelgruppenMaterialbedarfsvorschau(HashMap hmParent, Integer vatergruppeIId) {

		javax.persistence.Query queryLagerort = em.createNamedQuery("ArtgrufindByArtgruIId");
		queryLagerort.setParameter(1, vatergruppeIId);

		String lagerplatz = "";

		Collection<?> cl = queryLagerort.getResultList();
		if (cl.size() > 0) {

			ReportMaterialbedarfsvorschauDto r = new ReportMaterialbedarfsvorschauDto();
			Iterator it = cl.iterator();
			while (it.hasNext()) {
				Artgru pl = (Artgru) it.next();

				r.getSubGruppen().put(pl.getIId(), new ReportMaterialbedarfsvorschauDto());

				addUnterArtikelgruppenMaterialbedarfsvorschau(r.getSubGruppen(), pl.getIId());

			}

			hmParent.put(vatergruppeIId, r);

		}
		return null;
	}

	public HashMap baumErstellenShopgruppe(TheClientDto theClientDto) {
		HashMap hmRoot = new HashMap();
		javax.persistence.Query queryLagerort = em.createNamedQuery("ShopgruppefindAllRoot");
		Collection<?> cl = queryLagerort.getResultList();
		Iterator it = cl.iterator();

		// --------------
		while (it.hasNext()) {

			Shopgruppe flrShopgruppe = (Shopgruppe) it.next();
			ReportArtikelgruppeDto r = new ReportArtikelgruppeDto();

			hmRoot.put(flrShopgruppe.getIId(), r);

			addUnterShopgruppen(hmRoot, flrShopgruppe.getIId());

		}

		return hmRoot;

	}

	private HashMap addUnterShopgruppen(HashMap hmParent, Integer vatergruppeIId) {

		javax.persistence.Query query = em.createNamedQuery(Shopgruppe.QueryFindByParentIId);
		query.setParameter("id", vatergruppeIId);

		Collection<?> cl = query.getResultList();
		if (cl.size() > 0) {

			ReportArtikelgruppeDto r = new ReportArtikelgruppeDto();
			Iterator it = cl.iterator();
			while (it.hasNext()) {
				Shopgruppe pl = (Shopgruppe) it.next();

				r.getSubGruppen().put(pl.getIId(), new ReportArtikelgruppeDto());

				addUnterShopgruppen(r.getSubGruppen(), pl.getIId());

			}

			hmParent.put(vatergruppeIId, r);

		}
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printMaterialbedarfsvorschau(Timestamp tVergangenheit_Von, Timestamp tVergangenheit_Bis,
			Timestamp tAngebotRealisierungstermin, Timestamp tOffeneRahmenAuftraegeBis, Timestamp tAngelegteAuftraege,
			Integer kundeIId, Integer artikelgruppeIId, boolean bNurVerrechnetePositionen, TheClientDto theClientDto) {

		HashMap hmBaum = baumErstellenMaterialbedarfsvorschau(theClientDto);
		index = -1;
		sAktuellerReport = LagerReportFac.REPORT_MATERIALBEDARFSVORSCHAU;

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		parameter.put("P_NUR_VERRECHNETE_POSITIONEN", bNurVerrechnetePositionen);

		String mandantCNr = theClientDto.getMandant();

		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			mandantCNr = getSystemFac().getHauptmandant();
		}
		KundeDto kundeDto = null;
		if (kundeIId != null) {
			kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto);

			parameter.put("P_KUNDE", kundeDto.getPartnerDto().formatAnrede());
		}

		if (artikelgruppeIId != null) {

			parameter.put("P_ARTIKELGRUPPE",
					getArtikelFac().artgruFindByPrimaryKey(artikelgruppeIId, theClientDto).getCNr());

		}

		if (tVergangenheit_Von != null && tVergangenheit_Bis != null) {
			Session session = FLRSessionFactory.getFactory().openSession();
			String queryString = "SELECT l FROM FLRLagerbewegung as l WHERE l.flrartikel.mandant_c_nr='" + mandantCNr
					+ "' AND   (l.b_abgang=1 OR (l.b_abgang=0 AND l.c_belegartnr='" + LocaleFac.BELEGART_GUTSCHRIFT
					+ "')) AND l.b_historie=0 AND l.n_menge>0 AND l.flrartikel.artikelart_c_nr NOT IN ('"
					+ ArtikelFac.ARTIKELART_HANDARTIKEL + "') AND l.c_belegartnr NOT IN ('"
					+ LocaleFac.BELEGART_INVENTUR + "') ";

			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
					theClientDto)) {
				queryString += " AND l.flrlager.mandant_c_nr='" + theClientDto.getMandant() + "'";
			}

			queryString += " AND l.c_belegartnr NOT IN ('" + LocaleFac.BELEGART_HAND + "','" + LocaleFac.BELEGART_LOS
					+ "','" + LocaleFac.BELEGART_LOSABLIEFERUNG + "')";

			queryString += " AND l.t_belegdatum >='"
					+ Helper.formatDateWithSlashes(new java.sql.Date(tVergangenheit_Von.getTime())) + "'";
			parameter.put("P_VERGANGENHEIT_VON", tVergangenheit_Von);

			queryString += " AND l.t_belegdatum <='"
					+ Helper.formatDateWithSlashes(new java.sql.Date(tVergangenheit_Bis.getTime())) + "'";
			parameter.put("P_VERGANGENHEIT_BIS", tVergangenheit_Bis);

			if (artikelgruppeIId != null) {
				queryString += " AND l.flrartikel.flrartikelgruppe.i_id=" + artikelgruppeIId;
			}

			queryString += " ORDER BY l.flrartikel.c_nr ,l.i_id_buchung ASC, l.t_buchungszeit DESC";

			Query query = session.createQuery(queryString);

			List<?> results = query.list();
			Iterator<?> resultListIterator = results.iterator();

			// 1. Vergangenheitsdaten

			// --------------
			while (resultListIterator.hasNext()) {

				FLRLagerbewegung flrLagerbewegung = (FLRLagerbewegung) resultListIterator.next();

				if (kundeDto != null) {
					try {
						BelegInfos bi = getLagerFac().getBelegInfos(flrLagerbewegung.getC_belegartnr(),
								flrLagerbewegung.getI_belegartid(), null, theClientDto);

						if (bi != null && bi.getPartnerIId() != null
								&& bi.getPartnerIId().equals(kundeDto.getPartnerIId())) {

							// Kunde passt
						} else {
							continue;
						}
					} catch (RemoteException e) {
						//
					}
				}

				// PJ20332
				if (bNurVerrechnetePositionen == true
						&& flrLagerbewegung.getC_belegartnr().equals(LocaleFac.BELEGART_LIEFERSCHEIN)) {
					LieferscheinDto lsDto = getLieferscheinFac()
							.lieferscheinFindByPrimaryKeyOhneExc(flrLagerbewegung.getI_belegartid());

					if (lsDto != null && !lsDto.getStatusCNr().equals(LieferscheinFac.LSSTATUS_VERRECHNET)) {
						continue;
					}
				}

				add2MaterialbedarfsvorschauDaten(flrLagerbewegung,
						flrLagerbewegung.getFlrartikel().getFlrartikelgruppe(), null, null, null, hmBaum);

			}
			session.close();

		}

		// 2.

		if (tAngebotRealisierungstermin != null) {

			parameter.put("P_REALISIERUNGSTERMIN", tAngebotRealisierungstermin);

			Session session = FLRSessionFactory.getFactory().openSession();

			String queryString = "SELECT l FROM FLRAngebotposition as l WHERE l.positionart_c_nr='"
					+ AngebotServiceFac.ANGEBOTPOSITIONART_IDENT + "' AND l.flrartikel.mandant_c_nr='" + mandantCNr
					+ "' AND  (l.flrangebot.t_realisierungstermin IS NULL OR l.flrangebot.t_realisierungstermin <='"
					+ Helper.formatDateWithSlashes(new java.sql.Date(tAngebotRealisierungstermin.getTime())) + "')";

			if (artikelgruppeIId != null) {
				queryString += " AND l.flrartikel.flrartikelgruppe.i_id=" + artikelgruppeIId;
			}

			if (kundeIId != null) {
				queryString += " AND l.flrangebot.kunde_i_id_angebotsadresse=" + kundeIId;
			}

			Query query = session.createQuery(queryString);
			List<?> results = query.list();
			Iterator<?> resultListIterator = results.iterator();

			// --------------
			while (resultListIterator.hasNext()) {

				FLRAngebotposition flrAngebotposition = (FLRAngebotposition) resultListIterator.next();

				add2MaterialbedarfsvorschauDaten(null, flrAngebotposition.getFlrartikel().getFlrartikelgruppe(),
						flrAngebotposition, null, null, hmBaum);

			}
			session.close();
		}

		// 3.offene Rahmen

		if (tOffeneRahmenAuftraegeBis != null) {

			parameter.put("P_RAHMENENDTERMIN", tOffeneRahmenAuftraegeBis);

			Session session = FLRSessionFactory.getFactory().openSession();

			// Hiberante Criteria fuer alle Tabellen ausgehend von meiner
			// Haupttabelle anlegen,
			// nach denen ich filtern und sortieren kann
			Criteria critAuftragposition = session.createCriteria(FLRAuftragpositionReport.class);

			critAuftragposition
					.add(Restrictions.eq("auftragpositionart_c_nr", AuftragServiceFac.AUFTRAGPOSITIONART_IDENT));

			// flrauftragpositionreport > flrauftrag
			Criteria critAuftrag = critAuftragposition
					.createCriteria(AuftragpositionFac.FLR_AUFTRAGPOSITION_FLRAUFTRAG);

			if (kundeIId != null) {
				critAuftrag.add(Restrictions.eq(AuftragFac.FLR_AUFTRAG_KUNDE_I_ID_AUFTRAGSADRESSE, kundeIId));
			}

			if (artikelgruppeIId != null) {
				Criteria critArtikel = critAuftragposition
						.createCriteria(AuftragpositionFac.FLR_AUFTRAGPOSITION_FLRARTIKEL);

				Criteria critArtikelgruppe = critArtikel.createCriteria(ArtikelFac.FLR_ARTIKEL_FLRARTIKELGRUPPE);

				critArtikelgruppe.add(Restrictions.eq("i_id", artikelgruppeIId));
			}
			// Einschraenken auf Rahmenauftraege des Mandanten
			critAuftrag.add(Restrictions.eq(AuftragFac.FLR_AUFTRAG_MANDANT_C_NR, theClientDto.getMandant()));
			critAuftrag
					.add(Restrictions.eq(AuftragFac.FLR_AUFTRAG_AUFTRAGART_C_NR, AuftragServiceFac.AUFTRAGART_RAHMEN));

			// keine stornierten oder erledigten Auftraege.
			Collection<String> cStati = new LinkedList<String>();
			cStati.add(AuftragServiceFac.AUFTRAGSTATUS_STORNIERT);
			cStati.add(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT);
			critAuftrag.add(Restrictions.not(Restrictions.in(AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR, cStati)));

			critAuftrag.add(Restrictions.le(AuftragFac.FLR_AUFTRAG_T_LIEFERTERMIN, tOffeneRahmenAuftraegeBis));

			List<?> list = critAuftrag.list();

			Iterator<?> it = list.iterator();

			// --------------
			while (it.hasNext()) {

				FLRAuftragpositionReport flrauftragposition = (FLRAuftragpositionReport) it.next();

				add2MaterialbedarfsvorschauDaten(null, flrauftragposition.getFlrartikel().getFlrartikelgruppe(), null,
						flrauftragposition, null, hmBaum);

			}
			session.close();
		}
		// 4.angelegte Auftraege

		if (tAngelegteAuftraege != null) {

			parameter.put("P_ANGELEGTEAUFTRAEGE_BIS_LIEFERTERMIN", tAngelegteAuftraege);

			Session session = FLRSessionFactory.getFactory().openSession();
			Criteria critAuftragposition = session.createCriteria(FLRAuftragpositionReport.class);

			critAuftragposition
					.add(Restrictions.eq("auftragpositionart_c_nr", AuftragServiceFac.AUFTRAGPOSITIONART_IDENT));

			// flrauftragpositionreport > flrauftrag
			Criteria critAuftrag = critAuftragposition
					.createCriteria(AuftragpositionFac.FLR_AUFTRAGPOSITION_FLRAUFTRAG);

			if (kundeIId != null) {
				critAuftrag.add(Restrictions.eq(AuftragFac.FLR_AUFTRAG_KUNDE_I_ID_AUFTRAGSADRESSE, kundeIId));
			}

			// Einschraenken auf Rahmenauftraege des Mandanten
			critAuftrag.add(Restrictions.eq(AuftragFac.FLR_AUFTRAG_MANDANT_C_NR, theClientDto.getMandant()));

			if (artikelgruppeIId != null) {
				Criteria critArtikel = critAuftragposition
						.createCriteria(AuftragpositionFac.FLR_AUFTRAGPOSITION_FLRARTIKEL);

				Criteria critArtikelgruppe = critArtikel.createCriteria(ArtikelFac.FLR_ARTIKEL_FLRARTIKELGRUPPE);

				critArtikelgruppe.add(Restrictions.eq("i_id", artikelgruppeIId));
			}

			// keine stornierten oder erledigten Auftraege.
			Collection<String> cStati = new LinkedList<String>();
			cStati.add(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT);
			critAuftrag.add(Restrictions.in(AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR, cStati));

			critAuftrag.add(Restrictions.le(AuftragFac.FLR_AUFTRAG_T_LIEFERTERMIN, tAngelegteAuftraege));

			List<?> list = critAuftrag.list();

			Iterator<?> it = list.iterator();

			// --------------
			while (it.hasNext()) {

				FLRAuftragpositionReport flrauftragposition = (FLRAuftragpositionReport) it.next();

				add2MaterialbedarfsvorschauDaten(null, flrauftragposition.getFlrartikel().getFlrartikelgruppe(), null,
						null, flrauftragposition, hmBaum);

			}
			session.close();
		}

		ArrayList alDaten = new ArrayList();

		materialbedarfsvorschauAufloesen(alDaten, hmBaum, theClientDto, 0);

		// Subreport Teil2 einbinden
		HashMap<Integer, ReportMaterialbedarfsvorschauSubreportDto> hmDatenSubreport = new HashMap();
		Iterator it = hmBaum.keySet().iterator();

		while (it.hasNext()) {

			Integer artikelgruppeId = (Integer) it.next();

			ReportMaterialbedarfsvorschauDto r = (ReportMaterialbedarfsvorschauDto) hmBaum.get(artikelgruppeId);

			hmDatenSubreport = add2SubreportMaterialbedarfsvorschau(
					ReportMaterialbedarfsvorschauSubreportDto.TYP_MENGE_VERGANGENHEIT, theClientDto, r,
					hmDatenSubreport);

			hmDatenSubreport = add2SubreportMaterialbedarfsvorschau(
					ReportMaterialbedarfsvorschauSubreportDto.TYP_MENGE_ANGELEGTE_AUFTRAEGE, theClientDto, r,
					hmDatenSubreport);

			hmDatenSubreport = add2SubreportMaterialbedarfsvorschau(
					ReportMaterialbedarfsvorschauSubreportDto.TYP_MENGE_OFFENE_ANGEBOTE, theClientDto, r,
					hmDatenSubreport);

			hmDatenSubreport = add2SubreportMaterialbedarfsvorschau(
					ReportMaterialbedarfsvorschauSubreportDto.TYP_MENGE_OFFENE_RAHMEN, theClientDto, r,
					hmDatenSubreport);

		}

		String[] fieldnames = new String[] { "Artikelnummer", "MengeAngelegteAuftraege", "MengeOffeneAngebote",
				"MengeOffeneRahmenauftraege", "MengeVergangenheit", "Arbeitszeit", "Lief1Preis", "LagerstandAlleLaeger",
				"LagerstandSperrlaeger", "Bestellt", "InFertigung", "Fehlmengen", "Reservierungen", "Rahmenbestellt",
				"Einheit", "Bezeichnung", "Zusatzbezeichnung" };

		ArrayList alDataSub = new ArrayList();

		Iterator hmSub = hmDatenSubreport.keySet().iterator();
		while (hmSub.hasNext()) {
			Object[] oZeileSub = new Object[fieldnames.length];

			ReportMaterialbedarfsvorschauSubreportDto zeile = hmDatenSubreport.get(hmSub.next());

			if (zeile == null) {
				int z = 0;
			}

			ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(zeile.getArtikelIId(), theClientDto);

			oZeileSub[0] = aDto.getCNr();
			oZeileSub[1] = zeile.getMengeAngelegteAuftraege();
			oZeileSub[2] = zeile.getMengeOffeneAngebote();
			oZeileSub[3] = zeile.getMengeOffeneRahmen();
			oZeileSub[4] = zeile.getMengeVergangenheit();
			oZeileSub[5] = aDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT) ? true : false;

			// Lief1
			try {

				ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreisDesBevorzugtenLieferanten(
						zeile.getArtikelIId(), BigDecimal.ONE, theClientDto.getSMandantenwaehrung(), theClientDto);
				if (alDto != null) {
					oZeileSub[6] = alDto.getNNettopreis();
				}
				oZeileSub[7] = getLagerFac().getLagerstandAllerLagerEinesMandanten(zeile.getArtikelIId(), theClientDto);
				oZeileSub[8] = getLagerFac().getLagerstandAllerSperrlaegerEinesMandanten(zeile.getArtikelIId(),
						theClientDto);
				oZeileSub[9] = getArtikelbestelltFac().getAnzahlBestellt(zeile.getArtikelIId());

				oZeileSub[10] = getFertigungFac().getAnzahlInFertigung(zeile.getArtikelIId(), theClientDto);

				oZeileSub[11] = getFehlmengeFac().getAnzahlFehlmengeEinesArtikels(zeile.getArtikelIId(), theClientDto);
				oZeileSub[12] = getReservierungFac().getAnzahlReservierungen(zeile.getArtikelIId(), theClientDto);

				Hashtable htAnzahlRahmenbestellt = getArtikelbestelltFac()
						.getAnzahlRahmenbestellt(zeile.getArtikelIId(), theClientDto);
				if (htAnzahlRahmenbestellt.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
					BigDecimal rahmenbestellt = (BigDecimal) htAnzahlRahmenbestellt
							.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
					oZeileSub[13] = rahmenbestellt;
				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			oZeileSub[14] = aDto.getEinheitCNr();

			if (aDto.getArtikelsprDto() != null) {
				oZeileSub[15] = aDto.getArtikelsprDto().getCBez();
				oZeileSub[16] = aDto.getArtikelsprDto().getCZbez();
			}

			alDataSub.add(oZeileSub);

		}

		// Nach Artikelnummer sortieren

		for (int m = alDataSub.size() - 1; m > 0; --m) {
			for (int n = 0; n < m; ++n) {
				Object[] o1 = (Object[]) alDataSub.get(n);
				Object[] o2 = (Object[]) alDataSub.get(n + 1);

				String artikel1 = (String) o1[0];
				String artikel2 = (String) o2[0];

				if (artikel1.compareTo(artikel2) > 0) {
					alDataSub.set(n, o2);
					alDataSub.set(n + 1, o1);
				}
			}
		}

		Object[][] dataSub = new Object[alDataSub.size()][fieldnames.length];

		dataSub = (Object[][]) alDataSub.toArray(dataSub);

		parameter.put("P_SUBREPORT_ARTIKEL", new LPDatenSubreport(dataSub, fieldnames));

		Object[][] returnArray = new Object[alDaten.size()][REPORT_MATERIALBEDARFSVORSCHAU_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(returnArray);

		initJRDS(parameter, LagerReportFac.REPORT_MODUL, LagerReportFac.REPORT_MATERIALBEDARFSVORSCHAU,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	private HashMap<Integer, ReportMaterialbedarfsvorschauSubreportDto> add2SubreportMaterialbedarfsvorschau(int iTyp,
			TheClientDto theClientDto, ReportMaterialbedarfsvorschauDto r,
			HashMap<Integer, ReportMaterialbedarfsvorschauSubreportDto> hmDatenSubreport) {
		HashMap<Integer, BigDecimal> artikelmengen = null;

		if (iTyp == ReportMaterialbedarfsvorschauSubreportDto.TYP_MENGE_VERGANGENHEIT) {
			artikelmengen = r.getArtikelmengenVergangenheit();

		} else if (iTyp == ReportMaterialbedarfsvorschauSubreportDto.TYP_MENGE_ANGELEGTE_AUFTRAEGE) {
			artikelmengen = r.getArtikelmengenAngelegteAuftraege();

		} else if (iTyp == ReportMaterialbedarfsvorschauSubreportDto.TYP_MENGE_OFFENE_ANGEBOTE) {
			artikelmengen = r.getArtikelmengenOffeneAngebote();

		} else if (iTyp == ReportMaterialbedarfsvorschauSubreportDto.TYP_MENGE_OFFENE_RAHMEN) {
			artikelmengen = r.getArtikelmengenOffeneRahmen();

		}

		Iterator itArtikel = artikelmengen.keySet().iterator();
		while (itArtikel.hasNext()) {
			Integer artikelIId = (Integer) itArtikel.next();
			BigDecimal gesamtmenge = artikelmengen.get(artikelIId);

			StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByArtikelIIdMandantCNrOhneExc(artikelIId,
					theClientDto.getMandant());

			if (stklDto == null) {
				// Handelsware

				ReportMaterialbedarfsvorschauSubreportDto reportMaterialbedarfsvorschauSubreportDto = null;

				if (hmDatenSubreport.containsKey(artikelIId)) {
					reportMaterialbedarfsvorschauSubreportDto = hmDatenSubreport.get(artikelIId);
				} else {
					reportMaterialbedarfsvorschauSubreportDto = new ReportMaterialbedarfsvorschauSubreportDto();
					reportMaterialbedarfsvorschauSubreportDto.setArtikelIId(artikelIId);
					reportMaterialbedarfsvorschauSubreportDto.setBArbeitszeit(false);

				}

				reportMaterialbedarfsvorschauSubreportDto.add2Menge(iTyp, gesamtmenge);

				hmDatenSubreport.put(artikelIId, reportMaterialbedarfsvorschauSubreportDto);
			} else {
				// Stueckliste aufloesen

				List<?> m = null;
				try {
					m = getStuecklisteFac().getStrukturDatenEinerStuecklisteMitArbeitsplan(stklDto.getIId(),
							theClientDto, StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_ARTIKELNR, 0, null,
							true, true, gesamtmenge, null, false, false, false, null);
				} catch (RemoteException ex4) {
					throwEJBExceptionLPRespectOld(ex4);
				}

				Iterator<?> itStkl = m.listIterator();

				while (itStkl.hasNext()) {
					StuecklisteMitStrukturDto struktur = (StuecklisteMitStrukturDto) itStkl.next();

					if (!struktur.isBStueckliste()) {

						Integer artikelIIdAusStkl;

						BigDecimal bdMengeGesamt = BigDecimal.ZERO;

						if (!struktur.isBArbeitszeit()) {

							StuecklistepositionDto position = struktur.getStuecklistepositionDto();
							artikelIIdAusStkl = position.getArtikelIId();
							if (Helper.short2boolean(position.getBRuestmenge())) {
								bdMengeGesamt = Helper.rundeKaufmaennisch(position.getNZielmenge(), 4);
							} else {
								bdMengeGesamt = Helper.rundeKaufmaennisch(position.getNZielmenge(gesamtmenge), 4);
							}

						} else {
							StuecklistearbeitsplanDto stuecklistearbeitsplanDto = struktur
									.getStuecklistearbeitsplanDto();
							artikelIIdAusStkl = stuecklistearbeitsplanDto.getArtikelIId();
							bdMengeGesamt = Helper.berechneGesamtzeitInStunden(
									stuecklistearbeitsplanDto.getLRuestzeit(),
									stuecklistearbeitsplanDto.getLStueckzeit(), gesamtmenge, null,
									stuecklistearbeitsplanDto.getIAufspannung());

						}

						ReportMaterialbedarfsvorschauSubreportDto reportMaterialbedarfsvorschauSubreportDto = null;

						if (hmDatenSubreport.containsKey(artikelIIdAusStkl)) {
							reportMaterialbedarfsvorschauSubreportDto = hmDatenSubreport.get(artikelIIdAusStkl);
						} else {
							reportMaterialbedarfsvorschauSubreportDto = new ReportMaterialbedarfsvorschauSubreportDto();
							reportMaterialbedarfsvorschauSubreportDto.setArtikelIId(artikelIIdAusStkl);
							reportMaterialbedarfsvorschauSubreportDto.setBArbeitszeit(struktur.isBArbeitszeit());

						}
						reportMaterialbedarfsvorschauSubreportDto.add2Menge(iTyp, bdMengeGesamt);

						hmDatenSubreport.put(artikelIIdAusStkl, reportMaterialbedarfsvorschauSubreportDto);

					}

				}

			}

		}
		return hmDatenSubreport;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printArtikelgruppen(Timestamp dVon, Timestamp dBis, boolean bMitHandlagerbewegungen,
			boolean bMitFertigung, boolean bMitVersteckten, boolean bNurVerrechnetePositionen,
			TheClientDto theClientDto) {

		HashMap hmBaum = baumErstellenArtikelgruppe(theClientDto);
		index = -1;
		sAktuellerReport = LagerReportFac.REPORT_ARTIKLEGRUPPEN;
		Session session = FLRSessionFactory.getFactory().openSession();

		String mandantCNr = theClientDto.getMandant();

		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			mandantCNr = getSystemFac().getHauptmandant();
		}

		String queryString = "SELECT l FROM FLRLagerbewegung as l WHERE l.flrartikel.mandant_c_nr='" + mandantCNr
				+ "' AND   (l.b_abgang=1 OR (l.b_abgang=0 AND l.c_belegartnr='" + LocaleFac.BELEGART_GUTSCHRIFT
				+ "')) AND l.b_historie=0 AND l.n_menge>0 AND l.flrartikel.artikelart_c_nr NOT IN ('"
				+ ArtikelFac.ARTIKELART_HANDARTIKEL + "') AND l.c_belegartnr NOT IN ('" + LocaleFac.BELEGART_INVENTUR
				+ "') ";

		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			queryString += " AND l.flrlager.mandant_c_nr='" + theClientDto.getMandant() + "'";
		}

		if (bMitVersteckten == false) {
			queryString += " AND l.flrartikel.b_versteckt = 0 ";
		}

		if (!bMitHandlagerbewegungen) {

			queryString += " AND l.c_belegartnr NOT IN ('" + LocaleFac.BELEGART_HAND + "')";
		}
		if (!bMitFertigung) {

			queryString += " AND l.c_belegartnr NOT IN ('" + LocaleFac.BELEGART_LOS + "','"
					+ LocaleFac.BELEGART_LOSABLIEFERUNG + "')";
		}

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_NUR_VERRECHNETE_POSITIONEN", bNurVerrechnetePositionen);

		parameter.put("P_BERUECKSICHTIGEFERTIGUNG", new Boolean(bMitFertigung));
		parameter.put("P_BERUECKSICHTIGEHANDBUCHUNG", new Boolean(bMitHandlagerbewegungen));
		parameter.put("P_MITVERSTECKTEN", new Boolean(bMitVersteckten));
		if (dVon != null) {
			queryString += " AND l.t_belegdatum >='" + Helper.formatDateWithSlashes(new java.sql.Date(dVon.getTime()))
					+ "'";
			parameter.put("P_VON", dVon);
		}
		if (dBis != null) {
			queryString += " AND l.t_belegdatum <='" + Helper.formatDateWithSlashes(new java.sql.Date(dBis.getTime()))
					+ "'";
			parameter.put("P_BIS", dBis);
		}

		queryString += " ORDER BY l.flrartikel.c_nr ,l.i_id_buchung ASC, l.t_buchungszeit DESC";

		Query query = session.createQuery(queryString);

		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		ReportArtikelgruppeDto r = new ReportArtikelgruppeDto();

		BigDecimal gestwertUnbekannt = new BigDecimal(0);
		BigDecimal vkWertUnbekannt = new BigDecimal(0);
		BigDecimal mengeUnbekannt = new BigDecimal(0);

		// --------------
		while (resultListIterator.hasNext()) {

			FLRLagerbewegung flrLagerbewegung = (FLRLagerbewegung) resultListIterator.next();

			// PJ20332
			if (bNurVerrechnetePositionen == true
					&& flrLagerbewegung.getC_belegartnr().equals(LocaleFac.BELEGART_LIEFERSCHEIN)) {
				LieferscheinDto lsDto = getLieferscheinFac()
						.lieferscheinFindByPrimaryKeyOhneExc(flrLagerbewegung.getI_belegartid());

				if (lsDto != null && !lsDto.getStatusCNr().equals(LieferscheinFac.LSSTATUS_VERRECHNET)) {
					continue;
				}
			}

			if (flrLagerbewegung.getFlrartikel().getFlrartikelgruppe() != null) {
				add2ArtikelgruppenDaten(flrLagerbewegung, flrLagerbewegung.getFlrartikel().getFlrartikelgruppe(),
						hmBaum);
			} else {
				if (Helper.short2boolean(flrLagerbewegung.getB_abgang())) {
					gestwertUnbekannt = gestwertUnbekannt
							.add(flrLagerbewegung.getN_menge().multiply(flrLagerbewegung.getN_gestehungspreis()));
					vkWertUnbekannt = vkWertUnbekannt
							.add(flrLagerbewegung.getN_menge().multiply(flrLagerbewegung.getN_verkaufspreis()));
					mengeUnbekannt = mengeUnbekannt.add(flrLagerbewegung.getN_menge());
				} else {
					BigDecimal vkPreis = BigDecimal.ZERO;

					try {
						RechnungPositionDto rechposDto = getRechnungFac()
								.rechnungPositionFindByPrimaryKeyOhneExc(flrLagerbewegung.getI_belegartpositionid());

						if (rechposDto != null) {
							RechnungDto rechnungDto = getRechnungFac()
									.rechnungFindByPrimaryKey(rechposDto.getRechnungIId());

							vkPreis = rechposDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()
									.divide(rechnungDto.getNKurs(), 5, BigDecimal.ROUND_HALF_EVEN);

							System.out.println(rechnungDto.getCNr() + " " + vkPreis);

						} else {

							vkPreis = flrLagerbewegung.getN_einstandspreis();
						}
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

					gestwertUnbekannt = gestwertUnbekannt
							.subtract(flrLagerbewegung.getN_menge().multiply(flrLagerbewegung.getN_gestehungspreis()));
					vkWertUnbekannt = vkWertUnbekannt.subtract(flrLagerbewegung.getN_menge().multiply(vkPreis));
					mengeUnbekannt = mengeUnbekannt.subtract(flrLagerbewegung.getN_menge());

				}

			}

		}
		session.close();

		ArrayList alDaten = new ArrayList();

		Object[] oZeile = new Object[REPORT_ARTIKELGRUPPE_ANZAHL_SPALTEN];
		oZeile[REPORT_ARTIKELGRUPPE_ARTIKELGRUPPE] = "Unbekannt";
		oZeile[REPORT_ARTIKELGRUPPE_VKWERT] = vkWertUnbekannt;
		oZeile[REPORT_ARTIKELGRUPPE_GESTWERT] = gestwertUnbekannt;
		oZeile[REPORT_ARTIKELGRUPPE_MENGE] = mengeUnbekannt;
		alDaten.add(oZeile);

		artikelgruppenAufloesen(alDaten, hmBaum, theClientDto, 0);

		Object[][] returnArray = new Object[alDaten.size()][3];
		data = (Object[][]) alDaten.toArray(returnArray);

		initJRDS(parameter, LagerReportFac.REPORT_MODUL, LagerReportFac.REPORT_ARTIKLEGRUPPEN,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printShopgruppen(Timestamp dVon, Timestamp dBis, boolean bMitHandlagerbewegungen,
			boolean bMitFertigung, boolean bMitVersteckten, boolean bNurVerrechnetePositionen,
			TheClientDto theClientDto) {

		HashMap hmBaum = baumErstellenShopgruppe(theClientDto);
		index = -1;
		sAktuellerReport = LagerReportFac.REPORT_SHOPGRUPPEN;
		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT l FROM FLRLagerbewegung as l WHERE l.flrartikel.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' AND l.b_abgang=1 AND l.b_historie=0 AND l.n_menge>0 AND l.flrartikel.artikelart_c_nr NOT IN ('"
				+ ArtikelFac.ARTIKELART_HANDARTIKEL + "') AND l.c_belegartnr NOT IN ('" + LocaleFac.BELEGART_INVENTUR
				+ "') ";

		if (bMitVersteckten == false) {
			queryString += " AND l.flrartikel.b_versteckt = 0 ";
		}

		if (!bMitHandlagerbewegungen) {

			queryString += " AND l.c_belegartnr NOT IN ('" + LocaleFac.BELEGART_HAND + "')";
		}
		if (!bMitFertigung) {

			queryString += " AND l.c_belegartnr NOT IN ('" + LocaleFac.BELEGART_LOS + "','"
					+ LocaleFac.BELEGART_LOSABLIEFERUNG + "')";
		}

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_NUR_VERRECHNETE_POSITIONEN", bNurVerrechnetePositionen);

		parameter.put("P_BERUECKSICHTIGEFERTIGUNG", new Boolean(bMitFertigung));
		parameter.put("P_BERUECKSICHTIGEHANDBUCHUNG", new Boolean(bMitHandlagerbewegungen));
		parameter.put("P_MITVERSTECKTEN", new Boolean(bMitVersteckten));
		if (dVon != null) {
			queryString += " AND l.t_belegdatum >='" + Helper.formatDateWithSlashes(new java.sql.Date(dVon.getTime()))
					+ "'";
			parameter.put("P_VON", dVon);
		}
		if (dBis != null) {
			queryString += " AND l.t_belegdatum <='" + Helper.formatDateWithSlashes(new java.sql.Date(dBis.getTime()))
					+ "'";
			parameter.put("P_BIS", dBis);
		}

		queryString += " ORDER BY l.flrartikel.c_nr ,l.i_id_buchung ASC, l.t_buchungszeit DESC";

		Query query = session.createQuery(queryString);

		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		BigDecimal gestwertUnbekannt = new BigDecimal(0);
		BigDecimal vkWertUnbekannt = new BigDecimal(0);
		BigDecimal mengeUnbekannt = new BigDecimal(0);

		// --------------
		while (resultListIterator.hasNext()) {

			FLRLagerbewegung flrLagerbewegung = (FLRLagerbewegung) resultListIterator.next();

			// PJ20332
			if (bNurVerrechnetePositionen == true
					&& flrLagerbewegung.getC_belegartnr().equals(LocaleFac.BELEGART_LIEFERSCHEIN)) {
				LieferscheinDto lsDto = getLieferscheinFac()
						.lieferscheinFindByPrimaryKeyOhneExc(flrLagerbewegung.getI_belegartid());

				if (lsDto != null && !lsDto.getStatusCNr().equals(LieferscheinFac.LSSTATUS_VERRECHNET)) {
					continue;
				}
			}

			if (flrLagerbewegung.getFlrartikel().getFlrshopgruppe() != null) {
				add2ShopgruppenDaten(flrLagerbewegung, flrLagerbewegung.getFlrartikel().getFlrshopgruppe(), hmBaum);
			} else {

				if (Helper.short2boolean(flrLagerbewegung.getB_abgang())) {
					gestwertUnbekannt = gestwertUnbekannt
							.add(flrLagerbewegung.getN_menge().multiply(flrLagerbewegung.getN_gestehungspreis()));
					vkWertUnbekannt = vkWertUnbekannt
							.add(flrLagerbewegung.getN_menge().multiply(flrLagerbewegung.getN_verkaufspreis()));
					mengeUnbekannt = mengeUnbekannt.add(flrLagerbewegung.getN_menge());

				} else if (flrLagerbewegung.getC_belegartnr().equals(LocaleFac.BELEGART_GUTSCHRIFT)) {

					gestwertUnbekannt = gestwertUnbekannt
							.subtract(flrLagerbewegung.getN_menge().multiply(flrLagerbewegung.getN_gestehungspreis()));
					vkWertUnbekannt = vkWertUnbekannt
							.subtract(flrLagerbewegung.getN_menge().multiply(flrLagerbewegung.getN_einstandspreis()));
					mengeUnbekannt = mengeUnbekannt.subtract(flrLagerbewegung.getN_menge());
				}

			}

		}
		session.close();

		ArrayList alDaten = new ArrayList();

		Object[] oZeile = new Object[4];
		oZeile[REPORT_SHOPGRUPPE_SHOPGRUPPE] = "Unbekannt";
		oZeile[REPORT_SHOPGRUPPE_VKWERT] = vkWertUnbekannt;
		oZeile[REPORT_SHOPGRUPPE_GESTWERT] = gestwertUnbekannt;
		oZeile[REPORT_SHOPGRUPPE_MENGE] = mengeUnbekannt;
		alDaten.add(oZeile);

		shopgruppenAufloesen(alDaten, hmBaum, theClientDto, 0);

		Object[][] returnArray = new Object[alDaten.size()][3];
		data = (Object[][]) alDaten.toArray(returnArray);

		initJRDS(parameter, LagerReportFac.REPORT_MODUL, LagerReportFac.REPORT_SHOPGRUPPEN, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	private void materialbedarfsvorschauAufloesen(ArrayList alDaten, HashMap hmBaum, TheClientDto theClientDto,
			int iEbene) {

		Iterator it = hmBaum.keySet().iterator();

		while (it.hasNext()) {

			Integer artikelgruppeId = (Integer) it.next();

			ReportMaterialbedarfsvorschauDto r = (ReportMaterialbedarfsvorschauDto) hmBaum.get(artikelgruppeId);

			String ebene = "";
			for (int i = 0; i < iEbene; i++) {
				ebene += "  ";
			}
			Object[] oZeile = new Object[REPORT_MATERIALBEDARFSVORSCHAU_ANZAHL_SPALTEN];

			if (artikelgruppeId != null) {
				oZeile[REPORT_MATERIALBEDARFSVORSCHAU_ARTIKELGRUPPE] = ebene
						+ getArtikelFac().artgruFindByPrimaryKey(artikelgruppeId, theClientDto).getBezeichnung();
			} else {
				oZeile[REPORT_MATERIALBEDARFSVORSCHAU_ARTIKELGRUPPE] = "Unbekannt";
			}

			oZeile[REPORT_MATERIALBEDARFSVORSCHAU_MENGE_VERGANGENHEIT] = r.getMengeVergangenheit();
			oZeile[REPORT_MATERIALBEDARFSVORSCHAU_MENGE_OFFENE_RAHMENAUFTRAEGE] = r.getMengeOffeneRahmen();
			oZeile[REPORT_MATERIALBEDARFSVORSCHAU_MENGE_OFFENE_ANGEBOTE] = r.getMengeOffeneAngebote();
			oZeile[REPORT_MATERIALBEDARFSVORSCHAU_MENGE_ANGELEGTE_AUFTRAEGE] = r.getMengeAngelegteAuftraege();

			alDaten.add(oZeile);

			if (r.getSubGruppen() != null) {
				int iEbeneTmp = iEbene + 1;
				materialbedarfsvorschauAufloesen(alDaten, r.getSubGruppen(), theClientDto, iEbeneTmp);
			}

		}

	}

	private void artikelgruppenAufloesen(ArrayList alDaten, HashMap hmBaum, TheClientDto theClientDto, int iEbene) {

		Iterator it = hmBaum.keySet().iterator();

		while (it.hasNext()) {

			Integer artikelgruppeId = (Integer) it.next();

			ReportArtikelgruppeDto r = (ReportArtikelgruppeDto) hmBaum.get(artikelgruppeId);

			String ebene = "";
			for (int i = 0; i < iEbene; i++) {
				ebene += "  ";
			}
			Object[] oZeile = new Object[REPORT_ARTIKELGRUPPE_ANZAHL_SPALTEN];
			try {

				ArtgruDto artgruDto = getArtikelFac().artgruFindByPrimaryKey(artikelgruppeId, theClientDto);

				oZeile[REPORT_ARTIKELGRUPPE_ARTIKELGRUPPE] = ebene + artgruDto.getBezeichnung();

				if (artgruDto.getKontoIId() != null) {
					KontoDto ktDto = getFinanzFac().kontoFindByPrimaryKey(artgruDto.getKontoIId());

					oZeile[REPORT_ARTIKELGRUPPE_KONTO] = ktDto.getCNr();
					oZeile[REPORT_ARTIKELGRUPPE_KONTOBEZEICHNUNG] = ktDto.getCBez();

					if (ktDto.getErgebnisgruppeIId() != null) {
						oZeile[REPORT_ARTIKELGRUPPE_ERGEBNISGRUPPE] = getFinanzFac()
								.ergebnisgruppeFindByPrimaryKey(ktDto.getErgebnisgruppeIId()).getCBez();
					}

				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
			oZeile[REPORT_ARTIKELGRUPPE_VKWERT] = r.getUmsatz();
			oZeile[REPORT_ARTIKELGRUPPE_GESTWERT] = r.getGestwert();
			oZeile[REPORT_ARTIKELGRUPPE_MENGE] = r.getMenge();

			alDaten.add(oZeile);

			if (r.getSubGruppen() != null) {
				int iEbeneTmp = iEbene + 1;
				artikelgruppenAufloesen(alDaten, r.getSubGruppen(), theClientDto, iEbeneTmp);
			}

		}

	}

	private void shopgruppenAufloesen(ArrayList alDaten, HashMap hmBaum, TheClientDto theClientDto, int iEbene) {

		Iterator it = hmBaum.keySet().iterator();

		while (it.hasNext()) {

			Integer shopgruppeId = (Integer) it.next();

			ReportArtikelgruppeDto r = (ReportArtikelgruppeDto) hmBaum.get(shopgruppeId);

			String ebene = "";
			for (int i = 0; i < iEbene; i++) {
				ebene += "  ";
			}
			Object[] oZeile = new Object[4];

			oZeile[REPORT_SHOPGRUPPE_SHOPGRUPPE] = ebene
					+ getArtikelFac().shopgruppeFindByPrimaryKey(shopgruppeId, theClientDto).getBezeichnung();

			oZeile[REPORT_SHOPGRUPPE_VKWERT] = r.getUmsatz();
			oZeile[REPORT_SHOPGRUPPE_GESTWERT] = r.getGestwert();
			oZeile[REPORT_SHOPGRUPPE_MENGE] = r.getMenge();

			alDaten.add(oZeile);

			if (r.getSubGruppen() != null) {
				int iEbeneTmp = iEbene + 1;
				shopgruppenAufloesen(alDaten, r.getSubGruppen(), theClientDto, iEbeneTmp);
			}

		}

	}

	private TreeMap<String, ArrayList> holeZugangsbeleg(String lieferbeleg, String verbrauchsbeleg, String kunde,
			Integer kundeIId, FLRLagerbewegung l, TreeMap<String, ArrayList> tmDaten, int iEbene, String kopfsortierung,
			BigDecimal bdFaktorFuerNaechstEbene, TheClientDto theClientDto) {

		Object[] oZeileVorlage = new Object[REPORT_INDIREKTE_WE_STATISTIK_ANZAHL_SPALTEN];
		oZeileVorlage[REPORT_INDIREKTE_WE_STATISTIK_LIEFERBELEG] = lieferbeleg;
		oZeileVorlage[REPORT_INDIREKTE_WE_STATISTIK_KUNDE] = kunde;
		oZeileVorlage[REPORT_INDIREKTE_WE_STATISTIK_VERBRAUCHSBELEG] = verbrauchsbeleg;
		oZeileVorlage[REPORT_INDIREKTE_WE_STATISTIK_VERBRAUCHSDATUM] = l.getT_buchungszeit();
		oZeileVorlage[REPORT_INDIREKTE_WE_STATISTIK_ARTIKELNUMMER] = l.getFlrartikel().getC_nr();
		oZeileVorlage[REPORT_INDIREKTE_WE_STATISTIK_ARTIKELREFERENZNUMMER] = l.getFlrartikel().getC_referenznr();

		//
		ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(l.getFlrartikel().getI_id(), theClientDto);
		if (aDto.getArtikelsprDto() != null) {
			oZeileVorlage[REPORT_INDIREKTE_WE_STATISTIK_BEZEICHNUNG] = aDto.getArtikelsprDto().getCBez();
			oZeileVorlage[REPORT_INDIREKTE_WE_STATISTIK_ZUSATZBEZEICHNUNG] = aDto.getArtikelsprDto().getCZbez();
		}

		oZeileVorlage[REPORT_INDIREKTE_WE_STATISTIK_EBENE] = new Integer(iEbene);

		if (bdFaktorFuerNaechstEbene != null) {
			oZeileVorlage[REPORT_INDIREKTE_WE_STATISTIK_MENGE] = l.getN_menge().divide(bdFaktorFuerNaechstEbene, 6,
					BigDecimal.ROUND_HALF_EVEN);
		} else {
			oZeileVorlage[REPORT_INDIREKTE_WE_STATISTIK_MENGE] = l.getN_menge();
		}

		oZeileVorlage[REPORT_INDIREKTE_WE_STATISTIK_VKPREIS_LIEFERBELEG] = l.getN_verkaufspreis();
		oZeileVorlage[REPORT_INDIREKTE_WE_STATISTIK_LIEFERDATUM] = l.getT_belegdatum();
		oZeileVorlage[REPORT_INDIREKTE_WE_STATISTIK_GESTEHUNGSPREIS] = l.getN_gestehungspreis();

		// VK-Preisfindung

		try {
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKeySmall(kundeIId);

			if (kundeDto.getMwstsatzbezIId() != null) {
				Date belegDate = Helper.cutDate(new Date(l.getT_belegdatum().getTime()));
				MwstsatzDto mwstsatzKunde = getMandantFac().mwstsatzZuDatumValidate(kundeDto.getMwstsatzbezIId(),
						new Timestamp(belegDate.getTime()), theClientDto);
				VkpreisfindungDto vkpreisDto = getVkPreisfindungFac().verkaufspreisfindung(l.getFlrartikel().getI_id(),
						kundeIId, l.getN_menge(), belegDate, kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
//						getMandantFac()
//								.mwstsatzFindByMwstsatzbezIIdAktuellster(kundeDto.getMwstsatzbezIId(), theClientDto)
//								.getIId(),
						mwstsatzKunde.getIId(), theClientDto.getSMandantenwaehrung(), theClientDto);

				VerkaufspreisDto kundenVKPreisDto = Helper.getVkpreisBerechnet(vkpreisDto);

				if (kundenVKPreisDto != null && kundenVKPreisDto.nettopreis != null) {

					oZeileVorlage[REPORT_INDIREKTE_WE_STATISTIK_VKPREIS] = kundenVKPreisDto.nettopreis;

				}
			}

			// KundeArtikelnr gueltig zu Belegdatum
			KundesokoDto kundeSokoDto_gueltig = this.getKundesokoFac()
					.kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc(kundeDto.getIId(),
							l.getFlrartikel().getI_id(), new java.sql.Date(l.getT_belegdatum().getTime()));
			if (kundeSokoDto_gueltig != null) {
				oZeileVorlage[REPORT_INDIREKTE_WE_STATISTIK_KUNDENARTIKELNUMMER] = kundeSokoDto_gueltig
						.getCKundeartikelnummer();
			}

		} catch (RemoteException e1) {
			throwEJBExceptionLPRespectOld(e1);
		}

		String sortierung = Helper.fitString2Length(kunde, 80, ' ') + kopfsortierung;

		ArrayList alDaten = null;

		if (tmDaten.containsKey(sortierung)) {
			alDaten = tmDaten.get(sortierung);
		} else {
			alDaten = new ArrayList();
		}
		alDaten.add(oZeileVorlage);
		tmDaten.put(sortierung, alDaten);
		iEbene++;

		try {
			LagerabgangursprungDto[] urspruenge = getLagerFac()
					.lagerabgangursprungFindByLagerbewegungIIdBuchung(l.getI_id_buchung());

			boolean bZeileBereitsVerwendet = false;

			for (int i = 0; i < urspruenge.length; i++) {
				urspruenge[i].getILagerbewegungidursprung();

				Object[] oZeile = oZeileVorlage.clone();

				oZeile[REPORT_INDIREKTE_WE_STATISTIK_EBENE] = new Integer(iEbene);
				Session s2 = FLRSessionFactory.getFactory().openSession();
				String queryString2 = " SELECT l FROM FLRLagerbewegung l WHERE l.i_id_buchung="
						+ urspruenge[i].getILagerbewegungidursprung() + " AND l.b_historie=0 AND l.n_menge>0";
				Query query2 = s2.createQuery(queryString2);
				List<?> resultList2 = query2.list();
				Iterator<?> resultListIterator2 = resultList2.iterator();
				while (resultListIterator2.hasNext()) {
					FLRLagerbewegung lUrsprung = (FLRLagerbewegung) resultListIterator2.next();

					if (lUrsprung.getC_belegartnr().equals(LocaleFac.BELEGART_LOSABLIEFERUNG)) {

						LosDto losDto = getFertigungFac().losFindByPrimaryKeyOhneExc(lUrsprung.getI_belegartid());
						if (losDto != null) {

							bdFaktorFuerNaechstEbene = losDto.getNLosgroesse().divide(l.getN_menge(), 6,
									BigDecimal.ROUND_HALF_EVEN);

							LossollmaterialDto[] sollDtos = getFertigungFac()
									.lossollmaterialFindByLosIIdOrderByISort(losDto.getIId());
							for (int j = 0; j < sollDtos.length; j++) {

								LosistmaterialDto[] istDtos = getFertigungFac()
										.losistmaterialFindByLossollmaterialIId(sollDtos[j].getIId());
								for (int k = 0; k < istDtos.length; k++) {

									Session s = FLRSessionFactory.getFactory().openSession();
									String queryString = " SELECT l FROM FLRLagerbewegung l WHERE l.c_belegartnr='"
											+ LocaleFac.BELEGART_LOS + "' AND l.i_belegartpositionid="
											+ istDtos[k].getIId() + " AND l.n_menge>0";
									Query query = s.createQuery(queryString);
									List<?> resultList = query.list();
									Iterator<?> resultListIterator = resultList.iterator();
									while (resultListIterator.hasNext()) {
										FLRLagerbewegung lIstmaterial = (FLRLagerbewegung) resultListIterator.next();

										holeZugangsbeleg(lieferbeleg, "LO" + losDto.getCNr(), kunde, kundeIId,
												lIstmaterial, tmDaten, iEbene, kopfsortierung, bdFaktorFuerNaechstEbene,
												theClientDto);

									}
									s.close();

								}
							}

						}

					} else {

						ArrayList alDatenVorhanden = null;

						if (tmDaten.containsKey(sortierung)) {
							alDatenVorhanden = tmDaten.get(sortierung);
						} else {
							alDatenVorhanden = new ArrayList();
						}

						// Zu vorherigen Zeile hinzufuegen

						Object[] oZeileVorher = null;
						if (bZeileBereitsVerwendet == false) {
							oZeileVorher = (Object[]) alDatenVorhanden.get(alDatenVorhanden.size() - 1);
						} else {
							oZeileVorher = oZeileVorlage.clone();

						}

						BelegInfos biDto = getLagerFac().getBelegInfos(lUrsprung.getC_belegartnr(),
								lUrsprung.getI_belegartid(), null, theClientDto);

						if (bdFaktorFuerNaechstEbene != null) {
							oZeileVorher[REPORT_INDIREKTE_WE_STATISTIK_MENGE] = urspruenge[i].getNVerbrauchtemenge()
									.divide(bdFaktorFuerNaechstEbene, 4, BigDecimal.ROUND_HALF_EVEN);
						} else {
							oZeileVorher[REPORT_INDIREKTE_WE_STATISTIK_MENGE] = urspruenge[i].getNVerbrauchtemenge();
						}

						oZeileVorher[REPORT_INDIREKTE_WE_STATISTIK_WARENEINGANGSBELEG] = lUrsprung.getC_belegartnr()
								.trim() + " " + biDto.getBelegnummer();
						oZeileVorher[REPORT_INDIREKTE_WE_STATISTIK_EINSTANDSPREIS] = lUrsprung.getN_einstandspreis();

						oZeileVorher[REPORT_INDIREKTE_WE_STATISTIK_WARENEINGANGSDATUM] = lUrsprung.getT_belegdatum();

						oZeileVorher[REPORT_INDIREKTE_WE_STATISTIK_LIEFERANT] = biDto.getKundeLieferant();

						if (bZeileBereitsVerwendet == false) {
							alDatenVorhanden.set(alDatenVorhanden.size() - 1, oZeileVorher);
						} else {
							alDatenVorhanden.add(oZeileVorher);
						}

						tmDaten.put(sortierung, alDatenVorhanden);
						bZeileBereitsVerwendet = true;

					}

				}

			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return tmDaten;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printIndirekterWareneinsatz(DatumsfilterVonBis datumsfilter, Integer kundeIId, int iSortierung,
			TheClientDto theClientDto) {
		sAktuellerReport = LagerReportFac.REPORT_INDIREKTE_WARENEINSATZ_STATISTIK;

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_VON", datumsfilter.getTimestampVon());
		parameter.put("P_BIS", datumsfilter.getTimestampBisUnveraendert());
		if (iSortierung == REPORT_INDIREKTE_WARENEINSATZ_SORTIERUNG_KUNDE_ARTIKEL) {
			parameter.put("P_SORTIERUNG", "Kunde + Artikelnummer");
		} else if (iSortierung == REPORT_INDIREKTE_WARENEINSATZ_SORTIERUNG_KUNDE_BELEG) {
			parameter.put("P_SORTIERUNG", "Kunde + Belegnummer");
		}

		if (kundeIId != null) {
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto);
			parameter.put("P_KUNDE", kundeDto.getPartnerDto().formatAnrede());
		}

		Session sessionLS = FLRSessionFactory.getFactory().openSession();

		String queryStringLS = " SELECT l  FROM FLRLieferscheinposition l WHERE l.flrlieferschein.d_belegdatum>='"
				+ Helper.formatTimestampWithSlashes(datumsfilter.getTimestampVon())
				+ "' AND l.flrlieferschein.d_belegdatum <'"
				+ Helper.formatTimestampWithSlashes(datumsfilter.getTimestampBis())
				+ "' AND  l.n_menge <> 0 AND l.flrlieferschein.lieferscheinstatus_status_c_nr <>'"
				+ LocaleFac.STATUS_STORNIERT + "' AND l.flrartikel.artikelart_c_nr='" + ArtikelFac.ARTIKELART_ARTIKEL
				+ "' ";

		if (kundeIId != null) {
			queryStringLS += " AND l.flrlieferschein.flrkunde.i_id=" + kundeIId;

		}

		Query queryLS = sessionLS.createQuery(queryStringLS);
		List<?> resultListLS = queryLS.list();

		Iterator<?> resultListIteratorLS = resultListLS.iterator();
		TreeMap<String, ArrayList> tmDaten = new TreeMap<String, ArrayList>();
		while (resultListIteratorLS.hasNext()) {
			FLRLieferscheinposition flrLieferscheinposition = (FLRLieferscheinposition) resultListIteratorLS.next();

			Session s2 = FLRSessionFactory.getFactory().openSession();
			String queryString2 = " SELECT l FROM FLRLagerbewegung l WHERE l.c_belegartnr='"
					+ LocaleFac.BELEGART_LIEFERSCHEIN + "' AND l.i_belegartpositionid="
					+ flrLieferscheinposition.getI_id() + " AND l.n_menge>0";
			Query query2 = s2.createQuery(queryString2);
			List<?> resultList2 = query2.list();
			Iterator<?> resultListIterator2 = resultList2.iterator();
			while (resultListIterator2.hasNext()) {
				FLRLagerbewegung l = (FLRLagerbewegung) resultListIterator2.next();

				String lsNr = "LS" + flrLieferscheinposition.getFlrlieferschein().getC_nr();

				String kopfsortierung = "";
				if (iSortierung == REPORT_INDIREKTE_WARENEINSATZ_SORTIERUNG_KUNDE_ARTIKEL) {
					kopfsortierung = l.getFlrartikel().getC_nr();
				} else {
					kopfsortierung = lsNr;
				}

				holeZugangsbeleg(lsNr, null,
						HelperServer.formatNameAusFLRPartner(
								flrLieferscheinposition.getFlrlieferschein().getFlrkunde().getFlrpartner()),
						flrLieferscheinposition.getFlrlieferschein().getFlrkunde().getI_id(), l, tmDaten, 1,
						kopfsortierung, null, theClientDto);

			}
			s2.close();
		}
		sessionLS.close();

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = " SELECT l  FROM FLRRechnungPosition l WHERE l.flrrechnung.d_belegdatum>='"
				+ Helper.formatTimestampWithSlashes(datumsfilter.getTimestampVon())
				+ "' AND l.flrrechnung.d_belegdatum <'"
				+ Helper.formatTimestampWithSlashes(datumsfilter.getTimestampBis())
				+ "' AND  l.n_menge <> 0 AND l.flrrechnung.status_c_nr <>'" + LocaleFac.STATUS_STORNIERT
				+ "' AND l.flrartikel.artikelart_c_nr='" + ArtikelFac.ARTIKELART_ARTIKEL + "' ";

		if (kundeIId != null) {
			queryString += " AND l.flrrechnung.flrkunde.i_id=" + kundeIId;

		}

		Query query = session.createQuery(queryString);
		List<?> resultList = query.list();

		Iterator<?> resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			FLRRechnungPosition flrRechnungposition = (FLRRechnungPosition) resultListIterator.next();

			String belegartCNr = LocaleFac.BELEGART_RECHNUNG;
			String kuerzel = "RE";

			if (flrRechnungposition.getFlrrechnung().getFlrrechnungart().getRechnungtyp_c_nr()
					.equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
				belegartCNr = LocaleFac.BELEGART_GUTSCHRIFT;
				kuerzel = "GS";
			}

			Session s2 = FLRSessionFactory.getFactory().openSession();
			String queryString2 = " SELECT l FROM FLRLagerbewegung l WHERE l.c_belegartnr='" + belegartCNr
					+ "' AND l.i_belegartpositionid=" + flrRechnungposition.getI_id() + " AND l.n_menge>0";
			Query query2 = s2.createQuery(queryString2);
			List<?> resultList2 = query2.list();
			Iterator<?> resultListIterator2 = resultList2.iterator();
			while (resultListIterator2.hasNext()) {
				FLRLagerbewegung l = (FLRLagerbewegung) resultListIterator2.next();

				String reNr = kuerzel + flrRechnungposition.getFlrrechnung().getC_nr();

				String kopfsortierung = "";
				if (iSortierung == REPORT_INDIREKTE_WARENEINSATZ_SORTIERUNG_KUNDE_ARTIKEL) {
					kopfsortierung = l.getFlrartikel().getC_nr();
				} else {
					kopfsortierung = reNr;
				}

				holeZugangsbeleg(reNr, null,
						HelperServer.formatNameAusFLRPartner(
								flrRechnungposition.getFlrrechnung().getFlrkunde().getFlrpartner()),
						flrRechnungposition.getFlrrechnung().getFlrkunde().getI_id(), l, tmDaten, 1, kopfsortierung,
						null, theClientDto);

			}
			s2.close();
		}
		session.close();

		// Sortieren
		ArrayList alDaten = new ArrayList();

		Iterator it = tmDaten.keySet().iterator();
		while (it.hasNext()) {
			String sort = (String) it.next();
			ArrayList al = tmDaten.get(sort);

			for (int i = 0; i < al.size(); i++) {
				alDaten.add(al.get(i));
			}

		}

		Object[][] returnArray = new Object[alDaten.size()][REPORT_INDIREKTE_WE_STATISTIK_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(returnArray);

		initJRDS(parameter, LagerReportFac.REPORT_MODUL, LagerReportFac.REPORT_INDIREKTE_WARENEINSATZ_STATISTIK,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printGestpreisUeberMinVK(Integer lagerIId, Integer vkPreislisteIId, boolean bMitVersteckten,
			boolean bVergleichMitMinVKPReis, boolean bMitStuecklisten, TheClientDto theClientDto) {

		sAktuellerReport = LagerReportFac.REPORT_GESTPREISUEBERMINVK;

		// SP6794
		String eingeloggterMandant = theClientDto.getMandant();
		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			eingeloggterMandant = getSystemFac().getHauptmandant();
		}

		String queryString = "SELECT artikelliste.i_id, artikelliste.c_nr, aspr.c_bez, (SELECT sum(artikellager.n_lagerstand) FROM FLRArtikellager AS artikellager WHERE artikellager.compId.artikel_i_id=artikelliste.i_id AND artikellager.compId.lager_i_id="
				+ lagerIId
				+ " ) ,(SELECT sum(artikellager.n_gestehungspreis*artikellager.n_lagerstand) FROM FLRArtikellager AS artikellager WHERE artikellager.compId.artikel_i_id=artikelliste.i_id AND artikellager.compId.lager_i_id="
				+ lagerIId
				+ ") , artikelliste.b_versteckt, (SELECT stkl FROM FLRStueckliste stkl WHERE stkl.artikel_i_id=artikelliste.i_id AND stkl.mandant_c_nr=artikelliste.mandant_c_nr),artikelliste.artikelart_c_nr, artikelliste.c_referenznr "
				+ " FROM FLRArtikelliste AS artikelliste" + " LEFT OUTER JOIN artikelliste.artikelsprset AS aspr ";

		queryString = queryString + " WHERE artikelliste.mandant_c_nr='" + eingeloggterMandant
				+ "' AND artikelliste.artikelart_c_nr not in('" + ArtikelFac.ARTIKELART_HANDARTIKEL
				+ "') AND artikelliste.b_lagerbewirtschaftet=1 ";

		if (bMitVersteckten == false) {
			queryString += " AND artikelliste.b_versteckt = 0 ";
		}

		queryString += " ORDER BY artikelliste.c_nr";

		Session session = FLRSessionFactory.getFactory().openSession();

		session.enableFilter("filterMandant").setParameter("paramMandant", theClientDto.getMandant());
		session.enableFilter("filterLocale").setParameter("paramLocale", Helper.locale2String(theClientDto.getLocUi()));

		Query query = session.createQuery(queryString);

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

		List<?> resultList = query.list();

		Iterator<?> resultListIterator = resultList.iterator();
		ArrayList alDaten = new ArrayList();
		while (resultListIterator.hasNext()) {
			Object o[] = (Object[]) resultListIterator.next();

			BigDecimal lagerstand = (BigDecimal) o[3];
			BigDecimal vergleichspreis = (BigDecimal) o[4]; // Gestehungspreis
			boolean bVersteckt = Helper.short2boolean((Short) o[5]);

			FLRStueckliste stkl = (FLRStueckliste) o[6];
			String artikelart = (String) o[7];
			String referenznr = (String) o[8];

			if (vergleichspreis != null && lagerstand.doubleValue() > 0) {
				vergleichspreis = vergleichspreis.divide(lagerstand, 4, BigDecimal.ROUND_HALF_EVEN);
			}

			try {
				BigDecimal vkPreis = new BigDecimal(0);

				BigDecimal materialzuschlag = getMaterialFac().getMaterialzuschlagVKInZielwaehrung((Integer) o[0], null,
						Helper.cutDate(new java.sql.Date(System.currentTimeMillis())),
						theClientDto.getSMandantenwaehrung(), theClientDto);

				if (bVergleichMitMinVKPReis == true) {
					vergleichspreis = getLagerFac().getMindestverkaufspreis((Integer) o[0], lagerIId, new BigDecimal(1),
							theClientDto);
				}

				if (vkPreislisteIId != null) {
					Integer artikelIId = (Integer) o[0];
					VkpreisfindungDto dto = getVkPreisfindungFac().verkaufspreisfindungStufe1(artikelIId,
							Helper.cutDate(new java.sql.Date(System.currentTimeMillis())), vkPreislisteIId,
							new VkpreisfindungDto(theClientDto.getLocUi()), mwstsatzId_0, new BigDecimal(1),
							theClientDto.getSMandantenwaehrung(), theClientDto);
					if (dto.getVkpStufe1() != null && dto.getVkpStufe1().nettopreis != null) {
						vkPreis = dto.getVkpStufe1().nettopreis;
					} else {
						vkPreis = new BigDecimal(0);
					}
				} else {
					VkPreisfindungEinzelverkaufspreisDto vkpfDto = getVkPreisfindungFac().getArtikeleinzelverkaufspreis(
							(Integer) o[0], new java.sql.Date(System.currentTimeMillis()),
							theClientDto.getSMandantenwaehrung(), theClientDto);
					if (vkpfDto != null && vkpfDto.getNVerkaufspreisbasis() != null) {
						vkPreis = vkpfDto.getNVerkaufspreisbasis();
					}
				}

				if (vergleichspreis == null) {
					vergleichspreis = new BigDecimal(0);
				}
				if (vkPreis.doubleValue() < vergleichspreis.doubleValue()) {
					Object[] zeile = new Object[REPORT_GESTPREISUNTERMINVK_ANZAHL_SPALTEN];
					zeile[REPORT_GESTPREISUNTERMINVK_ARTIKELNUMMER] = o[1];
					zeile[REPORT_GESTPREISUNTERMINVK_BEZEICHNUNG] = o[2];
					zeile[REPORT_GESTPREISUNTERMINVK_GESTPREIS] = vergleichspreis;
					zeile[REPORT_GESTPREISUNTERMINVK_VKPREIS] = vkPreis;
					zeile[REPORT_GESTPREISUNTERMINVK_MATERIALZUSCHLAG] = materialzuschlag;
					zeile[REPORT_GESTPREISUNTERMINVK_VERSTECKT] = new Boolean(bVersteckt);
					zeile[REPORT_GESTPREISUNTERMINVK_ARTIKELART] = artikelart;
					if (stkl != null) {
						zeile[REPORT_GESTPREISUNTERMINVK_STUECKLISTENART] = stkl.getStuecklisteart_c_nr();
					}

					zeile[REPORT_GESTPREISUNTERMINVK_VERSTECKT] = new Boolean(bVersteckt);
					zeile[REPORT_GESTPREISUNTERMINVK_LAGERSTAND] = lagerstand;
					zeile[REPORT_GESTPREISUNTERMINVK_ARTIKELREFERENZNUMMER] = referenznr;

					alDaten.add(zeile);
				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}

		Object[][] returnArray = new Object[alDaten.size()][6];
		data = (Object[][]) alDaten.toArray(returnArray);

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_MITVERSTECKTEN", new Boolean(bMitVersteckten));
		parameter.put("P_MITSTUECKLISTEN", new Boolean(bMitStuecklisten));

		if (bVergleichMitMinVKPReis == true) {
			parameter.put("P_AUSGEHENDVON",
					getTextRespectUISpr("lp.mindestvkpreis", theClientDto.getMandant(), theClientDto.getLocUi()));
		} else {
			parameter.put("P_AUSGEHENDVON",
					getTextRespectUISpr("lp.gestehungspreis", theClientDto.getMandant(), theClientDto.getLocUi()));
		}

		try {
			LagerDto dto = getLagerFac().lagerFindByPrimaryKey(lagerIId);
			parameter.put("P_LAGER", dto.getCNr());

			if (vkPreislisteIId != null) {
				parameter.put("P_PREISLISTE",
						getVkPreisfindungFac().vkpfartikelpreislisteFindByPrimaryKey(vkPreislisteIId).getCNr());
			} else {
				parameter.put("P_PREISLISTE", "Verkaufspreisbasis");
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		initJRDS(parameter, LagerReportFac.REPORT_MODUL, LagerReportFac.REPORT_GESTPREISUEBERMINVK,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printZaehlliste(Integer lagerIId, BigDecimal abLageragerwert, java.math.BigDecimal abGestpreis,
			Boolean bNurLagerbewirtschafteteArtikel, Boolean bNurArtikelMitLagerstand, int iSortierung,
			boolean bMitVersteckten, Integer artikelgruppeIId, Integer artikelklasseIId, String lagerplatzVon,
			String lagerplatzBis, int iArtikelarten, String artikelNrVon, String artikelNrBis,
			boolean bNurLagerbewerteteArtikel, TheClientDto theClientDto) {
		if (bNurLagerbewirtschafteteArtikel == null || bNurArtikelMitLagerstand == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"bNurLagerbewirtschafteteArtikel==null || bNurArtikelMitLagerstand==null || bSortiereNachLagerort == null"));
		}

		LagerDto[] lagerDtos = null;
		try {
			if (lagerIId == null) {

				lagerDtos = getLagerFac().lagerFindByMandantCNr(theClientDto.getMandant());

			} else {
				lagerDtos = new LagerDto[1];
				lagerDtos[0] = getLagerFac().lagerFindByPrimaryKey(lagerIId);
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		JasperPrintLP print = null;
		for (int i = 0; i < lagerDtos.length; i++) {

			Session session = FLRSessionFactory.getFactory().openSession();

			String mandantArtikel = theClientDto.getMandant();
			String mandantLager = theClientDto.getMandant();
			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
					theClientDto)) {

				mandantArtikel = getSystemFac().getHauptmandant();

				if (!getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER,
						theClientDto)) {
					mandantLager = getSystemFac().getHauptmandant();
				}

			}

			session.enableFilter("filterMandant").setParameter("paramMandant", mandantArtikel);
			session.enableFilter("filterLocale").setParameter("paramLocale",
					Helper.locale2String(theClientDto.getLocUi()));

			String queryString = "SELECT a.i_id, a.c_nr, l.c_nr,"
					+ "(SELECT al.n_lagerstand FROM FLRArtikellager al WHERE compId.artikel_i_id=a.i_id AND compId.lager_i_id=l.i_id ) as lagerstand,"
					+ "(SELECT al.n_gestehungspreis FROM FLRArtikellager al WHERE compId.artikel_i_id=a.i_id AND compId.lager_i_id=l.i_id ) as gestpreis,"
					+ "1 as lagerplatz, "
					+ "aspr.c_bez, aspr.c_zbez,aspr.c_zbez2,a.einheit_c_nr, a.b_lagerbewirtschaftet, a.b_seriennrtragend, a.b_chargennrtragend,l.i_id, (SELECT stkl FROM FLRStueckliste stkl WHERE stkl.artikel_i_id=a.i_id AND stkl.mandant_c_nr=a.mandant_c_nr), "
					+ "a.c_referenznr FROM FLRArtikelliste a, FLRLager as l LEFT OUTER JOIN a.artikelsprset AS aspr ";

			if (lagerplatzVon != null || lagerplatzBis != null) {
				queryString += " LEFT OUTER JOIN a.artikellagerplatzset AS lagerplatz ";
			}

			queryString += "WHERE a.mandant_c_nr='" + mandantArtikel + "' AND l.mandant_c_nr='" + mandantLager
					+ "' AND a.artikelart_c_nr NOT IN ('" + ArtikelFac.ARTIKELART_HANDARTIKEL + "') AND l.i_id="
					+ lagerDtos[i].getIId() + " ";

			if (bNurLagerbewirtschafteteArtikel == true) {
				queryString += "AND a.b_lagerbewirtschaftet >0 ";
			}
			if (bNurLagerbewerteteArtikel == true) {
				queryString += "AND a.b_lagerbewertet >0 ";
			}
			if (bMitVersteckten == false) {
				queryString += "AND a.b_versteckt <> 1 ";
			}
			if (lagerIId != null) {
				queryString += "AND l.i_id=" + lagerIId;
			}
			if (artikelklasseIId != null) {
				queryString += "AND a.flrartikelklasse.i_id=" + artikelklasseIId + " ";
			}
			if (artikelgruppeIId != null) {
				queryString += "AND a.flrartikelgruppe.i_id=" + artikelgruppeIId + " ";
			}
			if (lagerplatzVon != null || lagerplatzBis != null) {

				if (lagerplatzVon != null) {
					queryString += " AND lagerplatz.flrlagerplatz.c_lagerplatz >='" + lagerplatzVon + "' ";
				}

				if (lagerplatzBis != null) {

					String lagerplatzBis_Gefuellt = Helper.fitString2Length(lagerplatzBis, 25, '_');
					queryString += " AND lagerplatz.flrlagerplatz.c_lagerplatz <='" + lagerplatzBis_Gefuellt + "' ";

				}

			}

			if (artikelNrVon != null) {
				queryString += " AND a.c_nr >='" + artikelNrVon + "'";
			}
			if (artikelNrBis != null) {

				String artikelNrBis_Gefuellt = Helper.fitString2Length(artikelNrBis, 25, '_');
				queryString += " AND a.c_nr <='" + artikelNrBis_Gefuellt + "'";
			}

			HashMap<String, Object> parameter = new HashMap<String, Object>();

			if (iSortierung == LagerReportFac.REPORT_ZAEHLLISTE_SORTIERUNG_ARTIKEL) {
				queryString += "ORDER BY l.c_nr ASC ,a.c_nr ASC";
				parameter.put("P_SORTIERUNG", "Artikel");
			} else if (iSortierung == LagerReportFac.REPORT_ZAEHLLISTE_SORTIERUNG_REFERENZNUMMER) {
				queryString += "ORDER BY l.c_nr ASC ,a.c_referenznr ASC";
				parameter.put("P_SORTIERUNG", "Referenznummer");
			} else if (iSortierung == LagerReportFac.REPORT_ZAEHLLISTE_SORTIERUNG_LAGERPLATZ) {
				parameter.put("P_SORTIERUNG", "Lagerplatz");
			}

			org.hibernate.Query query = session.createQuery(queryString);
			List<?> resultList = query.list();

			Iterator<?> resultListIterator = resultList.iterator();
			List<Object[]> list = new ArrayList<Object[]>();
			int row = 0;
			while (resultListIterator.hasNext()) {
				Object[] artikel = (Object[]) resultListIterator.next();

				boolean bStueckliste = false;

				if (artikel[14] != null) {
					bStueckliste = true;
				}

				boolean bGehoertZuArtikelart = true;

				if (iArtikelarten == LagerReportFac.REPORT_LAGERSTANDSLISTE_ARTIKEL_NURSTKL && bStueckliste == false) {
					bGehoertZuArtikelart = false;
				}
				if (iArtikelarten == LagerReportFac.REPORT_LAGERSTANDSLISTE_ARTIKEL_OHNESTKL && bStueckliste == true) {
					bGehoertZuArtikelart = false;
				}

				if (bGehoertZuArtikelart == false) {
					continue;
				}

				// Wenn Gestehungspreis mit angegeben
				if (abGestpreis != null) {
					if (artikel[4] == null || abGestpreis.doubleValue() > ((BigDecimal) artikel[4]).doubleValue()) {
						continue;
					}
				}

				// Wenn Wert angegeben wurde
				if (abLageragerwert != null) {
					if (artikel[4] != null && artikel[3] != null) {
						BigDecimal lagerWert = ((BigDecimal) artikel[4]).multiply((BigDecimal) artikel[3]);
						if (abLageragerwert.doubleValue() > lagerWert.doubleValue()) {
							continue;
						}
					} else {
						continue;
					}
				}

				Object[] zeile = new Object[REPORT_ZAEHLLISTE_FELDANZAHL];
				zeile[10] = artikel[0];
				if (HelperReport.pruefeObCode39Konform((String) artikel[1])) {
					zeile[REPORT_ZAEHLLISTE_BARCODE_DRUCKBAR] = Boolean.TRUE;
				} else {
					zeile[REPORT_ZAEHLLISTE_BARCODE_DRUCKBAR] = Boolean.FALSE;
				}

				zeile[REPORT_ZAEHLLISTE_ARTIKELNUMMER] = (String) artikel[1];
				zeile[REPORT_ZAEHLLISTE_REFERENZNUMMER] = (String) artikel[15];

				zeile[REPORT_ZAEHLLISTE_ARTIKELBEZEICHNUNG] = artikel[6];
				zeile[REPORT_ZAEHLLISTE_ARTIELZUSATZBEZEICHNUNG] = artikel[7];
				zeile[REPORT_ZAEHLLISTE_ARTIELZUSATZBEZEICHNUNG2] = artikel[8];

				zeile[REPORT_ZAEHLLISTE_LAGERSTAND] = artikel[3];

				boolean bSnrTagend = Helper.short2boolean((Short) artikel[11]);
				boolean bChnrTagend = Helper.short2boolean((Short) artikel[12]);

				zeile[REPORT_ZAEHLLISTE_SNRTRAGEND] = new Boolean(bSnrTagend);

				zeile[REPORT_ZAEHLLISTE_CHNRTRAGEND] = new Boolean(bChnrTagend);

				zeile[REPORT_ZAEHLLISTE_MENGENART] = artikel[9];

				try {
					zeile[REPORT_ZAEHLLISTE_LAGERORT] = getLagerFac().getLagerplaezteEinesArtikels((Integer) artikel[0],
							lagerIId);
				} catch (RemoteException e1) {
					throwEJBExceptionLPRespectOld(e1);
				}

				if (bChnrTagend || bSnrTagend) {

					SeriennrChargennrAufLagerDto[] dtos = null;
					try {
						dtos = getLagerFac().getAllSerienChargennrAufLager((Integer) artikel[0], (Integer) artikel[13],
								theClientDto, false, false);
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

					String snrchnr = "";

					for (int k = 0; k < dtos.length; k++) {
						SeriennrChargennrAufLagerDto dto = dtos[k];

						if (bSnrTagend) {
							snrchnr += dto.getCSeriennrChargennr() + ", ";
						} else {
							snrchnr += dto.getCSeriennrChargennr() + " "
									+ Helper.formatZahl(dto.getNMenge(), 2, theClientDto.getLocUi()) + "\n";

						}

					}

					zeile[REPORT_ZAEHLLISTE_SERIENNRCHARGENNR] = snrchnr;
				}

				if (bNurArtikelMitLagerstand.booleanValue() == true) {
					if (artikel[3] == null || ((BigDecimal) artikel[3]).doubleValue() <= 0) {
						continue;

					}
				}

				ArtikelDto artDto = getArtikelFac().artikelFindByPrimaryKey((Integer) artikel[0], theClientDto);

				zeile[REPORT_ZAEHLLISTE_VERPACKUNGMENGE] = artDto.getFVerpackungsmenge();

				if (artDto.getVerpackungDto() != null) {
					zeile[REPORT_ZAEHLLISTE_ARTIKELBAUFORM] = artDto.getVerpackungDto().getCBauform();
					zeile[REPORT_ZAEHLLISTE_VERPACKUNGSART] = artDto.getVerpackungDto().getCVerpackungsart();
				} else {
					zeile[REPORT_ZAEHLLISTE_ARTIKELBAUFORM] = "";
					zeile[REPORT_ZAEHLLISTE_VERPACKUNGSART] = "";
				}
				if (artDto.getGeometrieDto() != null) {
					zeile[REPORT_ZAEHLLISTE_BREITETEXT] = artDto.getGeometrieDto().getCBreitetext();
					zeile[REPORT_ZAEHLLISTE_BREITE] = artDto.getGeometrieDto().getFBreite();
					zeile[REPORT_ZAEHLLISTE_HOEHE] = artDto.getGeometrieDto().getFHoehe();
					zeile[REPORT_ZAEHLLISTE_TIEFE] = artDto.getGeometrieDto().getFTiefe();
				} else {
					zeile[REPORT_ZAEHLLISTE_BREITETEXT] = "";
					zeile[REPORT_ZAEHLLISTE_BREITE] = null;
					zeile[REPORT_ZAEHLLISTE_HOEHE] = null;
					zeile[REPORT_ZAEHLLISTE_TIEFE] = null;
				}

				list.add(zeile);
				row++;
			}
			session.close();

			// Wenn sortiert nach Lagerort
			if (iSortierung == LagerReportFac.REPORT_ZAEHLLISTE_SORTIERUNG_LAGERPLATZ) {
				for (int m = list.size() - 1; m > 0; --m) {
					for (int n = 0; n < m; ++n) {
						Object[] o1 = (Object[]) list.get(n);
						Object[] o2 = (Object[]) list.get(n + 1);

						String ort1 = (String) o1[REPORT_ZAEHLLISTE_LAGERORT];
						String ort2 = (String) o2[REPORT_ZAEHLLISTE_LAGERORT];

						if (ort1 == null) {
							ort1 = "";
						}
						if (ort2 == null) {
							ort2 = "";
						}
						if (ort1.compareTo(ort2) > 0) {
							list.set(n, o2);
							list.set(n + 1, o1);
						}
					}
				}

			}

			Object[][] returnArray = new Object[row][REPORT_ZAEHLLISTE_FELDANZAHL];
			data = (Object[][]) list.toArray(returnArray);

			index = -1;
			sAktuellerReport = LagerFac.REPORT_ZAEHLLISTE;

			parameter.put("P_LAGER", lagerDtos[i].getCNr());

			parameter.put("P_NURLAGERBEWERTETE_ARTIKEL", bNurLagerbewerteteArtikel);
			parameter.put("P_NURLAGERBEWIRTSCHAFTETE_ARTIKEL", bNurLagerbewirtschafteteArtikel);
			parameter.put("P_NURARTIKELMITLAGERSTAND", bNurArtikelMitLagerstand);

			parameter.put("P_ARTIKELNRVON", artikelNrVon);
			parameter.put("P_ARTIKELNRBIS", artikelNrBis);

			parameter.put("P_LAGERPLATZ_VON", lagerplatzVon);
			parameter.put("P_LAGERPLATZ_BIS", lagerplatzBis);

			initJRDS(parameter, LagerFac.REPORT_MODUL, LagerFac.REPORT_ZAEHLLISTE, theClientDto.getMandant(),
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
	public JasperPrintLP printLagerstandliste(Integer lagerIId, java.sql.Timestamp tStichtag, Boolean bMitAZArtikel,
			Boolean bNurLagerbewertete, String artikelNrVon, String artikelNrBis, Integer artikelgruppeIId,
			Integer artikelklasseIId, Integer vkPreislisteIId, int sortierung, int iArtikelarten,
			boolean bMitAbgewertetemGestpreis, boolean bMitArtikelOhneLagerstand, Integer lagerplatzIId,
			boolean bMitVersteckten, Integer shopgruppeIId, boolean bMitNichtLagerbewirtschaftetenArtikeln,
			boolean bDetailliert, boolean bLagerstandMitBelegen, final TheClientDto theClientDto) {

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		parameter.put("P_MITABGEWERTETEMGESTPREIS", new Boolean(bMitAbgewertetemGestpreis));
		parameter.put("P_MITNICHTLAGERBEWIRTSCHAFTETEN", new Boolean(bMitNichtLagerbewirtschaftetenArtikeln));

		parameter.put("P_MITVERSTECKTEN", new Boolean(bMitVersteckten));
		parameter.put("P_LAGERDETAILLIERT", new Boolean(bDetailliert));

		parameter.put("P_MIT_BELEGEN", new Boolean(bLagerstandMitBelegen));

		if (tStichtag != null) {
			parameter.put("P_STICHTAG", tStichtag);
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(tStichtag.getTime());
			c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);
			tStichtag = new java.sql.Timestamp(c.getTimeInMillis());
			tStichtag = Helper.cutTimestamp(tStichtag);
		}

		int iMonateGestpreisAbwerten = 6;
		double dProzentGestpreisAbwerten = 10;
		boolean bEKPreisWennGestpreisNull = true;
		try {
			ParametermandantDto mandantparameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_GESTEHUNGSPREISABWERTEN_AB_MONATE);

			iMonateGestpreisAbwerten = (Integer) mandantparameter.getCWertAsObject();

			mandantparameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_GESTEHUNGSPREISABWERTEN_PROZENT_PRO_MONAT);

			dProzentGestpreisAbwerten = (Double) mandantparameter.getCWertAsObject();

			mandantparameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_LAGERSTANDSLISTE_EKPREIS_WENN_GESTPREIS_NULL);
			bEKPreisWennGestpreisNull = ((Boolean) mandantparameter.getCWertAsObject()).booleanValue();

		}

		catch (RemoteException ex8) {
			throwEJBExceptionLPRespectOld(ex8);
		}

		Session session = FLRSessionFactory.getFactory().openSession();

		session.enableFilter("filterMandant").setParameter("paramMandant", theClientDto.getMandant());
		session.enableFilter("filterLocale").setParameter("paramLocale", Helper.locale2String(theClientDto.getLocUi()));

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

		String queryString = null;

		if (lagerIId != null) {

			queryString = "SELECT artikelliste.i_id, artikelliste.c_nr, aspr.c_bez, (SELECT sum(artikellager.n_lagerstand) FROM FLRArtikellager AS artikellager WHERE artikellager.compId.artikel_i_id=artikelliste.i_id AND artikellager.compId.lager_i_id="
					+ lagerIId
					+ " ) ,(SELECT sum(artikellager.n_gestehungspreis*artikellager.n_lagerstand) FROM FLRArtikellager AS artikellager WHERE artikellager.compId.artikel_i_id=artikelliste.i_id AND artikellager.compId.lager_i_id="
					+ lagerIId
					+ "), artikelliste.b_lagerbewirtschaftet, artikelliste.b_seriennrtragend, artikelliste.b_chargennrtragend , artikelliste.einheit_c_nr, gruppe.c_nr, klasse.c_nr, (SELECT stkl FROM FLRStueckliste stkl WHERE stkl.artikel_i_id=artikelliste.i_id AND stkl.mandant_c_nr=artikelliste.mandant_c_nr),artikelliste.artikelart_c_nr, artikelliste.b_versteckt, "
					+ " artikelliste.einheit_c_nr_bestellung,artikelliste.n_umrechnugsfaktor,aspr.c_kbez,aspr.c_zbez,aspr.c_zbez2,artikelliste.c_referenznr, shopgruppe.c_nr, artikelliste.f_verpackungsmenge";
			if (bDetailliert) {
				queryString += ", artikellagerset.n_lagerstand, artikellagerset.n_gestehungspreis, artikellagerset.flrlager.i_id, artikellagerset.flrlager.c_nr, artikellagerset.flrlager.lagerart_c_nr, artikellagerset.flrlager.b_konsignationslager";
			}
			queryString += " FROM FLRArtikelliste AS artikelliste"
					+ " LEFT OUTER JOIN artikelliste.artikelsprset AS aspr "
					+ " LEFT OUTER JOIN artikelliste.flrartikelgruppe AS gruppe "
					+ " LEFT OUTER JOIN artikelliste.flrartikelklasse AS klasse "
					+ " LEFT OUTER JOIN artikelliste.flrshopgruppe AS shopgruppe "
					+ " LEFT OUTER JOIN artikelliste.artikelshopgruppeset AS webshopgruppen ";

		} else {

			String filterWennZentralerArtikelstamm = "";
			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
					theClientDto)) {
				filterWennZentralerArtikelstamm = " AND artikellager.flrlager.mandant_c_nr='"
						+ theClientDto.getMandant() + "'";
			}

			queryString = "SELECT artikelliste.i_id, artikelliste.c_nr, aspr.c_bez,(SELECT sum(artikellager.n_lagerstand) FROM FLRArtikellager AS artikellager WHERE artikellager.compId.artikel_i_id=artikelliste.i_id "
					+ filterWennZentralerArtikelstamm + "  AND artikellager.flrlager.lagerart_c_nr NOT IN('"
					+ LagerFac.LAGERART_WERTGUTSCHRIFT
					+ "')) ,(SELECT sum(artikellager.n_gestehungspreis*artikellager.n_lagerstand) FROM FLRArtikellager AS artikellager WHERE artikellager.compId.artikel_i_id=artikelliste.i_id  AND artikellager.flrlager.lagerart_c_nr NOT IN('"
					+ LagerFac.LAGERART_WERTGUTSCHRIFT
					+ "')), artikelliste.b_lagerbewirtschaftet, artikelliste.b_seriennrtragend, artikelliste.b_chargennrtragend , artikelliste.einheit_c_nr, gruppe.c_nr, klasse.c_nr, (SELECT stkl FROM FLRStueckliste stkl WHERE stkl.artikel_i_id=artikelliste.i_id AND stkl.mandant_c_nr=artikelliste.mandant_c_nr),artikelliste.artikelart_c_nr, artikelliste.b_versteckt, "
					+ " artikelliste.einheit_c_nr_bestellung,artikelliste.n_umrechnugsfaktor,aspr.c_kbez,aspr.c_zbez,aspr.c_zbez2,artikelliste.c_referenznr, shopgruppe.c_nr, artikelliste.f_verpackungsmenge";
			if (bDetailliert) {
				queryString += ", artikellagerset.n_lagerstand, artikellagerset.n_gestehungspreis, artikellagerset.flrlager.i_id, artikellagerset.flrlager.c_nr, artikellagerset.flrlager.lagerart_c_nr, artikellagerset.flrlager.b_konsignationslager";
			}
			queryString += " FROM FLRArtikelliste AS artikelliste"
					+ " LEFT OUTER JOIN artikelliste.artikelsprset AS aspr "
					+ " LEFT OUTER JOIN artikelliste.flrartikelgruppe AS gruppe "
					+ " LEFT OUTER JOIN artikelliste.flrartikelklasse AS klasse "
					+ " LEFT OUTER JOIN artikelliste.flrshopgruppe AS shopgruppe "
					+ " LEFT OUTER JOIN artikelliste.artikelshopgruppeset AS webshopgruppen ";
		}

		if (lagerplatzIId != null) {
			queryString += " LEFT OUTER JOIN artikelliste.artikellagerplatzset AS lagerplatz ";
		}
		if (bDetailliert) {
			queryString += " LEFT OUTER JOIN artikelliste.artikellagerset AS artikellagerset ";
		}

		String mandantCNr = theClientDto.getMandant();

		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			mandantCNr = getSystemFac().getHauptmandant();
		}

		if (bMitAZArtikel.booleanValue() == true) {

			queryString = queryString + " WHERE artikelliste.mandant_c_nr='" + mandantCNr
					+ "' AND artikelliste.artikelart_c_nr not in('" + ArtikelFac.ARTIKELART_HANDARTIKEL + "') ";

		} else {
			queryString = queryString + " WHERE artikelliste.mandant_c_nr='" + mandantCNr
					+ "' AND artikelliste.artikelart_c_nr not in('" + ArtikelFac.ARTIKELART_HANDARTIKEL + "','"
					+ ArtikelFac.ARTIKELART_ARBEITSZEIT + "') ";

		}

		if (bMitNichtLagerbewirtschaftetenArtikeln == false) {
			queryString += " AND artikelliste.b_lagerbewirtschaftet = 1 ";
		}

		if (bMitVersteckten == false) {
			queryString += " AND artikelliste.b_versteckt = 0 ";
		}

		if (bNurLagerbewertete.booleanValue() == true) {

			queryString = queryString + " AND artikelliste." + ArtikelFac.FLR_ARTIKEL_B_LAGERBEWERTET + "=1 ";
		}

		if (artikelNrVon != null) {
			queryString += " AND artikelliste.c_nr >='" + artikelNrVon + "'";
		}
		if (artikelNrBis != null) {

			String artikelNrBis_Gefuellt = Helper.fitString2Length(artikelNrBis, 25, '_');
			queryString += " AND artikelliste.c_nr <='" + artikelNrBis_Gefuellt + "'";
		}
		if (artikelklasseIId != null) {
			queryString += " AND klasse.i_id IN "
					+ getArtikelFac().holeAlleArtikelklassen(artikelklasseIId).erzeuge_IN_Fuer_Query();
		}
		if (artikelgruppeIId != null) {
			queryString += " AND gruppe.i_id IN "
					+ getArtikelFac().holeAlleArtikelgruppen(artikelgruppeIId).erzeuge_IN_Fuer_Query();
		}
		if (shopgruppeIId != null) {
			String in = getArtikelFac().holeAlleShopgruppen(shopgruppeIId).erzeuge_IN_Fuer_Query();

			queryString += " AND( shopgruppe.i_id  IN " + in + " OR webshopgruppen.shopgruppe_i_id IN " + in + "  ) ";

			parameter.put("P_SHOPGRUPPE",
					getArtikelFac().shopgruppeFindByPrimaryKey(shopgruppeIId, theClientDto).getBezeichnung());
		}

		if (lagerplatzIId != null) {
			queryString += " AND lagerplatz.lagerplatz_i_id=" + lagerplatzIId;
		}

		queryString = queryString
				+ " GROUP BY artikelliste.i_id,artikelliste.c_nr, aspr.c_bez, artikelliste.b_lagerbewirtschaftet, artikelliste.b_seriennrtragend, artikelliste.b_chargennrtragend, artikelliste.einheit_c_nr , gruppe.c_nr, klasse.c_nr, shopgruppe.c_nr,artikelliste.b_lagerbewirtschaftet, artikelliste.mandant_c_nr, artikelliste.artikelart_c_nr, artikelliste.b_versteckt,artikelliste.einheit_c_nr_bestellung,artikelliste.n_umrechnugsfaktor,aspr.c_kbez,aspr.c_zbez,aspr.c_zbez2,artikelliste.c_referenznr, artikelliste.f_verpackungsmenge"
				+ (bDetailliert
						? ", artikellagerset.n_lagerstand, artikellagerset.n_gestehungspreis, artikellagerset.flrlager.i_id, artikellagerset.flrlager.c_nr, artikellagerset.flrlager.lagerart_c_nr, artikellagerset.flrlager.b_konsignationslager "
						: " ");

		if (sortierung == LagerReportFac.REPORT_LAGERSTANDSLISTE_SORTIERUNG_ARTIKELNUMMER) {
			queryString = queryString + " ORDER BY artikelliste.c_nr ";
			parameter.put("P_SORTIERUNG", getTextRespectUISpr("artikel.artikelnummerlang", theClientDto.getMandant(),
					theClientDto.getLocUi()));

		} else if (sortierung == LagerReportFac.REPORT_LAGERSTANDSLISTE_SORTIERUNG_ARTIKELKLASSE) {
			queryString = queryString + " ORDER BY klasse.c_nr,artikelliste.c_nr ";
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.artikelklasse", theClientDto.getMandant(), theClientDto.getLocUi()));

		} else if (sortierung == LagerReportFac.REPORT_LAGERSTANDSLISTE_SORTIERUNG_ARTIKELGRUPPE) {
			queryString = queryString + " ORDER BY gruppe.c_nr,artikelliste.c_nr ";
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.artikelgruppe", theClientDto.getMandant(), theClientDto.getLocUi()));

		} else if (sortierung == LagerReportFac.REPORT_LAGERSTANDSLISTE_SORTIERUNG_SHOPGRUPPE) {
			queryString = queryString + " ORDER BY shopgruppe.c_nr,artikelliste.c_nr ";
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.shopgruppe", theClientDto.getMandant(), theClientDto.getLocUi()));

		} else if (sortierung == LagerReportFac.REPORT_LAGERSTANDSLISTE_SORTIERUNG_REFERENZNUMMER) {
			queryString = queryString + " ORDER BY artikelliste.c_referenznr ";
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.referenznummer", theClientDto.getMandant(), theClientDto.getLocUi()));

		}
		// Wenn sortier nach Lagerwert oder Gestpreis, dann wird erst im
		// nachhinein sortiert

		Query query = session.createQuery(queryString);
		List<?> resultList = query.list();

		final Timestamp stichtag = tStichtag;
		HvCreatingCachingProvider<Integer, HashMap<Integer, BigDecimal>> lagerstaendeMap = new HvCreatingCachingProvider<Integer, HashMap<Integer, BigDecimal>>() {
			@Override
			protected HashMap<Integer, BigDecimal> provideValue(Integer key, Integer transformedKey) {
				if (stichtag == null) {
					return null;
				}
				return getLagerFac().getLagerstandAllerArtikelZumZeitpunkt(key, stichtag, theClientDto);
			}
		};

		Iterator<?> resultListIterator = resultList.iterator();
		List<Object[]> dataHelpList = new ArrayList<Object[]>();
		while (resultListIterator.hasNext()) {
			Object[] dataHelp = new Object[REPORT_LAGERSTANDLISTE_ANZAHL_SPALTEN];
			Object o[] = (Object[]) resultListIterator.next();

			// Ist Artikel Stueckliste?
			boolean bStueckliste = false;

			if (o[11] != null) {
				bStueckliste = true;
				com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste s = (com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste) o[11];
				dataHelp[REPORT_LAGERSTANDLISTE_STUECKLISTE] = Boolean.TRUE;

				if (s.getStuecklisteart_c_nr()
						.equals(com.lp.server.stueckliste.service.StuecklisteFac.STUECKLISTEART_HILFSSTUECKLISTE)) {
					dataHelp[REPORT_LAGERSTANDLISTE_HILFSSTUECKLISTE] = Boolean.TRUE;
				} else {
					dataHelp[REPORT_LAGERSTANDLISTE_HILFSSTUECKLISTE] = Boolean.FALSE;
				}
			} else {
				dataHelp[REPORT_LAGERSTANDLISTE_STUECKLISTE] = Boolean.FALSE;
				dataHelp[REPORT_LAGERSTANDLISTE_HILFSSTUECKLISTE] = Boolean.FALSE;
			}

			boolean bGehoertZuArtikelart = true;

			if (iArtikelarten == LagerReportFac.REPORT_LAGERSTANDSLISTE_ARTIKEL_NURSTKL && bStueckliste == false) {
				bGehoertZuArtikelart = false;
			}
			if (iArtikelarten == LagerReportFac.REPORT_LAGERSTANDSLISTE_ARTIKEL_OHNESTKL && bStueckliste == true) {
				bGehoertZuArtikelart = false;
			}

			// Alle Artikel mit Lagerstand groesser 0 kommen in die
			// Lagerstandsliste
			if (!bGehoertZuArtikelart) {
				continue;
			}

			Integer artikelIId = (Integer) o[0];

			dataHelp[REPORT_LAGERSTANDLISTE_IDENTNUMMER] = o[1];
			dataHelp[REPORT_LAGERSTANDLISTE_EINHEIT] = o[8];
			dataHelp[REPORT_LAGERSTANDLISTE_BEZEICHNUNG] = o[2];

			dataHelp[REPORT_LAGERSTANDLISTE_KURZBEZEICHNUNG] = o[16];
			dataHelp[REPORT_LAGERSTANDLISTE_ZUSATZBEZEICHNUNG] = o[17];
			dataHelp[REPORT_LAGERSTANDLISTE_ZUSATZBEZEICHNUNG2] = o[18];
			dataHelp[REPORT_LAGERSTANDLISTE_REFERENZNUMMER] = o[19];
			dataHelp[REPORT_LAGERSTANDLISTE_SHOPGRUPPE] = o[20];
			dataHelp[REPORT_LAGERSTANDLISTE_VERPACKUNGSMENGE] = o[21];

			dataHelp[REPORT_LAGERSTANDLISTE_BESTELLMENGENEINHEIT] = o[14];
			dataHelp[REPORT_LAGERSTANDLISTE_UMRECHNUNGFAKTOR] = o[15];

			dataHelp[REPORT_LAGERSTANDLISTE_ARTIKELART] = o[12];
			dataHelp[REPORT_LAGERSTANDLISTE_ARTIKELGRUPPE] = o[9];
			dataHelp[REPORT_LAGERSTANDLISTE_ARTIKELKLASSE] = o[10];

			dataHelp[REPORT_LAGERSTANDLISTE_VERSTECKT] = new Boolean(Helper.short2boolean(((Short) o[13])));

			try {
				dataHelp[REPORT_LAGERSTANDLISTE_LAGERORT] = getLagerFac().getLagerplaezteEinesArtikels((Integer) o[0],
						lagerIId);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			if (bDetailliert) {
				dataHelp[REPORT_LAGERSTANDLISTE_LAGERSTAND] = (BigDecimal) o[22];
				setLagerstandslisteLagerabhaengigeFieldsDetailliert(dataHelp, o, tStichtag, lagerstaendeMap,
						vkPreislisteIId, mwstsatzId_0, bMitAbgewertetemGestpreis, iMonateGestpreisAbwerten,
						dProzentGestpreisAbwerten, bEKPreisWennGestpreisNull, theClientDto);
			} else {
				setLagerstandslisteLagerabhaengigeFields(dataHelp, o, lagerIId, tStichtag, lagerstaendeMap,
						vkPreislisteIId, mwstsatzId_0, bMitAbgewertetemGestpreis, iMonateGestpreisAbwerten,
						dProzentGestpreisAbwerten, bEKPreisWennGestpreisNull, theClientDto);
			}

			if (bLagerstandMitBelegen) {

				Session sessionBELEGE = FLRSessionFactory.getFactory().openSession();
				String sBELEGE = "SELECT l, (SELECT SUM(u.n_verbrauchtemenge) FROM FLRLagerabgangursprung as u WHERE u.compId.i_lagerbewegungidursprung=l.i_id_buchung) as verbrauchtemenge  FROM FLRLagerbewegung l WHERE  l.artikel_i_id = "
						+ artikelIId;

				if (lagerIId != null) {
					sBELEGE += " AND l.lager_i_id=" + lagerIId;
				}

				sBELEGE += " AND l.b_abgang = 0 AND l.b_historie=0 AND l.b_vollstaendigverbraucht = 0 AND l.n_menge >0 ";

				org.hibernate.Query queryBELEGE = sessionBELEGE.createQuery(sBELEGE);
				List<?> resultsBELEGE = queryBELEGE.list();
				Iterator<?> resultListIteratorBELEGE = resultsBELEGE.iterator();

				String[] fieldnames = new String[] { "F_BELEGART", "F_BELEGNUMMER", "F_BELEGDATUM",
						"F_MENGE_AUF_LAGER", "F_SNRCHNR" };

				ArrayList alSub = new ArrayList();

				while (resultListIteratorBELEGE.hasNext()) {

					Object[] oZeile = (Object[]) resultListIteratorBELEGE.next();

					FLRLagerbewegung flrLagerbewegung = (FLRLagerbewegung) oZeile[0];

					BigDecimal bdVerbraucht = (BigDecimal) oZeile[1];
					
					if(bdVerbraucht==null) {
						bdVerbraucht=BigDecimal.ZERO;
					}else {
						int z=0;
					}
					

					BigDecimal bdAufLager = flrLagerbewegung.getN_menge().subtract(bdVerbraucht);

					try {
						BelegInfos bi = getLagerFac().getBelegInfos(flrLagerbewegung.getC_belegartnr(),
								flrLagerbewegung.getI_belegartid(), flrLagerbewegung.getI_belegartpositionid(),
								theClientDto);

						Object[] oZeileSub = new Object[fieldnames.length];

						oZeileSub[0] = bi.getBelegart();
						oZeileSub[1] = bi.getBelegnummer();
						oZeileSub[2] = bi.getBelegdatum();
						oZeileSub[3] = bdAufLager;
						oZeileSub[4] = flrLagerbewegung.getC_seriennrchargennr();

						alSub.add(oZeileSub);

					} catch (RemoteException ex) {
						throwEJBExceptionLPRespectOld(ex);
					}

				}

				dataHelp[REPORT_LAGERSTANDLISTE_SUBREPORT_BELEGE] = new LPDatenSubreport(alSub, fieldnames);
			}

			if (((BigDecimal) dataHelp[REPORT_LAGERSTANDLISTE_LAGERSTAND]).compareTo(BigDecimal.ZERO) > 0
					|| bMitArtikelOhneLagerstand == true) {
				dataHelpList.add(dataHelp);
			}

		}
		session.close();

		// data = new Object[row][9];
		// for (int i = 0; i < row; i++) {
		// data[i] = dataHelp[i];
		// }
		data = new Object[dataHelpList.size()][9];
		for (int i = 0; i < dataHelpList.size(); i++) {
			data[i] = dataHelpList.get(i);
		}

		if (sortierung == LagerReportFac.REPORT_LAGERSTANDSLISTE_SORTIERUNG_LAGERWERT) {

			for (int i = data.length - 1; i > 0; --i) {
				for (int j = 0; j < i; ++j) {
					Object[] o = data[j];
					Object[] o1 = data[j + 1];

					BigDecimal wert = (BigDecimal) o[REPORT_LAGERSTANDLISTE_LAGERWERT];
					BigDecimal wert1 = (BigDecimal) o1[REPORT_LAGERSTANDLISTE_LAGERWERT];
					if (wert.compareTo(wert1) < 0) {
						data[j] = o1;
						data[j + 1] = o;
					}
				}
			}

			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.lagerwert", theClientDto.getMandant(), theClientDto.getLocUi()));
		} else if (sortierung == LagerReportFac.REPORT_LAGERSTANDSLISTE_SORTIERUNG_LAGERORT) {

			for (int i = data.length - 1; i > 0; --i) {
				for (int j = 0; j < i; ++j) {
					Object[] o = data[j];
					Object[] o1 = data[j + 1];

					String wert = (String) o[REPORT_LAGERSTANDLISTE_LAGERORT];
					String wert1 = (String) o1[REPORT_LAGERSTANDLISTE_LAGERORT];
					if (wert.compareTo(wert1) > 0) {
						data[j] = o1;
						data[j + 1] = o;
					}
				}
			}

			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.lagerort", theClientDto.getMandant(), theClientDto.getLocUi()));
		} else if (sortierung == LagerReportFac.REPORT_LAGERSTANDSLISTE_SORTIERUNG_ARTIKELBEZEICHNUNG) {

			for (int i = data.length - 1; i > 0; --i) {
				for (int j = 0; j < i; ++j) {
					Object[] o = data[j];
					Object[] o1 = data[j + 1];

					String wert = (String) o[REPORT_LAGERSTANDLISTE_BEZEICHNUNG];
					String wert1 = (String) o1[REPORT_LAGERSTANDLISTE_BEZEICHNUNG];

					if (wert == null) {
						wert = "";
					}
					if (wert1 == null) {
						wert1 = "";
					}

					if (wert.toUpperCase().compareTo(wert1.toUpperCase()) > 0) {
						data[j] = o1;
						data[j + 1] = o;
					}
				}
			}

			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("bes.artikelbezeichnung", theClientDto.getMandant(), theClientDto.getLocUi()));
		} else if (sortierung == LagerReportFac.REPORT_LAGERSTANDSLISTE_SORTIERUNG_ARTIKELKURZBEZEICHNUNG) {

			for (int i = data.length - 1; i > 0; --i) {
				for (int j = 0; j < i; ++j) {
					Object[] o = data[j];
					Object[] o1 = data[j + 1];

					String wert = (String) o[REPORT_LAGERSTANDLISTE_KURZBEZEICHNUNG];
					String wert1 = (String) o1[REPORT_LAGERSTANDLISTE_KURZBEZEICHNUNG];

					if (wert == null) {
						wert = "";
					}
					if (wert1 == null) {
						wert1 = "";
					}

					if (wert.toUpperCase().compareTo(wert1.toUpperCase()) > 0) {
						data[j] = o1;
						data[j + 1] = o;
					}
				}
			}

			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.kurzbezeichnunglang", theClientDto.getMandant(), theClientDto.getLocUi()));
		} else if (sortierung == LagerReportFac.REPORT_LAGERSTANDSLISTE_SORTIERUNG_GESTEHUNGSPREIS) {
			for (int i = data.length - 1; i > 0; --i) {
				for (int j = 0; j < i; ++j) {
					Object[] o = data[j];
					Object[] o1 = data[j + 1];

					BigDecimal wert = (BigDecimal) o[REPORT_LAGERSTANDLISTE_GESTEHUNGSPREIS];
					BigDecimal wert1 = (BigDecimal) o1[REPORT_LAGERSTANDLISTE_GESTEHUNGSPREIS];
					if (wert.compareTo(wert1) < 0) {
						data[j] = o1;
						data[j + 1] = o;
					}
				}
			}
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.gestehungspreis", theClientDto.getMandant(), theClientDto.getLocUi()));
		}

		// Erstellung des Reports
		JasperPrintLP print = null;

		try {
			if (lagerIId != null) {
				LagerDto dto = getLagerFac().lagerFindByPrimaryKey(lagerIId);
				parameter.put("P_LAGER", dto.getCNr());
				if (dto.getPartnerIIdStandort() != null) {
					PartnerDto pDto = getPartnerFac().partnerFindByPrimaryKey(dto.getPartnerIIdStandort(),
							theClientDto);
					parameter.put("P_LAGER_STANDORT", pDto.getCKbez());
				}

			} else {
				parameter.put("P_LAGER", "ALLE");
			}

			if (lagerplatzIId != null) {
				LagerplatzDto lpDto = getLagerFac().lagerplatzFindByPrimaryKey(lagerplatzIId);
				parameter.put("P_LAGERPLATZ", lpDto.getCLagerplatz());
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		try {
			parameter.put("P_WAEHRUNG",
					getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto).getWaehrungCNr());

			if (artikelgruppeIId != null) {
				parameter.put("P_ARTIKELGRUPPE",
						getArtikelFac().artgruFindByPrimaryKey(artikelgruppeIId, theClientDto).getCNr());
			}
			if (artikelklasseIId != null) {
				parameter.put("P_ARTIKELKLASSE",
						getArtikelFac().artklaFindByPrimaryKey(artikelklasseIId, theClientDto).getCNr());
			}
			if (vkPreislisteIId != null) {
				VkpfartikelpreislisteDto preislisteDto = getVkPreisfindungFac()
						.vkpfartikelpreislisteFindByPrimaryKey(vkPreislisteIId);
				parameter.put("P_PREISLISTE", preislisteDto.getCNr());
				parameter.put("P_WAEHRUNG_PREISLISTE", preislisteDto.getWaehrungCNr());

				parameter.put("P_PREIS",
						getTextRespectUISpr("lp.verkaufspreis", theClientDto.getMandant(), theClientDto.getLocUi()));
			} else {
				parameter.put("P_PREIS",
						getTextRespectUISpr("lp.gestehungspreis", theClientDto.getMandant(), theClientDto.getLocUi()));
			}

		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

		parameter.put("P_ARTIKELNRVON", artikelNrVon);
		parameter.put("P_ARTIKELNRBIS", artikelNrBis);
		index = -1;
		sAktuellerReport = LagerReportFac.REPORT_LAGERSTANDLISTE;
		initJRDS(parameter, LagerReportFac.REPORT_MODUL, LagerReportFac.REPORT_LAGERSTANDLISTE,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		print = getReportPrint();

		PrintInfoDto values = new PrintInfoDto();
		values.setDocPath(new HeliumDocPath().add(new DocNodeLiteral(theClientDto.getMandant()))
				.add(new DocNodeFolder(DocNodeBase.BELEGART_ARTIKEL))
				.add(new DocNodeFolder(DocNodeBase.BELEGART_LAGERSTANDSLISTE)));
		// values[0] = JCRDocFac.HELIUMV_NODE + "/"
		// + LocaleFac.BELEGART_ARTIKEL.trim() + "/" + "Lagerstandsliste";
		values.setiId(theClientDto.getIDPersonal());
		values.setTable("");

		print.setOInfoForArchive(values);

		return print;
	}

	private void setLagerstandslisteLagerabhaengigeFields(Object[] dataHelp, Object[] o, Integer lagerIId,
			Timestamp tStichtag, HvCreatingCachingProvider<Integer, HashMap<Integer, BigDecimal>> lagerstaendeMap,
			Integer vkPreislisteIId, Integer mwstsatzId_0, boolean bMitAbgewertetemGestpreis,
			int iMonateGestpreisAbwerten, double dProzentGestpreisAbwerten, boolean bEKPreisWennGestpreisNull,
			TheClientDto theClientDto) {

		// Lagerstand holen
		BigDecimal lagerstand = null;
		if (Helper.short2boolean((Short) o[5]) == false) {
			lagerstand = new BigDecimal(99999999);
		} else {
			lagerstand = tStichtag != null ? lagerstaendeMap.getValueOfKey(lagerIId).get((Integer) o[0])
					: (BigDecimal) o[3];
		}

		dataHelp[REPORT_LAGERSTANDLISTE_LAGERSTAND] = lagerstand != null ? lagerstand : BigDecimal.ZERO;

		if (lagerIId != null) {
			Lager lager = em.find(Lager.class, lagerIId);
			dataHelp[REPORT_LAGERSTANDLISTE_LAGER] = lager.getCNr();
			dataHelp[REPORT_LAGERSTANDLISTE_LAGERART] = lager.getLagerartCNr();
			dataHelp[REPORT_LAGERSTANDLISTE_KONSIGNATIONSLAGER] = Helper.short2boolean(lager.getBKonsignationslager());
		}
		setLagerstandslisteLagerabhaengigeFields(dataHelp, o, lagerIId, tStichtag, (BigDecimal) o[3], (BigDecimal) o[4],
				vkPreislisteIId, mwstsatzId_0, bMitAbgewertetemGestpreis, iMonateGestpreisAbwerten,
				dProzentGestpreisAbwerten, bEKPreisWennGestpreisNull, theClientDto);
	}

	private void setLagerstandslisteLagerabhaengigeFieldsDetailliert(Object[] dataHelp, Object[] o, Timestamp tStichtag,
			HvCreatingCachingProvider<Integer, HashMap<Integer, BigDecimal>> lagerstaendeMap, Integer vkPreislisteIId,
			Integer mwstsatzId_0, boolean bMitAbgewertetemGestpreis, int iMonateGestpreisAbwerten,
			double dProzentGestpreisAbwerten, boolean bEKPreisWennGestpreisNull, TheClientDto theClientDto) {

		BigDecimal lagerstand = null;
		if (Helper.short2boolean((Short) o[5]) == false) {
			lagerstand = new BigDecimal(99999999);
		} else {
			lagerstand = tStichtag != null ? lagerstaendeMap.getValueOfKey((Integer) o[24]).get((Integer) o[0])
					: (BigDecimal) o[22];
		}

		dataHelp[REPORT_LAGERSTANDLISTE_LAGERSTAND] = lagerstand != null ? lagerstand : BigDecimal.ZERO;
		dataHelp[REPORT_LAGERSTANDLISTE_LAGER] = (String) o[25];
		dataHelp[REPORT_LAGERSTANDLISTE_LAGERART] = (String) o[26];
		dataHelp[REPORT_LAGERSTANDLISTE_KONSIGNATIONSLAGER] = Helper.short2boolean((Short) o[27]);
		BigDecimal lagerwert = o[23] != null && lagerstand != null ? ((BigDecimal) o[23]).multiply(lagerstand) : null;

		setLagerstandslisteLagerabhaengigeFields(dataHelp, o, (Integer) o[24], tStichtag, (BigDecimal) o[22], lagerwert,
				vkPreislisteIId, mwstsatzId_0, bMitAbgewertetemGestpreis, iMonateGestpreisAbwerten,
				dProzentGestpreisAbwerten, bEKPreisWennGestpreisNull, theClientDto);
	}

	private void setLagerstandslisteLagerabhaengigeFields(Object[] dataHelp, Object[] o, Integer lagerIId,
			Timestamp tStichtag, BigDecimal sumLagerstand, BigDecimal lagerwert, Integer vkPreislisteIId,
			Integer mwstsatzId_0, boolean bMitAbgewertetemGestpreis, int iMonateGestpreisAbwerten,
			double dProzentGestpreisAbwerten, boolean bEKPreisWennGestpreisNull, TheClientDto theClientDto) {

		BigDecimal lagerstand = (BigDecimal) dataHelp[REPORT_LAGERSTANDLISTE_LAGERSTAND];
		// Wenn SNR oder CHNR-Behaftet, dann kommen noch die
		// Lagernden
		// SNRS auf die Liste
		if (Helper.short2boolean(((Short) o[6])) || Helper.short2boolean(((Short) o[7]))) {
			try {
				SeriennrChargennrAufLagerDto[] dtos = getLagerFac()
						.getAllSerienChargennrAufLagerInfoDtos((Integer) o[0], lagerIId, true, tStichtag, theClientDto);

				String[] fieldnames = new String[] { "F_SNRCHNR", "F_VERSION", "F_MENGE", "F_ARTIKELSNRCHNR_I_ID" };

				Object[][] dataSub = new Object[dtos.length][fieldnames.length];

				String s = "";
				for (int i = 0; i < dtos.length; i++) {
					if (Helper.short2boolean(dtos[i].getBSeriennr()) == true) {
						s = s + dtos[i].getCSeriennrChargennr();

						if (dtos[i].getCVersion() != null && dtos[i].getCVersion().length() > 0) {
							s += ":" + dtos[i].getCVersion();
						}

						s += ", ";
					} else {
						s = s + dtos[i].getCSeriennrChargennr() + " "
								+ Helper.formatZahl(dtos[i].getNMenge(), 2, theClientDto.getLocUi()) + " "
								+ (String) o[8] + "\n";
					}

					dataSub[i][0] = dtos[i].getCSeriennrChargennr();

					if (dtos[i].getCVersion() != null && dtos[i].getCVersion().length() > 0) {
						dataSub[i][1] = dtos[i].getCVersion();
					}
					dataSub[i][2] = dtos[i].getNMenge();
					dataSub[i][3] = getLagerFac().artikelsnrchnrIIdFindByArtikelIIdCSeriennrchargennr((Integer) o[0],
							dtos[i].getCSeriennrChargennr());

				}
				dataHelp[REPORT_LAGERSTANDLISTE_SNRCHNRAUFLAGER] = s;

				dataHelp[REPORT_LAGERSTANDLISTE_SUBREPORT_SNRCHNRAUFLAGER] = new LPDatenSubreport(dataSub, fieldnames);

			} catch (RemoteException ex) {
				// nothing
			}
		}

		// Gestehungspreis holen
		BigDecimal preis = null;

		BigDecimal lagerstandFuerPreis = lagerstand;

		BigDecimal wertMaterialzuschlag = new BigDecimal(0);

		if (vkPreislisteIId == null) {

			if (tStichtag != null) {
				try {
					preis = getLagerFac().getGestehungspreisZumZeitpunkt((Integer) o[0], lagerIId, tStichtag,
							theClientDto);
					// Wenns keinen Gest-Preis zum Zeitpunkt gibt,
					// dann

					// den aktuellen verwenden
					if (preis == null) {
						preis = lagerwert;

						if (sumLagerstand != null) {
							lagerstandFuerPreis = sumLagerstand;
						} else {
							lagerstandFuerPreis = BigDecimal.ZERO;
						}
					} else {
						// Mit lagerstand multiplizieren, da der
						// Gestpreis zum Zeitpunkt nur fuer einen
						// Artikel
						// ist
						preis = preis.multiply(lagerstand);
					}
				} catch (RemoteException ex6) {
					throwEJBExceptionLPRespectOld(ex6);
				}
			} else {
				preis = lagerwert;
			}

			if (preis != null && lagerstandFuerPreis.compareTo(BigDecimal.ZERO) > 0) {
				preis = preis.divide(lagerstandFuerPreis, 4, BigDecimal.ROUND_HALF_EVEN);
			} else {
				preis = BigDecimal.ZERO;
			}

		} else {
			Integer artikelIId = (Integer) o[0];
			VkpreisfindungDto dto = getVkPreisfindungFac().verkaufspreisfindungStufe1(artikelIId,
					Helper.cutDate(new java.sql.Date(System.currentTimeMillis())), vkPreislisteIId,
					new VkpreisfindungDto(theClientDto.getLocUi()), mwstsatzId_0, new BigDecimal(1),
					theClientDto.getSMandantenwaehrung(), theClientDto);
			if (dto.getVkpStufe1() != null && dto.getVkpStufe1().nettopreis != null) {
				preis = dto.getVkpStufe1().nettopreis;
			} else {
				preis = new BigDecimal(0);
			}

			wertMaterialzuschlag = getMaterialFac().getMaterialzuschlagVKInZielwaehrung(artikelIId, null,
					Helper.cutDate(new java.sql.Date(System.currentTimeMillis())), theClientDto.getSMandantenwaehrung(),
					theClientDto).multiply(lagerstand);

		}
		dataHelp[REPORT_LAGERSTANDLISTE_LAGERWERT_MATERIALZUSCHLAG] = wertMaterialzuschlag;
		// Einkaufspreis des ersten Lieferanten hinzufuegen
		Date dPreisgueltigkeit = tStichtag != null ? Helper.extractDate(tStichtag) : null;
		ArtikellieferantDto dto = getArtikelFac().getArtikelEinkaufspreis((Integer) o[0], null, new BigDecimal(1),
				theClientDto.getSMandantenwaehrung(), dPreisgueltigkeit, theClientDto);
		if (dto != null) {
			dataHelp[REPORT_LAGERSTANDLISTE_LIEF1PREIS] = dto.getLief1Preis();
		}

		if (bEKPreisWennGestpreisNull == true) {

			if (BigDecimal.ZERO.compareTo(preis) == 0 && dto != null) {
				preis = dto.getLief1Preis();
			}
		}

		dataHelp[REPORT_LAGERSTANDLISTE_GESTEHUNGSPREIS] = preis;

		if (preis == null) {
			preis = new BigDecimal(0);
		}
		dataHelp[REPORT_LAGERSTANDLISTE_LAGERWERT] = preis.multiply(lagerstand);

		if (bMitAbgewertetemGestpreis) {
			try {
				dataHelp[REPORT_LAGERSTANDLISTE_GESTEHUNGSPREIS_ABGEWERTET] = getLagerFac()
						.getAbgewertetenGestehungspreis(preis, (Integer) o[0], lagerIId, tStichtag,
								iMonateGestpreisAbwerten, dProzentGestpreisAbwerten);
			} catch (RemoteException ex7) {
				throwEJBExceptionLPRespectOld(ex7);
			}
		}

		// Reservierungen holen
		BigDecimal freierWert = BigDecimal.ZERO;
		try {
			BigDecimal reservierungen = getReservierungFac().getAnzahlReservierungen((Integer) o[0], theClientDto);
			if (reservierungen != null) {
				dataHelp[REPORT_LAGERSTANDLISTE_RESERVIERTE_MENGE] = new Double(reservierungen.doubleValue());
				dataHelp[REPORT_LAGERSTANDLISTE_RESERVIERTER_WERT] = preis.multiply(reservierungen);

				freierWert = preis.multiply(lagerstand).subtract(preis.multiply(reservierungen));

				if (freierWert.doubleValue() < 0) {
					freierWert = BigDecimal.ZERO;
				}

				dataHelp[REPORT_LAGERSTANDLISTE_FREIER_WERT] = freierWert;

			}
		} catch (RemoteException ex3) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex3);
		}

		// Fehlmengen holen

		try {
			BigDecimal fehlmengen = getFehlmengeFac().getAnzahlFehlmengeEinesArtikels((Integer) o[0], theClientDto);
			if (fehlmengen != null) {
				dataHelp[REPORT_LAGERSTANDLISTE_FEHLMENGE] = fehlmengen;
				dataHelp[REPORT_LAGERSTANDLISTE_FEHLMENGEN_WERT] = preis.multiply(fehlmengen);

				BigDecimal freierWertInklFehlmengen = freierWert.subtract(preis.multiply(fehlmengen));

				if (freierWertInklFehlmengen.doubleValue() > 0) {
					dataHelp[REPORT_LAGERSTANDLISTE_FREIER_WERT_INKL_FEHLMENGE] = freierWertInklFehlmengen;
				} else {
					dataHelp[REPORT_LAGERSTANDLISTE_FREIER_WERT_INKL_FEHLMENGE] = new BigDecimal(0);
				}
			}
		} catch (RemoteException ex3) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex3);
		}

	}

	public JasperPrintLP printUmbuchungsbeleg(Integer lagerbewegungIIdBuchungZubuchung,
			Integer lagerbewegungIIdBuchungAbbuchung, TheClientDto theClientDto) {
		index = -1;
		sAktuellerReport = LagerReportFac.REPORT_UMBUCHUNGSBELEG;
		data = new Object[1][REPORT_UMBUCHUNGSBELEG_ANZAHL_SPALTEN];
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		try {

			LagerbewegungDto zubuchung = getLagerFac()
					.getJuengsteBuchungEinerBuchungsNummer(lagerbewegungIIdBuchungZubuchung);
			LagerbewegungDto abbuchung = getLagerFac()
					.getJuengsteBuchungEinerBuchungsNummer(lagerbewegungIIdBuchungAbbuchung);

			LagerDto lagerZubuchungDto = getLagerFac().lagerFindByPrimaryKey(zubuchung.getLagerIId());
			LagerDto lagerAbbuchungDto = getLagerFac().lagerFindByPrimaryKey(abbuchung.getLagerIId());

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(zubuchung.getArtikelIId(), theClientDto);

			data[0][REPORT_UMBUCHUNGSBELEG_ARTIKELNUMMER] = artikelDto.getCNr();

			if (artikelDto.getArtikelsprDto() != null) {
				data[0][REPORT_UMBUCHUNGSBELEG_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
				data[0][REPORT_UMBUCHUNGSBELEG_KURZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCKbez();
				data[0][REPORT_UMBUCHUNGSBELEG_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCZbez();
				data[0][REPORT_UMBUCHUNGSBELEG_ZUSATZBEZEICHNUNG2] = artikelDto.getArtikelsprDto().getCZbez2();
			}

			data[0][REPORT_UMBUCHUNGSBELEG_LAGERORT] = getLagerFac().getLagerplaezteEinesArtikels(artikelDto.getIId(),
					null);

			data[0][REPORT_UMBUCHUNGSBELEG_SERIENNR] = zubuchung.getCSeriennrchargennr();
			data[0][REPORT_UMBUCHUNGSBELEG_MENGE] = zubuchung.getNMenge();
			data[0][REPORT_UMBUCHUNGSBELEG_DATUM] = zubuchung.getTBuchungszeit();
			data[0][REPORT_UMBUCHUNGSBELEG_VONLAGER] = lagerAbbuchungDto.getCNr();
			data[0][REPORT_UMBUCHUNGSBELEG_NACHLAGER] = lagerZubuchungDto.getCNr();
			data[0][REPORT_UMBUCHUNGSBELEG_EINHEIT] = artikelDto.getEinheitCNr().trim();

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		initJRDS(parameter, LagerReportFac.REPORT_MODUL, LagerReportFac.REPORT_UMBUCHUNGSBELEG,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	public JasperPrintLP printLagerbuchungsbeleg(Integer iExemplare, Integer iIdBuchung, TheClientDto theClientDto) {
		index = -1;
		sAktuellerReport = LagerReportFac.REPORT_LAGERBUCHUNGSBELEG;
		data = new Object[1][REPORT_LAGERBUCHUNGSBELEG_ANZAHL_SPALTEN];
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		try {

			LagerbewegungDto buchung = getLagerFac().getJuengsteBuchungEinerBuchungsNummer(iIdBuchung);

			LagerDto lagerDto = getLagerFac().lagerFindByPrimaryKey(buchung.getLagerIId());

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(buchung.getArtikelIId(), theClientDto);

			ArrayList alDaten = new ArrayList();

			Object[] oZeile = new Object[REPORT_LAGERBUCHUNGSBELEG_ANZAHL_SPALTEN];
			oZeile[REPORT_LAGERBUCHUNGSBELEG_ARTIKELNUMMER] = artikelDto.getCNr();

			oZeile[REPORT_LAGERBUCHUNGSBELEG_INDEX] = artikelDto.getCIndex();

			oZeile[REPORT_LAGERBUCHUNGSBELEG_REFERENZNUMMER] = artikelDto.getCReferenznr();
			oZeile[REPORT_LAGERBUCHUNGSBELEG_I_ID_BUCHUNG] = iIdBuchung;

			// Wenn Handlagerbewegung
			if (buchung.getCBelegartnr().equals(LocaleFac.BELEGART_HAND)) {

				try {
					Handlagerbewegung h = em.find(Handlagerbewegung.class, buchung.getIBelegartid());
					if (h != null) {
						oZeile[REPORT_LAGERBUCHUNGSBELEG_KOMMENTAR] = h.getCKommentar();
					}
				} catch (Exception e) {
					// NIX
				}
			}

			if (artikelDto.getArtikelsprDto() != null) {
				oZeile[REPORT_LAGERBUCHUNGSBELEG_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
				oZeile[REPORT_LAGERBUCHUNGSBELEG_KURZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCKbez();
				oZeile[REPORT_LAGERBUCHUNGSBELEG_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCZbez();
				oZeile[REPORT_LAGERBUCHUNGSBELEG_ZUSATZBEZEICHNUNG2] = artikelDto.getArtikelsprDto().getCZbez2();
			}

			oZeile[REPORT_LAGERBUCHUNGSBELEG_LAGERORT] = getLagerFac().getLagerplaezteEinesArtikels(artikelDto.getIId(),
					null);
			oZeile[REPORT_LAGERBUCHUNGSBELEG_SERIENNR] = buchung.getCSeriennrchargennr();

			if (Helper.short2boolean(buchung.getBAbgang()) == true) {
				oZeile[REPORT_LAGERBUCHUNGSBELEG_MENGE] = new BigDecimal(0).subtract(buchung.getNMenge());
			} else {
				oZeile[REPORT_LAGERBUCHUNGSBELEG_MENGE] = buchung.getNMenge();
			}

			oZeile[REPORT_LAGERBUCHUNGSBELEG_DATUM] = buchung.getTBuchungszeit();
			oZeile[REPORT_LAGERBUCHUNGSBELEG_LAGER] = lagerDto.getCNr();
			oZeile[REPORT_LAGERBUCHUNGSBELEG_EINHEIT] = artikelDto

					.getEinheitCNr().trim();

			oZeile[REPORT_LAGERBUCHUNGSBELEG_EXEMPLAREGESAMT] = iExemplare;

			for (int i = 0; i < iExemplare; i++) {

				Object[] oZeileKopie = oZeile.clone();
				oZeileKopie[REPORT_LAGERBUCHUNGSBELEG_EXEMPLAR] = new Integer(i + 1);
				alDaten.add(oZeileKopie);
			}

			Object[][] returnArray = new Object[alDaten.size()][REPORT_LAGERBUCHUNGSBELEG_ANZAHL_SPALTEN];
			data = (Object[][]) alDaten.toArray(returnArray);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		initJRDS(parameter, LagerReportFac.REPORT_MODUL, LagerReportFac.REPORT_LAGERBUCHUNGSBELEG,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printChargeneigenschaften(Integer iIdBuchung, TheClientDto theClientDto) {
		index = -1;
		sAktuellerReport = LagerReportFac.REPORT_CHARGENEIGENSCHAFTEN;
		data = new Object[1][REPORT_CHARGENEIGENSCHAFTEN_ANZAHL_SPALTEN];
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		try {
			ArtikelDto artikelDto = null;

			BelegInfos bi = null;
			LagerbewegungDto buchung = getLagerFac().getJuengsteBuchungEinerBuchungsNummer(iIdBuchung);

			bi = getLagerFac().getBelegInfos(buchung.getCBelegartnr(), buchung.getIBelegartid(),
					buchung.getIBelegartpositionid(), theClientDto);

			LagerDto lagerDto = getLagerFac().lagerFindByPrimaryKey(buchung.getLagerIId());

			artikelDto = getArtikelFac().artikelFindByPrimaryKey(buchung.getArtikelIId(), theClientDto);

			data[0][REPORT_CHARGENEIGENSCHAFTEN_ARTIKELNUMMER] = artikelDto.getCNr();

			data[0][REPORT_CHARGENEIGENSCHAFTEN_INDEX] = artikelDto.getCIndex();

			data[0][REPORT_CHARGENEIGENSCHAFTEN_REFERENZNUMMER] = artikelDto.getCReferenznr();
			data[0][REPORT_CHARGENEIGENSCHAFTEN_I_ID_BUCHUNG] = iIdBuchung;

			data[0][REPORT_CHARGENEIGENSCHAFTEN_ARTGRU_I_ID] = artikelDto.getArtgruIId();

			if (buchung.getCSeriennrchargennr() != null) {
				data[0][REPORT_CHARGENEIGENSCHAFTEN_ARTIKELSNRCHNR_I_ID] = getLagerFac()
						.artikelsnrchnrIIdFindByArtikelIIdCSeriennrchargennr(artikelDto.getIId(),
								buchung.getCSeriennrchargennr());
			}

			if (artikelDto.getArtikelsprDto() != null) {
				data[0][REPORT_CHARGENEIGENSCHAFTEN_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
				data[0][REPORT_CHARGENEIGENSCHAFTEN_KURZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCKbez();
				data[0][REPORT_CHARGENEIGENSCHAFTEN_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCZbez();
				data[0][REPORT_CHARGENEIGENSCHAFTEN_ZUSATZBEZEICHNUNG2] = artikelDto.getArtikelsprDto().getCZbez2();
			}

			data[0][REPORT_CHARGENEIGENSCHAFTEN_LAGERORT] = getLagerFac()
					.getLagerplaezteEinesArtikels(artikelDto.getIId(), null);
			data[0][REPORT_CHARGENEIGENSCHAFTEN_SERIENNR] = buchung.getCSeriennrchargennr();

			if (Helper.short2boolean(buchung.getBAbgang()) == true) {
				data[0][REPORT_CHARGENEIGENSCHAFTEN_MENGE] = new BigDecimal(0).subtract(buchung.getNMenge());
			} else {
				data[0][REPORT_CHARGENEIGENSCHAFTEN_MENGE] = buchung.getNMenge();
			}

			data[0][REPORT_CHARGENEIGENSCHAFTEN_DATUM] = buchung.getTBuchungszeit();
			data[0][REPORT_CHARGENEIGENSCHAFTEN_LAGER] = lagerDto.getCNr();
			data[0][REPORT_CHARGENEIGENSCHAFTEN_EINHEIT] = artikelDto

					.getEinheitCNr().trim();

			initJRDS(parameter, LagerReportFac.REPORT_MODUL, LagerReportFac.REPORT_CHARGENEIGENSCHAFTEN,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

			JasperPrintLP print = getReportPrint();

			String snrchr = "KEINE_SNRCHNR";

			if (buchung.getCSeriennrchargennr() != null) {
				snrchr = buchung.getCSeriennrchargennr();
			}

			String zusatz = "";

			if (bi.getZusatz() != null) {
				zusatz = " " + bi.getZusatz();
			}
			PrintInfoDto values = new PrintInfoDto();
			values.setDocPath(new HeliumDocPath().add(new DocNodeLiteral(theClientDto.getMandant()))
					.add(new DocNodeFolder(DocNodeBase.BELEGART_ARTIKEL)).add(new DocNodeFolder(artikelDto.getCNr()))
					.add(new DocNodeFolder(WarenbewegungenHandler.DOCNODE_CHARGENDOKUMENTE))
					.add(new DocNodeFolder(snrchr))
					.add(new DocNodeFolder(buchung.getCBelegartnr().trim() + " " + bi.getBelegnummer() + zusatz)));

			values.setTable("");

			if (print != null) {
				print.setOInfoForArchive(values);
			}

			return print;

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printHandlagerbewegungen(Timestamp dVon, Timestamp dBis, Integer artikelIId,
			Integer artikelgruppeIId, TheClientDto theClientDto) {

		index = -1;
		sAktuellerReport = LagerReportFac.REPORT_HANDLAGERBEWEGUNGEN;

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		Session session = FLRSessionFactory.getFactory().openSession();

		// SP6794
		String eingeloggterMandant = theClientDto.getMandant();
		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			eingeloggterMandant = getSystemFac().getHauptmandant();
		}

		String queryString = "  SELECT hd, aspr FROM FLRHandlagerbewegung AS hd LEFT OUTER JOIN hd.flrartikel.artikelsprset AS aspr ";
		queryString += " WHERE hd.flrartikel.mandant_c_nr='" + eingeloggterMandant + "' AND hd.t_buchungszeit>='"
				+ Helper.formatTimestampWithSlashes(dVon) + "'  AND hd.t_buchungszeit<'"
				+ Helper.formatTimestampWithSlashes(Helper.addiereTageZuTimestamp(dBis, 1)) + "' ";

		session.enableFilter("filterMandant").setParameter("paramMandant", eingeloggterMandant);
		String sLocUI = Helper.locale2String(theClientDto.getLocUi());

		session.enableFilter("filterLocale").setParameter("paramLocale", sLocUI);

		if (artikelgruppeIId != null) {

			parameter.put("P_ARTIKELGRUPPE",
					getArtikelFac().artgruFindByPrimaryKey(artikelgruppeIId, theClientDto).getBezeichnung());

			queryString += " AND hd.flrartikel.flrartikelgruppe.i_id IN"
					+ getArtikelFac().holeAlleArtikelgruppen(artikelgruppeIId).erzeuge_IN_Fuer_Query();

		}

		if (artikelIId != null) {

			parameter.put("P_ARTIKEL",
					getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto).formatArtikelbezeichnung());

			queryString += " AND hd.flrartikel.i_id=" + artikelIId + " ";
		}

		queryString += " ORDER BY hd.t_buchungszeit ASC ";

		Query query = session.createQuery(queryString);
		List results = query.list();
		Iterator resultListIterator = results.iterator();

		ArrayList alDaten = new ArrayList();

		while (resultListIterator.hasNext()) {

			Object[] oResult = (Object[]) resultListIterator.next();

			FLRHandlagerbewegung flrHandlagerbewegung = (FLRHandlagerbewegung) oResult[0];
			FLRArtikellistespr flrArtikellistespr = (FLRArtikellistespr) oResult[1];

			Object[] oZeile = new Object[REPORT_HANDLAGERBEWEGUNGEN_ANZAHL_SPALTEN];

			oZeile[REPORT_HANDLAGERBEWEGUNGEN_ARTIKELNUMMER] = flrHandlagerbewegung.getFlrartikel().getC_nr();
			oZeile[REPORT_HANDLAGERBEWEGUNGEN_ARTIKELBEZEICHNUNG] = flrArtikellistespr.getC_bez();
			oZeile[REPORT_HANDLAGERBEWEGUNGEN_ARTIKELZUSATZBEZEICHNUNG] = flrArtikellistespr.getC_zbez();
			oZeile[REPORT_HANDLAGERBEWEGUNGEN_ARTIKELZUSATZBEZEICHNUNG2] = flrArtikellistespr.getC_zbez2();
			oZeile[REPORT_HANDLAGERBEWEGUNGEN_ABGANG] = Helper.short2Boolean(flrHandlagerbewegung.getB_abgang());
			oZeile[REPORT_HANDLAGERBEWEGUNGEN_BUCHUNGSZEIT] = flrHandlagerbewegung.getT_buchungszeit();
			oZeile[REPORT_HANDLAGERBEWEGUNGEN_EINSTANDSPREIS] = flrHandlagerbewegung.getN_einstandspreis();
			oZeile[REPORT_HANDLAGERBEWEGUNGEN_VKPREIS] = flrHandlagerbewegung.getN_verkaufspreis();
			oZeile[REPORT_HANDLAGERBEWEGUNGEN_MENGE] = flrHandlagerbewegung.getN_menge();

			oZeile[REPORT_HANDLAGERBEWEGUNGEN_PERSON_GEBUCHT] = HelperServer
					.formatPersonAusFLRPartner(flrHandlagerbewegung.getFlrpersonal_aendern().getFlrpartner());
			oZeile[REPORT_HANDLAGERBEWEGUNGEN_KOMMENTAR] = flrHandlagerbewegung.getC_kommentar();
			oZeile[REPORT_HANDLAGERBEWEGUNGEN_LAGER] = flrHandlagerbewegung.getFlrlager().getC_nr();

			if (Helper.short2boolean(flrHandlagerbewegung.getFlrartikel().getB_seriennrtragend())
					|| Helper.short2boolean(flrHandlagerbewegung.getFlrartikel().getB_chargennrtragend())) {
				oZeile[REPORT_HANDLAGERBEWEGUNGEN_SUBREPORT_SNRCHNR] = getLagerFac()
						.getSerienChargennummernEinerBelegpositionSubreport(LocaleFac.BELEGART_HAND,
								flrHandlagerbewegung.getI_id(), theClientDto);
			}

			alDaten.add(oZeile);

		}

		Object[][] returnArray = new Object[alDaten.size()][REPORT_HANDLAGERBEWEGUNGEN_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(returnArray);

		parameter.put("P_VON", dVon);
		parameter.put("P_BIS", dBis);

		parameter.put("P_MANDANTENWAEHRUNG", theClientDto.getSMandantenwaehrung());

		initJRDS(parameter, LagerReportFac.REPORT_MODUL, LagerReportFac.REPORT_HANDLAGERBEWEGUNGEN,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printLadenhueter(Timestamp dVon, Timestamp dBis, Integer iSortierung, String artiklenrVon,
			String artiklenrBis, Integer lagerIId, boolean bMitHandlagerbewegungen, boolean bMitFertigung,
			boolean bMitVersteckten, Integer artikelgruppeIId, boolean bZugaengeBeruecksichtigen,
			TheClientDto theClientDto) {

		/**
		 * Ladenh&uuml;ter sind die Teile, die l&auml;nger als xx-Tage zwischen VON und
		 * BIS auf Lager liegen.
		 */

		if (dVon == null || dBis == null || iSortierung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("dVon == null || dBis == null || iSortierung == null"));
		}
		index = -1;
		sAktuellerReport = LagerReportFac.REPORT_LADENHUETER;

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		String sQuery = "SELECT l.artikel_i_id, l.i_id_buchung, l.n_menge, b_abgang, c_belegartnr FROM FLRLagerbewegung l WHERE l.flrartikel.mandant_c_nr='"
				+ theClientDto.getMandant() + "'  "
				+ " AND l.flrartikel.b_lagerbewirtschaftet=1 AND l.b_historie=0 AND l.flrartikel.b_lagerbewertet=1 AND l.c_belegartnr NOT IN ('"
				+ LocaleFac.BELEGART_INVENTUR + "') " + " AND l.t_belegdatum >='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(dVon.getTime())) + "'" + " AND l.t_belegdatum <='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(dBis.getTime())) + "'";

		if (lagerIId != null) {
			try {
				LagerDto lagerDto = getLagerFac().lagerFindByPrimaryKey(lagerIId);
				parameter.put("P_LAGER", lagerDto.getCNr());
				sQuery += " AND l.flrlager.i_id=" + lagerIId + " ";

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
		}

		if (!bMitHandlagerbewegungen) {
			sQuery += " AND l.c_belegartnr NOT IN ('" + LocaleFac.BELEGART_HAND + "') ";
		}

		if (!bMitFertigung) {
			sQuery += " AND l.c_belegartnr NOT IN ('" + LocaleFac.BELEGART_LOS + "') ";
		}

		sQuery += " ORDER BY l.artikel_i_id ASC,  l.i_id_buchung ASC, l.t_buchungszeit DESC";

		Session session = FLRSessionFactory.getFactory().openSession();

		parameter.put("P_BERUECKSICHTIGEFERTIGUNG", new Boolean(bMitFertigung));
		parameter.put("P_BERUECKSICHTIGEZUGAENGE", new Boolean(bZugaengeBeruecksichtigen));
		parameter.put("P_BERUECKSICHTIGEHANDBUCHUNG", new Boolean(bMitHandlagerbewegungen));
		parameter.put("P_MITVERSTECKTEN", new Boolean(bMitVersteckten));
		Query querylagerbewegungen = session.createQuery(sQuery);
		List<?> results = querylagerbewegungen.list();

		Iterator<?> resultListIterator = results.iterator();
		int lastArtikel = -1;
		HashMap<Integer, String> hmVerwendeteArtikel = new HashMap<Integer, String>();

		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();

			if (((BigDecimal) o[2]).doubleValue() != 0) {

				// Abgang oder Zugang
				if (Helper.short2boolean((Short) o[3]) == true) {
					// Wenn Abgang ungleich 0, dann kein ladenhueter
					if (!hmVerwendeteArtikel.containsKey((Integer) o[0])) {
						hmVerwendeteArtikel.put((Integer) o[0], "");
					}

				} else {
					if (bZugaengeBeruecksichtigen) {
						// PJ 16554
						if (!hmVerwendeteArtikel.containsKey((Integer) o[0])) {
							hmVerwendeteArtikel.put((Integer) o[0], "");
						}
					}
				}

			}

		}
		session.close();

		session = FLRSessionFactory.getFactory().openSession();

		// SP6794
		String eingeloggterMandant = theClientDto.getMandant();
		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			eingeloggterMandant = getSystemFac().getHauptmandant();
		}

		String queryString = "  SELECT artikelliste.i_id, artikelliste.c_nr, aspr.c_bez, sum(alager.n_lagerstand),sum(alager.n_gestehungspreis*alager.n_lagerstand), artikelliste.b_lagerbewirtschaftet,aspr.c_kbez,artikelliste.b_versteckt, "
				+ " (SELECT SUM(f.n_menge) FROM FLRFehlmenge f WHERE f.artikel_i_id=artikelliste.i_id), "
				+ " (SELECT SUM(r.n_menge) FROM FLRArtikelreservierung r WHERE r.flrartikel.i_id=artikelliste.i_id), "
				+ " gruppe.c_nr, klasse.c_nr, "

				+ " (SELECT SUM(ra.n_gesamtmenge) FROM FLRRahmenbedarfe ra WHERE ra.flrartikel.i_id=artikelliste.i_id), "
				+ " (SELECT s FROM FLRStueckliste s WHERE s.artikel_i_id=artikelliste.i_id AND s.mandant_c_nr='"
				+ theClientDto.getMandant() + "'), artikelliste.artikelart_c_nr, 1, artikelliste.c_referenznr  "
				+ " FROM FLRArtikelliste AS artikelliste" + " LEFT OUTER JOIN artikelliste.artikellagerset AS alager "
				+ " LEFT OUTER JOIN artikelliste.flrartikelgruppe AS gruppe "
				+ " LEFT OUTER JOIN artikelliste.flrartikelklasse AS klasse "
				+ " LEFT OUTER JOIN artikelliste.artikelsprset AS aspr ";
		queryString += " WHERE artikelliste.mandant_c_nr='" + eingeloggterMandant
				+ "'  AND artikelliste.artikelart_c_nr not in('" + ArtikelFac.ARTIKELART_HANDARTIKEL
				+ "') AND artikelliste.b_lagerbewirtschaftet=1 AND artikelliste.b_lagerbewertet=1";

		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER,
				theClientDto)) {
			session.enableFilter("filterMandant").setParameter("paramMandant", theClientDto.getMandant());
		} else {
			session.enableFilter("filterMandant").setParameter("paramMandant", eingeloggterMandant);
		}

		String sLocUI = Helper.locale2String(theClientDto.getLocUi());

		session.enableFilter("filterLocale").setParameter("paramLocale", sLocUI);

		if (artiklenrVon != null) {
			parameter.put("P_ARTIKELNRVON", artiklenrVon);
			queryString += " AND artikelliste.c_nr >='" + artiklenrVon + "'";
		}
		if (artiklenrBis != null) {
			parameter.put("P_ARTIKELNRBIS", artiklenrBis);

			queryString += " AND artikelliste.c_nr <='" + Helper.fitString2Length(artiklenrBis, 25, '_') + "'";
		}
		if (lagerIId != null) {
			queryString += " AND alager.compId.lager_i_id=" + lagerIId + " ";
		}

		if (artikelgruppeIId != null) {
			queryString += " AND artikelliste.flrartikelgruppe.i_id=" + artikelgruppeIId + " ";
		}

		queryString += " GROUP BY artikelliste.i_id,artikelliste.c_nr, aspr.c_bez,aspr.c_kbez, artikelliste.b_lagerbewirtschaftet,artikelliste.b_versteckt,gruppe.c_nr,klasse.c_nr,artikelliste.artikelart_c_nr, artikelliste.c_referenznr ";

		if (iSortierung == LagerReportFac.REPORT_LADENHUETER_SORTIERUNG_ARTIKELGRUPPE) {
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.artikelgruppe", theClientDto.getMandant(), theClientDto.getLocUi()));
			queryString += " ORDER BY gruppe.c_nr ASC";
		} else if (iSortierung == LagerReportFac.REPORT_LADENHUETER_SORTIERUNG_ARTIKELKLASSE) {
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.artikelklasse", theClientDto.getMandant(), theClientDto.getLocUi()));
			queryString += " ORDER BY klasse.c_nr ASC";
		} else {
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.artikelnummer", theClientDto.getMandant(), theClientDto.getLocUi()));
			queryString += " ORDER BY artikelliste.c_nr ASC";
		}

		Query query = session.createQuery(queryString);
		results = query.list();
		resultListIterator = results.iterator();

		ArrayList<Object[]> ladenhueter = new ArrayList<Object[]>();

		HashMap<Integer, BigDecimal> hmLagerstaendeZumZeitpunkt = getLagerFac()
				.getLagerstandAllerArtikelZumZeitpunkt(lagerIId, dBis, theClientDto);

		while (resultListIterator.hasNext()) {
			Object o[] = (Object[]) resultListIterator.next();

			// Lagerstand zum Stichtag PJ17940
			BigDecimal lagerstand = hmLagerstaendeZumZeitpunkt.get((Integer) o[0]);

			if (lagerstand == null) {
				lagerstand = BigDecimal.ZERO;
			}

			if (o[3] != null && lagerstand.doubleValue() > 0) {
				if (!hmVerwendeteArtikel.containsKey(o[0])) {
					o[15] = lagerstand;
					ladenhueter.add(o);
				}
			}

		}
		session.close();

		data = new Object[ladenhueter.size()][20];

		for (int i = 0; i < ladenhueter.size(); i++) {

			Object[] o = (Object[]) ladenhueter.get(i);

			data[i][REPORT_LADENHUETER_ARTIKELNUMMER] = o[1];
			data[i][REPORT_LADENHUETER_BEZEICHNUNG] = o[2];
			data[i][REPORT_LADENHUETER_ARTIKELREFERENZNUMMER] = o[16];

			// Einkaufspreis des ersten Lieferanten hinzufuegen
			ArtikellieferantDto dto = getArtikelFac().getArtikelEinkaufspreis((Integer) o[0], null, new BigDecimal(1),
					theClientDto.getSMandantenwaehrung(), null, theClientDto);
			if (dto != null) {
				data[i][REPORT_LADENHUETER_EINKAUFSPREIS] = dto.getLief1Preis();
				data[i][REPORT_LADENHUETER_LIEFERANTARTIKELNUMMER] = dto.getCArtikelnrlieferant();

				LieferantDto lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(dto.getLieferantIId(),
						theClientDto);
				data[i][REPORT_LADENHUETER_BEVORZUGTERLIEFERANT] = lieferantDto.getPartnerDto().formatTitelAnrede();
			}

			if (o[4] != null && ((BigDecimal) o[3]).doubleValue() > 0) {
				data[i][REPORT_LADENHUETER_GESTPREIS] = ((BigDecimal) o[4]).divide((BigDecimal) o[3],
						BigDecimal.ROUND_HALF_EVEN);
			} else {
				if (dto != null && dto.getLief1Preis() != null) {
					data[i][REPORT_LADENHUETER_GESTPREIS] = dto.getLief1Preis();
				} else {
					data[i][REPORT_LADENHUETER_GESTPREIS] = new BigDecimal(0);
				}
			}

			data[i][REPORT_LADENHUETER_LAGERSTAND_HEUTE] = o[3];
			data[i][REPORT_LADENHUETER_LAGERSTAND_ZUMSTICHTAG] = o[15];

			data[i][REPORT_LADENHUETER_ARTIKELART] = o[14];
			data[i][REPORT_LADENHUETER_RESERVIERUNGEN] = o[9];
			data[i][REPORT_LADENHUETER_VERSTECHT] = Helper.short2Boolean((Short) o[7]);
			data[i][REPORT_LADENHUETER_FEHLMENGEN] = o[8];
			data[i][REPORT_LADENHUETER_ARTIKELGRUPPE] = o[10];
			data[i][REPORT_LADENHUETER_ARTIKELKLASSE] = o[11];

			data[i][REPORT_LADENHUETER_RAHMENDETAILBEDARFE] = o[12];
			data[i][REPORT_LADENHUETER_ARTIKELART] = o[14];

			try {
				data[i][REPORT_LADENHUETER_RAHMENRESERVIERUNGEN] = getReservierungFac()
						.getAnzahlRahmenreservierungen((Integer) o[0], theClientDto);
			} catch (RemoteException ex2) {
				throwEJBExceptionLPRespectOld(ex2);
			}

			if (o[13] != null) {
				com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste s = (com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste) o[13];
				data[i][REPORT_LADENHUETER_STUECKLISTE] = Boolean.TRUE;

				if (s.getStuecklisteart_c_nr()
						.equals(com.lp.server.stueckliste.service.StuecklisteFac.STUECKLISTEART_HILFSSTUECKLISTE)) {
					data[i][REPORT_LADENHUETER_HILFSSTUECKLISTE] = Boolean.TRUE;
				} else {
					data[i][REPORT_LADENHUETER_HILFSSTUECKLISTE] = Boolean.FALSE;
				}
			} else {
				data[i][REPORT_LADENHUETER_STUECKLISTE] = Boolean.FALSE;
				data[i][REPORT_LADENHUETER_HILFSSTUECKLISTE] = Boolean.FALSE;
			}

			try {
				data[i][REPORT_LADENHUETER_EINSTANDSPREIS] = getLagerFac()
						.getGemittelterEinstandspreisAllerLagerndenArtikel((Integer) o[0], lagerIId, theClientDto);
			} catch (RemoteException ex1) {
				throwEJBExceptionLPRespectOld(ex1);
			}

		}

		if (iSortierung == LagerReportFac.REPORT_LADENHUETER_SORTIERUNG_LAGERWERT) {
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.lagerwert", theClientDto.getMandant(), theClientDto.getLocUi()));
			for (int i = data.length - 1; i > 0; --i) {
				for (int j = 0; j < i; ++j) {
					Object[] o = data[j];
					Object[] o1 = data[j + 1];

					BigDecimal wert = ((BigDecimal) o[REPORT_LADENHUETER_GESTPREIS])
							.multiply((BigDecimal) o[REPORT_LADENHUETER_LAGERSTAND_ZUMSTICHTAG]);
					BigDecimal wert1 = ((BigDecimal) o1[REPORT_LADENHUETER_GESTPREIS])
							.multiply((BigDecimal) o1[REPORT_LADENHUETER_LAGERSTAND_ZUMSTICHTAG]);
					if (wert.compareTo(wert1) < 0) {
						data[j] = o1;
						data[j + 1] = o;
					}
				}
			}
		} else if (iSortierung == LagerReportFac.REPORT_LADENHUETER_SORTIERUNG_GESTPREIS) {
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.gestehungspreis", theClientDto.getMandant(), theClientDto.getLocUi()));
			for (int i = data.length - 1; i > 0; --i) {
				for (int j = 0; j < i; ++j) {
					Object[] o = data[j];
					Object[] o1 = data[j + 1];

					BigDecimal wert = ((BigDecimal) o[REPORT_LADENHUETER_GESTPREIS]);
					BigDecimal wert1 = ((BigDecimal) o1[REPORT_LADENHUETER_GESTPREIS]);
					if (wert.compareTo(wert1) < 0) {
						data[j] = o1;
						data[j + 1] = o;
					}
				}
			}

		} else if (iSortierung == LagerReportFac.REPORT_LADENHUETER_SORTIERUNG_EINSTANDSPREIS) {
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.einstandspreis", theClientDto.getMandant(), theClientDto.getLocUi()));
			for (int i = data.length - 1; i > 0; --i) {
				for (int j = 0; j < i; ++j) {
					Object[] o = data[j];
					Object[] o1 = data[j + 1];

					BigDecimal wert = ((BigDecimal) o[REPORT_LADENHUETER_EINSTANDSPREIS]);
					BigDecimal wert1 = ((BigDecimal) o1[REPORT_LADENHUETER_EINSTANDSPREIS]);
					if (wert.compareTo(wert1) < 0) {
						data[j] = o1;
						data[j + 1] = o;
					}
				}
			}
		}

		parameter.put("P_VON", dVon);
		parameter.put("P_BIS", dBis);

		// SK Parameter Mandantenwaehrung
		parameter.put("P_MANDANTENWAEHRUNG", theClientDto.getSMandantenwaehrung());

		initJRDS(parameter, LagerReportFac.REPORT_MODUL, LagerReportFac.REPORT_LADENHUETER, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printWarenbewegungsjournal(Integer artikelIId, Integer lagerIId, Timestamp dVon,
			Timestamp dBis, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		if (artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null"));
		}
		ArtikelDto dto = getArtikelFac().artikelFindByPrimaryKeySmall(artikelIId, theClientDto);

		boolean darfEinkaufspreisSehen = getTheJudgeFac().hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF,
				theClientDto);
		boolean darfVerkaufspreisSehen = getTheJudgeFac().hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF,
				theClientDto);

		Session session = FLRSessionFactory.getFactory().openSession();

		// Testfunktion AD
		// recalcGestehungspreisKomplett(artikelIId, session, true);

		org.hibernate.Criteria crit = session.createCriteria(FLRLagerbewegung.class).createAlias("flrartikel", "a")
				.add(Restrictions.eq("a.i_id", artikelIId)).createAlias("flrlager", "l")
				.add(Restrictions.eq("l.mandant_c_nr", theClientDto.getMandant()))
				.add(Restrictions.not(Restrictions.eq("l.lagerart_c_nr", LagerFac.LAGERART_WERTGUTSCHRIFT)))
				.add(Restrictions.eq(LagerFac.FLR_LAGERBEWEGUNG_B_ABGANG, Helper.boolean2Short(false)))
				.add(Restrictions.eq(LagerFac.FLR_LAGERBEWEGUNG_B_HISTORIE, Helper.boolean2Short(false)))
				.addOrder(Order.desc(LagerFac.FLR_LAGERBEWEGUNG_T_BELEGDATUM))
				.addOrder(Order.desc(LagerFac.FLR_LAGERBEWEGUNG_T_BUCHUNGSZEIT));

		if (lagerIId != null) {
			crit.add(Restrictions.eq("l.i_id", lagerIId));
		}

		if (dVon != null) {
			crit.add(Restrictions.ge(LagerFac.FLR_LAGERBEWEGUNG_T_BUCHUNGSZEIT, Helper.cutTimestamp(dVon)));
		}
		if (dBis != null) {
			Timestamp tsBis = new Timestamp(dBis.getTime());
			tsBis.setTime(Helper.addiereTageZuTimestamp(tsBis, 1).getTime());

			crit.add(Restrictions.lt(LagerFac.FLR_LAGERBEWEGUNG_T_BUCHUNGSZEIT, Helper.cutTimestamp(tsBis)));
		}

		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();

		index = -1;
		sAktuellerReport = LagerReportFac.REPORT_WARENBEWEGUNGSJOURNAL;

		ArrayList<Object[]> al = new ArrayList<Object[]>();

		while (resultListIterator.hasNext()) {
			FLRLagerbewegung lagerbewegung = (FLRLagerbewegung) resultListIterator.next();
			if (lagerbewegung.getN_menge().doubleValue() != 0) {
				Object[] dataHelp = new Object[23];

				try {
					BelegInfos infosZugang = getLagerFac().getBelegInfos(lagerbewegung.getC_belegartnr(),
							lagerbewegung.getI_belegartid(), lagerbewegung.getI_belegartpositionid(), theClientDto);

					dataHelp[REPORT_WARENBEWEGUNGSJOURNAL_ZUGANG_WER_HAT_GEBUCHT] = lagerbewegung.getFlrpersonal()
							.getC_kurzzeichen();

					dataHelp[REPORT_WARENBEWEGUNGSJOURNAL_ZUGANGSBELEG] = lagerbewegung.getFlrbelegart().getC_kbez()
							+ infosZugang.getBelegnummer();
					dataHelp[REPORT_WARENBEWEGUNGSJOURNAL_MANDANT_ZUGANGSBELEG] = infosZugang.getMandantCNr();
					dataHelp[REPORT_WARENBEWEGUNGSJOURNAL_LAGER] = lagerbewegung.getFlrlager().getC_nr();

					dataHelp[REPORT_WARENBEWEGUNGSJOURNAL_SNR] = lagerbewegung.getC_seriennrchargennr();
					dataHelp[REPORT_WARENBEWEGUNGSJOURNAL_VERSION] = lagerbewegung.getC_version();

					dataHelp[REPORT_WARENBEWEGUNGSJOURNAL_ZUBUCHUNGSZEITPUNKT] = lagerbewegung.getT_buchungszeit();
					if (darfEinkaufspreisSehen) {
						dataHelp[REPORT_WARENBEWEGUNGSJOURNAL_EINSTANDSPREIS] = lagerbewegung.getN_einstandspreis();
					}

					if (lagerbewegung.getFlrhersteller() != null) {
						dataHelp[REPORT_WARENBEWEGUNGSJOURNAL_HERSTELLER] = lagerbewegung.getFlrhersteller().getC_nr();
					}
					if (lagerbewegung.getFlrland() != null) {
						dataHelp[REPORT_WARENBEWEGUNGSJOURNAL_URSPRUNGSLAND] = lagerbewegung.getFlrland().getC_lkz();
					}

					dataHelp[REPORT_WARENBEWEGUNGSJOURNAL_WE_REFERENZ] = getLagerFac()
							.getWareneingangsreferenzSubreport(lagerbewegung.getC_belegartnr(),
									lagerbewegung.getI_belegartpositionid(),
									SeriennrChargennrMitMengeDto.erstelleDtoAusEinerChargennummer(
											lagerbewegung.getC_seriennrchargennr(), lagerbewegung.getN_menge()),
									false, theClientDto);

					dataHelp[REPORT_WARENBEWEGUNGSJOURNAL_ZUGANGSMENGE] = lagerbewegung.getN_menge();

					dataHelp[REPORT_WARENBEWEGUNGSJOURNAL_ZUGANGFIRMAKOMMENTAR] = infosZugang.getKundeLieferant();

					dataHelp[REPORT_WARENBEWEGUNGSJOURNAL_ZUGANGSDATUM] = infosZugang.getBelegdatum();

					Session sessionSub = FLRSessionFactory.getFactory().openSession();
					String queryString = "FROM FLRLagerabgangursprung AS lagerabgangursprung WHERE lagerabgangursprung.compId.i_lagerbewegungidursprung="
							+ lagerbewegung.getI_id_buchung() + " AND lagerabgangursprung.n_verbrauchtemenge>0";
					Query querySub = sessionSub.createQuery(queryString);
					List<?> subResults = querySub.list();
					Iterator<?> subResultListIterator = subResults.iterator();
					if (subResults.size() == 0) {

						// CK: Projekt 8200
						if (darfEinkaufspreisSehen) {
							dataHelp[REPORT_WARENBEWEGUNGSJOURNAL_GESTPREIS] = getLagerFac()
									.getGemittelterGestehungspreisEinesLagers(artikelIId,
											lagerbewegung.getFlrlager().getI_id(), theClientDto);
						}

						al.add(dataHelp);
					} else {
						while (subResultListIterator.hasNext()) {
							FLRLagerabgangursprung lagerabgangursprung = (FLRLagerabgangursprung) subResultListIterator
									.next();

							Session sessionAbgang = FLRSessionFactory.getFactory().openSession();
							org.hibernate.Criteria critAbgang = sessionAbgang.createCriteria(FLRLagerbewegung.class);
							critAbgang.add(Restrictions.eq(LagerFac.FLR_LAGERBEWEGUNG_I_ID_BUCHUNG,
									lagerabgangursprung.getCompId().getI_lagerbewegungid()));

							critAbgang.add(Restrictions.eq(LagerFac.FLR_LAGERBEWEGUNG_B_HISTORIE,
									Helper.boolean2Short(false)));

							critAbgang.addOrder(Order.desc(LagerFac.FLR_LAGERBEWEGUNG_T_BUCHUNGSZEIT));
							List<?> resultsAbgang = critAbgang.list();
							try {
								FLRLagerbewegung flrLagerbewegungAbgang = (FLRLagerbewegung) resultsAbgang.iterator()
										.next();

								BelegInfos infosAbgang = getLagerFac().getBelegInfos(
										flrLagerbewegungAbgang.getC_belegartnr(),
										flrLagerbewegungAbgang.getI_belegartid(),
										lagerbewegung.getI_belegartpositionid(), theClientDto);
								dataHelp[REPORT_WARENBEWEGUNGSJOURNAL_ABGANG_WER_HAT_GEBUCHT] = flrLagerbewegungAbgang
										.getFlrpersonal().getC_kurzzeichen();
								dataHelp[REPORT_WARENBEWEGUNGSJOURNAL_ABGANGSBELEG] = flrLagerbewegungAbgang
										.getFlrbelegart().getC_kbez() + infosAbgang.getBelegnummer();

								dataHelp[REPORT_WARENBEWEGUNGSJOURNAL_MANDANT_ABGANGSBELEG] = infosAbgang
										.getMandantCNr();

								dataHelp[REPORT_WARENBEWEGUNGSJOURNAL_ZUGANGSBELEG] = lagerbewegung.getFlrbelegart()
										.getC_kbez() + infosZugang.getBelegnummer();
								dataHelp[REPORT_WARENBEWEGUNGSJOURNAL_MANDANT_ZUGANGSBELEG] = infosZugang
										.getMandantCNr();

								dataHelp[REPORT_WARENBEWEGUNGSJOURNAL_ABGANGFIRMAKOMMENTAR] = infosAbgang
										.getKundeLieferant();
								dataHelp[REPORT_WARENBEWEGUNGSJOURNAL_ABGANGSDATUM] = infosAbgang.getBelegdatum();

								dataHelp[REPORT_WARENBEWEGUNGSJOURNAL_ABBUCHUNGSZEITPUNKT] = flrLagerbewegungAbgang
										.getT_buchungszeit();

								if (darfEinkaufspreisSehen) {
									dataHelp[REPORT_WARENBEWEGUNGSJOURNAL_GESTPREIS] = lagerabgangursprung
											.getN_gestehungspreis();
								}
								if (darfVerkaufspreisSehen) {
									dataHelp[REPORT_WARENBEWEGUNGSJOURNAL_VKPREIS] = flrLagerbewegungAbgang
											.getN_verkaufspreis();
								}
								dataHelp[REPORT_WARENBEWEGUNGSJOURNAL_ABGANGSMENGE] = lagerabgangursprung
										.getN_verbrauchtemenge();
								al.add(dataHelp);
							} catch (java.util.NoSuchElementException e) {
								dataHelp[REPORT_WARENBEWEGUNGSJOURNAL_ABGANGSBELEG] = "FEHLER:";
								dataHelp[REPORT_WARENBEWEGUNGSJOURNAL_ABGANGFIRMAKOMMENTAR] = "KEINE INFORMATIONEN";
								al.add(dataHelp);
							}
							dataHelp = new Object[23];
							sessionAbgang.close();
						}
					}
					sessionSub.close();
				} catch (RemoteException ex1) {
					throwEJBExceptionLPRespectOld(ex1);
				}
			}

		}
		session.close();

		Object[][] returnArray = new Object[al.size()][23];
		data = (Object[][]) al.toArray(returnArray);

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_ARTIKEL", dto.formatArtikelbezeichnung());
		parameter.put("P_ARTIKELREFERENZNUMMER", dto.getCReferenznr());
		parameter.put("P_RECHT_LP_DARF_PREISE_SEHEN_EINKAUF", darfEinkaufspreisSehen);
		parameter.put("P_RECHT_LP_DARF_PREISE_SEHEN_VERKAUF", darfVerkaufspreisSehen);
		if (dVon != null) {
			parameter.put("P_VON", dVon);
		}
		if (dBis != null) {
			parameter.put("P_BIS", dBis);
		}
		if (lagerIId != null) {
			try {
				LagerDto lagerDto = getLagerFac().lagerFindByPrimaryKey(lagerIId);
				parameter.put("P_LAGER", lagerDto.getCNr());
			} catch (RemoteException ex1) {
				throwEJBExceptionLPRespectOld(ex1);
			}
		}

		initJRDS(parameter, LagerReportFac.REPORT_MODUL, LagerReportFac.REPORT_WARENBEWEGUNGSJOURNAL,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	public BigDecimal recalcGestehungspreisKomplett(Integer artikelIId, boolean debugFile) {

		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = null;
		if (FLRSessionFactory.isMSSQL()) {
			sQuery = "select  I_ID, B_ABGANG, SUM(N_MENGE) as N_MENGE, N_EINSTANDSPREIS, N_GESTEHUNGSPREIS, LAGER_I_ID, T_BUCHUNGSZEIT, CONVERT(CHAR(10), T_BELEGDATUM, 102) AS T_BELEGDATUM "
					+ "from WW_LAGERBEWEGUNG " + "where ARTIKEL_I_ID=? and B_ABGANG=0 and B_HISTORIE=0 "
					+ "group by LAGER_I_ID, CONVERT(CHAR(10), T_BELEGDATUM, 102), T_BUCHUNGSZEIT, I_ID, B_ABGANG, N_EINSTANDSPREIS, N_GESTEHUNGSPREIS "
					+ "union "
					+ "select I_ID, B_ABGANG, SUM(U.N_VERBRAUCHTEMENGE) as MENGE, N_EINSTANDSPREIS, U.N_GESTEHUNGSPREIS, LAGER_I_ID, T_BUCHUNGSZEIT, CONVERT(CHAR(10), B.T_BELEGDATUM, 102) AS T_BELEGDATUM "
					+ "from WW_LAGERBEWEGUNG B inner join WW_LAGERABGANGURSPRUNG U on U.I_LAGERBEWEGUNGID = B.I_ID_BUCHUNG "
					+ " where ARTIKEL_I_ID=? and B_ABGANG=1 and B_HISTORIE=0 "
					+ "group by LAGER_I_ID, CONVERT(CHAR(10), B.T_BELEGDATUM, 102), B.T_BUCHUNGSZEIT, B.I_ID, B_ABGANG, N_EINSTANDSPREIS, U.N_GESTEHUNGSPREIS "
					+ "order by LAGER_I_ID, T_BELEGDATUM, T_BUCHUNGSZEIT, B_ABGANG";
		} else {
			sQuery = "select  I_ID, B_ABGANG, SUM(N_MENGE) as N_MENGE, N_EINSTANDSPREIS, N_GESTEHUNGSPREIS, LAGER_I_ID, T_BUCHUNGSZEIT, CAST(T_BELEGDATUM AS CHAR(10)) AS T_BELEGDATUM "
					+ "from WW_LAGERBEWEGUNG " + "where ARTIKEL_I_ID=? and B_ABGANG=0 and B_HISTORIE=0 "
					+ "group by LAGER_I_ID, CAST(T_BELEGDATUM AS CHAR(10)), T_BUCHUNGSZEIT, I_ID, B_ABGANG, N_EINSTANDSPREIS, N_GESTEHUNGSPREIS "
					+ "union "
					+ "select I_ID, B_ABGANG, SUM(U.N_VERBRAUCHTEMENGE) as MENGE, N_EINSTANDSPREIS, U.N_GESTEHUNGSPREIS, LAGER_I_ID, T_BUCHUNGSZEIT, CAST(B.T_BELEGDATUM AS CHAR(10)) AS T_BELEGDATUM "
					+ "from WW_LAGERBEWEGUNG B inner join WW_LAGERABGANGURSPRUNG U on U.I_LAGERBEWEGUNGID = B.I_ID_BUCHUNG "
					+ " where ARTIKEL_I_ID=? and B_ABGANG=1 and B_HISTORIE=0 "
					+ "group by LAGER_I_ID, CAST(B.T_BELEGDATUM AS CHAR(10)), B.T_BUCHUNGSZEIT, B.I_ID, B_ABGANG, N_EINSTANDSPREIS, U.N_GESTEHUNGSPREIS "
					+ "order by LAGER_I_ID, T_BELEGDATUM, T_BUCHUNGSZEIT, B_ABGANG";
		}
		org.hibernate.Query query = session.createSQLQuery(sQuery);
		query.setParameter(0, artikelIId);
		query.setParameter(1, artikelIId);
		List<?> list = query.list();
		ArtikelGestehungspreisCalc agc = new ArtikelGestehungspreisCalc(list);
		int lageranzahl = agc.getLageranzahl();
		agc.doRecalc();
		if (debugFile) {
			agc.saveUpdateSQL("c:/temp/gp" + artikelIId + ".sql");
			agc.save2Csv("c:/temp/gp" + artikelIId + ".csv");
			agc.saveInfo("c:/temp/gp" + artikelIId + ".info");
		}
		return agc.getGestehungspreisNeu();
	}

	private void recalcGestehungspreisKomplett(Integer artikelIId, Integer lagerIId, Session session,
			boolean debugFile) {

		String sQuery = null;
		if (FLRSessionFactory.isMSSQL()) {
			sQuery = "select  I_ID, B_ABGANG, SUM(N_MENGE) as N_MENGE, N_EINSTANDSPREIS, N_GESTEHUNGSPREIS, LAGER_I_ID, T_BUCHUNGSZEIT, CONVERT(CHAR(10), T_BELEGDATUM, 102) AS T_BELEGDATUM "
					+ "from WW_LAGERBEWEGUNG "
					+ "where LAGER_I_ID=? and ARTIKEL_I_ID=? and B_ABGANG=0 and B_HISTORIE=0 "
					+ "group by LAGER_I_ID, CONVERT(CHAR(10), T_BELEGDATUM, 102), T_BUCHUNGSZEIT, I_ID, B_ABGANG, N_EINSTANDSPREIS, N_GESTEHUNGSPREIS "
					+ "union "
					+ "select I_ID, B_ABGANG, SUM(U.N_VERBRAUCHTEMENGE) as MENGE, N_EINSTANDSPREIS, U.N_GESTEHUNGSPREIS, LAGER_I_ID, T_BUCHUNGSZEIT, CONVERT(CHAR(10), B.T_BELEGDATUM, 102) AS T_BELEGDATUM "
					+ "from WW_LAGERBEWEGUNG B inner join WW_LAGERABGANGURSPRUNG U on U.I_LAGERBEWEGUNGID = B.I_ID_BUCHUNG "
					+ " where LAGER_I_ID=? and ARTIKEL_I_ID=? and B_ABGANG=1 and B_HISTORIE=0 "
					+ "group by LAGER_I_ID, CONVERT(CHAR(10), B.T_BELEGDATUM, 102), B.T_BUCHUNGSZEIT, B.I_ID, B_ABGANG, N_EINSTANDSPREIS, U.N_GESTEHUNGSPREIS "
					+ "order by LAGER_I_ID, T_BELEGDATUM, T_BUCHUNGSZEIT, B_ABGANG";
		} else {
			sQuery = "select  I_ID, B_ABGANG, SUM(N_MENGE) as N_MENGE, N_EINSTANDSPREIS, N_GESTEHUNGSPREIS, LAGER_I_ID, T_BUCHUNGSZEIT, CAST(T_BELEGDATUM AS CHAR(10)) AS T_BELEGDATUM "
					+ "from WW_LAGERBEWEGUNG "
					+ "where LAGER_I_ID=? and ARTIKEL_I_ID=? and B_ABGANG=0 and B_HISTORIE=0 "
					+ "group by LAGER_I_ID, CAST(T_BELEGDATUM AS CHAR(10)), T_BUCHUNGSZEIT, I_ID, B_ABGANG, N_EINSTANDSPREIS, N_GESTEHUNGSPREIS "
					+ "union "
					+ "select I_ID, B_ABGANG, SUM(U.N_VERBRAUCHTEMENGE) as MENGE, N_EINSTANDSPREIS, U.N_GESTEHUNGSPREIS, LAGER_I_ID, T_BUCHUNGSZEIT, CAST(B.T_BELEGDATUM AS CHAR(10)) AS T_BELEGDATUM "
					+ "from WW_LAGERBEWEGUNG B inner join WW_LAGERABGANGURSPRUNG U on U.I_LAGERBEWEGUNGID = B.I_ID_BUCHUNG "
					+ " where LAGER_I_ID=? and ARTIKEL_I_ID=? and B_ABGANG=1 and B_HISTORIE=0 "
					+ "group by LAGER_I_ID, CAST(B.T_BELEGDATUM AS CHAR(10)), B.T_BUCHUNGSZEIT, B.I_ID, B_ABGANG, N_EINSTANDSPREIS, U.N_GESTEHUNGSPREIS "
					+ "order by LAGER_I_ID, T_BELEGDATUM, T_BUCHUNGSZEIT, B_ABGANG";
		}
		org.hibernate.Query query = session.createSQLQuery(sQuery);
		query.setParameter(0, lagerIId);
		query.setParameter(1, artikelIId);
		query.setParameter(2, lagerIId);
		query.setParameter(3, artikelIId);
		List<?> list = query.list();
		ArtikelGestehungspreisCalc agc = new ArtikelGestehungspreisCalc(list);
		int lageranzahl = agc.getLageranzahl();
		agc.doRecalc();
		if (debugFile) {
			agc.saveUpdateSQL("c:/temp/gp" + artikelIId + ".sql");
			agc.save2Csv("c:/temp/gp" + artikelIId + ".csv");
			agc.saveInfo("c:/temp/gp" + artikelIId + ".info");
		}
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

	public Object getFieldValue(JRField jRField) throws JRException {
		Object value = null;

		String fieldName = jRField.getName();
		if (sAktuellerReport.equals(LagerFac.REPORT_ZAEHLLISTE)) {

			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_ZAEHLLISTE_ARTIKELNUMMER];
			} else if ("Referenznummer".equals(fieldName)) {
				value = data[index][REPORT_ZAEHLLISTE_REFERENZNUMMER];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ZAEHLLISTE_ARTIKELBEZEICHNUNG];
			} else if ("Artikelzusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ZAEHLLISTE_ARTIELZUSATZBEZEICHNUNG];
			} else if ("Artikelzusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_ZAEHLLISTE_ARTIELZUSATZBEZEICHNUNG2];
			} else if ("Mengenart".equals(fieldName)) {
				value = data[index][REPORT_ZAEHLLISTE_MENGENART];
			} else if ("Lagerort".equals(fieldName)) {
				value = data[index][REPORT_ZAEHLLISTE_LAGERORT];
			} else if ("Seriennummerntragend".equals(fieldName)) {
				value = data[index][REPORT_ZAEHLLISTE_SNRTRAGEND];
			} else if ("Chargennummerntragend".equals(fieldName)) {
				value = data[index][REPORT_ZAEHLLISTE_CHNRTRAGEND];
			} else if ("Seriennrchargennr".equals(fieldName)) {
				value = data[index][REPORT_ZAEHLLISTE_SERIENNRCHARGENNR];
			} else if ("Barcodedruckbar".equals(fieldName)) {
				value = data[index][REPORT_ZAEHLLISTE_BARCODE_DRUCKBAR];
			} else if ("F_BREITE".equals(fieldName)) {
				value = data[index][REPORT_ZAEHLLISTE_BREITE];
			} else if ("F_HOEHE".equals(fieldName)) {
				value = data[index][REPORT_ZAEHLLISTE_HOEHE];
			} else if ("F_TIEFE".equals(fieldName)) {
				value = data[index][REPORT_ZAEHLLISTE_TIEFE];
			} else if ("F_BREITETEXT".equals(fieldName)) {
				value = data[index][REPORT_ZAEHLLISTE_BREITETEXT];
			} else if ("F_ARTIKELBAUFORM".equals(fieldName)) {
				value = data[index][REPORT_ZAEHLLISTE_ARTIKELBAUFORM];
			} else if ("F_VERPACKUNGSART".equals(fieldName)) {
				value = data[index][REPORT_ZAEHLLISTE_VERPACKUNGSART];
			} else if ("F_VERPACKUNGSMENGE".equals(fieldName)) {
				value = data[index][REPORT_ZAEHLLISTE_VERPACKUNGMENGE];
			} else if ("Lagerstand".equals(fieldName)) {
				value = data[index][REPORT_ZAEHLLISTE_LAGERSTAND];
			}

		} else if (sAktuellerReport.equals(LagerReportFac.REPORT_INDIREKTE_WARENEINSATZ_STATISTIK)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_INDIREKTE_WE_STATISTIK_ARTIKELNUMMER];
			} else if ("Artikelreferenznummer".equals(fieldName)) {
				value = data[index][REPORT_INDIREKTE_WE_STATISTIK_ARTIKELREFERENZNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_INDIREKTE_WE_STATISTIK_BEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_INDIREKTE_WE_STATISTIK_ZUSATZBEZEICHNUNG];
			} else if ("Ebene".equals(fieldName)) {
				value = data[index][REPORT_INDIREKTE_WE_STATISTIK_EBENE];
			} else if ("Lieferbeleg".equals(fieldName)) {
				value = data[index][REPORT_INDIREKTE_WE_STATISTIK_LIEFERBELEG];
			} else if ("Verbrauchsbeleg".equals(fieldName)) {
				value = data[index][REPORT_INDIREKTE_WE_STATISTIK_VERBRAUCHSBELEG];
			} else if ("Wareneingangsbeleg".equals(fieldName)) {
				value = data[index][REPORT_INDIREKTE_WE_STATISTIK_WARENEINGANGSBELEG];
			} else if ("Kunde".equals(fieldName)) {
				value = data[index][REPORT_INDIREKTE_WE_STATISTIK_KUNDE];
			} else if ("VKPreisLieferbeleg".equals(fieldName)) {
				value = data[index][REPORT_INDIREKTE_WE_STATISTIK_VKPREIS_LIEFERBELEG];
			} else if ("Lieferant".equals(fieldName)) {
				value = data[index][REPORT_INDIREKTE_WE_STATISTIK_LIEFERANT];
			} else if ("Lieferdatum".equals(fieldName)) {
				value = data[index][REPORT_INDIREKTE_WE_STATISTIK_LIEFERDATUM];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_INDIREKTE_WE_STATISTIK_MENGE];
			} else if ("Einstandspreis".equals(fieldName)) {
				value = data[index][REPORT_INDIREKTE_WE_STATISTIK_EINSTANDSPREIS];
			} else if ("Gestehungspreis".equals(fieldName)) {
				value = data[index][REPORT_INDIREKTE_WE_STATISTIK_GESTEHUNGSPREIS];
			} else if ("VKPreis".equals(fieldName)) {
				value = data[index][REPORT_INDIREKTE_WE_STATISTIK_VKPREIS];
			} else if ("Kundenartikelnummer".equals(fieldName)) {
				value = data[index][REPORT_INDIREKTE_WE_STATISTIK_KUNDENARTIKELNUMMER];
			} else if ("Verbrauchsdatum".equals(fieldName)) {
				value = data[index][REPORT_INDIREKTE_WE_STATISTIK_VERBRAUCHSDATUM];
			} else if ("Wareneingangsdatum".equals(fieldName)) {
				value = data[index][REPORT_INDIREKTE_WE_STATISTIK_WARENEINGANGSDATUM];
			}

		} else if (sAktuellerReport.equals(LagerReportFac.REPORT_WARENBEWEGUNGSJOURNAL)) {
			if ("Abbuchungszeit".equals(fieldName)) {
				value = data[index][REPORT_WARENBEWEGUNGSJOURNAL_ABBUCHUNGSZEITPUNKT];
			} else if ("Abbuchungfirma".equals(fieldName)) {
				value = data[index][REPORT_WARENBEWEGUNGSJOURNAL_ABGANGFIRMAKOMMENTAR];
			} else if ("Abgangsbeleg".equals(fieldName)) {
				value = data[index][REPORT_WARENBEWEGUNGSJOURNAL_ABGANGSBELEG];
			} else if ("MandantAbgangsbeleg".equals(fieldName)) {
				value = data[index][REPORT_WARENBEWEGUNGSJOURNAL_MANDANT_ABGANGSBELEG];
			} else if ("Abgangsbelegdatum".equals(fieldName)) {
				value = data[index][REPORT_WARENBEWEGUNGSJOURNAL_ABGANGSDATUM];
			} else if ("Abgangsmenge".equals(fieldName)) {
				value = data[index][REPORT_WARENBEWEGUNGSJOURNAL_ABGANGSMENGE];
			} else if ("Einstandspreis".equals(fieldName)) {
				value = data[index][REPORT_WARENBEWEGUNGSJOURNAL_EINSTANDSPREIS];
			} else if ("Gestpreis".equals(fieldName)) {
				value = data[index][REPORT_WARENBEWEGUNGSJOURNAL_GESTPREIS];
			} else if ("Lager".equals(fieldName)) {
				value = data[index][REPORT_WARENBEWEGUNGSJOURNAL_LAGER];
			} else if ("Zugangsmenge".equals(fieldName)) {
				value = data[index][REPORT_WARENBEWEGUNGSJOURNAL_ZUGANGSMENGE];
			} else if ("Snrchnr".equals(fieldName)) {
				value = data[index][REPORT_WARENBEWEGUNGSJOURNAL_SNR];
			} else if ("Verkaufspreis".equals(fieldName)) {
				value = data[index][REPORT_WARENBEWEGUNGSJOURNAL_VKPREIS];
			} else if ("Zubuchungszeit".equals(fieldName)) {
				value = data[index][REPORT_WARENBEWEGUNGSJOURNAL_ZUBUCHUNGSZEITPUNKT];
			} else if ("Zubuchungfirma".equals(fieldName)) {
				value = data[index][REPORT_WARENBEWEGUNGSJOURNAL_ZUGANGFIRMAKOMMENTAR];
			} else if ("Zugangsbeleg".equals(fieldName)) {
				value = data[index][REPORT_WARENBEWEGUNGSJOURNAL_ZUGANGSBELEG];
			} else if ("MandantZugangsbeleg".equals(fieldName)) {
				value = data[index][REPORT_WARENBEWEGUNGSJOURNAL_MANDANT_ZUGANGSBELEG];
			} else if ("Zubuchungsbelegdatum".equals(fieldName)) {
				value = data[index][REPORT_WARENBEWEGUNGSJOURNAL_ZUGANGSDATUM];
			} else if ("F_HERSTELLER".equals(fieldName)) {
				value = data[index][REPORT_WARENBEWEGUNGSJOURNAL_HERSTELLER];
			} else if ("F_URSPRUNGSLAND".equals(fieldName)) {
				value = data[index][REPORT_WARENBEWEGUNGSJOURNAL_URSPRUNGSLAND];
			} else if ("F_WE_REFERENZ".equals(fieldName)) {
				value = data[index][REPORT_WARENBEWEGUNGSJOURNAL_WE_REFERENZ];
			} else if ("F_ABGANG_WER_HAT_GEBUCHT".equals(fieldName)) {
				value = data[index][REPORT_WARENBEWEGUNGSJOURNAL_ABGANG_WER_HAT_GEBUCHT];
			} else if ("F_ZUGANG_WER_HAT_GEBUCHT".equals(fieldName)) {
				value = data[index][REPORT_WARENBEWEGUNGSJOURNAL_ZUGANG_WER_HAT_GEBUCHT];
			} else if ("Version".equals(fieldName)) {
				value = data[index][REPORT_WARENBEWEGUNGSJOURNAL_VERSION];
			}

		} else if (sAktuellerReport.equals(LagerReportFac.REPORT_LAGERSTANDLISTE)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_IDENTNUMMER];
			} else if ("Lagerstand".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_LAGERSTAND];
			} else if ("Lagerort".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_LAGERORT];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_EINHEIT];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_BEZEICHNUNG];
			} else if ("Gestehungspreis".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_GESTEHUNGSPREIS];
			} else if ("Einkaufspreis".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_LIEF1PREIS];
			} else if ("Lagerwert".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_LAGERWERT];
			} else if ("LagerwertMaterialzuschlag".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_LAGERWERT];
			} else if ("Reserviertemenge".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_RESERVIERTE_MENGE];
			} else if ("Reservierterwert".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_RESERVIERTER_WERT];
			} else if ("Freierwert".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_FREIER_WERT];
			} else if ("Snrchnrauflager".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_SNRCHNRAUFLAGER];
			} else if ("SubreportSnrchnrauflager".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_SUBREPORT_SNRCHNRAUFLAGER];
			} else if ("Artikelgruppe".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_ARTIKELGRUPPE];
			} else if ("Shopgruppe".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_SHOPGRUPPE];
			} else if ("Artikelklasse".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_ARTIKELKLASSE];
			} else if ("Stueckliste".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_STUECKLISTE];
			} else if ("Hilfsstueckliste".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_HILFSSTUECKLISTE];
			} else if ("Artikelart".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_ARTIKELART];
			} else if ("Abgewertetergestehungspreis".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_GESTEHUNGSPREIS_ABGEWERTET];
			} else if ("Versteckt".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_VERSTECKT];
			}

			else if ("Kurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_KURZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_ZUSATZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_ZUSATZBEZEICHNUNG2];
			} else if ("Referenznummer".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_REFERENZNUMMER];
			} else if ("Verpackungsmenge".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_VERPACKUNGSMENGE];
			} else if ("Bestellmengeneinheit".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_BESTELLMENGENEINHEIT];
			} else if ("Umrechnungsfaktor".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_UMRECHNUNGFAKTOR];
			} else if ("Lager".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_LAGER];
			} else if ("Lagerart".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_LAGERART];
			} else if ("Konsignationslager".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_KONSIGNATIONSLAGER];
			} else if ("Fehlmenge".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_FEHLMENGE];
			} else if ("Fehlmengenwert".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_FEHLMENGEN_WERT];
			} else if ("FreierwertInklFehlmenge".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_FREIER_WERT_INKL_FEHLMENGE];
			} else if ("SubreportBelege".equals(fieldName)) {
				value = data[index][REPORT_LAGERSTANDLISTE_SUBREPORT_BELEGE];
			}

		}

		else if (sAktuellerReport.equals(LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK)
				|| sAktuellerReport.equals(LagerReportFac.REPORT_KUNDEDBSTATISTIK)) {
			if ("Kunde".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_KUNDE];
			} else if ("Umsatz".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_UMSATZ];
			} else if ("Deckungsbeitrag".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_DECKUNGSBEITRAG];
			} else if ("ABC".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_ABCKLASSE];
			} else if ("Spalte1".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_SPALTE1];
			} else if ("Spalte2".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_SPALTE2];
			} else if ("Spalte3".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_SPALTE3];
			} else if ("Spalte4".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_SPALTE4];
			} else if ("Spalte5".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_SPALTE5];
			} else if ("Spalte6".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_SPALTE6];
			} else if ("Spalte7".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_SPALTE7];
			} else if ("Spalte8".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_SPALTE8];
			} else if ("Spalte1DB".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_SPALTE1DB];
			} else if ("Spalte2DB".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_SPALTE2DB];
			} else if ("Spalte3DB".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_SPALTE3DB];
			} else if ("Spalte4DB".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_SPALTE4DB];
			} else if ("Spalte5DB".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_SPALTE5DB];
			} else if ("Spalte6DB".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_SPALTE6DB];
			} else if ("Spalte7DB".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_SPALTE7DB];
			} else if ("Spalte8DB".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_SPALTE8DB];
			} else if ("Kundengruppierung".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_KUNDENGRUPPIERUNG];
			} else if ("Erstumsatz".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_ERSTUMSATZ];
			} else if ("DatumErstumsatz".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_DATUM_ERSTUMSATZ];
			} else if ("DatumLetztumsatz".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_DATUM_LETZTUMSATZ];
			} else if ("Zahlungsziel".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_ZAHLUNGSZIEL];
			} else if ("Lieferart".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_LIEFERART];
			} else if ("Spediteur".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_SPEDITEUR];
			} else if ("Lkz".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_LKZ];
			} else if ("Plz".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_PLZ];
			} else if ("Kundennummer".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_KUNDENNUMMER];
			} else if ("Kreditlimit".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_KREDITLIMIT];
			} else if ("Iln".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_ILN];
			} else if ("KundeIId".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_KUNDE_I_ID];
			} else if ("PartnerIId".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_PARTNER_I_ID];
			} else if ("ProvisionsempfaengerKurzzeichen".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_PROVISIONSEMPFAENGER_KURZZEICHEN];
			} else if ("ProvisionsempfaengerName".equals(fieldName)) {
				value = data[index][REPORT_KUNDEUMSATZSTATISTIK_PROVISIONSEMPFAENGER_NAME];
			}
		} else if (sAktuellerReport.equals(LagerReportFac.REPORT_LIEFERANTUMSATZSTATISTIK)) {
			if ("Lieferant".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTUMSATZSTATISTIK_LIEFERANT];
			} else if ("Umsatz".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTUMSATZSTATISTIK_UMSATZ];
			} else if ("ABC".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTUMSATZSTATISTIK_ABCKLASSE];
			} else if ("Spalte1".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTUMSATZSTATISTIK_SPALTE1];
			} else if ("Spalte2".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTUMSATZSTATISTIK_SPALTE2];
			} else if ("Spalte3".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTUMSATZSTATISTIK_SPALTE3];
			} else if ("Spalte4".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTUMSATZSTATISTIK_SPALTE4];
			} else if ("Spalte5".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTUMSATZSTATISTIK_SPALTE5];
			} else if ("Spalte6".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTUMSATZSTATISTIK_SPALTE6];
			} else if ("Spalte7".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTUMSATZSTATISTIK_SPALTE7];
			} else if ("Spalte8".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTUMSATZSTATISTIK_SPALTE8];
			} else if ("Lieferantgruppierung".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTUMSATZSTATISTIK_LIEFERANTGRUPPIERUNG];
			} else if ("Zahlungsziel".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTUMSATZSTATISTIK_ZAHLUNGSZIEL];
			} else if ("Lieferart".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTUMSATZSTATISTIK_LIEFERART];
			} else if ("Spediteur".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTUMSATZSTATISTIK_SPEDITEUR];
			} else if ("Lkz".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTUMSATZSTATISTIK_LKZ];
			} else if ("Plz".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTUMSATZSTATISTIK_PLZ];
			} else if ("Ort".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTUMSATZSTATISTIK_ORT];
			} else if ("LIEFERANT_I_ID".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTUMSATZSTATISTIK_LIEFERANT_I_ID];
			}
		}

		else if (sAktuellerReport.equals(LagerReportFac.REPORT_LADENHUETER)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_LADENHUETER_ARTIKELNUMMER];
			} else if ("Artikelreferenznummer".equals(fieldName)) {
				value = data[index][REPORT_LADENHUETER_ARTIKELREFERENZNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_LADENHUETER_BEZEICHNUNG];
			} else if ("LagerstandHeute".equals(fieldName)) {
				value = data[index][REPORT_LADENHUETER_LAGERSTAND_HEUTE];
			} else if ("LagerstandZumStichtag".equals(fieldName)) {
				value = data[index][REPORT_LADENHUETER_LAGERSTAND_ZUMSTICHTAG];
			} else if ("Gestpreis".equals(fieldName)) {
				value = data[index][REPORT_LADENHUETER_GESTPREIS];
			} else if ("Einkaufspreis".equals(fieldName)) {
				value = data[index][REPORT_LADENHUETER_EINKAUFSPREIS];
			} else if ("Artikelgruppe".equals(fieldName)) {
				value = data[index][REPORT_LADENHUETER_ARTIKELGRUPPE];
			} else if ("Artikelklasse".equals(fieldName)) {
				value = data[index][REPORT_LADENHUETER_ARTIKELKLASSE];
			} else if ("Einstandspreis".equals(fieldName)) {
				value = data[index][REPORT_LADENHUETER_EINSTANDSPREIS];
			} else if ("Reservierungen".equals(fieldName)) {
				value = data[index][REPORT_LADENHUETER_RESERVIERUNGEN];
			} else if ("Fehlmengen".equals(fieldName)) {
				value = data[index][REPORT_LADENHUETER_FEHLMENGEN];
			} else if ("Versteckt".equals(fieldName)) {
				value = data[index][REPORT_LADENHUETER_VERSTECHT];
			} else if ("Stueckliste".equals(fieldName)) {
				value = data[index][REPORT_LADENHUETER_STUECKLISTE];
			} else if ("Hilfsstueckliste".equals(fieldName)) {
				value = data[index][REPORT_LADENHUETER_HILFSSTUECKLISTE];
			} else if ("Rahmenbedarfe".equals(fieldName)) {
				value = data[index][REPORT_LADENHUETER_RAHMENDETAILBEDARFE];
			} else if ("Rahmenreservierungen".equals(fieldName)) {
				value = data[index][REPORT_LADENHUETER_RAHMENRESERVIERUNGEN];
			} else if ("Artikelart".equals(fieldName)) {
				value = data[index][REPORT_LADENHUETER_ARTIKELART];
			} else if ("Bevorzugterlieferant".equals(fieldName)) {
				value = data[index][REPORT_LADENHUETER_BEVORZUGTERLIEFERANT];
			} else if ("Lieferantartikelnummer".equals(fieldName)) {
				value = data[index][REPORT_LADENHUETER_LIEFERANTARTIKELNUMMER];
			}
		} else if (sAktuellerReport.equals(LagerReportFac.REPORT_HITLISTE)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_HILISTE_ARTIKELNUMMER];
			} else if ("Artikelreferenznummer".equals(fieldName)) {
				value = data[index][REPORT_HILISTE_ARTIKELREFERENZNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_HILISTE_BEZEICHNUNG];
			} else if ("Verkauftemenge".equals(fieldName)) {
				value = data[index][REPORT_HILISTE_VERKAUFTEMENGE];
			} else if ("DurchschnittlicheWBZ".equals(fieldName)) {
				value = data[index][REPORT_HILISTE_DURCHSCHNITTLICHE_WBZ];
			} else if ("WBZLief1".equals(fieldName)) {
				value = data[index][REPORT_HILISTE_WBZ_LIEF1];
			} else if ("Durchschnittlichervkpreis".equals(fieldName)) {
				value = data[index][REPORT_HILISTE_DURCHSCHNITTLICHERVKPREIS];
			} else if ("Artikelgruppe".equals(fieldName)) {
				value = data[index][REPORT_HILISTE_ARTIKELGRUPPE];
			} else if ("Shopgruppe".equals(fieldName)) {
				value = data[index][REPORT_HILISTE_SHOPGRUPPE];
			} else if ("AnzahlTeilerledigterLose".equals(fieldName)) {
				value = data[index][REPORT_HILISTE_ANZAHL_TEILERLEDIGTER_LOSE];
			} else if ("Artikelklasse".equals(fieldName)) {
				value = data[index][REPORT_HILISTE_ARTIKELKLASSE];
			} else if ("Durchschnittlichergestpreis".equals(fieldName)) {
				value = data[index][REPORT_HILISTE_DURCHSCHNITTLICHERGESTPREIS];
			} else if ("Dbwert".equals(fieldName)) {
				value = data[index][REPORT_HILISTE_DBWERT];
			} else if ("Versteckt".equals(fieldName)) {
				value = data[index][REPORT_HILISTE_VERSTECKT];
			} else if ("Jahresmenge".equals(fieldName)) {
				value = data[index][REPORT_HILISTE_JAHRESMENGE];
			} else if ("Lagermindeststand".equals(fieldName)) {
				value = data[index][REPORT_HILISTE_LAGERMINDESTSTAND];
			} else if ("Lagersollstand".equals(fieldName)) {
				value = data[index][REPORT_HILISTE_LAGERSOLLSTAND];
			} else if ("Lagerstand".equals(fieldName)) {
				value = data[index][REPORT_HILISTE_LAGERSTAND];
			}

			else if ("LSNichtverrechenbarDBWert".equals(fieldName)) {
				value = data[index][REPORT_HILISTE_LS_NICHT_VERRECHENBAR_DBWERT];
			} else if ("LSNichtverrechenbarGestWert".equals(fieldName)) {
				value = data[index][REPORT_HILISTE_LS_NICHT_VERRECHENBAR_GESTWERT];
			} else if ("LSNichtverrechenbarMenge".equals(fieldName)) {
				value = data[index][REPORT_HILISTE_LS_NICHT_VERRECHENBAR_MENGE];
			}

			else if ("LSVerrechenbarZiellagerNichtKonsiDBWert".equals(fieldName)) {
				value = data[index][REPORT_HILISTE_LS_VERRECHENBAR_MIT_ZIELLAGER_OHNE_KONSI_DBWERT];
			} else if ("LSVerrechenbarZiellagerNichtKonsiGestwert".equals(fieldName)) {
				value = data[index][REPORT_HILISTE_LS_VERRECHENBAR_MIT_ZIELLAGER_OHNE_KONSI_GESTWERT];
			} else if ("LSVerrechenbarZiellagerNichtKonsiMenge".equals(fieldName)) {
				value = data[index][REPORT_HILISTE_LS_VERRECHENBAR_MIT_ZIELLAGER_OHNE_KONSI_MENGE];
			}

			else if ("LSVerrechenbarZiellagerIstKonsiDBWert".equals(fieldName)) {
				value = data[index][REPORT_HILISTE_LS_VERRECHENBAR_MIT_ZIELLAGER_UND_KONSI_DBWERT];
			} else if ("LSVerrechenbarZiellagerIstGestwert".equals(fieldName)) {
				value = data[index][REPORT_HILISTE_LS_VERRECHENBAR_MIT_ZIELLAGER_UND_KONSI_GESTWERT];
			} else if ("LSVerrechenbarZiellagerIstKonsiMenge".equals(fieldName)) {
				value = data[index][REPORT_HILISTE_LS_VERRECHENBAR_MIT_ZIELLAGER_UND_KONSI_MENGE];
			}

			else if ("LSVerrechenbarDBWert".equals(fieldName)) {
				value = data[index][REPORT_HILISTE_LS_VERRECHENBAR_OHNE_ZIELLAGER_DBWERT];
			} else if ("LSVerrechenbarGestWert".equals(fieldName)) {
				value = data[index][REPORT_HILISTE_LS_VERRECHENBAR_OHNE_ZIELLAGER_GESTWERT];
			} else if ("LSVerrechenbarMenge".equals(fieldName)) {
				value = data[index][REPORT_HILISTE_LS_VERRECHENBAR_OHNE_ZIELLAGER_MENGE];
			}

		} else if (sAktuellerReport.equals(LagerReportFac.REPORT_ARTIKLEGRUPPEN)) {
			if ("Artikelgruppe".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELGRUPPE_ARTIKELGRUPPE];
			} else if ("Vkwert".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELGRUPPE_VKWERT];
			} else if ("Gestwert".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELGRUPPE_GESTWERT];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELGRUPPE_MENGE];
			} else if ("Konto".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELGRUPPE_KONTO];
			} else if ("Kontobezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELGRUPPE_KONTOBEZEICHNUNG];
			} else if ("Ergebnisgruppe".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELGRUPPE_ERGEBNISGRUPPE];
			}

		} else if (sAktuellerReport.equals(LagerReportFac.REPORT_HANDLAGERBEWEGUNGEN)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_HANDLAGERBEWEGUNGEN_ARTIKELNUMMER];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_HANDLAGERBEWEGUNGEN_ARTIKELBEZEICHNUNG];
			} else if ("Artikelzusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_HANDLAGERBEWEGUNGEN_ARTIKELZUSATZBEZEICHNUNG];
			} else if ("Artikelzusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_HANDLAGERBEWEGUNGEN_ARTIKELZUSATZBEZEICHNUNG2];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_HANDLAGERBEWEGUNGEN_MENGE];
			} else if ("Abgang".equals(fieldName)) {
				value = data[index][REPORT_HANDLAGERBEWEGUNGEN_ABGANG];
			} else if ("Vkpreis".equals(fieldName)) {
				value = data[index][REPORT_HANDLAGERBEWEGUNGEN_VKPREIS];
			} else if ("Einstandspreis".equals(fieldName)) {
				value = data[index][REPORT_HANDLAGERBEWEGUNGEN_EINSTANDSPREIS];
			} else if ("Buchungszeit".equals(fieldName)) {
				value = data[index][REPORT_HANDLAGERBEWEGUNGEN_BUCHUNGSZEIT];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][REPORT_HANDLAGERBEWEGUNGEN_KOMMENTAR];
			} else if ("Lager".equals(fieldName)) {
				value = data[index][REPORT_HANDLAGERBEWEGUNGEN_LAGER];
			} else if ("PersonGebucht".equals(fieldName)) {
				value = data[index][REPORT_HANDLAGERBEWEGUNGEN_PERSON_GEBUCHT];
			} else if ("SubreportSnrChnr".equals(fieldName)) {

				value = data[index][REPORT_HANDLAGERBEWEGUNGEN_SUBREPORT_SNRCHNR];
			}

		} else if (sAktuellerReport.equals(LagerReportFac.REPORT_MATERIALBEDARFSVORSCHAU)) {
			if ("Artikelgruppe".equals(fieldName)) {
				value = data[index][REPORT_MATERIALBEDARFSVORSCHAU_ARTIKELGRUPPE];
			} else if ("MengeAngelegteAuftraege".equals(fieldName)) {
				value = data[index][REPORT_MATERIALBEDARFSVORSCHAU_MENGE_ANGELEGTE_AUFTRAEGE];
			} else if ("MengeOffeneAngebote".equals(fieldName)) {
				value = data[index][REPORT_MATERIALBEDARFSVORSCHAU_MENGE_OFFENE_ANGEBOTE];
			} else if ("MengeOffeneRahmenauftraege".equals(fieldName)) {
				value = data[index][REPORT_MATERIALBEDARFSVORSCHAU_MENGE_OFFENE_RAHMENAUFTRAEGE];
			} else if ("MengeVergangenheit".equals(fieldName)) {
				value = data[index][REPORT_MATERIALBEDARFSVORSCHAU_MENGE_VERGANGENHEIT];
			}
		} else if (sAktuellerReport.equals(LagerReportFac.REPORT_SHOPGRUPPEN)) {
			if ("Shopgruppe".equals(fieldName)) {
				value = data[index][REPORT_SHOPGRUPPE_SHOPGRUPPE];
			} else if ("Vkwert".equals(fieldName)) {
				value = data[index][REPORT_SHOPGRUPPE_VKWERT];
			} else if ("Gestwert".equals(fieldName)) {
				value = data[index][REPORT_SHOPGRUPPE_GESTWERT];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_SHOPGRUPPE_MENGE];
			}
		} else if (sAktuellerReport.equals(LagerReportFac.REPORT_MINDESTHALTBARKEIT)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_MINDESTHALTBARKEIT_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_MINDESTHALTBARKEIT_BEZEICHNUNG];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_MINDESTHALTBARKEIT_MENGE];
			} else if ("Haltbarmonate".equals(fieldName)) {
				value = data[index][REPORT_MINDESTHALTBARKEIT_HALTBARMONATE];
			} else if ("Haltbartage".equals(fieldName)) {
				value = data[index][REPORT_MINDESTHALTBARKEIT_HALTBARTAGE];
			} else if ("Chargennummer".equals(fieldName)) {
				value = data[index][REPORT_MINDESTHALTBARKEIT_CHARGENNUMMER];
			}
		} else if (sAktuellerReport.equals(LagerReportFac.REPORT_UMBUCHUNGSBELEG)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_UMBUCHUNGSBELEG_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_UMBUCHUNGSBELEG_BEZEICHNUNG];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_UMBUCHUNGSBELEG_MENGE];
			} else if ("Datum".equals(fieldName)) {
				value = data[index][REPORT_UMBUCHUNGSBELEG_DATUM];
			} else if ("Vonlager".equals(fieldName)) {
				value = data[index][REPORT_UMBUCHUNGSBELEG_VONLAGER];
			} else if ("Nachlager".equals(fieldName)) {
				value = data[index][REPORT_UMBUCHUNGSBELEG_NACHLAGER];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][REPORT_UMBUCHUNGSBELEG_EINHEIT];
			} else if ("Serienchargennr".equals(fieldName)) {
				value = data[index][REPORT_UMBUCHUNGSBELEG_SERIENNR];
			} else if ("Kurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_UMBUCHUNGSBELEG_KURZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_UMBUCHUNGSBELEG_ZUSATZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_UMBUCHUNGSBELEG_ZUSATZBEZEICHNUNG2];
			} else if ("Lagerort".equals(fieldName)) {
				value = data[index][REPORT_UMBUCHUNGSBELEG_LAGERORT];
			}

		} else if (sAktuellerReport.equals(LagerReportFac.REPORT_LAGERBUCHUNGSBELEG)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_LAGERBUCHUNGSBELEG_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_LAGERBUCHUNGSBELEG_BEZEICHNUNG];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_LAGERBUCHUNGSBELEG_MENGE];
			} else if ("Datum".equals(fieldName)) {
				value = data[index][REPORT_LAGERBUCHUNGSBELEG_DATUM];
			} else if ("Lager".equals(fieldName)) {
				value = data[index][REPORT_LAGERBUCHUNGSBELEG_LAGER];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][REPORT_LAGERBUCHUNGSBELEG_EINHEIT];
			} else if ("Serienchargennr".equals(fieldName)) {
				value = data[index][REPORT_LAGERBUCHUNGSBELEG_SERIENNR];
			} else if ("Kurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_LAGERBUCHUNGSBELEG_KURZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_LAGERBUCHUNGSBELEG_ZUSATZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_LAGERBUCHUNGSBELEG_ZUSATZBEZEICHNUNG2];
			} else if ("Lagerort".equals(fieldName)) {
				value = data[index][REPORT_LAGERBUCHUNGSBELEG_LAGERORT];
			} else if ("Index".equals(fieldName)) {
				value = data[index][REPORT_LAGERBUCHUNGSBELEG_INDEX];
			} else if ("i_id_Buchung".equals(fieldName)) {
				value = data[index][REPORT_LAGERBUCHUNGSBELEG_I_ID_BUCHUNG];
			} else if ("Referenznummer".equals(fieldName)) {
				value = data[index][REPORT_LAGERBUCHUNGSBELEG_REFERENZNUMMER];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][REPORT_LAGERBUCHUNGSBELEG_KOMMENTAR];
			} else if ("Exemplar".equals(fieldName)) {
				value = data[index][REPORT_LAGERBUCHUNGSBELEG_EXEMPLAR];
			} else if ("ExemplareGesamt".equals(fieldName)) {
				value = data[index][REPORT_LAGERBUCHUNGSBELEG_EXEMPLAREGESAMT];
			}

		} else if (sAktuellerReport.equals(LagerReportFac.REPORT_CHARGENEIGENSCHAFTEN)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_CHARGENEIGENSCHAFTEN_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_CHARGENEIGENSCHAFTEN_BEZEICHNUNG];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_CHARGENEIGENSCHAFTEN_MENGE];
			} else if ("Datum".equals(fieldName)) {
				value = data[index][REPORT_CHARGENEIGENSCHAFTEN_DATUM];
			} else if ("Lager".equals(fieldName)) {
				value = data[index][REPORT_CHARGENEIGENSCHAFTEN_LAGER];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][REPORT_CHARGENEIGENSCHAFTEN_EINHEIT];
			} else if ("Serienchargennr".equals(fieldName)) {
				value = data[index][REPORT_CHARGENEIGENSCHAFTEN_SERIENNR];
			} else if ("Kurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_CHARGENEIGENSCHAFTEN_KURZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_CHARGENEIGENSCHAFTEN_ZUSATZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_CHARGENEIGENSCHAFTEN_ZUSATZBEZEICHNUNG2];
			} else if ("Lagerort".equals(fieldName)) {
				value = data[index][REPORT_CHARGENEIGENSCHAFTEN_LAGERORT];
			} else if ("Index".equals(fieldName)) {
				value = data[index][REPORT_CHARGENEIGENSCHAFTEN_INDEX];
			} else if ("i_id_Buchung".equals(fieldName)) {
				value = data[index][REPORT_CHARGENEIGENSCHAFTEN_I_ID_BUCHUNG];
			} else if ("artikelsnrchnr_i_id".equals(fieldName)) {
				value = data[index][REPORT_CHARGENEIGENSCHAFTEN_ARTIKELSNRCHNR_I_ID];
			} else if ("artgru_i_id".equals(fieldName)) {
				value = data[index][REPORT_CHARGENEIGENSCHAFTEN_ARTGRU_I_ID];
			} else if ("Referenznummer".equals(fieldName)) {
				value = data[index][REPORT_CHARGENEIGENSCHAFTEN_REFERENZNUMMER];
			}

		} else if (sAktuellerReport.equals(LagerReportFac.REPORT_GESTPREISUEBERMINVK)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_GESTPREISUNTERMINVK_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_GESTPREISUNTERMINVK_BEZEICHNUNG];
			} else if ("Gestpreis".equals(fieldName)) {
				value = data[index][REPORT_GESTPREISUNTERMINVK_GESTPREIS];
			} else if ("VKPreis".equals(fieldName)) {
				value = data[index][REPORT_GESTPREISUNTERMINVK_VKPREIS];
			} else if ("Materialzuschlag".equals(fieldName)) {
				value = data[index][REPORT_GESTPREISUNTERMINVK_MATERIALZUSCHLAG];
			} else if ("Artikelart".equals(fieldName)) {
				value = data[index][REPORT_GESTPREISUNTERMINVK_ARTIKELART];
			} else if ("Stuecklistenart".equals(fieldName)) {
				value = data[index][REPORT_GESTPREISUNTERMINVK_STUECKLISTENART];
			} else if ("Lagerstand".equals(fieldName)) {
				value = data[index][REPORT_GESTPREISUNTERMINVK_LAGERSTAND];
			} else if ("Artikelreferenznummer".equals(fieldName)) {
				value = data[index][REPORT_GESTPREISUNTERMINVK_ARTIKELREFERENZNUMMER];
			}
		} else if (sAktuellerReport.equals(LagerReportFac.REPORT_WARENENTNAHMESTATISTIK)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_WARENENTNAHMESTATISTIK_ARTIKELNUMMER];
			} else if ("Artikelart".equals(fieldName)) {
				value = data[index][REPORT_WARENENTNAHMESTATISTIK_ARTIKELART];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_WARENENTNAHMESTATISTIK_BEZEICHNUNG];
			} else if ("Kurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_WARENENTNAHMESTATISTIK_KURZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_WARENENTNAHMESTATISTIK_ZUSATZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_WARENENTNAHMESTATISTIK_ZUSATZBEZEICHNUNG2];
			} else if ("LagerstandBeginn".equals(fieldName)) {
				value = data[index][REPORT_WARENENTNAHMESTATISTIK_LAGERSTAND_BEGINN];
			} else if ("LagerstandEnde".equals(fieldName)) {
				value = data[index][REPORT_WARENENTNAHMESTATISTIK_LAGERSTAND_ENDE];
			} else if ("MengeZugang".equals(fieldName)) {
				value = data[index][REPORT_WARENENTNAHMESTATISTIK_MENGE_ZUGANG];
			} else if ("MengeAbgang".equals(fieldName)) {
				value = data[index][REPORT_WARENENTNAHMESTATISTIK_MENGE_ABGANG];
			} else if ("LagerwertBeginn".equals(fieldName)) {
				value = data[index][REPORT_WARENENTNAHMESTATISTIK_LAGERWERT_BEGINN];
			} else if ("LagerwertEnde".equals(fieldName)) {
				value = data[index][REPORT_WARENENTNAHMESTATISTIK_LAGERWERT_ENDE];
			} else if ("Lager".equals(fieldName)) {
				value = data[index][REPORT_WARENENTNAHMESTATISTIK_LAGER];
			} else if ("Versteckt".equals(fieldName)) {
				value = data[index][REPORT_WARENENTNAHMESTATISTIK_VERSTECKT];
			} else if ("Einkaufspreis".equals(fieldName)) {
				value = data[index][REPORT_WARENENTNAHMESTATISTIK_EKPREIS];
			} else if ("AnzahlWareneingaenge".equals(fieldName)) {
				value = data[index][REPORT_WARENENTNAHMESTATISTIK_ANZAHL_BESTELLUNGEN];
			} else if ("Artikelgruppe".equals(fieldName)) {
				value = data[index][REPORT_WARENENTNAHMESTATISTIK_ARTIKELGRUPPE];
			} else if ("Artikelklasse".equals(fieldName)) {
				value = data[index][REPORT_WARENENTNAHMESTATISTIK_ARTIKELKLASSE];
			} else if ("Sachkonto".equals(fieldName)) {
				value = data[index][REPORT_WARENENTNAHMESTATISTIK_SACHKONTO];
			} else if ("WertAbgaenge".equals(fieldName)) {
				value = data[index][REPORT_WARENENTNAHMESTATISTIK_WERT_ABGAENGE];
			} else if ("WertZugaenge".equals(fieldName)) {
				value = data[index][REPORT_WARENENTNAHMESTATISTIK_WERT_ZUGAENGE];
			} else if ("EinstandspreisProStueck".equals(fieldName)) {
				value = data[index][REPORT_WARENENTNAHMESTATISTIK_EINSTANDSPREIS_PRO_STUECK];
			} else if ("Artikelreferenznummer".equals(fieldName)) {
				value = data[index][REPORT_WARENENTNAHMESTATISTIK_ARTIKELREFERENZNUMMER];
			} else if ("Belegart".equals(fieldName)) {
				value = data[index][REPORT_WARENENTNAHMESTATISTIK_BELEGART];
			} else if ("Belegnummer".equals(fieldName)) {
				value = data[index][REPORT_WARENENTNAHMESTATISTIK_BELEGNUMMER];
			} else if ("Buchungszeit".equals(fieldName)) {
				value = data[index][REPORT_WARENENTNAHMESTATISTIK_BUCHUNGSZEIT];
			} else if ("Belegdatum".equals(fieldName)) {
				value = data[index][REPORT_WARENENTNAHMESTATISTIK_BELEGDATUM];
			}
		}

		return value;

	}

}
