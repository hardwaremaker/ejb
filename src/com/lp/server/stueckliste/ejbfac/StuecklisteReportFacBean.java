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
package com.lp.server.stueckliste.ejbfac;

import java.awt.Image;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.HerstellerDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkPreisfindungEinzelverkaufspreisDto;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklistearbeitsplan;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklisteeigenschaft;
import com.lp.server.stueckliste.service.PosersatzDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklisteMitStrukturDto;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklisteeigenschaftDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.service.EinheitDto;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
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
public class StuecklisteReportFacBean extends LPReport implements
		StuecklisteReportFac {

	private Object[][] data = null;
	private String sAktuellerReport = null;

	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ARTIKEL = 0;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ZIELMENGENEINHEIT = 1;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ZIELMENGE = 2;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_MONTAGEART = 3;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_LAGERSTAND = 4;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_VERFUEGBAR = 5;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_BESTELLT = 6;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_LAGERORT = 7;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_BEZEICHNUNG = 8;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_KURZBEZEICHNUNG = 9;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ZUSATZBEZEICHNUNG = 10;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ZUSATZBEZEICHNUNG2 = 11;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_INDEX = 12;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_REVISION = 13;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_REFERENZNUMMER = 14;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_STUECKLISTEPOSITION_I_ID = 15;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ERSATZARTIKEL = 16;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ERSATZARTIKEL_BEZEICHNUNG = 17;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_I_EBENE = 18;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_IN_FERTIGUNG = 19;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_F_ARTIKELBILD = 20;
	private static int REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ANZAHL_SPALTEN = 21;

	private static int REPORT_ARBEITSPLAN_ARTIKEL = 0;
	private static int REPORT_ARBEITSPLAN_ARTIKELBEZEICHNUNG = 1;
	private static int REPORT_ARBEITSPLAN_ARBEITSGANG = 2;
	private static int REPORT_ARBEITSPLAN_STUECKZEIT = 3;
	private static int REPORT_ARBEITSPLAN_RUESTZEIT = 4;
	private static int REPORT_ARBEITSPLAN_GESAMTZEIT = 5;
	private static int REPORT_ARBEITSPLAN_KOMMENTAR = 6;
	private static int REPORT_ARBEITSPLAN_PREIS = 7;
	private static int REPORT_ARBEITSPLAN_AGART = 8;
	private static int REPORT_ARBEITSPLAN_AUFSPANNUNG = 9;
	private static int REPORT_ARBEITSPLAN_UNTERARBEITSGANG = 10;
	private static int REPORT_ARBEITSPLAN_MASCHINE = 11;
	private static int REPORT_ARBEITSPLAN_KOSTEN_MASCHINE = 12;
	private static int REPORT_ARBEITSPLAN_FREMDMATERIAL_ARTIKEL = 13;
	private static int REPORT_ARBEITSPLAN_FREMDMATERIAL_ARTIKELBEZEICHNUNG = 14;
	private static int REPORT_ARBEITSPLAN_FREMDMATERIAL_ARTIKELKURZBEZEICHNUNG = 15;
	private static int REPORT_ARBEITSPLAN_FREMDMATERIAL_ARTIKELZUSATZBEZEICHNUNG = 16;
	private static int REPORT_ARBEITSPLAN_FREMDMATERIAL_ARTIKELZUSATZBEZEICHNUNG2 = 17;
	private static int REPORT_ARBEITSPLAN_FREMDMATERIAL_SOLLMENGE = 18;
	private static int REPORT_ARBEITSPLAN_INDEX = 19;
	private static int REPORT_ARBEITSPLAN_REVISION = 20;
	private static int REPORT_ARBEITSPLAN_ARTIKELZUSATZBEZEICHNUNG = 21;
	private static int REPORT_ARBEITSPLAN_ARTIKELZUSATZBEZEICHNUNG2 = 22;
	private static int REPORT_ARBEITSPLAN_LANGTEXT = 23;
	private static int REPORT_ARBEITSPLAN_ANZAHL_SPALTEN = 24;

	private static int REPORT_GESAMTKALKULATION_ARTIKEL = 0;
	private static int REPORT_GESAMTKALKULATION_ARTIKELBEZEICHNUNG = 1;
	private static int REPORT_GESAMTKALKULATION_MENGE = 2;
	private static int REPORT_GESAMTKALKULATION_MENGENEINHEIT = 3;
	private static int REPORT_GESAMTKALKULATION_GESTPREIS = 4;
	private static int REPORT_GESAMTKALKULATION_GESTWERT = 5;
	private static int REPORT_GESAMTKALKULATION_LIEF1PREIS = 6;
	private static int REPORT_GESAMTKALKULATION_LIEF1PREISGESAMT = 7;
	private static int REPORT_GESAMTKALKULATION_STUECKZEIT = 8;
	private static int REPORT_GESAMTKALKULATION_RUESTZEIT = 9;
	private static int REPORT_GESAMTKALKULATION_GESTPREISWENNSTUECKLISTE = 10;
	private static int REPORT_GESAMTKALKULATION_SUMMEGESTPREISPOSITIONENWENNSTUECKLISTE = 11;
	private static int REPORT_GESAMTKALKULATION_GESTPREISEWEICHENAB = 12;
	private static int REPORT_GESAMTKALKULATION_DURCHLAUFZEIT = 13;
	private static int REPORT_GESAMTKALKULATION_KALKPREIS = 14;
	private static int REPORT_GESAMTKALKULATION_KALKWERT = 15;
	private static int REPORT_GESAMTKALKULATION_VKPREIS = 16;
	private static int REPORT_GESAMTKALKULATION_VKWERT = 17;
	private static int REPORT_GESAMTKALKULATION_MINDESTDECKUNGSBEITRAG = 18;
	private static int REPORT_GESAMTKALKULATION_STKLART = 19;
	private static int REPORT_GESAMTKALKULATION_GEWICHT = 20;
	private static int REPORT_GESAMTKALKULATION_MATERIALGEWICHT = 21;
	private static int REPORT_GESAMTKALKULATION_WIEDERBESCHAFFUNGSZEIT = 22;
	private static int REPORT_GESAMTKALKULATION_MATERIALZUSCHLAG = 23;
	private static int REPORT_GESAMTKALKULATION_FIXKOSTEN = 24;
	private static int REPORT_GESAMTKALKULATION_ANZAHL_SPALTEN = 25;

	@PersistenceContext
	private EntityManager em;

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAusgabestueckliste(Integer[] stuecklisteIId,
			Integer lagerIId, Boolean bMitStuecklistenkommentar,
			Boolean bUnterstuecklistenEinbinden,
			Boolean bGleichePositionenZusammenfassen,
			Integer iOptionSortierungUnterstuecklisten, BigDecimal nLosgroesse,
			boolean bUnterstklstrukurBelassen, TheClientDto theClientDto) {

		if (stuecklisteIId == null || lagerIId == null
				|| bMitStuecklistenkommentar == null
				|| bUnterstuecklistenEinbinden == null
				|| bGleichePositionenZusammenfassen == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"stuecklisteIId == null || lagerIId == null || bMitStuecklistenkommentar == null || bUnterstuecklistenEinbinden == null || bGleichePositionenZusammenfassen == null"));
		}
		if (bUnterstuecklistenEinbinden.booleanValue() == true
				&& iOptionSortierungUnterstuecklisten == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"bUnterstuecklistenEinbinden.booleanValue()==true && iOptionSortierungUnterstuecklisten==null"));
		}
		// Losgreosse updaten
		StuecklisteDto dto = null;
		String stuecklisten = "";
		for (int i = 0; i < stuecklisteIId.length; i++) {

			StuecklisteDto stklDto = getStuecklisteFac()
					.stuecklisteFindByPrimaryKey(stuecklisteIId[i],
							theClientDto);

			stuecklisten += stklDto.getArtikelDto().getCNr();
			if(i!=stuecklisteIId.length-1){
				stuecklisten +=", ";
			}

			if (i == 0) {
				dto = stklDto;
			}

			getStuecklisteFac().updateStuecklisteLosgroesse(stuecklisteIId[i],
					nLosgroesse);
		}

		index = -1;
		sAktuellerReport = StuecklisteReportFac.REPORT_STUECKLISTE_AUSGABESTUECKLSITE;

		List<?> m = getStuecklisteFac().getStrukturDatenEinerStueckliste(
				stuecklisteIId, theClientDto,
				iOptionSortierungUnterstuecklisten.intValue(), 0, null,
				bUnterstuecklistenEinbinden.booleanValue(),
				bGleichePositionenZusammenfassen.booleanValue(), nLosgroesse,
				null, bUnterstklstrukurBelassen);

		Iterator<?> it = m.listIterator();
		data = new Object[m.size()][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ANZAHL_SPALTEN];
		int row = 0;
		while (it.hasNext()) {
			StuecklisteMitStrukturDto struktur = (StuecklisteMitStrukturDto) it
					.next();
			StuecklistepositionDto position = struktur
					.getStuecklistepositionDto();

			String einrueckung = "";
			for (int i = 0; i < struktur.getIEbene(); i++) {
				einrueckung = einrueckung + "   ";
			}

			data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_I_EBENE] = struktur
					.getIEbene();

			try {

				data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_STUECKLISTEPOSITION_I_ID] = position
						.getIId();

				com.lp.server.artikel.service.ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(position.getArtikelIId(),
								theClientDto);

				if (artikelDto.getArtikelartCNr() != null
						&& artikelDto
								.getArtikelartCNr()
								.equals(com.lp.server.artikel.service.ArtikelFac.ARTIKELART_HANDARTIKEL)) {

					data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ARTIKEL] = "";

				} else {
					data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ARTIKEL] = einrueckung
							+ artikelDto.getCNr();

					// Artikelkommentar Text und Bild
					Image imageKommentar = null;
					try {
						ArtikelkommentarDto[] aKommentarDto = getArtikelkommentarFac()
								.artikelkommentardruckFindByArtikelIIdBelegartCNr(
										artikelDto.getIId(),
										LocaleFac.BELEGART_LOS,
										theClientDto.getLocUiAsString(),
										theClientDto);

						// Artikelkommentar kann Text oder Bild sein
						if (aKommentarDto != null) {
							for (int k = 0; k < aKommentarDto.length; k++) {
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
								}
							}
						}

					} catch (RemoteException ex3) {
						throwEJBExceptionLPRespectOld(ex3);
					}

					data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_F_ARTIKELBILD] = imageKommentar;

				}

				data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_REFERENZNUMMER] = artikelDto
						.getCReferenznr();

				if (artikelDto.getArtikelsprDto() != null) {
					data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_BEZEICHNUNG] = artikelDto
							.getArtikelsprDto().getCBez();
					data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ZUSATZBEZEICHNUNG] = artikelDto
							.getArtikelsprDto().getCZbez();
					data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ZUSATZBEZEICHNUNG2] = artikelDto
							.getArtikelsprDto().getCZbez2();
					data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_KURZBEZEICHNUNG] = artikelDto
							.getArtikelsprDto().getCKbez();
				}

				data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ZIELMENGENEINHEIT] = artikelDto
						.getEinheitCNr();

				data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_INDEX] = artikelDto
						.getCIndex();
				data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_REVISION] = artikelDto
						.getCRevision();

				BigDecimal lagerstand = null;
				lagerstand = getLagerFac().getLagerstandOhneExc(
						artikelDto.getIId(), lagerIId, theClientDto);
				data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_LAGERSTAND] = lagerstand;

				// Verfuegbar
				BigDecimal reservierungen = getReservierungFac()
						.getAnzahlReservierungen(artikelDto.getIId(),
								theClientDto);
				BigDecimal fehlmengen = getFehlmengeFac()
						.getAnzahlFehlmengeEinesArtikels(artikelDto.getIId(),
								theClientDto);

				BigDecimal verfuegbar = lagerstand.subtract(fehlmengen)
						.subtract(reservierungen);
				data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_VERFUEGBAR] = verfuegbar;

				data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_IN_FERTIGUNG] = getFertigungFac()
						.getAnzahlInFertigung(artikelDto.getIId(), theClientDto);

				data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_BESTELLT] = getArtikelbestelltFac()
						.getAnzahlBestellt(artikelDto.getIId());

				data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_MONTAGEART] = position
						.getMontageartDto().getCBez();

				if (dto.getNLosgroesse() != null) {
					data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ZIELMENGE] = Helper
							.rundeKaufmaennisch(position.getNZielmenge()
									.multiply(dto.getNLosgroesse()), 2);
				} else {
					data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ZIELMENGE] = position
							.getNZielmenge();
				}

				data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_MONTAGEART] = position
						.getMontageartDto().getCBez();

				try {
					data[row][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_LAGERORT] = getLagerFac()
							.getLagerplaezteEinesArtikels(artikelDto.getIId(),
									lagerIId);

				} catch (javax.ejb.EJBException ex1) {
					// kein lagerort vorhanden
				} catch (EJBExceptionLP ex1) {
					// kein lagerort vorhanden
				}
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
			row++;
		}

		if (bUnterstuecklistenEinbinden == false
				|| (bUnterstuecklistenEinbinden == true && !bUnterstklstrukurBelassen)) {

			// Nach Fertigungsgruppe sortieren
			for (int k = data.length - 1; k > 0; --k) {
				for (int j = 0; j < k; ++j) {
					Object[] a1 = (Object[]) data[j];
					Object[] a2 = (Object[]) data[j + 1];

					if (((String) a1[REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ARTIKEL])
							.compareTo((String) a2[REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ARTIKEL]) > 0) {
						data[j] = a2;
						data[j + 1] = a1;

					}

				}

			}

		}

		// PJ15813 Ersatztypen hinzufuegen
		if (bGleichePositionenZusammenfassen == false) {

			ArrayList alDaten = new ArrayList();
			for (int i = 0; i < data.length; i++) {

				alDaten.add(data[i]);

				PosersatzDto[] posersatzDtos = getStuecklisteFac()
						.posersatzFindByStuecklistepositionIId(
								(Integer) data[i][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_STUECKLISTEPOSITION_I_ID]);

				for (int j = 0; j < posersatzDtos.length; j++) {
					Object[] neueZeile = data[i].clone();

					String einrueckung = "";
					for (int k = 0; k < (Integer) data[i][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_I_EBENE]; k++) {
						einrueckung = einrueckung + "   ";
					}

					com.lp.server.artikel.service.ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(
									posersatzDtos[j].getArtikelIIdErsatz(),
									theClientDto);

					neueZeile[REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ERSATZARTIKEL] = einrueckung
							+ artikelDto.getCNr();

					neueZeile[REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ERSATZARTIKEL_BEZEICHNUNG] = artikelDto
							.formatBezeichnung();

					neueZeile[REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ZIELMENGE] = new BigDecimal(
							0);

					alDaten.add(neueZeile);

				}

			}

			data = new Object[alDaten.size()][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ANZAHL_SPALTEN];
			data = (Object[][]) alDaten.toArray(data);

		}

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		if (stuecklisteIId.length > 1) {
			parameter.put("P_STUECKLISTE", stuecklisten);
		} else {
			parameter.put("P_STUECKLISTE", dto.getArtikelDto().getCNr());

		}

		parameter.put("P_STUECKLISTE", dto.getArtikelDto().getCNr());
		parameter.put("P_ERFASSUNGSFAKTOR", dto.getIErfassungsfaktor());
		parameter.put("P_STUECKLISTEBEZEICHNUNG", dto.getArtikelDto()
				.formatBezeichnung());
		parameter.put("P_EINHEIT", dto.getArtikelDto().getEinheitCNr());
		parameter.put("P_ANLEGEN", dto.getTAnlegen());
		parameter.put("P_AENDERN", dto.getTAendern());
		if (bMitStuecklistenkommentar.booleanValue() == true) {
			parameter.put("P_KOMMENTAR",
					Helper.formatStyledTextForJasper(dto.getXKommentar()));
		}

		parameter.put("P_LOSGROESSE", dto.getNLosgroesse());
		initJRDS(parameter, StuecklisteReportFac.REPORT_MODUL,
				StuecklisteReportFac.REPORT_STUECKLISTE_AUSGABESTUECKLSITE,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printGesamtkalkulation(Integer stuecklisteIId,
			BigDecimal nLosgroesse, TheClientDto theClientDto) {

		if (stuecklisteIId == null || nLosgroesse == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"stuecklisteIId == null || nLosgroesse == null"));
		}
		if (nLosgroesse.doubleValue() <= 0) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ZAHL_ZU_KLEIN,
					new Exception("nLosgroesse.doubleValue()<=0"));
		}

		index = -1;
		sAktuellerReport = StuecklisteReportFac.REPORT_STUECKLISTE_GESAMTKALKULATION;
		// Mandantenwaehrung holen
		String mandantenWaehrung = null;
		try {
			mandantenWaehrung = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto).getWaehrungCNr();
		} catch (RemoteException ex2) {
			throwEJBExceptionLPRespectOld(ex2);

		}

		// Mandantparameter holen
		ParametermandantDto parameter = null;
		boolean bGestpreisberechnungHauptlager = false;
		Integer hauptlagerIId = null;
		try {
			parameter = (ParametermandantDto) getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_STUECKLISTE,
							ParameterFac.PARAMETER_STUECKLISTE_ARBEITSPLAN_ZEITEINHEIT);

			ParametermandantDto parameterGestpreisBerechnung = null;
			parameterGestpreisBerechnung = (ParametermandantDto) getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_GESTPREISBERECHNUNG_HAUPTLAGER);

			bGestpreisberechnungHauptlager = ((Boolean) parameterGestpreisBerechnung
					.getCWertAsObject()).booleanValue();

			hauptlagerIId = getLagerFac().getHauptlagerDesMandanten(
					theClientDto).getIId();

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		String sEinheit = parameter.getCWert().trim();
		StuecklisteDto stklDto = null;
		KundeDto kundeDto = null;

		try {
			stklDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(
					stuecklisteIId, theClientDto);
			if (stklDto.getPartnerIId() != null) {
				kundeDto = getKundeFac()
						.kundeFindByiIdPartnercNrMandantOhneExc(
								stklDto.getPartnerIId(),
								theClientDto.getMandant(), theClientDto);
			}

		} catch (RemoteException ex2) {
			throwEJBExceptionLPRespectOld(ex2);
		}

		// Losgreosse updaten
		getStuecklisteFac().updateStuecklisteLosgroesse(stuecklisteIId,
				nLosgroesse);

		List<?> m = null;
		try {
			m = getStuecklisteFac()
					.getStrukturDatenEinerStuecklisteMitArbeitsplan(
							stuecklisteIId,
							theClientDto,
							StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_ARTIKELNR,
							0, null, true, true, nLosgroesse, null);
		} catch (RemoteException ex4) {
			throwEJBExceptionLPRespectOld(ex4);
		}

		Iterator<?> it = m.listIterator();
		data = new Object[m.size()][REPORT_GESAMTKALKULATION_ANZAHL_SPALTEN];

		BigDecimal bdArbeitszeitkosten = new BigDecimal(0);
		BigDecimal bdRuestzeitMannGesamt = new BigDecimal(0);
		BigDecimal bdZeitMannGesamt = new BigDecimal(0);
		BigDecimal bdRuestzeitMaschineGesamt = new BigDecimal(0);
		BigDecimal bdZeitMaschineGesamt = new BigDecimal(0);
		BigDecimal bdMaschinenzeitkosten = new BigDecimal(0);

		// POSITIONEN

		BigDecimal bdMaterialkosten = new BigDecimal(0);
		BigDecimal bdMaterialkostenLief = new BigDecimal(0);
		BigDecimal bdFixkostenLief = new BigDecimal(0);

		HashMap<Integer, BigDecimal> hmHoechsteDurchlaufzeitEinerEbene = new HashMap<Integer, BigDecimal>();

		int row = 0;
		while (it.hasNext()) {
			StuecklisteMitStrukturDto struktur = (StuecklisteMitStrukturDto) it
					.next();

			String einrueckung = "";
			for (int i = 0; i < struktur.getIEbene(); i++) {
				einrueckung = einrueckung + "   ";
			}

			// Wenn Position
			if (!struktur.isBArbeitszeit()) {
				StuecklistepositionDto position = struktur
						.getStuecklistepositionDto();
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(position.getArtikelIId(),
								theClientDto);

				if (artikelDto.getArtikelartCNr() != null
						&& artikelDto
								.getArtikelartCNr()
								.equals(com.lp.server.artikel.service.ArtikelFac.ARTIKELART_HANDARTIKEL)) {

					data[row][REPORT_GESAMTKALKULATION_ARTIKEL] = "";

				} else {
					data[row][REPORT_GESAMTKALKULATION_ARTIKEL] = einrueckung
							+ artikelDto.getCNr();
				}

				data[row][REPORT_GESAMTKALKULATION_GEWICHT] = artikelDto
						.getFGewichtkg();
				data[row][REPORT_GESAMTKALKULATION_MATERIALGEWICHT] = artikelDto
						.getFMaterialgewicht();

				data[row][REPORT_GESAMTKALKULATION_ARTIKELBEZEICHNUNG] = artikelDto
						.formatBezeichnung();
				data[row][REPORT_GESAMTKALKULATION_MENGE] = position
						.getNZielmenge().multiply(nLosgroesse);

				data[row][REPORT_GESAMTKALKULATION_MENGENEINHEIT] = artikelDto
						.getEinheitCNr().trim();
				data[row][REPORT_GESAMTKALKULATION_MINDESTDECKUNGSBEITRAG] = artikelDto
						.getFMindestdeckungsbeitrag();
				data[row][REPORT_GESAMTKALKULATION_DURCHLAUFZEIT] = struktur
						.getDurchlaufzeit();

				// Hoechste durclaufzeit pro ebene ermitteln
				if (struktur.getDurchlaufzeit() != null) {
					if (hmHoechsteDurchlaufzeitEinerEbene.containsKey(struktur
							.getIEbene())) {
						BigDecimal value = (BigDecimal) hmHoechsteDurchlaufzeitEinerEbene
								.get(struktur.getIEbene());
						if (value.doubleValue() < struktur.getDurchlaufzeit()
								.doubleValue()) {
							hmHoechsteDurchlaufzeitEinerEbene.put(
									struktur.getIEbene(),
									struktur.getDurchlaufzeit());
						}
					} else {
						hmHoechsteDurchlaufzeitEinerEbene.put(
								struktur.getIEbene(),
								struktur.getDurchlaufzeit());
					}
				}

				// Kalkpreis (fuer CNC-Rettenbacher)
				if (position.getNKalkpreis() != null) {
					data[row][REPORT_GESAMTKALKULATION_KALKPREIS] = position
							.getNKalkpreis();
					data[row][REPORT_GESAMTKALKULATION_KALKWERT] = position
							.getNKalkpreis().multiply(
									position.getNZielmenge().multiply(
											nLosgroesse));
				}

				// VKPreise
				if (stklDto.getPartnerIId() != null) {
					try {
						if (kundeDto != null) {

							if (kundeDto.getMwstsatzbezIId() != null) {
								VkpreisfindungDto vkpreisDto = getVkPreisfindungFac()
										.verkaufspreisfindung(
												artikelDto.getIId(),
												kundeDto.getIId(),
												position.getNZielmenge()
														.multiply(nLosgroesse),
												new java.sql.Date(System
														.currentTimeMillis()),
												kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
												getMandantFac()
														.mwstsatzFindByMwstsatzbezIIdAktuellster(
																kundeDto.getMwstsatzbezIId(),
																theClientDto)
														.getIId(),
												theClientDto
														.getSMandantenwaehrung(),
												theClientDto);

								VerkaufspreisDto kundenVKPreisDto = Helper
										.getVkpreisBerechnet(vkpreisDto);

								if (kundenVKPreisDto != null
										&& kundenVKPreisDto.nettopreis != null) {

									data[row][REPORT_GESAMTKALKULATION_VKPREIS] = kundenVKPreisDto.nettopreis;
									data[row][REPORT_GESAMTKALKULATION_MATERIALZUSCHLAG] = kundenVKPreisDto.bdMaterialzuschlag;
									data[row][REPORT_GESAMTKALKULATION_VKWERT] = position
											.getNZielmenge()
											.multiply(nLosgroesse)
											.multiply(
													kundenVKPreisDto.nettopreis);
								}

							}
						}
					} catch (RemoteException ex5) {
						throwEJBExceptionLPRespectOld(ex5);
					}

				}

				try {
					ArtikellieferantDto artikellieferantDto = getArtikelFac()
							.getArtikelEinkaufspreis(
									artikelDto.getIId(),
									position.getNZielmenge().multiply(
											nLosgroesse), mandantenWaehrung,
									theClientDto);
					if (artikellieferantDto != null
							&& artikellieferantDto.getLief1Preis() != null) {
						data[row][REPORT_GESAMTKALKULATION_LIEF1PREIS] = artikellieferantDto
								.getLief1Preis();
						data[row][REPORT_GESAMTKALKULATION_LIEF1PREISGESAMT] = position
								.getNZielmenge().multiply(nLosgroesse)
								.multiply(artikellieferantDto.getLief1Preis());
						data[row][REPORT_GESAMTKALKULATION_WIEDERBESCHAFFUNGSZEIT] = artikellieferantDto
								.getIWiederbeschaffungszeit();
						if (artikellieferantDto.getNFixkosten() != null) {
							data[row][REPORT_GESAMTKALKULATION_FIXKOSTEN] = artikellieferantDto
									.getNFixkosten();
						} else {
							data[row][REPORT_GESAMTKALKULATION_FIXKOSTEN] = new BigDecimal(
									0);
						}

					} else {
						data[row][REPORT_GESAMTKALKULATION_LIEF1PREIS] = new BigDecimal(
								0);
						data[row][REPORT_GESAMTKALKULATION_LIEF1PREISGESAMT] = new BigDecimal(
								0);
						data[row][REPORT_GESAMTKALKULATION_FIXKOSTEN] = new BigDecimal(
								0);
					}

					BigDecimal bdGestpreis = null;

					if (bGestpreisberechnungHauptlager == true) {
						bdGestpreis = getLagerFac()
								.getGemittelterGestehungspreisEinesLagers(
										artikelDto.getIId(), hauptlagerIId,
										theClientDto);

					} else {
						bdGestpreis = getLagerFac()
								.getGemittelterGestehungspreisAllerLaegerEinesMandanten(
										artikelDto.getIId(), theClientDto);

					}

					if (!struktur.isBStueckliste()) {

						data[row][REPORT_GESAMTKALKULATION_GESTPREIS] = bdGestpreis;
						data[row][REPORT_GESAMTKALKULATION_GESTWERT] = bdGestpreis
								.multiply(position.getNZielmenge().multiply(
										nLosgroesse));

					} else {

						if (struktur.getStuecklisteDto() != null) {
							data[row][REPORT_GESAMTKALKULATION_STKLART] = struktur
									.getStuecklisteDto().getStuecklisteartCNr();
						}

						data[row][REPORT_GESAMTKALKULATION_GESTPREISWENNSTUECKLISTE] = bdGestpreis;
						// Summe der Gestehungspreise der Unterstueckliste
						// berechnen
						BigDecimal summePositionen = getStuecklisteFac()
								.berechneStuecklistenGestehungspreisAusPositionen(
										artikelDto.getIId(), theClientDto);

						if (summePositionen == null) {
							summePositionen = new BigDecimal(0);
						}
						data[row][REPORT_GESAMTKALKULATION_SUMMEGESTPREISPOSITIONENWENNSTUECKLISTE] = summePositionen;
						// Wenn mehr wie 10% Abweichung, dann wird Flag gesetzt

						BigDecimal abweichung = bdGestpreis
								.subtract(summePositionen)
								.abs()
								.divide(new BigDecimal(10),
										BigDecimal.ROUND_HALF_EVEN);

						if (summePositionen.doubleValue() < bdGestpreis.add(
								abweichung).doubleValue()
								&& summePositionen.doubleValue() > bdGestpreis
										.subtract(abweichung).doubleValue()) {
							data[row][REPORT_GESAMTKALKULATION_GESTPREISEWEICHENAB] = new Boolean(
									false);
						} else {
							data[row][REPORT_GESAMTKALKULATION_GESTPREISEWEICHENAB] = new Boolean(
									true);
						}
					}
					if (artikelDto.getArtikelartCNr().equals(
							ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
						bdArbeitszeitkosten = bdArbeitszeitkosten
								.add((BigDecimal) data[row][REPORT_GESAMTKALKULATION_GESTWERT]);
						bdZeitMannGesamt = bdZeitMannGesamt.add(position
								.getNMenge());
						if (sEinheit.equals(SystemFac.EINHEIT_STUNDE.trim())) {
							data[row][REPORT_GESAMTKALKULATION_STUECKZEIT] = position
									.getNMenge();
						} else if (sEinheit.equals(SystemFac.EINHEIT_MINUTE
								.trim())) {
							data[row][REPORT_GESAMTKALKULATION_STUECKZEIT] = position
									.getNMenge().multiply(new BigDecimal(60));
						} else if (sEinheit.equals(SystemFac.EINHEIT_SEKUNDE
								.trim())) {
							data[row][REPORT_GESAMTKALKULATION_STUECKZEIT] = position
									.getNMenge().multiply(new BigDecimal(3600));
						}
					} else {
						if (data[row][REPORT_GESAMTKALKULATION_GESTWERT] != null) {
							bdMaterialkosten = bdMaterialkosten
									.add((BigDecimal) data[row][REPORT_GESAMTKALKULATION_GESTWERT]);
						}
						if (data[row][REPORT_GESAMTKALKULATION_LIEF1PREISGESAMT] != null
								&& data[row][REPORT_GESAMTKALKULATION_GESTPREISWENNSTUECKLISTE] == null) {
							bdMaterialkostenLief = bdMaterialkostenLief
									.add((BigDecimal) data[row][REPORT_GESAMTKALKULATION_LIEF1PREISGESAMT]);
						}

						if (data[row][REPORT_GESAMTKALKULATION_FIXKOSTEN] != null
								&& data[row][REPORT_GESAMTKALKULATION_GESTPREISWENNSTUECKLISTE] == null) {
							bdFixkostenLief = bdFixkostenLief
									.add((BigDecimal) data[row][REPORT_GESAMTKALKULATION_FIXKOSTEN]);
						}
					}
				} catch (RemoteException ex1) {
					throwEJBExceptionLPRespectOld(ex1);
				}
			} else {

				StuecklistearbeitsplanDto stuecklistearbeitsplanDto = struktur
						.getStuecklistearbeitsplanDto();
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(
								stuecklistearbeitsplanDto.getArtikelIId(),
								theClientDto);

				if (artikelDto.getArtikelartCNr() != null
						&& artikelDto
								.getArtikelartCNr()
								.equals(com.lp.server.artikel.service.ArtikelFac.ARTIKELART_HANDARTIKEL)) {
					data[row][REPORT_GESAMTKALKULATION_ARTIKEL] = "";
				} else {
					data[row][REPORT_GESAMTKALKULATION_ARTIKEL] = einrueckung
							+ artikelDto.getCNr();
				}

				data[row][REPORT_GESAMTKALKULATION_ARTIKELBEZEICHNUNG] = artikelDto
						.formatBezeichnung();
				data[row][REPORT_GESAMTKALKULATION_MINDESTDECKUNGSBEITRAG] = artikelDto
						.getFMindestdeckungsbeitrag();
				data[row][REPORT_GESAMTKALKULATION_MENGENEINHEIT] = artikelDto
						.getEinheitCNr().trim();

				BigDecimal bdGesamtzeit = Helper.berechneGesamtzeitInStunden(
						stuecklistearbeitsplanDto.getLRuestzeit(),
						stuecklistearbeitsplanDto.getLStueckzeit(),
						nLosgroesse, null,
						stuecklistearbeitsplanDto.getIAufspannung());

				data[row][REPORT_GESAMTKALKULATION_MENGE] = bdGesamtzeit;

				try {
					// Wenn Maschinenzeit, dann statt Artikelnr und Bezeichnung,
					// Maschinenr und Bezeichnung einbauen

					if (!struktur.isBMaschinenzeit()) {

						ArtikellieferantDto artikellieferantDto = getArtikelFac()
								.getArtikelEinkaufspreis(artikelDto.getIId(),
										bdGesamtzeit, mandantenWaehrung,
										theClientDto);

						if (artikellieferantDto != null
								&& artikellieferantDto.getLief1Preis() != null) {
							data[row][REPORT_GESAMTKALKULATION_GESTPREIS] = artikellieferantDto
									.getLief1Preis();
							data[row][REPORT_GESAMTKALKULATION_GESTWERT] = artikellieferantDto
									.getLief1Preis().multiply(bdGesamtzeit);
							bdArbeitszeitkosten = bdArbeitszeitkosten
									.add(artikellieferantDto.getLief1Preis()
											.multiply(bdGesamtzeit));
						} else {
							data[row][REPORT_GESAMTKALKULATION_GESTPREIS] = new BigDecimal(
									0);
							data[row][REPORT_GESAMTKALKULATION_GESTWERT] = new BigDecimal(
									0);
						}
					} else {
						com.lp.server.personal.service.MaschineDto maschineDto = getZeiterfassungFac()
								.maschineFindByPrimaryKey(
										stuecklistearbeitsplanDto
												.getMaschineIId());

						data[row][REPORT_GESAMTKALKULATION_ARTIKEL] = einrueckung
								+ "M:" + maschineDto.getCIdentifikationsnr();

						data[row][REPORT_GESAMTKALKULATION_ARTIKELBEZEICHNUNG] = maschineDto
								.getCBez();

						data[row][REPORT_GESAMTKALKULATION_MENGENEINHEIT] = SystemFac.EINHEIT_STUNDE
								.trim();

						BigDecimal maschinenkosten = getZeiterfassungFac()
								.getMaschinenKostenZumZeitpunkt(
										maschineDto.getIId(),
										new java.sql.Timestamp(System
												.currentTimeMillis()));

						data[row][REPORT_GESAMTKALKULATION_GESTPREIS] = maschinenkosten;
						data[row][REPORT_GESAMTKALKULATION_GESTWERT] = maschinenkosten
								.multiply(bdGesamtzeit);
						bdMaschinenzeitkosten = bdMaschinenzeitkosten
								.add(maschinenkosten.multiply(bdGesamtzeit));

					}
				} catch (RemoteException ex1) {
					throwEJBExceptionLPRespectOld(ex1);
				}

				BigDecimal bdStueckzeit = null;
				if (nLosgroesse.doubleValue() != 0) {
					bdStueckzeit = bdGesamtzeit.divide(nLosgroesse, 8,
							BigDecimal.ROUND_HALF_EVEN);
				} else {
					bdStueckzeit = new BigDecimal(0);
				}

				double dRuestzeit = 0;
				double lRuestzeit = stuecklistearbeitsplanDto.getLRuestzeit()
						.longValue();

				if (stuecklistearbeitsplanDto.getMaschineIId() == null) {
					bdZeitMannGesamt = bdZeitMannGesamt.add(bdStueckzeit);
				} else {
					bdZeitMaschineGesamt = bdZeitMaschineGesamt
							.add(bdStueckzeit);

				}
				if (sEinheit.equals(SystemFac.EINHEIT_STUNDE.trim())) {
					dRuestzeit = lRuestzeit / 3600000;
					if (stuecklistearbeitsplanDto.getMaschineIId() == null) {
						bdRuestzeitMannGesamt = bdRuestzeitMannGesamt
								.add(new BigDecimal(dRuestzeit));
					} else {
						bdRuestzeitMaschineGesamt = bdRuestzeitMaschineGesamt
								.add(new BigDecimal(dRuestzeit));

					}
				} else if (sEinheit.equals(SystemFac.EINHEIT_MINUTE.trim())) {
					bdStueckzeit = bdStueckzeit.multiply(new BigDecimal(60));
					dRuestzeit = lRuestzeit / 60000;
					if (stuecklistearbeitsplanDto.getMaschineIId() == null) {
						bdRuestzeitMannGesamt = bdRuestzeitMannGesamt
								.add(new BigDecimal(dRuestzeit / 60));
					} else {
						bdRuestzeitMaschineGesamt = bdRuestzeitMaschineGesamt
								.add(new BigDecimal(dRuestzeit / 60));
					}
				} else if (sEinheit.equals(SystemFac.EINHEIT_SEKUNDE.trim())) {
					bdStueckzeit = bdStueckzeit.multiply(new BigDecimal(3600));
					dRuestzeit = lRuestzeit / 100;
					if (stuecklistearbeitsplanDto.getMaschineIId() == null) {
						bdRuestzeitMannGesamt = bdRuestzeitMannGesamt
								.add(new BigDecimal(dRuestzeit / 3600));
					} else {
						bdRuestzeitMaschineGesamt = bdRuestzeitMaschineGesamt
								.add(new BigDecimal(dRuestzeit / 3600));
					}
				}
				data[row][REPORT_GESAMTKALKULATION_STUECKZEIT] = bdStueckzeit;
				data[row][REPORT_GESAMTKALKULATION_RUESTZEIT] = new BigDecimal(
						dRuestzeit);

				// VKPreise
				if (stklDto.getPartnerIId() != null) {
					try {
						if (kundeDto != null) {

							if (kundeDto.getMwstsatzbezIId() != null) {
								VkpreisfindungDto vkpreisDto = getVkPreisfindungFac()
										.verkaufspreisfindung(
												artikelDto.getIId(),
												kundeDto.getIId(),
												bdGesamtzeit
														.multiply(nLosgroesse),
												new java.sql.Date(System
														.currentTimeMillis()),
												kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
												getMandantFac()
														.mwstsatzFindByMwstsatzbezIIdAktuellster(
																kundeDto.getMwstsatzbezIId(),
																theClientDto)
														.getIId(),
												theClientDto
														.getSMandantenwaehrung(),
												theClientDto);

								VerkaufspreisDto kundenVKPreisDto = Helper
										.getVkpreisBerechnet(vkpreisDto);

								if (kundenVKPreisDto != null
										&& kundenVKPreisDto.nettopreis != null) {

									data[row][REPORT_GESAMTKALKULATION_VKPREIS] = kundenVKPreisDto.nettopreis;
									data[row][REPORT_GESAMTKALKULATION_VKWERT] = bdGesamtzeit
											.multiply(kundenVKPreisDto.nettopreis);
								}

							}
						}
					} catch (RemoteException ex5) {
						throwEJBExceptionLPRespectOld(ex5);
					}

				}

			}
			row++;
		}

		HashMap<String, Object> reportParameter = new HashMap<String, Object>();
		StuecklisteDto dto = getStuecklisteFac().stuecklisteFindByPrimaryKey(
				stuecklisteIId, theClientDto);

		reportParameter.put("P_STUECKLISTE", dto.getArtikelDto().getCNr());
		if (dto.getArtikelDto().getSollverkaufDto() != null) {
			reportParameter.put("P_STUECKLISTE_AUFSCHLAG", dto.getArtikelDto()
					.getSollverkaufDto().getFAufschlag());
			reportParameter.put("P_STUECKLISTE_SOLLVERKAUF", dto
					.getArtikelDto().getSollverkaufDto().getFSollverkauf());
		}

		reportParameter.put("P_STUECKLISTE_MINDESTDECKUNGSBEITRAG", dto
				.getArtikelDto().getFMindestdeckungsbeitrag());

		reportParameter.put("P_ERFASSUNGSFAKTOR", dto.getIErfassungsfaktor());
		reportParameter.put("P_STUECKLISTEBEZEICHNUNG", dto.getArtikelDto()
				.formatBezeichnung());
		reportParameter.put("P_EINHEIT", dto.getArtikelDto().getEinheitCNr()
				.trim());
		reportParameter.put("P_EINHEITZEIT", sEinheit);
		reportParameter.put("P_LOSGROESSE", dto.getNLosgroesse());
		reportParameter.put("P_DURCHLAUFZEIT", dto.getNDefaultdurchlaufzeit());
		reportParameter.put("P_WAEHRUNG", mandantenWaehrung);

		try {
			ParametermandantDto parameterDto = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT);

			reportParameter.put("P_EINHEIT_WIEDERBESCHAFFUNGSZEIT",
					parameterDto.getCWert());
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		bdMaterialkosten = bdMaterialkosten.divide(nLosgroesse,
				BigDecimal.ROUND_HALF_EVEN);
		// bdMaterialkosten = Helper.rundeKaufmaennisch(bdMaterialkosten, 2);
		reportParameter.put("P_MATERIALKOSTEN", bdMaterialkosten);

		if (kundeDto != null
				&& kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste() != null) {

			try {
				VkpfartikelpreislisteDto preislisteDto = getVkPreisfindungFac()
						.vkpfartikelpreislisteFindByPrimaryKey(
								kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste());

				reportParameter.put("P_PREISLISTE", preislisteDto.getCNr());
			} catch (RemoteException ex6) {
				throwEJBExceptionLPRespectOld(ex6);
			}
		}

		bdMaterialkostenLief = bdMaterialkostenLief.divide(nLosgroesse,
				BigDecimal.ROUND_HALF_EVEN);
		// bdMaterialkostenLief =
		// Helper.rundeKaufmaennisch(bdMaterialkostenLief, 2);
		reportParameter.put("P_MATERIALKOSTENLIEF", bdMaterialkostenLief);
		reportParameter.put("P_FIXKOSTENLIEF", bdFixkostenLief);

		bdArbeitszeitkosten = bdArbeitszeitkosten.divide(nLosgroesse,
				BigDecimal.ROUND_HALF_EVEN);
		// bdArbeitszeitkosten = Helper.rundeKaufmaennisch(bdArbeitszeitkosten,
		// 4);

		reportParameter.put("P_ARBEITSZEITKOSTEN", bdArbeitszeitkosten);

		bdMaschinenzeitkosten = bdMaschinenzeitkosten.divide(nLosgroesse,
				BigDecimal.ROUND_HALF_EVEN);

		reportParameter.put("P_MASCHINENKOSTEN", bdMaschinenzeitkosten);
		reportParameter.put("P_RUESTZEITGESAMTMANN", bdRuestzeitMannGesamt);
		reportParameter.put("P_ZEITGESAMTMANN", bdZeitMannGesamt);
		reportParameter.put("P_RUESTZEITGESAMTMASCHINE",
				bdRuestzeitMaschineGesamt);
		reportParameter.put("P_ZEITGESAMTMASCHINE", bdZeitMaschineGesamt);

		// Mandantparameter holen

		try {
			parameter = (ParametermandantDto) getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.MATERIALGEMEINKOSTENFAKTOR);

			double dMaterialgemeinkostenfaktor = ((Double) parameter
					.getCWertAsObject()).doubleValue();
			reportParameter.put(
					"P_MATERIALGEMEINKOSTENPROZENT",
					Helper.formatZahl(dMaterialgemeinkostenfaktor, 1,
							theClientDto.getLocUi()) + "%");

			dMaterialgemeinkostenfaktor = (dMaterialgemeinkostenfaktor / 100);

			BigDecimal bdMaterialgemeinskosten = bdMaterialkosten
					.multiply(new BigDecimal(dMaterialgemeinkostenfaktor));

			reportParameter.put("P_MATERIALGEMEINKOSTEN",
					bdMaterialgemeinskosten);

			BigDecimal bdMaterialgemeinskostenLief = bdMaterialkostenLief
					.multiply(new BigDecimal(dMaterialgemeinkostenfaktor));

			reportParameter.put("P_MATERIALGEMEINKOSTENLIEF",
					bdMaterialgemeinskostenLief);

			parameter = (ParametermandantDto) getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.FERTIGUNGSGEMEINKOSTENFAKTOR);

			double dFertigungsgemeinkostenfaktor = ((Double) parameter
					.getCWertAsObject()).doubleValue();
			reportParameter.put(
					"P_FERTIGUNGSGEMEINKOSTENPROZENT",
					Helper.formatZahl(dFertigungsgemeinkostenfaktor, 1,
							theClientDto.getLocUi()) + "%");
			dFertigungsgemeinkostenfaktor = (dFertigungsgemeinkostenfaktor / 100);

			BigDecimal bdFertigungskosten = bdMaschinenzeitkosten.add(
					bdArbeitszeitkosten).multiply(
					new BigDecimal(dFertigungsgemeinkostenfaktor));

			reportParameter.put("P_FERTIGUNGSGEMEINKOSTEN", bdFertigungskosten);

			BigDecimal herstellkosten = bdMaterialkosten
					.add(bdArbeitszeitkosten).add(bdMaterialgemeinskosten)
					.add(bdFertigungskosten).add(bdMaschinenzeitkosten);
			reportParameter.put("P_HERSTELLKOSTEN", herstellkosten);

			BigDecimal herstellkostenLief = bdMaterialkostenLief
					.add(bdArbeitszeitkosten).add(bdMaterialgemeinskostenLief)
					.add(bdFertigungskosten).add(bdMaschinenzeitkosten);
			reportParameter.put("P_HERSTELLKOSTENLIEF", herstellkostenLief);

			parameter = (ParametermandantDto) getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.ENTWICKLUNGSGEMEINKOSTENFAKTOR);
			double dEntwicklungsgemeinkostenfaktor = ((Double) parameter
					.getCWertAsObject()).doubleValue();
			reportParameter.put(
					"P_ENTWICKLUNGSGEMEINKOSTENPROZENT",
					Helper.formatZahl(dEntwicklungsgemeinkostenfaktor, 1,
							theClientDto.getLocUi()) + "%");

			dEntwicklungsgemeinkostenfaktor = (dEntwicklungsgemeinkostenfaktor / 100);

			BigDecimal bdEntwicklungsgemeinkosten = herstellkosten
					.multiply(new BigDecimal(dEntwicklungsgemeinkostenfaktor));

			reportParameter.put("P_ENTWICKLUNGSGEMEINKOSTEN",
					bdEntwicklungsgemeinkosten);
			BigDecimal bdEntwicklungsgemeinkostenLief = herstellkostenLief
					.multiply(new BigDecimal(dEntwicklungsgemeinkostenfaktor));

			reportParameter.put("P_ENTWICKLUNGSGEMEINKOSTENLIEF",
					bdEntwicklungsgemeinkostenLief);

			parameter = (ParametermandantDto) getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.VERWALTUNGSGEMEINKOSTENFAKTOR);

			double dVerwaltungssgemeinkostenfaktor = ((Double) parameter
					.getCWertAsObject()).doubleValue();
			reportParameter.put(
					"P_VERWALTUNGSGEMEINKOSTENPROZENT",
					Helper.formatZahl(dVerwaltungssgemeinkostenfaktor, 1,
							theClientDto.getLocUi()) + "%");
			dVerwaltungssgemeinkostenfaktor = (dVerwaltungssgemeinkostenfaktor / 100);

			BigDecimal bdVerwaltungsgemeinkosten = herstellkosten
					.multiply(new BigDecimal(dVerwaltungssgemeinkostenfaktor));

			reportParameter.put("P_VERWALTUNGSGEMEINKOSTEN",
					bdVerwaltungsgemeinkosten);

			BigDecimal bdVerwaltungsgemeinkostenLief = herstellkostenLief
					.multiply(new BigDecimal(dVerwaltungssgemeinkostenfaktor));

			reportParameter.put("P_VERWALTUNGSGEMEINKOSTENLIEF",
					bdVerwaltungsgemeinkostenLief);
			parameter = (ParametermandantDto) getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.VERTRIEBSGEMEINKOSTENFAKTOR);

			double dVertriebsgemeinkostenfaktor = ((Double) parameter
					.getCWertAsObject()).doubleValue();
			reportParameter.put(
					"P_VERTRIEBSGEMEINKOSTENPROZENT",
					Helper.formatZahl(dVertriebsgemeinkostenfaktor, 1,
							theClientDto.getLocUi()) + "%");

			dVertriebsgemeinkostenfaktor = (dVertriebsgemeinkostenfaktor / 100);

			BigDecimal bdVertriebsgemeinkosten = herstellkosten
					.multiply(new BigDecimal(dVertriebsgemeinkostenfaktor));

			reportParameter.put("P_VERTRIEBSGEMEINKOSTEN",
					bdVertriebsgemeinkosten);

			BigDecimal bdVertriebsgemeinkostenLief = herstellkostenLief
					.multiply(new BigDecimal(dVertriebsgemeinkostenfaktor));

			reportParameter.put("P_VERTRIEBSGEMEINKOSTENLIEF",
					bdVertriebsgemeinkostenLief);

			BigDecimal selbstkosten = herstellkosten
					.add(bdEntwicklungsgemeinkosten)
					.add(bdVerwaltungsgemeinkosten)
					.add(bdVertriebsgemeinkosten);
			reportParameter.put("P_SELBSTKOSTEN", selbstkosten);

			BigDecimal selbstkostenLief = herstellkostenLief
					.add(bdEntwicklungsgemeinkostenLief)
					.add(bdVerwaltungsgemeinkostenLief)
					.add(bdVertriebsgemeinkostenLief);
			reportParameter.put("P_SELBSTKOSTENLIEF", selbstkostenLief);

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		BigDecimal durchlaufzeitGesamt = new BigDecimal(0);
		if (dto.getNDefaultdurchlaufzeit() != null) {
			durchlaufzeitGesamt = dto.getNDefaultdurchlaufzeit();
		}
		Iterator<Integer> i = hmHoechsteDurchlaufzeitEinerEbene.keySet()
				.iterator();
		while (i.hasNext()) {
			Integer key = (Integer) i.next();
			BigDecimal wert = (BigDecimal) hmHoechsteDurchlaufzeitEinerEbene
					.get(key);
			durchlaufzeitGesamt = durchlaufzeitGesamt.add(wert);
		}

		reportParameter.put("P_DURCHLAUFZEITGESAMT", durchlaufzeitGesamt);

		initJRDS(reportParameter, StuecklisteReportFac.REPORT_MODUL,
				StuecklisteReportFac.REPORT_STUECKLISTE_GESAMTKALKULATION,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	public JasperPrintLP printArbeitsplan(Integer stuecklisteIId,
			BigDecimal nLosgroesse, TheClientDto theClientDto) {

		index = -1;
		sAktuellerReport = StuecklisteReportFac.REPORT_STUECKLISTE_ARBEITSPLAN;

		FLRStuecklistearbeitsplan flrStuecklistearbeitsplan = new FLRStuecklistearbeitsplan();
		flrStuecklistearbeitsplan.setStueckliste_i_id(stuecklisteIId);

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session.createCriteria(
				FLRStuecklistearbeitsplan.class).add(
				Example.create(flrStuecklistearbeitsplan));

		crit.addOrder(Order
				.asc(StuecklisteFac.FLR_STUECKLISTEARBEITSPLAN_I_ARBEITSGANG));
		crit.addOrder(Order
				.asc(StuecklisteFac.FLR_STUECKLISTEARBEITSPLAN_I_UNTERARBEITSGANG));
		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();
		data = new Object[results.size()][REPORT_ARBEITSPLAN_ANZAHL_SPALTEN];

		String mandantenWaehrung = null;
		try {
			mandantenWaehrung = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto).getWaehrungCNr();
		} catch (RemoteException ex2) {
			throwEJBExceptionLPRespectOld(ex2);

		}
		// Losgreosse updaten
		getStuecklisteFac().updateStuecklisteLosgroesse(stuecklisteIId,
				nLosgroesse);

		ParametermandantDto parameter = null;
		try {
			parameter = (ParametermandantDto) getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_STUECKLISTE,
							ParameterFac.PARAMETER_STUECKLISTE_ARBEITSPLAN_ZEITEINHEIT);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		String sEinheit = parameter.getCWert().trim();

		int row = 0;
		while (resultListIterator.hasNext()) {
			FLRStuecklistearbeitsplan stuecklistearbeitsplan = (FLRStuecklistearbeitsplan) resultListIterator
					.next();

			com.lp.server.artikel.service.ArtikelDto artikelDtoAP = getArtikelFac()
					.artikelFindByPrimaryKeySmall(
							stuecklistearbeitsplan.getFlrartikel().getI_id(),
							theClientDto);

			if (artikelDtoAP.getArtikelartCNr() != null
					&& artikelDtoAP
							.getArtikelartCNr()
							.equals(com.lp.server.artikel.service.ArtikelFac.ARTIKELART_HANDARTIKEL)) {

				data[row][REPORT_ARBEITSPLAN_ARTIKEL] = "";

			} else {
				data[row][REPORT_ARBEITSPLAN_ARTIKEL] = artikelDtoAP.getCNr();

			}

			if (artikelDtoAP.getArtikelsprDto() != null) {
				data[row][REPORT_ARBEITSPLAN_ARTIKELBEZEICHNUNG] = artikelDtoAP
						.getArtikelsprDto().getCBez();
				data[row][REPORT_ARBEITSPLAN_ARTIKELZUSATZBEZEICHNUNG] = artikelDtoAP
						.getArtikelsprDto().getCZbez();
				data[row][REPORT_ARBEITSPLAN_ARTIKELZUSATZBEZEICHNUNG2] = artikelDtoAP
						.getArtikelsprDto().getCZbez2();
			}

			data[row][REPORT_ARBEITSPLAN_INDEX] = artikelDtoAP.getCIndex();
			data[row][REPORT_ARBEITSPLAN_REVISION] = artikelDtoAP
					.getCRevision();

			data[row][REPORT_ARBEITSPLAN_KOMMENTAR] = stuecklistearbeitsplan
					.getC_kommentar();
			data[row][REPORT_ARBEITSPLAN_ARBEITSGANG] = stuecklistearbeitsplan
					.getI_arbeitsgang();

			data[row][REPORT_ARBEITSPLAN_UNTERARBEITSGANG] = stuecklistearbeitsplan
					.getI_unterarbeitsgang();
			data[row][REPORT_ARBEITSPLAN_AUFSPANNUNG] = stuecklistearbeitsplan
					.getI_aufspannung();

			try {
				StuecklistearbeitsplanDto stuecklistearbeitsplanDto = getStuecklisteFac()
						.stuecklistearbeitsplanFindByPrimaryKey(
								stuecklistearbeitsplan.getI_id(), theClientDto);
				data[row][REPORT_ARBEITSPLAN_AGART] = stuecklistearbeitsplanDto
						.getAgartCNr();
				data[row][REPORT_ARBEITSPLAN_LANGTEXT] = stuecklistearbeitsplanDto
						.getXLangtext();

				if (stuecklistearbeitsplan.getFlrmaschine() != null) {

					String maschine = "";
					if (stuecklistearbeitsplan.getFlrmaschine()
							.getC_identifikationsnr() != null) {
						maschine += stuecklistearbeitsplan.getFlrmaschine()
								.getC_identifikationsnr() + " ";
					}
					if (stuecklistearbeitsplan.getFlrmaschine().getC_bez() != null) {
						maschine += stuecklistearbeitsplan.getFlrmaschine()
								.getC_bez() + " ";
					}
					if (stuecklistearbeitsplan.getFlrmaschine()
							.getC_inventarnummer() != null) {
						maschine += stuecklistearbeitsplan.getFlrmaschine()
								.getC_inventarnummer();
					}

					data[row][REPORT_ARBEITSPLAN_MASCHINE] = maschine;
					data[row][REPORT_ARBEITSPLAN_KOSTEN_MASCHINE] = getZeiterfassungFac()
							.getMaschinenKostenZumZeitpunkt(
									stuecklistearbeitsplan.getMaschine_i_id(),
									new Timestamp(System.currentTimeMillis()));
				}

				if (stuecklistearbeitsplanDto.getStuecklistepositionIId() != null) {
					StuecklistepositionDto posDto = getStuecklisteFac()
							.stuecklistepositionFindByPrimaryKey(
									stuecklistearbeitsplanDto
											.getStuecklistepositionIId(),
									theClientDto);
					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(
									posDto.getArtikelIId(), theClientDto);
					data[row][REPORT_ARBEITSPLAN_FREMDMATERIAL_ARTIKEL] = artikelDto
							.getCNr();
					if (artikelDto.getArtikelsprDto() != null) {
						data[row][REPORT_ARBEITSPLAN_FREMDMATERIAL_ARTIKELBEZEICHNUNG] = artikelDto
								.getArtikelsprDto().getCBez();
						data[row][REPORT_ARBEITSPLAN_FREMDMATERIAL_ARTIKELKURZBEZEICHNUNG] = artikelDto
								.getArtikelsprDto().getCKbez();
						data[row][REPORT_ARBEITSPLAN_FREMDMATERIAL_ARTIKELZUSATZBEZEICHNUNG] = artikelDto
								.getArtikelsprDto().getCZbez();
						data[row][REPORT_ARBEITSPLAN_FREMDMATERIAL_ARTIKELZUSATZBEZEICHNUNG2] = artikelDto
								.getArtikelsprDto().getCZbez2();
					}
					data[row][REPORT_ARBEITSPLAN_FREMDMATERIAL_SOLLMENGE] = posDto
							.getNMenge();
				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			double lStueckzeit = stuecklistearbeitsplan.getL_stueckzeit()
					.longValue();
			double lRuestzeit = stuecklistearbeitsplan.getL_ruestzeit()
					.longValue();

			double dRuestzeit = 0;
			double dStueckzeit = 0;

			if (sEinheit.equals(SystemFac.EINHEIT_STUNDE.trim())) {
				dStueckzeit = lStueckzeit / 3600000;
				dRuestzeit = lRuestzeit / 3600000;
			} else if (sEinheit.equals(SystemFac.EINHEIT_MINUTE.trim())) {
				dStueckzeit = lStueckzeit / 60000;
				dRuestzeit = lRuestzeit / 60000;
			} else if (sEinheit.equals(SystemFac.EINHEIT_SEKUNDE.trim())) {
				dStueckzeit = lStueckzeit / 100;
				dRuestzeit = lRuestzeit / 100;
			}

			data[row][REPORT_ARBEITSPLAN_RUESTZEIT] = Helper
					.rundeKaufmaennisch(new BigDecimal(dRuestzeit), 5);
			data[row][REPORT_ARBEITSPLAN_STUECKZEIT] = Helper
					.rundeKaufmaennisch(new BigDecimal(dStueckzeit), 5);
			data[row][REPORT_ARBEITSPLAN_PREIS] = new BigDecimal(0);
			if (stuecklistearbeitsplan.getFlrstueckliste().getN_losgroesse() != null) {

				BigDecimal dGesamt = (new BigDecimal(dStueckzeit)
						.multiply(stuecklistearbeitsplan.getFlrstueckliste()
								.getN_losgroesse())).add(new BigDecimal(
						dRuestzeit));

				data[row][REPORT_ARBEITSPLAN_GESAMTZEIT] = Helper
						.rundeKaufmaennisch(dGesamt, 5);

				try {
					ArtikellieferantDto artikellieferantDto = getArtikelFac()
							.getArtikelEinkaufspreis(
									stuecklistearbeitsplan.getFlrartikel()
											.getI_id(),
									(BigDecimal) data[row][REPORT_ARBEITSPLAN_GESAMTZEIT],
									mandantenWaehrung, theClientDto);

					if (artikellieferantDto != null) {
						data[row][REPORT_ARBEITSPLAN_PREIS] = artikellieferantDto
								.getLief1Preis();
					}
				} catch (RemoteException ex1) {
					throwEJBExceptionLPRespectOld(ex1);
				}

			}

			row++;
		}
		session.close();
		HashMap<String, Object> reportParameter = new HashMap<String, Object>();
		StuecklisteDto dto = getStuecklisteFac().stuecklisteFindByPrimaryKey(
				stuecklisteIId, theClientDto);

		reportParameter.put("P_STUECKLISTE", dto.getArtikelDto().getCNr());
		reportParameter.put("P_EINHEIT", dto.getArtikelDto().getEinheitCNr());
		reportParameter.put("P_LOSGROESSE", dto.getNLosgroesse());
		reportParameter.put("P_WAEHRUNG", mandantenWaehrung);

		reportParameter.put("P_INDEX", dto.getArtikelDto().getCIndex());
		reportParameter.put("P_REVISION", dto.getArtikelDto().getCRevision());
		reportParameter.put("P_REFERENZNUMMER", dto.getArtikelDto()
				.getCReferenznr());

		if (dto.getArtikelDto().getArtikelsprDto() != null) {
			reportParameter.put("P_BEZEICHNUNG", dto.getArtikelDto()
					.getArtikelsprDto().getCBez());
			reportParameter.put("P_ZUSATZBEZEICHNUNG", dto.getArtikelDto()
					.getArtikelsprDto().getCZbez());
			reportParameter.put("P_ZUSATZBEZEICHNUNG2", dto.getArtikelDto()
					.getArtikelsprDto().getCZbez2());
			reportParameter.put("P_KURZBEZEICHNUNG", dto.getArtikelDto()
					.getArtikelsprDto().getCKbez());
		}

		initJRDS(reportParameter, StuecklisteReportFac.REPORT_MODUL,
				StuecklisteReportFac.REPORT_STUECKLISTE_ARBEITSPLAN,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printStuecklisteAllgemeinMitPreis(
			Integer stuecklisteIId, Integer iOptionPreis,
			Boolean bMitPositionskommentar, Boolean bMitStuecklistenkommentar,
			Boolean bUnterstuecklistenEinbinden,
			Boolean bGleichePositionenZusammenfassen,
			Integer iOptionSortierungUnterstuecklisten,
			boolean bUnterstklstrukurBelassen, TheClientDto theClientDto,
			Integer iOptionSortierungStuecklisteGesamt1,
			Integer iOptionSortierungStuecklisteGesamt2,
			Integer iOptionSortierungStuecklisteGesamt3) {

		if (stuecklisteIId == null || iOptionPreis == null
				|| bMitPositionskommentar == null
				|| bMitStuecklistenkommentar == null
				|| bUnterstuecklistenEinbinden == null
				|| bGleichePositionenZusammenfassen == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"stuecklisteIId == null || iOptionPreis == null || bMitPositionskommentar == null || bMitStuecklistenkommentar == null || bUnterstuecklistenEinbinden == null || bGleichePositionenZusammenfassen == null"));
		}
		if (bUnterstuecklistenEinbinden.booleanValue() == true
				&& iOptionSortierungUnterstuecklisten == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"bUnterstuecklistenEinbinden.booleanValue()==true && iOptionSortierungUnterstuecklisten==null"));
		}
		StuecklisteDto dto = getStuecklisteFac().stuecklisteFindByPrimaryKey(
				stuecklisteIId, theClientDto);
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		KundeDto kdDto = null;
		MwstsatzDto mwstsatzDtoAktuell = null;
		try {
			if (dto.getPartnerIId() != null) {
				kdDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(
						dto.getPartnerIId(), theClientDto.getMandant(),
						theClientDto);

			}

			mwstsatzDtoAktuell = null;
			if (kdDto != null) {
				mwstsatzDtoAktuell = getMandantFac()
						.mwstsatzFindByMwstsatzbezIIdAktuellster(
								kdDto.getMwstsatzbezIId(), theClientDto);
				PartnerDto pDto = getPartnerFac().partnerFindByPrimaryKey(
						kdDto.getPartnerIId(), theClientDto);
				parameter.put("P_KUNDE", pDto.formatFixTitelName1Name2());
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		// Report und Datasoure initialisieren
		index = -1;
		sAktuellerReport = StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEIN_MITPREIS;
		// Strukurdaten holen
		List<?> m = null;
		try {
			m = getStuecklisteFac().getStrukturDatenEinerStueckliste(
					stuecklisteIId, theClientDto,
					iOptionSortierungUnterstuecklisten.intValue(), 0, null,
					bUnterstuecklistenEinbinden.booleanValue(),
					bGleichePositionenZusammenfassen.booleanValue(),
					new BigDecimal(1), null, bUnterstklstrukurBelassen);
		} catch (RemoteException ex3) {
			throwEJBExceptionLPRespectOld(ex3);
		}

		String sPreis = null;
		// Mandantenwaehrung holen
		String sMandantenwaehrung = null;
		boolean bGestpreisberechnungHauptlager = true;
		Integer hauptlagerIId = null;
		try {
			sMandantenwaehrung = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto).getWaehrungCNr();
			hauptlagerIId = getLagerFac().getHauptlagerDesMandanten(
					theClientDto).getIId();
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

		try {
			ParametermandantDto parameterGestpreisBerechnung = null;
			parameterGestpreisBerechnung = (ParametermandantDto) getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_GESTPREISBERECHNUNG_HAUPTLAGER);

			bGestpreisberechnungHauptlager = ((Boolean) (parameterGestpreisBerechnung
					.getCWertAsObject())).booleanValue();
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

		Iterator<?> it = m.listIterator();
		data = new Object[m.size()][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ANZAHL_SPALTEN];
		int row = 0;
		while (it.hasNext()) {
			StuecklisteMitStrukturDto struktur = (StuecklisteMitStrukturDto) it
					.next();
			StuecklistepositionDto position = struktur
					.getStuecklistepositionDto();

			// Artikelkommentar Text und Bild
			ArtikelkommentarDto[] aKommentarDto = null;
			try {
				aKommentarDto = getArtikelkommentarFac()
						.artikelkommentardruckFindByArtikelIIdBelegartCNr(
								position.getArtikelIId(),
								LocaleFac.BELEGART_STUECKLISTE,
								theClientDto.getLocUiAsString(), theClientDto);
			} catch (RemoteException ex3) {
			} catch (EJBExceptionLP ex3) {
			}

			// Einrueckung fuer Unterstuecklisten
			String einrueckung = "";
			for (int i = 0; i < struktur.getIEbene(); i++) {
				einrueckung = einrueckung + "   ";
			}

			try {

				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_I_EBENE] = struktur
						.getIEbene();

				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_STUECKLISTEPOSITION_I_ID] = position
						.getIId();
				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_AUF_BELEG_MITDRUCKEN] = Helper
						.short2Boolean(position.getBMitdrucken());

				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KALK_PREIS] = position
						.getNKalkpreis();
				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KOMMENTAR_POSITION] = position
						.getCKommentar();
				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_I_SORT] = Helper
						.fitString2LengthAlignRight(position.getISort() + "",
								10, '0');

				// Artikel + Einheit holen
				com.lp.server.artikel.service.ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(position.getArtikelIId(),
								theClientDto);

				String sArtikelKommentar = "";
				if (artikelDto
						.getArtikelartCNr()
						.equals(com.lp.server.artikel.service.ArtikelFac.ARTIKELART_HANDARTIKEL)) {

					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKEL] = "";

				} else {
					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKEL] = einrueckung
							+ artikelDto.getCNr();

					Image imageKommentar = null;
					// Artikelkommentar kann Text oder Bild sein
					if (aKommentarDto != null) {
						for (int k = 0; k < aKommentarDto.length; k++) {
							String cDatenformat = aKommentarDto[k]
									.getDatenformatCNr().trim();
							if (cDatenformat
									.indexOf(MediaFac.DATENFORMAT_MIMETYPEART_TEXT) != -1) {
								sArtikelKommentar += "\n"
										+ aKommentarDto[k]
												.getArtikelkommentarsprDto()
												.getXKommentar();
							} else if (cDatenformat
									.indexOf(MediaFac.DATENFORMAT_MIMETYPEART_IMAGE) != -1) {
								// es wird hoechstens 1 Bild pro Belegart
								// gedruckt
								imageKommentar = Helper
										.byteArrayToImage(aKommentarDto[k]
												.getArtikelkommentarsprDto()
												.getOMedia());
								data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_IMAGE] = imageKommentar;
							}
						}
					}

				}
				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_GEWICHT] = artikelDto
						.getFGewichtkg();
				String sBezeichnung = artikelDto.getArtikelsprDto().getCBez();
				if (sArtikelKommentar != "") {
					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KOMMENTAR] = sArtikelKommentar;
				}
				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKELBEZEICHNUNG] = sBezeichnung;

				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKELZUSATZBEZEICHNUNG] = artikelDto
						.getArtikelsprDto().getCZbez();
				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKELZUSATZBEZEICHNUNG2] = artikelDto
						.getArtikelsprDto().getCZbez2();
				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_REFERENZNUMMER] = artikelDto
						.getCReferenznr();

				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_POSITION] = position
						.getCPosition();
				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_MONTAGEART] = position
						.getMontageartDto().getCBez();
				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_LFDNUMMER] = position
						.getILfdnummer();
				if (artikelDto.getArtikelsprDto() != null) {
					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KURZBEZEICHNUNG] = artikelDto
							.getArtikelsprDto().getCKbez();
				}

				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_INDEX] = artikelDto
						.getCIndex();
				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_REVISION] = artikelDto
						.getCRevision();

				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ZIELMENGENEINHEIT] = artikelDto
						.getEinheitCNr();

				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_WARENVERKEHRSNUMMER] = artikelDto
						.getCWarenverkehrsnummer();

				if (artikelDto.getLandIIdUrsprungsland() != null) {

					LandDto landDto = getSystemFac().landFindByPrimaryKey(
							artikelDto.getLandIIdUrsprungsland());
					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_URSPRUNGSLAND_LKZ] = landDto
							.getCLkz();
					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_URSPRUNGSLAND_NAME] = landDto
							.getCName();
				}

				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_HOCHSTELLEN] = "N";
				if (artikelDto.getMontageDto() != null
						&& Helper.short2boolean(artikelDto.getMontageDto()
								.getBHochstellen()) == true) {
					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_HOCHSTELLEN] = "J";
				}
				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_HOCHSETZEN] = "N";
				if (artikelDto.getMontageDto() != null
						&& Helper.short2boolean(artikelDto.getMontageDto()
								.getBHochsetzen()) == true) {
					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_HOCHSETZEN] = "J";
				}

				if (artikelDto.getMontageDto() != null) {
					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_RASTERLIEGEND] = artikelDto
							.getMontageDto().getFRasterliegend();
					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_RASTERSTEHEND] = artikelDto
							.getMontageDto().getFRasterstehend();
				}

				if (artikelDto.getHerstellerIId() != null) {
					HerstellerDto herstellerDto = getArtikelFac()
							.herstellerFindByPrimaryKey(
									artikelDto.getHerstellerIId(), theClientDto);

					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_HERSTELLER] = herstellerDto
							.getCNr();
					PartnerDto partnerDto = getPartnerFac()
							.partnerFindByPrimaryKey(
									herstellerDto.getPartnerIId(), theClientDto);
					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_HERSTELLER_NAME] = partnerDto
							.formatFixName1Name2();
				}

				if (position.getFDimension1() == null) {
					position.setFDimension1(new Float(1));
				} else if (position.getFDimension2() == null) {
					position.setFDimension2(new Float(1));
				}
				if (position.getFDimension3() == null) {
					position.setFDimension3(new Float(1));
				}

				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_MENGE] = position
						.getNMenge();

				// Positionseinheit nach Zieleinheit umrechnen
				BigDecimal preis = new BigDecimal(0);
				BigDecimal kundenpreis = new BigDecimal(0);

				if (iOptionPreis.intValue() == StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_PREIS_EINKAUFSPREIS) {
					ArtikellieferantDto artikellieferantDto = getArtikelFac()
							.getArtikelEinkaufspreis(artikelDto.getIId(),
									position.getNMenge(), sMandantenwaehrung,
									theClientDto);
					sPreis = "Lief1";
					if (artikellieferantDto != null
							&& artikellieferantDto.getLief1Preis() != null) {
						preis = artikellieferantDto.getLief1Preis();
					}

				} else if (iOptionPreis.intValue() == StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_PREIS_GESTEHUNGSSPREIS) {

					if (bGestpreisberechnungHauptlager == true) {

						preis = getLagerFac()
								.getGemittelterGestehungspreisEinesLagers(
										artikelDto.getIId(), hauptlagerIId,
										theClientDto);
					} else {
						preis = getLagerFac()
								.getGemittelterGestehungspreisAllerLaegerEinesMandanten(
										artikelDto.getIId(), theClientDto);

					}
					sPreis = "Gest";
				} else if (iOptionPreis.intValue() == StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_PREIS_VERKAUFSSPREIS) {
					VkPreisfindungEinzelverkaufspreisDto vkpreisDto = getVkPreisfindungFac()
							.getArtikeleinzelverkaufspreis(artikelDto.getIId(),
									null, theClientDto.getSMandantenwaehrung(),
									theClientDto);
					if (vkpreisDto != null
							&& vkpreisDto.getNVerkaufspreisbasis() != null) {
						preis = vkpreisDto.getNVerkaufspreisbasis();
					}
					sPreis = "VK";

					if (kdDto != null && mwstsatzDtoAktuell != null) {
						VkpreisfindungDto vkpreisfindungDto = getVkPreisfindungFac()
								.verkaufspreisfindung(
										artikelDto.getIId(),
										kdDto.getIId(),
										position.getNZielmenge(),
										new java.sql.Date(System
												.currentTimeMillis()),
										kdDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
										mwstsatzDtoAktuell.getIId(),
										theClientDto.getSMandantenwaehrung(),
										theClientDto);

						VerkaufspreisDto kundenVKPreisDto = Helper
								.getVkpreisBerechnet(vkpreisfindungDto);
						if (kundenVKPreisDto != null) {
							kundenpreis = kundenVKPreisDto.nettopreis;
							data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_MATERIALZUSCHLAG] = kundenVKPreisDto.bdMaterialzuschlag;
						}

					}

				}

				// Wenn Stuecklsite, dann wird fuer den Kopf kein Preis
				// angedruckt, da ja sonst alles Doppelt gerechnet wird
				if (!struktur.isBStueckliste()) {
					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_PREIS] = preis;
					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KUNDENPREIS] = kundenpreis;
					data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_WERT] = position
							.getNZielmenge().multiply(preis);
				}
				data[row][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ZIELMENGE] = position
						.getNZielmenge();

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

			row++;
		}

		if (!bUnterstklstrukurBelassen) {

			// Nach Fertigungsgruppe sortieren
			for (int k = data.length - 1; k > 0; --k) {
				for (int j = 0; j < k; ++j) {
					Object[] a1 = (Object[]) data[j];
					Object[] a2 = (Object[]) data[j + 1];

					if (((String) a1[REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKEL])
							.compareTo((String) a2[REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKEL]) > 0) {
						data[j] = a2;
						data[j + 1] = a1;

					}

				}

			}

		}

		parameter.put("P_WAEHRUNG", sMandantenwaehrung);
		parameter.put("P_PREIS", sPreis);
		if (bMitStuecklistenkommentar.booleanValue() == true) {
			parameter.put("P_KOMMENTAR",
					Helper.formatStyledTextForJasper(dto.getXKommentar()));
		}
		parameter.put("P_STUECKLISTE", dto.getArtikelDto().getCNr());
		parameter.put("P_ERFASSUNGSFAKTOR", dto.getIErfassungsfaktor());
		parameter.put("P_STUECKLISTEBEZEICHNUNG", dto.getArtikelDto()
				.formatBezeichnung());
		if (dto.getArtikelDto().getArtikelsprDto() != null) {
			parameter.put("P_STUECKLISTEKURZBEZEICHNUNG", dto.getArtikelDto()
					.getArtikelsprDto().getCKbez());
			parameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG", dto.getArtikelDto()
					.getArtikelsprDto().getCZbez());
			parameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG2", dto
					.getArtikelDto().getArtikelsprDto().getCZbez2());
		}
		parameter.put("P_ANLEGEN", dto.getTAnlegen());
		parameter.put("P_AENDERN", dto.getTAendern());
		try {
			if (dto.getArtikelDto().getLandIIdUrsprungsland() != null) {
				LandDto landDto = getSystemFac().landFindByPrimaryKey(
						dto.getArtikelDto().getLandIIdUrsprungsland());

				String s = landDto.getCName();
				if (landDto.getEUMitglied() != null
						&& new Date().after(landDto.getEUMitglied())) {
					s += " ("
							+ getTextRespectUISpr("lp.eumitglied",
									theClientDto.getMandant(),
									theClientDto.getLocUi()) + ")";
				}

				parameter.put("P_URSPRUNGSLAND", s);

			}
			// Zeichnungsnummer
			StuecklisteeigenschaftDto[] stuecklisteeigenschaftDtos = getStuecklisteFac()
					.stuecklisteeigenschaftFindByStuecklisteIId(dto.getIId());
			ArrayList<Object[]> al = new ArrayList<Object[]>();
			for (int i = 0; i < stuecklisteeigenschaftDtos.length; i++) {
				StuecklisteeigenschaftDto stuecklisteeigenschaftDto = stuecklisteeigenschaftDtos[i];

				Object[] o = new Object[2];
				o[0] = stuecklisteeigenschaftDto
						.getStuecklisteeigenschaftartDto().getCBez();
				o[1] = stuecklisteeigenschaftDto.getCBez();
				al.add(o);
			}

			if (stuecklisteeigenschaftDtos.length > 0) {
				String[] fieldnames = new String[] { "F_EIGENSCHAFTART",
						"F_BEZEICHNUNG" };
				Object[][] dataSub = new Object[al.size()][fieldnames.length];
				dataSub = (Object[][]) al.toArray(dataSub);

				parameter.put("DATENSUBREPORT", new LPDatenSubreport(dataSub,
						fieldnames));
			}

		} catch (RemoteException ex4) {
			throwEJBExceptionLPRespectOld(ex4);
		}

		// Nun nach Kriterium 1/2/3 sortieren PJ 06/2418
		if (!bUnterstuecklistenEinbinden) {
			data = sortiereStuecklistendruck(data,
					iOptionSortierungStuecklisteGesamt1,
					iOptionSortierungStuecklisteGesamt2,
					iOptionSortierungStuecklisteGesamt3, true);
		}

		// PJ15813 Ersatztypen hinzufuegen
		if (bGleichePositionenZusammenfassen == false) {

			ArrayList alDaten = new ArrayList();
			for (int i = 0; i < data.length; i++) {

				alDaten.add(data[i]);

				PosersatzDto[] posersatzDtos = getStuecklisteFac()
						.posersatzFindByStuecklistepositionIId(
								(Integer) data[i][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_STUECKLISTEPOSITION_I_ID]);

				for (int j = 0; j < posersatzDtos.length; j++) {
					Object[] neueZeile = data[i].clone();

					String einrueckung = "";
					for (int k = 0; k < (Integer) data[i][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_I_EBENE]; k++) {
						einrueckung = einrueckung + "   ";
					}

					com.lp.server.artikel.service.ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(
									posersatzDtos[j].getArtikelIIdErsatz(),
									theClientDto);

					neueZeile[REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ERSATZARTIKEL] = einrueckung
							+ artikelDto.getCNr();

					neueZeile[REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ERSATZARTIKEL_BEZEICHNUNG] = artikelDto
							.formatBezeichnung();

					if (artikelDto.getArtikelsprDto() != null) {
						neueZeile[REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ERSATZARTIKEL_ZUSATZBEZEICHNUNG] = artikelDto
								.getArtikelsprDto().getCZbez();
						neueZeile[REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ERSATZARTIKEL_ZUSATZBEZEICHNUNG2] = artikelDto
								.getArtikelsprDto().getCZbez2();
					}

					neueZeile[REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ZIELMENGE] = new BigDecimal(
							0);

					alDaten.add(neueZeile);

				}

			}

			data = new Object[alDaten.size()][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ANZAHL_SPALTEN];
			data = (Object[][]) alDaten.toArray(data);

		}

		initJRDS(parameter, StuecklisteReportFac.REPORT_MODUL,
				StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEIN_MITPREIS,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printLoseAktualisiert(
			TreeMap<String, Object[]> tmAufgeloesteFehlmengen,
			TheClientDto theClientDto) {

		Iterator<?> it = tmAufgeloesteFehlmengen.keySet().iterator();

		data = new Object[tmAufgeloesteFehlmengen.size()][5];
		int i = 0;
		while (it.hasNext()) {
			Object key = it.next();

			data[i] = tmAufgeloesteFehlmengen.get(key);

			i++;
		}

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		index = -1;
		sAktuellerReport = StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT;

		initJRDS(parameter, StuecklisteReportFac.REPORT_MODUL,
				StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printStuecklisteAllgemein(Integer stuecklisteIId,
			Boolean bMitPositionskommentar, Boolean bMitStuecklistenkommentar,
			Boolean bUnterstuecklistenEinbinden,
			Boolean bGleichePositionenZusammenfassen,
			Integer iOptionSortierungUnterstuecklisten,
			boolean bUnterstklstrukurBelassen, TheClientDto theClientDto,
			Integer iOptionSortierungStuecklisteGesamt1,
			Integer iOptionSortierungStuecklisteGesamt2,
			Integer iOptionSortierungStuecklisteGesamt3) {

		if (stuecklisteIId == null || bMitPositionskommentar == null
				|| bMitStuecklistenkommentar == null
				|| bUnterstuecklistenEinbinden == null
				|| bGleichePositionenZusammenfassen == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"stuecklisteIId == null || bMitPositionskommentar == null || bMitStuecklistenkommentar == null || bUnterstuecklistenEinbinden == null || bGleichePositionenZusammenfassen == null"));
		}
		if (bUnterstuecklistenEinbinden.booleanValue() == true
				&& iOptionSortierungUnterstuecklisten == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"bUnterstuecklistenEinbinden.booleanValue()==true && iOptionSortierungUnterstuecklisten==null"));
		}

		index = -1;
		sAktuellerReport = StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEIN_OHNEPREIS;

		List<?> m = null;
		try {
			m = getStuecklisteFac().getStrukturDatenEinerStueckliste(
					stuecklisteIId, theClientDto,
					iOptionSortierungUnterstuecklisten.intValue(), 0, null,
					bUnterstuecklistenEinbinden.booleanValue(),
					bGleichePositionenZusammenfassen.booleanValue(),
					new BigDecimal(1), null, bUnterstklstrukurBelassen);
		} catch (RemoteException ex2) {
			throwEJBExceptionLPRespectOld(ex2);
		}

		Iterator<?> it = m.listIterator();
		data = new Object[m.size()][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ANZAHL_SPALTEN];
		int row = 0;
		while (it.hasNext()) {
			StuecklisteMitStrukturDto struktur = (StuecklisteMitStrukturDto) it
					.next();
			StuecklistepositionDto position = struktur
					.getStuecklistepositionDto();

			// Artikelkommentar Text und Bild
			ArtikelkommentarDto[] aKommentarDto = null;
			try {
				aKommentarDto = getArtikelkommentarFac()
						.artikelkommentardruckFindByArtikelIIdBelegartCNr(
								position.getArtikelIId(),
								LocaleFac.BELEGART_STUECKLISTE,
								theClientDto.getLocUiAsString(), theClientDto);
			} catch (RemoteException ex3) {
			} catch (EJBExceptionLP ex3) {
			}

			String einrueckung = "";
			for (int i = 0; i < struktur.getIEbene(); i++) {
				einrueckung = einrueckung + "   ";
			}
			try {

				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_I_EBENE] = struktur
						.getIEbene();

				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_STUECKLISTEPOSITION_I_ID] = position
						.getIId();

				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_I_SORT] = Helper
						.fitString2LengthAlignRight(position.getISort() + "",
								10, '0');

				com.lp.server.artikel.service.ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKey(position.getArtikelIId(),
								theClientDto);

				String sArtikelKommentar = "";
				if (artikelDto.getArtikelartCNr() != null
						&& artikelDto
								.getArtikelartCNr()
								.equals(com.lp.server.artikel.service.ArtikelFac.ARTIKELART_HANDARTIKEL)) {

					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKEL] = "";

				} else {
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKEL] = einrueckung
							+ artikelDto.getCNr();

					Image imageKommentar = null;
					// Artikelkommentar kann Text oder Bild sein
					if (aKommentarDto != null) {
						for (int k = 0; k < aKommentarDto.length; k++) {
							String cDatenformat = aKommentarDto[k]
									.getDatenformatCNr().trim();
							if (cDatenformat
									.indexOf(MediaFac.DATENFORMAT_MIMETYPEART_TEXT) != -1) {
								sArtikelKommentar += "\n"
										+ aKommentarDto[k]
												.getArtikelkommentarsprDto()
												.getXKommentar();
							} else if (cDatenformat
									.indexOf(MediaFac.DATENFORMAT_MIMETYPEART_IMAGE) != -1) {
								// es wird hoechstens 1 Bild pro Belegart
								// gedruckt
								imageKommentar = Helper
										.byteArrayToImage(aKommentarDto[k]
												.getArtikelkommentarsprDto()
												.getOMedia());
								data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_IMAGE] = imageKommentar;
							}
						}
					}

				}

				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_INDEX] = artikelDto
						.getCIndex();
				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_REVISION] = artikelDto
						.getCRevision();
				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_GEWICHT] = artikelDto
						.getFGewichtkg();
				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_WARENVERKEHRSNUMMER] = artikelDto
						.getCWarenverkehrsnummer();

				if (artikelDto.getLandIIdUrsprungsland() != null) {

					LandDto landDto = getSystemFac().landFindByPrimaryKey(
							artikelDto.getLandIIdUrsprungsland());
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_URSPRUNGSLAND_LKZ] = landDto
							.getCLkz();
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_URSPRUNGSLAND_NAME] = landDto
							.getCName();
				}

				String sBezeichnung = artikelDto.getArtikelsprDto().getCBez();
				if (sArtikelKommentar != "") {
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_KOMMENTAR] = sArtikelKommentar;
				}
				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKELBEZEICHNUNG] = sBezeichnung;

				String sZusatzbez = artikelDto.getArtikelsprDto().getCZbez();
				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKELZUSATZBEZEICHNUNG] = sZusatzbez;
				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKELZUSATZBEZEICHNUNG2] = artikelDto
						.getArtikelsprDto().getCZbez2();
				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_REFERENZNUMMER] = artikelDto
						.getCReferenznr();

				if (artikelDto.getArtikelsprDto() != null) {
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_KURZBEZEICHNUNG] = artikelDto
							.getArtikelsprDto().getCKbez();
				}
				EinheitDto einheitDto = getSystemFac().einheitFindByPrimaryKey(
						position.getEinheitCNr(), theClientDto);

				int dimension = einheitDto.getIDimension().intValue();

				if (position.getFDimension1() == null) {
					position.setFDimension1(new Float(1));
				} else if (position.getFDimension2() == null) {
					position.setFDimension2(new Float(1));
				}
				if (position.getFDimension3() == null) {
					position.setFDimension3(new Float(1));
				}

				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_MENGE] = position
						.getNMenge();

				if (dimension == 1) {
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_DIMENSION] = "L"
							+ Helper.formatZahl(position.getFDimension1(), 2,
									theClientDto.getLocUi());
				} else if (dimension == 2) {
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_DIMENSION] = "B"
							+ Helper.formatZahl(position.getFDimension1(), 2,
									theClientDto.getLocUi())
							+ "/T"
							+ Helper.formatZahl(position.getFDimension2(), 2,
									theClientDto.getLocUi());

				} else if (dimension == 3) {
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_DIMENSION] = "B"
							+ Helper.formatZahl(position.getFDimension1(), 2,
									theClientDto.getLocUi())
							+ "/T"
							+ Helper.formatZahl(position.getFDimension2(), 2,
									theClientDto.getLocUi())
							+ "/H"
							+ Helper.formatZahl(position.getFDimension3(), 2,
									theClientDto.getLocUi());

				}

				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ZIELMENGE] = position
						.getNZielmenge();

				if (dimension != 0) {
					// Wenn Dimension der Einheit <> 0, dann wird Stk als
					// Ausgangseinheit eingetragen
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_STUECKLISTENEINHEIT] = SystemFac.EINHEIT_STUECK
							.trim();
				} else {
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_STUECKLISTENEINHEIT] = position
							.getEinheitCNr();
				}

				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ZIELMENGENEINHEIT] = artikelDto
						.getEinheitCNr();
				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_POSITION] = position
						.getCPosition();
				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_LFDNUMMER] = position
						.getILfdnummer();
				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_MONTAGEART] = position
						.getMontageartDto().getCBez();
				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_HOCHSTELLEN] = "N";
				if (artikelDto.getMontageDto() != null
						&& Helper.short2boolean(artikelDto.getMontageDto()
								.getBHochstellen()) == true) {
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_HOCHSTELLEN] = "J";
				}
				data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_HOCHSETZEN] = "N";
				if (artikelDto.getMontageDto() != null
						&& Helper.short2boolean(artikelDto.getMontageDto()
								.getBHochsetzen()) == true) {
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_HOCHSETZEN] = "J";
				}

				if (artikelDto.getMontageDto() != null) {
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_RASTERLIEGEND] = artikelDto
							.getMontageDto().getFRasterliegend();
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_RASTERSTEHEND] = artikelDto
							.getMontageDto().getFRasterstehend();
				}

				if (artikelDto.getHerstellerIId() != null) {
					HerstellerDto herstellerDto = getArtikelFac()
							.herstellerFindByPrimaryKey(
									artikelDto.getHerstellerIId(), theClientDto);

					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_HERSTELLER] = herstellerDto
							.getCNr();
					PartnerDto partnerDto = getPartnerFac()
							.partnerFindByPrimaryKey(
									herstellerDto.getPartnerIId(), theClientDto);
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_HERSTELLER_NAME] = partnerDto
							.formatFixName1Name2();
				}

				if (bMitPositionskommentar.booleanValue() == true) {
					data[row][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_POSITIONSKOMMENTAR] = position
							.getCKommentar();
				}
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

			row++;
		}

		if (!bUnterstklstrukurBelassen) {

			// Nach Fertigungsgruppe sortieren
			for (int k = data.length - 1; k > 0; --k) {
				for (int j = 0; j < k; ++j) {
					Object[] a1 = (Object[]) data[j];
					Object[] a2 = (Object[]) data[j + 1];

					if (iOptionSortierungUnterstuecklisten == StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_ARTIKELNR) {

						if (((String) a1[REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKEL])
								.compareTo((String) a2[REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKEL]) > 0) {
							data[j] = a2;
							data[j + 1] = a1;

						}
					} else if (iOptionSortierungUnterstuecklisten == StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_POSITION) {
						if (((String) a1[REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_POSITION])
								.compareTo((String) a2[REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_POSITION]) > 0) {
							data[j] = a2;
							data[j + 1] = a1;

						}

					}
				}

			}

		}

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		StuecklisteDto dto = getStuecklisteFac().stuecklisteFindByPrimaryKey(
				stuecklisteIId, theClientDto);

		parameter.put("P_STUECKLISTE", dto.getArtikelDto().getCNr());
		parameter.put("P_ERFASSUNGSFAKTOR", dto.getIErfassungsfaktor());
		parameter.put("P_ANLEGEN", dto.getTAnlegen());
		parameter.put("P_AENDERN", dto.getTAendern());

		parameter.put("P_STUECKLISTEBEZEICHNUNG", dto.getArtikelDto()
				.formatBezeichnung());
		if (dto.getArtikelDto().getArtikelsprDto() != null) {
			parameter.put("P_STUECKLISTEKURZBEZEICHNUNG", dto.getArtikelDto()
					.getArtikelsprDto().getCKbez());
			parameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG", dto.getArtikelDto()
					.getArtikelsprDto().getCZbez());
			parameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG2", dto
					.getArtikelDto().getArtikelsprDto().getCZbez2());
		}
		try {
			// Zeichnungsnummer
			StuecklisteeigenschaftDto[] stuecklisteeigenschaftDtos = getStuecklisteFac()
					.stuecklisteeigenschaftFindByStuecklisteIId(dto.getIId());
			ArrayList<Object[]> al = new ArrayList<Object[]>();
			for (int i = 0; i < stuecklisteeigenschaftDtos.length; i++) {
				StuecklisteeigenschaftDto stuecklisteeigenschaftDto = stuecklisteeigenschaftDtos[i];

				Object[] o = new Object[2];
				o[0] = stuecklisteeigenschaftDto
						.getStuecklisteeigenschaftartDto().getCBez();
				o[1] = stuecklisteeigenschaftDto.getCBez();
				al.add(o);
			}

			if (stuecklisteeigenschaftDtos.length > 0) {
				String[] fieldnames = new String[] { "F_EIGENSCHAFTART",
						"F_BEZEICHNUNG" };
				Object[][] dataSub = new Object[al.size()][fieldnames.length];
				dataSub = (Object[][]) al.toArray(dataSub);

				parameter.put("DATENSUBREPORT", new LPDatenSubreport(dataSub,
						fieldnames));
			}

		} catch (RemoteException ex4) {
			throwEJBExceptionLPRespectOld(ex4);
		}
		if (bMitStuecklistenkommentar.booleanValue() == true) {
			parameter.put("P_KOMMENTAR",
					Helper.formatStyledTextForJasper(dto.getXKommentar()));
		}

		// Nun nach Kriterium 1/2/3 sortieren PJ 06/2418
		if (!bUnterstuecklistenEinbinden) {
			data = sortiereStuecklistendruck(data,
					iOptionSortierungStuecklisteGesamt1,
					iOptionSortierungStuecklisteGesamt2,
					iOptionSortierungStuecklisteGesamt3, false);
		}

		// PJ15813 Ersatztypen hinzufuegen
		if (bGleichePositionenZusammenfassen == false) {

			ArrayList alDaten = new ArrayList();
			for (int i = 0; i < data.length; i++) {

				alDaten.add(data[i]);

				PosersatzDto[] posersatzDtos = getStuecklisteFac()
						.posersatzFindByStuecklistepositionIId(
								(Integer) data[i][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_STUECKLISTEPOSITION_I_ID]);

				for (int j = 0; j < posersatzDtos.length; j++) {
					Object[] neueZeile = data[i].clone();

					String einrueckung = "";
					for (int k = 0; k < (Integer) data[i][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_I_EBENE]; k++) {
						einrueckung = einrueckung + "   ";
					}

					com.lp.server.artikel.service.ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(
									posersatzDtos[j].getArtikelIIdErsatz(),
									theClientDto);

					neueZeile[REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ERSATZARTIKEL] = einrueckung
							+ artikelDto.getCNr();

					neueZeile[REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ERSATZARTIKEL_BEZEICHNUNG] = artikelDto
							.formatBezeichnung();

					if (artikelDto.getArtikelsprDto() != null) {
						neueZeile[REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ERSATZARTIKEL_ZUSATZBEZEICHNUNG] = artikelDto
								.getArtikelsprDto().getCZbez();
						neueZeile[REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ERSATZARTIKEL_ZUSATZBEZEICHNUNG2] = artikelDto
								.getArtikelsprDto().getCZbez2();
					}

					neueZeile[REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ZIELMENGE] = new BigDecimal(
							0);
					neueZeile[REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_MENGE] = new BigDecimal(
							0);

					alDaten.add(neueZeile);

				}

			}

			data = new Object[alDaten.size()][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ANZAHL_SPALTEN];
			data = (Object[][]) alDaten.toArray(data);

		}

		initJRDS(parameter, StuecklisteReportFac.REPORT_MODUL,
				StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEIN_OHNEPREIS,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	/**
	 * Methode fuer JRDataSource
	 * 
	 * @return boolean
	 * @throws JRException
	 */
	public boolean next() throws JRException {

		index++;
		return (index < data.length);

	}

	public Object[][] sortiereStuecklistendruck(Object[][] dataTemp,
			int iSortierung1, int iSortierung2, int iSortierung3,
			boolean bAllgemeinMitPreis) {

		for (int k = dataTemp.length - 1; k > 0; --k) {
			for (int j = 0; j < k; ++j) {
				Object[] a1 = (Object[]) dataTemp[j];
				Object[] a2 = (Object[]) dataTemp[j + 1];

				a1 = befuelleFelder(a1, iSortierung1, bAllgemeinMitPreis);
				a1 = befuelleFelder(a1, iSortierung2, bAllgemeinMitPreis);
				a1 = befuelleFelder(a1, iSortierung3, bAllgemeinMitPreis);

				a2 = befuelleFelder(a2, iSortierung1, bAllgemeinMitPreis);
				a2 = befuelleFelder(a2, iSortierung2, bAllgemeinMitPreis);
				a2 = befuelleFelder(a2, iSortierung3, bAllgemeinMitPreis);

				int v = (a1[iSortierung1].toString().compareTo(a2[iSortierung1]
						.toString()));

				if (v == 0) {
					v = (a1[iSortierung2].toString().compareTo(a2[iSortierung2]
							.toString()));

					if (v == 0) {
						v = (a1[iSortierung3].toString()
								.compareTo(a2[iSortierung3].toString()));
					}

				}

				if (v > 0) {
					dataTemp[j] = a2;
					dataTemp[j + 1] = a1;

				}

			}

		}

		return dataTemp;

	}

	private Object[] befuelleFelder(Object[] zeile, int iSortierung,
			boolean bAllgemeinMitPreis) {

		if (zeile[iSortierung] == null) {
			if (bAllgemeinMitPreis) {
				if (iSortierung == StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEINMITPREIS_LFDNUMMER) {
					zeile[iSortierung] = new Integer(0);
					zeile[iSortierung] = new Integer(0);
				} else {
					zeile[iSortierung] = "";
					zeile[iSortierung] = "";
				}
			} else {
				if (iSortierung == StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_LFDNUMMER) {
					zeile[iSortierung] = new Integer(0);
					zeile[iSortierung] = new Integer(0);
				} else {
					zeile[iSortierung] = "";
					zeile[iSortierung] = "";
				}
			}
		}
		return zeile;

	}

	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;

		String fieldName = field.getName();

		if (sAktuellerReport
				.equals(StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEIN_OHNEPREIS)) {
			if ("Artikel".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKEL];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKELBEZEICHNUNG];
			} else if ("Artikelzusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKELZUSATZBEZEICHNUNG];
			} else if ("Artikelzusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKELZUSATZBEZEICHNUNG2];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_STUECKLISTENEINHEIT];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_MENGE];
			} else if ("Position".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_POSITION];
			} else if ("Preis".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_PREIS];
			} else if ("Dimension".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_DIMENSION];
			} else if ("Zielmenge".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ZIELMENGE];
			} else if ("Zieleinheit".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ZIELMENGENEINHEIT];
			} else if ("Montageart".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_MONTAGEART];
			} else if ("Hochstellen".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_HOCHSTELLEN];
			} else if ("Hochsetzen".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_HOCHSETZEN];
			} else if ("F_IMAGE".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_IMAGE];
			} else if ("Kurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_KURZBEZEICHNUNG];
			} else if ("Referenznummer".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_REFERENZNUMMER];
			} else if ("Rasterliegend".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_RASTERLIEGEND];
			} else if ("Rasterstehend".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_RASTERSTEHEND];
			} else if ("Hersteller".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_HERSTELLER];
			} else if ("Herstellername".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_HERSTELLER_NAME];
			} else if ("Positionskommentar".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_POSITIONSKOMMENTAR];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_KOMMENTAR];
			} else if ("Lfdnummer".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_LFDNUMMER];
			} else if ("Index".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_INDEX];
			} else if ("Revision".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_REVISION];
			} else if ("Warenverkehrsnummer".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_WARENVERKEHRSNUMMER];
			} else if ("UrsprungslandLkz".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_URSPRUNGSLAND_LKZ];
			} else if ("UrsprungslandName".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_URSPRUNGSLAND_NAME];
			} else if ("Ersatzartikel".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ERSATZARTIKEL];
			} else if ("Ersatzartikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ERSATZARTIKEL_BEZEICHNUNG];
			} else if ("Ersatzartikelzusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ERSATZARTIKEL_ZUSATZBEZEICHNUNG];
			} else if ("Ersatzartikelzusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ERSATZARTIKEL_ZUSATZBEZEICHNUNG2];
			} else if ("Artikelgewicht".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_GEWICHT];
			}
		} else if (sAktuellerReport
				.equals(StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEIN_MITPREIS)) {
			if ("Artikel".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKEL];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKELBEZEICHNUNG];
			} else if ("Artikelzusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKELZUSATZBEZEICHNUNG];
			} else if ("Artikelzusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKELZUSATZBEZEICHNUNG2];
			} else if ("Zieleinheit".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ZIELMENGENEINHEIT];
			} else if ("Zielmenge".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ZIELMENGE];
			} else if ("Wert".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_WERT];
			} else if ("Preis".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_PREIS];
			} else if ("Kundenpreis".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KUNDENPREIS];
			} else if ("F_IMAGE".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_IMAGE];
			} else if ("Kurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KURZBEZEICHNUNG];
			} else if ("Rasterliegend".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_RASTERLIEGEND];
			} else if ("Referenznummer".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_REFERENZNUMMER];
			} else if ("Rasterstehend".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_RASTERSTEHEND];
			} else if ("Hochstellen".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_HOCHSTELLEN];
			} else if ("Hochsetzen".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_HOCHSETZEN];
			} else if ("Hersteller".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_HERSTELLER];
			} else if ("Herstellername".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_HERSTELLER_NAME];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KOMMENTAR];
			} else if ("Montageart".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_MONTAGEART];
			} else if ("Position".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_POSITION];
			} else if ("Lfdnummer".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_LFDNUMMER];
			} else if ("Index".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_INDEX];
			} else if ("Revision".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_REVISION];
			} else if ("Warenverkehrsnummer".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_WARENVERKEHRSNUMMER];
			} else if ("UrsprungslandLkz".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_URSPRUNGSLAND_LKZ];
			} else if ("UrsprungslandName".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_URSPRUNGSLAND_NAME];
			} else if ("Ersatzartikel".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ERSATZARTIKEL];
			} else if ("Ersatzartikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ERSATZARTIKEL_BEZEICHNUNG];
			} else if ("Ersatzartikelzusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ERSATZARTIKEL_ZUSATZBEZEICHNUNG];
			} else if ("Ersatzartikelzusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ERSATZARTIKEL_ZUSATZBEZEICHNUNG2];
			} else if ("Artikelgewicht".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_GEWICHT];
			} else if ("Materialzuschlag".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_MATERIALZUSCHLAG];
			} else if ("AufBelegMitdrucken".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_AUF_BELEG_MITDRUCKEN];
			} else if ("KalkPreis".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KALK_PREIS];
			} else if ("KommentarPosition".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KOMMENTAR_POSITION];
			}

		} else if (sAktuellerReport
				.equals(StuecklisteReportFac.REPORT_STUECKLISTE_ARBEITSPLAN)) {
			if ("Artikel".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_ARTIKEL];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_ARTIKELBEZEICHNUNG];
			} else if ("Artikelzusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_ARTIKELZUSATZBEZEICHNUNG];
			} else if ("Artikelzusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_ARTIKELZUSATZBEZEICHNUNG2];
			} else if ("Arbeitsgang".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_ARBEITSGANG];
			} else if ("Stueckzeit".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_STUECKZEIT];
			} else if ("Ruestzeit".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_RUESTZEIT];
			} else if ("Gesamtzeit".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_GESAMTZEIT];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_KOMMENTAR];
			} else if ("Preis".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_PREIS];
			} else if ("Agart".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_AGART];
			} else if ("Aufspannung".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_AUFSPANNUNG];
			} else if ("Unterarbeitsgang".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_UNTERARBEITSGANG];
			} else if ("Maschine".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_MASCHINE];
			} else if ("Maschinenkosten".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_KOSTEN_MASCHINE];
			} else if ("FremdmaterialArtikel".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_FREMDMATERIAL_ARTIKEL];
			} else if ("FremdmaterialArtikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_FREMDMATERIAL_ARTIKELBEZEICHNUNG];
			} else if ("FremdmaterialSollmenge".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_FREMDMATERIAL_SOLLMENGE];
			} else if ("FremdmaterialArtikelkurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_FREMDMATERIAL_ARTIKELKURZBEZEICHNUNG];
			} else if ("FremdmaterialArtikelzusatzbezeichnung"
					.equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_FREMDMATERIAL_ARTIKELZUSATZBEZEICHNUNG];
			} else if ("FremdmaterialArtikelzusatzbezeichnung2"
					.equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_FREMDMATERIAL_ARTIKELZUSATZBEZEICHNUNG2];
			} else if ("Revision".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_REVISION];
			} else if ("Index".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_INDEX];
			} else if ("Langtext".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSPLAN_LANGTEXT];
			}
		} else if (sAktuellerReport
				.equals(StuecklisteReportFac.REPORT_STUECKLISTE_AUSGABESTUECKLSITE)) {
			if ("Artikel".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ARTIKEL];
			} else if ("Lagerort".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_LAGERORT];
			} else if ("F_ARTIKELBILD".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_F_ARTIKELBILD];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_BEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ZUSATZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ZUSATZBEZEICHNUNG2];
			} else if ("Kurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_KURZBEZEICHNUNG];
			} else if ("Montageart".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_MONTAGEART];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ZIELMENGE];
			} else if ("Mengeneinheit".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ZIELMENGENEINHEIT];
			} else if ("Lagerstand".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_LAGERSTAND];
			} else if ("Verfuegbar".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_VERFUEGBAR];
			} else if ("Bestellt".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_BESTELLT];
			} else if ("Referenznummer".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_REFERENZNUMMER];
			} else if ("Revision".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_REVISION];
			} else if ("Index".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_INDEX];
			} else if ("Ersatzartikel".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ERSATZARTIKEL];
			} else if ("Ersatzartikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_ERSATZARTIKEL_BEZEICHNUNG];
			} else if ("InFertigung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_AUSGABESTUECKLISTE_IN_FERTIGUNG];
			}
		} else if (sAktuellerReport
				.equals(StuecklisteReportFac.REPORT_STUECKLISTE_GESAMTKALKULATION)) {
			if ("Artikel".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_ARTIKEL];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_ARTIKELBEZEICHNUNG];
			} else if ("Gestpreis".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_GESTPREIS];
			} else if ("Gestwert".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_GESTWERT];
			} else if ("Lief1preis".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_LIEF1PREIS];
			} else if ("Fixkosten".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_FIXKOSTEN];
			} else if ("Liefwert".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_LIEF1PREISGESAMT];
			} else if ("Wiederbeschaffungszeit".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_WIEDERBESCHAFFUNGSZEIT];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_MENGE];
			} else if ("Mengeneinheit".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_MENGENEINHEIT];
			} else if ("Stueckzeit".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_STUECKZEIT];
			} else if ("Ruestzeit".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_RUESTZEIT];
			} else if ("Gestpreiswennstueckliste".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_GESTPREISWENNSTUECKLISTE];
			} else if ("Summegestpreispositionen".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_SUMMEGESTPREISPOSITIONENWENNSTUECKLISTE];
			} else if ("Stuecklistenart".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_STKLART];
			} else if ("Gestpreiseweichenab".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_GESTPREISEWEICHENAB];
			} else if ("Durchlaufzeit".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_DURCHLAUFZEIT];
			} else if ("Kalkpreis".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_KALKPREIS];
			} else if ("Kalkwert".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_KALKWERT];
			} else if ("Vkpreis".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_VKPREIS];
			} else if ("Materialzuschlag".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_MATERIALZUSCHLAG];
			} else if ("Vkwert".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_VKWERT];
			} else if ("Mindestdeckungsbeitrag".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_MINDESTDECKUNGSBEITRAG];
			} else if ("Artikelgewicht".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_GEWICHT];
			} else if ("ArtikelMaterialgewicht".equals(fieldName)) {
				value = data[index][REPORT_GESAMTKALKULATION_MATERIALGEWICHT];
			}
		} else if (sAktuellerReport
				.equals(StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT)) {
			if ("Artikel".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_LOSEAKTUALISIERT_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_LOSEAKTUALISIERT_BEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_LOSEAKTUALISIERT_ZUSATZBEZEICHNUNG];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_LOSEAKTUALISIERT_EINHEIT];
			} else if ("KorrekturAusgabemenge".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_AUSGABEMENGE];
			} else if ("KorrekturSollmenge".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_SOLLMENGE];
			} else if ("Bemerkung".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_LOSEAKTUALISIERT_BEMERKUNG];
			} else if ("Los".equals(fieldName)) {
				value = data[index][REPORT_STUECKLISTE_LOSEAKTUALISIERT_LOSNUMMER];
			}
		}
		return value;
	}

	/**
	 * Hole Stuecklisteneigenschaften fuer einen Artikel. Keys in Hashtable sind
	 * StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_INDEX und
	 * StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_MATERIALPLATZ.
	 * 
	 * @param iIdStklIDI
	 *            Integer StuecklisteId
	 * @param sMandantCNr
	 *            String
	 * @param theClientDto der aktuelle Benutzer
	 * @return Hashtable
	 * @throws EJBExceptionLP
	 */
	public Hashtable getStuecklisteEigenschaften(Integer iIdStklIDI,
			String sMandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Hashtable<String, String> daten = new Hashtable<String, String>();
		;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			Criteria crit = session
					.createCriteria(FLRStuecklisteeigenschaft.class);
			Criteria cStueckliste = crit.createCriteria("flrstueckliste");
			Criteria cArt = crit.createCriteria("flrstuecklisteeigenschaftart");

			cStueckliste.add(Restrictions.eq("i_id", iIdStklIDI));
			cStueckliste.add(Restrictions.eq("mandant_c_nr", sMandantCNr));

			List<?> resultList = crit.list();
			Iterator<?> it = resultList.iterator();
			while (it.hasNext()) {
				FLRStuecklisteeigenschaft flr = (FLRStuecklisteeigenschaft) it
						.next();
				if (flr.getFlrstuecklisteeigenschaftart()
						.getC_bez()
						.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_INDEX)) {
					daten.put(
							StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_INDEX,
							flr.getC_bez());
				}
				if (flr.getFlrstuecklisteeigenschaftart()
						.getC_bez()
						.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_MATERIALPLATZ)) {
					daten.put(
							StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_MATERIALPLATZ,
							flr.getC_bez());
				}
			}

		} finally {
			try {
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

}
