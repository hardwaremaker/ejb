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
package com.lp.server.projekt.ejbfac;

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
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.GeometrieDto;
import com.lp.server.artikel.service.LagerabgangursprungDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.artikel.service.VerpackungDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragNachkalkulationDto;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.auftrag.service.AuftragzeitenDto;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungAuftragszuordnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosablieferungDto;
import com.lp.server.fertigung.service.LosistmaterialDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.fertigung.service.ReportLosnachkalkulationDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.fastlanereader.generated.FLRTelefonzeiten;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.PersonalgehaltDto;
import com.lp.server.personal.service.ReiseDto;
import com.lp.server.personal.service.TelefonzeitenDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.projekt.fastlanereader.ProjektHandler;
import com.lp.server.projekt.fastlanereader.ProjektverlaufHandler;
import com.lp.server.projekt.fastlanereader.generated.FLRHistory;
import com.lp.server.projekt.fastlanereader.generated.FLRProjekt;
import com.lp.server.projekt.service.BereichDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.projekt.service.ProjektFac;
import com.lp.server.projekt.service.ProjektReportFac;
import com.lp.server.projekt.service.ProjektServiceFac;
import com.lp.server.projekt.service.ProjektVerlaufHelperDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungartDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.service.BelegartDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.server.util.report.TimingInterceptor;
import com.lp.util.AufgeloesteFehlmengenDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;

