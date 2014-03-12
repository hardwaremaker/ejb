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
package com.lp.server.inserat.ejbfac;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.hibernate.Session;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerReportFac;
import com.lp.server.inserat.fastlanereader.generated.FLRInserat;
import com.lp.server.inserat.fastlanereader.generated.FLRInserater;
import com.lp.server.inserat.fastlanereader.generated.FLRInseratrechnung;
import com.lp.server.inserat.service.InseratDto;
import com.lp.server.inserat.service.InseratReportFac;
import com.lp.server.inserat.service.InseratartikelDto;
import com.lp.server.inserat.service.ReportJournalInseratDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.HelperServer;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;

@Stateless
public class InseratReportFacBean extends LPReport implements InseratReportFac {

	private String sAktuellerReport = null;
	private Object[][] data = null;

	public final static int REPORT_ZUVERRECHNEN_BELEGNUMMER = 0;
	public final static int REPORT_ZUVERRECHNEN_SUBREPORT_EINGANGSRECHNUNG = 1;
	public final static int REPORT_ZUVERRECHNEN_SUBREPORT_KUNDEN = 2;
	public final static int REPORT_ZUVERRECHNEN_BESTELLNUMMER = 3;
	public final static int REPORT_ZUVERRECHNEN_DATUM_ERSCHIENEN = 4;
	public final static int REPORT_ZUVERRECHNEN_PERSON_ERSCHIENEN = 5;
	public final static int REPORT_ZUVERRECHNEN_RUBRIK = 6;
	public final static int REPORT_ZUVERRECHNEN_RUBRIK2 = 7;
	public final static int REPORT_ZUVERRECHNEN_STICHWORT = 8;
	public final static int REPORT_ZUVERRECHNEN_STICHWORT2 = 9;
	public final static int REPORT_ZUVERRECHNEN_BEZEICHNUNG = 10;
	public final static int REPORT_ZUVERRECHNEN_BELEGDATUM = 11;
	public final static int REPORT_ZUVERRECHNEN_TERMIN = 12;
	public final static int REPORT_ZUVERRECHNEN_MEDIUM = 13;
	public final static int REPORT_ZUVERRECHNEN_LIEFERANT = 14;
	public final static int REPORT_ZUVERRECHNEN_ARTIKEL = 15;
	public final static int REPORT_ZUVERRECHNEN_MENGE = 16;
	public final static int REPORT_ZUVERRECHNEN_PREIS_EK = 17;
	public final static int REPORT_ZUVERRECHNEN_PREIS_VK = 18;
	public final static int REPORT_ZUVERRECHNEN_STATUS = 19;
	public final static int REPORT_ZUVERRECHNEN_VERTRETER = 20;
	public final static int REPORT_ZUVERRECHNEN_ANHANG_LF = 21;
	public final static int REPORT_ZUVERRECHNEN_ANHANG_KD = 22;
	public final static int REPORT_ZUVERRECHNEN_DRUCK_BESTELLUNG_LF = 23;
	public final static int REPORT_ZUVERRECHNEN_DRUCK_BESTELLUNG_KD = 24;
	public final static int REPORT_ZUVERRECHNEN_DRUCK_RECHNUNG_KD = 25;
	public final static int REPORT_ZUVERRECHNEN_SUBREPORT_ARTIKEL = 26;
	public final static int REPORT_ZUVERRECHNEN_GRUND_GESTOPPT = 27;
	public final static int REPORT_ZUVERRECHNEN_PERSON_VERRECHENBAR = 28;
	public final static int REPORT_ZUVERRECHNEN_PERSON_GESTOPPT = 29;
	public final static int REPORT_ZUVERRECHNEN_ANZAHL_SPALTEN = 30;

	public final static int REPORT_ALLE_BELEGNUMMER = 0;
	public final static int REPORT_ALLE_SUBREPORT_EINGANGSRECHNUNG = 1;
	public final static int REPORT_ALLE_SUBREPORT_KUNDEN = 2;
	public final static int REPORT_ALLE_BESTELLNUMMER = 3;
	public final static int REPORT_ALLE_DATUM_ERSCHIENEN = 4;
	public final static int REPORT_ALLE_PERSON_ERSCHIENEN = 5;
	public final static int REPORT_ALLE_RUBRIK = 6;
	public final static int REPORT_ALLE_RUBRIK2 = 7;
	public final static int REPORT_ALLE_STICHWORT = 8;
	public final static int REPORT_ALLE_STICHWORT2 = 9;
	public final static int REPORT_ALLE_BEZEICHNUNG = 10;
	public final static int REPORT_ALLE_BELEGDATUM = 11;
	public final static int REPORT_ALLE_TERMIN = 12;
	public final static int REPORT_ALLE_MEDIUM = 13;
	public final static int REPORT_ALLE_LIEFERANT = 14;
	public final static int REPORT_ALLE_ARTIKEL = 15;
	public final static int REPORT_ALLE_MENGE = 16;
	public final static int REPORT_ALLE_PREIS_EK = 17;
	public final static int REPORT_ALLE_PREIS_VK = 18;
	public final static int REPORT_ALLE_STATUS = 19;
	public final static int REPORT_ALLE_VERTRETER = 20;
	public final static int REPORT_ALLE_ANHANG_LF = 21;
	public final static int REPORT_ALLE_ANHANG_KD = 22;
	public final static int REPORT_ALLE_DRUCK_BESTELLUNG_LF = 23;
	public final static int REPORT_ALLE_DRUCK_BESTELLUNG_KD = 24;
	public final static int REPORT_ALLE_DRUCK_RECHNUNG_KD = 25;
	public final static int REPORT_ALLE_SUBREPORT_ARTIKEL = 26;
	public final static int REPORT_ALLE_GRUND_GESTOPPT = 27;
	public final static int REPORT_ALLE_PERSON_VERRECHENBAR = 28;
	public final static int REPORT_ALLE_PERSON_GESTOPPT = 29;
	public final static int REPORT_ALLE_ANZAHL_SPALTEN = 30;

	public final static int REPORT_DBAUSWERTUNG_BELEGNUMMER = 0;
	public final static int REPORT_DBAUSWERTUNG_SUBREPORT_EINGANGSRECHNUNG = 1;
	public final static int REPORT_DBAUSWERTUNG_SUBREPORT_KUNDEN = 2;
	public final static int REPORT_DBAUSWERTUNG_BESTELLNUMMER = 3;
	public final static int REPORT_DBAUSWERTUNG_DATUM_ERSCHIENEN = 4;
	public final static int REPORT_DBAUSWERTUNG_PERSON_ERSCHIENEN = 5;
	public final static int REPORT_DBAUSWERTUNG_RUBRIK = 6;
	public final static int REPORT_DBAUSWERTUNG_RUBRIK2 = 7;
	public final static int REPORT_DBAUSWERTUNG_STICHWORT = 8;
	public final static int REPORT_DBAUSWERTUNG_STICHWORT2 = 9;
	public final static int REPORT_DBAUSWERTUNG_BEZEICHNUNG = 10;
	public final static int REPORT_DBAUSWERTUNG_BELEGDATUM = 11;
	public final static int REPORT_DBAUSWERTUNG_TERMIN = 12;
	public final static int REPORT_DBAUSWERTUNG_MEDIUM = 13;
	public final static int REPORT_DBAUSWERTUNG_LIEFERANT = 14;
	public final static int REPORT_DBAUSWERTUNG_ARTIKEL = 15;
	public final static int REPORT_DBAUSWERTUNG_MENGE = 16;
	public final static int REPORT_DBAUSWERTUNG_PREIS_EK = 17;
	public final static int REPORT_DBAUSWERTUNG_PREIS_VK = 18;
	public final static int REPORT_DBAUSWERTUNG_STATUS = 19;
	public final static int REPORT_DBAUSWERTUNG_VERTRETER = 20;
	public final static int REPORT_DBAUSWERTUNG_ANHANG_LF = 21;
	public final static int REPORT_DBAUSWERTUNG_ANHANG_KD = 22;
	public final static int REPORT_DBAUSWERTUNG_DRUCK_BESTELLUNG_LF = 23;
	public final static int REPORT_DBAUSWERTUNG_DRUCK_BESTELLUNG_KD = 24;
	public final static int REPORT_DBAUSWERTUNG_DRUCK_RECHNUNG_KD = 25;
	public final static int REPORT_DBAUSWERTUNG_SUBREPORT_ARTIKEL = 26;
	public final static int REPORT_DBAUSWERTUNG_GRUND_GESTOPPT = 27;
	public final static int REPORT_DBAUSWERTUNG_PERSON_VERRECHENBAR = 28;
	public final static int REPORT_DBAUSWERTUNG_PERSON_GESTOPPT = 29;
	public final static int REPORT_DBAUSWERTUNG_VOLLSTAENDIG_BEZAHLT = 30;
	public final static int REPORT_DBAUSWERTUNG_VOLLSTAENDIG_VERRECHNET = 31;
	public final static int REPORT_DBAUSWERTUNG_KUNDE = 32;
	public final static int REPORT_DBAUSWERTUNG_RECHNUNG_NUMMER = 33;
	public final static int REPORT_DBAUSWERTUNG_RECHNUNG_STATUS = 34;
	public final static int REPORT_DBAUSWERTUNG_RECHNUNGSDATUM = 35;
	public final static int REPORT_DBAUSWERTUNG_RECHNUNG_ZAHLUNGSZIEL = 36;
	public final static int REPORT_DBAUSWERTUNG_RECHNUNGBEZAHLT_BETRAG = 37;
	public final static int REPORT_DBAUSWERTUNG_RECHNUNG_LETZTES_ZAHLDATUM = 38;
	public final static int REPORT_DBAUSWERTUNG_ERRECHNETERWERT_EK = 39;
	public final static int REPORT_DBAUSWERTUNG_ERRECHNETERWERT_VK = 40;
	public final static int REPORT_DBAUSWERTUNG_MANUELL_ERLEDIGT = 41;
	public final static int REPORT_DBAUSWERTUNG_ANZAHL_SPALTEN = 42;

	public final static int REPORT_OFFENE_BELEGNUMMER = 0;
	public final static int REPORT_OFFENE_SUBREPORT_EINGANGSRECHNUNG = 1;
	public final static int REPORT_OFFENE_SUBREPORT_KUNDEN = 2;
	public final static int REPORT_OFFENE_BESTELLNUMMER = 3;
	public final static int REPORT_OFFENE_DATUM_ERSCHIENEN = 4;
	public final static int REPORT_OFFENE_PERSON_ERSCHIENEN = 5;
	public final static int REPORT_OFFENE_RUBRIK = 6;
	public final static int REPORT_OFFENE_RUBRIK2 = 7;
	public final static int REPORT_OFFENE_STICHWORT = 8;
	public final static int REPORT_OFFENE_STICHWORT2 = 9;
	public final static int REPORT_OFFENE_BEZEICHNUNG = 10;
	public final static int REPORT_OFFENE_BELEGDATUM = 11;
	public final static int REPORT_OFFENE_TERMIN = 12;
	public final static int REPORT_OFFENE_MEDIUM = 13;
	public final static int REPORT_OFFENE_LIEFERANT = 14;
	public final static int REPORT_OFFENE_ARTIKEL = 15;
	public final static int REPORT_OFFENE_MENGE = 16;
	public final static int REPORT_OFFENE_PREIS_EK = 17;
	public final static int REPORT_OFFENE_PREIS_VK = 18;
	public final static int REPORT_OFFENE_STATUS = 19;
	public final static int REPORT_OFFENE_VERTRETER = 20;
	public final static int REPORT_OFFENE_ANHANG_LF = 21;
	public final static int REPORT_OFFENE_ANHANG_KD = 22;
	public final static int REPORT_OFFENE_DRUCK_BESTELLUNG_LF = 23;
	public final static int REPORT_OFFENE_DRUCK_BESTELLUNG_KD = 24;
	public final static int REPORT_OFFENE_DRUCK_RECHNUNG_KD = 25;
	public final static int REPORT_OFFENE_SUBREPORT_ARTIKEL = 26;
	public final static int REPORT_OFFENE_GRUND_GESTOPPT = 27;
	public final static int REPORT_OFFENE_PERSON_VERRECHENBAR = 28;
	public final static int REPORT_OFFENE_PERSON_GESTOPPT = 29;
	public final static int REPORT_OFFENE_ANZAHL_SPALTEN = 30;

