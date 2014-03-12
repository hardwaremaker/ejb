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
package com.lp.server.angebotstkl.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

import com.lp.server.angebot.fastlanereader.generated.FLRAngebot;
import com.lp.server.angebot.fastlanereader.generated.FLRAngebotposition;
import com.lp.server.angebotstkl.ejb.Agstkl;
import com.lp.server.angebotstkl.ejb.Agstklaufschlag;
import com.lp.server.angebotstkl.ejb.Agstklposition;
import com.lp.server.angebotstkl.ejb.Agstklpositionsart;
import com.lp.server.angebotstkl.ejb.Aufschlag;
import com.lp.server.angebotstkl.ejb.Einkaufsangebot;
import com.lp.server.angebotstkl.ejb.Einkaufsangebotposition;
import com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstklposition;
import com.lp.server.angebotstkl.fastlanereader.generated.FLREinkaufsangebotposition;
import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.angebotstkl.service.AgstklDtoAssembler;
import com.lp.server.angebotstkl.service.AgstklaufschlagDto;
import com.lp.server.angebotstkl.service.AgstklaufschlagDtoAssembler;
import com.lp.server.angebotstkl.service.AgstklpositionDto;
import com.lp.server.angebotstkl.service.AgstklpositionsartDto;
import com.lp.server.angebotstkl.service.AgstklpositionsartDtoAssembler;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.angebotstkl.service.AngebotstklServiceFac;
import com.lp.server.angebotstkl.service.AufschlagDto;
import com.lp.server.angebotstkl.service.AufschlagDtoAssembler;
import com.lp.server.angebotstkl.service.EinkaufsangebotDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotDtoAssembler;
import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDtoAssembler;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.HerstellerDto;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.pkgenerator.format.LpBelegnummer;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;

@Stateless
public class AngebotstklFacBean extends LPReport implements AngebotstklFac {
	@PersistenceContext
	private EntityManager em;

	private Object[][] data = null;
	private String sAktuellerReport = null;

	private static int REPORT_ANGEBOTSTKL_NR = 0;
	private static int REPORT_ANGEBOTSTKL_ARTIKELNUMMER = 1;
	private static int REPORT_ANGEBOTSTKL_ARTIKELBEZEICHNUNG = 2;
	private static int REPORT_ANGEBOTSTKL_MENGE = 3;
	private static int REPORT_ANGEBOTSTKL_MENGENEINHEIT = 4;
	private static int REPORT_ANGEBOTSTKL_EINZELPREIS = 5;
	private static int REPORT_ANGEBOTSTKL_NETTOGESAMTPREIS = 6;
	private static int REPORT_ANGEBOTSTKL_AUFSCHLAG = 7;
	private static int REPORT_ANGEBOTSTKL_GESTPREIS = 8;
	private static int REPORT_ANGEBOTSTKL_GESTPREISGESAMT = 9;
	private static int REPORT_ANGEBOTSTKL_POSITIONSART = 10;
	private static int REPORT_ANGEBOTSTKL_POSITIONSOBJEKT = 11;
	private static int REPORT_ANGEBOTSTKL_NETTOPREIS = 12;
	private static int REPORT_ANGEBOTSTKL_RABATTSATZ = 13;
	private static int REPORT_ANGEBOTSTKL_ZUSATZRABATTSATZ = 14;
	private static int REPORT_ANGEBOTSTKL_AUFSCHLAG_PROZENT = 15;
	private static int REPORT_ANGEBOTSTKL_AUFSCHLAG_BETRAG = 16;
	private static int REPORT_ANGEBOTSTKL_NETTOGESAMTPREISMITAUFSCHLAG = 17;
	private static int REPORT_ANGEBOTSTKL_LIEF1PREIS = 18;
	private static int REPORT_ANGEBOTSTKL_LETZTER_GELIEFERT_PREIS = 19;
	private static int REPORT_ANGEBOTSTKL_LETZTES_WE_DATUM = 20;
	private static int REPORT_ANGEBOTSTKL_ARTIKELART = 21;
	private static int REPORT_ANGEBOTSTKL_ANZAHL_FELDER = 22;

	private static int REPORT_EINKAUFSANGEBOT_ARTIKELNUMMER = 0;
	private static int REPORT_EINKAUFSANGEBOT_BEZEICHNUNG = 1;
	private static int REPORT_EINKAUFSANGEBOT_MENGE = 2;
	private static int REPORT_EINKAUFSANGEBOT_EINHEIT = 3;
	private static int REPORT_EINKAUFSANGEBOT_PREIS1 = 4;
	private static int REPORT_EINKAUFSANGEBOT_PREIS2 = 5;
	private static int REPORT_EINKAUFSANGEBOT_PREIS3 = 6;
	private static int REPORT_EINKAUFSANGEBOT_PREIS4 = 7;
	private static int REPORT_EINKAUFSANGEBOT_PREIS5 = 8;
	private static int REPORT_EINKAUFSANGEBOT_HERSTELLER = 9;
	private static int REPORT_EINKAUFSANGEBOT_HERSTELLERNR = 10;
	private static int REPORT_EINKAUFSANGEBOT_VERPACKUNGSEINHEIT = 11;
	private static int REPORT_EINKAUFSANGEBOT_WIEDERBESCHAFFUNGSZEIT = 12;
	private static int REPORT_EINKAUFSANGEBOT_MINDESTBESTELLMENGE = 13;
	private static int REPORT_EINKAUFSANGEBOT_BEMERKUNG = 14;
	private static int REPORT_EINKAUFSANGEBOT_POSITION = 15;
	private static int REPORT_EINKAUFSANGEBOT_INTERNE_BEMERKUNG = 16;
	private static int REPORT_EINKAUFSANGEBOT_BEMERKUNG_MITDRUCKEN = 17;
	private static int REPORT_EINKAUFSANGEBOT_KOMMENTAR1 = 18;
	private static int REPORT_EINKAUFSANGEBOT_KOMMENTAR2 = 19;

