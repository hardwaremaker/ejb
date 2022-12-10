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
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.LagerabgangursprungDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.artikel.service.WarenzugangsreferenzDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragzeitenDto;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungAuftragszuordnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.fertigung.service.GesamtkalkulationDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosablieferungDto;
import com.lp.server.fertigung.service.LosistmaterialDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.LfliefergruppeDto;
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
import com.lp.server.projekt.fastlanereader.generated.FLRProjektgruppe;
import com.lp.server.projekt.fastlanereader.generated.FLRProjekttechniker;
import com.lp.server.projekt.fastlanereader.generated.FLRVkfortschrittspr;
import com.lp.server.projekt.service.BereichDto;
import com.lp.server.projekt.service.HistoryDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.projekt.service.ProjektFac;
import com.lp.server.projekt.service.ProjektReportFac;
import com.lp.server.projekt.service.ProjektServiceFac;
import com.lp.server.projekt.service.ProjektVerlaufHelperDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungartDto;
import com.lp.server.reklamation.service.ReklamationDto;
import com.lp.server.reklamation.service.ReklamationFac;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.fastlanereader.generated.FLREntitylog;
import com.lp.server.system.service.EditorContentDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.DatumsfilterVonBis;
import com.lp.server.util.HelperServer;
import com.lp.server.util.HvOptional;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.server.util.report.TimingInterceptor;
import com.lp.service.BelegDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.LPBlockContentSubreport;
import com.lp.util.LPDatenSubreport;
import com.lp.util.report.PersonRpt;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

@Stateless
@Interceptors(TimingInterceptor.class)
public class ProjektReportFacBean extends LPReport implements ProjektReportFac, JRDataSource {

	@PersistenceContext
	private EntityManager em;

	private String cAktuellerReport = null;
	private Object[][] data = null;

	/*
	 * font-family:"Calibri","sans-serif"; <p><span
	 * style="font-size: 10pt; font-family: arial,helvetica,sans-serif;">Sehr
	 * geehrte Damen und Herren,<br /></span></p> <p><span
	 * style="font-size: 10pt; font-family: arial,helvetica,sans-serif;"
	 * >grunds&auml;tzlich ist es so, dass die erste Positi *
	 */

	/**
	 * Den Tag font-family entfernen</br>
	 * <p>
	 * Dieser f&uuml;hrt beim Druck von als HTML markierten Text dazu, dass Jasper
	 * den Java Font nicht finden kann. Bisher wurde noch nicht n&auml;her eruiert,
	 * warum dieser Fehler entsteht, denn die Fonts sind ansich vorhanden und
	 * funktionieren bei normalen Markup.
	 * </p>
	 * 
	 * @param html
	 * @return html ohne den font-family tag
	 */
	private String stripHtmlFontFamily(String html) {
		int indexFontfamily;
		while ((indexFontfamily = html.indexOf("font-family:")) > -1) {
			int indexSemikolon = html.indexOf(";", indexFontfamily);
			if (indexSemikolon > -1) {
				String s = html.substring(0, indexFontfamily);
				String t = html.substring(indexSemikolon + 1);
				html = s + t;
			} else {
				break;
			}
		}
		return html;
	}

	private String stripHtmlFontFace(String html) {
		String faceStart = "face=\"";
		int startIndex = 0;
		int indexFont;
		while ((indexFont = html.indexOf("<font", startIndex)) > -1) {
			int indexFontEnd = html.indexOf(">", indexFont);
			if (indexFontEnd > -1) {
				int indexFace = html.indexOf(faceStart, indexFont);
				if (indexFace > -1 && indexFace < indexFontEnd) {
					int indexFaceEnd = html.indexOf("\"", indexFace + faceStart.length() + 1);
					if (indexFaceEnd > -1) {
						String s = html.substring(0, indexFace);
						String t = html.substring(indexFaceEnd + 1);
						html = s + t;
						startIndex = indexFace;
					} else {
						break;
					}
				} else {
					startIndex = indexFontEnd;
				}
			} else {
				break;
			}
		}

		return html;
	}

	private String stripHtmlHead(String html) {
		int headBegin = html.indexOf("<head>");
		if (headBegin == -1)
			return html;

		int headEnd = html.indexOf("</head>");
		if (headEnd == -1)
			return html;

		String s = html.substring(0, headBegin);
		String t = html.substring(headEnd + 7);
		return s + t;
	}

