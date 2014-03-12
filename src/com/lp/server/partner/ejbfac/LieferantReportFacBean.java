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
package com.lp.server.partner.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.anfrage.fastlanereader.generated.FLRAnfrage;
import com.lp.server.anfrage.service.AnfrageFac;
import com.lp.server.angebot.fastlanereader.generated.FLRAngebot;
import com.lp.server.angebot.service.AngebotFac;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikellieferant;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelbestelltFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtklaDto;
import com.lp.server.artikel.service.HerstellerDto;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellung;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartner;
import com.lp.server.partner.fastlanereader.generated.FLRKontakt;
import com.lp.server.partner.fastlanereader.generated.FLRKurzbrief;
import com.lp.server.partner.fastlanereader.generated.FLRLieferant;
import com.lp.server.partner.fastlanereader.generated.FLRLiefergruppe;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.AnsprechpartnerfunktionDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeReportFac;
import com.lp.server.partner.service.LflfliefergruppeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.LieferantReportFac;
import com.lp.server.partner.service.LieferantbeurteilungDto;
import com.lp.server.partner.service.PASelektionDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.SelektionDto;
import com.lp.server.partner.service.StatistikParamDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.projekt.fastlanereader.generated.FLRProjekt;
import com.lp.server.projekt.service.ProjektServiceFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MwstsatzbezDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.server.util.report.TimingInterceptor;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;