	public Map<String, String> getAllAgstklpositionsart() throws EJBExceptionLP {

		myLogger.entry();
		TreeMap tmArten = new TreeMap<Object, Object>();
		// try {
		Query query = em.createNamedQuery("AgstklpositionsartfindAll");

		Collection<?> clArten = query.getResultList();
		// if (clArten.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }

		Iterator<?> itArten = clArten.iterator();
		while (itArten.hasNext()) {
			Agstklpositionsart agstklpositionsartTemp = (Agstklpositionsart) itArten
					.next();
			Object key = agstklpositionsartTemp.getPositionsartCNr();
			Object value = key;
			tmArten.put(key, value);
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		return tmArten;
	}

	public AufschlagDto[] aufschlagFindByBMaterial(Integer agstklIId,
			Short bMaterial, TheClientDto theClientDto) {

		boolean bBereitsEintraegeVorhanden = false;

		Query queryAgstkl = em
				.createNamedQuery("AgstklaufschlagFindByAgstklIId");
		queryAgstkl.setParameter(1, agstklIId);
		Collection<?> clAgstkl = queryAgstkl.getResultList();

		if (clAgstkl.size() > 0) {
			bBereitsEintraegeVorhanden = true;
		}

		Query query = em.createNamedQuery("AufschlagFindByMandantCNrBMaterial");
		query.setParameter(1, theClientDto.getMandant());
		query.setParameter(2, bMaterial);
		Collection<?> cl = query.getResultList();

		AufschlagDto[] dtos = AufschlagDtoAssembler.createDtos(cl);
		for (int i = 0; i < dtos.length; i++) {
			try {
				Query query2 = em
						.createNamedQuery("AgstklaufschlagFindByAgstklIIdAufschlagIId");
				query2.setParameter(1, agstklIId);
				query2.setParameter(2, dtos[i].getIId());
				Agstklaufschlag agstklaufschlag = (Agstklaufschlag) query2
						.getSingleResult();
				dtos[i].setAgstklaufschlagDto(AgstklaufschlagDtoAssembler
						.createDto(agstklaufschlag));
			} catch (NoResultException ex) {

				AgstklaufschlagDto aga = new AgstklaufschlagDto();
				aga.setAgstklIId(agstklIId);
				aga.setAufschlagIId(dtos[i].getIId());
				if (bBereitsEintraegeVorhanden == true) {
					aga.setFAufschlag(0D);
				} else {
					aga.setFAufschlag(dtos[i].getFAufschlag());
				}
				dtos[i].setAgstklaufschlagDto(aga);
			}
		}

		return dtos;

	}

	public JasperPrintLP printEinkaufsangebot(Integer einkaufsangebotIId,
			int iSortierung, TheClientDto theClientDto) throws EJBExceptionLP {
		index = -1;
		sAktuellerReport = AngebotstklFac.REPORT_EINKAUFSANGEBOT;

		HashMap parameter = new HashMap<Object, Object>();

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT e FROM FLREinkaufsangebotposition e LEFT OUTER JOIN e.flrartikel AS a WHERE e.einkaufsangebot_i_id="
				+ einkaufsangebotIId + " ORDER BY ";

		if (iSortierung == AngebotstklFac.REPORT_AGSTKL_OPTION_SORTIERUNG_ARTIKELNR) {
			sQuery += " a.c_nr ASC ";
		} else if (iSortierung == AngebotstklFac.REPORT_AGSTKL_OPTION_SORTIERUNG_POSITION) {
			sQuery += " e.c_position ASC, a.c_nr ASC ";
		} else if (iSortierung == AngebotstklFac.REPORT_AGSTKL_OPTION_SORTIERUNG_BEMERKUNG) {
			sQuery += " e.c_bemerkung, a.c_nr ASC ASC ";
		} else if (iSortierung == AngebotstklFac.REPORT_AGSTKL_OPTION_SORTIERUNG_SORT) {
			sQuery += " e.i_sort ASC ";
		}

		org.hibernate.Query inventurliste = session.createQuery(sQuery);

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListIterator = resultList.iterator();
		int row = 0;
		data = new Object[resultList.size()][20];

		while (resultListIterator.hasNext()) {

			FLREinkaufsangebotposition epos = (FLREinkaufsangebotposition) resultListIterator
					.next();

			EinkaufsangebotpositionDto dto = einkaufsangebotpositionFindByPrimaryKey(epos
					.getI_id());
			data[row][REPORT_EINKAUFSANGEBOT_EINHEIT] = dto.getEinheitCNr();
			data[row][REPORT_EINKAUFSANGEBOT_MENGE] = dto.getNMenge();
			data[row][REPORT_EINKAUFSANGEBOT_PREIS1] = dto.getNPreis1();
			data[row][REPORT_EINKAUFSANGEBOT_PREIS2] = dto.getNPreis2();
			data[row][REPORT_EINKAUFSANGEBOT_PREIS3] = dto.getNPreis3();
			data[row][REPORT_EINKAUFSANGEBOT_PREIS4] = dto.getNPreis4();
			data[row][REPORT_EINKAUFSANGEBOT_PREIS5] = dto.getNPreis5();
			data[row][REPORT_EINKAUFSANGEBOT_KOMMENTAR1] = dto.getCKommentar1();
			data[row][REPORT_EINKAUFSANGEBOT_KOMMENTAR2] = dto.getCKommentar2();
			data[row][REPORT_EINKAUFSANGEBOT_MINDESTBESTELLMENGE] = dto
					.getFMindestbestellmenge();
			data[row][REPORT_EINKAUFSANGEBOT_VERPACKUNGSEINHEIT] = dto
					.getIVerpackungseinheit();
			data[row][REPORT_EINKAUFSANGEBOT_WIEDERBESCHAFFUNGSZEIT] = dto
					.getIWiederbeschaffungszeit();

			data[row][REPORT_EINKAUFSANGEBOT_BEMERKUNG] = dto.getCBemerkung();
			data[row][REPORT_EINKAUFSANGEBOT_INTERNE_BEMERKUNG] = dto
					.getCInternebemerkung();
			data[row][REPORT_EINKAUFSANGEBOT_BEMERKUNG_MITDRUCKEN] = Helper
					.short2Boolean(dto.getBMitdrucken());
			data[row][REPORT_EINKAUFSANGEBOT_POSITION] = dto.getCPosition();

			if (dto.getPositionsartCNr().equals(
					AngebotstklFac.POSITIONSART_AGSTKL_IDENT)) {

				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(dto.getArtikelIId(),
								theClientDto);

				data[row][REPORT_EINKAUFSANGEBOT_ARTIKELNUMMER] = artikelDto
						.getCNr();
				data[row][REPORT_EINKAUFSANGEBOT_BEZEICHNUNG] = artikelDto
						.formatBezeichnung();

				if (artikelDto.getHerstellerIId() != null) {
					HerstellerDto herstellerDto = getArtikelFac()
							.herstellerFindByPrimaryKey(
									artikelDto.getHerstellerIId(), theClientDto);
					data[row][REPORT_EINKAUFSANGEBOT_HERSTELLERNR] = herstellerDto
							.getCNr();
					data[row][REPORT_EINKAUFSANGEBOT_HERSTELLER] = herstellerDto
							.getPartnerDto().formatFixTitelName1Name2();
				}

			} else {
				String sBez = dto.getCBez();
				if (dto.getCZusatzbez() != null) {
					sBez += "\n" + dto.getCZusatzbez();
				}
				data[row][REPORT_EINKAUFSANGEBOT_BEZEICHNUNG] = sBez;
			}
			row++;
		}

		EinkaufsangebotDto einkaufsangebotDto = einkaufsangebotFindByPrimaryKey(einkaufsangebotIId);

		parameter.put("P_BELEGNUMMER", einkaufsangebotDto.getCNr());
		parameter.put("P_PROJEKT", einkaufsangebotDto.getCProjekt());

		parameter.put("P_STAFFEL1", einkaufsangebotDto.getNMenge1());
		parameter.put("P_STAFFEL2", einkaufsangebotDto.getNMenge2());
		parameter.put("P_STAFFEL3", einkaufsangebotDto.getNMenge3());
		parameter.put("P_STAFFEL4", einkaufsangebotDto.getNMenge4());
		parameter.put("P_STAFFEL5", einkaufsangebotDto.getNMenge5());

		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
				einkaufsangebotDto.getKundeIId(), theClientDto);

		parameter.put("P_KUNDE", kundeDto.getPartnerDto()
				.formatFixTitelName1Name2());

		initJRDS(parameter, AngebotstklFac.REPORT_MODUL,
				AngebotstklFac.REPORT_EINKAUFSANGEBOT,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();

	}

	public JasperPrintLP printAngebotstkl(Integer iIdAngebotstkl,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		index = -1;
		sAktuellerReport = AngebotstklFac.REPORT_ANGEBOTSTUECKLISTE;

		AgstklDto agstklDto = agstklFindByPrimaryKey(iIdAngebotstkl);
		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
				agstklDto.getKundeIId(), theClientDto);

		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria crit = session
				.createCriteria(FLRAgstklposition.class);
		crit.add(Expression.eq(AngebotstklFac.FLR_AGSTKLPOSITION_AGSTKL_I_ID,
				iIdAngebotstkl));
		crit.addOrder(Order.asc("i_sort"));
		List<?> resultList = crit.list();

		Iterator<?> resultListIterator = resultList.iterator();
		int row = 0;
		data = new Object[resultList.size()][REPORT_ANGEBOTSTKL_ANZAHL_FELDER];

		BigDecimal bdArbeitswert = new BigDecimal(0);
		BigDecimal bdMaterialwert = new BigDecimal(0);

		while (resultListIterator.hasNext()) {
			FLRAgstklposition flrAgstklposition = (FLRAgstklposition) resultListIterator
					.next();

			ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(
							flrAgstklposition.getFlrartikel().getI_id(),
							theClientDto);

			if (artikelDto.getArtikelartCNr() != null
					&& artikelDto.getArtikelartCNr().equals(
							ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
				bdArbeitswert = bdArbeitswert.add(flrAgstklposition
						.getN_menge().multiply(
								flrAgstklposition.getN_gestehungspreis()));
			} else {
				bdMaterialwert = bdMaterialwert.add(flrAgstklposition
						.getN_menge().multiply(
								flrAgstklposition.getN_gestehungspreis()));

			}
			data[row][REPORT_ANGEBOTSTKL_POSITIONSOBJEKT] = getSystemReportFac()
					.getPositionForReport(LocaleFac.BELEGART_AGSTUECKLISTE,
							flrAgstklposition.getI_id(), theClientDto);
			data[row][REPORT_ANGEBOTSTKL_NR] = new Integer(row + 1);
			if (artikelDto.getArtikelartCNr() != null
					&& !artikelDto.getArtikelartCNr().equals(
							ArtikelFac.ARTIKELART_HANDARTIKEL)) {
				data[row][REPORT_ANGEBOTSTKL_ARTIKELNUMMER] = artikelDto
						.getCNr();
			}
			data[row][REPORT_ANGEBOTSTKL_ARTIKELART] = artikelDto
					.getArtikelartCNr();
			data[row][REPORT_ANGEBOTSTKL_ARTIKELBEZEICHNUNG] = artikelDto
					.formatBezeichnung();
			data[row][REPORT_ANGEBOTSTKL_MENGE] = flrAgstklposition
					.getN_menge();
			data[row][REPORT_ANGEBOTSTKL_MENGENEINHEIT] = flrAgstklposition
					.getEinheit_c_nr();
			data[row][REPORT_ANGEBOTSTKL_EINZELPREIS] = flrAgstklposition
					.getN_nettoeinzelpreis();
			data[row][REPORT_ANGEBOTSTKL_POSITIONSART] = flrAgstklposition
					.getAgstklpositionsart_c_nr();

			data[row][REPORT_ANGEBOTSTKL_NETTOPREIS] = flrAgstklposition
					.getN_nettogesamtpreis();

			AgstklpositionDto agstklposDto = getAngebotstklpositionFac()
					.agstklpositionFindByPrimaryKey(
							flrAgstklposition.getI_id(), theClientDto);

			data[row][REPORT_ANGEBOTSTKL_AUFSCHLAG_PROZENT] = agstklposDto
					.getFAufschlag();
			data[row][REPORT_ANGEBOTSTKL_AUFSCHLAG_BETRAG] = agstklposDto
					.getNAufschlag();
			data[row][REPORT_ANGEBOTSTKL_NETTOGESAMTPREISMITAUFSCHLAG] = agstklposDto
					.getNNettogesamtmitaufschlag();

			if (flrAgstklposition.getAgstklpositionsart_c_nr().equals(
					LocaleFac.POSITIONSART_IDENT)) {

				ArtikellieferantDto alDto = getArtikelFac()
						.getArtikelEinkaufspreis(
								flrAgstklposition.getFlrartikel().getI_id(),
								null,
								flrAgstklposition.getN_menge(),
								flrAgstklposition.getFlragstkl()
										.getWaehrung_c_nr(),
								new java.sql.Date(flrAgstklposition
										.getFlragstkl().getT_belegdatum()
										.getTime()), theClientDto);
				if (alDto != null) {
					data[row][REPORT_ANGEBOTSTKL_LIEF1PREIS] = alDto
							.getNNettopreis();
				}

				Integer wepIId = getLagerFac().getLetzteWEP_IID(
						flrAgstklposition.getFlrartikel().getI_id());
				if (wepIId != null) {
					WareneingangspositionDto wepDto = getWareneingangFac()
							.wareneingangspositionFindByPrimaryKeyOhneExc(
									wepIId);

					if (wepDto != null) {

						WareneingangDto weDto = getWareneingangFac()
								.wareneingangFindByPrimaryKey(
										wepDto.getWareneingangIId());

						data[row][REPORT_ANGEBOTSTKL_LETZTER_GELIEFERT_PREIS] = wepDto
								.getNGelieferterpreis();
						data[row][REPORT_ANGEBOTSTKL_LETZTES_WE_DATUM] = weDto
								.getTWareneingangsdatum();

					}
				}

			}

			data[row][REPORT_ANGEBOTSTKL_RABATTSATZ] = agstklposDto
					.getFRabattsatz();
			data[row][REPORT_ANGEBOTSTKL_ZUSATZRABATTSATZ] = agstklposDto
					.getFZusatzrabattsatz();

			data[row][REPORT_ANGEBOTSTKL_NETTOGESAMTPREIS] = flrAgstklposition
					.getN_menge().multiply(
							flrAgstklposition.getN_nettogesamtpreis());

			if (flrAgstklposition.getN_gestehungspreis().doubleValue() != 0) {

				data[row][REPORT_ANGEBOTSTKL_AUFSCHLAG] = Helper
						.getAufschlagProzent(
								flrAgstklposition.getN_nettogesamtpreis(),
								flrAgstklposition.getN_gestehungspreis(), 4);
			} else {
				data[row][REPORT_ANGEBOTSTKL_AUFSCHLAG] = new BigDecimal(0);
			}

			data[row][REPORT_ANGEBOTSTKL_GESTPREIS] = flrAgstklposition
					.getN_gestehungspreis();
			if (flrAgstklposition.getN_menge() != null) {
				data[row][REPORT_ANGEBOTSTKL_GESTPREISGESAMT] = flrAgstklposition
						.getN_gestehungspreis().multiply(
								flrAgstklposition.getN_menge());
			}
			row++;
		}
		session.close();
		HashMap parameter = new HashMap<Object, Object>();

		parameter.put("P_ANGEBOTSTKL", agstklDto.getCNr());

		parameter.put("P_KUNDE", kundeDto.getPartnerDto().formatTitelAnrede());

		parameter.put("P_PROJEKT", agstklDto.getCBez());

		parameter.put("P_WAEHRUNG", agstklDto.getWaehrungCNr());

		parameter.put("P_ARBEITSWERT", bdArbeitswert);

		parameter.put("P_MATERIALWERT", bdMaterialwert);

		ParametermandantDto parameterMand = getParameterFac()
				.getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE,
						ParameterFac.PARAMETER_KALKULATIONSART);
		parameter.put("P_KALKULATIONSART",
				(Integer) parameterMand.getCWertAsObject());

		// PJ18040
		ArrayList alDaten = new ArrayList();
		String[] fieldnamesAufschlag = new String[] { "Bezeichnung",
				"AufschlagProzent", "AufschlagWert", "Typ" };

		BigDecimal[] nWerte = getAngebotstklFac()
				.berechneAgstklMaterialwertUndArbeitszeitwert(iIdAngebotstkl,
						theClientDto);

		BigDecimal bdStartwertMaterial = nWerte[0];

		BigDecimal bdAufschlaegeMaterial = new BigDecimal(0);
		Object[] zeile = new Object[fieldnamesAufschlag.length];

		zeile[0] = "Nettosumme Material";
		zeile[1] = null;
		zeile[2] = nWerte[0];
		zeile[3] = "1";
		alDaten.add(zeile);
		AufschlagDto[] aufschlagDtos = aufschlagFindByBMaterial(iIdAngebotstkl,
				Helper.boolean2Short(true), theClientDto);

		for (int i = 0; i < aufschlagDtos.length; i++) {

			zeile = new Object[fieldnamesAufschlag.length];

			BigDecimal aufschlag = Helper.getProzentWert(bdStartwertMaterial,
					new BigDecimal(aufschlagDtos[i].getAgstklaufschlagDto()
							.getFAufschlag()), 2);

			bdAufschlaegeMaterial = bdAufschlaegeMaterial.add(aufschlag);

			zeile[0] = aufschlagDtos[i].getCBez();
			zeile[1] = aufschlagDtos[i].getAgstklaufschlagDto().getFAufschlag();
			zeile[2] = aufschlag;
			zeile[3] = "1";

			alDaten.add(zeile);

		}

		BigDecimal gesamtwertMaterial = bdStartwertMaterial
				.add(bdAufschlaegeMaterial);

		zeile = new Object[fieldnamesAufschlag.length];
		zeile[0] = "Materialverkaufspreis";
		zeile[1] = null;
		zeile[2] = gesamtwertMaterial;
		zeile[3] = "2";
		alDaten.add(zeile);

		zeile = new Object[fieldnamesAufschlag.length];
		zeile[0] = "Arbeitszeit";
		zeile[1] = null;
		zeile[2] = nWerte[1];
		zeile[3] = "2";
		alDaten.add(zeile);

		BigDecimal bdStartwertAZ = nWerte[1];
		BigDecimal bdAufschlaegeAZ = new BigDecimal(0);

		aufschlagDtos = aufschlagFindByBMaterial(iIdAngebotstkl,
				Helper.boolean2Short(false), theClientDto);

		for (int i = 0; i < aufschlagDtos.length; i++) {

			zeile = new Object[fieldnamesAufschlag.length];

			BigDecimal aufschlag = Helper.getProzentWert(bdStartwertAZ,
					new BigDecimal(aufschlagDtos[i].getAgstklaufschlagDto()
							.getFAufschlag()), 2);

			bdAufschlaegeAZ = bdAufschlaegeAZ.add(aufschlag);

			zeile[0] = aufschlagDtos[i].getCBez();
			zeile[1] = aufschlagDtos[i].getAgstklaufschlagDto().getFAufschlag();
			zeile[2] = aufschlag;
			zeile[3] = "2";

			alDaten.add(zeile);

		}

		zeile = new Object[fieldnamesAufschlag.length];
		zeile[0] = "Verkaufspreis Gesamt";
		zeile[1] = null;
		zeile[2] = gesamtwertMaterial.add(bdStartwertAZ.add(bdAufschlaegeAZ));
		zeile[3] = "3";
		alDaten.add(zeile);

		Object[][] dataSubAufschlag = new Object[alDaten.size()][fieldnamesAufschlag.length];
		dataSubAufschlag = (Object[][]) alDaten.toArray(dataSubAufschlag);
		parameter.put("P_SUBREPORT_AUFSCHLAEGE", new LPDatenSubreport(
				dataSubAufschlag, fieldnamesAufschlag));

		initJRDS(parameter, AngebotstklFac.REPORT_MODUL,
				AngebotstklFac.REPORT_ANGEBOTSTUECKLISTE,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	public Integer createAgstkl(AgstklDto agstklDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (agstklDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("agstklDto == null"));
		}
		if (agstklDto.getKundeIId() == null
				|| agstklDto.getWaehrungCNr() == null
				|| agstklDto.getFWechselkursmandantwaehrungzuagstklwaehrung() == null
				|| agstklDto.getTBelegdatum() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"agstklDto.getKundeIId() == null || agstklDto.getWaehrungCNr() == null || agstklDto.getFWechselkursmandantwaehrungzuagstklwaehrung() == null || agstklDto.getTBelegdatum() == null"));
		}