	private String stripHtmlFontFamilyIfHtml(String possibleHtml, Short isHtml) {
		if (!Helper.short2boolean(isHtml))
			return possibleHtml;

		String html = stripHtmlFontFamily(stripHtmlHead(possibleHtml));
		html = stripHtmlFontFace(html);
		return html;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printProjektverlauf(Integer projektIId, boolean bMitAZDetails, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		cAktuellerReport = ProjektReportFac.REPORT_PROJEKTVERLAUF;

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		ProjektDto projektDto = getProjektFac().projektFindByPrimaryKey(projektIId);

		LinkedHashMap<String, ProjektVerlaufHelperDto> hm = getProjektFac().getProjektVerlauf(projektIId, theClientDto);

		PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(projektDto.getPartnerIId(), theClientDto);
		MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(projektDto.getMandantCNr(), theClientDto);

		Locale locDruck = Helper.string2Locale(partnerDto.getLocaleCNrKommunikation());

		KundeDto kdDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(projektDto.getPartnerIId(),
				projektDto.getMandantCNr(), theClientDto);

		Integer kundeIIdFuerVKPreis = null;
		if (kdDto != null) {
			kundeIIdFuerVKPreis = kdDto.getIId();
		}

		parameter.put("P_PROJEKTNUMMER", projektDto.getCNr());

		parameter.put("P_MIT_AZ_DETAILS", bMitAZDetails);

		BereichDto bereichDto = getProjektServiceFac().bereichFindByPrimaryKey(projektDto.getBereichIId());

		parameter.put("P_BEREICH", bereichDto.getCBez());

		parameter.put("P_TITEL", projektDto.getCTitel());
		parameter.put("P_KUNDE_ADRESSBLOCK", formatAdresseFuerAusdruck(partnerDto, null, mandantDto, locDruck));

		parameter.put("P_MANDANTENWAEHRUNG", theClientDto.getSMandantenwaehrung());

		ParametermandantDto parameterAufschlag = (ParametermandantDto) getParameterFac().getMandantparameter(
				theClientDto.getMandant(), ParameterFac.KATEGORIE_PROJEKT, ParameterFac.PARAMETER_AUFSCHLAG1,
				projektDto.getTAnlegen());

		double dAufschlag1 = ((Double) parameterAufschlag.getCWertAsObject()).doubleValue();
		parameter.put("P_AUFSCHLAG1", dAufschlag1);

		parameterAufschlag = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
				ParameterFac.KATEGORIE_PROJEKT, ParameterFac.PARAMETER_AUFSCHLAG2, projektDto.getTAnlegen());

		double dAufschlag2 = ((Double) parameterAufschlag.getCWertAsObject()).doubleValue();
		parameter.put("P_AUFSCHLAG2", dAufschlag2);

		ArrayList alDaten = new ArrayList();

		Iterator<String> it = hm.keySet().iterator();

		boolean bProjektHinzugefuegt = false;

		Object[] oZeileProjekt = new Object[ProjektReportFac.REPORT_PROJEKTVERLAUF_ANZAHL_SPALTEN];
		oZeileProjekt[ProjektReportFac.REPORT_PROJEKTVERLAUF_BELEGART] = LocaleFac.BELEGART_PROJEKT;
		oZeileProjekt[ProjektReportFac.REPORT_PROJEKTVERLAUF_BELEGNUMMER] = projektDto.getCNr();
		oZeileProjekt[ProjektReportFac.REPORT_PROJEKTVERLAUF_EBENE] = 0;
		oZeileProjekt[ProjektReportFac.REPORT_PROJEKTVERLAUF_STATUS] = projektDto.getStatusCNr();

		ArrayList<Object[]> alZeilenAZ = befuelleZeileProjektverlaufMitZeitdaten(LocaleFac.BELEGART_PROJEKT,
				projektDto.getIId(), oZeileProjekt, kundeIIdFuerVKPreis, bMitAZDetails, theClientDto);

		alDaten.addAll(alZeilenAZ);

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

			BigDecimal bdEinstandswertMaterial = BigDecimal.ZERO;
			BigDecimal bdLief1WertMaterial = BigDecimal.ZERO;

			BigDecimal bdEinstandswertHandeingaben = BigDecimal.ZERO;

			Object[] oZeile = new Object[ProjektReportFac.REPORT_PROJEKTVERLAUF_ANZAHL_SPALTEN];

			if (belegDto.getBelegDto() instanceof AngebotDto) {
				AngebotDto dto = (AngebotDto) belegDto.getBelegDto();
				belegart = LocaleFac.BELEGART_ANGEBOT;
				belegnummer = dto.getCNr();
				belegIId = dto.getIId();

				bdGestMaterialwert = getAngebotFac().berechneGestehungswertSoll(dto.getIId(),
						ArtikelFac.ARTIKELART_ARTIKEL, true, theClientDto);
				bdGestAZWert = getAngebotFac().berechneGestehungswertSoll(dto.getIId(),
						ArtikelFac.ARTIKELART_ARBEITSZEIT, true, theClientDto);

				bdVKMaterialwert = getAngebotFac().berechneVerkaufswertSoll(dto.getIId(), ArtikelFac.ARTIKELART_ARTIKEL,
						theClientDto);
				bdVKAZWert = getAngebotFac().berechneVerkaufswertSoll(dto.getIId(), ArtikelFac.ARTIKELART_ARBEITSZEIT,
						theClientDto);
				belegdatum = dto.getTBelegdatum();

				status = dto.getStatusCNr();

			} else if (belegDto.getBelegDto() instanceof AuftragDto) {
				AuftragDto dto = (AuftragDto) belegDto.getBelegDto();
				belegart = LocaleFac.BELEGART_AUFTRAG;
				belegnummer = dto.getCNr();
				belegIId = dto.getIId();

				bdGestMaterialwert = getAuftragFac().berechneGestehungswertSoll(dto.getIId(),
						ArtikelFac.ARTIKELART_ARTIKEL, true, theClientDto);
				bdGestAZWert = getAuftragFac().berechneGestehungswertSoll(dto.getIId(),
						ArtikelFac.ARTIKELART_ARBEITSZEIT, true, theClientDto);

				bdVKMaterialwert = getAuftragFac().berechneVerkaufswertSoll(dto.getIId(), ArtikelFac.ARTIKELART_ARTIKEL,
						theClientDto);
				bdVKAZWert = getAuftragFac().berechneVerkaufswertSoll(dto.getIId(), ArtikelFac.ARTIKELART_ARBEITSZEIT,
						theClientDto);
				belegdatum = dto.getTBelegdatum();

				status = dto.getStatusCNr();

				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_AUFTRAG_BESTELLNUMMER] = dto.getCBestellnummer();
				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_LIEFERTERMIN] = dto.getDLiefertermin();

			} else if (belegDto.getBelegDto() instanceof LieferscheinDto) {
				LieferscheinDto dto = (LieferscheinDto) belegDto.getBelegDto();
				belegart = LocaleFac.BELEGART_LIEFERSCHEIN;
				belegnummer = dto.getCNr();

				bdGestMaterialwert = getLieferscheinFac().berechneGestehungswertOderEinstandwertIst(dto.getIId(), null,
						ArtikelFac.ARTIKELART_ARTIKEL, true, theClientDto);
				bdGestAZWert = getLieferscheinFac().berechneGestehungswertOderEinstandwertIst(dto.getIId(), null,
						ArtikelFac.ARTIKELART_ARBEITSZEIT, true, theClientDto);

				if (Helper.short2boolean(dto.getBVerrechenbar()) == true) {
					if (!dto.getLieferscheinartCNr().equals(LieferscheinFac.LSART_LIEFERANT)) {
						bdVKMaterialwert = getLieferscheinFac().berechneVerkaufswertIst(dto.getIId(), null,
								ArtikelFac.ARTIKELART_ARTIKEL, theClientDto);
						bdVKAZWert = getLieferscheinFac().berechneVerkaufswertIst(dto.getIId(), null,
								ArtikelFac.ARTIKELART_ARBEITSZEIT, theClientDto);
					}
				}

				bdEKMaterialwert = getLagerFac().getEinstandsWertEinesBeleges(LocaleFac.BELEGART_LIEFERSCHEIN,
						dto.getIId(), ArtikelFac.ARTIKELART_ARTIKEL, theClientDto);

				bdEKAZWert = getLagerFac().getEinstandsWertEinesBeleges(LocaleFac.BELEGART_LIEFERSCHEIN, dto.getIId(),
						ArtikelFac.ARTIKELART_ARBEITSZEIT, theClientDto);

				belegdatum = dto.getTBelegdatum();

				status = dto.getStatusCNr();

				Collection<LieferscheinpositionDto> alLsPos = getLieferscheinpositionFac()
						.lieferscheinpositionFindByLieferscheinIId(dto.getIId(), theClientDto);

				HashMap<Integer, GesamtkalkulationDto> hmLosablieferungGesamtkalkulationsdaten = new HashMap<Integer, GesamtkalkulationDto>();

				Iterator itLSPOS = alLsPos.iterator();
				while (itLSPOS.hasNext()) {

					LieferscheinpositionDto lsPosDto = (LieferscheinpositionDto) itLSPOS.next();

					if (lsPosDto.getNMenge() != null && lsPosDto.getArtikelIId() != null) {

						ArrayList<WarenzugangsreferenzDto> wzu = getLagerFac().getWareneingangsreferenz(belegart,
								lsPosDto.getIId(), lsPosDto.getSeriennrChargennrMitMenge(), false, theClientDto);

						BigDecimal einstandspreis = BigDecimal.ZERO;
						BigDecimal lief1Preis = BigDecimal.ZERO;

						for (int k = 0; k < wzu.size(); k++) {

							WarenzugangsreferenzDto ref = wzu.get(k);

							if (ref.getBelegart().equals(LocaleFac.BELEGART_LOSABLIEFERUNG)) {

								GesamtkalkulationDto gkDto = null;
								if (hmLosablieferungGesamtkalkulationsdaten.containsKey(ref.getBelegartpositionIId())) {
									gkDto = hmLosablieferungGesamtkalkulationsdaten.get(ref.getBelegartpositionIId());
								} else {
									gkDto = getFertigungReportFac().getDatenGesamtkalkulation(ref.getBelegartIId(),
											ref.getBelegartpositionIId(), 99, theClientDto);
									hmLosablieferungGesamtkalkulationsdaten.put(ref.getBelegartpositionIId(), gkDto);
								}

								einstandspreis = gkDto.getDurchschnittlicherMaterialpreisProStueck();

								lief1Preis = gkDto.getDurchschnittlicherLief1WertProStueck();

							} else {
								einstandspreis = ref.getnEinstandspreis();

								ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreis(
										lsPosDto.getArtikelIId(), null, lsPosDto.getNMenge(),
										theClientDto.getSMandantenwaehrung(),
										new java.sql.Date(dto.getTBelegdatum().getTime()), theClientDto);
								if (alDto != null && alDto.getNNettopreis() != null) {
									lief1Preis = alDto.getNNettopreis();
								}

							}
							bdEinstandswertMaterial = bdEinstandswertMaterial
									.add(einstandspreis.multiply(ref.getMenge()));

							bdLief1WertMaterial = bdLief1WertMaterial.add(lief1Preis.multiply(ref.getMenge()));

						}

					}
				}

			} else if (belegDto.getBelegDto() instanceof RechnungDto) {
				RechnungDto dto = (RechnungDto) belegDto.getBelegDto();
				belegart = dto.getRechnungartCNr();
				belegnummer = dto.getCNr();

				boolean bGutschrift = false;

				RechnungartDto raDto = getRechnungServiceFac().rechnungartFindByPrimaryKey(dto.getRechnungartCNr(),
						theClientDto);

				if (raDto.getRechnungtypCNr().equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
					bGutschrift = true;
				}

				bdGestMaterialwert = getRechnungFac().berechneGestehungswertOderEinstandwertIst(dto,
						ArtikelFac.ARTIKELART_ARTIKEL, true, theClientDto);
				bdGestAZWert = getRechnungFac().berechneGestehungswertOderEinstandwertIst(dto,
						ArtikelFac.ARTIKELART_ARBEITSZEIT, true, theClientDto);

				bdVKMaterialwert = getRechnungFac().berechneVerkaufswertIst(dto, ArtikelFac.ARTIKELART_ARTIKEL,
						theClientDto);
				bdVKAZWert = getRechnungFac().berechneVerkaufswertIst(dto, ArtikelFac.ARTIKELART_ARBEITSZEIT,
						theClientDto);

				bdEKMaterialwert = getLagerFac().getEinstandsWertEinesBeleges(
						bGutschrift ? LocaleFac.BELEGART_GUTSCHRIFT : LocaleFac.BELEGART_RECHNUNG, dto.getIId(),
						ArtikelFac.ARTIKELART_ARTIKEL, theClientDto);

				bdEKAZWert = getLagerFac().getEinstandsWertEinesBeleges(
						bGutschrift ? LocaleFac.BELEGART_GUTSCHRIFT : LocaleFac.BELEGART_RECHNUNG, dto.getIId(),
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

				bdEKMaterialwert = getBestellungFac().berechneEinkaufswertIst(dto.getIId(),
						ArtikelFac.ARTIKELART_ARTIKEL, theClientDto);
				bdEKAZWert = getBestellungFac().berechneEinkaufswertIst(dto.getIId(), ArtikelFac.ARTIKELART_ARBEITSZEIT,
						theClientDto);

				bdEinstandswertHandeingaben = getBestellungFac().berechneEinstandswertEinerBestellung(dto.getIId(),
						true, theClientDto);

				belegdatum = dto.getDBelegdatum();

				status = dto.getStatusCNr();

				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_LIEFERANT] = getLieferantFac()
						.lieferantFindByPrimaryKey(dto.getLieferantIIdBestelladresse(), theClientDto).getPartnerDto()
						.formatFixName1Name2();

				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_LIEFERTERMIN] = dto.getDLiefertermin();

				WareneingangDto[] weDtos = getWareneingangFac().wareneingangFindByBestellungIId(dto.getIId());
				if (weDtos != null && weDtos.length > 0) {
					String ers = "";
					for (int i = 0; i < weDtos.length; i++) {

						Integer erIId = weDtos[i].getEingangsrechnungIId();

						if (erIId != null) {
							EingangsrechnungDto erDto = getEingangsrechnungFac()
									.eingangsrechnungFindByPrimaryKey(erIId);
							ers += erDto.getCNr() + ",";

						}

						// SP5849
						BigDecimal weWertInBSWaehrung = getWareneingangFac().getWareneingangWertsumme(weDtos[i],
								theClientDto);
						if (weWertInBSWaehrung != null) {
							BigDecimal wechselkursmandantwaehrungzuBSwaehrung = new BigDecimal(
									dto.getFWechselkursmandantwaehrungzubelegwaehrung().doubleValue());
							BigDecimal bdWertinmandantenwaehrung = getBetragMalWechselkurs(weWertInBSWaehrung,
									Helper.getKehrwert(wechselkursmandantwaehrungzuBSwaehrung));

							bdEinstandswertMaterial = bdEinstandswertMaterial.add(bdWertinmandantenwaehrung);
						}

					}
					if (ers.endsWith(",")) {
						ers = ers.substring(0, ers.length() - 1);
					}

					oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_BESTELLUNG_VERRECHNET_MIT_ER] = ers;

				}

			} else if (belegDto.getBelegDto() instanceof AnfrageDto) {
				AnfrageDto dto = (AnfrageDto) belegDto.getBelegDto();
				belegart = LocaleFac.BELEGART_ANFRAGE;
				belegnummer = dto.getCNr();

				bdEKMaterialwert = getAnfrageFac().berechneEinkaufswertIst(dto.getIId(), ArtikelFac.ARTIKELART_ARTIKEL,
						theClientDto);
				bdEKAZWert = getAnfrageFac().berechneEinkaufswertIst(dto.getIId(), ArtikelFac.ARTIKELART_ARBEITSZEIT,
						theClientDto);

				belegdatum = dto.getTBelegdatum();

				status = dto.getStatusCNr();

				// SP6015 Wenn Liefergruppenanfrage, dannn auslassen
				if (dto.getLieferantIIdAnfrageadresse() != null) {

					oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_LIEFERANT] = getLieferantFac()
							.lieferantFindByPrimaryKey(dto.getLieferantIIdAnfrageadresse(), theClientDto)
							.getPartnerDto().formatFixName1Name2();
				} else {
					if (dto.getLiefergruppeIId() != null) {

						LfliefergruppeDto lgDto = getLieferantServicesFac()
								.lfliefergruppeFindByPrimaryKey(dto.getLiefergruppeIId(), theClientDto);

						oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_LIEFERANT] = lgDto.getBezeichnung();
					}
				}

			} else if (belegDto.getBelegDto() instanceof LosDto) {
				LosDto dto = (LosDto) belegDto.getBelegDto();
				belegart = LocaleFac.BELEGART_LOS;
				belegnummer = dto.getCNr();
				belegIId = dto.getIId();

				status = dto.getStatusCNr();

				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_LOS_BEGINN] = dto.getTProduktionsbeginn();
				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_LOS_ENDE] = dto.getTProduktionsende();

				if (dto.getStuecklisteIId() != null) {
					StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(dto.getStuecklisteIId(),
							theClientDto);
					oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_LOS_STKL_NR] = stklDto.getArtikelDto().getCNr();
					oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_LOS_STKL_BEZ] = stklDto.getArtikelDto()
							.getCBezAusSpr();
					oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_LOS_STKL_ZBEZ] = stklDto.getArtikelDto()
							.getCZBezAusSpr();

				}

				BigDecimal ablMenge = new BigDecimal(0);
				BigDecimal ablWert = new BigDecimal(0);
				LosablieferungDto[] ablDtos = getFertigungFac().losablieferungFindByLosIId(dto.getIId(), true,
						theClientDto);
				for (int i = 0; i < ablDtos.length; i++) {
					ablMenge = ablMenge.add(ablDtos[i].getNMenge());
					ablWert = ablWert.add(ablDtos[i].getNMaterialwert().multiply(ablDtos[i].getNMenge()));
				}

				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_MATERIALWERT_LOS] = ablWert;
				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_LOS_ABGELIEFERTE_MENGE] = ablMenge;

				bdEKMaterialwert = getLagerFac().getEinstandsWertEinesBeleges(LocaleFac.BELEGART_LOS, dto.getIId(),
						ArtikelFac.ARTIKELART_ARTIKEL, theClientDto);

				// AZ
				AuftragzeitenDto[] belegzeitenDtos = getZeiterfassungFac().getAllZeitenEinesBeleges(
						LocaleFac.BELEGART_LOS, dto.getIId(), null, null, null, null,
						ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ARTIKEL, false, theClientDto);
				BigDecimal bdKostenGesamt = BigDecimal.ZERO;

				for (int i = 0; i < belegzeitenDtos.length; i++) {
					bdKostenGesamt = bdKostenGesamt.add(belegzeitenDtos[i].getBdKosten());
				}

				bdEKAZWert = bdKostenGesamt;

			} else if (belegDto.getBelegDto() instanceof ReiseDto) {
				ReiseDto reiseDto = (ReiseDto) belegDto.getBelegDto();

				belegart = "Reise";
				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_REISE_KOMMENTAR] = reiseDto.getCKommentar();
				bdGestMaterialwert = reiseDto.getNKostenDesAbschnitts();

			} else if (belegDto.getBelegDto() instanceof EingangsrechnungAuftragszuordnungDto) {
				EingangsrechnungAuftragszuordnungDto eaDto = (EingangsrechnungAuftragszuordnungDto) belegDto
						.getBelegDto();
				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_ER_KEINE_AUFTRAGSWERTUNG] = Helper
						.short2Boolean(eaDto.getBKeineAuftragswertung());

				EingangsrechnungDto erDto = getEingangsrechnungFac()
						.eingangsrechnungFindByPrimaryKey(eaDto.getEingangsrechnungIId());

				belegart = LocaleFac.BELEGART_EINGANGSRECHNUNG;
				belegnummer = erDto.getCNr();

				bdGestMaterialwert = getLocaleFac().rechneUmInMandantenWaehrung(eaDto.getNBetrag(), erDto.getNKurs());

				// PJ22154
				if (erDto.getLieferantIId() != null) {
					oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_LIEFERANT] = getLieferantFac()
							.lieferantFindByPrimaryKey(erDto.getLieferantIId(), theClientDto).getPartnerDto()
							.formatFixName1Name2();
				}

				belegdatum = erDto.getDBelegdatum();

				status = erDto.getStatusCNr();

			} else if (belegDto.getBelegDto() instanceof AgstklDto) {
				AgstklDto agstklDto = (AgstklDto) belegDto.getBelegDto();

				belegart = LocaleFac.BELEGART_AGSTUECKLISTE;
				belegnummer = agstklDto.getCNr();

			} else if (belegDto.getBelegDto() instanceof ReklamationDto) {
				ReklamationDto reklamationDto = (ReklamationDto) belegDto.getBelegDto();

				belegart = LocaleFac.BELEGART_REKLAMATION;
				belegnummer = reklamationDto.getCNr();

				bdGestAZWert = reklamationDto.getNKostenarbeitszeit();
				bdGestMaterialwert = reklamationDto.getNKostenmaterial();

			} else if (belegDto.getBelegDto() instanceof TelefonzeitenDto) {
				TelefonzeitenDto telefonzeitenDto = (TelefonzeitenDto) belegDto.getBelegDto();
				belegart = "Telefon";

				// Die Kosten kommen aus dem Stundensatz
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(telefonzeitenDto.getTVon().getTime());

				PersonalgehaltDto pgDto = getPersonalFac().personalgehaltFindLetztePersonalgehalt(
						telefonzeitenDto.getPersonalIId(), c.get(Calendar.YEAR), c.get(Calendar.MONTH));

				if (telefonzeitenDto.getTBis() != null) {
					Double dauer = new Double(
							((double) (telefonzeitenDto.getTBis().getTime() - telefonzeitenDto.getTVon().getTime())
									/ 3600000));

					oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_AZ_DAUER] = dauer;

					if (pgDto != null && pgDto.getNStundensatz() != null) {

						oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_AZ_KOSTEN] = pgDto.getNStundensatz()
								.multiply(new BigDecimal(dauer));

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
			oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_EBENE] = belegDto.getiEbene();

			if (status != null && status.equals(LocaleFac.STATUS_STORNIERT)) {

			} else {

				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_VKWERT_MATERIAL] = bdVKMaterialwert;
				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_VKWERT_AZ] = bdVKAZWert;

				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_GESTWERT_MATERIAL] = bdGestMaterialwert;
				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_GESTWERT_AZ] = bdGestAZWert;

				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_EKWERT_MATERIAL] = bdEKMaterialwert;
				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_EKWERT_AZ] = bdEKAZWert;

				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_EINSTANDSWERT_MATERIAL] = bdEinstandswertMaterial;
				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_LIEF1WERT_MATERIAL] = bdEinstandswertMaterial;

				oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_EINSTANDSWERT_HANDEINGABEN] = bdEinstandswertHandeingaben;

			}

			oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_STATUS] = status;

			ArrayList<Object[]> alZeilenAZ2 = befuelleZeileProjektverlaufMitZeitdaten(belegart, belegIId, oZeile,
					kundeIIdFuerVKPreis, bMitAZDetails, theClientDto);

			if (belegDto.getBelegDto() instanceof LosDto) {

				for (int i = 0; i < alZeilenAZ2.size(); i++) {
					if (alZeilenAZ2.get(i)[ProjektReportFac.REPORT_PROJEKTVERLAUF_AZ_KOSTEN] != null) {
						alZeilenAZ2.get(i)[ProjektReportFac.REPORT_PROJEKTVERLAUF_AZWERT_LOS] = new BigDecimal(
								((BigDecimal) alZeilenAZ2.get(i)[ProjektReportFac.REPORT_PROJEKTVERLAUF_AZ_KOSTEN])
										.doubleValue());
					}
				}

			}

			alDaten.addAll(alZeilenAZ2);

			if (belegDto.getBelegDto() instanceof LieferscheinDto) {

				LieferscheinDto dto = (LieferscheinDto) belegDto.getBelegDto();

				LieferscheinpositionDto[] lsposDtos = getLieferscheinpositionFac()
						.lieferscheinpositionFindByLieferscheinIId(dto.getIId());

				for (int i = 0; i < lsposDtos.length; i++) {

					alDaten = losablieferungHinzufuegen(belegart, lsposDtos[i].getIId(), theClientDto,
							lsposDtos[i].getNMenge(), belegDto.getiEbene() + 1, alDaten);
				}
			}

		}

		Object[][] returnArray = new Object[alDaten.size()][ProjektReportFac.REPORT_PROJEKTVERLAUF_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(returnArray);

		initJRDS(parameter, ProjektReportFac.REPORT_MODUL, ProjektReportFac.REPORT_PROJEKTVERLAUF,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	private ArrayList losablieferungHinzufuegen(String belegartCNr, Integer belegpositionIId, TheClientDto theClientDto,
			BigDecimal bdVerbrauchteMenge, int iEbene, ArrayList alDaten) throws RemoteException {
		// PJ18623

		BigDecimal bdLosanteilImLieferschein = new BigDecimal(0);
		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "select distinct lagerbewegung.i_id_buchung from FLRLagerbewegung lagerbewegung WHERE lagerbewegung.c_belegartnr='"
				+ belegartCNr + "' AND lagerbewegung.i_belegartpositionid=" + belegpositionIId;

		Query inventurliste = session.createQuery(sQuery);
		List<?> resultList = inventurliste.list();
		Iterator<?> resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {
			Integer o = (Integer) resultListIterator.next();
			LagerabgangursprungDto[] dtos = getLagerFac().lagerabgangursprungFindByLagerbewegungIIdBuchung(o);

			// Fuer jeden Lagerabgangs- Ursprung, der aus einem Los
			// kommt, einen zusaetzlichen eintrag anlegen
			for (int j = 0; j < dtos.length; j++) {
				// aber nur wenn verbrauchte menge grosser 0
				LagerabgangursprungDto dto = dtos[j];
				if (dto.getNVerbrauchtemenge().doubleValue() != 0) {
					Session session2 = FLRSessionFactory.getFactory().openSession();
					String sQuery2 = "from FLRLagerbewegung lagerbewegung WHERE lagerbewegung.i_id_buchung="
							+ dtos[j].getILagerbewegungidursprung()
							+ " AND lagerbewegung.b_historie=0 order by lagerbewegung.t_buchungszeit DESC";
					Query ursrungsbuchung = session2.createQuery(sQuery2);
					ursrungsbuchung.setMaxResults(1);

					List<?> resultList2 = ursrungsbuchung.list();

					com.lp.server.artikel.fastlanereader.generated.FLRLagerbewegung lagerbewegung_ursprung = (com.lp.server.artikel.fastlanereader.generated.FLRLagerbewegung) resultList2
							.iterator().next();

					if (lagerbewegung_ursprung.getC_belegartnr().equals(LocaleFac.BELEGART_LOSABLIEFERUNG)) {

						LosablieferungDto losablieferungDto = getFertigungFac().losablieferungFindByPrimaryKey(
								lagerbewegung_ursprung.getI_belegartpositionid(), true, theClientDto);

						LosDto losDto = getFertigungFac().losFindByPrimaryKey(lagerbewegung_ursprung.getI_belegartid());
						// Neuer Eintrag

						Object[] oZeileLosanteil = new Object[ProjektReportFac.REPORT_PROJEKTVERLAUF_ANZAHL_SPALTEN];

						oZeileLosanteil[ProjektReportFac.REPORT_PROJEKTVERLAUF_LOSANTEIL_LIEFERSCHEIN_LOSNUMMER] = losDto
								.getCNr();

						oZeileLosanteil[ProjektReportFac.REPORT_PROJEKTVERLAUF_EBENE] = iEbene;
						BigDecimal gestWertArbeitIst = losablieferungDto.getNArbeitszeitwertdetailliert()
								.multiply(dto.getNVerbrauchtemenge());

						BigDecimal gestWertMaterialIst = losablieferungDto.getNMaterialwertdetailliert()
								.multiply(dto.getNVerbrauchtemenge());

						oZeileLosanteil[ProjektReportFac.REPORT_PROJEKTVERLAUF_LOSANTEIL_LIEFERSCHEIN_EINSTANDSWERT_MATERIAL] = gestWertMaterialIst;

						bdLosanteilImLieferschein = bdLosanteilImLieferschein.add(gestWertArbeitIst)
								.add(gestWertMaterialIst);

						BigDecimal gesamtAbgeliefert = getFertigungFac().getErledigteMenge(losDto.getIId(),
								theClientDto);

						AuftragzeitenDto[] azDtos = getZeiterfassungFac().getAllZeitenEinesBeleges(
								LocaleFac.BELEGART_LOS, losDto.getIId(), null, null, null, null,
								ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ARTIKEL, theClientDto);

						BigDecimal summeKosten = BigDecimal.ZERO;
						for (int i = 0; i < azDtos.length; i++) {
							summeKosten = summeKosten.add(azDtos[i].getBdKosten());
						}

						oZeileLosanteil[ProjektReportFac.REPORT_PROJEKTVERLAUF_LOSANTEIL_LIEFERSCHEIN_EINSTANDSWERT_AZ] = new BigDecimal(
								summeKosten.doubleValue() / gesamtAbgeliefert.doubleValue()
										* dto.getNVerbrauchtemenge().doubleValue());

						BigDecimal einstandswert = BigDecimal.ZERO;

						LossollmaterialDto[] sollMatDtos = getFertigungFac()
								.lossollmaterialFindByLosIId(losablieferungDto.getLosIId());
						for (int i = 0; i < sollMatDtos.length; i++) {
							LosistmaterialDto[] istmatDto = getFertigungFac()
									.losistmaterialFindByLossollmaterialIId(sollMatDtos[i].getIId());

							for (int k = 0; k < istmatDto.length; k++) {

								// Wert

								List<SeriennrChargennrMitMengeDto> snrs = getLagerFac()
										.getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(
												LocaleFac.BELEGART_LOS, istmatDto[k].getIId());

								for (int m = 0; m < snrs.size(); m++) {

									BigDecimal bdWEinstandswertZeile = getLagerFac().getEinstandspreis(
											LocaleFac.BELEGART_LOS, istmatDto[k].getIId(),
											snrs.get(m).getCSeriennrChargennr());
									if (bdWEinstandswertZeile != null) {
										einstandswert = einstandswert.add(bdWEinstandswertZeile);
									}

								}
							}

						}

						oZeileLosanteil[ProjektReportFac.REPORT_PROJEKTVERLAUF_LOSANTEIL_LIEFERSCHEIN_EINSTANDSWERT_MATERIAL] = new BigDecimal(
								einstandswert.doubleValue() / gesamtAbgeliefert.doubleValue()
										* dto.getNVerbrauchtemenge().doubleValue());

						alDaten.add(oZeileLosanteil);

						for (int i = 0; i < sollMatDtos.length; i++) {
							LosistmaterialDto[] istmatDto = getFertigungFac()
									.losistmaterialFindByLossollmaterialIId(sollMatDtos[i].getIId());

							for (int k = 0; k < istmatDto.length; k++) {
								// Wenn istmaterial aus Los kommt
								losablieferungHinzufuegen(LocaleFac.BELEGART_LOS, istmatDto[k].getIId(), theClientDto,
										istmatDto[k].getNMenge(), iEbene + 1, alDaten);

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

	private ArrayList<Object[]> befuelleZeileProjektverlaufMitZeitdaten(String belegart, Integer belegIId,
			Object[] oZeile, Integer kundeIIdFuerVKPreis, boolean bMitAZDetails, TheClientDto theClientDto) {
		ArrayList<Object[]> alZeilen = new ArrayList();
		if (belegart != null && belegIId != null) {

			try {

				AuftragzeitenDto[] azDtos = getZeiterfassungFac().getAllZeitenEinesBeleges(belegart, belegIId, null,
						null, null, null, ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ARTIKEL, theClientDto);

				// SP8017
				AuftragzeitenDto[] maschinenZeitenDtos = new AuftragzeitenDto[0];

				if (belegart != null && belegart.equals(LocaleFac.BELEGART_LOS)) {
					maschinenZeitenDtos = getZeiterfassungFac().getAllMaschinenzeitenEinesBeleges(belegIId, null, null,
							null, theClientDto);

				}

				if (bMitAZDetails == false || azDtos.length == 0) {

					// Personal
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

					oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_AZ_DAUER] = new Double(zeiten);
					oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_AZ_KOSTEN] = bdKosten;

					// Maschinenkosten
					double maschineZeiten = 0;
					BigDecimal bdMaschinenKosten = new BigDecimal(0);
					for (int i = 0; i < maschinenZeitenDtos.length; i++) {
						if (maschinenZeitenDtos[i] != null && maschinenZeitenDtos[i].getDdDauer() != null) {
							maschineZeiten = maschineZeiten + maschinenZeitenDtos[i].getDdDauer().doubleValue();
						}

						if (maschinenZeitenDtos[i] != null && maschinenZeitenDtos[i].getBdKosten() != null) {
							bdMaschinenKosten = bdMaschinenKosten.add(maschinenZeitenDtos[i].getBdKosten());
						}

					}

					oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_MASCH_DAUER] = new Double(maschineZeiten);
					oZeile[ProjektReportFac.REPORT_PROJEKTVERLAUF_MASCH_KOSTEN] = bdMaschinenKosten;

					alZeilen.add(oZeile);

				} else {

					for (int i = 0; i < azDtos.length; i++) {

						Object[] oZeileClon = oZeile.clone();

						if (i > 0) {

							oZeileClon[REPORT_PROJEKTVERLAUF_AZWERT_LOS] = null;
							oZeileClon[REPORT_PROJEKTVERLAUF_GESTWERT_AZ] = null;
							oZeileClon[REPORT_PROJEKTVERLAUF_MATERIALWERT_LOS] = null;
							oZeileClon[REPORT_PROJEKTVERLAUF_GESTWERT_MATERIAL] = null;
							oZeileClon[REPORT_PROJEKTVERLAUF_EINSTANDSWERT_MATERIAL] = null;
							oZeileClon[REPORT_PROJEKTVERLAUF_LIEF1WERT_MATERIAL] = null;
							oZeileClon[REPORT_PROJEKTVERLAUF_EKWERT_AZ] = null;
							oZeileClon[REPORT_PROJEKTVERLAUF_EKWERT_MATERIAL] = null;
							oZeileClon[REPORT_PROJEKTVERLAUF_LOSANTEIL_LIEFERSCHEIN_EINSTANDSWERT_AZ] = null;
							oZeileClon[REPORT_PROJEKTVERLAUF_LOSANTEIL_LIEFERSCHEIN_EINSTANDSWERT_MATERIAL] = null;
							oZeileClon[REPORT_PROJEKTVERLAUF_LOSANTEIL_LIEFERSCHEIN_LOSNUMMER] = null;
							oZeileClon[REPORT_PROJEKTVERLAUF_LOS_ABGELIEFERTE_MENGE] = null;

						}

						BigDecimal bdVkPreis = null;
						Timestamp belegDatum = azDtos[i].getTsBeginn();
						if (kundeIIdFuerVKPreis != null) {
							KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIIdFuerVKPreis, theClientDto);
							MwstsatzDto mwstsatzKunde = getMandantFac()
									.mwstsatzZuDatumValidate(kundeDto.getMwstsatzbezIId(), belegDatum, theClientDto);
							if (kundeDto.getMwstsatzbezIId() != null) {
								VkpreisfindungDto vkpreisDto = getVkPreisfindungFac().verkaufspreisfindung(
										azDtos[i].getArtikelIId(), kundeIIdFuerVKPreis, new BigDecimal(1),
										new java.sql.Date(belegDatum.getTime()),
										kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
//										getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(
//												kundeDto.getMwstsatzbezIId(),
//										theClientDto).getIId(),
										mwstsatzKunde.getIId(), theClientDto.getSMandantenwaehrung(), theClientDto);

								VerkaufspreisDto kundenVKPreisDto = Helper.getVkpreisBerechnet(vkpreisDto);

								if (kundenVKPreisDto != null && kundenVKPreisDto.nettopreis != null) {
									bdVkPreis = kundenVKPreisDto.nettopreis;
								}

							}
						} else {
							bdVkPreis = getVkPreisfindungFac().ermittlePreisbasis(azDtos[i].getArtikelIId(),
									new java.sql.Date(belegDatum.getTime()), null, theClientDto.getSMandantenwaehrung(),
									theClientDto);
						}

						oZeileClon[ProjektReportFac.REPORT_PROJEKTVERLAUF_AZ_VKPREIS] = bdVkPreis;

						oZeileClon[ProjektReportFac.REPORT_PROJEKTVERLAUF_AZ_DAUER] = azDtos[i].getDdDauer();
						oZeileClon[ProjektReportFac.REPORT_PROJEKTVERLAUF_AZ_KOSTEN] = azDtos[i].getBdKosten();

						oZeileClon[ProjektReportFac.REPORT_PROJEKTVERLAUF_AZ_ARTIKELNUMMER] = azDtos[i]
								.getSArtikelcnr();
						oZeileClon[ProjektReportFac.REPORT_PROJEKTVERLAUF_AZ_ARTIKELBEZEICHNUNG] = azDtos[i]
								.getSArtikelbezeichnung();
						oZeileClon[ProjektReportFac.REPORT_PROJEKTVERLAUF_AZ_ARTIKELGRUPPE] = azDtos[i]
								.getSArtikelgruppe();

						/*
						 * VkpreisfindungDto vkpreisfindungDto = getVkPreisfindungFac()
						 * .verkaufspreisfindung( azDtos[i].getArtikelIId(), kDto_Quellmandant
						 * .getIId(), new BigDecimal(azDtos[i].getDdDauer()), new Date(
						 * azDtos[i].getTsBeginn().getTime()), kDto_Quellmandant
						 * .getVkpfArtikelpreislisteIIdStdpreisliste(), mwstsatzDtoAktuell .getIId(),
						 * theClientDto .getSMandantenwaehrung(), theClientDto);
						 * 
						 * VerkaufspreisDto kundenVKPreisDto = Helper
						 * .getVkpreisBerechnet(vkpreisfindungDto);
						 * 
						 * 
						 * if (kundenVKPreisDto != null && kundenVKPreisDto.nettopreis != null) {
						 * oZeileClon[ProjektReportFac .REPORT_PROJEKTVERLAUF_VKWERT_AZ] =
						 * kundenVKPreisDto.nettopreis; }
						 */

						alZeilen.add(oZeileClon);

					}

					for (int i = 0; i < maschinenZeitenDtos.length; i++) {

						Object[] oZeileClon = oZeile.clone();

						if (i > 0) {

							oZeileClon[REPORT_PROJEKTVERLAUF_AZWERT_LOS] = null;
							oZeileClon[REPORT_PROJEKTVERLAUF_GESTWERT_AZ] = null;
							oZeileClon[REPORT_PROJEKTVERLAUF_MATERIALWERT_LOS] = null;
							oZeileClon[REPORT_PROJEKTVERLAUF_GESTWERT_MATERIAL] = null;
							oZeileClon[REPORT_PROJEKTVERLAUF_EINSTANDSWERT_MATERIAL] = null;
							oZeileClon[REPORT_PROJEKTVERLAUF_LIEF1WERT_MATERIAL] = null;
							oZeileClon[REPORT_PROJEKTVERLAUF_EKWERT_AZ] = null;
							oZeileClon[REPORT_PROJEKTVERLAUF_EKWERT_MATERIAL] = null;
							oZeileClon[REPORT_PROJEKTVERLAUF_LOSANTEIL_LIEFERSCHEIN_EINSTANDSWERT_AZ] = null;
							oZeileClon[REPORT_PROJEKTVERLAUF_LOSANTEIL_LIEFERSCHEIN_EINSTANDSWERT_MATERIAL] = null;
							oZeileClon[REPORT_PROJEKTVERLAUF_LOSANTEIL_LIEFERSCHEIN_LOSNUMMER] = null;
							oZeileClon[REPORT_PROJEKTVERLAUF_LOS_ABGELIEFERTE_MENGE] = null;

						}
						BigDecimal bdVkPreis = null;
						Timestamp belegDatum = maschinenZeitenDtos[i].getTsBeginn();
						if (kundeIIdFuerVKPreis != null) {
							KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIIdFuerVKPreis, theClientDto);
							MwstsatzDto mwstsatzKunde = getMandantFac()
									.mwstsatzZuDatumValidate(kundeDto.getMwstsatzbezIId(), belegDatum, theClientDto);
							if (kundeDto.getMwstsatzbezIId() != null) {
								VkpreisfindungDto vkpreisDto = getVkPreisfindungFac().verkaufspreisfindung(
										maschinenZeitenDtos[i].getArtikelIId(), kundeIIdFuerVKPreis, new BigDecimal(1),
										new java.sql.Date(belegDatum.getTime()),
										kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
//										getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(
//												kundeDto.getMwstsatzbezIId(), theClientDto).getIId(),
										mwstsatzKunde.getIId(), theClientDto.getSMandantenwaehrung(), theClientDto);

								VerkaufspreisDto kundenVKPreisDto = Helper.getVkpreisBerechnet(vkpreisDto);

								if (kundenVKPreisDto != null && kundenVKPreisDto.nettopreis != null) {
									bdVkPreis = kundenVKPreisDto.nettopreis;
								}

							}
						} else {
							bdVkPreis = getVkPreisfindungFac().ermittlePreisbasis(
									maschinenZeitenDtos[i].getArtikelIId(), new java.sql.Date(belegDatum.getTime()),
									null, theClientDto.getSMandantenwaehrung(), theClientDto);
						}

						oZeileClon[ProjektReportFac.REPORT_PROJEKTVERLAUF_MASCH_VKPREIS] = bdVkPreis;

						oZeileClon[ProjektReportFac.REPORT_PROJEKTVERLAUF_MASCH_DAUER] = maschinenZeitenDtos[i]
								.getDdDauer();
						oZeileClon[ProjektReportFac.REPORT_PROJEKTVERLAUF_MASCH_KOSTEN] = maschinenZeitenDtos[i]
								.getBdKosten();

						oZeileClon[ProjektReportFac.REPORT_PROJEKTVERLAUF_AZ_ARTIKELNUMMER] = maschinenZeitenDtos[i]
								.getSArtikelcnr();
						oZeileClon[ProjektReportFac.REPORT_PROJEKTVERLAUF_AZ_ARTIKELBEZEICHNUNG] = maschinenZeitenDtos[i]
								.getSArtikelbezeichnung();
						oZeileClon[ProjektReportFac.REPORT_PROJEKTVERLAUF_AZ_ARTIKELGRUPPE] = maschinenZeitenDtos[i]
								.getSArtikelgruppe();

						alZeilen.add(oZeileClon);

					}

				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		} else {
			alZeilen.add(oZeile);
		}

		return alZeilen;
	}

	private LPDatenSubreport getSubreportVerknuepfteProjekteDerNaechstenEbene(Integer projektIId,
			boolean bVaterprojekte, TheClientDto theClientDto) {
		String[] fieldnames = new String[] { "Projektnummer", "Belegdatum", "Zieltermin", "Titel", "Partner",
				"Kategorie", "Bereich", "Erzeuger", "Zugewiesener", "Umsatzgeplant", "Wahrscheinlichkeit", "Dauer",
				"Status", "DatumInternerledigt" };

		ArrayList alDaten = new ArrayList();

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		session = factory.openSession();
		String queryString = "SELECT pg FROM FLRProjektgruppe pg WHERE ";

		if (bVaterprojekte == true) {
			queryString += "pg.projekt_i_id_kind=" + projektIId + " ORDER BY pg.flrprojekt_vater.c_nr ASC ";
		} else {
			queryString += "pg.projekt_i_id_vater=" + projektIId + " ORDER BY pg.flrprojekt_kind.c_nr ASC ";
		}

		String sLocUI = Helper.locale2String(theClientDto.getLocUi());
		session.enableFilter("filterLocale").setParameter("paramLocale", sLocUI);
		org.hibernate.Query query = session.createQuery(queryString);
		List<?> resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {
			FLRProjektgruppe o = (FLRProjektgruppe) resultListIterator.next();

			FLRProjekt p = null;
			if (bVaterprojekte == true) {
				p = o.getFlrprojekt_vater();
			} else {
				p = o.getFlrprojekt_kind();
			}

			Object[] oZeile = new Object[fieldnames.length];

			oZeile[0] = p.getC_nr();
			oZeile[1] = p.getT_anlegen();
			oZeile[2] = p.getT_zielwunschdatum();
			oZeile[3] = p.getC_titel();
			oZeile[4] = HelperServer.formatNameAusFLRPartner(p.getFlrpartner());
			oZeile[5] = p.getKategorie_c_nr();
			oZeile[6] = p.getFlrbereich().getC_bez();
			oZeile[7] = HelperServer.formatPersonAusFLRPartner(p.getFlrpersonalErzeuger().getFlrpartner());
			oZeile[8] = HelperServer.formatPersonAusFLRPartner(p.getFlrpersonalZugewiesener().getFlrpartner());
			oZeile[9] = p.getN_umsatzgeplant();
			oZeile[10] = p.getI_wahrscheinlichkeit();
			oZeile[11] = p.getD_dauer();
			oZeile[12] = p.getStatus_c_nr();
			oZeile[13] = p.getT_internerledigt();
			alDaten.add(oZeile);
		}

		session.close();

		Object[][] dataSub = new Object[alDaten.size()][fieldnames.length];
		dataSub = (Object[][]) alDaten.toArray(dataSub);
		return new LPDatenSubreport(dataSub, fieldnames);
	}

	private ArrayList holeAlleKopfprojekte(Integer projektIId, ArrayList al) {

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		session = factory.openSession();
		String queryString = "SELECT pg FROM FLRProjektgruppe pg WHERE pg.projekt_i_id_kind=" + projektIId;

		TreeMap tmKopfprojekte = new TreeMap();
		org.hibernate.Query query = session.createQuery(queryString);
		List<?> resultList = query.list();

		if (resultList.size() == 0) {
			al.add(projektIId);
		}

		Iterator<?> resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {
			FLRProjektgruppe o = (FLRProjektgruppe) resultListIterator.next();

			al = holeAlleKopfprojekte(o.getProjekt_i_id_vater(), al);

		}

		return al;

	}

	public JasperPrintLP printProjektbaum(Integer projektIId, TheClientDto theClientDto) {

		cAktuellerReport = ProjektReportFac.REPORT_PROJEKTBAUM;

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		try {
			ProjektDto projektDto = getProjektFac().projektFindByPrimaryKey(projektIId);

			ArrayList alDaten = new ArrayList();

			parameter.put("P_PROJEKT", projektDto.getCNr());

			ArrayList alKopfprojekt = holeAlleKopfprojekte(projektIId, new ArrayList());

			Iterator it = alKopfprojekt.iterator();

			while (it.hasNext()) {

				Integer projektIIdKopf = (Integer) it.next();

				ProjektDto pjDto = getProjektFac().projektFindByPrimaryKey(projektIIdKopf);

				Object[] oZeile = new Object[REPORT_PROJEKTBAUM_ANZAHL_SPALTEN];

				oZeile[REPORT_PROJEKTBAUM_PROJEKTNUMMER] = pjDto.getCNr();
				oZeile[REPORT_PROJEKTBAUM_TITEL] = pjDto.getCTitel();
				oZeile[REPORT_PROJEKTBAUM_STATUS] = pjDto.getStatusCNr();

				oZeile[REPORT_PROJEKTBAUM_KUNDE] = getPartnerFac()
						.partnerFindByPrimaryKey(pjDto.getPartnerIId(), theClientDto).formatAnrede();
				oZeile[REPORT_PROJEKTBAUM_PRIO] = pjDto.getIPrio();
				oZeile[REPORT_PROJEKTBAUM_KATEGORIE] = pjDto.getKategorieCNr();
				oZeile[REPORT_PROJEKTBAUM_TYP] = pjDto.getProjekttypCNr();
				oZeile[REPORT_PROJEKTBAUM_TERMIN] = pjDto.getTZielwunschdatum();

				oZeile[REPORT_PROJEKTBAUM_EBENE] = 0;
				alDaten.add(oZeile);

				alDaten = holeNaechsteEbene(alDaten, projektIIdKopf, 0);

			}

			for (int j = 0; j < alDaten.size(); j++) {

				Object[] o = (Object[]) alDaten.get(j);

				int iEbene = (Integer) o[REPORT_PROJEKTBAUM_EBENE];

				String einrueckung = "";
				for (int i = 0; i < iEbene; i++) {
					einrueckung = einrueckung + "   ";
				}
				o[REPORT_PROJEKTBAUM_PROJEKTNUMMER] = einrueckung + o[REPORT_PROJEKTBAUM_PROJEKTNUMMER];
			}

			data = new Object[alDaten.size()][ProjektReportFac.REPORT_PROJEKTBAUM_ANZAHL_SPALTEN];
			data = (Object[][]) alDaten.toArray(data);
		} catch (RemoteException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_IN_ZEITDATEN, new Exception(e));
		}

		initJRDS(parameter, ProjektReportFac.REPORT_MODUL, cAktuellerReport, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	public ArrayList holeNaechsteEbene(ArrayList alDaten, Integer projektIId, int iEbene) {
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		iEbene++;

		session = factory.openSession();
		String queryString = "SELECT pg FROM FLRProjektgruppe pg WHERE pg.projekt_i_id_vater=" + projektIId;

		org.hibernate.Query query = session.createQuery(queryString);
		List<?> resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {
			FLRProjektgruppe o = (FLRProjektgruppe) resultListIterator.next();

			Object[] oZeile = new Object[REPORT_PROJEKTBAUM_ANZAHL_SPALTEN];

			oZeile[REPORT_PROJEKTBAUM_PROJEKTNUMMER] = o.getFlrprojekt_kind().getC_nr();
			oZeile[REPORT_PROJEKTBAUM_TITEL] = o.getFlrprojekt_kind().getC_titel();
			oZeile[REPORT_PROJEKTBAUM_STATUS] = o.getFlrprojekt_kind().getStatus_c_nr();
			oZeile[REPORT_PROJEKTBAUM_EBENE] = new Integer(iEbene);

			oZeile[REPORT_PROJEKTBAUM_KATEGORIE] = o.getFlrprojekt_kind().getKategorie_c_nr();
			oZeile[REPORT_PROJEKTBAUM_KUNDE] = HelperServer
					.formatNameAusFLRPartner(o.getFlrprojekt_kind().getFlrpartner());
			oZeile[REPORT_PROJEKTBAUM_TYP] = o.getFlrprojekt_kind().getTyp_c_nr();
			oZeile[REPORT_PROJEKTBAUM_TERMIN] = o.getFlrprojekt_kind().getT_zielwunschdatum();
			oZeile[REPORT_PROJEKTBAUM_PRIO] = o.getFlrprojekt_kind().getI_prio();

			alDaten.add(oZeile);

			holeNaechsteEbene(alDaten, o.getProjekt_i_id_kind(), iEbene);

		}

		return alDaten;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printProjektEinesArtikels(Integer artikelIId, boolean bNurOffene, DatumsfilterVonBis vonbis,
			TheClientDto theClientDto) {
		cAktuellerReport = REPORT_PROJEKTE_EINES_ARTIKELS;

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session.createCriteria(FLRProjekt.class);
		crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));

		crit.add(Restrictions.eq("artikel_i_id", artikelIId));

		if (bNurOffene) {
			crit.createAlias("flrprojektstatus", "s", CriteriaSpecification.LEFT_JOIN);
			crit.add(Restrictions.eq("s.b_erledigt", (short) 0));
		}

		parameter.put("P_NUROFFENE", new Boolean(bNurOffene));

		if (vonbis != null) {
			parameter.put("P_VON", vonbis.getTimestampVon());
			parameter.put("P_BIS", vonbis.getTimestampBisUnveraendert());

			if (vonbis.getTimestampVon() != null) {
				crit.add(Restrictions.ge("t_anlegen", vonbis.getTimestampVon()));
			}
			if (vonbis.getTimestampBis() != null) {
				crit.add(Restrictions.lt("t_anlegen", vonbis.getTimestampBis()));
			}

		}

		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();

		data = new Object[results.size()][REPORT_PROJEKTE_EINES_ARTIKELS_ANZAHL_SPALTEN];
		int row = 0;
		while (resultListIterator.hasNext()) {
			FLRProjekt flrProjekt = (FLRProjekt) resultListIterator.next();
			data[row][REPORT_PROJEKTE_EINES_ARTIKELS_BELEGNUMMER] = flrProjekt.getC_nr();
			data[row][REPORT_PROJEKTE_EINES_ARTIKELS_PROJEKT_I_ID] = flrProjekt.getI_id();
			data[row][REPORT_PROJEKTE_EINES_ARTIKELS_TITEL] = flrProjekt.getC_titel();

			data[row][REPORT_PROJEKTE_EINES_ARTIKELS_T_ANLEGEN] = flrProjekt.getT_anlegen();

			BereichDto bereichDto = getProjektServiceFac().bereichFindByPrimaryKey(flrProjekt.getBereich_i_id());
			data[row][REPORT_PROJEKTE_EINES_ARTIKELS_BEREICH] = bereichDto.getCBez();

			data[row][ProjektReportFacBean.REPORT_PROJEKTE_EINES_ARTIKELS_PARTNER] = HelperServer
					.formatNameAusFLRPartner(flrProjekt.getFlrpartner());
			data[row][ProjektReportFacBean.REPORT_PROJEKTE_EINES_ARTIKELS_PERSON_ZUGEORDNET] = HelperServer
					.formatPersonAusFLRPErsonal(flrProjekt.getFlrpersonalZugewiesener());

			data[row][ProjektReportFacBean.REPORT_PROJEKTE_EINES_ARTIKELS_TYP] = flrProjekt.getFlrtyp().getC_nr();
			data[row][ProjektReportFacBean.REPORT_PROJEKTE_EINES_ARTIKELS_STATUS] = flrProjekt.getFlrprojektstatus()
					.getStatus_c_nr();
			data[row][ProjektReportFacBean.REPORT_PROJEKTE_EINES_ARTIKELS_KATEGORIE] = flrProjekt.getKategorie_c_nr();

			row++;
		}

		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(artikelIId, theClientDto);
		parameter.put("P_ARTIKEL", artikelDto.formatArtikelbezeichnung());
		parameter.put("P_ARTIKELREFERENZNUMMER", artikelDto.getCReferenznr());
		initJRDS(parameter, REPORT_MODUL, cAktuellerReport, theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	public JasperPrintLP printProjekt(Integer iIdProjektI, Integer iAnzahlKopienI, Boolean bMitLogo,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		JasperPrintLP aJasperPrint = null;

		try {
			ProjektDto projektDto = getProjektFac().projektFindByPrimaryKey(iIdProjektI);
			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(projektDto.getPartnerIId(), theClientDto);
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(projektDto.getMandantCNr(), theClientDto);
			cAktuellerReport = ProjektReportFac.REPORT_PROJEKT;
			// dem Report seine Parameter setzen
			HashMap<String, Object> parameter = new HashMap<String, Object>();

			Locale locDruck = Helper.string2Locale(partnerDto.getLocaleCNrKommunikation());

			if (partnerDto.getPartnerklasseIId() != null) {
				parameter.put("P_PARTNERKLASSE",
						getPartnerFac().partnerklasseFindByPrimaryKey(partnerDto.getPartnerklasseIId(), theClientDto)
								.getBezeichnung());
			}

			if (projektDto.getPartnerIIdBetreiber() != null) {
				parameter.put("P_BETREIBER",
						getPartnerFac().partnerFindByPrimaryKey(projektDto.getPartnerIIdBetreiber(), theClientDto)
								.formatFixName1Name2());
			}

			if (projektDto.getArtikelIId() != null) {
				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(projektDto.getArtikelIId(), theClientDto);
				parameter.put("P_ARTIKEL", aDto.getCNr());
				parameter.put("P_ARTIKELBEZEICHNUNG", aDto.getCBezAusSpr());
			}

			BereichDto bereichDto = getProjektServiceFac().bereichFindByPrimaryKey(projektDto.getBereichIId());

			parameter.put("P_BEREICH", bereichDto.getCBez());
			parameter.put("P_BEREICH_ID", bereichDto.getIId());

			if (partnerDto.getBrancheIId() != null) {
				parameter.put("P_BRANCHE", getPartnerServicesFac()
						.brancheFindByPrimaryKey(partnerDto.getBrancheIId(), theClientDto).getBezeichnung());
			}
			parameter.put("P_MANDANTADRESSE", Helper.formatMandantAdresse(mandantDto));

			parameter.put("P_REALISIERUNG", projektDto.getTRealisierung());

			parameter.put("P_TITEL", projektDto.getCTitel());
			parameter.put("P_PROJEKTNUMMER", projektDto.getCNr());
			parameter.put("P_PROJEKT_ID", projektDto.getIId());

			parameter.put("P_KOPFTEXT", Helper.formatStyledTextForJasper(projektDto.getXFreetext()));
			parameter.put("P_KATEGORIE", projektDto.getKategorieCNr());

			parameter.put("P_WAHRSCHEINLICHKEIT", projektDto.getIWahrscheinlichkeit());
			parameter.put("P_UMSATZGEPLANT", projektDto.getNUmsatzgeplant());

			parameter.put("P_ZIELTERMIN", "" + Helper.formatDatum(projektDto.getTZielwunschdatum(), locDruck));
			parameter.put("P_ERLEDIGUNGSDATUM", "" + Helper.formatDatum(projektDto.getTErledigt(), locDruck));
			parameter.put("P_BELEGDATUM", "" + Helper.formatDatum(projektDto.getTAnlegen(), locDruck));

			// SP4880
			if (projektDto.getProjektIIdNachfolger() != null) {

				ProjektDto pjDto = getProjektFac().projektFindByPrimaryKey(projektDto.getProjektIIdNachfolger());

				parameter.put("P_NACHFOLGEPROJEKT",
						getProjektServiceFac().bereichFindByPrimaryKey(pjDto.getBereichIId()).getCBez() + " "
								+ pjDto.getCNr());

			}

			ArrayList<String> s = getProjektFac().getVorgaengerProjekte(projektDto.getIId());

			StringBuffer str = new StringBuffer("");
			for (int i = 0; i < s.size(); i++) {
				str.append(s.get(i));
				str.append(", ");
			}

			parameter.put("P_VORGAENGERPROJEKTE", str.toString());

			parameter.put("P_KUNDE_ADRESSBLOCK", formatAdresseFuerAusdruck(partnerDto, null, mandantDto, locDruck));

			parameter.put("P_SUBREPORT_VATERPROJEKTE",
					getSubreportVerknuepfteProjekteDerNaechstenEbene(projektDto.getIId(), true, theClientDto));
			parameter.put("P_SUBREPORT_KINDPROJEKTE",
					getSubreportVerknuepfteProjekteDerNaechstenEbene(projektDto.getIId(), false, theClientDto));

			if (projektDto.getAnsprechpartnerIId() != null) {
				AnsprechpartnerDto ansprechpartnerDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(projektDto.getAnsprechpartnerIId(), theClientDto);
				parameter.put(LPReport.P_ANSPRECHPARTNER,
						ansprechpartnerDto.getPartnerDto().formatFixTitelVornameNachnameNTitel());

				parameter.put(LPReport.P_ANSPRECHPARTNERHANDY, ansprechpartnerDto.getCHandy());

				parameter.put(LPReport.P_ANSPRECHPARTNERDW, ansprechpartnerDto.getCTelefon());

				parameter.put(LPReport.P_ANSPRECHPARTNEREMAIL, ansprechpartnerDto.getCEmail());

				parameter.put(LPReport.P_ANSPRECHPARTNERFAX, ansprechpartnerDto.getCDirektfax());

				String sTelefon = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(
						ansprechpartnerDto.getIId(), partnerDto, PartnerFac.KOMMUNIKATIONSART_TELEFON,
						theClientDto.getMandant(), theClientDto);

				parameter.put(LPReport.P_ANSPRECHPARTNERTELEFON, sTelefon != null ? sTelefon : "");

			}
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				// Hiberante Criteria fuer alle Tabellen ausgehend von meiner
				// Haupttabelle anlegen,
				// nach denen ich filtern und sortieren kann
				Criteria crit = session.createCriteria(FLRHistory.class);

				crit.createCriteria(ProjektFac.FLR_HISTORY_FLRPROJEKT)
						.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_I_ID, projektDto.getIId()));
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
						data[i][ProjektReportFac.REPORT_PROJEKT_ERZEUGER] = history.getFlrpersonal().getFlrpartner()
								.getC_name1nachnamefirmazeile1();
						data[i][ProjektReportFac.REPORT_PROJEKT_HISTORY_BELEGDATUM] = Helper
								.formatDatum(history.getT_belegdatum(), locDruck);
						data[i][ProjektReportFac.REPORT_PROJEKT_HISTORY_BELEGDATUM_AS_TIMESTAMP] = history
								.getT_belegdatum();
						data[i][ProjektReportFac.REPORT_PROJEKT_HISTORY_HTML] = Helper
								.short2Boolean(history.getB_html());
						data[i][ProjektReportFac.REPORT_PROJEKT_HISTORY_TEXT] = stripHtmlFontFamilyIfHtml(
								history.getX_text(), history.getB_html());
						data[i][ProjektReportFac.REPORT_PROJEKT_TITEL] = history.getC_titel();

						if (history.getFlrhistoryart() != null) {
							data[i][ProjektReportFac.REPORT_PROJEKT_HISTORYART] = history.getFlrhistoryart().getC_bez();

							data[i][ProjektReportFac.REPORT_PROJEKT_ROT] = history.getFlrhistoryart().getI_rot();
							data[i][ProjektReportFac.REPORT_PROJEKT_BLAU] = history.getFlrhistoryart().getI_blau();
							data[i][ProjektReportFac.REPORT_PROJEKT_GRUEN] = history.getFlrhistoryart().getI_gruen();
						}

						data[i][ProjektReportFac.REPORT_PROJEKT_T_AENDERN] = history.getT_aendern();

						data[i][ProjektReportFac.REPORT_PROJEKT_KURZZEICHEN_PERSON_AENDERN] = history
								.getFlrpersonal_aendern().getC_kurzzeichen();
						data[i][ProjektReportFac.REPORT_PROJEKT_PERSON_AENDERN] = HelperServer
								.formatPersonAusFLRPartner(history.getFlrpersonal_aendern().getFlrpartner());

						if (history.getFlrpersonal_wirddurchgefuehrtvon() != null) {
							data[i][ProjektReportFac.REPORT_PROJEKT_HISTORY_DURCHGEFUEHRT_VON] = history
									.getFlrpersonal_wirddurchgefuehrtvon().getC_kurzzeichen();
						}

						data[i][ProjektReportFac.REPORT_PROJEKT_HISTORY_ERLEDIGUNGSGRAD] = history
								.getF_erledigungsgrad();
						data[i][ProjektReportFac.REPORT_PROJEKT_HISTORY_DAUER_GEPLANT] = history.getN_dauer_geplant();

						i++;
					}
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

			Session sessionTechniker = factory.openSession();
			String sQuery = " SELECT t FROM FLRProjekttechniker t WHERE t.projekt_i_id=" + projektDto.getIId();

			org.hibernate.Query techniker = sessionTechniker.createQuery(sQuery);

			List<?> resultListTechniker = techniker.list();

			Iterator<?> it = resultListTechniker.iterator();

			ArrayList<Object[]> alSub = new ArrayList<Object[]>();
			while (it.hasNext()) {
				FLRProjekttechniker t = (FLRProjekttechniker) it.next();

				Object[] oSub = new Object[1];
				PersonRpt pRpt = getPersonalFac().getPersonRpt(t.getPersonal_i_id(), theClientDto);

				oSub[0] = pRpt;

				alSub.add(oSub);
			}

			String[] fieldnames = new String[] { "F_PERSONAL_OBJ" };

			Object[][] dataSub = new Object[alSub.size()][fieldnames.length];
			dataSub = (Object[][]) alSub.toArray(dataSub);

			parameter.put("P_SUBREPORT_TECHNIKER", new LPDatenSubreport(dataSub, fieldnames));

			sessionTechniker.close();

			// SP2599 Bild anhaengen

			// Bild einfuegen

			parameter.put("P_SUBREPORT_BILDER",
					getSubreportBilder(projektDto.getOAttachments(), projektDto.getCAttachmentsType()));

			if (projektDto.hasContentId()) {
				HvOptional<EditorContentDto> dto = getMediaFac()
						.editorContentFindByPrimaryKey(projektDto.getContentId(), theClientDto);
				parameter.put("P_SUBREPORT_BLOCKCONTENT", new LPBlockContentSubreport(dto.get()));
			}

			initJRDS(parameter, ProjektReportFac.REPORT_MODUL, cAktuellerReport, theClientDto.getMandant(), locDruck,
					theClientDto);

			JasperPrintLP print = getReportPrint();

			return print;

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, new Exception(t));
		}

		return aJasperPrint;
	}

	private LPDatenSubreport getSubreportBilder(byte[] attachments, String attachmentsType) {

		if (attachments != null && attachmentsType != null) {
			ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();

			if (attachmentsType.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
					|| attachmentsType.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)
					|| attachmentsType.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)) {
				byte[] bild = attachments;

				images.add(Helper.byteArrayToImage(bild));

			} else if (attachmentsType.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF)) {

				byte[] bild = attachments;

				java.awt.image.BufferedImage[] tiffs = Helper.tiffToImageArray(bild);
				if (tiffs != null) {
					for (int k = 0; k < tiffs.length; k++) {
						images.add(tiffs[k]);
					}
				}

			} else if (attachmentsType.equals(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF)) {

				byte[] pdf = attachments;

				PDDocument document = null;

				try {

					InputStream myInputStream = new ByteArrayInputStream(pdf);

					document = PDDocument.load(myInputStream);
					PDFRenderer renderer = new PDFRenderer(document);
					int numPages = document.getNumberOfPages();

					for (int p = 0; p < numPages; p++) {

						BufferedImage image = renderer.renderImageWithDPI(p, 150);
						images.add(image);
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

			String[] fieldnamesImages = new String[] { "F_BILD" };
			Object[][] dataSubImages = new Object[images.size()][fieldnamesImages.length];
			for (int i = 0; i < images.size(); i++) {
				dataSubImages[i][0] = images.get(i);
			}

			return new LPDatenSubreport(dataSubImages, fieldnamesImages);
		} else {
			return null;
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printProjektAlleDetailliert(ReportJournalKriterienDto reportJournalKriterienDtoI,
			Date dStichtag, Integer bereichIId, boolean belegdatumStattZieltermin, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		JasperPrintLP oPrint = null;
		int iAnzahlZeilen = 0;
		cAktuellerReport = ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLLIERT;
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
			critProjekt.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_MANDANT_C_NR, theClientDto.getMandant()));

			critProjekt.add(Restrictions.not(
					Restrictions.eq(ProjektFac.FLR_PROJEKT_STATUS_C_NR, ProjektServiceFac.PROJEKT_STATUS_STORNIERT)));

			// Einschraenkung nach Belegdatum von - bis
			String sVon = null;
			String sBis = null;

			if (reportJournalKriterienDtoI.dVon != null) {

				if (belegdatumStattZieltermin) {
					critProjekt.add(Restrictions.ge(ProjektFac.FLR_PROJEKT_T_ANLEGEN, reportJournalKriterienDtoI.dVon));
				} else {
					critProjekt
							.add(Restrictions.ge(ProjektFac.FLR_PROJEKT_T_ZIELDATUM, reportJournalKriterienDtoI.dVon));
				}

				sVon = Helper.formatDatum(reportJournalKriterienDtoI.dVon, theClientDto.getLocUi());
			}

			if (reportJournalKriterienDtoI.dBis != null) {
				if (belegdatumStattZieltermin) {
					critProjekt.add(Restrictions.lt(ProjektFac.FLR_PROJEKT_T_ANLEGEN,
							Helper.addiereTageZuDatum(reportJournalKriterienDtoI.dBis, 1)));
				} else {
					critProjekt.add(Restrictions.lt(ProjektFac.FLR_PROJEKT_T_ZIELDATUM,
							Helper.addiereTageZuDatum(reportJournalKriterienDtoI.dBis, 1)));
				}
				sBis = Helper.formatDatum(reportJournalKriterienDtoI.dBis, theClientDto.getLocUi());
			}

			if (reportJournalKriterienDtoI.sBelegnummerVon != null) {
				sVon = reportJournalKriterienDtoI.sBelegnummerVon;
				critProjekt.add(Restrictions.ge(ProjektFac.FLR_PROJEKT_C_NR, new Integer(sVon)));
			}

			if (bereichIId != null) {
				critProjekt.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_BEREICH_I_ID, bereichIId));
			}

			if (reportJournalKriterienDtoI.sBelegnummerBis != null) {
				sBis = reportJournalKriterienDtoI.sBelegnummerBis;
				critProjekt.add(Restrictions.le(ProjektFac.FLR_PROJEKT_C_NR, new Integer(sBis)));
			}

			// Einschraenkung nach einer bestimmten Perosn
			if (reportJournalKriterienDtoI.personalIId != null) {
				critProjekt.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_PERSONAL_I_ID_ZUGEWIESENER,
						reportJournalKriterienDtoI.personalIId));
			}

			// Sortierung nach Personal ist immer die erste Sortierung
			if (reportJournalKriterienDtoI.bSortiereNachPersonal) {
				critProjekt.createCriteria(ProjektFac.FLR_PROJEKT_FLRPERSONALZUGEWIESENER)
						.createCriteria(KundeFac.FLR_PARTNER)
						.addOrder(Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
			}

			// Sortierung nach Partner,
			if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				critProjekt.createCriteria(ProjektFac.FLR_PROJEKT_FLRPARTNER)
						.addOrder(Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
			}
			// es wird in jedem Fall nach der Iid sortiert
			critProjekt.addOrder(Order.asc(ProjektFac.FLR_PROJEKT_C_NR));

			List<?> list = critProjekt.list();
			Iterator<?> it = list.iterator();

			ArrayList alDaten = new ArrayList();

			it = list.iterator();

			while (it.hasNext()) {
				FLRProjekt projekt = (FLRProjekt) it.next();

				Object[] oZeile = new Object[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_ANZAHL_SPALTEN];

				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_PROJEKTTITEL] = projekt.getC_titel();
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_PROJEKTKATEGORIE] = projekt
						.getKategorie_c_nr();
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_PROJEKTCNR] = projekt.getC_nr();
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_KUNDECNAME1] = projekt.getFlrpartner()
						.getC_name1nachnamefirmazeile1();
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_STATUS] = projekt.getStatus_c_nr();
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_TYP] = projekt.getTyp_c_nr();
				if (projekt.getPersonal_i_id_internerledigt() != null) {
					PersonalDto personalDto = getPersonalFac()
							.personalFindByPrimaryKey(projekt.getPersonal_i_id_internerledigt(), theClientDto);
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_INTERNERLEDIGT_PERSON] = personalDto
							.getPartnerDto().formatAnrede();
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_INTERNERLEDIGT_ZEIT] = projekt
							.getT_internerledigt();
				}

				if (projekt.getPartner_i_id_betreiber() != null) {
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_BETREIBER] = HelperServer
							.formatNameAusFLRPartner(projekt.getFlrpartnerbetreiber());
				}

				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_WAHRSCHEINLICHKEIT] = projekt
						.getI_wahrscheinlichkeit();
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_GEPLANTERUMSATZ] = projekt
						.getN_umsatzgeplant();

				locDruck = Helper.string2Locale(projekt.getFlrpartner().getLocale_c_nr_kommunikation());
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_ZIELTERMIN] = projekt
						.getT_zielwunschdatum();
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_BELEGDATUM] = projekt.getT_anlegen();
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_ERLEDIGUNGSDATUM] = projekt
						.getT_erledigungsdatum();

				Calendar calendar = new GregorianCalendar();
				calendar.setTime(new Date(projekt.getT_zielwunschdatum().getTime()));
				int KW = calendar.get(Calendar.WEEK_OF_YEAR); // Kalendarwochen

				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_ZIELWOCHE] = "" + KW;

				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_PRIO] = projekt.getI_prio();
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_TEXT] = projekt.getX_freetext();
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_ERZEUGER] = HelperServer
						.formatPersonAusFLRPartner(projekt.getFlrpersonalErzeuger().getFlrpartner());
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_ZUGEWIESENER] = HelperServer
						.formatPersonAusFLRPartner(projekt.getFlrpersonalZugewiesener().getFlrpartner());
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_ZEIT] = projekt.getT_zeit();
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_DAUER] = projekt.getD_dauer();
				//
				//

				if (projekt.getFlrvkfortschritt() != null) {
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_VKFORTSCHRITT] = projekt
							.getFlrvkfortschritt().getC_nr();

					Iterator itVK = projekt.getFlrvkfortschritt().getVkfortschrittspr_set().iterator();
					while (itVK.hasNext()) {
						FLRVkfortschrittspr spr = (FLRVkfortschrittspr) itVK.next();
						if (spr.getLocale().getC_nr().equals(theClientDto.getLocUiAsString())) {
							oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_VKFORTSCHRITT_BEZEICHNUNG] = spr
									.getC_bez();
						}

					}
					if (projekt.getFlrvkfortschritt().getFlrleadstatus() != null) {
						oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_VKFORTSCHRITT_LEADSTATUS] = projekt
								.getFlrvkfortschritt().getFlrleadstatus().getC_bez();
					}

				}

				Criteria crit1 = session.createCriteria(FLRHistory.class);
				crit1.createCriteria(ProjektFac.FLR_HISTORY_FLRPROJEKT)
						.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_I_ID, projekt.getI_id()));
				List<?> resultList = crit1.list();
				Iterator<?> itHistory = resultList.iterator();

				// Subreport History

				String[] fieldnames = new String[] { "F_PERSONAL", "F_BELEGDATUM", "F_TEXT", "F_HTML",
						"F_ERLEDIGUNGSGRAD", "F_WIRD_DURCHGEFUEHRT_VON", "F_HISTORYART", "F_TITEL", "F_GEAENDERT_AM",
						"F_DAUER_GEPLANT" };

				ArrayList alSub = new ArrayList();

				while (itHistory.hasNext()) {
					FLRHistory history = (FLRHistory) itHistory.next();

					Object[] oZeileSub = new Object[fieldnames.length];
					oZeileSub[0] = HelperServer.formatPersonAusFLRPartner(history.getFlrpersonal().getFlrpartner());
					oZeileSub[1] = history.getT_belegdatum();
					oZeileSub[2] = stripHtmlFontFamilyIfHtml(history.getX_text(), history.getB_html());
					oZeileSub[3] = Helper.short2Boolean(history.getB_html());
					oZeileSub[4] = history.getF_erledigungsgrad();
					if (history.getFlrpersonal_wirddurchgefuehrtvon() != null) {
						oZeileSub[5] = HelperServer.formatPersonAusFLRPartner(
								history.getFlrpersonal_wirddurchgefuehrtvon().getFlrpartner());
					}

					if (history.getFlrhistoryart() != null) {
						oZeileSub[6] = history.getFlrhistoryart().getC_bez();
					}
					oZeileSub[7] = history.getC_titel();
					oZeileSub[8] = history.getT_aendern();
					oZeileSub[9] = history.getN_dauer_geplant();

					alSub.add(oZeileSub);

				}

				Object[][] dataSub = new Object[alSub.size()][fieldnames.length];
				dataSub = (Object[][]) alSub.toArray(dataSub);

				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_SUBREPORT_HISTORY] = new LPDatenSubreport(
						dataSub, fieldnames);

				// Subreport Zugehoerige Belege

				LinkedHashMap<String, ProjektVerlaufHelperDto> hm = getProjektFac().getProjektVerlauf(projekt.getI_id(),
						theClientDto);
				Iterator<String> itVerlauf = hm.keySet().iterator();

				fieldnames = new String[] { "F_BELEGART", "F_BELEGNUMMER", "F_BELEGDATUM", "F_STATUS", "F_WERT",
						"F_EBENE", "F_BELEG_I_ID" };

				alSub = new ArrayList();

				while (itVerlauf.hasNext()) {
					ProjektVerlaufHelperDto belegDto = hm.get(itVerlauf.next());

					Object[] oZeileSub = new Object[fieldnames.length];

					String belegart = "Unbekannt";
					String belegnummer = "Unbekannt";
					Integer belegIId = null;
					Integer iEbene = belegDto.getiEbene();
					BigDecimal wert = null;

					String status = null;

					java.util.Date belegdatum = null;

					if (belegDto.getBelegDto() instanceof AngebotDto) {
						AngebotDto dto = (AngebotDto) belegDto.getBelegDto();
						belegart = LocaleFac.BELEGART_ANGEBOT;
						belegnummer = dto.getCNr();
						belegIId = dto.getIId();

						wert = dto.getNGesamtwertinbelegwaehrung();

						belegdatum = dto.getTBelegdatum();

						status = dto.getStatusCNr();

					} else if (belegDto.getBelegDto() instanceof AuftragDto) {
						AuftragDto dto = (AuftragDto) belegDto.getBelegDto();
						belegart = LocaleFac.BELEGART_AUFTRAG;
						belegnummer = dto.getCNr();
						belegIId = dto.getIId();

						wert = dto.getNGesamtauftragswertInAuftragswaehrung();

						belegdatum = dto.getTBelegdatum();

						status = dto.getStatusCNr();

					} else if (belegDto.getBelegDto() instanceof LieferscheinDto) {
						LieferscheinDto dto = (LieferscheinDto) belegDto.getBelegDto();
						belegart = LocaleFac.BELEGART_LIEFERSCHEIN;
						belegnummer = dto.getCNr();
						wert = dto.getNGesamtwertInLieferscheinwaehrung();

						belegdatum = dto.getTBelegdatum();

						status = dto.getStatusCNr();
						belegIId = dto.getIId();

					} else if (belegDto.getBelegDto() instanceof RechnungDto) {
						RechnungDto dto = (RechnungDto) belegDto.getBelegDto();
						belegart = dto.getRechnungartCNr();
						belegnummer = dto.getCNr();

						wert = dto.getNGesamtwertinbelegwaehrung();

						boolean bGutschrift = false;

						RechnungartDto raDto = getRechnungServiceFac()
								.rechnungartFindByPrimaryKey(dto.getRechnungartCNr(), theClientDto);

						if (raDto.getRechnungtypCNr().equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
							bGutschrift = true;
						}

						belegdatum = dto.getTBelegdatum();

						status = dto.getStatusCNr();
						belegIId = dto.getIId();

					} else if (belegDto.getBelegDto() instanceof BestellungDto) {
						BestellungDto dto = (BestellungDto) belegDto.getBelegDto();
						belegart = LocaleFac.BELEGART_BESTELLUNG;
						belegnummer = dto.getCNr();

						status = dto.getStatusCNr();
						belegdatum = dto.getDBelegdatum();

						wert = dto.getNBestellwert();

						belegIId = dto.getIId();

					} else if (belegDto.getBelegDto() instanceof AnfrageDto) {
						AnfrageDto dto = (AnfrageDto) belegDto.getBelegDto();
						belegart = LocaleFac.BELEGART_ANFRAGE;
						belegnummer = dto.getCNr();

						wert = dto.getNGesamtwertinbelegwaehrung();

						belegdatum = dto.getTBelegdatum();

						status = dto.getStatusCNr();

						belegIId = dto.getIId();

					} else if (belegDto.getBelegDto() instanceof LosDto) {
						LosDto dto = (LosDto) belegDto.getBelegDto();
						belegart = LocaleFac.BELEGART_LOS;
						belegnummer = dto.getCNr();
						belegIId = dto.getIId();
						belegdatum = dto.getTProduktionsbeginn();
						status = dto.getStatusCNr();

						wert = getLagerFac().getEinstandsWertEinesBeleges(LocaleFac.BELEGART_LOS, dto.getIId(),
								ArtikelFac.ARTIKELART_ARTIKEL, theClientDto);

					} else if (belegDto.getBelegDto() instanceof ReiseDto) {
						ReiseDto reiseDto = (ReiseDto) belegDto.getBelegDto();

						belegIId = reiseDto.getIId();

						belegart = "Reise";

						wert = reiseDto.getNKostenDesAbschnitts();

					} else if (belegDto.getBelegDto() instanceof EingangsrechnungAuftragszuordnungDto) {
						EingangsrechnungAuftragszuordnungDto eaDto = (EingangsrechnungAuftragszuordnungDto) belegDto
								.getBelegDto();

						EingangsrechnungDto erDto = getEingangsrechnungFac()
								.eingangsrechnungFindByPrimaryKey(eaDto.getEingangsrechnungIId());

						belegart = LocaleFac.BELEGART_EINGANGSRECHNUNG;
						belegnummer = erDto.getCNr();
						status = erDto.getStatusCNr();
						wert = getLocaleFac().rechneUmInMandantenWaehrung(eaDto.getNBetrag(), erDto.getNKurs());

					} else if (belegDto.getBelegDto() instanceof AgstklDto) {
						AgstklDto agstklDto = (AgstklDto) belegDto.getBelegDto();

						belegart = LocaleFac.BELEGART_AGSTUECKLISTE;
						belegnummer = agstklDto.getCNr();
						belegdatum = agstklDto.getTBelegdatum();

						belegIId = agstklDto.getIId();

					} else if (belegDto.getBelegDto() instanceof ReklamationDto) {
						ReklamationDto reklamationDto = (ReklamationDto) belegDto.getBelegDto();

						belegart = LocaleFac.BELEGART_REKLAMATION;
						belegnummer = reklamationDto.getCNr();

						belegdatum = reklamationDto.getTBelegdatum();

						belegIId = reklamationDto.getIId();
						status = reklamationDto.getStatusCNr();

					} else if (belegDto.getBelegDto() instanceof TelefonzeitenDto) {
						TelefonzeitenDto telefonzeitenDto = (TelefonzeitenDto) belegDto.getBelegDto();
						belegart = "Telefon";

						// Die Kosten kommen aus dem Stundensatz
						Calendar c = Calendar.getInstance();
						c.setTimeInMillis(telefonzeitenDto.getTVon().getTime());

						PersonalgehaltDto pgDto = getPersonalFac().personalgehaltFindLetztePersonalgehalt(
								telefonzeitenDto.getPersonalIId(), c.get(Calendar.YEAR), c.get(Calendar.MONTH));

						if (telefonzeitenDto.getTBis() != null) {
							Double dauer = new Double(((double) (telefonzeitenDto.getTBis().getTime()
									- telefonzeitenDto.getTVon().getTime()) / 3600000));
							if (pgDto != null && pgDto.getNStundensatz() != null) {
								wert = pgDto.getNStundensatz().multiply(new BigDecimal(dauer));

							}
						}

					}

					oZeileSub[0] = belegart;
					oZeileSub[1] = belegnummer;
					oZeileSub[2] = belegdatum;

					oZeileSub[3] = status;

					oZeileSub[4] = wert;
					oZeileSub[5] = iEbene;
					oZeileSub[6] = belegIId;

					alSub.add(oZeileSub);
				}

				dataSub = new Object[alSub.size()][fieldnames.length];
				dataSub = (Object[][]) alSub.toArray(dataSub);

				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_SUBREPORT_ZUGEHOERIGE_BELEGE] = new LPDatenSubreport(
						dataSub, fieldnames);

				alDaten.add(oZeile);

			}

			Object[][] returnArray = new Object[alDaten
					.size()][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_ANZAHL_SPALTEN];
			data = (Object[][]) alDaten.toArray(returnArray);

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
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		parameter.put(LPReport.P_SORTIERUNG, buildSortierungProjektAlle(reportJournalKriterienDtoI, theClientDto));

		if (bereichIId != null) {
			parameter.put("P_BEREICH", getProjektServiceFac().bereichFindByPrimaryKey(bereichIId).getCBez());
		}
		parameter.put("P_BELEGDATUM_STATT_ZIELTERMIN", new Boolean(belegdatumStattZieltermin));

		parameter.put(LPReport.P_FILTER, buildFilterProjektAlle(reportJournalKriterienDtoI, theClientDto));

		parameter.put(LPReport.P_SORTIERENACHPERSONAL, new Boolean(reportJournalKriterienDtoI.bSortiereNachPersonal));

		initJRDS(parameter, ProjektReportFac.REPORT_MODUL, cAktuellerReport, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		oPrint = getReportPrint();

		return oPrint;

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printProjektAlle(ReportJournalKriterienDto reportJournalKriterienDtoI, Date dStichtag,
			Integer bereichIId, boolean belegdatumStattZieltermin, boolean bMitIstzeitdaten, TheClientDto theClientDto)
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
			critProjekt.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_MANDANT_C_NR, theClientDto.getMandant()));

			critProjekt.add(Restrictions.not(
					Restrictions.eq(ProjektFac.FLR_PROJEKT_STATUS_C_NR, ProjektServiceFac.PROJEKT_STATUS_STORNIERT)));

			// Einschraenkung nach Belegdatum von - bis
			String sVon = null;
			String sBis = null;

			if (reportJournalKriterienDtoI.dVon != null) {

				if (belegdatumStattZieltermin) {
					critProjekt.add(Restrictions.ge(ProjektFac.FLR_PROJEKT_T_ANLEGEN, reportJournalKriterienDtoI.dVon));
				} else {
					critProjekt
							.add(Restrictions.ge(ProjektFac.FLR_PROJEKT_T_ZIELDATUM, reportJournalKriterienDtoI.dVon));
				}

				sVon = Helper.formatDatum(reportJournalKriterienDtoI.dVon, theClientDto.getLocUi());
			}

			if (reportJournalKriterienDtoI.dBis != null) {
				if (belegdatumStattZieltermin) {
					critProjekt.add(Restrictions.le(ProjektFac.FLR_PROJEKT_T_ANLEGEN, reportJournalKriterienDtoI.dBis));
				} else {
					critProjekt
							.add(Restrictions.le(ProjektFac.FLR_PROJEKT_T_ZIELDATUM, reportJournalKriterienDtoI.dBis));
				}
				sBis = Helper.formatDatum(reportJournalKriterienDtoI.dBis, theClientDto.getLocUi());
			}

			if (reportJournalKriterienDtoI.sBelegnummerVon != null) {
				sVon = reportJournalKriterienDtoI.sBelegnummerVon;
				critProjekt.add(Restrictions.ge(ProjektFac.FLR_PROJEKT_C_NR, new Integer(sVon)));
			}

			if (bereichIId != null) {
				critProjekt.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_BEREICH_I_ID, bereichIId));
			}

			if (reportJournalKriterienDtoI.sBelegnummerBis != null) {
				sBis = reportJournalKriterienDtoI.sBelegnummerBis;
				critProjekt.add(Restrictions.le(ProjektFac.FLR_PROJEKT_C_NR, new Integer(sBis)));
			}

			// Einschraenkung nach einer bestimmten Perosn
			if (reportJournalKriterienDtoI.personalIId != null) {
				critProjekt.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_PERSONAL_I_ID_ZUGEWIESENER,
						reportJournalKriterienDtoI.personalIId));
			}

			// Sortierung nach Personal ist immer die erste Sortierung
			if (reportJournalKriterienDtoI.bSortiereNachPersonal) {
				critProjekt.createCriteria(ProjektFac.FLR_PROJEKT_FLRPERSONALZUGEWIESENER)
						.createCriteria(KundeFac.FLR_PARTNER)
						.addOrder(Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
			}

			// Sortierung nach Partner,
			if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				critProjekt.createCriteria(ProjektFac.FLR_PROJEKT_FLRPARTNER)
						.addOrder(Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
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
						.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_I_ID, projekt.getI_id()));
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
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_PROJEKTTITEL] = projekt.getC_titel();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_PROJEKTKATEGORIE] = projekt.getKategorie_c_nr();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_PROJEKTCNR] = projekt.getC_nr();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_KUNDECNAME1] = projekt.getFlrpartner()
						.getC_name1nachnamefirmazeile1();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_STATUS] = projekt.getStatus_c_nr();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_TYP] = projekt.getTyp_c_nr();
				if (projekt.getPersonal_i_id_internerledigt() != null) {
					PersonalDto personalDto = getPersonalFac()
							.personalFindByPrimaryKey(projekt.getPersonal_i_id_internerledigt(), theClientDto);
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_INTERNERLEDIGT_PERSON] = personalDto
							.getPartnerDto().formatAnrede();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_INTERNERLEDIGT_ZEIT] = projekt
							.getT_internerledigt();
				}

				if (projekt.getPartner_i_id_betreiber() != null) {
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_BETREIBER] = HelperServer
							.formatNameAusFLRPartner(projekt.getFlrpartnerbetreiber());
				}

				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_WAHRSCHEINLICHKEIT] = projekt
						.getI_wahrscheinlichkeit();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_GEPLANTERUMSATZ] = projekt.getN_umsatzgeplant();

				locDruck = Helper.string2Locale(projekt.getFlrpartner().getLocale_c_nr_kommunikation());
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_ZIELTERMIN] = projekt.getT_zielwunschdatum();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_BELEGDATUM] = projekt.getT_anlegen();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_ERLEDIGUNGSDATUM] = projekt
						.getT_erledigungsdatum();

				Calendar calendar = new GregorianCalendar();
				calendar.setTime(new Date(projekt.getT_zielwunschdatum().getTime()));
				int KW = calendar.get(Calendar.WEEK_OF_YEAR); // Kalendarwochen

				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_ZIELWOCHE] = "" + KW;

				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_PRIO] = projekt.getI_prio();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_TEXT] = projekt.getX_freetext();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_ERZEUGER] = HelperServer
						.formatPersonAusFLRPartner(projekt.getFlrpersonalErzeuger().getFlrpartner());
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_ZUGEWIESENER] = HelperServer
						.formatPersonAusFLRPartner(projekt.getFlrpersonalZugewiesener().getFlrpartner());
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_ZEIT] = projekt.getT_zeit();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DAUER] = projekt.getD_dauer();

				// PJ22284

				if (bMitIstzeitdaten) {
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_ISTZEIT] = getZeiterfassungFac()
							.getSummeZeitenEinesBeleges(LocaleFac.BELEGART_PROJEKT, projekt.getI_id(), null, null, null,
									null, theClientDto);
				}

				if (projekt.getFlrvkfortschritt() != null) {
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_VKFORTSCHRITT] = projekt.getFlrvkfortschritt()
							.getC_nr();

					Iterator itVK = projekt.getFlrvkfortschritt().getVkfortschrittspr_set().iterator();
					while (itVK.hasNext()) {
						FLRVkfortschrittspr spr = (FLRVkfortschrittspr) itVK.next();
						if (spr.getLocale().getC_nr().equals(theClientDto.getLocUiAsString())) {
							data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_VKFORTSCHRITT_BEZEICHNUNG] = spr
									.getC_bez();
						}

					}
					if (projekt.getFlrvkfortschritt().getFlrleadstatus() != null) {
						data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_VKFORTSCHRITT_LEADSTATUS] = projekt
								.getFlrvkfortschritt().getFlrleadstatus().getC_bez();
					}

				}

				Criteria crit1 = session.createCriteria(FLRHistory.class);
				crit1.createCriteria(ProjektFac.FLR_HISTORY_FLRPROJEKT)
						.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_I_ID, projekt.getI_id()));
				List<?> resultList = crit1.list();
				Iterator<?> itHistory = resultList.iterator();
				i++;

				while (itHistory.hasNext()) {
					FLRHistory history = (FLRHistory) itHistory.next();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_PROJEKTCNR] = projekt.getC_nr();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_ZUGEWIESENER] = HelperServer
							.formatPersonAusFLRPartner(projekt.getFlrpersonalZugewiesener().getFlrpartner());
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_HISTORY_MITARBEITER] = HelperServer
							.formatPersonAusFLRPartner(history.getFlrpersonal().getFlrpartner());
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_HISTORY_BELEGDATUM] = history
							.getT_belegdatum();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_HISTORY_TEXT] = stripHtmlFontFamilyIfHtml(
							history.getX_text(), history.getB_html());
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_HISTORY_HTML] = Helper
							.short2Boolean(history.getB_html());
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_ERLEDIGUNGSGRAD] = history
							.getF_erledigungsgrad();
					if (history.getFlrpersonal_wirddurchgefuehrtvon() != null) {
						data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_WIRD_DURCHGEFUEHRT_VON] = HelperServer
								.formatPersonAusFLRPartner(
										history.getFlrpersonal_wirddurchgefuehrtvon().getFlrpartner());
					}

					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_HISTORY_DAUER_GEPLANT] = history
							.getN_dauer_geplant();

					i++;
				}

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
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		parameter.put(LPReport.P_SORTIERUNG, buildSortierungProjektAlle(reportJournalKriterienDtoI, theClientDto));

		if (bereichIId != null) {
			parameter.put("P_BEREICH", getProjektServiceFac().bereichFindByPrimaryKey(bereichIId).getCBez());
		}
		parameter.put("P_BELEGDATUM_STATT_ZIELTERMIN", new Boolean(belegdatumStattZieltermin));

		parameter.put("P_MIT_ISTZEITDATEN", new Boolean(bMitIstzeitdaten));

		parameter.put(LPReport.P_FILTER, buildFilterProjektAlle(reportJournalKriterienDtoI, theClientDto));

		parameter.put(LPReport.P_SORTIERENACHPERSONAL, reportJournalKriterienDtoI.bSortiereNachPersonal);

		parameter.put("P_TITLE",
				getTextRespectUISpr("proj.print.alle", theClientDto.getMandant(), theClientDto.getLocUi()));

		initJRDS(parameter, ProjektReportFac.REPORT_MODUL, cAktuellerReport, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		oPrint = getReportPrint();

		return oPrint;
	}

	public JasperPrintLP printProjektErledigt(ReportJournalKriterienDto reportJournalKriterienDtoI, Date dStichtag,
			Integer bereichIId, boolean interneErledigungBeruecksichtigen, TheClientDto theClientDto)
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
			crit.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_MANDANT_C_NR, theClientDto.getMandant()));
			crit.add(Restrictions.not(
					Restrictions.eq(ProjektFac.FLR_PROJEKT_STATUS_C_NR, ProjektServiceFac.PROJEKT_STATUS_STORNIERT)));
			if (bereichIId != null) {
				crit.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_BEREICH_I_ID, bereichIId));
			}
			// Einschraenkung nach Status Offen, Erledigt

			if (interneErledigungBeruecksichtigen == false) {
				// PJ18471
				crit.createAlias("flrprojektstatus", "s");
				crit.add(Restrictions.eq("s.b_erledigt", Helper.boolean2Short(true)));
			}

			if (reportJournalKriterienDtoI.dVon != null) {

				if (interneErledigungBeruecksichtigen == true) {

					crit.add(Restrictions.or(
							Restrictions.and(Restrictions.isNotNull(ProjektFac.FLR_PROJEKT_T_INTERNERLEDIGT),
									Restrictions.ge(ProjektFac.FLR_PROJEKT_T_INTERNERLEDIGT,
											reportJournalKriterienDtoI.dVon)),
							Restrictions.ge(ProjektFac.FLR_PROJEKT_T_ERLEDIGUNGSDATUM,
									reportJournalKriterienDtoI.dVon)));

				} else {
					crit.add(Restrictions.ge(ProjektFac.FLR_PROJEKT_T_ERLEDIGUNGSDATUM,
							reportJournalKriterienDtoI.dVon));
				}
			}

			if (reportJournalKriterienDtoI.dBis != null) {

				Date d = Helper.addiereTageZuDatum(reportJournalKriterienDtoI.dBis, 1);

				if (interneErledigungBeruecksichtigen == true) {
					crit.add(Restrictions.or(
							Restrictions.and(Restrictions.isNotNull(ProjektFac.FLR_PROJEKT_T_INTERNERLEDIGT),
									Restrictions.lt(ProjektFac.FLR_PROJEKT_T_INTERNERLEDIGT, d)),
							Restrictions.lt(ProjektFac.FLR_PROJEKT_T_ERLEDIGUNGSDATUM, d)));
				} else {
					crit.add(Restrictions.lt(ProjektFac.FLR_PROJEKT_T_ERLEDIGUNGSDATUM, d));
				}

			}
			// Einschraenkung nach einer bestimmten Perosn
			if (reportJournalKriterienDtoI.personalIId != null) {
				crit.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_PERSONAL_I_ID_ERLEDIGER,
						reportJournalKriterienDtoI.personalIId));
			}
			// Sortierung nach Partner,
			if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				crit.createCriteria(ProjektFac.FLR_PROJEKT_FLRPARTNER)
						.addOrder(Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
				crit.addOrder(Order.asc(ProjektFac.FLR_PROJEKT_T_ERLEDIGUNGSDATUM));
				crit.addOrder(Order.asc(ProjektFac.FLR_PROJEKT_C_NR));

			}

			crit.addOrder(Order.asc(ProjektFac.FLR_PROJEKT_KATEGORIE_C_NR));
			List<?> list = crit.list();
			ArrayList<Object[]> alDaten = new ArrayList<Object[]>();
			Iterator<?> it = list.iterator();

			while (it.hasNext()) {
				FLRProjekt projekt = (FLRProjekt) it.next();
				Object[] oZeile = new Object[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ANZAHL_SPALTEN];

				if (interneErledigungBeruecksichtigen == true && projekt.getT_internerledigt() != null
						&& projekt.getT_erledigungsdatum() != null) {

					// Wenn intern-Erledigt und normal erledigt, dann gilt jenes
					// Datum, das frueher war

					if (projekt.getT_internerledigt().getTime() <= projekt.getT_erledigungsdatum().getTime()) {
						if (reportJournalKriterienDtoI.dVon != null && projekt.getT_internerledigt()
								.getTime() < reportJournalKriterienDtoI.dVon.getTime()) {
							continue;
						}

						if (reportJournalKriterienDtoI.dBis != null && projekt.getT_internerledigt()
								.getTime() > reportJournalKriterienDtoI.dBis.getTime()) {
							continue;
						}

					}

				}

				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_PROJEKTTITEL] = projekt.getC_titel();
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_PROJEKTKATEGORIE] = projekt.getKategorie_c_nr();
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_PROJEKTCNR] = projekt.getC_nr();
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_TYP] = projekt.getTyp_c_nr();
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_KUNDECNAME1] = projekt.getFlrpartner()
						.getC_name1nachnamefirmazeile1();

				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_WAHRSCHEINLICHKEIT] = projekt
						.getI_wahrscheinlichkeit();
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_GEPLANTERUMSATZ] = projekt.getN_umsatzgeplant();
				if (projekt.getPersonal_i_id_internerledigt() != null) {
					PersonalDto personalDto = getPersonalFac()
							.personalFindByPrimaryKey(projekt.getPersonal_i_id_internerledigt(), theClientDto);
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_INTERNERLEDIGT_PERSON] = personalDto
							.getPartnerDto().formatAnrede();
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_INTERNERLEDIGT_ZEIT] = projekt
							.getT_internerledigt();
				}

				if (projekt.getPartner_i_id_betreiber() != null) {
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_BETREIBER] = HelperServer
							.formatNameAusFLRPartner(projekt.getFlrpartnerbetreiber());
				}

				locDruck = Helper.string2Locale(projekt.getFlrpartner().getLocale_c_nr_kommunikation());
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ZIELTERMIN] = projekt.getT_zielwunschdatum();
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_BELEGDATUM] = projekt.getT_anlegen();
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ERLEDIGUNGSDATUM] = projekt
						.getT_erledigungsdatum();

				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_B_VERRECHENBAR] = getProjektServiceFac()
						.getTextVerrechenbar(projekt.getI_verrechenbar(), theClientDto);

				if (projekt.getB_freigegeben().equals(new Integer(1).shortValue())) {
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_FREIGEGEBEN] = "freigegeben";
				} else {
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_FREIGEGEBEN] = null;
				}

				Calendar calendar = new GregorianCalendar();
				calendar.setTime(new Date(projekt.getT_zielwunschdatum().getTime()));
				int KW = calendar.get(Calendar.WEEK_OF_YEAR); // Kalendarwochen

				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ZIELWOCHE] = "" + KW;
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_STATUS] = projekt.getStatus_c_nr();
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_PRIO] = projekt.getI_prio();
				if (projekt.getFlrprojekterledigungsgrund() != null) {
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ERLEDIGUNGSGRUND] = projekt
							.getFlrprojekterledigungsgrund().getC_bez();
				}
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_TEXT] = Helper
						.formatStyledTextForJasper(projekt.getX_freetext());
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ERZEUGER] = HelperServer
						.formatPersonAusFLRPartner(projekt.getFlrpersonalErzeuger().getFlrpartner());
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ZUGEWIESENER] = HelperServer
						.formatPersonAusFLRPartner(projekt.getFlrpersonalZugewiesener().getFlrpartner());
				if (projekt.getPersonal_i_id_erlediger() != null) {
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ERLEDIGER] = HelperServer
							.formatPersonAusFLRPartner(projekt.getFlrpersonalErlediger().getFlrpartner());
				}
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ZEIT] = projekt.getT_zeit();
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_DAUER] = projekt.getD_dauer();

				if (projekt.getFlrvkfortschritt() != null) {
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_VKFORTSCHRITT] = projekt
							.getFlrvkfortschritt().getC_nr();

					Iterator itVK = projekt.getFlrvkfortschritt().getVkfortschrittspr_set().iterator();
					while (itVK.hasNext()) {
						FLRVkfortschrittspr spr = (FLRVkfortschrittspr) itVK.next();
						if (spr.getLocale().getC_nr().equals(theClientDto.getLocUiAsString())) {
							oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_VKFORTSCHRITT_BEZEICHNUNG] = spr
									.getC_bez();
						}

					}

					if (projekt.getFlrvkfortschritt().getFlrleadstatus() != null) {
						oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_VKFORTSCHRITT_LEADSTATUS] = projekt
								.getFlrvkfortschritt().getFlrleadstatus().getC_bez();
					}

				}

				// Gesamte Dauer eines Projektes
				Double ddArbeitszeitist = getZeiterfassungFac().getSummeZeitenEinesBeleges(LocaleFac.BELEGART_PROJEKT,
						projekt.getI_id(), null, null, null, null, theClientDto);
				oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_GESAMTDAUER] = ddArbeitszeitist;

				Criteria crit1 = session.createCriteria(FLRHistory.class);
				crit1.createCriteria(ProjektFac.FLR_HISTORY_FLRPROJEKT)
						.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_I_ID, projekt.getI_id()));
				List<?> resultList = crit1.list();
				Iterator<?> itHistory = resultList.iterator();
				alDaten.add(oZeile);
				while (itHistory.hasNext()) {
					FLRHistory history = (FLRHistory) itHistory.next();

					oZeile = new Object[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ANZAHL_SPALTEN];

					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_PROJEKTCNR] = projekt.getC_nr();
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ZUGEWIESENER] = HelperServer
							.formatPersonAusFLRPartner(projekt.getFlrpersonalZugewiesener().getFlrpartner());
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_HISTORY_MITARBEITER] = HelperServer
							.formatPersonAusFLRPartner(history.getFlrpersonal().getFlrpartner());
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_HISTORY_BELEGDATUM] = history
							.getT_belegdatum();
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_HISTORY_TEXT] = stripHtmlFontFamilyIfHtml(
							history.getX_text(), history.getB_html());
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_HISTORY_HTML] = Helper
							.short2Boolean(history.getB_html());

					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_HISTORY_ERLEDIGUNGSGRAD] = history
							.getF_erledigungsgrad();
					if (history.getFlrpersonal_wirddurchgefuehrtvon() != null) {
						oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_HISTORY_WIRD_DURCHGEFUEHRT_VON] = HelperServer
								.formatPersonAusFLRPartner(
										history.getFlrpersonal_wirddurchgefuehrtvon().getFlrpartner());
					}

					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_HISTORY_DAUER_GEPLANT] = history
							.getN_dauer_geplant();

					alDaten.add(oZeile);

				}
			}
			Object[][] returnArray = new Object[alDaten
					.size()][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ANZAHL_SPALTEN];
			data = (Object[][]) alDaten.toArray(returnArray);

		} catch (RemoteException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_IN_ZEITDATEN, new Exception(e));
		} finally {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE, he);
			}
		}

		// die Parameter dem Report uebergeben
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put(LPReport.P_FILTER, buildFilterProjektErledigt(reportJournalKriterienDtoI, theClientDto));
		if (bereichIId != null) {
			parameter.put("P_BEREICH", getProjektServiceFac().bereichFindByPrimaryKey(bereichIId).getCBez());
		}
		parameter.put("P_INTERNEERLEDIGUNGBERUECKSICHTIGEN", interneErledigungBeruecksichtigen);
		parameter.put("P_TITLE",
				getTextRespectUISpr("proj.print.erledigt", theClientDto.getMandant(), theClientDto.getLocUi()));
		initJRDS(parameter, ProjektReportFac.REPORT_MODUL, cAktuellerReport, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);
		oPrint = getReportPrint();
		return oPrint;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printProjektOffene(ReportJournalKriterienDto reportJournalKriterienDtoI, Date dStichtag,
			Integer bereichIId, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		JasperPrintLP oPrint = null;
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
			critProjekt.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_MANDANT_C_NR, theClientDto.getMandant()));
			critProjekt.add(Restrictions.not(
					Restrictions.eq(ProjektFac.FLR_PROJEKT_STATUS_C_NR, ProjektServiceFac.PROJEKT_STATUS_STORNIERT)));

			if (bereichIId != null) {
				critProjekt.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_BEREICH_I_ID, bereichIId));
			}

			// PJ18471
			critProjekt.createAlias("flrprojektstatus", "s");
			critProjekt.add(Restrictions.eq("s.b_erledigt", Helper.boolean2Short(false)));

			// Das Belegdatum muss vor dem Stichtag liegen
			critProjekt.add(Restrictions.le(ProjektFac.FLR_PROJEKT_T_ANLEGEN, dStichtag));

			// Einschraenkung nach einer bestimmten Perosn
			if (reportJournalKriterienDtoI.personalIId != null) {
				critProjekt.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_PERSONAL_I_ID_ZUGEWIESENER,
						reportJournalKriterienDtoI.personalIId));
			}

			// Sortierung nach Personal ist immer die erste Sortierung
			if (reportJournalKriterienDtoI.bSortiereNachPersonal) {
				critProjekt.createCriteria(ProjektFac.FLR_PROJEKT_FLRPERSONALZUGEWIESENER)
						.createCriteria(KundeFac.FLR_PARTNER)
						.addOrder(Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
			}

			// Sortierung nach Partner,
			if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				critProjekt.createCriteria(ProjektFac.FLR_PROJEKT_FLRPARTNER)
						.addOrder(Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
				critProjekt.addOrder(Order.asc(ProjektFac.FLR_PROJEKT_TYP_C_NR));
				critProjekt.addOrder(Order.asc(ProjektFac.FLR_PROJEKT_I_PRIO));
				critProjekt.addOrder(Order.asc(ProjektFac.FLR_PROJEKT_KATEGORIE_C_NR));
			}
			// es wird in jedem Fall nach der Belegnummer sortiert
			critProjekt.addOrder(Order.asc(ProjektFac.FLR_PROJEKT_C_NR));

			List<?> list = critProjekt.list();

			HashMap<?, ?> hmStatusbilder = getLocaleFac().getAllStatiIcon();

			ArrayList<Object[]> alDaten = new ArrayList<Object[]>();

			Iterator<?> it = list.iterator();
			while (it.hasNext()) {
				FLRProjekt projekt = (FLRProjekt) it.next();

				Object[] oZeileVorlage = new Object[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ANZAHL_SPALTEN];

				oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PROJEKTTITEL] = projekt.getC_titel();
				oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PROJEKTKATEGORIE] = projekt
						.getKategorie_c_nr();
				oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY] = Boolean.FALSE;
				oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_REALISIERUNGSTERMIN] = projekt
						.getT_realisierung();
				oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PROJEKTCNR] = projekt.getC_nr();
				oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_STATUS] = projekt.getStatus_c_nr();

				oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_STATUS_BILD] = Helper
						.byteArrayToImage((byte[]) hmStatusbilder.get(projekt.getStatus_c_nr()));

				if (projekt.getPartner_i_id_betreiber() != null) {
					oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_BETREIBER] = HelperServer
							.formatNameAusFLRPartner(projekt.getFlrpartnerbetreiber());
				}

				oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_TYP] = projekt.getTyp_c_nr();

				if (projekt.getPersonal_i_id_internerledigt() != null) {
					PersonalDto personalDto = getPersonalFac()
							.personalFindByPrimaryKey(projekt.getPersonal_i_id_internerledigt(), theClientDto);
					oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_INTERNERLEDIGT_PERSON] = personalDto
							.getPartnerDto().formatAnrede();
					oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_INTERNERLEDIGT_ZEIT] = projekt
							.getT_internerledigt();
				}
				oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_WAHRSCHEINLICHKEIT] = projekt
						.getI_wahrscheinlichkeit();
				oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_GEPLANTERUMSATZ] = projekt
						.getN_umsatzgeplant();
				// Gesamte Dauer eines Projektes
				Double ddArbeitszeitist = getZeiterfassungFac().getSummeZeitenEinesBeleges(LocaleFac.BELEGART_PROJEKT,
						projekt.getI_id(), null, null, null, null, theClientDto);
				oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_GESAMTDAUER] = ddArbeitszeitist;

				if (projekt.getFlrpartner().getFlrlandplzort() != null) {
					oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_LKZ] = projekt.getFlrpartner()
							.getFlrlandplzort().getFlrland().getC_lkz();
					oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PLZ] = projekt.getFlrpartner()
							.getFlrlandplzort().getC_plz();
					oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ORT] = projekt.getFlrpartner()
							.getFlrlandplzort().getFlrort().getC_name();
				}

				if (projekt.getFlrpartner().getFlrpartnerklasse() != null) {
					oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PARTNERKLASSE] = projekt
							.getFlrpartner().getFlrpartnerklasse().getC_nr();
				}

				if (projekt.getFlrpartner().getFlrbranche() != null) {
					oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_BRANCHE] = projekt.getFlrpartner()
							.getFlrbranche().getC_nr();
				}

				oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_KUNDECNAME1] = projekt.getFlrpartner()
						.getC_name1nachnamefirmazeile1();
				// locDruck = Helper.string2Locale(projekt.getFlrpartner()
				// .getLocale_c_nr_kommunikation());
				// SP6037
				locDruck = theClientDto.getLocUi();

				oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ZIELTERMIN] = projekt
						.getT_zielwunschdatum();
				oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_BELEGDATUM] = projekt.getT_anlegen();

				Calendar calendar = new GregorianCalendar();
				calendar.setTime(new Date(projekt.getT_zielwunschdatum().getTime()));
				int KW = calendar.get(Calendar.WEEK_OF_YEAR); // Kalendarwochen

				oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ZIELWOCHE] = "" + KW;

				oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PRIO] = projekt.getI_prio();
				oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_I_SORT_QUEUE] = projekt.getI_sort();

				oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_SUBREPORT_VATERPROJEKTE] = getSubreportVerknuepfteProjekteDerNaechstenEbene(
						projekt.getI_id(), true, theClientDto);
				oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_SUBREPORT_KINDPROJEKTE] = getSubreportVerknuepfteProjekteDerNaechstenEbene(
						projekt.getI_id(), false, theClientDto);

				oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_TEXT] = projekt.getX_freetext();
				oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ERZEUGER] = HelperServer
						.formatPersonAusFLRPartner(projekt.getFlrpersonalErzeuger().getFlrpartner());
				oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ZUGEWIESENER] = HelperServer
						.formatPersonAusFLRPartner(projekt.getFlrpersonalZugewiesener().getFlrpartner());

				// PJ21294
				oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_WIRD_DURCHGEFUEHRT_VON] = HelperServer
						.formatPersonAusFLRPartner(projekt.getFlrpersonalZugewiesener().getFlrpartner());

				oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ZEIT] = projekt.getT_zeit();
				oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_DAUER] = projekt.getD_dauer();
				oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_VERRECHENBAR] = getProjektServiceFac()
						.getTextVerrechenbar(projekt.getI_verrechenbar(), theClientDto);
				oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_B_FREIGEGEBEN] = Helper
						.short2Boolean(projekt.getB_freigegeben());
				oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_DATEINAME] = projekt.getC_dateiname();

				if (projekt.getFlrvkfortschritt() != null) {
					oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_VKFORTSCHRITT] = projekt
							.getFlrvkfortschritt().getC_nr();

					Iterator itVK = projekt.getFlrvkfortschritt().getVkfortschrittspr_set().iterator();
					while (itVK.hasNext()) {
						FLRVkfortschrittspr spr = (FLRVkfortschrittspr) itVK.next();
						if (spr.getLocale().getC_nr().equals(theClientDto.getLocUiAsString())) {
							oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_VKFORTSCHRITT_BEZEICHNUNG] = spr
									.getC_bez();
						}

					}
					if (projekt.getFlrvkfortschritt().getFlrleadstatus() != null) {
						oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_VKFORTSCHRITT_LEADSTATUS] = projekt
								.getFlrvkfortschritt().getFlrleadstatus().getC_bez();
					}

				}

				// try {
				if (projekt.getFlransprechpartner() != null && (projekt.getFlransprechpartner().getI_id() != null)) {
					if (projekt.getFlransprechpartner().getFlrpartneransprechpartner() != null
							&& projekt.getFlransprechpartner().getFlrpartneransprechpartner().getI_id() != null) {
						oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ANSPRECHPARTNERCNAME1] = projekt
								.getFlransprechpartner().getFlrpartneransprechpartner().getC_name1nachnamefirmazeile1();
						oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ANSPRECHPARTNER] = (projekt
								.getFlransprechpartner().getFlrpartneransprechpartner().getC_name1nachnamefirmazeile1()
								+ " "
								+ (projekt.getFlransprechpartner().getFlrpartneransprechpartner()
										.getC_name2vornamefirmazeile2() == null
												? ""
												: projekt.getFlransprechpartner().getFlrpartneransprechpartner()
														.getC_name2vornamefirmazeile2())
								+ " "
								+ (projekt.getFlransprechpartner().getFlrpartneransprechpartner()
										.getC_name3vorname2abteilung() == null ? ""
												: projekt.getFlransprechpartner().getFlrpartneransprechpartner()
														.getC_name3vorname2abteilung())).trim();
					}
				}
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				//
				Criteria crit1 = session.createCriteria(FLRHistory.class);
				crit1.createCriteria(ProjektFac.FLR_HISTORY_FLRPROJEKT)
						.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_I_ID, projekt.getI_id()));

				crit1.addOrder(Order.desc(ProjektFac.FLR_HISTORY_T_BELEGDATUM));

				List<?> resultList = crit1.list();
				Iterator<?> itHistory = resultList.iterator();
				java.util.Date juengstesAenderungsdatum = null;
				while (itHistory.hasNext()) {
					FLRHistory history = (FLRHistory) itHistory.next();

					if (juengstesAenderungsdatum == null) {
						juengstesAenderungsdatum = history.getT_aendern();
					} else if (juengstesAenderungsdatum != null
							&& juengstesAenderungsdatum.before(history.getT_aendern())) {
						juengstesAenderungsdatum = history.getT_aendern();
					}

				}

				oZeileVorlage[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_JUENGSTES_AENDERUNGSDATUM] = juengstesAenderungsdatum;
				alDaten.add(oZeileVorlage);

				itHistory = resultList.iterator();
				while (itHistory.hasNext()) {
					FLRHistory history = (FLRHistory) itHistory.next();
					Object[] oZeile = oZeileVorlage.clone();

					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY] = Boolean.TRUE;
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PROJEKTCNR] = projekt.getC_nr();
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ZUGEWIESENER] = HelperServer
							.formatPersonAusFLRPartner(projekt.getFlrpersonalZugewiesener().getFlrpartner());
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_MITARBEITER] = HelperServer
							.formatPersonAusFLRPartner(history.getFlrpersonal().getFlrpartner());
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_BELEGDATUM] = history
							.getT_belegdatum();
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_AENDERUNGSDATUM] = history
							.getT_aendern();

					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_TEXT] = stripHtmlFontFamilyIfHtml(
							history.getX_text(), history.getB_html());
					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_HTML] = Helper
							.short2Boolean(history.getB_html());

					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_TITEL] = history.getC_titel();
					if (history.getFlrhistoryart() != null) {
						oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_HISTORYART] = history
								.getFlrhistoryart().getC_bez();
					}

					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_ERLEDIGUNGSGRAD] = history
							.getF_erledigungsgrad();
					if (history.getFlrpersonal_wirddurchgefuehrtvon() != null) {
						oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_WIRD_DURCHGEFUEHRT_VON] = HelperServer
								.formatPersonAusFLRPartner(
										history.getFlrpersonal_wirddurchgefuehrtvon().getFlrpartner());
					} else {
						// PJ21294
						oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_WIRD_DURCHGEFUEHRT_VON] = HelperServer
								.formatPersonAusFLRPartner(projekt.getFlrpersonalZugewiesener().getFlrpartner());
					}

					oZeile[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_DAUER_GEPLANT] = history
							.getN_dauer_geplant();

					alDaten.add(oZeile);
				}

				// die Parameter dem Report uebergeben
				HashMap<String, Object> parameter = new HashMap<String, Object>();

				parameter.put("P_STICHTAG", dStichtag);

				parameter.put(LPReport.P_SORTIERUNG,
						buildSortierungProjektOffene(reportJournalKriterienDtoI, theClientDto));
				if (bereichIId != null) {
					parameter.put("P_BEREICH", getProjektServiceFac().bereichFindByPrimaryKey(bereichIId).getCBez());
				}
				parameter.put(LPReport.P_FILTER, buildFilterProjektOffene(reportJournalKriterienDtoI, theClientDto));

				parameter.put(LPReport.P_SORTIERENACHPERSONAL,
						new Boolean(reportJournalKriterienDtoI.bSortiereNachPersonal));

				parameter.put("P_TITLE",
						getTextRespectUISpr("proj.print.offene", theClientDto.getMandant(), theClientDto.getLocUi()));

				if (reportJournalKriterienDtoI.bSortiereNachPersonal) {
					// Nach Personal sortieren
					for (int i = alDaten.size() - 1; i > 0; --i) {
						for (int j = 0; j < i; ++j) {
							Object[] a = (Object[]) alDaten.get(j);
							Object[] b = (Object[]) alDaten.get(j + 1);

							String partnerA = (String) a[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_WIRD_DURCHGEFUEHRT_VON];
							String partnerB = (String) b[ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_WIRD_DURCHGEFUEHRT_VON];

							if (partnerA.compareTo(partnerB) > 0) {
								Object[] h = a;
								alDaten.set(j, b);
								alDaten.set(j + 1, h);
							}
						}
					}
				}

				Object[][] returnArray = new Object[alDaten
						.size()][ProjektReportFac.REPORT_PROJEKTVERLAUF_ANZAHL_SPALTEN];
				data = (Object[][]) alDaten.toArray(returnArray);

				initJRDS(parameter, ProjektReportFac.REPORT_MODUL, cAktuellerReport, theClientDto.getMandant(),
						theClientDto.getLocUi(), theClientDto);

				oPrint = getReportPrint();

			}

		} finally {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE, he);
			}
		}

		return oPrint;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printProjektForecast(ReportJournalKriterienDto reportJournalKriterienDtoI, Date dStichtag,
			Integer bereichIId, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		JasperPrintLP oPrint = null;
		int iAnzahlZeilen = 0;
		cAktuellerReport = ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST;
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
			critProjekt.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_MANDANT_C_NR, theClientDto.getMandant()));
			critProjekt.add(Restrictions.not(
					Restrictions.eq(ProjektFac.FLR_PROJEKT_STATUS_C_NR, ProjektServiceFac.PROJEKT_STATUS_STORNIERT)));
			if (bereichIId != null) {
				critProjekt.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_BEREICH_I_ID, bereichIId));
			}

			// PJ18471
			critProjekt.createAlias("flrprojektstatus", "s");
			critProjekt.add(Restrictions.eq("s.b_erledigt", Helper.boolean2Short(false)));

			// SP4867 Realisierungstermin muss <= Stichag sein
			critProjekt.add(Restrictions.or(Restrictions.isNull(ProjektFac.FLR_PROJEKT_T_REALISIERUNG),
					Restrictions.le(ProjektFac.FLR_PROJEKT_T_REALISIERUNG, dStichtag)));

			// Einschraenkung nach einer bestimmten Perosn
			if (reportJournalKriterienDtoI.personalIId != null) {
				critProjekt.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_PERSONAL_I_ID_ZUGEWIESENER,
						reportJournalKriterienDtoI.personalIId));
			}

			// Sortierung nach Personal ist immer die erste Sortierung
			if (reportJournalKriterienDtoI.bSortiereNachPersonal) {
				critProjekt.createCriteria(ProjektFac.FLR_PROJEKT_FLRPERSONALZUGEWIESENER)
						.createCriteria(KundeFac.FLR_PARTNER)
						.addOrder(Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
			}

			// Sortierung nach Partner,
			if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				critProjekt.createCriteria(ProjektFac.FLR_PROJEKT_FLRPARTNER)
						.addOrder(Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
				critProjekt.addOrder(Order.asc(ProjektFac.FLR_PROJEKT_TYP_C_NR));
				critProjekt.addOrder(Order.asc(ProjektFac.FLR_PROJEKT_I_PRIO));
				critProjekt.addOrder(Order.asc(ProjektFac.FLR_PROJEKT_KATEGORIE_C_NR));
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
						.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_I_ID, projekt.getI_id()));
				List<?> historyList = critHistory.list();
				if (historyList.size() != 0) {
					iAnzahlZeilen = iAnzahlZeilen + historyList.size();
				}
				iAnzahlZeilen++;
			}

			data = new Object[iAnzahlZeilen][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_ANZAHL_SPALTEN];
			int i = 0;
			it = list.iterator();
			while (it.hasNext()) {
				FLRProjekt projekt = (FLRProjekt) it.next();
				if (projekt.getC_nr().equals("2016/00162")) {
					System.out.println("x");
				}
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_PROJEKTTITEL] = projekt.getC_titel();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_PROJEKTKATEGORIE] = projekt
						.getKategorie_c_nr();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_PROJEKTCNR] = projekt.getC_nr();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_STATUS] = projekt.getStatus_c_nr();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_REALISIERUNG] = projekt.getT_realisierung();

				if (projekt.getPartner_i_id_betreiber() != null) {
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_BETREIBER] = HelperServer
							.formatNameAusFLRPartner(projekt.getFlrpartnerbetreiber());
				}

				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_TYP] = projekt.getTyp_c_nr();

				if (projekt.getPersonal_i_id_internerledigt() != null) {
					PersonalDto personalDto = getPersonalFac()
							.personalFindByPrimaryKey(projekt.getPersonal_i_id_internerledigt(), theClientDto);
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_INTERNERLEDIGT_PERSON] = personalDto
							.getPartnerDto().formatAnrede();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_INTERNERLEDIGT_ZEIT] = projekt
							.getT_internerledigt();
				}
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_WAHRSCHEINLICHKEIT] = projekt
						.getI_wahrscheinlichkeit();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_GEPLANTERUMSATZ] = projekt
						.getN_umsatzgeplant();
				// Gesamte Dauer eines Projektes
				Double ddArbeitszeitist = getZeiterfassungFac().getSummeZeitenEinesBeleges(LocaleFac.BELEGART_PROJEKT,
						projekt.getI_id(), null, null, null, null, theClientDto);
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_GESAMTDAUER] = ddArbeitszeitist;

				if (projekt.getFlrpartner().getFlrlandplzort() != null) {
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_LKZ] = projekt.getFlrpartner()
							.getFlrlandplzort().getFlrland().getC_lkz();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_PLZ] = projekt.getFlrpartner()
							.getFlrlandplzort().getC_plz();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_ORT] = projekt.getFlrpartner()
							.getFlrlandplzort().getFlrort().getC_name();
				}

				if (projekt.getFlrpartner().getFlrpartnerklasse() != null) {
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_PARTNERKLASSE] = projekt.getFlrpartner()
							.getFlrpartnerklasse().getC_nr();
				}

				if (projekt.getFlrpartner().getFlrbranche() != null) {
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_BRANCHE] = projekt.getFlrpartner()
							.getFlrbranche().getC_nr();
				}

				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_KUNDECNAME1] = projekt.getFlrpartner()
						.getC_name1nachnamefirmazeile1();
				locDruck = Helper.string2Locale(projekt.getFlrpartner().getLocale_c_nr_kommunikation());
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_ZIELTERMIN] = Helper
						.formatDatum(projekt.getT_zielwunschdatum(), locDruck);
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_BELEGDATUM] = Helper
						.formatDatum(projekt.getT_anlegen(), locDruck);

				Calendar calendar = new GregorianCalendar();
				calendar.setTime(new Date(projekt.getT_zielwunschdatum().getTime()));
				int KW = calendar.get(Calendar.WEEK_OF_YEAR); // Kalendarwochen

				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_ZIELWOCHE] = "" + KW;

				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_PRIO] = projekt.getI_prio();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_I_SORT_QUEUE] = projekt.getI_sort();

				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_SUBREPORT_VATERPROJEKTE] = getSubreportVerknuepfteProjekteDerNaechstenEbene(
						projekt.getI_id(), true, theClientDto);
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_SUBREPORT_KINDPROJEKTE] = getSubreportVerknuepfteProjekteDerNaechstenEbene(
						projekt.getI_id(), false, theClientDto);

				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_TEXT] = projekt.getX_freetext();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_ERZEUGER] = projekt.getFlrpersonalErzeuger()
						.getFlrpartner().getC_name1nachnamefirmazeile1();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_ZUGEWIESENER] = projekt
						.getFlrpersonalZugewiesener().getFlrpartner().getC_name1nachnamefirmazeile1();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_ZEIT] = projekt.getT_zeit();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_DAUER] = projekt.getD_dauer();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_VERRECHENBAR] = getProjektServiceFac()
						.getTextVerrechenbar(projekt.getI_verrechenbar(), theClientDto);
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_B_FREIGEGEBEN] = Helper
						.short2Boolean(projekt.getB_freigegeben());
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_DATEINAME] = projekt.getC_dateiname();

				if (projekt.getFlrvkfortschritt() != null) {
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_VKFORTSCHRITT] = projekt
							.getFlrvkfortschritt().getC_nr();

					Iterator itVK = projekt.getFlrvkfortschritt().getVkfortschrittspr_set().iterator();
					while (itVK.hasNext()) {
						FLRVkfortschrittspr spr = (FLRVkfortschrittspr) itVK.next();
						if (spr.getLocale().getC_nr().equals(theClientDto.getLocUiAsString())) {
							data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_VKFORTSCHRITT_BEZEICHNUNG] = spr
									.getC_bez();
						}

					}
					if (projekt.getFlrvkfortschritt().getFlrleadstatus() != null) {
						data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_VKFORTSCHRITT_LEADSTATUS] = projekt
								.getFlrvkfortschritt().getFlrleadstatus().getC_bez();
					}

				}

				// try {
				if (projekt.getFlransprechpartner() != null && (projekt.getFlransprechpartner().getI_id() != null)) {
					if (projekt.getFlransprechpartner().getFlrpartneransprechpartner() != null
							&& projekt.getFlransprechpartner().getFlrpartneransprechpartner().getI_id() != null) {
						data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_ANSPRECHPARTNERCNAME1] = projekt
								.getFlransprechpartner().getFlrpartneransprechpartner().getC_name1nachnamefirmazeile1();
						data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_ANSPRECHPARTNER] = (projekt
								.getFlransprechpartner().getFlrpartneransprechpartner().getC_name1nachnamefirmazeile1()
								+ " "
								+ (projekt.getFlransprechpartner().getFlrpartneransprechpartner()
										.getC_name2vornamefirmazeile2() == null
												? ""
												: projekt.getFlransprechpartner().getFlrpartneransprechpartner()
														.getC_name2vornamefirmazeile2())
								+ " "
								+ (projekt.getFlransprechpartner().getFlrpartneransprechpartner()
										.getC_name3vorname2abteilung() == null ? ""
												: projekt.getFlransprechpartner().getFlrpartneransprechpartner()
														.getC_name3vorname2abteilung())).trim();
					}
				}

				// PJ21121
				LinkedHashMap<String, ProjektVerlaufHelperDto> hm = getProjektFac().getProjektVerlauf(projekt.getI_id(),
						theClientDto);
				Iterator<String> itVerlauf = hm.keySet().iterator();

				BigDecimal bdWertAuftrag = BigDecimal.ZERO;
				BigDecimal bdWertVerrechnet = BigDecimal.ZERO;
				BigDecimal bdWertGutschrift = BigDecimal.ZERO;

				while (itVerlauf.hasNext()) {
					ProjektVerlaufHelperDto belegDto = hm.get(itVerlauf.next());
					if (belegDto.getBelegDto() instanceof AuftragDto) {
						AuftragDto dto = (AuftragDto) belegDto.getBelegDto();
						if (!dto.getStatusCNr().equals(LocaleFac.STATUS_STORNIERT)
								&& dto.getNGesamtauftragswertInAuftragswaehrung() != null) {

							BigDecimal preisInMandantenwaehrung = dto.getNGesamtauftragswertInAuftragswaehrung().divide(
									new BigDecimal(dto.getFWechselkursmandantwaehrungzubelegwaehrung()), 4,
									BigDecimal.ROUND_HALF_EVEN);

							bdWertAuftrag = bdWertAuftrag.add(preisInMandantenwaehrung);
						}
					} else if (belegDto.getBelegDto() instanceof RechnungDto) {
						RechnungDto dto = (RechnungDto) belegDto.getBelegDto();

						if (!dto.getStatusCNr().equals(LocaleFac.STATUS_STORNIERT)
								&& dto.getNGesamtwertinbelegwaehrung() != null) {

							boolean bGutschrift = false;

							RechnungartDto raDto = getRechnungServiceFac()
									.rechnungartFindByPrimaryKey(dto.getRechnungartCNr(), theClientDto);

							if (raDto.getRechnungtypCNr().equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
								bGutschrift = true;
							}

							if (bGutschrift) {
								bdWertGutschrift = bdWertGutschrift.add(dto.getNWert());
							} else {
								bdWertVerrechnet = bdWertVerrechnet.add(dto.getNWert());
							}
						}
					}
				}

				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_WERT_AUFTRAG] = bdWertAuftrag;
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_WERT_RECHNUNG] = bdWertVerrechnet;
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_WERT_GUTSCHRIFT] = bdWertGutschrift;

				Criteria crit1 = session.createCriteria(FLRHistory.class);
				crit1.createCriteria(ProjektFac.FLR_HISTORY_FLRPROJEKT)
						.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_I_ID, projekt.getI_id()));

				crit1.addOrder(Order.desc(ProjektFac.FLR_HISTORY_T_BELEGDATUM));

				List<?> resultList = crit1.list();
				Iterator<?> itHistory = resultList.iterator();
				i++;
				while (itHistory.hasNext()) {
					FLRHistory history = (FLRHistory) itHistory.next();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_PROJEKTCNR] = projekt.getC_nr();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_ZUGEWIESENER] = projekt
							.getFlrpersonalZugewiesener().getFlrpartner().getC_name1nachnamefirmazeile1();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_HISTORY_MITARBEITER] = history
							.getFlrpersonal().getFlrpartner().getC_name1nachnamefirmazeile1();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_HISTORY_BELEGDATUM] = Helper
							.formatDatum(history.getT_belegdatum(), locDruck);
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_HISTORY_AENDERUNGSDATUM] = history
							.getT_aendern();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_HISTORY_TEXT] = stripHtmlFontFamilyIfHtml(
							history.getX_text(), history.getB_html());
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_HISTORY_HTML] = Helper
							.short2Boolean(history.getB_html());
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

		parameter.put("P_STICHTAG", dStichtag);
		parameter.put(LPReport.P_SORTIERUNG, buildSortierungProjektOffene(reportJournalKriterienDtoI, theClientDto));
		if (bereichIId != null) {
			parameter.put("P_BEREICH", getProjektServiceFac().bereichFindByPrimaryKey(bereichIId).getCBez());
		}

		parameter.put(LPReport.P_FILTER, buildFilterProjektOffene(reportJournalKriterienDtoI, theClientDto));

		parameter.put(LPReport.P_SORTIERENACHPERSONAL, reportJournalKriterienDtoI.bSortiereNachPersonal);

		if (reportJournalKriterienDtoI.personalIId != null) {
			parameter.put("P_PERSON",
					getPersonalFac().personalFindByPrimaryKey(reportJournalKriterienDtoI.personalIId, theClientDto)
							.formatFixUFTitelName2Name1());
		}

		parameter.put("P_TITLE",
				getTextRespectUISpr("proj.print.forecsat", theClientDto.getMandant(), theClientDto.getLocUi()));

		initJRDS(parameter, ProjektReportFac.REPORT_MODUL, cAktuellerReport, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		oPrint = getReportPrint();

		return oPrint;
	}

	private Object[] projektFuerAktivitaetsuebersichtBefuellen(Integer projektIId, TheClientDto theClientDto) {

		Object[] oZeile = new Object[REPORT_AKTIVITAETSUEBERSICHT_ANZAHL_SPALTEN];

		ProjektDto projektDto = null;
		try {
			projektDto = getProjektFac().projektFindByPrimaryKey(projektIId);

			if (projektDto.getCNr().indexOf("04558") > 0) {
				myLogger.warn("SP04558");
			}
			oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_VON] = projektDto.getTAnlegen();

			oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_ZIELWUNSCHTERMIN] = projektDto
					.getTZielwunschdatum();

			BereichDto bDto = getProjektServiceFac().bereichFindByPrimaryKey(projektDto.getBereichIId());
			oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_BEREICH] = bDto.getCBez();
			if (projektDto.getPartnerIIdBetreiber() != null) {
				oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_BETREIBER] = getPartnerFac()
						.partnerFindByPrimaryKey(projektDto.getPartnerIIdBetreiber(), theClientDto)
						.formatFixName1Name2();
			}

			oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_PERSONAL_ERZEUGER] = getPersonalFac()
					.personalFindByPrimaryKey(projektDto.getPersonalIIdErzeuger(), theClientDto).getPartnerDto()
					.formatFixName1Name2();
			oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_PERSONAL_MITARBEITER] = getPersonalFac()
					.personalFindByPrimaryKey(projektDto.getPersonalIIdZugewiesener(), theClientDto).getPartnerDto()
					.formatFixName1Name2();

			oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGART] = LocaleFac.BELEGART_PROJEKT;

			oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGNUMMER] = projektDto.getCNr();
			oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGSTATUS] = projektDto.getStatusCNr();

			if (projektDto.getPersonalIIdInternerledigt() != null) {
				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKey(projektDto.getPersonalIIdInternerledigt(), theClientDto);
				oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_INTERNERLEDIGT_PERSON] = personalDto
						.getPartnerDto().formatAnrede();
				oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_INTERNERLEDIGT_ZEIT] = projektDto
						.getTInternerledigt();
			}

			oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGTEXT] = projektDto.getCTitel() + " "
					+ projektDto.getXFreetext();

			PartnerDto pDto = getPartnerFac().partnerFindByPrimaryKey(projektDto.getPartnerIId(), theClientDto);

			oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PARTNER] = pDto.formatTitelAnrede();
			if (pDto.getLandplzortDto() != null) {
				oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PLZ] = pDto.getLandplzortDto().getCPlz();
				oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_LKZ] = pDto.getLandplzortDto().getLandDto()
						.getCLkz();
				oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_ORT] = pDto.getLandplzortDto().getOrtDto()
						.getCName();
			}

			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = factory.openSession();
			Criteria crit = session.createCriteria(FLRHistory.class);

			crit.createCriteria(ProjektFac.FLR_HISTORY_FLRPROJEKT)
					.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_I_ID, projektDto.getIId()));
			crit.addOrder(Order.desc(ProjektFac.FLR_HISTORY_T_BELEGDATUM));
			List<?> resultListHistory = crit.list();

			Iterator<?> it = resultListHistory.iterator();

			ArrayList<Object[]> alSub = new ArrayList<Object[]>();
			while (it.hasNext()) {
				FLRHistory history = (FLRHistory) it.next();

				Object[] oSub = new Object[9];

				oSub[0] = history.getT_belegdatum();
				oSub[1] = HelperServer.formatNameAusFLRPartner(history.getFlrpersonal().getFlrpartner());
				oSub[2] = stripHtmlFontFamilyIfHtml(history.getX_text(), history.getB_html());
				if (history.getFlrhistoryart() != null) {
					oSub[3] = history.getFlrhistoryart().getC_bez();
				}
				oSub[4] = history.getC_titel();
				oSub[5] = Helper.short2Boolean(history.getB_html());
				oSub[6] = history.getT_aendern();
				oSub[7] = history.getF_erledigungsgrad();

				if (history.getFlrpersonal_wirddurchgefuehrtvon() != null) {
					oSub[8] = HelperServer
							.formatPersonAusFLRPartner(history.getFlrpersonal_wirddurchgefuehrtvon().getFlrpartner());
				}

				alSub.add(oSub);
			}

			String[] fieldnames = new String[] { "F_DATUM", "F_PERSONAL", "F_TEXT", "F_ART", "F_TITEL", "F_HTML",
					"F_AENDERN", "F_ERLEDIGUNGSGRAD", "F_WIRD_DURCHGEFUEHRT_VON" };

			Object[][] dataSub = new Object[alSub.size()][fieldnames.length];
			dataSub = (Object[][]) alSub.toArray(dataSub);

			oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEG_SUBREPORT] = new LPDatenSubreport(dataSub,
					fieldnames);

			session.close();
		} catch (RemoteException ex3) {
			throwEJBExceptionLPRespectOld(ex3);
		}
		return oZeile;

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAktivitaetsuebersicht(DatumsfilterVonBis datumsfilter, boolean bGesamtinfo,
			int iSortierung, TheClientDto theClientDto) {

		cAktuellerReport = ProjektReportFac.REPORT_PROJEKT_JOURNAL_AKTIVITAETSUEBERSICHT;

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		ArrayList alDaten = new ArrayList();

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		HashMap hmProjekte = new HashMap();

		String sQuery = "select distinct  zeitdaten.c_belegartnr,zeitdaten.i_belegartid, zeitdaten.personal_i_id from FLRZeitdaten zeitdaten WHERE  zeitdaten.c_belegartnr is not null AND zeitdaten.t_zeit>='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(datumsfilter.getTimestampVon().getTime()))
				+ "' AND zeitdaten.t_zeit<='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(datumsfilter.getTimestampBis().getTime()))
				+ "' AND zeitdaten.flrpersonal.mandant_c_nr='" + theClientDto.getMandant() + "'";

		org.hibernate.Query inventurliste = session.createQuery(sQuery);

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListIterator = resultList.iterator();

		int row = 0;
		while (resultListIterator.hasNext()) {

			Object[] o = (Object[]) resultListIterator.next();

			Integer personalIId = (Integer) o[2];

			try {
				PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(personalIId, theClientDto);
				Object[] oZeile = new Object[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_ANZAHL_SPALTEN];

				oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGART] = o[0];
				oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PERSONAL] = personalDto.getPartnerDto()
						.formatFixName1Name2();

				String sBezeichnung = "";
				PartnerDto partnerDto = null;
				if (((String) o[0]).equals(LocaleFac.BELEGART_AUFTRAG)) {
					AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey((Integer) o[1]);
					oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGNUMMER] = auftragDto.getCNr();
					oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGSTATUS] = auftragDto.getStatusCNr();

					sBezeichnung = auftragDto.getCBezProjektbezeichnung();
					partnerDto = getKundeFac()
							.kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(), theClientDto)
							.getPartnerDto();
				} else if (((String) o[0]).equals(LocaleFac.BELEGART_LOS)) {
					com.lp.server.fertigung.service.LosDto losDto = getFertigungFac()
							.losFindByPrimaryKey((Integer) o[1]);
					oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGNUMMER] = losDto.getCNr();
					oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGSTATUS] = losDto.getStatusCNr();

					sBezeichnung = losDto.getCProjekt();

					if (losDto.getAuftragIId() != null) {

						AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(losDto.getAuftragIId());

						partnerDto = getKundeFac()
								.kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(), theClientDto)
								.getPartnerDto();
					}

				} else if (((String) o[0]).equals(LocaleFac.BELEGART_PROJEKT)) {

					if (!hmProjekte.containsKey((Integer) o[1])) {
						hmProjekte.put((Integer) o[1], "");
					}

					oZeile = projektFuerAktivitaetsuebersichtBefuellen((Integer) o[1], theClientDto);

				} else if (((String) o[0]).equals(LocaleFac.BELEGART_ANGEBOT)) {
					AngebotDto angebotDto = getAngebotFac().angebotFindByPrimaryKey((Integer) o[1], theClientDto);
					oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGNUMMER] = angebotDto.getCNr();
					oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGSTATUS] = angebotDto.getStatusCNr();

					sBezeichnung = angebotDto.getCBez();

					partnerDto = getKundeFac()
							.kundeFindByPrimaryKey(angebotDto.getKundeIIdAngebotsadresse(), theClientDto)
							.getPartnerDto();

					// oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_VON]
					// = projektDto.getTAnlegen();
					// oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BIS]
					// = projektDto.getTZielwunschdatum();

				}

				AuftragzeitenDto[] dtos = getZeiterfassungFac().getAllZeitenEinesBeleges((String) o[0], (Integer) o[1],
						null, personalIId, datumsfilter.getTimestampVon(), datumsfilter.getTimestampBis(),
						ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ARTIKEL, theClientDto);

				if (dtos.length > 0) {

					if (((String) o[0]).equals(LocaleFac.BELEGART_PROJEKT) != true) {
						oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_VON] = dtos[0].getTsBeginn();
					}
					oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BIS] = dtos[dtos.length - 1].getTsEnde();
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
						oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PARTNER] = partnerDto.formatTitelAnrede();

						if (partnerDto.getLandplzortDto() != null) {
							oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_LKZ] = partnerDto.getLandplzortDto()
									.getLandDto().getCLkz();
							oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PLZ] = partnerDto.getLandplzortDto()
									.getCPlz();
							oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_ORT] = partnerDto.getLandplzortDto()
									.getOrtDto().getCName();
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
		org.hibernate.Criteria crit = session.createCriteria(FLRTelefonzeiten.class)
				.createAlias(ZeiterfassungFac.FLR_TELEFONZEITEN_FLRPERSONAL, "p")
				.add(Restrictions.eq("p.mandant_c_nr", theClientDto.getMandant()));

		crit.add(Restrictions.ge(ZeiterfassungFac.FLR_TELEFONZEITEN_T_VON, datumsfilter.getTimestampVon()));
		crit.add(Restrictions.lt(ZeiterfassungFac.FLR_TELEFONZEITEN_T_VON, datumsfilter.getTimestampBis()));

		List<?> list = crit.list();
		Iterator<?> iterator = list.iterator();

		while (iterator.hasNext()) {
			FLRTelefonzeiten flrTelefonzeiten = (FLRTelefonzeiten) iterator.next();

			Object[] oZeile = new Object[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_ANZAHL_SPALTEN];

			oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGART] = "Telefon";
			oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGTEXT] = flrTelefonzeiten.getX_kommentarext();

			if (flrTelefonzeiten.getT_bis() != null) {
				java.sql.Time tTemp = new java.sql.Time(
						flrTelefonzeiten.getT_bis().getTime() - flrTelefonzeiten.getT_von().getTime() - 3600000);
				oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_DAUER] = Helper.time2Double(tTemp);
				oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BIS] = new Timestamp(
						flrTelefonzeiten.getT_bis().getTime());
			}

			oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_VON] = new Timestamp(
					flrTelefonzeiten.getT_von().getTime());

			if (flrTelefonzeiten.getFlrpartner() != null) {

				com.lp.server.partner.service.PartnerDto partnerDto = getPartnerFac()
						.partnerFindByPrimaryKey(flrTelefonzeiten.getFlrpartner().getI_id(), theClientDto);
				oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PARTNER] = partnerDto.formatFixTitelName1Name2();

			} else {
				oZeile[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PARTNER] = "";
			}

		}

		// Projekte, welche wiese Woche angelegt wurde, bzw. ein neues Detail
		// hinzugefuegt wurde

		Criteria critProjekt = session.createCriteria(FLRHistory.class);

		critProjekt.add(Restrictions.between(ProjektFac.FLR_HISTORY_T_AENDERN, datumsfilter.getTimestampVon(),
				datumsfilter.getTimestampBis()));

		critProjekt.createAlias("flrprojekt", "p").add(Restrictions.eq("p.mandant_c_nr", theClientDto.getMandant()));

		List<?> resultListVorher = critProjekt.list();

		Iterator<?> itVorher = resultListVorher.iterator();

		while (itVorher.hasNext()) {
			FLRHistory history = (FLRHistory) itVorher.next();

			if (!hmProjekte.containsKey(history.getProjekt_i_id())) {
				if (!hmProjekte.containsKey(history.getProjekt_i_id())) {
					hmProjekte.put(history.getProjekt_i_id(), "");
				}
				Object[] z = projektFuerAktivitaetsuebersichtBefuellen(history.getProjekt_i_id(), theClientDto);

				alDaten.add(z);
			}

		}

		session.close();

		session = factory.openSession();

		critProjekt = session.createCriteria(FLRProjekt.class);

		critProjekt.add(Restrictions.between(ProjektFac.FLR_PROJEKT_T_ANLEGEN, datumsfilter.getTimestampVon(),
				datumsfilter.getTimestampBis()));
		critProjekt.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));

		resultListVorher = critProjekt.list();

		itVorher = resultListVorher.iterator();

		while (itVorher.hasNext()) {
			FLRProjekt projekt = (FLRProjekt) itVorher.next();

			if (!hmProjekte.containsKey(projekt.getI_id())) {

				Object[] z = projektFuerAktivitaetsuebersichtBefuellen(projekt.getI_id(), theClientDto);
				alDaten.add(z);

			}

		}

		session.close();

		// Nach Partner sortieren
		for (int i = alDaten.size() - 1; i > 0; --i) {
			for (int j = 0; j < i; ++j) {
				Object[] a = (Object[]) alDaten.get(j);
				Object[] b = (Object[]) alDaten.get(j + 1);

				String partnerA = (String) a[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PARTNER];
				String partnerB = (String) b[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PARTNER];

				partnerA = Helper.fitString2Length(partnerA, 80, ' ');
				partnerB = Helper.fitString2Length(partnerB, 80, ' ');

				String belegartA = (String) a[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGART];
				String belegartB = (String) b[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGART];

				belegartA = Helper.fitString2Length(belegartA, 40, ' ');
				belegartB = Helper.fitString2Length(belegartB, 40, ' ');

				String belegnummerA = (String) a[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGNUMMER];
				String belegnummerB = (String) b[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGNUMMER];

				if (belegnummerA == null) {
					belegnummerA = "";
				}
				if (belegnummerB == null) {
					belegnummerB = "";
				}

				belegnummerA = Helper.fitString2Length(belegnummerA, 40, ' ');
				belegnummerB = Helper.fitString2Length(belegnummerB, 40, ' ');

				String bereichA = (String) a[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_BEREICH];
				String bereichB = (String) b[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_BEREICH];

				if (bereichA == null) {
					bereichA = "";
				}
				if (bereichB == null) {
					bereichB = "";
				}
				bereichA = Helper.fitString2Length(bereichA, 40, ' ');
				bereichB = Helper.fitString2Length(bereichB, 40, ' ');

				String mitarbeiterA = (String) a[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_PERSONAL_MITARBEITER];
				String mitarbeiterB = (String) b[ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_PERSONAL_MITARBEITER];

				if (mitarbeiterA == null) {
					mitarbeiterA = "";
				}
				if (mitarbeiterB == null) {
					mitarbeiterB = "";
				}

				mitarbeiterA = Helper.fitString2Length(mitarbeiterA, 100, ' ');
				mitarbeiterB = Helper.fitString2Length(mitarbeiterB, 100, ' ');

				String sortStringA = "";
				String sortStringB = "";

				if (iSortierung == OPTION_SORTIERUNG_AKTIVITAETSUEBERSICHT_PARTNER) {
					sortStringA = partnerA + belegartA + bereichA + belegnummerA;
					sortStringB = partnerB + belegartB + bereichB + belegnummerB;
				} else if (iSortierung == OPTION_SORTIERUNG_AKTIVITAETSUEBERSICHT_MITARBEITER) {
					sortStringA = mitarbeiterA + belegartA + bereichA + belegnummerA;
					sortStringB = mitarbeiterB + belegartB + bereichB + belegnummerB;
				} else if (iSortierung == OPTION_SORTIERUNG_AKTIVITAETSUEBERSICHT_BELEGART_BELEGNUMMER) {
					sortStringA = belegartA + bereichA + belegnummerA;
					sortStringB = belegartB + bereichB + belegnummerB;
				}

				if (sortStringA.compareTo(sortStringB) > 0) {
					Object[] h = a;
					alDaten.set(j, b);
					alDaten.set(j + 1, h);
				}
			}
		}

		Object[][] returnArray = new Object[alDaten
				.size()][ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(returnArray);

		parameter.put("P_VON", datumsfilter.getTimestampVon());
		parameter.put("P_BIS", datumsfilter.getTimestampBisUnveraendert());
		parameter.put("P_GESAMTINFO", new Boolean(bGesamtinfo));

		if (iSortierung == OPTION_SORTIERUNG_AKTIVITAETSUEBERSICHT_MITARBEITER) {
			parameter.put("P_SORTIERUNG", getTextRespectUISpr("projekt.aktivitaetsuebersicht.sortierung.mitarbeiter",
					theClientDto.getMandant(), theClientDto.getLocUi()));
		} else if (iSortierung == OPTION_SORTIERUNG_AKTIVITAETSUEBERSICHT_PARTNER) {
			parameter.put("P_SORTIERUNG", getTextRespectUISpr("projekt.aktivitaetsuebersicht.sortierung.partner",
					theClientDto.getMandant(), theClientDto.getLocUi()));
		} else if (iSortierung == OPTION_SORTIERUNG_AKTIVITAETSUEBERSICHT_BELEGART_BELEGNUMMER) {
			parameter.put("P_SORTIERUNG", getTextRespectUISpr("projekt.aktivitaetsuebersicht.sortierung.beleg",
					theClientDto.getMandant(), theClientDto.getLocUi()));
		}

		initJRDS(parameter, ProjektReportFac.REPORT_MODUL, cAktuellerReport, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAenderungenEigenschaften(Integer projektIId, TheClientDto theClientDto) {

		// Erstellung des Reports
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		ProjektDto dto = null;
		try {
			dto = getProjektFac().projektFindByPrimaryKey(projektIId);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		BereichDto bDto = getProjektServiceFac().bereichFindByPrimaryKey(dto.getBereichIId());
		parameter.put("P_PROJEKT", dto.getCNr());
		parameter.put("P_BEREICH", bDto.getCBez());
		parameter.put("P_TITEL", dto.getCTitel());
		index = -1;
		cAktuellerReport = ProjektReportFac.REPORT_AENDERUNGEN_EIGENSCHAFTEN;

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT el FROM FLREntitylog el WHERE el.entity_i_id IN (SELECT el2.entity_i_id FROM FLREntitylog el2 WHERE el2.filter_i_id='"
				+ projektIId + "' AND el2.c_von='" + PanelFac.PANEL_PROJEKTEIGENSCHAFTEN
				+ "') ORDER BY el.t_aendern DESC";

		org.hibernate.Query query = session.createQuery(sQuery);

		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		data = new Object[results.size()][ProjektReportFac.REPORT_AENDERUNGEN_EIGENSCHAFTEN_ANZAHL_SPALTEN];

		int i = 0;
		while (resultListIterator.hasNext()) {
			FLREntitylog flrArtikellog = (FLREntitylog) resultListIterator.next();
			data[i][ProjektReportFac.REPORT_AENDERUNGEN_EIGENSCHAFTEN_I_ID] = flrArtikellog.getC_key();
			data[i][ProjektReportFac.REPORT_AENDERUNGEN_EIGENSCHAFTEN_VON] = flrArtikellog.getC_von();
			data[i][ProjektReportFac.REPORT_AENDERUNGEN_EIGENSCHAFTEN_NACH] = flrArtikellog.getC_nach();
			data[i][ProjektReportFac.REPORT_AENDERUNGEN_EIGENSCHAFTEN_LOCALE] = flrArtikellog.getLocale_c_nr();
			data[i][ProjektReportFac.REPORT_AENDERUNGEN_EIGENSCHAFTEN_ZEITPUNKT] = flrArtikellog.getT_aendern();
			data[i][ProjektReportFac.REPORT_AENDERUNGEN_EIGENSCHAFTEN_OPERATION] = flrArtikellog.getC_operation();
			data[i][ProjektReportFac.REPORT_AENDERUNGEN_EIGENSCHAFTEN_KEY] = flrArtikellog.getC_key();

			data[i][ProjektReportFac.REPORT_AENDERUNGEN_EIGENSCHAFTEN_PERSON] = HelperServer
					.formatNameAusFLRPartner(flrArtikellog.getFlrpersonal().getFlrpartner());

			i++;
		}

		initJRDS(parameter, ProjektReportFac.REPORT_MODUL, ProjektReportFac.REPORT_AENDERUNGEN_EIGENSCHAFTEN,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printProjektzeiten(Integer projektIId, int iSortierung, TheClientDto theClientDto) {

		cAktuellerReport = ProjektReportFac.REPORT_PROJEKTZEITDATEN;
		TreeMap<String, Object[]> alDaten = new TreeMap<String, Object[]>();
		try {
			ProjektDto pjDto = getProjektFac().projektFindByPrimaryKey(projektIId);

			FilterKriterium[] filterKrit = new FilterKriterium[1];
			FilterKriterium krit1 = new FilterKriterium("projekt_i_id", true, projektIId.toString(),
					FilterKriterium.OPERATOR_EQUAL, false);

			filterKrit[0] = krit1;

			QueryParameters p = new QueryParameters(QueryParameters.UC_ID_PROJEKTVERLAUF, null,
					new FilterBlock(filterKrit, "AND"), null, null);

			ProjektverlaufHandler pv = new ProjektverlaufHandler();
			pv.setCurrentUser(theClientDto);
			pv.setQuery(p);

			LinkedHashMap<String, ProjektVerlaufHelperDto> hm = pv.setInhalt();

			ProjektVerlaufHelperDto pvDto = new ProjektVerlaufHelperDto(0, pjDto);
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

					alDaten = befuelleZeileProjektzeitdaten(iSortierung, theClientDto, alDaten, belegart, belegnummer,
							belegIId, belegDto.getiEbene());
				}

			}
			HashMap<String, Object> parameter = new HashMap<String, Object>();
			if (iSortierung == SORTIERUNG_PROJEKTZEITEN_BELEGART_BELEG_PERSON) {
				parameter.put("P_SORTIERUNG",
						getTextRespectUISpr("proj.projektzeitdaten.sortierung.belegartbelegperson",
								theClientDto.getMandant(), theClientDto.getLocUi()));
			} else if (iSortierung == SORTIERUNG_PROJEKTZEITEN_PERSON_BELEGART_BELEG) {
				parameter.put("P_SORTIERUNG",
						getTextRespectUISpr("proj.projektzeitdaten.sortierung.personbelegartbeleg",
								theClientDto.getMandant(), theClientDto.getLocUi()));
			} else if (iSortierung == SORTIERUNG_PROJEKTZEITEN_TAETIEKGKEIT_DATUM_PERSON) {
				parameter.put("P_SORTIERUNG", getTextRespectUISpr("proj.report.projektzeiten.sortierungtaetigkeit",
						theClientDto.getMandant(), theClientDto.getLocUi()));
			} else if (iSortierung == SORTIERUNG_PROJEKTZEITEN_DATUM_ABSTEIGEND_PERSON) {
				parameter.put("P_SORTIERUNG", getTextRespectUISpr("proj.report.projektzeiten.sortierung.datum",
						theClientDto.getMandant(), theClientDto.getLocUi()));
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

			initJRDS(parameter, ProjektReportFac.REPORT_MODUL, cAktuellerReport, theClientDto.getMandant(),
					theClientDto.getLocUi(), theClientDto);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return getReportPrint();

	}

	private TreeMap<String, Object[]> befuelleZeileProjektzeitdaten(int iSortierung, TheClientDto theClientDto,
			TreeMap<String, Object[]> alDaten, String belegart, String belegnummer, Integer belegIId, int iEbene) {
		AuftragzeitenDto[] dtos = getZeiterfassungFac().getAllZeitenEinesBeleges(belegart, belegIId, null, null, null,
				null, ZeiterfassungFac.SORTIERUNG_ZEITDATEN_PERSONAL, theClientDto);

		if (belegart == LocaleFac.BELEGART_PROJEKT) {
			// Telefonzeiten hinzufuegen und sortieren
			dtos = AuftragzeitenDto.add2BelegzeitenDtos(
					getZeiterfassungFac().getAllTelefonzeitenEinesProjekts(belegIId, null, null, null, theClientDto),
					dtos);

		}

		for (int i = 0; i < dtos.length; i++) {

			Object[] oZeile = new Object[ProjektReportFac.REPORT_PROJEKTZEITEN_ANZAHL_SPALTEN];
			oZeile[ProjektReportFac.REPORT_PROJEKTZEITEN_BELEG] = belegnummer;
			oZeile[ProjektReportFac.REPORT_PROJEKTZEITEN_EBENE] = iEbene;
			oZeile[ProjektReportFac.REPORT_PROJEKTZEITEN_BELEGART] = belegart;
			oZeile[ProjektReportFac.REPORT_PROJEKTZEITEN_TELEFONZEIT] = new Boolean(dtos[i].isBTelefonzeit());

			oZeile[ProjektReportFac.REPORT_PROJEKTZEITEN_BIS] = dtos[i].getTsEnde();

			oZeile[ProjektReportFac.REPORT_PROJEKTZEITEN_VON] = dtos[i].getTsBeginn();

			oZeile[ProjektReportFac.REPORT_PROJEKTZEITEN_PERSON] = dtos[i].getSPersonalMaschinenname();
			oZeile[ProjektReportFac.REPORT_PROJEKTZEITEN_ZEITDATEN_I_ID] = dtos[i].getZeitdatenIIdBelegbuchung();
			oZeile[ProjektReportFac.REPORT_PROJEKTZEITEN_DAUER] = dtos[i].getDdDauer();

			oZeile[ProjektReportFac.REPORT_PROJEKTZEITEN_ARTIKEL] = dtos[i].getSArtikelcnr();
			oZeile[ProjektReportFac.REPORT_PROJEKTZEITEN_BEZEICHNUNG] = dtos[i].getSArtikelbezeichnung();
			oZeile[ProjektReportFac.REPORT_PROJEKTZEITEN_BEMERKUNG] = dtos[i].getSZeitbuchungtext();
			oZeile[ProjektReportFac.REPORT_PROJEKTZEITEN_KOMMENTAR] = dtos[i].getSKommentar();
			oZeile[ProjektReportFac.REPORT_PROJEKTZEITEN_KOSTEN] = dtos[i].getBdKosten();

			String sort = "";
			if (iSortierung == SORTIERUNG_PROJEKTZEITEN_BELEGART_BELEG_PERSON) {
				sort = Helper.fitString2Length(belegart, 15, ' ') + Helper.fitString2Length(belegnummer, 20, ' ')
						+ Helper.fitString2Length(dtos[i].getSPersonalMaschinenname(), 40, ' ') + dtos[i].getTsBeginn();
			} else if (iSortierung == SORTIERUNG_PROJEKTZEITEN_TAETIEKGKEIT_DATUM_PERSON) {
				sort = Helper.fitString2Length(dtos[i].getSArtikelcnr(), 40, ' ') + dtos[i].getTsBeginn()
						+ Helper.fitString2Length(dtos[i].getSPersonalMaschinenname(), 40, ' ');
			} else if (iSortierung == SORTIERUNG_PROJEKTZEITEN_PERSON_BELEGART_BELEG) {
				sort = Helper.fitString2Length(dtos[i].getSPersonalMaschinenname(), 40, ' ')
						+ Helper.fitString2Length(belegart, 15, ' ') + Helper.fitString2Length(belegnummer, 20, ' ')
						+ dtos[i].getTsBeginn();
			} else if (iSortierung == SORTIERUNG_PROJEKTZEITEN_DATUM_ABSTEIGEND_PERSON) {
				sort = new Timestamp((1L - dtos[i].getTsBeginn().getTime()))
						+ Helper.fitString2Length(dtos[i].getSPersonalMaschinenname(), 40, ' ')
						+ Helper.fitString2Length(dtos[i].getSArtikelcnr(), 40, ' ');
			}

			alDaten.put(sort, oZeile);

		}

		// PJ19883
		if (belegart == LocaleFac.BELEGART_PROJEKT) {

			iEbene = iEbene + 1;

			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			session = factory.openSession();
			String queryString = "SELECT pg FROM FLRProjektgruppe pg WHERE pg.projekt_i_id_vater=" + belegIId;

			org.hibernate.Query query = session.createQuery(queryString);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			while (resultListIterator.hasNext()) {
				FLRProjektgruppe o = (FLRProjektgruppe) resultListIterator.next();

				alDaten = befuelleZeileProjektzeitdaten(iSortierung, theClientDto, alDaten, LocaleFac.BELEGART_PROJEKT,
						o.getFlrprojekt_kind().getC_nr(), o.getFlrprojekt_kind().getI_id(), iEbene);

			}
		}

		return alDaten;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printProjektstatistik(Integer projektIId, int iOption, boolean bAktuellerStand,
			TheClientDto theClientDto) {
		cAktuellerReport = ProjektReportFac.REPORT_PROJEKTSTATISTIK;
		try {
			ProjektDto pjDto = getProjektFac().projektFindByPrimaryKey(projektIId);

			HashMap<String, Object> parameter = new HashMap<String, Object>();

			parameter.put("P_PROJEKT", pjDto.getCNr());

			BereichDto bereichDto = getProjektServiceFac().bereichFindByPrimaryKey(pjDto.getBereichIId());

			parameter.put("P_BEREICH", bereichDto.getCBez());

			parameter.put("P_TITEL", pjDto.getCTitel());

			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(pjDto.getPartnerIId(), theClientDto);
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(pjDto.getMandantCNr(), theClientDto);

			Locale locDruck = Helper.string2Locale(partnerDto.getLocaleCNrKommunikation());

			parameter.put("P_KUNDE_ADRESSBLOCK", formatAdresseFuerAusdruck(partnerDto, null, mandantDto, locDruck));

			parameter.put("P_AKTUELLER_STAND", bAktuellerStand);

			if (iOption == REPORT_PROJEKTSTATISTIK_OPTION_ALLE) {
				parameter.put("P_OPTION",
						getTextRespectUISpr("lp.alle", theClientDto.getMandant(), theClientDto.getLocUi()));
			} else if (iOption == REPORT_PROJEKTSTATISTIK_OPTION_EK) {
				parameter.put("P_OPTION",
						getTextRespectUISpr("lp.einkauf", theClientDto.getMandant(), theClientDto.getLocUi()));
			} else if (iOption == REPORT_PROJEKTSTATISTIK_OPTION_VK) {
				parameter.put("P_OPTION",
						getTextRespectUISpr("lp.verkauf", theClientDto.getMandant(), theClientDto.getLocUi()));
			} else if (iOption == REPORT_PROJEKTSTATISTIK_OPTION_FERTIGUNG) {
				parameter.put("P_OPTION",
						getTextRespectUISpr("lp.fertigung", theClientDto.getMandant(), theClientDto.getLocUi()));
			}

			LinkedHashMap<String, ProjektVerlaufHelperDto> hm = getProjektFac().getProjektVerlauf(projektIId,
					theClientDto);

			Iterator<String> it = hm.keySet().iterator();

			ArrayList alDaten = new ArrayList();

			while (it.hasNext()) {
				ProjektVerlaufHelperDto belegDto = hm.get(it.next());

				// Ohne ER
				if (belegDto.getBelegDto() instanceof EingangsrechnungAuftragszuordnungDto
						|| belegDto.getBelegDto() instanceof TelefonzeitenDto) {
					continue;
				}

				String belegart = "Unbekannt";
				String belegnummer = "Unbekannt";
				java.util.Date belegdatum = null;
				String partner = "";
				if (belegDto.getPartnerIId() != null) {
					partner = getPartnerFac().partnerFindByPrimaryKeySmall(belegDto.getPartnerIId(), theClientDto)
							.formatFixName1Name2();
				}

				if (belegDto.getBelegDto() instanceof BelegDto) {
					belegdatum = ((BelegDto) belegDto.getBelegDto()).getTBelegdatum();
					belegnummer = ((BelegDto) belegDto.getBelegDto()).getCNr();
					belegart = ((BelegDto) belegDto.getBelegDto()).getBelegartCNr();
				}

				boolean bVorzeichenNegieren = false;
				int iTyp = -1;
				if (belegDto.getBelegDto() instanceof AngebotDto || belegDto.getBelegDto() instanceof AuftragDto
						|| belegDto.getBelegDto() instanceof LieferscheinDto
						|| belegDto.getBelegDto() instanceof RechnungDto
						|| belegDto.getBelegDto() instanceof AgstklDto) {
					iTyp = REPORT_PROJEKTSTATISTIK_OPTION_VK;

					bVorzeichenNegieren = true;

					if (belegDto.getBelegDto() instanceof RechnungDto) {
						String rechnungart = ((RechnungDto) belegDto.getBelegDto()).getRechnungartCNr();
						RechnungartDto raDto = getRechnungServiceFac().rechnungartFindByPrimaryKey(rechnungart,
								theClientDto);
						if (raDto.getRechnungtypCNr().equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
							bVorzeichenNegieren = false;
						}

					}

				}

				if (belegDto.getBelegDto() instanceof ReklamationDto) {
					if (((ReklamationDto) belegDto.getBelegDto()).getReklamationartCNr()
							.equals(ReklamationFac.REKLAMATIONART_LIEFERANT)) {
						iTyp = REPORT_PROJEKTSTATISTIK_OPTION_EK;
					} else {
						iTyp = REPORT_PROJEKTSTATISTIK_OPTION_VK;
					}
				}

				if (belegDto.getBelegDto() instanceof BestellungDto || belegDto.getBelegDto() instanceof AnfrageDto) {
					iTyp = REPORT_PROJEKTSTATISTIK_OPTION_EK;
				}
				if (belegDto.getBelegDto() instanceof LosDto) {
					iTyp = REPORT_PROJEKTSTATISTIK_OPTION_FERTIGUNG;
					bVorzeichenNegieren = true;
				}

				if (iOption == REPORT_PROJEKTSTATISTIK_OPTION_VK && iTyp != REPORT_PROJEKTSTATISTIK_OPTION_VK) {
					continue;
				}
				if (iOption == REPORT_PROJEKTSTATISTIK_OPTION_EK && iTyp != REPORT_PROJEKTSTATISTIK_OPTION_EK) {
					continue;
				}
				if (iOption == REPORT_PROJEKTSTATISTIK_OPTION_FERTIGUNG
						&& iTyp != REPORT_PROJEKTSTATISTIK_OPTION_FERTIGUNG) {
					continue;
				}

				Object[] oZeileVorlage = new Object[REPORT_PROJEKTSTATISTIK_ANZAHL_SPALTEN];

				oZeileVorlage[REPORT_PROJEKTSTATISTIK_MENGE] = BigDecimal.ONE;

				if (belegDto.getBelegDto() instanceof ReklamationDto) {
					ReklamationDto reklaDto = (ReklamationDto) belegDto.getBelegDto();
					belegdatum = reklaDto.getTBelegdatum();
					belegnummer = reklaDto.getCNr();
					belegart = LocaleFac.BELEGART_REKLAMATION;
					oZeileVorlage[REPORT_PROJEKTSTATISTIK_MENGE] = reklaDto.getNMenge();
					oZeileVorlage[REPORT_PROJEKTSTATISTIK_SNRCHNR] = reklaDto.getCSeriennrchargennr();
					if (reklaDto.getArtikelIId() != null) {
						ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(reklaDto.getArtikelIId(),
								theClientDto);

						oZeileVorlage[REPORT_PROJEKTSTATISTIK_ARTIKEL] = aDto.getCNr();
						if (aDto.getArtikelsprDto() != null) {
							oZeileVorlage[REPORT_PROJEKTSTATISTIK_ARTIKELBEZEICHNUNG] = aDto.getArtikelsprDto()
									.getCBez();
							oZeileVorlage[REPORT_PROJEKTSTATISTIK_ARTIKELZUSATZBEZEICHNUNG] = aDto.getArtikelsprDto()
									.getCZbez();
							oZeileVorlage[REPORT_PROJEKTSTATISTIK_ARTIKELZUSATZBEZEICHNUNG2] = aDto.getArtikelsprDto()
									.getCZbez2();
						}
					} else {
						oZeileVorlage[REPORT_PROJEKTSTATISTIK_ARTIKEL] = reklaDto.getCHandartikel();
					}

					oZeileVorlage[REPORT_PROJEKTSTATISTIK_MENGE] = reklaDto.getNMenge();
				}

				if (belegDto.getBelegDto() instanceof LosDto) {
					LosDto losDto = (LosDto) belegDto.getBelegDto();
					belegart = LocaleFac.BELEGART_LOS;
					belegnummer = losDto.getCNr();
					belegdatum = losDto.getTProduktionsende();
				}

				oZeileVorlage[REPORT_PROJEKTSTATISTIK_BELEGART] = belegart;
				oZeileVorlage[REPORT_PROJEKTSTATISTIK_BELEGNUMMER] = belegnummer;
				oZeileVorlage[REPORT_PROJEKTSTATISTIK_BELEGDATUM] = belegdatum;
				oZeileVorlage[REPORT_PROJEKTSTATISTIK_PARTNER] = partner;

				if (belegDto.getBelegpositionenDtos() != null && belegDto.getBelegpositionenDtos().size() > 0) {

					for (int i = 0; i < belegDto.getBelegpositionenDtos().size(); i++) {

						Object[] oZeile = oZeileVorlage.clone();

						Integer artikelIId = belegDto.getBelegpositionenDtos().get(i).getArtikelIId();

						if (artikelIId != null) {

							ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(artikelIId, theClientDto);

							oZeile[REPORT_PROJEKTSTATISTIK_ARTIKEL] = aDto.getCNr();
							if (aDto.getArtikelsprDto() != null) {
								oZeile[REPORT_PROJEKTSTATISTIK_ARTIKELBEZEICHNUNG] = aDto.getArtikelsprDto().getCBez();
								oZeile[REPORT_PROJEKTSTATISTIK_ARTIKELZUSATZBEZEICHNUNG] = aDto.getArtikelsprDto()
										.getCZbez();
								oZeile[REPORT_PROJEKTSTATISTIK_ARTIKELZUSATZBEZEICHNUNG2] = aDto.getArtikelsprDto()
										.getCZbez2();
							}

							BigDecimal bdPositionsmenge = belegDto.getBelegpositionenDtos().get(i).getNMenge();

							if (bVorzeichenNegieren) {
								bdPositionsmenge = bdPositionsmenge.negate();
							}
							oZeile[REPORT_PROJEKTSTATISTIK_MENGE] = bdPositionsmenge;

							if (belegDto.getBelegpositionenDtos().get(i) instanceof LossollmaterialDto) {

								LossollmaterialDto sollDto = (LossollmaterialDto) belegDto.getBelegpositionenDtos()
										.get(i);

								LosistmaterialDto[] istMatDtos = getFertigungFac()
										.losistmaterialFindByLossollmaterialIId(sollDto.getIId());
								for (int j = 0; j < istMatDtos.length; j++) {
									LosistmaterialDto istmatDto = istMatDtos[j];

									List<SeriennrChargennrMitMengeDto> snrs = getLagerFac()
											.getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(
													LocaleFac.BELEGART_LOS, istmatDto.getIId());

									if (snrs != null && snrs.size() > 0) {
										for (int k = 0; k < snrs.size(); k++) {
											Object[] oZeileSnr = oZeile.clone();

											oZeileSnr[REPORT_PROJEKTSTATISTIK_SNRCHNR] = snrs.get(k)
													.getCSeriennrChargennr();
											oZeileSnr[REPORT_PROJEKTSTATISTIK_MENGE] = snrs.get(k)
													.getNMengeInAbhaengigkeitVonPositionsmenge(bdPositionsmenge);

											alDaten.add(oZeileSnr);
										}

									} else {
										alDaten.add(oZeile);
									}

								}

							} else if (belegDto.getBelegpositionenDtos().get(i) instanceof BestellpositionDto) {

								BestellpositionDto bsDto = (BestellpositionDto) belegDto.getBelegpositionenDtos()
										.get(i);

								WareneingangspositionDto[] weposDtos = getWareneingangFac()
										.wareneingangspositionFindByBestellpositionIId(bsDto.getIId());
								for (int j = 0; j < weposDtos.length; j++) {
									WareneingangDto weDto = getWareneingangFac()
											.wareneingangFindByPrimaryKey(weposDtos[j].getWareneingangIId());

									oZeile[REPORT_PROJEKTSTATISTIK_BELEGDATUM] = weDto.getTWareneingangsdatum();

									List<SeriennrChargennrMitMengeDto> snrs = weposDtos[j]
											.getSeriennrChargennrMitMenge();

									if (snrs != null && snrs.size() > 0) {
										for (int k = 0; k < snrs.size(); k++) {
											Object[] oZeileSnr = oZeile.clone();

											oZeileSnr[REPORT_PROJEKTSTATISTIK_SNRCHNR] = snrs.get(k)
													.getCSeriennrChargennr();
											oZeileSnr[REPORT_PROJEKTSTATISTIK_MENGE] = snrs.get(k).getNMenge();

											alDaten.add(oZeileSnr);
										}

									} else {
										alDaten.add(oZeile);
									}

								}

							} else {

								List<SeriennrChargennrMitMengeDto> snrs = belegDto.getBelegpositionenDtos().get(i)
										.getSeriennrChargennrMitMenge();

								if (snrs != null && snrs.size() > 0) {
									for (int j = 0; j < snrs.size(); j++) {
										Object[] oZeileSnr = oZeile.clone();

										oZeileSnr[REPORT_PROJEKTSTATISTIK_SNRCHNR] = snrs.get(j)
												.getCSeriennrChargennr();

										oZeileSnr[REPORT_PROJEKTSTATISTIK_MENGE] = snrs.get(j)
												.getNMengeInAbhaengigkeitVonPositionsmenge(bdPositionsmenge);

										alDaten.add(oZeileSnr);
									}

								} else {
									alDaten.add(oZeile);
								}
							}

						}

					}
				} else {
					alDaten.add(oZeileVorlage);
				}

			}

			// Sortieren nach Artikel + Snr + Datum

			for (int i = alDaten.size() - 1; i > 0; --i) {
				for (int j = 0; j < i; ++j) {
					Object[] a = (Object[]) alDaten.get(j);
					Object[] b = (Object[]) alDaten.get(j + 1);

					String artikelA = (String) a[ProjektReportFac.REPORT_PROJEKTSTATISTIK_ARTIKEL];
					String artikelB = (String) b[ProjektReportFac.REPORT_PROJEKTSTATISTIK_ARTIKEL];

					if (artikelA == null) {
						artikelA = "";
					}
					if (artikelB == null) {
						artikelB = "";
					}

					artikelA = Helper.fitString2Length(artikelA, 80, ' ');
					artikelB = Helper.fitString2Length(artikelB, 80, ' ');

					String snrA = (String) a[ProjektReportFac.REPORT_PROJEKTSTATISTIK_SNRCHNR];
					String snrB = (String) b[ProjektReportFac.REPORT_PROJEKTSTATISTIK_SNRCHNR];

					if (snrA == null) {
						snrA = "";
					}
					if (snrB == null) {
						snrB = "";
					}

					snrA = Helper.fitString2Length(snrA, 40, ' ');
					snrB = Helper.fitString2Length(snrB, 40, ' ');

					java.util.Date dateA = (java.util.Date) a[ProjektReportFac.REPORT_PROJEKTSTATISTIK_BELEGDATUM];
					java.util.Date dateB = (java.util.Date) b[ProjektReportFac.REPORT_PROJEKTSTATISTIK_BELEGDATUM];

					if (dateA == null) {
						dateA = new Date(0);
					}
					if (dateB == null) {
						dateB = new Date(0);
					}

					String sortStringA = artikelA + snrA
							+ Helper.fitString2LengthAlignRight(dateA.getTime() + "", 30, ' ');
					String sortStringB = artikelB + snrB
							+ Helper.fitString2LengthAlignRight(dateB.getTime() + "", 30, ' ');

					if (sortStringA.compareTo(sortStringB) > 0) {
						Object[] h = a;
						alDaten.set(j, b);
						alDaten.set(j + 1, h);
					}
				}
			}

			Object[][] returnArray = new Object[alDaten
					.size()][ProjektReportFac.REPORT_PROJEKTSTATISTIK_ANZAHL_SPALTEN];
			data = (Object[][]) alDaten.toArray(returnArray);

			initJRDS(parameter, ProjektReportFac.REPORT_MODUL, cAktuellerReport, theClientDto.getMandant(),
					theClientDto.getLocUi(), theClientDto);

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printProjektOffeneAuswahlListe(Integer bereichIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		JasperPrintLP oPrint = null;
		int iAnzahlZeilen = 0;
		cAktuellerReport = ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE;
		Locale locDruck;

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		session = factory.openSession();
		// Hiberante Criteria fuer alle Tabellen ausgehend von meiner
		// Haupttabelle anlegen,
		// nach denen ich filtern und sortieren kann

		Query query = session.createQuery(ProjektHandler.lastQuery);
		List<?> list = query.list();
		Iterator<?> it = list.iterator();
		while (it.hasNext()) {
			Object[] o = (Object[]) it.next();
			Integer projektIId = (Integer) o[0];
			session = factory.openSession();
			Criteria crit1 = session.createCriteria(FLRHistory.class);
			crit1.createCriteria(ProjektFac.FLR_HISTORY_FLRPROJEKT)
					.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_I_ID, projektIId));
			List<?> resultList = crit1.list();
			if (resultList.size() != 0) {
				iAnzahlZeilen = iAnzahlZeilen + resultList.size();
			}
			iAnzahlZeilen++;
		}

		HashMap<?, ?> hmStatusbilder = getLocaleFac().getAllStatiIcon();

		data = new Object[iAnzahlZeilen][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ANZAHL_SPALTEN];
		int i = 0;
		it = list.iterator();
		while (it.hasNext()) {
			Object[] o = (Object[]) it.next();
			Integer projektIId = (Integer) o[0];

			Session session3 = factory.openSession();
			Query query3 = session3.createQuery("from FLRProjekt projekt WHERE projekt.i_id=" + projektIId);
			List<?> list3 = query3.list();
			Iterator<?> it3 = list3.iterator();
			if (it3.hasNext()) {

				FLRProjekt projekt = (FLRProjekt) it3.next();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY] = Boolean.FALSE;
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PROJEKTTITEL] = projekt.getC_titel();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PROJEKTKATEGORIE] = projekt.getKategorie_c_nr();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_REALISIERUNGSTERMIN] = projekt
						.getT_realisierung();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PROJEKTCNR] = projekt.getC_nr();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_STATUS] = projekt.getStatus_c_nr();

				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_STATUS_BILD] = Helper
						.byteArrayToImage((byte[]) hmStatusbilder.get(projekt.getStatus_c_nr()));

				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_KUNDECNAME1] = projekt.getFlrpartner()
						.getC_name1nachnamefirmazeile1();
				if (projekt.getFlrpartner().getFlrlandplzort() != null) {
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_LKZ] = projekt.getFlrpartner()
							.getFlrlandplzort().getFlrland().getC_lkz();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PLZ] = projekt.getFlrpartner()
							.getFlrlandplzort().getC_plz();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ORT] = projekt.getFlrpartner()
							.getFlrlandplzort().getFlrort().getC_name();
				}

				if (projekt.getFlrpartner().getFlrpartnerklasse() != null) {
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PARTNERKLASSE] = projekt.getFlrpartner()
							.getFlrpartnerklasse().getC_nr();
				}

				if (projekt.getPartner_i_id_betreiber() != null) {
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_BETREIBER] = HelperServer
							.formatNameAusFLRPartner(projekt.getFlrpartnerbetreiber());
				}

				if (projekt.getFlrpartner().getFlrbranche() != null) {
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_BRANCHE] = projekt.getFlrpartner()
							.getFlrbranche().getC_nr();
				}

				// locDruck = Helper.string2Locale(projekt.getFlrpartner()
				// .getLocale_c_nr_kommunikation());

				// SP6037
				locDruck = theClientDto.getLocUi();

				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ZIELTERMIN] = projekt.getT_zielwunschdatum();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_BELEGDATUM] = projekt.getT_anlegen();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_TYP] = projekt.getTyp_c_nr();

				Calendar calendar = new GregorianCalendar();
				calendar.setTime(new Date(projekt.getT_zielwunschdatum().getTime()));
				int KW = calendar.get(Calendar.WEEK_OF_YEAR); // Kalendarwochen

				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ZIELWOCHE] = "" + KW;

				// Gesamte Dauer eines Projektes
				Double ddArbeitszeitist = getZeiterfassungFac().getSummeZeitenEinesBeleges(LocaleFac.BELEGART_PROJEKT,
						projekt.getI_id(), null, null, null, null, theClientDto);
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_GESAMTDAUER] = ddArbeitszeitist;

				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PRIO] = projekt.getI_prio();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_I_SORT_QUEUE] = projekt.getI_sort();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_TEXT] = projekt.getX_freetext();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ERZEUGER] = HelperServer
						.formatPersonAusFLRPartner(projekt.getFlrpersonalErzeuger().getFlrpartner());

				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_SUBREPORT_VATERPROJEKTE] = getSubreportVerknuepfteProjekteDerNaechstenEbene(
						projekt.getI_id(), true, theClientDto);
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_SUBREPORT_KINDPROJEKTE] = getSubreportVerknuepfteProjekteDerNaechstenEbene(
						projekt.getI_id(), false, theClientDto);

				if (projekt.getPersonal_i_id_internerledigt() != null) {
					PersonalDto personalDto = getPersonalFac()
							.personalFindByPrimaryKey(projekt.getPersonal_i_id_internerledigt(), theClientDto);
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_INTERNERLEDIGT_PERSON] = personalDto
							.getPartnerDto().formatAnrede();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_INTERNERLEDIGT_ZEIT] = projekt
							.getT_internerledigt();
				}

				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ZUGEWIESENER] = HelperServer
						.formatPersonAusFLRPartner(projekt.getFlrpersonalZugewiesener().getFlrpartner());

				// PJ21294
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_WIRD_DURCHGEFUEHRT_VON] = HelperServer
						.formatPersonAusFLRPartner(projekt.getFlrpersonalZugewiesener().getFlrpartner());

				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ZEIT] = projekt.getT_zeit();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_WAHRSCHEINLICHKEIT] = projekt
						.getI_wahrscheinlichkeit();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_GEPLANTERUMSATZ] = projekt.getN_umsatzgeplant();

				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_DAUER] = projekt.getD_dauer();
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_VERRECHENBAR] = getProjektServiceFac()
						.getTextVerrechenbar(projekt.getI_verrechenbar(), theClientDto);
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_B_FREIGEGEBEN] = Helper
						.short2Boolean(projekt.getB_freigegeben());
				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_DATEINAME] = projekt.getC_dateiname();

				if (projekt.getFlrvkfortschritt() != null) {
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_VKFORTSCHRITT] = projekt
							.getFlrvkfortschritt().getC_nr();

					Iterator itVK = projekt.getFlrvkfortschritt().getVkfortschrittspr_set().iterator();
					while (itVK.hasNext()) {
						FLRVkfortschrittspr spr = (FLRVkfortschrittspr) itVK.next();
						if (spr.getLocale().getC_nr().equals(theClientDto.getLocUiAsString())) {
							data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_VKFORTSCHRITT_BEZEICHNUNG] = spr
									.getC_bez();
						}

					}
					if (projekt.getFlrvkfortschritt().getFlrleadstatus() != null) {
						data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_VKFORTSCHRITT_LEADSTATUS] = projekt
								.getFlrvkfortschritt().getFlrleadstatus().getC_bez();
					}

				}

				// try {
				if (projekt.getFlransprechpartner() != null && (projekt.getFlransprechpartner().getI_id() != null)) {
					if (projekt.getFlransprechpartner().getFlrpartneransprechpartner() != null
							&& projekt.getFlransprechpartner().getFlrpartneransprechpartner().getI_id() != null) {
						data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ANSPRECHPARTNERCNAME1] = projekt
								.getFlransprechpartner().getFlrpartneransprechpartner().getC_name1nachnamefirmazeile1();
						data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ANSPRECHPARTNER] = (projekt
								.getFlransprechpartner().getFlrpartneransprechpartner().getC_name1nachnamefirmazeile1()
								+ " "
								+ (projekt.getFlransprechpartner().getFlrpartneransprechpartner()
										.getC_name2vornamefirmazeile2() == null
												? ""
												: projekt.getFlransprechpartner().getFlrpartneransprechpartner()
														.getC_name2vornamefirmazeile2())
								+ " "
								+ (projekt.getFlransprechpartner().getFlrpartneransprechpartner()
										.getC_name3vorname2abteilung() == null ? ""
												: projekt.getFlransprechpartner().getFlrpartneransprechpartner()
														.getC_name3vorname2abteilung())).trim();
					}
				}
				//
				Criteria critHistory = session.createCriteria(FLRHistory.class);
				critHistory.createCriteria(ProjektFac.FLR_HISTORY_FLRPROJEKT)
						.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_I_ID, projekt.getI_id()));
				List<?> rlHistory = critHistory.list();
				Iterator<?> itHistory = rlHistory.iterator();

				java.util.Date juengstesAenderungsdatum = null;
				while (itHistory.hasNext()) {
					FLRHistory history = (FLRHistory) itHistory.next();

					if (juengstesAenderungsdatum == null) {
						juengstesAenderungsdatum = history.getT_aendern();
					} else if (juengstesAenderungsdatum != null
							&& juengstesAenderungsdatum.before(history.getT_aendern())) {
						juengstesAenderungsdatum = history.getT_aendern();
					}

				}

				data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_JUENGSTES_AENDERUNGSDATUM] = juengstesAenderungsdatum;
				i++;

				itHistory = rlHistory.iterator();
				while (itHistory.hasNext()) {
					FLRHistory history = (FLRHistory) itHistory.next();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY] = Boolean.TRUE;
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PROJEKTCNR] = projekt.getC_nr();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ZUGEWIESENER] = HelperServer
							.formatPersonAusFLRPartner(projekt.getFlrpersonalZugewiesener().getFlrpartner());
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_MITARBEITER] = HelperServer
							.formatPersonAusFLRPartner(history.getFlrpersonal().getFlrpartner());
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_BELEGDATUM] = history
							.getT_belegdatum();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_AENDERUNGSDATUM] = history
							.getT_aendern();
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_TEXT] = stripHtmlFontFamilyIfHtml(
							history.getX_text(), history.getB_html());
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_HTML] = Helper
							.short2Boolean(history.getB_html());
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_TITEL] = history.getC_titel();
					if (history.getFlrhistoryart() != null) {
						data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_HISTORYART] = history
								.getFlrhistoryart().getC_bez();
					}

					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_ERLEDIGUNGSGRAD] = history
							.getF_erledigungsgrad();
					if (history.getFlrpersonal_wirddurchgefuehrtvon() != null) {
						data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_WIRD_DURCHGEFUEHRT_VON] = HelperServer
								.formatPersonAusFLRPartner(
										history.getFlrpersonal_wirddurchgefuehrtvon().getFlrpartner());
					}
					data[i][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_DAUER_GEPLANT] = history
							.getN_dauer_geplant();

					i++;
				}

			}
			session3.close();

		}

		// die Parameter dem Report uebergeben
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		if (bereichIId != null) {
			parameter.put("P_BEREICH", getProjektServiceFac().bereichFindByPrimaryKey(bereichIId).getCBez());
		}

		parameter.put("P_TITLE",
				getTextRespectUISpr("proj.print.offene", theClientDto.getMandant(), theClientDto.getLocUi()));

		initJRDS(parameter, ProjektReportFac.REPORT_MODUL, cAktuellerReport, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		oPrint = getReportPrint();

		return oPrint;
	}

	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;
		String fieldName = field.getName();
		if (cAktuellerReport.equals(ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE)) {
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
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_BELEGDATUM];
			} else if ("F_ERLEDIGUNGSDATUM".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ERLEDIGUNGSDATUM];
			} else if ("F_KATEGORIE".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PROJEKTKATEGORIE];
			} else if ("F_TITEL".equals(fieldName)) {
				// value = Helper
				// .formatStyledTextForJasper(data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PROJEKTTITEL]);
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PROJEKTTITEL];
			} else if ("F_PRIO".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_PRIO];
			} else if ("F_I_SORT_QUEUE".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_I_SORT_QUEUE];
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
			} else if ("F_AENDERUNGSDATUM".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_AENDERUNGSDATUM];
			} else if ("F_WAS".equals(fieldName)) {
				value = Helper.formatStyledTextForJasper(
						data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_TEXT]);
			} else if ("F_HTML".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_HTML];
			} else if ("F_HISTORY_HISTORYART".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_HISTORYART];
			} else if ("F_HISTORY_TITEL".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_TITEL];
			} else if ("F_ANSPRECHPARTNER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_ANSPRECHPARTNER];
			} else if ("F_DATEINAME".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_DATEINAME];
			} else if ("F_FREIGEGEBEN".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_B_FREIGEGEBEN];
			} else if ("F_VERRECHENBAR".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_VERRECHENBAR];
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
			} else if ("F_BETREIBER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_BETREIBER];
			} else if ("F_SUBREPORT_KINDPROJEKTE".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_SUBREPORT_KINDPROJEKTE];
			} else if ("F_SUBREPORT_VATERPROJEKTE".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_SUBREPORT_VATERPROJEKTE];
			} else if ("F_VKFORTSCHRITT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_VKFORTSCHRITT];
			} else if ("F_VKFORTSCHRITT_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_VKFORTSCHRITT_BEZEICHNUNG];
			} else if ("F_VKFORTSCHRITT_LEADSTATUS".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_VKFORTSCHRITT_LEADSTATUS];
			} else if ("F_REALISIERUNGSTERMIN".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_REALISIERUNGSTERMIN];
			} else if ("F_JUENGSTES_AENDERUNGSDATUM".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_JUENGSTES_AENDERUNGSDATUM];
			} else if ("F_ERLEDIGUNGSGRAD".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_ERLEDIGUNGSGRAD];
			} else if ("F_WIRD_DURCHGEFUEHRT_VON".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_WIRD_DURCHGEFUEHRT_VON];
			} else if ("F_STATUS_BILD".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_STATUS_BILD];
			} else if ("F_HISTORY_DAUER_GEPLANT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_DAUER_GEPLANT];
			} else if ("F_HISTORY".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY];
			}

		} else if (cAktuellerReport.equals(ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST)) {
			if ("F_CNR".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_PROJEKTCNR];
			} else if ("F_STATUS".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_STATUS];
			} else if ("F_PARTNER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_KUNDECNAME1];
			} else if ("F_ORT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_ORT];
			} else if ("F_BRANCHE".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_BRANCHE];
			} else if ("F_PARTNERKLASSE".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_PARTNERKLASSE];
			} else if ("F_LKZ".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_LKZ];
			} else if ("F_PLZ".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_PLZ];
			} else if ("F_TYP".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_TYP];
			} else if ("F_ZIELTERMIN".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_ZIELTERMIN];
			} else if ("F_BELEGDATUM".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_BELEGDATUM];
			} else if ("F_ERLEDIGUNGSDATUM".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_ERLEDIGUNGSDATUM];
			} else if ("F_KATEGORIE".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_PROJEKTKATEGORIE];
			} else if ("F_TITEL".equals(fieldName)) {
				// value = Helper
				// .formatStyledTextForJasper(data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_PROJEKTTITEL]);
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_PROJEKTTITEL];
			} else if ("F_PRIO".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_PRIO];
			} else if ("F_I_SORT_QUEUE".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_I_SORT_QUEUE];
			} else if ("F_ERZEUGER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_ERZEUGER];
			} else if ("F_DAUER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_DAUER];
			} else if ("F_ZEIT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_ZEIT];
			} else if ("F_ZUGEWIESENER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_ZUGEWIESENER];
			} else if ("F_ZIELWOCHE".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_ZIELWOCHE];
			} else if ("F_TEXT".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_TEXT]);
			} else if ("F_WER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_HISTORY_MITARBEITER];
			} else if ("F_WANN".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_HISTORY_BELEGDATUM];
			} else if ("F_AENDERUNGSDATUM".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_HISTORY_AENDERUNGSDATUM];
			} else if ("F_WAS".equals(fieldName)) {
				value = Helper.formatStyledTextForJasper(
						data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_HISTORY_TEXT]);
			} else if ("F_HTML".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_HISTORY_HTML];
			} else if ("F_ANSPRECHPARTNER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_ANSPRECHPARTNER];
			} else if ("F_DATEINAME".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_DATEINAME];
			} else if ("F_FREIGEGEBEN".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_B_FREIGEGEBEN];
			} else if ("F_VERRECHENBAR".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_VERRECHENBAR];
			} else if ("F_ANSPRECHPARTNERCNAME1".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_ANSPRECHPARTNERCNAME1];
			} else if ("F_WAHRSCHEINLICHKEIT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_WAHRSCHEINLICHKEIT];
			} else if ("F_UMSATZGEPLANT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_GEPLANTERUMSATZ];
			} else if ("F_GESAMTDAUER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_GESAMTDAUER];
			} else if ("F_INTERNERLEDIGT_PERSON".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_INTERNERLEDIGT_PERSON];
			} else if ("F_INTERNERLEDIGT_ZEIT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_INTERNERLEDIGT_ZEIT];
			} else if ("F_BETREIBER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_BETREIBER];
			} else if ("F_SUBREPORT_KINDPROJEKTE".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_SUBREPORT_KINDPROJEKTE];
			} else if ("F_SUBREPORT_VATERPROJEKTE".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_SUBREPORT_VATERPROJEKTE];
			} else if ("F_VKFORTSCHRITT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_VKFORTSCHRITT];
			} else if ("F_VKFORTSCHRITT_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_VKFORTSCHRITT_BEZEICHNUNG];
			} else if ("F_VKFORTSCHRITT_LEADSTATUS".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_VKFORTSCHRITT_LEADSTATUS];
			} else if ("F_REALISIERUNG".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_REALISIERUNG];
			} else if ("F_WERT_AUFTRAG".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_WERT_AUFTRAG];
			} else if ("F_WERT_GUTSCHRIFT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_WERT_GUTSCHRIFT];
			} else if ("F_WERT_RECHNUNG".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_FORECAST_WERT_RECHNUNG];
			}

		} else if (cAktuellerReport.equals(REPORT_GANZSEITIGESBILD)) {
			if ("F_BILD".equals(fieldName)) {
				value = data[index][0];
			}
		} else if (cAktuellerReport.equals(REPORT_PROJEKTE_EINES_ARTIKELS)) {
			if ("Belegnummer".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTE_EINES_ARTIKELS_BELEGNUMMER];
			} else if ("Bereich".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTE_EINES_ARTIKELS_BEREICH];
			} else if ("Kategorie".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTE_EINES_ARTIKELS_KATEGORIE];
			} else if ("Partner".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTE_EINES_ARTIKELS_PARTNER];
			} else if ("PersonZugeordnet".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTE_EINES_ARTIKELS_PERSON_ZUGEORDNET];
			} else if ("Status".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTE_EINES_ARTIKELS_STATUS];
			} else if ("Datum".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTE_EINES_ARTIKELS_T_ANLEGEN];
			} else if ("Typ".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTE_EINES_ARTIKELS_TYP];
			} else if ("Titel".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTE_EINES_ARTIKELS_TITEL];
			} else if ("PROJEKT_I_ID".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTE_EINES_ARTIKELS_PROJEKT_I_ID];
			}
		} else if (cAktuellerReport.equals(ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE)) {
			if ("F_CNR".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_PROJEKTCNR];
			} else if ("F_PARTNER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_KUNDECNAME1];
			} else if ("F_ZIELTERMIN".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_ZIELTERMIN];
			} else if ("F_BELEGDATUM".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_BELEGDATUM];
			} else if ("F_ERLEDIGUNGSDATUM".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_ERLEDIGUNGSDATUM];
			} else if ("F_KATEGORIE".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_PROJEKTKATEGORIE];
			} else if ("F_TITEL".equals(fieldName)) {
				// value = Helper
				// .formatStyledTextForJasper(data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_PROJEKTTITEL]);
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_PROJEKTTITEL];
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
			} else if ("F_ISTZEIT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_ISTZEIT];
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
			} else if ("F_DAUER_GEPLANT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_HISTORY_DAUER_GEPLANT];
			} else if ("F_WAS".equals(fieldName)) {
				value = Helper.formatStyledTextForJasper(
						data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_HISTORY_TEXT]);
			} else if ("F_HTML".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_HISTORY_HTML];
			} else if ("F_WAHRSCHEINLICHKEIT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_WAHRSCHEINLICHKEIT];
			} else if ("F_UMSATZGEPLANT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_GEPLANTERUMSATZ];
			} else if ("F_INTERNERLEDIGT_PERSON".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_INTERNERLEDIGT_PERSON];
			} else if ("F_INTERNERLEDIGT_ZEIT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_INTERNERLEDIGT_ZEIT];
			} else if ("F_BETREIBER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_BETREIBER];
			} else if ("F_VKFORTSCHRITT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_VKFORTSCHRITT];
			} else if ("F_VKFORTSCHRITT_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_VKFORTSCHRITT_BEZEICHNUNG];
			} else if ("F_VKFORTSCHRITT_LEADSTATUS".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_VKFORTSCHRITT_LEADSTATUS];
			} else if ("F_ERLEDIGUNGSGRAD".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_ERLEDIGUNGSGRAD];
			} else if ("F_WIRD_DURCHGEFUEHRT_VON".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_WIRD_DURCHGEFUEHRT_VON];
			}

		} else if (cAktuellerReport.equals(ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLLIERT)) {
			if ("F_CNR".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_PROJEKTCNR];
			} else if ("F_PARTNER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_KUNDECNAME1];
			} else if ("F_ZIELTERMIN".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_ZIELTERMIN];
			} else if ("F_BELEGDATUM".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_BELEGDATUM];
			} else if ("F_ERLEDIGUNGSDATUM".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_ERLEDIGUNGSDATUM];
			} else if ("F_KATEGORIE".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_PROJEKTKATEGORIE];
			} else if ("F_TITEL".equals(fieldName)) {
				// value = Helper
				// .formatStyledTextForJasper(data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_PROJEKTTITEL]);
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_PROJEKTTITEL];
			} else if ("F_STATUS".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_STATUS];
			} else if ("F_TYP".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_TYP];
			} else if ("F_PRIO".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_PRIO];
			} else if ("F_ERZEUGER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_ERZEUGER];
			} else if ("F_DAUER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_DAUER];
			} else if ("F_ZEIT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_ZEIT];
			} else if ("F_ZUGEWIESENER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_ZUGEWIESENER];
			} else if ("F_ZIELWOCHE".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_ZIELWOCHE];
			} else if ("F_TEXT".equals(fieldName)) {
				value = Helper.formatStyledTextForJasper(
						data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_TEXT]);
			} else if ("F_WAHRSCHEINLICHKEIT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_WAHRSCHEINLICHKEIT];
			} else if ("F_UMSATZGEPLANT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_GEPLANTERUMSATZ];
			} else if ("F_INTERNERLEDIGT_PERSON".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_INTERNERLEDIGT_PERSON];
			} else if ("F_INTERNERLEDIGT_ZEIT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_INTERNERLEDIGT_ZEIT];
			} else if ("F_BETREIBER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_BETREIBER];
			} else if ("F_VKFORTSCHRITT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_VKFORTSCHRITT];
			} else if ("F_VKFORTSCHRITT_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_VKFORTSCHRITT_BEZEICHNUNG];
			} else if ("F_VKFORTSCHRITT_LEADSTATUS".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_VKFORTSCHRITT_LEADSTATUS];
			} else if ("F_SUBREPORT_HISTORY".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_SUBREPORT_HISTORY];
			} else if ("F_SUBREPORT_ZUGEHOERIGE_BELEGE".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_SUBREPORT_ZUGEHOERIGE_BELEGE];
			}

		} else if (cAktuellerReport.equals(ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT)) {
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
			} else if ("F_ERLEDIGUNGSGRUND".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ERLEDIGUNGSGRUND];
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
				value = Helper.formatStyledTextForJasper(
						data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_HISTORY_TEXT]);
			} else if ("F_HTML".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_HISTORY_HTML];
			} else if ("F_VERRECHENBAR".equals(fieldName)) {
				value = Helper.formatStyledTextForJasper(
						data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_B_VERRECHENBAR]);
			} else if ("F_FREIGEGEBEN".equals(fieldName)) {
				value = Helper.formatStyledTextForJasper(
						data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_FREIGEGEBEN]);
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
			} else if ("F_BETREIBER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_BETREIBER];
			} else if ("F_VKFORTSCHRITT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_VKFORTSCHRITT];
			} else if ("F_VKFORTSCHRITT_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_VKFORTSCHRITT_BEZEICHNUNG];
			} else if ("F_VKFORTSCHRITT_LEADSTATUS".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_VKFORTSCHRITT_LEADSTATUS];
			} else if ("F_ERLEDIGUNGSGRAD".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_HISTORY_ERLEDIGUNGSGRAD];
			} else if ("F_DAUER_GEPLANT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_HISTORY_DAUER_GEPLANT];
			} else if ("F_WIRD_DURCHGEFUEHRT_VON".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_HISTORY_WIRD_DURCHGEFUEHRT_VON];
			}

		}

		else if (cAktuellerReport.equals(ProjektReportFac.REPORT_PROJEKT)) {
			if ("F_POSITION".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_POSITION];
			} else if ("F_TEXT".equals(fieldName)) {
				value = Helper.formatStyledTextForJasper(data[index][ProjektReportFac.REPORT_PROJEKT_HISTORY_TEXT]);
			} else if ("F_ERZEUGER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_ERZEUGER];
			} else if ("F_DATUM".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_HISTORY_BELEGDATUM];
			} else if ("F_HTML".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_HISTORY_HTML];
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

			else if ("F_AENDERUNGSZEITPUNKT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_T_AENDERN];
			} else if ("F_PERSON_AENDERN".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_PERSON_AENDERN];
			} else if ("F_KURZZEICHEN_PERSON_AENDERN".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_KURZZEICHEN_PERSON_AENDERN];
			} else if ("F_ERLEDIGUNGSGRAD".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_HISTORY_ERLEDIGUNGSGRAD];
			} else if ("F_DURCHGEFUEHRT_VON".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_HISTORY_DURCHGEFUEHRT_VON];
			} else if ("F_DAUER_GEPLANT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_HISTORY_DAUER_GEPLANT];
			} else if ("F_DATUM_AS_TIMESTAMP".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKT_HISTORY_BELEGDATUM_AS_TIMESTAMP];
			}

		} else if (cAktuellerReport.equals(ProjektReportFac.REPORT_PROJEKT_JOURNAL_AKTIVITAETSUEBERSICHT)) {
			if ("F_BELEGDETAIL".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEG_SUBREPORT];
			} else if ("F_BELEGART".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGART];
			} else if ("F_BELEGNUMMER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGNUMMER];
				myLogger.warn("F_BELEGNUMMER=" + (String) value);
			} else if ("F_BELEGSTATUS".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BELEGSTATUS];
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
			} else if ("F_PROJEKT_BETREIBER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_BETREIBER];
			} else if ("F_PROJEKT_PERSONAL_MITARBEITER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_PERSONAL_MITARBEITER];
			} else if ("F_ZIELWUNSCHTERMIN".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_ZIELWUNSCHTERMIN];
			} else if ("F_VON".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_VON];
			} else if ("F_BIS".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_BIS];
			} else if ("F_PROJEKT_INTERNERLEDIGT_PERSON".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_INTERNERLEDIGT_PERSON];
			} else if ("F_PROJEKT_INTERNERLEDIGT_ZEIT".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_INTERNERLEDIGT_ZEIT];
			}
		} else if (cAktuellerReport.equals(ProjektReportFac.REPORT_PROJEKTVERLAUF)) {
			if ("F_BELEGART".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_BELEGART];
			} else if ("F_BELEGNUMMER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_BELEGNUMMER];
			} else if ("F_MASCH_DAUER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_MASCH_DAUER];
			} else if ("F_MASCH_KOSTEN".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_MASCH_KOSTEN];
			} else if ("F_MASCH_VKPREIS".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_MASCH_VKPREIS];
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
			} else if ("F_EINSTANDSWERT_MATERIAL".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_EINSTANDSWERT_MATERIAL];
			} else if ("F_EINSTANDSWERT_HANDEINGABEN".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_EINSTANDSWERT_HANDEINGABEN];
			} else if ("F_LIEF1WERT_MATERIAL".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_LIEF1WERT_MATERIAL];
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
			} else if ("F_BESTELLUNG_VERRECHNET_MIT_ER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_BESTELLUNG_VERRECHNET_MIT_ER];
			} else if ("F_STATUS".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_STATUS];
			} else if ("F_AUFTRAG_BESTELLNUMMER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_AUFTRAG_BESTELLNUMMER];
			} else if ("F_LIEFERTERMIN".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_LIEFERTERMIN];
			} else if ("F_LOS_STKL_NR".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_LOS_STKL_NR];
			} else if ("F_LOS_STKL_BEZ".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_LOS_STKL_BEZ];
			} else if ("F_LOS_STKL_ZBEZ".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_LOS_STKL_ZBEZ];
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
			} else if ("F_LOSANTEIL_LIEFERSCHEIN_EINSTANDSWERT_MATERIAL".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_LOSANTEIL_LIEFERSCHEIN_EINSTANDSWERT_MATERIAL];
			} else if ("F_LOSANTEIL_LIEFERSCHEIN_EINSTANDSWERT_AZ".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_LOSANTEIL_LIEFERSCHEIN_EINSTANDSWERT_AZ];
			}

			else if ("F_AZ_ARTIKELNUMMER".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_AZ_ARTIKELNUMMER];
			} else if ("F_AZ_ARTIKELBEZEICHNUNG".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_AZ_ARTIKELBEZEICHNUNG];
			} else if ("F_AZ_ARTIKELGRUPPE".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_AZ_ARTIKELGRUPPE];
			} else if ("F_AZ_VKPREIS".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTVERLAUF_AZ_VKPREIS];
			}

		} else if (cAktuellerReport.equals(ProjektReportFac.REPORT_PROJEKTZEITDATEN)) {
			if ("Belegart".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTZEITEN_BELEGART];
			} else if ("Belegnummer".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTZEITEN_BELEG];
			} else if ("Ebene".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTZEITEN_EBENE];
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
			} else if ("ZEITDATEN_I_ID".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTZEITEN_ZEITDATEN_I_ID];
			}

		} else if (cAktuellerReport.equals(ProjektReportFac.REPORT_PROJEKTBAUM)) {
			if ("Ebene".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTBAUM_EBENE];
			} else if ("Projektnummer".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTBAUM_PROJEKTNUMMER];
			} else if ("Status".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTBAUM_STATUS];
			} else if ("Titel".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTBAUM_TITEL];
			} else if ("Kunde".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTBAUM_KUNDE];
			} else if ("Kategorie".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTBAUM_KATEGORIE];
			} else if ("Typ".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTBAUM_TYP];
			} else if ("Termin".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTBAUM_TERMIN];
			} else if ("Prio".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTBAUM_PRIO];
			}
		} else if (cAktuellerReport.equals(ProjektReportFac.REPORT_PROJEKTSTATISTIK)) {
			if ("Artikel".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTSTATISTIK_ARTIKEL];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTSTATISTIK_ARTIKELBEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTSTATISTIK_ARTIKELZUSATZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung2".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTSTATISTIK_ARTIKELZUSATZBEZEICHNUNG2];
			} else if ("Belegart".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTSTATISTIK_BELEGART];
			} else if ("Belegdatum".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTSTATISTIK_BELEGDATUM];
			} else if ("Belegnummer".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTSTATISTIK_BELEGNUMMER];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTSTATISTIK_MENGE];
			} else if ("Partner".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTSTATISTIK_PARTNER];
			} else if ("SnrChnr".equals(fieldName)) {
				value = data[index][ProjektReportFac.REPORT_PROJEKTSTATISTIK_SNRCHNR];
			}
		} else if (cAktuellerReport.equals(ProjektReportFac.REPORT_AENDERUNGEN_EIGENSCHAFTEN)) {
			if ("Operation".equals(fieldName)) {
				value = data[index][REPORT_AENDERUNGEN_EIGENSCHAFTEN_OPERATION];
			} else if ("Id".equals(fieldName)) {
				value = data[index][REPORT_AENDERUNGEN_EIGENSCHAFTEN_I_ID];
			} else if ("Key".equals(fieldName)) {
				value = data[index][REPORT_AENDERUNGEN_EIGENSCHAFTEN_KEY];
			} else if ("Von".equals(fieldName)) {
				value = data[index][REPORT_AENDERUNGEN_EIGENSCHAFTEN_VON];
			} else if ("Nach".equals(fieldName)) {
				value = data[index][REPORT_AENDERUNGEN_EIGENSCHAFTEN_NACH];
			} else if ("Locale".equals(fieldName)) {
				value = data[index][REPORT_AENDERUNGEN_EIGENSCHAFTEN_LOCALE];
			} else if ("Person".equals(fieldName)) {
				value = data[index][REPORT_AENDERUNGEN_EIGENSCHAFTEN_PERSON];
			} else if ("Zeitpunkt".equals(fieldName)) {
				value = data[index][REPORT_AENDERUNGEN_EIGENSCHAFTEN_ZEITPUNKT];
			}
		}

		return value;
	}

	public boolean next() throws JRException {
		index++;
		return (index < data.length);
	}

	private String buildSortierungProjektAlle(ReportJournalKriterienDto reportJournalKriterienDtoI,
			TheClientDto theClientDto) {

		StringBuffer buff = new StringBuffer(
				getTextRespectUISpr("lp.sortierungnach", theClientDto.getMandant(), theClientDto.getLocUi()));
		buff.append(" ");

		if (reportJournalKriterienDtoI.bSortiereNachKostenstelle) {
			buff.append(getTextRespectUISpr("lp.kostenstelle", theClientDto.getMandant(), theClientDto.getLocUi()))
					.append(", ");
		}

		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
			buff.append(getTextRespectUISpr("lp.kunde", theClientDto.getMandant(), theClientDto.getLocUi()))
					.append(", ");
		}

		buff.append(getTextRespectUISpr("auft.auftragsnummer", theClientDto.getMandant(), theClientDto.getLocUi()));

		return buff.toString();
	}

	private String buildFilterProjektAlle(ReportJournalKriterienDto reportJournalKriterienDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		StringBuffer buff = new StringBuffer("");

		// Belegdatum
		if (reportJournalKriterienDtoI.dVon != null || reportJournalKriterienDtoI.dBis != null) {
			buff.append(getTextRespectUISpr("bes.belegdatum", theClientDto.getMandant(), theClientDto.getLocUi()));
		}

		if (reportJournalKriterienDtoI.dVon != null) {
			buff.append(" ").append(getTextRespectUISpr("lp.von", theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ").append(Helper.formatDatum(reportJournalKriterienDtoI.dVon, theClientDto.getLocUi()));
		}

		if (reportJournalKriterienDtoI.dBis != null) {
			buff.append(" ").append(getTextRespectUISpr("lp.bis", theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ").append(Helper.formatDatum(reportJournalKriterienDtoI.dBis, theClientDto.getLocUi()));
		}

		// Kostenstelle
		if (reportJournalKriterienDtoI.kostenstelleIId != null) {
			buff.append(" ")
					.append(getTextRespectUISpr("lp.kostenstelle", theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ").append(
					getSystemFac().kostenstelleFindByPrimaryKey(reportJournalKriterienDtoI.kostenstelleIId).getCNr());
		}

		// Kunde
		if (reportJournalKriterienDtoI.kundeIId != null) {
			buff.append(" ")
					.append(getTextRespectUISpr("lp.kunde", theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ")
					.append(getKundeFac().kundeFindByPrimaryKey(reportJournalKriterienDtoI.kundeIId, theClientDto)
							.getPartnerDto().getCName1nachnamefirmazeile1());
		}

		// Auftragsnummer
		if (reportJournalKriterienDtoI.sBelegnummerVon != null || reportJournalKriterienDtoI.sBelegnummerBis != null) {
			buff.append(" ").append(
					getTextRespectUISpr("auft.auftragsnummer", theClientDto.getMandant(), theClientDto.getLocUi()));
		}

		if (reportJournalKriterienDtoI.sBelegnummerVon != null) {
			buff.append(" ").append(getTextRespectUISpr("lp.von", theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ").append(reportJournalKriterienDtoI.sBelegnummerVon);
		}

		if (reportJournalKriterienDtoI.sBelegnummerBis != null) {
			buff.append(" ").append(getTextRespectUISpr("lp.bis", theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ").append(reportJournalKriterienDtoI.sBelegnummerBis);
		}

		String cBuffer = buff.toString().trim();

		return cBuffer;
	}

	private String buildFilterProjektErledigt(ReportJournalKriterienDto reportJournalKriterienDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		StringBuffer buff = new StringBuffer("");

		// Belegdatum
		if (reportJournalKriterienDtoI.dVon != null || reportJournalKriterienDtoI.dBis != null) {
			buff.append(getTextRespectUISpr("bes.belegdatum", theClientDto.getMandant(), theClientDto.getLocUi()));
		}

		if (reportJournalKriterienDtoI.dVon != null) {
			buff.append(" ").append(getTextRespectUISpr("lp.von", theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ").append(Helper.formatDatum(reportJournalKriterienDtoI.dVon, theClientDto.getLocUi()));
		}

		if (reportJournalKriterienDtoI.dBis != null) {
			buff.append(" ").append(getTextRespectUISpr("lp.bis", theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ").append(Helper.formatDatum(reportJournalKriterienDtoI.dBis, theClientDto.getLocUi()));
		}

		// Kostenstelle
		if (reportJournalKriterienDtoI.kostenstelleIId != null) {
			buff.append(" ")
					.append(getTextRespectUISpr("lp.kostenstelle", theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ").append(
					getSystemFac().kostenstelleFindByPrimaryKey(reportJournalKriterienDtoI.kostenstelleIId).getCNr());
		}

		// Kunde
		if (reportJournalKriterienDtoI.kundeIId != null) {
			buff.append(" ")
					.append(getTextRespectUISpr("lp.kunde", theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ")
					.append(getKundeFac().kundeFindByPrimaryKey(reportJournalKriterienDtoI.kundeIId, theClientDto)
							.getPartnerDto().getCName1nachnamefirmazeile1());
		}

		// Auftragsnummer
		if (reportJournalKriterienDtoI.sBelegnummerVon != null || reportJournalKriterienDtoI.sBelegnummerBis != null) {
			buff.append(" ").append(
					getTextRespectUISpr("auft.auftragsnummer", theClientDto.getMandant(), theClientDto.getLocUi()));
		}

		if (reportJournalKriterienDtoI.sBelegnummerVon != null) {
			buff.append(" ").append(getTextRespectUISpr("lp.von", theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ").append(reportJournalKriterienDtoI.sBelegnummerVon);
		}

		if (reportJournalKriterienDtoI.sBelegnummerBis != null) {
			buff.append(" ").append(getTextRespectUISpr("lp.bis", theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ").append(reportJournalKriterienDtoI.sBelegnummerBis);
		}

		String cBuffer = buff.toString().trim();

		return cBuffer;
	}

	private String buildSortierungProjektOffene(ReportJournalKriterienDto reportJournalKriterienDtoI,
			TheClientDto theClientDto) {

		StringBuffer buff = new StringBuffer(
				getTextRespectUISpr("lp.sortierungnach", theClientDto.getMandant(), theClientDto.getLocUi()));
		buff.append(" ");

		if (reportJournalKriterienDtoI.bSortiereNachKostenstelle) {
			buff.append(getTextRespectUISpr("lp.kostenstelle", theClientDto.getMandant(), theClientDto.getLocUi()))
					.append(", ");
		}

		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
			buff.append(getTextRespectUISpr("proj.sort.partner.typ.prio.modul", theClientDto.getMandant(),
					theClientDto.getLocUi())).append(", ");
		}

		buff.append(getTextRespectUISpr("lp.belegnummer", theClientDto.getMandant(), theClientDto.getLocUi()));

		return buff.toString();
	}

	private String buildFilterProjektOffene(ReportJournalKriterienDto reportJournalKriterienDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		StringBuffer buff = new StringBuffer("");

		// Belegdatum
		if (reportJournalKriterienDtoI.dVon != null || reportJournalKriterienDtoI.dBis != null) {
			buff.append(getTextRespectUISpr("bes.belegdatum", theClientDto.getMandant(), theClientDto.getLocUi()));
		}

		if (reportJournalKriterienDtoI.dVon != null) {
			buff.append(" ").append(getTextRespectUISpr("lp.von", theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ").append(Helper.formatDatum(reportJournalKriterienDtoI.dVon, theClientDto.getLocUi()));
		}

		if (reportJournalKriterienDtoI.dBis != null) {
			buff.append(" ").append(getTextRespectUISpr("lp.bis", theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ").append(Helper.formatDatum(reportJournalKriterienDtoI.dBis, theClientDto.getLocUi()));
		}

		// Kostenstelle
		if (reportJournalKriterienDtoI.kostenstelleIId != null) {
			buff.append(" ")
					.append(getTextRespectUISpr("lp.kostenstelle", theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ").append(
					getSystemFac().kostenstelleFindByPrimaryKey(reportJournalKriterienDtoI.kostenstelleIId).getCNr());
		}

		// Kunde
		if (reportJournalKriterienDtoI.kundeIId != null) {
			buff.append(" ")
					.append(getTextRespectUISpr("lp.kunde", theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ")
					.append(getKundeFac().kundeFindByPrimaryKey(reportJournalKriterienDtoI.kundeIId, theClientDto)
							.getPartnerDto().getCName1nachnamefirmazeile1());
		}

		// Auftragsnummer
		if (reportJournalKriterienDtoI.sBelegnummerVon != null || reportJournalKriterienDtoI.sBelegnummerBis != null) {
			buff.append(" ").append(
					getTextRespectUISpr("auft.auftragsnummer", theClientDto.getMandant(), theClientDto.getLocUi()));
		}

		if (reportJournalKriterienDtoI.sBelegnummerVon != null) {
			buff.append(" ").append(getTextRespectUISpr("lp.von", theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ").append(reportJournalKriterienDtoI.sBelegnummerVon);
		}

		if (reportJournalKriterienDtoI.sBelegnummerBis != null) {
			buff.append(" ").append(getTextRespectUISpr("lp.bis", theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ").append(reportJournalKriterienDtoI.sBelegnummerBis);
		}

		String cBuffer = buff.toString().trim();

		return cBuffer;
	}

}
