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
package com.lp.server.angebot.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.anfrage.service.ZertifikatartDto;
import com.lp.server.angebot.fastlanereader.generated.FLRAngebot;
import com.lp.server.angebot.fastlanereader.generated.FLRAngebotposition;
import com.lp.server.angebot.fastlanereader.generated.FLRAngebotpositionReport;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotFac;
import com.lp.server.angebot.service.AngebotReportFac;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.angebot.service.AngeboterledigungsgrundDto;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.angebot.service.AngebotpositionFac;
import com.lp.server.angebot.service.AngebottextDto;
import com.lp.server.angebot.service.ReportAngebotJournalKriterienDto;
import com.lp.server.angebot.service.ReportAngebotpositionJournalDto;
import com.lp.server.angebot.service.ReportAngebotsstatistikDto;
import com.lp.server.angebot.service.ReportAngebotsstatistikKriterienDto;
import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.angebotstkl.service.AgstklpositionDto;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.MaterialDto;
import com.lp.server.artikel.service.MaterialzuschlagDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.bestellung.service.BestellungReportFac;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklisteMitStrukturDto;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.ejb.Mwstsatz;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.BelegartDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MediastandardDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.system.service.SpediteurDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.BelegPositionDruckIdentDto;
import com.lp.server.util.BelegPositionDruckTextbausteinDto;
import com.lp.server.util.HelperServer;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.server.util.report.MwstsatzReportDto;
import com.lp.server.util.report.TimingInterceptor;
import com.lp.service.StuecklisteInfoDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.report.PositionRpt;

