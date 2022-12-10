package com.lp.server.forecast.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import org.hibernate.Query;
import org.hibernate.Session;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikellistespr;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelbestelltFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VerpackungsmittelDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.fertigung.fastlanereader.generated.FLRLos;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosablieferung;
import com.lp.server.forecast.fastlanereader.generated.FLRForecastauftrag;
import com.lp.server.forecast.fastlanereader.generated.FLRForecastposition;
import com.lp.server.forecast.fastlanereader.generated.FLRForecastpositionLiefersituation;
import com.lp.server.forecast.fastlanereader.generated.FLRLinienabruf;
import com.lp.server.forecast.service.FclieferadresseDto;
import com.lp.server.forecast.service.ForecastDto;
import com.lp.server.forecast.service.ForecastFac;
import com.lp.server.forecast.service.ForecastHelper;
import com.lp.server.forecast.service.ForecastReportFac;
import com.lp.server.forecast.service.ForecastauftragDto;
import com.lp.server.forecast.service.ForecastpositionDto;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferscheinposition;
import com.lp.server.partner.fastlanereader.generated.FLRLiefermengen;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeReportFac;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklisteposition;
import com.lp.server.stueckliste.service.StuecklisteAufgeloest;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFacLocal;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.HelperServer;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.server.util.report.TimingInterceptor;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

@Stateless
@Interceptors(TimingInterceptor.class)
public class ForecastReportFacBean extends LPReport implements ForecastReportFac {

	@EJB
	private StuecklisteFacLocal stuecklisteFacLocalBean;

	private String sAktuellerReport = null;
	private Object[][] data = null;

	public boolean next() throws JRException {
		index++;
		return (index < data.length);
	}

	public static int REPORT_BESCHAFFUNG_FORECAST_NUMMER = 0;
	public static int REPORT_BESCHAFFUNG_FORECAST_PROJEKT = 1;
	public static int REPORT_BESCHAFFUNG_FORECAST_STATUS = 2;
	public static int REPORT_BESCHAFFUNG_FORECAST_KUNDE = 3;
	public static int REPORT_BESCHAFFUNG_FORECAST_LIEFERADRESSE = 4;
	public static int REPORT_BESCHAFFUNG_FORECASTAUFTRAG_ANLAGEDATUM = 5;
	public static int REPORT_BESCHAFFUNG_FORECASTAUFTRAG_STATUS = 6;
	public static int REPORT_BESCHAFFUNG_FORECASTAUFTRAG_BEMERKUNG = 7;
	public static int REPORT_BESCHAFFUNG_ARTIKEL = 8;
	public static int REPORT_BESCHAFFUNG_LIEFERANT = 9;
	public static int REPORT_BESCHAFFUNG_EK_PREIS = 10;
	public static int REPORT_BESCHAFFUNG_SUMME_EK_MENGE = 11;
	public static int REPORT_BESCHAFFUNG_SUBREPORT_OFFSET = 12;
	public static int REPORT_BESCHAFFUNG_DLZ = 13;
	public static int REPORT_BESCHAFFUNG_WBZ = 14;
	public static int REPORT_BESCHAFFUNG_STKL_EBENE = 15;
	public static int REPORT_BESCHAFFUNG_LAGERSTAND = 16;
	public static int REPORT_BESCHAFFUNG_INFERTIGUNG = 17;
	public static int REPORT_BESCHAFFUNG_BESTELLT = 18;
	public static int REPORT_BESCHAFFUNG_RESERVIERT = 19;
	public static int REPORT_BESCHAFFUNG_RAHMENBESTELLT = 20;
	public static int REPORT_BESCHAFFUNG_FEHLMENGE = 21;
	public static int REPORT_BESCHAFFUNG_LAGERMINDEST = 22;
	public static int REPORT_BESCHAFFUNG_LAGERSOLL = 23;

	public static int REPORT_BESCHAFFUNG_BEZEICHNUNG = 24;
	public static int REPORT_BESCHAFFUNG_ZUSATZBEZEICHNUNG = 25;
	public static int REPORT_BESCHAFFUNG_ARTIKELNUMMER_LIEFERANT = 26;
	public static int REPORT_BESCHAFFUNG_ARTIKELBEZEICHNUNG_LIEFERANT = 26;
	public static int REPORT_BESCHAFFUNG_JAHRESMENGE = 28;
	public static int REPORT_BESCHAFFUNG_MANDANT = 29;
	public static int REPORT_BESCHAFFUNG_B_STUECKLISTE = 30;
	public static int REPORT_BESCHAFFUNG_FORECASTAUFTRAG_ID = 31;
	public static int REPORT_BESCHAFFUNG_VERBUNDENES_UNTERNEHMEN = 32;
	public static int REPORT_BESCHAFFUNG_EINHEIT = 33;
	public static int REPORT_BESCHAFFUNG_FORECASTAUFTRAG_KOMMENTAR = 34;

	public static int REPORT_BESCHAFFUNG_ZUSATZBEZEICHNUNG2 = 35;
	public static int REPORT_BESCHAFFUNG_KURZBEZEICHNUNG = 36;
	public static int REPORT_BESCHAFFUNG_INDEX = 37;
	public static int REPORT_BESCHAFFUNG_REVISION = 38;

	public static int REPORT_BESCHAFFUNG_ANZAHL_SPALTEN = 39;

	public static int REPORT_UEBERSICHT_KUNDE = 0;
	public static int REPORT_UEBERSICHT_LIEFERADRESSE = 1;
	public static int REPORT_UEBERSICHT_FORECASTNUMMER = 2;
	public static int REPORT_UEBERSICHT_ANLAGEDATUM = 3;
	public static int REPORT_UEBERSICHT_KOMMENTAR = 4;
	public static int REPORT_UEBERSICHT_PROJEKT = 5;
	public static int REPORT_UEBERSICHT_BEMERKUNG = 6;
	public static int REPORT_UEBERSICHT_ANZAHL_SPALTEN = 7;

	public static int REPORT_DELTALISTE_ARTIKELNUMMER = 0;
	public static int REPORT_DELTALISTE_BEZEICHNUNG = 1;
	public static int REPORT_DELTALISTE_DATUM = 2;
	public static int REPORT_DELTALISTE_MENGE_ALT = 3;
	public static int REPORT_DELTALISTE_MENGE_NEU = 4;
	public static int REPORT_DELTALISTE_EINHEIT = 5;

	public static int REPORT_DELTALISTE_LAGERSTAND = 6;
	public static int REPORT_DELTALISTE_IN_FERTIGUNG = 7;
	public static int REPORT_DELTALISTE_BESTELLT = 8;
	public static int REPORT_DELTALISTE_RAHMENBESTELLT = 9;
	public static int REPORT_DELTALISTE_KOMMENTAR_ALT = 10;
	public static int REPORT_DELTALISTE_KOMMENTAR_NEU = 11;
	public static int REPORT_DELTALISTE_FORECASTART = 12;
	public static int REPORT_DELTALISTE_LINIENABRUFE_ALT = 13;
	public static int REPORT_DELTALISTE_LINIENABRUFE_NEU = 14;
	public static int REPORT_DELTALISTE_WBZ_LIEF1 = 15;
	public static int REPORT_DELTALISTE_FORECASTPOSITION_ID = 16;
	public static int REPORT_DELTALISTE_STATUS_ALT_CNR = 17;
	public static int REPORT_DELTALISTE_STATUS_NEU_CNR = 18;
	public static int REPORT_DELTALISTE_FORECASTPOSITION_NEU_ID = 19;

	public static int REPORT_DELTALISTE_ERSATZARTIKEL_NUMMER = 20;
	public static int REPORT_DELTALISTE_ERSATZARTIKEL_BEZEICHNUNG = 21;
	public static int REPORT_DELTALISTE_ERSATZARTIKEL_IN_FERTIGUNG = 22;
	public static int REPORT_DELTALISTE_ERSATZARTIKEL_BESTELLT = 23;
	public static int REPORT_DELTALISTE_ERSATZARTIKEL_RAHMENBESTELLT = 24;

	public static int REPORT_DELTALISTE_STKLPOS_ARTIKEL = 25;
	public static int REPORT_DELTALISTE_STKLPOS_BEZEICHNUNG = 26;
	public static int REPORT_DELTALISTE_STKLPOS_ZIELMENGE = 27;
	public static int REPORT_DELTALISTE_STKLPOS_LAGERSTAND = 28;
	public static int REPORT_DELTALISTE_STKLPOS_IN_FERTIGUNG = 29;
	public static int REPORT_DELTALISTE_STKLPOS_BESTELLT = 30;
	public static int REPORT_DELTALISTE_STKLPOS_RAHMENBESTELLT = 31;
	public static int REPORT_DELTALISTE_ANZAHL_SPALTEN = 32;

	public static int REPORT_FORECASTPOSITIONEN_FORECAST_KENNUNG = 0;
	public static int REPORT_FORECASTPOSITIONEN_FORECAST_KUNDE = 1;
	public static int REPORT_FORECASTPOSITIONEN_FORECAST_PROJEKT = 2;
	public static int REPORT_FORECASTPOSITIONEN_LIEFERADRESSE = 3;
	public static int REPORT_FORECASTPOSITIONEN_FORECASTAUFTRAG_BEMERKUNG = 4;
	public static int REPORT_FORECASTPOSITIONEN_FORECASTAUFTRAG_KOMMENTAR = 5;
	public static int REPORT_FORECASTPOSITIONEN_FORECASTAUFTRAG_STATUS = 6;
	public static int REPORT_FORECASTPOSITIONEN_FORECASTAUFTRAG_FREIGABEDATUM = 7;
	public static int REPORT_FORECASTPOSITIONEN_FORECASTAUFTRAG_ANLAGEDATUM = 8;

	public static int REPORT_FORECASTPOSITIONEN_BESTELLNUMMER = 9;
	public static int REPORT_FORECASTPOSITIONEN_TERMIN = 10;
	public static int REPORT_FORECASTPOSITIONEN_MENGE = 11;
	public static int REPORT_FORECASTPOSITIONEN_STATUS = 12;
	public static int REPORT_FORECASTPOSITIONEN_FORECASTART = 13;
	public static int REPORT_FORECASTPOSITIONEN_FORECASTART_MANUELL = 14;
	public static int REPORT_FORECASTPOSITIONEN_KOMMENTAR = 15;

	public static int REPORT_FORECASTPOSITIONEN_LINIENABRUF_PRODUKTIONSDATUM = 16;
	public static int REPORT_FORECASTPOSITIONEN_LINIENABRUF_LINIE = 17;
	public static int REPORT_FORECASTPOSITIONEN_LINIENABRUF_BEREICH = 18;
	public static int REPORT_FORECASTPOSITIONEN_LINIENABRUF_BEREICH_BEZ = 19;
	public static int REPORT_FORECASTPOSITIONEN_LINIENABRUF_BESTELLNUMMER = 20;
	public static int REPORT_FORECASTPOSITIONEN_LINIENABRUF_MENGE = 21;
	public static int REPORT_FORECASTPOSITIONEN_LOSE = 22;
	public static int REPORT_FORECASTPOSITIONEN_I_ID = 23;
	public static int REPORT_FORECASTPOSITIONEN_MANDANT = 24;
	public static int REPORT_FORECASTPOSITIONEN_ANZAHL_SPALTEN = 25;

	public static int REPORT_LIEFERSITUATION_BESTELLNUMMER = 0;
	public static int REPORT_LIEFERSITUATION_TERMIN = 1;
	public static int REPORT_LIEFERSITUATION_MENGE = 2;
	public static int REPORT_LIEFERSITUATION_STATUS = 3;
	public static int REPORT_LIEFERSITUATION_FORECASTART = 4;
	public static int REPORT_LIEFERSITUATION_FORECASTART_MANUELL = 5;
	public static int REPORT_LIEFERSITUATION_KOMMENTAR = 6;
	public static int REPORT_LIEFERSITUATION_LINIENABRUF_MENGE = 7;
	public static int REPORT_LIEFERSITUATION_LOSNUMMER = 8;
	public static int REPORT_LIEFERSITUATION_LIEFERSCHEINNUMMER = 9;
	public static int REPORT_LIEFERSITUATION_ARTIKELNUMMER = 10;
	public static int REPORT_LIEFERSITUATION_ERFASSTE_MENGE = 11;
	public static int REPORT_LIEFERSITUATION_LIEFERSCHEIN_DATUM = 12;
	public static int REPORT_LIEFERSITUATION_LIEFERSCHEIN_STATUS = 13;
	public static int REPORT_LIEFERSITUATION_LIEFERSCHEIN_RECHNUNGNR = 14;
	public static int REPORT_LIEFERSITUATION_LIEFERSCHEIN_MENGE = 15;
	public static int REPORT_LIEFERSITUATION_LOS_ABLIEFERMENGE = 16;
	public static int REPORT_LIEFERSITUATION_LOS_GROESSE = 17;
	public static int REPORT_LIEFERSITUATION_LOS_STATUS = 18;
	public static int REPORT_LIEFERSITUATION_LIEFERMENGEN_LETZTER_STARTWERT = 19;
	public static int REPORT_LIEFERSITUATION_LIEFERMENGE_GESAMT = 20;
	public static int REPORT_LIEFERSITUATION_LIEFERMENGEN_DATUM_LETZTER_STARTWERT = 21;
	public static int REPORT_LIEFERSITUATION_LIEFERMENGEN_MENGE_UNTERWEGS = 22;

	public static int REPORT_LIEFERSITUATION_BEZEICHNUNG = 23;
	public static int REPORT_LIEFERSITUATION_KURZBEZEICHNUNG = 24;
	public static int REPORT_LIEFERSITUATION_ZUSATZBEZEICHNUNG = 25;
	public static int REPORT_LIEFERSITUATION_ZUSATZBEZEICHNUNG2 = 26;

	public static int REPORT_LIEFERSITUATION_ANZAHL_SPALTEN = 27;

	public static int REPORT_LINIENABRUFE_ARTIKELNUMMER = 0;
	public static int REPORT_LINIENABRUFE_BEZEICHNUNG = 1;
	public static int REPORT_LINIENABRUFE_KURZBEZEICHNUNG = 2;
	public static int REPORT_LINIENABRUFE_PRODUKTIONSDATUM = 3;
	public static int REPORT_LINIENABRUFE_BEREICH_NR = 4;
	public static int REPORT_LINIENABRUFE_BEREICH_BEZ = 5;
	public static int REPORT_LINIENABRUFE_BESTELLNUMMER = 6;
	public static int REPORT_LINIENABRUFE_MENGE = 7;
	public static int REPORT_LINIENABRUFE_LINIE = 8;
	public static int REPORT_LINIENABRUFE_FORECAST_AUFTRAG = 9;
	public static int REPORT_LINIENABRUFE_FORECAST_KUNDE = 10;
	public static int REPORT_LINIENABRUFE_FORECAST_LIEFERADRESSE = 11;
	public static int REPORT_LINIENABRUFE_ARTIKEL_KOMMISSIONIEREN = 12;
	public static int REPORT_LINIENABRUFE_LIEFERADRESSE_KOMMISSIONIEREN = 13;
	public static int REPORT_LINIENABRUFE_AUSLIEFERTERMIN = 14;

	public static int REPORT_LINIENABRUFE_VERPACKUNGSMITTEL_KENNUNG = 14;
	public static int REPORT_LINIENABRUFE_VERPACKUNGSMITTEL_BEZEICHNUNG = 15;
	public static int REPORT_LINIENABRUFE_VERPACKUNGSMITTEL_GEWICHT_IN_KG = 16;
	public static int REPORT_LINIENABRUFE_VERPACKUNGSMITTELMENGE = 17;
	public static int REPORT_LINIENABRUFE_VERPACKUNGSMENGE = 18;
	public static int REPORT_LINIENABRUFE_ARTIKEL_GEWICHT_IN_KG = 19;

	public static int REPORT_LINIENABRUFE_POSITIONSTERMIN = 20;
	public static int REPORT_LINIENABRUFE_FORECASTPOSITION_I_ID = 21;
	public static int REPORT_LINIENABRUFE_ANZAHL_SPALTEN = 22;