	public final static int REPORT_INSERAT_ARTIKEL = 0;
	public final static int REPORT_INSERAT_BEZEICHNUNG = 1;
	public final static int REPORT_INSERAT_ZUSATZBEZEICHNUNG = 2;
	public final static int REPORT_INSERAT_ZUSATZBEZEICHNUNG2 = 3;
	public final static int REPORT_INSERAT_KURZBEZEICHNUNG = 4;
	public final static int REPORT_INSERAT_EINHEIT = 5;
	public final static int REPORT_INSERAT_MENGE = 6;
	public final static int REPORT_INSERAT_NETTOEINZELPREIS_EK = 7;
	public final static int REPORT_INSERAT_NETTOEINZELPREIS_VK = 8;
	public final static int REPORT_INSERAT_ANZAHL_SPALTEN = 9;

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printZuverrechnen(TheClientDto theClientDto) {

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		sAktuellerReport = InseratReportFac.REPORT_ZUVERRECHNEN;

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT inserat FROM FLRInserat inserat   LEFT OUTER JOIN inserat.rechnungset as rechnungset WHERE rechnungset.i_sort=1 AND  rechnungset.rechnungposition_i_id IS NULL AND inserat.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' AND inserat.status_c_nr in ('"
				+ LocaleFac.STATUS_VERRECHENBAR
				+ "','"
				+ LocaleFac.STATUS_GESTOPPT + "') ORDER BY inserat.c_nr ASC";

		org.hibernate.Query query = session.createQuery(queryString);
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		ArrayList alDaten = new ArrayList();
		while (resultListIterator.hasNext()) {
			FLRInserat flrInserat = (FLRInserat) resultListIterator.next();

			InseratDto inseratDto = getInseratFac().inseratFindByPrimaryKey(
					flrInserat.getI_id());

			Object[] oZeile = new Object[REPORT_ZUVERRECHNEN_ANZAHL_SPALTEN];

			oZeile[REPORT_ZUVERRECHNEN_BELEGNUMMER] = flrInserat.getC_nr();
			oZeile[REPORT_ZUVERRECHNEN_DATUM_ERSCHIENEN] = flrInserat
					.getT_erschienen();
			oZeile[REPORT_ZUVERRECHNEN_BELEGDATUM] = flrInserat
					.getT_belegdatum();
			oZeile[REPORT_ZUVERRECHNEN_TERMIN] = flrInserat.getT_termin();
			oZeile[REPORT_ZUVERRECHNEN_RUBRIK] = flrInserat.getC_rubrik();
			oZeile[REPORT_ZUVERRECHNEN_RUBRIK2] = flrInserat.getC_rubrik2();

			oZeile[REPORT_ZUVERRECHNEN_RUBRIK2] = flrInserat.getC_rubrik2();
			oZeile[REPORT_ZUVERRECHNEN_RUBRIK2] = flrInserat.getC_rubrik2();
			oZeile[REPORT_ZUVERRECHNEN_RUBRIK2] = flrInserat.getC_rubrik2();
			oZeile[REPORT_ZUVERRECHNEN_STICHWORT] = flrInserat.getC_stichwort();
			oZeile[REPORT_ZUVERRECHNEN_STICHWORT2] = flrInserat
					.getC_stichwort2();
			oZeile[REPORT_ZUVERRECHNEN_STATUS] = flrInserat.getStatus_c_nr();

			oZeile[REPORT_ZUVERRECHNEN_ANHANG_LF] = inseratDto.getXAnhangLf();
			oZeile[REPORT_ZUVERRECHNEN_ANHANG_KD] = inseratDto.getXAnhang();

			oZeile[REPORT_ZUVERRECHNEN_MEDIUM] = inseratDto.getCMedium();
			oZeile[REPORT_ZUVERRECHNEN_BEZEICHNUNG] = flrInserat.getC_bez();
			oZeile[REPORT_ZUVERRECHNEN_MENGE] = flrInserat.getN_menge();
			oZeile[REPORT_ZUVERRECHNEN_ARTIKEL] = flrInserat
					.getFlrartikel_inseratart().getC_nr();
			oZeile[REPORT_ZUVERRECHNEN_PREIS_EK] = flrInserat
					.getN_nettoeinzelpreis_ek();
			oZeile[REPORT_ZUVERRECHNEN_PREIS_VK] = flrInserat
					.getN_nettoeinzelpreis_vk();

			oZeile[REPORT_ZUVERRECHNEN_LIEFERANT] = flrInserat
					.getFlrlieferant().getFlrpartner()
					.getC_name1nachnamefirmazeile1();
			oZeile[REPORT_ZUVERRECHNEN_VERTRETER] = HelperServer
					.formatNameAusFLRPartner(flrInserat.getFlrvertreter()
							.getFlrpartner());

			if (flrInserat.getFlrbestellposition() != null) {
				oZeile[REPORT_ZUVERRECHNEN_BESTELLNUMMER] = flrInserat
						.getFlrbestellposition().getFlrbestellung().getC_nr();
			}
			if (inseratDto.getPersonalIIdErschienen() != null) {
				oZeile[REPORT_ZUVERRECHNEN_PERSON_ERSCHIENEN] = getPersonalFac()
						.personalFindByPrimaryKey(
								inseratDto.getPersonalIIdErschienen(),
								theClientDto).formatAnrede();
			}

			if (inseratDto.getPersonalIIdVerrechnen() != null) {
				oZeile[REPORT_ZUVERRECHNEN_PERSON_VERRECHENBAR] = getPersonalFac()
						.personalFindByPrimaryKey(
								inseratDto.getPersonalIIdVerrechnen(),
								theClientDto).formatAnrede();
			}
			if (inseratDto.getPersonalIIdGestoppt() != null) {
				oZeile[REPORT_ZUVERRECHNEN_PERSON_GESTOPPT] = getPersonalFac()
						.personalFindByPrimaryKey(
								inseratDto.getPersonalIIdGestoppt(),
								theClientDto).formatAnrede();
			}
			oZeile[REPORT_ZUVERRECHNEN_GRUND_GESTOPPT] = inseratDto
					.getCGestoppt();

			oZeile[REPORT_ZUVERRECHNEN_DRUCK_BESTELLUNG_KD] = Helper
					.short2Boolean(inseratDto.getBDruckBestellungKd());
			oZeile[REPORT_ZUVERRECHNEN_DRUCK_BESTELLUNG_LF] = Helper
					.short2Boolean(inseratDto.getBDruckBestellungLf());
			oZeile[REPORT_ZUVERRECHNEN_DRUCK_RECHNUNG_KD] = Helper
					.short2Boolean(inseratDto.getBDruckRechnungKd());

			oZeile[REPORT_ZUVERRECHNEN_SUBREPORT_KUNDEN] = getSubreportKunde(
					flrInserat, theClientDto);

			String[] fieldnamesArtikel = new String[] { "Artikel",
					"Bezeichnung", "Menge", "PreisVK", "PreisEK" };

			InseratartikelDto[] iaDtos = getInseratFac()
					.inseratartikelFindByInseratIId(flrInserat.getI_id());

			Object[][] dataSubArtikel = new Object[iaDtos.length][fieldnamesArtikel.length];

			for (int i = 0; i < iaDtos.length; i++) {
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(
								iaDtos[i].getArtikelIId(), theClientDto);
				dataSubArtikel[i][0] = artikelDto.getCNr();
				dataSubArtikel[i][1] = artikelDto.formatBezeichnung();
				dataSubArtikel[i][2] = iaDtos[i].getNMenge();
				dataSubArtikel[i][3] = iaDtos[i].getNNettoeinzelpreisVk();
				dataSubArtikel[i][4] = iaDtos[i].getNNettoeinzelpreisEk();

			}

			oZeile[REPORT_ZUVERRECHNEN_SUBREPORT_ARTIKEL] = new LPDatenSubreport(
					dataSubArtikel, fieldnamesArtikel);

			String[] fieldnamesER = new String[] { "Eingangsrechnung" };
			Object[][] dataSub = new Object[flrInserat.getErset().size()][fieldnamesER.length];

			Iterator iterEr = flrInserat.getErset().iterator();
			int u = 0;
			while (iterEr.hasNext()) {
				FLRInserater inseratEr = (FLRInserater) iterEr.next();
				dataSub[u][0] = inseratEr.getFlreingangsrechnung().getC_nr();
				u++;
			}

			oZeile[REPORT_ZUVERRECHNEN_SUBREPORT_EINGANGSRECHNUNG] = new LPDatenSubreport(
					dataSub, fieldnamesER);

			alDaten.add(oZeile);

		}
		session.close();
		Object[][] returnArray = new Object[alDaten.size()][REPORT_ZUVERRECHNEN_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(returnArray);

		initJRDS(parameter, InseratReportFac.REPORT_MODUL,
				InseratReportFac.REPORT_ZUVERRECHNEN,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP[] printInserat(Integer inseratIId, Boolean bMitLogo,
			Integer iAnzahlKopien, TheClientDto theClientDto) {
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		sAktuellerReport = InseratReportFac.REPORT_INSERAT;
		MandantDto mandantDto = null;
		try {
			mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
			parameter.put(P_MANDANTADRESSE,
					Helper.formatMandantAdresse(mandantDto));
			parameter.put("P_BELEGWAEHRUNG",
					theClientDto.getSMandantenwaehrung());
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		InseratDto inseratDto = getInseratFac().inseratFindByPrimaryKey(
				inseratIId);

		parameter.put("P_BELEGNUMMER", inseratDto.getCNr());
		parameter.put("P_DATUM_ERSCHIENEN", inseratDto.getTErschienen());
		parameter.put("P_DATUM_GESTOPPT", inseratDto.getTGestoppt());
		parameter.put("P_BELEGDATUM", inseratDto.getTBelegdatum());
		parameter.put("P_TERMIN", inseratDto.getTTermin());
		parameter.put("P_RUBRIK", inseratDto.getCRubrik());
		parameter.put("P_RUBRIK2", inseratDto.getCRubrik2());
		parameter.put("P_STICHWORT", inseratDto.getCStichwort());
		parameter.put("P_STICHWORT2", inseratDto.getCStichwort2());
		parameter.put("P_STATUS", inseratDto.getStatusCNr());
		parameter.put("P_ANHANG_LF", inseratDto.getXAnhangLf());
		parameter.put("P_ANHANG_KD", inseratDto.getXAnhang());

		parameter.put("P_MEDIUM", inseratDto.getCMedium());
		parameter.put("P_BEZEICHNUNG", inseratDto.getCBez());
		parameter.put("P_MENGE", inseratDto.getNMenge());

		// Unser Zeichen
		PersonalDto oPersonalBenutzer = getPersonalFac()
				.personalFindByPrimaryKey(theClientDto.getIDPersonal(),
						theClientDto);
		PersonalDto oPersonalAnleger;
		// wenn Anleger und Ausdrucker identisch sind, erspar ich mir einen
		// DB-Zugriff.
		if (theClientDto.getIDPersonal().equals(
				inseratDto.getPersonalIIdAnlegen())) {
			oPersonalAnleger = oPersonalBenutzer;
		} else {
			oPersonalAnleger = getPersonalFac().personalFindByPrimaryKey(
					inseratDto.getPersonalIIdAnlegen(), theClientDto);
		}
		parameter.put("P_UNSERZEICHEN", Helper.getKurzzeichenkombi(
				oPersonalBenutzer.getCKurzzeichen(),
				oPersonalAnleger.getCKurzzeichen()));

		ArtikelDto artikelDtoInseratartikel = getArtikelFac()
				.artikelFindByPrimaryKeySmall(
						inseratDto.getArtikelIIdInseratart(), theClientDto);

		parameter.put("P_ARTIKEL", artikelDtoInseratartikel.getCNr());

		if (artikelDtoInseratartikel.getArtikelsprDto() != null) {
			parameter.put("P_ARTIKELBEZEICHNUNG", artikelDtoInseratartikel
					.getArtikelsprDto().getCBez());
			parameter.put("P_ARTIKELZUSATZBEZEICHNUNG",
					artikelDtoInseratartikel.getArtikelsprDto().getCZbez());
			parameter.put("P_ARTIKELZUSATZBEZEICHNUNG2",
					artikelDtoInseratartikel.getArtikelsprDto().getCZbez2());

		}

		parameter.put("P_NETTOEINZELPREIS_EK",
				inseratDto.getNNettoeinzelpreisEk());
		parameter.put("P_NETTOEINZELPREIS_VK",
				inseratDto.getNNettoeinzelpreisVk());

		parameter.put("P_NACHLASS_KD", inseratDto.getFKdNachlass());
		parameter.put("P_NACHLASS_LF", inseratDto.getFLfNachlass());
		parameter.put("P_RABATT_KD", inseratDto.getFKdRabatt());
		parameter.put("P_RABATT_LF", inseratDto.getFLFRabatt());
		parameter.put("P_ZUSATZRABATT_KD", inseratDto.getFKdZusatzrabatt());
		parameter.put("P_ZUSATZRABATT_LF", inseratDto.getFLfZusatzrabatt());

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT inserat FROM FLRInserat inserat  WHERE inserat.i_id="
				+ inseratDto.getIId();

		org.hibernate.Query query = session.createQuery(queryString);
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		FLRInserat flrInserat = (FLRInserat) resultListIterator.next();

		parameter.put("P_LIEFERANT", HelperServer
				.formatNameAusFLRPartner(flrInserat.getFlrlieferant()
						.getFlrpartner()));
		if (flrInserat.getFlrlieferant().getFlrpartner().getFlrlandplzort() != null) {
			parameter
					.put("P_LIEFERANT_LKZ", flrInserat.getFlrlieferant()
							.getFlrpartner().getFlrlandplzort().getFlrland()
							.getC_lkz());
			parameter.put("P_LIEFERANT_PLZ", flrInserat.getFlrlieferant()
					.getFlrpartner().getFlrlandplzort().getC_plz());
			parameter
					.put("P_LIEFERANT_ORT", flrInserat.getFlrlieferant()
							.getFlrpartner().getFlrlandplzort().getFlrort()
							.getC_name());

			if (mandantDto.getPartnerDto().getLandplzortDto() != null
					&& !mandantDto
							.getPartnerDto()
							.getLandplzortDto()
							.getLandDto()
							.getIID()
							.equals(flrInserat.getFlrlieferant()
									.getFlrpartner().getFlrlandplzort()
									.getFlrland().getI_id())) {
				parameter.put("P_LIEFERANT_LAND_WENN_NICHTINLAND", flrInserat
						.getFlrlieferant().getFlrpartner().getFlrlandplzort()
						.getFlrland().getC_name());

			}

		}

		parameter.put("P_LIEFERANT_STRASSE", flrInserat.getFlrlieferant()
				.getFlrpartner().getC_strasse());

		parameter.put("P_VERTRETER", HelperServer
				.formatNameAusFLRPartner(flrInserat.getFlrvertreter()
						.getFlrpartner()));

		if (flrInserat.getFlrbestellposition() != null) {
			parameter.put("P_BESTELLUNG", flrInserat.getFlrbestellposition()
					.getFlrbestellung().getC_nr());
		}
		if (inseratDto.getPersonalIIdErschienen() != null) {
			parameter.put(
					"P_PERSON_ERSCHIENEN",
					getPersonalFac()
							.personalFindByPrimaryKey(
									inseratDto.getPersonalIIdErschienen(),
									theClientDto).formatAnrede());
		}

		if (inseratDto.getPersonalIIdVerrechnen() != null) {
			parameter.put(
					"P_PERSON_VERRECHENBAR",
					getPersonalFac()
							.personalFindByPrimaryKey(
									inseratDto.getPersonalIIdVerrechnen(),
									theClientDto).formatAnrede());
		}
		if (inseratDto.getPersonalIIdGestoppt() != null) {
			parameter.put(
					"P_PERSON_GESTOPPT",
					getPersonalFac().personalFindByPrimaryKey(
							inseratDto.getPersonalIIdGestoppt(), theClientDto)
							.formatAnrede());
		}
		parameter.put("P_GRUND_GESTOPPT", inseratDto.getCGestoppt());

		parameter.put("P_DRUCK_BESTELLUNG_KD",
				Helper.short2Boolean(inseratDto.getBDruckBestellungKd()));

		parameter.put("P_DRUCK_BESTELLUNG_LF",
				Helper.short2Boolean(inseratDto.getBDruckBestellungLf()));
		parameter.put("P_DRUCK_RECHNUNG_KD",
				Helper.short2Boolean(inseratDto.getBDruckRechnungKd()));

		parameter.put("P_WERTAUFTEILEN",
				Helper.short2Boolean(inseratDto.getBWertaufteilen()));

		parameter.put(
				"P_SUBREPORT_KUNDEN",
				getSubreportKundeFuerInseratDruck(flrInserat, theClientDto,
						mandantDto));

		InseratartikelDto[] iaDtos = getInseratFac()
				.inseratartikelFindByInseratIId(flrInserat.getI_id());

		ArrayList alDaten = new ArrayList();

		for (int i = 0; i < iaDtos.length; i++) {
			Object[] zeileArtikel = new Object[REPORT_INSERAT_ANZAHL_SPALTEN];
			ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(iaDtos[i].getArtikelIId(),
							theClientDto);
			zeileArtikel[REPORT_INSERAT_ARTIKEL] = artikelDto.getCNr();
			if (artikelDto.getArtikelsprDto() != null) {
				zeileArtikel[REPORT_INSERAT_BEZEICHNUNG] = artikelDto
						.getArtikelsprDto().getCBez();
				zeileArtikel[REPORT_INSERAT_ZUSATZBEZEICHNUNG] = artikelDto
						.getArtikelsprDto().getCZbez();
				zeileArtikel[REPORT_INSERAT_ZUSATZBEZEICHNUNG2] = artikelDto
						.getArtikelsprDto().getCZbez2();
				zeileArtikel[REPORT_INSERAT_KURZBEZEICHNUNG] = artikelDto
						.getArtikelsprDto().getCKbez();
			}

			zeileArtikel[REPORT_INSERAT_MENGE] = iaDtos[i].getNMenge();
			zeileArtikel[REPORT_INSERAT_EINHEIT] = artikelDto.getEinheitCNr();
			zeileArtikel[REPORT_INSERAT_NETTOEINZELPREIS_VK] = iaDtos[i]
					.getNNettoeinzelpreisVk();
			zeileArtikel[REPORT_INSERAT_NETTOEINZELPREIS_EK] = iaDtos[i]
					.getNNettoeinzelpreisEk();

			alDaten.add(zeileArtikel);

		}

		if (alDaten.size() == 0) {
			parameter.put("P_KEINE_DETAILS", Boolean.TRUE);
		} else {
			parameter.put("P_KEINE_DETAILS", Boolean.FALSE);
		}

		session.close();
		Object[][] returnArray = new Object[alDaten.size()][REPORT_ZUVERRECHNEN_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(returnArray);

		int iExemplare;
		if (iAnzahlKopien == null || iAnzahlKopien.intValue() <= 0) {
			iExemplare = 1;
		} else {
			iExemplare = 1 + iAnzahlKopien.intValue();
		}

		JasperPrintLP[] prints = new JasperPrintLP[iExemplare];

		for (int iKopieNummer = 0; iKopieNummer < iExemplare; iKopieNummer++) {

			// Index zuruecksetzen
			index = -1;
			initJRDS(parameter, InseratReportFac.REPORT_MODUL,
					InseratReportFac.REPORT_INSERAT, theClientDto.getMandant(),
					theClientDto.getLocUi(), theClientDto,
					bMitLogo.booleanValue(), null);
			prints[iKopieNummer] = getReportPrint();
		}

		return prints;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAlle(ReportJournalInseratDto krit,
			TheClientDto theClientDto) {

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		sAktuellerReport = InseratReportFac.REPORT_INSERATE_ALLE;

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT inserat FROM FLRInserat inserat   LEFT OUTER JOIN inserat.rechnungset as rechnungset WHERE rechnungset.i_sort=1 AND inserat.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' AND inserat.status_c_nr not in ('"
				+ LocaleFac.STATUS_STORNIERT + "') ";

		if (krit.personalIId != null) {
			queryString += " AND inserat.flrvertreter.i_id=" + krit.personalIId;
			parameter.put("P_VERTRETER", getPersonalFac()
					.personalFindByPrimaryKey(krit.personalIId, theClientDto)
					.formatAnrede());
		}
		if (krit.lieferantIId != null) {
			queryString += " AND inserat.flrlieferant.i_id="
					+ krit.lieferantIId;
			parameter.put("P_LIEFERANT", getLieferantFac()
					.lieferantFindByPrimaryKey(krit.lieferantIId, theClientDto)
					.getPartnerDto().formatFixTitelName1Name2());
		}
		if (krit.kundeIId != null) {
			queryString += " AND rechnungset.flrkunde.i_id=" + krit.kundeIId;
			parameter.put(
					"P_KUNDE",
					getKundeFac()
							.kundeFindByPrimaryKey(krit.kundeIId, theClientDto)
							.getPartnerDto().formatFixTitelName1Name2());
		}

		// Datum von/bis

		if (krit.dVon != null) {
			queryString += " AND inserat.t_belegdatum>='"
					+ Helper.formatDateWithSlashes(krit.dVon) + "' ";

			parameter.put("P_VON", krit.dVon);

		}
		if (krit.dBis != null) {
			queryString += "AND inserat.t_belegdatum<='"
					+ Helper.formatDateWithSlashes(krit.dBis) + "' ";
			parameter.put("P_BIS", krit.dBis);
		}

		// Belegnummer von/bis
		LpBelegnummerFormat f = getBelegnummerGeneratorObj()
				.getBelegnummernFormat(theClientDto.getMandant());
		Integer iGeschaeftsjahr = null;
		String sMandantKuerzel = null;
		try {
			iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(
					theClientDto.getMandant());
			sMandantKuerzel = getParameterFac().getMandantparameter(
					theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_BELEGNUMMER_MANDANTKENNUNG)
					.getCWert();
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		if (krit.sBelegnummerVon != null) {
			String sVon = HelperServer
					.getBelegnummernFilterForHibernateCriterias(f,
							iGeschaeftsjahr, sMandantKuerzel,
							krit.sBelegnummerVon);

			queryString += " AND inserat.c_nr>='" + sVon + "' ";
			parameter.put("P_BELEGNUMMER_VON", sVon);
		}
		if (krit.sBelegnummerBis != null) {
			String sBis = HelperServer
					.getBelegnummernFilterForHibernateCriterias(f,
							iGeschaeftsjahr, sMandantKuerzel,
							krit.sBelegnummerBis);
			queryString += " AND inserat.c_nr<='" + sBis + "' ";
			parameter.put("P_BELEGNUMMER_BIS", sBis);

		}

		// Sortierung

		if (krit.iSortierung == ReportJournalInseratDto.KRIT_SORT_NACH_BELEGNUMMER) {
			queryString += " ORDER BY inserat.c_nr ASC";

			parameter
					.put("P_SORTIERUNG",
							getTextRespectUISpr("bes.belegnummer",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));

		} else if (krit.iSortierung == ReportJournalInseratDto.KRIT_SORT_NACH_KUNDE) {
			queryString += " ORDER BY rechnungset.flrkunde.flrpartner.c_name1nachnamefirmazeile1 ASC, inserat.c_nr ASC";
			parameter.put(
					"P_SORTIERUNG",
					getTextRespectUISpr("lp.kunde", theClientDto.getMandant(),
							theClientDto.getLocUi()));
		} else if (krit.iSortierung == ReportJournalInseratDto.KRIT_SORT_NACH_LIEFERANT) {
			queryString += " ORDER BY inserat.flrlieferant.flrpartner.c_name1nachnamefirmazeile1 ASC, inserat.c_nr ASC";
			parameter
					.put("P_SORTIERUNG",
							getTextRespectUISpr("lp.lieferant",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
		} else if (krit.iSortierung == ReportJournalInseratDto.KRIT_SORT_NACH_VERTRETER) {
			queryString += " ORDER BY inserat.flrvertreter.flrpartner.c_name1nachnamefirmazeile1 ASC, inserat.c_nr ASC";
			parameter
					.put("P_SORTIERUNG",
							getTextRespectUISpr("lp.vertreter",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
		}
		org.hibernate.Query query = session.createQuery(queryString);
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		ArrayList alDaten = new ArrayList();
		while (resultListIterator.hasNext()) {
			FLRInserat flrInserat = (FLRInserat) resultListIterator.next();

			InseratDto inseratDto = getInseratFac().inseratFindByPrimaryKey(
					flrInserat.getI_id());

			Object[] oZeile = new Object[REPORT_ALLE_ANZAHL_SPALTEN];

			oZeile[REPORT_ALLE_BELEGNUMMER] = flrInserat.getC_nr();
			oZeile[REPORT_ALLE_DATUM_ERSCHIENEN] = flrInserat.getT_erschienen();
			oZeile[REPORT_ALLE_BELEGDATUM] = flrInserat.getT_belegdatum();
			oZeile[REPORT_ALLE_TERMIN] = flrInserat.getT_termin();
			oZeile[REPORT_ALLE_RUBRIK] = flrInserat.getC_rubrik();
			oZeile[REPORT_ALLE_RUBRIK2] = flrInserat.getC_rubrik2();

			oZeile[REPORT_ALLE_RUBRIK2] = flrInserat.getC_rubrik2();
			oZeile[REPORT_ALLE_RUBRIK2] = flrInserat.getC_rubrik2();
			oZeile[REPORT_ALLE_RUBRIK2] = flrInserat.getC_rubrik2();
			oZeile[REPORT_ALLE_STICHWORT] = flrInserat.getC_stichwort();
			oZeile[REPORT_ALLE_STICHWORT2] = flrInserat.getC_stichwort2();
			oZeile[REPORT_ALLE_STATUS] = flrInserat.getStatus_c_nr();

			oZeile[REPORT_ALLE_ANHANG_LF] = inseratDto.getXAnhangLf();
			oZeile[REPORT_ALLE_ANHANG_KD] = inseratDto.getXAnhang();

			oZeile[REPORT_ALLE_MEDIUM] = inseratDto.getCMedium();
			oZeile[REPORT_ALLE_BEZEICHNUNG] = flrInserat.getC_bez();
			oZeile[REPORT_ALLE_MENGE] = flrInserat.getN_menge();
			oZeile[REPORT_ALLE_ARTIKEL] = flrInserat.getFlrartikel_inseratart()
					.getC_nr();
			oZeile[REPORT_ALLE_PREIS_EK] = flrInserat
					.getN_nettoeinzelpreis_ek();
			oZeile[REPORT_ALLE_PREIS_VK] = flrInserat
					.getN_nettoeinzelpreis_vk();

			oZeile[REPORT_ALLE_LIEFERANT] = flrInserat.getFlrlieferant()
					.getFlrpartner().getC_name1nachnamefirmazeile1();
			oZeile[REPORT_ALLE_VERTRETER] = HelperServer
					.formatNameAusFLRPartner(flrInserat.getFlrvertreter()
							.getFlrpartner());

			if (flrInserat.getFlrbestellposition() != null) {
				oZeile[REPORT_ALLE_BESTELLNUMMER] = flrInserat
						.getFlrbestellposition().getFlrbestellung().getC_nr();
			}
			if (inseratDto.getPersonalIIdErschienen() != null) {
				oZeile[REPORT_ALLE_PERSON_ERSCHIENEN] = getPersonalFac()
						.personalFindByPrimaryKey(
								inseratDto.getPersonalIIdErschienen(),
								theClientDto).formatAnrede();
			}

			if (inseratDto.getPersonalIIdVerrechnen() != null) {
				oZeile[REPORT_ALLE_PERSON_VERRECHENBAR] = getPersonalFac()
						.personalFindByPrimaryKey(
								inseratDto.getPersonalIIdVerrechnen(),
								theClientDto).formatAnrede();
			}
			if (inseratDto.getPersonalIIdGestoppt() != null) {
				oZeile[REPORT_ALLE_PERSON_GESTOPPT] = getPersonalFac()
						.personalFindByPrimaryKey(
								inseratDto.getPersonalIIdGestoppt(),
								theClientDto).formatAnrede();
			}
			oZeile[REPORT_ALLE_GRUND_GESTOPPT] = inseratDto.getCGestoppt();

			oZeile[REPORT_ALLE_DRUCK_BESTELLUNG_KD] = Helper
					.short2Boolean(inseratDto.getBDruckBestellungKd());
			oZeile[REPORT_ALLE_DRUCK_BESTELLUNG_LF] = Helper
					.short2Boolean(inseratDto.getBDruckBestellungLf());
			oZeile[REPORT_ALLE_DRUCK_RECHNUNG_KD] = Helper
					.short2Boolean(inseratDto.getBDruckRechnungKd());

			oZeile[REPORT_ALLE_SUBREPORT_KUNDEN] = getSubreportKunde(
					flrInserat, theClientDto);

			String[] fieldnamesArtikel = new String[] { "Artikel",
					"Bezeichnung", "Menge", "PreisVK", "PreisEK" };

			InseratartikelDto[] iaDtos = getInseratFac()
					.inseratartikelFindByInseratIId(flrInserat.getI_id());

			Object[][] dataSubArtikel = new Object[iaDtos.length][fieldnamesArtikel.length];

			for (int i = 0; i < iaDtos.length; i++) {
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(
								iaDtos[i].getArtikelIId(), theClientDto);
				dataSubArtikel[i][0] = artikelDto.getCNr();
				dataSubArtikel[i][1] = artikelDto.formatBezeichnung();
				dataSubArtikel[i][2] = iaDtos[i].getNMenge();
				dataSubArtikel[i][3] = iaDtos[i].getNNettoeinzelpreisVk();
				dataSubArtikel[i][4] = iaDtos[i].getNNettoeinzelpreisEk();

			}

			oZeile[REPORT_ALLE_SUBREPORT_ARTIKEL] = new LPDatenSubreport(
					dataSubArtikel, fieldnamesArtikel);

			String[] fieldnamesER = new String[] { "Eingangsrechnung" };
			Object[][] dataSub = new Object[flrInserat.getErset().size()][fieldnamesER.length];

			Iterator iterEr = flrInserat.getErset().iterator();
			int u = 0;
			while (iterEr.hasNext()) {
				FLRInserater inseratEr = (FLRInserater) iterEr.next();
				dataSub[u][0] = inseratEr.getFlreingangsrechnung().getC_nr();
				u++;
			}

			oZeile[REPORT_ALLE_SUBREPORT_EINGANGSRECHNUNG] = new LPDatenSubreport(
					dataSub, fieldnamesER);

			alDaten.add(oZeile);

		}
		session.close();
		Object[][] returnArray = new Object[alDaten.size()][REPORT_ALLE_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(returnArray);

		initJRDS(parameter, InseratReportFac.REPORT_MODUL,
				InseratReportFac.REPORT_INSERATE_ALLE,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printDBAuswertung(ReportJournalInseratDto krit,
			int iOptionDatum, TheClientDto theClientDto) {

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		sAktuellerReport = InseratReportFac.REPORT_INSERATE_DBAUSWERTUNG;

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT inserat FROM FLRInserat inserat LEFT OUTER JOIN inserat.rechnungset as rechnungset WHERE inserat.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' AND inserat.status_c_nr not in ('"
				+ LocaleFac.STATUS_STORNIERT + "') ";

		if (krit.personalIId != null) {
			queryString += " AND inserat.flrvertreter.i_id=" + krit.personalIId;
			parameter.put("P_VERTRETER", getPersonalFac()
					.personalFindByPrimaryKey(krit.personalIId, theClientDto)
					.formatAnrede());
		}
		if (krit.lieferantIId != null) {
			queryString += " AND inserat.flrlieferant.i_id="
					+ krit.lieferantIId;
			parameter.put("P_LIEFERANT", getLieferantFac()
					.lieferantFindByPrimaryKey(krit.lieferantIId, theClientDto)
					.getPartnerDto().formatFixTitelName1Name2());
		}
		if (krit.kundeIId != null) {
			queryString += " AND rechnungset.flrkunde.i_id=" + krit.kundeIId;
			parameter.put(
					"P_KUNDE",
					getKundeFac()
							.kundeFindByPrimaryKey(krit.kundeIId, theClientDto)
							.getPartnerDto().formatFixTitelName1Name2());
		}

		// Datum von/bis

		if (krit.dVon != null) {

			if (iOptionDatum == InseratReportFac.REPORT_DBAUSWERTUNG_OPTION_DATUM_BEZAHLTDATUM) {
				queryString += " AND (rechnungset.flrrechnungposition.flrrechnung.t_bezahltdatum>='"
						+ Helper.formatDateWithSlashes(krit.dVon)
						+ "' OR  inserat.t_manuellerledigt >='"
						+ Helper.formatDateWithSlashes(krit.dVon) + "' ) ";
				parameter.put("P_OPTION_DATUM", "Bezahltdatum");
			} else if (iOptionDatum == InseratReportFac.REPORT_DBAUSWERTUNG_OPTION_DATUM_VERRECHNETDATUM) {

				queryString += " AND (rechnungset.flrrechnungposition.flrrechnung.d_belegdatum>='"
						+ Helper.formatDateWithSlashes(krit.dVon)
						+ "'  OR inserat.t_manuellerledigt >='"
						+ Helper.formatDateWithSlashes(krit.dVon) + "' ) ";
				parameter.put("P_OPTION_DATUM", "Verrechnetdatum");
			} else {
				queryString += " AND inserat.t_belegdatum>='"
						+ Helper.formatDateWithSlashes(krit.dVon) + "' ";
				parameter.put("P_OPTION_DATUM", "Belegdatum");
			}

			parameter.put("P_VON", krit.dVon);

		}
		if (krit.dBis != null) {
			if (iOptionDatum == InseratReportFac.REPORT_DBAUSWERTUNG_OPTION_DATUM_BEZAHLTDATUM) {
				queryString += "AND rechnungset.flrrechnungposition.flrrechnung.t_bezahltdatum<='"
						+ Helper.formatDateWithSlashes(krit.dBis)
						+ "' OR  inserat.t_manuellerledigt <='"
						+ Helper.formatDateWithSlashes(krit.dBis) + "' ) ";
			} else if (iOptionDatum == InseratReportFac.REPORT_DBAUSWERTUNG_OPTION_DATUM_VERRECHNETDATUM) {
				queryString += "AND rechnungset.flrrechnungposition.flrrechnung.d_belegdatum<='"
						+ Helper.formatDateWithSlashes(krit.dBis)
						+ "' OR  inserat.t_manuellerledigt <='"
						+ Helper.formatDateWithSlashes(krit.dBis) + "' ) ";
			} else {
				queryString += "AND inserat.t_belegdatum<='"
						+ Helper.formatDateWithSlashes(krit.dBis) + "' ";
			}
			parameter.put("P_BIS", krit.dBis);
		}

		// Belegnummer von/bis
		LpBelegnummerFormat f = getBelegnummerGeneratorObj()
				.getBelegnummernFormat(theClientDto.getMandant());
		Integer iGeschaeftsjahr = null;
		String sMandantKuerzel = null;
		try {
			iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(
					theClientDto.getMandant());
			sMandantKuerzel = getParameterFac().getMandantparameter(
					theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_BELEGNUMMER_MANDANTKENNUNG)
					.getCWert();
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		if (krit.sBelegnummerVon != null) {
			String sVon = HelperServer
					.getBelegnummernFilterForHibernateCriterias(f,
							iGeschaeftsjahr, sMandantKuerzel,
							krit.sBelegnummerVon);

			queryString += " AND inserat.c_nr>='" + sVon + "' ";
			parameter.put("P_BELEGNUMMER_VON", sVon);
		}
		if (krit.sBelegnummerBis != null) {
			String sBis = HelperServer
					.getBelegnummernFilterForHibernateCriterias(f,
							iGeschaeftsjahr, sMandantKuerzel,
							krit.sBelegnummerBis);
			queryString += " AND inserat.c_nr<='" + sBis + "' ";
			parameter.put("P_BELEGNUMMER_BIS", sBis);

		}

		int iNachkommastellenPreis = 2;
		try {
			iNachkommastellenPreis = getMandantFac()
					.getNachkommastellenPreisVK(theClientDto.getMandant());
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		org.hibernate.Query query = session.createQuery(queryString);
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		ArrayList alDaten = new ArrayList();
		while (resultListIterator.hasNext()) {

			FLRInserat flrInserat = (FLRInserat) resultListIterator.next();

			if (iOptionDatum == InseratReportFac.REPORT_DBAUSWERTUNG_OPTION_DATUM_KEINE
					|| iOptionDatum == InseratReportFac.REPORT_DBAUSWERTUNG_OPTION_DATUM_BELEGDATUM) {

				Iterator itInseratrechnung = flrInserat.getRechnungset()
						.iterator();
				while (itInseratrechnung.hasNext()) {
					befuelleZeileDBAuswertung(flrInserat,
							(FLRInseratrechnung) itInseratrechnung.next(),
							iNachkommastellenPreis, alDaten, theClientDto);
				}
			} else if (iOptionDatum == InseratReportFac.REPORT_DBAUSWERTUNG_OPTION_DATUM_VERRECHNETDATUM) {

				Iterator itInseratrechnung = flrInserat.getRechnungset()
						.iterator();
				while (itInseratrechnung.hasNext()) {
					FLRInseratrechnung flrInseratrechnung = (FLRInseratrechnung) itInseratrechnung
							.next();

					java.util.Date dDatum = null;

					if (flrInseratrechnung.getFlrrechnungposition() != null) {

						dDatum = flrInseratrechnung.getFlrrechnungposition()
								.getFlrrechnung().getD_belegdatum();

					} else {
						dDatum = flrInserat.getT_manuellerledigt();
					}

					if (dDatum != null) {
						if (krit.dVon != null) {
							if (dDatum.before(krit.dVon)) {
								continue;

							}
						}

						if (krit.dBis != null) {
							if (dDatum.after(krit.dBis)) {
								continue;

							}
						}
						befuelleZeileDBAuswertung(flrInserat,
								flrInseratrechnung, iNachkommastellenPreis,
								alDaten, theClientDto);
					}

				}

			} else if (iOptionDatum == InseratReportFac.REPORT_DBAUSWERTUNG_OPTION_DATUM_BEZAHLTDATUM) {
				Iterator itInseratrechnung = flrInserat.getRechnungset()
						.iterator();
				boolean bAlleRechnungenBezahlt = true;
				while (itInseratrechnung.hasNext()) {
					FLRInseratrechnung kunde = (FLRInseratrechnung) itInseratrechnung
							.next();

					java.util.Date dDatum = flrInserat.getT_manuellerledigt();

					if (dDatum == null
							&& kunde.getFlrrechnungposition() != null
							&& kunde.getFlrrechnungposition().getFlrrechnung()
									.getT_bezahltdatum() != null) {
						dDatum = kunde.getFlrrechnungposition()
								.getFlrrechnung().getT_bezahltdatum();
					}

					if (dDatum != null) {

						if (krit.dVon != null) {

							if (dDatum.before(krit.dVon)) {
								bAlleRechnungenBezahlt = false;
								break;
							}

						}

						if (krit.dBis != null) {

							if (dDatum.after(krit.dBis)) {
								bAlleRechnungenBezahlt = false;
								break;
							}

						}

					} else {
						bAlleRechnungenBezahlt = false;
						break;
					}
				}

				// Wenn alle Rechnungen beahlt sind, dann muessen alle in der
				// Statistik angefuehrt werden.
				// Wenn eine Rechnung nicht bezahlt ist, dann darf keine
				// Rechnung angefuehrt werden.

				if (bAlleRechnungenBezahlt == true) {
					itInseratrechnung = flrInserat.getRechnungset().iterator();
					while (itInseratrechnung.hasNext()) {
						befuelleZeileDBAuswertung(flrInserat,
								(FLRInseratrechnung) itInseratrechnung.next(),
								iNachkommastellenPreis, alDaten, theClientDto);
					}
				}
			}

		}

		// Nun noch sortieren

		// Sortierung

		if (krit.iSortierung == ReportJournalInseratDto.KRIT_SORT_NACH_BELEGNUMMER) {
			parameter
					.put("P_SORTIERUNG",
							getTextRespectUISpr("bes.belegnummer",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));

		} else if (krit.iSortierung == ReportJournalInseratDto.KRIT_SORT_NACH_KUNDE) {
			parameter.put(
					"P_SORTIERUNG",
					getTextRespectUISpr("lp.kunde", theClientDto.getMandant(),
							theClientDto.getLocUi()));
		} else if (krit.iSortierung == ReportJournalInseratDto.KRIT_SORT_NACH_LIEFERANT) {
			parameter
					.put("P_SORTIERUNG",
							getTextRespectUISpr("lp.lieferant",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
		} else if (krit.iSortierung == ReportJournalInseratDto.KRIT_SORT_NACH_VERTRETER) {
			parameter
					.put("P_SORTIERUNG",
							getTextRespectUISpr("lp.vertreter",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
		}

		for (int i = alDaten.size() - 1; i > 0; --i) {
			for (int j = 0; j < i; ++j) {
				Object[] o = (Object[]) alDaten.get(j);
				Object[] o1 = (Object[]) alDaten.get(j + 1);

				String beleg1 = (String) o[REPORT_DBAUSWERTUNG_BELEGNUMMER];
				String beleg2 = (String) o1[REPORT_DBAUSWERTUNG_BELEGNUMMER];

				String s1 = "";
				String s2 = "";

				if (krit.iSortierung == ReportJournalInseratDto.KRIT_SORT_NACH_BELEGNUMMER) {

					s1 = beleg1;
					s2 = beleg2;

				} else if (krit.iSortierung == ReportJournalInseratDto.KRIT_SORT_NACH_VERTRETER) {

					String v1 = (String) o[REPORT_DBAUSWERTUNG_VERTRETER];
					String v2 = (String) o1[REPORT_DBAUSWERTUNG_VERTRETER];
					String k1 = (String) o[REPORT_DBAUSWERTUNG_KUNDE];
					String k2 = (String) o1[REPORT_DBAUSWERTUNG_KUNDE];

					s1 = Helper.fitString2Length(v1, 60, ' ')
							+ Helper.fitString2Length(k1, 80, ' ') + beleg1;
					s2 = Helper.fitString2Length(v2, 60, ' ')
							+ Helper.fitString2Length(k2, 80, ' ') + beleg2;
				} else if (krit.iSortierung == ReportJournalInseratDto.KRIT_SORT_NACH_KUNDE) {

					String v1 = (String) o[REPORT_DBAUSWERTUNG_KUNDE];
					String v2 = (String) o1[REPORT_DBAUSWERTUNG_KUNDE];

					s1 = Helper.fitString2Length(v1, 80, ' ') + beleg1;
					s2 = Helper.fitString2Length(v2, 80, ' ') + beleg2;
				} else if (krit.iSortierung == ReportJournalInseratDto.KRIT_SORT_NACH_LIEFERANT) {

					String v1 = (String) o[REPORT_DBAUSWERTUNG_LIEFERANT];
					String v2 = (String) o1[REPORT_DBAUSWERTUNG_LIEFERANT];

					s1 = Helper.fitString2Length(v1, 80, ' ') + beleg1;
					s2 = Helper.fitString2Length(v2, 80, ' ') + beleg2;
				}

				if (s1.compareTo(s2) > 0) {
					alDaten.set(j, o1);
					alDaten.set(j + 1, o);
				}
			}
		}

		session.close();
		Object[][] returnArray = new Object[alDaten.size()][REPORT_DBAUSWERTUNG_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(returnArray);

		initJRDS(parameter, InseratReportFac.REPORT_MODUL,
				InseratReportFac.REPORT_INSERATE_DBAUSWERTUNG,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();

	}

	private void befuelleZeileDBAuswertung(FLRInserat flrInserat,
			FLRInseratrechnung flrInseratrechnung, int iNachkommastellenPreis,
			ArrayList alDaten, TheClientDto theClientDto) {
		InseratDto inseratDto = getInseratFac().inseratFindByPrimaryKey(
				flrInserat.getI_id());

		Object[] oZeile = new Object[REPORT_DBAUSWERTUNG_ANZAHL_SPALTEN];

		oZeile[REPORT_DBAUSWERTUNG_BELEGNUMMER] = flrInserat.getC_nr();
		oZeile[REPORT_DBAUSWERTUNG_DATUM_ERSCHIENEN] = flrInserat
				.getT_erschienen();
		oZeile[REPORT_DBAUSWERTUNG_BELEGDATUM] = flrInserat.getT_belegdatum();
		oZeile[REPORT_DBAUSWERTUNG_TERMIN] = flrInserat.getT_termin();
		oZeile[REPORT_DBAUSWERTUNG_RUBRIK] = flrInserat.getC_rubrik();
		oZeile[REPORT_DBAUSWERTUNG_RUBRIK2] = flrInserat.getC_rubrik2();

		oZeile[REPORT_DBAUSWERTUNG_RUBRIK2] = flrInserat.getC_rubrik2();
		oZeile[REPORT_DBAUSWERTUNG_RUBRIK2] = flrInserat.getC_rubrik2();
		oZeile[REPORT_DBAUSWERTUNG_RUBRIK2] = flrInserat.getC_rubrik2();
		oZeile[REPORT_DBAUSWERTUNG_STICHWORT] = flrInserat.getC_stichwort();
		oZeile[REPORT_DBAUSWERTUNG_STICHWORT2] = flrInserat.getC_stichwort2();
		oZeile[REPORT_DBAUSWERTUNG_STATUS] = flrInserat.getStatus_c_nr();

		oZeile[REPORT_DBAUSWERTUNG_ANHANG_LF] = inseratDto.getXAnhangLf();
		oZeile[REPORT_DBAUSWERTUNG_ANHANG_KD] = inseratDto.getXAnhang();

		oZeile[REPORT_DBAUSWERTUNG_MEDIUM] = inseratDto.getCMedium();
		oZeile[REPORT_DBAUSWERTUNG_BEZEICHNUNG] = flrInserat.getC_bez();
		oZeile[REPORT_DBAUSWERTUNG_MENGE] = flrInserat.getN_menge();
		oZeile[REPORT_DBAUSWERTUNG_ARTIKEL] = flrInserat
				.getFlrartikel_inseratart().getC_nr();
		oZeile[REPORT_DBAUSWERTUNG_PREIS_EK] = flrInserat
				.getN_nettoeinzelpreis_ek();
		oZeile[REPORT_DBAUSWERTUNG_PREIS_VK] = flrInserat
				.getN_nettoeinzelpreis_vk();
		oZeile[REPORT_DBAUSWERTUNG_ERRECHNETERWERT_EK] = inseratDto
				.getErrechneterWertEK(iNachkommastellenPreis);
		oZeile[REPORT_DBAUSWERTUNG_ERRECHNETERWERT_VK] = inseratDto
				.getErrechneterWertVK(iNachkommastellenPreis);
		oZeile[REPORT_DBAUSWERTUNG_MANUELL_ERLEDIGT] = inseratDto
				.getTManuellerledigt();

		oZeile[REPORT_DBAUSWERTUNG_LIEFERANT] = flrInserat.getFlrlieferant()
				.getFlrpartner().getC_name1nachnamefirmazeile1();
		oZeile[REPORT_DBAUSWERTUNG_VERTRETER] = HelperServer
				.formatNameAusFLRPartner(flrInserat.getFlrvertreter()
						.getFlrpartner());

		if (flrInserat.getFlrbestellposition() != null) {
			oZeile[REPORT_DBAUSWERTUNG_BESTELLNUMMER] = flrInserat
					.getFlrbestellposition().getFlrbestellung().getC_nr();
		}
		if (inseratDto.getPersonalIIdErschienen() != null) {
			oZeile[REPORT_DBAUSWERTUNG_PERSON_ERSCHIENEN] = getPersonalFac()
					.personalFindByPrimaryKey(
							inseratDto.getPersonalIIdErschienen(), theClientDto)
					.formatAnrede();
		}

		if (inseratDto.getPersonalIIdVerrechnen() != null) {
			oZeile[REPORT_DBAUSWERTUNG_PERSON_VERRECHENBAR] = getPersonalFac()
					.personalFindByPrimaryKey(
							inseratDto.getPersonalIIdVerrechnen(), theClientDto)
					.formatAnrede();
		}
		if (inseratDto.getPersonalIIdGestoppt() != null) {
			oZeile[REPORT_DBAUSWERTUNG_PERSON_GESTOPPT] = getPersonalFac()
					.personalFindByPrimaryKey(
							inseratDto.getPersonalIIdGestoppt(), theClientDto)
					.formatAnrede();
		}
		oZeile[REPORT_DBAUSWERTUNG_GRUND_GESTOPPT] = inseratDto.getCGestoppt();

		oZeile[REPORT_DBAUSWERTUNG_DRUCK_BESTELLUNG_KD] = Helper
				.short2Boolean(inseratDto.getBDruckBestellungKd());
		oZeile[REPORT_DBAUSWERTUNG_DRUCK_BESTELLUNG_LF] = Helper
				.short2Boolean(inseratDto.getBDruckBestellungLf());
		oZeile[REPORT_DBAUSWERTUNG_DRUCK_RECHNUNG_KD] = Helper
				.short2Boolean(inseratDto.getBDruckRechnungKd());

		oZeile[REPORT_DBAUSWERTUNG_SUBREPORT_KUNDEN] = getSubreportKunde(
				flrInserat, theClientDto);

		String[] fieldnamesArtikel = new String[] { "Artikel", "Bezeichnung",
				"Menge", "PreisVK", "PreisEK" };

		InseratartikelDto[] iaDtos = getInseratFac()
				.inseratartikelFindByInseratIId(flrInserat.getI_id());

		Object[][] dataSubArtikel = new Object[iaDtos.length][fieldnamesArtikel.length];

		for (int i = 0; i < iaDtos.length; i++) {
			ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(iaDtos[i].getArtikelIId(),
							theClientDto);
			dataSubArtikel[i][0] = artikelDto.getCNr();
			dataSubArtikel[i][1] = artikelDto.formatBezeichnung();
			dataSubArtikel[i][2] = iaDtos[i].getNMenge();
			dataSubArtikel[i][3] = iaDtos[i].getNNettoeinzelpreisVk();
			dataSubArtikel[i][4] = iaDtos[i].getNNettoeinzelpreisEk();

		}

		oZeile[REPORT_DBAUSWERTUNG_SUBREPORT_ARTIKEL] = new LPDatenSubreport(
				dataSubArtikel, fieldnamesArtikel);

		String[] fieldnamesER = new String[] { "Eingangsrechnung" };
		Object[][] dataSub = new Object[flrInserat.getErset().size()][fieldnamesER.length];

		Iterator iterEr = flrInserat.getErset().iterator();
		int u = 0;
		while (iterEr.hasNext()) {
			FLRInserater inseratEr = (FLRInserater) iterEr.next();
			dataSub[u][0] = inseratEr.getFlreingangsrechnung().getC_nr();
			u++;
		}

		oZeile[REPORT_DBAUSWERTUNG_SUBREPORT_EINGANGSRECHNUNG] = new LPDatenSubreport(
				dataSub, fieldnamesER);

		java.util.Set s = flrInserat.getRechnungset();
		Iterator it = s.iterator();

		Timestamp tBezahlt = null;
		Timestamp tVerrechnet = null;

		while (it.hasNext()) {
			FLRInseratrechnung ir = (FLRInseratrechnung) it.next();

			if (ir.getRechnungposition_i_id() != null) {

				if (tVerrechnet == null) {
					tVerrechnet = new Timestamp(ir.getFlrrechnungposition()
							.getFlrrechnung().getD_belegdatum().getTime());
				}

				if (tVerrechnet.before(ir.getFlrrechnungposition()
						.getFlrrechnung().getD_belegdatum())) {
					tVerrechnet = new Timestamp(ir.getFlrrechnungposition()
							.getFlrrechnung().getD_belegdatum().getTime());
				}

				if (ir.getFlrrechnungposition().getFlrrechnung()
						.getStatus_c_nr().equals(RechnungFac.STATUS_BEZAHLT)) {

					java.util.Date dBezahlt = getRechnungFac()
							.getDatumLetzterZahlungseingang(
									ir.getFlrrechnungposition()
											.getFlrrechnung().getI_id());

					if (tBezahlt == null && dBezahlt != null) {
						tBezahlt = new Timestamp(dBezahlt.getTime());
					}

					if (tBezahlt != null && dBezahlt != null) {
						if (tBezahlt.before(dBezahlt)) {
							tBezahlt = new Timestamp(dBezahlt.getTime());
						}
					}
				}
			}
		}

		oZeile[REPORT_DBAUSWERTUNG_VOLLSTAENDIG_VERRECHNET] = tVerrechnet;

		oZeile[REPORT_DBAUSWERTUNG_VOLLSTAENDIG_BEZAHLT] = tBezahlt;

		oZeile[REPORT_DBAUSWERTUNG_KUNDE] = HelperServer
				.formatNameAusFLRPartner(flrInseratrechnung.getFlrkunde()
						.getFlrpartner());

		if (flrInseratrechnung.getRechnungposition_i_id() != null) {
			oZeile[REPORT_DBAUSWERTUNG_RECHNUNG_NUMMER] = flrInseratrechnung
					.getFlrrechnungposition().getFlrrechnung().getC_nr();
			oZeile[REPORT_DBAUSWERTUNG_RECHNUNG_STATUS] = flrInseratrechnung
					.getFlrrechnungposition().getFlrrechnung().getStatus_c_nr();
			oZeile[REPORT_DBAUSWERTUNG_RECHNUNGSDATUM] = flrInseratrechnung
					.getFlrrechnungposition().getFlrrechnung()
					.getD_belegdatum();

			try {
				RechnungDto reDto = getRechnungFac().rechnungFindByPrimaryKey(
						flrInseratrechnung.getFlrrechnungposition()
								.getFlrrechnung().getI_id());

				oZeile[REPORT_DBAUSWERTUNG_RECHNUNG_ZAHLUNGSZIEL] = getMandantFac()
						.zahlungszielFindByPrimaryKey(
								reDto.getZahlungszielIId(), theClientDto)
						.getCBez();
				oZeile[REPORT_DBAUSWERTUNG_RECHNUNGBEZAHLT_BETRAG] = getRechnungFac()
						.getBereitsBezahltWertVonRechnung(reDto.getIId(), null);
				oZeile[REPORT_DBAUSWERTUNG_RECHNUNG_LETZTES_ZAHLDATUM] = getRechnungFac()
						.getDatumLetzterZahlungseingang(reDto.getIId());
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}

		oZeile[REPORT_DBAUSWERTUNG_KUNDE] = getKundeFac()
				.kundeFindByPrimaryKey(flrInseratrechnung.getKunde_i_id(),
						theClientDto).getPartnerDto().formatName();
		alDaten.add(oZeile);
	}

	private LPDatenSubreport getSubreportKunde(FLRInserat flrInserat,
			TheClientDto theClientDto) {
		String[] fieldnames = new String[] { "Kunde", "Rechnung",
				"Rechnungsstatus", "Rechnungsdatum", "Zahlungsziel",
				"BetragBezahlt", "LetztesZahldatum" };
		Object[][] dataSubKD = new Object[flrInserat.getRechnungset().size()][fieldnames.length];

		Iterator iterRe = flrInserat.getRechnungset().iterator();
		int u = 0;
		while (iterRe.hasNext()) {
			FLRInseratrechnung inseratRe = (FLRInseratrechnung) iterRe.next();
			dataSubKD[u][0] = HelperServer.formatNameAusFLRPartner(inseratRe
					.getFlrkunde().getFlrpartner());
			if (inseratRe.getRechnungposition_i_id() != null) {
				dataSubKD[u][1] = inseratRe.getFlrrechnungposition()
						.getFlrrechnung().getC_nr();
				dataSubKD[u][2] = inseratRe.getFlrrechnungposition()
						.getFlrrechnung().getStatus_c_nr();
				dataSubKD[u][3] = inseratRe.getFlrrechnungposition()
						.getFlrrechnung().getD_belegdatum();
				try {
					RechnungDto reDto = getRechnungFac()
							.rechnungFindByPrimaryKey(
									inseratRe.getFlrrechnungposition()
											.getFlrrechnung().getI_id());

					dataSubKD[u][4] = getMandantFac()
							.zahlungszielFindByPrimaryKey(
									reDto.getZahlungszielIId(), theClientDto)
							.getCBez();
					dataSubKD[u][5] = getRechnungFac()
							.getBereitsBezahltWertVonRechnung(reDto.getIId(),
									null);
					dataSubKD[u][6] = getRechnungFac()
							.getDatumLetzterZahlungseingang(reDto.getIId());

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}

			u++;
		}

		return new LPDatenSubreport(dataSubKD, fieldnames);
	}

	private LPDatenSubreport getSubreportKundeFuerInseratDruck(
			FLRInserat flrInserat, TheClientDto theClientDto,
			MandantDto mandantDto) {
		String[] fieldnames = new String[] { "Kunde", "Rechnung",
				"Rechnungsstatus", "Rechnungsdatum", "Zahlungsziel",
				"BetragBezahlt", "LetztesZahldatum", "Lkz", "Plz", "Ort",
				"Strasse", "LandWennNichtInland" };
		Object[][] dataSubKD = new Object[flrInserat.getRechnungset().size()][fieldnames.length];

		Iterator iterRe = flrInserat.getRechnungset().iterator();
		int u = 0;
		while (iterRe.hasNext()) {
			FLRInseratrechnung inseratRe = (FLRInseratrechnung) iterRe.next();
			dataSubKD[u][0] = HelperServer.formatNameAusFLRPartner(inseratRe
					.getFlrkunde().getFlrpartner());
			if (inseratRe.getRechnungposition_i_id() != null) {
				dataSubKD[u][1] = inseratRe.getFlrrechnungposition()
						.getFlrrechnung().getC_nr();
				dataSubKD[u][2] = inseratRe.getFlrrechnungposition()
						.getFlrrechnung().getStatus_c_nr();
				dataSubKD[u][3] = inseratRe.getFlrrechnungposition()
						.getFlrrechnung().getD_belegdatum();
				try {
					RechnungDto reDto = getRechnungFac()
							.rechnungFindByPrimaryKey(
									inseratRe.getFlrrechnungposition()
											.getFlrrechnung().getI_id());

					dataSubKD[u][4] = getMandantFac()
							.zahlungszielFindByPrimaryKey(
									reDto.getZahlungszielIId(), theClientDto)
							.getCBez();
					dataSubKD[u][5] = getRechnungFac()
							.getBereitsBezahltWertVonRechnung(reDto.getIId(),
									null);
					dataSubKD[u][6] = getRechnungFac()
							.getDatumLetzterZahlungseingang(reDto.getIId());

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}
			if (inseratRe.getFlrkunde().getFlrpartner().getFlrlandplzort() != null) {
				dataSubKD[u][7] = inseratRe.getFlrkunde().getFlrpartner()
						.getFlrlandplzort().getFlrland().getC_lkz();
				dataSubKD[u][8] = inseratRe.getFlrkunde().getFlrpartner()
						.getFlrlandplzort().getC_plz();
				dataSubKD[u][9] = inseratRe.getFlrkunde().getFlrpartner()
						.getFlrlandplzort().getFlrort().getC_name();

				if (mandantDto.getPartnerDto().getLandplzortDto() != null
						&& !mandantDto
								.getPartnerDto()
								.getLandplzortDto()
								.getLandDto()
								.getIID()
								.equals(inseratRe.getFlrkunde().getFlrpartner()
										.getFlrlandplzort().getFlrland()
										.getI_id())) {
					dataSubKD[u][11] = inseratRe.getFlrkunde().getFlrpartner()
							.getFlrlandplzort().getFlrland().getC_name();
				}

			}

			dataSubKD[u][10] = inseratRe.getFlrkunde().getFlrpartner()
					.getC_strasse();

			u++;
		}

		return new LPDatenSubreport(dataSubKD, fieldnames);
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printOffene(ReportJournalInseratDto krit,
			TheClientDto theClientDto) {
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		sAktuellerReport = InseratReportFac.REPORT_INSERATE_OFFENE;

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT inseratrechnung FROM FLRInseratrechnung inseratrechnung LEFT OUTER JOIN inseratrechnung.flrrechnungposition repos LEFT OUTER JOIN repos.flrrechnung re LEFT OUTER JOIN inseratrechnung.flrinserat.flrbestellposition bestpos LEFT OUTER JOIN bestpos.flrbestellung bestellung LEFT OUTER JOIN inseratrechnung.flrinserat.erset erset LEFT OUTER JOIN erset.flreingangsrechnung er  WHERE inseratrechnung.flrinserat.mandant_c_nr='"
				+ theClientDto.getMandant() + "' ";

		if (krit.dStichtag == null) {
			queryString += " AND inseratrechnung.flrinserat.status_c_nr NOT IN ('"
					+ LocaleFac.STATUS_STORNIERT
					+ "','"
					+ LocaleFac.STATUS_ERLEDIGT
					+ "','"
					+ LocaleFac.STATUS_VERRECHNET
					+ "') AND (inseratrechnung.rechnungposition_i_id IS NULL OR re.status_c_nr <> '"
					+ RechnungFac.STATUS_BEZAHLT + "' ) ";
		} else {
			queryString += "AND inseratrechnung.flrinserat.t_belegdatum<='"
					+ Helper.formatDateWithSlashes(krit.dStichtag)
					+ "'  AND inseratrechnung.flrinserat.status_c_nr NOT IN ('"
					+ LocaleFac.STATUS_STORNIERT
					+ "') AND (inseratrechnung.flrinserat.t_manuellerledigt IS NULL OR inseratrechnung.flrinserat.t_manuellerledigt > '"
					+ Helper.formatDateWithSlashes(krit.dStichtag) + "' ) ";
		}

		if (krit.personalIId != null) {
			queryString += " AND inseratrechnung.flrinserat.flrvertreter.i_id="
					+ krit.personalIId;
			parameter.put("P_VERTRETER", getPersonalFac()
					.personalFindByPrimaryKey(krit.personalIId, theClientDto)
					.formatAnrede());
		}
		if (krit.lieferantIId != null) {
			queryString += " AND inseratrechnung.flrinserat.flrlieferant.i_id="
					+ krit.lieferantIId;
			parameter.put("P_LIEFERANT", getLieferantFac()
					.lieferantFindByPrimaryKey(krit.lieferantIId, theClientDto)
					.getPartnerDto().formatFixTitelName1Name2());
		}
		if (krit.kundeIId != null) {
			queryString += " AND inseratrechnung.flrkunde.i_id="
					+ krit.kundeIId;
			parameter.put(
					"P_KUNDE",
					getKundeFac()
							.kundeFindByPrimaryKey(krit.kundeIId, theClientDto)
							.getPartnerDto().formatFixTitelName1Name2());
		}

		// Datum von/bis

		if (krit.dVon != null) {
			queryString += " AND inseratrechnung.flrinserat.t_belegdatum>='"
					+ Helper.formatDateWithSlashes(krit.dVon) + "' ";

			parameter.put("P_VON", krit.dVon);

		}
		if (krit.dBis != null) {
			queryString += "AND inseratrechnung.flrinserat.t_belegdatum<='"
					+ Helper.formatDateWithSlashes(krit.dBis) + "' ";
			parameter.put("P_BIS", krit.dBis);
		}

		if (krit.dStichtag != null) {
			parameter.put("P_STICHTAG", krit.dStichtag);

			if (krit.iOptionStichtag == ReportJournalInseratDto.KRIT_OPTION_STICHTAG_NICHT_BESTELLT) {
				queryString += "AND (bestpos.i_id IS NULL OR bestellung.t_belegdatum >'"
						+ Helper.formatDateWithSlashes(krit.dStichtag) + "') ";
				parameter.put(
						"P_OPTION_STICHTAG",
						getTextRespectUISpr(
								"iv.inserate.offene.stichtag.nichtbestellt",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			} else if (krit.iOptionStichtag == ReportJournalInseratDto.KRIT_OPTION_STICHTAG_NICHT_ERSCHIENEN) {
				queryString += "AND (  inseratrechnung.flrinserat.t_erschienen IS NULL OR inseratrechnung.flrinserat.t_erschienen>'"
						+ Helper.formatDateWithSlashes(krit.dStichtag) + "') ";
				parameter.put(
						"P_OPTION_STICHTAG",
						getTextRespectUISpr(
								"iv.inserate.offene.stichtag.nichterschienen",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));

			} else if (krit.iOptionStichtag == ReportJournalInseratDto.KRIT_OPTION_STICHTAG_NICHT_VERRECHNET) {
				queryString += "AND (inseratrechnung.rechnungposition_i_id IS NULL OR re.d_belegdatum >'"
						+ Helper.formatDateWithSlashes(krit.dStichtag) + "') ";

				parameter.put(
						"P_OPTION_STICHTAG",
						getTextRespectUISpr(
								"iv.inserate.offene.stichtag.nichtverrechnet",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			} else if (krit.iOptionStichtag == ReportJournalInseratDto.KRIT_OPTION_STICHTAG_OHNE_ER) {
				queryString += "AND (erset.eingangsrechnung_i_id IS NULL OR er.t_belegdatum >'"
						+ Helper.formatDateWithSlashes(krit.dStichtag) + "') ";
				parameter.put(
						"P_OPTION_STICHTAG",
						getTextRespectUISpr(
								"iv.inserate.offene.stichtag.ohneer",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			}
		}

		// Belegnummer von/bis
		LpBelegnummerFormat f = getBelegnummerGeneratorObj()
				.getBelegnummernFormat(theClientDto.getMandant());
		Integer iGeschaeftsjahr = null;
		String sMandantKuerzel = null;
		try {
			iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(
					theClientDto.getMandant());
			sMandantKuerzel = getParameterFac().getMandantparameter(
					theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_BELEGNUMMER_MANDANTKENNUNG)
					.getCWert();
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		if (krit.sBelegnummerVon != null) {
			String sVon = HelperServer
					.getBelegnummernFilterForHibernateCriterias(f,
							iGeschaeftsjahr, sMandantKuerzel,
							krit.sBelegnummerVon);

			queryString += " AND inseratrechnung.flrinserat.c_nr>='" + sVon
					+ "' ";
			parameter.put("P_BELEGNUMMER_VON", sVon);
		}
		if (krit.sBelegnummerBis != null) {
			String sBis = HelperServer
					.getBelegnummernFilterForHibernateCriterias(f,
							iGeschaeftsjahr, sMandantKuerzel,
							krit.sBelegnummerBis);
			queryString += " AND inseratrechnung.flrinserat.c_nr<='" + sBis
					+ "' ";
			parameter.put("P_BELEGNUMMER_BIS", sBis);

		}

		// Sortierung

		if (krit.iSortierung == ReportJournalInseratDto.KRIT_SORT_NACH_BELEGNUMMER) {
			queryString += " ORDER BY inseratrechnung.flrinserat.c_nr ASC";

			parameter
					.put("P_SORTIERUNG",
							getTextRespectUISpr("bes.belegnummer",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));

		} else if (krit.iSortierung == ReportJournalInseratDto.KRIT_SORT_NACH_KUNDE) {
			queryString += " ORDER BY inseratrechnung.flrkunde.flrpartner.c_name1nachnamefirmazeile1 ASC, inseratrechnung.flrinserat.c_nr ASC";
			parameter.put(
					"P_SORTIERUNG",
					getTextRespectUISpr("lp.kunde", theClientDto.getMandant(),
							theClientDto.getLocUi()));
		} else if (krit.iSortierung == ReportJournalInseratDto.KRIT_SORT_NACH_LIEFERANT) {
			queryString += " ORDER BY inseratrechnung.flrinserat.flrlieferant.flrpartner.c_name1nachnamefirmazeile1 ASC, inseratrechnung.flrinserat.c_nr ASC";
			parameter
					.put("P_SORTIERUNG",
							getTextRespectUISpr("lp.lieferant",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
		} else if (krit.iSortierung == ReportJournalInseratDto.KRIT_SORT_NACH_VERTRETER) {
			queryString += " ORDER BY inseratrechnung.flrinserat.flrvertreter.flrpartner.c_name1nachnamefirmazeile1 ASC, inseratrechnung.flrinserat.c_nr ASC";
			parameter
					.put("P_SORTIERUNG",
							getTextRespectUISpr("lp.vertreter",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
		}
		org.hibernate.Query query = session.createQuery(queryString);
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();
		LinkedHashMap<Integer, FLRInserat> hmInserate = new LinkedHashMap<Integer, FLRInserat>();
		ArrayList alDaten = new ArrayList();
		while (resultListIterator.hasNext()) {
			FLRInseratrechnung flrInseratrechnung = (FLRInseratrechnung) resultListIterator
					.next();
			if (!hmInserate.containsKey(flrInseratrechnung.getInserat_i_id())) {
				hmInserate.put(flrInseratrechnung.getInserat_i_id(),
						flrInseratrechnung.getFlrinserat());
			}

		}

		Iterator it = hmInserate.keySet().iterator();
		while (it.hasNext()) {

			Integer key = (Integer) it.next();
			FLRInserat flrInserat = hmInserate.get(key);

			InseratDto inseratDto = getInseratFac().inseratFindByPrimaryKey(
					flrInserat.getI_id());

			Object[] oZeile = new Object[REPORT_OFFENE_ANZAHL_SPALTEN];

			oZeile[REPORT_OFFENE_BELEGNUMMER] = flrInserat.getC_nr();
			oZeile[REPORT_OFFENE_DATUM_ERSCHIENEN] = flrInserat
					.getT_erschienen();
			oZeile[REPORT_OFFENE_BELEGDATUM] = flrInserat.getT_belegdatum();
			oZeile[REPORT_OFFENE_TERMIN] = flrInserat.getT_termin();
			oZeile[REPORT_OFFENE_RUBRIK] = flrInserat.getC_rubrik();
			oZeile[REPORT_OFFENE_RUBRIK2] = flrInserat.getC_rubrik2();

			oZeile[REPORT_OFFENE_RUBRIK2] = flrInserat.getC_rubrik2();
			oZeile[REPORT_OFFENE_RUBRIK2] = flrInserat.getC_rubrik2();
			oZeile[REPORT_OFFENE_RUBRIK2] = flrInserat.getC_rubrik2();
			oZeile[REPORT_OFFENE_STICHWORT] = flrInserat.getC_stichwort();
			oZeile[REPORT_OFFENE_STICHWORT2] = flrInserat.getC_stichwort2();
			oZeile[REPORT_OFFENE_STATUS] = flrInserat.getStatus_c_nr();

			oZeile[REPORT_OFFENE_ANHANG_LF] = inseratDto.getXAnhangLf();
			oZeile[REPORT_OFFENE_ANHANG_KD] = inseratDto.getXAnhang();

			oZeile[REPORT_OFFENE_MEDIUM] = inseratDto.getCMedium();
			oZeile[REPORT_OFFENE_BEZEICHNUNG] = flrInserat.getC_bez();
			oZeile[REPORT_OFFENE_MENGE] = flrInserat.getN_menge();
			oZeile[REPORT_OFFENE_ARTIKEL] = flrInserat
					.getFlrartikel_inseratart().getC_nr();
			oZeile[REPORT_OFFENE_PREIS_EK] = flrInserat
					.getN_nettoeinzelpreis_ek();
			oZeile[REPORT_OFFENE_PREIS_VK] = flrInserat
					.getN_nettoeinzelpreis_vk();

			oZeile[REPORT_OFFENE_LIEFERANT] = flrInserat.getFlrlieferant()
					.getFlrpartner().getC_name1nachnamefirmazeile1();
			oZeile[REPORT_OFFENE_VERTRETER] = HelperServer
					.formatNameAusFLRPartner(flrInserat.getFlrvertreter()
							.getFlrpartner());

			if (flrInserat.getFlrbestellposition() != null) {
				oZeile[REPORT_OFFENE_BESTELLNUMMER] = flrInserat
						.getFlrbestellposition().getFlrbestellung().getC_nr();
			}
			if (inseratDto.getPersonalIIdErschienen() != null) {
				oZeile[REPORT_OFFENE_PERSON_ERSCHIENEN] = getPersonalFac()
						.personalFindByPrimaryKey(
								inseratDto.getPersonalIIdErschienen(),
								theClientDto).formatAnrede();
			}

			if (inseratDto.getPersonalIIdVerrechnen() != null) {
				oZeile[REPORT_OFFENE_PERSON_VERRECHENBAR] = getPersonalFac()
						.personalFindByPrimaryKey(
								inseratDto.getPersonalIIdVerrechnen(),
								theClientDto).formatAnrede();
			}

			if (inseratDto.getPersonalIIdGestoppt() != null) {
				oZeile[REPORT_OFFENE_PERSON_GESTOPPT] = getPersonalFac()
						.personalFindByPrimaryKey(
								inseratDto.getPersonalIIdGestoppt(),
								theClientDto).formatAnrede();
			}

			oZeile[REPORT_OFFENE_GRUND_GESTOPPT] = inseratDto.getCGestoppt();

			oZeile[REPORT_OFFENE_DRUCK_BESTELLUNG_KD] = Helper
					.short2Boolean(inseratDto.getBDruckBestellungKd());
			oZeile[REPORT_OFFENE_DRUCK_BESTELLUNG_LF] = Helper
					.short2Boolean(inseratDto.getBDruckBestellungLf());
			oZeile[REPORT_OFFENE_DRUCK_RECHNUNG_KD] = Helper
					.short2Boolean(inseratDto.getBDruckRechnungKd());

			oZeile[REPORT_OFFENE_SUBREPORT_KUNDEN] = getSubreportKunde(
					flrInserat, theClientDto);

			String[] fieldnamesArtikel = new String[] { "Artikel",
					"Bezeichnung", "Menge", "PreisVK", "PreisEK" };

			InseratartikelDto[] iaDtos = getInseratFac()
					.inseratartikelFindByInseratIId(flrInserat.getI_id());

			Object[][] dataSubArtikel = new Object[iaDtos.length][fieldnamesArtikel.length];

			for (int i = 0; i < iaDtos.length; i++) {
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(
								iaDtos[i].getArtikelIId(), theClientDto);
				dataSubArtikel[i][0] = artikelDto.getCNr();
				dataSubArtikel[i][1] = artikelDto.formatBezeichnung();
				dataSubArtikel[i][2] = iaDtos[i].getNMenge();
				dataSubArtikel[i][3] = iaDtos[i].getNNettoeinzelpreisVk();
				dataSubArtikel[i][4] = iaDtos[i].getNNettoeinzelpreisEk();

			}

			oZeile[REPORT_OFFENE_SUBREPORT_ARTIKEL] = new LPDatenSubreport(
					dataSubArtikel, fieldnamesArtikel);

			String[] fieldnamesER = new String[] { "Eingangsrechnung" };
			Object[][] dataSub = new Object[flrInserat.getErset().size()][fieldnamesER.length];

			Iterator iterEr = flrInserat.getErset().iterator();
			int u = 0;
			while (iterEr.hasNext()) {
				FLRInserater inseratEr = (FLRInserater) iterEr.next();
				dataSub[u][0] = inseratEr.getFlreingangsrechnung().getC_nr();
				u++;
			}

			oZeile[REPORT_OFFENE_SUBREPORT_EINGANGSRECHNUNG] = new LPDatenSubreport(
					dataSub, fieldnamesER);

			alDaten.add(oZeile);

		}
		session.close();
		Object[][] returnArray = new Object[alDaten.size()][REPORT_OFFENE_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(returnArray);

		initJRDS(parameter, InseratReportFac.REPORT_MODUL,
				InseratReportFac.REPORT_INSERATE_OFFENE,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();

	}

	public Object getFieldValue(JRField jRField) throws JRException {
		Object value = null;
		String fieldName = jRField.getName();
		if (sAktuellerReport.equals(InseratReportFac.REPORT_ZUVERRECHNEN)) {
			if ("Belegnummer".equals(fieldName)) {
				value = data[index][REPORT_ZUVERRECHNEN_BELEGNUMMER];
			} else if ("Bestellnummer".equals(fieldName)) {
				value = data[index][REPORT_ZUVERRECHNEN_BESTELLNUMMER];
			} else if ("DatumErschienen".equals(fieldName)) {
				value = data[index][REPORT_ZUVERRECHNEN_DATUM_ERSCHIENEN];
			} else if ("PersonErschienen".equals(fieldName)) {
				value = data[index][REPORT_ZUVERRECHNEN_PERSON_ERSCHIENEN];
			} else if ("Rubrik".equals(fieldName)) {
				value = data[index][REPORT_ZUVERRECHNEN_RUBRIK];
			} else if ("Rubrik2".equals(fieldName)) {
				value = data[index][REPORT_ZUVERRECHNEN_RUBRIK2];
			} else if ("Stichwort".equals(fieldName)) {
				value = data[index][REPORT_ZUVERRECHNEN_STICHWORT];
			} else if ("Stichwort2".equals(fieldName)) {
				value = data[index][REPORT_ZUVERRECHNEN_STICHWORT2];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ZUVERRECHNEN_BEZEICHNUNG];
			} else if ("Belegdatum".equals(fieldName)) {
				value = data[index][REPORT_ZUVERRECHNEN_BELEGDATUM];
			} else if ("Termin".equals(fieldName)) {
				value = data[index][REPORT_ZUVERRECHNEN_TERMIN];
			} else if ("Medium".equals(fieldName)) {
				value = data[index][REPORT_ZUVERRECHNEN_MEDIUM];
			} else if ("Lieferant".equals(fieldName)) {
				value = data[index][REPORT_ZUVERRECHNEN_LIEFERANT];
			} else if ("Artikel".equals(fieldName)) {
				value = data[index][REPORT_ZUVERRECHNEN_ARTIKEL];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_ZUVERRECHNEN_MENGE];
			} else if ("PreisEK".equals(fieldName)) {
				value = data[index][REPORT_ZUVERRECHNEN_PREIS_EK];
			} else if ("PreisVK".equals(fieldName)) {
				value = data[index][REPORT_ZUVERRECHNEN_PREIS_VK];
			} else if ("Status".equals(fieldName)) {
				value = data[index][REPORT_ZUVERRECHNEN_STATUS];
			} else if ("Vertreter".equals(fieldName)) {
				value = data[index][REPORT_ZUVERRECHNEN_VERTRETER];
			} else if ("SubreportEingangsrechnungen".equals(fieldName)) {
				value = data[index][REPORT_ZUVERRECHNEN_SUBREPORT_EINGANGSRECHNUNG];
			} else if ("SubreportKunden".equals(fieldName)) {
				value = data[index][REPORT_ZUVERRECHNEN_SUBREPORT_KUNDEN];
			} else if ("AnhangLF".equals(fieldName)) {
				value = data[index][REPORT_ZUVERRECHNEN_ANHANG_LF];
			} else if ("AnhangKD".equals(fieldName)) {
				value = data[index][REPORT_ZUVERRECHNEN_ANHANG_KD];
			} else if ("DruckBestellungLF".equals(fieldName)) {
				value = data[index][REPORT_ZUVERRECHNEN_DRUCK_BESTELLUNG_LF];
			} else if ("DruckBestellungKD".equals(fieldName)) {
				value = data[index][REPORT_ZUVERRECHNEN_DRUCK_BESTELLUNG_KD];
			} else if ("DruckRechnungKD".equals(fieldName)) {
				value = data[index][REPORT_ZUVERRECHNEN_DRUCK_RECHNUNG_KD];
			} else if ("SubreportArtikel".equals(fieldName)) {
				value = data[index][REPORT_ZUVERRECHNEN_SUBREPORT_ARTIKEL];
			} else if ("GrundGestoppt".equals(fieldName)) {
				value = data[index][REPORT_ZUVERRECHNEN_GRUND_GESTOPPT];
			} else if ("PersonVerrechenbar".equals(fieldName)) {
				value = data[index][REPORT_ZUVERRECHNEN_PERSON_VERRECHENBAR];
			} else if ("PersonGestoppt".equals(fieldName)) {
				value = data[index][REPORT_ZUVERRECHNEN_PERSON_GESTOPPT];
			}

		} else if (sAktuellerReport
				.equals(InseratReportFac.REPORT_INSERATE_ALLE)) {
			if ("Belegnummer".equals(fieldName)) {
				value = data[index][REPORT_ALLE_BELEGNUMMER];
			} else if ("Bestellnummer".equals(fieldName)) {
				value = data[index][REPORT_ALLE_BESTELLNUMMER];
			} else if ("DatumErschienen".equals(fieldName)) {
				value = data[index][REPORT_ALLE_DATUM_ERSCHIENEN];
			} else if ("PersonErschienen".equals(fieldName)) {
				value = data[index][REPORT_ALLE_PERSON_ERSCHIENEN];
			} else if ("Rubrik".equals(fieldName)) {
				value = data[index][REPORT_ALLE_RUBRIK];
			} else if ("Rubrik2".equals(fieldName)) {
				value = data[index][REPORT_ALLE_RUBRIK2];
			} else if ("Stichwort".equals(fieldName)) {
				value = data[index][REPORT_ALLE_STICHWORT];
			} else if ("Stichwort2".equals(fieldName)) {
				value = data[index][REPORT_ALLE_STICHWORT2];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ALLE_BEZEICHNUNG];
			} else if ("Belegdatum".equals(fieldName)) {
				value = data[index][REPORT_ALLE_BELEGDATUM];
			} else if ("Termin".equals(fieldName)) {
				value = data[index][REPORT_ALLE_TERMIN];
			} else if ("Medium".equals(fieldName)) {
				value = data[index][REPORT_ALLE_MEDIUM];
			} else if ("Lieferant".equals(fieldName)) {
				value = data[index][REPORT_ALLE_LIEFERANT];
			} else if ("Artikel".equals(fieldName)) {
				value = data[index][REPORT_ALLE_ARTIKEL];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_ALLE_MENGE];
			} else if ("PreisEK".equals(fieldName)) {
				value = data[index][REPORT_ALLE_PREIS_EK];
			} else if ("PreisVK".equals(fieldName)) {
				value = data[index][REPORT_ALLE_PREIS_VK];
			} else if ("Status".equals(fieldName)) {
				value = data[index][REPORT_ALLE_STATUS];
			} else if ("Vertreter".equals(fieldName)) {
				value = data[index][REPORT_ALLE_VERTRETER];
			} else if ("SubreportEingangsrechnungen".equals(fieldName)) {
				value = data[index][REPORT_ALLE_SUBREPORT_EINGANGSRECHNUNG];
			} else if ("SubreportKunden".equals(fieldName)) {
				value = data[index][REPORT_ALLE_SUBREPORT_KUNDEN];
			} else if ("AnhangLF".equals(fieldName)) {
				value = data[index][REPORT_ALLE_ANHANG_LF];
			} else if ("AnhangKD".equals(fieldName)) {
				value = data[index][REPORT_ALLE_ANHANG_KD];
			} else if ("DruckBestellungLF".equals(fieldName)) {
				value = data[index][REPORT_ALLE_DRUCK_BESTELLUNG_LF];
			} else if ("DruckBestellungKD".equals(fieldName)) {
				value = data[index][REPORT_ALLE_DRUCK_BESTELLUNG_KD];
			} else if ("DruckRechnungKD".equals(fieldName)) {
				value = data[index][REPORT_ALLE_DRUCK_RECHNUNG_KD];
			} else if ("SubreportArtikel".equals(fieldName)) {
				value = data[index][REPORT_ALLE_SUBREPORT_ARTIKEL];
			} else if ("GrundGestoppt".equals(fieldName)) {
				value = data[index][REPORT_ALLE_GRUND_GESTOPPT];
			} else if ("PersonVerrechenbar".equals(fieldName)) {
				value = data[index][REPORT_ALLE_PERSON_VERRECHENBAR];
			} else if ("PersonGestoppt".equals(fieldName)) {
				value = data[index][REPORT_ALLE_PERSON_GESTOPPT];
			}

		} else if (sAktuellerReport
				.equals(InseratReportFac.REPORT_INSERATE_DBAUSWERTUNG)) {
			if ("Belegnummer".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_BELEGNUMMER];
			} else if ("Bestellnummer".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_BESTELLNUMMER];
			} else if ("DatumErschienen".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_DATUM_ERSCHIENEN];
			} else if ("PersonErschienen".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_PERSON_ERSCHIENEN];
			} else if ("Rubrik".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_RUBRIK];
			} else if ("Rubrik2".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_RUBRIK2];
			} else if ("Stichwort".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_STICHWORT];
			} else if ("Stichwort2".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_STICHWORT2];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_BEZEICHNUNG];
			} else if ("Belegdatum".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_BELEGDATUM];
			} else if ("Termin".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_TERMIN];
			} else if ("Medium".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_MEDIUM];
			} else if ("Lieferant".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_LIEFERANT];
			} else if ("Artikel".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_ARTIKEL];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_MENGE];
			} else if ("PreisEK".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_PREIS_EK];
			} else if ("PreisVK".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_PREIS_VK];
			} else if ("Status".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_STATUS];
			} else if ("Vertreter".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_VERTRETER];
			} else if ("SubreportEingangsrechnungen".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_SUBREPORT_EINGANGSRECHNUNG];
			} else if ("SubreportKunden".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_SUBREPORT_KUNDEN];
			} else if ("AnhangLF".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_ANHANG_LF];
			} else if ("AnhangKD".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_ANHANG_KD];
			} else if ("DruckBestellungLF".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_DRUCK_BESTELLUNG_LF];
			} else if ("DruckBestellungKD".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_DRUCK_BESTELLUNG_KD];
			} else if ("DruckRechnungKD".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_DRUCK_RECHNUNG_KD];
			} else if ("SubreportArtikel".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_SUBREPORT_ARTIKEL];
			} else if ("GrundGestoppt".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_GRUND_GESTOPPT];
			} else if ("PersonVerrechenbar".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_PERSON_VERRECHENBAR];
			} else if ("PersonGestoppt".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_PERSON_GESTOPPT];
			} else if ("VollstaendigBezahlt".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_VOLLSTAENDIG_BEZAHLT];
			} else if ("VollstaendigVerrechnet".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_VOLLSTAENDIG_VERRECHNET];
			} else if ("RechnungKunde".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_KUNDE];
			} else if ("RechnungNummer".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_RECHNUNG_NUMMER];
			} else if ("RechnungStatus".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_RECHNUNG_STATUS];
			} else if ("RechnungDatum".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_RECHNUNGSDATUM];
			} else if ("RechnungZahlungsziel".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_RECHNUNG_ZAHLUNGSZIEL];
			} else if ("RechnungBezahltBetrag".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_RECHNUNGBEZAHLT_BETRAG];
			} else if ("RechnungLetztesZahldatum".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_RECHNUNG_LETZTES_ZAHLDATUM];
			} else if ("ErrechneterWertEK".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_ERRECHNETERWERT_EK];
			} else if ("ErrechneterWertVK".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_ERRECHNETERWERT_VK];
			} else if ("ManuellErledigt".equals(fieldName)) {
				value = data[index][REPORT_DBAUSWERTUNG_MANUELL_ERLEDIGT];
			}

		} else if (sAktuellerReport.equals(InseratReportFac.REPORT_INSERAT)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_INSERAT_ARTIKEL];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_INSERAT_BEZEICHNUNG];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][REPORT_INSERAT_EINHEIT];
			} else if ("Kurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_INSERAT_KURZBEZEICHNUNG];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_INSERAT_MENGE];
			} else if ("NettoeinzelpreisEK".equals(fieldName)) {
				value = data[index][REPORT_INSERAT_NETTOEINZELPREIS_EK];
			} else if ("NettoeinzelpreisVK".equals(fieldName)) {
				value = data[index][REPORT_INSERAT_NETTOEINZELPREIS_VK];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_INSERAT_ZUSATZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_INSERAT_ZUSATZBEZEICHNUNG2];
			}

		} else if (sAktuellerReport
				.equals(InseratReportFac.REPORT_INSERATE_OFFENE)) {
			if ("Belegnummer".equals(fieldName)) {
				value = data[index][REPORT_OFFENE_BELEGNUMMER];
			} else if ("Bestellnummer".equals(fieldName)) {
				value = data[index][REPORT_OFFENE_BESTELLNUMMER];
			} else if ("DatumErschienen".equals(fieldName)) {
				value = data[index][REPORT_OFFENE_DATUM_ERSCHIENEN];
			} else if ("PersonErschienen".equals(fieldName)) {
				value = data[index][REPORT_OFFENE_PERSON_ERSCHIENEN];
			} else if ("Rubrik".equals(fieldName)) {
				value = data[index][REPORT_OFFENE_RUBRIK];
			} else if ("Rubrik2".equals(fieldName)) {
				value = data[index][REPORT_OFFENE_RUBRIK2];
			} else if ("Stichwort".equals(fieldName)) {
				value = data[index][REPORT_OFFENE_STICHWORT];
			} else if ("Stichwort2".equals(fieldName)) {
				value = data[index][REPORT_OFFENE_STICHWORT2];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_OFFENE_BEZEICHNUNG];
			} else if ("Belegdatum".equals(fieldName)) {
				value = data[index][REPORT_OFFENE_BELEGDATUM];
			} else if ("Termin".equals(fieldName)) {
				value = data[index][REPORT_OFFENE_TERMIN];
			} else if ("Medium".equals(fieldName)) {
				value = data[index][REPORT_OFFENE_MEDIUM];
			} else if ("Lieferant".equals(fieldName)) {
				value = data[index][REPORT_OFFENE_LIEFERANT];
			} else if ("Artikel".equals(fieldName)) {
				value = data[index][REPORT_OFFENE_ARTIKEL];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_OFFENE_MENGE];
			} else if ("PreisEK".equals(fieldName)) {
				value = data[index][REPORT_OFFENE_PREIS_EK];
			} else if ("PreisVK".equals(fieldName)) {
				value = data[index][REPORT_OFFENE_PREIS_VK];
			} else if ("Status".equals(fieldName)) {
				value = data[index][REPORT_OFFENE_STATUS];
			} else if ("Vertreter".equals(fieldName)) {
				value = data[index][REPORT_OFFENE_VERTRETER];
			} else if ("SubreportEingangsrechnungen".equals(fieldName)) {
				value = data[index][REPORT_OFFENE_SUBREPORT_EINGANGSRECHNUNG];
			} else if ("SubreportKunden".equals(fieldName)) {
				value = data[index][REPORT_OFFENE_SUBREPORT_KUNDEN];
			} else if ("AnhangLF".equals(fieldName)) {
				value = data[index][REPORT_OFFENE_ANHANG_LF];
			} else if ("AnhangKD".equals(fieldName)) {
				value = data[index][REPORT_OFFENE_ANHANG_KD];
			} else if ("DruckBestellungLF".equals(fieldName)) {
				value = data[index][REPORT_OFFENE_DRUCK_BESTELLUNG_LF];
			} else if ("DruckBestellungKD".equals(fieldName)) {
				value = data[index][REPORT_OFFENE_DRUCK_BESTELLUNG_KD];
			} else if ("DruckRechnungKD".equals(fieldName)) {
				value = data[index][REPORT_OFFENE_DRUCK_RECHNUNG_KD];
			} else if ("SubreportArtikel".equals(fieldName)) {
				value = data[index][REPORT_OFFENE_SUBREPORT_ARTIKEL];
			} else if ("GrundGestoppt".equals(fieldName)) {
				value = data[index][REPORT_OFFENE_GRUND_GESTOPPT];
			} else if ("PersonVerrechenbar".equals(fieldName)) {
				value = data[index][REPORT_OFFENE_PERSON_VERRECHENBAR];
			} else if ("PersonGestoppt".equals(fieldName)) {
				value = data[index][REPORT_OFFENE_PERSON_GESTOPPT];
			}

		}

		return value;
	}

	public boolean next() throws JRException {
		index++;
		return (index < data.length);
	}

}
