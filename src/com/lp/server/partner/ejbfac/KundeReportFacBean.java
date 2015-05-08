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
package com.lp.server.partner.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.angebot.fastlanereader.generated.FLRAngebot;
import com.lp.server.angebot.service.AngebotFac;
import com.lp.server.artikel.ejb.Artikelspr;
import com.lp.server.artikel.ejb.ArtikelsprPK;
import com.lp.server.artikel.fastlanereader.generated.FLRShopgruppe;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.artikel.service.VkPreisfindungPreislisteDto;
import com.lp.server.artikel.service.VkpfMengenstaffelDto;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragposition;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferscheinposition;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartner;
import com.lp.server.partner.fastlanereader.generated.FLRKontakt;
import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.partner.fastlanereader.generated.FLRKurzbrief;
import com.lp.server.partner.fastlanereader.generated.FLRPASelektion;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.AnsprechpartnerfunktionDto;
import com.lp.server.partner.service.CustomerPricelistItemDescriptionDto;
import com.lp.server.partner.service.CustomerPricelistItemDto;
import com.lp.server.partner.service.CustomerPricelistPriceDto;
import com.lp.server.partner.service.CustomerPricelistReportDto;
import com.lp.server.partner.service.CustomerPricelistShopgroupDto;
import com.lp.server.partner.service.IdValueDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.KundeLieferstatistikDto;
import com.lp.server.partner.service.KundeReportFac;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.partner.service.KundesokomengenstaffelDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PASelektionDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.PartnerReportFac;
import com.lp.server.partner.service.SelektionDto;
import com.lp.server.partner.service.StatistikParamDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.projekt.fastlanereader.generated.FLRProjekt;
import com.lp.server.projekt.service.ProjektServiceFac;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungPosition;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.FacadeBeauftragter;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.report.IJasperPrintTransformer;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.server.util.report.TimingInterceptor;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;