	public static int REPORT_GEPLANTERUMSATZ_ARTIKELNUMMER = 0;
	public static int REPORT_GEPLANTERUMSATZ_BEZEICHNUNG = 1;
	public static int REPORT_GEPLANTERUMSATZ_ZUSATZBEZEICHNUNG = 2;
	public static int REPORT_GEPLANTERUMSATZ_ZUSATZBEZEICHNUNG2 = 3;
	public static int REPORT_GEPLANTERUMSATZ_KURZBEZEICHNUNG = 4;
	public static int REPORT_GEPLANTERUMSATZ_REFERENZNUMMER = 5;
	public static int REPORT_GEPLANTERUMSATZ_INDEX = 6;
	public static int REPORT_GEPLANTERUMSATZ_REVISION = 7;
	public static int REPORT_GEPLANTERUMSATZ_EINHEIT = 8;
	public static int REPORT_GEPLANTERUMSATZ_LAGERSTAND = 9;
	public static int REPORT_GEPLANTERUMSATZ_LAGERMINDESTSTAND = 10;
	public static int REPORT_GEPLANTERUMSATZ_MENGE_MONAT01 = 11;
	public static int REPORT_GEPLANTERUMSATZ_MENGE_MONAT02 = 12;
	public static int REPORT_GEPLANTERUMSATZ_MENGE_MONAT03 = 13;
	public static int REPORT_GEPLANTERUMSATZ_MENGE_MONAT04 = 14;
	public static int REPORT_GEPLANTERUMSATZ_MENGE_MONAT05 = 15;
	public static int REPORT_GEPLANTERUMSATZ_MENGE_MONAT06 = 16;
	public static int REPORT_GEPLANTERUMSATZ_MENGE_MONAT07 = 17;
	public static int REPORT_GEPLANTERUMSATZ_MENGE_MONAT08 = 18;
	public static int REPORT_GEPLANTERUMSATZ_MENGE_MONAT09 = 19;
	public static int REPORT_GEPLANTERUMSATZ_MENGE_MONAT10 = 20;
	public static int REPORT_GEPLANTERUMSATZ_MENGE_MONAT11 = 21;
	public static int REPORT_GEPLANTERUMSATZ_MENGE_MONAT12 = 22;
	public static int REPORT_GEPLANTERUMSATZ_VKWERT_MONAT01 = 23;
	public static int REPORT_GEPLANTERUMSATZ_VKWERT_MONAT02 = 24;
	public static int REPORT_GEPLANTERUMSATZ_VKWERT_MONAT03 = 25;
	public static int REPORT_GEPLANTERUMSATZ_VKWERT_MONAT04 = 26;
	public static int REPORT_GEPLANTERUMSATZ_VKWERT_MONAT05 = 27;
	public static int REPORT_GEPLANTERUMSATZ_VKWERT_MONAT06 = 28;
	public static int REPORT_GEPLANTERUMSATZ_VKWERT_MONAT07 = 29;
	public static int REPORT_GEPLANTERUMSATZ_VKWERT_MONAT08 = 30;
	public static int REPORT_GEPLANTERUMSATZ_VKWERT_MONAT09 = 31;
	public static int REPORT_GEPLANTERUMSATZ_VKWERT_MONAT10 = 32;
	public static int REPORT_GEPLANTERUMSATZ_VKWERT_MONAT11 = 33;
	public static int REPORT_GEPLANTERUMSATZ_VKWERT_MONAT12 = 34;
	public static int REPORT_GEPLANTERUMSATZ_MONATSMENGEN_INTERN = 35;
	public static int REPORT_GEPLANTERUMSATZ_ARTIKEL_I_ID_INTERN = 36;
	public static int REPORT_GEPLANTERUMSATZ_KUNDE_DTO_INTERN = 37;
	public static int REPORT_GEPLANTERUMSATZ_KUNDE = 38;
	public static int REPORT_GEPLANTERUMSATZ_ARTIKELGRUPPE = 39;
	public static int REPORT_GEPLANTERUMSATZ_ANZAHL_SPALTEN = 40;

