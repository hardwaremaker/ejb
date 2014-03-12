/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung;
import com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnungKontierung;
import com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnungReport;
import com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnungzahlung;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungKontierungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungReportFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungzahlungDto;
import com.lp.server.eingangsrechnung.service.ReportEingangsrechnungKontierungsjournalDto;
import com.lp.server.finanz.ejbfac.BuchungDetailQueryBuilder;
import com.lp.server.finanz.ejbfac.FinanzSubreportGenerator;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzBuchungDetail;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.KontoDtoSmall;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferschein;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.partner.fastlanereader.generated.FLRLieferant;
import com.lp.server.partner.service.BankDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.PartnerbankDto;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.WechselkursDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.util.HelperServer;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.server.util.report.TimingInterceptor;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;

@Stateless
@Interceptors(TimingInterceptor.class)
public class EingangsrechnungReportFacBean extends LPReport implements
		EingangsrechnungReportFac, JRDataSource {
	public final static int UC_OFFENE = 0;
	public final static int UC_ALLE = 1;
	public final static int UC_ZAHLUNGEN = 2;
	public final static int UC_KONTIERUNG = 3;
	public final static int UC_EINGANGSRECHNUNG = 4;
	public final static int UC_FEHLENDE_ZOLLPAPIERE = 5;
	public final static int UC_ERFASSTE_ZOLLPAPIERE = 6;

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
	private static int ZAHLUNGEN_ANZAHL_FELDER = 18;

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
	private static final int OFFENE_ANZAHL_FELDER = 31;

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
	private static int KONTIERUNG_ANZAHL_FELDER = 24;

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
	private final static int ALLE_ANZAHL_FELDER = 28;

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
			}

			else if ("ER_WERT_FW".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_WERT_FW];
			} else if ("ER_WERT_UST_FW".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_WERT_UST_FW];
			} else if ("ER_WERT_BEZAHLT_FW".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_WERT_BEZAHLT_FW];
			} else if ("F_WAEHRUNG".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_WAEHRUNG_C_NR];
			} else if ("F_ER_KURS".equals(fieldName)) {
				value = data[index][KONTIERUNG_FELD_ER_KURS];
			}

			else if ("KONTOART_C_NR".equals(fieldName)) {
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
			}else if ("F_LSNUMMER".equals(fieldName)) {
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
			}
		}
			break;
		case UC_ZAHLUNGEN: {
			if ("ER_C_NR".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_ER_C_NR];
			} else if ("FIRMA".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_FIRMA];
			} else if ("ZAHLDATUM".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_ZAHLDATUM];
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
	public JasperPrintLP printZahlungsjournal(TheClientDto theClientDto,
			int iSortierung, Date dVon, Date dBis, boolean bZusatzkosten) {
		this.useCase = UC_ZAHLUNGEN;

		boolean fertig = true;

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();

			String s = "from FLREingangsrechnungzahlung zahlung WHERE zahlung.flreingangsrechnung.mandant_c_nr='"
					+ theClientDto.getMandant() + "'";

			s += " AND zahlung.t_zahldatum>='"
					+ Helper.formatDateWithSlashes(dVon) + "' ";

			s += " AND zahlung.t_zahldatum<'"
					+ Helper.formatDateWithSlashes(Helper.addiereTageZuDatum(
							dBis, 1)) + "' ";

			if (bZusatzkosten) {
				s += " AND zahlung.flreingangsrechnung.eingangsrechnungart_c_nr='"
						+ EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN
						+ "' ";
			} else {
				s += " AND zahlung.flreingangsrechnung.eingangsrechnungart_c_nr<>'"
						+ EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN
						+ "' ";
			}

			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			mapParameter.put("P_VON", dVon);
			mapParameter.put("P_BIS", dBis);
			mapParameter.put("P_ZUSATZKOSTEN", new Boolean(bZusatzkosten));

			mapParameter.put(LPReport.P_WAEHRUNG,
					theClientDto.getSMandantenwaehrung());

			String sSortierung = null;
			if (iSortierung == EingangsrechnungReportFac.REPORT_ZAHLUNGEN_SORT_BANK_AUSZUG) {
				sSortierung = "Bank, Auszug";
				s += " ORDER BY zahlung.flrbankverbindung.flrbank.flrpartner.c_name1nachnamefirmazeile1, "
						+ "zahlung.i_auszug, zahlung.zahlungsart_c_nr";
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
				FLREingangsrechnungzahlung zahlung = (FLREingangsrechnungzahlung) resultListIterator
						.next();
				EingangsrechnungDto erDto = getEingangsrechnungFac()
						.eingangsrechnungFindByPrimaryKey(
								zahlung.getEingangsrechnung_i_id());
				String sErCNr = erDto.getCNr();
				LieferantDto lieferantDto = getLieferantFac()
						.lieferantFindByPrimaryKey(erDto.getLieferantIId(),
								theClientDto);
				PartnerDto partnerDto = lieferantDto.getPartnerDto();
				String sFirma = partnerDto.formatFixTitelName1Name2();

				String sLaenderart = getFinanzServiceFac()
						.getLaenderartZuPartner(partnerDto.getIId(),
								theClientDto);
				data[row][ZAHLUNGEN_FELD_KREDITORENKONTO] = lieferantDto
						.getIKreditorenkontoAsIntegerNotiId();
				data[row][ZAHLUNGEN_FELD_LAENDERART] = sLaenderart;
				data[row][ZAHLUNGEN_FELD_ER_C_NR] = sErCNr;
				data[row][ZAHLUNGEN_FELD_FIRMA] = sFirma;
				data[row][ZAHLUNGEN_FELD_ZAHLDATUM] = zahlung.getT_zahldatum();
				data[row][ZAHLUNGEN_FELD_BETRAG] = zahlung.getN_betrag();
				data[row][ZAHLUNGEN_FELD_BETRAG_UST] = zahlung
						.getN_betrag_ust();
				if (zahlung.getN_betragfw() != null) {
					data[row][ZAHLUNGEN_FELD_BETRAG_MANDANTENWAEHRUNG_ZU_BELEGZEIT] = zahlung
							.getN_betragfw().multiply(erDto.getNKurs());
				}
				if (zahlung.getN_betragfw() != null) {
					data[row][ZAHLUNGEN_FELD_BETRAG_UST_MANDANTENWAEHRUNG_ZU_BELEGZEIT] = zahlung
							.getN_betrag_ustfw().multiply(erDto.getNKurs());
				}
				if (zahlung.getFlrbankverbindung() != null) {
					data[row][ZAHLUNGEN_FELD_BANK] = zahlung
							.getFlrbankverbindung().getFlrbank()
							.getFlrpartner().getC_name1nachnamefirmazeile1();
					data[row][ZAHLUNGEN_FELD_BLZ] = zahlung
							.getFlrbankverbindung().getFlrbank().getC_blz();
				}
				data[row][ZAHLUNGEN_FELD_BELEGWAEHRUNG] = erDto
						.getWaehrungCNr();
				data[row][ZAHLUNGEN_FELD_BETRAG_FW] = zahlung.getN_betragfw();
				data[row][ZAHLUNGEN_FELD_BETRAG_UST_FW] = zahlung
						.getN_betrag_ustfw();
				if (zahlung.getFlrbankverbindung() != null) {
					data[row][ZAHLUNGEN_FELD_KONTO] = zahlung
							.getFlrbankverbindung().getC_kontonummer();
				}

				data[row][ZAHLUNGEN_FELD_AUSZUG] = zahlung.getI_auszug();
				data[row][ZAHLUNGEN_FELD_ZAHLUNGSART] = zahlung
						.getZahlungsart_c_nr();
				if (zahlung.getFlrkassenbuch() != null) {
					data[row][ZAHLUNGEN_FELD_KASSENBUCH] = zahlung
							.getFlrkassenbuch().getC_bez();
				} else {
					data[row][ZAHLUNGEN_FELD_KASSENBUCH] = "";
				}
				row++;
			}

			/*
			 * int maxrow = row; row = 0;
			 * 
			 * do{ for(int i = 0; i < maxrow; i++){ fertig = false; if(maxrow !=
			 * (row + 1)){ Character help =
			 * data[row][ZAHLUNGEN_FELD_AUSZUG].toString().charAt(1);
			 * if(data[row][ZAHLUNGEN_FELD_AUSZUG].toString().charAt(1) ==
			 * data[row][ZAHLUNGEN_FELD_AUSZUG].toString().charAt(9) &&
			 * data[row+1][ZAHLUNGEN_FELD_AUSZUG].toString().charAt(1) ==
			 * data[row+1][ZAHLUNGEN_FELD_AUSZUG].toString().charAt(9)){
			 * if(data[row][ZAHLUNGEN_FELD_AUSZUG].toString().charAt(0) >
			 * data[row + 1][ZAHLUNGEN_FELD_AUSZUG].toString().charAt(0) ||
			 * data[row + 1][ZAHLUNGEN_FELD_AUSZUG].toString().charAt(0) ==
			 * '0'){ Object helpobj = new Object[resultList.size()]; helpobj =
			 * data[row]; data[row] = data[row+1]; data[row+1] = (Object[])
			 * helpobj; fertig = true; } }else{
			 * 
			 * } } row++; } row = 0; }while(fertig == true);
			 */

			initJRDS(
					mapParameter,
					EingangsrechnungReportFac.REPORT_MODUL,
					EingangsrechnungReportFac.REPORT_EINGANGSRECHNUNG_ZAHLUNGSJOURNAL,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
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
	 * Mehrwertsteuerbetrags erraten </br> Bei Faellen wie Bruttobetrag 0,15???
	 * und 7,5% Mwst bzw. 8% Mwst funktioniert das leider auch nicht wirklich
	 * 
	 * @param mandant
	 *            der Mandant fuer den der Mehrwertsteuersatz ermittelt werden
	 *            soll
	 * @param tBelegDatum
	 *            das (Beleg)datum fuer das die zugrundeliegende Mwst ermittelt
	 *            werden soll
	 * @param bruttoBetrag
	 * @param mwstBetrag
	 * @return den Mehrwertsteuersatz in Prozent, also 10, 20 oder auch 7,5 bzw.
	 *         8%.
	 */
	protected BigDecimal getMwstSatzVonBruttoBetragUndUst(String mandant,
			Timestamp tBelegDatum, BigDecimal bruttoBetrag,
			BigDecimal mwstBetrag) {
		if (null == mwstBetrag || mwstBetrag.signum() == 0)
			return BigDecimal.ZERO;

		MwstsatzDto[] mwstdtos = getMandantFac().mwstsatzfindAllByMandant(
				mandant, tBelegDatum, false);

		BigDecimal minDiff = null;
		BigDecimal selectedMwstSatz = null;
		for (MwstsatzDto mwstsatzDto : mwstdtos) {
			BigDecimal satz = new BigDecimal(mwstsatzDto.getFMwstsatz());
			BigDecimal mwst = Helper
					.getMehrwertsteuerBetrag(bruttoBetrag, satz);
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
				+ theClientDto.getMandant()
				+ "' AND er.t_zollimportpapier IS NULL AND er.eingangsrechnungart_c_nr = '"
				+ EingangsrechnungFac.EINGANGSRECHNUNGART_EINGANGSRECHNUNG
				+ "' ORDER BY er.flrlieferant.flrpartner.c_name1nachnamefirmazeile1, er.c_nr";

		Map<String, Object> mapParameter = new TreeMap<String, Object>();

		Query query = session.createQuery(s);

		List<?> resultList = query.list();

		Iterator resultListIterator = resultList.iterator();

		ArrayList alDaten = new ArrayList();

		while (resultListIterator.hasNext()) {
			FLREingangsrechnung item = (FLREingangsrechnung) resultListIterator
					.next();

			Object[] oZeile = new Object[FEHLENDE_ZOLLPAPIERE_ANZAHL_FELDER];
			oZeile[FEHLENDE_ZOLLPAPIERE_ER_C_NR] = item.getC_nr();
			oZeile[FEHLENDE_ZOLLPAPIERE_BRUTTOBETRAG] = item.getN_betrag();
			oZeile[FEHLENDE_ZOLLPAPIERE_ER_DATUM] = item.getT_belegdatum();
			oZeile[FEHLENDE_ZOLLPAPIERE_LIEFERANT] = HelperServer
					.formatNameAusFLRPartner(item.getFlrlieferant()
							.getFlrpartner());
			alDaten.add(oZeile);

		}
		session.close();
		session = factory.openSession();

		s = "SELECT ls, (select l.b_zollimportpapier from FLRLieferant l where l.mandant_c_nr=ls.mandant_c_nr and l.flrpartner.i_id=ls.flrkunde.flrpartner.i_id) as b_zollimport from FLRLieferschein ls WHERE ls.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' AND ls.t_zollexportpapier IS NULL AND ls.lieferscheinart_c_nr = '"
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
				oZeile[FEHLENDE_ZOLLPAPIERE_BRUTTOBETRAG] = item
						.getN_gesamtwertinlieferscheinwaehrung();
				oZeile[FEHLENDE_ZOLLPAPIERE_ER_DATUM] = item.getD_belegdatum();
				oZeile[FEHLENDE_ZOLLPAPIERE_LIEFERANT] = HelperServer
						.formatNameAusFLRPartner(item.getFlrkunde()
								.getFlrpartner());
				alDaten.add(oZeile);
			}
		}

		data = new Object[alDaten.size()][FEHLENDE_ZOLLPAPIERE_ANZAHL_FELDER];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(
				mapParameter,
				EingangsrechnungReportFac.REPORT_MODUL,
				EingangsrechnungReportFac.REPORT_EINGANGSRECHNUNG_FEHLENDE_ZOLLPAPIERE,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printErfassteZollpapiere(Date dVon, Date dBis,
			TheClientDto theClientDto) {
		this.useCase = UC_ERFASSTE_ZOLLPAPIERE;

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		session = factory.openSession();

		Map<String, Object> mapParameter = new TreeMap<String, Object>();

		mapParameter.put("P_VON", dVon);
		mapParameter.put("P_BIS", dBis);

		dBis = new Date(dBis.getTime() + 24 * 3600000);

		String s = "from FLREingangsrechnung er WHERE er.t_zollimportpapier>='"
				+ Helper.formatDateWithSlashes(dVon)
				+ "' AND er.t_zollimportpapier<'"
				+ Helper.formatDateWithSlashes(dBis)
				+ "' AND er.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' ORDER BY er.flrlieferant.flrpartner.c_name1nachnamefirmazeile1, er.c_nr";

		Query query = session.createQuery(s);

		List<?> resultList = query.list();

		Iterator resultListIterator = resultList.iterator();
		
		data = new Object[resultList.size()][ERFASSTE_ZOLLPAPIERE_ANZAHL_FELDER];

		ArrayList alDaten = new ArrayList();
		
		while (resultListIterator.hasNext()) {
			FLREingangsrechnung item = (FLREingangsrechnung) resultListIterator
					.next();

			Object[] oZeile = new Object[ERFASSTE_ZOLLPAPIERE_ANZAHL_FELDER];
			
			oZeile[ERFASSTE_ZOLLPAPIERE_ER_NUMMER] = item.getC_nr();
			oZeile[ERFASSTE_ZOLLPAPIERE_BRUTTOBETRAG] = item.getN_betrag();
			oZeile[ERFASSTE_ZOLLPAPIERE_ER_DATUM] = item.getT_belegdatum();
			oZeile[ERFASSTE_ZOLLPAPIERE_LIEFERANT] = HelperServer
					.formatNameAusFLRPartner(item.getFlrlieferant()
							.getFlrpartner());
			oZeile[ERFASSTE_ZOLLPAPIERE_ERFASSUNGSZEITPUNKT] = item
					.getT_zollimportpapier();
			try {
				EingangsrechnungDto erDto = getEingangsrechnungFac()
						.eingangsrechnungFindByPrimaryKey(item.getI_id());

				oZeile[ERFASSTE_ZOLLPAPIERE_ZOLLPAPIERNUMMER] = erDto
						.getCZollimportpapier();
				oZeile[ERFASSTE_ZOLLPAPIERE_WAEHRUNG] = erDto
						.getWaehrungCNr();

				if (erDto.getPersonalIIdZollimportpapier() != null) {
					oZeile[ERFASSTE_ZOLLPAPIERE_PERSON_ERFASST] = getPersonalFac()
							.personalFindByPrimaryKey(
									erDto.getPersonalIIdZollimportpapier(),
									theClientDto).formatFixName1Name2();
				}

				if (erDto.getEingangsrechnungIdZollimport() != null) {
					EingangsrechnungDto erDtoZollmiport = getEingangsrechnungFac()
							.eingangsrechnungFindByPrimaryKey(
									erDto.getEingangsrechnungIdZollimport());
					oZeile[ERFASSTE_ZOLLPAPIERE_EINGANGSRECHNUNG_ZOLLPAPIER] = erDtoZollmiport
							.getCNr();
				}

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);

			}
			alDaten.add(oZeile);
		}
		
		
		session.close();
		session = factory.openSession();
		
		
		 s = "from FLRLieferschein ls WHERE ls.t_zollexportpapier>='"
				+ Helper.formatDateWithSlashes(dVon)
				+ "' AND ls.t_zollexportpapier<'"
				+ Helper.formatDateWithSlashes(dBis)
				+ "' AND ls.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' ORDER BY ls.flrkunde.flrpartner.c_name1nachnamefirmazeile1, ls.c_nr";

		 query = session.createQuery(s);

		resultList = query.list();

		resultListIterator = resultList.iterator();
		

		
		while (resultListIterator.hasNext()) {
			FLRLieferschein item = (FLRLieferschein) resultListIterator
					.next();

			Object[] oZeile = new Object[ERFASSTE_ZOLLPAPIERE_ANZAHL_FELDER];
			
			oZeile[ERFASSTE_ZOLLPAPIERE_LS_NUMMER] = item.getC_nr();
			oZeile[ERFASSTE_ZOLLPAPIERE_BRUTTOBETRAG] = item.getN_gesamtwertinlieferscheinwaehrung();
			oZeile[ERFASSTE_ZOLLPAPIERE_ER_DATUM] = item.getD_belegdatum();
			oZeile[ERFASSTE_ZOLLPAPIERE_LIEFERANT] = HelperServer
					.formatNameAusFLRPartner(item.getFlrkunde()
							.getFlrpartner());
			oZeile[ERFASSTE_ZOLLPAPIERE_ERFASSUNGSZEITPUNKT] = item
					.getT_zollexportpapier();
			try {
				LieferscheinDto lsDto = getLieferscheinFac()
						.lieferscheinFindByPrimaryKey(item.getI_id());

				oZeile[ERFASSTE_ZOLLPAPIERE_ZOLLPAPIERNUMMER] = lsDto
						.getCZollexportpapier();
				oZeile[ERFASSTE_ZOLLPAPIERE_WAEHRUNG] = lsDto
						.getWaehrungCNr();

				if (lsDto.getPersonalIIdZollexportpapier() != null) {
					oZeile[ERFASSTE_ZOLLPAPIERE_PERSON_ERFASST] = getPersonalFac()
							.personalFindByPrimaryKey(
									lsDto.getPersonalIIdZollexportpapier(),
									theClientDto).formatFixName1Name2();
				}

				if (lsDto.getEingangsrechnungIdZollexport() != null) {
					EingangsrechnungDto erDtoZollmiport = getEingangsrechnungFac()
							.eingangsrechnungFindByPrimaryKey(
									lsDto.getEingangsrechnungIdZollexport());
					oZeile[ERFASSTE_ZOLLPAPIERE_EINGANGSRECHNUNG_ZOLLPAPIER] = erDtoZollmiport
							.getCNr();
				}

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);

			}
			alDaten.add(oZeile);
		}
		

		
		data = new Object[alDaten.size()][ERFASSTE_ZOLLPAPIERE_ANZAHL_FELDER];
		data = (Object[][]) alDaten.toArray(data);
		
		initJRDS(
				mapParameter,
				EingangsrechnungReportFac.REPORT_MODUL,
				EingangsrechnungReportFac.REPORT_EINGANGSRECHNUNG_ERFASSTE_ZOLLPAPIERE,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printKontierungsjournal(TheClientDto theClientDto,
			int iFilterER, Integer kostenstelleIId, int iKritDatum, Date dVon,
			Date dBis, boolean bZusatzkosten) {
		Session session = null;
		try {
			this.useCase = UC_KONTIERUNG;
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria cKontierung = session
					.createCriteria(FLREingangsrechnungKontierung.class);

			if (kostenstelleIId != null) {
				cKontierung.add(Restrictions.eq(
						EingangsrechnungFac.FLR_KONTIERUNG_FLRKOSTENSTELLE
								+ ".i_id", kostenstelleIId));
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

			String mandantCNr = theClientDto.getMandant();
			Criteria cEigangsrechnung = cKontierung
					.createCriteria(EingangsrechnungFac.FLR_KONTIERUNG_FLREINGANGSRECHNUNG);

			if (iKritDatum == EingangsrechnungReportFac.REPORT_KONTIERUNG_KRIT_DATUM_BELEG) {
				cEigangsrechnung
						.add(Restrictions.eq(
								EingangsrechnungFac.FLR_ER_MANDANT_C_NR,
								mandantCNr))
						.add(Restrictions.ge(
								EingangsrechnungFac.FLR_ER_D_BELEGDATUM, dVon))
						.add(Restrictions.le(
								EingangsrechnungFac.FLR_ER_D_BELEGDATUM, dBis))
						.add(Restrictions.in(
								EingangsrechnungFac.FLR_ER_STATUS_C_NR, cStati))
						.add(Restrictions
								.in(EingangsrechnungFac.FLR_ER_EINGANGSRECHNUNGART_C_NR,
										cArten));
			} else if (iKritDatum == EingangsrechnungReportFac.REPORT_KONTIERUNG_KRIT_DATUM_FREIGABE) {
				cEigangsrechnung
						.add(Restrictions.eq(
								EingangsrechnungFac.FLR_ER_MANDANT_C_NR,
								mandantCNr))
						.add(Restrictions.ge(
								EingangsrechnungFac.FLR_ER_D_FREIGABEDATUM,
								dVon))
						.add(Restrictions.le(
								EingangsrechnungFac.FLR_ER_D_FREIGABEDATUM,
								dBis))
						.add(Restrictions.in(
								EingangsrechnungFac.FLR_ER_STATUS_C_NR, cStati))
						.add(Restrictions
								.in(EingangsrechnungFac.FLR_ER_EINGANGSRECHNUNGART_C_NR,
										cArten));
			}
			// Sortierung noch kostenstelle, konto
			cKontierung.createCriteria(
					EingangsrechnungFac.FLR_KONTIERUNG_FLRKOSTENSTELLE)
					.addOrder(Order.asc("c_nr"));
			cKontierung.createCriteria(
					EingangsrechnungFac.FLR_KONTIERUNG_FLRKONTO).addOrder(
					Order.asc("c_nr"));

			List<?> listKontierung = cKontierung.list();
			ArrayList<ReportEingangsrechnungKontierungsjournalDto> coll = new ArrayList<ReportEingangsrechnungKontierungsjournalDto>();
			for (Iterator<?> iter = listKontierung.iterator(); iter.hasNext();) {
				FLREingangsrechnungKontierung item = (FLREingangsrechnungKontierung) iter
						.next();
				EingangsrechnungDto erDto = getEingangsrechnungFac()
						.eingangsrechnungFindByPrimaryKey(
								item.getEingangsrechnung_i_id());
				ReportEingangsrechnungKontierungsjournalDto dto = new ReportEingangsrechnungKontierungsjournalDto();
				// Bezahlter Wert in Mandantenwaehrung
				BigDecimal bdBezahlt = getEingangsrechnungFac()
						.getBezahltBetrag(erDto.getIId(), item.getI_id());
				dto.setBdBezahlt(bdBezahlt);

				dto.setBdBezahltFW(getEingangsrechnungFac().getBezahltBetragFw(
						erDto.getIId(), item.getI_id()));

				// Bezahlt Wert mit Kurs der ER in Mandantenwaehrung
				BigDecimal bdBezahltERKurs = getEingangsrechnungFac()
						.getBezahltBetragFwKontierung(erDto.getIId(),
								item.getI_id());
				if (bdBezahltERKurs != null) {
					bdBezahltERKurs = bdBezahltERKurs
							.multiply(erDto.getNKurs());
				}
				dto.setBdBezahltzuERKurs(bdBezahltERKurs);
				// Letztes Zahldatum
				EingangsrechnungzahlungDto letzteZahlung = getEingangsrechnungFac()
						.getLetzteZahlung(erDto.getIId());
				Date dLetztesZahldatum = null;
				if (letzteZahlung != null) {
					dLetztesZahldatum = new Date(letzteZahlung.getTZahldatum()
							.getTime());
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

				dto.setBdUst(item
						.getN_betrag_ust()
						.multiply(erDto.getNKurs())
						.setScale(FinanzFac.NACHKOMMASTELLEN,
								BigDecimal.ROUND_HALF_EVEN));
				dto.setBdUstFW(item.getN_betrag_ust());
				dto.setBdWert(item
						.getN_betrag()
						.multiply(erDto.getNKurs())
						.setScale(FinanzFac.NACHKOMMASTELLEN,
								BigDecimal.ROUND_HALF_EVEN));
				dto.setBdWertFW(item.getN_betrag());
				BigDecimal bdErrechneterSteuersatz = getMwstSatzVonBruttoBetragUndUst(
						erDto.getMandantCNr(), new Timestamp(erDto
								.getDBelegdatum().getTime()), dto.getBdWert(),
						dto.getBdUst());
				// BigDecimal bdErrechneterSteuersatz = dto
				// .getBdUst()
				// .divide(dto.getBdWert().subtract(
				// dto.getBdUst()), 4,
				// BigDecimal.ROUND_HALF_EVEN).movePointRight(2);
				dto.setBdErrechneterSteuersatz(bdErrechneterSteuersatz);
				dto.setDEingangsrechnungsdatum(item.getFlreingangsrechnung()
						.getT_belegdatum());
				dto.setSEingangsrechnungsnummer(item.getFlreingangsrechnung()
						.getC_nr());
				dto.setSEingangsrechnungText(erDto.getCText());
				dto.setSEingangsrechnungWeartikel(erDto.getCWeartikel());
				dto.setSKontobezeichnung(item.getFlrkonto().getC_bez());
				dto.setSKontonummer(item.getFlrkonto().getC_nr());
				dto.setSKostenstelleBezeichnung(item.getFlrkostenstelle()
						.getC_bez());
				dto.setSKostenstellenummer(item.getFlrkostenstelle().getC_nr());
				dto.setSLieferant(item.getFlreingangsrechnung()
						.getFlrlieferant().getFlrpartner()
						.getC_name1nachnamefirmazeile1());
				if (item.getFlrkonto().getFlrkontoart() != null) {
					dto.setSKontoart(getFinanzServiceFac()
							.uebersetzeKontoartOptimal(
									item.getFlrkonto().getFlrkontoart()
											.getC_nr(),
									theClientDto.getLocUi(),
									theClientDto.getLocMandant()));
				}
				coll.add(dto);
			}

			// jetzt noch die nicht mehrfach kontierten holen
			Criteria cER = session
					.createCriteria(FLREingangsrechnungReport.class);
			if (kostenstelleIId != null) {
				cER.add(Restrictions.eq(
						EingangsrechnungFac.FLR_ER_FLRKOSTENSTELLE + ".i_id",
						kostenstelleIId));
			}
			if (bZusatzkosten) {
				cER.add(Restrictions.eq(
						EingangsrechnungFac.FLR_ER_EINGANGSRECHNUNGART_C_NR,
						EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN));
			} else {
				cER.add(Restrictions.not(Restrictions.eq(
						EingangsrechnungFac.FLR_ER_EINGANGSRECHNUNGART_C_NR,
						EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN)));
			}

			if (iKritDatum == EingangsrechnungReportFac.REPORT_KONTIERUNG_KRIT_DATUM_BELEG) {
				cER.add(Restrictions.eq(
						EingangsrechnungFac.FLR_ER_MANDANT_C_NR, mandantCNr))
						.add(Restrictions.ge(
								EingangsrechnungFac.FLR_ER_D_BELEGDATUM, dVon))
						.add(Restrictions.le(
								EingangsrechnungFac.FLR_ER_D_BELEGDATUM, dBis))
						.add(Restrictions.in(
								EingangsrechnungFac.FLR_ER_STATUS_C_NR, cStati));
			} else if (iKritDatum == EingangsrechnungReportFac.REPORT_KONTIERUNG_KRIT_DATUM_FREIGABE) {
				cER.add(Restrictions.eq(
						EingangsrechnungFac.FLR_ER_MANDANT_C_NR, mandantCNr))
						.add(Restrictions.ge(
								EingangsrechnungFac.FLR_ER_D_FREIGABEDATUM,
								dVon))
						.add(Restrictions.le(
								EingangsrechnungFac.FLR_ER_D_FREIGABEDATUM,
								dBis))
						.add(Restrictions.in(
								EingangsrechnungFac.FLR_ER_STATUS_C_NR, cStati));
			}

			List<?> listER = cER.list();
			for (Iterator<?> iter = listER.iterator(); iter.hasNext();) {
				FLREingangsrechnungReport item = (FLREingangsrechnungReport) iter
						.next();
				EingangsrechnungDto erDto = getEingangsrechnungFac()
						.eingangsrechnungFindByPrimaryKey(item.getI_id());
				if (erDto.getKostenstelleIId() != null) {
					ReportEingangsrechnungKontierungsjournalDto dto = new ReportEingangsrechnungKontierungsjournalDto();
					// Bezahlter Wert in Mandantenwaehrung
					BigDecimal bdBezahlt = getEingangsrechnungFac()
							.getBezahltBetrag(erDto.getIId(), null);
					dto.setBdBezahlt(bdBezahlt);
					// Bezahlt Wert mit Kurs der ER in Mandantenwaehrung
					BigDecimal bdBezahltERKurs = getEingangsrechnungFac()
							.getBezahltBetragFw(erDto.getIId(), null);

					if (bdBezahltERKurs != null) {

						dto.setBdBezahltFW(new BigDecimal(bdBezahltERKurs
								.doubleValue()));

						bdBezahltERKurs = bdBezahltERKurs.multiply(erDto
								.getNKurs());
					}
					dto.setBdBezahltzuERKurs(bdBezahltERKurs);
					// Letztes Zahldatum
					EingangsrechnungzahlungDto letzteZahlung = getEingangsrechnungFac()
							.getLetzteZahlung(erDto.getIId());
					Date dLetztesZahldatum = null;
					if (letzteZahlung != null) {
						dLetztesZahldatum = new Date(letzteZahlung
								.getTZahldatum().getTime());
					}
					dto.setBdERKurs(erDto.getNKurs());
					dto.setWaehrungCNr(erDto.getWaehrungCNr());
					dto.setDLetzesZahldatum(dLetztesZahldatum);
					// Werte
					dto.setBdUst(erDto.getNUstBetrag().setScale(
							FinanzFac.NACHKOMMASTELLEN,
							BigDecimal.ROUND_HALF_EVEN));
					dto.setBdWert(erDto.getNBetrag().setScale(
							FinanzFac.NACHKOMMASTELLEN,
							BigDecimal.ROUND_HALF_EVEN));

					dto.setBdUstFW(erDto.getNUstBetragfw().setScale(
							FinanzFac.NACHKOMMASTELLEN,
							BigDecimal.ROUND_HALF_EVEN));
					dto.setBdWertFW(erDto.getNBetragfw().setScale(
							FinanzFac.NACHKOMMASTELLEN,
							BigDecimal.ROUND_HALF_EVEN));

					// Errechneter Steuersatz
					BigDecimal bdErrechneterSteuersatz = null;
					if (erDto.getNBetrag().subtract(erDto.getNUstBetrag())
							.doubleValue() == 0) {
						bdErrechneterSteuersatz = new BigDecimal(100);
					} else {
						// bdErrechneterSteuersatz = erDto
						// .getNUstBetrag()
						// .divide(erDto.getNBetrag().subtract(
						// erDto.getNUstBetrag()), 4,
						// BigDecimal.ROUND_HALF_EVEN)
						// .movePointRight(2);
						bdErrechneterSteuersatz = getMwstSatzVonBruttoBetragUndUst(
								erDto.getMandantCNr(), new Timestamp(erDto
										.getDBelegdatum().getTime()),
								dto.getBdWert(), dto.getBdUst());
					}

					dto.setBdErrechneterSteuersatz(bdErrechneterSteuersatz);

					dto.setDEingangsrechnungsdatum(item.getT_belegdatum());
					dto.setSEingangsrechnungsnummer(item.getC_nr());
					dto.setSEingangsrechnungText(erDto.getCText());
					dto.setSEingangsrechnungWeartikel(erDto.getCWeartikel());
					dto.setSLieferantenrechnungsnummer(item
							.getC_lieferantenrechnungsnummer());
					dto.setDFreigabedatum(item.getT_freigabedatum());
					if (erDto.getKontoIId() != null) {
						KontoDto kontoDto = getFinanzFac()
								.kontoFindByPrimaryKey(erDto.getKontoIId());
						dto.setSKontobezeichnung(kontoDto.getCBez());
						dto.setSKontonummer(kontoDto.getCNr());
						if (kontoDto.getKontoartCNr() != null) {
							dto.setSKontoart(getFinanzServiceFac()
									.uebersetzeKontoartOptimal(
											kontoDto.getKontoartCNr(),
											theClientDto.getLocUi(),
											theClientDto.getLocMandant()));
						}
					} else {

						dto.setSKontobezeichnung(getTextRespectUISpr(
								"er.kontierungfehlerhaft",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
						dto.setSKontonummer("ERR");
					}

					dto.setSKostenstelleBezeichnung(item.getFlrkostenstelle()
							.getC_bez());
					dto.setSKostenstellenummer(item.getFlrkostenstelle()
							.getC_nr());
					dto.setSLieferant(item.getFlrlieferant().getFlrpartner()
							.getC_name1nachnamefirmazeile1());

					if (item.getFlrlieferant().getKonto_i_id_kreditorenkonto() != null) {
						KontoDto kredKontoDto = getFinanzFac()
								.kontoFindByPrimaryKey(
										item.getFlrlieferant()
												.getKonto_i_id_kreditorenkonto());
						dto.setSKreditorennummer(kredKontoDto.getCNr());
					} else {
						dto.setSKreditorennummer(null);
					}

					coll.add(dto);
				}
			}
			// Sortieren
			Collections.sort(coll, new ComparatorKontierungsjournal(
					Helper.SORTIERUNG_NACH_KOSTENSTELLE_UND_KONTO));

			data = new Object[coll.size()][KONTIERUNG_ANZAHL_FELDER];
			int i = 0;
			for (Iterator<ReportEingangsrechnungKontierungsjournalDto> iter = coll
					.iterator(); iter.hasNext(); i++) {
				ReportEingangsrechnungKontierungsjournalDto item = (ReportEingangsrechnungKontierungsjournalDto) iter
						.next();
				data[i][KONTIERUNG_FELD_WERT_BEZAHLT] = item.getBdBezahlt();
				data[i][KONTIERUNG_FELD_WERT_UST] = item.getBdUst();
				data[i][KONTIERUNG_FELD_WERT] = item.getBdWert();

				data[i][KONTIERUNG_FELD_WERT_BEZAHLT_FW] = item
						.getBdBezahltFW();
				data[i][KONTIERUNG_FELD_WERT_UST_FW] = item.getBdUstFW();
				data[i][KONTIERUNG_FELD_WERT_FW] = item.getBdWertFW();
				data[i][KONTIERUNG_FELD_ER_KURS] = item.getBdERKurs();
				data[i][KONTIERUNG_FELD_WAEHRUNG_C_NR] = item.getWaehrungCNr();

				data[i][KONTIERUNG_FELD_ER_DATUM] = item
						.getDEingangsrechnungsdatum();
				data[i][KONTIERUNG_FELD_ER_C_NR] = item
						.getSEingangsrechnungsnummer();
				data[i][KONTIERUNG_FELD_ER_TEXT] = item
						.getSEingangsrechnungText();
				data[i][KONTIERUNG_FELD_ER_WEARTIKEL] = item
						.getSEingangsrechnungWeartikel();
				data[i][KONTIERUNG_FELD_KONTOART_C_NR] = item.getSKontoart();
				data[i][KONTIERUNG_FELD_KONTO_C_BEZ] = item
						.getSKontobezeichnung();
				data[i][KONTIERUNG_FELD_KONTO_C_NR] = item.getSKontonummer();
				data[i][KONTIERUNG_FELD_KOSTENSTELLE_C_BEZ] = item
						.getSKostenstelleBezeichnung();
				data[i][KONTIERUNG_FELD_KOSTENSTELLE_C_NR] = item
						.getSKostenstellenummer();
				data[i][KONTIERUNG_FELD_ER_LIEFERANT] = item.getSLieferant();
				data[i][KONTIERUNG_FELD_LETZTES_ZAHLDATUM] = item
						.getDLetzesZahldatum();
				data[i][KONTIERUNG_FELD_ERRECHNETER_STEUERSATZ] = item
						.getBdErrechneterSteuersatz();
				data[i][KONTIERUNG_FELD_LIEFERANTENRECHNUNGSNUMMER] = item
						.getSLieferantenrechnungsnummer();
				if (item.getDFreigabedatum() == null) {
					cKontierung = session
							.createCriteria(FLREingangsrechnungKontierung.class);

					if (kostenstelleIId != null) {
						cKontierung
								.add(Restrictions
										.eq(EingangsrechnungFac.FLR_KONTIERUNG_FLRKOSTENSTELLE
												+ ".i_id", kostenstelleIId));
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
						cKontierung
								.createCriteria(
										EingangsrechnungFac.FLR_KONTIERUNG_FLREINGANGSRECHNUNG)
								.add(Restrictions
										.eq(EingangsrechnungFac.FLR_ER_MANDANT_C_NR,
												mandantCNr))
								.add(Restrictions
										.ge(EingangsrechnungFac.FLR_ER_D_BELEGDATUM,
												dVon))
								.add(Restrictions
										.le(EingangsrechnungFac.FLR_ER_D_BELEGDATUM,
												dBis))
								.add(Restrictions.in(
										EingangsrechnungFac.FLR_ER_STATUS_C_NR,
										cStati));
					} else if (iKritDatum == EingangsrechnungReportFac.REPORT_KONTIERUNG_KRIT_DATUM_FREIGABE) {
						cKontierung
								.createCriteria(
										EingangsrechnungFac.FLR_KONTIERUNG_FLREINGANGSRECHNUNG)
								.add(Restrictions
										.eq(EingangsrechnungFac.FLR_ER_MANDANT_C_NR,
												mandantCNr))
								.add(Restrictions
										.ge(EingangsrechnungFac.FLR_ER_D_FREIGABEDATUM,
												dVon))
								.add(Restrictions
										.le(EingangsrechnungFac.FLR_ER_D_FREIGABEDATUM,
												dBis))
								.add(Restrictions.in(
										EingangsrechnungFac.FLR_ER_STATUS_C_NR,
										cStati));
					}
					// Sortierung noch kostenstelle, konto
					cKontierung.createCriteria(
							EingangsrechnungFac.FLR_KONTIERUNG_FLRKOSTENSTELLE)
							.addOrder(Order.asc("c_nr"));
					cKontierung.createCriteria(
							EingangsrechnungFac.FLR_KONTIERUNG_FLRKONTO)
							.addOrder(Order.asc("c_nr"));

					listKontierung = cKontierung.list();
					coll = new ArrayList<ReportEingangsrechnungKontierungsjournalDto>();
					for (Iterator<?> iter1 = listKontierung.iterator(); iter1
							.hasNext();) {
						FLREingangsrechnungKontierung item1 = (FLREingangsrechnungKontierung) iter1
								.next();
						EingangsrechnungDto erDto = getEingangsrechnungFac()
								.eingangsrechnungFindByPrimaryKey(
										item1.getEingangsrechnung_i_id());
						ReportEingangsrechnungKontierungsjournalDto dto = new ReportEingangsrechnungKontierungsjournalDto();

						BigDecimal bdBezahlt = getEingangsrechnungFac()
								.getBezahltBetrag(erDto.getIId(),
										item1.getI_id());
						dto.setBdBezahlt(bdBezahlt);
						// Bezahlt Wert mit Kurs der ER in Mandantenwaehrung
						BigDecimal bdBezahltERKurs = getEingangsrechnungFac()
								.getBezahltBetragFw(erDto.getIId(),
										item1.getI_id());
						if (bdBezahltERKurs != null) {
							dto.setBdBezahltFW(new BigDecimal(bdBezahltERKurs
									.doubleValue()));
							bdBezahltERKurs = bdBezahltERKurs.multiply(erDto
									.getNKurs());
						}
						dto.setBdBezahltzuERKurs(bdBezahltERKurs);
						// Letztes Zahldatum
						EingangsrechnungzahlungDto letzteZahlung = getEingangsrechnungFac()
								.getLetzteZahlung(erDto.getIId());
						Date dLetztesZahldatum = null;
						if (letzteZahlung != null) {
							dLetztesZahldatum = new Date(letzteZahlung
									.getTZahldatum().getTime());
						}
						dto.setDLetzesZahldatum(dLetztesZahldatum);
						// Errechneter Steuersatz
						BigDecimal bdErrechneterSteuersatz = erDto
								.getNUstBetrag()
								.divide(erDto.getNBetrag().subtract(
										erDto.getNUstBetrag()), 4,
										BigDecimal.ROUND_HALF_EVEN)
								.movePointRight(2);
						dto.setBdErrechneterSteuersatz(bdErrechneterSteuersatz);

						dto.setBdUst(item1
								.getN_betrag_ust()
								.multiply(erDto.getNKurs())
								.setScale(FinanzFac.NACHKOMMASTELLEN,
										BigDecimal.ROUND_HALF_EVEN));
						dto.setBdWert(item1
								.getN_betrag()
								.multiply(erDto.getNKurs())
								.setScale(FinanzFac.NACHKOMMASTELLEN,
										BigDecimal.ROUND_HALF_EVEN));

						dto.setBdUstFW(item1.getN_betrag_ust().setScale(
								FinanzFac.NACHKOMMASTELLEN,
								BigDecimal.ROUND_HALF_EVEN));
						dto.setBdWertFW(item1.getN_betrag().setScale(
								FinanzFac.NACHKOMMASTELLEN,
								BigDecimal.ROUND_HALF_EVEN));

						dto.setWaehrungCNr(erDto.getWaehrungCNr());

						dto.setDEingangsrechnungsdatum(item1
								.getFlreingangsrechnung().getT_belegdatum());
						dto.setSEingangsrechnungsnummer(item1
								.getFlreingangsrechnung().getC_nr());
						dto.setSEingangsrechnungText(erDto.getCText());
						dto.setSEingangsrechnungWeartikel(erDto.getCWeartikel());
						dto.setSKontobezeichnung(item1.getFlrkonto().getC_bez());
						dto.setSKontonummer(item1.getFlrkonto().getC_nr());
						dto.setSKostenstelleBezeichnung(item1
								.getFlrkostenstelle().getC_bez());
						dto.setSKostenstellenummer(item1.getFlrkostenstelle()
								.getC_nr());
						dto.setSLieferant(item1.getFlreingangsrechnung()
								.getFlrlieferant().getFlrpartner()
								.getC_name1nachnamefirmazeile1());
						if (item1.getFlrkonto().getFlrkontoart() != null) {
							dto.setSKontoart(getFinanzServiceFac()
									.uebersetzeKontoartOptimal(
											item1.getFlrkonto()
													.getFlrkontoart().getC_nr(),
											theClientDto.getLocUi(),
											theClientDto.getLocMandant()));
						}

						if (erDto.getDFreigabedatum() != null) {
							item.setDFreigabedatum(erDto.getDFreigabedatum());
						}

					}
				}
				data[i][KONTIERUNG_FELD_ER_FREIGABEDATUM] = item
						.getDFreigabedatum();
				data[i][KONTIERUNG_FELD_WERT_BEZAHLT_ERKURS] = item
						.getBdBezahltzuERKurs();
				data[i][KONTIERUNG_FELD_KREDITORENNUMMER] = item
						.getSKreditorennummer();
			}
			// Datumsbereich
			StringBuffer sDatum = new StringBuffer();
			if (iKritDatum == EingangsrechnungReportFac.REPORT_KONTIERUNG_KRIT_DATUM_BELEG) {
				sDatum.append("Belegdatum ");
			} else if (iKritDatum == EingangsrechnungReportFac.REPORT_KONTIERUNG_KRIT_DATUM_FREIGABE) {
				sDatum.append("Freigabedatum ");
			}
			if (dVon != null) {
				sDatum.append("von "
						+ Helper.formatDatum(dVon, theClientDto.getLocUi()));
			}
			if (dBis != null) {
				sDatum.append(" bis "
						+ Helper.formatDatum(dBis, theClientDto.getLocUi()));
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

			initJRDS(
					mapParameter,
					EingangsrechnungReportFac.REPORT_MODUL,
					EingangsrechnungReportFac.REPORT_EINGANGSRECHNUNG_KONTIERUNG,
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

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printOffene(TheClientDto theClientDto, int iSort,
			Integer lieferantIId, Date dStichtag,
			boolean bStichtagIstFreigabeDatum, boolean bZusatzkosten,
			boolean mitNichtZugeordnetenBelegen) {
		this.useCase = UC_OFFENE;

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			Map<Integer, ZahlungszielDto> mZahlungsziel = getMandantFac()
					.zahlungszielFindAllByMandantAsDto(
							theClientDto.getMandant(), theClientDto);

			session = factory.openSession();
			
			List<Integer> lieferantenIIds = new ArrayList<Integer>();
			if(lieferantIId != null) {
				lieferantenIIds.add(lieferantIId);
			} else if(iSort == EingangsrechnungReportFac.REPORT_OFFENE_SORT_LIEFERANT) {
				Iterator<?> iter = session.createCriteria(FLRLieferant.class)
						.createAlias("flrpartner", "p")
						.addOrder(Order.asc("p.c_name1nachnamefirmazeile1"))
						.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()))
						
						.list().iterator();
				while (iter.hasNext()){
					lieferantenIIds.add(((FLRLieferant)iter.next()).getI_id());
				}
			} else {
				lieferantenIIds.add(null);
			}
			
			List<Object[]> dataList = new ArrayList<Object[]>();
			
			for (Integer liefIId : lieferantenIIds) {
				Criteria crit = session
						.createCriteria(FLREingangsrechnungReport.class);
				// Filter nach Mandant
				crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));
	
				Collection<String> cStati = new LinkedList<String>();
				cStati.add(EingangsrechnungFac.STATUS_TEILBEZAHLT);
				cStati.add(EingangsrechnungFac.STATUS_ANGELEGT);
				cStati.add(EingangsrechnungFac.STATUS_ERLEDIGT);
				crit.add(Restrictions.in("status_c_nr", cStati));
	
				if (bZusatzkosten) {
					crit.add(Restrictions.eq(
							EingangsrechnungFac.FLR_ER_EINGANGSRECHNUNGART_C_NR,
							EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN));
				} else {
					crit.add(Restrictions.not(Restrictions.eq(
							EingangsrechnungFac.FLR_ER_EINGANGSRECHNUNGART_C_NR,
							EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN)));
				}
	
				if (liefIId != null) {
					crit.add(Restrictions.eq("lieferant_i_id", liefIId));
				}
				if (bStichtagIstFreigabeDatum) {
					crit.add(Restrictions.le("t_freigabedatum", dStichtag));
				} else {
					crit.add(Restrictions.le("t_belegdatum", dStichtag));
				}
				crit.add(Restrictions.or(
						Restrictions.gt("t_bezahltdatum", dStichtag),
						Restrictions.isNull("t_bezahltdatum")));
				crit.add(Restrictions.or(
						Restrictions.gt("t_manuellerledigt", dStichtag),
						Restrictions.isNull("t_manuellerledigt")));
	
				if (iSort == EingangsrechnungReportFac.REPORT_OFFENE_SORT_RECHNUNGSNUMMER) {
					crit.addOrder(Order.asc("c_nr"));
				} else if (iSort == EingangsrechnungReportFac.REPORT_OFFENE_SORT_LIEFERANT) {
					crit.addOrder(Order.asc("c_nr"));
				} else if (iSort == EingangsrechnungReportFac.REPORT_OFFENE_SORT_FAELLIGKEIT) {
					crit.addOrder(Order.asc("t_faelligkeit")).addOrder(
							Order.asc("c_nr")); // Inerhalb wird immer nach
												// Rechnungsnummer sortiert
				} else if (iSort == EingangsrechnungReportFac.REPORT_OFFENE_SORT_FAELLIGKEIT_SKONTO1) {
					crit.addOrder(Order.asc("t_faelligkeit_skonto1")).addOrder(
							Order.asc("c_nr"));
				} else if (iSort == EingangsrechnungReportFac.REPORT_OFFENE_SORT_FAELLIGKEIT_SKONTO2) {
					crit.addOrder(Order.asc("t_faelligkeit_skonto2")).addOrder(
							Order.asc("c_nr"));
				}
				if (iSort != EingangsrechnungReportFac.REPORT_OFFENE_SORT_LIEFERANT) {
					mitNichtZugeordnetenBelegen = false;
				}
				List<?> resultList = crit.list();
	
				Iterator<?> resultListIterator = resultList.iterator();
				int row = 0;
	
				Object[][] tempData = new Object[resultList.size()][OFFENE_ANZAHL_FELDER];
				
				if (mitNichtZugeordnetenBelegen) {
					LieferantDto liefDto = getLieferantFac()
							.lieferantFindByPrimaryKey(liefIId,
									theClientDto);

					if(liefDto.getKontoIIdKreditorenkonto() != null) {
						// TODO: nur FLRFinanzBuchungDetail holen
						Query query = session
								.createQuery("SELECT buchungdetail from FLRFinanzBuchungDetail buchungdetail LEFT OUTER JOIN buchungdetail.flrbuchung AS buchung"
										+ " WHERE"
										+ BuchungDetailQueryBuilder
												.buildNurOffeneBuchungDetails("buchungdetail")
										+ "AND"
										+ BuchungDetailQueryBuilder
												.buildNichtZuordenbareVonKonto(
														"buchungdetail",
														"buchung",
														liefDto.getKontoIIdKreditorenkonto())
										+ (dStichtag == null ? ""
												: ("AND buchung.d_buchungsdatum<='"
														+ Helper.formatDateWithSlashes(dStichtag) + "'")));
	
						@SuppressWarnings("unchecked")
						List<FLRFinanzBuchungDetail> bdList = query.list();
						if(bdList.size() > 0) {
							if(tempData.length < 1) {
								tempData = new Object[1][OFFENE_ANZAHL_FELDER];
								String sFirma = liefDto.getPartnerDto()
										.formatFixTitelName1Name2();
								tempData[0][OFFENE_FELD_FIRMA] = sFirma;
								tempData[0][OFFENE_FELD_KREDITORENNR] = getFinanzFac()
										.kontoFindByPrimaryKeySmall(
												liefDto.getKontoIIdKreditorenkonto()).getCNr();
							}
							tempData[0][OFFENE_SUBREPORT_OFFENE_BUCHUNGEN] = FinanzSubreportGenerator
									.createBuchungsdetailSubreport(bdList, false);
						}
					}

				}
				while (resultListIterator.hasNext()) {
					FLREingangsrechnungReport er = (FLREingangsrechnungReport) resultListIterator
							.next();
					EingangsrechnungDto erDto = getEingangsrechnungFac()
							.eingangsrechnungFindByPrimaryKey(er.getI_id());
					LieferantDto liefDto = getLieferantFac()
							.lieferantFindByPrimaryKey(erDto.getLieferantIId(),
									theClientDto);
					
					String sErCNr = erDto.getCNr();
					String sFirma = liefDto.getPartnerDto()
							.formatFixTitelName1Name2();
					ZahlungszielDto zzDto = mZahlungsziel.get(erDto
							.getZahlungszielIId());
					tempData[row][OFFENE_FELD_ER_C_NR] = sErCNr;
					tempData[row][OFFENE_FELD_FIRMA] = sFirma;
					tempData[row][OFFENE_FELD_ERDATUM] = er.getT_belegdatum();
					tempData[row][OFFENE_FELD_MAHNDATUM] = erDto.getTMahndatum();
					tempData[row][OFFENE_FELD_KREDITORENNR] = liefDto
							.getKontoIIdKreditorenkonto() != null ? getFinanzFac()
							.kontoFindByPrimaryKeySmall(
									liefDto.getKontoIIdKreditorenkonto()).getCNr()
							: null;
					tempData[row][OFFENE_FELD_MAHNSTUFE] = er.getMahnstufe_i_id();
					tempData[row][OFFENE_FELD_FREIGABEDATUM] = er.getT_freigabedatum();
					tempData[row][OFFENE_FELD_WERT] = erDto.getNBetrag();
					tempData[row][OFFENE_FELD_LIEFERANTENRECHNUNGSNUMMER] = erDto
							.getCLieferantenrechnungsnummer();
					tempData[row][OFFENE_FELD_TEXT] = erDto.getCText();
					if (zzDto != null) {
						tempData[row][OFFENE_FELD_ZAHLUNGSZIEL] = zzDto.getCBez();
						tempData[row][OFFENE_FELD_SKONTOTAGE1] = zzDto
								.getSkontoAnzahlTage1();
						tempData[row][OFFENE_FELD_SKONTOTAGE2] = zzDto
								.getSkontoAnzahlTage2();
						tempData[row][OFFENE_FELD_SKONTOPROZENT1] = zzDto
								.getSkontoProzentsatz1();
						tempData[row][OFFENE_FELD_SKONTOPROZENT2] = zzDto
								.getSkontoProzentsatz2();
						tempData[row][OFFENE_FELD_NETTOTAGE] = zzDto
								.getAnzahlZieltageFuerNetto();
						if (erDto.getDFreigabedatum() != null) {
							if (zzDto.getAnzahlZieltageFuerNetto() != null) {
								tempData[row][OFFENE_FELD_FAELLIGKEIT] = er
										.getT_faelligkeit();
							}
							if (zzDto.getSkontoAnzahlTage1() != null) {
								tempData[row][OFFENE_FELD_FAELLIGKEIT_SKONTO1] = er
										.getT_faelligkeit_skonto1();
							}
							if (zzDto.getSkontoAnzahlTage2() != null) {
								tempData[row][OFFENE_FELD_FAELLIGKEIT_SKONTO2] = er
										.getT_faelligkeit_skonto2();
							}
						}
					}
	
					// datum der letzten zahlung bis zum stichtag ermitteln
					EingangsrechnungzahlungDto[] zahlungen = getEingangsrechnungFac()
							.eingangsrechnungzahlungFindByEingangsrechnungIId(
									er.getI_id());
					java.sql.Date dZahldatum = null;
					for (int i = 0; i < zahlungen.length; i++) {
						if ((dZahldatum == null || zahlungen[i].getTZahldatum()
								.after(dZahldatum))
								&& !zahlungen[i].getTZahldatum().after(dStichtag)) {
							dZahldatum = new Date(zahlungen[i].getTZahldatum()
									.getTime());
						}
					}
					tempData[row][OFFENE_FELD_ZAHLDATUM] = dZahldatum;
					// Zahlungsbetrag bis zum Stichtag ermitteln
					BigDecimal bdBezahltFw = new BigDecimal(0);
					BigDecimal bdBezahltKursBelegdatum = new BigDecimal(0);
					for (int i = 0; i < zahlungen.length; i++) {
						if (!zahlungen[i].getTZahldatum().after(dStichtag)) {
							bdBezahltFw = bdBezahltFw.add(zahlungen[i]
									.getNBetragfw());
							bdBezahltKursBelegdatum = bdBezahltKursBelegdatum
									.add(zahlungen[i].getNBetragfw().multiply(
											erDto.getNKurs()));
						}
					}
	
					tempData[row][OFFENE_FELD_BETRAG] = getLocaleFac()
							.rechneUmInAndereWaehrungZuDatum(bdBezahltFw,
									erDto.getWaehrungCNr(),
									theClientDto.getSMandantenwaehrung(),
									// new Date(System.currentTimeMillis()),
									dStichtag, theClientDto);
					tempData[row][OFFENE_FELD_BETRAG_KURS_BELEGDATUM] = bdBezahltKursBelegdatum;
					tempData[row][OFFENE_FELD_OFFEN] = getLocaleFac()
							.rechneUmInAndereWaehrungZuDatum(
									erDto.getNBetragfw().subtract(bdBezahltFw),
									erDto.getWaehrungCNr(),
									theClientDto.getSMandantenwaehrung(),
									// new Date(System.currentTimeMillis()),
									dStichtag, theClientDto);
					tempData[row][OFFENE_FELD_OFFEN_KURS_BELEGDATUM] = erDto
							.getNBetrag().subtract(bdBezahltKursBelegdatum);
	
					tempData[row][OFFENE_FELD_BETRAG_FW] = bdBezahltFw;
					tempData[row][OFFENE_FELD_OFFEN_FW] = erDto.getNBetragfw()
							.subtract(bdBezahltFw);
					tempData[row][OFFENE_FELD_ERWAEHRUNG] = erDto.getWaehrungCNr();
					tempData[row][OFFENE_FELD_WERT_FW] = erDto.getNBetragfw();
					tempData[row][OFFENE_FELD_ERKURS] = erDto.getNKurs();
					WechselkursDto wkDto = getLocaleFac().getKursZuDatum(
							erDto.getWaehrungCNr(),
							theClientDto.getSMandantenwaehrung(), dStichtag,
							theClientDto);
					tempData[row][OFFENE_FELD_KURS_STICHTAG] = wkDto.getNKurs()
							.setScale(
									LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS,
									RoundingMode.HALF_EVEN);
					row++;
				}
				dataList.addAll(Arrays.asList(tempData));
			}
			
			data = dataList.toArray(new Object[0][]);
			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			mapParameter.put("P_DATUM", dStichtag);
			mapParameter.put("P_STICHTAGISTFREIGABEDATUM", new Boolean(
					bStichtagIstFreigabeDatum));
			mapParameter.put("P_ZUSATZKOSTEN", new Boolean(bZusatzkosten));
			String sSortierung = null;
			if (iSort == EingangsrechnungReportFac.REPORT_OFFENE_SORT_LIEFERANT) {
				sSortierung = "Lieferant";
			} else if (iSort == EingangsrechnungReportFac.REPORT_OFFENE_SORT_RECHNUNGSNUMMER) {
				sSortierung = "Rechnungsnummer";
			} else if (iSort == EingangsrechnungReportFac.REPORT_OFFENE_SORT_FAELLIGKEIT) {
				sSortierung = getTextRespectUISpr("lp.faelligkeit",
						theClientDto.getMandant(), theClientDto.getLocUi());
			} else if (iSort == EingangsrechnungReportFac.REPORT_OFFENE_SORT_FAELLIGKEIT_SKONTO1) {
				sSortierung = getTextRespectUISpr("er.faelligkeitskonto1",
						theClientDto.getMandant(), theClientDto.getLocUi());
			} else if (iSort == EingangsrechnungReportFac.REPORT_OFFENE_SORT_FAELLIGKEIT_SKONTO2) {
				sSortierung = getTextRespectUISpr("er.faelligkeitskonto2",
						theClientDto.getMandant(), theClientDto.getLocUi());
			}
			mapParameter
					.put("P_SORTIERUNGNACHLIEFERANT",
							new Boolean(
									iSort == EingangsrechnungReportFac.REPORT_OFFENE_SORT_LIEFERANT));
			mapParameter.put(LPReport.P_SORTIERUNG, sSortierung);

			String sZessionstext = null;
			sZessionstext = getParameterFac()
					.parametermandantFindByPrimaryKey(
							ParameterFac.PARAMETER_ZESSIONSTEXT,
							ParameterFac.KATEGORIE_ALLGEMEIN,
							theClientDto.getMandant()).getCWert();
			if (sZessionstext != null) {
				mapParameter.put("P_ZESSIONSTEXT", sZessionstext);
			}
			mapParameter.put("P_MANDANTENWAEHRUNG",
					theClientDto.getSMandantenwaehrung());
			initJRDS(mapParameter, EingangsrechnungReportFac.REPORT_MODUL,
					EingangsrechnungReportFac.REPORT_EINGANGSRECHNUNG_OFFENE,
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

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAlle(ReportJournalKriterienDto krit,
			TheClientDto theClientDto, boolean bZusatzkosten,
			boolean bDatumIstFreigabedatum) {
		Session session = null;
		try {
			this.useCase = UC_ALLE;

			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session
					.createCriteria(FLREingangsrechnungReport.class);
			c.add(Restrictions.eq(EingangsrechnungFac.FLR_ER_MANDANT_C_NR,
					theClientDto.getMandant()));
			if (krit.kostenstelleIId != null) {
				c.add(Restrictions.eq(
						EingangsrechnungFac.FLR_ER_KOSTENSTELLE_I_ID,
						krit.kostenstelleIId));
			}
			if (krit.lieferantIId != null) {
				c.add(Restrictions.eq(
						EingangsrechnungFac.FLR_ER_LIEFERANT_I_ID,
						krit.lieferantIId));
			}
			String sVon = null;
			String sBis = null;
			if (krit.dVon != null) {
				if (bDatumIstFreigabedatum) {
					c.add(Restrictions.ge(
							EingangsrechnungFac.FLR_ER_D_FREIGABEDATUM,
							krit.dVon));
				} else {
					c.add(Restrictions.ge(
							EingangsrechnungFac.FLR_ER_D_BELEGDATUM, krit.dVon));
				}
				sVon = Helper.formatDatum(krit.dVon, theClientDto.getLocUi());
			}
			if (krit.dBis != null) {
				if (bDatumIstFreigabedatum) {
					c.add(Restrictions.le(
							EingangsrechnungFac.FLR_ER_D_FREIGABEDATUM,
							krit.dBis));
				} else {
					c.add(Restrictions.le(
							EingangsrechnungFac.FLR_ER_D_BELEGDATUM, krit.dBis));
				}
				sBis = Helper.formatDatum(krit.dBis, theClientDto.getLocUi());
			}

			if (bZusatzkosten) {
				c.add(Restrictions.eq(
						EingangsrechnungFac.FLR_ER_EINGANGSRECHNUNGART_C_NR,
						EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN));
			} else {
				c.add(Restrictions.not(Restrictions.eq(
						EingangsrechnungFac.FLR_ER_EINGANGSRECHNUNGART_C_NR,
						EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN)));
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
				c.add(Restrictions.ge(EingangsrechnungFac.FLR_ER_C_NR, sVon));
			}
			if (krit.sBelegnummerBis != null) {
				sBis = HelperServer.getBelegnummernFilterForHibernateCriterias(
						f, iGeschaeftsjahr, sMandantKuerzel,
						krit.sBelegnummerBis);
				c.add(Restrictions.le(EingangsrechnungFac.FLR_ER_C_NR, sBis));
			}
			if (krit.bSortiereNachKostenstelle) {
				c.createCriteria(EingangsrechnungFac.FLR_ER_FLRKOSTENSTELLE)
						.addOrder(Order.asc("c_nr"));
			}
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				c.createCriteria(EingangsrechnungFac.FLR_ER_FLRLIEFERANT)
						.createCriteria(LieferantFac.FLR_PARTNER)
						.addOrder(
								Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
				c.addOrder(Order.asc(EingangsrechnungFac.FLR_ER_C_NR));
			} else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_BELEGNUMMER) {
				c.addOrder(Order.asc(EingangsrechnungFac.FLR_ER_C_NR));
			} else {
				c.addOrder(Order.asc(EingangsrechnungFac.FLR_ER_C_NR));
			}

			List<?> list = c.list();
			data = new Object[list.size()][ALLE_ANZAHL_FELDER];
			int i = 0;
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLREingangsrechnungReport e = (FLREingangsrechnungReport) iter
						.next();
				data[i][FELD_ALLE_AUSZUG] = getEingangsrechnungFac()
						.getAuszugDerLetztenZahlung(e.getI_id());
				BankverbindungDto bvDto = getEingangsrechnungFac()
						.getZuletztVerwendeteBankverbindung(e.getI_id());
				if (bvDto != null) {
					BankDto bankDto = getBankFac().bankFindByPrimaryKey(
							bvDto.getBankIId(), theClientDto);
					data[i][FELD_ALLE_BANK] = bankDto.getPartnerDto()
							.getCName1nachnamefirmazeile1();
				}
				data[i][FELD_ALLE_BELEGDATUM] = e.getT_belegdatum();
				data[i][FELD_ALLE_BEZAHLTDATUM] = e.getT_bezahltdatum();
				data[i][FELD_ALLE_EINGANGSRECHNUNGSNUMMER] = e.getC_nr();
				data[i][FELD_ALLE_STATUS] = e.getStatus_c_nr();
				data[i][FELD_ALLE_FREIGABEDATUM] = e.getT_freigabedatum();
				data[i][FELD_ALLE_KOSTENSTELLENUMMER] = e.getFlrkostenstelle() != null ? e
						.getFlrkostenstelle().getC_nr() : "";
				data[i][FELD_ALLE_LIEFERANT] = e.getFlrlieferant()
						.getFlrpartner().getC_name1nachnamefirmazeile1();
				data[i][FELD_ALLE_TEXT] = e.getC_text();
				data[i][FELD_ALLE_WEARTIKEL] = e.getC_weartikel();
				data[i][FELD_ALLE_WERT] = e.getN_betrag();
				data[i][FELD_ALLE_KURS] = e.getN_kurs();
				data[i][FELD_ALLE_WAEHRUNG] = e.getWaehrung_c_nr();
				data[i][FELD_ALLE_ZOLLBELEGNUMMER] = e.getC_zollimportpapier();
				data[i][FELD_ALLE_WERTNETTO] = e.getN_betrag() != null ? e
						.getN_betrag().subtract(e.getN_ustbetrag()) : null;
				data[i][FELD_ALLE_WERTUST] = e.getN_ustbetrag();

				data[i][FELD_ALLE_WERT_FW] = e.getN_betragfw();
				data[i][FELD_ALLE_WERTNETTO_FW] = e.getN_betragfw() != null ? e
						.getN_betragfw().subtract(e.getN_ustbetragfw()) : null;
				data[i][FELD_ALLE_WERTUST_FW] = e.getN_ustbetragfw();

				data[i][FELD_ALLE_ZIELDATUM] = getMandantFac()
						.berechneZielDatumFuerBelegdatum(
								e.getT_freigabedatum(),
								e.getZahlungsziel_i_id(), theClientDto);
				data[i][FELD_ALLE_LAENDERART] = getFinanzServiceFac()
						.getLaenderartZuPartner(
								e.getFlrlieferant().getFlrpartner().getI_id(),
								theClientDto);

				data[i][FELD_ALLE_ZAHLBETRAG] = getEingangsrechnungFac()
						.getBezahltBetrag(e.getI_id(), null);

				data[i][FELD_ALLE_ZAHLBETRAG_FW] = getEingangsrechnungFac()
						.getBezahltBetragFw(e.getI_id(), null);

				if (e.getFlrkonto() != null) {
					data[i][FELD_ALLE_KONTO] = e.getFlrkonto().getC_nr();
				}

				EingangsrechnungKontierungDto[] kontierungDtos = getEingangsrechnungFac()
						.eingangsrechnungKontierungFindByEingangsrechnungIId(
								e.getI_id());

				if (kontierungDtos.length > 0) {
					String[] fieldnames = new String[] { "F_BETRAG",
							"F_USTBETRAG", "F_KOSTENSTELLE", "F_SACHKONTO",
							"F_UST" };
					Object[][] dataSub = new Object[kontierungDtos.length][fieldnames.length];

					BigDecimal gesamtUst = new BigDecimal(0);

					for (int j = 0; j < kontierungDtos.length; j++) {
						dataSub[j][0] = kontierungDtos[j].getNBetrag();
						dataSub[j][1] = kontierungDtos[j].getNBetragUst();

						gesamtUst = gesamtUst.add(kontierungDtos[j]
								.getNBetragUst());

						dataSub[j][2] = getSystemFac()
								.kostenstelleFindByPrimaryKey(
										kontierungDtos[j].getKostenstelleIId())
								.getCNr();
						dataSub[j][3] = getFinanzFac()
								.kontoFindByPrimaryKeySmall(
										kontierungDtos[j].getKontoIId())
								.getCNr();
						dataSub[j][4] = new BigDecimal(getMandantFac()
								.mwstsatzFindByPrimaryKey(
										kontierungDtos[j].getMwstsatzIId(),
										theClientDto).getFMwstsatz());
					}

					data[i][FELD_ALLE_KONTIERUNG] = new LPDatenSubreport(
							dataSub, fieldnames);

					data[i][FELD_ALLE_WERTUST] = gesamtUst;

				} else {
					data[i][FELD_ALLE_UST] = getMwstSatzVonBruttoBetragUndUst(
							e.getMandant_c_nr(), new Timestamp(e
									.getT_belegdatum().getTime()),
							e.getN_betrag(), e.getN_ustbetrag());
				}

				// Debitorenkontonummer
				KontoDtoSmall kontoDtoKred = getFinanzFac()
						.kontoFindByPrimaryKeySmallOhneExc(
								e.getFlrlieferant()
										.getKonto_i_id_kreditorenkonto());
				String sKontonummer;
				if (kontoDtoKred != null) {
					sKontonummer = kontoDtoKred.getCNr();
				} else {
					sKontonummer = "";
				}
				data[i][FELD_ALLE_KREDITORENKONTO] = sKontonummer;
				i++;
			}
			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
			// Waehrung
			mapParameter.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());

			mapParameter.put("P_ZUSATZKOSTEN", new Boolean(bZusatzkosten));
			mapParameter.put("P_AUSWERTUNG_NACH_FREIGABEDATUM", new Boolean(
					bDatumIstFreigabedatum));

			StringBuffer sSortierung = new StringBuffer();
			// Sortierung nach Kostenstelle
			mapParameter.put(LPReport.P_SORTIERENACHKOSTENSTELLE, new Boolean(
					krit.bSortiereNachKostenstelle));
			if (krit.bSortiereNachKostenstelle) {
				sSortierung.append(getTextRespectUISpr("lp.kostenstelle",
						theClientDto.getMandant(), theClientDto.getLocUi()));
				sSortierung.append(", ");
			}
			// Sortieung nach Lieferant
			mapParameter.put(LPReport.P_SORTIERENACHLIEFERANT, new Boolean(
					krit.iSortierung == krit.KRIT_SORT_NACH_PARTNER));
			if (krit.iSortierung == krit.KRIT_SORT_NACH_PARTNER) {
				sSortierung.append(getTextRespectUISpr("lp.lieferant",
						theClientDto.getMandant(), theClientDto.getLocUi()));
			} else if (krit.iSortierung == krit.KRIT_SORT_NACH_BELEGNUMMER) {
				sSortierung.append(getTextRespectUISpr(
						"er.eingangsrechnungsnummer",
						theClientDto.getMandant(), theClientDto.getLocUi()));
			}
			StringBuffer sFilter = new StringBuffer();
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
			mapParameter.put(LPReport.P_FILTER, sFilter.toString());
			initJRDS(mapParameter, EingangsrechnungReportFac.REPORT_MODUL,
					EingangsrechnungReportFac.REPORT_EINGANGSRECHNUNG_ALLE,
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

	public JasperPrintLP[] printEingangsrechnung(Integer iEingangsrechnungIId,
			String sReportname, Integer iKopien, BigDecimal bdBetrag,
			String sZusatztext, Integer schecknummer, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Map<String, Object> mapParameter = new TreeMap<String, Object>();
		EingangsrechnungDto eingangsrechnungDto = getEingangsrechnungFac()
				.eingangsrechnungFindByPrimaryKey(iEingangsrechnungIId);
		LieferantDto lieferantDto = getLieferantFac()
				.lieferantFindByPrimaryKey(
						eingangsrechnungDto.getLieferantIId(), theClientDto);
		PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(
				lieferantDto.getPartnerIId(), theClientDto);
		mapParameter.put("P_LIEFERANT_NAME1",
				partnerDto.getCName1nachnamefirmazeile1());
		mapParameter.put("P_LIEFERANT_NAME2",
				partnerDto.getCName2vornamefirmazeile2());
		mapParameter.put("P_LIEFERANT_NAME3",
				partnerDto.getCName3vorname2abteilung());

		mapParameter.put("P_EINGANGSRECHNUNGSART",
				eingangsrechnungDto.getEingangsrechnungartCNr());

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

		// Bank
		PartnerbankDto[] bankDtos = getBankFac()
				.partnerbankFindByPartnerIIdOhneExc(
						lieferantDto.getPartnerIId(), theClientDto);
		if (bankDtos != null && bankDtos.length > 0) {

			PartnerDto bankDto = getPartnerFac().partnerFindByPrimaryKey(
					bankDtos[0].getBankPartnerIId(), theClientDto);
			BankDto bDto = getBankFac().bankFindByPrimaryKey(
					bankDtos[0].getBankPartnerIId(), theClientDto);

			String sLKZBank = "";
			String sPLZBank = "";
			String sOrtBank = "";
			if (bankDto.getLandplzortDto() != null) {
				if (bankDto.getLandplzortDto().getLandDto() != null) {
					sLKZBank = bankDto.getLandplzortDto().getLandDto()
							.getCLkz();
				}
				sPLZBank = bankDto.getLandplzortDto().getCPlz();
				if (bankDto.getLandplzortDto().getOrtDto() != null) {
					sOrtBank = bankDto.getLandplzortDto().getOrtDto()
							.getCName();
				}
			}

			mapParameter.put("P_BANK_LKZ", sLKZBank);
			mapParameter.put("P_BANK_PLZ", sPLZBank);
			mapParameter.put("P_BANK_ORT", sOrtBank);
			mapParameter.put("P_BANK_NAME1",
					bankDto.getCName1nachnamefirmazeile1());
			mapParameter.put("P_BANK_NAME2",
					bankDto.getCName2vornamefirmazeile2());
			mapParameter.put("P_BANK_NAME3",
					bankDto.getCName3vorname2abteilung());

			mapParameter.put("P_BANK_IBAN", bankDtos[0].getCIban());
			mapParameter.put("P_BANK_BIC", bDto.getCBic());
			mapParameter.put("P_BANK_KONTO", bankDtos[0].getCKtonr());
		}

		mapParameter.put("P_ZUSATZTEXT", sZusatztext);

		mapParameter.put("P_SCHECKNUMMER", schecknummer);
		mapParameter.put("P_BETRAG", bdBetrag);
		mapParameter.put("P_BELEGNUMMER", eingangsrechnungDto.getCNr());
		mapParameter.put("P_LIEFERANT_RECHNUNGSNUMMER",
				eingangsrechnungDto.getCLieferantenrechnungsnummer());
		mapParameter.put("P_LIEFERANT_KUNDENDATEN",
				eingangsrechnungDto.getCKundendaten());
		mapParameter.put("P_TEXT", eingangsrechnungDto.getCText());
		PartnerbankDto[] partnerbankDto = getBankFac()
				.partnerbankFindByPartnerIId(partnerDto.getIId(), theClientDto);
		// Sind geordnet nach iSort also die erste
		if (partnerbankDto.length > 0) {
			mapParameter.put("P_LIEFERANT_KONTONUMMER",
					partnerbankDto[0].getCKtonr());
			BankDto bankDto = getBankFac().bankFindByPrimaryKey(
					partnerbankDto[0].getBankPartnerIId(), theClientDto);
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
				mapParameter.put(LPReport.P_KOPIE_NUMMER, new Integer(
						iKopieNummer));
			}
			index = -1;
			initJRDS(mapParameter, EingangsrechnungReportFac.REPORT_MODUL,
					sReportname, theClientDto.getMandant(),
					theClientDto.getLocUi(), theClientDto, true,
					eingangsrechnungDto.getKostenstelleIId());
			aJasperPrint[iKopieNummer] = getReportPrint();

		}
		aJasperPrint[0].putAdditionalInformation(JasperPrintLP.KEY_BELEGART,
				LocaleFac.BELEGART_EINGANGSRECHNUNG);
		aJasperPrint[0].putAdditionalInformation(JasperPrintLP.KEY_BELEGIID,
				eingangsrechnungDto.getIId());
		return aJasperPrint;
	}

}