@Stateless
@Interceptors(TimingInterceptor.class)
public class AngebotReportFacBean extends LPReport implements AngebotReportFac,
		JRDataSource {

	private String cAktuellerReport = null;
	private Object[][] data = null;

	public boolean next() throws JRException {
		index++;
		return (index < data.length);
	}

	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;
		String fieldName = field.getName();
		if (cAktuellerReport.equals(AngebotReportFac.REPORT_ANGEBOT)) {
			if ("F_POSITION".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_POSITION];
			}
			if ("F_POSITION_NR".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_POSITION_NR];
			} else if ("F_IDENT".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_IDENT];
			} else if ("F_MENGE".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_MENGE];
			} else if ("F_EINHEIT".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_EINHEIT];
			} else if ("F_EINZELPREIS".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_EINZELPREIS];
			} else if ("F_RABATTSATZ".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_RABATT];
			} else if ("F_ZUSATZRABATTSATZ".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_ZUSATZRABATTSATZ];
			} else if ("F_GESAMTPREIS".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_GESAMTPREIS];
			} else if ("F_MWSTSATZ".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_MWSTSATZ];
			} else if ("F_MWSTBETRAG".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_MWSTBETRAG];
			} else if ("F_MATERIALZUSCHLAG".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_MATERIALZUSCHLAG];
			} else if ("F_POSITIONSART".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_POSITIONSART];
			} else if ("F_FREIERTEXT".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][AngebotReportFac.REPORT_ANGEBOT_FREIERTEXT]);
			} else if ("F_LEERZEILE".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_LEERZEILE];
			} else if ("F_IMAGE".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_IMAGE];
			} else if ("F_SEITENUMBRUCH".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_SEITENUMBRUCH];
			} else if ("F_TYP_CNR".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_TYP_CNR];
			} else if ("F_SETARTIKEL_TYP".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_SETARTIKEL_TYP];
			} else if ("F_VERLEIHTAGE".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_VERLEIHTAGE];
			} else if ("F_VERLEIHFAKTOR".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_VERLEIHFAKTOR];
			}
			// unterstkl: 3 die folgenden 4 Felder fuer Druck der
			// Stuecklistenpositionen auslesen
			// diese Felder muessen im zugehoerigen Report definiert und im
			// Layout gesetzt werden
			// bitte am Beispiel von angb_angebot.jrxml orientieren, die Felder
			// sind mit dem
			// Andrucken der Ident und Handeingabepositionen ueberlagert und
			// muessen mit der
			// richtigen Bedingung zum Andrucken versehen werden.
			// Das Feld F_STKLPOSITION ist obsolet und muss auch im Report
			// selbst entfernt werden.
			else if ("F_STKLMENGE".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_STKLMENGE];
			} else if ("F_STKLEINHEIT".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_STKLEINHEIT];
			} else if ("F_STKLARTIKELCNR".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][AngebotReportFac.REPORT_ANGEBOT_STKLARTIKELCNR]);
			} else if ("F_STKLARTIKELBEZ".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][AngebotReportFac.REPORT_ANGEBOT_STKLARTIKELBEZ]);
			} else if ("F_STKLARTIKELKBEZ".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][AngebotReportFac.REPORT_ANGEBOT_STKLARTIKELKBEZ]);
			} else if ("F_STKLARTIKEL_KDARTIKELNR".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][AngebotReportFac.REPORT_ANGEBOT_STKLARTIKEL_KDARTIKELNR]);
			} else if ("F_STKLARTIKEL_KDPREIS".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_STKLARTIKEL_KDPREIS];
			} else if ("F_B_ALTERNATIVE".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_B_ALTERNATIVE];
			} else if ("F_TEXTNACHENDSUMME".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_TEXTNACHENDSUMME];
			} else if (F_IDENTNUMMER.equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][AngebotReportFac.REPORT_ANGEBOT_IDENTNUMMER]);
			} else if (F_BEZEICHNUNG.equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][AngebotReportFac.REPORT_ANGEBOT_BEZEICHNUNG]);
			} else if (F_KURZBEZEICHNUNG.equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_KURZBEZEICHNUNG];
			} else if (F_KUNDEARTIKELNR.equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_KUNDEARTIKELNR];
			} else if (F_REFERENZNUMMER.equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_REFERENZNUMMER];
			} else if (F_ZUSATZBEZEICHNUNG2.equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_ARTIKELCZBEZ2];
			} else if (F_ZUSATZBEZEICHNUNG.equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_ZUSATZBEZEICHNUNG];
			} else if (F_ARTIKEL_BAUFORM.equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_BAUFORM];
			} else if (F_ARTIKEL_VERPACKUNGSART.equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_VERPACKUNGSART];
			} else if (F_ARTIKEL_MATERIAL.equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_MATERIAL];
			} else if (F_ARTIKEL_MATERIALGEWICHT.equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_MATERIALGEWICHT];
			} else if (F_ARTIKEL_KURS_MATERIALZUSCHLAG.equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_KURS_MATERIALZUSCHLAG];
			} else if (F_ARTIKEL_DATUM_MATERIALZUSCHLAG.equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_DATUM_MATERIALZUSCHLAG];
			} else if ("F_ARTIKEL_WERBEABGABEPFLICHTIG".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_WERBEABGABEPFLICHTIG];
			} else if (F_ARTIKEL_BREITE.equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_BREITE];
			} else if (F_ARTIKEL_HOEHE.equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_HOEHE];
			} else if (F_ARTIKEL_TIEFE.equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_TIEFE];
			} else if ("F_IDENT_TEXTEINGABE".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_IDENT_TEXTEINGABE];
			} else if ("F_POSITIONSOBJEKT".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_POSITIONOBJEKT];
			} else if ("F_ZERTIFIKATART".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_ZERTIFIKATART];
			} else if ("F_ARTIKEL_INDEX".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_INDEX];
			} else if ("F_ARTIKEL_REVISION".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_REVISION];
			} else if ("F_VONPOSITION".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOT_VONPOSITION];
			} else if ("F_BISPOSITION".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOT_BISPOSITION];
			} else if ("F_ZWSNETTOSUMME".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOT_ZWSNETTOSUMME];
			} else if ("F_ZWSTEXT".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOT_ZWSTEXT];
			} else if ("F_LV_POSITION".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_LVPOSITION];
			}
		} else if (cAktuellerReport
				.equals(AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ALLE)
				|| cAktuellerReport
						.equals(AngebotReportFac.REPORT_ANGEBOT_JOURNAL_OFFENE)) {
			if ("F_ANGEBOTIID".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANGEBOTIID];
			} else if ("F_ANGEBOTCNR".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANGEBOTCNR];
			} else if ("F_KUNDECNAME1".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDECNAME1];
			} else if ("F_KUNDECNAME2".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDECNAME2];
			} else if ("F_KUNDECNAME3".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDECNAME3];
			} else if ("F_KUNDESTRASSE".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDESTRASSE];
			} else if ("F_KUNDEPLZ".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDEPLZ];
			} else if ("F_KUNDEORT".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDEORT];
			} else if ("F_KUNDELKZ".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDELKZ];
			} else if ("F_KUNDETELEFON".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDETELEFON];
			} else if ("F_KUNDEFAX".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDEFAX];
			} else if ("F_KUNDEEMAIL".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDEEMAIL];
			} else if ("F_KUNDEKUNDEHOMEPAGE".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDEHOMEPAGE];
			} else if ("F_ANSPRECHPARTNER_VORNAME".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_VORNAME];
			} else if ("F_ANSPRECHPARTNER_NACHNAME".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_NACHNAME];
			} else if ("F_ANSPRECHPARTNER_TITEL".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_TITEL];
			} else if ("F_ANSPRECHPARTNER_ANREDE".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_ANREDE];
			} else if ("F_ANSPRECHPARTNER_TELEFON".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_TELEFON];
			} else if ("F_ANSPRECHPARTNER_TELEFONDW".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_TELEFONDW];
			} else if ("F_ANSPRECHPARTNER_FAX".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_FAX];
			} else if ("F_ANSPRECHPARTNER_FAXDW".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_FAXDW];
			} else if ("F_ANSPRECHPARTNER_EMAIL".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_EMAIL];

			} else if ("F_KOSTENSTELLECNR".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KOSTENSTELLECNR];
			} else if ("F_VERTRETERCNAME1".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_VERTRETERCNAME1];
			} else if ("F_PROJEKTBEZ".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_PROJEKTBEZ];
			} else if ("F_REALISIERUNGSTERMIN".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_REALISIERUNGSTERMIN];
			} else if ("F_INTERNERKOMMENTAR".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_INTERNERKOMMENTAR]);
			} else if ("F_ARTIKELCNR".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ARTIKELCNR];
			} else if ("F_ARTIKELBEZ".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ARTIKELBEZ];
			} else if ("F_ARTIKELMENGE".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ARTIKELMENGE];
			} else if ("F_ARTIKELEINHEIT".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ARTIKELEINHEIT];
			} else if ("F_ARTIKELPREIS".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ARTIKELPREIS];
			} else if ("F_ERLEDIGUNGSGRUND".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANGEBOTERLEDIGUNGSGRUND];
			} else if ("F_INTERNERKOMMENTAR".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_INTERNERKOMMENTAR];
			} else if ("F_EXTERNERKOMMENTAR".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_EXTERNERKOMMENTAR];
			} else if ("F_AUFTRAGSWAHRSCHEINLICHKEIT".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_AUFTRAGWAHRSCHEINLICHKEIT];
			} else if ("F_WERT".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANGEBOTWERT];
			} else if ("F_ERLEDIGUNGSGRUND_AB_NR".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ERLEDIGUNGSGRUND_AB_NR];
			}
		} else if (cAktuellerReport
				.equals(AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE)) {
			if ("F_ANGEBOTIID".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_ANGEBOTIID];
			} else if ("F_ANGEBOTCNR".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_ANGEBOTCNR];
			} else if ("F_KUNDECNAME1".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_KUNDECNAME1];
			} else if ("F_KOSTENSTELLECNR".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_KOSTENSTELLECNR];
			} else if ("F_VERTRETERCNAME1".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_VERTRETERCNAME1];
			} else if ("F_PROJEKTBEZ".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_PROJEKTBEZ];
			} else if ("F_REALISIERUNGSTERMIN".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_REALISIERUNGSTERMIN];
			} else if ("F_INTERNERKOMMENTAR".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_INTERNERKOMMENTAR]);
			} else if ("F_EXTERNERKOMMENTAR".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_EXTERNERKOMMENTAR]);
			} else if ("F_GESAMTANGEBOTSWERT".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_GESAMTANGEBOTSWERT];
			} else if ("F_ERLEDIGUNGSGRUND".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_ANGEBOTERLEDIGUNGSGRUND];
			} else if ("F_ERLEDIGUNGSGRUND_AB_NR".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_ERLEDIGUNGSGRUND_AB_NR];
			}
		}

		else if (cAktuellerReport
				.equals(AngebotReportFac.REPORT_VORKALKULATION)) {
			if ("F_IDENT".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][AngebotReportFac.REPORT_VORKALKULATION_IDENT]);
			} else if (F_BEZEICHNUNG.equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_BEZEICHNUNG];
			} else if (F_ZUSATZBEZEICHNUNG.equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_ZUSATZBEZEICHNUNG];
			} else if ("F_MENGE".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_MENGE];
			} else if ("F_EINHEIT".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_EINHEIT];
			} else if ("F_MENGE_UEBERGEORDNET".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_MENGE_UEBERGEORDNET];
			} else if ("F_EINHEIT_UEBERGEORDNET".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_EINHEIT_UEBERGEORDNET];
			} else if ("F_GESTEHUNGSPREIS_MANUELL".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS_MANUELL];
			} else if ("F_GESTEHUNGSPREIS".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS];
			} else if ("F_GESTEHUNGSWERT_MANUELL".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT_MANUELL];
			} else if ("F_GESTEHUNGSWERT".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT];
			} else if ("F_VERKAUFSPREIS".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_VERKAUFSPREIS];
			} else if ("F_VERKAUFSWERT".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_VERKAUFSWERT];
			} else if ("F_POSITIONSART".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_POSITIONSART];
			} else if ("F_INDEX_GRUPPE".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_INDEX_GRUPPE];
			} else if ("F_FIKTIVERLAGERSTAND".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_FIKTIVER_LAGERSTAND];
			} else if ("F_LIEF1PREIS".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_LIEF1PREIS];
			} else if ("F_LIEF1PREISGUELTIGBIS".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_LIEF1PREISGUELTIGBIS];
			} else if ("F_LIEF1PREISSTAFFEL".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_LIEF1PREISSTAFFEL];
			} else if ("F_TPKOSTENLIEFERUNG".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_TPKOSTENLIEFERUNG];
			} else if ("F_LIEFERANT".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_LIEFERANT];
			} else if ("F_POSITIONSNUMMER".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_POSITIONSNUMMER];
			} else if ("F_MATERIALZUSCHLAG".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_MATERIALZUSCHLAG];
			} else if ("F_MATERIAL".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_MATERIAL];
			}else if ("F_SETARTIKEL_TYP".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_SETARTIKEL_TYP];
			}
		} else if (cAktuellerReport
				.equals(AngebotReportFac.REPORT_ANGEBOT_ADRESSETIKETT)) {
			if ("F_ABSENDER_NAME".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ADRESSETIKETT_ABSENDER_NAME];
			} else if ("F_ABSENDER_PLZORT".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ADRESSETIKETT_ABSENDER_PLZORT];
			} else if ("F_ABSENDER_STRASSE".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ADRESSETIKETT_ABSENDER_STRASSE];
			} else if ("F_BILD".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ADRESSETIKETT_BILD];
			} else if ("F_NAME".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ADRESSETIKETT_NAME];
			} else if ("F_PLZORT".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ADRESSETIKETT_PLZORT];
			} else if ("F_STRASSE".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ADRESSETIKETT_STRASSE];
			} else if ("F_ZHD_NAME".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ADRESSETIKETT_ZHD_NAME];
			} else if ("F_LAND".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ADRESSETIKETT_LAND];
			}
		} else if (cAktuellerReport
				.equals(AngebotReportFac.REPORT_ANGEBOTSSTATISTIK)) {
			if ("Angebotenemenge".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOTSSTATISTIK_ANGEBOTENEMENGE];
			} else if ("Angebotenerpreis".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOTSSTATISTIK_ANGEBOTENERPREIS];
			} else if ("Belegdatum".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOTSSTATISTIK_BELEGDATUM];
			} else if ("Cnr".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOTSSTATISTIK_CNR];
			} else if ("Kunde".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOTSSTATISTIK_KUNDE];
			} else if ("Materialzuschlag".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOTSSTATISTIK_MATERIALZUSCHLAG];
			}
		} else if (cAktuellerReport
				.equals(AngebotReportFac.REPORT_ANGEBOTSPOTENTIAL)) {
			if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOTSPOTENTIAL_ARTIKELBEZEICHNUNG];
			} else if ("Artikelnummer".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOTSPOTENTIAL_ARTIKELNUMMER];
			} else if ("Belegdatum".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOTSPOTENTIAL_BELEGDATUM];
			} else if ("Belegnummer".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOTSPOTENTIAL_BELEGNUMMER];
			} else if ("Gueltigbis".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOTSPOTENTIAL_GUELTIGBIS];
			} else if ("Kunde".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOTSPOTENTIAL_KUNDE];
			} else if ("KundeKurzbezeichnung".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOTSPOTENTIAL_KUNDE_KURZBEZEICHNUNG];
			} else if ("Lieferant".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOTSPOTENTIAL_LIEFERANT];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOTSPOTENTIAL_MENGE];
			} else if ("Projekt".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOTSPOTENTIAL_PROJEKT];
			} else if ("Ekpreis".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOTSPOTENTIAL_EKPREIS];
			}
		}

		return value;
	}

	/**
	 * Ein bestehendes Angebot drucken.
	 * 
	 * @param iIdAngebotI
	 *            PK des Angebots
	 * @param iAnzahlKopienI
	 *            wieviele Kopien zusaetzlich zum Original?
	 * @param bMitLogo
	 *            Boolean
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return JasperPrint[] der Druck und seine Kopien
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public JasperPrintLP[] printAngebot(Integer iIdAngebotI,
			Integer iAnzahlKopienI, Boolean bMitLogo, String sReportname,
			TheClientDto theClientDto) throws EJBExceptionLP {
		return printAngebot(iIdAngebotI, iAnzahlKopienI, bMitLogo, sReportname,
				null, theClientDto);
	}

	public JasperPrintLP[] printAngebot(Integer iIdAngebotI,
			Integer iAnzahlKopienI, Boolean bMitLogo, String sReportname,
			String sDrucktype, TheClientDto theClientDto) throws EJBExceptionLP {
		Session session = null;
		JasperPrintLP[] aJasperPrint = null;
		index = -1;
		cAktuellerReport = AngebotReportFac.REPORT_ANGEBOT;
		int iArtikelpositionsnummer = 1;
		Boolean bbSeitenumbruch = new Boolean(false); // dieser Wert wechselt
		// mit jedem
		// Seitenumbruch
		// zwischen true und
		// false

		try {
			AngebotDto angebotDto = getAngebotFac().angebotFindByPrimaryKey(
					iIdAngebotI, theClientDto);

			// UW 23.03.06 Tiefe der Stuecklistenaufloeseung bestimmen
			ParametermandantDto parametermandantDto = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ANGEBOT,
							ParameterFac.PARAMETER_ANGEBOT_AG_STKL_AUFLOESUNG_TIEFE);

			int iStuecklisteaufloesungTiefe = ((Integer) parametermandantDto
					.getCWertAsObject()).intValue();

			// alle relevanten Positionen zum Andrucken holen
			session = FLRSessionFactory.getFactory().openSession();

			FLRAngebotpositionReport[] aFLRAngebotposition = holeAngebotpositionen(
					session, iIdAngebotI, false, // auch NULL Mengen
					false, // auch NULL Mengen
					false); // mit alternativen Positionen

			// es gilt das Locale des Kunden
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					angebotDto.getKundeIIdAngebotsadresse(), theClientDto);

			Locale locDruck = Helper.string2Locale(kundeDto.getPartnerDto()
					.getLocaleCNrKommunikation());

			AnsprechpartnerDto ansprechpartnerDto = null;

			if (angebotDto.getAnsprechpartnerIIdKunde() != null) {
				ansprechpartnerDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(
								angebotDto.getAnsprechpartnerIIdKunde(),
								theClientDto);
			}

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);

			// fuer das Andrucken der Mwstsatze wird eine Map vorbereitet, die
			// einen
			// Eintrag fuer jeden Mwstsatz des Mandanten enthaelt
			final Set<?> mwstSatzKeys = getMandantFac()
					.mwstsatzIIdFindAllByMandant(angebotDto.getMandantCNr(),
							theClientDto);
			LinkedHashMap<Integer, MwstsatzReportDto> mwstMap = new LinkedHashMap<Integer, MwstsatzReportDto>();
			for (Iterator<?> iter = mwstSatzKeys.iterator(); iter.hasNext();) {
				Integer item = (Integer) iter.next();
				MwstsatzReportDto mwstsatzReportDto = new MwstsatzReportDto();
				mwstMap.put(item, mwstsatzReportDto);
			}

			ArrayList<Object> dataList = new ArrayList<Object>(); // in dieser
																	// Liste
			// werden die
			// Daten
			// fuer den Druck gesammelt
			String positionsartCNr = null;
			String typCNr = null;
			
			boolean pauschalkorrekturHinzugefuegt = false;

			for (int i = 0; i < aFLRAngebotposition.length; i++) {
				positionsartCNr = aFLRAngebotposition[i]
						.getAngebotpositionart_c_nr();

				typCNr = aFLRAngebotposition[i].getTyp_c_nr();
				if (typCNr == null) {

					if (positionsartCNr
							.equals(AngebotServiceFac.ANGEBOTPOSITIONART_IDENT)
							|| positionsartCNr
									.equals(AngebotServiceFac.ANGEBOTPOSITIONART_HANDEINGABE)
							|| positionsartCNr
									.equals(AngebotServiceFac.ANGEBOTPOSITIONART_AGSTUECKLISTE)) {

						// PJ18020 ohne Kalkulatorische Artikel
						if (aFLRAngebotposition[i].getArtikel_i_id() != null) {
							ArtikelDto oArtikelDto = getArtikelFac()
									.artikelFindByPrimaryKey(
											aFLRAngebotposition[i]
													.getArtikel_i_id(),
											theClientDto);
							if (Helper.short2boolean(oArtikelDto
									.getBKalkulatorisch())) {
								continue;
							}

						}
						dataList.add(befuelleZeileAGMitPreisbehafteterPosition(
								aFLRAngebotposition[i], kundeDto,
								iArtikelpositionsnummer, mwstMap,
								bbSeitenumbruch, locDruck, theClientDto));

						iArtikelpositionsnummer++;

						// unterstkl: 6 Wenn es zu einem Artikel eine
						// Stueckliste
						// gibt...
						if (aFLRAngebotposition[i]
								.getAngebotpositionart_c_nr()
								.equals(AngebotServiceFac.ANGEBOTPOSITIONART_IDENT)) {
							StuecklisteInfoDto stuecklisteInfoDto = getStuecklisteFac()
									.getStrukturdatenEinesArtikels(
											aFLRAngebotposition[i]
													.getArtikel_i_id(),
											true, null, // in
											// die
											// Rekursion
											// mit
											// einer
											// leeren
											// Listen
											// einsteigen
											0, // in die Rekursion mit Ebene 0
												// einsteigen
											iStuecklisteaufloesungTiefe, false, // Menge
											// pro
											// Einheit
											// der
											// uebergeorndeten
											// Position
											new BigDecimal(1), // 1 Einheit der
											// Stueckliste
											true, // Fremdfertigung aufloesen
											theClientDto);

							if (stuecklisteInfoDto.getIiAnzahlPositionen()
									.intValue() > 0) {
								ArrayList<?> alStuecklisteAufgeloest = stuecklisteInfoDto
										.getAlStuecklisteAufgeloest();

								Iterator<?> it = alStuecklisteAufgeloest
										.iterator();

								while (it.hasNext()) {
									StuecklisteMitStrukturDto stuecklisteMitStrukturDto = (StuecklisteMitStrukturDto) it
											.next();

									dataList.add(befuelleZeileAGMitStuecklistenposition(
											stuecklisteMitStrukturDto,
											"    ",
											bbSeitenumbruch,
											locDruck,
											angebotDto,
											aFLRAngebotposition[i]
													.getMwstsatz_i_id(),
											kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
											theClientDto));
								}
							}
						} else if (aFLRAngebotposition[i]
								.getAngebotpositionart_c_nr()
								.equals(AngebotServiceFac.ANGEBOTPOSITIONART_AGSTUECKLISTE)) {
							// UW 23.03.06 Wenn die gewuenschte
							// Stuecklistentiefe ==
							// 0, dann kein Zugriff noetig
							if (iStuecklisteaufloesungTiefe != 0) {
								AgstklpositionDto[] aAgstklpositionDto = getAngebotstklpositionFac()
										.agstklpositionFindByAgstklIIdBDruckenOhneExc(
												aFLRAngebotposition[i]
														.getAgstkl_i_id(),
												new Short((short) 1), // alle
												// Positionen
												// , die
												// mitgedruckt
												// werden
												// sollen
												theClientDto);

								for (int k = 0; k < aAgstklpositionDto.length; k++) {
									ArtikelDto artikelDtoAgstklposition = getArtikelFac()
											.artikelFindByPrimaryKey(
													aAgstklpositionDto[k]
															.getArtikelIId(),
													theClientDto);

									dataList.add(befuelleZeileAGMitAGSTKLPosition(
											aAgstklpositionDto[k],
											artikelDtoAgstklposition,
											bbSeitenumbruch, locDruck,
											angebotDto, theClientDto));

									// Wenn es zu einem Artikel eine Stueckliste
									// gibt...
									if (!artikelDtoAgstklposition
											.getArtikelartCNr()
											.equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
										StuecklisteInfoDto stuecklisteInfoDto = getStuecklisteFac()
												.getStrukturdatenEinesArtikels(
														artikelDtoAgstklposition
																.getIId(),
														true,
														null, // in die
														// Rekursion
														// mit einer leeren
														// Listen einsteigen
														0, // WH 23.03.06 AGSTKL
															// werden eine Ebene
															// tiefer aufgeloest
														iStuecklisteaufloesungTiefe,
														false, // Menge pro
														// Einheit
														// der
														// uebergeorndeten
														// Position
														new BigDecimal(1), // 1
														// Einheit
														// der
														// Stueckliste
														true, // Fremdfertigung
														// aufloesen
														theClientDto);

										if (stuecklisteInfoDto
												.getIiAnzahlPositionen()
												.intValue() > 0) {
											ArrayList<?> alStuecklisteAufgeloest = stuecklisteInfoDto
													.getAlStuecklisteAufgeloest();

											Iterator<?> it = alStuecklisteAufgeloest
													.iterator();

											while (it.hasNext()) {
												StuecklisteMitStrukturDto stuecklisteMitStrukturDto = (StuecklisteMitStrukturDto) it
														.next();

												dataList.add(befuelleZeileAGMitStuecklistenposition(
														stuecklisteMitStrukturDto,
														"    ",
														bbSeitenumbruch,
														locDruck,
														angebotDto,
														aFLRAngebotposition[i]
																.getMwstsatz_i_id(),
														kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
														theClientDto));
											}
										}
									}
								}
							}
						}
					} else if (positionsartCNr
							.equals(AngebotServiceFac.ANGEBOTPOSITIONART_BETRIFFT)) {
						dataList.add(befuelleZeileAGMitBetrifft(
								aFLRAngebotposition[i], false, // die Position
								// wird
								// im Report Detail
								// angedruckt
								bbSeitenumbruch, theClientDto));
					} else if (positionsartCNr
							.equals(AngebotServiceFac.ANGEBOTPOSITIONART_TEXTEINGABE)) {
						dataList.add(befuelleZeileAGMitTexteingabe(
								aFLRAngebotposition[i], false, // die Position
								// wird
								// im Report Detail
								// angedruckt
								bbSeitenumbruch, theClientDto));
					} else if (positionsartCNr
							.equals(AngebotServiceFac.ANGEBOTPOSITIONART_TEXTBAUSTEIN)) {
						dataList.add(befuelleZeileAGMitTextbaustein(
								aFLRAngebotposition[i], false, // die Position
								// wird
								// im Report Detail
								// angedruckt
								bbSeitenumbruch, theClientDto));
					} else if (positionsartCNr
							.equals(AngebotServiceFac.ANGEBOTPOSITIONART_LEERZEILE)) {
						dataList.add(befuelleZeileAGMitLeerzeile(false, // die
								// Position
								// wird im
								// Report
								// Detail
								// angedruckt
								bbSeitenumbruch));
					} else if (positionsartCNr
							.equals(AngebotServiceFac.ANGEBOTPOSITIONART_SEITENUMBRUCH)) {
						bbSeitenumbruch = new Boolean(
								!bbSeitenumbruch.booleanValue()); // toggle
					} else if (positionsartCNr
							.equals(AngebotServiceFac.ANGEBOTPOSITIONART_INTELLIGENTE_ZWISCHENSUMME)) {
						dataList.add(befuelleZeileAGMitIntelligenterZwischensumme(
								aFLRAngebotposition[i], kundeDto, mwstMap,
								bbSeitenumbruch, locDruck, theClientDto));
						updateZwischensummenData(
								dataList,
								aFLRAngebotposition[i].getZwsvonposition_i_id(),
								aFLRAngebotposition[i].getC_bez());
					}

					// endsumme: 3 Die Positionsart Endsumme schliesst das
					// Report
					// Detail ab.
					// Alle weiteren Positionen werden nach der Endsumme im
					// naechsten Band
					// angedruckt. Alle weiteren Positionen werden daher mit der
					// Positionsart Endsumme
					// gekennzeichnet, die spezielle Behandlung erfolgt dann
					// innerhalb des
					// Drucks aufgrund der Positionsart
					else if (positionsartCNr
							.equals(AngebotServiceFac.ANGEBOTPOSITIONART_ENDSUMME)) {
						// SP1581 -> Pauschalkorrektur hinzufuegen
						if (angebotDto.getNKorrekturbetrag() != null
								&& angebotDto.getNKorrekturbetrag().doubleValue() != 0) {
							dataList.add(befuelleZeileAGMitPauschalkorrektur(
									angebotDto.getNKorrekturbetrag(), bbSeitenumbruch,
									kundeDto, mwstMap, locDruck, theClientDto));
							pauschalkorrekturHinzugefuegt = true;
						}
						dataList.add(befuelleZeileAGMitPositionsartEndsumme(
								aFLRAngebotposition[i], bbSeitenumbruch,
								theClientDto));
					}
				} else {

					if (positionsartCNr
							.equals(AngebotServiceFac.ANGEBOTPOSITIONART_POSITION)
							&& !aFLRAngebotposition[i].getC_zbez().equals(
									LocaleFac.POSITIONBEZ_ENDE)) {

						Object[] dataRow = befuelleZeileAGMitPosition(
								aFLRAngebotposition[i],
								iArtikelpositionsnummer, mwstMap, false,
								bbSeitenumbruch, theClientDto);

						if (aFLRAngebotposition[i].getTyp_c_nr().equals(
								LocaleFac.POSITIONTYP_MITPREISE)) {
							dataList.add(dataRow);
							List<?> l = null;
							String sArtikelInfo = new String();
							SessionFactory factory = FLRSessionFactory
									.getFactory();
							session = factory.openSession();
							Criteria crit = session
									.createCriteria(FLRAngebotposition.class);
							crit.add(Restrictions.eq("position_i_id",
									aFLRAngebotposition[i].getI_id()));
							crit.addOrder(Order
									.asc(AngebotpositionFac.FLR_ANGEBOTPOSITION_I_SORT));
							l = crit.list();
							Iterator<?> iter = l.iterator();

							while (iter.hasNext()) {
								dataRow = new Object[AngebotReportFac.REPORT_ANGEBOT_ANZAHL_SPALTEN];
								FLRAngebotposition pos = (FLRAngebotposition) iter
										.next();
								if (pos.getPositionart_c_nr().equals(
										LocaleFac.POSITIONSART_IDENT)
										|| pos.getPositionart_c_nr()
												.equals(LocaleFac.POSITIONSART_HANDEINGABE)
										|| pos.getPositionart_c_nr()
												.equals(LocaleFac.POSITIONSART_TEXTEINGABE)) {
									if (pos.getPositionart_c_nr().equals(
											LocaleFac.POSITIONSART_IDENT))
										sArtikelInfo = pos.getFlrartikel()
												.getC_nr();
									else if (pos.getPositionart_c_nr().equals(
											LocaleFac.POSITIONSART_TEXTEINGABE))
										sArtikelInfo = pos.getX_textinhalt();
									else if (pos.getPositionart_c_nr().equals(
											LocaleFac.POSITIONSART_HANDEINGABE))
										sArtikelInfo = pos.getC_bez();
									dataRow[AngebotReportFac.REPORT_ANGEBOT_IDENTNUMMER] = sArtikelInfo;
									dataRow[AngebotReportFac.REPORT_ANGEBOT_IDENT] = sArtikelInfo
											.toString();

									// weitere Daten
									dataRow[AngebotReportFac.REPORT_ANGEBOT_MENGE] = pos
											.getN_menge();
									if (pos.getEinheit_c_nr() != null)
										dataRow[AngebotReportFac.REPORT_ANGEBOT_EINHEIT] = pos
												.getEinheit_c_nr().trim();
									dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITIONSART] = LocaleFac.POSITIONSART_IDENT;
									dataRow[AngebotReportFac.REPORT_ANGEBOT_EINZELPREIS] = pos
											.getN_nettogesamtpreis();
									if (pos.getN_menge() != null
											&& pos.getN_nettogesamtpreis() != null)
										dataRow[AngebotReportFac.REPORT_ANGEBOT_GESAMTPREIS] = pos
												.getN_menge()
												.multiply(
														pos.getN_nettogesamtpreis());
									dataRow[AngebotReportFac.REPORT_ANGEBOT_SEITENUMBRUCH] = bbSeitenumbruch;
									dataRow[AngebotReportFac.REPORT_ANGEBOT_TYP_CNR] = pos
											.getTyp_c_nr();
									dataList.add(dataRow);
								} else if (pos.getPositionart_c_nr().equals(
										LocaleFac.POSITIONSART_LEERZEILE)) {
									dataList.add(befuelleZeileAGMitLeerzeile(
											false, // die
											// Position
											// wird im
											// Report
											// Detail
											// angedruckt
											bbSeitenumbruch));
								}
							}
						} else {

							List<?> l = null;
							StringBuffer sbArtikelInfo = new StringBuffer();
							SessionFactory factory = FLRSessionFactory
									.getFactory();
							session = factory.openSession();
							Criteria crit = session
									.createCriteria(FLRAngebotposition.class);
							crit.add(Restrictions.eq("position_i_id",
									aFLRAngebotposition[i].getI_id()));
							crit.addOrder(Order
									.asc(AngebotpositionFac.FLR_ANGEBOTPOSITION_I_SORT));
							l = crit.list();
							Iterator<?> iter = l.iterator();
							while (iter.hasNext()) {
								FLRAngebotposition pos = (FLRAngebotposition) iter
										.next();
								if (pos.getPositionart_c_nr().equals(
										LocaleFac.POSITIONSART_IDENT)) {
									sbArtikelInfo.append(pos.getFlrartikel()
											.getC_nr());
									sbArtikelInfo.append("  ");
									if (pos.getFlrartikel()
											.getFlrartikelgruppe() != null)
										sbArtikelInfo.append(pos
												.getFlrartikel()
												.getFlrartikelgruppe()
												.getC_nr());
									if (iter.hasNext()) {
										sbArtikelInfo.append("\n");
									}
								} else if (pos.getPositionart_c_nr().equals(
										LocaleFac.POSITIONSART_TEXTEINGABE)) {
									sbArtikelInfo.append(pos.getX_textinhalt());
									if (iter.hasNext()) {
										sbArtikelInfo.append("\n");
									}
								}
								if (pos.getPositionart_c_nr().equals(
										LocaleFac.POSITIONSART_HANDEINGABE)) {
									if (pos.getC_bez() != null) {
										sbArtikelInfo.append(pos.getC_bez());
										if (iter.hasNext()) {
											sbArtikelInfo.append("\n");
										}
									}
								}
							}
							// Druckdaten zusammenstellen
							dataRow[AngebotReportFac.REPORT_ANGEBOT_IDENTNUMMER] = sbArtikelInfo
									.toString();
							dataRow[AngebotReportFac.REPORT_ANGEBOT_IDENT] = sbArtikelInfo
									.toString();

							dataList.add(dataRow);

						}

					}
				}

			}
			// SP1581 -> Pauschalkorrektur hinzufuegen
			if (!pauschalkorrekturHinzugefuegt && angebotDto.getNKorrekturbetrag() != null
					&& angebotDto.getNKorrekturbetrag().doubleValue() != 0) {
				dataList.add(befuelleZeileAGMitPauschalkorrektur(
						angebotDto.getNKorrekturbetrag(), bbSeitenumbruch,
						kundeDto, mwstMap, locDruck, theClientDto));
			}

			// Erstellung des Report
			HashMap<String, Object> parameter = new HashMap<String, Object>();
			parameter.put("P_ZUSAMMENFASSUNG",
					Helper.short2Boolean(angebotDto.getBMitzusammenfassung()));
			// PJ17046 Wenn 'mit Zusammenfassung' dann Positionsdaten
			// duplizieren
			duplizierePositionenWegenZusammenfassung(angebotDto, dataList,
					AngebotReportFac.REPORT_ANGEBOT_POSITIONSART);

			// jetzt die Map mit dataRows in ein Object[][] fuer den Druck
			// umwandeln
			data = new Object[dataList.size()][AngebotReportFac.REPORT_ANGEBOT_ANZAHL_SPALTEN];
			data = (Object[][]) dataList.toArray(data);

			// Kopftext
			String sKopftext = angebotDto.getXKopftextuebersteuert();

			if (sKopftext == null || sKopftext.length() == 0) {
				AngebottextDto angebottextDto = getAngebotServiceFac()
						.angebottextFindByMandantCNrLocaleCNrCNr(
								kundeDto.getPartnerDto()
										.getLocaleCNrKommunikation(),
								AngebotServiceFac.ANGEBOTTEXT_KOPFTEXT,
								theClientDto);

				sKopftext = angebottextDto.getXTextinhalt();
			}

			// Fusstext
			String sFusstext = angebotDto.getXFusstextuebersteuert();

			if (sFusstext == null || sFusstext.length() == 0) {
				AngebottextDto angebottextDto = getAngebotServiceFac()
						.angebottextFindByMandantCNrLocaleCNrCNr(
								kundeDto.getPartnerDto()
										.getLocaleCNrKommunikation(),
								AngebotServiceFac.ANGEBOTTEXT_FUSSTEXT,
								theClientDto);

				sFusstext = angebottextDto.getXTextinhalt();
			}

			// CK: PJ 13849
			parameter.put(
					"P_BEARBEITER",
					getPersonalFac().getPersonRpt(
							angebotDto.getPersonalIIdAnlegen(), theClientDto));

			parameter.put("P_BELEGWAEHRUNG", angebotDto.getWaehrungCNr());
			parameter.put("Mandantenadresse",
					Helper.formatMandantAdresse(mandantDto));
			parameter.put(
					"Adressefuerausdruck",
					formatAdresseFuerAusdruck(kundeDto.getPartnerDto(),
							ansprechpartnerDto, mandantDto, locDruck,
							LocaleFac.BELEGART_ANGEBOT));

			if (ansprechpartnerDto != null) {
				parameter.put(
						"P_ANSPRECHPARTNER_ADRESSBLOCK",
						getPartnerFac()
								.formatFixAnredeTitelName2Name1FuerAdresskopf(
										ansprechpartnerDto.getPartnerDto(),
										locDruck, null));
			}

			parameter.put("Kundeuid", kundeDto.getPartnerDto().getCUid());
			parameter.put("KundeEori", kundeDto.getPartnerDto().getCEori());
			parameter.put("P_LIEFERANTENNR", kundeDto.getCLieferantennr());
			parameter.put("P_KUNDE_KUNDENNUMMER", kundeDto.getIKundennummer());

			ParametermandantDto parameterAbweichung = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_RECHNUNG,
							ParameterFac.PARAMETER_MAXIMALE_ABWEICHUNG_SUMMENAUSDRUCK);
			final Double dAbweichung = (Double) parameterAbweichung
					.getCWertAsObject();
			parameter.put("P_MAXIMALE_ABWEICHUNG", dAbweichung);

			if (kundeDto.getIidDebitorenkonto() != null) {
				parameter.put("P_DEBITORENKONTO", getFinanzFac()
						.kontoFindByPrimaryKey(kundeDto.getIidDebitorenkonto())
						.getCNr());
			}
			ParametermandantDto parameterWerbeabgabe = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_WERBEABGABE_PROZENT);
			parameter.put("P_WERBEABGABE_PROZENT",
					(Integer) parameterWerbeabgabe.getCWertAsObject());
			ParametermandantDto parameterWerbeabgabeArtikel = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_WERBEABGABE_ARTIKEL);
			parameter.put("P_WERBEABGABE_ARTIKEL",
					parameterWerbeabgabeArtikel.getCWert());
			parameter.put("P_RECHNUNGSDRUCKMITRABATT", Helper
					.short2Boolean(kundeDto.getBRechnungsdruckmitrabatt()));
			PersonalDto vertreterDto = null;
			String cVertreterAnredeShort = null;

			if (angebotDto.getPersonalIIdVertreter() != null) {
				vertreterDto = getPersonalFac().personalFindByPrimaryKey(
						angebotDto.getPersonalIIdVertreter(), theClientDto);

				if (vertreterDto != null) {
					// Vertreter Kontaktdaten
					String sVertreterEmail = vertreterDto.getCEmail();

					String sVertreterFaxDirekt = vertreterDto.getCDirektfax();

					String sVertreterFax = vertreterDto.getCFax();

					String sVertreterTelefon = vertreterDto.getCTelefon();

					parameter.put(LPReport.P_VERTRETEREMAIL,
							sVertreterEmail != null ? sVertreterEmail : "");
					if (sVertreterFaxDirekt != null
							&& sVertreterFaxDirekt != "") {
						parameter.put(LPReport.P_VERTRETERFAX,
								sVertreterFaxDirekt);
					} else {
						parameter.put(LPReport.P_VERTRETERFAX,
								sVertreterFax != null ? sVertreterFax : "");
					}
					parameter.put(LPReport.P_VERTRETEERTELEFON,
							sVertreterTelefon != null ? sVertreterTelefon : "");

				}

				cVertreterAnredeShort = getPersonalFac()
						.personalFindByPrimaryKey(
								angebotDto.getPersonalIIdVertreter(),
								theClientDto).getPartnerDto()
						.formatFixName2Name1();
			}

			parameter.put("Vertreteranrede", cVertreterAnredeShort);
			parameter.put(
					"Lieferart",
					getLocaleFac().lieferartFindByIIdLocaleOhneExc(
							angebotDto.getLieferartIId(), locDruck,
							theClientDto));
			parameter.put("P_LIEFERART_ORT", angebotDto.getCLieferartort());
			parameter
					.put("Anfragedatum", Helper.formatDatum(
							angebotDto.getTAnfragedatum(), locDruck));

			parameter.put("P_ALLGEMEINERRABATT",
					angebotDto.getFAllgemeinerRabattsatz());
			parameter.put("P_ALLGEMEINERRABATT_STRING", Helper.formatZahl(
					angebotDto.getFAllgemeinerRabattsatz(), locDruck));
			parameter.put("P_PROJEKT_RABATT",
					angebotDto.getFProjektierungsrabattsatz());
			parameter.put("P_PROJEKT_RABATT_STRING", Helper.formatZahl(
					angebotDto.getFProjektierungsrabattsatz(), locDruck));

			parameter.put("Belegkennung",
					getAngebotFac()
							.getAngebotkennung(iIdAngebotI, theClientDto));

			parameter.put("Projekt", angebotDto.getCBez());
			
			if (angebotDto.getProjektIId() != null) {
				ProjektDto pjDto = getProjektFac().projektFindByPrimaryKey(
						angebotDto.getProjektIId());
				parameter.put("P_PROJEKTNUMMER", pjDto.getCNr());
			}
			
			parameter.put("Kundenanfrage", angebotDto.getCKundenanfrage());

			String cBriefanrede = "";

			if (ansprechpartnerDto != null) {
				cBriefanrede = getPartnerServicesFac().getBriefanredeFuerBeleg(
						angebotDto.getAnsprechpartnerIIdKunde(),
						kundeDto.getPartnerIId(), locDruck, theClientDto);
			} else {
				// neutrale Anrede

				cBriefanrede = getBriefanredeNeutralOderPrivatperson(
						kundeDto.getPartnerIId(), locDruck, theClientDto);
			}

			parameter.put("Briefanrede", cBriefanrede);

			PersonalDto personalBenutzer = getPersonalFac()
					.personalFindByPrimaryKey(theClientDto.getIDPersonal(),
							theClientDto);
			PersonalDto oPersonalAnleger = getPersonalFac()
					.personalFindByPrimaryKey(
							angebotDto.getPersonalIIdAnlegen(), theClientDto);
			parameter.put("Unserzeichen", Helper.getKurzzeichenkombi(
					personalBenutzer.getCKurzzeichen(),
					oPersonalAnleger.getCKurzzeichen()));
			if (angebotDto.getKostenstelleIId() != null) {
				KostenstelleDto kostenstelleDto = getSystemFac()
						.kostenstelleFindByPrimaryKey(
								angebotDto.getKostenstelleIId());
				parameter
						.put(LPReport.P_KOSTENSTELLE, kostenstelleDto.getCNr());
			}

			parameter.put("Belegdatum",
					Helper.formatDatum(angebotDto.getTBelegdatum(), locDruck));
			parameter.put("Kopftext",
					Helper.formatStyledTextForJasper(sKopftext));

			// die Kommunikationsinformation des Kunden
			Integer partnerIIdAnsprechpartner = null;
			if (ansprechpartnerDto != null) {
				partnerIIdAnsprechpartner = ansprechpartnerDto
						.getPartnerIIdAnsprechpartner();
			}

			String sEmail = getPartnerFac()
					.partnerkommFindRespectPartnerAsStringOhneExec(
							partnerIIdAnsprechpartner,
							kundeDto.getPartnerDto(),
							PartnerFac.KOMMUNIKATIONSART_EMAIL,
							theClientDto.getMandant(), theClientDto);
			String sFax = getPartnerFac()
					.partnerkommFindRespectPartnerAsStringOhneExec(
							partnerIIdAnsprechpartner,
							kundeDto.getPartnerDto(),
							PartnerFac.KOMMUNIKATIONSART_FAX,
							theClientDto.getMandant(), theClientDto);
			String sTelefon = getPartnerFac()
					.partnerkommFindRespectPartnerAsStringOhneExec(
							partnerIIdAnsprechpartner,
							kundeDto.getPartnerDto(),
							PartnerFac.KOMMUNIKATIONSART_TELEFON,
							theClientDto.getMandant(), theClientDto);

			parameter.put(LPReport.P_ANSPRECHPARTNEREMAIL,
					sEmail != null ? sEmail : "");
			parameter.put(LPReport.P_ANSPRECHPARTNERFAX, sFax != null ? sFax
					: "");
			parameter.put(LPReport.P_ANSPRECHPARTNERTELEFON,
					sTelefon != null ? sTelefon : "");
			/*
			 * // das Andrucken der gesammelten Mwstinformationen steuern
			 * StringBuffer sbMwstsatz = new StringBuffer(); StringBuffer
			 * sbSummePositionsbetrag = new StringBuffer(); StringBuffer
			 * sbWaehrung = new StringBuffer(); StringBuffer sbSummeMwstbetrag =
			 * new StringBuffer();
			 * 
			 * // UW 03.03.06: Die folgenden Informationen erscheinen am Druck,
			 * mit // diesen // Werten wird intern nicht mehr weitergerechnet ->
			 * daher auf 2 // Stellen runden BigDecimal
			 * nAngebotssendbetragMitMwst = Helper.rundeKaufmaennisch(
			 * angebotDto.getNGesamtwertinbelegwaehrung(), 2);
			 * 
			 * boolean bHatMwstWerte = false;
			 * 
			 * for (Iterator<Object> iter = mwstMap.keySet().iterator(); iter
			 * .hasNext();) { Integer key = (Integer) iter.next(); // IId des
			 * Mwstsatzes MwstsatzReportDto mwstsatzReportDto =
			 * (MwstsatzReportDto) mwstMap .get(key); // Summen der Mwstbetraege
			 * if (mwstsatzReportDto != null &&
			 * mwstsatzReportDto.getNSummeMwstbetrag() .doubleValue() > 0.0) {
			 * MwstsatzDto mwstsatzDto = getMandantFac()
			 * .mwstsatzFindByPrimaryKey(key, theClientDto); // MR: FIX, statt
			 * festverdrahtetem UST verwende // Localeabh&auml;ngigen Wert lp.ust
			 * sbMwstsatz.append(getTextRespectUISpr("lp.ust",
			 * theClientDto.getMandant(), locDruck)); sbMwstsatz.append(": ");
			 * sbMwstsatz.append(Helper.formatZahl(mwstsatzDto .getFMwstsatz(),
			 * 2, locDruck)); sbMwstsatz.append(" % "); sbMwstsatz.append(
			 * getTextRespectUISpr("lp.ustvon", theClientDto .getMandant(),
			 * locDruck)).append(" "); // Fix Ende
			 * sbSummePositionsbetrag.append(Helper.formatZahl(
			 * mwstsatzReportDto.getNSummePositionsbetrag(), 2, locDruck));
			 * sbWaehrung.append(angebotDto.getWaehrungCNr());
			 * sbSummeMwstbetrag.append(Helper.formatZahl(
			 * mwstsatzReportDto.getNSummeMwstbetrag(), 2, locDruck));
			 * 
			 * sbMwstsatz.append("\n"); sbSummePositionsbetrag.append("\n");
			 * sbWaehrung.append("\n"); sbSummeMwstbetrag.append("\n");
			 * 
			 * nAngebotssendbetragMitMwst = nAngebotssendbetragMitMwst
			 * .add(Helper.rundeKaufmaennisch(mwstsatzReportDto
			 * .getNSummeMwstbetrag(), 2));
			 * 
			 * bHatMwstWerte = true; } }
			 * 
			 * if (bHatMwstWerte) { // die letzten \n wieder loeschen
			 * sbMwstsatz.delete(sbMwstsatz.length() - 1, sbMwstsatz.length());
			 * sbSummePositionsbetrag.delete( sbSummePositionsbetrag.length() -
			 * 1, sbSummePositionsbetrag.length());
			 * sbWaehrung.delete(sbWaehrung.length() - 1, sbWaehrung.length());
			 * sbSummeMwstbetrag.delete(sbSummeMwstbetrag.length() - 1,
			 * sbSummeMwstbetrag.length()); }
			 * 
			 * parameter.put("P_MWST_TABELLE_LINKS", sbMwstsatz.toString());
			 * parameter.put("P_MWST_TABELLE_SUMME_POSITIONEN",
			 * sbSummePositionsbetrag.toString());
			 * parameter.put("P_MWST_TABELLE_WAEHRUNG", sbWaehrung.toString());
			 * parameter .put("P_MWST_TABELLE_RECHTS",
			 * sbSummeMwstbetrag.toString());
			 * parameter.put("P_ANGEBOTSENDBETRAGMITMWST",
			 * nAngebotssendbetragMitMwst);
			 */
			Object sMwstsatz[] = null;
			sMwstsatz = getBelegVerkaufFac().getMwstTabelle(mwstMap,
					angebotDto, locDruck, theClientDto);
			parameter.put("P_MWST_TABELLE_LINKS",
					sMwstsatz[LPReport.MWST_TABELLE_LINKS]);
			parameter.put("P_MWST_TABELLE_SUMME_POSITIONEN",
					sMwstsatz[LPReport.MWST_TABELLE_SUMME_POSITIONEN]);
			parameter.put("P_MWST_TABELLE_WAEHRUNG",
					sMwstsatz[LPReport.MWST_MWST_TABELLE_WAEHRUNG]);
			parameter.put("P_MWST_TABELLE_RECHTS",
					sMwstsatz[LPReport.MWST_TABELLE_RECHTS]);
			parameter.put("P_ANGEBOTSENDBETRAGMITMWST",
					(BigDecimal) sMwstsatz[LPReport.MWST_ENDBETRAGMITMWST]);

			// Lieferzeit in hinterlegter Einheit
			Integer iiLieferzeit = getAngebotServiceFac()
					.getLieferzeitInAngeboteinheit(angebotDto.getIId(),
							angebotDto.getAngeboteinheitCNr(), theClientDto);

			parameter.put(
					"P_ANGEBOTEINHEIT",
					getSystemFac().formatEinheit(
							angebotDto.getAngeboteinheitCNr(), locDruck,
							theClientDto));
			parameter.put("P_LIEFERZEIT", iiLieferzeit);

			// die Lieferart
			parameter.put(
					"P_LIEFERART",
					getLocaleFac().lieferartFindByIIdLocaleOhneExc(
							angebotDto.getLieferartIId(), locDruck,
							theClientDto));

			// das Zahlungsziel
			parameter.put(
					"P_ZAHLUNGSZIEL",
					getMandantFac().zahlungszielFindByIIdLocaleOhneExc(
							angebotDto.getZahlungszielIId(), locDruck,
							theClientDto));

			// Spediteur
			SpediteurDto spediteurDto = getMandantFac()
					.spediteurFindByPrimaryKey(angebotDto.getSpediteurIId());
			parameter.put("P_SPEDITEUR", spediteurDto.getCNamedesspediteurs());

			if (spediteurDto.getPartnerIId() != null) {
				PartnerDto partnerDto = getPartnerFac()
						.partnerFindByPrimaryKey(spediteurDto.getPartnerIId(),
								theClientDto);

				AnsprechpartnerDto ansprechpartnerDtoSpediteur = null;

				if (spediteurDto.getAnsprechpartnerIId() != null) {
					ansprechpartnerDtoSpediteur = getAnsprechpartnerFac()
							.ansprechpartnerFindByPrimaryKey(
									spediteurDto.getAnsprechpartnerIId(),
									theClientDto);
				}

				parameter.put(
						"P_SPEDITEUR_ADRESSBLOCK",
						formatAdresseFuerAusdruck(partnerDto,
								ansprechpartnerDtoSpediteur, mandantDto,
								locDruck));
			}

			// Konsistenzpruefung: Die Summe der einzelnen Positionen muss den
			// Angebotswert ergeben
			parameter.put("P_ANGEBOTSWERT",
					angebotDto.getNGesamtwertinbelegwaehrung());

			parameter.put("P_ANGEBOTSGUELTIGKEIT",
					angebotDto.getTAngebotsgueltigkeitbis());

			// die folgenden Felder werden im Report mit jeweils einer
			// trennenden Leerzeile
			// hintereinandergehaengt
			// MR 20071114: Die Werte muessen auch einzeln an Report uebergeben
			// werden.
			StringBuffer buff = new StringBuffer();

			// endsumme: 4 die Endsumme zum Andrucken vorbereiten
			String cTextNachEndsumme = "";

			for (int i = 0; i < data.length; i++) {
				Object[] dataRow = data[i];

				if (dataRow[AngebotReportFac.REPORT_ANGEBOT_TEXTNACHENDSUMME] != null) {
					if (cTextNachEndsumme != null
							&& cTextNachEndsumme.length() > 0) {
						cTextNachEndsumme += "\n";
					}

					cTextNachEndsumme += dataRow[AngebotReportFac.REPORT_ANGEBOT_TEXTNACHENDSUMME];
				}
			}

			cTextNachEndsumme = cTextNachEndsumme == "" ? null
					: cTextNachEndsumme;

			if (cTextNachEndsumme != null && cTextNachEndsumme.length() > 0) {
				buff.append(cTextNachEndsumme).append("\n\n");
				parameter.put("P_TEXTNACHENDSUMME",
						Helper.formatStyledTextForJasper(cTextNachEndsumme));
			}

			// Externer Kommentar
			if (angebotDto.getXExternerkommentar() != null
					&& angebotDto.getXExternerkommentar().length() > 0) {
				parameter.put(P_EXTERNERKOMMENTAR, Helper
						.formatStyledTextForJasper(angebotDto
								.getXExternerkommentar()));
				buff.append(angebotDto.getXExternerkommentar()).append("\n\n");
			}

			// Eigentumsvorbehalt
			MediastandardDto mediastandardDto = getMediaFac()
					.mediastandardFindByCNrDatenformatCNrMandantCNr(
							MediaFac.MEDIASTANDARD_EIGENTUMSVORBEHALT,
							MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML,
							theClientDto.getMandant(),
							kundeDto.getPartnerDto()
									.getLocaleCNrKommunikation(), theClientDto);

			if (mediastandardDto != null
					&& mediastandardDto.getOMediaImage() != null
					&& mediastandardDto.getOMediaImage().length > 0) {
				parameter.put(LPReport.P_EIGENTUMSVORBEHALT, Helper
						.formatStyledTextForJasper(mediastandardDto
								.getOMediaText()));
				buff.append(mediastandardDto.getOMediaText()).append("\n\n");
			}

			// Lieferbedingungen
			mediastandardDto = getMediaFac()
					.mediastandardFindByCNrDatenformatCNrMandantCNr(
							MediaFac.MEDIASTANDARD_LIEFERBEDINGUNGEN,
							MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML,
							theClientDto.getMandant(),
							kundeDto.getPartnerDto()
									.getLocaleCNrKommunikation(), theClientDto);

			if (mediastandardDto != null
					&& mediastandardDto.getOMediaImage() != null
					&& mediastandardDto.getOMediaImage().length > 0) {
				parameter.put(LPReport.P_LIEFERBEDINGUNGEN, Helper
						.formatStyledTextForJasper(mediastandardDto
								.getOMediaText()));
				buff.append(mediastandardDto.getOMediaText()).append("\n\n");
			}

			// Fusstext andrucken
			if (sFusstext != null) {
				parameter.put("P_FUSSTEXT",
						Helper.formatStyledTextForJasper(sFusstext));
				buff.append(sFusstext).append("\n\n");
			}

			StringBuffer buffVertreter = new StringBuffer();
			// Anrede des Mandanten
			String sMandantAnrede = mandantDto.getPartnerDto()
					.formatFixName1Name2();
			if (sMandantAnrede != null && sMandantAnrede.length() > 0) {
				parameter.put(P_MANDANT_ANREDE_UND_NAME, sMandantAnrede);
				buff.append(sMandantAnrede).append("\n\n");
				buffVertreter.append(sMandantAnrede).append("\n\n");
			}

			// Die Unterschrift fuer Belege inclusive Unterschriftstext und
			// -funktion
			// Beispiel:
			// "i.A. Ing. Werner Hehenwarter" - im Falle des Angebots der
			// Vertreter aus den Kopfdaten
			// "Einkaufsleiter"
			if (vertreterDto != null) {
				String sVertreterUFTitelName2Name1 = vertreterDto
						.formatFixUFTitelName2Name1();

				parameter.put(P_VERTRETER, vertreterDto.formatAnrede());

				buff.append(sVertreterUFTitelName2Name1);
				buffVertreter.append(sVertreterUFTitelName2Name1);
				parameter.put(P_VERTRETER_UNTERSCHRIFTSFUNKTION_UND_NAME,
						sVertreterUFTitelName2Name1);

				if (vertreterDto.getCUnterschriftstext() != null
						&& vertreterDto.getCUnterschriftstext().length() > 0) {
					String sUnterschriftstext = vertreterDto
							.getCUnterschriftstext();
					buff.append("\n").append(sUnterschriftstext);
					buffVertreter.append("\n").append(sUnterschriftstext);
					parameter.put(P_VERTRETER_UNTERSCHRIFTSTEXT,
							sUnterschriftstext);
				}
			}

			parameter.put("P_SUMMARY",
					Helper.formatStyledTextForJasper(buff.toString()));
			parameter.put(P_DRUCKTYPE, sDrucktype);

			// die Anzahl der Exemplare ist 1 + Anzahl der Kopien
			int iAnzahlExemplare = 1;

			if (iAnzahlKopienI != null && iAnzahlKopienI.intValue() > 0) {
				iAnzahlExemplare += iAnzahlKopienI.intValue();
			}

			aJasperPrint = new JasperPrintLP[iAnzahlExemplare];

			for (int iKopieNummer = 0; iKopieNummer < iAnzahlExemplare; iKopieNummer++) {
				// jede Kopie bekommt eine Kopienummer, das Original bekommt
				// keine
				if (iKopieNummer > 0) {
					parameter.put(LPReport.P_KOPIE_NUMMER, new Integer(
							iKopieNummer));
				}

				// Index zuruecksetzen !!!
				index = -1;
				String sReportToUse = AngebotReportFac.REPORT_ANGEBOT;
				if (sReportname != null) {
					sReportToUse = sReportname;
				}

				initJRDS(parameter, AngebotReportFac.REPORT_MODUL,
						sReportToUse, theClientDto.getMandant(), locDruck,
						theClientDto, bMitLogo.booleanValue(),
						angebotDto.getKostenstelleIId());

				aJasperPrint[iKopieNummer] = getReportPrint();
			}

			PrintInfoDto values = getJCRDocFac().getPathAndPartnerAndTable(
					angebotDto.getIId(), QueryParameters.UC_ID_ANGEBOT,
					theClientDto);
			aJasperPrint[0].setOInfoForArchive(values);
			aJasperPrint[0].putAdditionalInformation(
					JasperPrintLP.KEY_BELEGART, LocaleFac.BELEGART_ANGEBOT);
			aJasperPrint[0].putAdditionalInformation(
					JasperPrintLP.KEY_BELEGIID, angebotDto.getIId());

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} finally {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE, he);
			}
		}

		return aJasperPrint;
	}

	/**
	 * Liste aller Angebote drucken.
	 * 
	 * @param kritDtoI
	 *            die Filter- und Sortierkriterien
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return JasperPrint der Druck
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAngebotAlle(
			ReportAngebotJournalKriterienDto kritDtoI,
			String erledigungsgrundCNr, TheClientDto theClientDto)
			throws EJBExceptionLP {
		JasperPrintLP oPrint = null;
		cAktuellerReport = AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ALLE;

		try {
			// reportflr: 4 die Daten des Report ueber Hibernate holen
			ReportAngebotpositionJournalDto[] aReportDto = getListeAngebotpositionenJournal(
					kritDtoI, erledigungsgrundCNr,
					AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ALLE, theClientDto);

			int iAnzahlZeilen = aReportDto.length; // Anzahl der Positionen
			int iAnzahlSpalten = 37; // Anzahl der Spalten in der Gruppe

			data = new Object[iAnzahlZeilen][iAnzahlSpalten];

			// die Datenmatrix befuellen
			for (int i = 0; i < iAnzahlZeilen; i++) {
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANGEBOTCNR] = aReportDto[i]
						.getCNr();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDECNAME1] = aReportDto[i]
						.getKundeCName1();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KOSTENSTELLECNR] = aReportDto[i]
						.getKostenstelleCNr();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_VERTRETERCNAME1] = aReportDto[i]
						.getVertreterCName1();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_PROJEKTBEZ] = aReportDto[i]
						.getProejktBez();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_REALISIERUNGSTERMIN] = aReportDto[i]
						.getRealisierungstermin();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ARTIKELCNR] = aReportDto[i]
						.getArtikelCNr();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ARTIKELBEZ] = aReportDto[i]
						.getArtkelCBez();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ARTIKELMENGE] = aReportDto[i]
						.getNMenge();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ARTIKELEINHEIT] = aReportDto[i]
						.getEinheitCNr();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ARTIKELPREIS] = aReportDto[i]
						.getNPreis();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANGEBOTERLEDIGUNGSGRUND] = aReportDto[i]
						.getAngeboterledigungsgrundCNr();

				AngebotDto angebotDto = getAngebotFac()
						.angebotFindByPrimaryKey(aReportDto[i].getIId(),
								theClientDto);
				angebotDto.setIId(aReportDto[i].getIId());

				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_INTERNERKOMMENTAR] = angebotDto
						.getXInternerkommentar();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_EXTERNERKOMMENTAR] = angebotDto
						.getXExternerkommentar();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ERLEDIGUNGSGRUND_AB_NR] = aReportDto[i]
						.getErledigungsgrundABNr();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDECNAME2] = aReportDto[i]
						.getKundeCName2();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDECNAME3] = aReportDto[i]
						.getKundeCName3();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDESTRASSE] = aReportDto[i]
						.getKundeStrasse();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDEPLZ] = aReportDto[i]
						.getKundePlz();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDEORT] = aReportDto[i]
						.getKundeOrt();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDELKZ] = aReportDto[i]
						.getKundeLkz();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDETELEFON] = aReportDto[i]
						.getKundeTelefon();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDEFAX] = aReportDto[i]
						.getKundeFax();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDEEMAIL] = aReportDto[i]
						.getKundeEmail();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDEHOMEPAGE] = aReportDto[i]
						.getKundeHomepage();

				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_VORNAME] = aReportDto[i]
						.getAnsprechpartnerVorname();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_NACHNAME] = aReportDto[i]
						.getAnsprechpartnerNachname();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_TITEL] = aReportDto[i]
						.getAnsprechpartnerTitel();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_ANREDE] = aReportDto[i]
						.getAnsprechpartnerAnrede();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_TELEFON] = aReportDto[i]
						.getAnsprechpartnerTelefon();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_TELEFONDW] = aReportDto[i]
						.getAnsprechpartnerTelefonDw();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_FAX] = aReportDto[i]
						.getAnsprechpartnerFax();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_FAXDW] = aReportDto[i]
						.getAnsprechpartnerFaxDw();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_EMAIL] = aReportDto[i]
						.getAnsprechpartnerEmail();

			}

			// Erstellung des Report
			HashMap<String, Object> parameter = new HashMap<String, Object>();

			// die Parameter dem Report uebergeben
			parameter.put(LPReport.P_SORTIERUNG,
					buildSortierungAngebotJournal(kritDtoI, theClientDto));
			parameter.put(LPReport.P_FILTER,
					buildFilterAngebotJournal(kritDtoI, theClientDto));

			// die Parameter zur Bildung von Zwischensummen uebergeben
			if (kritDtoI.bSortiereNachKostenstelle) {
				parameter.put(LPReport.P_SORTIERENACHKOSTENSTELLE, new Boolean(
						true));
			} else {
				parameter.put(LPReport.P_SORTIERENACHKOSTENSTELLE, new Boolean(
						false));
			}

			if (kritDtoI.getBMitDetails()) {
				parameter.put(LPReport.P_MITDETAILS, new Boolean(true));
			} else {
				parameter.put(LPReport.P_MITDETAILS, new Boolean(false));
			}

			if (kritDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				parameter.put(LPReport.P_SORTIERENACHKUNDE, new Boolean(true));
			} else {
				parameter.put(LPReport.P_SORTIERENACHKUNDE, new Boolean(false));
			}

			if (kritDtoI.iSortierung == ReportAngebotJournalKriterienDto.KRIT_SORT_NACH_VERTRETER) {
				parameter.put(P_SORTIERENACHVERTRETER, new Boolean(true));
			} else {
				parameter.put(P_SORTIERENACHVERTRETER, new Boolean(false));
			}

			parameter
					.put("P_TITLE",
							getTextRespectUISpr("angb.print.alleangebote",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
			parameter.put(P_MANDANTWAEHRUNG,
					theClientDto.getSMandantenwaehrung());

			initJRDS(parameter, AngebotReportFac.REPORT_MODUL,
					AngebotReportFac.REPORT_ANGEBOT_JOURNAL,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);

			oPrint = getReportPrint();
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN,
					new Exception(t));
		}

		return oPrint;
	}

	/**
	 * reportflr: 2 Diese Methode liefert eine Liste von allen
	 * Angebotspositionen eines Mandanten, die nach den eingegebenen Kriterien
	 * des Benutzers zusammengestellt wird. <br>
	 * Achtung: Hibernate verwendet lazy initialization, d.h. der Zugriff auf
	 * Collections muss innerhalb der Session erfolgen.
	 * 
	 * @param kritDtoI
	 *            die Filter- und Sortierkriterien
	 * @param cAktuellerReport
	 *            wird fuer alle und offene Angebote verwendet
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return ReportAngebotpositionDto[] die Liste aller Positionen.
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private ReportAngebotpositionJournalDto[] getListeAngebotpositionenJournal(
			ReportAngebotJournalKriterienDto kritDtoI,
			String erledigungsgrundCNr, String cAktuellerReport,
			TheClientDto theClientDto) throws EJBExceptionLP {

		ReportAngebotpositionJournalDto[] aResult = null;
		Session session = null;
		// es gilt das Locale des Benutzers
		Locale locDruck = theClientDto.getLocUi();
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();

			// Criteria duerfen keine Texts oder Blobs enthalten!

			// Criteria anlegen fuer alle referenzierten Objekte anlegen
			Criteria crit = session
					.createCriteria(FLRAngebotpositionReport.class);
			Criteria critAngebot = crit
					.createCriteria(AngebotpositionFac.FLR_ANGEBOTPOSITION_FLRANGEBOT);

			// Einschraenken nach Mandant
			critAngebot.add(Restrictions.eq("mandant_c_nr",
					theClientDto.getMandant()));

			// Einschraenken nach Status
			if (cAktuellerReport
					.equals(AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ALLE)) {
				if (kritDtoI.getBNurErledigteAngebote()) {
					critAngebot.add(Restrictions.eq(
							AngebotFac.FLR_ANGEBOT_ANGEBOTSTATUS_C_NR,
							AngebotServiceFac.ANGEBOTSTATUS_ERLEDIGT));
					if (erledigungsgrundCNr != null) {
						critAngebot
								.add(Restrictions
										.eq(AngebotFac.FLR_ANGEBOT_ANGEBOTERLEDIGUNGSGRUND_C_NR,
												erledigungsgrundCNr));
					}
				} else {
					// PJ 07/0011040
					/*
					 * critAngebot.add(Restrictions.ne(
					 * AngebotFac.FLR_ANGEBOT_ANGEBOTSTATUS_C_NR,
					 * AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT));
					 */
					critAngebot.add(Restrictions.ne(
							AngebotFac.FLR_ANGEBOT_ANGEBOTSTATUS_C_NR,
							AngebotServiceFac.ANGEBOTSTATUS_STORNIERT));
				}
			} else if (cAktuellerReport
					.equals(AngebotReportFac.REPORT_ANGEBOT_JOURNAL_OFFENE)) {
				critAngebot.add(Restrictions.eq(
						AngebotFac.FLR_ANGEBOT_ANGEBOTSTATUS_C_NR,
						AngebotServiceFac.ANGEBOTSTATUS_OFFEN));
			}

			// UW 22.03.06 alle nicht alternativen Positionen mit positiver
			// Menge
			crit.add(Restrictions
					.isNotNull(AngebotpositionFac.FLR_ANGEBOTPOSITION_N_MENGE));
			crit.add(Restrictions.gt(
					AngebotpositionFac.FLR_ANGEBOTPOSITION_N_MENGE,
					new BigDecimal(0)));
			crit.add(Restrictions.eq(
					AngebotpositionFac.FLR_ANGEBOTPOSITION_B_ALTERNATIVE,
					new Short((short) 0)));

			// Einschraenkung nach einer bestimmten Kostenstelle
			if (kritDtoI.kostenstelleIId != null) {
				critAngebot.add(Restrictions.eq(
						AngebotFac.FLR_ANGEBOT_KOSTENSTELLE_I_ID,
						kritDtoI.kostenstelleIId));
			}

			// Einschraenkung nach einem bestimmten Kunden
			if (kritDtoI.kundeIId != null) {
				critAngebot.add(Restrictions.eq(
						AngebotFac.FLR_ANGEBOT_KUNDE_I_ID_ANGEBOTSADRESSE,
						kritDtoI.kundeIId));
			}

			// Einschraenkung nach einem bestimmten Vertreter
			if (kritDtoI.vertreterIId != null) {
				critAngebot.add(Restrictions.eq(
						AngebotFac.FLR_ANGEBOT_VERTRETER_I_ID,
						kritDtoI.vertreterIId));
			}
			/*
			 * // Belegdatum von bis: flrangebotposition >
			 * flrangebot.t_belegdatum if (kritDtoI.dVon != null) {
			 * critAngebot.add(Restrictions.ge(
			 * AngebotFac.FLR_ANGEBOT_T_BELEGDATUM, kritDtoI.dVon)); }
			 * 
			 * if (kritDtoI.dBis != null) { critAngebot.add(Restrictions.le(
			 * AngebotFac.FLR_ANGEBOT_T_BELEGDATUM, kritDtoI.dBis)); }
			 */
			if (kritDtoI.dVon != null && kritDtoI.dBis != null) {
				Calendar cal = Calendar.getInstance();
				Timestamp von = null;
				cal.setTimeInMillis(kritDtoI.dVon.getTime());
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MILLISECOND, 0);
				von = new Timestamp(cal.getTimeInMillis());

				Timestamp bis = null;
				cal.setTimeInMillis(kritDtoI.dBis.getTime());
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.SECOND, 59);
				cal.set(Calendar.HOUR_OF_DAY, 23);
				bis = new Timestamp(cal.getTimeInMillis());
				critAngebot.add(Restrictions.between(
						AngebotFac.FLR_ANGEBOT_T_BELEGDATUM, von, bis));
			}

			// Einschraenkung nach Belegnummer von - bis
			LpBelegnummerFormat f = getBelegnummerGeneratorObj()
					.getBelegnummernFormat(theClientDto.getMandant());

			Integer iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(
					theClientDto.getMandant());

			String sMandantKuerzel = getParameterFac().getMandantparameter(
					theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_BELEGNUMMER_MANDANTKENNUNG)
					.getCWert();

			if (kritDtoI.sBelegnummerVon != null) {
				critAngebot.add(Restrictions.ge("c_nr", HelperServer
						.getBelegnummernFilterForHibernateCriterias(f,
								iGeschaeftsjahr, sMandantKuerzel,
								kritDtoI.sBelegnummerVon)));
			}

			if (kritDtoI.sBelegnummerBis != null) {
				critAngebot.add(Restrictions.le("c_nr", HelperServer
						.getBelegnummernFilterForHibernateCriterias(f,
								iGeschaeftsjahr, sMandantKuerzel,
								kritDtoI.sBelegnummerBis)));
			}

			// Sortierung nach Kostenstelle ist immer die erste Sortierung
			if (kritDtoI.bSortiereNachKostenstelle) {
				critAngebot.createCriteria(
						AngebotFac.FLR_ANGEBOT_FLRKOSTENSTELLE).addOrder(
						Order.asc("c_nr"));
			}

			// Sortierung nach Kunde, eventuell innerhalb der Kostenstelle
			if (kritDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				critAngebot
						.createCriteria(AngebotFac.FLR_ANGEBOT_FLRKUNDE)
						.createCriteria(KundeFac.FLR_PARTNER)
						.addOrder(
								Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
			}

			// Sortierung nach Vertreter, eventuell innerhalb der Kostenstelle
			if (kritDtoI.iSortierung == ReportAngebotJournalKriterienDto.KRIT_SORT_NACH_VERTRETER) {
				critAngebot
						.createCriteria(AngebotFac.FLR_ANGEBOT_FLRVERTRETER)
						.addOrder(
								Order.asc(PersonalFac.FLR_PERSONAL_C_KURZZEICHEN));
			}

			if (cAktuellerReport
					.equals(AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ALLE)) {
				if (kritDtoI.getBNurErledigteAngebote()) {
					critAngebot
							.addOrder(Order
									.asc(AngebotFac.FLR_ANGEBOT_ANGEBOTERLEDIGUNGSGRUND_C_NR));
				} else {
					critAngebot.addOrder(Order.asc("c_nr"));
				}
			} else {
				// es wird in jedem Fall nach der Belegnummer sortiert
				critAngebot.addOrder(Order.asc("c_nr"));
			}
			List<?> list = crit.list();
			aResult = new ReportAngebotpositionJournalDto[list.size()];
			int iIndex = 0;
			Iterator<?> it = list.iterator();
			ReportAngebotpositionJournalDto reportDto = null;

			while (it.hasNext()) {
				FLRAngebotpositionReport flrangebotposition = (FLRAngebotpositionReport) it
						.next();
				FLRAngebot flrangebot = flrangebotposition.getFlrangebot();
				FLRArtikel flrartikel = flrangebotposition.getFlrartikel();

				reportDto = new ReportAngebotpositionJournalDto();
				reportDto.setIId(flrangebot.getI_id());
				reportDto.setCNr(flrangebot.getC_nr());

				if (flrangebot.getAngeboterledigungsgrund_c_nr() != null) {
					AngeboterledigungsgrundDto angeboterledigungsgrundDto = getAngebotServiceFac()
							.angeboterledigungsgrundFindByPrimaryKey(
									flrangebot
											.getAngeboterledigungsgrund_c_nr(),
									theClientDto);
					reportDto
							.setAngeboterledigungsgrundCNr(angeboterledigungsgrundDto
									.getBezeichnung());
				}
				reportDto.setKundeCName1(flrangebot.getFlrkunde()
						.getFlrpartner().getC_name1nachnamefirmazeile1());
				reportDto.setKundeCName2(flrangebot.getFlrkunde()
						.getFlrpartner().getC_name2vornamefirmazeile2());
				reportDto.setKundeCName3(flrangebot.getFlrkunde()
						.getFlrpartner().getC_name3vorname2abteilung());
				reportDto.setKundeStrasse(flrangebot.getFlrkunde()
						.getFlrpartner().getC_strasse());
				if (flrangebot.getFlrkunde().getFlrpartner().getFlrlandplzort() != null) {
					reportDto.setKundePlz(flrangebot.getFlrkunde()
							.getFlrpartner().getFlrlandplzort().getC_plz());
					reportDto.setKundeLkz(flrangebot.getFlrkunde()
							.getFlrpartner().getFlrlandplzort().getFlrland()
							.getC_lkz());
					reportDto.setKundeOrt(flrangebot.getFlrkunde()
							.getFlrpartner().getFlrlandplzort().getFlrort()
							.getC_name());
				}

				reportDto.setKundeEmail(flrangebot.getFlrkunde()
						.getFlrpartner().getC_email());

				reportDto.setKundeFax(flrangebot.getFlrkunde().getFlrpartner()
						.getC_fax());

				reportDto.setKundeTelefon(flrangebot.getFlrkunde()
						.getFlrpartner().getC_telefon());

				AngebotDto angebotDto = getAngebotFac()
						.angebotFindByPrimaryKey(flrangebot.getI_id(),
								theClientDto);
				if (angebotDto.getAnsprechpartnerIIdKunde() != null) {
					AnsprechpartnerDto ansprechpartnerDto = getAnsprechpartnerFac()
							.ansprechpartnerFindByPrimaryKey(
									angebotDto.getAnsprechpartnerIIdKunde(),
									theClientDto);

					reportDto.setAnsprechpartnerNachname(ansprechpartnerDto
							.getPartnerDto().getCName1nachnamefirmazeile1());
					reportDto.setAnsprechpartnerVorname(ansprechpartnerDto
							.getPartnerDto().getCName2vornamefirmazeile2());
					reportDto.setAnsprechpartnerAnrede(ansprechpartnerDto
							.getPartnerDto().getAnredeCNr());
					reportDto.setAnsprechpartnerTitel(ansprechpartnerDto
							.getPartnerDto().getCTitel());

					reportDto.setAnsprechpartnerFax(ansprechpartnerDto
							.getCDirektfax());

					reportDto.setAnsprechpartnerFaxDw(ansprechpartnerDto
							.getCFax());

					reportDto.setAnsprechpartnerTelefon(ansprechpartnerDto
							.getCHandy());

					reportDto.setAnsprechpartnerTelefonDw(ansprechpartnerDto
							.getCTelefon());

					reportDto.setAnsprechpartnerEmail(ansprechpartnerDto
							.getCEmail());

				}

				reportDto.setKostenstelleCNr(flrangebot.getFlrkostenstelle()
						.getC_nr());
				reportDto.setVertreterCName1(getPersonalFac()
						.personalFindByPrimaryKey(
								flrangebot.getVertreter_i_id(), theClientDto)
						.getPartnerDto().formatFixName2Name1());
				reportDto.setProjektBez(flrangebot.getC_bez());
				reportDto.setRealisierungstermin(Helper.formatDatum(
						flrangebot.getT_realisierungstermin(),
						theClientDto.getLocUi()));

				if (flrangebot.getAngeboterledigungsgrund_c_nr() != null)
					if (flrangebot
							.getAngeboterledigungsgrund_c_nr()
							.equals(AngebotServiceFac.ANGEBOTERLEDIGUNGSGRUND_AUFTRAGERHALTEN)) {
						AuftragDto[] auftrag = getAuftragFac()
								.auftragFindByAngebotIId(flrangebot.getI_id(),
										theClientDto);
						if (auftrag != null && auftrag.length > 0) {
							reportDto.setErledigungsgrundABNr(auftrag[0]
									.getCNr());
						}
					}

				// UW 08.03.06 Es kann sich um eine Ident, Handeingabe oder
				// AGStueckliste handeln
				String identCNr = null;
				String identCBez = null;

				if (flrangebotposition.getAngebotpositionart_c_nr().equals(
						AngebotServiceFac.ANGEBOTPOSITIONART_IDENT)) {
					identCNr = flrartikel.getC_nr();
					identCBez = getArtikelFac()
							.formatArtikelbezeichnungEinzeiligOhneExc(
									flrartikel.getI_id(),
									theClientDto.getLocUi());
				} else if (flrangebotposition
						.getAngebotpositionart_c_nr()
						.equals(AngebotServiceFac.ANGEBOTPOSITIONART_AGSTUECKLISTE)) {
					identCBez = flrangebotposition.getFlragstkl().getC_bez();
				} else if (flrangebotposition
						.getAngebotpositionart_c_nr()
						.equals(AngebotServiceFac.ANGEBOTPOSITIONART_HANDEINGABE)) {
					identCBez = getArtikelFac()
							.formatArtikelbezeichnungEinzeiligOhneExc(
									flrartikel.getI_id(),
									theClientDto.getLocUi());
				}

				reportDto.setArtikelCNr(identCNr);
				reportDto.setArtikelCBez(identCBez);

				reportDto.setNMenge(flrangebotposition.getN_menge());
				reportDto
						.setEinheitCNr(flrangebotposition.getEinheit_c_nr() == null ? ""
								: flrangebotposition.getEinheit_c_nr().trim());
				reportDto.setDAuftragwahrscheinlichkeit(flrangebot
						.getF_auftragswahrscheinlichkeit());
				reportDto.setNWert(flrangebot
						.getN_gesamtangebotswertinangebotswaehrung());

				// Umrechnen in Mandantenwaehrung, Positionspreise sind in
				// Belegwaehrung abgelegt
				BigDecimal nPreisAmDruck = flrangebotposition
						.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte();

				nPreisAmDruck = getBetragMalWechselkurs(
						nPreisAmDruck,
						Helper.getKehrwert(new BigDecimal(
								flrangebot
										.getF_wechselkursmandantwaehrungzuangebotswaehrung()
										.doubleValue())));

				reportDto.setNPreis(nPreisAmDruck);

				aResult[iIndex] = reportDto;
				iIndex++;
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR,
					new Exception(t));
		} finally {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE, he);
			}
		}

		return aResult;
	}

	/**
	 * String zum Andrucken der Filterkriterien.
	 * 
	 * @param kritDtoI
	 *            die Kriterien
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return String die Filterkriterien
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private String buildFilterAngebotJournal(
			ReportAngebotJournalKriterienDto kritDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		StringBuffer buff = new StringBuffer("");

		// Belegdatum
		if (kritDtoI.dVon != null || kritDtoI.dBis != null) {
			buff.append(getTextRespectUISpr("bes.belegdatum",
					theClientDto.getMandant(), theClientDto.getLocUi()));
		}

		if (kritDtoI.dVon != null) {
			buff.append(" ").append(
					getTextRespectUISpr("lp.von", theClientDto.getMandant(),
							theClientDto.getLocUi()));
			buff.append(" ").append(
					Helper.formatDatum(kritDtoI.dVon, theClientDto.getLocUi()));
		}

		if (kritDtoI.dBis != null) {
			buff.append(" ").append(
					getTextRespectUISpr("lp.bis", theClientDto.getMandant(),
							theClientDto.getLocUi()));
			buff.append(" ").append(
					Helper.formatDatum(kritDtoI.dBis, theClientDto.getLocUi()));
		}

		// Kostenstelle
		if (kritDtoI.kostenstelleIId != null) {
			buff.append(" ")
					.append(getTextRespectUISpr("lp.kostenstelle",
							theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ").append(
					getSystemFac().kostenstelleFindByPrimaryKey(
							kritDtoI.kostenstelleIId).getCNr());
		}

		// Kunde
		if (kritDtoI.kundeIId != null) {
			buff.append(" ").append(
					getTextRespectUISpr("lp.kunde", theClientDto.getMandant(),
							theClientDto.getLocUi()));
			buff.append(" ").append(
					getKundeFac()
							.kundeFindByPrimaryKey(kritDtoI.kundeIId,
									theClientDto).getPartnerDto()
							.getCName1nachnamefirmazeile1());
		}

		// Vertreter
		if (kritDtoI.vertreterIId != null) {
			buff.append(" ")
					.append(getTextRespectUISpr("lp.vertreter",
							theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ").append(
					getPersonalFac()
							.personalFindByPrimaryKey(kritDtoI.vertreterIId,
									theClientDto).getPartnerDto()
							.formatFixName2Name1());
		}

		// Angebotsnummer
		if (kritDtoI.sBelegnummerVon != null
				|| kritDtoI.sBelegnummerBis != null) {
			buff.append(" ")
					.append(getTextRespectUISpr("angb.angebotnummer",
							theClientDto.getMandant(), theClientDto.getLocUi()));
		}

		if (kritDtoI.sBelegnummerVon != null) {
			buff.append(" ").append(
					getTextRespectUISpr("lp.von", theClientDto.getMandant(),
							theClientDto.getLocUi()));
			buff.append(" ").append(kritDtoI.sBelegnummerVon);
		}

		if (kritDtoI.sBelegnummerBis != null) {
			buff.append(" ").append(
					getTextRespectUISpr("lp.bis", theClientDto.getMandant(),
							theClientDto.getLocUi()));
			buff.append(" ").append(kritDtoI.sBelegnummerBis);
		}

		if (kritDtoI.getBNurErledigteAngebote()) {
			buff.append(" ")
					.append(getTextRespectUISpr("angb.nurerledigte",
							theClientDto.getMandant(), theClientDto.getLocUi()));
		}

		String cBuffer = buff.toString().trim();

		return cBuffer;
	}

	/**
	 * String zum Andrucken der Sortierkriterien.
	 * 
	 * @param reportJournalKriterienDtoI
	 *            die Kriterien
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return String die Sortierkriterien
	 */
	private String buildSortierungAngebotJournal(
			ReportJournalKriterienDto reportJournalKriterienDtoI,
			TheClientDto theClientDto) {
		StringBuffer buff = new StringBuffer(getTextRespectUISpr(
				"lp.sortierungnach", theClientDto.getMandant(),
				theClientDto.getLocUi()));

		if (reportJournalKriterienDtoI.bSortiereNachKostenstelle) {
			buff.append(
					getTextRespectUISpr("lp.kostenstelle",
							theClientDto.getMandant(), theClientDto.getLocUi()))
					.append(", ");
		}

		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
			buff.append(
					getTextRespectUISpr("lp.kunde", theClientDto.getMandant(),
							theClientDto.getLocUi())).append(", ");
		}

		if (reportJournalKriterienDtoI.iSortierung == ReportAngebotJournalKriterienDto.KRIT_SORT_NACH_VERTRETER) {
			buff.append(
					getTextRespectUISpr("lp.vertreter",
							theClientDto.getMandant(), theClientDto.getLocUi()))
					.append(", ");
		}

		buff.append(getTextRespectUISpr("angb.angebotnummer",
				theClientDto.getMandant(), theClientDto.getLocUi()));

		return buff.toString();
	}

	/**
	 * Alle offenen Angebote drucken.
	 * 
	 * @param kritDtoI
	 *            die Filter- und Sortierkriterien
	 * @param theClientDto
	 *            der atkuelle Benutzer
	 * @return JasperPrint der Druck
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAngebotOffene(
			ReportAngebotJournalKriterienDto kritDtoI, Boolean bKommentare,
			Boolean bDetails, Boolean bKundenstammdaten,
			TheClientDto theClientDto) throws EJBExceptionLP {

		JasperPrintLP oPrint = null;
		cAktuellerReport = AngebotReportFac.REPORT_ANGEBOT_JOURNAL_OFFENE;

		try {
			// die Daten des Report ueber Hibernate holen
			ReportAngebotpositionJournalDto[] aReportDto = getListeAngebotpositionenJournal(
					kritDtoI, null,
					AngebotReportFac.REPORT_ANGEBOT_JOURNAL_OFFENE,
					theClientDto);

			int iAnzahlZeilen = aReportDto.length; // Anzahl der Positionen
			int iAnzahlSpalten = 37; // Anzahl der Spalten in der Gruppe

			data = new Object[iAnzahlZeilen][iAnzahlSpalten];

			// die Datenmatrix befuellen
			for (int i = 0; i < iAnzahlZeilen; i++) {
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANGEBOTCNR] = aReportDto[i]
						.getCNr();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDECNAME1] = aReportDto[i]
						.getKundeCName1();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KOSTENSTELLECNR] = aReportDto[i]
						.getKostenstelleCNr();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_VERTRETERCNAME1] = aReportDto[i]
						.getVertreterCName1();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_PROJEKTBEZ] = aReportDto[i]
						.getProejktBez();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_REALISIERUNGSTERMIN] = aReportDto[i]
						.getRealisierungstermin();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_AUFTRAGWAHRSCHEINLICHKEIT] = aReportDto[i]
						.getDAuftragwahrscheinlichkeit();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANGEBOTWERT] = aReportDto[i]
						.getNWert();

				// @todo kann man das besser machen? xText darf in Hibernate
				// Criteria nicht vorkommen PJ 3749
				AngebotDto angebotDto = getAngebotFac()
						.angebotFindByPrimaryKey(aReportDto[i].getIId(),
								theClientDto);

				if (bKommentare.booleanValue()) {
					data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_INTERNERKOMMENTAR] = angebotDto
							.getXInternerkommentar();
				} else {
					data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_INTERNERKOMMENTAR] = "";
				}
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_EXTERNERKOMMENTAR] = angebotDto
						.getXExternerkommentar();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ARTIKELCNR] = aReportDto[i]
						.getArtikelCNr();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ARTIKELBEZ] = aReportDto[i]
						.getArtkelCBez();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ARTIKELMENGE] = aReportDto[i]
						.getNMenge();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ARTIKELEINHEIT] = aReportDto[i]
						.getEinheitCNr();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ARTIKELPREIS] = aReportDto[i]
						.getNPreis();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANGEBOTERLEDIGUNGSGRUND] = aReportDto[i]
						.getAngeboterledigungsgrundCNr();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ERLEDIGUNGSGRUND_AB_NR] = aReportDto[i]
						.getErledigungsgrundABNr();

				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDECNAME2] = aReportDto[i]
						.getKundeCName2();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDECNAME3] = aReportDto[i]
						.getKundeCName3();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDESTRASSE] = aReportDto[i]
						.getKundeStrasse();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDEPLZ] = aReportDto[i]
						.getKundePlz();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDEORT] = aReportDto[i]
						.getKundeOrt();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDELKZ] = aReportDto[i]
						.getKundeLkz();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDETELEFON] = aReportDto[i]
						.getKundeTelefon();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDEFAX] = aReportDto[i]
						.getKundeFax();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDEEMAIL] = aReportDto[i]
						.getKundeEmail();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDEHOMEPAGE] = aReportDto[i]
						.getKundeHomepage();

				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_VORNAME] = aReportDto[i]
						.getAnsprechpartnerVorname();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_NACHNAME] = aReportDto[i]
						.getAnsprechpartnerNachname();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_TITEL] = aReportDto[i]
						.getAnsprechpartnerTitel();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_ANREDE] = aReportDto[i]
						.getAnsprechpartnerAnrede();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_TELEFON] = aReportDto[i]
						.getAnsprechpartnerTelefon();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_TELEFONDW] = aReportDto[i]
						.getAnsprechpartnerTelefonDw();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_FAX] = aReportDto[i]
						.getAnsprechpartnerFax();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_FAXDW] = aReportDto[i]
						.getAnsprechpartnerFaxDw();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_EMAIL] = aReportDto[i]
						.getAnsprechpartnerEmail();

			}

			// Erstellung des Report
			HashMap<String, Object> parameter = new HashMap<String, Object>();

			// die Parameter dem Report uebergeben
			parameter.put(LPReport.P_SORTIERUNG,
					buildSortierungAngebotJournal(kritDtoI, theClientDto));
			parameter.put(LPReport.P_FILTER,
					buildFilterAngebotJournal(kritDtoI, theClientDto));

			parameter.put("P_MITKUNDENSTAMMDATEN", bKundenstammdaten);

			// die Parameter zur Bildung von Zwischensummen uebergeben
			if (kritDtoI.bSortiereNachKostenstelle) {
				parameter.put(LPReport.P_SORTIERENACHKOSTENSTELLE, new Boolean(
						true));
			} else {
				parameter.put(LPReport.P_SORTIERENACHKOSTENSTELLE, new Boolean(
						false));
			}

			if (kritDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				parameter.put(LPReport.P_SORTIERENACHKUNDE, new Boolean(true));
			} else {
				parameter.put(LPReport.P_SORTIERENACHKUNDE, new Boolean(false));
			}

			if (kritDtoI.iSortierung == ReportAngebotJournalKriterienDto.KRIT_SORT_NACH_VERTRETER) {
				parameter.put(P_SORTIERENACHVERTRETER, new Boolean(true));
			} else {
				parameter.put(P_SORTIERENACHVERTRETER, new Boolean(false));
			}

			parameter
					.put("P_TITLE",
							getTextRespectUISpr("angb.print.offeneangebote",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));

			parameter.put(P_MANDANTWAEHRUNG,
					theClientDto.getSMandantenwaehrung());

			parameter.put(P_MITDETAILS, bDetails);

			initJRDS(parameter, AngebotReportFac.REPORT_MODUL,
					AngebotReportFac.REPORT_ANGEBOT_JOURNAL,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);

			oPrint = getReportPrint();
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return oPrint;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAngebotspotential(TheClientDto theClientDto) {

		cAktuellerReport = AngebotReportFac.REPORT_ANGEBOTSPOTENTIAL;

		Session session = FLRSessionFactory.getFactory().openSession();
		String queryString = "SELECT pos,(SELECT spr.c_bez FROM FLRArtikellistespr as spr WHERE spr.Id.artikelliste=pos.flrartikel.i_id AND spr.Id.locale='"
				+ theClientDto.getLocUiAsString()
				+ "' ) FROM FLRAngebotpositionReport pos WHERE pos.flrangebot.angebotstatus_c_nr='"
				+ AngebotServiceFac.ANGEBOTSTATUS_OFFEN
				+ "' AND pos.artikel_i_id IS NOT NULL AND pos.angebotpositionart_c_nr='"
				+ AngebotServiceFac.ANGEBOTPOSITIONART_IDENT
				+ "' ORDER BY pos.flrartikel.i_id";

		Query query = session.createQuery(queryString);

		List<?> resultList = query.list();

		Iterator<?> resultListIterator = resultList.iterator();

		ArrayList alDaten = new ArrayList();

		while (resultListIterator.hasNext()) {
			Object[] f = (Object[]) resultListIterator.next();
			FLRAngebotpositionReport pos = (FLRAngebotpositionReport) f[0];

			Object[] zeile = new Object[11];

			zeile[REPORT_ANGEBOTSPOTENTIAL_ARTIKELNUMMER] = pos.getFlrartikel()
					.getC_nr();
			zeile[REPORT_ANGEBOTSPOTENTIAL_ARTIKELBEZEICHNUNG] = f[1];
			zeile[REPORT_ANGEBOTSPOTENTIAL_BELEGDATUM] = pos.getFlrangebot()
					.getT_belegdatum();
			zeile[REPORT_ANGEBOTSPOTENTIAL_BELEGNUMMER] = pos.getFlrangebot()
					.getC_nr();
			zeile[REPORT_ANGEBOTSPOTENTIAL_MENGE] = pos.getN_menge();
			zeile[REPORT_ANGEBOTSPOTENTIAL_PROJEKT] = pos.getFlrangebot()
					.getC_bez();

			String kunde = pos.getFlrangebot().getFlrkunde().getFlrpartner()
					.getC_name1nachnamefirmazeile1();

			if (pos.getFlrangebot().getFlrkunde().getFlrpartner()
					.getC_name2vornamefirmazeile2() != null) {
				kunde += " "
						+ pos.getFlrangebot().getFlrkunde().getFlrpartner()
								.getC_name2vornamefirmazeile2();
			}

			zeile[REPORT_ANGEBOTSPOTENTIAL_KUNDE] = kunde;

			zeile[REPORT_ANGEBOTSPOTENTIAL_KUNDE_KURZBEZEICHNUNG] = pos
					.getFlrangebot().getFlrkunde().getFlrpartner().getC_kbez();

			zeile[REPORT_ANGEBOTSPOTENTIAL_LIEFERANT] = "";
			try {
				ArtikellieferantDto alDto = getArtikelFac()
						.getArtikelEinkaufspreis(pos.getFlrartikel().getI_id(),
								pos.getN_menge(),
								theClientDto.getSMandantenwaehrung(),
								theClientDto);

				if (alDto != null) {
					zeile[REPORT_ANGEBOTSPOTENTIAL_EKPREIS] = alDto
							.getLief1Preis();

					if (alDto.getLieferantDto() != null) {

						String lieferant = alDto.getLieferantDto()
								.getPartnerDto().getCName1nachnamefirmazeile1();

						if (alDto.getLieferantDto().getPartnerDto()
								.getCName2vornamefirmazeile2() != null) {
							lieferant += " "
									+ alDto.getLieferantDto().getPartnerDto()
											.getCName2vornamefirmazeile2();
						}

						zeile[REPORT_ANGEBOTSPOTENTIAL_LIEFERANT] = lieferant;
					}

				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			alDaten.add(zeile);

		}

		for (int i = alDaten.size() - 1; i > 0; --i) {
			for (int j = 0; j < i; ++j) {
				Object[] o = (Object[]) alDaten.get(j);
				Object[] o1 = (Object[]) alDaten.get(j + 1);

				String wert = Helper.fillStringWithBlankRight(
						((String) o[REPORT_ANGEBOTSPOTENTIAL_LIEFERANT]), 80)
						+ ((String) o[REPORT_ANGEBOTSPOTENTIAL_ARTIKELNUMMER]);
				String wert1 = Helper.fillStringWithBlankRight(
						((String) o1[REPORT_ANGEBOTSPOTENTIAL_LIEFERANT]), 80)
						+ ((String) o1[REPORT_ANGEBOTSPOTENTIAL_ARTIKELNUMMER]);
				if (wert.compareTo(wert1) > 0) {
					alDaten.set(j, o1);
					alDaten.set(j + 1, o);
				}
			}
		}

		Object[][] returnArray = new Object[alDaten.size()][11];
		data = (Object[][]) alDaten.toArray(returnArray);

		session.close();

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		initJRDS(parameter, AngebotReportFac.REPORT_MODUL,
				AngebotReportFac.REPORT_ANGEBOTSPOTENTIAL,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	/**
	 * Alle abgelehnte Angebote drucken.
	 * 
	 * @param kritDtoI
	 *            die Filter- und Sortierkriterien
	 * @param theClientDto
	 *            der atkuelle Benutzer
	 * @return JasperPrint der Druck
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public JasperPrintLP printAngebotAbgelehnte(
			ReportAngebotJournalKriterienDto kritDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		JasperPrintLP oPrint = null;
		StringBuffer buff = null;
		StringBuffer zbuff = null;
		cAktuellerReport = AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE;
		try {
			Session session = null;
			try {
				SessionFactory factory = FLRSessionFactory.getFactory();
				session = factory.openSession();
				// Criteria anlegen fuer alle referenzierten Objekte anlegen
				Criteria crit = session.createCriteria(FLRAngebot.class);
				// Einschraenken nach Mandant
				crit.add(Restrictions.eq("mandant_c_nr",
						theClientDto.getMandant()));
				// Einschraenken nach Status
				crit.add(Restrictions.eq(
						AngebotFac.FLR_ANGEBOT_ANGEBOTSTATUS_C_NR,
						AngebotServiceFac.ANGEBOTSTATUS_ERLEDIGT));
				crit.add(Restrictions
						.isNotNull(AngebotFac.FLR_ANGEBOT_T_MANUELLERLEDIGT));
				crit.add(Restrictions
						.isNotNull(AngebotFac.FLR_ANGEBOT_ANGEBOTERLEDIGUNGSGRUND_C_NR));
				// Einschraenkung nach einem bestimmten Kunden
				if (kritDtoI.kundeIId != null) {
					crit.add(Restrictions.eq(
							AngebotFac.FLR_ANGEBOT_KUNDE_I_ID_ANGEBOTSADRESSE,
							kritDtoI.kundeIId));
				}
				if (kritDtoI.dVon != null) {
					crit.add(Restrictions.ge(
							AngebotFac.FLR_ANGEBOT_T_MANUELLERLEDIGT,
							kritDtoI.dVon));
				}

				if (kritDtoI.dBis != null) {
					crit.add(Restrictions.le(
							AngebotFac.FLR_ANGEBOT_T_MANUELLERLEDIGT,
							kritDtoI.dBis));
				}
				// es wird in jedem Fall nach der Belegnummer sortiert

				crit.addOrder(Order
						.asc(AngebotFac.FLR_ANGEBOT_ANGEBOTERLEDIGUNGSGRUND_C_NR));
				crit.addOrder(Order.asc("c_nr"));

				int iIndex = 0;
				List<?> list = crit.list();
				Iterator<?> it = list.iterator();
				int iAnzahlZeilen = list.size(); // Anzahl der Positionen
				int iAnzahlSpalten = 12; // Anzahl der Spalten in der Gruppe
				data = new Object[iAnzahlZeilen][iAnzahlSpalten];
				HashMap<String, Integer> hm = new HashMap<String, Integer>();
				while (it.hasNext()) {
					FLRAngebot flrangebot = (FLRAngebot) it.next();
					// erledigungsgruende zaehlen
					String erledigungsgrund = flrangebot
							.getAngeboterledigungsgrund_c_nr();
					Integer value = hm.get(erledigungsgrund);
					if (value == null) {
						hm.put(erledigungsgrund, 1);
					} else {
						hm.put(erledigungsgrund, value + 1); // wird ersetzt
					}
					// Daten fuer den Report
					data[iIndex][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_ANGEBOTCNR] = flrangebot
							.getC_nr();
					data[iIndex][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_KUNDECNAME1] = flrangebot
							.getFlrkunde().getFlrpartner()
							.getC_name1nachnamefirmazeile1();
					data[iIndex][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_KOSTENSTELLECNR] = flrangebot
							.getFlrkostenstelle().getC_nr();
					data[iIndex][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_VERTRETERCNAME1] = flrangebot
							.getFlrvertreter().getFlrpartner()
							.getC_name1nachnamefirmazeile1();
					if (flrangebot.getC_bez() != null) {
						data[iIndex][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_PROJEKTBEZ] = flrangebot
								.getC_bez();
					}
					data[iIndex][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_REALISIERUNGSTERMIN] = Helper
							.formatDatum(flrangebot.getT_realisierungstermin(),
									theClientDto.getLocUi());
					data[iIndex][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_ANGEBOTERLEDIGUNGSGRUND] = flrangebot
							.getAngeboterledigungsgrund_c_nr();
					if (flrangebot.getAngeboterledigungsgrund_c_nr() != null)
						if (flrangebot
								.getAngeboterledigungsgrund_c_nr()
								.equals(AngebotServiceFac.ANGEBOTERLEDIGUNGSGRUND_AUFTRAGERHALTEN)) {
							AuftragDto[] auftrag = getAuftragFac()
									.auftragFindByAngebotIId(
											flrangebot.getI_id(), theClientDto);
							data[iIndex][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_ERLEDIGUNGSGRUND_AB_NR] = auftrag[0]
									.getCNr();
						}
					data[iIndex][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_GESAMTANGEBOTSWERT] = flrangebot
							.getN_gesamtangebotswertinangebotswaehrung();
					data[iIndex][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_INTERNERKOMMENTAR] = flrangebot
							.getX_internerkommentar();
					data[iIndex][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_EXTERNERKOMMENTAR] = flrangebot
							.getX_externerkommentar();

					iIndex++;
				}
				// erledigungsgruende nach der anzahl sortieren
				buff = new StringBuffer("");
				zbuff = new StringBuffer("");
				Map<Integer, String> sortedMap = new TreeMap<Integer, String>();
				for (Iterator<String> it3 = hm.keySet().iterator(); it3
						.hasNext();) {
					String cGrund = (String) it3.next();
					sortedMap.put(-hm.get(cGrund), cGrund); // negieren, damit
					// die reihenfolge
					// stimmt (die
					// grossen zahlen
					// zuerst), muss
					// nachher wieder
					// umgedreht werden
					buff.append(cGrund);
					buff.append("\n");
					zbuff.append(hm.get(cGrund));
					zbuff.append("\n");
				}
			} catch (Throwable t) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR,
						new Exception(t));
			} finally {
				try {
					session.close();
				} catch (HibernateException he) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE,
							he);
				}
			}
			// Erstellung des Report
			HashMap<String, Object> parameter = new HashMap<String, Object>();
			// die Parameter dem Report uebergeben
			parameter.put(LPReport.P_SORTIERUNG,
					buildSortierungAngebotJournal(kritDtoI, theClientDto));
			parameter.put(LPReport.P_FILTER,
					buildFilterAngebotJournal(kritDtoI, theClientDto));

			if (kritDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				parameter.put(LPReport.P_SORTIERENACHKUNDE, new Boolean(true));
			} else {
				parameter.put(LPReport.P_SORTIERENACHKUNDE, new Boolean(false));
			}

			parameter
					.put("P_TITLE",
							getTextRespectUISpr("angb.print.abgelehnte",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
			String cBuffer = buff.toString().trim();
			parameter.put("P_STATISTIK_SPALTE1", cBuffer);
			parameter.put("P_STATISTIK_SPALTE2", zbuff.toString());
			parameter.put(P_MANDANTWAEHRUNG,
					theClientDto.getSMandantenwaehrung());

			initJRDS(parameter, AngebotReportFac.REPORT_MODUL,
					AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);

			oPrint = getReportPrint();
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN,
					new Exception(t));
		}

		return oPrint;
	}

	/**
	 * Die Vorkalkulation eines Angebots drucken. <br>
	 * Beruecksichtigt werden nur preisbehaftete Positionen.
	 * 
	 * @param iIdAngebotI
	 *            PK des Angebots
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return JasperPrint der Druck
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public JasperPrintLP printAngebotVorkalkulation(Integer iIdAngebotI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		JasperPrintLP oPrint = null;
		cAktuellerReport = AngebotReportFac.REPORT_VORKALKULATION;
		Session session = null;

		try {
			AngebotDto angebotDto = getAngebotFac().angebotFindByPrimaryKey(
					iIdAngebotI, theClientDto);

			// UW 29.03.06 Tiefe der Stuecklistenaufloeseung bestimmen
			ParametermandantDto parametermandantDto = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ANGEBOT,
							ParameterFac.PARAMETER_ANGEBOT_AGVORKALK_STKL_AUFLOESUNG_TIEFE);

			ParametermandantDto parameterGestpreisBerechnung = null;
			parameterGestpreisBerechnung = (ParametermandantDto) getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_GESTPREISBERECHNUNG_HAUPTLAGER);

			boolean bGestpreisberechnungHauptlager = ((Boolean) parameterGestpreisBerechnung
					.getCWertAsObject()).booleanValue();

			int iStuecklisteaufloesungTiefe = ((Integer) parametermandantDto
					.getCWertAsObject()).intValue();

			session = FLRSessionFactory.getFactory().openSession();

			Criteria crit = session
					.createCriteria(FLRAngebotpositionReport.class);
			crit.add(Restrictions
					.eq(AngebotpositionFac.FLR_ANGEBOTPOSITION_FLRANGEBOT
							+ ".i_id", iIdAngebotI));

			// UW 22.03.06 alle nicht alternativen Positionen mit positiver
			// Menge
			crit.add(Restrictions
					.isNotNull(AngebotpositionFac.FLR_ANGEBOTPOSITION_N_MENGE));
			crit.add(Restrictions.gt(
					AngebotpositionFac.FLR_ANGEBOTPOSITION_N_MENGE,
					new BigDecimal(0)));
			crit.add(Restrictions.eq(
					AngebotpositionFac.FLR_ANGEBOTPOSITION_B_ALTERNATIVE,
					new Short((short) 0)));

			crit.addOrder(Order
					.asc(AngebotpositionFac.FLR_ANGEBOTPOSITION_I_SORT));

			// Liste aller Positionen, die behandelt werden sollen
			List<?> list = crit.list();

			Iterator itListe = list.iterator();

			ArrayList<Object[]> alDaten = new ArrayList<Object[]>();

			// Der Index der aktuell verarbeiteten Angebotposition, Index laeuft
			// ueber die Unterpositionen!
			int row = 0;

			while (itListe.hasNext()) { // iterieren ueber die
				row++;
				// gesamte Datenmatrix
				FLRAngebotpositionReport flrangebotposition = (FLRAngebotpositionReport) itListe
						.next();

				if (flrangebotposition.getAngebotpositionart_c_nr().equals(
						AngebotServiceFac.ANGEBOTPOSITIONART_IDENT)) {

					boolean bSetArtikel=false;
					StuecklisteDto stuecklisteDto = getStuecklisteFac()
							.stuecklisteFindByMandantCNrArtikelIIdOhneExc(flrangebotposition.getArtikel_i_id(),
									theClientDto);

					// wenn die aktuelle Artikelposition eine Stueckliste ist
					if (stuecklisteDto != null && stuecklisteDto.getStuecklisteartCNr().equals(StuecklisteFac.STUECKLISTEART_SETARTIKEL)) {
						bSetArtikel=true;
					}
					
					// PJ15141

					// zum Befuellen der Preisinformationen muss bekannt sein,
					// ob es Stuecklistenpositionen gibt
					StuecklisteInfoDto stuecklisteInfoDto = getStuecklisteFac()
							.getStrukturdatenEinesArtikels(
									flrangebotposition.getArtikel_i_id(),
									false, null, // in die Rekursion mit einer
									// leeren Listen einsteigen
									0, // in die Rekursion mit Ebene 0
										// einsteigen
									iStuecklisteaufloesungTiefe, // alle
									// Stuecklisten
									// lt.
									// Parameter
									// aufloesen
									true, // Basis sind die Einheiten der
									// Stueckliste
									flrangebotposition.getN_menge(), // Basis
									// sind
									// n
									// Einheiten
									// der
									// Stueckliste
									false, // Fremdfertigung nicht aufloesen
									theClientDto);

					if (bSetArtikel == false && stuecklisteInfoDto.getIiAnzahlPositionen().intValue() > 0
							&& (!Helper.short2boolean(stuecklisteInfoDto
									.getBIstFremdfertigung()) || iStuecklisteaufloesungTiefe != 0)) {
						alDaten.add(befuelleZeileVKMitIdentMitSTKLAusAngebotposition(
								flrangebotposition,
								angebotDto.getTRealisierungstermin(),
								theClientDto));

						if (stuecklisteInfoDto.getIiAnzahlPositionen()
								.intValue() > 0) {
							ArrayList<?> alStuecklisteAufgeloest = stuecklisteInfoDto
									.getAlStuecklisteAufgeloest();
							Iterator<?> it = alStuecklisteAufgeloest.iterator();

							while (it.hasNext()) {
								alDaten.add(befuelleZeileVorkalkulationMitStuecklistenposition(
										flrangebotposition.getArtikel_i_id(), // eine
										// Zwischensumme
										// fuer den
										// uebergeordneten
										// Artikel
										// bilden
										"",
										(StuecklisteMitStrukturDto) it.next(),
										flrangebotposition
												.getFlrangebot()
												.getWaehrung_c_nr_angebotswaehrung(),
										flrangebotposition.getN_menge(),
										flrangebotposition.getEinheit_c_nr(),
										iStuecklisteaufloesungTiefe,
										bGestpreisberechnungHauptlager,
										angebotDto.getTRealisierungstermin(),
										theClientDto));
							}
						}
					} else {
						// UW 29.03.06 Eine STKL wird wie ein normaler Artikel
						// angedruckt, wenn...
						// ...es sich um eine Fremdfertigung handelt
						// ...es keine Positionen gibt
						// ...die gewuenschte Aufloesungstiefe lt. Parameter 0
						// ist
						alDaten.add(befuelleZeileVKMitIdentOhneSTKLAusAngebotposition(
								flrangebotposition,
								angebotDto.getTRealisierungstermin(),
								theClientDto, angebotDto.getWaehrungCNr()));
					}

					// PJ 16216
					StuecklisteDto stklDto = getStuecklisteFac()
							.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
									flrangebotposition.getArtikel_i_id(),
									theClientDto);
					if (stklDto != null
							&& (!Helper.short2boolean(stklDto
									.getBFremdfertigung()) || iStuecklisteaufloesungTiefe != 0)) {

						StuecklistearbeitsplanDto[] stuecklistearbeitsplanDtos = getStuecklisteFac()
								.stuecklistearbeitsplanFindByStuecklisteIId(
										stklDto.getIId(), theClientDto);

						for (int i = 0; i < stuecklistearbeitsplanDtos.length; i++) {
							StuecklistearbeitsplanDto stuecklistearbeitsplanDto = stuecklistearbeitsplanDtos[i];

							if (stklDto.getIErfassungsfaktor() != 0) {
								stuecklistearbeitsplanDto
										.setLStueckzeit(stuecklistearbeitsplanDto
												.getLStueckzeit()
												/ stklDto
														.getIErfassungsfaktor());
							}

							if (!Helper.short2boolean(stuecklistearbeitsplanDto
									.getBNurmaschinenzeit())) {

								alDaten.add(befuelleZeileVorkalkulationMitStuecklistearbeitsplan(
										flrangebotposition.getArtikel_i_id(), // eine
										// Zwischensumme
										// fuer den
										// uebergeordneten
										// Artikel
										// bilden
										"",
										stuecklistearbeitsplanDto,
										flrangebotposition
												.getFlrangebot()
												.getWaehrung_c_nr_angebotswaehrung(),
										flrangebotposition.getN_menge(),
										flrangebotposition.getEinheit_c_nr(),
										iStuecklisteaufloesungTiefe,
										bGestpreisberechnungHauptlager,
										angebotDto.getTRealisierungstermin(),
										stklDto, theClientDto, false));
							}
							if (stuecklistearbeitsplanDto.getMaschineIId() != null) {

								alDaten.add(befuelleZeileVorkalkulationMitStuecklistearbeitsplan(
										flrangebotposition.getArtikel_i_id(), // eine
										// Zwischensumme
										// fuer den
										// uebergeordneten
										// Artikel
										// bilden
										"",
										stuecklistearbeitsplanDto,
										flrangebotposition
												.getFlrangebot()
												.getWaehrung_c_nr_angebotswaehrung(),
										flrangebotposition.getN_menge(),
										flrangebotposition.getEinheit_c_nr(),
										iStuecklisteaufloesungTiefe,
										bGestpreisberechnungHauptlager,
										angebotDto.getTRealisierungstermin(),
										stklDto, theClientDto, true));
							}

						}

					}

				} else if (flrangebotposition
						.getAngebotpositionart_c_nr()
						.equals(AngebotServiceFac.ANGEBOTPOSITIONART_HANDEINGABE)) {
					alDaten.add(befuelleZeileVKMitHandAusAngebotposition(
							flrangebotposition, theClientDto));

				} else if (flrangebotposition
						.getAngebotpositionart_c_nr()
						.equals(AngebotServiceFac.ANGEBOTPOSITIONART_AGSTUECKLISTE)) {
					// jede Zeile muss als INDEX_GRUPPE die AGSTKL cNr haben!
					alDaten.add(befuelleZeileVKMitAGSTKLPosition(
							flrangebotposition, theClientDto));

					AgstklpositionDto[] aAgstklpositionDto = getAngebotstklpositionFac()
							.agstklpositionFindByAgstklIIdOhneExc(
									flrangebotposition.getAgstkl_i_id(),
									theClientDto);

					// Einrueckungsebene gegenueber der AGSTKL
					String cEinrueckung = "    ";

					for (int j = 0; j < aAgstklpositionDto.length; j++) {
						// AGPosition ist dieselbe, aber die naechste Zeile
						// im data[][] befuellen

						ArtikelDto artikelDtoAgstklposition = getArtikelFac()
								.artikelFindByPrimaryKeySmall(
										aAgstklpositionDto[j].getArtikelIId(),
										theClientDto);

						if (artikelDtoAgstklposition.getArtikelartCNr().equals(
								ArtikelFac.ARTIKELART_HANDARTIKEL)) {

							alDaten.add(befuelleZeileVKMitHandAusAGSTKLPosition(
									aAgstklpositionDto[j],
									flrangebotposition.getN_menge(),
									flrangebotposition.getEinheit_c_nr(),
									theClientDto, cEinrueckung));
						} else {
							// jeder Artikel koennte selbst wieder ein
							// Stueckliste mit Unterpositionen sein
							StuecklisteInfoDto stuecklisteInfoDto = getStuecklisteFac()
									.getStrukturdatenEinesArtikels(
											aAgstklpositionDto[j]
													.getArtikelIId(),
											false, null, // in die Rekursion mit
											// einer
											// leeren Listen einsteigen
											0, // in die Rekursion mit Ebene 0
												// einsteigen
											iStuecklisteaufloesungTiefe, // alle
											// Stuecklisten
											// lt
											// .
											// Parameter
											// aufloesen
											true, // Basis sind die n Einheiten
											// der AGSTKL
											flrangebotposition
													.getN_menge()
													.multiply(
															aAgstklpositionDto[j]
																	.getNMenge()), // Basismenge
											false, // Fremdfertigung nicht
											// aufloesen
											theClientDto);

							// wenn die Identposition selbst eine echte STKL ist
							if (stuecklisteInfoDto.getIiAnzahlPositionen()
									.intValue() > 0
									&& (!Helper
											.short2boolean(stuecklisteInfoDto
													.getBIstFremdfertigung()) || iStuecklisteaufloesungTiefe != 0)) {
								alDaten.add(befuelleZeileVKMitIdentMitSTKLAusAGSTKLPosition(
										aAgstklpositionDto[j],
										flrangebotposition.getN_menge(),
										flrangebotposition.getEinheit_c_nr(),
										angebotDto.getTRealisierungstermin(),
										theClientDto, cEinrueckung));
								ArrayList<?> alStuecklisteAufgeloest = stuecklisteInfoDto
										.getAlStuecklisteAufgeloest();
								Iterator<?> it = alStuecklisteAufgeloest
										.iterator();

								while (it.hasNext()) {
									StuecklisteMitStrukturDto stuktDto = (StuecklisteMitStrukturDto) it
											.next();

									alDaten.add(befuelleZeileVorkalkulationMitStuecklistenposition(
											flrangebotposition.getAgstkl_i_id(),
											cEinrueckung,
											stuktDto,
											flrangebotposition
													.getFlrangebot()
													.getWaehrung_c_nr_angebotswaehrung(),
											aAgstklpositionDto[j].getNMenge(),
											aAgstklpositionDto[j]
													.getEinheitCNr(),
											iStuecklisteaufloesungTiefe,
											bGestpreisberechnungHauptlager,
											angebotDto
													.getTRealisierungstermin(),
											theClientDto));
								}

							} else {
								alDaten.add(befuelleZeileVKMitIdentOhneSTKLAusAGSTKLPosition(
										aAgstklpositionDto[j],
										flrangebotposition.getN_menge(),
										flrangebotposition.getEinheit_c_nr(),
										theClientDto,
										angebotDto.getTRealisierungstermin(),
										cEinrueckung));
							}
						}
					}

				}
			}
			data = new Object[alDaten.size()][AngebotReportFac.REPORT_ANGEBOT_ANZAHL_SPALTEN];
			data = (Object[][]) alDaten.toArray(data);

			// Erstellung des Report
			HashMap<String, Object> parameter = new HashMap<String, Object>();

			// es gilt das Locale des Benutzers
			Locale locDruck = theClientDto.getLocUi();

			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					angebotDto.getKundeIIdAngebotsadresse(), theClientDto);

			AnsprechpartnerDto ansprechpartnerDto = null;

			if (angebotDto.getAnsprechpartnerIIdKunde() != null) {
				ansprechpartnerDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(
								angebotDto.getAnsprechpartnerIIdKunde(),
								theClientDto);
			}

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);

			parameter.put(
					LPReport.P_ADRESSBLOCK,
					formatAdresseFuerAusdruck(kundeDto.getPartnerDto(),
							ansprechpartnerDto, mandantDto, locDruck));
			parameter.put("P_ANGEBOTNUMMER", angebotDto.getCNr());

			parameter.put("P_KOMMENTAR_INTERN",
					angebotDto.getXInternerkommentar());
			parameter.put("P_KOMMENTAR_EXTERN",
					angebotDto.getXExternerkommentar());

			parameter.put("P_ANGEBOTCBEZ",
					angebotDto.getCBez() != null ? angebotDto.getCBez() : "");
			parameter.put(
					"P_LIEFERART",
					getLocaleFac().lieferartFindByIIdLocaleOhneExc(
							angebotDto.getLieferartIId(), locDruck,
							theClientDto));

			// Lieferzeit in hinterlegter Einheit
			Integer iiLieferzeit = getAngebotServiceFac()
					.getLieferzeitInAngeboteinheit(angebotDto.getIId(),
							angebotDto.getAngeboteinheitCNr(), theClientDto);

			parameter.put(
					"P_ANGEBOTEINHEIT",
					getSystemFac().formatEinheit(
							angebotDto.getAngeboteinheitCNr(), locDruck,
							theClientDto));

			parameter.put("P_LIEFERZEIT", iiLieferzeit);
			parameter.put("P_BELEGWAEHRUNG", angebotDto.getWaehrungCNr());
			parameter.put("P_MANDANTENWAEHRUNG",
					theClientDto.getSMandantenwaehrung());
			parameter.put("P_WECHSELKURSMANDANTZUBELEG",
					angebotDto.getFWechselkursmandantwaehrungzubelegwaehrung());

			// das Zahlungsziel
			parameter.put(
					"P_ZAHLUNGSZIEL",
					getMandantFac().zahlungszielFindByIIdLocaleOhneExc(
							angebotDto.getZahlungszielIId(), locDruck,
							theClientDto));
			parameter.put("P_BELEGDATUM", angebotDto.getTBelegdatum());
			parameter.put("P_ANGEBOTSGUELTIGKEIT",
					angebotDto.getTAngebotsgueltigkeitbis());

			parameter.put("P_ALLGEMEINERRABATT",
					angebotDto.getFAllgemeinerRabattsatz());
			parameter.put("P_ALLGEMEINERRABATT_STRING", Helper.formatZahl(
					angebotDto.getFAllgemeinerRabattsatz(), locDruck));
			parameter.put("P_PROJEKT_RABATT",
					angebotDto.getFProjektierungsrabattsatz());
			parameter.put("P_PROJEKT_RABATT_STRING", Helper.formatZahl(
					angebotDto.getFProjektierungsrabattsatz(), locDruck));
			parameter.put("P_VERSTECKTER_AUFSCHLAG",
					angebotDto.getFVersteckterAufschlag());
			parameter.put("P_VERSTECKTER_AUFSCHLAG_STRING", Helper.formatZahl(
					angebotDto.getFVersteckterAufschlag(), locDruck));
			parameter.put("P_REALISIERUNGSTERMIN",
					angebotDto.getTRealisierungstermin());

			initJRDS(parameter, AngebotReportFac.REPORT_MODUL,
					AngebotReportFac.REPORT_VORKALKULATION,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);

			oPrint = getReportPrint();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} finally {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE, he);
			}
		}

		return oPrint;
	}

	public JasperPrintLP printAdressetikett(Integer partnerIId,
			Integer ansprechpartnerIId, TheClientDto theClientDto) {

		cAktuellerReport = AngebotReportFac.REPORT_ANGEBOT_ADRESSETIKETT;
		data = new Object[1][9];

		try {
			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(
					partnerIId, theClientDto);
			String sLocalePartner = partnerDto.getLocaleCNrKommunikation();
			data[0][AngebotReportFac.REPORT_ADRESSETIKETT_NAME] = getPartnerFac()
					.formatFixAnredeTitelName2Name1(partnerDto,
							Helper.string2Locale(sLocalePartner), theClientDto);
			data[0][AngebotReportFac.REPORT_ADRESSETIKETT_STRASSE] = partnerDto
					.getCStrasse();

			if (partnerDto.getLandplzortDto() != null) {
				data[0][AngebotReportFac.REPORT_ADRESSETIKETT_PLZORT] = partnerDto
						.getLandplzortDto().formatPlzOrt();
				data[0][AngebotReportFac.REPORT_ADRESSETIKETT_LAND] = partnerDto
						.getLandplzortDto().getLandDto().getCName();
			}

			if (ansprechpartnerIId != null) {
				AnsprechpartnerDto ansprechpartnerDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(ansprechpartnerIId,
								theClientDto);
				data[0][AngebotReportFac.REPORT_ADRESSETIKETT_ZHD_NAME] = getPartnerFac()
						.formatFixAnredeTitelName2Name1(
								ansprechpartnerDto.getPartnerDto(),
								Helper.string2Locale(sLocalePartner),
								theClientDto);
			}

			PartnerDto partnerMandantDto = getMandantFac()
					.mandantFindByPrimaryKey(theClientDto.getMandant(),
							theClientDto).getPartnerDto();

			data[0][AngebotReportFac.REPORT_ADRESSETIKETT_ABSENDER_NAME] = partnerMandantDto
					.formatTitelAnrede();
			data[0][AngebotReportFac.REPORT_ADRESSETIKETT_ABSENDER_STRASSE] = partnerMandantDto
					.getCStrasse();

			if (partnerMandantDto.getLandplzortDto() != null) {
				data[0][AngebotReportFac.REPORT_ADRESSETIKETT_ABSENDER_PLZORT] = partnerMandantDto
						.getLandplzortDto().formatPlzOrt();
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		HashMap<String, Object> mapParameter = new HashMap<String, Object>();

		initJRDS(mapParameter, AngebotReportFac.REPORT_MODUL,
				AngebotReportFac.REPORT_ANGEBOT_ADRESSETIKETT,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	/**
	 * Eine Zeile der AG Vorkalkulation mit artikelCNr, CBez, CZBez befuellen. <br>
	 * Die Felder muessen getrennt sein, damit sie im Druck unterschiedlich
	 * formatiert werden koennen.
	 * 
	 * @param aDataI
	 *            Object[][] das Array der zu druckenden Daten
	 * @param iZeileI
	 *            in welche Zeile wird die Information geschrieben
	 * @param artikelDtoI
	 *            der anzudruckende Artikel
	 * @param cPraefixArtikelCNrI
	 *            die Einrueckung fuer Stuecklistenpositionen
	 * @param cBezUebersteuertI
	 *            die Artikelbezeichnung kann uebersteuert sein
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Object[][] das Array der zu druckenden Daten
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileVorkalkulationMitArtikelCNrCBezCZBez(
			Object[] aDataI, ArtikelDto artikelDtoI,
			String cPraefixArtikelCNrI, String cBezUebersteuertI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (artikelDtoI.getArtikelartCNr().equals(
				ArtikelFac.ARTIKELART_HANDARTIKEL)) {
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_IDENT] = cPraefixArtikelCNrI
					+ AngebotReportFac.REPORT_VORKALKULATION_ZEICHEN_FUER_HANDEINGABE;
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_BEZEICHNUNG] = artikelDtoI
					.getArtikelsprDto().getCBez();
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_ZUSATZBEZEICHNUNG] = artikelDtoI
					.getArtikelsprDto().getCZbez();
		} else {
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_IDENT] = cPraefixArtikelCNrI
					+ artikelDtoI.getCNr();

			// das Andrucken der Bezeichnungen erfolgt in getrennten Feldern:
			// - wenn die Bezeichnung uebersteuert wurde, dann die uebersteuerte
			// Bezeichnung
			// - sonst die Bezeichnung aus dem Artikel in der Sprache des
			// Benutzers
			// - wenn es eine gibt, dann auch die Zusatzbezeichnung
			String cBez = artikelDtoI.getArtikelsprDto().getCBez();

			if (cBezUebersteuertI != null) {
				cBez = cBezUebersteuertI;
			}

			aDataI[AngebotReportFac.REPORT_VORKALKULATION_BEZEICHNUNG] = cBez;
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_ZUSATZBEZEICHNUNG] = artikelDtoI
					.getArtikelsprDto().getCZbez();
		}

		return aDataI;
	}

	/**
	 * Eine Zeile der AG Vorkalkulation mit der Stuecklisteninformation
	 * befuellen.
	 * 
	 * @param aDataI
	 *            Object[][] das Array der zu druckenden Daten
	 * @param iZeileI
	 *            in welche Zeile wird die Information geschrieben
	 * @param indexGruppeIIdI
	 *            zu welcher Gruppe werden die Werte addiert
	 * @param cPraefixArtikelCNrI
	 *            Praefix fuer die Einrueckung der artikelCNr
	 * @param stuecklisteMitStrukturDtoI
	 *            die anzudruckende Stuecklistenposition
	 * @param waehrungCNrI
	 *            die gewuenschte Waehrung
	 * @param nUebergeordneteMengeI
	 *            die Menge der Basisposition zum Andrucken der Zwischensumme
	 * @param cNrUebergeordneteEinheitI
	 *            die EInheit der Basisposition zum Andrucken der Zwischensumme
	 * @param iStuecklistenAufloesungTiefeI
	 *            bis in welche Tiefe werden die STKL Position aufgeloest
	 * @param bHauptlager
	 *            boolean
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Object[][] das Array der zu druckenden Daten
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileVorkalkulationMitStuecklistenposition(
			Integer indexGruppeIIdI, String cPraefixArtikelCNrI,
			StuecklisteMitStrukturDto stuecklisteMitStrukturDtoI,
			String waehrungCNrI, BigDecimal nUebergeordneteMengeI,
			String cNrUebergeordneteEinheitI,
			int iStuecklistenAufloesungTiefeI, boolean bHauptlager,
			Timestamp tRealisierungstermin, TheClientDto theClientDto)
			throws EJBExceptionLP {

		Object[] aDataI = new Object[AngebotReportFac.REPORT_VORKALKULATION_ANZAHL_SPALTEN];
		try {

			// Pro Position eine kuenstliche Zeile zum Andrucken erzeugen,
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_STUECKLISTENPOSITION;

			StuecklistepositionDto stuecklistepositionDto = stuecklisteMitStrukturDtoI
					.getStuecklistepositionDto();

			ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(
							stuecklistepositionDto.getArtikelIId(),
							theClientDto);

			// Einrueckung 1x fuer Stuecklistenposition der STKL plus die Ebene
			cPraefixArtikelCNrI += "    ";

			for (int j = 0; j < stuecklisteMitStrukturDtoI.getIEbene(); j++) {
				cPraefixArtikelCNrI = cPraefixArtikelCNrI + "    ";
			}

			aDataI = befuelleZeileVorkalkulationMitArtikelCNrCBezCZBez(aDataI,
					artikelDto, cPraefixArtikelCNrI, null, // keine
					// Uebersteuerung
					// der cBez
					// des
					// Artikels
					theClientDto);

			// es wurde die Gesamtmenge der Unterpositionen in der STKL
			// berechnet
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE] = stuecklistepositionDto
					.getNMenge();
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT] = stuecklistepositionDto
					.getEinheitCNr();
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE_UEBERGEORDNET] = nUebergeordneteMengeI;
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT_UEBERGEORDNET] = cNrUebergeordneteEinheitI;
			if (tRealisierungstermin != null) {
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_FIKTIVER_LAGERSTAND] = getInternebestellungFac()
						.getFiktivenLagerstandZuZeitpunkt(artikelDto,
								theClientDto, tRealisierungstermin);
			} else {
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_FIKTIVER_LAGERSTAND] = null;
			}

			// die Gestehungspreise duerfen nur in der ersten Ebene nach der
			// STKL angedruckt werden,
			// sonst werden Unterpositionen mehrfach bewertet!!!
			// if (stuecklisteMitStrukturDtoI.getIEbene() == 0) {}

			if (artikelDto.getArtikelartCNr().equals(
					ArtikelFac.ARTIKELART_HANDARTIKEL)) {
				// WH xx.03.06 Handeingaben in STKL haben keinen Gestehungspreis
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS] = new BigDecimal(
						0);
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT] = new BigDecimal(
						0);
			} else {
				StuecklisteInfoDto stuecklisteInfoDto = getStuecklisteFac()
						.getStrukturdatenEinesArtikels(artikelDto.getIId(),
								false, null, // in die Rekursion mit einer
								// leeren Listen einsteigen
								0, // in die Rekursion mit Ebene 0 einsteigen
								-1, // alle Stuecklisten komplett aufloesen
								true, // Basis sind die Einheiten der
								// Stueckliste
								stuecklistepositionDto.getNMenge(), // Basis
								// sind n
								// Einheiten
								// der
								// Stueckliste
								false, // Fremdfertigung nicht aufloesen
								theClientDto);

				// IMS 1802 Die Gestehungspreise einer STKL Position werden
				// angedruckt, wenn
				// - die Position keine STKL ist
				// - die Position keine echte STKL ist, d.h. sie ist eine
				// Fremdfertigung oder hat keine Positionen
				if (stuecklisteInfoDto.getIiAnzahlPositionen().intValue() == 0
						|| Helper.short2boolean(stuecklisteInfoDto
								.getBIstFremdfertigung())
						|| stuecklisteMitStrukturDtoI.getIEbene() < iStuecklistenAufloesungTiefeI) {
					// das Hauptlager des Mandanten bestimmen
					LagerDto hauptlager = getLagerFac()
							.getHauptlagerDesMandanten(theClientDto);

					// Grundlage ist der Gestehungspreis des Artikels am
					// Hauptlager des Mandanten
					// CK 23.06.2006: ODER Gemittelter Gestpreis aller Laeger (je
					// nach Mandanteparameter
					// GESTPREISBERECHNUNG_HAUPTLAGER)
					BigDecimal gestehungspreisInMandantenwaehrung = null;
					if (bHauptlager == true) {
						gestehungspreisInMandantenwaehrung = getLagerFac()
								.getGemittelterGestehungspreisEinesLagers(
										stuecklistepositionDto.getArtikelIId(),
										hauptlager.getIId(), theClientDto);
					} else {
						gestehungspreisInMandantenwaehrung = getLagerFac()
								.getGemittelterGestehungspreisAllerLaegerEinesMandanten(
										stuecklistepositionDto.getArtikelIId(),
										theClientDto);

					}
					// Umrechnen in Belegwaehrung
					BigDecimal gestehungspreisInBelegwaehrung = getLocaleFac()
							.rechneUmInAndereWaehrungZuDatum(
									gestehungspreisInMandantenwaehrung,
									theClientDto.getSMandantenwaehrung(),
									waehrungCNrI,
									new Date(System.currentTimeMillis()),
									theClientDto);

					if (stuecklisteInfoDto.getIiAnzahlPositionen().intValue() == 0
							|| stuecklisteMitStrukturDtoI.isBFremdfertigung()
							|| !stuecklisteMitStrukturDtoI.isBStueckliste()) {
						aDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS] = gestehungspreisInBelegwaehrung;
						aDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT] = gestehungspreisInBelegwaehrung
								.multiply(stuecklistepositionDto.getNMenge());
					}
				}

				try {
					ArtikellieferantDto artikellieferantDto = getArtikelFac()
							.getArtikelEinkaufspreis(artikelDto.getIId(),
									new BigDecimal(1), waehrungCNrI,
									theClientDto);

					if (artikellieferantDto != null) {

						if (artikellieferantDto.getLieferantIId() != null) {
							LieferantDto liefDto = getLieferantFac()
									.lieferantFindByPrimaryKey(
											artikellieferantDto
													.getLieferantIId(),
											theClientDto);

							aDataI[REPORT_VORKALKULATION_LIEFERANT] = liefDto
									.getPartnerDto().formatFixName1Name2();

							if (liefDto.getNTransportkostenprolieferung() != null) {
								aDataI[REPORT_VORKALKULATION_TPKOSTENLIEFERUNG] = getLocaleFac()
										.rechneUmInAndereWaehrungZuDatumOhneExc(
												liefDto.getNTransportkostenprolieferung(),
												liefDto.getWaehrungCNr(),
												waehrungCNrI,
												new Date(System
														.currentTimeMillis()),
												theClientDto);
							}

							if (artikellieferantDto != null
									&& artikellieferantDto.getLief1Preis() != null) {

								aDataI[REPORT_VORKALKULATION_LIEF1PREIS] = artikellieferantDto
										.getLief1Preis();
								aDataI[REPORT_VORKALKULATION_LIEF1PREISGUELTIGBIS] = artikellieferantDto
										.getTPreisgueltigbis();

							}

							artikellieferantDto = getArtikelFac()
									.getArtikelEinkaufspreis(
											artikelDto.getIId(),
											stuecklistepositionDto.getNMenge(),
											waehrungCNrI, theClientDto);
							if (artikellieferantDto != null
									&& artikellieferantDto.getLief1Preis() != null) {
								aDataI[REPORT_VORKALKULATION_LIEF1PREISSTAFFEL] = artikellieferantDto
										.getLief1Preis();

							}

						}

					}

				} catch (RemoteException ex1) {
					throwEJBExceptionLPRespectOld(ex1);
				}

			}

			aDataI[AngebotReportFac.REPORT_VORKALKULATION_INDEX_GRUPPE] = indexGruppeIIdI; // fuer
			// die
			// Bildung
			// der
			// Zwischensumme
			// !
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return aDataI;
	}

	private Object[] befuelleZeileVorkalkulationMitStuecklistearbeitsplan(
			Integer indexGruppeIIdI, String cPraefixArtikelCNrI,
			StuecklistearbeitsplanDto stuecklistearbeitsplanDto,
			String waehrungCNrI, BigDecimal nUebergeordneteMengeI,
			String cNrUebergeordneteEinheitI,
			int iStuecklistenAufloesungTiefeI, boolean bHauptlager,
			Timestamp tRealisierungstermin, StuecklisteDto stklDto,
			TheClientDto theClientDto, boolean bMaschine) throws EJBExceptionLP {

		Object[] aDataI = new Object[AngebotReportFac.REPORT_VORKALKULATION_ANZAHL_SPALTEN];
		try {

			// Pro Position eine kuenstliche Zeile zum Andrucken erzeugen,
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_STUECKLISTENPOSITION;

			ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(
							stuecklistearbeitsplanDto.getArtikelIId(),
							theClientDto);

			if (bMaschine == true
					&& stuecklistearbeitsplanDto.getMaschineIId() != null) {

				MaschineDto maschineDto = getZeiterfassungFac()
						.maschineFindByPrimaryKey(
								stuecklistearbeitsplanDto.getMaschineIId());
				if (artikelDto.getArtikelsprDto() != null) {
					artikelDto.getArtikelsprDto()
							.setCBez(maschineDto.getCBez());
				}
				artikelDto.setCNr("M:" + maschineDto.getCInventarnummer());
			}

			cPraefixArtikelCNrI += "    ";
			aDataI = befuelleZeileVorkalkulationMitArtikelCNrCBezCZBez(aDataI,
					artikelDto, cPraefixArtikelCNrI, null, // keine
					// Uebersteuerung
					// der cBez
					// des
					// Artikels
					theClientDto);

			// es wurde die Gesamtmenge der Unterpositionen in der STKL
			// berechnet
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE] = stuecklistearbeitsplanDto
					.getNMenge();
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT] = stuecklistearbeitsplanDto
					.getEinheitCNr();
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE_UEBERGEORDNET] = nUebergeordneteMengeI;
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT_UEBERGEORDNET] = cNrUebergeordneteEinheitI;
			if (tRealisierungstermin != null) {
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_FIKTIVER_LAGERSTAND] = getInternebestellungFac()
						.getFiktivenLagerstandZuZeitpunkt(artikelDto,
								theClientDto, tRealisierungstermin);
			} else {
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_FIKTIVER_LAGERSTAND] = null;
			}

			// die Gestehungspreise duerfen nur in der ersten Ebene nach der
			// STKL angedruckt werden,
			// sonst werden Unterpositionen mehrfach bewertet!!!
			// if (stuecklisteMitStrukturDtoI.getIEbene() == 0) {}

			if (artikelDto.getArtikelartCNr().equals(
					ArtikelFac.ARTIKELART_HANDARTIKEL)) {
				// WH xx.03.06 Handeingaben in STKL haben keinen Gestehungspreis
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS] = new BigDecimal(
						0);
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT] = new BigDecimal(
						0);
			} else {

				// das Hauptlager des Mandanten bestimmen
				LagerDto hauptlager = getLagerFac().getHauptlagerDesMandanten(
						theClientDto);

				// Grundlage ist der Gestehungspreis des Artikels am
				// Hauptlager des Mandanten
				// CK 23.06.2006: ODER Gemittelter Gestpreis aller Laeger (je
				// nach Mandanteparameter
				// GESTPREISBERECHNUNG_HAUPTLAGER)
				BigDecimal gestehungspreisInMandantenwaehrung = null;
				if (bMaschine == true
						&& stuecklistearbeitsplanDto.getMaschineIId() != null) {
					gestehungspreisInMandantenwaehrung = getZeiterfassungFac()
							.getMaschinenKostenZumZeitpunkt(
									stuecklistearbeitsplanDto.getMaschineIId(),
									new java.sql.Timestamp(System
											.currentTimeMillis()));
				} else {
					if (bHauptlager == true) {
						gestehungspreisInMandantenwaehrung = getLagerFac()
								.getGemittelterGestehungspreisEinesLagers(
										stuecklistearbeitsplanDto
												.getArtikelIId(),
										hauptlager.getIId(), theClientDto);
					} else {
						gestehungspreisInMandantenwaehrung = getLagerFac()
								.getGemittelterGestehungspreisAllerLaegerEinesMandanten(
										stuecklistearbeitsplanDto
												.getArtikelIId(),
										theClientDto);

					}
				}
				// Umrechnen in Belegwaehrung
				BigDecimal gestehungspreisInBelegwaehrung = getLocaleFac()
						.rechneUmInAndereWaehrungZuDatum(
								gestehungspreisInMandantenwaehrung,
								theClientDto.getSMandantenwaehrung(),
								waehrungCNrI,
								new Date(System.currentTimeMillis()),
								theClientDto);

				BigDecimal bdGesamtzeitInStunden = Helper
						.berechneGesamtzeitInStunden(
								stuecklistearbeitsplanDto.getLRuestzeit(),
								stuecklistearbeitsplanDto.getLStueckzeit(),
								nUebergeordneteMengeI, null,
								stuecklistearbeitsplanDto.getIAufspannung());
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE] = bdGesamtzeitInStunden;
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS] = gestehungspreisInBelegwaehrung;
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT] = gestehungspreisInBelegwaehrung
						.multiply(bdGesamtzeitInStunden);

				try {
					ArtikellieferantDto artikellieferantDto = getArtikelFac()
							.getArtikelEinkaufspreis(artikelDto.getIId(),
									new BigDecimal(1), waehrungCNrI,
									theClientDto);

					if (artikellieferantDto != null) {

						if (artikellieferantDto.getLieferantIId() != null) {
							LieferantDto liefDto = getLieferantFac()
									.lieferantFindByPrimaryKey(
											artikellieferantDto
													.getLieferantIId(),
											theClientDto);

							aDataI[REPORT_VORKALKULATION_LIEFERANT] = liefDto
									.getPartnerDto().formatFixName1Name2();

							if (liefDto.getNTransportkostenprolieferung() != null) {
								aDataI[REPORT_VORKALKULATION_TPKOSTENLIEFERUNG] = getLocaleFac()
										.rechneUmInAndereWaehrungZuDatumOhneExc(
												liefDto.getNTransportkostenprolieferung(),
												liefDto.getWaehrungCNr(),
												waehrungCNrI,
												new Date(System
														.currentTimeMillis()),
												theClientDto);
							}

							if (artikellieferantDto != null
									&& artikellieferantDto.getLief1Preis() != null) {

								aDataI[REPORT_VORKALKULATION_LIEF1PREIS] = artikellieferantDto
										.getLief1Preis();
								aDataI[REPORT_VORKALKULATION_LIEF1PREISGUELTIGBIS] = artikellieferantDto
										.getTPreisgueltigbis();

							}

							artikellieferantDto = getArtikelFac()
									.getArtikelEinkaufspreis(
											artikelDto.getIId(),
											bdGesamtzeitInStunden,
											waehrungCNrI, theClientDto);
							if (artikellieferantDto != null
									&& artikellieferantDto.getLief1Preis() != null) {
								aDataI[REPORT_VORKALKULATION_LIEF1PREISSTAFFEL] = artikellieferantDto
										.getLief1Preis();

							}

						}

					}

				} catch (RemoteException ex1) {
					throwEJBExceptionLPRespectOld(ex1);
				}

			}

			aDataI[AngebotReportFac.REPORT_VORKALKULATION_INDEX_GRUPPE] = indexGruppeIIdI; // fuer
			// die
			// Bildung
			// der
			// Zwischensumme
			// !
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return aDataI;
	}

	/**
	 * Eine Zeile der VK mit einer Handposition aus einer AGSTKL befuellen.
	 * 
	 * @param aaDataI
	 *            Object[][] das Array der zu druckenden Daten
	 * @param iZeileI
	 *            die aktuelle Zeile, die zu befuellen ist
	 * @param agstklpositionDtoI
	 *            die zu druckende AGSTKL Position
	 * @param nUebergeordneteMengeI
	 *            die Menge der Basisposition zum Andrucken der Zwischensumme
	 * @param cNrUebergeordneteEinheitI
	 *            die EInheit der Basisposition zum Andrucken der Zwischensumme
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @param cPraefixArtikelCNrI
	 *            die Einrueckung fuer die Unterstuecklisten
	 * @return Object[][] das Array der zu druckenden Daten
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileVKMitHandAusAGSTKLPosition(
			AgstklpositionDto agstklpositionDtoI,
			BigDecimal nUebergeordneteMengeI, String cNrUebergeordneteEinheitI,
			TheClientDto theClientDto, String cPraefixArtikelCNrI)
			throws EJBExceptionLP {
		Object[] aaDataI = new Object[AngebotReportFac.REPORT_VORKALKULATION_ANZAHL_SPALTEN];

		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
				agstklpositionDtoI.getArtikelIId(), theClientDto);

		BigDecimal nAnzudruckendeMenge = agstklpositionDtoI.getNMenge(); // .
		// multiply
		// (
		// nUebergeordneteMengeI
		// )
		// ;

		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_IDENT] = cPraefixArtikelCNrI
				+ AngebotReportFac.REPORT_VORKALKULATION_ZEICHEN_FUER_HANDEINGABE;
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_BEZEICHNUNG] = artikelDto
				.getArtikelsprDto().getCBez();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_ZUSATZBEZEICHNUNG] = artikelDto
				.getArtikelsprDto().getCZbez();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE] = nAnzudruckendeMenge;
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT] = agstklpositionDtoI
				.getEinheitCNr();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE_UEBERGEORDNET] = nUebergeordneteMengeI;
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT_UEBERGEORDNET] = cNrUebergeordneteEinheitI;
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS] = agstklpositionDtoI
				.getNGestehungspreis();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT] = agstklpositionDtoI
				.getNGestehungspreis().multiply(nUebergeordneteMengeI)
				.multiply(agstklpositionDtoI.getNMenge());
		// in einer AGSTKL gilt der Verkaufswert der AGSTKL
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_HANDEINGABE;
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_INDEX_GRUPPE] = agstklpositionDtoI
				.getAgstklIId(); // fuer die Bildung der Zwischensumme!

		return aaDataI;
	}

	/**
	 * Eine Zeile der VK mit einer Identposition/STKL aus einer AGSTKL
	 * befuellen.
	 * 
	 * @param aaDataI
	 *            Object[][] das Array der zu druckenden Daten
	 * @param iZeileI
	 *            die aktuelle Zeile, die zu befuellen ist
	 * @param agstklpositionDtoI
	 *            die zu druckende AGSTKL Position
	 * @param nUebergeordneteMengeI
	 *            die Menge der Basisposition zum Andrucken der Zwischensumme
	 * @param cNrUebergeordneteEinheitI
	 *            die EInheit der Basisposition zum Andrucken der Zwischensumme
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @param cPraefixArtikelCNrI
	 *            die Einrueckung fuer die Unterstuecklisten
	 * @return Object[][] das Array der zu druckenden Daten
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileVKMitIdentOhneSTKLAusAGSTKLPosition(
			AgstklpositionDto agstklpositionDtoI,
			BigDecimal nUebergeordneteMengeI, String cNrUebergeordneteEinheitI,
			TheClientDto theClientDto, Timestamp tRealisierungstermin,
			String cPraefixArtikelCNrI) throws EJBExceptionLP {
		Object[] aaDataI = new Object[AngebotReportFac.REPORT_VORKALKULATION_ANZAHL_SPALTEN];

		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
				agstklpositionDtoI.getArtikelIId(), theClientDto);

		BigDecimal nAnzudruckendeMenge = agstklpositionDtoI.getNMenge()
				.multiply(nUebergeordneteMengeI);

		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_IDENT] = cPraefixArtikelCNrI
				+ artikelDto.getCNr();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_BEZEICHNUNG] = artikelDto
				.getArtikelsprDto().getCBez();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_ZUSATZBEZEICHNUNG] = artikelDto
				.getArtikelsprDto().getCZbez();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE] = agstklpositionDtoI
				.getNMenge();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT] = agstklpositionDtoI
				.getEinheitCNr();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE_UEBERGEORDNET] = nUebergeordneteMengeI;
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT_UEBERGEORDNET] = cNrUebergeordneteEinheitI;
		// in einer AGSTKL gilt der Verkaufswert der AGSTKL
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS] = agstklpositionDtoI
				.getNGestehungspreis();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT] = agstklpositionDtoI
				.getNGestehungspreis().multiply(nAnzudruckendeMenge);
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_IDENT;
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_INDEX_GRUPPE] = agstklpositionDtoI
				.getAgstklIId(); // fuer die Bildung der Zwischensumme!
		if (tRealisierungstermin != null) {
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_FIKTIVER_LAGERSTAND] = getInternebestellungFac()
					.getFiktivenLagerstandZuZeitpunkt(artikelDto, theClientDto,
							tRealisierungstermin);
		} else {
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_FIKTIVER_LAGERSTAND] = null;
		}

		return aaDataI;
	}

	/**
	 * Eine Zeile der VK mit einer Identposition aus einer AGSTKL befuellen.
	 * 
	 * @param aaDataI
	 *            Object[][] das Array der zu druckenden Daten
	 * @param iZeileI
	 *            die aktuelle Zeile, die zu befuellen ist
	 * @param agstklpositionDtoI
	 *            die zu druckende AGSTKL Position
	 * @param nUebergeordneteMengeI
	 *            die Menge der Basisposition zum Andrucken der Zwischensumme
	 * @param cNrUebergeordneteEinheitI
	 *            die EInheit der Basisposition zum Andrucken der Zwischensumme
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @param cPraefixArtikelCNrI
	 *            die Einrueckung fuer die Unterstuecklisten
	 * @return Object[][] das Array der zu druckenden Daten
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileVKMitIdentMitSTKLAusAGSTKLPosition(
			AgstklpositionDto agstklpositionDtoI,
			BigDecimal nUebergeordneteMengeI, String cNrUebergeordneteEinheitI,
			Timestamp tRealisierungstermin, TheClientDto theClientDto,
			String cPraefixArtikelCNrI) throws EJBExceptionLP {
		Object[] aaDataI = new Object[AngebotReportFac.REPORT_VORKALKULATION_ANZAHL_SPALTEN];

		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
				agstklpositionDtoI.getArtikelIId(), theClientDto);

		BigDecimal nAnzudruckendeMenge = agstklpositionDtoI.getNMenge()
				.multiply(nUebergeordneteMengeI);
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_IDENT] = cPraefixArtikelCNrI
				+ artikelDto.getCNr();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_BEZEICHNUNG] = artikelDto
				.getArtikelsprDto().getCBez();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_ZUSATZBEZEICHNUNG] = artikelDto
				.getArtikelsprDto().getCZbez();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE] = nAnzudruckendeMenge;
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT] = agstklpositionDtoI
				.getEinheitCNr();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE_UEBERGEORDNET] = nUebergeordneteMengeI;
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT_UEBERGEORDNET] = cNrUebergeordneteEinheitI;
		// in einer AGSTKL gilt der Verkaufswert der AGSTKL
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS_MANUELL] = formatBigDecimalAsInfo(agstklpositionDtoI
				.getNGestehungspreis());
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT_MANUELL] = formatBigDecimalAsInfo(agstklpositionDtoI
				.getNGestehungspreis().multiply(nAnzudruckendeMenge));
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_IDENT;
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_INDEX_GRUPPE] = agstklpositionDtoI
				.getAgstklIId(); // fuer die Bildung der Zwischensumme!
		if (tRealisierungstermin != null) {
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_FIKTIVER_LAGERSTAND] = getInternebestellungFac()
					.getFiktivenLagerstandZuZeitpunkt(artikelDto, theClientDto,
							tRealisierungstermin);
		} else {
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_FIKTIVER_LAGERSTAND] = null;
		}

		return aaDataI;
	}

	private String formatBigDecimalAsInfo(BigDecimal nZahlI)
			throws EJBExceptionLP {
		StringBuffer buff = new StringBuffer();

		if (nZahlI != null) {
			buff.append("[").append(Helper.rundeKaufmaennisch(nZahlI, 2))
					.append("]");
		}

		return buff.toString();
	}

	/**
	 * Eine Zeile der VK mit einer Handposition befuellen.
	 * 
	 * @param aaDataI
	 *            Object[][] das Array der zu druckenden Daten
	 * @param iZeileI
	 *            die aktuelle Zeile, die zu befuellen ist
	 * @param angebotpositionDtoI
	 *            die zu druckende Angebotposition
	 * @param theClientDto
	 *            der aktuelle Beutzer
	 * @return Object[][] das Array der zu druckenden Daten
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileVKMitAGSTKLPosition(
			FLRAngebotpositionReport angebotpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Object[] aaDataI = new Object[AngebotReportFac.REPORT_VORKALKULATION_ANZAHL_SPALTEN];
		try {
			AgstklDto agstklDto = getAngebotstklFac().agstklFindByPrimaryKey(
					angebotpositionDtoI.getAgstkl_i_id());
			BelegartDto belegartDto = getLocaleFac().belegartFindByCNr(
					LocaleFac.BELEGART_AGSTUECKLISTE);
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_IDENT] = belegartDto
					.getCKurzbezeichnung() + agstklDto.getCNr(); // die
			// Belegnummer
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_BEZEICHNUNG] = angebotpositionDtoI
					.getC_bez();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE] = angebotpositionDtoI
					.getN_menge();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT] = angebotpositionDtoI
					.getEinheit_c_nr();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_VERKAUFSPREIS] = angebotpositionDtoI
					.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte();

			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_VERKAUFSWERT] = angebotpositionDtoI
					.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte()
					.multiply(angebotpositionDtoI.getN_menge());
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_AGSTUECKLISTE;
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_INDEX_GRUPPE] = angebotpositionDtoI
					.getAgstkl_i_id(); // fuer die Bildung der Zwischensumme!
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return aaDataI;
	}

	/**
	 * Eine Zeile der VK mit der Handposition einer AG Position befuellen. <br>
	 * Die Position wird immer in Ebene 0 angedruckt.
	 * 
	 * @param aaDataI
	 *            Object[][] das Array der zu druckenden Daten
	 * @param iZeileI
	 *            die aktuelle Zeile, die zu befuellen ist
	 * @param angebotpositionDtoI
	 *            die zu druckende AG Position
	 * @param theClientDto
	 *            der aktuelle Beutzer
	 * @return Object[][] das Array der zu druckenden Daten
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileVKMitHandAusAngebotposition(
			FLRAngebotpositionReport angebotpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Object[] aaDataI = new Object[AngebotReportFac.REPORT_VORKALKULATION_ANZAHL_SPALTEN];
		try {
			ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(
							angebotpositionDtoI.getArtikel_i_id(), theClientDto);
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_POSITIONSNUMMER] = getAngebotpositionFac()
					.getPositionNummer(angebotpositionDtoI.getI_id());
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_IDENT] = AngebotReportFac.REPORT_VORKALKULATION_ZEICHEN_FUER_HANDEINGABE;
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_BEZEICHNUNG] = artikelDto
					.getArtikelsprDto().getCBez();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_ZUSATZBEZEICHNUNG] = artikelDto
					.getArtikelsprDto().getCZbez();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE] = angebotpositionDtoI
					.getN_menge();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE_UEBERGEORDNET] = angebotpositionDtoI
					.getN_menge();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT] = angebotpositionDtoI
					.getEinheit_c_nr();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS] = angebotpositionDtoI
					.getN_gestehungspreis();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT] = angebotpositionDtoI
					.getN_gestehungspreis().multiply(
							angebotpositionDtoI.getN_menge());
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_VERKAUFSPREIS] = angebotpositionDtoI
					.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_VERKAUFSWERT] = angebotpositionDtoI
					.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte()
					.multiply(angebotpositionDtoI.getN_menge());
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_HANDEINGABE;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return aaDataI;
	}

	/**
	 * Eine Zeile der VK mit der Identposition einer Angebotposition befuellen. <br>
	 * Die Position wird immer in Ebene 0 angedruckt.
	 * 
	 * @param aaDataI
	 *            Object[][] das Array der zu druckenden Daten
	 * @param iZeileI
	 *            die aktuelle Zeile, die zu befuellen ist
	 * @param angebotpositionDtoI
	 *            die zu druckende AG Position
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Object[][] das Array der zu druckenden Daten
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileVKMitIdentOhneSTKLAusAngebotposition(
			FLRAngebotpositionReport angebotpositionDtoI,
			Timestamp tRealisierungstermin, TheClientDto theClientDto,
			String angebotswaehrung) throws EJBExceptionLP {
		Object[] aaDataI = new Object[AngebotReportFac.REPORT_VORKALKULATION_ANZAHL_SPALTEN];
		try {
			ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(
							angebotpositionDtoI.getArtikel_i_id(), theClientDto);

			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_POSITIONSNUMMER] = getAngebotpositionFac()
					.getPositionNummer(angebotpositionDtoI.getI_id());

			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_IDENT] = artikelDto
					.getCNr();
			
			
			// Typ, wenn Setartikel

			if (angebotpositionDtoI.getPosition_i_id_artikelset() != null) {

				aaDataI[AngebotReportFac.REPORT_VORKALKULATION_SETARTIKEL_TYP] = ArtikelFac.SETARTIKEL_TYP_POSITION;

			} else {

				Session session = null;
				SessionFactory factory = FLRSessionFactory.getFactory();
				session = factory.openSession();
				Criteria crit = session
						.createCriteria(FLRAngebotposition.class);
				crit.add(Restrictions.eq("position_i_id_artikelset",
						angebotpositionDtoI.getI_id()));

				int iZeilen = crit.list().size();

				if (iZeilen > 0) {
					aaDataI[AngebotReportFac.REPORT_VORKALKULATION_SETARTIKEL_TYP] = ArtikelFac.SETARTIKEL_TYP_KOPF;
				}
				session.close();

			}
			
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_BEZEICHNUNG] = artikelDto
					.getArtikelsprDto().getCBez();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_ZUSATZBEZEICHNUNG] = artikelDto
					.getArtikelsprDto().getCZbez();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE] = angebotpositionDtoI
					.getN_menge();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT] = angebotpositionDtoI
					.getEinheit_c_nr();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS] = angebotpositionDtoI
					.getN_gestehungspreis();
			if (angebotpositionDtoI.getN_gestehungspreis() != null) {
				aaDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT] = angebotpositionDtoI
						.getN_gestehungspreis().multiply(
								angebotpositionDtoI.getN_menge());
			}
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_VERKAUFSPREIS] = angebotpositionDtoI
					.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MATERIALZUSCHLAG] = angebotpositionDtoI
					.getN_materialzuschlag();
			if (artikelDto.getMaterialIId() != null) {
				MaterialDto materialDto = getMaterialFac()
						.materialFindByPrimaryKey(artikelDto.getMaterialIId(),
								theClientDto);
				aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MATERIAL] = materialDto
						.getBezeichnung();
			}
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_VERKAUFSWERT] = angebotpositionDtoI
					.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte()
					.multiply(angebotpositionDtoI.getN_menge());
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_IDENT;
			if (tRealisierungstermin != null) {
				aaDataI[AngebotReportFac.REPORT_VORKALKULATION_FIKTIVER_LAGERSTAND] = getInternebestellungFac()
						.getFiktivenLagerstandZuZeitpunkt(artikelDto,
								theClientDto, tRealisierungstermin);
			} else {
				aaDataI[AngebotReportFac.REPORT_VORKALKULATION_FIKTIVER_LAGERSTAND] = null;
			}

			try {
				ArtikellieferantDto artikellieferantDto = getArtikelFac()
						.getArtikelEinkaufspreis(artikelDto.getIId(),
								new BigDecimal(1), angebotswaehrung,
								theClientDto);

				if (artikellieferantDto != null) {

					if (artikellieferantDto.getLieferantIId() != null) {
						LieferantDto liefDto = getLieferantFac()
								.lieferantFindByPrimaryKey(
										artikellieferantDto.getLieferantIId(),
										theClientDto);

						aaDataI[REPORT_VORKALKULATION_LIEFERANT] = liefDto
								.getPartnerDto().formatFixName1Name2();

						if (liefDto.getNTransportkostenprolieferung() != null) {
							aaDataI[REPORT_VORKALKULATION_TPKOSTENLIEFERUNG] = getLocaleFac()
									.rechneUmInAndereWaehrungZuDatumOhneExc(
											liefDto.getNTransportkostenprolieferung(),
											liefDto.getWaehrungCNr(),
											angebotswaehrung,
											new Date(System.currentTimeMillis()),
											theClientDto);
						}

						if (artikellieferantDto != null
								&& artikellieferantDto.getLief1Preis() != null) {

							aaDataI[REPORT_VORKALKULATION_LIEF1PREIS] = artikellieferantDto
									.getLief1Preis();
							aaDataI[REPORT_VORKALKULATION_LIEF1PREISGUELTIGBIS] = artikellieferantDto
									.getTPreisgueltigbis();
						}

						artikellieferantDto = getArtikelFac()
								.getArtikelEinkaufspreis(artikelDto.getIId(),
										angebotpositionDtoI.getN_menge(),
										angebotswaehrung, theClientDto);
						if (artikellieferantDto != null
								&& artikellieferantDto.getLief1Preis() != null) {
							aaDataI[REPORT_VORKALKULATION_LIEF1PREISSTAFFEL] = artikellieferantDto
									.getLief1Preis();

						}

					}

				}

			} catch (RemoteException ex1) {
				throwEJBExceptionLPRespectOld(ex1);
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return aaDataI;
	}

	/**
	 * Eine Zeile der VK mit Angebotposition vom Typ Ident befuellen, wobei der
	 * Artikel eine STKL ist. <br>
	 * Die Position wird immer in Ebene 0 angedruckt.
	 * 
	 * @param aaDataI
	 *            das Array der zu druckenden Daten
	 * @param iZeileI
	 *            die aktuelle Zeile, die zu befuellen ist
	 * @param angebotpositionDtoI
	 *            die zu druckende AG Position
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Object[][] das Array der zu druckenden Daten
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileVKMitIdentMitSTKLAusAngebotposition(
			FLRAngebotpositionReport angebotpositionDtoI,
			Timestamp tRealisierungstermin, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Object[] aaDataI = new Object[AngebotReportFac.REPORT_VORKALKULATION_ANZAHL_SPALTEN];

		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
				angebotpositionDtoI.getArtikel_i_id(), theClientDto);

		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_IDENT] = artikelDto
				.getCNr();
		
		// Typ, wenn Setartikel

		if (angebotpositionDtoI.getPosition_i_id_artikelset() != null) {

			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_SETARTIKEL_TYP] = ArtikelFac.SETARTIKEL_TYP_POSITION;

		} else {

			Session session = null;
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria crit = session
					.createCriteria(FLRAngebotposition.class);
			crit.add(Restrictions.eq("position_i_id_artikelset",
					angebotpositionDtoI.getI_id()));

			int iZeilen = crit.list().size();

			if (iZeilen > 0) {
				aaDataI[AngebotReportFac.REPORT_VORKALKULATION_SETARTIKEL_TYP] = ArtikelFac.SETARTIKEL_TYP_KOPF;
			}
			session.close();

		}
		
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_BEZEICHNUNG] = artikelDto
				.getArtikelsprDto().getCBez();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_ZUSATZBEZEICHNUNG] = artikelDto
				.getArtikelsprDto().getCZbez();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE] = angebotpositionDtoI
				.getN_menge();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT] = angebotpositionDtoI
				.getEinheit_c_nr();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS_MANUELL] = formatBigDecimalAsInfo(angebotpositionDtoI
				.getN_gestehungspreis());
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT_MANUELL] = formatBigDecimalAsInfo(angebotpositionDtoI
				.getN_gestehungspreis().multiply(
						angebotpositionDtoI.getN_menge()));
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_VERKAUFSPREIS] = angebotpositionDtoI
				.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte();

		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MATERIALZUSCHLAG] = angebotpositionDtoI
				.getN_materialzuschlag();
		if (artikelDto.getMaterialIId() != null) {
			try {
				MaterialDto materialDto = getMaterialFac()
						.materialFindByPrimaryKey(artikelDto.getMaterialIId(),
								theClientDto);
				aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MATERIAL] = materialDto
						.getBezeichnung();
			} catch (RemoteException ex1) {
				throwEJBExceptionLPRespectOld(ex1);
			}
		}

		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_VERKAUFSWERT] = angebotpositionDtoI
				.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte()
				.multiply(angebotpositionDtoI.getN_menge());
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_IDENT;
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_INDEX_GRUPPE] = artikelDto
				.getIId();
		if (tRealisierungstermin != null) {
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_FIKTIVER_LAGERSTAND] = getInternebestellungFac()
					.getFiktivenLagerstandZuZeitpunkt(artikelDto, theClientDto,
							tRealisierungstermin);
		} else {
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_FIKTIVER_LAGERSTAND] = null;
		}

		return aaDataI;
	}

	/**
	 * Ueber Hibernate alle berechnungsrelevanten Positionen eines Angebots
	 * holen. <br>
	 * Diese Methode mu&szlig; innerhalb einer offenen Hibernate Session aufgerufen
	 * werden. <br>
	 * 
	 * @todo diese Methode muesste eigentlich in der AngebotpositionFac
	 *       sitzen... PJ 3799
	 * 
	 * @param sessionI
	 *            die Hibernate Session
	 * @param iIdAngebotI
	 *            PK des Angebots
	 * @param bNurMengenbehafteteI
	 *            nur mengenbehaftete Positionen beruecksichtigen
	 * @param bNurPositiveMengenI
	 *            nur positive Mengen beruecksichtigen; wird bei
	 *            !bNurMengenbehafteteI nicht ausgewertet
	 * @param bOhneAlternativenI
	 *            alternative Positionen werden nicht beruecksichtigt
	 * @return FLRAngebotpositionReport[] die berechnungsrelevanten Positionen
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private FLRAngebotpositionReport[] holeAngebotpositionen(Session sessionI,
			Integer iIdAngebotI, boolean bNurMengenbehafteteI,
			boolean bNurPositiveMengenI, boolean bOhneAlternativenI)
			throws EJBExceptionLP {
		FLRAngebotpositionReport[] aFLRAngebotpositionReport = null;

		try {
			Criteria crit = sessionI
					.createCriteria(FLRAngebotpositionReport.class);

			crit.add(Restrictions.eq(
					AngebotpositionFac.FLR_ANGEBOTPOSITION_ANGEBOT_I_ID,
					iIdAngebotI));

			if (bNurMengenbehafteteI) {
				crit.add(Restrictions
						.isNotNull(AngebotpositionFac.FLR_ANGEBOTPOSITION_N_MENGE));

				if (bNurPositiveMengenI) {
					crit.add(Restrictions.gt(
							AngebotpositionFac.FLR_ANGEBOTPOSITION_N_MENGE,
							new BigDecimal(0)));
				}
			}

			// nur Positionen beruecksichtigen, die keine Alternative sind
			if (bOhneAlternativenI) {
				crit.add(Restrictions.eq(
						AngebotpositionFac.FLR_ANGEBOTPOSITION_B_ALTERNATIVE,
						new Short((short) 0)));
			}
			crit.addOrder(Order.asc("i_sort"));

			// Liste aller Positionen, die behandelt werden sollen
			java.util.List<?> list = crit.list();
			aFLRAngebotpositionReport = new FLRAngebotpositionReport[list
					.size()];
			aFLRAngebotpositionReport = (FLRAngebotpositionReport[]) list
					.toArray(aFLRAngebotpositionReport);
		} catch (HibernateException he) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE,
					new Exception(he));
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return aFLRAngebotpositionReport;
	}

	/**
	 * Eine Zeile eines Angebotdrucks mit den Informationen einer
	 * preisbehafteten Position befuellen (Ident, Handeingabe, AGStueckliste).
	 * 
	 * @param flrangebotpositionI
	 *            die preisbehaftete Angebotposition
	 * @param kundeDto
	 *            KundeDto
	 * @param iArtikelpositionsnummerI
	 *            Referenz auf die letzte vergebene Artikelpositionsnummer
	 * @param mwstMapI
	 *            Refernz auf die Liste aller definierten Mwstsaetze
	 * @param bbSeitenumbruchI
	 *            dieses Flag toggelt bei einem benutzerdefinierten
	 *            Seitenumbruch
	 * @param localeCNrI
	 *            das Locale des Drucks
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Object[] die Zeile des Angebotdrucks
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileAGMitPreisbehafteterPosition(
			FLRAngebotpositionReport flrangebotpositionI,
			KundeDto kundeDto,
			int iArtikelpositionsnummerI, // Referenz
			LinkedHashMap<Integer, MwstsatzReportDto> mwstMapI, // Referenz
			Boolean bbSeitenumbruchI, Locale localeCNrI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Object[] dataRow = null;
		try {
			dataRow = new Object[AngebotReportFac.REPORT_ANGEBOT_ANZAHL_SPALTEN];
			// Dto holen damit LPReport.printIdent verwendet werden kann
			AngebotpositionDto angebotpositionDto = getAngebotpositionFac()
					.angebotpositionFindByPrimaryKey(
							flrangebotpositionI.getI_id(), theClientDto);

			dataRow[REPORT_ANGEBOT_INTERNAL_IID] = flrangebotpositionI
					.getI_id();

			// UW 22.03.06 Alternative Positionen erhalten keine
			// Positionsnummer, sie
			// werden im Druck mit dem Text "Option" gekennzeichnet
			dataRow[AngebotReportFac.REPORT_ANGEBOT_B_ALTERNATIVE] = flrangebotpositionI
					.getB_alternative();
			dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITIONOBJEKT] = getSystemReportFac()
					.getPositionForReport(LocaleFac.BELEGART_ANGEBOT,
							flrangebotpositionI.getI_id(), theClientDto);
			// PJ 15926
			if (flrangebotpositionI.getFlrverleih() != null) {
				dataRow[AngebotReportFac.REPORT_ANGEBOT_VERLEIHTAGE] = flrangebotpositionI
						.getFlrverleih().getI_tage();
				dataRow[AngebotReportFac.REPORT_ANGEBOT_VERLEIHFAKTOR] = flrangebotpositionI
						.getFlrverleih().getF_faktor();
			}

			String positionCNr = null;

			if (Helper.short2boolean(flrangebotpositionI.getB_alternative())) {
				positionCNr = AngebotpositionFac.ANGEBOTPOSITION_ALTERNATIVE_TEXT;
			} else {
				positionCNr = getAngebotpositionFac().getPositionNummer(
						flrangebotpositionI.getI_id())
						+ "";
			}

			if (Helper.short2boolean(flrangebotpositionI.getB_alternative())) {
				dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITION_NR] = AngebotpositionFac.ANGEBOTPOSITION_ALTERNATIVE_TEXT;
			} else {
				dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITION_NR] = iArtikelpositionsnummerI
						+ "";
			}

			dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITION] = positionCNr;
			String cBezeichnung = null;
			if (flrangebotpositionI.getAngebotpositionart_c_nr().equals(
					AngebotServiceFac.ANGEBOTPOSITIONART_AGSTUECKLISTE)) {
				AgstklDto agstklDto = getAngebotstklFac()
						.agstklFindByPrimaryKey(
								flrangebotpositionI.getAgstkl_i_id());

				cBezeichnung = agstklDto.getCBez(); // WH: 15.12.05 Wenn es
				// keine Bezeichnung gibt,
				// dann steht da nichts
			} else { // Ident und Handeingabe

				ArtikelDto oArtikelDto = getArtikelFac()
						.artikelFindByPrimaryKey(
								flrangebotpositionI.getArtikel_i_id(),
								theClientDto);
				dataRow[AngebotReportFac.REPORT_ANGEBOT_REFERENZNUMMER] = oArtikelDto
						.getCReferenznr();
				dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_INDEX] = oArtikelDto
						.getCIndex();
				dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_REVISION] = oArtikelDto
						.getCRevision();
				// PJ15260
				ArtikellieferantDto alDto = getArtikelFac()
						.getArtikelEinkaufspreis(oArtikelDto.getIId(),
								new BigDecimal(1), kundeDto.getCWaehrung(),
								theClientDto);
				if (alDto != null && alDto.getZertifikatartIId() != null) {

					ZertifikatartDto zDto = getAnfrageServiceFac()
							.zertifikatartFindByPrimaryKey(
									alDto.getZertifikatartIId());

					dataRow[AngebotReportFac.REPORT_ANGEBOT_ZERTIFIKATART] = zDto
							.getCBez();

				}

				BelegPositionDruckIdentDto druckDto = printIdent(
						angebotpositionDto, LocaleFac.BELEGART_ANGEBOT,
						oArtikelDto, localeCNrI, kundeDto.getPartnerIId(),
						theClientDto);
				cBezeichnung = druckDto.getSArtikelInfo();
				dataRow[AngebotReportFac.REPORT_ANGEBOT_KURZBEZEICHNUNG] = druckDto
						.getSKurzbezeichnung();
				dataRow[AngebotReportFac.REPORT_ANGEBOT_BEZEICHNUNG] = druckDto
						.getSBezeichnung();

				dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKELCZBEZ2] = druckDto
						.getSArtikelZusatzBezeichnung2();
				dataRow[AngebotReportFac.REPORT_ANGEBOT_IMAGE] = druckDto
						.getOImageKommentar();
				dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKELKOMMENTAR] = druckDto
						.getSArtikelkommentar();
				dataRow[AngebotReportFac.REPORT_ANGEBOT_ZUSATZBEZEICHNUNG] = druckDto
						.getSZusatzBezeichnung();

				if (flrangebotpositionI.getAngebotpositionart_c_nr().equals(
						AngebotServiceFac.ANGEBOTPOSITIONART_IDENT)) {
					// Ident nur fuer "echte" Artikel
					dataRow[AngebotReportFac.REPORT_ANGEBOT_IDENTNUMMER] = oArtikelDto
							.getCNr();
					dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_WERBEABGABEPFLICHTIG] = Helper
							.short2Boolean(oArtikelDto
									.getBWerbeabgabepflichtig());

					// Typ, wenn Setartikel

					if (angebotpositionDto.getPositioniIdArtikelset() != null) {

						dataRow[AngebotReportFac.REPORT_ANGEBOT_SETARTIKEL_TYP] = ArtikelFac.SETARTIKEL_TYP_POSITION;

					} else {

						Session session = null;
						SessionFactory factory = FLRSessionFactory.getFactory();
						session = factory.openSession();
						Criteria crit = session
								.createCriteria(FLRAngebotposition.class);
						crit.add(Restrictions.eq("position_i_id_artikelset",
								angebotpositionDto.getIId()));

						int iZeilen = crit.list().size();

						if (iZeilen > 0) {
							dataRow[AngebotReportFac.REPORT_ANGEBOT_SETARTIKEL_TYP] = ArtikelFac.SETARTIKEL_TYP_KOPF;
						}
						session.close();

					}

					// KundeArtikelnr gueltig zu Belegdatum
					KundesokoDto kundeSokoDto_gueltig = this
							.getKundesokoFac()
							.kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc(
									kundeDto.getIId(),
									oArtikelDto.getIId(),
									new java.sql.Date(flrangebotpositionI
											.getFlrangebot().getT_belegdatum()
											.getTime()));
					if (kundeSokoDto_gueltig != null) {
						dataRow[AngebotReportFac.REPORT_ANGEBOT_KUNDEARTIKELNR] = kundeSokoDto_gueltig
								.getCKundeartikelnummer();
					}

					if (oArtikelDto.getVerpackungDto() != null) {
						dataRow[AngebotReportFac.REPORT_ANGEBOT_BAUFORM] = oArtikelDto
								.getVerpackungDto().getCBauform();
						dataRow[AngebotReportFac.REPORT_ANGEBOT_VERPACKUNGSART] = oArtikelDto
								.getVerpackungDto().getCVerpackungsart();
					}
					if (oArtikelDto.getMaterialIId() != null) {
						MaterialDto materialDto = getMaterialFac()
								.materialFindByPrimaryKey(
										oArtikelDto.getMaterialIId(),
										theClientDto);
						if (materialDto.getMaterialsprDto() != null) {
							/**
							 * @todo MR->MR richtige Mehrsprachigkeit: Material
							 *       in Drucksprache.
							 */
							dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_MATERIAL] = materialDto
									.getMaterialsprDto().getCBez();
						} else {
							dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_MATERIAL] = materialDto
									.getCNr();
						}
						
						MaterialzuschlagDto mzDto=getMaterialFac()
								.getKursMaterialzuschlagDtoInZielwaehrung(
									oArtikelDto.getMaterialIId(),
									flrangebotpositionI.getFlrangebot().getT_belegdatum(),
									flrangebotpositionI.getFlrangebot().getWaehrung_c_nr_angebotswaehrung(),
									theClientDto);
						
						dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_KURS_MATERIALZUSCHLAG] =mzDto.getNZuschlag();
						dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_DATUM_MATERIALZUSCHLAG] =mzDto.getTGueltigab();
						
					}
					
					dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_MATERIALGEWICHT] = oArtikelDto
							.getFMaterialgewicht();
					
					if (oArtikelDto.getGeometrieDto() != null) {
						dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_BREITE] = oArtikelDto
								.getGeometrieDto().getFBreite();
						dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_HOEHE] = oArtikelDto
								.getGeometrieDto().getFHoehe();
						dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_TIEFE] = oArtikelDto
								.getGeometrieDto().getFTiefe();
					}

				}
			}
			dataRow[AngebotReportFac.REPORT_ANGEBOT_IDENT] = cBezeichnung;
			dataRow[AngebotReportFac.REPORT_ANGEBOT_IDENT_TEXTEINGABE] = flrangebotpositionI
					.getX_textinhalt();

			dataRow[AngebotReportFac.REPORT_ANGEBOT_MENGE] = flrangebotpositionI
					.getN_menge();
			dataRow[AngebotReportFac.REPORT_ANGEBOT_EINHEIT] = flrangebotpositionI
					.getEinheit_c_nr() == null ? "" : getSystemFac()
					.formatEinheit(flrangebotpositionI.getEinheit_c_nr(),
							localeCNrI, theClientDto);

			AngebotDto angebotDto = getAngebotFac().angebotFindByPrimaryKey(
					angebotpositionDto.getBelegIId(), theClientDto);
			int iNachkommastellenPreis = getUINachkommastellenPreisVK(angebotDto
					.getMandantCNr());
			angebotpositionDto = (AngebotpositionDto) getBelegVerkaufFac()
					.getBelegpositionVerkaufReport(angebotpositionDto,
							angebotDto, iNachkommastellenPreis);

			dataRow[AngebotReportFac.REPORT_ANGEBOT_LVPOSITION] = angebotpositionDto
					.getCLvposition();

			if (Helper.short2Boolean(kundeDto.getBRechnungsdruckmitrabatt())) {
				dataRow[AngebotReportFac.REPORT_ANGEBOT_EINZELPREIS] = angebotpositionDto
						.getNReportEinzelpreisplusversteckteraufschlag();
				dataRow[AngebotReportFac.REPORT_ANGEBOT_RABATT] = angebotpositionDto
						.getDReportRabattsatz();
				dataRow[AngebotReportFac.REPORT_ANGEBOT_ZUSATZRABATTSATZ] = angebotpositionDto
						.getDReportZusatzrabattsatz();
			} else {
				dataRow[AngebotReportFac.REPORT_ANGEBOT_EINZELPREIS] = angebotpositionDto
						.getNReportNettoeinzelpreisplusversteckteraufschlag();
				dataRow[AngebotReportFac.REPORT_ANGEBOT_RABATT] = new Double(0);
				dataRow[AngebotReportFac.REPORT_ANGEBOT_ZUSATZRABATTSATZ] = new Double(0);
			}
			// UW 22.03.06 Alternative Positionen haben im Druck keinen
			// Gesamtwert.
			// Sie werden weder beim Angebotsendbetrag noch bei der Mwstsumme
			// beruecksichtigt
			if (!Helper.short2boolean(angebotpositionDto.getBAlternative())) {

				dataRow[AngebotReportFac.REPORT_ANGEBOT_GESAMTPREIS] = angebotpositionDto
						.getNReportGesamtpreis();
				if (angebotpositionDto.getPositioniIdArtikelset() == null) {
					MwstsatzReportDto m = ((MwstsatzReportDto) mwstMapI
							.get(angebotpositionDto.getMwstsatzIId()));
					m.setNSummeMwstbetrag(m.getNSummeMwstbetrag().add(
							angebotpositionDto.getNReportMwstsatzbetrag()));
					m.setNSummePositionsbetrag(m.getNSummePositionsbetrag()
							.add(angebotpositionDto.getNReportGesamtpreis()));
				}
			}
			
			dataRow[AngebotReportFac.REPORT_ANGEBOT_MWSTSATZ] = angebotpositionDto
					.getDReportMwstsatz();
			dataRow[AngebotReportFac.REPORT_ANGEBOT_MWSTBETRAG] = angebotpositionDto
					.getNReportMwstsatzbetrag();
			dataRow[AngebotReportFac.REPORT_ANGEBOT_MATERIALZUSCHLAG] = angebotpositionDto
					.getNMaterialzuschlag();

			/*
			 * if (Helper.short2Boolean(kundeDto.getBRechnungsdruckmitrabatt()))
			 * { dataRow[AngebotReportFac.REPORT_ANGEBOT_EINZELPREIS] =
			 * flrangebotpositionI
			 * .getN_nettoeinzelpreisplusversteckteraufschlag(); } else {
			 * dataRow[AngebotReportFac.REPORT_ANGEBOT_EINZELPREIS] =
			 * flrangebotpositionI
			 * .getN_nettogesamtpreisplusversteckteraufschlag(); } //
			 * Rabattsaetze nur andrucken, wenn sie nicht 0.0 sind Double
			 * ddRabattsatz = null; if (flrangebotpositionI.getF_rabattsatz() !=
			 * null && flrangebotpositionI.getF_rabattsatz().doubleValue() != 0)
			 * { ddRabattsatz = flrangebotpositionI.getF_rabattsatz(); }
			 * dataRow[AngebotReportFac.REPORT_ANGEBOT_RABATT] = ddRabattsatz;
			 * 
			 * Double ddZusatzrabattsatz = null; if
			 * (flrangebotpositionI.getF_zusatzrabattsatz() != null &&
			 * flrangebotpositionI.getF_zusatzrabattsatz() .doubleValue() != 0)
			 * { ddZusatzrabattsatz = flrangebotpositionI
			 * .getF_zusatzrabattsatz(); }
			 * dataRow[AngebotReportFac.REPORT_ANGEBOT_ZUSATZRABATTSATZ] =
			 * ddZusatzrabattsatz;
			 * 
			 * dataRow[AngebotReportFac.REPORT_ANGEBOT_RABATT] = ddRabattsatz;
			 * dataRow[AngebotReportFac.REPORT_ANGEBOT_ZUSATZRABATTSATZ] =
			 * ddZusatzrabattsatz;
			 * 
			 * // UW 22.03.06 Alternative Positionen haben im Druck keinen //
			 * Gesamtwert. // Sie werden weder beim Angebotsendbetrag noch bei
			 * der Mwstsumme // beruecksichtigt if
			 * (!Helper.short2boolean(flrangebotpositionI.getB_alternative())) {
			 * if (flrangebotpositionI
			 * .getN_nettogesamtpreisplusversteckteraufschlag() != null &&
			 * flrangebotpositionI.getN_menge() != null) { BigDecimal
			 * bdGesamtpreis = flrangebotpositionI
			 * .getN_nettogesamtpreisplusversteckteraufschlag()
			 * .multiply(flrangebotpositionI.getN_menge());
			 * 
			 * dataRow[AngebotReportFac.REPORT_ANGEBOT_GESAMTPREIS] =
			 * bdGesamtpreis; }
			 * 
			 * // Mwstsatzinformationen hinterlegen MwstsatzDto mwstsatzDto =
			 * getMandantFac() .mwstsatzFindByPrimaryKey(
			 * flrangebotpositionI.getMwstsatz_i_id(), theClientDto);
			 * 
			 * dataRow[AngebotReportFac.REPORT_ANGEBOT_MWSTSATZ] = mwstsatzDto
			 * .getFMwstsatz();
			 * dataRow[AngebotReportFac.REPORT_ANGEBOT_MWSTBETRAG] =
			 * flrangebotpositionI .getN_mwstbetrag();
			 * 
			 * // die Summen fuer den Mwstsatz der Position erhoehen
			 * MwstsatzReportDto mwstsatzReportDto = (MwstsatzReportDto)
			 * mwstMapI .get(flrangebotpositionI.getMwstsatz_i_id());
			 * 
			 * mwstsatzDto = getMandantFac().mwstsatzFindByPrimaryKey(
			 * flrangebotpositionI.getMwstsatz_i_id(), theClientDto); if
			 * (mwstsatzDto.getFMwstsatz().doubleValue() > 0) { if
			 * (flrangebotpositionI.getN_menge().doubleValue() > 0) { BigDecimal
			 * nSummePositionsbetrag = mwstsatzReportDto
			 * .getNSummePositionsbetrag() .add( flrangebotpositionI
			 * .getN_nettogesamtpreisplusversteckteraufschlagminusrabatte()
			 * .multiply( flrangebotpositionI .getN_menge()));
			 * 
			 * BigDecimal nSummeMwstbetrag = mwstsatzReportDto
			 * .getNSummeMwstbetrag() .add( flrangebotpositionI
			 * .getN_nettogesamtpreisplusversteckteraufschlagminusrabatte()
			 * .multiply( flrangebotpositionI .getN_menge()) .multiply( new
			 * BigDecimal( mwstsatzDto .getFMwstsatz() .doubleValue())
			 * .movePointLeft(2)));
			 * 
			 * mwstsatzReportDto.setNSummeMwstbetrag(nSummeMwstbetrag);
			 * mwstsatzReportDto
			 * .setNSummePositionsbetrag(nSummePositionsbetrag);
			 * 
			 * mwstMapI.put(flrangebotpositionI.getMwstsatz_i_id(),
			 * mwstsatzReportDto); }
			 * 
			 * } }
			 */
			dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITIONSART] = flrangebotpositionI
					.getAngebotpositionart_c_nr();
			dataRow[AngebotReportFac.REPORT_ANGEBOT_SEITENUMBRUCH] = bbSeitenumbruchI;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return dataRow;
	}

	private Object[] befuelleZeileAGMitPauschalkorrektur(
			BigDecimal bdKorrekturbetrag, Boolean bbSeitenumbruchI,
			KundeDto kundeDto, // Referenz
			LinkedHashMap<Integer, MwstsatzReportDto> mwstMapI, // Referenz
			Locale localeCNrI, TheClientDto theClientDto) throws EJBExceptionLP {
		Object[] dataRow = null;
		try {
			dataRow = new Object[AngebotReportFac.REPORT_ANGEBOT_ANZAHL_SPALTEN];
			// Dto holen damit LPReport.printIdent verwendet werden kann
			dataRow[AngebotReportFac.REPORT_ANGEBOT_B_ALTERNATIVE] = Helper.boolean2Short(false);

			PositionRpt posRpt = new PositionRpt();
			posRpt.setBdPreis(bdKorrekturbetrag);
			posRpt.setBdMenge(new BigDecimal(1));
			posRpt.setSBezeichnung("Pauschalkorrektur");
			posRpt.setSPositionsartCNr(LocaleFac.POSITIONSART_HANDEINGABE);
			dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITIONOBJEKT] = posRpt;

			String cBezeichnung = "Pauschalkorrektur";

			dataRow[AngebotReportFac.REPORT_ANGEBOT_BEZEICHNUNG] = cBezeichnung;
			dataRow[AngebotReportFac.REPORT_ANGEBOT_IDENT] = cBezeichnung;
			dataRow[AngebotReportFac.REPORT_ANGEBOT_MENGE] = new BigDecimal(1);
			dataRow[AngebotReportFac.REPORT_ANGEBOT_EINHEIT] = "x";

			dataRow[AngebotReportFac.REPORT_ANGEBOT_EINZELPREIS] = bdKorrekturbetrag;

			dataRow[AngebotReportFac.REPORT_ANGEBOT_GESAMTPREIS] = bdKorrekturbetrag;

			MwstsatzDto mwstsatzDto = getMandantFac()
					.mwstsatzFindByMwstsatzbezIIdAktuellster(
							kundeDto.getMwstsatzbezIId(), theClientDto);
			// Mwstsatz

			BigDecimal ust = bdKorrekturbetrag.multiply(new BigDecimal(
					mwstsatzDto.getFMwstsatz()).movePointLeft(2));

			MwstsatzReportDto m = ((MwstsatzReportDto) mwstMapI.get(mwstsatzDto
					.getIId()));
			m.setNSummeMwstbetrag(m.getNSummeMwstbetrag().add(ust));
			m.setNSummePositionsbetrag(m.getNSummePositionsbetrag().add(
					bdKorrekturbetrag));

			dataRow[AngebotReportFac.REPORT_ANGEBOT_MWSTSATZ] = mwstsatzDto
					.getFMwstsatz();

			dataRow[AngebotReportFac.REPORT_ANGEBOT_MWSTBETRAG] = ust;

			dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITIONSART] = LocaleFac.POSITIONSART_HANDEINGABE;
			dataRow[AngebotReportFac.REPORT_ANGEBOT_SEITENUMBRUCH] = bbSeitenumbruchI;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return dataRow;
	}

	/**
	 * Eine Zeile des Angebots mit einer STKL Position befuellen.
	 * 
	 * @param stuecklisteMitStrukturDtoI
	 *            die Stuecklistenposition
	 * @param cEinrueckungI
	 *            die Eintueckung fuer die cNr
	 * @param bbSeitenumbruchI
	 *            Toggle fuer Seitenumbruch
	 * @param locDruck
	 *            Locale
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Object[] die zu druckende Zeile
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileAGMitStuecklistenposition(
			StuecklisteMitStrukturDto stuecklisteMitStrukturDtoI,
			String cEinrueckungI, Boolean bbSeitenumbruchI, Locale locDruck,
			AngebotDto angebotDto, Integer mwstsatzIId, Integer preislisteIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Object[] dataRow = null;
		try {
			dataRow = new Object[AngebotReportFac.REPORT_ANGEBOT_ANZAHL_SPALTEN];

			StuecklistepositionDto stuecklistepositionDto = stuecklisteMitStrukturDtoI
					.getStuecklistepositionDto();

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					stuecklistepositionDto.getArtikelIId(), theClientDto);

			// unterstkl: 8 Pro Position eine kuenstliche Zeile zum Andrucken
			// erzeugen,
			// als Bezugsmenge gilt immer 1 Einheit der Stueckliste
			dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_STUECKLISTENPOSITION;
			dataRow[AngebotReportFac.REPORT_ANGEBOT_SEITENUMBRUCH] = bbSeitenumbruchI;
			dataRow[AngebotReportFac.REPORT_ANGEBOT_STKLMENGE] = stuecklistepositionDto
					.getNMenge();
			dataRow[AngebotReportFac.REPORT_ANGEBOT_STKLEINHEIT] = getSystemFac()
					.formatEinheit(stuecklistepositionDto.getEinheitCNr(),
							locDruck, theClientDto);

			// Einrueckung fuer Unterstuecklisten
			for (int j = 0; j < stuecklisteMitStrukturDtoI.getIEbene(); j++) {
				cEinrueckungI = cEinrueckungI + "    ";
			}

			String artikelCNr = null;

			if (artikelDto.getArtikelartCNr().equals(
					ArtikelFac.ARTIKELART_HANDARTIKEL)) {
				artikelCNr = AngebotReportFac.REPORT_VORKALKULATION_ZEICHEN_FUER_HANDEINGABE;
			} else {
				artikelCNr = stuecklistepositionDto.getArtikelDto().getCNr();
				dataRow[AngebotReportFac.REPORT_ANGEBOT_REFERENZNUMMER] = artikelDto
						.getCReferenznr();
				dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_INDEX] = artikelDto
						.getCIndex();
				dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_REVISION] = artikelDto
						.getCRevision();
				dataRow[AngebotReportFac.REPORT_ANGEBOT_STKLARTIKELKBEZ] = artikelDto
						.getKbezAusSpr();

				// KundeArtikelnr gueltig zu Belegdatum
				KundesokoDto kundeSokoDto_gueltig = this
						.getKundesokoFac()
						.kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc(
								angebotDto.getKundeIIdAngebotsadresse(),
								artikelDto.getIId(),
								new java.sql.Date(angebotDto.getTBelegdatum()
										.getTime()));
				if (kundeSokoDto_gueltig != null) {
					dataRow[AngebotReportFac.REPORT_ANGEBOT_STKLARTIKEL_KDARTIKELNR] = kundeSokoDto_gueltig
							.getCKundeartikelnummer();
				}

				// PJ18038
				VkpreisfindungDto vkpreisfindungDto = getVkPreisfindungFac()
						.verkaufspreisfindung(
								artikelDto.getIId(),
								angebotDto.getKundeIIdAngebotsadresse(),
								stuecklistepositionDto.getNMenge(),
								new Date(angebotDto.getTBelegdatum().getTime()),
								preislisteIId, mwstsatzIId,
								theClientDto.getSMandantenwaehrung(),
								theClientDto);

				VerkaufspreisDto kundenVKPreisDto = Helper
						.getVkpreisBerechnet(vkpreisfindungDto);
				if (kundenVKPreisDto != null) {
					dataRow[AngebotReportFac.REPORT_ANGEBOT_STKLARTIKEL_KDPREIS] = kundenVKPreisDto.nettopreis;
				}

			}

			dataRow[AngebotReportFac.REPORT_ANGEBOT_STKLARTIKELCNR] = cEinrueckungI
					+ artikelCNr;
			dataRow[AngebotReportFac.REPORT_ANGEBOT_STKLARTIKELBEZ] = getArtikelFac()
					.baueArtikelBezeichnungMehrzeilig(
							stuecklistepositionDto.getArtikelIId(),
							LocaleFac.POSITIONSART_IDENT, null, null, false,
							locDruck, theClientDto);

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return dataRow;
	}

	/**
	 * Eine Zeile des Angebots mit einer AGSTKL Position befuellen.
	 * 
	 * @param agstklpositionDtoI
	 *            die AGSTKL Position
	 * @param artikelDtoI
	 *            der enthaltene Artikel
	 * @param bbSeitenumbruchI
	 *            Toggle fuer Seitenumbruch
	 * @param localeDruck
	 *            Locale
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Object[] die zu druckenden Daten
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileAGMitAGSTKLPosition(
			AgstklpositionDto agstklpositionDtoI, ArtikelDto artikelDtoI,
			Boolean bbSeitenumbruchI, Locale localeDruck,
			AngebotDto angebotDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Object[] dataRow = null;
		try {
			dataRow = new Object[AngebotReportFac.REPORT_ANGEBOT_ANZAHL_SPALTEN];

			// Eine kuenstliche Zeile zum Andrucken der AGStuecklistenposition
			// erzeugen
			dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_STUECKLISTENPOSITION;
			dataRow[AngebotReportFac.REPORT_ANGEBOT_SEITENUMBRUCH] = bbSeitenumbruchI;
			dataRow[AngebotReportFac.REPORT_ANGEBOT_STKLMENGE] = agstklpositionDtoI
					.getNMenge();
			dataRow[AngebotReportFac.REPORT_ANGEBOT_STKLEINHEIT] = getSystemFac()
					.formatEinheit(agstklpositionDtoI.getEinheitCNr(),
							localeDruck, theClientDto);

			if (artikelDtoI.getArtikelartCNr().equals(
					ArtikelFac.ARTIKELART_HANDARTIKEL)) {
				dataRow[AngebotReportFac.REPORT_ANGEBOT_STKLARTIKELCNR] = AngebotReportFac.REPORT_VORKALKULATION_ZEICHEN_FUER_HANDEINGABE;
			} else {
				dataRow[AngebotReportFac.REPORT_ANGEBOT_STKLARTIKELCNR] = artikelDtoI
						.getCNr();
				dataRow[AngebotReportFac.REPORT_ANGEBOT_STKLARTIKELKBEZ] = artikelDtoI
						.getKbezAusSpr();

				// KundeArtikelnr gueltig zu Belegdatum
				KundesokoDto kundeSokoDto_gueltig = this
						.getKundesokoFac()
						.kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc(
								angebotDto.getKundeIIdAngebotsadresse(),
								artikelDtoI.getIId(),
								new java.sql.Date(angebotDto.getTBelegdatum()
										.getTime()));
				if (kundeSokoDto_gueltig != null) {
					dataRow[AngebotReportFac.REPORT_ANGEBOT_STKLARTIKEL_KDARTIKELNR] = kundeSokoDto_gueltig
							.getCKundeartikelnummer();
				}

			}

			dataRow[AngebotReportFac.REPORT_ANGEBOT_STKLARTIKELBEZ] = getArtikelFac()
					.baueArtikelBezeichnungMehrzeilig(artikelDtoI.getIId(),
							LocaleFac.POSITIONSART_IDENT, null, null, false,
							localeDruck, theClientDto);
			dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITIONOBJEKT] = getSystemReportFac()
					.getPositionForReport(LocaleFac.BELEGART_AGSTUECKLISTE,
							agstklpositionDtoI.getIId(), theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return dataRow;
	}

	/**
	 * Eine Zeile des Angebots mit einer Leerzeile befuellen.
	 * 
	 * @param bNachEndsummeI
	 *            true, wenn die Leerzeile im Anschluss an die Endsumme gedruckt
	 *            werden soll
	 * @param bbSeitenumbruchI
	 *            Toggle fuer Seitenumbruch
	 * @return Object[] die zu druckenden Daten
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileAGMitLeerzeile(boolean bNachEndsummeI,
			Boolean bbSeitenumbruchI) throws EJBExceptionLP {
		Object[] dataRow = new Object[AngebotReportFac.REPORT_ANGEBOT_ANZAHL_SPALTEN];
		String cText = " ";

		if (bNachEndsummeI) {
			dataRow[AngebotReportFac.REPORT_ANGEBOT_TEXTNACHENDSUMME] = cText;
		} else {
			dataRow[AngebotReportFac.REPORT_ANGEBOT_LEERZEILE] = cText;
		}

		dataRow[AngebotReportFac.REPORT_ANGEBOT_SEITENUMBRUCH] = bbSeitenumbruchI;
		dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_LEERZEILE;

		return dataRow;
	}

	/**
	 * Eine Zeile des Angebots mit einem Betrifft befuellen.
	 * 
	 * @param flrangebotpositionI
	 *            die Angebotposition
	 * @param bNachEndsummeI
	 *            true, wenn die Leerzeile im Anschluss an die Endsumme gedruckt
	 *            werden soll
	 * @param bbSeitenumbruchI
	 *            Toggle fuer Seitenumbruch
	 * @return Object[] die zu druckenden Daten
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	private Object[] befuelleZeileAGMitBetrifft(
			FLRAngebotpositionReport flrangebotpositionI,
			boolean bNachEndsummeI, Boolean bbSeitenumbruchI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Object[] dataRow = new Object[AngebotReportFac.REPORT_ANGEBOT_ANZAHL_SPALTEN];
		String cText = flrangebotpositionI.getC_bez();

		if (bNachEndsummeI) {
			dataRow[AngebotReportFac.REPORT_ANGEBOT_TEXTNACHENDSUMME] = cText;
		} else {
			dataRow[AngebotReportFac.REPORT_ANGEBOT_FREIERTEXT] = cText;
		}

		dataRow[AngebotReportFac.REPORT_ANGEBOT_SEITENUMBRUCH] = bbSeitenumbruchI;
		dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_BETRIFFT;
		dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITIONOBJEKT] = getSystemReportFac()
				.getPositionForReport(LocaleFac.BELEGART_ANGEBOT,
						flrangebotpositionI.getI_id(), theClientDto);

		return dataRow;
	}

	/**
	 * Eine Zeile des Angebots mit einer Texteingabe befuellen.
	 * 
	 * @param flrangebotpositionI
	 *            die Angebotposition
	 * @param bNachEndsummeI
	 *            true, wenn die Leerzeile im Anschluss an die Endsumme gedruckt
	 *            werden soll
	 * @param bbSeitenumbruchI
	 *            Toggle fuer Seitenumbruch
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Object[] die zu druckenden Daten
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileAGMitTexteingabe(
			FLRAngebotpositionReport flrangebotpositionI,
			boolean bNachEndsummeI, Boolean bbSeitenumbruchI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Object[] dataRow = null;
		try {
			dataRow = new Object[AngebotReportFac.REPORT_ANGEBOT_ANZAHL_SPALTEN];

			// UW 22.03.06 Aufgrund der Criteria ist blob und text nicht in der
			// Position enthalten
			AngebotpositionDto texteingabeDto = getAngebotpositionFac()
					.angebotpositionFindByPrimaryKey(
							flrangebotpositionI.getI_id(), theClientDto);

			// IMS 1619 leerer Text soll als Leerezeile erscheinen
			String cText = texteingabeDto.getXTextinhalt();

			if (cText != null && cText.trim().equals("")) {
				cText = " ";
			}

			if (bNachEndsummeI) {
				dataRow[AngebotReportFac.REPORT_ANGEBOT_TEXTNACHENDSUMME] = cText;
			} else {
				dataRow[AngebotReportFac.REPORT_ANGEBOT_FREIERTEXT] = cText;
			}

			dataRow[AngebotReportFac.REPORT_ANGEBOT_SEITENUMBRUCH] = bbSeitenumbruchI;
			dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_TEXTEINGABE;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return dataRow;
	}

	private Object[] befuelleZeileAGMitPosition(
			FLRAngebotpositionReport flrangebotpositionI,
			int iArtikelpositionsnummerI,
			LinkedHashMap<Integer, MwstsatzReportDto> mwstMap,
			boolean bNachEndsummeI, Boolean bbSeitenumbruchI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Object[] dataRow = null;
		try {
			dataRow = new Object[AngebotReportFac.REPORT_ANGEBOT_ANZAHL_SPALTEN];
			dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITIONOBJEKT] = getSystemReportFac()
					.getPositionForReport(LocaleFac.BELEGART_ANGEBOT,
							flrangebotpositionI.getI_id(), theClientDto);
			dataRow[AngebotReportFac.REPORT_ANGEBOT_SEITENUMBRUCH] = bbSeitenumbruchI;
			dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_IDENT;
			BigDecimal nMenge = new BigDecimal(1);
			if (flrangebotpositionI.getN_menge() != null)
				nMenge = flrangebotpositionI.getN_menge();
			dataRow[AngebotReportFac.REPORT_ANGEBOT_MENGE] = nMenge;
			dataRow[AngebotReportFac.REPORT_ANGEBOT_EINHEIT] = SystemFac.EINHEIT_STUECK
					.trim();

			String sIdent = null;
			// Druckdaten zusammenstellen
			if (flrangebotpositionI.getC_bez() != null)
				sIdent = flrangebotpositionI.getC_bez();
			else
				sIdent = "";
			dataRow[AngebotReportFac.REPORT_ANGEBOT_IDENT] = sIdent;

			String positionCNr = null;

			if (Helper.short2boolean(flrangebotpositionI.getB_alternative())) {
				positionCNr = AngebotpositionFac.ANGEBOTPOSITION_ALTERNATIVE_TEXT;
			} else {
				positionCNr = getAngebotpositionFac().getPositionNummer(
						flrangebotpositionI.getI_id())
						+ "";
			}

			if (Helper.short2boolean(flrangebotpositionI.getB_alternative())) {
				dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITION_NR] = AngebotpositionFac.ANGEBOTPOSITION_ALTERNATIVE_TEXT;
			} else {
				dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITION_NR] = iArtikelpositionsnummerI
						+ "";
			}

			dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITION] = positionCNr;

			BigDecimal bdNettogesamtpreisplusversteckteraufschlagminusrabatte = getAngebotpositionFac()
					.getGesamtpreisPosition(flrangebotpositionI.getI_id(),
							theClientDto);
			dataRow[AngebotReportFac.REPORT_ANGEBOT_GESAMTPREIS] = bdNettogesamtpreisplusversteckteraufschlagminusrabatte
					.multiply(nMenge);

			AngebotpositionDto unterpos = getAngebotpositionFac()
					.angebotpositionFindByAngebotIIdISort(
							flrangebotpositionI.getAngebot_i_id(),
							flrangebotpositionI.getI_sort() + 1);
			MwstsatzDto mwstsatzDto = getMandantFac().mwstsatzFindByPrimaryKey(
					unterpos.getMwstsatzIId(), theClientDto);
			dataRow[AngebotReportFac.REPORT_ANGEBOT_MWSTSATZ] = mwstsatzDto
					.getFMwstsatz();
			MwstsatzReportDto mwstsatzReportDto = (MwstsatzReportDto) mwstMap
					.get(mwstsatzDto.getIId());
			if (mwstsatzDto.getFMwstsatz().doubleValue() > 0) {
				BigDecimal nPositionsbetrag = bdNettogesamtpreisplusversteckteraufschlagminusrabatte
						.multiply(new BigDecimal(1));
				BigDecimal nSummeMwstbetrag = mwstsatzReportDto
						.getNSummeMwstbetrag().add(
								nPositionsbetrag.multiply(new BigDecimal(
										mwstsatzDto.getFMwstsatz()
												.doubleValue())
										.movePointLeft(2)));
				mwstsatzReportDto.setNSummeMwstbetrag(nSummeMwstbetrag);
				mwstsatzReportDto.setNSummePositionsbetrag(mwstsatzReportDto
						.getNSummePositionsbetrag().add(nPositionsbetrag));

			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return dataRow;
	}

	/**
	 * Eine Zeile des Angebots mit einem Textbaustein befuellen.
	 * 
	 * @param flrangebotpositionI
	 *            die Angebotposition
	 * @param bNachEndsummeI
	 *            true, wenn die Leerzeile im Anschluss an die Endsumme gedruckt
	 *            werden soll
	 * @param bbSeitenumbruchI
	 *            Toggle fuer Seitenumbruch
	 * @param theClientDto
	 *            String
	 * @return Object[] die zu druckenden Daten
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileAGMitTextbaustein(
			FLRAngebotpositionReport flrangebotpositionI,
			boolean bNachEndsummeI, Boolean bbSeitenumbruchI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Object[] dataRow = null;
		try {
			dataRow = new Object[AngebotReportFac.REPORT_ANGEBOT_ANZAHL_SPALTEN];
			// Dto holen
			MediastandardDto oMediastandardDto = getMediaFac()
					.mediastandardFindByPrimaryKey(
							flrangebotpositionI.getMediastandard_i_id());
			// zum Drucken vorbereiten
			BelegPositionDruckTextbausteinDto druckDto = printTextbaustein(
					oMediastandardDto, theClientDto);
			if (bNachEndsummeI) {
				dataRow[AngebotReportFac.REPORT_ANGEBOT_TEXTNACHENDSUMME] = druckDto
						.getSFreierText();
			} else {
				dataRow[AngebotReportFac.REPORT_ANGEBOT_FREIERTEXT] = druckDto
						.getSFreierText();
			}
			dataRow[AngebotReportFac.REPORT_ANGEBOT_IMAGE] = druckDto
					.getOImage();

			dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_TEXTBAUSTEIN;
			dataRow[AngebotReportFac.REPORT_ANGEBOT_SEITENUMBRUCH] = bbSeitenumbruchI;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return dataRow;
	}

	/**
	 * endumme: 4 Eine Zeile des Angebots mit einer Position nach der Position
	 * Endsumme befuellen.
	 * 
	 * @param flrangebotpositionI
	 *            die Angebotposition
	 * @param bbSeitenumbruchI
	 *            Toggle fuer Seitenumbruch
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Object[] die zu druckenden Daten
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	private Object[] befuelleZeileAGMitPositionNachEndsumme(
			FLRAngebotpositionReport flrangebotpositionI,
			Boolean bbSeitenumbruchI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		String positionsartCNr = flrangebotpositionI
				.getAngebotpositionart_c_nr();
		Object[] dataRow = new Object[AngebotReportFac.REPORT_ANGEBOT_ANZAHL_SPALTEN];

		// UW 06.04.06 Nach der Endsumme Position werden alle folgenden
		// Textpositionen
		// angedruckt, der Text steht dabei an
		// AngebotReportFac.REPORT_ANGEBOT_FREIERTEXT
		if (positionsartCNr
				.equals(AngebotServiceFac.ANGEBOTPOSITIONART_BETRIFFT)) {
			dataRow = befuelleZeileAGMitBetrifft(flrangebotpositionI, true, // Position
					// im
					// Anschluss
					// an
					// die
					// Endsumme
					// drucken
					bbSeitenumbruchI, theClientDto);
		} else if (positionsartCNr
				.equals(AngebotServiceFac.ANGEBOTPOSITIONART_TEXTBAUSTEIN)) {
			dataRow = befuelleZeileAGMitTextbaustein(flrangebotpositionI, true, // Position
					// im
					// Anschluss
					// an
					// die
					// Endsumme
					// drucken
					bbSeitenumbruchI, theClientDto); // IMAGE wird im Druck
			// nicht
			// beruecksichtigt!
		} else if (positionsartCNr
				.equals(AngebotServiceFac.ANGEBOTPOSITIONART_TEXTEINGABE)) {
			dataRow = befuelleZeileAGMitTexteingabe(flrangebotpositionI, true, // Position
					// im
					// Anschluss
					// an
					// die
					// Endsumme
					// drucken
					bbSeitenumbruchI, theClientDto);
		} else if (positionsartCNr
				.equals(AngebotServiceFac.ANGEBOTPOSITIONART_SEITENUMBRUCH)) {
			dataRow = befuelleZeileAGMitTexteingabe(flrangebotpositionI, true, // Position
					// im
					// Anschluss
					// an
					// die
					// Endsumme
					// drucken
					bbSeitenumbruchI, theClientDto);
			/**
			 * @todo MB das funktioniert nicht richtig PR 2263
			 */
			dataRow[AngebotReportFac.REPORT_ANGEBOT_TEXTNACHENDSUMME] = "";
		} else if (positionsartCNr
				.equals(AngebotServiceFac.ANGEBOTPOSITIONART_LEERZEILE)) {
			dataRow = befuelleZeileAGMitLeerzeile(true, // die Position wird im
					// Report Detail
					// angedruckt
					bbSeitenumbruchI);
		} else {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_DRUCKEN_POSITIONSART, new Exception(
							flrangebotpositionI.getAngebotpositionart_c_nr()));
		}

		// jede Position nach der Endsumme wird entsprechend gekennzeichnet
		dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_ENDSUMME;

		return dataRow;
	}

	/**
	 * Update des Data-Arrays um die Zwischensummenbezeichnung in allen
	 * Positionen verf&uuml;gbar zu haben.
	 * 
	 * @param i
	 *            ist der Index an dem die Zwischensummenposition definiert ist
	 * @param zwsVonPosition
	 *            ist die IId der Von-Position
	 * @param zwsBisPosition
	 *            ist die IId der Bis-Position
	 * @param cBez
	 */
	private void updateZwischensummenData(ArrayList<Object> datalist,
			Integer zwsVonPosition, String cBez) {
		for (Object object : datalist) {
			Object[] o = (Object[]) object;
			if (zwsVonPosition.equals(o[REPORT_ANGEBOT_INTERNAL_IID])) {
				if (null == o[REPORT_ANGEBOT_ZWSTEXT]) {
					o[REPORT_ANGEBOT_ZWSTEXT] = cBez;
				} else {
					String s = (String) o[REPORT_ANGEBOT_ZWSTEXT] + "\n" + cBez;
					o[REPORT_ANGEBOT_ZWSTEXT] = s;
				}

				return;
			}
		}
	}

	private Object[] befuelleZeileAGMitIntelligenterZwischensumme(
			FLRAngebotpositionReport flrangebotpositionI, KundeDto kundeDto,
			LinkedHashMap<Integer, MwstsatzReportDto> mwstMapI,
			Boolean bbSeitenumbruchI, Locale localeCNrI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Object[] dataRow = new Object[AngebotReportFac.REPORT_ANGEBOT_ANZAHL_SPALTEN];

		dataRow[REPORT_ANGEBOT_INTERNAL_IID] = flrangebotpositionI.getI_id();
		dataRow[REPORT_ANGEBOT_POSITIONSART] = flrangebotpositionI
				.getAngebotpositionart_c_nr();
		dataRow[REPORT_ANGEBOT_VONPOSITION] = getAngebotpositionFac()
				.getPositionNummer(flrangebotpositionI.getZwsvonposition_i_id());
		dataRow[REPORT_ANGEBOT_BISPOSITION] = getAngebotpositionFac()
				.getPositionNummer(flrangebotpositionI.getZwsbisposition_i_id());
		dataRow[REPORT_ANGEBOT_ZWSNETTOSUMME] = flrangebotpositionI
				.getZwsnettosumme();

		dataRow[REPORT_ANGEBOT_SEITENUMBRUCH] = bbSeitenumbruchI;

		Integer posnr = getAngebotpositionFac().getPositionNummer(
				flrangebotpositionI.getI_id());
		dataRow[REPORT_ANGEBOT_POSITION] = posnr == null ? "" : posnr
				.toString();
		dataRow[REPORT_ANGEBOT_BEZEICHNUNG] = flrangebotpositionI.getC_bez();
		dataRow[REPORT_ANGEBOT_KURZBEZEICHNUNG] = "";
		dataRow[REPORT_ANGEBOT_IDENT] = flrangebotpositionI.getC_bez();
		dataRow[REPORT_ANGEBOT_IDENT_TEXTEINGABE] = "";
		dataRow[REPORT_ANGEBOT_ZUSATZBEZEICHNUNG] = "";
		dataRow[REPORT_ANGEBOT_ARTIKELKOMMENTAR] = "";

		dataRow[AngebotReportFac.REPORT_ANGEBOT_MENGE] = flrangebotpositionI
				.getN_menge();
		dataRow[AngebotReportFac.REPORT_ANGEBOT_EINHEIT] = flrangebotpositionI
				.getEinheit_c_nr() == null ? "" : getSystemFac()
				.formatEinheit(flrangebotpositionI.getEinheit_c_nr(),
						localeCNrI, theClientDto);

		AngebotpositionDto angebotpositionDto = getAngebotpositionFac()
				.angebotpositionFindByPrimaryKey(flrangebotpositionI.getI_id(),
						theClientDto);

		AngebotDto angebotDto = getAngebotFac().angebotFindByPrimaryKey(
				angebotpositionDto.getBelegIId(), theClientDto);

		int iNachkommastellenPreis = getUINachkommastellenPreisVK(angebotDto
				.getMandantCNr());
		angebotpositionDto = (AngebotpositionDto) getBelegVerkaufFac()
				.getBelegpositionVerkaufReport(angebotpositionDto, angebotDto,
						iNachkommastellenPreis);
		if (Helper.short2Boolean(kundeDto.getBRechnungsdruckmitrabatt())) {
			dataRow[AngebotReportFac.REPORT_ANGEBOT_EINZELPREIS] = angebotpositionDto
					.getNReportEinzelpreisplusversteckteraufschlag();
		} else {
			dataRow[AngebotReportFac.REPORT_ANGEBOT_EINZELPREIS] = angebotpositionDto
					.getNReportNettoeinzelpreisplusversteckteraufschlag();
		}
		// UW 22.03.06 Alternative Positionen haben im Druck keinen
		// Gesamtwert.
		// Sie werden weder beim Angebotsendbetrag noch bei der Mwstsumme
		// beruecksichtigt
		if (!Helper.short2boolean(angebotpositionDto.getBAlternative())) {

			dataRow[AngebotReportFac.REPORT_ANGEBOT_GESAMTPREIS] = angebotpositionDto
					.getNReportGesamtpreis();
			if (angebotpositionDto.getPositioniIdArtikelset() == null) {
				MwstsatzReportDto m = ((MwstsatzReportDto) mwstMapI
						.get(angebotpositionDto.getMwstsatzIId()));
				m.setNSummeMwstbetrag(m.getNSummeMwstbetrag().add(
						angebotpositionDto.getNReportMwstsatzbetrag()));
				m.setNSummePositionsbetrag(m.getNSummePositionsbetrag().add(
						angebotpositionDto.getNReportGesamtpreis()));
			}
		}
		dataRow[AngebotReportFac.REPORT_ANGEBOT_RABATT] = angebotpositionDto
				.getDReportRabattsatz();
		dataRow[AngebotReportFac.REPORT_ANGEBOT_ZUSATZRABATTSATZ] = angebotpositionDto
				.getDReportZusatzrabattsatz();
		dataRow[AngebotReportFac.REPORT_ANGEBOT_MWSTSATZ] = angebotpositionDto
				.getDReportMwstsatz();
		dataRow[AngebotReportFac.REPORT_ANGEBOT_MWSTBETRAG] = angebotpositionDto
				.getNReportMwstsatzbetrag();

		// PJ 15348
		if (flrangebotpositionI.getMwstsatz_i_id() != null) {
			MwstsatzDto mwstSatzDto = getMandantFac().mwstsatzFindByPrimaryKey(
					flrangebotpositionI.getMwstsatz_i_id(), theClientDto);
			dataRow[REPORT_ANGEBOT_MWSTSATZ] = mwstSatzDto.getIFibumwstcode();
		}

		return dataRow;
	}

	private Object[] befuelleZeileAGMitPositionsartEndsumme(
			FLRAngebotpositionReport flrangebotpositionI,
			Boolean bbSeitenumbruchI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		String positionsartCNr = flrangebotpositionI
				.getAngebotpositionart_c_nr();
		Object[] dataRow = new Object[AngebotReportFac.REPORT_ANGEBOT_ANZAHL_SPALTEN];

		// jede Position nach der Endsumme wird entsprechend gekennzeichnet
		dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_ENDSUMME;
		dataRow[AngebotReportFac.REPORT_ANGEBOT_SEITENUMBRUCH] = bbSeitenumbruchI;
		return dataRow;
	}

	public JasperPrintLP printAngebotsstatistik(
			ReportAngebotsstatistikKriterienDto reportAngebotsstatistikKriterienDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		if (reportAngebotsstatistikKriterienDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception(
							"reportAngebotsstatistikKriterienDtoI == null"));
		}

		JasperPrintLP oPrint = null;

		try {
			// es gilt das Locale des Benutzers

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);

			// die Daten fuer den Report ueber Hibernate holen
			ReportAngebotsstatistikDto[] aReportAngebotsstatisikDto = getReportAngebotsstatistik(
					reportAngebotsstatistikKriterienDtoI, theClientDto);

			cAktuellerReport = AngebotReportFac.REPORT_ANGEBOTSSTATISTIK;

			int iAnzahlZeilen = aReportAngebotsstatisikDto.length; // Anzahl der
			// Zeilen in
			// der
			// Gruppe
			int iAnzahlSpalten = 6; // Anzahl der Spalten in der Gruppe

			data = new Object[iAnzahlZeilen][iAnzahlSpalten];

			// die Datenmatrix befuellen
			for (int i = 0; i < aReportAngebotsstatisikDto.length; i++) {
				ReportAngebotsstatistikDto angebotsstatistikDto = (ReportAngebotsstatistikDto) aReportAngebotsstatisikDto[i];

				data[i][AngebotReportFac.REPORT_ANGEBOTSSTATISTIK_CNR] = angebotsstatistikDto
						.getAngebotCNr();
				data[i][AngebotReportFac.REPORT_ANGEBOTSSTATISTIK_KUNDE] = angebotsstatistikDto
						.getKundenname();
				data[i][AngebotReportFac.REPORT_ANGEBOTSSTATISTIK_BELEGDATUM] = angebotsstatistikDto
						.getBelegdatumCNr();
				data[i][AngebotReportFac.REPORT_ANGEBOTSSTATISTIK_ANGEBOTENEMENGE] = angebotsstatistikDto
						.getNAngebotenemenge();
				data[i][AngebotReportFac.REPORT_ANGEBOTSSTATISTIK_ANGEBOTENERPREIS] = angebotsstatistikDto
						.getNAngebotenerpreis();
				data[i][AngebotReportFac.REPORT_ANGEBOTSSTATISTIK_MATERIALZUSCHLAG] = angebotsstatistikDto
						.getNMaterialzuschlag();
			}

			// Erstellung des Report
			HashMap<String, Object> parameter = new HashMap<String, Object>();

			parameter.put("Mandant", mandantDto.getPartnerDto()
					.getCName1nachnamefirmazeile1());
			parameter.put(
					"Artikel",
					formatAngebotsstatistikKriterien(
							reportAngebotsstatistikKriterienDtoI,
							theClientDto.getLocUi(), theClientDto));

			initJRDS(parameter, AngebotReportFac.REPORT_MODUL,
					AngebotReportFac.REPORT_ANGEBOTSSTATISTIK,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);

			oPrint = getReportPrint();

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} catch (Exception t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, t);
		}

		return oPrint;
	}

	private ReportAngebotsstatistikDto[] getReportAngebotsstatistik(
			ReportAngebotsstatistikKriterienDto reportAngebotsstatistikKriterienDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		ReportAngebotsstatistikDto[] aResult = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		try {
			session = factory.openSession();

			// Hiberante Criteria fuer alle Tabellen ausgehend von meiner
			// Haupttabelle anlegen,
			// nach denen ich filtern und sortieren kann
			Criteria crit = session
					.createCriteria(FLRAngebotpositionReport.class);

			// flranfragepositionlieferdatenReport > flranfragepositionReport >
			// flrartikel
			Criteria critArtikel = crit.createCriteria("flrartikel");

			// flranfragepositionReport > flranfrage
			Criteria critAngebot = crit.createCriteria("flrangebot");

			// Einschraenkung der Anfragen auf den aktuellen Mandanten
			critAngebot.add(Restrictions.eq("mandant_c_nr",
					theClientDto.getMandant()));

			critArtikel.add(Restrictions.eq("i_id",
					reportAngebotsstatistikKriterienDtoI.getArtikelIId()));

			// Einschraenkung nach Belegdatum von - bis
			if (reportAngebotsstatistikKriterienDtoI.getDVon() != null) {
				critAngebot.add(Restrictions.ge(
						AngebotFac.FLR_ANGEBOT_T_BELEGDATUM,
						reportAngebotsstatistikKriterienDtoI.getDVon()));
			}

			if (reportAngebotsstatistikKriterienDtoI.getDBis() != null) {
				critAngebot.add(Restrictions.le(
						AngebotFac.FLR_ANGEBOT_T_BELEGDATUM,
						reportAngebotsstatistikKriterienDtoI.getDBis()));
			}

			critAngebot.addOrder(Order.desc("c_nr"));
			crit.addOrder(Order.asc("i_sort"));

			List<?> list = crit.list();
			aResult = new ReportAngebotsstatistikDto[list.size()];
			int iIndex = 0;
			Iterator<?> it = list.iterator();
			ReportAngebotsstatistikDto reportDto = null;

			while (it.hasNext()) {
				FLRAngebotpositionReport flrangebotpositionlieferdaten = (FLRAngebotpositionReport) it
						.next();
				FLRAngebot flrangebot = flrangebotpositionlieferdaten
						.getFlrangebot();

				reportDto = new ReportAngebotsstatistikDto();
				reportDto.setAngebotCNr(flrangebot.getC_nr());
				reportDto.setKundenname(flrangebot.getFlrkunde()
						.getFlrpartner().getC_name1nachnamefirmazeile1());

				Date datBelegdatum = new Date(flrangebot.getT_belegdatum()
						.getTime());
				reportDto.setBelegdatumCNr(Helper.formatDatum(datBelegdatum,
						theClientDto.getLocUi()));

				reportDto.setIIndex(new Integer(iIndex));
				reportDto.setNAngebotenemenge(flrangebotpositionlieferdaten
						.getN_menge());

				// der Preis wird in Mandantenwaehrung angezeigt, es gilt der
				// hinterlegte Wechselkurs

				BigDecimal bdPreisinmandantenwaehrung = flrangebotpositionlieferdaten
						.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte();

				reportDto.setNAngebotenerpreis(bdPreisinmandantenwaehrung);
				reportDto.setNMaterialzuschlag(flrangebotpositionlieferdaten
						.getN_materialzuschlag());

				reportDto.setNAngebotenemenge(flrangebotpositionlieferdaten
						.getN_menge());

				aResult[iIndex] = reportDto;
				iIndex++;
			}
		} finally {
			closeSession(session);
		}
		return aResult;
	}

	private String formatAngebotsstatistikKriterien(
			ReportAngebotsstatistikKriterienDto reportAngebotsstatistikKriterienDtoI,
			Locale localeI, TheClientDto theClientDto) throws EJBExceptionLP {
		StringBuffer buff = new StringBuffer("");

		try {
			buff.append(getArtikelFac()
					.baueArtikelBezeichnungMehrzeiligOhneExc(
							reportAngebotsstatistikKriterienDtoI
									.getArtikelIId(),
							LocaleFac.POSITIONSART_IDENT, null, null, true,
							null, theClientDto));

			// Belegdatum
			if (reportAngebotsstatistikKriterienDtoI.getDVon() != null
					|| reportAngebotsstatistikKriterienDtoI.getDBis() != null) {
				buff.append("\n").append(
						getTextRespectUISpr("bes.belegdatum",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			}

			if (reportAngebotsstatistikKriterienDtoI.getDVon() != null) {
				buff.append(" ").append(
						getTextRespectUISpr("lp.von",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
				buff.append(" ").append(
						Helper.formatDatum(
								reportAngebotsstatistikKriterienDtoI.getDVon(),
								localeI));
			}

			if (reportAngebotsstatistikKriterienDtoI.getDBis() != null) {
				buff.append(" ").append(
						getTextRespectUISpr("lp.bis",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
				buff.append(" ").append(
						Helper.formatDatum(
								reportAngebotsstatistikKriterienDtoI.getDBis(),
								localeI));
			}
		} catch (RemoteException re) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, re);
		}

		String cBuffer = buff.toString().trim();

		return cBuffer;
	}

}
