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
package com.lp.server.reklamation.ejbfac;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.BelegInfos;
import com.lp.server.bestellung.fastlanereader.generated.FLRWareneingangspositionen;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.LieferantbeurteilungDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.personal.service.MaschinengruppeDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.reklamation.fastlanereader.generated.FLRReklamation;
import com.lp.server.reklamation.service.ReklamationDto;
import com.lp.server.reklamation.service.ReklamationFac;
import com.lp.server.reklamation.service.ReklamationFehlerartenJournalKriterienDto;
import com.lp.server.reklamation.service.ReklamationReportFac;
import com.lp.server.reklamation.service.ReklamationbildDto;
import com.lp.server.reklamation.service.TermintreueDto;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeReklamation;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.DatumsfilterVonBis;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.server.util.report.TimingInterceptor;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

@Stateless
@Interceptors(TimingInterceptor.class)
public class ReklamationReportFacBean extends LPReport implements ReklamationReportFac {

	private String sAktuellerReport = null;
	private Object[][] data = null;

	private static int REPORT_REKLAMATIONSJOURNAL_BELEGNR = 0;
	private static int REPORT_REKLAMATIONSJOURNAL_DATUM = 1;
	private static int REPORT_REKLAMATIONSJOURNAL_PROJEKT = 2;
	private static int REPORT_REKLAMATIONSJOURNAL_ARTIKELNR = 3;
	private static int REPORT_REKLAMATIONSJOURNAL_ARTIKELBEZEICHNUNG = 4;
	private static int REPORT_REKLAMATIONSJOURNAL_MENGE = 5;
	private static int REPORT_REKLAMATIONSJOURNAL_MATERIALKOSTEN = 6;
	private static int REPORT_REKLAMATIONSJOURNAL_AZKOSTEN = 7;
	private static int REPORT_REKLAMATIONSJOURNAL_GRUND = 8;
	private static int REPORT_REKLAMATIONSJOURNAL_REKLAMATIONSART = 9;
	private static int REPORT_REKLAMATIONSJOURNAL_REKLAMATIONSBELEG = 10;
	private static int REPORT_REKLAMATIONSJOURNAL_KUNDELIEFERANT = 11;
	private static int REPORT_REKLAMATIONSJOURNAL_FEHLER = 12;

	private static int REPORT_REKLAMATIONSJOURNAL_KOSTENSTELLE = 13;
	private static int REPORT_REKLAMATIONSJOURNAL_FEHLERANGABE = 14;
	private static int REPORT_REKLAMATIONSJOURNAL_AUFNAHMEART = 15;
	private static int REPORT_REKLAMATIONSJOURNAL_PERSON_AUFNEHMER = 16;
	private static int REPORT_REKLAMATIONSJOURNAL_ANSPRECHPARTNER = 17;
	private static int REPORT_REKLAMATIONSJOURNAL_TELEFONANSPRECHPARTNER = 18;
	private static int REPORT_REKLAMATIONSJOURNAL_ANALYSE = 19;
	private static int REPORT_REKLAMATIONSJOURNAL_KOMMENTAR = 20;
	private static int REPORT_REKLAMATIONSJOURNAL_B_BERECHTIGT = 21;
	private static int REPORT_REKLAMATIONSJOURNAL_PERSON_RUECKSPRACHE = 22;
	private static int REPORT_REKLAMATIONSJOURNAL_RUECKSPRECHEZEIT = 23;
	private static int REPORT_REKLAMATIONSJOURNAL_RUECKSPRECHEMIT = 24;
	private static int REPORT_REKLAMATIONSJOURNAL_MASSNAHME_KURZ = 25;
	private static int REPORT_REKLAMATIONSJOURNAL_PERSON_MASSNAHME_KURZ = 26;
	private static int REPORT_REKLAMATIONSJOURNAL_MASSNAHME_BIS_KURZ = 27;
	private static int REPORT_REKLAMATIONSJOURNAL_MASSNAHME_EINGEFUEHRT_KURZ = 28;
	private static int REPORT_REKLAMATIONSJOURNAL_MASSNAHME_KOMMENTAR_KURZ = 29;
	private static int REPORT_REKLAMATIONSJOURNAL_MASSNAHME_MITTEL = 30;
	private static int REPORT_REKLAMATIONSJOURNAL_PERSON_MASSNAHME_MITTEL = 31;
	private static int REPORT_REKLAMATIONSJOURNAL_MASSNAHME_BIS_MITTEL = 32;
	private static int REPORT_REKLAMATIONSJOURNAL_MASSNAHME_EINGEFUEHRT_MITTEL = 33;
	private static int REPORT_REKLAMATIONSJOURNAL_MASSNAHME_KOMMENTAR_MITTEL = 34;
	private static int REPORT_REKLAMATIONSJOURNAL_MASSNAHME_LANG = 35;
	private static int REPORT_REKLAMATIONSJOURNAL_PERSON_MASSNAHME_LANG = 36;
	private static int REPORT_REKLAMATIONSJOURNAL_MASSNAHME_BIS_LANG = 37;
	private static int REPORT_REKLAMATIONSJOURNAL_MASSNAHME_EINGEFUEHRT_LANG = 38;
	private static int REPORT_REKLAMATIONSJOURNAL_MASSNAHME_KOMMENTAR_LANG = 39;
	private static int REPORT_REKLAMATIONSJOURNAL_ERLEDIGTDATUM = 40;
	private static int REPORT_REKLAMATIONSJOURNAL_PERSON_ERLEDIGT = 41;
	private static int REPORT_REKLAMATIONSJOURNAL_SCHWERE = 42;
	private static int REPORT_REKLAMATIONSJOURNAL_BEHANDLUNG = 43;
	private static int REPORT_REKLAMATIONSJOURNAL_WIRKSAMKEIT = 44;
	private static int REPORT_REKLAMATIONSJOURNAL_WIRKSAMKEIT_KOMMENTAR = 45;
	private static int REPORT_REKLAMATIONSJOURNAL_B_BETRIFFTLAGERSTAND = 46;
	private static int REPORT_REKLAMATIONSJOURNAL_B_BETRIFFTGELIEFERTE = 47;
	private static int REPORT_REKLAMATIONSJOURNAL_STUECKLAGERSTAND = 48;
	private static int REPORT_REKLAMATIONSJOURNAL_STUECKGELIEFERTE = 49;
	private static int REPORT_REKLAMATIONSJOURNAL_PERSON_WIRKSAMKEIT = 50;
	private static int REPORT_REKLAMATIONSJOURNAL_WIRKSAMKEIT_BIS = 51;
	private static int REPORT_REKLAMATIONSJOURNAL_WIRKSAMKEIT_DURCHGEFUEHRT = 52;
	private static int REPORT_REKLAMATIONSJOURNAL_GRUND_KOMMENTAR = 53;

	private static int REPORT_REKLAMATIONSJOURNAL_MASCHINE_BEZEICHNUNG = 54;
	private static int REPORT_REKLAMATIONSJOURNAL_MASCHINE_IDENTIFIKATIONSNUMMER = 55;
	private static int REPORT_REKLAMATIONSJOURNAL_MASCHINE_INVENTARNUMMER = 56;
	private static int REPORT_REKLAMATIONSJOURNAL_KNDREKLANR = 57;
	private static int REPORT_REKLAMATIONSJOURNAL_KNDLSNR = 58;
	private static int REPORT_REKLAMATIONSJOURNAL_SNRCHNR = 59;
	private static int REPORT_REKLAMATIONSJOURNAL_KUNDEUNTERART = 60;
	private static int REPORT_REKLAMATIONSJOURNAL_LIEFERANT_KUNDEUNTERART_LIEFERANT = 61;
	private static int REPORT_REKLAMATIONSJOURNAL_BESTELLUNG_KUNDEUNTERART_LIEFERANT = 62;
	private static int REPORT_REKLAMATIONSJOURNAL_WARENEINGANG_KUNDEUNTERART_LIEFERANT = 63;
	private static int REPORT_REKLAMATIONSJOURNAL_LOS_KUNDEUNTERART_FERTIGUNG = 64;
	private static int REPORT_REKLAMATIONSJOURNAL_ARBEITSGANG_KUNDEUNTERART_FERTIGUNG = 65;
	private static int REPORT_REKLAMATIONSJOURNAL_MASCHINE_KUNDEUNTERART_FERTIGUNG = 66;
	private static int REPORT_REKLAMATIONSJOURNAL_VERURSACHER_KUNDEUNTERART_FERTIGUNG = 67;
	private static int REPORT_REKLAMATIONSJOURNAL_LFREKLANR = 68;
	private static int REPORT_REKLAMATIONSJOURNAL_LFLSNR = 69;
	private static int REPORT_REKLAMATIONSJOURNAL_ARTIKELKURZBEZEICHNUNG = 70;
	private static int REPORT_REKLAMATIONSJOURNAL_ARTIKELZUSATZBEZEICHNUNG = 71;
	private static int REPORT_REKLAMATIONSJOURNAL_ARTIKELZUSATZBEZEICHNUNG2 = 72;
	private static int REPORT_REKLAMATIONSJOURNAL_ANLAGEDATUM = 73;
	private static int REPORT_REKLAMATIONSJOURNAL_WARE_ERHALTEN = 74;
	private static int REPORT_REKLAMATIONSJOURNAL_MASCHINE_SERIENNUMMER = 75;
	private static int REPORT_REKLAMATIONSJOURNAL_ANZAHL_SPALTEN = 76;

	private static int REPORT_LIEFERANTENTERMINTREUE_LIEFERANT = 0;
	private static int REPORT_LIEFERANTENTERMINTREUE_BESTELLNUMMER = 1;
	private static int REPORT_LIEFERANTENTERMINTREUE_WARENEINGANGSDATUM = 2;
	private static int REPORT_LIEFERANTENTERMINTREUE_WELIEFERSCHEIN = 3;
	private static int REPORT_LIEFERANTENTERMINTREUE_SOLLTERMIN = 4;
	private static int REPORT_LIEFERANTENTERMINTREUE_VERSPAETUNG = 5;
	private static int REPORT_LIEFERANTENTERMINTREUE_ARTIKELNUMMER = 6;
	private static int REPORT_LIEFERANTENTERMINTREUE_BEZEICHNUNG = 7;
	private static int REPORT_LIEFERANTENTERMINTREUE_LIEFERANT_I_ID = 8;
	private static int REPORT_LIEFERANTENTERMINTREUE_MENGE = 9;
	private static int REPORT_LIEFERANTENTERMINTREUE_ANZAHL_SPALTEN = 10;

	private static int REPORT_REKLAMATION_BILD = 0;
	private static int REPORT_REKLAMATION_BILD_BEZEICHNUNG = 1;

	private static int REPORT_LIEFERANTENBEURTEILUNG_LIEFERANT = 0;
	private static int REPORT_LIEFERANTENBEURTEILUNG_BESTELLNUMMER = 1;
	private static int REPORT_LIEFERANTENBEURTEILUNG_WARENEINGANGSDATUM = 2;
	private static int REPORT_LIEFERANTENBEURTEILUNG_WELIEFERSCHEIN = 3;
	private static int REPORT_LIEFERANTENBEURTEILUNG_SOLLTERMIN = 4;
	private static int REPORT_LIEFERANTENBEURTEILUNG_VERSPAETUNG = 5;
	private static int REPORT_LIEFERANTENBEURTEILUNG_ARTIKELNUMMER = 6;
	private static int REPORT_LIEFERANTENBEURTEILUNG_BEZEICHNUNG = 7;
	private static int REPORT_LIEFERANTENBEURTEILUNG_MENGE = 8;
	private static int REPORT_LIEFERANTENBEURTEILUNG_REKLAMATIONSNUMMER = 9;
	private static int REPORT_LIEFERANTENBEURTEILUNG_PUNKTE_TERMIN = 10;
	private static int REPORT_LIEFERANTENBEURTEILUNG_PUNKTE_SCHWERE = 11;
	private static int REPORT_LIEFERANTENBEURTEILUNG_PUNKTE_BEHANDLUNG = 12;
	private static int REPORT_LIEFERANTENBEURTEILUNG_PUNKTE_GESAMT = 13;
	private static int REPORT_LIEFERANTENBEURTEILUNG_WEIID = 14;
	private static int REPORT_LIEFERANTENBEURTEILUNG_KLASSE_BISHER = 15;
	private static int REPORT_LIEFERANTENBEURTEILUNG_PUNKTE_BISHER = 16;
	private static int REPORT_LIEFERANTENBEURTEILUNG_LIEFERANT_LKZ = 17;
	private static int REPORT_LIEFERANTENBEURTEILUNG_LIEFERANT_PLZ = 18;
	private static int REPORT_LIEFERANTENBEURTEILUNG_LIEFERANT_ORT = 19;
	private static int REPORT_LIEFERANTENBEURTEILUNG_LIEFERANT_STRASSE = 20;
	private static int REPORT_LIEFERANTENBEURTEILUNG_LIEFERANT_KBEZ = 22;
	private static int REPORT_LIEFERANTENBEURTEILUNG_LIEFERANT_I_ID = 23;
	private static int REPORT_LIEFERANTENBEURTEILUNG_PUNKTE_TERMIN_JE_WEP = 24;
	private static int REPORT_LIEFERANTENBEURTEILUNG_ANZAHL_SPALTEN = 25;

	private static int REPORT_FEHLERART_REKLAMATIONSNUMMER = 0;
	private static int REPORT_FEHLERART_FEHLERART = 1;
	private static int REPORT_FEHLERART_VERURSACHER = 2;
	private static int REPORT_FEHLERART_MASCHINENGRUPPE = 3;
	private static int REPORT_FEHLERART_KUNDE = 4;
	private static int REPORT_FEHLERART_REKLAMATIONSART = 5;
	private static int REPORT_FEHLERART_GRUPPIERUNG = 6;
	private static int REPORT_FEHLERART_GRUND = 7;
	private static int REPORT_FEHLERART_REKLAMATIONID = 8;
	private static int REPORT_FEHLERART_ANZAHL_SPALTEN = 9;

	private static int REPORT_MITARBEITERREKLAMATION_REKLAMATIONSNUMMER = 0;
	private static int REPORT_MITARBEITERREKLAMATION_VERURSACHER = 1;
	private static int REPORT_MITARBEITERREKLAMATION_KUNDE = 2;
	private static int REPORT_MITARBEITERREKLAMATION_REKLAMATIONSART = 3;
	private static int REPORT_MITARBEITERREKLAMATION_ANZAHL_SPALTEN = 4;

	private static int REPORT_MASCHINENREKLAMATION_REKLAMATIONSNUMMER = 0;
	private static int REPORT_MASCHINENREKLAMATION_VERURSACHER = 1;
	private static int REPORT_MASCHINENREKLAMATION_KUNDE = 2;
	private static int REPORT_MASCHINENREKLAMATION_REKLAMATIONSART = 3;
	private static int REPORT_MASCHINENREKLAMATION_MASCHINENGRUPPE = 4;
	private static int REPORT_MASCHINENREKLAMATION_ANZAHL_SPALTEN = 5;

	private static int REPORT_OFFENEREKLAMATIONENEINESARTIKEL_BELEGNUMMER = 0;
	private static int REPORT_OFFENEREKLAMATIONENEINESARTIKEL_ART = 1;
	private static int REPORT_OFFENEREKLAMATIONENEINESARTIKEL_KUNDE = 2;
	private static int REPORT_OFFENEREKLAMATIONENEINESARTIKEL_LIEFERANT = 3;
	private static int REPORT_OFFENEREKLAMATIONENEINESARTIKEL_DATUM = 4;
	private static int REPORT_OFFENEREKLAMATIONENEINESARTIKEL_GRUND = 5;
	private static int REPORT_OFFENEREKLAMATIONENEINESARTIKEL_DATUM_ERLEDIGT = 6;
	private static int REPORT_OFFENEREKLAMATIONENEINESARTIKEL_FEHLER = 7;
	private static int REPORT_OFFENEREKLAMATIONENEINESARTIKEL_FEHLERANGABE = 8;

	private static int REPORT_OFFENEREKLAMATIONENEINESARTIKEL_ANZAHL_SPALTEN = 9;

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printLieferantenbeurteilung(Timestamp tVon, Timestamp tBis, Integer lieferantIId,
			Integer brancheIId, Integer liefergruppeIId, Integer partnerklasseIId, boolean bVerdichtet, boolean bDokumentenablage,
			TheClientDto theClientDto) {

		index = -1;
		sAktuellerReport = ReklamationReportFac.REPORT_LIEFERANTENBEURTEILUNG;

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		parameter.put("P_VON", Helper.cutTimestamp(tVon));
		parameter.put("P_BIS", Helper.cutTimestamp(tBis));
		parameter.put("P_VERDICHTET", bVerdichtet);
		parameter.put("P_DOKUMENTENABLAGE", bDokumentenablage);

		HashMap<Integer, String> hmLieferanten = new HashMap<Integer, String>();

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tBis.getTime());
		Timestamp tBeurteilungsdatum = Helper.cutTimestamp(tBis);

		// MaxPunkteholen

		int iMaxPunkteSchwere = 30;
		int iMaxPunkteBehandlung = 30;

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT max(s.i_punkte) FROM FLRSchwere s";
		org.hibernate.Query query = session.createQuery(sQuery);

		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		while (resultListIterator.hasNext()) {
			Integer iMax = (Integer) resultListIterator.next();
			if (iMax != null) {
				iMaxPunkteSchwere = iMax;
			}
		}
		session.close();
		session = FLRSessionFactory.getFactory().openSession();

		sQuery = "SELECT max(s.i_punkte) FROM FLRBehandlung s";
		query = session.createQuery(sQuery);