@Stateless
@Interceptors(TimingInterceptor.class)
public class KundeReportFacBean extends LPReport implements KundeReportFac,
		JRDataSource {

	private int useCase;
	private Object[][] data = null;
	private BigDecimal summePreis = null;
	// private BigDecimal summeWert = null ;

	private IJasperPrintTransformer jasperPrintTransformer;

	@PersistenceContext
	private EntityManager em;

	private static int REPORT_KUNDENSTAMMBLATT_ANSPRECHPARTNER_VORNAME = 0;
	private static int REPORT_KUNDENSTAMMBLATT_ANSPRECHPARTNER_NACHNAME = 1;
	private static int REPORT_KUNDENSTAMMBLATT_ANSPRECHPARTNER_FUNKTION = 2;
	private static int REPORT_KUNDENSTAMMBLATT_ANSPRECHPARTNER_TITEL = 3;
	private static int REPORT_KUNDENSTAMMBLATT_ANSPRECHPARTNER_TELDW = 4;
	private static int REPORT_KUNDENSTAMMBLATT_ANSPRECHPARTNER_FAXDW = 5;
	private static int REPORT_KUNDENSTAMMBLATT_ANSPRECHPARTNER_MOBIL = 6;
	private static int REPORT_KUNDENSTAMMBLATT_ANSPRECHPARTNER_EMAIL = 7;

	private static int REPORT_KUNDENLISTE_BRIEFANREDE = 0;
	private static int REPORT_KUNDENLISTE_PARTNERART = 1;
	private static int REPORT_KUNDENLISTE_ANREDE = 2;
	private static int REPORT_KUNDENLISTE_KURZBEZEICHNUNG = 3;
	private static int REPORT_KUNDENLISTE_CNAME1 = 4;
	private static int REPORT_KUNDENLISTE_CNAME2 = 5;
	private static int REPORT_KUNDENLISTE_CNAME3 = 6;
	private static int REPORT_KUNDENLISTE_TITEL = 7;
	private static int REPORT_KUNDENLISTE_UIDNUMMER = 8;
	private static int REPORT_KUNDENLISTE_STRASSE = 9;
	private static int REPORT_KUNDENLISTE_LAND = 10;
	private static int REPORT_KUNDENLISTE_PLZ = 11;
	private static int REPORT_KUNDENLISTE_ORT = 12;
	private static int REPORT_KUNDENLISTE_LAND_POSTFACH = 13;
	private static int REPORT_KUNDENLISTE_PLZ_POSTFACH = 14;
	private static int REPORT_KUNDENLISTE_ORT_POSTFACH = 15;
	private static int REPORT_KUNDENLISTE_POSTFACH = 16;
	private static int REPORT_KUNDENLISTE_KOMMUNIKATIONSSPRACHE = 17;
	private static int REPORT_KUNDENLISTE_PARTNERKLASSE = 18;
	private static int REPORT_KUNDENLISTE_BRANCHE = 19;
	private static int REPORT_KUNDENLISTE_GERICHTSSTAND = 20;
	private static int REPORT_KUNDENLISTE_FIRMENBUCHNUMMER = 21;
	private static int REPORT_KUNDENLISTE_TELEFON = 22;
	private static int REPORT_KUNDENLISTE_FAX = 23;
	private static int REPORT_KUNDENLISTE_HOMEPAGE = 24;
	private static int REPORT_KUNDENLISTE_EMAIL = 25;
	private static int REPORT_KUNDENLISTE_MWSTSATZ = 26;
	private static int REPORT_KUNDENLISTE_PREISLISTE = 27;
	private static int REPORT_KUNDENLISTE_INTERESSENT = 28;
	private static int REPORT_KUNDENLISTE_UMSATZ_HEUER = 29;
	private static int REPORT_KUNDENLISTE_UMSATZ_VORJAHR = 30;
	private static int REPORT_KUNDENLISTE_ANZAHL_RECHNUNGEN_HEUER = 31;
	private static int REPORT_KUNDENLISTE_ANZAHL_RECHNUNGEN_VORJAHR = 32;
	private static int REPORT_KUNDENLISTE_KOSTENSTELLE = 33;
	private static int REPORT_KUNDENLISTE_ERLOESKONTO = 34;
	private static int REPORT_KUNDENLISTE_DEBITORENKONTO = 35;
	private static int REPORT_KUNDENLISTE_ZESSION = 36;
	private static int REPORT_KUNDENLISTE_WAEHRUNG = 37;
	private static int REPORT_KUNDENLISTE_ABW_UST_LAND = 38;
	private static int REPORT_KUNDENLISTE_ZAHLUNGSZIEL = 39;
	private static int REPORT_KUNDENLISTE_LIEFERART = 40;
	private static int REPORT_KUNDENLISTE_SPEDITEUR = 41;
	private static int REPORT_KUNDENLISTE_RABATT = 42;
	private static int REPORT_KUNDENLISTE_KREDITLIMIT = 43;
	private static int REPORT_KUNDENLISTE_LIEFERANTENNUMMER = 44;
	private static int REPORT_KUNDENLISTE_ABC = 45;
	private static int REPORT_KUNDENLISTE_LETZTE_BONITAETSPRUEFUNG = 46;
	private static int REPORT_KUNDENLISTE_PROVISIONSEMPFAENGER = 47;
	private static int REPORT_KUNDENLISTE_RECHNUNGSADRESSE_ANREDE = 48;
	private static int REPORT_KUNDENLISTE_RECHNUNGSADRESSE_KURZBEZEICHNUNG = 49;
	private static int REPORT_KUNDENLISTE_RECHNUNGSADRESSE_CNAME1 = 50;
	private static int REPORT_KUNDENLISTE_RECHNUNGSADRESSE_CNAME2 = 51;
	private static int REPORT_KUNDENLISTE_RECHNUNGSADRESSE_CNAME3 = 52;
	private static int REPORT_KUNDENLISTE_RECHNUNGSADRESSE_TITEL = 53;
	private static int REPORT_KUNDENLISTE_RECHNUNGSADRESSE_UIDNUMMER = 54;
	private static int REPORT_KUNDENLISTE_RECHNUNGSADRESSE_STRASSE = 55;
	private static int REPORT_KUNDENLISTE_RECHNUNGSADRESSE_LAND = 56;
	private static int REPORT_KUNDENLISTE_RECHNUNGSADRESSE_PLZ = 57;
	private static int REPORT_KUNDENLISTE_RECHNUNGSADRESSE_ORT = 58;
	private static int REPORT_KUNDENLISTE_RECHNUNGSADRESSE_LAND_POSTFACH = 59;
	private static int REPORT_KUNDENLISTE_RECHNUNGSADRESSE_PLZ_POSTFACH = 60;
	private static int REPORT_KUNDENLISTE_RECHNUNGSADRESSE_ORT_POSTFACH = 61;
	private static int REPORT_KUNDENLISTE_RECHNUNGSADRESSE_POSTFACH = 62;
	private static int REPORT_KUNDENLISTE_SELEKTION01 = 63;
	private static int REPORT_KUNDENLISTE_SELEKTION02 = 64;
	private static int REPORT_KUNDENLISTE_SELEKTION03 = 65;
	private static int REPORT_KUNDENLISTE_SELEKTION04 = 66;
	private static int REPORT_KUNDENLISTE_SELEKTION05 = 67;
	private static int REPORT_KUNDENLISTE_SELEKTION06 = 68;
	private static int REPORT_KUNDENLISTE_SELEKTION07 = 69;
	private static int REPORT_KUNDENLISTE_SELEKTION08 = 70;
	private static int REPORT_KUNDENLISTE_SELEKTION09 = 71;
	private static int REPORT_KUNDENLISTE_SELEKTION10 = 72;
	private static int REPORT_KUNDENLISTE_SELEKTION11 = 73;
	private static int REPORT_KUNDENLISTE_SELEKTION12 = 74;
	private static int REPORT_KUNDENLISTE_SELEKTION13 = 75;
	private static int REPORT_KUNDENLISTE_SELEKTION14 = 76;
	private static int REPORT_KUNDENLISTE_SELEKTION15 = 77;
	private static int REPORT_KUNDENLISTE_ANSPRECHPARTNER_ANREDE = 78;
	private static int REPORT_KUNDENLISTE_ANSPRECHPARTNER_TITEL = 79;
	private static int REPORT_KUNDENLISTE_ANSPRECHPARTNER_VORNAME = 80;
	private static int REPORT_KUNDENLISTE_ANSPRECHPARTNER_NACHNAME = 81;
	private static int REPORT_KUNDENLISTE_ANSPRECHPARTNER_EMAIL = 82;
	private static int REPORT_KUNDENLISTE_ANSPRECHPARTNER_MOBIL = 83;
	private static int REPORT_KUNDENLISTE_ANSPRECHPARTNER_TELDW = 84;
	private static int REPORT_KUNDENLISTE_ANSPRECHPARTNER_FAXDW = 85;
	private static int REPORT_KUNDENLISTE_ANSPRECHPARTNER_BEMERKUNG = 86;
	private static int REPORT_KUNDENLISTE_BEMERKUNG = 87;
	private static int REPORT_KUNDENLISTE_SUBREPORT_PROJEKTE = 88;
	private static int REPORT_KUNDENLISTE_KUNDENNUMMER = 89;
	private static int REPORT_KUNDENLISTE_KUNDE_I_ID = 90;
	private static int REPORT_KUNDENLISTE_ANZAHL_SPALTEN = 91;

	private static int REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_KUNDE = 0;
	private static int REPORT_WARTUNGSAUSWERTUNG_FAELLIGKEIT = 1;
	private static int REPORT_WARTUNGSAUSWERTUNG_ARTIKELGRUPPE = 2;
	private static int REPORT_WARTUNGSAUSWERTUNG_INTERVALL = 3;
	private static int REPORT_WARTUNGSAUSWERTUNG_LETZTEPRUEFUNG = 4;
	private static int REPORT_WARTUNGSAUSWERTUNG_LETZTEPRUEFUNGDURCHGEFUEHRT = 5;
	private static int REPORT_WARTUNGSAUSWERTUNG_ARTIKELNUMMER = 6;
	private static int REPORT_WARTUNGSAUSWERTUNG_BEZEICHNUNG = 7;
	private static int REPORT_WARTUNGSAUSWERTUNG_ARTIKELLIEFERANT = 8;
	private static int REPORT_WARTUNGSAUSWERTUNG_PRUEFER = 9;
	private static int REPORT_WARTUNGSAUSWERTUNG_LETZTEPRUEFUNGDURCHGEFUEHRTLS = 10;
	private static int REPORT_WARTUNGSAUSWERTUNG_LETZTEPRUEFUNGDURCHGEFUEHRTPERSON = 11;
	private static int REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_PLZ = 12;
	private static int REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_ORT = 13;
	private static int REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_STRASSE = 14;
	private static int REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_LKZ = 15;
	private static int REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_FILIALNUMMER = 16;
	private static int REPORT_WARTUNGSAUSWERTUNG_AUFTRAG_AUSLOESER = 17;
	private static int REPORT_WARTUNGSAUSWERTUNG_POSITIONSKOMMENTAR_LIEFERSCHEIN = 18;
	private static int REPORT_WARTUNGSAUSWERTUNG_POSITIONSKOMMENTAR_AUFTRAG = 19;

	public final static int REPORT_KUNDENPREISLISTE_ARTIKELNUMMER = 0;
	public final static int REPORT_KUNDENPREISLISTE_BEZEICHNUNG = 1;
	public final static int REPORT_KUNDENPREISLISTE_KURZBEZEICHNUNG = 2;
	public final static int REPORT_KUNDENPREISLISTE_ZUSATZBEZEICHNUNG = 3;
	public final static int REPORT_KUNDENPREISLISTE_ZUSATZBEZEICHNUNG2 = 4;
	public final static int REPORT_KUNDENPREISLISTE_ARTIKELGRUPPE = 5;
	public final static int REPORT_KUNDENPREISLISTE_BEMERKUNG = 6;
	public final static int REPORT_KUNDENPREISLISTE_GUELTIGAB = 7;
	public final static int REPORT_KUNDENPREISLISTE_GUELTIGBIS = 8;
	public final static int REPORT_KUNDENPREISLISTE_KUNDENARTIKELNUMMER = 9;
	public final static int REPORT_KUNDENPREISLISTE_KUNDENARTIKELBEZEICHNUNG = 10;
	public final static int REPORT_KUNDENPREISLISTE_VERSTECKT = 11;
	public final static int REPORT_KUNDENPREISLISTE_SUBREPORT_PREISE = 12;
	public final static int REPORT_KUNDENPREISLISTE_ARTIKELKLASSE = 13;
	public final static int REPORT_KUNDENPREISLISTE_ENTHAELT_SOKO = 14;
	public final static int REPORT_KUNDENPREISLISTE_ANZAHL_SPALTEN = 15;

	protected void setData(Object[][] newData) {
		data = newData;
		jasperPrintTransformer = null;
	}

	protected void setDataTransformer(IJasperPrintTransformer transformer) {
		data = null;
		jasperPrintTransformer = transformer;
	}

	public boolean next() throws JRException {
		index++;
		if (data != null) {
			return (index < data.length);
		} else {
			return jasperPrintTransformer.next(index);
		}
	}

	public Object getFieldValue(JRField field) throws JRException {

		Object value = null;
		String fieldName = field.getName();

		switch (useCase) {
		case KundeReportFac.UC_REPORT_KUNDE_LIEFERSTATISTIK: {
			value = getFieldValueLieferstatistik(fieldName);
		}
			break;
		case KundeReportFac.UC_REPORT_KUNDE_MONATSSTATISTIK: {
			value = getFieldValueMonatsstatistik(fieldName);
		}
			break;
		case KundeReportFac.UC_REPORT_KUNDE_STAMMBLATT: {
			value = getFieldValueStammblatt(fieldName);
		}
			break;
		case KundeReportFac.UC_REPORT_KUNDE_STATISTIK: {
			value = getFieldValueStatistik(fieldName);
		}
			break;
		case KundeReportFac.UC_REPORT_KUNDE_KUNDENLISTE: {
			value = getFieldValueKundenliste(fieldName);
		}
			break;
		case KundeReportFac.UC_REPORT_KUNDE_KUNDENPREISLISTE: {
			value = getFieldValueKundenpreisliste(fieldName);
		}
			break;
		case KundeReportFac.UC_REPORT_KUNDE_WARTUNGSAUSWERTUNG: {
			value = getFieldValueWartungsauswertung(fieldName);
		}
			break;
		}
		return value;
	}

	/**
	 * getFieldValueStatistik
	 * 
	 * @param fieldName
	 *            String
	 * @return Object
	 */
	private Object getFieldValueStatistik(String fieldName) {
		return null;
	}

	private Object getFieldValueLieferstatistik(String fieldName) {
		Object value = null;
		if ("F_RECHNUNG".equals(fieldName)) {
			value = data[index][REPORT_STATISTIK_RECHNUNGSNUMMER];
		} else if ("F_LIEFERSCHEIN".equals(fieldName)) {
			value = data[index][REPORT_STATISTIK_LIEFERSCHEINNUMMER];
		} else if ("F_DATUM".equals(fieldName)) {
			value = data[index][REPORT_STATISTIK_DATUM];
		} else if ("F_IDENT".equals(fieldName)) {
			value = data[index][REPORT_STATISTIK_IDENT];
		} else if (F_BEZEICHNUNG.equals(fieldName)) {
			value = data[index][REPORT_STATISTIK_BEZEICHNUNG];
		} else if ("F_MENGE".equals(fieldName)) {
			value = data[index][REPORT_STATISTIK_MENGE];
		} else if ("F_SNRCHNR".equals(fieldName)) {
			value = data[index][REPORT_STATISTIK_SERIENNUMMER];
		} else if ("F_MATERIALZUSCHLAG".equals(fieldName)) {
			value = data[index][REPORT_STATISTIK_MATERIALZUSCHLAG];
		} else if ("F_SETARTIKEL_TYP".equals(fieldName)) {
			value = data[index][REPORT_STATISTIK_SETARTIKEL_TYP];
		} else if ("F_WERT".equals(fieldName)) {
			// BigDecimal preis = (BigDecimal)
			// data[index][REPORT_STATISTIK_PREIS];
			// BigDecimal menge = (BigDecimal)
			// data[index][REPORT_STATISTIK_MENGE];
			// BigDecimal wert = preis;
			// if (preis != null && menge != null) {
			// wert = preis.multiply(menge);
			// }
			// value = wert;

			value = data[index][REPORT_STATISTIK_WERT];
			BigDecimal wert = (BigDecimal) data[index][REPORT_STATISTIK_PREIS];
			BigDecimal menge = (BigDecimal) data[index][REPORT_STATISTIK_MENGE];
			if (wert != null && menge != null) {
				wert = wert.multiply(menge);
			}
			value = wert;
		} else if ("F_PREIS".equals(fieldName)) {
			BigDecimal preis = (BigDecimal) data[index][REPORT_STATISTIK_PREIS];
			BigDecimal menge = (BigDecimal) data[index][REPORT_STATISTIK_MENGE];
			if (preis != null && menge != null) {
				summePreis = summePreis.add(preis.multiply(menge));
			}
			value = data[index][REPORT_STATISTIK_PREIS];
		} else if ("F_SUMMEPREIS".equals(fieldName)) {
			value = summePreis;
		} else if ("F_VERLEIHTAGE".equals(fieldName)) {
			value = data[index][REPORT_STATISTIK_VERLEIHTAGE];
		} else if ("F_VERLEIHFAKTOR".equals(fieldName)) {
			value = data[index][REPORT_STATISTIK_VERLEIHFAKTOR];
		}
		return value;
	}

	private Object getFieldValueStammblatt(String fieldName) {
		Object value = null;
		if ("F_FAXDW".equals(fieldName)) {
			value = data[index][REPORT_KUNDENSTAMMBLATT_ANSPRECHPARTNER_FAXDW];
		} else if ("F_FUNKTION".equals(fieldName)) {
			value = data[index][REPORT_KUNDENSTAMMBLATT_ANSPRECHPARTNER_FUNKTION];
		} else if ("F_MOBIL".equals(fieldName)) {
			value = data[index][REPORT_KUNDENSTAMMBLATT_ANSPRECHPARTNER_MOBIL];
		} else if ("F_EMAIL".equals(fieldName)) {
			value = data[index][REPORT_KUNDENSTAMMBLATT_ANSPRECHPARTNER_EMAIL];
		} else if ("F_NACHNAME".equals(fieldName)) {
			value = data[index][REPORT_KUNDENSTAMMBLATT_ANSPRECHPARTNER_NACHNAME];
		} else if ("F_TELDW".equals(fieldName)) {
			value = data[index][REPORT_KUNDENSTAMMBLATT_ANSPRECHPARTNER_TELDW];
		} else if ("F_TITEL".equals(fieldName)) {
			value = data[index][REPORT_KUNDENSTAMMBLATT_ANSPRECHPARTNER_TITEL];
		} else if ("F_VORNAME".equals(fieldName)) {
			value = data[index][REPORT_KUNDENSTAMMBLATT_ANSPRECHPARTNER_VORNAME];
		}

		return value;
	}

	private Object getFieldValueMonatsstatistik(String fieldName) {
		Object value = null;
		if ("Jahr".equals(fieldName)) {
			value = data[index][REPORT_MONATSSTATISTIK_JAHR];
		} else if ("Monat".equals(fieldName)) {
			value = data[index][REPORT_MONATSSTATISTIK_MONAT];
		} else if ("Menge".equals(fieldName)) {
			value = data[index][REPORT_MONATSSTATISTIK_MENGE];
		} else if ("Wert".equals(fieldName)) {
			value = data[index][REPORT_MONATSSTATISTIK_WERT];
		}

		return value;
	}

	private Object getFieldValueWartungsauswertung(String fieldName) {
		Object value = null;
		if ("F_LIEFERADRESSE_KUNDE".equals(fieldName)) {
			value = data[index][REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_KUNDE];
		} else if ("F_INTERVALL".equals(fieldName)) {
			value = data[index][REPORT_WARTUNGSAUSWERTUNG_INTERVALL];
		} else if ("F_FAELLIGKEIT".equals(fieldName)) {
			value = data[index][REPORT_WARTUNGSAUSWERTUNG_FAELLIGKEIT];
		} else if ("F_LETZTEPRUEFUNG".equals(fieldName)) {
			value = data[index][REPORT_WARTUNGSAUSWERTUNG_LETZTEPRUEFUNG];
		} else if ("F_LETZTEPRUEFUNGDURCHGEFUEHRT".equals(fieldName)) {
			value = data[index][REPORT_WARTUNGSAUSWERTUNG_LETZTEPRUEFUNGDURCHGEFUEHRT];
		} else if ("F_ARTIKELGRUPPE".equals(fieldName)) {
			value = data[index][REPORT_WARTUNGSAUSWERTUNG_ARTIKELGRUPPE];
		} else if ("F_ARTIKELNUMMER".equals(fieldName)) {
			value = data[index][REPORT_WARTUNGSAUSWERTUNG_ARTIKELNUMMER];
		} else if ("F_ARTIKELBEZEICHNUNG".equals(fieldName)) {
			value = data[index][REPORT_WARTUNGSAUSWERTUNG_BEZEICHNUNG];
		} else if ("F_PRUEFER".equals(fieldName)) {
			value = data[index][REPORT_WARTUNGSAUSWERTUNG_PRUEFER];
		} else if ("F_LETZTERLIEFERSCHEIN".equals(fieldName)) {
			value = data[index][REPORT_WARTUNGSAUSWERTUNG_LETZTEPRUEFUNGDURCHGEFUEHRTLS];
		} else if ("F_LETZTERPRUEFER".equals(fieldName)) {
			value = data[index][REPORT_WARTUNGSAUSWERTUNG_LETZTEPRUEFUNGDURCHGEFUEHRTPERSON];
		} else if ("F_AUFTRAG_AUSLOESER".equals(fieldName)) {
			value = data[index][REPORT_WARTUNGSAUSWERTUNG_AUFTRAG_AUSLOESER];
		} else if ("F_LIEFERADRESSE_LKZ".equals(fieldName)) {
			value = data[index][REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_LKZ];
		} else if ("F_LIEFERADRESSE_ORT".equals(fieldName)) {
			value = data[index][REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_ORT];
		} else if ("F_LIEFERADRESSE_PLZ".equals(fieldName)) {
			value = data[index][REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_PLZ];
		} else if ("F_LIEFERADRESSE_STRASSE".equals(fieldName)) {
			value = data[index][REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_STRASSE];
		} else if ("F_LIEFERADRESSE_FILIALNUMMER".equals(fieldName)) {
			value = data[index][REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_FILIALNUMMER];
		} else if ("F_POSITIONSKOMMENTAR_AUFTRAG".equals(fieldName)) {
			value = data[index][REPORT_WARTUNGSAUSWERTUNG_POSITIONSKOMMENTAR_AUFTRAG];
		} else if ("F_POSITIONSKOMMENTAR_LIEFERSCHEIN".equals(fieldName)) {
			value = data[index][REPORT_WARTUNGSAUSWERTUNG_POSITIONSKOMMENTAR_LIEFERSCHEIN];
		} else if ("F_ARTIKELLIEFERANT".equals(fieldName)) {
			value = data[index][REPORT_WARTUNGSAUSWERTUNG_ARTIKELLIEFERANT];
		}

		return value;
	}

	private Object getFieldValueKundenpreisliste(String fieldName) {
		return jasperPrintTransformer.transformData(index, fieldName);
		//
		// Object value = null;
		// if ("Artikelnummer".equals(fieldName)) {
		// value = data[index][REPORT_KUNDENPREISLISTE_ARTIKELNUMMER];
		// } else if ("Bezeichnung".equals(fieldName)) {
		// value = data[index][REPORT_KUNDENPREISLISTE_BEZEICHNUNG];
		// } else if ("Kurzbezeichnung".equals(fieldName)) {
		// value = data[index][REPORT_KUNDENPREISLISTE_KURZBEZEICHNUNG];
		// } else if ("Zusatzbezeichnung".equals(fieldName)) {
		// value = data[index][REPORT_KUNDENPREISLISTE_ZUSATZBEZEICHNUNG];
		// } else if ("Zusatzbezeichnung2".equals(fieldName)) {
		// value = data[index][REPORT_KUNDENPREISLISTE_ZUSATZBEZEICHNUNG2];
		// } else if ("Versteckt".equals(fieldName)) {
		// value = data[index][REPORT_KUNDENPREISLISTE_VERSTECKT];
		// } else if ("Artikelgruppe".equals(fieldName)) {
		// value = data[index][REPORT_KUNDENPREISLISTE_ARTIKELGRUPPE];
		// } else if ("Artikelklasse".equals(fieldName)) {
		// value = data[index][REPORT_KUNDENPREISLISTE_ARTIKELKLASSE];
		// } else if ("SubreportPreise".equals(fieldName)) {
		// value = data[index][REPORT_KUNDENPREISLISTE_SUBREPORT_PREISE];
		// } else if ("EnthaeltSokos".equals(fieldName)) {
		// value = data[index][REPORT_KUNDENPREISLISTE_ENTHAELT_SOKO];
		// }
		//
		// return value;
	}

	private Object getFieldValueKundenliste(String fieldName) {
		Object value = null;
		if ("F_ABC".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_ABC];
		} else if ("F_ABW_UST_LAND".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_ABW_UST_LAND];
		} else if ("F_ANREDE".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_ANREDE];
		} else if ("F_ANSPRECHPARTNER_ANREDE".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_ANSPRECHPARTNER_ANREDE];
		} else if ("F_ANSPRECHPARTNER_EMAIL".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_ANSPRECHPARTNER_EMAIL];
		} else if ("F_ANSPRECHPARTNER_FAXDW".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_ANSPRECHPARTNER_FAXDW];
		} else if ("F_ANSPRECHPARTNER_MOBIL".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_ANSPRECHPARTNER_MOBIL];
		} else if ("F_ANSPRECHPARTNER_BEMERKUNG".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_ANSPRECHPARTNER_BEMERKUNG];
		} else if ("F_ANSPRECHPARTNER_NACHNAME".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_ANSPRECHPARTNER_NACHNAME];
		} else if ("F_ANSPRECHPARTNER_TELDW".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_ANSPRECHPARTNER_TELDW];
		} else if ("F_ANSPRECHPARTNER_TITEL".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_ANSPRECHPARTNER_TITEL];
		} else if ("F_ANSPRECHPARTNER_VORNAME".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_ANSPRECHPARTNER_VORNAME];
		} else if ("F_ANZAHL_RECHNUNGEN_HEUER".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_ANZAHL_RECHNUNGEN_HEUER];
		} else if ("F_ANZAHL_RECHNUNGEN_VORJAHR".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_ANZAHL_RECHNUNGEN_VORJAHR];
		} else if ("F_BRANCHE".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_BRANCHE];
		} else if ("F_BRIEFANREDE".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_BRIEFANREDE];
		} else if ("F_CNAME1".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_CNAME1];
		} else if ("F_CNAME2".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_CNAME2];
		} else if ("F_CNAME3".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_CNAME3];
		} else if ("F_BEMERKUNG".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_BEMERKUNG];
		} else if ("F_DEBITORENKONTO".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_DEBITORENKONTO];
		} else if ("F_EMAIL".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_EMAIL];
		} else if ("F_ERLOESKONTO".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_ERLOESKONTO];
		} else if ("F_FAX".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_FAX];
		} else if ("F_FIRMENBUCHNUMMER".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_FIRMENBUCHNUMMER];
		} else if ("F_GERICHTSSTAND".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_GERICHTSSTAND];
		} else if ("F_HOMEPAGE".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_HOMEPAGE];
		} else if ("F_INTERESSENT".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_INTERESSENT];
		} else if ("F_KOMMUNIKATIONSSPRACHE".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_KOMMUNIKATIONSSPRACHE];
		} else if ("F_KOSTENSTELLE".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_KOSTENSTELLE];
		} else if ("F_KREDITLIMIT".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_KREDITLIMIT];
		} else if ("F_KURZBEZEICHNUNG".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_KURZBEZEICHNUNG];
		} else if ("F_LAND".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_LAND];
		} else if ("F_LAND_POSTFACH".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_LAND_POSTFACH];
		} else if ("F_LETZTE_BONITAETSPRUEFUNG".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_LETZTE_BONITAETSPRUEFUNG];
		} else if ("F_LIEFERANTENNUMMER".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_LIEFERANTENNUMMER];
		} else if ("F_LIEFERART".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_LIEFERART];
		} else if ("F_MWSTSATZ".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_MWSTSATZ];
		} else if ("F_ORT".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_ORT];
		} else if ("F_ORT_POSTFACH".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_ORT_POSTFACH];
		} else if ("F_PARTNERART".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_PARTNERART];
		} else if ("F_PARTNERKLASSE".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_PARTNERKLASSE];
		} else if ("F_PLZ".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_PLZ];
		} else if ("F_PLZ_POSTFACH".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_PLZ_POSTFACH];
		} else if ("F_POSTFACH".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_POSTFACH];
		} else if ("F_PREISLISTE".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_PREISLISTE];
		} else if ("F_PROVISIONSEMPFAENGER".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_PROVISIONSEMPFAENGER];
		} else if ("F_RABATT".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_RABATT];
		} else if ("F_RECHNUNGSADRESSE_ANREDE".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_RECHNUNGSADRESSE_ANREDE];
		} else if ("F_RECHNUNGSADRESSE_CNAME1".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_RECHNUNGSADRESSE_CNAME1];
		} else if ("F_RECHNUNGSADRESSE_CNAME2".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_RECHNUNGSADRESSE_CNAME2];
		} else if ("F_RECHNUNGSADRESSE_CNAME3".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_RECHNUNGSADRESSE_CNAME3];
		} else if ("F_RECHNUNGSADRESSE_KURZBEZEICHNUNG".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_RECHNUNGSADRESSE_KURZBEZEICHNUNG];
		} else if ("F_RECHNUNGSADRESSE_LAND".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_RECHNUNGSADRESSE_LAND];
		} else if ("F_RECHNUNGSADRESSE_LAND_POSTFACH".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_RECHNUNGSADRESSE_LAND_POSTFACH];
		} else if ("F_RECHNUNGSADRESSE_ORT".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_RECHNUNGSADRESSE_ORT];
		} else if ("F_RECHNUNGSADRESSE_ORT_POSTFACH".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_RECHNUNGSADRESSE_ORT_POSTFACH];
		} else if ("F_RECHNUNGSADRESSE_PLZ".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_RECHNUNGSADRESSE_PLZ];
		} else if ("F_RECHNUNGSADRESSE_PLZ_POSTFACH".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_RECHNUNGSADRESSE_PLZ_POSTFACH];
		} else if ("F_RECHNUNGSADRESSE_POSTFACH".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_RECHNUNGSADRESSE_POSTFACH];
		} else if ("F_RECHNUNGSADRESSE_STRASSE".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_RECHNUNGSADRESSE_STRASSE];
		} else if ("F_RECHNUNGSADRESSE_TITEL".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_RECHNUNGSADRESSE_TITEL];
		} else if ("F_RECHNUNGSADRESSE_UIDNUMMER".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_RECHNUNGSADRESSE_UIDNUMMER];
		} else if ("F_KUNDENLISTE_SELEKTION01".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_SELEKTION01];
		} else if ("F_KUNDENLISTE_SELEKTION02".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_SELEKTION02];
		} else if ("F_KUNDENLISTE_SELEKTION03".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_SELEKTION03];
		} else if ("F_KUNDENLISTE_SELEKTION04".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_SELEKTION04];
		} else if ("F_KUNDENLISTE_SELEKTION05".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_SELEKTION05];
		} else if ("F_KUNDENLISTE_SELEKTION06".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_SELEKTION06];
		} else if ("F_KUNDENLISTE_SELEKTION07".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_SELEKTION07];
		} else if ("F_KUNDENLISTE_SELEKTION08".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_SELEKTION08];
		} else if ("F_KUNDENLISTE_SELEKTION09".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_SELEKTION09];
		} else if ("F_KUNDENLISTE_SELEKTION10".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_SELEKTION10];
		} else if ("F_KUNDENLISTE_SELEKTION11".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_SELEKTION11];
		} else if ("F_KUNDENLISTE_SELEKTION12".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_SELEKTION12];
		} else if ("F_KUNDENLISTE_SELEKTION13".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_SELEKTION13];
		} else if ("F_KUNDENLISTE_SELEKTION14".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_SELEKTION14];
		} else if ("F_KUNDENLISTE_SELEKTION15".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_SELEKTION15];
		} else if ("F_SPEDITEUR".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_SPEDITEUR];
		} else if ("F_STRASSE".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_STRASSE];
		} else if ("F_TELEFON".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_TELEFON];
		} else if ("F_TITEL".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_TITEL];
		} else if ("F_UIDNUMMER".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_UIDNUMMER];
		} else if ("F_UMSATZ_HEUER".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_UMSATZ_HEUER];
		} else if ("F_UMSATZ_VORJAHR".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_UMSATZ_VORJAHR];
		} else if ("F_WAEHRUNG".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_WAEHRUNG];
		} else if ("F_ZAHLUNGSZIEL".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_ZAHLUNGSZIEL];
		} else if ("F_ZESSION".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_ZESSION];
		} else if ("F_KUNDENNUMMER".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_KUNDENNUMMER];
		} else if ("F_SUBREPORT_PROJEKTE".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_SUBREPORT_PROJEKTE];
		} else if ("F_KUNDE_I_ID".equals(fieldName)) {
			value = data[index][REPORT_KUNDENLISTE_KUNDE_I_ID];
		}

		return value;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printWartungsauswertung(java.sql.Timestamp tStichtag,
			boolean bVerdichtet, boolean bSortiertNachArtikellieferant,
			TheClientDto theClientDto) {
		useCase = KundeReportFac.UC_REPORT_KUNDE_WARTUNGSAUSWERTUNG;
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		parameter.put("P_STICHTAG", tStichtag);
		parameter.put("P_VERDICHTET", new Boolean(bVerdichtet));

		if (bSortiertNachArtikellieferant == true) {
			parameter
					.put("P_SORTIERUNG",
							getTextRespectUISpr("lp.artikellieferant",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
		} else {
			parameter
					.put("P_SORTIERUNG",
							getTextRespectUISpr("lp.pruefer",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
		}

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT ap FROM FLRAuftragposition ap WHERE ap.flrauftrag.auftragstatus_c_nr IN ('"
				+ LocaleFac.STATUS_OFFEN
				+ "','"
				+ LocaleFac.STATUS_TEILERLEDIGT
				+ "') "
				+ " AND ap.flrartikel.i_id IS NOT NULL AND ap.flrauftrag.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' AND ap.t_uebersteuerterliefertermin<='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tStichtag
						.getTime()))
				+ "'"
				+ " ORDER BY ap.flrauftrag.flrkunde.flrpartner.c_name1nachnamefirmazeile1 ASC";

		Query inventurliste = session.createQuery(sQuery);

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListIterator = resultList.iterator();

		ArrayList alDaten = new ArrayList();

		while (resultListIterator.hasNext()) {

			FLRAuftragposition pos = (FLRAuftragposition) resultListIterator
					.next();

			Object[] zeile = new Object[20];
			zeile[REPORT_WARTUNGSAUSWERTUNG_PRUEFER] = "";
			java.sql.Timestamp tTermin = new java.sql.Timestamp(pos
					.getT_uebersteuerterliefertermin().getTime());

			zeile[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_KUNDE] = pos
					.getFlrauftrag().getFlrkunde().getFlrpartner()
					.getC_name1nachnamefirmazeile1();

			if (pos.getFlrauftrag().getFlrkunde().getFlrpartner()
					.getFlrlandplzort() != null) {
				zeile[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_LKZ] = pos
						.getFlrauftrag().getFlrkunde().getFlrpartner()
						.getFlrlandplzort().getFlrland().getC_lkz();

				zeile[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_PLZ] = pos
						.getFlrauftrag().getFlrkunde().getFlrpartner()
						.getFlrlandplzort().getC_plz();

				zeile[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_ORT] = pos
						.getFlrauftrag().getFlrkunde().getFlrpartner()
						.getFlrlandplzort().getFlrort().getC_name();

				zeile[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_STRASSE] = pos
						.getFlrauftrag().getFlrkunde().getFlrpartner()
						.getC_strasse();
			}
			zeile[REPORT_WARTUNGSAUSWERTUNG_POSITIONSKOMMENTAR_AUFTRAG] = pos
					.getX_textinhalt();

			PartnerDto partnerDto = getPartnerFac()
					.partnerFindByPrimaryKey(
							pos.getFlrauftrag().getFlrkunde().getFlrpartner()
									.getI_id(), theClientDto);

			zeile[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_FILIALNUMMER] = partnerDto
					.getCFilialnummer();

			zeile[REPORT_WARTUNGSAUSWERTUNG_AUFTRAG_AUSLOESER] = pos
					.getFlrauftrag().getC_nr();

			PersonalDto personalDtoAB = getPersonalFac()
					.personalFindByPrimaryKey(
							pos.getFlrauftrag().getVertreter_i_id(),
							theClientDto);
			zeile[REPORT_WARTUNGSAUSWERTUNG_PRUEFER] = personalDtoAB
					.getPartnerDto().formatAnrede();

			ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(
							pos.getFlrartikel().getI_id(), theClientDto);

			zeile[REPORT_WARTUNGSAUSWERTUNG_ARTIKELNUMMER] = artikelDto
					.getCNr();

			// 1. Artikellieferant einfuegen

			ArtikellieferantDto[] aldtos = getArtikelFac()
					.artikellieferantFindByArtikelIId(artikelDto.getIId(),
							theClientDto);
			if (aldtos != null && aldtos.length > 0) {

				LieferantDto lDto = getLieferantFac()
						.lieferantFindByPrimaryKey(aldtos[0].getLieferantIId(),
								theClientDto);

				zeile[REPORT_WARTUNGSAUSWERTUNG_ARTIKELLIEFERANT] = lDto
						.getPartnerDto().formatAnrede();
			} else {
				zeile[REPORT_WARTUNGSAUSWERTUNG_ARTIKELLIEFERANT] = "";
			}

			if (artikelDto.getArtikelsprDto() != null) {
				zeile[REPORT_WARTUNGSAUSWERTUNG_BEZEICHNUNG] = artikelDto
						.getArtikelsprDto().getCBez();
			}
			int iWartungsintervall = 0;
			if (artikelDto.getIWartungsintervall() != null) {
				iWartungsintervall = artikelDto.getIWartungsintervall();
			}

			// Wenn das Wartungsintervall 0 ist, dann steht sie immer auf
			// der Liste
			if (iWartungsintervall == 0) {
				zeile[REPORT_WARTUNGSAUSWERTUNG_FAELLIGKEIT] = tTermin;
			}

			zeile[REPORT_WARTUNGSAUSWERTUNG_LETZTEPRUEFUNGDURCHGEFUEHRT] = new Boolean(
					true);

			zeile[REPORT_WARTUNGSAUSWERTUNG_INTERVALL] = new Integer(
					iWartungsintervall);

			// Nun die letzte Lieferscheinposition des Artkels mit der
			// Auftragsadresse holen
			Session session2 = FLRSessionFactory.getFactory().openSession();

			String sQuery2 = "SELECT lp FROM FLRLieferscheinposition lp WHERE lp.flrlieferschein.lieferscheinstatus_status_c_nr NOT IN ('"
					+ LocaleFac.STATUS_STORNIERT
					+ "') "
					+ " AND lp.flrartikel.i_id="
					+ artikelDto.getIId()
					+ "AND lp.flrlieferschein.flrkunde="
					+ pos.getFlrauftrag().getFlrkunde().getI_id()
					+ " AND lp.flrlieferschein.d_belegdatum>='"
					+ Helper.formatDateWithSlashes(new java.sql.Date(tTermin
							.getTime()))
					+ "' "
					+ " ORDER BY lp.flrlieferschein.d_belegdatum DESC";

			Query query2 = session2.createQuery(sQuery2);
			query2.setMaxResults(1);
			List<?> resultList2 = query2.list();

			if (resultList2.size() > 0) {
				FLRLieferscheinposition lsPos = (FLRLieferscheinposition) resultList2
						.iterator().next();

				// Faelligkeit berechnen
				java.sql.Timestamp tLetzteWartung = new java.sql.Timestamp(
						lsPos.getFlrlieferschein().getD_belegdatum().getTime());

				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKey(
								lsPos.getFlrlieferschein()
										.getPersonal_i_id_vertreter(),
								theClientDto);

				zeile[REPORT_WARTUNGSAUSWERTUNG_POSITIONSKOMMENTAR_LIEFERSCHEIN] = lsPos
						.getC_textinhalt();

				zeile[REPORT_WARTUNGSAUSWERTUNG_LETZTEPRUEFUNGDURCHGEFUEHRTLS] = lsPos
						.getFlrlieferschein().getC_nr();

				zeile[REPORT_WARTUNGSAUSWERTUNG_LETZTEPRUEFUNG] = tLetzteWartung;
				zeile[REPORT_WARTUNGSAUSWERTUNG_LETZTEPRUEFUNGDURCHGEFUEHRTPERSON] = personalDto
						.getPartnerDto().formatAnrede();

				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(tLetzteWartung.getTime());
				c.set(Calendar.MONTH, c.get(Calendar.MONTH)
						+ iWartungsintervall);

				if (c.getTimeInMillis() <= tStichtag.getTime()) {
					zeile[REPORT_WARTUNGSAUSWERTUNG_FAELLIGKEIT] = new java.sql.Timestamp(
							c.getTimeInMillis());
				} else {
					continue;
				}

				if (lsPos.getN_menge() != null
						&& lsPos.getN_menge().doubleValue() == 0) {
					zeile[REPORT_WARTUNGSAUSWERTUNG_LETZTEPRUEFUNGDURCHGEFUEHRT] = new Boolean(
							false);
				}

			} else {
				zeile[REPORT_WARTUNGSAUSWERTUNG_FAELLIGKEIT] = tTermin;
			}

			session2.close();

			alDaten.add(zeile);
		}
		session.close();

		if (bSortiertNachArtikellieferant == true) {
			for (int k = alDaten.size() - 1; k > 0; --k) {
				for (int j = 0; j < k; ++j) {
					Object[] a1 = (Object[]) alDaten.get(j);
					Object[] a2 = (Object[]) alDaten.get(j + 1);

					String s1 = (String) a1[REPORT_WARTUNGSAUSWERTUNG_ARTIKELLIEFERANT];

					s1 += " "
							+ a1[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_KUNDE];

					if (a1[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_LKZ] != null) {
						s1 += " "
								+ a1[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_LKZ];
					}
					if (a1[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_PLZ] != null) {
						s1 += " "
								+ a1[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_PLZ];
					}
					if (a1[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_ORT] != null) {
						s1 += " "
								+ a1[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_ORT];
					}
					if (a1[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_STRASSE] != null) {
						s1 += " "
								+ a1[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_STRASSE];
					}

					String s2 = (String) a2[REPORT_WARTUNGSAUSWERTUNG_ARTIKELLIEFERANT];
					s2 += " "
							+ a2[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_KUNDE];

					if (a2[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_LKZ] != null) {
						s2 += " "
								+ a2[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_LKZ];
					}
					if (a2[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_PLZ] != null) {
						s2 += " "
								+ a2[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_PLZ];
					}
					if (a2[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_ORT] != null) {
						s2 += " "
								+ a2[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_ORT];
					}
					if (a2[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_STRASSE] != null) {
						s2 += " "
								+ a2[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_STRASSE];
					}

					if (s1.compareTo(s2) > 0) {
						alDaten.set(j, a2);
						alDaten.set(j + 1, a1);
					}
				}

			}
		} else {
			for (int k = alDaten.size() - 1; k > 0; --k) {
				for (int j = 0; j < k; ++j) {
					Object[] a1 = (Object[]) alDaten.get(j);
					Object[] a2 = (Object[]) alDaten.get(j + 1);

					String s1 = (String) a1[REPORT_WARTUNGSAUSWERTUNG_PRUEFER];

					s1 += " "
							+ a1[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_KUNDE];

					if (a1[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_LKZ] != null) {
						s1 += " "
								+ a1[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_LKZ];
					}
					if (a1[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_PLZ] != null) {
						s1 += " "
								+ a1[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_PLZ];
					}
					if (a1[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_ORT] != null) {
						s1 += " "
								+ a1[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_ORT];
					}
					if (a1[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_STRASSE] != null) {
						s1 += " "
								+ a1[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_STRASSE];
					}

					String s2 = (String) a2[REPORT_WARTUNGSAUSWERTUNG_PRUEFER];
					s2 += " "
							+ a2[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_KUNDE];

					if (a2[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_LKZ] != null) {
						s2 += " "
								+ a2[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_LKZ];
					}
					if (a2[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_PLZ] != null) {
						s2 += " "
								+ a2[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_PLZ];
					}
					if (a2[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_ORT] != null) {
						s2 += " "
								+ a2[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_ORT];
					}
					if (a2[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_STRASSE] != null) {
						s2 += " "
								+ a2[REPORT_WARTUNGSAUSWERTUNG_LIEFERADRESSE_STRASSE];
					}

					if (s1.compareTo(s2) > 0) {
						alDaten.set(j, a2);
						alDaten.set(j + 1, a1);
					}
				}

			}
		}

		// Nach Person sortieren, damit danach gruppiert werden kann

		data = new Object[alDaten.size()][20];

		// data = (Object[][]) alDaten.toArray(data);
		setData((Object[][]) alDaten.toArray(data));

		if (bSortiertNachArtikellieferant == true) {
			initJRDS(parameter, KundeReportFac.REPORT_MODUL,
					KundeReportFac.REPORT_WARTUNGSAUSWERTUNG_ARTIKELLIEFERANT,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
		} else {

			initJRDS(parameter, KundeReportFac.REPORT_MODUL,
					KundeReportFac.REPORT_WARTUNGSAUSWERTUNG,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
		}

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public CustomerPricelistReportDto printKundenpreislisteRaw(
			Integer kundeIId, Integer artikelgruppeIId,
			Integer artikelklasseIId, boolean bMitGesperrten,
			String artikelNrVon, String artikelNrBis, boolean bMitVersteckten,
			java.sql.Date datGueltikeitsdatumI, boolean nurSonderkonditionen,
			boolean bMitArtikelbezeichnungenInMandantensprache,
			boolean nurWebshopartikel, TheClientDto theClientDto) {

		CustomerPricelistReportDto returnDto = new CustomerPricelistReportDto();

		useCase = KundeReportFac.UC_REPORT_KUNDE_KUNDENPREISLISTE;
		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId,
				theClientDto);
		if (!kundeDto.getMandantCNr().equals(theClientDto.getMandant())) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FALSCHER_MANDANT,
					"Kunde (i_id = " + kundeIId + ") in Mandant "
							+ theClientDto.getMandant() + " nicht vorhanden");
		}
		// HashMap<String, Object> parameter = new HashMap<String, Object>();
		try {
			returnDto.setCustomer(new IdValueDto(kundeDto.getIId(), kundeDto
					.getPartnerDto().formatAnrede()));

			if (artikelgruppeIId != null) {
				returnDto.setItemgroup(new IdValueDto(artikelgruppeIId,
						getArtikelFac().artgruFindByPrimaryKey(
								artikelgruppeIId, theClientDto).getCNr()));
			}
			if (artikelklasseIId != null) {
				returnDto.setItemclass(new IdValueDto(artikelklasseIId,
						getArtikelFac().artklaFindByPrimaryKey(
								artikelklasseIId, theClientDto).getCNr()));
			}

			returnDto.setOnlyWebshopItems(nurWebshopartikel);
			ParametermandantDto param = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_PREISBASIS_VERKAUF);

			int iPreisbasis = (Integer) param.getCWertAsObject();
			// Mwstsatz aus Artikel

			String sQuery = "SELECT artikelliste.i_id, artikelliste.c_nr, aspr.c_bez, aspr.c_kbez, aspr.c_zbez,aspr.c_zbez2,artikelliste.einheit_c_nr, gruppe.c_nr, "
					+ " klasse.c_nr, artikelliste.b_versteckt,artikelliste.mwstsatz_i_id, gruppe.i_id, shopgruppe, sgspr.c_bez "
					+ " FROM FLRArtikelliste AS artikelliste"
					+ " LEFT OUTER JOIN artikelliste.flrshopgruppe as shopgruppe "
					+ " LEFT OUTER JOIN shopgruppe.shopgruppesprset AS sgspr "
					+ " LEFT OUTER JOIN artikelliste.flrartikelgruppe AS gruppe "
					+ " LEFT OUTER JOIN artikelliste.flrartikelklasse AS klasse "
					// +
					// " LEFT OUTER JOIN artikelliste.artikelsperreset AS sperren "
					+ " LEFT OUTER JOIN artikelliste.artikelsprset AS aspr WHERE artikelliste.mandant_c_nr='"
					+ theClientDto.getMandant()
					+ "' AND artikelliste.artikelart_c_nr NOT IN ('"
					+ ArtikelFac.ARTIKELART_HANDARTIKEL + "') ";

			if (!bMitGesperrten) {
				sQuery += " AND artikelliste.artikelsperreset IS EMPTY";
			}
			if (artikelklasseIId != null) {
				sQuery += " AND klasse.i_id=" + artikelklasseIId.intValue();
			}
			if (artikelgruppeIId != null) {
				sQuery += " AND gruppe.i_id=" + artikelgruppeIId.intValue();
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
			if (nurWebshopartikel) {
				sQuery = sQuery + " AND shopgruppe.i_id IS NOT NULL ";
			}
			sQuery += "ORDER BY artikelliste.c_nr";

			Session session = FLRSessionFactory.getFactory().openSession();
			Query inventurliste = session.createQuery(sQuery);

			session.enableFilter("filterLocale").setParameter("paramLocale",
					kundeDto.getPartnerDto().getLocaleCNrKommunikation());

			List<?> resultList = inventurliste.list();

			Iterator<?> resultListIterator = resultList.iterator();

			// ArrayList alDaten = new ArrayList();
			//
			// int row = 0;

			// int preisNachkommastellen =
			// getMandantFac().getNachkommastellenPreisVK(theClientDto.getMandant())
			// ;
			int preisNachkommastellen = 4;

			while (resultListIterator.hasNext()) {
				Object o[] = (Object[]) resultListIterator.next();

				Integer artikel_i_id = (Integer) o[0];
				Integer artgruIId = (Integer) o[11];

				CustomerPricelistItemDto reportArtikel = new CustomerPricelistItemDto(
						artikel_i_id, (String) o[1]);
				// Integer artikel_i_id = (Integer) o[0];
				// String artikelnummer = (String) o[1];
				// String bezeichnung = (String) o[2];
				// String kurzbezeichnung = (String) o[3];
				// String zusatzbezeichnung = (String) o[4];
				// String zusatzbezeichnung2 = (String) o[5];
				// String einheit = (String) o[6];
				// String gruppe = (String) o[7];
				// String klasse = (String) o[8];
				// Short versteckt = (Short) o[9];
				// Integer mwstsatzIId = (Integer) o[10];

				reportArtikel.setName((String) o[2]);
				reportArtikel.setShortName((String) o[3]);
				reportArtikel.setAdditionalName((String) o[4]);
				reportArtikel.setAdditionalName2((String) o[5]);
				reportArtikel.setUnit((String) o[6]);
				reportArtikel.setItemGroup((String) o[7]);
				reportArtikel.setItemClass((String) o[8]);
				reportArtikel.setHidden((Short) o[9]);
				reportArtikel.setVATId((Integer) o[10]);
				// reportArtikel.setArtikelgruppeId(artgruIId);
				reportArtikel.setSpecialCondition(false);
				reportArtikel.setItemGroupDto(new IdValueDto(artgruIId,
						(String) o[7]));

				if (o[12] != null) {
					FLRShopgruppe flrShopgruppe = (FLRShopgruppe) o[12];
					CustomerPricelistShopgroupDto shopgroupDto = new CustomerPricelistShopgroupDto();
					shopgroupDto.setId(flrShopgruppe.getI_id());
					shopgroupDto.setCnr(flrShopgruppe.getC_nr());
					shopgroupDto.setName(flrShopgruppe.getC_nr());
					shopgroupDto.setName(o[13] != null ? (String) o[13]
							: flrShopgruppe.getC_nr());
					reportArtikel.setShopgroupDto(shopgroupDto);
				}

				BigDecimal preisBasis = null;
				if (iPreisbasis == 0 || iPreisbasis == 2) {

					preisBasis = getVkPreisfindungFac().ermittlePreisbasis(
							artikel_i_id, datGueltikeitsdatumI, null,
							kundeDto.getCWaehrung(), theClientDto);
				} else {

					preisBasis = getVkPreisfindungFac()
							.ermittlePreisbasis(
									artikel_i_id,
									datGueltikeitsdatumI,
									kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
									kundeDto.getCWaehrung(), theClientDto);

				}

				// Preisbasis fuer Menge =1 ist nun definiert

				// int iAnzahlZeilenSubreport = 8;
				// String[] fieldnames = new String[] { "Menge", "Basis",
				// "BasisPreis", "Fixpreis", "Rabattsatz",
				// "BerechneterPreis", "Waehrung", "Soko" };

				// ArrayList al = new ArrayList();

				if (nurSonderkonditionen == false) {
					CustomerPricelistPriceDto preisDto = new CustomerPricelistPriceDto(
							CustomerPricelistPriceDto.PREISTYP_VKPREISBASIS,
							preisNachkommastellen);
					preisDto.setBasePrice(preisBasis);

					// Object[] zeile = new Object[iAnzahlZeilenSubreport];
					// zeile[0] = null;
					// zeile[1] = "VK-Preisbasis";
					// zeile[2] = preisBasis;
					// zeile[3] = null;

					VkPreisfindungPreislisteDto artikelPreisliste = null;

					try {
						artikelPreisliste = getVkPreisfindungFac()
								.getAktuellePreislisteByArtikelIIdPreislisteIId(
										artikel_i_id,
										kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
										new Date(System.currentTimeMillis()),
										kundeDto.getCWaehrung(), theClientDto);
					} catch (Throwable t) {
						// ignore
					}

					if (artikelPreisliste != null) {

						if (artikelPreisliste.getNArtikelfixpreis() != null) {
							// zeile[5] =
							// artikelPreisliste.getNArtikelfixpreis();
							preisDto.setCalculatedPrice(artikelPreisliste
									.getNArtikelfixpreis());
						} else {
							
							BigDecimal standardRabattsatz=artikelPreisliste
									.getNArtikelstandardrabattsatz();
							if(iPreisbasis==1){
								standardRabattsatz=BigDecimal.ZERO;
							}
							
							
							
							// zeile[4] = artikelPreisliste
							// .getNArtikelstandardrabattsatz()
							// .doubleValue();
							preisDto.setDiscountRate(standardRabattsatz.doubleValue());
							if (preisBasis != null) {
								BigDecimal p = getVkPreisfindungFac()
										.berechneVerkaufspreis(
												preisBasis,
												standardRabattsatz.doubleValue()).nettopreis;
								preisDto.setCalculatedPrice(p);
								// zeile[5] = getVkPreisfindungFac()
								// .berechneVerkaufspreis(
								// preisBasis,
								// artikelPreisliste
								// .getNArtikelstandardrabattsatz()
								// .doubleValue()).nettopreis;
							} else {
								// zeile[5] = new BigDecimal(0);
								preisDto.setCalculatedPrice(BigDecimal.ZERO);
							}
						}

					} else {
						// zeile[5] = preisBasis;
						preisDto.setCalculatedPrice(preisBasis);
					}

					// zeile[6] = kundeDto.getCWaehrung();
					// zeile[7] = new Boolean(false);
					// al.add(zeile);

					preisDto.setCurrency(kundeDto.getCWaehrung());
					preisDto.setSpecialCondition(false);
					reportArtikel.getPrices().add(preisDto);

					// Staffelpreis
					VkpfMengenstaffelDto[] vkpfMengenstaffelDtos = getVkPreisfindungFac()
							.vkpfMengenstaffelFindByArtikelIIdGueltigkeitsdatum(
									artikel_i_id,
									datGueltikeitsdatumI,
									kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
									theClientDto);
					for (int i = 0; i < vkpfMengenstaffelDtos.length; i++) {
						VkpfMengenstaffelDto vkpfMengenstaffelDto = vkpfMengenstaffelDtos[i];

						if (vkpfMengenstaffelDto.getVkpfartikelpreislisteIId() == null
								|| vkpfMengenstaffelDto
										.getVkpfartikelpreislisteIId()
										.equals(kundeDto
												.getVkpfArtikelpreislisteIIdStdpreisliste())) {
							BigDecimal preisBasisStaffel = getVkPreisfindungFac()
									.ermittlePreisbasis(
											artikel_i_id,
											datGueltikeitsdatumI,
											vkpfMengenstaffelDto.getNMenge(),
											vkpfMengenstaffelDto
													.getVkpfartikelpreislisteIId(),
											kundeDto.getCWaehrung(),
											theClientDto);

							CustomerPricelistPriceDto staffelpreisDto = new CustomerPricelistPriceDto(
									CustomerPricelistPriceDto.PREISTYP_VKSTAFFELPREIS,
									preisNachkommastellen);
							staffelpreisDto.setAmount(vkpfMengenstaffelDto
									.getNMenge());
							staffelpreisDto.setBasePrice(preisBasisStaffel);
							// zeile = new Object[iAnzahlZeilenSubreport];
							// zeile[0] = vkpfMengenstaffelDto.getNMenge();
							// zeile[1] = "VK-Staffelpreis";
							// zeile[2] = preisBasisStaffel;

							String waehrung = theClientDto
									.getSMandantenwaehrung();

							if (vkpfMengenstaffelDto
									.getVkpfartikelpreislisteIId() != null) {
								waehrung = getVkPreisfindungFac()
										.vkpfartikelpreislisteFindByPrimaryKey(
												vkpfMengenstaffelDto
														.getVkpfartikelpreislisteIId())
										.getWaehrungCNr();
							}

							// zeile[5] = waehrung;

							if (vkpfMengenstaffelDto.getNArtikelfixpreis() != null) {
								// zeile[3] = vkpfMengenstaffelDto
								// .getNArtikelfixpreis();
								// zeile[5] = vkpfMengenstaffelDto
								// .getNArtikelfixpreis();
								// zeile[6] = waehrung;

								staffelpreisDto
										.setFixPrice(vkpfMengenstaffelDto
												.getNArtikelfixpreis());
								staffelpreisDto
										.setCalculatedPrice(vkpfMengenstaffelDto
												.getNArtikelfixpreis());
								staffelpreisDto.setCurrency(waehrung);
							} else {
								// zeile[3] = null;
								// zeile[4] = vkpfMengenstaffelDto
								// .getFArtikelstandardrabattsatz();
								// zeile[5] = getVkPreisfindungFac()
								// .berechneVerkaufspreis(
								// preisBasisStaffel,
								// vkpfMengenstaffelDto
								// .getFArtikelstandardrabattsatz()).nettopreis;
								// zeile[6] = kundeDto.getCWaehrung();
								// zeile[7] = new Boolean(false);

								staffelpreisDto
										.setDiscountRate(vkpfMengenstaffelDto
												.getFArtikelstandardrabattsatz());
								staffelpreisDto
										.setCalculatedPrice(getVkPreisfindungFac()
												.berechneVerkaufspreis(
														preisBasisStaffel,
														vkpfMengenstaffelDto
																.getFArtikelstandardrabattsatz()).nettopreis);
								staffelpreisDto.setCurrency(kundeDto
										.getCWaehrung());
							}

							// al.add(zeile);
							reportArtikel.getPrices().add(staffelpreisDto);
						}
					}
				}

				// Soko Artikel
				KundesokoDto kundeSokoArtikel = getKundesokoFac()
						.kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc(
								kundeIId, artikel_i_id, datGueltikeitsdatumI);

				if (kundeSokoArtikel != null) {
					// oZeile[REPORT_KUNDENPREISLISTE_ENTHAELT_SOKO] = new
					// Boolean(
					// true);
					reportArtikel.setSpecialCondition(true);

					KundesokomengenstaffelDto[] kundesokomengenstaffelDto = getKundesokoFac()
							.kundesokomengenstaffelFindByKundesokoIIdInZielWaehrung(
									kundeSokoArtikel.getIId(),
									datGueltikeitsdatumI,
									kundeDto.getCWaehrung(), theClientDto);

					for (int u = 0; u < kundesokomengenstaffelDto.length; u++) {

						KundesokomengenstaffelDto kdsDto = kundesokomengenstaffelDto[u];

						CustomerPricelistPriceDto sokopreisDto = new CustomerPricelistPriceDto(
								CustomerPricelistPriceDto.PREISTYP_SOKOARTIKEL,
								preisNachkommastellen);
						sokopreisDto.setSpecialCondition(true);
						sokopreisDto.setBasePrice(preisBasis);
						// Object[] zeile = new Object[iAnzahlZeilenSubreport];
						// zeile[0] = kdsDto.getNMenge();
						// zeile[1] = "Soko-Artikel";
						// zeile[2] = preisBasis;

						if (kdsDto.getNArtikelfixpreis() != null) {
							// zeile[3] = kdsDto.getNArtikelfixpreis();
							// zeile[5] = kdsDto.getNArtikelfixpreis();
							sokopreisDto.setFixPrice(kdsDto
									.getNArtikelfixpreis());
							sokopreisDto.setCalculatedPrice(kdsDto
									.getNArtikelfixpreis());
						} else {
							// zeile[4] =
							// kdsDto.getFArtikelstandardrabattsatz();
							sokopreisDto.setDiscountRate(kdsDto
									.getFArtikelstandardrabattsatz());

							BigDecimal nPreisbasis = null;
							if (iPreisbasis == 0 || iPreisbasis == 2) {

								// WH 21.06.06 Es gilt die VK-Basis, die zu
								// Beginn
								// der Mengenstaffel gueltig ist
								nPreisbasis = getVkPreisfindungFac()
										.ermittlePreisbasis(artikel_i_id,
												datGueltikeitsdatumI, null,
												kundeDto.getCWaehrung(),
												theClientDto);
							} else {
								nPreisbasis = getVkPreisfindungFac()
										.ermittlePreisbasis(
												artikel_i_id,
												datGueltikeitsdatumI,
												kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
												kundeDto.getCWaehrung(),
												theClientDto);
							}

							if (nPreisbasis != null) {
								// zeile[6] = kundeDto.getCWaehrung();
								// zeile[5] = getVkPreisfindungFac()
								// .berechneVerkaufspreis(
								// nPreisbasis,
								// kdsDto.getFArtikelstandardrabattsatz()).nettopreis;
								sokopreisDto
										.setCalculatedPrice(getVkPreisfindungFac()
												.berechneVerkaufspreis(
														nPreisbasis,
														kdsDto.getFArtikelstandardrabattsatz()).nettopreis);
							}
						}

						// zeile[6] = kundeDto.getCWaehrung();
						sokopreisDto.setCurrency(kundeDto.getCWaehrung());
						sokopreisDto.setAmount(kdsDto.getNMenge());

						// al.add(zeile);
						reportArtikel.getPrices().add(sokopreisDto);
					}
				}

				// Soko Artikelgruppe
				if (artgruIId != null) {
					KundesokoDto kundeSokoArtGru = getKundesokoFac()
							.kundesokoFindByKundeIIdArtgruIIdGueltigkeitsdatumOhneExc(
									kundeIId, artgruIId, datGueltikeitsdatumI);
					if (kundeSokoArtGru != null) {
						KundesokomengenstaffelDto[] kundesokomengenstaffelDto = getKundesokoFac()
								.kundesokomengenstaffelFindByKundesokoIIdInZielWaehrung(
										kundeSokoArtGru.getIId(),
										datGueltikeitsdatumI,
										kundeDto.getCWaehrung(), theClientDto);

						for (int u = 0; u < kundesokomengenstaffelDto.length; u++) {

							KundesokomengenstaffelDto kdsDto = kundesokomengenstaffelDto[u];

							CustomerPricelistPriceDto sokopreisDto = new CustomerPricelistPriceDto(
									CustomerPricelistPriceDto.PREISTYP_SOKOARTIKELGRUPPE,
									preisNachkommastellen);
							sokopreisDto.setSpecialCondition(true);
							sokopreisDto.setAmount(kdsDto.getNMenge());
							sokopreisDto.setBasePrice(preisBasis);
							// Object[] zeile = new
							// Object[iAnzahlZeilenSubreport];
							// zeile[0] = kdsDto.getNMenge();
							// zeile[1] = "Soko-Artikelgruppe";
							// zeile[2] = preisBasis;

							if (kdsDto.getNArtikelfixpreis() != null) {
								sokopreisDto.setFixPrice(kdsDto
										.getNArtikelfixpreis());
								sokopreisDto.setCalculatedPrice(kdsDto
										.getNArtikelfixpreis());
								// zeile[3] = kdsDto.getNArtikelfixpreis();
								// zeile[5] = kdsDto.getNArtikelfixpreis();
							} else {
								sokopreisDto.setDiscountRate(kdsDto
										.getFArtikelstandardrabattsatz());
								// zeile[4] = kdsDto
								// .getFArtikelstandardrabattsatz();

								BigDecimal nPreisbasis = null;
								if (iPreisbasis == 0 || iPreisbasis == 2) {

									// WH 21.06.06 Es gilt die VK-Basis, die
									// zu
									// Beginn
									// der Mengenstaffel gueltig ist
									nPreisbasis = getVkPreisfindungFac()
											.ermittlePreisbasis(artikel_i_id,
													datGueltikeitsdatumI, null,
													kundeDto.getCWaehrung(),
													theClientDto);
								} else {
									nPreisbasis = getVkPreisfindungFac()
											.ermittlePreisbasis(
													artikel_i_id,
													datGueltikeitsdatumI,
													kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
													kundeDto.getCWaehrung(),
													theClientDto);
								}

								if (nPreisbasis != null) {
									sokopreisDto
											.setCalculatedPrice(getVkPreisfindungFac()
													.berechneVerkaufspreis(
															nPreisbasis,
															kdsDto.getFArtikelstandardrabattsatz()).nettopreis);
									// zeile[5] = getVkPreisfindungFac()
									// .berechneVerkaufspreis(
									// nPreisbasis,
									// kdsDto.getFArtikelstandardrabattsatz()).nettopreis;
								}
							}

							sokopreisDto.setCurrency(kundeDto.getCWaehrung());
							// zeile[6] = kundeDto.getCWaehrung();

							reportArtikel.getPrices().add(sokopreisDto);
							// al.add(zeile);
						}
					}
				}

				// Object[][] dataSub = new
				// Object[al.size()][fieldnames.length];
				// dataSub = (Object[][]) al.toArray(dataSub);
				//
				// oZeile[REPORT_KUNDENPREISLISTE_SUBREPORT_PREISE] = new
				// LPDatenSubreport(
				// dataSub, fieldnames);
				// alDaten.add(oZeile);

				if (bMitArtikelbezeichnungenInMandantensprache) {
					// Object[] oZeileMand = oZeile.clone();

					Artikelspr artikelspr = em.find(
							Artikelspr.class,
							new ArtikelsprPK(artikel_i_id, theClientDto
									.getLocUiAsString()));
					CustomerPricelistItemDescriptionDto bezDto = new CustomerPricelistItemDescriptionDto();
					if (artikelspr != null) {
						bezDto.setName(artikelspr.getCBez());
						bezDto.setShortName(artikelspr.getCKbez());
						bezDto.setAdditionalName(artikelspr.getCZbez());
						bezDto.setAdditionalName2(artikelspr.getCZbez2());
						// oZeileMand[REPORT_KUNDENPREISLISTE_BEZEICHNUNG] =
						// artikelspr
						// .getCBez();
						// oZeileMand[REPORT_KUNDENPREISLISTE_KURZBEZEICHNUNG] =
						// artikelspr
						// .getCKbez();
						// oZeileMand[REPORT_KUNDENPREISLISTE_ZUSATZBEZEICHNUNG]
						// = artikelspr
						// .getCZbez();
						// oZeileMand[REPORT_KUNDENPREISLISTE_ZUSATZBEZEICHNUNG2]
						// = artikelspr
						// .getCZbez2();
						// } else {
						// oZeileMand[REPORT_KUNDENPREISLISTE_BEZEICHNUNG] =
						// null;
						// oZeileMand[REPORT_KUNDENPREISLISTE_KURZBEZEICHNUNG] =
						// null;
						// oZeileMand[REPORT_KUNDENPREISLISTE_ZUSATZBEZEICHNUNG]
						// = null;
						// oZeileMand[REPORT_KUNDENPREISLISTE_ZUSATZBEZEICHNUNG2]
						// = null;
					}
					reportArtikel.setClientDescriptionDto(bezDto);
					// alDaten.add(oZeileMand);
				}

				if (!nurSonderkonditionen
						|| (nurSonderkonditionen & reportArtikel
								.getSpecialCondition())) {
					returnDto.getItems().add(reportArtikel);
				}
			}

			session.close();

			// Object[][] dataTemp = new Object[1][1];
			// data = (Object[][]) alDaten.toArray(dataTemp);
			// parameter.put("P_ARTIKELNRVON", artikelNrVon);
			// parameter.put("P_ARTIKELNRBIS", artikelNrBis);
			// parameter.put("P_MITVERSTECKTEN", new Boolean(bMitVersteckten));
			// parameter.put("P_NURSOKO", new Boolean(nurSonderkonditionen));
			// parameter.put("P_MITMANDANTENSPRACHE", new Boolean(
			// bMitArtikelbezeichnungenInMandantensprache));
			//
			// parameter.put("P_PREISGUELTIGKEIT", datGueltikeitsdatumI);

			returnDto.setItemRangeFrom(artikelNrVon);
			returnDto.setItemRangeTo(artikelNrBis);
			returnDto.setWithHidden(bMitVersteckten);
			returnDto.setOnlySpecialCondition(nurSonderkonditionen);
			returnDto
					.setWithClientLanguage(bMitArtikelbezeichnungenInMandantensprache);
			returnDto.setPriceValidityMs(datGueltikeitsdatumI.getTime());
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
			// } catch(Throwable t) {
			// System.out.println("uups " + t.getMessage()) ;
		}
		// initJRDS(parameter, KundeReportFac.REPORT_MODUL,
		// KundeReportFac.REPORT_KUNDENPREISLISTE,
		// theClientDto.getMandant(), theClientDto.getLocUi(),
		// theClientDto);
		return returnDto;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printKundenpreisliste(Integer kundeIId,
			Integer artikelgruppeIId, Integer artikelklasseIId,
			boolean bMitGesperrten, String artikelNrVon, String artikelNrBis,
			boolean bMitVersteckten, java.sql.Date datGueltikeitsdatumI,
			boolean nurSonderkonditionen,
			boolean bMitArtikelbezeichnungenInMandantensprache,
			TheClientDto theClientDto) {
		boolean useOld = false;
		if (useOld) {
			return printKundenpreislisteOld(kundeIId, artikelgruppeIId,
					artikelklasseIId, bMitGesperrten, artikelNrVon,
					artikelNrBis, bMitVersteckten, datGueltikeitsdatumI,
					nurSonderkonditionen,
					bMitArtikelbezeichnungenInMandantensprache, theClientDto);
		}

		CustomerPricelistReportDto reportDto = printKundenpreislisteRaw(
				kundeIId, artikelgruppeIId, artikelklasseIId, bMitGesperrten,
				artikelNrVon, artikelNrBis, bMitVersteckten,
				datGueltikeitsdatumI, nurSonderkonditionen,
				bMitArtikelbezeichnungenInMandantensprache, false, theClientDto);

		setDataTransformer(new KundenpreislisteTransformer(reportDto));
		initJRDS(jasperPrintTransformer.transformParameter(),
				KundeReportFac.REPORT_MODUL,
				KundeReportFac.REPORT_KUNDENPREISLISTE,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();
	}

	private JasperPrintLP printKundenpreislisteOld(Integer kundeIId,
			Integer artikelgruppeIId, Integer artikelklasseIId,
			boolean bMitInaktiven, String artikelNrVon, String artikelNrBis,
			boolean bMitVersteckten, java.sql.Date datGueltikeitsdatumI,
			boolean nurSonderkonditionen,
			boolean bMitArtikelbezeichnungenInMandantensprache,
			TheClientDto theClientDto) {
		useCase = KundeReportFac.UC_REPORT_KUNDE_KUNDENPREISLISTE;
		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId,
				theClientDto);
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		try {

			parameter.put("P_KUNDE", kundeDto.getPartnerDto().formatAnrede());

			if (artikelgruppeIId != null) {
				parameter.put("P_ARTIKELGRUPPE", getArtikelFac()
						.artgruFindByPrimaryKey(artikelgruppeIId, theClientDto)
						.getCNr());
			}
			if (artikelklasseIId != null) {
				parameter.put("P_ARTIKELKLASSE", getArtikelFac()
						.artklaFindByPrimaryKey(artikelklasseIId, theClientDto)
						.getCNr());
			}

			ParametermandantDto param = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_PREISBASIS_VERKAUF);

			int iPreisbasis = (Integer) param.getCWertAsObject();

			// Mwstsatz aus Artikel

			String sQuery = "SELECT artikelliste.i_id, artikelliste.c_nr, aspr.c_bez, aspr.c_kbez, aspr.c_zbez,aspr.c_zbez2,artikelliste.einheit_c_nr, gruppe.c_nr, klasse.c_nr, artikelliste.b_versteckt,artikelliste.mwstsatz_i_id, gruppe.i_id "
					+ " FROM FLRArtikelliste AS artikelliste"
					+ " LEFT OUTER JOIN artikelliste.flrartikelgruppe AS gruppe "
					+ " LEFT OUTER JOIN artikelliste.flrartikelklasse AS klasse "
					+ " LEFT OUTER JOIN artikelliste.artikelsprset AS aspr WHERE artikelliste.mandant_c_nr='"
					+ theClientDto.getMandant()
					+ "' AND artikelliste.artikelart_c_nr NOT IN ('"
					+ ArtikelFac.ARTIKELART_HANDARTIKEL + "') ";

			if (artikelklasseIId != null) {
				sQuery += " AND klasse.i_id=" + artikelklasseIId.intValue();
			}
			if (artikelgruppeIId != null) {
				sQuery += " AND gruppe.i_id=" + artikelgruppeIId.intValue();
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

			Session session = FLRSessionFactory.getFactory().openSession();
			Query inventurliste = session.createQuery(sQuery);

			session.enableFilter("filterLocale").setParameter("paramLocale",
					kundeDto.getPartnerDto().getLocaleCNrKommunikation());

			List<?> resultList = inventurliste.list();

			Iterator<?> resultListIterator = resultList.iterator();

			ArrayList alDaten = new ArrayList();

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
				Integer mwstsatzIId = (Integer) o[10];
				Integer artgruIId = (Integer) o[11];

				Object[] oZeile = new Object[REPORT_KUNDENPREISLISTE_ANZAHL_SPALTEN];

				oZeile[REPORT_KUNDENPREISLISTE_ARTIKELNUMMER] = artikelnummer;
				oZeile[REPORT_KUNDENPREISLISTE_BEZEICHNUNG] = bezeichnung;
				oZeile[REPORT_KUNDENPREISLISTE_KURZBEZEICHNUNG] = kurzbezeichnung;
				oZeile[REPORT_KUNDENPREISLISTE_ZUSATZBEZEICHNUNG] = zusatzbezeichnung;
				oZeile[REPORT_KUNDENPREISLISTE_ZUSATZBEZEICHNUNG2] = zusatzbezeichnung2;
				oZeile[REPORT_KUNDENPREISLISTE_ARTIKELGRUPPE] = gruppe;
				oZeile[REPORT_KUNDENPREISLISTE_ARTIKELKLASSE] = klasse;
				oZeile[REPORT_KUNDENPREISLISTE_VERSTECKT] = Helper
						.short2Boolean(versteckt);
				oZeile[REPORT_KUNDENPREISLISTE_ENTHAELT_SOKO] = new Boolean(
						false);

				BigDecimal preisBasis = getVkPreisfindungFac()
						.ermittlePreisbasis(artikel_i_id, datGueltikeitsdatumI,
								null, kundeDto.getCWaehrung(), theClientDto);

				// Preisbasis fuer Menge =1 ist nun definiert

				int iAnzahlZeilenSubreport = 8;
				String[] fieldnames = new String[] { "Menge", "Basis",
						"BasisPreis", "Fixpreis", "Rabattsatz",
						"BerechneterPreis", "Waehrung", "Soko" };

				ArrayList al = new ArrayList();

				if (nurSonderkonditionen == false) {

					Object[] zeile = new Object[iAnzahlZeilenSubreport];
					zeile[0] = null;
					zeile[1] = "VK-Preisbasis";
					zeile[2] = preisBasis;
					zeile[3] = null;

					VkPreisfindungPreislisteDto artikelPreisliste = null;

					try {
						artikelPreisliste = getVkPreisfindungFac()
								.getAktuellePreislisteByArtikelIIdPreislisteIId(
										artikel_i_id,
										kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
										new Date(System.currentTimeMillis()),
										kundeDto.getCWaehrung(), theClientDto);
					} catch (Throwable t) {
						// ignore
					}

					if (artikelPreisliste != null) {

						if (artikelPreisliste.getNArtikelfixpreis() != null) {
							zeile[5] = artikelPreisliste.getNArtikelfixpreis();
						} else {
							zeile[4] = artikelPreisliste
									.getNArtikelstandardrabattsatz()
									.doubleValue();

							if (preisBasis != null) {

								zeile[5] = getVkPreisfindungFac()
										.berechneVerkaufspreis(
												preisBasis,
												artikelPreisliste
														.getNArtikelstandardrabattsatz()
														.doubleValue()).nettopreis;
							} else {
								zeile[5] = new BigDecimal(0);
							}
						}

					} else {

						zeile[5] = preisBasis;
					}

					zeile[6] = kundeDto.getCWaehrung();
					zeile[7] = new Boolean(false);

					al.add(zeile);

					// Staffelpreis
					VkpfMengenstaffelDto[] vkpfMengenstaffelDtos = getVkPreisfindungFac()
							.vkpfMengenstaffelFindByArtikelIIdGueltigkeitsdatum(
									artikel_i_id,
									datGueltikeitsdatumI,
									kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
									theClientDto);
					for (int i = 0; i < vkpfMengenstaffelDtos.length; i++) {
						VkpfMengenstaffelDto vkpfMengenstaffelDto = vkpfMengenstaffelDtos[i];

						if (vkpfMengenstaffelDto.getVkpfartikelpreislisteIId() == null
								|| vkpfMengenstaffelDto
										.getVkpfartikelpreislisteIId()
										.equals(kundeDto
												.getVkpfArtikelpreislisteIIdStdpreisliste())) {
							BigDecimal preisBasisStaffel = getVkPreisfindungFac()
									.ermittlePreisbasis(
											artikel_i_id,
											datGueltikeitsdatumI,
											vkpfMengenstaffelDto.getNMenge(),
											vkpfMengenstaffelDto
													.getVkpfartikelpreislisteIId(),
											kundeDto.getCWaehrung(),
											theClientDto);

							zeile = new Object[iAnzahlZeilenSubreport];
							zeile[0] = vkpfMengenstaffelDto.getNMenge();
							zeile[1] = "VK-Staffelpreis";
							zeile[2] = preisBasisStaffel;

							String waehrung = theClientDto
									.getSMandantenwaehrung();

							if (vkpfMengenstaffelDto
									.getVkpfartikelpreislisteIId() != null) {
								waehrung = getVkPreisfindungFac()
										.vkpfartikelpreislisteFindByPrimaryKey(
												vkpfMengenstaffelDto
														.getVkpfartikelpreislisteIId())
										.getWaehrungCNr();
							}

							zeile[5] = waehrung;
							if (vkpfMengenstaffelDto.getNArtikelfixpreis() != null) {
								zeile[3] = vkpfMengenstaffelDto
										.getNArtikelfixpreis();
								zeile[5] = vkpfMengenstaffelDto
										.getNArtikelfixpreis();
								zeile[6] = waehrung;

							} else {

								zeile[3] = null;
								zeile[4] = vkpfMengenstaffelDto
										.getFArtikelstandardrabattsatz();
								zeile[5] = getVkPreisfindungFac()
										.berechneVerkaufspreis(
												preisBasisStaffel,
												vkpfMengenstaffelDto
														.getFArtikelstandardrabattsatz()).nettopreis;
								zeile[6] = kundeDto.getCWaehrung();
								zeile[7] = new Boolean(false);
							}

							al.add(zeile);
						}

					}

				}

				// Soko Artikel
				KundesokoDto kundeSokoArtikel = getKundesokoFac()
						.kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc(
								kundeIId, artikel_i_id, datGueltikeitsdatumI);

				if (kundeSokoArtikel != null) {
					oZeile[REPORT_KUNDENPREISLISTE_ENTHAELT_SOKO] = new Boolean(
							true);
					KundesokomengenstaffelDto[] kundesokomengenstaffelDto = getKundesokoFac()
							.kundesokomengenstaffelFindByKundesokoIIdInZielWaehrung(
									kundeSokoArtikel.getIId(),
									datGueltikeitsdatumI,
									kundeDto.getCWaehrung(), theClientDto);

					for (int u = 0; u < kundesokomengenstaffelDto.length; u++) {

						KundesokomengenstaffelDto kdsDto = kundesokomengenstaffelDto[u];

						Object[] zeile = new Object[iAnzahlZeilenSubreport];
						zeile[0] = kdsDto.getNMenge();
						zeile[1] = "Soko-Artikel";
						zeile[2] = preisBasis;

						if (kdsDto.getNArtikelfixpreis() != null) {
							zeile[3] = kdsDto.getNArtikelfixpreis();
							zeile[5] = kdsDto.getNArtikelfixpreis();
						} else {
							zeile[4] = kdsDto.getFArtikelstandardrabattsatz();

							BigDecimal nPreisbasis = null;
							if (iPreisbasis == 0 || iPreisbasis == 2) {

								// WH 21.06.06 Es gilt die VK-Basis, die zu
								// Beginn
								// der Mengenstaffel gueltig ist
								nPreisbasis = getVkPreisfindungFac()
										.ermittlePreisbasis(artikel_i_id,
												datGueltikeitsdatumI, null,
												kundeDto.getCWaehrung(),
												theClientDto);
							} else {
								nPreisbasis = getVkPreisfindungFac()
										.ermittlePreisbasis(
												artikel_i_id,
												datGueltikeitsdatumI,
												kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
												kundeDto.getCWaehrung(),
												theClientDto);
							}

							if (nPreisbasis != null) {
								zeile[6] = kundeDto.getCWaehrung();
								zeile[5] = getVkPreisfindungFac()
										.berechneVerkaufspreis(
												nPreisbasis,
												kdsDto.getFArtikelstandardrabattsatz()).nettopreis;
							}

						}

						zeile[6] = kundeDto.getCWaehrung();

						al.add(zeile);
					}

					// Soko Artikelgruppe
					if (artgruIId != null) {
						KundesokoDto kundeSokoArtGru = getKundesokoFac()
								.kundesokoFindByKundeIIdArtgruIIdGueltigkeitsdatumOhneExc(
										kundeIId, artgruIId,
										datGueltikeitsdatumI);

						if (kundeSokoArtGru != null) {
							kundesokomengenstaffelDto = getKundesokoFac()
									.kundesokomengenstaffelFindByKundesokoIIdInZielWaehrung(
											kundeSokoArtGru.getIId(),
											datGueltikeitsdatumI,
											kundeDto.getCWaehrung(),
											theClientDto);

							for (int u = 0; u < kundesokomengenstaffelDto.length; u++) {

								KundesokomengenstaffelDto kdsDto = kundesokomengenstaffelDto[u];

								Object[] zeile = new Object[iAnzahlZeilenSubreport];
								zeile[0] = kdsDto.getNMenge();
								zeile[1] = "Soko-Artikelgruppe";
								zeile[2] = preisBasis;

								if (kdsDto.getNArtikelfixpreis() != null) {
									zeile[3] = kdsDto.getNArtikelfixpreis();
									zeile[5] = kdsDto.getNArtikelfixpreis();
								} else {
									zeile[4] = kdsDto
											.getFArtikelstandardrabattsatz();

									BigDecimal nPreisbasis = null;
									if (iPreisbasis == 0 || iPreisbasis == 2) {

										// WH 21.06.06 Es gilt die VK-Basis, die
										// zu
										// Beginn
										// der Mengenstaffel gueltig ist
										nPreisbasis = getVkPreisfindungFac()
												.ermittlePreisbasis(
														artikel_i_id,
														datGueltikeitsdatumI,
														null,
														kundeDto.getCWaehrung(),
														theClientDto);
									} else {
										nPreisbasis = getVkPreisfindungFac()
												.ermittlePreisbasis(
														artikel_i_id,
														datGueltikeitsdatumI,
														kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
														kundeDto.getCWaehrung(),
														theClientDto);
									}

									if (nPreisbasis != null) {

										zeile[5] = getVkPreisfindungFac()
												.berechneVerkaufspreis(
														nPreisbasis,
														kdsDto.getFArtikelstandardrabattsatz()).nettopreis;
									}

								}

								zeile[6] = kundeDto.getCWaehrung();

								al.add(zeile);
							}

						}
					}

				}
				Object[][] dataSub = new Object[al.size()][fieldnames.length];
				dataSub = (Object[][]) al.toArray(dataSub);

				oZeile[REPORT_KUNDENPREISLISTE_SUBREPORT_PREISE] = new LPDatenSubreport(
						dataSub, fieldnames);
				alDaten.add(oZeile);

				if (bMitArtikelbezeichnungenInMandantensprache) {
					Object[] oZeileMand = oZeile.clone();

					Artikelspr artikelspr = em.find(
							Artikelspr.class,
							new ArtikelsprPK(artikel_i_id, theClientDto
									.getLocUiAsString()));
					if (artikelspr != null) {
						oZeileMand[REPORT_KUNDENPREISLISTE_BEZEICHNUNG] = artikelspr
								.getCBez();
						oZeileMand[REPORT_KUNDENPREISLISTE_KURZBEZEICHNUNG] = artikelspr
								.getCKbez();
						oZeileMand[REPORT_KUNDENPREISLISTE_ZUSATZBEZEICHNUNG] = artikelspr
								.getCZbez();
						oZeileMand[REPORT_KUNDENPREISLISTE_ZUSATZBEZEICHNUNG2] = artikelspr
								.getCZbez2();
					} else {
						oZeileMand[REPORT_KUNDENPREISLISTE_BEZEICHNUNG] = null;
						oZeileMand[REPORT_KUNDENPREISLISTE_KURZBEZEICHNUNG] = null;
						oZeileMand[REPORT_KUNDENPREISLISTE_ZUSATZBEZEICHNUNG] = null;
						oZeileMand[REPORT_KUNDENPREISLISTE_ZUSATZBEZEICHNUNG2] = null;
					}

					alDaten.add(oZeileMand);

				}

			}

			session.close();

			Object[][] dataTemp = new Object[1][1];
			data = (Object[][]) alDaten.toArray(dataTemp);
			parameter.put("P_ARTIKELNRVON", artikelNrVon);
			parameter.put("P_ARTIKELNRBIS", artikelNrBis);
			parameter.put("P_MITVERSTECKTEN", new Boolean(bMitVersteckten));
			parameter.put("P_NURSOKO", new Boolean(nurSonderkonditionen));
			parameter.put("P_MITMANDANTENSPRACHE", new Boolean(
					bMitArtikelbezeichnungenInMandantensprache));

			parameter.put("P_PREISGUELTIGKEIT", datGueltikeitsdatumI);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		initJRDS(parameter, KundeReportFac.REPORT_MODUL,
				KundeReportFac.REPORT_KUNDENPREISLISTE,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printKundenliste(TheClientDto theClientDto,
			boolean bUmsatzNachStatistikadresse, boolean bMitVersteckten,
			boolean bMitInteressenten, boolean bMitAnsprechpartner,
			Integer kundeIIdSelektiert, int iProjektemitdrucken, String cPlz,
			Integer landIId, Integer brancheIId, Integer partnerklasseIId) {
		useCase = KundeReportFac.UC_REPORT_KUNDE_KUNDENLISTE;
		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session.createCriteria(FLRKunde.class);
		crit.createAlias(KundeFac.FLR_PARTNER, "p");
		crit.createAlias("p." + PartnerFac.FLR_PARTNER_FLRLANDPLZORT,
				"landplzort", CriteriaSpecification.LEFT_JOIN);
		crit.createAlias("landplzort.flrland", "land",
				CriteriaSpecification.LEFT_JOIN);

		// SP3266
		crit.add(Restrictions.eq(KundeFac.FLR_KUNDE_B_VERSTECKTERLIEFERANT,
				Helper.boolean2Short(false)));

		if (kundeIIdSelektiert != null) {
			crit.add(Restrictions.eq("i_id", kundeIIdSelektiert));
		}

		if (cPlz != null) {
			crit.add(Restrictions.like("landplzort.c_plz", cPlz + "%"));
		}

		if (landIId != null) {
			crit.add(Restrictions.eq("land.i_id", landIId));
		}
		if (brancheIId != null) {
			crit.add(Restrictions.eq("p.branche_i_id", brancheIId));
		}
		if (partnerklasseIId != null) {
			crit.add(Restrictions.eq("p.partnerklasse_i_id", partnerklasseIId));
		}

		crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));
		crit.addOrder(Order.asc("p."
				+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
		if (bMitVersteckten == false) {
			crit.add(Restrictions.eq("p." + PartnerFac.FLR_PARTNER_VERSTECKT,
					Helper.boolean2Short(false)));
		}
		if (bMitInteressenten == false) {
			crit.add(Restrictions.eq(KundeFac.FLR_KUNDE_B_ISTINTERESSENT,
					Helper.boolean2Short(false)));
		}
		ArrayList<Object[]> daten = new ArrayList<Object[]>();
		List<?> list = crit.list();
		Iterator<?> resultListIterator = list.iterator();
		while (resultListIterator.hasNext()) {
			Object[] zeile = new Object[92];
			FLRKunde kunde = (FLRKunde) resultListIterator.next();

			try {
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						kunde.getI_id(), theClientDto);
				zeile[REPORT_KUNDENLISTE_ABC] = kundeDto.getCAbc();
				zeile[REPORT_KUNDENLISTE_KUNDE_I_ID] = kundeDto.getIId();

				if (kundeDto.getPartnerDto().getLandIIdAbweichendesustland() != null) {
					zeile[REPORT_KUNDENLISTE_ABW_UST_LAND] = getSystemFac()
							.landFindByPrimaryKey(
									kundeDto.getPartnerDto()
											.getLandIIdAbweichendesustland())
							.getCLkz();
				}
				zeile[REPORT_KUNDENLISTE_ANREDE] = kundeDto.getPartnerDto()
						.getAnredeCNr();

				if (kundeDto.getPartnerDto().getBrancheIId() != null) {
					zeile[REPORT_KUNDENLISTE_BRANCHE] = getPartnerServicesFac()
							.brancheFindByPrimaryKey(
									kundeDto.getPartnerDto().getBrancheIId(),
									theClientDto).getBezeichnung();
				}

				zeile[REPORT_KUNDENLISTE_CNAME1] = kundeDto.getPartnerDto()
						.getCName1nachnamefirmazeile1();
				zeile[REPORT_KUNDENLISTE_CNAME2] = kundeDto.getPartnerDto()
						.getCName2vornamefirmazeile2();
				zeile[REPORT_KUNDENLISTE_CNAME3] = kundeDto.getPartnerDto()
						.getCName3vorname2abteilung();

				zeile[REPORT_KUNDENLISTE_BEMERKUNG] = kundeDto.getPartnerDto()
						.getXBemerkung();

				zeile[REPORT_KUNDENLISTE_KUNDENNUMMER] = kundeDto
						.getIKundennummer();

				if (kundeDto.getIidErloeseKonto() != null) {
					zeile[REPORT_KUNDENLISTE_ERLOESKONTO] = getFinanzFac()
							.kontoFindByPrimaryKey(
									kundeDto.getIidErloeseKonto()).getCNr();
				}
				if (kunde.getFlrkonto() != null) {
					zeile[REPORT_KUNDENLISTE_DEBITORENKONTO] = kunde
							.getFlrkonto().getC_nr();
				}

				zeile[REPORT_KUNDENLISTE_EMAIL] = kundeDto.getPartnerDto()
						.getCEmail();

				zeile[REPORT_KUNDENLISTE_FAX] = kundeDto.getPartnerDto()
						.getCFax();

				zeile[REPORT_KUNDENLISTE_HOMEPAGE] = kundeDto.getPartnerDto()
						.getCHomepage();

				zeile[REPORT_KUNDENLISTE_TELEFON] = kundeDto.getPartnerDto()
						.getCTelefon();

				zeile[REPORT_KUNDENLISTE_FIRMENBUCHNUMMER] = kundeDto
						.getPartnerDto().getCFirmenbuchnr();
				zeile[REPORT_KUNDENLISTE_GERICHTSSTAND] = kundeDto
						.getPartnerDto().getCGerichtsstand();
				zeile[REPORT_KUNDENLISTE_INTERESSENT] = kundeDto
						.getbIstinteressent();
				zeile[REPORT_KUNDENLISTE_KOMMUNIKATIONSSPRACHE] = kundeDto
						.getPartnerDto().getLocaleCNrKommunikation();

				if (kundeDto.getKostenstelleIId() != null) {
					zeile[REPORT_KUNDENLISTE_KOSTENSTELLE] = getSystemFac()
							.kostenstelleFindByPrimaryKey(
									kundeDto.getKostenstelleIId()).getCNr();
				}
				zeile[REPORT_KUNDENLISTE_KREDITLIMIT] = kundeDto
						.getNKreditlimit();
				zeile[REPORT_KUNDENLISTE_KURZBEZEICHNUNG] = kundeDto
						.getPartnerDto().getCKbez();
				if (kundeDto.getPartnerDto().getLandplzortDto() != null) {
					zeile[REPORT_KUNDENLISTE_LAND] = kundeDto.getPartnerDto()
							.getLandplzortDto().getLandDto().getCLkz();
					zeile[REPORT_KUNDENLISTE_PLZ] = kundeDto.getPartnerDto()
							.getLandplzortDto().getCPlz();
					zeile[REPORT_KUNDENLISTE_ORT] = kundeDto.getPartnerDto()
							.getLandplzortDto().getOrtDto().getCName();
				}
				if (kundeDto.getPartnerDto().getLandplzortDto_Postfach() != null) {
					zeile[REPORT_KUNDENLISTE_LAND_POSTFACH] = kundeDto
							.getPartnerDto().getLandplzortDto_Postfach()
							.getLandDto().getCLkz();
					zeile[REPORT_KUNDENLISTE_PLZ_POSTFACH] = kundeDto
							.getPartnerDto().getLandplzortDto_Postfach()
							.getCPlz();
					zeile[REPORT_KUNDENLISTE_ORT_POSTFACH] = kundeDto
							.getPartnerDto().getLandplzortDto_Postfach()
							.getOrtDto().getCName();
				}
				zeile[REPORT_KUNDENLISTE_POSTFACH] = kundeDto.getPartnerDto()
						.getCPostfach();

				if (kundeDto.getTBonitaet() != null) {
					zeile[REPORT_KUNDENLISTE_LETZTE_BONITAETSPRUEFUNG] = new java.sql.Timestamp(
							kundeDto.getTBonitaet().getTime());
				}
				zeile[REPORT_KUNDENLISTE_LIEFERANTENNUMMER] = kundeDto
						.getCLieferantennr();
				zeile[REPORT_KUNDENLISTE_PARTNERART] = kundeDto.getPartnerDto()
						.getPartnerartCNr();

				if (kundeDto.getPartnerDto().getPartnerklasseIId() != null) {
					zeile[REPORT_KUNDENLISTE_PARTNERKLASSE] = getPartnerFac()
							.partnerklasseFindByPrimaryKey(
									kundeDto.getPartnerDto()
											.getPartnerklasseIId(),
									theClientDto).getCNr();
				}
				zeile[REPORT_KUNDENLISTE_RABATT] = kundeDto.getFRabattsatz();
				zeile[REPORT_KUNDENLISTE_ZESSION] = kundeDto
						.getFZessionsfaktor();

				if (kundeDto.getPersonaliIdProvisionsempfaenger() != null) {
					PersonalDto personalDto = getPersonalFac()
							.personalFindByPrimaryKey(
									kundeDto.getPersonaliIdProvisionsempfaenger(),
									theClientDto);
					zeile[REPORT_KUNDENLISTE_PROVISIONSEMPFAENGER] = personalDto
							.getPartnerDto().formatFixTitelName1Name2();
				}

				if (kundeDto.getPartnerRechnungsadresseDto() != null) {
					zeile[REPORT_KUNDENLISTE_RECHNUNGSADRESSE_ANREDE] = kundeDto
							.getPartnerRechnungsadresseDto().getAnredeCNr();
					zeile[REPORT_KUNDENLISTE_RECHNUNGSADRESSE_CNAME1] = kundeDto
							.getPartnerRechnungsadresseDto()
							.getCName1nachnamefirmazeile1();
					zeile[REPORT_KUNDENLISTE_RECHNUNGSADRESSE_CNAME2] = kundeDto
							.getPartnerRechnungsadresseDto()
							.getCName2vornamefirmazeile2();
					zeile[REPORT_KUNDENLISTE_RECHNUNGSADRESSE_CNAME3] = kundeDto
							.getPartnerRechnungsadresseDto()
							.getCName3vorname2abteilung();
					zeile[REPORT_KUNDENLISTE_RECHNUNGSADRESSE_KURZBEZEICHNUNG] = kundeDto
							.getPartnerRechnungsadresseDto().getCKbez();
					if (kundeDto.getPartnerRechnungsadresseDto()
							.getLandplzortDto() != null) {
						zeile[REPORT_KUNDENLISTE_RECHNUNGSADRESSE_LAND] = kundeDto
								.getPartnerRechnungsadresseDto()
								.getLandplzortDto().getLandDto().getCName();
						zeile[REPORT_KUNDENLISTE_RECHNUNGSADRESSE_PLZ] = kundeDto
								.getPartnerRechnungsadresseDto()
								.getLandplzortDto().getCPlz();
						zeile[REPORT_KUNDENLISTE_RECHNUNGSADRESSE_ORT] = kundeDto
								.getPartnerRechnungsadresseDto()
								.getLandplzortDto().getOrtDto().getCName();
					}
					if (kundeDto.getPartnerRechnungsadresseDto()
							.getLandplzortDto_Postfach() != null) {
						zeile[REPORT_KUNDENLISTE_RECHNUNGSADRESSE_LAND_POSTFACH] = kundeDto
								.getPartnerRechnungsadresseDto()
								.getLandplzortDto_Postfach().getLandDto()
								.getCName();
						zeile[REPORT_KUNDENLISTE_RECHNUNGSADRESSE_PLZ_POSTFACH] = kundeDto
								.getPartnerRechnungsadresseDto()
								.getLandplzortDto_Postfach().getCPlz();
						zeile[REPORT_KUNDENLISTE_RECHNUNGSADRESSE_ORT] = kundeDto
								.getPartnerRechnungsadresseDto()
								.getLandplzortDto_Postfach().getOrtDto()
								.getCName();
					}
					zeile[REPORT_KUNDENLISTE_RECHNUNGSADRESSE_POSTFACH] = kundeDto
							.getPartnerRechnungsadresseDto().getCPostfach();
					zeile[REPORT_KUNDENLISTE_RECHNUNGSADRESSE_STRASSE] = kundeDto
							.getPartnerRechnungsadresseDto().getCStrasse();
					zeile[REPORT_KUNDENLISTE_RECHNUNGSADRESSE_TITEL] = kundeDto
							.getPartnerRechnungsadresseDto().getCTitel();
					zeile[REPORT_KUNDENLISTE_RECHNUNGSADRESSE_UIDNUMMER] = kundeDto
							.getPartnerRechnungsadresseDto().getCUid();

				}

				zeile[REPORT_KUNDENLISTE_STRASSE] = kundeDto.getPartnerDto()
						.getCStrasse();
				zeile[REPORT_KUNDENLISTE_TITEL] = kundeDto.getPartnerDto()
						.getCTitel();
				zeile[REPORT_KUNDENLISTE_UIDNUMMER] = kundeDto.getPartnerDto()
						.getCUid();
				zeile[REPORT_KUNDENLISTE_WAEHRUNG] = kundeDto.getCWaehrung();

				if (kundeDto.getZahlungszielIId() != null) {
					zeile[REPORT_KUNDENLISTE_ZAHLUNGSZIEL] = getMandantFac()
							.zahlungszielFindByPrimaryKey(
									kundeDto.getZahlungszielIId(), theClientDto)
							.getCBez();
				}

				if (kundeDto.getSpediteurIId() != null) {
					zeile[REPORT_KUNDENLISTE_SPEDITEUR] = getMandantFac()
							.spediteurFindByPrimaryKey(
									kundeDto.getSpediteurIId())
							.getCNamedesspediteurs();
				}
				if (kundeDto.getLieferartIId() != null) {
					zeile[REPORT_KUNDENLISTE_LIEFERART] = getLocaleFac()
							.lieferartFindByPrimaryKey(
									kundeDto.getLieferartIId(), theClientDto)
							.formatBez();
				}
				if (kundeDto.getMwstsatzbezIId() != null) {
					zeile[REPORT_KUNDENLISTE_MWSTSATZ] = getMandantFac()
							.mwstsatzbezFindByPrimaryKey(
									kundeDto.getMwstsatzbezIId(), theClientDto)
							.getCBezeichnung();
				}
				if (kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste() != null) {
					zeile[REPORT_KUNDENLISTE_PREISLISTE] = getVkPreisfindungFac()
							.vkpfartikelpreislisteFindByPrimaryKey(
									kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste())
							.getCNr();
				}

				if (iProjektemitdrucken == REPORT_KUNDENLISTE_OPTION_PROJEKTEALLE
						|| iProjektemitdrucken == REPORT_KUNDENLISTE_OPTION_PROJEKTEOFFENE) {

					if (iProjektemitdrucken == REPORT_KUNDENLISTE_OPTION_PROJEKTEALLE) {
						zeile[REPORT_KUNDENLISTE_SUBREPORT_PROJEKTE] = getSubreportProjekte(
								kundeDto.getPartnerIId(), false, theClientDto);
					} else {
						zeile[REPORT_KUNDENLISTE_SUBREPORT_PROJEKTE] = getSubreportProjekte(
								kundeDto.getPartnerIId(), true, theClientDto);
					}
				}

				zeile[REPORT_KUNDENLISTE_UMSATZ_HEUER] = getRechnungFac()
						.getUmsatzVomKundenHeuer(theClientDto,
								kundeDto.getIId(), bUmsatzNachStatistikadresse);
				zeile[REPORT_KUNDENLISTE_UMSATZ_VORJAHR] = getRechnungFac()
						.getUmsatzVomKundenVorjahr(theClientDto,
								kundeDto.getIId(), bUmsatzNachStatistikadresse);
				zeile[REPORT_KUNDENLISTE_ANZAHL_RECHNUNGEN_HEUER] = getRechnungFac()
						.getAnzahlDerRechnungenVomKundenHeuer(theClientDto,
								kundeDto.getIId(), bUmsatzNachStatistikadresse);
				zeile[REPORT_KUNDENLISTE_ANZAHL_RECHNUNGEN_VORJAHR] = getRechnungFac()
						.getAnzahlDerRechnungenVomKundenVorjahr(theClientDto,
								kundeDto.getIId(), bUmsatzNachStatistikadresse);

				// Selektionen
				Set<?> selektionen = kunde.getFlrpartner()
						.getPartner_paselektion_set();
				Iterator<?> isSel = selektionen.iterator();
				if (isSel.hasNext() == true) {
					FLRPASelektion selektion = (FLRPASelektion) isSel.next();
					zeile[REPORT_KUNDENLISTE_SELEKTION01] = selektion
							.getFlrselektion().getC_nr();
				}
				if (isSel.hasNext() == true) {
					FLRPASelektion selektion = (FLRPASelektion) isSel.next();
					zeile[REPORT_KUNDENLISTE_SELEKTION02] = selektion
							.getFlrselektion().getC_nr();
				}
				if (isSel.hasNext() == true) {
					FLRPASelektion selektion = (FLRPASelektion) isSel.next();
					zeile[REPORT_KUNDENLISTE_SELEKTION03] = selektion
							.getFlrselektion().getC_nr();
				}
				if (isSel.hasNext() == true) {
					FLRPASelektion selektion = (FLRPASelektion) isSel.next();
					zeile[REPORT_KUNDENLISTE_SELEKTION04] = selektion
							.getFlrselektion().getC_nr();
				}
				if (isSel.hasNext() == true) {
					FLRPASelektion selektion = (FLRPASelektion) isSel.next();
					zeile[REPORT_KUNDENLISTE_SELEKTION05] = selektion
							.getFlrselektion().getC_nr();
				}
				if (isSel.hasNext() == true) {
					FLRPASelektion selektion = (FLRPASelektion) isSel.next();
					zeile[REPORT_KUNDENLISTE_SELEKTION06] = selektion
							.getFlrselektion().getC_nr();
				}
				if (isSel.hasNext() == true) {
					FLRPASelektion selektion = (FLRPASelektion) isSel.next();
					zeile[REPORT_KUNDENLISTE_SELEKTION07] = selektion
							.getFlrselektion().getC_nr();
				}
				if (isSel.hasNext() == true) {
					FLRPASelektion selektion = (FLRPASelektion) isSel.next();
					zeile[REPORT_KUNDENLISTE_SELEKTION08] = selektion
							.getFlrselektion().getC_nr();
				}
				if (isSel.hasNext() == true) {
					FLRPASelektion selektion = (FLRPASelektion) isSel.next();
					zeile[REPORT_KUNDENLISTE_SELEKTION09] = selektion
							.getFlrselektion().getC_nr();
				}
				if (isSel.hasNext() == true) {
					FLRPASelektion selektion = (FLRPASelektion) isSel.next();
					zeile[REPORT_KUNDENLISTE_SELEKTION10] = selektion
							.getFlrselektion().getC_nr();
				}
				if (isSel.hasNext() == true) {
					FLRPASelektion selektion = (FLRPASelektion) isSel.next();
					zeile[REPORT_KUNDENLISTE_SELEKTION11] = selektion
							.getFlrselektion().getC_nr();
				}
				if (isSel.hasNext() == true) {
					FLRPASelektion selektion = (FLRPASelektion) isSel.next();
					zeile[REPORT_KUNDENLISTE_SELEKTION12] = selektion
							.getFlrselektion().getC_nr();
				}
				if (isSel.hasNext() == true) {
					FLRPASelektion selektion = (FLRPASelektion) isSel.next();
					zeile[REPORT_KUNDENLISTE_SELEKTION13] = selektion
							.getFlrselektion().getC_nr();
				}
				if (isSel.hasNext() == true) {
					FLRPASelektion selektion = (FLRPASelektion) isSel.next();
					zeile[REPORT_KUNDENLISTE_SELEKTION14] = selektion
							.getFlrselektion().getC_nr();
				}
				if (isSel.hasNext() == true) {
					FLRPASelektion selektion = (FLRPASelektion) isSel.next();
					zeile[REPORT_KUNDENLISTE_SELEKTION15] = selektion
							.getFlrselektion().getC_nr();
				}

				Set<?> ansprechpartner = kunde.getFlrpartner()
						.getAnsprechpartner();
				if (ansprechpartner.size() > 0) {

					int z = 0;
					Iterator<?> anspIt = ansprechpartner.iterator();
					while (anspIt.hasNext()) {
						z++;

						if (z == 2) {
							int u = 0;
						}

						Object[] oKopie = new Object[92];

						for (int i = 0; i < 91; i++) {
							oKopie[i] = zeile[i];
						}

						FLRAnsprechpartner flrAnsprechpartner = (FLRAnsprechpartner) anspIt
								.next();

						oKopie[REPORT_KUNDENLISTE_ANSPRECHPARTNER_ANREDE] = flrAnsprechpartner
								.getFlrpartneransprechpartner()
								.getAnrede_c_nr();

						oKopie[REPORT_KUNDENLISTE_ANSPRECHPARTNER_EMAIL] = flrAnsprechpartner
								.getC_email();

						oKopie[REPORT_KUNDENLISTE_ANSPRECHPARTNER_FAXDW] = flrAnsprechpartner
								.getC_fax();

						oKopie[REPORT_KUNDENLISTE_ANSPRECHPARTNER_MOBIL] = flrAnsprechpartner
								.getC_handy();

						oKopie[REPORT_KUNDENLISTE_ANSPRECHPARTNER_BEMERKUNG] = flrAnsprechpartner
								.getX_bemerkung();

						oKopie[REPORT_KUNDENLISTE_ANSPRECHPARTNER_NACHNAME] = flrAnsprechpartner
								.getFlrpartneransprechpartner()
								.getC_name1nachnamefirmazeile1();

						oKopie[REPORT_KUNDENLISTE_ANSPRECHPARTNER_TELDW] = flrAnsprechpartner
								.getC_telefon();

						oKopie[REPORT_KUNDENLISTE_ANSPRECHPARTNER_TITEL] = flrAnsprechpartner
								.getFlrpartneransprechpartner().getC_titel();
						oKopie[REPORT_KUNDENLISTE_ANSPRECHPARTNER_VORNAME] = flrAnsprechpartner
								.getFlrpartneransprechpartner()
								.getC_name2vornamefirmazeile2();

						if (z == 1) {

							daten.add(oKopie);
						}

						if (z > 1 && bMitAnsprechpartner == true) {
							daten.add(oKopie);
						}

					}
				} else {
					zeile[REPORT_KUNDENLISTE_BRIEFANREDE] = getPartnerServicesFac()
							.getBriefanredeFuerBeleg(null,
									kundeDto.getPartnerIId(),
									theClientDto.getLocUi(), theClientDto);
					daten.add(zeile);
				}
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
		}
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		parameter.put("P_MITANSPRECHPARTNER", new Boolean(bMitAnsprechpartner));

		// data = new Object[daten.size()][87];
		setData(new Object[daten.size()][87]);
		for (int i = 0; i < daten.size(); i++) {
			data[i] = (Object[]) daten.get(i);
		}

		initJRDS(parameter, KundeReportFac.REPORT_MODUL,
				KundeReportFac.REPORT_KUNDENLISTE, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	public Object[][] getDataLieferstatistik(TheClientDto theClientDto,
			Integer kundeIId, Integer artikelIId, Integer artikelgruppeIId,
			Date dVon, Date dBis, Integer iSortierung, String mandantCNr,
			boolean bMitTexteingaben, boolean bVerdichtetNachArtikel,
			boolean bEingeschraenkt, int iOptionAdrsse, boolean bRechnungsdatum)
			throws EJBExceptionLP {

		Object[][] data = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			FacadeBeauftragter fac = new FacadeBeauftragter();
			session = factory.openSession();

			String queryRechnung = " from FLRRechnungPosition rechpos  WHERE 1=1 ";

			if (bMitTexteingaben == true) {
				queryRechnung += " AND rechpos.positionsart_c_nr NOT IN ('"
						+ LocaleFac.POSITIONSART_LIEFERSCHEIN + "','"
						+ LocaleFac.POSITIONSART_LEERZEILE + "')";
			} else {
				queryRechnung += " AND rechpos.positionsart_c_nr NOT IN ('"
						+ LocaleFac.POSITIONSART_LIEFERSCHEIN + "','"
						+ LocaleFac.POSITIONSART_LEERZEILE + "','"
						+ LocaleFac.POSITIONSART_TEXTEINGABE + "')";

			}

			queryRechnung += " AND rechpos.flrrechnung.status_c_nr NOT IN ('"
					+ RechnungFac.STATUS_STORNIERT + "') ";

			if (kundeIId != null) {
				if (iOptionAdrsse == REPORT_LIEFERSTATISTIK_OPTION_STATISTIKADRESSE) {
					queryRechnung += " AND rechpos.flrrechnung.flrstatistikadresse.i_id="
							+ kundeIId;
				} else {
					queryRechnung += " AND rechpos.flrrechnung.flrkunde.i_id="
							+ kundeIId;
				}
			}

			if (artikelIId != null) {
				queryRechnung += " AND rechpos.artikel_i_id=" + artikelIId
						+ " ";
			}

			if (artikelgruppeIId != null) {
				queryRechnung += " AND rechpos.flrartikel.flrartikelgruppe.i_id="
						+ artikelgruppeIId + " ";
			}

			if (mandantCNr != null) {
				queryRechnung += " AND rechpos.flrrechnung.mandant_c_nr= '"
						+ mandantCNr + "'";
			}

			if (dVon != null) {
				queryRechnung += " AND rechpos.flrrechnung.d_belegdatum>='"
						+ Helper.formatDateWithSlashes(Helper.cutDate(dVon))
						+ "'";
			}
			if (dBis != null) {
				queryRechnung += " AND rechpos.flrrechnung.d_belegdatum<'"
						+ Helper.formatDateWithSlashes(dBis) + "'";
			}

			queryRechnung += " ORDER BY rechpos.flrrechnung.d_belegdatum DESC, rechpos.i_sort ASC";

			Query qRechnung = session.createQuery(queryRechnung);

			if (bEingeschraenkt) {
				qRechnung.setMaxResults(50);
			}

			List<?> resultListRechnung = qRechnung.list();

			String queryLieferschein = " from FLRLieferscheinposition lspos  WHERE  1=1 ";

			if (bMitTexteingaben == false) {
				queryLieferschein += " AND lspos.positionsart_c_nr NOT IN ('"
						+ LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_TEXTEINGABE
						+ "')";
			}

			if (kundeIId != null) {
				if (iOptionAdrsse == REPORT_LIEFERSTATISTIK_OPTION_RECHNUNGSADRESSE) {
					queryLieferschein += " AND lspos.flrlieferschein.kunde_i_id_rechnungsadresse="
							+ kundeIId;
				} else {
					queryLieferschein += " AND lspos.flrlieferschein.kunde_i_id_lieferadresse="
							+ kundeIId;
				}
			}

			if (artikelIId != null) {
				queryLieferschein += " AND lspos.flrartikel.i_id=" + artikelIId
						+ " ";
			}

			if (artikelgruppeIId != null) {
				queryLieferschein += " AND lspos.flrartikel.flrartikelgruppe.i_id="
						+ artikelgruppeIId + " ";
			}

			queryLieferschein += " AND lspos.flrlieferschein.lieferscheinstatus_status_c_nr NOT IN ('"
					+ LieferscheinFac.LSSTATUS_STORNIERT + "') ";

			if (mandantCNr != null) {
				queryLieferschein += " AND lspos.flrlieferschein.mandant_c_nr= '"
						+ mandantCNr + "'";
			}

			if (bRechnungsdatum == true) {

				queryLieferschein += " AND lspos.flrlieferschein.flrrechnung.i_id IS NOT NULL ";

				if (dVon != null) {

					queryLieferschein += " AND lspos.flrlieferschein.flrrechnung.d_belegdatum>='"
							+ Helper.formatDateWithSlashes(dVon) + "'";
				}
				if (dBis != null) {
					queryLieferschein += " AND lspos.flrlieferschein.flrrechnung.d_belegdatum<'"
							+ Helper.formatDateWithSlashes(dBis) + "'";
				}
			} else {
				if (dVon != null) {

					queryLieferschein += " AND lspos.flrlieferschein.d_belegdatum>='"
							+ Helper.formatDateWithSlashes(dVon) + "'";
				}
				if (dBis != null) {
					queryLieferschein += " AND lspos.flrlieferschein.d_belegdatum<'"
							+ Helper.formatDateWithSlashes(dBis) + "'";
				}
			}

			queryLieferschein += " ORDER BY lspos.flrlieferschein.d_belegdatum DESC, lspos.i_sort ASC";

			Query qLieferschein = session.createQuery(queryLieferschein);

			if (bEingeschraenkt) {
				qLieferschein.setMaxResults(50);
			}

			List<?> resultListLieferschein = qLieferschein.list();

			Iterator<?> resultListIteratorRechnung = resultListRechnung
					.iterator();
			// Rechnungspositionen verarbeiten
			ArrayList<KundeLieferstatistikDto> cResult = new ArrayList<KundeLieferstatistikDto>();

			while (resultListIteratorRechnung.hasNext()) {
				FLRRechnungPosition rePos = (FLRRechnungPosition) resultListIteratorRechnung
						.next();
				if (RechnungFac.POSITIONSART_RECHNUNG_INTELLIGENTE_ZWISCHENSUMME
						.equals(rePos.getPositionsart_c_nr())) {
					continue;
				}

				RechnungDto d = fac.getRechnungFac().rechnungFindByPrimaryKey(
						rePos.getRechnung_i_id());

				KundeLieferstatistikDto dto = new KundeLieferstatistikDto();

				if (rePos.getFlrverleih() != null) {
					dto.setdVerleihfaktor(rePos.getFlrverleih().getF_faktor());
					dto.setiVerleihtage(rePos.getFlrverleih().getI_tage());
				}

				String sRechnungsart = d.getRechnungartCNr();
				dto.setSWarenausgangverursacher(d.getRechnungartCNr());

				// Rechnungsnummer
				dto.setSRechnungsnummer(rePos.getFlrrechnung().getC_nr());
				// Lieferscheinnummer
				if (rePos.getFlrlieferschein() != null) {
					dto.setSLieferscheinnummer(rePos.getFlrlieferschein()
							.getC_nr());
				}
				// Mwstsatz
				if (rePos.getFlrmwstsatz() != null) {
					dto.setNMwstsatz(Helper.rundeKaufmaennisch(new BigDecimal(
							rePos.getFlrmwstsatz().getF_mwstsatz()), 2));
				}
				// Vertreter
				if (rePos.getFlrrechnung().getFlrvertreter() != null) {
					dto.setSVertreter(rePos.getFlrrechnung().getFlrvertreter()
							.getC_kurzzeichen());
				}
				// Provisionsempfaenger
				if (rePos.getFlrrechnung().getFlrkunde().getFlrpersonal() != null) {
					dto.setSProvisionsempfaenger(rePos.getFlrrechnung()
							.getFlrkunde().getFlrpersonal().getC_kurzzeichen());
				}
				// Kunde + LAND + PLZ + ORT
				String kundenname = rePos.getFlrrechnung().getFlrkunde()
						.getFlrpartner().getC_name1nachnamefirmazeile1();

				String firma2 = rePos.getFlrrechnung().getFlrkunde()
						.getFlrpartner().getC_name2vornamefirmazeile2();

				if (firma2 != null) {
					kundenname += " " + firma2;
				}
				dto.setSKundenname(kundenname);
				if (rePos.getFlrrechnung().getFlrkunde().getFlrpartner()
						.getFlrlandplzort() != null) {
					dto.setSLand(rePos.getFlrrechnung().getFlrkunde()
							.getFlrpartner().getFlrlandplzort().getFlrland()
							.getC_lkz());
					dto.setSPlz(rePos.getFlrrechnung().getFlrkunde()
							.getFlrpartner().getFlrlandplzort().getC_plz());
					dto.setSOrt(rePos.getFlrrechnung().getFlrkunde()
							.getFlrpartner().getFlrlandplzort().getFlrort()
							.getC_name());
				}
				// Statistikadresse + LAND + PLZ + ORT
				String statistikadresse = rePos.getFlrrechnung()
						.getFlrstatistikadresse().getFlrpartner()
						.getC_name1nachnamefirmazeile1();

				String statistikadressefirma2 = rePos.getFlrrechnung()
						.getFlrstatistikadresse().getFlrpartner()
						.getC_name2vornamefirmazeile2();

				if (statistikadressefirma2 != null) {
					statistikadresse += " " + statistikadressefirma2;
				}
				dto.setSStatistikadresse(statistikadresse);
				if (rePos.getFlrrechnung().getFlrstatistikadresse()
						.getFlrpartner().getFlrlandplzort() != null) {
					dto.setSLandStatistikadresse(rePos.getFlrrechnung()
							.getFlrstatistikadresse().getFlrpartner()
							.getFlrlandplzort().getFlrland().getC_lkz());
					dto.setSPlzStatistikadresse(rePos.getFlrrechnung()
							.getFlrstatistikadresse().getFlrpartner()
							.getFlrlandplzort().getC_plz());
					dto.setSOrtStatistikadresse(rePos.getFlrrechnung()
							.getFlrstatistikadresse().getFlrpartner()
							.getFlrlandplzort().getFlrort().getC_name());
				}

				// Datum
				dto.setDWarenausgangsdatum(new Timestamp(rePos.getFlrrechnung()
						.getD_belegdatum().getTime()));
				// Ident

				if (rePos.getPositionsart_c_nr().equals(
						RechnungFac.POSITIONSART_RECHNUNG_IDENT)) {
					if (Helper.short2boolean(rePos.getFlrartikel()
							.getB_seriennrtragend())
							|| Helper.short2boolean(rePos.getFlrartikel()
									.getB_chargennrtragend())) {
						dto.setSnrs(getLagerFac()
								.getAllSeriennrchargennrEinerBelegartpositionOhneChargeneigenschaften(
										LocaleFac.BELEGART_RECHNUNG,
										rePos.getI_id()));
					}
				}

				if (rePos.getPositionsart_c_nr().equals(
						RechnungFac.POSITIONSART_RECHNUNG_IDENT)
						|| rePos.getPositionsart_c_nr().equals(
								RechnungFac.POSITIONSART_RECHNUNG_HANDEINGABE)) {
					if (rePos.getFlrartikel() != null) {
						if (rePos.getFlrartikel().getArtikelart_c_nr()
								.equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
							dto.setSIdent(ArtikelFac.ARTIKELART_HANDARTIKEL
									.trim());
						} else {
							dto.setSIdent(rePos.getFlrartikel().getC_nr());
						}
					}
				} else if (rePos.getPositionsart_c_nr().equals(
						RechnungFac.POSITIONSART_RECHNUNG_TEXTEINGABE)) {
					dto.setSBezeichnung(rePos.getX_textinhalt());
				}

				if (rePos.getPosition_i_id_artikelset() != null) {

					dto.setSSetartikelTyp(ArtikelFac.SETARTIKEL_TYP_POSITION);

				} else {
					if (rePos.getSetartikel_set().size() > 0) {
						dto.setSSetartikelTyp(ArtikelFac.SETARTIKEL_TYP_KOPF);
					}
				}

				// String posartCnr = rePos.getPositionsart_c_nr() ;
				// if(!RechnungFac.POSITIONSART_RECHNUNG_TEXTEINGABE.equals(posartCnr)
				// &&
				// !RechnungFac.POSITIONSART_RECHNUNG_INTELLIGENTE_ZWISCHENSUMME.equals(posartCnr))
				// {
				if (!rePos.getPositionsart_c_nr().equals(
						RechnungFac.POSITIONSART_RECHNUNG_TEXTEINGABE)) {
					if (rePos.getFlrartikel() != null) {
						ArtikelDto artikelDto = fac.getArtikelFac()
								.artikelFindByPrimaryKeySmall(
										rePos.getFlrartikel().getI_id(),
										theClientDto);

						if (rePos.getC_bez() == null) {
							dto.setSBezeichnung(artikelDto.formatBezeichnung());
						} else {
							dto.setSBezeichnung(rePos.getC_bez());
						}
						BigDecimal bMenge = rePos.getN_menge();
						dto.setNMenge(sRechnungsart
								.equals(RechnungFac.RECHNUNGART_GUTSCHRIFT) ? bMenge
								.negate() : bMenge);

						dto.setNMaterialzuschlag(rePos.getN_materialzuschlag());

						// if (rePos.getFlrrechnung().getN_kurs().doubleValue()
						// != 0
						// && rePos.getN_nettoeinzelpreis() != null) {
						// dto.setNPreis(rePos.getN_nettoeinzelpreis().divide(
						// rePos.getFlrrechnung().getN_kurs(), 4,
						// BigDecimal.ROUND_HALF_EVEN));
						// } else {
						// dto.setNPreis(BigDecimal.ZERO);
						// }

						if (rePos.getFlrrechnung().getN_kurs().signum() != 0) {
							BigDecimal kurs = rePos.getFlrrechnung()
									.getN_kurs();
							if (rePos
									.getN_nettoeinzelpreis_plus_aufschlag_minus_rabatt() != null) {
								dto.setNPreis(rePos
										.getN_nettoeinzelpreis_plus_aufschlag_minus_rabatt()
										.divide(kurs, 4,
												BigDecimal.ROUND_HALF_EVEN));
							} else {
								dto.setNPreis(BigDecimal.ZERO);
							}

							// if(rePos.getN_nettoeinzelpreis() != null) {
							// dto.setNPreis(rePos.getN_nettoeinzelpreis().divide(kurs,
							// 4, BigDecimal.ROUND_HALF_EVEN));
							// } else {
							// dto.setNPreis(BigDecimal.ZERO) ;
							// }
							//
							// if(rePos.getN_nettoeinzelpreis_plus_aufschlag_minus_rabatt()
							// != null) {
							// dto.setNWert(rePos.getN_nettoeinzelpreis_plus_aufschlag_minus_rabatt()
							// .divide(kurs, 4, BigDecimal.ROUND_HALF_EVEN));
							// } else {
							// dto.setNWert(BigDecimal.ZERO);
							// }
						}

						if (rePos.getFlrartikel().getFlrartikelgruppe() != null) {
							dto.setSArtikelgruppe(rePos.getFlrartikel()
									.getFlrartikelgruppe().getC_nr());
							if (rePos.getFlrartikel().getFlrartikelgruppe()
									.getFlrkonto() != null) {
								dto.setSKonto(rePos.getFlrartikel()
										.getFlrartikelgruppe().getFlrkonto()
										.getC_nr());
							}
						}
						if (rePos.getFlrartikel().getFlrartikelklasse() != null) {
							dto.setSArtikelklasse(rePos.getFlrartikel()
									.getFlrartikelklasse().getC_nr());
						}
					} else {
						dto.setSBezeichnung(rePos.getC_bez());
					}
				}

				if (bVerdichtetNachArtikel) {
					boolean bGefunden = false;
					for (int j = 0; j < cResult.size(); j++) {
						KundeLieferstatistikDto temp = (KundeLieferstatistikDto) cResult
								.get(j);
						if (temp.getSIdent().equals(dto.getSIdent())) {
							if (dto.getNMenge() != null
									&& temp.getNMenge() != null) {
								BigDecimal mengeNeu = dto.getNMenge().add(
										temp.getNMenge());

								BigDecimal preisWertAlt = temp.getNPreis()
										.multiply(temp.getNMenge());
								BigDecimal preisWertNeu = dto.getNPreis()
										.multiply(dto.getNMenge());

								BigDecimal preisNeu = dto.getNPreis();
								if (mengeNeu.doubleValue() != 0) {
									preisNeu = preisWertNeu.add(preisWertAlt)
											.divide(mengeNeu,
													BigDecimal.ROUND_HALF_EVEN);
								}
								temp.setNMenge(mengeNeu);
								temp.setNPreis(preisNeu);

								temp.setSnrs(SeriennrChargennrMitMengeDto
										.add2SnrChnrDtos(temp.getSnrs(),
												dto.getSnrs()));

								temp.setDWarenausgangsdatum(null);
								temp.setSLieferscheinnummer(null);
								temp.setSRechnungsnummer(null);
								cResult.set(j, temp);
								bGefunden = true;
								break;
							}

						}
					}
					if (bGefunden == false) {
						cResult.add(dto);
					}
				} else {
					cResult.add(dto);
				}
			}

			Iterator<?> resultListIteratorLieferschein = resultListLieferschein
					.iterator();
			// lieferscheinpositionen verarbeiten
			while (resultListIteratorLieferschein.hasNext()) {
				FLRLieferscheinposition lsPos = (FLRLieferscheinposition) resultListIteratorLieferschein
						.next();
				if (RechnungFac.POSITIONSART_RECHNUNG_INTELLIGENTE_ZWISCHENSUMME
						.equals(lsPos.getPositionsart_c_nr())) {
					continue;
				}

				KundeLieferstatistikDto dto = new KundeLieferstatistikDto();

				dto.setSWarenausgangverursacher(RechnungFac.RECHNUNGART_RECHNUNG);
				if (lsPos.getFlrverleih() != null) {
					dto.setdVerleihfaktor(lsPos.getFlrverleih().getF_faktor());
					dto.setiVerleihtage(lsPos.getFlrverleih().getI_tage());
				}

				// Rechnungsnummer
				if (lsPos.getFlrlieferschein().getFlrrechnung() != null) {
					dto.setSRechnungsnummer(lsPos.getFlrlieferschein()
							.getFlrrechnung().getC_nr());
				}
				// Lieferscheinnummer
				if (lsPos.getFlrlieferschein() != null) {
					dto.setSLieferscheinnummer(lsPos.getFlrlieferschein()
							.getC_nr());
				}
				// Mwstsatz
				if (lsPos.getFlrmwstsatz() != null) {
					dto.setNMwstsatz(Helper.rundeKaufmaennisch(new BigDecimal(
							lsPos.getFlrmwstsatz().getF_mwstsatz()), 2));
				}
				// Kunde + LAND + PLZ + ORT
				String kundenname = lsPos.getFlrlieferschein().getFlrkunde()
						.getFlrpartner().getC_name1nachnamefirmazeile1();

				String firma2 = lsPos.getFlrlieferschein().getFlrkunde()
						.getFlrpartner().getC_name2vornamefirmazeile2();

				if (firma2 != null) {
					kundenname += " " + firma2;
				}
				dto.setSKundenname(kundenname);
				if (lsPos.getFlrlieferschein().getFlrkunde().getFlrpartner()
						.getFlrlandplzort() != null) {
					dto.setSLand(lsPos.getFlrlieferschein().getFlrkunde()
							.getFlrpartner().getFlrlandplzort().getFlrland()
							.getC_lkz());
					dto.setSPlz(lsPos.getFlrlieferschein().getFlrkunde()
							.getFlrpartner().getFlrlandplzort().getC_plz());
					dto.setSOrt(lsPos.getFlrlieferschein().getFlrkunde()
							.getFlrpartner().getFlrlandplzort().getFlrort()
							.getC_name());
				}
				// Vertreter
				if (lsPos.getFlrlieferschein().getFlrvertreter() != null) {
					dto.setSVertreter(lsPos.getFlrlieferschein()
							.getFlrvertreter().getC_kurzzeichen());
				}
				// Provisionsempfaenger
				if (lsPos.getFlrlieferschein().getFlrkunde().getFlrpersonal() != null) {
					dto.setSProvisionsempfaenger(lsPos.getFlrlieferschein()
							.getFlrkunde().getFlrpersonal().getC_kurzzeichen());
				}
				// Datum
				if (bRechnungsdatum == true) {
					dto.setDWarenausgangsdatum(lsPos.getFlrlieferschein()
							.getFlrrechnung().getD_belegdatum());
				} else {
					dto.setDWarenausgangsdatum(lsPos.getFlrlieferschein()
							.getD_belegdatum());
				}
				// Ident
				if (lsPos.getFlrartikel() != null) {
					if (lsPos.getFlrartikel().getArtikelart_c_nr()
							.equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
						dto.setSIdent(ArtikelFac.ARTIKELART_HANDARTIKEL.trim());
					} else {
						dto.setSIdent(lsPos.getFlrartikel().getC_nr());
					}
				}
				dto.setNMenge(lsPos.getN_menge());

				// BigDecimal bdKurs = new BigDecimal(
				// lsPos.getFlrlieferschein()
				// .getF_wechselkursmandantwaehrungzulieferscheinwaehrung()
				// .doubleValue());

				// if (bdKurs.doubleValue() != 0
				// && lsPos.getN_nettogesamtpreis() != null) {
				// dto.setNPreis(lsPos.getN_nettogesamtpreis().divide(bdKurs,
				// 4, BigDecimal.ROUND_HALF_EVEN));
				// } else {
				// dto.setNPreis(new BigDecimal(0));
				// }

				// TODO: Hier werden die Werte erneut gesetzt. Ein paar Zeilen
				// oberhalb wird mit Kurs gerechnet?
				// dto.setNPreis(lsPos.getN_nettogesamtpreis());
				dto.setNPreis(lsPos
						.getN_nettogesamtpreisplusversteckteraufschlagminusrabatt());
				dto.setNMaterialzuschlag(lsPos.getN_materialzuschlag());

				if (lsPos.getPositionsart_c_nr().equals(
						RechnungFac.POSITIONSART_RECHNUNG_IDENT)) {
					dto.setSnrs(getLagerFac()
							.getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(
									LocaleFac.BELEGART_LIEFERSCHEIN,
									lsPos.getI_id()));

				}

				if (lsPos.getPosition_i_id_artikelset() != null) {

					dto.setSSetartikelTyp(ArtikelFac.SETARTIKEL_TYP_POSITION);

				} else {

					if (lsPos.getSetartikel_set().size() > 0) {
						dto.setSSetartikelTyp(ArtikelFac.SETARTIKEL_TYP_KOPF);
					}

				}

				if (lsPos.getPositionsart_c_nr().equals(
						LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)
						|| lsPos.getPositionsart_c_nr()
								.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE)) {
					if (lsPos.getFlrartikel() != null) {
						ArtikelDto artikelDto = fac.getArtikelFac()
								.artikelFindByPrimaryKeySmall(
										lsPos.getFlrartikel().getI_id(),
										theClientDto);

						if (Helper.short2boolean(lsPos
								.getB_artikelbezeichnunguebersteuert())) {
							dto.setSBezeichnung(lsPos.getC_bez());
						} else {
							dto.setSBezeichnung(artikelDto.formatBezeichnung());
						}

						if (lsPos.getFlrartikel().getFlrartikelgruppe() != null) {
							dto.setSArtikelgruppe(lsPos.getFlrartikel()
									.getFlrartikelgruppe().getC_nr());
							if (lsPos.getFlrartikel().getFlrartikelgruppe()
									.getFlrkonto() != null) {
								dto.setSKonto(lsPos.getFlrartikel()
										.getFlrartikelgruppe().getFlrkonto()
										.getC_nr());
							}
						}
						if (lsPos.getFlrartikel().getFlrartikelklasse() != null) {
							dto.setSArtikelklasse(lsPos.getFlrartikel()
									.getFlrartikelklasse().getC_nr());
						}

					} else {
						dto.setSBezeichnung(lsPos.getC_bez());
					}
				} else if (lsPos
						.getPositionsart_c_nr()
						.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_TEXTEINGABE)) {
					dto.setSBezeichnung(lsPos.getC_textinhalt());
				}

				if (bVerdichtetNachArtikel) {
					boolean bGefunden = false;
					for (int j = 0; j < cResult.size(); j++) {
						KundeLieferstatistikDto temp = (KundeLieferstatistikDto) cResult
								.get(j);
						if (temp.getSIdent().equals(dto.getSIdent())) {
							if (dto.getNMenge() != null
									&& temp.getNMenge() != null) {
								BigDecimal mengeNeu = dto.getNMenge().add(
										temp.getNMenge());

								BigDecimal wertAlt = temp.getNPreis().multiply(
										temp.getNMenge());
								BigDecimal wertNeu = dto.getNPreis().multiply(
										dto.getNMenge());

								BigDecimal preisNeu = dto.getNPreis();
								if (mengeNeu.doubleValue() != 0) {
									preisNeu = wertNeu.add(wertAlt).divide(
											mengeNeu,
											BigDecimal.ROUND_HALF_EVEN);
								}
								temp.setNMenge(mengeNeu);
								temp.setNPreis(preisNeu);

								temp.setSnrs(SeriennrChargennrMitMengeDto
										.add2SnrChnrDtos(temp.getSnrs(),
												dto.getSnrs()));

								temp.setDWarenausgangsdatum(null);
								temp.setSLieferscheinnummer(null);
								temp.setSRechnungsnummer(null);
								cResult.set(j, temp);
								bGefunden = true;
								break;
							}
						}
					}
					if (bGefunden == false) {
						cResult.add(dto);
					}
				} else {
					cResult.add(dto);
				}

			}
			int row = 0;

			// Datenarray erzeugen
			data = new Object[cResult.size()][REPORT_STATISTIK_ANZAHL_FELDER];

			Collections.sort(cResult, new ComparatorKD(iSortierung.intValue()));

			if (bEingeschraenkt) {
				while (cResult.size() > 50) {
					cResult.remove(cResult.size() - 1);
				}
			}

			for (Iterator<KundeLieferstatistikDto> iter = cResult.iterator(); iter
					.hasNext();) {
				KundeLieferstatistikDto dto = (KundeLieferstatistikDto) iter
						.next();
				data[row][REPORT_STATISTIK_RE_OR_GS] = dto
						.getSWarenausgangverursacher();
				data[row][REPORT_STATISTIK_BEZEICHNUNG] = dto.getSBezeichnung();
				data[row][REPORT_STATISTIK_DATUM] = dto
						.getDWarenausgangsdatum();
				data[row][REPORT_STATISTIK_IDENT] = dto.getSIdent();
				data[row][REPORT_STATISTIK_LIEFERSCHEINNUMMER] = dto
						.getSLieferscheinnummer();
				data[row][REPORT_STATISTIK_MENGE] = dto.getNMenge();
				data[row][REPORT_STATISTIK_PREIS] = dto.getNPreis();
				data[row][REPORT_STATISTIK_MATERIALZUSCHLAG] = dto
						.getNMaterialzuschlag();
				data[row][REPORT_STATISTIK_RECHNUNGSNUMMER] = dto
						.getSRechnungsnummer();
				data[row][REPORT_STATISTIK_SERIENNUMMER] = SeriennrChargennrMitMengeDto
						.erstelleStringAusMehrerenSeriennummern(dto.getSnrs());
				data[row][REPORT_STATISTIK_LAND] = dto.getSLand();
				data[row][REPORT_STATISTIK_STATISTIKADRESSE] = dto
						.getSStatistikadresse();
				data[row][REPORT_STATISTIK_VERTRETER] = dto.getSVertreter();
				data[row][REPORT_STATISTIK_PROVISIONSEMPFAENGER] = dto
						.getSProvisionsempfaenger();
				data[row][REPORT_STATISTIK_PLZ] = dto.getSPlz();
				data[row][REPORT_STATISTIK_MWSTSATZ] = dto.getNMwstsatz();
				data[row][REPORT_STATISTIK_KUNDENNAME] = dto.getSKundenname();
				data[row][REPORT_STATISTIK_KONTO] = dto.getSKonto();
				data[row][REPORT_STATISTIK_ARTIKELGRUPPE] = dto
						.getSArtikelgruppe();
				data[row][REPORT_STATISTIK_ARTIKELKLASSE] = dto
						.getSArtikelklasse();
				data[row][REPORT_STATISTIK_SETARTIKEL_TYP] = dto
						.getSSetartikelTyp();

				data[row][REPORT_STATISTIK_ORT] = dto.getSOrt();
				data[row][REPORT_STATISTIK_ORT_STATISTIKADRESSE] = dto
						.getSOrtStatistikadresse();
				data[row][REPORT_STATISTIK_PLZ_STATISTIKADRESSE] = dto
						.getSPlzStatistikadresse();
				data[row][REPORT_STATISTIK_LAND_STATISTIKADRESSE] = dto
						.getSLandStatistikadresse();
				data[row][REPORT_STATISTIK_VERLEIHFAKTOR] = dto
						.getdVerleihfaktor();
				data[row][REPORT_STATISTIK_VERLEIHTAGE] = dto.getiVerleihtage();

				row++;
			}
			row++;
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		} finally {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, he);
			}
		}
		return data;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printLieferStatistik(TheClientDto theClientDto,
			Integer iIdkundeI, Integer artikelIId, Integer artikelgruppeIId,
			Date dVonI, Date dBisI, Integer iSortierungI,
			boolean bMitTexteingaben, boolean bVerdichtetNachArtikel,
			boolean bEingeschraenkt, boolean bMonatsstatistik,
			int iOptionAdresse, boolean bRechnungsdatum) {

		if (dBisI != null) {
			dBisI = Helper.cutDate(dBisI);
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(dBisI.getTime());
			cal.add(Calendar.DAY_OF_MONTH, 1);
			dBisI = new java.sql.Date(cal.getTimeInMillis());
		}

		long t = System.currentTimeMillis();
		myLogger.logData("KD>Lieferstatistik serverentrytime 0: " + t);
		myLogger.logData("KD>Lieferstatistik 1: 0");

		useCase = KundeReportFac.UC_REPORT_KUNDE_LIEFERSTATISTIK;

		if (bMonatsstatistik == true) {
			iSortierungI = Helper.SORTIERUNG_NACH_DATUM;
		}

		// Daten erzeugen
		// data = getDataLieferstatistik(theClientDto, iIdkundeI, artikelIId,
		// artikelgruppeIId, dVonI, dBisI, iSortierungI,
		// theClientDto.getMandant(), bMitTexteingaben,
		// bVerdichtetNachArtikel, bEingeschraenkt, iOptionAdresse,
		// bRechnungsdatum);
		setData(getDataLieferstatistik(theClientDto, iIdkundeI, artikelIId,
				artikelgruppeIId, dVonI, dBisI, iSortierungI,
				theClientDto.getMandant(), bMitTexteingaben,
				bVerdichtetNachArtikel, bEingeschraenkt, iOptionAdresse,
				bRechnungsdatum));

		String report = PartnerReportFac.REPORT_KUNDE_LIEFERSTATISTIK;

		if (bMonatsstatistik == true && data.length > 0) {

			ArrayList alMonate = new ArrayList();
			useCase = KundeReportFac.UC_REPORT_KUNDE_MONATSSTATISTIK;
			report = PartnerReportFac.REPORT_KUNDE_MONATSSTATISTIK;

			java.text.DateFormatSymbols symbols = new java.text.DateFormatSymbols(
					theClientDto.getLocUi());
			String[] defaultMonths = symbols.getMonths();

			Calendar cAktuell = Calendar.getInstance();
			cAktuell.setTimeInMillis(((Timestamp) data[data.length - 1][REPORT_STATISTIK_DATUM])
					.getTime());
			cAktuell.set(Calendar.DAY_OF_MONTH, 1);

			while (cAktuell.getTimeInMillis() <= ((Timestamp) data[0][REPORT_STATISTIK_DATUM])
					.getTime()) {
				BigDecimal menge = new BigDecimal(0);
				BigDecimal wert = new BigDecimal(0);
				for (int i = 0; i < data.length; i++) {
					Object[] zeile = data[i];
					java.util.Date d = (java.util.Date) zeile[REPORT_STATISTIK_DATUM];

					Calendar cZeile = Calendar.getInstance();
					cZeile.setTimeInMillis(d.getTime());

					if (cAktuell.get(Calendar.MONTH) == cZeile
							.get(Calendar.MONTH)
							&& cAktuell.get(Calendar.YEAR) == cZeile
									.get(Calendar.YEAR)) {
						BigDecimal mengeZeile = (BigDecimal) zeile[REPORT_STATISTIK_MENGE];
						BigDecimal preisZeile = (BigDecimal) zeile[REPORT_STATISTIK_PREIS];
						if (mengeZeile != null && preisZeile != null) {
							menge = menge.add(mengeZeile);
							wert = wert.add(mengeZeile.multiply(preisZeile));
						}
					}

				}

				Object[] zeileMonate = new Object[KundeReportFac.REPORT_MONATSSTATISTIK_ANZAHL_FELDER];
				zeileMonate[REPORT_MONATSSTATISTIK_MONAT] = defaultMonths[cAktuell
						.get(Calendar.MONTH)];
				zeileMonate[REPORT_MONATSSTATISTIK_JAHR] = cAktuell
						.get(Calendar.YEAR);
				zeileMonate[REPORT_MONATSSTATISTIK_MENGE] = menge;
				zeileMonate[REPORT_MONATSSTATISTIK_WERT] = wert;
				alMonate.add(zeileMonate);
				cAktuell.add(Calendar.MONTH, 1);
			}

			Object[][] dataTemp = new Object[1][1];
			// data = (Object[][]) alMonate.toArray(dataTemp);
			setData((Object[][]) alMonate.toArray(dataTemp));
		}

		JasperPrintLP print = null;
		HashMap<String, Object> parameter = null;
		try {
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(iIdkundeI,
					theClientDto);
			summePreis = BigDecimal.ZERO;

			parameter = new HashMap<String, Object>();
			parameter.put("P_KUNDE_NAME", kundeDto.getPartnerDto()
					.formatTitelAnrede());
			parameter.put("P_KUNDE_ANSCHRIFT", kundeDto.getPartnerDto()
					.formatLKZPLZOrt());

			if (artikelIId != null) {
				parameter.put("P_ARTIKEL", getArtikelFac()
						.artikelFindByPrimaryKeySmall(artikelIId, theClientDto)
						.formatArtikelbezeichnung());
			}
			if (artikelgruppeIId != null) {
				parameter.put("P_ARTIKELGRUPPE", getArtikelFac()
						.artgruFindByPrimaryKey(artikelgruppeIId, theClientDto)
						.getBezeichnung());
			}
			parameter.put("P_VON", dVonI);
			parameter.put("P_BIS", dBisI);
			if (Helper.SORTIERUNG_NACH_IDENT == iSortierungI) {
				parameter.put(
						"P_SORTIERUNG",
						getTextRespectUISpr("lp.artikel",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			} else {
				parameter.put(
						"P_SORTIERUNG",
						getTextRespectUISpr("lp.datum",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			}
			parameter.put("P_VERDICHTET", new Boolean(bVerdichtetNachArtikel));
			parameter.put("P_EINGESCHRAENKT", new Boolean(bEingeschraenkt));
			if (iOptionAdresse == REPORT_LIEFERSTATISTIK_OPTION_STATISTIKADRESSE) {
				parameter.put(
						"P_ADRESSE",
						getTextRespectUISpr("rech.statistikadresse",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			} else if (iOptionAdresse == REPORT_LIEFERSTATISTIK_OPTION_LIEFERADRESSE) {
				parameter.put(
						"P_ADRESSE",
						getTextRespectUISpr("lsch.lieferadreesse",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			} else if (iOptionAdresse == REPORT_LIEFERSTATISTIK_OPTION_RECHNUNGSADRESSE) {
				parameter.put(
						"P_ADRESSE",
						getTextRespectUISpr("lp.rechnungsadresse",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			}

			initJRDS(parameter, PartnerReportFac.REPORT_MODUL, report,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
			print = getReportPrint();
		} catch (Exception ex) {
			throw new EJBExceptionLP(ex);
		}

		myLogger.logData("KD>Lieferstatistik 2: "
				+ (System.currentTimeMillis() - t));
		t = System.currentTimeMillis();
		return print;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printKundenstammblatt(Integer kundeIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		useCase = KundeReportFac.UC_REPORT_KUNDE_STAMMBLATT;
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		try {
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId,
					theClientDto);

			parameter.put("P_NAME1", kundeDto.getPartnerDto()
					.getCName1nachnamefirmazeile1());
			parameter.put("P_NAME2", kundeDto.getPartnerDto()
					.getCName2vornamefirmazeile2());
			parameter.put("P_NAME3", kundeDto.getPartnerDto()
					.getCName3vorname2abteilung());
			parameter.put("P_STRASSE", kundeDto.getPartnerDto().getCStrasse());

			if (kundeDto.getPartnerDto().getLandplzortDto() != null) {
				parameter.put("P_LANDPLZORT", kundeDto.getPartnerDto()
						.getLandplzortDto().formatLandPlzOrt());
			}

			parameter.put("P_TELEFON", kundeDto.getPartnerDto().getCTelefon());

			parameter
					.put("P_HOMEPAGE", kundeDto.getPartnerDto().getCHomepage());

			parameter.put("P_FAX", kundeDto.getPartnerDto().getCFax());

			parameter.put("P_EMAIL", kundeDto.getPartnerDto().getCEmail());

			parameter.put("P_HINWEISEXTERN", kundeDto.getSHinweisextern());
			parameter.put("P_HINWEISINTERN", kundeDto.getSHinweisintern());

			String sKommentar = null;

			if (kundeDto.getPartnerDto().getXBemerkung() != null
					&& kundeDto.getPartnerDto().getXBemerkung().length() > 0) {
				sKommentar = getTextRespectUISpr("lp.partner",
						theClientDto.getMandant(), theClientDto.getLocUi());
				sKommentar += ":\n" + kundeDto.getPartnerDto().getXBemerkung()
						+ "\n";
			}

			if (kundeDto.getXKommentar() != null
					&& kundeDto.getXKommentar().length() > 0) {
				if (sKommentar == null) {
					sKommentar = getTextRespectUISpr("lp.kunde",
							theClientDto.getMandant(), theClientDto.getLocUi());
				} else {
					sKommentar += "\n"
							+ getTextRespectUISpr("lp.kunde",
									theClientDto.getMandant(),
									theClientDto.getLocUi());
				}
				sKommentar += ":\n" + kundeDto.getXKommentar();
			}

			parameter.put("P_KOMMENTAR", sKommentar);

			// Selektionen
			PASelektionDto[] paselDtos = getPartnerFac()
					.pASelektionFindByPartnerIId(kundeDto.getPartnerIId());
			if (paselDtos != null && paselDtos.length > 0) {
				String sPasel = "";
				for (int i = 0; i < paselDtos.length; i++) {
					PASelektionDto paselDto = paselDtos[i];
					SelektionDto selektionDto = getPartnerServicesFac()
							.selektionFindByPrimaryKey(
									paselDto.getSelektionIId(), theClientDto);
					sPasel += " " + (i + 1) + ": "
							+ selektionDto.getBezeichnung();
				}
				parameter.put("P_SELEKTIONEN", sPasel);
			}

			ArrayList<?> alAnsprechpartner = getAnsprechpartnerFac()
					.getAllAnsprechpartner(kundeDto.getPartnerIId(),
							theClientDto);

			// data = new Object[alAnsprechpartner.size()][8];
			setData(new Object[alAnsprechpartner.size()][8]);
			if (alAnsprechpartner.size() == 0) {
				data = new Object[0][8];
			}

			for (int i = 0; i < alAnsprechpartner.size(); i++) {
				AnsprechpartnerDto dtoTemp = (AnsprechpartnerDto) alAnsprechpartner
						.get(i);
				data[i][REPORT_KUNDENSTAMMBLATT_ANSPRECHPARTNER_NACHNAME] = dtoTemp
						.getPartnerDto().getCName1nachnamefirmazeile1();
				data[i][REPORT_KUNDENSTAMMBLATT_ANSPRECHPARTNER_VORNAME] = dtoTemp
						.getPartnerDto().getCName2vornamefirmazeile2();
				data[i][REPORT_KUNDENSTAMMBLATT_ANSPRECHPARTNER_TITEL] = dtoTemp
						.getPartnerDto().getCTitel();

				if (dtoTemp.getAnsprechpartnerfunktionIId() != null) {
					AnsprechpartnerfunktionDto dto = getAnsprechpartnerFac()
							.ansprechpartnerfunktionFindByPrimaryKey(
									dtoTemp.getAnsprechpartnerfunktionIId(),
									theClientDto);
					data[i][REPORT_KUNDENSTAMMBLATT_ANSPRECHPARTNER_FUNKTION] = dto
							.getBezeichnung();
				}

				data[i][REPORT_KUNDENSTAMMBLATT_ANSPRECHPARTNER_FAXDW] = dtoTemp
						.getCFax();

				data[i][REPORT_KUNDENSTAMMBLATT_ANSPRECHPARTNER_TELDW] = dtoTemp
						.getCTelefon();

				data[i][REPORT_KUNDENSTAMMBLATT_ANSPRECHPARTNER_MOBIL] = dtoTemp
						.getCHandy();

				data[i][REPORT_KUNDENSTAMMBLATT_ANSPRECHPARTNER_EMAIL] = dtoTemp
						.getCEmail();

			}

			// Auftraege

			Session session = FLRSessionFactory.getFactory().openSession();
			// Filter und Sortierung
			Criteria crit = session.createCriteria(FLRAuftragReport.class);
			// Filter nach Kunde
			crit.add(Restrictions
					.eq(AuftragFac.FLR_AUFTRAG_KUNDE_I_ID_AUFTRAGSADRESSE,
							kundeIId));
			crit.addOrder(Order.desc(AuftragFac.FLR_AUFTRAG_C_NR));

			List<?> list = crit.list();
			Iterator<?> it = list.iterator();
			ArrayList<Object[]> al = new ArrayList<Object[]>();
			while (it.hasNext()) {
				FLRAuftragReport flrauftrag = (FLRAuftragReport) it.next();

				Object[] o = new Object[7];
				o[0] = flrauftrag.getC_nr();
				o[1] = flrauftrag.getAuftragstatus_c_nr();

				BigDecimal wert = flrauftrag
						.getN_gesamtauftragswertinauftragswaehrung();

				if (wert != null) {

					wert = getLocaleFac()
							.rechneUmInMandantenWaehrung(
									wert,
									new BigDecimal(
											flrauftrag
													.getF_wechselkursmandantwaehrungzuauftragswaehrung()));

					o[2] = wert;
				}
				o[3] = null; // todo;
				o[4] = flrauftrag.getT_liefertermin();
				o[5] = flrauftrag.getC_bez();
				o[6] = flrauftrag.getC_bestellnummer();
				al.add(o);
			}

			if (al.size() > 0) {
				String[] fieldnames = new String[] { "Auftragsnummer",
						"Status", "Wert", "OffenerWert", "Liefertermin",
						"Projekt", "Bestellnummer" };
				Object[][] dataSub = new Object[al.size()][fieldnames.length];
				dataSub = (Object[][]) al.toArray(dataSub);

				parameter
						.put("P_SUBREPORT_AUFTRAEGE",
								((net.sf.jasperreports.engine.JRDataSource) new LPDatenSubreport(
										dataSub, fieldnames)));
			}
			session.close();

			// Kurzbriefe
			session = FLRSessionFactory.getFactory().openSession();
			// Filter und Sortierung
			crit = session.createCriteria(FLRKurzbrief.class);
			// Filter nach Kunde
			crit.add(Restrictions.eq("partner_i_id", kundeDto.getPartnerIId()));
			crit.addOrder(Order.desc("t_aendern"));

			list = crit.list();
			it = list.iterator();
			al = new ArrayList<Object[]>();
			while (it.hasNext()) {
				FLRKurzbrief kurzbrief = (FLRKurzbrief) it.next();

				Object[] o = new Object[7];
				o[0] = kurzbrief.getC_betreff();

				if (kurzbrief.getFlransprechpartner() != null) {
					AnsprechpartnerDto oAnsprechpartner = getAnsprechpartnerFac()
							.ansprechpartnerFindByPrimaryKey(
									kurzbrief.getFlransprechpartner().getI_id(),
									theClientDto);
					o[1] = oAnsprechpartner.getPartnerDto().formatAnrede();
				}
				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKey(
								kurzbrief.getPersonal_i_id_aendern(),
								theClientDto);

				o[2] = personalDto.getPartnerDto().formatAnrede();
				o[3] = kurzbrief.getT_aendern();
				al.add(o);
			}

			if (al.size() > 0) {
				String[] fieldnames = new String[] { "Betreff",
						"Ansprechpartner", "Person", "Zeitpunkt" };
				Object[][] dataSub = new Object[al.size()][fieldnames.length];
				dataSub = (Object[][]) al.toArray(dataSub);

				parameter
						.put("P_SUBREPORT_KURZBRIEFE",
								((net.sf.jasperreports.engine.JRDataSource) new LPDatenSubreport(
										dataSub, fieldnames)));
			}

			// Umsaetze der vergangenen Jahre

			al = new ArrayList<Object[]>();

			Calendar cVon = Calendar.getInstance();
			cVon = Helper.cutCalendar(cVon);
			cVon.set(Calendar.MONTH, Calendar.JANUARY);
			cVon.set(Calendar.DAY_OF_MONTH, 1);

			Calendar cBis = Calendar.getInstance();
			cBis = Helper.cutCalendar(cBis);
			cBis.set(Calendar.MONTH, Calendar.DECEMBER);
			cBis.set(Calendar.DAY_OF_MONTH, 31);

			for (int i = 0; i < 10; i++) {

				int iJahr = cVon.get(Calendar.YEAR);

				java.sql.Date dVon = new java.sql.Date(cVon.getTime().getTime());
				java.sql.Date dBis = new java.sql.Date(cBis.getTime().getTime());

				// den Umsatz errechnen
				BigDecimal bdUmsatz = getRechnungFac()
						.getUmsatzVomKundenImZeitraum(theClientDto, kundeIId,
								dVon, dBis, false);
				Object[] o = new Object[2];
				o[0] = new Integer(iJahr);
				o[1] = bdUmsatz;
				cVon.set(Calendar.YEAR, cVon.get(Calendar.YEAR) - 1);
				cBis.set(Calendar.YEAR, cBis.get(Calendar.YEAR) - 1);
				al.add(o);
			}

			if (al.size() > 0) {
				String[] fieldnames = new String[] { "Jahr", "Umsatz" };
				Object[][] dataSub = new Object[al.size()][fieldnames.length];
				dataSub = (Object[][]) al.toArray(dataSub);

				parameter
						.put("P_SUBREPORT_UMSAETZE",
								((net.sf.jasperreports.engine.JRDataSource) new LPDatenSubreport(
										dataSub, fieldnames)));
			}

			// Umsaetze der vergangenen Geschaeftsjahre

			Integer gfJahr = getParameterFac().getGeschaeftsjahr(
					theClientDto.getMandant());

			al = new ArrayList<Object[]>();

			for (int i = 0; i < 10; i++) {

				int iJahr = gfJahr;

				Timestamp[] tVonBis = getBuchenFac()
						.getDatumVonBisGeschaeftsjahr(gfJahr, theClientDto);

				java.sql.Date dVon = new java.sql.Date(tVonBis[0].getTime());
				java.sql.Date dBis = new java.sql.Date(tVonBis[1].getTime());

				// den Umsatz errechnen
				BigDecimal bdUmsatz = getRechnungFac()
						.getUmsatzVomKundenImZeitraum(theClientDto, kundeIId,
								dVon, dBis, false);
				Object[] o = new Object[2];
				o[0] = new Integer(iJahr);
				o[1] = bdUmsatz;

				al.add(o);

				gfJahr--;
			}

			if (al.size() > 0) {
				String[] fieldnames = new String[] { "Jahr", "Umsatz" };
				Object[][] dataSub = new Object[al.size()][fieldnames.length];
				dataSub = (Object[][]) al.toArray(dataSub);

				parameter
						.put("P_SUBREPORT_UMSAETZEGFJAHR",
								((net.sf.jasperreports.engine.JRDataSource) new LPDatenSubreport(
										dataSub, fieldnames)));
			}

			// Kontakte

			// Kurzbriefe
			session = FLRSessionFactory.getFactory().openSession();
			// Filter und Sortierung
			crit = session.createCriteria(FLRKontakt.class);
			// Filter nach Kunde
			crit.add(Restrictions.eq("partner_i_id", kundeDto.getPartnerIId()));
			crit.addOrder(Order.desc("t_kontakt"));

			list = crit.list();
			it = list.iterator();
			al = new ArrayList<Object[]>();
			while (it.hasNext()) {
				FLRKontakt kontakt = (FLRKontakt) it.next();

				Object[] o = new Object[7];
				o[0] = kontakt.getC_titel();
				o[1] = kontakt.getFlrkontaktart().getC_bez();
				o[2] = kontakt.getT_kontakt();
				o[3] = kontakt.getT_kontaktbis();

				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKey(
								kontakt.getFlrpersonal().getI_id(),
								theClientDto);

				o[4] = personalDto.getPartnerDto().formatAnrede();
				al.add(o);
			}

			if (al.size() > 0) {
				String[] fieldnames = new String[] { "Titel", "Kontaktart",
						"Von", "Bis", "Zugewiesener" };
				Object[][] dataSub = new Object[al.size()][fieldnames.length];
				dataSub = (Object[][]) al.toArray(dataSub);

				parameter
						.put("P_SUBREPORT_KONTAKTE",
								((net.sf.jasperreports.engine.JRDataSource) new LPDatenSubreport(
										dataSub, fieldnames)));
			}

			// Angebote
			session = FLRSessionFactory.getFactory().openSession();
			// Filter und Sortierung
			crit = session.createCriteria(FLRAngebot.class);
			// Filter nach Kunde
			crit.add(Restrictions
					.eq(AngebotFac.FLR_ANGEBOT_KUNDE_I_ID_ANGEBOTSADRESSE,
							kundeIId));
			crit.addOrder(Order.desc("c_nr"));

			list = crit.list();
			it = list.iterator();
			al = new ArrayList<Object[]>();
			while (it.hasNext()) {
				FLRAngebot angebot = (FLRAngebot) it.next();

				Object[] o = new Object[8];
				o[0] = angebot.getC_nr();
				o[1] = angebot.getAngebotstatus_c_nr();

				BigDecimal wert = angebot
						.getN_gesamtangebotswertinangebotswaehrung();

				if (wert != null) {

					wert = getLocaleFac()
							.rechneUmInMandantenWaehrung(
									wert,
									new BigDecimal(
											angebot.getF_wechselkursmandantwaehrungzuangebotswaehrung()));

					o[2] = wert;
				}
				o[3] = angebot.getT_belegdatum();
				o[4] = angebot.getAngeboterledigungsgrund_c_nr();
				o[5] = angebot.getT_manuellerledigt();

				AuftragDto[] aAuftragDto = getAuftragFac()
						.auftragFindByAngebotIId(angebot.getI_id(),
								theClientDto);

				String s = "";
				for (int i = 0; i < aAuftragDto.length; i++) {
					s += aAuftragDto[i].getCNr() + ", ";
				}
				o[6] = s;

				o[7] = angebot.getC_bez();

				al.add(o);
			}

			if (al.size() > 0) {
				String[] fieldnames = new String[] { "Angebotsnummer",
						"Status", "Wert", "Belegdatum", "Erledigungsgrund",
						"Erledigungsdatum", "Erledigungsauftrag", "Projekt" };
				Object[][] dataSub = new Object[al.size()][fieldnames.length];
				dataSub = (Object[][]) al.toArray(dataSub);

				parameter
						.put("P_SUBREPORT_ANGEBOTE",
								((net.sf.jasperreports.engine.JRDataSource) new LPDatenSubreport(
										dataSub, fieldnames)));
			}
			session.close();

			parameter.put(
					"P_SUBREPORT_PROJEKTE",
					getSubreportProjekte(kundeDto.getPartnerIId(), false,
							theClientDto));

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		parameter.put("P_MANDANTENWAEHRUNG",
				theClientDto.getSMandantenwaehrung());

		initJRDS(parameter, KundeReportFac.REPORT_MODUL,
				KundeReportFac.REPORT_KUNDENSTAMMBLATT,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();
	}

	public LPDatenSubreport getSubreportProjekte(Integer partnerIId,
			boolean bNurOffene, TheClientDto theClientDto) {
		// Projekte
		Session session = FLRSessionFactory.getFactory().openSession();
		// Filter und Sortierung
		Criteria crit = session.createCriteria(FLRProjekt.class);
		// Filter nach Kunde
		crit.add(Restrictions.eq("partner_i_id", partnerIId));
		crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));

		if (bNurOffene) {
			crit.add(Restrictions.in("status_c_nr", new String[] {
					ProjektServiceFac.PROJEKT_STATUS_ANGELEGT,
					ProjektServiceFac.PROJEKT_STATUS_OFFEN }));
		}

		crit.addOrder(Order.desc("c_nr"));

		List<?> list = crit.list();
		Iterator it = list.iterator();
		ArrayList al = new ArrayList<Object[]>();
		while (it.hasNext()) {
			FLRProjekt projekt = (FLRProjekt) it.next();

			Object[] o = new Object[11];
			o[0] = projekt.getC_nr();
			o[1] = projekt.getC_titel();
			o[2] = projekt.getTyp_c_nr();

			PersonalDto personalDto = getPersonalFac()
					.personalFindByPrimaryKey(
							projekt.getFlrpersonalZugewiesener().getI_id(),
							theClientDto);

			o[3] = personalDto.getPartnerDto().formatAnrede();
			o[4] = projekt.getT_anlegen();
			o[5] = projekt.getStatus_c_nr();
			o[6] = projekt.getD_dauer();
			o[7] = projekt.getKategorie_c_nr();
			o[8] = projekt.getI_prio();
			o[9] = projekt.getN_umsatzgeplant();
			o[10] = projekt.getI_wahrscheinlichkeit();

			al.add(o);
		}

		if (al.size() > 0) {
			String[] fieldnames = new String[] { "Projektnummer", "Titel",
					"Typ", "Zugewiesener", "Anlagedatum", "Status",
					"Schaetzung", "Kategorie", "Prio", "UmsatzGeplant",
					"Wahrscheinlichkeit" };
			Object[][] dataSub = new Object[al.size()][fieldnames.length];
			dataSub = (Object[][]) al.toArray(dataSub);

			return new LPDatenSubreport(dataSub, fieldnames);
		}
		session.close();
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printKundenstatistik(
			StatistikParamDto statistikParamDtoI) {
		return null;
	}

	public class KundenpreislisteTransformer implements IJasperPrintTransformer {
		private CustomerPricelistReportDto reportDto;
		private String[] fieldnames = new String[] { "Menge", "Basis",
				"BasisPreis", "Fixpreis", "Rabattsatz", "BerechneterPreis",
				"Waehrung", "Soko" };
		private int totalSize;

		public KundenpreislisteTransformer(CustomerPricelistReportDto reportDto) {
			this.reportDto = reportDto;
			totalSize = this.reportDto.getItems().size();
			if (this.reportDto.getWithClientLanguage())
				totalSize += totalSize;
		}

		public boolean next(int index) {
			return index < totalSize;
		}

		public HashMap<String, Object> transformParameter() {
			HashMap<String, Object> parameter = new HashMap<String, Object>();
			parameter.put("P_KUNDE", reportDto.getCustomer().getValue());
			parameter.put("P_ARTIKELGRUPPE",
					reportDto.getItemgroup() != null ? reportDto.getItemgroup()
							.getValue() : null);
			parameter.put("P_ARTIKELKLASSE",
					reportDto.getItemclass() != null ? reportDto.getItemclass()
							.getValue() : null);
			parameter.put("P_ARTIKELNRVON", reportDto.getItemRangeFrom());
			parameter.put("P_ARTIKELNRBIS", reportDto.getItemRangeTo());
			parameter.put("P_MITVERSTECKTEN", reportDto.getWithHidden());
			parameter.put("P_NURSOKO", reportDto.getOnlySpecialCondition());
			parameter.put("P_MITMANDANTENSPRACHE",
					reportDto.getWithClientLanguage());
			parameter.put("P_PREISGUELTIGKEIT",
					new Date(reportDto.getPriceValidityMs()));
			return parameter;
		}

		public Object transformData(int rowIndex, String keyName) {
			int index = rowIndex;
			if (reportDto.getWithClientLanguage()) {
				index >>= 1;
			}
			CustomerPricelistItemDescriptionDto descDto = reportDto
					.getWithClientLanguage() && ((rowIndex & 1) == 1) ? reportDto
					.getItems().get(index).getClientDescriptionDto()
					: reportDto.getItems().get(index).getDescriptionDto();
			// CustomerPricelistItemDescriptionDto descDto =
			// reportDto.getItems().get(index).getDescriptionDto() ;
			// if(reportDto.getWithClientLanguage()) {
			// if((rowIndex & 1) == 1) {
			// descDto =
			// reportDto.getItems().get(index).getClientDescriptionDto() ;
			// }
			// }
			if ("Artikelnummer".equals(keyName)) {
				return reportDto.getItems().get(index).getItem().getValue();
			} else if ("Bezeichnung".equals(keyName)) {
				// return reportDto.getItems().get(index).getName() ;
				return descDto.getName();
			} else if ("Kurzbezeichnung".equals(keyName)) {
				// return reportDto.getItems().get(index).getShortName() ;
				return descDto.getShortName();
			} else if ("Zusatzbezeichnung".equals(keyName)) {
				// return reportDto.getItems().get(index).getAdditionalName() ;
				return descDto.getAdditionalName();
			} else if ("Zusatzbezeichnung2".equals(keyName)) {
				// return reportDto.getItems().get(index).getAdditionalName2() ;
				return descDto.getAdditionalName2();
			} else if ("Versteckt".equals(keyName)) {
				return reportDto.getItems().get(index).getHidden();
			} else if ("Artikelgruppe".equals(keyName)) {
				return reportDto.getItems().get(index).getItemGroupDto()
						.getValue();
			} else if ("Artikelklasse".equals(keyName)) {
				return reportDto.getItems().get(index).getItemClass();
			} else if ("SubreportPreise".equals(keyName)) {
				return createPreiseSubreport(reportDto.getItems().get(index)
						.getPrices());
			} else if ("EnthaeltSokos".equals(keyName)) {
				return reportDto.getItems().get(index).getSpecialCondition();
			}

			return null;
		}

		private LPDatenSubreport createPreiseSubreport(
				List<CustomerPricelistPriceDto> preise) {
			Object[][] dataSub = new Object[preise.size()][fieldnames.length];
			for (int i = 0; i < preise.size(); i++) {
				CustomerPricelistPriceDto preisDto = preise.get(i);
				dataSub[i][0] = preisDto.getAmount();
				dataSub[i][1] = preisDto.getPricetypKey();
				dataSub[i][2] = preisDto.getBasePrice();
				dataSub[i][3] = preisDto.getFixPrice();
				dataSub[i][4] = preisDto.getDiscountRate();
				dataSub[i][5] = preisDto.getCalculatedPrice();
				dataSub[i][6] = preisDto.getCurrency();
				dataSub[i][7] = preisDto.getSpecialCondition();
			}
			return new LPDatenSubreport(dataSub, fieldnames);
		}
	}
}
