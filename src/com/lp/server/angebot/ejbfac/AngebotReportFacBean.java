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
package com.lp.server.angebot.ejbfac;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
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
import com.lp.server.angebotstkl.service.AgstklarbeitsplanDto;
import com.lp.server.angebotstkl.service.AgstklmaterialDto;
import com.lp.server.angebotstkl.service.AgstklpositionDto;
import com.lp.server.angebotstkl.service.AngebotstklFac;
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
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferscheinposition;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
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
import com.lp.server.stueckliste.service.StklparameterDto;
import com.lp.server.stueckliste.service.StuecklisteAufgeloest;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklisteFacLocal;
import com.lp.server.stueckliste.service.StuecklisteMitStrukturDto;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.ejbfac.SteuercodeInfo;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.BelegartDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
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
import com.lp.server.util.HvOptional;
import com.lp.server.util.LPReport;
import com.lp.server.util.MwstsatzId;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.server.util.report.MwstsatzReportDto;
import com.lp.server.util.report.TimingInterceptor;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;
import com.lp.util.report.PositionRpt;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

@Stateless
@Interceptors(TimingInterceptor.class)
public class AngebotReportFacBean extends LPReport implements AngebotReportFac, JRDataSource {

	@EJB
	private StuecklisteFacLocal stuecklisteFacLocalBean;

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
				value = Helper.formatStyledTextForJasper(data[index][AngebotReportFac.REPORT_ANGEBOT_FREIERTEXT]);
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
				value = Helper.formatStyledTextForJasper(data[index][AngebotReportFac.REPORT_ANGEBOT_STKLARTIKELCNR]);
			} else if ("F_STKLARTIKELBEZ".equals(fieldName)) {
				value = Helper.formatStyledTextForJasper(data[index][AngebotReportFac.REPORT_ANGEBOT_STKLARTIKELBEZ]);
			} else if ("F_STKLARTIKELKBEZ".equals(fieldName)) {
				value = Helper.formatStyledTextForJasper(data[index][AngebotReportFac.REPORT_ANGEBOT_STKLARTIKELKBEZ]);
			} else if ("F_STKLARTIKEL_KDARTIKELNR".equals(fieldName)) {
				value = Helper.formatStyledTextForJasper(
						data[index][AngebotReportFac.REPORT_ANGEBOT_STKLARTIKEL_KDARTIKELNR]);
			} else if ("F_STKLARTIKEL_KDPREIS".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_STKLARTIKEL_KDPREIS];
			} else if ("F_B_ALTERNATIVE".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_B_ALTERNATIVE];
			} else if ("F_TEXTNACHENDSUMME".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_TEXTNACHENDSUMME];
			} else if (F_IDENTNUMMER.equals(fieldName)) {
				value = Helper.formatStyledTextForJasper(data[index][AngebotReportFac.REPORT_ANGEBOT_IDENTNUMMER]);
			} else if (F_BEZEICHNUNG.equals(fieldName)) {
				value = Helper.formatStyledTextForJasper(data[index][AngebotReportFac.REPORT_ANGEBOT_BEZEICHNUNG]);
			} else if (F_KURZBEZEICHNUNG.equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_KURZBEZEICHNUNG];
			} else if (F_KUNDEARTIKELNR.equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_KUNDEARTIKELNR];
			} else if (F_KUNDEARTIKELBEZEICHNUNG.equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_KUNDEARTIKELBEZEICHNUNG];
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
			} else if ("F_ZWSNETTOSUMMEN".equals(fieldName)) {
				value = new ZwsAngebotReportHelper(data[index]).nettoSummen();
			} else if ("F_ZWSTEXTE".equals(fieldName)) {
				value = new ZwsAngebotReportHelper(data[index]).cbezs();
			} else if ("F_LV_POSITION".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_LVPOSITION];
			} else if ("F_ZWSPOSPREISDRUCKEN".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_ZWSPOSPREISDRUCKEN];
			} else if ("F_AGSTKL_SUBREPORT_MENGENSTAFFEL".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_AGSTKL_SUBREPORT_MENGENSTAFFEL];
			} else if ("F_AGSTKL_SUBREPORT_MENGENSTAFFEL_SCHNELLERFASSUNG".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_AGSTKL_SUBREPORT_MENGENSTAFFEL_SCHNELLERFASSUNG];
			} else if ("F_GEWICHT".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_GEWICHT];
			} else if ("F_ARTIKEL_URSPRUNGSLAND".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_URSPRUNGSLAND];
			} else if ("F_ARTIKEL_PRAEFERENZBEGUENSTIGT".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_PRAEFERENZBEGUENSTIGT];
			} else if ("F_WARENVERKEHRSNUMMER".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_WARENVERKEHRSNUMMER];
			} else if ("F_ARTIKEL_MATERIAL_AUS_KUNDEMATERIAL".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_MATERIAL_AUS_KUNDEMATERIAL];
			} else if ("F_ARTIKEL_MATERIALBASIS_AUS_KUNDEMATERIAL".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_MATERIALBASIS_AUS_KUNDEMATERIAL];
			} else if (F_ARTIKEL_AUFSCHLAG_BETRAG.equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_AUFSCHLAG_BETRAG];
			} else if (F_ARTIKEL_AUFSCHLAG_PROZENT.equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_AUFSCHLAG_PROZENT];
			} else if ("F_SUBREPORT_PRODUKTSTUECKLISTE".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_SUBREPORT_PRODUKTSTUECKLISTE];
			} else if ("F_SUBREPORT_PRODUKTSTUECKLISTE_KONFIGURATIONSWERTE".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_SUBREPORT_PRODUKTSTUECKLISTE_KONFIGURATIONSWERTE];
			} else if ("F_LIEFERZEIT".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_LIEFERZEIT];
			} else if ("F_AGSTKL_ZEICHNUNGSNUMMER".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_AGSTKL_ZEICHNUNGSNUMMER];
			} else if ("F_AGSTKL_INITIALKOSTEN".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_AGSTKL_INITIALKOSTEN];
			}
		} else if (cAktuellerReport.equals(AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ALLE)
				|| cAktuellerReport.equals(AngebotReportFac.REPORT_ANGEBOT_JOURNAL_OFFENE)) {
			if ("F_ANGEBOTIID".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANGEBOTIID];
			} else if ("F_ANGEBOTCNR".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANGEBOTCNR];
			} else if ("F_WAEHRUNG".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_WAEHRUNG];
			} else if ("F_BELEGDATUM".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_BELEGDATUM];
			} else if ("F_NACHFASSTERMIN".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_NACHFASSTERMIN];
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
				value = Helper.formatStyledTextForJasper(
						data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_INTERNERKOMMENTAR]);
			} else if ("F_ARTIKELCNR".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ARTIKELCNR];
			} else if ("F_LIEFERZEIT".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_LIEFERZEIT];
			} else if ("F_EINHEIT_LIEFERZEIT".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_EINHEIT_LIEFERZEIT];
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
			} else if ("F_I_VERSION".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_I_VERSION];
			} else if ("F_T_VERSION".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_T_VERSION];
			} else if ("F_KUNDE_PROVISIONSEMPFAENGER_KURZZEICHEN".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_PROVISIONSEMPFAENGER_KURZZEICHEN];
			} else if ("F_KUNDE_PROVISIONSEMPFAENGER_NAME".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_PROVISIONSEMPFAENGER_NAME];
			}
		} else if (cAktuellerReport.equals(AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE)) {
			if ("F_ANGEBOTIID".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_ANGEBOTIID];
			} else if ("F_ANGEBOTCNR".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_ANGEBOTCNR];
			} else if ("F_BELEGDATUM".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_BELEGDATUM];
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
				value = Helper.formatStyledTextForJasper(
						data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_INTERNERKOMMENTAR]);
			} else if ("F_EXTERNERKOMMENTAR".equals(fieldName)) {
				value = Helper.formatStyledTextForJasper(
						data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_EXTERNERKOMMENTAR]);
			} else if ("F_GESAMTANGEBOTSWERT".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_GESAMTANGEBOTSWERT];
			} else if ("F_ERLEDIGUNGSGRUND".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_ANGEBOTERLEDIGUNGSGRUND];
			} else if ("F_ERLEDIGUNGSGRUND_AB_NR".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_ERLEDIGUNGSGRUND_AB_NR];
			}
		}

		else if (cAktuellerReport.equals(AngebotReportFac.REPORT_VORKALKULATION)) {
			if ("F_IDENT".equals(fieldName)) {
				value = Helper.formatStyledTextForJasper(data[index][AngebotReportFac.REPORT_VORKALKULATION_IDENT]);
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
			} else if ("F_VERKAUFSPREIS_KALKULATORISCHER_ARTIKEL".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_VERKAUFSPREIS_KALKULATORISCHER_ARTIKEL];
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
			} else if ("F_LIEF1PREISGUELTIGAB".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_LIEF1PREISGUELTIGAB];
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
			} else if ("F_SETARTIKEL_TYP".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_SETARTIKEL_TYP];
			} else if ("F_EK_PREIS".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_EK_PREIS];
			} else if ("F_WIEDERBESCHAFFUNGSZEIT".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_WIEDERBESCHAFFUNGSZEIT];
			} else if ("F_LIEFERANT_AUS_POSITION".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_LIEFERANT_AUS_POSITION];
			} else if ("F_TEXTEINGABE".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_TEXTEINGABE];
			} else if ("F_ARTIKEL_IST_ARBEITSZEIT".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_ARTIKEL_IST_ARBEITSZEIT];
			} else if ("F_LIEFERZEIT".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_LIEFERZEIT];
			} else if ("F_ARTIKELART".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_ARTIKELART];
			} else if ("F_STUECKLISTEART".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_STUECKLISTEART];
			} else if ("F_FREMDFERTIGUNG".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_FREMDFERTIGUNG];
			} else if ("F_MELDEPFLICHTIG".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_MELDEPFLICHTIG];
			} else if ("F_BEWILLIGUNGSPFLICHTIG".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_BEWILLIGUNGSPFLICHTIG];
			} else if ("F_RUESTMENGE".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_RUESTMENGE];
			} else if ("Initialkosten".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_INITIALKOSTEN];
			} else if ("F_AGSTKL_MATERIAL".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_AGSTKL_MATERIAL];
			} else if ("F_AGSTKLPOSITION_PREIS".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_AGSTKLPOSITION_PREIS];
			} else if ("F_AGSTKL_ZEICHNUNGSNUMMER".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_AGSTKL_ZEICHNUNGSNUMMER];
			}

		} else if (cAktuellerReport.equals(AngebotReportFac.REPORT_ANGEBOT_ADRESSETIKETT)) {
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
		} else if (cAktuellerReport.equals(AngebotReportFac.REPORT_ANGEBOTSSTATISTIK)) {
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
		} else if (cAktuellerReport.equals(AngebotReportFac.REPORT_ANGEBOTSPOTENTIAL)) {
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
	 * @param iIdAngebotI    PK des Angebots
	 * @param iAnzahlKopienI wieviele Kopien zusaetzlich zum Original?
	 * @param bMitLogo       Boolean
	 * @param theClientDto   der aktuelle Benutzer
	 * @return JasperPrint[] der Druck und seine Kopien
	 * @throws EJBExceptionLP Ausnahme
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public JasperPrintLP[] printAngebot(Integer iIdAngebotI, Integer iAnzahlKopienI, Boolean bMitLogo,
			String sReportname, TheClientDto theClientDto) throws EJBExceptionLP {
		return printAngebot(iIdAngebotI, iAnzahlKopienI, bMitLogo, sReportname, null, theClientDto);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public JasperPrintLP[] printAngebot(Integer iIdAngebotI, Integer iAnzahlKopienI, Boolean bMitLogo,
			String sReportname, String sDrucktype, TheClientDto theClientDto) throws EJBExceptionLP {
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
			AngebotDto angebotDto = getAngebotFac().angebotFindByPrimaryKey(iIdAngebotI, theClientDto);

			// UW 23.03.06 Tiefe der Stuecklistenaufloeseung bestimmen
			ParametermandantDto parametermandantDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ANGEBOT, ParameterFac.PARAMETER_ANGEBOT_AG_STKL_AUFLOESUNG_TIEFE);

			int iStuecklisteaufloesungTiefe = ((Integer) parametermandantDto.getCWertAsObject()).intValue();

			// alle relevanten Positionen zum Andrucken holen
			session = FLRSessionFactory.getFactory().openSession();

			FLRAngebotpositionReport[] aFLRAngebotposition = holeAngebotpositionen(session, iIdAngebotI, false, // auch
																												// NULL
																												// Mengen
					false, // auch NULL Mengen
					false); // mit alternativen Positionen

			// es gilt das Locale des Kunden
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(angebotDto.getKundeIIdAngebotsadresse(),
					theClientDto);

			KundeDto kundeDtoRechnungsadresse = getKundeFac()
					.kundeFindByPrimaryKey(angebotDto.getKundeIIdRechnungsadresse(), theClientDto);

			KundeDto kundeDtoLieferadresse = getKundeFac().kundeFindByPrimaryKey(angebotDto.getKundeIIdLieferadresse(),
					theClientDto);
			kundeDtoLieferadresse.getPartnerDto().getLandplzortDto();

			Locale locDruck = Helper.string2Locale(kundeDto.getPartnerDto().getLocaleCNrKommunikation());

			AnsprechpartnerDto ansprechpartnerDto = null;

			if (angebotDto.getAnsprechpartnerIIdKunde() != null) {
				ansprechpartnerDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(angebotDto.getAnsprechpartnerIIdKunde(), theClientDto);
			}

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);

			// fuer das Andrucken der Mwstsatze wird eine Map vorbereitet, die
			// einen
			// Eintrag fuer jeden Mwstsatz des Mandanten enthaelt
			final Set<?> mwstSatzKeys = getMandantFac().mwstsatzIIdFindAllByMandant(angebotDto.getMandantCNr(),
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

			boolean bLieferzeitUnterschiedlich = false;

			for (int i = 0; i < aFLRAngebotposition.length; i++) {
				positionsartCNr = aFLRAngebotposition[i].getAngebotpositionart_c_nr();

				typCNr = aFLRAngebotposition[i].getTyp_c_nr();
				if (typCNr == null) {

					if (positionsartCNr.equals(AngebotServiceFac.ANGEBOTPOSITIONART_IDENT)
							|| positionsartCNr.equals(AngebotServiceFac.ANGEBOTPOSITIONART_HANDEINGABE)
							|| positionsartCNr.equals(AngebotServiceFac.ANGEBOTPOSITIONART_AGSTUECKLISTE)) {

						if (positionsartCNr.equals(AngebotServiceFac.ANGEBOTPOSITIONART_IDENT)
								|| positionsartCNr.equals(AngebotServiceFac.ANGEBOTPOSITIONART_HANDEINGABE)) {

							if (aFLRAngebotposition[i].getI_lieferzeitinstunden() != null
									&& angebotDto.getILieferzeitinstunden() != null) {

								if (!(angebotDto.getILieferzeitinstunden()
										.equals(aFLRAngebotposition[i].getI_lieferzeitinstunden()))) {
									bLieferzeitUnterschiedlich = true;
								}
							}

						}

						// PJ18020 ohne Kalkulatorische Artikel
						if (aFLRAngebotposition[i].getArtikel_i_id() != null) {
							ArtikelDto oArtikelDto = getArtikelFac()
									.artikelFindByPrimaryKey(aFLRAngebotposition[i].getArtikel_i_id(), theClientDto);
							if (Helper.short2boolean(oArtikelDto.getBKalkulatorisch())) {

								// SP5891
								if (aFLRAngebotposition[i]
										.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte() != null
										&& aFLRAngebotposition[i]
												.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte()
												.doubleValue() == 0) {
									continue;
								} else {
									ArrayList al = new ArrayList();
									al.add(oArtikelDto.getCNr());
									throw new EJBExceptionLP(
											EJBExceptionLP.FEHLER_KALKULATORISCHER_ARTIKEL_MIT_PREIS_UNGLEICH_0, al,
											new Exception("FEHLER_KALKULATORISCHER_ARTIKEL_MIT_PREIS_UNGLEICH_0"));

								}

							}

						}
						dataList.add(befuelleZeileAGMitPreisbehafteterPosition(aFLRAngebotposition[i], kundeDto,
								kundeDtoRechnungsadresse, kundeDtoLieferadresse, iArtikelpositionsnummer, mwstMap,
								bbSeitenumbruch, locDruck, theClientDto));

						iArtikelpositionsnummer++;

						// unterstkl: 6 Wenn es zu einem Artikel eine
						// Stueckliste
						// gibt...
						if (aFLRAngebotposition[i].getAngebotpositionart_c_nr()
								.equals(AngebotServiceFac.ANGEBOTPOSITIONART_IDENT)) {

							StuecklisteAufgeloest stuecklisteInfoDto = stuecklisteFacLocalBean
									.getStrukturdatenEinesArtikelsStrukturiert(aFLRAngebotposition[i].getArtikel_i_id(),
											true, iStuecklisteaufloesungTiefe, false, new BigDecimal(1), true, false,
											theClientDto);

							// PJ20526
							if (stuecklisteInfoDto != null && stuecklisteInfoDto.getStuecklisteDto() != null
									&& stuecklisteInfoDto.getStuecklisteDto()
											.getStuecklisteIIdFormelstueckliste() != null) {

								JasperPrintLP datenGesamtkalkulation = getStuecklisteReportFac().printGesamtkalkulation(
										stuecklisteInfoDto.getStuecklisteDto().getIId(),
										aFLRAngebotposition[i].getN_menge(), false, false, false, false, true, false,
										false, null, null, null, false, null, theClientDto);

								Object[] oletzteZeile = (Object[]) dataList.get(dataList.size() - 1);
								oletzteZeile[REPORT_ANGEBOT_SUBREPORT_PRODUKTSTUECKLISTE] = datenGesamtkalkulation
										.transformToSubreport();

								String[] fieldnames = new String[] { "Parameter", "Bezeichnung", "Typ",
										"WertAsObject" };

								StklparameterDto[] stklparameterDtos = getStuecklisteFac()
										.stklparameterFindByStuecklisteIId(
												stuecklisteInfoDto.getStuecklisteDto().getIId(), theClientDto);

								Object[][] dataSub = new Object[stklparameterDtos.length][fieldnames.length];

								for (int j = 0; j < stklparameterDtos.length; j++) {

									dataSub[j][0] = stklparameterDtos[j].getCNr();

									StklparameterDto stklparameterDto = getStuecklisteFac()
											.stklparameterFindByPrimaryKey(stklparameterDtos[j].getIId(), theClientDto);
									if (stklparameterDto.getStklparametersprDto() != null) {
										dataSub[j][1] = stklparameterDto.getStklparametersprDto().getCBez();
									}
									dataSub[j][2] = stklparameterDto.getCTyp();
									dataSub[j][3] = stklparameterDto.getCWert();

								}

								oletzteZeile[REPORT_ANGEBOT_SUBREPORT_PRODUKTSTUECKLISTE_KONFIGURATIONSWERTE] = new LPDatenSubreport(
										dataSub, fieldnames);

							}

							if (stuecklisteInfoDto.getAnzahlPositionen() > 0) {
								ArrayList<?> alStuecklisteAufgeloest = stuecklisteInfoDto.getAlStuecklisteAufgeloest();

								Iterator<?> it = alStuecklisteAufgeloest.iterator();

								while (it.hasNext()) {
									StuecklisteAufgeloest stuecklisteMitStrukturDto = (StuecklisteAufgeloest) it.next();

									dataList.add(befuelleZeileAGMitStuecklistenposition(stuecklisteMitStrukturDto,
											"    ", bbSeitenumbruch, locDruck, angebotDto,
											aFLRAngebotposition[i].getMwstsatz_i_id(),
											kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(), theClientDto));
								}
							}

						} else if (aFLRAngebotposition[i].getAngebotpositionart_c_nr()
								.equals(AngebotServiceFac.ANGEBOTPOSITIONART_AGSTUECKLISTE)) {
							// UW 23.03.06 Wenn die gewuenschte
							// Stuecklistentiefe ==
							// 0, dann kein Zugriff noetig
							if (iStuecklisteaufloesungTiefe != 0) {
								AgstklpositionDto[] aAgstklpositionDto = getAngebotstklpositionFac()
										.agstklpositionFindByAgstklIIdBDruckenOhneExc(
												aFLRAngebotposition[i].getAgstkl_i_id(), new Short((short) 1), // alle
												// Positionen
												// , die
												// mitgedruckt
												// werden
												// sollen
												theClientDto);

								for (int k = 0; k < aAgstklpositionDto.length; k++) {
									ArtikelDto artikelDtoAgstklposition = getArtikelFac().artikelFindByPrimaryKey(
											aAgstklpositionDto[k].getArtikelIId(), theClientDto);

									dataList.add(befuelleZeileAGMitAGSTKLPosition(aAgstklpositionDto[k],
											artikelDtoAgstklposition, bbSeitenumbruch, locDruck, angebotDto,
											theClientDto));

									// Wenn es zu einem Artikel eine Stueckliste
									// gibt...
									if (!artikelDtoAgstklposition.getArtikelartCNr()
											.equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
										StuecklisteAufgeloest stuecklisteInfoDto = stuecklisteFacLocalBean
												.getStrukturdatenEinesArtikelsStrukturiert(
														artikelDtoAgstklposition.getIId(), true,
														iStuecklisteaufloesungTiefe, false, new BigDecimal(1), true,
														false, theClientDto);

										if (stuecklisteInfoDto.getAnzahlPositionen() > 0) {
											ArrayList<?> alStuecklisteAufgeloest = stuecklisteInfoDto
													.getAlStuecklisteAufgeloest();

											Iterator<?> it = alStuecklisteAufgeloest.iterator();

											while (it.hasNext()) {
												StuecklisteAufgeloest stuecklisteMitStrukturDto = (StuecklisteAufgeloest) it
														.next();

												dataList.add(befuelleZeileAGMitStuecklistenposition(
														stuecklisteMitStrukturDto, "    ", bbSeitenumbruch, locDruck,
														angebotDto, aFLRAngebotposition[i].getMwstsatz_i_id(),
														kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
														theClientDto));
											}
										}
									}
								}
							}
						}
					} else if (positionsartCNr.equals(AngebotServiceFac.ANGEBOTPOSITIONART_BETRIFFT)) {
						dataList.add(befuelleZeileAGMitBetrifft(aFLRAngebotposition[i], false, // die Position
								// wird
								// im Report Detail
								// angedruckt
								bbSeitenumbruch, theClientDto));
					} else if (positionsartCNr.equals(AngebotServiceFac.ANGEBOTPOSITIONART_TEXTEINGABE)) {
						dataList.add(befuelleZeileAGMitTexteingabe(aFLRAngebotposition[i], false, // die Position
								// wird
								// im Report Detail
								// angedruckt
								bbSeitenumbruch, theClientDto));
					} else if (positionsartCNr.equals(AngebotServiceFac.ANGEBOTPOSITIONART_TEXTBAUSTEIN)) {
						dataList.add(befuelleZeileAGMitTextbaustein(aFLRAngebotposition[i], false, // die Position
								// wird
								// im Report Detail
								// angedruckt
								bbSeitenumbruch, theClientDto));
					} else if (positionsartCNr.equals(AngebotServiceFac.ANGEBOTPOSITIONART_LEERZEILE)) {
						dataList.add(befuelleZeileAGMitLeerzeile(false, // die
								// Position
								// wird im
								// Report
								// Detail
								// angedruckt
								bbSeitenumbruch));
					} else if (positionsartCNr.equals(AngebotServiceFac.ANGEBOTPOSITIONART_SEITENUMBRUCH)) {
						bbSeitenumbruch = new Boolean(!bbSeitenumbruch.booleanValue()); // toggle
					} else if (positionsartCNr
							.equals(AngebotServiceFac.ANGEBOTPOSITIONART_INTELLIGENTE_ZWISCHENSUMME)) {
						dataList.add(befuelleZeileAGMitIntelligenterZwischensumme(aFLRAngebotposition[i], kundeDto,
								mwstMap, bbSeitenumbruch, locDruck, theClientDto));
						// updateZwischensummenData(
						// dataList,
						// aFLRAngebotposition[i].getZwsvonposition_i_id(),
						// aFLRAngebotposition[i].getC_bez());
						updateZwischensummenData(dataList, aFLRAngebotposition[i]);
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
					else if (positionsartCNr.equals(AngebotServiceFac.ANGEBOTPOSITIONART_ENDSUMME)) {
						// SP1581 -> Pauschalkorrektur hinzufuegen
						if (angebotDto.getNKorrekturbetrag() != null
								&& angebotDto.getNKorrekturbetrag().doubleValue() != 0) {
							dataList.add(befuelleZeileAGMitPauschalkorrektur(angebotDto.getNKorrekturbetrag(),
									bbSeitenumbruch, kundeDto, mwstMap, locDruck, angebotDto.getTBelegdatum(),
									theClientDto));
							pauschalkorrekturHinzugefuegt = true;
						}
						dataList.add(befuelleZeileAGMitPositionsartEndsumme(aFLRAngebotposition[i], bbSeitenumbruch,
								theClientDto));
					}
				} else {

					if (positionsartCNr.equals(AngebotServiceFac.ANGEBOTPOSITIONART_POSITION)
							&& !aFLRAngebotposition[i].getC_zbez().equals(LocaleFac.POSITIONBEZ_ENDE)) {

						Object[] dataRow = befuelleZeileAGMitPosition(aFLRAngebotposition[i], iArtikelpositionsnummer,
								mwstMap, false, bbSeitenumbruch, theClientDto);

						if (aFLRAngebotposition[i].getTyp_c_nr().equals(LocaleFac.POSITIONTYP_MITPREISE)) {
							dataList.add(dataRow);
							List<?> l = null;
							String sArtikelInfo = new String();
							SessionFactory factory = FLRSessionFactory.getFactory();
							session = factory.openSession();
							Criteria crit = session.createCriteria(FLRAngebotposition.class);
							crit.add(Restrictions.eq("position_i_id", aFLRAngebotposition[i].getI_id()));
							crit.addOrder(Order.asc(AngebotpositionFac.FLR_ANGEBOTPOSITION_I_SORT));
							l = crit.list();
							Iterator<?> iter = l.iterator();

							while (iter.hasNext()) {
								dataRow = new Object[AngebotReportFac.REPORT_ANGEBOT_ANZAHL_SPALTEN];
								FLRAngebotposition pos = (FLRAngebotposition) iter.next();
								if (pos.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_IDENT)
										|| pos.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_HANDEINGABE)
										|| pos.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_TEXTEINGABE)) {
									if (pos.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_IDENT))
										sArtikelInfo = pos.getFlrartikel().getC_nr();
									else if (pos.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_TEXTEINGABE))
										sArtikelInfo = pos.getX_textinhalt();
									else if (pos.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_HANDEINGABE))
										sArtikelInfo = pos.getC_bez();
									dataRow[AngebotReportFac.REPORT_ANGEBOT_IDENTNUMMER] = sArtikelInfo;
									dataRow[AngebotReportFac.REPORT_ANGEBOT_IDENT] = sArtikelInfo.toString();

									// weitere Daten
									dataRow[AngebotReportFac.REPORT_ANGEBOT_MENGE] = pos.getN_menge();
									if (pos.getEinheit_c_nr() != null)
										dataRow[AngebotReportFac.REPORT_ANGEBOT_EINHEIT] = pos.getEinheit_c_nr().trim();
									dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITIONSART] = LocaleFac.POSITIONSART_IDENT;
									dataRow[AngebotReportFac.REPORT_ANGEBOT_EINZELPREIS] = pos.getN_nettogesamtpreis();
									if (pos.getN_menge() != null && pos.getN_nettogesamtpreis() != null)
										dataRow[AngebotReportFac.REPORT_ANGEBOT_GESAMTPREIS] = pos.getN_menge()
												.multiply(pos.getN_nettogesamtpreis());
									dataRow[AngebotReportFac.REPORT_ANGEBOT_SEITENUMBRUCH] = bbSeitenumbruch;
									dataRow[AngebotReportFac.REPORT_ANGEBOT_TYP_CNR] = pos.getTyp_c_nr();
									dataList.add(dataRow);
								} else if (pos.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_LEERZEILE)) {
									dataList.add(befuelleZeileAGMitLeerzeile(false, // die
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
							SessionFactory factory = FLRSessionFactory.getFactory();
							session = factory.openSession();
							Criteria crit = session.createCriteria(FLRAngebotposition.class);
							crit.add(Restrictions.eq("position_i_id", aFLRAngebotposition[i].getI_id()));
							crit.addOrder(Order.asc(AngebotpositionFac.FLR_ANGEBOTPOSITION_I_SORT));
							l = crit.list();
							Iterator<?> iter = l.iterator();
							while (iter.hasNext()) {
								FLRAngebotposition pos = (FLRAngebotposition) iter.next();
								if (pos.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_IDENT)) {
									sbArtikelInfo.append(pos.getFlrartikel().getC_nr());
									sbArtikelInfo.append("  ");
									if (pos.getFlrartikel().getFlrartikelgruppe() != null)
										sbArtikelInfo.append(pos.getFlrartikel().getFlrartikelgruppe().getC_nr());
									if (iter.hasNext()) {
										sbArtikelInfo.append("\n");
									}
								} else if (pos.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_TEXTEINGABE)) {
									sbArtikelInfo.append(pos.getX_textinhalt());
									if (iter.hasNext()) {
										sbArtikelInfo.append("\n");
									}
								}
								if (pos.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_HANDEINGABE)) {
									if (pos.getC_bez() != null) {
										sbArtikelInfo.append(pos.getC_bez());
										if (iter.hasNext()) {
											sbArtikelInfo.append("\n");
										}
									}
								}
							}
							// Druckdaten zusammenstellen
							dataRow[AngebotReportFac.REPORT_ANGEBOT_IDENTNUMMER] = sbArtikelInfo.toString();
							dataRow[AngebotReportFac.REPORT_ANGEBOT_IDENT] = sbArtikelInfo.toString();

							dataList.add(dataRow);

						}

					}
				}

			}
			// SP1581 -> Pauschalkorrektur hinzufuegen
			if (!pauschalkorrekturHinzugefuegt && angebotDto.getNKorrekturbetrag() != null
					&& angebotDto.getNKorrekturbetrag().doubleValue() != 0) {
				dataList.add(befuelleZeileAGMitPauschalkorrektur(angebotDto.getNKorrekturbetrag(), bbSeitenumbruch,
						kundeDto, mwstMap, locDruck, angebotDto.getTBelegdatum(), theClientDto));
			}

			// Erstellung des Report
			HashMap<String, Object> parameter = new HashMap<String, Object>();
			parameter.put("P_ZUSAMMENFASSUNG", Helper.short2Boolean(angebotDto.getBMitzusammenfassung()));
			// PJ17046 Wenn 'mit Zusammenfassung' dann Positionsdaten
			// duplizieren
			duplizierePositionenWegenZusammenfassung(angebotDto, dataList, AngebotReportFac.REPORT_ANGEBOT_POSITIONSART,
					AngebotReportFac.REPORT_ANGEBOT_SEITENUMBRUCH);

			// jetzt die Map mit dataRows in ein Object[][] fuer den Druck
			// umwandeln
			data = new Object[dataList.size()][AngebotReportFac.REPORT_ANGEBOT_ANZAHL_SPALTEN];
			data = (Object[][]) dataList.toArray(data);

			// Kopftext
			String sKopftext = angebotDto.getXKopftextuebersteuert();

			if (sKopftext == null || sKopftext.length() == 0) {
				AngebottextDto angebottextDto = getAngebotServiceFac().angebottextFindByMandantCNrLocaleCNrCNr(
						kundeDto.getPartnerDto().getLocaleCNrKommunikation(), AngebotServiceFac.ANGEBOTTEXT_KOPFTEXT,
						theClientDto);

				sKopftext = angebottextDto.getXTextinhalt();
			}

			// Fusstext
			String sFusstext = angebotDto.getXFusstextuebersteuert();

			if (sFusstext == null || sFusstext.length() == 0) {
				AngebottextDto angebottextDto = getAngebotServiceFac().angebottextFindByMandantCNrLocaleCNrCNr(
						kundeDto.getPartnerDto().getLocaleCNrKommunikation(), AngebotServiceFac.ANGEBOTTEXT_FUSSTEXT,
						theClientDto);

				sFusstext = angebottextDto.getXTextinhalt();
			}

			// CK: PJ 13849
			parameter.put("P_BEARBEITER",
					getPersonalFac().getPersonRpt(angebotDto.getPersonalIIdAnlegen(), theClientDto));

			// PJ21553
			parameter.put("P_SPEDITEUR_ACCOUNTINGNR", getKundeFac().getAccountingNr(
					angebotDto.getKundeIIdLieferadresse(), angebotDto.getSpediteurIId(), theClientDto));

			// PJ19177
			parameter.put("P_PERSON_AENDERN",
					getPersonalFac().getPersonRpt(angebotDto.getPersonalIIdAendern(), theClientDto));

			parameter.put("P_BELEGWAEHRUNG", angebotDto.getWaehrungCNr());
			parameter.put("Mandantenadresse", Helper.formatMandantAdresse(mandantDto));
			parameter.put("Adressefuerausdruck", formatAdresseFuerAusdruck(kundeDto.getPartnerDto(), ansprechpartnerDto,
					mandantDto, locDruck, LocaleFac.BELEGART_ANGEBOT));

			parameter.put("P_FREMDSYSTEMNR", kundeDto.getCFremdsystemnr());

			if (ansprechpartnerDto != null) {
				parameter.put("P_ANSPRECHPARTNER_ADRESSBLOCK",
						getPartnerFac().formatFixAnredeTitelName2Name1FuerAdresskopf(ansprechpartnerDto.getPartnerDto(),
								locDruck, null));
			}

			// PJ18870
			parameter.put("P_SUBREPORT_PARTNERKOMMENTAR",
					getPartnerServicesFacLocal().getSubreportAllerMitzudruckendenPartnerkommentare(
							kundeDto.getPartnerDto().getIId(), true, LocaleFac.BELEGART_ANGEBOT, theClientDto));

			parameter.put("Kundeuid", kundeDto.getPartnerDto().getCUid());
			parameter.put("KundeEori", kundeDto.getPartnerDto().getCEori());
			parameter.put("P_LIEFERANTENNR", kundeDto.getCLieferantennr());
			parameter.put("P_KUNDE_KUNDENNUMMER", kundeDto.getIKundennummer());

			// PJ20323
			parameter.put("P_LKZ_ANGEBOTSADRESSE", kundeDto.getPartnerDto().formatLKZ());

			// SP7686
			parameter.put("P_LIEFERZEITENUNTERSCHIEDLICH", bLieferzeitUnterschiedlich);

			// PJ19209
			if (kundeDto.getNKupferzahl() != null) {
				parameter.put("P_KUPFERZAHL", kundeDto.getNKupferzahl());
			} else {
				// PJ19683
				ParametermandantDto parameterKupferzahl = getParameterFac().getMandantparameter(
						theClientDto.getMandant(), ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_KUPFERZAHL);

				Double kupferzahl = (Double) parameterKupferzahl.getCWertAsObject();
				if (kupferzahl != null) {
					parameter.put("P_KUPFERZAHL", new BigDecimal(kupferzahl));
				}
			}

			if (angebotDto.getKundeIIdLieferadresse() != null) {

				KundeDto lieferadresseKunde = getKundeFac().kundeFindByPrimaryKey(angebotDto.getKundeIIdLieferadresse(),
						theClientDto);
				AnsprechpartnerDto aLief = null;

				if (angebotDto.getAnsprechpartnerIIdLieferadresse() != null) {
					aLief = getAnsprechpartnerFac().ansprechpartnerFindByPrimaryKey(
							angebotDto.getAnsprechpartnerIIdLieferadresse(), theClientDto);
				}

				// SP8343
				if (!angebotDto.getKundeIIdLieferadresse().equals(angebotDto.getKundeIIdAngebotsadresse())) {

					parameter.put("P_LIEFERADRESSE", formatAdresseFuerAusdruck(lieferadresseKunde.getPartnerDto(),
							aLief, mandantDto, locDruck, LocaleFac.BELEGART_ANGEBOT));
				}

				if (aLief != null) {
					parameter.put("P_ANSPRECHPARTNER_LIEFERADRESSE", getPartnerFac()
							.formatFixAnredeTitelName2Name1FuerAdresskopf(aLief.getPartnerDto(), locDruck, null));
				}

				// SP3594
				if (lieferadresseKunde.getPartnerDto().getCPostfach() != null) {
					parameter.put("P_LIEFER_HAUSADRESSE", formatAdresseFuerAusdruck(lieferadresseKunde.getPartnerDto(),
							aLief, mandantDto, locDruck, LocaleFac.BELEGART_ANGEBOT, true));
				}

			}

			// Nur Andrucken, wenn Auftragsadresse und Rechnungsadresse ungleich
			if (kundeDtoRechnungsadresse.getIId().equals(kundeDto.getIId()) && sindAnsprechpartnerGleich(
					angebotDto.getAnsprechpartnerIIdKunde(), angebotDto.getAnsprechpartnerIIdRechnungsadresse())) {

			} else {
				AnsprechpartnerDto ansprechpartnerDtoRechnungsadresse = null;

				if (angebotDto.getAnsprechpartnerIIdRechnungsadresse() != null) {
					ansprechpartnerDtoRechnungsadresse = getAnsprechpartnerFac().ansprechpartnerFindByPrimaryKey(
							angebotDto.getAnsprechpartnerIIdRechnungsadresse(), theClientDto);
				}

				parameter.put("P_KUNDERECHNUNGSADRESSE_ADRESSBLOCK",
						formatAdresseFuerAusdruck(kundeDtoRechnungsadresse.getPartnerDto(),
								ansprechpartnerDtoRechnungsadresse, mandantDto, locDruck, LocaleFac.BELEGART_ANGEBOT));

				if (ansprechpartnerDtoRechnungsadresse != null) {
					parameter.put("P_ANSPRECHPARTNER_KUNDERECHNUNGSADRESSE_ADRESSBLOCK",
							getPartnerFac().formatFixAnredeTitelName2Name1FuerAdresskopf(
									ansprechpartnerDtoRechnungsadresse.getPartnerDto(), locDruck, null));
				}

			}

			// PJ19814
			parameter.put("P_ZUSCHLAG_INKLUSIVE", Helper.short2Boolean(kundeDto.getBZuschlagInklusive()));

			
			parameter.put("P_ZUSATZFUNKTION_SCHNELLERFASSUNG",getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_MV_AG_SCHNELLERFASSUNG,
					theClientDto));
		
			
			ParametermandantDto parameterAbweichung = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_RECHNUNG, ParameterFac.PARAMETER_MAXIMALE_ABWEICHUNG_SUMMENAUSDRUCK);
			final Double dAbweichung = (Double) parameterAbweichung.getCWertAsObject();
			parameter.put("P_MAXIMALE_ABWEICHUNG", dAbweichung);

			if (kundeDto.getIidDebitorenkonto() != null) {
				parameter.put("P_DEBITORENKONTO",
						getFinanzFac().kontoFindByPrimaryKey(kundeDto.getIidDebitorenkonto()).getCNr());
			}
			ParametermandantDto parameterWerbeabgabe = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_WERBEABGABE_PROZENT);
			parameter.put("P_WERBEABGABE_PROZENT", (Integer) parameterWerbeabgabe.getCWertAsObject());
			ParametermandantDto parameterWerbeabgabeArtikel = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_WERBEABGABE_ARTIKEL);
			parameter.put("P_WERBEABGABE_ARTIKEL", parameterWerbeabgabeArtikel.getCWert());
			parameter.put("P_RECHNUNGSDRUCKMITRABATT", Helper.short2Boolean(kundeDto.getBRechnungsdruckmitrabatt()));
			PersonalDto vertreterDto = null;
			String cVertreterAnredeShort = null;

			if (angebotDto.getPersonalIIdVertreter() != null) {
				vertreterDto = getPersonalFac().personalFindByPrimaryKey(angebotDto.getPersonalIIdVertreter(),
						theClientDto);

				if (vertreterDto != null) {
					// Vertreter Kontaktdaten
					String sVertreterEmail = vertreterDto.getCEmail();

					String sVertreterFaxDirekt = vertreterDto.getCDirektfax();

					String sVertreterFax = vertreterDto.getCFax();

					String sVertreterTelefon = vertreterDto.getCTelefon();

					parameter.put(LPReport.P_VERTRETEREMAIL, sVertreterEmail != null ? sVertreterEmail : "");
					if (sVertreterFaxDirekt != null && sVertreterFaxDirekt != "") {
						parameter.put(LPReport.P_VERTRETERFAX, sVertreterFaxDirekt);
					} else {
						parameter.put(LPReport.P_VERTRETERFAX, sVertreterFax != null ? sVertreterFax : "");
					}
					parameter.put(LPReport.P_VERTRETERTELEFON, sVertreterTelefon != null ? sVertreterTelefon : "");

				}

				cVertreterAnredeShort = getPersonalFac()
						.personalFindByPrimaryKey(angebotDto.getPersonalIIdVertreter(), theClientDto).getPartnerDto()
						.formatFixName2Name1();

				parameter.put(LPReport.P_VERTRETER_TELEFON_FIRMA_MIT_DW,
						getPartnerFac().enrichNumber(mandantDto.getPartnerIId(), PartnerFac.KOMMUNIKATIONSART_TELEFON,
								theClientDto, vertreterDto.getCTelefon(), false));

			}

			parameter.put("Vertreteranrede", cVertreterAnredeShort);
			parameter.put("Lieferart", getLocaleFac().lieferartFindByIIdLocaleOhneExc(angebotDto.getLieferartIId(),
					locDruck, theClientDto));

			parameter.put("P_LIEFERART_ORT", getLieferartOrt(angebotDto.getLieferartIId(),
					angebotDto.getCLieferartort(), kundeDtoLieferadresse.getPartnerDto(), theClientDto));

			parameter.put("Anfragedatum", Helper.formatDatum(angebotDto.getTAnfragedatum(), locDruck));

			parameter.put("P_ALLGEMEINERRABATT", angebotDto.getFAllgemeinerRabattsatz());
			parameter.put("P_ALLGEMEINERRABATT_STRING",
					Helper.formatZahl(angebotDto.getFAllgemeinerRabattsatz(), locDruck));
			parameter.put("P_PROJEKT_RABATT", angebotDto.getFProjektierungsrabattsatz());
			parameter.put("P_PROJEKT_RABATT_STRING",
					Helper.formatZahl(angebotDto.getFProjektierungsrabattsatz(), locDruck));

			parameter.put("Belegkennung", getAngebotFac().getAngebotkennung(iIdAngebotI, theClientDto));

			parameter.put("Projekt", angebotDto.getCBez());

			parameter.put("P_PROJEKT_I_ID", angebotDto.getProjektIId());

			if (angebotDto.getProjektIId() != null) {
				ProjektDto pjDto = getProjektFac().projektFindByPrimaryKey(angebotDto.getProjektIId());
				parameter.put("P_PROJEKTNUMMER", pjDto.getCNr());
			}

			parameter.put("Kundenanfrage", angebotDto.getCKundenanfrage());

			parameter.put("P_KOMMISSION", angebotDto.getCKommission());

			String cBriefanrede = "";

			if (ansprechpartnerDto != null) {
				cBriefanrede = getPartnerServicesFac().getBriefanredeFuerBeleg(angebotDto.getAnsprechpartnerIIdKunde(),
						kundeDto.getPartnerIId(), locDruck, theClientDto);
			} else {
				// neutrale Anrede

				cBriefanrede = getBriefanredeNeutralOderPrivatperson(kundeDto.getPartnerIId(), locDruck, theClientDto);
			}

			parameter.put("Briefanrede", cBriefanrede);

			PersonalDto personalBenutzer = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(),
					theClientDto);
			PersonalDto oPersonalAnleger = getPersonalFac().personalFindByPrimaryKey(angebotDto.getPersonalIIdAnlegen(),
					theClientDto);
			parameter.put("Unserzeichen",
					Helper.getKurzzeichenkombi(personalBenutzer.getCKurzzeichen(), oPersonalAnleger.getCKurzzeichen()));
			if (angebotDto.getKostenstelleIId() != null) {
				KostenstelleDto kostenstelleDto = getSystemFac()
						.kostenstelleFindByPrimaryKey(angebotDto.getKostenstelleIId());
				parameter.put(LPReport.P_KOSTENSTELLE, kostenstelleDto.getCNr());
			}

			parameter.put("Belegdatum", Helper.formatDatum(angebotDto.getTBelegdatum(), locDruck));
			parameter.put("Kopftext", Helper.formatStyledTextForJasper(sKopftext));

			// die Kommunikationsinformation des Kunden
			Integer ansprechpartnerIId = null;
			if (ansprechpartnerDto != null) {
				ansprechpartnerIId = ansprechpartnerDto.getIId();
			}

			String sEmail = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(ansprechpartnerIId,
					kundeDto.getPartnerDto(), PartnerFac.KOMMUNIKATIONSART_EMAIL, theClientDto.getMandant(),
					theClientDto);
			String sFax = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(ansprechpartnerIId,
					kundeDto.getPartnerDto(), PartnerFac.KOMMUNIKATIONSART_FAX, theClientDto.getMandant(),
					theClientDto);
			String sTelefon = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(ansprechpartnerIId,
					kundeDto.getPartnerDto(), PartnerFac.KOMMUNIKATIONSART_TELEFON, theClientDto.getMandant(),
					theClientDto);

			parameter.put(LPReport.P_ANSPRECHPARTNEREMAIL, sEmail != null ? sEmail : "");
			parameter.put(LPReport.P_ANSPRECHPARTNERFAX, sFax != null ? sFax : "");
			parameter.put(LPReport.P_ANSPRECHPARTNERTELEFON, sTelefon != null ? sTelefon : "");

			parameter = uebersteuereAnsprechpartnerKommmunikationsdaten(theClientDto, kundeDto.getPartnerDto(),
					parameter);

			/*
			 * // das Andrucken der gesammelten Mwstinformationen steuern StringBuffer
			 * sbMwstsatz = new StringBuffer(); StringBuffer sbSummePositionsbetrag = new
			 * StringBuffer(); StringBuffer sbWaehrung = new StringBuffer(); StringBuffer
			 * sbSummeMwstbetrag = new StringBuffer();
			 * 
			 * // UW 03.03.06: Die folgenden Informationen erscheinen am Druck, mit //
			 * diesen // Werten wird intern nicht mehr weitergerechnet -> daher auf 2 //
			 * Stellen runden BigDecimal nAngebotssendbetragMitMwst =
			 * Helper.rundeKaufmaennisch( angebotDto.getNGesamtwertinbelegwaehrung(), 2);
			 * 
			 * boolean bHatMwstWerte = false;
			 * 
			 * for (Iterator<Object> iter = mwstMap.keySet().iterator(); iter .hasNext();) {
			 * Integer key = (Integer) iter.next(); // IId des Mwstsatzes MwstsatzReportDto
			 * mwstsatzReportDto = (MwstsatzReportDto) mwstMap .get(key); // Summen der
			 * Mwstbetraege if (mwstsatzReportDto != null &&
			 * mwstsatzReportDto.getNSummeMwstbetrag() .doubleValue() > 0.0) { MwstsatzDto
			 * mwstsatzDto = getMandantFac() .mwstsatzFindByPrimaryKey(key, theClientDto);
			 * // MR: FIX, statt festverdrahtetem UST verwende // Localeabh&auml;ngigen Wert
			 * lp.ust sbMwstsatz.append(getTextRespectUISpr("lp.ust",
			 * theClientDto.getMandant(), locDruck)); sbMwstsatz.append(": ");
			 * sbMwstsatz.append(Helper.formatZahl(mwstsatzDto .getFMwstsatz(), 2,
			 * locDruck)); sbMwstsatz.append(" % "); sbMwstsatz.append(
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
			 * .add(Helper.rundeKaufmaennisch(mwstsatzReportDto .getNSummeMwstbetrag(), 2));
			 * 
			 * bHatMwstWerte = true; } }
			 * 
			 * if (bHatMwstWerte) { // die letzten \n wieder loeschen
			 * sbMwstsatz.delete(sbMwstsatz.length() - 1, sbMwstsatz.length());
			 * sbSummePositionsbetrag.delete( sbSummePositionsbetrag.length() - 1,
			 * sbSummePositionsbetrag.length()); sbWaehrung.delete(sbWaehrung.length() - 1,
			 * sbWaehrung.length()); sbSummeMwstbetrag.delete(sbSummeMwstbetrag.length() -
			 * 1, sbSummeMwstbetrag.length()); }
			 * 
			 * parameter.put("P_MWST_TABELLE_LINKS", sbMwstsatz.toString());
			 * parameter.put("P_MWST_TABELLE_SUMME_POSITIONEN",
			 * sbSummePositionsbetrag.toString()); parameter.put("P_MWST_TABELLE_WAEHRUNG",
			 * sbWaehrung.toString()); parameter .put("P_MWST_TABELLE_RECHTS",
			 * sbSummeMwstbetrag.toString()); parameter.put("P_ANGEBOTSENDBETRAGMITMWST",
			 * nAngebotssendbetragMitMwst);
			 */
			Object sMwstsatz[] = null;
			sMwstsatz = getBelegVerkaufFac().getMwstTabelle(mwstMap, angebotDto, locDruck, null, theClientDto);
			parameter.put("P_MWST_TABELLE_LINKS", sMwstsatz[LPReport.MWST_TABELLE_LINKS]);
			parameter.put("P_MWST_TABELLE_SUMME_POSITIONEN", sMwstsatz[LPReport.MWST_TABELLE_SUMME_POSITIONEN]);
			parameter.put("P_MWST_TABELLE_WAEHRUNG", sMwstsatz[LPReport.MWST_MWST_TABELLE_WAEHRUNG]);
			parameter.put("P_MWST_TABELLE_RECHTS", sMwstsatz[LPReport.MWST_TABELLE_RECHTS]);
			parameter.put("P_ANGEBOTSENDBETRAGMITMWST", (BigDecimal) sMwstsatz[LPReport.MWST_ENDBETRAGMITMWST]);

			// Lieferzeit in hinterlegter Einheit
			Integer iiLieferzeit = getAngebotServiceFac().getLieferzeitInAngeboteinheit(angebotDto.getIId(), null,
					angebotDto.getAngeboteinheitCNr(), theClientDto);

			parameter.put("P_ANGEBOTEINHEIT",
					getSystemFac().formatEinheit(angebotDto.getAngeboteinheitCNr(), locDruck, theClientDto));
			parameter.put("P_LIEFERZEIT", iiLieferzeit);

			// die Lieferart
			parameter.put("P_LIEFERART", getLocaleFac().lieferartFindByIIdLocaleOhneExc(angebotDto.getLieferartIId(),
					locDruck, theClientDto));

			// das Zahlungsziel
			parameter.put("P_ZAHLUNGSZIEL", getMandantFac()
					.zahlungszielFindByIIdLocaleOhneExc(angebotDto.getZahlungszielIId(), locDruck, theClientDto));

			// Spediteur
			SpediteurDto spediteurDto = getMandantFac().spediteurFindByPrimaryKey(angebotDto.getSpediteurIId());
			parameter.put("P_SPEDITEUR", spediteurDto.getCNamedesspediteurs());

			if (spediteurDto.getPartnerIId() != null) {
				PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(spediteurDto.getPartnerIId(),
						theClientDto);

				AnsprechpartnerDto ansprechpartnerDtoSpediteur = null;

				if (spediteurDto.getAnsprechpartnerIId() != null) {
					ansprechpartnerDtoSpediteur = getAnsprechpartnerFac()
							.ansprechpartnerFindByPrimaryKey(spediteurDto.getAnsprechpartnerIId(), theClientDto);
				}

				parameter.put("P_SPEDITEUR_ADRESSBLOCK",
						formatAdresseFuerAusdruck(partnerDto, ansprechpartnerDtoSpediteur, mandantDto, locDruck));
			}

			// Konsistenzpruefung: Die Summe der einzelnen Positionen muss den
			// Angebotswert ergeben
			parameter.put("P_ANGEBOTSWERT", angebotDto.getNGesamtwertinbelegwaehrung());

			parameter.put("P_ANGEBOTSGUELTIGKEIT", angebotDto.getTAngebotsgueltigkeitbis());

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
					if (cTextNachEndsumme != null && cTextNachEndsumme.length() > 0) {
						cTextNachEndsumme += "\n";
					}

					cTextNachEndsumme += dataRow[AngebotReportFac.REPORT_ANGEBOT_TEXTNACHENDSUMME];
				}
			}

			cTextNachEndsumme = cTextNachEndsumme == "" ? null : cTextNachEndsumme;

			if (cTextNachEndsumme != null && cTextNachEndsumme.length() > 0) {
				buff.append(cTextNachEndsumme).append("\n\n");
				parameter.put("P_TEXTNACHENDSUMME", Helper.formatStyledTextForJasper(cTextNachEndsumme));
			}

			// Externer Kommentar
			if (angebotDto.getXExternerkommentar() != null && angebotDto.getXExternerkommentar().length() > 0) {
				parameter.put(P_EXTERNERKOMMENTAR,
						Helper.formatStyledTextForJasper(angebotDto.getXExternerkommentar()));
				buff.append(angebotDto.getXExternerkommentar()).append("\n\n");
			}

			// Eigentumsvorbehalt
			MediastandardDto mediastandardDto = getMediaFac().mediastandardFindByCNrDatenformatCNrMandantCNr(
					MediaFac.MEDIASTANDARD_EIGENTUMSVORBEHALT, MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML,
					theClientDto.getMandant(), kundeDto.getPartnerDto().getLocaleCNrKommunikation(), theClientDto);

			if (mediastandardDto != null && mediastandardDto.getOMediaImage() != null
					&& mediastandardDto.getOMediaImage().length > 0) {
				parameter.put(LPReport.P_EIGENTUMSVORBEHALT,
						Helper.formatStyledTextForJasper(mediastandardDto.getOMediaText()));
				buff.append(mediastandardDto.getOMediaText()).append("\n\n");
			}

			// Lieferbedingungen
			mediastandardDto = getMediaFac().mediastandardFindByCNrDatenformatCNrMandantCNr(
					MediaFac.MEDIASTANDARD_LIEFERBEDINGUNGEN, MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML,
					theClientDto.getMandant(), kundeDto.getPartnerDto().getLocaleCNrKommunikation(), theClientDto);

			if (mediastandardDto != null && mediastandardDto.getOMediaImage() != null
					&& mediastandardDto.getOMediaImage().length > 0) {
				parameter.put(LPReport.P_LIEFERBEDINGUNGEN,
						Helper.formatStyledTextForJasper(mediastandardDto.getOMediaText()));
				buff.append(mediastandardDto.getOMediaText()).append("\n\n");
			}

			// Fusstext andrucken
			if (sFusstext != null) {
				parameter.put("P_FUSSTEXT", Helper.formatStyledTextForJasper(sFusstext));
				buff.append(sFusstext).append("\n\n");
			}

			StringBuffer buffVertreter = new StringBuffer();
			// Anrede des Mandanten
			String sMandantAnrede = mandantDto.getPartnerDto().formatFixName1Name2();
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
				String sVertreterUFTitelName2Name1 = vertreterDto.formatFixUFTitelName2Name1();

				parameter.put(P_VERTRETER,
						getPersonalFac().formatAnrede(vertreterDto.getPartnerDto(), locDruck, theClientDto));

				buff.append(sVertreterUFTitelName2Name1);
				buffVertreter.append(sVertreterUFTitelName2Name1);
				parameter.put(P_VERTRETER_UNTERSCHRIFTSFUNKTION_UND_NAME, sVertreterUFTitelName2Name1);

				if (vertreterDto.getCUnterschriftstext() != null && vertreterDto.getCUnterschriftstext().length() > 0) {
					String sUnterschriftstext = vertreterDto.getCUnterschriftstext();
					buff.append("\n").append(sUnterschriftstext);
					buffVertreter.append("\n").append(sUnterschriftstext);
					parameter.put(P_VERTRETER_UNTERSCHRIFTSTEXT, sUnterschriftstext);
				}
			}

			if (angebotDto.getPersonalIIdVertreter2() != null) {
				PersonalDto vertreterDto2 = getPersonalFac()
						.personalFindByPrimaryKey(angebotDto.getPersonalIIdVertreter2(), theClientDto);

				String sVertreterUFTitelName2Name1 = vertreterDto2.formatFixUFTitelName2Name1();

				parameter.put("P_VERTRETER2",
						getPersonalFac().formatAnrede(vertreterDto2.getPartnerDto(), locDruck, theClientDto));

				buff.append(sVertreterUFTitelName2Name1);
				buffVertreter.append(sVertreterUFTitelName2Name1);
				parameter.put("P_VERTRETER2_UNTERSCHRIFTSFUNKTION_UND_NAME", sVertreterUFTitelName2Name1);

				if (vertreterDto2.getCUnterschriftstext() != null
						&& vertreterDto2.getCUnterschriftstext().length() > 0) {
					String sUnterschriftstext = vertreterDto2.getCUnterschriftstext();
					buff.append("\n").append(sUnterschriftstext);
					buffVertreter.append("\n").append(sUnterschriftstext);
					parameter.put("P_VERTRETER2_UNTERSCHRIFTSTEXT", sUnterschriftstext);
				}

				String sVertreterEmail = vertreterDto2.getCEmail();

				String sVertreterFaxDirekt = vertreterDto2.getCDirektfax();

				String sVertreterFax = vertreterDto2.getCFax();

				String sVertreterTelefon = vertreterDto2.getCTelefon();

				parameter.put("P_VERTRETEREMAIL2", sVertreterEmail != null ? sVertreterEmail : "");
				if (sVertreterFaxDirekt != null && sVertreterFaxDirekt != "") {
					parameter.put("P_VERTRETERFAX2", sVertreterFaxDirekt);
				} else {
					parameter.put("P_VERTRETERFAX2", sVertreterFax != null ? sVertreterFax : "");
				}
				parameter.put("P_VERTRETERTELEFON2", sVertreterTelefon != null ? sVertreterTelefon : "");

				parameter.put(LPReport.P_VERTRETER2_TELEFON_FIRMA_MIT_DW,
						getPartnerFac().enrichNumber(mandantDto.getPartnerIId(), PartnerFac.KOMMUNIKATIONSART_TELEFON,
								theClientDto, vertreterDto2.getCTelefon(), false));

			}

			parameter.put("P_SUMMARY", Helper.formatStyledTextForJasper(buff.toString()));

			parameter.put(P_DRUCKTYPE, sDrucktype);

			parameter.put("P_AENDERUNGSANGEBOT", new Boolean(angebotDto.getTAenderungsangebot() != null));
			parameter.put("P_AENDERUNGSANGEBOTSDATUM", angebotDto.getTAenderungsangebot());
			parameter.put("P_AENDERUNGSANGEBOT_VERSION", angebotDto.getIVersion());

			// die Anzahl der Exemplare ist 1 + Anzahl der Kopien
			int iAnzahlExemplare = 1;

			if (iAnzahlKopienI != null && iAnzahlKopienI.intValue() > 0) {
				iAnzahlExemplare += iAnzahlKopienI.intValue();
			}

			aJasperPrint = new JasperPrintLP[iAnzahlExemplare];

			// PJ21318
			JasperPrintLP jasperPrintLP_AGB = getSystemReportFac().getABGReport(LocaleFac.BELEGART_ANGEBOT, locDruck,
					theClientDto);
			if (jasperPrintLP_AGB != null) {
				parameter.put("P_SEITENANZAHL_AGB", jasperPrintLP_AGB.getPrint().getPages().size());
			}

			for (int iKopieNummer = 0; iKopieNummer < iAnzahlExemplare; iKopieNummer++) {
				// jede Kopie bekommt eine Kopienummer, das Original bekommt
				// keine
				if (iKopieNummer > 0) {
					parameter.put(LPReport.P_KOPIE_NUMMER, new Integer(iKopieNummer));
				}

				// Index zuruecksetzen !!!
				index = -1;
				String sReportToUse = AngebotReportFac.REPORT_ANGEBOT;
				if (sReportname != null) {
					sReportToUse = sReportname;
				}

				initJRDS(parameter, AngebotReportFac.REPORT_MODUL, sReportToUse, theClientDto.getMandant(), locDruck,
						theClientDto, bMitLogo.booleanValue(), angebotDto.getKostenstelleIId());

				aJasperPrint[iKopieNummer] = Helper.addReport2Report(getReportPrint(), jasperPrintLP_AGB);
			}

			PrintInfoDto values = getJCRDocFac().getPathAndPartnerAndTable(angebotDto.getIId(),
					QueryParameters.UC_ID_ANGEBOT, theClientDto);
			aJasperPrint[0].setOInfoForArchive(values);
			aJasperPrint[0].putAdditionalInformation(JasperPrintLP.KEY_BELEGART, LocaleFac.BELEGART_ANGEBOT);
			aJasperPrint[0].putAdditionalInformation(JasperPrintLP.KEY_BELEGIID, angebotDto.getIId());

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
	 * @param kritDtoI     die Filter- und Sortierkriterien
	 * @param theClientDto der aktuelle Benutzer
	 * @return JasperPrint der Druck
	 * @throws EJBExceptionLP Ausnahme
	 */

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAngebotAlle(ReportAngebotJournalKriterienDto kritDtoI, String erledigungsgrundCNr,
			TheClientDto theClientDto) throws EJBExceptionLP {
		JasperPrintLP oPrint = null;
		cAktuellerReport = AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ALLE;

		try {
			// reportflr: 4 die Daten des Report ueber Hibernate holen
			ArrayList<ReportAngebotpositionJournalDto> aReportDto = getListeAngebotpositionenJournal(kritDtoI,
					erledigungsgrundCNr, AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ALLE, theClientDto);

			int iAnzahlSpalten = REPORT_ANGEBOT_JOURNAL_ANZAHL_SPALTEN; // Anzahl
																		// der
																		// Spalten
																		// in
																		// der
																		// Gruppe

			data = new Object[aReportDto.size()][iAnzahlSpalten];

			// die Datenmatrix befuellen
			for (int i = 0; i < aReportDto.size(); i++) {
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANGEBOTCNR] = aReportDto.get(i).getCNr();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANGEBOTWERT] = aReportDto.get(i).getNWert();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_WAEHRUNG] = aReportDto.get(i).getWaehrungCNr();

				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_PROVISIONSEMPFAENGER_KURZZEICHEN] = aReportDto.get(i)
						.getKundeProvisionsempfaengerKurzzeichen();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_PROVISIONSEMPFAENGER_NAME] = aReportDto.get(i)
						.getKundeProvisionsempfaengerName();

				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_I_VERSION] = aReportDto.get(i).getIVersion();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_T_VERSION] = aReportDto.get(i).getTVersion();

				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_BELEGDATUM] = aReportDto.get(i).getBelegdatum();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_NACHFASSTERMIN] = aReportDto.get(i).getNachfasstermin();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDECNAME1] = aReportDto.get(i).getKundeCName1();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KOSTENSTELLECNR] = aReportDto.get(i)
						.getKostenstelleCNr();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_VERTRETERCNAME1] = aReportDto.get(i)
						.getVertreterCName1();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_PROJEKTBEZ] = aReportDto.get(i).getProejktBez();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_REALISIERUNGSTERMIN] = aReportDto.get(i)
						.getRealisierungstermin();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ARTIKELCNR] = aReportDto.get(i).getArtikelCNr();

				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_LIEFERZEIT] = aReportDto.get(i).getLieferzeit();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_EINHEIT_LIEFERZEIT] = aReportDto.get(i)
						.getEinheitLieferzeit();

				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ARTIKELBEZ] = aReportDto.get(i).getArtkelCBez();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ARTIKELMENGE] = aReportDto.get(i).getNMenge();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ARTIKELEINHEIT] = aReportDto.get(i).getEinheitCNr();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ARTIKELPREIS] = aReportDto.get(i).getNPreis();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANGEBOTERLEDIGUNGSGRUND] = aReportDto.get(i)
						.getAngeboterledigungsgrundCNr();

				AngebotDto angebotDto = getAngebotFac().angebotFindByPrimaryKey(aReportDto.get(i).getIId(),
						theClientDto);
				angebotDto.setIId(aReportDto.get(i).getIId());

				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_INTERNERKOMMENTAR] = angebotDto.getXInternerkommentar();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_EXTERNERKOMMENTAR] = angebotDto.getXExternerkommentar();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ERLEDIGUNGSGRUND_AB_NR] = aReportDto.get(i)
						.getErledigungsgrundABNr();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDECNAME2] = aReportDto.get(i).getKundeCName2();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDECNAME3] = aReportDto.get(i).getKundeCName3();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDESTRASSE] = aReportDto.get(i).getKundeStrasse();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDEPLZ] = aReportDto.get(i).getKundePlz();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDEORT] = aReportDto.get(i).getKundeOrt();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDELKZ] = aReportDto.get(i).getKundeLkz();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDETELEFON] = aReportDto.get(i).getKundeTelefon();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDEFAX] = aReportDto.get(i).getKundeFax();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDEEMAIL] = aReportDto.get(i).getKundeEmail();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDEHOMEPAGE] = aReportDto.get(i).getKundeHomepage();

				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_VORNAME] = aReportDto.get(i)
						.getAnsprechpartnerVorname();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_NACHNAME] = aReportDto.get(i)
						.getAnsprechpartnerNachname();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_TITEL] = aReportDto.get(i)
						.getAnsprechpartnerTitel();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_ANREDE] = aReportDto.get(i)
						.getAnsprechpartnerAnrede();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_TELEFON] = aReportDto.get(i)
						.getAnsprechpartnerTelefon();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_TELEFONDW] = aReportDto.get(i)
						.getAnsprechpartnerTelefonDw();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_FAX] = aReportDto.get(i)
						.getAnsprechpartnerFax();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_FAXDW] = aReportDto.get(i)
						.getAnsprechpartnerFaxDw();
				data[i][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_EMAIL] = aReportDto.get(i)
						.getAnsprechpartnerEmail();

			}

			// Erstellung des Report
			HashMap<String, Object> parameter = new HashMap<String, Object>();

			// die Parameter dem Report uebergeben
			parameter.put(LPReport.P_SORTIERUNG, buildSortierungAngebotJournal(kritDtoI, theClientDto));
			parameter.put(LPReport.P_FILTER, buildFilterAngebotJournal(kritDtoI, theClientDto));

			// die Parameter zur Bildung von Zwischensummen uebergeben
			if (kritDtoI.bSortiereNachKostenstelle) {
				parameter.put(LPReport.P_SORTIERENACHKOSTENSTELLE, new Boolean(true));
			} else {
				parameter.put(LPReport.P_SORTIERENACHKOSTENSTELLE, new Boolean(false));
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

			parameter.put("P_TITLE",
					getTextRespectUISpr("angb.print.alleangebote", theClientDto.getMandant(), theClientDto.getLocUi()));
			parameter.put(P_MANDANTWAEHRUNG, theClientDto.getSMandantenwaehrung());

			initJRDS(parameter, AngebotReportFac.REPORT_MODUL, AngebotReportFac.REPORT_ANGEBOT_JOURNAL,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

			oPrint = getReportPrint();
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, new Exception(t));
		}

		return oPrint;
	}

	/**
	 * reportflr: 2 Diese Methode liefert eine Liste von allen Angebotspositionen
	 * eines Mandanten, die nach den eingegebenen Kriterien des Benutzers
	 * zusammengestellt wird. <br>
	 * Achtung: Hibernate verwendet lazy initialization, d.h. der Zugriff auf
	 * Collections muss innerhalb der Session erfolgen.
	 * 
	 * @param kritDtoI         die Filter- und Sortierkriterien
	 * @param cAktuellerReport wird fuer alle und offene Angebote verwendet
	 * @param theClientDto     der aktuelle Benutzer
	 * @return ReportAngebotpositionDto[] die Liste aller Positionen.
	 * @throws EJBExceptionLP Ausnahme
	 */
	private ArrayList<ReportAngebotpositionJournalDto> getListeAngebotpositionenJournal(
			ReportAngebotJournalKriterienDto kritDtoI, String erledigungsgrundCNr, String cAktuellerReport,
			TheClientDto theClientDto) throws EJBExceptionLP {

		ArrayList<ReportAngebotpositionJournalDto> aResult = new ArrayList<ReportAngebotpositionJournalDto>();
		Session session = null;
		// es gilt das Locale des Benutzers
		Locale locDruck = theClientDto.getLocUi();
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();

			// Criteria duerfen keine Texts oder Blobs enthalten!

			// Criteria anlegen fuer alle referenzierten Objekte anlegen
			Criteria crit = session.createCriteria(FLRAngebotpositionReport.class);
			Criteria critAngebot = crit.createCriteria(AngebotpositionFac.FLR_ANGEBOTPOSITION_FLRANGEBOT);

			Criteria critKunde = critAngebot.createCriteria("flrkunde_rechnungsadresse");

			// Einschraenken nach Mandant
			critAngebot.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));

			// Einschraenken nach Status
			if (cAktuellerReport.equals(AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ALLE)) {
				if (kritDtoI.getBNurErledigteAngebote()) {
					critAngebot.add(Restrictions.eq(AngebotFac.FLR_ANGEBOT_ANGEBOTSTATUS_C_NR,
							AngebotServiceFac.ANGEBOTSTATUS_ERLEDIGT));
					if (erledigungsgrundCNr != null) {
						critAngebot.add(Restrictions.eq(AngebotFac.FLR_ANGEBOT_ANGEBOTERLEDIGUNGSGRUND_C_NR,
								erledigungsgrundCNr));
					}
				} else {
					// PJ 07/0011040
					/*
					 * critAngebot.add(Restrictions.ne( AngebotFac.FLR_ANGEBOT_ANGEBOTSTATUS_C_NR,
					 * AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT));
					 */
					critAngebot.add(Restrictions.ne(AngebotFac.FLR_ANGEBOT_ANGEBOTSTATUS_C_NR,
							AngebotServiceFac.ANGEBOTSTATUS_STORNIERT));
				}
			} else if (cAktuellerReport.equals(AngebotReportFac.REPORT_ANGEBOT_JOURNAL_OFFENE)) {
				critAngebot.add(Restrictions.eq(AngebotFac.FLR_ANGEBOT_ANGEBOTSTATUS_C_NR,
						AngebotServiceFac.ANGEBOTSTATUS_OFFEN));
			}

			// UW 22.03.06 alle nicht alternativen Positionen mit positiver
			// Menge
			crit.add(Restrictions.isNotNull(AngebotpositionFac.FLR_ANGEBOTPOSITION_N_MENGE));
			crit.add(Restrictions.gt(AngebotpositionFac.FLR_ANGEBOTPOSITION_N_MENGE, new BigDecimal(0)));
			crit.add(Restrictions.eq(AngebotpositionFac.FLR_ANGEBOTPOSITION_B_ALTERNATIVE, new Short((short) 0)));

			// Einschraenkung nach einer bestimmten Kostenstelle
			if (kritDtoI.kostenstelleIId != null) {
				critAngebot.add(Restrictions.eq(AngebotFac.FLR_ANGEBOT_KOSTENSTELLE_I_ID, kritDtoI.kostenstelleIId));
			}

			// Einschraenkung nach einem bestimmten Kunden
			if (kritDtoI.kundeIId != null) {
				critAngebot.add(Restrictions.eq(AngebotFac.FLR_ANGEBOT_KUNDE_I_ID_ANGEBOTSADRESSE, kritDtoI.kundeIId));
			}

			// Einschraenkung nach einem bestimmten Vertreter
			if (kritDtoI.vertreterIId != null) {
				critAngebot.add(Restrictions.eq(AngebotFac.FLR_ANGEBOT_VERTRETER_I_ID, kritDtoI.vertreterIId));
			}

			if (kritDtoI.provisionsempfaengerIId != null) {
				critKunde.add(Restrictions.eq(KundeFac.FLR_PERSONAL + ".i_id", kritDtoI.provisionsempfaengerIId));
			}

			/*
			 * // Belegdatum von bis: flrangebotposition > flrangebot.t_belegdatum if
			 * (kritDtoI.dVon != null) { critAngebot.add(Restrictions.ge(
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
				critAngebot.add(Restrictions.between(AngebotFac.FLR_ANGEBOT_T_BELEGDATUM, von, bis));
			}

			// Einschraenkung nach Belegnummer von - bis
			LpBelegnummerFormat f = getBelegnummerGeneratorObj().getBelegnummernFormat(theClientDto.getMandant());

			Integer iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(theClientDto.getMandant());

			String sMandantKuerzel = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_BELEGNUMMER_MANDANTKENNUNG).getCWert();

			if (kritDtoI.sBelegnummerVon != null) {
				critAngebot.add(Restrictions.ge("c_nr", HelperServer.getBelegnummernFilterForHibernateCriterias(f,
						iGeschaeftsjahr, sMandantKuerzel, kritDtoI.sBelegnummerVon)));
			}

			if (kritDtoI.sBelegnummerBis != null) {
				critAngebot.add(Restrictions.le("c_nr", HelperServer.getBelegnummernFilterForHibernateCriterias(f,
						iGeschaeftsjahr, sMandantKuerzel, kritDtoI.sBelegnummerBis)));
			}

			// Sortierung nach Kostenstelle ist immer die erste Sortierung
			if (kritDtoI.bSortiereNachKostenstelle) {
				critAngebot.createCriteria(AngebotFac.FLR_ANGEBOT_FLRKOSTENSTELLE).addOrder(Order.asc("c_nr"));
			}

			// Sortierung nach Kunde, eventuell innerhalb der Kostenstelle
			if (kritDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				critAngebot.createCriteria(AngebotFac.FLR_ANGEBOT_FLRKUNDE).createCriteria(KundeFac.FLR_PARTNER)
						.addOrder(Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
			}

			// Sortierung nach Vertreter, eventuell innerhalb der Kostenstelle
			if (kritDtoI.iSortierung == ReportAngebotJournalKriterienDto.KRIT_SORT_NACH_VERTRETER) {
				critAngebot.createCriteria(AngebotFac.FLR_ANGEBOT_FLRVERTRETER)
						.addOrder(Order.asc(PersonalFac.FLR_PERSONAL_C_KURZZEICHEN));
			}

			boolean bOrderCNrHinzugefuegt = false;
			if (kritDtoI.iSortierung == ReportAngebotJournalKriterienDto.KRIT_SORT_NACH_BELEGNUMMER) {
				critAngebot.addOrder(Order.asc("c_nr"));
				bOrderCNrHinzugefuegt = true;
			}

			// PJ20303
			if (cAktuellerReport.equals(AngebotReportFac.REPORT_ANGEBOT_JOURNAL_OFFENE)) {
				if (kritDtoI.isBSortiertNachNachfassterminNachrangig()) {
					critAngebot.addOrder(Order.asc(AngebotFac.FLR_ANGEBOT_T_NACHFASSTERMIN));
				}
			}

			if (kritDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PROVISIONSEMPFAENGER) {

				Criteria kritProvisionsempf = critKunde.createCriteria("flrpersonal");

				kritProvisionsempf.addOrder(Order.asc(PersonalFac.FLR_PERSONAL_C_KURZZEICHEN));
				kritProvisionsempf.createCriteria("flrpartner")
						.addOrder(Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1))
						.addOrder(Order.asc(PartnerFac.FLR_PARTNER_NAME2VORNAMEFIRMAZEILE2));
			}

			if (cAktuellerReport.equals(AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ALLE)) {
				if (kritDtoI.getBNurErledigteAngebote()) {
					critAngebot.addOrder(Order.asc(AngebotFac.FLR_ANGEBOT_ANGEBOTERLEDIGUNGSGRUND_C_NR));
				} else {
					if (bOrderCNrHinzugefuegt == false) {
						critAngebot.addOrder(Order.asc("c_nr"));
					}
				}
			} else {
				// es wird in jedem Fall nach der Belegnummer sortiert
				if (bOrderCNrHinzugefuegt == false) {
					critAngebot.addOrder(Order.asc("c_nr"));
				}
			}

			List<?> list = crit.list();
			int iIndex = 0;
			Iterator<?> it = list.iterator();

			while (it.hasNext()) {
				FLRAngebotpositionReport flrangebotposition = (FLRAngebotpositionReport) it.next();
				FLRAngebot flrangebot = flrangebotposition.getFlrangebot();
				FLRArtikel flrartikel = flrangebotposition.getFlrartikel();

				ReportAngebotpositionJournalDto reportDto = new ReportAngebotpositionJournalDto();

				int iEintragGefunden = -1;
				if (!kritDtoI.getBMitDetails()) {
					for (int i = 0; i < aResult.size(); i++) {
						if (aResult.get(i).getCNr().equals(flrangebot.getC_nr())) {
							reportDto = aResult.get(i);
							iEintragGefunden = i;
						}
					}
				}

				reportDto.setIId(flrangebot.getI_id());
				reportDto.setCNr(flrangebot.getC_nr());
				reportDto.setWaehrungCNr(flrangebot.getWaehrung_c_nr_angebotswaehrung());
				reportDto.setBelegdatum(flrangebot.getT_belegdatum());
				reportDto.setNachfasstermin(flrangebot.getT_nachfasstermin());

				if (flrangebot.getFlrkunde_rechnungsadresse().getFlrpersonal() != null) {
					reportDto.setKundeProvisionsempfaengerName(HelperServer
							.formatPersonAusFLRPErsonal(flrangebot.getFlrkunde_rechnungsadresse().getFlrpersonal()));
					reportDto.setKundeProvisionsempfaengerKurzzeichen(
							flrangebot.getFlrkunde_rechnungsadresse().getFlrpersonal().getC_kurzzeichen());
				}

				if (flrangebot.getAngeboterledigungsgrund_c_nr() != null) {
					AngeboterledigungsgrundDto angeboterledigungsgrundDto = getAngebotServiceFac()
							.angeboterledigungsgrundFindByPrimaryKey(flrangebot.getAngeboterledigungsgrund_c_nr(),
									theClientDto);
					reportDto.setAngeboterledigungsgrundCNr(angeboterledigungsgrundDto.getBezeichnung());
				}
				reportDto.setKundeCName1(flrangebot.getFlrkunde().getFlrpartner().getC_name1nachnamefirmazeile1());
				reportDto.setKundeCName2(flrangebot.getFlrkunde().getFlrpartner().getC_name2vornamefirmazeile2());
				reportDto.setKundeCName3(flrangebot.getFlrkunde().getFlrpartner().getC_name3vorname2abteilung());
				reportDto.setKundeStrasse(flrangebot.getFlrkunde().getFlrpartner().getC_strasse());
				if (flrangebot.getFlrkunde().getFlrpartner().getFlrlandplzort() != null) {
					reportDto.setKundePlz(flrangebot.getFlrkunde().getFlrpartner().getFlrlandplzort().getC_plz());
					reportDto.setKundeLkz(
							flrangebot.getFlrkunde().getFlrpartner().getFlrlandplzort().getFlrland().getC_lkz());
					reportDto.setKundeOrt(
							flrangebot.getFlrkunde().getFlrpartner().getFlrlandplzort().getFlrort().getC_name());
				}

				reportDto.setKundeEmail(flrangebot.getFlrkunde().getFlrpartner().getC_email());

				reportDto.setKundeFax(flrangebot.getFlrkunde().getFlrpartner().getC_fax());

				reportDto.setKundeTelefon(flrangebot.getFlrkunde().getFlrpartner().getC_telefon());

				AngebotDto angebotDto = getAngebotFac().angebotFindByPrimaryKey(flrangebot.getI_id(), theClientDto);

				reportDto.setIVersion(angebotDto.getIVersion());
				reportDto.setTVersion(angebotDto.getTVersion());

				if (angebotDto.getAnsprechpartnerIIdKunde() != null) {
					AnsprechpartnerDto ansprechpartnerDto = getAnsprechpartnerFac()
							.ansprechpartnerFindByPrimaryKey(angebotDto.getAnsprechpartnerIIdKunde(), theClientDto);

					reportDto.setAnsprechpartnerNachname(
							ansprechpartnerDto.getPartnerDto().getCName1nachnamefirmazeile1());
					reportDto.setAnsprechpartnerVorname(
							ansprechpartnerDto.getPartnerDto().getCName2vornamefirmazeile2());
					reportDto.setAnsprechpartnerAnrede(ansprechpartnerDto.getPartnerDto().getAnredeCNr());
					reportDto.setAnsprechpartnerTitel(ansprechpartnerDto.getPartnerDto().getCTitel());

					reportDto.setAnsprechpartnerFax(ansprechpartnerDto.getCDirektfax());

					reportDto.setAnsprechpartnerFaxDw(ansprechpartnerDto.getCFax());

					reportDto.setAnsprechpartnerTelefon(ansprechpartnerDto.getCHandy());

					reportDto.setAnsprechpartnerTelefonDw(ansprechpartnerDto.getCTelefon());

					reportDto.setAnsprechpartnerEmail(ansprechpartnerDto.getCEmail());

				}

				reportDto.setKostenstelleCNr(flrangebot.getFlrkostenstelle().getC_nr());
				reportDto.setVertreterCName1(
						getPersonalFac().personalFindByPrimaryKey(flrangebot.getVertreter_i_id(), theClientDto)
								.getPartnerDto().formatFixName2Name1());
				reportDto.setProjektBez(flrangebot.getC_bez());
				reportDto.setRealisierungstermin(
						Helper.formatDatum(flrangebot.getT_realisierungstermin(), theClientDto.getLocUi()));

				reportDto.setDAuftragwahrscheinlichkeit(flrangebot.getF_auftragswahrscheinlichkeit());
				reportDto.setNWert(flrangebot.getN_gesamtangebotswertinangebotswaehrung());

				if (flrangebot.getAngeboterledigungsgrund_c_nr() != null)
					if (flrangebot.getAngeboterledigungsgrund_c_nr()
							.equals(AngebotServiceFac.ANGEBOTERLEDIGUNGSGRUND_AUFTRAGERHALTEN)) {
						AuftragDto[] auftrag = getAuftragFac().auftragFindByAngebotIId(flrangebot.getI_id(),
								theClientDto);
						if (auftrag != null && auftrag.length > 0) {
							reportDto.setErledigungsgrundABNr(auftrag[0].getCNr());
						}
					}
				if (kritDtoI.getBMitDetails()) {
					// UW 08.03.06 Es kann sich um eine Ident, Handeingabe oder
					// AGStueckliste handeln
					String identCNr = null;
					String identCBez = null;

					if (flrangebotposition.getAngebotpositionart_c_nr()
							.equals(AngebotServiceFac.ANGEBOTPOSITIONART_IDENT)) {
						identCNr = flrartikel.getC_nr();
						identCBez = getArtikelFac().formatArtikelbezeichnungEinzeiligOhneExc(flrartikel.getI_id(),
								theClientDto.getLocUi());

						reportDto
								.setLieferzeit(getAngebotServiceFac().getLieferzeitInAngeboteinheit(angebotDto.getIId(),
										flrangebotposition.getI_id(), angebotDto.getAngeboteinheitCNr(), theClientDto));
						reportDto.setEinheitLieferzeit(angebotDto.getAngeboteinheitCNr());

					} else if (flrangebotposition.getAngebotpositionart_c_nr()
							.equals(AngebotServiceFac.ANGEBOTPOSITIONART_AGSTUECKLISTE)) {
						identCBez = flrangebotposition.getFlragstkl().getC_bez();
					} else if (flrangebotposition.getAngebotpositionart_c_nr()
							.equals(AngebotServiceFac.ANGEBOTPOSITIONART_HANDEINGABE)) {
						identCBez = getArtikelFac().formatArtikelbezeichnungEinzeiligOhneExc(flrartikel.getI_id(),
								theClientDto.getLocUi());
					}

					reportDto.setArtikelCNr(identCNr);
					reportDto.setArtikelCBez(identCBez);

					reportDto.setNMenge(flrangebotposition.getN_menge());
					reportDto.setEinheitCNr(flrangebotposition.getEinheit_c_nr() == null ? ""
							: flrangebotposition.getEinheit_c_nr().trim());

					// Umrechnen in Mandantenwaehrung, Positionspreise sind in
					// Belegwaehrung abgelegt
					BigDecimal nPreisAmDruck = flrangebotposition
							.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte();

					nPreisAmDruck = getBetragMalWechselkurs(nPreisAmDruck, Helper.getKehrwert(new BigDecimal(
							flrangebot.getF_wechselkursmandantwaehrungzuangebotswaehrung().doubleValue())));

					reportDto.setNPreis(nPreisAmDruck);

				}

				if (iEintragGefunden >= 0) {
					aResult.set(iEintragGefunden, reportDto);
				} else {
					aResult.add(reportDto);
				}

				iIndex++;
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, new Exception(t));
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
	 * @param kritDtoI     die Kriterien
	 * @param theClientDto der aktuelle Benutzer
	 * @return String die Filterkriterien
	 * @throws EJBExceptionLP Ausnahme
	 */
	private String buildFilterAngebotJournal(ReportAngebotJournalKriterienDto kritDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		StringBuffer buff = new StringBuffer("");

		// Belegdatum
		if (kritDtoI.dVon != null || kritDtoI.dBis != null) {
			buff.append(getTextRespectUISpr("bes.belegdatum", theClientDto.getMandant(), theClientDto.getLocUi()));
		}

		if (kritDtoI.dVon != null) {
			buff.append(" ").append(getTextRespectUISpr("lp.von", theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ").append(Helper.formatDatum(kritDtoI.dVon, theClientDto.getLocUi()));
		}

		if (kritDtoI.dBis != null) {
			buff.append(" ").append(getTextRespectUISpr("lp.bis", theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ").append(Helper.formatDatum(kritDtoI.dBis, theClientDto.getLocUi()));
		}

		// Kostenstelle
		if (kritDtoI.kostenstelleIId != null) {
			buff.append(" ")
					.append(getTextRespectUISpr("lp.kostenstelle", theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ").append(getSystemFac().kostenstelleFindByPrimaryKey(kritDtoI.kostenstelleIId).getCNr());
		}

		// Kunde
		if (kritDtoI.kundeIId != null) {
			buff.append(" ")
					.append(getTextRespectUISpr("lp.kunde", theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ").append(getKundeFac().kundeFindByPrimaryKey(kritDtoI.kundeIId, theClientDto).getPartnerDto()
					.getCName1nachnamefirmazeile1());
		}

		// Vertreter
		if (kritDtoI.vertreterIId != null) {
			buff.append(" ")
					.append(getTextRespectUISpr("lp.vertreter", theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ").append(getPersonalFac().personalFindByPrimaryKey(kritDtoI.vertreterIId, theClientDto)
					.getPartnerDto().formatFixName2Name1());
		}

		// Angebotsnummer
		if (kritDtoI.sBelegnummerVon != null || kritDtoI.sBelegnummerBis != null) {
			buff.append(" ").append(
					getTextRespectUISpr("angb.angebotnummer", theClientDto.getMandant(), theClientDto.getLocUi()));
		}

		if (kritDtoI.sBelegnummerVon != null) {
			buff.append(" ").append(getTextRespectUISpr("lp.von", theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ").append(kritDtoI.sBelegnummerVon);
		}

		if (kritDtoI.sBelegnummerBis != null) {
			buff.append(" ").append(getTextRespectUISpr("lp.bis", theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ").append(kritDtoI.sBelegnummerBis);
		}

		if (kritDtoI.provisionsempfaengerIId != null) {
			buff.append(" ").append(
					getTextRespectUISpr("lp.provisionsempfaenger", theClientDto.getMandant(), theClientDto.getLocUi()));
			PersonalDto pDto = getPersonalFac().personalFindByPrimaryKey(kritDtoI.provisionsempfaengerIId,
					theClientDto);

			buff.append(" ").append(pDto.formatFixUFTitelName2Name1());
		}

		if (kritDtoI.getBNurErledigteAngebote()) {
			buff.append(" ").append(
					getTextRespectUISpr("angb.nurerledigte", theClientDto.getMandant(), theClientDto.getLocUi()));
		}

		String cBuffer = buff.toString().trim();

		return cBuffer;
	}

	/**
	 * String zum Andrucken der Sortierkriterien.
	 * 
	 * @param reportJournalKriterienDtoI die Kriterien
	 * @param theClientDto               der aktuelle Benutzer
	 * @return String die Sortierkriterien
	 */
	private String buildSortierungAngebotJournal(ReportJournalKriterienDto reportJournalKriterienDtoI,
			TheClientDto theClientDto) {
		StringBuffer buff = new StringBuffer(
				getTextRespectUISpr("lp.sortierungnach", theClientDto.getMandant(), theClientDto.getLocUi()) + " ");

		if (reportJournalKriterienDtoI.bSortiereNachKostenstelle) {
			buff.append(getTextRespectUISpr("lp.kostenstelle", theClientDto.getMandant(), theClientDto.getLocUi()))
					.append(", ");
		}

		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
			buff.append(getTextRespectUISpr("lp.kunde", theClientDto.getMandant(), theClientDto.getLocUi()))
					.append(", ");
		}

		if (reportJournalKriterienDtoI.iSortierung == ReportAngebotJournalKriterienDto.KRIT_SORT_NACH_VERTRETER) {
			buff.append(getTextRespectUISpr("lp.vertreter", theClientDto.getMandant(), theClientDto.getLocUi()))
					.append(", ");
		}

		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PROVISIONSEMPFAENGER) {
			buff.append(
					getTextRespectUISpr("lp.provisionsempfaenger", theClientDto.getMandant(), theClientDto.getLocUi()))
					.append(", ");
		}

		buff.append(getTextRespectUISpr("angb.angebotnummer", theClientDto.getMandant(), theClientDto.getLocUi()));

		return buff.toString();
	}

	/**
	 * Alle offenen Angebote drucken.
	 * 
	 * @param kritDtoI     die Filter- und Sortierkriterien
	 * @param theClientDto der atkuelle Benutzer
	 * @return JasperPrint der Druck
	 * @throws EJBExceptionLP Ausnahme
	 */

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAngebotOffene(ReportAngebotJournalKriterienDto kritDtoI, Boolean bKommentare,
			Boolean bDetails, Boolean bKundenstammdaten, TheClientDto theClientDto) throws EJBExceptionLP {

		JasperPrintLP oPrint = null;
		cAktuellerReport = AngebotReportFac.REPORT_ANGEBOT_JOURNAL_OFFENE;

		try {
			// die Daten des Report ueber Hibernate holen
			ArrayList<ReportAngebotpositionJournalDto> aReportDto = getListeAngebotpositionenJournal(kritDtoI, null,
					AngebotReportFac.REPORT_ANGEBOT_JOURNAL_OFFENE, theClientDto);

			ArrayList<Object[]> alDaten = new ArrayList<>();

			// die Datenmatrix befuellen
			for (int i = 0; i < aReportDto.size(); i++) {

				Object[] oZeile = new Object[REPORT_ANGEBOT_JOURNAL_ANZAHL_SPALTEN];
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANGEBOTCNR] = aReportDto.get(i).getCNr();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_BELEGDATUM] = aReportDto.get(i).getBelegdatum();

				// PJ20303 Nachfasstermin <=Stichtag
				if (kritDtoI.dStichtag != null) {
					if (aReportDto.get(i).getNachfasstermin().getTime() <= kritDtoI.dStichtag.getTime()) {
					} else {
						continue;
					}
				}

				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_NACHFASSTERMIN] = aReportDto.get(i).getNachfasstermin();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDECNAME1] = aReportDto.get(i).getKundeCName1();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KOSTENSTELLECNR] = aReportDto.get(i)
						.getKostenstelleCNr();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_VERTRETERCNAME1] = aReportDto.get(i)
						.getVertreterCName1();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_PROJEKTBEZ] = aReportDto.get(i).getProejktBez();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_REALISIERUNGSTERMIN] = aReportDto.get(i)
						.getRealisierungstermin();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_AUFTRAGWAHRSCHEINLICHKEIT] = aReportDto.get(i)
						.getDAuftragwahrscheinlichkeit();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANGEBOTWERT] = aReportDto.get(i).getNWert();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_WAEHRUNG] = aReportDto.get(i).getWaehrungCNr();

				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_PROVISIONSEMPFAENGER_KURZZEICHEN] = aReportDto.get(i)
						.getKundeProvisionsempfaengerKurzzeichen();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_PROVISIONSEMPFAENGER_NAME] = aReportDto.get(i)
						.getKundeProvisionsempfaengerName();

				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_I_VERSION] = aReportDto.get(i).getIVersion();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_T_VERSION] = aReportDto.get(i).getTVersion();

				// @todo kann man das besser machen? xText darf in Hibernate
				// Criteria nicht vorkommen PJ 3749
				AngebotDto angebotDto = getAngebotFac().angebotFindByPrimaryKey(aReportDto.get(i).getIId(),
						theClientDto);

				if (bKommentare.booleanValue()) {
					oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_INTERNERKOMMENTAR] = angebotDto
							.getXInternerkommentar();
				} else {
					oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_INTERNERKOMMENTAR] = "";
				}
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_EXTERNERKOMMENTAR] = angebotDto.getXExternerkommentar();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ARTIKELCNR] = aReportDto.get(i).getArtikelCNr();

				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_LIEFERZEIT] = aReportDto.get(i).getLieferzeit();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_EINHEIT_LIEFERZEIT] = aReportDto.get(i)
						.getEinheitLieferzeit();

				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ARTIKELBEZ] = aReportDto.get(i).getArtkelCBez();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ARTIKELMENGE] = aReportDto.get(i).getNMenge();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ARTIKELEINHEIT] = aReportDto.get(i).getEinheitCNr();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ARTIKELPREIS] = aReportDto.get(i).getNPreis();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANGEBOTERLEDIGUNGSGRUND] = aReportDto.get(i)
						.getAngeboterledigungsgrundCNr();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ERLEDIGUNGSGRUND_AB_NR] = aReportDto.get(i)
						.getErledigungsgrundABNr();

				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDECNAME2] = aReportDto.get(i).getKundeCName2();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDECNAME3] = aReportDto.get(i).getKundeCName3();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDESTRASSE] = aReportDto.get(i).getKundeStrasse();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDEPLZ] = aReportDto.get(i).getKundePlz();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDEORT] = aReportDto.get(i).getKundeOrt();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDELKZ] = aReportDto.get(i).getKundeLkz();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDETELEFON] = aReportDto.get(i).getKundeTelefon();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDEFAX] = aReportDto.get(i).getKundeFax();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDEEMAIL] = aReportDto.get(i).getKundeEmail();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_KUNDEHOMEPAGE] = aReportDto.get(i).getKundeHomepage();

				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_VORNAME] = aReportDto.get(i)
						.getAnsprechpartnerVorname();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_NACHNAME] = aReportDto.get(i)
						.getAnsprechpartnerNachname();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_TITEL] = aReportDto.get(i)
						.getAnsprechpartnerTitel();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_ANREDE] = aReportDto.get(i)
						.getAnsprechpartnerAnrede();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_TELEFON] = aReportDto.get(i)
						.getAnsprechpartnerTelefon();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_TELEFONDW] = aReportDto.get(i)
						.getAnsprechpartnerTelefonDw();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_FAX] = aReportDto.get(i)
						.getAnsprechpartnerFax();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_FAXDW] = aReportDto.get(i)
						.getAnsprechpartnerFaxDw();
				oZeile[AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_EMAIL] = aReportDto.get(i)
						.getAnsprechpartnerEmail();
				alDaten.add(oZeile);
			}

			Object[][] returnArray = new Object[alDaten.size()][REPORT_ANGEBOT_JOURNAL_ANZAHL_SPALTEN];
			data = (Object[][]) alDaten.toArray(returnArray);

			// Erstellung des Report
			HashMap<String, Object> parameter = new HashMap<String, Object>();

			// die Parameter dem Report uebergeben
			parameter.put(LPReport.P_SORTIERUNG, buildSortierungAngebotJournal(kritDtoI, theClientDto));
			parameter.put(LPReport.P_FILTER, buildFilterAngebotJournal(kritDtoI, theClientDto));

			parameter.put("P_MITKUNDENSTAMMDATEN", bKundenstammdaten);

			parameter.put("P_SORTIERE_NACH_NACHFASSTERMIN", kritDtoI.isBSortiertNachNachfassterminNachrangig());

			// die Parameter zur Bildung von Zwischensummen uebergeben
			if (kritDtoI.bSortiereNachKostenstelle) {
				parameter.put(LPReport.P_SORTIERENACHKOSTENSTELLE, new Boolean(true));
			} else {
				parameter.put(LPReport.P_SORTIERENACHKOSTENSTELLE, new Boolean(false));
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

			parameter.put("P_TITLE", getTextRespectUISpr("angb.print.offeneangebote", theClientDto.getMandant(),
					theClientDto.getLocUi()));

			parameter.put(P_MANDANTWAEHRUNG, theClientDto.getSMandantenwaehrung());

			parameter.put(P_MITDETAILS, bDetails);

			parameter.put("P_STICHTAG", kritDtoI.dStichtag);

			initJRDS(parameter, AngebotReportFac.REPORT_MODUL, AngebotReportFac.REPORT_ANGEBOT_JOURNAL,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

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
				+ "' ) FROM FLRAngebotpositionReport pos WHERE pos.flrangebot.mandant_c_nr='"
				+ theClientDto.getMandant() + "' AND pos.flrangebot.angebotstatus_c_nr='"
				+ AngebotServiceFac.ANGEBOTSTATUS_OFFEN
				+ "' AND pos.artikel_i_id IS NOT NULL AND pos.angebotpositionart_c_nr='"
				+ AngebotServiceFac.ANGEBOTPOSITIONART_IDENT + "' ORDER BY pos.flrartikel.i_id";

		Query query = session.createQuery(queryString);

		List<?> resultList = query.list();

		Iterator<?> resultListIterator = resultList.iterator();

		ArrayList alDaten = new ArrayList();

		while (resultListIterator.hasNext()) {
			Object[] f = (Object[]) resultListIterator.next();
			FLRAngebotpositionReport pos = (FLRAngebotpositionReport) f[0];

			Object[] zeile = new Object[11];

			zeile[REPORT_ANGEBOTSPOTENTIAL_ARTIKELNUMMER] = pos.getFlrartikel().getC_nr();
			zeile[REPORT_ANGEBOTSPOTENTIAL_ARTIKELBEZEICHNUNG] = f[1];
			zeile[REPORT_ANGEBOTSPOTENTIAL_BELEGDATUM] = pos.getFlrangebot().getT_belegdatum();
			zeile[REPORT_ANGEBOTSPOTENTIAL_BELEGNUMMER] = pos.getFlrangebot().getC_nr();
			zeile[REPORT_ANGEBOTSPOTENTIAL_MENGE] = pos.getN_menge();
			zeile[REPORT_ANGEBOTSPOTENTIAL_PROJEKT] = pos.getFlrangebot().getC_bez();

			String kunde = pos.getFlrangebot().getFlrkunde().getFlrpartner().getC_name1nachnamefirmazeile1();

			if (pos.getFlrangebot().getFlrkunde().getFlrpartner().getC_name2vornamefirmazeile2() != null) {
				kunde += " " + pos.getFlrangebot().getFlrkunde().getFlrpartner().getC_name2vornamefirmazeile2();
			}

			zeile[REPORT_ANGEBOTSPOTENTIAL_KUNDE] = kunde;

			zeile[REPORT_ANGEBOTSPOTENTIAL_KUNDE_KURZBEZEICHNUNG] = pos.getFlrangebot().getFlrkunde().getFlrpartner()
					.getC_kbez();

			zeile[REPORT_ANGEBOTSPOTENTIAL_LIEFERANT] = "";
			try {
				ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreisDesBevorzugtenLieferanten(
						pos.getFlrartikel().getI_id(), pos.getN_menge(), theClientDto.getSMandantenwaehrung(),
						theClientDto);

				if (alDto != null) {
					zeile[REPORT_ANGEBOTSPOTENTIAL_EKPREIS] = alDto.getLief1Preis();

					if (alDto.getLieferantDto() != null) {

						String lieferant = alDto.getLieferantDto().getPartnerDto().getCName1nachnamefirmazeile1();

						if (alDto.getLieferantDto().getPartnerDto().getCName2vornamefirmazeile2() != null) {
							lieferant += " " + alDto.getLieferantDto().getPartnerDto().getCName2vornamefirmazeile2();
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

				String wert = Helper.fillStringWithBlankRight(((String) o[REPORT_ANGEBOTSPOTENTIAL_LIEFERANT]), 80)
						+ ((String) o[REPORT_ANGEBOTSPOTENTIAL_ARTIKELNUMMER]);
				String wert1 = Helper.fillStringWithBlankRight(((String) o1[REPORT_ANGEBOTSPOTENTIAL_LIEFERANT]), 80)
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
		initJRDS(parameter, AngebotReportFac.REPORT_MODUL, AngebotReportFac.REPORT_ANGEBOTSPOTENTIAL,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	/**
	 * Alle abgelehnte Angebote drucken.
	 * 
	 * @param kritDtoI     die Filter- und Sortierkriterien
	 * @param theClientDto der atkuelle Benutzer
	 * @return JasperPrint der Druck
	 * @throws EJBExceptionLP Ausnahme
	 */
	public JasperPrintLP printAngebotAbgelehnte(ReportAngebotJournalKriterienDto kritDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		JasperPrintLP oPrint = null;
		StringBuffer buff = null;
		StringBuffer zbuff = null;
		cAktuellerReport = AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE;
		try {
			Session session = null;
			// Erstellung des Report
			HashMap<String, Object> parameter = new HashMap<String, Object>();

			try {
				SessionFactory factory = FLRSessionFactory.getFactory();
				session = factory.openSession();
				// Criteria anlegen fuer alle referenzierten Objekte anlegen
				Criteria crit = session.createCriteria(FLRAngebot.class);
				// Einschraenken nach Mandant
				crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));
				// Einschraenken nach Status
				crit.add(Restrictions.eq(AngebotFac.FLR_ANGEBOT_ANGEBOTSTATUS_C_NR,
						AngebotServiceFac.ANGEBOTSTATUS_ERLEDIGT));

				crit.add(Restrictions.isNotNull(AngebotFac.FLR_ANGEBOT_ANGEBOTERLEDIGUNGSGRUND_C_NR));
				// Einschraenkung nach einem bestimmten Kunden
				if (kritDtoI.kundeIId != null) {
					crit.add(Restrictions.eq(AngebotFac.FLR_ANGEBOT_KUNDE_I_ID_ANGEBOTSADRESSE, kritDtoI.kundeIId));
				}
				if (kritDtoI.dVon != null) {
					crit.add(Restrictions.ge(AngebotFac.FLR_ANGEBOT_T_BELEGDATUM, kritDtoI.dVon));
				}

				if (kritDtoI.dBis != null) {
					crit.add(Restrictions.le(AngebotFac.FLR_ANGEBOT_T_BELEGDATUM, kritDtoI.dBis));
				}
				// es wird in jedem Fall nach der Belegnummer sortiert

				crit.addOrder(Order.asc(AngebotFac.FLR_ANGEBOT_ANGEBOTERLEDIGUNGSGRUND_C_NR));
				crit.addOrder(Order.asc("c_nr"));

				// PJ19361
				String sQueryAnzahlAngebote = "SELECT COUNT (*) FROM FLRAngebot a WHERE a.angebotstatus_c_nr<>'"
						+ LocaleFac.STATUS_STORNIERT + "' AND a.mandant_c_nr='" + theClientDto.getMandant() + "'";

				if (kritDtoI.dVon != null) {

					sQueryAnzahlAngebote += " AND a.t_belegdatum >='" + Helper.formatDateWithSlashes(kritDtoI.dVon)
							+ "'";

				}

				if (kritDtoI.dBis != null) {
					sQueryAnzahlAngebote += " AND a.t_belegdatum <='" + Helper.formatDateWithSlashes(kritDtoI.dBis)
							+ "'";
				}

				Session session2 = FLRSessionFactory.getFactory().openSession();

				Query query = session2.createQuery(sQueryAnzahlAngebote);

				List<?> resultList = query.list();

				Iterator<?> resultListIterator = resultList.iterator();

				if (resultListIterator.hasNext()) {
					Long l = (Long) resultListIterator.next();
					parameter.put("P_ANZAHL_ANGEBOTE_IM_ZEITRAUM", l);
				}
				session2.close();

				int iIndex = 0;
				List<?> list = crit.list();
				Iterator<?> it = list.iterator();
				int iAnzahlZeilen = list.size(); // Anzahl der Positionen
				int iAnzahlSpalten = REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_ANZAHL_SPALTEN; // Anzahl
																						// der
				// Spalten
				// in
				// der
				// Gruppe

				int iAnzahlAuftragErhalten = 0;
				BigDecimal bdSummeAuftragErhalten = BigDecimal.ZERO;

				data = new Object[iAnzahlZeilen][iAnzahlSpalten];
				HashMap<String, Integer> hm = new HashMap<String, Integer>();
				while (it.hasNext()) {
					FLRAngebot flrangebot = (FLRAngebot) it.next();
					// erledigungsgruende zaehlen
					String erledigungsgrund = flrangebot.getAngeboterledigungsgrund_c_nr();
					Integer value = hm.get(erledigungsgrund);
					if (value == null) {
						hm.put(erledigungsgrund, 1);
					} else {
						hm.put(erledigungsgrund, value + 1); // wird ersetzt
					}
					// Daten fuer den Report
					data[iIndex][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_ANGEBOTCNR] = flrangebot.getC_nr();
					data[iIndex][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_BELEGDATUM] = flrangebot
							.getT_belegdatum();
					data[iIndex][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_KUNDECNAME1] = flrangebot
							.getFlrkunde().getFlrpartner().getC_name1nachnamefirmazeile1();
					data[iIndex][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_KOSTENSTELLECNR] = flrangebot
							.getFlrkostenstelle().getC_nr();
					data[iIndex][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_VERTRETERCNAME1] = flrangebot
							.getFlrvertreter().getFlrpartner().getC_name1nachnamefirmazeile1();
					if (flrangebot.getC_bez() != null) {
						data[iIndex][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_PROJEKTBEZ] = flrangebot
								.getC_bez();
					}
					data[iIndex][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_REALISIERUNGSTERMIN] = Helper
							.formatDatum(flrangebot.getT_realisierungstermin(), theClientDto.getLocUi());
					data[iIndex][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_ANGEBOTERLEDIGUNGSGRUND] = flrangebot
							.getAngeboterledigungsgrund_c_nr();
					if (flrangebot.getAngeboterledigungsgrund_c_nr() != null) {

						data[iIndex][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_ANGEBOTERLEDIGUNGSGRUND] = getAngebotServiceFac()
								.angeboterledigungsgrundFindByPrimaryKey(flrangebot.getAngeboterledigungsgrund_c_nr(),
										theClientDto)
								.getBezeichnung();

						if (flrangebot.getAngeboterledigungsgrund_c_nr()
								.equals(AngebotServiceFac.ANGEBOTERLEDIGUNGSGRUND_AUFTRAGERHALTEN)) {

							iAnzahlAuftragErhalten++;

							if (flrangebot.getN_gesamtangebotswertinangebotswaehrung() != null) {

								BigDecimal wert = flrangebot.getN_gesamtangebotswertinangebotswaehrung();

								if (wert != null) {
									wert = getLocaleFac().rechneUmInMandantenWaehrung(wert, new BigDecimal(
											flrangebot.getF_wechselkursmandantwaehrungzuangebotswaehrung()));
									bdSummeAuftragErhalten = bdSummeAuftragErhalten.add(wert);
								}

							}

							AuftragDto[] auftrag = getAuftragFac().auftragFindByAngebotIId(flrangebot.getI_id(),
									theClientDto);
							if (auftrag != null && auftrag.length > 0) {
								data[iIndex][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_ERLEDIGUNGSGRUND_AB_NR] = auftrag[0]
										.getCNr();
							}
						}
					}
					data[iIndex][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_GESAMTANGEBOTSWERT] = flrangebot
							.getN_gesamtangebotswertinangebotswaehrung();
					data[iIndex][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_INTERNERKOMMENTAR] = flrangebot
							.getX_internerkommentar();
					data[iIndex][AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_EXTERNERKOMMENTAR] = flrangebot
							.getX_externerkommentar();

					iIndex++;
				}

				parameter.put("P_ANZAHL_AUFTRAG_ERHALTEN", iAnzahlAuftragErhalten);

				parameter.put("P_GESAMTWERT_AUFTRAG_ERHALTEN_IN_MANDANTENWAEHRUNG", bdSummeAuftragErhalten);

				// erledigungsgruende nach der anzahl sortieren
				buff = new StringBuffer("");
				zbuff = new StringBuffer("");
				Map<Integer, String> sortedMap = new TreeMap<Integer, String>();
				for (Iterator<String> it3 = hm.keySet().iterator(); it3.hasNext();) {
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
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, new Exception(t));
			} finally {
				try {
					session.close();
				} catch (HibernateException he) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE, he);
				}
			}

			// die Parameter dem Report uebergeben
			parameter.put(LPReport.P_SORTIERUNG, buildSortierungAngebotJournal(kritDtoI, theClientDto));
			parameter.put(LPReport.P_FILTER, buildFilterAngebotJournal(kritDtoI, theClientDto));

			if (kritDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				parameter.put(LPReport.P_SORTIERENACHKUNDE, new Boolean(true));
			} else {
				parameter.put(LPReport.P_SORTIERENACHKUNDE, new Boolean(false));
			}

			parameter.put("P_TITLE",
					getTextRespectUISpr("angb.print.abgelehnte", theClientDto.getMandant(), theClientDto.getLocUi()));
			String cBuffer = buff.toString().trim();
			parameter.put("P_STATISTIK_SPALTE1", cBuffer);
			parameter.put("P_STATISTIK_SPALTE2", zbuff.toString());
			parameter.put(P_MANDANTWAEHRUNG, theClientDto.getSMandantenwaehrung());

			initJRDS(parameter, AngebotReportFac.REPORT_MODUL, AngebotReportFac.REPORT_ANGEBOT_JOURNAL_ABGELEHNTE,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

			oPrint = getReportPrint();
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, new Exception(t));
		}

		return oPrint;
	}

	public JasperPrintLP printAngebotVorkalkulation(Integer iIdAngebotI, Date preisGueltig, TheClientDto theClientDto)
			throws EJBExceptionLP {
		JasperPrintLP oPrint = null;
		cAktuellerReport = AngebotReportFac.REPORT_VORKALKULATION;
		Session session = null;

		if (preisGueltig == null) {
			preisGueltig = new Date(System.currentTimeMillis());
		}

		try {
			AngebotDto angebotDto = getAngebotFac().angebotFindByPrimaryKey(iIdAngebotI, theClientDto);

			// UW 29.03.06 Tiefe der Stuecklistenaufloeseung bestimmen
			ParametermandantDto parametermandantDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ANGEBOT, ParameterFac.PARAMETER_ANGEBOT_AGVORKALK_STKL_AUFLOESUNG_TIEFE);

			ParametermandantDto parameterGestpreisBerechnung = null;
			parameterGestpreisBerechnung = (ParametermandantDto) getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_GESTPREISBERECHNUNG_HAUPTLAGER);

			boolean bGestpreisberechnungHauptlager = ((Boolean) parameterGestpreisBerechnung.getCWertAsObject())
					.booleanValue();

			LagerDto hauptlager = getLagerFac().getHauptlagerDesMandanten(theClientDto);

			int iStuecklisteaufloesungTiefe = ((Integer) parametermandantDto.getCWertAsObject()).intValue();

			session = FLRSessionFactory.getFactory().openSession();

			Criteria crit = session.createCriteria(FLRAngebotpositionReport.class);
			crit.add(Restrictions.eq(AngebotpositionFac.FLR_ANGEBOTPOSITION_FLRANGEBOT + ".i_id", iIdAngebotI));

			// UW 22.03.06 alle nicht alternativen Positionen mit positiver
			// Menge
			crit.add(Restrictions.isNotNull(AngebotpositionFac.FLR_ANGEBOTPOSITION_N_MENGE));
			crit.add(Restrictions.gt(AngebotpositionFac.FLR_ANGEBOTPOSITION_N_MENGE, new BigDecimal(0)));
			crit.add(Restrictions.eq(AngebotpositionFac.FLR_ANGEBOTPOSITION_B_ALTERNATIVE, new Short((short) 0)));

			crit.addOrder(Order.asc(AngebotpositionFac.FLR_ANGEBOTPOSITION_I_SORT));

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
				FLRAngebotpositionReport flrangebotposition = (FLRAngebotpositionReport) itListe.next();

				if (flrangebotposition.getAngebotpositionart_c_nr()
						.equals(AngebotServiceFac.ANGEBOTPOSITIONART_IDENT)) {

					// SP8661 Mit WH beprochen: Wie ersetzen die Stuecklistenaufloesung gegen die
					// normale Stkl-Aufloesung der Gesamtkalkulation mit Arbeitsplan

					// und dann Positionen

					StuecklisteDto stuecklisteDto = getStuecklisteFac().stuecklisteFindByMandantCNrArtikelIIdOhneExc(
							flrangebotposition.getArtikel_i_id(), theClientDto);

					// wenn die aktuelle Artikelposition eine Stueckliste ist
					if (stuecklisteDto != null && !stuecklisteDto.getStuecklisteartCNr()
							.equals(StuecklisteFac.STUECKLISTEART_SETARTIKEL)) {

						List<?> n = getStuecklisteFac().getStrukturDatenEinerStuecklisteMitArbeitsplan(
								stuecklisteDto.getIId(), theClientDto,
								StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_OHNE, 0, null, true, true,
								flrangebotposition.getN_menge(), null, false, false, false, null);

						alDaten.add(befuelleZeileVKMitIdentMitSTKLAusAngebotposition(flrangebotposition,
								angebotDto.getTRealisierungstermin(), theClientDto,
								hauptlager.getPartnerIIdStandort()));

						List<StuecklisteMitStrukturDto> m = (List<StuecklisteMitStrukturDto>) n;
						Iterator<StuecklisteMitStrukturDto> it = m.listIterator();

						while (it.hasNext()) {
							StuecklisteMitStrukturDto struktur = it.next();

							if (struktur.getIEbene() <= iStuecklisteaufloesungTiefe) {
								if (struktur.getStuecklistepositionDto() != null) {
									// Position
									alDaten.add(befuelleZeileVorkalkulationMitStuecklistenposition(
											flrangebotposition.getI_id(), // eine
											// Zwischensumme
											// fuer den
											// uebergeordneten
											// Artikel
											// bilden
											"", struktur,
											flrangebotposition.getFlrangebot().getWaehrung_c_nr_angebotswaehrung(),
											flrangebotposition.getN_menge(), flrangebotposition.getEinheit_c_nr(),
											iStuecklisteaufloesungTiefe, bGestpreisberechnungHauptlager,
											angebotDto.getTRealisierungstermin(), preisGueltig,
											struktur.getStuecklistepositionDto().getBRuestmenge(), theClientDto));
								} else if (struktur.getStuecklistearbeitsplanDto() != null) {
									// Arbeitsplan
									alDaten.add(befuelleZeileVorkalkulationMitStuecklistearbeitsplan(
											flrangebotposition.getI_id(), // eine
											// Zwischensumme
											// fuer den
											// uebergeordneten
											// Artikel
											// bilden
											"", struktur,
											flrangebotposition.getFlrangebot().getWaehrung_c_nr_angebotswaehrung(),
											flrangebotposition.getN_menge(), flrangebotposition.getEinheit_c_nr(),
											iStuecklisteaufloesungTiefe, bGestpreisberechnungHauptlager,
											angebotDto.getTRealisierungstermin(), stuecklisteDto, theClientDto,
											preisGueltig, hauptlager.getPartnerIIdStandort()));
								}
							}
						}

					} else {
						// UW 29.03.06 Eine STKL wird wie ein normaler Artikel
						// angedruckt, wenn...
						// ...es sich um eine Fremdfertigung handelt
						// ...es keine Positionen gibt
						// ...die gewuenschte Aufloesungstiefe lt. Parameter 0
						// ist
						alDaten.add(befuelleZeileVKMitIdentOhneSTKLAusAngebotposition(flrangebotposition,
								angebotDto.getTRealisierungstermin(), theClientDto, angebotDto.getWaehrungCNr(),
								angebotDto.getAngeboteinheitCNr(), hauptlager.getPartnerIIdStandort(), preisGueltig));
					}

				} else if (flrangebotposition.getAngebotpositionart_c_nr()
						.equals(AngebotServiceFac.ANGEBOTPOSITIONART_HANDEINGABE)) {
					alDaten.add(befuelleZeileVKMitHandAusAngebotposition(flrangebotposition, theClientDto));

				} else if (flrangebotposition.getAngebotpositionart_c_nr()
						.equals(AngebotServiceFac.ANGEBOTPOSITIONART_AGSTUECKLISTE)) {
					// jede Zeile muss als INDEX_GRUPPE die AGSTKL cNr haben!
					alDaten.add(befuelleZeileVKMitAGSTKLPosition(flrangebotposition, theClientDto));

					AgstklpositionDto[] aAgstklpositionDto = getAngebotstklpositionFac()
							.agstklpositionFindByAgstklIIdOhneExc(flrangebotposition.getAgstkl_i_id(), theClientDto);

					Integer indexGruppe = new Integer(
							flrangebotposition.getAgstkl_i_id() + flrangebotposition.getI_id());

					// Einrueckungsebene gegenueber der AGSTKL
					String cEinrueckung = "    ";

					for (int j = 0; j < aAgstklpositionDto.length; j++) {
						// AGPosition ist dieselbe, aber die naechste Zeile
						// im data[][] befuellen

						ArtikelDto artikelDtoAgstklposition = getArtikelFac()
								.artikelFindByPrimaryKeySmall(aAgstklpositionDto[j].getArtikelIId(), theClientDto);

						if (artikelDtoAgstklposition.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {

							alDaten.add(befuelleZeileVKMitHandAusAGSTKLPosition(indexGruppe, aAgstklpositionDto[j],
									flrangebotposition.getN_menge(), flrangebotposition.getEinheit_c_nr(), theClientDto,
									cEinrueckung));
						} else {
							// jeder Artikel koennte selbst wieder ein
							// Stueckliste mit Unterpositionen sein

							StuecklisteDto stuecklisteDto = getStuecklisteFac()
									.stuecklisteFindByMandantCNrArtikelIIdOhneExc(aAgstklpositionDto[j].getArtikelIId(),
											theClientDto);

							if (stuecklisteDto != null) {
								List<?> n = getStuecklisteFac().getStrukturDatenEinerStuecklisteMitArbeitsplan(
										stuecklisteDto.getIId(), theClientDto,
										StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_OHNE, 0, null, true,
										true,
										flrangebotposition.getN_menge().multiply(aAgstklpositionDto[j].getNMenge()),
										null, false, false, false, null);

								List<StuecklisteMitStrukturDto> m = (List<StuecklisteMitStrukturDto>) n;
								Iterator<StuecklisteMitStrukturDto> it = m.listIterator();

								alDaten.add(befuelleZeileVKMitIdentMitSTKLAusAGSTKLPosition(indexGruppe,
										aAgstklpositionDto[j], flrangebotposition.getN_menge(),
										flrangebotposition.getEinheit_c_nr(), angebotDto.getTRealisierungstermin(),
										theClientDto, cEinrueckung, hauptlager.getPartnerIIdStandort()));

								while (it.hasNext()) {
									StuecklisteMitStrukturDto struktur = it.next();

									if (struktur.getStuecklistepositionDto() != null) {

										alDaten.add(befuelleZeileVorkalkulationMitStuecklistenposition(indexGruppe,
												cEinrueckung, struktur,
												flrangebotposition.getFlrangebot().getWaehrung_c_nr_angebotswaehrung(),
												aAgstklpositionDto[j].getNMenge()
														.multiply(flrangebotposition.getN_menge()),
												aAgstklpositionDto[j].getEinheitCNr(), iStuecklisteaufloesungTiefe,
												bGestpreisberechnungHauptlager, angebotDto.getTRealisierungstermin(),
												preisGueltig, struktur.getStuecklistepositionDto().getBRuestmenge(),
												theClientDto));
									} else if (struktur.getStuecklistearbeitsplanDto() != null) {
										alDaten.add(befuelleZeileVorkalkulationMitStuecklistearbeitsplan(indexGruppe,
												cEinrueckung, struktur,
												flrangebotposition.getFlrangebot().getWaehrung_c_nr_angebotswaehrung(),
												flrangebotposition.getN_menge()
														.multiply(flrangebotposition.getN_menge()),
												flrangebotposition.getEinheit_c_nr(), iStuecklisteaufloesungTiefe,
												bGestpreisberechnungHauptlager, angebotDto.getTRealisierungstermin(),
												stuecklisteDto, theClientDto, preisGueltig,
												hauptlager.getPartnerIIdStandort()));
									}
								}

							} else {
								// Artikel ohne Stueckliste
								alDaten.add(befuelleZeileVKMitArtikelAusAGStkl(indexGruppe, cEinrueckung,
										artikelDtoAgstklposition,
										flrangebotposition.getFlrangebot().getWaehrung_c_nr_angebotswaehrung(),
										aAgstklpositionDto[j].getNMenge().multiply(flrangebotposition.getN_menge()),
										aAgstklpositionDto[j].getNMenge(), aAgstklpositionDto[j].getEinheitCNr(),
										bGestpreisberechnungHauptlager, angebotDto.getTRealisierungstermin(),
										preisGueltig, aAgstklpositionDto[j], theClientDto));
							}

						}
					}
					if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_AGSTKL_ARBEITSPLAN, theClientDto)) {
						// PJ18725
						AgstklarbeitsplanDto[] aAgstklarbeitsplanDto = getAngebotstklFac()
								.agstklarbeitsplanFindByAgstklIId(flrangebotposition.getAgstkl_i_id(), theClientDto);

						for (int j = 0; j < aAgstklarbeitsplanDto.length; j++) {
							if (!Helper.short2boolean(aAgstklarbeitsplanDto[j].getBNurmaschinenzeit())) {

								alDaten.add(befuelleZeileVorkalkulationMitAgstklarbeitsplan(indexGruppe, // eine
										// Zwischensumme
										// fuer den
										// uebergeordneten
										// Artikel
										// bilden
										"", aAgstklarbeitsplanDto[j],
										flrangebotposition.getFlrangebot().getWaehrung_c_nr_angebotswaehrung(),
										flrangebotposition.getN_menge(), flrangebotposition.getEinheit_c_nr(),
										iStuecklisteaufloesungTiefe, bGestpreisberechnungHauptlager,
										angebotDto.getTRealisierungstermin(), theClientDto, false,
										hauptlager.getPartnerIIdStandort(), preisGueltig));
							}
							if (aAgstklarbeitsplanDto[j].getMaschineIId() != null) {

								alDaten.add(befuelleZeileVorkalkulationMitAgstklarbeitsplan(indexGruppe, // eine
										// Zwischensumme
										// fuer den
										// uebergeordneten
										// Artikel
										// bilden
										"", aAgstklarbeitsplanDto[j],
										flrangebotposition.getFlrangebot().getWaehrung_c_nr_angebotswaehrung(),
										flrangebotposition.getN_menge(), flrangebotposition.getEinheit_c_nr(),
										iStuecklisteaufloesungTiefe, bGestpreisberechnungHauptlager,
										angebotDto.getTRealisierungstermin(), theClientDto, true,
										hauptlager.getPartnerIIdStandort(), preisGueltig));
							}

						}
					}

					if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_MV_AG_SCHNELLERFASSUNG, theClientDto)) {
						// PJ18725

						ArrayList<AgstklmaterialDto> agstklmaterialDto = getAngebotstklpositionFac()
								.agstklmaterialFindByAgstklIId(flrangebotposition.getAgstkl_i_id(), theClientDto);

						for (int i = 0; i < agstklmaterialDto.size(); i++) {
							AgstklmaterialDto matDto = agstklmaterialDto.get(i);

							alDaten.add(befuelleZeileVKMitMaterialAusAGSTKLPosition(indexGruppe, matDto,
									flrangebotposition.getN_menge(), flrangebotposition.getEinheit_c_nr(), theClientDto,
									cEinrueckung));

						}

					}

				}
			}
			data = new Object[alDaten.size()][AngebotReportFac.REPORT_ANGEBOT_ANZAHL_SPALTEN];
			data = (Object[][]) alDaten.toArray(data);

			// Erstellung des Report
			HashMap<String, Object> reportParameter = new HashMap<String, Object>();

			// es gilt das Locale des Benutzers
			Locale locDruck = theClientDto.getLocUi();

			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(angebotDto.getKundeIIdAngebotsadresse(),
					theClientDto);

			AnsprechpartnerDto ansprechpartnerDto = null;

			if (angebotDto.getAnsprechpartnerIIdKunde() != null) {
				ansprechpartnerDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(angebotDto.getAnsprechpartnerIIdKunde(), theClientDto);
			}

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);

			reportParameter.put(LPReport.P_ADRESSBLOCK,
					formatAdresseFuerAusdruck(kundeDto.getPartnerDto(), ansprechpartnerDto, mandantDto, locDruck));
			reportParameter.put("P_ANGEBOTNUMMER", angebotDto.getCNr());

			reportParameter.put("P_KOMMENTAR_INTERN", angebotDto.getXInternerkommentar());
			reportParameter.put("P_KOMMENTAR_EXTERN", angebotDto.getXExternerkommentar());

			ParametermandantDto parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT);

			reportParameter.put("P_EINHEIT_WIEDERBESCHAFFUNGSZEIT", parameterDto.getCWert());

			ParametermandantDto parameterDtoDefaultWBZ = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_DEFAULT_WIEDERBESCHAFFUNGSZEIT);

			reportParameter.put("P_DEFAULT_WIEDERBESCHAFFUNGSZEIT",
					(Integer) parameterDtoDefaultWBZ.getCWertAsObject());

			// PJ22395
			ParametermandantDto parameterDtoLief1Gueltigkeit = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_LIEF1PREIS_GUELTIG_IN_MONATEN_FUER_REPORT);

			if (parameterDtoLief1Gueltigkeit.getCWert() != null
					&& parameterDtoLief1Gueltigkeit.getCWert().length() > 0) {
				reportParameter.put("P_LIEF1PREIS_GUELTIG_IN_MONATEN",
						(Integer) parameterDtoLief1Gueltigkeit.getCWertAsObject());
			}

			reportParameter.put("P_ANGEBOTCBEZ", angebotDto.getCBez() != null ? angebotDto.getCBez() : "");
			reportParameter.put("P_LIEFERART", getLocaleFac()
					.lieferartFindByIIdLocaleOhneExc(angebotDto.getLieferartIId(), locDruck, theClientDto));

			// Lieferzeit in hinterlegter Einheit
			Integer iiLieferzeit = getAngebotServiceFac().getLieferzeitInAngeboteinheit(angebotDto.getIId(), null,
					angebotDto.getAngeboteinheitCNr(), theClientDto);

			reportParameter.put("P_ANGEBOTEINHEIT",
					getSystemFac().formatEinheit(angebotDto.getAngeboteinheitCNr(), locDruck, theClientDto));

			reportParameter.put("P_LIEFERZEIT", iiLieferzeit);
			reportParameter.put("P_BELEGWAEHRUNG", angebotDto.getWaehrungCNr());
			reportParameter.put("P_MANDANTENWAEHRUNG", theClientDto.getSMandantenwaehrung());
			reportParameter.put("P_WECHSELKURSMANDANTZUBELEG",
					angebotDto.getFWechselkursmandantwaehrungzubelegwaehrung());

			// das Zahlungsziel
			reportParameter.put("P_ZAHLUNGSZIEL", getMandantFac()
					.zahlungszielFindByIIdLocaleOhneExc(angebotDto.getZahlungszielIId(), locDruck, theClientDto));
			reportParameter.put("P_BELEGDATUM", angebotDto.getTBelegdatum());
			reportParameter.put("P_ANGEBOTSGUELTIGKEIT", angebotDto.getTAngebotsgueltigkeitbis());

			reportParameter.put("P_ALLGEMEINERRABATT", angebotDto.getFAllgemeinerRabattsatz());
			reportParameter.put("P_ALLGEMEINERRABATT_STRING",
					Helper.formatZahl(angebotDto.getFAllgemeinerRabattsatz(), locDruck));
			reportParameter.put("P_PROJEKT_RABATT", angebotDto.getFProjektierungsrabattsatz());
			reportParameter.put("P_PROJEKT_RABATT_STRING",
					Helper.formatZahl(angebotDto.getFProjektierungsrabattsatz(), locDruck));
			reportParameter.put("P_VERSTECKTER_AUFSCHLAG", angebotDto.getFVersteckterAufschlag());
			reportParameter.put("P_VERSTECKTER_AUFSCHLAG_STRING",
					Helper.formatZahl(angebotDto.getFVersteckterAufschlag(), locDruck));
			reportParameter.put("P_REALISIERUNGSTERMIN", angebotDto.getTRealisierungstermin());

			// Parameter

			ParametermandantDto parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.ARBEITSZEITGEMEINKOSTENFAKTOR, new Timestamp(System.currentTimeMillis()));
			reportParameter.put("P_ARBEITSZEITGEMEINKOSTENPROZENT", ((Double) parameter.getCWertAsObject()));

			parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.MATERIALGEMEINKOSTENFAKTOR,
					new Timestamp(System.currentTimeMillis()));
			reportParameter.put("P_MATERIALGEMEINKOSTENPROZENT", ((Double) parameter.getCWertAsObject()));

			parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.FERTIGUNGSGEMEINKOSTENFAKTOR,
					new Timestamp(System.currentTimeMillis()));
			reportParameter.put("P_FERTIGUNGSGEMEINKOSTENPROZENT", ((Double) parameter.getCWertAsObject()));

			parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.ENTWICKLUNGSGEMEINKOSTENFAKTOR,
					new Timestamp(System.currentTimeMillis()));
			reportParameter.put("P_ENTWICKLUNGSGEMEINKOSTENPROZENT", ((Double) parameter.getCWertAsObject()));

			parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.VERWALTUNGSGEMEINKOSTENFAKTOR,
					new Timestamp(System.currentTimeMillis()));
			reportParameter.put("P_VERWALTUNGSGEMEINKOSTENPROZENT", ((Double) parameter.getCWertAsObject()));

			parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.VERTRIEBSGEMEINKOSTENFAKTOR,
					new Timestamp(System.currentTimeMillis()));
			reportParameter.put("P_VERTRIEBSGEMEINKOSTENPROZENT", ((Double) parameter.getCWertAsObject()));

			reportParameter.put("P_DATUM_PREISGUELTIGKEIT", preisGueltig);

			initJRDS(reportParameter, AngebotReportFac.REPORT_MODUL, AngebotReportFac.REPORT_VORKALKULATION,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

			oPrint = getReportPrint();
			PrintInfoDto values = getJCRDocFac().getPathAndPartnerAndTable(angebotDto.getIId(),
					QueryParameters.UC_ID_ANGEBOT, theClientDto);
			oPrint.setOInfoForArchive(values);
			oPrint.putAdditionalInformation(JasperPrintLP.KEY_BELEGART, LocaleFac.BELEGART_ANGEBOT);
			oPrint.putAdditionalInformation(JasperPrintLP.KEY_BELEGIID, angebotDto.getIId());

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

	/**
	 * Befuelle eine Zeile der Vorkalkulation mit den Daten eines Artikels in einer
	 * Angebotsstueckliste, der keine Stueckliste hat
	 */
	private Object[] befuelleZeileVKMitArtikelAusAGStkl(Integer indexGruppe, String cEinrueckung, ArtikelDto artikelDto,
			String waehrungCnr, BigDecimal zielmenge, BigDecimal mengeUebergeordnet, String einheitCNr,
			boolean bGestpreisberechnungHauptlager, Timestamp tRealisierungstermin, Date preisGueltig,AgstklpositionDto agstklpositionDto,
			TheClientDto theClientDto) {
		Object[] aDataI = new Object[REPORT_VORKALKULATION_ANZAHL_SPALTEN];
		try {
			LagerDto hauptlagerDto = getLagerFac().getHauptlagerDesMandanten(theClientDto);

			aDataI[AngebotReportFac.REPORT_VORKALKULATION_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_STUECKLISTENPOSITION;

			aDataI = befuelleZeileVorkalkulationMitArtikelCNrCBezCZBez(aDataI, artikelDto, cEinrueckung, null,
					theClientDto);

			if (agstklpositionDto.getBRuestmenge() != null) {
				aDataI[REPORT_VORKALKULATION_RUESTMENGE] = Helper.short2boolean(agstklpositionDto.getBRuestmenge());
				if(Helper.short2boolean(agstklpositionDto.getBRuestmenge())){
				zielmenge=agstklpositionDto.getNMenge();
				}
			}

			aDataI[REPORT_VORKALKULATION_INITIALKOSTEN] = Helper.short2boolean(agstklpositionDto.getBInitial());
			
			aDataI[REPORT_VORKALKULATION_AGSTKL_MATERIAL] = Boolean.FALSE;
			aDataI[REPORT_VORKALKULATION_MENGE] = zielmenge;
			aDataI[REPORT_VORKALKULATION_EINHEIT] = artikelDto.getEinheitCNr();
			aDataI[REPORT_VORKALKULATION_MENGE_UEBERGEORDNET] = mengeUebergeordnet;
			aDataI[REPORT_VORKALKULATION_EINHEIT_UEBERGEORDNET] = einheitCNr;
			aDataI[REPORT_VORKALKULATION_AGSTKLPOSITION_PREIS] = agstklpositionDto.getNNettoeinzelpreis();
			if (tRealisierungstermin != null) {
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_FIKTIVER_LAGERSTAND] = getInternebestellungFac()
						.getFiktivenLagerstandZuZeitpunkt(artikelDto, theClientDto, tRealisierungstermin,
								hauptlagerDto.getPartnerIIdStandort());
			} else {
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_FIKTIVER_LAGERSTAND] = null;
			}
			if (artikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
				// WH xx.03.06 Handeingaben in STKL haben keinen Gestehungspreis
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS] = new BigDecimal(0);
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT] = new BigDecimal(0);
			} else {

				BigDecimal gestehungspreisInBelegwaehrung = getGestehungspreisInBelegwaehrungUndParameterGESTPREISBERECHNUNG_HAUPTLAGER(
						waehrungCnr, bGestpreisberechnungHauptlager, theClientDto, artikelDto.getIId(), preisGueltig,
						hauptlagerDto);

				aDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS] = gestehungspreisInBelegwaehrung;
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT] = gestehungspreisInBelegwaehrung
						.multiply(zielmenge);
			}

			befuelleZeileVKMitArtikellieferantDaten(aDataI, artikelDto, zielmenge, waehrungCnr, preisGueltig,
					theClientDto);

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		aDataI[AngebotReportFac.REPORT_VORKALKULATION_INDEX_GRUPPE] = indexGruppe;

		return aDataI;
	}

	public JasperPrintLP printAdressetikett(Integer partnerIId, Integer ansprechpartnerIId, TheClientDto theClientDto) {

		cAktuellerReport = AngebotReportFac.REPORT_ANGEBOT_ADRESSETIKETT;
		data = new Object[1][9];

		try {
			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(partnerIId, theClientDto);
			String sLocalePartner = partnerDto.getLocaleCNrKommunikation();
			data[0][AngebotReportFac.REPORT_ADRESSETIKETT_NAME] = getPartnerFac()
					.formatFixAnredeTitelName2Name1(partnerDto, Helper.string2Locale(sLocalePartner), theClientDto);
			data[0][AngebotReportFac.REPORT_ADRESSETIKETT_STRASSE] = partnerDto.getCStrasse();

			if (partnerDto.getLandplzortDto() != null) {
				data[0][AngebotReportFac.REPORT_ADRESSETIKETT_PLZORT] = partnerDto.getLandplzortDto().formatPlzOrt();
				data[0][AngebotReportFac.REPORT_ADRESSETIKETT_LAND] = partnerDto.getLandplzortDto().getLandDto()
						.getCName();
			}

			if (ansprechpartnerIId != null) {
				AnsprechpartnerDto ansprechpartnerDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(ansprechpartnerIId, theClientDto);
				data[0][AngebotReportFac.REPORT_ADRESSETIKETT_ZHD_NAME] = getPartnerFac()
						.formatFixAnredeTitelName2Name1(ansprechpartnerDto.getPartnerDto(),
								Helper.string2Locale(sLocalePartner), theClientDto);
			}

			PartnerDto partnerMandantDto = getMandantFac()
					.mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto).getPartnerDto();

			data[0][AngebotReportFac.REPORT_ADRESSETIKETT_ABSENDER_NAME] = partnerMandantDto.formatTitelAnrede();
			data[0][AngebotReportFac.REPORT_ADRESSETIKETT_ABSENDER_STRASSE] = partnerMandantDto.getCStrasse();

			if (partnerMandantDto.getLandplzortDto() != null) {
				data[0][AngebotReportFac.REPORT_ADRESSETIKETT_ABSENDER_PLZORT] = partnerMandantDto.getLandplzortDto()
						.formatPlzOrt();
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		HashMap<String, Object> mapParameter = new HashMap<String, Object>();

		initJRDS(mapParameter, AngebotReportFac.REPORT_MODUL, AngebotReportFac.REPORT_ANGEBOT_ADRESSETIKETT,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	/**
	 * Eine Zeile der AG Vorkalkulation mit artikelCNr, CBez, CZBez befuellen. <br>
	 * Die Felder muessen getrennt sein, damit sie im Druck unterschiedlich
	 * formatiert werden koennen.
	 * 
	 * @param aDataI              Object[][] das Array der zu druckenden Daten
	 * @param iZeileI             in welche Zeile wird die Information geschrieben
	 * @param artikelDtoI         der anzudruckende Artikel
	 * @param cPraefixArtikelCNrI die Einrueckung fuer Stuecklistenpositionen
	 * @param cBezUebersteuertI   die Artikelbezeichnung kann uebersteuert sein
	 * @param theClientDto        der aktuelle Benutzer
	 * @return Object[][] das Array der zu druckenden Daten
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileVorkalkulationMitArtikelCNrCBezCZBez(Object[] aDataI, ArtikelDto artikelDtoI,
			String cPraefixArtikelCNrI, String cBezUebersteuertI, TheClientDto theClientDto) throws EJBExceptionLP {

		aDataI[AngebotReportFac.REPORT_VORKALKULATION_ARTIKELART] = artikelDtoI.getArtikelartCNr();

		if (artikelDtoI.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_IDENT] = cPraefixArtikelCNrI
					+ AngebotReportFac.REPORT_VORKALKULATION_ZEICHEN_FUER_HANDEINGABE;
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_BEZEICHNUNG] = artikelDtoI.getArtikelsprDto().getCBez();
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_ZUSATZBEZEICHNUNG] = artikelDtoI.getArtikelsprDto()
					.getCZbez();
		} else {
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_IDENT] = cPraefixArtikelCNrI + artikelDtoI.getCNr();

			aDataI[AngebotReportFac.REPORT_VORKALKULATION_MELDEPFLICHTIG] = Helper
					.short2Boolean(artikelDtoI.getBMeldepflichtig());
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_BEWILLIGUNGSPFLICHTIG] = Helper
					.short2Boolean(artikelDtoI.getBBewilligungspflichtig());

			StuecklisteDto stklDto = getStuecklisteFac()
					.stuecklisteFindByArtikelIIdMandantCNrOhneExc(artikelDtoI.getIId(), theClientDto.getMandant());
			if (stklDto != null) {
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_STUECKLISTEART] = stklDto.getStuecklisteartCNr();
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_FREMDFERTIGUNG] = Helper
						.short2Boolean(stklDto.getBFremdfertigung());
			}

			if (artikelDtoI.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_ARTIKEL_IST_ARBEITSZEIT] = Boolean.TRUE;
			} else {
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_ARTIKEL_IST_ARBEITSZEIT] = Boolean.FALSE;
			}

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
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_ZUSATZBEZEICHNUNG] = artikelDtoI.getArtikelsprDto()
					.getCZbez();
		}

		return aDataI;
	}

	/**
	 * Eine Zeile der AG Vorkalkulation mit der Stuecklisteninformation befuellen.
	 * 
	 * @param indexGruppeIIdI               zu welcher Gruppe werden die Werte
	 *                                      addiert
	 * @param cPraefixArtikelCNrI           Praefix fuer die Einrueckung der
	 *                                      artikelCNr
	 * @param stuecklisteMitStrukturDtoI    die anzudruckende Stuecklistenposition
	 * @param waehrungCNrI                  die gewuenschte Waehrung
	 * @param nUebergeordneteMengeI         die Menge der Basisposition zum
	 *                                      Andrucken der Zwischensumme
	 * @param cNrUebergeordneteEinheitI     die EInheit der Basisposition zum
	 *                                      Andrucken der Zwischensumme
	 * @param iStuecklistenAufloesungTiefeI bis in welche Tiefe werden die STKL
	 *                                      Position aufgeloest
	 * @param bHauptlager                   boolean
	 * @param tRealisierungstermin          der Realisierungstermin des Angebots
	 * @param theClientDto                  der aktuelle Benutzer
	 * @return Object[] die neue Zeile
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileVorkalkulationMitStuecklistenposition(Integer indexGruppeIIdI,
			String cPraefixArtikelCNrI, StuecklisteMitStrukturDto stuecklisteMitStrukturDtoI, String waehrungCNrI,
			BigDecimal nUebergeordneteMengeI, String cNrUebergeordneteEinheitI, int iStuecklistenAufloesungTiefeI,
			boolean bHauptlager, Timestamp tRealisierungstermin, Date preisGueltig, Short ruestmenge,
			TheClientDto theClientDto) throws EJBExceptionLP {

		Object[] aDataI = new Object[AngebotReportFac.REPORT_VORKALKULATION_ANZAHL_SPALTEN];
		try {

			LagerDto hauptlagerDto = getLagerFac().getHauptlagerDesMandanten(theClientDto);

			// Pro Position eine kuenstliche Zeile zum Andrucken erzeugen,
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_STUECKLISTENPOSITION;

			StuecklistepositionDto stuecklistepositionDto = stuecklisteMitStrukturDtoI.getStuecklistepositionDto();

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(stuecklistepositionDto.getArtikelIId(),
					theClientDto);

			// Einrueckung 1x fuer Stuecklistenposition der STKL plus die Ebene
			cPraefixArtikelCNrI += "    ";

			for (int j = 0; j < stuecklisteMitStrukturDtoI.getIEbene(); j++) {
				cPraefixArtikelCNrI = cPraefixArtikelCNrI + "    ";
			}

			aDataI = befuelleZeileVorkalkulationMitArtikelCNrCBezCZBez(aDataI, artikelDto, cPraefixArtikelCNrI, null, // keine
					// Uebersteuerung
					// der cBez
					// des
					// Artikels
					theClientDto);

			// es wurde die Gesamtmenge der Unterpositionen in der STKL
			// berechnet

			BigDecimal zielmenge = stuecklistepositionDto.getNZielmenge(nUebergeordneteMengeI);

			if (ruestmenge != null) {
				aDataI[REPORT_VORKALKULATION_RUESTMENGE] = Helper.short2boolean(ruestmenge);
			}

			aDataI[REPORT_VORKALKULATION_INITIALKOSTEN] = Helper.short2boolean(stuecklistepositionDto.getBInitial());
			
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE] = zielmenge;
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT] = stuecklistepositionDto.getEinheitCNr();
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE_UEBERGEORDNET] = nUebergeordneteMengeI;
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT_UEBERGEORDNET] = cNrUebergeordneteEinheitI;
			if (tRealisierungstermin != null) {
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_FIKTIVER_LAGERSTAND] = getInternebestellungFac()
						.getFiktivenLagerstandZuZeitpunkt(artikelDto, theClientDto, tRealisierungstermin,
								hauptlagerDto.getPartnerIIdStandort());
			} else {
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_FIKTIVER_LAGERSTAND] = null;
			}

			// die Gestehungspreise duerfen nur in der ersten Ebene nach der
			// STKL angedruckt werden,
			// sonst werden Unterpositionen mehrfach bewertet!!!
			// if (stuecklisteMitStrukturDtoI.getIEbene() == 0) {}

			if (artikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
				// WH xx.03.06 Handeingaben in STKL haben keinen Gestehungspreis
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS] = new BigDecimal(0);
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT] = new BigDecimal(0);
			} else {
				StuecklisteAufgeloest stuecklisteInfoDto = stuecklisteFacLocalBean
						.getStrukturdatenEinesArtikelsStrukturiert(artikelDto.getIId(), false,
								StuecklisteFacLocal.STUECKLISTENAUFLOESUNGSTIEFE_UNBEGRENZT, true,
								stuecklistepositionDto.getNMenge(), false, false, theClientDto);

				// IMS 1802 Die Gestehungspreise einer STKL Position werden
				// angedruckt, wenn
				// - die Position keine STKL ist
				// - die Position keine echte STKL ist, d.h. sie ist eine
				// Fremdfertigung oder hat keine Positionen
				if (stuecklisteInfoDto.getAnzahlPositionen() == 0 || stuecklisteInfoDto.isBFremdfertigung()
						|| stuecklisteMitStrukturDtoI.getIEbene() < iStuecklistenAufloesungTiefeI) {
					// das Hauptlager des Mandanten bestimmen
					LagerDto hauptlager = getLagerFac().getHauptlagerDesMandanten(theClientDto);

					// Grundlage ist der Gestehungspreis des Artikels am
					// Hauptlager des Mandanten
					// CK 23.06.2006: ODER Gemittelter Gestpreis aller Laeger
					// (je
					// nach Mandanteparameter
					// GESTPREISBERECHNUNG_HAUPTLAGER)
					BigDecimal gestehungspreisInBelegwaehrung = getGestehungspreisInBelegwaehrungUndParameterGESTPREISBERECHNUNG_HAUPTLAGER(
							waehrungCNrI, bHauptlager, theClientDto, stuecklistepositionDto.getArtikelIId(),
							preisGueltig, hauptlager);

					if (stuecklisteInfoDto.getAnzahlPositionen() == 0 || stuecklisteMitStrukturDtoI.isBFremdfertigung()
							|| !stuecklisteMitStrukturDtoI.isBStueckliste()) {
						aDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS] = gestehungspreisInBelegwaehrung;
						aDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT] = gestehungspreisInBelegwaehrung
								.multiply(zielmenge);
					}
				}

				try {
					befuelleZeileVKMitArtikellieferantDaten(aDataI, artikelDto, zielmenge, waehrungCNrI, preisGueltig,
							theClientDto);

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

	private void befuelleZeileVKMitArtikellieferantDaten(Object[] aDataI, ArtikelDto artikelDto, BigDecimal zielmenge,
			String waehrungCNrI, Date preisGueltig, TheClientDto theClientDto) throws RemoteException {
		ArtikellieferantDto artikellieferantDto = getArtikelFac().getArtikelEinkaufspreis(artikelDto.getIId(), null,
				zielmenge, waehrungCNrI, preisGueltig, theClientDto);

		if (artikellieferantDto != null) {

			aDataI[REPORT_VORKALKULATION_WIEDERBESCHAFFUNGSZEIT] = artikellieferantDto.getIWiederbeschaffungszeit();

			if (artikellieferantDto.getLieferantIId() != null) {
				LieferantDto liefDto = getLieferantFac()
						.lieferantFindByPrimaryKey(artikellieferantDto.getLieferantIId(), theClientDto);

				aDataI[REPORT_VORKALKULATION_LIEFERANT] = liefDto.getPartnerDto().formatFixName1Name2();

				if (liefDto.getNTransportkostenprolieferung() != null) {
					aDataI[REPORT_VORKALKULATION_TPKOSTENLIEFERUNG] = getLocaleFac()
							.rechneUmInAndereWaehrungZuDatumOhneExc(liefDto.getNTransportkostenprolieferung(),
									liefDto.getWaehrungCNr(), waehrungCNrI, new Date(System.currentTimeMillis()),
									theClientDto);
				}

				if (artikellieferantDto != null && artikellieferantDto.getLief1Preis() != null) {

					aDataI[REPORT_VORKALKULATION_LIEF1PREIS] = artikellieferantDto.getLief1Preis();
					aDataI[REPORT_VORKALKULATION_LIEF1PREISGUELTIGAB] = artikellieferantDto.getTPreisgueltigab();
					aDataI[REPORT_VORKALKULATION_LIEF1PREISGUELTIGBIS] = artikellieferantDto.getTPreisgueltigbis();

				}

				artikellieferantDto = getArtikelFac().getArtikelEinkaufspreis(artikelDto.getIId(), null, zielmenge,
						waehrungCNrI, preisGueltig, theClientDto);
				if (artikellieferantDto != null && artikellieferantDto.getLief1Preis() != null) {
					aDataI[REPORT_VORKALKULATION_LIEF1PREISSTAFFEL] = artikellieferantDto.getLief1Preis();

				}

			}

		}
	}

	private BigDecimal getGestehungspreisInBelegwaehrungUndParameterGESTPREISBERECHNUNG_HAUPTLAGER(String waehrungCNrI,
			boolean bHauptlager, TheClientDto theClientDto, Integer artikelIId, Date tDatum, LagerDto hauptlager)
			throws RemoteException {
		BigDecimal gestehungspreisInMandantenwaehrung = null;
		ArtikelDto artikel = getArtikelFac().artikelFindByPrimaryKeySmall(artikelIId, theClientDto);
		// Bei Arbeitszeit ist GestPreis = Lief1Preis. In LagerFac Methode wird dafuer
		// aber immer heutiges Datum verwendet, bei Vorkalk muss Datum aber eingestellt
		// werden koennen
		if (ArtikelFac.ARTIKELART_ARBEITSZEIT.equals(artikel.getArtikelartCNr())) {
			ArtikellieferantDto dto = getArtikelFac().getArtikelEinkaufspreisDesBevorzugtenLieferantenZuDatum(
					artikelIId, new BigDecimal(1), theClientDto.getSMandantenwaehrung(), tDatum, theClientDto);
			if (dto != null) {
				if (dto.getNNettopreis() != null) {
					return dto.getNNettopreis();
				} else {
					return new BigDecimal(0);
				}
			} else {
				return new BigDecimal(0);
			}
		} else if (bHauptlager) {
			gestehungspreisInMandantenwaehrung = getLagerFac().getGemittelterGestehungspreisEinesLagers(artikelIId,
					artikel.getBLagerbewirtschaftet(), artikel.getArtikelartCNr(), hauptlager.getIId(), theClientDto);
		} else {
			gestehungspreisInMandantenwaehrung = getLagerFac()
					.getGemittelterGestehungspreisAllerLaegerEinesMandanten(artikelIId, theClientDto);

		}
		// Umrechnen in Belegwaehrung
		BigDecimal gestehungspreisInBelegwaehrung = getLocaleFac().rechneUmInAndereWaehrungZuDatum(
				gestehungspreisInMandantenwaehrung, theClientDto.getSMandantenwaehrung(), waehrungCNrI,
				new Date(System.currentTimeMillis()), theClientDto);
		return gestehungspreisInBelegwaehrung;
	}

	private Object[] befuelleZeileVorkalkulationMitAgstklarbeitsplan(Integer indexGruppeIIdI,
			String cPraefixArtikelCNrI, AgstklarbeitsplanDto agstklarbeitsplanDto, String waehrungCNrI,
			BigDecimal nUebergeordneteMengeI, String cNrUebergeordneteEinheitI, int iStuecklistenAufloesungTiefeI,
			boolean bHauptlager, Timestamp tRealisierungstermin, TheClientDto theClientDto, boolean bMaschine,
			Integer partnerIIdStandort, Date preisGueltig) throws EJBExceptionLP {

		Object[] aDataI = new Object[AngebotReportFac.REPORT_VORKALKULATION_ANZAHL_SPALTEN];
		try {

			// Pro Position eine kuenstliche Zeile zum Andrucken erzeugen,
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_STUECKLISTENPOSITION;

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(agstklarbeitsplanDto.getArtikelIId(),
					theClientDto);

			if (bMaschine == true && agstklarbeitsplanDto.getMaschineIId() != null) {

				MaschineDto maschineDto = getZeiterfassungFac()
						.maschineFindByPrimaryKey(agstklarbeitsplanDto.getMaschineIId());
				if (artikelDto.getArtikelsprDto() != null) {
					artikelDto.getArtikelsprDto().setCBez(maschineDto.getCBez());
				}
				artikelDto.setCNr("M:" + maschineDto.getCInventarnummer());
			}

			cPraefixArtikelCNrI += "    ";
			aDataI = befuelleZeileVorkalkulationMitArtikelCNrCBezCZBez(aDataI, artikelDto, cPraefixArtikelCNrI, null, // keine
					// Uebersteuerung
					// der cBez
					// des
					// Artikels
					theClientDto);

			// es wurde die Gesamtmenge der Unterpositionen in der STKL
			// berechnet
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_AGSTKL_MATERIAL] = Boolean.FALSE;

			aDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE] = agstklarbeitsplanDto.getNMenge();
			
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_INITIALKOSTEN] = Helper.short2boolean(agstklarbeitsplanDto.getBInitial());
			
			if (artikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_ARTIKEL_IST_ARBEITSZEIT] = Boolean.TRUE;
			} else {
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_ARTIKEL_IST_ARBEITSZEIT] = Boolean.FALSE;
			}
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT] = agstklarbeitsplanDto.getEinheitCNr();
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE_UEBERGEORDNET] = nUebergeordneteMengeI;
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT_UEBERGEORDNET] = cNrUebergeordneteEinheitI;
			if (tRealisierungstermin != null) {
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_FIKTIVER_LAGERSTAND] = getInternebestellungFac()
						.getFiktivenLagerstandZuZeitpunkt(artikelDto, theClientDto, tRealisierungstermin,
								partnerIIdStandort);
			} else {
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_FIKTIVER_LAGERSTAND] = null;
			}

			// die Gestehungspreise duerfen nur in der ersten Ebene nach der
			// STKL angedruckt werden,
			// sonst werden Unterpositionen mehrfach bewertet!!!
			// if (stuecklisteMitStrukturDtoI.getIEbene() == 0) {}

			if (artikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
				// WH xx.03.06 Handeingaben in STKL haben keinen Gestehungspreis
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS] = new BigDecimal(0);
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT] = new BigDecimal(0);
			} else {

				// das Hauptlager des Mandanten bestimmen
				LagerDto hauptlager = getLagerFac().getHauptlagerDesMandanten(theClientDto);

				// Grundlage ist der Gestehungspreis des Artikels am
				// Hauptlager des Mandanten
				// CK 23.06.2006: ODER Gemittelter Gestpreis aller Laeger (je
				// nach Mandanteparameter
				// GESTPREISBERECHNUNG_HAUPTLAGER)
				BigDecimal gestehungspreisInBelegwaehrung = null;
				if (bMaschine == true && agstklarbeitsplanDto.getMaschineIId() != null) {

					if (agstklarbeitsplanDto.getNStundensatzMaschine() != null) {
						gestehungspreisInBelegwaehrung = agstklarbeitsplanDto.getNStundensatzMaschine();
					} else {
						BigDecimal gestehungspreisInMandantenwaehrung = getZeiterfassungFac()
								.getMaschinenKostenZumZeitpunkt(agstklarbeitsplanDto.getMaschineIId(),
										new java.sql.Timestamp(System.currentTimeMillis()), LocaleFac.BELEGART_AGSTUECKLISTE, agstklarbeitsplanDto.getIId()).getBdStundensatz();
						// Umrechnen in Belegwaehrung
						gestehungspreisInBelegwaehrung = getLocaleFac().rechneUmInAndereWaehrungZuDatum(
								gestehungspreisInMandantenwaehrung, theClientDto.getSMandantenwaehrung(), waehrungCNrI,
								new Date(System.currentTimeMillis()), theClientDto);
					}

				} else {

					if (agstklarbeitsplanDto.getNStundensatzMann() != null) {
						gestehungspreisInBelegwaehrung = agstklarbeitsplanDto.getNStundensatzMann();
					} else {
						gestehungspreisInBelegwaehrung = getGestehungspreisInBelegwaehrungUndParameterGESTPREISBERECHNUNG_HAUPTLAGER(
								waehrungCNrI, bHauptlager, theClientDto, agstklarbeitsplanDto.getArtikelIId(),
								preisGueltig, hauptlager);
					}

				}

				BigDecimal bdGesamtzeitInStunden = Helper.berechneGesamtzeitInStunden(
						agstklarbeitsplanDto.getLRuestzeit(), agstklarbeitsplanDto.getLStueckzeit(),
						nUebergeordneteMengeI, null, agstklarbeitsplanDto.getIAufspannung());
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE] = bdGesamtzeitInStunden;
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS] = gestehungspreisInBelegwaehrung;
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT] = gestehungspreisInBelegwaehrung
						.multiply(bdGesamtzeitInStunden);

				try {
					befuelleZeileVKMitArtikellieferantDaten(aDataI, artikelDto, bdGesamtzeitInStunden, waehrungCNrI,
							preisGueltig, theClientDto);

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

	private Object[] befuelleZeileVorkalkulationMitStuecklistearbeitsplan(Integer indexGruppeIIdI,
			String cPraefixArtikelCNrI, StuecklisteMitStrukturDto stuecklisteMitStrukturDtoI, String waehrungCNrI,
			BigDecimal nUebergeordneteMengeI, String cNrUebergeordneteEinheitI, int iStuecklistenAufloesungTiefeI,
			boolean bHauptlager, Timestamp tRealisierungstermin, StuecklisteDto stklDto, TheClientDto theClientDto,
			Date preisGueltig, Integer partnerIIdStandort) throws EJBExceptionLP {

		Object[] aDataI = new Object[AngebotReportFac.REPORT_VORKALKULATION_ANZAHL_SPALTEN];
		try {

			StuecklistearbeitsplanDto stuecklistearbeitsplanDto = stuecklisteMitStrukturDtoI
					.getStuecklistearbeitsplanDto();

			// Pro Position eine kuenstliche Zeile zum Andrucken erzeugen,
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_STUECKLISTENPOSITION;

			ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(stuecklistearbeitsplanDto.getArtikelIId(), theClientDto);

			if (stuecklistearbeitsplanDto.getMaschineIId() != null) {

				MaschineDto maschineDto = getZeiterfassungFac()
						.maschineFindByPrimaryKey(stuecklistearbeitsplanDto.getMaschineIId());
				if (artikelDto.getArtikelsprDto() != null) {
					artikelDto.getArtikelsprDto().setCBez(maschineDto.getCBez());
				}
				artikelDto.setCNr("M:" + maschineDto.getCInventarnummer());
			}

			cPraefixArtikelCNrI += "    ";
			for (int j = 0; j < stuecklisteMitStrukturDtoI.getIEbene(); j++) {
				cPraefixArtikelCNrI = cPraefixArtikelCNrI + "    ";
			}

			aDataI = befuelleZeileVorkalkulationMitArtikelCNrCBezCZBez(aDataI, artikelDto, cPraefixArtikelCNrI, null, // keine
					// Uebersteuerung
					// der cBez
					// des
					// Artikels
					theClientDto);

			// es wurde die Gesamtmenge der Unterpositionen in der STKL
			// berechnet
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE] = stuecklistearbeitsplanDto.getNMenge();
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT] = stuecklistearbeitsplanDto.getEinheitCNr();
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE_UEBERGEORDNET] = nUebergeordneteMengeI;
			aDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT_UEBERGEORDNET] = cNrUebergeordneteEinheitI;
			if (tRealisierungstermin != null) {
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_FIKTIVER_LAGERSTAND] = getInternebestellungFac()
						.getFiktivenLagerstandZuZeitpunkt(artikelDto, theClientDto, tRealisierungstermin,
								partnerIIdStandort);
			} else {
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_FIKTIVER_LAGERSTAND] = null;
			}

			aDataI[AngebotReportFac.REPORT_VORKALKULATION_INITIALKOSTEN] = Helper.short2boolean(stuecklistearbeitsplanDto.getBInitial());
			
			// die Gestehungspreise duerfen nur in der ersten Ebene nach der
			// STKL angedruckt werden,
			// sonst werden Unterpositionen mehrfach bewertet!!!
			// if (stuecklisteMitStrukturDtoI.getIEbene() == 0) {}

			if (artikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
				// WH xx.03.06 Handeingaben in STKL haben keinen Gestehungspreis
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS] = new BigDecimal(0);
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT] = new BigDecimal(0);
			} else {

				// das Hauptlager des Mandanten bestimmen
				LagerDto hauptlager = getLagerFac().getHauptlagerDesMandanten(theClientDto);

				// Grundlage ist der Gestehungspreis des Artikels am
				// Hauptlager des Mandanten
				// CK 23.06.2006: ODER Gemittelter Gestpreis aller Laeger (je
				// nach Mandanteparameter
				// GESTPREISBERECHNUNG_HAUPTLAGER)
				BigDecimal gestehungspreisInBelegwaehrung = null;
				if (stuecklistearbeitsplanDto.getMaschineIId() != null) {
					BigDecimal gestehungspreisInMandantenwaehrung = getZeiterfassungFac()
							.getMaschinenKostenZumZeitpunkt(stuecklistearbeitsplanDto.getMaschineIId(),
									new Timestamp(preisGueltig.getTime()),LocaleFac.BELEGART_STUECKLISTE,stuecklistearbeitsplanDto.getIId()).getBdStundensatz();
					// Umrechnen in Belegwaehrung
					gestehungspreisInBelegwaehrung = getLocaleFac().rechneUmInAndereWaehrungZuDatum(
							gestehungspreisInMandantenwaehrung, theClientDto.getSMandantenwaehrung(), waehrungCNrI,
							new Date(System.currentTimeMillis()), theClientDto);
				} else {
					gestehungspreisInBelegwaehrung = getGestehungspreisInBelegwaehrungUndParameterGESTPREISBERECHNUNG_HAUPTLAGER(
							waehrungCNrI, bHauptlager, theClientDto, stuecklistearbeitsplanDto.getArtikelIId(),
							preisGueltig, hauptlager);
				}

				BigDecimal bdGesamtzeitInStunden = Helper.berechneGesamtzeitInStunden(
						stuecklistearbeitsplanDto.getLRuestzeit(), stuecklistearbeitsplanDto.getLStueckzeit(),
						nUebergeordneteMengeI, null, stuecklistearbeitsplanDto.getIAufspannung());
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE] = bdGesamtzeitInStunden;
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS] = gestehungspreisInBelegwaehrung;
				aDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT] = gestehungspreisInBelegwaehrung
						.multiply(bdGesamtzeitInStunden);

				try {
					befuelleZeileVKMitArtikellieferantDaten(aDataI, artikelDto, bdGesamtzeitInStunden, waehrungCNrI,
							preisGueltig, theClientDto);

					// SP4087
					if (stuecklistearbeitsplanDto.getMaschineIId() != null) {
						aDataI[REPORT_VORKALKULATION_LIEF1PREISSTAFFEL] = aDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS];
						aDataI[REPORT_VORKALKULATION_LIEF1PREIS] = aDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS];

						aDataI[REPORT_VORKALKULATION_LIEF1PREISGUELTIGAB] = null;
						aDataI[REPORT_VORKALKULATION_LIEF1PREISGUELTIGBIS] = null;

						aDataI[REPORT_VORKALKULATION_WIEDERBESCHAFFUNGSZEIT] = null;
						aDataI[REPORT_VORKALKULATION_TPKOSTENLIEFERUNG] = null;
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
	 * @param aaDataI                   Object[][] das Array der zu druckenden Daten
	 * @param iZeileI                   die aktuelle Zeile, die zu befuellen ist
	 * @param agstklpositionDtoI        die zu druckende AGSTKL Position
	 * @param nUebergeordneteMengeI     die Menge der Basisposition zum Andrucken
	 *                                  der Zwischensumme
	 * @param cNrUebergeordneteEinheitI die EInheit der Basisposition zum Andrucken
	 *                                  der Zwischensumme
	 * @param theClientDto              der aktuelle Benutzer
	 * @param cPraefixArtikelCNrI       die Einrueckung fuer die Unterstuecklisten
	 * @return Object[][] das Array der zu druckenden Daten
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileVKMitHandAusAGSTKLPosition(Integer indexGruppe, AgstklpositionDto agstklpositionDtoI,
			BigDecimal nUebergeordneteMengeI, String cNrUebergeordneteEinheitI, TheClientDto theClientDto,
			String cPraefixArtikelCNrI) throws EJBExceptionLP {
		Object[] aaDataI = new Object[AngebotReportFac.REPORT_VORKALKULATION_ANZAHL_SPALTEN];

		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(agstklpositionDtoI.getArtikelIId(),
				theClientDto);

		BigDecimal nAnzudruckendeMenge = agstklpositionDtoI.getNMenge(); // .
		// multiply
		// (
		// nUebergeordneteMengeI
		// )
		// ;

		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_IDENT] = cPraefixArtikelCNrI
				+ AngebotReportFac.REPORT_VORKALKULATION_ZEICHEN_FUER_HANDEINGABE;
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_ARTIKEL_IST_ARBEITSZEIT] = Boolean.FALSE;
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCZbez();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE] = nAnzudruckendeMenge;
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT] = agstklpositionDtoI.getEinheitCNr();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE_UEBERGEORDNET] = nUebergeordneteMengeI;
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT_UEBERGEORDNET] = cNrUebergeordneteEinheitI;
		if (agstklpositionDtoI.getNNettogesamtpreis() != null) {
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS] = agstklpositionDtoI.getNNettogesamtpreis();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT] = agstklpositionDtoI.getNNettogesamtpreis()
					.multiply(nUebergeordneteMengeI).multiply(agstklpositionDtoI.getNMenge());
		}
		// in einer AGSTKL gilt der Verkaufswert der AGSTKL
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_HANDEINGABE;
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_INDEX_GRUPPE] = indexGruppe; // fuer
																					// die
																					// Bildung
																					// der
																					// Zwischensumme!

		return aaDataI;
	}

	private Object[] befuelleZeileVKMitMaterialAusAGSTKLPosition(Integer indexGruppe,
			AgstklmaterialDto agstklpositionDtoI, BigDecimal nUebergeordneteMengeI, String cNrUebergeordneteEinheitI,
			TheClientDto theClientDto, String cPraefixArtikelCNrI) throws EJBExceptionLP {
		Object[] aaDataI = new Object[AngebotReportFac.REPORT_VORKALKULATION_ANZAHL_SPALTEN];

		BigDecimal nAnzudruckendeMenge = BigDecimal.ONE; // .
		// multiply
		// (
		// nUebergeordneteMengeI
		// )
		// ;

		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_IDENT] = cPraefixArtikelCNrI
				+ AngebotReportFac.REPORT_VORKALKULATION_ZEICHEN_FUER_HANDEINGABE;
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_ARTIKEL_IST_ARBEITSZEIT] = Boolean.FALSE;

		String cBez = agstklpositionDtoI.getCBez();

		if (agstklpositionDtoI.getNDimension1() != null) {
			cBez += " " + Helper.formatZahl(agstklpositionDtoI.getNDimension1(), 2, theClientDto.getLocUi());
		}
		if (agstklpositionDtoI.getNDimension2() != null) {
			cBez += " x " + Helper.formatZahl(agstklpositionDtoI.getNDimension2(), 2, theClientDto.getLocUi());
		}
		if (agstklpositionDtoI.getNDimension3() != null) {
			cBez += " x " + Helper.formatZahl(agstklpositionDtoI.getNDimension3(), 2, theClientDto.getLocUi());
		}

		if (agstklpositionDtoI.getNGewicht() != null) {
			cBez += " " + Helper.formatZahl(agstklpositionDtoI.getNGewicht(), 2, theClientDto.getLocUi()) + "kg";
		}

		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_BEZEICHNUNG] = cBez;

		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_AGSTKL_MATERIAL] = Boolean.TRUE;

		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE] = nAnzudruckendeMenge.multiply(nUebergeordneteMengeI);
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT] = SystemFac.EINHEIT_STUECK;
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE_UEBERGEORDNET] = nUebergeordneteMengeI;
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT_UEBERGEORDNET] = cNrUebergeordneteEinheitI;
		if (agstklpositionDtoI.getNGewichtpreis() != null) {
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_LIEF1PREIS] = agstklpositionDtoI.getNGewichtpreis();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS] = agstklpositionDtoI.getNGewichtpreis();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT] = agstklpositionDtoI.getNGewichtpreis()
					.multiply(nUebergeordneteMengeI);
		}
		// in einer AGSTKL gilt der Verkaufswert der AGSTKL
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_HANDEINGABE;
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_INDEX_GRUPPE] = indexGruppe;

		return aaDataI;
	}

	/**
	 * Eine Zeile der VK mit einer Identposition/STKL aus einer AGSTKL befuellen.
	 * 
	 * @param aaDataI                   Object[][] das Array der zu druckenden Daten
	 * @param iZeileI                   die aktuelle Zeile, die zu befuellen ist
	 * @param agstklpositionDtoI        die zu druckende AGSTKL Position
	 * @param nUebergeordneteMengeI     die Menge der Basisposition zum Andrucken
	 *                                  der Zwischensumme
	 * @param cNrUebergeordneteEinheitI die EInheit der Basisposition zum Andrucken
	 *                                  der Zwischensumme
	 * @param theClientDto              der aktuelle Benutzer
	 * @param cPraefixArtikelCNrI       die Einrueckung fuer die Unterstuecklisten
	 * @return Object[][] das Array der zu druckenden Daten
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileVKMitIdentOhneSTKLAusAGSTKLPosition(Integer indexGruppe,
			AgstklpositionDto agstklpositionDtoI, BigDecimal nUebergeordneteMengeI, String cNrUebergeordneteEinheitI,
			TheClientDto theClientDto, Timestamp tRealisierungstermin, String cPraefixArtikelCNrI, LagerDto hauptlager,
			boolean bHauptlager, String waehrungCNrI, AgstklDto agstklDto, Integer partnerIIdStandort,
			Date preisGueltig) throws RemoteException {
		Object[] aaDataI = new Object[AngebotReportFac.REPORT_VORKALKULATION_ANZAHL_SPALTEN];

		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(agstklpositionDtoI.getArtikelIId(),
				theClientDto);

		BigDecimal nAnzudruckendeMenge = agstklpositionDtoI.getNMenge().multiply(nUebergeordneteMengeI);

		if (artikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_ARTIKEL_IST_ARBEITSZEIT] = Boolean.TRUE;
		} else {
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_ARTIKEL_IST_ARBEITSZEIT] = Boolean.FALSE;
		}
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_IDENT] = cPraefixArtikelCNrI + artikelDto.getCNr();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MELDEPFLICHTIG] = Helper
				.short2Boolean(artikelDto.getBMeldepflichtig());
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_BEWILLIGUNGSPFLICHTIG] = Helper
				.short2Boolean(artikelDto.getBBewilligungspflichtig());

		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCZbez();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE] = agstklpositionDtoI.getNMenge();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT] = agstklpositionDtoI.getEinheitCNr();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE_UEBERGEORDNET] = nUebergeordneteMengeI;
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT_UEBERGEORDNET] = cNrUebergeordneteEinheitI;
		// in einer AGSTKL gilt der Verkaufswert der AGSTKL
		BigDecimal gestehungspreis = getGestehungspreisInBelegwaehrungUndParameterGESTPREISBERECHNUNG_HAUPTLAGER(
				waehrungCNrI, bHauptlager, theClientDto, agstklpositionDtoI.getArtikelIId(), preisGueltig, hauptlager);
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS] = gestehungspreis;
		if (gestehungspreis != null) {
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT] = gestehungspreis
					.multiply(nAnzudruckendeMenge);
		}
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_IDENT;
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_INDEX_GRUPPE] = indexGruppe; // fuer
																					// die
																					// Bildung
																					// der
																					// Zwischensumme!
		if (tRealisierungstermin != null) {
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_FIKTIVER_LAGERSTAND] = getInternebestellungFac()
					.getFiktivenLagerstandZuZeitpunkt(artikelDto, theClientDto, tRealisierungstermin,
							partnerIIdStandort);
		} else {
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_FIKTIVER_LAGERSTAND] = null;
		}

		if (agstklDto.getIEkpreisbasis().intValue() == AngebotstklFac.EK_PREISBASIS_LIEF1PREIS) {

			ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreisDesBevorzugtenLieferanten(
					agstklpositionDtoI.getArtikelIId(),

					nAnzudruckendeMenge, waehrungCNrI, theClientDto);
			if (alDto != null) {
				aaDataI[AngebotReportFac.REPORT_VORKALKULATION_LIEF1PREIS] = alDto.getNNettopreis();
				aaDataI[AngebotReportFac.REPORT_VORKALKULATION_LIEF1PREISSTAFFEL] = alDto.getNNettopreis();
			}
		} else {
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_LIEF1PREIS] = agstklpositionDtoI.getNNettogesamtpreis();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_LIEF1PREISSTAFFEL] = agstklpositionDtoI
					.getNNettogesamtpreis();
		}

		return aaDataI;
	}

	/**
	 * Eine Zeile der VK mit einer Identposition aus einer AGSTKL befuellen.
	 * 
	 * @param aaDataI                   Object[][] das Array der zu druckenden Daten
	 * @param iZeileI                   die aktuelle Zeile, die zu befuellen ist
	 * @param agstklpositionDtoI        die zu druckende AGSTKL Position
	 * @param nUebergeordneteMengeI     die Menge der Basisposition zum Andrucken
	 *                                  der Zwischensumme
	 * @param cNrUebergeordneteEinheitI die EInheit der Basisposition zum Andrucken
	 *                                  der Zwischensumme
	 * @param theClientDto              der aktuelle Benutzer
	 * @param cPraefixArtikelCNrI       die Einrueckung fuer die Unterstuecklisten
	 * @return Object[][] das Array der zu druckenden Daten
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileVKMitIdentMitSTKLAusAGSTKLPosition(Integer indexGruppe,
			AgstklpositionDto agstklpositionDtoI, BigDecimal nUebergeordneteMengeI, String cNrUebergeordneteEinheitI,
			Timestamp tRealisierungstermin, TheClientDto theClientDto, String cPraefixArtikelCNrI,
			Integer partnerIIdStandort) throws EJBExceptionLP {
		Object[] aaDataI = new Object[AngebotReportFac.REPORT_VORKALKULATION_ANZAHL_SPALTEN];

		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(agstklpositionDtoI.getArtikelIId(),
				theClientDto);

		BigDecimal nAnzudruckendeMenge = agstklpositionDtoI.getNMenge().multiply(nUebergeordneteMengeI);
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_IDENT] = cPraefixArtikelCNrI + artikelDto.getCNr();

		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_ARTIKELART] = artikelDto.getArtikelartCNr();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MELDEPFLICHTIG] = Helper
				.short2Boolean(artikelDto.getBMeldepflichtig());
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_BEWILLIGUNGSPFLICHTIG] = Helper
				.short2Boolean(artikelDto.getBBewilligungspflichtig());

		if (artikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_ARTIKEL_IST_ARBEITSZEIT] = Boolean.TRUE;
		} else {
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_ARTIKEL_IST_ARBEITSZEIT] = Boolean.FALSE;
		}
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCZbez();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE] = nAnzudruckendeMenge;
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT] = agstklpositionDtoI.getEinheitCNr();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE_UEBERGEORDNET] = nUebergeordneteMengeI;
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT_UEBERGEORDNET] = cNrUebergeordneteEinheitI;
		// in einer AGSTKL gilt der Verkaufswert der AGSTKL
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS_MANUELL] = formatBigDecimalAsInfo(
				agstklpositionDtoI.getNGestehungspreis());
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT_MANUELL] = formatBigDecimalAsInfo(
				agstklpositionDtoI.getNGestehungspreis().multiply(nAnzudruckendeMenge));
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_IDENT;
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_INDEX_GRUPPE] = indexGruppe; // fuer
																					// die
																					// Bildung
																					// der
																					// Zwischensumme!
		if (tRealisierungstermin != null) {
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_FIKTIVER_LAGERSTAND] = getInternebestellungFac()
					.getFiktivenLagerstandZuZeitpunkt(artikelDto, theClientDto, tRealisierungstermin,
							partnerIIdStandort);
		} else {
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_FIKTIVER_LAGERSTAND] = null;
		}

		return aaDataI;
	}

	private String formatBigDecimalAsInfo(BigDecimal nZahlI) throws EJBExceptionLP {
		StringBuffer buff = new StringBuffer();

		if (nZahlI != null) {
			buff.append("[").append(Helper.rundeKaufmaennisch(nZahlI, 2)).append("]");
		}

		return buff.toString();
	}

	/**
	 * Eine Zeile der VK mit einer Handposition befuellen.
	 * 
	 * @param aaDataI             Object[][] das Array der zu druckenden Daten
	 * @param iZeileI             die aktuelle Zeile, die zu befuellen ist
	 * @param angebotpositionDtoI die zu druckende Angebotposition
	 * @param theClientDto        der aktuelle Beutzer
	 * @return Object[][] das Array der zu druckenden Daten
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileVKMitAGSTKLPosition(FLRAngebotpositionReport angebotpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Object[] aaDataI = new Object[AngebotReportFac.REPORT_VORKALKULATION_ANZAHL_SPALTEN];
		try {
			AgstklDto agstklDto = getAngebotstklFac().agstklFindByPrimaryKey(angebotpositionDtoI.getAgstkl_i_id());
			BelegartDto belegartDto = getLocaleFac().belegartFindByCNr(LocaleFac.BELEGART_AGSTUECKLISTE);
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_IDENT] = belegartDto.getCKurzbezeichnung()
					+ agstklDto.getCNr(); // die
			// Belegnummer

			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_ARTIKEL_IST_ARBEITSZEIT] = Boolean.FALSE;

			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_AGSTKL_ZEICHNUNGSNUMMER] = agstklDto.getCZeichnungsnummer();
			
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_BEZEICHNUNG] = agstklDto.getCBez();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE] = angebotpositionDtoI.getN_menge();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT] = angebotpositionDtoI.getEinheit_c_nr();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_VERKAUFSPREIS] = angebotpositionDtoI
					.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte();

			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_VERKAUFSWERT] = angebotpositionDtoI
					.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte()
					.multiply(angebotpositionDtoI.getN_menge());
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_AGSTUECKLISTE;
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_INDEX_GRUPPE] = angebotpositionDtoI.getAgstkl_i_id()
					+ angebotpositionDtoI.getI_id(); // fuer
														// die
														// Bildung
														// der
														// Zwischensumme!
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return aaDataI;
	}

	/**
	 * Eine Zeile der VK mit der Handposition einer AG Position befuellen. <br>
	 * Die Position wird immer in Ebene 0 angedruckt.
	 * 
	 * @param aaDataI             Object[][] das Array der zu druckenden Daten
	 * @param iZeileI             die aktuelle Zeile, die zu befuellen ist
	 * @param angebotpositionDtoI die zu druckende AG Position
	 * @param theClientDto        der aktuelle Beutzer
	 * @return Object[][] das Array der zu druckenden Daten
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileVKMitHandAusAngebotposition(FLRAngebotpositionReport angebotpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Object[] aaDataI = new Object[AngebotReportFac.REPORT_VORKALKULATION_ANZAHL_SPALTEN];
		try {
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(angebotpositionDtoI.getArtikel_i_id(),
					theClientDto);
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_POSITIONSNUMMER] = getAngebotpositionFac()
					.getPositionNummer(angebotpositionDtoI.getI_id());
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_IDENT] = AngebotReportFac.REPORT_VORKALKULATION_ZEICHEN_FUER_HANDEINGABE;
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto()
					.getCZbez();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE] = angebotpositionDtoI.getN_menge();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE_UEBERGEORDNET] = angebotpositionDtoI.getN_menge();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT] = angebotpositionDtoI.getEinheit_c_nr();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS] = angebotpositionDtoI
					.getN_gestehungspreis();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT] = angebotpositionDtoI.getN_gestehungspreis()
					.multiply(angebotpositionDtoI.getN_menge());
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
	 * @param aaDataI             Object[][] das Array der zu druckenden Daten
	 * @param iZeileI             die aktuelle Zeile, die zu befuellen ist
	 * @param angebotpositionDtoI die zu druckende AG Position
	 * @param theClientDto        der aktuelle Benutzer
	 * @return Object[][] das Array der zu druckenden Daten
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileVKMitIdentOhneSTKLAusAngebotposition(FLRAngebotpositionReport angebotpositionDtoI,
			Timestamp tRealisierungstermin, TheClientDto theClientDto, String angebotswaehrung, String angebotseinheit,
			Integer partnerIIdStandort, Date preisGueltig) throws EJBExceptionLP {
		Object[] aaDataI = new Object[AngebotReportFac.REPORT_VORKALKULATION_ANZAHL_SPALTEN];
		try {
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(angebotpositionDtoI.getArtikel_i_id(),
					theClientDto);

			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_POSITIONSNUMMER] = getAngebotpositionFac()
					.getPositionNummer(angebotpositionDtoI.getI_id());

			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_IDENT] = artikelDto.getCNr();

			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MELDEPFLICHTIG] = Helper
					.short2Boolean(artikelDto.getBMeldepflichtig());
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_BEWILLIGUNGSPFLICHTIG] = Helper
					.short2Boolean(artikelDto.getBBewilligungspflichtig());

			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_ARTIKELART] = artikelDto.getArtikelartCNr();

			Integer iiLieferzeit = getAngebotServiceFac().getLieferzeitInAngeboteinheit(
					angebotpositionDtoI.getAngebot_i_id(), angebotpositionDtoI.getI_id(), angebotseinheit,
					theClientDto);
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_LIEFERZEIT] = iiLieferzeit;

			aaDataI[REPORT_VORKALKULATION_EK_PREIS] = angebotpositionDtoI.getN_einkaufpreis();
			aaDataI[REPORT_VORKALKULATION_TEXTEINGABE] = angebotpositionDtoI.getX_textinhalt();
			if (angebotpositionDtoI.getLieferant_i_id() != null) {
				LieferantDto liefDto = getLieferantFac()
						.lieferantFindByPrimaryKey(angebotpositionDtoI.getLieferant_i_id(), theClientDto);

				aaDataI[REPORT_VORKALKULATION_LIEFERANT_AUS_POSITION] = liefDto.getPartnerDto().formatFixName1Name2();
			}

			// Typ, wenn Setartikel

			if (angebotpositionDtoI.getPosition_i_id_artikelset() != null) {

				aaDataI[AngebotReportFac.REPORT_VORKALKULATION_SETARTIKEL_TYP] = ArtikelFac.SETARTIKEL_TYP_POSITION;

			} else {

				Session session = null;
				SessionFactory factory = FLRSessionFactory.getFactory();
				session = factory.openSession();
				Criteria crit = session.createCriteria(FLRAngebotposition.class);
				crit.add(Restrictions.eq("position_i_id_artikelset", angebotpositionDtoI.getI_id()));

				int iZeilen = crit.list().size();

				if (iZeilen > 0) {
					aaDataI[AngebotReportFac.REPORT_VORKALKULATION_SETARTIKEL_TYP] = ArtikelFac.SETARTIKEL_TYP_KOPF;
				}
				session.close();

			}

			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto()
					.getCZbez();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE] = angebotpositionDtoI.getN_menge();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT] = angebotpositionDtoI.getEinheit_c_nr();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS] = angebotpositionDtoI
					.getN_gestehungspreis();
			if (angebotpositionDtoI.getN_gestehungspreis() != null) {
				aaDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT] = angebotpositionDtoI
						.getN_gestehungspreis().multiply(angebotpositionDtoI.getN_menge());
			}

			if (Helper.short2boolean(artikelDto.getBKalkulatorisch())) {
				AngebotpositionDto agPosDtoTemp = getAngebotpositionFac()
						.angebotpositionFindByPrimaryKey(angebotpositionDtoI.getI_id(), theClientDto);
				agPosDtoTemp = (AngebotpositionDto) befuellePreisfelderAnhandVKPreisfindung(agPosDtoTemp,
						new Timestamp(angebotpositionDtoI.getFlrangebot().getT_belegdatum().getTime()),
						angebotpositionDtoI.getFlrangebot().getKunde_i_id_angebotsadresse(), angebotswaehrung,
						theClientDto);
				aaDataI[AngebotReportFac.REPORT_VORKALKULATION_VERKAUFSPREIS_KALKULATORISCHER_ARTIKEL] = agPosDtoTemp
						.getNNettoeinzelpreis();
			}

			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_VERKAUFSPREIS] = angebotpositionDtoI
					.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte();

			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_VERKAUFSWERT] = angebotpositionDtoI
					.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte()
					.multiply(angebotpositionDtoI.getN_menge());

			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MATERIALZUSCHLAG] = angebotpositionDtoI
					.getN_materialzuschlag();
			if (artikelDto.getMaterialIId() != null) {
				MaterialDto materialDto = getMaterialFac().materialFindByPrimaryKey(artikelDto.getMaterialIId(),
						theClientDto);
				aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MATERIAL] = materialDto.getBezeichnung();
			}

			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_IDENT;
			if (tRealisierungstermin != null) {
				aaDataI[AngebotReportFac.REPORT_VORKALKULATION_FIKTIVER_LAGERSTAND] = getInternebestellungFac()
						.getFiktivenLagerstandZuZeitpunkt(artikelDto, theClientDto, tRealisierungstermin,
								partnerIIdStandort);
			} else {
				aaDataI[AngebotReportFac.REPORT_VORKALKULATION_FIKTIVER_LAGERSTAND] = null;
			}

			try {
				befuelleZeileVKMitArtikellieferantDaten(aaDataI, artikelDto, angebotpositionDtoI.getN_menge(),
						angebotswaehrung, preisGueltig, theClientDto);
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
	 * @param aaDataI             das Array der zu druckenden Daten
	 * @param iZeileI             die aktuelle Zeile, die zu befuellen ist
	 * @param angebotpositionDtoI die zu druckende AG Position
	 * @param theClientDto        der aktuelle Benutzer
	 * @return Object[][] das Array der zu druckenden Daten
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileVKMitIdentMitSTKLAusAngebotposition(FLRAngebotpositionReport angebotpositionDtoI,
			Timestamp tRealisierungstermin, TheClientDto theClientDto, Integer partnerIIdStandort)
			throws EJBExceptionLP {
		Object[] aaDataI = new Object[AngebotReportFac.REPORT_VORKALKULATION_ANZAHL_SPALTEN];

		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(angebotpositionDtoI.getArtikel_i_id(),
				theClientDto);

		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_IDENT] = artikelDto.getCNr();

		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MELDEPFLICHTIG] = Helper
				.short2Boolean(artikelDto.getBMeldepflichtig());
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_BEWILLIGUNGSPFLICHTIG] = Helper
				.short2Boolean(artikelDto.getBBewilligungspflichtig());

		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_ARTIKELART] = artikelDto.getArtikelartCNr();

		StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByArtikelIIdMandantCNrOhneExc(artikelDto.getIId(),
				theClientDto.getMandant());
		if (stklDto != null) {
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_STUECKLISTEART] = stklDto.getStuecklisteartCNr();
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_FREMDFERTIGUNG] = Helper
					.short2Boolean(stklDto.getBFremdfertigung());
		}

		// Typ, wenn Setartikel

		if (angebotpositionDtoI.getPosition_i_id_artikelset() != null) {

			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_SETARTIKEL_TYP] = ArtikelFac.SETARTIKEL_TYP_POSITION;

		} else {

			Session session = null;
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria crit = session.createCriteria(FLRAngebotposition.class);
			crit.add(Restrictions.eq("position_i_id_artikelset", angebotpositionDtoI.getI_id()));

			int iZeilen = crit.list().size();

			if (iZeilen > 0) {
				aaDataI[AngebotReportFac.REPORT_VORKALKULATION_SETARTIKEL_TYP] = ArtikelFac.SETARTIKEL_TYP_KOPF;
			}
			session.close();

		}

		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCZbez();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MENGE] = angebotpositionDtoI.getN_menge();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT] = angebotpositionDtoI.getEinheit_c_nr();
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS_MANUELL] = formatBigDecimalAsInfo(
				angebotpositionDtoI.getN_gestehungspreis());
		if (angebotpositionDtoI.getN_gestehungspreis() != null) {
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT_MANUELL] = formatBigDecimalAsInfo(
					angebotpositionDtoI.getN_gestehungspreis().multiply(angebotpositionDtoI.getN_menge()));
		}
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_VERKAUFSPREIS] = angebotpositionDtoI
				.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte();

		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MATERIALZUSCHLAG] = angebotpositionDtoI.getN_materialzuschlag();
		if (artikelDto.getMaterialIId() != null) {
			try {
				MaterialDto materialDto = getMaterialFac().materialFindByPrimaryKey(artikelDto.getMaterialIId(),
						theClientDto);
				aaDataI[AngebotReportFac.REPORT_VORKALKULATION_MATERIAL] = materialDto.getBezeichnung();
			} catch (RemoteException ex1) {
				throwEJBExceptionLPRespectOld(ex1);
			}
		}

		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_VERKAUFSWERT] = angebotpositionDtoI
				.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte().multiply(angebotpositionDtoI.getN_menge());
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_IDENT;
		aaDataI[AngebotReportFac.REPORT_VORKALKULATION_INDEX_GRUPPE] = angebotpositionDtoI.getI_id();
		if (tRealisierungstermin != null) {
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_FIKTIVER_LAGERSTAND] = getInternebestellungFac()
					.getFiktivenLagerstandZuZeitpunkt(artikelDto, theClientDto, tRealisierungstermin,
							partnerIIdStandort);
		} else {
			aaDataI[AngebotReportFac.REPORT_VORKALKULATION_FIKTIVER_LAGERSTAND] = null;
		}

		return aaDataI;
	}

	/**
	 * Ueber Hibernate alle berechnungsrelevanten Positionen eines Angebots holen.
	 * <br>
	 * Diese Methode mu&szlig; innerhalb einer offenen Hibernate Session aufgerufen
	 * werden. <br>
	 * 
	 * @todo diese Methode muesste eigentlich in der AngebotpositionFac sitzen... PJ
	 *       3799
	 * 
	 * @param sessionI             die Hibernate Session
	 * @param iIdAngebotI          PK des Angebots
	 * @param bNurMengenbehafteteI nur mengenbehaftete Positionen beruecksichtigen
	 * @param bNurPositiveMengenI  nur positive Mengen beruecksichtigen; wird bei
	 *                             !bNurMengenbehafteteI nicht ausgewertet
	 * @param bOhneAlternativenI   alternative Positionen werden nicht
	 *                             beruecksichtigt
	 * @return FLRAngebotpositionReport[] die berechnungsrelevanten Positionen
	 * @throws EJBExceptionLP Ausnahme
	 */
	private FLRAngebotpositionReport[] holeAngebotpositionen(Session sessionI, Integer iIdAngebotI,
			boolean bNurMengenbehafteteI, boolean bNurPositiveMengenI, boolean bOhneAlternativenI)
			throws EJBExceptionLP {
		FLRAngebotpositionReport[] aFLRAngebotpositionReport = null;

		try {
			Criteria crit = sessionI.createCriteria(FLRAngebotpositionReport.class);

			crit.add(Restrictions.eq(AngebotpositionFac.FLR_ANGEBOTPOSITION_ANGEBOT_I_ID, iIdAngebotI));

			if (bNurMengenbehafteteI) {
				crit.add(Restrictions.isNotNull(AngebotpositionFac.FLR_ANGEBOTPOSITION_N_MENGE));

				if (bNurPositiveMengenI) {
					crit.add(Restrictions.gt(AngebotpositionFac.FLR_ANGEBOTPOSITION_N_MENGE, new BigDecimal(0)));
				}
			}

			// nur Positionen beruecksichtigen, die keine Alternative sind
			if (bOhneAlternativenI) {
				crit.add(Restrictions.eq(AngebotpositionFac.FLR_ANGEBOTPOSITION_B_ALTERNATIVE, new Short((short) 0)));
			}
			crit.addOrder(Order.asc("i_sort"));

			// Liste aller Positionen, die behandelt werden sollen
			java.util.List<?> list = crit.list();
			aFLRAngebotpositionReport = new FLRAngebotpositionReport[list.size()];
			aFLRAngebotpositionReport = (FLRAngebotpositionReport[]) list.toArray(aFLRAngebotpositionReport);
		} catch (HibernateException he) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE, new Exception(he));
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return aFLRAngebotpositionReport;
	}

	/**
	 * Eine Zeile eines Angebotdrucks mit den Informationen einer preisbehafteten
	 * Position befuellen (Ident, Handeingabe, AGStueckliste).
	 * 
	 * @param flrangebotpositionI      die preisbehaftete Angebotposition
	 * @param kundeDto                 KundeDto
	 * @param iArtikelpositionsnummerI Referenz auf die letzte vergebene
	 *                                 Artikelpositionsnummer
	 * @param mwstMapI                 Refernz auf die Liste aller definierten
	 *                                 Mwstsaetze
	 * @param bbSeitenumbruchI         dieses Flag toggelt bei einem
	 *                                 benutzerdefinierten Seitenumbruch
	 * @param localeCNrI               das Locale des Drucks
	 * @param theClientDto             der aktuelle Benutzer
	 * @return Object[] die Zeile des Angebotdrucks
	 * @throws EJBExceptionLP
	 */

	public String getArtikelsetType(AngebotpositionDto agposDto) {
		String setartikelType = null;

		if (agposDto.getPositioniIdArtikelset() != null) {

			setartikelType = ArtikelFac.SETARTIKEL_TYP_POSITION;

		} else {

			Session session = null;
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria crit = session.createCriteria(FLRAngebotposition.class);
			crit.add(Restrictions.eq("position_i_id_artikelset", agposDto.getIId()));

			List l = crit.list();
			int iZeilen = l.size();

			if (iZeilen > 0) {
				setartikelType = ArtikelFac.SETARTIKEL_TYP_KOPF;
			}

		}
		return setartikelType;

	}

	private Object[] befuelleZeileAGMitPreisbehafteterPosition(FLRAngebotpositionReport flrangebotpositionI,
			KundeDto kundeDto, KundeDto kundeDtoRechnungsadresse, KundeDto kundeDtoLieferadresse,
			int iArtikelpositionsnummerI, // Referenz
			LinkedHashMap<Integer, MwstsatzReportDto> mwstMapI, // Referenz
			Boolean bbSeitenumbruchI, Locale localeCNrI, TheClientDto theClientDto) throws EJBExceptionLP {
		Object[] dataRow = null;
		try {
			dataRow = new Object[AngebotReportFac.REPORT_ANGEBOT_ANZAHL_SPALTEN];
			// Dto holen damit LPReport.printIdent verwendet werden kann
			AngebotpositionDto angebotpositionDto = getAngebotpositionFac()
					.angebotpositionFindByPrimaryKey(flrangebotpositionI.getI_id(), theClientDto);

			dataRow[REPORT_ANGEBOT_INTERNAL_IID] = flrangebotpositionI.getI_id();

			// UW 22.03.06 Alternative Positionen erhalten keine
			// Positionsnummer, sie
			// werden im Druck mit dem Text "Option" gekennzeichnet
			dataRow[AngebotReportFac.REPORT_ANGEBOT_B_ALTERNATIVE] = flrangebotpositionI.getB_alternative();
			dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITIONOBJEKT] = getSystemReportFac()
					.getPositionForReport(LocaleFac.BELEGART_ANGEBOT, flrangebotpositionI.getI_id(), theClientDto);
			// PJ 15926
			if (flrangebotpositionI.getFlrverleih() != null) {
				dataRow[AngebotReportFac.REPORT_ANGEBOT_VERLEIHTAGE] = flrangebotpositionI.getFlrverleih().getI_tage();
				dataRow[AngebotReportFac.REPORT_ANGEBOT_VERLEIHFAKTOR] = flrangebotpositionI.getFlrverleih()
						.getF_faktor();
			}

			String positionCNr = null;

			if (Helper.short2boolean(flrangebotpositionI.getB_alternative())) {
				positionCNr = AngebotpositionFac.ANGEBOTPOSITION_ALTERNATIVE_TEXT;
			} else {
				positionCNr = getAngebotpositionFac().getPositionNummer(flrangebotpositionI.getI_id()) + "";
			}

			if (Helper.short2boolean(flrangebotpositionI.getB_alternative())) {
				dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITION_NR] = AngebotpositionFac.ANGEBOTPOSITION_ALTERNATIVE_TEXT;
			} else {
				dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITION_NR] = iArtikelpositionsnummerI + "";
			}

			dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITION] = positionCNr;
			String cBezeichnung = null;
			if (flrangebotpositionI.getAngebotpositionart_c_nr()
					.equals(AngebotServiceFac.ANGEBOTPOSITIONART_AGSTUECKLISTE)) {
				AgstklDto agstklDto = getAngebotstklFac().agstklFindByPrimaryKey(flrangebotpositionI.getAgstkl_i_id());

				cBezeichnung = agstklDto.getCBez(); // WH: 15.12.05 Wenn es
				// keine Bezeichnung gibt,
				// dann steht da nichts

				
				dataRow[AngebotReportFac.REPORT_ANGEBOT_AGSTKL_ZEICHNUNGSNUMMER] = agstklDto.getCZeichnungsnummer();
				dataRow[AngebotReportFac.REPORT_ANGEBOT_AGSTKL_INITIALKOSTEN] = agstklDto.getNInitialkosten();
					
				
				if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_AGSTKL_ARBEITSPLAN,
						theClientDto)) {

					dataRow[AngebotReportFac.REPORT_ANGEBOT_AGSTKL_SUBREPORT_MENGENSTAFFEL] = getAngebotstklFac()
							.getSubreportAgstklMengenstaffel(agstklDto.getIId(), theClientDto);
				}

				
				if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_MV_AG_SCHNELLERFASSUNG,
						theClientDto)) {

					dataRow[AngebotReportFac.REPORT_ANGEBOT_AGSTKL_SUBREPORT_MENGENSTAFFEL_SCHNELLERFASSUNG] = getAngebotstklFac()
							.getSubreportAgstklMengenstaffelSchnellerfassung(agstklDto.getIId(), theClientDto);
				}

				
				// PJ20553

				if (agstklDto.getDatenformatCNr() != null && agstklDto.getOMedia() != null) {
					if (agstklDto.getDatenformatCNr().indexOf(MediaFac.DATENFORMAT_MIMETYPEART_IMAGE) != -1) {
						BufferedImage image = Helper.byteArrayToImage(agstklDto.getOMedia());

						dataRow[AngebotReportFac.REPORT_ANGEBOT_IMAGE] = image;

					} else if (agstklDto.getDatenformatCNr().indexOf(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF) != -1) {

						byte[] pdf = agstklDto.getOMedia();

						PDDocument document = null;

						try {

							InputStream myInputStream = new ByteArrayInputStream(pdf);

							document = PDDocument.load(myInputStream);
							PDFRenderer renderer = new PDFRenderer(document);
							int numPages = document.getNumberOfPages();

							if (numPages > 0) {

								BufferedImage image = renderer.renderImageWithDPI(0, 150);
								dataRow[AngebotReportFac.REPORT_ANGEBOT_IMAGE] = image;

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

			} else { // Ident und Handeingabe

				ArtikelDto oArtikelDto = getArtikelFac().artikelFindByPrimaryKey(flrangebotpositionI.getArtikel_i_id(),
						theClientDto);
				dataRow[AngebotReportFac.REPORT_ANGEBOT_REFERENZNUMMER] = oArtikelDto.getCReferenznr();
				dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_INDEX] = oArtikelDto.getCIndex();
				dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_REVISION] = oArtikelDto.getCRevision();
				// PJ15260
				ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreisDesBevorzugtenLieferanten(
						oArtikelDto.getIId(), new BigDecimal(1), kundeDto.getCWaehrung(), theClientDto);
				if (alDto != null && alDto.getZertifikatartIId() != null) {

					ZertifikatartDto zDto = getAnfrageServiceFac()
							.zertifikatartFindByPrimaryKey(alDto.getZertifikatartIId());

					dataRow[AngebotReportFac.REPORT_ANGEBOT_ZERTIFIKATART] = zDto.getCBez();

				}

				BelegPositionDruckIdentDto druckDto = printIdent(angebotpositionDto, LocaleFac.BELEGART_ANGEBOT,
						oArtikelDto, localeCNrI, kundeDto.getPartnerIId(), theClientDto);
				cBezeichnung = druckDto.getSArtikelInfo();
				dataRow[AngebotReportFac.REPORT_ANGEBOT_KURZBEZEICHNUNG] = druckDto.getSKurzbezeichnung();
				dataRow[AngebotReportFac.REPORT_ANGEBOT_BEZEICHNUNG] = druckDto.getSBezeichnung();

				dataRow[AngebotReportFac.REPORT_ANGEBOT_WARENVERKEHRSNUMMER] = druckDto.getSWarenverkehrsnummer();

				dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKELCZBEZ2] = druckDto.getSArtikelZusatzBezeichnung2();
				dataRow[AngebotReportFac.REPORT_ANGEBOT_IMAGE] = druckDto.getOImageKommentar();
				dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKELKOMMENTAR] = druckDto.getSArtikelkommentar();
				dataRow[AngebotReportFac.REPORT_ANGEBOT_ZUSATZBEZEICHNUNG] = druckDto.getSZusatzBezeichnung();

				dataRow[AngebotReportFac.REPORT_ANGEBOT_FREIERTEXT] = flrangebotpositionI.getX_textinhalt();

				if (flrangebotpositionI.getAngebotpositionart_c_nr()
						.equals(AngebotServiceFac.ANGEBOTPOSITIONART_IDENT)) {
					// Ident nur fuer "echte" Artikel
					dataRow[AngebotReportFac.REPORT_ANGEBOT_IDENTNUMMER] = oArtikelDto.getCNr();
					dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_WERBEABGABEPFLICHTIG] = Helper
							.short2Boolean(oArtikelDto.getBWerbeabgabepflichtig());

					// SP5614
					if (oArtikelDto.getNAufschlagBetrag() != null) {
						dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_AUFSCHLAG_BETRAG] = oArtikelDto
								.getNAufschlagBetrag();
					} else {
						dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_AUFSCHLAG_BETRAG] = BigDecimal.ZERO;
					}

					if (oArtikelDto.getFAufschlagProzent() != null) {
						dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_AUFSCHLAG_PROZENT] = oArtikelDto
								.getFAufschlagProzent();
					} else {
						dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_AUFSCHLAG_PROZENT] = 0D;
					}

					// Typ, wenn Setartikel

					if (angebotpositionDto.getPositioniIdArtikelset() != null) {

						dataRow[AngebotReportFac.REPORT_ANGEBOT_SETARTIKEL_TYP] = ArtikelFac.SETARTIKEL_TYP_POSITION;

					} else {

						Session session = null;
						SessionFactory factory = FLRSessionFactory.getFactory();
						session = factory.openSession();
						Criteria crit = session.createCriteria(FLRAngebotposition.class);
						crit.add(Restrictions.eq("position_i_id_artikelset", angebotpositionDto.getIId()));

						int iZeilen = crit.list().size();

						if (iZeilen > 0) {
							dataRow[AngebotReportFac.REPORT_ANGEBOT_SETARTIKEL_TYP] = ArtikelFac.SETARTIKEL_TYP_KOPF;
						}
						session.close();

					}

					// KundeArtikelnr gueltig zu Belegdatum
					KundesokoDto kundeSokoDto_gueltig = this.getKundesokoFac()
							.kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc(
									kundeDtoRechnungsadresse.getIId(), oArtikelDto.getIId(),
									new java.sql.Date(flrangebotpositionI.getFlrangebot().getT_belegdatum().getTime()));
					if (kundeSokoDto_gueltig != null) {
						dataRow[AngebotReportFac.REPORT_ANGEBOT_KUNDEARTIKELNR] = kundeSokoDto_gueltig
								.getCKundeartikelnummer();
						dataRow[AngebotReportFac.REPORT_ANGEBOT_KUNDEARTIKELBEZEICHNUNG] = kundeSokoDto_gueltig
								.getCKundeartikelbez();
					}

					if (oArtikelDto.getVerpackungDto() != null) {
						dataRow[AngebotReportFac.REPORT_ANGEBOT_BAUFORM] = oArtikelDto.getVerpackungDto().getCBauform();
						dataRow[AngebotReportFac.REPORT_ANGEBOT_VERPACKUNGSART] = oArtikelDto.getVerpackungDto()
								.getCVerpackungsart();
					}
					if (oArtikelDto.getMaterialIId() != null) {
						MaterialDto materialDto = getMaterialFac()
								.materialFindByPrimaryKey(oArtikelDto.getMaterialIId(), localeCNrI, theClientDto);
						if (materialDto.getMaterialsprDto() != null
								&& materialDto.getMaterialsprDto().getCBez() != null) {
							dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_MATERIAL] = materialDto.getMaterialsprDto()
									.getCBez();
						} else {
							dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_MATERIAL] = materialDto.getCNr();
						}

						dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_KURS_MATERIALZUSCHLAG] = angebotpositionDto
								.getNMaterialzuschlagKurs();
						dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_DATUM_MATERIALZUSCHLAG] = angebotpositionDto
								.getTMaterialzuschlagDatum();

						MaterialzuschlagDto mzDto = getMaterialFac().getKursMaterialzuschlagDtoVKInZielwaehrung(
								kundeDto.getIId(), oArtikelDto.getIId(),
								flrangebotpositionI.getFlrangebot().getT_belegdatum(),
								flrangebotpositionI.getFlrangebot().getWaehrung_c_nr_angebotswaehrung(), theClientDto);

						if (mzDto != null && mzDto.materialIIdWennMaterialAusKundematerial != null) {
							dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_MATERIALBASIS_AUS_KUNDEMATERIAL] = mzDto.nMaterialbasisWennMaterialAusKundematerial;
							MaterialDto materialDtoAusKundematerial = getMaterialFac().materialFindByPrimaryKey(
									mzDto.materialIIdWennMaterialAusKundematerial, localeCNrI, theClientDto);
							dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_MATERIAL_AUS_KUNDEMATERIAL] = materialDtoAusKundematerial
									.getBezeichnung();

						}

						// PJ19909
						ParametermandantDto parameterMat = getParameterFac().getMandantparameter(
								theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
								ParameterFac.PARAMETER_MATERIALKURS_AUF_BASIS_RECHNUNGSDATUM);
						boolean materialzuschlagAufBasisRechnungsdatum = ((Boolean) parameterMat.getCWertAsObject())
								.booleanValue();

						if (materialzuschlagAufBasisRechnungsdatum == true && mzDto != null) {
							dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_KURS_MATERIALZUSCHLAG] = mzDto
									.getNZuschlag();
							dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_DATUM_MATERIALZUSCHLAG] = mzDto
									.getTGueltigab();
						}

					}

					dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_MATERIALGEWICHT] = oArtikelDto
							.getFMaterialgewicht();

					dataRow[AngebotReportFac.REPORT_ANGEBOT_GEWICHT] = oArtikelDto.getFGewichtkg();

					dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_PRAEFERENZBEGUENSTIGT] = Boolean.FALSE;

					// Ursprungsland
					if (oArtikelDto.getLandIIdUrsprungsland() != null) {
						LandDto landDto = getSystemFac().landFindByPrimaryKey(oArtikelDto.getLandIIdUrsprungsland());
						dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_URSPRUNGSLAND] = landDto.getCName()
								.toUpperCase();
						if (Helper.short2Boolean(landDto.getBPraeferenzbeguenstigt())
								&& kundeDtoLieferadresse.getPartnerDto().getLandplzortDto() != null
								&& Helper.short2Boolean(kundeDtoLieferadresse.getPartnerDto().getLandplzortDto()
										.getLandDto().getBPraeferenzbeguenstigt())) {
							dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_PRAEFERENZBEGUENSTIGT] = Boolean.TRUE;
						}

					}

					if (oArtikelDto.getGeometrieDto() != null) {
						dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_BREITE] = oArtikelDto.getGeometrieDto()
								.getFBreite();
						dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_HOEHE] = oArtikelDto.getGeometrieDto()
								.getFHoehe();
						dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_TIEFE] = oArtikelDto.getGeometrieDto()
								.getFTiefe();
					}

				}
			}
			dataRow[AngebotReportFac.REPORT_ANGEBOT_IDENT] = cBezeichnung;
			dataRow[AngebotReportFac.REPORT_ANGEBOT_IDENT_TEXTEINGABE] = flrangebotpositionI.getX_textinhalt();

			dataRow[AngebotReportFac.REPORT_ANGEBOT_MENGE] = flrangebotpositionI.getN_menge();
			dataRow[AngebotReportFac.REPORT_ANGEBOT_EINHEIT] = flrangebotpositionI.getEinheit_c_nr() == null ? ""
					: getSystemFac().formatEinheit(flrangebotpositionI.getEinheit_c_nr(), localeCNrI, theClientDto);

			AngebotDto angebotDto = getAngebotFac().angebotFindByPrimaryKey(angebotpositionDto.getBelegIId(),
					theClientDto);

			dataRow[AngebotReportFac.REPORT_ANGEBOT_LIEFERZEIT] = getAngebotServiceFac().getLieferzeitInAngeboteinheit(
					angebotpositionDto.getBelegIId(), angebotpositionDto.getIId(), angebotDto.getAngeboteinheitCNr(),
					theClientDto);

			int iNachkommastellenPreis = getUINachkommastellenPreisVK(angebotDto.getMandantCNr());
			angebotpositionDto = (AngebotpositionDto) getBelegVerkaufFac()
					.getBelegpositionVerkaufReport(angebotpositionDto, angebotDto, iNachkommastellenPreis);

			dataRow[AngebotReportFac.REPORT_ANGEBOT_LVPOSITION] = angebotpositionDto.getCLvposition();

			if (rabattDrucken(kundeDto, angebotpositionDto.getArtikelIId(), theClientDto)) {
				dataRow[AngebotReportFac.REPORT_ANGEBOT_EINZELPREIS] = angebotpositionDto
						.getNReportEinzelpreisplusversteckteraufschlag();
				dataRow[AngebotReportFac.REPORT_ANGEBOT_RABATT] = angebotpositionDto.getDReportRabattsatz();
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

				dataRow[AngebotReportFac.REPORT_ANGEBOT_GESAMTPREIS] = angebotpositionDto.getNReportGesamtpreis();
				if (angebotpositionDto.getPositioniIdArtikelset() == null) {
					MwstsatzReportDto m = ((MwstsatzReportDto) mwstMapI.get(angebotpositionDto.getMwstsatzIId()));
					m.setNSummeMwstbetrag(m.getNSummeMwstbetrag().add(angebotpositionDto.getNReportMwstsatzbetrag()));
					m.setNSummePositionsbetrag(
							m.getNSummePositionsbetrag().add(angebotpositionDto.getNReportGesamtpreis()));
				}
			}

			dataRow[AngebotReportFac.REPORT_ANGEBOT_MWSTSATZ] = angebotpositionDto.getDReportMwstsatz();
			dataRow[AngebotReportFac.REPORT_ANGEBOT_MWSTBETRAG] = angebotpositionDto.getNReportMwstsatzbetrag();
			dataRow[AngebotReportFac.REPORT_ANGEBOT_MATERIALZUSCHLAG] = angebotpositionDto.getNMaterialzuschlag();

			dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITIONSART] = flrangebotpositionI.getAngebotpositionart_c_nr();
			dataRow[AngebotReportFac.REPORT_ANGEBOT_SEITENUMBRUCH] = bbSeitenumbruchI;
		} catch (

		RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return dataRow;
	}

	private Object[] befuelleZeileAGMitPauschalkorrektur(BigDecimal bdKorrekturbetrag, Boolean bbSeitenumbruchI,
			KundeDto kundeDto, // Referenz
			LinkedHashMap<Integer, MwstsatzReportDto> mwstMapI, // Referenz
			Locale localeCNrI, Timestamp belegDatum, TheClientDto theClientDto) throws EJBExceptionLP {
		Object[] dataRow = new Object[AngebotReportFac.REPORT_ANGEBOT_ANZAHL_SPALTEN];
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

		/*
		 * MwstsatzDto mwstsatzDto = getMandantFac()
		 * .mwstsatzFindByMwstsatzbezIIdAktuellster(kundeDto.getMwstsatzbezIId(),
		 * theClientDto);
		 */
		MwstsatzDto mwstsatzDto = getMandantFac().mwstsatzZuDatumValidate(kundeDto.getMwstsatzbezIId(), belegDatum,
				theClientDto);
		// Mwstsatz

		BigDecimal ust = bdKorrekturbetrag.multiply(new BigDecimal(mwstsatzDto.getFMwstsatz()).movePointLeft(2));

		MwstsatzReportDto m = ((MwstsatzReportDto) mwstMapI.get(mwstsatzDto.getIId()));
		m.setNSummeMwstbetrag(m.getNSummeMwstbetrag().add(ust));
		m.setNSummePositionsbetrag(m.getNSummePositionsbetrag().add(bdKorrekturbetrag));

		dataRow[AngebotReportFac.REPORT_ANGEBOT_MWSTSATZ] = mwstsatzDto.getFMwstsatz();

		dataRow[AngebotReportFac.REPORT_ANGEBOT_MWSTBETRAG] = ust;

		dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITIONSART] = LocaleFac.POSITIONSART_HANDEINGABE;
		dataRow[AngebotReportFac.REPORT_ANGEBOT_SEITENUMBRUCH] = bbSeitenumbruchI;

		return dataRow;
	}

	/**
	 * Eine Zeile des Angebots mit einer STKL Position befuellen.
	 * 
	 * @param stuecklisteMitStrukturDtoI die Stuecklistenposition
	 * @param cEinrueckungI              die Eintueckung fuer die cNr
	 * @param bbSeitenumbruchI           Toggle fuer Seitenumbruch
	 * @param locDruck                   Locale
	 * @param theClientDto               der aktuelle Benutzer
	 * @return Object[] die zu druckende Zeile
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileAGMitStuecklistenposition(StuecklisteAufgeloest stuecklisteMitStrukturDtoI,
			String cEinrueckungI, Boolean bbSeitenumbruchI, Locale locDruck, AngebotDto angebotDto, Integer mwstsatzIId,
			Integer preislisteIId, TheClientDto theClientDto) throws EJBExceptionLP {
		Object[] dataRow = null;
		try {
			dataRow = new Object[AngebotReportFac.REPORT_ANGEBOT_ANZAHL_SPALTEN];

			StuecklistepositionDto stuecklistepositionDto = stuecklisteMitStrukturDtoI.getStuecklistepositionDto();

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(stuecklistepositionDto.getArtikelIId(),
					theClientDto);

			// unterstkl: 8 Pro Position eine kuenstliche Zeile zum Andrucken
			// erzeugen,
			// als Bezugsmenge gilt immer 1 Einheit der Stueckliste
			dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_STUECKLISTENPOSITION;
			dataRow[AngebotReportFac.REPORT_ANGEBOT_SEITENUMBRUCH] = bbSeitenumbruchI;
			dataRow[AngebotReportFac.REPORT_ANGEBOT_STKLMENGE] = stuecklistepositionDto.getNMenge();
			dataRow[AngebotReportFac.REPORT_ANGEBOT_STKLEINHEIT] = getSystemFac()
					.formatEinheit(stuecklistepositionDto.getEinheitCNr(), locDruck, theClientDto);

			// Einrueckung fuer Unterstuecklisten
			for (int j = 0; j < stuecklisteMitStrukturDtoI.getIEbene(); j++) {
				cEinrueckungI = cEinrueckungI + "    ";
			}

			String artikelCNr = null;

			if (artikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
				artikelCNr = AngebotReportFac.REPORT_VORKALKULATION_ZEICHEN_FUER_HANDEINGABE;
			} else {
				artikelCNr = stuecklistepositionDto.getArtikelDto().getCNr();
				dataRow[AngebotReportFac.REPORT_ANGEBOT_REFERENZNUMMER] = artikelDto.getCReferenznr();
				dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_INDEX] = artikelDto.getCIndex();
				dataRow[AngebotReportFac.REPORT_ANGEBOT_ARTIKEL_REVISION] = artikelDto.getCRevision();
				dataRow[AngebotReportFac.REPORT_ANGEBOT_STKLARTIKELKBEZ] = artikelDto.getKbezAusSpr();

				// KundeArtikelnr gueltig zu Belegdatum
				KundesokoDto kundeSokoDto_gueltig = this.getKundesokoFac()
						.kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc(
								angebotDto.getKundeIIdAngebotsadresse(), artikelDto.getIId(),
								new java.sql.Date(angebotDto.getTBelegdatum().getTime()));
				if (kundeSokoDto_gueltig != null) {
					dataRow[AngebotReportFac.REPORT_ANGEBOT_STKLARTIKEL_KDARTIKELNR] = kundeSokoDto_gueltig
							.getCKundeartikelnummer();
				}

				// PJ18038
				VkpreisfindungDto vkpreisfindungDto = getVkPreisfindungFac().verkaufspreisfindung(artikelDto.getIId(),
						angebotDto.getKundeIIdAngebotsadresse(), stuecklistepositionDto.getNMenge(),
						new Date(angebotDto.getTBelegdatum().getTime()), preislisteIId, mwstsatzIId,
						theClientDto.getSMandantenwaehrung(), theClientDto);

				VerkaufspreisDto kundenVKPreisDto = Helper.getVkpreisBerechnet(vkpreisfindungDto);
				if (kundenVKPreisDto != null) {
					dataRow[AngebotReportFac.REPORT_ANGEBOT_STKLARTIKEL_KDPREIS] = kundenVKPreisDto.nettopreis;
				}

			}

			dataRow[AngebotReportFac.REPORT_ANGEBOT_STKLARTIKELCNR] = cEinrueckungI + artikelCNr;
			dataRow[AngebotReportFac.REPORT_ANGEBOT_STKLARTIKELBEZ] = getArtikelFac().baueArtikelBezeichnungMehrzeilig(
					stuecklistepositionDto.getArtikelIId(), LocaleFac.POSITIONSART_IDENT, null, null, false, locDruck,
					theClientDto);

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return dataRow;
	}

	/**
	 * Eine Zeile des Angebots mit einer AGSTKL Position befuellen.
	 * 
	 * @param agstklpositionDtoI die AGSTKL Position
	 * @param artikelDtoI        der enthaltene Artikel
	 * @param bbSeitenumbruchI   Toggle fuer Seitenumbruch
	 * @param localeDruck        Locale
	 * @param theClientDto       der aktuelle Benutzer
	 * @return Object[] die zu druckenden Daten
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileAGMitAGSTKLPosition(AgstklpositionDto agstklpositionDtoI, ArtikelDto artikelDtoI,
			Boolean bbSeitenumbruchI, Locale localeDruck, AngebotDto angebotDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Object[] dataRow = null;
		try {
			dataRow = new Object[AngebotReportFac.REPORT_ANGEBOT_ANZAHL_SPALTEN];

			// Eine kuenstliche Zeile zum Andrucken der AGStuecklistenposition
			// erzeugen
			dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_STUECKLISTENPOSITION;
			dataRow[AngebotReportFac.REPORT_ANGEBOT_SEITENUMBRUCH] = bbSeitenumbruchI;
			dataRow[AngebotReportFac.REPORT_ANGEBOT_STKLMENGE] = agstklpositionDtoI.getNMenge();
			dataRow[AngebotReportFac.REPORT_ANGEBOT_STKLEINHEIT] = getSystemFac()
					.formatEinheit(agstklpositionDtoI.getEinheitCNr(), localeDruck, theClientDto);

			if (artikelDtoI.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
				dataRow[AngebotReportFac.REPORT_ANGEBOT_STKLARTIKELCNR] = AngebotReportFac.REPORT_VORKALKULATION_ZEICHEN_FUER_HANDEINGABE;
			} else {
				dataRow[AngebotReportFac.REPORT_ANGEBOT_STKLARTIKELCNR] = artikelDtoI.getCNr();
				dataRow[AngebotReportFac.REPORT_ANGEBOT_STKLARTIKELKBEZ] = artikelDtoI.getKbezAusSpr();

				// KundeArtikelnr gueltig zu Belegdatum
				KundesokoDto kundeSokoDto_gueltig = this.getKundesokoFac()
						.kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc(
								angebotDto.getKundeIIdAngebotsadresse(), artikelDtoI.getIId(),
								new java.sql.Date(angebotDto.getTBelegdatum().getTime()));
				if (kundeSokoDto_gueltig != null) {
					dataRow[AngebotReportFac.REPORT_ANGEBOT_STKLARTIKEL_KDARTIKELNR] = kundeSokoDto_gueltig
							.getCKundeartikelnummer();
				}

			}

			dataRow[AngebotReportFac.REPORT_ANGEBOT_STKLARTIKELBEZ] = getArtikelFac().baueArtikelBezeichnungMehrzeilig(
					artikelDtoI.getIId(), LocaleFac.POSITIONSART_IDENT, null, null, false, localeDruck, theClientDto);
			dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITIONOBJEKT] = getSystemReportFac()
					.getPositionForReport(LocaleFac.BELEGART_AGSTUECKLISTE, agstklpositionDtoI.getIId(), theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return dataRow;
	}

	/**
	 * Eine Zeile des Angebots mit einer Leerzeile befuellen.
	 * 
	 * @param bNachEndsummeI   true, wenn die Leerzeile im Anschluss an die Endsumme
	 *                         gedruckt werden soll
	 * @param bbSeitenumbruchI Toggle fuer Seitenumbruch
	 * @return Object[] die zu druckenden Daten
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileAGMitLeerzeile(boolean bNachEndsummeI, Boolean bbSeitenumbruchI)
			throws EJBExceptionLP {
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
	 * @param flrangebotpositionI die Angebotposition
	 * @param bNachEndsummeI      true, wenn die Leerzeile im Anschluss an die
	 *                            Endsumme gedruckt werden soll
	 * @param bbSeitenumbruchI    Toggle fuer Seitenumbruch
	 * @return Object[] die zu druckenden Daten
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	private Object[] befuelleZeileAGMitBetrifft(FLRAngebotpositionReport flrangebotpositionI, boolean bNachEndsummeI,
			Boolean bbSeitenumbruchI, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
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
				.getPositionForReport(LocaleFac.BELEGART_ANGEBOT, flrangebotpositionI.getI_id(), theClientDto);

		return dataRow;
	}

	/**
	 * Eine Zeile des Angebots mit einer Texteingabe befuellen.
	 * 
	 * @param flrangebotpositionI die Angebotposition
	 * @param bNachEndsummeI      true, wenn die Leerzeile im Anschluss an die
	 *                            Endsumme gedruckt werden soll
	 * @param bbSeitenumbruchI    Toggle fuer Seitenumbruch
	 * @param theClientDto        der aktuelle Benutzer
	 * @return Object[] die zu druckenden Daten
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileAGMitTexteingabe(FLRAngebotpositionReport flrangebotpositionI, boolean bNachEndsummeI,
			Boolean bbSeitenumbruchI, TheClientDto theClientDto) throws EJBExceptionLP {
		Object[] dataRow = null;
		try {
			dataRow = new Object[AngebotReportFac.REPORT_ANGEBOT_ANZAHL_SPALTEN];

			// UW 22.03.06 Aufgrund der Criteria ist blob und text nicht in der
			// Position enthalten
			AngebotpositionDto texteingabeDto = getAngebotpositionFac()
					.angebotpositionFindByPrimaryKey(flrangebotpositionI.getI_id(), theClientDto);

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
			dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITIONOBJEKT] = getSystemReportFac()
					.getPositionForReport(LocaleFac.BELEGART_ANGEBOT, flrangebotpositionI.getI_id(), theClientDto);

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return dataRow;
	}

	private Object[] befuelleZeileAGMitPosition(FLRAngebotpositionReport flrangebotpositionI,
			int iArtikelpositionsnummerI, LinkedHashMap<Integer, MwstsatzReportDto> mwstMap, boolean bNachEndsummeI,
			Boolean bbSeitenumbruchI, TheClientDto theClientDto) throws EJBExceptionLP {
		Object[] dataRow = null;
		try {
			dataRow = new Object[AngebotReportFac.REPORT_ANGEBOT_ANZAHL_SPALTEN];
			dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITIONOBJEKT] = getSystemReportFac()
					.getPositionForReport(LocaleFac.BELEGART_ANGEBOT, flrangebotpositionI.getI_id(), theClientDto);
			dataRow[AngebotReportFac.REPORT_ANGEBOT_SEITENUMBRUCH] = bbSeitenumbruchI;
			dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_IDENT;
			BigDecimal nMenge = new BigDecimal(1);
			if (flrangebotpositionI.getN_menge() != null)
				nMenge = flrangebotpositionI.getN_menge();
			dataRow[AngebotReportFac.REPORT_ANGEBOT_MENGE] = nMenge;
			dataRow[AngebotReportFac.REPORT_ANGEBOT_EINHEIT] = SystemFac.EINHEIT_STUECK.trim();

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
				positionCNr = getAngebotpositionFac().getPositionNummer(flrangebotpositionI.getI_id()) + "";
			}

			if (Helper.short2boolean(flrangebotpositionI.getB_alternative())) {
				dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITION_NR] = AngebotpositionFac.ANGEBOTPOSITION_ALTERNATIVE_TEXT;
			} else {
				dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITION_NR] = iArtikelpositionsnummerI + "";
			}

			dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITION] = positionCNr;

			BigDecimal bdNettogesamtpreisplusversteckteraufschlagminusrabatte = getAngebotpositionFac()
					.getGesamtpreisPosition(flrangebotpositionI.getI_id(), theClientDto);
			dataRow[AngebotReportFac.REPORT_ANGEBOT_GESAMTPREIS] = bdNettogesamtpreisplusversteckteraufschlagminusrabatte
					.multiply(nMenge);

			AngebotpositionDto unterpos = getAngebotpositionFac().angebotpositionFindByAngebotIIdISort(
					flrangebotpositionI.getAngebot_i_id(), flrangebotpositionI.getI_sort() + 1);
			MwstsatzDto mwstsatzDto = getMandantFac().mwstsatzFindByPrimaryKey(unterpos.getMwstsatzIId(), theClientDto);
			dataRow[AngebotReportFac.REPORT_ANGEBOT_MWSTSATZ] = mwstsatzDto.getFMwstsatz();
			MwstsatzReportDto mwstsatzReportDto = (MwstsatzReportDto) mwstMap.get(mwstsatzDto.getIId());
			if (mwstsatzDto.getFMwstsatz().doubleValue() > 0) {
				BigDecimal nPositionsbetrag = bdNettogesamtpreisplusversteckteraufschlagminusrabatte
						.multiply(new BigDecimal(1));
				BigDecimal nSummeMwstbetrag = mwstsatzReportDto.getNSummeMwstbetrag().add(nPositionsbetrag
						.multiply(new BigDecimal(mwstsatzDto.getFMwstsatz().doubleValue()).movePointLeft(2)));
				mwstsatzReportDto.setNSummeMwstbetrag(nSummeMwstbetrag);
				mwstsatzReportDto
						.setNSummePositionsbetrag(mwstsatzReportDto.getNSummePositionsbetrag().add(nPositionsbetrag));

			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return dataRow;
	}

	/**
	 * Eine Zeile des Angebots mit einem Textbaustein befuellen.
	 * 
	 * @param flrangebotpositionI die Angebotposition
	 * @param bNachEndsummeI      true, wenn die Leerzeile im Anschluss an die
	 *                            Endsumme gedruckt werden soll
	 * @param bbSeitenumbruchI    Toggle fuer Seitenumbruch
	 * @param theClientDto        String
	 * @return Object[] die zu druckenden Daten
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileAGMitTextbaustein(FLRAngebotpositionReport flrangebotpositionI,
			boolean bNachEndsummeI, Boolean bbSeitenumbruchI, TheClientDto theClientDto) throws EJBExceptionLP {
		Object[] dataRow = null;
		try {
			dataRow = new Object[AngebotReportFac.REPORT_ANGEBOT_ANZAHL_SPALTEN];
			// Dto holen
			MediastandardDto oMediastandardDto = getMediaFac()
					.mediastandardFindByPrimaryKey(flrangebotpositionI.getMediastandard_i_id());
			// zum Drucken vorbereiten
			BelegPositionDruckTextbausteinDto druckDto = printTextbaustein(oMediastandardDto, theClientDto);
			if (bNachEndsummeI) {
				dataRow[AngebotReportFac.REPORT_ANGEBOT_TEXTNACHENDSUMME] = druckDto.getSFreierText();
			} else {
				dataRow[AngebotReportFac.REPORT_ANGEBOT_FREIERTEXT] = druckDto.getSFreierText();
			}
			dataRow[AngebotReportFac.REPORT_ANGEBOT_IMAGE] = druckDto.getOImage();

			dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_TEXTBAUSTEIN;
			dataRow[AngebotReportFac.REPORT_ANGEBOT_SEITENUMBRUCH] = bbSeitenumbruchI;
			dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITIONOBJEKT] = getSystemReportFac()
					.getPositionForReport(LocaleFac.BELEGART_ANGEBOT, flrangebotpositionI.getI_id(), theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return dataRow;
	}

	/**
	 * endumme: 4 Eine Zeile des Angebots mit einer Position nach der Position
	 * Endsumme befuellen.
	 * 
	 * @param flrangebotpositionI die Angebotposition
	 * @param bbSeitenumbruchI    Toggle fuer Seitenumbruch
	 * @param theClientDto        der aktuelle Benutzer
	 * @return Object[] die zu druckenden Daten
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	private Object[] befuelleZeileAGMitPositionNachEndsumme(FLRAngebotpositionReport flrangebotpositionI,
			Boolean bbSeitenumbruchI, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		String positionsartCNr = flrangebotpositionI.getAngebotpositionart_c_nr();
		Object[] dataRow = new Object[AngebotReportFac.REPORT_ANGEBOT_ANZAHL_SPALTEN];

		// UW 06.04.06 Nach der Endsumme Position werden alle folgenden
		// Textpositionen
		// angedruckt, der Text steht dabei an
		// AngebotReportFac.REPORT_ANGEBOT_FREIERTEXT
		if (positionsartCNr.equals(AngebotServiceFac.ANGEBOTPOSITIONART_BETRIFFT)) {
			dataRow = befuelleZeileAGMitBetrifft(flrangebotpositionI, true, // Position
					// im
					// Anschluss
					// an
					// die
					// Endsumme
					// drucken
					bbSeitenumbruchI, theClientDto);
		} else if (positionsartCNr.equals(AngebotServiceFac.ANGEBOTPOSITIONART_TEXTBAUSTEIN)) {
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
		} else if (positionsartCNr.equals(AngebotServiceFac.ANGEBOTPOSITIONART_TEXTEINGABE)) {
			dataRow = befuelleZeileAGMitTexteingabe(flrangebotpositionI, true, // Position
					// im
					// Anschluss
					// an
					// die
					// Endsumme
					// drucken
					bbSeitenumbruchI, theClientDto);
		} else if (positionsartCNr.equals(AngebotServiceFac.ANGEBOTPOSITIONART_SEITENUMBRUCH)) {
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
		} else if (positionsartCNr.equals(AngebotServiceFac.ANGEBOTPOSITIONART_LEERZEILE)) {
			dataRow = befuelleZeileAGMitLeerzeile(true, // die Position wird im
					// Report Detail
					// angedruckt
					bbSeitenumbruchI);
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DRUCKEN_POSITIONSART,
					new Exception(flrangebotpositionI.getAngebotpositionart_c_nr()));
		}

		// jede Position nach der Endsumme wird entsprechend gekennzeichnet
		dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_ENDSUMME;

		return dataRow;
	}

	/**
	 * Update des Data-Arrays um die Zwischensummenbezeichnung in allen Positionen
	 * verf&uuml;gbar zu haben.
	 * 
	 * @param i              ist der Index an dem die Zwischensummenposition
	 *                       definiert ist
	 * @param zwsVonPosition ist die IId der Von-Position
	 * @param zwsBisPosition ist die IId der Bis-Position
	 * @param cBez
	 */
	private void updateZwischensummenData(ArrayList<Object> datalist, Integer zwsVonPosition, String cBez) {
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

	private void updateZwischensummenData(ArrayList<Object> theData, FLRAngebotpositionReport zwsPos) {
		Integer zwsVonPosition = zwsPos.getZwsvonposition_i_id();
		if (zwsVonPosition == null || zwsPos.getZwsbisposition_i_id() == null
				|| ((Object[]) theData.get(theData.size() - 1))[REPORT_ANGEBOT_BISPOSITION] == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_POSITION_ZWISCHENSUMME_UNVOLLSTAENDIG,
					"Position '" + zwsPos.getC_bez() + "' unvollst\u00E4ndig",
					new Object[] { zwsPos.getC_bez(), zwsPos.getI_id() });
		}

		int foundIndex = -1;
		int lastIndex = theData.size();
		for (int i = 0; i < lastIndex; i++) {
			Object[] o = (Object[]) theData.get(i);
			if (zwsVonPosition.equals(o[REPORT_ANGEBOT_INTERNAL_IID])) {
				ZwsAngebotReportHelper helper = new ZwsAngebotReportHelper(o);
				helper.setupNettoSumme(zwsPos.getZwsnettosumme());
				helper.setupZwsText(zwsPos.getC_bez());
				/*
				 * if (null == o[REPORT_ANGEBOT_ZWSTEXT]) { o[REPORT_ANGEBOT_ZWSTEXT] =
				 * zwsPos.getC_bez(); } else { String s = (String) o[REPORT_ANGEBOT_ZWSTEXT] +
				 * "\n" + zwsPos.getC_bez(); o[REPORT_ANGEBOT_ZWSTEXT] = s; }
				 * 
				 * o[REPORT_ANGEBOT_ZWSNETTOSUMME] = zwsPos.getZwsnettosumme();
				 * 
				 * helper.addCBez(zwsPos.getC_bez());
				 * helper.addNettoSumme(zwsPos.getZwsnettosumme());
				 */
				foundIndex = i;
				break;
			}
		}

		if (foundIndex == -1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_POSITION_ZWISCHENSUMME_UNVOLLSTAENDIG,
					"Position '" + zwsPos.getC_bez() + "' unvollst\u00E4ndig",
					new Object[] { zwsPos.getC_bez(), zwsPos.getI_id() });
		}

		Integer zwsBisPosition = zwsPos.getZwsbisposition_i_id();
		for (int i = foundIndex; i < lastIndex; i++) {
			Object[] o = (Object[]) theData.get(i);
			o[REPORT_ANGEBOT_ZWSPOSPREISDRUCKEN] = zwsPos.getB_zwspositionspreiszeigen();
			if (zwsBisPosition.equals(o[REPORT_ANGEBOT_INTERNAL_IID]))
				break;
		}
	}

	private Object[] befuelleZeileAGMitIntelligenterZwischensumme(FLRAngebotpositionReport flrangebotpositionI,
			KundeDto kundeDto, LinkedHashMap<Integer, MwstsatzReportDto> mwstMapI, Boolean bbSeitenumbruchI,
			Locale localeCNrI, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Object[] dataRow = new Object[AngebotReportFac.REPORT_ANGEBOT_ANZAHL_SPALTEN];

		dataRow[REPORT_ANGEBOT_INTERNAL_IID] = flrangebotpositionI.getI_id();
		dataRow[REPORT_ANGEBOT_POSITIONSART] = flrangebotpositionI.getAngebotpositionart_c_nr();
		dataRow[REPORT_ANGEBOT_VONPOSITION] = getAngebotpositionFac()
				.getPositionNummer(flrangebotpositionI.getZwsvonposition_i_id());
		dataRow[REPORT_ANGEBOT_BISPOSITION] = getAngebotpositionFac()
				.getPositionNummer(flrangebotpositionI.getZwsbisposition_i_id());
		dataRow[REPORT_ANGEBOT_ZWSNETTOSUMME] = flrangebotpositionI.getZwsnettosumme();
		dataRow[REPORT_ANGEBOT_ZWSPOSPREISDRUCKEN] = flrangebotpositionI.getB_zwspositionspreiszeigen();
		dataRow[REPORT_ANGEBOT_SEITENUMBRUCH] = bbSeitenumbruchI;

		Integer posnr = getAngebotpositionFac().getPositionNummer(flrangebotpositionI.getI_id());
		dataRow[REPORT_ANGEBOT_POSITION] = posnr == null ? "" : posnr.toString();
		dataRow[REPORT_ANGEBOT_BEZEICHNUNG] = flrangebotpositionI.getC_bez();
		dataRow[REPORT_ANGEBOT_KURZBEZEICHNUNG] = "";
		dataRow[REPORT_ANGEBOT_IDENT] = flrangebotpositionI.getC_bez();
		dataRow[REPORT_ANGEBOT_IDENT_TEXTEINGABE] = "";
		dataRow[REPORT_ANGEBOT_ZUSATZBEZEICHNUNG] = "";
		dataRow[REPORT_ANGEBOT_ARTIKELKOMMENTAR] = "";

		dataRow[AngebotReportFac.REPORT_ANGEBOT_MENGE] = flrangebotpositionI.getN_menge();
		dataRow[AngebotReportFac.REPORT_ANGEBOT_EINHEIT] = flrangebotpositionI.getEinheit_c_nr() == null ? ""
				: getSystemFac().formatEinheit(flrangebotpositionI.getEinheit_c_nr(), localeCNrI, theClientDto);

		AngebotpositionDto angebotpositionDto = getAngebotpositionFac()
				.angebotpositionFindByPrimaryKey(flrangebotpositionI.getI_id(), theClientDto);

		AngebotDto angebotDto = getAngebotFac().angebotFindByPrimaryKey(angebotpositionDto.getBelegIId(), theClientDto);

		int iNachkommastellenPreis = getUINachkommastellenPreisVK(angebotDto.getMandantCNr());
		angebotpositionDto = (AngebotpositionDto) getBelegVerkaufFac().getBelegpositionVerkaufReport(angebotpositionDto,
				angebotDto, iNachkommastellenPreis);
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

			dataRow[AngebotReportFac.REPORT_ANGEBOT_GESAMTPREIS] = angebotpositionDto.getNReportGesamtpreis();
			if (angebotpositionDto.getPositioniIdArtikelset() == null) {
				MwstsatzReportDto m = ((MwstsatzReportDto) mwstMapI.get(angebotpositionDto.getMwstsatzIId()));
				m.setNSummeMwstbetrag(m.getNSummeMwstbetrag().add(angebotpositionDto.getNReportMwstsatzbetrag()));
				m.setNSummePositionsbetrag(
						m.getNSummePositionsbetrag().add(angebotpositionDto.getNReportGesamtpreis()));
			}
		}
		dataRow[AngebotReportFac.REPORT_ANGEBOT_RABATT] = angebotpositionDto.getDReportRabattsatz();
		dataRow[AngebotReportFac.REPORT_ANGEBOT_ZUSATZRABATTSATZ] = angebotpositionDto.getDReportZusatzrabattsatz();
		dataRow[AngebotReportFac.REPORT_ANGEBOT_MWSTSATZ] = angebotpositionDto.getDReportMwstsatz();
		dataRow[AngebotReportFac.REPORT_ANGEBOT_MWSTBETRAG] = angebotpositionDto.getNReportMwstsatzbetrag();

		// PJ 15348
		if (flrangebotpositionI.getMwstsatz_i_id() != null) {
			HvOptional<SteuercodeInfo> steuercode = getMandantFac()
					.getSteuercodeArDefault(new MwstsatzId(flrangebotpositionI.getMwstsatz_i_id()));
			if (steuercode.isPresent())
				dataRow[REPORT_ANGEBOT_MWSTSATZ] = steuercode.get().getCode();
		}

		return dataRow;
	}

	private Object[] befuelleZeileAGMitPositionsartEndsumme(FLRAngebotpositionReport flrangebotpositionI,
			Boolean bbSeitenumbruchI, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		String positionsartCNr = flrangebotpositionI.getAngebotpositionart_c_nr();
		Object[] dataRow = new Object[AngebotReportFac.REPORT_ANGEBOT_ANZAHL_SPALTEN];

		// jede Position nach der Endsumme wird entsprechend gekennzeichnet
		dataRow[AngebotReportFac.REPORT_ANGEBOT_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_ENDSUMME;
		dataRow[AngebotReportFac.REPORT_ANGEBOT_SEITENUMBRUCH] = bbSeitenumbruchI;
		return dataRow;
	}

	public JasperPrintLP printAngebotsstatistik(
			ReportAngebotsstatistikKriterienDto reportAngebotsstatistikKriterienDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		if (reportAngebotsstatistikKriterienDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("reportAngebotsstatistikKriterienDtoI == null"));
		}

		JasperPrintLP oPrint = null;

		try {
			// es gilt das Locale des Benutzers

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);

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

				data[i][AngebotReportFac.REPORT_ANGEBOTSSTATISTIK_CNR] = angebotsstatistikDto.getAngebotCNr();
				data[i][AngebotReportFac.REPORT_ANGEBOTSSTATISTIK_KUNDE] = angebotsstatistikDto.getKundenname();
				data[i][AngebotReportFac.REPORT_ANGEBOTSSTATISTIK_BELEGDATUM] = angebotsstatistikDto.getBelegdatumCNr();
				data[i][AngebotReportFac.REPORT_ANGEBOTSSTATISTIK_ANGEBOTENEMENGE] = angebotsstatistikDto
						.getNAngebotenemenge();
				data[i][AngebotReportFac.REPORT_ANGEBOTSSTATISTIK_ANGEBOTENERPREIS] = angebotsstatistikDto
						.getNAngebotenerpreis();
				data[i][AngebotReportFac.REPORT_ANGEBOTSSTATISTIK_MATERIALZUSCHLAG] = angebotsstatistikDto
						.getNMaterialzuschlag();
			}

			// Erstellung des Report
			HashMap<String, Object> parameter = new HashMap<String, Object>();

			parameter.put("Mandant", mandantDto.getPartnerDto().getCName1nachnamefirmazeile1());
			parameter.put("Artikel", formatAngebotsstatistikKriterien(reportAngebotsstatistikKriterienDtoI,
					theClientDto.getLocUi(), theClientDto));

			if (reportAngebotsstatistikKriterienDtoI.getArtikelIId() != null) {
				parameter.put("P_ARTIKELREFERENZNUMMER", getArtikelFac()
						.artikelFindByPrimaryKey(reportAngebotsstatistikKriterienDtoI.getArtikelIId(), theClientDto)
						.getCReferenznr());
			}

			initJRDS(parameter, AngebotReportFac.REPORT_MODUL, AngebotReportFac.REPORT_ANGEBOTSSTATISTIK,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

			oPrint = getReportPrint();

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} catch (Exception t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, t);
		}

		return oPrint;
	}

	private ReportAngebotsstatistikDto[] getReportAngebotsstatistik(
			ReportAngebotsstatistikKriterienDto reportAngebotsstatistikKriterienDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		ReportAngebotsstatistikDto[] aResult = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		try {
			session = factory.openSession();

			// Hiberante Criteria fuer alle Tabellen ausgehend von meiner
			// Haupttabelle anlegen,
			// nach denen ich filtern und sortieren kann
			Criteria crit = session.createCriteria(FLRAngebotpositionReport.class);

			// flranfragepositionlieferdatenReport > flranfragepositionReport >
			// flrartikel
			Criteria critArtikel = crit.createCriteria("flrartikel");

			// flranfragepositionReport > flranfrage
			Criteria critAngebot = crit.createCriteria("flrangebot");

			// Einschraenkung der Anfragen auf den aktuellen Mandanten
			critAngebot.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));

			critArtikel.add(Restrictions.eq("i_id", reportAngebotsstatistikKriterienDtoI.getArtikelIId()));

			// Einschraenkung nach Belegdatum von - bis
			if (reportAngebotsstatistikKriterienDtoI.getDVon() != null) {
				critAngebot.add(Restrictions.ge(AngebotFac.FLR_ANGEBOT_T_BELEGDATUM,
						reportAngebotsstatistikKriterienDtoI.getDVon()));
			}

			if (reportAngebotsstatistikKriterienDtoI.getDBis() != null) {
				critAngebot.add(Restrictions.le(AngebotFac.FLR_ANGEBOT_T_BELEGDATUM,
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
				FLRAngebotpositionReport flrangebotpositionlieferdaten = (FLRAngebotpositionReport) it.next();
				FLRAngebot flrangebot = flrangebotpositionlieferdaten.getFlrangebot();

				reportDto = new ReportAngebotsstatistikDto();
				reportDto.setAngebotCNr(flrangebot.getC_nr());
				reportDto.setKundenname(flrangebot.getFlrkunde().getFlrpartner().getC_name1nachnamefirmazeile1());

				Date datBelegdatum = new Date(flrangebot.getT_belegdatum().getTime());
				reportDto.setBelegdatumCNr(Helper.formatDatum(datBelegdatum, theClientDto.getLocUi()));

				reportDto.setIIndex(new Integer(iIndex));
				reportDto.setNAngebotenemenge(flrangebotpositionlieferdaten.getN_menge());

				// der Preis wird in Mandantenwaehrung angezeigt, es gilt der
				// hinterlegte Wechselkurs

				BigDecimal bdPreisinmandantenwaehrung = flrangebotpositionlieferdaten
						.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte();

				reportDto.setNAngebotenerpreis(bdPreisinmandantenwaehrung);
				reportDto.setNMaterialzuschlag(flrangebotpositionlieferdaten.getN_materialzuschlag());

				reportDto.setNAngebotenemenge(flrangebotpositionlieferdaten.getN_menge());

				aResult[iIndex] = reportDto;
				iIndex++;
			}
		} finally {
			closeSession(session);
		}
		return aResult;
	}

	private String formatAngebotsstatistikKriterien(
			ReportAngebotsstatistikKriterienDto reportAngebotsstatistikKriterienDtoI, Locale localeI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		StringBuffer buff = new StringBuffer("");

		try {
			buff.append(getArtikelFac().baueArtikelBezeichnungMehrzeiligOhneExc(
					reportAngebotsstatistikKriterienDtoI.getArtikelIId(), LocaleFac.POSITIONSART_IDENT, null, null,
					true, null, theClientDto));

			// Belegdatum
			if (reportAngebotsstatistikKriterienDtoI.getDVon() != null
					|| reportAngebotsstatistikKriterienDtoI.getDBis() != null) {
				buff.append("\n").append(
						getTextRespectUISpr("bes.belegdatum", theClientDto.getMandant(), theClientDto.getLocUi()));
			}

			if (reportAngebotsstatistikKriterienDtoI.getDVon() != null) {
				buff.append(" ")
						.append(getTextRespectUISpr("lp.von", theClientDto.getMandant(), theClientDto.getLocUi()));
				buff.append(" ").append(Helper.formatDatum(reportAngebotsstatistikKriterienDtoI.getDVon(), localeI));
			}

			if (reportAngebotsstatistikKriterienDtoI.getDBis() != null) {
				buff.append(" ")
						.append(getTextRespectUISpr("lp.bis", theClientDto.getMandant(), theClientDto.getLocUi()));
				buff.append(" ").append(Helper.formatDatum(reportAngebotsstatistikKriterienDtoI.getDBis(), localeI));
			}
		} catch (RemoteException re) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, re);
		}

		String cBuffer = buff.toString().trim();

		return cBuffer;
	}

}
