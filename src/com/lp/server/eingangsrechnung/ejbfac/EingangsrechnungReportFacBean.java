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
package com.lp.server.eingangsrechnung.ejbfac;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung;
import com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnungKontierung;
import com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnungReport;
import com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnungzahlung;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungKontierungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungOffeneKriterienDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungReportFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungtextDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungzahlungDto;
import com.lp.server.eingangsrechnung.service.ErZahlungsempfaenger;
import com.lp.server.eingangsrechnung.service.ReportEingangsrechnungKontierungsjournalDto;
import com.lp.server.finanz.ejbfac.BuchungDetailQueryBuilder;
import com.lp.server.finanz.ejbfac.FinanzSubreportGenerator;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzBuchungDetail;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.KontoDtoSmall;
import com.lp.server.finanz.service.ReversechargeartDto;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferschein;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.partner.fastlanereader.generated.FLRLieferant;
import com.lp.server.partner.service.BankDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.PartnerbankDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeBase;
import com.lp.server.system.jcr.service.docnode.DocNodeEingangsrechnung;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocNodeVersion;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.WechselkursDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.util.BelegPositionDruckIdentDto;
import com.lp.server.util.EingangsrechnungId;
import com.lp.server.util.HelperServer;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.server.util.report.MwstsatzReportDto;
import com.lp.server.util.report.TimingInterceptor;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

@Stateless
@Interceptors(TimingInterceptor.class)
public class EingangsrechnungReportFacBean extends LPReport implements EingangsrechnungReportFac, JRDataSource {
	public final static int UC_OFFENE = 0;
	public final static int UC_ALLE = 1;
	public final static int UC_ZAHLUNGEN = 2;
	public final static int UC_KONTIERUNG = 3;
	public final static int UC_EINGANGSRECHNUNG = 4;
	public final static int UC_FEHLENDE_ZOLLPAPIERE = 5;
	public final static int UC_ERFASSTE_ZOLLPAPIERE = 6;
	public final static int UC_ER_MIT_POSITIONEN = 7;
	public final static int UC_NICHT_ABGERECHNETE_AZ = 8;
	public final static int UC_DOKUMENTE = 9;

	private int useCase;
	private Object[][] data = null;
	private static int ZAHLUNGEN_FELD_ER_C_NR = 0;
	private static int ZAHLUNGEN_FELD_FIRMA = 1;
	private static int ZAHLUNGEN_FELD_ZAHLDATUM = 2;
	private static int ZAHLUNGEN_FELD_BETRAG = 3;
	private static int ZAHLUNGEN_FELD_BETRAG_UST = 4;
	private static int ZAHLUNGEN_FELD_BANK = 5;
	private static int ZAHLUNGEN_FELD_AUSZUG = 6;
	private static int ZAHLUNGEN_FELD_LAENDERART = 7;
	private static int ZAHLUNGEN_FELD_BETRAG_MANDANTENWAEHRUNG_ZU_BELEGZEIT = 8;
	private static int ZAHLUNGEN_FELD_BETRAG_UST_MANDANTENWAEHRUNG_ZU_BELEGZEIT = 9;
	private static int ZAHLUNGEN_FELD_BELEGWAEHRUNG = 10;
	private static int ZAHLUNGEN_FELD_BETRAG_FW = 11;
	private static int ZAHLUNGEN_FELD_BETRAG_UST_FW = 12;
	private static int ZAHLUNGEN_FELD_KONTO = 13;
	private static int ZAHLUNGEN_FELD_ZAHLUNGSART = 14;
	private static int ZAHLUNGEN_FELD_KASSENBUCH = 15;
	private static int ZAHLUNGEN_FELD_KREDITORENKONTO = 16;
	private static int ZAHLUNGEN_FELD_BLZ = 17;
	private static int ZAHLUNGEN_FELD_KURS = 18;
	private static int ZAHLUNGEN_FELD_BELEGDATUM = 19;
	private static int ZAHLUNGEN_ANZAHL_FELDER = 20;

	private static int FEHLENDE_ZOLLPAPIERE_ER_C_NR = 0;
	private static int FEHLENDE_ZOLLPAPIERE_LIEFERANT = 1;
	private static int FEHLENDE_ZOLLPAPIERE_BRUTTOBETRAG = 2;
	private static int FEHLENDE_ZOLLPAPIERE_ER_DATUM = 3;
	private static int FEHLENDE_ZOLLPAPIERE_LIEFERSCHEIN_C_NR = 4;
	private static int FEHLENDE_ZOLLPAPIERE_ANZAHL_FELDER = 5;

	private static int ERFASSTE_ZOLLPAPIERE_ER_NUMMER = 0;
	private static int ERFASSTE_ZOLLPAPIERE_LIEFERANT = 1;
	private static int ERFASSTE_ZOLLPAPIERE_BRUTTOBETRAG = 2;
	private static int ERFASSTE_ZOLLPAPIERE_ER_DATUM = 3;
	private static int ERFASSTE_ZOLLPAPIERE_ZOLLPAPIERNUMMER = 4;
	private static int ERFASSTE_ZOLLPAPIERE_EINGANGSRECHNUNG_ZOLLPAPIER = 5;
	private static int ERFASSTE_ZOLLPAPIERE_ERFASSUNGSZEITPUNKT = 6;
	private static int ERFASSTE_ZOLLPAPIERE_PERSON_ERFASST = 7;
	private static int ERFASSTE_ZOLLPAPIERE_WAEHRUNG = 8;
	private static int ERFASSTE_ZOLLPAPIERE_LS_NUMMER = 9;
	private static int ERFASSTE_ZOLLPAPIERE_ANZAHL_FELDER = 10;

	private static int OFFENE_FELD_ER_C_NR = 0;
	private static int OFFENE_FELD_FIRMA = 1;
	private static int OFFENE_FELD_ERDATUM = 2;
	private static int OFFENE_FELD_WERT = 3;
	private static int OFFENE_FELD_ZAHLDATUM = 4;
	private static int OFFENE_FELD_BETRAG = 5;
	private static int OFFENE_FELD_OFFEN = 6;
	private static int OFFENE_FELD_LIEFERANTENRECHNUNGSNUMMER = 7;
	private static int OFFENE_FELD_TEXT = 8;
	private static int OFFENE_FELD_FREIGABEDATUM = 9;
	private static int OFFENE_FELD_BETRAG_KURS_BELEGDATUM = 10;
	private static int OFFENE_FELD_OFFEN_KURS_BELEGDATUM = 11;
	private static int OFFENE_FELD_ZAHLUNGSZIEL = 12;
	private static int OFFENE_FELD_SKONTOTAGE1 = 13;
	private static int OFFENE_FELD_SKONTOTAGE2 = 14;
	private static int OFFENE_FELD_SKONTOPROZENT1 = 15;
	private static int OFFENE_FELD_SKONTOPROZENT2 = 16;
	private static int OFFENE_FELD_NETTOTAGE = 17;
	private static int OFFENE_FELD_FAELLIGKEIT = 18;
	private static int OFFENE_FELD_FAELLIGKEIT_SKONTO1 = 19;
	private static int OFFENE_FELD_FAELLIGKEIT_SKONTO2 = 20;
	private static int OFFENE_FELD_OFFEN_FW = 21;
	private static int OFFENE_FELD_BETRAG_FW = 22;
	private static int OFFENE_FELD_ERWAEHRUNG = 23;
	private static int OFFENE_FELD_WERT_FW = 24;
	private static int OFFENE_FELD_ERKURS = 25;
	private static int OFFENE_FELD_KURS_STICHTAG = 26;
	private static int OFFENE_FELD_MAHNSTUFE = 27;
	private static int OFFENE_FELD_MAHNDATUM = 28;
	private static int OFFENE_FELD_KREDITORENNR = 29;
	private static int OFFENE_SUBREPORT_OFFENE_BUCHUNGEN = 30;
	private static int OFFENE_FELD_ART = 31;
	private static int OFFENE_FELD_MIT_POSITIONEN = 32;
	private static int OFFENE_FELD_WE_ARTIKEL = 33;

	private static int OFFENE_FELD_BANKVERBINDUNG_BANKNAME = 34;
	private static int OFFENE_FELD_BANKVERBINDUNG_BIC = 35;
	private static int OFFENE_FELD_BANKVERBINDUNG_IBAN = 36;
	private static int OFFENE_FELD_BANKVERBINDUNG_PERSON_ABWEICHEND = 37;

	private static int OFFENE_FELD_GEPRUEFT_VON = 38;
	private static int OFFENE_FELD_GEPRUEFT_AM = 39;

	private static final int OFFENE_ANZAHL_FELDER = 41;

	private static int KONTIERUNG_FELD_KOSTENSTELLE_C_NR = 0;
	private static int KONTIERUNG_FELD_KOSTENSTELLE_C_BEZ = 1;
	private static int KONTIERUNG_FELD_KONTO_C_NR = 2;
	private static int KONTIERUNG_FELD_KONTO_C_BEZ = 3;
	private static int KONTIERUNG_FELD_ER_C_NR = 4;
	private static int KONTIERUNG_FELD_ER_DATUM = 5;
	private static int KONTIERUNG_FELD_ER_LIEFERANT = 6;
	private static int KONTIERUNG_FELD_ER_TEXT = 7;
	private static int KONTIERUNG_FELD_WERT = 8;
	private static int KONTIERUNG_FELD_WERT_UST = 9;
	private static int KONTIERUNG_FELD_WERT_BEZAHLT = 10;
	private static int KONTIERUNG_FELD_KONTOART_C_NR = 11;
	private static int KONTIERUNG_FELD_LETZTES_ZAHLDATUM = 12;
	private static int KONTIERUNG_FELD_ERRECHNETER_STEUERSATZ = 13;
	private static int KONTIERUNG_FELD_LIEFERANTENRECHNUNGSNUMMER = 14;
	private static int KONTIERUNG_FELD_ER_FREIGABEDATUM = 15;
	private static int KONTIERUNG_FELD_WERT_BEZAHLT_ERKURS = 16;
	private static int KONTIERUNG_FELD_KREDITORENNUMMER = 17;
	private static int KONTIERUNG_FELD_WAEHRUNG_C_NR = 18;
	private static int KONTIERUNG_FELD_WERT_FW = 19;
	private static int KONTIERUNG_FELD_WERT_UST_FW = 20;
	private static int KONTIERUNG_FELD_WERT_BEZAHLT_FW = 21;
	private static int KONTIERUNG_FELD_ER_KURS = 22;
	private static int KONTIERUNG_FELD_ER_WEARTIKEL = 23;
	private static int KONTIERUNG_FELD_LAENDERART_LIEFERANT = 24;
	private static int KONTIERUNG_FELD_UVA_ART = 25;
	private static int KONTIERUNG_FELD_STEUERKATEGORIE = 26;
	private static int KONTIERUNG_FELD_ART = 27;
	private static int KONTIERUNG_FELD_MIT_POSITIONEN = 28;

	private static int KONTIERUNG_FELD_FINANZAMT_KONTO = 29;
	private static int KONTIERUNG_FELD_FINANZAMT_LIEFERANT = 30;
	private static int KONTIERUNG_FELD_FINANZAEMTER_UNTERSCHIEDLICH = 31;
	private static int KONTIERUNG_FELD_GEPRUEFT_VON = 32;
	private static int KONTIERUNG_FELD_GEPRUEFT_AM = 33;
	
	private static int KONTIERUNG_ANZAHL_FELDER = 34;

	private final static int FELD_ALLE_EINGANGSRECHNUNGSNUMMER = 0;
	private final static int FELD_ALLE_LIEFERANT = 1;
	private final static int FELD_ALLE_BELEGDATUM = 2;
	private final static int FELD_ALLE_FREIGABEDATUM = 3;
	private final static int FELD_ALLE_ZIELDATUM = 4;
	private final static int FELD_ALLE_BEZAHLTDATUM = 5;
	private final static int FELD_ALLE_TEXT = 6;
	private final static int FELD_ALLE_KOSTENSTELLENUMMER = 7;
	private final static int FELD_ALLE_WERTUST = 8;
	private final static int FELD_ALLE_WERT = 9;
	private final static int FELD_ALLE_WERTNETTO = 10;
	private final static int FELD_ALLE_BANK = 11;
	private final static int FELD_ALLE_AUSZUG = 12;
	private final static int FELD_ALLE_KREDITORENKONTO = 13;
	private final static int FELD_ALLE_KONTO = 14;
	private final static int FELD_ALLE_LAENDERART = 15;
	private final static int FELD_ALLE_KONTIERUNG = 16;
	private final static int FELD_ALLE_STATUS = 17;
	private final static int FELD_ALLE_ZAHLBETRAG = 18;
	private final static int FELD_ALLE_WERTUST_FW = 19;
	private final static int FELD_ALLE_WERT_FW = 20;
	private final static int FELD_ALLE_WERTNETTO_FW = 21;
	private final static int FELD_ALLE_ZAHLBETRAG_FW = 22;
	private final static int FELD_ALLE_KURS = 23;
	private final static int FELD_ALLE_WAEHRUNG = 24;
	private final static int FELD_ALLE_ZOLLBELEGNUMMER = 25;
	private final static int FELD_ALLE_UST = 26;
	private final static int FELD_ALLE_WEARTIKEL = 27;
	private final static int FELD_ALLE_ART = 28;
	private final static int FELD_ALLE_REVERSE_CHARGE = 29;
	private final static int FELD_ALLE_MIT_POSITIONEN = 30;
	private final static int FELD_ALLE_REVERSECHARGEART_I_ID = 31;
	private final static int FELD_ALLE_REVERSECHARGEART_C_NR = 32;
	private final static int FELD_ALLE_REVERSECHARGEART_DTO = 33;
	private final static int FELD_ALLE_LIEFERANTENRECHNUNGSNUMMER = 34;
	private final static int FELD_ALLE_GEPRUEFT_VON = 35;
	private final static int FELD_ALLE_GEPRUEFT_AM = 36;
	private final static int ALLE_ANZAHL_FELDER = 37;

	private final static int ER_MIT_POSITIONEN_IDENT = 0;
	private final static int ER_MIT_POSITIONEN_KONTO = 1;
	private final static int ER_MIT_POSITIONEN_BETRAG = 2;
	private final static int ER_MIT_POSITIONEN_BETRAG_UST = 3;
	private final static int ER_MIT_POSITIONEN_MWSTSATZ = 4;
	private final static int ER_MIT_POSITIONEN_KOSTENSTELLE = 5;
	private final static int ER_MIT_POSITIONEN_POSITIONSART = 6;
	private final static int ER_MIT_POSITIONEN_POSITIONSTEXT = 7;
	private final static int ER_MIT_POSITIONEN_ANZAHL_FELDER = 8;

	private final static int NICHT_ABGERECHNETE_AZ_BELEGNUMMER = 0;
	private final static int NICHT_ABGERECHNETE_AZ_BELEGDATUM = 1;
	private final static int NICHT_ABGERECHNETE_AZ_FREIGABEDATUM = 2;
	private final static int NICHT_ABGERECHNETE_AZ_LIEFERANT = 3;
	private final static int NICHT_ABGERECHNETE_AZ_BRUTTO = 4;
	private final static int NICHT_ABGERECHNETE_AZ_NETTO = 5;
	private final static int NICHT_ABGERECHNETE_AZ_LKZ = 6;
	private final static int NICHT_ABGERECHNETE_AZ_LAENDERART = 7;
	private final static int NICHT_ABGERECHNETE_AZ_ZAHLUNGSZIEL = 8;
	private final static int NICHT_ABGERECHNETE_AZ_KREDITORENNNUMMER = 9;
	private final static int NICHT_ABGERECHNETE_AZ_BESTELLNUMMER = 10;
	private final static int NICHT_ABGERECHNETE_AZ_WAEHRUNG = 11;
	private final static int NICHT_ABGERECHNETE_AZ_ANZAHL_FELDER = 12;