@Stateless
@Interceptors(TimingInterceptor.class)
public class LieferantReportFacBean extends LPReport implements
		LieferantReportFac, JRDataSource {

	private String sAktuellerReport = null;
	private ArrayList<?> alWEPOS = null;
	private Object[][] data = null;

	private static int REPORT_ARTIKELDESLIEFERANTEN_ARTIKELNUMMER = 0;
	private static int REPORT_ARTIKELDESLIEFERANTEN_BEZEICHNUNG = 1;
	private static int REPORT_ARTIKELDESLIEFERANTEN_ZUSATZBEZEICHNUNG = 2;
	private static int REPORT_ARTIKELDESLIEFERANTEN_ZUSATZBEZEICHNUNG2 = 3;
	private static int REPORT_ARTIKELDESLIEFERANTEN_KURZBEZEICHNUNG = 4;
	private static int REPORT_ARTIKELDESLIEFERANTEN_REFERENZNUMMER = 5;
	private static int REPORT_ARTIKELDESLIEFERANTEN_HERSTELLER = 6;
	private static int REPORT_ARTIKELDESLIEFERANTEN_MWSTSATZ = 7;
	private static int REPORT_ARTIKELDESLIEFERANTEN_ARTIKELGRUPPE = 8;
	private static int REPORT_ARTIKELDESLIEFERANTEN_ARTIKELKLASSE = 9;
	private static int REPORT_ARTIKELDESLIEFERANTEN_LIEF_ARTIKELNUMMER = 10;
	private static int REPORT_ARTIKELDESLIEFERANTEN_LIEF_BEZEICHNUNG = 11;
	private static int REPORT_ARTIKELDESLIEFERANTEN_LIEF_EINZELPREIS = 12;
	private static int REPORT_ARTIKELDESLIEFERANTEN_LIEF_RABATT = 13;
	private static int REPORT_ARTIKELDESLIEFERANTEN_LIEF_NETTOPREIS = 14;
	private static int REPORT_ARTIKELDESLIEFERANTEN_LIEF_FIXKOSTEN = 15;
	private static int REPORT_ARTIKELDESLIEFERANTEN_LIEF_GUELTIGAB = 16;
	private static int REPORT_ARTIKELDESLIEFERANTEN_LIEF_STDMENGE = 17;
	private static int REPORT_ARTIKELDESLIEFERANTEN_LIEF_BESTELLMENGE = 18;
	private static int REPORT_ARTIKELDESLIEFERANTEN_LIEF_VERPACKUNGSEINHEIT = 19;
	private static int REPORT_ARTIKELDESLIEFERANTEN_LIEF_WIEDERBESCHAFFUNGSZEIT = 20;
	private static int REPORT_ARTIKELDESLIEFERANTEN_LAGERSTAND = 21;
	private static int REPORT_ARTIKELDESLIEFERANTEN_RESERVIERT = 22;
	private static int REPORT_ARTIKELDESLIEFERANTEN_FEHLMENGE = 23;
	private static int REPORT_ARTIKELDESLIEFERANTEN_INFERTIGUNG = 24;
	private static int REPORT_ARTIKELDESLIEFERANTEN_BESTELLT = 25;
	private static int REPORT_ARTIKELDESLIEFERANTEN_RAHMENRESERVIERT = 26;
	private static int REPORT_ARTIKELDESLIEFERANTEN_RAHMENBESTELLT = 27;
	private static int REPORT_ARTIKELDESLIEFERANTEN_RAHMENDETAILBEDARF = 28;
	private static int REPORT_ARTIKELDESLIEFERANTEN_LIEF_REIHUNG = 29;
	private static int REPORT_ARTIKELDESLIEFERANTEN_EINHEIT = 30;
	private static int REPORT_ARTIKELDESLIEFERANTEN_BESTELLMENENEINHEIT = 31;
	private static int REPORT_ARTIKELDESLIEFERANTEN_UMRECHNUNGSFAKTOR = 32;
	private static int REPORT_ARTIKELDESLIEFERANTEN_VERSTECKT = 33;
	private static int REPORT_ARTIKELDESLIEFERANTEN_LIEFERANT = 34;
	private static int REPORT_ARTIKELDESLIEFERANTEN_LIEFERANT_MINDESTBESTELLWERT = 36;
	private static int REPORT_ARTIKELDESLIEFERANTEN_ANZAHL_SPALTEN = 37;

	private static int REPORT_LIEFERANTENLISTE_BRIEFANREDE = 0;
	private static int REPORT_LIEFERANTENLISTE_PARTNERART = 1;
	private static int REPORT_LIEFERANTENLISTE_ANREDE = 2;
	private static int REPORT_LIEFERANTENLISTE_KURZBEZEICHNUNG = 3;
	private static int REPORT_LIEFERANTENLISTE_CNAME1 = 4;
	private static int REPORT_LIEFERANTENLISTE_CNAME2 = 5;
	private static int REPORT_LIEFERANTENLISTE_CNAME3 = 6;
	private static int REPORT_LIEFERANTENLISTE_TITEL = 7;
	private static int REPORT_LIEFERANTENLISTE_UIDNUMMER = 8;
	private static int REPORT_LIEFERANTENLISTE_STRASSE = 9;
	private static int REPORT_LIEFERANTENLISTE_LAND = 10;
	private static int REPORT_LIEFERANTENLISTE_PLZ = 11;
	private static int REPORT_LIEFERANTENLISTE_ORT = 12;
	private static int REPORT_LIEFERANTENLISTE_LAND_POSTFACH = 13;
	private static int REPORT_LIEFERANTENLISTE_PLZ_POSTFACH = 14;
	private static int REPORT_LIEFERANTENLISTE_ORT_POSTFACH = 15;
	private static int REPORT_LIEFERANTENLISTE_POSTFACH = 16;
	private static int REPORT_LIEFERANTENLISTE_KOMMUNIKATIONSSPRACHE = 17;
	private static int REPORT_LIEFERANTENLISTE_PARTNERKLASSE = 18;
	private static int REPORT_LIEFERANTENLISTE_BRANCHE = 19;
	private static int REPORT_LIEFERANTENLISTE_GERICHTSSTAND = 20;
	private static int REPORT_LIEFERANTENLISTE_FIRMENBUCHNUMMER = 21;
	private static int REPORT_LIEFERANTENLISTE_TELEFON = 22;
	private static int REPORT_LIEFERANTENLISTE_FAX = 23;
	private static int REPORT_LIEFERANTENLISTE_HOMEPAGE = 24;
	private static int REPORT_LIEFERANTENLISTE_EMAIL = 25;
	private static int REPORT_LIEFERANTENLISTE_MWSTSATZ = 26;
	private static int REPORT_LIEFERANTENLISTE_MOEGLICHERLIEFERANT = 27;
	private static int REPORT_LIEFERANTENLISTE_KOSTENSTELLE = 28;
	private static int REPORT_LIEFERANTENLISTE_WARENKONTO = 29;
	private static int REPORT_LIEFERANTENLISTE_KREDITORENKONTO = 30;
	private static int REPORT_LIEFERANTENLISTE_WAEHRUNG = 31;
	private static int REPORT_LIEFERANTENLISTE_ABW_UST_LAND = 32;
	private static int REPORT_LIEFERANTENLISTE_ZAHLUNGSZIEL = 33;
	private static int REPORT_LIEFERANTENLISTE_LIEFERART = 34;
	private static int REPORT_LIEFERANTENLISTE_SPEDITEUR = 35;
	private static int REPORT_LIEFERANTENLISTE_RABATT = 36;
	private static int REPORT_LIEFERANTENLISTE_KREDITLIMIT = 37;
	private static int REPORT_LIEFERANTENLISTE_LIEFERANTENNUMMER = 38;
	private static int REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_ANREDE = 39;
	private static int REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_KURZBEZEICHNUNG = 40;
	private static int REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_CNAME1 = 41;
	private static int REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_CNAME2 = 42;
	private static int REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_CNAME3 = 43;
	private static int REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_TITEL = 44;
	private static int REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_UIDNUMMER = 45;
	private static int REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_STRASSE = 46;
	private static int REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_LAND = 47;
	private static int REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_PLZ = 48;
	private static int REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_ORT = 49;
	private static int REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_LAND_POSTFACH = 50;
	private static int REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_PLZ_POSTFACH = 51;
	private static int REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_ORT_POSTFACH = 52;
	private static int REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_POSTFACH = 53;
	private static int REPORT_LIEFERANTENLISTE_ANSPRECHPARTNER_ANREDE = 54;
	private static int REPORT_LIEFERANTENLISTE_ANSPRECHPARTNER_TITEL = 55;
	private static int REPORT_LIEFERANTENLISTE_ANSPRECHPARTNER_VORNAME = 56;
	private static int REPORT_LIEFERANTENLISTE_ANSPRECHPARTNER_NACHNAME = 57;
	private static int REPORT_LIEFERANTENLISTE_ANSPRECHPARTNER_EMAIL = 58;
	private static int REPORT_LIEFERANTENLISTE_ANSPRECHPARTNER_MOBIL = 59;
	private static int REPORT_LIEFERANTENLISTE_ANSPRECHPARTNER_TELDW = 60;
	private static int REPORT_LIEFERANTENLISTE_ANSPRECHPARTNER_FAXDW = 61;
	private static int REPORT_LIEFERANTENLISTE_ANSPRECHPARTNER_BEMERKUNG = 62;
	private static int REPORT_LIEFERANTENLISTE_BEMERKUNG = 63;
	private static int REPORT_LIEFERANTENLISTE_KUNDENNUMMER = 64;
	private static int REPORT_LIEFERANTENLISTE_BESTELLSPERRE_AM = 65;
	private static int REPORT_LIEFERANTENLISTE_KOMMENTAR = 66;
	private static int REPORT_LIEFERANTENLISTE_FREIGABE = 67;
	private static int REPORT_LIEFERANTENLISTE_SUBREPORT_LIEFERGRUPPEN = 68;
	private static int REPORT_LIEFERANTENLISTE_FREIGABE_TEXT = 69;
	private static int REPORT_LIEFERANTENLISTE_FREIGABE_PERSON = 70;
	private static int REPORT_LIEFERANTENLISTE_FREIGABE_PERSONDATUM = 71;
	private static int REPORT_LIEFERANTENLISTE_LETZTE_BEURTEILUNG_PUNKTE = 72;
	private static int REPORT_LIEFERANTENLISTE_LETZTE_BEURTEILUNG_KLASSE = 73;
	private static int REPORT_LIEFERANTENLISTE_LETZTE_BEURTEILUNG_KOMMENTAR = 74;
	private static int REPORT_LIEFERANTENLISTE_LETZTE_BEURTEILUNG_DATUM = 75;
	private static int REPORT_LIEFERANTENLISTE_LIEFERANT_I_ID = 76;
	private static int REPORT_LIEFERANTENLISTE_ANZAHL_SPALTEN = 77;

	private static int REPORT_LIEFERANTENSTAMMBLATT_ANSPRECHPARTNER_VORNAME = 0;
	private static int REPORT_LIEFERANTENSTAMMBLATT_ANSPRECHPARTNER_NACHNAME = 1;
	private static int REPORT_LIEFERANTENSTAMMBLATT_ANSPRECHPARTNER_FUNKTION = 2;
	private static int REPORT_LIEFERANTENSTAMMBLATT_ANSPRECHPARTNER_TITEL = 3;
	private static int REPORT_LIEFERANTENSTAMMBLATT_ANSPRECHPARTNER_TELDW = 4;
	private static int REPORT_LIEFERANTENSTAMMBLATT_ANSPRECHPARTNER_FAXDW = 5;
	private static int REPORT_LIEFERANTENSTAMMBLATT_ANSPRECHPARTNER_MOBIL = 6;
	private static int REPORT_LIEFERANTENSTAMMBLATT_ANSPRECHPARTNER_EMAIL = 7;

	public JasperPrintLP printLieferantenStatistik(
			StatistikParamDto statistikParamDtoI,
			boolean bVerdichtetNachArtikel, boolean bEingeschraenkt,
			TheClientDto theClientDto) throws EJBExceptionLP {

		alWEPOS = new ArrayList<Object>(10);

		try {
			com.lp.server.system.service.MandantDto mandantDto = getMandantFac()
					.mandantFindByPrimaryKey(theClientDto.getMandant(),
							theClientDto);

			LieferantDto lieferantDto = getLieferantFac()
					.lieferantFindByPrimaryKey(statistikParamDtoI.getId(),
							theClientDto);

			sAktuellerReport = LieferantReportFac.REPORT_LIEFERANT_LIEFERSTATISTIK;

			String sMandantWaehrung = theClientDto.getSMandantenwaehrung();
			// in die gewuenschte Waehrung umrechnen
			alWEPOS = getLieferantFac().getWareneingangspositionen(
					statistikParamDtoI, sMandantWaehrung,
					bVerdichtetNachArtikel, bEingeschraenkt, theClientDto);

			HashMap<String, Object> parameter = new HashMap<String, Object>();
			parameter.put("P_VON", statistikParamDtoI.getDDatumVon());
			parameter.put("P_BIS", statistikParamDtoI.getDDatumBis());

			parameter.put("P_ANREDE", lieferantDto.getPartnerDto()
					.formatAnrede());

			parameter.put(P_WAEHRUNG, theClientDto.getSMandantenwaehrung());

			parameter.put(
					P_ADRESSBLOCK,
					formatAdresseFuerAusdruck(lieferantDto.getPartnerDto(),
							null, mandantDto, theClientDto.getLocUi()));

			Collections.sort(alWEPOS, new ComparatorLF(statistikParamDtoI
					.getISortierungNachWas().intValue()));
			initJRDS(parameter, LieferantReportFac.REPORT_MODUL,
					LieferantReportFac.REPORT_LIEFERANT_LIEFERSTATISTIK,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);

			return getReportPrint();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printLieferantenstammblatt(Integer lieferantIId,
			TheClientDto theClientDto) {
		sAktuellerReport = LieferantReportFac.REPORT_LIEFERANTENSTAMMBLATT;
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		try {
			LieferantDto lieferantDto = getLieferantFac()
					.lieferantFindByPrimaryKey(lieferantIId, theClientDto);

			parameter.put("P_NAME1", lieferantDto.getPartnerDto()
					.getCName1nachnamefirmazeile1());
			parameter.put("P_NAME2", lieferantDto.getPartnerDto()
					.getCName2vornamefirmazeile2());
			parameter.put("P_NAME3", lieferantDto.getPartnerDto()
					.getCName3vorname2abteilung());
			parameter.put("P_STRASSE", lieferantDto.getPartnerDto()
					.getCStrasse());

			if (lieferantDto.getPartnerDto().getLandplzortDto() != null) {
				parameter.put("P_LANDPLZORT", lieferantDto.getPartnerDto()
						.getLandplzortDto().formatLandPlzOrt());
			}

			parameter.put("P_TELEFON", lieferantDto.getPartnerDto()
					.getCTelefon());

			parameter.put("P_HOMEPAGE", lieferantDto.getPartnerDto()
					.getCHomepage());

			parameter.put("P_FAX", lieferantDto.getPartnerDto().getCFax());

			parameter.put("P_EMAIL", lieferantDto.getPartnerDto().getCEmail());

			parameter.put("P_HINWEISEXTERN", lieferantDto.getCHinweisextern());
			parameter.put("P_HINWEISINTERN", lieferantDto.getCHinweisintern());

			String sKommentar = null;

			if (lieferantDto.getPartnerDto().getXBemerkung() != null
					&& lieferantDto.getPartnerDto().getXBemerkung().length() > 0) {
				sKommentar = getTextRespectUISpr("lp.partner",
						theClientDto.getMandant(), theClientDto.getLocUi());
				sKommentar += ":\n"
						+ lieferantDto.getPartnerDto().getXBemerkung() + "\n";
			}

			if (lieferantDto.getXKommentar() != null
					&& lieferantDto.getXKommentar().length() > 0) {
				if (sKommentar == null) {
					sKommentar = getTextRespectUISpr("lp.kunde",
							theClientDto.getMandant(), theClientDto.getLocUi());
				} else {
					sKommentar += "\n"
							+ getTextRespectUISpr("lp.kunde",
									theClientDto.getMandant(),
									theClientDto.getLocUi());
				}
				sKommentar += ":\n" + lieferantDto.getXKommentar();
			}

			parameter.put("P_KOMMENTAR", sKommentar);

			// Selektionen
			PASelektionDto[] paselDtos = getPartnerFac()
					.pASelektionFindByPartnerIId(lieferantDto.getPartnerIId());
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
					.getAllAnsprechpartner(lieferantDto.getPartnerIId(),
							theClientDto);

			data = new Object[alAnsprechpartner.size()][8];
			if (alAnsprechpartner.size() == 0) {
				data = new Object[0][8];
			}

			for (int i = 0; i < alAnsprechpartner.size(); i++) {
				AnsprechpartnerDto dtoTemp = (AnsprechpartnerDto) alAnsprechpartner
						.get(i);
				data[i][REPORT_LIEFERANTENSTAMMBLATT_ANSPRECHPARTNER_NACHNAME] = dtoTemp
						.getPartnerDto().getCName1nachnamefirmazeile1();
				data[i][REPORT_LIEFERANTENSTAMMBLATT_ANSPRECHPARTNER_VORNAME] = dtoTemp
						.getPartnerDto().getCName2vornamefirmazeile2();
				data[i][REPORT_LIEFERANTENSTAMMBLATT_ANSPRECHPARTNER_TITEL] = dtoTemp
						.getPartnerDto().getCTitel();

				if (dtoTemp.getAnsprechpartnerfunktionIId() != null) {
					AnsprechpartnerfunktionDto dto = getAnsprechpartnerFac()
							.ansprechpartnerfunktionFindByPrimaryKey(
									dtoTemp.getAnsprechpartnerfunktionIId(),
									theClientDto);
					data[i][REPORT_LIEFERANTENSTAMMBLATT_ANSPRECHPARTNER_FUNKTION] = dto
							.getBezeichnung();
				}

				data[i][REPORT_LIEFERANTENSTAMMBLATT_ANSPRECHPARTNER_FAXDW] = dtoTemp
						.getCFax();

				data[i][REPORT_LIEFERANTENSTAMMBLATT_ANSPRECHPARTNER_TELDW] = dtoTemp
						.getCTelefon();

				data[i][REPORT_LIEFERANTENSTAMMBLATT_ANSPRECHPARTNER_MOBIL] = dtoTemp
						.getCHandy();

				data[i][REPORT_LIEFERANTENSTAMMBLATT_ANSPRECHPARTNER_EMAIL] = dtoTemp
						.getCEmail();

			}

			// Bestellungen

			Session session = FLRSessionFactory.getFactory().openSession();
			// Filter und Sortierung
			Criteria crit = session.createCriteria(FLRBestellung.class);
			// Filter nach Kunde
			crit.add(Restrictions.eq(
					BestellungFac.FLR_BESTELLUNG_LIEFERANT_I_ID_BESTELLADRESSE,
					lieferantIId));
			crit.addOrder(Order.desc(BestellungFac.FLR_BESTELLUNG_C_NR));

			List<?> list = crit.list();
			Iterator<?> it = list.iterator();
			ArrayList<Object[]> al = new ArrayList<Object[]>();
			while (it.hasNext()) {
				FLRBestellung flrauftrag = (FLRBestellung) it.next();

				Object[] o = new Object[7];
				o[0] = flrauftrag.getC_nr();
				o[1] = flrauftrag.getBestellungstatus_c_nr();

				BigDecimal wert = flrauftrag.getN_bestellwert();

				if (wert != null) {

					wert = getLocaleFac()
							.rechneUmInMandantenWaehrung(
									wert,
									new BigDecimal(
											flrauftrag
													.getF_wechselkursmandantwaehrungbestellungswaehrung()));

					o[2] = wert;
				}
				o[3] = flrauftrag.getT_liefertermin();
				o[4] = flrauftrag.getC_bezprojektbezeichnung();
				al.add(o);
			}

			if (al.size() > 0) {
				String[] fieldnames = new String[] { "Bestellnummer", "Status",
						"Wert", "Liefertermin", "Projekt" };
				Object[][] dataSub = new Object[al.size()][fieldnames.length];
				dataSub = (Object[][]) al.toArray(dataSub);

				parameter
						.put("P_SUBREPORT_BESTELLUNGEN",
								((net.sf.jasperreports.engine.JRDataSource) new LPDatenSubreport(
										dataSub, fieldnames)));
			}
			session.close();

			// Kurzbriefe
			session = FLRSessionFactory.getFactory().openSession();
			// Filter und Sortierung
			crit = session.createCriteria(FLRKurzbrief.class);
			// Filter nach Kunde
			crit.add(Restrictions.eq("partner_i_id",
					lieferantDto.getPartnerIId()));
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

			// Kontakte

			// Kurzbriefe
			session = FLRSessionFactory.getFactory().openSession();
			// Filter und Sortierung
			crit = session.createCriteria(FLRKontakt.class);
			// Filter nach Kunde
			crit.add(Restrictions.eq("partner_i_id",
					lieferantDto.getPartnerIId()));
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
			crit = session.createCriteria(FLRAnfrage.class);
			// Filter nach Lieferant
			crit.createAlias("flrlieferant", "l");

			crit.add(Restrictions.eq("l.i_id", lieferantIId));
			crit.addOrder(Order.desc("c_nr"));

			list = crit.list();
			it = list.iterator();
			al = new ArrayList<Object[]>();
			while (it.hasNext()) {
				FLRAnfrage angebot = (FLRAnfrage) it.next();

				Object[] o = new Object[8];
				o[0] = angebot.getC_nr();
				o[1] = angebot.getAnfragestatus_c_nr();

				BigDecimal wert = angebot
						.getN_gesamtanfragewertinanfragewaehrung();

				if (wert != null) {

					wert = getLocaleFac()
							.rechneUmInMandantenWaehrung(
									wert,
									new BigDecimal(
											angebot.getF_wechselkursmandantwaehrungzuanfragewaehrung()));

					o[2] = wert;
				}
				o[3] = angebot.getT_belegdatum();

				o[4] = angebot.getC_bez();

				al.add(o);
			}

			if (al.size() > 0) {
				String[] fieldnames = new String[] { "Anfragenummer", "Status",
						"Wert", "Belegdatum", "Projekt" };
				Object[][] dataSub = new Object[al.size()][fieldnames.length];
				dataSub = (Object[][]) al.toArray(dataSub);

				parameter
						.put("P_SUBREPORT_ANFRAGEN",
								((net.sf.jasperreports.engine.JRDataSource) new LPDatenSubreport(
										dataSub, fieldnames)));
			}
			session.close();

			parameter.put(
					"P_SUBREPORT_PROJEKTE",
					getKundeReportFac().getSubreportProjekte(
							lieferantDto.getPartnerIId(), false, theClientDto));

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		parameter.put("P_MANDANTENWAEHRUNG",
				theClientDto.getSMandantenwaehrung());

		initJRDS(parameter, LieferantReportFac.REPORT_MODUL,
				LieferantReportFac.REPORT_LIEFERANTENSTAMMBLATT,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printArtikeldesLieferanten(Integer lieferantIId,
			boolean bSortiertNachBezeichnung, boolean bMitVersteckten,
			boolean bSortiertNachLieferant, boolean bNurLagerbewirtschaftete,
			java.sql.Timestamp tStichtag, TheClientDto theClientDto) {

		sAktuellerReport = LieferantReportFac.REPORT_ARTIKELDESLIEFERANTEN;

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		parameter.put("P_MITVERSTECKTEN", new Boolean(bMitVersteckten));
		parameter.put("P_SORTIERTNACHLIEFERANT", new Boolean(
				bSortiertNachLieferant));
		parameter.put("P_SORTIERTNACHBEZEICHNUNG", new Boolean(
				bSortiertNachBezeichnung));
		parameter.put("P_NURLAGERBEWIRTSCHAFTETE", new Boolean(
				bNurLagerbewirtschaftete));

		if (lieferantIId != null) {
			try {
				LieferantDto lieferantDto = getLieferantFac()
						.lieferantFindByPrimaryKey(lieferantIId, theClientDto);
				MandantDto mandantDto = getMandantFac()
						.mandantFindByPrimaryKey(theClientDto.getMandant(),
								theClientDto);
				parameter.put(
						"P_LIEFERANT_ADRESSBLOCK",
						formatAdresseFuerAusdruck(lieferantDto.getPartnerDto(),
								null, mandantDto, theClientDto.getLocUi()));

				parameter.put("P_LIEFERANT_MINDESTBESTELLWERT",
						lieferantDto.getNMindestbestellwert());

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT distinct al.lieferant_i_id, al.artikel_i_id,flrartikel.c_nr,al.flrlieferant.flrpartner.c_name1nachnamefirmazeile1  FROM FLRArtikellieferant al LEFT JOIN al.flrartikel as flrartikel WHERE 1=1 ";
		if (lieferantIId != null) {
			sQuery += " AND al.lieferant_i_id=" + lieferantIId;
		}

		if (tStichtag != null) {

			parameter.put("P_STICHTAG", tStichtag);
			sQuery += " AND al.t_preisgueltigab<='"
					+ Helper.formatTimestampWithSlashes(Helper
							.cutTimestamp(tStichtag)) + "'";
		}

		if (bNurLagerbewirtschaftete) {
			sQuery += " AND al.flrartikel.b_lagerbewirtschaftet=1 ";
		}
		if (bMitVersteckten==false) {
			sQuery += " AND al.flrartikel.b_versteckt=0 ";
		}

		if (bSortiertNachLieferant) {
			sQuery += " ORDER BY al.flrlieferant.flrpartner.c_name1nachnamefirmazeile1 ASC, flrartikel.c_nr ASC";
		} else {
			sQuery += " ORDER BY flrartikel.c_nr ASC";
		}

		Query query = session.createQuery(sQuery);
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		int row = 0;

		data = new Object[results.size()][REPORT_ARTIKELDESLIEFERANTEN_ANZAHL_SPALTEN];

		HashMap<Integer, LieferantDto> hmLieferanten = new HashMap<Integer, LieferantDto>();

		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();
			Integer lieferantIIdZeile = (Integer) o[0];

			if (!hmLieferanten.containsKey(lieferantIIdZeile)) {
				hmLieferanten.put(
						lieferantIIdZeile,
						getLieferantFac().lieferantFindByPrimaryKey(
								lieferantIIdZeile, theClientDto));
			}

			Integer artikelIId = (Integer) o[1];
			try {

				LieferantDto lfDto = hmLieferanten.get(lieferantIIdZeile);
				data[row][REPORT_ARTIKELDESLIEFERANTEN_LIEFERANT] = lfDto
						.getPartnerDto().formatFixName1Name2();
				data[row][REPORT_ARTIKELDESLIEFERANTEN_LIEFERANT_MINDESTBESTELLWERT] = lfDto
						.getNMindestbestellwert();
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(artikelIId, theClientDto);

				data[row][REPORT_ARTIKELDESLIEFERANTEN_ARTIKELNUMMER] = artikelDto
						.getCNr();

				data[row][REPORT_ARTIKELDESLIEFERANTEN_EINHEIT] = artikelDto
						.getEinheitCNr();
				data[row][REPORT_ARTIKELDESLIEFERANTEN_VERSTECKT] = Helper
						.short2Boolean(artikelDto.getBVersteckt());
				data[row][REPORT_ARTIKELDESLIEFERANTEN_BESTELLMENENEINHEIT] = artikelDto
						.getEinheitCNrBestellung();
				data[row][REPORT_ARTIKELDESLIEFERANTEN_UMRECHNUNGSFAKTOR] = artikelDto
						.getNUmrechnungsfaktor();

				if (artikelDto.getArtikelsprDto() != null) {
					data[row][REPORT_ARTIKELDESLIEFERANTEN_BEZEICHNUNG] = artikelDto
							.getArtikelsprDto().getCBez();
					data[row][REPORT_ARTIKELDESLIEFERANTEN_KURZBEZEICHNUNG] = artikelDto
							.getArtikelsprDto().getCKbez();
					data[row][REPORT_ARTIKELDESLIEFERANTEN_ZUSATZBEZEICHNUNG] = artikelDto
							.getArtikelsprDto().getCZbez();
					data[row][REPORT_ARTIKELDESLIEFERANTEN_ZUSATZBEZEICHNUNG2] = artikelDto
							.getArtikelsprDto().getCZbez2();
				}
				data[row][REPORT_ARTIKELDESLIEFERANTEN_REFERENZNUMMER] = artikelDto
						.getCReferenznr();

				// Artikelklasse
				if (artikelDto.getArtklaIId() != null) {
					ArtklaDto akDto = getArtikelFac().artklaFindByPrimaryKey(
							artikelDto.getArtklaIId(), theClientDto);
					data[row][REPORT_ARTIKELDESLIEFERANTEN_ARTIKELKLASSE] = akDto
							.getBezeichnung();
				}
				// Artikelgruppe
				if (artikelDto.getArtgruIId() != null) {
					ArtgruDto agDto = getArtikelFac().artgruFindByPrimaryKey(
							artikelDto.getArtgruIId(), theClientDto);
					data[row][REPORT_ARTIKELDESLIEFERANTEN_ARTIKELGRUPPE] = agDto
							.getBezeichnung();
				}

				// Hersteller
				if (artikelDto.getHerstellerIId() != null) {
					HerstellerDto hDto = getArtikelFac()
							.herstellerFindByPrimaryKey(
									artikelDto.getHerstellerIId(), theClientDto);
					data[row][REPORT_ARTIKELDESLIEFERANTEN_HERSTELLER] = hDto
							.getCNr();
				}
				// MWstsatz
				if (artikelDto.getMwstsatzbezIId() != null) {
					MwstsatzbezDto mDto = getMandantFac()
							.mwstsatzbezFindByPrimaryKey(
									artikelDto.getMwstsatzbezIId(),
									theClientDto);
					data[row][REPORT_ARTIKELDESLIEFERANTEN_MWSTSATZ] = mDto
							.getCBezeichnung();
				}

				java.sql.Date tDatumPreisgueltigkeit = new java.sql.Date(
						System.currentTimeMillis());
				if (tStichtag != null) {
					tDatumPreisgueltigkeit = new java.sql.Date(
							tStichtag.getTime());
				}

				ArtikellieferantDto artikellieferantDto = getArtikelFac()
						.artikellieferantFindByArtikellIIdLieferantIIdTPreisgueltigabKleiner(
								artikelIId, lieferantIIdZeile,
								tDatumPreisgueltigkeit, theClientDto);

				if (artikellieferantDto != null) {

					data[row][REPORT_ARTIKELDESLIEFERANTEN_LIEF_ARTIKELNUMMER] = artikellieferantDto
							.getCArtikelnrlieferant();
					data[row][REPORT_ARTIKELDESLIEFERANTEN_LIEF_BESTELLMENGE] = artikellieferantDto
							.getFMindestbestelmenge();
					data[row][REPORT_ARTIKELDESLIEFERANTEN_LIEF_BEZEICHNUNG] = artikellieferantDto
							.getCBezbeilieferant();
					data[row][REPORT_ARTIKELDESLIEFERANTEN_LIEF_EINZELPREIS] = artikellieferantDto
							.getNEinzelpreis();
					data[row][REPORT_ARTIKELDESLIEFERANTEN_LIEF_FIXKOSTEN] = artikellieferantDto
							.getNFixkosten();
					data[row][REPORT_ARTIKELDESLIEFERANTEN_LIEF_GUELTIGAB] = artikellieferantDto
							.getTPreisgueltigab();
					data[row][REPORT_ARTIKELDESLIEFERANTEN_LIEF_NETTOPREIS] = artikellieferantDto
							.getNNettopreis();
					data[row][REPORT_ARTIKELDESLIEFERANTEN_LIEF_RABATT] = artikellieferantDto
							.getFRabatt();
					data[row][REPORT_ARTIKELDESLIEFERANTEN_LIEF_STDMENGE] = artikellieferantDto
							.getFStandardmenge();
					data[row][REPORT_ARTIKELDESLIEFERANTEN_LIEF_VERPACKUNGSEINHEIT] = artikellieferantDto
							.getNVerpackungseinheit();
					data[row][REPORT_ARTIKELDESLIEFERANTEN_LIEF_WIEDERBESCHAFFUNGSZEIT] = artikellieferantDto
							.getIWiederbeschaffungszeit();
					data[row][REPORT_ARTIKELDESLIEFERANTEN_LIEF_REIHUNG] = artikellieferantDto
							.getISort();
				}
				//
				data[row][REPORT_ARTIKELDESLIEFERANTEN_LAGERSTAND] = getLagerFac()
						.getLagerstandAllerLagerEinesMandanten(artikelIId,
								theClientDto);
				data[row][REPORT_ARTIKELDESLIEFERANTEN_RESERVIERT] = getReservierungFac()
						.getAnzahlReservierungen(artikelIId, theClientDto);
				data[row][REPORT_ARTIKELDESLIEFERANTEN_FEHLMENGE] = getFehlmengeFac()
						.getAnzahlFehlmengeEinesArtikels(artikelIId,
								theClientDto);

				data[row][REPORT_ARTIKELDESLIEFERANTEN_INFERTIGUNG] = getFertigungFac()
						.getAnzahlInFertigung(artikelIId, theClientDto);
				data[row][REPORT_ARTIKELDESLIEFERANTEN_BESTELLT] = getArtikelbestelltFac()
						.getAnzahlBestellt(artikelIId);
				data[row][REPORT_ARTIKELDESLIEFERANTEN_RAHMENRESERVIERT] = getReservierungFac()
						.getAnzahlRahmenreservierungen(artikelIId, theClientDto);

				Hashtable htAnzahlRahmenbestellt = getArtikelbestelltFac()
						.getAnzahlRahmenbestellt(artikelIId, theClientDto);

				if (htAnzahlRahmenbestellt
						.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
					BigDecimal rahmenbestellt = (BigDecimal) htAnzahlRahmenbestellt
							.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
					data[row][REPORT_ARTIKELDESLIEFERANTEN_RAHMENBESTELLT] = rahmenbestellt;

				}

				data[row][REPORT_ARTIKELDESLIEFERANTEN_RAHMENDETAILBEDARF] = getRahmenbedarfeFac()
						.getSummeAllerRahmenbedarfeEinesArtikels(artikelIId);

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
			row++;
		}

		if (bSortiertNachBezeichnung) {

			for (int i = data.length - 1; i > 0; --i) {
				for (int j = 0; j < i; ++j) {
					Object[] o = data[j];
					Object[] o1 = data[j + 1];

					String wert = (String) o[REPORT_ARTIKELDESLIEFERANTEN_BEZEICHNUNG];
					String wert1 = (String) o1[REPORT_ARTIKELDESLIEFERANTEN_BEZEICHNUNG];

					if (wert == null) {
						wert = "";
					}
					if (wert1 == null) {
						wert1 = "";
					}

					if (bSortiertNachLieferant) {
						wert = Helper
								.fitString2Length(
										(String) o[REPORT_ARTIKELDESLIEFERANTEN_LIEFERANT],
										80, ' ')
								+ wert;
						wert1 = Helper
								.fitString2Length(
										(String) o1[REPORT_ARTIKELDESLIEFERANTEN_LIEFERANT],
										80, ' ')
								+ wert1;
					}

					if (wert.toUpperCase().compareTo(wert1.toUpperCase()) > 0) {
						data[j] = o1;
						data[j + 1] = o;
					}
				}
			}

		}

		initJRDS(parameter, LieferantReportFac.REPORT_MODUL,
				LieferantReportFac.REPORT_ARTIKELDESLIEFERANTEN,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	public boolean next() throws JRException {

		index++;

		if (sAktuellerReport
				.equals(LieferantReportFac.REPORT_LIEFERANT_LIEFERSTATISTIK)) {
			return (index < alWEPOS.size());

		} else {
			return (index < data.length);
		}

	}

	public Object getFieldValue(JRField jRField) throws JRException {

		Object ret = null;
		String fieldName = jRField.getName();
		if (sAktuellerReport
				.equals(LieferantReportFac.REPORT_LIEFERANT_LIEFERSTATISTIK)) {
			if ("F_BELEGDATUM".equals(fieldName)) {
				ret = ((WareneingangspositionenDto) alWEPOS.get(index))
						.getDBelegdatum();
			} else if ("F_WAS".equals(fieldName)) {
				ret = ((WareneingangspositionenDto) alWEPOS.get(index))
						.getSWas();
			} else if ("F_MENGE".equals(fieldName)) {
				ret = ((WareneingangspositionenDto) alWEPOS.get(index))
						.getBdMenge();
			} else if ("F_EINHEIT".equals(fieldName)) {
				ret = ((WareneingangspositionenDto) alWEPOS.get(index))
						.getSEinheit();
			} else if ("F_WERT".equals(fieldName)) {
				ret = ((WareneingangspositionenDto) alWEPOS.get(index))
						.getBdWert();
			} else if ("F_IDENT".equals(fieldName)) {
				ret = ((WareneingangspositionenDto) alWEPOS.get(index))
						.getSIdent();
			} else if ("F_NR".equals(fieldName)) {
				ret = ((WareneingangspositionenDto) alWEPOS.get(index))
						.getSNr();
			} else if (F_BEZEICHNUNG.equals(fieldName)) {
				ret = ((WareneingangspositionenDto) alWEPOS.get(index))
						.getSBezeichnung();
			} else if ("F_PREIS".equals(fieldName)) {
				ret = ((WareneingangspositionenDto) alWEPOS.get(index))
						.getBdPreis();
			}
		} else if (sAktuellerReport
				.equals(LieferantReportFac.REPORT_ARTIKELDESLIEFERANTEN)) {
			if ("Artikelnummer".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_BEZEICHNUNG];
			} else if ("Artikelgruppe".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_ARTIKELGRUPPE];
			} else if ("Artkelklasse".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_ARTIKELKLASSE];
			} else if ("Hersteller".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_HERSTELLER];
			} else if ("Mwstsatz".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_MWSTSATZ];
			} else if ("Referenznummer".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_REFERENZNUMMER];
			} else if ("Versteckt".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_VERSTECKT];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_ZUSATZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung2".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_ZUSATZBEZEICHNUNG2];
			} else if ("Kurzbezeichnung".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_KURZBEZEICHNUNG];
			} else if ("LiefArtikelnummer".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_LIEF_ARTIKELNUMMER];
			} else if ("LiefBestellmenge".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_LIEF_BESTELLMENGE];
			} else if ("LiefBezeichnung".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_LIEF_BEZEICHNUNG];
			} else if ("LiefEinzelpreis".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_LIEF_EINZELPREIS];
			} else if ("LiefFixkosten".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_LIEF_FIXKOSTEN];
			} else if ("LiefGueltigab".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_LIEF_GUELTIGAB];
			} else if ("LiefNettopreis".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_LIEF_NETTOPREIS];
			} else if ("LiefRabatt".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_LIEF_RABATT];
			} else if ("LiefStdmenge".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_LIEF_STDMENGE];
			} else if ("LiefVerpackungseinheit".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_LIEF_VERPACKUNGSEINHEIT];
			} else if ("LiefReihung".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_LIEF_REIHUNG];
			} else if ("LiefWiederbeschaffungszeit".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_LIEF_WIEDERBESCHAFFUNGSZEIT];
			} else if ("Lagerstand".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_LAGERSTAND];
			} else if ("Reserviert".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_RESERVIERT];
			} else if ("Fehlmenge".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_FEHLMENGE];
			} else if ("Infertigung".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_INFERTIGUNG];
			} else if ("Bestellt".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_BESTELLT];
			} else if ("Rahmenreserviert".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_RAHMENRESERVIERT];
			} else if ("Rahmenbestellt".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_RAHMENBESTELLT];
			} else if ("Rahmendetailbedarf".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_RAHMENDETAILBEDARF];
			} else if ("Einheit".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_EINHEIT];
			} else if ("Bestellmengeneinheit".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_BESTELLMENENEINHEIT];
			} else if ("Umrechnungsfaktor".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_UMRECHNUNGSFAKTOR];
			}else if ("Lieferant".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_LIEFERANT];
			}else if ("LieferantMindestbestellwert".equals(fieldName)) {
				ret = data[index][REPORT_ARTIKELDESLIEFERANTEN_LIEFERANT_MINDESTBESTELLWERT];
			}

		} else if (sAktuellerReport
				.equals(LieferantReportFac.REPORT_LIEFERANTENLISTE)) {
			if ("F_ABW_UST_LAND".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_ABW_UST_LAND];
			} else if ("F_ANREDE".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_ANREDE];
			} else if ("F_ANSPRECHPARTNER_ANREDE".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_ANSPRECHPARTNER_ANREDE];
			} else if ("F_ANSPRECHPARTNER_EMAIL".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_ANSPRECHPARTNER_EMAIL];
			} else if ("F_ANSPRECHPARTNER_FAXDW".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_ANSPRECHPARTNER_FAXDW];
			} else if ("F_ANSPRECHPARTNER_MOBIL".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_ANSPRECHPARTNER_MOBIL];
			} else if ("F_ANSPRECHPARTNER_BEMERKUNG".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_ANSPRECHPARTNER_BEMERKUNG];
			} else if ("F_ANSPRECHPARTNER_NACHNAME".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_ANSPRECHPARTNER_NACHNAME];
			} else if ("F_ANSPRECHPARTNER_TELDW".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_ANSPRECHPARTNER_TELDW];
			} else if ("F_ANSPRECHPARTNER_TITEL".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_ANSPRECHPARTNER_TITEL];
			} else if ("F_ANSPRECHPARTNER_VORNAME".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_ANSPRECHPARTNER_VORNAME];
			} else if ("F_BRANCHE".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_BRANCHE];
			} else if ("F_BRIEFANREDE".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_BRIEFANREDE];
			} else if ("F_CNAME1".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_CNAME1];
			} else if ("F_CNAME2".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_CNAME2];
			} else if ("F_CNAME3".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_CNAME3];
			} else if ("F_BEMERKUNG".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_BEMERKUNG];
			} else if ("F_KREDITORENKONTO".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_KREDITORENKONTO];
			} else if ("F_EMAIL".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_EMAIL];
			} else if ("F_WARENKONTO".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_WARENKONTO];
			} else if ("F_FAX".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_FAX];
			} else if ("F_FIRMENBUCHNUMMER".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_FIRMENBUCHNUMMER];
			} else if ("F_GERICHTSSTAND".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_GERICHTSSTAND];
			} else if ("F_HOMEPAGE".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_HOMEPAGE];
			} else if ("F_MOEGLICHERLIEFERANT".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_MOEGLICHERLIEFERANT];
			} else if ("F_KOMMUNIKATIONSSPRACHE".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_KOMMUNIKATIONSSPRACHE];
			} else if ("F_KOSTENSTELLE".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_KOSTENSTELLE];
			} else if ("F_KREDITLIMIT".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_KREDITLIMIT];
			} else if ("F_KURZBEZEICHNUNG".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_KURZBEZEICHNUNG];
			} else if ("F_LAND".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_LAND];
			} else if ("F_LAND_POSTFACH".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_LAND_POSTFACH];
			} else if ("F_LIEFERANTENNUMMER".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_LIEFERANTENNUMMER];
			} else if ("F_LIEFERART".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_LIEFERART];
			} else if ("F_MWSTSATZ".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_MWSTSATZ];
			} else if ("F_ORT".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_ORT];
			} else if ("F_ORT_POSTFACH".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_ORT_POSTFACH];
			} else if ("F_PARTNERART".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_PARTNERART];
			} else if ("F_PARTNERKLASSE".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_PARTNERKLASSE];
			} else if ("F_PLZ".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_PLZ];
			} else if ("F_PLZ_POSTFACH".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_PLZ_POSTFACH];
			} else if ("F_POSTFACH".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_POSTFACH];
			} else if ("F_RABATT".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_RABATT];
			} else if ("F_RECHNUNGSADRESSE_ANREDE".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_ANREDE];
			} else if ("F_RECHNUNGSADRESSE_CNAME1".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_CNAME1];
			} else if ("F_RECHNUNGSADRESSE_CNAME2".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_CNAME2];
			} else if ("F_RECHNUNGSADRESSE_CNAME3".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_CNAME3];
			} else if ("F_RECHNUNGSADRESSE_KURZBEZEICHNUNG".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_KURZBEZEICHNUNG];
			} else if ("F_RECHNUNGSADRESSE_LAND".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_LAND];
			} else if ("F_RECHNUNGSADRESSE_LAND_POSTFACH".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_LAND_POSTFACH];
			} else if ("F_RECHNUNGSADRESSE_ORT".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_ORT];
			} else if ("F_RECHNUNGSADRESSE_ORT_POSTFACH".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_ORT_POSTFACH];
			} else if ("F_RECHNUNGSADRESSE_PLZ".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_PLZ];
			} else if ("F_RECHNUNGSADRESSE_PLZ_POSTFACH".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_PLZ_POSTFACH];
			} else if ("F_RECHNUNGSADRESSE_POSTFACH".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_POSTFACH];
			} else if ("F_RECHNUNGSADRESSE_STRASSE".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_STRASSE];
			} else if ("F_RECHNUNGSADRESSE_TITEL".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_TITEL];
			} else if ("F_RECHNUNGSADRESSE_UIDNUMMER".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_UIDNUMMER];
			} else if ("F_SPEDITEUR".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_SPEDITEUR];
			} else if ("F_STRASSE".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_STRASSE];
			} else if ("F_TELEFON".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_TELEFON];
			} else if ("F_TITEL".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_TITEL];
			} else if ("F_UIDNUMMER".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_UIDNUMMER];
			} else if ("F_WAEHRUNG".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_WAEHRUNG];
			} else if ("F_ZAHLUNGSZIEL".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_ZAHLUNGSZIEL];
			} else if ("F_KUNDENNUMMER".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_KUNDENNUMMER];
			} else if ("F_BESTELLSPERRE_AM".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_BESTELLSPERRE_AM];
			} else if ("F_KOMMENTAR".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_KOMMENTAR];
			} else if ("F_FREIGABE".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_FREIGABE];
			} else if ("F_SUBREPORT_LIEFERGRUPPEN".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_SUBREPORT_LIEFERGRUPPEN];
			} else if ("F_FREIGABE_TEXT".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_FREIGABE_TEXT];
			} else if ("F_FREIGABE_PERSON".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_FREIGABE_PERSON];
			} else if ("F_FREIGABE_PERSONDATUM".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_FREIGABE_PERSONDATUM];
			} else if ("F_LETZTE_BEURTEILUNG_PUNKTE".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_LETZTE_BEURTEILUNG_PUNKTE];
			} else if ("F_LETZTE_BEURTEILUNG_KLASSE".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_LETZTE_BEURTEILUNG_KLASSE];
			} else if ("F_LETZTE_BEURTEILUNG_DATUM".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_LETZTE_BEURTEILUNG_DATUM];
			} else if ("F_LETZTE_BEURTEILUNG_KOMMENTAR".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_LETZTE_BEURTEILUNG_KOMMENTAR];
			} else if ("F_LIEFERANT_I_ID".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENLISTE_LIEFERANT_I_ID];
			}
		} else if (sAktuellerReport
				.equals(LieferantReportFac.REPORT_LIEFERANTENSTAMMBLATT)) {
			if ("F_FAXDW".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENSTAMMBLATT_ANSPRECHPARTNER_FAXDW];
			} else if ("F_FUNKTION".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENSTAMMBLATT_ANSPRECHPARTNER_FUNKTION];
			} else if ("F_MOBIL".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENSTAMMBLATT_ANSPRECHPARTNER_MOBIL];
			} else if ("F_EMAIL".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENSTAMMBLATT_ANSPRECHPARTNER_EMAIL];
			} else if ("F_NACHNAME".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENSTAMMBLATT_ANSPRECHPARTNER_NACHNAME];
			} else if ("F_TELDW".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENSTAMMBLATT_ANSPRECHPARTNER_TELDW];
			} else if ("F_TITEL".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENSTAMMBLATT_ANSPRECHPARTNER_TITEL];
			} else if ("F_VORNAME".equals(fieldName)) {
				ret = data[index][REPORT_LIEFERANTENSTAMMBLATT_ANSPRECHPARTNER_VORNAME];
			}
		}
		return ret;
	}

	private LPDatenSubreport getSubreportLiefergruppen(Integer lieferantIId,
			TheClientDto theClientDto) {

		// Projekte
		Session session = FLRSessionFactory.getFactory().openSession();
		// Filter und Sortierung
		Criteria crit = session.createCriteria(FLRLiefergruppe.class);
		// Filter nach Kunde
		crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));

		crit.addOrder(Order.desc("c_nr"));

		List<?> list = crit.list();
		Iterator it = list.iterator();
		ArrayList al = new ArrayList<Object[]>();
		while (it.hasNext()) {
			FLRLiefergruppe projekt = (FLRLiefergruppe) it.next();

			Object[] o = new Object[11];
			o[0] = projekt.getC_nr();

			Boolean b = Boolean.FALSE;

			if (lieferantIId != null) {
				LflfliefergruppeDto[] dto = getLieferantFac()
						.lflfliefergruppeFindByLieferantIIdLiefergruppeIIdOhneExc(
								lieferantIId, projekt.getI_id(), theClientDto);

				if (dto != null && dto.length > 0) {
					b = Boolean.TRUE;
				}
			}

			o[1] = b;

			al.add(o);
		}

		if (al.size() > 0) {
			String[] fieldnames = new String[] { "Liefergruppe",
					"IstInLiefergruppe" };
			Object[][] dataSub = new Object[al.size()][fieldnames.length];
			dataSub = (Object[][]) al.toArray(dataSub);

			return new LPDatenSubreport(dataSub, fieldnames);
		}
		session.close();
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printLieferantenliste(TheClientDto theClientDto,
			boolean bMitVersteckten, boolean bMitMoeglichen,
			boolean bMitAnsprechpartner, boolean bNurFreigegebeneLieferanten,
			Integer lieferantIIdSelektiert, String cPlz, Integer landIId,
			Integer brancheIId, Integer partnerklasseIId,
			Integer liefergruppeIId) {
		sAktuellerReport = LieferantReportFac.REPORT_LIEFERANTENLISTE;
		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session
				.createCriteria(FLRLieferant.class);
		crit.createAlias(LieferantFac.FLR_PARTNER, "p");
		crit.createAlias("p." + PartnerFac.FLR_PARTNER_FLRLANDPLZORT,
				"landplzort");
		crit.createAlias("landplzort.flrland", "land");
		if (lieferantIIdSelektiert != null) {
			crit.add(Restrictions.eq("i_id", lieferantIIdSelektiert));
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

		if (liefergruppeIId != null) {
			crit.createAlias("set_liefergruppen", "set");
			crit.createAlias("set.flrliefergruppe", "lg");
			crit.add(Restrictions.eq("lg.i_id", liefergruppeIId));
		}

		crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));
		crit.addOrder(Order.asc("p."
				+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
		if (bMitVersteckten == false) {
			crit.add(Restrictions.eq("p." + PartnerFac.FLR_PARTNER_VERSTECKT,
					Helper.boolean2Short(false)));
		}
		if (bMitMoeglichen == false) {
			crit.add(Restrictions.eq(
					LieferantFac.FLR_LIEFERANT_B_MOEGLICHERLIEFERANT,
					Helper.boolean2Short(false)));
		}

		if (bNurFreigegebeneLieferanten == true) {
			crit.add(Restrictions
					.isNotNull(LieferantFac.FLR_LIEFERANT_T_FREIGABE));
		}

		ArrayList<Object[]> daten = new ArrayList<Object[]>();
		List<?> list = crit.list();
		Iterator<?> resultListIterator = list.iterator();
		while (resultListIterator.hasNext()) {
			Object[] zeile = new Object[92];
			FLRLieferant lieferant = (FLRLieferant) resultListIterator.next();

			try {
				LieferantDto lieferantDto = getLieferantFac()
						.lieferantFindByPrimaryKey(lieferant.getI_id(),
								theClientDto);

				if (lieferantDto.getPartnerDto()
						.getLandIIdAbweichendesustland() != null) {
					zeile[REPORT_LIEFERANTENLISTE_ABW_UST_LAND] = getSystemFac()
							.landFindByPrimaryKey(
									lieferantDto.getPartnerDto()
											.getLandIIdAbweichendesustland())
							.getCLkz();
				}
				zeile[REPORT_LIEFERANTENLISTE_ANREDE] = lieferantDto
						.getPartnerDto().getAnredeCNr();

				if (lieferantDto.getPartnerDto().getBrancheIId() != null) {
					zeile[REPORT_LIEFERANTENLISTE_BRANCHE] = getPartnerServicesFac()
							.brancheFindByPrimaryKey(
									lieferantDto.getPartnerDto()
											.getBrancheIId(), theClientDto)
							.getBezeichnung();
				}

				zeile[REPORT_LIEFERANTENLISTE_LIEFERANT_I_ID] = lieferantDto
						.getIId();

				zeile[REPORT_LIEFERANTENLISTE_CNAME1] = lieferantDto
						.getPartnerDto().getCName1nachnamefirmazeile1();
				zeile[REPORT_LIEFERANTENLISTE_CNAME2] = lieferantDto
						.getPartnerDto().getCName2vornamefirmazeile2();
				zeile[REPORT_LIEFERANTENLISTE_CNAME3] = lieferantDto
						.getPartnerDto().getCName3vorname2abteilung();

				zeile[REPORT_LIEFERANTENLISTE_BEMERKUNG] = lieferantDto
						.getPartnerDto().getXBemerkung();

				zeile[REPORT_LIEFERANTENLISTE_KUNDENNUMMER] = lieferantDto
						.getCKundennr();

				if (lieferantDto.getKontoIIdWarenkonto() != null) {
					zeile[REPORT_LIEFERANTENLISTE_WARENKONTO] = getFinanzFac()
							.kontoFindByPrimaryKey(
									lieferantDto.getKontoIIdWarenkonto())
							.getCNr();
				}
				if (lieferant.getFlrkonto() != null) {
					zeile[REPORT_LIEFERANTENLISTE_KREDITORENKONTO] = lieferant
							.getFlrkonto().getC_nr();
				}

				zeile[REPORT_LIEFERANTENLISTE_EMAIL] = lieferantDto
						.getPartnerDto().getCEmail();

				zeile[REPORT_LIEFERANTENLISTE_FAX] = lieferantDto
						.getPartnerDto().getCFax();

				zeile[REPORT_LIEFERANTENLISTE_HOMEPAGE] = lieferantDto
						.getPartnerDto().getCHomepage();

				zeile[REPORT_LIEFERANTENLISTE_TELEFON] = lieferantDto
						.getPartnerDto().getCTelefon();

				zeile[REPORT_LIEFERANTENLISTE_SUBREPORT_LIEFERGRUPPEN] = getSubreportLiefergruppen(
						lieferantDto.getIId(), theClientDto);

				zeile[REPORT_LIEFERANTENLISTE_FIRMENBUCHNUMMER] = lieferantDto
						.getPartnerDto().getCFirmenbuchnr();
				zeile[REPORT_LIEFERANTENLISTE_GERICHTSSTAND] = lieferantDto
						.getPartnerDto().getCGerichtsstand();
				zeile[REPORT_LIEFERANTENLISTE_MOEGLICHERLIEFERANT] = lieferantDto
						.getBMoeglicherLieferant();
				zeile[REPORT_LIEFERANTENLISTE_KOMMUNIKATIONSSPRACHE] = lieferantDto
						.getPartnerDto().getLocaleCNrKommunikation();

				if (lieferantDto.getIIdKostenstelle() != null) {
					zeile[REPORT_LIEFERANTENLISTE_KOSTENSTELLE] = getSystemFac()
							.kostenstelleFindByPrimaryKey(
									lieferantDto.getIIdKostenstelle()).getCNr();
				}
				zeile[REPORT_LIEFERANTENLISTE_KREDITLIMIT] = lieferantDto
						.getNKredit();
				zeile[REPORT_LIEFERANTENLISTE_KURZBEZEICHNUNG] = lieferantDto
						.getPartnerDto().getCKbez();
				if (lieferantDto.getPartnerDto().getLandplzortDto() != null) {
					zeile[REPORT_LIEFERANTENLISTE_LAND] = lieferantDto
							.getPartnerDto().getLandplzortDto().getLandDto()
							.getCLkz();
					zeile[REPORT_LIEFERANTENLISTE_PLZ] = lieferantDto
							.getPartnerDto().getLandplzortDto().getCPlz();
					zeile[REPORT_LIEFERANTENLISTE_ORT] = lieferantDto
							.getPartnerDto().getLandplzortDto().getOrtDto()
							.getCName();
				}
				if (lieferantDto.getPartnerDto().getLandplzortDto_Postfach() != null) {
					zeile[REPORT_LIEFERANTENLISTE_LAND_POSTFACH] = lieferantDto
							.getPartnerDto().getLandplzortDto_Postfach()
							.getLandDto().getCLkz();
					zeile[REPORT_LIEFERANTENLISTE_PLZ_POSTFACH] = lieferantDto
							.getPartnerDto().getLandplzortDto_Postfach()
							.getCPlz();
					zeile[REPORT_LIEFERANTENLISTE_ORT_POSTFACH] = lieferantDto
							.getPartnerDto().getLandplzortDto_Postfach()
							.getOrtDto().getCName();
				}
				zeile[REPORT_LIEFERANTENLISTE_POSTFACH] = lieferantDto
						.getPartnerDto().getCPostfach();

				zeile[REPORT_LIEFERANTENLISTE_PARTNERART] = lieferantDto
						.getPartnerDto().getPartnerartCNr();

				if (lieferantDto.getPartnerDto().getPartnerklasseIId() != null) {
					zeile[REPORT_LIEFERANTENLISTE_PARTNERKLASSE] = getPartnerFac()
							.partnerklasseFindByPrimaryKey(
									lieferantDto.getPartnerDto()
											.getPartnerklasseIId(),
									theClientDto).getCNr();
				}
				zeile[REPORT_LIEFERANTENLISTE_RABATT] = lieferantDto
						.getNRabatt();

				if (lieferantDto.getPartnerRechnungsadresseDto() != null) {
					zeile[REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_ANREDE] = lieferantDto
							.getPartnerRechnungsadresseDto().getAnredeCNr();
					zeile[REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_CNAME1] = lieferantDto
							.getPartnerRechnungsadresseDto()
							.getCName1nachnamefirmazeile1();
					zeile[REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_CNAME2] = lieferantDto
							.getPartnerRechnungsadresseDto()
							.getCName2vornamefirmazeile2();
					zeile[REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_CNAME3] = lieferantDto
							.getPartnerRechnungsadresseDto()
							.getCName3vorname2abteilung();
					zeile[REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_KURZBEZEICHNUNG] = lieferantDto
							.getPartnerRechnungsadresseDto().getCKbez();
					if (lieferantDto.getPartnerRechnungsadresseDto()
							.getLandplzortDto() != null) {
						zeile[REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_LAND] = lieferantDto
								.getPartnerRechnungsadresseDto()
								.getLandplzortDto().getLandDto().getCName();
						zeile[REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_PLZ] = lieferantDto
								.getPartnerRechnungsadresseDto()
								.getLandplzortDto().getCPlz();
						zeile[REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_ORT] = lieferantDto
								.getPartnerRechnungsadresseDto()
								.getLandplzortDto().getOrtDto().getCName();
					}
					if (lieferantDto.getPartnerRechnungsadresseDto()
							.getLandplzortDto_Postfach() != null) {
						zeile[REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_LAND_POSTFACH] = lieferantDto
								.getPartnerRechnungsadresseDto()
								.getLandplzortDto_Postfach().getLandDto()
								.getCName();
						zeile[REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_PLZ_POSTFACH] = lieferantDto
								.getPartnerRechnungsadresseDto()
								.getLandplzortDto_Postfach().getCPlz();
						zeile[REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_ORT] = lieferantDto
								.getPartnerRechnungsadresseDto()
								.getLandplzortDto_Postfach().getOrtDto()
								.getCName();
					}
					zeile[REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_POSTFACH] = lieferantDto
							.getPartnerRechnungsadresseDto().getCPostfach();
					zeile[REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_STRASSE] = lieferantDto
							.getPartnerRechnungsadresseDto().getCStrasse();
					zeile[REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_TITEL] = lieferantDto
							.getPartnerRechnungsadresseDto().getCTitel();
					zeile[REPORT_LIEFERANTENLISTE_RECHNUNGSADRESSE_UIDNUMMER] = lieferantDto
							.getPartnerRechnungsadresseDto().getCUid();

				}

				// Beurteilung
				LieferantbeurteilungDto[] bDtos = getLieferantFac()
						.lieferantbeurteilungfindByLetzteBeurteilungByLieferantIId(
								lieferantDto.getIId(),
								new java.sql.Timestamp(System
										.currentTimeMillis()));

				if (bDtos != null && bDtos.length > 0) {
					zeile[REPORT_LIEFERANTENLISTE_LETZTE_BEURTEILUNG_PUNKTE] = bDtos[0]
							.getIPunkte();
					zeile[REPORT_LIEFERANTENLISTE_LETZTE_BEURTEILUNG_KLASSE] = bDtos[0]
							.getCKlasse();
					zeile[REPORT_LIEFERANTENLISTE_LETZTE_BEURTEILUNG_KOMMENTAR] = bDtos[0]
							.getCKommentar();
					zeile[REPORT_LIEFERANTENLISTE_LETZTE_BEURTEILUNG_DATUM] = bDtos[0]
							.getTDatum();

				}

				zeile[REPORT_LIEFERANTENLISTE_STRASSE] = lieferantDto
						.getPartnerDto().getCStrasse();
				zeile[REPORT_LIEFERANTENLISTE_TITEL] = lieferantDto
						.getPartnerDto().getCTitel();
				zeile[REPORT_LIEFERANTENLISTE_UIDNUMMER] = lieferantDto
						.getPartnerDto().getCUid();
				zeile[REPORT_LIEFERANTENLISTE_WAEHRUNG] = lieferantDto
						.getWaehrungCNr();

				zeile[REPORT_LIEFERANTENLISTE_BESTELLSPERRE_AM] = lieferantDto
						.getTBestellsperream();
				zeile[REPORT_LIEFERANTENLISTE_KOMMENTAR] = lieferantDto
						.getXKommentar();
				zeile[REPORT_LIEFERANTENLISTE_FREIGABE] = lieferantDto
						.getTFreigabe();
				zeile[REPORT_LIEFERANTENLISTE_FREIGABE_TEXT] = lieferantDto
						.getCFreigabe();
				zeile[REPORT_LIEFERANTENLISTE_FREIGABE_PERSONDATUM] = lieferantDto
						.getTPersonalFreigabe();

				if (lieferantDto.getPersonalIIdFreigabe() != null) {
					PersonalDto personalDto = getPersonalFac()
							.personalFindByPrimaryKey(
									lieferantDto.getPersonalIIdFreigabe(),
									theClientDto);
					zeile[REPORT_LIEFERANTENLISTE_FREIGABE_PERSON] = personalDto
							.getCKurzzeichen();

				}

				if (lieferantDto.getZahlungszielIId() != null) {
					zeile[REPORT_LIEFERANTENLISTE_ZAHLUNGSZIEL] = getMandantFac()
							.zahlungszielFindByPrimaryKey(
									lieferantDto.getZahlungszielIId(),
									theClientDto).getCBez();
				}

				if (lieferantDto.getIdSpediteur() != null) {
					zeile[REPORT_LIEFERANTENLISTE_SPEDITEUR] = getMandantFac()
							.spediteurFindByPrimaryKey(
									lieferantDto.getIdSpediteur())
							.getCNamedesspediteurs();
				}
				if (lieferantDto.getLieferartIId() != null) {
					zeile[REPORT_LIEFERANTENLISTE_LIEFERART] = getLocaleFac()
							.lieferartFindByPrimaryKey(
									lieferantDto.getLieferartIId(),
									theClientDto).formatBez();
				}
				if (lieferantDto.getMwstsatzbezIId() != null) {
					zeile[REPORT_LIEFERANTENLISTE_MWSTSATZ] = getMandantFac()
							.mwstsatzbezFindByPrimaryKey(
									lieferantDto.getMwstsatzbezIId(),
									theClientDto).getCBezeichnung();
				}

				Set<?> ansprechpartner = lieferant.getFlrpartner()
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

						for (int i = 0; i < 90; i++) {
							oKopie[i] = zeile[i];
						}

						FLRAnsprechpartner flrAnsprechpartner = (FLRAnsprechpartner) anspIt
								.next();

						oKopie[REPORT_LIEFERANTENLISTE_ANSPRECHPARTNER_ANREDE] = flrAnsprechpartner
								.getFlrpartneransprechpartner()
								.getAnrede_c_nr();

						oKopie[REPORT_LIEFERANTENLISTE_ANSPRECHPARTNER_EMAIL] = flrAnsprechpartner
								.getC_email();

						oKopie[REPORT_LIEFERANTENLISTE_ANSPRECHPARTNER_FAXDW] = flrAnsprechpartner
								.getC_fax();

						oKopie[REPORT_LIEFERANTENLISTE_ANSPRECHPARTNER_MOBIL] = flrAnsprechpartner
								.getC_handy();

						oKopie[REPORT_LIEFERANTENLISTE_ANSPRECHPARTNER_BEMERKUNG] = flrAnsprechpartner
								.getX_bemerkung();

						oKopie[REPORT_LIEFERANTENLISTE_ANSPRECHPARTNER_NACHNAME] = flrAnsprechpartner
								.getFlrpartneransprechpartner()
								.getC_name1nachnamefirmazeile1();

						oKopie[REPORT_LIEFERANTENLISTE_ANSPRECHPARTNER_TELDW] = flrAnsprechpartner
								.getC_telefon();

						oKopie[REPORT_LIEFERANTENLISTE_ANSPRECHPARTNER_TITEL] = flrAnsprechpartner
								.getFlrpartneransprechpartner().getC_titel();
						oKopie[REPORT_LIEFERANTENLISTE_ANSPRECHPARTNER_VORNAME] = flrAnsprechpartner
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
					zeile[REPORT_LIEFERANTENLISTE_BRIEFANREDE] = getPartnerServicesFac()
							.getBriefanredeFuerBeleg(null,
									lieferantDto.getPartnerIId(),
									theClientDto.getLocUi(), theClientDto);
					daten.add(zeile);
				}
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
		}
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		parameter.put("P_MITANSPRECHPARTNER", new Boolean(bMitAnsprechpartner));
		parameter.put("P_MITVERSTECKTEN", new Boolean(bMitVersteckten));
		parameter.put("P_NURFREIGEGEBENE", new Boolean(
				bNurFreigegebeneLieferanten));
		parameter.put("P_MITMOEGLICHEN", new Boolean(bMitMoeglichen));

		if (lieferantIIdSelektiert == null) {
			parameter.put("P_NURSELEKTIERTER", Boolean.TRUE);
		} else {
			parameter.put("P_NURSELEKTIERTER", Boolean.FALSE);
		}

		parameter
				.put("P_SUBREPORT_LIEFERGRUPPEN",
						(net.sf.jasperreports.engine.JRDataSource) getSubreportLiefergruppen(
								null, theClientDto));

		parameter.put("P_PLZ", cPlz);
		if (landIId != null) {
			parameter.put("P_LAND", getSystemFac()
					.landFindByPrimaryKey(landIId).getCLkz());
		}
		if (brancheIId != null) {
			parameter.put("P_BRANCHE", getPartnerServicesFac()
					.brancheFindByPrimaryKey(brancheIId, theClientDto)
					.getBezeichnung());
		}

		if (partnerklasseIId != null) {
			parameter.put(
					"P_PARTNERKLASSE",
					getPartnerFac().partnerklasseFindByPrimaryKey(
							partnerklasseIId, theClientDto).getBezeichnung());
		}
		if (liefergruppeIId != null) {
			parameter.put(
					"P_LIEFERGUPPE",
					getLieferantServicesFac().lfliefergruppeFindByPrimaryKey(
							liefergruppeIId, theClientDto).getBezeichnung());
		}

		data = new Object[daten.size()][87];

		for (int i = 0; i < daten.size(); i++) {
			data[i] = (Object[]) daten.get(i);
		}

		initJRDS(parameter, KundeReportFac.REPORT_MODUL,
				LieferantReportFac.REPORT_LIEFERANTENLISTE,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();
	}
}
