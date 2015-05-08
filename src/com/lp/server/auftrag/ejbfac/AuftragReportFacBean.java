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
package com.lp.server.auftrag.ejbfac;

import java.awt.Image;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
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
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.angebot.service.AngebotReportFac;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.fastlanereader.generated.FLRLagerbewegung;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.artikel.service.ArtklaDto;
import com.lp.server.artikel.service.HandlagerbewegungDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.artikel.service.LagerabgangursprungDto;
import com.lp.server.artikel.service.LagerbewegungDto;
import com.lp.server.artikel.service.MaterialDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VerleihDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftrag;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragOD;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragposition;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragpositionOD;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragpositionOP;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragpositionReport;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragseriennrn;
import com.lp.server.auftrag.fastlanereader.generated.FLRZahlungsplan;
import com.lp.server.auftrag.fastlanereader.generated.FLRZahlungsplanmeilenstein;
import com.lp.server.auftrag.fastlanereader.generated.FLRZeitplan;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragNachkalkulationDto;
import com.lp.server.auftrag.service.AuftragPacklistePositionDto;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragauftragdokumentDto;
import com.lp.server.auftrag.service.AuftragdokumentDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.AuftragpositionFac;
import com.lp.server.auftrag.service.AuftragseriennrnDto;
import com.lp.server.auftrag.service.AuftragtextDto;
import com.lp.server.auftrag.service.AuftragzeitenDto;
import com.lp.server.auftrag.service.ReportAuftragOffeneDetailsDto;
import com.lp.server.auftrag.service.ReportAuftragOffeneDto;
import com.lp.server.auftrag.service.ReportAuftragVerfuegbarkeitDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungAuftragszuordnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosReport;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosablieferungDto;
import com.lp.server.fertigung.service.LosistmaterialDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.fertigung.service.ReportLosnachkalkulationDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferschein;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferscheinposition;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.PartnerklasseDto;
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.personal.service.PersonalgehaltDto;
import com.lp.server.personal.service.ReiseDto;
import com.lp.server.personal.service.ReiseKomplettDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungDtoAssembler;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklisteeigenschaft;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteMitStrukturDto;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.fastlanereader.generated.FLRPanelbeschreibung;
import com.lp.server.system.fastlanereader.generated.FLRPaneldaten;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.BelegartDto;
import com.lp.server.system.service.KostenstelleDto;
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
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.system.service.ZusatzfunktionberechtigungDto;
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
import com.lp.util.AddableHashMap;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;

@Stateless
@Interceptors(TimingInterceptor.class)
public class AuftragReportFacBean extends LPReport implements AuftragReportFac,
		JRDataSource {

	public static final int REPORT_AUFTRAGSTATISTIK_SORTIERUNG_AUFTRAG = 0;
	private String cAktuellerReport = null;
	private Object[][] data = null;

	private String TOKEN_GESAMTLAGERSTAND = "GESAMTLAGERSTAND";

	@PersistenceContext
	private EntityManager em;

	public boolean next() throws JRException {
		index++;
		return (index < data.length);
	}

	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;
		String fieldName = field.getName();
		if (cAktuellerReport.equals(AuftragReportFac.REPORT_AUFTRAG_OFFENE)) {
			if ("Auftragcnr".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_AUFTRAGCNR];
			} else if ("F_AUFTRAGSART".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_AUFTRAGSART];
			} else if ("F_KUNDE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_AUFTRAGKUNDE];
			} else if ("F_VERTRETER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_AUFTRAGVERTRETER];
			} else if ("Auftragliefertermin".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_AUFTRAGLIEFERTERMIN];
			} else if ("Auftragfinaltermin".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_AUFTRAGFINALTERMIN];
			} else if ("Auftragzahlungsziel".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_AUFTRAGZAHLUNGSZIEL];
			} else if ("Projektbezeichnung".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_AUFTRAGPROJEKTBEZEICHNUNG];
			} else if ("F_BESTELLNUMMER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_AUFTRAGBESTELLNUMMER];
			} else if ("Artikelcnr".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTIKELCNR];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTIKELBEZEICHNUNG];
			} else if ("Artikelmenge".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTIKELMENGE];
			} else if ("Artikeleinheit".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTIKELEINHEIT];
			} else if ("Artikelnettogesamtpreisplusversteckteraufschlagminusrabatte"
					.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTIKELNETTOGESAMTPREISPLUSVERSTECKTERAUFSCHLAGMINUSRABATTE];
			} else if ("Artikelgestehungspreis".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTIKELGESTEHUNGSPREIS];
			} else if ("Artikelgeliefertemenge".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTIKELGELIEFERTEMENGE];
			} else if ("Artikeloffenemenge".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTIKELOFFENEMENGE];
			} else if ("Artikeloffenerwert".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTIKELOFFENERWERT];
			} else if ("Artikeloffenerdb".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTIKELOFFENERDB];
			} else if ("F_KOSTENSTELLECNR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_KOSTENSTELLECNR];
			} else if ("F_INTERNERKOMMENTAR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_INTERNERKOMMENTAR];
			} else if ("F_EXTERNERKOMMENTAR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_EXTERNERKOMMENTAR];
			} else if ("F_POSITIONSTERMIN".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONSTERMIN];
			} else if ("F_ARTIKELLAGERSTAND".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTIKELLAGERSTAND];
			} else if (F_AUFTRAGEIGENSCHAFT_FA.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_AUFTRAGEIGENSCHAFT_FA];
			} else if (F_AUFTRAGEIGENSCHAFT_CLUSTER.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_AUFTRAGEIGENSCHAFT_CLUSTER];
			} else if (F_AUFTRAGEIGENSCHAFT_EQNR.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_AUFTRAGEIGENSCHAFT_EQNR];
			} else if ("F_EINKAUFSPREIS".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_EINKAUFSPREIS];
			} else if ("F_ZEITPUNKT_VERRECHENBAR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_ZEIT_VERRECHENBAR];
			} else if ("F_PERSON_VERRECHENBAR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_PERSON_VERRECHENBAR];
			}

		} else if (cAktuellerReport
				.equals(AuftragReportFac.REPORT_AUFTRAG_ERFUELLUNGSGRAD)) {
			if ("Auftragswert".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSGRAD_AUFTRAGSWERT];
			} else if ("Rechnungswert".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSGRAD_RECHNUNGSWERT];
			} else if ("Sollstunden".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSGRAD_GEPLANTESTUNDEN];
			} else if ("Iststunden".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSGRAD_GELEISTETESTUNDEN];
			} else if ("Wertiststunden".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSGRAD_WERTGELEISTETESTUNDEN];
			} else if ("Projekt".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSGRAD_PROJEKT];
			} else if ("Saldo".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSGRAD_SALDO];
			} else if ("Vorleistung".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSGRAD_VORLEISTUNG];
			} else if ("Vertreter".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSGRAD_VERTRETER];
			} else if ("Zahlungsgrad".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSGRAD_ZAHLUNGSGRAD];
			} else if ("Erfuellungsgrad".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSGRAD_THEORETISCHERFUELLT];
			} else if ("Erfuellungsgradmanuell".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSGRAD_TATSAECHLICHERFUELLT];
			} else if ("Auftrag".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSGRAD_AUFTRAGSNUMMER];
			} else if ("Beleg".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSGRAD_ZUSATZBELEG];
			} else if ("Subreport".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSGRAD_SUBREPORT_PERSONALZEITEN];
			} else if ("Rechnungsadresse".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSGRAD_RECHNUNGSADRESSE];
			} else if ("Lieferadresse".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSGRAD_LIEFERADRESSE];
			} else if ("Auftragkundename1".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSGRAD_AUFTRAGSADRESSE_NAME1];
			} else if ("Auftragkundename2".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSGRAD_AUFTRAGSADRESSE_NAME2];
			} else if ("Auftragkundeort".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSGRAD_AUFTRAGSADRESSE_ORT];
			} else if ("Auftragkundeplz".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSGRAD_AUFTRAGSADRESSE_PLZ];
			} else if ("Auftragkundelkz".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSGRAD_AUFTRAGSADRESSE_LKZ];
			} else if ("Auftragkundestrasse".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSGRAD_AUFTRAGSADRESSE_STRASSE];
			}
		} else if (cAktuellerReport
				.equals(AuftragReportFac.REPORT_AUFTRAG_ERFUELLUNGSJOURNAL)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_ARTIKELNUMMER];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_ARTIKELBEZEICHNUNG];
			} else if ("LagerstandZumBisZeitpunkt".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_LAGERSTAND_ZUM_BIS_ZEITPUNKT];
			} else if ("VerfuegbarZumBisZeitpunkt".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_VERFUEGBAR_ZUM_BIS_ZEITPUNKT];
			} else if ("Einstandspreis".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_EINSTANDSPREIS];
			} else if ("Geliefert".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_GELIEFERT];
			} else if ("Abgeliefert".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_ABGELIEFERT];
			} else if ("VKPreis".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_VKPREIS];
			} else if ("RahmenGeplant".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_RAHMEN_GEPLANT];
			} else if ("Gestpreis".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_GESTPREIS];
			} else if ("Eingekauft".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_EINGEKAUFT];
			}
		} else if (cAktuellerReport
				.equals(AuftragReportFac.REPORT_TAETIGKEITSSTATISTIK)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_TAETIGKEITSSTATISTIK_ARTIKELNUMMER];
			} else if ("Auftragsnummer".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_TAETIGKEITSSTATISTIK_AUFTRAGSNUMMER];
			} else if ("AZArtikel".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_TAETIGKEITSSTATISTIK_AZ_ARTIKEL];
			} else if ("AZArtikelBez".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_TAETIGKEITSSTATISTIK_AZ_ARTIKELBEZ];
			} else if ("AZIst".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_TAETIGKEITSSTATISTIK_AZ_IST];
			} else if ("AZKosten".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_TAETIGKEITSSTATISTIK_AZ_KOSTEN];
			} else if ("AZSoll".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_TAETIGKEITSSTATISTIK_AZ_SOLL];
			} else if ("Kunde".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_TAETIGKEITSSTATISTIK_KUNDE];
			} else if ("Projekt".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_TAETIGKEITSSTATISTIK_PROJEKT];
			} else if ("WeitereArtikelnummern".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_TAETIGKEITSSTATISTIK_WEITERE_ARTIKELNUMMERN];
			}
		} else if (cAktuellerReport
				.equals(AuftragReportFac.REPORT_AUFTRAGSUEBERSICHT)) {
			if ("Arbeitszeit".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGSUEBERSICHT_ARBEITSZEIT];
			} else if ("Artikelnummer".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGSUEBERSICHT_ARTIKELNUMMER];
			} else if ("AZBis".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGSUEBERSICHT_AZ_BIS];
			} else if ("AZDauer".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGSUEBERSICHT_AZ_DAUER];
			} else if ("AZPerson".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGSUEBERSICHT_AZ_PERSON];
			} else if ("AZVon".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGSUEBERSICHT_AZ_VON];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGSUEBERSICHT_BEZEICHNUNG];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGSUEBERSICHT_EINHEIT];
			} else if ("Istmenge".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGSUEBERSICHT_ISTMENGE];
			} else if ("Lieferschein".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGSUEBERSICHT_LIEFERSCHEIN];
			} else if ("Nettopreis".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGSUEBERSICHT_NETTOPREIS];
			} else if ("NichtZugeordnet".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGSUEBERSICHT_NICHT_ZUGEORDNET];
			} else if ("Rechnung".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGSUEBERSICHT_RECHNUNG];
			} else if ("Sollmenge".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGSUEBERSICHT_SOLLMENGE];
			} else if ("Artikelzusatzbezeichnung".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGSUEBERSICHT_ZUSATZBEZEICHNUNG];
			} else if ("AZPersonKurzzeichen".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGSUEBERSICHT_AZ_PERSON_KURZZEICHEN];
			} else if ("Liefertermin".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGSUEBERSICHT_LIEFERTERMIN];
			} else if ("Belegdatum".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGSUEBERSICHT_BELEGDATM];
			}

		} else if (cAktuellerReport
				.equals(AuftragReportFac.REPORT_RAHMENUEBERSICHT)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_RAHMENUEBERSICHT_ARTIKELNUMMER];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_RAHMENUEBERSICHT_ARTIKELBEZEICHNUNG];
			} else if ("Auftragart".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_RAHMENUEBERSICHT_AUFRAGART];
			} else if ("Auftragsnummer".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_RAHMENUEBERSICHT_AUFRAGNR];
			} else if ("Auftragswert".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_RAHMENUEBERSICHT_AUFTRAGWERT];
			} else if ("Belegdatum".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_RAHMENUEBERSICHT_BELEGDATUM];
			} else if ("Kundenbestellnummer".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_RAHMENUEBERSICHT_KUNDENBESTELLNUMMER];
			} else if ("Lieferschein".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_RAHMENUEBERSICHT_LIEFERSCHEIN];
			} else if ("Liefertermin".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_RAHMENUEBERSICHT_LIEFERTERMIN];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_RAHMENUEBERSICHT_MENGE];
			} else if ("Preis".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_RAHMENUEBERSICHT_PREIS];
			} else if ("Rechnung".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_RAHMENUEBERSICHT_RECHNUNG];
			} else if ("Storniert".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_RAHMENUEBERSICHT_STORNIERT];
			}
		} else if (cAktuellerReport
				.equals(AuftragReportFac.REPORT_AUFTRAGSTATISTIK)) {
			if ("Belegart".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_BELEGART];
			} else if ("Belegnummer".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_BELEGNUMMER];
			} else if ("Verkaufswertarbeitsoll".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_VERKAUFSWERTARBEITSOLL];
			} else if ("Verkaufswertmaterialsoll".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_VERKAUFSWERTMATERIALSOLL];
			} else if ("Verkaufswertarbeitist".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_VERKAUFSWERTARBEITIST];
			} else if ("Verkaufswertmaterialist".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_VERKAUFSWERTMATERIALIST];
			} else if ("Gestehungswertarbeitsoll".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_GESTEHUNGSWERTARBEITSOLL];
			} else if ("Gestehungswertmaterialsoll".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_GESTEHUNGSWERTMATERIALSOLL];
			} else if ("Gestehungswertarbeitist".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_GESTEHUNGSWERTARBEITIST];
			} else if ("Gestehungswertmaterialist".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_GESTEHUNGSWERTMATERIALIST];
			} else if ("Arbeitszeitsoll".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_ARBEITSZEITSOLL];
			} else if ("Arbeitszeitist".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_ARBEITSZEITIST];
			} else if ("Artikelnummerarbeitszeitist".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_ARTIKELNUMMERARBEITSZEIT];
			} else if ("Artikelbezeichnungarbeitszeitist".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_ARTIKELBEZEICHNUNGARBEITSZEIT];
			} else if ("Maschinenzeitsoll".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_MASCHINENZEITSOLL];
			} else if ("Maschinenzeitist".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_MASCHINENZEITIST];
			} else if ("Eingangsrechnungtext".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_EINGANGSRECHNUNGTEXT];
			} else if ("Gruppierung".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_GRUPPIERUNG];
			} else if ("Gruppierungbezeichnung".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_GRUPPIERUNGBEZEICHNUNG];
			} else if ("Auftragsnummer".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_AUFTRAGSNUMMER];
			} else if ("FuehrenderArtikel".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_FUEHRENDER_ARTIKEL];
			} else if ("Auftragkundename1".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_BELEGKUNDE_NAME1];
			} else if ("Auftragkundename2".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_BELEGKUNDE_NAME2];
			} else if ("Auftragkundename3".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_BELEGKUNDE_NAME3];
			} else if ("Auftragkundeort".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_BELEGKUNDE_ORT];
			} else if ("Auftragkundeplz".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_BELEGKUNDE_PLZ];
			} else if ("Auftragkundelkz".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_BELEGKUNDE_LKZ];
			} else if ("Auftragkundestrasse".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_BELEGKUNDE_STRASSE];
			} else if ("Rechnungsadressename1".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_BELEGRECHNUNGSADRESSE_NAME1];
			} else if ("Rechnungsadressename2".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_BELEGRECHNUNGSADRESSE_NAME2];
			} else if ("Rechnungsadressename3".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_BELEGRECHNUNGSADRESSE_NAME3];
			} else if ("Rechnungsadresseort".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_BELEGRECHNUNGSADRESSE_ORT];
			} else if ("Rechnungsadresseplz".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_BELEGRECHNUNGSADRESSE_PLZ];
			} else if ("Rechnungsadresselkz".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_BELEGRECHNUNGSADRESSE_LKZ];
			} else if ("Rechnungsadressestrasse".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_BELEGRECHNUNGSADRESSE_STRASSE];
			} else if ("Projekt".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_PROJEKT];
			} else if ("Bestellnummer".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_BESTELLNUMMER];
			} else if ("Losstuecklisteartikelnummer".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_LOS_STUECKLISTE_ARTIKELNUMMER];
			} else if ("Losstuecklisteartikelbezeichnung".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_LOS_STUECKLISTE_BEZEICHNUNG];
			} else if ("Losstuecklistezusatzbezeichnung".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_LOS_STUECKLISTE_ZUSATZBEZEICHNUNG];
			} else if ("Losprojekt".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_LOS_PROJEKT];
			} else if ("Erledigungsdatum".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_LOS_ERLEDIGUNGSDATUM];
			} else if ("Bewertung".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_LOS_BEWERTUNG];
			} else if ("Loskommentar".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_LOS_KOMMENTAR];
			} else if ("ReBestellnummer".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_RE_BESTELLNUMMER];
			} else if ("ErLieferant".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_ER_LIEFERANT];
			} else if ("RechnungsnummerLSVerrechnet".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_RECHNUNGSNUMMER_LS_VERRECHNET];
			} else if ("Rechnungsart".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_RECHNUNGSART];
			} else if ("ReisePartner".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_REISE_PARTNER];
			} else if ("ReiseKommentar".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_REISE_KOMMENTAR];
			} else if ("ReiseMitarbeiter".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_REISE_MITARBEITER];
			} else if ("ReiseVon".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_REISE_VON];
			} else if ("ReiseBis".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_REISE_BIS];
			} else if ("ErEingangsrechnungsart".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_ER_EINGANGSRECHNUNGSART];
			} else if ("ErAuftragszuordnungKeineAuftragswertung"
					.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_ER_AUFTRAGSZUORDNUNG_KEINE_AUFTRAGSWERTUNG];
			} else if ("LosnummerLiQuelle".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_LOSNUMMER_LI_QUELLE];
			} else if ("Zahlbetrag".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_ZAHLBETRAG];
			} else if ("Belegstatus".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_BELEGSTATUS];
			} else if ("ErSchlussrechnungNr".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_STATISTIK_ER_SCHLUSSRECHNUNG_NR];
				// } else if ("DiaetenAusScript".equals(fieldName)) {
				// value =
				// data[index][AuftragReportFac.REPORT_STATISTIK_DIAETENAUSSCRIPT];
			}

		}

		else if (cAktuellerReport
				.equals(AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS)) {
			if ("Auftragcnr".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGCNR];
			} else if ("F_AUFTRAGART".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGARTCNR];
			} else if ("F_KUNDE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGKUNDE];
			} else if ("F_KUNDE2".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGKUNDE2];
			} else if ("Auftragliefertermin".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGLIEFERTERMIN];
			} else if ("Auftragfinaltermin".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGFINALTERMIN];
			} else if ("Auftragzahlungsziel".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGZAHLUNGSZIEL];
			} else if ("Projektbezeichnung".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGPROJEKTBEZEICHNUNG];
			} else if ("F_BESTELLNUMMER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGBESTELLNUMMER];
			} else if ("Artikelcnr".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELCNR];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELBEZEICHNUNG];
			} else if ("Artikelmenge".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELMENGE];
			} else if ("IstStuecklistenposition".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_IST_STUECKLISTENPOSITION];
			} else if ("Artikeleinheit".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELEINHEIT];
			} else if ("Artikelnettogesamtpreisplusversteckteraufschlagminusrabatte"
					.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELNETTOGESAMTPREISPLUSVERSTECKTERAUFSCHLAGMINUSRABATTE];
			} else if ("Artikelgestehungspreis".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELGESTEHUNGSPREIS];
			} else if ("Artikelgeliefertemenge".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELGELIEFERTEMENGE];
			} else if ("Artikeloffenemenge".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELOFFENEMENGE];
			} else if ("Artikeloffenerwert".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELOFFENERWERT];
			} else if ("Artikeloffenerdb".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELOFFENERDB];
			} else if ("F_KOSTENSTELLECNR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_KOSTENSTELLECNR];
			} else if ("F_INTERNERKOMMENTAR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_INTERNERKOMMENTAR];
			} else if ("F_EXTERNERKOMMENTAR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_EXTERNERKOMMENTAR];
			} else if ("F_POSITIONSTERMIN".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_POSITIONSTERMIN];
			} else if ("F_POSITIONSTERMIN_OHNE_LIEFERDAUER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_POSITIONSTERMIN_OHNE_LIEFERDAUER];
			} else if ("F_ARTIKELLAGERSTAND".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELLAGERSTAND];
			} else if ("F_KUNDEAUFTRAGADRESSE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_KUNDEAUFTRAGADRESSE];
			} else if ("F_KUNDELIEFERADRESSE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_KUNDELIEFERADRESSE];
			} else if ("F_KUNDELIEFERADRESSE_LIEFERDAUER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_KUNDELIEFERADRESSE_LIEFERDAUER];
			} else if ("F_LOSNUMMER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_LOSNUMMER];
			} else if (F_AUFTRAGEIGENSCHAFT_FA.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGEIGENSCHAFT_FA];
			} else if (F_AUFTRAGEIGENSCHAFT_CLUSTER.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGEIGENSCHAFT_CLUSTER];
			} else if (F_AUFTRAGEIGENSCHAFT_EQNR.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGEIGENSCHAFT_EQNR];
			} else if ("F_FERTIGUNGSGRUPPE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_LOS_FERTIGUNGSGRUPPE];
			} else if ("F_STKL_FERTIGUNGSGRUPPE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_STKL_FERTIGUNGSGRUPPE];
			} else if (F_ARTIKELMENGE_OFFEN.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELBESTELLTMENGE];
			} else if ("Spediteur".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_SPEDITEUR];
			} else if ("F_EINKAUFSPREIS".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_EINKAUFSPREIS];
			} else if ("F_LIEFERTERMINUNVERBINDLICH".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_B_LIEFERTERMIN];
			} else if ("F_RAHMENAUFTRAG".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_RAHMENAUFTRAG];
			} else if ("F_LAGERSTAND_KUNDENLAGER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTAND_KUNDENLAGER];
			} else if ("F_LAGERSTAND_NORMALLAGER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTAND_NORMALLAGER];
			} else if ("F_LAGERSTAND_PERSOENLICH".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTAND_PERSOENLICH];
			} else if ("F_LAGERSTAND_SCHROTT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTAND_SCHROTT];
			} else if ("F_LAGERSTAND_SPERRLAGER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTAND_SPERRLAGER];
			} else if ("F_LAGERSTAND_ZOLLLAGER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTAND_ZOLLLAGER];
			} else if ("F_LAGERSTAND_LIEFERANT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTAND_LIEFERANT];
			} else if ("F_LAGERSTAND_HALBFERTIG".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTAND_HALBFERTIG];
			} else if ("F_LAGERSTANDS_FEHLMENGE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTANDS_FEHLMENGE];
			} else if ("F_LAGERSTANDS_FEHLMENGE_KUMULIERT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTANDS_FEHLMENGE_KUMULIERT];
			} else if ("F_IST_STUECKLISTENPOSITION".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_IST_STUECKLISTENPOSITION];
			} else if ("F_ARTIKEL_RAHMENRESERVIERUNGEN".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKEL_RAHMENRESERVIERUNGEN];
			} else if ("F_ARTIKEL_FERTIGUNGSSATZGROESSE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKEL_FERTIGUNGSSATZGROESSE];
			} else if ("F_ARTIKEL_LAGERSOLL".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKEL_LAGERSOLL];
			} else if ("F_ARTIKEL_LAGERMINDEST".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKEL_LAGERMINDEST];
			} else if ("F_ZEITPUNKT_VERRECHENBAR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ZEIT_VERRECHENBAR];
			} else if ("F_PERSON_VERRECHENBAR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_PERSON_VERRECHENBAR];
			}

		} else if (cAktuellerReport
				.equals(AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN)) {
			if ("Auftragcnr".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGCNR];
			} else if ("F_AUFTRAGART".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGARTCNR];
			} else if ("F_KUNDE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGKUNDE];
			} else if ("F_KUNDE2".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGKUNDE2];
			} else if ("Auftragliefertermin".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGLIEFERTERMIN];
			} else if ("Auftragfinaltermin".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGFINALTERMIN];
			} else if ("Auftragzahlungsziel".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGZAHLUNGSZIEL];
			} else if ("Projektbezeichnung".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGPROJEKTBEZEICHNUNG];
			} else if ("F_BESTELLNUMMER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGBESTELLNUMMER];
			} else if ("Artikelcnr".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELCNR];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELBEZEICHNUNG];
			} else if ("Artikelmenge".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELMENGE];
			} else if ("Artikeleinheit".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELEINHEIT];
			} else if ("F_ARTIKELSNR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELSNR];
			} else if ("Artikelgestehungspreis".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELGESTEHUNGSPREIS];
			} else if ("Artikelgeliefertemenge".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELGELIEFERTEMENGE];
			} else if ("Artikeloffenemenge".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELOFFENEMENGE];
			} else if ("Offenerahmenmenge".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_OFFENERAHMENMENGE];
			} else if ("Artikeloffenerwert".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELOFFENERWERT];
			} else if ("Artikeloffenerdb".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELOFFENERDB];
			} else if ("F_AUFTRAGPOENALE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGPOENALE];
			} else if ("F_AUFTRAGROHS".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGROHS];
			} else if ("F_INTERNERKOMMENTAR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_INTERNERKOMMENTAR];
			} else if ("F_POSITIONSTERMIN".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_POSITIONSTERMIN];
			} else if ("F_ARTIKELLAGERSTAND".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELLAGERSTAND];
			} else if ("F_LAGERSTAND".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_LAGERSTAND];
			} else if ("F_POSITIONSSTATUS".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_POSITIONSSTATUS];
			} else if ("F_FIKTIVERLAGERSTAND".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_FIKTIVERLAGERSTAND];
			} else if ("F_KUNDEAUFTRAGADRESSE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_KUNDEAUFTRAGADRESSE];
			} else if ("F_KUNDELIEFERADRESSE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_KUNDELIEFERADRESSE];
			} else if ("F_LOSNUMMER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_LOSNUMMER];
			} else if (F_AUFTRAGEIGENSCHAFT_FA.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGEIGENSCHAFT_FA];
			} else if (F_AUFTRAGEIGENSCHAFT_CLUSTER.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGEIGENSCHAFT_CLUSTER];
			} else if (F_AUFTRAGEIGENSCHAFT_EQNR.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGEIGENSCHAFT_EQNR];
			} else if ("Artikelnettogesamtpreisplusversteckteraufschlagminusrabatte"
					.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELNETTOGESAMTPREISPLUSVERSTECKTERAUFSCHLAGMINUSRABATTE];
			} else if ("F_STKL_FERTIGUNGSGRUPPE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_STKL_FERTIGUNGSGRUPPE];
			} else if (F_ARTIKELMENGE_OFFEN.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELBESTELLTMENGE];
			} else if ("F_ERSTLOS".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ERSTLOS];
			} else if ("F_NURMATERIALLISTEN".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_NUR_MATERIALLISTEN];
			} else if ("F_ZEITPUNKT_VERRECHENBAR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ZEIT_VERRECHENBAR];
			} else if ("F_PERSON_VERRECHENBAR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_PERSON_VERRECHENBAR];
			}
		} else if (cAktuellerReport
				.equals(AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG)) {
			if ("F_POSITION".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION];
			}
			if ("F_POSITION_NR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION_NR];
			} else if ("F_POSITION_TITEL".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION_TITEL]);
			} else if ("F_POSITION_UNTERTITEL".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION_UNTERTITEL];
			} else if ("F_POSITION_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION_BEZEICHNUNG];
			} else if ("F_POSITION_ZUSATZBEZ".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION_ZUSATZBEZ];
			} else if ("F_POSITION_ZUSATZBEZ2".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION_ZUSATZBEZ2];
			} else if ("F_POSITION_KOMMENTAR_TEXT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION_KOMMENTAR_TEXT];
			} else if ("F_POSITION_KOMMENTAR_IMAGE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION_KOMMENTAR_IMAGE];
			} else if ("F_MENGE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_MENGE];
			} else if ("F_GELIEFERTEMENGE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_GELIEFERTE_MENGE];
			} else if ("F_EINHEIT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_EINHEIT];
			} else if ("F_EINZELPREIS".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_EINZELPREIS];
			} else if ("F_MATERIALZUSCHLAG".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_MATERIALZUSCHLAG];
			} else if ("F_RABATTSATZ".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_RABATTSATZ];
			} else if ("F_ZUSATZRABATTSATZ".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ZUSATZRABATTSATZ];
			} else if ("F_MWSTSATZ".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_MWSTSATZ];
			} else if ("F_GESAMTPREIS".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_GESAMTPREIS];
			} else if ("F_POSITIONSART".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITIONSART];
			} else if ("F_POSITIONTERMIN".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITIONTERMIN];
			} else if ("F_SETARTIKEL_TYP".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_SETARTIKEL_TYP];
			} else if ("F_VERLEIHTAGE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_VERLEIHTAGE];
			} else if ("F_VERLEIHFAKTOR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_VERLEIHFAKTOR];
			} else if ("F_FREIERTEXT".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_FREIERTEXT]);
			} else if ("F_LEERZEILE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_LEERZEILE];
			} else if ("F_IMAGE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_IMAGE];
			} else if ("F_SEITENUMBRUCH".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_SEITENUMBRUCH];
			} else if ("F_LV_POSITION".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_LV_POSITION];
			} else if ("F_STKLMENGE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_STKLMENGE];
			} else if ("F_STKLEINHEIT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_STKLEINHEIT];
			} else if ("F_STKLARTIKELCNR".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_STKLARTIKELCNR]);
			} else if ("F_STKLARTIKELBEZ".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_STKLARTIKELBEZ];
			} else if ("F_STKLARTIKELKBEZ".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_STKLARTIKELKBEZ];
			} else if ("F_STKLARTIKEL_KDARTIKELNR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_STKLARTIKEL_KDARTIKELNR];
			} else if ("F_STKLARTIKEL_KDPREIS".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_STKLARTIKEL_KDPREIS];
			} else if (F_IDENTNUMMER.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_IDENTNUMMER];
			} else if (F_BEZEICHNUNG.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_BEZEICHNUNG];
			} else if (F_KURZBEZEICHNUNG.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_KURZBEZEICHNUNG];
			} else if ("F_ARTIKELCZBEZ2".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ARTIKELCZBEZ2];
			} else if (F_KUNDEARTIKELNR.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_KUNDEARTIKELNR];
			} else if ("F_KUNDEARTIKELNR_AUFTRAGSADRESSE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_KUNDEARTIKELNR_AUFTRAGSADRESSE];
			} else if ("F_ZWANGSSERIENNUMMER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ZWANGSSERIENNUMMER];
			} else if (F_REFERENZNUMMER.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_REFERENZNUMMER];
			} else if (F_ARTIKEL_BAUFORM.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_BAUFORM];
			} else if (F_ARTIKEL_VERPACKUNGSART.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_VERPACKUNGSART];
			} else if (F_ARTIKEL_MATERIAL.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_MATERIAL];
			} else if (F_ARTIKEL_MATERIALGEWICHT.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_MATERIALGEWICHT];
			} else if (F_ARTIKEL_KURS_MATERIALZUSCHLAG.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_KURS_MATERIALZUSCHLAG];
			} else if (F_ARTIKEL_DATUM_MATERIALZUSCHLAG.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_DATUM_MATERIALZUSCHLAG];
			} else if (F_ARTIKEL_BREITE.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_BREITE];
			} else if (F_ARTIKEL_HOEHE.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_HOEHE];
			} else if (F_ARTIKEL_TIEFE.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_TIEFE];
			} else if ("F_FIKTIVERLAGERSTAND".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_FIKTIVERLAGERSTAND];
			} else if ("F_POSITIONSTERMIN_TIMESTAMP".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_LIEFERTERMIN];
			} else if ("F_TYP_CNR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_TYP_CNR];
			} else if ("F_IDENT_TEXTEINGABE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_IDENT_TEXTEINGABE];
			} else if ("F_POSITIONSOBJEKT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITIONOBJEKT];
			} else if ("F_ARTIKEL_INDEX".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_INDEX];
			} else if ("F_ARTIKEL_WERBEABGABEPFLICHTIG".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_WERBEABGABEPFLICHTIG];
			} else if ("F_ARTIKEL_REVISION".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_REVISION];
			} else if ("F_VONPOSITION".equals(fieldName)) {
				value = data[index][REPORT_AUFTRAGBESTAETIGUNG_VONPOSITION];
			} else if ("F_BISPOSITION".equals(fieldName)) {
				value = data[index][REPORT_AUFTRAGBESTAETIGUNG_BISPOSITION];
			} else if ("F_ZWSNETTOSUMME".equals(fieldName)) {
				value = data[index][REPORT_AUFTRAGBESTAETIGUNG_ZWSNETTOSUMME];
			} else if ("F_ZWSTEXT".equals(fieldName)) {
				value = data[index][REPORT_AUFTRAGBESTAETIGUNG_ZWSTEXT];
			} else if ("F_ZWSPOSPREISDRUCKEN".equals(fieldName)) {
				value = data[index][REPORT_AUFTRAGBESTAETIGUNG_ZWSPOSPREISDRUCKEN];
			} else if ("F_RAHMENMENGE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_RAHMENMENGE];
			} else if ("F_ABGERUFENE_MENGE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ABGERUFENE_MENGE];
			} else if ("F_LETZTER_ABRUF".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_LETZTER_ABRUF];
			}

		} else if (cAktuellerReport
				.equals(AuftragReportFac.REPORT_AUFTRAG_PACKLISTE)
				|| cAktuellerReport
						.equals(AuftragReportFac.REPORT_AUFTRAG_PACKLISTE3)
				|| cAktuellerReport
						.equals(AuftragReportFac.REPORT_AUFTRAG_PACKLISTE4)
				|| cAktuellerReport
						.equals(AuftragReportFac.REPORT_AUFTRAG_PACKLISTE_SNR)) {
			if ("Ident".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_IDENT];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_BEZEICHNUNG];
			} else if ("Gesamtmenge".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_GESAMTMENGE];
			} else if ("Offenemenge".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_OFFENEMENGE];
			} else if ("Lagerstand".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_LAGERSTAND];
			} else if ("Lagerort".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_LAGERORT];
			} else if ("Gewicht".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_GEWICHT];
			} else if ("F_RASTER_LIEGEND".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_RASTER_LIEGEND];
			} else if ("F_RASTER_STEHEND".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_RASTER_STEHEND];
			} else if ("F_MATERIALGEWICHT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_MATERIALGEWICHT];
			} else if ("F_SERIENCHARGENR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_SERIENCHARGENR];
			} else if ("F_VOLLSTAENDIGKEIT_IDENT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_VOLLSTAENDIGKEIT_KOMPONENTEN_IDENT];
			} else if ("F_VOLLSTAENDIGKEIT_BEZ".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_VOLLSTAENDIGKEIT_KOMPONENTEN_BEZ];
			} else if ("F_IMAGE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_POSITION_KOMMENTAR_IMAGE];
			} else if ("F_FREIERTEXT".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][AuftragReportFac.REPORT_PACKLISTE_POSITION_FREIERTEXT]);
			} else if ("F_VORZEICHEN".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_VORZEICHEN];
			} else if ("F_POSITIONSTERMIN".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_POSITIONSTERMIN];
			} else if ("F_POSITIONSTERMIN_TIMESTAMP".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_POSITIONSTERMIN_TIMESTAMP];
			} else if ("F_FIKTIVERLAGERSTAND".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_FIKTIVERLAGERSTAND];
			} else if ("F_ARTIKELKOMMENTAR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_ARTIKELKOMMENTAR];

			} else if ("F_FARBCODE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_FARBCODE];
			} else if ("F_ARTIKEL_HOEHE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_HOEHE];
			} else if (F_ARTIKEL_BREITE.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_BREITE];
			} else if (F_ARTIKEL_TIEFE.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_TIEFE];
			} else if (F_ARTIKEL_BAUFORM.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_BAUFORM];
			} else if (F_ARTIKEL_VERPACKUNGSART.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_VERPACKUNGSART];
			} else if ("F_ARTIKELKLASSE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_ARTIKELKLASSE];
			} else if ("F_ARTIKELGRUPPE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_ARTIKELGRUPPE];
			} else if ("F_MATERIAL".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_MATERIAL];
			} else if ("F_VERKAUFSEAN".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_VERKAUFSEAN];
			} else if ("F_VERPACKUNGSEAN".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_VERPACKUNGSEAN];
			} else if ("F_VERPACKUNGSMENGE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_VERPACKUNGSMENGE];
			} else if ("F_ARBEITSGAENGE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_ARBEITSGAENGE];
			} else if ("F_MENGENTEILER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_MENGENTEILER];
			} else if ("F_POSITIONSSTATUS".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_POSITIONSSTATUS];
			} else if ("F_SUBREPORT_DATA".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_PACKLISTE_SUBREPORT_DATA];
			}

		} else if (cAktuellerReport
				.equals(AuftragReportFac.REPORT_AUFTRAG_VORKALKULATION)) {
			if ("F_IDENT".equals(fieldName)) {
				value = data[index][AngebotReportFac.REPORT_VORKALKULATION_IDENT];
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
			}
		} else if (cAktuellerReport
				.equals(AuftragReportFac.REPORT_AUFTRAG_OFFENE_OHNE_DETAILS)) {
			if ("F_AUFTRAGSNUMMER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_AUFTRAGSNUMMER];
			} else if ("F_AUFTRAGSART".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_AUFTRAGSART];
			} else if ("F_KUNDE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_KUNDE];
			} else if ("F_INTERNERKOMMENTAR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_INTERNERKOMMENTAR];
			} else if ("F_PROJEKT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_PROJEKT];
			} else if ("F_BESTELLNUMMER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_AUFTRAGBESTELLNUMMER];
			} else if ("F_VERTRETER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_VERTRETERCNAME1];
			} else if ("F_KOSTENSTELLENUMMER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_KOSTENSTELLENUMMER];
			} else if ("F_WERT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_WERT];
			} else if ("F_WERTOFFEN".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_WERTOFFEN];
			} else if ("F_LIEFERTERMIN".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_LIEFERTERMIN];
			} else if ("F_ORT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_KUNDE_ORT];
			} else if (F_AUFTRAGEIGENSCHAFT_FA.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_AUFTRAGEIGENSCHAFT_FA];
			} else if (F_AUFTRAGEIGENSCHAFT_CLUSTER.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_AUFTRAGEIGENSCHAFT_CLUSTER];
			} else if (F_AUFTRAGEIGENSCHAFT_EQNR.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_AUFTRAGEIGENSCHAFT_EQNR];
			} else if ("F_ZEITPUNKT_VERRECHENBAR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_ZEIT_VERRECHENBAR];
			} else if ("F_PERSON_VERRECHENBAR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_PERSON_VERRECHENBAR];
			} else if ("F_RAHMENWERTOFFEN".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_RAHMENWERTOFFEN];
			} else if ("F_RAHMENAUFTRAG".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_RAHMENAUFTRAG];
			}

		} else if (cAktuellerReport
				.equals(AuftragReportFac.REPORT_AUFTRAG_TEIL_LIEFERBAR)) {
			if ("F_AUFTRAGSNUMMER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_TEILLIFERBAR_AUFTRAGSNUMMER];
			} else if ("F_KUNDE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_TEILLIFERBAR_KUNDE];
			} else if ("F_PROJEKT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_TEILLIFERBAR_PROJEKT];
			} else if ("F_KOSTENSTELLENUMMER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_TEILLIFERBAR_KOSTENSTELLENUMMER];
			} else if ("F_WERT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_TEILLIFERBAR_WERT];
			} else if ("F_WERTOFFEN".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_TEILLIFERBAR_WERTOFFEN];
			} else if ("F_LIEFERTERMIN".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_TEILLIFERBAR_LIEFERTERMIN];
			} else if ("F_ORT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAG_TEILLIFERBAR_KUNDE_ORT];
			}
		} else if (cAktuellerReport
				.equals(AuftragReportFac.REPORT_AUFTRAG_WIEDERBESCHAFFUNG)) {
			if (F_BEZEICHNUNG.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_BEZEICHNUNG];
			} else if ("F_EINHEIT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_EINHEIT];
			} else if ("F_GESTEHUNGSPREIS".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_GESTEHUNGSPREIS];
			} else if (F_IDENTNUMMER.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_IDENTNUMMER];
			} else if ("F_LAGERSTAND".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_LAGERSTAND];
			} else if ("F_LAGERSTAND_SPERRLAEGER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_LAGERSTAND_SPERRLAEGER];
			} else if ("F_MENGE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_MENGE];
			} else if ("F_MENGE_BESTELLT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_MENGE_BESTELLT];
			} else if ("F_MENGE_FEHLMENGE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_MENGE_FEHLMENGE];
			} else if ("F_MENGE_RESERVIERT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_MENGE_RESERVIERT];
			} else if ("F_WIEDERBESCHAFFUNGSZEIT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_WIEDERBESCHAFFUNGSZEIT];
			} else if ("F_DURCHLAUFZEIT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_DURCHLAUFZEIT];
			} else if (F_KURZBEZEICHNUNG.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_KURZBEZEICHNUNG];
			} else if ("F_BEDARF".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_BEDARF];
			} else if ("F_LAGERND".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_LAGERND];
			} else if ("F_LIEFERANT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_LIEFERANT];
			} else if ("F_LIEFERANT_ARTIKELNUMMER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_LIEFERANT_ARTIKELNUMMER];
			} else if ("F_LIEFERANT_ARTIKELBEZEICHNUNG".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_LIEFERANT_ARTIKELBEZEICHNUNG];
			} else if ("F_SUMME_SOLLZEITEN".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_SUMME_SOLLZEITEN];
			} else if ("F_SETARTIKEL_TYP".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_SETARTIKEL_TYP];
			} else if ("F_STKL_EBENE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_STKL_EBENE];
			} else if ("F_GESAMT_WIEDERBESCHAFFUNGSZEIT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_GESAMT_WIEDERBESCHAFFUNGSZEIT];
			}
		} else if (cAktuellerReport
				.equals(AuftragReportFac.REPORT_AUFTRAG_ROLLIERENDEPLANUNG)) {
			if (F_BEZEICHNUNG.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_BEZEICHNUNG];
			} else if ("F_EINHEIT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_EINHEIT];
			} else if ("F_GESTEHUNGSPREIS".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_GESTEHUNGSPREIS];
			} else if (F_IDENTNUMMER.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_IDENTNUMMER];
			} else if ("F_LAGERSTAND".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_LAGERSTAND];
			} else if ("F_LAGERSTAND_SPERRLAEGER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_LAGERSTAND_SPERRLAEGER];
			} else if ("F_MENGE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE];
			} else if ("F_MENGE2".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE2];
			} else if ("F_MENGE3".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE3];
			} else if ("F_MENGE4".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE4];
			} else if ("F_MENGE5".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE5];
			} else if ("F_MENGE6".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE6];
			} else if ("F_MENGE7".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE7];
			} else if ("F_MENGE8".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE8];
			} else if ("F_MENGE9".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE9];
			} else if ("F_MENGE10".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE10];
			} else if ("F_MENGE11".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE11];
			} else if ("F_MENGE12".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE12];
			} else if ("F_MENGE13".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE13];
			} else if ("F_MENGE_BESTELLT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE_BESTELLT];
			} else if ("F_MENGE_FEHLMENGE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE_FEHLMENGE];
			} else if ("F_MENGE_RESERVIERT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE_RESERVIERT];
			} else if ("F_WIEDERBESCHAFFUNGSZEIT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_WIEDERBESCHAFFUNGSZEIT];
			} else if ("F_DURCHLAUFZEIT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_DURCHLAUFZEIT];
			} else if (F_KURZBEZEICHNUNG.equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_KURZBEZEICHNUNG];
			} else if ("F_LIEFERANT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_LIEFERANT];
			} else if ("F_LIEFERANT_ARTIKELNUMMER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_LIEFERANT_ARTIKELNUMMER];
			} else if ("F_LIEFERANT_ARTIKELBEZEICHNUNG".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_LIEFERANT_ARTIKELBEZEICHNUNG];
			}

		} else if (cAktuellerReport
				.equals(AuftragReportFac.REPORT_AUFTRAG_SRN_ETIKETT_G)) {
			if ("F_IDENT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_SRN_ETIKETT_IDENT];
			} else if ("F_BEZ".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_SRN_ETIKETT_BEZEICHNUNG];
			} else if ("F_FA".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_SRN_ETIKETT_FA];
			} else if ("F_CLUSTER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_SRN_ETIKETT_CLUSTER];
			} else if ("F_EQNR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_SRN_ETIKETT_EQNR];
			} else if ("F_SRNNR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_SRN_ETIKETT_SRNNR];
			} else if ("F_LIEFERTERMIN".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_SRN_ETIKETT_LIEFERTERMIN];
			} else if ("F_PRUEFTERMIN".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_SRN_ETIKETT_PRUEFTERMIN];
			} else if ("F_STLK_INDEX".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_SRN_ETIKETT_STLK_INDEX];
			} else if ("F_LIEFERANTENNUMMER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_SRN_ETIKETT_LIEFERANTENNUMMER];
			} else if ("F_KUNDENNAME".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_SRN_ETIKETT_KUNDENNAME];
			} else if ("F_KUNDENADRESSE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_SRN_ETIKETT_KUNDENADRESSE];
			} else if ("F_KOMMENTAR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_SRN_ETIKETT_KOMMENTAR];
			} else if ("F_LIEFERADRESSE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_SRN_ETIKETT_LIEFERADRESSE];
			}
		} else if (cAktuellerReport
				.equals(AuftragReportFac.REPORT_AUFTRAG_SRN_ETIKETT_K)) {
			if ("F_IDENT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_SRN_ETIKETT_IDENT];
			} else if ("F_BEZ".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_SRN_ETIKETT_BEZEICHNUNG];
			} else if ("F_FA".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_SRN_ETIKETT_FA];
			} else if ("F_CLUSTER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_SRN_ETIKETT_CLUSTER];
			} else if ("F_EQNR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_SRN_ETIKETT_EQNR];
			} else if ("F_SRNNR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_SRN_ETIKETT_SRNNR];
			} else if ("F_LIEFERTERMIN".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_SRN_ETIKETT_LIEFERTERMIN];
			} else if ("F_PRUEFTERMIN".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_SRN_ETIKETT_PRUEFTERMIN];
			} else if ("F_STLK_INDEX".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_SRN_ETIKETT_STLK_INDEX];
			} else if ("F_LIEFERANTENNUMMER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_SRN_ETIKETT_LIEFERANTENNUMMER];
			} else if ("F_KUNDENNAME".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_SRN_ETIKETT_KUNDENNAME];
			} else if ("F_KUNDENADRESSE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_SRN_ETIKETT_KUNDENADRESSE];
			} else if ("F_KOMMENTAR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_SRN_ETIKETT_KOMMENTAR];
			} else if ("F_LIEFERADRESSE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_SRN_ETIKETT_LIEFERADRESSE];
			}
		} else if (cAktuellerReport
				.equals(AuftragReportFac.REPORT_AUFTRAGSZEITEN)) {
			if ("Person".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGZEITEN_PERSON];
			} else if ("Artikel".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGZEITEN_ARTIKEL];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGZEITEN_BEZEICHNUNG];
			} else if ("Stundensatz".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGZEITEN_STUNDENSATZ];
			} else if ("Von".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGZEITEN_VON];
			} else if ("Bis".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGZEITEN_BIS];
			} else if ("Dauer".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGZEITEN_DAUER];
			} else if ("Bemerkung".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGZEITEN_BEMERKUNG];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGZEITEN_KOMMENTAR];
			} else if ("Kosten".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGZEITEN_KOSTEN];
			} else if ("BezeichnungPosition".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGZEITEN_BEZEICHNUNG_POSITION];
			} else if ("ArtikelPosition".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGZEITEN_ARTIKEL_POSITION];
			} else if ("ZusatzbezeichnungPosition".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGZEITEN_ZUSATZBEZEICHNUNG_POSITION];
			} else if ("Positionsnummer".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_AUFTRAGZEITEN_POSITIONSNUMMER];
			}

		} else if (cAktuellerReport
				.equals(AuftragReportFac.REPORT_AUFTRAG_ALLE)) {
			if ("F_AUFTRAGCNR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ALLE_CNR];
			} else if ("F_KUNDECNAME1".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ALLE_KUNDE];
			} else if ("F_KOSTENSTELLECNR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ALLE_KOSTENSTELLE];
			} else if ("F_DATUM".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ALLE_DATUM];
			} else if ("F_VERTRETERCNAME1".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ALLE_VERTRETER];
			} else if ("F_STATUS".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ALLE_STATUS];
			} else if ("F_WERT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ALLE_WERT];
			} else if ("F_AUFTRAGART".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ALLE_AUFTRAGART];
			} else if ("F_PROJEKT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ALLE_PROJEKT];
			} else if ("F_WAEHRUNG".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ALLE_WAEHRUNG];
			} else if ("F_KURS".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ALLE_KURS];
			} else if ("F_BESTELLTERMIN".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ALLE_BESTELLTERMIN];
			} else if ("F_LIEFERTERMIN".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ALLE_LIEFERTERMIN];
			} else if ("F_FINALTERMIN".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ALLE_FINALTERMIN];
			} else if ("F_POENALE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ALLE_POENALE];
			} else if ("F_ROHS".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ALLE_ROHS];
			} else if ("F_TEILLIEFERBAR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ALLE_TEILLIEFERBAR];
			} else if ("F_UNVERBINDLICH".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ALLE_UNVERBINDLICH];
			} else if ("F_ADRESSE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ALLE_ADRESSE];
			} else if ("F_LIEFERADRESSE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ALLE_LIEFERADRESSE];
			} else if ("F_RECHNUNGSADRESSE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ALLE_RECHNUNGSADRESSE];
			} else if ("F_ANSPRECHPARTNER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ALLE_ANSPRECHPARTNER];
			} else if ("F_BESTELLNUMMER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ALLE_BESTELLNUMMER];
			} else if ("F_LAENDERART".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ALLE_LAENDERART];
			}
		} else if (cAktuellerReport
				.equals(AuftragReportFac.REPORT_AUFTRAG_ERLEDIGT)) {
			if ("F_AUFTRAGCNR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERLEDIGT_CNR];
			} else if ("F_KUNDECNAME1".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERLEDIGT_KUNDE];
			} else if ("F_KOSTENSTELLECNR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERLEDIGT_KOSTENSTELLE];
			} else if ("F_DATUM".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERLEDIGT_DATUM];
			} else if ("F_VERTRETERCNAME1".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERLEDIGT_VERTRETER];
			} else if ("F_STATUS".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERLEDIGT_STATUS];
			} else if ("F_ERLEDIGT_AM".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERLEDIGT_ERLEDIGT_AM];
			} else if ("F_ERLEDIGT_DURCH".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERLEDIGT_ERLEDIGT_DURCH];
			} else if ("F_WERT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERLEDIGT_WERT];
			} else if ("F_AUFTRAGART".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERLEDIGT_AUFTRAGART];
			} else if ("F_PROJEKT".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERLEDIGT_PROJEKT];
			} else if ("F_WAEHRUNG".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERLEDIGT_WAEHRUNG];
			} else if ("F_KURS".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERLEDIGT_KURS];
			} else if ("F_BESTELLTERMIN".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERLEDIGT_BESTELLTERMIN];
			} else if ("F_LIEFERTERMIN".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERLEDIGT_LIEFERTERMIN];
			} else if ("F_FINALTERMIN".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERLEDIGT_FINALTERMIN];
			} else if ("F_POENALE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERLEDIGT_POENALE];
			} else if ("F_ROHS".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERLEDIGT_ROHS];
			} else if ("F_TEILLIEFERBAR".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERLEDIGT_TEILLIEFERBAR];
			} else if ("F_UNVERBINDLICH".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERLEDIGT_UNVERBINDLICH];
			} else if ("F_ADRESSE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERLEDIGT_ADRESSE];
			} else if ("F_LIEFERADRESSE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERLEDIGT_LIEFERADRESSE];
			} else if ("F_RECHNUNGSADRESSE".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERLEDIGT_RECHNUNGSADRESSE];
			} else if ("F_ANSPRECHPARTNER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERLEDIGT_ANSPRECHPARTNER];
			} else if ("F_BESTELLNUMMER".equals(fieldName)) {
				value = data[index][AuftragReportFac.REPORT_ERLEDIGT_BESTELLNUMMER];
			}
		}

		return value;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printRahmenuebersicht(Integer auftragIId,
			TheClientDto theClientDto) {

		cAktuellerReport = AuftragReportFac.REPORT_RAHMENUEBERSICHT;

		ArrayList alDaten = new ArrayList();

		AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(
				auftragIId);
		HashMap<String, Object> mapParameter = new HashMap<String, Object>();
		try {
			AuftragpositionDto[] aufposDtos = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(auftragIId);

			for (int i = 0; i < aufposDtos.length; i++) {
				befuelleZeileRahmenUebersicht(auftragDto, aufposDtos[i],
						alDaten, theClientDto);
			}

			AuftragDto[] abrufAuftragDtos = getAuftragFac()
					.abrufauftragFindByAuftragIIdRahmenauftrag(auftragIId,
							theClientDto);

			for (int i = 0; i < abrufAuftragDtos.length; i++) {
				AuftragpositionDto[] abrufaufposDtos = getAuftragpositionFac()
						.auftragpositionFindByAuftrag(
								abrufAuftragDtos[i].getIId());
				for (int j = 0; j < abrufaufposDtos.length; j++) {
					befuelleZeileRahmenUebersicht(abrufAuftragDtos[i],
							abrufaufposDtos[j], alDaten, theClientDto);
				}

			}

			Object[][] returnArray = new Object[alDaten.size()][AuftragReportFac.REPORT_RAHMENUEBERSICHT_ANZAHL_SPALTEN];
			data = (Object[][]) alDaten.toArray(returnArray);

			// die Parameter dem Report uebergeben

			mapParameter.put("P_AUFTRAG", auftragDto.getCNr());
			mapParameter.put("P_FINALTERMIN", auftragDto.getDFinaltermin());
			mapParameter.put("P_PROJEKT",
					auftragDto.getCBezProjektbezeichnung());

			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					auftragDto.getKundeIIdAuftragsadresse(), theClientDto);
			mapParameter.put("P_KUNDE_NAME1", kundeDto.getPartnerDto()
					.getCName1nachnamefirmazeile1());
			mapParameter.put("P_KUNDE_NAME2", kundeDto.getPartnerDto()
					.getCName2vornamefirmazeile2());
			mapParameter.put("P_KUNDE_NAME3", kundeDto.getPartnerDto()
					.getCName3vorname2abteilung());

			if (kundeDto.getPartnerDto().getLandplzortDto() != null) {
				mapParameter.put("P_KUNDE_STRASSE", kundeDto.getPartnerDto()
						.getCStrasse());
				mapParameter.put("P_KUNDE_LAND", kundeDto.getPartnerDto()
						.getLandplzortDto().getLandDto().getCLkz());
				mapParameter.put("P_KUNDE_PLZ", kundeDto.getPartnerDto()
						.getLandplzortDto().getCPlz());
				mapParameter.put("P_KUNDE_ORT", kundeDto.getPartnerDto()
						.getLandplzortDto().getOrtDto().getCName());
			}

			mapParameter.put(
					"P_ZAHLUNGSZIEL",
					getMandantFac().zahlungszielFindByIIdLocaleOhneExc(
							auftragDto.getZahlungszielIId(),
							theClientDto.getLocUi(), theClientDto));

			mapParameter.put(
					"P_LIEFERART",
					getLocaleFac().lieferartFindByIIdLocaleOhneExc(
							auftragDto.getLieferartIId(),
							theClientDto.getLocUi(), theClientDto));

			KostenstelleDto kostenstelleDto = getSystemFac()
					.kostenstelleFindByPrimaryKey(auftragDto.getKostIId());
			mapParameter.put("P_KOSTENSTELLE",
					kostenstelleDto.formatKostenstellenbezeichnung());

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		initJRDS(mapParameter, AuftragReportFac.REPORT_MODUL,
				AuftragReportFac.REPORT_RAHMENUEBERSICHT,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	private void befuelleZeileRahmenUebersicht(AuftragDto auftragDto,
			AuftragpositionDto auftragpositionDto, ArrayList alDaten,
			TheClientDto theClientDto) {

		if (auftragpositionDto.getNMenge() != null) {
			Object[] oZeile = new Object[AuftragReportFac.REPORT_RAHMENUEBERSICHT_ANZAHL_SPALTEN];

			oZeile[AuftragReportFac.REPORT_RAHMENUEBERSICHT_AUFRAGART] = auftragDto
					.getAuftragartCNr();
			oZeile[AuftragReportFac.REPORT_RAHMENUEBERSICHT_AUFTRAGWERT] = auftragDto
					.getNGesamtauftragswertInAuftragswaehrung();
			oZeile[AuftragReportFac.REPORT_RAHMENUEBERSICHT_AUFRAGNR] = auftragDto
					.getCNr();
			oZeile[AuftragReportFac.REPORT_RAHMENUEBERSICHT_BELEGDATUM] = auftragDto
					.getTBelegdatum();
			oZeile[AuftragReportFac.REPORT_RAHMENUEBERSICHT_KUNDENBESTELLNUMMER] = auftragDto
					.getCBestellnummer();
			oZeile[AuftragReportFac.REPORT_RAHMENUEBERSICHT_LIEFERTERMIN] = auftragDto
					.getDLiefertermin();
			oZeile[AuftragReportFac.REPORT_RAHMENUEBERSICHT_MENGE] = auftragpositionDto
					.getNMenge();

			Boolean bStorniert = false;
			if (auftragDto.getStatusCNr().equals(LocaleFac.STATUS_STORNIERT)) {
				bStorniert = true;
			}
			oZeile[AuftragReportFac.REPORT_RAHMENUEBERSICHT_STORNIERT] = bStorniert;

			oZeile[AuftragReportFac.REPORT_RAHMENUEBERSICHT_PREIS] = auftragpositionDto
					.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte();

			if (auftragpositionDto.getPositionsartCNr().equals(
					AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {

				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
						auftragpositionDto.getArtikelIId(), theClientDto);

				oZeile[AuftragReportFac.REPORT_RAHMENUEBERSICHT_ARTIKELNUMMER] = aDto
						.getCNr();
				oZeile[AuftragReportFac.REPORT_RAHMENUEBERSICHT_ARTIKELBEZEICHNUNG] = aDto
						.formatBezeichnung();

			} else {
				oZeile[AuftragReportFac.REPORT_RAHMENUEBERSICHT_ARTIKELBEZEICHNUNG] = auftragpositionDto
						.getCBez();
			}

			alDaten.add(oZeile);

			try {

				LieferscheinpositionDto[] lsPosDtos = getLieferscheinpositionFac()
						.lieferscheinpositionFindByAuftragpositionIId(
								auftragpositionDto.getIId(), theClientDto);

				String ls = "";
				for (int u = 0; u < lsPosDtos.length; u++) {

					LieferscheinpositionDto lsPosDto = lsPosDtos[u];
					oZeile = oZeile.clone();

					if (lsPosDto.getLieferscheinpositionartCNr().equals(
							AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {

						ArtikelDto aDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(
										lsPosDto.getArtikelIId(), theClientDto);

						oZeile[AuftragReportFac.REPORT_RAHMENUEBERSICHT_ARTIKELNUMMER] = aDto
								.getCNr();
						oZeile[AuftragReportFac.REPORT_RAHMENUEBERSICHT_ARTIKELBEZEICHNUNG] = aDto
								.formatBezeichnung();

					} else {
						oZeile[AuftragReportFac.REPORT_RAHMENUEBERSICHT_ARTIKELBEZEICHNUNG] = lsPosDto
								.getCBez();
					}

					oZeile[AuftragReportFac.REPORT_RAHMENUEBERSICHT_MENGE] = lsPosDto
							.getNMenge();

					oZeile[AuftragReportFac.REPORT_RAHMENUEBERSICHT_PREIS] = lsPosDto
							.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte();

					LieferscheinDto lsDto = getLieferscheinFac()
							.lieferscheinFindByPrimaryKey(
									lsPosDto.getLieferscheinIId());

					Boolean bStorniertLS = false;
					if (lsDto.getStatusCNr().equals(LocaleFac.STATUS_STORNIERT)) {
						bStorniertLS = true;
					}
					oZeile[AuftragReportFac.REPORT_RAHMENUEBERSICHT_STORNIERT] = bStorniertLS;

					oZeile[AuftragReportFac.REPORT_RAHMENUEBERSICHT_LIEFERSCHEIN] = lsDto
							.getCNr();
					if (lsDto.getRechnungIId() != null) {
						oZeile[AuftragReportFac.REPORT_RAHMENUEBERSICHT_RECHNUNG] = getRechnungFac()
								.rechnungFindByPrimaryKey(
										lsDto.getRechnungIId()).getCNr();
					}

					alDaten.add(oZeile);
				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}

	}

	/**
	 * Alle offenen Auftraege fuer einen bestimmten Mandanten drucken.
	 * 
	 * @param krit
	 *            die Filter- und Sortierkriterien
	 * @param dStichtag
	 *            Date
	 * @param bSortierungNachLiefertermin
	 *            Boolean
	 * @param bInternenKommentarDrucken
	 *            Boolean
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @return JasperPrint der Druck
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAuftragOffene(ReportJournalKriterienDto krit,
			Date dStichtag, Boolean bSortierungNachLiefertermin,
			Boolean bInternenKommentarDrucken, Integer iArt,
			Integer iArtUnverbindlich, boolean bMitAngelegten,
			boolean bStichtagGreiftBeiLiefertermin, TheClientDto theClientDto)
			throws EJBExceptionLP {

		JasperPrintLP oPrintO = null;
		cAktuellerReport = AuftragReportFac.REPORT_AUFTRAG_OFFENE;
		// die Parameter dem Report uebergeben
		HashMap<String, Object> mapParameter = new HashMap<String, Object>();

		Locale locDruck = null;

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		// vom Stichtag die Uhrzeit abschneiden
		dStichtag = Helper.cutDate(dStichtag);

		try {

			ParametermandantDto parametermandantDto = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ANGEBOT,
							ParameterFac.PARAMETER_LIEFERANT_ANGEBEN);

			boolean bLieferantAngeben = (Boolean) parametermandantDto
					.getCWertAsObject();

			session = factory.openSession();
			// Hiberante Criteria fuer alle Tabellen ausgehend von meiner
			// Haupttabelle anlegen,
			// nach denen ich filtern und sortieren kann

			Criteria crit = session
					.createCriteria(FLRAuftragpositionReport.class);

			crit.createAlias("flrauftrag", "a");

			// Einschraenkung auf den aktuellen Mandanten
			crit.add(Restrictions.eq(
					"a." + AuftragFac.FLR_AUFTRAG_MANDANT_C_NR,
					theClientDto.getMandant()));
			// Einschraenkung nach Auftragart

			if (iArt != null) {
				if (iArt == 1) {
					crit.add(Restrictions.ne("a."
							+ AuftragFac.FLR_AUFTRAG_AUFTRAGART_C_NR,
							AuftragServiceFac.AUFTRAGART_RAHMEN));
					mapParameter.put(
							"P_ART_RAHMENAUFTRAEGE",
							getTextRespectUISpr(
									"auft.journal.ohnerahmenauftraege",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
				} else if (iArt == 2) {
					crit.add(Restrictions.eq("a."
							+ AuftragFac.FLR_AUFTRAG_AUFTRAGART_C_NR,
							AuftragServiceFac.AUFTRAGART_RAHMEN));
					mapParameter.put(
							"P_ART_RAHMENAUFTRAEGE",
							getTextRespectUISpr(
									"auft.journal.nurrahmenauftraege",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
				}
			}

			mapParameter.put("P_ART_UNVERBINDLICH", iArtUnverbindlich);

			if (iArtUnverbindlich != AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTUNVERBINDLICH_ALLE) {
				if (iArtUnverbindlich == AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTUNVERBINDLICH_NUR_UNVERBINDLICHE) {
					crit.add(Restrictions
							.eq("a."
									+ AuftragFac.FLR_AUFTRAG_B_LIEFERTERMINUNVERBINDLICH,
									Helper.boolean2Short(true)));

				} else if (iArtUnverbindlich == AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTUNVERBINDLICH_OHNE_UNVERBINDLICHE) {
					crit.add(Restrictions
							.eq("a."
									+ AuftragFac.FLR_AUFTRAG_B_LIEFERTERMINUNVERBINDLICH,
									Helper.boolean2Short(false)));

				}
			}

			crit.add(Restrictions
					.ne(AuftragpositionFac.FLR_AUFTRAGPOSITION_AUFTRAGPOSITIONSTATUS_C_NR,
							AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT));
			crit.add(Restrictions
					.ne("auftragpositionart_c_nr",
							AuftragServiceFac.AUFTRAGPOSITIONART_INTELLIGENTE_ZWISCHENSUMME));
			crit.add(Restrictions
					.isNotNull(AuftragpositionFac.FLR_AUFTRAGPOSITION_N_MENGE));

			// Einschraenkung nach Status Offen, Teilerledigt, Erledigt
			Collection<String> cStati = new LinkedList<String>();
			cStati.add(AuftragServiceFac.AUFTRAGSTATUS_OFFEN);

			if (bMitAngelegten == true) {
				cStati.add(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT);
			}

			cStati.add(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT);
			crit.add(Restrictions.in("a."
					+ AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR, cStati));

			// Das Belegdatum muss vor dem Stichtag liegen
			crit.add(Restrictions.le(
					"a." + AuftragFac.FLR_AUFTRAG_D_BELEGDATUM, dStichtag));
			mapParameter.put("P_STICHTAG", dStichtag);

			if (bStichtagGreiftBeiLiefertermin) {
				crit.add(Restrictions.or(
						Restrictions
								.isNull(AuftragpositionFac.FLR_AUFTRAGPOSITION_T_UEBERSTEUERTERLIEFERTERMIN),
						Restrictions
								.le(AuftragpositionFac.FLR_AUFTRAGPOSITION_T_UEBERSTEUERTERLIEFERTERMIN,
										dStichtag)));
				mapParameter.put(
						"P_STICHTAG_ART",
						getTextRespectUISpr(
								"auft.report.offene.stichtag.liefertermin",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			} else {
				// Der Auftrag darf zum Stichtag noch nicht erledigt worden sein
				crit.add(Restrictions.or(
						Restrictions.isNull("a."
								+ AuftragFac.FLR_AUFTRAG_T_ERLEDIGT),
						Restrictions.gt("a."
								+ AuftragFac.FLR_AUFTRAG_T_ERLEDIGT, dStichtag)));

				mapParameter.put(
						"P_STICHTAG_ART",
						getTextRespectUISpr(
								"auft.report.offene.stichtag.erledigungsdatum",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			}

			// Einschraenkung nach einer bestimmten Kostenstelle
			if (krit.kostenstelleIId != null) {
				crit.add(Restrictions.eq("a."
						+ AuftragFac.FLR_AUFTRAG_KOSTENSTELLE_I_ID,
						krit.kostenstelleIId));
				mapParameter.put("P_KOSTENSTELLE", getSystemFac()
						.kostenstelleFindByPrimaryKey(krit.kostenstelleIId)
						.getCNr());
			}

			// Einschraenkung nach einem bestimmten Kunden
			if (krit.kundeIId != null) {
				crit.add(Restrictions.eq("a."
						+ AuftragFac.FLR_AUFTRAG_KUNDE_I_ID_AUFTRAGSADRESSE,
						krit.kundeIId));
				mapParameter.put("P_KUNDE", getKundeFac()
						.kundeFindByPrimaryKey(krit.kundeIId, theClientDto)
						.getPartnerDto().getCName1nachnamefirmazeile1());
			}
			// Einschraenkung nach einem bestimmten Vertreter
			if (krit.vertreterIId != null) {
				crit.add(Restrictions.eq("a."
						+ AuftragFac.FLR_AUFTRAG_VERTRETER_I_ID,
						krit.vertreterIId));
				mapParameter.put(
						"P_VERTRETER",
						getPersonalFac()
								.personalFindByPrimaryKey(krit.vertreterIId,
										theClientDto).getPartnerDto()
								.getCName1nachnamefirmazeile1());

			}
			// Einschraenkung nach Belegdatum von - bis
			String sVon = null;
			String sBis = null;

			if (krit.dVon != null) {
				crit.add(Restrictions.ge("a."
						+ AuftragFac.FLR_AUFTRAG_D_BELEGDATUM, krit.dVon));
				sVon = Helper.formatDatum(krit.dVon, theClientDto.getLocUi());
			}

			if (krit.dBis != null) {
				crit.add(Restrictions.le("a."
						+ AuftragFac.FLR_AUFTRAG_D_BELEGDATUM, krit.dBis));
				sBis = Helper.formatDatum(krit.dBis, theClientDto.getLocUi());
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

			if (krit.sBelegnummerVon != null) {
				sVon = HelperServer.getBelegnummernFilterForHibernateCriterias(
						f, iGeschaeftsjahr, sMandantKuerzel,
						krit.sBelegnummerVon);
				crit.add(Restrictions.ge("a." + "c_nr", sVon));
			}

			if (krit.sBelegnummerBis != null) {
				sBis = HelperServer.getBelegnummernFilterForHibernateCriterias(
						f, iGeschaeftsjahr, sMandantKuerzel,
						krit.sBelegnummerBis);
				crit.add(Restrictions.le("a." + "c_nr", sBis));
			}
			// Sortierung nach Kostenstelle ist immer die erste Sortierung
			if (krit.bSortiereNachKostenstelle) {
				crit.createCriteria(
						"a." + AuftragFac.FLR_AUFTRAG_FLRKOSTENSTELLE)
						.addOrder(Order.asc("a.c_nr"));
			}

			// Sortierung nach Kunde, eventuell innerhalb der Kostenstelle
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				crit.createCriteria("a." + AuftragFac.FLR_AUFTRAG_FLRKUNDE)
						.createCriteria(KundeFac.FLR_PARTNER)
						.addOrder(
								Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
			}

			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_VERTRETER) {
				crit.createCriteria("a." + AuftragFac.FLR_AUFTRAG_FLRVERTRETER)
						.addOrder(
								Order.asc(PersonalFac.FLR_PERSONAL_C_KURZZEICHEN));
			}

			// Sortierung nach Liefertermin (optional)
			if (bSortierungNachLiefertermin != null
					&& bSortierungNachLiefertermin.booleanValue()) {
				crit.addOrder(Order.asc("a."
						+ AuftragFac.FLR_AUFTRAG_T_LIEFERTERMIN));
			}

			// es wird in jedem Fall nach der Belegnummer sortiert
			// crit.addOrder(Order.asc("c_nr"));

			List<?> list = crit.list();
			Iterator<?> it = list.iterator();

			data = new Object[list.size()][AuftragReportFac.REPORT_AUFTRAG_OFFENE_ANZAHL_SPALTEN];

			int i = 0;
			while (it.hasNext()) {
				FLRAuftragpositionReport item = (FLRAuftragpositionReport) it
						.next();
				FLRAuftragReport flrauftrag = item.getFlrauftrag();

				data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_AUFTRAGCNR] = flrauftrag
						.getC_nr();
				data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_AUFTRAGSART] = flrauftrag
						.getAuftragart_c_nr();
				data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_ZEIT_VERRECHENBAR] = flrauftrag
						.getT_verrechenbar();
				if (flrauftrag.getFlrpersonalverrechenbar() != null) {
					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_PERSON_VERRECHENBAR] = HelperServer
							.formatNameAusFLRPartner(flrauftrag
									.getFlrpersonalverrechenbar()
									.getFlrpartner());
				}

				data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_AUFTRAGKUNDE] = flrauftrag
						.getFlrkunde().getFlrpartner()
						.getC_name1nachnamefirmazeile1();
				if (flrauftrag.getFlrvertreter() != null) {
					if (flrauftrag.getFlrvertreter().getFlrpartner()
							.getC_name2vornamefirmazeile2() != null) {
						data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_AUFTRAGVERTRETER] = flrauftrag
								.getFlrvertreter().getFlrpartner()
								.getC_name1nachnamefirmazeile1()
								+ " "
								+ flrauftrag.getFlrvertreter().getFlrpartner()
										.getC_name2vornamefirmazeile2();
					} else {
						data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_AUFTRAGVERTRETER] = flrauftrag
								.getFlrvertreter().getFlrpartner()
								.getC_name1nachnamefirmazeile1();
					}
				}

				// Zahlungsziel zum Andrucken
				String sZahlungsziel = null;
				if (flrauftrag.getZahlungsziel_i_id() != null) {
					if (sZahlungsziel == null) {
						ZahlungszielDto oDto = getMandantFac()
								.zahlungszielFindByPrimaryKey(
										flrauftrag.getZahlungsziel_i_id(),
										theClientDto);

						sZahlungsziel = oDto.getCBez();
					}
				}
				// AUFTRAGSEIGENSCHAFTEN
				Hashtable<?, ?> hAE = getAuftragEigenschaften(
						flrauftrag.getI_id(), theClientDto);
				if (hAE != null) {
					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_AUFTRAGEIGENSCHAFT_FA] = hAE
							.get(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_FA);
					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_AUFTRAGEIGENSCHAFT_CLUSTER] = hAE
							.get(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_CLUSTER);
					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_AUFTRAGEIGENSCHAFT_EQNR] = hAE
							.get(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_EQNR);
				}
				AuftragDto auftragDto = getAuftragFac()
						.auftragFindByPrimaryKey(flrauftrag.getI_id());
				if (bInternenKommentarDrucken.booleanValue()) {
					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_INTERNERKOMMENTAR] = auftragDto
							.getXInternerkommentar();
					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_EXTERNERKOMMENTAR] = auftragDto
							.getXExternerkommentar();
				} else {
					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_INTERNERKOMMENTAR] = "";
					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_EXTERNERKOMMENTAR] = "";
				}

				data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_AUFTRAGZAHLUNGSZIEL] = sZahlungsziel;
				locDruck = Helper.string2Locale(flrauftrag.getFlrkunde()
						.getFlrpartner().getLocale_c_nr_kommunikation());
				data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_AUFTRAGLIEFERTERMIN] = Helper
						.formatDatum(flrauftrag.getT_liefertermin(), locDruck);
				data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_AUFTRAGFINALTERMIN] = Helper
						.formatDatum(flrauftrag.getT_finaltermin(), locDruck);
				data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_AUFTRAGPROJEKTBEZEICHNUNG] = flrauftrag
						.getC_bez();
				data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_AUFTRAGBESTELLNUMMER] = flrauftrag
						.getC_bestellnummer();

				if (flrauftrag.getFlrkostenstelle() != null) {
					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_KOSTENSTELLECNR] = flrauftrag
							.getFlrkostenstelle().getC_nr();
				}

				session = factory.openSession();
				Criteria crit1 = session
						.createCriteria(FLRAuftragpositionReport.class);
				crit1.add(Restrictions
						.ne(AuftragpositionFac.FLR_AUFTRAGPOSITION_AUFTRAGPOSITIONSTATUS_C_NR,
								AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT));
				crit1.createCriteria("flrauftrag").add(
						Restrictions.eq("i_id", flrauftrag.getI_id()));
				List<?> resultList = crit1.list();

				boolean darfVerkaufspreisSehen = getTheJudgeFac().hatRecht(
						RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF,
						theClientDto);

				String artikelCNr = null;
				if (item.getFlrartikel().getC_nr().startsWith("~")) {
					artikelCNr = AngebotReportFac.REPORT_VORKALKULATION_ZEICHEN_FUER_HANDEINGABE;
				} else {
					artikelCNr = item.getFlrartikel().getC_nr();
				}
				//
				data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_AUFTRAGCNR] = flrauftrag
						.getC_nr();
				data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_AUFTRAGKUNDE] = flrauftrag
						.getFlrkunde().getFlrpartner()
						.getC_name1nachnamefirmazeile1();
				if (flrauftrag.getFlrvertreter() != null) {
					if (flrauftrag.getFlrvertreter().getFlrpartner()
							.getC_name2vornamefirmazeile2() != null) {
						data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_AUFTRAGVERTRETER] = flrauftrag
								.getFlrvertreter().getFlrpartner()
								.getC_name1nachnamefirmazeile1()
								+ " "
								+ flrauftrag.getFlrvertreter().getFlrpartner()
										.getC_name2vornamefirmazeile2();
					} else {
						data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_AUFTRAGVERTRETER] = flrauftrag
								.getFlrvertreter().getFlrpartner()
								.getC_name1nachnamefirmazeile1();
					}
				}

				data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTIKELCNR] = artikelCNr;
				ArtikelDto oArtikelDto = getArtikelFac()
						.artikelFindByPrimaryKey(
								item.getFlrartikel().getI_id(), theClientDto);
				String cArtikelBezeichnung = "";
				if (item.getAuftragpositionart_c_nr().equals(
						AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)
						|| item.getAuftragpositionart_c_nr()
								.equals(AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE)) {
					cArtikelBezeichnung = oArtikelDto.getArtikelsprDto()
							.getCBez();
				}
				data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTIKELBEZEICHNUNG] = cArtikelBezeichnung;
				data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTIKELMENGE] = item
						.getN_menge();
				BigDecimal bdLagerstand = getLagerFac()
						.getLagerstandAllerLagerEinesMandanten(
								item.getFlrartikel().getI_id(), theClientDto);
				data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTIKELLAGERSTAND] = bdLagerstand;
				if (item.getT_uebersteuerterliefertermin() != null) {
					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONSTERMIN] = Helper
							.formatDatum(
									item.getT_uebersteuerterliefertermin(),
									locDruck);
				}
				data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTIKELEINHEIT] = item
						.getEinheit_c_nr() == null ? null : item
						.getEinheit_c_nr().trim();

				// Positionspreise sind in Belegwaehrung abgelegt
				BigDecimal nPreisInBelegwaehrung = item
						.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte();
				BigDecimal wechselkursmandantwaehrungzuauftragswaehrung = null;
				if (flrauftrag
						.getF_wechselkursmandantwaehrungzuauftragswaehrung()
						.doubleValue() != 0) {
					wechselkursmandantwaehrungzuauftragswaehrung = new BigDecimal(
							flrauftrag
									.getF_wechselkursmandantwaehrungzuauftragswaehrung()
									.doubleValue());
				} else {
					wechselkursmandantwaehrungzuauftragswaehrung = new BigDecimal(
							1);
				}
				nPreisInBelegwaehrung = getBetragMalWechselkurs(
						nPreisInBelegwaehrung,
						Helper.getKehrwert(wechselkursmandantwaehrungzuauftragswaehrung));
				if (darfVerkaufspreisSehen) {
					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTIKELNETTOGESAMTPREISPLUSVERSTECKTERAUFSCHLAGMINUSRABATTE] = nPreisInBelegwaehrung;
				} else {
					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTIKELNETTOGESAMTPREISPLUSVERSTECKTERAUFSCHLAGMINUSRABATTE] = null;
				}
				// Grundlage ist der Gestehungspreis des Artikels am
				// Hauptlager des Mandanten
				BigDecimal bdGestehungspreis = getLagerFac()
						.getGemittelterGestehungspreisDesHauptlagers(
								item.getArtikel_i_id(), theClientDto);

				if (bLieferantAngeben == true) {
					if (item.getN_einkaufpreis() != null) {
						bdGestehungspreis = item.getN_einkaufpreis();
					} else {
						ArtikellieferantDto alDto = getArtikelFac()
								.getArtikelEinkaufspreis(
										item.getArtikel_i_id(),
										null,
										item.getN_menge(),
										theClientDto.getSMandantenwaehrung(),
										new java.sql.Date(item.getFlrauftrag()
												.getT_belegdatum().getTime()),
										theClientDto);

						if (alDto != null && alDto.getNNettopreis() != null) {
							bdGestehungspreis = alDto.getNNettopreis();
						}
					}

				}

				if (darfVerkaufspreisSehen) {
					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTIKELGESTEHUNGSPREIS] = bdGestehungspreis;
					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_EINKAUFSPREIS] = item
							.getN_einkaufpreis();
					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTIKELOFFENERWERT] = item
							.getN_offenemenge().multiply(nPreisInBelegwaehrung);
					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTIKELOFFENERDB] = item
							.getN_offenemenge().multiply(
									nPreisInBelegwaehrung
											.subtract(bdGestehungspreis));
				} else {
					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTIKELGESTEHUNGSPREIS] = null;
					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_EINKAUFSPREIS] = null;
					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTIKELOFFENERWERT] = null;
					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTIKELOFFENERDB] = null;

				}
				data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTIKELGELIEFERTEMENGE] = item
						.getN_menge().subtract(item.getN_offenemenge());
				data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTIKELOFFENEMENGE] = item
						.getN_offenemenge();
				i++;

			}

			mapParameter.put("P_STICHTAG", dStichtag);

			mapParameter.put("P_MIT_ANGELEGTEN", new Boolean(bMitAngelegten));

			mapParameter.put(LPReport.P_SORTIERUNG,
					buildSortierungAuftragOffene(krit, theClientDto));
			mapParameter.put(LPReport.P_FILTER,
					buildFilterAuftragOffene(krit, theClientDto));

			mapParameter.put(LPReport.P_SORTIERENACHKOSTENSTELLE, new Boolean(
					krit.bSortiereNachKostenstelle));
			mapParameter
					.put(LPReport.P_SORTIERENACHKUNDE,
							new Boolean(
									krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER));
			mapParameter
					.put(LPReport.P_SORTIERENACHVERTRETER,
							new Boolean(
									krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_VERTRETER));

			mapParameter
					.put("P_TITLE",
							getTextRespectUISpr("bes.print.offene",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
			mapParameter.put(P_MANDANTWAEHRUNG,
					theClientDto.getSMandantenwaehrung());
			mapParameter.put("P_AUFTRAGWAEHRUNG",
					theClientDto.getSMandantenwaehrung());

			initJRDS(mapParameter, AuftragReportFac.REPORT_MODUL,
					AuftragReportFac.REPORT_AUFTRAG_OFFENE,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);

			oPrintO = getReportPrint();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return oPrintO;
	}

	// SP2709
	private ArrayList<AuftragNachkalkulationDto> addZeileLI(
			ArrayList<AuftragNachkalkulationDto> alDaten, LosDto losDto,
			AuftragDto auftragDto, LossollmaterialDto[] lossollmaterialDtos,
			java.sql.Timestamp tStichtag, TheClientDto theClientDto) {

		for (int k = 0; k < lossollmaterialDtos.length; k++) {
			LossollmaterialDto lossollmaterialDto = lossollmaterialDtos[k];

			if (lossollmaterialDto.getArtikelIId() != null) {

				StuecklisteDto stklDto = getStuecklisteFac()
						.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
								lossollmaterialDto.getArtikelIId(),
								theClientDto);

				if (stklDto != null) {

					try {
						LosistmaterialDto[] istmatDtos = getFertigungFac()
								.losistmaterialFindByLossollmaterialIId(
										lossollmaterialDto.getIId());

						for (int i = 0; i < istmatDtos.length; i++) {

							LosistmaterialDto istmatDto = istmatDtos[i];

							if (istmatDto.getNMenge().doubleValue() != 0) {

								// Ursprung holen

								Session session = FLRSessionFactory
										.getFactory().openSession();

								String sQuery = "select distinct lagerbewegung.i_id_buchung from FLRLagerbewegung lagerbewegung WHERE lagerbewegung.c_belegartnr='Los' AND lagerbewegung.i_belegartpositionid="
										+ istmatDto.getIId();

								// SP2943
								if (tStichtag != null) {
									sQuery += " AND lagerbewegung.t_buchungszeit<='"
											+ Helper.formatTimestampWithSlashes(tStichtag)
											+ "'";
								}

								Query inventurliste = session
										.createQuery(sQuery);
								List<?> resultList = inventurliste.list();
								Iterator<?> resultListIterator = resultList
										.iterator();
								while (resultListIterator.hasNext()) {
									Integer o = (Integer) resultListIterator
											.next();

									alDaten.addAll(holeUrsprungslose(losDto,
											auftragDto, theClientDto, o));
								}
								session.close();

							}
						}

					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

				}

			}

		}

		return alDaten;
	}

	private ArrayList<AuftragNachkalkulationDto> holeUrsprungslose(
			LosDto losDto, AuftragDto auftragDto, TheClientDto theClientDto,
			Integer o) throws RemoteException {
		LagerabgangursprungDto[] dtos = getLagerFac()
				.lagerabgangursprungFindByLagerbewegungIIdBuchung(o);

		ArrayList<AuftragNachkalkulationDto> alDaten = new ArrayList<AuftragNachkalkulationDto>();

		// Fuer jeden Lagerabgangs- Ursprung, der
		// aus einem
		// Los
		// kommt, einen zusaetzlichen eintrag
		// anlegen
		for (int j = 0; j < dtos.length; j++) {
			// aber nur wenn verbrauchte menge
			// grosser 0
			LagerabgangursprungDto dto = dtos[j];
			if (dto.getNVerbrauchtemenge().doubleValue() != 0) {

				Session session2 = FLRSessionFactory.getFactory().openSession();
				String sQuery2 = "from FLRLagerbewegung lagerbewegung WHERE lagerbewegung.i_id_buchung="
						+ dto.getILagerbewegungidursprung()
						+ " AND lagerbewegung.b_historie=0  order by lagerbewegung.t_buchungszeit DESC";
				Query ursrungsbuchung = session2.createQuery(sQuery2);
				ursrungsbuchung.setMaxResults(1);

				List<?> resultList2 = ursrungsbuchung.list();

				com.lp.server.artikel.fastlanereader.generated.FLRLagerbewegung lagerbewegung_ursprung = (com.lp.server.artikel.fastlanereader.generated.FLRLagerbewegung) resultList2
						.iterator().next();

				if (lagerbewegung_ursprung.getC_belegartnr().equals(
						LocaleFac.BELEGART_HAND)) {
					HandlagerbewegungDto handlagerbewegungDto = getLagerFac()
							.getZugehoerigeUmbuchung(
									lagerbewegung_ursprung
											.getI_belegartpositionid(),
									theClientDto);

					if (handlagerbewegungDto != null
							&& Helper.short2boolean(handlagerbewegungDto
									.getBAbgang())) {

						String cSnrChnr = null;

						if (handlagerbewegungDto.getSeriennrChargennrMitMenge() != null
								&& handlagerbewegungDto
										.getSeriennrChargennrMitMenge().size() > 0) {
							cSnrChnr = handlagerbewegungDto
									.getSeriennrChargennrMitMenge().get(0)
									.getCSeriennrChargennr();
						}

						LagerbewegungDto lbewDto = getLagerFac()
								.getLetzteintrag(LocaleFac.BELEGART_HAND,
										handlagerbewegungDto.getIId(), cSnrChnr);

						alDaten.addAll(holeUrsprungslose(losDto, auftragDto,
								theClientDto, lbewDto.getIIdBuchung()));

						continue;
					}

				}

				if (lagerbewegung_ursprung.getC_belegartnr().equals(
						LocaleFac.BELEGART_LOSABLIEFERUNG)) {

					LosablieferungDto losablieferungDto = getFertigungFac()
							.losablieferungFindByPrimaryKeyOhneExc(
									lagerbewegung_ursprung
											.getI_belegartpositionid(),
									true, theClientDto);

					if (losablieferungDto != null
							&& losablieferungDto.getNGestehungspreis() != null) {

						// Nur wenn aus demselben
						// Auftrag

						LosDto losDto_Ursprung = getFertigungFac()
								.losFindByPrimaryKey(
										losablieferungDto.getLosIId());

						Integer auftragIIdUrsprung = losDto_Ursprung
								.getAuftragIId();

						if (auftragIIdUrsprung == null) {

							if (losDto_Ursprung.getAuftragpositionIId() != null) {

								AuftragpositionDto apDtoUrsprung = getAuftragpositionFac()
										.auftragpositionFindByPrimaryKey(
												losDto_Ursprung
														.getAuftragpositionIId());
								auftragIIdUrsprung = apDtoUrsprung
										.getBelegIId();

							}

						}

						if (auftragIIdUrsprung != null
								&& auftragDto.getIId().equals(
										auftragIIdUrsprung)) {

							BigDecimal bdAblieferwertAusDemselbenAuftrag = losablieferungDto
									.getNGestehungspreis()
									.multiply(
											lagerbewegung_ursprung.getN_menge())
									.multiply(new BigDecimal(-1));

							AuftragNachkalkulationDto oNachkalkulationDto = new AuftragNachkalkulationDto(
									auftragDto);

							oNachkalkulationDto.setSBelegart("Li");
							oNachkalkulationDto
									.setSBelegnummer(losDto.getCNr());
							oNachkalkulationDto.setSBelegstatus(losDto
									.getStatusCNr());
							oNachkalkulationDto
									.setLosnummerLiQuelle(losDto_Ursprung
											.getCNr());
							oNachkalkulationDto
									.setBdGestehungswertmaterialist(bdAblieferwertAusDemselbenAuftrag);

							alDaten.add(oNachkalkulationDto);

						}

					}
				}
				session2.close();
			}
		}
		return alDaten;
	}

	private ArrayList<AuftragNachkalkulationDto> getDataAuftragNachkalkulation(
			Integer iIdAuftragI, boolean bArbeitszeitVerdichtet,
			java.sql.Timestamp tStichtag, TheClientDto theClientDto) {
		checkAuftragIId(iIdAuftragI);
		ArrayList<AuftragNachkalkulationDto> alAuftragNachkalkulationDtos = new ArrayList<AuftragNachkalkulationDto>();
		try {
			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(
					iIdAuftragI);
			// alle Ergebniszeilen aufbauen; die Anzahl der Zeilen im Ergebnis
			// wird im Zuge des Aufbaus bestimmt

			ParametermandantDto parametermandantDto = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FERTIGUNG,
							ParameterFac.PARAMETER_ISTZEITEN_GLEICH_SOLLZEITEN);
			boolean bSollGleichIstzeiten = false;

			if (((java.lang.Boolean) parametermandantDto.getCWertAsObject())
					.booleanValue() == true) {
				bSollGleichIstzeiten = true;
			}

			parametermandantDto = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_ALLE_LOSE_BERUECKSICHTIGEN);
			boolean bAlleLoseberuecksichtigen = false;

			if (((java.lang.Boolean) parametermandantDto.getCWertAsObject())
					.booleanValue() == true) {
				bAlleLoseberuecksichtigen = true;
			}

			parametermandantDto = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_AUFTRAG,
							ParameterFac.PARAMETER_AB_NACHKALKULATION_NUR_RECHNUNGS_ERLOESE);
			boolean bNurRechnungserloese = false;

			if (((java.lang.Boolean) parametermandantDto.getCWertAsObject())
					.booleanValue() == true) {
				bNurRechnungserloese = true;
			}

			AuftragNachkalkulationDto oNachkalkulationDto = null;

			// eine Zeile pro Eingangsrechnung zu diesem Auftrag
			EingangsrechnungAuftragszuordnungDto[] aEingangsrechnungDtos = getEingangsrechnungFac()
					.eingangsrechnungAuftragszuordnungFindByAuftragIId(
							auftragDto.getIId());

			for (int i = 0; i < aEingangsrechnungDtos.length; i++) {
				EingangsrechnungDto oEingangsrechnungDto = getEingangsrechnungFac()
						.eingangsrechnungFindByPrimaryKey(
								aEingangsrechnungDtos[i]
										.getEingangsrechnungIId());

				oNachkalkulationDto = new AuftragNachkalkulationDto(auftragDto);

				BelegartDto oBelegartDto = getLocaleFac().belegartFindByCNr(
						LocaleFac.BELEGART_EINGANGSRECHNUNG);

				oNachkalkulationDto.setSBelegart(oBelegartDto
						.getCKurzbezeichnung());
				oNachkalkulationDto
						.setSEingangsgrechnungsart(oEingangsrechnungDto
								.getEingangsrechnungartCNr());

				// SP3281
				if (oEingangsrechnungDto.getEingangsrechnungartCNr().equals(
						EingangsrechnungFac.EINGANGSRECHNUNGART_ANZAHLUNG)
						&& oEingangsrechnungDto.getBestellungIId() != null) {

					EingangsrechnungDto[] erDtos = getEingangsrechnungFac()
							.eingangsrechnungFindByBestellungIId(
									oEingangsrechnungDto.getBestellungIId());
					for (EingangsrechnungDto erDto : erDtos) {
						if (!erDto.getStatusCNr().equals(
								EingangsrechnungFac.STATUS_STORNIERT)
								&& erDto.getEingangsrechnungartCNr()
										.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_SCHLUSSZAHLUNG)) {
							oNachkalkulationDto.setSErSchlussrechnungNr(erDto
									.getCNr());
						}
					}

				}

				oNachkalkulationDto.setBKeineAuftragwertung(Helper
						.short2boolean(aEingangsrechnungDtos[i]
								.getBKeineAuftragswertung()));
				oNachkalkulationDto.setSBelegnummer(oEingangsrechnungDto
						.getCNr());
				oNachkalkulationDto.setSBelegstatus(oEingangsrechnungDto
						.getStatusCNr());

				LieferantDto oLieferantDto = getLieferantFac()
						.lieferantFindByPrimaryKey(
								oEingangsrechnungDto.getLieferantIId(),
								theClientDto);

				oNachkalkulationDto.setSBelegkundename(oLieferantDto
						.getPartnerDto().formatTitelAnrede());

				oNachkalkulationDto.setErLieferant(oLieferantDto
						.getPartnerDto().formatTitelAnrede());

				// Betrag auf Mandantenwaehrung normieren
				Double ddWechselkurs = getEingangsrechnungFac()
						.getWechselkursEingangsrechnungswaehrungZuMandantwaehrung(
								aEingangsrechnungDtos[i]
										.getEingangsrechnungIId(),
								theClientDto);
				BigDecimal nPreisinmandantwaehrung = aEingangsrechnungDtos[i]
						.getNBetrag().multiply(
								new BigDecimal(ddWechselkurs.doubleValue()));
				nPreisinmandantwaehrung = Helper.rundeKaufmaennisch(
						nPreisinmandantwaehrung, 4);
				checkNumberFormat(nPreisinmandantwaehrung);

				oNachkalkulationDto
						.setBdGestehungswertmaterialist(nPreisinmandantwaehrung);
				oNachkalkulationDto
						.setSEingangsrechnungtext(aEingangsrechnungDtos[i]
								.getCText());

				// Wenn die Eingangsrechnung nach dem Stichtag hinzugefuegt
				// wurde, dann zaehlt sie nicht
				if (tStichtag == null
						|| tStichtag.getTime() >= oEingangsrechnungDto
								.getDBelegdatum().getTime()) {
					alAuftragNachkalkulationDtos.add(oNachkalkulationDto);
				}

			}

			// eine Zeile pro Rechnung zu diesem Auftrag
			ArrayList<RechnungDto> aRechnungDtos = new ArrayList<RechnungDto>();
			RechnungDto[] reDtos = getRechnungFac().rechnungFindByAuftragIId(
					auftragDto.getIId());
			for (int i = 0; i < reDtos.length; i++) {
				aRechnungDtos.add(reDtos[i]);
			}

			// eine Zeile pro Lieferschein zu diesem Auftrag
			LieferscheinDto[] aLieferscheinDtos = getLieferscheinFac()
					.lieferscheinFindByAuftrag(auftragDto.getIId(),
							theClientDto);

			for (int i = 0; i < aLieferscheinDtos.length; i++) {

				if (tStichtag == null
						|| tStichtag.getTime() >= aLieferscheinDtos[i]
								.getTBelegdatum().getTime()) {
					if (aLieferscheinDtos[i].getRechnungIId() != null
							&& !aLieferscheinDtos[i].getStatusCNr().equals(
									LieferscheinFac.LSSTATUS_STORNIERT)) {

						// SP2846
						if (bAlleLoseberuecksichtigen) {
							// Pruefen, ob in dem Lieferschein ev. noch ein
							// andere Auftrag vorhanden ist, wenn ja, dann
							// Fehler

							Session sessionLspos = FLRSessionFactory
									.getFactory().openSession();
							String sQueryLspos = "SELECT lspos.flrpositionensichtauftrag.flrauftrag.i_id from FLRLieferscheinposition lspos WHERE lspos.flrlieferschein="
									+ aLieferscheinDtos[i].getIId()
									+ " AND auftragposition_i_id IS NOT NULL";
							Query queryLspos = sessionLspos
									.createQuery(sQueryLspos);

							List<?> resultListLspos = queryLspos.list();

							Iterator it = resultListLspos.iterator();
							while (it.hasNext()) {
								Integer auftragIIdLieferschein = (Integer) it
										.next();

								if (!auftragIIdLieferschein.equals(iIdAuftragI)) {
									// Fehler
									ArrayList al = new ArrayList();
									al.add(aLieferscheinDtos[i].getCNr());
									throw new EJBExceptionLP(
											EJBExceptionLP.FEHLER_ALLE_LOSE_BERUECKSICHTIGEN_UND_SAMMELLIEFERSCHIEN_MEHRERE_AUFTRAEGE,
											al,
											new Exception(
													"FEHLER_ALLE_LOSE_BERUECKSICHTIGEN_UND_SAMMELLIEFERSCHIEN_MEHRERE_AUFTRAEGE: "
															+ aLieferscheinDtos[i]
																	.getCNr()));

								}
							}

						}

						RechnungDto reDto = getRechnungFac()
								.rechnungFindByPrimaryKey(
										aLieferscheinDtos[i].getRechnungIId());

						// SP 990 Nur hinzufuegen, wenn noch nicht vorhanden
						boolean bBereitsVorhanden = false;

						for (int j = 0; j < aRechnungDtos.size(); j++) {
							if (aRechnungDtos.get(j).getIId()
									.equals(reDto.getIId())) {
								bBereitsVorhanden = true;
								break;
							}
						}

						if (bBereitsVorhanden == false) {
							aRechnungDtos.add(reDto);
						}
					}
				}
			}

			for (int i = 0; i < aRechnungDtos.size(); i++) {
				// stornierte/angelegte nicht
				if (!aRechnungDtos.get(i).getStatusCNr()
						.equals(RechnungFac.STATUS_STORNIERT)
						&& !aRechnungDtos.get(i).getStatusCNr()
								.equals(RechnungFac.STATUS_ANGELEGT)) {

					oNachkalkulationDto = new AuftragNachkalkulationDto(
							auftragDto);

					BelegartDto oBelegartDto = getLocaleFac()
							.belegartFindByCNr(LocaleFac.BELEGART_RECHNUNG);

					oNachkalkulationDto.setSBelegart(oBelegartDto
							.getCKurzbezeichnung());
					oNachkalkulationDto.setSBelegnummer(aRechnungDtos.get(i)
							.getCNr());
					oNachkalkulationDto.setSBelegstatus(aRechnungDtos.get(i)
							.getStatusCNr());

					KundeDto kundeDtoDto = getKundeFac().kundeFindByPrimaryKey(
							aRechnungDtos.get(i).getKundeIId(), theClientDto);

					oNachkalkulationDto.setSBelegkundename(kundeDtoDto
							.getPartnerDto().formatTitelAnrede());
					oNachkalkulationDto.setReBestellnummer(aRechnungDtos.get(i)
							.getCBestellnummer());

					oNachkalkulationDto.setSRechnungsart(aRechnungDtos.get(i)
							.getRechnungartCNr());

					oNachkalkulationDto.setBdZahlbetrag(getRechnungFac()
							.getBereitsBezahltWertVonRechnung(
									aRechnungDtos.get(i).getIId(), null,
									tStichtag));

					BigDecimal bdTmpWert = null;

					// IMS 986: Wenn ein Lieferschein mit einer Rechnung
					// verrechnet wurde,
					// dann muss der Wechselkurs der Rechnung verwendet werden.
					// Ansonsten gilt
					// der Wechselkurs des Lieferscheins.
					BigDecimal bdWechselkursZuMandantenwaehrung = null;
					RechnungDto rechnungDto = getRechnungFac()
							.rechnungFindByPrimaryKey(
									aRechnungDtos.get(i).getIId());

					if (rechnungDto.getIId() != null) {
						bdWechselkursZuMandantenwaehrung = new BigDecimal(
								getWechselkursRechnungswaehrungZuMandantwaehrung(rechnungDto
										.getIId()));
					} else {
						bdWechselkursZuMandantenwaehrung = Helper
								.getKehrwert(aRechnungDtos.get(i).getNKurs());
					}

					// Verkaufswert Arbeit Ist in Lieferscheinwaehrung
					bdTmpWert = getRechnungFac().berechneVerkaufswertIst(
							aRechnungDtos.get(i).getIId(),
							ArtikelFac.ARTIKELART_ARBEITSZEIT, theClientDto);

					// Betrag auf Mandantenwaehrung normieren
					bdTmpWert = getBetragMalWechselkurs(bdTmpWert,
							bdWechselkursZuMandantenwaehrung);
					bdTmpWert = Helper.rundeKaufmaennisch(bdTmpWert, 4);
					checkNumberFormat(bdTmpWert);

					oNachkalkulationDto.setBdVkwArbeitist(bdTmpWert);

					// Verkaufswert Material Ist in Lieferscheinwaehrung
					bdTmpWert = getRechnungFac().berechneVerkaufswertIst(
							aRechnungDtos.get(i).getIId(),
							ArtikelFac.ARTIKELART_ARTIKEL, theClientDto);

					// Betrag auf Mandantenwaehrung normieren
					bdTmpWert = bdTmpWert
							.multiply(bdWechselkursZuMandantenwaehrung);
					bdTmpWert = Helper.rundeKaufmaennisch(bdTmpWert, 4);
					checkNumberFormat(bdTmpWert);

					oNachkalkulationDto.setBdVkwMaterialist(bdTmpWert);

					// Gestehungswert Material Ist in Mandantenwaehrung
					bdTmpWert = getRechnungFac().berechneGestehungswertIst(
							aRechnungDtos.get(i).getIId(),
							ArtikelFac.ARTIKELART_ARTIKEL, theClientDto);

					// Betrag auf Mandantenwaehrung normieren
					bdTmpWert = bdTmpWert
							.multiply(bdWechselkursZuMandantenwaehrung);
					bdTmpWert = Helper.rundeKaufmaennisch(bdTmpWert, 4);
					checkNumberFormat(bdTmpWert);

					oNachkalkulationDto
							.setBdGestehungswertmaterialist(bdTmpWert);

					if (tStichtag == null
							|| tStichtag.getTime() >= aRechnungDtos.get(i)
									.getTBelegdatum().getTime()) {

						alAuftragNachkalkulationDtos.add(oNachkalkulationDto);

						// CK: Pro Position, die aus einem Los kommt, noch eine
						// Zeile einfuegen
						BigDecimal bdLosanteilInRechnung = new BigDecimal(0);
						boolean bEsGibtLoseInDerRechnung = false;
						Session session = FLRSessionFactory.getFactory()
								.openSession();
						String sQuery = "select distinct lagerbewegung.i_id_buchung from FLRLagerbewegung lagerbewegung WHERE lagerbewegung.c_belegartnr='Rechnung' AND lagerbewegung.i_belegartid="
								+ aRechnungDtos.get(i).getIId();
						Query inventurliste = session.createQuery(sQuery);
						List<?> resultList = inventurliste.list();
						Iterator<?> resultListIterator = resultList.iterator();
						while (resultListIterator.hasNext()) {
							Integer o = (Integer) resultListIterator.next();
							LagerabgangursprungDto[] dtos = getLagerFac()
									.lagerabgangursprungFindByLagerbewegungIIdBuchung(
											o);

							// Fuer jeden Lagerabgangs- Ursprung, der aus einem
							// Los
							// kommt, einen zusaetzlichen eintrag anlegen
							for (int j = 0; j < dtos.length; j++) {
								// aber nur wenn verbrauchte menge grosser 0
								LagerabgangursprungDto dto = dtos[j];
								if (dto.getNVerbrauchtemenge().doubleValue() != 0) {
									Session session2 = FLRSessionFactory
											.getFactory().openSession();
									String sQuery2 = "from FLRLagerbewegung lagerbewegung WHERE lagerbewegung.i_id_buchung="
											+ dto.getILagerbewegungidursprung()
											+ " AND lagerbewegung.b_historie=0  order by lagerbewegung.t_buchungszeit DESC";
									Query ursrungsbuchung = session2
											.createQuery(sQuery2);
									ursrungsbuchung.setMaxResults(1);

									List<?> resultList2 = ursrungsbuchung
											.list();

									com.lp.server.artikel.fastlanereader.generated.FLRLagerbewegung lagerbewegung_ursprung = (com.lp.server.artikel.fastlanereader.generated.FLRLagerbewegung) resultList2
											.iterator().next();

									if (lagerbewegung_ursprung
											.getC_belegartnr()
											.equals(LocaleFac.BELEGART_LOSABLIEFERUNG)) {
										bEsGibtLoseInDerRechnung = true;
										LosablieferungDto losablieferungDto = getFertigungFac()
												.losablieferungFindByPrimaryKey(
														lagerbewegung_ursprung
																.getI_belegartpositionid(),
														true, theClientDto);

										LosDto losDto = getFertigungFac()
												.losFindByPrimaryKey(
														lagerbewegung_ursprung
																.getI_belegartid());

										// Neuer Eintrag
										oNachkalkulationDto = new AuftragNachkalkulationDto(
												auftragDto);

										BelegartDto belegartDto = getLocaleFac()
												.belegartFindByCNr(
														LocaleFac.BELEGART_LOSABLIEFERUNG);

										oNachkalkulationDto
												.setSBelegart(belegartDto
														.getCKurzbezeichnung());
										oNachkalkulationDto
												.setSBelegnummer(losDto
														.getCNr());
										oNachkalkulationDto
												.setSBelegstatus(losDto
														.getStatusCNr());

										oNachkalkulationDto
												.setSLosprojekt(losDto
														.getCProjekt());

										if (losDto.getTManuellerledigt() != null) {
											oNachkalkulationDto
													.settLoserledigungsdatum(losDto
															.getTManuellerledigt());

										} else {
											oNachkalkulationDto
													.settLoserledigungsdatum(losDto
															.getTErledigt());

										}

										oNachkalkulationDto
												.setdLosbewertung(losDto
														.getFBewertung());

										oNachkalkulationDto
												.setSLoskommentar(losDto
														.getCKommentar());

										if (losDto.getStuecklisteIId() != null) {

											StuecklisteDto stklDto = getStuecklisteFac()
													.stuecklisteFindByPrimaryKey(
															losDto.getStuecklisteIId(),
															theClientDto);
											oNachkalkulationDto
													.setSLosstuecklistenartikelnummer(stklDto
															.getArtikelDto()
															.getCNr());
											if (stklDto.getArtikelDto()
													.getArtikelsprDto() != null) {
												oNachkalkulationDto
														.setSLosstuecklistenartikelbezeichnung(stklDto
																.getArtikelDto()
																.getArtikelsprDto()
																.getCBez());
												oNachkalkulationDto
														.setSLosstuecklistenartikelzusatzbezeichnung(stklDto
																.getArtikelDto()
																.getArtikelsprDto()
																.getCZbez());
											}
										}

										BigDecimal gestWertArbeitIst = losablieferungDto
												.getNArbeitszeitwertdetailliert()
												.multiply(
														lagerbewegung_ursprung
																.getN_menge());

										oNachkalkulationDto
												.setBdGestehungswertarbeitist(gestWertArbeitIst);

										BigDecimal gestWertMaterialIst = losablieferungDto
												.getNMaterialwertdetailliert()
												.multiply(
														dto.getNVerbrauchtemenge());

										oNachkalkulationDto
												.setBdGestehungswertmaterialist(gestWertMaterialIst);

										if (bAlleLoseberuecksichtigen) {

											if (losDto.getAuftragIId() != null
													&& !losDto
															.getAuftragIId()
															.equals(iIdAuftragI)) {
												bdLosanteilInRechnung = bdLosanteilInRechnung
														.add(gestWertArbeitIst)
														.add(gestWertMaterialIst);
											}
										} else {
											bdLosanteilInRechnung = bdLosanteilInRechnung
													.add(gestWertArbeitIst)
													.add(gestWertMaterialIst);
										}

										/*
										 * LossollmaterialDto[]
										 * lossollmaterialDtos =
										 * getFertigungFac()
										 * .lossollmaterialFindByLosIId(
										 * lagerbewegung_ursprung
										 * .getI_belegartid()); BigDecimal
										 * sollmaterial = new BigDecimal( 0);
										 * for (int k = 0; k <
										 * lossollmaterialDtos.length; k++) {
										 * LossollmaterialDto lossollmaterialDto
										 * = lossollmaterialDtos[k];
										 * 
										 * BigDecimal temp = lossollmaterialDto
										 * .getNSollpreis() .multiply(
										 * dto.getNVerbrauchtemenge())
										 * .multiply( lagerbewegung_ursprung
										 * .getN_menge()); if
										 * (losDto.getNLosgroesse()
										 * .doubleValue() != 0) { temp = temp
										 * .divide(losDto .getNLosgroesse(),
										 * BigDecimal.ROUND_HALF_UP);
										 * sollmaterial = sollmaterial
										 * .add(temp); } } oNachkalkulationDto
										 * .setBdGestehungswertmaterialsoll
										 * (sollmaterial);
										 */

										// Arbeitszeit soll
										LossollarbeitsplanDto[] sollarbeitsplanDtos = getFertigungFac()
												.lossollarbeitsplanFindByLosIId(
														losDto.getIId());
										BigDecimal arbeitszeitsoll = new BigDecimal(
												0);
										BigDecimal arbeitswertsoll = new BigDecimal(
												0);
										BigDecimal maschinenzeitsoll = new BigDecimal(
												0);

										for (int k = 0; k < sollarbeitsplanDtos.length; k++) {
											LossollarbeitsplanDto lossollarbeitsplanDto = sollarbeitsplanDtos[k];
											if (losDto.getNLosgroesse()
													.doubleValue() != 0) {
												BigDecimal menge = lossollarbeitsplanDto
														.getNGesamtzeit()
														.divide(losDto
																.getNLosgroesse(),
																BigDecimal.ROUND_HALF_EVEN)
														.multiply(
																dto.getNVerbrauchtemenge());
												if (lossollarbeitsplanDto
														.getMaschineIId() == null) {
													arbeitszeitsoll = arbeitszeitsoll
															.add(menge);
												} else {
													maschinenzeitsoll = maschinenzeitsoll
															.add(menge);
												}

												ArtikellieferantDto artikelieferantDto = getArtikelFac()
														.getArtikelEinkaufspreis(
																lossollarbeitsplanDto
																		.getArtikelIIdTaetigkeit(),
																menge,
																theClientDto
																		.getSMandantenwaehrung(),
																theClientDto);
												if (artikelieferantDto != null
														&& artikelieferantDto
																.getLief1Preis() != null) {
													ReportLosnachkalkulationDto[] zeit = getFertigungReportFac()
															.getDataNachkalkulationZeitdaten(
																	losDto.getIId(),
																	theClientDto);
													for (int l = 0; l < zeit.length; l++) {
														ReportLosnachkalkulationDto dtoTemp = zeit[l];
														if (dtoTemp
																.getBdSollmenge() != null) {
															BigDecimal bdTeilwert = dtoTemp
																	.getBdSollmenge()
																	.multiply(
																			dto.getNVerbrauchtemenge())
																	.divide(losDto
																			.getNLosgroesse(),
																			4);
															arbeitswertsoll = arbeitswertsoll
																	.add(bdTeilwert
																			.multiply(artikelieferantDto
																					.getLief1Preis()));
														}
													}
												}
											}
										}

										oNachkalkulationDto
												.setDdArbeitszeitsoll(new Double(
														arbeitszeitsoll
																.doubleValue()));

										oNachkalkulationDto
												.setDdMaschinenzeitsoll(new Double(
														maschinenzeitsoll
																.doubleValue()));
										/*
										 * oNachkalkulationDto
										 * .setBdGestehungswertarbeitsoll
										 * (arbeitswertsoll);
										 */

										if (bSollGleichIstzeiten == false) {
											BigDecimal gesamtAbgeliefert = getFertigungFac()
													.getErledigteMenge(
															losDto.getIId(),
															theClientDto);

											// IST-Arbeitszeit
											Double arbeitszeitIst = getZeiterfassungFac()
													.getSummeZeitenEinesBeleges(
															LocaleFac.BELEGART_LOS,
															losDto.getIId(),
															null, null, null,
															tStichtag,
															theClientDto);

											arbeitszeitIst = arbeitszeitIst
													/ gesamtAbgeliefert
															.doubleValue()
													* dto.getNVerbrauchtemenge()
															.doubleValue();

											oNachkalkulationDto
													.setDdArbeitszeitist(arbeitszeitIst);

											// IST-Maschinenzeit
											Double maschinenzeitist = getZeiterfassungFac()
													.getSummeMaschinenZeitenEinesBeleges(
															losDto.getIId(),
															null, tStichtag,
															theClientDto);

											maschinenzeitist = maschinenzeitist
													/ gesamtAbgeliefert
															.doubleValue()
													* dto.getNVerbrauchtemenge()
															.doubleValue();
											oNachkalkulationDto
													.setDdMaschinenzeitist(maschinenzeitist);
										} else {
											oNachkalkulationDto
													.setDdMaschinenzeitist(maschinenzeitsoll
															.doubleValue());
											oNachkalkulationDto
													.setDdArbeitszeitist(arbeitszeitsoll
															.doubleValue());
										}

										// SP2709
										if (bAlleLoseberuecksichtigen == true) {
											oNachkalkulationDto
													.setBdGestehungswertmaterialist(oNachkalkulationDto
															.getBdGestehungswertmaterialist()
															.negate());
											oNachkalkulationDto
													.setBdGestehungswertarbeitist(oNachkalkulationDto
															.getBdGestehungswertarbeitist()
															.negate());
											// SP2943
											if (oNachkalkulationDto
													.getBdGestehungswertmaterialsoll() != null) {
												oNachkalkulationDto
														.setBdGestehungswertmaterialsoll(oNachkalkulationDto
																.getBdGestehungswertmaterialsoll()
																.negate());
											}
											if (oNachkalkulationDto
													.getBdGestehungswertarbeitsoll() != null) {
												oNachkalkulationDto
														.setBdGestehungswertarbeitsoll(oNachkalkulationDto
																.getBdGestehungswertarbeitsoll()
																.negate());
											}
											oNachkalkulationDto
													.setDdArbeitszeitist(0D);
											oNachkalkulationDto
													.setDdArbeitszeitsoll(0D);
											oNachkalkulationDto
													.setDdMaschinenzeitist(0D);
											oNachkalkulationDto
													.setDdMaschinenzeitsoll(0D);
										}

										alAuftragNachkalkulationDtos
												.add(oNachkalkulationDto);
									}
								}
							}

						}

						if (bEsGibtLoseInDerRechnung == true) {
							AuftragNachkalkulationDto losanteil = new AuftragNachkalkulationDto(
									auftragDto);
							losanteil.setSBelegart("ZZ");
							losanteil.setSBelegnummer(aRechnungDtos.get(i)
									.getCNr());
							losanteil.setSBelegart(LocaleFac.BELEGART_RECHNUNG);
							losanteil.setSBelegstatus(aRechnungDtos.get(i)
									.getStatusCNr());

							losanteil
									.setBdGestehungswertmaterialist(bdLosanteilInRechnung
											.multiply(new BigDecimal(-1)));
							String sTemp = getTextRespectUISpr(
									"auft.nachkalkulation.losanteil.re",
									theClientDto.getMandant(),
									theClientDto.getLocUi());
							losanteil.setSBelegkundename(sTemp + " "
									+ aRechnungDtos.get(i).getCNr());

							alAuftragNachkalkulationDtos.add(losanteil);
						}

						session.close();

					}
					// PJ 16987 Gutschriften
					RechnungDto[] aGutschriftenDtos = null;
					javax.persistence.Query query = em
							.createNamedQuery("RechnungfindByRechnungIIdZuRechnung");
					query.setParameter(1, aRechnungDtos.get(i).getIId());

					Collection<?> cl = query.getResultList();
					aGutschriftenDtos = RechnungDtoAssembler.createDtos(cl);

					for (int k = 0; k < aGutschriftenDtos.length; k++) {

						AuftragNachkalkulationDto oNachkalkulationDtoGS = new AuftragNachkalkulationDto(
								auftragDto);

						if (aGutschriftenDtos[k].getNWert() != null) {
							oNachkalkulationDtoGS.setSBelegart("GS");
							oNachkalkulationDtoGS
									.setSBelegnummer(aGutschriftenDtos[k]
											.getCNr());
							oNachkalkulationDtoGS
									.setSBelegstatus(aGutschriftenDtos[k]
											.getStatusCNr());
							oNachkalkulationDtoGS
									.setBdVkwMaterialist(aGutschriftenDtos[k]
											.getNWert().negate());

							// PJ18843
							if (aRechnungDtos.get(i).getRechnungartCNr()
									.equals(RechnungFac.RECHNUNGART_ANZAHLUNG)) {
								oNachkalkulationDtoGS
										.setSRechnungsart(RechnungFac.RECHNUNGART_ANZAHLUNG);
							}

							BigDecimal bdZahlbetrag = getRechnungFac()
									.getBereitsBezahltWertVonRechnung(
											aGutschriftenDtos[k].getIId(),
											null, tStichtag);
							if (bdZahlbetrag != null) {
								oNachkalkulationDtoGS
										.setBdZahlbetrag(bdZahlbetrag.negate());
							}

							alAuftragNachkalkulationDtos
									.add(oNachkalkulationDtoGS);
						}
					}
				}
			}

			// eine Zeile pro Materialliste, welche mit dem Auftrag verknuepft
			// ist
			LosDto[] losDtos = getFertigungFac().losFindByAuftragIId(
					auftragDto.getIId());

			BelegartDto oBelegartDtoLos = getLocaleFac().belegartFindByCNr(
					LocaleFac.BELEGART_LOS);

			for (int i = 0; i < losDtos.length; i++) {

				if (bAlleLoseberuecksichtigen == true
						|| losDtos[i].getStuecklisteIId() == null) {

					LosDto losDto = losDtos[i];

					oNachkalkulationDto = new AuftragNachkalkulationDto(
							auftragDto);
					oNachkalkulationDto.setSBelegart(oBelegartDtoLos
							.getCKurzbezeichnung());
					oNachkalkulationDto.setSBelegnummer(losDtos[i].getCNr());
					oNachkalkulationDto.setSBelegstatus(losDtos[i]
							.getStatusCNr());
					oNachkalkulationDto
							.setSLosprojekt(losDtos[i].getCProjekt());

					if (losDtos[i].getTManuellerledigt() != null) {
						oNachkalkulationDto.settLoserledigungsdatum(losDtos[i]
								.getTManuellerledigt());

					} else {
						oNachkalkulationDto.settLoserledigungsdatum(losDtos[i]
								.getTErledigt());

					}

					oNachkalkulationDto.setdLosbewertung(losDtos[i]
							.getFBewertung());

					oNachkalkulationDto.setSLoskommentar(losDtos[i]
							.getCKommentar());

					if (losDtos[i].getStuecklisteIId() != null) {

						StuecklisteDto stklDto = getStuecklisteFac()
								.stuecklisteFindByPrimaryKey(
										losDtos[i].getStuecklisteIId(),
										theClientDto);
						oNachkalkulationDto
								.setSLosstuecklistenartikelnummer(stklDto
										.getArtikelDto().getCNr());
						if (stklDto.getArtikelDto().getArtikelsprDto() != null) {
							oNachkalkulationDto
									.setSLosstuecklistenartikelbezeichnung(stklDto
											.getArtikelDto().getArtikelsprDto()
											.getCBez());
							oNachkalkulationDto
									.setSLosstuecklistenartikelzusatzbezeichnung(stklDto
											.getArtikelDto().getArtikelsprDto()
											.getCZbez());
						}
					}

					// Sollzeiten
					// Arbeitszeit soll
					LossollarbeitsplanDto[] sollarbeitsplanDtos = getFertigungFac()
							.lossollarbeitsplanFindByLosIId(losDto.getIId());
					BigDecimal arbeitszeitsoll = new BigDecimal(0);
					BigDecimal arbeitswertsoll = new BigDecimal(0);
					BigDecimal maschinenzeitsoll = new BigDecimal(0);

					for (int k = 0; k < sollarbeitsplanDtos.length; k++) {
						LossollarbeitsplanDto lossollarbeitsplanDto = sollarbeitsplanDtos[k];
						if (losDto.getNLosgroesse().doubleValue() != 0) {
							BigDecimal menge = lossollarbeitsplanDto
									.getNGesamtzeit();
							if (Helper.short2boolean(lossollarbeitsplanDto
									.getBNurmaschinenzeit())) {
								maschinenzeitsoll = maschinenzeitsoll
										.add(menge);
							} else {

								arbeitszeitsoll = arbeitszeitsoll.add(menge);
								if (lossollarbeitsplanDto.getMaschineIId() != null) {
									maschinenzeitsoll = maschinenzeitsoll
											.add(menge);
								}
							}

							ArtikellieferantDto artikelieferantDto = getArtikelFac()
									.getArtikelEinkaufspreis(
											lossollarbeitsplanDto
													.getArtikelIIdTaetigkeit(),
											menge,
											theClientDto
													.getSMandantenwaehrung(),
											theClientDto);
							if (artikelieferantDto != null
									&& artikelieferantDto.getLief1Preis() != null) {

								arbeitswertsoll = arbeitswertsoll.add(menge
										.multiply(artikelieferantDto
												.getLief1Preis()));

							}
						}
					}

					oNachkalkulationDto.setDdArbeitszeitsoll(new Double(
							arbeitszeitsoll.doubleValue()));

					oNachkalkulationDto.setDdMaschinenzeitsoll(new Double(
							maschinenzeitsoll.doubleValue()));
					oNachkalkulationDto
							.setBdGestehungswertarbeitsoll(arbeitswertsoll);

					if (bSollGleichIstzeiten == false) {
						// IST-Arbeitszeit
						BigDecimal gestWertArbeitIst = new BigDecimal(0);

						AuftragzeitenDto[] dtos = getZeiterfassungFac()
								.getAllZeitenEinesBeleges(
										LocaleFac.BELEGART_LOS,
										losDto.getIId(), null, null, null,
										tStichtag, false, false, theClientDto);

						double zeiten = 0;
						for (int j = 0; j < dtos.length; j++) {
							if (dtos[j] != null && dtos[j].getDdDauer() != null) {
								zeiten = zeiten
										+ dtos[j].getDdDauer().doubleValue();
								gestWertArbeitIst = gestWertArbeitIst
										.add(dtos[j].getBdKosten());
							}
						}
						oNachkalkulationDto.setDdArbeitszeitist(zeiten);

						// IST-Maschinenzeit
						AuftragzeitenDto[] mzDtos = getZeiterfassungFac()
								.getAllMaschinenzeitenEinesBeleges(
										losDto.getIId(), null, null, tStichtag,
										theClientDto);
						Double maschinenzeitist = new Double(0);
						for (int mz = 0; mz < mzDtos.length; mz++) {
							AuftragzeitenDto mzDto = mzDtos[mz];
							maschinenzeitist += mzDto.getDdDauer();
							gestWertArbeitIst = gestWertArbeitIst.add(mzDto
									.getBdKosten());
						}

						oNachkalkulationDto
								.setBdGestehungswertarbeitist(gestWertArbeitIst);

						oNachkalkulationDto
								.setDdMaschinenzeitist(maschinenzeitist);

					} else {
						oNachkalkulationDto
								.setDdMaschinenzeitist(maschinenzeitsoll
										.doubleValue());
						oNachkalkulationDto.setDdArbeitszeitist(arbeitszeitsoll
								.doubleValue());
					}

					if (losDto.getIId() == 3379) {
						int f = 0;
					}

					// Material
					BigDecimal gestWertMaterialIst = new BigDecimal(0);

					LossollmaterialDto[] lossollmaterialDtos = getFertigungFac()
							.lossollmaterialFindByLosIId(losDto.getIId());
					BigDecimal sollmaterial = new BigDecimal(0);

					for (int k = 0; k < lossollmaterialDtos.length; k++) {
						LossollmaterialDto lossollmaterialDto = lossollmaterialDtos[k];

						if (losDto.getNLosgroesse().doubleValue() != 0) {

							sollmaterial = sollmaterial.add(lossollmaterialDto
									.getNSollpreis().multiply(
											lossollmaterialDto.getNMenge()));

							gestWertMaterialIst = gestWertMaterialIst
									.add(getFertigungFac()
											.getAusgegebeneMengePreis(
													lossollmaterialDto.getIId(),
													null, theClientDto)
											.multiply(
													getFertigungFac()
															.getAusgegebeneMenge(
																	lossollmaterialDto
																			.getIId(),
																	tStichtag,
																	theClientDto)));

						}
					}

					// PJ 16249
					if (losDto.getNSollmaterial() != null) {
						oNachkalkulationDto
								.setBdGestehungswertmaterialsoll(losDto
										.getNSollmaterial());
					} else {

						oNachkalkulationDto
								.setBdGestehungswertmaterialsoll(sollmaterial);
					}

					oNachkalkulationDto
							.setBdGestehungswertmaterialist(gestWertMaterialIst);

					alAuftragNachkalkulationDtos.add(oNachkalkulationDto);

					// SP2709

					addZeileLI(alAuftragNachkalkulationDtos, losDto,
							auftragDto, lossollmaterialDtos, tStichtag,
							theClientDto);

				}
			}

			// eine Zeile pro Lieferschein zu diesem Auftrag

			for (int i = 0; i < aLieferscheinDtos.length; i++) {

				if (tStichtag == null
						|| tStichtag.getTime() >= aLieferscheinDtos[i]
								.getTBelegdatum().getTime()) {

					boolean bSammellieferschein = false;
					if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_SAMMELLIEFERSCHEIN,
							theClientDto)) {
						bSammellieferschein = true;
					}
					HashMap lsPosIds = null;

					if (bSammellieferschein) {

						lsPosIds = getLieferscheinpositionFac()
								.lieferscheinpositionFindByLieferscheinIIdAuftragIId(
										aLieferscheinDtos[i].getIId(),
										auftragDto.getIId(), theClientDto);
					}

					oNachkalkulationDto = new AuftragNachkalkulationDto(
							auftragDto);

					BelegartDto oBelegartDto = getLocaleFac()
							.belegartFindByCNr(LocaleFac.BELEGART_LIEFERSCHEIN);

					oNachkalkulationDto.setSBelegart(oBelegartDto
							.getCKurzbezeichnung());
					oNachkalkulationDto.setSBelegnummer(aLieferscheinDtos[i]
							.getCNr());
					oNachkalkulationDto.setSBelegstatus(aLieferscheinDtos[i]
							.getStatusCNr());
					if (aLieferscheinDtos[i].getRechnungIId() != null) {

						oNachkalkulationDto
								.setSRechnungsnummerLSVerrechnet(getRechnungFac()
										.rechnungFindByPrimaryKey(
												aLieferscheinDtos[i]
														.getRechnungIId())
										.getCNr());
					}

					KundeDto kundeDtoDto = getKundeFac().kundeFindByPrimaryKey(
							aLieferscheinDtos[i].getKundeIIdLieferadresse(),
							theClientDto);

					oNachkalkulationDto.setSBelegkundename(kundeDtoDto
							.getPartnerDto().formatTitelAnrede());

					BigDecimal bdTmpWert = null;

					// IMS 986: Wenn ein Lieferschein mit einer Rechnung
					// verrechnet
					// wurde,
					// dann muss der Wechselkurs der Rechnung verwendet werden.
					// Ansonsten gilt
					// der Wechselkurs des Lieferscheins.
					BigDecimal bdWechselkursZuMandantenwaehrung = null;
					LieferscheinDto lieferscheinDto = getLieferscheinFac()
							.lieferscheinFindByPrimaryKey(
									aLieferscheinDtos[i].getIId(), theClientDto);

					if (lieferscheinDto.getRechnungIId() != null) {
						bdWechselkursZuMandantenwaehrung = new BigDecimal(
								getWechselkursRechnungswaehrungZuMandantwaehrung(lieferscheinDto
										.getRechnungIId()));
					} else {
						bdWechselkursZuMandantenwaehrung = Helper
								.getKehrwert(new BigDecimal(
										aLieferscheinDtos[i]
												.getFWechselkursmandantwaehrungzubelegwaehrung()
												.doubleValue()));
					}

					// Verkaufswert Arbeit Ist in Lieferscheinwaehrung
					bdTmpWert = getLieferscheinFac().berechneVerkaufswertIst(
							aLieferscheinDtos[i].getIId(), lsPosIds,
							ArtikelFac.ARTIKELART_ARBEITSZEIT, theClientDto);

					// Betrag auf Mandantenwaehrung normieren
					bdTmpWert = getBetragMalWechselkurs(bdTmpWert,
							bdWechselkursZuMandantenwaehrung);
					bdTmpWert = Helper.rundeKaufmaennisch(bdTmpWert, 4);
					checkNumberFormat(bdTmpWert);

					oNachkalkulationDto.setBdVkwArbeitist(bdTmpWert);

					// Verkaufswert Material Ist in Lieferscheinwaehrung
					bdTmpWert = getLieferscheinFac().berechneVerkaufswertIst(
							aLieferscheinDtos[i].getIId(), lsPosIds,
							ArtikelFac.ARTIKELART_ARTIKEL, theClientDto);

					// Betrag auf Mandantenwaehrung normieren
					bdTmpWert = bdTmpWert
							.multiply(bdWechselkursZuMandantenwaehrung);
					bdTmpWert = Helper.rundeKaufmaennisch(bdTmpWert, 4);
					checkNumberFormat(bdTmpWert);

					oNachkalkulationDto.setBdVkwMaterialist(bdTmpWert);

					// SP2808
					if (Helper.short2boolean(aLieferscheinDtos[i]
							.getBVerrechenbar()) == false) {
						// Wenn nicht verrechenbar, dann vkwerte 0

						// Aber nur, wenn er wirklich nicht verrechenet ist
						if (aLieferscheinDtos[i].getRechnungIId() == null) {

							oNachkalkulationDto
									.setBdVkwArbeitist(BigDecimal.ZERO);
							oNachkalkulationDto
									.setBdVkwMaterialist(BigDecimal.ZERO);
						}
					}

					// PJ18675
					if (bNurRechnungserloese == true
							&& aLieferscheinDtos[i].getRechnungIId() == null) {
						oNachkalkulationDto.setBdVkwArbeitist(BigDecimal.ZERO);
						oNachkalkulationDto
								.setBdVkwMaterialist(BigDecimal.ZERO);
					}

					// Gestehungswert Material Ist in Mandantenwaehrung, daher
					// braucht nicht umgerechnet werden
					bdTmpWert = getLieferscheinFac().berechneGestehungswertIst(
							aLieferscheinDtos[i].getIId(), lsPosIds,
							ArtikelFac.ARTIKELART_ARTIKEL, theClientDto);
					bdTmpWert = Helper.rundeKaufmaennisch(bdTmpWert, 4);
					checkNumberFormat(bdTmpWert);

					oNachkalkulationDto
							.setBdGestehungswertmaterialist(bdTmpWert);

					alAuftragNachkalkulationDtos.add(oNachkalkulationDto);

					// CK: Pro Position, die aus einem Los kommt, noch eine
					// Zeile
					// einfuegen
					BigDecimal bdLosanteilImLieferschein = new BigDecimal(0);
					Session session = FLRSessionFactory.getFactory()
							.openSession();
					String sQuery = "select distinct lagerbewegung.i_id_buchung from FLRLagerbewegung lagerbewegung WHERE lagerbewegung.c_belegartnr='"
							+ LocaleFac.BELEGART_LIEFERSCHEIN
							+ "' AND lagerbewegung.i_belegartid="
							+ aLieferscheinDtos[i].getIId();

					if (bSammellieferschein) {
						if (lsPosIds != null && lsPosIds.size() > 0) {
							String lsPos = " AND lagerbewegung.i_belegartpositionid IN (";

							Iterator it = lsPosIds.keySet().iterator();
							while (it.hasNext()) {
								lsPos += it.next() + "";

								if (it.hasNext() == true) {
									lsPos += ",";
								}

							}
							lsPos += ")";
							sQuery += lsPos;
						}
					}

					Query inventurliste = session.createQuery(sQuery);
					List<?> resultList = inventurliste.list();
					Iterator<?> resultListIterator = resultList.iterator();
					while (resultListIterator.hasNext()) {
						Integer o = (Integer) resultListIterator.next();
						LagerabgangursprungDto[] dtos = getLagerFac()
								.lagerabgangursprungFindByLagerbewegungIIdBuchung(
										o);

						// Fuer jeden Lagerabgangs- Ursprung, der aus einem Los
						// kommt, einen zusaetzlichen eintrag anlegen
						for (int j = 0; j < dtos.length; j++) {
							// aber nur wenn verbrauchte menge grosser 0
							LagerabgangursprungDto dto = dtos[j];
							if (dto.getNVerbrauchtemenge().doubleValue() != 0) {
								Session session2 = FLRSessionFactory
										.getFactory().openSession();
								String sQuery2 = "from FLRLagerbewegung lagerbewegung WHERE lagerbewegung.i_id_buchung="
										+ dtos[j].getILagerbewegungidursprung()
										+ " AND lagerbewegung.b_historie=0 order by lagerbewegung.t_buchungszeit DESC";
								Query ursrungsbuchung = session2
										.createQuery(sQuery2);
								ursrungsbuchung.setMaxResults(1);

								List<?> resultList2 = ursrungsbuchung.list();

								com.lp.server.artikel.fastlanereader.generated.FLRLagerbewegung lagerbewegung_ursprung = (com.lp.server.artikel.fastlanereader.generated.FLRLagerbewegung) resultList2
										.iterator().next();

								if (lagerbewegung_ursprung
										.getC_belegartnr()
										.equals(LocaleFac.BELEGART_LOSABLIEFERUNG)) {

									LosablieferungDto losablieferungDto = getFertigungFac()
											.losablieferungFindByPrimaryKey(
													lagerbewegung_ursprung
															.getI_belegartpositionid(),
													true, theClientDto);

									LosDto losDto = getFertigungFac()
											.losFindByPrimaryKey(
													lagerbewegung_ursprung
															.getI_belegartid());
									// Neuer Eintrag
									oNachkalkulationDto = new AuftragNachkalkulationDto(
											auftragDto);

									BelegartDto belegartDto = getLocaleFac()
											.belegartFindByCNr(
													LocaleFac.BELEGART_LOSABLIEFERUNG);

									oNachkalkulationDto
											.setSBelegart(belegartDto
													.getCKurzbezeichnung());
									oNachkalkulationDto.setSBelegnummer(losDto
											.getCNr());
									oNachkalkulationDto.setSBelegstatus(losDto
											.getStatusCNr());

									oNachkalkulationDto.setSLosprojekt(losDto
											.getCProjekt());

									oNachkalkulationDto.setSLosprojekt(losDto
											.getCProjekt());

									if (losDto.getTManuellerledigt() != null) {
										oNachkalkulationDto
												.settLoserledigungsdatum(losDto
														.getTManuellerledigt());

									} else {
										oNachkalkulationDto
												.settLoserledigungsdatum(losDto
														.getTErledigt());

									}

									oNachkalkulationDto.setSLoskommentar(losDto
											.getCKommentar());

									if (losDto.getStuecklisteIId() != null) {

										StuecklisteDto stklDto = getStuecklisteFac()
												.stuecklisteFindByPrimaryKey(
														losDto.getStuecklisteIId(),
														theClientDto);
										oNachkalkulationDto
												.setSLosstuecklistenartikelnummer(stklDto
														.getArtikelDto()
														.getCNr());
										if (stklDto.getArtikelDto()
												.getArtikelsprDto() != null) {
											oNachkalkulationDto
													.setSLosstuecklistenartikelbezeichnung(stklDto
															.getArtikelDto()
															.getArtikelsprDto()
															.getCBez());
											oNachkalkulationDto
													.setSLosstuecklistenartikelzusatzbezeichnung(stklDto
															.getArtikelDto()
															.getArtikelsprDto()
															.getCZbez());
										}
									}

									BigDecimal gestWertArbeitIst = losablieferungDto
											.getNArbeitszeitwertdetailliert()
											.multiply(
													dto.getNVerbrauchtemenge());

									oNachkalkulationDto
											.setBdGestehungswertarbeitist(gestWertArbeitIst);

									BigDecimal gestWertMaterialIst = losablieferungDto
											.getNMaterialwertdetailliert()
											.multiply(
													dto.getNVerbrauchtemenge());

									oNachkalkulationDto
											.setBdGestehungswertmaterialist(gestWertMaterialIst);

									// SP2808
									if (bAlleLoseberuecksichtigen) {

										if (losDto.getAuftragIId() != null
												&& !losDto.getAuftragIId()
														.equals(iIdAuftragI)) {

											bdLosanteilImLieferschein = bdLosanteilImLieferschein
													.add(gestWertArbeitIst)
													.add(gestWertMaterialIst);
										}

									} else {
										bdLosanteilImLieferschein = bdLosanteilImLieferschein
												.add(gestWertArbeitIst).add(
														gestWertMaterialIst);
									}

									LossollmaterialDto[] lossollmaterialDtos = getFertigungFac()
											.lossollmaterialFindByLosIId(
													lagerbewegung_ursprung
															.getI_belegartid());
									BigDecimal sollmaterial = new BigDecimal(0);
									for (int k = 0; k < lossollmaterialDtos.length; k++) {
										LossollmaterialDto lossollmaterialDto = lossollmaterialDtos[k];

										// SP778 WH:Der Wert muss ueber die
										// Sollsatzgroesse gerechnet werden
										if (losDto.getNLosgroesse()
												.doubleValue() != 0) {
											sollmaterial = sollmaterial
													.add(lossollmaterialDto
															.getNMenge()
															.divide(losDto
																	.getNLosgroesse(),
																	BigDecimal.ROUND_HALF_UP)
															.multiply(
																	dto.getNVerbrauchtemenge())
															.multiply(
																	lossollmaterialDto
																			.getNSollpreis()));
										}
									}
									oNachkalkulationDto
											.setBdGestehungswertmaterialsoll(sollmaterial);

									// Arbeitszeit soll
									LossollarbeitsplanDto[] sollarbeitsplanDtos = getFertigungFac()
											.lossollarbeitsplanFindByLosIId(
													losDto.getIId());
									BigDecimal arbeitszeitsoll = new BigDecimal(
											0);
									BigDecimal arbeitswertsoll = new BigDecimal(
											0);
									BigDecimal maschinenzeitsoll = new BigDecimal(
											0);

									for (int k = 0; k < sollarbeitsplanDtos.length; k++) {
										LossollarbeitsplanDto lossollarbeitsplanDto = sollarbeitsplanDtos[k];
										if (losDto.getNLosgroesse()
												.doubleValue() != 0) {
											// = Sollsatzgroesse x
											// Lieferscheinmenge = Anteilige
											// Arbeitszeit
											BigDecimal menge = lossollarbeitsplanDto
													.getNGesamtzeit()
													.divide(losDto
															.getNLosgroesse(),
															BigDecimal.ROUND_HALF_EVEN)
													.multiply(
															dto.getNVerbrauchtemenge());
											if (lossollarbeitsplanDto
													.getMaschineIId() == null) {
												arbeitszeitsoll = arbeitszeitsoll
														.add(menge);
											} else {
												maschinenzeitsoll = maschinenzeitsoll
														.add(menge);

												if (!Helper
														.short2boolean(lossollarbeitsplanDto
																.getBNurmaschinenzeit())) {
													arbeitszeitsoll = arbeitszeitsoll
															.add(menge);
												}

											}

											ArtikellieferantDto artikelieferantDto = getArtikelFac()
													.getArtikelEinkaufspreis(
															lossollarbeitsplanDto
																	.getArtikelIIdTaetigkeit(),
															menge,
															theClientDto
																	.getSMandantenwaehrung(),
															theClientDto);
											if (artikelieferantDto != null
													&& artikelieferantDto
															.getLief1Preis() != null) {
												ReportLosnachkalkulationDto[] zeit = getFertigungReportFac()
														.getDataNachkalkulationZeitdaten(
																losDto.getIId(),
																theClientDto);
												for (int l = 0; l < zeit.length; l++) {
													ReportLosnachkalkulationDto dtoTemp = zeit[l];
													if (dtoTemp
															.getBdSollmenge() != null) {
														BigDecimal bdTeilwert = dtoTemp
																.getBdSollmenge()
																.multiply(
																		dto.getNVerbrauchtemenge())
																.divide(losDto
																		.getNLosgroesse(),
																		4);
														arbeitswertsoll = arbeitswertsoll
																.add(bdTeilwert
																		.multiply(artikelieferantDto
																				.getLief1Preis()));
													}
												}
											}
										}
									}

									oNachkalkulationDto
											.setDdArbeitszeitsoll(new Double(
													arbeitszeitsoll
															.doubleValue()));

									oNachkalkulationDto
											.setDdMaschinenzeitsoll(new Double(
													maschinenzeitsoll
															.doubleValue()));
									/*
									 * oNachkalkulationDto
									 * .setBdGestehungswertarbeitsoll
									 * (arbeitswertsoll);
									 */

									BigDecimal gesamtAbgeliefert = getFertigungFac()
											.getErledigteMenge(losDto.getIId(),
													theClientDto);

									// IST-Arbeitszeit
									Double arbeitszeitIst = getZeiterfassungFac()
											.getSummeZeitenEinesBeleges(
													LocaleFac.BELEGART_LOS,
													losDto.getIId(), null,
													null, null, tStichtag,
													theClientDto);

									arbeitszeitIst = arbeitszeitIst
											/ gesamtAbgeliefert.doubleValue()
											* dto.getNVerbrauchtemenge()
													.doubleValue();

									oNachkalkulationDto
											.setDdArbeitszeitist(arbeitszeitIst);

									// IST-Maschinenzeit
									Double maschinenzeitist = getZeiterfassungFac()
											.getSummeMaschinenZeitenEinesBeleges(
													losDto.getIId(), null,
													tStichtag, theClientDto);

									maschinenzeitist = maschinenzeitist
											/ gesamtAbgeliefert.doubleValue()
											* dto.getNVerbrauchtemenge()
													.doubleValue();
									oNachkalkulationDto
											.setDdMaschinenzeitist(maschinenzeitist);

									oNachkalkulationDto = getFertigungFac()
											.getWerteAusUnterlosen(losDto,
													dto.getNVerbrauchtemenge(),
													oNachkalkulationDto,
													theClientDto);

									// SP2709
									if (bAlleLoseberuecksichtigen == true) {
										oNachkalkulationDto
												.setBdGestehungswertmaterialist(oNachkalkulationDto
														.getBdGestehungswertmaterialist()
														.negate());
										oNachkalkulationDto
												.setBdGestehungswertarbeitist(oNachkalkulationDto
														.getBdGestehungswertarbeitist()
														.negate());
										// SP2943
										if (oNachkalkulationDto
												.getBdGestehungswertmaterialsoll() != null) {
											oNachkalkulationDto
													.setBdGestehungswertmaterialsoll(oNachkalkulationDto
															.getBdGestehungswertmaterialsoll()
															.negate());
										}
										if (oNachkalkulationDto
												.getBdGestehungswertarbeitsoll() != null) {
											oNachkalkulationDto
													.setBdGestehungswertarbeitsoll(oNachkalkulationDto
															.getBdGestehungswertarbeitsoll()
															.negate());
										}
										oNachkalkulationDto
												.setDdArbeitszeitist(0D);
										oNachkalkulationDto
												.setDdArbeitszeitsoll(0D);
										oNachkalkulationDto
												.setDdMaschinenzeitist(0D);
										oNachkalkulationDto
												.setDdMaschinenzeitsoll(0D);
									}

									alAuftragNachkalkulationDtos
											.add(oNachkalkulationDto);
								}
							}
						}

					}

					if (resultList.size() > 0) {

						if (bdLosanteilImLieferschein != null
								&& bdLosanteilImLieferschein.doubleValue() != 0) {

							AuftragNachkalkulationDto losanteil = new AuftragNachkalkulationDto(
									auftragDto);

							losanteil.setSBelegart("ZZ");
							losanteil.setSBelegnummer(aLieferscheinDtos[i]
									.getCNr());

							losanteil.setSBelegstatus(aLieferscheinDtos[i]
									.getStatusCNr());

							losanteil
									.setBdGestehungswertmaterialist(bdLosanteilImLieferschein
											.multiply(new BigDecimal(-1)));

							String sTemp = getTextRespectUISpr(
									"auft.nachkalkulation.losanteil",
									theClientDto.getMandant(),
									theClientDto.getLocUi());
							losanteil.setSBelegkundename(sTemp + " "
									+ aLieferscheinDtos[i].getCNr());

							alAuftragNachkalkulationDtos.add(losanteil);
						}
					}

					session.close();

				}
			}

			// eine Zeile pro Auftrag zur Darstellung der Auftragsdaten
			oNachkalkulationDto = new AuftragNachkalkulationDto(auftragDto);

			BelegartDto oBelegartDto = getLocaleFac().belegartFindByCNr(
					LocaleFac.BELEGART_AUFTRAG);

			oNachkalkulationDto
					.setSBelegart(oBelegartDto.getCKurzbezeichnung());
			oNachkalkulationDto.setSBelegnummer(auftragDto.getCNr());
			oNachkalkulationDto.setSBelegstatus(auftragDto.getStatusCNr());

			KundeDto kundeDtoDto = getKundeFac().kundeFindByPrimaryKey(
					auftragDto.getKundeIIdAuftragsadresse(), theClientDto);

			oNachkalkulationDto.setSBelegkundename(kundeDtoDto.getPartnerDto()
					.formatTitelAnrede());

			// Verkaufswert Arbeit Soll in Mandantenwaehrung
			BigDecimal nTmpWert = getAuftragFac().berechneVerkaufswertSoll(
					iIdAuftragI, ArtikelFac.ARTIKELART_ARBEITSZEIT,
					theClientDto);

			BigDecimal ddWechselkursAuftragswaehrungZuMandantwaehrung = Helper
					.getKehrwert(new BigDecimal(auftragDto
							.getFWechselkursmandantwaehrungzubelegwaehrung()
							.doubleValue()));

			nTmpWert = nTmpWert
					.multiply(ddWechselkursAuftragswaehrungZuMandantwaehrung);
			nTmpWert = Helper.rundeKaufmaennisch(nTmpWert, 4);
			checkNumberFormat(nTmpWert);

			oNachkalkulationDto.setBdVkwArbeitsoll(nTmpWert);

			// Verkaufswert Material Soll in Mandantenwaehrung
			nTmpWert = getAuftragFac().berechneVerkaufswertSoll(iIdAuftragI,
					ArtikelFac.ARTIKELART_ARTIKEL, theClientDto);

			nTmpWert = nTmpWert
					.multiply(ddWechselkursAuftragswaehrungZuMandantwaehrung);
			nTmpWert = Helper.rundeKaufmaennisch(nTmpWert, 4);
			checkNumberFormat(nTmpWert);

			oNachkalkulationDto.setBdVkwMaterialsoll(nTmpWert);

			// Gestehungswert Arbeit Soll in Mandantenwaehrung

			nTmpWert = getAuftragFac().berechneGestehungswertSoll(
					auftragDto.getIId(), ArtikelFac.ARTIKELART_ARBEITSZEIT,
					false, theClientDto);

			oNachkalkulationDto.setBdGestehungswertarbeitsoll(nTmpWert);

			// Gestehungswert Material Soll in Mandantenwaehrung

			nTmpWert = getAuftragFac().berechneGestehungswertSoll(
					auftragDto.getIId(), ArtikelFac.ARTIKELART_ARTIKEL, false,
					theClientDto);

			oNachkalkulationDto.setBdGestehungswertmaterialsoll(nTmpWert);

			AuftragzeitenDto[] aAuftragzeitenDtos = getZeiterfassungFac()
					.getAllZeitenEinesBeleges(LocaleFac.BELEGART_AUFTRAG,
							auftragDto.getIId(), null, null, null, tStichtag,
							false, // order
							// by
							// artikelcnr
							true, // order by personal
							theClientDto);

			// den tatsaechlichen Gestehungswert der Arbeit berechnen
			BigDecimal bdGestehungswertArbeitIst = Helper.getBigDecimalNull();

			for (int i = 0; i < aAuftragzeitenDtos.length; i++) {
				BigDecimal bdGestehungskosten = aAuftragzeitenDtos[i]
						.getBdKosten();

				if (bdGestehungskosten != null) {
					bdGestehungswertArbeitIst = bdGestehungswertArbeitIst
							.add(bdGestehungskosten);
				}
			}

			oNachkalkulationDto
					.setBdGestehungswertarbeitist(bdGestehungswertArbeitIst);

			if (bArbeitszeitVerdichtet == true) {
				// Arbeitszeit Soll aus dem Auftrag
				Double ddArbeitszeitsoll = getAuftragpositionFac()
						.berechneArbeitszeitSoll(auftragDto.getIId(),
								theClientDto);

				oNachkalkulationDto.setDdArbeitszeitsoll(ddArbeitszeitsoll);

				// Arbeitszeit Ist aus den erfassten Auftragzeiten
				Double ddArbeitszeitist = getZeiterfassungFac()
						.getSummeZeitenEinesBeleges(LocaleFac.BELEGART_AUFTRAG,
								auftragDto.getIId(), null, null, null,
								tStichtag, theClientDto);

				oNachkalkulationDto.setDdArbeitszeitist(ddArbeitszeitist);

				alAuftragNachkalkulationDtos.add(oNachkalkulationDto);

			} else {

				// -----------------

				Session session = FLRSessionFactory.getFactory().openSession();
				String sQuery = "select artikel_i_id,n_menge from FLRAuftragpositionReport auftragposition WHERE auftragposition.artikel_i_id IS NOT NULL AND auftragposition.flrartikel."
						+ ArtikelFac.FLR_ARTIKEL_ARTIKELART_C_NR
						+ "='"
						+ ArtikelFac.ARTIKELART_ARBEITSZEIT
						+ "' AND auftragposition.flrauftrag.i_id="
						+ auftragDto.getIId();
				Query azpos = session.createQuery(sQuery);
				List<?> resultList = azpos.list();
				Iterator<?> resultListIterator = resultList.iterator();

				HashMap<Object, Object[]> artikelZeiten = new HashMap<Object, Object[]>();

				while (resultListIterator.hasNext()) {

					Object[] o = (Object[]) resultListIterator.next();
					Integer artikelIId = (Integer) o[0];

					// TEMP Spalte 1 = SOLLZEIT
					// Spalte2 ist IST-Zeit
					// Spalte3 ist Kosten
					Object[] oTemp = new Object[3];
					oTemp[0] = o[1];
					oTemp[1] = new Double(0);
					oTemp[2] = new BigDecimal(0);

					artikelZeiten.put(artikelIId, oTemp);
				}
				session.close();

				AuftragzeitenDto[] auftragzeitenDtos = getZeiterfassungFac()
						.getAllZeitenEinesBeleges(LocaleFac.BELEGART_AUFTRAG,
								auftragDto.getIId(), null, null, null,
								tStichtag, true, false, theClientDto);

				for (int i = 0; i < auftragzeitenDtos.length; i++) {
					AuftragzeitenDto auftZeit = auftragzeitenDtos[i];
					if (artikelZeiten.containsKey(auftZeit.getArtikelIId()) == true) {
						Object[] oTemp = (Object[]) artikelZeiten.get(auftZeit
								.getArtikelIId());

						Double dIst = ((Double) oTemp[1])
								+ auftZeit.getDdDauer();
						oTemp[1] = dIst;

						BigDecimal bdKosten = ((BigDecimal) oTemp[2])
								.add(auftZeit.getBdKosten());
						oTemp[2] = bdKosten;

						artikelZeiten.put(auftZeit.getArtikelIId(), oTemp);
					} else {
						Object[] oTemp = new Object[3];
						oTemp[0] = new BigDecimal(0);
						oTemp[1] = auftZeit.getDdDauer();
						oTemp[2] = auftZeit.getBdKosten();

						artikelZeiten.put(auftZeit.getArtikelIId(), oTemp);
					}
				}

				Iterator<?> artikelZeitenIterator = artikelZeiten.keySet()
						.iterator();

				if (artikelZeitenIterator.hasNext() == false) {
					alAuftragNachkalkulationDtos.add(oNachkalkulationDto);
				} else {

					while (artikelZeitenIterator.hasNext()) {
						Integer key = (Integer) artikelZeitenIterator.next();

						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKey(key, theClientDto);

						Object[] oTemp = (Object[]) artikelZeiten.get(key);
						BigDecimal soll = (BigDecimal) oTemp[0];
						oNachkalkulationDto.setDdArbeitszeitsoll(new Double(
								soll.doubleValue()));

						oNachkalkulationDto
								.setCArtikelnummerArbeitszeit(artikelDto
										.getCNr());
						oNachkalkulationDto
								.setCArtikelbezeichnungArbeitszeit(artikelDto
										.formatBezeichnung());

						oNachkalkulationDto
								.setDdArbeitszeitist((Double) oTemp[1]);

						oNachkalkulationDto
								.setBdGestehungswertarbeitist((BigDecimal) oTemp[2]);

						alAuftragNachkalkulationDtos.add(oNachkalkulationDto);
						oNachkalkulationDto = new AuftragNachkalkulationDto(
								auftragDto);
						oNachkalkulationDto.setSBelegart(oBelegartDto
								.getCKurzbezeichnung());
						oNachkalkulationDto
								.setSBelegnummer(auftragDto.getCNr());
						oNachkalkulationDto.setSBelegstatus(auftragDto
								.getStatusCNr());
					}
				}
			}

			// PJ18286
			ArrayList<ReiseKomplettDto> alReisen = getZeiterfassungFac()
					.holeReisenKomplett(LocaleFac.BELEGART_AUFTRAG,
							iIdAuftragI, null, null, theClientDto);

			for (int k = 0; k < alReisen.size(); k++) {

				ReiseKomplettDto rkDto = alReisen.get(k);

				Iterator it = rkDto.getTmReiseBeginn().keySet().iterator();
				ReiseDto rDtoErstesBeginn = null;
				while (it.hasNext()) {
					ReiseDto rDto = (ReiseDto) rkDto.getTmReiseBeginn().get(
							it.next());
					// Kosten

					if (rDtoErstesBeginn == null) {
						rDtoErstesBeginn = rDto;
					}

					if ((rDto.getBelegartCNr() != null && rDto.getBelegartCNr()
							.equals(LocaleFac.BELEGART_AUFTRAG))
							&& rDto.getIBelegartid() != null
							&& rDto.getIBelegartid().equals(iIdAuftragI)) {

						BigDecimal kmKostenKomplett = getZeiterfassungFac()
								.getKmKostenEinerReise(rkDto, theClientDto);

						Timestamp tBis = rkDto.getReiseEnde().getTZeit();
						if (it.hasNext()) {

							Iterator itNaechster = rkDto.getTmReiseBeginn()
									.keySet().iterator();
							while (itNaechster.hasNext()) {
								ReiseDto rDtoNaechster = (ReiseDto) rkDto
										.getTmReiseBeginn().get(
												itNaechster.next());
								if (rDtoNaechster.getIId()
										.equals(rDto.getIId())) {
									ReiseDto temp = (ReiseDto) rkDto
											.getTmReiseBeginn().get(
													itNaechster.next());
									tBis = temp.getTZeit();
								}
							}

						}

						// ///////////////////////////////////////////////////

						// Daten fuer JRuby Script
						String personalart = getPersonalFac()
								.personalFindByPrimaryKey(
										rDto.getPersonalIId(), theClientDto)
								.getPersonalartCNr().trim();

						BigDecimal bdDiaeten = getZeiterfassungFac()
								.berechneDiaetenAusScript(rDto.getDiaetenIId(),
										rDto.getTZeit(), tBis, theClientDto,
										personalart);

						// ///////////////////////////////////////////////////

						// BigDecimal bdDiaeten = getZeiterfassungFac()
						// .berechneDiaeten(rDto.getDiaetenIId(),
						// rDto.getTZeit(), tBis, theClientDto);

						BigDecimal kostenDesProjekts = rkDto
								.getAnteiligeKostenEinesAbschnitts(
										rDto.getIId(), kmKostenKomplett);

						kostenDesProjekts = kostenDesProjekts.add(bdDiaeten);

						// Neue Zeile einfuegen

						AuftragNachkalkulationDto oNachkalkulationDtoReise = new AuftragNachkalkulationDto(
								auftragDto);

						oNachkalkulationDtoReise
								.setBdGestehungswertmaterialist(kostenDesProjekts);
						oNachkalkulationDtoReise.setSBelegart("DR");
						oNachkalkulationDtoReise.setSBelegnummer("Reise");
						oNachkalkulationDtoReise.setSReiseKommentar(rDto
								.getCKommentar());

						if (rDtoErstesBeginn.getPartnerIId() != null) {
							oNachkalkulationDtoReise
									.setSReisePartner(getPartnerFac()
											.partnerFindByPrimaryKey(
													rDtoErstesBeginn
															.getPartnerIId(),
													theClientDto)
											.formatFixName1Name2());
						}

						oNachkalkulationDtoReise
								.setSReiseMitarbeiter(getPersonalFac()
										.personalFindByPrimaryKey(
												rDto.getPersonalIId(),
												theClientDto).formatAnrede());
						oNachkalkulationDtoReise.settReiseBis(tBis);
						oNachkalkulationDtoReise.settReiseVon(rDto.getTZeit());

						alAuftragNachkalkulationDtos
								.add(oNachkalkulationDtoReise);

					}

				}

			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		// PJ 16248

		for (int k = alAuftragNachkalkulationDtos.size() - 1; k > 0; --k) {
			for (int j = 0; j < k; ++j) {
				AuftragNachkalkulationDto a1 = alAuftragNachkalkulationDtos
						.get(j);
				AuftragNachkalkulationDto a2 = alAuftragNachkalkulationDtos
						.get(j + 1);
				if (a1.getSBelegart().compareTo(a2.getSBelegart()) > 0) {
					alAuftragNachkalkulationDtos.set(j, a2);
					alAuftragNachkalkulationDtos.set(j + 1, a1);

				} else if (a1.getSBelegart().compareTo(a2.getSBelegart()) == 0) {

					if (a1.getSBelegnummer().compareTo(a2.getSBelegnummer()) > 0) {

						alAuftragNachkalkulationDtos.set(j, a2);
						alAuftragNachkalkulationDtos.set(j + 1, a1);
					}
				}
			}
		}

		return alAuftragNachkalkulationDtos;
	}

	private int addPositionToDataPosition(AuftragpositionDto pos,
			Integer iArtikelpositionsnummer, AuftragDto auftragDto,
			LinkedHashMap mwstMap, int index, Boolean bbSeitenumbruch,
			TheClientDto theClientDto) throws EJBExceptionLP {
		data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION_NR] = iArtikelpositionsnummer;
		data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_SEITENUMBRUCH] = bbSeitenumbruch;
		iArtikelpositionsnummer++;
		try {
			// Positionsnummer
			data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION] = getAuftragpositionFac()
					.getPositionNummer(pos.getIId());
			data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_MENGE] = new BigDecimal(
					1);
			data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_EINHEIT] = SystemFac.EINHEIT_STUECK
					.trim();
			data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITIONSART] = LocaleFac.POSITIONSART_IDENT;
			data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_SEITENUMBRUCH] = bbSeitenumbruch;
			BigDecimal bdNettogesamtpreisplusversteckteraufschlagminusrabatte = getAuftragpositionFac()
					.getGesamtpreisPosition(pos.getIId(), theClientDto);
			data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_GESAMTPREIS] = bdNettogesamtpreisplusversteckteraufschlagminusrabatte;

			AuftragpositionDto unterpos = getAuftragpositionFac()
					.auftragpositionFindByAuftragISort(pos.getBelegIId(),
							pos.getISort() + 1);
			MwstsatzDto mwstsatzDto = getMandantFac().mwstsatzFindByPrimaryKey(
					unterpos.getMwstsatzIId(), theClientDto);
			data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_MWSTSATZ] = mwstsatzDto
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

			String sIdent = null;
			// Druckdaten zusammenstellen
			if (pos.getCBez() != null)
				sIdent = pos.getCBez();
			else
				sIdent = "";
			data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_IDENTNUMMER] = sIdent;
			Session session = null;
			try {
				if (pos.getTypCNr().equals(LocaleFac.POSITIONTYP_MITPREISE)) {
					data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITIONSART] = LocaleFac.POSITIONSART_IDENT;
					data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION_TITEL] = pos
							.getCBez();
					data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION_TITEL] = sIdent;
					List<?> l = null;
					String sArtikelInfo = new String();
					SessionFactory factory = FLRSessionFactory.getFactory();
					session = factory.openSession();
					Criteria crit = session
							.createCriteria(FLRAuftragposition.class);
					crit.add(Restrictions.eq("position_i_id", pos.getIId()));
					crit.addOrder(Order
							.asc(RechnungFac.FLR_RECHNUNGPOSITION_I_SORT));
					l = crit.list();
					Iterator<?> iter = l.iterator();
					while (iter.hasNext()) {
						index++;
						FLRAuftragposition flrpos = (FLRAuftragposition) iter
								.next();
						if (flrpos.getPositionart_c_nr().equals(
								LocaleFac.POSITIONSART_IDENT))
							sArtikelInfo = flrpos.getFlrartikel().getC_nr();
						else if (flrpos.getPositionart_c_nr().equals(
								LocaleFac.POSITIONSART_TEXTEINGABE))
							sArtikelInfo = flrpos.getX_textinhalt();
						else if (flrpos.getPositionart_c_nr().equals(
								LocaleFac.POSITIONSART_HANDEINGABE))
							sArtikelInfo = flrpos.getC_bez();
						// Druckdaten zusammenstellen
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_IDENTNUMMER] = sArtikelInfo;
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION_TITEL] = sArtikelInfo;
						// weitere Daten
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_MENGE] = flrpos
								.getN_menge();
						if (flrpos.getEinheit_c_nr() != null)
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_EINHEIT] = flrpos
									.getEinheit_c_nr().trim();
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITIONSART] = LocaleFac.POSITIONSART_IDENT;
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_SEITENUMBRUCH] = new Boolean(
								false);

						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_EINZELPREIS] = flrpos
								.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte();
						if (flrpos.getN_menge() != null
								&& flrpos
										.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte() != null)
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_GESAMTPREIS] = flrpos
									.getN_menge()
									.multiply(
											flrpos.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte());
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_TYP_CNR] = flrpos
								.getTyp_c_nr();
					}
				} else {
					if (pos.getCZusatzbez() != null) {
						if (pos.getCZusatzbez().equals(
								LocaleFac.POSITIONBEZ_BEGINN)) {
							sIdent = buildZeileIdentPosition(pos.getIId(),
									theClientDto);
							String sBez = buildZeileBezPosition(pos.getIId(),
									theClientDto);
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION] = getAuftragpositionFac()
									.getPositionNummer(pos.getIId());
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION_NR] = iArtikelpositionsnummer;
							++iArtikelpositionsnummer;
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITIONSART] = AuftragServiceFac.AUFTRAGPOSITIONART_IDENT;
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_SEITENUMBRUCH] = false;
							if (pos.getCBez() != null) {
								data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_IDENTNUMMER] = pos
										.getCBez();
							}
							if (pos.getCBez() != null) {
								data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION_TITEL] = pos
										.getCBez() + "\n" + sIdent;
							} else {
								data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION_TITEL] = sIdent;
							}
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION_BEZEICHNUNG] = sBez;
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_MENGE] = new BigDecimal(
									1);
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_EINHEIT] = SystemFac.EINHEIT_STUECK
									.trim();
						}
					}

				}
			} finally {
				if (session != null) {
					session.close();
				}
			}
		} catch (RemoteException ex) {
		} catch (EJBExceptionLP ex) {
		}

		return index;
	}

	protected String getArtikelsetTypeImpl(Integer auftragpositionIId,
			Integer positionIIdArtikelset) {
		String setartikelType = null;

		if (positionIIdArtikelset != null) {
			setartikelType = ArtikelFac.SETARTIKEL_TYP_POSITION;
		} else {
			Session session = null;
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria crit = session.createCriteria(FLRAuftragposition.class);
			crit.add(Restrictions.eq("position_i_id_artikelset",
					auftragpositionIId));

			int iZeilen = crit.list().size();

			if (iZeilen > 0) {
				setartikelType = ArtikelFac.SETARTIKEL_TYP_KOPF;
			}

			session.close();
		}

		return setartikelType;

	}

	protected String getArtikelsetType(AuftragpositionDto auftragpositionDto) {
		return getArtikelsetTypeImpl(auftragpositionDto.getIId(),
				auftragpositionDto.getPositioniIdArtikelset());
	}

	protected String getArtikelsetType(
			FLRAuftragpositionReport auftragpositionDto) {
		return getArtikelsetTypeImpl(auftragpositionDto.getI_id(),
				auftragpositionDto.getPosition_i_id_artikelset());
	}

	/**
	 * Die Auftragbestaetigung zu einem bestimmten Auftrag drucken.
	 * 
	 * @param iIdAuftragI
	 *            PK des Auftrags
	 * @param iAnzahlKopienI
	 *            wieviele Kopien zusaetzlich zum Original?
	 * @param bMitLogo
	 *            Boolean
	 * @param sReportname
	 *            String
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return JasperPrint[] der Druck und seine Kopien
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public JasperPrintLP[] printAuftragbestaetigung(Integer iIdAuftragI,
			Integer iAnzahlKopienI, Boolean bMitLogo, String sReportname,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAuftragIId(iIdAuftragI);
		JasperPrintLP[] aJasperPrint = null;
		boolean bTermineUnterschiedlich = false;
		Timestamp tLieferTermin = null;
		try {
			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(
					iIdAuftragI);
			tLieferTermin = Helper.cutTimestamp(auftragDto.getDLiefertermin());
			ArrayList<AuftragpositionDto> dtos = new ArrayList<AuftragpositionDto>();
			AuftragpositionDto[] aPositionDtos = null;
			Session sesion = null;
			Timestamp tposLieferTermin = null;
			try {
				SessionFactory factory = FLRSessionFactory.getFactory();
				sesion = factory.openSession();
				Criteria crit = sesion.createCriteria(FLRAuftragposition.class);
				crit.add(Restrictions.eq("flrauftrag.i_id", iIdAuftragI));
				crit.add(Restrictions.isNull("position_i_id"));
				crit.addOrder(Order.asc("i_sort"));
				List<?> l = crit.list();
				Iterator<?> iter = l.iterator();
				while (iter.hasNext()) {
					FLRAuftragposition pos = (FLRAuftragposition) iter.next();
					AuftragpositionDto apositionDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(pos.getI_id());
					if (pos.getPositionart_c_nr().equals(
							AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)
							|| pos.getPositionart_c_nr()
									.equals(AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE)) {
						if (apositionDto.getTUebersteuerbarerLiefertermin() != null) {
							tposLieferTermin = Helper.cutTimestamp(apositionDto
									.getTUebersteuerbarerLiefertermin());
							if (!(tLieferTermin.equals(tposLieferTermin))) {
								bTermineUnterschiedlich = true;
							}
						}
					}
					if (pos.getPositionart_c_nr().equals(
							AuftragServiceFac.AUFTRAGPOSITIONART_POSITION)) {
						if (!pos.getC_zbez().equals(LocaleFac.POSITIONBEZ_ENDE)) {
							dtos.add(apositionDto);
						}
					} else {

						if (pos.getFlrartikel() != null) {
							ArtikelDto aDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(
											pos.getFlrartikel().getI_id(),
											theClientDto);
							if (Helper.short2boolean(aDto.getBKalkulatorisch())) {
								continue;
							}
						}

						dtos.add(apositionDto);
					}
				}
			} finally {
				if (sesion != null) {
					sesion.close();
				}
			}
			aPositionDtos = new AuftragpositionDto[dtos.size()];
			for (int i = 0; i < dtos.size(); i++) {
				aPositionDtos[i] = dtos.get(i);
			}

			// Erstellung des Report

			cAktuellerReport = AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG;
			int iArtikelpositionsnummer = 1;
			Boolean bbSeitenumbruch = new Boolean(false); // dieser Wert
			// wechselt mit
			// jedem
			// Seitenumbruch
			// zwischen true und
			// false

			// es gilt das Locale des Auftragskunden
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					auftragDto.getKundeIIdAuftragsadresse(), theClientDto);

			Locale locDruck = Helper.string2Locale(kundeDto.getPartnerDto()
					.getLocaleCNrKommunikation());

			AnsprechpartnerDto ansprechpartnerDto = null;

			if (auftragDto.getAnsprechparnterIId() != null) {
				ansprechpartnerDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(
								auftragDto.getAnsprechparnterIId(),
								theClientDto);
			}

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);

			// fuer das Andrucken der Mwstsatze wird eine Map vorbereitet, die
			// einen
			// Eintrag fuer jeden Mwstsatz des Mandanten enthaelt
			final Set<?> mwstSatzKeys = getMandantFac()
					.mwstsatzIIdFindAllByMandant(auftragDto.getMandantCNr(),
							theClientDto);
			LinkedHashMap<Object, MwstsatzReportDto> mwstMap = new LinkedHashMap<Object, MwstsatzReportDto>();
			for (Iterator<?> iter = mwstSatzKeys.iterator(); iter.hasNext();) {
				Object item = (Object) iter.next();
				MwstsatzReportDto mwstsatzReportDto = new MwstsatzReportDto();
				mwstMap.put(item, mwstsatzReportDto);
			}

			// Kurzzeichenkombi zum Andrucken zusammenbauen
			PersonalDto oPersonalBenutzer = getPersonalFac()
					.personalFindByPrimaryKey(theClientDto.getIDPersonal(),
							theClientDto);
			PersonalDto oPersonalAnleger = getPersonalFac()
					.personalFindByPrimaryKey(
							auftragDto.getPersonalIIdAnlegen(), theClientDto);
			String sKurzzeichenkombi = Helper.getKurzzeichenkombi(
					oPersonalBenutzer.getCKurzzeichen(),
					oPersonalAnleger.getCKurzzeichen());

			// Anrede fuer den Geschaeftsfuehrer zusammenbauen
			PersonalDto[] aGeschaeftsfDto = getPersonalFac()
					.personalFindByMandantCNrPersonalfunktionCNr(
							theClientDto.getMandant(),
							PersonalFac.PERSONALFUNKTION_GESCHAEFTSFUEHRER);
			String sAnredeGeschaeftsfuehrer = null;
			if (aGeschaeftsfDto != null && aGeschaeftsfDto.length > 0) {
				if (aGeschaeftsfDto[0] != null) {
					sAnredeGeschaeftsfuehrer = getPartnerFac()
							.partnerFindByPrimaryKey(
									aGeschaeftsfDto[0].getPartnerIId(),
									theClientDto).formatAnrede();
					// @todo anredeGeschaeftsfueherer =
					// aGeschaeftsfDto[0].getPartnerDto
					// ().formatAnredeName2Name1(); PJ 3780
				}
			}

			AuftragtextDto auftragtextDto = null;

			// Kopftext
			String sKopftext = auftragDto.getCKopftextUebersteuert();

			if (sKopftext == null || sKopftext.length() == 0) {
				if (auftragDto.getAuftragIIdKopftext() != null) {
					auftragtextDto = getAuftragServiceFac()
							.auftragtextFindByMandantLocaleCNr(
									theClientDto.getMandant(),
									kundeDto.getPartnerDto()
											.getLocaleCNrKommunikation(),
									AuftragServiceFac.AUFTRAGTEXT_KOPFTEXT,
									theClientDto);
					sKopftext = auftragtextDto.getCTextinhalt();
				} else {
					sKopftext = "";
				}

			}

			// Fusstext
			String sFusstext = auftragDto.getCFusstextUebersteuert();

			if (sFusstext == null || sFusstext.length() == 0) {
				if (auftragDto.getAuftragIIdFusstext() != null) {
					auftragtextDto = getAuftragServiceFac()
							.auftragtextFindByMandantLocaleCNr(
									theClientDto.getMandant(),
									kundeDto.getPartnerDto()
											.getLocaleCNrKommunikation(),
									AuftragServiceFac.AUFTRAGTEXT_FUSSTEXT,
									theClientDto);
					sFusstext = auftragtextDto.getCTextinhalt();
				} else {
					sFusstext = "";
				}
			}

			// dem Report seine Parameter setzen
			HashMap<String, Object> parameter = new HashMap<String, Object>();
			// CK: PJ 13849
			parameter.put(
					"P_BEARBEITER",
					getPersonalFac().getPersonRpt(
							auftragDto.getPersonalIIdAnlegen(), theClientDto));
			parameter.put(P_MANDANTADRESSE,
					Helper.formatMandantAdresse(mandantDto));

			KundeDto kundeDtoRechnungsadresse = getKundeFac()
					.kundeFindByPrimaryKey(
							auftragDto.getKundeIIdRechnungsadresse(),
							theClientDto);

			// Nur Andrucken, wenn Auftragsadresse und Rechnungsadresse ungleich
			if (kundeDtoRechnungsadresse.getIId().equals(kundeDto.getIId())
					&& sindAnsprechpartnerGleich(
							auftragDto.getAnsprechparnterIId(),
							auftragDto.getAnsprechpartnerIIdRechnungsadresse())) {

			} else {
				AnsprechpartnerDto ansprechpartnerDtoRechnungsadresse = null;

				if (auftragDto.getAnsprechpartnerIIdRechnungsadresse() != null) {
					ansprechpartnerDtoRechnungsadresse = getAnsprechpartnerFac()
							.ansprechpartnerFindByPrimaryKey(
									auftragDto
											.getAnsprechpartnerIIdRechnungsadresse(),
									theClientDto);
				}

				parameter.put(
						"P_KUNDERECHNUNGSADRESSE_ADRESSBLOCK",
						formatAdresseFuerAusdruck(
								kundeDtoRechnungsadresse.getPartnerDto(),
								ansprechpartnerDtoRechnungsadresse, mandantDto,
								locDruck, LocaleFac.BELEGART_RECHNUNG));

				if (ansprechpartnerDtoRechnungsadresse != null) {
					parameter
							.put("P_ANSPRECHPARTNER_KUNDERECHNUNGSADRESSE_ADRESSBLOCK",
									getPartnerFac()
											.formatFixAnredeTitelName2Name1FuerAdresskopf(
													ansprechpartnerDtoRechnungsadresse
															.getPartnerDto(),
													locDruck, null));
				}

			}

			// PJ18870
			parameter.put(
					"P_SUBREPORT_PARTNERKOMMENTAR",
					getPartnerServicesFac()
							.getSubreportAllerMitzudruckendenPartnerkommentare(
									kundeDto.getPartnerDto().getIId(), true,
									LocaleFac.BELEGART_AUFTRAG, theClientDto));

			parameter.put(
					"P_KUNDE_ADRESSBLOCK",
					formatAdresseFuerAusdruck(kundeDto.getPartnerDto(),
							ansprechpartnerDto, mandantDto, locDruck,
							LocaleFac.BELEGART_AUFTRAG));

			if (ansprechpartnerDto != null) {
				parameter.put(
						"P_ANSPRECHPARTNER_KUNDE_ADRESSBLOCK",
						getPartnerFac()
								.formatFixAnredeTitelName2Name1FuerAdresskopf(
										ansprechpartnerDto.getPartnerDto(),
										locDruck, null));
			}

			parameter.put("P_KUNDE_UID", kundeDto.getPartnerDto().getCUid());
			parameter.put("P_KUNDE_EORI", kundeDto.getPartnerDto().getCEori());
			if (kundeDto.getIidDebitorenkonto() != null) {
				KontoDto kontoDto = getFinanzFac().kontoFindByPrimaryKey(
						kundeDto.getIidDebitorenkonto());
				parameter.put("P_KUNDE_DEBITORENKONTO", kontoDto.getCNr());
			}
			parameter.put("P_KUNDE_KUNDENNUMMER", kundeDto.getIKundennummer());
			parameter.put("P_KOPFTEXT",
					Helper.formatStyledTextForJasper(sKopftext));

			ParametermandantDto parameterAbweichung = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_RECHNUNG,
							ParameterFac.PARAMETER_MAXIMALE_ABWEICHUNG_SUMMENAUSDRUCK);
			final Double dAbweichung = (Double) parameterAbweichung
					.getCWertAsObject();
			parameter.put("P_MAXIMALE_ABWEICHUNG", dAbweichung);

			// PJ15507

			AuftragauftragdokumentDto[] auftragauftragdokumentDtos = getAuftragServiceFac()
					.auftragauftragdokumentFindByAuftragIId(auftragDto.getIId());
			if (auftragauftragdokumentDtos != null
					&& auftragauftragdokumentDtos.length > 0) {

				Object[][] oSubData = new Object[auftragauftragdokumentDtos.length][1];

				for (int i = 0; i < auftragauftragdokumentDtos.length; i++) {
					AuftragdokumentDto aDokDto = getAuftragServiceFac()
							.auftragdokumentFindByPrimaryKey(
									auftragauftragdokumentDtos[i]
											.getAuftragdokumentIId());
					oSubData[i][0] = aDokDto.getBezeichnung();
				}
				String[] fieldnames = new String[] { "F_AUFTRAGDOKUMENT" };

				parameter.put("P_AUFTRAGDOKUMENTE", new LPDatenSubreport(
						oSubData, fieldnames));

			}

			// die Rabatte sollen nicht sichtbar sein, wenn keine vergeben
			// wurden
			parameter.put("P_RABATTSPALTE_DRUCKEN", Helper
					.short2Boolean(kundeDto.getBRechnungsdruckmitrabatt()));
			parameter.put("P_ZUSATZRABATTSPALTE_DRUCKEN", Helper
					.short2Boolean(kundeDto.getBRechnungsdruckmitrabatt()));
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
			if (kundeDto.getIidDebitorenkonto() != null) {
				parameter.put("P_DEBITORENKONTO", getFinanzFac()
						.kontoFindByPrimaryKey(kundeDto.getIidDebitorenkonto())
						.getCNr());
			}

			PersonalDto vertreterDto = null;
			String cVertreterAnredeShort = null;

			if (auftragDto.getPersonalIIdVertreter() != null) {
				vertreterDto = getPersonalFac().personalFindByPrimaryKey(
						auftragDto.getPersonalIIdVertreter(), theClientDto);
				cVertreterAnredeShort = vertreterDto.getPartnerDto()
						.formatFixName2Name1();

				// Vertreter Kontaktdaten
				String sVertreterEmail = vertreterDto.getCEmail();

				String sVertreterFaxDirekt = vertreterDto.getCDirektfax();

				String sVertreterFax = vertreterDto.getCFax();

				String sVertreterTelefon = vertreterDto.getCTelefon();
				parameter.put(LPReport.P_VERTRETEREMAIL,
						sVertreterEmail != null ? sVertreterEmail : "");
				if (sVertreterFaxDirekt != null && sVertreterFaxDirekt != "") {
					parameter.put(LPReport.P_VERTRETERFAX, sVertreterFaxDirekt);
				} else {
					parameter.put(LPReport.P_VERTRETERFAX,
							sVertreterFax != null ? sVertreterFax : "");
				}
				parameter.put(LPReport.P_VERTRETEERTELEFON,
						sVertreterTelefon != null ? sVertreterTelefon : "");

			}

			parameter.put("P_VERTRETER_ANREDE", cVertreterAnredeShort);
			parameter.put(LPReport.P_UNSERZEICHEN, sKurzzeichenkombi);

			parameter.put("P_BELEGDATUM",
					Helper.formatDatum(auftragDto.getTBelegdatum(), locDruck));
			parameter
					.put("P_BELEGDATUM_TIMESTAMP", auftragDto.getTBelegdatum());
			parameter.put("P_BESTELLDATUM_TIMESTAMP",
					auftragDto.getDBestelldatum());
			parameter.put("P_FINALTERMIN_TIMESTAMP",
					auftragDto.getDFinaltermin());
			parameter.put("P_BESTELLNUMMER", auftragDto.getCBestellnummer());
			parameter.put("P_BEZEICHNUNG",
					auftragDto.getCBezProjektbezeichnung());

			if (auftragDto.getProjektIId() != null) {
				ProjektDto pjDto = getProjektFac().projektFindByPrimaryKey(
						auftragDto.getProjektIId());
				parameter.put("P_PROJEKTNUMMER", pjDto.getCNr());
			}

			// Storno ?
			if (auftragDto.getStatusCNr().equals(RechnungFac.STATUS_STORNIERT)) {
				parameter.put("P_STORNIERT", new Boolean(true));
			} else {
				parameter.put("P_STORNIERT", new Boolean(false));
			}

			parameter.put("P_LIEFERANTENNR", kundeDto.getCLieferantennr());
			parameter.put("P_TERMINEUNTERSCHIEDLICH", new Boolean(
					bTermineUnterschiedlich));

			String cBriefanrede = "";

			if (ansprechpartnerDto != null) {
				cBriefanrede = getPartnerServicesFac().getBriefanredeFuerBeleg(
						auftragDto.getAnsprechparnterIId(),
						kundeDto.getPartnerIId(), locDruck, theClientDto);
			} else {
				// neutrale Anrede
				cBriefanrede = getBriefanredeNeutralOderPrivatperson(
						kundeDto.getPartnerIId(), locDruck, theClientDto);
			}

			if (auftragDto.getKundeIIdLieferadresse() != null) {

				ParametermandantDto parametermandantDto = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_AUFTRAG,
								ParameterFac.PARAMETER_LIEFERADRESSE_IST_AUFTRAGSADRESSE);
				boolean bLieferadresseIstAuftragsadresse = ((java.lang.Boolean) parametermandantDto
						.getCWertAsObject()).booleanValue();
				if (auftragDto.getKundeIIdLieferadresse().equals(
						auftragDto.getKundeIIdAuftragsadresse())
						&& bLieferadresseIstAuftragsadresse == true) {

				} else {

					KundeDto lieferadresseKunde = getKundeFac()
							.kundeFindByPrimaryKey(
									auftragDto.getKundeIIdLieferadresse(),
									theClientDto);
					AnsprechpartnerDto aLief = null;

					if (auftragDto.getAnsprechpartnerIIdLieferadresse() != null) {
						aLief = getAnsprechpartnerFac()
								.ansprechpartnerFindByPrimaryKey(
										auftragDto
												.getAnsprechpartnerIIdLieferadresse(),
										theClientDto);
					}

					if (auftragDto.getKundeIIdLieferadresse().equals(
							auftragDto.getKundeIIdAuftragsadresse())
							&& sindAnsprechpartnerGleich(
									auftragDto.getAnsprechparnterIId(),
									auftragDto
											.getAnsprechpartnerIIdLieferadresse())) {

					} else {
						parameter.put(
								"P_LIEFERADRESSE",
								formatAdresseFuerAusdruck(
										lieferadresseKunde.getPartnerDto(),
										aLief, mandantDto, locDruck,
										LocaleFac.BELEGART_LIEFERSCHEIN));

						if (aLief != null) {
							parameter
									.put("P_ANSPRECHPARTNER_LIEFERADRESSE",
											getPartnerFac()
													.formatFixAnredeTitelName2Name1FuerAdresskopf(
															aLief.getPartnerDto(),
															locDruck, null));
						}

					}
					if (lieferadresseKunde.getIidDebitorenkonto() != null) {
						KontoDto kontoDto = getFinanzFac()
								.kontoFindByPrimaryKey(
										lieferadresseKunde
												.getIidDebitorenkonto());
						parameter.put("P_DEBITORENKONTO", kontoDto.getCNr());
					}
					parameter.put("P_LIEFERKUNDE_KUNDENNUMMER",
							lieferadresseKunde.getIKundennummer());
				}

			}

			parameter.put("P_BRIEFANREDE", cBriefanrede);

			String cLabelRahmenOderLiefertermin = getTextRespectUISpr(
					"lp.liefertermin", theClientDto.getMandant(), locDruck);

			String cLabelAuftragcnr = null;

			if (auftragDto.getAuftragartCNr().equals(
					AuftragServiceFac.AUFTRAGART_RAHMEN)) {
				cLabelRahmenOderLiefertermin = getTextRespectUISpr(
						"lp.rahmentermin", theClientDto.getMandant(), locDruck);
				cLabelAuftragcnr = getTextRespectUISpr("auft.rahmenauftragcnr",
						theClientDto.getMandant(), locDruck);
			} else if (auftragDto.getAuftragartCNr().equals(
					AuftragServiceFac.AUFTRAGART_ABRUF)) {
				AuftragDto rahmenauftragDto = getAuftragFac()
						.auftragFindByPrimaryKey(
								auftragDto.getAuftragIIdRahmenauftrag());
				String rahmenCNr = getAuftragFac().auftragFindByPrimaryKey(
						auftragDto.getAuftragIIdRahmenauftrag()).getCNr();
				cLabelAuftragcnr = getTextRespectUISpr("lp.abrufzurahmen",
						theClientDto.getMandant(), locDruck);
				cLabelAuftragcnr = cLabelAuftragcnr + " " + rahmenCNr;
			} else if (auftragDto.getAuftragartCNr().equals(
					AuftragServiceFac.AUFTRAGART_FREI)) {
				cLabelAuftragcnr = getTextRespectUISpr("auft.aufragcnr",
						theClientDto.getMandant(), locDruck);
			} else if (auftragDto.getAuftragartCNr().equals(
					AuftragServiceFac.AUFTRAGART_WIEDERHOLEND)) {
				cLabelAuftragcnr = getTextRespectUISpr(
						"auft.wiederholauftragnr", theClientDto.getMandant(),
						locDruck);
				parameter.put("P_VERRECHNUNGSBEGINN",
						auftragDto.getTLauftermin());
				// Auftragwiederholungsintervall
				String sAuftragwiederholungsintervall = null;
				Map<?, ?> mapAuftragWHIntervallSpr = getAuftragServiceFac()
						.getAuftragwiederholungsintervall(locDruck);
				if (mapAuftragWHIntervallSpr.containsKey(auftragDto
						.getWiederholungsintervallCNr())) {
					sAuftragwiederholungsintervall = (String) mapAuftragWHIntervallSpr
							.get(auftragDto.getWiederholungsintervallCNr());
				} else {
					sAuftragwiederholungsintervall = auftragDto
							.getWiederholungsintervallCNr();
				}
				parameter.put("P_VERRECHNUNGSINTERVALL",
						sAuftragwiederholungsintervall);
			}

			parameter.put("P_LABELAUFTRAGCNR", cLabelAuftragcnr);
			parameter.put("P_BELEGKENNUNG",
					baueKennungAuftragbestaetigung(auftragDto, theClientDto));

			parameter.put("P_LABELLIEFERTERMIN", cLabelRahmenOderLiefertermin);
			parameter
					.put("P_LIEFERTERMIN", Helper.formatDatum(
							auftragDto.getDLiefertermin(), locDruck));
			parameter.put("P_LIEFERTERMIN_TIMESTAMP",
					auftragDto.getDLiefertermin());

			parameter.put(
					"P_UNVERBINDLICHERLIEFERTERMIN",
					new Boolean(Helper.short2boolean(auftragDto
							.getBLieferterminUnverbindlich())));

			// die Lieferart
			parameter.put(
					"P_LIEFERART",
					getLocaleFac().lieferartFindByIIdLocaleOhneExc(
							auftragDto.getLieferartIId(), locDruck,
							theClientDto));
			parameter.put("P_LIEFERART_ORT", auftragDto.getCLieferartort());

			// Spediteur
			SpediteurDto spediteurDto = getMandantFac()
					.spediteurFindByPrimaryKey(auftragDto.getSpediteurIId());
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

			// das Zahlungsziel
			parameter.put(
					"P_ZAHLUNGSZIEL",
					getMandantFac().zahlungszielFindByIIdLocaleOhneExc(
							auftragDto.getZahlungszielIId(), locDruck,
							theClientDto));

			parameter.put("P_BELEGWAEHRUNG", auftragDto.getCAuftragswaehrung());
			parameter.put("P_GESCHAEFTSFUEHRER_ANREDE",
					sAnredeGeschaeftsfuehrer);
			parameter.put("P_ALLGEMEINER_RABATT",
					auftragDto.getFAllgemeinerRabattsatz());
			parameter.put("P_ALLGEMEINER_RABATT_STRING", Helper.formatZahl(
					auftragDto.getFAllgemeinerRabattsatz(), locDruck));
			parameter.put("P_PROJEKT_RABATT",
					auftragDto.getFProjektierungsrabattsatz());
			parameter.put("P_PROJEKT_RABATT_STRING", Helper.formatZahl(
					auftragDto.getFProjektierungsrabattsatz(), locDruck));
			parameter.put("P_RECHNUNGSDRUCKMITRABATT", Helper
					.short2Boolean(kundeDto.getBRechnungsdruckmitrabatt()));
			// Konsistenzpruefung: Die Summe der einzelnen Positionen muss den
			// Auftragwert ergeben
			parameter.put("P_AUFTRAGSWERT",
					auftragDto.getNGesamtauftragswertInAuftragswaehrung());

			if (auftragDto.getKostIId() != null) {
				KostenstelleDto kostenstelleDto = getSystemFac()
						.kostenstelleFindByPrimaryKey(auftragDto.getKostIId());
				parameter
						.put(LPReport.P_KOSTENSTELLE, kostenstelleDto.getCNr());
			}

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

			// folgende Werte werden als eigene Parameter uebergeben.
			// aus Kompatibilitaetsgruenden wird noch das alte P_SUMMARY in dem
			// die Werte
			// mit jeweils einer trennenden Leerzeile hintereinandergehaengt
			// werden zusaetzlich befuellt.
			StringBuffer buffSummaryAlt = new StringBuffer();
			// Externer Kommentar
			if (auftragDto.getXExternerkommentar() != null
					&& auftragDto.getXExternerkommentar().length() > 0) {
				parameter.put(P_EXTERNERKOMMENTAR, Helper
						.formatStyledTextForJasper(auftragDto
								.getXExternerkommentar()));
				buffSummaryAlt.append(parameter.get(P_EXTERNERKOMMENTAR))
						.append("\n\n");
			}

			// Eigentumsvorbehalt
			MediastandardDto mediastandardDto = null;
			try {
				mediastandardDto = getMediaFac()
						.mediastandardFindByCNrDatenformatCNrMandantCNr(
								MediaFac.MEDIASTANDARD_EIGENTUMSVORBEHALT,
								MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML,
								theClientDto.getMandant(),
								kundeDto.getPartnerDto()
										.getLocaleCNrKommunikation(),
								theClientDto);
			} catch (Exception ex) {
				if (ex.getCause() instanceof NoResultException) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_KEIN_EIGENTUMSVORBEHALT_DEFINIERT,
							new Exception("Kein Eigenumsvorbehalt gefunden"));
				}
			}

			if (mediastandardDto != null) {
				parameter.put(LPReport.P_EIGENTUMSVORBEHALT, Helper
						.formatStyledTextForJasper(mediastandardDto
								.getOMediaText()));
				buffSummaryAlt.append(
						parameter.get(LPReport.P_EIGENTUMSVORBEHALT)).append(
						"\n\n");
			}

			// lieferbed: 0 Lieferbedingungen
			try {
				mediastandardDto = getMediaFac()
						.mediastandardFindByCNrDatenformatCNrMandantCNr(
								MediaFac.MEDIASTANDARD_LIEFERBEDINGUNGEN,
								MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML,
								theClientDto.getMandant(),
								kundeDto.getPartnerDto()
										.getLocaleCNrKommunikation(),
								theClientDto);
			} catch (Exception ex) {
				if (ex.getCause() instanceof NoResultException) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_KEINE_LIEFERBEDINGUNGEN_DEFINIERT,
							new Exception("Keine Lieferbedingungen gefunden"));
				}
			}

			if (mediastandardDto != null) {
				parameter.put(LPReport.P_LIEFERBEDINGUNGEN, Helper
						.formatStyledTextForJasper(mediastandardDto
								.getOMediaText()));
				buffSummaryAlt.append(
						parameter.get(LPReport.P_LIEFERBEDINGUNGEN)).append(
						"\n\n");
			}

			// Fusstext
			if (sFusstext != null) {
				parameter.put("P_FUSSTEXT",
						Helper.formatStyledTextForJasper(sFusstext));
				buffSummaryAlt.append(parameter.get("P_FUSSTEXT")).append(
						"\n\n");
			}

			// Anrede des Mandanten
			String sMandantAnrede = mandantDto.getPartnerDto()
					.formatFixName1Name2();
			StringBuffer buff = new StringBuffer();
			if (sMandantAnrede != null) {
				parameter.put(P_MANDANT_ANREDE_UND_NAME, sMandantAnrede);
				buff.append(sMandantAnrede).append("\n\n");
			}

			// P_SUMMARY: Die Unterschrift fuer Belege inclusive
			// Unterschriftstext und -funktion
			// Beispiel:
			// "i.A. Ing. Werner Hehenwarter" - im Falle des Auftrags der
			// Vertreter aus den Kopfdaten
			// "Einkaufsleiter"
			if (vertreterDto != null) {
				String sVertreterUFTitelName2Name1 = vertreterDto
						.formatFixUFTitelName2Name1();

				parameter.put(P_VERTRETER, vertreterDto.formatAnrede());

				parameter.put(P_VERTRETER_UNTERSCHRIFTSFUNKTION_UND_NAME,
						sVertreterUFTitelName2Name1);
				buff.append(sVertreterUFTitelName2Name1);

				if (vertreterDto.getCUnterschriftstext() != null
						&& vertreterDto.getCUnterschriftstext().length() > 0) {
					String sUnterschriftstext = vertreterDto
							.getCUnterschriftstext();
					parameter.put(P_VERTRETER_UNTERSCHRIFTSTEXT,
							sUnterschriftstext);
					buff.append("\n").append(sUnterschriftstext);
				}
			}

			buffSummaryAlt.append(buff.toString());
			parameter
					.put("P_SUMMARY", Helper
							.formatStyledTextForJasper(buffSummaryAlt
									.toString()));

			// AUFTRAGSEIGENSCHAFTEN
			Hashtable<String, String> hAE = getAuftragEigenschaften(
					auftragDto.getIId(), theClientDto);
			if (hAE != null) {
				parameter.put(P_AUFTRAGEIGENSCHAFT_FA,
						hAE.get(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_FA));
				parameter
						.put(P_AUFTRAGEIGENSCHAFT_CLUSTER,
								hAE.get(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_CLUSTER));
				parameter.put(P_AUFTRAGEIGENSCHAFT_EQNR,
						hAE.get(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_EQNR));
			}

			int iAnzahlZeilen = 0; // Anzahl der Zeilen in der Gruppe

			for (int i = 0; i < aPositionDtos.length; i++) {
				if (aPositionDtos[i].getTypCNr() == null) {
					if (aPositionDtos[i].getPositionsartCNr().equals(
							AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {

						// Die Zeilenanzahl muss vor dem Befuellen festgelegt
						// werden.
						StuecklisteInfoDto stuecklisteInfoDto = getStuecklisteFac()
								.getStrukturdatenEinesArtikels(
										aPositionDtos[i].getArtikelIId(), true,
										null, // in die Rekursion mit einer
										// leeren Listen einsteigen
										0, // in die Rekursion mit Ebene 0
											// einsteigen
										-1, // alle Stuecklisten komplett
										// aufloesen
										false, // Menge pro Einheit der
										// uebergeorndeten Position
										new BigDecimal(1), // fuer 1 Einheit der
										// STKL
										true, // Fremdfertigung aufloesen
										theClientDto);

						iAnzahlZeilen++; // fuer die eigentliche Ident Position
						iAnzahlZeilen += stuecklisteInfoDto
								.getIiAnzahlPositionen().intValue();

					} else {
						iAnzahlZeilen++; // fuer die Positionszeile
					}
				} else if (aPositionDtos[i].getPositionsartCNr().equals(
						LocaleFac.POSITIONSART_POSITION)) {
					iAnzahlZeilen++; // fuer die eigentliche Position
					if (aPositionDtos[i].getTypCNr().equals(
							LocaleFac.POSITIONTYP_MITPREISE)) {
						Session session = null;
						SessionFactory factory = FLRSessionFactory.getFactory();
						session = factory.openSession();
						Criteria crit = session
								.createCriteria(FLRAuftragposition.class);
						crit.add(Restrictions.eq("position_i_id",
								aPositionDtos[i].getIId()));
						crit.addOrder(Order
								.asc(RechnungFac.FLR_RECHNUNGPOSITION_I_SORT));
						iAnzahlZeilen = iAnzahlZeilen + crit.list().size();
					}
				}

			}

			// PJ1581
			if (auftragDto.getNKorrekturbetrag() != null
					&& auftragDto.getNKorrekturbetrag().doubleValue() != 0) {
				iAnzahlZeilen++;
			}

			data = new Object[iAnzahlZeilen][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ANZAHL_SPALTEN];

			boolean korrekturbetragHinzugefuegt = false;
			// Der Index der aktuell verarbeiteten Angebotposition
			int index = 0;
			// die Datenmatrix befuellen
			for (int i = 0; i < aPositionDtos.length; i++) {

				if (aPositionDtos[i].getTypCNr() != null) {
					if (aPositionDtos[i].getPositionsartCNr().equals(
							AuftragServiceFac.AUFTRAGPOSITIONART_POSITION)) {
						index = addPositionToDataPosition(aPositionDtos[i],
								iArtikelpositionsnummer, auftragDto, mwstMap,
								index, bbSeitenumbruch, theClientDto);
						index++;
					}
				} else {
					// Artikelpositionen

					data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITIONOBJEKT] = getSystemReportFac()
							.getPositionForReport(LocaleFac.BELEGART_AUFTRAG,
									aPositionDtos[i].getIId(), theClientDto);

					// CK: LT CU soll die Positionsart bei allen Positionsarten
					// angedruckt werden
					data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITIONSART] = aPositionDtos[i]
							.getPositionsartCNr();

					if (aPositionDtos[i].getPositionsartCNr().equals(
							AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)
							|| aPositionDtos[i]
									.getPositionsartCNr()
									.equals(AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE)) {
						data[index][REPORT_AUFTRAGBESTAETIGUNG_INTERNAL_IID] = aPositionDtos[i]
								.getIId();

						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION] = getAuftragpositionFac()
								.getPositionNummer(aPositionDtos[i].getIId());
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_IDENT_TEXTEINGABE] = aPositionDtos[i]
								.getXTextinhalt();

						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_LV_POSITION] = aPositionDtos[i]
								.getCLvposition();
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_SEITENUMBRUCH] = bbSeitenumbruch;
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION_NR] = iArtikelpositionsnummer;

						// PJ 15926
						if (aPositionDtos[i].getVerleihIId() != null) {
							VerleihDto verleihDto = getArtikelFac()
									.verleihFindByPrimaryKey(
											aPositionDtos[i].getVerleihIId());
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_VERLEIHTAGE] = verleihDto
									.getITage();
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_VERLEIHFAKTOR] = verleihDto
									.getFFaktor();
						}

						iArtikelpositionsnummer++;

						// stattdessen wird Report Feld
						// REPORT_AUFTRAGBESTAETIGUNG_IMAGE verwendet
						// data[index][AuftragReportFac.
						// REPORT_AUFTRAGBESTAETIGUNG_POSITION_KOMMENTAR_IMAGE]
						// = imageKommentar;

						ArtikelDto oArtikelDto = getArtikelFac()
								.artikelFindByPrimaryKey(
										aPositionDtos[i].getArtikelIId(),
										theClientDto);
						BelegPositionDruckIdentDto druckDto = printIdent(
								aPositionDtos[i], LocaleFac.BELEGART_AUFTRAG,
								oArtikelDto, locDruck,
								kundeDto.getPartnerIId(), theClientDto);

						// TODO: (ghp) Hmm, warum artikelkommentare laden wenn
						// sie nicht verwendet werden?
						// ArtikelkommentarDto[] artikelkommentarDto =
						// getArtikelkommentarFac()
						// .artikelkommentardruckFindByArtikelIIdBelegartCNr(
						// oArtikelDto.getIId(),
						// LocaleFac.BELEGART_AUFTRAG,
						// Helper.locale2String(locDruck),
						// theClientDto);
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_BEZEICHNUNG] = druckDto
								.getSBezeichnung();
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_KURZBEZEICHNUNG] = druckDto
								.getSKurzbezeichnung();
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION_TITEL] = druckDto
								.getSArtikelInfo();
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION_ZUSATZBEZ] = druckDto
								.getSZusatzBezeichnung();
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ARTIKELCZBEZ2] = druckDto
								.getSArtikelZusatzBezeichnung2();
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION_KOMMENTAR_TEXT] = druckDto
								.getSArtikelkommentar();
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_IMAGE] = druckDto
								.getOImageKommentar();

						try {
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_FIKTIVERLAGERSTAND] = getInternebestellungFac()
									.getFiktivenLagerstandZuZeitpunkt(
											oArtikelDto,
											theClientDto,
											aPositionDtos[i]
													.getTUebersteuerbarerLiefertermin());
						} catch (Exception e) {
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_FIKTIVERLAGERSTAND] = null;
						}

						StringBuffer sbArtikelInfo = new StringBuffer("");

						if (aPositionDtos[i].getPositionsartCNr().equals(
								AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {
							// Ident nur fuer "echte" Artikel
							sbArtikelInfo.append(oArtikelDto.getCNr());
							sbArtikelInfo.append("\n");
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_IDENTNUMMER] = oArtikelDto
									.getCNr();
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_REFERENZNUMMER] = oArtikelDto
									.getCReferenznr();
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_INDEX] = oArtikelDto
									.getCIndex();
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_WERBEABGABEPFLICHTIG] = Helper
									.short2Boolean(oArtikelDto
											.getBWerbeabgabepflichtig());
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_REVISION] = oArtikelDto
									.getCRevision();
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_FREIERTEXT] = aPositionDtos[i]
									.getXTextinhalt();

							// Typ, wenn Setartikel
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_SETARTIKEL_TYP] = getArtikelsetType(aPositionDtos[i]);

							// if (aPositionDtos[i].getPositioniIdArtikelset()
							// != null) {
							//
							// data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_SETARTIKEL_TYP]
							// = ArtikelFac.SETARTIKEL_TYP_POSITION;
							//
							// } else {
							//
							// Session session = null;
							// SessionFactory factory = FLRSessionFactory
							// .getFactory();
							// session = factory.openSession();
							// Criteria crit = session
							// .createCriteria(FLRAuftragposition.class);
							// crit.add(Restrictions.eq(
							// "position_i_id_artikelset",
							// aPositionDtos[i].getIId()));
							//
							// int iZeilen = crit.list().size();
							//
							// if (iZeilen > 0) {
							// data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_SETARTIKEL_TYP]
							// = ArtikelFac.SETARTIKEL_TYP_KOPF;
							// }
							// session.close();
							// }

							// KundeArtikelnr gueltig zu Belegdatum
							KundesokoDto kundeSokoDto_gueltig = this
									.getKundesokoFac()
									.kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc(
											auftragDto
													.getKundeIIdRechnungsadresse(),
											oArtikelDto.getIId(),
											new java.sql.Date(auftragDto
													.getTBelegdatum().getTime()));
							if (kundeSokoDto_gueltig != null) {
								data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_KUNDEARTIKELNR] = kundeSokoDto_gueltig
										.getCKundeartikelnummer();
							}

							// KundeArtikelnr gueltig zu Belegdatum
							KundesokoDto kundeSokoDto_gueltig_Auftragsadresse = this
									.getKundesokoFac()
									.kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc(
											kundeDto.getIId(),
											oArtikelDto.getIId(),
											new java.sql.Date(auftragDto
													.getTBelegdatum().getTime()));
							if (kundeSokoDto_gueltig_Auftragsadresse != null) {
								data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_KUNDEARTIKELNR_AUFTRAGSADRESSE] = kundeSokoDto_gueltig_Auftragsadresse
										.getCKundeartikelnummer();
							}

							if (oArtikelDto.getVerpackungDto() != null) {
								data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_BAUFORM] = oArtikelDto
										.getVerpackungDto().getCBauform();
								data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_VERPACKUNGSART] = oArtikelDto
										.getVerpackungDto()
										.getCVerpackungsart();
							}
							if (oArtikelDto.getMaterialIId() != null) {
								MaterialDto materialDto = getMaterialFac()
										.materialFindByPrimaryKey(
												oArtikelDto.getMaterialIId(),
												locDruck, theClientDto);
								if (materialDto.getMaterialsprDto() != null) {
									data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_MATERIAL] = materialDto
											.getMaterialsprDto().getCBez();
								} else {
									data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_MATERIAL] = materialDto
											.getCNr();
								}

								data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_KURS_MATERIALZUSCHLAG] = aPositionDtos[i]
										.getNMaterialzuschlagKurs();
								data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_DATUM_MATERIALZUSCHLAG] = aPositionDtos[i]
										.getTMaterialzuschlagDatum();

							}

							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_MATERIALGEWICHT] = oArtikelDto
									.getFMaterialgewicht();

							if (oArtikelDto.getGeometrieDto() != null) {
								data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_BREITE] = oArtikelDto
										.getGeometrieDto().getFBreite();
								data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_HOEHE] = oArtikelDto
										.getGeometrieDto().getFHoehe();
								data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_TIEFE] = oArtikelDto
										.getGeometrieDto().getFTiefe();
							}
						}

						if (!auftragDto.getDLiefertermin().equals(
								aPositionDtos[i]
										.getTUebersteuerbarerLiefertermin())) {
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITIONTERMIN] = Helper
									.formatDatum(
											Helper.extractDate(aPositionDtos[i]
													.getTUebersteuerbarerLiefertermin()),
											locDruck);

						}
						// Die Positionstermine sollen auch als TS uebergeben
						// werden.
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_LIEFERTERMIN] = aPositionDtos[i]
								.getTUebersteuerbarerLiefertermin();

						String sSnr = null;
						if (aPositionDtos[i].getCSeriennrchargennr() != null) {
							sSnr = aPositionDtos[i].getCSeriennrchargennr();
						} else {
							sSnr = getAuftragpositionFac().getSeriennummmern(
									aPositionDtos[i].getIId(), theClientDto);
							if (sSnr.equals("")) {
								sSnr = null;
							}
						}
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ZWANGSSERIENNUMMER] = sSnr;
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_MENGE] = aPositionDtos[i]
								.getNMenge();

						// PJ18824
						if (aPositionDtos[i]
								.getAuftragpositionIIdRahmenposition() != null) {

							AuftragpositionDto rahmenposDto = getAuftragpositionFac()
									.auftragpositionFindByPrimaryKey(
											aPositionDtos[i]
													.getAuftragpositionIIdRahmenposition());

							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_RAHMENMENGE] = rahmenposDto
									.getNMenge();

							if (rahmenposDto.getNMenge() != null) {

								BigDecimal bdAbgerufen = BigDecimal.ZERO;

								AuftragpositionDto[] rahmenposDtos = getAuftragpositionFac()
										.auftragpositionFindByAuftragpositionIIdRahmenpositionOhneExc(
												aPositionDtos[i]
														.getAuftragpositionIIdRahmenposition(),
												theClientDto);
								Timestamp tLetzteLieferung = null;
								for (int j = 0; j < rahmenposDtos.length; j++) {

									AuftragDto abrufauftragDto = getAuftragFac()
											.auftragFindByPrimaryKey(
													rahmenposDtos[j]
															.getBelegIId());

									if (Helper.cutTimestamp(
											abrufauftragDto.getTBelegdatum())
											.before(Helper
													.cutTimestamp(auftragDto
															.getTBelegdatum()))
											|| Helper
													.cutTimestamp(
															abrufauftragDto
																	.getTBelegdatum())
													.equals(Helper
															.cutTimestamp(auftragDto
																	.getTBelegdatum()))
											&& abrufauftragDto.getIId() <= auftragDto
													.getIId()) {

										bdAbgerufen = bdAbgerufen
												.add(rahmenposDtos[j]
														.getNMenge());
										if (abrufauftragDto.getIId() != auftragDto
												.getIId()) {

											tLetzteLieferung = abrufauftragDto
													.getTBelegdatum();
										}
									}

								}
								data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ABGERUFENE_MENGE] = bdAbgerufen;
								data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_LETZTER_ABRUF] = tLetzteLieferung;
							}

						}

						if (aPositionDtos[i].getNOffeneMenge() != null
								&& aPositionDtos[i].getNMenge() != null) {
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_GELIEFERTE_MENGE] = aPositionDtos[i]
									.getNMenge().subtract(
											aPositionDtos[i].getNOffeneMenge());
						}

						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_EINHEIT] = aPositionDtos[i]
								.getEinheitCNr() == null ? null
								: getSystemFac().formatEinheit(
										aPositionDtos[i].getEinheitCNr(),
										locDruck, theClientDto);
						int iNachkommastellenPreis = getUINachkommastellenPreisVK(auftragDto
								.getMandantCNr());
						AuftragpositionDto auftragpositionDto = (AuftragpositionDto) getBelegVerkaufFac()
								.getBelegpositionVerkaufReport(
										aPositionDtos[i], auftragDto,
										iNachkommastellenPreis);
						if (Helper.short2Boolean(kundeDto
								.getBRechnungsdruckmitrabatt())) {
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_EINZELPREIS] = auftragpositionDto
									.getNReportEinzelpreisplusversteckteraufschlag();
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_RABATTSATZ] = auftragpositionDto
									.getDReportRabattsatz();
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ZUSATZRABATTSATZ] = auftragpositionDto
									.getDReportZusatzrabattsatz();

						} else {
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_EINZELPREIS] = auftragpositionDto
									.getNReportNettoeinzelpreisplusversteckteraufschlag();
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_RABATTSATZ] = new Double(
									0);
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ZUSATZRABATTSATZ] = new Double(
									0);
						}

						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_GESAMTPREIS] = auftragpositionDto
								.getNReportGesamtpreis();
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_MWSTSATZ] = auftragpositionDto
								.getDReportMwstsatz();
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_MATERIALZUSCHLAG] = auftragpositionDto
								.getNMaterialzuschlag();
						if (auftragpositionDto.getPositioniIdArtikelset() == null) {
							MwstsatzReportDto m = ((MwstsatzReportDto) mwstMap
									.get(auftragpositionDto.getMwstsatzIId()));
							m.setNSummeMwstbetrag(m.getNSummeMwstbetrag().add(
									auftragpositionDto
											.getNReportMwstsatzbetrag()));
							m.setNSummePositionsbetrag(m
									.getNSummePositionsbetrag().add(
											auftragpositionDto
													.getNReportGesamtpreis()));
						}

						/*
						 * if (Helper.short2Boolean(kundeDto
						 * .getBRechnungsdruckmitrabatt())) {
						 * data[index][AuftragReportFac
						 * .REPORT_AUFTRAGBESTAETIGUNG_EINZELPREIS] =
						 * aPositionDtos[i]
						 * .getNEinzelpreisplusversteckteraufschlag();
						 * 
						 * } else {data[index][AuftragReportFac.
						 * REPORT_AUFTRAGBESTAETIGUNG_EINZELPREIS] =
						 * aPositionDtos[i]
						 * .getNNettoeinzelpreisplusversteckteraufschlag(); } //
						 * Rabattsaetze nur andrucken, wenn sie nicht 0.0 sind
						 * Double ddRabattsatz = null; if
						 * (aPositionDtos[i].getFRabattsatz() != null &&
						 * aPositionDtos[i].getFRabattsatz() .doubleValue() !=
						 * 0) { ddRabattsatz =
						 * aPositionDtos[i].getFRabattsatz(); }
						 * data[index][AuftragReportFac
						 * .REPORT_AUFTRAGBESTAETIGUNG_RABATTSATZ] =
						 * ddRabattsatz;
						 * 
						 * Double ddZusatzrabattsatz = null; if
						 * (aPositionDtos[i].getFZusatzrabattsatz() != null &&
						 * aPositionDtos[i].getFZusatzrabattsatz()
						 * .doubleValue() != 0) { ddZusatzrabattsatz =
						 * aPositionDtos[i] .getFZusatzrabattsatz(); }
						 * data[index][AuftragReportFac.
						 * REPORT_AUFTRAGBESTAETIGUNG_ZUSATZRABATTSATZ] =
						 * ddZusatzrabattsatz;
						 * 
						 * BigDecimal bdGesamtpreis = null;
						 * 
						 * if (aPositionDtos[i]
						 * .getNNettoeinzelpreisplusversteckteraufschlag() !=
						 * null && aPositionDtos[i].getNMenge() != null) {
						 * bdGesamtpreis = aPositionDtos[i]
						 * .getNNettoeinzelpreisplusversteckteraufschlag()
						 * .multiply(aPositionDtos[i].getNMenge()); } // TODO
						 * SK: Gesamtpreis skaliert an Report &uuml;bergeben.
						 * data[index
						 * ][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_GESAMTPREIS
						 * ] = bdGesamtpreis;
						 * 
						 * // Mwstsatzinformationen hinterlegen MwstsatzDto
						 * mwstsatzDto = getMandantFac()
						 * .mwstsatzFindByPrimaryKey(
						 * aPositionDtos[i].getMwstsatzIId(), theClientDto);
						 * data[index][AuftragReportFac.
						 * REPORT_AUFTRAGBESTAETIGUNG_MWSTSATZ] = mwstsatzDto
						 * .getFMwstsatz();
						 * 
						 * // die Summen fuer den Mwstsatz der Position erhoehen
						 * MwstsatzReportDto mwstsatzReportDto =
						 * (MwstsatzReportDto) mwstMap
						 * .get(aPositionDtos[i].getMwstsatzIId());
						 * 
						 * // mwstsatzDto = //
						 * getMandantFac().mwstsatzFindByPrimaryKey( //
						 * aPositionDtos[i].getMwstsatzIId() // , theClientDto);
						 * if (mwstsatzDto.getFMwstsatz().doubleValue() > 0) {
						 * BigDecimal nPositionsbetrag = aPositionDtos[i]
						 * .getNNettoeinzelpreisplusversteckteraufschlag()
						 * .multiply(aPositionDtos[i].getNMenge());
						 * 
						 * BigDecimal nSummeMwstbetrag = mwstsatzReportDto
						 * .getNSummeMwstbetrag() .add( nPositionsbetrag
						 * .multiply(new BigDecimal( mwstsatzDto .getFMwstsatz()
						 * .doubleValue()) .movePointLeft(2)));
						 * 
						 * mwstsatzReportDto
						 * .setNSummeMwstbetrag(nSummeMwstbetrag);
						 * mwstsatzReportDto
						 * .setNSummePositionsbetrag(mwstsatzReportDto
						 * .getNSummePositionsbetrag().add( nPositionsbetrag));
						 * 
						 * // mwstMap.update(aPositionDtos[i //
						 * ].getMwstsatzIId(), // mwstsatzReportDto); //
						 * mwstMap.put(aPositionDtos[i]. // getMwstsatzIId(), //
						 * mwstsatzReportDto); }
						 */
						// Wenn es zu einem Artikel eine Stueckliste gibt...
						if (aPositionDtos[i].getPositionsartCNr().equals(
								AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {
							StuecklisteInfoDto stuecklisteInfoDto = getStuecklisteFac()
									.getStrukturdatenEinesArtikels(
											aPositionDtos[i].getArtikelIId(),
											true, null, // in die Rekursion mit
											// einer
											// leeren Listen einsteigen
											0, // in die Rekursion mit Ebene 0
												// einsteigen
											-1, // alle Stuecklisten komplett
											// aufloesen
											false, // Menge pro Einheit der
											// uebergeorndeten Position
											new BigDecimal(1), // fuer 1 Einheit
											// der STKL
											true, // Fremdfertigung aufloesen
											theClientDto);

							if (stuecklisteInfoDto.getIiAnzahlPositionen()
									.intValue() > 0) {
								// in die naechste Zeile schreiben, zuerst noch
								// die aktuelle fertig machen
								data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITIONSART] = aPositionDtos[i]
										.getPositionsartCNr();
								data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_SEITENUMBRUCH] = bbSeitenumbruch;

								ArrayList<?> alStuecklisteAufgeloest = stuecklisteInfoDto
										.getAlStuecklisteAufgeloest();

								Iterator<?> it = alStuecklisteAufgeloest
										.iterator();

								while (it.hasNext()) {
									index++;

									StuecklisteMitStrukturDto stuecklisteMitStrukturDto = (StuecklisteMitStrukturDto) it
											.next();

									StuecklistepositionDto stuecklistepositionDto = stuecklisteMitStrukturDto
											.getStuecklistepositionDto();

									// Pro Position eine kuenstliche Zeile zum
									// Andrucken erzeugen,
									// als Bezugsmenge gilt immer 1 Einheit der
									// Stueckliste
									data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITIONSART] = AuftragServiceFac.AUFTRAGPOSITIONART_STUECKLISTENPOSITION;
									data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_SEITENUMBRUCH] = bbSeitenumbruch;
									data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_STKLMENGE] = stuecklistepositionDto
											.getNMenge();
									data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_STKLEINHEIT] = getSystemFac()
											.formatEinheit(
													stuecklistepositionDto
															.getEinheitCNr(),
													locDruck, theClientDto);

									// Einrueckung fuer Unterstuecklisten
									String einrueckung = "";
									for (int j = 0; j < stuecklisteMitStrukturDto
											.getIEbene(); j++) {
										einrueckung = einrueckung + "    ";
									}

									String artikelCNr = null;

									// @todo boeser Workaround ... PJ 3779
									if (stuecklistepositionDto.getArtikelDto()
											.getCNr().startsWith("~")) {
										artikelCNr = AngebotReportFac.REPORT_VORKALKULATION_ZEICHEN_FUER_HANDEINGABE;
									} else {
										artikelCNr = stuecklistepositionDto
												.getArtikelDto().getCNr();
									}

									data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_STKLARTIKELCNR] = einrueckung
											+ artikelCNr;
									data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_STKLARTIKELBEZ] = getArtikelFac()
											.baueArtikelBezeichnungMehrzeilig(
													stuecklistepositionDto
															.getArtikelIId(),
													LocaleFac.POSITIONSART_IDENT,
													null, null, false,
													locDruck, theClientDto);

									if (stuecklistepositionDto.getArtikelIId() != null) {

										ArtikelDto artikelDto = getArtikelFac()
												.artikelFindByPrimaryKeySmall(
														stuecklistepositionDto
																.getArtikelIId(),
														theClientDto);

										data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_STKLARTIKELKBEZ] = artikelDto
												.getKbezAusSpr();
									}

									// KundeArtikelnr gueltig zu Belegdatum
									KundesokoDto kundeSokoDto_gueltig = this
											.getKundesokoFac()
											.kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc(
													auftragDto
															.getKundeIIdRechnungsadresse(),
													stuecklistepositionDto
															.getArtikelIId(),
													new java.sql.Date(
															auftragDto
																	.getTBelegdatum()
																	.getTime()));
									if (kundeSokoDto_gueltig != null) {
										data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_STKLARTIKEL_KDARTIKELNR] = kundeSokoDto_gueltig
												.getCKundeartikelnummer();
									}

									// PJ18038
									VkpreisfindungDto vkpreisfindungDto = getVkPreisfindungFac()
											.verkaufspreisfindung(
													stuecklistepositionDto
															.getArtikelIId(),
													auftragDto
															.getKundeIIdRechnungsadresse(),
													stuecklistepositionDto
															.getNMenge(),
													new Date(auftragDto
															.getTBelegdatum()
															.getTime()),
													kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
													aPositionDtos[i]
															.getMwstsatzIId(),
													theClientDto
															.getSMandantenwaehrung(),
													theClientDto);

									VerkaufspreisDto kundenVKPreisDto = Helper
											.getVkpreisBerechnet(vkpreisfindungDto);
									if (kundenVKPreisDto != null) {
										data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_STKLARTIKEL_KDPREIS] = kundenVKPreisDto.nettopreis;
									}

								}
							}
						}
						index++;
					}

					if (aPositionDtos[i].getPositionsartCNr().equals(
							AuftragServiceFac.AUFTRAGPOSITIONART_POSITION)) {
						if (aPositionDtos[i].getTypCNr() != null) {
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_BEZEICHNUNG] = aPositionDtos[i]
									.getCBez();
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_MENGE] = new BigDecimal(
									1);
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_EINZELPREIS] = getAuftragpositionFac()
									.getGesamtpreisPosition(
											aPositionDtos[i].getIId(),
											theClientDto);
						}
					}

					// Betrifft Positionen
					if (aPositionDtos[i].getPositionsartCNr().equals(
							AuftragServiceFac.AUFTRAGPOSITIONART_BETRIFFT)) {
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITIONSART] = aPositionDtos[i]
								.getPositionsartCNr();
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_FREIERTEXT] = aPositionDtos[i]
								.getCBez();
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_SEITENUMBRUCH] = bbSeitenumbruch;
						index++;
					}

					// Texteingabe Positionen
					if (aPositionDtos[i].getPositionsartCNr().equals(
							AuftragServiceFac.AUFTRAGPOSITIONART_TEXTEINGABE)) {
						// IMS 1619 leerer Text soll als Leerezeile erscheinen
						String sText = aPositionDtos[i].getXTextinhalt();

						if (sText != null && sText.trim().equals("")) {
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_LEERZEILE] = " ";
						} else {
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_FREIERTEXT] = sText;
						}
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_SEITENUMBRUCH] = bbSeitenumbruch;
						index++;
					}

					// Textbaustein Positionen
					if (aPositionDtos[i].getPositionsartCNr().equals(
							AuftragServiceFac.AUFTRAGPOSITIONART_TEXTBAUSTEIN)) {
						// Dto holen
						MediastandardDto oMediastandardDto = getMediaFac()
								.mediastandardFindByPrimaryKey(
										aPositionDtos[i].getMediastandardIId());
						// zum Drucken vorbereiten
						BelegPositionDruckTextbausteinDto druckDto = printTextbaustein(
								oMediastandardDto, theClientDto);
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_FREIERTEXT] = druckDto
								.getSFreierText();
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_IMAGE] = druckDto
								.getOImage();
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_SEITENUMBRUCH] = bbSeitenumbruch;
						index++;
					}

					// Leerzeile Positionen
					if (aPositionDtos[i].getPositionsartCNr().equals(
							AuftragServiceFac.AUFTRAGPOSITIONART_LEERZEILE)) {
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_LEERZEILE] = " ";
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_SEITENUMBRUCH] = bbSeitenumbruch;
						index++;
					}

					if (aPositionDtos[i]
							.getPositionsartCNr()
							.equals(AuftragServiceFac.AUFTRAGPOSITIONART_INTELLIGENTE_ZWISCHENSUMME)) {
						befuelleZeileABMitIntelligenterZwischensumme(index,
								auftragDto, aPositionDtos[i], mwstMap,
								bbSeitenumbruch, kundeDtoRechnungsadresse,
								locDruck, theClientDto);
						// updateZwischensummenData(index,
						// aPositionDtos[i].getZwsVonPosition(),
						// aPositionDtos[i].getCBez(),
						// aPositionDtos[i].getZwsNettoSumme());
						updateZwischensummenData(index, aPositionDtos[i]);
						++index;
					}

					// Seitenumbruch Positionen
					if (aPositionDtos[i].getPositionsartCNr().equals(
							AuftragServiceFac.AUFTRAGPOSITIONART_SEITENUMBRUCH)) {
						bbSeitenumbruch = new Boolean(
								!bbSeitenumbruch.booleanValue()); // toggle
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_SEITENUMBRUCH] = bbSeitenumbruch;
						index++;
					}

					else if (aPositionDtos[i].getPositionsartCNr().equals(
							AngebotServiceFac.ANGEBOTPOSITIONART_ENDSUMME)) {
						// SP1581
						if (auftragDto.getNKorrekturbetrag() != null
								&& auftragDto.getNKorrekturbetrag()
										.doubleValue() != 0) {
							// Handeingabe hinzufuegen
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITIONSART] = AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE;
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_IDENTNUMMER] = "Pauschalkorrektur";
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_BEZEICHNUNG] = "Pauschalkorrektur";
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_IDENT_TEXTEINGABE] = "Pauschalkorrektur";
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION_TITEL] = "Pauschalkorrektur";
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_MENGE] = new BigDecimal(
									1);
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_EINHEIT] = "x";
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_EINZELPREIS] = auftragDto
									.getNKorrekturbetrag();
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_GESAMTPREIS] = auftragDto
									.getNKorrekturbetrag();
							data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_SEITENUMBRUCH] = bbSeitenumbruch;

							MwstsatzDto mwstsatzDto = getMandantFac()
									.mwstsatzFindByMwstsatzbezIIdAktuellster(
											kundeDto.getMwstsatzbezIId(),
											theClientDto);

							BigDecimal ust = auftragDto.getNKorrekturbetrag()
									.multiply(
											new BigDecimal(mwstsatzDto
													.getFMwstsatz())
													.movePointLeft(2));

							MwstsatzReportDto m = ((MwstsatzReportDto) mwstMap
									.get(mwstsatzDto.getIId()));
							m.setNSummeMwstbetrag(m.getNSummeMwstbetrag().add(
									ust));
							m.setNSummePositionsbetrag(m
									.getNSummePositionsbetrag().add(
											auftragDto.getNKorrekturbetrag()));
							index++;

						}
						korrekturbetragHinzugefuegt = true;
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITIONSART] = aPositionDtos[i]
								.getPositionsartCNr();
						data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_SEITENUMBRUCH] = bbSeitenumbruch;
						index++;
					}

					/*
					 * 
					 * // Positionsart setzen, wenn es keine kuenstliche
					 * Position // ist if(data[index][AuftragReportFac.
					 * REPORT_AUFTRAGBESTAETIGUNG_POSITIONSART] == null) {
					 * data[index
					 * ][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITIONSART
					 * ] = aPositionDtos[i] .getAuftragpositionartCNr(); }
					 */
				}

			}
			// SP1581
			if (!korrekturbetragHinzugefuegt
					&& auftragDto.getNKorrekturbetrag() != null
					&& auftragDto.getNKorrekturbetrag().doubleValue() != 0) {
				// Handeingabe hinzufuegen
				data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITIONSART] = AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE;
				data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_IDENTNUMMER] = "Pauschalkorrektur";
				data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_BEZEICHNUNG] = "Pauschalkorrektur";
				data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_IDENT_TEXTEINGABE] = "Pauschalkorrektur";
				data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION_TITEL] = "Pauschalkorrektur";
				data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_MENGE] = new BigDecimal(
						1);
				data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_EINHEIT] = "x";
				data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_EINZELPREIS] = auftragDto
						.getNKorrekturbetrag();
				data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_GESAMTPREIS] = auftragDto
						.getNKorrekturbetrag();
				data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_SEITENUMBRUCH] = bbSeitenumbruch;

				MwstsatzDto mwstsatzDto = getMandantFac()
						.mwstsatzFindByMwstsatzbezIIdAktuellster(
								kundeDto.getMwstsatzbezIId(), theClientDto);

				BigDecimal ust = auftragDto.getNKorrekturbetrag().multiply(
						new BigDecimal(mwstsatzDto.getFMwstsatz())
								.movePointLeft(2));

				MwstsatzReportDto m = ((MwstsatzReportDto) mwstMap
						.get(mwstsatzDto.getIId()));
				m.setNSummeMwstbetrag(m.getNSummeMwstbetrag().add(ust));
				m.setNSummePositionsbetrag(m.getNSummePositionsbetrag().add(
						auftragDto.getNKorrekturbetrag()));

			}

			// das Andrucken der gesammelten Mwstinformationen steuern
			StringBuffer sbMwstsatz = new StringBuffer();
			StringBuffer sbSummePositionsbetrag = new StringBuffer();
			StringBuffer sbWaehrung = new StringBuffer();
			StringBuffer sbSummeMwstbetrag = new StringBuffer();

			// UW 03.03.06: Die folgenden Informationen erscheinen am Druck, mit
			// diesen
			// Werten wird intern nicht mehr weitergerechnet -> daher auf 2
			// Stellen runden
			BigDecimal nAuftragsendbetragMitMwst = Helper.rundeKaufmaennisch(
					auftragDto.getNGesamtauftragswertInAuftragswaehrung(), 2);

			boolean bHatMwstWerte = false;

			for (Iterator<Object> iter = mwstMap.keySet().iterator(); iter
					.hasNext();) {
				Integer key = (Integer) iter.next(); // IId des Mwstsatzes
				MwstsatzReportDto mwstsatzReportDto = (MwstsatzReportDto) mwstMap
						.get(key); // Summen der Mwstbetraege

				if (mwstsatzReportDto != null
						&& mwstsatzReportDto.getNSummeMwstbetrag()
								.doubleValue() > 0.0) {
					MwstsatzDto mwstsatzDto = getMandantFac()
							.mwstsatzFindByPrimaryKey(key, theClientDto);
					BigDecimal bdSummePositionBetragMitRabatten = mwstsatzReportDto
							.getNSummePositionsbetragMinusRabatte(
									auftragDto.getFAllgemeinerRabattsatz(),
									auftragDto.getFProjektierungsrabattsatz());
					BigDecimal bdSummeMWSTBetrag = mwstsatzReportDto
							.getNSummeMWSTbetragMinusRabatte(
									auftragDto.getFAllgemeinerRabattsatz(),
									auftragDto.getFProjektierungsrabattsatz(),
									mwstsatzDto.getFMwstsatz());
					// MR: FIX, statt festverdrahtetem UST verwende
					// Localeabhaengigen Wert lp.ust
					sbMwstsatz.append(getTextRespectUISpr("lp.ust",
							theClientDto.getMandant(), locDruck));
					sbMwstsatz.append(": ");
					sbMwstsatz.append(Helper.formatZahl(
							mwstsatzDto.getFMwstsatz(), 2, locDruck));
					sbMwstsatz.append(" % ");
					sbMwstsatz.append(
							getTextRespectUISpr("lp.ustvon",
									theClientDto.getMandant(), locDruck))
							.append(" ");
					// Fix Ende
					sbSummePositionsbetrag.append(Helper.formatZahl(
							bdSummePositionBetragMitRabatten, 2, locDruck));
					sbWaehrung.append(auftragDto.getCAuftragswaehrung());
					sbSummeMwstbetrag.append(Helper.formatZahl(
							bdSummeMWSTBetrag, 2, locDruck));

					sbMwstsatz.append("\n");
					sbSummePositionsbetrag.append("\n");
					sbWaehrung.append("\n");
					sbSummeMwstbetrag.append("\n");

					if (nAuftragsendbetragMitMwst != null) {
						nAuftragsendbetragMitMwst = nAuftragsendbetragMitMwst
								.add(Helper.rundeKaufmaennisch(
										bdSummeMWSTBetrag, 2));
					}
					bHatMwstWerte = true;
				}
			}

			if (bHatMwstWerte) {
				// die letzten \n wieder loeschen
				sbMwstsatz.delete(sbMwstsatz.length() - 1, sbMwstsatz.length());
				sbSummePositionsbetrag.delete(
						sbSummePositionsbetrag.length() - 1,
						sbSummePositionsbetrag.length());
				sbWaehrung.delete(sbWaehrung.length() - 1, sbWaehrung.length());
				sbSummeMwstbetrag.delete(sbSummeMwstbetrag.length() - 1,
						sbSummeMwstbetrag.length());
			}

			parameter.put("P_MWST_TABELLE_LINKS", sbMwstsatz.toString());
			parameter.put("P_MWST_TABELLE_SUMME_POSITIONEN",
					sbSummePositionsbetrag.toString());
			parameter.put("P_MWST_TABELLE_WAEHRUNG", sbWaehrung.toString());
			parameter
					.put("P_MWST_TABELLE_RECHTS", sbSummeMwstbetrag.toString());
			parameter.put("P_AUFTRAGSENDBETRAGMITMWST",
					nAuftragsendbetragMitMwst);
			parameter.put("P_POENALE",
					Helper.short2Boolean(auftragDto.getBPoenale()));
			parameter
					.put("P_ROHS", Helper.short2Boolean(auftragDto.getBRoHs()));
			// 13273
			if (auftragDto.getAngebotIId() != null) {
				parameter.put(
						"P_ANGEBOTSNUMMER",
						getAngebotFac().angebotFindByPrimaryKey(
								auftragDto.getAngebotIId(), theClientDto)
								.getCNr());
			}

			parameter.put("P_ZUSAMMENFASSUNG",
					Helper.short2Boolean(auftragDto.getBMitzusammenfassung()));

			buildVersandwegParam(theClientDto, auftragDto, locDruck, parameter);

			// PJ17046 Wenn 'mit Zusammenfassung' dann Positionsdaten
			// duplizieren
			data = duplizierePositionenWegenZusammenfassung(auftragDto, data,
					AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITIONSART);

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

				String reportName = AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG;
				if (sReportname != null) {
					reportName = sReportname;
				}

				initJRDS(parameter, AuftragReportFac.REPORT_MODUL, reportName,
						theClientDto.getMandant(), locDruck, theClientDto,
						bMitLogo.booleanValue(), auftragDto.getKostIId());

				aJasperPrint[iKopieNummer] = getReportPrint();
			}

			PrintInfoDto values = getJCRDocFac().getPathAndPartnerAndTable(
					auftragDto.getIId(), QueryParameters.UC_ID_AUFTRAG,
					theClientDto);
			aJasperPrint[0].setOInfoForArchive(values);
			aJasperPrint[0].putAdditionalInformation(
					JasperPrintLP.KEY_BELEGART, LocaleFac.BELEGART_AUFTRAG);
			aJasperPrint[0].putAdditionalInformation(
					JasperPrintLP.KEY_BELEGIID, auftragDto.getIId());

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return aJasperPrint;
	}

	private void buildVersandwegParam(TheClientDto theClientDto,
			AuftragDto auftragDto, Locale locDruck,
			HashMap<String, Object> parameter) throws RemoteException {
		parameter.put(
				"P_VERSANDWEG_BESTAETIGUNG_TERMIN",
				auftragDto.getTResponse() == null ? null : Helper.formatDatum(
						auftragDto.getTResponse(), locDruck));
		boolean hasVersandweg = getAuftragFac().hatAuftragVersandweg(
				auftragDto, theClientDto);
		parameter.put("P_VERSANDWEG", new Boolean(hasVersandweg));
	}

	private void befuelleZeileABMitIntelligenterZwischensumme(int index,
			AuftragDto auftragDto, AuftragpositionDto aPositionDto,
			LinkedHashMap<Object, MwstsatzReportDto> mwstMap,
			Boolean bbSeitenumbruch, KundeDto kundeDto, Locale locDruck,
			TheClientDto theClientDto) throws RemoteException {
		data[index][REPORT_AUFTRAGBESTAETIGUNG_INTERNAL_IID] = aPositionDto
				.getIId();
		data[index][REPORT_AUFTRAGBESTAETIGUNG_POSITIONSART] = aPositionDto
				.getPositionsartCNr();
		data[index][REPORT_AUFTRAGBESTAETIGUNG_VONPOSITION] = getAuftragpositionFac()
				.getPositionNummer(aPositionDto.getZwsVonPosition());
		data[index][REPORT_AUFTRAGBESTAETIGUNG_BISPOSITION] = getAuftragpositionFac()
				.getPositionNummer(aPositionDto.getZwsBisPosition());
		data[index][REPORT_AUFTRAGBESTAETIGUNG_ZWSNETTOSUMME] = aPositionDto
				.getZwsNettoSumme();
		data[index][REPORT_AUFTRAGBESTAETIGUNG_ZWSPOSPREISDRUCKEN] = aPositionDto
				.getBZwsPositionspreisZeigen();
		data[index][REPORT_AUFTRAGBESTAETIGUNG_BEZEICHNUNG] = aPositionDto
				.getCBez();

		data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION] = getAuftragpositionFac()
				.getPositionNummer(aPositionDto.getIId());
		data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_SEITENUMBRUCH] = bbSeitenumbruch;

		data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_MENGE] = aPositionDto
				.getNMenge();

		data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_EINHEIT] = aPositionDto
				.getEinheitCNr() == null ? null : getSystemFac().formatEinheit(
				aPositionDto.getEinheitCNr(), locDruck, theClientDto);
		int iNachkommastellenPreis = getUINachkommastellenPreisVK(auftragDto
				.getMandantCNr());
		AuftragpositionDto auftragpositionDto = (AuftragpositionDto) getBelegVerkaufFac()
				.getBelegpositionVerkaufReport(aPositionDto, auftragDto,
						iNachkommastellenPreis);
		if (Helper.short2Boolean(kundeDto.getBRechnungsdruckmitrabatt())) {
			data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_EINZELPREIS] = auftragpositionDto
					.getNReportEinzelpreisplusversteckteraufschlag();
		} else {
			data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_EINZELPREIS] = auftragpositionDto
					.getNReportNettoeinzelpreisplusversteckteraufschlag();
		}
		data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_RABATTSATZ] = auftragpositionDto
				.getDReportRabattsatz();
		data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ZUSATZRABATTSATZ] = auftragpositionDto
				.getDReportZusatzrabattsatz();
		data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_GESAMTPREIS] = auftragpositionDto
				.getNReportGesamtpreis();
		data[index][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_MWSTSATZ] = auftragpositionDto
				.getDReportMwstsatz();
		if (auftragpositionDto.getPositioniIdArtikelset() == null) {
			MwstsatzReportDto m = ((MwstsatzReportDto) mwstMap
					.get(auftragpositionDto.getMwstsatzIId()));
			m.setNSummeMwstbetrag(m.getNSummeMwstbetrag().add(
					auftragpositionDto.getNReportMwstsatzbetrag()));
			m.setNSummePositionsbetrag(m.getNSummePositionsbetrag().add(
					auftragpositionDto.getNReportGesamtpreis()));
		}
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
	 *            die Bezeichnung der Zwischensumme
	 * @param nettoSumme
	 *            die Summe der Zwischensummenpositionen
	 */
	private void updateZwischensummenData(int lastIndex,
			Integer zwsVonPosition, String cBez, BigDecimal nettoSumme) {
		for (int i = 0; i < lastIndex; i++) {
			Object[] o = (Object[]) data[i];
			if (zwsVonPosition
					.equals(o[REPORT_AUFTRAGBESTAETIGUNG_INTERNAL_IID])) {
				if (null == o[REPORT_AUFTRAGBESTAETIGUNG_ZWSTEXT]) {
					o[REPORT_AUFTRAGBESTAETIGUNG_ZWSTEXT] = cBez;
				} else {
					String s = (String) o[REPORT_AUFTRAGBESTAETIGUNG_ZWSTEXT]
							+ "\n" + cBez;
					o[REPORT_AUFTRAGBESTAETIGUNG_ZWSTEXT] = s;
				}

				o[REPORT_AUFTRAGBESTAETIGUNG_ZWSNETTOSUMME] = nettoSumme;
				return;
			}
		}
	}

	private void updateZwischensummenData(int lastIndex,
			AuftragpositionDto zwsPos) {
		Integer zwsVonPosition = zwsPos.getZwsVonPosition();
		if (zwsVonPosition == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_POSITION_ZWISCHENSUMME_UNVOLLSTAENDIG,
					"Position '" + zwsPos.getCBez() + "' unvollst\u00E4ndig",
					new Object[] { zwsPos.getCBez(), zwsPos.getIId() });
		}

		int foundIndex = -1;
		for (int i = 0; i < lastIndex; i++) {
			Object[] o = (Object[]) data[i];
			if (zwsVonPosition
					.equals(o[REPORT_AUFTRAGBESTAETIGUNG_INTERNAL_IID])) {
				if (null == o[REPORT_AUFTRAGBESTAETIGUNG_ZWSTEXT]) {
					o[REPORT_AUFTRAGBESTAETIGUNG_ZWSTEXT] = zwsPos.getCBez();
				} else {
					String s = (String) o[REPORT_AUFTRAGBESTAETIGUNG_ZWSTEXT]
							+ "\n" + zwsPos.getCBez();
					o[REPORT_AUFTRAGBESTAETIGUNG_ZWSTEXT] = s;
				}

				o[REPORT_AUFTRAGBESTAETIGUNG_ZWSNETTOSUMME] = zwsPos
						.getZwsNettoSumme();
				foundIndex = i;
				break;
			}
		}

		if (foundIndex == -1) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_POSITION_ZWISCHENSUMME_UNVOLLSTAENDIG,
					"Position '" + zwsPos.getCBez() + "' unvollst\u00E4ndig",
					new Object[] { zwsPos.getCBez(), zwsPos.getIId() });
		}

		for (int i = foundIndex; i < lastIndex; i++) {
			Object[] o = (Object[]) data[i];
			o[REPORT_AUFTRAGBESTAETIGUNG_ZWSPOSPREISDRUCKEN] = zwsPos
					.getBZwsPositionspreisZeigen();
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printProjektblatt(Integer auftragIId,
			TheClientDto theClientDto) {

		cAktuellerReport = AuftragReportFac.REPORT_AUFTRAG_PROJEKTBLATT;
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		try {

			// Auftragskopfdaten

			// die Parameter dem Report uebergeben

			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(
					auftragIId);

			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					auftragDto.getKundeIIdAuftragsadresse(), theClientDto);

			AnsprechpartnerDto ansprechpartnerDto = null;

			if (auftragDto.getAnsprechparnterIId() != null) {
				ansprechpartnerDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(
								auftragDto.getAnsprechparnterIId(),
								theClientDto);
			}

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);

			Locale clientLocale = theClientDto.getLocUi();

			parameter.put(
					"P_ADRESSE_FUER_AUSDRUCK",
					formatAdresseFuerAusdruck(kundeDto.getPartnerDto(),
							ansprechpartnerDto, mandantDto, clientLocale,
							LocaleFac.BELEGART_AUFTRAG));

			if (ansprechpartnerDto != null) {
				parameter.put(
						"P_ANSPRECHPARTNER_ADRESSBLOCK",
						getPartnerFac()
								.formatFixAnredeTitelName2Name1FuerAdresskopf(
										ansprechpartnerDto.getPartnerDto(),
										clientLocale, null));
			}

			SpediteurDto spediteurDto = getMandantFac()
					.spediteurFindByPrimaryKey(auftragDto.getSpediteurIId());
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
								theClientDto.getLocUi()));
			}

			// die Lieferart
			parameter.put(
					"P_LIEFERART",
					getLocaleFac().lieferartFindByIIdLocaleOhneExc(
							auftragDto.getLieferartIId(), clientLocale,
							theClientDto));

			parameter.put("P_LIEFERTERMIN", auftragDto.getDLiefertermin());
			parameter.put("P_FINALTERMIN", auftragDto.getDFinaltermin());
			parameter.put("P_BESTELLDATUM", auftragDto.getDBestelldatum());
			parameter.put("P_AUFTRAGSNUMMER", auftragDto.getCNr());
			parameter
					.put("P_ROHS", Helper.short2Boolean(auftragDto.getBRoHs()));
			parameter.put("P_BESTELLNUMMER", auftragDto.getCBestellnummer());

			parameter.put("P_PROJEKTBEZEICHNUNG",
					auftragDto.getCBezProjektbezeichnung()); // ());

			if (auftragDto.getKostIId() != null) {
				KostenstelleDto kostenstelleDto = getSystemFac()
						.kostenstelleFindByPrimaryKey(auftragDto.getKostIId());
				parameter.put(LPReport.P_KOSTENSTELLE,
						kostenstelleDto.formatKostenstellenbezeichnung());
			}

			KundeDto kundeDtoLieferadresse = getKundeFac()
					.kundeFindByPrimaryKey(
							auftragDto.getKundeIIdLieferadresse(), theClientDto);
			parameter.put(
					"P_LIEFERADRESSE",
					formatAdresseFuerAusdruck(
							kundeDtoLieferadresse.getPartnerDto(),
							ansprechpartnerDto, mandantDto, clientLocale));
			parameter.put("P_LIEFERDAUER",
					kundeDtoLieferadresse.getILieferdauer());

			// Subreport Zeitplan

			Session session = FLRSessionFactory.getFactory().openSession();

			String queryString = "SELECT zp FROM FLRZeitplan zp WHERE zp.flrauftrag.i_id= "
					+ auftragIId +" ORDER BY zp.i_termin_vor_liefertermin DESC";

			org.hibernate.Query query = session.createQuery(queryString);
			List<?> resultList = query.list();

			Iterator<?> resultListIterator = resultList.iterator();

			String[] fieldnamesZeitplan = new String[] { "Termin", "Material",
					"Dauer", "Kommentar", "KommentarLang" };

			ArrayList<Object[]> alZeitplan = new ArrayList<Object[]>();

			while (resultListIterator.hasNext()) {
				FLRZeitplan zp = (FLRZeitplan) resultListIterator.next();

				Object[] oZeile = new Object[5];
				oZeile[0] = Helper.addiereTageZuDatum(
						auftragDto.getDLiefertermin(),
						-zp.getI_termin_vor_liefertermin());
				oZeile[1] = zp.getN_material();
				oZeile[2] = zp.getN_dauer();
				oZeile[3] = zp.getC_kommentar();
				oZeile[4] = zp.getX_text();
				alZeitplan.add(oZeile);

			}
			session.close();
			Object[][] dataSub = new Object[alZeitplan.size()][fieldnamesZeitplan.length];
			dataSub = (Object[][]) alZeitplan.toArray(dataSub);

			parameter.put("P_SUBREPORT_ZEITPLAN", new LPDatenSubreport(dataSub,
					fieldnamesZeitplan));

			session = FLRSessionFactory.getFactory().openSession();

			queryString = "SELECT zp FROM FLRZahlungsplan zp WHERE zp.flrauftrag.i_id= "
					+ auftragIId +" ORDER BY zp.i_tage_vor_liefertermin DESC";;

			query = session.createQuery(queryString);
			resultList = query.list();

			resultListIterator = resultList.iterator();

			String[] fieldnames = new String[] { "Termin", "Betrag",
					"SubreportMeilenstein", };

			ArrayList<Object[]> alZeichnung = new ArrayList<Object[]>();

			while (resultListIterator.hasNext()) {
				FLRZahlungsplan zp = (FLRZahlungsplan) resultListIterator
						.next();

				Object[] oZeile = new Object[3];
				oZeile[0] = Helper.addiereTageZuDatum(
						auftragDto.getDLiefertermin(),
						-zp.getI_tage_vor_liefertermin());
				oZeile[1] = zp.getN_betrag();

				if (zp.getFlrzahlungsplanmeilenstein().size() > 0) {
					String[] fieldnamesMeilenstein = new String[] {
							"Meilenstein", "Kommentar", "KommentarLang" };
					ArrayList<Object[]> alMeilensteine = new ArrayList<Object[]>();
					Iterator it = zp.getFlrzahlungsplanmeilenstein().iterator();
					while (it.hasNext()) {
						FLRZahlungsplanmeilenstein zpm = (FLRZahlungsplanmeilenstein) it
								.next();
						zpm.getFlrmeilenstein().getC_nr();
						Object[] oZeileMeilsentein = new Object[3];

						oZeileMeilsentein[0] = zpm.getFlrmeilenstein()
								.getC_nr();

						oZeileMeilsentein[1] = zpm.getC_kommentar();
						oZeileMeilsentein[2] = zpm.getX_text();

						alMeilensteine.add(oZeileMeilsentein);

					}

					Object[][] dataSubMeilenstein = new Object[alMeilensteine
							.size()][fieldnamesMeilenstein.length];
					dataSubMeilenstein = (Object[][]) alMeilensteine
							.toArray(dataSubMeilenstein);
					oZeile[2] = new LPDatenSubreport(dataSubMeilenstein,
							fieldnamesMeilenstein);

				}

				alZeichnung.add(oZeile);

			}
			session.close();
			dataSub = new Object[alZeichnung.size()][fieldnames.length];
			dataSub = (Object[][]) alZeichnung.toArray(dataSub);

			parameter.put("P_SUBREPORT_ZAHLUNGSPLAN", new LPDatenSubreport(
					dataSub, fieldnames));
			// Subreport Zahlungsplan
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		data = new Object[0][0];

		initJRDS(parameter, AuftragReportFac.REPORT_MODUL, cAktuellerReport,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();

	}

	/*
	 * public JasperPrintLP[] printAuftragbestaetigung(Integer iIdAuftragI,
	 * Integer iAnzahlKopienI, Boolean bMitLogo, TheClientDto theClientDto)
	 * throws EJBExceptionLP { checkAuftragIId(iIdAuftragI);
	 * 
	 * JasperPrintLP[] aJasperPrint = null;
	 * 
	 * try { AuftragDto auftragDto =
	 * getAuftragFac().auftragFindByPrimaryKey(iIdAuftragI);
	 * 
	 * AuftragpositionDto[] aPositionDtos = getAuftragpositionFac().
	 * auftragpositionFindByAuftrag(iIdAuftragI);
	 * 
	 * // Erstellung des Report index = -1; cAktuellerReport =
	 * AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG; int iArtikelpositionsnummer
	 * = 1; Boolean bbSeitenumbruch = new Boolean(false); // dieser Wert
	 * wechselt mit jedem Seitenumbruch zwischen true und false
	 * 
	 * // es gilt das Locale des Auftragskunden KundeDto kundeDto =
	 * getKundeFac().kundeFindByPrimaryKey(auftragDto.
	 * getKundeIIdAuftragsadresse(), theClientDto);
	 * 
	 * Locale locDruck = Helper.string2Locale(kundeDto.getPartnerDto().
	 * getLocaleCNrKommunikation());
	 * 
	 * AnsprechpartnerDto ansprechpartnerDto = null;
	 * 
	 * if (auftragDto.getAnsprechparnterIId() != null) { ansprechpartnerDto =
	 * getAnsprechpartnerFac().ansprechpartnerFindByPrimaryKey(
	 * auftragDto.getAnsprechparnterIId(), theClientDto); }
	 * 
	 * MandantDto mandantDto =
	 * getMandantFac().mandantFindByPrimaryKey(theClientDto. getMandant(),
	 * theClientDto);
	 * 
	 * // fuer das Andrucken der Mwstsatze wird eine Map vorbereitet, die einen
	 * // Eintrag fuer jeden Mwstsatz des Mandanten enthaelt final Set
	 * mwstSatzKeys = getMandantFac().mwstsatzIIdFindAllByMandant(auftragDto.
	 * getMandantCNr(), theClientDto); LinkedHashMap mwstMap = new
	 * LinkedHashMap(); for (Iterator iter = mwstSatzKeys.iterator();
	 * iter.hasNext(); ) { Object item = (Object) iter.next(); MwstsatzReportDto
	 * mwstsatzReportDto = new MwstsatzReportDto(); mwstMap.put(item,
	 * mwstsatzReportDto); }
	 * 
	 * // Kurzzeichenkombi zum Andrucken zusammenbauen PersonalDto
	 * oPersonalBenutzer = getPersonalFac().personalFindByPrimaryKey(
	 * theClientDto.getIDPersonal(), theClientDto); PersonalDto oPersonalAnleger
	 * = getPersonalFac().personalFindByPrimaryKey(
	 * auftragDto.getPersonalIIdAnlegen(), theClientDto); String
	 * sKurzzeichenkombi = Helper.getKurzzeichenkombi(
	 * oPersonalBenutzer.getCKurzzeichen(), oPersonalAnleger.getCKurzzeichen());
	 * 
	 * // Anrede fuer den Geschaeftsfuehrer zusammenbauen PersonalDto[]
	 * aGeschaeftsfDto = getPersonalFac().
	 * personalFindByMandantCNrPersonalfunktionCNr( theClientDto.getMandant(),
	 * PersonalFac.PERSONALFUNKTION_GESCHAEFTSFUEHRER); String
	 * sAnredeGeschaeftsfuehrer = null; if (aGeschaeftsfDto != null &&
	 * aGeschaeftsfDto.length > 0) { if (aGeschaeftsfDto[0] != null) {
	 * sAnredeGeschaeftsfuehrer = getPartnerFac().partnerFindByPrimaryKey(
	 * aGeschaeftsfDto[0].getPartnerIId(), theClientDto). formatAnrede(); //
	 * 
	 * @todo anredeGeschaeftsfueherer =
	 * aGeschaeftsfDto[0].getPartnerDto().formatAnredeName2Name1(); PJ 3780 } }
	 * 
	 * AuftragtextDto auftragtextDto = null;
	 * 
	 * // Kopftext String sKopftext = auftragDto.getCKopftextUebersteuert();
	 * 
	 * if (sKopftext == null || sKopftext.length() == 0) { if
	 * (auftragDto.getAuftragIIdKopftext() != null) { auftragtextDto =
	 * getAuftragServiceFac().auftragtextFindByMandantLocaleCNr(theClientDto.
	 * getMandant(), kundeDto.getPartnerDto(). getLocaleCNrKommunikation(),
	 * AuftragServiceFac.AUFTRAGTEXT_KOPFTEXT, theClientDto); }
	 * 
	 * sKopftext = auftragtextDto.getCTextinhalt(); }
	 * 
	 * // Fusstext String sFusstext = auftragDto.getCFusstextUebersteuert();
	 * 
	 * if (sFusstext == null || sFusstext.length() == 0) { if
	 * (auftragDto.getAuftragIIdFusstext() != null) { auftragtextDto =
	 * getAuftragServiceFac().auftragtextFindByMandantLocaleCNr(
	 * theClientDto.getMandant(), kundeDto.getPartnerDto().
	 * getLocaleCNrKommunikation(), AuftragServiceFac.AUFTRAGTEXT_FUSSTEXT,
	 * theClientDto);
	 * 
	 * }
	 * 
	 * sFusstext = auftragtextDto.getCTextinhalt(); }
	 * 
	 * // dem Report seine Parameter setzen HashMap parameter = new HashMap();
	 * 
	 * parameter.put(P_MANDANTADRESSE, Helper.formatMandantAdresse(mandantDto));
	 * parameter.put("P_KUNDE_ADRESSBLOCK",
	 * formatAdresseFuerAusdruck(kundeDto.getPartnerDto(), ansprechpartnerDto,
	 * mandantDto, locDruck)); parameter.put("P_KUNDE_UID",
	 * kundeDto.getPartnerDto().getCUid()); if (kundeDto.getIidDebitorenkonto()
	 * != null) { KontoDto kontoDto =
	 * getFinanzFac().kontoFindByPrimaryKey(kundeDto. getIidDebitorenkonto());
	 * parameter.put("P_KUNDE_DEBITORENKONTO", kontoDto.getCNr()); }
	 * parameter.put("P_KUNDE_KUNDENNUMMER",kundeDto.getIKundennummer());
	 * parameter.put("P_KOPFTEXT", Helper.formatStyledTextForJasper(sKopftext));
	 * 
	 * // die Rabatte sollen nicht sichtbar sein, wenn keine vergeben wurden
	 * boolean bDruckeRabattspalte = true;
	 * parameter.put("P_RABATTSPALTE_DRUCKEN", new
	 * Boolean(bDruckeRabattspalte)); boolean bDruckeZusatzrabattspalte = true;
	 * parameter.put("P_ZUSATZRABATTSPALTE_DRUCKEN", new
	 * Boolean(bDruckeZusatzrabattspalte));
	 * 
	 * PersonalDto vertreterDto = null; String cVertreterAnredeShort = null;
	 * 
	 * if (auftragDto.getPersonalIIdVertreter() != null) { vertreterDto =
	 * getPersonalFac().personalFindByPrimaryKey(
	 * auftragDto.getPersonalIIdVertreter(), theClientDto);
	 * cVertreterAnredeShort =
	 * vertreterDto.getPartnerDto().formatFixName2Name1();
	 * 
	 * if(vertreterDto!=null){ //Vertreter Kontaktdaten String sVertreterEmail =
	 * null; if (vertreterDto.getPartnerkommunikationDtoEmail() != null) {
	 * sVertreterEmail =
	 * vertreterDto.getPartnerkommunikationDtoEmail().getCInhalt(); } String
	 * sVertreterFaxDirekt = null; if
	 * (vertreterDto.getPartnerkommunikationDtoDirektfax() != null) {
	 * sVertreterFaxDirekt =
	 * vertreterDto.getPartnerkommunikationDtoDirektfax().getCInhalt(); } String
	 * sVertreterFax = null; if (vertreterDto.getPartnerkommunikationDtoFax() !=
	 * null) { sVertreterFax =
	 * vertreterDto.getPartnerkommunikationDtoFax().getCInhalt(); } String
	 * sVertreterTelefon = null; if
	 * (vertreterDto.getPartnerkommunikationDtoTelefon() != null) {
	 * sVertreterTelefon =
	 * vertreterDto.getPartnerkommunikationDtoTelefon().getCInhalt(); }
	 * parameter.put(LPReport.P_VERTRETEREMAIL, sVertreterEmail != null ?
	 * sVertreterEmail : ""); if (sVertreterFaxDirekt != null &&
	 * sVertreterFaxDirekt != "") { parameter.put(LPReport.P_VERTRETERFAX,
	 * sVertreterFaxDirekt); } else { parameter.put(LPReport.P_VERTRETERFAX,
	 * sVertreterFax != null ? sVertreterFax : ""); }
	 * parameter.put(LPReport.P_VERTRETEERTELEFON, sVertreterTelefon != null ?
	 * sVertreterTelefon : "");
	 * 
	 * } }
	 * 
	 * parameter.put("P_VERTRETER_ANREDE", cVertreterAnredeShort);
	 * parameter.put(LPReport.P_UNSERZEICHEN, sKurzzeichenkombi);
	 * 
	 * parameter.put("P_BELEGDATUM",
	 * Helper.formatDatum(auftragDto.getDBelegdatum(), locDruck));
	 * parameter.put("P_BESTELLNUMMER", auftragDto.getCBestellnummer());
	 * parameter.put("P_BEZEICHNUNG", auftragDto. getCBezProjektbezeichnung());
	 * 
	 * parameter.put("P_LIEFERANTENNR", kundeDto.getCLieferantennr());
	 * 
	 * String cBriefanrede = "";
	 * 
	 * if (ansprechpartnerDto != null) { cBriefanrede =
	 * getPartnerServicesFac().getBriefanredeFuerBeleg(
	 * auftragDto.getAnsprechparnterIId(), kundeDto.getPartnerIId(), locDruck,
	 * theClientDto); } else { // neutrale Anrede cBriefanrede =
	 * getTextRespectUISpr("lp.anrede.sehrgeehrtedamenundherren",
	 * theClientDto.getMandant(), locDruck); }
	 * 
	 * if (auftragDto.getKundeIIdLieferadresse() != null) { KundeDto
	 * lieferadresseKunde = getKundeFac().kundeFindByPrimaryKey(auftragDto.
	 * getKundeIIdLieferadresse(), theClientDto); AnsprechpartnerDto akunde =
	 * null; if (lieferadresseKunde.getAnsprechpartnerDto() != null) {
	 * AnsprechpartnerDto[] anspkunde =
	 * lieferadresseKunde.getAnsprechpartnerDto(); akunde = anspkunde[0]; }
	 * parameter.put("P_LIEFERADRESSE",
	 * formatAdresseFuerAusdruck(lieferadresseKunde.getPartnerDto(), akunde,
	 * mandantDto, locDruck)); if (lieferadresseKunde.getIidDebitorenkonto() !=
	 * null) { KontoDto kontoDto =
	 * getFinanzFac().kontoFindByPrimaryKey(lieferadresseKunde.
	 * getIidDebitorenkonto()); parameter.put("P_DEBITORENKONTO",
	 * kontoDto.getCNr()); }
	 * parameter.put("P_LIEFERKUNDE_KUNDENNUMMER",lieferadresseKunde
	 * .getIKundennummer()); }
	 * 
	 * parameter.put("P_BRIEFANREDE", cBriefanrede);
	 * 
	 * String cLabelRahmenOderLiefertermin =
	 * getTextRespectUISpr("lp.liefertermin", theClientDto.getMandant(),
	 * theClientDto.getLocUi()); ; String cLabelAuftragcnr = null;
	 * 
	 * if
	 * (auftragDto.getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_RAHMEN
	 * )) { cLabelRahmenOderLiefertermin =
	 * getTextRespectUISpr("lp.rahmentermin", theClientDto.getMandant(),
	 * theClientDto.getLocUi()); cLabelAuftragcnr =
	 * getTextRespectUISpr("auft.rahmenauftragcnr", theClientDto.getMandant(),
	 * locDruck); } else if
	 * (auftragDto.getAuftragartCNr().equals(AuftragServiceFac
	 * .AUFTRAGART_ABRUF)) { AuftragDto rahmenauftragDto =
	 * getAuftragFac().auftragFindByPrimaryKey(
	 * auftragDto.getAuftragIIdRahmenauftrag());
	 * 
	 * cLabelAuftragcnr = getTextRespectUISpr("lp.abrufzurahmen",
	 * theClientDto.getMandant(), locDruck); } else if
	 * (auftragDto.getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_FREI))
	 * { cLabelAuftragcnr = getTextRespectUISpr("auft.aufragcnr",
	 * theClientDto.getMandant(), locDruck); } else if
	 * (auftragDto.getAuftragartCNr().equals(AuftragServiceFac.
	 * AUFTRAGART_WIEDERHOLEND)) { cLabelAuftragcnr =
	 * getTextRespectUISpr("auft.wiederholauftragnr", theClientDto.getMandant(),
	 * locDruck); parameter.put("P_VERRECHNUNGSBEGINN",
	 * auftragDto.getTLauftermin()); //Auftragwiederholungsintervall String
	 * sAuftragwiederholungsintervall = null; Map mapAuftragWHIntervallSpr =
	 * getAuftragServiceFac(). getAuftragwiederholungsintervall(locDruck); if
	 * (mapAuftragWHIntervallSpr
	 * .containsKey(auftragDto.getWiederholungsintervallCNr())) {
	 * sAuftragwiederholungsintervall = (String) mapAuftragWHIntervallSpr.get(
	 * auftragDto.getWiederholungsintervallCNr()); } else {
	 * sAuftragwiederholungsintervall =
	 * auftragDto.getWiederholungsintervallCNr(); }
	 * parameter.put("P_VERRECHNUNGSINTERVALL", sAuftragwiederholungsintervall);
	 * }
	 * 
	 * parameter.put("P_LABELAUFTRAGCNR", cLabelAuftragcnr);
	 * parameter.put("P_BELEGKENNUNG", baueKennungAuftragbestaetigung(
	 * auftragDto, theClientDto));
	 * 
	 * parameter.put("P_LABELLIEFERTERMIN", cLabelRahmenOderLiefertermin);
	 * parameter.put("P_LIEFERTERMIN",
	 * Helper.formatDatum(auftragDto.getDLiefertermin(), locDruck));
	 * 
	 * parameter.put("P_UNVERBINDLICHERLIEFERTERMIN", new
	 * Boolean(Helper.short2boolean(auftragDto.
	 * getBLieferterminUnverbindlich())));
	 * 
	 * // die Lieferart parameter.put("P_LIEFERART",
	 * getLocaleFac().lieferartFindByIIdLocaleOhneExc(
	 * auftragDto.getLieferartIId(), locDruck, theClientDto));
	 * 
	 * // das Zahlungsziel parameter.put("P_ZAHLUNGSZIEL",
	 * getMandantFac().zahlungszielFindByIIdLocaleOhneExc(
	 * auftragDto.getZahlungszielIId(), locDruck, theClientDto));
	 * 
	 * parameter.put("P_BELEGWAEHRUNG", auftragDto.getCAuftragswaehrung());
	 * parameter.put("P_GESCHAEFTSFUEHRER_ANREDE", sAnredeGeschaeftsfuehrer);
	 * parameter.put("P_ALLGEMEINER_RABATT",
	 * auftragDto.getFAllgemeinerRabattsatz());
	 * parameter.put("P_ALLGEMEINER_RABATT_STRING",
	 * Helper.formatZahl(auftragDto.getFAllgemeinerRabattsatz(), locDruck));
	 * parameter.put("P_PROJEKT_RABATT",
	 * auftragDto.getFProjektierungsrabattsatz());
	 * parameter.put("P_PROJEKT_RABATT_STRING",
	 * Helper.formatZahl(auftragDto.getFProjektierungsrabattsatz(), locDruck));
	 * parameter.put("P_RECHNUNGSDRUCKMITRABATT",
	 * Helper.short2Boolean(kundeDto.getBRechnungsdruckmitrabatt())); //
	 * Konsistenzpruefung: Die Summe der einzelnen Positionen muss den
	 * Auftragwert ergeben parameter.put("P_AUFTRAGSWERT",
	 * auftragDto.getNGesamtauftragswertInAuftragswaehrung());
	 * 
	 * if (auftragDto.getKostIId() != null) { KostenstelleDto kostenstelleDto =
	 * getSystemFac().kostenstelleFindByPrimaryKey( auftragDto.getKostIId());
	 * parameter.put(LPReport.P_KOSTENSTELLE, kostenstelleDto.getCNr()); }
	 * 
	 * // die Kommunikationsinformation des Kunden Integer
	 * partnerIIdAnsprechpartner = null; if (ansprechpartnerDto != null) {
	 * partnerIIdAnsprechpartner =
	 * ansprechpartnerDto.getPartnerIIdAnsprechpartner(); }
	 * 
	 * String sEmail =
	 * getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(
	 * partnerIIdAnsprechpartner, kundeDto.getPartnerDto(),
	 * PartnerFac.KOMMUNIKATIONSART_EMAIL, theClientDto.getMandant(),
	 * theClientDto); String sFax =
	 * getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(
	 * partnerIIdAnsprechpartner, kundeDto.getPartnerDto(),
	 * PartnerFac.KOMMUNIKATIONSART_FAX, theClientDto.getMandant(),
	 * theClientDto); String sTelefon =
	 * getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(
	 * partnerIIdAnsprechpartner, kundeDto.getPartnerDto(),
	 * PartnerFac.KOMMUNIKATIONSART_TELEFON, theClientDto.getMandant(),
	 * theClientDto);
	 * 
	 * parameter.put(LPReport.P_ANSPRECHPARTNEREMAIL, sEmail != null ? sEmail :
	 * ""); parameter.put(LPReport.P_ANSPRECHPARTNERFAX, sFax != null ? sFax :
	 * ""); parameter.put(LPReport.P_ANSPRECHPARTNERTELEFON, sTelefon != null ?
	 * sTelefon : "");
	 * 
	 * //folgende Werte werden als eigene Parameter uebergeben. // aus
	 * Kompatibilitaetsgruenden wird noch das alte P_SUMMARY in dem die Werte
	 * //mit jeweils einer trennenden Leerzeile hintereinandergehaengt werden
	 * zusaetzlich befuellt. StringBuffer buffSummaryAlt = new StringBuffer();
	 * // Externer Kommentar if (auftragDto.getXExternerkommentar() != null &&
	 * auftragDto.getXExternerkommentar().length() > 0) {
	 * parameter.put(P_EXTERNERKOMMENTAR,
	 * Helper.formatStyledTextForJasper(auftragDto.getXExternerkommentar()));
	 * buffSummaryAlt.append(parameter.get(P_EXTERNERKOMMENTAR)).append("\n\n");
	 * }
	 * 
	 * // Eigentumsvorbehalt MediastandardDto mediastandardDto = getMediaFac().
	 * mediastandardFindByCNrDatenformatCNrMandantCNr(
	 * MediaFac.MEDIASTANDARD_EIGENTUMSVORBEHALT,
	 * MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML, theClientDto.getMandant(),
	 * kundeDto.getPartnerDto().getLocaleCNrKommunikation(), theClientDto);
	 * 
	 * if (mediastandardDto != null) {
	 * parameter.put(LPReport.P_EIGENTUMSVORBEHALT,
	 * Helper.formatStyledTextForJasper(mediastandardDto.getOMediaText()));
	 * buffSummaryAlt
	 * .append(parameter.get(LPReport.P_EIGENTUMSVORBEHALT)).append("\n\n"); }
	 * 
	 * // lieferbed: 0 Lieferbedingungen mediastandardDto = getMediaFac().
	 * mediastandardFindByCNrDatenformatCNrMandantCNr(
	 * MediaFac.MEDIASTANDARD_LIEFERBEDINGUNGEN,
	 * MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML, theClientDto.getMandant(),
	 * kundeDto.getPartnerDto().getLocaleCNrKommunikation(), theClientDto);
	 * 
	 * if (mediastandardDto != null) {
	 * parameter.put(LPReport.P_LIEFERBEDINGUNGEN,
	 * Helper.formatStyledTextForJasper(mediastandardDto.getOMediaText()));
	 * buffSummaryAlt
	 * .append(parameter.get(LPReport.P_LIEFERBEDINGUNGEN)).append("\n\n"); }
	 * 
	 * // Fusstext if (sFusstext != null) { parameter.put("P_FUSSTEXT",
	 * Helper.formatStyledTextForJasper(sFusstext));
	 * buffSummaryAlt.append(parameter.get("P_FUSSTEXT")).append("\n\n"); }
	 * 
	 * // Anrede des Mandanten String sMandantAnrede =
	 * mandantDto.getPartnerDto().formatFixName1Name2(); StringBuffer buff = new
	 * StringBuffer(); if (sMandantAnrede != null) {
	 * parameter.put(P_MANDANT_ANREDE_UND_NAME, sMandantAnrede);
	 * buff.append(sMandantAnrede).append("\n\n"); }
	 * 
	 * // P_SUMMARY: Die Unterschrift fuer Belege inclusive Unterschriftstext
	 * und -funktion // Beispiel: // "i.A. Ing. Werner Hehenwarter" - im Falle
	 * des Auftrags der Vertreter aus den Kopfdaten // "Einkaufsleiter" if
	 * (vertreterDto != null) { String sVertreterUFTitelName2Name1 =
	 * vertreterDto.formatFixUFTitelName2Name1();
	 * parameter.put(P_VERTRETER_UNTERSCHRIFTSFUNKTION_UND_NAME,
	 * sVertreterUFTitelName2Name1); buff.append(sVertreterUFTitelName2Name1);
	 * 
	 * if (vertreterDto.getCUnterschriftstext() != null &&
	 * vertreterDto.getCUnterschriftstext().length() > 0) { String
	 * sUnterschriftstext = vertreterDto.getCUnterschriftstext();
	 * parameter.put(P_VERTRETER_UNTERSCHRIFTSTEXT, sUnterschriftstext);
	 * buff.append("\n").append(sUnterschriftstext); } }
	 * parameter.put(P_VERTRETER,
	 * Helper.formatStyledTextForJasper(buff.toString()));
	 * 
	 * buffSummaryAlt.append(buff.toString()); parameter.put("P_SUMMARY",
	 * Helper.formatStyledTextForJasper(buffSummaryAlt.toString()));
	 * 
	 * //AUFTRAGSEIGENSCHAFTEN PaneldatenDto[] auftDaten = null; auftDaten =
	 * getPanelFac().paneldatenFindByPanelCNrCKey(
	 * PanelFac.PANEL_AUFTRAGSEIGENSCHAFTEN, iIdAuftragI.toString());
	 * if(auftDaten.length != 0){ parameter.put(P_AUFTRAGEIGENSCHAFT_FA, new
	 * String(auftDaten[2].getOInhalt()));
	 * parameter.put(P_AUFTRAGEIGENSCHAFT_CLUSTER, new
	 * String(auftDaten[1].getOInhalt()));
	 * parameter.put(P_AUFTRAGEIGENSCHAFT_EQNR, new
	 * String(auftDaten[0].getOInhalt())); }
	 * 
	 * int iAnzahlZeilen = 0; // Anzahl der Zeilen in der Gruppe
	 * 
	 * for (int i = 0; i < aPositionDtos.length; i++) { if
	 * (aPositionDtos[i].getAuftragpositionartCNr().equals(AuftragServiceFac.
	 * AUFTRAGPOSITIONART_IDENT)) { // Die Zeilenanzahl muss vor dem Befuellen
	 * festgelegt werden. StuecklisteInfoDto stuecklisteInfoDto =
	 * getStuecklisteFac(). getStrukturdatenEinesArtikels(
	 * aPositionDtos[i].getArtikelIId(), true, null, // in die Rekursion mit
	 * einer leeren Listen einsteigen 0, // in die Rekursion mit Ebene 0
	 * einsteigen -1, // alle Stuecklisten komplett aufloesen false, // Menge
	 * pro Einheit der uebergeorndeten Position new BigDecimal(1), // fuer 1
	 * Einheit der STKL true, // Fremdfertigung aufloesen theClientDto);
	 * 
	 * iAnzahlZeilen++; // fuer die eigentliche Ident Position iAnzahlZeilen +=
	 * stuecklisteInfoDto.getIiAnzahlPositionen().intValue(); } else {
	 * iAnzahlZeilen++; // fuer die Positionszeile } } data = new
	 * Object[iAnzahlZeilen][AuftragReportFac.
	 * REPORT_AUFTRAGBESTAETIGUNG_ANZAHL_SPALTEN];
	 * 
	 * // Der Index der aktuell verarbeiteten Angebotposition int
	 * iIndexAktuellePosition = 0;
	 * 
	 * // die Datenmatrix befuellen for (int i = 0; i < iAnzahlZeilen; i++) {
	 * 
	 * // Artikelpositionen if
	 * (aPositionDtos[iIndexAktuellePosition].getAuftragpositionartCNr().equals(
	 * AuftragServiceFac. AUFTRAGPOSITIONART_IDENT) ||
	 * aPositionDtos[iIndexAktuellePosition].getAuftragpositionartCNr().equals(
	 * AuftragServiceFac. AUFTRAGPOSITIONART_HANDEINGABE)) {
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION] = new
	 * Integer(iArtikelpositionsnummer);
	 * 
	 * iArtikelpositionsnummer++;
	 * 
	 * //stattdessen wird Report Feld REPORT_AUFTRAGBESTAETIGUNG_IMAGE verwendet
	 * //
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION_KOMMENTAR_IMAGE
	 * ] = imageKommentar;
	 * 
	 * ArtikelDto oArtikelDto = getArtikelFac().artikelFindByPrimaryKey(
	 * aPositionDtos[iIndexAktuellePosition].getArtikelIId(), theClientDto);
	 * BelegPositionDruckIdentDto druckDto = printIdent(aPositionDtos[
	 * iIndexAktuellePosition], LocaleFac.BELEGART_AUFTRAG, oArtikelDto,
	 * locDruck, theClientDto.getIDUser());
	 * 
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_BEZEICHNUNG] =
	 * druckDto. getSBezeichnung();
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_KURZBEZEICHNUNG] =
	 * druckDto. getSKurzbezeichnung();
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION_TITEL] =
	 * druckDto. getSArtikelInfo();
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION_ZUSATZBEZ] =
	 * druckDto.getSZusatzBezeichnung();
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ARTIKELCZBEZ2] =
	 * druckDto. getSArtikelZusatzBezeichnung2();
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITION_KOMMENTAR_TEXT
	 * ] = druckDto.getSArtikelkommentar();
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_IMAGE] = druckDto.
	 * getOImageKommentar();
	 * 
	 * StringBuffer sbArtikelInfo = new StringBuffer("");
	 * 
	 * if
	 * (aPositionDtos[iIndexAktuellePosition].getAuftragpositionartCNr().equals(
	 * AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) { // Ident nur fuer "echte"
	 * Artikel sbArtikelInfo.append(oArtikelDto.getCNr());
	 * sbArtikelInfo.append("\n");
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_IDENTNUMMER] =
	 * oArtikelDto.getCNr();
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_REFERENZNUMMER] =
	 * oArtikelDto.getCReferenznr();
	 * 
	 * //KundeArtikelnr gueltig zu Belegdatum KundesokoDto kundeSokoDto_gueltig
	 * = this.getKundesokoFac().
	 * kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc
	 * (kundeDto.getIId(), oArtikelDto.getIId(), new
	 * java.sql.Date(auftragDto.getDBelegdatum().getTime())); if
	 * (kundeSokoDto_gueltig != null) {
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_KUNDEARTIKELNR] =
	 * kundeSokoDto_gueltig.getCKundeartikelnummer(); }
	 * 
	 * if (oArtikelDto.getVerpackungDto() != null) {
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_BAUFORM] =
	 * oArtikelDto. getVerpackungDto(). getCBauform();
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_VERPACKUNGSART] =
	 * oArtikelDto. getVerpackungDto(). getCVerpackungsart(); } if
	 * (oArtikelDto.getMaterialIId() != null) { MaterialDto materialDto =
	 * getMaterialFac().materialFindByPrimaryKey( oArtikelDto.getMaterialIId(),
	 * theClientDto); if (materialDto.getMaterialsprDto() != null) {
	 * 
	 * // @todo MR->MR richtige Mehrsprachigkeit: Material in Drucksprache.
	 * 
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_MATERIAL] =
	 * materialDto. getMaterialsprDto().getCBez(); } else {
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_MATERIAL] =
	 * materialDto. getCNr(); } } if (oArtikelDto.getGeometrieDto() != null) {
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_BREITE] =
	 * oArtikelDto. getGeometrieDto(). getFBreite();
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_HOEHE] =
	 * oArtikelDto. getGeometrieDto(). getFHoehe();
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_TIEFE] =
	 * oArtikelDto. getGeometrieDto(). getFTiefe(); } }
	 * 
	 * if
	 * (!auftragDto.getDLiefertermin().equals(aPositionDtos[iIndexAktuellePosition
	 * ]. getTUebersteuerbarerLiefertermin())) {
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITIONTERMIN] =
	 * Helper.formatDatum(Helper.extractDate(aPositionDtos[
	 * iIndexAktuellePosition]. getTUebersteuerbarerLiefertermin()), locDruck);
	 * 
	 * }
	 * 
	 * String sSnr = null; if
	 * (aPositionDtos[iIndexAktuellePosition].getCSeriennrchargennr() != null) {
	 * sSnr = aPositionDtos[i].getCSeriennrchargennr(); } else { sSnr =
	 * getAuftragpositionFac().getSeriennummmern(aPositionDtos[
	 * iIndexAktuellePosition].getIId(), theClientDto); if (sSnr.equals("")) {
	 * sSnr = null; } }
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ZWANGSSERIENNUMMER] =
	 * sSnr; data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_MENGE] =
	 * aPositionDtos[ iIndexAktuellePosition]. getNMenge();
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_EINHEIT] =
	 * aPositionDtos[ iIndexAktuellePosition]. getEinheitCNr() == null ? null :
	 * getSystemFac().formatEinheit(aPositionDtos[iIndexAktuellePosition].
	 * getEinheitCNr(), locDruck, theClientDto);
	 * 
	 * if (Helper.short2Boolean(kundeDto.getBRechnungsdruckmitrabatt())) {
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_EINZELPREIS] =
	 * aPositionDtos[iIndexAktuellePosition].
	 * getNEinzelpreisplusversteckteraufschlag();
	 * 
	 * } else { data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_EINZELPREIS]
	 * = aPositionDtos[iIndexAktuellePosition].
	 * getNNettoeinzelpreisplusversteckteraufschlagminusrabatte(); } //
	 * Rabattsaetze nur andrucken, wenn sie nicht 0.0 sind Double ddRabattsatz =
	 * null; if (aPositionDtos[iIndexAktuellePosition].getFRabattsatz() != null
	 * && aPositionDtos[iIndexAktuellePosition].getFRabattsatz().doubleValue()
	 * != 0) { ddRabattsatz =
	 * aPositionDtos[iIndexAktuellePosition].getFRabattsatz(); }
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_RABATTSATZ] =
	 * ddRabattsatz;
	 * 
	 * Double ddZusatzrabattsatz = null; if
	 * (aPositionDtos[iIndexAktuellePosition].getFZusatzrabattsatz() != null &&
	 * aPositionDtos
	 * [iIndexAktuellePosition].getFZusatzrabattsatz().doubleValue() != 0) {
	 * ddZusatzrabattsatz = aPositionDtos[iIndexAktuellePosition].
	 * getFZusatzrabattsatz(); }
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ZUSATZRABATTSATZ] =
	 * ddZusatzrabattsatz;
	 * 
	 * BigDecimal bdGesamtpreis = null;
	 * 
	 * if (aPositionDtos[iIndexAktuellePosition].
	 * getNNettoeinzelpreisplusversteckteraufschlag() != null &&
	 * aPositionDtos[iIndexAktuellePosition].getNMenge() != null) {
	 * bdGesamtpreis = aPositionDtos[iIndexAktuellePosition].
	 * getNNettoeinzelpreisplusversteckteraufschlag().multiply(
	 * aPositionDtos[iIndexAktuellePosition].getNMenge()); }
	 * 
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_GESAMTPREIS] =
	 * bdGesamtpreis;
	 * 
	 * // Mwstsatzinformationen hinterlegen MwstsatzDto mwstsatzDto =
	 * getMandantFac().mwstsatzFindByPrimaryKey(
	 * aPositionDtos[iIndexAktuellePosition]. getMwstsatzIId(), theClientDto);
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_MWSTSATZ] =
	 * mwstsatzDto. getFMwstsatz();
	 * 
	 * // die Summen fuer den Mwstsatz der Position erhoehen MwstsatzReportDto
	 * mwstsatzReportDto = (MwstsatzReportDto) mwstMap.get(
	 * aPositionDtos[iIndexAktuellePosition]. getMwstsatzIId());
	 * 
	 * // mwstsatzDto = getMandantFac().mwstsatzFindByPrimaryKey( //
	 * aPositionDtos[iIndexAktuellePosition].getMwstsatzIId(), theClientDto); if
	 * (mwstsatzDto.getFMwstsatz().doubleValue() > 0) { BigDecimal
	 * nPositionsbetrag = aPositionDtos[iIndexAktuellePosition].
	 * getNNettoeinzelpreisplusversteckteraufschlagminusrabatte().
	 * multiply(aPositionDtos[iIndexAktuellePosition]. getNMenge());
	 * 
	 * BigDecimal nSummeMwstbetrag = mwstsatzReportDto.getNSummeMwstbetrag().
	 * add(nPositionsbetrag.multiply(new BigDecimal(mwstsatzDto.getFMwstsatz().
	 * doubleValue()).movePointLeft(2)));
	 * 
	 * mwstsatzReportDto.setNSummeMwstbetrag(nSummeMwstbetrag);
	 * mwstsatzReportDto.setNSummePositionsbetrag(mwstsatzReportDto.
	 * getNSummePositionsbetrag().add(nPositionsbetrag));
	 * 
	 * //mwstMap.update(aPositionDtos[iIndexAktuellePosition].getMwstsatzIId(),
	 * // mwstsatzReportDto); //
	 * mwstMap.put(aPositionDtos[iIndexAktuellePosition].getMwstsatzIId(), //
	 * mwstsatzReportDto); }
	 * 
	 * // Wenn es zu einem Artikel eine Stueckliste gibt... if
	 * (aPositionDtos[iIndexAktuellePosition].getAuftragpositionartCNr().equals(
	 * AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) { StuecklisteInfoDto
	 * stuecklisteInfoDto = getStuecklisteFac(). getStrukturdatenEinesArtikels(
	 * aPositionDtos[iIndexAktuellePosition].getArtikelIId(), true, null, // in
	 * die Rekursion mit einer leeren Listen einsteigen 0, // in die Rekursion
	 * mit Ebene 0 einsteigen -1, // alle Stuecklisten komplett aufloesen false,
	 * // Menge pro Einheit der uebergeorndeten Position new BigDecimal(1), //
	 * fuer 1 Einheit der STKL true, // Fremdfertigung aufloesen theClientDto);
	 * 
	 * if (stuecklisteInfoDto.getIiAnzahlPositionen().intValue() > 0) { // in
	 * die naechste Zeile schreiben, zuerst noch die aktuelle fertig machen
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITIONSART] =
	 * aPositionDtos[iIndexAktuellePosition].getAuftragpositionartCNr();
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_SEITENUMBRUCH] =
	 * bbSeitenumbruch;
	 * 
	 * ArrayList alStuecklisteAufgeloest = stuecklisteInfoDto.
	 * getAlStuecklisteAufgeloest();
	 * 
	 * Iterator it = alStuecklisteAufgeloest.iterator();
	 * 
	 * while (it.hasNext()) { i++;
	 * 
	 * StuecklisteMitStrukturDto stuecklisteMitStrukturDto =
	 * (StuecklisteMitStrukturDto) it.next();
	 * 
	 * StuecklistepositionDto stuecklistepositionDto =
	 * stuecklisteMitStrukturDto.getStuecklistepositionDto();
	 * 
	 * // Pro Position eine kuenstliche Zeile zum Andrucken erzeugen, // als
	 * Bezugsmenge gilt immer 1 Einheit der Stueckliste
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITIONSART] =
	 * AuftragServiceFac.AUFTRAGPOSITIONART_STUECKLISTENPOSITION;
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_SEITENUMBRUCH] =
	 * bbSeitenumbruch;
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_STKLMENGE] =
	 * stuecklistepositionDto.getNMenge();
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_STKLEINHEIT] =
	 * getSystemFac().formatEinheit(stuecklistepositionDto.getEinheitCNr(),
	 * locDruck, theClientDto);
	 * 
	 * // Einrueckung fuer Unterstuecklisten String einrueckung = ""; for (int j
	 * = 0; j < stuecklisteMitStrukturDto.getIEbene(); j++) { einrueckung =
	 * einrueckung + "    "; }
	 * 
	 * String artikelCNr = null;
	 * 
	 * // @todo boeser Workaround ... PJ 3779 if
	 * (stuecklistepositionDto.getArtikelDto().getCNr().startsWith("~")) {
	 * artikelCNr = AngebotReportFac.
	 * REPORT_VORKALKULATION_ZEICHEN_FUER_HANDEINGABE; } else { artikelCNr =
	 * stuecklistepositionDto.getArtikelDto().getCNr(); }
	 * 
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_STKLARTIKELCNR] =
	 * einrueckung + artikelCNr;
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_STKLARTIKELBEZ] =
	 * getArtikelFac().baueArtikelBezeichnungMehrzeilig(
	 * stuecklistepositionDto.getArtikelIId(), LocaleFac.POSITIONSART_IDENT,
	 * null, null, false, locDruck, theClientDto); } } } }
	 * 
	 * // Betrifft Positionen if
	 * (aPositionDtos[iIndexAktuellePosition].getAuftragpositionartCNr().equals(
	 * AuftragServiceFac. AUFTRAGPOSITIONART_BETRIFFT)) {
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_FREIERTEXT] =
	 * aPositionDtos[iIndexAktuellePosition]. getCBez(); }
	 * 
	 * // Texteingabe Positionen if
	 * (aPositionDtos[iIndexAktuellePosition].getAuftragpositionartCNr().equals(
	 * AuftragServiceFac. AUFTRAGPOSITIONART_TEXTEINGABE)) { // IMS 1619 leerer
	 * Text soll als Leerezeile erscheinen String sText =
	 * aPositionDtos[iIndexAktuellePosition].getXTextinhalt();
	 * 
	 * if (sText != null && sText.trim().equals("")) {
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_LEERZEILE] = " "; }
	 * else { data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_FREIERTEXT] =
	 * sText; } }
	 * 
	 * // Textbaustein Positionen if
	 * (aPositionDtos[iIndexAktuellePosition].getAuftragpositionartCNr().equals(
	 * AuftragServiceFac.AUFTRAGPOSITIONART_TEXTBAUSTEIN)) { // Dto holen
	 * MediastandardDto oMediastandardDto = getMediaFac().
	 * mediastandardFindByPrimaryKey(
	 * aPositionDtos[iIndexAktuellePosition].getMediastandardIId()); // zum
	 * Drucken vorbereiten BelegPositionDruckTextbausteinDto druckDto =
	 * printTextbaustein( oMediastandardDto, theClientDto);
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_FREIERTEXT] =
	 * druckDto. getSFreierText();
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_IMAGE] =
	 * druckDto.getOImage(); }
	 * 
	 * // Leerzeile Positionen if
	 * (aPositionDtos[iIndexAktuellePosition].getAuftragpositionartCNr().equals(
	 * AuftragServiceFac. AUFTRAGPOSITIONART_LEERZEILE)) {
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_LEERZEILE] = " "; }
	 * 
	 * // Seitenumbruch Positionen if
	 * (aPositionDtos[iIndexAktuellePosition].getAuftragpositionartCNr().equals(
	 * AuftragServiceFac. AUFTRAGPOSITIONART_SEITENUMBRUCH)) { bbSeitenumbruch =
	 * new Boolean(!bbSeitenumbruch.booleanValue()); // toggle }
	 * data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_SEITENUMBRUCH] =
	 * bbSeitenumbruch;
	 * 
	 * // Positionsart setzen, wenn es keine kuenstliche Position ist if
	 * (data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITIONSART] ==
	 * null) { data[i][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_POSITIONSART]
	 * = aPositionDtos[iIndexAktuellePosition].getAuftragpositionartCNr(); }
	 * 
	 * iIndexAktuellePosition++; // die naechste Angebotposition bearbeiten }
	 * 
	 * // das Andrucken der gesammelten Mwstinformationen steuern StringBuffer
	 * sbMwstsatz = new StringBuffer(); StringBuffer sbSummePositionsbetrag =
	 * new StringBuffer(); StringBuffer sbWaehrung = new StringBuffer();
	 * StringBuffer sbSummeMwstbetrag = new StringBuffer();
	 * 
	 * // UW 03.03.06: Die folgenden Informationen erscheinen am Druck, mit
	 * diesen // Werten wird intern nicht mehr weitergerechnet -> daher auf 2
	 * Stellen runden BigDecimal nAuftragsendbetragMitMwst =
	 * Helper.rundeKaufmaennisch(
	 * auftragDto.getNGesamtauftragswertInAuftragswaehrung(), 2);
	 * 
	 * boolean bHatMwstWerte = false;
	 * 
	 * for (Iterator iter = mwstMap.keySet().iterator(); iter.hasNext(); ) {
	 * Integer key = (Integer) iter.next(); // IId des Mwstsatzes
	 * MwstsatzReportDto mwstsatzReportDto = (MwstsatzReportDto)
	 * mwstMap.get(key); // Summen der Mwstbetraege if (mwstsatzReportDto !=
	 * null && mwstsatzReportDto.getNSummeMwstbetrag().doubleValue() > 0.0) {
	 * MwstsatzDto mwstsatzDto = getMandantFac().mwstsatzFindByPrimaryKey(key,
	 * theClientDto);
	 * 
	 * //MR: FIX, statt festverdrahtetem UST verwende Localeabh&auml;ngigen Wert
	 * lp.ust sbMwstsatz.append(getTextRespectUISpr("lp.ust",
	 * theClientDto.getMandant(), locDruck)); sbMwstsatz.append(": ");
	 * sbMwstsatz.append(Helper.formatZahl(mwstsatzDto.getFMwstsatz(), 2,
	 * locDruck)); sbMwstsatz.append(" % ");
	 * sbMwstsatz.append(getTextRespectUISpr("lp.ustvon",
	 * theClientDto.getMandant(), locDruck)). append(" "); //Fix Ende
	 * sbSummePositionsbetrag.append(Helper.formatZahl(mwstsatzReportDto.
	 * getNSummePositionsbetrag(), 2, locDruck));
	 * sbWaehrung.append(auftragDto.getCAuftragswaehrung());
	 * sbSummeMwstbetrag.append(Helper.formatZahl(mwstsatzReportDto.
	 * getNSummeMwstbetrag(), 2, locDruck));
	 * 
	 * sbMwstsatz.append("\n"); sbSummePositionsbetrag.append("\n");
	 * sbWaehrung.append("\n"); sbSummeMwstbetrag.append("\n");
	 * 
	 * nAuftragsendbetragMitMwst = nAuftragsendbetragMitMwst.add(
	 * Helper.rundeKaufmaennisch( mwstsatzReportDto.getNSummeMwstbetrag(), 2));
	 * 
	 * bHatMwstWerte = true; } }
	 * 
	 * if (bHatMwstWerte) { // die letzten \n wieder loeschen
	 * sbMwstsatz.delete(sbMwstsatz.length() - 1, sbMwstsatz.length());
	 * sbSummePositionsbetrag.delete(sbSummePositionsbetrag.length() - 1,
	 * sbSummePositionsbetrag.length()); sbWaehrung.delete(sbWaehrung.length() -
	 * 1, sbWaehrung.length());
	 * sbSummeMwstbetrag.delete(sbSummeMwstbetrag.length() - 1,
	 * sbSummeMwstbetrag.length()); }
	 * 
	 * parameter.put("P_MWST_TABELLE_LINKS", sbMwstsatz.toString());
	 * parameter.put("P_MWST_TABELLE_SUMME_POSITIONEN",
	 * sbSummePositionsbetrag.toString());
	 * parameter.put("P_MWST_TABELLE_WAEHRUNG", sbWaehrung.toString());
	 * parameter.put("P_MWST_TABELLE_RECHTS", sbSummeMwstbetrag.toString());
	 * parameter.put("P_AUFTRAGSENDBETRAGMITMWST", nAuftragsendbetragMitMwst);
	 * 
	 * // die Anzahl der Exemplare ist 1 + Anzahl der Kopien int
	 * iAnzahlExemplare = 1;
	 * 
	 * if (iAnzahlKopienI != null && iAnzahlKopienI.intValue() > 0) {
	 * iAnzahlExemplare += iAnzahlKopienI.intValue(); }
	 * 
	 * aJasperPrint = new JasperPrintLP[iAnzahlExemplare];
	 * 
	 * for (int iKopieNummer = 0; iKopieNummer < iAnzahlExemplare;
	 * iKopieNummer++) { // jede Kopie bekommt eine Kopienummer, das Original
	 * bekommt keine if (iKopieNummer > 0) {
	 * parameter.put(LPReport.P_KOPIE_NUMMER, new Integer(iKopieNummer)); }
	 * 
	 * // Index zuruecksetzen !!! index = -1;
	 * 
	 * initJRDS(parameter, AuftragReportFac.REPORT_MODUL,
	 * AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG, theClientDto.getMandant(),
	 * locDruck, theClientDto, bMitLogo.booleanValue(),
	 * auftragDto.getKostIId());
	 * 
	 * aJasperPrint[iKopieNummer] = getReportPrint(); }
	 * 
	 * // den Druckzeitpunkt vermerken auftragDto.setTGedruckt(getTimestamp());
	 * } catch (RemoteException ex) { throwEJBExceptionLPRespectOld(ex); }
	 * return aJasperPrint; }
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printErfuellungsgrad(java.sql.Timestamp tStichtag,
			Integer personalIId_Vertreter, Integer kostenstelleIId,
			boolean bMitWiederholenden, TheClientDto theClientDto) {

		cAktuellerReport = AuftragReportFac.REPORT_AUFTRAG_ERFUELLUNGSGRAD;

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		// vom Stichtag die Uhrzeit abschneiden
		tStichtag = Helper.cutTimestamp(tStichtag);

		session = factory.openSession();

		Criteria crit = session.createCriteria(FLRAuftragReport.class);

		// Ohne stornierte
		crit.add(Restrictions.not(Restrictions.eq(
				AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR,
				AuftragServiceFac.AUFTRAGSTATUS_STORNIERT)));
		crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));

		if (!bMitWiederholenden) {
			crit.add(Restrictions.not(Restrictions.eq(
					AuftragFac.FLR_AUFTRAG_AUFTRAGART_C_NR,
					AuftragServiceFac.AUFTRAGART_WIEDERHOLEND)));
		}
		crit.createAlias(AuftragFac.FLR_AUFTRAG_FLRVERTRETER, "p");
		HashMap<String, Object> mapReportParameterI = new HashMap<String, Object>();
		mapReportParameterI.put("P_STICHTAG", tStichtag);
		if (personalIId_Vertreter != null) {
			crit.add(Restrictions.eq("p.i_id", personalIId_Vertreter));

			PersonalDto personalDto = getPersonalFac()
					.personalFindByPrimaryKey(personalIId_Vertreter,
							theClientDto);
			mapReportParameterI.put("P_VERTRETER", personalDto.getPartnerDto()
					.formatFixTitelName1Name2());

		}
		if (kostenstelleIId != null) {
			crit.add(Restrictions.eq(AuftragFac.FLR_AUFTRAG_KOSTENSTELLE_I_ID,
					kostenstelleIId));

			KostenstelleDto kostenstelleDto = getSystemFac()
					.kostenstelleFindByPrimaryKey(kostenstelleIId);
			mapReportParameterI.put("P_KOSTENSTELLE",
					kostenstelleDto.formatKostenstellenbezeichnung());

		}

		// Das Belegdatum muss vor dem Stichtag liegen
		crit.add(Restrictions
				.le(AuftragFac.FLR_AUFTRAG_D_BELEGDATUM, tStichtag));

		// Der Auftrag darf zum Stichtag noch nicht erledigt worden sein
		crit.add(Restrictions.or(
				Restrictions.isNull(AuftragFac.FLR_AUFTRAG_T_ERLEDIGT),
				Restrictions.gt(AuftragFac.FLR_AUFTRAG_T_ERLEDIGT, tStichtag)));

		crit.addOrder(Order.asc("p."
				+ PersonalFac.FLR_PERSONAL_C_PERSONALNUMMER));
		crit.addOrder(Order.asc("c_nr"));

		List<?> list = crit.list();
		Iterator<?> it = list.iterator();

		ArrayList<Object[]> alDaten = new ArrayList<Object[]>();

		TreeMap<String, Double> auftragszeitenGesamtVerdichtet = new TreeMap<String, Double>();

		while (it.hasNext()) {
			FLRAuftragReport flrAuftrag = (FLRAuftragReport) it.next();
			Object[] oZeile = new Object[23];

			oZeile[AuftragReportFac.REPORT_ERFUELLUNGSGRAD_AUFTRAGSNUMMER] = flrAuftrag
					.getC_nr();

			BigDecimal nAuftragsWertInMandantenwaehrung = new BigDecimal(0);
			if (flrAuftrag.getN_gesamtauftragswertinauftragswaehrung() != null) {
				nAuftragsWertInMandantenwaehrung = getBetragMalWechselkurs(
						flrAuftrag.getN_gesamtauftragswertinauftragswaehrung(),
						Helper.getKehrwert(new BigDecimal(
								flrAuftrag
										.getF_wechselkursmandantwaehrungzuauftragswaehrung()
										.doubleValue())));
			}

			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					flrAuftrag.getKunde_i_id_auftragsadresse(), theClientDto);
			oZeile[AuftragReportFac.REPORT_ERFUELLUNGSGRAD_AUFTRAGSADRESSE_NAME1] = kundeDto
					.getPartnerDto().getCName1nachnamefirmazeile1();
			oZeile[AuftragReportFac.REPORT_ERFUELLUNGSGRAD_AUFTRAGSADRESSE_NAME2] = kundeDto
					.getPartnerDto().getCName2vornamefirmazeile2();
			oZeile[AuftragReportFac.REPORT_ERFUELLUNGSGRAD_AUFTRAGSADRESSE_STRASSE] = kundeDto
					.getPartnerDto().getCStrasse();

			if (kundeDto.getPartnerDto().getLandplzortDto() != null) {
				oZeile[AuftragReportFac.REPORT_ERFUELLUNGSGRAD_AUFTRAGSADRESSE_ORT] = kundeDto
						.getPartnerDto().getLandplzortDto().getOrtDto()
						.getCName();
				oZeile[AuftragReportFac.REPORT_ERFUELLUNGSGRAD_AUFTRAGSADRESSE_PLZ] = kundeDto
						.getPartnerDto().getLandplzortDto().getCPlz();
				oZeile[AuftragReportFac.REPORT_ERFUELLUNGSGRAD_AUFTRAGSADRESSE_LKZ] = kundeDto
						.getPartnerDto().getLandplzortDto().getLandDto()
						.getCLkz();
			}

			KundeDto kundeDto_Lieferadresse = getKundeFac()
					.kundeFindByPrimaryKey(
							flrAuftrag.getKunde_i_id_lieferadresse(),
							theClientDto);
			oZeile[AuftragReportFac.REPORT_ERFUELLUNGSGRAD_LIEFERADRESSE] = kundeDto_Lieferadresse;
			KundeDto kundeDto_Rechnungsadresse = getKundeFac()
					.kundeFindByPrimaryKey(
							flrAuftrag.getKunde_i_id_rechnungsadresse(),
							theClientDto);
			oZeile[AuftragReportFac.REPORT_ERFUELLUNGSGRAD_RECHNUNGSADRESSE] = kundeDto_Rechnungsadresse;

			oZeile[AuftragReportFac.REPORT_ERFUELLUNGSGRAD_AUFTRAGSWERT] = nAuftragsWertInMandantenwaehrung;
			try {
				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKey(
								flrAuftrag.getFlrvertreter().getI_id(),
								theClientDto);

				oZeile[AuftragReportFac.REPORT_ERFUELLUNGSGRAD_VERTRETER] = personalDto
						.getPartnerDto().formatFixTitelName1Name2();
				oZeile[AuftragReportFac.REPORT_ERFUELLUNGSGRAD_ZUSATZBELEG] = LocaleFac.BELEGART_AUFTRAG
						.trim() + " " + flrAuftrag.getC_nr();

				ArrayList<?> al = getDataAuftragNachkalkulation(
						flrAuftrag.getI_id(), true, tStichtag, theClientDto);
				BigDecimal bdSollstunden = new BigDecimal(0);
				BigDecimal bdIststunden = new BigDecimal(0);
				BigDecimal bdGestwertIststunden = new BigDecimal(0);
				for (int i = 0; i < al.size(); i++) {
					AuftragNachkalkulationDto temp = (AuftragNachkalkulationDto) al
							.get(i);
					if (temp.getBdGestehungswertarbeitist() != null) {
						bdGestwertIststunden = bdGestwertIststunden.add(temp
								.getBdGestehungswertarbeitist());
					}
					if (temp.getDdArbeitszeitsoll() != null) {
						bdSollstunden = bdSollstunden.add(new BigDecimal(temp
								.getDdArbeitszeitsoll().doubleValue()));
					}
					if (temp.getDdMaschinenzeitsoll() != null) {
						bdSollstunden = bdSollstunden.add(new BigDecimal(temp
								.getDdMaschinenzeitsoll().doubleValue()));
					}
					if (temp.getDdArbeitszeitist() != null) {
						bdIststunden = bdIststunden.add(new BigDecimal(temp
								.getDdArbeitszeitist().doubleValue()));
					}
					if (temp.getDdMaschinenzeitist() != null) {
						bdIststunden = bdIststunden.add(new BigDecimal(temp
								.getDdMaschinenzeitist().doubleValue()));
					}
				}
				oZeile[AuftragReportFac.REPORT_ERFUELLUNGSGRAD_PROJEKT] = flrAuftrag
						.getC_bez();
				oZeile[AuftragReportFac.REPORT_ERFUELLUNGSGRAD_GEPLANTESTUNDEN] = bdSollstunden;
				oZeile[AuftragReportFac.REPORT_ERFUELLUNGSGRAD_GELEISTETESTUNDEN] = bdIststunden;
				oZeile[AuftragReportFac.REPORT_ERFUELLUNGSGRAD_WERTGELEISTETESTUNDEN] = bdGestwertIststunden;

				BigDecimal erfuellungsgrad = new BigDecimal(1);
				if (bdSollstunden.doubleValue() != 0) {
					erfuellungsgrad = bdIststunden.divide(bdSollstunden, 4,
							BigDecimal.ROUND_HALF_EVEN);
					oZeile[AuftragReportFac.REPORT_ERFUELLUNGSGRAD_THEORETISCHERFUELLT] = erfuellungsgrad
							.multiply(new BigDecimal(100));
				} else {

					oZeile[AuftragReportFac.REPORT_ERFUELLUNGSGRAD_THEORETISCHERFUELLT] = new BigDecimal(
							100);

				}
				// Tatsaechlich erfuellt
				if (flrAuftrag.getF_erfuellungsgrad() != null) {
					BigDecimal bdErfTemp = new BigDecimal(flrAuftrag
							.getF_erfuellungsgrad().doubleValue());
					erfuellungsgrad = bdErfTemp.divide(new BigDecimal(100), 4,
							BigDecimal.ROUND_HALF_EVEN);
					oZeile[AuftragReportFac.REPORT_ERFUELLUNGSGRAD_TATSAECHLICHERFUELLT] = bdErfTemp;
				}

				RechnungDto[] rechnungDtos = getRechnungFac()
						.rechnungFindByAuftragIId(flrAuftrag.getI_id());

				boolean bSchlussrechnung = false;
				BigDecimal bereitsbezahlt = new BigDecimal(0);
				for (int i = 0; i < rechnungDtos.length; i++) {
					if (!rechnungDtos[i].getStatusCNr().equals(
							RechnungFac.STATUS_STORNIERT)) {
						if (rechnungDtos[i].getRechnungartCNr().equals(
								RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
							bSchlussrechnung = true;

							if (rechnungDtos[i].getNWert() != null) {
								bereitsbezahlt = rechnungDtos[i].getNWert();
							}
						}

						if (bSchlussrechnung == false
								&& rechnungDtos[i].getNWert() != null) {
							bereitsbezahlt = bereitsbezahlt.add(rechnungDtos[i]
									.getNWert());
						}

					}
				}

				BigDecimal zahlungsgrad = new BigDecimal(1);
				if (nAuftragsWertInMandantenwaehrung.doubleValue() != 0) {
					zahlungsgrad = bereitsbezahlt.divide(
							nAuftragsWertInMandantenwaehrung, 4,
							BigDecimal.ROUND_HALF_EVEN);
				}
				oZeile[AuftragReportFac.REPORT_ERFUELLUNGSGRAD_ZAHLUNGSGRAD] = zahlungsgrad
						.multiply(new BigDecimal(100));

				BigDecimal saldo = erfuellungsgrad.subtract(zahlungsgrad);
				oZeile[AuftragReportFac.REPORT_ERFUELLUNGSGRAD_SALDO] = saldo
						.multiply(new BigDecimal(100));
				oZeile[AuftragReportFac.REPORT_ERFUELLUNGSGRAD_VORLEISTUNG] = nAuftragsWertInMandantenwaehrung
						.multiply(saldo);

				// Personalzeiten nach Personen verdichtet
				AuftragzeitenDto[] auftragzeitenDtos = getZeiterfassungFac()
						.getAllZeitenEinesBeleges(LocaleFac.BELEGART_AUFTRAG,
								flrAuftrag.getI_id(), null, null, null,
								tStichtag, false, true, theClientDto);

				TreeMap<String, Double> auftragszeitenVerdichtet = new TreeMap<String, Double>();

				for (int i = 0; i < auftragzeitenDtos.length; i++) {

					String sName = auftragzeitenDtos[i]
							.getSPersonalMaschinenname();

					if (auftragszeitenVerdichtet.containsKey(sName)) {
						Double dDauer = (Double) auftragszeitenVerdichtet
								.get(sName);
						dDauer += auftragzeitenDtos[i].getDdDauer();

						auftragszeitenVerdichtet.put(sName, dDauer);
					} else {
						auftragszeitenVerdichtet.put(sName,
								auftragzeitenDtos[i].getDdDauer());
					}

					// Auch bei gesamt hinzufuegen
					if (auftragszeitenGesamtVerdichtet.containsKey(sName)) {
						Double dDauer = (Double) auftragszeitenGesamtVerdichtet
								.get(sName);
						dDauer += auftragzeitenDtos[i].getDdDauer();

						auftragszeitenGesamtVerdichtet.put(sName, dDauer);
					} else {
						auftragszeitenGesamtVerdichtet.put(sName,
								auftragzeitenDtos[i].getDdDauer());
					}

				}

				String[] fieldnames = new String[] { "Person", "Dauer" };
				Iterator<?> itAuftragszeiten = auftragszeitenVerdichtet
						.keySet().iterator();
				Object[][] dataSub = new Object[auftragszeitenVerdichtet.size()][fieldnames.length];
				int j = 0;
				while (itAuftragszeiten.hasNext()) {
					String key = (String) itAuftragszeiten.next();
					Double dauer = auftragszeitenVerdichtet.get(key);

					dataSub[j][0] = key;
					dataSub[j][1] = dauer;
					j++;
					// Zeile einfuegen
				}

				oZeile[AuftragReportFac.REPORT_ERFUELLUNGSGRAD_SUBREPORT_PERSONALZEITEN] = new LPDatenSubreport(
						dataSub, fieldnames);
				alDaten.add(oZeile);

				for (int i = 0; i < rechnungDtos.length; i++) {
					RechnungDto rechnungDto = rechnungDtos[i];
					if (!rechnungDto.getStatusCNr().equals(
							RechnungFac.STATUS_STORNIERT)) {
						oZeile = new Object[23];

						oZeile[AuftragReportFac.REPORT_ERFUELLUNGSGRAD_AUFTRAGSNUMMER] = flrAuftrag
								.getC_nr();
						oZeile[AuftragReportFac.REPORT_ERFUELLUNGSGRAD_VERTRETER] = personalDto
								.getPartnerDto().formatFixTitelName1Name2();
						oZeile[AuftragReportFac.REPORT_ERFUELLUNGSGRAD_RECHNUNGSWERT] = rechnungDto
								.getNWert();
						oZeile[AuftragReportFac.REPORT_ERFUELLUNGSGRAD_ZUSATZBELEG] = rechnungDto
								.getRechnungartCNr()
								+ " "
								+ rechnungDto.getCNr();

						if (bSchlussrechnung == true) {
							if (rechnungDto.getRechnungartCNr().equals(
									RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
								alDaten.add(oZeile);
							}
						} else {
							alDaten.add(oZeile);
						}
					}
				}

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

		}

		// Summe Personalzeiten einfuegen
		String[] fieldnames = new String[] { "Person", "Dauer" };
		Iterator<?> itAuftragszeiten = auftragszeitenGesamtVerdichtet.keySet()
				.iterator();
		Object[][] dataSub = new Object[auftragszeitenGesamtVerdichtet.size()][fieldnames.length];
		int j = 0;
		while (itAuftragszeiten.hasNext()) {
			String key = (String) itAuftragszeiten.next();
			Double dauer = auftragszeitenGesamtVerdichtet.get(key);

			dataSub[j][0] = key;
			dataSub[j][1] = dauer;
			j++;
			// Zeile einfuegen
		}

		mapReportParameterI.put("P_SUBREPORT", new LPDatenSubreport(dataSub,
				fieldnames));

		data = new Object[alDaten.size()][23];

		for (int i = 0; i < alDaten.size(); i++) {
			data[i] = (Object[]) alDaten.get(i);
		}

		initJRDS(mapReportParameterI, AuftragReportFac.REPORT_MODUL,
				AuftragReportFac.REPORT_AUFTRAG_ERFUELLUNGSGRAD,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();

	}

	/**
	 * Die Packliste zu einem bestimmten Auftrag drucken.
	 * 
	 * @param iIdAuftragI
	 *            PK des Auftrags
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return JasperPrint der Druck
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public JasperPrintLP printAuftragPackliste(Integer iIdAuftragI,
			String sReportName, TheClientDto theClientDto)
			throws EJBExceptionLP {

		checkAuftragIId(iIdAuftragI);

		JasperPrintLP jasperPrint = null;
		boolean bAuftragStundenMinuten = false;
		boolean auftragSNR = false;

		try {
			ZusatzfunktionberechtigungDto zusatzfunktionberechtigungDto = getMandantFac()
					.zusatzfunktionberechtigungFindByPrimaryKey(
							MandantFac.ZUSATZFUNKTION_AUFTRAGTERMIN_STUNDEN_MINUTEN,
							theClientDto.getMandant());
			ZusatzfunktionberechtigungDto zfbAuftragSNR = getMandantFac()
					.zusatzfunktionberechtigungFindByPrimaryKey(
							MandantFac.ZUSATZFUNKTION_AUFTRAG_SERIENNUMMERN,
							theClientDto.getMandant());
			if (zusatzfunktionberechtigungDto != null) {
				bAuftragStundenMinuten = true;
			}
			if (zfbAuftragSNR != null) {
				auftragSNR = true;
				sReportName = REPORT_AUFTRAG_PACKLISTE_SNR;
			}

			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(
					iIdAuftragI);
			/*
			 * AuftragpositionDto[] aPositionDtos = getAuftragpositionFac().
			 * auftragpositionFindByAuftragPositiveMenge(iIdAuftragI,
			 * theClientDto);
			 */
			AuftragpositionDto[] aPositionDtos = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(iIdAuftragI);
			// Erstellung des Report
			cAktuellerReport = sReportName;
			// es gilt das Locale des Auftragskunden
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					auftragDto.getKundeIIdAuftragsadresse(), theClientDto);

			AnsprechpartnerDto ansprechpartnerDto = null;

			if (auftragDto.getAnsprechparnterIId() != null) {
				ansprechpartnerDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(
								auftragDto.getAnsprechparnterIId(),
								theClientDto);
			}

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);

			Locale clientLocale = theClientDto.getLocUi();
			ArtikelsprDto oArtikelsprDto = null;
			int iAnzahlZeilen = aPositionDtos.length; // Anzahl der Zeilen in
			// der Gruppe

			List<AuftragPacklistePositionDto> dataList = new ArrayList<AuftragPacklistePositionDto>();

			// die Datenmatrix befuellen
			for (int i = 0; i < iAnzahlZeilen; i++) {
				AuftragPacklistePositionDto posDto = new AuftragPacklistePositionDto();
				if (aPositionDtos[i].getArtikelIId() != null) {
					ArtikelDto oArtikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(
									aPositionDtos[i].getArtikelIId(),
									theClientDto);

					StringBuffer sbIdent = new StringBuffer();
					StringBuffer sbArtikelInfo = new StringBuffer();

					// druckdetail: 2 Artikelkommentare vom Typ Text und Bild
					// andrucken
					ArtikelkommentarDto[] aKommentarDto = getArtikelkommentarFac()
							.artikelkommentardruckFindByArtikelIIdBelegartCNr(
									aPositionDtos[i].getArtikelIId(),
									LocaleFac.BELEGART_AUFTRAG,
									theClientDto.getLocUiAsString(),
									theClientDto);

					String cPositionKommentarText = "";
					Image imageKommentar = null;

					if (aKommentarDto != null) {
						for (int k = 0; k < aKommentarDto.length; k++) {
							// Kommentar kann Text oder Bild sein
							String cDatenformat = aKommentarDto[k]
									.getDatenformatCNr().trim();

							if (cDatenformat
									.indexOf(MediaFac.DATENFORMAT_MIMETYPEART_IMAGE) != -1) {
								// es wird hoechstens 1 Bild pro Belegart
								// gedruckt
								imageKommentar = Helper
										.byteArrayToImage(aKommentarDto[k]
												.getArtikelkommentarsprDto()
												.getOMedia());
								posDto.setKommentarImage(imageKommentar);

							} else if (cDatenformat
									.indexOf(MediaFac.DATENFORMAT_MIMETYPEART_TEXT) != -1) {
								String cKommentar = aKommentarDto[k]
										.getArtikelkommentarsprDto()
										.getXKommentar();

								if (cPositionKommentarText != null
										&& cPositionKommentarText.length() > 0) {
									cPositionKommentarText += "\n";
								}

								if (cKommentar != null) {
									cPositionKommentarText += cKommentar;
								}
							}
						}
					}

					posDto.setArtikelkommentar(cPositionKommentarText);

					posDto.setFreierText(aPositionDtos[i].getXTextinhalt());

					if (aPositionDtos[i].getPositionsartCNr().equals(
							AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {
						sbIdent.append(oArtikelDto.getCNr());

						if (aPositionDtos[i].getCBez() != null) {
							sbArtikelInfo.append(aPositionDtos[i].getCBez());
						} else {
							oArtikelsprDto = getArtikelFac()
									.artikelsprFindByArtikelIIdLocaleCNrOhneExc(
											oArtikelDto.getIId(),
											clientLocale.toString(),
											theClientDto);
							if (oArtikelsprDto != null) {
								if (oArtikelsprDto.getCBez() != null) {
									if (sbArtikelInfo.length() > 0) {
										sbArtikelInfo.append("\n");
									}

									sbArtikelInfo.append(oArtikelsprDto
											.getCBez());
								}
							} else {
								if (oArtikelDto.getArtikelsprDto() != null) {
									if (oArtikelDto.getArtikelsprDto()
											.getCBez() != null) {
										if (sbArtikelInfo.length() > 0) {
											sbArtikelInfo.append("\n");
										}

										sbArtikelInfo.append(oArtikelDto
												.getArtikelsprDto().getCBez());
									}
								}
							}
						}
						if (aPositionDtos[i].getCZusatzbez() != null) {
							sbArtikelInfo.append(aPositionDtos[i]
									.getCZusatzbez());
						} else {

							if (oArtikelsprDto != null) {
								if (oArtikelsprDto.getCZbez() != null) {
									sbArtikelInfo.append("\n").append(
											oArtikelsprDto.getCZbez());
								}
							} else {
								if (oArtikelDto.getArtikelsprDto() != null) {
									if (oArtikelDto.getArtikelsprDto()
											.getCZbez() != null) {
										sbArtikelInfo.append("\n").append(
												oArtikelDto.getArtikelsprDto()
														.getCZbez());
									}
								}
							}
						}
						// Vollstaendigkeit der Komponenten.
						StringBuffer sbArtikelVollstaendigkeitIdent = new StringBuffer();
						StringBuffer sbArtikelVollstaendigkeitBez = new StringBuffer();
						if (aPositionDtos[i].getArtikelIId() != null) {
							Integer[] artikelIIds = getArtikelFac()
									.getZugehoerigeArtikel(
											aPositionDtos[i].getArtikelIId());
							if (artikelIIds.length != 0) {
								for (int k = 0; k < artikelIIds.length; k++) {
									ArtikelDto artikelDto = getArtikelFac()
											.artikelFindByPrimaryKey(
													artikelIIds[k],
													theClientDto);
									ArtikelsprDto artikelsprDto = artikelDto
											.getArtikelsprDto();
									sbArtikelVollstaendigkeitIdent
											.append(artikelDto.getCNr());
									sbArtikelVollstaendigkeitIdent.append(", ");
									if (artikelsprDto != null) {
										sbArtikelVollstaendigkeitBez
												.append(artikelsprDto.getCBez());
										sbArtikelVollstaendigkeitBez
												.append(", ");
									}
								}
							}
						}
						posDto.setVollstaendigkeitKomponentenIdent(sbArtikelVollstaendigkeitIdent
								.toString());
						posDto.setVollstaendigkeitKomponentenBez(sbArtikelVollstaendigkeitBez
								.toString());
						String sSnr = null;
						if (aPositionDtos[i].getCSeriennrchargennr() != null) {
							sSnr = aPositionDtos[i].getCSeriennrchargennr();
						} else {
							sSnr = getAuftragpositionFac().getSeriennummmern(
									aPositionDtos[i].getIId(), theClientDto);
							if (sSnr.equals("")) {
								sSnr = null;
							}
						}
						// TODO: einzeln andrucken
						posDto.setSerienChargenNr(sSnr);

						StuecklisteDto stuecklisteDto = getStuecklisteFac()
								.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
										oArtikelDto.getIId(), theClientDto);
						if (stuecklisteDto != null) {
							StuecklistearbeitsplanDto[] sollDtos = getStuecklisteFac()
									.stuecklistearbeitsplanFindByStuecklisteIId(
											stuecklisteDto.getIId(),
											theClientDto);

							if (sollDtos.length > 0) {

								Object[][] oSubData = new Object[sollDtos.length][13];

								for (int j = 0; j < sollDtos.length; j++) {

									oSubData[j][0] = sollDtos[j]
											.getIArbeitsgang();

									ArtikelDto artikelDto = getArtikelFac()
											.artikelFindByPrimaryKeySmall(
													sollDtos[j].getArtikelIId(),
													theClientDto);
									oSubData[j][1] = artikelDto.getCNr();
									oSubData[j][2] = artikelDto
											.formatBezeichnung();

									BigDecimal losgroesse = new BigDecimal(1);

									if (aPositionDtos[i].getNMenge() != null) {
										losgroesse = aPositionDtos[i]
												.getNMenge();
									}

									oSubData[j][3] = Helper
											.berechneGesamtzeitInStunden(
													sollDtos[j].getLRuestzeit(),
													sollDtos[j]
															.getLStueckzeit(),
													losgroesse,
													stuecklisteDto
															.getIErfassungsfaktor(),
													sollDtos[j]
															.getIAufspannung());

									if (artikelDto.getArtgruIId() != null) {
										oSubData[j][4] = getArtikelFac()
												.artgruFindByPrimaryKey(
														artikelDto
																.getArtgruIId(),
														theClientDto).getCNr();
									}

									oSubData[j][5] = sollDtos[j]
											.getIUnterarbeitsgang();

									if (sollDtos[j].getMaschineIId() != null) {
										MaschineDto maschineDto = getZeiterfassungFac()
												.maschineFindByPrimaryKey(
														sollDtos[j]
																.getMaschineIId());
										oSubData[j][6] = maschineDto
												.getCIdentifikationsnr();
										oSubData[j][7] = maschineDto.getCBez();
									}
									if (sollDtos[j].getIMaschinenversatztage() != null) {
										oSubData[j][8] = sollDtos[j]
												.getIMaschinenversatztage();
									}

								}

								String[] fieldnames = new String[] {
										"F_AGNUMMER", "F_ARTIKEL",
										"F_BEZEICHNUNG", "F_SOLLZEIT",
										"F_ARTIKELGRUPPE", "F_UAGNUMMER",
										"F_MASCHINENIDENTIFIKATION",
										"F_MASCHINENBEZEICHNUNG", "F_AGBEGINN" };
								posDto.setArbeitsgaenge(new LPDatenSubreport(
										oSubData, fieldnames));
							}
						}
					} else if (aPositionDtos[i].getPositionsartCNr().equals(
							AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE)) {
						if (oArtikelDto.getArtikelsprDto() != null) {
							sbArtikelInfo.append(oArtikelDto.getArtikelsprDto()
									.getCBez());

							if (oArtikelDto.getArtikelsprDto().getCZbez() != null) {
								sbArtikelInfo.append("\n").append(
										oArtikelDto.getArtikelsprDto()
												.getCZbez());
							}
						} else {
							sbArtikelInfo.append(aPositionDtos[i].getCBez());
							if (aPositionDtos[i].getCZusatzbez() != null)
								sbArtikelInfo.append("\n").append(
										aPositionDtos[i].getCZusatzbez());
						}
					}
					posDto.setIdent(sbIdent.toString());
					posDto.setBezeichnung(sbArtikelInfo.toString());
					posDto.setGesamtMenge(aPositionDtos[i].getNMenge());

					if (aPositionDtos[i].getNMenge().intValue() > 0)
						posDto.setVorzeichen(new Integer(0));

					if (aPositionDtos[i].getNMenge().intValue() < 0)
						posDto.setVorzeichen(new Integer(1));
					posDto.setPositionsTerminString(Helper.formatTimestamp(
							aPositionDtos[i].getTUebersteuerbarerLiefertermin(),
							clientLocale));
					posDto.setPositionsTerminTimestamp(aPositionDtos[i]
							.getTUebersteuerbarerLiefertermin());
					posDto.setPositionsStatus(aPositionDtos[i]
							.getAuftragpositionstatusCNr());
					BigDecimal bdFiktiverLagerstand = Helper
							.getBigDecimalNull();
					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(
									aPositionDtos[i].getArtikelIId(),
									theClientDto);

					getInternebestellungFac()
							.getFiktivenLagerstandZuZeitpunkt(
									artikelDto,
									theClientDto,
									aPositionDtos[i]
											.getTUebersteuerbarerLiefertermin());
					posDto.setFiktiverLagerstand(bdFiktiverLagerstand);

					posDto.setOffeneMenge(aPositionDtos[i].getNOffeneMenge());

					BigDecimal bdLagerstand = Helper.getBigDecimalNull();
					String sLagerplatz = null;

					if (Helper.short2boolean(oArtikelDto
							.getBLagerbewirtschaftet())) {
						// das Hauptlager des Mandanten bestimmen
						LagerDto hauptlager = getLagerFac()
								.getHauptlagerDesMandanten(theClientDto);

						bdLagerstand = getLagerFac().getLagerstandOhneExc(
								oArtikelDto.getIId(), hauptlager.getIId(),
								theClientDto);

						sLagerplatz = getLagerFac()
								.getLagerplaezteEinesArtikels(
										oArtikelDto.getIId(),
										hauptlager.getIId());

					}

					posDto.setLagerstand(bdLagerstand);
					posDto.setLagerplatz(sLagerplatz);

					BigDecimal bdGewichtkg = null;

					if (oArtikelDto.getFGewichtkg() != null) {
						bdGewichtkg = new BigDecimal(oArtikelDto
								.getFGewichtkg().doubleValue())
								.multiply(aPositionDtos[i].getNOffeneMenge());
						bdGewichtkg = Helper.rundeKaufmaennisch(bdGewichtkg, 4);
					}

					posDto.setGewicht(bdGewichtkg);
					if (oArtikelDto.getMontageDto() != null) {
						posDto.setRasterLiegend(oArtikelDto.getMontageDto()
								.getFRasterliegend());
						posDto.setRasterStehend(oArtikelDto.getMontageDto()
								.getFRasterstehend());
					}
					posDto.setMaterialgewicht(oArtikelDto.getFMaterialgewicht());

					// Staerke/Hoehe
					if (oArtikelDto.getGeometrieDto() != null) {

						posDto.setHoehe(oArtikelDto.getGeometrieDto()
								.getFHoehe());
						posDto.setBreite(oArtikelDto.getGeometrieDto()
								.getFBreite());
						posDto.setTiefe(oArtikelDto.getGeometrieDto()
								.getFTiefe());
					}

					// Verpackung
					if (oArtikelDto.getVerpackungDto() != null) {
						posDto.setBauform(oArtikelDto.getVerpackungDto()
								.getCBauform());
						posDto.setVerpackungsart(oArtikelDto.getVerpackungDto()
								.getCVerpackungsart());
					}
					if (oArtikelDto.getFarbcodeIId() != null) {
						posDto.setFarbcode(getArtikelFac()
								.farbcodeFindByPrimaryKey(
										oArtikelDto.getFarbcodeIId()).getCNr());
					}

					// Material
					if (oArtikelDto.getMaterialIId() != null) {
						MaterialDto materialDto = getMaterialFac()
								.materialFindByPrimaryKey(
										oArtikelDto.getMaterialIId(),
										theClientDto);
						posDto.setMaterial(materialDto.getBezeichnung());
					}

					if (oArtikelDto.getArtklaIId() != null) {
						ArtklaDto artklaDto = getArtikelFac()
								.artklaFindByPrimaryKey(
										oArtikelDto.getArtklaIId(),
										theClientDto);
						posDto.setArtikelklasse(artklaDto.getBezeichnung());
					}
					if (oArtikelDto.getArtgruIId() != null) {
						ArtgruDto artgruDto = getArtikelFac()
								.artgruFindByPrimaryKey(
										oArtikelDto.getArtgruIId(),
										theClientDto);
						posDto.setArtikelgruppe(artgruDto.getBezeichnung());
					}

					posDto.setVerkaufsEAN(oArtikelDto.getCVerkaufseannr());
					posDto.setVerpackungsEAN(oArtikelDto.getCVerpackungseannr());
					posDto.setVerpackungsmenge(oArtikelDto
							.getFVerpackungsmenge());

				} else {
					// Texteingabe Positionen
					if (aPositionDtos[i].getPositionsartCNr().equals(
							AuftragServiceFac.AUFTRAGPOSITIONART_TEXTEINGABE)) {

						posDto.setFreierText(aPositionDtos[i].getXTextinhalt());

					}
					// Textbaustein Positionen
					if (aPositionDtos[i].getPositionsartCNr().equals(
							AuftragServiceFac.AUFTRAGPOSITIONART_TEXTBAUSTEIN)) {
						// Dto holen
						MediastandardDto oMediastandardDto = getMediaFac()
								.mediastandardFindByPrimaryKey(
										aPositionDtos[i].getMediastandardIId());
						// zum Drucken vorbereiten
						BelegPositionDruckTextbausteinDto druckDto = printTextbaustein(
								oMediastandardDto, theClientDto);
						posDto.setFreierText(druckDto.getSFreierText());
						posDto.setKommentarImage(druckDto.getOImage());
					}
					// Betrifft Positionen
					if (aPositionDtos[i].getPositionsartCNr().equals(
							AuftragServiceFac.AUFTRAGPOSITIONART_BETRIFFT)) {
						posDto.setFreierText(aPositionDtos[i].getCBez());
					}
				}
				dataList.add(posDto);
			}

			if (auftragSNR) {
				List<List<AuftragPacklistePositionDto>> allPosPerSnr = splitBySnr(dataList);

				// jede der innere Liste kommt auf eine eigene Seite
				List<List<AuftragPacklistePositionDto>> snrGetrennt = new ArrayList<List<AuftragPacklistePositionDto>>();

				for (List<AuftragPacklistePositionDto> posPerSnr : allPosPerSnr) {
					// wenn die erste Position ohne SNR ist, kommt
					if (posPerSnr.get(0).getSerienChargenNr() == null) {
						snrGetrennt.add(posPerSnr);
					} else {
						// hier trennen wir die seriennummern der Position
						String[] sNrs = posPerSnr.get(0).getSerienChargenNr()
								.split(",");

						List<AuftragPacklistePositionDto> temp;
						// fuer jede seriennummer ...
						for (String snr : sNrs) {
							// ...gibt es eine eigene Seite mit den Positionen
							// welche in der Liste temp sind
							temp = new ArrayList<AuftragPacklistePositionDto>();
							// fast alle Eigentschaften sind gleich wie bei der
							// urspruenglichen Position
							AuftragPacklistePositionDto newPosSNR = (AuftragPacklistePositionDto) posPerSnr
									.get(0).clone();
							// auszer die SNR, welche jetzt nur noch eine der in
							// der urspruenglichen Position
							// zusammengefassten SNRs ist.
							newPosSNR.setSerienChargenNr(snr);
							// die Menge ist 1, wir haben ja nur noch eine SNR
							newPosSNR.setGesamtMenge(BigDecimal.ONE);
							newPosSNR.setMengenTeiler(sNrs.length);

							temp.add(newPosSNR);
							// jetzt alle weiteren Positionen ohne Seriennummer,
							// welche darauf folgen einfuegen
							for (int i = 1; i < posPerSnr.size(); i++) {
								AuftragPacklistePositionDto newPos = (AuftragPacklistePositionDto) posPerSnr
										.get(i).clone();
								// die weitern Positionen sind jetzt auf n
								// Seiten vorhanden. (n=anzahl der SNRs)
								// darum ist nur 1/n der Menge der Position pro
								// Seite noetig
								newPos.setMengenTeiler(sNrs.length);
								temp.add(newPos);
							}
							// die Positionsliste der Seite den schon
							// generierten Listen hinzufuegen
							snrGetrennt.add(temp);
						}
					}
				}

				data = new Object[snrGetrennt.size()][]; // fuer
															// snr
															// pos
															// je
															// ein
				int i = 0;
				for (List<AuftragPacklistePositionDto> posEinerSeite : snrGetrennt) {
					data[i] = new Object[AuftragReportFac.REPORT_PACKLISTE_ANZAHL_SPALTEN];
					data[i++][AuftragReportFac.REPORT_PACKLISTE_SUBREPORT_DATA] = packlistePositionListToSubreportData(posEinerSeite);
				}
			} else {
				data = new Object[dataList.size()][];
				int i = 0;
				for (AuftragPacklistePositionDto pos : dataList) {
					data[i++] = pos.toDataRow();
				}
			}

			if (bAuftragStundenMinuten) {
				// Sort by column 0 and print results
				// (see bottom for ArrayColumnComparator definition)
				Arrays.sort(data, new ArrayColumnComparator(
						AuftragReportFac.REPORT_PACKLISTE_LAGERORT));
			}
			// die Parameter dem Report uebergeben
			HashMap<String, Object> parameter = new HashMap<String, Object>();

			parameter.put(
					"Adressefuerausdruck",
					formatAdresseFuerAusdruck(kundeDto.getPartnerDto(),
							ansprechpartnerDto, mandantDto, clientLocale,
							LocaleFac.BELEGART_AUFTRAG));

			if (ansprechpartnerDto != null) {
				parameter.put(
						"P_ANSPRECHPARTNER_ADRESSBLOCK",
						getPartnerFac()
								.formatFixAnredeTitelName2Name1FuerAdresskopf(
										ansprechpartnerDto.getPartnerDto(),
										clientLocale, null));
			}

			SpediteurDto spediteurDto = getMandantFac()
					.spediteurFindByPrimaryKey(auftragDto.getSpediteurIId());
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
								theClientDto.getLocUi()));
			}

			parameter.put("Belegkennung",
					baueKennungAuftragbestaetigung(auftragDto, theClientDto));
			parameter.put(
					"VertreterAnrede",
					baueAnredeVertreter(auftragDto.getPersonalIIdVertreter(),
							clientLocale, theClientDto));

			// die Lieferart
			parameter.put(
					"Lieferart",
					getLocaleFac().lieferartFindByIIdLocaleOhneExc(
							auftragDto.getLieferartIId(), clientLocale,
							theClientDto));

			parameter.put("Liefertermin", Helper.formatDatum(
					auftragDto.getDLiefertermin(), clientLocale));
			parameter.put("P_LIEFERTERMIN_TIMESTAMP",
					auftragDto.getDLiefertermin());
			parameter.put("P_FINALTERMIN", auftragDto.getDFinaltermin());
			parameter.put("P_BESTELLDATUM", auftragDto.getDBestelldatum());
			parameter.put("Auftragnummer", auftragDto.getCNr());
			parameter
					.put("P_ROHS", Helper.short2Boolean(auftragDto.getBRoHs()));
			parameter.put("P_BESTELLNUMMER", auftragDto.getCBestellnummer());

			parameter.put("P_PROJEKTBEZEICHNUNG",
					auftragDto.getCBezProjektbezeichnung()); // ());

			if (auftragDto.getKostIId() != null) {
				KostenstelleDto kostenstelleDto = getSystemFac()
						.kostenstelleFindByPrimaryKey(auftragDto.getKostIId());
				parameter.put(LPReport.P_KOSTENSTELLE,
						kostenstelleDto.formatKostenstellenbezeichnung());
			}

			KundeDto kundeDtoLieferadresse = getKundeFac()
					.kundeFindByPrimaryKey(
							auftragDto.getKundeIIdLieferadresse(), theClientDto);
			parameter.put(
					"P_LIEFERADRESSE",
					formatAdresseFuerAusdruck(
							kundeDtoLieferadresse.getPartnerDto(),
							ansprechpartnerDto, mandantDto, clientLocale));
			parameter.put("P_LIEFERDAUER",
					kundeDtoLieferadresse.getILieferdauer());
			// AUFTRAGSEIGENSCHAFTEN
			Hashtable<?, ?> hAE = getAuftragEigenschaften(auftragDto.getIId(),
					theClientDto);
			if (hAE != null) {
				parameter.put(P_AUFTRAGEIGENSCHAFT_FA,
						hAE.get(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_FA));
				parameter
						.put(P_AUFTRAGEIGENSCHAFT_CLUSTER,
								hAE.get(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_CLUSTER));
				parameter.put(P_AUFTRAGEIGENSCHAFT_EQNR,
						hAE.get(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_EQNR));
			}
			// LosNr zu Auftrag suchen.
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;
			StringBuffer buff = new StringBuffer();
			try {
				session = factory.openSession();
				Criteria crit = session.createCriteria(FLRLosReport.class);
				crit.add(Restrictions.eq("flrauftrag.i_id", iIdAuftragI));
				List<?> resultList = crit.list();
				Iterator<?> it = resultList.iterator();
				while (it.hasNext()) {
					FLRLosReport flrlos = (FLRLosReport) it.next();
					buff.append(flrlos.getC_nr());
					if (it.hasNext()) {
						buff.append(" ");
					}
				}
				if (buff.length() != 0) {
					parameter.put("P_LOSNUMMER", buff.toString());
				}
			} finally {
				try {
					session.close();
				} catch (HibernateException he) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE,
							he);
				}
			}

			initJRDS(parameter, AuftragReportFac.REPORT_MODUL,
					cAktuellerReport, theClientDto.getMandant(),
					theClientDto.getLocUi(), theClientDto, true,
					auftragDto.getKostIId());

			jasperPrint = getReportPrint();
		} catch (RemoteException ex) {
			// exccatch: immer so!
			throwEJBExceptionLPRespectOld(ex);
		}
		return jasperPrint;
	}

	/**
	 * Trennt die Positionen bei jeder Seriennummer in eine neue Liste. Die
	 * Positionen mit Seriennummer(n) stehen immer an erster Stelle der inneren
	 * Listen, gefolgt von allen folgenden Positionen welche keine Seriennummer
	 * haben. Die n&auml;chste seriennummernbehaftete Position bildet das erste
	 * Element der n&auml;chsten Liste. Dadurch kann die erste Liste eventuell
	 * auch keine Position mit Serienummer haben.
	 * 
	 * @param positionen
	 * @return eine Liste von Positionslisten, welche nach
	 *         Seriennummernzugeh&ouml;rigkeit getrennt und sortiert sind.
	 */
	private List<List<AuftragPacklistePositionDto>> splitBySnr(
			List<AuftragPacklistePositionDto> positionen) {
		List<List<AuftragPacklistePositionDto>> returnList = new ArrayList<List<AuftragPacklistePositionDto>>();
		List<AuftragPacklistePositionDto> temp = new ArrayList<AuftragPacklistePositionDto>();
		for (AuftragPacklistePositionDto pos : positionen) {
			// falls es eine SNR gibt und in der Liste temp schon Positionen
			// sind...
			if (pos.getSerienChargenNr() != null && temp.size() > 0) {
				// ... fuege temp der Rueckgabeliste hinzu
				returnList.add(temp);
				// und mach aus temp eine neue Liste, denn jetzt kommen die
				// Positionen
				// welche zur naechsten SNR gehoeren
				temp = new ArrayList<AuftragPacklistePositionDto>();
			}
			temp.add(pos);
		}
		// weil beim letzten Durchlauf temp nicht hinzugefuegt wird
		if (temp.size() > 0)
			returnList.add(temp);
		return returnList;
	}

	private LPDatenSubreport packlistePositionListToSubreportData(
			List<AuftragPacklistePositionDto> list) {
		Object[][] data = new Object[list.size()][];
		int i = 0;
		for (AuftragPacklistePositionDto pos : list) {
			data[i++] = pos.toDataRow();
		}
		return new LPDatenSubreport(data, new String[] { "Ident",
				"Bezeichnung", "Gesamtmenge", "Offenemenge", "Lagerstand",
				"Lagerort", "Gewicht", "F_RASTER_LIEGEND", "F_RASTER_STEHEND",
				"F_MATERIALGEWICHT", "F_SERIENCHARGENR", "F_FREIERTEXT",
				"F_IMAGE",
				"F_VOLLSTAENDIGKEIT_IDENT",
				"F_VOLLSTAENDIGKEIT_BEZ",
				"F_VORZEICHEN",
				"F_POSITIONSTERMIN",
				"F_POSITIONSTERMIN_TIMESTAMP",
				"F_FIKTIVERLAGERSTAND",
				"F_ARTIKELKLASSE",
				"F_ARTIKELGRUPPE",
				"null",
				"null", // die Zwei gibts nicht mehr,
				// duerfen aber nicht "" sein, da auf endsWith() geprueft wird

				"F_FARBCODE", "F_MATERIAL", "F_ARTIKEL_HOEHE",
				"F_ARTIKEL_BREITE", "F_ARTIKEL_TIEFE", "F_ARTIKEL_BAUFORM",
				"F_ARTIKEL_VERPACKUNGSART", "F_ARTIKELKOMMENTAR",
				"F_VERKAUFSEAN", "F_VERPACKUNGSMENGE", "F_VERPACKUNGSEAN",
				"F_ARBEITSGAENGE", "F_MENGENTEILER" });
	}

	public JasperPrintLP printAuftragSrnnrnEtikett(Integer iIdAuftragI,
			Integer iIdAuftragpositionI, String cAktuellerReport,
			TheClientDto theClientDto) throws EJBExceptionLP {

		checkAuftragIId(iIdAuftragI);
		this.cAktuellerReport = cAktuellerReport;
		JasperPrintLP jasperPrint = null;
		try {
			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(
					iIdAuftragI);
			AuftragpositionDto auftragpositionDto = getAuftragpositionFac()
					.auftragpositionFindByPrimaryKey(iIdAuftragpositionI);
			AuftragseriennrnDto auftragseriennrnDto = getAuftragpositionFac()
					.auftragseriennrnFindByAuftragpsotionIId(
							iIdAuftragpositionI, theClientDto);
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					auftragDto.getKundeIIdAuftragsadresse(), theClientDto);
			// AUFTRAGSEIGENSCHAFTEN
			Hashtable<?, ?> hAE = getAuftragEigenschaften(auftragDto.getIId(),
					theClientDto);
			// Index
			String stklIndex = null;
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;
			try {
				session = factory.openSession();
				Criteria crit = session
						.createCriteria(FLRStuecklisteeigenschaft.class);
				Criteria cStueckliste = crit.createCriteria("flrstueckliste");
				Criteria cArt = crit
						.createCriteria("flrstuecklisteeigenschaftart");
				cStueckliste.add(Restrictions.eq("artikel_i_id",
						auftragpositionDto.getArtikelIId()));
				cStueckliste.add(Restrictions.eq("mandant_c_nr",
						auftragDto.getMandantCNr()));
				cArt.add(Restrictions.eq("c_bez", "Index"));
				List<?> resultList = crit.list();
				Iterator<?> it = resultList.iterator();
				if (it.hasNext()) {
					FLRStuecklisteeigenschaft flr = (FLRStuecklisteeigenschaft) it
							.next();
					stklIndex = flr.getC_bez();
				}
			} finally {
				session.close();
			}
			Locale locDruck = Helper.string2Locale(kundeDto.getPartnerDto()
					.getLocaleCNrKommunikation());
			Locale clientLocale = theClientDto.getLocUi();
			Timestamp pruefTermin = new Timestamp(System.currentTimeMillis());
			int iAnzahlZeilen = 1;
			data = new Object[iAnzahlZeilen][AuftragReportFac.REPORT_SRN_ETIKETT_ANZAHL_SPALTEN];
			for (int i = 0; i < iAnzahlZeilen; i++) {
				// Ident
				if (auftragpositionDto.getArtikelIId() != null) {
					ArtikelDto oArtikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(
									auftragpositionDto.getArtikelIId(),
									theClientDto);
					if (auftragpositionDto.getPositioniId() != null) {
						data[i][AuftragReportFac.REPORT_SRN_ETIKETT_IDENT] = buildZeileIdentPosition(
								auftragpositionDto.getPositioniId(),
								theClientDto);
						data[i][AuftragReportFac.REPORT_SRN_ETIKETT_BEZEICHNUNG] = buildZeileBezPosition(
								auftragpositionDto.getPositioniId(),
								theClientDto);
					} else {
						data[i][AuftragReportFac.REPORT_SRN_ETIKETT_IDENT] = oArtikelDto
								.getCNr();
						data[i][AuftragReportFac.REPORT_SRN_ETIKETT_BEZEICHNUNG] = oArtikelDto
								.formatArtikelbezeichnung();
					}

					data[i][AuftragReportFac.REPORT_SRN_ETIKETT_PRUEFTERMIN] = Helper
							.formatDatum(pruefTermin, clientLocale);
					data[i][AuftragReportFac.REPORT_SRN_ETIKETT_LIEFERTERMIN] = Helper
							.formatDatum(auftragpositionDto
									.getTUebersteuerbarerLiefertermin(),
									clientLocale);
					if (stklIndex != null) {
						data[i][AuftragReportFac.REPORT_SRN_ETIKETT_STLK_INDEX] = stklIndex;
					}
					if (hAE != null) {
						data[i][AuftragReportFac.REPORT_SRN_ETIKETT_FA] = hAE
								.get(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_FA);
						data[i][AuftragReportFac.REPORT_SRN_ETIKETT_CLUSTER] = hAE
								.get(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_CLUSTER);
						data[i][AuftragReportFac.REPORT_SRN_ETIKETT_EQNR] = hAE
								.get(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_EQNR);
					}
					if (kundeDto.getCLieferantennr() != null) {
						data[i][AuftragReportFac.REPORT_SRN_ETIKETT_LIEFERANTENNUMMER] = kundeDto
								.getCLieferantennr();
					}
					data[i][AuftragReportFac.REPORT_SRN_ETIKETT_KUNDENNAME] = kundeDto
							.getPartnerDto().formatFixName1Name2();
					data[i][AuftragReportFac.REPORT_SRN_ETIKETT_KUNDENADRESSE] = kundeDto
							.getPartnerDto().formatAdresse();
					if (auftragseriennrnDto != null) {
						data[i][AuftragReportFac.REPORT_SRN_ETIKETT_SRNNR] = auftragseriennrnDto
								.getCSeriennr();
						data[i][AuftragReportFac.REPORT_SRN_ETIKETT_KOMMENTAR] = auftragseriennrnDto
								.getCKommentar();
					}
					KundeDto lieferkundeDto = getKundeFac()
							.kundeFindByPrimaryKey(
									auftragDto.getKundeIIdLieferadresse(),
									theClientDto);
					data[i][AuftragReportFac.REPORT_SRN_ETIKETT_LIEFERADRESSE] = lieferkundeDto
							.getPartnerDto().formatAdresse();
				} else {
					if (auftragpositionDto.getPositionsartCNr().equals(
							LocaleFac.POSITIONSART_POSITION)) {
						if (kundeDto.getCLieferantennr() != null) {
							data[i][AuftragReportFac.REPORT_SRN_ETIKETT_LIEFERANTENNUMMER] = kundeDto
									.getCLieferantennr();
						}
						data[i][AuftragReportFac.REPORT_SRN_ETIKETT_KUNDENNAME] = kundeDto
								.getPartnerDto().formatFixName1Name2();
						data[i][AuftragReportFac.REPORT_SRN_ETIKETT_KUNDENADRESSE] = kundeDto
								.getPartnerDto().formatAdresse();
						if (hAE != null) {
							data[i][AuftragReportFac.REPORT_SRN_ETIKETT_FA] = hAE
									.get(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_FA);
							data[i][AuftragReportFac.REPORT_SRN_ETIKETT_CLUSTER] = hAE
									.get(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_CLUSTER);
							data[i][AuftragReportFac.REPORT_SRN_ETIKETT_EQNR] = hAE
									.get(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_EQNR);
						}
						data[i][AuftragReportFac.REPORT_SRN_ETIKETT_IDENT] = buildZeileIdentPosition(
								auftragpositionDto.getIId(), theClientDto);
						data[i][AuftragReportFac.REPORT_SRN_ETIKETT_PRUEFTERMIN] = Helper
								.formatDatum(pruefTermin, clientLocale);
						if (auftragpositionDto
								.getTUebersteuerbarerLiefertermin() != null) {
							data[i][AuftragReportFac.REPORT_SRN_ETIKETT_LIEFERTERMIN] = Helper
									.formatDatum(
											auftragpositionDto
													.getTUebersteuerbarerLiefertermin(),
											clientLocale);
						}
						if (stklIndex != null) {
							data[i][AuftragReportFac.REPORT_SRN_ETIKETT_STLK_INDEX] = stklIndex;
						}
						if (auftragseriennrnDto != null) {
							data[i][AuftragReportFac.REPORT_SRN_ETIKETT_SRNNR] = auftragseriennrnDto
									.getCSeriennr();
							data[i][AuftragReportFac.REPORT_SRN_ETIKETT_KOMMENTAR] = auftragseriennrnDto
									.getCKommentar();
						}
						KundeDto lieferkundeDto = getKundeFac()
								.kundeFindByPrimaryKey(
										auftragDto.getKundeIIdLieferadresse(),
										theClientDto);
						data[i][AuftragReportFac.REPORT_SRN_ETIKETT_LIEFERADRESSE] = lieferkundeDto
								.getPartnerDto().formatAdresse();
					}
				}
			}

			// die Parameter dem Report uebergeben
			HashMap<String, Object> parameter = new HashMap<String, Object>();
			initJRDS(parameter, AuftragReportFac.REPORT_MODUL,
					cAktuellerReport, theClientDto.getMandant(),
					theClientDto.getLocUi(), theClientDto, true,
					auftragDto.getKostIId());

			jasperPrint = getReportPrint();
		} catch (RemoteException ex) {
			// exccatch: immer so!
			throwEJBExceptionLPRespectOld(ex);
		}
		return jasperPrint;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAuftragspositionsetikett(
			Integer auftragpositionIId, TheClientDto theClientDto) {
		cAktuellerReport = AuftragReportFac.REPORT_AUFTRAGSPOSITIONSETIKETT;
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		try {
			AuftragpositionDto posDto = getAuftragpositionFac()
					.auftragpositionFindByPrimaryKey(auftragpositionIId);
			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(
					posDto.getBelegIId());

			parameter.put("P_AUFTRAG_DTO", auftragDto);
			parameter.put(
					"P_POSITIONSOBJEKT",
					getSystemReportFac().getPositionForReport(
							LocaleFac.BELEGART_AUFTRAG, auftragpositionIId,
							theClientDto));

			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					auftragDto.getKundeIIdAuftragsadresse(), theClientDto);

			AnsprechpartnerDto oAnsprechpartnerDto = null;

			if (auftragDto.getAnsprechparnterIId() != null) {
				oAnsprechpartnerDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(
								auftragDto.getAnsprechparnterIId(),
								theClientDto);
			}

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);

			// es gilt das Locale des Lieferscheinkunden
			Locale locDruck = Helper.string2Locale(kundeDto.getPartnerDto()
					.getLocaleCNrKommunikation());

			parameter.put("P_MANDANTADRESSE",
					Helper.formatMandantAdresse(mandantDto));

			String sAdressefuerausdruck = formatAdresseFuerAusdruck(
					kundeDto.getPartnerDto(), oAnsprechpartnerDto, mandantDto,
					locDruck);

			parameter.put("P_KUNDE_ADRESSBLOCK", sAdressefuerausdruck);

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		data = new Object[0][0];

		initJRDS(parameter, AuftragReportFac.REPORT_MODUL, cAktuellerReport,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAuftragstatistik(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, boolean bAuftragsdatum,
			Integer kundeIId_Auftragsadresse,
			Integer kundeIId_Rechnungsadresse, Integer auftragIId,
			String cProjekt, String cBestellnummer, int iOptionSortierung,
			boolean bArbeitszeitVerdichtet, java.sql.Timestamp tStichtag,
			Integer projektIId, TheClientDto theClientDto) {

		cAktuellerReport = AuftragReportFac.REPORT_AUFTRAGSTATISTIK;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		tVon = Helper.cutTimestamp(tVon);
		tBis = Helper.cutTimestamp(tBis);

		if (tStichtag != null) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(tStichtag.getTime());
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			c.set(Calendar.MILLISECOND, 999);
			tStichtag = new Timestamp(c.getTimeInMillis());
		}
		// die Parameter dem Report uebergeben
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		org.hibernate.Criteria crit = session
				.createCriteria(FLRAuftragReport.class);
		crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));
		if (projektIId == null) {

			// crit.add(Restrictions.eq("c_nr", "11/0012878"));
			if (bAuftragsdatum) {
				crit.add(Restrictions.ge(AuftragFac.FLR_AUFTRAG_D_BELEGDATUM,
						tVon));
				crit.add(Restrictions.lt(AuftragFac.FLR_AUFTRAG_D_BELEGDATUM,
						tBis));

			} else {

				crit.add(Restrictions.ge(AuftragFac.FLR_AUFTRAG_T_ERLEDIGT,
						tVon));
				crit.add(Restrictions.lt(AuftragFac.FLR_AUFTRAG_T_ERLEDIGT,
						tBis));
			}

			if (auftragIId != null) {
				crit.add(Restrictions.eq("i_id", auftragIId));

				AuftragDto auftragDto = getAuftragFac()
						.auftragFindByPrimaryKey(auftragIId);
				parameter.put("P_AUFTRAG", auftragDto.getCNr());
			}
		} else {

			// Projektklammer

			// Alle Auftraege des Projekts andrucken

			Session sessionAB = FLRSessionFactory.getFactory().openSession();

			Query queryAB = sessionAB
					.createQuery("SELECT ab FROM FLRAuftrag ab left join ab.flrangebot as ag WHERE ag.projekt_i_id="
							+ projektIId
							+ " OR ab.projekt_i_id="
							+ projektIId
							+ "  ORDER BY ab.c_nr ASC");

			List<?> resultListAB = queryAB.list();
			Iterator<?> resultListIteratorAB = resultListAB.iterator();

			ArrayList alAuftraege = new ArrayList();

			while (resultListIteratorAB.hasNext()) {

				FLRAuftrag flrauftrag = (FLRAuftrag) resultListIteratorAB
						.next();
				alAuftraege.add(flrauftrag.getI_id());
			}

			sessionAB.close();

			crit.add(Restrictions.in("i_id", alAuftraege));

			try {
				ProjektDto pjDto = getProjektFac().projektFindByPrimaryKey(
						projektIId);
				parameter.put("P_PROJEKT", pjDto.getCNr());
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}

		if (cProjekt != null) {
			crit.add(Restrictions.eq(
					AuftragFac.FLR_AUFTRAG_C_BEZ_PROJEKTBEZEICHNUNG, cProjekt));
			parameter.put("P_PROJEKT", cProjekt);
		}
		if (cBestellnummer != null) {
			crit.add(Restrictions.eq(AuftragFac.FLR_AUFTRAG_C_BESTELLNUMMER,
					cBestellnummer));
			parameter.put("P_BESTELLNUMMER", cBestellnummer);
		}

		if (kundeIId_Auftragsadresse != null) {
			crit.add(Restrictions.eq(
					AuftragFac.FLR_AUFTRAG_KUNDE_I_ID_AUFTRAGSADRESSE,
					kundeIId_Auftragsadresse));
			parameter.put(
					"P_KUNDE",
					getKundeFac()
							.kundeFindByPrimaryKey(kundeIId_Auftragsadresse,
									theClientDto).getPartnerDto()
							.formatFixTitelName1Name2());

		}
		if (kundeIId_Rechnungsadresse != null) {
			crit.add(Restrictions.eq(
					AuftragFac.FLR_AUFTRAG_KUNDE_I_ID_RECHNUNGSADRESSE,
					kundeIId_Rechnungsadresse));
			parameter.put(
					"P_RECHNUNGSADRESSE",
					getKundeFac()
							.kundeFindByPrimaryKey(kundeIId_Rechnungsadresse,
									theClientDto).getPartnerDto()
							.formatFixTitelName1Name2());
		}

		String sortierung = "";
		if (iOptionSortierung == AuftragReportFac.REPORT_AUFTRAGSTATISTIK_SORTIERUNG_AUFTRAG) {
			crit.addOrder(Order.asc(AuftragFac.FLR_AUFTRAG_C_NR));
			sortierung = getTextRespectUISpr("lp.auftrag",
					theClientDto.getMandant(), theClientDto.getLocUi());

		} else if (iOptionSortierung == AuftragReportFac.REPORT_AUFTRAGSTATISTIK_SORTIERUNG_BESTELLNUMMER) {
			crit.addOrder(Order.asc(AuftragFac.FLR_AUFTRAG_C_BESTELLNUMMER));
			sortierung = getTextRespectUISpr("lp.bestellnummer",
					theClientDto.getMandant(), theClientDto.getLocUi());
		} else if (iOptionSortierung == AuftragReportFac.REPORT_AUFTRAGSTATISTIK_SORTIERUNG_PROJEKT) {
			crit.addOrder(Order
					.asc(AuftragFac.FLR_AUFTRAG_C_BEZ_PROJEKTBEZEICHNUNG));
			sortierung = getTextRespectUISpr("lp.projekt",
					theClientDto.getMandant(), theClientDto.getLocUi());
		} else if (iOptionSortierung == AuftragReportFac.REPORT_AUFTRAGSTATISTIK_SORTIERUNG_KUNDE_AUFTRAGSADRESSE) {
			crit.createAlias(AuftragFac.FLR_AUFTRAG_FLRKUNDE, "k")
					.createAlias("k." + KundeFac.FLR_PARTNER, "p")
					.addOrder(
							Order.asc("p."
									+ PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1));
			sortierung = getTextRespectUISpr("lp.kunde",
					theClientDto.getMandant(), theClientDto.getLocUi());
		} else if (iOptionSortierung == AuftragReportFac.REPORT_AUFTRAGSTATISTIK_SORTIERUNG_KUNDE_RECHNUNGSADRESSE) {
			crit.createAlias(AuftragFac.FLR_AUFTRAG_FLRKUNDERECHNUNGSADRESSE,
					"k")
					.createAlias("k." + KundeFac.FLR_PARTNER, "p")
					.addOrder(
							Order.asc("p."
									+ PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1));
			sortierung = getTextRespectUISpr("lp.rechnungsadresse",
					theClientDto.getMandant(), theClientDto.getLocUi());
		}

		List<?> resultList = crit.list();

		Iterator<?> resultListIterator = resultList.iterator();

		ArrayList<Object[]> daten = new ArrayList<Object[]>();
		int iGesamtSize = 0;
		while (resultListIterator.hasNext()) {
			FLRAuftragReport zeile = (FLRAuftragReport) resultListIterator
					.next();

			ArrayList<?> al = getDataAuftragNachkalkulation(zeile.getI_id(),
					bArbeitszeitVerdichtet, tStichtag, theClientDto);

			iGesamtSize += al.size();

			Object[] temp = new Object[4];
			temp[0] = al;

			temp[3] = getAuftragFac().auftragFindByPrimaryKey(zeile.getI_id());

			if (iOptionSortierung == AuftragReportFac.REPORT_AUFTRAGSTATISTIK_SORTIERUNG_BESTELLNUMMER) {
				temp[1] = zeile.getC_bestellnummer();
				temp[2] = zeile.getC_bestellnummer();
				daten.add(temp);
			} else if (iOptionSortierung == AuftragReportFac.REPORT_AUFTRAGSTATISTIK_SORTIERUNG_PROJEKT) {
				temp[1] = zeile.getC_bez();
				temp[2] = zeile.getC_bez();
				daten.add(temp);

			} else if (iOptionSortierung == AuftragReportFac.REPORT_AUFTRAGSTATISTIK_SORTIERUNG_KUNDE_AUFTRAGSADRESSE) {

				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						zeile.getKunde_i_id_auftragsadresse(), theClientDto);
				daten.add(temp);
				temp[1] = kundeDto.getIId();
				temp[2] = kundeDto.getPartnerDto().formatFixTitelName1Name2();

			} else if (iOptionSortierung == AuftragReportFac.REPORT_AUFTRAGSTATISTIK_SORTIERUNG_KUNDE_RECHNUNGSADRESSE) {

				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						zeile.getKunde_i_id_rechnungsadresse(), theClientDto);
				daten.add(temp);
				temp[1] = kundeDto.getIId();
				temp[2] = kundeDto.getPartnerDto().formatFixTitelName1Name2();

			} else {
				temp[1] = "";
				daten.add(temp);
			}
			parameter.put("P_PROJEKTBEZEICHNUNG", zeile.getC_bez());
		}

		data = new Object[iGesamtSize][REPORT_STATISTIK_ANZAHL_SPALTEN];
		int row = 0;

		for (int i = 0; i < daten.size(); i++) {

			Object[] o = (Object[]) daten.get(i);

			ArrayList<?> nachkalk = (ArrayList<?>) o[0];
			Object gruppierung = o[1];
			String gruppierungBez = (String) o[2];
			AuftragDto auftragDto = (AuftragDto) o[3];
			for (int j = 0; j < nachkalk.size(); j++) {

				AuftragNachkalkulationDto oCurrentDto = (AuftragNachkalkulationDto) nachkalk
						.get(j);

				data[row][AuftragReportFac.REPORT_STATISTIK_GRUPPIERUNG] = gruppierung;
				data[row][AuftragReportFac.REPORT_STATISTIK_GRUPPIERUNGBEZEICHNUNG] = gruppierungBez;
				data[row][AuftragReportFac.REPORT_STATISTIK_AUFTRAGSNUMMER] = auftragDto
						.getCNr();

				data[row][AuftragReportFac.REPORT_STATISTIK_FUEHRENDER_ARTIKEL] = "";
				try {
					AuftragpositionDto[] posDtos = getAuftragpositionFac()
							.auftragpositionFindByAuftrag(auftragDto.getIId());
					for (int k = 0; k < posDtos.length; k++) {
						if (posDtos[k].getPositionsartCNr().equals(
								AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)
								&& posDtos[k].getArtikelIId() != null) {
							ArtikelDto aDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(
											posDtos[k].getArtikelIId(),
											theClientDto);
							data[row][AuftragReportFac.REPORT_STATISTIK_FUEHRENDER_ARTIKEL] = aDto
									.getCNr();
							break;
						}
					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				data[row][AuftragReportFac.REPORT_STATISTIK_BELEGART] = oCurrentDto
						.getSBelegart();
				data[row][AuftragReportFac.REPORT_STATISTIK_BELEGNUMMER] = oCurrentDto
						.getSBelegnummer();
				data[row][AuftragReportFac.REPORT_STATISTIK_BELEGSTATUS] = oCurrentDto
						.getSBelegstatus();
				data[row][AuftragReportFac.REPORT_STATISTIK_RECHNUNGSART] = oCurrentDto
						.getSRechnungsart();
				data[row][AuftragReportFac.REPORT_STATISTIK_ZAHLBETRAG] = oCurrentDto
						.getBdZahlbetrag();
				data[row][AuftragReportFac.REPORT_STATISTIK_PROJEKT] = oCurrentDto
						.getAuftragDto().getCBezProjektbezeichnung();
				data[row][AuftragReportFac.REPORT_STATISTIK_BESTELLNUMMER] = oCurrentDto
						.getAuftragDto().getCBestellnummer();
				data[row][AuftragReportFac.REPORT_STATISTIK_RE_BESTELLNUMMER] = oCurrentDto
						.getReBestellnummer();
				data[row][AuftragReportFac.REPORT_STATISTIK_ER_LIEFERANT] = oCurrentDto
						.getErLieferant();
				data[row][AuftragReportFac.REPORT_STATISTIK_LOS_KOMMENTAR] = oCurrentDto
						.getSLoskommentar();
				data[row][AuftragReportFac.REPORT_STATISTIK_LOS_PROJEKT] = oCurrentDto
						.getSLosprojekt();

				data[row][AuftragReportFac.REPORT_STATISTIK_LOS_ERLEDIGUNGSDATUM] = oCurrentDto
						.gettLoserledigungsdatum();
				data[row][AuftragReportFac.REPORT_STATISTIK_LOS_BEWERTUNG] = oCurrentDto
						.getdLosbewertung();

				data[row][AuftragReportFac.REPORT_STATISTIK_LOS_STUECKLISTE_ARTIKELNUMMER] = oCurrentDto
						.getSLosstuecklistenartikelnummer();
				data[row][AuftragReportFac.REPORT_STATISTIK_LOS_STUECKLISTE_BEZEICHNUNG] = oCurrentDto
						.getSLosstuecklistenartikelbezeichnung();
				data[row][AuftragReportFac.REPORT_STATISTIK_LOS_STUECKLISTE_ZUSATZBEZEICHNUNG] = oCurrentDto
						.getSLosstuecklistenartikelzusatzbezeichnung();

				PartnerDto partnerDto_belegadresse = getKundeFac()
						.kundeFindByPrimaryKey(
								oCurrentDto.getAuftragDto()
										.getKundeIIdAuftragsadresse(),
								theClientDto).getPartnerDto();

				data[row][AuftragReportFac.REPORT_STATISTIK_BELEGKUNDE_STRASSE] = partnerDto_belegadresse
						.getCStrasse();
				data[row][AuftragReportFac.REPORT_STATISTIK_BELEGKUNDE_NAME1] = partnerDto_belegadresse
						.getCName1nachnamefirmazeile1();
				data[row][AuftragReportFac.REPORT_STATISTIK_BELEGKUNDE_NAME2] = partnerDto_belegadresse
						.getCName2vornamefirmazeile2();
				data[row][AuftragReportFac.REPORT_STATISTIK_BELEGKUNDE_NAME3] = partnerDto_belegadresse
						.getCName3vorname2abteilung();

				if (partnerDto_belegadresse.getLandplzortDto() != null) {
					data[row][AuftragReportFac.REPORT_STATISTIK_BELEGKUNDE_LKZ] = partnerDto_belegadresse
							.getLandplzortDto().getLandDto().getCLkz();
					data[row][AuftragReportFac.REPORT_STATISTIK_BELEGKUNDE_PLZ] = partnerDto_belegadresse
							.getLandplzortDto().getCPlz();
					data[row][AuftragReportFac.REPORT_STATISTIK_BELEGKUNDE_ORT] = partnerDto_belegadresse
							.getLandplzortDto().getOrtDto().getCName();
				}

				PartnerDto partnerDto_rechnungsadresse = getKundeFac()
						.kundeFindByPrimaryKey(
								oCurrentDto.getAuftragDto()
										.getKundeIIdRechnungsadresse(),
								theClientDto).getPartnerDto();

				data[row][AuftragReportFac.REPORT_STATISTIK_BELEGRECHNUNGSADRESSE_STRASSE] = partnerDto_rechnungsadresse
						.getCStrasse();
				data[row][AuftragReportFac.REPORT_STATISTIK_BELEGRECHNUNGSADRESSE_NAME1] = partnerDto_rechnungsadresse
						.getCName1nachnamefirmazeile1();
				data[row][AuftragReportFac.REPORT_STATISTIK_BELEGRECHNUNGSADRESSE_NAME2] = partnerDto_rechnungsadresse
						.getCName2vornamefirmazeile2();
				data[row][AuftragReportFac.REPORT_STATISTIK_BELEGRECHNUNGSADRESSE_NAME3] = partnerDto_rechnungsadresse
						.getCName3vorname2abteilung();

				if (partnerDto_rechnungsadresse.getLandplzortDto() != null) {
					if (partnerDto_rechnungsadresse.getLandplzortDto()
							.getLandDto() != null) {
						data[row][AuftragReportFac.REPORT_STATISTIK_BELEGRECHNUNGSADRESSE_LKZ] = partnerDto_rechnungsadresse
								.getLandplzortDto().getLandDto().getCLkz();
					}
					data[row][AuftragReportFac.REPORT_STATISTIK_BELEGRECHNUNGSADRESSE_PLZ] = partnerDto_rechnungsadresse
							.getLandplzortDto().getCPlz();
					if (partnerDto_rechnungsadresse.getLandplzortDto()
							.getOrtDto() != null) {
						data[row][AuftragReportFac.REPORT_STATISTIK_BELEGRECHNUNGSADRESSE_ORT] = partnerDto_rechnungsadresse
								.getLandplzortDto().getOrtDto().getCName();
					}
				}

				data[row][AuftragReportFac.REPORT_STATISTIK_VERKAUFSWERTARBEITSOLL] = oCurrentDto
						.getBdVkwArbeitsoll();
				data[row][AuftragReportFac.REPORT_STATISTIK_VERKAUFSWERTMATERIALSOLL] = oCurrentDto
						.getBdVkwMaterialsoll();
				data[row][AuftragReportFac.REPORT_STATISTIK_VERKAUFSWERTARBEITIST] = oCurrentDto
						.getBdVkwArbeitist();
				data[row][AuftragReportFac.REPORT_STATISTIK_VERKAUFSWERTMATERIALIST] = oCurrentDto
						.getBdVkwMaterialist();
				data[row][AuftragReportFac.REPORT_STATISTIK_GESTEHUNGSWERTARBEITSOLL] = oCurrentDto
						.getBdGestehungswertarbeitsoll();
				data[row][AuftragReportFac.REPORT_STATISTIK_GESTEHUNGSWERTMATERIALSOLL] = oCurrentDto
						.getBdGestehungswertmaterialsoll();
				data[row][AuftragReportFac.REPORT_STATISTIK_GESTEHUNGSWERTARBEITIST] = oCurrentDto
						.getBdGestehungswertarbeitist();
				data[row][AuftragReportFac.REPORT_STATISTIK_GESTEHUNGSWERTMATERIALIST] = oCurrentDto
						.getBdGestehungswertmaterialist();
				data[row][AuftragReportFac.REPORT_STATISTIK_ARBEITSZEITSOLL] = oCurrentDto
						.getDdArbeitszeitsoll();
				data[row][AuftragReportFac.REPORT_STATISTIK_ARBEITSZEITIST] = oCurrentDto
						.getDdArbeitszeitist();
				data[row][AuftragReportFac.REPORT_STATISTIK_ARTIKELNUMMERARBEITSZEIT] = oCurrentDto
						.getCArtikelnummerArbeitszeit();
				data[row][AuftragReportFac.REPORT_STATISTIK_ARTIKELBEZEICHNUNGARBEITSZEIT] = oCurrentDto
						.getCArtikelbezeichnungArbeitszeit();
				data[row][AuftragReportFac.REPORT_STATISTIK_ARTIKELNUMMERARBEITSZEIT] = oCurrentDto
						.getCArtikelnummerArbeitszeit();
				data[row][AuftragReportFac.REPORT_STATISTIK_ARTIKELBEZEICHNUNGARBEITSZEIT] = oCurrentDto
						.getCArtikelbezeichnungArbeitszeit();
				data[row][AuftragReportFac.REPORT_STATISTIK_MASCHINENZEITSOLL] = oCurrentDto
						.getDdMaschinenzeitsoll();
				data[row][AuftragReportFac.REPORT_STATISTIK_MASCHINENZEITIST] = oCurrentDto
						.getDdMaschinenzeitist();
				data[row][AuftragReportFac.REPORT_STATISTIK_EINGANGSRECHNUNGTEXT] = oCurrentDto
						.getSEingangsrechnungtext();
				data[row][AuftragReportFac.REPORT_STATISTIK_RECHNUNGSNUMMER_LS_VERRECHNET] = oCurrentDto
						.getSRechnungsnummerLSVerrechnet();

				data[row][AuftragReportFac.REPORT_STATISTIK_REISE_KOMMENTAR] = oCurrentDto
						.getSReiseKommentar();
				data[row][AuftragReportFac.REPORT_STATISTIK_REISE_PARTNER] = oCurrentDto
						.getSReisePartner();
				data[row][AuftragReportFac.REPORT_STATISTIK_REISE_MITARBEITER] = oCurrentDto
						.getSReiseMitarbeiter();
				data[row][AuftragReportFac.REPORT_STATISTIK_REISE_VON] = oCurrentDto
						.gettReiseVon();
				data[row][AuftragReportFac.REPORT_STATISTIK_REISE_BIS] = oCurrentDto
						.gettReiseBis();
				data[row][AuftragReportFac.REPORT_STATISTIK_ER_AUFTRAGSZUORDNUNG_KEINE_AUFTRAGSWERTUNG] = oCurrentDto
						.isBKeineAuftragswertung();
				data[row][AuftragReportFac.REPORT_STATISTIK_ER_EINGANGSRECHNUNGSART] = oCurrentDto
						.getSEingangsgrechnungsart();
				data[row][AuftragReportFac.REPORT_STATISTIK_ER_SCHLUSSRECHNUNG_NR] = oCurrentDto
						.getSErSchlussrechnungNr();
				data[row][AuftragReportFac.REPORT_STATISTIK_LOSNUMMER_LI_QUELLE] = oCurrentDto
						.getLosnummerLiQuelle();
				// data[row][AuftragReportFac.REPORT_STATISTIK_DIAETENAUSSCRIPT]
				// = oCurrentDto
				// .getBdGestehungswertmaterialist();

				row++;
			}
		}

		if (iOptionSortierung == AuftragReportFac.REPORT_AUFTRAGSTATISTIK_SORTIERUNG_FUEHRENDER_ARTIKEL) {

			for (int i = data.length - 1; i > 0; --i) {
				for (int j = 0; j < i; ++j) {
					Object[] o = data[j];
					Object[] o1 = data[j + 1];

					String s1 = Helper
							.fitString2Length(
									(String) o[AuftragReportFac.REPORT_STATISTIK_FUEHRENDER_ARTIKEL],
									80, ' ')
							+ o[AuftragReportFac.REPORT_STATISTIK_AUFTRAGSNUMMER];
					String s2 = Helper
							.fitString2Length(
									(String) o1[AuftragReportFac.REPORT_STATISTIK_FUEHRENDER_ARTIKEL],
									80, ' ')
							+ o1[AuftragReportFac.REPORT_STATISTIK_AUFTRAGSNUMMER];

					if (s1.compareTo(s2) > 0) {
						data[j] = o1;
						data[j + 1] = o;
					}
				}
			}

			sortierung = getTextRespectUISpr(
					"auftragsstatistik.sortierung.fuehrenderartikel",
					theClientDto.getMandant(), theClientDto.getLocUi());
		}

		parameter.put("P_VON", tVon);

		parameter.put("P_STICHTAG", tStichtag);

		if (tBis != null) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(tBis.getTime());
			c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);

			parameter.put("P_BIS", new java.sql.Timestamp(c.getTimeInMillis()));

		}
		parameter.put("P_SORTIERUNG", sortierung);

		// Mandantparameter holen

		ParametermandantDto parameterDto = (ParametermandantDto) getParameterFac()
				.getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.MATERIALGEMEINKOSTENFAKTOR, tStichtag);

		Double dMaterialgemeinkostenfaktor = ((Double) parameterDto
				.getCWertAsObject()).doubleValue();
		parameter.put("P_MATERIALGEMEINKOSTENPROZENT",
				dMaterialgemeinkostenfaktor);

		parameterDto = (ParametermandantDto) getParameterFac()
				.getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.FERTIGUNGSGEMEINKOSTENFAKTOR, tStichtag);

		Double dFertigungsgemeinkostenfaktor = ((Double) parameterDto
				.getCWertAsObject()).doubleValue();
		parameter.put("P_FERTIGUNGSGEMEINKOSTENPROZENT",
				dFertigungsgemeinkostenfaktor);

		parameterDto = (ParametermandantDto) getParameterFac()
				.getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.ENTWICKLUNGSGEMEINKOSTENFAKTOR, tStichtag);
		Double dEntwicklungsgemeinkostenfaktor = ((Double) parameterDto
				.getCWertAsObject()).doubleValue();
		parameter.put("P_ENTWICKLUNGSGEMEINKOSTENPROZENT",
				dEntwicklungsgemeinkostenfaktor);

		parameterDto = (ParametermandantDto) getParameterFac()
				.getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.VERWALTUNGSGEMEINKOSTENFAKTOR, tStichtag);

		Double dVerwaltungssgemeinkostenfaktor = ((Double) parameterDto
				.getCWertAsObject()).doubleValue();
		parameter.put("P_VERWALTUNGSGEMEINKOSTENPROZENT",
				dVerwaltungssgemeinkostenfaktor);

		parameterDto = (ParametermandantDto) getParameterFac()
				.getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.VERTRIEBSGEMEINKOSTENFAKTOR, tStichtag);

		Double dVertriebsgemeinkostenfaktor = ((Double) parameterDto
				.getCWertAsObject()).doubleValue();
		parameter.put("P_VERTRIEBSGEMEINKOSTENPROZENT",
				dVertriebsgemeinkostenfaktor);

		initJRDS(parameter, AuftragReportFac.REPORT_MODUL,
				AuftragReportFac.REPORT_AUFTRAGSTATISTIK,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	private void checkAuftragIId(Integer iIdAuftragI) throws EJBExceptionLP {
		if (iIdAuftragI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAuftragI == null"));
		}

		myLogger.info("AuftragIId: " + iIdAuftragI);
	}

	/**
	 * Kennung fuer eine Auftragbestaetigung zum Andrucken zusammenbauen.
	 * 
	 * @param auftragDtoI
	 *            der Auftrag
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 * @return String die Kennung
	 */
	private String baueKennungAuftragbestaetigung(AuftragDto auftragDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		StringBuffer abKennung = null;
		try {
			BelegartDto belegartDto = getLocaleFac().belegartFindByPrimaryKey(
					auftragDtoI.getBelegartCNr(), theClientDto);

			abKennung = new StringBuffer(belegartDto.getCKurzbezeichnung());
			abKennung.append(" ").append(auftragDtoI.getCNr());
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return abKennung.toString();
	}

	/**
	 * Anrede fuer den provisionsberechtigten Vertreter des Auftrags holen.
	 * 
	 * @param iIdVertreterI
	 *            pk des Vertreters
	 * @param locDruck
	 *            Locale
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return String die Anrede des Vertreters, kann NULL sein
	 * @throws EJBExceptionLP
	 */
	private String baueAnredeVertreter(Integer iIdVertreterI, Locale locDruck,
			TheClientDto theClientDto) throws EJBExceptionLP {
		String sAnredeVertreter = null;
		try {
			if (iIdVertreterI != null) {
				PersonalDto vertreterDto = getPersonalFac()
						.personalFindByPrimaryKey(iIdVertreterI, theClientDto);

				sAnredeVertreter = getPartnerFac()
						.formatFixAnredeTitelName2Name1(
								vertreterDto.getPartnerDto(), locDruck,
								theClientDto);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return sAnredeVertreter;
	}

	private double getWechselkursRechnungswaehrungZuMandantwaehrung(
			Integer iIdRechnungI) throws EJBExceptionLP {
		RechnungDto rechnungDto = null;

		try {
			rechnungDto = getRechnungFac().rechnungFindByPrimaryKey(
					iIdRechnungI);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		double dWechselkursReziprok = rechnungDto.getNKurs().doubleValue();

		if (dWechselkursReziprok != 0) {
			dWechselkursReziprok = 1 / dWechselkursReziprok;
		}

		return dWechselkursReziprok;
	}

	/**
	 * Diese Methode liefert eine Liste von allen offenen Auftraegen eines
	 * Mandanten, die nach den eingegebenen Kriterien des Benutzers
	 * zusammengestellt wird. <br>
	 * Achtung: Hibernate verwendet lazy initialization, d.h. der Zugriff auf
	 * Collections muss innerhalb der Session erfolgen.
	 * 
	 * @param krit
	 *            die Kriterien des Benutzers
	 * @param dStichtag
	 *            Date
	 * @param bSortierungNachLiefertermin
	 *            Boolean
	 * @param artikelklasseIId
	 *            Integer
	 * @param artikelgruppeIId
	 *            Integer
	 * @param artikelCNrVon
	 *            String
	 * @param artikelCNrBis
	 *            String
	 * @param projektCBezeichnung
	 *            String
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return ReportAuftragOffeneDto[] die Liste der Auftraege
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private ReportAuftragOffeneDetailsDto[] getListeReportAuftragOffeneDetails(
			ReportJournalKriterienDto krit, Date dStichtag,
			Boolean bSortierungNachLiefertermin, Integer artikelklasseIId,
			Integer artikelgruppeIId, String artikelCNrVon,
			String artikelCNrBis, String projektCBezeichnung,
			TheClientDto theClientDto) throws EJBExceptionLP {

		ReportAuftragOffeneDetailsDto[] aResult = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		// vom Stichtag die Uhrzeit abschneiden
		dStichtag = Helper.cutDate(dStichtag);

		try {
			session = factory.openSession();

			// Hiberante Criteria fuer alle Tabellen ausgehend von meiner
			// Haupttabelle anlegen,
			// nach denen ich filtern und sortieren kann
			Criteria crit = session
					.createCriteria(FLRAuftragpositionReport.class);

			crit.add(Restrictions.eq(
					AuftragpositionFac.FLR_AUFTRAGPOSITIONART_C_NR,
					AuftragServiceFac.AUFTRAGPOSITIONART_IDENT));
			Criteria critAuftrag = crit
					.createCriteria(AuftragpositionFac.FLR_AUFTRAGPOSITION_FLRAUFTRAG);
			// Einschraenkung auf den aktuellen Mandanten
			critAuftrag.add(Restrictions.eq(
					AuftragFac.FLR_AUFTRAG_MANDANT_C_NR,
					theClientDto.getMandant()));

			// Einschraenkung nach Status Offen, Teilerledigt, Erledigt
			Collection<String> cStati = new LinkedList<String>();
			cStati.add(AuftragServiceFac.AUFTRAGSTATUS_OFFEN);
			// cStati.add(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT);
			cStati.add(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT);
			critAuftrag.add(Restrictions.in(
					AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR, cStati));

			// Das Belegdatum muss vor dem Stichtag liegen
			critAuftrag.add(Restrictions.le(
					AuftragFac.FLR_AUFTRAG_D_BELEGDATUM, dStichtag));

			// Der Auftrag darf zum Stichtag noch nicht erledigt worden sein
			critAuftrag.add(Restrictions.or(Restrictions
					.isNull(AuftragFac.FLR_AUFTRAG_T_ERLEDIGT), Restrictions
					.gt(AuftragFac.FLR_AUFTRAG_T_ERLEDIGT, dStichtag)));

			// Einschraenkung nach einer bestimmten Kostenstelle
			if (krit.kostenstelleIId != null) {
				critAuftrag.add(Restrictions.eq(
						AuftragFac.FLR_AUFTRAG_KOSTENSTELLE_I_ID,
						krit.kostenstelleIId));
			}

			// Einschraenkung nach einem bestimmten Kunden
			if (krit.kundeIId != null) {
				critAuftrag.add(Restrictions.eq(
						AuftragFac.FLR_AUFTRAG_KUNDE_I_ID_AUFTRAGSADRESSE,
						krit.kundeIId));
			}
			// Einschraenkung nach einem bestimmten Vertreter
			if (krit.vertreterIId != null) {
				critAuftrag.add(Restrictions.eq(
						AuftragFac.FLR_AUFTRAG_VERTRETER_I_ID,
						krit.vertreterIId));
			}
			// Filter nach Projektbezeichnung
			if (projektCBezeichnung != null) {
				critAuftrag.add(Restrictions.like(
						AuftragFac.FLR_AUFTRAG_C_BEZ_PROJEKTBEZEICHNUNG, "%"
								+ projektCBezeichnung + "%"));
			}

			// Einschraenkung nach Belegdatum von - bis
			String sVon = null;
			String sBis = null;

			if (krit.dVon != null) {
				critAuftrag.add(Restrictions.ge(
						AuftragFac.FLR_AUFTRAG_D_BELEGDATUM, krit.dVon));
				sVon = Helper.formatDatum(krit.dVon, theClientDto.getLocUi());
			}

			if (krit.dBis != null) {
				critAuftrag.add(Restrictions.le(
						AuftragFac.FLR_AUFTRAG_D_BELEGDATUM, krit.dBis));
				sBis = Helper.formatDatum(krit.dBis, theClientDto.getLocUi());
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

			if (krit.sBelegnummerVon != null) {
				sVon = HelperServer.getBelegnummernFilterForHibernateCriterias(
						f, iGeschaeftsjahr, sMandantKuerzel,
						krit.sBelegnummerVon);
				critAuftrag.add(Restrictions.ge(AuftragFac.FLR_AUFTRAG_C_NR,
						sVon));
			}
			if (krit.sBelegnummerBis != null) {
				sBis = HelperServer.getBelegnummernFilterForHibernateCriterias(
						f, iGeschaeftsjahr, sMandantKuerzel,
						krit.sBelegnummerBis);
				critAuftrag.add(Restrictions.le(AuftragFac.FLR_AUFTRAG_C_NR,
						sBis));
			}
			// Sortierung nach Kostenstelle ist immer die erste Sortierung
			if (krit.bSortiereNachKostenstelle) {
				critAuftrag.createCriteria(
						AuftragFac.FLR_AUFTRAG_FLRKOSTENSTELLE).addOrder(
						Order.asc(AuftragFac.FLR_AUFTRAG_C_NR));
			}
			// Sortierung nach Kunde, eventuell innerhalb der Kostenstelle
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				critAuftrag
						.createCriteria(AuftragFac.FLR_AUFTRAG_FLRKUNDE)
						.createCriteria(KundeFac.FLR_PARTNER)
						.addOrder(
								Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
			} else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_BELEGNUMMER) {
				// es wird in jedem Fall nach der Belegnummer sortiert
				critAuftrag.addOrder(Order.asc(AuftragFac.FLR_AUFTRAG_C_NR));
			}
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_ART) {
				critAuftrag.addOrder(Order
						.asc(AuftragFac.FLR_AUFTRAG_AUFTRAGART_C_NR));
			}
			// Sortierung nach Liefertermin (optional)
			if (bSortierungNachLiefertermin != null
					&& bSortierungNachLiefertermin.booleanValue()) {
				crit.addOrder(Order
						.asc(AuftragpositionFac.FLR_AUFTRAGPOSITION_T_UEBERSTEUERTERLIEFERTERMIN));
			}
			Criteria critArtikel = crit
					.createCriteria(AuftragpositionFac.FLR_AUFTRAGPOSITION_FLRARTIKEL);
			// Sortierung nach Ident
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_IDENT) {
				critArtikel.addOrder(Order.asc(AuftragFac.FLR_AUFTRAG_C_NR));
			}
			if (artikelklasseIId != null || artikelgruppeIId != null
					|| artikelCNrVon != null || artikelCNrBis != null) {
				// Wenn nach Artikelklassen/Gruppen gefiltert wird, dann kommen
				// nur Ident-Positionen
				if (artikelklasseIId != null) {
					critArtikel.createCriteria(
							ArtikelFac.FLR_ARTIKEL_FLRARTIKELKLASSE).add(
							Restrictions.eq("i_id", artikelklasseIId));
				}
				if (artikelgruppeIId != null) {
					critArtikel.createCriteria(
							ArtikelFac.FLR_ARTIKEL_FLRARTIKELGRUPPE).add(
							Restrictions.eq("i_id", artikelgruppeIId));
				}
				if (artikelCNrVon != null) {
					critArtikel.add(Restrictions.ge(
							ArtikelFac.FLR_ARTIKEL_C_NR, artikelCNrVon));
				}
				if (artikelCNrBis != null) {
					critArtikel.add(Restrictions.le(
							ArtikelFac.FLR_ARTIKEL_C_NR, artikelCNrBis));
				}
			}
			List<?> list = crit.list();

			aResult = new ReportAuftragOffeneDetailsDto[list.size()];
			int iIndex = 0;
			Iterator<?> it = list.iterator();
			ReportAuftragOffeneDetailsDto reportDto = null;
			while (it.hasNext()) {
				FLRAuftragpositionReport flrAuftragposition = (FLRAuftragpositionReport) it
						.next();
				FLRAuftragReport flrauftrag = flrAuftragposition
						.getFlrauftrag();
				reportDto = new ReportAuftragOffeneDetailsDto();
				reportDto.setIIdAuftragposition(flrAuftragposition.getI_id());
				reportDto.setIIdAuftrag(flrauftrag.getI_id());
				reportDto.setCNrAuftrag(flrauftrag.getC_nr());
				reportDto.setKundeCName1(flrauftrag.getFlrkunde()
						.getFlrpartner().getC_name1nachnamefirmazeile1());
				reportDto.setKostenstelleCNr(flrauftrag.getFlrkostenstelle()
						.getC_nr());
				aResult[iIndex] = reportDto;
				iIndex++;
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
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
	 * Alle offenen Auftraege fuer einen bestimmten Mandanten drucken.
	 * 
	 * @param krit
	 *            die Filter- und Sortierkriterien
	 * @param dStichtag
	 *            Date
	 * @param bSortierungNachLiefertermin
	 *            Boolean
	 * @param bInternenKommentarDrucken
	 *            Boolean
	 * @param artikelklasseIId
	 *            Integer
	 * @param artikelgruppeIId
	 *            Integer
	 * @param artikelCNrVon
	 *            String
	 * @param artikelCNrBis
	 *            String
	 * @param projektCBezeichnung
	 *            String
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @return JasperPrint der Druck
	 */
	/*
	 * public JasperPrintLP printAuftragOffeneDetails(ReportJournalKriterienDto
	 * krit, Date dStichtag, Boolean bSortierungNachLiefertermin, Boolean
	 * bInternenKommentarDrucken, Integer artikelklasseIId, Integer
	 * artikelgruppeIId, String artikelCNrVon, String artikelCNrBis, String
	 * projektCBezeichnung, TheClientDto theClientDto) throws EJBExceptionLP {
	 * 
	 * 
	 * 
	 * if (krit == null) { throw new
	 * EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new
	 * Exception("reportJournalKriterienDtoI == null")); }
	 * 
	 * if (dStichtag == null) { throw new
	 * EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new
	 * Exception("dStichtag == null")); }
	 * 
	 * JasperPrintLP oPrintO = null;
	 * 
	 * index = -1; cAktuellerReport =
	 * AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS;
	 * 
	 * try { // die Liste aller offenen Auftraege entsprechend den Kriterien
	 * zusammenstellen ReportAuftragOffeneDetailsDto[] aReportAuftragOffeneDto =
	 * getListeReportAuftragOffeneDetails( krit, dStichtag,
	 * bSortierungNachLiefertermin, artikelklasseIId, artikelgruppeIId,
	 * artikelCNrVon, artikelCNrBis, projektCBezeichnung, theClientDto);
	 * 
	 * // wieviele Zeilen wird der Report haben? int iAnzahlZeilen = 0;
	 * 
	 * data = new Object[aReportAuftragOffeneDto.length][AuftragReportFac.
	 * REPORT_AUFTRAG_OFFENE_DETAILS_ANZAHL_SPALTEN];
	 * 
	 * AuftragDto oAuftragDto = null; // der aktuelle Auftrag KostenstelleDto
	 * kostenstelleDto = null; KundeDto kundeAuftragDto = null; KundeDto
	 * kundeLieferAdresseDto = null; AuftragpositionDto aPositionDto = null; //
	 * die Positionen zu dem aktuellen Auftrag
	 * 
	 * // die Datenmatrix pro Auftrag befuellen; alle Felder, die nicht explizit
	 * besetzt werden, sind null for (int i = 0; i <
	 * aReportAuftragOffeneDto.length; i++) {
	 * 
	 * oAuftragDto =
	 * getAuftragFac().auftragFindByPrimaryKey(aReportAuftragOffeneDto[
	 * i].getIIdAuftrag()); aPositionDto =
	 * getAuftragpositionFac().auftragpositionFindByPrimaryKey(
	 * aReportAuftragOffeneDto[ i].getIIdAuftragposition()); String sLosNummer =
	 * ""; StringBuffer buffLos = new StringBuffer(""); SessionFactory factory =
	 * FLRSessionFactory.getFactory(); Session session = null;
	 * 
	 * //nur nicht erledigte Positionen ber&uuml;cksichtigen if
	 * (aPositionDto.getNOffeneMenge().doubleValue() != 0) { kostenstelleDto =
	 * getSystemFac().kostenstelleFindByPrimaryKey(oAuftragDto.getKostIId());
	 * kundeAuftragDto =
	 * getKundeFac().kundeFindByPrimaryKey(oAuftragDto.getKundeIIdAuftragsadresse
	 * (), theClientDto); kundeLieferAdresseDto =
	 * getKundeFac().kundeFindByPrimaryKey
	 * (oAuftragDto.getKundeIIdLieferadresse(), theClientDto);
	 * 
	 * try { session = factory.openSession(); // Hiberante Criteria fuer alle
	 * Tabellen ausgehend von meiner Haupttabelle anlegen, // nach denen ich
	 * filtern und sortieren kann Criteria crit =
	 * session.createCriteria(FLRLosReport.class);
	 * crit.add(Restrictions.eq("flrauftrag.i_id", oAuftragDto.getIId()));
	 * List<?> resultList = crit.list(); Iterator it = resultList.iterator();
	 * while (it.hasNext()) { FLRLosReport flrlos = (FLRLosReport) it.next();
	 * buffLos.append(flrlos.getC_nr()); buffLos.append(" "); } } finally { try
	 * { session.close(); } catch (HibernateException he) { throw new
	 * EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE, he); } }
	 * 
	 * data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGCNR] =
	 * oAuftragDto. getCNr();
	 * data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGARTCNR] =
	 * oAuftragDto.getAuftragartCNr();
	 * data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGKUNDE] =
	 * kundeAuftragDto. getPartnerDto().getCName1nachnamefirmazeile1(); if
	 * (kundeAuftragDto.getPartnerDto().getCName2vornamefirmazeile2() != null) {
	 * data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGKUNDE2] =
	 * kundeAuftragDto.getPartnerDto().getCName2vornamefirmazeile2(); }
	 * data[i][AuftragReportFac
	 * .REPORT_AUFTRAG_OFFENE_DETAILS_KUNDEAUFTRAGADRESSE] =
	 * kundeAuftragDto.getPartnerDto().getCKbez() + " " +
	 * kundeAuftragDto.getPartnerDto().formatAdresse();
	 * data[i][AuftragReportFac.
	 * REPORT_AUFTRAG_OFFENE_DETAILS_KUNDELIEFERADRESSE] =
	 * kundeLieferAdresseDto.getPartnerDto().getCKbez() + " " +
	 * kundeLieferAdresseDto.getPartnerDto().formatAdresse();
	 * data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_LOSNUMMER] =
	 * buffLos. toString();
	 * data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_KOSTENSTELLECNR] =
	 * kostenstelleDto.getCNr();
	 * data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGLIEFERTERMIN
	 * ] =
	 * Helper.formatDatum(Helper.extractDate(oAuftragDto.getDLiefertermin()),
	 * theClientDto.getLocUi());
	 * data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGFINALTERMIN
	 * ] = Helper.formatDatum(Helper.extractDate(oAuftragDto.getDFinaltermin()),
	 * theClientDto.getLocUi());
	 * 
	 * // Zahlungsziel zum Andrucken String sZahlungsziel = null;
	 * 
	 * if (oAuftragDto.getZahlungszielIId() != null) { if (sZahlungsziel ==
	 * null) { ZahlungszielDto oDto =
	 * getMandantFac().zahlungszielFindByPrimaryKey(
	 * oAuftragDto.getZahlungszielIId(), theClientDto);
	 * 
	 * sZahlungsziel = oDto.getCBez(); } } if
	 * (bInternenKommentarDrucken.booleanValue()) {
	 * data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_INTERNERKOMMENTAR]
	 * = oAuftragDto.getXInternerkommentar(); } else {
	 * data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_INTERNERKOMMENTAR]
	 * = ""; }
	 * data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGZAHLUNGSZIEL
	 * ] = sZahlungsziel; data[i][AuftragReportFac.
	 * REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGPROJEKTBEZEICHNUNG] = oAuftragDto.
	 * getCBezProjektbezeichnung(); data[i][AuftragReportFac.
	 * REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGBESTELLNUMMER] =
	 * oAuftragDto.getCBestellnummer(); // nur mengenbehaftete Positionen
	 * beruecksichtigen if (aPositionDto.getNMenge() != null) { ArtikelDto
	 * oArtikelDto = getArtikelFac().artikelFindByPrimaryKey(
	 * aPositionDto.getArtikelIId(), theClientDto); String artikelCNr = null;
	 * 
	 * // @todo boeser Workaround ... PJ 3779
	 * 
	 * if (oArtikelDto.getCNr().startsWith("~")) { artikelCNr =
	 * AngebotReportFac. REPORT_VORKALKULATION_ZEICHEN_FUER_HANDEINGABE; } else
	 * { artikelCNr = oArtikelDto.getCNr(); }
	 * data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELCNR] =
	 * artikelCNr; data[i][AuftragReportFac.
	 * REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGPROJEKTBEZEICHNUNG] = oAuftragDto.
	 * getCBezProjektbezeichnung();
	 * data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGCNR] =
	 * oAuftragDto.getCNr();
	 * data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGARTCNR] =
	 * oAuftragDto.getAuftragartCNr(); String cArtikelBezeichnung = ""; if
	 * (aPositionDto.getAuftragpositionartCNr().equals(AuftragServiceFac.
	 * AUFTRAGPOSITIONART_IDENT) ||
	 * aPositionDto.getAuftragpositionartCNr().equals(AuftragServiceFac.
	 * AUFTRAGPOSITIONART_HANDEINGABE)) { cArtikelBezeichnung = getArtikelFac().
	 * baueArtikelBezeichnungMehrzeiligOhneExc( oArtikelDto.getIId(),
	 * aPositionDto.getAuftragpositionartCNr(), aPositionDto.getCBez(),
	 * aPositionDto.getCZusatzbez(), false, null, theClientDto); // Lagerstand
	 * BigDecimal bdLagerstand = getLagerFac().
	 * getLagerstandAllerLagerEinesMandanten( oArtikelDto.getIId(),
	 * theClientDto);
	 * data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELLAGERSTAND]
	 * = bdLagerstand; }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELBEZEICHNUNG]
	 * = cArtikelBezeichnung;
	 * data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELMENGE] =
	 * aPositionDto. getNMenge(); if (aPositionDto.
	 * getTUebersteuerbarerLiefertermin() != null) {
	 * data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_POSITIONSTERMIN] =
	 * Helper.formatDatum(Helper.extractDate(aPositionDto.
	 * getTUebersteuerbarerLiefertermin()), theClientDto.getLocUi()); }
	 * data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELEINHEIT] =
	 * aPositionDto. getEinheitCNr() == null ? null :
	 * aPositionDto.getEinheitCNr().trim();
	 * 
	 * // Positionspreise sind in Belegwaehrung abgelegt BigDecimal
	 * nPreisInBelegwaehrung = aPositionDto.
	 * getNNettoeinzelpreisplusversteckteraufschlagminusrabatte();
	 * 
	 * nPreisInBelegwaehrung = getBetragMalWechselkurs( nPreisInBelegwaehrung,
	 * Helper.getKehrwert(new BigDecimal(
	 * oAuftragDto.getDWechselkursMandantWaehrungZuAuftragswaehrung().
	 * doubleValue())));
	 * 
	 * data[i][ AuftragReportFac.
	 * REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELNETTOGESAMTPREISPLUSVERSTECKTERAUFSCHLAGMINUSRABATTE
	 * ] = nPreisInBelegwaehrung;
	 * 
	 * // Grundlage ist der Gestehungspreis des Artikels am Hauptlager des
	 * Mandanten BigDecimal bdGestehungspreis = getLagerFac().
	 * getGemittelterGestehungspreisDesHauptlagers(aPositionDto.getArtikelIId(),
	 * theClientDto); BigDecimal bdGelieferteMenge = aPositionDto.getNMenge();
	 * data[i
	 * ][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELGESTEHUNGSPREIS]
	 * = bdGestehungspreis; if
	 * (oAuftragDto.getAuftragartCNr().equals(AuftragServiceFac
	 * .AUFTRAGART_RAHMEN)) { try { session = factory.openSession(); Criteria
	 * cAuftragposition = session.createCriteria(FLRAuftragposition.class);
	 * cAuftragposition.add(Restrictions.eq(
	 * "auftragposition_i_id_rahmenposition", aPositionDto.getIId())); List<?>
	 * listAuftragposition = cAuftragposition.list(); Iterator itAuftragposition
	 * = listAuftragposition.iterator(); while (itAuftragposition.hasNext()) {
	 * FLRAuftragposition auftragposition = (FLRAuftragposition)
	 * itAuftragposition.next(); Criteria cLieferscheinposition =
	 * session.createCriteria( FLRLieferscheinposition.class);
	 * cLieferscheinposition.add(Restrictions.eq(LieferscheinpositionFac.
	 * FLR_LIEFERSCHEINPOSITION_AUFTRAGPOSITION_I_ID,
	 * auftragposition.getI_id())); List<?> listLieferscheinposition =
	 * cLieferscheinposition.list(); Iterator itLieferscheinposition =
	 * listLieferscheinposition.iterator(); while
	 * (itLieferscheinposition.hasNext()) { FLRLieferscheinposition
	 * lieferscheinposition = ( FLRLieferscheinposition)
	 * itLieferscheinposition.next(); FLRLieferschein lieferschein =
	 * lieferscheinposition. getFlrlieferschein(); bdGelieferteMenge =
	 * bdGelieferteMenge.subtract(lieferscheinposition. getN_menge()); } } }
	 * finally { try { session.close(); } catch (HibernateException he) { throw
	 * new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE, he); } }
	 * 
	 * } else { bdGelieferteMenge =
	 * aPositionDto.getNMenge().subtract(aPositionDto. getNOffeneMenge()); }
	 * data[i][AuftragReportFac.
	 * REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELGELIEFERTEMENGE] =
	 * bdGelieferteMenge;
	 * data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELOFFENEMENGE
	 * ] = aPositionDto. getNOffeneMenge();
	 * data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELOFFENERWERT
	 * ] = aPositionDto.getNOffeneMenge().multiply(nPreisInBelegwaehrung);
	 * 
	 * data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELOFFENERDB]
	 * = aPositionDto.getNOffeneMenge().multiply(nPreisInBelegwaehrung.subtract(
	 * bdGestehungspreis));
	 * 
	 * // die Positionen brauchen alle Attribute, nach denen im Report gruppiert
	 * wird data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGCNR] =
	 * oAuftragDto.getCNr();
	 * data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGARTCNR] =
	 * oAuftragDto.getAuftragartCNr();
	 * data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_KOSTENSTELLECNR] =
	 * kostenstelleDto.getCNr();
	 * data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGKUNDE] =
	 * kundeAuftragDto.getPartnerDto().getCName1nachnamefirmazeile1();
	 * 
	 * } } }
	 * 
	 * // die Parameter dem Report uebergeben HashMap parameter = new HashMap();
	 * 
	 * parameter.put(LPReport.P_SORTIERUNG,
	 * buildSortierungAuftragOffeneDetails(krit, theClientDto));
	 * parameter.put(LPReport.P_FILTER, buildFilterAuftragOffeneDetails(krit,
	 * artikelklasseIId, artikelgruppeIId, artikelCNrVon, artikelCNrBis,
	 * projektCBezeichnung, theClientDto));
	 * 
	 * parameter.put(LPReport.P_SORTIERENACHKUNDE, new Boolean(krit.iSortierung
	 * == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER));
	 * parameter.put(LPReport.P_SORTIERENACHKOSTENSTELLE, new
	 * Boolean(krit.bSortiereNachKostenstelle));
	 * parameter.put("P_SORTIERENACHAUFTRAGART", new Boolean(krit.iSortierung ==
	 * ReportJournalKriterienDto.KRIT_SORT_NACH_ART));
	 * parameter.put("P_AUFTRAGWAEHRUNG", theClientDto.getSMandantenwaehrung());
	 * parameter.put("P_STICHTAG", Helper.formatDatum(dStichtag,
	 * theClientDto.getLocUi()));
	 * 
	 * initJRDS(parameter, AuftragReportFac.REPORT_MODUL,
	 * AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS,
	 * theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
	 * 
	 * oPrintO = getReportPrint(); } catch (RemoteException re) {
	 * throwEJBExceptionLPRespectOld(re); } return oPrintO; }
	 */

	/**
	 * Alle offenen Auftraege fuer einen bestimmten Mandanten drucken.
	 * 
	 * @param krit
	 *            die Filter- und Sortierkriterien
	 * @param dStichtag
	 *            Date
	 * @param bSortierungNachLiefertermin
	 *            Boolean
	 * @param bInternenKommentarDrucken
	 *            Boolean
	 * @param artikelklasseIId
	 *            Integer
	 * @param artikelgruppeIId
	 *            Integer
	 * @param artikelCNrVon
	 *            String
	 * @param artikelCNrBis
	 *            String
	 * @param projektCBezeichnung
	 *            String
	 * @param iArt
	 *            Integer
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @return JasperPrint der Druck
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAuftragOffeneDetails(
			ReportJournalKriterienDto krit, Date dStichtag,
			Boolean bSortierungNachLiefertermin,
			Boolean bInternenKommentarDrucken, Integer artikelklasseIId,
			Integer artikelgruppeIId, String artikelCNrVon,
			String artikelCNrBis, String projektCBezeichnung, Integer iArt,
			boolean bLagerstandsdetail, boolean bMitAngelegten,
			TheClientDto theClientDto) {

		if (krit == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("reportJournalKriterienDtoI == null"));
		}

		if (dStichtag == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("dStichtag == null"));
		}

		JasperPrintLP oPrintO = null;

		cAktuellerReport = AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS;

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		// vom Stichtag die Uhrzeit abschneiden
		dStichtag = Helper.cutDate(dStichtag);
		try {

			ParametermandantDto parametermandantDto = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ANGEBOT,
							ParameterFac.PARAMETER_LIEFERANT_ANGEBEN);

			boolean bLieferantAngeben = (Boolean) parametermandantDto
					.getCWertAsObject();

			session = factory.openSession();

			// Hiberante Criteria fuer alle Tabellen ausgehend von meiner
			// Haupttabelle anlegen,
			// nach denen ich filtern und sortieren kann
			Criteria critPosition = session
					.createCriteria(FLRAuftragpositionOD.class);

			Collection<String> cPositionart = new LinkedList<String>();
			cPositionart.add(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT);
			cPositionart.add(AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE);
			cPositionart.add(AuftragServiceFac.AUFTRAGPOSITIONART_POSITION);
			critPosition.add(Restrictions.in(
					AuftragpositionFac.FLR_AUFTRAGPOSITIONART_C_NR,
					cPositionart));

			Criteria critAuftrag = critPosition
					.createCriteria(AuftragpositionFac.FLR_AUFTRAGPOSITION_FLRAUFTRAG);
			// Einschraenkung auf den aktuellen Mandanten
			critAuftrag.add(Restrictions.eq(
					AuftragFac.FLR_AUFTRAG_MANDANT_C_NR,
					theClientDto.getMandant()));

			// Einschraenkung nach Status Offen, Teilerledigt
			Collection<String> cStati = new LinkedList<String>();

			if (bMitAngelegten) {
				cStati.add(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT);
			}
			cStati.add(AuftragServiceFac.AUFTRAGSTATUS_OFFEN);
			cStati.add(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT);
			critAuftrag.add(Restrictions.in(
					AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR, cStati));

			// Einschraenkung nach Status Offen
			critPosition
					.add(Restrictions
							.ne(AuftragpositionFac.FLR_AUFTRAGPOSITIONSICHTAUFTRAG_AUFTRAGPOSITIONSTATUS_C_NR,
									AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT));

			// Das Belegdatum muss vor dem Stichtag liegen
			critAuftrag.add(Restrictions.le(
					AuftragFac.FLR_AUFTRAG_D_BELEGDATUM, dStichtag));

			// Der Auftrag darf zum Stichtag noch nicht erledigt worden sein
			critAuftrag.add(Restrictions.or(Restrictions
					.isNull(AuftragFac.FLR_AUFTRAG_T_ERLEDIGT), Restrictions
					.gt(AuftragFac.FLR_AUFTRAG_T_ERLEDIGT, dStichtag)));
			// Einschraenkung nach Auftragart
			Collection<String> cArt = null;
			if (iArt != null) {
				if (iArt == 1) {
					cArt = new LinkedList<String>();
					cArt.add(AuftragServiceFac.AUFTRAGART_FREI);
					cArt.add(AuftragServiceFac.AUFTRAGART_ABRUF);
					critAuftrag.add(Restrictions.in(
							AuftragFac.FLR_AUFTRAG_AUFTRAGART_C_NR, cArt));
				} else if (iArt == 2) {
					cArt = new LinkedList<String>();
					cArt.add(AuftragServiceFac.AUFTRAGART_RAHMEN);
					critAuftrag.add(Restrictions.in(
							AuftragFac.FLR_AUFTRAG_AUFTRAGART_C_NR, cArt));
				}
			}
			// Einschraenkung nach einer bestimmten Kostenstelle
			if (krit.kostenstelleIId != null) {
				critAuftrag.add(Restrictions.eq(
						AuftragFac.FLR_AUFTRAG_KOSTENSTELLE_I_ID,
						krit.kostenstelleIId));
			}

			// Einschraenkung nach einem bestimmten Kunden
			if (krit.kundeIId != null) {
				critAuftrag.add(Restrictions.eq(
						AuftragFac.FLR_AUFTRAG_KUNDE_I_ID_AUFTRAGSADRESSE,
						krit.kundeIId));
			}
			// Einschraenkung nach einem bestimmten Vertreter
			if (krit.vertreterIId != null) {
				critAuftrag.add(Restrictions.eq(
						AuftragFac.FLR_AUFTRAG_VERTRETER_I_ID,
						krit.vertreterIId));
			}
			// Filter nach Projektbezeichnung
			if (projektCBezeichnung != null) {
				critAuftrag.add(Restrictions.like(
						AuftragFac.FLR_AUFTRAG_C_BEZ_PROJEKTBEZEICHNUNG, "%"
								+ projektCBezeichnung + "%"));
			}

			// Einschraenkung nach Belegdatum von - bis
			String sVon = null;
			String sBis = null;

			if (krit.dVon != null) {
				critAuftrag.add(Restrictions.ge(
						AuftragFac.FLR_AUFTRAG_D_BELEGDATUM, krit.dVon));
				sVon = Helper.formatDatum(krit.dVon, theClientDto.getLocUi());
			}

			if (krit.dBis != null) {
				critAuftrag.add(Restrictions.le(
						AuftragFac.FLR_AUFTRAG_D_BELEGDATUM, krit.dBis));
				sBis = Helper.formatDatum(krit.dBis, theClientDto.getLocUi());
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

			if (krit.sBelegnummerVon != null) {
				sVon = HelperServer.getBelegnummernFilterForHibernateCriterias(
						f, iGeschaeftsjahr, sMandantKuerzel,
						krit.sBelegnummerVon);
				critAuftrag.add(Restrictions.ge(AuftragFac.FLR_AUFTRAG_C_NR,
						sVon));
			}
			if (krit.sBelegnummerBis != null) {
				sBis = HelperServer.getBelegnummernFilterForHibernateCriterias(
						f, iGeschaeftsjahr, sMandantKuerzel,
						krit.sBelegnummerBis);
				critAuftrag.add(Restrictions.le(AuftragFac.FLR_AUFTRAG_C_NR,
						sBis));
			}

			// Sortierung nach Kostenstelle ist immer die erste Sortierung
			if (krit.bSortiereNachKostenstelle) {
				critAuftrag.createCriteria(
						AuftragFac.FLR_AUFTRAG_FLRKOSTENSTELLE).addOrder(
						Order.asc(AuftragFac.FLR_AUFTRAG_C_NR));
			}
			// Sortierung nach Kunde, eventuell innerhalb der Kostenstelle

			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				critAuftrag
						.createCriteria(AuftragFac.FLR_AUFTRAG_FLRKUNDE)
						.createCriteria(KundeFac.FLR_PARTNER)
						.addOrder(
								Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
			} else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_BELEGNUMMER
					&& bSortierungNachLiefertermin == false) {
				// es wird in jedem Fall nach der Belegnummer sortiert
				critAuftrag.addOrder(Order.asc(AuftragFac.FLR_AUFTRAG_C_NR));
			}
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_ART) {
				critAuftrag.addOrder(Order
						.asc(AuftragFac.FLR_AUFTRAG_AUFTRAGART_C_NR));
			}

			Criteria critArtikel = critPosition
					.createCriteria(AuftragpositionFac.FLR_AUFTRAGPOSITION_FLRARTIKEL);
			// Sortierung nach Ident
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_IDENT) {
				critArtikel.addOrder(Order.asc(AuftragFac.FLR_AUFTRAG_C_NR));
			}
			if (artikelklasseIId != null || artikelgruppeIId != null
					|| artikelCNrVon != null || artikelCNrBis != null) {
				// Wenn nach Artikelklassen/Gruppen gefiltert wird, dann kommen
				// nur Ident-Positionen
				if (artikelklasseIId != null) {
					critArtikel.createCriteria(
							ArtikelFac.FLR_ARTIKEL_FLRARTIKELKLASSE).add(
							Restrictions.eq("i_id", artikelklasseIId));
				}
				if (artikelgruppeIId != null) {
					critArtikel.createCriteria(
							ArtikelFac.FLR_ARTIKEL_FLRARTIKELGRUPPE).add(
							Restrictions.eq("i_id", artikelgruppeIId));
				}
				if (artikelCNrVon != null) {
					critArtikel.add(Restrictions.ge(
							ArtikelFac.FLR_ARTIKEL_C_NR, artikelCNrVon));
				}
				if (artikelCNrBis != null) {
					critArtikel.add(Restrictions.le(
							ArtikelFac.FLR_ARTIKEL_C_NR, artikelCNrBis));
				}
			}

			// Sortierung nach Liefertermin (optional)
			if (bSortierungNachLiefertermin != null
					&& bSortierungNachLiefertermin.booleanValue()) {
				critPosition
						.addOrder(Order
								.asc(AuftragpositionFac.FLR_AUFTRAGPOSITION_T_UEBERSTEUERTERLIEFERTERMIN));
				critArtikel.addOrder(Order.asc("c_nr"));
				critPosition.addOrder(Order
						.asc(AuftragpositionFac.FLR_AUFTRAGPOSITION_I_SORT));
			}

			List<?> lPosition = critPosition.list();
			int i = 0;
			int iSortierungUeberGanzeListe = 0;
			Iterator<?> itPosition = lPosition.iterator();

			ArrayList<Object[]> alDaten = new ArrayList<Object[]>();

			KostenstelleDto kostenstelleDto = null;
			KundeDto kundeAuftragDto = null;
			KundeDto kundeLieferAdresseDto = null;
			HashMap<Integer, KostenstelleDto> hmKostenstelleCache = new HashMap<Integer, KostenstelleDto>();
			HashMap<Integer, KundeDto> hmKundeCache = new HashMap<Integer, KundeDto>();

			HashMap<Integer, HashMap<String, BigDecimal>> hmLagerstendeAllerArtikel = new HashMap<Integer, HashMap<String, BigDecimal>>();

			// die Datenmatrix pro Auftrag befuellen; alle Felder, die nicht
			// explizit besetzt werden, sind null
			while (itPosition.hasNext()) {
				// Object helper = itPosition.next();
				// Object tmp = helper;
				// FLRAuftragposition flrauftragpositionHelper =
				// (FLRAuftragposition) helper;
				FLRAuftragpositionOD flrauftragposition = (FLRAuftragpositionOD) itPosition
						.next();
				FLRAuftragOD flrauftrag = flrauftragposition.getFlrauftrag();
				StringBuffer buffLos = new StringBuffer("");
				StringBuffer buffFertigungsgruppen = new StringBuffer("");
				// nur nicht erledigte Positionen beruecksichtigen
				if (flrauftragposition.getN_offenemenge().doubleValue() != 0) {
					kostenstelleDto = (KostenstelleDto) hmKostenstelleCache
							.get(flrauftrag.getKostenstelle_i_id());
					// wenn sie noch nicht im cache war, dann aus der db holen
					// und cachen
					if (kostenstelleDto == null) {
						kostenstelleDto = getSystemFac()
								.kostenstelleFindByPrimaryKey(
										flrauftrag.getKostenstelle_i_id());
						hmKostenstelleCache.put(
								flrauftrag.getKostenstelle_i_id(),
								kostenstelleDto);
					}
					// Kunde aus dem Cache holen
					kundeAuftragDto = (KundeDto) hmKundeCache.get(flrauftrag
							.getKunde_i_id_auftragsadresse());
					// wenn er noch nicht im cache war, dann aus der db holen
					// und cachen
					if (kundeAuftragDto == null) {
						kundeAuftragDto = getKundeFac().kundeFindByPrimaryKey(
								flrauftrag.getKunde_i_id_auftragsadresse(),
								theClientDto);
						hmKundeCache.put(
								flrauftrag.getKunde_i_id_auftragsadresse(),
								kundeAuftragDto);
					}
					kundeLieferAdresseDto = (KundeDto) hmKundeCache
							.get(flrauftrag.getKunde_i_id_lieferadresse());
					// wenn er noch nicht im cache war, dann aus der db holen
					// und cachen
					if (kundeLieferAdresseDto == null) {
						kundeLieferAdresseDto = getKundeFac()
								.kundeFindByPrimaryKey(
										flrauftrag
												.getKunde_i_id_lieferadresse(),
										theClientDto);
						hmKundeCache.put(
								flrauftrag.getKunde_i_id_lieferadresse(),
								kundeLieferAdresseDto);
					}
					try {
						session = factory.openSession();
						// Hiberante Criteria fuer alle Tabellen ausgehend von
						// meiner Haupttabelle anlegen,
						// nach denen ich filtern und sortieren kann
						Criteria crit = session
								.createCriteria(FLRLosReport.class);
						crit.add(Restrictions.eq("flrauftrag.i_id",
								flrauftrag.getI_id()));
						List<?> resultList = crit.list();
						Iterator<?> it = resultList.iterator();

						while (it.hasNext()) {
							FLRLosReport flrlos = (FLRLosReport) it.next();
							buffLos.append(flrlos.getC_nr());
							buffLos.append(" ");
							// Fertigungsgruppe
							buffFertigungsgruppen.append(flrlos
									.getFlrfertigungsgruppe().getC_bez());
							if (it.hasNext()) {
								buffFertigungsgruppen.append(" ");
							}
						}
					} finally {
						try {
							session.close();
						} catch (HibernateException he) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_HIBERNATE, he);
						}
					}
					boolean darfVerkaufspreisSehen = getTheJudgeFac().hatRecht(
							RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF,
							theClientDto);

					Object[] zeile = new Object[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ANZAHL_SPALTEN];

					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_LOS_FERTIGUNGSGRUPPE] = buffFertigungsgruppen
							.toString();
					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGCNR] = flrauftrag
							.getC_nr();

					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ZEIT_VERRECHENBAR] = flrauftrag
							.getT_verrechenbar();
					if (flrauftrag.getFlrpersonalverrechenbar() != null) {
						zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_PERSON_VERRECHENBAR] = HelperServer
								.formatNameAusFLRPartner(flrauftrag
										.getFlrpersonalverrechenbar()
										.getFlrpartner());
					}

					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGARTCNR] = flrauftrag
							.getAuftragart_c_nr();
					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGKUNDE] = kundeAuftragDto
							.getPartnerDto().getCName1nachnamefirmazeile1();

					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ORIGINAL_SORTIERUNG] = i;
					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_SORTIERUNG_UEBER_GANZE_LISTE] = iSortierungUeberGanzeListe;

					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_SORTIERUNG_STKLPOS] = 1;
					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_IST_STUECKLISTENPOSITION] = new Boolean(
							false);
					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_SPEDITEUR] = flrauftrag
							.getFlrspediteur().getC_namedesspediteurs();

					if (kundeAuftragDto.getPartnerDto()
							.getCName2vornamefirmazeile2() != null) {
						zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGKUNDE2] = kundeAuftragDto
								.getPartnerDto().getCName2vornamefirmazeile2();
					}
					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_KUNDEAUFTRAGADRESSE] = kundeAuftragDto
							.getPartnerDto().getCKbez()
							+ " "
							+ kundeAuftragDto.getPartnerDto().formatAdresse();
					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_KUNDELIEFERADRESSE] = kundeLieferAdresseDto
							.getPartnerDto().getCKbez()
							+ " "
							+ kundeLieferAdresseDto.getPartnerDto()
									.formatAdresse();

					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_KUNDELIEFERADRESSE_LIEFERDAUER] = kundeLieferAdresseDto
							.getILieferdauer();

					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_LOSNUMMER] = buffLos
							.toString();
					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_KOSTENSTELLECNR] = kostenstelleDto
							.getCNr();
					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGLIEFERTERMIN] = Helper
							.formatDatum(flrauftrag.getT_liefertermin(),
									theClientDto.getLocUi());
					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGFINALTERMIN] = Helper
							.formatDatum(flrauftrag.getT_finaltermin(),
									theClientDto.getLocUi());

					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_INTERNERKOMMENTAR] = flrauftrag
							.getX_internerkommentar();
					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_EXTERNERKOMMENTAR] = flrauftrag
							.getX_externerkommentar();
					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGZAHLUNGSZIEL] = "";
					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGPROJEKTBEZEICHNUNG] = flrauftrag
							.getC_bez();
					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGBESTELLNUMMER] = flrauftrag
							.getC_bestellnummer();
					// AUFTRAGSEIGENSCHAFTEN
					Hashtable<?, ?> hAE = getAuftragEigenschaften(
							flrauftrag.getI_id(), theClientDto);
					if (hAE != null) {
						zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGEIGENSCHAFT_FA] = hAE
								.get(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_FA);
						zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGEIGENSCHAFT_CLUSTER] = hAE
								.get(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_CLUSTER);
						zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGEIGENSCHAFT_EQNR] = hAE
								.get(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_EQNR);
					}
					if (flrauftragposition.getFlrauftragpositionrahmen() != null) {
						zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_RAHMENAUFTRAG] = flrauftragposition
								.getFlrauftragpositionrahmen().getFlrauftrag()
								.getC_nr();
					}

					// nur mengenbehaftete Positionen beruecksichtigen
					if (flrauftragposition.getN_menge() != null) {
						zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELCNR] = flrauftragposition
								.getFlrartikel().getC_nr();

						zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKEL_FERTIGUNGSSATZGROESSE] = flrauftragposition
								.getFlrartikel().getF_fertigungssatzgroesse();
						zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKEL_LAGERMINDEST] = flrauftragposition
								.getFlrartikel().getF_lagermindest();
						zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKEL_LAGERSOLL] = flrauftragposition
								.getFlrartikel().getF_lagersoll();
						zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKEL_RAHMENRESERVIERUNGEN] = getReservierungFac()
								.getAnzahlRahmenreservierungen(
										flrauftragposition.getFlrartikel()
												.getI_id(), theClientDto);

						// Fertigungsgruppe von Stueckliste des Artikels, wenn
						// vorhanden.
						StuecklisteDto stklDto = getStuecklisteFac()
								.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
										flrauftragposition.getFlrartikel()
												.getI_id(), theClientDto);
						if (stklDto != null) {
							FertigungsgruppeDto fertigungsgruppeDto = getStuecklisteFac()
									.fertigungsgruppeFindByPrimaryKey(
											stklDto.getFertigungsgruppeIId());
							zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_STKL_FERTIGUNGSGRUPPE] = fertigungsgruppeDto
									.getCBez();

						}

						zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGPROJEKTBEZEICHNUNG] = flrauftrag
								.getC_bez();
						zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGCNR] = flrauftrag
								.getC_nr();
						zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGARTCNR] = flrauftrag
								.getAuftragart_c_nr();
						String cArtikelBezeichnung = "";
						if (flrauftragposition
								.getAuftragpositionart_c_nr()
								.equals(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)
								|| flrauftragposition
										.getAuftragpositionart_c_nr()
										.equals(AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE)) {
							cArtikelBezeichnung = getArtikelFac()
									.baueArtikelBezeichnungMehrzeiligOhneExc(
											flrauftragposition
													.getArtikel_i_id(),
											flrauftragposition
													.getAuftragpositionart_c_nr(),
											flrauftragposition.getC_bez(),
											flrauftragposition.getC_zbez(),
											false, null, theClientDto);

							// Offene Bestellmenge
							zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELBESTELLTMENGE] = getArtikelbestelltFac()
									.getAnzahlBestellt(
											flrauftragposition
													.getArtikel_i_id());

							befuelleOffeneDetailMitLagerstaendenDerLagerarten(
									flrauftragposition.getArtikel_i_id(),
									hmLagerstendeAllerArtikel, zeile,
									theClientDto);

						}

						zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELBEZEICHNUNG] = cArtikelBezeichnung;
						zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELMENGE] = flrauftragposition
								.getN_menge();

						zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_POSITIONSTERMIN_OHNE_LIEFERDAUER] = flrauftragposition
								.getT_uebersteuerterliefertermin();

						if (flrauftragposition
								.getT_uebersteuerterliefertermin() != null) {

							java.util.Date lieferdatum = Helper
									.addiereTageZuDatum(
											flrauftragposition
													.getT_uebersteuerterliefertermin(),
											kundeLieferAdresseDto
													.getILieferdauer() * -1);

							zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_POSITIONSTERMIN] = Helper
									.formatDatum(lieferdatum,
											theClientDto.getLocUi());
						}
						zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_POSITIONSTERMIN_TIMESTAMP] = flrauftragposition
								.getT_uebersteuerterliefertermin();
						zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKEL_I_ID] = flrauftragposition
								.getArtikel_i_id();

						zeile[REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTAND_BEREITS_ABGEZOGEN] = false;

						zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELEINHEIT] = flrauftragposition
								.getEinheit_c_nr() == null ? null
								: flrauftragposition.getEinheit_c_nr().trim();

						// Positionspreise sind in Belegwaehrung abgelegt
						BigDecimal nPreisInBelegwaehrung = flrauftragposition
								.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte();

						nPreisInBelegwaehrung = getBetragMalWechselkurs(
								nPreisInBelegwaehrung,
								Helper.getKehrwert(new BigDecimal(
										flrauftrag
												.getF_wechselkursmandantwaehrungzuauftragswaehrung()
												.doubleValue())));
						if (darfVerkaufspreisSehen) {
							zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELNETTOGESAMTPREISPLUSVERSTECKTERAUFSCHLAGMINUSRABATTE] = nPreisInBelegwaehrung;
						} else {
							zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELNETTOGESAMTPREISPLUSVERSTECKTERAUFSCHLAGMINUSRABATTE] = null;
						}

						// Grundlage ist der Gestehungspreis des Artikels am
						// Hauptlager des Mandanten
						BigDecimal bdGelieferteMenge = flrauftragposition
								.getN_menge();
						if (flrauftrag.getAuftragart_c_nr().equals(
								AuftragServiceFac.AUFTRAGART_RAHMEN)) {
							bdGelieferteMenge = flrauftragposition.getN_menge()
									.subtract(
											flrauftragposition
													.getN_offenerahmenmenge());
						} else {
							bdGelieferteMenge = flrauftragposition.getN_menge()
									.subtract(
											flrauftragposition
													.getN_offenemenge());
						}

						if (darfVerkaufspreisSehen) {

							BigDecimal bdGestehungspreis = new BigDecimal(0);
							if (bLieferantAngeben == true) {
								if (flrauftragposition.getN_einkaufpreis() != null) {
									bdGestehungspreis = flrauftragposition
											.getN_einkaufpreis();
								} else {
									ArtikellieferantDto alDto = getArtikelFac()
											.getArtikelEinkaufspreis(
													flrauftragposition
															.getArtikel_i_id(),
													null,
													flrauftragposition
															.getN_menge(),
													theClientDto
															.getSMandantenwaehrung(),
													new java.sql.Date(
															flrauftragposition
																	.getFlrauftrag()
																	.getT_belegdatum()
																	.getTime()),
													theClientDto);

									if (alDto != null
											&& alDto.getNNettopreis() != null) {
										bdGestehungspreis = alDto
												.getNNettopreis();
									} else {
										bdGestehungspreis = getLagerFac()
												.getGemittelterGestehungspreisDesHauptlagers(
														flrauftragposition
																.getArtikel_i_id(),
														theClientDto);
									}

								}

							} else {
								bdGestehungspreis = getLagerFac()
										.getGemittelterGestehungspreisDesHauptlagers(
												flrauftragposition
														.getArtikel_i_id(),
												theClientDto);
							}

							BigDecimal bdEinkaufspreis = new BigDecimal(0);

							if (flrauftragposition.getN_einkaufpreis() != null) {
								bdEinkaufspreis = flrauftragposition
										.getN_einkaufpreis();
							}

							zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_EINKAUFSPREIS] = bdEinkaufspreis;

							zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELGESTEHUNGSPREIS] = bdGestehungspreis;

							zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELOFFENERWERT] = flrauftragposition
									.getN_offenemenge().multiply(
											nPreisInBelegwaehrung);
							zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELOFFENERDB] = flrauftragposition
									.getN_offenemenge()
									.multiply(
											nPreisInBelegwaehrung
													.subtract(bdGestehungspreis));
						} else {
							zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_EINKAUFSPREIS] = null;
							zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELGESTEHUNGSPREIS] = null;
							zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELOFFENERWERT] = null;
							zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELOFFENERDB] = null;

						}
						zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELGELIEFERTEMENGE] = bdGelieferteMenge;
						zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELOFFENEMENGE] = flrauftragposition
								.getN_offenemenge();
						zeile[REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTANDS_FEHLMENGE] = new BigDecimal(
								0);
						zeile[REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTANDS_FEHLMENGE_KUMULIERT] = new BigDecimal(
								0);

						// die Positionen brauchen alle Attribute, nach denen im
						// Report gruppiert wird
						zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGCNR] = flrauftrag
								.getC_nr();
						zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGARTCNR] = flrauftrag
								.getAuftragart_c_nr();
						zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_KOSTENSTELLECNR] = kostenstelleDto
								.getCNr();
						zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGKUNDE] = kundeAuftragDto
								.getPartnerDto().getCName1nachnamefirmazeile1();
						zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_B_LIEFERTERMIN] = flrauftrag
								.getB_lieferterminunverbindlich();

						// PJ17840

						alDaten.add(zeile);

						if (stklDto != null
								&& Helper.short2boolean(stklDto
										.getBDruckeinlagerstandsdetail()) == true
								&& bLagerstandsdetail == true) {

							StuecklisteInfoDto stklposDtos = getStuecklisteFac()
									.getStrukturdatenEinesArtikels(
											flrauftragposition.getFlrartikel()
													.getI_id(),
											false,
											null,
											0,
											1,
											true,
											flrauftragposition
													.getN_offenemenge(), true,
											theClientDto);

							ArrayList<StuecklisteMitStrukturDto> strukt = stklposDtos
									.getAlStuecklisteAufgeloest();

							for (int u = 0; u < strukt.size(); u++) {

								StuecklisteMitStrukturDto struktDto = strukt
										.get(u);

								befuelleOffeneDetailMitLagerstaendenDerLagerarten(
										struktDto.getStuecklistepositionDto()
												.getArtikelIId(),
										hmLagerstendeAllerArtikel, zeile,
										theClientDto);

							}
						}
						iSortierungUeberGanzeListe++;
						i++;
					}

				}

			}

			// PJ17840
			if (bLagerstandsdetail == true) {

				if (bSortierungNachLiefertermin == true) {
					// PJ18028
					// Zuerst noch die Lieferdauer der Lieferadresse vom Termin
					// abziehen;
					for (int k = 0; k < alDaten.size(); k++) {
						Object[] zeile = alDaten.get(k);

						java.util.Date lieferdatum = (java.util.Date) zeile[REPORT_AUFTRAG_OFFENE_DETAILS_POSITIONSTERMIN_TIMESTAMP];
						lieferdatum = Helper
								.addiereTageZuDatum(
										lieferdatum,
										((Integer) zeile[REPORT_AUFTRAG_OFFENE_DETAILS_KUNDELIEFERADRESSE_LIEFERDAUER])
												.intValue() * -1);
						zeile[REPORT_AUFTRAG_OFFENE_DETAILS_POSITIONSTERMIN_TIMESTAMP] = lieferdatum;
					}
				}

				// Zuerst nach ARTIKEL + TERMIN sortieren
				alDaten = sortiereOffeneDetailsNachArtikelnummerUndTermin(alDaten);

				// Nun die Offenen Mengen um die Lagerstaende reduzieren

				for (int k = 0; k < alDaten.size(); k++) {
					Object[] zeile = alDaten.get(k);

					offeneDetailsDetailauswertungLagerstaendeAbziehen(
							hmLagerstendeAllerArtikel, zeile);

				}

				// Nun fuer die Offenen Mengen die Stuecklisten Positionen +
				// Mengen hinzufuegen

				ArrayList<Object[]> alDatenMitStuecklistenpositionen = new ArrayList<Object[]>();

				for (int k = 0; k < alDaten.size(); k++) {
					Object[] zeile = alDaten.get(k);

					Integer artikelIId = (Integer) zeile[REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKEL_I_ID];
					StuecklisteDto stklDto = getStuecklisteFac()
							.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
									artikelIId, theClientDto);
					if (stklDto != null
							&& Helper.short2boolean(stklDto
									.getBDruckeinlagerstandsdetail()) == true) {

						StuecklisteInfoDto stklposDtos = getStuecklisteFac()
								.getStrukturdatenEinesArtikels(
										artikelIId,
										false,
										null,
										0,
										1,
										true,
										(BigDecimal) zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTANDS_FEHLMENGE],
										true, theClientDto);

						ArrayList<StuecklisteMitStrukturDto> strukt = stklposDtos
								.getAlStuecklisteAufgeloest();

						int iSortStklPos = 1;
						for (int u = 0; u < strukt.size(); u++) {
							iSortStklPos++;
							StuecklisteMitStrukturDto struktDto = strukt.get(u);

							Object[] zeileTemp = zeile.clone();
							zeileTemp[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKEL_I_ID] = struktDto
									.getStuecklistepositionDto()
									.getArtikelIId();

							ArtikelDto aDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(
											struktDto
													.getStuecklistepositionDto()
													.getArtikelIId(),
											theClientDto);

							zeileTemp[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKEL_LAGERSOLL] = aDto
									.getFLagersoll();
							zeileTemp[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKEL_LAGERMINDEST] = aDto
									.getFLagermindest();
							zeileTemp[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELBEZEICHNUNG] = aDto
									.formatBezeichnung();

							zeileTemp[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELBEZEICHNUNG] = aDto
									.formatBezeichnung();
							zeileTemp[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELMENGE] = struktDto
									.getStuecklistepositionDto().getNMenge();
							zeileTemp[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELOFFENEMENGE] = struktDto
									.getStuecklistepositionDto().getNMenge();
							zeileTemp[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_SORTIERUNG_STKLPOS] = iSortStklPos;

							zeileTemp[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_EINKAUFSPREIS] = null;
							zeileTemp[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELBESTELLTMENGE] = null;
							zeileTemp[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELEINHEIT] = null;
							zeileTemp[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELGELIEFERTEMENGE] = null;
							zeileTemp[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELGESTEHUNGSPREIS] = null;
							zeileTemp[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELNETTOGESAMTPREISPLUSVERSTECKTERAUFSCHLAGMINUSRABATTE] = null;
							zeileTemp[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_IST_STUECKLISTENPOSITION] = new Boolean(
									true);
							zeileTemp[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELOFFENERDB] = null;
							zeileTemp[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELOFFENERWERT] = null;

							zeileTemp[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTAND_BEREITS_ABGEZOGEN] = false;
							zeileTemp[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_SORTIERUNG_UEBER_GANZE_LISTE] = iSortierungUeberGanzeListe;

							zeileTemp[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELCNR] = struktDto
									.getStuecklistepositionDto()
									.getArtikelDto().getCNr();

							alDatenMitStuecklistenpositionen.add(zeileTemp);

						}
					}
					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_SORTIERUNG_STKLPOS] = 0;
					alDatenMitStuecklistenpositionen.add(zeile);

				}

				// Nun wieder nach Artikelnummer + Termin sortieren, da nun die
				// Stkl-Positionen einsortiert werden muessen
				alDatenMitStuecklistenpositionen = sortiereOffeneDetailsNachArtikelnummerUndTermin(alDatenMitStuecklistenpositionen);

				// Nun wieder die Lagerstaende abziehen
				for (int k = 0; k < alDatenMitStuecklistenpositionen.size(); k++) {
					Object[] zeile = alDatenMitStuecklistenpositionen.get(k);

					offeneDetailsDetailauswertungLagerstaendeAbziehen(
							hmLagerstendeAllerArtikel, zeile);

				}

				// Nun die Lagerstands-Fehlmenge kumulieren PJ17913
				HashMap<Integer, BigDecimal> hmFehlmengen = new HashMap<Integer, BigDecimal>();
				for (int k = 0; k < alDatenMitStuecklistenpositionen.size(); k++) {
					Object[] zeile = alDatenMitStuecklistenpositionen.get(k);

					Integer artikelIId = (Integer) zeile[REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKEL_I_ID];

					BigDecimal bdLagerstandsFehlmengeKumuliert = (BigDecimal) zeile[REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTANDS_FEHLMENGE];

					if (hmFehlmengen.containsKey(artikelIId)) {
						bdLagerstandsFehlmengeKumuliert = hmFehlmengen.get(
								artikelIId)
								.add(bdLagerstandsFehlmengeKumuliert);
					}

					hmFehlmengen.put(artikelIId,
							bdLagerstandsFehlmengeKumuliert);

					zeile[REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTANDS_FEHLMENGE_KUMULIERT] = bdLagerstandsFehlmengeKumuliert;

				}

				// Wieder ins Original zuruecksortieren

				for (int k = alDatenMitStuecklistenpositionen.size() - 1; k > 0; --k) {
					for (int j = 0; j < k; ++j) {
						Object[] a1 = (Object[]) alDatenMitStuecklistenpositionen
								.get(j);
						Object[] a2 = (Object[]) alDatenMitStuecklistenpositionen
								.get(j + 1);

						Integer sort1 = (Integer) a1[REPORT_AUFTRAG_OFFENE_DETAILS_ORIGINAL_SORTIERUNG];
						Integer sort2 = (Integer) a2[REPORT_AUFTRAG_OFFENE_DETAILS_ORIGINAL_SORTIERUNG];

						if (sort1 > sort2) {
							alDatenMitStuecklistenpositionen.set(j, a2);
							alDatenMitStuecklistenpositionen.set(j + 1, a1);
						} else if (sort1 == sort2) {
							Integer subSort1 = (Integer) a1[REPORT_AUFTRAG_OFFENE_DETAILS_SORTIERUNG_STKLPOS];
							Integer subSort2 = (Integer) a2[REPORT_AUFTRAG_OFFENE_DETAILS_SORTIERUNG_STKLPOS];
							if (subSort1 > subSort2) {
								alDatenMitStuecklistenpositionen.set(j, a2);
								alDatenMitStuecklistenpositionen.set(j + 1, a1);
							}
						}
					}
				}
				data = new Object[alDatenMitStuecklistenpositionen.size()][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ANZAHL_SPALTEN];
				data = (Object[][]) alDatenMitStuecklistenpositionen
						.toArray(data);
			} else {
				data = new Object[alDaten.size()][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ANZAHL_SPALTEN];
				data = (Object[][]) alDaten.toArray(data);
			}

			// die Parameter dem Report uebergeben
			HashMap<String, Object> parameter = new HashMap<String, Object>();
			parameter.put("P_LAGERSTANDSDETAILAUSWERTUNG", new Boolean(
					bLagerstandsdetail));

			parameter.put(LPReport.P_SORTIERUNG,
					buildSortierungAuftragOffeneDetails(krit, theClientDto));
			parameter.put(
					LPReport.P_FILTER,
					buildFilterAuftragOffeneDetails(krit, artikelklasseIId,
							artikelgruppeIId, artikelCNrVon, artikelCNrBis,
							projektCBezeichnung, theClientDto));

			parameter
					.put(LPReport.P_SORTIERENACHKUNDE,
							new Boolean(
									krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER));
			parameter.put(LPReport.P_SORTIERENACHKOSTENSTELLE, new Boolean(
					krit.bSortiereNachKostenstelle));
			parameter
					.put("P_SORTIERENACHAUFTRAGART",
							new Boolean(
									krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_ART));
			parameter.put("P_AUFTRAGWAEHRUNG",
					theClientDto.getSMandantenwaehrung());
			parameter.put("P_STICHTAG",
					Helper.formatDatum(dStichtag, theClientDto.getLocUi()));

			initJRDS(parameter, AuftragReportFac.REPORT_MODUL,
					AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);

			oPrintO = getReportPrint();
		} catch (RemoteException re) {
			throwEJBExceptionLPRespectOld(re);
		}
		return oPrintO;
	}

	private void offeneDetailsDetailauswertungLagerstaendeAbziehen(
			HashMap<Integer, HashMap<String, BigDecimal>> hmLagerstendeAllerArtikel,
			Object[] zeile) {

		if (((Boolean) zeile[REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTAND_BEREITS_ABGEZOGEN]) == true) {
			return;
		}

		// Zuerst Hauptlager (=Hauptlager+Normallager) dann
		// Lieferantenlager dann Halbfertiglager

		// Wenn nichts mehr auf Lager, dann Fehlmenge anzeigen

		HashMap<String, BigDecimal> hmLagerstaendeAllerLagerarten = hmLagerstendeAllerArtikel
				.get(zeile[REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKEL_I_ID]);

		zeile[REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTAND_BEREITS_ABGEZOGEN] = true;
		zeile[REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELLAGERSTAND] = hmLagerstaendeAllerLagerarten
				.get(TOKEN_GESAMTLAGERSTAND);

		zeile[REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTAND_HALBFERTIG] = hmLagerstaendeAllerLagerarten
				.get(LagerFac.LAGERART_HALBFERTIG);
		zeile[REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTAND_KUNDENLAGER] = hmLagerstaendeAllerLagerarten
				.get(LagerFac.LAGERART_KUNDENLAGER);
		zeile[REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTAND_LIEFERANT] = hmLagerstaendeAllerLagerarten
				.get(LagerFac.LAGERART_LIEFERANT);
		zeile[REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTAND_NORMALLAGER] = hmLagerstaendeAllerLagerarten
				.get(LagerFac.LAGERART_NORMAL);
		zeile[REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTAND_PERSOENLICH] = hmLagerstaendeAllerLagerarten
				.get(LagerFac.LAGERART_PERSOENLICH);
		zeile[REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTAND_SCHROTT] = hmLagerstaendeAllerLagerarten
				.get(LagerFac.LAGERART_SCHROTT);
		zeile[REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTAND_SPERRLAGER] = hmLagerstaendeAllerLagerarten
				.get(LagerFac.LAGERART_SPERRLAGER);
		zeile[REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTAND_ZOLLLAGER] = hmLagerstaendeAllerLagerarten
				.get(LagerFac.LAGERART_ZOLLLAGER);

		zeile[REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELLAGERSTAND] = hmLagerstaendeAllerLagerarten
				.get(TOKEN_GESAMTLAGERSTAND);

		BigDecimal offeneMenge = (BigDecimal) zeile[REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELOFFENEMENGE];

		if (offeneMenge.doubleValue() > 0) {
			// Zuerst von Normallager holen
			BigDecimal lagerstandNormal = hmLagerstaendeAllerLagerarten
					.get(LagerFac.LAGERART_NORMAL);

			if (offeneMenge.doubleValue() > lagerstandNormal.doubleValue()) {
				hmLagerstaendeAllerLagerarten.put(LagerFac.LAGERART_NORMAL,
						new BigDecimal(0));
				hmLagerstaendeAllerLagerarten.put(
						TOKEN_GESAMTLAGERSTAND,
						hmLagerstaendeAllerLagerarten.get(
								TOKEN_GESAMTLAGERSTAND).subtract(
								lagerstandNormal));
				offeneMenge = offeneMenge.subtract(lagerstandNormal);
			} else {
				hmLagerstaendeAllerLagerarten.put(LagerFac.LAGERART_NORMAL,
						lagerstandNormal.subtract(offeneMenge));
				hmLagerstaendeAllerLagerarten.put(
						TOKEN_GESAMTLAGERSTAND,
						hmLagerstaendeAllerLagerarten.get(
								TOKEN_GESAMTLAGERSTAND).subtract(offeneMenge));
				offeneMenge = new BigDecimal(0);
			}

		}
		if (offeneMenge.doubleValue() > 0) {
			// Zuerst von Normallager holen
			BigDecimal lagerstandLieferantenlager = hmLagerstaendeAllerLagerarten
					.get(LagerFac.LAGERART_LIEFERANT);

			if (offeneMenge.doubleValue() > lagerstandLieferantenlager
					.doubleValue()) {
				hmLagerstaendeAllerLagerarten.put(LagerFac.LAGERART_LIEFERANT,
						new BigDecimal(0));
				hmLagerstaendeAllerLagerarten.put(
						TOKEN_GESAMTLAGERSTAND,
						hmLagerstaendeAllerLagerarten.get(
								TOKEN_GESAMTLAGERSTAND).subtract(
								lagerstandLieferantenlager));
				offeneMenge = offeneMenge.subtract(lagerstandLieferantenlager);
			} else {
				hmLagerstaendeAllerLagerarten.put(LagerFac.LAGERART_LIEFERANT,
						lagerstandLieferantenlager.subtract(offeneMenge));
				hmLagerstaendeAllerLagerarten.put(
						TOKEN_GESAMTLAGERSTAND,
						hmLagerstaendeAllerLagerarten.get(
								TOKEN_GESAMTLAGERSTAND).subtract(offeneMenge));
				offeneMenge = new BigDecimal(0);
			}

		}

		if (offeneMenge.doubleValue() > 0) {
			// Zuerst von Normallager holen
			BigDecimal lagerstandHalbfertig = hmLagerstaendeAllerLagerarten
					.get(LagerFac.LAGERART_HALBFERTIG);

			if (offeneMenge.doubleValue() > lagerstandHalbfertig.doubleValue()) {
				hmLagerstaendeAllerLagerarten.put(LagerFac.LAGERART_HALBFERTIG,
						new BigDecimal(0));
				hmLagerstaendeAllerLagerarten.put(
						TOKEN_GESAMTLAGERSTAND,
						hmLagerstaendeAllerLagerarten.get(
								TOKEN_GESAMTLAGERSTAND).subtract(
								lagerstandHalbfertig));
				offeneMenge = offeneMenge.subtract(lagerstandHalbfertig);
			} else {
				hmLagerstaendeAllerLagerarten.put(LagerFac.LAGERART_HALBFERTIG,
						lagerstandHalbfertig.subtract(offeneMenge));
				hmLagerstaendeAllerLagerarten.put(
						TOKEN_GESAMTLAGERSTAND,
						hmLagerstaendeAllerLagerarten.get(
								TOKEN_GESAMTLAGERSTAND).subtract(offeneMenge));
				offeneMenge = new BigDecimal(0);
			}

		}

		if (offeneMenge.doubleValue() > 0) {
			zeile[REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTANDS_FEHLMENGE] = offeneMenge;
		} else {
			zeile[REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTANDS_FEHLMENGE] = new BigDecimal(
					0);
		}

		hmLagerstendeAllerArtikel.put(
				(Integer) zeile[REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKEL_I_ID],
				hmLagerstaendeAllerLagerarten);
	}

	private ArrayList<Object[]> sortiereOffeneDetailsNachArtikelnummerUndTermin(
			ArrayList<Object[]> alDaten) {
		for (int k = alDaten.size() - 1; k > 0; --k) {
			for (int j = 0; j < k; ++j) {
				Object[] a1 = (Object[]) alDaten.get(j);
				Object[] a2 = (Object[]) alDaten.get(j + 1);

				String sort1 = (String) a1[REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELCNR];
				String sort2 = (String) a2[REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELCNR];

				if (sort1.compareTo(sort2) > 0) {
					alDaten.set(j, a2);
					alDaten.set(j + 1, a1);
				} else if (sort1.compareTo(sort2) == 0) {

					java.util.Date t1 = (java.util.Date) a1[REPORT_AUFTRAG_OFFENE_DETAILS_POSITIONSTERMIN_TIMESTAMP];
					java.util.Date t2 = (java.util.Date) a2[REPORT_AUFTRAG_OFFENE_DETAILS_POSITIONSTERMIN_TIMESTAMP];

					if (t1.after(t2)) {
						alDaten.set(j, a2);
						alDaten.set(j + 1, a1);
					}
				}
			}
		}

		return alDaten;
	}

	private Object[] befuelleOffeneDetailMitLagerstaendenDerLagerarten(
			Integer artikelIId,
			HashMap<Integer, HashMap<String, BigDecimal>> hmLagerstendeAllerArtikel,
			Object[] zeile, TheClientDto theClientDto) {
		HashMap<String, BigDecimal> lagerstaende = getLagerFac()
				.getLagerstaendeAllerLagerartenOhneKeinLager(artikelIId,
						theClientDto);

		if (hmLagerstendeAllerArtikel.containsKey(artikelIId)) {
			zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELLAGERSTAND] = hmLagerstendeAllerArtikel
					.get(artikelIId).get(TOKEN_GESAMTLAGERSTAND);
		} else {
			HashMap<String, BigDecimal> hmLagerstaendeEinesArtikels = new HashMap<String, BigDecimal>();
			BigDecimal lagerstandGesamt = lagerstaende
					.get(LagerFac.LAGERART_KUNDENLAGER)
					.add(lagerstaende.get(LagerFac.LAGERART_NORMAL))
					.add(lagerstaende.get(LagerFac.LAGERART_HAUPTLAGER))
					.add(lagerstaende.get(LagerFac.LAGERART_PERSOENLICH))
					.add(lagerstaende.get(LagerFac.LAGERART_SCHROTT))
					.add(lagerstaende.get(LagerFac.LAGERART_SPERRLAGER))
					.add(lagerstaende.get(LagerFac.LAGERART_ZOLLLAGER))
					.add(lagerstaende.get(LagerFac.LAGERART_LIEFERANT))
					.add(lagerstaende.get(LagerFac.LAGERART_HALBFERTIG));

			hmLagerstaendeEinesArtikels.put(TOKEN_GESAMTLAGERSTAND,
					lagerstandGesamt);
			hmLagerstaendeEinesArtikels.put(LagerFac.LAGERART_KUNDENLAGER,
					lagerstaende.get(LagerFac.LAGERART_KUNDENLAGER));
			hmLagerstaendeEinesArtikels.put(
					LagerFac.LAGERART_NORMAL,
					lagerstaende.get(LagerFac.LAGERART_NORMAL).add(
							lagerstaende.get(LagerFac.LAGERART_HAUPTLAGER)));

			hmLagerstaendeEinesArtikels.put(LagerFac.LAGERART_PERSOENLICH,
					lagerstaende.get(LagerFac.LAGERART_PERSOENLICH));
			hmLagerstaendeEinesArtikels.put(LagerFac.LAGERART_SCHROTT,
					lagerstaende.get(LagerFac.LAGERART_SCHROTT));
			hmLagerstaendeEinesArtikels.put(LagerFac.LAGERART_SPERRLAGER,
					lagerstaende.get(LagerFac.LAGERART_SPERRLAGER));
			hmLagerstaendeEinesArtikels.put(LagerFac.LAGERART_ZOLLLAGER,
					lagerstaende.get(LagerFac.LAGERART_ZOLLLAGER));
			hmLagerstaendeEinesArtikels.put(LagerFac.LAGERART_LIEFERANT,
					lagerstaende.get(LagerFac.LAGERART_LIEFERANT));
			hmLagerstaendeEinesArtikels.put(LagerFac.LAGERART_HALBFERTIG,
					lagerstaende.get(LagerFac.LAGERART_HALBFERTIG));
			hmLagerstendeAllerArtikel.put(artikelIId,
					hmLagerstaendeEinesArtikels);
		}

		return zeile;
	}

	/**
	 * Alle offenen Auftraege fuer einen bestimmten Mandanten drucken.
	 * 
	 * @param krit
	 *            die Filter- und Sortierkriterien
	 * @param bSortierungNachLiefertermin
	 *            Boolean
	 * @param bOhnePositionen
	 *            Boolean
	 * @param fertigungsgruppeIId
	 *            Integer
	 * @param iArt
	 *            Integer
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @return JasperPrint der Druck
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAuftragOffenePositionen(
			ReportJournalKriterienDto krit, Date dVon, Date dStichtagBzwBis,
			Boolean bSortierungNachLiefertermin, Boolean bOhnePositionen,
			Boolean bSortierungNachAbliefertermin,
			Integer[] fertigungsgruppeIId, Integer iArt,
			Boolean bSortierungNurLiefertermin, String sReportname,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (krit == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("reportJournalKriterienDtoI == null"));
		}

		if (dStichtagBzwBis == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("dStichtagBzwBis == null"));
		}

		JasperPrintLP oPrintO = null;

		cAktuellerReport = AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN;

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		// vom Stichtag die Uhrzeit abschneiden
		dStichtagBzwBis = Helper.cutDate(dStichtagBzwBis);
		try {
			session = factory.openSession();
			Criteria critPosition = null;
			Criteria critAuftrag = null;
			Criteria critStkl = null;
			critPosition = session.createCriteria(FLRAuftragpositionOP.class);
			critAuftrag = critPosition
					.createCriteria(AuftragpositionFac.FLR_AUFTRAGPOSITION_FLRAUFTRAG);
			Collection<String> cPositionart = new LinkedList<String>();
			cPositionart.add(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT);
			cPositionart.add(AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE);
			if (bOhnePositionen) {
				critPosition.add(Restrictions.and(Restrictions.in(
						AuftragpositionFac.FLR_AUFTRAGPOSITIONART_C_NR,
						cPositionart), Restrictions.isNull("position_i_id")));
			} else {
				critPosition.add(Restrictions.in(
						AuftragpositionFac.FLR_AUFTRAGPOSITIONART_C_NR,
						cPositionart));
			}
			// Einschraenkung auf den aktuellen Mandanten
			critAuftrag.add(Restrictions.eq(
					AuftragFac.FLR_AUFTRAG_MANDANT_C_NR,
					theClientDto.getMandant()));
			// keine Positionen mit Menge null
			critPosition.add(Restrictions
					.isNotNull(AuftragpositionFac.FLR_AUFTRAGPOSITION_N_MENGE));
			// Einschraenkung nach Status Offen, Teilerledigt, Erledigt
			Collection<String> cStati = new LinkedList<String>();
			cStati.add(AuftragServiceFac.AUFTRAGSTATUS_OFFEN);
			cStati.add(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT);
			critAuftrag.add(Restrictions.in(
					AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR, cStati));
			// Der Liefertermin muss nach dem von Stichtag liegen
			if (dVon != null) {
				// PJ18713
				critPosition
						.add(Restrictions
								.ge(AuftragpositionFac.FLR_AUFTRAGPOSITIONSICHTAUFTRAG_T_UEBERSTEUERTERLIEFERTERMIN,
										dVon));
			}
			// Der Liefertermin muss vor dem Stichtag liegen
			critPosition
					.add(Restrictions
							.le(AuftragpositionFac.FLR_AUFTRAGPOSITIONSICHTAUFTRAG_T_UEBERSTEUERTERLIEFERTERMIN,
									dStichtagBzwBis));
			// critAuftrag.add(Restrictions.le(AuftragFac.FLR_AUFTRAG_D_BELEGDATUM
			// , dStichtag));
			// Der Auftrag darf zum Stichtag noch nicht erledigt worden sein
			critAuftrag.add(Restrictions.or(Restrictions
					.isNull(AuftragFac.FLR_AUFTRAG_T_ERLEDIGT), Restrictions
					.gt(AuftragFac.FLR_AUFTRAG_T_ERLEDIGT, dStichtagBzwBis)));
			// Einschraenkung nach Auftragart
			Collection<String> cArt = null;
			if (iArt != null) {
				if (iArt == 1) {
					cArt = new LinkedList<String>();
					cArt.add(AuftragServiceFac.AUFTRAGART_FREI);
					cArt.add(AuftragServiceFac.AUFTRAGART_ABRUF);
					critAuftrag.add(Restrictions.in(
							AuftragFac.FLR_AUFTRAG_AUFTRAGART_C_NR, cArt));
				} else if (iArt == 2) {
					cArt = new LinkedList<String>();
					cArt.add(AuftragServiceFac.AUFTRAGART_RAHMEN);
					critAuftrag.add(Restrictions.in(
							AuftragFac.FLR_AUFTRAG_AUFTRAGART_C_NR, cArt));
				}
			}
			// Einschraenkung nach einem bestimmten Kunden
			if (krit.kundeIId != null) {
				critAuftrag.add(Restrictions.eq(
						AuftragFac.FLR_AUFTRAG_KUNDE_I_ID_AUFTRAGSADRESSE,
						krit.kundeIId));
			}
			// Sortierung nur nach Termin
			boolean bOrderAuftragSchonHinzugefuegt = false;
			if (bSortierungNurLiefertermin) {
				critPosition
						.addOrder(Order
								.asc(AuftragpositionFac.FLR_AUFTRAGPOSITION_T_UEBERSTEUERTERLIEFERTERMIN));
				if (fertigungsgruppeIId != null
						&& fertigungsgruppeIId.length > 0) {
					critStkl = critPosition.createCriteria("flrstueckliste");
					critStkl.add(Restrictions.in("flrfertigungsgruppe.i_id",
							fertigungsgruppeIId));
				}
			} else {
				// Sortierung nach Kunde, eventuell innerhalb der Kostenstelle
				if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
					critAuftrag
							.createCriteria(AuftragFac.FLR_AUFTRAG_FLRKUNDE)
							.createCriteria(KundeFac.FLR_PARTNER)
							.addOrder(
									Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
				}
				// Sortierung nach Liefertermin (optional)
				if (bSortierungNachLiefertermin != null
						&& bSortierungNachLiefertermin.booleanValue()) {
					critPosition
							.addOrder(Order
									.asc(AuftragpositionFac.FLR_AUFTRAGPOSITION_T_UEBERSTEUERTERLIEFERTERMIN));
				}
				critAuftrag.addOrder(Order.asc(AuftragFac.FLR_AUFTRAG_C_NR));
				bOrderAuftragSchonHinzugefuegt = true;
				if (fertigungsgruppeIId != null
						&& fertigungsgruppeIId.length > 0) {
					critStkl = critPosition.createCriteria("flrstueckliste");
					critStkl.add(Restrictions.in("flrfertigungsgruppe.i_id",
							fertigungsgruppeIId));
				}
			}

			// PJ 15819
			/*
			 * // Sortierung nach iSort innerhalb der Positionen
			 * critPosition.addOrder(Order
			 * .asc(AuftragpositionFac.FLR_AUFTRAGPOSITION_I_SORT));
			 */
			if (bOrderAuftragSchonHinzugefuegt == false) {
				critAuftrag.addOrder(Order.asc(AuftragFac.FLR_AUFTRAG_C_NR));
			}

			List<?> lPositionen = critPosition.list();
			Iterator<?> itPositionen = lPositionen.iterator();

			ArrayList alDaten = new ArrayList();

			KundeDto kundeAuftragDto = null;
			KundeDto kundeLieferAdresseDto = null;

			boolean darfVerkaufspreisSehen = getTheJudgeFac().hatRecht(
					RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF, theClientDto);

			HashMap<Integer, KundeDto> hmKundeCache = new HashMap<Integer, KundeDto>();
			while (itPositionen.hasNext()) {
				FLRAuftragpositionOP flrauftragposition = (FLRAuftragpositionOP) itPositionen
						.next();
				FLRAuftragOD flrauftrag = flrauftragposition.getFlrauftrag();

				if (flrauftrag.getAuftragart_c_nr().equals(
						AuftragServiceFac.AUFTRAGART_RAHMEN)) {
					if (flrauftragposition.getN_offenerahmenmenge()
							.doubleValue() <= 0) {
						continue;
					}

				} else {
					BigDecimal bdOffeneMenge = flrauftragposition
							.getN_offenemenge();
					if (bdOffeneMenge == null) {
						bdOffeneMenge = flrauftragposition.getN_menge();
					}
					if (bdOffeneMenge.doubleValue() <= 0) {
						continue;
					}
				}

				Object[] zeile = new Object[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ANZAHL_SPALTEN];

				// Kunde aus dem Cache holen
				kundeAuftragDto = (KundeDto) hmKundeCache.get(flrauftrag
						.getKunde_i_id_auftragsadresse());
				// wenn er noch nicht im cache war, dann aus der db holen und
				// cachen
				if (kundeAuftragDto == null) {
					kundeAuftragDto = getKundeFac().kundeFindByPrimaryKey(
							flrauftrag.getKunde_i_id_auftragsadresse(),
							theClientDto);
					hmKundeCache.put(
							flrauftrag.getKunde_i_id_auftragsadresse(),
							kundeAuftragDto);
				}
				kundeLieferAdresseDto = (KundeDto) hmKundeCache.get(flrauftrag
						.getKunde_i_id_lieferadresse());
				// wenn er noch nicht im cache war, dann aus der db holen und
				// cachen
				if (kundeLieferAdresseDto == null) {
					kundeLieferAdresseDto = getKundeFac()
							.kundeFindByPrimaryKey(
									flrauftrag.getKunde_i_id_lieferadresse(),
									theClientDto);
					hmKundeCache.put(flrauftrag.getKunde_i_id_lieferadresse(),
							kundeLieferAdresseDto);
				}

				String sAuftragart = null;
				if (flrauftrag.getAuftragart_c_nr().equals(
						AuftragServiceFac.AUFTRAGART_RAHMEN)) {
					sAuftragart = new String(
							AuftragServiceFac.AUFTRAGART_RAHMEN_SHORT);
				} else if (flrauftrag.getAuftragart_c_nr().equals(
						AuftragServiceFac.AUFTRAGART_ABRUF)) {
					sAuftragart = new String(
							AuftragServiceFac.AUFTRAGART_ABRUF_SHORT);
				} else {
					sAuftragart = flrauftrag.getAuftragart_c_nr();
				}
				StringBuffer buffLos = new StringBuffer("");

				try {
					session = factory.openSession();

					// FLRAuftragseriennrn flrauftragsrn = (FLRAuftragseriennrn)

					org.hibernate.Criteria c = session
							.createCriteria(FLRAuftragseriennrn.class)
							.setFetchMode("permissions", FetchMode.JOIN)
							.add(Restrictions.and(Restrictions.eq(
									"auftragposition_i_id",
									flrauftragposition.getI_id()), Restrictions
									.eq("artikel_i_id", flrauftragposition
											.getArtikel_i_id())));

					List<?> list = c.list();
					Iterator<?> it = list.iterator();

					String s = "";

					while (it.hasNext()) {
						FLRAuftragseriennrn flrauftragsrn = (FLRAuftragseriennrn) it
								.next();
						s += flrauftragsrn.getC_seriennr() + ",";
					}

					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELSNR] = s;
				} finally {
					try {
						session.close();
					} catch (HibernateException he) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_HIBERNATE, he);
					}
				}

				zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGARTCNR] = sAuftragart;
				zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGKUNDE] = kundeAuftragDto
						.getPartnerDto().getCName1nachnamefirmazeile1();
				if (kundeAuftragDto.getPartnerDto()
						.getCName2vornamefirmazeile2() != null) {
					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGKUNDE2] = kundeAuftragDto
							.getPartnerDto().getCName2vornamefirmazeile2();
				}
				zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_KUNDEAUFTRAGADRESSE] = kundeAuftragDto
						.getPartnerDto().getCKbez()
						+ " "
						+ kundeAuftragDto.getPartnerDto().formatAdresse();
				zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_KUNDELIEFERADRESSE] = kundeLieferAdresseDto
						.getPartnerDto().getCKbez()
						+ " "
						+ kundeLieferAdresseDto.getPartnerDto().formatAdresse();
				zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_LOSNUMMER] = buffLos
						.toString();

				// PJ 16713
				LosDto[] losDtos = getFertigungFac().losFindByAuftragIId(
						flrauftrag.getI_id());
				boolean bNurMateriallisten = true;
				boolean bErstlos = false;
				for (int k = 0; k < losDtos.length; k++) {

					LosDto losDto = losDtos[k];
					if (!losDto.getStatusCNr().equals(
							LocaleFac.STATUS_STORNIERT)) {
						if (losDto.getStuecklisteIId() != null) {
							bNurMateriallisten = false;
						}
						if (getFertigungReportFac().istErstlos(losDto,
								theClientDto) == true) {
							bErstlos = true;
						}
					}
				}
				if (losDtos.length > 0) {
					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ERSTLOS] = new Boolean(
							bErstlos);

					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_NUR_MATERIALLISTEN] = new Boolean(
							bNurMateriallisten);
				}
				/*
				 * if(bSortierungNachAbliefertermin == true &&
				 * bSortierungNachLiefertermin != true){ zeile[AuftragReportFac.
				 * REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGLIEFERTERMIN] =
				 * Helper .formatDatum(new
				 * Date(flrauftrag.getT_liefertermin().getYear(),
				 * flrauftrag.getT_liefertermin().getMonth(),
				 * flrauftrag.getT_liefertermin().getDate() -
				 * kundeLieferAdresseDto.getILieferdauer()),
				 * theClientDto.getLocUi()); zeile[AuftragReportFac.
				 * REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGFINALTERMIN] = Helper
				 * .formatDatum(new
				 * Date(flrauftrag.getT_finaltermin().getYear(),
				 * flrauftrag.getT_finaltermin().getMonth(),
				 * flrauftrag.getT_finaltermin().getDate() -
				 * kundeLieferAdresseDto.getILieferdauer()),
				 * theClientDto.getLocUi());
				 */
				// } else {
				zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGLIEFERTERMIN] = Helper
						.formatDatum(flrauftrag.getT_liefertermin(),
								theClientDto.getLocUi());
				zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGFINALTERMIN] = Helper
						.formatDatum(flrauftrag.getT_finaltermin(),
								theClientDto.getLocUi());
				// }

				zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGZAHLUNGSZIEL] = "";
				zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGPROJEKTBEZEICHNUNG] = flrauftrag
						.getC_bez();
				zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGBESTELLNUMMER] = flrauftrag
						.getC_bestellnummer();

				zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ZEIT_VERRECHENBAR] = flrauftrag
						.getT_verrechenbar();
				if (flrauftrag.getFlrpersonalverrechenbar() != null) {
					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_PERSON_VERRECHENBAR] = HelperServer
							.formatNameAusFLRPartner(flrauftrag
									.getFlrpersonalverrechenbar()
									.getFlrpartner());
				}

				if (darfVerkaufspreisSehen) {
					// Positionspreise sind in Belegwaehrung abgelegt
					BigDecimal nPreisInBelegwaehrung = flrauftragposition
							.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte();

					nPreisInBelegwaehrung = getBetragMalWechselkurs(
							nPreisInBelegwaehrung,
							Helper.getKehrwert(new BigDecimal(
									flrauftrag
											.getF_wechselkursmandantwaehrungzuauftragswaehrung()
											.doubleValue())));
					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELNETTOGESAMTPREISPLUSVERSTECKTERAUFSCHLAGMINUSRABATTE] = nPreisInBelegwaehrung;
				} else {
					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELNETTOGESAMTPREISPLUSVERSTECKTERAUFSCHLAGMINUSRABATTE] = null;

				}

				String sPonale = new String("0");
				if (flrauftrag.getB_poenale().equals(new Short((short) 1))) {
					sPonale = "1";
				}
				zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGPOENALE] = sPonale;

				zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGROHS] = Helper
						.short2Boolean(flrauftrag.getB_rohs());

				// AUFTRAGSEIGENSCHAFTEN
				Hashtable<?, ?> hAE = getAuftragEigenschaften(
						flrauftrag.getI_id(), theClientDto);
				if (hAE != null) {
					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGEIGENSCHAFT_FA] = hAE
							.get(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_FA);
					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGEIGENSCHAFT_CLUSTER] = hAE
							.get(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_CLUSTER);
					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGEIGENSCHAFT_EQNR] = hAE
							.get(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_EQNR);
				}
				// nur mengenbehaftete Positionen beruecksichtigen
				// if (flrauftragposition.getN_menge() != null) {
				zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELCNR] = flrauftragposition
						.getFlrartikel().getC_nr();
				// Fertigungsgruppe von Stueckliste des Artikels, wenn
				// vorhanden.
				StuecklisteDto stklDto = getStuecklisteFac()
						.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
								flrauftragposition.getFlrartikel().getI_id(),
								theClientDto);
				if (stklDto != null) {
					FertigungsgruppeDto fertigungsgruppeDto = getStuecklisteFac()
							.fertigungsgruppeFindByPrimaryKey(
									stklDto.getFertigungsgruppeIId());
					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_STKL_FERTIGUNGSGRUPPE] = fertigungsgruppeDto
							.getCBez();
				}

				zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGPROJEKTBEZEICHNUNG] = flrauftrag
						.getC_bez();
				zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGCNR] = flrauftrag
						.getC_nr();
				String cArtikelBezeichnung = "";
				if (flrauftragposition.getAuftragpositionart_c_nr().equals(
						AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)
						|| flrauftragposition
								.getAuftragpositionart_c_nr()
								.equals(AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE)) {
					cArtikelBezeichnung = getArtikelFac()
							.baueArtikelBezeichnungMehrzeiligOhneExc(
									flrauftragposition.getArtikel_i_id(),
									flrauftragposition
											.getAuftragpositionart_c_nr(),
									flrauftragposition.getC_bez(),
									flrauftragposition.getC_zbez(), false,
									null, theClientDto);

					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(
									flrauftragposition.getArtikel_i_id(),
									theClientDto);
					// Fiktiver Lagerstand anhand Bewegugnsvorschau
					BigDecimal bdFiktiverLagerstand = getInternebestellungFac()
							.getFiktivenLagerstandZuZeitpunkt(artikelDto,
									theClientDto,
									new Timestamp(dStichtagBzwBis.getTime()));

					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_FIKTIVERLAGERSTAND] = bdFiktiverLagerstand;

					// Lagerstand
					BigDecimal bdLagerstand = getLagerFac()
							.getLagerstandAllerLagerEinesMandanten(
									flrauftragposition.getArtikel_i_id(),
									false, theClientDto);

					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_LAGERSTAND] = bdLagerstand;

					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELLAGERSTAND] = bdLagerstand;
					// Offene Bestellmenge
					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELBESTELLTMENGE] = getArtikelbestelltFac()
							.getAnzahlBestellt(
									flrauftragposition.getArtikel_i_id());
				}

				zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELBEZEICHNUNG] = cArtikelBezeichnung;
				zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELMENGE] = flrauftragposition
						.getN_menge();

				if (flrauftragposition.getT_uebersteuerterliefertermin() != null) {
					if (bSortierungNachAbliefertermin == true
							&& bSortierungNachLiefertermin != true) {
						zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_POSITIONSTERMIN] = new Date(
								flrauftragposition
										.getT_uebersteuerterliefertermin()
										.getYear(), flrauftragposition
										.getT_uebersteuerterliefertermin()
										.getMonth(),
								flrauftragposition
										.getT_uebersteuerterliefertermin()
										.getDate()
										- kundeLieferAdresseDto
												.getILieferdauer());
					} else {
						zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_POSITIONSTERMIN] = flrauftragposition
								.getT_uebersteuerterliefertermin();
					}

				}

				zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_POSITIONSSTATUS] = flrauftragposition
						.getAuftragpositionstatus_c_nr();

				zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELEINHEIT] = flrauftragposition
						.getEinheit_c_nr() == null ? null : flrauftragposition
						.getEinheit_c_nr().trim();

				// Grundlage ist der Gestehungspreis des Artikels am Hauptlager
				// des Mandanten
				BigDecimal bdGestehungspreis = getLagerFac()
						.getGemittelterGestehungspreisDesHauptlagers(
								flrauftragposition.getArtikel_i_id(),
								theClientDto);
				BigDecimal bdGelieferteMenge = flrauftragposition.getN_menge();

				if (darfVerkaufspreisSehen) {
					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELGESTEHUNGSPREIS] = bdGestehungspreis;
				} else {
					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELGESTEHUNGSPREIS] = null;
				}
				if (flrauftrag.getAuftragart_c_nr().equals(
						AuftragServiceFac.AUFTRAGART_RAHMEN)) {
					bdGelieferteMenge = flrauftragposition
							.getN_menge()
							.subtract(
									flrauftragposition.getN_offenerahmenmenge());
					zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_OFFENERAHMENMENGE] = flrauftragposition
							.getN_offenerahmenmenge();
				} else {

					BigDecimal bdOffeneMenge = flrauftragposition
							.getN_offenemenge();
					if (bdOffeneMenge == null) {
						bdOffeneMenge = flrauftragposition.getN_menge();
					}

					bdGelieferteMenge = flrauftragposition.getN_menge()
							.subtract(bdOffeneMenge);
				}

				zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELGELIEFERTEMENGE] = bdGelieferteMenge;
				zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELOFFENEMENGE] = flrauftragposition
						.getN_offenemenge();

				// die Positionen brauchen alle Attribute, nach denen im Report
				// gruppiert wird
				zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGCNR] = flrauftrag
						.getC_nr();
				zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGARTCNR] = sAuftragart;
				zeile[AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGKUNDE] = kundeAuftragDto
						.getPartnerDto().getCName1nachnamefirmazeile1();
				alDaten.add(zeile);

			}

			data = new Object[alDaten.size()][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ANZAHL_SPALTEN];
			data = (Object[][]) alDaten.toArray(data);

			// Die fiktiven Artikellagerstaende berechnen
			for (int x = 0; x < data.length; x++) {
				for (int y = 0; y < data.length; y++) {
					if (data[x][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELCNR]
							.equals(data[y][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELCNR])
							&& (x != y)) {
						Date dHelper = (Date) data[x][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_POSITIONSTERMIN];
						Date dTemp = (Date) data[y][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_POSITIONSTERMIN];
						if (!dHelper.before(dTemp)) {
							if (dHelper.equals(dTemp)) {
								String sAuftragnummerx = (String) data[x][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGCNR];
								String sAuftragnummery = (String) data[y][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGCNR];
								if (!sAuftragnummerx.equals(sAuftragnummery)) {
									BigDecimal bdHelper = (BigDecimal) data[x][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELLAGERSTAND];
									if (data[y][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGARTCNR] != null) {
										if (!data[y][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGARTCNR]
												.equals(AuftragServiceFac.AUFTRAGART_RAHMEN)) {
											data[x][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELLAGERSTAND] = bdHelper
													.subtract((BigDecimal) data[y][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELOFFENEMENGE]);
										}
									}
								}
							} else {
								BigDecimal bdHelper = (BigDecimal) data[x][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELLAGERSTAND];
								if (data[y][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGARTCNR] != null) {
									if (!data[y][AuftragReportFac.REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGARTCNR]
											.equals(AuftragServiceFac.AUFTRAGART_RAHMEN)) {
										data[x][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELLAGERSTAND] = bdHelper
												.subtract((BigDecimal) data[y][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELOFFENEMENGE]);
									}
								} else {
									data[x][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELLAGERSTAND] = bdHelper
											.subtract((BigDecimal) data[y][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELOFFENEMENGE]);

								}
							}
						}
					}
				}
				// SK: Die eigene offene wird nur von den anderen Artikeln
				// beruecksichtigt
				/*
				 * BigDecimal bdHelper2 = (BigDecimal) data[x][AuftragReportFac.
				 * REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELLAGERSTAND];
				 * data[x][AuftragReportFac
				 * .REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELLAGERSTAND] =
				 * bdHelper2.subtract( (BigDecimal) data[x][AuftragReportFac.
				 * REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELOFFENEMENGE]);
				 */
			}
			// Daten werden als Datum uebergeben und nicht als String
			for (int z = 0; z < data.length; z++) {
				java.sql.Date dateHelper = (java.sql.Date) data[z][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_POSITIONSTERMIN];
				data[z][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_POSITIONSTERMIN] = new java.util.Date(
						dateHelper.getTime());
			}
			// die Parameter dem Report uebergeben
			HashMap<String, Object> parameter = new HashMap<String, Object>();
			parameter.put(
					LPReport.P_SORTIERUNG,
					buildSortierungAuftragOffenePositionen(krit,
							bSortierungNachLiefertermin,
							bSortierungNachAbliefertermin, bOhnePositionen,
							fertigungsgruppeIId, iArt, theClientDto));

			parameter
					.put(LPReport.P_SORTIERENACHKUNDE,
							new Boolean(
									krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER));
			parameter.put("P_SORTIERENACHFERTIGUNGSGRUPPE", new Boolean(
					fertigungsgruppeIId != null));
			parameter
					.put("P_SORTIERENACHAUFTRAGART",
							new Boolean(
									krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_ART));
			parameter.put("P_AUFTRAGWAEHRUNG",
					theClientDto.getSMandantenwaehrung());
			if (dVon != null) {
				parameter.put("P_VON", dVon);
			}
			parameter.put("P_STICHTAG", Helper.formatDatum(dStichtagBzwBis,
					theClientDto.getLocUi()));
			parameter.put("P_BIS", dStichtagBzwBis);

			String sReportToUse = AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN;
			if (sReportname != null) {
				sReportToUse = sReportname;
			}

			initJRDS(parameter, AuftragReportFac.REPORT_MODUL, sReportToUse,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);

			oPrintO = getReportPrint();
		} catch (RemoteException re) {
			throwEJBExceptionLPRespectOld(re);
		}
		return oPrintO;
	}

	/**
	 * Diese Methode liefert eine Liste von allen offenen Auftraegen eines
	 * Mandanten, die nach den eingegebenen Kriterien des Benutzers
	 * zusammengestellt wird. <br>
	 * Achtung: Hibernate verwendet lazy initialization, d.h. der Zugriff auf
	 * Collections muss innerhalb der Session erfolgen.
	 * 
	 * @param reportJournalKriterienDtoI
	 *            die Kriterien des Benutzers
	 * @param dStichtag
	 *            Date
	 * @param bSortierungNachLiefertermin
	 *            Boolean
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return ReportAuftragOffeneDto[] die Liste der Auftraege
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private ReportAuftragOffeneDto[] getListeReportAuftragTeilLieferbar(
			ReportJournalKriterienDto reportJournalKriterienDtoI,
			Date dStichtag, Boolean bSortierungNachLiefertermin,
			TheClientDto theClientDto) throws EJBExceptionLP {

		ReportAuftragOffeneDto[] aResult = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		// vom Stichtag die Uhrzeit abschneiden
		dStichtag = Helper.cutDate(dStichtag);

		try {
			session = factory.openSession();

			// Hiberante Criteria fuer alle Tabellen ausgehend von meiner
			// Haupttabelle anlegen,
			// nach denen ich filtern und sortieren kann
			Criteria crit = session.createCriteria(FLRAuftragReport.class);

			// Einschraenkung auf den aktuellen Mandanten
			crit.add(Restrictions.eq(AuftragFac.FLR_AUFTRAG_MANDANT_C_NR,
					theClientDto.getMandant()));

			// Einschraenkung nach Auftragart
			crit.add(Restrictions.ne(AuftragFac.FLR_AUFTRAG_AUFTRAGART_C_NR,
					AuftragServiceFac.AUFTRAGART_RAHMEN));

			crit.add(Restrictions.eq(
					AuftragFac.FLR_AUFTRAG_B_TEILLIEFERUNGMOEGLICH, "1"));

			// Einschraenkung nach Status Offen, Teilerledigt, Erledigt
			Collection<String> cStati = new LinkedList<String>();
			cStati.add(AuftragServiceFac.AUFTRAGSTATUS_OFFEN);
			cStati.add(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT);
			cStati.add(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT);
			crit.add(Restrictions.in(AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR,
					cStati));

			// Das Belegdatum muss vor dem Stichtag liegen
			crit.add(Restrictions.le(AuftragFac.FLR_AUFTRAG_D_BELEGDATUM,
					dStichtag));

			// Der Auftrag darf zum Stichtag noch nicht erledigt worden sein
			crit.add(Restrictions.or(Restrictions
					.isNull(AuftragFac.FLR_AUFTRAG_T_ERLEDIGT), Restrictions
					.gt(AuftragFac.FLR_AUFTRAG_T_ERLEDIGT, dStichtag)));

			// Einschraenkung nach einer bestimmten Kostenstelle
			if (reportJournalKriterienDtoI.kostenstelleIId != null) {
				crit.add(Restrictions.eq(
						AuftragFac.FLR_AUFTRAG_KOSTENSTELLE_I_ID,
						reportJournalKriterienDtoI.kostenstelleIId));
			}

			// Einschraenkung nach einem bestimmten Kunden
			if (reportJournalKriterienDtoI.kundeIId != null) {
				crit.add(Restrictions.eq(
						AuftragFac.FLR_AUFTRAG_KUNDE_I_ID_AUFTRAGSADRESSE,
						reportJournalKriterienDtoI.kundeIId));
			}

			// Sortierung nach Kostenstelle ist immer die erste Sortierung
			if (reportJournalKriterienDtoI.bSortiereNachKostenstelle) {
				crit.createCriteria(AuftragFac.FLR_AUFTRAG_FLRKOSTENSTELLE)
						.addOrder(Order.asc(AuftragFac.FLR_AUFTRAG_C_NR));
			}

			// Sortierung nach Kunde, eventuell innerhalb der Kostenstelle
			if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				crit.createCriteria(AuftragFac.FLR_AUFTRAG_FLRKUNDE)
						.createCriteria(KundeFac.FLR_PARTNER)
						.addOrder(
								Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
			}

			// Sortierung nach Liefertermin (optional)
			if (bSortierungNachLiefertermin != null
					&& bSortierungNachLiefertermin.booleanValue()) {
				crit.addOrder(Order.asc(AuftragFac.FLR_AUFTRAG_T_LIEFERTERMIN));
			}

			// es wird in jedem Fall nach der Belegnummer sortiert
			crit.addOrder(Order.asc(AuftragFac.FLR_AUFTRAG_C_NR));

			List<?> list = crit.list();
			aResult = new ReportAuftragOffeneDto[list.size()];
			int iIndex = 0;
			Iterator<?> it = list.iterator();
			ReportAuftragOffeneDto reportDto = null;

			while (it.hasNext()) {
				FLRAuftragReport flrauftrag = (FLRAuftragReport) it.next();

				reportDto = new ReportAuftragOffeneDto();
				reportDto.setIIdAuftrag(flrauftrag.getI_id());
				reportDto.setCNrAuftrag(flrauftrag.getC_nr());
				reportDto.setKundeCName1(flrauftrag.getFlrkunde()
						.getFlrpartner().getC_name1nachnamefirmazeile1());
				reportDto.setKostenstelleCNr(flrauftrag.getFlrkostenstelle()
						.getC_nr());

				aResult[iIndex] = reportDto;
				iIndex++;
			}
		} finally {
			try {
				if (session != null)
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
	 * @param reportJournalKriterienDtoI
	 *            die Kriterien
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return String die Filterkriterien
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private String buildFilterAuftragOffene(
			ReportJournalKriterienDto reportJournalKriterienDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		StringBuffer buff = new StringBuffer("");

		// Belegdatum
		if (reportJournalKriterienDtoI.dVon != null
				|| reportJournalKriterienDtoI.dBis != null) {
			buff.append(getTextRespectUISpr("bes.belegdatum",
					theClientDto.getMandant(), theClientDto.getLocUi()));
		}

		if (reportJournalKriterienDtoI.dVon != null) {
			buff.append(" ").append(
					getTextRespectUISpr("lp.von", theClientDto.getMandant(),
							theClientDto.getLocUi()));
			buff.append(" ").append(
					Helper.formatDatum(reportJournalKriterienDtoI.dVon,
							theClientDto.getLocUi()));
		}

		if (reportJournalKriterienDtoI.dBis != null) {
			buff.append(" ").append(
					getTextRespectUISpr("lp.bis", theClientDto.getMandant(),
							theClientDto.getLocUi()));
			buff.append(" ").append(
					Helper.formatDatum(reportJournalKriterienDtoI.dBis,
							theClientDto.getLocUi()));
		}

		// Kostenstelle
		if (reportJournalKriterienDtoI.kostenstelleIId != null) {
			buff.append(" ")
					.append(getTextRespectUISpr("lp.kostenstelle",
							theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ").append(
					getSystemFac().kostenstelleFindByPrimaryKey(
							reportJournalKriterienDtoI.kostenstelleIId)
							.getCNr());
		}

		// Kunde
		if (reportJournalKriterienDtoI.kundeIId != null) {
			buff.append(" ").append(
					getTextRespectUISpr("lp.kunde", theClientDto.getMandant(),
							theClientDto.getLocUi()));
			buff.append(" ").append(
					getKundeFac()
							.kundeFindByPrimaryKey(
									reportJournalKriterienDtoI.kundeIId,
									theClientDto).getPartnerDto()
							.getCName1nachnamefirmazeile1());
		}
		// Vertreter
		if (reportJournalKriterienDtoI.vertreterIId != null) {
			buff.append(" ")
					.append(getTextRespectUISpr("lp.vertreter",
							theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ").append(
					getPersonalFac()
							.personalFindByPrimaryKey(
									reportJournalKriterienDtoI.vertreterIId,
									theClientDto).getPartnerDto()
							.getCName1nachnamefirmazeile1());
		}
		// Auftragsnummer
		if (reportJournalKriterienDtoI.sBelegnummerVon != null
				|| reportJournalKriterienDtoI.sBelegnummerBis != null) {
			buff.append(" ")
					.append(getTextRespectUISpr("auft.auftragsnummer",
							theClientDto.getMandant(), theClientDto.getLocUi()));
		}

		if (reportJournalKriterienDtoI.sBelegnummerVon != null) {
			buff.append(" ").append(
					getTextRespectUISpr("lp.von", theClientDto.getMandant(),
							theClientDto.getLocUi()));
			buff.append(" ").append(reportJournalKriterienDtoI.sBelegnummerVon);
		}

		if (reportJournalKriterienDtoI.sBelegnummerBis != null) {
			buff.append(" ").append(
					getTextRespectUISpr("lp.bis", theClientDto.getMandant(),
							theClientDto.getLocUi()));
			buff.append(" ").append(reportJournalKriterienDtoI.sBelegnummerBis);
		}

		String cBuffer = buff.toString().trim();

		return cBuffer;
	}

	/**
	 * String zum Andrucken der Filterkriterien.
	 * 
	 * @param reportJournalKriterienDtoI
	 *            die Kriterien
	 * @param artikelklasseIId
	 *            Integer
	 * @param artikelgruppeIId
	 *            Integer
	 * @param artikelCNrVon
	 *            String
	 * @param artikelCNrBis
	 *            String
	 * @param projektCBezeichnung
	 *            String
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return String die Filterkriterien
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private String buildFilterAuftragOffeneDetails(
			ReportJournalKriterienDto reportJournalKriterienDtoI,
			Integer artikelklasseIId, Integer artikelgruppeIId,
			String artikelCNrVon, String artikelCNrBis,
			String projektCBezeichnung, TheClientDto theClientDto)
			throws EJBExceptionLP {
		StringBuffer buff = new StringBuffer("");

		try {
			// Belegdatum
			if (reportJournalKriterienDtoI.dVon != null
					|| reportJournalKriterienDtoI.dBis != null) {
				buff.append(getTextRespectUISpr("bes.belegdatum",
						theClientDto.getMandant(), theClientDto.getLocUi()));
			}

			if (reportJournalKriterienDtoI.dVon != null) {
				buff.append(" ").append(
						getTextRespectUISpr("lp.von",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
				buff.append(" ").append(
						Helper.formatDatum(reportJournalKriterienDtoI.dVon,
								theClientDto.getLocUi()));
			}

			if (reportJournalKriterienDtoI.dBis != null) {
				buff.append(" ").append(
						getTextRespectUISpr("lp.bis",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
				buff.append(" ").append(
						Helper.formatDatum(reportJournalKriterienDtoI.dBis,
								theClientDto.getLocUi()));
			}

			// Kostenstelle
			if (reportJournalKriterienDtoI.kostenstelleIId != null) {
				buff.append(" ").append(
						getTextRespectUISpr("lp.kostenstelle",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
				buff.append(" ").append(
						getSystemFac().kostenstelleFindByPrimaryKey(
								reportJournalKriterienDtoI.kostenstelleIId)
								.getCNr());
			}

			// Kunde
			if (reportJournalKriterienDtoI.kundeIId != null) {
				buff.append(" ").append(
						getTextRespectUISpr("lp.kunde",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
				buff.append(" ").append(
						getKundeFac()
								.kundeFindByPrimaryKey(
										reportJournalKriterienDtoI.kundeIId,
										theClientDto).getPartnerDto()
								.getCName1nachnamefirmazeile1());
			}
			// Vertreter
			if (reportJournalKriterienDtoI.vertreterIId != null) {
				buff.append(" ").append(
						getTextRespectUISpr("lp.vertreter",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
				buff.append(" ")
						.append(getPersonalFac()
								.personalFindByPrimaryKey(
										reportJournalKriterienDtoI.vertreterIId,
										theClientDto).getPartnerDto()
								.getCName1nachnamefirmazeile1());
			}
			// Auftragsnummer
			if (reportJournalKriterienDtoI.sBelegnummerVon != null
					|| reportJournalKriterienDtoI.sBelegnummerBis != null) {
				buff.append(" ").append(
						getTextRespectUISpr("auft.auftragsnummer",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			}

			if (reportJournalKriterienDtoI.sBelegnummerVon != null) {
				buff.append(" ").append(
						getTextRespectUISpr("lp.von",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
				buff.append(" ").append(
						reportJournalKriterienDtoI.sBelegnummerVon);
			}

			if (reportJournalKriterienDtoI.sBelegnummerBis != null) {
				buff.append(" ").append(
						getTextRespectUISpr("lp.bis",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
				buff.append(" ").append(
						reportJournalKriterienDtoI.sBelegnummerBis);
			}

			if (projektCBezeichnung != null) {
				buff.append(" ").append(
						getTextRespectUISpr("lp.projektbezeichnung",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
				buff.append(" ").append(projektCBezeichnung);
			}

			if (artikelklasseIId != null) {
				buff.append(" ").append(
						getTextRespectUISpr("lp.artikelklasse",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
				ArtklaDto artikelklasseDto = getArtikelFac()
						.artklaFindByPrimaryKey(artikelklasseIId, theClientDto);
				if (artikelklasseDto.getArtklasprDto() != null) {
					buff.append(" ").append(
							artikelklasseDto.getArtklasprDto().getCBez());
				}
			}

			if (artikelgruppeIId != null) {
				buff.append(" ").append(
						getTextRespectUISpr("lp.artikelgruppe",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
				ArtgruDto artikelgruppeDto = getArtikelFac()
						.artgruFindByPrimaryKey(artikelgruppeIId, theClientDto);
				if (artikelgruppeDto.getArtgrusprDto() != null) {
					buff.append(" ").append(
							artikelgruppeDto.getArtgrusprDto().getCBez());
				}
			}
		} catch (RemoteException re) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, re);
		}

		String cBuffer = buff.toString().trim();

		return cBuffer;
	}

	private String buildZeileIdentPosition(Integer auftragpositionIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Session session = null;
		List<?> l = null;
		StringBuffer sbArtikelInfo = new StringBuffer();
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria crit = session.createCriteria(FLRAuftragposition.class);
			crit.add(Restrictions.eq(
					AuftragpositionFac.FLR_AUFTRAGPOSITIONART_POSITION_I_ID,
					auftragpositionIId));
			crit.addOrder(Order
					.asc(AuftragpositionFac.FLR_AUFTRAGPOSITION_I_SORT));
			l = crit.list();
			Iterator<?> iter = l.iterator();
			while (iter.hasNext()) {
				FLRAuftragposition pos = (FLRAuftragposition) iter.next();
				if (pos.getPositionart_c_nr().equals(
						LocaleFac.POSITIONSART_IDENT)) {
					sbArtikelInfo.append(pos.getFlrartikel().getC_nr());
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
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return sbArtikelInfo.toString();
	}

	private String buildZeileBezPosition(Integer auftragpositionIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Session session = null;
		List<?> l = null;
		StringBuffer sbArtikelInfo = new StringBuffer();
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria crit = session.createCriteria(FLRAuftragposition.class);
			crit.add(Restrictions.eq(
					AuftragpositionFac.FLR_AUFTRAGPOSITIONART_POSITION_I_ID,
					auftragpositionIId));
			crit.addOrder(Order
					.asc(AuftragpositionFac.FLR_AUFTRAGPOSITION_I_SORT));
			l = crit.list();
			Iterator<?> iter = l.iterator();
			while (iter.hasNext()) {
				FLRAuftragposition pos = (FLRAuftragposition) iter.next();
				if (pos.getPositionart_c_nr().equals(
						LocaleFac.POSITIONSART_IDENT)) {
					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(
									pos.getFlrartikel().getI_id(), theClientDto);

					if (artikelDto.getArtikelsprDto() != null) {
						if (artikelDto.getArtikelsprDto().getCBez() != null) {
							sbArtikelInfo.append(artikelDto.getArtikelsprDto()
									.getCBez());
						}
					}
					if (iter.hasNext()) {
						sbArtikelInfo.append("\n");
					}
				} else if (pos.getPositionart_c_nr().equals(
						LocaleFac.POSITIONSART_HANDEINGABE)) {
					if (pos.getC_bez() != null) {
						sbArtikelInfo.append(pos.getC_bez());
						if (iter.hasNext()) {
							sbArtikelInfo.append("\n");
						}
					}
				}
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return sbArtikelInfo.toString();
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
	private String buildSortierungAuftragOffene(
			ReportJournalKriterienDto reportJournalKriterienDtoI,
			TheClientDto theClientDto) {

		StringBuffer buff = new StringBuffer(getTextRespectUISpr(
				"lp.sortierungnach", theClientDto.getMandant(),
				theClientDto.getLocUi()));
		buff.append(" ");

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

		buff.append(getTextRespectUISpr("auft.auftragsnummer",
				theClientDto.getMandant(), theClientDto.getLocUi()));

		return buff.toString();
	}

	public Hashtable<String, String> getAuftragEigenschaften(
			Integer iIdAuftragI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		SessionFactory factory = FLRSessionFactory.getFactory();
		Hashtable<String, String> daten = null;
		Session session = null;
		try {
			session = factory.openSession();
			Criteria crit = session.createCriteria(FLRPaneldaten.class);
			crit.add(Restrictions.eq("c_key", iIdAuftragI.toString()));
			List<?> resultList = crit.list();
			if (resultList.size() != 0) {
				daten = new Hashtable<String, String>();
			} else {
				return null;
			}
			Iterator<?> it = resultList.iterator();
			while (it.hasNext()) {
				FLRPaneldaten flrpaneldaten = (FLRPaneldaten) it.next();
				FLRPanelbeschreibung flrpanelbeschreibung = (FLRPanelbeschreibung) flrpaneldaten
						.getFlrpanelbeschreibung();
				if (flrpanelbeschreibung.getC_name().equals("FA Nr")) {
					if (flrpaneldaten.getX_inhalt() != null) {
						daten.put(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_FA,
								flrpaneldaten.getX_inhalt());
					}
				}
				if (flrpanelbeschreibung.getC_name().equals("ClusterText")) {
					if (flrpaneldaten.getX_inhalt() != null) {
						daten.put(
								AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_CLUSTER,
								flrpaneldaten.getX_inhalt());
					}
				}
				if (flrpanelbeschreibung.getC_name().equals("Equipmentnr")) {
					if (flrpaneldaten.getX_inhalt() != null) {
						daten.put(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_EQNR,
								flrpaneldaten.getX_inhalt());
					}
				}
			}
		} finally {
			try {
				if (session != null)
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

	/**
	 * String zum Andrucken der Sortierkriterien.
	 * 
	 * @param reportJournalKriterienDtoI
	 *            die Kriterien
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return String die Sortierkriterien
	 */
	private String buildSortierungAuftragOffeneDetails(
			ReportJournalKriterienDto reportJournalKriterienDtoI,
			TheClientDto theClientDto) {

		StringBuffer buff = new StringBuffer(getTextRespectUISpr(
				"lp.sortierungnach", theClientDto.getMandant(),
				theClientDto.getLocUi()));
		buff.append(" ");

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
		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_IDENT) {
			buff.append(
					getTextRespectUISpr("lp.ident", theClientDto.getMandant(),
							theClientDto.getLocUi())).append(", ");
		}
		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_BELEGNUMMER) {
			buff.append(getTextRespectUISpr("auft.auftragsnummer",
					theClientDto.getMandant(), theClientDto.getLocUi()));
		}
		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PROJEKT) {
			buff.append(getTextRespectUISpr("lp.projekt",
					theClientDto.getMandant(), theClientDto.getLocUi()));
		}

		return buff.toString();
	}

	/**
	 * String zum Andrucken der Sortierkriterien.
	 * 
	 * @param reportJournalKriterienDtoI
	 *            die Kriterien
	 * @param dStichtag
	 *            Date
	 * @param bSortierungNachLiefertermin
	 *            Boolean
	 * @param bOhnePositionen
	 *            Boolean
	 * @param fertigungsgruppeIId
	 *            Integer
	 * @param iArt
	 *            Integer
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return String die Sortierkriterien
	 */
	private String buildSortierungAuftragOffenePositionen(
			ReportJournalKriterienDto reportJournalKriterienDtoI,
			Boolean bSortierungNachLiefertermin,
			Boolean bSortierungNachAbliefertermin, Boolean bOhnePositionen,
			Integer[] fertigungsgruppeIId, Integer iArt,
			TheClientDto theClientDto) {

		StringBuffer buff = new StringBuffer(getTextRespectUISpr(
				"lp.sortierungnach", theClientDto.getMandant(),
				theClientDto.getLocUi()));
		buff.append(" ");
		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
			buff.append(
					getTextRespectUISpr("lp.kunde", theClientDto.getMandant(),
							theClientDto.getLocUi())).append(", ");
		}
		if (bSortierungNachLiefertermin) {
			buff.append(
					getTextRespectUISpr("lp.liefertermin",
							theClientDto.getMandant(), theClientDto.getLocUi()))
					.append(", ");
		} else if (bSortierungNachAbliefertermin) {
			buff.append(
					getTextRespectUISpr("lp.abliefertermin",
							theClientDto.getMandant(), theClientDto.getLocUi()))
					.append(", ");
		}
		if (bOhnePositionen) {
			buff.append(
					getTextRespectUISpr("lp.ohnepositionen",
							theClientDto.getMandant(), theClientDto.getLocUi()))
					.append(", ");
		}
		if (fertigungsgruppeIId != null && fertigungsgruppeIId.length > 0) {
			buff.append(getTextRespectUISpr("lp.fertigungsgruppe",
					theClientDto.getMandant(), theClientDto.getLocUi()));

			for (int i = 0; i < fertigungsgruppeIId.length; i++) {
				try {
					FertigungsgruppeDto fertigungsgruppeDto = getStuecklisteFac()
							.fertigungsgruppeFindByPrimaryKey(
									fertigungsgruppeIId[i]);
					buff.append(" " + fertigungsgruppeDto.getCBez());
					buff.append(", ");
				} catch (RemoteException ex) {
					throwEJBExceptionLPRespectOld(ex);
				}
			}
		}
		if (iArt != null) {
			if (iArt == 1) {
				buff.append(getTextRespectUISpr("auft.ohnerahmenauftraege",
						theClientDto.getMandant(), theClientDto.getLocUi()));
			} else if (iArt == 2) {
				buff.append(getTextRespectUISpr("auft.nurrahmenauftraege",
						theClientDto.getMandant(), theClientDto.getLocUi()));
			} else {
				buff.append(getTextRespectUISpr("auft.alle",
						theClientDto.getMandant(), theClientDto.getLocUi()));
			}
		}
		return buff.toString();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAuftragsuebersicht(Integer iIdAuftragI,
			TheClientDto theClientDto) {

		cAktuellerReport = AuftragReportFac.REPORT_AUFTRAGSUEBERSICHT;

		data = getDataAuftragsuebersicht(iIdAuftragI, theClientDto);

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(
				iIdAuftragI);
		parameter.put("P_AUFTRAG", auftragDto.getCNr());
		parameter.put("P_PROJEKT", auftragDto.getCBezProjektbezeichnung());
		parameter.put("P_KOMMENTARINTERN", auftragDto.getXInternerkommentar());
		parameter.put("P_KOMMENTAREXTERN", auftragDto.getXExternerkommentar());

		parameter.put("P_LIEFERTERMIN", auftragDto.getDLiefertermin());

		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
				auftragDto.getKundeIIdAuftragsadresse(), theClientDto);
		parameter.put("P_KUNDE_NAME1", kundeDto.getPartnerDto()
				.getCName1nachnamefirmazeile1());
		parameter.put("P_KUNDE_NAME2", kundeDto.getPartnerDto()
				.getCName2vornamefirmazeile2());
		parameter.put("P_KUNDE_NAME3", kundeDto.getPartnerDto()
				.getCName3vorname2abteilung());
		parameter
				.put("P_KUNDE_STRASSE", kundeDto.getPartnerDto().getCStrasse());
		if (kundeDto.getPartnerDto().getLandplzortDto() != null) {
			parameter.put("P_KUNDE_ADRESSE", kundeDto.getPartnerDto()
					.getLandplzortDto().formatLandPlzOrt());
		}

		parameter.put("P_BELEGDATUM", auftragDto.getTBelegdatum());

		initJRDS(parameter, AuftragReportFac.REPORT_MODUL,
				AuftragReportFac.REPORT_AUFTRAGSUEBERSICHT,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	public Object[][] getDataAuftragsuebersicht(Integer iIdAuftragI,
			TheClientDto theClientDto) {
		AuftragzeitenDto[] dtos = null;

		dtos = getZeiterfassungFac().getAllZeitenEinesBeleges(
				LocaleFac.BELEGART_AUFTRAG, iIdAuftragI, null, null, null,
				null, false, false, theClientDto);

		ArrayList alDatenZeit = new ArrayList();

		for (int i = 0; i < dtos.length; i++) {

			Object[] oZeile = new Object[REPORT_AUFTRAGSUEBERSICHT_ANZAHL_SPALTEN];
			oZeile[AuftragReportFac.REPORT_AUFTRAGSUEBERSICHT_AZ_PERSON] = dtos[i]
					.getsPersonNachnameVorname();
			oZeile[AuftragReportFac.REPORT_AUFTRAGSUEBERSICHT_AZ_PERSON_KURZZEICHEN] = dtos[i]
					.getSPersonalKurzzeichen();
			oZeile[AuftragReportFac.REPORT_AUFTRAGSUEBERSICHT_ARTIKELNUMMER] = dtos[i]
					.getSArtikelcnr();
			oZeile[AuftragReportFac.REPORT_AUFTRAGSUEBERSICHT_BEZEICHNUNG] = dtos[i]
					.getSArtikelbezeichnung();
			oZeile[AuftragReportFac.REPORT_AUFTRAGSUEBERSICHT_ZUSATZBEZEICHNUNG] = dtos[i]
					.getSArtikelzusatzbezeichnung();
			oZeile[AuftragReportFac.REPORT_AUFTRAGSUEBERSICHT_AZ_VON] = dtos[i]
					.getTsBeginn();
			oZeile[AuftragReportFac.REPORT_AUFTRAGSUEBERSICHT_AZ_BIS] = dtos[i]
					.getTsEnde();
			oZeile[AuftragReportFac.REPORT_AUFTRAGSUEBERSICHT_AZ_DAUER] = dtos[i]
					.getDdDauer();
			oZeile[AuftragReportFac.REPORT_AUFTRAGSUEBERSICHT_ARBEITSZEIT] = Boolean.TRUE;

			alDatenZeit.add(oZeile);
		}

		Set hmLieferscheine = getAlleLieferscheineEinesAuftrags(iIdAuftragI);

		Set hmRechnungen = getAlleRechnungenEinesAuftrags(iIdAuftragI);

		TreeMap<String, Object[]> tmPositionen = new TreeMap();
		// LS-Pos hinzufuegen
		Iterator itLs = hmLieferscheine.iterator();
		while (itLs.hasNext()) {
			Integer lieferscheinIId = (Integer) itLs.next();

			LieferscheinDto lsDto = getLieferscheinFac()
					.lieferscheinFindByPrimaryKey(lieferscheinIId);

			try {
				Collection<LieferscheinpositionDto> cl = getLieferscheinpositionFac()
						.lieferscheinpositionFindByLieferscheinIId(
								lieferscheinIId, theClientDto);
				Iterator itLsPos = cl.iterator();

				while (itLsPos.hasNext()) {
					LieferscheinpositionDto lsposDto = (LieferscheinpositionDto) itLsPos
							.next();

					if (lsposDto.getArtikelIId() != null) {

						Object[] oZeile = new Object[REPORT_AUFTRAGSUEBERSICHT_ANZAHL_SPALTEN];

						ArtikelDto aDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(
										lsposDto.getArtikelIId(), theClientDto);

						oZeile[REPORT_AUFTRAGSUEBERSICHT_ARTIKELNUMMER] = aDto
								.getCNr();
						oZeile[REPORT_AUFTRAGSUEBERSICHT_ARTIKELNUMMER] = aDto
								.getCNr();
						if (aDto.getArtikelsprDto() != null) {
							oZeile[REPORT_AUFTRAGSUEBERSICHT_BEZEICHNUNG] = aDto
									.getArtikelsprDto().getCBez();
							oZeile[REPORT_AUFTRAGSUEBERSICHT_ZUSATZBEZEICHNUNG] = aDto
									.getArtikelsprDto().getCZbez();
						}

						if (lsposDto.getAuftragpositionIId() != null) {
							oZeile[REPORT_AUFTRAGSUEBERSICHT_NICHT_ZUGEORDNET] = Boolean.FALSE;

							oZeile[REPORT_AUFTRAGSUEBERSICHT_SOLLMENGE] = getAuftragpositionFac()
									.auftragpositionFindByPrimaryKey(
											lsposDto.getAuftragpositionIId())
									.getNMenge();

						} else {
							oZeile[REPORT_AUFTRAGSUEBERSICHT_NICHT_ZUGEORDNET] = Boolean.TRUE;
						}

						oZeile[REPORT_AUFTRAGSUEBERSICHT_ARBEITSZEIT] = Boolean.FALSE;

						oZeile[REPORT_AUFTRAGSUEBERSICHT_EINHEIT] = lsposDto
								.getEinheitCNr();
						oZeile[REPORT_AUFTRAGSUEBERSICHT_ISTMENGE] = lsposDto
								.getNMenge();

						oZeile[REPORT_AUFTRAGSUEBERSICHT_LIEFERSCHEIN] = lsDto
								.getCNr();
						oZeile[REPORT_AUFTRAGSUEBERSICHT_BELEGDATM] = lsDto
								.getTBelegdatum();

						oZeile[REPORT_AUFTRAGSUEBERSICHT_POS_NR] = lsposDto
								.getISort();
						oZeile[REPORT_AUFTRAGSUEBERSICHT_LIEFERTERMIN] = lsDto
								.getTLiefertermin();
						oZeile[REPORT_AUFTRAGSUEBERSICHT_NETTOPREIS] = lsposDto
								.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte();

						String sortString = lsDto.getCNr()
								+ " "
								+ Helper.fitString2LengthAlignRight(
										lsposDto.getISort() + "", 10, ' ');
						tmPositionen.put(sortString, oZeile);
					}
				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}
		// RE-Pos hinzufuegen
		Iterator itRe = hmRechnungen.iterator();
		while (itRe.hasNext()) {
			Integer rechnungIId = (Integer) itRe.next();

			try {
				RechnungDto reDto = getRechnungFac().rechnungFindByPrimaryKey(
						rechnungIId);

				RechnungPositionDto[] reposDtos = getRechnungFac()
						.rechnungPositionFindByRechnungIId(rechnungIId);

				for (int i = 0; i < reposDtos.length; i++) {

					RechnungPositionDto reposDto = reposDtos[i];

					if (reposDto.getArtikelIId() != null) {

						Object[] oZeile = new Object[REPORT_AUFTRAGSUEBERSICHT_ANZAHL_SPALTEN];

						ArtikelDto aDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(
										reposDto.getArtikelIId(), theClientDto);

						oZeile[REPORT_AUFTRAGSUEBERSICHT_ARTIKELNUMMER] = aDto
								.getCNr();
						oZeile[REPORT_AUFTRAGSUEBERSICHT_ARTIKELNUMMER] = aDto
								.getCNr();
						if (aDto.getArtikelsprDto() != null) {
							oZeile[REPORT_AUFTRAGSUEBERSICHT_BEZEICHNUNG] = aDto
									.getArtikelsprDto().getCBez();
							oZeile[REPORT_AUFTRAGSUEBERSICHT_ZUSATZBEZEICHNUNG] = aDto
									.getArtikelsprDto().getCZbez();
						}

						if (reposDto.getAuftragpositionIId() != null) {
							oZeile[REPORT_AUFTRAGSUEBERSICHT_NICHT_ZUGEORDNET] = Boolean.FALSE;

							oZeile[REPORT_AUFTRAGSUEBERSICHT_SOLLMENGE] = getAuftragpositionFac()
									.auftragpositionFindByPrimaryKey(
											reposDto.getAuftragpositionIId())
									.getNMenge();

						} else {
							oZeile[REPORT_AUFTRAGSUEBERSICHT_NICHT_ZUGEORDNET] = Boolean.TRUE;
						}

						oZeile[REPORT_AUFTRAGSUEBERSICHT_ARBEITSZEIT] = Boolean.FALSE;

						oZeile[REPORT_AUFTRAGSUEBERSICHT_EINHEIT] = reposDto
								.getEinheitCNr();
						oZeile[REPORT_AUFTRAGSUEBERSICHT_ISTMENGE] = reposDto
								.getNMenge();

						oZeile[REPORT_AUFTRAGSUEBERSICHT_RECHNUNG] = reDto
								.getCNr();
						oZeile[REPORT_AUFTRAGSUEBERSICHT_BELEGDATM] = reDto
								.getTBelegdatum();

						oZeile[REPORT_AUFTRAGSUEBERSICHT_NETTOPREIS] = reposDto
								.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte();

						String sortString = "XXX"
								+ reDto.getCNr()
								+ "  "
								+ Helper.fitString2LengthAlignRight(
										reposDto.getISort() + "", 10, ' ');
						tmPositionen.put(sortString, oZeile);

					}
				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}

		// Positionsdaten sortieren

		ArrayList alDatenKomplett = new ArrayList();

		// Nun AZ und anschliessend PosDaten hinzufuegen

		for (int i = 0; i < alDatenZeit.size(); i++) {
			alDatenKomplett.add(alDatenZeit.get(i));
		}

		Iterator itPos = tmPositionen.keySet().iterator();

		while (itPos.hasNext()) {
			alDatenKomplett.add(tmPositionen.get(itPos.next()));
		}

		Object[][] dataLocal = new Object[alDatenKomplett.size()][AuftragReportFac.REPORT_AUFTRAG_OFFENE_POSITIONEN_ANZAHL_SPALTEN];
		dataLocal = (Object[][]) alDatenKomplett.toArray(dataLocal);

		return dataLocal;
	}

	public Set getAlleRechnungenEinesAuftrags(Integer iIdAuftragI) {
		// Alle Rechnungen holen, die einen Bezug zum Auftrag haben
		Set hmRechnungen = new TreeSet();
		Session sessionRepos = FLRSessionFactory.getFactory().openSession();
		String sQueryRepos = "SELECT distinct re.i_id from FLRRechnung re WHERE re.auftrag_i_id="
				+ iIdAuftragI;
		Query queryRepos = sessionRepos.createQuery(sQueryRepos);

		List<?> resultListrepos = queryRepos.list();

		Iterator itRepos = resultListrepos.iterator();
		while (itRepos.hasNext()) {
			Integer rechnungIId = (Integer) itRepos.next();

			if (!hmRechnungen.contains(rechnungIId)) {
				hmRechnungen.add(rechnungIId);
			}

		}
		sessionRepos.close();
		// Alle Rechungspositionen holen, die einen Bezug zum Auftrag haben
		sessionRepos = FLRSessionFactory.getFactory().openSession();
		sQueryRepos = "SELECT distinct repos.flrrechnung.i_id from FLRRechnungPosition repos WHERE repos.flrpositionensichtauftrag.flrauftrag.i_id="
				+ iIdAuftragI
				+ " OR repos.flrrechnung.auftrag_i_id="
				+ iIdAuftragI;
		queryRepos = sessionRepos.createQuery(sQueryRepos);

		resultListrepos = queryRepos.list();

		itRepos = resultListrepos.iterator();
		while (itRepos.hasNext()) {
			Integer rechnungIId = (Integer) itRepos.next();

			if (!hmRechnungen.contains(rechnungIId)) {
				hmRechnungen.add(rechnungIId);
			}

		}
		sessionRepos.close();
		return hmRechnungen;
	}

	public Set getAlleLieferscheineEinesAuftrags(Integer iIdAuftragI) {

		Set hmLieferscheine = new TreeSet();

		// Alle Lieferscheine holen, die einen Bezug zum Auftrag haben
		Session session = FLRSessionFactory.getFactory().openSession();
		String sQueryLspos = "SELECT distinct ls.i_id from FLRLieferschein ls WHERE ls.auftrag_i_id="
				+ iIdAuftragI;
		Query queryLspos = session.createQuery(sQueryLspos);

		List<?> resultListLspos = queryLspos.list();

		Iterator it = resultListLspos.iterator();
		while (it.hasNext()) {
			Integer lieferscheinIId = (Integer) it.next();

			if (!hmLieferscheine.contains(lieferscheinIId)) {
				hmLieferscheine.add(lieferscheinIId);
			}

		}
		session.close();

		// Alle Lieferscheinpositionen holen, die einen Bezug zum Auftrag haben
		session = FLRSessionFactory.getFactory().openSession();
		sQueryLspos = "SELECT distinct lspos.flrlieferschein.i_id from FLRLieferscheinposition lspos WHERE lspos.flrpositionensichtauftrag.flrauftrag.i_id="
				+ iIdAuftragI
				+ " OR lspos.flrlieferschein.auftrag_i_id="
				+ iIdAuftragI;
		queryLspos = session.createQuery(sQueryLspos);

		resultListLspos = queryLspos.list();

		it = resultListLspos.iterator();
		while (it.hasNext()) {
			Integer lieferscheinIId = (Integer) it.next();

			if (!hmLieferscheine.contains(lieferscheinIId)) {
				hmLieferscheine.add(lieferscheinIId);
			}

		}
		session.close();
		return hmLieferscheine;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAuftragszeiten(Integer iIdAuftragI,
			boolean bSortiertNachPerson, TheClientDto theClientDto)
			throws EJBExceptionLP {

		cAktuellerReport = AuftragReportFac.REPORT_AUFTRAGSZEITEN;

		AuftragzeitenDto[] dtos = null;

		dtos = getZeiterfassungFac().getAllZeitenEinesBeleges(
				LocaleFac.BELEGART_AUFTRAG, iIdAuftragI, null, null, null,
				null, false, true, theClientDto);

		data = new Object[dtos.length][AuftragReportFac.REPORT_AUFTRAGZEITEN_ANZAHL_SPALTEN];

		for (int i = 0; i < dtos.length; i++) {
			data[i][AuftragReportFac.REPORT_AUFTRAGZEITEN_PERSON] = dtos[i]
					.getSPersonalMaschinenname();
			data[i][AuftragReportFac.REPORT_AUFTRAGZEITEN_ARTIKEL] = dtos[i]
					.getSArtikelcnr();
			data[i][AuftragReportFac.REPORT_AUFTRAGZEITEN_BEMERKUNG] = dtos[i]
					.getSZeitbuchungtext();
			data[i][AuftragReportFac.REPORT_AUFTRAGZEITEN_BEZEICHNUNG] = dtos[i]
					.getSArtikelbezeichnung();
			data[i][AuftragReportFac.REPORT_AUFTRAGZEITEN_BIS] = dtos[i]
					.getTsEnde();
			data[i][AuftragReportFac.REPORT_AUFTRAGZEITEN_VON] = dtos[i]
					.getTsBeginn();
			data[i][AuftragReportFac.REPORT_AUFTRAGZEITEN_DAUER] = dtos[i]
					.getDdDauer();
			data[i][AuftragReportFac.REPORT_AUFTRAGZEITEN_KOMMENTAR] = dtos[i]
					.getSKommentar();
			data[i][AuftragReportFac.REPORT_AUFTRAGZEITEN_KOSTEN] = dtos[i]
					.getBdKosten();

			// Stundensatz aus Personalgehalt holen
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(dtos[i].getTsBeginn().getTime());
			try {
				PersonalgehaltDto pgDto = getPersonalFac()
						.personalgehaltFindLetztePersonalgehalt(
								dtos[i].getIPersonalMaschinenId(),
								c.get(Calendar.YEAR), c.get(Calendar.MONTH));
				if (pgDto != null) {
					data[i][AuftragReportFac.REPORT_AUFTRAGZEITEN_STUNDENSATZ] = pgDto
							.getNStundensatz();
				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			try {

				if (dtos[i].getBelegpositionIId() != null) {
					AuftragpositionDto apDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKeyOhneExc(
									dtos[i].getBelegpositionIId());

					if (apDto != null) {

						data[i][AuftragReportFac.REPORT_AUFTRAGZEITEN_POSITIONSNUMMER] = getAuftragpositionFac()
								.getPositionNummer(apDto.getIId());

						if (apDto.getArtikelIId() != null) {

							ArtikelDto artikelDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(
											apDto.getArtikelIId(), theClientDto);
							data[i][AuftragReportFac.REPORT_AUFTRAGZEITEN_ARTIKEL_POSITION] = artikelDto
									.getCNr();
							if (apDto.getCBez() != null) {
								data[i][AuftragReportFac.REPORT_AUFTRAGZEITEN_BEZEICHNUNG_POSITION] = apDto
										.getCBez();
							} else {
								if (artikelDto.getArtikelsprDto() != null) {
									data[i][AuftragReportFac.REPORT_AUFTRAGZEITEN_BEZEICHNUNG_POSITION] = artikelDto
											.getArtikelsprDto().getCBez();
								}
							}

							if (apDto.getCZusatzbez() != null) {
								data[i][AuftragReportFac.REPORT_AUFTRAGZEITEN_ZUSATZBEZEICHNUNG_POSITION] = apDto
										.getCZusatzbez();
							} else {
								if (artikelDto.getArtikelsprDto() != null) {
									data[i][AuftragReportFac.REPORT_AUFTRAGZEITEN_ZUSATZBEZEICHNUNG_POSITION] = artikelDto
											.getArtikelsprDto().getCZbez();
								}
							}
						}

					} else {
						data[i][AuftragReportFac.REPORT_AUFTRAGZEITEN_POSITIONSNUMMER] = new Integer(
								0);
					}

				} else {
					data[i][AuftragReportFac.REPORT_AUFTRAGZEITEN_POSITIONSNUMMER] = new Integer(
							0);
				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}

		if (bSortiertNachPerson == false) {
			for (int k = data.length - 1; k > 0; --k) {
				for (int j = 0; j < k; ++j) {
					Object[] a1 = (Object[]) data[j];
					Object[] a2 = (Object[]) data[j + 1];

					Integer i1 = (Integer) a1[AuftragReportFac.REPORT_AUFTRAGZEITEN_POSITIONSNUMMER];
					Integer i2 = (Integer) a2[AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_LIEFERANT];

					if (i1 > i2) {
						data[j] = a2;
						data[j + 1] = a1;
					}
				}
			}
		}

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		try {
			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(
					iIdAuftragI);
			parameter.put("P_AUFTRAG", auftragDto.getCNr());
			parameter.put("P_PROJEKT", auftragDto.getCBezProjektbezeichnung());
			parameter.put("P_KOMMENTARINTERN",
					auftragDto.getXInternerkommentar());
			parameter.put("P_KOMMENTAREXTERN",
					auftragDto.getXExternerkommentar());

			parameter.put("P_SORTIERTNACHPERSON", new Boolean(
					bSortiertNachPerson));

			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					auftragDto.getKundeIIdAuftragsadresse(), theClientDto);
			parameter.put("P_KUNDE_NAME1", kundeDto.getPartnerDto()
					.getCName1nachnamefirmazeile1());
			parameter.put("P_KUNDE_NAME2", kundeDto.getPartnerDto()
					.getCName2vornamefirmazeile2());
			parameter.put("P_KUNDE_NAME3", kundeDto.getPartnerDto()
					.getCName3vorname2abteilung());

			if (kundeDto.getPartnerDto().getLandplzortDto() != null) {
				parameter.put("P_KUNDE_STRASSE", kundeDto.getPartnerDto()
						.getCStrasse());
				parameter.put("P_KUNDE_LAND", kundeDto.getPartnerDto()
						.getLandplzortDto().getLandDto().getCLkz());
				parameter.put("P_KUNDE_PLZ", kundeDto.getPartnerDto()
						.getLandplzortDto().getCPlz());
				parameter.put("P_KUNDE_ORT", kundeDto.getPartnerDto()
						.getLandplzortDto().getOrtDto().getCName());
			}

			if (auftragDto.getAnsprechparnterIId() != null) {
				AnsprechpartnerDto ansprechpartnerDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(
								auftragDto.getAnsprechparnterIId(),
								theClientDto);
				parameter.put("P_ANSPRECHPARTNER", ansprechpartnerDto
						.getPartnerDto().formatAnrede());
			}

			parameter.put("P_BESTELLNUMMER", auftragDto.getCBestellnummer());
			parameter.put("P_BESTELLDATUM", auftragDto.getDBestelldatum());
			parameter.put("P_LIEFERTERMIN", auftragDto.getDLiefertermin());
			parameter.put("P_FINALTERMIN", auftragDto.getDFinaltermin());
			parameter.put("P_BELEGDATUM", auftragDto.getTBelegdatum());

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		String[] fieldnames = new String[] { "F_ARTIKELNUMMER",
				"F_BEZEICHNUNG", "F_SOLLZEIT" };

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = FLRSessionFactory.getFactory().openSession();

		String query = "SELECT SUM(apos.n_menge),apos.flrartikel.i_id,apos.flrartikel.c_nr FROM FLRAuftragposition apos WHERE apos.flrartikel.artikelart_c_nr='"
				+ ArtikelFac.ARTIKELART_ARBEITSZEIT
				+ "' AND apos.flrauftrag.i_id="
				+ iIdAuftragI
				+ " GROUP BY apos.flrartikel.i_id,apos.flrartikel.c_nr ORDER BY apos.flrartikel.c_nr ASC";

		Query qResult = session.createQuery(query);

		List<?> list = qResult.list();
		Iterator<?> it = list.iterator();
		Object[][] dataSub = new Object[list.size()][fieldnames.length];
		int i = 0;
		while (it.hasNext()) {
			Object[] o = (Object[]) it.next();
			dataSub[i][0] = o[2];

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					(Integer) o[1], theClientDto);
			dataSub[i][1] = artikelDto.formatBezeichnung();

			dataSub[i][2] = o[0];
			i++;

		}
		session.close();

		parameter.put("DATENSUBREPORT", new LPDatenSubreport(dataSub,
				fieldnames));

		initJRDS(parameter, AuftragReportFac.REPORT_MODUL,
				AuftragReportFac.REPORT_AUFTRAGSZEITEN,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	/**
	 * Die Vorkalkulation eines Auftrags drucken. <br>
	 * Beruecksichtigt werden nur preisbehaftete Positionen.
	 * 
	 * @param iIdAuftragI
	 *            PK des Auftrags
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return JasperPrint der Druck
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public JasperPrintLP printAuftragVorkalkulation(Integer iIdAuftragI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		JasperPrintLP oPrint = null;

		cAktuellerReport = AuftragReportFac.REPORT_AUFTRAG_VORKALKULATION;
		Session session = null;

		try {
			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(
					iIdAuftragI);

			// UW 29.03.06 Tiefe der Stuecklistenaufloeseung bestimmen
			ParametermandantDto parametermandantDto = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_AUFTRAG,
							ParameterFac.PARAMETER_AUFTRAG_ABVORKALK_STKL_AUFLOESUNG_TIEFE);

			int iStuecklisteaufloesungTiefe = ((Integer) parametermandantDto
					.getCWertAsObject()).intValue();

			session = FLRSessionFactory.getFactory().openSession();

			Criteria crit = session
					.createCriteria(FLRAuftragpositionReport.class);
			crit.add(Restrictions
					.eq(AuftragpositionFac.FLR_AUFTRAGPOSITION_FLRAUFTRAG
							+ ".i_id", iIdAuftragI));

			// UW 06.04.06 alle Positionen mit positiver Menge
			crit.add(Restrictions
					.isNotNull(AuftragpositionFac.FLR_AUFTRAGPOSITION_N_MENGE));
			crit.add(Restrictions.gt(
					AuftragpositionFac.FLR_AUFTRAGPOSITION_N_MENGE,
					new BigDecimal(0)));

			crit.addOrder(Order
					.asc(AuftragpositionFac.FLR_AUFTRAGPOSITION_I_SORT));

			// Liste aller Positionen, die behandelt werden sollen
			List<?> list = crit.list();
			FLRAuftragpositionReport[] aFlrauftragposition = new FLRAuftragpositionReport[list
					.size()];
			aFlrauftragposition = (FLRAuftragpositionReport[]) list
					.toArray(aFlrauftragposition);

			List<Object> dataList = new ArrayList<Object>(); // in dieser Liste
			// werden
			// die Daten fuer den
			// Druck gesammelt
			String positionsartCNr = null;

			for (int i = 0; i < aFlrauftragposition.length; i++) {
				positionsartCNr = aFlrauftragposition[i]
						.getAuftragpositionart_c_nr();

				if (positionsartCNr
						.equals(AngebotServiceFac.ANGEBOTPOSITIONART_IDENT)) {
					FLRArtikel flrartikel = aFlrauftragposition[i]
							.getFlrartikel();

					// zum Befuellen der Preisinformationen muss bekannt sein,
					// ob es Stuecklistenpositionen gibt
					StuecklisteInfoDto stuecklisteInfoDto = getStuecklisteFac()
							.getStrukturdatenEinesArtikels(
									flrartikel.getI_id(), false, null, // in die
									// Rekursion
									// mit
									// einer
									// leeren
									// Listen
									// einsteigen
									0, // in die Rekursion mit Ebene 0
										// einsteigen
									iStuecklisteaufloesungTiefe, // alle
									// Stuecklisten
									// lt.
									// Parameter
									// aufloesen
									true, // Basis sind die Einheiten der
									// Stueckliste
									aFlrauftragposition[i].getN_menge(), // Basis
									// sind
									// n
									// Einheiten
									// der
									// Stueckliste
									false, // Fremdfertigung nicht aufloesen
									theClientDto);

					if (stuecklisteInfoDto.getIiAnzahlPositionen().intValue() > 0
							&& (!Helper.short2boolean(stuecklisteInfoDto
									.getBIstFremdfertigung()) || iStuecklisteaufloesungTiefe != 0)) {
						dataList.add(befuelleZeileVKMitIdentMitSTKLAusAuftragposition(
								aFlrauftragposition[i], theClientDto));

						if (stuecklisteInfoDto.getIiAnzahlPositionen()
								.intValue() > 0) {
							ArrayList<?> alStuecklisteAufgeloest = stuecklisteInfoDto
									.getAlStuecklisteAufgeloest();
							Iterator<?> it = alStuecklisteAufgeloest.iterator();

							while (it.hasNext()) {
								dataList.add(befuelleZeileVorkalkulationMitStuecklistenposition(
										flrartikel.getI_id(), // eine
										// Zwischensumme
										// fuer
										// den
										// uebergeordneten
										// Artikel
										// bilden
										"",
										(StuecklisteMitStrukturDto) it.next(),
										aFlrauftragposition[i]
												.getFlrauftrag()
												.getWaehrung_c_nr_auftragswaehrung(),
										aFlrauftragposition[i].getN_menge(),
										aFlrauftragposition[i]
												.getEinheit_c_nr(),
										iStuecklisteaufloesungTiefe,
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
						dataList.add(befuelleZeileVKMitIdentOhneSTKLAusAuftragposition(
								aFlrauftragposition[i], theClientDto));
					}
				} else if (positionsartCNr
						.equals(AngebotServiceFac.ANGEBOTPOSITIONART_HANDEINGABE)) {
					dataList.add(befuelleZeileVKMitHandAusAuftragposition(
							aFlrauftragposition[i], theClientDto));
				}
			}

			// jetzt die Map mit dataRows in ein Object[][] fuer den Druck
			// umwandeln
			data = new Object[dataList.size()][AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ANZAHL_SPALTEN];
			data = (Object[][]) dataList.toArray(data);

			// Erstellung des Report
			HashMap<String, Object> parameter = new HashMap<String, Object>();

			// es gilt das Locale des Benutzers
			Locale locDruck = theClientDto.getLocUi();

			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					auftragDto.getKundeIIdAuftragsadresse(), theClientDto);

			AnsprechpartnerDto ansprechpartnerDto = null;

			if (auftragDto.getAnsprechparnterIId() != null) {
				ansprechpartnerDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(
								auftragDto.getAnsprechparnterIId(),
								theClientDto);
			}

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);

			parameter.put(
					LPReport.P_ADRESSBLOCK,
					formatAdresseFuerAusdruck(kundeDto.getPartnerDto(),
							ansprechpartnerDto, mandantDto, locDruck));
			parameter.put("P_AUFTRAGNUMMER", auftragDto.getCNr());
			parameter.put("P_AUFTRAGCBEZ",
					auftragDto.getCBezProjektbezeichnung());
			parameter.put(
					"P_LIEFERART",
					getLocaleFac().lieferartFindByIIdLocaleOhneExc(
							auftragDto.getLieferartIId(), locDruck,
							theClientDto));
			parameter.put(LPReport.P_WAEHRUNG,
					theClientDto.getSMandantenwaehrung());
			parameter.put(
					"P_ZAHLUNGSZIEL",
					getMandantFac().zahlungszielFindByIIdLocaleOhneExc(
							auftragDto.getZahlungszielIId(), locDruck,
							theClientDto));
			parameter.put("P_BELEGDATUM", auftragDto.getTBelegdatum());

			initJRDS(parameter, AuftragReportFac.REPORT_MODUL,
					AuftragReportFac.REPORT_AUFTRAG_VORKALKULATION,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto, true, auftragDto.getKostIId());

			oPrint = getReportPrint();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} finally {
			try {
				if (session != null)
					session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE, he);
			}
		}

		return oPrint;
	}

	/**
	 * Eine Zeile der VK mit Auftragposition vom Typ Ident befuellen, wobei der
	 * Artikel eine STKL ist. <br>
	 * Die Position wird immer in Ebene 0 angedruckt.
	 * 
	 * @param flrauftragpositionI
	 *            die zu druckende AB Position
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Object[] die zu druckenden Daten
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileVKMitIdentMitSTKLAusAuftragposition(
			FLRAuftragpositionReport flrauftragpositionI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Object[] dataRow = new Object[AuftragReportFac.REPORT_VORKALKULATION_ANZAHL_SPALTEN];
		try {
			ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(
							flrauftragpositionI.getArtikel_i_id(), theClientDto);

			dataRow[AngebotReportFac.REPORT_VORKALKULATION_IDENT] = artikelDto
					.getCNr();
			dataRow[AngebotReportFac.REPORT_VORKALKULATION_BEZEICHNUNG] = artikelDto
					.getArtikelsprDto().getCBez();
			dataRow[AngebotReportFac.REPORT_VORKALKULATION_ZUSATZBEZEICHNUNG] = artikelDto
					.getArtikelsprDto().getCZbez();
			dataRow[AngebotReportFac.REPORT_VORKALKULATION_MENGE] = flrauftragpositionI
					.getN_menge();
			dataRow[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT] = flrauftragpositionI
					.getEinheit_c_nr();

			// Gestehungspreis vom Hauptlager des Mandanten holen
			BigDecimal nGestehungspreis = getLagerFac()
					.getGemittelterGestehungspreisDesHauptlagers(
							flrauftragpositionI.getArtikel_i_id(), theClientDto);

			dataRow[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS_MANUELL] = formatBigDecimalAsInfo(nGestehungspreis);
			dataRow[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT_MANUELL] = formatBigDecimalAsInfo(nGestehungspreis
					.multiply(flrauftragpositionI.getN_menge()));
			dataRow[AngebotReportFac.REPORT_VORKALKULATION_VERKAUFSPREIS] = flrauftragpositionI
					.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte();
			dataRow[AngebotReportFac.REPORT_VORKALKULATION_VERKAUFSWERT] = flrauftragpositionI
					.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte()
					.multiply(flrauftragpositionI.getN_menge());
			dataRow[AngebotReportFac.REPORT_VORKALKULATION_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_IDENT;
			dataRow[AngebotReportFac.REPORT_VORKALKULATION_INDEX_GRUPPE] = artikelDto
					.getIId();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return dataRow;
	}

	/**
	 * Eine Zeile der AB Vorkalkulation mit der Stuecklisteninformation
	 * befuellen.
	 * 
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
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Object[] die zu druckenden Daten
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileVorkalkulationMitStuecklistenposition(
			Integer indexGruppeIIdI, String cPraefixArtikelCNrI,
			StuecklisteMitStrukturDto stuecklisteMitStrukturDtoI,
			String waehrungCNrI, BigDecimal nUebergeordneteMengeI,
			String cNrUebergeordneteEinheitI,
			int iStuecklistenAufloesungTiefeI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		Object[] dataRow = new Object[AuftragReportFac.REPORT_VORKALKULATION_ANZAHL_SPALTEN];
		try {
			// Pro Position eine kuenstliche Zeile zum Andrucken erzeugen,
			dataRow[AngebotReportFac.REPORT_VORKALKULATION_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_STUECKLISTENPOSITION;

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

			dataRow = befuelleZeileVorkalkulationMitArtikelCNrCBezCZBez(
					dataRow, artikelDto, cPraefixArtikelCNrI, null, // keine
					// Uebersteuerung
					// der cBez
					// des
					// Artikels
					theClientDto);

			// es wurde die Gesamtmenge der Unterpositionen in der STKL
			// berechnet
			dataRow[AngebotReportFac.REPORT_VORKALKULATION_MENGE] = stuecklistepositionDto
					.getNMenge();
			dataRow[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT] = stuecklistepositionDto
					.getEinheitCNr();
			dataRow[AngebotReportFac.REPORT_VORKALKULATION_MENGE_UEBERGEORDNET] = nUebergeordneteMengeI;
			dataRow[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT_UEBERGEORDNET] = cNrUebergeordneteEinheitI;

			if (artikelDto.getArtikelartCNr().equals(
					ArtikelFac.ARTIKELART_HANDARTIKEL)) {
				// WH xx.03.06 Handeingaben in STKL haben keinen Gestehungspreis
				dataRow[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS] = new BigDecimal(
						0);
				dataRow[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT] = new BigDecimal(
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
					// Grundlage ist der Gestehungspreis des Artikels am
					// Hauptlager des Mandanten
					BigDecimal gestehungspreisInMandantenwaehrung = getLagerFac()
							.getGemittelterGestehungspreisDesHauptlagers(
									stuecklistepositionDto.getArtikelIId(),
									theClientDto);

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
						dataRow[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS] = gestehungspreisInBelegwaehrung;
						dataRow[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT] = gestehungspreisInBelegwaehrung
								.multiply(stuecklistepositionDto.getNMenge());
					}
				}
			}

			dataRow[AngebotReportFac.REPORT_VORKALKULATION_INDEX_GRUPPE] = indexGruppeIIdI; // fuer
			// die
			// Bildung
			// der
			// Zwischensumme
			// !
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return dataRow;
	}

	/**
	 * Eine Zeile der AG Vorkalkulation mit artikelCNr, CBez, CZBez befuellen. <br>
	 * Die Felder muessen getrennt sein, damit sie im Druck unterschiedlich
	 * formatiert werden koennen.
	 * 
	 * @param dataRowI
	 *            die zu druckenden Daten
	 * @param artikelDtoI
	 *            der anzudruckende Artikel
	 * @param cPraefixArtikelCNrI
	 *            die Einrueckung fuer Stuecklistenpositionen
	 * @param cBezUebersteuertI
	 *            die Artikelbezeichnung kann uebersteuert sein
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Object[] die zu druckenden Daten
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileVorkalkulationMitArtikelCNrCBezCZBez(
			Object[] dataRowI, ArtikelDto artikelDtoI,
			String cPraefixArtikelCNrI, String cBezUebersteuertI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (artikelDtoI.getArtikelartCNr().equals(
				ArtikelFac.ARTIKELART_HANDARTIKEL)) {
			dataRowI[AngebotReportFac.REPORT_VORKALKULATION_IDENT] = cPraefixArtikelCNrI
					+ AngebotReportFac.REPORT_VORKALKULATION_ZEICHEN_FUER_HANDEINGABE;
			dataRowI[AngebotReportFac.REPORT_VORKALKULATION_BEZEICHNUNG] = artikelDtoI
					.getArtikelsprDto().getCBez();
			dataRowI[AngebotReportFac.REPORT_VORKALKULATION_ZUSATZBEZEICHNUNG] = artikelDtoI
					.getArtikelsprDto().getCZbez();
		} else {
			dataRowI[AngebotReportFac.REPORT_VORKALKULATION_IDENT] = cPraefixArtikelCNrI
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

			dataRowI[AngebotReportFac.REPORT_VORKALKULATION_BEZEICHNUNG] = cBez;
			dataRowI[AngebotReportFac.REPORT_VORKALKULATION_ZUSATZBEZEICHNUNG] = artikelDtoI
					.getArtikelsprDto().getCZbez();
		}

		return dataRowI;
	}

	/**
	 * Eine Zeile der VK mit der Identposition einer AB Position befuellen. <br>
	 * Die Position wird immer in Ebene 0 angedruckt.
	 * 
	 * @param flrauftragpositionI
	 *            die zu druckende AB Position
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Object[] die zu druckenden Daten
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileVKMitIdentOhneSTKLAusAuftragposition(
			FLRAuftragpositionReport flrauftragpositionI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Object[] dataRow = new Object[AuftragReportFac.REPORT_VORKALKULATION_ANZAHL_SPALTEN];
		try {
			ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(
							flrauftragpositionI.getArtikel_i_id(), theClientDto);

			dataRow[AngebotReportFac.REPORT_VORKALKULATION_IDENT] = artikelDto
					.getCNr();
			dataRow[AngebotReportFac.REPORT_VORKALKULATION_BEZEICHNUNG] = artikelDto
					.getArtikelsprDto().getCBez();
			dataRow[AngebotReportFac.REPORT_VORKALKULATION_ZUSATZBEZEICHNUNG] = artikelDto
					.getArtikelsprDto().getCZbez();
			dataRow[AngebotReportFac.REPORT_VORKALKULATION_MENGE] = flrauftragpositionI
					.getN_menge();
			dataRow[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT] = flrauftragpositionI
					.getEinheit_c_nr();

			BigDecimal nGestehungspreisInMandantenwaehrung = getLagerFac()
					.getGemittelterGestehungspreisDesHauptlagers(
							flrauftragpositionI.getArtikel_i_id(), theClientDto);

			dataRow[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS] = nGestehungspreisInMandantenwaehrung;
			dataRow[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT] = nGestehungspreisInMandantenwaehrung
					.multiply(flrauftragpositionI.getN_menge());
			dataRow[AngebotReportFac.REPORT_VORKALKULATION_VERKAUFSPREIS] = flrauftragpositionI
					.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte();
			dataRow[AngebotReportFac.REPORT_VORKALKULATION_VERKAUFSWERT] = flrauftragpositionI
					.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte()
					.multiply(flrauftragpositionI.getN_menge());
			dataRow[AngebotReportFac.REPORT_VORKALKULATION_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_IDENT;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return dataRow;
	}

	/**
	 * Eine Zeile der VK mit der Handposition einer AB Position befuellen. <br>
	 * Die Position wird immer in Ebene 0 angedruckt.
	 * 
	 * @param flrauftragpositionI
	 *            die zu druckende AB Position
	 * @param theClientDto
	 *            der aktuelle Beutzer
	 * @return Object[] die zu druckenden Daten
	 * @throws EJBExceptionLP
	 */
	private Object[] befuelleZeileVKMitHandAusAuftragposition(
			FLRAuftragpositionReport flrauftragpositionI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Object[] dataRow = new Object[AuftragReportFac.REPORT_VORKALKULATION_ANZAHL_SPALTEN];
		try {
			ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(
							flrauftragpositionI.getArtikel_i_id(), theClientDto);

			dataRow[AngebotReportFac.REPORT_VORKALKULATION_IDENT] = AngebotReportFac.REPORT_VORKALKULATION_ZEICHEN_FUER_HANDEINGABE;
			dataRow[AngebotReportFac.REPORT_VORKALKULATION_BEZEICHNUNG] = artikelDto
					.getArtikelsprDto().getCBez();
			dataRow[AngebotReportFac.REPORT_VORKALKULATION_ZUSATZBEZEICHNUNG] = artikelDto
					.getArtikelsprDto().getCZbez();
			dataRow[AngebotReportFac.REPORT_VORKALKULATION_MENGE] = flrauftragpositionI
					.getN_menge();
			dataRow[AngebotReportFac.REPORT_VORKALKULATION_EINHEIT] = flrauftragpositionI
					.getEinheit_c_nr();

			BigDecimal nGestehungspreisInMandantenwaehrung = getLagerFac()
					.getGemittelterGestehungspreisDesHauptlagers(
							flrauftragpositionI.getArtikel_i_id(), theClientDto);

			dataRow[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSPREIS] = nGestehungspreisInMandantenwaehrung;
			dataRow[AngebotReportFac.REPORT_VORKALKULATION_GESTEHUNGSWERT] = nGestehungspreisInMandantenwaehrung
					.multiply(flrauftragpositionI.getN_menge());
			dataRow[AngebotReportFac.REPORT_VORKALKULATION_VERKAUFSPREIS] = flrauftragpositionI
					.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte();
			dataRow[AngebotReportFac.REPORT_VORKALKULATION_VERKAUFSWERT] = flrauftragpositionI
					.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte()
					.multiply(flrauftragpositionI.getN_menge());
			dataRow[AngebotReportFac.REPORT_VORKALKULATION_POSITIONSART] = AngebotServiceFac.ANGEBOTPOSITIONART_HANDEINGABE;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return dataRow;
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
	 * Alle offenen Auftraege fuer einen bestimmten Mandanten drucken. Diese
	 * Methode liefert eine Liste von allen offenen Auftraegen eines Mandanten,
	 * die nach den eingegebenen Kriterien des Benutzers zusammengestellt wird. <br>
	 * Achtung: Hibernate verwendet lazy initialization, d.h. der Zugriff auf
	 * Collections muss innerhalb der Session erfolgen.
	 * 
	 * @param krit
	 *            die Filter- und Sortierkriterien
	 * @param dStichtag
	 *            Date
	 * @param bSortierungNachLiefertermin
	 *            Boolean
	 * @param bInternenKommentarDrucken
	 *            Boolean
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @return JasperPrint der Druck
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAuftragOffeneOhneDetail(
			ReportJournalKriterienDto krit, Date dStichtag,
			Boolean bSortierungNachLiefertermin,
			Boolean bInternenKommentarDrucken, Integer iArt,
			Integer iArtUnverbindlich, boolean bMitAngelegten,
			boolean bStichtagGreiftBeiLiefertermin, TheClientDto theClientDto)
			throws EJBExceptionLP {

		long l = System.currentTimeMillis();
		if (krit == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("reportJournalKriterienDtoI == null"));
		}

		if (dStichtag == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("dStichtag == null"));
		}

		JasperPrintLP oPrintO = null;

		cAktuellerReport = AuftragReportFac.REPORT_AUFTRAG_OFFENE_OHNE_DETAILS;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		// vom Stichtag die Uhrzeit abschneiden
		dStichtag = Helper.cutDate(dStichtag);

		try {
			session = factory.openSession();

			// Hiberante Criteria fuer alle Tabellen ausgehend von meiner
			// Haupttabelle anlegen,
			// nach denen ich filtern und sortieren kann
			Criteria critAuftrag = session
					.createCriteria(FLRAuftragReport.class);

			// Einschraenkung auf den aktuellen Mandanten
			critAuftrag.add(Restrictions.eq(
					AuftragFac.FLR_AUFTRAG_MANDANT_C_NR,
					theClientDto.getMandant()));
			// die Parameter dem Report uebergeben
			HashMap<String, Object> mapParameter = new HashMap<String, Object>();
			// Einschraenkung nach Auftragart
			Collection<String> cArt = null;
			if (iArt != null) {
				if (iArt == 1) {
					critAuftrag.add(Restrictions.ne(
							AuftragFac.FLR_AUFTRAG_AUFTRAGART_C_NR,
							AuftragServiceFac.AUFTRAGART_RAHMEN));
					mapParameter.put(
							"P_ART_RAHMENAUFTRAEGE",
							getTextRespectUISpr(
									"auft.journal.ohnerahmenauftraege",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
				} else if (iArt == 2) {
					critAuftrag.add(Restrictions.eq(
							AuftragFac.FLR_AUFTRAG_AUFTRAGART_C_NR,
							AuftragServiceFac.AUFTRAGART_RAHMEN));
					mapParameter.put(
							"P_ART_RAHMENAUFTRAEGE",
							getTextRespectUISpr(
									"auft.journal.nurrahmenauftraege",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
				}
			}

			mapParameter.put("P_STICHTAG", dStichtag);

			mapParameter.put("P_ART_UNVERBINDLICH", iArtUnverbindlich);

			if (iArtUnverbindlich != AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTUNVERBINDLICH_ALLE) {
				if (iArtUnverbindlich == AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTUNVERBINDLICH_NUR_UNVERBINDLICHE) {
					critAuftrag.add(Restrictions.eq(
							AuftragFac.FLR_AUFTRAG_B_LIEFERTERMINUNVERBINDLICH,
							Helper.boolean2Short(true)));

				} else if (iArtUnverbindlich == AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTUNVERBINDLICH_OHNE_UNVERBINDLICHE) {
					critAuftrag.add(Restrictions.eq(
							AuftragFac.FLR_AUFTRAG_B_LIEFERTERMINUNVERBINDLICH,
							Helper.boolean2Short(false)));

				}
			}

			// Einschraenkung nach Status Offen, Teilerledigt, Erledigt
			Collection<String> cStati = new LinkedList<String>();
			cStati.add(AuftragServiceFac.AUFTRAGSTATUS_OFFEN);
			if (bMitAngelegten) {
				cStati.add(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT);
			}

			// cStati.add(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT);
			cStati.add(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT);
			critAuftrag.add(Restrictions.in(
					AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR, cStati));

			// Das Belegdatum muss vor dem Stichtag liegen
			critAuftrag.add(Restrictions.le(
					AuftragFac.FLR_AUFTRAG_D_BELEGDATUM, dStichtag));

			// SP3294
			if (bStichtagGreiftBeiLiefertermin) {
				critAuftrag.add(Restrictions.or(Restrictions
						.isNull(AuftragFac.FLR_AUFTRAG_T_LIEFERTERMIN),
						Restrictions.le(AuftragFac.FLR_AUFTRAG_T_LIEFERTERMIN,
								dStichtag)));
				mapParameter.put(
						"P_STICHTAG_ART",
						getTextRespectUISpr(
								"auft.report.offene.stichtag.liefertermin",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			} else {

				// Der Auftrag darf zum Stichtag noch nicht erledigt worden sein
				critAuftrag.add(Restrictions.or(Restrictions
						.isNull(AuftragFac.FLR_AUFTRAG_T_ERLEDIGT),
						Restrictions.gt(AuftragFac.FLR_AUFTRAG_T_ERLEDIGT,
								dStichtag)));
				mapParameter.put(
						"P_STICHTAG_ART",
						getTextRespectUISpr(
								"auft.report.offene.stichtag.erledigungsdatum",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			}

			// Einschraenkung nach einer bestimmten Kostenstelle
			if (krit.kostenstelleIId != null) {
				critAuftrag.add(Restrictions.eq(
						AuftragFac.FLR_AUFTRAG_KOSTENSTELLE_I_ID,
						krit.kostenstelleIId));

				mapParameter.put("P_KOSTENSTELLE", getSystemFac()
						.kostenstelleFindByPrimaryKey(krit.kostenstelleIId)
						.getCNr());

			}

			// Einschraenkung nach einem bestimmten Kunden
			if (krit.kundeIId != null) {
				critAuftrag.add(Restrictions.eq(
						AuftragFac.FLR_AUFTRAG_KUNDE_I_ID_AUFTRAGSADRESSE,
						krit.kundeIId));

				mapParameter.put("P_KUNDE", getKundeFac()
						.kundeFindByPrimaryKey(krit.kundeIId, theClientDto)
						.getPartnerDto().getCName1nachnamefirmazeile1());

			}

			// Einschraenkung nach einem bestimmten Vertreter
			if (krit.vertreterIId != null) {
				critAuftrag.add(Restrictions.eq(
						AuftragFac.FLR_AUFTRAG_VERTRETER_I_ID,
						krit.vertreterIId));

				mapParameter.put(
						"P_VERTRETER",
						getPersonalFac()
								.personalFindByPrimaryKey(krit.vertreterIId,
										theClientDto).getPartnerDto()
								.getCName1nachnamefirmazeile1());

			}
			// Einschraenkung nach Belegdatum von - bis
			String sVon = null;
			String sBis = null;

			if (krit.dVon != null) {
				critAuftrag.add(Restrictions.ge(
						AuftragFac.FLR_AUFTRAG_D_BELEGDATUM, krit.dVon));
				sVon = Helper.formatDatum(krit.dVon, theClientDto.getLocUi());
			}

			if (krit.dBis != null) {
				critAuftrag.add(Restrictions.le(
						AuftragFac.FLR_AUFTRAG_D_BELEGDATUM, krit.dBis));
				sBis = Helper.formatDatum(krit.dBis, theClientDto.getLocUi());
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

			if (krit.sBelegnummerVon != null) {
				sVon = HelperServer.getBelegnummernFilterForHibernateCriterias(
						f, iGeschaeftsjahr, sMandantKuerzel,
						krit.sBelegnummerVon);
				critAuftrag.add(Restrictions.ge("c_nr", sVon));
			}

			if (krit.sBelegnummerBis != null) {
				sBis = HelperServer.getBelegnummernFilterForHibernateCriterias(
						f, iGeschaeftsjahr, sMandantKuerzel,
						krit.sBelegnummerBis);
				critAuftrag.add(Restrictions.le("c_nr", sBis));
			}

			// Sortierung nach Kostenstelle ist immer die erste Sortierung
			if (krit.bSortiereNachKostenstelle) {
				critAuftrag.createCriteria(
						AuftragFac.FLR_AUFTRAG_FLRKOSTENSTELLE).addOrder(
						Order.asc("c_nr"));
			}

			// Sortierung nach Kunde, eventuell innerhalb der Kostenstelle
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				critAuftrag
						.createCriteria(AuftragFac.FLR_AUFTRAG_FLRKUNDE)
						.createCriteria(KundeFac.FLR_PARTNER)
						.addOrder(
								Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
			}

			// Sortierung nach Vertreter, eventuell innerhalb der Kostenstelle
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_VERTRETER) {
				critAuftrag.createCriteria(
						RechnungFac.FLR_RECHNUNG_FLRVERTRETER).addOrder(
						Order.asc(PersonalFac.FLR_PERSONAL_C_KURZZEICHEN));
			}

			// Sortierung nach Liefertermin (optional)
			if (bSortierungNachLiefertermin != null
					&& bSortierungNachLiefertermin.booleanValue()) {
				critAuftrag.addOrder(Order
						.asc(AuftragFac.FLR_AUFTRAG_T_LIEFERTERMIN));
			}

			// es wird in jedem Fall nach der Belegnummer sortiert
			critAuftrag.addOrder(Order.asc("c_nr"));
			List<?> list = critAuftrag.list();
			int i = 0;
			Iterator<?> it = list.iterator();
			data = new Object[list.size()][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_ANZAHL_SPALTEN];

			HashMap<Integer, KostenstelleDto> hmKostenstelleCache = new HashMap<Integer, KostenstelleDto>();
			HashMap<Integer, KundeDto> hmKundeCache = new HashMap<Integer, KundeDto>();

			while (it.hasNext()) {
				// die Datenmatrix pro Auftrag befuellen; alle Felder, die nicht
				// explizit besetzt werden, sind null
				FLRAuftragReport flrauftrag = (FLRAuftragReport) it.next();
				// Kostenstelle aus dem Cache
				KostenstelleDto kostenstelleDto = (KostenstelleDto) hmKostenstelleCache
						.get(flrauftrag.getFlrkostenstelle().getI_id());
				// wenn sie noch nicht im cache war, dann aus der db holen und
				// cachen
				if (kostenstelleDto == null) {
					kostenstelleDto = getSystemFac()
							.kostenstelleFindByPrimaryKey(
									flrauftrag.getFlrkostenstelle().getI_id());
					hmKostenstelleCache.put(flrauftrag.getFlrkostenstelle()
							.getI_id(), kostenstelleDto);
				}
				// Kunde aus dem Cache holen
				KundeDto kundeDto = (KundeDto) hmKundeCache.get(flrauftrag
						.getFlrkunde().getI_id());
				// PersonalDto vertreterDto =
				// getPersonalFac().personalFindByPrimaryKey
				// (oAuftragDto.getPersonalIIdVertreter(), theClientDto);
				// wenn er noch nicht im cache war, dann aus der db holen und
				// cachen
				if (kundeDto == null) {
					kundeDto = getKundeFac().kundeFindByPrimaryKey(
							flrauftrag.getFlrkunde().getI_id(), theClientDto);
					hmKundeCache.put(flrauftrag.getFlrkunde().getI_id(),
							kundeDto);
				}
				// Daten array befuellen
				data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_AUFTRAGSNUMMER] = flrauftrag
						.getC_nr();
				data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_AUFTRAGSART] = flrauftrag
						.getAuftragart_c_nr();
				data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_ZEIT_VERRECHENBAR] = flrauftrag
						.getT_verrechenbar();
				if (flrauftrag.getFlrpersonalverrechenbar() != null) {
					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_PERSON_VERRECHENBAR] = HelperServer
							.formatNameAusFLRPartner(flrauftrag
									.getFlrpersonalverrechenbar()
									.getFlrpartner());
				}

				data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_PROJEKT] = flrauftrag
						.getC_bez();
				data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_AUFTRAGBESTELLNUMMER] = flrauftrag
						.getC_bestellnummer();
				data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_KUNDE] = kundeDto
						.getPartnerDto().getCName1nachnamefirmazeile1();
				if (bInternenKommentarDrucken) {
					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_INTERNERKOMMENTAR] = flrauftrag
							.getX_internerkommentar();
				} else {
					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_INTERNERKOMMENTAR] = "";
				}

				String sVertreterCName = null;

				if (flrauftrag.getFlrvertreter() != null) {
					if (flrauftrag.getFlrvertreter().getFlrpartner()
							.getC_name2vornamefirmazeile2() != null) {
						sVertreterCName = flrauftrag.getFlrvertreter()
								.getFlrpartner()
								.getC_name1nachnamefirmazeile1()
								+ " "
								+ flrauftrag.getFlrvertreter().getFlrpartner()
										.getC_name2vornamefirmazeile2();
					} else {
						sVertreterCName = flrauftrag.getFlrvertreter()
								.getFlrpartner()
								.getC_name1nachnamefirmazeile1();
					}
				}

				data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_VERTRETERCNAME1] = sVertreterCName;
				if (kundeDto.getPartnerDto().getLandplzortDto() != null) {
					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_KUNDE_ORT] = kundeDto
							.getPartnerDto().getLandplzortDto().getOrtDto()
							.getCName();
				}
				data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_KOSTENSTELLENUMMER] = kostenstelleDto
						.getCNr();
				try {
					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_LIEFERTERMIN] = flrauftrag
							.getT_liefertermin();
				} catch (Exception ex) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
				}
				// AUFTRAGSEIGENSCHAFTEN
				Hashtable<?, ?> hAE = getAuftragEigenschaften(
						flrauftrag.getI_id(), theClientDto);
				if (hAE != null) {
					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_AUFTRAGEIGENSCHAFT_FA] = hAE
							.get(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_FA);
					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_AUFTRAGEIGENSCHAFT_CLUSTER] = hAE
							.get(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_CLUSTER);
					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_AUFTRAGEIGENSCHAFT_EQNR] = hAE
							.get(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_EQNR);
				}
				/**
				 * @todo wieso hat der Auftrag kein Feld Gesamtwert in
				 *       Mandantenwaehrung????? PJ 3788
				 */
				BigDecimal wechselkursMandantWaehrungZuAuftragswaehrung = null;
				if (flrauftrag
						.getF_wechselkursmandantwaehrungzuauftragswaehrung()
						.doubleValue() != 0) {
					wechselkursMandantWaehrungZuAuftragswaehrung = new BigDecimal(
							flrauftrag
									.getF_wechselkursmandantwaehrungzuauftragswaehrung()
									.doubleValue());
				} else {
					wechselkursMandantWaehrungZuAuftragswaehrung = new BigDecimal(
							1);
				}

				// wg. PJ18770 -> Wenn Auftrag angelegt, dann ist der Wert 0
				BigDecimal auftragGesamtWert = BigDecimal.ZERO;

				if (flrauftrag.getN_gesamtauftragswertinauftragswaehrung() != null) {
					auftragGesamtWert = flrauftrag
							.getN_gesamtauftragswertinauftragswaehrung()
							.divide(wechselkursMandantWaehrungZuAuftragswaehrung,
									2, BigDecimal.ROUND_HALF_EVEN);
				}

				boolean darfVerkaufspreisSehen = getTheJudgeFac().hatRecht(
						RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF,
						theClientDto);

				if (darfVerkaufspreisSehen) {
					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_WERT] = auftragGesamtWert;
				} else {
					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_WERT] = null;
				}

				if (darfVerkaufspreisSehen) {
					// den offenen wert zum stichtag berechnen
					BigDecimal bdOffenerWert = auftragGesamtWert;
					// Lieferscheinpositionen per hibernate holen
					session = factory.openSession();
					Criteria crit = session
							.createCriteria(FLRLieferscheinposition.class);
					// nur auftragsbezogene.
					crit.add(Restrictions
							.isNotNull(LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_FLRPOSITIONENSICHTAUFTRAG));
					// und nur die, die sich auf den aktuellen Auftrag beziehen.
					crit.createCriteria(
							LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_FLRPOSITIONENSICHTAUFTRAG)
							.add(Restrictions
									.eq(AuftragpositionFac.FLR_AUFTRAGPOSITIONSICHTAUFTRAG_AUFTRAG_I_ID,
											flrauftrag.getI_id()));

					List<?> resultList = crit.list();

					for (Iterator<?> iter = resultList.iterator(); iter
							.hasNext();) {
						FLRLieferscheinposition item = (FLRLieferscheinposition) iter
								.next();
						// Den LS brauchen wir auch, wegen stichtag und der
						// eventuellen waehrungsumrechnung
						FLRLieferschein lieferschein = item
								.getFlrlieferschein();
						// stichtag pruefen
						if (!lieferschein.getD_belegdatum().after(dStichtag)) {
							// preis- und mengenbehaftet?
							if (item.getN_menge() != null
									&& item.getN_nettogesamtpreisplusversteckteraufschlagminusrabatt() != null) {
								BigDecimal posWert = item
										.getN_menge()
										.multiply(
												item.getN_nettogesamtpreisplusversteckteraufschlagminusrabatt());
								// wenn auftrag und lieferschein verschiedene
								// waehrungen haben, muss umgerechnet werden
								if (!lieferschein
										.getWaehrung_c_nr_lieferscheinwaehrung()
										.equals(theClientDto
												.getSMandantenwaehrung())) {
									posWert = getBetragMalWechselkurs(
											posWert,
											Helper.getKehrwert(new BigDecimal(
													lieferschein
															.getF_wechselkursmandantwaehrungzulieferscheinwaehrung()
															.doubleValue())));
								}
								bdOffenerWert = bdOffenerWert.subtract(posWert);
							}
						}
					}

					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_WERTOFFEN] = bdOffenerWert;
				}

				// SP3197
				if (flrauftrag.getAuftragart_c_nr().equals(
						AuftragServiceFac.AUFTRAGART_ABRUF)
						&& flrauftrag.getFlrauftrag_rahmenauftrag().getC_nr() != null) {
					data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_RAHMENAUFTRAG] = flrauftrag
							.getFlrauftrag_rahmenauftrag().getC_nr();

				} else if (flrauftrag.getAuftragart_c_nr().equals(
						AuftragServiceFac.AUFTRAGART_RAHMEN)) {
					// Offener Abrufwert

					if (darfVerkaufspreisSehen) {

						BigDecimal bdOffenerRahmenWert = auftragGesamtWert;
						// Lieferscheinpositionen per hibernate holen
						session = factory.openSession();
						Criteria crit = session
								.createCriteria(FLRAuftragpositionOD.class);
						// nur auftragsbezogene.
						crit.add(Restrictions
								.isNotNull("flrauftragpositionrahmen"));
						// und nur die, die sich auf den aktuellen Auftrag
						// beziehen.
						crit.createCriteria("flrauftragpositionrahmen")
								.createCriteria("flrauftrag")
								.add(Restrictions.eq("i_id",
										flrauftrag.getI_id()));

						List<?> resultList = crit.list();

						for (Iterator<?> iter = resultList.iterator(); iter
								.hasNext();) {
							FLRAuftragpositionOD item = (FLRAuftragpositionOD) iter
									.next();
							// Den LS brauchen wir auch, wegen stichtag und der
							// eventuellen waehrungsumrechnung
							FLRAuftragOD auftrag = item.getFlrauftrag();
							// stichtag pruefen
							if (!auftrag.getT_belegdatum().after(dStichtag)) {
								// preis- und mengenbehaftet?
								if (item.getN_menge() != null
										&& item.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte() != null) {
									BigDecimal posWert = item
											.getN_menge()
											.multiply(
													item.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte());
									// wenn auftrag und lieferschein
									// verschiedene
									// waehrungen haben, muss umgerechnet werden
									if (!auftrag
											.getWaehrung_c_nr_auftragswaehrung()
											.equals(theClientDto
													.getSMandantenwaehrung())) {
										posWert = getBetragMalWechselkurs(
												posWert,
												Helper.getKehrwert(new BigDecimal(
														auftrag.getF_wechselkursmandantwaehrungzuauftragswaehrung()
																.doubleValue())));
									}
									bdOffenerRahmenWert = bdOffenerRahmenWert
											.subtract(posWert);
								}
							}
						}

						data[i][AuftragReportFac.REPORT_AUFTRAG_OFFENE_OD_RAHMENWERTOFFEN] = bdOffenerRahmenWert;

					}
				}

				i++;
			}

			mapParameter.put("P_MIT_ANGELEGTEN", new Boolean(bMitAngelegten));
			mapParameter
					.put(LPReport.P_SORTIERUNG,
							buildSortierungAuftragOffeneOhneDetails(krit,
									theClientDto));
			mapParameter.put(LPReport.P_FILTER,
					buildFilterAuftragOffene(krit, theClientDto));

			mapParameter.put("P_AUFTRAGWAEHRUNG",
					theClientDto.getSMandantenwaehrung());

			mapParameter.put(LPReport.P_SORTIERENACHKOSTENSTELLE, new Boolean(
					krit.bSortiereNachKostenstelle));
			mapParameter
					.put(LPReport.P_SORTIERENACHKUNDE,
							new Boolean(
									krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER));
			mapParameter
					.put(LPReport.P_SORTIERENACHVERTRETER,
							new Boolean(
									krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_VERTRETER));

			initJRDS(mapParameter, AuftragReportFac.REPORT_MODUL,
					AuftragReportFac.REPORT_AUFTRAG_OFFENE_OHNE_DETAILS,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);

			oPrintO = getReportPrint();

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;

		} finally {
			try {
				if (session != null)
					session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE, he);
			}
		}

		System.out.print("printAuftragOffeneOhneDetail "
				+ (System.currentTimeMillis() - l) + " ms.");
		return oPrintO;
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
	private String buildSortierungAuftragOffeneOhneDetails(
			ReportJournalKriterienDto reportJournalKriterienDtoI,
			TheClientDto theClientDto) {

		StringBuffer buff = new StringBuffer(getTextRespectUISpr(
				"lp.sortierungnach", theClientDto.getMandant(),
				theClientDto.getLocUi()));
		buff.append(" ");
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

		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_VERTRETER) {
			buff.append(
					getTextRespectUISpr("lp.vertreter",
							theClientDto.getMandant(), theClientDto.getLocUi()))
					.append(", ");
		}

		buff.append(getTextRespectUISpr("auft.auftragsnummer",
				theClientDto.getMandant(), theClientDto.getLocUi()));

		return buff.toString();
	}

	/**
	 * Verfuegbarkeitsdaten mehrerer Artikel drucken. alle mengenbehafteten
	 * Positionen, inkl. Unterstuecklistenaufloesung.
	 * 
	 * @param aIIdArtikelI
	 *            Integer
	 * @param aBdMenge
	 *            BigDecimal[]
	 * @param mapReportParameterI
	 *            Map
	 * @param theClientDto
	 *            String
	 * @return JasperPrintLP
	 * @throws EJBExceptionLP
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printWiederbeschaffung(Integer[] aIIdArtikelI,
			BigDecimal[] aBdMenge, String[] aArtikelsetType,
			Map<String, Object> mapReportParameterI, int iSortierung,
			Double dWBZWennNichtDefiniertInTagen, Integer kundenlieferdauer,
			TheClientDto theClientDto) throws EJBExceptionLP {

		JasperPrintLP oPrint = null;
		cAktuellerReport = AuftragReportFac.REPORT_AUFTRAG_WIEDERBESCHAFFUNG;

		try {
			// Die Einheit der Wiederbeschaffungszeit ist ein
			// Mandantenparameter.
			ParametermandantDto parameterDtoWBZ = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT);
			int iFaktor;
			if (parameterDtoWBZ.getCWert().equals(
					ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_KW)) {
				iFaktor = 7;

			} else if (parameterDtoWBZ.getCWert().equals(
					ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_TAGE)) {
				iFaktor = 1;
			} else {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_MANDANTPARAMETER_NICHT_ANGELEGT,
						new Exception(
								ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT
										+ " ist nicht richtig definiert"));
			}
			// Tiefe der Stuecklistenaufloeseung bestimmen
			ParametermandantDto parametermandantDto = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_AUFTRAG,
							ParameterFac.PARAMETER_AUFTRAG_ABVORKALK_STKL_AUFLOESUNG_TIEFE);
			int iStuecklisteaufloesungTiefe = ((Integer) parametermandantDto
					.getCWertAsObject()).intValue();

			// in dieser Liste werden die Daten fuer den Druck gesammelt
			List<ReportAuftragVerfuegbarkeitDto> dataList = new ArrayList<ReportAuftragVerfuegbarkeitDto>();
			// Fuer Positionen aus Unterstuecklisten gibt es noch keine
			// Reservierungen.
			AddableHashMap<Integer, BigDecimal> hmTheoretischeReservierungen = new AddableHashMap<Integer, BigDecimal>();
			for (int i = 0; i < aIIdArtikelI.length; i++) {
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKey(aIIdArtikelI[i], theClientDto);

				// zum Befuellen der Preisinformationen muss bekannt sein, ob es
				// Stuecklistenpositionen gibt
				StuecklisteInfoDto stuecklisteInfoDto = getStuecklisteFac()
						.getStrukturdatenEinesArtikels(artikelDto.getIId(),
								false, null, // in die Rekursion mit einer
								// leeren Listen einsteigen
								0, // in die Rekursion mit Ebene 0 einsteigen
								iStuecklisteaufloesungTiefe, // alle
								// Stuecklisten
								// lt. Parameter
								// aufloesen
								true, // Basis sind die Einheiten der
								// Stueckliste
								aBdMenge[i], // Basis sind n Einheiten der
								// Stueckliste
								false, // Fremdfertigung nicht aufloesen
								theClientDto);

				if (stuecklisteInfoDto.getIiAnzahlPositionen().intValue() > 0
						&& (!Helper.short2boolean(stuecklisteInfoDto
								.getBIstFremdfertigung()) || iStuecklisteaufloesungTiefe > 0)) {

					ReportAuftragVerfuegbarkeitDto verfDto = new ReportAuftragVerfuegbarkeitDto();
					StuecklisteDto stklDto = getStuecklisteFac()
							.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
									artikelDto.getIId(), theClientDto);
					if (stklDto != null
							&& stklDto.getNDefaultdurchlaufzeit() != null) {
						verfDto.setBdDefaultdurchlaufzeit(stklDto
								.getNDefaultdurchlaufzeit().divide(
										new BigDecimal(iFaktor), 2,
										RoundingMode.HALF_UP));
					}

					verfDto.setArtikelDto(artikelDto);

					verfDto.setBdMenge(aBdMenge[i]);
					verfDto.setBdBedarf(aBdMenge[i]);
					verfDto.setEinheitCNr(artikelDto.getEinheitCNr());

					verfDto.setArtikelsetType(aArtikelsetType[i]);
					// Wiederbeschaffungsinformationen des ersten Lieferanten.
					ArtikellieferantDto[] verfArtLief = getArtikelFac()
							.artikellieferantFindByArtikelIId(
									artikelDto.getIId(), theClientDto);
					if (verfArtLief.length > 0) {
						verfDto.setArtikellieferantDto(verfArtLief[0]);
					}
					dataList.add(verfDto);

					if (stuecklisteInfoDto.getIiAnzahlPositionen().intValue() > 0) {
						ArrayList<StuecklisteMitStrukturDto> alStuecklisteAufgeloest = stuecklisteInfoDto
								.getAlStuecklisteAufgeloest();
						Iterator<?> it = alStuecklisteAufgeloest.iterator();

						while (it.hasNext()) {
							final StuecklisteMitStrukturDto stkDto = (StuecklisteMitStrukturDto) it
									.next();
							StuecklistepositionDto stuecklistepositionDto = stkDto
									.getStuecklistepositionDto();
							ArtikelDto artikelDtoUnterstkl = getArtikelFac()
									.artikelFindByPrimaryKey(
											stuecklistepositionDto
													.getArtikelIId(),
											theClientDto);
							StuecklisteDto unterstklDto = getStuecklisteFac()
									.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
											artikelDtoUnterstkl.getIId(),
											theClientDto);
							ReportAuftragVerfuegbarkeitDto dto = new ReportAuftragVerfuegbarkeitDto();
							if (unterstklDto != null
									&& unterstklDto.getNDefaultdurchlaufzeit() != null) {
								dto.setBdDefaultdurchlaufzeit(unterstklDto
										.getNDefaultdurchlaufzeit().divide(
												new BigDecimal(iFaktor), 2,
												RoundingMode.HALF_UP));
							}

							dto.setIEbene(stkDto.getIEbene() + 1);

							dto.setArtikelDto(artikelDtoUnterstkl);
							dto.setBdMenge(stkDto.getStuecklistepositionDto()
									.getNMenge());
							// Da es sich hier um eine Stuecklistenposition
							// handelt,
							// gibt es zumindest aus dem Auftrag noch keine
							// Reservierung
							// Theoretisch koennte es aber schon ein Los geben -
							// aber das ist eine andere geschichte ... ;-)
							hmTheoretischeReservierungen.add(
									artikelDtoUnterstkl.getIId(), stkDto
											.getStuecklistepositionDto()
											.getNMenge());
							dto.setBdBedarf(stkDto.getStuecklistepositionDto()
									.getNMenge());
							dto.setEinheitCNr(artikelDto.getEinheitCNr());

							BigDecimal dbGesamtWBZInTagen = BigDecimal.ZERO;
							if (stkDto.getDurchlaufzeit() != null) {
								dbGesamtWBZInTagen = dbGesamtWBZInTagen
										.add(stkDto.getDurchlaufzeit());
							}

							if (artikelDtoUnterstkl.getIId() != null) {
								// Wiederbeschaffungsinformationen des ersten
								// Lieferanten.
								ArtikellieferantDto[] artLief = getArtikelFac()
										.artikellieferantFindByArtikelIId(
												artikelDtoUnterstkl.getIId(),
												theClientDto);
								if (artLief.length > 0) {
									dto.setArtikellieferantDto(artLief[0]);

									if (artLief[0].getIWiederbeschaffungszeit() != null) {
										dbGesamtWBZInTagen = dbGesamtWBZInTagen
												.add(new BigDecimal(
														artLief[0]
																.getIWiederbeschaffungszeit()
																* iFaktor));
									} else {
										dbGesamtWBZInTagen = dbGesamtWBZInTagen
												.add(new BigDecimal(
														dWBZWennNichtDefiniertInTagen));
									}

								} else {
									dbGesamtWBZInTagen = dbGesamtWBZInTagen
											.add(new BigDecimal(
													dWBZWennNichtDefiniertInTagen));
								}
							}

							dto.setDGesamtWBZ(dbGesamtWBZInTagen.doubleValue());

							dataList.add(dto);
						}
					}
				} else {
					// Eine STKL wird wie ein normaler Artikel angedruckt,
					// wenn...
					// ...es sich um eine Fremdfertigung handelt
					// ...es keine Positionen gibt
					// ...die gewuenschte Aufloesungstiefe lt. Parameter 0 ist
					StuecklisteDto stklDto = getStuecklisteFac()
							.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
									artikelDto.getIId(), theClientDto);
					ReportAuftragVerfuegbarkeitDto dto = new ReportAuftragVerfuegbarkeitDto();
					if (stklDto != null
							&& stklDto.getNDefaultdurchlaufzeit() != null) {
						dto.setBdDefaultdurchlaufzeit(stklDto
								.getNDefaultdurchlaufzeit().divide(
										new BigDecimal(iFaktor), 2,
										RoundingMode.HALF_UP));
					}
					dto.setArtikelDto(artikelDto);
					dto.setBdMenge(aBdMenge[i]);
					dto.setBdBedarf(aBdMenge[i]);
					dto.setArtikelsetType(aArtikelsetType[i]);

					dto.setEinheitCNr(artikelDto.getEinheitCNr());
					// Wiederbeschaffungsinformationen des ersten Lieferanten.
					ArtikellieferantDto[] artLief = getArtikelFac()
							.artikellieferantFindByArtikelIId(
									artikelDto.getIId(), theClientDto);
					if (artLief.length > 0) {
						dto.setArtikellieferantDto(artLief[0]);
					}
					dataList.add(dto);
				}
			}
			// Anhand der aufsummierten Bedarfe kann ich jetzt feststellen, ob
			// genug auf Lager ist.
			for (Iterator<ReportAuftragVerfuegbarkeitDto> iter = dataList
					.iterator(); iter.hasNext();) {
				ReportAuftragVerfuegbarkeitDto item = (ReportAuftragVerfuegbarkeitDto) iter
						.next();
				if (item.getArtikelDto() != null
						&& item.getArtikelDto().getIId() != null) {
					BigDecimal bdLagerstand = getLagerFac()
							.getLagerstandAllerLagerEinesMandanten(
									item.getArtikelDto().getIId(), theClientDto);
					BigDecimal bdFM = getFehlmengeFac()
							.getAnzahlFehlmengeEinesArtikels(
									item.getArtikelDto().getIId(), theClientDto);
					BigDecimal bdRes = getReservierungFac()
							.getAnzahlReservierungen(
									item.getArtikelDto().getIId(), theClientDto);
					BigDecimal bdVerfuegbar = bdLagerstand.subtract(bdFM)
							.subtract(bdRes);
					// noch die theoretischen Reservierungen abziehen.
					if (hmTheoretischeReservierungen.get(item.getArtikelDto()
							.getIId()) != null) {
						bdVerfuegbar = bdVerfuegbar
								.subtract(hmTheoretischeReservierungen.get(item
										.getArtikelDto().getIId()));
					}
					if (bdVerfuegbar.compareTo(new BigDecimal(0)) >= 0) {
						item.setBLagernd(true);
					}
				}
			}

			// Liste jetzt sortieren
			Collections.sort(dataList, new ComparatorAuftragVerfuegbarkeit());
			// jetzt die Map mit dataRows in ein Object[][] fuer den Druck
			// umwandeln
			data = new Object[dataList.size()][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_ANZAHL_SPALTEN];
			int i = 0;
			int maxWiederbeschaffungszeit = 0;
			double maxGesamtWBZ = 0;

			// DatenArrary befuellen.
			for (Iterator<ReportAuftragVerfuegbarkeitDto> iter = dataList
					.iterator(); iter.hasNext();) {
				ReportAuftragVerfuegbarkeitDto item = (ReportAuftragVerfuegbarkeitDto) iter
						.next();
				BelegPositionDruckIdentDto druckDto = printIdent(null,
						LocaleFac.BELEGART_AUFTRAG, item.getArtikelDto(),
						theClientDto.getLocUi(), theClientDto);
				data[i][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_BEZEICHNUNG] = druckDto
						.getSBezeichnung();
				data[i][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_KURZBEZEICHNUNG] = druckDto
						.getSKurzbezeichnung();
				data[i][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_IDENTNUMMER] = druckDto
						.getSIdentnummer();
				data[i][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_STKL_EBENE] = item
						.getIEbene();

				if (item.isBLagernd()) {
					data[i][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_GESAMT_WIEDERBESCHAFFUNGSZEIT] = 0D;

				} else {
					if (item.getDGesamtWBZ() != null) {
						data[i][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_GESAMT_WIEDERBESCHAFFUNGSZEIT] = new BigDecimal(
								item.getDGesamtWBZ().doubleValue()).divide(
								new BigDecimal(iFaktor), 2,
								RoundingMode.HALF_UP).doubleValue();
					} else {
						data[i][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_GESAMT_WIEDERBESCHAFFUNGSZEIT] = new BigDecimal(
								dWBZWennNichtDefiniertInTagen).divide(
								new BigDecimal(iFaktor), 2,
								RoundingMode.HALF_UP).doubleValue();
					}
				}

				if (item.getDGesamtWBZ() != null
						&& item.getDGesamtWBZ() > maxGesamtWBZ) {
					maxGesamtWBZ = item.getDGesamtWBZ();
				}

				data[i][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_EINHEIT] = item
						.getEinheitCNr();
				// Menge
				data[i][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_MENGE] = item
						.getBdMenge();
				// Bedarf
				data[i][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_BEDARF] = item
						.getBdBedarf();
				if (item.getArtikelDto() != null
						&& item.getArtikelDto().getIId() != null) {
					// aktueller Gestehungspreis.
					data[i][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_GESTEHUNGSPREIS] = getLagerFac()
							.getGemittelterGestehungspreisAllerLaegerEinesMandanten(
									item.getArtikelDto().getIId(), theClientDto);
					// aktueller Lagerstand.
					data[i][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_LAGERSTAND] = getLagerFac()
							.getLagerstandAllerLagerEinesMandanten(
									item.getArtikelDto().getIId(), false,
									theClientDto);
					// aktueller Lagerstand.
					data[i][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_LAGERSTAND_SPERRLAEGER] = getLagerFac()
							.getLagerstandAllerSperrlaegerEinesMandanten(
									item.getArtikelDto().getIId(), theClientDto);
					data[i][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_EINHEIT] = item
							.getArtikelDto().getEinheitCNr();
					// welche Menge ist bestellt?
					data[i][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_MENGE_BESTELLT] = getArtikelbestelltFac()
							.getAnzahlBestellt(item.getArtikelDto().getIId());
					// welche Menge ist als Fehlmenge eingetragen?
					data[i][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_MENGE_FEHLMENGE] = getFehlmengeFac()
							.getAnzahlFehlmengeEinesArtikels(
									item.getArtikelDto().getIId(), theClientDto);
					data[i][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_MENGE_RESERVIERT] = getReservierungFac()
							.getAnzahlReservierungen(
									item.getArtikelDto().getIId(), theClientDto);
					// Durchlaufzeit fuer Eigengefertigte Produkte. muss evtl
					// auf Wochen umgerechnet werden
					data[i][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_DURCHLAUFZEIT] = item
							.getBdDefaultdurchlaufzeit();
					data[i][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_LAGERND] = item
							.isBLagernd();
					// Wiederbeschaffungszeit des ersten Lieferanten
					if (item.getArtikellieferantDto() != null) {
						Integer wbZeit = item.getArtikellieferantDto()
								.getIWiederbeschaffungszeit();
						data[i][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_WIEDERBESCHAFFUNGSZEIT] = wbZeit;
						if (wbZeit != null
								&& wbZeit > maxWiederbeschaffungszeit) {
							maxWiederbeschaffungszeit = wbZeit;
						}
					}

					// PJ 15504

					StuecklisteDto stklDto = getStuecklisteFac()
							.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
									item.getArtikelDto().getIId(), theClientDto);
					if (stklDto != null) {
						BigDecimal bdGesamt = new BigDecimal(0);

						StuecklistearbeitsplanDto[] apDtos = getStuecklisteFac()
								.stuecklistearbeitsplanFindByStuecklisteIId(
										stklDto.getIId(), theClientDto);
						for (int j = 0; j < apDtos.length; j++) {
							bdGesamt = bdGesamt.add(Helper
									.berechneGesamtzeitInStunden(
											apDtos[j].getLRuestzeit(),
											apDtos[j].getLStueckzeit(),
											item.getBdMenge(),
											stklDto.getIErfassungsfaktor(),
											apDtos[j].getIAufspannung()));
						}
						data[i][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_SUMME_SOLLZEITEN] = bdGesamt;
					}

					// PJ 15019

					ArtikellieferantDto[] dtos = getArtikelFac()
							.artikellieferantFindByArtikelIId(
									item.getArtikelDto().getIId(), theClientDto);
					if (dtos != null && dtos.length > 0) {

						LieferantDto lfDto = getLieferantFac()
								.lieferantFindByPrimaryKey(
										dtos[0].getLieferantIId(), theClientDto);

						data[i][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_LIEFERANT] = lfDto
								.getPartnerDto().formatFixName1Name2();

						data[i][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_LIEFERANT_ARTIKELNUMMER] = dtos[0]
								.getCArtikelnrlieferant();
						data[i][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_LIEFERANT_ARTIKELBEZEICHNUNG] = dtos[0]
								.getCBezbeilieferant();

					} else {
						data[i][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_LIEFERANT] = "";
					}

					data[i][AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_SETARTIKEL_TYP] = item
							.getArtikelsetType();
				}
				i++;
			}

			if (iSortierung == SORT_REPORT_WIEDERBESCHAFFUNG_LIEFERANT) {
				for (int k = data.length - 1; k > 0; --k) {
					for (int j = 0; j < k; ++j) {
						Object[] a1 = (Object[]) data[j];
						Object[] a2 = (Object[]) data[j + 1];

						String s1 = (String) a1[AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_LIEFERANT];
						String s2 = (String) a2[AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_LIEFERANT];

						if (s1.compareTo(s2) > 0) {
							data[j] = a2;
							data[j + 1] = a1;
						} else if (s1.compareTo(s2) == 0) {

							String artikel1 = (String) a1[AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_IDENTNUMMER];
							String artikel2 = (String) a2[AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_IDENTNUMMER];

							if (artikel1 == null) {
								artikel1 = "";
							}
							if (artikel2 == null) {
								artikel2 = "";
							}

							if (artikel1.compareTo(artikel2) > 0) {

								data[j] = a2;
								data[j + 1] = a1;
							}

						}
					}

				}
			} else if (iSortierung == SORT_REPORT_WIEDERBESCHAFFUNG_GESAMT_WBZ) {
				for (int k = data.length - 1; k > 0; --k) {
					for (int j = 0; j < k; ++j) {
						Object[] a1 = (Object[]) data[j];
						Object[] a2 = (Object[]) data[j + 1];

						Double d1 = (Double) a1[AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_GESAMT_WIEDERBESCHAFFUNGSZEIT];
						Double d2 = (Double) a2[AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_GESAMT_WIEDERBESCHAFFUNGSZEIT];
						if (d1 == null) {
							d1 = 0D;
						}
						if (d2 == null) {
							d2 = 0D;
						}

						if (d1 < d2) {
							data[j] = a2;
							data[j + 1] = a1;
						} else if (d1 == d2) {

							String artikel1 = (String) a1[AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_IDENTNUMMER];
							String artikel2 = (String) a2[AuftragReportFac.REPORT_WIEDERBESCHAFFUNG_IDENTNUMMER];

							if (artikel1 == null) {
								artikel1 = "";
							}
							if (artikel2 == null) {
								artikel2 = "";
							}

							if (artikel1.compareTo(artikel2) > 0) {

								data[j] = a2;
								data[j + 1] = a1;
							}

						}
					}

				}
			}

			if (iSortierung == SORT_REPORT_WIEDERBESCHAFFUNG_LIEFERANT) {

				mapReportParameterI.put(
						"P_SORTIERUNG",
						getTextRespectUISpr("bes.lieferant",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			} else if (iSortierung == SORT_REPORT_WIEDERBESCHAFFUNG_GESAMT_WBZ) {

				mapReportParameterI.put(
						"P_SORTIERUNG",
						getTextRespectUISpr(
								"auft.wiederbeschaffung.sort.gesamtwbz",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			} else if (iSortierung == SORT_REPORT_WIEDERBESCHAFFUNG_AUFTRAGSPOSITION) {
				mapReportParameterI
						.put("P_SORTIERUNG",
								getTextRespectUISpr(
										"auft.report.wiederbeschaffung.sortierung.auftragsposition",
										theClientDto.getMandant(),
										theClientDto.getLocUi()));
			}

			// Parameter
			mapReportParameterI.put(P_MANDANTWAEHRUNG,
					theClientDto.getSMandantenwaehrung());
			mapReportParameterI.put("P_WIEDERBESCHAFFUNGSZEIT_EINHEIT",
					parameterDtoWBZ.getCWert());

			// Fruehester Liefertermin = Morgen + hoechste GeamtWBZ +
			// Kundenlieferdauer

			Date dLiefertermin = Helper.cutDate(new Date(System
					.currentTimeMillis()));
			dLiefertermin = Helper.addiereTageZuDatum(dLiefertermin, 1);
			dLiefertermin = Helper.addiereTageZuDatum(dLiefertermin,
					(int) maxGesamtWBZ);
			if (kundenlieferdauer != null) {
				dLiefertermin = Helper.addiereTageZuDatum(dLiefertermin,
						kundenlieferdauer);
			}

			mapReportParameterI.put("P_FRUEHESTER_LIEFERTERMIN", dLiefertermin);

			initJRDS(mapReportParameterI, AuftragReportFac.REPORT_MODUL,
					AuftragReportFac.REPORT_AUFTRAG_WIEDERBESCHAFFUNG,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
			oPrint = getReportPrint();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return oPrint;
	}

	private BigDecimal[] getNeuesArrayMitLaenge13() {
		BigDecimal[] bdMengen = new BigDecimal[13];
		for (int i = 0; i < bdMengen.length; i++) {
			bdMengen[i] = new BigDecimal(0);
		}
		return bdMengen;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printRollierendeplanung(Integer auftragIId,
			boolean bSortiertNachLieferant, TheClientDto theClientDto) {

		JasperPrintLP oPrint = null;
		cAktuellerReport = AuftragReportFac.REPORT_AUFTRAG_ROLLIERENDEPLANUNG;

		try {

			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(
					auftragIId);
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = factory.openSession();
			org.hibernate.Criteria crit = session
					.createCriteria(FLRAuftragReport.class);
			crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));
			crit.add(Restrictions.eq("kunde_i_id_auftragsadresse",
					auftragDto.getKundeIIdAuftragsadresse()));

			crit.add(Restrictions.eq(AuftragFac.FLR_AUFTRAG_AUFTRAGART_C_NR,
					AuftragServiceFac.AUFTRAGART_RAHMEN));
			crit.add(Restrictions.eq(AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR,
					LocaleFac.STATUS_OFFEN));
			crit.addOrder(Order.asc("t_liefertermin"));
			crit.setMaxResults(12);
			List<?> resultList = crit.list();

			Iterator<?> resultListIterator = resultList.iterator();

			ArrayList<AuftragDto> alAuftraege = new ArrayList<AuftragDto>();
			HashMap mapParameter = new HashMap();
			int p = 1;
			while (resultListIterator.hasNext()) {
				FLRAuftragReport zeile = (FLRAuftragReport) resultListIterator
						.next();
				alAuftraege.add(getAuftragFac().auftragFindByPrimaryKey(
						zeile.getI_id()));
				mapParameter.put("P_AUFTRAG" + p, zeile.getC_nr());
				mapParameter
						.put("P_AUFTRAG" + "_PROJEKT" + p, zeile.getC_bez());
				p++;
			}
			// Die Einheit der Wiederbeschaffungszeit ist ein
			// Mandantenparameter.
			ParametermandantDto parameterDtoWBZ = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT);
			int iFaktor;
			if (parameterDtoWBZ.getCWert().equals(
					ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_KW)) {
				iFaktor = 7;
			} else if (parameterDtoWBZ.getCWert().equals(
					ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_TAGE)) {
				iFaktor = 1;
			} else {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_MANDANTPARAMETER_NICHT_ANGELEGT,
						new Exception(
								ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT
										+ " ist nicht richtig definiert"));
			}
			// Tiefe der Stuecklistenaufloeseung bestimmen
			ParametermandantDto parametermandantDto = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_AUFTRAG,
							ParameterFac.PARAMETER_AUFTRAG_ABVORKALK_STKL_AUFLOESUNG_TIEFE);
			int iStuecklisteaufloesungTiefe = ((Integer) parametermandantDto
					.getCWertAsObject()).intValue();

			HashMap<Integer, BigDecimal[]> hmArtikelVerdichtetPlusMengen = new HashMap<Integer, BigDecimal[]>();

			for (int i = 0; i < alAuftraege.size(); i++) {
				AuftragpositionDto[] aPosDtos = getAuftragpositionFac()
						.auftragpositionFindByAuftrag(
								alAuftraege.get(i).getIId());

				for (int j = 0; j < aPosDtos.length; j++) {
					AuftragpositionDto aPosDto = aPosDtos[j];

					if (aPosDto.getArtikelIId() != null) {

						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(
										aPosDto.getArtikelIId(), theClientDto);
						if (!artikelDto.getArtikelartCNr().equals(
								ArtikelFac.ARTIKELART_HANDARTIKEL)) {

							// zum Befuellen der Preisinformationen muss bekannt
							// sein, ob es
							// Stuecklistenpositionen gibt
							StuecklisteInfoDto stuecklisteInfoDto = getStuecklisteFac()
									.getStrukturdatenEinesArtikels(
											artikelDto.getIId(), false, null, // in
											// die
											// Rekursion
											// mit
											// einer
											// leeren Listen einsteigen
											0, // in die Rekursion mit Ebene 0
												// einsteigen
											iStuecklisteaufloesungTiefe, // alle
											// Stuecklisten
											// lt. Parameter
											// aufloesen
											true, // Basis sind die Einheiten
											// der
											// Stueckliste
											aPosDto.getNMenge(), // Basis sind n
											// Einheiten der
											// Stueckliste
											false, // Fremdfertigung nicht
											// aufloesen
											theClientDto);

							BigDecimal[] bdMengen = getNeuesArrayMitLaenge13();
							if (hmArtikelVerdichtetPlusMengen
									.containsKey(aPosDto.getArtikelIId())) {
								bdMengen = hmArtikelVerdichtetPlusMengen
										.get(aPosDto.getArtikelIId());
							}
							bdMengen[i] = bdMengen[i].add(aPosDto.getNMenge());
							hmArtikelVerdichtetPlusMengen.put(
									aPosDto.getArtikelIId(), bdMengen);

							if (stuecklisteInfoDto.getIiAnzahlPositionen()
									.intValue() > 0
									&& (!Helper
											.short2boolean(stuecklisteInfoDto
													.getBIstFremdfertigung()) || iStuecklisteaufloesungTiefe > 0)) {

								ArrayList<StuecklisteMitStrukturDto> alStuecklisteAufgeloest = stuecklisteInfoDto
										.getAlStuecklisteAufgeloest();
								Iterator<?> it = alStuecklisteAufgeloest
										.iterator();

								while (it.hasNext()) {
									final StuecklisteMitStrukturDto stkDto = (StuecklisteMitStrukturDto) it
											.next();

									StuecklistepositionDto stuecklistepositionDto = stkDto
											.getStuecklistepositionDto();
									if (stuecklistepositionDto
											.getSHandeingabe() == null) {

										BigDecimal[] bdMengenSub = getNeuesArrayMitLaenge13();
										if (hmArtikelVerdichtetPlusMengen
												.containsKey(stuecklistepositionDto
														.getArtikelIId())) {
											bdMengenSub = hmArtikelVerdichtetPlusMengen
													.get(stuecklistepositionDto
															.getArtikelIId());
										}
										bdMengenSub[i] = bdMengenSub[i]
												.add(stuecklistepositionDto
														.getNMenge());
										hmArtikelVerdichtetPlusMengen.put(
												stuecklistepositionDto
														.getArtikelIId(),
												bdMengenSub);
									}

								}

							} else {
								BigDecimal[] bdMengenSub = getNeuesArrayMitLaenge13();
								if (hmArtikelVerdichtetPlusMengen
										.containsKey(aPosDto.getArtikelIId())) {
									bdMengenSub = hmArtikelVerdichtetPlusMengen
											.get(aPosDto.getArtikelIId());
								}
								bdMengenSub[i] = bdMengenSub[i].add(aPosDto
										.getNMenge());
								hmArtikelVerdichtetPlusMengen.put(
										aPosDto.getArtikelIId(), bdMengenSub);
							}
						}

					}
				}
			}

			data = new Object[hmArtikelVerdichtetPlusMengen.size()][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_ANZAHL_SPALTEN];
			int i = 0;
			int maxWiederbeschaffungszeit = 0;
			// Anhand der aufsummierten Bedarfe kann ich jetzt feststellen, ob
			// genug auf Lager ist.
			for (Iterator<Integer> iter = hmArtikelVerdichtetPlusMengen
					.keySet().iterator(); iter.hasNext();) {
				Integer artikelIId = (Integer) iter.next();
				BigDecimal[] bdMengen = hmArtikelVerdichtetPlusMengen
						.get(artikelIId);

				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(artikelIId, theClientDto);

				BigDecimal bdFM = getFehlmengeFac()
						.getAnzahlFehlmengeEinesArtikels(artikelIId,
								theClientDto);
				BigDecimal bdRes = getReservierungFac()
						.getAnzahlReservierungen(artikelIId, theClientDto);

				BelegPositionDruckIdentDto druckDto = printIdent(null,
						LocaleFac.BELEGART_AUFTRAG, artikelDto,
						theClientDto.getLocUi(), theClientDto);
				data[i][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_BEZEICHNUNG] = druckDto
						.getSBezeichnung();
				data[i][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_KURZBEZEICHNUNG] = druckDto
						.getSKurzbezeichnung();
				data[i][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_IDENTNUMMER] = druckDto
						.getSIdentnummer();

				data[i][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_EINHEIT] = artikelDto
						.getEinheitCNr();
				// Menge
				data[i][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE] = bdMengen[0];
				data[i][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE2] = bdMengen[1];
				data[i][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE3] = bdMengen[2];
				data[i][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE4] = bdMengen[3];
				data[i][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE5] = bdMengen[4];
				data[i][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE6] = bdMengen[5];
				data[i][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE7] = bdMengen[6];
				data[i][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE8] = bdMengen[7];
				data[i][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE9] = bdMengen[8];
				data[i][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE10] = bdMengen[9];
				data[i][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE11] = bdMengen[10];
				data[i][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE12] = bdMengen[11];
				data[i][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE13] = bdMengen[12];

				// aktueller Gestehungspreis.
				data[i][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_GESTEHUNGSPREIS] = getLagerFac()
						.getGemittelterGestehungspreisAllerLaegerEinesMandanten(
								artikelIId, theClientDto);
				// aktueller Lagerstand.
				data[i][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_LAGERSTAND] = getLagerFac()
						.getLagerstandAllerLagerEinesMandanten(artikelIId,
								false, theClientDto);
				// aktueller Lagerstand.
				data[i][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_LAGERSTAND_SPERRLAEGER] = getLagerFac()
						.getLagerstandAllerSperrlaegerEinesMandanten(
								artikelIId, theClientDto);
				data[i][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_EINHEIT] = artikelDto
						.getEinheitCNr();
				// welche Menge ist bestellt?
				data[i][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE_BESTELLT] = getArtikelbestelltFac()
						.getAnzahlBestellt(artikelDto.getIId());
				// welche Menge ist als Fehlmenge eingetragen?
				data[i][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE_FEHLMENGE] = bdFM;
				data[i][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_MENGE_RESERVIERT] = bdRes;

				// Wiederbeschaffungszeit des ersten Lieferanten
				ArtikellieferantDto[] verfArtLief = getArtikelFac()
						.artikellieferantFindByArtikelIId(artikelDto.getIId(),
								theClientDto);
				if (verfArtLief.length > 0) {
					Integer wbZeit = verfArtLief[0]
							.getIWiederbeschaffungszeit();
					data[i][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_WIEDERBESCHAFFUNGSZEIT] = wbZeit;
					if (wbZeit != null && wbZeit > maxWiederbeschaffungszeit) {
						maxWiederbeschaffungszeit = wbZeit;
					}
				}

				// PJ 15019

				ArtikellieferantDto[] dtos = getArtikelFac()
						.artikellieferantFindByArtikelIId(artikelIId,
								theClientDto);
				if (dtos != null && dtos.length > 0) {

					LieferantDto lfDto = getLieferantFac()
							.lieferantFindByPrimaryKey(
									dtos[0].getLieferantIId(), theClientDto);

					data[i][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_LIEFERANT] = lfDto
							.getPartnerDto().formatFixName1Name2();

					data[i][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_LIEFERANT_ARTIKELNUMMER] = dtos[0]
							.getCArtikelnrlieferant();
					data[i][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_LIEFERANT_ARTIKELBEZEICHNUNG] = dtos[0]
							.getCBezbeilieferant();

				} else {
					data[i][AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_LIEFERANT] = "";
				}
				i++;

			}

			if (bSortiertNachLieferant == true) {
				for (int k = data.length - 1; k > 0; --k) {
					for (int j = 0; j < k; ++j) {
						Object[] a1 = (Object[]) data[j];
						Object[] a2 = (Object[]) data[j + 1];

						String s1 = (String) a1[AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_LIEFERANT];
						String s2 = (String) a2[AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_LIEFERANT];

						if (s1.compareTo(s2) > 0) {
							data[j] = a2;
							data[j + 1] = a1;
						} else if (s1.compareTo(s2) == 0) {

							String artikel1 = (String) a1[AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_IDENTNUMMER];
							String artikel2 = (String) a2[AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_IDENTNUMMER];

							if (artikel1 == null) {
								artikel1 = "";
							}
							if (artikel2 == null) {
								artikel2 = "";
							}

							if (artikel1.compareTo(artikel2) > 0) {

								data[j] = a2;
								data[j + 1] = a1;
							}

						}
					}

				}
			} else {
				for (int k = data.length - 1; k > 0; --k) {
					for (int j = 0; j < k; ++j) {
						Object[] a1 = (Object[]) data[j];
						Object[] a2 = (Object[]) data[j + 1];

						String s1 = (String) a1[AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_IDENTNUMMER];
						String s2 = (String) a2[AuftragReportFac.REPORT_ROLLIERENDEPLANUNG_IDENTNUMMER];

						if (s1.compareTo(s2) > 0) {
							data[j] = a2;
							data[j + 1] = a1;
						}

					}

				}
			}

			mapParameter.put(
					"P_BELEG",
					getLocaleFac().uebersetzeBelegartOptimal(
							LocaleFac.BELEGART_AUFTRAG,
							theClientDto.getLocUi(),
							theClientDto.getLocMandant()).trim()
							+ " " + auftragDto.getCNr());
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					auftragDto.getKundeIIdAuftragsadresse(), theClientDto);
			AnsprechpartnerDto ansprechpartnerDto = null;
			if (auftragDto.getAnsprechparnterIId() != null) {
				ansprechpartnerDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(
								auftragDto.getAnsprechparnterIId(),
								theClientDto);
			}
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
			mapParameter.put(
					LPReport.P_ADRESSBLOCK,
					formatAdresseFuerAusdruck(kundeDto.getPartnerDto(),
							ansprechpartnerDto, mandantDto,
							theClientDto.getLocUi()));
			mapParameter.put("P_LIEFERTERMIN", auftragDto.getDLiefertermin());
			// Parameter
			mapParameter.put(P_MANDANTWAEHRUNG,
					theClientDto.getSMandantenwaehrung());
			// den fruehesten Liefertermin bestimmen.
			mapParameter.put("P_LIEFERTERMIN", auftragDto.getDLiefertermin());

			mapParameter.put("P_SORTIERTNACHLIEFERANT", new Boolean(
					bSortiertNachLieferant));

			initJRDS(mapParameter, AuftragReportFac.REPORT_MODUL,
					AuftragReportFac.REPORT_AUFTRAG_ROLLIERENDEPLANUNG,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
			oPrint = getReportPrint();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return oPrint;
	}

	/**
	 * Die Verfuegbarkeitspr&uuml;fung fuer alle Positionen eines Auftrags
	 * drucken. <br>
	 * Beruecksichtigt werden nur mengenbehaftete Positionen.
	 * 
	 * @param iIdAuftragI
	 *            PK des Auftrags
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return JasperPrint der Druck
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printVerfuegbarkeitspruefung(Integer iIdAuftragI,
			int iSortierung, Double dWBZWennNichtDefiniert,
			TheClientDto theClientDto) throws EJBExceptionLP {

		JasperPrintLP oPrint = null;
		cAktuellerReport = AuftragReportFac.REPORT_AUFTRAG_VORKALKULATION;
		Session session = null;

		try {
			session = FLRSessionFactory.getFactory().openSession();

			Criteria crit = session
					.createCriteria(FLRAuftragpositionReport.class);
			crit.add(Restrictions
					.eq(AuftragpositionFac.FLR_AUFTRAGPOSITION_FLRAUFTRAG
							+ ".i_id", iIdAuftragI));

			// Positionsart Ident
			crit.add(Restrictions.eq(
					AuftragpositionFac.FLR_AUFTRAGPOSITIONART_C_NR,
					LocaleFac.POSITIONSART_IDENT));
			// Positionen mit positiver Menge
			crit.add(Restrictions
					.isNotNull(AuftragpositionFac.FLR_AUFTRAGPOSITION_N_MENGE));
			crit.add(Restrictions.gt(
					AuftragpositionFac.FLR_AUFTRAGPOSITION_N_MENGE,
					new BigDecimal(0)));
			crit.addOrder(Order
					.asc(AuftragpositionFac.FLR_AUFTRAGPOSITION_I_SORT));

			// Liste aller Positionen, die behandelt werden sollen
			List<?> list = crit.list();
			FLRAuftragpositionReport[] aFlrauftragposition = new FLRAuftragpositionReport[list
					.size()];
			aFlrauftragposition = (FLRAuftragpositionReport[]) list
					.toArray(aFlrauftragposition);
			Integer[] aArtikel = new Integer[aFlrauftragposition.length];
			BigDecimal[] aMengen = new BigDecimal[aFlrauftragposition.length];
			String[] aArtikelsetTyp = new String[aFlrauftragposition.length];
			for (int i = 0; i < aFlrauftragposition.length; i++) {
				aArtikel[i] = aFlrauftragposition[i].getArtikel_i_id();
				aMengen[i] = aFlrauftragposition[i].getN_menge();
				aArtikelsetTyp[i] = getArtikelsetType(aFlrauftragposition[i]);
			}
			// Parameter fuer den Report
			HashMap<String, Object> mapParameter = new HashMap<String, Object>();
			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(
					iIdAuftragI);
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					auftragDto.getKundeIIdAuftragsadresse(), theClientDto);
			AnsprechpartnerDto ansprechpartnerDto = null;
			if (auftragDto.getAnsprechparnterIId() != null) {
				ansprechpartnerDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(
								auftragDto.getAnsprechparnterIId(),
								theClientDto);
			}
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
			mapParameter.put(
					"P_BELEG",
					getLocaleFac().uebersetzeBelegartOptimal(
							LocaleFac.BELEGART_AUFTRAG,
							theClientDto.getLocUi(),
							theClientDto.getLocMandant()).trim()
							+ " " + auftragDto.getCNr());
			mapParameter.put(
					LPReport.P_ADRESSBLOCK,
					formatAdresseFuerAusdruck(kundeDto.getPartnerDto(),
							ansprechpartnerDto, mandantDto,
							theClientDto.getLocUi()));
			mapParameter.put("P_LIEFERTERMIN", auftragDto.getDLiefertermin());

			oPrint = printWiederbeschaffung(aArtikel, aMengen, aArtikelsetTyp,
					mapParameter, iSortierung, dWBZWennNichtDefiniert,
					kundeDto.getILieferdauer(), theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} finally {
			try {
				if (session != null)
					session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE, he);
			}
		}
		return oPrint;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printErfuellungsjournal(
			Integer auftragIIdRahmenauftrag, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, TheClientDto theClientDto) {
		cAktuellerReport = AuftragReportFac.REPORT_AUFTRAG_ERFUELLUNGSJOURNAL;

		HashMap parameter = new HashMap();

		parameter.put("P_VON", tVon);
		parameter.put("P_BIS", tBis);

		tBis = Helper.cutTimestamp(new java.sql.Timestamp(
				tBis.getTime() + 24 * 3600000));

		AuftragDto auftragDto = null;
		try {
			auftragDto = getAuftragFac().auftragFindByPrimaryKey(
					auftragIIdRahmenauftrag);

			parameter.put("P_AUFTRAG", auftragDto.getCNr());

			AuftragpositionDto[] posDtos = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(auftragIIdRahmenauftrag);

			HashMap hmStuecklisten = new HashMap();
			// Zuerst Positionen verdichten
			for (int i = 0; i < posDtos.length; i++) {
				AuftragpositionDto posDto = posDtos[i];
				if (posDto.getArtikelIId() != null
						&& posDto.getNMenge().doubleValue() != 0) {
					StuecklisteDto stklDto = getStuecklisteFac()
							.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
									posDto.getArtikelIId(), theClientDto);
					if (stklDto != null) {
						if (hmStuecklisten.containsKey(stklDto.getIId())) {
							AuftragpositionDto posDtoTemp = (AuftragpositionDto) hmStuecklisten
									.get(stklDto.getIId());
							// Preis neu berechnen
							BigDecimal wertBisher = posDtoTemp
									.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()
									.multiply(posDtoTemp.getNMenge());

							BigDecimal wertNeu = posDto
									.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()
									.multiply(posDto.getNMenge());

							posDtoTemp
									.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(wertBisher
											.add(wertNeu)
											.divide(posDtoTemp.getNMenge().add(
													posDto.getNMenge()), 4,
													BigDecimal.ROUND_HALF_EVEN));

							// Menge neu berechnen
							posDtoTemp.setNMenge(posDtoTemp.getNMenge().add(
									posDto.getNMenge()));

						} else {
							hmStuecklisten.put(stklDto.getIId(), posDto);
						}
					}
				}
			}

			TreeMap hmPositionenVerdichtet = new TreeMap();

			Iterator itStuecklisten = hmStuecklisten.keySet().iterator();

			while (itStuecklisten.hasNext()) {

				Integer stuecklisteIId = (Integer) itStuecklisten.next();

				AuftragpositionDto posDto = (AuftragpositionDto) hmStuecklisten
						.get(stuecklisteIId);

				StuecklisteDto stklDto = getStuecklisteFac()
						.stuecklisteFindByPrimaryKey(stuecklisteIId,
								theClientDto);

				Object[] oKopfZeile = new Object[AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_ANZAHL_SPALTEN];

				ArtikelDto artikelDtoKopf = getArtikelFac()
						.artikelFindByPrimaryKeySmall(posDto.getArtikelIId(),
								theClientDto);

				oKopfZeile[AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_ARTIKELNUMMER] = artikelDtoKopf
						.getCNr();
				oKopfZeile[AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_ARTIKELBEZEICHNUNG] = artikelDtoKopf
						.formatBezeichnung();
				oKopfZeile[AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_RAHMEN_GEPLANT] = posDto
						.getNMenge();
				oKopfZeile[AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_VKPREIS] = posDto
						.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte();

				hmPositionenVerdichtet.put(artikelDtoKopf.getIId(), oKopfZeile);

				List<?> m = null;
				try {
					m = getStuecklisteFac()
							.getStrukturDatenEinerStuecklisteMitArbeitsplan(
									stklDto.getIId(),
									theClientDto,
									StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_ARTIKELNR,
									0, null, true, true, posDto.getNMenge(),
									null);
				} catch (RemoteException ex4) {
					throwEJBExceptionLPRespectOld(ex4);
				}

				Iterator<?> it = m.listIterator();

				while (it.hasNext()) {

					StuecklisteMitStrukturDto struktur = (StuecklisteMitStrukturDto) it
							.next();

					// Wenn Position
					if (!struktur.isBArbeitszeit()) {
						StuecklistepositionDto position = struktur
								.getStuecklistepositionDto();

						if (position.getArtikelIId() != null) {

							Object[] oZeile = new Object[AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_ANZAHL_SPALTEN];

							oZeile[AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_RAHMEN_GEPLANT] = Helper
									.rundeKaufmaennisch(
											position.getNZielmenge().multiply(
													posDto.getNMenge()), 4);

							if (hmPositionenVerdichtet.containsKey(position
									.getArtikelIId())) {

								Object[] oZeileTemp = (Object[]) hmPositionenVerdichtet
										.get(position.getArtikelIId());

								BigDecimal mengeBisher = (BigDecimal) oZeileTemp[AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_RAHMEN_GEPLANT];

								BigDecimal mengeNeu = (BigDecimal) oZeile[AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_RAHMEN_GEPLANT];

								oZeile[AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_RAHMEN_GEPLANT] = mengeBisher
										.add(mengeNeu);

								/*
								 * // Preis neu berechnen BigDecimal wertBisher
								 * = ((BigDecimal) oZeileTemp[AuftragReportFac.
								 * REPORT_ERFUELLUNGSJOURNAL_VKPREIS])
								 * .multiply(mengeBisher);
								 * 
								 * BigDecimal wertNeu = ((BigDecimal)
								 * oZeile[AuftragReportFac
								 * .REPORT_ERFUELLUNGSJOURNAL_VKPREIS])
								 * .multiply(mengeNeu);
								 * 
								 * 
								 * 
								 * 
								 * 
								 * BigDecimal
								 * neuerVKPreis=wertBisher.add(wertNeu
								 * ).divide(mengeBisher.add(mengeNeu),4,
								 * BigDecimal.ROUND_HALF_EVEN);
								 * oZeile[AuftragReportFac
								 * .REPORT_ERFUELLUNGSJOURNAL_RAHMEN_GEPLANT
								 * ]=neuerVKPreis;
								 */

								hmPositionenVerdichtet.put(
										position.getArtikelIId(), oZeile);

							} else {
								hmPositionenVerdichtet.put(
										position.getArtikelIId(), oZeile);
							}

						}
					}
				}

			}

			Iterator itListeVerdichtet = hmPositionenVerdichtet.keySet()
					.iterator();
			int i = 0;
			data = new Object[hmPositionenVerdichtet.size()][AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_ANZAHL_SPALTEN];
			while (itListeVerdichtet.hasNext()) {

				Integer artikelIId = (Integer) itListeVerdichtet.next();

				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(artikelIId, theClientDto);

				Object[] oZeile = (Object[]) hmPositionenVerdichtet
						.get(artikelIId);

				oZeile[AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_ARTIKELNUMMER] = artikelDto
						.getCNr();
				oZeile[AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_ARTIKELBEZEICHNUNG] = artikelDto
						.formatBezeichnung();

				oZeile[AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_GELIEFERT] = getLagerFac()
						.getVerbrauchteMengeEinesArtikels(artikelDto.getIId(),
								tVon, tBis, theClientDto);

				oZeile[AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_ABGELIEFERT] = getLagerFac()
						.getLosablieferungenEinesArtikels(artikelDto.getIId(),
								tVon, tBis, theClientDto);

				// eingekauft
				Session session = FLRSessionFactory.getFactory().openSession();
				// PJ 14006
				org.hibernate.Criteria crit = session
						.createCriteria(FLRLagerbewegung.class)
						.createAlias("flrartikel", "a")
						.add(Restrictions.eq("a.i_id", artikelIId))
						.createAlias("flrlager", "l")
						.add(Restrictions.eq("l.mandant_c_nr",
								theClientDto.getMandant()));
				crit.add(Restrictions.ge(
						LagerFac.FLR_LAGERBEWEGUNG_T_BELEGDATUM, tVon));
				crit.add(Restrictions.le(
						LagerFac.FLR_LAGERBEWEGUNG_T_BELEGDATUM, tBis));
				crit.add(Restrictions.eq(LagerFac.FLR_LAGERBEWEGUNG_B_ABGANG,
						Helper.boolean2Short(false)));
				crit.add(Restrictions.eq(LagerFac.FLR_LAGERBEWEGUNG_B_HISTORIE,
						Helper.boolean2Short(false)));

				crit.add(Restrictions.in(
						LagerFac.FLR_LAGERBEWEGUNG_C_BELEGARTNR,
						new String[] { LocaleFac.BELEGART_BESTELLUNG }));

				List<?> results = crit.list();
				Iterator<?> resultListIterator = results.iterator();

				BigDecimal bdMengeEingekauft = new BigDecimal(0);
				BigDecimal bdWertEingekauft = new BigDecimal(0);

				while (resultListIterator.hasNext()) {
					FLRLagerbewegung lagerbewegung = (FLRLagerbewegung) resultListIterator
							.next();
					if (lagerbewegung.getN_menge().doubleValue() > 0) {
						bdMengeEingekauft = bdMengeEingekauft.add(lagerbewegung
								.getN_menge());
						bdWertEingekauft = bdWertEingekauft.add(lagerbewegung
								.getN_menge().multiply(
										lagerbewegung.getN_einstandspreis()));
					}

				}

				if (bdMengeEingekauft.doubleValue() != 0) {
					oZeile[AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_EINSTANDSPREIS] = bdWertEingekauft
							.divide(bdMengeEingekauft, 4,
									BigDecimal.ROUND_HALF_UP);
				}

				session.close();

				oZeile[AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_EINGEKAUFT] = bdMengeEingekauft;

				BigDecimal fiktiverLagerstand = getInternebestellungFac()
						.getFiktivenLagerstandZuZeitpunkt(artikelDto,
								theClientDto, tBis);
				BigDecimal lagerstandZumBis = getLagerFac()
						.getLagerstandZumZeitpunkt(artikelDto.getIId(), null,
								tBis, theClientDto);
				oZeile[AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_LAGERSTAND_ZUM_BIS_ZEITPUNKT] = lagerstandZumBis;

				oZeile[AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_VERFUEGBAR_ZUM_BIS_ZEITPUNKT] = fiktiverLagerstand;

				oZeile[AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_GESTPREIS] = getLagerFac()
						.getGemittelterGestehungspreisAllerLaegerEinesMandanten(
								artikelIId, theClientDto);

				data[i] = oZeile;

				i++;
			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		// Nach Artikelnummer sortieren
		for (int k = data.length - 1; k > 0; --k) {
			for (int j = 0; j < k; ++j) {
				Object[] a1 = (Object[]) data[j];
				Object[] a2 = (Object[]) data[j + 1];

				String s1 = (String) a1[AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_ARTIKELNUMMER];
				String s2 = (String) a2[AuftragReportFac.REPORT_ERFUELLUNGSJOURNAL_ARTIKELNUMMER];

				if (s1.compareTo(s2) > 0) {
					data[j] = a2;
					data[j + 1] = a1;
				}
			}
		}

		initJRDS(parameter, AuftragReportFac.REPORT_MODUL,
				AuftragReportFac.REPORT_AUFTRAG_ERFUELLUNGSJOURNAL,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAuftragAlle(ReportJournalKriterienDto kritDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		JasperPrintLP oPrint = null;
		cAktuellerReport = AuftragReportFac.REPORT_AUFTRAG_ALLE;
		Session session = null;
		session = FLRSessionFactory.getFactory().openSession();
		// Filter und Sortierung
		Criteria crit = session.createCriteria(FLRAuftragReport.class);
		// Filter nach Mandant
		crit.add(Restrictions.eq(AuftragFac.FLR_AUFTRAG_MANDANT_C_NR,
				theClientDto.getMandant()));
		if (kritDtoI.kostenstelleIId != null) {
			crit.add(Restrictions.eq(AuftragFac.FLR_AUFTRAG_KOSTENSTELLE_I_ID,
					kritDtoI.kostenstelleIId));
		}
		if (kritDtoI.bSortiereNachKostenstelle) {
			crit.addOrder(Order.asc(AuftragFac.FLR_AUFTRAG_KOSTENSTELLE_I_ID));
		}
		if (kritDtoI.dVon != null) {
			crit.add(Restrictions.ge(AuftragFac.FLR_AUFTRAG_D_BELEGDATUM,
					kritDtoI.dVon));
		}
		if (kritDtoI.dBis != null) {
			crit.add(Restrictions.le(AuftragFac.FLR_AUFTRAG_D_BELEGDATUM,
					kritDtoI.dBis));
		}
		if (kritDtoI.sBelegnummerVon != null) {
			crit.add(Restrictions.ge(AuftragFac.FLR_AUFTRAG_C_NR,
					kritDtoI.sBelegnummerVon));
		}
		if (kritDtoI.sBelegnummerBis != null) {
			crit.add(Restrictions.le(AuftragFac.FLR_AUFTRAG_C_NR,
					kritDtoI.sBelegnummerBis));
		}

		// Einschraenkung nach einem bestimmten Vertreter
		if (kritDtoI.vertreterIId != null) {
			crit.add(Restrictions.eq(AuftragFac.FLR_AUFTRAG_VERTRETER_I_ID,
					kritDtoI.vertreterIId));
		}

		if (kritDtoI.kundeIId != null) {
			crit.add(Restrictions.eq(
					AuftragFac.FLR_AUFTRAG_KUNDE_I_ID_AUFTRAGSADRESSE,
					kritDtoI.kundeIId));
		}
		if (kritDtoI.bSortiereNachIdent) {
			crit.addOrder(Order.asc(AuftragFac.FLR_AUFTRAG_C_NR));
		}
		if (kritDtoI.bSortiereNachPersonal) {
			crit.addOrder(Order
					.asc(AuftragFac.FLR_AUFTRAG_KUNDE_I_ID_AUFTRAGSADRESSE));
		}
		if (kritDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_VERTRETER) {
			crit.createCriteria(AuftragFac.FLR_AUFTRAG_FLRVERTRETER).addOrder(
					Order.asc(PersonalFac.FLR_PERSONAL_C_KURZZEICHEN));
			crit.addOrder(Order
					.asc(AuftragFac.FLR_AUFTRAG_KUNDE_I_ID_AUFTRAGSADRESSE));
		}

		crit.addOrder(Order.asc(AuftragFac.FLR_AUFTRAG_C_NR));

		boolean darfVerkaufspreisSehen = getTheJudgeFac().hatRecht(
				RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF, theClientDto);

		// Liste holen
		List<FLRAuftragReport> list = crit.list();

		FLRAuftragReport[] aFLRAuftragReport = new FLRAuftragReport[list.size()];
		aFLRAuftragReport = list.toArray(aFLRAuftragReport);
		data = new Object[aFLRAuftragReport.length][AuftragReportFac.REPORT_ALLE_ANZAHL_SPALTEN];
		for (int i = 0; i < aFLRAuftragReport.length; i++) {
			data[i][AuftragReportFac.REPORT_ALLE_CNR] = aFLRAuftragReport[i]
					.getC_nr();
			data[i][AuftragReportFac.REPORT_ALLE_KUNDE] = aFLRAuftragReport[i]
					.getFlrkunde().getFlrpartner()
					.getC_name1nachnamefirmazeile1();
			data[i][AuftragReportFac.REPORT_ALLE_KOSTENSTELLE] = aFLRAuftragReport[i]
					.getFlrkostenstelle().getC_nr();
			data[i][AuftragReportFac.REPORT_ALLE_DATUM] = aFLRAuftragReport[i]
					.getT_belegdatum();
			if (aFLRAuftragReport[i].getFlrvertreter() != null) {
				data[i][AuftragReportFac.REPORT_ALLE_VERTRETER] = aFLRAuftragReport[i]
						.getFlrvertreter().getC_kurzzeichen();
			}
			data[i][AuftragReportFac.REPORT_ALLE_STATUS] = aFLRAuftragReport[i]
					.getAuftragstatus_c_nr();
			if (darfVerkaufspreisSehen) {
				data[i][AuftragReportFac.REPORT_ALLE_WERT] = aFLRAuftragReport[i]
						.getN_gesamtauftragswertinauftragswaehrung();
			} else {
				data[i][AuftragReportFac.REPORT_ALLE_WERT] = null;
			}

			data[i][AuftragReportFac.REPORT_ALLE_LAENDERART] = getFinanzServiceFac()
					.getLaenderartZuPartner(
							aFLRAuftragReport[i].getFlrkunde().getFlrpartner()
									.getI_id(), theClientDto);

			data[i][AuftragReportFac.REPORT_ALLE_AUFTRAGART] = aFLRAuftragReport[i]
					.getAuftragart_c_nr();
			data[i][AuftragReportFac.REPORT_ALLE_PROJEKT] = aFLRAuftragReport[i]
					.getC_bez();
			data[i][AuftragReportFac.REPORT_ALLE_WAEHRUNG] = aFLRAuftragReport[i]
					.getWaehrung_c_nr_auftragswaehrung();
			data[i][AuftragReportFac.REPORT_ALLE_KURS] = aFLRAuftragReport[i]
					.getF_wechselkursmandantwaehrungzuauftragswaehrung();
			data[i][AuftragReportFac.REPORT_ALLE_BESTELLNUMMER] = aFLRAuftragReport[i]
					.getC_bestellnummer();
			data[i][AuftragReportFac.REPORT_ALLE_BESTELLTERMIN] = aFLRAuftragReport[i]
					.getT_bestelldatum();
			data[i][AuftragReportFac.REPORT_ALLE_LIEFERTERMIN] = aFLRAuftragReport[i]
					.getT_liefertermin();
			data[i][AuftragReportFac.REPORT_ALLE_FINALTERMIN] = aFLRAuftragReport[i]
					.getT_finaltermin();
			if (aFLRAuftragReport[i].getB_poenale() != null) {
				data[i][AuftragReportFac.REPORT_ALLE_POENALE] = Helper
						.short2Boolean(aFLRAuftragReport[i].getB_poenale());
			} else {
				data[i][AuftragReportFac.REPORT_ALLE_POENALE] = new Boolean(
						false);
			}
			if (aFLRAuftragReport[i].getB_rohs() != null) {
				data[i][AuftragReportFac.REPORT_ALLE_ROHS] = Helper
						.short2Boolean(aFLRAuftragReport[i].getB_rohs());
			} else {
				data[i][AuftragReportFac.REPORT_ALLE_ROHS] = new Boolean(false);
			}
			if (aFLRAuftragReport[i].getB_teillieferungmoeglich() != null) {
				data[i][AuftragReportFac.REPORT_ALLE_TEILLIEFERBAR] = Helper
						.short2Boolean(aFLRAuftragReport[i]
								.getB_teillieferungmoeglich());
			} else {
				data[i][AuftragReportFac.REPORT_ALLE_TEILLIEFERBAR] = new Boolean(
						false);
			}
			if (aFLRAuftragReport[i].getB_lieferterminunverbindlich() != null) {
				data[i][AuftragReportFac.REPORT_ALLE_UNVERBINDLICH] = Helper
						.short2Boolean(aFLRAuftragReport[i]
								.getB_lieferterminunverbindlich());
			} else {
				data[i][AuftragReportFac.REPORT_ALLE_UNVERBINDLICH] = new Boolean(
						false);
			}
			if (aFLRAuftragReport[i].getFlrkundeansprechpartner() != null) {
				if (aFLRAuftragReport[i].getFlrkundeansprechpartner()
						.getFlrpartneransprechpartner() != null) {
					String sAnsprechpartner = "";
					sAnsprechpartner = sAnsprechpartner
							.concat(aFLRAuftragReport[i]
									.getFlrkundeansprechpartner()
									.getFlrpartneransprechpartner()
									.getAnrede_c_nr() != null ? (aFLRAuftragReport[i]
									.getFlrkundeansprechpartner()
									.getFlrpartneransprechpartner()
									.getAnrede_c_nr().trim() + " ")
									: "");
					sAnsprechpartner = sAnsprechpartner
							.concat(aFLRAuftragReport[i]
									.getFlrkundeansprechpartner()
									.getFlrpartneransprechpartner()
									.getC_name2vornamefirmazeile2() != null ? (aFLRAuftragReport[i]
									.getFlrkundeansprechpartner()
									.getFlrpartneransprechpartner()
									.getC_name2vornamefirmazeile2().trim() + " ")
									: "");
					sAnsprechpartner = sAnsprechpartner
							.concat(aFLRAuftragReport[i]
									.getFlrkundeansprechpartner()
									.getFlrpartneransprechpartner()
									.getC_name1nachnamefirmazeile1() != null ? aFLRAuftragReport[i]
									.getFlrkundeansprechpartner()
									.getFlrpartneransprechpartner()
									.getC_name1nachnamefirmazeile1().trim()
									: "");
					data[i][AuftragReportFac.REPORT_ALLE_ANSPRECHPARTNER] = sAnsprechpartner;
				} else {
					data[i][AuftragReportFac.REPORT_ALLE_ANSPRECHPARTNER] = "";
				}
			} else {
				data[i][AuftragReportFac.REPORT_ALLE_ANSPRECHPARTNER] = "";
			}
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					aFLRAuftragReport[i].getKunde_i_id_auftragsadresse(),
					theClientDto);
			PartnerDto partnerAdresseDto = null;
			if (kundeDto != null) {
				partnerAdresseDto = getPartnerFac().partnerFindByPrimaryKey(
						kundeDto.getPartnerIId(), theClientDto);
			}
			if (partnerAdresseDto != null) {
				data[i][AuftragReportFac.REPORT_ALLE_ADRESSE] = partnerAdresseDto
						.formatAdresse();
			} else {
				data[i][AuftragReportFac.REPORT_ALLE_ADRESSE] = null;
			}
			KundeDto kundeLieferDto = getKundeFac().kundeFindByPrimaryKey(
					aFLRAuftragReport[i].getKunde_i_id_lieferadresse(),
					theClientDto);
			PartnerDto partnerLieferDto = null;
			if (kundeLieferDto != null) {
				partnerLieferDto = getPartnerFac().partnerFindByPrimaryKey(
						kundeLieferDto.getPartnerIId(), theClientDto);
			}
			if (partnerLieferDto != null) {
				data[i][AuftragReportFac.REPORT_ALLE_LIEFERADRESSE] = partnerLieferDto
						.formatAdresse();
			} else {
				data[i][AuftragReportFac.REPORT_ALLE_LIEFERADRESSE] = null;
			}
			KundeDto kundeRechnungDto = getKundeFac().kundeFindByPrimaryKey(
					aFLRAuftragReport[i].getKunde_i_id_rechnungsadresse(),
					theClientDto);
			PartnerDto partnerRechnungDto = null;
			if (kundeRechnungDto != null) {
				partnerRechnungDto = getPartnerFac().partnerFindByPrimaryKey(
						kundeRechnungDto.getPartnerIId(), theClientDto);
			}
			if (partnerRechnungDto != null) {
				data[i][AuftragReportFac.REPORT_ALLE_RECHNUNGSADRESSE] = partnerRechnungDto
						.formatAdresse();
			} else {
				data[i][AuftragReportFac.REPORT_ALLE_RECHNUNGSADRESSE] = null;
			}

		}
		session.close();
		HashMap<String, Object> mapParameter = new HashMap<String, Object>();
		mapParameter.put("P_MANDANTWAEHRUNG",
				theClientDto.getSMandantenwaehrung());
		mapParameter.put(
				"P_TITLE",
				getTextRespectUISpr("auft.print.alleauftraege",
						theClientDto.getMandant(), theClientDto.getLocUi()));
		mapParameter.put("P_SORTIERENACHKOSTENSTELLE", new Boolean(
				kritDtoI.bSortiereNachKostenstelle));
		mapParameter.put("P_SORTIERENACHKUNDE", new Boolean(
				kritDtoI.bSortiereNachPersonal));
		mapParameter.put(LPReport.P_SORTIERUNG,
				buildSortierungAuftragJournal(kritDtoI, theClientDto));
		mapParameter.put(LPReport.P_FILTER,
				buildFilterAuftragJournal(kritDtoI, theClientDto));
		initJRDS(mapParameter, AuftragReportFac.REPORT_MODUL,
				AuftragReportFac.REPORT_AUFTRAG_ALLE,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		oPrint = getReportPrint();

		return oPrint;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printTaetigkeitsstatistik(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, PartnerklasseDto partnerklasse,
			TheClientDto theClientDto) {
		cAktuellerReport = AuftragReportFac.REPORT_TAETIGKEITSSTATISTIK;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		tVon = Helper.cutTimestamp(tVon);
		tBis = Helper.cutTimestamp(tBis);

		// die Parameter dem Report uebergeben
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put(JP_VON, tVon);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tBis.getTime());
		c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);

		parameter.put(JP_BIS, new java.sql.Timestamp(c.getTimeInMillis()));
		org.hibernate.Criteria crit = session
				.createCriteria(FLRAuftragReport.class);
		crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));
		// crit.add(Restrictions.eq(", value))
		crit.add(Restrictions.not(Restrictions.eq("auftragstatus_c_nr",
				LocaleFac.STATUS_STORNIERT)));

		crit.add(Restrictions.ge(AuftragFac.FLR_AUFTRAG_T_ERLEDIGT, tVon));
		crit.add(Restrictions.lt(AuftragFac.FLR_AUFTRAG_T_ERLEDIGT, tBis));

		crit.createAlias(AuftragFac.FLR_AUFTRAG_FLRKUNDE, "t");
		crit.createAlias("t." + KundeFac.FLR_PARTNER, "u");

		/**
		 * Die Partnerklasse ist optional im Client erfassbar
		 */
		if (null != partnerklasse) {
			crit.add(Restrictions.eq("u.partnerklasse_i_id",
					partnerklasse.getIId()));
			parameter.put(JP_PARTNERKLASSE, partnerklasse.getCNr());
		}

		crit.add(Restrictions.not(Restrictions.eq("auftragart_c_nr",
				AuftragServiceFac.AUFTRAGART_RAHMEN)));

		crit.addOrder(Order.asc("c_nr"));

		List<?> resultList = crit.list();

		Iterator<?> resultListIterator = resultList.iterator();

		ArrayList<Object[]> daten = new ArrayList<Object[]>();

		while (resultListIterator.hasNext()) {
			FLRAuftragReport zeile = (FLRAuftragReport) resultListIterator
					.next();

			try {

				AuftragzeitenDto[] azDtos = getZeiterfassungFac()
						.getAllZeitenEinesBeleges(LocaleFac.BELEGART_AUFTRAG,
								zeile.getI_id(), null, null, null, null, true,
								false, theClientDto);

				AuftragpositionDto[] aPositionDtos = getAuftragpositionFac()
						.auftragpositionFindByAuftrag(zeile.getI_id());

				HashMap hmZeilen = new HashMap();

				for (int i = 0; i < aPositionDtos.length; i++) {
					if (aPositionDtos[i].getArtikelIId() != null) {
						StuecklisteDto stuecklisteDto = getStuecklisteFac()
								.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
										aPositionDtos[i].getArtikelIId(),
										theClientDto);
						if (stuecklisteDto != null
								&& aPositionDtos[i].getNMenge() != null) {
							StuecklistearbeitsplanDto[] sollDtos = getStuecklisteFac()
									.stuecklistearbeitsplanFindByStuecklisteIId(
											stuecklisteDto.getIId(),
											theClientDto);

							if (sollDtos.length > 0) {

								for (int j = 0; j < sollDtos.length; j++) {

									BigDecimal bdSoll = Helper
											.berechneGesamtzeitInStunden(
													sollDtos[j].getLRuestzeit(),
													sollDtos[j]
															.getLStueckzeit(),
													aPositionDtos[i]
															.getNMenge(),
													stuecklisteDto
															.getIErfassungsfaktor(),
													sollDtos[j]
															.getIAufspannung());

									if (hmZeilen.containsKey(sollDtos[j]
											.getArtikelIId())) {
										Object[] oZeile = (Object[]) hmZeilen
												.get(sollDtos[j]
														.getArtikelIId());

										ArtikelDto artikelDtoPosition = getArtikelFac()
												.artikelFindByPrimaryKeySmall(
														aPositionDtos[i]
																.getArtikelIId(),
														theClientDto);

										oZeile[REPORT_TAETIGKEITSSTATISTIK_WEITERE_ARTIKELNUMMERN] = oZeile[REPORT_TAETIGKEITSSTATISTIK_WEITERE_ARTIKELNUMMERN]
												+ artikelDtoPosition.getCNr()
												+ ", ";

										oZeile[REPORT_TAETIGKEITSSTATISTIK_AZ_SOLL] = ((BigDecimal) oZeile[REPORT_TAETIGKEITSSTATISTIK_AZ_SOLL])
												.add(bdSoll);
										hmZeilen.put(
												sollDtos[j].getArtikelIId(),
												oZeile);
									} else {
										Object[] oZeile = new Object[REPORT_TAETIGKEITSSTATISTIK_ANZAHL_SPALTEN];

										ArtikelDto artikelDto = getArtikelFac()
												.artikelFindByPrimaryKeySmall(
														sollDtos[j]
																.getArtikelIId(),
														theClientDto);

										oZeile[REPORT_TAETIGKEITSSTATISTIK_AZ_ARTIKEL] = artikelDto
												.getCNr();
										oZeile[REPORT_TAETIGKEITSSTATISTIK_AZ_ARTIKELBEZ] = artikelDto
												.formatBezeichnung();
										oZeile[REPORT_TAETIGKEITSSTATISTIK_AZ_SOLL] = bdSoll;
										oZeile[REPORT_TAETIGKEITSSTATISTIK_PROJEKT] = zeile
												.getC_bez();
										oZeile[REPORT_TAETIGKEITSSTATISTIK_AUFTRAGSNUMMER] = zeile
												.getC_nr();

										oZeile[REPORT_TAETIGKEITSSTATISTIK_KUNDE] = HelperServer
												.formatAdresseEinesFLRPartner(zeile
														.getFlrkunde()
														.getFlrpartner());

										ArtikelDto artikelDtoPosition = getArtikelFac()
												.artikelFindByPrimaryKeySmall(
														aPositionDtos[i]
																.getArtikelIId(),
														theClientDto);

										oZeile[REPORT_TAETIGKEITSSTATISTIK_ARTIKELNUMMER] = artikelDtoPosition
												.getCNr();
										oZeile[REPORT_TAETIGKEITSSTATISTIK_WEITERE_ARTIKELNUMMERN] = "";
										hmZeilen.put(
												sollDtos[j].getArtikelIId(),
												oZeile);
									}

								}

								// Artikel aus Istzeitdaten nachtragen, falls
								// noch nicht vorhanden
								for (int k = 0; k < azDtos.length; k++) {
									if (!hmZeilen.containsKey(azDtos[k]
											.getArtikelIId())) {
										Object[] oZeile = new Object[REPORT_TAETIGKEITSSTATISTIK_ANZAHL_SPALTEN];

										ArtikelDto artikelDto = getArtikelFac()
												.artikelFindByPrimaryKeySmall(
														azDtos[k]
																.getArtikelIId(),
														theClientDto);

										oZeile[REPORT_TAETIGKEITSSTATISTIK_AZ_ARTIKEL] = artikelDto
												.getCNr();
										oZeile[REPORT_TAETIGKEITSSTATISTIK_AZ_ARTIKELBEZ] = artikelDto
												.formatBezeichnung();
										oZeile[REPORT_TAETIGKEITSSTATISTIK_AZ_SOLL] = new BigDecimal(
												0);
										oZeile[REPORT_TAETIGKEITSSTATISTIK_PROJEKT] = zeile
												.getC_bez();
										oZeile[REPORT_TAETIGKEITSSTATISTIK_AUFTRAGSNUMMER] = zeile
												.getC_nr();

										oZeile[REPORT_TAETIGKEITSSTATISTIK_KUNDE] = HelperServer
												.formatAdresseEinesFLRPartner(zeile
														.getFlrkunde()
														.getFlrpartner());

										ArtikelDto artikelDtoPosition = getArtikelFac()
												.artikelFindByPrimaryKeySmall(
														aPositionDtos[i]
																.getArtikelIId(),
														theClientDto);

										oZeile[REPORT_TAETIGKEITSSTATISTIK_ARTIKELNUMMER] = artikelDtoPosition
												.getCNr();
										oZeile[REPORT_TAETIGKEITSSTATISTIK_WEITERE_ARTIKELNUMMERN] = "";
										hmZeilen.put(azDtos[k].getArtikelIId(),
												oZeile);
									}
								}

							}
						}
					}
				}

				// Ist-Zeiten eintragen
				Iterator it = hmZeilen.keySet().iterator();

				while (it.hasNext()) {

					Integer artikelIId = (Integer) it.next();

					BigDecimal gesamtzeitEinesArtikels = new BigDecimal(0);
					BigDecimal gesamtkostenEinesArtikels = new BigDecimal(0);
					for (int k = 0; k < azDtos.length; k++) {

						if (artikelIId.equals(azDtos[k].getArtikelIId())) {
							gesamtzeitEinesArtikels = gesamtzeitEinesArtikels
									.add(new BigDecimal(azDtos[k].getDdDauer()));
							gesamtkostenEinesArtikels = gesamtkostenEinesArtikels
									.add(azDtos[k].getBdKosten());
						}
					}

					Object[] oZeile = (Object[]) hmZeilen.get(artikelIId);

					oZeile[REPORT_TAETIGKEITSSTATISTIK_AZ_IST] = gesamtzeitEinesArtikels;
					oZeile[REPORT_TAETIGKEITSSTATISTIK_AZ_KOSTEN] = gesamtkostenEinesArtikels;

					daten.add(oZeile);

				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}

		data = new Object[daten.size()][AuftragReportFac.REPORT_TAETIGKEITSSTATISTIK_ANZAHL_SPALTEN];
		data = (Object[][]) daten.toArray(data);

		initJRDS(parameter, AuftragReportFac.REPORT_MODUL,
				AuftragReportFac.REPORT_TAETIGKEITSSTATISTIK,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAuftraegeErledigt(
			ReportJournalKriterienDto kritDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		JasperPrintLP oPrint = null;
		cAktuellerReport = AuftragReportFac.REPORT_AUFTRAG_ERLEDIGT;
		Session session = null;
		session = FLRSessionFactory.getFactory().openSession();
		// Filter und Sortierung
		Criteria crit = session.createCriteria(FLRAuftragReport.class);
		// Filter nach Mandant
		crit.add(Restrictions.eq(AuftragFac.FLR_AUFTRAG_MANDANT_C_NR,
				theClientDto.getMandant()));
		if (kritDtoI.kostenstelleIId != null) {
			crit.add(Restrictions.eq(AuftragFac.FLR_AUFTRAG_KOSTENSTELLE_I_ID,
					kritDtoI.kostenstelleIId));
		}
		if (kritDtoI.bSortiereNachKostenstelle) {
			crit.addOrder(Order.asc(AuftragFac.FLR_AUFTRAG_KOSTENSTELLE_I_ID));
		}

		crit.add(Restrictions.isNotNull(AuftragFac.FLR_AUFTRAG_T_ERLEDIGT));

		if (kritDtoI.dVon != null) {
			crit.add(Restrictions.ge(AuftragFac.FLR_AUFTRAG_T_ERLEDIGT,
					kritDtoI.dVon));
		}
		if (kritDtoI.dBis != null) {
			crit.add(Restrictions.le(AuftragFac.FLR_AUFTRAG_T_ERLEDIGT,
					kritDtoI.dBis));
		}
		if (kritDtoI.sBelegnummerVon != null) {
			crit.add(Restrictions.ge(AuftragFac.FLR_AUFTRAG_C_NR,
					kritDtoI.sBelegnummerVon));
		}
		if (kritDtoI.sBelegnummerBis != null) {
			crit.add(Restrictions.le(AuftragFac.FLR_AUFTRAG_C_NR,
					kritDtoI.sBelegnummerBis));
		}

		// Einschraenkung nach einem bestimmten Vertreter
		if (kritDtoI.vertreterIId != null) {
			crit.add(Restrictions.eq(AuftragFac.FLR_AUFTRAG_VERTRETER_I_ID,
					kritDtoI.vertreterIId));
		}

		if (kritDtoI.kundeIId != null) {
			crit.add(Restrictions.eq(
					AuftragFac.FLR_AUFTRAG_KUNDE_I_ID_AUFTRAGSADRESSE,
					kritDtoI.kundeIId));
		}
		if (kritDtoI.bSortiereNachIdent) {
			crit.addOrder(Order.asc(AuftragFac.FLR_AUFTRAG_C_NR));
		}
		if (kritDtoI.bSortiereNachPersonal) {
			crit.addOrder(Order
					.asc(AuftragFac.FLR_AUFTRAG_KUNDE_I_ID_AUFTRAGSADRESSE));
		}
		if (kritDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_VERTRETER) {
			crit.createCriteria(AuftragFac.FLR_AUFTRAG_FLRVERTRETER).addOrder(
					Order.asc(PersonalFac.FLR_PERSONAL_C_KURZZEICHEN));
			crit.addOrder(Order
					.asc(AuftragFac.FLR_AUFTRAG_KUNDE_I_ID_AUFTRAGSADRESSE));
		}

		crit.addOrder(Order.asc(AuftragFac.FLR_AUFTRAG_C_NR));

		boolean darfVerkaufspreisSehen = getTheJudgeFac().hatRecht(
				RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF, theClientDto);

		// Liste holen
		List<FLRAuftragReport> list = crit.list();

		FLRAuftragReport[] aFLRAuftragReport = new FLRAuftragReport[list.size()];
		aFLRAuftragReport = list.toArray(aFLRAuftragReport);
		data = new Object[aFLRAuftragReport.length][AuftragReportFac.REPORT_ERLEDIGT_ANZAHL_SPALTEN];
		for (int i = 0; i < aFLRAuftragReport.length; i++) {
			data[i][AuftragReportFac.REPORT_ERLEDIGT_CNR] = aFLRAuftragReport[i]
					.getC_nr();
			data[i][AuftragReportFac.REPORT_ERLEDIGT_KUNDE] = aFLRAuftragReport[i]
					.getFlrkunde().getFlrpartner()
					.getC_name1nachnamefirmazeile1();
			data[i][AuftragReportFac.REPORT_ERLEDIGT_KOSTENSTELLE] = aFLRAuftragReport[i]
					.getFlrkostenstelle().getC_nr();
			data[i][AuftragReportFac.REPORT_ERLEDIGT_DATUM] = aFLRAuftragReport[i]
					.getT_belegdatum();
			if (aFLRAuftragReport[i].getFlrvertreter() != null) {
				data[i][AuftragReportFac.REPORT_ERLEDIGT_VERTRETER] = aFLRAuftragReport[i]
						.getFlrvertreter().getC_kurzzeichen();
			}
			data[i][AuftragReportFac.REPORT_ERLEDIGT_STATUS] = aFLRAuftragReport[i]
					.getAuftragstatus_c_nr();
			data[i][AuftragReportFac.REPORT_ERLEDIGT_ERLEDIGT_AM] = aFLRAuftragReport[i]
					.getT_erledigt();
			if (aFLRAuftragReport[i].getPersonal_i_id_erledigt() != null) {
				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKey(
								aFLRAuftragReport[i]
										.getPersonal_i_id_erledigt(),
								theClientDto);
				data[i][AuftragReportFac.REPORT_ERLEDIGT_ERLEDIGT_DURCH] = personalDto
						.formatAnrede();
			}

			if (darfVerkaufspreisSehen) {
				data[i][AuftragReportFac.REPORT_ERLEDIGT_WERT] = aFLRAuftragReport[i]
						.getN_gesamtauftragswertinauftragswaehrung();
			} else {
				data[i][AuftragReportFac.REPORT_ERLEDIGT_WERT] = null;
			}
			data[i][AuftragReportFac.REPORT_ERLEDIGT_AUFTRAGART] = aFLRAuftragReport[i]
					.getAuftragart_c_nr();
			data[i][AuftragReportFac.REPORT_ERLEDIGT_PROJEKT] = aFLRAuftragReport[i]
					.getC_bez();
			data[i][AuftragReportFac.REPORT_ERLEDIGT_WAEHRUNG] = aFLRAuftragReport[i]
					.getWaehrung_c_nr_auftragswaehrung();
			data[i][AuftragReportFac.REPORT_ERLEDIGT_KURS] = aFLRAuftragReport[i]
					.getF_wechselkursmandantwaehrungzuauftragswaehrung();
			data[i][AuftragReportFac.REPORT_ERLEDIGT_BESTELLNUMMER] = aFLRAuftragReport[i]
					.getC_bestellnummer();
			data[i][AuftragReportFac.REPORT_ERLEDIGT_BESTELLTERMIN] = aFLRAuftragReport[i]
					.getT_bestelldatum();
			data[i][AuftragReportFac.REPORT_ERLEDIGT_LIEFERTERMIN] = aFLRAuftragReport[i]
					.getT_liefertermin();
			data[i][AuftragReportFac.REPORT_ERLEDIGT_FINALTERMIN] = aFLRAuftragReport[i]
					.getT_finaltermin();
			if (aFLRAuftragReport[i].getB_poenale() != null) {
				data[i][AuftragReportFac.REPORT_ERLEDIGT_POENALE] = Helper
						.short2Boolean(aFLRAuftragReport[i].getB_poenale());
			} else {
				data[i][AuftragReportFac.REPORT_ERLEDIGT_POENALE] = new Boolean(
						false);
			}
			if (aFLRAuftragReport[i].getB_rohs() != null) {
				data[i][AuftragReportFac.REPORT_ERLEDIGT_ROHS] = Helper
						.short2Boolean(aFLRAuftragReport[i].getB_rohs());
			} else {
				data[i][AuftragReportFac.REPORT_ERLEDIGT_ROHS] = new Boolean(
						false);
			}
			if (aFLRAuftragReport[i].getB_teillieferungmoeglich() != null) {
				data[i][AuftragReportFac.REPORT_ERLEDIGT_TEILLIEFERBAR] = Helper
						.short2Boolean(aFLRAuftragReport[i]
								.getB_teillieferungmoeglich());
			} else {
				data[i][AuftragReportFac.REPORT_ERLEDIGT_TEILLIEFERBAR] = new Boolean(
						false);
			}
			if (aFLRAuftragReport[i].getB_lieferterminunverbindlich() != null) {
				data[i][AuftragReportFac.REPORT_ERLEDIGT_UNVERBINDLICH] = Helper
						.short2Boolean(aFLRAuftragReport[i]
								.getB_lieferterminunverbindlich());
			} else {
				data[i][AuftragReportFac.REPORT_ERLEDIGT_UNVERBINDLICH] = new Boolean(
						false);
			}
			if (aFLRAuftragReport[i].getFlrkundeansprechpartner() != null) {
				if (aFLRAuftragReport[i].getFlrkundeansprechpartner()
						.getFlrpartneransprechpartner() != null) {
					String sAnsprechpartner = "";
					sAnsprechpartner = sAnsprechpartner
							.concat(aFLRAuftragReport[i]
									.getFlrkundeansprechpartner()
									.getFlrpartneransprechpartner()
									.getAnrede_c_nr() != null ? (aFLRAuftragReport[i]
									.getFlrkundeansprechpartner()
									.getFlrpartneransprechpartner()
									.getAnrede_c_nr().trim() + " ")
									: "");
					sAnsprechpartner = sAnsprechpartner
							.concat(aFLRAuftragReport[i]
									.getFlrkundeansprechpartner()
									.getFlrpartneransprechpartner()
									.getC_name2vornamefirmazeile2() != null ? (aFLRAuftragReport[i]
									.getFlrkundeansprechpartner()
									.getFlrpartneransprechpartner()
									.getC_name2vornamefirmazeile2().trim() + " ")
									: "");
					sAnsprechpartner = sAnsprechpartner
							.concat(aFLRAuftragReport[i]
									.getFlrkundeansprechpartner()
									.getFlrpartneransprechpartner()
									.getC_name1nachnamefirmazeile1() != null ? aFLRAuftragReport[i]
									.getFlrkundeansprechpartner()
									.getFlrpartneransprechpartner()
									.getC_name1nachnamefirmazeile1().trim()
									: "");
					data[i][AuftragReportFac.REPORT_ERLEDIGT_ANSPRECHPARTNER] = sAnsprechpartner;
				} else {
					data[i][AuftragReportFac.REPORT_ERLEDIGT_ANSPRECHPARTNER] = "";
				}
			} else {
				data[i][AuftragReportFac.REPORT_ERLEDIGT_ANSPRECHPARTNER] = "";
			}
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					aFLRAuftragReport[i].getKunde_i_id_auftragsadresse(),
					theClientDto);
			PartnerDto partnerAdresseDto = null;
			if (kundeDto != null) {
				partnerAdresseDto = getPartnerFac().partnerFindByPrimaryKey(
						kundeDto.getPartnerIId(), theClientDto);
			}
			if (partnerAdresseDto != null) {
				data[i][AuftragReportFac.REPORT_ERLEDIGT_ADRESSE] = partnerAdresseDto
						.formatAdresse();
			} else {
				data[i][AuftragReportFac.REPORT_ERLEDIGT_ADRESSE] = null;
			}
			KundeDto kundeLieferDto = getKundeFac().kundeFindByPrimaryKey(
					aFLRAuftragReport[i].getKunde_i_id_lieferadresse(),
					theClientDto);
			PartnerDto partnerLieferDto = null;
			if (kundeLieferDto != null) {
				partnerLieferDto = getPartnerFac().partnerFindByPrimaryKey(
						kundeLieferDto.getPartnerIId(), theClientDto);
			}
			if (partnerLieferDto != null) {
				data[i][AuftragReportFac.REPORT_ERLEDIGT_LIEFERADRESSE] = partnerLieferDto
						.formatAdresse();
			} else {
				data[i][AuftragReportFac.REPORT_ERLEDIGT_LIEFERADRESSE] = null;
			}
			KundeDto kundeRechnungDto = getKundeFac().kundeFindByPrimaryKey(
					aFLRAuftragReport[i].getKunde_i_id_rechnungsadresse(),
					theClientDto);
			PartnerDto partnerRechnungDto = null;
			if (kundeRechnungDto != null) {
				partnerRechnungDto = getPartnerFac().partnerFindByPrimaryKey(
						kundeRechnungDto.getPartnerIId(), theClientDto);
			}
			if (partnerRechnungDto != null) {
				data[i][AuftragReportFac.REPORT_ERLEDIGT_RECHNUNGSADRESSE] = partnerRechnungDto
						.formatAdresse();
			} else {
				data[i][AuftragReportFac.REPORT_ERLEDIGT_RECHNUNGSADRESSE] = null;
			}

		}
		session.close();
		HashMap<String, Object> mapParameter = new HashMap<String, Object>();
		mapParameter.put("P_MANDANTWAEHRUNG",
				theClientDto.getSMandantenwaehrung());
		mapParameter.put("P_SORTIERENACHKOSTENSTELLE", new Boolean(
				kritDtoI.bSortiereNachKostenstelle));
		mapParameter.put("P_SORTIERENACHKUNDE", new Boolean(
				kritDtoI.bSortiereNachPersonal));
		mapParameter.put(LPReport.P_SORTIERUNG,
				buildSortierungAuftragJournal(kritDtoI, theClientDto));
		mapParameter.put(LPReport.P_FILTER,
				buildFilterAuftragJournal(kritDtoI, theClientDto));
		initJRDS(mapParameter, AuftragReportFac.REPORT_MODUL,
				AuftragReportFac.REPORT_AUFTRAG_ERLEDIGT,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		oPrint = getReportPrint();

		return oPrint;
	}

	private String buildSortierungAuftragJournal(
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
		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_VERTRETER) {
			buff.append(
					" "
							+ getTextRespectUISpr("lp.vertreter",
									theClientDto.getMandant(),
									theClientDto.getLocUi())).append(", ");
		}
		buff.append(" "
				+ getTextRespectUISpr("auft.print.auftragsnummer",
						theClientDto.getMandant(), theClientDto.getLocUi()));

		return buff.toString();
	}

	private String buildFilterAuftragJournal(
			ReportJournalKriterienDto kritDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		StringBuffer buff = new StringBuffer("");

		// Belegdatum
		if (kritDtoI.dVon != null || kritDtoI.dBis != null) {
			if (cAktuellerReport
					.equals(AuftragReportFac.REPORT_AUFTRAG_ERLEDIGT)) {
				buff.append(getTextRespectUISpr("lp.erledigt",
						theClientDto.getMandant(), theClientDto.getLocUi()));
			} else {
				buff.append(getTextRespectUISpr("bes.belegdatum",
						theClientDto.getMandant(), theClientDto.getLocUi()));
			}

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
							.getCName1nachnamefirmazeile1());
		}
		// Angebotsnummer
		if (kritDtoI.sBelegnummerVon != null
				|| kritDtoI.sBelegnummerBis != null) {
			buff.append(" ")
					.append(getTextRespectUISpr("auft.print.auftragsnummer",
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

		String cBuffer = buff.toString().trim();

		return cBuffer;
	}

	public final static String JP_BIS = "P_BIS";
	public final static String JP_PARTNERKLASSE = "P_PARTNERKLASSE";
	public final static String JP_VON = "P_VON";
}

// Class that tells the sort method the result of comparing 2 rows

class ArrayColumnComparator implements Comparator<Object> {

	private int columnToSortOn = 0;

	// Constructor takes & stores the column to use for sorting
	ArrayColumnComparator(int columnToSortOn) {
		this.columnToSortOn = columnToSortOn;
	}

	// Return the result of comparing the two row arrays
	public int compare(Object o1, Object o2) {
		int compare = 0;

		// cast the object args back to string arrays
		Object[] row1 = (Object[]) o1;
		Object[] row2 = (Object[]) o2;
		String v1 = (String) row1[columnToSortOn];
		String v2 = (String) row2[columnToSortOn];
		if (v1 == null) {
			v1 = "";
		}
		if (v2 == null) {
			v2 = "";
		}
		Integer i1 = (Integer) row1[15];
		Integer i2 = (Integer) row2[15];
		if (i1 == null) {
			i1 = 0;
		}
		if (i2 == null) {
			i2 = 0;
		}
		if (i1 == 1 && i2 == 1)
			compare = v1.compareTo(v2);
		if (i1 == 0 && i2 == 0)
			compare = v1.compareTo(v2);
		// compare the desired column values & return result
		return compare;
	}
}
