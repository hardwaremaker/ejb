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
package com.lp.server.instandhaltung.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.fertigung.fastlanereader.generated.FLRWiederholendelose;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.instandhaltung.fastlanereader.generated.FLRWartungsschritte;
import com.lp.server.instandhaltung.service.GeraetDto;
import com.lp.server.instandhaltung.service.InstandhaltungReportFac;
import com.lp.server.instandhaltung.service.StandortDto;
import com.lp.server.instandhaltung.service.WartungslisteDto;
import com.lp.server.instandhaltung.service.WartungsschritteDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
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
public class InstandhaltungReportFacBean extends LPReport implements
		InstandhaltungReportFac {

	private String sAktuellerReport = null;
	private Object[][] data = null;

	private static int REPORT_WARTUNGSPLAN_KUNDE = 0;
	private static int REPORT_WARTUNGSPLAN_STANDORT = 1;
	private static int REPORT_WARTUNGSPLAN_HALLE = 2;
	private static int REPORT_WARTUNGSPLAN_ANLAGE = 3;
	private static int REPORT_WARTUNGSPLAN_MASCHINE = 4;
	private static int REPORT_WARTUNGSPLAN_GERAETESNR = 5;
	private static int REPORT_WARTUNGSPLAN_FABRIKAT = 6;
	private static int REPORT_WARTUNGSPLAN_VERSORGUNSKREIS = 7;
	private static int REPORT_WARTUNGSPLAN_GERAETETYP = 8;
	private static int REPORT_WARTUNGSPLAN_BEMERKUNG = 9;
	private static int REPORT_WARTUNGSPLAN_BEZEICHNUNG = 10;
	private static int REPORT_WARTUNGSPLAN_GRENZWERT = 11;
	private static int REPORT_WARTUNGSPLAN_GRENZWERTMIN = 12;
	private static int REPORT_WARTUNGSPLAN_GRENZWERTMAX = 13;
	private static int REPORT_WARTUNGSPLAN_ARTIKEL = 14;
	private static int REPORT_WARTUNGSPLAN_ARTIKELBEZEICHNUNG = 15;
	private static int REPORT_WARTUNGSPLAN_GERAETEPOSITION = 16;
	private static int REPORT_WARTUNGSPLAN_PERSONALGRUPPE = 17;
	private static int REPORT_WARTUNGSPLAN_KOMMENTAR = 18;
	private static int REPORT_WARTUNGSPLAN_FAELLIG_AM = 19;
	private static int REPORT_WARTUNGSPLAN_KOSTENSTELLE = 21;
	private static int REPORT_WARTUNGSPLAN_SUBREPORT_WARTUNGSLISTE = 22;
	private static int REPORT_WARTUNGSPLAN_DAUER = 23;
	private static int REPORT_WARTUNGSPLAN_KUNDE_ADRESSBLOCK = 24;
	private static int REPORT_WARTUNGSPLAN_STANDORT_ADRESSBLOCK = 25;
	private static int REPORT_WARTUNGSPLAN_LEISTUNG = 26;
	private static int REPORT_WARTUNGSPLAN_AUFTRAG = 27;
	private static int REPORT_WARTUNGSPLAN_GERAETENUMMER = 28;
	private static int REPORT_WARTUNGSPLAN_GEWERK = 29;
	private static int REPORT_WARTUNGSPLAN_HERSTELLER = 30;
	private static int REPORT_WARTUNGSPLAN_LIEFERANT = 31;
	private static int REPORT_WARTUNGSPLAN_LIEFERANT_BEMERKUNG = 32;

	private static int REPORT_WARTUNGSPLAN_ANZAHL_SPALTEN = 33;

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printWartungsplan(java.sql.Timestamp tBis,
			Integer personalgrupppeIId, Integer kostenstelleIId,
			Integer iSortierung, Integer standortIId, TheClientDto theClientDto) {

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_BIS", tBis);

		sAktuellerReport = InstandhaltungReportFac.REPORT_WARTUNGSPLAN;

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tBis.getTime());
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);

		tBis = Helper.cutTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));

		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT g FROM FLRWartungsschritte g WHERE  g.t_abdurchfuehren<'"
				+ Helper.formatDateWithSlashes(new java.sql.Date(c
						.getTimeInMillis()))
				+ "' AND g.flrgeraet.mandant_c_nr='"
				+ theClientDto.getMandant() + "'";

		
		if (standortIId != null) {
			
			StandortDto standortDto=getInstandhaltungFac()
			.standortFindByPrimaryKey(standortIId);
			
			PartnerDto pDto=getPartnerFac().partnerFindByPrimaryKey(standortDto.getPartnerIId(), theClientDto);
			
			parameter.put("P_STANDORT", pDto.formatAnrede());
			sQuery += " AND g.flrgeraet.flrstandort.i_id=" + standortIId;
		}
		
		if (personalgrupppeIId != null) {
			parameter.put("P_PERSONALGRUPPE", getPersonalFac()
					.personalgruppeFindByPrimaryKey(personalgrupppeIId)
					.getCBez());
			sQuery += " AND g.flrpersonalgruppe.i_id=" + personalgrupppeIId;
		}
		if (kostenstelleIId != null) {
			parameter.put("P_KOSTENSTELLE", getSystemFac()
					.kostenstelleFindByPrimaryKey(kostenstelleIId).getCBez());
			sQuery += " AND g.flrgeraet.flrstandort.flrinstandhaltung.flrkunde.flrkostenstelle.i_id="
					+ kostenstelleIId;
		}

		if (iSortierung == InstandhaltungReportFac.REPORT_WARTUNGSPLAN_OPTION_SORTIERUNG_KOSTENSTELLE) {
			sQuery += " ORDER BY g.flrgeraet.flrstandort.flrinstandhaltung.flrkunde.flrkostenstelle.c_bez,g.flrgeraet.flrstandort.flrinstandhaltung.flrkunde.flrpartner.c_name1nachnamefirmazeile1,g.flrgeraet.flrstandort.flrpartner.c_name1nachnamefirmazeile1 ASC";

		} else {
			sQuery += " ORDER BY g.flrpersonalgruppe.c_bez, g.flrgeraet.flrstandort.flrinstandhaltung.flrkunde.flrkostenstelle.c_bez,g.flrgeraet.flrstandort.flrinstandhaltung.flrkunde.flrpartner.c_name1nachnamefirmazeile1,g.flrgeraet.flrstandort.flrpartner.c_name1nachnamefirmazeile1 ASC";

		}

		MandantDto mandantDto = null;
		try {
			mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		ArrayList alDaten = new ArrayList();

		org.hibernate.Query qResult = session.createQuery(sQuery);
		List<?> results = qResult.list();

		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			FLRWartungsschritte flrWartugnsschritte = (FLRWartungsschritte) resultListIterator
					.next();

			WartungsschritteDto wartungsschritteDto = getInstandhaltungFac()
					.wartungsschritteFindByPrimaryKey(
							flrWartugnsschritte.getI_id());

			// Naechster faelliger Termin nach Heute
			Calendar cBeginn = Calendar.getInstance();
			cBeginn.setTimeInMillis(wartungsschritteDto.getTAbdurchfuehren()
					.getTime());

			String intervall = flrWartugnsschritte
					.getAuftragwiederholungsintervall_c_nr();

			Timestamp tHeute = Helper.cutTimestamp(new Timestamp(System
					.currentTimeMillis()));

			while (cBeginn.getTimeInMillis() < tBis.getTime()) {

				Timestamp tBeginndatumFuerLos = new Timestamp(
						cBeginn.getTimeInMillis());

				if (tBeginndatumFuerLos.after(tHeute)
						&& tBeginndatumFuerLos.before(tBis)) {

					Object[] zeile = new Object[REPORT_WARTUNGSPLAN_ANZAHL_SPALTEN];

					GeraetDto geraetDto = getInstandhaltungFac()
							.geraetFindByPrimaryKey(
									flrWartugnsschritte.getFlrgeraet()
											.getI_id());

					if (flrWartugnsschritte.getFlrgeraet().getFlrstandort() != null) {
						zeile[REPORT_WARTUNGSPLAN_STANDORT] = flrWartugnsschritte
								.getFlrgeraet().getFlrstandort()
								.getFlrpartner()
								.getC_name1nachnamefirmazeile1();

						PartnerDto partnerDto = getPartnerFac()
								.partnerFindByPrimaryKey(
										flrWartugnsschritte.getFlrgeraet()
												.getFlrstandort()
												.getFlrpartner().getI_id(),
										theClientDto);

						zeile[REPORT_WARTUNGSPLAN_STANDORT_ADRESSBLOCK] = formatAdresseFuerAusdruck(
								partnerDto, null, mandantDto,
								theClientDto.getLocUi());
						zeile[REPORT_WARTUNGSPLAN_AUFTRAG] = flrWartugnsschritte
								.getFlrgeraet().getFlrstandort()
								.getFlrauftrag().getC_nr();

					}

					WartungsschritteDto wDto = getInstandhaltungFac()
							.wartungsschritteFindByPrimaryKey(
									flrWartugnsschritte.getI_id());

					ArtikelDto artikelDtoWS = getArtikelFac()
							.artikelFindByPrimaryKeySmall(wDto.getArtikelIId(),
									theClientDto);
					zeile[REPORT_WARTUNGSPLAN_ARTIKEL] = artikelDtoWS.getCNr();
					if (artikelDtoWS.getArtikelsprDto() != null) {
						zeile[REPORT_WARTUNGSPLAN_ARTIKELBEZEICHNUNG] = artikelDtoWS
								.getArtikelsprDto().getCBez();
					}

					zeile[REPORT_WARTUNGSPLAN_BEMERKUNG] = wDto.getCBemerkung();

					if (wDto.getLieferantIId() != null) {
						LieferantDto lDto = getLieferantFac()
								.lieferantFindByPrimaryKey(
										wDto.getLieferantIId(), theClientDto);
						zeile[REPORT_WARTUNGSPLAN_LIEFERANT] = lDto
								.getPartnerDto().getCName1nachnamefirmazeile1();
					}

					if (geraetDto.getGewerkIId() != null) {

						zeile[REPORT_WARTUNGSPLAN_GEWERK] = getInstandhaltungFac()
								.gewerkFindByPrimaryKey(
										geraetDto.getGewerkIId()).getCBez();
					}

					if (geraetDto.getHerstellerIId() != null) {

						zeile[REPORT_WARTUNGSPLAN_HERSTELLER] = getArtikelFac()
								.herstellerFindByPrimaryKey(
										geraetDto.getHerstellerIId(),
										theClientDto).getCNr();
					}

					zeile[REPORT_WARTUNGSPLAN_LIEFERANT_BEMERKUNG] = wDto
							.getCBemerkunglieferant();

					zeile[REPORT_WARTUNGSPLAN_FABRIKAT] = geraetDto
							.getCFabrikat();
					zeile[REPORT_WARTUNGSPLAN_LEISTUNG] = geraetDto
							.getCLeistung();
					zeile[REPORT_WARTUNGSPLAN_GERAETEPOSITION] = geraetDto
							.getCStandort();
					zeile[REPORT_WARTUNGSPLAN_GERAETENUMMER] = geraetDto
							.getIId();
					zeile[REPORT_WARTUNGSPLAN_GERAETESNR] = geraetDto
							.getCGeraetesnr();
					zeile[REPORT_WARTUNGSPLAN_VERSORGUNSKREIS] = geraetDto
							.getCVersorgungskreis();

					zeile[REPORT_WARTUNGSPLAN_BEZEICHNUNG] = flrWartugnsschritte
							.getFlrgeraet().getC_bez();
					zeile[REPORT_WARTUNGSPLAN_KOSTENSTELLE] = flrWartugnsschritte
							.getFlrgeraet().getFlrstandort()
							.getFlrinstandhaltung().getFlrkunde()
							.getFlrkostenstelle().getC_bez();

					zeile[REPORT_WARTUNGSPLAN_FAELLIG_AM] = tBeginndatumFuerLos;
					zeile[REPORT_WARTUNGSPLAN_GRENZWERT] = flrWartugnsschritte
							.getFlrgeraet().getN_grenzwert();
					zeile[REPORT_WARTUNGSPLAN_GRENZWERTMAX] = flrWartugnsschritte
							.getFlrgeraet().getN_grenzwertmax();
					zeile[REPORT_WARTUNGSPLAN_GRENZWERTMIN] = flrWartugnsschritte
							.getFlrgeraet().getN_grenzwertmin();

					zeile[REPORT_WARTUNGSPLAN_PERSONALGRUPPE] = flrWartugnsschritte
							.getFlrpersonalgruppe().getC_bez();

					zeile[REPORT_WARTUNGSPLAN_KUNDE] = flrWartugnsschritte
							.getFlrgeraet().getFlrstandort()
							.getFlrinstandhaltung().getFlrkunde()
							.getFlrpartner().getC_name1nachnamefirmazeile1();

					KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
							flrWartugnsschritte.getFlrgeraet().getFlrstandort()
									.getFlrinstandhaltung().getFlrkunde()
									.getI_id(), theClientDto);

					zeile[REPORT_WARTUNGSPLAN_KUNDE_ADRESSBLOCK] = formatAdresseFuerAusdruck(
							kundeDto.getPartnerDto(), null, mandantDto,
							theClientDto.getLocUi());

					if (flrWartugnsschritte.getFlrgeraet().getFlrhalle() != null) {
						zeile[REPORT_WARTUNGSPLAN_HALLE] = flrWartugnsschritte
								.getFlrgeraet().getFlrhalle().getC_bez();
					}

					if (flrWartugnsschritte.getFlrgeraet().getFlrgeraetetyp() != null) {
						zeile[REPORT_WARTUNGSPLAN_GERAETETYP] = flrWartugnsschritte
								.getFlrgeraet().getFlrgeraetetyp().getC_bez();
					}
					if (flrWartugnsschritte.getFlrgeraet().getFlranlage() != null) {
						zeile[REPORT_WARTUNGSPLAN_ANLAGE] = flrWartugnsschritte
								.getFlrgeraet().getFlranlage().getC_bez();
					}
					if (flrWartugnsschritte.getFlrgeraet().getFlrismaschine() != null) {
						zeile[REPORT_WARTUNGSPLAN_MASCHINE] = flrWartugnsschritte
								.getFlrgeraet().getFlrismaschine().getC_bez();
					}

					zeile[REPORT_WARTUNGSPLAN_DAUER] = new BigDecimal(
							flrWartugnsschritte.getL_dauer().doubleValue())
							.divide(new BigDecimal(1000 * 60 * 60), 4,
									BigDecimal.ROUND_HALF_EVEN);

					WartungslisteDto[] wartungslisteDtos = getInstandhaltungFac()
							.wartungslisteFindByGeraetIId(
									flrWartugnsschritte.getFlrgeraet()
											.getI_id());

					Object[][] oSubData = new Object[wartungslisteDtos.length][9];

					String[] fieldnames = new String[] { "Artikelnummer",
							"Bezeichnung", "Menge", "Verrechenbar",
							"Wartungsmaterial", "VeraltetDatum",
							"VeraltetPerson", "VeraltetGrund", "Kommentar" };

					for (int i = 0; i < wartungslisteDtos.length; i++) {

						if (wartungslisteDtos[i].getArtikelIId() != null) {

							ArtikelDto artikelDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(
											wartungslisteDtos[i]
													.getArtikelIId(),
											theClientDto);
							oSubData[i][0] = artikelDto.getCNr();
							if (artikelDto.getArtikelsprDto() != null) {
								oSubData[i][1] = artikelDto.getArtikelsprDto()
										.getCBez();
							}

						} else {
							oSubData[i][1] = wartungslisteDtos[i].getCBez();
						}

						oSubData[i][2] = wartungslisteDtos[i].getNMenge();
						oSubData[i][3] = Helper
								.short2Boolean(wartungslisteDtos[i]
										.getBVerrechenbar());
						oSubData[i][4] = Helper
								.short2Boolean(wartungslisteDtos[i]
										.getBWartungsmaterial());

						oSubData[i][5] = wartungslisteDtos[i].getTVeraltet();

						if (wartungslisteDtos[i].getPersonalIIdVeraltet() != null) {

							oSubData[i][6] = getPersonalFac()
									.personalFindByPrimaryKey(
											wartungslisteDtos[i]
													.getPersonalIIdVeraltet(),
											theClientDto).getCKurzzeichen();
						}
						oSubData[i][7] = wartungslisteDtos[i].getCVeraltet();
						oSubData[i][8] = wartungslisteDtos[i].getXBemerkung();

					}

					zeile[REPORT_WARTUNGSPLAN_SUBREPORT_WARTUNGSLISTE] = new LPDatenSubreport(
							oSubData, fieldnames);

					alDaten.add(zeile);
				}

				if (intervall
						.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_2WOECHENTLICH)) {
					cBeginn.add(Calendar.DAY_OF_MONTH, 14);
				} else if (intervall
						.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_WOECHENTLICH)) {
					cBeginn.add(Calendar.DAY_OF_MONTH, 7);
				}

				if (intervall
						.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_JAHR)) {
					cBeginn.add(Calendar.YEAR, 1);
				} else if (intervall
						.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_2JAHR)) {
					cBeginn.add(Calendar.YEAR, 2);
				} else if (intervall
						.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_3JAHR)) {
					cBeginn.add(Calendar.YEAR, 3);
				} else if (intervall
						.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_4JAHR)) {
					cBeginn.add(Calendar.YEAR, 4);
				} else if (intervall
						.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_5JAHR)) {
					cBeginn.add(Calendar.YEAR, 5);
				} else if (intervall
						.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_MONATLICH)) {
					cBeginn.add(Calendar.MONTH, 1);
				} else if (intervall
						.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_QUARTAL)) {
					cBeginn.add(Calendar.MONTH, 3);
				} else if (intervall
						.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_HALBJAHR)) {
					cBeginn.add(Calendar.MONTH, 6);
				}

			}

		}

		data = new Object[alDaten.size()][REPORT_WARTUNGSPLAN_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(parameter, InstandhaltungReportFac.REPORT_MODUL,
				InstandhaltungReportFac.REPORT_WARTUNGSPLAN,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();
	}

	public Object getFieldValue(JRField jRField) throws JRException {
		Object value = null;
		String fieldName = jRField.getName();
		if (sAktuellerReport
				.equals(InstandhaltungReportFac.REPORT_WARTUNGSPLAN)) {
			if ("Kunde".equals(fieldName)) {
				value = data[index][REPORT_WARTUNGSPLAN_KUNDE];
			} else if ("KundeAdressblock".equals(fieldName)) {
				value = data[index][REPORT_WARTUNGSPLAN_KUNDE_ADRESSBLOCK];
			} else if ("StandortAdressblock".equals(fieldName)) {
				value = data[index][REPORT_WARTUNGSPLAN_STANDORT_ADRESSBLOCK];
			} else if ("Leistung".equals(fieldName)) {
				value = data[index][REPORT_WARTUNGSPLAN_LEISTUNG];
			} else if ("Auftrag".equals(fieldName)) {
				value = data[index][REPORT_WARTUNGSPLAN_AUFTRAG];
			} else if ("Kostenstelle".equals(fieldName)) {
				value = data[index][REPORT_WARTUNGSPLAN_KOSTENSTELLE];
			} else if ("Geraet".equals(fieldName)) {
				value = data[index][REPORT_WARTUNGSPLAN_BEZEICHNUNG];
			} else if ("Grenzwert".equals(fieldName)) {
				value = data[index][REPORT_WARTUNGSPLAN_GRENZWERT];
			} else if ("Faelligkeit".equals(fieldName)) {
				value = data[index][REPORT_WARTUNGSPLAN_FAELLIG_AM];
			} else if ("Artikel".equals(fieldName)) {
				value = data[index][REPORT_WARTUNGSPLAN_ARTIKEL];
			} else if ("Geraeteposition".equals(fieldName)) {
				value = data[index][REPORT_WARTUNGSPLAN_GERAETEPOSITION];
			} else if ("Personalgruppe".equals(fieldName)) {
				value = data[index][REPORT_WARTUNGSPLAN_PERSONALGRUPPE];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_WARTUNGSPLAN_ARTIKELBEZEICHNUNG];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][REPORT_WARTUNGSPLAN_KOMMENTAR];
			} else if ("Bemerkung".equals(fieldName)) {
				value = data[index][REPORT_WARTUNGSPLAN_BEMERKUNG];
			} else if ("Versorgungskreis".equals(fieldName)) {
				value = data[index][REPORT_WARTUNGSPLAN_VERSORGUNSKREIS];
			} else if ("Fabrikat".equals(fieldName)) {
				value = data[index][REPORT_WARTUNGSPLAN_FABRIKAT];
			} else if ("Halle".equals(fieldName)) {
				value = data[index][REPORT_WARTUNGSPLAN_HALLE];
			} else if ("Standort".equals(fieldName)) {
				value = data[index][REPORT_WARTUNGSPLAN_STANDORT];
			} else if ("Maschine".equals(fieldName)) {
				value = data[index][REPORT_WARTUNGSPLAN_MASCHINE];
			} else if ("Anlage".equals(fieldName)) {
				value = data[index][REPORT_WARTUNGSPLAN_ANLAGE];
			} else if ("Grenzwertmin".equals(fieldName)) {
				value = data[index][REPORT_WARTUNGSPLAN_GRENZWERTMIN];
			} else if ("Grenzwertmax".equals(fieldName)) {
				value = data[index][REPORT_WARTUNGSPLAN_GRENZWERTMAX];
			} else if ("SubreportWartungsliste".equals(fieldName)) {
				value = data[index][REPORT_WARTUNGSPLAN_SUBREPORT_WARTUNGSLISTE];
			} else if ("Dauer".equals(fieldName)) {
				value = data[index][REPORT_WARTUNGSPLAN_DAUER];
			} else if ("Geraetenummer".equals(fieldName)) {
				value = data[index][REPORT_WARTUNGSPLAN_GERAETENUMMER];
			} else if ("Lieferant".equals(fieldName)) {
				value = data[index][REPORT_WARTUNGSPLAN_LIEFERANT];
			} else if ("BemerkungLieferant".equals(fieldName)) {
				value = data[index][REPORT_WARTUNGSPLAN_LIEFERANT_BEMERKUNG];
			} else if ("Hersteller".equals(fieldName)) {
				value = data[index][REPORT_WARTUNGSPLAN_HERSTELLER];
			} else if ("Gewerk".equals(fieldName)) {
				value = data[index][REPORT_WARTUNGSPLAN_GEWERK];
			}

		}

		return value;
	}

	public boolean next() throws JRException {
		index++;
		return (index < data.length);
	}

}