		results = query.list();
		resultListIterator = results.iterator();

		while (resultListIterator.hasNext()) {
			Integer iMax = (Integer) resultListIterator.next();
			if (iMax != null) {
				iMaxPunkteBehandlung = iMax;
			}
		}

		// Klassen holen
		int iPunkteA = 0;
		int iPunkteB = 0;
		int iPunkteC = 0;

		try {
			ParametermandantDto parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_LIEFERANT, ParameterFac.PARAMETER_LIEFERANTENBEURTEILUNG_PUNKTE_KLASSE_A);
			iPunkteA = (Integer) parameterDto.getCWertAsObject();

			parameter.put("P_PUNKTEA", iPunkteA);

			parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_LIEFERANT, ParameterFac.PARAMETER_LIEFERANTENBEURTEILUNG_PUNKTE_KLASSE_B);
			iPunkteB = (Integer) parameterDto.getCWertAsObject();

			parameter.put("P_PUNKTEB", iPunkteB);

			parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_LIEFERANT, ParameterFac.PARAMETER_LIEFERANTENBEURTEILUNG_PUNKTE_KLASSE_C);
			iPunkteC = (Integer) parameterDto.getCWertAsObject();

			parameter.put("P_PUNKTEC", iPunkteC);

		} catch (RemoteException e2) {
			e2.printStackTrace();
		}
		tVon = Helper.cutTimestamp(tVon);
		tBis = Helper.cutTimestamp(Helper.addiereTageZuTimestamp(tBis, 1));

		session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session.createCriteria(FLRWareneingangspositionen.class);

		crit.createAlias("flrwareneingang", "w");
		crit.createAlias("w.flrbestellung", "b");
		crit.createAlias("b.flrlieferant", "l");
		crit.createAlias("l.flrpartner", "p");
		crit.add(Restrictions.ge("w.t_wareneingansdatum", tVon));
		crit.add(Restrictions.lt("w.t_wareneingansdatum", tBis));
		crit.add(Restrictions.eq("b.mandant_c_nr", theClientDto.getMandant()));

		crit.add(Restrictions.eq("l.b_beurteilen", Helper.boolean2Short(true)));

		if (lieferantIId != null) {

			crit.add(Restrictions.eq("l.i_id", lieferantIId));

		}

		if (brancheIId != null) {
			crit.add(Restrictions.eq("p.branche_i_id", brancheIId));
			parameter.put("P_BRANCHE",
					getPartnerServicesFac().brancheFindByPrimaryKey(brancheIId, theClientDto).getBezeichnung());
		}
		
		if (partnerklasseIId != null) {
			crit.add(Restrictions.eq("p.partnerklasse_i_id", partnerklasseIId));
			parameter.put("P_PARTNERKLASSE",
					getPartnerFac().partnerklasseFindByPrimaryKey(partnerklasseIId, theClientDto).getBezeichnung());
		}
		

		if (liefergruppeIId != null) {
			crit.createAlias("l.set_liefergruppen", "set");
			crit.createAlias("set.flrliefergruppe", "lg");
			crit.add(Restrictions.eq("lg.i_id", liefergruppeIId));
			parameter.put("P_LIEFERGUPPE", getLieferantServicesFac()
					.lfliefergruppeFindByPrimaryKey(liefergruppeIId, theClientDto).getBezeichnung());

		}

		crit.addOrder(Order.asc("p." + PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1));
		crit.addOrder(Order.desc("b.c_nr"));

		results = crit.list();
		Iterator<?> resultListIteratorWEP = results.iterator();

		TermintreueDto[] dtos = null;
		try {
			dtos = getReklamationFac().termintreueFindAll();
		} catch (RemoteException e1) {
			throwEJBExceptionLPRespectOld(e1);
		}

		data = new Object[results.size()][REPORT_LIEFERANTENBEURTEILUNG_ANZAHL_SPALTEN];
		int iLetzterWareneingang = -1;
		int iLetzterLieferant = -1;
		int iAnzahlWareneingaengeProLieferant = 0;
		int iAnzahlPunkteProLieferant = 0;
		int row = 0;

		HashMap hmWareneingaengeAbgehandelt = new HashMap();

		ArrayList<FLRWareneingangspositionen> alWEPos = sortiereNachLieferantBestellnummerWEDatumSollTermin(
				resultListIteratorWEP, theClientDto);

		resultListIterator = alWEPos.iterator();
		while (resultListIterator.hasNext()) {
			FLRWareneingangspositionen wareneingangspositionen = (FLRWareneingangspositionen) resultListIterator.next();

			data[row][REPORT_LIEFERANTENBEURTEILUNG_LIEFERANT] = wareneingangspositionen.getFlrwareneingang()
					.getFlrbestellung().getFlrlieferant().getFlrpartner().getC_name1nachnamefirmazeile1();
			data[row][REPORT_LIEFERANTENBEURTEILUNG_LIEFERANT_I_ID] = wareneingangspositionen.getFlrwareneingang()
					.getFlrbestellung().getFlrlieferant().getI_id();
			data[row][REPORT_LIEFERANTENBEURTEILUNG_LIEFERANT_STRASSE] = wareneingangspositionen.getFlrwareneingang()
					.getFlrbestellung().getFlrlieferant().getFlrpartner().getC_strasse();
			data[row][REPORT_LIEFERANTENBEURTEILUNG_LIEFERANT_KBEZ] = wareneingangspositionen.getFlrwareneingang()
					.getFlrbestellung().getFlrlieferant().getFlrpartner().getC_kbez();
			if (wareneingangspositionen.getFlrwareneingang().getFlrbestellung().getFlrlieferant().getFlrpartner()
					.getFlrlandplzort() != null) {
				data[row][REPORT_LIEFERANTENBEURTEILUNG_LIEFERANT_PLZ] = wareneingangspositionen.getFlrwareneingang()
						.getFlrbestellung().getFlrlieferant().getFlrpartner().getFlrlandplzort().getC_plz();
				data[row][REPORT_LIEFERANTENBEURTEILUNG_LIEFERANT_LKZ] = wareneingangspositionen.getFlrwareneingang()
						.getFlrbestellung().getFlrlieferant().getFlrpartner().getFlrlandplzort().getFlrland()
						.getC_lkz();
				data[row][REPORT_LIEFERANTENBEURTEILUNG_LIEFERANT_ORT] = wareneingangspositionen.getFlrwareneingang()
						.getFlrbestellung().getFlrlieferant().getFlrpartner().getFlrlandplzort().getFlrort()
						.getC_name();
			}

			Timestamp weDatum = Helper.cutTimestamp(
					new Timestamp(wareneingangspositionen.getFlrwareneingang().getT_wareneingansdatum().getTime()));

			data[row][REPORT_LIEFERANTENBEURTEILUNG_WARENEINGANGSDATUM] = weDatum;
			data[row][REPORT_LIEFERANTENBEURTEILUNG_WEIID] = wareneingangspositionen.getFlrwareneingang().getI_id();
			data[row][REPORT_LIEFERANTENBEURTEILUNG_BESTELLNUMMER] = wareneingangspositionen.getFlrwareneingang()
					.getFlrbestellung().getC_nr();
			data[row][REPORT_LIEFERANTENBEURTEILUNG_WELIEFERSCHEIN] = wareneingangspositionen.getFlrwareneingang()
					.getC_lieferscheinnr();
			data[row][REPORT_LIEFERANTENBEURTEILUNG_MENGE] = wareneingangspositionen.getN_geliefertemenge();
			data[row][REPORT_LIEFERANTENBEURTEILUNG_ARTIKELNUMMER] = wareneingangspositionen.getFlrbestellposition()
					.getFlrartikel().getC_nr();

			hmLieferanten.put(
					wareneingangspositionen.getFlrwareneingang().getFlrbestellung().getFlrlieferant().getI_id(), "");

			try {
				// SP5539
				LieferantbeurteilungDto[] lbDtos = getLieferantFac()
						.lieferantbeurteilungfindByLetzteBeurteilungByLieferantIId(wareneingangspositionen
								.getFlrwareneingang().getFlrbestellung().getFlrlieferant().getI_id(), tVon);
				if (lbDtos != null && lbDtos.length > 0) {
					data[row][REPORT_LIEFERANTENBEURTEILUNG_KLASSE_BISHER] = lbDtos[0].getCKlasse();
					data[row][REPORT_LIEFERANTENBEURTEILUNG_PUNKTE_BISHER] = lbDtos[0].getIPunkte();
				}
			} catch (RemoteException e1) {
				throwEJBExceptionLPRespectOld(e1);
			}

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
					wareneingangspositionen.getFlrbestellposition().getFlrartikel().getI_id(), theClientDto);
			data[row][REPORT_LIEFERANTENBEURTEILUNG_BEZEICHNUNG] = artikelDto.formatBezeichnung();

			java.util.Date sollTermin = berechneSollTermin(wareneingangspositionen,theClientDto);
			data[row][REPORT_LIEFERANTENBEURTEILUNG_SOLLTERMIN] = sollTermin;

			Integer iVerspaetungstage = getVerspaetungstage(weDatum, sollTermin);

			data[row][REPORT_LIEFERANTENBEURTEILUNG_VERSPAETUNG] = iVerspaetungstage;

			int iPunkteTermin = getPunkteTermin(iVerspaetungstage, dtos);

			if (!hmWareneingaengeAbgehandelt.containsKey(wareneingangspositionen.getFlrwareneingang().getI_id())) {
				hmWareneingaengeAbgehandelt.put(wareneingangspositionen.getFlrwareneingang().getI_id(), "");
				// Punkte berechnen
				// Reklamation holen, wenn vorhanden

				int iPunkteSchwere = iMaxPunkteSchwere;
				int iPunkteBehandlung = iMaxPunkteBehandlung;

				try {

					ReklamationDto[] reklamationDto = getReklamationFac().reklamationFindByWareneingangIIdMandantCNr(
							wareneingangspositionen.getFlrwareneingang().getI_id(), theClientDto);
					if (reklamationDto != null && reklamationDto.length > 0) {

						data[row][REPORT_LIEFERANTENBEURTEILUNG_REKLAMATIONSNUMMER] = reklamationDto[0].getCNr();

						if (reklamationDto[0].getSchwereIId() != null) {
							iPunkteSchwere = getReklamationFac()
									.schwereFindByPrimaryKey(reklamationDto[0].getSchwereIId(), theClientDto)
									.getIPunkte();
						}
						if (reklamationDto[0].getBehandlungIId() != null) {
							iPunkteBehandlung = getReklamationFac()
									.behandlungFindByPrimaryKey(reklamationDto[0].getBehandlungIId(), theClientDto)
									.getIPunkte();
						}
					}

				} catch (RemoteException e) {
					// Keine vorhanden
				}

				data[row][REPORT_LIEFERANTENBEURTEILUNG_PUNKTE_BEHANDLUNG] = iPunkteBehandlung;
				data[row][REPORT_LIEFERANTENBEURTEILUNG_PUNKTE_TERMIN] = iPunkteTermin;
				data[row][REPORT_LIEFERANTENBEURTEILUNG_PUNKTE_SCHWERE] = iPunkteSchwere;

				int iPunkteGesamt = iPunkteTermin * (iPunkteSchwere + iPunkteBehandlung);

				data[row][REPORT_LIEFERANTENBEURTEILUNG_PUNKTE_GESAMT] = iPunkteGesamt;

				iAnzahlWareneingaengeProLieferant++;
				iAnzahlPunkteProLieferant += iPunkteGesamt;
			}

			data[row][REPORT_LIEFERANTENBEURTEILUNG_PUNKTE_TERMIN_JE_WEP] = iPunkteTermin;

			if ((iLetzterLieferant != -1 && (iLetzterLieferant != wareneingangspositionen.getFlrbestellposition()
					.getFlrbestellung().getFlrlieferant().getI_id())) || resultListIterator.hasNext() == false) {

				// Zuerst nachsehen ob vorhanden
				int iPunkte = 0;
				String klasse = "D";
				if (iAnzahlWareneingaengeProLieferant != 0) {
					iPunkte = iAnzahlPunkteProLieferant / iAnzahlWareneingaengeProLieferant;
				}

				if (iPunkte >= iPunkteC) {
					klasse = "C";
				}

				if (iPunkte >= iPunkteB) {
					klasse = "B";
				}

				if (iPunkte >= iPunkteA) {
					klasse = "A";
				}

				Integer lieferantIIdBeurteilung = iLetzterLieferant;
				if (lieferantIId != null) {
					lieferantIIdBeurteilung = lieferantIId;
				}

				try {
					LieferantbeurteilungDto dto = getLieferantFac()
							.lieferantbeurteilungfindByLieferantIIdTDatum(lieferantIIdBeurteilung, tBeurteilungsdatum);

					if (Helper.short2boolean(dto.getBGesperrt()) == false) {
						dto.setBManuellgeaendert(Helper.boolean2Short(false));
						dto.setIPunkte(iPunkte);
						dto.setCKlasse(klasse);
					}

					try {
						getLieferantFac().updateLieferantbeurteilung(dto, theClientDto);
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				} catch (javax.ejb.EJBException e) {
					// Wenn nichts gefunden, dann neu anlegen
					// Beurteilung zurueckschreiben
					LieferantbeurteilungDto lbDto = new LieferantbeurteilungDto();

					// Durchschnitt
					lbDto.setIPunkte(iPunkte);
					lbDto.setBGesperrt(Helper.boolean2Short(false));
					lbDto.setTDatum(tBeurteilungsdatum);

					lbDto.setLieferantIId(lieferantIIdBeurteilung);
					lbDto.setCKlasse(klasse);
					lbDto.setBManuellgeaendert(Helper.boolean2Short(false));
					try {
						getLieferantFac().createLieferantbeurteilung(lbDto, theClientDto);
					} catch (RemoteException ez) {
						throwEJBExceptionLPRespectOld(ez);
					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				iAnzahlWareneingaengeProLieferant = 0;
				iAnzahlPunkteProLieferant = 0;
			}

			iLetzterLieferant = wareneingangspositionen.getFlrbestellposition().getFlrbestellung().getFlrlieferant()
					.getI_id();
			row++;
		}

		initJRDS(parameter, ReklamationReportFac.REPORT_MODUL, ReklamationReportFac.REPORT_LIEFERANTENBEURTEILUNG,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		JasperPrintLP print = getReportPrint();

		print.putAdditionalInformation("LIEFERANTEN",
				(Integer[]) hmLieferanten.keySet().toArray(new Integer[hmLieferanten.keySet().size()]));

		return print;
	}

	private ArrayList<FLRWareneingangspositionen> sortiereNachLieferantBestellnummerWEDatumSollTermin(
			Iterator<?> resultListIteratorWEP, TheClientDto theClientDto) {
		ArrayList<FLRWareneingangspositionen> alWEPos = new ArrayList<FLRWareneingangspositionen>();
		while (resultListIteratorWEP.hasNext()) {
			FLRWareneingangspositionen wareneingangspositionen = (FLRWareneingangspositionen) resultListIteratorWEP
					.next();
			alWEPos.add(wareneingangspositionen);
		}

		// Nach Lieferant/WE-Datum + Solltermin sortiern
		for (int i = alWEPos.size() - 1; i > 0; --i) {
			for (int j = 0; j < i; ++j) {
				FLRWareneingangspositionen o = alWEPos.get(j);
				FLRWareneingangspositionen o1 = alWEPos.get(j + 1);

				// Lieferant aufsteigend

				String l1 = Helper
						.fitString2Length(o.getFlrwareneingang().getFlrbestellung().getFlrlieferant().getFlrpartner()
								.getC_name1nachnamefirmazeile1(), 100, ' ')
						+ Helper.fitString2LengthAlignRight(
								o.getFlrwareneingang().getFlrbestellung().getFlrlieferant().getI_id() + "", 20, ' ');
				String l2 = Helper
						.fitString2Length(o1.getFlrwareneingang().getFlrbestellung().getFlrlieferant().getFlrpartner()
								.getC_name1nachnamefirmazeile1(), 100, ' ')
						+ Helper.fitString2LengthAlignRight(
								o1.getFlrwareneingang().getFlrbestellung().getFlrlieferant().getI_id() + "", 20, ' ');

				if (l1.compareTo(l2) > 0) {
					alWEPos.set(j, o1);
					alWEPos.set(j + 1, o);

				} else if (l1.equals(l2)) {
					// Danach nach Bestellnummer absteigend

					String b1 = o.getFlrwareneingang().getFlrbestellung().getC_nr();
					String b2 = o1.getFlrwareneingang().getFlrbestellung().getC_nr();
					if (b2.compareTo(b1) > 0) {
						alWEPos.set(j, o1);
						alWEPos.set(j + 1, o);

					} else if (b1.equals(b2)) {
						String w1 = Helper.fitString2LengthAlignRight(
								o.getFlrwareneingang().getT_wareneingansdatum().getTime() + "", 20, ' ') + " "
								+ o.getFlrwareneingang().getI_id();
						String w2 = Helper.fitString2LengthAlignRight(
								o1.getFlrwareneingang().getT_wareneingansdatum().getTime() + "", 20, ' ') + " "
								+ o.getFlrwareneingang().getI_id();

						if (w1.compareTo(w2) > 0) {
							alWEPos.set(j, o1);
							alWEPos.set(j + 1, o);
						} else if (w1.equals(w2)) {
							String d1 = Helper.fitString2LengthAlignRight(berechneSollTermin(o,theClientDto).getTime() + "", 20,
									' ');
							String d2 = Helper.fitString2LengthAlignRight(berechneSollTermin(o1,theClientDto).getTime() + "", 20,
									' ');
							if (d1.compareTo(d2) > 0) {
								alWEPos.set(j, o1);
								alWEPos.set(j + 1, o);
							}
						}

					}
				}
			}
		}
		return alWEPos;
	}

	private java.util.Date berechneSollTermin(FLRWareneingangspositionen wareneingangspositionen, TheClientDto theClientDto) {
		// Solltermin:
		java.util.Date sollTermin = null;

		//SP7115
		boolean bMitUrsprungstermin=true;
		try {
			ParametermandantDto parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_URSPRUNGSTERMIN_FUER_LIEFERANTENBEURTEILUNG);
			bMitUrsprungstermin = (Boolean) parameterDto.getCWertAsObject();
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		} 
		if (bMitUrsprungstermin) {
			sollTermin = wareneingangspositionen.getFlrbestellposition().getT_abursprungstermin();
		}
		if (sollTermin == null
				&& wareneingangspositionen.getFlrbestellposition().getT_auftragsbestaetigungstermin() != null) {
			sollTermin = wareneingangspositionen.getFlrbestellposition().getT_auftragsbestaetigungstermin();
		}
		if (sollTermin == null
				&& wareneingangspositionen.getFlrbestellposition().getT_uebersteuerterliefertermin() != null) {
			sollTermin = wareneingangspositionen.getFlrbestellposition().getT_uebersteuerterliefertermin();
		}
		if (sollTermin == null
				&& wareneingangspositionen.getFlrwareneingang().getFlrbestellung().getT_liefertermin() != null) {
			sollTermin = wareneingangspositionen.getFlrwareneingang().getFlrbestellung().getT_liefertermin();
		}
		return sollTermin;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printOffeneReklamationenEinesArtikels(Integer artikelIId, boolean bNurOffene,
			DatumsfilterVonBis vonbis, TheClientDto theClientDto) {
		sAktuellerReport = ReklamationReportFac.REPORT_OFFENEREKLAMATIONENEINESARTIKEL;

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session.createCriteria(FLRReklamation.class);
		crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));

		session.enableFilter("filterMandant").setParameter("paramMandant", theClientDto.getMandant());
		session.enableFilter("filterLocale").setParameter("paramLocale", theClientDto.getLocUiAsString());

		crit.createAlias(ReklamationFac.FLR_REKLAMATION_FLRARTIKEL, "a", CriteriaSpecification.LEFT_JOIN);
		crit.add(Restrictions.eq("a.i_id", artikelIId));

		if (bNurOffene) {
			crit.add(Restrictions.isNull(ReklamationFac.FLR_REKLAMATION_T_ERLEDIGT));
		}

		parameter.put("P_NUROFFENE", new Boolean(bNurOffene));

		if (vonbis != null) {
			parameter.put("P_VON", vonbis.getTimestampVon());
			parameter.put("P_BIS", vonbis.getTimestampBisUnveraendert());

			if (vonbis.getTimestampVon() != null) {
				crit.add(Restrictions.ge("t_belegdatum", vonbis.getTimestampVon()));
			}
			if (vonbis.getTimestampBis() != null) {
				crit.add(Restrictions.lt("t_belegdatum", vonbis.getTimestampBis()));
			}

		}

		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();

		data = new Object[results.size()][REPORT_OFFENEREKLAMATIONENEINESARTIKEL_ANZAHL_SPALTEN];
		int row = 0;
		while (resultListIterator.hasNext()) {
			FLRReklamation flrReklamation = (FLRReklamation) resultListIterator.next();
			data[row][REPORT_OFFENEREKLAMATIONENEINESARTIKEL_ART] = flrReklamation.getReklamationart_c_nr();
			data[row][REPORT_OFFENEREKLAMATIONENEINESARTIKEL_BELEGNUMMER] = flrReklamation.getC_nr();
			data[row][REPORT_OFFENEREKLAMATIONENEINESARTIKEL_DATUM] = new java.sql.Timestamp(
					flrReklamation.getT_belegdatum().getTime());
			data[row][REPORT_OFFENEREKLAMATIONENEINESARTIKEL_GRUND] = flrReklamation.getC_grund();

			data[row][REPORT_OFFENEREKLAMATIONENEINESARTIKEL_DATUM_ERLEDIGT] = flrReklamation.getT_erledigt();
			if (flrReklamation.getFlrfehler() != null) {
				data[row][REPORT_OFFENEREKLAMATIONENEINESARTIKEL_FEHLER] = flrReklamation.getFlrfehler().getC_bez();
			}
			if (flrReklamation.getFlrfehlerangabe() != null) {
				data[row][REPORT_OFFENEREKLAMATIONENEINESARTIKEL_FEHLERANGABE] = flrReklamation.getFlrfehlerangabe()
						.getC_bez();
			}

			if (flrReklamation.getKunde_i_id() != null) {
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(flrReklamation.getKunde_i_id(), theClientDto);
				data[row][REPORT_OFFENEREKLAMATIONENEINESARTIKEL_KUNDE] = kundeDto.getPartnerDto()
						.formatFixTitelName1Name2();
			}
			if (flrReklamation.getLieferant_i_id() != null) {
				LieferantDto lieferantDto = getLieferantFac()
						.lieferantFindByPrimaryKey(flrReklamation.getLieferant_i_id(), theClientDto);
				data[row][REPORT_OFFENEREKLAMATIONENEINESARTIKEL_LIEFERANT] = lieferantDto.getPartnerDto()
						.formatFixTitelName1Name2();
			}
			row++;
		}

		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(artikelIId, theClientDto);
		parameter.put("P_ARTIKEL", artikelDto.formatArtikelbezeichnung());
		parameter.put("P_ARTIKELREFERENZNUMMER", artikelDto.getCReferenznr());
		initJRDS(parameter, ReklamationReportFac.REPORT_MODUL,
				ReklamationReportFac.REPORT_OFFENEREKLAMATIONENEINESARTIKEL, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printLieferantentermintreue(Timestamp tVon, Timestamp tBis, Integer lieferantIId,
			TheClientDto theClientDto) {

		index = -1;
		sAktuellerReport = ReklamationReportFac.REPORT_LEFERANTENTERMINTREUE;

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		parameter.put("P_VON", tVon);
		parameter.put("P_BIS", tBis);

		tVon = Helper.cutTimestamp(tVon);
		tBis = Helper.cutTimestamp(Helper.addiereTageZuTimestamp(tBis, 1));

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session.createCriteria(FLRWareneingangspositionen.class);

		crit.createAlias("flrwareneingang", "w");
		crit.createAlias("w.flrbestellung", "b");
		crit.createAlias("b.flrlieferant", "l");
		crit.createAlias("l.flrpartner", "p");
		crit.add(Restrictions.ge("w.t_wareneingansdatum", tVon));
		crit.add(Restrictions.lt("w.t_wareneingansdatum", tBis));
		crit.add(Restrictions.eq("b.mandant_c_nr", theClientDto.getMandant()));

		if (lieferantIId != null) {
			crit.add(Restrictions.eq("l.i_id", lieferantIId));

			LieferantDto lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(lieferantIId, theClientDto);

			parameter.put("P_LIEFERANT", lieferantDto.getPartnerDto().formatFixTitelName1Name2());

		}

		crit.addOrder(Order.asc("p." + PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1));
		crit.addOrder(Order.desc("b.c_nr"));

		List<?> results = crit.list();
		Iterator<?> resultListIteratorWEP = results.iterator();

		HashMap hmWareneingaengeAbgehandelt = new HashMap();

		ArrayList<FLRWareneingangspositionen> alWEPos = sortiereNachLieferantBestellnummerWEDatumSollTermin(
				resultListIteratorWEP, theClientDto);

		data = new Object[results.size()][REPORT_LIEFERANTENTERMINTREUE_ANZAHL_SPALTEN];

		int row = 0;
		Iterator resultListIterator = alWEPos.iterator();
		while (resultListIterator.hasNext()) {
			FLRWareneingangspositionen wareneingangspositionen = (FLRWareneingangspositionen) resultListIterator.next();

			data[row][REPORT_LIEFERANTENTERMINTREUE_LIEFERANT] = wareneingangspositionen.getFlrwareneingang()
					.getFlrbestellung().getFlrlieferant().getFlrpartner().getC_name1nachnamefirmazeile1();

			data[row][REPORT_LIEFERANTENTERMINTREUE_LIEFERANT_I_ID] = wareneingangspositionen.getFlrwareneingang()
					.getFlrbestellung().getFlrlieferant().getI_id();

			Timestamp weDatum = Helper.cutTimestamp(
					new Timestamp(wareneingangspositionen.getFlrwareneingang().getT_wareneingansdatum().getTime()));

			data[row][REPORT_LIEFERANTENTERMINTREUE_WARENEINGANGSDATUM] = weDatum;
			data[row][REPORT_LIEFERANTENTERMINTREUE_BESTELLNUMMER] = wareneingangspositionen.getFlrwareneingang()
					.getFlrbestellung().getC_nr();
			data[row][REPORT_LIEFERANTENTERMINTREUE_WELIEFERSCHEIN] = wareneingangspositionen.getFlrwareneingang()
					.getC_lieferscheinnr();
			data[row][REPORT_LIEFERANTENTERMINTREUE_MENGE] = wareneingangspositionen.getN_geliefertemenge();
			data[row][REPORT_LIEFERANTENTERMINTREUE_ARTIKELNUMMER] = wareneingangspositionen.getFlrbestellposition()
					.getFlrartikel().getC_nr();

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
					wareneingangspositionen.getFlrbestellposition().getFlrartikel().getI_id(), theClientDto);
			data[row][REPORT_LIEFERANTENTERMINTREUE_BEZEICHNUNG] = artikelDto.formatBezeichnung();

			// if (wareneingangspositionen.getFlrbestellposition()
			// .getT_auftragsbestaetigungstermin() != null) {
			// sollTermin = wareneingangspositionen.getFlrbestellposition()
			// .getT_auftragsbestaetigungstermin();
			// }

			java.util.Date sollTermin = berechneSollTermin(wareneingangspositionen, theClientDto);

			data[row][REPORT_LIEFERANTENTERMINTREUE_SOLLTERMIN] = sollTermin;

			data[row][REPORT_LIEFERANTENTERMINTREUE_VERSPAETUNG] = getVerspaetungstage(weDatum, sollTermin);

			row++;
		}

		initJRDS(parameter, ReklamationReportFac.REPORT_MODUL, ReklamationReportFac.REPORT_LEFERANTENTERMINTREUE,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	private Integer getPunkteTermin(Integer iTageAbweichung, TermintreueDto[] dtos) {

		int iPunkte = 0;
		if (dtos != null) {
			if (iTageAbweichung >= 0) {
				for (int i = 0; i < dtos.length; i++) {
					if (dtos[i].getITage() >= 0) {

						if (iTageAbweichung >= dtos[i].getITage()) {
							iPunkte = dtos[i].getIPunkte();
						}

					}

				}
			} else {

				for (int i = dtos.length - 1; i >= 0; i--) {

					if (dtos[i].getITage() <= 0) {

						if (iTageAbweichung <= dtos[i].getITage()) {
							iPunkte = dtos[i].getIPunkte();
						}

					}

				}
			}

		}

		return iPunkte;

	}

	private Integer getVerspaetungstage(java.util.Date t1, java.util.Date t2) {

		if (t1.after(t2)) {
			// Verspaetung in Werktagen (ohne Samstag/ Sonntag/ Feiertag lt.
			// Betriebskalender)
			int iAnzahl = 0;

			while (t2.before(t1)) {

				Calendar c = Calendar.getInstance();
				c.setTime(t2);
				c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);

				if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
						|| c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {

				} else {
					iAnzahl++;
				}
				t2 = new Timestamp(c.getTimeInMillis());

			}

			return iAnzahl;
		} else if (t1.equals(t2)) {
			return 0;
		} else {
			java.util.Date temp = t1;
			t1 = t2;
			t2 = temp;

			// Verspaetung in Werktagen (ohne Samstag/ Sonntag/ Feiertag lt.
			// Betriebskalender)
			int iAnzahl = 0;

			while (t2.before(t1)) {

				Calendar c = Calendar.getInstance();
				c.setTime(t2);
				c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);

				if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
						|| c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {

				} else {
					iAnzahl++;
				}
				t2 = new Timestamp(c.getTimeInMillis());

			}

			return iAnzahl * -1;
		}

	}

	public JasperPrintLP printReklamation(Integer reklamationIId, boolean druckeUnterartLieferant,
			TheClientDto theClientDto) {

		index = -1;
		if (druckeUnterartLieferant == true) {
			sAktuellerReport = ReklamationReportFac.REPORT_REKLAMATION_LIEFERANT;
		} else {
			sAktuellerReport = ReklamationReportFac.REPORT_REKLAMATION;
		}

		data = new Object[0][0];

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		ReklamationDto reklamationDto = null;
		PartnerDto partnerDto = null;
		Locale loc = theClientDto.getLocUi();
		Locale locDruck = theClientDto.getLocUi();
		try {
			reklamationDto = getReklamationFac().reklamationFindByPrimaryKey(reklamationIId);

			parameter.put("P_KNDLSNR", reklamationDto.getCKdlsnr());
			parameter.put("P_SNRCHNR", reklamationDto.getCSeriennrchargennr());
			parameter.put("P_KNDREKLANR", reklamationDto.getCKdreklanr());

			if (reklamationDto.getMaschineIId() != null) {
				MaschineDto maschineDto = getZeiterfassungFac()
						.maschineFindByPrimaryKey(reklamationDto.getMaschineIId());
				parameter.put("P_MASCHINE_BEZEICHNUNG", maschineDto.getCBez());
				parameter.put("P_MASCHINE_IDENTIFIKATIONSNUMMER", maschineDto.getCIdentifikationsnr());
				parameter.put("P_MASCHINE_INVENTARNUMMER", maschineDto.getCInventarnummer());
			}

			parameter.put("P_BELEGNUMMER", reklamationDto.getCNr());

			parameter.put("P_MENGE", reklamationDto.getNMenge());
			parameter.put("P_ANALYSE", reklamationDto.getXAnalyse());
			parameter.put("P_KOMMENTAR", reklamationDto.getXKommentar());
			parameter.put("P_KOSTENAZ", reklamationDto.getNKostenarbeitszeit());
			parameter.put("P_KOSTENMATERIAL", reklamationDto.getNKostenmaterial());
			parameter.put("P_GRUND", reklamationDto.getCGrund());
			parameter.put("P_GRUNDKOMMENTAR", reklamationDto.getXGrundLang());
			parameter.put("P_PROJEKT", reklamationDto.getCProjekt());
			parameter.put("P_BELEGDATUM", reklamationDto.getTBelegdatum());
			parameter.put("P_ANLAGEDATUM", reklamationDto.getTAnlegen());
			parameter.put("P_WARE_ERHALTEN", reklamationDto.getTWareErhalten());
			parameter.put("P_RUECKSPRACHEAM", reklamationDto.getTRuecksprache());
			parameter.put("P_RUECKSPRACHEMIT", reklamationDto.getCRuecksprachemit());
			parameter.put("P_REKLAMATIONSART", reklamationDto.getReklamationartCNr());
			parameter.put("P_BERECHTIGT", Helper.short2Boolean(reklamationDto.getBBerechtigt()));
			parameter.put("P_FREMDPRODUKT", Helper.short2Boolean(reklamationDto.getBFremdprodukt()));
			parameter.put("P_MASSNAHMEKURZEINGEFUEHRT", reklamationDto.getTEingefuehrtkurz());
			parameter.put("P_MASSNAHMEMITTELEINGEFUEHRT", reklamationDto.getTEingefuehrtmittel());
			parameter.put("P_MASSNAHMELANGEINGEFUEHRT", reklamationDto.getTEingefuehrtlang());
			parameter.put("P_MASSNAHMEKURZKOMMENTAR", reklamationDto.getXMassnahmeKurz());
			parameter.put("P_MASSNAHMEMITTELKOMMENTAR", reklamationDto.getXMassnahmeMittel());
			parameter.put("P_MASSNAHMELANGKOMMENTAR", reklamationDto.getXMassnahmeLang());
			parameter.put("P_MASSNAHMEKURZGEPLANT", reklamationDto.getTMassnahmebiskurz());
			parameter.put("P_MASSNAHMEMITTELGEPLANT", reklamationDto.getTMassnahmebismittel());
			parameter.put("P_MASSNAHMELANGGEPLANT", reklamationDto.getTMassnahmebislang());

			parameter.put("P_WIRKSAMKEITGEPLANT", reklamationDto.getTWirksamkeitbis());
			parameter.put("P_WIRKSAMKEITDURCHGEFUEHRT", reklamationDto.getTWirksamkeiteingefuehrt());

			parameter.put("P_BETRIFFTLAGERSTAND", Helper.short2Boolean(reklamationDto.getBBetrifftlagerstand()));
			parameter.put("P_BETRIFFTGELIEFERTE", Helper.short2Boolean(reklamationDto.getBBetrifftgelieferte()));

			parameter.put("P_STUECKLAGERSTAND", reklamationDto.getNStuecklagerstand());
			parameter.put("P_STUECKGELIEFERTE", reklamationDto.getNStueckgelieferte());

			if (Helper.short2boolean(reklamationDto.getBArtikel()) == true && reklamationDto.getArtikelIId() != null) {
				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(reklamationDto.getArtikelIId(),
						theClientDto);
				parameter.put("P_ARTIKEL", artikelDto.formatArtikelbezeichnung());

				parameter.put("P_ARTIKELNUMMER", artikelDto.getCNr());

				parameter.put("P_ARTIKELBEZEICHNUNG", artikelDto.getCBezAusSpr());

				parameter.put("P_ARTIKELKURZBEZEICHNUNG", artikelDto.getKbezAusSpr());

				parameter.put("P_ARTIKELZUSATZBEZEICHNUNG", artikelDto.getCZBezAusSpr());

				parameter.put("P_ARTIKELZUSATZBEZEICHNUNG2", artikelDto.getCZBez2AusSpr());

			} else {
				parameter.put("P_ARTIKEL", reklamationDto.getCHandartikel());
			}
			parameter.put("P_KOSTENSTELLE",
					getSystemFac().kostenstelleFindByPrimaryKey(reklamationDto.getKostenstelleIId())
							.formatKostenstellenbezeichnung());

			if (reklamationDto.getPersonalIIdAnlegen() != null) {
				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKey(reklamationDto.getPersonalIIdAnlegen(), theClientDto);
				parameter.put("P_PERSONANGELEGT", personalDto.getPartnerDto().formatTitelAnrede());
			}

			if (reklamationDto.getPersonalIIdAufnehmer() != null) {
				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKey(reklamationDto.getPersonalIIdAufnehmer(), theClientDto);
				parameter.put("P_PERSONAUFNEHMER", personalDto.getPartnerDto().formatTitelAnrede());

				// PJ19413

				parameter.put("P_AUFNEHMER_ANSPRECHPARTNERTELEFON", personalDto.getCTelefon());
				parameter.put("P_AUFNEHMER_ANSPRECHPARTNEREMAIL", personalDto.getCEmail());
				parameter.put("P_AUFNEHMER_ANSPRECHPARTNERHANDY", personalDto.getCHandy());
				parameter.put("P_AUFNEHMER_ANSPRECHPARTNERFAX", personalDto.getCFax());
				parameter.put("P_AUFNEHMER_ANSPRECHPARTNERDIREKTFAX", personalDto.getCDirektfax());

			}
			if (reklamationDto.getPersonalIIdEingefuehrtkurz() != null) {
				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKey(reklamationDto.getPersonalIIdEingefuehrtkurz(), theClientDto);
				parameter.put("P_PERSONMASSNAHMEKURZ", personalDto.getPartnerDto().formatTitelAnrede());
			}
			if (reklamationDto.getPersonalIIdEingefuehrtmittel() != null) {
				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKey(reklamationDto.getPersonalIIdEingefuehrtmittel(), theClientDto);
				parameter.put("P_PERSONMASSNAHMEMITTEL", personalDto.getPartnerDto().formatTitelAnrede());
			}
			if (reklamationDto.getPersonalIIdEingefuehrtlang() != null) {
				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKey(reklamationDto.getPersonalIIdEingefuehrtlang(), theClientDto);
				parameter.put("P_PERSONMASSNAHMELANG", personalDto.getPartnerDto().formatTitelAnrede());
			}
			if (reklamationDto.getPersonalIIdRuecksprache() != null) {
				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKey(reklamationDto.getPersonalIIdRuecksprache(), theClientDto);
				parameter.put("P_PERSONRUECKSPRACHE", personalDto.getPartnerDto().formatTitelAnrede());
			}

			if (reklamationDto.getPersonalIIdWirksamkeit() != null) {
				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKey(reklamationDto.getPersonalIIdWirksamkeit(), theClientDto);
				parameter.put("P_PERSONWIRKSAMKEIT", personalDto.getPartnerDto().formatTitelAnrede());
			}

			if (reklamationDto.getPersonalIIdErledigt() != null) {
				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKey(reklamationDto.getPersonalIIdErledigt(), theClientDto);
				parameter.put("P_PERSONERLEDIGT", personalDto.getPartnerDto().formatTitelAnrede());
			}
			parameter.put("P_ERLEDIGT", reklamationDto.getTErledigt());

			if (reklamationDto.getWareneingangIId() != null) {
				WareneingangDto weDto = getWareneingangFac()
						.wareneingangFindByPrimaryKey(reklamationDto.getWareneingangIId());
				parameter.put("P_WARENEINGANG_LS_NUMMER", weDto.getCLieferscheinnr());
				parameter.put("P_WARENEINGANG_DATUM", weDto.getTWareneingangsdatum());
			}

			if (reklamationDto.getReklamationartCNr().equals(ReklamationFac.REKLAMATIONART_FERTIGUNG)
					|| reklamationDto.getReklamationartCNr().equals(ReklamationFac.REKLAMATIONART_KUNDE)) {

				if (reklamationDto.getLosIId() != null) {

					LosDto losDto = getFertigungFac().losFindByPrimaryKey(reklamationDto.getLosIId());

					parameter.put("P_REKLAMATIONSBELEG", "LO" + losDto.getCNr());

					if (losDto.getAuftragIId() != null) {
						BelegInfos beleg = getLagerFac().getBelegInfos(LocaleFac.BELEGART_AUFTRAG,
								losDto.getAuftragIId(), null, theClientDto);
						parameter.put("P_KUNDELIEFERANT", beleg.getKundeLieferant());

						com.lp.server.auftrag.service.AuftragDto auftragDto = getAuftragFac()
								.auftragFindByPrimaryKey(losDto.getAuftragIId());

						KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(),
								theClientDto);
						partnerDto = kundeDto.getPartnerDto();

						// PJ18870

						parameter.put("P_SUBREPORT_PARTNERKOMMENTAR_LIEFERANT",
								getPartnerServicesFacLocal().getSubreportAllerMitzudruckendenPartnerkommentare(
										kundeDto.getPartnerDto().getIId(), true, LocaleFac.BELEGART_REKLAMATION,
										theClientDto));

					}
				}
				if (reklamationDto.getMaschineIId() != null) {
					MaschineDto maschineDto = getZeiterfassungFac()
							.maschineFindByPrimaryKey(reklamationDto.getMaschineIId());

					parameter.put("P_MASCHINE", maschineDto.getBezeichnung());

					MaschinengruppeDto mgrDto = getZeiterfassungFac()
							.maschinengruppeFindByPrimaryKey(maschineDto.getMaschinengruppeIId());

					parameter.put("P_MASCHINENGRUPPE", mgrDto.getCBez());

				}
				if (reklamationDto.getLosIId() != null) {

					LosDto losDto = getFertigungFac().losFindByPrimaryKey(reklamationDto.getLosIId());

					parameter.put("P_LOSNUMMER", losDto.getCNr());
				}
				if (reklamationDto.getLossollarbeitsplanIId() != null) {
					LossollarbeitsplanDto sollaDto = getFertigungFac()
							.lossollarbeitsplanFindByPrimaryKey(reklamationDto.getLossollarbeitsplanIId());

					String s = "AG: " + sollaDto.getIArbeitsgangnummer() + "";
					if (sollaDto.getIUnterarbeitsgang() != null) {
						s += ", UAG: " + sollaDto.getIUnterarbeitsgang() + "";
					}

					parameter.put("P_ARBEITSGANG", s);
				}

				if (reklamationDto.getPersonalIIdVerursacher() != null) {
					PersonalDto personalDto = getPersonalFac()
							.personalFindByPrimaryKey(reklamationDto.getPersonalIIdVerursacher(), theClientDto);
					parameter.put("P_PERSONVERURSACHER", personalDto.getPartnerDto().formatTitelAnrede());
				}

			}

			if (reklamationDto.getReklamationartCNr().equals(ReklamationFac.REKLAMATIONART_KUNDE)) {

				if (reklamationDto.getIKundeunterart() != null) {
					if (reklamationDto.getIKundeunterart()
							.intValue() == ReklamationFac.REKLAMATION_KUNDEUNTERART_FERTIGUNG) {
						parameter.put("P_KUNDEUNTERART", "Fertigung");
					} else {
						parameter.put("P_KUNDEUNTERART", "Lieferant");
					}
				}

				if (reklamationDto.getLieferantIId() != null) {

					LieferantDto lieferantDto = getLieferantFac()
							.lieferantFindByPrimaryKey(reklamationDto.getLieferantIId(), theClientDto);

					if (druckeUnterartLieferant == true) {
						locDruck = Helper.string2Locale(lieferantDto.getPartnerDto().getLocaleCNrKommunikation());
					}

					AnsprechpartnerDto ansprechpartnerDto = null;
					if (reklamationDto.getAnsprechpartnerIIdLieferant() != null) {
						ansprechpartnerDto = getAnsprechpartnerFac().ansprechpartnerFindByPrimaryKey(
								reklamationDto.getAnsprechpartnerIIdLieferant(), theClientDto);

						// PJ19413

						String sEmail = getPartnerFac().partnerkommFindOhneAnpassungOhneExec(lieferantDto.getPartnerDto().getIId(),
								ansprechpartnerDto.getIId(), PartnerFac.KOMMUNIKATIONSART_EMAIL,
								theClientDto.getMandant(), theClientDto);
						String sFax = getPartnerFac().partnerkommFindOhneAnpassungOhneExec(lieferantDto.getPartnerDto().getIId(),
								ansprechpartnerDto.getIId(), PartnerFac.KOMMUNIKATIONSART_FAX,
								theClientDto.getMandant(), theClientDto);
						String sTelefon = getPartnerFac().partnerkommFindOhneAnpassungOhneExec(lieferantDto.getPartnerDto().getIId(),
								ansprechpartnerDto.getIId(),
								PartnerFac.KOMMUNIKATIONSART_TELEFON, theClientDto.getMandant(), theClientDto);

						parameter.put("P_LIEFERANT_ANSPRECHPARTNEREMAIL", sEmail);
						parameter.put("P_LIEFERANT_ANSPRECHPARTNERFAX", sFax);

						parameter.put("P_LIEFERANT_ANSPRECHPARTNERTELEFON", sTelefon);

					}
					if (lieferantDto != null) {
						loc = Helper.string2Locale(lieferantDto.getPartnerDto().getLocaleCNrKommunikation());
						parameter.put("P_LIEFERANT_ADRESSBLOCK",
								formatAdresseFuerAusdruck(
										lieferantDto.getPartnerDto(), ansprechpartnerDto, getMandantFac()
												.mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto),
										loc));

						// PJ18870
						parameter.put("P_SUBREPORT_PARTNERKOMMENTAR_LIEFERANT",
								getPartnerServicesFacLocal().getSubreportAllerMitzudruckendenPartnerkommentare(
										lieferantDto.getPartnerDto().getIId(), false, LocaleFac.BELEGART_REKLAMATION,
										theClientDto));

					}
				}

				if (reklamationDto.getBestellungIId() != null) {
					BelegInfos beleg = getLagerFac().getBelegInfos(LocaleFac.BELEGART_BESTELLUNG,
							reklamationDto.getBestellungIId(), null, theClientDto);
					parameter.put("P_BESTELLUNG", beleg.getBelegnummer());
				} else {
					parameter.put("P_BESTELLUNG", reklamationDto.getCBestellnummer());
				}

				if (reklamationDto.getKundeIId() != null) {
					KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(reklamationDto.getKundeIId(), theClientDto);
					partnerDto = kundeDto.getPartnerDto();
					parameter.put("P_KUNDELIEFERANT", kundeDto.getPartnerDto().formatFixTitelName1Name2());

					if (druckeUnterartLieferant == false) {
						locDruck = Helper.string2Locale(kundeDto.getPartnerDto().getLocaleCNrKommunikation());
					}

					// PJ18870
					parameter.put("P_SUBREPORT_PARTNERKOMMENTAR_KUNDE",
							getPartnerServicesFacLocal().getSubreportAllerMitzudruckendenPartnerkommentare(
									kundeDto.getPartnerDto().getIId(), true, LocaleFac.BELEGART_REKLAMATION,
									theClientDto));

					// PJ19413
					if (reklamationDto.getAnsprechpartnerIId() != null) {

						AnsprechpartnerDto oAnsprechpartner = getAnsprechpartnerFac()
								.ansprechpartnerFindByPrimaryKey(reklamationDto.getAnsprechpartnerIId(), theClientDto);

						String sEmail = getPartnerFac().partnerkommFindOhneAnpassungOhneExec(kundeDto.getPartnerDto().getIId(),
								oAnsprechpartner.getIId(), PartnerFac.KOMMUNIKATIONSART_EMAIL,
								theClientDto.getMandant(), theClientDto);
						String sFax = getPartnerFac().partnerkommFindOhneAnpassungOhneExec(kundeDto.getPartnerDto().getIId(),
								oAnsprechpartner.getIId(), PartnerFac.KOMMUNIKATIONSART_FAX,
								theClientDto.getMandant(), theClientDto);
						String sTelefon = getPartnerFac().partnerkommFindOhneAnpassungOhneExec(kundeDto.getPartnerDto().getIId(),
								oAnsprechpartner.getIId(), PartnerFac.KOMMUNIKATIONSART_TELEFON,
								theClientDto.getMandant(), theClientDto);

						parameter.put("P_KUNDELIEFERANT_ANSPRECHPARTNEREMAIL", sEmail);
						parameter.put("P_KUNDELIEFERANT_ANSPRECHPARTNERFAX", sFax);
						parameter.put("P_KUNDELIEFERANT_ANSPRECHPARTNERTELEFON", sTelefon);

					}

				}
				if (reklamationDto.getLieferscheinIId() != null) {
					BelegInfos beleg = getLagerFac().getBelegInfos(LocaleFac.BELEGART_LIEFERSCHEIN,
							reklamationDto.getLieferscheinIId(), null, theClientDto);
					parameter.put("P_REKLAMATIONSBELEG", "LS" + beleg.getBelegnummer());
				}
				if (reklamationDto.getRechnungIId() != null) {
					BelegInfos beleg = getLagerFac().getBelegInfos(LocaleFac.BELEGART_RECHNUNG,
							reklamationDto.getRechnungIId(), null, theClientDto);
					parameter.put("P_REKLAMATIONSBELEG2", "RE" + beleg.getBelegnummer());
				}

			} else if (reklamationDto.getReklamationartCNr().equals(ReklamationFac.REKLAMATIONART_LIEFERANT)) {

				// PJ19413
				parameter.put("P_LFLSNR", reklamationDto.getCLflsnr());
				parameter.put("P_LFREKLANR", reklamationDto.getCLfreklanr());

				if (reklamationDto.getLieferantIId() != null) {
					LieferantDto lieferantDto = getLieferantFac()
							.lieferantFindByPrimaryKey(reklamationDto.getLieferantIId(), theClientDto);

					partnerDto = lieferantDto.getPartnerDto();

					// SP3898
					locDruck = Helper.string2Locale(lieferantDto.getPartnerDto().getLocaleCNrKommunikation());

					parameter.put("P_KUNDELIEFERANT", lieferantDto.getPartnerDto().formatFixTitelName1Name2());
					// PJ18870
					parameter.put("P_SUBREPORT_PARTNERKOMMENTAR_LIEFERANT",
							getPartnerServicesFacLocal().getSubreportAllerMitzudruckendenPartnerkommentare(
									lieferantDto.getPartnerDto().getIId(), false, LocaleFac.BELEGART_REKLAMATION,
									theClientDto));

					// PJ19413
					if (reklamationDto.getAnsprechpartnerIIdLieferant() != null) {

						AnsprechpartnerDto oAnsprechpartner = getAnsprechpartnerFac().ansprechpartnerFindByPrimaryKey(
								reklamationDto.getAnsprechpartnerIIdLieferant(), theClientDto);

						String sEmail = getPartnerFac().partnerkommFindOhneAnpassungOhneExec(lieferantDto.getPartnerDto().getIId(),
								oAnsprechpartner.getIId(), PartnerFac.KOMMUNIKATIONSART_EMAIL,
								theClientDto.getMandant(), theClientDto);
						String sFax = getPartnerFac().partnerkommFindOhneAnpassungOhneExec(lieferantDto.getPartnerDto().getIId(),
								oAnsprechpartner.getIId(), PartnerFac.KOMMUNIKATIONSART_FAX,
								theClientDto.getMandant(), theClientDto);
						String sTelefon = getPartnerFac().partnerkommFindOhneAnpassungOhneExec(lieferantDto.getPartnerDto().getIId(),
								oAnsprechpartner.getIId(), PartnerFac.KOMMUNIKATIONSART_TELEFON,
								theClientDto.getMandant(), theClientDto);

						parameter.put("P_KUNDELIEFERANT_ANSPRECHPARTNEREMAIL", sEmail);
						parameter.put("P_KUNDELIEFERANT_ANSPRECHPARTNERFAX", sFax);
						parameter.put("P_KUNDELIEFERANT_ANSPRECHPARTNERTELEFON", sTelefon);

					}

				}
				if (reklamationDto.getBestellungIId() != null) {
					BelegInfos beleg = getLagerFac().getBelegInfos(LocaleFac.BELEGART_BESTELLUNG,
							reklamationDto.getBestellungIId(), null, theClientDto);
					parameter.put("P_REKLAMATIONSBELEG", "BS" + beleg.getBelegnummer());
				} else {
					parameter.put("P_REKLAMATIONSBELEG", reklamationDto.getCBestellnummer());
				}

				if (reklamationDto.getWareneingangIId() != null) {
					WareneingangDto weDto = getWareneingangFac()
							.wareneingangFindByPrimaryKey(reklamationDto.getWareneingangIId());
					parameter.put("P_REKLAMATIONSBELEG_ZUSATZ", weDto.getCLieferscheinnr());
				} else {
					parameter.put("P_REKLAMATIONSBELEG_ZUSATZ", reklamationDto.getCWareneingang());
				}

			}
			theClientDto.setUiLoc(locDruck);

			if (reklamationDto.getFehlerIId() != null) {
				parameter.put("P_FEHLER", getReklamationFac()
						.fehlerFindByPrimaryKey(reklamationDto.getFehlerIId(), theClientDto).getBezeichnung());
			}
			if (reklamationDto.getAufnahmeartIId() != null) {
				parameter.put("P_AUFNAHMEART",
						getReklamationFac()
								.aufnahmeartFindByPrimaryKey(reklamationDto.getAufnahmeartIId(), theClientDto)
								.getBezeichnung());
			}
			if (reklamationDto.getWirksamkeitIId() != null) {
				parameter.put("P_WIRKSAMKEIT",
						getReklamationFac()
								.wirksamkeitFindByPrimaryKey(reklamationDto.getWirksamkeitIId(), theClientDto)
								.getBezeichnung());
			}

			if (reklamationDto.getSchwereIId() != null) {

				parameter.put("P_SCHWERE", getReklamationFac()
						.schwereFindByPrimaryKey(reklamationDto.getSchwereIId(), theClientDto).formatBezeichnung());
			}

			parameter.put("P_WIRKSAMKEITKOMMENTAR", reklamationDto.getXWirksamkeit());

			if (reklamationDto.getFehlerangabeIId() != null) {
				parameter.put("P_FEHLERANGABE",
						getReklamationFac()
								.fehlerangabeFindByPrimaryKey(reklamationDto.getFehlerangabeIId(), theClientDto)
								.getBezeichnung());
			}

			if (reklamationDto.getMassnahmeIIdKurz() != null) {
				parameter.put("P_MASSNAHMEKURZ",
						getReklamationFac()
								.massnahmeFindByPrimaryKey(reklamationDto.getMassnahmeIIdKurz(), theClientDto)
								.getBezeichnung());
			}
			if (reklamationDto.getMassnahmeIIdMittel() != null) {
				parameter.put("P_MASSNAHMEMITTEL",
						getReklamationFac()
								.massnahmeFindByPrimaryKey(reklamationDto.getMassnahmeIIdMittel(), theClientDto)
								.getBezeichnung());
			}
			if (reklamationDto.getMassnahmeIIdLang() != null) {
				parameter.put("P_MASSNAHMELANG",
						getReklamationFac()
								.massnahmeFindByPrimaryKey(reklamationDto.getMassnahmeIIdLang(), theClientDto)
								.getBezeichnung());
			}

			AnsprechpartnerDto ansprechpartnerDto = null;

			if (reklamationDto.getReklamationartCNr().equals(ReklamationFac.REKLAMATIONART_LIEFERANT)) {
				if (reklamationDto.getAnsprechpartnerIIdLieferant() != null) {
					ansprechpartnerDto = getAnsprechpartnerFac().ansprechpartnerFindByPrimaryKey(
							reklamationDto.getAnsprechpartnerIIdLieferant(), theClientDto);
				}
			} else {
				if (reklamationDto.getAnsprechpartnerIId() != null) {
					ansprechpartnerDto = getAnsprechpartnerFac()
							.ansprechpartnerFindByPrimaryKey(reklamationDto.getAnsprechpartnerIId(), theClientDto);
				}
			}

			if (partnerDto != null) {
				loc = Helper.string2Locale(partnerDto.getLocaleCNrKommunikation());
				parameter.put("P_KUNDE_ADRESSBLOCK", formatAdresseFuerAusdruck(partnerDto, ansprechpartnerDto,
						getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto), loc));
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		data = new Object[0][2];

		try {
			ReklamationbildDto[] dtos = getReklamationFac().reklamationbildFindByReklamationIId(reklamationIId);

			// data = new Object[dtos.length][2];
			//
			// for (int i = 0; i < dtos.length; i++) {
			// data[i][REPORT_REKLAMATION_BILD] = Helper
			// .byteArrayToImage(dtos[i].getOBild());
			// data[i][REPORT_REKLAMATION_BILD_BEZEICHNUNG] = dtos[i]
			// .getCBez();
			// }
			List<Object[]> bilder = new ArrayList<Object[]>();
			for (ReklamationbildDto bildDto : dtos) {

				if (MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF.equals(bildDto.getDatenformatCNr())
						|| MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG.equals(bildDto.getDatenformatCNr())
						|| MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG.equals(bildDto.getDatenformatCNr())) {
					Object[] oZeile = new Object[2];
					oZeile[REPORT_REKLAMATION_BILD] = Helper.byteArrayToImage(bildDto.getOBild());
					oZeile[REPORT_REKLAMATION_BILD_BEZEICHNUNG] = bildDto.getCBez();
					bilder.add(oZeile);

				} else if (MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF.equals(bildDto.getDatenformatCNr())) {
					BufferedImage[] tiffs = Helper.tiffToImageArray(bildDto.getOBild());
					if (tiffs == null)
						continue;
					for (BufferedImage bImage : tiffs) {
						Object[] oZeile = new Object[2];
						oZeile[REPORT_REKLAMATION_BILD] = bImage;
						oZeile[REPORT_REKLAMATION_BILD_BEZEICHNUNG] = bildDto.getCBez();
						bilder.add(oZeile);
					}

				} else if (MediaFac.DATENFORMAT_MIMETYPE_APP_PDF.equals(bildDto.getDatenformatCNr())) {
					PDDocument document = null;
					try {
						document = PDDocument.load(new ByteArrayInputStream(bildDto.getOBild()));
						PDFRenderer renderer = new PDFRenderer(document);
						for (int pageIdx = 0; pageIdx < document.getNumberOfPages(); pageIdx++) {
							Object[] oZeile = new Object[2];
							oZeile[REPORT_REKLAMATION_BILD] = renderer.renderImageWithDPI(pageIdx, 150);
							oZeile[REPORT_REKLAMATION_BILD_BEZEICHNUNG] = bildDto.getCBez();
							bilder.add(oZeile);
						}
					} catch (IOException e) {
						myLogger.error("Exception waehrend Umwandlung von PDF in Image", e);
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e.getMessage());
					} finally {
						if (document != null) {
							try {
								document.close();
							} catch (IOException e) {
								myLogger.error("Exception waehrend Umwandlung von PDF in Image", e);
								throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e.getMessage());
							}
						}
					}
				}
			}

			data = new Object[bilder.size()][2];
			data = bilder.toArray(data);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		initJRDS(parameter, ReklamationReportFac.REPORT_MODUL, sAktuellerReport, theClientDto.getMandant(), locDruck,
				theClientDto);

		JasperPrintLP print = getReportPrint();
		PrintInfoDto values = new PrintInfoDto();
		values.setDocPath(new DocPath(new DocNodeReklamation(reklamationDto)));
		// values[0] = JCRDocFac.HELIUMV_NODE + "/"
		// + LocaleFac.BELEGART_REKLAMATION.trim() + "/"
		// + LocaleFac.BELEGART_REKLAMATION.trim() + "/"
		// + reklamationDto.getCNr().replace("/", ".");
		values.setiId(theClientDto.getIDPersonal());
		values.setTable("");
		if (partnerDto != null) {
			values.setiId(partnerDto.getIId());
		}

		print.setOInfoForArchive(values);

		return print;

	}

	public JasperPrintLP printFehlerarten(ReklamationFehlerartenJournalKriterienDto krit,
			TheClientDto theClientDto) {		
		sAktuellerReport = ReklamationReportFac.REPORT_FEHLERART;

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		Timestamp tVon = new Timestamp(krit.dVon.getTime());
		Timestamp tBis = new Timestamp(krit.dBis.getTime());
		
		parameter.put("P_VON", tVon);
		parameter.put("P_BIS", tBis);
		parameter.put("P_NURBERECHTIGTE", new Boolean(krit.nurBerechtigt));

		tVon = Helper.cutTimestamp(tVon);

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session.createCriteria(FLRReklamation.class);
		crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));

		session.enableFilter("filterMandant").setParameter("paramMandant", theClientDto.getMandant());
		session.enableFilter("filterLocale").setParameter("paramLocale", theClientDto.getLocUiAsString());

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tBis.getTime());
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);
		tBis = Helper.cutTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));

		crit.add(Restrictions.ge(ReklamationFac.FLR_REKLAMATION_T_BELEGDATUM, tVon));
		crit.add(Restrictions.lt(ReklamationFac.FLR_REKLAMATION_T_BELEGDATUM, tBis));

		crit.createAlias(ReklamationFac.FLR_REKLAMATION_FLRLIEFERANT, "l", CriteriaSpecification.LEFT_JOIN);
		crit.createAlias(ReklamationFac.FLR_REKLAMATION_FRLKUNDE, "k", CriteriaSpecification.LEFT_JOIN);

		crit.createAlias(ReklamationFac.FLR_REKLAMATION_FLRLOS, "lo", CriteriaSpecification.LEFT_JOIN);
		crit.createAlias("lo." + FertigungFac.FLR_LOSREPORT_FLRAUFTRAG, "au", CriteriaSpecification.LEFT_JOIN);

		crit.createAlias("l." + LieferantFac.FLR_PARTNER, "pl", CriteriaSpecification.LEFT_JOIN);
		crit.createAlias("k." + KundeFac.FLR_PARTNER, "kl", CriteriaSpecification.LEFT_JOIN);
		crit.createAlias(ReklamationFac.FLR_REKLAMATION_FLRFEHLERANGABE, "f", CriteriaSpecification.LEFT_JOIN);

		if (krit.nurBerechtigt) {
			crit.add(Restrictions.eq(ReklamationFac.FLR_REKLAMATION_B_BERECHTIGT, Helper.boolean2Short(true)));
		}

		ArrayList<String> alArten = new ArrayList<String>();
		if (krit.mitFertigung) {
			alArten.add(ReklamationFac.REKLAMATIONART_FERTIGUNG);
		}
		if (krit.mitKunde) {
			alArten.add(ReklamationFac.REKLAMATIONART_KUNDE);
		}
		if (krit.mitLieferant) {
			alArten.add(ReklamationFac.REKLAMATIONART_LIEFERANT);
		}

		crit.add(Restrictions.in(ReklamationFac.FLR_REKLAMATION_REKLAMATIONART_C_NR, alArten));

		if (alArten.size() == 0) {
			return null;
		}

		if (krit.kundeIId != null) {
			crit.add(Restrictions.or(Restrictions.eq("au.kunde_i_id_auftragsadresse", krit.kundeIId),
					Restrictions.eq(ReklamationFac.FLR_REKLAMATION_KUNDE_I_ID, krit.kundeIId)));

			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(krit.kundeIId, theClientDto);
			parameter.put("P_KUNDE", kundeDto.getPartnerDto().formatFixTitelName1Name2());
		}

		if (krit.gruppierung == ReklamationReportFac.SORTIERUNG_FEHLERART_MASCHINENGRUPPE
				|| krit.gruppierung == ReklamationReportFac.SORTIERUNG_FEHLERART_MASCHINENGRUPPE_MITARBEITER) {

			crit.createAlias(ReklamationFac.FLR_REKLAMATION_FLRMASCHINE, "masch", CriteriaSpecification.LEFT_JOIN);
			crit.createAlias("masch." + ZeiterfassungFac.FLR_MASCHINE_FLR_MASCHINENGRUPPE, "mgru",
					CriteriaSpecification.LEFT_JOIN);

			crit.addOrder(Order.asc("mgru.c_bez"));
		}

		if (krit.gruppierung == ReklamationReportFac.SORTIERUNG_FEHLERART_MITARBEITER
				|| krit.gruppierung == ReklamationReportFac.SORTIERUNG_FEHLERART_MASCHINENGRUPPE_MITARBEITER) {
			crit.createAlias(ReklamationFac.FLR_REKLAMATION_FLRVERURSACHER, "ver", CriteriaSpecification.LEFT_JOIN);
			crit.createAlias("ver." + PersonalFac.FLR_PERSONAL_FLRPARTNER, "part", CriteriaSpecification.LEFT_JOIN);
			crit.addOrder(Order.asc("part." + PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1));
			crit.addOrder(Order.asc("part." + PartnerFac.FLR_PARTNER_C_NAME2VORNAMEFIRMAZEILE2));
		}

		crit.addOrder(Order.asc("f.c_bez"));
		crit.addOrder(Order.asc("c_nr"));

		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();

		data = new Object[results.size()][REPORT_FEHLERART_ANZAHL_SPALTEN];
		int row = 0;

		if (krit.gruppierung == ReklamationReportFac.SORTIERUNG_FEHLERART_FEHLERART) {
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("rekla.fehlerart", theClientDto.getMandant(), theClientDto.getLocUi()));
		} else if (krit.gruppierung == ReklamationReportFac.SORTIERUNG_FEHLERART_MASCHINENGRUPPE) {
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.maschinengruppe", theClientDto.getMandant(), theClientDto.getLocUi()));
		} else if (krit.gruppierung == ReklamationReportFac.SORTIERUNG_FEHLERART_MITARBEITER) {
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("rekla.verursacher", theClientDto.getMandant(), theClientDto.getLocUi()));
		} else if (krit.gruppierung == ReklamationReportFac.SORTIERUNG_FEHLERART_MASCHINENGRUPPE_MITARBEITER) {
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.maschinengruppe", theClientDto.getMandant(), theClientDto.getLocUi())
							+ " + " + getTextRespectUISpr("rekla.verursacher", theClientDto.getMandant(),
									theClientDto.getLocUi()));
		}

		while (resultListIterator.hasNext()) {
			FLRReklamation flrReklamation = (FLRReklamation) resultListIterator.next();

			data[row][REPORT_FEHLERART_FEHLERART] = flrReklamation.getFlrfehlerangabe().getC_bez();
			data[row][REPORT_FEHLERART_REKLAMATIONSNUMMER] = flrReklamation.getC_nr();
			data[row][REPORT_FEHLERART_REKLAMATIONSART] = flrReklamation.getReklamationart_c_nr();
			data[row][REPORT_FEHLERART_MASCHINENGRUPPE] = "";

			if (flrReklamation.getFlrmaschine() != null) {
				data[row][REPORT_FEHLERART_MASCHINENGRUPPE] = flrReklamation.getFlrmaschine().getFlrmaschinengruppe()
						.getC_bez();
			}

			data[row][REPORT_FEHLERART_VERURSACHER] = "";
			if (flrReklamation.getFlrverursacher() != null) {
				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKey(flrReklamation.getFlrverursacher().getI_id(), theClientDto);

				data[row][REPORT_FEHLERART_VERURSACHER] = personalDto.formatAnrede();
			}
			data[row][REPORT_FEHLERART_KUNDE] = "";
			KundeDto kundeDto = null;
			if (flrReklamation.getReklamationart_c_nr().equals(ReklamationFac.REKLAMATIONART_KUNDE)
					&& flrReklamation.getKunde_i_id() != null) {

				kundeDto = getKundeFac().kundeFindByPrimaryKey(flrReklamation.getKunde_i_id(), theClientDto);

			} else if (flrReklamation.getReklamationart_c_nr().equals(ReklamationFac.REKLAMATIONART_FERTIGUNG)
					&& flrReklamation.getFlrlos() != null && flrReklamation.getFlrlos().getFlrauftrag() != null) {
				kundeDto = getKundeFac().kundeFindByPrimaryKey(
						flrReklamation.getFlrlos().getFlrauftrag().getKunde_i_id_auftragsadresse(), theClientDto);
			}

			if (kundeDto != null) {
				data[row][REPORT_FEHLERART_KUNDE] = kundeDto.getPartnerDto().formatTitelAnrede();
			}

			flrReklamation.getFlrfehlerangabe().getC_bez();
			data[row][REPORT_FEHLERART_FEHLERART] = flrReklamation.getFlrfehlerangabe().getC_bez();

			if (krit.gruppierung == ReklamationReportFac.SORTIERUNG_FEHLERART_FEHLERART) {
				data[row][REPORT_FEHLERART_GRUPPIERUNG] = data[row][REPORT_FEHLERART_FEHLERART];
			} else if (krit.gruppierung == ReklamationReportFac.SORTIERUNG_FEHLERART_MASCHINENGRUPPE) {
				data[row][REPORT_FEHLERART_GRUPPIERUNG] = data[row][REPORT_FEHLERART_MASCHINENGRUPPE];
			} else if (krit.gruppierung == ReklamationReportFac.SORTIERUNG_FEHLERART_MITARBEITER) {
				data[row][REPORT_FEHLERART_GRUPPIERUNG] = data[row][REPORT_FEHLERART_VERURSACHER];
			} else if (krit.gruppierung == ReklamationReportFac.SORTIERUNG_FEHLERART_MASCHINENGRUPPE_MITARBEITER) {
				data[row][REPORT_FEHLERART_GRUPPIERUNG] = data[row][REPORT_FEHLERART_MASCHINENGRUPPE] + " "
						+ data[row][REPORT_FEHLERART_VERURSACHER].toString();
			}
			
			data[row][REPORT_FEHLERART_GRUND] = flrReklamation.getC_grund();
			data[row][REPORT_FEHLERART_REKLAMATIONID] = flrReklamation.getI_id();
			
			row++;
		}
		
		parameter.put(P_KPI_VARIABLEN, krit.getKpiReportStorage());
		initJRDS(parameter, ReklamationReportFac.REPORT_MODUL, ReklamationReportFac.REPORT_FEHLERART,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	public JasperPrintLP printMitarbeiterreklamation(java.sql.Timestamp tVon, java.sql.Timestamp tBis, boolean bKunde,
			boolean bLieferant, boolean bFertigung, Integer kundeIId, boolean bNurBerechtigte,
			TheClientDto theClientDto) {

		sAktuellerReport = ReklamationReportFac.REPORT_MITARBEITERREKLAMATION;

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_VON", tVon);
		parameter.put("P_BIS", tBis);
		parameter.put("P_NURBERECHTIGTE", new Boolean(bNurBerechtigte));

		tVon = Helper.cutTimestamp(tVon);

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session.createCriteria(FLRReklamation.class);
		crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));

		session.enableFilter("filterMandant").setParameter("paramMandant", theClientDto.getMandant());
		session.enableFilter("filterLocale").setParameter("paramLocale", theClientDto.getLocUiAsString());

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tBis.getTime());
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);

		tBis = Helper.cutTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));

		crit.add(Restrictions.ge(ReklamationFac.FLR_REKLAMATION_T_BELEGDATUM, tVon));
		crit.add(Restrictions.lt(ReklamationFac.FLR_REKLAMATION_T_BELEGDATUM, tBis));

		crit.createAlias(ReklamationFac.FLR_REKLAMATION_FLRLIEFERANT, "l", CriteriaSpecification.LEFT_JOIN);
		crit.createAlias(ReklamationFac.FLR_REKLAMATION_FRLKUNDE, "k", CriteriaSpecification.LEFT_JOIN);

		crit.createAlias(ReklamationFac.FLR_REKLAMATION_FLRLOS, "lo", CriteriaSpecification.LEFT_JOIN);
		crit.createAlias("lo." + FertigungFac.FLR_LOSREPORT_FLRAUFTRAG, "au", CriteriaSpecification.LEFT_JOIN);

		crit.createAlias("l." + LieferantFac.FLR_PARTNER, "pl", CriteriaSpecification.LEFT_JOIN);
		crit.createAlias("k." + KundeFac.FLR_PARTNER, "kl", CriteriaSpecification.LEFT_JOIN);
		crit.createAlias(ReklamationFac.FLR_REKLAMATION_FLRFEHLERANGABE, "f", CriteriaSpecification.LEFT_JOIN);

		if (bNurBerechtigte == true) {
			crit.add(Restrictions.eq(ReklamationFac.FLR_REKLAMATION_B_BERECHTIGT, Helper.boolean2Short(true)));
		}

		ArrayList<String> alArten = new ArrayList<String>();
		if (bFertigung == true) {
			alArten.add(ReklamationFac.REKLAMATIONART_FERTIGUNG);
		}
		if (bKunde == true) {
			alArten.add(ReklamationFac.REKLAMATIONART_KUNDE);
		}
		if (bLieferant == true) {
			alArten.add(ReklamationFac.REKLAMATIONART_LIEFERANT);
		}

		crit.add(Restrictions.in(ReklamationFac.FLR_REKLAMATION_REKLAMATIONART_C_NR, alArten));

		if (kundeIId != null) {
			crit.add(Restrictions.or(Restrictions.eq("au.kunde_i_id_auftragsadresse", kundeIId),
					Restrictions.eq(ReklamationFac.FLR_REKLAMATION_KUNDE_I_ID, kundeIId)));

			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto);
			parameter.put("P_KUNDE", kundeDto.getPartnerDto().formatFixTitelName1Name2());

		}

		crit.createAlias(ReklamationFac.FLR_REKLAMATION_FLRVERURSACHER, "ver", CriteriaSpecification.LEFT_JOIN);
		crit.createAlias("ver." + PersonalFac.FLR_PERSONAL_FLRPARTNER, "part", CriteriaSpecification.LEFT_JOIN);
		crit.addOrder(Order.asc("part." + PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1));
		crit.addOrder(Order.asc("part." + PartnerFac.FLR_PARTNER_C_NAME2VORNAMEFIRMAZEILE2));

		crit.addOrder(Order.asc("f.c_bez"));
		crit.addOrder(Order.asc("c_nr"));

		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();

		data = new Object[results.size()][REPORT_MITARBEITERREKLAMATION_ANZAHL_SPALTEN];
		int row = 0;
		while (resultListIterator.hasNext()) {
			FLRReklamation flrReklamation = (FLRReklamation) resultListIterator.next();

			data[row][REPORT_MITARBEITERREKLAMATION_REKLAMATIONSNUMMER] = flrReklamation.getC_nr();
			data[row][REPORT_MITARBEITERREKLAMATION_REKLAMATIONSART] = flrReklamation.getReklamationart_c_nr();

			data[row][REPORT_MITARBEITERREKLAMATION_VERURSACHER] = "";
			if (flrReklamation.getFlrverursacher() != null) {
				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKey(flrReklamation.getFlrverursacher().getI_id(), theClientDto);

				data[row][REPORT_MITARBEITERREKLAMATION_VERURSACHER] = personalDto.formatAnrede();
			}
			data[row][REPORT_MITARBEITERREKLAMATION_KUNDE] = "";
			KundeDto kundeDto = null;
			if (flrReklamation.getReklamationart_c_nr().equals(ReklamationFac.REKLAMATIONART_KUNDE)
					&& flrReklamation.getKunde_i_id() != null) {

				kundeDto = getKundeFac().kundeFindByPrimaryKey(flrReklamation.getKunde_i_id(), theClientDto);

			} else if (flrReklamation.getReklamationart_c_nr().equals(ReklamationFac.REKLAMATIONART_FERTIGUNG)
					&& flrReklamation.getFlrlos() != null && flrReklamation.getFlrlos().getFlrauftrag() != null) {
				kundeDto = getKundeFac().kundeFindByPrimaryKey(
						flrReklamation.getFlrlos().getFlrauftrag().getKunde_i_id_auftragsadresse(), theClientDto);
			}

			if (kundeDto != null) {
				data[row][REPORT_MITARBEITERREKLAMATION_KUNDE] = kundeDto.getPartnerDto().formatTitelAnrede();
			}

			row++;
		}

		initJRDS(parameter, ReklamationReportFac.REPORT_MODUL, ReklamationReportFac.REPORT_MITARBEITERREKLAMATION,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	public JasperPrintLP printMaschinenreklamation(java.sql.Timestamp tVon, java.sql.Timestamp tBis, boolean bKunde,
			boolean bLieferant, boolean bFertigung, Integer kundeIId, boolean bNurBerechtigte,
			TheClientDto theClientDto) {

		sAktuellerReport = ReklamationReportFac.REPORT_MASCHINENREKLAMATION;

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_VON", tVon);
		parameter.put("P_BIS", tBis);
		parameter.put("P_NURBERECHTIGTE", new Boolean(bNurBerechtigte));

		tVon = Helper.cutTimestamp(tVon);

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session.createCriteria(FLRReklamation.class);
		crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));

		session.enableFilter("filterMandant").setParameter("paramMandant", theClientDto.getMandant());
		session.enableFilter("filterLocale").setParameter("paramLocale", theClientDto.getLocUiAsString());

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tBis.getTime());
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);

		tBis = Helper.cutTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));

		crit.add(Restrictions.ge(ReklamationFac.FLR_REKLAMATION_T_BELEGDATUM, tVon));
		crit.add(Restrictions.lt(ReklamationFac.FLR_REKLAMATION_T_BELEGDATUM, tBis));

		crit.createAlias(ReklamationFac.FLR_REKLAMATION_FLRLIEFERANT, "l", CriteriaSpecification.LEFT_JOIN);
		crit.createAlias(ReklamationFac.FLR_REKLAMATION_FRLKUNDE, "k", CriteriaSpecification.LEFT_JOIN);

		crit.createAlias(ReklamationFac.FLR_REKLAMATION_FLRLOS, "lo", CriteriaSpecification.LEFT_JOIN);
		crit.createAlias("lo." + FertigungFac.FLR_LOSREPORT_FLRAUFTRAG, "au", CriteriaSpecification.LEFT_JOIN);

		crit.createAlias("l." + LieferantFac.FLR_PARTNER, "pl", CriteriaSpecification.LEFT_JOIN);
		crit.createAlias("k." + KundeFac.FLR_PARTNER, "kl", CriteriaSpecification.LEFT_JOIN);
		crit.createAlias(ReklamationFac.FLR_REKLAMATION_FLRFEHLERANGABE, "f", CriteriaSpecification.LEFT_JOIN);

		if (bNurBerechtigte == true) {
			crit.add(Restrictions.eq(ReklamationFac.FLR_REKLAMATION_B_BERECHTIGT, Helper.boolean2Short(true)));
		}

		ArrayList<String> alArten = new ArrayList<String>();
		if (bFertigung == true) {
			alArten.add(ReklamationFac.REKLAMATIONART_FERTIGUNG);
		}
		if (bKunde == true) {
			alArten.add(ReklamationFac.REKLAMATIONART_KUNDE);
		}
		if (bLieferant == true) {
			alArten.add(ReklamationFac.REKLAMATIONART_LIEFERANT);
		}

		crit.add(Restrictions.in(ReklamationFac.FLR_REKLAMATION_REKLAMATIONART_C_NR, alArten));

		if (kundeIId != null) {
			crit.add(Restrictions.or(Restrictions.eq("au.kunde_i_id_auftragsadresse", kundeIId),
					Restrictions.eq(ReklamationFac.FLR_REKLAMATION_KUNDE_I_ID, kundeIId)));

			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto);
			parameter.put("P_KUNDE", kundeDto.getPartnerDto().formatFixTitelName1Name2());

		}

		crit.createAlias(ReklamationFac.FLR_REKLAMATION_FLRMASCHINE, "masch", CriteriaSpecification.LEFT_JOIN);
		crit.createAlias("masch." + ZeiterfassungFac.FLR_MASCHINE_FLR_MASCHINENGRUPPE, "mgru",
				CriteriaSpecification.LEFT_JOIN);

		crit.addOrder(Order.asc("mgru.c_bez"));
		crit.addOrder(Order.asc("f.c_bez"));
		crit.addOrder(Order.asc("c_nr"));

		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();

		data = new Object[results.size()][REPORT_MASCHINENREKLAMATION_ANZAHL_SPALTEN];
		int row = 0;
		while (resultListIterator.hasNext()) {
			FLRReklamation flrReklamation = (FLRReklamation) resultListIterator.next();

			data[row][REPORT_MASCHINENREKLAMATION_REKLAMATIONSNUMMER] = flrReklamation.getC_nr();
			data[row][REPORT_MASCHINENREKLAMATION_REKLAMATIONSART] = flrReklamation.getReklamationart_c_nr();

			if (flrReklamation.getFlrmaschine() != null) {
				data[row][REPORT_MASCHINENREKLAMATION_MASCHINENGRUPPE] = flrReklamation.getFlrmaschine()
						.getFlrmaschinengruppe().getC_bez();
			}

			data[row][REPORT_MASCHINENREKLAMATION_VERURSACHER] = "";
			if (flrReklamation.getFlrverursacher() != null) {
				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKey(flrReklamation.getFlrverursacher().getI_id(), theClientDto);

				data[row][REPORT_MASCHINENREKLAMATION_VERURSACHER] = personalDto.formatAnrede();
			}

			data[row][REPORT_MASCHINENREKLAMATION_KUNDE] = "";
			KundeDto kundeDto = null;
			if (flrReklamation.getReklamationart_c_nr().equals(ReklamationFac.REKLAMATIONART_KUNDE)
					&& flrReklamation.getKunde_i_id() != null) {

				kundeDto = getKundeFac().kundeFindByPrimaryKey(flrReklamation.getKunde_i_id(), theClientDto);

			} else if (flrReklamation.getReklamationart_c_nr().equals(ReklamationFac.REKLAMATIONART_FERTIGUNG)
					&& flrReklamation.getFlrlos() != null && flrReklamation.getFlrlos().getFlrauftrag() != null) {
				kundeDto = getKundeFac().kundeFindByPrimaryKey(
						flrReklamation.getFlrlos().getFlrauftrag().getKunde_i_id_auftragsadresse(), theClientDto);
			}

			if (kundeDto != null) {
				data[row][REPORT_MASCHINENREKLAMATION_KUNDE] = kundeDto.getPartnerDto().formatTitelAnrede();
			}

			row++;
		}

		initJRDS(parameter, ReklamationReportFac.REPORT_MODUL, ReklamationReportFac.REPORT_MASCHINENREKLAMATION,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	public JasperPrintLP printReklamationsjournal(Integer kostenstelleIId, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, boolean bKunde, boolean bLieferant, boolean bFertigung, boolean bNurOffene,
			int iSortierung, TheClientDto theClientDto) {

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_VON", tVon);
		parameter.put("P_BIS", tBis);
		parameter.put("P_NUROFFENE", new Boolean(bNurOffene));
		index = -1;
		sAktuellerReport = ReklamationReportFac.REPORT_REKLAMATIONSJOURNAL;

		tVon = Helper.cutTimestamp(tVon);

		Session session = FLRSessionFactory.getFactory().openSession();

		session.enableFilter("filterMandant").setParameter("paramMandant", theClientDto.getMandant());
		session.enableFilter("filterLocale").setParameter("paramLocale", theClientDto.getLocUiAsString());

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tBis.getTime());
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);

		tBis = Helper.cutTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));

		String sQuery = "SELECT r FROM FLRReklamation r LEFT OUTER JOIN r.flrfehler AS f   WHERE r.mandant_c_nr='"
				+ theClientDto.getMandant() + "' AND r.t_belegdatum>='" + Helper.formatTimestampWithSlashes(tVon)
				+ "' AND r.t_belegdatum<'" + Helper.formatTimestampWithSlashes(tBis) + "'";

		if (bNurOffene == true) {
			sQuery += " AND r.t_erledigt IS NULL";
		}

		if (bFertigung == true || bKunde == true || bLieferant == true) {

			sQuery += " AND (";

			if (bFertigung == true) {

				sQuery += " ( r.reklamationart_c_nr='" + ReklamationFac.REKLAMATIONART_FERTIGUNG
						+ "' OR (r.reklamationart_c_nr='" + ReklamationFac.REKLAMATIONART_KUNDE
						+ "' AND r.i_kundeunterart=" + ReklamationFac.REKLAMATION_KUNDEUNTERART_FERTIGUNG + "  )) ";

				if (bKunde == true || bLieferant == true) {
					sQuery += " OR ";
				}

			}

			if (bKunde == true) {

				sQuery += " r.reklamationart_c_nr='" + ReklamationFac.REKLAMATIONART_KUNDE + "' ";

				if (bLieferant == true) {
					sQuery += " OR ";
				}

			}

			if (bLieferant == true) {
				sQuery += " ( r.reklamationart_c_nr='" + ReklamationFac.REKLAMATIONART_LIEFERANT
						+ "' OR (r.reklamationart_c_nr='" + ReklamationFac.REKLAMATIONART_KUNDE
						+ "' AND r.i_kundeunterart=" + ReklamationFac.REKLAMATION_KUNDEUNTERART_LIEFERANT + "  )) ";

			}

			sQuery += ")";

		} else {
			sQuery += " AND r.reklamationart_c_nr='XXX' ";
		}

		if (kostenstelleIId != null) {

			sQuery += " AND kostenstelle_i_id=" + kostenstelleIId;

			KostenstelleDto kostenstelleDto = getSystemFac().kostenstelleFindByPrimaryKey(kostenstelleIId);
			parameter.put("P_KOSTENSTELLE", kostenstelleDto.formatKostenstellenbezeichnung());

		}

		if (iSortierung == ReklamationReportFac.SORTIERGUN_REKLAMATIONSJOURNAL_BELEGNR) {
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("bes.belegartnummer", theClientDto.getMandant(), theClientDto.getLocUi()));
			sQuery += " ORDER BY  r.c_nr";
		} else if (iSortierung == ReklamationReportFac.SORTIERGUN_REKLAMATIONSJOURNAL_FEHLER) {
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.fehler", theClientDto.getMandant(), theClientDto.getLocUi()));
			sQuery += " ORDER BY  f.c_bez";
		}

		Query query = session.createQuery(sQuery);

		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		data = new Object[results.size()][REPORT_REKLAMATIONSJOURNAL_ANZAHL_SPALTEN];
		int row = 0;
		while (resultListIterator.hasNext()) {
			FLRReklamation flrReklamation = (FLRReklamation) resultListIterator.next();

			ReklamationDto reklamationDto = null;

			reklamationDto = getReklamationFac().reklamationFindByPrimaryKey(flrReklamation.getI_id());

			data[row][REPORT_REKLAMATIONSJOURNAL_LFLSNR] = reklamationDto.getCLflsnr();
			data[row][REPORT_REKLAMATIONSJOURNAL_LFREKLANR] = reklamationDto.getCLfreklanr();

			data[row][REPORT_REKLAMATIONSJOURNAL_KNDLSNR] = reklamationDto.getCKdlsnr();
			data[row][REPORT_REKLAMATIONSJOURNAL_KNDREKLANR] = reklamationDto.getCKdreklanr();
			data[row][REPORT_REKLAMATIONSJOURNAL_SNRCHNR] = reklamationDto.getCSeriennrchargennr();

			if (reklamationDto.getMaschineIId() != null) {
				MaschineDto maschineDto = getZeiterfassungFac()
						.maschineFindByPrimaryKey(reklamationDto.getMaschineIId());

				data[row][REPORT_REKLAMATIONSJOURNAL_MASCHINE_BEZEICHNUNG] = maschineDto.getCBez();
				data[row][REPORT_REKLAMATIONSJOURNAL_MASCHINE_IDENTIFIKATIONSNUMMER] = maschineDto
						.getCIdentifikationsnr();
				data[row][REPORT_REKLAMATIONSJOURNAL_MASCHINE_SERIENNUMMER] = maschineDto
						.getCSeriennummer();
				data[row][REPORT_REKLAMATIONSJOURNAL_MASCHINE_INVENTARNUMMER] = maschineDto.getCInventarnummer();

			}

			data[row][REPORT_REKLAMATIONSJOURNAL_BELEGNR] = flrReklamation.getReklamationart_c_nr().substring(0, 1)
					+ flrReklamation.getC_nr();
			data[row][REPORT_REKLAMATIONSJOURNAL_DATUM] = new java.sql.Timestamp(
					flrReklamation.getT_belegdatum().getTime());
			data[row][REPORT_REKLAMATIONSJOURNAL_GRUND] = flrReklamation.getC_grund();
			data[row][REPORT_REKLAMATIONSJOURNAL_GRUND_KOMMENTAR] = reklamationDto.getXGrundLang();
			data[row][REPORT_REKLAMATIONSJOURNAL_REKLAMATIONSART] = flrReklamation.getReklamationart_c_nr();

			if (flrReklamation.getI_kundeunterart() != null) {

				if (flrReklamation.getI_kundeunterart().equals(ReklamationFac.REKLAMATION_KUNDEUNTERART_FERTIGUNG)) {
					data[row][REPORT_REKLAMATIONSJOURNAL_KUNDEUNTERART] = ReklamationFac.REKLAMATIONART_FERTIGUNG;
				} else if (flrReklamation.getI_kundeunterart()
						.equals(ReklamationFac.REKLAMATION_KUNDEUNTERART_LIEFERANT)) {
					data[row][REPORT_REKLAMATIONSJOURNAL_KUNDEUNTERART] = ReklamationFac.REKLAMATIONART_LIEFERANT;
				}
			}

			data[row][REPORT_REKLAMATIONSJOURNAL_MATERIALKOSTEN] = flrReklamation.getN_kostenmaterial();
			data[row][REPORT_REKLAMATIONSJOURNAL_AZKOSTEN] = flrReklamation.getN_kostenarbeitszeit();
			data[row][REPORT_REKLAMATIONSJOURNAL_PROJEKT] = flrReklamation.getC_projekt();
			data[row][REPORT_REKLAMATIONSJOURNAL_MENGE] = flrReklamation.getN_menge();

			if (flrReklamation.getFlrfehler() != null) {
				data[row][REPORT_REKLAMATIONSJOURNAL_FEHLER] = flrReklamation.getFlrfehler().getC_bez();
			}

			try {
				if (flrReklamation.getFlrartikel() != null) {
					data[row][REPORT_REKLAMATIONSJOURNAL_ARTIKELNR] = flrReklamation.getFlrartikel().getC_nr();
					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(flrReklamation.getFlrartikel().getI_id(), theClientDto);
					data[row][REPORT_REKLAMATIONSJOURNAL_ARTIKELBEZEICHNUNG] = artikelDto.getCBezAusSpr();
					data[row][REPORT_REKLAMATIONSJOURNAL_ARTIKELKURZBEZEICHNUNG] = artikelDto.getKbezAusSpr();
					data[row][REPORT_REKLAMATIONSJOURNAL_ARTIKELZUSATZBEZEICHNUNG] = artikelDto.getCZBezAusSpr();
					data[row][REPORT_REKLAMATIONSJOURNAL_ARTIKELZUSATZBEZEICHNUNG2] = artikelDto.getCZBez2AusSpr();
				} else {
					data[row][REPORT_REKLAMATIONSJOURNAL_ARTIKELBEZEICHNUNG] = reklamationDto.getCHandartikel();
				}

				if (flrReklamation.getReklamationart_c_nr().equals(ReklamationFac.REKLAMATIONART_FERTIGUNG)
						&& flrReklamation.getLos_i_id() != null) {
					LosDto losDto = getFertigungFac().losFindByPrimaryKey(flrReklamation.getLos_i_id());
					data[row][REPORT_REKLAMATIONSJOURNAL_REKLAMATIONSBELEG] = "LO" + losDto.getCNr();

					if (losDto.getAuftragIId() != null) {
						BelegInfos beleg = getLagerFac().getBelegInfos(LocaleFac.BELEGART_AUFTRAG,
								losDto.getAuftragIId(), null, theClientDto);
						data[row][REPORT_REKLAMATIONSJOURNAL_KUNDELIEFERANT] = beleg.getKundeLieferant();
					}
				} else if (flrReklamation.getReklamationart_c_nr().equals(ReklamationFac.REKLAMATIONART_KUNDE)) {
					if (flrReklamation.getKunde_i_id() != null) {
						KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(flrReklamation.getKunde_i_id(),
								theClientDto);
						data[row][REPORT_REKLAMATIONSJOURNAL_KUNDELIEFERANT] = kundeDto.getPartnerDto()
								.formatFixTitelName1Name2();
					}

					if (flrReklamation.getLieferant_i_id() != null) {
						LieferantDto lieferantDto = getLieferantFac()
								.lieferantFindByPrimaryKey(flrReklamation.getLieferant_i_id(), theClientDto);
						data[row][REPORT_REKLAMATIONSJOURNAL_LIEFERANT_KUNDEUNTERART_LIEFERANT] = lieferantDto
								.getPartnerDto().formatFixTitelName1Name2();
					}

					if (flrReklamation.getLieferschein_i_id() != null) {
						BelegInfos beleg = getLagerFac().getBelegInfos(LocaleFac.BELEGART_LIEFERSCHEIN,
								flrReklamation.getLieferschein_i_id(), null, theClientDto);
						data[row][REPORT_REKLAMATIONSJOURNAL_REKLAMATIONSBELEG] = "LS" + beleg.getBelegnummer();
					} else if (flrReklamation.getRechnung_i_id() != null) {
						BelegInfos beleg = getLagerFac().getBelegInfos(LocaleFac.BELEGART_RECHNUNG,
								flrReklamation.getRechnung_i_id(), null, theClientDto);
						data[row][REPORT_REKLAMATIONSJOURNAL_REKLAMATIONSBELEG] = "RE" + beleg.getBelegnummer();
					}

					if (flrReklamation.getBestellung_i_id() != null) {
						BelegInfos beleg = getLagerFac().getBelegInfos(LocaleFac.BELEGART_BESTELLUNG,
								flrReklamation.getBestellung_i_id(), null, theClientDto);
						data[row][REPORT_REKLAMATIONSJOURNAL_BESTELLUNG_KUNDEUNTERART_LIEFERANT] = beleg
								.getBelegnummer();
					} else {
						data[row][REPORT_REKLAMATIONSJOURNAL_BESTELLUNG_KUNDEUNTERART_LIEFERANT] = reklamationDto
								.getCBestellnummer();
					}

					if (reklamationDto.getWareneingangIId() != null) {

						data[row][REPORT_REKLAMATIONSJOURNAL_WARENEINGANG_KUNDEUNTERART_LIEFERANT] = getWareneingangFac()
								.wareneingangFindByPrimaryKey(reklamationDto.getWareneingangIId()).getCLieferscheinnr();
					} else {
						data[row][REPORT_REKLAMATIONSJOURNAL_WARENEINGANG_KUNDEUNTERART_LIEFERANT] = reklamationDto
								.getCWareneingang();
					}
					if (flrReklamation.getFlrlos() != null) {
						data[row][REPORT_REKLAMATIONSJOURNAL_LOS_KUNDEUNTERART_FERTIGUNG] = flrReklamation.getFlrlos()
								.getC_nr();
					}
					if (reklamationDto.getLossollarbeitsplanIId() != null) {

						LossollarbeitsplanDto sollarbeitsplanDto = getFertigungFac()
								.lossollarbeitsplanFindByPrimaryKey(reklamationDto.getLossollarbeitsplanIId());

						String sollarb = "AG:" + sollarbeitsplanDto.getIArbeitsgangnummer();
						if (sollarbeitsplanDto.getIUnterarbeitsgang() != null) {
							sollarb += " UAG:" + sollarbeitsplanDto.getIUnterarbeitsgang();
						}

						data[row][REPORT_REKLAMATIONSJOURNAL_ARBEITSGANG_KUNDEUNTERART_FERTIGUNG] = sollarb;

					}

					if (flrReklamation.getFlrmaschine() != null) {
						data[row][REPORT_REKLAMATIONSJOURNAL_MASCHINE_KUNDEUNTERART_FERTIGUNG] = getZeiterfassungFac()
								.maschineFindByPrimaryKey(flrReklamation.getFlrmaschine().getI_id()).getBezeichnung();
					}

					if (reklamationDto.getPersonalIIdVerursacher() != null) {
						PersonalDto personalDto = getPersonalFac()
								.personalFindByPrimaryKey(reklamationDto.getPersonalIIdVerursacher(), theClientDto);
						data[row][REPORT_REKLAMATIONSJOURNAL_VERURSACHER_KUNDEUNTERART_FERTIGUNG] = personalDto
								.getPartnerDto().formatTitelAnrede();
					}

				} else if (flrReklamation.getReklamationart_c_nr().equals(ReklamationFac.REKLAMATIONART_LIEFERANT)) {
					if (flrReklamation.getLieferant_i_id() != null) {
						LieferantDto lieferantDto = getLieferantFac()
								.lieferantFindByPrimaryKey(flrReklamation.getLieferant_i_id(), theClientDto);
						data[row][REPORT_REKLAMATIONSJOURNAL_KUNDELIEFERANT] = lieferantDto.getPartnerDto()
								.formatFixTitelName1Name2();
					}
					if (flrReklamation.getBestellung_i_id() != null) {
						BelegInfos beleg = getLagerFac().getBelegInfos(LocaleFac.BELEGART_BESTELLUNG,
								flrReklamation.getBestellung_i_id(), null, theClientDto);
						data[row][REPORT_REKLAMATIONSJOURNAL_REKLAMATIONSBELEG] = "BS" + beleg.getBelegnummer();
					} else if (reklamationDto.getCBestellnummer() != null) {
						data[row][REPORT_REKLAMATIONSJOURNAL_REKLAMATIONSBELEG] = reklamationDto.getCBestellnummer();
					}
				}

				data[row][REPORT_REKLAMATIONSJOURNAL_KOSTENSTELLE] = getSystemFac()
						.kostenstelleFindByPrimaryKey(reklamationDto.getKostenstelleIId())
						.formatKostenstellenbezeichnung();

				if (reklamationDto.getSchwereIId() != null) {
					data[row][REPORT_REKLAMATIONSJOURNAL_SCHWERE] = getReklamationFac()
							.schwereFindByPrimaryKey(reklamationDto.getSchwereIId(), theClientDto).formatBezeichnung();
				}

				data[row][REPORT_REKLAMATIONSJOURNAL_RUECKSPRECHEZEIT] = reklamationDto.getTRuecksprache();
				data[row][REPORT_REKLAMATIONSJOURNAL_RUECKSPRECHEMIT] = reklamationDto.getCRuecksprachemit();

				data[row][REPORT_REKLAMATIONSJOURNAL_B_BERECHTIGT] = Helper
						.short2Boolean(reklamationDto.getBBerechtigt());

				data[row][REPORT_REKLAMATIONSJOURNAL_MASSNAHME_BIS_KURZ] = reklamationDto.getTMassnahmebiskurz();
				data[row][REPORT_REKLAMATIONSJOURNAL_MASSNAHME_BIS_MITTEL] = reklamationDto.getTMassnahmebismittel();
				data[row][REPORT_REKLAMATIONSJOURNAL_MASSNAHME_BIS_LANG] = reklamationDto.getTMassnahmebislang();
				data[row][REPORT_REKLAMATIONSJOURNAL_WIRKSAMKEIT_BIS] = reklamationDto.getTWirksamkeitbis();

				data[row][REPORT_REKLAMATIONSJOURNAL_MASSNAHME_EINGEFUEHRT_KURZ] = reklamationDto.getTEingefuehrtkurz();
				data[row][REPORT_REKLAMATIONSJOURNAL_MASSNAHME_EINGEFUEHRT_MITTEL] = reklamationDto
						.getXMassnahmeMittel();
				data[row][REPORT_REKLAMATIONSJOURNAL_MASSNAHME_EINGEFUEHRT_LANG] = reklamationDto.getXMassnahmeLang();
				data[row][REPORT_REKLAMATIONSJOURNAL_WIRKSAMKEIT_DURCHGEFUEHRT] = reklamationDto
						.getTWirksamkeiteingefuehrt();
				data[row][REPORT_REKLAMATIONSJOURNAL_ANALYSE] = reklamationDto.getXAnalyse();
				data[row][REPORT_REKLAMATIONSJOURNAL_KOMMENTAR] = reklamationDto.getXKommentar();

				data[row][REPORT_REKLAMATIONSJOURNAL_MASSNAHME_KOMMENTAR_KURZ] = reklamationDto.getTEingefuehrtkurz();
				data[row][REPORT_REKLAMATIONSJOURNAL_MASSNAHME_KOMMENTAR_MITTEL] = reklamationDto
						.getTEingefuehrtmittel();
				data[row][REPORT_REKLAMATIONSJOURNAL_MASSNAHME_KOMMENTAR_LANG] = reklamationDto.getTEingefuehrtlang();

				data[row][REPORT_REKLAMATIONSJOURNAL_B_BETRIFFTLAGERSTAND] = Helper
						.short2Boolean(reklamationDto.getBBetrifftlagerstand());
				data[row][REPORT_REKLAMATIONSJOURNAL_B_BETRIFFTGELIEFERTE] = Helper
						.short2Boolean(reklamationDto.getBBetrifftgelieferte());

				data[row][REPORT_REKLAMATIONSJOURNAL_STUECKLAGERSTAND] = reklamationDto.getNStuecklagerstand();
				data[row][REPORT_REKLAMATIONSJOURNAL_STUECKGELIEFERTE] = reklamationDto.getNStueckgelieferte();

				if (reklamationDto.getAufnahmeartIId() != null) {
					data[row][REPORT_REKLAMATIONSJOURNAL_AUFNAHMEART] = getReklamationFac()
							.aufnahmeartFindByPrimaryKey(reklamationDto.getAufnahmeartIId(), theClientDto)
							.getBezeichnung();
				}
				if (reklamationDto.getWirksamkeitIId() != null) {
					data[row][REPORT_REKLAMATIONSJOURNAL_WIRKSAMKEIT] = getReklamationFac()
							.wirksamkeitFindByPrimaryKey(reklamationDto.getWirksamkeitIId(), theClientDto)
							.getBezeichnung();
				}

				data[row][REPORT_REKLAMATIONSJOURNAL_WIRKSAMKEIT_KOMMENTAR] = reklamationDto.getXWirksamkeit();

				if (reklamationDto.getFehlerangabeIId() != null) {
					data[row][REPORT_REKLAMATIONSJOURNAL_FEHLERANGABE] = getReklamationFac()
							.fehlerangabeFindByPrimaryKey(reklamationDto.getFehlerangabeIId(), theClientDto)
							.getBezeichnung();
				}

				if (reklamationDto.getPersonalIIdAufnehmer() != null) {
					PersonalDto personalDto = getPersonalFac()
							.personalFindByPrimaryKey(reklamationDto.getPersonalIIdAufnehmer(), theClientDto);
					data[row][REPORT_REKLAMATIONSJOURNAL_PERSON_AUFNEHMER] = personalDto.getPartnerDto()
							.formatTitelAnrede();
				}
				if (reklamationDto.getPersonalIIdEingefuehrtkurz() != null) {
					PersonalDto personalDto = getPersonalFac()
							.personalFindByPrimaryKey(reklamationDto.getPersonalIIdEingefuehrtkurz(), theClientDto);
					data[row][REPORT_REKLAMATIONSJOURNAL_PERSON_MASSNAHME_KURZ] = personalDto.getPartnerDto()
							.formatTitelAnrede();
				}
				if (reklamationDto.getPersonalIIdEingefuehrtmittel() != null) {
					PersonalDto personalDto = getPersonalFac()
							.personalFindByPrimaryKey(reklamationDto.getPersonalIIdEingefuehrtmittel(), theClientDto);
					data[row][REPORT_REKLAMATIONSJOURNAL_PERSON_MASSNAHME_MITTEL] = personalDto.getPartnerDto()
							.formatTitelAnrede();
				}
				if (reklamationDto.getPersonalIIdEingefuehrtlang() != null) {
					PersonalDto personalDto = getPersonalFac()
							.personalFindByPrimaryKey(reklamationDto.getPersonalIIdEingefuehrtlang(), theClientDto);
					data[row][REPORT_REKLAMATIONSJOURNAL_PERSON_MASSNAHME_LANG] = personalDto.getPartnerDto()
							.formatTitelAnrede();
				}
				if (reklamationDto.getPersonalIIdRuecksprache() != null) {
					PersonalDto personalDto = getPersonalFac()
							.personalFindByPrimaryKey(reklamationDto.getPersonalIIdRuecksprache(), theClientDto);
					data[row][REPORT_REKLAMATIONSJOURNAL_PERSON_RUECKSPRACHE] = personalDto.getPartnerDto()
							.formatTitelAnrede();
				}
				if (reklamationDto.getPersonalIIdWirksamkeit() != null) {
					PersonalDto personalDto = getPersonalFac()
							.personalFindByPrimaryKey(reklamationDto.getPersonalIIdWirksamkeit(), theClientDto);
					data[row][REPORT_REKLAMATIONSJOURNAL_PERSON_WIRKSAMKEIT] = personalDto.getPartnerDto()
							.formatTitelAnrede();
				}

				if (reklamationDto.getMassnahmeIIdKurz() != null) {
					data[row][REPORT_REKLAMATIONSJOURNAL_MASSNAHME_KURZ] = getReklamationFac()
							.massnahmeFindByPrimaryKey(reklamationDto.getMassnahmeIIdKurz(), theClientDto)
							.getBezeichnung();
				}
				if (reklamationDto.getMassnahmeIIdMittel() != null) {
					data[row][REPORT_REKLAMATIONSJOURNAL_MASSNAHME_MITTEL] = getReklamationFac()
							.massnahmeFindByPrimaryKey(reklamationDto.getMassnahmeIIdMittel(), theClientDto)
							.getBezeichnung();
				}
				if (reklamationDto.getMassnahmeIIdLang() != null) {
					data[row][REPORT_REKLAMATIONSJOURNAL_MASSNAHME_LANG] = getReklamationFac()
							.massnahmeFindByPrimaryKey(reklamationDto.getMassnahmeIIdLang(), theClientDto)
							.getBezeichnung();
				}

				if (reklamationDto.getPersonalIIdErledigt() != null) {
					PersonalDto personalDto = getPersonalFac()
							.personalFindByPrimaryKey(reklamationDto.getPersonalIIdErledigt(), theClientDto);
					data[row][REPORT_REKLAMATIONSJOURNAL_PERSON_ERLEDIGT] = personalDto.getPartnerDto()
							.formatTitelAnrede();
				}
				data[row][REPORT_REKLAMATIONSJOURNAL_ERLEDIGTDATUM] = reklamationDto.getTErledigt();

				data[row][REPORT_REKLAMATIONSJOURNAL_ANLAGEDATUM] = reklamationDto.getTAnlegen();

				data[row][REPORT_REKLAMATIONSJOURNAL_WARE_ERHALTEN] = reklamationDto.getTWareErhalten();

				AnsprechpartnerDto ansprechpartnerDto = null;
				if (reklamationDto.getAnsprechpartnerIId() != null) {
					ansprechpartnerDto = getAnsprechpartnerFac()
							.ansprechpartnerFindByPrimaryKey(reklamationDto.getAnsprechpartnerIId(), theClientDto);
					data[row][REPORT_REKLAMATIONSJOURNAL_ANSPRECHPARTNER] = ansprechpartnerDto.getPartnerDto()
							.formatTitelAnrede();

					if (flrReklamation.getKunde_i_id() != null) {
						KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(flrReklamation.getKunde_i_id(),
								theClientDto);

						String sTelefon = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(
								ansprechpartnerDto.getIId(), kundeDto.getPartnerDto(),
								PartnerFac.KOMMUNIKATIONSART_TELEFON, theClientDto.getMandant(), theClientDto);

						data[row][REPORT_REKLAMATIONSJOURNAL_TELEFONANSPRECHPARTNER] = sTelefon;
					}
				}

				if (reklamationDto.getBehandlungIId() != null) {
					data[row][REPORT_REKLAMATIONSJOURNAL_BEHANDLUNG] = getReklamationFac()
							.behandlungFindByPrimaryKey(reklamationDto.getBehandlungIId(), theClientDto)
							.formatBezeichnung();
				}

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

			row++;
		}

		if (iSortierung == ReklamationReportFac.SORTIERGUN_REKLAMATIONSJOURNAL_KUNDELIEFERANT) {
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.kunde", theClientDto.getMandant(), theClientDto.getLocUi()) + "/ "
							+ getTextRespectUISpr("lp.lieferant", theClientDto.getMandant(), theClientDto.getLocUi()));

			for (int i = 0; i < data.length - 1; i++) {
				for (int j = 0; j < data.length - 1 - i; j++) {

					String a = (String) data[j][REPORT_REKLAMATIONSJOURNAL_KUNDELIEFERANT];
					String b = (String) data[j + 1][REPORT_REKLAMATIONSJOURNAL_KUNDELIEFERANT];
					if (b == null || (a == null && b == null)) {
						continue;
					}

					if (a == null || a.compareTo(b) > 0) {
						Object[] oHelp = data[j];

						data[j] = data[j + 1];
						data[j + 1] = oHelp;
					}

				}
			}

		}

		initJRDS(parameter, ReklamationReportFac.REPORT_MODUL, ReklamationReportFac.REPORT_REKLAMATIONSJOURNAL,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	public Object getFieldValue(JRField jRField) throws JRException {
		Object value = null;
		String fieldName = jRField.getName();
		if (sAktuellerReport.equals(ReklamationReportFac.REPORT_REKLAMATION)
				|| sAktuellerReport.equals(ReklamationReportFac.REPORT_REKLAMATION_LIEFERANT)) {
			if ("F_BILD".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATION_BILD];
			} else if ("F_BILD_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATION_BILD_BEZEICHNUNG];
			}
		} else if (sAktuellerReport.equals(ReklamationReportFac.REPORT_FEHLERART)) {
			if ("Fehlerart".equals(fieldName)) {
				value = data[index][REPORT_FEHLERART_FEHLERART];
			} else if ("Kunde".equals(fieldName)) {
				value = data[index][REPORT_FEHLERART_KUNDE];
			} else if ("Maschinengruppe".equals(fieldName)) {
				value = data[index][REPORT_FEHLERART_MASCHINENGRUPPE];
			} else if ("Reklamationsart".equals(fieldName)) {
				value = data[index][REPORT_FEHLERART_REKLAMATIONSART];
			} else if ("Reklamationsnummer".equals(fieldName)) {
				value = data[index][REPORT_FEHLERART_REKLAMATIONSNUMMER];
			} else if ("Verursacher".equals(fieldName)) {
				value = data[index][REPORT_FEHLERART_VERURSACHER];
			} else if ("Gruppierung".equals(fieldName)) {
				value = data[index][REPORT_FEHLERART_GRUPPIERUNG];
			} else if ("Grund".equals(fieldName)) {
				value = data[index][REPORT_FEHLERART_GRUND];
			} else if ("ReklamationId".equals(fieldName)) {
				value = data[index][REPORT_FEHLERART_REKLAMATIONID];
			}
		} else if (sAktuellerReport.equals(ReklamationReportFac.REPORT_MITARBEITERREKLAMATION)) {
			if ("Kunde".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERREKLAMATION_KUNDE];
			} else if ("Reklamationsart".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERREKLAMATION_REKLAMATIONSART];
			} else if ("Reklamationsnummer".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERREKLAMATION_REKLAMATIONSNUMMER];
			} else if ("Verursacher".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERREKLAMATION_VERURSACHER];
			}
		} else if (sAktuellerReport.equals(ReklamationReportFac.REPORT_MASCHINENREKLAMATION)) {
			if ("Kunde".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENREKLAMATION_KUNDE];
			} else if ("Reklamationsart".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENREKLAMATION_REKLAMATIONSART];
			} else if ("Reklamationsnummer".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENREKLAMATION_REKLAMATIONSNUMMER];
			} else if ("Verursacher".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENREKLAMATION_VERURSACHER];
			} else if ("Maschinengruppe".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENREKLAMATION_MASCHINENGRUPPE];
			}
		} else if (sAktuellerReport.equals(ReklamationReportFac.REPORT_REKLAMATIONSJOURNAL)) {
			if ("Belegnummer".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_BELEGNR];
			} else if ("Datum".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_DATUM];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_MENGE];
			} else if ("Projekt".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_PROJEKT];
			} else if ("Grund".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_GRUND];
			} else if ("Fehler".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_FEHLER];
			} else if ("Kundelieferant".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_KUNDELIEFERANT];
			} else if ("LieferantKundeunterartLieferant".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_LIEFERANT_KUNDEUNTERART_LIEFERANT];
			} else if ("Reklamationsart".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_REKLAMATIONSART];
			} else if ("Kundeunterart".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_KUNDEUNTERART];
			} else if ("Reklamationsbeleg".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_REKLAMATIONSBELEG];
			} else if ("AZKosten".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_AZKOSTEN];
			} else if ("Materialkosten".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_MATERIALKOSTEN];
			} else if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_ARTIKELNR];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_ARTIKELBEZEICHNUNG];
			}

			else if ("BestellungKundeunterartLieferant".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_BESTELLUNG_KUNDEUNTERART_LIEFERANT];
			} else if ("WareneingangKundeunterartLieferant".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_WARENEINGANG_KUNDEUNTERART_LIEFERANT];
			} else if ("LosKundeunterartFertigung".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_LOS_KUNDEUNTERART_FERTIGUNG];
			} else if ("ArbeitsgangKundeunterartFertigung".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_ARBEITSGANG_KUNDEUNTERART_FERTIGUNG];
			} else if ("MaschineKundeunterartFertigung".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_MASCHINE_KUNDEUNTERART_FERTIGUNG];
			} else if ("VerursacherKundeunterartFertigung".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_VERURSACHER_KUNDEUNTERART_FERTIGUNG];
			}

			else if ("Kostenstelle".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_KOSTENSTELLE];
			} else if ("Fehlerangabe".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_FEHLERANGABE];
			} else if ("Aufnahmeart".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_AUFNAHMEART];
			} else if ("PersonAufnehmer".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_PERSON_AUFNEHMER];
			} else if ("Ansprechpartner".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_ANSPRECHPARTNER];
			} else if ("Analyse".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_ANALYSE];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_KOMMENTAR];
			} else if ("Berechtigt".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_B_BERECHTIGT];
			} else if ("PersonRuecksprache".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_PERSON_RUECKSPRACHE];
			} else if ("Rueckspracheam".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_RUECKSPRECHEZEIT];
			} else if ("RueckspracheMit".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_RUECKSPRECHEMIT];
			} else if ("MassnahmeKurz".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_MASSNAHME_KURZ];
			} else if ("PersonMassnahmeKurz".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_PERSON_MASSNAHME_KURZ];
			} else if ("MassnahmeKurzBis".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_MASSNAHME_BIS_KURZ];
			} else if ("MassnahmeKurzEingefuehrt".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_MASSNAHME_EINGEFUEHRT_KURZ];
			} else if ("MassnahmeKurzKommentar".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_MASSNAHME_KOMMENTAR_KURZ];
			} else if ("MassnahmeMittel".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_MASSNAHME_MITTEL];
			} else if ("PersonMassnahmeMittel".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_PERSON_MASSNAHME_MITTEL];
			} else if ("MassnahmeMittelBis".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_MASSNAHME_BIS_MITTEL];
			} else if ("MassnahmeMittelEingefuehrt".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_MASSNAHME_EINGEFUEHRT_MITTEL];
			} else if ("MassnahmeMittelKommentar".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_MASSNAHME_KOMMENTAR_MITTEL];
			} else if ("MassnahmeLang".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_MASSNAHME_LANG];
			} else if ("PersonMassnahmeLang".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_PERSON_MASSNAHME_LANG];
			} else if ("MassnahmeLangBis".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_MASSNAHME_BIS_LANG];
			} else if ("MassnahmeLangEingefuehrt".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_MASSNAHME_EINGEFUEHRT_LANG];
			} else if ("MassnahmeLangKommentar".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_MASSNAHME_KOMMENTAR_LANG];
			} else if ("Erledigtdatum".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_ERLEDIGTDATUM];
			} else if ("Anlagedatum".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_ANLAGEDATUM];
			} else if ("WareErhalten".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_WARE_ERHALTEN];
			} else if ("PersonErledigt".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_PERSON_ERLEDIGT];
			} else if ("Schwere".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_SCHWERE];
			} else if ("Behandlung".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_BEHANDLUNG];
			} else if ("Wirksamkeit".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_WIRKSAMKEIT];
			} else if ("WirksamkeitKommentar".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_WIRKSAMKEIT_KOMMENTAR];
			} else if ("BetrifftLagerstand".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_B_BETRIFFTLAGERSTAND];
			} else if ("BetrifftGelieferte".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_B_BETRIFFTGELIEFERTE];
			} else if ("StueckLagerstand".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_STUECKLAGERSTAND];
			} else if ("StueckGelieferte".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_STUECKGELIEFERTE];
			} else if ("PersonWirksamkeit".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_PERSON_WIRKSAMKEIT];
			} else if ("WirksamkeitBis".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_WIRKSAMKEIT_BIS];
			} else if ("WirksamkeitDurchgefuehrt".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_WIRKSAMKEIT_DURCHGEFUEHRT];
			} else if ("GrundKommentar".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_GRUND_KOMMENTAR];
			} else if ("MaschineIdentifikationsnummer".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_MASCHINE_IDENTIFIKATIONSNUMMER];
			} else if ("MaschineSeriennummer".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_MASCHINE_SERIENNUMMER];
			} else if ("MaschineInventarnummer".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_MASCHINE_INVENTARNUMMER];
			} else if ("MaschineBezeichnung".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_MASCHINE_BEZEICHNUNG];
			} else if ("KdReklaNr".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_KNDREKLANR];
			} else if ("KdLsNr".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_KNDLSNR];
			} else if ("SnrChnr".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_SNRCHNR];
			} else if ("LfReklaNr".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_LFREKLANR];
			} else if ("LfLsNr".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_LFLSNR];
			} else if ("Artikelkurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_ARTIKELKURZBEZEICHNUNG];
			} else if ("Artikelzusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_ARTIKELZUSATZBEZEICHNUNG];
			} else if ("Artikelzusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_REKLAMATIONSJOURNAL_ARTIKELZUSATZBEZEICHNUNG2];
			}

		} else if (sAktuellerReport.equals(ReklamationReportFac.REPORT_LEFERANTENTERMINTREUE)) {
			if ("Lieferant".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENTERMINTREUE_LIEFERANT];
			} else if ("LIEFERANT_I_ID".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENTERMINTREUE_LIEFERANT_I_ID];
			} else if ("Bestellnummer".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENTERMINTREUE_BESTELLNUMMER];
			} else if ("Wareneingangslieferschein".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENTERMINTREUE_WELIEFERSCHEIN];
			} else if ("Solldatum".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENTERMINTREUE_SOLLTERMIN];
			} else if ("Lieferscheindatum".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENTERMINTREUE_WARENEINGANGSDATUM];
			} else if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENTERMINTREUE_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENTERMINTREUE_BEZEICHNUNG];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENTERMINTREUE_MENGE];
			} else if ("Verspaetungstage".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENTERMINTREUE_VERSPAETUNG];
			}

		} else if (sAktuellerReport.equals(ReklamationReportFac.REPORT_LIEFERANTENBEURTEILUNG)) {
			if ("Lieferant".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENBEURTEILUNG_LIEFERANT];
			} else if ("LieferantLkz".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENBEURTEILUNG_LIEFERANT_LKZ];
			} else if ("LieferantPlz".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENBEURTEILUNG_LIEFERANT_PLZ];
			} else if ("LieferantOrt".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENBEURTEILUNG_LIEFERANT_ORT];
			} else if ("LieferantStrasse".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENBEURTEILUNG_LIEFERANT_STRASSE];
			} else if ("LieferantKbez".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENBEURTEILUNG_LIEFERANT_KBEZ];
			} else if ("Bestellnummer".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENBEURTEILUNG_BESTELLNUMMER];
			} else if ("Wareneingangslieferschein".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENBEURTEILUNG_WELIEFERSCHEIN];
			} else if ("Solldatum".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENBEURTEILUNG_SOLLTERMIN];
			} else if ("Lieferscheindatum".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENBEURTEILUNG_WARENEINGANGSDATUM];
			} else if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENBEURTEILUNG_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENBEURTEILUNG_BEZEICHNUNG];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENBEURTEILUNG_MENGE];
			} else if ("Verspaetungstage".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENBEURTEILUNG_VERSPAETUNG];
			} else if ("PunkteBehandlung".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENBEURTEILUNG_PUNKTE_BEHANDLUNG];
			} else if ("PunkteTermin".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENBEURTEILUNG_PUNKTE_TERMIN];
			} else if ("PunkteTerminJeWEP".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENBEURTEILUNG_PUNKTE_TERMIN_JE_WEP];
			} else if ("PunkteSchwere".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENBEURTEILUNG_PUNKTE_SCHWERE];
			} else if ("PunkteGesamt".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENBEURTEILUNG_PUNKTE_GESAMT];
			} else if ("Reklamationsnummer".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENBEURTEILUNG_REKLAMATIONSNUMMER];
			} else if ("WareneingangID".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENBEURTEILUNG_WEIID];
			} else if ("KlasseBisher".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENBEURTEILUNG_KLASSE_BISHER];
			} else if ("PunkteBisher".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENBEURTEILUNG_PUNKTE_BISHER];
			} else if ("LIEFERANT_I_ID".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENBEURTEILUNG_LIEFERANT_I_ID];
			}
		} else if (sAktuellerReport.equals(ReklamationReportFac.REPORT_OFFENEREKLAMATIONENEINESARTIKEL)) {
			if ("Lieferant".equals(fieldName)) {
				value = data[index][REPORT_OFFENEREKLAMATIONENEINESARTIKEL_LIEFERANT];
			} else if ("Kunde".equals(fieldName)) {
				value = data[index][REPORT_OFFENEREKLAMATIONENEINESARTIKEL_KUNDE];
			} else if ("Datum".equals(fieldName)) {
				value = data[index][REPORT_OFFENEREKLAMATIONENEINESARTIKEL_DATUM];
			} else if ("Grund".equals(fieldName)) {
				value = data[index][REPORT_OFFENEREKLAMATIONENEINESARTIKEL_GRUND];
			} else if ("Belegnummer".equals(fieldName)) {
				value = data[index][REPORT_OFFENEREKLAMATIONENEINESARTIKEL_BELEGNUMMER];
			} else if ("Art".equals(fieldName)) {
				value = data[index][REPORT_OFFENEREKLAMATIONENEINESARTIKEL_ART];
			} else if ("DatumErledigt".equals(fieldName)) {
				value = data[index][REPORT_OFFENEREKLAMATIONENEINESARTIKEL_DATUM_ERLEDIGT];
			} else if ("Fehler".equals(fieldName)) {
				value = data[index][REPORT_OFFENEREKLAMATIONENEINESARTIKEL_FEHLER];
			} else if ("Fehlerangabe".equals(fieldName)) {
				value = data[index][REPORT_OFFENEREKLAMATIONENEINESARTIKEL_FEHLERANGABE];
			}

		}

		return value;
	}

	public boolean next() throws JRException {
		index++;
		return (index < data.length);
	}

}