	public Object getFieldValue(JRField jRField) throws JRException {
		Object value = null;
		String fieldName = jRField.getName();
		if (sAktuellerReport.equals(ForecastReportFac.REPORT_BESCHAFFUNG)) {
			if ("Artikel".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_ARTIKEL];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_EINHEIT];
			} else if ("VerbundenesUnternehmen".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_VERBUNDENES_UNTERNEHMEN];
			} else if ("Lieferant".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_LIEFERANT];
			} else if ("SummeEKMenge".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_SUMME_EK_MENGE];
			} else if ("EKPreis".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_EK_PREIS];
			} else if ("SubreportOffset".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_SUBREPORT_OFFSET];
			}

			else if ("DLZ".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_DLZ];
			} else if ("WBZ".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_WBZ];
			} else if ("StklEbene".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_STKL_EBENE];
			} else if ("Lagerstand".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_LAGERSTAND];
			} else if ("InFertigung".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_INFERTIGUNG];
			} else if ("Bestellt".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_BESTELLT];
			} else if ("Reserviert".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_RESERVIERT];
			} else if ("Rahmenbestellt".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_RAHMENBESTELLT];
			} else if ("Fehlmenge".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_FEHLMENGE];
			} else if ("Lagermindest".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_LAGERMINDEST];
			} else if ("Lagersoll".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_LAGERSOLL];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_BEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_ZUSATZBEZEICHNUNG];
			} else if ("ArtikelnummerLieferant".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_ARTIKELNUMMER_LIEFERANT];
			} else if ("ArtikelbezeichnungLieferant".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_ARTIKELBEZEICHNUNG_LIEFERANT];
			} else if ("Jahresmenge".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_JAHRESMENGE];
			} else if ("ForecastNummer".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_FORECAST_NUMMER];
			} else if ("ForecastProjekt".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_FORECAST_PROJEKT];
			} else if ("ForecastStatus".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_FORECAST_STATUS];
			} else if ("ForecastKunde".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_FORECAST_KUNDE];
			} else if ("ForecastLieferadresse".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_FORECAST_LIEFERADRESSE];
			} else if ("ForecastauftragAnlagedatum".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_FORECASTAUFTRAG_ANLAGEDATUM];
			} else if ("ForecastauftragStatus".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_FORECASTAUFTRAG_STATUS];
			} else if ("ForecastauftragBemerkung".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_FORECASTAUFTRAG_BEMERKUNG];
			} else if ("ForecastauftragKommentar".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_FORECASTAUFTRAG_KOMMENTAR];
			} else if ("Mandant".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_MANDANT];
			} else if ("Stueckliste".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_B_STUECKLISTE];
			} else if ("ForecastauftragID".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_FORECASTAUFTRAG_ID];
			} else if ("Zusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_ZUSATZBEZEICHNUNG2];
			} else if ("Kurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_KURZBEZEICHNUNG];
			} else if ("Index".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_INDEX];
			} else if ("Revision".equals(fieldName)) {
				value = data[index][REPORT_BESCHAFFUNG_REVISION];
			}

		} else if (sAktuellerReport.equals(ForecastReportFac.REPORT_UEBERSICHT)) {
			if ("Kunde".equals(fieldName)) {
				value = data[index][REPORT_UEBERSICHT_KUNDE];
			} else if ("Lieferadresse".equals(fieldName)) {
				value = data[index][REPORT_UEBERSICHT_LIEFERADRESSE];
			} else if ("Forecastnummer".equals(fieldName)) {
				value = data[index][REPORT_UEBERSICHT_FORECASTNUMMER];
			} else if ("Anlagedatum".equals(fieldName)) {
				value = data[index][REPORT_UEBERSICHT_ANLAGEDATUM];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][REPORT_UEBERSICHT_KOMMENTAR];
			} else if ("Projekt".equals(fieldName)) {
				value = data[index][REPORT_UEBERSICHT_PROJEKT];
			} else if ("Bemerkung".equals(fieldName)) {
				value = data[index][REPORT_UEBERSICHT_BEMERKUNG];
			}
		} else if (sAktuellerReport.equals(ForecastReportFac.REPORT_FORECASTPOSITIONEN)) {
			if ("ForecastKennung".equals(fieldName)) {
				value = data[index][REPORT_FORECASTPOSITIONEN_FORECAST_KENNUNG];
			} else if ("ForecastKunde".equals(fieldName)) {
				value = data[index][REPORT_FORECASTPOSITIONEN_FORECAST_KUNDE];
			} else if ("Mandant".equals(fieldName)) {
				value = data[index][REPORT_FORECASTPOSITIONEN_MANDANT];
			} else if ("ForecastProjekt".equals(fieldName)) {
				value = data[index][REPORT_FORECASTPOSITIONEN_FORECAST_PROJEKT];
			} else if ("Lieferadresse".equals(fieldName)) {
				value = data[index][REPORT_FORECASTPOSITIONEN_LIEFERADRESSE];
			} else if ("ForecastauftragBemerkung".equals(fieldName)) {
				value = data[index][REPORT_FORECASTPOSITIONEN_FORECASTAUFTRAG_BEMERKUNG];
			} else if ("ForecastauftragKommentar".equals(fieldName)) {
				value = data[index][REPORT_FORECASTPOSITIONEN_FORECASTAUFTRAG_KOMMENTAR];
			} else if ("ForecastauftragStatus".equals(fieldName)) {
				value = data[index][REPORT_FORECASTPOSITIONEN_FORECASTAUFTRAG_STATUS];
			} else if ("ForecastauftragFreigabedatum".equals(fieldName)) {
				value = data[index][REPORT_FORECASTPOSITIONEN_FORECASTAUFTRAG_FREIGABEDATUM];
			} else if ("ForecastauftragAnlagedatum".equals(fieldName)) {
				value = data[index][REPORT_FORECASTPOSITIONEN_FORECASTAUFTRAG_ANLAGEDATUM];
			} else if ("Bestellnummer".equals(fieldName)) {
				value = data[index][REPORT_FORECASTPOSITIONEN_BESTELLNUMMER];
			} else if ("Termin".equals(fieldName)) {
				value = data[index][REPORT_FORECASTPOSITIONEN_TERMIN];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_FORECASTPOSITIONEN_MENGE];
			} else if ("Status".equals(fieldName)) {
				value = data[index][REPORT_FORECASTPOSITIONEN_STATUS];
			} else if ("Forecastart".equals(fieldName)) {
				value = data[index][REPORT_FORECASTPOSITIONEN_FORECASTART];
			} else if ("ForecastartManuell".equals(fieldName)) {
				value = data[index][REPORT_FORECASTPOSITIONEN_FORECASTART_MANUELL];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][REPORT_FORECASTPOSITIONEN_KOMMENTAR];
			} else if ("Lose".equals(fieldName)) {
				value = data[index][REPORT_FORECASTPOSITIONEN_LOSE];
			} else if ("LinienabrufProduktionsdatum".equals(fieldName)) {
				value = data[index][REPORT_FORECASTPOSITIONEN_LINIENABRUF_PRODUKTIONSDATUM];
			} else if ("LinienabrufLinie".equals(fieldName)) {
				value = data[index][REPORT_FORECASTPOSITIONEN_LINIENABRUF_LINIE];
			} else if ("LinienabrufBereich".equals(fieldName)) {
				value = data[index][REPORT_FORECASTPOSITIONEN_LINIENABRUF_BEREICH];
			} else if ("LinienabrufBereichBez".equals(fieldName)) {
				value = data[index][REPORT_FORECASTPOSITIONEN_LINIENABRUF_BEREICH_BEZ];
			} else if ("LinienabrufBestellnummer".equals(fieldName)) {
				value = data[index][REPORT_FORECASTPOSITIONEN_LINIENABRUF_BESTELLNUMMER];
			} else if ("LinienabrufMenge".equals(fieldName)) {
				value = data[index][REPORT_FORECASTPOSITIONEN_LINIENABRUF_MENGE];
			} else if ("FORECASTPOSITION_I_ID".equals(fieldName)) {
				value = data[index][REPORT_FORECASTPOSITIONEN_I_ID];
			}

		} else if (sAktuellerReport.equals(ForecastReportFac.REPORT_LIEFERSITUATION)) {
			if ("Bestellnummer".equals(fieldName)) {
				value = data[index][REPORT_LIEFERSITUATION_BESTELLNUMMER];
			} else if ("Termin".equals(fieldName)) {
				value = data[index][REPORT_LIEFERSITUATION_TERMIN];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_LIEFERSITUATION_MENGE];
			} else if ("LinienabrufMenge".equals(fieldName)) {
				value = data[index][REPORT_LIEFERSITUATION_LINIENABRUF_MENGE];
			} else if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_LIEFERSITUATION_ARTIKELNUMMER];
			} else if ("Status".equals(fieldName)) {
				value = data[index][REPORT_LIEFERSITUATION_STATUS];
			} else if ("Forecastart".equals(fieldName)) {
				value = data[index][REPORT_LIEFERSITUATION_FORECASTART];
			} else if ("ForecastartManuell".equals(fieldName)) {
				value = data[index][REPORT_LIEFERSITUATION_FORECASTART_MANUELL];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][REPORT_LIEFERSITUATION_KOMMENTAR];
			} else if ("Losnummer".equals(fieldName)) {
				value = data[index][REPORT_LIEFERSITUATION_LOSNUMMER];
			} else if ("Lieferscheinnummer".equals(fieldName)) {
				value = data[index][REPORT_LIEFERSITUATION_LIEFERSCHEINNUMMER];
			} else if ("ErfassteMenge".equals(fieldName)) {
				value = data[index][REPORT_LIEFERSITUATION_ERFASSTE_MENGE];
			} else if ("LieferscheinDatum".equals(fieldName)) {
				value = data[index][REPORT_LIEFERSITUATION_LIEFERSCHEIN_DATUM];
			} else if ("LieferscheinStatus".equals(fieldName)) {
				value = data[index][REPORT_LIEFERSITUATION_LIEFERSCHEIN_STATUS];
			} else if ("LieferscheinRechnungsnummer".equals(fieldName)) {
				value = data[index][REPORT_LIEFERSITUATION_LIEFERSCHEIN_RECHNUNGNR];
			} else if ("LieferscheinMenge".equals(fieldName)) {
				value = data[index][REPORT_LIEFERSITUATION_LIEFERSCHEIN_MENGE];
			} else if ("LosAbliefermenge".equals(fieldName)) {
				value = data[index][REPORT_LIEFERSITUATION_LOS_ABLIEFERMENGE];
			} else if ("Losgroesse".equals(fieldName)) {
				value = data[index][REPORT_LIEFERSITUATION_LOS_GROESSE];
			} else if ("LosStatus".equals(fieldName)) {
				value = data[index][REPORT_LIEFERSITUATION_LOS_STATUS];

			} else if ("LiefermengenLetzterStartwert".equals(fieldName)) {
				value = data[index][REPORT_LIEFERSITUATION_LIEFERMENGEN_LETZTER_STARTWERT];
			} else if ("LiefermengenDatumLetzterStartwert".equals(fieldName)) {
				value = data[index][REPORT_LIEFERSITUATION_LIEFERMENGEN_DATUM_LETZTER_STARTWERT];
			} else if ("LiefermengeGesamt".equals(fieldName)) {
				value = data[index][REPORT_LIEFERSITUATION_LIEFERMENGE_GESAMT];
			} else if ("MengeUnterwegs".equals(fieldName)) {
				value = data[index][REPORT_LIEFERSITUATION_LIEFERMENGEN_MENGE_UNTERWEGS];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_LIEFERSITUATION_BEZEICHNUNG];
			} else if ("KurzBezeichnung".equals(fieldName)) {
				value = data[index][REPORT_LIEFERSITUATION_KURZBEZEICHNUNG];
			} else if ("ZusatzBezeichnung".equals(fieldName)) {
				value = data[index][REPORT_LIEFERSITUATION_ZUSATZBEZEICHNUNG];
			} else if ("ZusatzBezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_LIEFERSITUATION_ZUSATZBEZEICHNUNG2];
			}

		} else if (sAktuellerReport.equals(ForecastReportFac.REPORT_DELTALISTE)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_BEZEICHNUNG];
			} else if ("Datum".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_DATUM];
			} else if ("MengeAlt".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_MENGE_ALT];
			} else if ("MengeNeu".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_MENGE_NEU];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_EINHEIT];
			} else if ("InFertigung".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_IN_FERTIGUNG];
			} else if ("Rahmenbestellt".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_RAHMENBESTELLT];
			} else if ("Lagerstand".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_LAGERSTAND];
			} else if ("Bestellt".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_BESTELLT];
			} else if ("WBZLief1".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_WBZ_LIEF1];
			} else if ("KommentarAlt".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_KOMMENTAR_ALT];
			} else if ("KommentarNeu".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_KOMMENTAR_NEU];
			} else if ("Forecastart".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_FORECASTART];
			} else if ("LinienabrufeAlt".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_LINIENABRUFE_ALT];
			} else if ("LinienabrufeNeu".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_LINIENABRUFE_NEU];
			} else if ("ForecastpositionIId".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_FORECASTPOSITION_ID];
			} else if ("StatusCnrAlt".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_STATUS_ALT_CNR];
			} else if ("StatusCnrNeu".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_STATUS_NEU_CNR];
			} else if ("ForecastpositionIIdNeu".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_FORECASTPOSITION_NEU_ID];
			} else if ("ErsatzartikelNummer".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_ERSATZARTIKEL_NUMMER];
			} else if ("ErsatzartikelBezeichnung".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_ERSATZARTIKEL_BEZEICHNUNG];
			} else if ("ErsatzartikelInFertigung".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_ERSATZARTIKEL_IN_FERTIGUNG];
			} else if ("ErsatzartikelBestellt".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_ERSATZARTIKEL_BESTELLT];
			} else if ("ErsatzartikelRahmenbestellt".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_ERSATZARTIKEL_RAHMENBESTELLT];
			} else if ("StklposArtikelnummer".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_STKLPOS_ARTIKEL];
			} else if ("StklposArtikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_STKLPOS_BEZEICHNUNG];
			} else if ("StklposBestellt".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_STKLPOS_BESTELLT];
			} else if ("StklposInFertigung".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_STKLPOS_IN_FERTIGUNG];
			} else if ("StklposLagerstand".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_STKLPOS_LAGERSTAND];
			} else if ("StklposRahmenbestellt".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_STKLPOS_RAHMENBESTELLT];
			} else if ("StklposZielmenge".equals(fieldName)) {
				value = data[index][REPORT_DELTALISTE_STKLPOS_ZIELMENGE];
			}
		} else if (sAktuellerReport.equals(ForecastReportFac.REPORT_LINIENABRUFE)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_LINIENABRUFE_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_LINIENABRUFE_BEZEICHNUNG];
			} else if ("Kurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_LINIENABRUFE_KURZBEZEICHNUNG];
			} else if ("BereichBezeichnung".equals(fieldName)) {
				value = data[index][REPORT_LINIENABRUFE_BEREICH_BEZ];
			} else if ("BereichKennung".equals(fieldName)) {
				value = data[index][REPORT_LINIENABRUFE_BEREICH_NR];
			} else if ("Bestellnummer".equals(fieldName)) {
				value = data[index][REPORT_LINIENABRUFE_BESTELLNUMMER];
			} else if ("ForecastAuftrag".equals(fieldName)) {
				value = data[index][REPORT_LINIENABRUFE_FORECAST_AUFTRAG];
			} else if ("ForecastKunde".equals(fieldName)) {
				value = data[index][REPORT_LINIENABRUFE_FORECAST_KUNDE];
			} else if ("ForecastLieferadressse".equals(fieldName)) {
				value = data[index][REPORT_LINIENABRUFE_FORECAST_LIEFERADRESSE];
			} else if ("Linie".equals(fieldName)) {
				value = data[index][REPORT_LINIENABRUFE_LINIE];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_LINIENABRUFE_MENGE];
			} else if ("Produktionsdatum".equals(fieldName)) {
				value = data[index][REPORT_LINIENABRUFE_PRODUKTIONSDATUM];
			} else if ("Ausliefertermin".equals(fieldName)) {
				value = data[index][REPORT_LINIENABRUFE_AUSLIEFERTERMIN];
			} else if ("VerpackungmittelKennung".equals(fieldName)) {
				value = data[index][REPORT_LINIENABRUFE_VERPACKUNGSMITTEL_KENNUNG];
			} else if ("VerpackungmittelBezeichnung".equals(fieldName)) {
				value = data[index][REPORT_LINIENABRUFE_VERPACKUNGSMITTEL_BEZEICHNUNG];
			} else if ("VerpackungmittelGewichtInKg".equals(fieldName)) {
				value = data[index][REPORT_LINIENABRUFE_VERPACKUNGSMITTEL_GEWICHT_IN_KG];
			} else if ("VerpackungmittelMenge".equals(fieldName)) {
				value = data[index][REPORT_LINIENABRUFE_VERPACKUNGSMITTELMENGE];
			} else if ("Verpackungsmenge".equals(fieldName)) {
				value = data[index][REPORT_LINIENABRUFE_VERPACKUNGSMENGE];
			} else if ("ArtikelGewichtInKg".equals(fieldName)) {
				value = data[index][REPORT_LINIENABRUFE_ARTIKEL_GEWICHT_IN_KG];
			} else if ("ArtikelKommissionieren".equals(fieldName)) {
				value = data[index][REPORT_LINIENABRUFE_ARTIKEL_KOMMISSIONIEREN];
			} else if ("LieferadresseKommissionieren".equals(fieldName)) {
				value = data[index][REPORT_LINIENABRUFE_LIEFERADRESSE_KOMMISSIONIEREN];
			} else if ("Positionstermin".equals(fieldName)) {
				value = data[index][REPORT_LINIENABRUFE_POSITIONSTERMIN];
			} else if ("FORECASTPOSITION_I_ID".equals(fieldName)) {
				value = data[index][REPORT_LINIENABRUFE_FORECASTPOSITION_I_ID];
			}

		} else if (sAktuellerReport.equals(ForecastReportFac.REPORT_GEPLANTERUMSATZ)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_BEZEICHNUNG];
			} else if ("Kurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_KURZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_ZUSATZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_ZUSATZBEZEICHNUNG2];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_EINHEIT];
			} else if ("Index".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_INDEX];
			} else if ("Kunde".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_KUNDE];
			}  else if ("Artikelgruppe".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_ARTIKELGRUPPE];
			} else if ("Lagermindeststand".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_LAGERMINDESTSTAND];
			} else if ("Lagerstand".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_LAGERSTAND];
			} else if ("Referenznummer".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_REFERENZNUMMER];
			} else if ("Revision".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_REVISION];
			} else if ("MengeMonat01".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_MENGE_MONAT01];
			} else if ("VKWertMonat01".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_VKWERT_MONAT01];
			} else if ("MengeMonat02".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_MENGE_MONAT02];
			} else if ("VKWertMonat02".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_VKWERT_MONAT02];
			} else if ("MengeMonat03".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_MENGE_MONAT03];
			} else if ("VKWertMonat03".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_VKWERT_MONAT03];
			} else if ("MengeMonat04".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_MENGE_MONAT04];
			} else if ("VKWertMonat04".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_VKWERT_MONAT04];
			} else if ("MengeMonat05".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_MENGE_MONAT05];
			} else if ("VKWertMonat05".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_VKWERT_MONAT05];
			} else if ("MengeMonat06".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_MENGE_MONAT06];
			} else if ("VKWertMonat06".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_VKWERT_MONAT06];
			} else if ("MengeMonat07".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_MENGE_MONAT07];
			} else if ("VKWertMonat07".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_VKWERT_MONAT07];
			} else if ("MengeMonat08".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_MENGE_MONAT08];
			} else if ("VKWertMonat08".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_VKWERT_MONAT08];
			} else if ("MengeMonat09".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_MENGE_MONAT09];
			} else if ("VKWertMonat09".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_VKWERT_MONAT09];
			} else if ("MengeMonat10".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_MENGE_MONAT10];
			} else if ("VKWertMonat10".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_VKWERT_MONAT10];
			} else if ("MengeMonat11".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_MENGE_MONAT11];
			} else if ("VKWertMonat11".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_VKWERT_MONAT11];
			} else if ("MengeMonat12".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_MENGE_MONAT12];
			} else if ("VKWertMonat12".equals(fieldName)) {
				value = data[index][REPORT_GEPLANTERUMSATZ_VKWERT_MONAT12];
			}
		}
		return value;
	}

	private Object[] addForecastDaten(ForecastDto fDto, FclieferadresseDto fclDto, ForecastauftragDto faDto,
			Object[] zeile, TheClientDto theClientDto) {

		zeile[REPORT_BESCHAFFUNG_FORECAST_KUNDE] = getKundeFac().kundeFindByPrimaryKey(fDto.getKundeIId(), theClientDto)
				.getPartnerDto().formatFixName1Name2();

		zeile[REPORT_BESCHAFFUNG_FORECAST_LIEFERADRESSE] = getKundeFac()
				.kundeFindByPrimaryKey(fclDto.getKundeIIdLieferadresse(), theClientDto).getPartnerDto()
				.formatFixName1Name2();

		zeile[REPORT_BESCHAFFUNG_FORECAST_NUMMER] = fDto.getCNr();
		zeile[REPORT_BESCHAFFUNG_FORECAST_PROJEKT] = fDto.getCProjekt();
		zeile[REPORT_BESCHAFFUNG_FORECAST_STATUS] = fDto.getStatusCNr();

		zeile[REPORT_BESCHAFFUNG_FORECAST_NUMMER] = fDto.getCNr();

		zeile[REPORT_BESCHAFFUNG_FORECASTAUFTRAG_BEMERKUNG] = faDto.getCBemerkung();
		zeile[REPORT_BESCHAFFUNG_FORECASTAUFTRAG_KOMMENTAR] = faDto.getXKommentar();
		zeile[REPORT_BESCHAFFUNG_FORECASTAUFTRAG_ANLAGEDATUM] = faDto.getTAnlegen();
		zeile[REPORT_BESCHAFFUNG_FORECASTAUFTRAG_ID] = faDto.getIId();
		zeile[REPORT_BESCHAFFUNG_FORECASTAUFTRAG_STATUS] = faDto.getStatusCNr();

		return zeile;
	}

	private Object[] holeArtikeldaten(Integer artikelIId, BigDecimal menge, java.sql.Date dStart,
			TheClientDto theClientDto) {

		ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(artikelIId, theClientDto);

		Object[] zeile = new Object[REPORT_BESCHAFFUNG_ANZAHL_SPALTEN];

		zeile[REPORT_BESCHAFFUNG_ARTIKEL] = aDto.getCNr();
		zeile[REPORT_BESCHAFFUNG_EINHEIT] = aDto.getEinheitCNr().trim();
		zeile[REPORT_BESCHAFFUNG_INDEX] = aDto.getCIndex();
		zeile[REPORT_BESCHAFFUNG_REVISION] = aDto.getCRevision();
		if (aDto.getArtikelsprDto() != null) {
			zeile[REPORT_BESCHAFFUNG_BEZEICHNUNG] = aDto.getArtikelsprDto().getCBez();
			zeile[REPORT_BESCHAFFUNG_ZUSATZBEZEICHNUNG] = aDto.getArtikelsprDto().getCZbez();
			zeile[REPORT_BESCHAFFUNG_ZUSATZBEZEICHNUNG2] = aDto.getArtikelsprDto().getCZbez2();
			zeile[REPORT_BESCHAFFUNG_KURZBEZEICHNUNG] = aDto.getArtikelsprDto().getCKbez();

		}

		zeile[REPORT_BESCHAFFUNG_JAHRESMENGE] = aDto.getFJahresmenge();

		// Lagermin//Soll

		boolean lagerminJeLager = false;
		try {
			ParametermandantDto parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_LAGERMIN_JE_LAGER);

			lagerminJeLager = (Boolean) parameterDto.getCWertAsObject();

			if (lagerminJeLager) {
				BigDecimal[] bd = getLagerFac().getSummeLagermindesUndLagerSollstandEinesStandorts(artikelIId, null,
						theClientDto);

				zeile[REPORT_BESCHAFFUNG_LAGERMINDEST] = new Double(bd[0].doubleValue());
				zeile[REPORT_BESCHAFFUNG_LAGERSOLL] = new Double(bd[1].doubleValue());

			} else {
				zeile[REPORT_BESCHAFFUNG_LAGERMINDEST] = aDto.getFLagermindest();
				zeile[REPORT_BESCHAFFUNG_LAGERSOLL] = aDto.getFLagersoll();
			}

			zeile[REPORT_BESCHAFFUNG_LAGERSTAND] = getLagerFac().getLagerstandAllerLagerEinesMandanten(artikelIId,
					theClientDto);

			zeile[REPORT_BESCHAFFUNG_FEHLMENGE] = getFehlmengeFac().getAnzahlFehlmengeEinesArtikels(artikelIId,
					theClientDto);

			zeile[REPORT_BESCHAFFUNG_RESERVIERT] = getReservierungFac().getAnzahlReservierungen(artikelIId,
					theClientDto);

			zeile[REPORT_BESCHAFFUNG_INFERTIGUNG] = getFertigungFac().getAnzahlInFertigung(artikelIId, theClientDto);

			zeile[REPORT_BESCHAFFUNG_BESTELLT] = getArtikelbestelltFac().getAnzahlBestellt(artikelIId);

			zeile[REPORT_BESCHAFFUNG_B_STUECKLISTE] = Boolean.FALSE;
			zeile[REPORT_BESCHAFFUNG_MANDANT] = aDto.getMandantCNr();

			Hashtable<?, ?> htAnzahlRahmenbestellt = getArtikelbestelltFac().getAnzahlRahmenbestellt(artikelIId,
					theClientDto);
			if (htAnzahlRahmenbestellt.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
				zeile[REPORT_BESCHAFFUNG_RAHMENBESTELLT] = (BigDecimal) htAnzahlRahmenbestellt
						.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreis(artikelIId, null, menge,
				theClientDto.getSMandantenwaehrung(), dStart, theClientDto);

		if (alDto != null) {
			zeile[REPORT_BESCHAFFUNG_EK_PREIS] = alDto.getLief1Preis();
			zeile[REPORT_BESCHAFFUNG_WBZ] = alDto.getIWiederbeschaffungszeit();
			zeile[REPORT_BESCHAFFUNG_LIEFERANT] = alDto.getLieferantDto().getPartnerDto()
					.getCName1nachnamefirmazeile1();

			zeile[REPORT_BESCHAFFUNG_ARTIKELNUMMER_LIEFERANT] = alDto.getCArtikelnrlieferant();
			zeile[REPORT_BESCHAFFUNG_ARTIKELBEZEICHNUNG_LIEFERANT] = alDto.getCBezbeilieferant();

		}

		return zeile;

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printForecastpositionen(Integer artikelIId, boolean bAlleMandanten,
			TheClientDto theClientDto) {

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		ArtikelDto dto = getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);
		parameter.put("P_ARTIKELNUMMER", dto.getCNr());
		parameter.put("P_ARTIKELBEZEICHNUNG", dto.getCBezAusSpr());
		parameter.put("P_ARTIKELKURZBEZEICHNUNG", dto.getKbezAusSpr());
		parameter.put("P_ARTIKELZUSATZBEZEICHNUNG", dto.getCZBezAusSpr());
		parameter.put("P_ARTIKELZUSATZBEZEICHNUNG2", dto.getCZBez2AusSpr());
		parameter.put("P_ARTIKELREFERENZNUMMER", dto.getCReferenznr());

		parameter.put("P_ALLE_MANDANTEN", bAlleMandanten);

		ArrayList alDaten = new ArrayList();

		sAktuellerReport = ForecastReportFac.REPORT_FORECASTPOSITIONEN;

		String sQuery = "SELECT fp FROM FLRForecastposition fp WHERE fp.flrartikel.i_id=" + artikelIId;
		if (bAlleMandanten == false) {
			sQuery += " AND fp.flrforecastauftrag.flrfclieferadresse.flrforecast.flrkunde.mandant_c_nr='"
					+ theClientDto.getMandant() + "' ";
		}

		sQuery += "  ORDER BY fp.t_termin DESC ";

		sQuery += "";
		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Query query = session.createQuery(sQuery);

		List l = query.list();

		Iterator it = l.iterator();

		while (it.hasNext()) {

			FLRForecastposition fp = (FLRForecastposition) it.next();
			Object[] oZeileForecast = new Object[REPORT_FORECASTPOSITIONEN_ANZAHL_SPALTEN];

			oZeileForecast[REPORT_FORECASTPOSITIONEN_I_ID] = fp.getI_id();

			oZeileForecast[REPORT_FORECASTPOSITIONEN_BESTELLNUMMER] = fp.getC_bestellnummer();
			oZeileForecast[REPORT_FORECASTPOSITIONEN_FORECASTART] = HelperServer
					.getForecastartEienrForecastposition(fp);
			oZeileForecast[REPORT_FORECASTPOSITIONEN_FORECASTART_MANUELL] = fp.getForecastart_c_nr();
			oZeileForecast[REPORT_FORECASTPOSITIONEN_KOMMENTAR] = fp.getX_kommentar();
			oZeileForecast[REPORT_FORECASTPOSITIONEN_MENGE] = fp.getN_menge();
			oZeileForecast[REPORT_FORECASTPOSITIONEN_STATUS] = fp.getStatus_c_nr();
			oZeileForecast[REPORT_FORECASTPOSITIONEN_TERMIN] = fp.getT_termin();

			oZeileForecast[REPORT_FORECASTPOSITIONEN_FORECAST_KENNUNG] = fp.getFlrforecastauftrag()
					.getFlrfclieferadresse().getFlrforecast().getC_nr();
			oZeileForecast[REPORT_FORECASTPOSITIONEN_FORECAST_KUNDE] = HelperServer.formatNameAusFLRPartner(
					fp.getFlrforecastauftrag().getFlrfclieferadresse().getFlrforecast().getFlrkunde().getFlrpartner());
			oZeileForecast[REPORT_FORECASTPOSITIONEN_MANDANT] = fp.getFlrforecastauftrag().getFlrfclieferadresse()
					.getFlrforecast().getFlrkunde().getMandant_c_nr();
			oZeileForecast[REPORT_FORECASTPOSITIONEN_FORECAST_PROJEKT] = fp.getFlrforecastauftrag()
					.getFlrfclieferadresse().getFlrforecast().getC_projekt();

			oZeileForecast[REPORT_FORECASTPOSITIONEN_LIEFERADRESSE] = HelperServer.formatNameAusFLRPartner(
					fp.getFlrforecastauftrag().getFlrfclieferadresse().getFlrkunde_lieferadresse().getFlrpartner());

			oZeileForecast[REPORT_FORECASTPOSITIONEN_FORECASTAUFTRAG_BEMERKUNG] = fp.getFlrforecastauftrag()
					.getC_bemerkung();
			oZeileForecast[REPORT_FORECASTPOSITIONEN_FORECASTAUFTRAG_FREIGABEDATUM] = fp.getFlrforecastauftrag()
					.getT_freigabe();
			oZeileForecast[REPORT_FORECASTPOSITIONEN_FORECASTAUFTRAG_ANLAGEDATUM] = fp.getFlrforecastauftrag()
					.getT_anlegen();
			oZeileForecast[REPORT_FORECASTPOSITIONEN_FORECASTAUFTRAG_KOMMENTAR] = fp.getFlrforecastauftrag()
					.getX_kommentar();
			oZeileForecast[REPORT_FORECASTPOSITIONEN_FORECASTAUFTRAG_STATUS] = fp.getFlrforecastauftrag()
					.getStatus_c_nr();

			if (fp.getLosset() != null && fp.getLosset().size() > 0) {
				Iterator itLose = fp.getLosset().iterator();
				String lose = "";

				while (itLose.hasNext()) {
					FLRLos la = (FLRLos) itLose.next();

					lose += la.getC_nr();

					if (itLose.hasNext()) {
						lose += ",";
					}

				}

				oZeileForecast[REPORT_FORECASTPOSITIONEN_LOSE] = lose;
			}

			if (fp.getLinienabrufset() != null && fp.getLinienabrufset().size() > 0) {

				Iterator itLinienabrufe = fp.getLinienabrufset().iterator();
				while (itLinienabrufe.hasNext()) {

					FLRLinienabruf la = (FLRLinienabruf) itLinienabrufe.next();

					Object[] oZeileLinienabruf = oZeileForecast.clone();
					oZeileLinienabruf[REPORT_FORECASTPOSITIONEN_LINIENABRUF_BEREICH] = la.getC_bereich_nr();
					oZeileLinienabruf[REPORT_FORECASTPOSITIONEN_LINIENABRUF_BEREICH_BEZ] = la.getC_bereich_bez();
					oZeileLinienabruf[REPORT_FORECASTPOSITIONEN_LINIENABRUF_BESTELLNUMMER] = la.getC_bestellnummer();
					oZeileLinienabruf[REPORT_FORECASTPOSITIONEN_LINIENABRUF_LINIE] = la.getC_linie();
					oZeileLinienabruf[REPORT_FORECASTPOSITIONEN_LINIENABRUF_MENGE] = la.getN_menge();
					oZeileLinienabruf[REPORT_FORECASTPOSITIONEN_LINIENABRUF_PRODUKTIONSDATUM] = la
							.getT_produktionstermin();
					alDaten.add(oZeileLinienabruf);
				}

			} else {
				alDaten.add(oZeileForecast);
			}

		}

		data = new Object[alDaten.size()][REPORT_FORECASTPOSITIONEN_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(parameter, ForecastReportFac.REPORT_MODUL, ForecastReportFac.REPORT_FORECASTPOSITIONEN,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printLiefersituation(Integer forecastauftragIId, TheClientDto theClientDto) {

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		ForecastauftragDto fcaDto = getForecastFac().forecastauftragFindByPrimaryKey(forecastauftragIId);
		FclieferadresseDto fclDto = getForecastFac().fclieferadresseFindByPrimaryKey(fcaDto.getFclieferadresseIId());
		ForecastDto fDto = getForecastFac().forecastFindByPrimaryKey(fclDto.getForecastIId());

		parameter.put("P_KUNDE", getKundeFac().kundeFindByPrimaryKey(fDto.getKundeIId(), theClientDto).getPartnerDto()
				.formatFixName1Name2());

		KundeDto kdLieferadresseDto = getKundeFac().kundeFindByPrimaryKey(fclDto.getKundeIIdLieferadresse(),
				theClientDto);

		parameter.put("P_LIEFERADRESSE", kdLieferadresseDto.getPartnerDto().formatFixName1Name2());

		parameter.put("P_ANLAGEDATUM", fcaDto.getTAnlegen());
		parameter.put("P_FREIGABEDATUM", fcaDto.getTFreigabe());

		parameter.put("P_LIEFERDAUER", kdLieferadresseDto.getILieferdauer());
		parameter.put("P_FORECASTNUMMER", fDto.getCNr());
		parameter.put("P_PROJEKT", fDto.getCProjekt());
		parameter.put("P_STATUS", fcaDto.getStatusCNr());
		parameter.put("P_KOMMENTAT", fcaDto.getXKommentar());

		HashMap<Integer, Object[][]> hmChacheLieferstatistik = new HashMap<Integer, Object[][]>();
		HashMap<Integer, ArtikelDto> hmChacheArtikelDto = new HashMap<Integer, ArtikelDto>();

		ArrayList alDaten = new ArrayList();

		sAktuellerReport = ForecastReportFac.REPORT_LIEFERSITUATION;

		String sQueryFP = "SELECT fp FROM FLRForecastpositionLiefersituation fp WHERE fp.flrforecastauftrag.i_id="
				+ forecastauftragIId + "  ORDER BY fp.flrartikel.c_nr ASC, fp.t_termin ASC";

		sQueryFP += "";
		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Query query = session.createQuery(sQueryFP);

		List l = query.list();

		Iterator it = l.iterator();

		while (it.hasNext()) {

			FLRForecastpositionLiefersituation fp = (FLRForecastpositionLiefersituation) it.next();
			ForecastpositionDto fpDto = getForecastFac().forecastpositionFindByPrimaryKey(fp.getI_id());

			Object[] oZeileForecast = new Object[REPORT_LIEFERSITUATION_ANZAHL_SPALTEN];

			oZeileForecast[REPORT_LIEFERSITUATION_BESTELLNUMMER] = fp.getC_bestellnummer();
			oZeileForecast[REPORT_LIEFERSITUATION_FORECASTART] = HelperServer.getForecastartEienrForecastposition(fp);
			oZeileForecast[REPORT_LIEFERSITUATION_FORECASTART_MANUELL] = fp.getForecastart_c_nr();
			oZeileForecast[REPORT_LIEFERSITUATION_KOMMENTAR] = fp.getX_kommentar();
			oZeileForecast[REPORT_LIEFERSITUATION_MENGE] = fp.getN_menge();

			oZeileForecast[REPORT_LIEFERSITUATION_LINIENABRUF_MENGE] = getForecastFac()
					.getSummeLinienabrufe(fp.getI_id());

			oZeileForecast[REPORT_LIEFERSITUATION_ERFASSTE_MENGE] = fpDto.getNMengeErfasst();
			oZeileForecast[REPORT_LIEFERSITUATION_ARTIKELNUMMER] = fp.getFlrartikel().getC_nr();

			if (!hmChacheArtikelDto.containsKey(fp.getFlrartikel().getI_id())) {
				hmChacheArtikelDto.put(fp.getFlrartikel().getI_id(),
						getArtikelFac().artikelFindByPrimaryKeySmall(fp.getFlrartikel().getI_id(), theClientDto));
			}

			ArtikelDto aDto = hmChacheArtikelDto.get(fp.getFlrartikel().getI_id());

			if (aDto.getIId().equals(new Integer(5457))) {
				myLogger.warn("halt");
			}
			if (aDto.getArtikelsprDto() != null) {
				oZeileForecast[REPORT_LIEFERSITUATION_BEZEICHNUNG] = aDto.getArtikelsprDto().getCBez();
				oZeileForecast[REPORT_LIEFERSITUATION_KURZBEZEICHNUNG] = aDto.getArtikelsprDto().getCKbez();
				oZeileForecast[REPORT_LIEFERSITUATION_ZUSATZBEZEICHNUNG] = aDto.getArtikelsprDto().getCZbez();
				oZeileForecast[REPORT_LIEFERSITUATION_ZUSATZBEZEICHNUNG2] = aDto.getArtikelsprDto().getCZbez2();
			}

			oZeileForecast[REPORT_LIEFERSITUATION_STATUS] = fp.getStatus_c_nr();
			oZeileForecast[REPORT_LIEFERSITUATION_TERMIN] = fp.getT_termin();

			Session session2 = FLRSessionFactory.getFactory().openSession();

			String sQuery = "SELECT lm FROM FLRLiefermengen lm WHERE kunde_i_id_lieferadresse="
					+ fp.getFlrforecastauftrag().getFlrfclieferadresse().getKunde_i_id_lieferadresse()
					+ " AND lm.flrartikel.i_id=" + fp.getFlrartikel().getI_id() + " ORDER BY  lm.t_datum DESC";

			Query inventurliste = session2.createQuery(sQuery);

			List<?> resultList = inventurliste.list();

			Iterator<?> resultListIterator = resultList.iterator();

			if (resultListIterator.hasNext()) {

				FLRLiefermengen lm = (FLRLiefermengen) resultListIterator.next();

				if (!hmChacheLieferstatistik.containsKey(fp.getFlrartikel().getI_id())) {

					Object[][] zeilenLieferstatistik = getKundeReportFac().getDataLieferstatistik(theClientDto,
							fp.getFlrforecastauftrag().getFlrfclieferadresse().getKunde_i_id_lieferadresse(),
							aDto.getIId(), null, null, null, Helper.SORTIERUNG_NACH_DATUM, null, false, false, false,
							false, KundeReportFac.REPORT_LIEFERSTATISTIK_OPTION_LIEFERADRESSE, false);

					hmChacheLieferstatistik.put(fp.getFlrartikel().getI_id(), zeilenLieferstatistik);
				}

				Object[] oZeileLM = getKundeReportFac().befuelleLiefermengenReportZeile(
						fp.getFlrforecastauftrag().getFlrfclieferadresse().getKunde_i_id_lieferadresse(), theClientDto,
						lm.getI_id(), hmChacheLieferstatistik.get(fp.getFlrartikel().getI_id()), aDto);

				oZeileForecast[REPORT_LIEFERSITUATION_LIEFERMENGEN_DATUM_LETZTER_STARTWERT] = oZeileLM[KundeReportFac.REPORT_LIEFERMENGEN_DATUM_LETZTER_STARTWERT];
				oZeileForecast[REPORT_LIEFERSITUATION_LIEFERMENGE_GESAMT] = oZeileLM[KundeReportFac.REPORT_LIEFERMENGEN_LIEFERMENGE_GESAMT];
				oZeileForecast[REPORT_LIEFERSITUATION_LIEFERMENGEN_LETZTER_STARTWERT] = oZeileLM[KundeReportFac.REPORT_LIEFERMENGEN_LETZTER_STARTWERT];
				oZeileForecast[REPORT_LIEFERSITUATION_LIEFERMENGEN_MENGE_UNTERWEGS] = oZeileLM[KundeReportFac.REPORT_LIEFERMENGEN_MENGE_UNTERWEGS];
			}

			session2.close();

			// Liefermengenfortschrittszahl

			int iAnzahlMax = 0;

			FLRLieferscheinposition[] lspos = new FLRLieferscheinposition[fp.getLieferscheinpositionen_set().size()];

			lspos = (FLRLieferscheinposition[]) fp.getLieferscheinpositionen_set().toArray(lspos);

			FLRLos[] lose = new FLRLos[fp.getLose_set().size()];

			lose = (FLRLos[]) fp.getLose_set().toArray(lose);

			iAnzahlMax = lspos.length;
			if (lose.length > iAnzahlMax) {
				iAnzahlMax = lose.length;
			}

			if (iAnzahlMax == 0) {
				alDaten.add(oZeileForecast);
			} else {

				for (int i = 0; i < iAnzahlMax; i++) {

					Object[] oZeileTemp = oZeileForecast.clone();

					if (i < lspos.length) {
						FLRLieferscheinposition flrLieferscheinposition = lspos[i];

						oZeileTemp[REPORT_LIEFERSITUATION_LIEFERSCHEINNUMMER] = flrLieferscheinposition
								.getFlrlieferschein().getC_nr();
						oZeileTemp[REPORT_LIEFERSITUATION_LIEFERSCHEIN_DATUM] = flrLieferscheinposition
								.getFlrlieferschein().getD_belegdatum();
						oZeileTemp[REPORT_LIEFERSITUATION_LIEFERSCHEIN_STATUS] = flrLieferscheinposition
								.getFlrlieferschein().getLieferscheinstatus_status_c_nr();
						if (flrLieferscheinposition.getFlrlieferschein().getFlrrechnung() != null) {
							oZeileTemp[REPORT_LIEFERSITUATION_LIEFERSCHEIN_RECHNUNGNR] = flrLieferscheinposition
									.getFlrlieferschein().getFlrrechnung().getC_nr();
						}
						oZeileTemp[REPORT_LIEFERSITUATION_LIEFERSCHEIN_MENGE] = flrLieferscheinposition.getN_menge();
					}
					if (i < lose.length) {

						FLRLos flrLos = lose[i];
						oZeileTemp[REPORT_LIEFERSITUATION_LOSNUMMER] = flrLos.getC_nr();

						BigDecimal bdAbgeliefert = BigDecimal.ZERO;

						Iterator itAbl = flrLos.getAblieferungset().iterator();

						while (itAbl.hasNext()) {
							bdAbgeliefert = bdAbgeliefert.add(((FLRLosablieferung) itAbl.next()).getN_menge());
						}

						oZeileTemp[REPORT_LIEFERSITUATION_LOS_ABLIEFERMENGE] = bdAbgeliefert;
						oZeileTemp[REPORT_LIEFERSITUATION_LOS_GROESSE] = flrLos.getN_losgroesse();
						oZeileTemp[REPORT_LIEFERSITUATION_LOS_STATUS] = flrLos.getStatus_c_nr();

					}

					alDaten.add(oZeileTemp);

				}

			}

		}

		data = new Object[alDaten.size()][REPORT_LIEFERSITUATION_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(parameter, ForecastReportFac.REPORT_MODUL, ForecastReportFac.REPORT_LIEFERSITUATION,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printGeplanterUmsatz(Integer forecastIId, int iOption, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, String artikelNrVon, String artikelNrBis, TheClientDto theClientDto) {
		sAktuellerReport = ForecastReportFac.REPORT_GEPLANTERUMSATZ;
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		tVon = Helper.cutTimestamp(tVon);

		Session session2 = FLRSessionFactory.getFactory().openSession();

		// session2.enableFilter("filterLocale").setParameter("paramLocale",
		// theClientDto.getLocUi());

		String sQueryForecast = "SELECT fp, aspr FROM FLRForecastposition fp LEFT OUTER JOIN fp.flrartikelliste.artikelsprset AS aspr  LEFT OUTER JOIN fp.flrartikel.flrartikelgruppe AS ag WHERE fp.flrforecastauftrag.flrfclieferadresse.flrforecast.flrkunde.mandant_c_nr='"
				+ theClientDto.getMandant() + "' AND fp.flrforecastauftrag.flrfclieferadresse.flrforecast.status_c_nr='"
				+ LocaleFac.STATUS_ANGELEGT + "' AND fp.status_c_nr='" + LocaleFac.STATUS_ANGELEGT
				+ "' AND fp.flrforecastauftrag.status_c_nr='" + LocaleFac.STATUS_FREIGEGEBEN + "'";

		{
			sQueryForecast += " AND fp.t_termin >='" + Helper.formatDateWithSlashes(new java.sql.Date(tVon.getTime()))
					+ "'";
			parameter.put("P_VON", tVon);
		}

		parameter.put("P_BIS", tBis);

		sQueryForecast += " AND fp.t_termin <'"
				+ Helper.formatDateWithSlashes(Helper.addiereTageZuDatum(new java.sql.Date(tBis.getTime()), 1)) + "'";

		if (artikelNrVon != null) {
			sQueryForecast += " AND fp.flrartikel.c_nr>='" + artikelNrVon + "'";

			parameter.put("P_ARTIKELNUMMER_VON", artikelNrVon);

		}
		if (artikelNrBis != null) {
			sQueryForecast += " AND fp.flrartikel.c_nr<='" + Helper.fitString2Length(artikelNrBis, 25, '_') + "'";

			parameter.put("P_ARTIKELNUMMER_BIS", artikelNrBis);

		}

		if (forecastIId != null) {
			sQueryForecast += " AND fp.flrforecastauftrag.flrfclieferadresse.forecast_i_id=" + forecastIId;

			ForecastDto fcDto = getForecastFac().forecastFindByPrimaryKey(forecastIId);

			parameter.put("P_FORECAST", fcDto.getCNr());

		}

		if (iOption == GEPLANTERUMSATZ_OPTION_ARTIKELGRUPPE) {
			sQueryForecast += " ORDER BY ag.c_nr, fp.flrartikel.c_nr ";

			parameter.put("P_SORTIERT_NACH_ARTIKEL", Boolean.FALSE);

		} else {
			sQueryForecast += " ORDER BY fp.flrartikel.c_nr ";

			parameter.put("P_SORTIERT_NACH_ARTIKEL", Boolean.TRUE);

		}

		org.hibernate.Query queryForecast = session2.createQuery(sQueryForecast);

		List lForecast = queryForecast.list();
		Iterator<?> itForecast = lForecast.iterator();

		ArrayList<Object[]> alDaten = new ArrayList<Object[]>();

		LinkedHashMap<Integer, LinkedHashMap<Integer, Object[]>> lhmArtikel = new LinkedHashMap<Integer, LinkedHashMap<Integer, Object[]>>();

		LinkedHashMap<java.util.Date, BigDecimal> lhmMengenMuster = new LinkedHashMap<java.util.Date, BigDecimal>();

		Calendar cAktuell = Calendar.getInstance();

		cAktuell.setTimeInMillis(tVon.getTime());
		cAktuell.set(Calendar.DAY_OF_MONTH, 1);

		while (tBis.getTime() > cAktuell.getTimeInMillis()) {

			lhmMengenMuster.put(cAktuell.getTime(), BigDecimal.ZERO);

			cAktuell.add(Calendar.MONTH, 1);

		}

		while (itForecast.hasNext()) {

			Object[] oTemp = (Object[]) itForecast.next();

			FLRForecastposition flrForecastposition = (FLRForecastposition) oTemp[0];

			Integer kundeIId = flrForecastposition.getFlrforecastauftrag().getFlrfclieferadresse().getFlrforecast()
					.getFlrkunde().getI_id();

			FLRArtikellistespr spr = (FLRArtikellistespr) oTemp[1];

			Object[] zeile = new Object[REPORT_GEPLANTERUMSATZ_ANZAHL_SPALTEN];

			Integer artikelIId = flrForecastposition.getFlrartikel().getI_id();

			LinkedHashMap<Integer, Object[]> lhmKunde = lhmArtikel.get(artikelIId);
			if (lhmArtikel.containsKey(artikelIId)) {
				lhmKunde = lhmArtikel.get(artikelIId);
			} else {
				lhmKunde = new LinkedHashMap<Integer, Object[]>();
			}

			if (lhmKunde.containsKey(kundeIId)) {
				zeile = lhmKunde.get(kundeIId);
			} else {
				zeile = new Object[REPORT_GEPLANTERUMSATZ_ANZAHL_SPALTEN];

				zeile[REPORT_GEPLANTERUMSATZ_ARTIKEL_I_ID_INTERN] = artikelIId;

				zeile[REPORT_GEPLANTERUMSATZ_KUNDE_DTO_INTERN] = getKundeFac().kundeFindByPrimaryKeySmall(kundeIId);

				zeile[REPORT_GEPLANTERUMSATZ_KUNDE] = HelperServer
						.formatNameAusFLRPartner(flrForecastposition.getFlrforecastauftrag().getFlrfclieferadresse()
								.getFlrforecast().getFlrkunde().getFlrpartner());

				zeile[REPORT_GEPLANTERUMSATZ_ARTIKELNUMMER] = flrForecastposition.getFlrartikel().getC_nr();

				if (flrForecastposition.getFlrartikel().getFlrartikelgruppe() != null) {
					zeile[REPORT_GEPLANTERUMSATZ_ARTIKELGRUPPE] = flrForecastposition.getFlrartikel()
							.getFlrartikelgruppe().getC_nr();
				}

				zeile[REPORT_GEPLANTERUMSATZ_REFERENZNUMMER] = flrForecastposition.getFlrartikel().getC_referenznr();
				zeile[REPORT_GEPLANTERUMSATZ_INDEX] = flrForecastposition.getFlrartikelliste().getC_index();
				zeile[REPORT_GEPLANTERUMSATZ_REVISION] = flrForecastposition.getFlrartikelliste().getC_revision();
				zeile[REPORT_GEPLANTERUMSATZ_EINHEIT] = flrForecastposition.getFlrartikel().getEinheit_c_nr();

				if (spr != null) {
					zeile[REPORT_GEPLANTERUMSATZ_BEZEICHNUNG] = spr.getC_bez();
					zeile[REPORT_GEPLANTERUMSATZ_KURZBEZEICHNUNG] = spr.getC_kbez();
					zeile[REPORT_GEPLANTERUMSATZ_ZUSATZBEZEICHNUNG] = spr.getC_zbez2();
					zeile[REPORT_GEPLANTERUMSATZ_ZUSATZBEZEICHNUNG2] = spr.getC_zbez2();
				}

				if (Helper.short2boolean(flrForecastposition.getFlrartikelliste().getB_lagerbewirtschaftet())) {

					zeile[REPORT_GEPLANTERUMSATZ_LAGERSTAND] = getLagerFac()
							.getLagerstandAllerLagerEinesMandanten(flrForecastposition.getI_id(), false, theClientDto);

				}
				zeile[REPORT_GEPLANTERUMSATZ_LAGERMINDESTSTAND] = flrForecastposition.getFlrartikelliste()
						.getF_lagermindest();

			}

			Calendar c = Calendar.getInstance();
			java.util.Date dErsterDesMonats = Helper.cutDate(flrForecastposition.getT_termin());
			c.setTimeInMillis(dErsterDesMonats.getTime());
			c.set(Calendar.DAY_OF_MONTH, 1);

			LinkedHashMap<java.util.Date, BigDecimal> lhmMengen = null;
			if (zeile[REPORT_GEPLANTERUMSATZ_MONATSMENGEN_INTERN] != null) {
				lhmMengen = (LinkedHashMap<java.util.Date, BigDecimal>) zeile[REPORT_GEPLANTERUMSATZ_MONATSMENGEN_INTERN];
			} else {
				lhmMengen = (LinkedHashMap<java.util.Date, BigDecimal>) lhmMengenMuster.clone();
			}

			if (lhmMengen.containsKey(dErsterDesMonats)) {
				BigDecimal bdMenge = lhmMengen.get(dErsterDesMonats);
				bdMenge = bdMenge.add(flrForecastposition.getN_menge());
				lhmMengen.put(dErsterDesMonats, bdMenge);

			}

			zeile[REPORT_GEPLANTERUMSATZ_MONATSMENGEN_INTERN] = lhmMengen;

			lhmKunde.put(kundeIId, zeile);

			lhmArtikel.put(artikelIId, lhmKunde);

		}

		Iterator itArtikel = lhmArtikel.keySet().iterator();

		while (itArtikel.hasNext()) {
			Integer artikelIId = (Integer) itArtikel.next();

			LinkedHashMap<Integer, Object[]> lhmKunde = lhmArtikel.get(artikelIId);
			Iterator itKunde = lhmKunde.keySet().iterator();
			while (itKunde.hasNext()) {

				Integer kundeIId = (Integer) itKunde.next();

				Object[] zeile = lhmKunde.get(kundeIId);

				LinkedHashMap<java.util.Date, BigDecimal> monate = (LinkedHashMap<java.util.Date, BigDecimal>) zeile[REPORT_GEPLANTERUMSATZ_MONATSMENGEN_INTERN];
				Iterator itMonat = monate.keySet().iterator();
				int iMonat = 0;

				while (itMonat.hasNext()) {
					java.util.Date dMonatserster = (java.util.Date) itMonat.next();

					KundeDto kdDto = (KundeDto) zeile[REPORT_GEPLANTERUMSATZ_KUNDE_DTO_INTERN];

					BigDecimal bdMenge = monate.get(dMonatserster);
					BigDecimal preis = BigDecimal.ZERO;
					if (bdMenge.doubleValue() != 0) {

						MwstsatzDto mwstsatzDtoAktuell = getMandantFac().mwstsatzZuDatumValidate(
								kdDto.getMwstsatzbezIId(), new Timestamp(dMonatserster.getTime()), theClientDto);

						VkpreisfindungDto vkpDto = getVkPreisfindungFac().verkaufspreisfindung(artikelIId, kundeIId,
								bdMenge, new java.sql.Date(dMonatserster.getTime()),
								kdDto.getVkpfArtikelpreislisteIIdStdpreisliste(), mwstsatzDtoAktuell.getIId(),
								theClientDto.getSMandantenwaehrung(), theClientDto);

						VerkaufspreisDto kundenVKPreisDto = Helper.getVkpreisBerechnet(vkpDto);

						if (kundenVKPreisDto != null && kundenVKPreisDto.nettopreis != null) {
							preis = kundenVKPreisDto.nettopreis;
						}
					}

					if (iMonat == 0) {
						zeile[REPORT_GEPLANTERUMSATZ_MENGE_MONAT01] = bdMenge;
						zeile[REPORT_GEPLANTERUMSATZ_VKWERT_MONAT01] = bdMenge.multiply(preis);
					} else if (iMonat == 1) {
						zeile[REPORT_GEPLANTERUMSATZ_MENGE_MONAT02] = bdMenge;
						zeile[REPORT_GEPLANTERUMSATZ_VKWERT_MONAT02] = bdMenge.multiply(preis);
					} else if (iMonat == 2) {
						zeile[REPORT_GEPLANTERUMSATZ_MENGE_MONAT03] = bdMenge;
						zeile[REPORT_GEPLANTERUMSATZ_VKWERT_MONAT03] = bdMenge.multiply(preis);
					} else if (iMonat == 3) {
						zeile[REPORT_GEPLANTERUMSATZ_MENGE_MONAT04] = bdMenge;
						zeile[REPORT_GEPLANTERUMSATZ_VKWERT_MONAT04] = bdMenge.multiply(preis);
					} else if (iMonat == 4) {
						zeile[REPORT_GEPLANTERUMSATZ_MENGE_MONAT05] = bdMenge;
						zeile[REPORT_GEPLANTERUMSATZ_VKWERT_MONAT05] = bdMenge.multiply(preis);
					} else if (iMonat == 5) {
						zeile[REPORT_GEPLANTERUMSATZ_MENGE_MONAT06] = bdMenge;
						zeile[REPORT_GEPLANTERUMSATZ_VKWERT_MONAT06] = bdMenge.multiply(preis);
					} else if (iMonat == 6) {
						zeile[REPORT_GEPLANTERUMSATZ_MENGE_MONAT07] = bdMenge;
						zeile[REPORT_GEPLANTERUMSATZ_VKWERT_MONAT07] = bdMenge.multiply(preis);
					} else if (iMonat == 7) {
						zeile[REPORT_GEPLANTERUMSATZ_MENGE_MONAT08] = bdMenge;
						zeile[REPORT_GEPLANTERUMSATZ_VKWERT_MONAT08] = bdMenge.multiply(preis);
					} else if (iMonat == 8) {
						zeile[REPORT_GEPLANTERUMSATZ_MENGE_MONAT09] = bdMenge;
						zeile[REPORT_GEPLANTERUMSATZ_VKWERT_MONAT09] = bdMenge.multiply(preis);
					} else if (iMonat == 9) {
						zeile[REPORT_GEPLANTERUMSATZ_MENGE_MONAT10] = bdMenge;
						zeile[REPORT_GEPLANTERUMSATZ_VKWERT_MONAT10] = bdMenge.multiply(preis);
					} else if (iMonat == 10) {
						zeile[REPORT_GEPLANTERUMSATZ_MENGE_MONAT11] = bdMenge;
						zeile[REPORT_GEPLANTERUMSATZ_VKWERT_MONAT11] = bdMenge.multiply(preis);
					} else if (iMonat == 11) {
						zeile[REPORT_GEPLANTERUMSATZ_MENGE_MONAT12] = bdMenge;
						zeile[REPORT_GEPLANTERUMSATZ_VKWERT_MONAT12] = bdMenge.multiply(preis);
					}

					iMonat++;

				}
				alDaten.add(zeile);
			}

		}

		data = new Object[alDaten.size()][REPORT_GEPLANTERUMSATZ_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(parameter, ForecastReportFac.REPORT_MODUL, ForecastReportFac.REPORT_GEPLANTERUMSATZ,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printDeltaliste(Integer fclieferadresseIId, boolean bMitStuecklistePositionen,
			TheClientDto theClientDto) {
		sAktuellerReport = ForecastReportFac.REPORT_DELTALISTE;

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		FclieferadresseDto fclDto = getForecastFac().fclieferadresseFindByPrimaryKey(fclieferadresseIId);

		ForecastDto fDto = getForecastFac().forecastFindByPrimaryKey(fclDto.getForecastIId());

		parameter.put("P_KUNDE", getKundeFac().kundeFindByPrimaryKey(fDto.getKundeIId(), theClientDto).getPartnerDto()
				.formatFixName1Name2());

		KundeDto kdLieferadresseDto = getKundeFac().kundeFindByPrimaryKey(fclDto.getKundeIIdLieferadresse(),
				theClientDto);

		parameter.put("P_LIEFERADRESSE", kdLieferadresseDto.getPartnerDto().formatFixName1Name2());
		parameter.put("P_LIEFERDAUER", kdLieferadresseDto.getILieferdauer());
		parameter.put("P_FORECASTNUMMER", fDto.getCNr());
		parameter.put("P_PROJEKT", fDto.getCProjekt());

		parameter.put("P_MIT_STUECKLISTENPOSITIONEN", bMitStuecklistePositionen);

		ArrayList<Object[]> alDaten = new ArrayList<Object[]>();

		String sQuery = "SELECT fa FROM FLRForecastauftrag fa WHERE fa.flrfclieferadresse.i_id=" + fclieferadresseIId
				+ " AND fa.status_c_nr IN ('" + LocaleFac.STATUS_ANGELEGT + "','" + LocaleFac.STATUS_FREIGEGEBEN
				+ "')  ORDER BY fa.status_c_nr  ";

		sQuery += "";
		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Query query = session.createQuery(sQuery);

		List l = query.list();

		Iterator it = l.iterator();

		FLRForecastauftrag fcaFreigegebenAlt = null;

		FLRForecastauftrag fcaNochNichtFreigegebenNeu = null;

		while (it.hasNext()) {

			FLRForecastauftrag fa = (FLRForecastauftrag) it.next();

			if (fa.getStatus_c_nr().equals(LocaleFac.STATUS_ANGELEGT)) {

				fcaNochNichtFreigegebenNeu = fa;

			} else {
				fcaFreigegebenAlt = fa;
			}
		}

		if (fcaNochNichtFreigegebenNeu != null) {
			parameter.put("P_KOMMENTAR_FORECASTAUFTRAG_ANGELEGT", getForecastFac()
					.forecastauftragFindByPrimaryKey(fcaNochNichtFreigegebenNeu.getI_id()).getXKommentar());
		}

		if (fcaFreigegebenAlt != null) {
			parameter.put("P_KOMMENTAR_FORECASTAUFTRAG_FREIGEGEBEN",
					getForecastFac().forecastauftragFindByPrimaryKey(fcaFreigegebenAlt.getI_id()).getXKommentar());
		}

		TreeMap<String, TreeMap<java.util.Date, DatenDeltaliste>> tmArtikel = new TreeMap();

		// PJ19953
		parameter.put("P_FORECASTAUFTRAGNEU", Boolean.FALSE);
		parameter.put("P_FORECASTAUFTRAGALT", Boolean.FALSE);

		if (fcaFreigegebenAlt != null) {
			tmArtikel = addToDeltaliste(fcaFreigegebenAlt, tmArtikel, true);
			parameter.put("P_FORECASTAUFTRAGALT", Boolean.TRUE);
		}
		if (fcaNochNichtFreigegebenNeu != null) {
			tmArtikel = addToDeltaliste(fcaNochNichtFreigegebenNeu, tmArtikel, false);
			parameter.put("P_FORECASTAUFTRAGNEU", Boolean.TRUE);
		}

		Iterator itArtikel = tmArtikel.keySet().iterator();
		while (itArtikel.hasNext()) {
			String artikel = (String) itArtikel.next();
			TreeMap<java.util.Date, DatenDeltaliste> tmDatum = tmArtikel.get(artikel);

			ArrayList<FLRStuecklisteposition> flrStklpos = new ArrayList<FLRStuecklisteposition>();

			if (bMitStuecklistePositionen) {

				Session sessionStklPos = FLRSessionFactory.getFactory().openSession();

				String sQueryStklpos = "SELECT pos FROM FLRStuecklisteposition pos WHERE pos.flrstueckliste.flrartikel.c_nr='"
						+ artikel + "' ORDER BY pos.i_sort";

				org.hibernate.Query queryStklpos = sessionStklPos.createQuery(sQueryStklpos);

				List listStklpos = queryStklpos.list();
				Iterator itStklpos = listStklpos.iterator();

				while (itStklpos.hasNext()) {
					FLRStuecklisteposition item = (FLRStuecklisteposition) itStklpos.next();
					flrStklpos.add(item);
				}

			}

			boolean bErsteZeile = true;

			Iterator itDatum = tmDatum.keySet().iterator();
			while (itDatum.hasNext()) {
				java.util.Date d = (java.util.Date) itDatum.next();

				DatenDeltaliste datenDeltaliste = tmDatum.get(d);

				Object[] zeile = new Object[REPORT_DELTALISTE_ANZAHL_SPALTEN];
				zeile[REPORT_DELTALISTE_DATUM] = d;
				zeile[REPORT_DELTALISTE_ARTIKELNUMMER] = artikel;
				try {
					befuelleZeileMitArtikeldaten(theClientDto, artikel, bErsteZeile, zeile);

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				zeile[REPORT_DELTALISTE_MENGE_ALT] = datenDeltaliste.bdMengeAlt;
				zeile[REPORT_DELTALISTE_MENGE_NEU] = datenDeltaliste.bdMengeNeu;

				zeile[REPORT_DELTALISTE_LINIENABRUFE_ALT] = datenDeltaliste.bdMengeLinienabrufeAlt;
				zeile[REPORT_DELTALISTE_LINIENABRUFE_NEU] = datenDeltaliste.bdMengeLinienabrufeNeu;

				zeile[REPORT_DELTALISTE_KOMMENTAR_ALT] = datenDeltaliste.kommentarAlt;
				zeile[REPORT_DELTALISTE_KOMMENTAR_NEU] = datenDeltaliste.kommentarNeu;
				if (datenDeltaliste.forecastpositionIId != null) {
					zeile[REPORT_DELTALISTE_FORECASTART] = getForecastFac()
							.getForecastartEienrForecastposition(datenDeltaliste.forecastpositionIId);
					zeile[REPORT_DELTALISTE_FORECASTPOSITION_ID] = datenDeltaliste.forecastpositionIId;
					zeile[REPORT_DELTALISTE_STATUS_ALT_CNR] = datenDeltaliste.getStatusCnrAlt();
					zeile[REPORT_DELTALISTE_STATUS_NEU_CNR] = datenDeltaliste.getStatusCnrNeu();
					zeile[REPORT_DELTALISTE_FORECASTPOSITION_NEU_ID] = datenDeltaliste.getForecastpositionIIdNeu();
				}

				if (bMitStuecklistePositionen && flrStklpos.size() > 0) {
					alDaten.addAll(umStuecklistenpositionenErweitern(zeile, flrStklpos, datenDeltaliste.bdMengeAlt,
							theClientDto));
				} else {
					alDaten.add(zeile);
				}

				bErsteZeile = false;
			}

		}

		session.close();

		// Nun alle Artikel des Forecasts noch hinzufuegen, welche in vorherigen
		// Forecasts verwendet wurden, jetzt jeoch nicht angezeigt wurden

		sQuery = "SELECT distinct fp.flrartikel.c_nr FROM FLRForecastposition fp WHERE fp.flrforecastauftrag.flrfclieferadresse.i_id="
				+ fclieferadresseIId
				+ " AND fp.flrforecastauftrag.t_freigabe IS NOT NULL AND fp.flrforecastauftrag.status_c_nr<>'"
				+ LocaleFac.STATUS_ERLEDIGT + "' ORDER BY fp.flrartikel.c_nr ";

		sQuery += "";
		session = FLRSessionFactory.getFactory().openSession();
		query = session.createQuery(sQuery);

		List listAlleArtikel = query.list();
		Iterator itNN = listAlleArtikel.iterator();
		while (itNN.hasNext()) {
			String artikelnr = (String) itNN.next();

			System.out.println(artikelnr);

			if (!tmArtikel.containsKey(artikelnr)) {

				Object[] zeile = new Object[REPORT_DELTALISTE_ANZAHL_SPALTEN];
				zeile[REPORT_DELTALISTE_DATUM] = null;
				zeile[REPORT_DELTALISTE_ARTIKELNUMMER] = artikelnr;
				try {
					befuelleZeileMitArtikeldaten(theClientDto, artikelnr, true, zeile);

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
				alDaten.add(zeile);
			}

		}

		data = new Object[alDaten.size()][REPORT_DELTALISTE_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(parameter, ForecastReportFac.REPORT_MODUL, ForecastReportFac.REPORT_DELTALISTE,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();

	}

	private void befuelleZeileMitArtikeldaten(TheClientDto theClientDto, String artikel, boolean bErsteZeile,
			Object[] zeile) throws RemoteException {
		ArtikelDto aDto = getArtikelFac().artikelFindByCNrOhneExc(artikel, theClientDto);

		if (aDto != null) {
			zeile[REPORT_DELTALISTE_EINHEIT] = aDto.getEinheitCNr();

			ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreisDesBevorzugtenLieferanten(aDto.getIId(),
					BigDecimal.ONE, theClientDto.getSMandantenwaehrung(), theClientDto);
			if (alDto != null) {
				zeile[REPORT_DELTALISTE_WBZ_LIEF1] = alDto.getIWiederbeschaffungszeit();
			}

			// PJ21943
			if (aDto.getArtikelIIdErsatz() != null) {
				ArtikelDto aDtoErsatz = getArtikelFac().artikelFindByPrimaryKeySmall(aDto.getArtikelIIdErsatz(),
						theClientDto);
				zeile[REPORT_DELTALISTE_ERSATZARTIKEL_NUMMER] = aDtoErsatz.getCNr();
				zeile[REPORT_DELTALISTE_ERSATZARTIKEL_BEZEICHNUNG] = aDtoErsatz.getCZBezAusSpr();

				if (bErsteZeile == true) {

					zeile[REPORT_DELTALISTE_ERSATZARTIKEL_IN_FERTIGUNG] = getFertigungFac()
							.getAnzahlInFertigung(aDtoErsatz.getIId(), theClientDto);
					zeile[REPORT_DELTALISTE_ERSATZARTIKEL_BESTELLT] = getArtikelbestelltFac()
							.getAnzahlBestellt(aDtoErsatz.getIId());

					BigDecimal rahmenbestellt = null;
					Hashtable htAnzahlRahmenbestellt = getArtikelbestelltFac()
							.getAnzahlRahmenbestellt(aDtoErsatz.getIId(), theClientDto);
					if (htAnzahlRahmenbestellt.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
						rahmenbestellt = (BigDecimal) htAnzahlRahmenbestellt
								.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
						zeile[REPORT_DELTALISTE_ERSATZARTIKEL_RAHMENBESTELLT] = rahmenbestellt;
					}

				}

			}

		}

		if (aDto != null && aDto.getArtikelsprDto() != null) {
			zeile[REPORT_DELTALISTE_BEZEICHNUNG] = aDto.getArtikelsprDto().getCBez();
		}

		if (bErsteZeile == true) {

			zeile[REPORT_DELTALISTE_IN_FERTIGUNG] = getFertigungFac().getAnzahlInFertigung(aDto.getIId(), theClientDto);
			zeile[REPORT_DELTALISTE_BESTELLT] = getArtikelbestelltFac().getAnzahlBestellt(aDto.getIId());

			BigDecimal rahmenbestellt = null;
			Hashtable htAnzahlRahmenbestellt = getArtikelbestelltFac().getAnzahlRahmenbestellt(aDto.getIId(),
					theClientDto);
			if (htAnzahlRahmenbestellt.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
				rahmenbestellt = (BigDecimal) htAnzahlRahmenbestellt.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
				zeile[REPORT_DELTALISTE_RAHMENBESTELLT] = rahmenbestellt;
			}

			zeile[REPORT_DELTALISTE_LAGERSTAND] = getLagerFac().getLagerstandAllerLagerEinesMandanten(aDto.getIId(),
					theClientDto);
		}
	}

	private ArrayList<Object[]> umStuecklistenpositionenErweitern(Object[] zeileVorlage,
			ArrayList<FLRStuecklisteposition> flrStklpos, BigDecimal bdMenge, TheClientDto theClientDto) {
		ArrayList<Object[]> alReturn = new ArrayList<Object[]>();

		for (int i = 0; i < flrStklpos.size(); i++) {
			FLRStuecklisteposition stklpos = flrStklpos.get(i);
			try {
				Object[] zeileStklpos = zeileVorlage.clone();

				if (stklpos.getFlrartikel() != null) {
					ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(stklpos.getFlrartikel().getI_id(),
							theClientDto);

					zeileStklpos[REPORT_DELTALISTE_STKLPOS_ARTIKEL] = stklpos.getFlrartikel().getC_nr();
					zeileStklpos[REPORT_DELTALISTE_STKLPOS_BEZEICHNUNG] = aDto.getCBezAusSpr();

				}

				BigDecimal bdZielmenge = getStuecklisteFac().berechneZielmenge(stklpos.getI_id(), theClientDto,
						bdMenge);

				zeileStklpos[REPORT_DELTALISTE_STKLPOS_ZIELMENGE] = bdZielmenge;

				zeileStklpos[REPORT_DELTALISTE_STKLPOS_IN_FERTIGUNG] = getFertigungFac()
						.getAnzahlInFertigung(stklpos.getFlrartikel().getI_id(), theClientDto);
				zeileStklpos[REPORT_DELTALISTE_STKLPOS_BESTELLT] = getArtikelbestelltFac()
						.getAnzahlBestellt(stklpos.getFlrartikel().getI_id());

				BigDecimal rahmenbestellt = null;
				Hashtable htAnzahlRahmenbestellt = getArtikelbestelltFac()
						.getAnzahlRahmenbestellt(stklpos.getFlrartikel().getI_id(), theClientDto);
				if (htAnzahlRahmenbestellt.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
					rahmenbestellt = (BigDecimal) htAnzahlRahmenbestellt
							.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
					zeileStklpos[REPORT_DELTALISTE_STKLPOS_RAHMENBESTELLT] = rahmenbestellt;
				}

				zeileStklpos[REPORT_DELTALISTE_STKLPOS_LAGERSTAND] = getLagerFac()
						.getLagerstandAllerLagerEinesMandanten(stklpos.getFlrartikel().getI_id(), theClientDto);
				alReturn.add(zeileStklpos);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}

		return alReturn;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printLinienabrufe(java.sql.Date dAusliefertermin, TheClientDto theClientDto) {
		sAktuellerReport = ForecastReportFac.REPORT_LINIENABRUFE;

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		parameter.put("P_BIS_AUSLIEFERTERMIN", dAusliefertermin);

		ArrayList alDaten = new ArrayList();

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT la FROM FLRLinienabruf la WHERE la.flrforecastposition.flrforecastauftrag.status_c_nr='"
				+ LocaleFac.STATUS_FREIGEGEBEN
				+ "' AND la.flrforecastposition.flrforecastauftrag.flrfclieferadresse.flrforecast.status_c_nr='"
				+ LocaleFac.STATUS_ANGELEGT + "' AND la.flrforecastposition.flrforecastauftrag.t_freigabe IS NOT NULL "
				+ " AND la.flrforecastposition.flrforecastauftrag.flrfclieferadresse.flrforecast.flrkunde.mandant_c_nr='"
				+ theClientDto.getMandant() + "'";

		// PJ19775
		// sQuery +=
		// " ORDER BY la.c_bereich_nr, la.c_linie DESC,
		// la.flrforecastposition.flrartikel.c_nr, la.c_bestellnummer";
		// PJ19820

		sQuery += " ORDER BY la.flrforecastposition.flrforecastauftrag.flrfclieferadresse.flrforecast.flrkunde.flrpartner.c_name1nachnamefirmazeile1,";
		sQuery += " la.flrforecastposition.flrforecastauftrag.flrfclieferadresse.flrkunde_lieferadresse.flrpartner.c_name1nachnamefirmazeile1,";

		// Sortierung muss gleich wie in Los/Versandetikett sein
		sQuery += " la.c_bereich_nr, la.c_linie DESC, la.flrforecastposition.flrartikel.c_nr DESC";

		org.hibernate.Query query = session.createQuery(sQuery);

		List list = query.list();
		Iterator it = list.iterator();

		while (it.hasNext()) {
			FLRLinienabruf item = (FLRLinienabruf) it.next();

			item.getFlrforecastposition().getFlrartikel().getC_nr();

			java.sql.Date dForecastDatum = new java.sql.Date(item.getFlrforecastposition().getT_termin().getTime());

			int iLieferdauer = item.getFlrforecastposition().getFlrforecastauftrag().getFlrfclieferadresse()
					.getFlrkunde_lieferadresse().getI_lieferdauer();

			Timestamp dAuslieferdatumZeile = getAusliefervorschlagFac().umKundenlieferdauerVersetzen(theClientDto,
					dForecastDatum, iLieferdauer);

			if (dAusliefertermin.getTime() >= dAuslieferdatumZeile.getTime() && item.getN_menge().doubleValue() > 0) {

				if (!getForecastFac().sindBereitsLieferscheinpositionenVorhanden(item.getI_id())) {

					// Zeile hinzufuegen
					Object[] oZeile = new Object[REPORT_LINIENABRUFE_ANZAHL_SPALTEN];

					Integer artikelIId = item.getFlrforecastposition().getFlrartikel().getI_id();

					ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(artikelIId, theClientDto);

					oZeile[REPORT_LINIENABRUFE_ARTIKELNUMMER] = aDto.getCNr();

					oZeile[REPORT_LINIENABRUFE_ARTIKEL_KOMMISSIONIEREN] = Helper
							.short2Boolean(aDto.getBKommissionieren());

					oZeile[REPORT_LINIENABRUFE_LIEFERADRESSE_KOMMISSIONIEREN] = Helper
							.short2Boolean(item.getFlrforecastposition().getFlrforecastauftrag().getFlrfclieferadresse()
									.getB_kommissionieren());

					if (aDto.getArtikelsprDto() != null) {
						oZeile[REPORT_LINIENABRUFE_BEZEICHNUNG] = aDto.getArtikelsprDto().getCBez();
						oZeile[REPORT_LINIENABRUFE_KURZBEZEICHNUNG] = aDto.getArtikelsprDto().getCKbez();
					}

					oZeile[REPORT_LINIENABRUFE_ARTIKEL_GEWICHT_IN_KG] = aDto.getFGewichtkg();
					oZeile[REPORT_LINIENABRUFE_VERPACKUNGSMENGE] = aDto.getFVerpackungsmenge();

					oZeile[REPORT_LINIENABRUFE_VERPACKUNGSMITTELMENGE] = aDto.getNVerpackungsmittelmenge();

					if (aDto.getVerpackungsmittelIId() != null) {
						VerpackungsmittelDto verpackungsmittelDto = getArtikelFac()
								.verpackungsmittelFindByPrimaryKey(aDto.getVerpackungsmittelIId(), theClientDto);
						oZeile[REPORT_LINIENABRUFE_VERPACKUNGSMITTEL_KENNUNG] = verpackungsmittelDto.getCNr();
						oZeile[REPORT_LINIENABRUFE_VERPACKUNGSMITTEL_BEZEICHNUNG] = verpackungsmittelDto
								.getBezeichnung();
						oZeile[REPORT_LINIENABRUFE_VERPACKUNGSMITTEL_GEWICHT_IN_KG] = verpackungsmittelDto
								.getNGewichtInKG();
					}

					oZeile[REPORT_LINIENABRUFE_PRODUKTIONSDATUM] = item.getT_produktionstermin();

					oZeile[REPORT_LINIENABRUFE_POSITIONSTERMIN] = item.getFlrforecastposition().getT_termin();
					oZeile[REPORT_LINIENABRUFE_FORECASTPOSITION_I_ID] = item.getForecastposition_i_id();

					oZeile[REPORT_LINIENABRUFE_AUSLIEFERTERMIN] = dAuslieferdatumZeile;

					oZeile[REPORT_LINIENABRUFE_BEREICH_NR] = item.getC_bereich_nr();
					oZeile[REPORT_LINIENABRUFE_BEREICH_BEZ] = item.getC_bereich_bez();
					oZeile[REPORT_LINIENABRUFE_BESTELLNUMMER] = item.getC_bestellnummer();
					oZeile[REPORT_LINIENABRUFE_LINIE] = item.getC_linie();
					oZeile[REPORT_LINIENABRUFE_MENGE] = item.getN_menge();

					oZeile[REPORT_LINIENABRUFE_FORECAST_AUFTRAG] = item.getFlrforecastposition().getFlrforecastauftrag()
							.getFlrfclieferadresse().getFlrforecast().getC_nr();

					oZeile[REPORT_LINIENABRUFE_FORECAST_KUNDE] = HelperServer
							.formatNameAusFLRPartner(item.getFlrforecastposition().getFlrforecastauftrag()
									.getFlrfclieferadresse().getFlrforecast().getFlrkunde().getFlrpartner());
					oZeile[REPORT_LINIENABRUFE_FORECAST_LIEFERADRESSE] = HelperServer
							.formatNameAusFLRPartner(item.getFlrforecastposition().getFlrforecastauftrag()
									.getFlrfclieferadresse().getFlrkunde_lieferadresse().getFlrpartner());

					alDaten.add(oZeile);

				}
			}

		}

		session.close();

		// PJ19843
		session = FLRSessionFactory.getFactory().openSession();
		session.enableFilter("filterLocale").setParameter("paramLocale", theClientDto.getLocUiAsString());

		sQuery = "SELECT fp,aspr FROM FLRForecastposition fp LEFT OUTER JOIN fp.flrartikelliste.artikelsprset AS aspr WHERE fp.flrforecastauftrag.status_c_nr='"
				+ LocaleFac.STATUS_FREIGEGEBEN
				+ "' AND fp.flrforecastauftrag.flrfclieferadresse.flrforecast.status_c_nr='" + LocaleFac.STATUS_ANGELEGT
				+ "' AND fp.flrforecastauftrag.t_freigabe IS NOT NULL "
				+ " AND fp.flrforecastauftrag.flrfclieferadresse.flrforecast.flrkunde.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' AND (fp.flrartikel.b_kommissionieren=1 OR fp.flrforecastauftrag.flrfclieferadresse.b_kommissionieren=1)";

		sQuery += " ORDER BY fp.flrforecastauftrag.flrfclieferadresse.flrforecast.flrkunde.flrpartner.c_name1nachnamefirmazeile1,";
		sQuery += " fp.flrforecastauftrag.flrfclieferadresse.flrkunde_lieferadresse.flrpartner.c_name1nachnamefirmazeile1,";

		sQuery += " aspr.c_zbez2, ";

		sQuery += " fp.flrartikel.c_nr DESC";

		query = session.createQuery(sQuery);

		list = query.list();
		it = list.iterator();

		while (it.hasNext()) {

			Object[] o = (Object[]) it.next();

			FLRForecastposition item = (FLRForecastposition) o[0];
			FLRArtikellistespr spr = (FLRArtikellistespr) o[1];

			java.sql.Date dForecastDatum = new java.sql.Date(item.getT_termin().getTime());

			int iLieferdauer = item.getFlrforecastauftrag().getFlrfclieferadresse().getFlrkunde_lieferadresse()
					.getI_lieferdauer();

			Timestamp dAuslieferdatumZeile = getAusliefervorschlagFac().umKundenlieferdauerVersetzen(theClientDto,
					dForecastDatum, iLieferdauer);

			if (dAusliefertermin.getTime() >= dAuslieferdatumZeile.getTime() && item.getN_menge().doubleValue() > 0) {
				// Zeile hinzufuegen
				Object[] oZeile = new Object[REPORT_LINIENABRUFE_ANZAHL_SPALTEN];

				Integer artikelIId = item.getFlrartikel().getI_id();

				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(artikelIId, theClientDto);

				oZeile[REPORT_LINIENABRUFE_ARTIKELNUMMER] = aDto.getCNr();

				if (spr != null) {
					oZeile[REPORT_LINIENABRUFE_BEREICH_NR] = spr.getC_zbez2();
				}

				oZeile[REPORT_LINIENABRUFE_ARTIKEL_KOMMISSIONIEREN] = Helper.short2Boolean(aDto.getBKommissionieren());

				oZeile[REPORT_LINIENABRUFE_LIEFERADRESSE_KOMMISSIONIEREN] = Helper
						.short2Boolean(item.getFlrforecastauftrag().getFlrfclieferadresse().getB_kommissionieren());

				if (aDto.getArtikelsprDto() != null) {
					oZeile[REPORT_LINIENABRUFE_BEZEICHNUNG] = aDto.getArtikelsprDto().getCBez();
					oZeile[REPORT_LINIENABRUFE_KURZBEZEICHNUNG] = aDto.getArtikelsprDto().getCKbez();
				}

				oZeile[REPORT_LINIENABRUFE_ARTIKEL_GEWICHT_IN_KG] = aDto.getFGewichtkg();
				oZeile[REPORT_LINIENABRUFE_VERPACKUNGSMENGE] = aDto.getFVerpackungsmenge();

				oZeile[REPORT_LINIENABRUFE_VERPACKUNGSMITTELMENGE] = aDto.getNVerpackungsmittelmenge();

				if (aDto.getVerpackungsmittelIId() != null) {
					VerpackungsmittelDto verpackungsmittelDto = getArtikelFac()
							.verpackungsmittelFindByPrimaryKey(aDto.getVerpackungsmittelIId(), theClientDto);
					oZeile[REPORT_LINIENABRUFE_VERPACKUNGSMITTEL_KENNUNG] = verpackungsmittelDto.getCNr();
					oZeile[REPORT_LINIENABRUFE_VERPACKUNGSMITTEL_BEZEICHNUNG] = verpackungsmittelDto.getBezeichnung();
					oZeile[REPORT_LINIENABRUFE_VERPACKUNGSMITTEL_GEWICHT_IN_KG] = verpackungsmittelDto
							.getNGewichtInKG();
				}

				oZeile[REPORT_LINIENABRUFE_AUSLIEFERTERMIN] = dAuslieferdatumZeile;

				oZeile[REPORT_LINIENABRUFE_BESTELLNUMMER] = item.getC_bestellnummer();

				oZeile[REPORT_LINIENABRUFE_MENGE] = item.getN_menge();

				oZeile[REPORT_LINIENABRUFE_FORECAST_AUFTRAG] = item.getFlrforecastauftrag().getFlrfclieferadresse()
						.getFlrforecast().getC_nr();

				oZeile[REPORT_LINIENABRUFE_FORECAST_KUNDE] = HelperServer
						.formatNameAusFLRPartner(item.getFlrforecastauftrag().getFlrfclieferadresse().getFlrforecast()
								.getFlrkunde().getFlrpartner());
				oZeile[REPORT_LINIENABRUFE_FORECAST_LIEFERADRESSE] = HelperServer.formatNameAusFLRPartner(item
						.getFlrforecastauftrag().getFlrfclieferadresse().getFlrkunde_lieferadresse().getFlrpartner());

				alDaten.add(oZeile);
			}

		}

		session.close();

		data = new Object[alDaten.size()][REPORT_LINIENABRUFE_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(parameter, ForecastReportFac.REPORT_MODUL, ForecastReportFac.REPORT_LINIENABRUFE,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();

	}

	private TreeMap<String, TreeMap<java.util.Date, DatenDeltaliste>> addToDeltaliste(
			FLRForecastauftrag forecastauftrag, TreeMap<String, TreeMap<java.util.Date, DatenDeltaliste>> tmArtikel,
			boolean bAlt) {
		// String sQueryOffset =
		// "SELECT fp FROM FLRForecastposition fp WHERE
		// fp.status_c_nr='"+LocaleFac.STATUS_ANGELEGT+"' AND
		// fp.flrforecastauftrag.i_id="
		// + forecastauftrag.getI_id() + " ";
		String sQueryOffset = "SELECT fp FROM FLRForecastposition fp WHERE fp.flrforecastauftrag.i_id="
				+ forecastauftrag.getI_id();
		Session session2 = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Query query2 = session2.createQuery(sQueryOffset);
		List lOffset = query2.list();

		Iterator itOffset = lOffset.iterator();
		while (itOffset.hasNext()) {

			FLRForecastposition fp = (FLRForecastposition) itOffset.next();

			java.util.Date dOffset = fp.getT_termin();

			TreeMap<java.util.Date, DatenDeltaliste> tmDatum = null;
			if (tmArtikel.containsKey(fp.getFlrartikel().getC_nr())) {

				tmDatum = tmArtikel.get(fp.getFlrartikel().getC_nr());

			} else {
				tmDatum = new TreeMap<java.util.Date, DatenDeltaliste>();
			}

			BigDecimal bdOffen = null;

			BigDecimal bdGeliefert = getForecastFac().getBereitsGelieferteMenge(fp.getI_id());

			if (bdGeliefert.doubleValue() > fp.getN_menge().doubleValue()) {
				bdOffen = BigDecimal.ZERO;
			} else {
				bdOffen = fp.getN_menge().subtract(bdGeliefert);
			}

			DatenDeltaliste daten = null;
			if (tmDatum.containsKey(dOffset)) {
				daten = tmDatum.get(dOffset);
			} else {
				daten = new DatenDeltaliste();
			}

			BigDecimal bdLinienabrufe = getForecastFac().getSummeLinienabrufe(fp.getI_id());

			if (bAlt == true) {

				BigDecimal bdAlt = daten.bdMengeAlt;
				bdAlt = bdAlt.add(bdOffen);

				daten.bdMengeAlt = bdAlt;

				daten.addToKommentarAlt(fp.getX_kommentar());

				if (bdLinienabrufe != null) {
					BigDecimal bdSummeLinienabrufe = daten.bdMengeLinienabrufeAlt;
					bdSummeLinienabrufe = bdSummeLinienabrufe.add(bdLinienabrufe);
					daten.bdMengeLinienabrufeAlt = bdSummeLinienabrufe;
				}
				daten.setStatusCnrAlt(fp.getStatus_c_nr());
				daten.forecastpositionIId = fp.getI_id();
			} else {
				BigDecimal bdNeu = daten.bdMengeNeu;
				bdNeu = bdNeu.add(bdOffen);

				daten.bdMengeNeu = bdNeu;

				daten.addToKommentarNeu(fp.getX_kommentar());

				if (bdLinienabrufe != null) {
					BigDecimal bdSummeLinienabrufe = daten.bdMengeLinienabrufeNeu;
					bdSummeLinienabrufe = bdSummeLinienabrufe.add(bdLinienabrufe);
					daten.bdMengeLinienabrufeNeu = bdSummeLinienabrufe;
				}

				daten.setStatusCnrNeu(fp.getStatus_c_nr());
				daten.setForecastpositionIIdNeu(fp.getI_id());
			}

			// Nun Mengen addieren
			tmDatum.put(dOffset, daten);

			tmArtikel.put(fp.getFlrartikel().getC_nr(), tmDatum);
		}
		session2.close();

		return tmArtikel;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printForecastUebersicht(TheClientDto theClientDto) {
		sAktuellerReport = ForecastReportFac.REPORT_UEBERSICHT;

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		ArrayList alDaten = new ArrayList();

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT fa FROM FLRForecastauftrag fa WHERE fa.flrfclieferadresse.flrforecast.flrkunde.mandant_c_nr='"
				+ theClientDto.getMandant() + "' AND fa.status_c_nr<>'" + LocaleFac.STATUS_ERLEDIGT
				+ "' AND fa.flrfclieferadresse.flrforecast.status_c_nr<>'" + LocaleFac.STATUS_ERLEDIGT
				+ "' ORDER BY fa.flrfclieferadresse.flrforecast.flrkunde.flrpartner.c_name1nachnamefirmazeile1, fa.flrfclieferadresse.flrkunde_lieferadresse.flrpartner.c_name1nachnamefirmazeile1";

		sQuery += "";

		org.hibernate.Query query = session.createQuery(sQuery);

		List l = query.list();

		Iterator it = l.iterator();

		while (it.hasNext()) {

			FLRForecastauftrag fa = (FLRForecastauftrag) it.next();

			Object[] oZeile = new Object[REPORT_UEBERSICHT_ANZAHL_SPALTEN];

			oZeile[REPORT_UEBERSICHT_FORECASTNUMMER] = fa.getFlrfclieferadresse().getFlrforecast().getC_nr();

			oZeile[REPORT_UEBERSICHT_PROJEKT] = fa.getFlrfclieferadresse().getFlrforecast().getC_projekt();
			oZeile[REPORT_UEBERSICHT_BEMERKUNG] = fa.getC_bemerkung();

			oZeile[REPORT_UEBERSICHT_KUNDE] = HelperServer
					.formatNameAusFLRPartner(fa.getFlrfclieferadresse().getFlrforecast().getFlrkunde().getFlrpartner());
			oZeile[REPORT_UEBERSICHT_LIEFERADRESSE] = HelperServer
					.formatNameAusFLRPartner(fa.getFlrfclieferadresse().getFlrkunde_lieferadresse().getFlrpartner());

			oZeile[REPORT_UEBERSICHT_ANLAGEDATUM] = fa.getT_anlegen();

			ForecastauftragDto faDto = getForecastFac().forecastauftragFindByPrimaryKey(fa.getI_id());

			oZeile[REPORT_UEBERSICHT_KOMMENTAR] = faDto.getXKommentar();
			alDaten.add(oZeile);
		}

		data = new Object[alDaten.size()][REPORT_BESCHAFFUNG_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(parameter, ForecastReportFac.REPORT_MODUL, ForecastReportFac.REPORT_UEBERSICHT,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printBeschaffung(Integer forecastauftragIId, String forecastartCNr, String statusCNr,
			int iSortierung, TheClientDto theClientDto) {

		// Erstellung des Reports

		String[] fieldnamesSubreport = new String[] { "Offset", "FCMenge", "EKMenge", "EKPreis" };

		int FIELD_SUBREPORT_OFFSET = 0;
		int FIELD_SUBREPORT_FCMENGE = 1;
		int FIELD_SUBREPORT_EKMENGE = 2;
		int FIELD_SUBREPORT_EKPREIS = 3;

		sAktuellerReport = ForecastReportFac.REPORT_BESCHAFFUNG;

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		parameter.put("P_FORECASTART", forecastartCNr);

		if (forecastauftragIId == null) {
			parameter.put("P_JOURNAL", Boolean.TRUE);
			parameter.put("P_STATUS", statusCNr);

		} else {
			parameter.put("P_JOURNAL", Boolean.FALSE);
		}

		if (iSortierung == BESCHAFFUNG_SORTIERUNG_AUFTRAG_STRUKTUR) {
			parameter.put("P_SORTIERUNG", getTextRespectUISpr("fc.beschaffung.sort.strukt", theClientDto.getMandant(),
					theClientDto.getLocUi()));
		} else {
			parameter.put("P_SORTIERUNG", getTextRespectUISpr("fc.beschaffung.sort.lieferant",
					theClientDto.getMandant(), theClientDto.getLocUi()));
		}

		ArrayList alDaten = new ArrayList();

		Session sessionForecastauftrag = FLRSessionFactory.getFactory().openSession();

		String sQueryForecastauftrag = "SELECT fa FROM FLRForecastauftrag AS fa WHERE 1=1 ";

		if (forecastauftragIId != null) {
			sQueryForecastauftrag += " AND  fa.i_id=" + forecastauftragIId + "";
		} else {
			sQueryForecastauftrag += "AND fa.status_c_nr = '" + statusCNr
					+ "' AND fa.flrfclieferadresse.flrforecast.status_c_nr='" + LocaleFac.STATUS_ANGELEGT + "'";

		}
		sQueryForecastauftrag += "  ORDER BY fa.flrfclieferadresse.flrforecast.c_nr,fa.t_anlegen ASC";

		org.hibernate.Query hqueryForecastauftrag = sessionForecastauftrag.createQuery(sQueryForecastauftrag);

		List<?> resultListForecastauftrag = hqueryForecastauftrag.list();
		Iterator<?> resultListIteratorForecastauftrag = resultListForecastauftrag.iterator();

		if (resultListForecastauftrag.size() == 0) {
			return null;
		}

		// Struktur
		LinkedHashMap<Integer, LinkedHashMap<Integer, ArrayList<FLRForecastposition>>> tmForecastauftrag = new LinkedHashMap<Integer, LinkedHashMap<Integer, ArrayList<FLRForecastposition>>>();
		java.util.Date dBeginn = null;

		java.util.Date dEnde = null;

		while (resultListIteratorForecastauftrag.hasNext()) {
			FLRForecastauftrag flrForecastauftrag = (FLRForecastauftrag) resultListIteratorForecastauftrag.next();

			Session session = FLRSessionFactory.getFactory().openSession();

			String sQuery = "SELECT fp FROM FLRForecastposition AS fp WHERE fp.flrforecastauftrag.i_id="
					+ flrForecastauftrag.getI_id() + "  ORDER BY fp.flrartikel.c_nr ASC";

			org.hibernate.Query hquery = session.createQuery(sQuery);

			List<?> resultList = hquery.list();
			Iterator<?> resultListIterator = resultList.iterator();

			while (resultListIterator.hasNext()) {
				FLRForecastposition fp = (FLRForecastposition) resultListIterator.next();

				// if(Helper.isOneOf(fp.getStatus_c_nr(),
				// LocaleFac.STATUS_ANGELEGT, LocaleFac.STATUS_IN_PRODUKTION)){
				if (fp.getStatus_c_nr().equals(LocaleFac.STATUS_ANGELEGT)) {

					if (getForecastFac().sindBereitsLieferscheinpositionenVorhanden(fp.getI_id())) {
						continue;

					}

					String forecastartCNrPositon = HelperServer.getForecastartEienrForecastposition(fp);

					if (forecastartCNrPositon != null && forecastartCNrPositon.equals(forecastartCNr)) {

						LinkedHashMap<Integer, ArrayList<FLRForecastposition>> tmArtikel = null;

						if (tmForecastauftrag.containsKey(flrForecastauftrag.getI_id())) {

							tmArtikel = tmForecastauftrag.get(flrForecastauftrag.getI_id());

						} else {
							tmArtikel = new LinkedHashMap<Integer, ArrayList<FLRForecastposition>>();
						}

						ArrayList<FLRForecastposition> alPositionen = null;

						if (tmArtikel.containsKey(fp.getFlrartikel().getI_id())) {
							alPositionen = tmArtikel.get(fp.getFlrartikel().getI_id());
						} else {
							alPositionen = new ArrayList<FLRForecastposition>();
						}

						if (dBeginn == null) {
							dBeginn = fp.getT_termin();
						}
						if (dEnde == null) {
							dEnde = fp.getT_termin();
						}

						if (fp.getT_termin().after(dEnde)) {
							dEnde = fp.getT_termin();
						}
						if (fp.getT_termin().before(dBeginn)) {
							dBeginn = fp.getT_termin();
						}

						alPositionen.add(fp);
						tmArtikel.put(fp.getFlrartikel().getI_id(), alPositionen);

						tmForecastauftrag.put(flrForecastauftrag.getI_id(), tmArtikel);

					}
				}
			}

		}

		if (forecastauftragIId != null) {
			ForecastauftragDto faDtoSekletiert = getForecastFac().forecastauftragFindByPrimaryKey(forecastauftragIId);

			parameter.put("P_FORECASTAUFTRAG", faDtoSekletiert.getTAnlegen());

		}

		if (dBeginn != null || dEnde != null) {

			Timestamp tHeute = Helper.cutTimestamp(new Timestamp(System.currentTimeMillis()));

			if (dBeginn.before(tHeute)) {
				dBeginn = tHeute;
			}

			Calendar cBeginn = Calendar.getInstance();

			cBeginn.setTime(dBeginn);

			Calendar cEnde = Calendar.getInstance();

			cEnde.setTime(dEnde);

			ArrayList alVorlage = new ArrayList();
			while (cBeginn.getTime().before(cEnde.getTime()) || cBeginn.getTime().equals(cEnde.getTime())) {

				alVorlage.add(ForecastHelper.getBeschriftungInAbhaengigkeitDerForecastart(forecastartCNr,
						new java.sql.Timestamp(cBeginn.getTimeInMillis()), theClientDto.getLocUi()));
				if (forecastartCNr.equals(ForecastFac.FORECASTART_FORECASTAUFTRAG)) {

					cBeginn.add(Calendar.MONTH, 1);
				} else if (forecastartCNr.equals(ForecastFac.FORECASTART_CALL_OFF_WOCHE)) {
					cBeginn.add(Calendar.WEEK_OF_YEAR, 1);
				} else if (forecastartCNr.equals(ForecastFac.FORECASTART_CALL_OFF_TAG)) {
					cBeginn.add(Calendar.DATE, 1);
				}

			}

			Iterator itFcAuftrag = tmForecastauftrag.keySet().iterator();

			while (itFcAuftrag.hasNext()) {

				Integer forecastauftragIIdLocal = (Integer) itFcAuftrag.next();

				ForecastauftragDto faDto = getForecastFac().forecastauftragFindByPrimaryKey(forecastauftragIIdLocal);
				FclieferadresseDto fclieferadresseDto = getForecastFac()
						.fclieferadresseFindByPrimaryKey(faDto.getFclieferadresseIId());
				ForecastDto fDto = getForecastFac().forecastFindByPrimaryKey(fclieferadresseDto.getForecastIId());

				java.sql.Date dStart = new java.sql.Date(cBeginn.getTimeInMillis());
				dStart = Helper.cutDate(dStart);

				LinkedHashMap<Integer, ArrayList<FLRForecastposition>> tmArtikel = tmForecastauftrag
						.get(forecastauftragIIdLocal);

				Iterator itArtikel = tmArtikel.keySet().iterator();

				while (itArtikel.hasNext()) {
					Integer artikelIId = (Integer) itArtikel.next();

					try {

						StuecklisteDto stklDtoKopf = null;

						StuecklisteDto[] stklDtos = getStuecklisteFac().stuecklisteFindByArtikelIId(artikelIId);
						if (stklDtos != null && stklDtos.length > 0) {
							stklDtoKopf = stklDtos[0];
						}

						if (stklDtoKopf != null || stklDtoKopf == null) {

							Object[] oZeile = holeArtikeldaten(artikelIId, BigDecimal.ONE, dStart, theClientDto);

							oZeile = addForecastDaten(fDto, fclieferadresseDto, faDto, oZeile, theClientDto);

							if (stklDtoKopf != null) {
								oZeile[REPORT_BESCHAFFUNG_B_STUECKLISTE] = Boolean.TRUE;
								oZeile[REPORT_BESCHAFFUNG_MANDANT] = stklDtoKopf.getMandantCNr();
								oZeile[REPORT_BESCHAFFUNG_STKL_EBENE] = 0;

								if (Helper.short2boolean(stklDtoKopf.getBFremdfertigung()) == false) {
									// Wenn eigengefertigt, dann ist der
									// Mandant
									// der Lieferant, Preis gibt es derzeit
									// keinen

									PartnerDto pDto = getMandantFac()
											.mandantFindByPrimaryKey(stklDtoKopf.getMandantCNr(), theClientDto)
											.getPartnerDto();

									oZeile[REPORT_BESCHAFFUNG_LIEFERANT] = pDto.formatFixName1Name2();
									oZeile[REPORT_BESCHAFFUNG_VERBUNDENES_UNTERNEHMEN] = Boolean.TRUE;
								}

							}

							Object[][] oSubData = new Object[alVorlage.size()][6];

							// Leere Spalten Beschriften
							for (int i = 0; i < alVorlage.size(); i++) {
								oSubData[i][FIELD_SUBREPORT_OFFSET] = alVorlage.get(i);
							}

							ArrayList<FLRForecastposition> alPositionen = tmArtikel.get(artikelIId);

							BigDecimal bdSummeMenge = BigDecimal.ZERO;
							for (int k = 0; k < alPositionen.size(); k++) {

								FLRForecastposition fp = alPositionen.get(k);

								String beschriftung = ForecastHelper.getBeschriftungInAbhaengigkeitDerForecastart(
										forecastartCNr, new java.sql.Timestamp(fp.getT_termin().getTime()),

										theClientDto.getLocUi());

								int iZeile = alVorlage.indexOf(beschriftung);

								if (iZeile >= 0) {

									oSubData[iZeile][FIELD_SUBREPORT_OFFSET] = beschriftung;

									oSubData[iZeile][FIELD_SUBREPORT_FCMENGE] = fp.getN_menge();
									oSubData[iZeile][FIELD_SUBREPORT_EKMENGE] = fp.getN_menge();

									ArtikellieferantDto alDtoSubreport = getArtikelFac().getArtikelEinkaufspreis(
											fp.getFlrartikel().getI_id(), null, fp.getN_menge(),
											theClientDto.getSMandantenwaehrung(),
											new java.sql.Date(fp.getT_termin().getTime()), theClientDto);

									if (alDtoSubreport != null) {
										oSubData[iZeile][FIELD_SUBREPORT_EKPREIS] = alDtoSubreport.getLief1Preis();

									}
								}
								bdSummeMenge = bdSummeMenge.add(fp.getN_menge());

							}
							oZeile[REPORT_BESCHAFFUNG_SUBREPORT_OFFSET] = oSubData;

							oZeile[REPORT_BESCHAFFUNG_SUMME_EK_MENGE] = bdSummeMenge;

							alDaten.add(oZeile);
						}
						if (stklDtoKopf != null) {
							// Tiefe der Stuecklistenaufloeseung bestimmen
							ParametermandantDto parametermandantDto = getParameterFac().getMandantparameter(
									theClientDto.getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
									ParameterFac.PARAMETER_AUFTRAG_ABVORKALK_STKL_AUFLOESUNG_TIEFE);
							int iStuecklisteaufloesungTiefe = ((Integer) parametermandantDto.getCWertAsObject())
									.intValue();

							BigDecimal bdGesamtForecastMenge = BigDecimal.ZERO;

							ArrayList<FLRForecastposition> alPositionen = tmArtikel.get(artikelIId);

							for (int k = 0; k < alPositionen.size(); k++) {
								FLRForecastposition fp = (FLRForecastposition) alPositionen.get(k);
								bdGesamtForecastMenge = bdGesamtForecastMenge.add(fp.getN_menge());
							}

							StuecklisteAufgeloest stuecklisteInfoDto = stuecklisteFacLocalBean
									.getStrukturdatenEinesArtikelsStrukturiert(artikelIId, false,
											iStuecklisteaufloesungTiefe, // in
																			// die
																			// Rekursion
											// mit einer
											// leeren Listen einsteigen

											true, // Basis sind die Einheiten
													// der
											// Stueckliste
											bdGesamtForecastMenge, // Basis sind
																	// n
																	// Einheiten
																	// der
											// Stueckliste
											false, true, // Fremdfertigung
											2, // nicht
												// aufloesen
											theClientDto);

							if (stuecklisteInfoDto.getAnzahlPositionen() > 0 && (!Helper
									.short2boolean(stuecklisteInfoDto.getStuecklisteDto().getBFremdfertigung())
									|| iStuecklisteaufloesungTiefe > 0)) {

								ArrayList<StuecklisteAufgeloest> alStuecklisteAufgeloest = stuecklisteInfoDto
										.getAlStuecklisteAufgeloest();
								Iterator<?> it = alStuecklisteAufgeloest.iterator();
								while (it.hasNext()) {
									final StuecklisteAufgeloest stkDto = (StuecklisteAufgeloest) it.next();
									StuecklistepositionDto stuecklistepositionDto = stkDto.getStuecklistepositionDto();

									Object[] oZeile = holeArtikeldaten(stuecklistepositionDto.getArtikelIId(),
											BigDecimal.ONE, dStart, theClientDto);

									oZeile = addForecastDaten(fDto, fclieferadresseDto, faDto, oZeile, theClientDto);

									if (stkDto.getStuecklisteDto() != null) {
										oZeile[REPORT_BESCHAFFUNG_B_STUECKLISTE] = Boolean.TRUE;
										oZeile[REPORT_BESCHAFFUNG_MANDANT] = stkDto.getStuecklisteDto().getMandantCNr();

										if (Helper.short2boolean(
												stkDto.getStuecklisteDto().getBFremdfertigung()) == false) {
											// Wenn eigengefertigt, dann ist der
											// Mandant
											// der Lieferant, Preis gibt es
											// derzeit
											// keinen

											PartnerDto pDto = getMandantFac()
													.mandantFindByPrimaryKey(stkDto.getStuecklisteDto().getMandantCNr(),
															theClientDto)
													.getPartnerDto();

											oZeile[REPORT_BESCHAFFUNG_LIEFERANT] = pDto.formatFixName1Name2();
											oZeile[REPORT_BESCHAFFUNG_VERBUNDENES_UNTERNEHMEN] = Boolean.TRUE;

											oZeile[REPORT_BESCHAFFUNG_ARTIKELBEZEICHNUNG_LIEFERANT] = null;
											oZeile[REPORT_BESCHAFFUNG_ARTIKELNUMMER_LIEFERANT] = null;
											oZeile[REPORT_BESCHAFFUNG_EK_PREIS] = null;

										}

									} else {
										oZeile[REPORT_BESCHAFFUNG_MANDANT] = stkDto.getVorgaenger().getStuecklisteDto()
												.getMandantCNr();
									}

									oZeile[REPORT_BESCHAFFUNG_STKL_EBENE] = stkDto.getIEbene();

									if (stkDto.getVorgaenger() != null
											&& stkDto.getVorgaenger().getStuecklisteDto() != null) {

										oZeile[REPORT_BESCHAFFUNG_DLZ] = stkDto.getVorgaenger().getStuecklisteDto()
												.getNDefaultdurchlaufzeit();

									}

									Object[][] oSubData = new Object[alVorlage.size()][6];
									BigDecimal bdSummeMenge = BigDecimal.ZERO;

									// Leere Spalten Beschriften
									for (int i = 0; i < alVorlage.size(); i++) {
										oSubData[i][FIELD_SUBREPORT_OFFSET] = alVorlage.get(i);
									}

									for (int k = 0; k < alPositionen.size(); k++) {
										FLRForecastposition fp = alPositionen.get(k);

										String beschriftung = ForecastHelper
												.getBeschriftungInAbhaengigkeitDerForecastart(forecastartCNr,
														new java.sql.Timestamp(fp.getT_termin().getTime()),

														theClientDto.getLocUi());

										int iZeile = alVorlage.indexOf(beschriftung);

										Calendar c = Calendar.getInstance();
										c.setTime(fp.getFlrforecastauftrag().getT_anlegen());

										/*
										 * if (forecastartCNr.equals(ForecastFac. FORECASTART_FORECASTAUFTRAG)) {
										 * c.add(Calendar.MONTH, fm.getI_offset()); }
										 */
										if (iZeile >= 0) {
											oSubData[iZeile][FIELD_SUBREPORT_OFFSET] = beschriftung;

											oSubData[iZeile][FIELD_SUBREPORT_FCMENGE] = fp.getN_menge();

											// EKMenge

											if (fp.getN_menge().doubleValue() != 0) {
												BigDecimal faktor = bdGesamtForecastMenge.divide(fp.getN_menge(), 4,
														BigDecimal.ROUND_HALF_EVEN);

												BigDecimal posMenge = stuecklistepositionDto.getNMenge().divide(faktor,
														4, BigDecimal.ROUND_HALF_EVEN);

												oSubData[iZeile][FIELD_SUBREPORT_EKMENGE] = posMenge;

												bdSummeMenge = bdSummeMenge.add(posMenge);

											}

											ArtikellieferantDto alDtoSubreport = getArtikelFac()
													.getArtikelEinkaufspreis(fp.getFlrartikel().getI_id(), null,
															fp.getN_menge(), theClientDto.getSMandantenwaehrung(),
															new java.sql.Date(c.getTimeInMillis()), theClientDto);

											if (alDtoSubreport != null) {
												oSubData[iZeile][FIELD_SUBREPORT_EKPREIS] = alDtoSubreport
														.getLief1Preis();

											}
										}

									}
									oZeile[REPORT_BESCHAFFUNG_SUBREPORT_OFFSET] = oSubData;

									oZeile[REPORT_BESCHAFFUNG_SUMME_EK_MENGE] = bdSummeMenge;

									alDaten.add(oZeile);

								}

							}

						}

						/*
						 * oZeile[REPORT_BESCHAFFUNG_DLZ] = fp.getFlrartikel().getC_nr();
						 * oZeile[REPORT_BESCHAFFUNG_LIEFERANT] = fp.getFlrartikel() .getC_nr();
						 */

					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

				}

			}
			sessionForecastauftrag.close();

			if (iSortierung == BESCHAFFUNG_SORTIERUNG_LIEFERANT) {

				// Zuerst nach Artikel verdichten
				TreeMap tmArtikel = new TreeMap();

				for (int i = 0; i < alDaten.size(); i++) {

					Object[] oZeile = (Object[]) alDaten.get(i);

					oZeile[REPORT_BESCHAFFUNG_FORECASTAUFTRAG_ID] = null;
					oZeile[REPORT_BESCHAFFUNG_FORECASTAUFTRAG_BEMERKUNG] = null;
					oZeile[REPORT_BESCHAFFUNG_FORECASTAUFTRAG_KOMMENTAR] = null;
					oZeile[REPORT_BESCHAFFUNG_FORECASTAUFTRAG_STATUS] = null;

					oZeile[REPORT_BESCHAFFUNG_FORECAST_KUNDE] = null;
					oZeile[REPORT_BESCHAFFUNG_FORECAST_LIEFERADRESSE] = null;
					oZeile[REPORT_BESCHAFFUNG_FORECAST_NUMMER] = null;
					oZeile[REPORT_BESCHAFFUNG_FORECAST_PROJEKT] = null;
					oZeile[REPORT_BESCHAFFUNG_FORECAST_STATUS] = null;

					if (tmArtikel.containsKey(oZeile[REPORT_BESCHAFFUNG_ARTIKEL])) {

						Object[] oZeileVorhanden = (Object[]) tmArtikel.get(oZeile[REPORT_BESCHAFFUNG_ARTIKEL]);

						// Nun Summieren und EK-Preis neu holen

						BigDecimal summeEKMenge = (BigDecimal) oZeileVorhanden[REPORT_BESCHAFFUNG_SUMME_EK_MENGE];
						summeEKMenge = summeEKMenge.add((BigDecimal) oZeile[REPORT_BESCHAFFUNG_SUMME_EK_MENGE]);
						oZeileVorhanden[REPORT_BESCHAFFUNG_SUMME_EK_MENGE] = summeEKMenge;

						// Subreport verdichten
						Object[][] subreportDatenVorhanden = (Object[][]) oZeileVorhanden[REPORT_BESCHAFFUNG_SUBREPORT_OFFSET];

						Object[][] subreportDatenAktuell = (Object[][]) oZeile[REPORT_BESCHAFFUNG_SUBREPORT_OFFSET];

						for (int x = 0; x < subreportDatenVorhanden.length; x++) {

							BigDecimal ekMengeVorhanden = (BigDecimal) subreportDatenVorhanden[x][FIELD_SUBREPORT_EKMENGE];

							BigDecimal ekMengeAktuell = (BigDecimal) subreportDatenAktuell[x][FIELD_SUBREPORT_EKMENGE];

							if (ekMengeVorhanden == null) {
								ekMengeVorhanden = BigDecimal.ZERO;
							}

							if (ekMengeAktuell == null) {
								ekMengeAktuell = BigDecimal.ZERO;
							}

							ekMengeVorhanden = ekMengeVorhanden.add(ekMengeAktuell);
							subreportDatenVorhanden[x][FIELD_SUBREPORT_EKMENGE] = ekMengeVorhanden;
						}

						oZeileVorhanden[REPORT_BESCHAFFUNG_SUBREPORT_OFFSET] = subreportDatenVorhanden;
						tmArtikel.put(oZeile[REPORT_BESCHAFFUNG_ARTIKEL], oZeileVorhanden);

					} else {
						tmArtikel.put(oZeile[REPORT_BESCHAFFUNG_ARTIKEL], oZeile);
					}

				}

				alDaten = new ArrayList();
				Iterator it = tmArtikel.keySet().iterator();
				while (it.hasNext()) {

					alDaten.add(tmArtikel.get(it.next()));

				}

				for (int i = alDaten.size() - 1; i > 0; --i) {
					for (int j = 0; j < i; ++j) {
						Object[] o = (Object[]) alDaten.get(j);
						Object[] o1 = (Object[]) alDaten.get(j + 1);

						String sort = (String) o[REPORT_BESCHAFFUNG_LIEFERANT];

						if (sort == null) {
							sort = "";
						}

						String sort1 = (String) o1[REPORT_BESCHAFFUNG_LIEFERANT];

						if (sort1 == null) {
							sort1 = "";
						}

						if (sort.compareTo(sort1) > 0) {

							alDaten.set(j, o1);
							alDaten.set(j + 1, o);
						}
					}
				}

			}

			for (int i = 0; i < alDaten.size(); i++) {
				Object[] oZeile = (Object[]) alDaten.get(i);

				// 19635
				if (i == 0) {
					parameter.put("P_SUBREPORT_OFFSET", new LPDatenSubreport(
							(Object[][]) oZeile[REPORT_BESCHAFFUNG_SUBREPORT_OFFSET], fieldnamesSubreport));
				}

				oZeile[REPORT_BESCHAFFUNG_SUBREPORT_OFFSET] = new LPDatenSubreport(
						(Object[][]) oZeile[REPORT_BESCHAFFUNG_SUBREPORT_OFFSET], fieldnamesSubreport);

			}
		}
		data = new Object[alDaten.size()][REPORT_BESCHAFFUNG_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(parameter, ForecastReportFac.REPORT_MODUL, ForecastReportFac.REPORT_BESCHAFFUNG,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}
}