	public boolean next() throws JRException {
		index++;
		return (index < data.length);
	}

	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;
		String fieldName = field.getName();
		switch (useCase) {
		case UC_ALLE: {
			if ("F_AUSZUG".equals(fieldName)) {
				value = data[index][FELD_ALLE_AUSZUG];
			} else if ("F_BANK".equals(fieldName)) {
				value = data[index][FELD_ALLE_BANK];
			} else if ("F_BELEGDATUM".equals(fieldName)) {
				value = data[index][FELD_ALLE_BELEGDATUM];
			} else if ("F_BEZAHLTDATUM".equals(fieldName)) {
				value = data[index][FELD_ALLE_BEZAHLTDATUM];
			} else if ("F_EINGANGSRECHNUNGSNUMMER".equals(fieldName)) {
				value = data[index][FELD_ALLE_EINGANGSRECHNUNGSNUMMER];
			} else if ("F_STATUS".equals(fieldName)) {
				value = data[index][FELD_ALLE_STATUS];
			} else if ("F_FREIGABEDATUM".equals(fieldName)) {
				value = data[index][FELD_ALLE_FREIGABEDATUM];
			} else if ("F_KOSTENSTELLENUMMER".equals(fieldName)) {
				value = data[index][FELD_ALLE_KOSTENSTELLENUMMER];
			} else if ("F_LIEFERANT".equals(fieldName)) {
				value = data[index][FELD_ALLE_LIEFERANT];
			} else if ("F_TEXT".equals(fieldName)) {
				value = data[index][FELD_ALLE_TEXT];
			} else if ("F_WE_ARTIKEL".equals(fieldName)) {
				value = data[index][FELD_ALLE_WEARTIKEL];
			} else if ("F_ART".equals(fieldName)) {
				value = data[index][FELD_ALLE_ART];
			} else if ("F_BETRAG".equals(fieldName)) {
				value = data[index][FELD_ALLE_WERT];
			} else if ("F_BETRAGNETTO".equals(fieldName)) {
				value = data[index][FELD_ALLE_WERTNETTO];
			} else if ("F_BETRAGUST".equals(fieldName)) {
				value = data[index][FELD_ALLE_WERTUST];
			} else if ("F_BETRAG_FW".equals(fieldName)) {
				value = data[index][FELD_ALLE_WERT_FW];
			} else if ("F_BETRAGNETTO_FW".equals(fieldName)) {
				value = data[index][FELD_ALLE_WERTNETTO_FW];
			} else if ("F_BETRAGUST_FW".equals(fieldName)) {
				value = data[index][FELD_ALLE_WERTUST_FW];
			} else if ("F_ZIELDATUM".equals(fieldName)) {
				value = data[index][FELD_ALLE_ZIELDATUM];
			} else if ("F_KREDITORENKONTO".equals(fieldName)) {
				value = data[index][FELD_ALLE_KREDITORENKONTO];
			} else if ("F_KONTO".equals(fieldName)) {
				value = data[index][FELD_ALLE_KONTO];
			} else if ("F_LAENDERART".equals(fieldName)) {
				value = data[index][FELD_ALLE_LAENDERART];
			} else if ("F_KONTIERUNG_SUBREPORT".equals(fieldName)) {
				value = data[index][FELD_ALLE_KONTIERUNG];
			} else if ("F_ZAHLBETRAG".equals(fieldName)) {
				value = data[index][FELD_ALLE_ZAHLBETRAG];
			} else if ("F_ZAHLBETRAG_FW".equals(fieldName)) {
				value = data[index][FELD_ALLE_ZAHLBETRAG_FW];
			} else if ("F_KURS".equals(fieldName)) {
				value = data[index][FELD_ALLE_KURS];
			} else if ("F_WAEHRUNG".equals(fieldName)) {
				value = data[index][FELD_ALLE_WAEHRUNG];
			} else if ("F_ZOLLBELEGNUMMER".equals(fieldName)) {
				value = data[index][FELD_ALLE_ZOLLBELEGNUMMER];
			} else if ("F_UST".equals(fieldName)) {
				value = data[index][FELD_ALLE_UST];
			} else if ("F_REVERSE_CHARGE".equals(fieldName)) {
				value = data[index][FELD_ALLE_REVERSE_CHARGE];
			} else if ("F_MIT_POSITIONEN".equals(fieldName)) {
				value = data[index][FELD_ALLE_MIT_POSITIONEN];
			} else if ("F_REVERSE_CHARGE_ART_I_ID".equals(fieldName)) {
				value = data[index][FELD_ALLE_REVERSECHARGEART_I_ID];
			} else if ("F_REVERSE_CHARGE_ART_C_NR".equals(fieldName)) {
				value = data[index][FELD_ALLE_REVERSECHARGEART_C_NR];
			} else if ("F_REVERSE_CHARGE_ART_DTO".equals(fieldName)) {
				value = data[index][FELD_ALLE_REVERSECHARGEART_DTO];
			} else if ("F_LIEFERANTENRECHNUNGSNUMMER".equals(fieldName)) {
				value = data[index][FELD_ALLE_LIEFERANTENRECHNUNGSNUMMER];
			} else if ("F_GEPRUEFT_VON".equals(fieldName)) {
				value = data[index][FELD_ALLE_GEPRUEFT_VON];
			} else if ("F_GEPRUEFT_AM".equals(fieldName)) {
				value = data[index][FELD_ALLE_GEPRUEFT_AM];
			}
		}
			break;
		case UC_KONTIERUNG: {
			if ("KOSTENSTELLE_C_NR".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_KOSTENSTELLE_C_NR];
			} else if ("KOSTENSTELLE_C_BEZ".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_KOSTENSTELLE_C_BEZ];
			} else if ("KONTO_C_NR".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_KONTO_C_NR];
			} else if ("KONTO_C_BEZ".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_KONTO_C_BEZ];
			} else if ("ER_C_NR".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_ER_C_NR];
			} else if ("ER_DATUM".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_ER_DATUM];
			} else if ("ER_LIEFERANT".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_ER_LIEFERANT];
			} else if ("ER_TEXT".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_ER_TEXT];
			} else if ("ER_WE_ARTIKEL".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_ER_WEARTIKEL];
			} else if ("ER_WERT".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_WERT];
			} else if ("ER_WERT_UST".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_WERT_UST];
			} else if ("ER_WERT_BEZAHLT".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_WERT_BEZAHLT];
			} else if ("ER_ART".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_ART];
			} else if ("ER_WERT_FW".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_WERT_FW];
			} else if ("ER_WERT_UST_FW".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_WERT_UST_FW];
			} else if ("ER_WERT_BEZAHLT_FW".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_WERT_BEZAHLT_FW];
			} else if ("F_WAEHRUNG".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_WAEHRUNG_C_NR];
			} else if ("F_ER_KURS".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_ER_KURS];
			} else if ("F_MIT_POSITIONEN".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_MIT_POSITIONEN];
			} else if ("KONTOART_C_NR".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_KONTOART_C_NR];
			} else if ("F_LETZTESZAHLDATUM".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_LETZTES_ZAHLDATUM];
			} else if ("F_ERRECHNETERSTEUERSATZ".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_ERRECHNETER_STEUERSATZ];
			} else if ("F_LIEFERANTENRECHNUNGSNUMMER".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_LIEFERANTENRECHNUNGSNUMMER];
			} else if ("ER_FREIGABEDATUM".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_ER_FREIGABEDATUM];
			} else if ("ER_WERT_BEZAHLT_ERKURS".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_WERT_BEZAHLT_ERKURS];
			} else if ("F_KREDITORENNUMMER".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_KREDITORENNUMMER];
			} else if ("F_LAENDERART_LIEFERANT".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_LAENDERART_LIEFERANT];
			} else if ("F_UVA_ART".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_UVA_ART];
			} else if ("F_STEUERKATEGORIE".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_STEUERKATEGORIE];
			} else if ("F_FINANZAMT_KONTO".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_FINANZAMT_KONTO];
			} else if ("F_FINANZAMT_LIEFERANT".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_FINANZAMT_LIEFERANT];
			} else if ("F_FINANZAEMTER_UNTERSCHIEDLICH".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_FINANZAEMTER_UNTERSCHIEDLICH];
			} else if ("F_GEPRUEFT_VON".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_GEPRUEFT_VON];
			} else if ("F_GEPRUEFT_AM".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_GEPRUEFT_AM];
			}

		}
			break;

		case UC_FEHLENDE_ZOLLPAPIERE: {
			if ("ER_C_NR".equals(fieldName)) {
				value = data[index][FEHLENDE_ZOLLPAPIERE_ER_C_NR];
			} else if ("BRUTTOBETRAG".equals(fieldName)) {
				value = data[index][FEHLENDE_ZOLLPAPIERE_BRUTTOBETRAG];
			} else if ("LIEFERANT".equals(fieldName)) {
				value = data[index][FEHLENDE_ZOLLPAPIERE_LIEFERANT];
			} else if ("ER_DATUM".equals(fieldName)) {
				value = data[index][FEHLENDE_ZOLLPAPIERE_ER_DATUM];
			} else if ("LIEFERSCHEIN_C_NR".equals(fieldName)) {
				value = data[index][FEHLENDE_ZOLLPAPIERE_LIEFERSCHEIN_C_NR];
			}
		}
			break;
		case UC_NICHT_ABGERECHNETE_AZ: {
			if ("Belegdatum".equals(fieldName)) {
				value = data[index][NICHT_ABGERECHNETE_AZ_BELEGDATUM];
			} else if ("Belegnummer".equals(fieldName)) {
				value = data[index][NICHT_ABGERECHNETE_AZ_BELEGNUMMER];
			} else if ("Bestellnummer".equals(fieldName)) {
				value = data[index][NICHT_ABGERECHNETE_AZ_BESTELLNUMMER];
			} else if ("Brutto".equals(fieldName)) {
				value = data[index][NICHT_ABGERECHNETE_AZ_BRUTTO];
			} else if ("Freigabedatum".equals(fieldName)) {
				value = data[index][NICHT_ABGERECHNETE_AZ_FREIGABEDATUM];
			} else if ("Kreditorennummer".equals(fieldName)) {
				value = data[index][NICHT_ABGERECHNETE_AZ_KREDITORENNNUMMER];
			} else if ("Laenderart".equals(fieldName)) {
				value = data[index][NICHT_ABGERECHNETE_AZ_LAENDERART];
			} else if ("Lieferant".equals(fieldName)) {
				value = data[index][NICHT_ABGERECHNETE_AZ_LIEFERANT];
			} else if ("Lkz".equals(fieldName)) {
				value = data[index][NICHT_ABGERECHNETE_AZ_LKZ];
			} else if ("Netto".equals(fieldName)) {
				value = data[index][NICHT_ABGERECHNETE_AZ_NETTO];
			} else if ("Waehrung".equals(fieldName)) {
				value = data[index][NICHT_ABGERECHNETE_AZ_WAEHRUNG];
			} else if ("Zahlungsziel".equals(fieldName)) {
				value = data[index][NICHT_ABGERECHNETE_AZ_ZAHLUNGSZIEL];
			}
		}
			break;
		case UC_ER_MIT_POSITIONEN: {
			if ("F_BETRAG".equals(fieldName)) {
				value = data[index][ER_MIT_POSITIONEN_BETRAG];
			} else if ("F_BETRAG_UST".equals(fieldName)) {
				value = data[index][ER_MIT_POSITIONEN_BETRAG_UST];
			} else if ("F_IDENT".equals(fieldName)) {
				value = data[index][ER_MIT_POSITIONEN_IDENT];
			} else if ("F_KONTO".equals(fieldName)) {
				value = data[index][ER_MIT_POSITIONEN_KONTO];
			} else if ("F_KOSTENSTELLE".equals(fieldName)) {
				value = data[index][ER_MIT_POSITIONEN_KOSTENSTELLE];
			} else if ("F_MWSTSATZ".equals(fieldName)) {
				value = data[index][ER_MIT_POSITIONEN_MWSTSATZ];
			} else if ("F_POSITIONSART".equals(fieldName)) {
				value = data[index][ER_MIT_POSITIONEN_POSITIONSART];
			} else if ("F_POSITIONSTEXT".equals(fieldName)) {
				value = data[index][ER_MIT_POSITIONEN_POSITIONSTEXT];
			}
		}
			break;
		case UC_ERFASSTE_ZOLLPAPIERE: {
			if ("F_ERNUMMER".equals(fieldName)) {
				value = data[index][ERFASSTE_ZOLLPAPIERE_ER_NUMMER];
			} else if ("F_LIEFERANT".equals(fieldName)) {
				value = data[index][ERFASSTE_ZOLLPAPIERE_LIEFERANT];
			} else if ("F_BRUTTOBETRAG".equals(fieldName)) {
				value = data[index][ERFASSTE_ZOLLPAPIERE_BRUTTOBETRAG];
			} else if ("F_ERDATUM".equals(fieldName)) {
				value = data[index][ERFASSTE_ZOLLPAPIERE_ER_DATUM];
			} else if ("F_ZOLLPAPIERNUMMER".equals(fieldName)) {
				value = data[index][ERFASSTE_ZOLLPAPIERE_ZOLLPAPIERNUMMER];
			} else if ("F_EINGANGSRECHNUNGSNUMMER_ZOLLIMPORT".equals(fieldName)) {
				value = data[index][ERFASSTE_ZOLLPAPIERE_EINGANGSRECHNUNG_ZOLLPAPIER];
			} else if ("F_ERFASSUNGSZEITPUNKT".equals(fieldName)) {
				value = data[index][ERFASSTE_ZOLLPAPIERE_ERFASSUNGSZEITPUNKT];
			} else if ("F_PERSON_ERFASST".equals(fieldName)) {
				value = data[index][ERFASSTE_ZOLLPAPIERE_PERSON_ERFASST];
			} else if ("F_WAEHRUNG".equals(fieldName)) {
				value = data[index][ERFASSTE_ZOLLPAPIERE_WAEHRUNG];
			} else if ("F_LSNUMMER".equals(fieldName)) {
				value = data[index][ERFASSTE_ZOLLPAPIERE_LS_NUMMER];
			}
		}
			break;
		case UC_OFFENE: {
			if ("ER_C_NR".equals(fieldName)) {
				value = data[index][OFFENE_FELD_ER_C_NR];
			} else if ("FIRMA".equals(fieldName)) {
				value = data[index][OFFENE_FELD_FIRMA];
			} else if ("ER_DATUM".equals(fieldName)) {
				value = data[index][OFFENE_FELD_ERDATUM];
			} else if ("MAHNDATUM".equals(fieldName)) {
				value = data[index][OFFENE_FELD_MAHNDATUM];
			} else if ("KREDITORENNR".equals(fieldName)) {
				value = data[index][OFFENE_FELD_KREDITORENNR];
			} else if ("F_ART".equals(fieldName)) {
				value = data[index][OFFENE_FELD_ART];
			} else if ("MAHNSTUFE".equals(fieldName)) {
				value = data[index][OFFENE_FELD_MAHNSTUFE];
			} else if ("WERT".equals(fieldName)) {
				value = data[index][OFFENE_FELD_WERT];
			} else if ("ZAHLDATUM".equals(fieldName)) {
				value = data[index][OFFENE_FELD_ZAHLDATUM];
			} else if ("BETRAG".equals(fieldName)) {
				value = data[index][OFFENE_FELD_BETRAG];
			} else if ("OFFEN".equals(fieldName)) {
				value = data[index][OFFENE_FELD_OFFEN];
			} else if ("F_LIEFERANTENRECHNUNGSNUMMER".equals(fieldName)) {
				value = data[index][OFFENE_FELD_LIEFERANTENRECHNUNGSNUMMER];
			} else if ("F_TEXT".equals(fieldName)) {
				value = data[index][OFFENE_FELD_TEXT];
			} else if ("F_FREIGABEDATUM".equals(fieldName)) {
				value = data[index][OFFENE_FELD_FREIGABEDATUM];
			} else if ("F_WE_ARTIKEL".equals(fieldName)) {
				value = data[index][OFFENE_FELD_WE_ARTIKEL];
			} else if ("F_BETRAG_BELEGKURS".equals(fieldName)) {
				value = data[index][OFFENE_FELD_BETRAG_KURS_BELEGDATUM];
			} else if ("F_OFFEN_BELEGKURS".equals(fieldName)) {
				value = data[index][OFFENE_FELD_OFFEN_KURS_BELEGDATUM];
			} else if ("F_ZAHLUNGSZIEL".equals(fieldName)) {
				value = data[index][OFFENE_FELD_ZAHLUNGSZIEL];
			} else if ("F_SKONTOTAGE1".equals(fieldName)) {
				value = data[index][OFFENE_FELD_SKONTOTAGE1];
			} else if ("F_SKONTOTAGE2".equals(fieldName)) {
				value = data[index][OFFENE_FELD_SKONTOTAGE2];
			} else if ("F_SKONTOPROZENT1".equals(fieldName)) {
				value = data[index][OFFENE_FELD_SKONTOPROZENT1];
			} else if ("F_SKONTOPROZENT2".equals(fieldName)) {
				value = data[index][OFFENE_FELD_SKONTOPROZENT2];
			} else if ("F_NETTOTAGE".equals(fieldName)) {
				value = data[index][OFFENE_FELD_NETTOTAGE];
			} else if ("F_FAELLIGKEIT".equals(fieldName)) {
				value = data[index][OFFENE_FELD_FAELLIGKEIT];
			} else if ("F_FAELLIGKEIT_SKONTO1".equals(fieldName)) {
				value = data[index][OFFENE_FELD_FAELLIGKEIT_SKONTO1];
			} else if ("F_FAELLIGKEIT_SKONTO2".equals(fieldName)) {
				value = data[index][OFFENE_FELD_FAELLIGKEIT_SKONTO2];
			} else if ("F_OFFEN_FW".equals(fieldName)) {
				value = data[index][OFFENE_FELD_OFFEN_FW];
			} else if ("F_BETRAG_FW".equals(fieldName)) {
				value = data[index][OFFENE_FELD_BETRAG_FW];
			} else if ("F_ER_WAEHRUNG".equals(fieldName)) {
				value = data[index][OFFENE_FELD_ERWAEHRUNG];
			} else if ("F_ER_WERT_FW".equals(fieldName)) {
				value = data[index][OFFENE_FELD_WERT_FW];
			} else if ("F_ER_KURS".equals(fieldName)) {
				value = data[index][OFFENE_FELD_ERKURS];
			} else if ("F_KURS_STICHTAG".equals(fieldName)) {
				value = data[index][OFFENE_FELD_KURS_STICHTAG];
			} else if ("F_SUBREPORT_OFFENE_BUCHUNGEN".equals(fieldName)) {
				value = data[index][OFFENE_SUBREPORT_OFFENE_BUCHUNGEN];
			} else if ("F_MIT_POSITIONEN".equals(fieldName)) {
				value = data[index][OFFENE_FELD_MIT_POSITIONEN];
			} else if ("F_BANKVERBINDUNG_BANKNAME".equals(fieldName)) {
				value = data[index][OFFENE_FELD_BANKVERBINDUNG_BANKNAME];
			} else if ("F_BANKVERBINDUNG_BIC".equals(fieldName)) {
				value = data[index][OFFENE_FELD_BANKVERBINDUNG_BIC];
			} else if ("F_BANKVERBINDUNG_IBAN".equals(fieldName)) {
				value = data[index][OFFENE_FELD_BANKVERBINDUNG_IBAN];
			} else if ("F_BANKVERBINDUNG_PERSON_ABWEICHEND".equals(fieldName)) {
				value = data[index][OFFENE_FELD_BANKVERBINDUNG_PERSON_ABWEICHEND];
			} else if ("F_GEPRUEFT_VON".equals(fieldName)) {
				value = data[index][OFFENE_FELD_GEPRUEFT_VON];
			} else if ("F_GEPRUEFT_AM".equals(fieldName)) {
				value = data[index][OFFENE_FELD_GEPRUEFT_AM];
			}
		}
			break;
		case UC_ZAHLUNGEN: {
			if ("ER_C_NR".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_ER_C_NR];
			} else if ("FIRMA".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_FIRMA];
			} else if ("F_BELEGDATUM".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_BELEGDATUM];
			} else if ("ZAHLDATUM".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_ZAHLDATUM];
			} else if ("F_KURS".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_KURS];
			} else if ("BETRAG".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_BETRAG];
			} else if ("BETRAG_UST".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_BETRAG_UST];
			} else if ("BANK".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_BANK];
			} else if ("AUSZUG".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_AUSZUG];
			} else if ("F_LAENDERART".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_LAENDERART];
			} else if ("F_BETRAG_BELEGZEIT".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_BETRAG_MANDANTENWAEHRUNG_ZU_BELEGZEIT];
			} else if ("F_BETRAG_UST_BELEGZEIT".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_BETRAG_UST_MANDANTENWAEHRUNG_ZU_BELEGZEIT];
			} else if ("F_WAEHRUNG".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_BELEGWAEHRUNG];
			} else if ("F_BETRAG_FW".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_BETRAG_FW];
			} else if ("F_BETRAG_UST_FW".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_BETRAG_UST_FW];
			} else if ("F_KONTO".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_KONTO];
			} else if ("F_BLZ".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_BLZ];
			} else if ("F_ZAHLUNGSART".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_ZAHLUNGSART];
			} else if ("F_KASSENBUCH".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_KASSENBUCH];
			} else if ("F_KREDITORENKONTO".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_KREDITORENKONTO];
			}
		}
			break;
		case UC_EINGANGSRECHNUNG: {
			value = null;
		}
			break;
		}
		return value;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printZahlungsjournal(TheClientDto theClientDto, int iSortierung, Date dVon, Date dBis,
			boolean bZusatzkosten) {
		this.useCase = UC_ZAHLUNGEN;

		boolean fertig = true;

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();

			String s = "from FLREingangsrechnungzahlung zahlung LEFT OUTER JOIN zahlung.flrbankverbindung.flrbank.flrpartner bkv WHERE zahlung.flreingangsrechnung.mandant_c_nr='"
					+ theClientDto.getMandant() + "'";

			s += " AND zahlung.t_zahldatum>='" + Helper.formatDateWithSlashes(dVon) + "' ";

			s += " AND zahlung.t_zahldatum<'" + Helper.formatDateWithSlashes(Helper.addiereTageZuDatum(dBis, 1)) + "' ";

			if (bZusatzkosten) {
				s += " AND zahlung.flreingangsrechnung.eingangsrechnungart_c_nr='"
						+ EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN + "' ";
			} else {
				s += " AND zahlung.flreingangsrechnung.eingangsrechnungart_c_nr<>'"
						+ EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN + "' ";
			}

			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			mapParameter.put("P_VON", dVon);
			mapParameter.put("P_BIS", dBis);
			mapParameter.put("P_ZUSATZKOSTEN", new Boolean(bZusatzkosten));

			mapParameter.put(LPReport.P_WAEHRUNG, theClientDto.getSMandantenwaehrung());

			String sSortierung = null;
			if (iSortierung == EingangsrechnungReportFac.REPORT_ZAHLUNGEN_SORT_BANK_AUSZUG) {
				sSortierung = "Bank, Auszug";
				s += " ORDER BY bkv.c_name1nachnamefirmazeile1, " + "zahlung.i_auszug, zahlung.zahlungsart_c_nr";
			} else if (iSortierung == EingangsrechnungReportFac.REPORT_ZAHLUNGEN_SORT_RECHNUNGSNUMMER) {
				sSortierung = "Rechnungsnummer";
				s += " ORDER BY zahlung.flreingangsrechnung.c_nr"; // c_lieferantenrechnungsnummer";
			} else if (iSortierung == EingangsrechnungReportFac.REPORT_ZAHLUNGEN_SORT_ZAHLUNGSAUSGANG) {
				sSortierung = "Zahlungsausgang";
				s += " ORDER BY zahlung.t_zahldatum";
			}

			Query query = session.createQuery(s);

			mapParameter.put(LPReport.P_SORTIERUNG, sSortierung);

			List<?> resultList = query.list();

			Iterator resultListIterator = resultList.iterator();
			int row = 0;
			data = new Object[resultList.size()][ZAHLUNGEN_ANZAHL_FELDER];

			while (resultListIterator.hasNext()) {
				Object[] o = (Object[]) resultListIterator.next();

				FLREingangsrechnungzahlung zahlung = (FLREingangsrechnungzahlung) o[0];
				EingangsrechnungDto erDto = getEingangsrechnungFac()
						.eingangsrechnungFindByPrimaryKey(zahlung.getEingangsrechnung_i_id());
				String sErCNr = erDto.getCNr();
				LieferantDto lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(erDto.getLieferantIId(),
						theClientDto);
				PartnerDto partnerDto = lieferantDto.getPartnerDto();
				String sFirma = partnerDto.formatFixTitelName1Name2();

				String sLaenderart = getFinanzServiceFac().getLaenderartZuPartner(partnerDto.getIId(),
						Helper.asTimestamp(erDto.getDBelegdatum()), theClientDto);
				data[row][ZAHLUNGEN_FELD_KREDITORENKONTO] = lieferantDto.getIKreditorenkontoAsIntegerNotiId();
				data[row][ZAHLUNGEN_FELD_LAENDERART] = sLaenderart;
				data[row][ZAHLUNGEN_FELD_ER_C_NR] = sErCNr;
				data[row][ZAHLUNGEN_FELD_BELEGDATUM] = erDto.getDBelegdatum();
				data[row][ZAHLUNGEN_FELD_FIRMA] = sFirma;
				data[row][ZAHLUNGEN_FELD_ZAHLDATUM] = zahlung.getT_zahldatum();
				data[row][ZAHLUNGEN_FELD_KURS] = zahlung.getN_kurs();
				data[row][ZAHLUNGEN_FELD_BETRAG] = zahlung.getN_betrag();
				data[row][ZAHLUNGEN_FELD_BETRAG_UST] = zahlung.getN_betrag_ust();
				if (zahlung.getN_betragfw() != null) {
					data[row][ZAHLUNGEN_FELD_BETRAG_MANDANTENWAEHRUNG_ZU_BELEGZEIT] = zahlung.getN_betragfw()
							.multiply(erDto.getNKurs());
				}
				if (zahlung.getN_betragfw() != null) {
					data[row][ZAHLUNGEN_FELD_BETRAG_UST_MANDANTENWAEHRUNG_ZU_BELEGZEIT] = zahlung.getN_betrag_ustfw()
							.multiply(erDto.getNKurs());
				}
				if (zahlung.getFlrbankverbindung() != null) {
					data[row][ZAHLUNGEN_FELD_BANK] = zahlung.getFlrbankverbindung().getFlrbank().getFlrpartner()
							.getC_name1nachnamefirmazeile1();
					data[row][ZAHLUNGEN_FELD_BLZ] = zahlung.getFlrbankverbindung().getFlrbank().getC_blz();
				}
				data[row][ZAHLUNGEN_FELD_BELEGWAEHRUNG] = erDto.getWaehrungCNr();
				data[row][ZAHLUNGEN_FELD_BETRAG_FW] = zahlung.getN_betragfw();
				data[row][ZAHLUNGEN_FELD_BETRAG_UST_FW] = zahlung.getN_betrag_ustfw();
				if (zahlung.getFlrbankverbindung() != null) {
					data[row][ZAHLUNGEN_FELD_KONTO] = zahlung.getFlrbankverbindung().getC_kontonummer();
				}

				data[row][ZAHLUNGEN_FELD_AUSZUG] = zahlung.getI_auszug();
				data[row][ZAHLUNGEN_FELD_ZAHLUNGSART] = zahlung.getZahlungsart_c_nr();
				if (zahlung.getFlrkassenbuch() != null) {
					data[row][ZAHLUNGEN_FELD_KASSENBUCH] = zahlung.getFlrkassenbuch().getC_bez();
				} else {
					data[row][ZAHLUNGEN_FELD_KASSENBUCH] = "";
				}
				row++;
			}

			/*
			 * int maxrow = row; row = 0;
			 * 
			 * do{ for(int i = 0; i < maxrow; i++){ fertig = false; if(maxrow != (row + 1)){
			 * Character help = data[row][ZAHLUNGEN_FELD_AUSZUG].toString().charAt(1);
			 * if(data[row][ZAHLUNGEN_FELD_AUSZUG].toString().charAt(1) ==
			 * data[row][ZAHLUNGEN_FELD_AUSZUG].toString().charAt(9) &&
			 * data[row+1][ZAHLUNGEN_FELD_AUSZUG].toString().charAt(1) ==
			 * data[row+1][ZAHLUNGEN_FELD_AUSZUG].toString().charAt(9)){
			 * if(data[row][ZAHLUNGEN_FELD_AUSZUG].toString().charAt(0) > data[row +
			 * 1][ZAHLUNGEN_FELD_AUSZUG].toString().charAt(0) || data[row +
			 * 1][ZAHLUNGEN_FELD_AUSZUG].toString().charAt(0) == '0'){ Object helpobj = new
			 * Object[resultList.size()]; helpobj = data[row]; data[row] = data[row+1];
			 * data[row+1] = (Object[]) helpobj; fertig = true; } }else{
			 * 
			 * } } row++; } row = 0; }while(fertig == true);
			 */

			initJRDS(mapParameter, EingangsrechnungReportFac.REPORT_MODUL,
					EingangsrechnungReportFac.REPORT_EINGANGSRECHNUNG_ZAHLUNGSJOURNAL, theClientDto.getMandant(),
					theClientDto.getLocUi(), theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} finally {
			closeSession(session);
		}

		return getReportPrint();
	}

	/**
	 * Den Mehrwertsteuersatz auf grund des Bruttobetrags und des
	 * Mehrwertsteuerbetrags erraten </br>
	 * Bei Faellen wie Bruttobetrag 0,15??? und 7,5% Mwst bzw. 8% Mwst funktioniert
	 * das leider auch nicht wirklich
	 * 
	 * @param mandant      der Mandant fuer den der Mehrwertsteuersatz ermittelt
	 *                     werden soll
	 * @param tBelegDatum  das (Beleg)datum fuer das die zugrundeliegende Mwst
	 *                     ermittelt werden soll
	 * @param bruttoBetrag
	 * @param mwstBetrag
	 * @return den Mehrwertsteuersatz in Prozent, also 10, 20 oder auch 7,5 bzw. 8%.
	 */
	protected BigDecimal getMwstSatzVonBruttoBetragUndUst(String mandant, Timestamp tBelegDatum,
			BigDecimal bruttoBetrag, BigDecimal mwstBetrag) {
		if (null == mwstBetrag || mwstBetrag.signum() == 0)
			return BigDecimal.ZERO;

		MwstsatzDto[] mwstdtos = getMandantFac().mwstsatzfindAllByMandant(mandant, tBelegDatum, false);

		BigDecimal minDiff = null;
		BigDecimal selectedMwstSatz = null;
		for (MwstsatzDto mwstsatzDto : mwstdtos) {
			BigDecimal satz = new BigDecimal(mwstsatzDto.getFMwstsatz());
			BigDecimal mwst = Helper.getMehrwertsteuerBetrag(bruttoBetrag, satz);
			BigDecimal diff = mwstBetrag.subtract(mwst).abs();
			if (minDiff == null || diff.compareTo(minDiff) < 0) {
				minDiff = diff;
				selectedMwstSatz = satz;
			}
		}

		return selectedMwstSatz;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printFehlendeZollpapiere(TheClientDto theClientDto) {
		this.useCase = UC_FEHLENDE_ZOLLPAPIERE;

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		session = factory.openSession();

		String s = "from FLREingangsrechnung er WHERE er.flrlieferant.b_zollimportpapier=1 AND er.mandant_c_nr='"
				+ theClientDto.getMandant() + "' AND er.t_zollimportpapier IS NULL AND er.eingangsrechnungart_c_nr = '"
				+ EingangsrechnungFac.EINGANGSRECHNUNGART_EINGANGSRECHNUNG
				+ "' ORDER BY er.flrlieferant.flrpartner.c_name1nachnamefirmazeile1, er.c_nr";

		Map<String, Object> mapParameter = new TreeMap<String, Object>();

		Query query = session.createQuery(s);

		List<?> resultList = query.list();

		Iterator resultListIterator = resultList.iterator();

		ArrayList alDaten = new ArrayList();

		while (resultListIterator.hasNext()) {
			FLREingangsrechnung item = (FLREingangsrechnung) resultListIterator.next();

			Object[] oZeile = new Object[FEHLENDE_ZOLLPAPIERE_ANZAHL_FELDER];
			oZeile[FEHLENDE_ZOLLPAPIERE_ER_C_NR] = item.getC_nr();
			oZeile[FEHLENDE_ZOLLPAPIERE_BRUTTOBETRAG] = item.getN_betrag();
			oZeile[FEHLENDE_ZOLLPAPIERE_ER_DATUM] = item.getT_belegdatum();
			oZeile[FEHLENDE_ZOLLPAPIERE_LIEFERANT] = HelperServer
					.formatNameAusFLRPartner(item.getFlrlieferant().getFlrpartner());
			alDaten.add(oZeile);

		}
		session.close();
		session = factory.openSession();

		s = "SELECT ls, (select l.b_zollimportpapier from FLRLieferant l where l.mandant_c_nr=ls.mandant_c_nr and l.flrpartner.i_id=ls.flrkunde.flrpartner.i_id) as b_zollimport from FLRLieferschein ls WHERE ls.mandant_c_nr='"
				+ theClientDto.getMandant() + "' AND ls.t_zollexportpapier IS NULL AND ls.lieferscheinart_c_nr = '"
				+ LieferscheinFac.LSART_LIEFERANT
				+ "' ORDER BY ls.flrkunde.flrpartner.c_name1nachnamefirmazeile1, ls.c_nr";

		query = session.createQuery(s);

		resultList = query.list();

		resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {

			Object[] o = (Object[]) resultListIterator.next();

			FLRLieferschein item = (FLRLieferschein) o[0];
			Short bZoll = (Short) o[1];
			if (bZoll != null && Helper.short2boolean(bZoll) == true) {

				Object[] oZeile = new Object[FEHLENDE_ZOLLPAPIERE_ANZAHL_FELDER];
				oZeile[FEHLENDE_ZOLLPAPIERE_LIEFERSCHEIN_C_NR] = item.getC_nr();
				oZeile[FEHLENDE_ZOLLPAPIERE_BRUTTOBETRAG] = item.getN_gesamtwertinlieferscheinwaehrung();
				oZeile[FEHLENDE_ZOLLPAPIERE_ER_DATUM] = item.getD_belegdatum();
				oZeile[FEHLENDE_ZOLLPAPIERE_LIEFERANT] = HelperServer
						.formatNameAusFLRPartner(item.getFlrkunde().getFlrpartner());
				alDaten.add(oZeile);
			}
		}

		data = new Object[alDaten.size()][FEHLENDE_ZOLLPAPIERE_ANZAHL_FELDER];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(mapParameter, EingangsrechnungReportFac.REPORT_MODUL,
				EingangsrechnungReportFac.REPORT_EINGANGSRECHNUNG_FEHLENDE_ZOLLPAPIERE, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printErfassteZollpapiere(Date dVon, Date dBis, TheClientDto theClientDto) {
		this.useCase = UC_ERFASSTE_ZOLLPAPIERE;

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		session = factory.openSession();

		Map<String, Object> mapParameter = new TreeMap<String, Object>();

		mapParameter.put("P_VON", dVon);
		mapParameter.put("P_BIS", dBis);

		dBis = Helper.addiereTageZuDatum(dBis, 1);

		String s = "from FLREingangsrechnung er WHERE er.t_zollimportpapier>='" + Helper.formatDateWithSlashes(dVon)
				+ "' AND er.t_zollimportpapier<'" + Helper.formatDateWithSlashes(dBis) + "' AND er.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' ORDER BY er.flrlieferant.flrpartner.c_name1nachnamefirmazeile1, er.c_nr";

		Query query = session.createQuery(s);

		List<?> resultList = query.list();

		Iterator resultListIterator = resultList.iterator();

		data = new Object[resultList.size()][ERFASSTE_ZOLLPAPIERE_ANZAHL_FELDER];

		ArrayList alDaten = new ArrayList();

		while (resultListIterator.hasNext()) {
			FLREingangsrechnung item = (FLREingangsrechnung) resultListIterator.next();

			Object[] oZeile = new Object[ERFASSTE_ZOLLPAPIERE_ANZAHL_FELDER];

			oZeile[ERFASSTE_ZOLLPAPIERE_ER_NUMMER] = item.getC_nr();
			oZeile[ERFASSTE_ZOLLPAPIERE_BRUTTOBETRAG] = item.getN_betrag();
			oZeile[ERFASSTE_ZOLLPAPIERE_ER_DATUM] = item.getT_belegdatum();
			oZeile[ERFASSTE_ZOLLPAPIERE_LIEFERANT] = HelperServer
					.formatNameAusFLRPartner(item.getFlrlieferant().getFlrpartner());
			oZeile[ERFASSTE_ZOLLPAPIERE_ERFASSUNGSZEITPUNKT] = item.getT_zollimportpapier();
			try {
				EingangsrechnungDto erDto = getEingangsrechnungFac().eingangsrechnungFindByPrimaryKey(item.getI_id());

				oZeile[ERFASSTE_ZOLLPAPIERE_ZOLLPAPIERNUMMER] = erDto.getCZollimportpapier();
				oZeile[ERFASSTE_ZOLLPAPIERE_WAEHRUNG] = erDto.getWaehrungCNr();

				if (erDto.getPersonalIIdZollimportpapier() != null) {
					oZeile[ERFASSTE_ZOLLPAPIERE_PERSON_ERFASST] = getPersonalFac()
							.personalFindByPrimaryKey(erDto.getPersonalIIdZollimportpapier(), theClientDto)
							.formatFixName1Name2();
				}

				if (erDto.getEingangsrechnungIdZollimport() != null) {
					EingangsrechnungDto erDtoZollmiport = getEingangsrechnungFac()
							.eingangsrechnungFindByPrimaryKey(erDto.getEingangsrechnungIdZollimport());
					oZeile[ERFASSTE_ZOLLPAPIERE_EINGANGSRECHNUNG_ZOLLPAPIER] = erDtoZollmiport.getCNr();
				}

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);

			}
			alDaten.add(oZeile);
		}

		session.close();
		session = factory.openSession();

		s = "from FLRLieferschein ls WHERE ls.t_zollexportpapier>='" + Helper.formatDateWithSlashes(dVon)
				+ "' AND ls.t_zollexportpapier<'" + Helper.formatDateWithSlashes(dBis) + "' AND ls.mandant_c_nr='"
				+ theClientDto.getMandant() + "' ORDER BY ls.flrkunde.flrpartner.c_name1nachnamefirmazeile1, ls.c_nr";

		query = session.createQuery(s);

		resultList = query.list();

		resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			FLRLieferschein item = (FLRLieferschein) resultListIterator.next();

			Object[] oZeile = new Object[ERFASSTE_ZOLLPAPIERE_ANZAHL_FELDER];

			oZeile[ERFASSTE_ZOLLPAPIERE_LS_NUMMER] = item.getC_nr();
			oZeile[ERFASSTE_ZOLLPAPIERE_BRUTTOBETRAG] = item.getN_gesamtwertinlieferscheinwaehrung();
			oZeile[ERFASSTE_ZOLLPAPIERE_ER_DATUM] = item.getD_belegdatum();
			oZeile[ERFASSTE_ZOLLPAPIERE_LIEFERANT] = HelperServer
					.formatNameAusFLRPartner(item.getFlrkunde().getFlrpartner());
			oZeile[ERFASSTE_ZOLLPAPIERE_ERFASSUNGSZEITPUNKT] = item.getT_zollexportpapier();
			try {
				LieferscheinDto lsDto = getLieferscheinFac().lieferscheinFindByPrimaryKey(item.getI_id());

				oZeile[ERFASSTE_ZOLLPAPIERE_ZOLLPAPIERNUMMER] = lsDto.getCZollexportpapier();
				oZeile[ERFASSTE_ZOLLPAPIERE_WAEHRUNG] = lsDto.getWaehrungCNr();

				if (lsDto.getPersonalIIdZollexportpapier() != null) {
					oZeile[ERFASSTE_ZOLLPAPIERE_PERSON_ERFASST] = getPersonalFac()
							.personalFindByPrimaryKey(lsDto.getPersonalIIdZollexportpapier(), theClientDto)
							.formatFixName1Name2();
				}

				if (lsDto.getEingangsrechnungIdZollexport() != null) {
					EingangsrechnungDto erDtoZollmiport = getEingangsrechnungFac()
							.eingangsrechnungFindByPrimaryKey(lsDto.getEingangsrechnungIdZollexport());
					oZeile[ERFASSTE_ZOLLPAPIERE_EINGANGSRECHNUNG_ZOLLPAPIER] = erDtoZollmiport.getCNr();
				}

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);

			}
			alDaten.add(oZeile);
		}

		data = new Object[alDaten.size()][ERFASSTE_ZOLLPAPIERE_ANZAHL_FELDER];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(mapParameter, EingangsrechnungReportFac.REPORT_MODUL,
				EingangsrechnungReportFac.REPORT_EINGANGSRECHNUNG_ERFASSTE_ZOLLPAPIERE, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printDokumente(Integer eingangsrechnungIId, TheClientDto theClientDto) {
		this.useCase = UC_DOKUMENTE;

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		session = factory.openSession();

		Map<String, Object> mapParameter = new TreeMap<String, Object>();
		EingangsrechnungDto eingangsrechnungDto = null;
		try {
			eingangsrechnungDto = getEingangsrechnungFac().eingangsrechnungFindByPrimaryKey(eingangsrechnungIId);

			mapParameter.put("P_BELEGNUMMER", eingangsrechnungDto.getCNr());

			mapParameter.put("P_LIEFERANT_RECHNUNGSNUMMER", eingangsrechnungDto.getCLieferantenrechnungsnummer());
			mapParameter.put("P_LIEFERANT_KUNDENDATEN", eingangsrechnungDto.getCKundendaten());
			mapParameter.put("P_TEXT", eingangsrechnungDto.getCText());
			mapParameter.put("P_WEARTIKEL", eingangsrechnungDto.getCWeartikel());

			mapParameter.put("P_STATUS", eingangsrechnungDto.getStatusCNr());

			mapParameter.put("P_BRUTTOBETRAG", eingangsrechnungDto.getNBetragfw());
			mapParameter.put("P_KURS", eingangsrechnungDto.getNKurs());
			mapParameter.put("P_MWSTBETRAG", eingangsrechnungDto.getNUstBetragfw());

			if (eingangsrechnungDto.getMwstsatzIId() != null) {
				mapParameter.put("P_MWSTSATZ", new BigDecimal(getMandantFac()
						.mwstsatzFindByPrimaryKey(eingangsrechnungDto.getMwstsatzIId(), theClientDto).getFMwstsatz()));
			}

			mapParameter.put("P_WAEHRUNG", eingangsrechnungDto.getWaehrungCNr());

			mapParameter.put("P_IGERWERB", Helper.short2boolean(eingangsrechnungDto.getBIgErwerb()));

			if (eingangsrechnungDto.getKostenstelleIId() != null) {
				KostenstelleDto kstDto = getSystemFac()
						.kostenstelleFindByPrimaryKey(eingangsrechnungDto.getKostenstelleIId());

				mapParameter.put("P_KOSTENSTELLE_NUMMER", kstDto.getCNr());
				mapParameter.put("P_KOSTENSTELLE_BEZEICHNUNG", kstDto.getCBez());

			}

			if (eingangsrechnungDto.getKontoIId() != null) {
				KontoDtoSmall kontoDto = getFinanzFac().kontoFindByPrimaryKeySmall(eingangsrechnungDto.getKontoIId());

				mapParameter.put("P_KONTO_NUMMER", kontoDto.getCNr());
				mapParameter.put("P_KONTO_BEZEICHNUNG", kontoDto.getCBez());

			}

			if (eingangsrechnungDto.getReversechargeartId() != null) {
				ReversechargeartDto raDto = getFinanzServiceFac()
						.reversechargeartFindByPrimaryKey(eingangsrechnungDto.getReversechargeartId(), theClientDto);
				mapParameter.put("P_REVERSECHARGEART", raDto.getCNr());
			}

			LieferantDto lieferantDto = getLieferantFac()
					.lieferantFindByPrimaryKey(eingangsrechnungDto.getLieferantIId(), theClientDto);
			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(lieferantDto.getPartnerIId(), theClientDto);
			mapParameter.put("P_LIEFERANT_NAME1", partnerDto.getCName1nachnamefirmazeile1());
			mapParameter.put("P_LIEFERANT_NAME2", partnerDto.getCName2vornamefirmazeile2());
			mapParameter.put("P_LIEFERANT_NAME3", partnerDto.getCName3vorname2abteilung());

			mapParameter.put("P_EINGANGSRECHNUNGSART", eingangsrechnungDto.getEingangsrechnungartCNr());

			mapParameter.put("P_LIEFERANT_STRASSE", partnerDto.getCStrasse());
			String sLKZ = "";
			String sPLZ = "";
			String sOrt = "";
			if (partnerDto.getLandplzortDto() != null) {
				if (partnerDto.getLandplzortDto().getLandDto() != null) {
					sLKZ = partnerDto.getLandplzortDto().getLandDto().getCLkz();
				}
				sPLZ = partnerDto.getLandplzortDto().getCPlz();
				if (partnerDto.getLandplzortDto().getOrtDto() != null) {
					sOrt = partnerDto.getLandplzortDto().getOrtDto().getCName();
				}
			}

			mapParameter.put("P_LIEFERANT_LKZ", sLKZ);
			mapParameter.put("P_LIEFERANT_PLZ", sPLZ);
			mapParameter.put("P_LIEFERANT_ORT", sOrt);

			if (lieferantDto.getKontoIIdKreditorenkonto() != null) {
				mapParameter.put("P_LIEFERANT_KREDITORENKONTO",
						getFinanzFac().kontoFindByPrimaryKey(lieferantDto.getKontoIIdKreditorenkonto()).getCNr());
			}

			EingangsrechnungKontierungDto[] kontierungDtos = getEingangsrechnungFac()
					.eingangsrechnungKontierungFindByEingangsrechnungIId(eingangsrechnungIId);

			if (kontierungDtos.length > 0) {
				String[] fieldnames = new String[] { "F_BETRAG", "F_USTBETRAG", "F_KOSTENSTELLE_NUMMER",
						"F_KOSTENSTELLE_BEZEICHNUNG", "F_SACHKONTO_NUMMER", "F_SACHKONTO_BEZEICHNUNG", "F_UST",
						"F_REVERSECHARGEART" };

				ArrayList alDatenSub = new ArrayList();

				for (int j = 0; j < kontierungDtos.length; j++) {

					Object[] zeileSub = new Object[8];
					zeileSub[0] = kontierungDtos[j].getNBetrag();
					zeileSub[1] = kontierungDtos[j].getNBetragUst();

					KostenstelleDto kstDto = getSystemFac()
							.kostenstelleFindByPrimaryKey(kontierungDtos[j].getKostenstelleIId());

					KontoDtoSmall kontoDto = getFinanzFac().kontoFindByPrimaryKeySmall(kontierungDtos[j].getKontoIId());

					zeileSub[2] = kstDto.getCNr();
					zeileSub[3] = kstDto.getCBez();
					zeileSub[4] = kontoDto.getCNr();
					zeileSub[5] = kontoDto.getCBez();
					zeileSub[6] = new BigDecimal(getMandantFac()
							.mwstsatzFindByPrimaryKey(kontierungDtos[j].getMwstsatzIId(), theClientDto).getFMwstsatz());
					ReversechargeartDto raDto = getFinanzServiceFac()
							.reversechargeartFindByPrimaryKey(kontierungDtos[j].getReversechargeartId(), theClientDto);

					zeileSub[7] = raDto.getCNr();

					alDatenSub.add(zeileSub);

				}

				Object[][] dataSub = new Object[alDatenSub.size()][5];
				dataSub = (Object[][]) alDatenSub.toArray(dataSub);

				mapParameter.put("P_SUBREPORT_KONTIERUNG", new LPDatenSubreport(dataSub, fieldnames));

			}

			DocPath docpathER = new DocPath(new DocNodeEingangsrechnung(eingangsrechnungDto));
			try {
				List<DocNodeBase> docsKopf = getJCRDocFac().getDocNodeChildrenFromNode(docpathER, theClientDto);
				String[] fieldnames = new String[] { "F_DATEINAME", "F_DATA", "F_ZEITPUNKT" };
				ArrayList alDatenSub = new ArrayList();

				for (int u = 0; u < docsKopf.size(); u++) {

					DocNodeBase base = docsKopf.get(u);

					if (base.getNodeType() == DocNodeBase.FILE) {

						JCRDocDto jcrDocDto = ((DocNodeFile) base).getJcrDocDto();

						ArrayList<DocNodeVersion> versions = getJCRDocFac().getAllVersions(jcrDocDto);

						for (int j = 0; j < versions.size(); j++) {

							JCRDocDto jcrDocDtoVersion = versions.get(j).getJCRDocDto();

							java.sql.Timestamp tZeitpunkt = new java.sql.Timestamp(jcrDocDtoVersion.getlZeitpunkt());

							JCRDocDto jcrData = getJCRDocFac().getData(jcrDocDtoVersion);

							if (jcrData.getbData() != null) {

								Object[] zeileSub = new Object[10];
								zeileSub[0] = jcrData.getsFilename();
								zeileSub[1] = jcrData.getbData();
								zeileSub[2] = tZeitpunkt;
								alDatenSub.add(zeileSub);
							}

						}

					}
				}

				Object[][] dataSub = new Object[alDatenSub.size()][10];
				dataSub = (Object[][]) alDatenSub.toArray(dataSub);

				mapParameter.put("P_SUBREPORT_DOKUMENTE", new LPDatenSubreport(dataSub, fieldnames));

			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		data = new Object[0][0];

		initJRDS(mapParameter, EingangsrechnungReportFac.REPORT_MODUL,
				EingangsrechnungReportFac.REPORT_EINGANGSRECHNUNG_DOKUMENTE, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printNichtabgerechneteAnzahlungen(java.sql.Date dStichtag, TheClientDto theClientDto) {
		this.useCase = UC_NICHT_ABGERECHNETE_AZ;

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		session = factory.openSession();

		Map<String, Object> mapParameter = new TreeMap<String, Object>();

		String s = "SELECT er from FLREingangsrechnung er WHERE er.mandant_c_nr='" + theClientDto.getMandant()
				+ "' AND flrbestellung.i_id is not null AND er.status_c_nr <>'" + LocaleFac.STATUS_STORNIERT
				+ "' AND er.eingangsrechnungart_c_nr='" + EingangsrechnungFac.EINGANGSRECHNUNGART_ANZAHLUNG
				+ "' AND (SELECT COUNT(*) FROM FLREingangsrechnung er2 WHERE er2.eingangsrechnungart_c_nr='"
				+ EingangsrechnungFac.EINGANGSRECHNUNGART_SCHLUSSZAHLUNG
				+ "' AND er2.flrbestellung.i_id=er.flrbestellung.i_id AND er2.status_c_nr <> '"
				+ LocaleFac.STATUS_STORNIERT + "')<1 ";

		if (dStichtag != null) {
			s += " AND er.t_belegdatum<='" + Helper.formatDateWithSlashes(dStichtag) + "'";

			mapParameter.put("P_SICHTAG", dStichtag);

		}

		s += " ORDER BY er.flrlieferant.flrpartner.c_name1nachnamefirmazeile1, er.c_nr";

		Query query = session.createQuery(s);

		List<?> resultList = query.list();

		Iterator resultListIterator = resultList.iterator();

		ArrayList alDaten = new ArrayList();

		while (resultListIterator.hasNext()) {
			FLREingangsrechnung item = (FLREingangsrechnung) resultListIterator.next();

			Object[] oZeile = new Object[NICHT_ABGERECHNETE_AZ_ANZAHL_FELDER];

			oZeile[NICHT_ABGERECHNETE_AZ_BELEGNUMMER] = item.getC_nr();
			oZeile[NICHT_ABGERECHNETE_AZ_BRUTTO] = item.getN_betrag();
			oZeile[NICHT_ABGERECHNETE_AZ_BELEGDATUM] = item.getT_belegdatum();
			oZeile[NICHT_ABGERECHNETE_AZ_LIEFERANT] = HelperServer
					.formatNameAusFLRPartner(item.getFlrlieferant().getFlrpartner());
			oZeile[NICHT_ABGERECHNETE_AZ_WAEHRUNG] = theClientDto.getSMandantenwaehrung();
			oZeile[NICHT_ABGERECHNETE_AZ_NETTO] = item.getN_betrag().subtract(item.getN_ustbetrag());
			oZeile[NICHT_ABGERECHNETE_AZ_BESTELLNUMMER] = item.getFlrbestellung().getC_nr();
			oZeile[NICHT_ABGERECHNETE_AZ_FREIGABEDATUM] = item.getT_freigabedatum();
			if (item.getFlrlieferant().getFlrkonto() != null) {
				oZeile[NICHT_ABGERECHNETE_AZ_KREDITORENNNUMMER] = item.getFlrlieferant().getFlrkonto().getC_nr();
			}

			try {
				EingangsrechnungDto erDto = getEingangsrechnungFac().eingangsrechnungFindByPrimaryKey(item.getI_id());

				if (erDto.getZahlungszielIId() != null) {
					oZeile[NICHT_ABGERECHNETE_AZ_ZAHLUNGSZIEL] = getMandantFac()
							.zahlungszielFindByPrimaryKey(erDto.getZahlungszielIId(), theClientDto).getCBez();
				}

				if (item.getFlrlieferant().getFlrpartner().getFlrlandplzort() != null) {
					oZeile[NICHT_ABGERECHNETE_AZ_LKZ] = item.getFlrlieferant().getFlrpartner().getFlrlandplzort()
							.getFlrland().getC_lkz();
				}

				oZeile[NICHT_ABGERECHNETE_AZ_LAENDERART] = getFinanzServiceFac().getLaenderartZuPartner(
						item.getFlrlieferant().getFlrpartner().getI_id(), Helper.asTimestamp(item.getT_belegdatum()),
						theClientDto);

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);

			}
			alDaten.add(oZeile);
		}

		session.close();

		data = new Object[alDaten.size()][NICHT_ABGERECHNETE_AZ_ANZAHL_FELDER];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(mapParameter, EingangsrechnungReportFac.REPORT_MODUL,
				EingangsrechnungReportFac.REPORT_EINGANGSRECHNUNG_NICHT_ABGERECHNETE_ANZAHLUNGEN,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printKontierungsjournal(TheClientDto theClientDto, int iFilterER, Integer kostenstelleIId,
			int iKritDatum, Date dVon, Date dBis, boolean bZusatzkosten) {
		Session session = null;
		try {
			this.useCase = UC_KONTIERUNG;
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria cKontierung = session.createCriteria(FLREingangsrechnungKontierung.class);

			if (kostenstelleIId != null) {
				cKontierung.add(
						Restrictions.eq(EingangsrechnungFac.FLR_KONTIERUNG_FLRKOSTENSTELLE + ".i_id", kostenstelleIId));
			}
			// Filter nach ER-status
			Collection<String> cStati = new TreeSet<String>();
			if (iFilterER == EingangsrechnungReportFac.REPORT_KONTIERUNG_FILTER_ER_ALLE) {
				cStati.add(EingangsrechnungFac.STATUS_ANGELEGT);
				cStati.add(EingangsrechnungFac.STATUS_ERLEDIGT);
				cStati.add(EingangsrechnungFac.STATUS_TEILBEZAHLT);
				// cStati.add(EingangsrechnungFac.STATUS_VERBUCHT);
			} else if (iFilterER == EingangsrechnungReportFac.REPORT_KONTIERUNG_FILTER_ER_BEZAHLT) {
				cStati.add(EingangsrechnungFac.STATUS_ERLEDIGT);
			} else if (iFilterER == EingangsrechnungReportFac.REPORT_KONTIERUNG_FILTER_ER_OFFENE) {
				cStati.add(EingangsrechnungFac.STATUS_ANGELEGT);
				cStati.add(EingangsrechnungFac.STATUS_TEILBEZAHLT);
				// cStati.add(EingangsrechnungFac.STATUS_VERBUCHT);
			}

			Collection<String> cArten = new TreeSet<String>();

			if (bZusatzkosten) {
				cArten.add(EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN);

			} else {
				cArten.add(EingangsrechnungFac.EINGANGSRECHNUNGART_ANZAHLUNG);
				cArten.add(EingangsrechnungFac.EINGANGSRECHNUNGART_EINGANGSRECHNUNG);
				cArten.add(EingangsrechnungFac.EINGANGSRECHNUNGART_GUTSCHRIFT);
				cArten.add(EingangsrechnungFac.EINGANGSRECHNUNGART_SCHLUSSZAHLUNG);
			}

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);

			String mandantCNr = theClientDto.getMandant();
			Criteria cEigangsrechnung = cKontierung
					.createCriteria(EingangsrechnungFac.FLR_KONTIERUNG_FLREINGANGSRECHNUNG);

			if (iKritDatum == EingangsrechnungReportFac.REPORT_KONTIERUNG_KRIT_DATUM_BELEG) {
				cEigangsrechnung.add(Restrictions.eq(EingangsrechnungFac.FLR_ER_MANDANT_C_NR, mandantCNr))
						.add(Restrictions.ge(EingangsrechnungFac.FLR_ER_D_BELEGDATUM, dVon))
						.add(Restrictions.le(EingangsrechnungFac.FLR_ER_D_BELEGDATUM, dBis))
						.add(Restrictions.in(EingangsrechnungFac.FLR_ER_STATUS_C_NR, cStati))
						.add(Restrictions.in(EingangsrechnungFac.FLR_ER_EINGANGSRECHNUNGART_C_NR, cArten));
			} else if (iKritDatum == EingangsrechnungReportFac.REPORT_KONTIERUNG_KRIT_DATUM_FREIGABE) {
				cEigangsrechnung.add(Restrictions.eq(EingangsrechnungFac.FLR_ER_MANDANT_C_NR, mandantCNr))
						.add(Restrictions.ge(EingangsrechnungFac.FLR_ER_D_FREIGABEDATUM, dVon))
						.add(Restrictions.le(EingangsrechnungFac.FLR_ER_D_FREIGABEDATUM, dBis))
						.add(Restrictions.in(EingangsrechnungFac.FLR_ER_STATUS_C_NR, cStati))
						.add(Restrictions.in(EingangsrechnungFac.FLR_ER_EINGANGSRECHNUNGART_C_NR, cArten));
			}
			// Sortierung noch kostenstelle, konto
			cKontierung.createCriteria(EingangsrechnungFac.FLR_KONTIERUNG_FLRKOSTENSTELLE).addOrder(Order.asc("c_nr"));
			cKontierung.createCriteria(EingangsrechnungFac.FLR_KONTIERUNG_FLRKONTO).addOrder(Order.asc("c_nr"));

			List<?> listKontierung = cKontierung.list();
			ArrayList<ReportEingangsrechnungKontierungsjournalDto> coll = new ArrayList<ReportEingangsrechnungKontierungsjournalDto>();
			for (Iterator<?> iter = listKontierung.iterator(); iter.hasNext();) {
				FLREingangsrechnungKontierung item = (FLREingangsrechnungKontierung) iter.next();
				EingangsrechnungDto erDto = getEingangsrechnungFac()
						.eingangsrechnungFindByPrimaryKey(item.getEingangsrechnung_i_id());
				ReportEingangsrechnungKontierungsjournalDto dto = new ReportEingangsrechnungKontierungsjournalDto();
				// Bezahlter Wert in Mandantenwaehrung
				BigDecimal bdBezahlt = getEingangsrechnungFac().getBezahltBetrag(erDto.getIId(), item.getI_id());
				dto.setBdBezahlt(bdBezahlt);

				if (bdBezahlt.signum() != 0) {
					System.out.println("halt");
				}
				dto.setBdBezahltFW(getEingangsrechnungFac().getBezahltBetragFw(erDto.getIId(), item.getI_id()));

				// Bezahlt Wert mit Kurs der ER in Mandantenwaehrung
				BigDecimal bdBezahltERKurs = getEingangsrechnungFac().getBezahltBetragFwKontierung(erDto.getIId(),
						item.getI_id());
				if (bdBezahltERKurs != null) {
					bdBezahltERKurs = bdBezahltERKurs.multiply(erDto.getNKurs());
				}
				dto.setBdBezahltzuERKurs(bdBezahltERKurs);
				// Letztes Zahldatum
				EingangsrechnungzahlungDto letzteZahlung = getEingangsrechnungFac().getLetzteZahlung(erDto.getIId());
				Date dLetztesZahldatum = null;
				if (letzteZahlung != null) {
					dLetztesZahldatum = new Date(letzteZahlung.getTZahldatum().getTime());
				}
				dto.setDLetzesZahldatum(dLetztesZahldatum);
				// Errechneter Steuersatz
				// BigDecimal bdErrechneterSteuersatz = erDto
				// .getNUstBetrag()
				// .divide(erDto.getNBetrag().subtract(
				// erDto.getNUstBetrag()), 4,
				// BigDecimal.ROUND_HALF_EVEN).movePointRight(2);
				// dto.setBdErrechneterSteuersatz(bdErrechneterSteuersatz);

				dto.setWaehrungCNr(erDto.getWaehrungCNr());

				dto.setBdUst(item.getN_betrag_ust().multiply(erDto.getNKurs()).setScale(FinanzFac.NACHKOMMASTELLEN,
						BigDecimal.ROUND_HALF_EVEN));
				dto.setBdUstFW(item.getN_betrag_ust());
				dto.setBdWert(item.getN_betrag().multiply(erDto.getNKurs()).setScale(FinanzFac.NACHKOMMASTELLEN,
						BigDecimal.ROUND_HALF_EVEN));
				dto.setBdWertFW(item.getN_betrag());
				BigDecimal bdErrechneterSteuersatz = getMwstSatzVonBruttoBetragUndUst(erDto.getMandantCNr(),
						new Timestamp(erDto.getDBelegdatum().getTime()), dto.getBdWert(), dto.getBdUst());
				// BigDecimal bdErrechneterSteuersatz = dto
				// .getBdUst()
				// .divide(dto.getBdWert().subtract(
				// dto.getBdUst()), 4,
				// BigDecimal.ROUND_HALF_EVEN).movePointRight(2);
				dto.setBdErrechneterSteuersatz(bdErrechneterSteuersatz);
				dto.setDEingangsrechnungsdatum(item.getFlreingangsrechnung().getT_belegdatum());
				dto.setSEingangsrechnungsnummer(item.getFlreingangsrechnung().getC_nr());

				dto.setBMitPositionen(Helper.short2boolean(item.getFlreingangsrechnung().getB_mitpositionen()));

				dto.setSArt(item.getFlreingangsrechnung().getEingangsrechnungart_c_nr());
				dto.setSEingangsrechnungText(erDto.getCText());
				dto.setSEingangsrechnungWeartikel(erDto.getCWeartikel());
				dto.setSKontobezeichnung(item.getFlrkonto().getC_bez());
				dto.setSKontonummer(item.getFlrkonto().getC_nr());
				dto.setSKostenstelleBezeichnung(item.getFlrkostenstelle().getC_bez());
				dto.setSKostenstellenummer(item.getFlrkostenstelle().getC_nr());
				dto.setSLieferant(item.getFlreingangsrechnung().getFlrlieferant().getFlrpartner()
						.getC_name1nachnamefirmazeile1());
				if (item.getFlrkonto().getFlrkontoart() != null) {
					dto.setSKontoart(getFinanzServiceFac().uebersetzeKontoartOptimal(
							item.getFlrkonto().getFlrkontoart().getC_nr(), theClientDto.getLocUi(),
							theClientDto.getLocMandant()));
				}

				if (item.getFlrkonto().getFinanzamt_i_id() != null) {
					FinanzamtDto faDto = getFinanzFac().finanzamtFindByPrimaryKey(
							item.getFlrkonto().getFinanzamt_i_id(), item.getFlrkonto().getMandant_c_nr(), theClientDto);

					dto.setSFinanzamtKonto(faDto.getPartnerDto().formatFixTitelName1Name2());

				}
				if (item.getFlreingangsrechnung().getFlrlieferant().getFlrkonto() != null
						&& item.getFlreingangsrechnung().getFlrlieferant().getFlrkonto().getFinanzamt_i_id() != null) {
					FinanzamtDto faDto = getFinanzFac().finanzamtFindByPrimaryKey(
							item.getFlreingangsrechnung().getFlrlieferant().getFlrkonto().getFinanzamt_i_id(),
							item.getFlrkonto().getMandant_c_nr(), theClientDto);
					dto.setSFinanzamtLieferant(faDto.getPartnerDto().formatFixTitelName1Name2());
				}

				if ((dto.getSFinanzamtKonto() == null && dto.getSFinanzamtLieferant() == null)
						|| (dto.getSFinanzamtKonto() != null && dto.getSFinanzamtLieferant() != null
								&& dto.getSFinanzamtKonto().equals(dto.getSFinanzamtLieferant()))) {

					dto.setBFinanzaemterUnterschiedlich(Boolean.FALSE);

				}

				// SP2121
				PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(
						item.getFlreingangsrechnung().getFlrlieferant().getFlrpartner().getI_id(), theClientDto);
				String laenderartCNr = getFinanzServiceFac().getLaenderartZuPartner(mandantDto, partnerDto,
						Helper.asTimestamp(item.getFlreingangsrechnung().getT_belegdatum()), theClientDto);
				dto.setSPartnerartLieferant(laenderartCNr);

				if (item.getFlrkonto().getUvaart_i_id() != null) {
					dto.setSUVAArt(item.getFlrkonto().getFlruvaart().getC_nr());
				}

				if (item.getFlreingangsrechnung().getFlrlieferant().getFlrkonto() != null && item
						.getFlreingangsrechnung().getFlrlieferant().getFlrkonto().getSteuerkategorie_c_nr() != null) {
					dto.setSSteuerkategorie(
							item.getFlreingangsrechnung().getFlrlieferant().getFlrkonto().getSteuerkategorie_c_nr());
				}

				coll.add(dto);
			}

			// jetzt noch die nicht mehrfach kontierten holen
			Criteria cER = session.createCriteria(FLREingangsrechnungReport.class);
			if (kostenstelleIId != null) {
				cER.add(Restrictions.eq(EingangsrechnungFac.FLR_ER_FLRKOSTENSTELLE + ".i_id", kostenstelleIId));
			}
			if (bZusatzkosten) {
				cER.add(Restrictions.eq(EingangsrechnungFac.FLR_ER_EINGANGSRECHNUNGART_C_NR,
						EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN));
			} else {
				cER.add(Restrictions.not(Restrictions.eq(EingangsrechnungFac.FLR_ER_EINGANGSRECHNUNGART_C_NR,
						EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN)));
			}

			if (iKritDatum == EingangsrechnungReportFac.REPORT_KONTIERUNG_KRIT_DATUM_BELEG) {
				cER.add(Restrictions.eq(EingangsrechnungFac.FLR_ER_MANDANT_C_NR, mandantCNr))
						.add(Restrictions.ge(EingangsrechnungFac.FLR_ER_D_BELEGDATUM, dVon))
						.add(Restrictions.le(EingangsrechnungFac.FLR_ER_D_BELEGDATUM, dBis))
						.add(Restrictions.in(EingangsrechnungFac.FLR_ER_STATUS_C_NR, cStati));
			} else if (iKritDatum == EingangsrechnungReportFac.REPORT_KONTIERUNG_KRIT_DATUM_FREIGABE) {
				cER.add(Restrictions.eq(EingangsrechnungFac.FLR_ER_MANDANT_C_NR, mandantCNr))
						.add(Restrictions.ge(EingangsrechnungFac.FLR_ER_D_FREIGABEDATUM, dVon))
						.add(Restrictions.le(EingangsrechnungFac.FLR_ER_D_FREIGABEDATUM, dBis))
						.add(Restrictions.in(EingangsrechnungFac.FLR_ER_STATUS_C_NR, cStati));
			}

			List<?> listER = cER.list();
			for (Iterator<?> iter = listER.iterator(); iter.hasNext();) {
				FLREingangsrechnungReport item = (FLREingangsrechnungReport) iter.next();
				EingangsrechnungDto erDto = getEingangsrechnungFac().eingangsrechnungFindByPrimaryKey(item.getI_id());
				if (erDto.getKostenstelleIId() != null) {
					ReportEingangsrechnungKontierungsjournalDto dto = new ReportEingangsrechnungKontierungsjournalDto();
					// Bezahlter Wert in Mandantenwaehrung
					BigDecimal bdBezahlt = getEingangsrechnungFac().getBezahltBetrag(erDto.getIId(), null);
					dto.setBdBezahlt(bdBezahlt);
					// Bezahlt Wert mit Kurs der ER in Mandantenwaehrung
					BigDecimal bdBezahltERKurs = getEingangsrechnungFac().getBezahltBetragFw(erDto.getIId(), null);

					if (bdBezahltERKurs != null) {

						dto.setBdBezahltFW(new BigDecimal(bdBezahltERKurs.doubleValue()));

						bdBezahltERKurs = bdBezahltERKurs.multiply(erDto.getNKurs());
					}
					dto.setBdBezahltzuERKurs(bdBezahltERKurs);
					// Letztes Zahldatum
					EingangsrechnungzahlungDto letzteZahlung = getEingangsrechnungFac()
							.getLetzteZahlung(erDto.getIId());
					Date dLetztesZahldatum = null;
					if (letzteZahlung != null) {
						dLetztesZahldatum = new Date(letzteZahlung.getTZahldatum().getTime());
					}
					dto.setBdERKurs(erDto.getNKurs());
					dto.setWaehrungCNr(erDto.getWaehrungCNr());
					dto.setDLetzesZahldatum(dLetztesZahldatum);
					// Werte
					dto.setBdUst(
							erDto.getNUstBetrag().setScale(FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN));
					dto.setBdWert(erDto.getNBetrag().setScale(FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN));

					dto.setBdUstFW(
							erDto.getNUstBetragfw().setScale(FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN));
					dto.setBdWertFW(
							erDto.getNBetragfw().setScale(FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN));

					// Errechneter Steuersatz
					BigDecimal bdErrechneterSteuersatz = null;
					if (erDto.getNBetrag().subtract(erDto.getNUstBetrag()).doubleValue() == 0) {
						bdErrechneterSteuersatz = new BigDecimal(100);
					} else {
						// bdErrechneterSteuersatz = erDto
						// .getNUstBetrag()
						// .divide(erDto.getNBetrag().subtract(
						// erDto.getNUstBetrag()), 4,
						// BigDecimal.ROUND_HALF_EVEN)
						// .movePointRight(2);
						bdErrechneterSteuersatz = getMwstSatzVonBruttoBetragUndUst(erDto.getMandantCNr(),
								new Timestamp(erDto.getDBelegdatum().getTime()), dto.getBdWert(), dto.getBdUst());
					}

					dto.setBdErrechneterSteuersatz(bdErrechneterSteuersatz);

					dto.setDEingangsrechnungsdatum(item.getT_belegdatum());
					dto.setSEingangsrechnungsnummer(item.getC_nr());

					dto.setBMitPositionen(Helper.short2boolean(item.getB_mitpositionen()));

					dto.setSArt(item.getEingangsrechnungart_c_nr());
					dto.setSEingangsrechnungText(erDto.getCText());
					dto.setSEingangsrechnungWeartikel(erDto.getCWeartikel());
					dto.setSLieferantenrechnungsnummer(item.getC_lieferantenrechnungsnummer());
					dto.setDFreigabedatum(item.getT_freigabedatum());
					if (erDto.getKontoIId() != null) {
						KontoDto kontoDto = getFinanzFac().kontoFindByPrimaryKey(erDto.getKontoIId());
						dto.setSKontobezeichnung(kontoDto.getCBez());
						dto.setSKontonummer(kontoDto.getCNr());
						if (kontoDto.getKontoartCNr() != null) {
							dto.setSKontoart(getFinanzServiceFac().uebersetzeKontoartOptimal(kontoDto.getKontoartCNr(),
									theClientDto.getLocUi(), theClientDto.getLocMandant()));
						}
					} else {

						dto.setSKontobezeichnung(getTextRespectUISpr("er.kontierungfehlerhaft",
								theClientDto.getMandant(), theClientDto.getLocUi()));
						dto.setSKontonummer("ERR");
					}

					dto.setSKostenstelleBezeichnung(item.getFlrkostenstelle().getC_bez());
					dto.setSKostenstellenummer(item.getFlrkostenstelle().getC_nr());
					dto.setSLieferant(item.getFlrlieferant().getFlrpartner().getC_name1nachnamefirmazeile1());

					if (item.getFlrlieferant().getKonto_i_id_kreditorenkonto() != null) {
						KontoDto kredKontoDto = getFinanzFac()
								.kontoFindByPrimaryKey(item.getFlrlieferant().getKonto_i_id_kreditorenkonto());
						dto.setSKreditorennummer(kredKontoDto.getCNr());
					} else {
						dto.setSKreditorennummer(null);
					}

					if (item.getFlrkonto().getFinanzamt_i_id() != null) {
						FinanzamtDto faDto = getFinanzFac().finanzamtFindByPrimaryKey(
								item.getFlrkonto().getFinanzamt_i_id(), item.getFlrkonto().getMandant_c_nr(),
								theClientDto);

						dto.setSFinanzamtKonto(faDto.getPartnerDto().formatFixTitelName1Name2());

					}
					if (item.getFlrlieferant().getFlrkonto() != null
							&& item.getFlrlieferant().getFlrkonto().getFinanzamt_i_id() != null) {
						FinanzamtDto faDto = getFinanzFac().finanzamtFindByPrimaryKey(
								item.getFlrlieferant().getFlrkonto().getFinanzamt_i_id(),
								item.getFlrkonto().getMandant_c_nr(), theClientDto);
						dto.setSFinanzamtLieferant(faDto.getPartnerDto().formatFixTitelName1Name2());
					}

					if ((dto.getSFinanzamtKonto() == null && dto.getSFinanzamtLieferant() == null)
							|| (dto.getSFinanzamtKonto() != null && dto.getSFinanzamtLieferant() != null
									&& dto.getSFinanzamtKonto().equals(dto.getSFinanzamtLieferant()))) {

						dto.setBFinanzaemterUnterschiedlich(Boolean.FALSE);

					}

					// SP2121
					PartnerDto partnerDto = getPartnerFac()
							.partnerFindByPrimaryKey(item.getFlrlieferant().getFlrpartner().getI_id(), theClientDto);
					String laenderartCNr = getFinanzServiceFac().getLaenderartZuPartner(mandantDto, partnerDto,
							Helper.asTimestamp(erDto.getDBelegdatum()), theClientDto);
					dto.setSPartnerartLieferant(laenderartCNr);

					if (item.getFlrkonto().getUvaart_i_id() != null) {
						dto.setSUVAArt(item.getFlrkonto().getFlruvaart().getC_nr());
					}

					if (item.getFlrlieferant().getFlrkonto() != null
							&& item.getFlrlieferant().getFlrkonto().getSteuerkategorie_c_nr() != null) {
						dto.setSSteuerkategorie(item.getFlrlieferant().getFlrkonto().getSteuerkategorie_c_nr());
					}
					
					
					if (item.getFlrpersonal_geprueft() != null) {
						dto.setTGeprueftAm(item.getT_geprueft());
						dto.setSGeprueftVon( item.getFlrpersonal_geprueft().getC_kurzzeichen());
					}
					

					coll.add(dto);
				}
			}
			// Sortieren
			Collections.sort(coll, new ComparatorKontierungsjournal(Helper.SORTIERUNG_NACH_KOSTENSTELLE_UND_KONTO));

			data = new Object[coll.size()][KONTIERUNG_ANZAHL_FELDER];
			int i = 0;
			for (Iterator<ReportEingangsrechnungKontierungsjournalDto> iter = coll.iterator(); iter.hasNext(); i++) {
				ReportEingangsrechnungKontierungsjournalDto item = (ReportEingangsrechnungKontierungsjournalDto) iter
						.next();
				data[i][KONTIERUNG_FELD_WERT_BEZAHLT] = item.getBdBezahlt();
				data[i][KONTIERUNG_FELD_WERT_UST] = item.getBdUst();
				data[i][KONTIERUNG_FELD_WERT] = item.getBdWert();

				data[i][KONTIERUNG_FELD_WERT_BEZAHLT_FW] = item.getBdBezahltFW();
				data[i][KONTIERUNG_FELD_WERT_UST_FW] = item.getBdUstFW();
				data[i][KONTIERUNG_FELD_WERT_FW] = item.getBdWertFW();
				data[i][KONTIERUNG_FELD_ER_KURS] = item.getBdERKurs();
				data[i][KONTIERUNG_FELD_WAEHRUNG_C_NR] = item.getWaehrungCNr();
				data[i][KONTIERUNG_FELD_MIT_POSITIONEN] = item.getBMitPositionen();

				data[i][KONTIERUNG_FELD_ER_DATUM] = item.getDEingangsrechnungsdatum();
				data[i][KONTIERUNG_FELD_ER_C_NR] = item.getSEingangsrechnungsnummer();
				data[i][KONTIERUNG_FELD_ART] = item.getSArt();
				data[i][KONTIERUNG_FELD_ER_TEXT] = item.getSEingangsrechnungText();
				data[i][KONTIERUNG_FELD_ER_WEARTIKEL] = item.getSEingangsrechnungWeartikel();
				data[i][KONTIERUNG_FELD_KONTOART_C_NR] = item.getSKontoart();
				data[i][KONTIERUNG_FELD_KONTO_C_BEZ] = item.getSKontobezeichnung();
				data[i][KONTIERUNG_FELD_KONTO_C_NR] = item.getSKontonummer();
				data[i][KONTIERUNG_FELD_KOSTENSTELLE_C_BEZ] = item.getSKostenstelleBezeichnung();
				data[i][KONTIERUNG_FELD_KOSTENSTELLE_C_NR] = item.getSKostenstellenummer();
				data[i][KONTIERUNG_FELD_ER_LIEFERANT] = item.getSLieferant();

				data[i][KONTIERUNG_FELD_FINANZAEMTER_UNTERSCHIEDLICH] = item.getBFinanzaemterUnterschiedlich();
				data[i][KONTIERUNG_FELD_FINANZAMT_KONTO] = item.getSFinanzamtKonto();
				data[i][KONTIERUNG_FELD_FINANZAMT_LIEFERANT] = item.getSFinanzamtLieferant();

				data[i][KONTIERUNG_FELD_LAENDERART_LIEFERANT] = item.getSPartnerartLieferant();
				data[i][KONTIERUNG_FELD_UVA_ART] = item.getSUVAArt();
				data[i][KONTIERUNG_FELD_STEUERKATEGORIE] = item.getSSteuerkategorie();
				data[i][KONTIERUNG_FELD_LETZTES_ZAHLDATUM] = item.getDLetzesZahldatum();
				data[i][KONTIERUNG_FELD_ERRECHNETER_STEUERSATZ] = item.getBdErrechneterSteuersatz();
				data[i][KONTIERUNG_FELD_LIEFERANTENRECHNUNGSNUMMER] = item.getSLieferantenrechnungsnummer();
				if (item.getDFreigabedatum() == null) {
					cKontierung = session.createCriteria(FLREingangsrechnungKontierung.class);

					if (kostenstelleIId != null) {
						cKontierung.add(Restrictions.eq(EingangsrechnungFac.FLR_KONTIERUNG_FLRKOSTENSTELLE + ".i_id",
								kostenstelleIId));
					}

					// Filter nach ER-status
					cStati = new TreeSet<String>();
					if (iFilterER == EingangsrechnungReportFac.REPORT_KONTIERUNG_FILTER_ER_ALLE) {
						cStati.add(EingangsrechnungFac.STATUS_ANGELEGT);
						cStati.add(EingangsrechnungFac.STATUS_ERLEDIGT);
						cStati.add(EingangsrechnungFac.STATUS_TEILBEZAHLT);
						// cStati.add(EingangsrechnungFac.STATUS_VERBUCHT);
					} else if (iFilterER == EingangsrechnungReportFac.REPORT_KONTIERUNG_FILTER_ER_BEZAHLT) {
						cStati.add(EingangsrechnungFac.STATUS_ERLEDIGT);
					} else if (iFilterER == EingangsrechnungReportFac.REPORT_KONTIERUNG_FILTER_ER_OFFENE) {
						cStati.add(EingangsrechnungFac.STATUS_ANGELEGT);
						cStati.add(EingangsrechnungFac.STATUS_TEILBEZAHLT);
						// cStati.add(EingangsrechnungFac.STATUS_VERBUCHT);
					}
					mandantCNr = theClientDto.getMandant();

					if (iKritDatum == EingangsrechnungReportFac.REPORT_KONTIERUNG_KRIT_DATUM_BELEG) {
						cKontierung.createCriteria(EingangsrechnungFac.FLR_KONTIERUNG_FLREINGANGSRECHNUNG)
								.add(Restrictions.eq(EingangsrechnungFac.FLR_ER_MANDANT_C_NR, mandantCNr))
								.add(Restrictions.ge(EingangsrechnungFac.FLR_ER_D_BELEGDATUM, dVon))
								.add(Restrictions.le(EingangsrechnungFac.FLR_ER_D_BELEGDATUM, dBis))
								.add(Restrictions.in(EingangsrechnungFac.FLR_ER_STATUS_C_NR, cStati));
					} else if (iKritDatum == EingangsrechnungReportFac.REPORT_KONTIERUNG_KRIT_DATUM_FREIGABE) {
						cKontierung.createCriteria(EingangsrechnungFac.FLR_KONTIERUNG_FLREINGANGSRECHNUNG)
								.add(Restrictions.eq(EingangsrechnungFac.FLR_ER_MANDANT_C_NR, mandantCNr))
								.add(Restrictions.ge(EingangsrechnungFac.FLR_ER_D_FREIGABEDATUM, dVon))
								.add(Restrictions.le(EingangsrechnungFac.FLR_ER_D_FREIGABEDATUM, dBis))
								.add(Restrictions.in(EingangsrechnungFac.FLR_ER_STATUS_C_NR, cStati));
					}
					// Sortierung noch kostenstelle, konto
					cKontierung.createCriteria(EingangsrechnungFac.FLR_KONTIERUNG_FLRKOSTENSTELLE)
							.addOrder(Order.asc("c_nr"));
					cKontierung.createCriteria(EingangsrechnungFac.FLR_KONTIERUNG_FLRKONTO).addOrder(Order.asc("c_nr"));

					listKontierung = cKontierung.list();
					coll = new ArrayList<ReportEingangsrechnungKontierungsjournalDto>();
					for (Iterator<?> iter1 = listKontierung.iterator(); iter1.hasNext();) {
						FLREingangsrechnungKontierung item1 = (FLREingangsrechnungKontierung) iter1.next();
						EingangsrechnungDto erDto = getEingangsrechnungFac()
								.eingangsrechnungFindByPrimaryKey(item1.getEingangsrechnung_i_id());
						ReportEingangsrechnungKontierungsjournalDto dto = new ReportEingangsrechnungKontierungsjournalDto();

						BigDecimal bdBezahlt = getEingangsrechnungFac().getBezahltBetrag(erDto.getIId(),
								item1.getI_id());
						dto.setBdBezahlt(bdBezahlt);
						// Bezahlt Wert mit Kurs der ER in Mandantenwaehrung
						BigDecimal bdBezahltERKurs = getEingangsrechnungFac().getBezahltBetragFw(erDto.getIId(),
								item1.getI_id());
						if (bdBezahltERKurs != null) {
							dto.setBdBezahltFW(new BigDecimal(bdBezahltERKurs.doubleValue()));
							bdBezahltERKurs = bdBezahltERKurs.multiply(erDto.getNKurs());
						}
						dto.setBdBezahltzuERKurs(bdBezahltERKurs);
						// Letztes Zahldatum
						EingangsrechnungzahlungDto letzteZahlung = getEingangsrechnungFac()
								.getLetzteZahlung(erDto.getIId());
						Date dLetztesZahldatum = null;
						if (letzteZahlung != null) {
							dLetztesZahldatum = new Date(letzteZahlung.getTZahldatum().getTime());
						}
						dto.setDLetzesZahldatum(dLetztesZahldatum);
						// Errechneter Steuersatz

						if (erDto.getNBetrag().subtract(erDto.getNUstBetrag()).doubleValue() != 0) {
							BigDecimal bdErrechneterSteuersatz = erDto.getNUstBetrag()
									.divide(erDto.getNBetrag().subtract(erDto.getNUstBetrag()), 4,
											BigDecimal.ROUND_HALF_EVEN)
									.movePointRight(2);
							dto.setBdErrechneterSteuersatz(bdErrechneterSteuersatz);
						} else {
							dto.setBdErrechneterSteuersatz(BigDecimal.ZERO);
						}

						dto.setBdUst(item1.getN_betrag_ust().multiply(erDto.getNKurs())
								.setScale(FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN));
						dto.setBdWert(item1.getN_betrag().multiply(erDto.getNKurs())
								.setScale(FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN));

						dto.setBdUstFW(item1.getN_betrag_ust().setScale(FinanzFac.NACHKOMMASTELLEN,
								BigDecimal.ROUND_HALF_EVEN));
						dto.setBdWertFW(
								item1.getN_betrag().setScale(FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN));

						dto.setWaehrungCNr(erDto.getWaehrungCNr());

						dto.setDEingangsrechnungsdatum(item1.getFlreingangsrechnung().getT_belegdatum());
						dto.setSEingangsrechnungsnummer(item1.getFlreingangsrechnung().getC_nr());

						dto.setBMitPositionen(
								Helper.short2boolean(item1.getFlreingangsrechnung().getB_mitpositionen()));

						dto.setSArt(item1.getFlreingangsrechnung().getEingangsrechnungart_c_nr());
						dto.setSEingangsrechnungText(erDto.getCText());
						dto.setSEingangsrechnungWeartikel(erDto.getCWeartikel());
						dto.setSKontobezeichnung(item1.getFlrkonto().getC_bez());
						dto.setSKontonummer(item1.getFlrkonto().getC_nr());
						dto.setSKostenstelleBezeichnung(item1.getFlrkostenstelle().getC_bez());
						dto.setSKostenstellenummer(item1.getFlrkostenstelle().getC_nr());
						dto.setSLieferant(item1.getFlreingangsrechnung().getFlrlieferant().getFlrpartner()
								.getC_name1nachnamefirmazeile1());
						if (item1.getFlrkonto().getFlrkontoart() != null) {
							dto.setSKontoart(getFinanzServiceFac().uebersetzeKontoartOptimal(
									item1.getFlrkonto().getFlrkontoart().getC_nr(), theClientDto.getLocUi(),
									theClientDto.getLocMandant()));
						}

						if (erDto.getDFreigabedatum() != null) {
							item.setDFreigabedatum(erDto.getDFreigabedatum());
						}

					}
				}
				data[i][KONTIERUNG_FELD_ER_FREIGABEDATUM] = item.getDFreigabedatum();
				data[i][KONTIERUNG_FELD_WERT_BEZAHLT_ERKURS] = item.getBdBezahltzuERKurs();
				data[i][KONTIERUNG_FELD_KREDITORENNUMMER] = item.getSKreditorennummer();
				
				data[i][KONTIERUNG_FELD_GEPRUEFT_VON] = item.getSGeprueftVon();
				data[i][KONTIERUNG_FELD_GEPRUEFT_AM] = item.getTGeprueftAm();
				
			}
			// Datumsbereich
			StringBuffer sDatum = new StringBuffer();
			if (iKritDatum == EingangsrechnungReportFac.REPORT_KONTIERUNG_KRIT_DATUM_BELEG) {
				sDatum.append("Belegdatum ");
			} else if (iKritDatum == EingangsrechnungReportFac.REPORT_KONTIERUNG_KRIT_DATUM_FREIGABE) {
				sDatum.append("Freigabedatum ");
			}
			if (dVon != null) {
				sDatum.append("von " + Helper.formatDatum(dVon, theClientDto.getLocUi()));
			}
			if (dBis != null) {
				sDatum.append(" bis " + Helper.formatDatum(dBis, theClientDto.getLocUi()));
			}
			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			mapParameter.put("P_DATUM", sDatum.toString());
			mapParameter.put("P_ZUSATZKOSTEN", new Boolean(bZusatzkosten));
			// Filter
			StringBuffer sFilter = new StringBuffer();
			if (iFilterER == EingangsrechnungReportFac.REPORT_KONTIERUNG_FILTER_ER_ALLE) {
				sFilter.append("Alle Eingangsrechnungen");
			} else if (iFilterER == EingangsrechnungReportFac.REPORT_KONTIERUNG_FILTER_ER_BEZAHLT) {
				sFilter.append("Bezahlte Eingangsrechnungen");
			} else if (iFilterER == EingangsrechnungReportFac.REPORT_KONTIERUNG_FILTER_ER_OFFENE) {
				sFilter.append("Offene Eingangsrechnungen");
			}
			sFilter.append(", ");
			if (kostenstelleIId == null) {
				sFilter.append("Alle Kostenstellen");
			} else {
				sFilter.append("Eine Kostenstelle");
			}
			mapParameter.put(LPReport.P_FILTER, sFilter.toString());

			
			ParametermandantDto parameterERPRuefen = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_EINGANGSRECHNUNG, ParameterFac.PARAMETER_EINGANGSRECHNUNG_PRUEFEN);

			Integer iERPruefen = (Integer) parameterERPRuefen.getCWertAsObject();

			mapParameter.put("P_EINGANGSRECHNUNG_PRUEFEN", iERPruefen);
			
			initJRDS(mapParameter, EingangsrechnungReportFac.REPORT_MODUL,
					EingangsrechnungReportFac.REPORT_EINGANGSRECHNUNG_KONTIERUNG, theClientDto.getMandant(),
					theClientDto.getLocUi(), theClientDto);
			return getReportPrint();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} finally {
			closeSession(session);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
//	public JasperPrintLP printOffene(TheClientDto theClientDto, int iSort, Integer lieferantIId, Date dStichtag,
//			boolean bStichtagIstFreigabeDatum, boolean bZusatzkosten, boolean mitNichtZugeordnetenBelegen) {
	public JasperPrintLP printOffene(EingangsrechnungOffeneKriterienDto krit, TheClientDto theClientDto) {
		this.useCase = UC_OFFENE;

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		Map<String, Object> mapParameter = new TreeMap<String, Object>();
		mapParameter.put("P_DATUM", krit.dStichtag);

		try {

			ParametermandantDto parameterERPRuefen = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_EINGANGSRECHNUNG, ParameterFac.PARAMETER_EINGANGSRECHNUNG_PRUEFEN);

			Integer iERPruefen = (Integer) parameterERPRuefen.getCWertAsObject();

			mapParameter.put("P_EINGANGSRECHNUNG_PRUEFEN", iERPruefen);

			Map<Integer, ZahlungszielDto> mZahlungsziel = getMandantFac()
					.zahlungszielFindAllByMandantAsDto(theClientDto.getMandant(), theClientDto);

			session = factory.openSession();

			List<Integer> lieferantenIIds = new ArrayList<Integer>();
			if (krit.lieferantIId != null) {
				lieferantenIIds.add(krit.lieferantIId);
			} else if (krit.sort == EingangsrechnungReportFac.REPORT_OFFENE_SORT_LIEFERANT) {
				Iterator<?> iter = session.createCriteria(FLRLieferant.class).createAlias("flrpartner", "p")
						.addOrder(Order.asc("p.c_name1nachnamefirmazeile1"))
						.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()))

						.list().iterator();
				while (iter.hasNext()) {
					lieferantenIIds.add(((FLRLieferant) iter.next()).getI_id());
				}
			} else {
				lieferantenIIds.add(null);
			}

			Set<Integer> schonGedruckteKonten = new HashSet<Integer>();
			List<Object[]> dataList = new ArrayList<Object[]>();
			
			// TODO: dStichtag kann null sein, PJ22556 ghp
			Integer gj = getSystemFac().getAeltestesOffenesGeschaeftsjahr(
					theClientDto.getMandant(), krit.dStichtag);
					
			for (Integer liefIId : lieferantenIIds) {
				Criteria crit = session.createCriteria(FLREingangsrechnungReport.class);
				// Filter nach Mandant
				crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));

				Collection<String> cStati = new LinkedList<String>();
				cStati.add(EingangsrechnungFac.STATUS_TEILBEZAHLT);
				cStati.add(EingangsrechnungFac.STATUS_ANGELEGT);
				cStati.add(EingangsrechnungFac.STATUS_ERLEDIGT);
				crit.add(Restrictions.in("status_c_nr", cStati));

				if (krit.zusatzkosten) {
					crit.add(Restrictions.eq(EingangsrechnungFac.FLR_ER_EINGANGSRECHNUNGART_C_NR,
							EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN));
				} else {
					crit.add(Restrictions.not(Restrictions.eq(EingangsrechnungFac.FLR_ER_EINGANGSRECHNUNGART_C_NR,
							EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN)));
				}

				if (liefIId != null) {
					crit.add(Restrictions.eq("lieferant_i_id", liefIId));
				}

				if (krit.dStichtag != null) {
					if (krit.stichtagIsFreigabedatum) {
						crit.add(Restrictions.le("t_freigabedatum", krit.dStichtag));
					} else {
						crit.add(Restrictions.le("t_belegdatum", krit.dStichtag));
					}
					crit.add(Restrictions.or(Restrictions.gt("t_bezahltdatum", krit.dStichtag),
							Restrictions.isNull("t_bezahltdatum")));
					crit.add(Restrictions.or(Restrictions.gt("t_manuellerledigt", krit.dStichtag),
							Restrictions.isNull("t_manuellerledigt")));
				} else {
					crit.add(Restrictions.isNull("t_bezahltdatum"));
					crit.add(Restrictions.isNull("t_manuellerledigt"));
					krit.dStichtag = getDate();
				}

				if (krit.sort == EingangsrechnungReportFac.REPORT_OFFENE_SORT_RECHNUNGSNUMMER) {
					crit.addOrder(Order.asc("c_nr"));
				} else if (krit.sort == EingangsrechnungReportFac.REPORT_OFFENE_SORT_LIEFERANT) {
					crit.addOrder(Order.asc("c_nr"));
				} else if (krit.sort  == EingangsrechnungReportFac.REPORT_OFFENE_SORT_FAELLIGKEIT) {

					// Innerhalb der Faelligkeit nach Rechnungsnummer sortieren
					if (krit.stichtagIsFreigabedatum) {
						crit.addOrder(Order.asc("t_faelligkeit"))
							.addOrder(Order.asc("c_nr"));
					} else {
						crit.addOrder(Order.asc("t_faelligkeit_belegdatum"))
							.addOrder(Order.asc("c_nr"));
					}

				} else if (krit.sort == EingangsrechnungReportFac.REPORT_OFFENE_SORT_FAELLIGKEIT_SKONTO1) {
					crit.addOrder(Order.asc("t_faelligkeit_skonto1"))
						.addOrder(Order.asc("c_nr"));
				} else if (krit.sort == EingangsrechnungReportFac.REPORT_OFFENE_SORT_FAELLIGKEIT_SKONTO2) {
					crit.addOrder(Order.asc("t_faelligkeit_skonto2"))
						.addOrder(Order.asc("c_nr"));
				}
				
				if (krit.sort != EingangsrechnungReportFac.REPORT_OFFENE_SORT_LIEFERANT) {
					krit.mitNichtZugeordnetenBelegen = false;
				}
				List<?> resultList = crit.list();

				Iterator<?> resultListIterator = resultList.iterator();
				int row = 0;

				Object[][] tempData = new Object[resultList.size()][OFFENE_ANZAHL_FELDER];

				if (krit.mitNichtZugeordnetenBelegen) {
					LieferantDto liefDto = getLieferantFac().lieferantFindByPrimaryKey(liefIId, theClientDto);

					if (liefDto.getKontoIIdKreditorenkonto() != null
							&& !schonGedruckteKonten.contains(liefDto.getKontoIIdKreditorenkonto())) {

						schonGedruckteKonten.add(liefDto.getKontoIIdKreditorenkonto());
						// TODO: nur FLRFinanzBuchungDetail holen
						Query query = session.createQuery(
								"SELECT buchungdetail from FLRFinanzBuchungDetail buchungdetail LEFT OUTER JOIN buchungdetail.flrbuchung AS buchung"
										+ " WHERE"
										+ BuchungDetailQueryBuilder.buildNurOffeneBuchungDetails("buchungdetail",
												new Timestamp(krit.dStichtag.getTime()), gj)
										+ "AND"
										+ BuchungDetailQueryBuilder.buildNichtZuordenbareVonKonto("buchungdetail",
												"buchung", liefDto.getKontoIIdKreditorenkonto(), gj)
										+ (krit.dStichtag == null ? ""
												: ("AND buchung.d_buchungsdatum<='"
														+ Helper.formatDateWithSlashes(krit.dStichtag) + "'")));

						@SuppressWarnings("unchecked")
						List<FLRFinanzBuchungDetail> bdList = query.list();
						if (bdList.size() > 0) {
							if (tempData.length < 1) {
								tempData = new Object[1][OFFENE_ANZAHL_FELDER];
								String sFirma = liefDto.getPartnerDto().formatFixTitelName1Name2();
								tempData[0][OFFENE_FELD_FIRMA] = sFirma;
								tempData[0][OFFENE_FELD_KREDITORENNR] = getFinanzFac()
										.kontoFindByPrimaryKeySmall(liefDto.getKontoIIdKreditorenkonto()).getCNr();
							}
							tempData[0][OFFENE_SUBREPORT_OFFENE_BUCHUNGEN] = FinanzSubreportGenerator
									.createBuchungsdetailSubreport(bdList, false);
						}
					}

				}
				while (resultListIterator.hasNext()) {
					FLREingangsrechnungReport er = (FLREingangsrechnungReport) resultListIterator.next();
					EingangsrechnungDto erDto = getEingangsrechnungFac().eingangsrechnungFindByPrimaryKey(er.getI_id());
					LieferantDto liefDto = getLieferantFac().lieferantFindByPrimaryKey(erDto.getLieferantIId(),
							theClientDto);

					String sErCNr = erDto.getCNr();
					String sFirma = liefDto.getPartnerDto().formatFixTitelName1Name2();
					ZahlungszielDto zzDto = mZahlungsziel.get(erDto.getZahlungszielIId());
					tempData[row][OFFENE_FELD_ER_C_NR] = sErCNr;
					tempData[row][OFFENE_FELD_ART] = erDto.getEingangsrechnungartCNr();

					tempData[row][OFFENE_FELD_MIT_POSITIONEN] = Helper.short2Boolean(erDto.getBMitpositionen());
					tempData[row][OFFENE_FELD_FIRMA] = sFirma;
					tempData[row][OFFENE_FELD_ERDATUM] = er.getT_belegdatum();
					tempData[row][OFFENE_FELD_MAHNDATUM] = erDto.getTMahndatum();
					tempData[row][OFFENE_FELD_ART] = er.getEingangsrechnungart_c_nr();
					tempData[row][OFFENE_FELD_KREDITORENNR] = liefDto.getKontoIIdKreditorenkonto() != null
							? getFinanzFac().kontoFindByPrimaryKeySmall(liefDto.getKontoIIdKreditorenkonto()).getCNr()
							: null;
					tempData[row][OFFENE_FELD_MAHNSTUFE] = er.getMahnstufe_i_id();
					tempData[row][OFFENE_FELD_FREIGABEDATUM] = er.getT_freigabedatum();
					tempData[row][OFFENE_FELD_WE_ARTIKEL] = er.getC_weartikel();
					tempData[row][OFFENE_FELD_WERT] = erDto.getNBetrag();
					tempData[row][OFFENE_FELD_LIEFERANTENRECHNUNGSNUMMER] = erDto.getCLieferantenrechnungsnummer();
					tempData[row][OFFENE_FELD_TEXT] = erDto.getCText();
					if (zzDto != null) {
						tempData[row][OFFENE_FELD_ZAHLUNGSZIEL] = zzDto.getCBez();
						tempData[row][OFFENE_FELD_SKONTOTAGE1] = zzDto.getSkontoAnzahlTage1();
						tempData[row][OFFENE_FELD_SKONTOTAGE2] = zzDto.getSkontoAnzahlTage2();
						tempData[row][OFFENE_FELD_SKONTOPROZENT1] = zzDto.getSkontoProzentsatz1();
						tempData[row][OFFENE_FELD_SKONTOPROZENT2] = zzDto.getSkontoProzentsatz2();
						tempData[row][OFFENE_FELD_NETTOTAGE] = zzDto.getAnzahlZieltageFuerNetto();
						if (erDto.getDFreigabedatum() != null) {
							if (zzDto.getAnzahlZieltageFuerNetto() != null) {

								if (krit.stichtagIsFreigabedatum) {
									tempData[row][OFFENE_FELD_FAELLIGKEIT] = er.getT_faelligkeit();
								} else {
									tempData[row][OFFENE_FELD_FAELLIGKEIT] = er.getT_faelligkeit_belegdatum();
								}
							}
							
							if (zzDto.getSkontoAnzahlTage1() != null) {

								if (krit.stichtagIsFreigabedatum) {
									tempData[row][OFFENE_FELD_FAELLIGKEIT_SKONTO1] = er.getT_faelligkeit_skonto1();
								} else {
									tempData[row][OFFENE_FELD_FAELLIGKEIT_SKONTO1] = er
											.getT_faelligkeit_skonto1_belegdatum();
								}

							} else {
								tempData[row][OFFENE_FELD_FAELLIGKEIT_SKONTO1] = tempData[row][OFFENE_FELD_FAELLIGKEIT];
							}

							if (zzDto.getSkontoAnzahlTage2() != null) {

								if (krit.stichtagIsFreigabedatum) {
									tempData[row][OFFENE_FELD_FAELLIGKEIT_SKONTO2] = er.getT_faelligkeit_skonto2();
								} else {
									tempData[row][OFFENE_FELD_FAELLIGKEIT_SKONTO2] = er
											.getT_faelligkeit_skonto2_belegdatum();
								}

							} else {

								if (zzDto.getSkontoAnzahlTage1() != null) {
									tempData[row][OFFENE_FELD_FAELLIGKEIT_SKONTO2] = tempData[row][OFFENE_FELD_FAELLIGKEIT_SKONTO1];
								} else {
									tempData[row][OFFENE_FELD_FAELLIGKEIT_SKONTO2] = tempData[row][OFFENE_FELD_FAELLIGKEIT];
								}

							}
						}
					}

					// datum der letzten zahlung bis zum stichtag ermitteln
					EingangsrechnungzahlungDto[] zahlungen = getEingangsrechnungFac()
							.eingangsrechnungzahlungFindByEingangsrechnungIId(er.getI_id());
					java.sql.Date dZahldatum = null;
					for (int i = 0; i < zahlungen.length; i++) {
						if ((dZahldatum == null || zahlungen[i].getTZahldatum().after(dZahldatum))
								&& !zahlungen[i].getTZahldatum().after(krit.dStichtag)) {
							dZahldatum = new Date(zahlungen[i].getTZahldatum().getTime());
						}
					}
					tempData[row][OFFENE_FELD_ZAHLDATUM] = dZahldatum;
					// Zahlungsbetrag bis zum Stichtag ermitteln
					BigDecimal bdBezahltFw = new BigDecimal(0);
					BigDecimal bdBezahltKursBelegdatum = new BigDecimal(0);
					for (int i = 0; i < zahlungen.length; i++) {
						if (!zahlungen[i].getTZahldatum().after(krit.dStichtag)) {
							bdBezahltFw = bdBezahltFw.add(zahlungen[i].getNBetragfw());
							bdBezahltKursBelegdatum = bdBezahltKursBelegdatum
									.add(zahlungen[i].getNBetragfw().multiply(erDto.getNKurs()));
						}
					}

					// anzahlungen dazusummieren
					if (er.getEingangsrechnungart_c_nr()
							.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_SCHLUSSZAHLUNG)) {
						BigDecimal bezahltAnzahlungenFw = getEingangsrechnungFac()
								.getAnzahlungenBezahltZuSchlussrechnungFw(er.getI_id());
						bdBezahltFw = bdBezahltFw.add(bezahltAnzahlungenFw);
						bdBezahltKursBelegdatum = bdBezahltKursBelegdatum
								.add(bezahltAnzahlungenFw.multiply(erDto.getNKurs()));
					}

					tempData[row][OFFENE_FELD_BETRAG] = getLocaleFac().rechneUmInAndereWaehrungZuDatum(bdBezahltFw,
							erDto.getWaehrungCNr(), theClientDto.getSMandantenwaehrung(),
							krit.dStichtag, theClientDto);
					tempData[row][OFFENE_FELD_BETRAG_KURS_BELEGDATUM] = bdBezahltKursBelegdatum;
					tempData[row][OFFENE_FELD_OFFEN] = getLocaleFac().rechneUmInAndereWaehrungZuDatum(
							erDto.getNBetragfw().subtract(bdBezahltFw), erDto.getWaehrungCNr(),
							theClientDto.getSMandantenwaehrung(),
							krit.dStichtag, theClientDto);
					tempData[row][OFFENE_FELD_OFFEN_KURS_BELEGDATUM] = erDto.getNBetrag()
							.subtract(bdBezahltKursBelegdatum);

					tempData[row][OFFENE_FELD_BETRAG_FW] = bdBezahltFw;
					tempData[row][OFFENE_FELD_OFFEN_FW] = erDto.getNBetragfw().subtract(bdBezahltFw);
					tempData[row][OFFENE_FELD_ERWAEHRUNG] = erDto.getWaehrungCNr();
					tempData[row][OFFENE_FELD_WERT_FW] = erDto.getNBetragfw();
					tempData[row][OFFENE_FELD_ERKURS] = erDto.getNKurs();
					WechselkursDto wkDto = getLocaleFac().getKursZuDatum(erDto.getWaehrungCNr(),
							theClientDto.getSMandantenwaehrung(), krit.dStichtag, theClientDto);
					tempData[row][OFFENE_FELD_KURS_STICHTAG] = wkDto.getNKurs()
							.setScale(LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS, RoundingMode.HALF_EVEN);

					// PJ22118
					ErZahlungsempfaenger empfaenger = getEingangsrechnungFac()
							.getErZahlungsempfaenger(new EingangsrechnungId(erDto.getIId()), theClientDto);

					if (empfaenger.exists()) {
						tempData[row][OFFENE_FELD_BANKVERBINDUNG_BANKNAME] = empfaenger.getBankDto().getPartnerDto()
								.getCName1nachnamefirmazeile1();
						tempData[row][OFFENE_FELD_BANKVERBINDUNG_IBAN] = empfaenger.getPartnerbankDto().getCIban();
						tempData[row][OFFENE_FELD_BANKVERBINDUNG_BIC] = empfaenger.getBankDto().getCBic();
						if (empfaenger.isAbweichend()) {
							tempData[row][OFFENE_FELD_BANKVERBINDUNG_PERSON_ABWEICHEND] = empfaenger.getPartnerDto()
									.formatFixName2Name1();
						}
					}

					
					if (er.getFlrpersonal_geprueft() != null) {
						tempData[row][OFFENE_FELD_GEPRUEFT_AM] = er.getT_geprueft();
						tempData[row][OFFENE_FELD_GEPRUEFT_VON] = er.getFlrpersonal_geprueft().getC_kurzzeichen();
					}

					
					row++;
				}
				dataList.addAll(Arrays.asList(tempData));
			}

			data = dataList.toArray(new Object[0][]);

			if (krit.sort == EingangsrechnungReportFac.REPORT_OFFENE_SORT_FAELLIGKEIT_SKONTO1
					|| krit.sort == EingangsrechnungReportFac.REPORT_OFFENE_SORT_FAELLIGKEIT_SKONTO2) {
				for (int i = data.length - 1; i > 0; --i) {
					for (int j = 0; j < i; ++j) {
						Object[] o = data[j];
						Object[] o1 = data[j + 1];

						java.util.Date d1 = (java.util.Date) o[OFFENE_FELD_FAELLIGKEIT];
						java.util.Date d2 = (java.util.Date) o1[OFFENE_FELD_FAELLIGKEIT];

						if (krit.sort == EingangsrechnungReportFac.REPORT_OFFENE_SORT_FAELLIGKEIT_SKONTO1) {
							if (o[OFFENE_FELD_FAELLIGKEIT_SKONTO1] != null) {
								d1 = (java.util.Date) o[OFFENE_FELD_FAELLIGKEIT_SKONTO1];
							}
							if (o1[OFFENE_FELD_FAELLIGKEIT_SKONTO1] != null) {
								d2 = (java.util.Date) o1[OFFENE_FELD_FAELLIGKEIT_SKONTO1];
							}
						}

						if (krit.sort == EingangsrechnungReportFac.REPORT_OFFENE_SORT_FAELLIGKEIT_SKONTO2) {
							if (o[OFFENE_FELD_FAELLIGKEIT_SKONTO2] != null) {
								d1 = (java.util.Date) o[OFFENE_FELD_FAELLIGKEIT_SKONTO2];
							}
							if (o1[OFFENE_FELD_FAELLIGKEIT_SKONTO2] != null) {
								d2 = (java.util.Date) o1[OFFENE_FELD_FAELLIGKEIT_SKONTO2];
							}
						}

						if (d1.after(d2)) {
							data[j] = o1;
							data[j + 1] = o;
						}
					}
				}
			}

			mapParameter.put("P_STICHTAGISTFREIGABEDATUM", new Boolean(krit.stichtagIsFreigabedatum));
			mapParameter.put("P_ZUSATZKOSTEN", new Boolean(krit.zusatzkosten));
			String sSortierung = null;
			if (krit.sort == EingangsrechnungReportFac.REPORT_OFFENE_SORT_LIEFERANT) {
				sSortierung = "Lieferant";
			} else if (krit.sort  == EingangsrechnungReportFac.REPORT_OFFENE_SORT_RECHNUNGSNUMMER) {
				sSortierung = "Rechnungsnummer";
			} else if (krit.sort == EingangsrechnungReportFac.REPORT_OFFENE_SORT_FAELLIGKEIT) {
				sSortierung = getTextRespectUISpr("lp.faelligkeit", theClientDto.getMandant(), theClientDto.getLocUi());
			} else if (krit.sort == EingangsrechnungReportFac.REPORT_OFFENE_SORT_FAELLIGKEIT_SKONTO1) {
				sSortierung = getTextRespectUISpr("er.faelligkeitskonto1", theClientDto.getMandant(),
						theClientDto.getLocUi());
			} else if (krit.sort == EingangsrechnungReportFac.REPORT_OFFENE_SORT_FAELLIGKEIT_SKONTO2) {
				sSortierung = getTextRespectUISpr("er.faelligkeitskonto2", theClientDto.getMandant(),
						theClientDto.getLocUi());
			}
			mapParameter.put("P_SORTIERUNGNACHLIEFERANT",
					new Boolean(krit.sort == EingangsrechnungReportFac.REPORT_OFFENE_SORT_LIEFERANT));
			mapParameter.put(LPReport.P_SORTIERUNG, sSortierung);

			String sZessionstext = null;
			sZessionstext = getParameterFac().parametermandantFindByPrimaryKey(ParameterFac.PARAMETER_ZESSIONSTEXT,
					ParameterFac.KATEGORIE_ALLGEMEIN, theClientDto.getMandant()).getCWert();
			if (sZessionstext != null) {
				mapParameter.put("P_ZESSIONSTEXT", sZessionstext);
			}
			mapParameter.put("P_MANDANTENWAEHRUNG", theClientDto.getSMandantenwaehrung());
			mapParameter.put(P_KPI_VARIABLEN, krit.getKpiReportStorage());
			initJRDS(mapParameter, EingangsrechnungReportFac.REPORT_MODUL,
					EingangsrechnungReportFac.REPORT_EINGANGSRECHNUNG_OFFENE, theClientDto.getMandant(),
					theClientDto.getLocUi(), theClientDto);
			return getReportPrint();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} finally {
			closeSession(session);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAlle(ReportJournalKriterienDto krit, TheClientDto theClientDto, boolean bZusatzkosten,
			boolean bDatumIstFreigabedatum) {
		Session session = null;
		try {
			this.useCase = UC_ALLE;

			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLREingangsrechnungReport.class);
			c.add(Restrictions.eq(EingangsrechnungFac.FLR_ER_MANDANT_C_NR, theClientDto.getMandant()));

			if (krit.lieferantIId != null) {
				c.add(Restrictions.eq(EingangsrechnungFac.FLR_ER_LIEFERANT_I_ID, krit.lieferantIId));
			}
			String sVon = null;
			String sBis = null;
			if (krit.dVon != null) {
				if (bDatumIstFreigabedatum) {
					c.add(Restrictions.ge(EingangsrechnungFac.FLR_ER_D_FREIGABEDATUM, krit.dVon));
				} else {
					c.add(Restrictions.ge(EingangsrechnungFac.FLR_ER_D_BELEGDATUM, krit.dVon));
				}
				sVon = Helper.formatDatum(krit.dVon, theClientDto.getLocUi());
			}
			if (krit.dBis != null) {
				if (bDatumIstFreigabedatum) {
					c.add(Restrictions.le(EingangsrechnungFac.FLR_ER_D_FREIGABEDATUM, krit.dBis));
				} else {
					c.add(Restrictions.le(EingangsrechnungFac.FLR_ER_D_BELEGDATUM, krit.dBis));
				}
				sBis = Helper.formatDatum(krit.dBis, theClientDto.getLocUi());
			}

			if (bZusatzkosten) {
				c.add(Restrictions.eq(EingangsrechnungFac.FLR_ER_EINGANGSRECHNUNGART_C_NR,
						EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN));
			} else {
				c.add(Restrictions.not(Restrictions.eq(EingangsrechnungFac.FLR_ER_EINGANGSRECHNUNGART_C_NR,
						EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN)));
			}

			LpBelegnummerFormat f = getBelegnummerGeneratorObj().getBelegnummernFormat(theClientDto.getMandant());
			Integer iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(theClientDto.getMandant());
			String sMandantKuerzel = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_BELEGNUMMER_MANDANTKENNUNG).getCWert();
			if (krit.sBelegnummerVon != null) {
				sVon = HelperServer.getBelegnummernFilterForHibernateCriterias(f, iGeschaeftsjahr, sMandantKuerzel,
						krit.sBelegnummerVon);
				c.add(Restrictions.ge(EingangsrechnungFac.FLR_ER_C_NR, sVon));
			}
			if (krit.sBelegnummerBis != null) {
				sBis = HelperServer.getBelegnummernFilterForHibernateCriterias(f, iGeschaeftsjahr, sMandantKuerzel,
						krit.sBelegnummerBis);
				c.add(Restrictions.le(EingangsrechnungFac.FLR_ER_C_NR, sBis));
			}
			if (krit.bSortiereNachKostenstelle) {
				c.createAlias(EingangsrechnungFac.FLR_ER_FLRKOSTENSTELLE, "a", CriteriaSpecification.LEFT_JOIN)
						.addOrder(Order.asc("a.c_nr"));
			}
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				c.createCriteria(EingangsrechnungFac.FLR_ER_FLRLIEFERANT).createCriteria(LieferantFac.FLR_PARTNER)
						.addOrder(Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
				c.addOrder(Order.asc(EingangsrechnungFac.FLR_ER_C_NR));
			} else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_BELEGNUMMER) {
				c.addOrder(Order.asc(EingangsrechnungFac.FLR_ER_C_NR));
			} else {
				c.addOrder(Order.asc(EingangsrechnungFac.FLR_ER_C_NR));
			}

			List<?> list = c.list();

			ArrayList alDaten = new ArrayList();

			int i = 0;
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLREingangsrechnungReport e = (FLREingangsrechnungReport) iter.next();

				if (krit.kostenstelleIId != null) {

					boolean bMehrfach = e.getFlrkonto() == null || e.getKostenstelle_i_id() == null;

					if (bMehrfach == false && !e.getKostenstelle_i_id().equals(krit.kostenstelleIId)) {
						continue;
					}

				}

				EingangsrechnungDto erDto = getEingangsrechnungFac().eingangsrechnungFindByPrimaryKey(e.getI_id());

				Object[] zeile = new Object[ALLE_ANZAHL_FELDER];

				zeile[FELD_ALLE_AUSZUG] = getEingangsrechnungFac().getAuszugDerLetztenZahlung(e.getI_id());
				BankverbindungDto bvDto = getEingangsrechnungFac().getZuletztVerwendeteBankverbindung(e.getI_id());
				if (bvDto != null) {
					BankDto bankDto = getBankFac().bankFindByPrimaryKey(bvDto.getBankIId(), theClientDto);
					zeile[FELD_ALLE_BANK] = bankDto.getPartnerDto().getCName1nachnamefirmazeile1();
				}
				zeile[FELD_ALLE_BELEGDATUM] = e.getT_belegdatum();
				zeile[FELD_ALLE_BEZAHLTDATUM] = e.getT_bezahltdatum();
				zeile[FELD_ALLE_EINGANGSRECHNUNGSNUMMER] = e.getC_nr();
				zeile[FELD_ALLE_LIEFERANTENRECHNUNGSNUMMER] = e.getC_lieferantenrechnungsnummer();
				zeile[FELD_ALLE_STATUS] = e.getStatus_c_nr();
				zeile[FELD_ALLE_FREIGABEDATUM] = e.getT_freigabedatum();
				zeile[FELD_ALLE_KOSTENSTELLENUMMER] = e.getFlrkostenstelle() != null ? e.getFlrkostenstelle().getC_nr()
						: "";
				zeile[FELD_ALLE_LIEFERANT] = e.getFlrlieferant().getFlrpartner().getC_name1nachnamefirmazeile1();
				zeile[FELD_ALLE_TEXT] = e.getC_text();
				zeile[FELD_ALLE_WEARTIKEL] = e.getC_weartikel();
				zeile[FELD_ALLE_WERT] = e.getN_betrag();
				zeile[FELD_ALLE_KURS] = e.getN_kurs();
				zeile[FELD_ALLE_WAEHRUNG] = e.getWaehrung_c_nr();

				if (e.getFlrpersonal_geprueft() != null) {
					zeile[FELD_ALLE_GEPRUEFT_AM] = e.getT_geprueft();
					zeile[FELD_ALLE_GEPRUEFT_VON] = e.getFlrpersonal_geprueft().getC_kurzzeichen();
				}

				zeile[FELD_ALLE_ZOLLBELEGNUMMER] = e.getC_zollimportpapier();
				zeile[FELD_ALLE_WERTNETTO] = e.getN_betrag() != null ? e.getN_betrag().subtract(e.getN_ustbetrag())
						: null;
				zeile[FELD_ALLE_WERTUST] = e.getN_ustbetrag();

				zeile[FELD_ALLE_WERT_FW] = e.getN_betragfw();
				zeile[FELD_ALLE_WERTNETTO_FW] = e.getN_betragfw() != null
						? e.getN_betragfw().subtract(e.getN_ustbetragfw())
						: null;
				zeile[FELD_ALLE_WERTUST_FW] = e.getN_ustbetragfw();

				zeile[FELD_ALLE_ZIELDATUM] = getMandantFac().berechneZielDatumFuerBelegdatum(e.getT_freigabedatum(),
						e.getZahlungsziel_i_id(), theClientDto);
				zeile[FELD_ALLE_LAENDERART] = getFinanzServiceFac().getLaenderartZuPartner(
						e.getFlrlieferant().getFlrpartner().getI_id(), Helper.asTimestamp(e.getT_belegdatum()),
						theClientDto);

				zeile[FELD_ALLE_ZAHLBETRAG] = getEingangsrechnungFac().getBezahltBetrag(e.getI_id(), null);

				zeile[FELD_ALLE_ZAHLBETRAG_FW] = getEingangsrechnungFac().getBezahltBetragFw(e.getI_id(), null);

				if (e.getFlrkonto() != null) {
					zeile[FELD_ALLE_KONTO] = e.getFlrkonto().getC_nr();
				}

				EingangsrechnungKontierungDto[] kontierungDtos = getEingangsrechnungFac()
						.eingangsrechnungKontierungFindByEingangsrechnungIId(e.getI_id());

				if (kontierungDtos.length > 0) {
					String[] fieldnames = new String[] { "F_BETRAG", "F_USTBETRAG", "F_KOSTENSTELLE", "F_SACHKONTO",
							"F_UST" };

					BigDecimal gesamtUst = new BigDecimal(0);

					ArrayList alDatenSub = new ArrayList();

					for (int j = 0; j < kontierungDtos.length; j++) {

						if (krit.kostenstelleIId == null
								|| krit.kostenstelleIId.equals(kontierungDtos[j].getKostenstelleIId())) {

							Object[] zeileSub = new Object[5];
							zeileSub[0] = kontierungDtos[j].getNBetrag();
							zeileSub[1] = kontierungDtos[j].getNBetragUst();

							gesamtUst = gesamtUst.add(kontierungDtos[j].getNBetragUst());

							zeileSub[2] = getSystemFac()
									.kostenstelleFindByPrimaryKey(kontierungDtos[j].getKostenstelleIId()).getCNr();
							zeileSub[3] = getFinanzFac().kontoFindByPrimaryKeySmall(kontierungDtos[j].getKontoIId())
									.getCNr();
							zeileSub[4] = new BigDecimal(getMandantFac()
									.mwstsatzFindByPrimaryKey(kontierungDtos[j].getMwstsatzIId(), theClientDto)
									.getFMwstsatz());
							alDatenSub.add(zeileSub);
						}
					}

					if (alDatenSub.size() == 0) {
						continue;
					}

					Object[][] dataSub = new Object[alDatenSub.size()][5];
					dataSub = (Object[][]) alDatenSub.toArray(dataSub);

					zeile[FELD_ALLE_KONTIERUNG] = new LPDatenSubreport(dataSub, fieldnames);

					zeile[FELD_ALLE_WERTUST] = gesamtUst;

				} else {
					// zeile[FELD_ALLE_UST] =
					// getMwstSatzVonBruttoBetragUndUst(
					// e.getMandant_c_nr(), new Timestamp(e
					// .getT_belegdatum().getTime()),
					// e.getN_betrag(), e.getN_ustbetrag());

					zeile[FELD_ALLE_UST] = null;
					if (erDto.getMwstsatzIId() != null) {
						zeile[FELD_ALLE_UST] = new BigDecimal(getMandantFac()
								.mwstsatzFindByPrimaryKey(erDto.getMwstsatzIId(), theClientDto).getFMwstsatz());
					}
				}

				// Debitorenkontonummer
				KontoDtoSmall kontoDtoKred = getFinanzFac()
						.kontoFindByPrimaryKeySmallOhneExc(e.getFlrlieferant().getKonto_i_id_kreditorenkonto());

				String sKontonummer = kontoDtoKred != null ? kontoDtoKred.getCNr() : "";
				zeile[FELD_ALLE_KREDITORENKONTO] = sKontonummer;

				ReversechargeartDto rcartDto = getFinanzServiceFac()
						.reversechargeartFindByPrimaryKey(erDto.getReversechargeartId(), theClientDto);
				zeile[FELD_ALLE_REVERSE_CHARGE] = Helper
						.boolean2Short(!FinanzServiceFac.ReversechargeArt.OHNE.equals(rcartDto.getCNr()));
				zeile[FELD_ALLE_MIT_POSITIONEN] = Helper.short2Boolean(erDto.getBMitpositionen());
				zeile[FELD_ALLE_REVERSECHARGEART_I_ID] = erDto.getReversechargeartId();
				zeile[FELD_ALLE_REVERSECHARGEART_C_NR] = rcartDto.getCNr();
				zeile[FELD_ALLE_REVERSECHARGEART_DTO] = rcartDto;
				alDaten.add(zeile);
			}

			data = new Object[alDaten.size()][ALLE_ANZAHL_FELDER];
			data = (Object[][]) alDaten.toArray(data);

			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			// Waehrung
			mapParameter.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());

			mapParameter.put("P_ZUSATZKOSTEN", new Boolean(bZusatzkosten));
			mapParameter.put("P_AUSWERTUNG_NACH_FREIGABEDATUM", new Boolean(bDatumIstFreigabedatum));

			ParametermandantDto parameterERPRuefen = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_EINGANGSRECHNUNG, ParameterFac.PARAMETER_EINGANGSRECHNUNG_PRUEFEN);

			Integer iERPruefen = (Integer) parameterERPRuefen.getCWertAsObject();

			mapParameter.put("P_EINGANGSRECHNUNG_PRUEFEN", iERPruefen);

			StringBuffer sSortierung = new StringBuffer();
			// Sortierung nach Kostenstelle
			mapParameter.put(LPReport.P_SORTIERENACHKOSTENSTELLE, new Boolean(krit.bSortiereNachKostenstelle));
			if (krit.bSortiereNachKostenstelle) {
				sSortierung.append(
						getTextRespectUISpr("lp.kostenstelle", theClientDto.getMandant(), theClientDto.getLocUi()));
				sSortierung.append(", ");
			}
			// Sortieung nach Lieferant
			mapParameter.put(LPReport.P_SORTIERENACHLIEFERANT,
					new Boolean(krit.iSortierung == krit.KRIT_SORT_NACH_PARTNER));
			if (krit.iSortierung == krit.KRIT_SORT_NACH_PARTNER) {
				sSortierung.append(
						getTextRespectUISpr("lp.lieferant", theClientDto.getMandant(), theClientDto.getLocUi()));
			} else if (krit.iSortierung == krit.KRIT_SORT_NACH_BELEGNUMMER) {
				sSortierung.append(getTextRespectUISpr("er.eingangsrechnungsnummer", theClientDto.getMandant(),
						theClientDto.getLocUi()));
			}
			StringBuffer sFilter = new StringBuffer();
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
			mapParameter.put(LPReport.P_FILTER, sFilter.toString());
			initJRDS(mapParameter, EingangsrechnungReportFac.REPORT_MODUL,
					EingangsrechnungReportFac.REPORT_EINGANGSRECHNUNG_ALLE, theClientDto.getMandant(),
					theClientDto.getLocUi(), theClientDto);
			return getReportPrint();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} finally {
			closeSession(session);
		}
	}

	public JasperPrintLP[] printEingangsrechnung(Integer iEingangsrechnungIId, String sReportname, Integer iKopien,
			BigDecimal bdBetrag, String sZusatztext, Integer schecknummer, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Map<String, Object> mapParameter = new TreeMap<String, Object>();
		EingangsrechnungDto eingangsrechnungDto = getEingangsrechnungFac()
				.eingangsrechnungFindByPrimaryKey(iEingangsrechnungIId);
		LieferantDto lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(eingangsrechnungDto.getLieferantIId(),
				theClientDto);

		PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(lieferantDto.getPartnerIId(), theClientDto);

		Locale locLF = Helper.string2Locale(partnerDto.getLocaleCNrKommunikation());

		mapParameter.put("P_LIEFERANT_NAME1", partnerDto.getCName1nachnamefirmazeile1());
		mapParameter.put("P_LIEFERANT_NAME2", partnerDto.getCName2vornamefirmazeile2());
		mapParameter.put("P_LIEFERANT_NAME3", partnerDto.getCName3vorname2abteilung());

		mapParameter.put("P_EINGANGSRECHNUNGSART", eingangsrechnungDto.getEingangsrechnungartCNr());

		mapParameter.put("P_LIEFERANT_STRASSE", partnerDto.getCStrasse());
		String sLKZ = "";
		String sPLZ = "";
		String sOrt = "";
		if (partnerDto.getLandplzortDto() != null) {
			if (partnerDto.getLandplzortDto().getLandDto() != null) {
				sLKZ = partnerDto.getLandplzortDto().getLandDto().getCLkz();
			}
			sPLZ = partnerDto.getLandplzortDto().getCPlz();
			if (partnerDto.getLandplzortDto().getOrtDto() != null) {
				sOrt = partnerDto.getLandplzortDto().getOrtDto().getCName();
			}
		}

		ParametermandantDto parameterERPRuefen = getParameterFac().getMandantparameter(theClientDto.getMandant(),
				ParameterFac.KATEGORIE_EINGANGSRECHNUNG, ParameterFac.PARAMETER_EINGANGSRECHNUNG_PRUEFEN);

		Integer iERPruefen = (Integer) parameterERPRuefen.getCWertAsObject();

		mapParameter.put("P_EINGANGSRECHNUNG_PRUEFEN", iERPruefen);

		if (eingangsrechnungDto.getPersonalIIdGeprueft() != null) {
			PersonalDto personalDto = getPersonalFac()
					.personalFindByPrimaryKeySmall(eingangsrechnungDto.getPersonalIIdGeprueft());
			mapParameter.put("P_GEPRUEFT_VON", personalDto.getCKurzzeichen());
			mapParameter.put("P_GEPRUEFT_AM", eingangsrechnungDto.getTGeprueft());
		}

		mapParameter.put("P_LIEFERANT_LKZ", sLKZ);
		mapParameter.put("P_LIEFERANT_PLZ", sPLZ);
		mapParameter.put("P_LIEFERANT_ORT", sOrt);

		// Bank
		PartnerbankDto[] bankDtos = getBankFac().partnerbankFindByPartnerIIdOhneExc(lieferantDto.getPartnerIId(),
				theClientDto);
		if (bankDtos != null && bankDtos.length > 0) {

			PartnerDto bankDto = getPartnerFac().partnerFindByPrimaryKey(bankDtos[0].getBankPartnerIId(), theClientDto);
			BankDto bDto = getBankFac().bankFindByPrimaryKey(bankDtos[0].getBankPartnerIId(), theClientDto);

			String sLKZBank = "";
			String sPLZBank = "";
			String sOrtBank = "";
			if (bankDto.getLandplzortDto() != null) {
				if (bankDto.getLandplzortDto().getLandDto() != null) {
					sLKZBank = bankDto.getLandplzortDto().getLandDto().getCLkz();
				}
				sPLZBank = bankDto.getLandplzortDto().getCPlz();
				if (bankDto.getLandplzortDto().getOrtDto() != null) {
					sOrtBank = bankDto.getLandplzortDto().getOrtDto().getCName();
				}
			}

			mapParameter.put("P_BANK_LKZ", sLKZBank);
			mapParameter.put("P_BANK_PLZ", sPLZBank);
			mapParameter.put("P_BANK_ORT", sOrtBank);
			mapParameter.put("P_BANK_NAME1", bankDto.getCName1nachnamefirmazeile1());
			mapParameter.put("P_BANK_NAME2", bankDto.getCName2vornamefirmazeile2());
			mapParameter.put("P_BANK_NAME3", bankDto.getCName3vorname2abteilung());

			mapParameter.put("P_BANK_IBAN", bankDtos[0].getCIban());
			mapParameter.put("P_BANK_BIC", bDto.getCBic());
			mapParameter.put("P_BANK_KONTO", bankDtos[0].getCKtonr());
		}

		mapParameter.put("P_ZUSATZTEXT", sZusatztext);

		mapParameter.put("P_SCHECKNUMMER", schecknummer);
		mapParameter.put("P_BETRAG", bdBetrag);
		mapParameter.put("P_BELEGNUMMER", eingangsrechnungDto.getCNr());
		mapParameter.put("P_LIEFERANT_RECHNUNGSNUMMER", eingangsrechnungDto.getCLieferantenrechnungsnummer());
		mapParameter.put("P_LIEFERANT_KUNDENDATEN", eingangsrechnungDto.getCKundendaten());
		mapParameter.put("P_TEXT", eingangsrechnungDto.getCText());
		PartnerbankDto[] partnerbankDto = getBankFac().partnerbankFindByPartnerIId(partnerDto.getIId(), theClientDto);
		// Sind geordnet nach iSort also die erste
		if (partnerbankDto.length > 0) {
			mapParameter.put("P_LIEFERANT_KONTONUMMER", partnerbankDto[0].getCKtonr());
			BankDto bankDto = getBankFac().bankFindByPrimaryKey(partnerbankDto[0].getBankPartnerIId(), theClientDto);
			mapParameter.put("P_LIEFERANT_BLZ", bankDto.getCBlz());
		}

		this.useCase = UC_EINGANGSRECHNUNG;
		data = new Object[1][1];

		int iAnzahlExemplare = 1;

		if (iKopien != null && iKopien.intValue() > 0) {
			iAnzahlExemplare += iKopien.intValue();
		}
		JasperPrintLP[] aJasperPrint = null;
		aJasperPrint = new JasperPrintLP[iAnzahlExemplare];
		for (int iKopieNummer = 0; iKopieNummer < iAnzahlExemplare; iKopieNummer++) {
			if (iKopieNummer > 0) {
				mapParameter.put(LPReport.P_KOPIE_NUMMER, new Integer(iKopieNummer));
			}
			index = -1;
			initJRDS(mapParameter, EingangsrechnungReportFac.REPORT_MODUL, sReportname, theClientDto.getMandant(),
					locLF, theClientDto, true, eingangsrechnungDto.getKostenstelleIId());
			aJasperPrint[iKopieNummer] = getReportPrint();

		}
		aJasperPrint[0].putAdditionalInformation(JasperPrintLP.KEY_BELEGART, LocaleFac.BELEGART_EINGANGSRECHNUNG);
		aJasperPrint[0].putAdditionalInformation(JasperPrintLP.KEY_BELEGIID, eingangsrechnungDto.getIId());
		return aJasperPrint;
	}

	public JasperPrintLP[] printEingangsrechnungMitPositionen(Integer iEingangsrechnungIId, Locale locale,
			Boolean bMitLogo, Integer iAnzahlKopien, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		this.useCase = UC_ER_MIT_POSITIONEN;
		Map<String, Object> mapParameter = new TreeMap<String, Object>();
		EingangsrechnungDto eingangsrechnungDto = getEingangsrechnungFac()
				.eingangsrechnungFindByPrimaryKey(iEingangsrechnungIId);
		LieferantDto lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(eingangsrechnungDto.getLieferantIId(),
				theClientDto);
		PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(lieferantDto.getPartnerIId(), theClientDto);

		MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);

		mapParameter.put("P_BEARBEITER",
				getPersonalFac().getPersonRpt(eingangsrechnungDto.getPersonalIIdAnlegen(), theClientDto));

		mapParameter.put("Mandantenadresse", Helper.formatMandantAdresse(mandantDto));
		mapParameter.put("P_LIEFERANT_UID", lieferantDto.getPartnerDto().getCUid());

		mapParameter.put("P_KUNDENNUMMER", lieferantDto.getCKundennr());
		if (lieferantDto.getKontoIIdKreditorenkonto() != null) {
			mapParameter.put("P_KREDITORENKONTO",
					getFinanzFac().kontoFindByPrimaryKey(lieferantDto.getKontoIIdKreditorenkonto()).getCNr());
		}

		ParametermandantDto parameterAusgangsgutrschriftAnKunden = getParameterFac().getMandantparameter(
				theClientDto.getMandant(), ParameterFac.KATEGORIE_EINGANGSRECHNUNG,
				ParameterFac.PARAMETER_AUSGANGSGUTSCHRIFT_AN_KUNDE);

		boolean bAusgangsgutschriftAnKunde = (Boolean) parameterAusgangsgutrschriftAnKunden.getCWertAsObject();

		mapParameter.put("P_AUSGANGSGUTSCHRIFT_AN_KUNDE", new Boolean(bAusgangsgutschriftAnKunde));

		if (bAusgangsgutschriftAnKunde == true) {

			KundeDto kdDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(lieferantDto.getPartnerIId(),
					theClientDto.getMandant(), theClientDto);
			if (kdDto != null) {
				if (kdDto.getIidDebitorenkonto() != null) {

					mapParameter.put("P_DEBITORENKONTO",
							getFinanzFac().kontoFindByPrimaryKey(kdDto.getIidDebitorenkonto()).getCNr());
				}

				mapParameter.put("P_LIEFERANTENNR", kdDto.getCLieferantennr());
			}

		}

		mapParameter.put("Adressefuerausdruck",
				formatAdresseFuerAusdruck(lieferantDto.getPartnerDto(), null, mandantDto,
						Helper.string2Locale(lieferantDto.getPartnerDto().getLocaleCNrKommunikation()),
						LocaleFac.BELEGART_BESTELLUNG));

		ParametermandantDto parameterERPRuefen = getParameterFac().getMandantparameter(theClientDto.getMandant(),
				ParameterFac.KATEGORIE_EINGANGSRECHNUNG, ParameterFac.PARAMETER_EINGANGSRECHNUNG_PRUEFEN);

		Integer iERPruefen = (Integer) parameterERPRuefen.getCWertAsObject();

		mapParameter.put("P_EINGANGSRECHNUNG_PRUEFEN", iERPruefen);

		if (eingangsrechnungDto.getPersonalIIdGeprueft() != null) {
			PersonalDto personalDto = getPersonalFac()
					.personalFindByPrimaryKeySmall(eingangsrechnungDto.getPersonalIIdGeprueft());
			mapParameter.put("P_GEPRUEFT_VON", personalDto.getCKurzzeichen());
			mapParameter.put("P_GEPRUEFT_AM", eingangsrechnungDto.getTGeprueft());
		}

		String cBriefanrede = "";

		// neutrale Anrede
		cBriefanrede = getBriefanredeNeutralOderPrivatperson(lieferantDto.getPartnerIId(), locale, theClientDto);

		mapParameter.put("Briefanrede", cBriefanrede);

		PersonalDto oPersonalBenutzer = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(),
				theClientDto);
		PersonalDto oPersonalAnleger = getPersonalFac()
				.personalFindByPrimaryKey(eingangsrechnungDto.getPersonalIIdAnlegen(), theClientDto);
		mapParameter.put("P_UNSER_ZEICHEN",
				Helper.getKurzzeichenkombi(oPersonalBenutzer.getCKurzzeichen(), oPersonalAnleger.getCKurzzeichen()));

		mapParameter.put("P_BELEGDATUM", Helper.formatDatum(eingangsrechnungDto.getDBelegdatum(), locale));

		mapParameter.put("P_IG_ERWERB", Helper.short2Boolean(eingangsrechnungDto.getBIgErwerb()));
		// TODO ghp, 14.12.2015 ReverseChargeArt uebermitteln
		ReversechargeartDto rcartDto = getFinanzServiceFac()
				.reversechargeartFindByPrimaryKey(eingangsrechnungDto.getReversechargeartId(), theClientDto);
		mapParameter.put("P_REVERSECHARGE", !FinanzServiceFac.ReversechargeArt.OHNE.equals(rcartDto.getCNr()));
		mapParameter.put("P_REVERSECHARGEART_I_ID", eingangsrechnungDto.getReversechargeartId());
		mapParameter.put("P_REVERSECHARGEART_C_NR", rcartDto.getCNr());
		mapParameter.put("P_REVERSECHARGEART_DTO", rcartDto);

		ParametermandantDto parameterAbweichung = getParameterFac().getMandantparameter(theClientDto.getMandant(),
				ParameterFac.KATEGORIE_RECHNUNG, ParameterFac.PARAMETER_MAXIMALE_ABWEICHUNG_SUMMENAUSDRUCK);

		final Double dAbweichung = (Double) parameterAbweichung.getCWertAsObject();
		mapParameter.put("P_MAXIMALE_ABWEICHUNG", dAbweichung);

		mapParameter.put("P_BELEGDATUM", Helper.formatDatum(eingangsrechnungDto.getDBelegdatum(), locale));

		mapParameter.put("P_EINGANGSRECHNUNGSART", eingangsrechnungDto.getEingangsrechnungartCNr());

		mapParameter.put("P_ZAHLUNGSKONDITION", getMandantFac()
				.zahlungszielFindByIIdLocaleOhneExc(eingangsrechnungDto.getZahlungszielIId(), locale, theClientDto));
		mapParameter.put("P_WAEHRUNG", eingangsrechnungDto.getWaehrungCNr());

		mapParameter.put("P_BELEGNUMMER", eingangsrechnungDto.getCNr());
		mapParameter.put("P_TEXT", eingangsrechnungDto.getCText());
		mapParameter.put("P_WEARTIKEL", eingangsrechnungDto.getCWeartikel());
		mapParameter.put("P_LIEFERANT_RECHNUNGSNUMMER", eingangsrechnungDto.getCLieferantenrechnungsnummer());

		mapParameter.put("P_LIEFERANT_KUNDENDATEN", eingangsrechnungDto.getCKundendaten());
		mapParameter.put("P_TEXT", eingangsrechnungDto.getCText());
		PartnerbankDto[] partnerbankDto = getBankFac().partnerbankFindByPartnerIId(partnerDto.getIId(), theClientDto);
		// Sind geordnet nach iSort also die erste
		if (partnerbankDto.length > 0) {
			mapParameter.put("P_LIEFERANT_KONTONUMMER", partnerbankDto[0].getCKtonr());
			BankDto bankDto = getBankFac().bankFindByPrimaryKey(partnerbankDto[0].getBankPartnerIId(), theClientDto);
			mapParameter.put("P_LIEFERANT_BLZ", bankDto.getCBlz());
		}

		// Kopftext
		String sKopftext = eingangsrechnungDto.getCKopftextuebersteuert();
		if (sKopftext == null || sKopftext.length() == 0) {

			EingangsrechnungtextDto oRechnungtextDto = getEingangsrechnungServiceFac()
					.eingangsrechnungtextFindByMandantLocaleCNr(mandantDto.getCNr(),
							lieferantDto.getPartnerDto().getLocaleCNrKommunikation(), MediaFac.MEDIAART_KOPFTEXT);
			if (oRechnungtextDto != null) {
				sKopftext = oRechnungtextDto.getCTextinhalt();
			} else {
				sKopftext = "";
			}

		}
		mapParameter.put("P_KOPFTEXT", Helper.formatStyledTextForJasper(sKopftext));
		// Fusstext
		String sFusstext = eingangsrechnungDto.getCFusstextuebersteuert();
		if (sFusstext == null || sFusstext.length() == 0) {

			EingangsrechnungtextDto oRechnungtextDto = getEingangsrechnungServiceFac()
					.eingangsrechnungtextFindByMandantLocaleCNr(mandantDto.getCNr(),
							lieferantDto.getPartnerDto().getLocaleCNrKommunikation(), MediaFac.MEDIAART_FUSSTEXT);
			if (oRechnungtextDto != null) {
				sFusstext = oRechnungtextDto.getCTextinhalt();
			} else {
				sFusstext = "";
			}

		}
		BigDecimal bdBelegwert = BigDecimal.ZERO;

		mapParameter.put("P_FUSSTEXT", Helper.formatStyledTextForJasper(sFusstext));

		String sMandantAnrede = mandantDto.getPartnerDto().formatFixName1Name2();
		if (sMandantAnrede != null) {
			mapParameter.put(P_MANDANT_ANREDE_UND_NAME, sMandantAnrede);

		}
		final Set<?> mwstSatzKeys = getMandantFac().mwstsatzIIdFindAllByMandant(eingangsrechnungDto.getMandantCNr(),
				theClientDto);
		final LinkedHashMap<Integer, MwstsatzReportDto> mwstMap = new LinkedHashMap<Integer, MwstsatzReportDto>();
		// Fuer jeden UST-Satz gibt es einen Eintrag
		for (Iterator<?> iter = mwstSatzKeys.iterator(); iter.hasNext();) {
			Integer item = (Integer) iter.next();
			MwstsatzReportDto mwstsatzReportDto = new MwstsatzReportDto();
			mwstMap.put(item, mwstsatzReportDto);
		}

		ArrayList alDaten = new ArrayList();
		EingangsrechnungKontierungDto[] erkontDtos = getEingangsrechnungFac()
				.eingangsrechnungKontierungFindByEingangsrechnungIId(eingangsrechnungDto.getIId());
		for (int i = 0; i < erkontDtos.length; i++) {
			Object[] oZeile = new Object[ER_MIT_POSITIONEN_ANZAHL_FELDER];

			oZeile[ER_MIT_POSITIONEN_BETRAG] = erkontDtos[i].getNBetrag();
			oZeile[ER_MIT_POSITIONEN_BETRAG_UST] = erkontDtos[i].getNBetragUst();

			if (erkontDtos[i].getArtikelIId() != null) {
				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(erkontDtos[i].getArtikelIId(),
						theClientDto);
				BelegPositionDruckIdentDto druckDto = printIdent(null, LocaleFac.BELEGART_EINGANGSRECHNUNG, artikelDto,
						locale, lieferantDto.getPartnerIId(), theClientDto);
				oZeile[ER_MIT_POSITIONEN_IDENT] = druckDto.getSArtikelInfo();

				if (artikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
					oZeile[ER_MIT_POSITIONEN_POSITIONSART] = LocaleFac.POSITIONSART_HANDEINGABE;
				} else {
					oZeile[ER_MIT_POSITIONEN_POSITIONSART] = LocaleFac.POSITIONSART_IDENT;
				}

			}

			oZeile[ER_MIT_POSITIONEN_KONTO] = erkontDtos[i].getNBetrag();

			oZeile[ER_MIT_POSITIONEN_POSITIONSTEXT] = erkontDtos[i].getCKommentar();

			if (erkontDtos[i].getMwstsatzIId() != null) {
				MwstsatzDto mwstsatzDto = getMandantFac().mwstsatzFindByPrimaryKey(erkontDtos[i].getMwstsatzIId(),
						theClientDto);
				oZeile[ER_MIT_POSITIONEN_MWSTSATZ] = mwstsatzDto.getFMwstsatz();

				MwstsatzDto mwstREPos = getMandantFac().mwstsatzFindByPrimaryKey(erkontDtos[i].getMwstsatzIId(),
						theClientDto);

				oZeile[ER_MIT_POSITIONEN_MWSTSATZ] = mwstREPos.getFMwstsatz();
				oZeile[ER_MIT_POSITIONEN_BETRAG_UST] = erkontDtos[i].getNBetragUst();
				MwstsatzReportDto m = mwstMap.get(erkontDtos[i].getMwstsatzIId());
				m.setNSummeMwstbetrag(m.getNSummeMwstbetrag().add(erkontDtos[i].getNBetragUst()));
				m.setNSummePositionsbetrag(m.getNSummePositionsbetrag()
						.add(erkontDtos[i].getNBetrag().subtract(erkontDtos[i].getNBetragUst())));

			}
			if (erkontDtos[i].getKostenstelleIId() != null) {
				KostenstelleDto kstDto = getSystemFac()
						.kostenstelleFindByPrimaryKey(erkontDtos[i].getKostenstelleIId());
				oZeile[ER_MIT_POSITIONEN_KOSTENSTELLE] = kstDto.formatKostenstellenbezeichnung();
			}
			if (erkontDtos[i].getKontoIId() != null) {

				oZeile[ER_MIT_POSITIONEN_KONTO] = getFinanzFac().kontoFindByPrimaryKey(erkontDtos[i].getKontoIId())
						.getCNr();
			}

			alDaten.add(oZeile);

			bdBelegwert = bdBelegwert.add(erkontDtos[i].getNBetrag());

		}

		Object sMwstsatz[] = null;
		sMwstsatz = getMwstTabelleFuerERMitPositionen(mwstMap, eingangsrechnungDto, locale, theClientDto);

		mapParameter.put("P_MWST_TABELLE_LINKS", sMwstsatz[LPReport.MWST_TABELLE_LINKS]);
		mapParameter.put("P_MWST_TABELLE_RECHTS", sMwstsatz[LPReport.MWST_TABELLE_RECHTS]);
		mapParameter.put("P_MWST_TABELLE_BETRAG", sMwstsatz[LPReport.MWST_TABELLE_SUMME_POSITIONEN]);
		mapParameter.put("P_MWST_TABELLE_WAEHRUNG", sMwstsatz[LPReport.MWST_MWST_TABELLE_WAEHRUNG]);

		mapParameter.put("P_BELEGWERT", bdBelegwert);

		data = new Object[alDaten.size()][ER_MIT_POSITIONEN_ANZAHL_FELDER];
		data = (Object[][]) alDaten.toArray(data);

		int iAnzahlExemplare = 1;

		if (iAnzahlKopien != null && iAnzahlKopien.intValue() > 0) {
			iAnzahlExemplare += iAnzahlKopien.intValue();
		}
		JasperPrintLP[] aJasperPrint = null;
		aJasperPrint = new JasperPrintLP[iAnzahlExemplare];
		for (int iKopieNummer = 0; iKopieNummer < iAnzahlExemplare; iKopieNummer++) {
			if (iKopieNummer > 0) {
				mapParameter.put(LPReport.P_KOPIE_NUMMER, new Integer(iKopieNummer));
			}
			index = -1;
			initJRDS(mapParameter, EingangsrechnungReportFac.REPORT_MODUL, REPORT_EINGANGSRECHNUNG_MIT_POSITIONEN,
					theClientDto.getMandant(), locale, theClientDto, bMitLogo,
					eingangsrechnungDto.getKostenstelleIId());
			aJasperPrint[iKopieNummer] = getReportPrint();

		}

		PrintInfoDto values = getJCRDocFac().getPathAndPartnerAndTable(eingangsrechnungDto.getIId(),
				QueryParameters.UC_ID_EINGANGSRECHNUNG, theClientDto);
		aJasperPrint[0].setOInfoForArchive(values);
		aJasperPrint[0].putAdditionalInformation(JasperPrintLP.KEY_BELEGART, LocaleFac.BELEGART_EINGANGSRECHNUNG);
		aJasperPrint[0].putAdditionalInformation(JasperPrintLP.KEY_BELEGIID, eingangsrechnungDto.getIId());
		return aJasperPrint;
	}

	public Object[] getMwstTabelleFuerERMitPositionen(LinkedHashMap<Integer, MwstsatzReportDto> mwstMap,
			EingangsrechnungDto erDto, Locale locDruck, TheClientDto theClientDto) {

		// das Andrucken der gesammelten Mwstinformationen steuern
		StringBuffer sbMwstsatz = new StringBuffer();
		StringBuffer sbSummePositionsbetrag = new StringBuffer();
		StringBuffer sbWaehrung = new StringBuffer();
		StringBuffer sbSummeMwstbetrag = new StringBuffer();

		BigDecimal bdSumme = BigDecimal.ZERO;
		BigDecimal bdSummeUst = BigDecimal.ZERO;
		try {
			EingangsrechnungKontierungDto[] ekDtos = getEingangsrechnungFac()
					.eingangsrechnungKontierungFindByEingangsrechnungIId(erDto.getIId());

			for (int i = 0; i < ekDtos.length; i++) {
				bdSumme = bdSumme.add(ekDtos[i].getNBetrag());
				bdSummeUst = bdSummeUst.add(ekDtos[i].getNBetragUst());
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		BigDecimal nEendbetragMitMwst = Helper.rundeKaufmaennisch(bdSumme, 2)
				.add(Helper.rundeKaufmaennisch(bdSummeUst, 2));

		boolean bHatMwstWerte = false;

		for (Iterator<Integer> iter = mwstMap.keySet().iterator(); iter.hasNext();) {
			Integer key = (Integer) iter.next(); // IId des Mwstsatzes
			MwstsatzReportDto mwstsatzReportDto = (MwstsatzReportDto) mwstMap.get(key); // Summen der Mwstbetraege
			if (mwstsatzReportDto != null && mwstsatzReportDto.getNSummeMwstbetrag().doubleValue() != 0.0) {

				MwstsatzDto mwst = getMandantFac().mwstsatzFindByPrimaryKey(key, theClientDto);

				// MR: FIX, statt festverdrahtetem UST verwende
				// Localeabhaengigen Wert lp.ust
				sbMwstsatz.append(getTextRespectUISpr("lp.ust", theClientDto.getMandant(), locDruck));
				sbMwstsatz.append(": ");
				sbMwstsatz.append(Helper.formatZahl(mwst.getFMwstsatz(), 2, locDruck));
				sbMwstsatz.append(" % ");
				sbMwstsatz.append(getTextRespectUISpr("lp.ustvon", theClientDto.getMandant(), locDruck)).append(" ");
				// Fix Ende
				BigDecimal nPositionMwstbetrag = mwstsatzReportDto.getNSummePositionsbetrag();
				sbSummePositionsbetrag.append(Helper.formatAndRoundCurrency(nPositionMwstbetrag, locDruck));

				sbWaehrung.append(erDto.getWaehrungCNr());

				BigDecimal nSummeMwstbetrag = mwstsatzReportDto.getNSummeMwstbetrag();
				sbSummeMwstbetrag.append(Helper.formatAndRoundCurrency(nSummeMwstbetrag, locDruck));

				sbMwstsatz.append("\n");
				sbSummePositionsbetrag.append("\n");
				sbWaehrung.append("\n");
				sbSummeMwstbetrag.append("\n");
				if (nEendbetragMitMwst != null)
					nEendbetragMitMwst = nEendbetragMitMwst.add(nSummeMwstbetrag);

				bHatMwstWerte = true;
			}
		}

		if (bHatMwstWerte) {
			// die letzten \n wieder loeschen
			sbMwstsatz.delete(sbMwstsatz.length() - 1, sbMwstsatz.length());
			sbSummePositionsbetrag.delete(sbSummePositionsbetrag.length() - 1, sbSummePositionsbetrag.length());
			sbWaehrung.delete(sbWaehrung.length() - 1, sbWaehrung.length());
			sbSummeMwstbetrag.delete(sbSummeMwstbetrag.length() - 1, sbSummeMwstbetrag.length());
		}

		String P_MWST_TABELLE_LINKS = sbMwstsatz.toString();
		String P_MWST_TABELLE_SUMME_POSITIONEN = sbSummePositionsbetrag.toString();
		String P_MWST_TABELLE_WAEHRUNG = sbWaehrung.toString();
		String P_MWST_TABELLE_RECHTS = sbSummeMwstbetrag.toString();
		// BigDecimal P_ENDBETRAGMITMWST = nEendbetragMitMwst;
		BigDecimal P_ENDBETRAGMITMWST = Helper.rundeGeldbetrag(nEendbetragMitMwst);
		Object[] sr = new Object[] { P_MWST_TABELLE_LINKS, P_MWST_TABELLE_SUMME_POSITIONEN, P_MWST_TABELLE_WAEHRUNG,
				P_MWST_TABELLE_RECHTS, P_ENDBETRAGMITMWST };
		return sr;
	}

}