@Stateless
@Interceptors(TimingInterceptor.class)
public class ProjektReportFacBean extends LPReport implements ProjektReportFac,
		JRDataSource {

	@PersistenceContext
	private EntityManager em;

	private String cAktuellerReport = null;
	private Object[][] data = null;

	public JasperPrintLP printProjektverlauf(Integer projektIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		cAktuellerReport = ProjektReportFac.REPORT_PROJEKTVERLAUF;

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		ProjektDto projektDto = getProjektFac().projektFindByPrimaryKey(
				projektIId);

		LinkedHashMap<String, ProjektVerlaufHelperDto> hm = getProjektFac()
				.getProjektVerlauf(projektIId, theClientDto);

		PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(
				projektDto.getPartnerIId(), theClientDto);
		MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
				projektDto.getMandantCNr(), theClientDto);

		Locale locDruck = Helper.string2Locale(partnerDto
				.getLocaleCNrKommunikation());

		parameter.put("P_PROJEKTNUMMER", projektDto.getCNr());
		parameter.put("P_TITEL", projektDto.getCTitel());
		parameter.put(
				"P_KUNDE_ADRESSBLOCK",
				formatAdresseFuerAusdruck(partnerDto, null, mandantDto,
						locDruck));

		parameter.put("P_MANDANTENWAEHRUNG",
				theClientDto.getSMandantenwaehrung());

		ArrayList alDaten = new ArrayList();

		Iterator<String> it = hm.keySet().iterator();

		boolean bProjektHinzugefuegt = false;

		Object[] oZeileProjekt = new Object[ProjektReportFac.REPORT_PROJEKTVERLAUF_ANZAHL_SPALTEN];
		oZeileProjekt[ProjektReportFac.REPORT_PROJEKTVERLAUF_BELEGART] = LocaleFac.BELEGART_PROJEKT;
		oZeileProjekt[ProjektReportFac.REPORT_PROJEKTVERLAUF_BELEGNUMMER] = projektDto
				.getCNr();
		oZeileProjekt[ProjektReportFac.REPORT_PROJEKTVERLAUF_EBENE] = 0;
		oZeileProjekt[ProjektReportFac.REPORT_PROJEKTVERLAUF_STATUS] = projektDto
				.getStatusCNr();

		oZeileProjekt = befuelleZeileProjektverlaufMitZeitdaten(
				LocaleFac.BELEGART_PROJEKT, projektDto.getIId(), oZeileProjekt,
				theClientDto);

		alDaten.add(oZeileProjekt);

		bProjektHinzugefuegt = true;

		while (it.hasNext()) {
			ProjektVerlaufHelperDto belegDto = hm.get(it.next());

			String einrueckung = "";
			for (int i = 0; i < belegDto.getiEbene(); i++) {
				einrueckung = einrueckung + "   ";
			}

			String belegart = "Unbekannt";
			String belegnummer = "Unbekannt";
			Integer belegIId = null;

			String status = null;

			java.util.Date belegdatum = null;

			BigDecimal bdVKMaterialwert = null;
			BigDecimal bdVKAZWert = null;

			BigDecimal bdEKMaterialwert = null;
			BigDecimal bdEKAZWert = null;

			BigDecimal bdGestMaterialwert = null;
			BigDecimal bdGestAZWert = null;

			Object[] oZeile = new Object[ProjektReportFac.REPORT_PROJEKTVERLAUF_ANZAHL_SPALTEN];

			if (belegDto.getBelegDto() instanceof AngebotDto) {
				AngebotDto dto = (AngebotDto) belegDto.getBelegDto();
				belegart = LocaleFac.BELEGART_ANGEBOT;
				belegnummer = dto.getCNr();
				belegIId = dto.getIId();

				bdGestMaterialwert = getAngebotFac()
						.berechneGestehungswertSoll(dto.getIId(),
								ArtikelFac.ARTIKELART_ARTIKEL, true,
								theClientDto);
				bdGestAZWert = getAngebotFac().berechneGestehungswertSoll(
						dto.getIId(), ArtikelFac.ARTIKELART_ARBEITSZEIT, true,
						theClientDto);

				bdVKMaterialwert = getAngebotFac().berechneVerkaufswertSoll(
						dto.getIId(), ArtikelFac.ARTIKELART_ARTIKEL,
						theClientDto);
				bdVKAZWert = getAngebotFac().berechneVerkaufswertSoll(
						dto.getIId(), ArtikelFac.ARTIKELART_ARBEITSZEIT,
						theClientDto);
				belegdatum = dto.getTBelegdatum();

				status = dto.getStatusCNr();

			} else if (belegDto.getBelegDto() instanceof AuftragDto) {
				AuftragDto dto = (AuftragDto) belegDto.getBelegDto();
				belegart = LocaleFac.BELEGART_AUFTRAG;
				belegnummer = dto.getCNr();
				belegIId = dto.getIId();

				bdGestMaterialwert = getAuftragFac()
						.berechneGestehungswertSoll(dto.getIId(),
								ArtikelFac.ARTIKELART_ARTIKEL, true,
								theClientDto);
				bdGestAZWert = getAuftragFac().berechneGestehungswertSoll(
						dto.getIId(), ArtikelFac.ARTIKELART_ARBEITSZEIT, true,
						theClientDto);

				bdVKMaterialwert = getAuftragFac().berechneVerkaufswertSoll(
						dto.getIId(), ArtikelFac.ARTIKELART_ARTIKEL,
						theClientDto);
				bdVKAZWert = getAuftragFac().berechneVerkaufswertSoll(
						dto.getIId(), ArtikelFac.ARTIKELART_ARBEITSZEIT,
						theClientDto);
				belegdatum = dto.getTBelegdatum();

				status = dto.getStatusCNr();

				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_AUFTRAG_BESTELLNUMMER] = dto
						.getCBestellnummer();
				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_LIEFERTERMIN] = dto
						.getDLiefertermin();

			} else if (belegDto.getBelegDto() instanceof LieferscheinDto) {
				LieferscheinDto dto = (LieferscheinDto) belegDto.getBelegDto();
				belegart = LocaleFac.BELEGART_LIEFERSCHEIN;
				belegnummer = dto.getCNr();

				bdGestMaterialwert = getLieferscheinFac()
						.berechneGestehungswertIst(dto.getIId(), null,
								ArtikelFac.ARTIKELART_ARTIKEL, theClientDto);
				bdGestAZWert = getLieferscheinFac().berechneGestehungswertIst(
						dto.getIId(), null, ArtikelFac.ARTIKELART_ARBEITSZEIT,
						theClientDto);

				bdVKMaterialwert = getLieferscheinFac()
						.berechneVerkaufswertIst(dto.getIId(), null,
								ArtikelFac.ARTIKELART_ARTIKEL, theClientDto);
				bdVKAZWert = getLieferscheinFac().berechneVerkaufswertIst(
						dto.getIId(), null, ArtikelFac.ARTIKELART_ARBEITSZEIT,
						theClientDto);

				bdEKMaterialwert = getLagerFac().getEinstandsWertEinesBeleges(
						LocaleFac.BELEGART_LIEFERSCHEIN, dto.getIId(),
						ArtikelFac.ARTIKELART_ARTIKEL, theClientDto);

				bdEKAZWert = getLagerFac().getEinstandsWertEinesBeleges(
						LocaleFac.BELEGART_LIEFERSCHEIN, dto.getIId(),
						ArtikelFac.ARTIKELART_ARBEITSZEIT, theClientDto);

				belegdatum = dto.getTBelegdatum();

				status = dto.getStatusCNr();

			} else if (belegDto.getBelegDto() instanceof RechnungDto) {
				RechnungDto dto = (RechnungDto) belegDto.getBelegDto();
				belegart = dto.getRechnungartCNr();
				belegnummer = dto.getCNr();

				boolean bGutschrift = false;

				RechnungartDto raDto = getRechnungServiceFac()
						.rechnungartFindByPrimaryKey(dto.getRechnungartCNr(),
								theClientDto);

				if (raDto.getRechnungtypCNr().equals(
						RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
					bGutschrift = true;
				}

				bdGestMaterialwert = getRechnungFac()
						.berechneGestehungswertIst(dto.getIId(),
								ArtikelFac.ARTIKELART_ARTIKEL, theClientDto);
				bdGestAZWert = getRechnungFac().berechneGestehungswertIst(
						dto.getIId(), ArtikelFac.ARTIKELART_ARBEITSZEIT,
						theClientDto);

				bdVKMaterialwert = getRechnungFac().berechneVerkaufswertIst(
						dto.getIId(), ArtikelFac.ARTIKELART_ARTIKEL,
						theClientDto);
				bdVKAZWert = getRechnungFac().berechneVerkaufswertIst(
						dto.getIId(), ArtikelFac.ARTIKELART_ARBEITSZEIT,
						theClientDto);

				bdEKMaterialwert = getLagerFac().getEinstandsWertEinesBeleges(
						bGutschrift ? LocaleFac.BELEGART_GUTSCHRIFT
								: LocaleFac.BELEGART_RECHNUNG, dto.getIId(),
						ArtikelFac.ARTIKELART_ARTIKEL, theClientDto);

				bdEKAZWert = getLagerFac().getEinstandsWertEinesBeleges(
						bGutschrift ? LocaleFac.BELEGART_GUTSCHRIFT
								: LocaleFac.BELEGART_RECHNUNG, dto.getIId(),
						ArtikelFac.ARTIKELART_ARBEITSZEIT, theClientDto);

				if (bGutschrift == true) {
					if (bdGestMaterialwert != null) {
						bdGestMaterialwert = bdGestMaterialwert.negate();
					}
					if (bdGestAZWert != null) {
						bdGestAZWert = bdGestAZWert.negate();
					}
					if (bdVKMaterialwert != null) {
						bdVKMaterialwert = bdVKMaterialwert.negate();
					}
					if (bdVKAZWert != null) {
						bdVKAZWert = bdVKAZWert.negate();
					}
				}

				belegdatum = dto.getTBelegdatum();

				status = dto.getStatusCNr();

			} else if (belegDto.getBelegDto() instanceof BestellungDto) {
				BestellungDto dto = (BestellungDto) belegDto.getBelegDto();
				belegart = LocaleFac.BELEGART_BESTELLUNG;
				belegnummer = dto.getCNr();

				bdEKMaterialwert = getBestellungFac().berechneEinkaufswertIst(
						dto.getIId(), ArtikelFac.ARTIKELART_ARTIKEL,
						theClientDto);
				bdEKAZWert = getBestellungFac().berechneEinkaufswertIst(
						dto.getIId(), ArtikelFac.ARTIKELART_ARBEITSZEIT,
						theClientDto);
				belegdatum = dto.getDBelegdatum();

				status = dto.getStatusCNr();

				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_LIEFERANT] = getLieferantFac()
						.lieferantFindByPrimaryKey(
								dto.getLieferantIIdBestelladresse(),
								theClientDto).getPartnerDto()
						.formatFixName1Name2();

				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_LIEFERTERMIN] = dto
						.getDLiefertermin();

			} else if (belegDto.getBelegDto() instanceof AnfrageDto) {
				AnfrageDto dto = (AnfrageDto) belegDto.getBelegDto();
				belegart = LocaleFac.BELEGART_ANFRAGE;
				belegnummer = dto.getCNr();

				bdEKMaterialwert = getAnfrageFac().berechneEinkaufswertIst(
						dto.getIId(), ArtikelFac.ARTIKELART_ARTIKEL,
						theClientDto);
				bdEKAZWert = getBestellungFac().berechneEinkaufswertIst(
						dto.getIId(), ArtikelFac.ARTIKELART_ARBEITSZEIT,
						theClientDto);

				belegdatum = dto.getTBelegdatum();

				status = dto.getStatusCNr();

				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_LIEFERANT] = getLieferantFac()
						.lieferantFindByPrimaryKey(
								dto.getLieferantIIdAnfrageadresse(),
								theClientDto).getPartnerDto()
						.formatFixName1Name2();

			} else if (belegDto.getBelegDto() instanceof LosDto) {
				LosDto dto = (LosDto) belegDto.getBelegDto();
				belegart = LocaleFac.BELEGART_LOS;
				belegnummer = dto.getCNr();
				belegIId = dto.getIId();

				status = dto.getStatusCNr();

				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_LOS_BEGINN] = dto
						.getTProduktionsbeginn();
				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_LOS_ENDE] = dto
						.getTProduktionsende();

				BigDecimal ablMenge = new BigDecimal(0);
				BigDecimal ablWert = new BigDecimal(0);
				LosablieferungDto[] ablDtos = getFertigungFac()
						.losablieferungFindByLosIId(dto.getIId(), true,
								theClientDto);
				for (int i = 0; i < ablDtos.length; i++) {
					ablMenge = ablMenge.add(ablDtos[i].getNMenge());
					ablWert = ablWert.add(ablDtos[i].getNMaterialwert()
							.multiply(ablDtos[i].getNMenge()));
				}

				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_MATERIALWERT_LOS] = ablWert;
				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_LOS_ABGELIEFERTE_MENGE] = ablMenge;

				bdEKMaterialwert = getLagerFac().getEinstandsWertEinesBeleges(
						LocaleFac.BELEGART_LOS, dto.getIId(),
						ArtikelFac.ARTIKELART_ARTIKEL, theClientDto);

				// AZ
				AuftragzeitenDto[] belegzeitenDtos = getZeiterfassungFac()
						.getAllZeitenEinesBeleges(LocaleFac.BELEGART_LOS,
								dto.getIId(), null, null, null, null, true,
								false, false, theClientDto);
				BigDecimal bdKostenGesamt = BigDecimal.ZERO;

				for (int i = 0; i < belegzeitenDtos.length; i++) {
					bdKostenGesamt = bdKostenGesamt.add(belegzeitenDtos[i]
							.getBdKosten());
				}

				bdEKAZWert = bdKostenGesamt;

			} else if (belegDto.getBelegDto() instanceof ReiseDto) {
				ReiseDto reiseDto = (ReiseDto) belegDto.getBelegDto();

				belegart = "Reise";
				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_REISE_KOMMENTAR] = reiseDto
						.getCKommentar();
				bdGestMaterialwert = reiseDto.getNKostenDesAbschnitts();

			} else if (belegDto.getBelegDto() instanceof EingangsrechnungAuftragszuordnungDto) {
				EingangsrechnungAuftragszuordnungDto eaDto = (EingangsrechnungAuftragszuordnungDto) belegDto
						.getBelegDto();
				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_ER_KEINE_AUFTRAGSWERTUNG] = Helper
						.short2Boolean(eaDto.getBKeineAuftragswertung());

				EingangsrechnungDto erDto = getEingangsrechnungFac()
						.eingangsrechnungFindByPrimaryKey(
								eaDto.getEingangsrechnungIId());

				belegart = LocaleFac.BELEGART_EINGANGSRECHNUNG;
				belegnummer = erDto.getCNr();

				bdGestMaterialwert = getLocaleFac()
						.rechneUmInMandantenWaehrung(eaDto.getNBetrag(),
								erDto.getNKurs());

			} else if (belegDto.getBelegDto() instanceof AgstklDto) {
				AgstklDto agstklDto = (AgstklDto) belegDto.getBelegDto();

				belegart = LocaleFac.BELEGART_AGSTUECKLISTE;
				belegnummer = agstklDto.getCNr();

			} else if (belegDto.getBelegDto() instanceof TelefonzeitenDto) {
				TelefonzeitenDto telefonzeitenDto = (TelefonzeitenDto) belegDto
						.getBelegDto();
				belegart = "Telefon";

				// Die Kosten kommen aus dem Stundensatz
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(telefonzeitenDto.getTVon().getTime());

				PersonalgehaltDto pgDto = getPersonalFac()
						.personalgehaltFindLetztePersonalgehalt(
								telefonzeitenDto.getPersonalIId(),
								c.get(Calendar.YEAR), c.get(Calendar.MONTH));

				if (telefonzeitenDto.getTBis() != null) {
					Double dauer = new Double(((double) (telefonzeitenDto
							.getTBis().getTime() - telefonzeitenDto.getTVon()
							.getTime()) / 3600000));

					oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_AZ_DAUER] = dauer;

					if (pgDto != null && pgDto.getNStundensatz() != null) {

						oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_AZ_KOSTEN] = pgDto
								.getNStundensatz().multiply(
										new BigDecimal(dauer));

					}
				}

				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_TELEFON_KOMMENTAR_EXTERN] = telefonzeitenDto
						.getXKommentarext();
				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_TELEFON_KOMMENTAR_INTERN] = telefonzeitenDto
						.getXKommentarint();

			}

			oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_BELEGDATUM] = belegdatum;

			oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_BELEGART] = belegart;
			oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_BELEGNUMMER] = belegnummer;
			oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_EBENE] = belegDto
					.getiEbene();

			oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_VKWERT_MATERIAL] = bdVKMaterialwert;
			oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_VKWERT_AZ] = bdVKAZWert;

			oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_GESTWERT_MATERIAL] = bdGestMaterialwert;
			oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_GESTWERT_AZ] = bdGestAZWert;

			oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_EKWERT_MATERIAL] = bdEKMaterialwert;
			oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_EKWERT_AZ] = bdEKAZWert;

			oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_STATUS] = status;

			oZeile = befuelleZeileProjektverlaufMitZeitdaten(belegart,
					belegIId, oZeile, theClientDto);

			if (belegDto.getBelegDto() instanceof LosDto) {
				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_AZWERT_LOS] = new BigDecimal(
						((BigDecimal) oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_AZ_KOSTEN])
								.doubleValue());
			}

			alDaten.add(oZeile);

			if (belegDto.getBelegDto() instanceof LieferscheinDto) {

				LieferscheinDto dto = (LieferscheinDto) belegDto.getBelegDto();

				LieferscheinpositionDto[] lsposDtos = getLieferscheinpositionFac()
						.lieferscheinpositionFindByLieferscheinIId(dto.getIId());

				for (int i = 0; i < lsposDtos.length; i++) {

					alDaten = losablieferungHinzufuegen(belegart,
							lsposDtos[i].getIId(), theClientDto,
							lsposDtos[i].getNMenge(), belegDto.getiEbene() + 1,
							alDaten);
				}
			}

		}

		Object[][] returnArray = new Object[alDaten.size()][ProjektReportFac.REPORT_PROJEKTVERLAUF_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(returnArray);

		initJRDS(parameter, ProjektReportFac.REPORT_MODUL,
				ProjektReportFac.REPORT_PROJEKTVERLAUF,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	private ArrayList losablieferungHinzufuegen(String belegartCNr,
			Integer belegpositionIId, TheClientDto theClientDto,
			BigDecimal bdVerbrauchteMenge, int iEbene, ArrayList alDaten)
			throws RemoteException {
		// PJ18623

		BigDecimal bdLosanteilImLieferschein = new BigDecimal(0);
		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "select distinct lagerbewegung.i_id_buchung from FLRLagerbewegung lagerbewegung WHERE lagerbewegung.c_belegartnr='"
				+ belegartCNr
				+ "' AND lagerbewegung.i_belegartpositionid="
				+ belegpositionIId;

		Query inventurliste = session.createQuery(sQuery);
		List<?> resultList = inventurliste.list();
		Iterator<?> resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {
			Integer o = (Integer) resultListIterator.next();
			LagerabgangursprungDto[] dtos = getLagerFac()
					.lagerabgangursprungFindByLagerbewegungIIdBuchung(o);

			// Fuer jeden Lagerabgangs- Ursprung, der aus einem Los
			// kommt, einen zusaetzlichen eintrag anlegen
			for (int j = 0; j < dtos.length; j++) {
				// aber nur wenn verbrauchte menge grosser 0
				LagerabgangursprungDto dto = dtos[j];
				if (dto.getNVerbrauchtemenge().doubleValue() != 0) {
					Session session2 = FLRSessionFactory.getFactory()
							.openSession();
					String sQuery2 = "from FLRLagerbewegung lagerbewegung WHERE lagerbewegung.i_id_buchung="
							+ dtos[j].getILagerbewegungidursprung()
							+ " AND lagerbewegung.b_historie=0 order by lagerbewegung.t_buchungszeit DESC";
					Query ursrungsbuchung = session2.createQuery(sQuery2);
					ursrungsbuchung.setMaxResults(1);

					List<?> resultList2 = ursrungsbuchung.list();

					com.lp.server.artikel.fastlanereader.generated.FLRLagerbewegung lagerbewegung_ursprung = (com.lp.server.artikel.fastlanereader.generated.FLRLagerbewegung) resultList2
							.iterator().next();

					if (lagerbewegung_ursprung.getC_belegartnr().equals(
							LocaleFac.BELEGART_LOSABLIEFERUNG)) {

						LosablieferungDto losablieferungDto = getFertigungFac()
								.losablieferungFindByPrimaryKey(
										lagerbewegung_ursprung
												.getI_belegartpositionid(),
										true, theClientDto);

						LosDto losDto = getFertigungFac().losFindByPrimaryKey(
								lagerbewegung_ursprung.getI_belegartid());
						// Neuer Eintrag

						Object[] oZeileLosanteil = new Object[ProjektReportFac.REPORT_PROJEKTVERLAUF_ANZAHL_SPALTEN];

						oZeileLosanteil[ProjektReportFac.REPORT_PROJEKTVERLAUF_LOSANTEIL_LIEFERSCHEIN_LOSNUMMER] = losDto
								.getCNr();

						oZeileLosanteil[ProjektReportFac.REPORT_PROJEKTVERLAUF_EBENE] = iEbene;
						BigDecimal gestWertArbeitIst = losablieferungDto
								.getNArbeitszeitwertdetailliert().multiply(
										dto.getNVerbrauchtemenge());

						BigDecimal gestWertMaterialIst = losablieferungDto
								.getNMaterialwertdetailliert().multiply(
										dto.getNVerbrauchtemenge());

						oZeileLosanteil[ProjektReportFac.REPORT_PROJEKTVERLAUF_LOSANTEIL_LIEFERSCHEIN_EINSTANDSWERT_MATERIAL] = gestWertMaterialIst;

						bdLosanteilImLieferschein = bdLosanteilImLieferschein
								.add(gestWertArbeitIst)
								.add(gestWertMaterialIst);

						BigDecimal gesamtAbgeliefert = getFertigungFac()
								.getErledigteMenge(losDto.getIId(),
										theClientDto);

						AuftragzeitenDto[] azDtos = getZeiterfassungFac()
								.getAllZeitenEinesBeleges(
										LocaleFac.BELEGART_LOS,
										losDto.getIId(), null, null, null,
										null, true, false, theClientDto);

						BigDecimal summeKosten = BigDecimal.ZERO;
						for (int i = 0; i < azDtos.length; i++) {
							summeKosten = summeKosten.add(azDtos[i]
									.getBdKosten());
						}

						oZeileLosanteil[ProjektReportFac.REPORT_PROJEKTVERLAUF_LOSANTEIL_LIEFERSCHEIN_EINSTANDSWERT_AZ] = new BigDecimal(
								summeKosten.doubleValue()
										/ gesamtAbgeliefert.doubleValue()
										* dto.getNVerbrauchtemenge()
												.doubleValue());

						BigDecimal einstandswert = BigDecimal.ZERO;

						LossollmaterialDto[] sollMatDtos = getFertigungFac()
								.lossollmaterialFindByLosIId(
										losablieferungDto.getLosIId());
						for (int i = 0; i < sollMatDtos.length; i++) {
							LosistmaterialDto[] istmatDto = getFertigungFac()
									.losistmaterialFindByLossollmaterialIId(
											sollMatDtos[i].getIId());

							for (int k = 0; k < istmatDto.length; k++) {

								// Wert

								List<SeriennrChargennrMitMengeDto> snrs = getLagerFac()
										.getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(
												LocaleFac.BELEGART_LOS,
												istmatDto[k].getIId());

								for (int m = 0; m < snrs.size(); m++) {

									BigDecimal bdWEinstandswertZeile = getLagerFac()
											.getEinstandspreis(
													LocaleFac.BELEGART_LOS,
													istmatDto[k].getIId(),
													snrs.get(m)
															.getCSeriennrChargennr());
									if (bdWEinstandswertZeile != null) {
										einstandswert = einstandswert
												.add(bdWEinstandswertZeile);
									}

								}
							}

						}

						oZeileLosanteil[ProjektReportFac.REPORT_PROJEKTVERLAUF_LOSANTEIL_LIEFERSCHEIN_EINSTANDSWERT_MATERIAL] = new BigDecimal(
								einstandswert.doubleValue()
										/ gesamtAbgeliefert.doubleValue()
										* dto.getNVerbrauchtemenge()
												.doubleValue());

						alDaten.add(oZeileLosanteil);

						for (int i = 0; i < sollMatDtos.length; i++) {
							LosistmaterialDto[] istmatDto = getFertigungFac()
									.losistmaterialFindByLossollmaterialIId(
											sollMatDtos[i].getIId());

							for (int k = 0; k < istmatDto.length; k++) {
								// Wenn istmaterial aus Los kommt
								losablieferungHinzufuegen(
										LocaleFac.BELEGART_LOS,
										istmatDto[k].getIId(), theClientDto,
										istmatDto[k].getNMenge(), iEbene + 1,
										alDaten);

								// Wert
								getLagerFac().getEinstandspreis(
										LocaleFac.BELEGART_LOS,
										istmatDto[k].getIId(), null);

							}

						}

						// ev rekursiv aufrufen

					}
				}
			}

		}

		session.close();

		return alDaten;
	}

	private Object[] befuelleZeileProjektverlaufMitZeitdaten(String belegart,
			Integer belegIId, Object[] oZeile, TheClientDto theClientDto) {

		if (belegart != null && belegIId != null) {
			AuftragzeitenDto[] azDtos = getZeiterfassungFac()
					.getAllZeitenEinesBeleges(belegart, belegIId, null, null,
							null, null, true, false, theClientDto);

			double zeiten = 0;
			BigDecimal bdKosten = new BigDecimal(0);
			for (int i = 0; i < azDtos.length; i++) {
				if (azDtos[i] != null && azDtos[i].getDdDauer() != null) {
					zeiten = zeiten + azDtos[i].getDdDauer().doubleValue();
				}

				if (azDtos[i] != null && azDtos[i].getBdKosten() != null) {
					bdKosten = bdKosten.add(azDtos[i].getBdKosten());
				}

			}

			oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_AZ_DAUER] = new Double(
					zeiten);
			oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_AZ_KOSTEN] = bdKosten;
		}
		return oZeile;
	}

	public JasperPrintLP printProjekt(Integer iIdProjektI,
			Integer iAnzahlKopienI, Boolean bMitLogo, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		JasperPrintLP aJasperPrint = null;

		try {
			ProjektDto projektDto = getProjektFac().projektFindByPrimaryKey(
					iIdProjektI);
			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(
					projektDto.getPartnerIId(), theClientDto);
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					projektDto.getMandantCNr(), theClientDto);
			cAktuellerReport = ProjektReportFac.REPORT_PROJEKT;
			// dem Report seine Parameter setzen
			HashMap<String, Object> parameter = new HashMap<String, Object>();

			Locale locDruck = Helper.string2Locale(partnerDto
					.getLocaleCNrKommunikation());

			if (partnerDto.getPartnerklasseIId() != null) {
				parameter.put(
						"P_PARTNERKLASSE",
						getPartnerFac().partnerklasseFindByPrimaryKey(
								partnerDto.getPartnerklasseIId(), theClientDto)
								.getBezeichnung());
			}

			parameter.put("P_BEREICH", getProjektServiceFac()
					.bereichFindByPrimaryKey(projektDto.getBereichIId())
					.getCBez());

			if (partnerDto.getBrancheIId() != null) {
				parameter.put(
						"P_BRANCHE",
						getPartnerServicesFac().brancheFindByPrimaryKey(
								partnerDto.getBrancheIId(), theClientDto)
								.getBezeichnung());
			}
			parameter.put("P_MANDANTADRESSE",
					Helper.formatMandantAdresse(mandantDto));

			parameter.put("P_TITEL", projektDto.getCTitel());
			parameter.put("P_PROJEKTNUMMER", projektDto.getCNr());
			parameter
					.put("P_KOPFTEXT", Helper
							.formatStyledTextForJasper(projektDto
									.getXFreetext()));
			parameter.put("P_KATEGORIE", projektDto.getKategorieCNr());

			parameter.put("P_WAHRSCHEINLICHKEIT",
					projektDto.getIWahrscheinlichkeit());
			parameter.put("P_UMSATZGEPLANT", projektDto.getNUmsatzgeplant());

			parameter
					.put("P_ZIELTERMIN",
							""
									+ Helper.formatDatum(
											projektDto.getTZielwunschdatum(),
											locDruck));
			parameter.put(
					"P_ERLEDIGUNGSDATUM",
					""
							+ Helper.formatDatum(projektDto.getTErledigt(),
									locDruck));
			parameter
					.put("P_BELEGDATUM",
							""
									+ Helper.formatDatum(
											projektDto.getTAnlegen(), locDruck));
			parameter.put(
					"P_KUNDE_ADRESSBLOCK",
					formatAdresseFuerAusdruck(partnerDto, null, mandantDto,
							locDruck));

			if (projektDto.getAnsprechpartnerIId() != null) {
				AnsprechpartnerDto ansprechpartnerDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(
								projektDto.getAnsprechpartnerIId(),
								theClientDto);
				parameter.put(LPReport.P_ANSPRECHPARTNER, ansprechpartnerDto
						.getPartnerDto().formatFixName1Name2());

				parameter.put(LPReport.P_ANSPRECHPARTNERHANDY,
						ansprechpartnerDto.getCHandy());

				parameter.put(LPReport.P_ANSPRECHPARTNERDW,
						ansprechpartnerDto.getCTelefon());

				parameter.put(LPReport.P_ANSPRECHPARTNEREMAIL,
						ansprechpartnerDto.getCEmail());

				parameter.put(LPReport.P_ANSPRECHPARTNERFAX,
						ansprechpartnerDto.getCDirektfax());

				String sTelefon = getPartnerFac()
						.partnerkommFindRespectPartnerAsStringOhneExec(
								ansprechpartnerDto.getIId(), partnerDto,
								PartnerFac.KOMMUNIKATIONSART_TELEFON,
								theClientDto.getMandant(), theClientDto);

				parameter.put(LPReport.P_ANSPRECHPARTNERTELEFON,
						sTelefon != null ? sTelefon : "");

			}
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				// Hiberante Criteria fuer alle Tabellen ausgehend von meiner
				// Haupttabelle anlegen,
				// nach denen ich filtern und sortieren kann
				Criteria crit = session.createCriteria(FLRHistory.class);

				crit.createCriteria(ProjektFac.FLR_HISTORY_FLRPROJEKT).add(
						Restrictions.eq(ProjektFac.FLR_PROJEKT_I_ID,
								projektDto.getIId()));
				crit.addOrder(Order.desc(ProjektFac.FLR_HISTORY_T_BELEGDATUM));
				List<?> resultList = crit.list();
				if (resultList.size() == 0) {
					data = new Object[1][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_ANZAHL_SPALTEN];
					parameter.put("P_OHNEPOSITION", new Boolean(true));
				} else {
					parameter.put("P_OHNEPOSITION", new Boolean(false));
					Iterator<?> it = resultList.iterator();
					int i = 0;
					data = new Object[resultList.size()][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_ANZAHL_SPALTEN];
					while (it.hasNext()) {
						FLRHistory history = (FLRHistory) it.next();
						data[i][ProjektReportFac.REPORT_PROJEKT_POSITION] = i;
						data[i][ProjektReportFac.REPORT_PROJEKT_ERZEUGER] = history
								.getFlrpersonal().getFlrpartner()
								.getC_name1nachnamefirmazeile1();
						data[i][ProjektReportFac.REPORT_PROJEKT_HISTORY_BELEGDATUM] = Helper
								.formatDatum(history.getT_belegdatum(),
										locDruck);
						data[i][ProjektReportFac.REPORT_PROJEKT_HISTORY_TEXT] = history
								.getX_text();
						data[i][ProjektReportFac.REPORT_PROJEKT_TITEL] = history
								.getC_titel();

						if (history.getFlrhistoryart() != null) {
							data[i][ProjektReportFac.REPORT_PROJEKT_HISTORYART] = history
									.getFlrhistoryart().getC_bez();

							data[i][ProjektReportFac.REPORT_PROJEKT_ROT] = history
									.getFlrhistoryart().getI_rot();
							data[i][ProjektReportFac.REPORT_PROJEKT_BLAU] = history
									.getFlrhistoryart().getI_blau();
							data[i][ProjektReportFac.REPORT_PROJEKT_GRUEN] = history
									.getFlrhistoryart().getI_gruen();
						}
						i++;
					}
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

			initJRDS(parameter, ProjektReportFac.REPORT_MODUL,
					cAktuellerReport, theClientDto.getMandant(), locDruck,
					theClientDto);

			// SP2599 Bild anhaengen

			ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();

			// Bild einfuegen
			if (projektDto.getOAttachments() != null
					&& projektDto.getCAttachmentsType() != null) {

				if (projektDto.getCAttachmentsType().equals(
						MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
						|| projektDto.getCAttachmentsType().equals(
								MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)
						|| projektDto.getCAttachmentsType().equals(
								MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)) {
					byte[] bild = projektDto.getOAttachments();

					images.add(Helper.byteArrayToImage(bild));

				} else if (projektDto.getCAttachmentsType().equals(
						MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF)) {

					byte[] bild = projektDto.getOAttachments();

					java.awt.image.BufferedImage[] tiffs = Helper
							.tiffToImageArray(bild);
					if (tiffs != null) {
						for (int k = 0; k < tiffs.length; k++) {
							images.add(tiffs[k]);
						}
					}

				} else if (projektDto.getCAttachmentsType().equals(
						MediaFac.DATENFORMAT_MIMETYPE_APP_PDF)) {

					byte[] pdf = projektDto.getOAttachments();

					PDDocument document = null;

					try {

						InputStream myInputStream = new ByteArrayInputStream(
								pdf);

						document = PDDocument.load(myInputStream);
						PDFRenderer renderer = new PDFRenderer(document);
						int numPages = document.getNumberOfPages();

						for (int p = 0; p < numPages; p++) {

							BufferedImage image = renderer.renderImageWithDPI(
									p, 150);
							images.add(image);
						}
					} catch (IOException e) {
						e.printStackTrace();
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER,
								e.getMessage());

					} finally {
						if (document != null) {

							try {
								document.close();
							} catch (IOException e) {
								e.printStackTrace();
								throw new EJBExceptionLP(EJBExceptionLP.FEHLER,
										e.getMessage());

							}
						}

					}

				}

			}
			JasperPrintLP print = getReportPrint();
			Integer cachedReportvariante = theClientDto.getReportvarianteIId();
			if (images != null) {
				for (int k = 0; k < images.size(); k++) {
					HashMap mapParameter = new HashMap<String, Object>();
					mapParameter.put("P_BILD", images.get(k));
					cAktuellerReport = REPORT_GANZSEITIGESBILD;
					this.index = -1;
					data = new Object[1][1];

					BufferedImage img = images.get(k);

					img = Helper.bildUm90GradDrehenWennNoetig(img);

					data[0][0] = img;
					theClientDto.setReportvarianteIId(cachedReportvariante);
					initJRDS(mapParameter, REPORT_MODUL_ALLGEMEIN,
							REPORT_GANZSEITIGESBILD, theClientDto.getMandant(),
							theClientDto.getLocUi(), theClientDto);
					print = Helper.addReport2Report(print, getReportPrint()
							.getPrint());
				}
			}

			return print;

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN,
					new Exception(t));
		}

		return aJasperPrint;
	}

	public JasperPrintLP printProjektAlle(
			ReportJournalKriterienDto reportJournalKriterienDtoI,
			Date dStichtag, Integer bereichIId,
			boolean belegdatumStattZieltermin, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		JasperPrintLP oPrint = null;
		int iAnzahlZeilen = 0;
		cAktuellerReport = ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE;
		Locale locDruck;

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		// vom Stichtag die Uhrzeit abschneiden
		dStichtag = Helper.cutDate(dStichtag);

		try {
			session = factory.openSession();
			// Hiberante Criteria fuer alle Tabellen ausgehend von meiner
			// Haupttabelle anlegen,
			// nach denen ich filtern und sortieren kann
			Criteria critProjekt = session.createCriteria(FLRProjekt.class);

			// Einschraenkung auf den aktuellen Mandanten
			critProjekt.add(Restrictions.eq(
					ProjektFac.FLR_PROJEKT_MANDANT_C_NR,
					theClientDto.getMandant()));

			critProjekt.add(Restrictions.not(Restrictions.eq(
					ProjektFac.FLR_PROJEKT_STATUS_C_NR,
					ProjektServiceFac.PROJEKT_STATUS_STORNIERT)));

			// Einschraenkung nach Belegdatum von - bis
			String sVon = null;
			String sBis = null;

			if (reportJournalKriterienDtoI.dVon != null) {

				if (belegdatumStattZieltermin) {
					critProjekt.add(Restrictions.ge(
							ProjektFac.FLR_PROJEKT_T_ANLEGEN,
							reportJournalKriterienDtoI.dVon));
				} else {
					critProjekt.add(Restrictions.ge(
							ProjektFac.FLR_PROJEKT_T_ZIELDATUM,
							reportJournalKriterienDtoI.dVon));
				}

				sVon = Helper.formatDatum(reportJournalKriterienDtoI.dVon,
						theClientDto.getLocUi());
			}

			if (reportJournalKriterienDtoI.dBis != null) {
				if (belegdatumStattZieltermin) {
					critProjekt.add(Restrictions.le(
							ProjektFac.FLR_PROJEKT_T_ANLEGEN,
							reportJournalKriterienDtoI.dBis));
				} else {
					critProjekt.add(Restrictions.le(
							ProjektFac.FLR_PROJEKT_T_ZIELDATUM,
							reportJournalKriterienDtoI.dBis));
				}
				sBis = Helper.formatDatum(reportJournalKriterienDtoI.dBis,
						theClientDto.getLocUi());
			}

			if (reportJournalKriterienDtoI.sBelegnummerVon != null) {
				sVon = reportJournalKriterienDtoI.sBelegnummerVon;
				critProjekt.add(Restrictions.ge(ProjektFac.FLR_PROJEKT_C_NR,
						new Integer(sVon)));
			}

			critProjekt.add(Restrictions.eq(
					ProjektFac.FLR_PROJEKT_BEREICH_I_ID, bereichIId));

			if (reportJournalKriterienDtoI.sBelegnummerBis != null) {
				sBis = reportJournalKriterienDtoI.sBelegnummerBis;
				critProjekt.add(Restrictions.le(ProjektFac.FLR_PROJEKT_C_NR,
						new Integer(sBis)));
			}

			// Einschraenkung nach einer bestimmten Perosn
			if (reportJournalKriterienDtoI.personalIId != null) {
				critProjekt.add(Restrictions.eq(
						ProjektFac.FLR_PROJEKT_PERSONAL_I_ID_ZUGEWIESENER,
						reportJournalKriterienDtoI.personalIId));
			}

			// Sortierung nach Personal ist immer die erste Sortierung
			if (reportJournalKriterienDtoI.bSortiereNachPersonal) {
				critProjekt
						.createCriteria(
								ProjektFac.FLR_PROJEKT_FLRPERSONALZUGEWIESENER)
						.createCriteria(KundeFac.FLR_PARTNER)
						.addOrder(
								Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
			}

			// Sortierung nach Partner,
			if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				critProjekt
						.createCriteria(ProjektFac.FLR_PROJEKT_FLRPARTNER)
						.addOrder(
								Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
			}
			// es wird in jedem Fall nach der Iid sortiert
			critProjekt.addOrder(Order.asc(ProjektFac.FLR_PROJEKT_C_NR));

			List<?> list = critProjekt.list();
			Iterator<?> it = list.iterator();

			while (it.hasNext()) {
				FLRProjekt projekt = (FLRProjekt) it.next();
				session = factory.openSession();
				Criteria critHistory = session.createCriteria(FLRHistory.class);
				critHistory.createCriteria(ProjektFac.FLR_HISTORY_FLRPROJEKT)
						.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_I_ID,
								projekt.getI_id()));
				List<?> historyList = critHistory.list();
				if (historyList.size() != 0) {
					iAnzahlZeilen = iAnzahlZeilen + historyList.size();
				}
				iAnzahlZeilen++;
			}
			data = new Object[iAnzahlZeilen][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_ANZAHL_SPALTEN];
			int i = 0;
			it = list.iterator();

			while (it.hasNext()) {
				FLRProjekt projekt = (FLRProjekt) it.next();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_PROJEKTTITEL] = projekt
						.getC_titel();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_PROJEKTKATEGORIE] = projekt
						.getKategorie_c_nr();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_PROJEKTCNR] = projekt
						.getC_nr();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_KUNDECNAME1] = projekt
						.getFlrpartner().getC_name1nachnamefirmazeile1();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_STATUS] = projekt
						.getStatus_c_nr();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_TYP] = projekt
						.getTyp_c_nr();
				if (projekt.getPersonal_i_id_internerledigt() != null) {
					PersonalDto personalDto = getPersonalFac()
							.personalFindByPrimaryKey(
									projekt.getPersonal_i_id_internerledigt(),
									theClientDto);
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_INTERNERLEDIGT_PERSON] = personalDto
							.getPartnerDto().formatAnrede();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_INTERNERLEDIGT_ZEIT] = projekt
							.getT_internerledigt();
				}
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_WAHRSCHEINLICHKEIT] = projekt
						.getI_wahrscheinlichkeit();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_GEPLANTERUMSATZ] = projekt
						.getN_umsatzgeplant();

				locDruck = Helper.string2Locale(projekt.getFlrpartner()
						.getLocale_c_nr_kommunikation());
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_ZIELTERMIN] = Helper
						.formatDatum(projekt.getT_zielwunschdatum(), locDruck);
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_BELEGDATUM] = Helper
						.formatDatum(projekt.getT_anlegen(), locDruck);
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_ERLEDIGUNGSDATUM] = Helper
						.formatDatumZeit(projekt.getT_erledigungsdatum(),
								locDruck);

				Calendar calendar = new GregorianCalendar();
				calendar.setTime(new Date(projekt.getT_zielwunschdatum()
						.getTime()));
				int KW = calendar.get(Calendar.WEEK_OF_YEAR); // Kalendarwochen

				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_ZIELWOCHE] = ""
						+ KW;

				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_PRIO] = projekt
						.getI_prio();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_TEXT] = projekt
						.getX_freetext();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_ERZEUGER] = projekt
						.getFlrpersonalErzeuger().getFlrpartner()
						.getC_name1nachnamefirmazeile1();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_ZUGEWIESENER] = projekt
						.getFlrpersonalZugewiesener().getFlrpartner()
						.getC_name1nachnamefirmazeile1();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_ZEIT] = projekt
						.getT_zeit();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DAUER] = projekt
						.getD_dauer();
				//
				//

				Criteria crit1 = session.createCriteria(FLRHistory.class);
				crit1.createCriteria(ProjektFac.FLR_HISTORY_FLRPROJEKT).add(
						Restrictions.eq(ProjektFac.FLR_PROJEKT_I_ID,
								projekt.getI_id()));
				List<?> resultList = crit1.list();
				Iterator<?> itHistory = resultList.iterator();
				i++;

				while (itHistory.hasNext()) {
					FLRHistory history = (FLRHistory) itHistory.next();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_PROJEKTCNR] = projekt
							.getC_nr();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_ZUGEWIESENER] = projekt
							.getFlrpersonalZugewiesener().getFlrpartner()
							.getC_name1nachnamefirmazeile1();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_HISTORY_MITARBEITER] = history
							.getFlrpersonal().getFlrpartner()
							.getC_name1nachnamefirmazeile1();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_HISTORY_BELEGDATUM] = Helper
							.formatDatum(history.getT_belegdatum(), locDruck);
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_HISTORY_TEXT] = history
							.getX_text();
					i++;
				}

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
		// die Parameter dem Report uebergeben
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		parameter.put(
				LPReport.P_SORTIERUNG,
				buildSortierungProjektAlle(reportJournalKriterienDtoI,
						theClientDto));

		parameter.put("P_BEREICH", getProjektServiceFac()
				.bereichFindByPrimaryKey(bereichIId).getCBez());
		parameter.put("P_BELEGDATUM_STATT_ZIELTERMIN", new Boolean(
				belegdatumStattZieltermin));

		parameter
				.put(LPReport.P_FILTER,
						buildFilterProjektAlle(reportJournalKriterienDtoI,
								theClientDto));

		if (reportJournalKriterienDtoI.personalIId != null) {
			parameter.put(LPReport.P_SORTIERENACHPERSONAL, new Boolean(true));
		} else {
			parameter.put(LPReport.P_SORTIERENACHPERSONAL, new Boolean(false));
		}

		parameter.put(
				"P_TITLE",
				getTextRespectUISpr("proj.print.alle",
						theClientDto.getMandant(), theClientDto.getLocUi()));

		initJRDS(parameter, ProjektReportFac.REPORT_MODUL, cAktuellerReport,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		oPrint = getReportPrint();

		return oPrint;
	}

	public JasperPrintLP printProjektErledigt(
			ReportJournalKriterienDto reportJournalKriterienDtoI,
			Date dStichtag, Integer bereichIId,
			boolean interneErledigungBeruecksichtigen, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		JasperPrintLP oPrint = null;

		cAktuellerReport = ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT;
		Locale locDruck;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		// vom Stichtag die Uhrzeit abschneiden
		dStichtag = Helper.cutDate(dStichtag);
		try {
			session = factory.openSession();
			// Hiberante Criteria fuer alle Tabellen ausgehend von meiner
			// Haupttabelle anlegen,
			// nach denen ich filtern und sortieren kann
			Criteria crit = session.createCriteria(FLRProjekt.class);
			// Einschraenkung auf den aktuellen Mandanten
			crit.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_MANDANT_C_NR,
					theClientDto.getMandant()));
			crit.add(Restrictions.not(Restrictions.eq(
					ProjektFac.FLR_PROJEKT_STATUS_C_NR,
					ProjektServiceFac.PROJEKT_STATUS_STORNIERT)));

			crit.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_BEREICH_I_ID,
					bereichIId));
			// Einschraenkung nach Status Offen, Erledigt

			if (interneErledigungBeruecksichtigen == false) {
				// PJ18471
				crit.createAlias("flrprojektstatus", "s");
				crit.add(Restrictions.eq("s.b_erledigt",
						Helper.boolean2Short(true)));
			}

			if (reportJournalKriterienDtoI.dVon != null) {

				if (interneErledigungBeruecksichtigen == true) {

					crit.add(Restrictions.or(
							Restrictions
									.and(Restrictions
											.isNotNull(ProjektFac.FLR_PROJEKT_T_INTERNERLEDIGT),
											Restrictions
													.ge(ProjektFac.FLR_PROJEKT_T_INTERNERLEDIGT,
															reportJournalKriterienDtoI.dVon)),
							Restrictions.ge(
									ProjektFac.FLR_PROJEKT_T_ERLEDIGUNGSDATUM,
									reportJournalKriterienDtoI.dVon)));

				} else {
					crit.add(Restrictions.ge(
							ProjektFac.FLR_PROJEKT_T_ERLEDIGUNGSDATUM,
							reportJournalKriterienDtoI.dVon));
				}
			}

			if (reportJournalKriterienDtoI.dBis != null) {

				Date d = Helper.addiereTageZuDatum(
						reportJournalKriterienDtoI.dBis, 1);

				if (interneErledigungBeruecksichtigen == true) {
					crit.add(Restrictions.or(
							Restrictions.and(
									Restrictions
											.isNotNull(ProjektFac.FLR_PROJEKT_T_INTERNERLEDIGT),
									Restrictions
											.lt(ProjektFac.FLR_PROJEKT_T_INTERNERLEDIGT,
													d)), Restrictions.lt(
									ProjektFac.FLR_PROJEKT_T_ERLEDIGUNGSDATUM,
									d)));
				} else {
					crit.add(Restrictions.lt(
							ProjektFac.FLR_PROJEKT_T_ERLEDIGUNGSDATUM, d));
				}

			}
			// Einschraenkung nach einer bestimmten Perosn
			if (reportJournalKriterienDtoI.personalIId != null) {
				crit.add(Restrictions.eq(
						ProjektFac.FLR_PROJEKT_PERSONAL_I_ID_ERLEDIGER,
						reportJournalKriterienDtoI.personalIId));
			}
			// Sortierung nach Partner,
			if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				crit.createCriteria(ProjektFac.FLR_PROJEKT_FLRPARTNER)
						.addOrder(
								Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
				crit.addOrder(Order
						.asc(ProjektFac.FLR_PROJEKT_T_ERLEDIGUNGSDATUM));
				crit.addOrder(Order.asc(ProjektFac.FLR_PROJEKT_C_NR));

			}

			crit.addOrder(Order.asc(ProjektFac.FLR_PROJEKT_KATEGORIE_C_NR));
			List<?> list = crit.list();
			ArrayList<Object[]> alDaten = new ArrayList<Object[]>();
			Iterator<?> it = list.iterator();

			while (it.hasNext()) {
				FLRProjekt projekt = (FLRProjekt) it.next();
				Object[] oZeile = new Object[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ANZAHL_SPALTEN];

				if (interneErledigungBeruecksichtigen == true
						&& projekt.getT_internerledigt() != null
						&& projekt.getT_erledigungsdatum() != null) {

					// Wenn intern-Erledigt und normal erledigt, dann gilt jenes
					// Datum, das frueher war

					if (projekt.getT_internerledigt().getTime() <= projekt
							.getT_erledigungsdatum().getTime()) {
						if (reportJournalKriterienDtoI.dVon != null
								&& projekt.getT_internerledigt().getTime() < reportJournalKriterienDtoI.dVon
										.getTime()) {
							continue;
						}

						if (reportJournalKriterienDtoI.dBis != null
								&& projekt.getT_internerledigt().getTime() > reportJournalKriterienDtoI.dBis
										.getTime()) {
							continue;
						}

					}

				}

				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_PROJEKTTITEL] = projekt
						.getC_titel();
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_PROJEKTKATEGORIE] = projekt
						.getKategorie_c_nr();
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_PROJEKTCNR] = projekt
						.getC_nr();
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_TYP] = projekt
						.getTyp_c_nr();
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_KUNDECNAME1] = projekt
						.getFlrpartner().getC_name1nachnamefirmazeile1();

				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_WAHRSCHEINLICHKEIT] = projekt
						.getI_wahrscheinlichkeit();
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_GEPLANTERUMSATZ] = projekt
						.getN_umsatzgeplant();
				if (projekt.getPersonal_i_id_internerledigt() != null) {
					PersonalDto personalDto = getPersonalFac()
							.personalFindByPrimaryKey(
									projekt.getPersonal_i_id_internerledigt(),
									theClientDto);
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_INTERNERLEDIGT_PERSON] = personalDto
							.getPartnerDto().formatAnrede();
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_INTERNERLEDIGT_ZEIT] = projekt
							.getT_internerledigt();
				}
				locDruck = Helper.string2Locale(projekt.getFlrpartner()
						.getLocale_c_nr_kommunikation());
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ZIELTERMIN] = Helper
						.formatDatum(projekt.getT_zielwunschdatum(), locDruck);
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_BELEGDATUM] = Helper
						.formatDatum(projekt.getT_anlegen(), locDruck);
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ERLEDIGUNGSDATUM] = Helper
						.formatDatumZeit(projekt.getT_erledigungsdatum(),
								locDruck);
				if (projekt.getB_verrechenbar().equals(
						new Integer(1).shortValue())) {
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_B_VERRECHENBAR] = "verrechenbar";
				} else {
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_B_VERRECHENBAR] = null;
				}
				if (projekt.getB_freigegeben().equals(
						new Integer(1).shortValue())) {
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_FREIGEGEBEN] = "freigegeben";
				} else {
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_FREIGEGEBEN] = null;
				}

				Calendar calendar = new GregorianCalendar();
				calendar.setTime(new Date(projekt.getT_zielwunschdatum()
						.getTime()));
				int KW = calendar.get(Calendar.WEEK_OF_YEAR); // Kalendarwochen

				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ZIELWOCHE] = ""
						+ KW;
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_STATUS] = projekt
						.getStatus_c_nr();
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_PRIO] = projekt
						.getI_prio();
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_TEXT] = Helper
						.formatStyledTextForJasper(projekt.getX_freetext());
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ERZEUGER] = projekt
						.getFlrpersonalErzeuger().getFlrpartner()
						.getC_name1nachnamefirmazeile1();
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ZUGEWIESENER] = projekt
						.getFlrpersonalZugewiesener().getFlrpartner()
						.getC_name1nachnamefirmazeile1();
				if (projekt.getPersonal_i_id_erlediger() != null) {
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ERLEDIGER] = projekt
							.getFlrpersonalErlediger().getFlrpartner()
							.getC_name1nachnamefirmazeile1();
				}
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ZEIT] = projekt
						.getT_zeit();
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_DAUER] = projekt
						.getD_dauer();

				// Gesamte Dauer eines Projektes
				Double ddArbeitszeitist = getZeiterfassungFac()
						.getSummeZeitenEinesBeleges(LocaleFac.BELEGART_PROJEKT,
								projekt.getI_id(), null, null, null, null,
								theClientDto);
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_GESAMTDAUER] = ddArbeitszeitist;

				Criteria crit1 = session.createCriteria(FLRHistory.class);
				crit1.createCriteria(ProjektFac.FLR_HISTORY_FLRPROJEKT).add(
						Restrictions.eq(ProjektFac.FLR_PROJEKT_I_ID,
								projekt.getI_id()));
				List<?> resultList = crit1.list();
				Iterator<?> itHistory = resultList.iterator();
				alDaten.add(oZeile);
				while (itHistory.hasNext()) {
					FLRHistory history = (FLRHistory) itHistory.next();

					oZeile = new Object[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ANZAHL_SPALTEN];

					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_PROJEKTCNR] = projekt
							.getC_nr();
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ZUGEWIESENER] = projekt
							.getFlrpersonalZugewiesener().getFlrpartner()
							.getC_name1nachnamefirmazeile1();
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_HISTORY_MITARBEITER] = history
							.getFlrpersonal().getFlrpartner()
							.getC_name1nachnamefirmazeile1();
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_HISTORY_BELEGDATUM] = Helper
							.formatDatum(history.getT_belegdatum(), locDruck);
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_HISTORY_TEXT] = history
							.getX_text();
					alDaten.add(oZeile);

				}
			}
			Object[][] returnArray = new Object[alDaten.size()][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ANZAHL_SPALTEN];
			data = (Object[][]) alDaten.toArray(returnArray);

		} catch (RemoteException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_IN_ZEITDATEN,
					new Exception(e));
		} finally {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE, he);
			}
		}

		// die Parameter dem Report uebergeben
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put(
				LPReport.P_FILTER,
				buildFilterProjektErledigt(reportJournalKriterienDtoI,
						theClientDto));
		parameter.put("P_BEREICH", getProjektServiceFac()
				.bereichFindByPrimaryKey(bereichIId).getCBez());
		parameter.put("P_INTERNEERLEDIGUNGBERUECKSICHTIGEN",
				interneErledigungBeruecksichtigen);
		parameter.put(
				"P_TITLE",
				getTextRespectUISpr("proj.print.erledigt",
						theClientDto.getMandant(), theClientDto.getLocUi()));
		initJRDS(parameter, ProjektReportFac.REPORT_MODUL, cAktuellerReport,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		oPrint = getReportPrint();
		return oPrint;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printProjektOffene(
			ReportJournalKriterienDto reportJournalKriterienDtoI,
			Date dStichtag, Integer bereichIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		JasperPrintLP oPrint = null;
		int iAnzahlZeilen = 0;
		cAktuellerReport = ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE;
		Locale locDruck;

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		// vom Stichtag die Uhrzeit abschneiden
		dStichtag = Helper.cutDate(dStichtag);

		try {
			session = factory.openSession();
			// Hiberante Criteria fuer alle Tabellen ausgehend von meiner
			// Haupttabelle anlegen,
			// nach denen ich filtern und sortieren kann
			Criteria critProjekt = session.createCriteria(FLRProjekt.class);

			// Einschraenkung auf den aktuellen Mandanten
			critProjekt.add(Restrictions.eq(
					ProjektFac.FLR_PROJEKT_MANDANT_C_NR,
					theClientDto.getMandant()));
			critProjekt.add(Restrictions.not(Restrictions.eq(
					ProjektFac.FLR_PROJEKT_STATUS_C_NR,
					ProjektServiceFac.PROJEKT_STATUS_STORNIERT)));
			critProjekt.add(Restrictions.eq(
					ProjektFac.FLR_PROJEKT_BEREICH_I_ID, bereichIId));

			// PJ18471
			critProjekt.createAlias("flrprojektstatus", "s");
			critProjekt.add(Restrictions.eq("s.b_erledigt",
					Helper.boolean2Short(false)));

			// Das Belegdatum muss vor dem Stichtag liegen
			critProjekt.add(Restrictions.le(ProjektFac.FLR_PROJEKT_T_ANLEGEN,
					dStichtag));

			// Einschraenkung nach einer bestimmten Perosn
			if (reportJournalKriterienDtoI.personalIId != null) {
				critProjekt.add(Restrictions.eq(
						ProjektFac.FLR_PROJEKT_PERSONAL_I_ID_ZUGEWIESENER,
						reportJournalKriterienDtoI.personalIId));
			}

			// Sortierung nach Personal ist immer die erste Sortierung
			if (reportJournalKriterienDtoI.bSortiereNachPersonal) {
				critProjekt
						.createCriteria(
								ProjektFac.FLR_PROJEKT_FLRPERSONALZUGEWIESENER)
						.createCriteria(KundeFac.FLR_PARTNER)
						.addOrder(
								Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
			}

			// Sortierung nach Partner,
			if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				critProjekt
						.createCriteria(ProjektFac.FLR_PROJEKT_FLRPARTNER)
						.addOrder(
								Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
				critProjekt
						.addOrder(Order.asc(ProjektFac.FLR_PROJEKT_TYP_C_NR));
				critProjekt.addOrder(Order.asc(ProjektFac.FLR_PROJEKT_I_PRIO));
				critProjekt.addOrder(Order
						.asc(ProjektFac.FLR_PROJEKT_KATEGORIE_C_NR));
			}
			// es wird in jedem Fall nach der Belegnummer sortiert
			critProjekt.addOrder(Order.asc(ProjektFac.FLR_PROJEKT_C_NR));

			List<?> list = critProjekt.list();
			Iterator<?> it = list.iterator();
			while (it.hasNext()) {
				FLRProjekt projekt = (FLRProjekt) it.next();
				session = factory.openSession();
				Criteria critHistory = session.createCriteria(FLRHistory.class);
				critHistory.createCriteria(ProjektFac.FLR_HISTORY_FLRPROJEKT)
						.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_I_ID,
								projekt.getI_id()));
				List<?> historyList = critHistory.list();
				if (historyList.size() != 0) {
					iAnzahlZeilen = iAnzahlZeilen + historyList.size();
				}
				iAnzahlZeilen++;
			}

			data = new Object[iAnzahlZeilen][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ANZAHL_SPALTEN];
			int i = 0;
			it = list.iterator();
			while (it.hasNext()) {
				FLRProjekt projekt = (FLRProjekt) it.next();
				if (projekt.getI_id() == 7689) {
					System.out.println("x");
				}
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PROJEKTTITEL] = projekt
						.getC_titel();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PROJEKTKATEGORIE] = projekt
						.getKategorie_c_nr();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PROJEKTCNR] = projekt
						.getC_nr();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_STATUS] = projekt
						.getStatus_c_nr();

				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_TYP] = projekt
						.getTyp_c_nr();

				if (projekt.getPersonal_i_id_internerledigt() != null) {
					PersonalDto personalDto = getPersonalFac()
							.personalFindByPrimaryKey(
									projekt.getPersonal_i_id_internerledigt(),
									theClientDto);
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_INTERNERLEDIGT_PERSON] = personalDto
							.getPartnerDto().formatAnrede();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_INTERNERLEDIGT_ZEIT] = projekt
							.getT_internerledigt();
				}
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_WAHRSCHEINLICHKEIT] = projekt
						.getI_wahrscheinlichkeit();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_GEPLANTERUMSATZ] = projekt
						.getN_umsatzgeplant();
				// Gesamte Dauer eines Projektes
				Double ddArbeitszeitist = getZeiterfassungFac()
						.getSummeZeitenEinesBeleges(LocaleFac.BELEGART_PROJEKT,
								projekt.getI_id(), null, null, null, null,
								theClientDto);
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_GESAMTDAUER] = ddArbeitszeitist;

				if (projekt.getFlrpartner().getFlrlandplzort() != null) {
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_LKZ] = projekt
							.getFlrpartner().getFlrlandplzort().getFlrland()
							.getC_lkz();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PLZ] = projekt
							.getFlrpartner().getFlrlandplzort().getC_plz();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ORT] = projekt
							.getFlrpartner().getFlrlandplzort().getFlrort()
							.getC_name();
				}

				if (projekt.getFlrpartner().getFlrpartnerklasse() != null) {
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PARTNERKLASSE] = projekt
							.getFlrpartner().getFlrpartnerklasse().getC_nr();
				}

				if (projekt.getFlrpartner().getFlrbranche() != null) {
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_BRANCHE] = projekt
							.getFlrpartner().getFlrbranche().getC_nr();
				}

				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_KUNDECNAME1] = projekt
						.getFlrpartner().getC_name1nachnamefirmazeile1();
				locDruck = Helper.string2Locale(projekt.getFlrpartner()
						.getLocale_c_nr_kommunikation());
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ZIELTERMIN] = Helper
						.formatDatum(projekt.getT_zielwunschdatum(), locDruck);
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_BELEGDATUM] = Helper
						.formatDatum(projekt.getT_anlegen(), locDruck);

				Calendar calendar = new GregorianCalendar();
				calendar.setTime(new Date(projekt.getT_zielwunschdatum()
						.getTime()));
				int KW = calendar.get(Calendar.WEEK_OF_YEAR); // Kalendarwochen

				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ZIELWOCHE] = ""
						+ KW;

				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PRIO] = projekt
						.getI_prio();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_TEXT] = projekt
						.getX_freetext();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ERZEUGER] = projekt
						.getFlrpersonalErzeuger().getFlrpartner()
						.getC_name1nachnamefirmazeile1();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ZUGEWIESENER] = projekt
						.getFlrpersonalZugewiesener().getFlrpartner()
						.getC_name1nachnamefirmazeile1();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ZEIT] = projekt
						.getT_zeit();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_DAUER] = projekt
						.getD_dauer();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_B_VERRECHENBAR] = Helper
						.short2Boolean(projekt.getB_verrechenbar());
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_B_FREIGEGEBEN] = Helper
						.short2Boolean(projekt.getB_freigegeben());
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_DATEINAME] = projekt
						.getC_dateiname();
				// try {
				if (projekt.getFlransprechpartner() != null
						&& (projekt.getFlransprechpartner().getI_id() != null)) {
					if (projekt.getFlransprechpartner()
							.getFlrpartneransprechpartner() != null
							&& projekt.getFlransprechpartner()
									.getFlrpartneransprechpartner().getI_id() != null) {
						data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ANSPRECHPARTNERCNAME1] = projekt
								.getFlransprechpartner()
								.getFlrpartneransprechpartner()
								.getC_name1nachnamefirmazeile1();
						data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ANSPRECHPARTNER] = (projekt
								.getFlransprechpartner()
								.getFlrpartneransprechpartner()
								.getC_name1nachnamefirmazeile1()
								+ " "
								+ (projekt.getFlransprechpartner()
										.getFlrpartneransprechpartner()
										.getC_name2vornamefirmazeile2() == null ? ""
										: projekt.getFlransprechpartner()
												.getFlrpartneransprechpartner()
												.getC_name2vornamefirmazeile2())
								+ " " + (projekt.getFlransprechpartner()
								.getFlrpartneransprechpartner()
								.getC_name3vorname2abteilung() == null ? ""
								: projekt.getFlransprechpartner()
										.getFlrpartneransprechpartner()
										.getC_name3vorname2abteilung())).trim();
					}
				}
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				//
				Criteria crit1 = session.createCriteria(FLRHistory.class);
				crit1.createCriteria(ProjektFac.FLR_HISTORY_FLRPROJEKT).add(
						Restrictions.eq(ProjektFac.FLR_PROJEKT_I_ID,
								projekt.getI_id()));
				List<?> resultList = crit1.list();
				Iterator<?> itHistory = resultList.iterator();
				i++;
				while (itHistory.hasNext()) {
					FLRHistory history = (FLRHistory) itHistory.next();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PROJEKTCNR] = projekt
							.getC_nr();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ZUGEWIESENER] = projekt
							.getFlrpersonalZugewiesener().getFlrpartner()
							.getC_name1nachnamefirmazeile1();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_MITARBEITER] = history
							.getFlrpersonal().getFlrpartner()
							.getC_name1nachnamefirmazeile1();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_BELEGDATUM] = Helper
							.formatDatum(history.getT_belegdatum(), locDruck);
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_TEXT] = history
							.getX_text();
					i++;
				}
			}

		} finally {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE, he);
			}
		}
		// die Parameter dem Report uebergeben
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		parameter.put(
				LPReport.P_SORTIERUNG,
				buildSortierungProjektOffene(reportJournalKriterienDtoI,
						theClientDto));
		parameter.put("P_BEREICH", getProjektServiceFac()
				.bereichFindByPrimaryKey(bereichIId).getCBez());
		parameter.put(
				LPReport.P_FILTER,
				buildFilterProjektOffene(reportJournalKriterienDtoI,
						theClientDto));

		if (reportJournalKriterienDtoI.personalIId != null) {
			parameter.put(LPReport.P_SORTIERENACHPERSONAL, new Boolean(true));
		} else {
			parameter.put(LPReport.P_SORTIERENACHPERSONAL, new Boolean(false));
		}

		parameter.put(
				"P_TITLE",
				getTextRespectUISpr("proj.print.offene",
						theClientDto.getMandant(), theClientDto.getLocUi()));

		initJRDS(parameter, ProjektReportFac.REPORT_MODUL, cAktuellerReport,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		oPrint = getReportPrint();

		return oPrint;
	}

	private Object[] projektFuerAktivitaetsuebersichtBefuellen(
			Integer projektIId, TheClientDto theClientDto) {

		Object[] oZeile = new Object[REPORT_AKTIVITAETSUEBERSICHT_ANZAHL_SPALTEN];

		ProjektDto projektDto = null;
		try {
			projektDto = getProjektFac().projektFindByPrimaryKey(projektIId);

			oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_VON] = projektDto
					.getTAnlegen();
			oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BIS] = projektDto
					.getTZielwunschdatum();

			BereichDto bDto = getProjektServiceFac().bereichFindByPrimaryKey(
					projektDto.getBereichIId());
			oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_BEREICH] = bDto
					.getCBez();

			oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_PERSONAL_ERZEUGER] = getPersonalFac()
					.personalFindByPrimaryKey(
							projektDto.getPersonalIIdErzeuger(), theClientDto)
					.getPartnerDto().formatFixName1Name2();
			oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_PERSONAL_MITARBEITER] = getPersonalFac()
					.personalFindByPrimaryKey(
							projektDto.getPersonalIIdZugewiesener(),
							theClientDto).getPartnerDto().formatFixName1Name2();

			oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGART] = LocaleFac.BELEGART_PROJEKT;

			oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGNUMMER] = projektDto
					.getCNr();

			if (projektDto.getPersonalIIdInternerledigt() != null) {
				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKey(
								projektDto.getPersonalIIdInternerledigt(),
								theClientDto);
				oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_INTERNERLEDIGT_PERSON] = personalDto
						.getPartnerDto().formatAnrede();
				oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_INTERNERLEDIGT_ZEIT] = projektDto
						.getTInternerledigt();
			}

			oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGTEXT] = projektDto
					.getCTitel() + " " + projektDto.getXFreetext();

			PartnerDto pDto = getPartnerFac().partnerFindByPrimaryKey(
					projektDto.getPartnerIId(), theClientDto);

			oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PARTNER] = pDto
					.formatTitelAnrede();
			if (pDto.getLandplzortDto() != null) {
				oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PLZ] = pDto
						.getLandplzortDto().getCPlz();
				oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_LKZ] = pDto
						.getLandplzortDto().getLandDto().getCLkz();
				oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_ORT] = pDto
						.getLandplzortDto().getOrtDto().getCName();
			}

			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = factory.openSession();
			Criteria crit = session.createCriteria(FLRHistory.class);

			crit.createCriteria(ProjektFac.FLR_HISTORY_FLRPROJEKT).add(
					Restrictions.eq(ProjektFac.FLR_PROJEKT_I_ID,
							projektDto.getIId()));
			crit.addOrder(Order.desc(ProjektFac.FLR_HISTORY_T_BELEGDATUM));
			List<?> resultListHistory = crit.list();

			Iterator<?> it = resultListHistory.iterator();

			ArrayList<Object[]> alSub = new ArrayList<Object[]>();
			while (it.hasNext()) {
				FLRHistory history = (FLRHistory) it.next();

				Object[] oSub = new Object[5];

				oSub[0] = history.getT_belegdatum();
				oSub[1] = history.getFlrpersonal().getFlrpartner()
						.getC_name1nachnamefirmazeile1();
				oSub[2] = history.getX_text();
				if (history.getFlrhistoryart() != null) {
					oSub[3] = history.getFlrhistoryart().getC_bez();
				}
				oSub[4] = history.getC_titel();
				alSub.add(oSub);
			}

			String[] fieldnames = new String[] { "F_DATUM", "F_PERSONAL",
					"F_TEXT", "F_ART", "F_TITEL" };

			Object[][] dataSub = new Object[alSub.size()][fieldnames.length];
			dataSub = (Object[][]) alSub.toArray(dataSub);

			oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEG_SUBREPORT] = new LPDatenSubreport(
					dataSub, fieldnames);

			session.close();
		} catch (RemoteException ex3) {
			throwEJBExceptionLPRespectOld(ex3);
		}
		return oZeile;

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAktivitaetsuebersicht(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, boolean bGesamtinfo,
			TheClientDto theClientDto) {

		cAktuellerReport = ProjektReportFac.REPORT_PROJEKT_JOURNAL_AKTIVITAETSUEBERSICHT;

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		ArrayList alDaten = new ArrayList();

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		HashMap hmProjekte = new HashMap();

		String sQuery = "select distinct  zeitdaten.c_belegartnr,zeitdaten.i_belegartid, zeitdaten.personal_i_id from FLRZeitdaten zeitdaten WHERE  zeitdaten.c_belegartnr is not null AND zeitdaten.t_zeit>='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tVon.getTime()))
				+ "' AND zeitdaten.t_zeit<='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tBis.getTime()))
				+ "' AND zeitdaten.flrpersonal.mandant_c_nr='"
				+ theClientDto.getMandant() + "'";

		org.hibernate.Query inventurliste = session.createQuery(sQuery);

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListIterator = resultList.iterator();

		int row = 0;
		while (resultListIterator.hasNext()) {

			Object[] o = (Object[]) resultListIterator.next();

			Integer personalIId = (Integer) o[2];

			try {
				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKey(personalIId, theClientDto);
				Object[] oZeile = new Object[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_ANZAHL_SPALTEN];

				oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGART] = o[0];
				oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PERSONAL] = personalDto
						.getPartnerDto().formatFixName1Name2();

				String sBezeichnung = "";
				PartnerDto partnerDto = null;
				if (((String) o[0]).equals(LocaleFac.BELEGART_AUFTRAG)) {
					AuftragDto auftragDto = getAuftragFac()
							.auftragFindByPrimaryKey((Integer) o[1]);
					oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGNUMMER] = auftragDto
							.getCNr();

					sBezeichnung = auftragDto.getCBezProjektbezeichnung();
					partnerDto = getKundeFac().kundeFindByPrimaryKey(
							auftragDto.getKundeIIdAuftragsadresse(),
							theClientDto).getPartnerDto();
				} else if (((String) o[0]).equals(LocaleFac.BELEGART_LOS)) {
					com.lp.server.fertigung.service.LosDto losDto = getFertigungFac()
							.losFindByPrimaryKey((Integer) o[1]);
					oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGNUMMER] = losDto
							.getCNr();

					sBezeichnung = losDto.getCProjekt();

					if (losDto.getAuftragIId() != null) {

						AuftragDto auftragDto = getAuftragFac()
								.auftragFindByPrimaryKey(losDto.getAuftragIId());

						partnerDto = getKundeFac().kundeFindByPrimaryKey(
								auftragDto.getKundeIIdAuftragsadresse(),
								theClientDto).getPartnerDto();
					}

				} else if (((String) o[0]).equals(LocaleFac.BELEGART_PROJEKT)) {

					if (!hmProjekte.containsKey((Integer) o[1])) {
						hmProjekte.put((Integer) o[1], "");
					}

					oZeile = projektFuerAktivitaetsuebersichtBefuellen(
							(Integer) o[1], theClientDto);

				} else if (((String) o[0]).equals(LocaleFac.BELEGART_ANGEBOT)) {
					AngebotDto angebotDto = getAngebotFac()
							.angebotFindByPrimaryKey((Integer) o[1],
									theClientDto);
					oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGNUMMER] = angebotDto
							.getCNr();

					sBezeichnung = angebotDto.getCBez();

					partnerDto = getKundeFac().kundeFindByPrimaryKey(
							angebotDto.getKundeIIdAngebotsadresse(),
							theClientDto).getPartnerDto();

					// oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_VON]
					// = projektDto.getTAnlegen();
					// oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BIS]
					// = projektDto.getTZielwunschdatum();

				}

				AuftragzeitenDto[] dtos = getZeiterfassungFac()
						.getAllZeitenEinesBeleges((String) o[0],
								(Integer) o[1], null, personalIId, tVon, tBis,
								false, false, theClientDto);

				if (dtos.length > 0
						&& (((String) o[0]).equals(LocaleFac.BELEGART_PROJEKT) != true)) {
					oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_VON] = dtos[0]
							.getTsBeginn();
					oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BIS] = dtos[dtos.length - 1]
							.getTsEnde();
				}

				double zeiten = 0;
				for (int i = 0; i < dtos.length; i++) {
					if (dtos[i] != null && dtos[i].getDdDauer() != null) {
						zeiten = zeiten + dtos[i].getDdDauer().doubleValue();
					}
				}
				Double dDauer = new Double(zeiten);

				oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_DAUER] = dDauer;

				if (!((String) o[0]).equals(LocaleFac.BELEGART_PROJEKT)) {

					if (partnerDto != null) {
						oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PARTNER] = partnerDto
								.formatTitelAnrede();

						if (partnerDto.getLandplzortDto() != null) {
							oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_LKZ] = partnerDto
									.getLandplzortDto().getLandDto().getCLkz();
							oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PLZ] = partnerDto
									.getLandplzortDto().getCPlz();
							oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_ORT] = partnerDto
									.getLandplzortDto().getOrtDto().getCName();
						}

					} else {
						oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PARTNER] = "";
					}
					oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGTEXT] = sBezeichnung;

				}
				row++;

				alDaten.add(oZeile);
			} catch (RemoteException ex1) {
				// Zeitdaten falsch
			}
		}
		session.close();

		// Telefonzeiten

		session = factory.openSession();
		org.hibernate.Criteria crit = session
				.createCriteria(FLRTelefonzeiten.class)
				.createAlias(ZeiterfassungFac.FLR_TELEFONZEITEN_FLRPERSONAL,
						"p")
				.add(Restrictions.eq("p.mandant_c_nr",
						theClientDto.getMandant()));

		crit.add(Restrictions
				.ge(ZeiterfassungFac.FLR_TELEFONZEITEN_T_VON, tVon));
		crit.add(Restrictions
				.lt(ZeiterfassungFac.FLR_TELEFONZEITEN_T_VON, tBis));

		List<?> list = crit.list();
		Iterator<?> iterator = list.iterator();

		while (iterator.hasNext()) {
			FLRTelefonzeiten flrTelefonzeiten = (FLRTelefonzeiten) iterator
					.next();

			Object[] oZeile = new Object[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_ANZAHL_SPALTEN];

			oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGART] = "Telefon";
			oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGTEXT] = flrTelefonzeiten
					.getX_kommentarext();

			if (flrTelefonzeiten.getT_bis() != null) {
				java.sql.Time tTemp = new java.sql.Time(flrTelefonzeiten
						.getT_bis().getTime()
						- flrTelefonzeiten.getT_von().getTime() - 3600000);
				oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_DAUER] = Helper
						.time2Double(tTemp);
				oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BIS] = new Timestamp(
						flrTelefonzeiten.getT_bis().getTime());
			}

			oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_VON] = new Timestamp(
					flrTelefonzeiten.getT_von().getTime());

			if (flrTelefonzeiten.getFlrpartner() != null) {

				com.lp.server.partner.service.PartnerDto partnerDto = getPartnerFac()
						.partnerFindByPrimaryKey(
								flrTelefonzeiten.getFlrpartner().getI_id(),
								theClientDto);
				oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PARTNER] = partnerDto
						.formatFixTitelName1Name2();

			} else {
				oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PARTNER] = "";
			}

		}

		// Projekte, welche wiese Woche angelegt wurde, bzw. ein neues Detail
		// hinzugefuegt wurde

		Criteria critProjekt = session.createCriteria(FLRHistory.class);

		critProjekt.add(Restrictions.between(
				ProjektFac.FLR_HISTORY_T_BELEGDATUM, tVon, tBis));

		critProjekt.createAlias("flrprojekt", "p").add(
				Restrictions.eq("p.mandant_c_nr", theClientDto.getMandant()));

		List<?> resultListVorher = critProjekt.list();

		Iterator<?> itVorher = resultListVorher.iterator();

		while (itVorher.hasNext()) {
			FLRHistory history = (FLRHistory) itVorher.next();

			if (!hmProjekte.containsKey(history.getProjekt_i_id())) {
				if (!hmProjekte.containsKey(history.getProjekt_i_id())) {
					hmProjekte.put(history.getProjekt_i_id(), "");
				}
				Object[] z = projektFuerAktivitaetsuebersichtBefuellen(
						history.getProjekt_i_id(), theClientDto);
				alDaten.add(z);
			}

		}

		session.close();

		session = factory.openSession();

		critProjekt = session.createCriteria(FLRProjekt.class);

		critProjekt.add(Restrictions.between(ProjektFac.FLR_PROJEKT_T_ANLEGEN,
				tVon, tBis));
		critProjekt.add(Restrictions.eq("mandant_c_nr",
				theClientDto.getMandant()));

		resultListVorher = critProjekt.list();

		itVorher = resultListVorher.iterator();

		while (itVorher.hasNext()) {
			FLRProjekt projekt = (FLRProjekt) itVorher.next();

			if (!hmProjekte.containsKey(projekt.getI_id())) {

				Object[] z = projektFuerAktivitaetsuebersichtBefuellen(
						projekt.getI_id(), theClientDto);
				alDaten.add(z);

			}

		}

		session.close();

		// Nach Artikel sortieren
		for (int i = alDaten.size() - 1; i > 0; --i) {
			for (int j = 0; j < i; ++j) {
				Object[] a = (Object[]) alDaten.get(j);
				Object[] b = (Object[]) alDaten.get(j + 1);
				if (((String) a[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PARTNER])
						.compareTo(((String) b[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PARTNER])) > 0) {
					Object[] h = a;
					alDaten.set(j, b);
					alDaten.set(j + 1, h);
				}
			}
		}

		Object[][] returnArray = new Object[alDaten.size()][ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(returnArray);

		parameter.put("P_VON", tVon);
		parameter.put("P_BIS", tBis);
		parameter.put("P_GESAMTINFO", new Boolean(bGesamtinfo));

		initJRDS(parameter, ProjektReportFac.REPORT_MODUL, cAktuellerReport,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printProjektzeiten(Integer projektIId,
			int iSortierung, TheClientDto theClientDto) {

		cAktuellerReport = ProjektReportFac.REPORT_PROJEKTZEITDATEN;
		TreeMap<String, Object[]> alDaten = new TreeMap<String, Object[]>();
		try {
			ProjektDto pjDto = getProjektFac().projektFindByPrimaryKey(
					projektIId);

			FilterKriterium[] filterKrit = new FilterKriterium[1];
			FilterKriterium krit1 = new FilterKriterium("projekt_i_id", true,
					projektIId.toString(), FilterKriterium.OPERATOR_EQUAL,
					false);

			filterKrit[0] = krit1;

			QueryParameters p = new QueryParameters(
					QueryParameters.UC_ID_PROJEKTVERLAUF, null,
					new FilterBlock(filterKrit, "AND"), null, null);

			ProjektverlaufHandler pv = new ProjektverlaufHandler();
			pv.setCurrentUser(theClientDto);
			pv.setQuery(p);

			LinkedHashMap<String, ProjektVerlaufHelperDto> hm = pv.setInhalt();

			ProjektVerlaufHelperDto pvDto = new ProjektVerlaufHelperDto(0,
					pjDto);
			hm.put("PROJEKT", pvDto);

			Iterator<String> it = hm.keySet().iterator();

			while (it.hasNext()) {
				ProjektVerlaufHelperDto belegDto = hm.get(it.next());

				String belegart = null;
				String belegnummer = null;
				Integer belegIId = null;
				if (belegDto.getBelegDto() instanceof AngebotDto) {
					AngebotDto dto = (AngebotDto) belegDto.getBelegDto();
					belegart = LocaleFac.BELEGART_ANGEBOT;
					belegnummer = dto.getCNr();
					belegIId = dto.getIId();

				} else if (belegDto.getBelegDto() instanceof AuftragDto) {
					AuftragDto dto = (AuftragDto) belegDto.getBelegDto();
					belegart = LocaleFac.BELEGART_AUFTRAG;
					belegnummer = dto.getCNr();
					belegIId = dto.getIId();
				} else if (belegDto.getBelegDto() instanceof LosDto) {
					LosDto dto = (LosDto) belegDto.getBelegDto();
					belegart = LocaleFac.BELEGART_LOS;
					belegnummer = dto.getCNr();
					belegIId = dto.getIId();
				} else if (belegDto.getBelegDto() instanceof ProjektDto) {
					ProjektDto dto = (ProjektDto) belegDto.getBelegDto();
					belegart = LocaleFac.BELEGART_PROJEKT;
					belegnummer = dto.getCNr();
					belegIId = dto.getIId();
				}

				if (belegart != null && belegIId != null) {

					AuftragzeitenDto[] dtos = getZeiterfassungFac()
							.getAllZeitenEinesBeleges(belegart, belegIId, null,
									null, null, null, false, true, theClientDto);

					if (belegart == LocaleFac.BELEGART_PROJEKT) {
						// Telefonzeiten hinzufuegen und sortieren
						dtos = AuftragzeitenDto.add2BelegzeitenDtos(
								getZeiterfassungFac()
										.getAllTelefonzeitenEinesProjekts(
												belegIId, null, null, null,
												theClientDto), dtos);

					}

					for (int i = 0; i < dtos.length; i++) {

						Object[] oZeile = new Object[ProjektReportFac.REPORT_PROJEKTZEITEN_ANZAHL_SPALTEN];
						oZeile[ProjektReportFac.REPORT_PROJEKTZEITEN_BELEG] = belegnummer;
						oZeile[ProjektReportFac.REPORT_PROJEKTZEITEN_BELEGART] = belegart;
						oZeile[ProjektReportFac.REPORT_PROJEKTZEITEN_TELEFONZEIT] = new Boolean(dtos[i].isBTelefonzeit()); 

						oZeile[ProjektReportFac.REPORT_PROJEKTZEITEN_BIS] = dtos[i]
								.getTsEnde();

						oZeile[ProjektReportFac.REPORT_PROJEKTZEITEN_VON] = dtos[i]
								.getTsBeginn();

						oZeile[ProjektReportFac.REPORT_PROJEKTZEITEN_PERSON] = dtos[i]
								.getSPersonalMaschinenname();
						oZeile[ProjektReportFac.REPORT_PROJEKTZEITEN_DAUER] = dtos[i]
								.getDdDauer();

						oZeile[ProjektReportFac.REPORT_PROJEKTZEITEN_ARTIKEL] = dtos[i]
								.getSArtikelcnr();
						oZeile[ProjektReportFac.REPORT_PROJEKTZEITEN_BEZEICHNUNG] = dtos[i]
								.getSArtikelbezeichnung();
						oZeile[ProjektReportFac.REPORT_PROJEKTZEITEN_BEMERKUNG] = dtos[i]
								.getSZeitbuchungtext();
						oZeile[ProjektReportFac.REPORT_PROJEKTZEITEN_KOMMENTAR] = dtos[i]
								.getSKommentar();
						oZeile[ProjektReportFac.REPORT_PROJEKTZEITEN_KOSTEN] = dtos[i]
								.getBdKosten();
						
						String sort = "";
						if (iSortierung == SORTIERUNG_PROJEKTZEITEN_BELEGART_BELEG_PERSON) {
							sort = Helper.fitString2Length(belegart, 15, ' ')
									+ Helper.fitString2Length(belegnummer, 20,
											' ')
									+ Helper.fitString2Length(
											dtos[i].getSPersonalMaschinenname(),
											40, ' ') + dtos[i].getTsBeginn();
						} else if (iSortierung == SORTIERUNG_PROJEKTZEITEN_TAETIEKGKEIT_DATUM_PERSON) {
							sort = Helper.fitString2Length(
									dtos[i].getSArtikelcnr(), 40, ' ')
									+ dtos[i].getTsBeginn()
									+ Helper.fitString2Length(
											dtos[i].getSPersonalMaschinenname(),
											40, ' ');
						} else if (iSortierung == SORTIERUNG_PROJEKTZEITEN_PERSON_BELEGART_BELEG) {
							sort = Helper.fitString2Length(
									dtos[i].getSPersonalMaschinenname(), 40,
									' ')
									+ Helper.fitString2Length(belegart, 15, ' ')
									+ Helper.fitString2Length(belegnummer, 20,
											' ') + dtos[i].getTsBeginn();
						}

						alDaten.put(sort, oZeile);

					}
				}

			}
			HashMap<String, Object> parameter = new HashMap<String, Object>();
			if (iSortierung == SORTIERUNG_PROJEKTZEITEN_BELEGART_BELEG_PERSON) {
				parameter
						.put("P_SORTIERUNG",
								getTextRespectUISpr(
										"proj.projektzeitdaten.sortierung.belegartbelegperson",
										theClientDto.getMandant(),
										theClientDto.getLocUi()));
			} else if (iSortierung == SORTIERUNG_PROJEKTZEITEN_PERSON_BELEGART_BELEG) {
				parameter
						.put("P_SORTIERUNG",
								getTextRespectUISpr(
										"proj.projektzeitdaten.sortierung.personbelegartbeleg",
										theClientDto.getMandant(),
										theClientDto.getLocUi()));
			} else if (iSortierung == SORTIERUNG_PROJEKTZEITEN_TAETIEKGKEIT_DATUM_PERSON) {
				parameter
						.put("P_SORTIERUNG",
								getTextRespectUISpr(
										"proj.report.projektzeiten.sortierungtaetigkeit",
										theClientDto.getMandant(),
										theClientDto.getLocUi()));
			}

			data = new Object[alDaten.size()][ProjektReportFac.REPORT_PROJEKTZEITEN_ANZAHL_SPALTEN];

			Iterator itKey = alDaten.keySet().iterator();
			int i = 0;
			while (itKey.hasNext()) {
				data[i] = alDaten.get(itKey.next());
				i++;
			}

			parameter.put("P_PROJEKT", pjDto.getCNr());
			parameter.put("P_TITEL", pjDto.getCTitel());

			initJRDS(parameter, ProjektReportFac.REPORT_MODUL,
					cAktuellerReport, theClientDto.getMandant(),
					theClientDto.getLocUi(), theClientDto);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printProjektOffeneAuswahlListe(
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		JasperPrintLP oPrint = null;
		int iAnzahlZeilen = 0;
		cAktuellerReport = ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE;
		Locale locDruck;

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		try {
			session = factory.openSession();
			// Hiberante Criteria fuer alle Tabellen ausgehend von meiner
			// Haupttabelle anlegen,
			// nach denen ich filtern und sortieren kann

			Query query = session.createQuery(ProjektHandler.lastQuery);
			List<?> list = query.list();
			Iterator<?> it = list.iterator();
			while (it.hasNext()) {
				FLRProjekt projekt = (FLRProjekt) it.next();
				session = factory.openSession();
				Criteria crit1 = session.createCriteria(FLRHistory.class);
				crit1.createCriteria(ProjektFac.FLR_HISTORY_FLRPROJEKT).add(
						Restrictions.eq(ProjektFac.FLR_PROJEKT_I_ID,
								projekt.getI_id()));
				List<?> resultList = crit1.list();
				if (resultList.size() != 0) {
					iAnzahlZeilen = iAnzahlZeilen + resultList.size();
				}
				iAnzahlZeilen++;
			}
			data = new Object[iAnzahlZeilen][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ANZAHL_SPALTEN];
			int i = 0;
			it = list.iterator();
			while (it.hasNext()) {
				FLRProjekt projekt = (FLRProjekt) it.next();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PROJEKTTITEL] = projekt
						.getC_titel();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PROJEKTKATEGORIE] = projekt
						.getKategorie_c_nr();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PROJEKTCNR] = projekt
						.getC_nr();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_STATUS] = projekt
						.getStatus_c_nr();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_KUNDECNAME1] = projekt
						.getFlrpartner().getC_name1nachnamefirmazeile1();
				if (projekt.getFlrpartner().getFlrlandplzort() != null) {
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_LKZ] = projekt
							.getFlrpartner().getFlrlandplzort().getFlrland()
							.getC_lkz();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PLZ] = projekt
							.getFlrpartner().getFlrlandplzort().getC_plz();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ORT] = projekt
							.getFlrpartner().getFlrlandplzort().getFlrort()
							.getC_name();
				}

				if (projekt.getFlrpartner().getFlrpartnerklasse() != null) {
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PARTNERKLASSE] = projekt
							.getFlrpartner().getFlrpartnerklasse().getC_nr();
				}

				if (projekt.getFlrpartner().getFlrbranche() != null) {
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_BRANCHE] = projekt
							.getFlrpartner().getFlrbranche().getC_nr();
				}

				locDruck = Helper.string2Locale(projekt.getFlrpartner()
						.getLocale_c_nr_kommunikation());
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ZIELTERMIN] = Helper
						.formatDatum(projekt.getT_zielwunschdatum(), locDruck);
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_BELEGDATUM] = Helper
						.formatDatum(projekt.getT_anlegen(), locDruck);
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_TYP] = projekt
						.getTyp_c_nr();

				Calendar calendar = new GregorianCalendar();
				calendar.setTime(new Date(projekt.getT_zielwunschdatum()
						.getTime()));
				int KW = calendar.get(Calendar.WEEK_OF_YEAR); // Kalendarwochen

				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ZIELWOCHE] = ""
						+ KW;

				// Gesamte Dauer eines Projektes
				Double ddArbeitszeitist = getZeiterfassungFac()
						.getSummeZeitenEinesBeleges(LocaleFac.BELEGART_PROJEKT,
								projekt.getI_id(), null, null, null, null,
								theClientDto);
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_GESAMTDAUER] = ddArbeitszeitist;

				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PRIO] = projekt
						.getI_prio();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_TEXT] = projekt
						.getX_freetext();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ERZEUGER] = projekt
						.getFlrpersonalErzeuger().getFlrpartner()
						.getC_name1nachnamefirmazeile1();

				if (projekt.getPersonal_i_id_internerledigt() != null) {
					PersonalDto personalDto = getPersonalFac()
							.personalFindByPrimaryKey(
									projekt.getPersonal_i_id_internerledigt(),
									theClientDto);
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_INTERNERLEDIGT_PERSON] = personalDto
							.getPartnerDto().formatAnrede();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_INTERNERLEDIGT_ZEIT] = projekt
							.getT_internerledigt();
				}

				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ZUGEWIESENER] = projekt
						.getFlrpersonalZugewiesener().getFlrpartner()
						.getC_name1nachnamefirmazeile1();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ZEIT] = projekt
						.getT_zeit();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_WAHRSCHEINLICHKEIT] = projekt
						.getI_wahrscheinlichkeit();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_GEPLANTERUMSATZ] = projekt
						.getN_umsatzgeplant();

				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_DAUER] = projekt
						.getD_dauer();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_B_VERRECHENBAR] = Helper
						.short2Boolean(projekt.getB_verrechenbar());
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_B_FREIGEGEBEN] = Helper
						.short2Boolean(projekt.getB_freigegeben());
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_DATEINAME] = projekt
						.getC_dateiname();
				// try {
				if (projekt.getFlransprechpartner() != null
						&& (projekt.getFlransprechpartner().getI_id() != null)) {
					if (projekt.getFlransprechpartner()
							.getFlrpartneransprechpartner() != null
							&& projekt.getFlransprechpartner()
									.getFlrpartneransprechpartner().getI_id() != null) {
						data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ANSPRECHPARTNERCNAME1] = projekt
								.getFlransprechpartner()
								.getFlrpartneransprechpartner()
								.getC_name1nachnamefirmazeile1();
						data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ANSPRECHPARTNER] = (projekt
								.getFlransprechpartner()
								.getFlrpartneransprechpartner()
								.getC_name1nachnamefirmazeile1()
								+ " "
								+ (projekt.getFlransprechpartner()
										.getFlrpartneransprechpartner()
										.getC_name2vornamefirmazeile2() == null ? ""
										: projekt.getFlransprechpartner()
												.getFlrpartneransprechpartner()
												.getC_name2vornamefirmazeile2())
								+ " " + (projekt.getFlransprechpartner()
								.getFlrpartneransprechpartner()
								.getC_name3vorname2abteilung() == null ? ""
								: projekt.getFlransprechpartner()
										.getFlrpartneransprechpartner()
										.getC_name3vorname2abteilung())).trim();
					}
				}
				//
				Criteria critHistory = session.createCriteria(FLRHistory.class);
				critHistory.createCriteria(ProjektFac.FLR_HISTORY_FLRPROJEKT)
						.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_I_ID,
								projekt.getI_id()));
				List<?> rlHistory = critHistory.list();
				Iterator<?> itHistory = rlHistory.iterator();
				i++;
				while (itHistory.hasNext()) {
					FLRHistory history = (FLRHistory) itHistory.next();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PROJEKTCNR] = projekt
							.getC_nr();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ZUGEWIESENER] = projekt
							.getFlrpersonalZugewiesener().getFlrpartner()
							.getC_name1nachnamefirmazeile1();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_MITARBEITER] = history
							.getFlrpersonal().getFlrpartner()
							.getC_name1nachnamefirmazeile1();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_BELEGDATUM] = Helper
							.formatDatum(history.getT_belegdatum(), locDruck);
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_TEXT] = history
							.getX_text();
					i++;
				}
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
		// die Parameter dem Report uebergeben
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		parameter.put(
				"P_TITLE",
				getTextRespectUISpr("proj.print.offene",
						theClientDto.getMandant(), theClientDto.getLocUi()));

		initJRDS(parameter, ProjektReportFac.REPORT_MODUL, cAktuellerReport,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		oPrint = getReportPrint();

		return oPrint;
	}

	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;
		String fieldName = field.getName();
		if (cAktuellerReport
				.equals(ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE)) {
			if ("F_CNR".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PROJEKTCNR];
			} else if ("F_STATUS".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_STATUS];
			} else if ("F_PARTNER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_KUNDECNAME1];
			} else if ("F_ORT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ORT];
			} else if ("F_BRANCHE".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_BRANCHE];
			} else if ("F_PARTNERKLASSE".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PARTNERKLASSE];
			} else if ("F_LKZ".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_LKZ];
			} else if ("F_PLZ".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PLZ];
			} else if ("F_TYP".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_TYP];
			} else if ("F_ZIELTERMIN".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ZIELTERMIN];
			} else if ("F_BELEGDATUM".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ZIELTERMIN];
			} else if ("F_ERLEDIGUNGSDATUM".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ERLEDIGUNGSDATUM];
			} else if ("F_KATEGORIE".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PROJEKTKATEGORIE];
			} else if ("F_TITEL".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PROJEKTTITEL]);
			} else if ("F_PRIO".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PRIO];
			} else if ("F_ERZEUGER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ERZEUGER];
			} else if ("F_DAUER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_DAUER];
			} else if ("F_ZEIT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ZEIT];
			} else if ("F_ZUGEWIESENER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ZUGEWIESENER];
			} else if ("F_ZIELWOCHE".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ZIELWOCHE];
			} else if ("F_TEXT".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_TEXT]);
			} else if ("F_WER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_MITARBEITER];
			} else if ("F_WANN".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_BELEGDATUM];
			} else if ("F_WAS".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_TEXT]);
			} else if ("F_ANSPRECHPARTNER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ANSPRECHPARTNER];
			} else if ("F_DATEINAME".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_DATEINAME];
			} else if ("F_FREIGEGEBEN".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_B_FREIGEGEBEN];
			} else if ("F_VERRECHENBAR".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_B_VERRECHENBAR];
			} else if ("F_ANSPRECHPARTNERCNAME1".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ANSPRECHPARTNERCNAME1];
			} else if ("F_WAHRSCHEINLICHKEIT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_WAHRSCHEINLICHKEIT];
			} else if ("F_UMSATZGEPLANT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_GEPLANTERUMSATZ];
			} else if ("F_GESAMTDAUER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_GESAMTDAUER];
			} else if ("F_INTERNERLEDIGT_PERSON".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_INTERNERLEDIGT_PERSON];
			} else if ("F_INTERNERLEDIGT_ZEIT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_INTERNERLEDIGT_ZEIT];
			}

		} else if (cAktuellerReport.equals(REPORT_GANZSEITIGESBILD)) {
			if ("F_BILD".equals(fieldName)) {
				value = data[index][0];
			}
		} else if (cAktuellerReport
				.equals(ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE)) {
			if ("F_CNR".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_PROJEKTCNR];
			} else if ("F_PARTNER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_KUNDECNAME1];
			} else if ("F_ZIELTERMIN".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_ZIELTERMIN];
			} else if ("F_BELEGDATUM".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_ZIELTERMIN];
			} else if ("F_ERLEDIGUNGSDATUM".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_ERLEDIGUNGSDATUM];
			} else if ("F_KATEGORIE".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_PROJEKTKATEGORIE];
			} else if ("F_TITEL".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_PROJEKTTITEL]);
			} else if ("F_STATUS".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_STATUS];
			} else if ("F_TYP".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_TYP];
			} else if ("F_PRIO".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_PRIO];
			} else if ("F_ERZEUGER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_ERZEUGER];
			} else if ("F_DAUER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DAUER];
			} else if ("F_ZEIT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_ZEIT];
			} else if ("F_ZUGEWIESENER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_ZUGEWIESENER];
			} else if ("F_ZIELWOCHE".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_ZIELWOCHE];
			} else if ("F_TEXT".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_TEXT]);
			} else if ("F_WER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_HISTORY_MITARBEITER];
			} else if ("F_WANN".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_HISTORY_BELEGDATUM];
			} else if ("F_WAS".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_HISTORY_TEXT]);
			} else if ("F_WAHRSCHEINLICHKEIT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_WAHRSCHEINLICHKEIT];
			} else if ("F_UMSATZGEPLANT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_GEPLANTERUMSATZ];
			} else if ("F_INTERNERLEDIGT_PERSON".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_INTERNERLEDIGT_PERSON];
			} else if ("F_INTERNERLEDIGT_ZEIT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_INTERNERLEDIGT_ZEIT];
			}

		} else if (cAktuellerReport
				.equals(ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT)) {
			if ("F_CNR".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_PROJEKTCNR];
			} else if ("F_PARTNER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_KUNDECNAME1];
			} else if ("F_TYP".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_TYP];
			} else if ("F_ZIELTERMIN".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ZIELTERMIN];
			} else if ("F_BELEGDATUM".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_BELEGDATUM];
			} else if ("F_ERLEDIGUNGSDATUM".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ERLEDIGUNGSDATUM];
			} else if ("F_KATEGORIE".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_PROJEKTKATEGORIE];
			} else if ("F_TITEL".equals(fieldName)) {
				// CK: lt WH ist der Titel des Projektes kein Styled Text
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_PROJEKTTITEL];
			} else if ("F_STATUS".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_STATUS];
			} else if ("F_PRIO".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_PRIO];
			} else if ("F_ERZEUGER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ERZEUGER];
			} else if ("F_ERLEDIGER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ERLEDIGER];
			} else if ("F_BELEGDATUM".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_BELEGDATUM];
			} else if ("F_ZEIT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ZEIT];
			} else if ("F_ZUGEWIESENER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ZUGEWIESENER];
			} else if ("F_ZIELWOCHE".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ZIELWOCHE];
			} else if ("F_DAUER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_DAUER];
			} else if ("F_TEXT".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_TEXT]);
			} else if ("F_WER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_HISTORY_MITARBEITER];
			} else if ("F_WANN".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_HISTORY_BELEGDATUM];
			} else if ("F_WAS".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_HISTORY_TEXT]);
			} else if ("F_VERRECHENBAR".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_B_VERRECHENBAR]);
			} else if ("F_FREIGEGEBEN".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_FREIGEGEBEN]);
			} else if ("F_GESAMTDAUER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_GESAMTDAUER];
			} else if ("F_WAHRSCHEINLICHKEIT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_WAHRSCHEINLICHKEIT];
			} else if ("F_UMSATZGEPLANT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_GEPLANTERUMSATZ];
			} else if ("F_INTERNERLEDIGT_PERSON".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_INTERNERLEDIGT_PERSON];
			} else if ("F_INTERNERLEDIGT_ZEIT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_INTERNERLEDIGT_ZEIT];
			}

		}

		else if (cAktuellerReport.equals(ProjektReportFac.REPORT_PROJEKT)) {
			if ("F_POSITION".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_POSITION];
			} else if ("F_TEXT".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][ProjektReportFac.REPORT_PROJEKT_HISTORY_TEXT]);
			} else if ("F_ERZEUGER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_ERZEUGER];
			} else if ("F_DATUM".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_HISTORY_BELEGDATUM];
			} else if ("F_ERLEDIGUNGSDATUM".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_ERLEDIGUNGSDATUM];
			}

			else if ("F_HISTORYART".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_HISTORYART];
			} else if ("F_TITEL".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_TITEL];
			} else if ("F_ROT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_ROT];
			} else if ("F_GRUEN".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_GRUEN];
			} else if ("F_BLAU".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_BLAU];
			}

		} else if (cAktuellerReport
				.equals(ProjektReportFac.REPORT_PROJEKT_JOURNAL_AKTIVITAETSUEBERSICHT)) {
			if ("F_BELEGDETAIL".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEG_SUBREPORT];
			} else if ("F_BELEGART".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGART];
			} else if ("F_BELEGNUMMER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGNUMMER];
			} else if ("F_BELEGTEXT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGTEXT];
			} else if ("F_DAUER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_DAUER];
			} else if ("F_PARTNER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PARTNER];
			} else if ("F_LKZ".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_LKZ];
			} else if ("F_PLZ".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PLZ];
			} else if ("F_ORT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_ORT];
			} else if ("F_PERSONAL".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PERSONAL];
			} else if ("F_PROJEKT_PERSONAL_ERZEUGER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_PERSONAL_ERZEUGER];
			} else if ("F_PROJEKT_BEREICH".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_BEREICH];
			} else if ("F_PROJEKT_PERSONAL_MITARBEITER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_PERSONAL_MITARBEITER];
			} else if ("F_VON".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_VON];
			} else if ("F_BIS".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BIS];
			} else if ("F_PROJEKT_INTERNERLEDIGT_PERSON".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_INTERNERLEDIGT_PERSON];
			} else if ("F_PROJEKT_INTERNERLEDIGT_ZEIT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_INTERNERLEDIGT_ZEIT];
			}
		} else if (cAktuellerReport
				.equals(ProjektReportFac.REPORT_PROJEKTVERLAUF)) {
			if ("F_BELEGART".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_BELEGART];
			} else if ("F_BELEGNUMMER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_BELEGNUMMER];
			} else if ("F_AZ_DAUER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_AZ_DAUER];
			} else if ("F_AZ_KOSTEN".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_AZ_KOSTEN];
			} else if ("F_MATERIALWERT_LOS".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_MATERIALWERT_LOS];
			} else if ("F_AZWERT_LOS".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_AZWERT_LOS];
			} else if ("F_GESTWERT_AZ".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_GESTWERT_AZ];
			} else if ("F_GESTWERT_MATERIAL".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_GESTWERT_MATERIAL];
			} else if ("F_VKWERT_AZ".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_VKWERT_AZ];
			} else if ("F_VKWERT_MATERIAL".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_VKWERT_MATERIAL];
			} else if ("F_EKWERT_AZ".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_EKWERT_AZ];
			} else if ("F_EKWERT_MATERIAL".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_EKWERT_MATERIAL];
			} else if ("F_EBENE".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_EBENE];
			} else if ("F_ABLIEFERMENGE_LOS".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_LOS_ABGELIEFERTE_MENGE];
			} else if ("F_LIEFERANT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_LIEFERANT];
			} else if ("F_STATUS".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_STATUS];
			} else if ("F_AUFTRAG_BESTELLNUMMER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_AUFTRAG_BESTELLNUMMER];
			} else if ("F_LIEFERTERMIN".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_LIEFERTERMIN];
			} else if ("F_LOS_BEGINN".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_LOS_BEGINN];
			} else if ("F_LOS_ENDE".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_LOS_ENDE];
			} else if ("F_BELEGDATUM".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_BELEGDATUM];
			} else if ("F_REISE_KOMMENTAR".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_REISE_KOMMENTAR];
			} else if ("F_TELEFON_KOMMENTAR_EXTERN".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_TELEFON_KOMMENTAR_EXTERN];
			} else if ("F_TELEFON_KOMMENTAR_INTERN".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_TELEFON_KOMMENTAR_INTERN];
			} else if ("F_ER_KEINE_AUFTRAGSWERTUNG".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_ER_KEINE_AUFTRAGSWERTUNG];
			}

			else if ("F_LOSANTEIL_LIEFERSCHEIN_LOSNUMMER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_LOSANTEIL_LIEFERSCHEIN_LOSNUMMER];
			} else if ("F_LOSANTEIL_LIEFERSCHEIN_EINSTANDSWERT_MATERIAL"
					.equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_LOSANTEIL_LIEFERSCHEIN_EINSTANDSWERT_MATERIAL];
			} else if ("F_LOSANTEIL_LIEFERSCHEIN_EINSTANDSWERT_AZ"
					.equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_LOSANTEIL_LIEFERSCHEIN_EINSTANDSWERT_AZ];
			}

		} else if (cAktuellerReport
				.equals(ProjektReportFac.REPORT_PROJEKTZEITDATEN)) {
			if ("Belegart".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTZEITEN_BELEGART];
			} else if ("Belegnummer".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTZEITEN_BELEG];
			} else if ("Ende".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTZEITEN_BIS];
			} else if ("Dauer".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTZEITEN_DAUER];
			} else if ("Person".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTZEITEN_PERSON];
			} else if ("Beginn".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTZEITEN_VON];
			} else if ("Artikel".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTZEITEN_ARTIKEL];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTZEITEN_BEZEICHNUNG];
			} else if ("Bemerkung".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTZEITEN_BEMERKUNG];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTZEITEN_KOMMENTAR];
			} else if ("Kosten".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTZEITEN_KOSTEN];
			} else if ("Telefonzeit".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTZEITEN_TELEFONZEIT];
			}

		}

		return value;
	}

	public boolean next() throws JRException {
		index++;
		return (index < data.length);
	}

	private String buildSortierungProjektAlle(
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

	private String buildFilterProjektAlle(
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

	private String buildFilterProjektErledigt(
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

	private String buildSortierungProjektOffene(
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

	private String buildFilterProjektOffene(
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

}