		try {

			// generieren von primary key & auftragsnummer
			LpBelegnummerFormat f = getBelegnummerGeneratorObj()
					.getBelegnummernFormat(theClientDto.getMandant());
			LpBelegnummer bnr = getBelegnummerGeneratorObj().getNextBelegNr(
					PKConst.PK_AGSTKL, theClientDto.getMandant(), theClientDto);

			agstklDto.setIId(bnr.getPrimaryKey());
			agstklDto.setCNr(f.format(bnr));
			agstklDto.setMandantCNr(theClientDto.getMandant());

			agstklDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			agstklDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			agstklDto.setTAnlegen(new Timestamp(System.currentTimeMillis()));
			agstklDto.setTAendern(new Timestamp(System.currentTimeMillis()));
			agstklDto.setBelegartCNr(LocaleFac.BELEGART_AGSTUECKLISTE);

			Agstkl agstkl = new Agstkl(agstklDto.getIId(),
					agstklDto.getMandantCNr(), agstklDto.getCNr(),
					agstklDto.getBelegartCNr(), agstklDto.getKundeIId(),
					agstklDto.getTBelegdatum(), agstklDto.getWaehrungCNr(),
					agstklDto.getFWechselkursmandantwaehrungzuagstklwaehrung(),
					agstklDto.getPersonalIIdAnlegen(),
					agstklDto.getPersonalIIdAendern());
			em.persist(agstkl);
			em.flush();
			setAgstklFromAgstklDto(agstkl, agstklDto);
			return agstklDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeAgstkl(AgstklDto agstklDto) throws EJBExceptionLP {
		myLogger.entry();
		if (agstklDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("agstklDto == null"));
		}
		if (agstklDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("agstklDto.getIId() == null"));
		}
		// try {
		// try {
		Agstkl toRemove = em.find(Agstkl.class, agstklDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeAgstkl es gibt keine iid "
							+ agstklDto.getIId() + "\nagstklDto.toString: "
							+ agstklDto.toString());
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN, ex);
		// }
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		// }

	}

	public void updateAgstkl(AgstklDto agstklDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (agstklDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("agstklDto == null"));
		}
		if (agstklDto.getIId() == null
				|| agstklDto.getWaehrungCNr() == null
				|| agstklDto.getFWechselkursmandantwaehrungzuagstklwaehrung() == null
				|| agstklDto.getBelegartCNr() == null
				|| agstklDto.getKundeIId() == null
				|| agstklDto.getMandantCNr() == null
				|| agstklDto.getTBelegdatum() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"agstklDto.getIId() == null || agstklDto.getWaehrungCNr() == null || agstklDto.getFWechselkursmandantwaehrungzuagstklwaehrung() == null ||  agstklDto.getBelegartCNr() == null || agstklDto.getKundeIId() == null  || agstklDto.getMandantCNr() == null || agstklDto.getTBelegdatum() == null"));
		}
		Integer iId = agstklDto.getIId();

		Agstkl agstkl = null;
		// try {
		agstkl = em.find(Agstkl.class, iId);
		if (agstkl == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateAgstkl es gibt keine iid "
							+ agstklDto.getIId() + "\nagstklDto.toString(): "
							+ agstklDto.toString());

		}
		// }
		// catch (FinderException ex1) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex1);

		// }
		try {
			Query query = em.createNamedQuery("AgstklfindByCNrMandantCNr");
			query.setParameter(1, agstklDto.getCNr());
			query.setParameter(2, agstklDto.getMandantCNr());
			Integer iIdVorhanden = ((Agstkl) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"AG_AGSTKL.CNR"));
			}
		} catch (NoResultException ex) {

		}
		agstklDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		agstklDto.setTAendern(new Timestamp(System.currentTimeMillis()));
		setAgstklFromAgstklDto(agstkl, agstklDto);

	}

	public AgstklDto agstklFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("iId == null"));
		}

		AgstklDto agstklDto = agstklFindByPrimaryKeyOhneExc(iId);

		if (agstklDto == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei agstklFindByPrimaryKey. Es gibt keine iid "
							+ iId);

		}
		return agstklDto;

	}

	public AgstklDto agstklFindByPrimaryKeyOhneExc(Integer iId) {
		Agstkl agstkl = em.find(Agstkl.class, iId);
		if (agstkl == null) {
			return null;
		}
		return assembleAgstklDto(agstkl);

	}

	public AgstklDto[] agstklFindByKundeIIdMandantCNr(Integer iIdKunde,
			String cNrMandant) throws EJBExceptionLP {
		if (iIdKunde == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"iIdKunde == null"));
		}
		// try {
		Query query = em.createNamedQuery("AgstklfindByKundeIIdMandantCNr");
		query.setParameter(1, iIdKunde);
		query.setParameter(2, cNrMandant);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		return assembleAgstklDtos(cl);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public AgstklDto[] agstklFindByKundeIIdMandantCNrOhneExc(Integer iIdKunde,
			String cNrMandant) {
		if (iIdKunde == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"iIdKunde == null"));
		}
		try {
			Query query = em.createNamedQuery("AgstklfindByKundeIIdMandantCNr");
			query.setParameter(1, iIdKunde);
			query.setParameter(2, cNrMandant);
			return assembleAgstklDtos((Collection<?>) query.getResultList());
		} catch (Throwable t) {
			myLogger.warn("iIdKunde=" + iIdKunde + " cNrMandant=" + cNrMandant,
					t);
			return null;
		}
	}

	private void setAgstklFromAgstklDto(Agstkl agstkl, AgstklDto agstklDto) {
		agstkl.setMandantCNr(agstklDto.getMandantCNr());
		agstkl.setCNr(agstklDto.getCNr());
		agstkl.setBelegartCNr(agstklDto.getBelegartCNr());
		agstkl.setKundeIId(agstklDto.getKundeIId());
		agstkl.setAnsprechpartnerIIdKunde(agstklDto
				.getAnsprechpartnerIIdKunde());
		agstkl.setCBez(agstklDto.getCBez());
		agstkl.setWaehrungCNr(agstklDto.getWaehrungCNr());
		agstkl.setFWechselkursmandantwaehrungzuagstklwaehrung(agstklDto
				.getFWechselkursmandantwaehrungzuagstklwaehrung());
		agstkl.setPersonalIIdAnlegen(agstklDto.getPersonalIIdAnlegen());
		agstkl.setTAnlegen(agstklDto.getTAnlegen());
		agstkl.setPersonalIIdAendern(agstklDto.getPersonalIIdAendern());
		agstkl.setTAendern(agstklDto.getTAendern());
		agstkl.setTBelegdatum(agstklDto.getTBelegdatum());
		em.merge(agstkl);
		em.flush();
	}

	private void setAufschlagFromAufschlagDto(Aufschlag aufschlag,
			AufschlagDto aufschlagDto) {
		aufschlag.setMandantCNr(aufschlagDto.getMandantCNr());
		aufschlag.setCBez(aufschlagDto.getCBez());
		aufschlag.setFAufschlag(aufschlagDto.getFAufschlag());
		aufschlag.setBMaterial(aufschlagDto.getBMaterial());

		em.merge(aufschlag);
		em.flush();
	}

	private AgstklDto assembleAgstklDto(Agstkl agstkl) {
		return AgstklDtoAssembler.createDto(agstkl);
	}

	private AgstklDto[] assembleAgstklDtos(Collection<?> agstkls) {
		List<AgstklDto> list = new ArrayList<AgstklDto>();
		if (agstkls != null) {
			Iterator<?> iterator = agstkls.iterator();
			while (iterator.hasNext()) {
				Agstkl agstkl = (Agstkl) iterator.next();
				list.add(assembleAgstklDto(agstkl));
			}
		}
		AgstklDto[] returnArray = new AgstklDto[list.size()];
		return (AgstklDto[]) list.toArray(returnArray);
	}

	public void createAgstklpositionsart(
			AgstklpositionsartDto agstklpositionsartDto) throws EJBExceptionLP {
		if (agstklpositionsartDto == null) {
			return;
		}
		try {

			Agstklpositionsart agstklpositionsart = new Agstklpositionsart(
					agstklpositionsartDto.getPositionsartCNr(),
					agstklpositionsartDto.getISort());
			em.persist(agstklpositionsart);
			em.flush();
			setAgstklpositionsartFromAgstklpositionsartDto(agstklpositionsart,
					agstklpositionsartDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
		}
	}

	public void removeAgstklpositionsart(String positionsartCNr)
			throws EJBExceptionLP {
		try {
			Agstklpositionsart toRemove = em.find(Agstklpositionsart.class,
					positionsartCNr);
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei removeAgstklPositionart. Es gibt keine Positionsart "
								+ positionsartCNr);
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						er);
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void removeAgstklpositionsart(
			AgstklpositionsartDto agstklpositionsartDto) throws EJBExceptionLP {
		if (agstklpositionsartDto != null) {
			String positionsartCNr = agstklpositionsartDto.getPositionsartCNr();
			removeAgstklpositionsart(positionsartCNr);
		}
	}

	public void updateAgstklpositionsart(
			AgstklpositionsartDto agstklpositionsartDto) throws EJBExceptionLP {
		if (agstklpositionsartDto != null) {
			String positionsartCNr = agstklpositionsartDto.getPositionsartCNr();
			try {
				Agstklpositionsart agstklpositionsart = em.find(
						Agstklpositionsart.class, positionsartCNr);
				setAgstklpositionsartFromAgstklpositionsartDto(
						agstklpositionsart, agstklpositionsartDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public AgstklpositionsartDto agstklpositionsartFindByPrimaryKey(
			String positionsartCNr) throws EJBExceptionLP {
		try {
			Agstklpositionsart agstklpositionsart = em.find(
					Agstklpositionsart.class, positionsartCNr);
			if (agstklpositionsart == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei agstklpositionartfindbypriomaryKey. Es gibt keine Positionsart "
								+ positionsartCNr);
			}
			return assembleAgstklpositionsartDto(agstklpositionsart);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	private void setAgstklpositionsartFromAgstklpositionsartDto(
			Agstklpositionsart agstklpositionsart,
			AgstklpositionsartDto agstklpositionsartDto) {
		agstklpositionsart.setISort(agstklpositionsartDto.getISort());
		em.merge(agstklpositionsart);
		em.flush();
	}

	private AgstklpositionsartDto assembleAgstklpositionsartDto(
			Agstklpositionsart agstklpositionsart) {
		return AgstklpositionsartDtoAssembler.createDto(agstklpositionsart);
	}

	private AgstklpositionsartDto[] assembleAgstklpositionsartDtos(
			Collection<?> agstklpositionsarts) {
		List<AgstklpositionsartDto> list = new ArrayList<AgstklpositionsartDto>();
		if (agstklpositionsarts != null) {
			Iterator<?> iterator = agstklpositionsarts.iterator();
			while (iterator.hasNext()) {
				Agstklpositionsart agstklpositionsart = (Agstklpositionsart) iterator
						.next();
				list.add(assembleAgstklpositionsartDto(agstklpositionsart));
			}
		}
		AgstklpositionsartDto[] returnArray = new AgstklpositionsartDto[list
				.size()];
		return (AgstklpositionsartDto[]) list.toArray(returnArray);
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

	public String getAngeboteDieBestimmteAngebotsstuecklisteVerwenden(
			Integer agstklIId, TheClientDto theClientDto) throws EJBExceptionLP {

		String sText = "";

		FLRAngebotposition flrStuecklisteposition = new FLRAngebotposition();
		flrStuecklisteposition.setAgstkl_i_id(agstklIId);

		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria crit = session.createCriteria(
				FLRAngebotposition.class).add(
				Example.create(flrStuecklisteposition)); // .createAlias(
		// AngebotpositionFac
		// .
		// FLR_ANGEBOTPOSITION_FLRANGEBOT, "a");
		crit.addOrder(Order.desc("i_id"));

		List<?> resultList = crit.list();

		Iterator<?> resultListIterator = resultList.iterator();
		FLRAngebot letztesAngebot = null;
		while (resultListIterator.hasNext()) {
			FLRAngebotposition flrAngebotposition = (FLRAngebotposition) resultListIterator
					.next();
			if (letztesAngebot == null
					|| letztesAngebot
							.equals(flrAngebotposition.getFlrangebot())) {
				letztesAngebot = flrAngebotposition.getFlrangebot();

				sText = sText + flrAngebotposition.getFlrangebot().getC_nr();
				if (flrAngebotposition.getFlrangebot().getC_bez() != null) {
					sText = sText + ", "
							+ flrAngebotposition.getFlrangebot().getC_bez();
				}
				sText = sText
						+ ", "
						+ flrAngebotposition.getFlrangebot().getFlrkunde()
								.getFlrpartner()
								.getC_name1nachnamefirmazeile1();
				sText = sText + "\n";
			}
		}
		session.close();
		return sText;
	}

	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;

		String fieldName = field.getName();

		if (sAktuellerReport.equals(AngebotstklFac.REPORT_ANGEBOTSTUECKLISTE)) {

			if ("Nummer".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_NR];
			} else if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_ARTIKELNUMMER];
			} else if ("Artikelart".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_ARTIKELART];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_ARTIKELBEZEICHNUNG];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_MENGE];
			} else if ("Mengeneinheit".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_MENGENEINHEIT];
			} else if ("Einzelpreis".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_EINZELPREIS];
			} else if ("Nettogesamtpreis".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_NETTOGESAMTPREIS];
			} else if ("Nettopreis".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_NETTOPREIS];
			} else if ("Rabattsatz".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_RABATTSATZ];
			} else if ("Zusatzrabattsatz".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_ZUSATZRABATTSATZ];
			} else if ("Aufschlag".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_AUFSCHLAG];
			} else if ("Gestpreis".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_GESTPREIS];
			} else if ("Gestpreisgesamt".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_GESTPREISGESAMT];
			} else if ("F_POSITIONSART".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_POSITIONSART];
			} else if ("F_POSITIONSOBJEKT".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_POSITIONSOBJEKT];
			} else if ("F_AUFSCHLAG_PROZENT".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_AUFSCHLAG_PROZENT];
			} else if ("F_AUFSCHLAG_BETRAG".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_AUFSCHLAG_BETRAG];
			} else if ("F_NETTOGESAMTPREISMITAUFSCHLAG".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_NETTOGESAMTPREISMITAUFSCHLAG];
			} else if ("F_LIEF1PREIS".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_LIEF1PREIS];
			} else if ("F_LETZTER_GELIEFERT_PREIS".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_LETZTER_GELIEFERT_PREIS];
			} else if ("F_LETZTES_WE_DATUM".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKL_LETZTES_WE_DATUM];
			}

		} else if (sAktuellerReport
				.equals(AngebotstklFac.REPORT_EINKAUFSANGEBOT)) {

			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_BEZEICHNUNG];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_EINHEIT];
			} else if ("Hersteller".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_HERSTELLER];
			} else if ("Herstellernr".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_HERSTELLERNR];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_MENGE];
			} else if ("Mindestbestellmenge".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_MINDESTBESTELLMENGE];
			} else if ("Preis1".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_PREIS1];
			} else if ("Preis2".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_PREIS2];
			} else if ("Preis3".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_PREIS3];
			} else if ("Preis4".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_PREIS4];
			} else if ("Preis5".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_PREIS5];
			} else if ("Verpackungseinheit".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_VERPACKUNGSEINHEIT];
			} else if ("Wiederbeschaffungszeit".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_WIEDERBESCHAFFUNGSZEIT];
			} else if ("Position".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_POSITION];
			} else if ("Bemerkung".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_BEMERKUNG];
			} else if ("InterneBemerkung".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_INTERNE_BEMERKUNG];
			} else if ("InterneBemerkungMitdrucken".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_BEMERKUNG_MITDRUCKEN];
			} else if ("Kommentar1".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_KOMMENTAR1];
			} else if ("Kommentar2".equals(fieldName)) {
				value = data[index][REPORT_EINKAUFSANGEBOT_KOMMENTAR2];
			}

		}
		return value;
	}

	/**
	 * Liefert den kalkulatorischen Wert einer Angebotsstueckliste.
	 * 
	 * @param iIdAgstklI
	 *            PK der Agstkl
	 * @param cNrWaehrungI
	 *            die gewuenschte Waehrung
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return BigDecimal der kalkulatorische Wert
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public BigDecimal berechneKalkulatorischenAgstklwert(Integer iIdAgstklI,
			String cNrWaehrungI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (iIdAgstklI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("iIdAgstklI == null"));
		}

		if (cNrWaehrungI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("cNrWaehrungI == null"));
		}

		BigDecimal nWert = new BigDecimal(0);

		try {
			AgstklpositionDto[] aAgstklpositionDto = getAngebotstklpositionFac()
					.agstklpositionFindByAgstklIIdMengeNotNullOhneExc(
							iIdAgstklI, theClientDto);

			if (aAgstklpositionDto != null) {
				for (int i = 0; i < aAgstklpositionDto.length; i++) {
					AgstklpositionDto agstklpositionDto = aAgstklpositionDto[i];

					// alle positiven mengenbehafteten Positionen
					// beruecksichtigen
					if (agstklpositionDto.getNMenge() != null
							&& agstklpositionDto.getNMenge().doubleValue() > 0) {
						BigDecimal nMenge = agstklpositionDto.getNMenge();
						// Grundlage ist der Nettogesamtwert der Position in
						// Agstklwaehrung
						BigDecimal nWertDerPosition = nMenge
								.multiply(agstklpositionDto
										.getNNettogesamtpreis());
						// PJ18040 Aufschlaege hinzufuegen
						boolean bMaterial = true;
						if (agstklpositionDto.getArtikelIId() != null) {
							ArtikelDto aDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(
											agstklpositionDto.getArtikelIId(),
											theClientDto);
							if (aDto.getArtikelartCNr().equals(
									ArtikelFac.ARTIKELART_ARBEITSZEIT) == true) {
								bMaterial = false;
							}
						}

						AufschlagDto[] aufschlagDtos = aufschlagFindByBMaterial(
								agstklpositionDto.getAgstklIId(),
								Helper.boolean2Short(bMaterial), theClientDto);

						Double dAufschlaege = 0D;

						for (int k = 0; k < aufschlagDtos.length; k++) {

							dAufschlaege = dAufschlaege
									+ aufschlagDtos[k].getAgstklaufschlagDto()
											.getFAufschlag();

						}
						nWertDerPosition = nWertDerPosition.add(Helper
								.getProzentWert(nWertDerPosition,
										new BigDecimal(dAufschlaege), 2));

						nWert = nWert.add(nWertDerPosition);
					}

					// der kalkulatorische Wert ist nun in der Waehrung der
					// Agstkl bekannt
					AgstklDto agstklDto = agstklFindByPrimaryKey(iIdAgstklI);

					if (!cNrWaehrungI.equals(agstklDto.getWaehrungCNr())) {
						nWert = getLocaleFac().rechneUmInAndereWaehrungZuDatum(
								nWert, agstklDto.getWaehrungCNr(),
								cNrWaehrungI,
								new Date(System.currentTimeMillis()),
								theClientDto);
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return nWert;
	}

	public BigDecimal[] berechneAgstklMaterialwertUndArbeitszeitwert(
			Integer iIdAgstklI, TheClientDto theClientDto) {

		if (iIdAgstklI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("iIdAgstklI == null"));
		}

		BigDecimal[] nWerte = new BigDecimal[2];
		nWerte[0] = new BigDecimal(0);
		nWerte[1] = new BigDecimal(0);

		try {
			AgstklpositionDto[] aAgstklpositionDto = getAngebotstklpositionFac()
					.agstklpositionFindByAgstklIIdMengeNotNullOhneExc(
							iIdAgstklI, theClientDto);

			if (aAgstklpositionDto != null) {
				for (int i = 0; i < aAgstklpositionDto.length; i++) {
					AgstklpositionDto agstklpositionDto = aAgstklpositionDto[i];

					// alle positiven mengenbehafteten Positionen
					// beruecksichtigen
					if (agstklpositionDto.getNMenge() != null
							&& agstklpositionDto.getNMenge().doubleValue() > 0) {

						boolean bArbeitszeit = false;
						if (agstklpositionDto.getArtikelIId() != null) {
							ArtikelDto aDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(
											agstklpositionDto.getArtikelIId(),
											theClientDto);
							if (aDto.getArtikelartCNr().equals(
									ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
								bArbeitszeit = true;
							}
						}

						BigDecimal nMenge = agstklpositionDto.getNMenge();

						// Grundlage ist der Nettogesamtwert der Position in
						// Agstklwaehrung
						BigDecimal nWertDerPosition = nMenge
								.multiply(agstklpositionDto
										.getNNettogesamtpreis());

						if (bArbeitszeit == false) {
							nWerte[0] = nWerte[0].add(nWertDerPosition);
						} else {
							nWerte[1] = nWerte[1].add(nWertDerPosition);
						}

					}

					// der kalkulatorische Wert ist nun in der Waehrung der
					// Agstkl bekannt

				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return nWerte;
	}

	public void vertauscheEinkausangebotpositionen(Integer idPosition1I,
			Integer idPosition2I) throws EJBExceptionLP {
		myLogger.entry();

		// try {
		Einkaufsangebotposition oPosition1 = em.find(
				Einkaufsangebotposition.class, idPosition1I);
		if (oPosition1 == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER,
					"Fehler bei vertauscheEinkaufsangebotpositionen. Es gibt keine Einkaufsangebotpos mit der iid "
							+ idPosition1I);
		}

		Einkaufsangebotposition oPosition2 = em.find(
				Einkaufsangebotposition.class, idPosition2I);

		Integer iSort1 = oPosition1.getISort();
		Integer iSort2 = oPosition2.getISort();

		// iSort der zweiten Position auf ungueltig setzen, damit UK constraint
		// nicht verletzt wird
		oPosition2.setISort(new Integer(-1));

		oPosition1.setISort(iSort2);
		oPosition2.setISort(iSort1);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		// }
	}

	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
			Integer agstklIId, int iSortierungNeuePositionI)
			throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("EinkaufsangebotpositionfindByEinkaufsangebotIId");
		query.setParameter(1, agstklIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		Iterator<?> it = cl.iterator();

		while (it.hasNext()) {
			Agstklposition oPreisliste = (Agstklposition) it.next();

			if (oPreisliste.getISort().intValue() >= iSortierungNeuePositionI) {
				iSortierungNeuePositionI++;
				oPreisliste.setISort(new Integer(iSortierungNeuePositionI));
			}
		}
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		// }

	}

	public Integer createEinkaufsangebot(EinkaufsangebotDto einkaufsangebotDto,
			TheClientDto theClientDto) {
		if (einkaufsangebotDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("einkaufsangebotDto == null"));
		}
		if (einkaufsangebotDto.getKundeIId() == null
				|| einkaufsangebotDto.getNMenge1() == null
				|| einkaufsangebotDto.getNMenge2() == null
				|| einkaufsangebotDto.getNMenge3() == null
				|| einkaufsangebotDto.getNMenge4() == null
				|| einkaufsangebotDto.getNMenge5() == null
				|| einkaufsangebotDto.getTBelegdatum() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"einkaufsangebotDto.getKundeIId() == null || einkaufsangebotDto.getNMenge1() == null || einkaufsangebotDto.getNMenge2() == null || einkaufsangebotDto.getNMenge3() == null || einkaufsangebotDto.getNMenge4() == null || einkaufsangebotDto.getNMenge5() == null || einkaufsangebotDto.getTBelegdatum() == null"));
		}

		try {

			// generieren von primary key & auftragsnummer
			LpBelegnummerFormat f = getBelegnummerGeneratorObj()
					.getBelegnummernFormat(theClientDto.getMandant());
			LpBelegnummer bnr = getBelegnummerGeneratorObj().getNextBelegNr(
					PKConst.PK_EINKAUFSANGEBOT, theClientDto.getMandant(),
					theClientDto);

			einkaufsangebotDto.setIId(bnr.getPrimaryKey());
			einkaufsangebotDto.setCNr(f.format(bnr));
			einkaufsangebotDto.setMandantCNr(theClientDto.getMandant());

			einkaufsangebotDto.setPersonalIIdAnlegen(theClientDto
					.getIDPersonal());
			einkaufsangebotDto.setPersonalIIdAendern(theClientDto
					.getIDPersonal());
			einkaufsangebotDto.setTAnlegen(new Timestamp(System
					.currentTimeMillis()));
			einkaufsangebotDto.setTAendern(new Timestamp(System
					.currentTimeMillis()));

			Einkaufsangebot einkaufsangebot = new Einkaufsangebot(
					einkaufsangebotDto.getIId(),
					einkaufsangebotDto.getMandantCNr(),
					einkaufsangebotDto.getCNr(),
					einkaufsangebotDto.getTBelegdatum(),
					einkaufsangebotDto.getKundeIId(),
					einkaufsangebotDto.getNMenge1(),
					einkaufsangebotDto.getNMenge2(),
					einkaufsangebotDto.getNMenge3(),
					einkaufsangebotDto.getNMenge4(),
					einkaufsangebotDto.getNMenge5(),
					einkaufsangebotDto.getPersonalIIdAnlegen(),
					einkaufsangebotDto.getPersonalIIdAendern());
			em.persist(einkaufsangebot);
			em.flush();
			setEinkaufsangebotFromEinkaufsangebotDto(einkaufsangebot,
					einkaufsangebotDto);
			return einkaufsangebotDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeEinkaufsangebot(EinkaufsangebotDto einkaufsangebotDto)
			throws EJBExceptionLP {
		if (einkaufsangebotDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("einkaufsangebotDto == null"));
		}
		if (einkaufsangebotDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("einkaufsangebotDto.getIId() == null"));
		}

		Integer iId = einkaufsangebotDto.getIId();
		// try {
		// try {
		Einkaufsangebot toRemove = em.find(Einkaufsangebot.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeEinkaufsangebot. Es gibt keine iid "
							+ iId + "\neinkaufsangebotDto.toString: "
							+ einkaufsangebotDto.toString());
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN, ex);
		// }
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		// }

	}

	public void updateEinkaufsangebot(EinkaufsangebotDto einkaufsangebotDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (einkaufsangebotDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("einkaufsangebotDto == null"));
		}
		if (einkaufsangebotDto.getIId() == null
				|| einkaufsangebotDto.getKundeIId() == null
				|| einkaufsangebotDto.getNMenge1() == null
				|| einkaufsangebotDto.getNMenge2() == null
				|| einkaufsangebotDto.getNMenge3() == null
				|| einkaufsangebotDto.getNMenge4() == null
				|| einkaufsangebotDto.getNMenge5() == null
				|| einkaufsangebotDto.getTBelegdatum() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"einkaufsangebotDto.getIId() == null || einkaufsangebotDto.getKundeIId() == null || einkaufsangebotDto.getNMenge1() == null || einkaufsangebotDto.getNMenge2() == null || einkaufsangebotDto.getNMenge3() == null || einkaufsangebotDto.getNMenge4() == null || einkaufsangebotDto.getNMenge5() == null || einkaufsangebotDto.getTBelegdatum() == null"));
		}

		Integer iId = einkaufsangebotDto.getIId();

		einkaufsangebotDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		einkaufsangebotDto
				.setTAendern(new Timestamp(System.currentTimeMillis()));

		// try {
		Einkaufsangebot einkaufsangebot = em.find(Einkaufsangebot.class, iId);
		if (einkaufsangebot == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateEinkaufsangebot. es gibt kein Einkaufsangebot mit der iid "
							+ einkaufsangebotDto.getIId()
							+ "\neinkaufsangebotDto.toString(): "
							+ einkaufsangebotDto.toString());

		}
		setEinkaufsangebotFromEinkaufsangebotDto(einkaufsangebot,
				einkaufsangebotDto);
		// }
		// catch (FinderException ex1) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex1);

		// }

	}

	public EinkaufsangebotDto einkaufsangebotFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Einkaufsangebot einkaufsangebot = em.find(Einkaufsangebot.class, iId);
		if (einkaufsangebot == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei FindByPrimaryKey. es gibt keine Einkaufsangebot mit der iid "
							+ iId);
		}
		return assembleEinkaufsangebotDto(einkaufsangebot);
		// }
		// catch (FinderException fe) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// fe);

		// }

	}

	private void setEinkaufsangebotFromEinkaufsangebotDto(
			Einkaufsangebot einkaufsangebot,
			EinkaufsangebotDto einkaufsangebotDto) {
		einkaufsangebot.setMandantCNr(einkaufsangebotDto.getMandantCNr());
		einkaufsangebot.setCNr(einkaufsangebotDto.getCNr());
		einkaufsangebot.setTBelegdatum(einkaufsangebotDto.getTBelegdatum());
		einkaufsangebot.setCProjekt(einkaufsangebotDto.getCProjekt());
		einkaufsangebot.setKundeIId(einkaufsangebotDto.getKundeIId());
		einkaufsangebot.setAnsprechpartnerIId(einkaufsangebotDto
				.getAnsprechpartnerIId());
		einkaufsangebot.setNMenge1(einkaufsangebotDto.getNMenge1());
		einkaufsangebot.setNMenge2(einkaufsangebotDto.getNMenge2());
		einkaufsangebot.setNMenge3(einkaufsangebotDto.getNMenge3());
		einkaufsangebot.setNMenge4(einkaufsangebotDto.getNMenge4());
		einkaufsangebot.setNMenge5(einkaufsangebotDto.getNMenge5());
		einkaufsangebot.setPersonalIIdAnlegen(einkaufsangebotDto
				.getPersonalIIdAnlegen());
		einkaufsangebot.setTAnlegen(einkaufsangebotDto.getTAnlegen());
		einkaufsangebot.setPersonalIIdAendern(einkaufsangebotDto
				.getPersonalIIdAendern());
		einkaufsangebot.setTAendern(einkaufsangebotDto.getTAendern());
		em.merge(einkaufsangebot);
		em.flush();
	}

	private EinkaufsangebotDto assembleEinkaufsangebotDto(
			Einkaufsangebot einkaufsangebot) {
		return EinkaufsangebotDtoAssembler.createDto(einkaufsangebot);
	}

	private EinkaufsangebotDto[] assembleEinkaufsangebotDtos(
			Collection<?> einkaufsangebots) {
		List<EinkaufsangebotDto> list = new ArrayList<EinkaufsangebotDto>();
		if (einkaufsangebots != null) {
			Iterator<?> iterator = einkaufsangebots.iterator();
			while (iterator.hasNext()) {
				Einkaufsangebot einkaufsangebot = (Einkaufsangebot) iterator
						.next();
				list.add(assembleEinkaufsangebotDto(einkaufsangebot));
			}
		}
		EinkaufsangebotDto[] returnArray = new EinkaufsangebotDto[list.size()];
		return (EinkaufsangebotDto[]) list.toArray(returnArray);
	}

	public Integer createEinkaufsangebotposition(
			EinkaufsangebotpositionDto einkaufsangebotpositionDto,
			TheClientDto theClientDto) {
		if (einkaufsangebotpositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("einkaufsangebotpositionDto == null"));
		}
		if (einkaufsangebotpositionDto.getBelegIId() == null
				|| einkaufsangebotpositionDto
						.getBArtikelbezeichnunguebersteuert() == null
				|| einkaufsangebotpositionDto.getPositionsartCNr() == null
				|| einkaufsangebotpositionDto.getBMitdrucken() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"einkaufsangebotpositionDto.getBelegIId() == null || einkaufsangebotpositionDto.getBArtikelbezeichnunguebersteuert() == null || einkaufsangebotpositionDto.getAgstklpositionsartCNr() == null"));
		}

		Integer einkaufsangebotpositionIId = null;

		einkaufsangebotpositionDto.setNMenge(Helper.rundeKaufmaennisch(
				einkaufsangebotpositionDto.getNMenge(), 4));

		try {

			if (einkaufsangebotpositionDto
					.getPositionsartCNr()
					.equalsIgnoreCase(
							AngebotstklServiceFac.AGSTKLPOSITIONART_HANDEINGABE)) {
				einkaufsangebotpositionDto.setArtikelIId(null);
			}

			// generieren von primary key
			einkaufsangebotpositionIId = getPKGeneratorObj().getNextPrimaryKey(
					PKConst.PK_EINKAUFSANGEBOTPOSITION);

			einkaufsangebotpositionDto.setIId(einkaufsangebotpositionIId);
			if (einkaufsangebotpositionDto.getISort() == null) {
				Query query = em
						.createNamedQuery("EinkaufsangebotpositionejbSelectMaxISort");
				query.setParameter(1, einkaufsangebotpositionDto.getBelegIId());
				Integer i = (Integer) query.getSingleResult();
				if (i == null) {
					i = new Integer(0);
				}
				i = new Integer(i.intValue() + 1);
				einkaufsangebotpositionDto.setISort(i);
			}
			Einkaufsangebotposition einkaufsangebotposition = new Einkaufsangebotposition(
					einkaufsangebotpositionDto.getIId(),
					einkaufsangebotpositionDto.getBelegIId(),
					einkaufsangebotpositionDto.getPositionsartCNr(),
					einkaufsangebotpositionDto
							.getBArtikelbezeichnunguebersteuert(),
					einkaufsangebotpositionDto.getBMitdrucken());
			em.persist(einkaufsangebotposition);
			em.flush();
			setEinkaufsangebotpositionFromEinkaufsangebotpositionDto(
					einkaufsangebotposition, einkaufsangebotpositionDto);

		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(t));
		}
		return einkaufsangebotpositionIId;

	}

	public void removeEinkaufsangebotposition(
			EinkaufsangebotpositionDto einkaufsangebotpositionDto)
			throws EJBExceptionLP {
		if (einkaufsangebotpositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("einkaufsangebotpositionDto == null"));
		}
		if (einkaufsangebotpositionDto.getIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("einkaufsangebotpositionDto.getIId() == null"));
		}
		// try {
		Einkaufsangebotposition toRemove = em.find(
				Einkaufsangebotposition.class,
				einkaufsangebotpositionDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeEinkaufsangebotposition. Es gibt keine Position mit iid "
							+ einkaufsangebotpositionDto.getIId()
							+ "\neinkaufsangebotpositionDto.toString: "
							+ einkaufsangebotpositionDto.toString());
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN, ex);
		// }

	}

	public void updateEinkaufsangebotposition(
			EinkaufsangebotpositionDto einkaufsangebotpositionDto)
			throws EJBExceptionLP {

		if (einkaufsangebotpositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("einkaufsangebotpositionDto == null"));
		}
		if (einkaufsangebotpositionDto.getIId() == null
				|| einkaufsangebotpositionDto.getBelegIId() == null
				|| einkaufsangebotpositionDto
						.getBArtikelbezeichnunguebersteuert() == null
				|| einkaufsangebotpositionDto.getPositionsartCNr() == null
				|| einkaufsangebotpositionDto.getBMitdrucken() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"einkaufsangebotpositionDto.getIId() == null || einkaufsangebotpositionDto.getBelegIId() == null || einkaufsangebotpositionDto.getBArtikelbezeichnunguebersteuert() == null || einkaufsangebotpositionDto.getAgstklpositionsartCNr() == null"));
		}

		if (einkaufsangebotpositionDto.getPositionsartCNr().equalsIgnoreCase(
				AngebotstklServiceFac.AGSTKLPOSITIONART_HANDEINGABE)) {
			einkaufsangebotpositionDto.setArtikelIId(null);
		}
		einkaufsangebotpositionDto.setNMenge(Helper.rundeKaufmaennisch(
				einkaufsangebotpositionDto.getNMenge(), 4));
		Integer iId = einkaufsangebotpositionDto.getIId();
		// try {
		Einkaufsangebotposition einkaufsangebotposition = em.find(
				Einkaufsangebotposition.class, iId);
		if (einkaufsangebotposition == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateEinkaufsangebotsposition. Es gibt keine iid "
							+ iId + "\neinkaufsangebotpositionDto.toString: "
							+ einkaufsangebotpositionDto.toString());
		}
		setEinkaufsangebotpositionFromEinkaufsangebotpositionDto(
				einkaufsangebotposition, einkaufsangebotpositionDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public void kopierePositionenAusStueckliste(Integer stuecklisteIId,
			Integer agstklIId, TheClientDto theClientDto) {
		StuecklistepositionDto[] stklPosDtos = getStuecklisteFac()
				.stuecklistepositionFindByStuecklisteIId(stuecklisteIId,
						theClientDto);

		try {
			AgstklDto agstklDto = agstklFindByPrimaryKey(agstklIId);

			for (int i = 0; i < stklPosDtos.length; i++) {
				StuecklistepositionDto stklPosDto = stklPosDtos[i];

				AgstklpositionDto agstklpositionDtoI = new AgstklpositionDto();
				agstklpositionDtoI.setAgstklIId(agstklIId);
				agstklpositionDtoI.setNMenge(stklPosDto.getNMenge());
				agstklpositionDtoI.setBArtikelbezeichnunguebersteuert(Helper
						.boolean2Short(false));
				agstklpositionDtoI.setBDrucken(stklPosDto.getBMitdrucken());

				ArtikelDto a = getArtikelFac().artikelFindByPrimaryKey(
						stklPosDto.getArtikelIId(), theClientDto);
				agstklpositionDtoI.setEinheitCNr(a.getEinheitCNr());
				agstklpositionDtoI.setArtikelIId(a.getIId());
				if (a.getArtikelartCNr().equals(
						ArtikelFac.ARTIKELART_HANDARTIKEL)) {
					agstklpositionDtoI
							.setAgstklpositionsartCNr(AngebotstklServiceFac.AGSTKLPOSITIONART_HANDEINGABE);
				} else {
					agstklpositionDtoI
							.setAgstklpositionsartCNr(AngebotstklServiceFac.AGSTKLPOSITIONART_IDENT);

				}

				agstklpositionDtoI.setBNettopreisuebersteuert(Helper
						.boolean2Short(false));

				BigDecimal gestpreis = getLagerFac()
						.getGemittelterGestehungspreisEinesLagers(
								stklPosDto.getArtikelIId(),
								getLagerFac().getHauptlagerDesMandanten(
										theClientDto).getIId(), theClientDto);
				agstklpositionDtoI.setNGestehungspreis(gestpreis);

				ParametermandantDto parameterMand = getParameterFac()
						.getMandantparameter(theClientDto.getMandant(),
								ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE,
								ParameterFac.PARAMETER_KALKULATIONSART);
				int iKalkulationsart = (Integer) parameterMand
						.getCWertAsObject();
				// EK-Preisbezug
				if (iKalkulationsart == 2 || iKalkulationsart == 3) {

					befuellePositionMitPreisenKalkulationsart2(theClientDto,
							agstklDto.getWaehrungCNr(),
							stklPosDto.getArtikelIId(), stklPosDto.getNMenge(),
							agstklpositionDtoI);
					
					if (iKalkulationsart == 3) {
						//SP2064
					
							parameterMand = getParameterFac().getMandantparameter(
									theClientDto.getMandant(),
									ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE,
									ParameterFac.PARAMETER_DEFAULT_AUFSCHLAG);
							Double aufschlag = (Double) parameterMand
									.getCWertAsObject();
							agstklpositionDtoI.setFAufschlag(aufschlag);

							agstklpositionDtoI.setBAufschlaggesamtFixiert(Helper
									.boolean2Short(false));

							BigDecimal bdAufschlag = Helper.getProzentWert(agstklpositionDtoI
									.getNNettogesamtpreis(), new BigDecimal(
									aufschlag), 4);

							agstklpositionDtoI.setNAufschlag(bdAufschlag);

							agstklpositionDtoI.setNNettogesamtmitaufschlag(agstklpositionDtoI
									.getNNettogesamtpreis().add(bdAufschlag));

						
					}
					
				} else {
					agstklpositionDtoI.setFRabattsatz(0D);
					agstklpositionDtoI.setNNettoeinzelpreis(gestpreis);
					agstklpositionDtoI.setNNettogesamtpreis(gestpreis);
					agstklpositionDtoI.setBRabattsatzuebersteuert(Helper
							.boolean2Short(false));
					agstklpositionDtoI.setBNettopreisuebersteuert(Helper
							.boolean2Short(true));
					
					
					
				}

				getAngebotstklpositionFac().createAgstklposition(
						agstklpositionDtoI, theClientDto);

			}

			StuecklistearbeitsplanDto[] arbeitsplanPos = getStuecklisteFac()
					.stuecklistearbeitsplanFindByStuecklisteIId(stuecklisteIId,
							theClientDto);
			for (int i = 0; i < arbeitsplanPos.length; i++) {
				StuecklistearbeitsplanDto arbeitsplanPosDto = arbeitsplanPos[i];

				AgstklpositionDto agstklpositionDtoI = new AgstklpositionDto();
				agstklpositionDtoI.setAgstklIId(agstklIId);

				agstklpositionDtoI.setNMenge(Helper
						.berechneGesamtzeitInStunden(
								arbeitsplanPosDto.getLRuestzeit(),
								arbeitsplanPosDto.getLStueckzeit(),
								new BigDecimal(1), null, null));
				agstklpositionDtoI.setBArtikelbezeichnunguebersteuert(Helper
						.boolean2Short(false));
				agstklpositionDtoI.setBDrucken(Helper.boolean2Short(true));

				ArtikelDto a = getArtikelFac().artikelFindByPrimaryKey(
						arbeitsplanPosDto.getArtikelIId(), theClientDto);
				agstklpositionDtoI.setArtikelIId(a.getIId());

				agstklpositionDtoI
						.setAgstklpositionsartCNr(AngebotstklServiceFac.AGSTKLPOSITIONART_IDENT);

				agstklpositionDtoI.setBNettopreisuebersteuert(Helper
						.boolean2Short(false));

				BigDecimal gestpreis = getLagerFac()
						.getGemittelterGestehungspreisEinesLagers(
								arbeitsplanPosDto.getArtikelIId(),
								getLagerFac().getHauptlagerDesMandanten(
										theClientDto).getIId(), theClientDto);
				agstklpositionDtoI.setNGestehungspreis(gestpreis);

				befuellePositionMitPreisenKalkulationsart2(theClientDto,
						agstklDto.getWaehrungCNr(),
						arbeitsplanPosDto.getArtikelIId(),
						agstklpositionDtoI.getNMenge(), agstklpositionDtoI);

				getAngebotstklpositionFac().createAgstklposition(
						agstklpositionDtoI, theClientDto);

			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

	}

	public AgstklpositionDto befuellePositionMitPreisenKalkulationsart2(
			TheClientDto theClientDto, String waehrungCNr, Integer artikelIId,
			BigDecimal nMenge, AgstklpositionDto agstklpositionDtoI) {
		int iNachkommastellen = 2;
		ArtikellieferantDto artliefDto = null;
		try {
			iNachkommastellen = getMandantFac().getNachkommastellenPreisEK(
					theClientDto.getMandant());

			artliefDto = getArtikelFac().getArtikelEinkaufspreis(artikelIId,
					nMenge, waehrungCNr, theClientDto);
		} catch (EJBExceptionLP e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		if (artliefDto != null && artliefDto.getNEinzelpreis() != null) {

			agstklpositionDtoI.setFRabattsatz(artliefDto.getFRabatt());
			agstklpositionDtoI.setNNettoeinzelpreis(artliefDto
					.getNEinzelpreis());
			agstklpositionDtoI.setNNettogesamtpreis(artliefDto
					.getNEinzelpreis().subtract(
							Helper.getProzentWert(artliefDto.getNEinzelpreis(),
									new BigDecimal(artliefDto.getFRabatt()),
									iNachkommastellen)));

			if (Helper.short2boolean(artliefDto.getBRabattbehalten())) {
				agstklpositionDtoI.setBRabattsatzuebersteuert(Helper
						.boolean2Short(true));
				agstklpositionDtoI.setBNettopreisuebersteuert(Helper
						.boolean2Short(false));
			} else {
				agstklpositionDtoI.setBNettopreisuebersteuert(Helper
						.boolean2Short(true));
				agstklpositionDtoI.setBRabattsatzuebersteuert(Helper
						.boolean2Short(false));
			}

		} else {
			agstklpositionDtoI.setFRabattsatz(0D);
			agstklpositionDtoI.setNNettoeinzelpreis(new BigDecimal(0));
			agstklpositionDtoI.setNNettogesamtpreis(new BigDecimal(0));
			agstklpositionDtoI.setBRabattsatzuebersteuert(Helper
					.boolean2Short(false));
			agstklpositionDtoI.setBNettopreisuebersteuert(Helper
					.boolean2Short(true));
		}
		return agstklpositionDtoI;
	}

	public EinkaufsangebotpositionDto einkaufsangebotpositionFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP {

		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Einkaufsangebotposition einkaufsangebotposition = em.find(
				Einkaufsangebotposition.class, iId);
		if (einkaufsangebotposition == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei EinkaufspositionFindByPrimaryKey. Es gibt keine iid "
							+ iId);
		}
		return assembleEinkaufsangebotpositionDto(einkaufsangebotposition);
		// }
		// catch (FinderException fe) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// fe);

		// }

	}

	public Integer einkaufsangebotpositionGetMaxISort(Integer einkaufsangebotIId) {
		Integer iiMaxISortO = null;
		try {
			Query query = em
					.createNamedQuery("EinkaufsangebotpositionejbSelectMaxISort");
			query.setParameter(1, einkaufsangebotIId);
			iiMaxISortO = (Integer) query.getSingleResult();
			if (iiMaxISortO == null) {
				iiMaxISortO = new Integer(0);
			}
		} catch (Throwable e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
					"Fehler. bei einkaufsangebotpositiongetmaxisort trat ein Fehler auf. iid: "
							+ einkaufsangebotIId);
		}
		return iiMaxISortO;
	}

	private void setEinkaufsangebotpositionFromEinkaufsangebotpositionDto(
			Einkaufsangebotposition einkaufsangebotposition,
			EinkaufsangebotpositionDto einkaufsangebotpositionDto) {
		einkaufsangebotposition
				.setEinkaufsangebotIId(einkaufsangebotpositionDto.getBelegIId());
		einkaufsangebotposition.setISort(einkaufsangebotpositionDto.getISort());
		einkaufsangebotposition
				.setAgstklpositionsartCNr(einkaufsangebotpositionDto
						.getPositionsartCNr());
		einkaufsangebotposition.setArtikelIId(einkaufsangebotpositionDto
				.getArtikelIId());
		einkaufsangebotposition.setCBemerkung(einkaufsangebotpositionDto
				.getCBemerkung());
		einkaufsangebotposition.setCBez(einkaufsangebotpositionDto.getCBez());
		einkaufsangebotposition.setCZbez(einkaufsangebotpositionDto
				.getCZusatzbez());
		einkaufsangebotposition
				.setBArtikelbezeichnunguebersteuert(einkaufsangebotpositionDto
						.getBArtikelbezeichnunguebersteuert());
		einkaufsangebotposition.setNMenge(einkaufsangebotpositionDto
				.getNMenge());
		einkaufsangebotposition.setNPreis1(einkaufsangebotpositionDto
				.getNPreis1());
		einkaufsangebotposition.setNPreis2(einkaufsangebotpositionDto
				.getNPreis2());
		einkaufsangebotposition.setNPreis3(einkaufsangebotpositionDto
				.getNPreis3());
		einkaufsangebotposition.setNPreis4(einkaufsangebotpositionDto
				.getNPreis4());
		einkaufsangebotposition.setNPreis5(einkaufsangebotpositionDto
				.getNPreis5());
		einkaufsangebotposition.setEinheitCNr(einkaufsangebotpositionDto
				.getEinheitCNr());

		einkaufsangebotposition
				.setIVerpackungseinheit(einkaufsangebotpositionDto
						.getIVerpackungseinheit());
		einkaufsangebotposition
				.setIWiederbeschaffungszeit(einkaufsangebotpositionDto
						.getIWiederbeschaffungszeit());
		einkaufsangebotposition
				.setFMindestbestellmenge(einkaufsangebotpositionDto
						.getFMindestbestellmenge());
		einkaufsangebotposition.setCPosition(einkaufsangebotpositionDto
				.getCPosition());
		einkaufsangebotposition.setCInternebemerkung(einkaufsangebotpositionDto
				.getCInternebemerkung());
		einkaufsangebotposition.setBMitdrucken(einkaufsangebotpositionDto
				.getBMitdrucken());

		einkaufsangebotposition.setCKommentar1(einkaufsangebotpositionDto
				.getCKommentar1());
		einkaufsangebotposition.setCKommentar2(einkaufsangebotpositionDto
				.getCKommentar2());

		em.merge(einkaufsangebotposition);
		em.flush();
	}

	private EinkaufsangebotpositionDto assembleEinkaufsangebotpositionDto(
			Einkaufsangebotposition einkaufsangebotposition) {
		return EinkaufsangebotpositionDtoAssembler
				.createDto(einkaufsangebotposition);
	}

	private EinkaufsangebotpositionDto[] assembleEinkaufsangebotpositionDtos(
			Collection<?> einkaufsangebotpositions) {
		List<EinkaufsangebotpositionDto> list = new ArrayList<EinkaufsangebotpositionDto>();
		if (einkaufsangebotpositions != null) {
			Iterator<?> iterator = einkaufsangebotpositions.iterator();
			while (iterator.hasNext()) {
				Einkaufsangebotposition einkaufsangebotposition = (Einkaufsangebotposition) iterator
						.next();
				list.add(assembleEinkaufsangebotpositionDto(einkaufsangebotposition));
			}
		}
		EinkaufsangebotpositionDto[] returnArray = new EinkaufsangebotpositionDto[list
				.size()];
		return (EinkaufsangebotpositionDto[]) list.toArray(returnArray);
	}

	public void createAgstkl(AgstklDto agstklDto) {
		if (agstklDto == null) {
			return;
		}
		Agstkl agstkl = new Agstkl(agstklDto.getIId(),
				agstklDto.getMandantCNr(), agstklDto.getCNr(),
				agstklDto.getBelegartCNr(), agstklDto.getKundeIId(),
				agstklDto.getTBelegdatum(), agstklDto.getWaehrungCNr(),
				agstklDto.getFWechselkursmandantwaehrungzuagstklwaehrung(),
				agstklDto.getPersonalIIdAnlegen(),
				agstklDto.getPersonalIIdAendern());
		em.persist(agstkl);
		em.flush();
		setAgstklFromAgstklDto(agstkl, agstklDto);
	}

	public Integer createAufschlag(AufschlagDto aufschlagDto,
			TheClientDto theClientDto) {

		try {
			Query query = em.createNamedQuery("AufschlagFindByMandantCNrCBez");
			query.setParameter(1, aufschlagDto.getMandantCNr());
			query.setParameter(2, aufschlagDto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Aufschlag doppelt = (Aufschlag) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("AS_AUFSCHLAG.UK"));
		} catch (NoResultException ex) {

		}

		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_AUFSCHLAG);
		aufschlagDto.setIId(pk);

		Aufschlag aufschlag = new Aufschlag(aufschlagDto.getIId(),
				aufschlagDto.getMandantCNr(), aufschlagDto.getBMaterial(),
				aufschlagDto.getFAufschlag(), aufschlagDto.getCBez());
		em.persist(aufschlag);
		em.flush();
		setAufschlagFromAufschlagDto(aufschlag, aufschlagDto);
		return aufschlagDto.getIId();
	}

	public void removeAufschlag(Integer aufschlagIId) {
		Aufschlag toRemove = em.find(Aufschlag.class, aufschlagIId);

		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void updateAufschlag(AufschlagDto aufschlagDto,
			TheClientDto theClientDto) {

		Aufschlag aufschlag = em.find(Aufschlag.class, aufschlagDto.getIId());

		try {
			Query query = em.createNamedQuery("AufschlagFindByMandantCNrCBez");
			query.setParameter(1, aufschlagDto.getMandantCNr());
			query.setParameter(2, aufschlagDto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Aufschlag) query.getSingleResult())
					.getIId();
			if (aufschlagDto.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"AS_AUFSCHLAG.UK"));
			}

		} catch (NoResultException ex) {

		}

		setAufschlagFromAufschlagDto(aufschlag, aufschlagDto);
	}

	public AufschlagDto aufschlagFindByPrimaryKey(Integer iId) {
		Aufschlag aufschlag = em.find(Aufschlag.class, iId);
		return AufschlagDtoAssembler.createDto(aufschlag);
	}

	public void removeAgstkl(Integer iId) {
		Agstkl toRemove = em.find(Agstkl.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeAgstkl. Es gibt keine Agstkl mit der iid "
							+ iId);
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void updateAgstklaufschlag(Integer agstklIId,
			AufschlagDto[] aufschlagDtos, TheClientDto theClientDto) {

		for (int i = 0; i < aufschlagDtos.length; i++) {

			try {
				Query query2 = em
						.createNamedQuery("AgstklaufschlagFindByAgstklIIdAufschlagIId");
				query2.setParameter(1, agstklIId);
				query2.setParameter(2, aufschlagDtos[i].getIId());
				Agstklaufschlag agstklaufschlag = (Agstklaufschlag) query2
						.getSingleResult();
				agstklaufschlag.setFAufschlag(aufschlagDtos[i]
						.getAgstklaufschlagDto().getFAufschlag());
			} catch (NoResultException ex) {
				PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
				Integer pk = pkGen
						.getNextPrimaryKey(PKConst.PK_AGSTKLAUFSCHLAG);
				Agstklaufschlag agstklaufschlag = new Agstklaufschlag(pk,
						agstklIId, aufschlagDtos[i].getIId(), aufschlagDtos[i]
								.getAgstklaufschlagDto().getFAufschlag());
				em.merge(agstklaufschlag);
				em.flush();

			}
		}

		if (aufschlagDtos.length > 0) {
			Agstkl agstkl = em.find(Agstkl.class, agstklIId);
			agstkl.setTAendern(new Timestamp(System.currentTimeMillis()));
			agstkl.setPersonalIIdAendern(theClientDto.getIDPersonal());
		}

	}

	public void updateAgstkl(AgstklDto agstklDto) {
		if (agstklDto != null) {
			Integer iId = agstklDto.getIId();

			Agstkl agstkl = em.find(Agstkl.class, iId);
			setAgstklFromAgstklDto(agstkl, agstklDto);
		}
	}

	public void updateAgstkls(AgstklDto[] agstklDtos) throws RemoteException {
		if (agstklDtos != null) {
			for (int i = 0; i < agstklDtos.length; i++) {
				updateAgstkl(agstklDtos[i]);
			}
		}
	}

	public AgstklDto agstklFindByCNrMandantCNr(String cNr, String mandantCNr)
			throws EJBExceptionLP {
		try {
			Query query = em.createNamedQuery("AgstklfindByCNrMandantCNr");
			query.setParameter(1, cNr);
			query.setParameter(2, mandantCNr);
			Agstkl agstkl = (Agstkl) query.getSingleResult();
			if (agstkl == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
						"Fehler bei find agstklbycnrmandantcnr. Es konnte keine Agstkl mit cnr "
								+ cNr + " fuer den Mandanten " + mandantCNr
								+ " gefunden werden");
			}
			return assembleAgstklDto(agstkl);
			// catch (FinderException fe) {
			// throw fe;
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}
	}

	public AgstklDto agstklFindByCNrMandantCNrOhneExc(String cNr,
			String mandantCNr) {
		Query query = em.createNamedQuery("AgstklfindByCNrMandantCNr");
		query.setParameter(1, cNr);
		query.setParameter(2, mandantCNr);
		try {
			Agstkl agstkl = (Agstkl) query.getSingleResult();
			return assembleAgstklDto(agstkl);
		} catch (NoResultException ex) {
			return null;
		}
	}

	public Integer createEinkaufsangebotpositions(
			EinkaufsangebotpositionDto[] einkaufsangebotpositionDtos,
			TheClientDto theClientDto) {
		Integer iId = null;
		for (int i = 0; i < einkaufsangebotpositionDtos.length; i++) {
			iId = createEinkaufsangebotposition(einkaufsangebotpositionDtos[i],
					theClientDto);
		}
		return iId;
	}

	public AgstklDto[] agstklFindByAnsprechpartnerIIdKunde(
			Integer iAnsprechpartnerIId) throws EJBExceptionLP {
		try {
			Query query = em
					.createNamedQuery("AgstklfindByAnsprechpartnerIIdKunde");
			query.setParameter(1, iAnsprechpartnerIId);
			// @todo getSingleResult oder getResultList ?
			return assembleAgstklDtos((Collection<?>) query.getResultList());
			// @ToDo null Pruefung?
			// }
			// catch (FinderException fe) {
			// throw fe;
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}
	}

	public void createEinkaufsangebot(EinkaufsangebotDto einkaufsangebotDto)
			throws EJBExceptionLP {
		if (einkaufsangebotDto == null) {
			return;
		}
		try {
			Einkaufsangebot einkaufsangebot = new Einkaufsangebot(
					einkaufsangebotDto.getIId(),
					einkaufsangebotDto.getMandantCNr(),
					einkaufsangebotDto.getCNr(),
					einkaufsangebotDto.getTBelegdatum(),
					einkaufsangebotDto.getKundeIId(),
					einkaufsangebotDto.getNMenge1(),
					einkaufsangebotDto.getNMenge2(),
					einkaufsangebotDto.getNMenge3(),
					einkaufsangebotDto.getNMenge4(),
					einkaufsangebotDto.getNMenge5(),
					einkaufsangebotDto.getPersonalIIdAnlegen(),
					einkaufsangebotDto.getPersonalIIdAendern());
			em.persist(einkaufsangebot);
			em.flush();
			setEinkaufsangebotFromEinkaufsangebotDto(einkaufsangebot,
					einkaufsangebotDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeEinkaufsangebot(Integer iId) throws EJBExceptionLP {
		try {
			Einkaufsangebot toRemove = em.find(Einkaufsangebot.class, iId);
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei removeEinkaufsangebot. Es gibt keine iid "
								+ iId);
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						er);
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void updateEinkaufsangebot(EinkaufsangebotDto einkaufsangebotDto)
			throws EJBExceptionLP {
		if (einkaufsangebotDto != null) {
			Integer iId = einkaufsangebotDto.getIId();
			try {
				Einkaufsangebot einkaufsangebot = em.find(
						Einkaufsangebot.class, iId);
				setEinkaufsangebotFromEinkaufsangebotDto(einkaufsangebot,
						einkaufsangebotDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public void updateEinkaufsangebots(EinkaufsangebotDto[] einkaufsangebotDtos)
			throws EJBExceptionLP {
		if (einkaufsangebotDtos != null) {
			for (int i = 0; i < einkaufsangebotDtos.length; i++) {
				updateEinkaufsangebot(einkaufsangebotDtos[i]);
			}
		}
	}

	public EinkaufsangebotDto[] einkaufsangebotFindByAnsprechpartnerIId(
			Integer iAnsprechpartnerIId) throws EJBExceptionLP {
		try {
			Query query = em
					.createNamedQuery("EinkaufsangebotfindByAnsprechpartnerIId");
			query.setParameter(1, iAnsprechpartnerIId);
			return assembleEinkaufsangebotDtos((Collection<?>) query
					.getResultList());
			// }
			// catch (FinderException fe) {
			// throw fe;
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}
	}
}
