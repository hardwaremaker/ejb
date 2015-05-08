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

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
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
import com.lp.server.angebotstkl.ejb.Agstklarbeitsplan;
import com.lp.server.angebotstkl.ejb.Agstklaufschlag;
import com.lp.server.angebotstkl.ejb.Agstklmengenstaffel;
import com.lp.server.angebotstkl.ejb.Agstklposition;
import com.lp.server.angebotstkl.ejb.Agstklpositionsart;
import com.lp.server.angebotstkl.ejb.Aufschlag;
import com.lp.server.angebotstkl.ejb.Einkaufsangebot;
import com.lp.server.angebotstkl.ejb.Einkaufsangebotposition;
import com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstklarbeitsplan;
import com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstklposition;
import com.lp.server.angebotstkl.fastlanereader.generated.FLREinkaufsangebotposition;
import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.angebotstkl.service.AgstklDtoAssembler;
import com.lp.server.angebotstkl.service.AgstklarbeitsplanDto;
import com.lp.server.angebotstkl.service.AgstklarbeitsplanDtoAssembler;
import com.lp.server.angebotstkl.service.AgstklaufschlagDto;
import com.lp.server.angebotstkl.service.AgstklaufschlagDtoAssembler;
import com.lp.server.angebotstkl.service.AgstklmengenstaffelDto;
import com.lp.server.angebotstkl.service.AgstklmengenstaffelDtoAssembler;
import com.lp.server.angebotstkl.service.AgstklpositionDto;
import com.lp.server.angebotstkl.service.AgstklpositionsartDto;
import com.lp.server.angebotstkl.service.AgstklpositionsartDtoAssembler;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.angebotstkl.service.AngebotstklServiceFac;
import com.lp.server.angebotstkl.service.AufschlagDto;
import com.lp.server.angebotstkl.service.AufschlagDtoAssembler;
import com.lp.server.angebotstkl.service.EinkaufsagstklImportSpezifikation;
import com.lp.server.angebotstkl.service.EinkaufsangebotDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotDtoAssembler;
import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDtoAssembler;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.HerstellerDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.stueckliste.service.IStklImportResult;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.pkgenerator.format.LpBelegnummer;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.IImportHead;
import com.lp.server.system.service.IImportPositionen;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.service.BelegpositionDto;
import com.lp.service.StklImportSpezifikation;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;

@Stateless
public class AngebotstklFacBean extends LPReport implements AngebotstklFac, IImportPositionen, IImportHead {
	@PersistenceContext
	private EntityManager em;

	@EJB
	private AngebotstklpositionLocalFac angebotstklPositionLocalFac;
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

	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_NR = 0;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARTIKELNUMMER = 1;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARTIKELBEZEICHNUNG = 2;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_MENGE = 3;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_MENGENEINHEIT = 4;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_EINZELPREIS = 5;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_NETTOGESAMTPREIS = 6;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_AUFSCHLAG = 7;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_GESTPREIS = 8;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_GESTPREISGESAMT = 9;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_POSITIONSART = 10;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_POSITIONSOBJEKT = 11;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_NETTOPREIS = 12;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_RABATTSATZ = 13;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ZUSATZRABATTSATZ = 14;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_AUFSCHLAG_PROZENT = 15;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_AUFSCHLAG_BETRAG = 16;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_NETTOGESAMTPREISMITAUFSCHLAG = 17;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_LIEF1PREIS = 18;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_LETZTER_GELIEFERT_PREIS = 19;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_LETZTES_WE_DATUM = 20;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARTIKELART = 21;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_VKPREIS_KUNDE = 22;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_AG = 23;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_MASCHINE = 24;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_MASCHINENKOSTEN = 25;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_AUFSPANNUNG = 26;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_AGART = 27;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_RUESTZEIT = 28;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_STUECKZEIT = 29;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_GESAMTZEIT = 30;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_GESPREIS_MITARBEITER = 31;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_MASCHINENKOSTENVK = 32;
	private static int REPORT_ANGEBOTSTKLMENGENSTAFFEL_ANZAHL_FELDER = 33;

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

	public JasperPrintLP printAngebotstklmenenstaffel(Integer iIdAngebotstkl,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		sAktuellerReport = AngebotstklFac.REPORT_ANGEBOTSTUECKLISTEMENGENSTAFFEL;

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

		ArrayList alDaten = new ArrayList();

		int row = 0;

		while (resultListIterator.hasNext()) {
			FLRAgstklposition flrAgstklposition = (FLRAgstklposition) resultListIterator
					.next();

			ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(
							flrAgstklposition.getFlrartikel().getI_id(),
							theClientDto);

			Object[] oZeile = new Object[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ANZAHL_FELDER];

			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_POSITIONSOBJEKT] = getSystemReportFac()
					.getPositionForReport(LocaleFac.BELEGART_AGSTUECKLISTE,
							flrAgstklposition.getI_id(), theClientDto);
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_NR] = new Integer(row + 1);
			if (artikelDto.getArtikelartCNr() != null
					&& !artikelDto.getArtikelartCNr().equals(
							ArtikelFac.ARTIKELART_HANDARTIKEL)) {
				oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARTIKELNUMMER] = artikelDto
						.getCNr();
			}
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARTIKELART] = artikelDto
					.getArtikelartCNr();
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARTIKELBEZEICHNUNG] = artikelDto
					.formatBezeichnung();
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_MENGE] = flrAgstklposition
					.getN_menge();
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_MENGENEINHEIT] = flrAgstklposition
					.getEinheit_c_nr();
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_EINZELPREIS] = flrAgstklposition
					.getN_nettoeinzelpreis();
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_POSITIONSART] = flrAgstklposition
					.getAgstklpositionsart_c_nr();

			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_NETTOPREIS] = flrAgstklposition
					.getN_nettogesamtpreis();

			AgstklpositionDto agstklposDto = getAngebotstklpositionFac()
					.agstklpositionFindByPrimaryKey(
							flrAgstklposition.getI_id(), theClientDto);

			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_AUFSCHLAG_PROZENT] = agstklposDto
					.getFAufschlag();
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_AUFSCHLAG_BETRAG] = agstklposDto
					.getNAufschlag();
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_NETTOGESAMTPREISMITAUFSCHLAG] = agstklposDto
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
					oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_LIEF1PREIS] = alDto
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

						oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_LETZTER_GELIEFERT_PREIS] = wepDto
								.getNGelieferterpreis();
						oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_LETZTES_WE_DATUM] = weDto
								.getTWareneingangsdatum();

					}
				}

				// VK-Preis
				if (kundeDto.getMwstsatzbezIId() != null) {
					VkpreisfindungDto vkpreisDto = getVkPreisfindungFac()
							.verkaufspreisfindung(
									flrAgstklposition.getFlrartikel().getI_id(),
									agstklDto.getKundeIId(),
									flrAgstklposition.getN_menge(),
									new java.sql.Date(flrAgstklposition
											.getFlragstkl().getT_belegdatum()
											.getTime()),
									kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
									getMandantFac()
											.mwstsatzFindByMwstsatzbezIIdAktuellster(
													kundeDto.getMwstsatzbezIId(),
													theClientDto).getIId(),
									agstklDto.getWaehrungCNr(), theClientDto);

					VerkaufspreisDto kundenVKPreisDto = Helper
							.getVkpreisBerechnet(vkpreisDto);

					if (kundenVKPreisDto != null
							&& kundenVKPreisDto.nettopreis != null) {

						oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_VKPREIS_KUNDE] = kundenVKPreisDto.nettopreis;

					}
				}

			}

			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_RABATTSATZ] = agstklposDto
					.getFRabattsatz();
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ZUSATZRABATTSATZ] = agstklposDto
					.getFZusatzrabattsatz();

			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_NETTOGESAMTPREIS] = flrAgstklposition
					.getN_menge().multiply(
							flrAgstklposition.getN_nettogesamtpreis());

			if (flrAgstklposition.getN_gestehungspreis().doubleValue() != 0) {

				oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_AUFSCHLAG] = Helper
						.getAufschlagProzent(
								flrAgstklposition.getN_nettogesamtpreis(),
								flrAgstklposition.getN_gestehungspreis(), 4);
			} else {
				oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_AUFSCHLAG] = new BigDecimal(
						0);
			}

			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_GESTPREIS] = flrAgstklposition
					.getN_gestehungspreis();
			if (flrAgstklposition.getN_menge() != null) {
				oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_GESTPREISGESAMT] = flrAgstklposition
						.getN_gestehungspreis().multiply(
								flrAgstklposition.getN_menge());
			}

			alDaten.add(oZeile);
			row++;
		}
		session.close();

		// Arbeitsplan

		ParametermandantDto parameterDto = null;
		try {
			parameterDto = (ParametermandantDto) getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_STUECKLISTE,
							ParameterFac.PARAMETER_STUECKLISTE_ARBEITSPLAN_ZEITEINHEIT);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		String sEinheit = parameterDto.getCWert().trim();

		session = FLRSessionFactory.getFactory().openSession();
		crit = session.createCriteria(FLRAgstklarbeitsplan.class);
		crit.add(Expression.eq(
				AngebotstklFac.FLR_AGSTKLARBEITSPLAN_AGSTKL_I_ID,
				iIdAngebotstkl));
		crit.addOrder(Order.asc(FLR_AGSTKLARBEITSPLAN_I_ARBEITSGANG));
		crit.addOrder(Order.asc(FLR_AGSTKLARBEITSPLAN_I_UNTERARBEITSGANG));
		resultList = crit.list();

		resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			FLRAgstklarbeitsplan flrAgstklposition = (FLRAgstklarbeitsplan) resultListIterator
					.next();

			ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(
							flrAgstklposition.getFlrartikel().getI_id(),
							theClientDto);

			Object[] oZeile = new Object[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ANZAHL_FELDER];

			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARTIKELNUMMER] = artikelDto
					.getCNr();

			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARTIKELART] = artikelDto
					.getArtikelartCNr();
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARTIKELBEZEICHNUNG] = artikelDto
					.formatBezeichnung();

			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_AG] = flrAgstklposition
					.getI_arbeitsgang();

			AgstklarbeitsplanDto apDto = agstklarbeitsplanFindByPrimaryKey(
					flrAgstklposition.getI_id(), theClientDto);

			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_AGART] = apDto
					.getAgartCNr();
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_AUFSPANNUNG] = flrAgstklposition
					.getI_aufspannung();
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_GESAMTZEIT] = flrAgstklposition
					.getI_arbeitsgang();

			if (flrAgstklposition.getFlrmaschine() != null) {

				String maschine = "";
				if (flrAgstklposition.getFlrmaschine().getC_identifikationsnr() != null) {
					maschine += flrAgstklposition.getFlrmaschine()
							.getC_identifikationsnr() + " ";
				}
				if (flrAgstklposition.getFlrmaschine().getC_bez() != null) {
					maschine += flrAgstklposition.getFlrmaschine().getC_bez()
							+ " ";
				}
				if (flrAgstklposition.getFlrmaschine().getC_inventarnummer() != null) {
					maschine += flrAgstklposition.getFlrmaschine()
							.getC_inventarnummer();
				}

				oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_MASCHINE] = maschine;
				oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_MASCHINENKOSTEN] = getZeiterfassungFac()
						.getMaschinenKostenZumZeitpunkt(
								flrAgstklposition.getMaschine_i_id(),
								new Timestamp(flrAgstklposition.getFlragstkl()
										.getT_belegdatum().getTime()));
				oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_MASCHINENKOSTENVK] = getZeiterfassungFac()
						.getMaschinenKostenVKZumZeitpunkt(
								flrAgstklposition.getMaschine_i_id(),
								new Timestamp(flrAgstklposition.getFlragstkl()
										.getT_belegdatum().getTime()));
			}
			double lStueckzeit = flrAgstklposition.getL_stueckzeit()
					.longValue();
			double lRuestzeit = flrAgstklposition.getL_ruestzeit().longValue();

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

			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_RUESTZEIT] = Helper
					.rundeKaufmaennisch(new BigDecimal(dRuestzeit), 5);
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_STUECKZEIT] = Helper
					.rundeKaufmaennisch(new BigDecimal(dStueckzeit), 5);
			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_GESPREIS_MITARBEITER] = new BigDecimal(
					0);

			BigDecimal dGesamt = new BigDecimal(dStueckzeit)
					.add(new BigDecimal(dRuestzeit));

			oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_GESAMTZEIT] = Helper
					.rundeKaufmaennisch(dGesamt, 5);

			if (Helper.short2boolean(flrAgstklposition.getB_nurmaschinenzeit())==false) {

				try {
					ArtikellieferantDto artikellieferantDto = getArtikelFac()
							.getArtikelEinkaufspreis(
									flrAgstklposition.getFlrartikel().getI_id(),
									(BigDecimal) oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_GESAMTZEIT],
									agstklDto.getWaehrungCNr(), theClientDto);

					if (artikellieferantDto != null) {
						oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_GESPREIS_MITARBEITER] = artikellieferantDto
								.getLief1Preis();
					}
				} catch (RemoteException ex1) {
					throwEJBExceptionLPRespectOld(ex1);
				}

				// VK-Preis
				if (kundeDto.getMwstsatzbezIId() != null) {
					VkpreisfindungDto vkpreisDto = getVkPreisfindungFac()
							.verkaufspreisfindung(
									flrAgstklposition.getFlrartikel().getI_id(),
									agstklDto.getKundeIId(),
									dGesamt,
									new Date(System.currentTimeMillis()),
									kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
									getMandantFac()
											.mwstsatzFindByMwstsatzbezIIdAktuellster(
													kundeDto.getMwstsatzbezIId(),
													theClientDto).getIId(),
									agstklDto.getWaehrungCNr(), theClientDto);

					VerkaufspreisDto kundenVKPreisDto = Helper
							.getVkpreisBerechnet(vkpreisDto);

					if (kundenVKPreisDto != null
							&& kundenVKPreisDto.nettopreis != null) {

						oZeile[REPORT_ANGEBOTSTKLMENGENSTAFFEL_VKPREIS_KUNDE] = kundenVKPreisDto.nettopreis;

					}
				}

			}

			alDaten.add(oZeile);
		}
		session.close();

		data = new Object[alDaten.size()][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ANZAHL_FELDER];
		data = (Object[][]) alDaten.toArray(data);

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		parameter.put("P_ANGEBOTSTKL", agstklDto.getCNr());

		parameter.put("P_KUNDE", kundeDto.getPartnerDto().formatTitelAnrede());

		parameter.put("P_PROJEKT", agstklDto.getCBez());

		parameter.put("P_WAEHRUNG", agstklDto.getWaehrungCNr());

		parameter.put("P_ARBEITSPLAN_ZEITEINHEIT", sEinheit);

		parameter.put("P_EK_PREISBASIS", agstklDto.getIEkpreisbasis());

		ParametermandantDto parameterMand = getParameterFac()
				.getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE,
						ParameterFac.PARAMETER_KALKULATIONSART);
		parameter.put("P_KALKULATIONSART",
				(Integer) parameterMand.getCWertAsObject());

		parameter.put("P_SUBREPORT_MENGENSTAFFEL",
				getSubreportAgstklMengenstaffel(iIdAngebotstkl, theClientDto));

		initJRDS(parameter, AngebotstklFac.REPORT_MODUL,
				AngebotstklFac.REPORT_ANGEBOTSTUECKLISTEMENGENSTAFFEL,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();

	}

	public LPDatenSubreport getSubreportAgstklMengenstaffel(
			Integer iIdAngebotstkl, TheClientDto theClientDto) {
		ArrayList alDatenSubreport = new ArrayList();
		String[] fieldnamesMengenstaffel = new String[] { "Menge",
				"MaterialeinsatzLief1", "AZEinsatzLief1", "VKPreisAusAgstkl",
				"VKPreisGewaehlt", "DBPreis_VKPreis", "DBPreisProzent_VKPreis",
				"DBPreis_VKPreisGewaehlt", "DBPreisProzent_VKPreisGewaehlt",
				"Person_Aendern", "Datum_Aendern",
				"VKPreisAusKundenpreisfindung", };

		Query query = em.createNamedQuery("AgstklmengenstaffelFindByAgstklIId");
		query.setParameter(1, iIdAngebotstkl);

		Collection c = query.getResultList();
		Iterator it = c.iterator();

		while (it.hasNext()) {

			Agstklmengenstaffel agstklmengenstaffel = (Agstklmengenstaffel) it
					.next();

			BigDecimal bdWareneinsatz = agstklmengenstaffel
					.getNMaterialeinsatzLief1();

			if (bdWareneinsatz == null) {
				bdWareneinsatz = getAngebotstklFac().getWareneinsatzLief1(
						agstklmengenstaffel.getNMenge(),
						agstklmengenstaffel.getAgstklIId(), theClientDto);
			}

			BigDecimal bdAZEinsatz = agstklmengenstaffel.getNAzeinsatzLief1();

			if (bdAZEinsatz == null) {
				bdAZEinsatz = getAngebotstklFac().getAZeinsatzLief1(
						agstklmengenstaffel.getNMenge(),
						agstklmengenstaffel.getAgstklIId(), theClientDto);
			}

			BigDecimal bdVkpreis = agstklmengenstaffel.getNVkpreis();

			BigDecimal[] bdVkpreise = getAngebotstklFac().getVKPreis(
					agstklmengenstaffel.getNMenge(),
					agstklmengenstaffel.getAgstklIId(), theClientDto);

			if (bdVkpreis == null) {
				bdVkpreis = bdVkpreise[AngebotstklFac.VKPREIS_LT_AGTSKLPOSITIONSPREIS];
			}

			Object[] zeile = new Object[fieldnamesMengenstaffel.length];
			zeile[0] = agstklmengenstaffel.getNMenge();
			zeile[1] = bdWareneinsatz;

			zeile[2] = bdAZEinsatz;
			zeile[3] = bdVkpreis;
			zeile[11] = bdVkpreise[AngebotstklFac.VKPREIS_LT_KUNDENPREISFINDUNG];

			BigDecimal dbPreis = bdVkpreis.subtract(bdWareneinsatz).subtract(
					bdAZEinsatz);

			zeile[5] = dbPreis;

			BigDecimal dbPreisProzent = BigDecimal.ZERO;
			if (bdVkpreis.doubleValue() != 0) {
				dbPreisProzent = dbPreis.divide(bdVkpreis, 4,
						BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
			}

			zeile[6] = dbPreisProzent;

			if (agstklmengenstaffel.getNVkpreisGewaehlt() != null) {
				zeile[4] = agstklmengenstaffel.getNVkpreisGewaehlt();

				BigDecimal dbPreisVKGewaehlt = agstklmengenstaffel
						.getNVkpreisGewaehlt().subtract(bdWareneinsatz)
						.subtract(bdAZEinsatz);

				zeile[7] = dbPreisVKGewaehlt;

				BigDecimal dbPreisProzentVKGewaehlt = BigDecimal.ZERO;
				if (bdVkpreis.doubleValue() != 0) {
					dbPreisProzentVKGewaehlt = dbPreisVKGewaehlt.divide(
							agstklmengenstaffel.getNVkpreisGewaehlt(), 4,
							BigDecimal.ROUND_HALF_UP).multiply(
							new BigDecimal(100));
				}

				zeile[8] = dbPreisProzentVKGewaehlt;
			}

			zeile[9] = getPersonalFac().personalFindByPrimaryKeySmall(
					agstklmengenstaffel.getPersonalIIdAendern())
					.getCKurzzeichen();
			zeile[10] = agstklmengenstaffel.getTAendern();

			alDatenSubreport.add(zeile);

		}

		Object[][] dataSubAufschlag = new Object[alDatenSubreport.size()][fieldnamesMengenstaffel.length];
		dataSubAufschlag = (Object[][]) alDatenSubreport
				.toArray(dataSubAufschlag);
		return new LPDatenSubreport(dataSubAufschlag, fieldnamesMengenstaffel);
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
		HashMap<String, Object> parameter = new HashMap<String, Object>();

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

			if (agstklDto.getIEkpreisbasis() == null) {
				try {
					ParametermandantDto parameterMand = getParameterFac()
							.getMandantparameter(theClientDto.getMandant(),
									ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE,
									ParameterFac.PARAMETER_EK_PREISBASIS);
					int iEkPreisbasis = (Integer) parameterMand
							.getCWertAsObject();
					agstklDto.setIEkpreisbasis(iEkPreisbasis);
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

			Agstkl agstkl = new Agstkl(agstklDto.getIId(),
					agstklDto.getMandantCNr(), agstklDto.getCNr(),
					agstklDto.getBelegartCNr(), agstklDto.getKundeIId(),
					agstklDto.getTBelegdatum(), agstklDto.getWaehrungCNr(),
					agstklDto.getFWechselkursmandantwaehrungzuagstklwaehrung(),
					agstklDto.getPersonalIIdAnlegen(),
					agstklDto.getPersonalIIdAendern(),
					agstklDto.getIEkpreisbasis());
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
		agstkl.setProjektIId(agstklDto.getProjektIId());
		agstkl.setIEkpreisbasis(agstklDto.getIEkpreisbasis());
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

	private void setAgstklmengenstaffelFromAgstklmengenstaffelDto(
			Agstklmengenstaffel agstklmengenstaffel,
			AgstklmengenstaffelDto agstklmengenstaffelDto) {
		agstklmengenstaffel.setAgstklIId(agstklmengenstaffelDto.getAgstklIId());
		agstklmengenstaffel.setNAzeinsatzLief1(agstklmengenstaffelDto
				.getNAzeinsatzLief1());
		agstklmengenstaffel.setNMaterialeinsatzLief1(agstklmengenstaffelDto
				.getNMaterialeinsatzLief1());
		agstklmengenstaffel.setNMenge(agstklmengenstaffelDto.getNMenge());
		agstklmengenstaffel.setNVkpreis(agstklmengenstaffelDto.getNVkpreis());
		agstklmengenstaffel.setNVkpreisGewaehlt(agstklmengenstaffelDto
				.getNVkpreisGewaehlt());
		agstklmengenstaffel.setPersonalIIdAendern(agstklmengenstaffelDto
				.getPersonalIIdAendern());
		agstklmengenstaffel.setTAendern(agstklmengenstaffelDto.getTAendern());

		em.merge(agstklmengenstaffel);
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
				.equals(AngebotstklFac.REPORT_ANGEBOTSTUECKLISTEMENGENSTAFFEL)) {

			if ("Nummer".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_NR];
			} else if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARTIKELNUMMER];
			} else if ("Artikelart".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARTIKELART];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARTIKELBEZEICHNUNG];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_MENGE];
			} else if ("Mengeneinheit".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_MENGENEINHEIT];
			} else if ("Einzelpreis".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_EINZELPREIS];
			} else if ("Nettogesamtpreis".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_NETTOGESAMTPREIS];
			} else if ("Nettopreis".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_NETTOPREIS];
			} else if ("Rabattsatz".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_RABATTSATZ];
			} else if ("Zusatzrabattsatz".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ZUSATZRABATTSATZ];
			} else if ("Aufschlag".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_AUFSCHLAG];
			} else if ("Gestpreis".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_GESTPREIS];
			} else if ("Gestpreisgesamt".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_GESTPREISGESAMT];
			} else if ("F_POSITIONSART".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_POSITIONSART];
			} else if ("F_POSITIONSOBJEKT".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_POSITIONSOBJEKT];
			} else if ("F_AUFSCHLAG_PROZENT".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_AUFSCHLAG_PROZENT];
			} else if ("F_AUFSCHLAG_BETRAG".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_AUFSCHLAG_BETRAG];
			} else if ("F_NETTOGESAMTPREISMITAUFSCHLAG".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_NETTOGESAMTPREISMITAUFSCHLAG];
			} else if ("F_LIEF1PREIS".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_LIEF1PREIS];
			} else if ("F_LETZTER_GELIEFERT_PREIS".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_LETZTER_GELIEFERT_PREIS];
			} else if ("F_LETZTES_WE_DATUM".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_LETZTES_WE_DATUM];
			} else if ("F_VKPREIS_KUNDE".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_VKPREIS_KUNDE];
			}

			else if ("F_ARBEITSPLAN_AG".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_AG];
			} else if ("F_ARBEITSPLAN_MASCHINE".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_MASCHINE];
			} else if ("F_ARBEITSPLAN_MASCHINENKOSTEN".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_MASCHINENKOSTEN];
			} else if ("F_ARBEITSPLAN_MASCHINENKOSTEN_VK".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_MASCHINENKOSTENVK];
			} else if ("F_ARBEITSPLAN_AUFSPANNUNG".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_AUFSPANNUNG];
			} else if ("F_ARBEITSPLAN_AGART".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_AGART];
			} else if ("F_ARBEITSPLAN_RUESTZEIT".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_RUESTZEIT];
			} else if ("F_ARBEITSPLAN_STUECKZEIT".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_STUECKZEIT];
			} else if ("F_ARBEITSPLAN_GESAMTZEIT".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_GESAMTZEIT];
			} else if ("F_ARBEITSPLAN_GESPREIS_MITARBEITER".equals(fieldName)) {
				value = data[index][REPORT_ANGEBOTSTKLMENGENSTAFFEL_ARBEITSPLAN_GESPREIS_MITARBEITER];
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
			BigDecimal nMengenstaffel, String cNrWaehrungI,
			TheClientDto theClientDto) throws EJBExceptionLP {

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

			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
					MandantFac.ZUSATZFUNKTION_AGSTKL_ARBEITSPLAN, theClientDto)
					&& nMengenstaffel != null) {

				Query query = em
						.createNamedQuery("AgstklmengenstaffelFindByAgstklIIdKleinerGleichNMenge");
				query.setParameter(1, iIdAgstklI);
				query.setParameter(2, nMengenstaffel);

				query.setMaxResults(1);
				Collection c = query.getResultList();
				Iterator it = c.iterator();

				BigDecimal bdWareneinsatz = null;
				BigDecimal bdAZEinsatz = null;

				if (it.hasNext()) {
					Agstklmengenstaffel agstklmengenstaffel = (Agstklmengenstaffel) it
							.next();

					bdWareneinsatz = agstklmengenstaffel
							.getNMaterialeinsatzLief1();
					bdAZEinsatz = agstklmengenstaffel.getNAzeinsatzLief1();
				}

				if (bdWareneinsatz == null) {
					bdWareneinsatz = getAngebotstklFac().getWareneinsatzLief1(
							nMengenstaffel, iIdAgstklI, theClientDto);
				}
				if (bdAZEinsatz == null) {
					bdAZEinsatz = getAngebotstklFac().getAZeinsatzLief1(
							nMengenstaffel, iIdAgstklI, theClientDto);
				}

				nWert = bdAZEinsatz.add(bdWareneinsatz);

			} else {

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
												agstklpositionDto
														.getArtikelIId(),
												theClientDto);
								if (aDto.getArtikelartCNr().equals(
										ArtikelFac.ARTIKELART_ARBEITSZEIT) == true) {
									bMaterial = false;
								}
							}

							AufschlagDto[] aufschlagDtos = aufschlagFindByBMaterial(
									agstklpositionDto.getAgstklIId(),
									Helper.boolean2Short(bMaterial),
									theClientDto);

							Double dAufschlaege = 0D;

							for (int k = 0; k < aufschlagDtos.length; k++) {

								dAufschlaege = dAufschlaege
										+ aufschlagDtos[k]
												.getAgstklaufschlagDto()
												.getFAufschlag();

							}
							nWertDerPosition = nWertDerPosition.add(Helper
									.getProzentWert(nWertDerPosition,
											new BigDecimal(dAufschlaege), 2));

							nWert = nWert.add(nWertDerPosition);
						}

					}
				}
			}

			// der kalkulatorische Wert ist nun in der Waehrung der
			// Agstkl bekannt
			AgstklDto agstklDto = agstklFindByPrimaryKey(iIdAgstklI);

			if (!cNrWaehrungI.equals(agstklDto.getWaehrungCNr())) {
				nWert = getLocaleFac().rechneUmInAndereWaehrungZuDatum(nWert,
						agstklDto.getWaehrungCNr(), cNrWaehrungI,
						new Date(System.currentTimeMillis()), theClientDto);
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
					if (a.getArtikelsprDto() != null) {
						agstklpositionDtoI.setCBez(a.getArtikelsprDto()
								.getCBez());
						agstklpositionDtoI.setCZbez(a.getArtikelsprDto()
								.getCZbez());
					}
				} else {
					agstklpositionDtoI
							.setAgstklpositionsartCNr(AngebotstklServiceFac.AGSTKLPOSITIONART_IDENT);

				}

				angebotstklPositionLocalFac.befuelleMitPreisenNachKalkulationsart(agstklpositionDtoI, 
						agstklDto.getWaehrungCNr(), theClientDto);
				
				getAngebotstklpositionFac().createAgstklposition(
						agstklpositionDtoI, theClientDto);

			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

	}

	public void kopiereArbeitsplanAusStuecklisteInPositionen(
			Integer stuecklisteIId, Integer agstklIId, TheClientDto theClientDto) {

		try {
			AgstklDto agstklDto = agstklFindByPrimaryKey(agstklIId);

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

				angebotstklPositionLocalFac.befuellePositionMitPreisenKalkulationsart2(theClientDto,
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

	public void kopiereArbeitsplanAusStuecklisteInArbeitsplan(
			Integer stuecklisteIId, Integer agstklIId, TheClientDto theClientDto) {

		StuecklistearbeitsplanDto[] arbeitsplanPos = getStuecklisteFac()
				.stuecklistearbeitsplanFindByStuecklisteIId(stuecklisteIId,
						theClientDto);
		for (int i = 0; i < arbeitsplanPos.length; i++) {
			StuecklistearbeitsplanDto arbeitsplanPosDto = arbeitsplanPos[i];

			AgstklarbeitsplanDto agstklarbeitsplanDto = new AgstklarbeitsplanDto();
			agstklarbeitsplanDto.setAgstklIId(agstklIId);
			agstklarbeitsplanDto.setAgartCNr(arbeitsplanPosDto.getAgartCNr());
			agstklarbeitsplanDto.setArtikelIId(arbeitsplanPosDto
					.getArtikelIId());
			agstklarbeitsplanDto.setBNurmaschinenzeit(arbeitsplanPosDto
					.getBNurmaschinenzeit());
			agstklarbeitsplanDto.setCKommentar(arbeitsplanPosDto
					.getCKommentar());
			agstklarbeitsplanDto.setIArbeitsgang(arbeitsplanPosDto
					.getIArbeitsgang());
			agstklarbeitsplanDto.setIAufspannung(arbeitsplanPosDto
					.getIAufspannung());
			agstklarbeitsplanDto.setIUnterarbeitsgang(arbeitsplanPosDto
					.getIUnterarbeitsgang());
			agstklarbeitsplanDto.setLRuestzeit(arbeitsplanPosDto
					.getLRuestzeit());
			agstklarbeitsplanDto.setLStueckzeit(arbeitsplanPosDto
					.getLStueckzeit());
			agstklarbeitsplanDto.setMaschineIId(arbeitsplanPosDto
					.getMaschineIId());
			agstklarbeitsplanDto.setXLangtext(arbeitsplanPosDto.getXLangtext());

			getAngebotstklFac().createAgstklarbeitsplan(agstklarbeitsplanDto,
					theClientDto);

		}

	}

	public AgstklarbeitsplanDto[] agstklarbeitsplanFindByAgstklIId(
			Integer iIdAgstklI, TheClientDto theClientDto) {
		AgstklarbeitsplanDto[] agstklarbeitsplanDto = null;
		Query query = em
				.createNamedQuery("AgstklarbeitsplanFindByAgstklIIdOrderByArbeitsgang");
		query.setParameter(1, iIdAgstklI);
		Collection<?> cl = query.getResultList();
		agstklarbeitsplanDto = AgstklarbeitsplanDtoAssembler.createDtos(cl);
		return agstklarbeitsplanDto;
	}

	public void kopiereAgstklArbeitsplan(Integer agstklIId_Quelle,
			Integer agstklIId_Ziel, TheClientDto theClientDto) {
		if (agstklIId_Quelle == null || agstklIId_Ziel == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"agstklIId_Quelle == null || agstklIId_Ziel == null"));
		}

		Query query = em.createNamedQuery("AgstklarbeitsplanFindByAgstklIId");
		query.setParameter(1, agstklIId_Quelle);
		Collection<?> cl = query.getResultList();

		AgstklarbeitsplanDto[] dtos = AgstklarbeitsplanDtoAssembler
				.createDtos(cl);

		for (int i = 0; i < dtos.length; i++) {
			AgstklarbeitsplanDto dto = dtos[i];
			dto.setAgstklIId(agstklIId_Ziel);
			createAgstklarbeitsplan(dto, theClientDto);
		}

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

	public Integer createAgstklmengenstaffel(
			AgstklmengenstaffelDto agstklmengenstaffelDto,
			TheClientDto theClientDto) {

		try {
			Query query = em
					.createNamedQuery("AgstklmengenstaffelFindByAgstklIIdNMenge");
			query.setParameter(1, agstklmengenstaffelDto.getAgstklIId());
			query.setParameter(2, agstklmengenstaffelDto.getNMenge());

			Agstklmengenstaffel doppelt = (Agstklmengenstaffel) query
					.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("AS_AGSTKLMENGENSTAFFEL.UK"));
		} catch (NoResultException ex) {

		}

		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_AGSTKLMENGENSTAFFEL);
		agstklmengenstaffelDto.setIId(pk);
		agstklmengenstaffelDto.setPersonalIIdAendern(theClientDto
				.getIDPersonal());
		agstklmengenstaffelDto.setTAendern(new Timestamp(System
				.currentTimeMillis()));

		Agstklmengenstaffel agstklmengenstaffel = new Agstklmengenstaffel(
				agstklmengenstaffelDto.getIId(),
				agstklmengenstaffelDto.getAgstklIId(),
				agstklmengenstaffelDto.getNMenge(),
				agstklmengenstaffelDto.getPersonalIIdAendern(),
				agstklmengenstaffelDto.getTAendern());
		em.persist(agstklmengenstaffel);
		em.flush();
		setAgstklmengenstaffelFromAgstklmengenstaffelDto(agstklmengenstaffel,
				agstklmengenstaffelDto);
		return agstklmengenstaffelDto.getIId();
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

	public void removeAgstklmengenstaffel(Integer agstklmengenstaffelIId) {
		Agstklmengenstaffel toRemove = em.find(Agstklmengenstaffel.class,
				agstklmengenstaffelIId);

		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public BigDecimal getWareneinsatzLief1(BigDecimal bdMenge,
			Integer agstklIId, TheClientDto theClientDto) {
		BigDecimal bdWareneinsatzLief1 = BigDecimal.ZERO;

		try {

			AgstklDto agstklDto = agstklFindByPrimaryKey(agstklIId);

			AgstklpositionDto[] dtos = getAngebotstklpositionFac()
					.agstklpositionFindByAgstklIId(agstklIId, theClientDto);

			for (int i = 0; i < dtos.length; i++) {
				AgstklpositionDto dto = dtos[i];

				if (dto.getArtikelIId() != null && dto.getNMenge() != null) {
					ArtikelDto aDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(dto.getArtikelIId(),
									theClientDto);

					if (aDto.getArtikelartCNr().equals(
							ArtikelFac.ARTIKELART_ARTIKEL)) {
						// MATERIAL

						// todo PJ18725
						// Wenn Parameterv EK_PREISBASIS=0 (LIEF1PREIS), dann so
						// wie nachstehend

						if (agstklDto.getIEkpreisbasis().intValue() == EK_PREISBASIS_LIEF1PREIS) {

							ArtikellieferantDto alDto = getArtikelFac()
									.getArtikelEinkaufspreis(
											dto.getArtikelIId(),
											bdMenge.multiply(dto.getNMenge()),
											agstklDto.getWaehrungCNr(),
											theClientDto);
							if (alDto != null && alDto.getNNettopreis() != null) {
								bdWareneinsatzLief1 = bdWareneinsatzLief1
										.add(alDto.getNNettopreis().multiply(
												dto.getNMenge()));
							}
						} else {

							// Wenn Parameterv EK_PREISBASIS=1 (NETTOPREIS),
							// dann so
							// wie nachstehend (Nettopreis aus Position)

							bdWareneinsatzLief1 = bdWareneinsatzLief1.add(dto
									.getNNettogesamtpreis().multiply(
											dto.getNMenge()));

						}
					} else if (aDto.getArtikelartCNr().equals(
							ArtikelFac.ARTIKELART_HANDARTIKEL)) {

						if (dto.getNNettogesamtmitaufschlag() != null) {
							bdWareneinsatzLief1 = bdWareneinsatzLief1.add(dto
									.getNNettogesamtmitaufschlag().multiply(
											dto.getNMenge()));
						} else {
							bdWareneinsatzLief1 = bdWareneinsatzLief1.add(dto
									.getNNettoeinzelpreis().multiply(
											dto.getNMenge()));
						}

					} else {
						// AZ
					}
				}
			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return bdWareneinsatzLief1;
	}

	public BigDecimal getAZeinsatzLief1(BigDecimal bdMenge, Integer agstklIId,
			TheClientDto theClientDto) {
		BigDecimal bdWareneinsatzLief1 = BigDecimal.ZERO;

		try {

			AgstklDto agstklDto = agstklFindByPrimaryKey(agstklIId);

			AgstklpositionDto[] dtos = getAngebotstklpositionFac()
					.agstklpositionFindByAgstklIId(agstklIId, theClientDto);

			for (int i = 0; i < dtos.length; i++) {
				AgstklpositionDto dto = dtos[i];

				if (dto.getArtikelIId() != null && dto.getNMenge() != null) {
					ArtikelDto aDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(dto.getArtikelIId(),
									theClientDto);

					if (aDto.getArtikelartCNr().equals(
							ArtikelFac.ARTIKELART_HANDARTIKEL)
							|| aDto.getArtikelartCNr().equals(
									ArtikelFac.ARTIKELART_ARTIKEL)) {
						// MATERIAL

					} else {
						// AZ
						ArtikellieferantDto alDto = getArtikelFac()
								.getArtikelEinkaufspreis(dto.getArtikelIId(),
										bdMenge.multiply(dto.getNMenge()),
										agstklDto.getWaehrungCNr(),
										theClientDto);
						if (alDto != null && alDto.getNNettopreis() != null) {
							bdWareneinsatzLief1 = bdWareneinsatzLief1
									.add(alDto.getNNettopreis().multiply(
											dto.getNMenge()));
						}
					}
				}
			}

			Query query = em
					.createNamedQuery("AgstklarbeitsplanFindByAgstklIId");
			query.setParameter(1, agstklIId);

			Collection c = query.getResultList();

			Iterator it = c.iterator();

			while (it.hasNext()) {
				Agstklarbeitsplan ap = (Agstklarbeitsplan) it.next();

				BigDecimal bdGesamtzeit = Helper.berechneGesamtzeitInStunden(
						ap.getLRuestzeit(), ap.getLStueckzeit(), bdMenge, 1,
						ap.getIAufspannung());
				if (bdMenge.doubleValue() != 0) {

					bdGesamtzeit = bdGesamtzeit.divide(bdMenge,
							BigDecimal.ROUND_HALF_UP, 4);
				}

				if (bdGesamtzeit != null) {

					if (Helper.short2boolean(ap.getBNurmaschinenzeit()) == false) {

						ArtikellieferantDto alDto = getArtikelFac()
								.getArtikelEinkaufspreis(
										ap.getArtikelIId(),
										null,
										bdMenge.multiply(bdGesamtzeit),
										agstklDto.getWaehrungCNr(),
										new java.sql.Date(agstklDto
												.getTBelegdatum().getTime()),
										theClientDto);
						if (alDto != null && alDto.getNNettopreis() != null) {
							bdWareneinsatzLief1 = bdWareneinsatzLief1.add(alDto
									.getNNettopreis().multiply(bdGesamtzeit));
						}
					}

					if (ap.getMaschineIId() != null) {

						BigDecimal bdMaschinenkosten = getZeiterfassungFac()
								.getMaschinenKostenZumZeitpunkt(
										ap.getMaschineIId(),
										agstklDto.getTBelegdatum());

						if (bdMaschinenkosten != null) {

							bdWareneinsatzLief1 = bdWareneinsatzLief1
									.add(bdMaschinenkosten
											.multiply(bdGesamtzeit));

						}

					}

				}

			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return bdWareneinsatzLief1;
	}

	public BigDecimal getVKPreisGewaehlt(BigDecimal bdMenge, Integer agstklIId,
			TheClientDto theClientDto) {

		BigDecimal bdVkPreisGewaehlt = null;

		Query query = em
				.createNamedQuery("AgstklmengenstaffelFindByAgstklIIdKleinerGleichNMenge");
		query.setParameter(1, agstklIId);
		query.setParameter(2, bdMenge);

		query.setMaxResults(1);
		Collection c = query.getResultList();
		Iterator it = c.iterator();

		if (it.hasNext()) {
			Agstklmengenstaffel agstklmengenstaffel = (Agstklmengenstaffel) it
					.next();
			return agstklmengenstaffel.getNVkpreisGewaehlt();
		}
		return bdVkPreisGewaehlt;
	}

	public BigDecimal[] getVKPreis(BigDecimal bdMenge, Integer agstklIId,
			TheClientDto theClientDto) {
		BigDecimal bdVkPreisAnhandPositionspreisPlusAufschlag = BigDecimal.ZERO;
		BigDecimal bdVkPreisAnhandKundenpreisfindung = BigDecimal.ZERO;

		try {

			AgstklDto agstklDto = agstklFindByPrimaryKey(agstklIId);

			AgstklpositionDto[] dtos = getAngebotstklpositionFac()
					.agstklpositionFindByAgstklIId(agstklIId, theClientDto);

			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					agstklDto.getKundeIId(), theClientDto);

			for (int i = 0; i < dtos.length; i++) {
				AgstklpositionDto dto = dtos[i];

				if (dto.getArtikelIId() != null && dto.getNMenge() != null) {

					BigDecimal kundenVKPreisProEinheit = BigDecimal.ZERO;
					if (kundeDto.getMwstsatzbezIId() != null) {
						VkpreisfindungDto vkpreisDto = getVkPreisfindungFac()
								.verkaufspreisfindung(
										dto.getArtikelIId(),
										agstklDto.getKundeIId(),
										bdMenge.multiply(dto.getNMenge()),
										new Date(agstklDto.getTBelegdatum()
												.getTime()),
										kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
										getMandantFac()
												.mwstsatzFindByMwstsatzbezIIdAktuellster(
														kundeDto.getMwstsatzbezIId(),
														theClientDto).getIId(),
										agstklDto.getWaehrungCNr(),
										theClientDto);

						VerkaufspreisDto kundenVKPreisDto = Helper
								.getVkpreisBerechnet(vkpreisDto);

						if (kundenVKPreisDto != null
								&& kundenVKPreisDto.nettopreis != null) {
							kundenVKPreisProEinheit = kundenVKPreisDto.nettopreis;
						}
					}

					bdVkPreisAnhandKundenpreisfindung = bdVkPreisAnhandKundenpreisfindung
							.add(kundenVKPreisProEinheit.multiply(dto
									.getNMenge()));

					bdVkPreisAnhandPositionspreisPlusAufschlag = bdVkPreisAnhandPositionspreisPlusAufschlag
							.add(dto.getNNettogesamtmitaufschlag().multiply(
									dto.getNMenge()));

				}
			}

			Query query = em
					.createNamedQuery("AgstklarbeitsplanFindByAgstklIId");
			query.setParameter(1, agstklIId);

			Collection c = query.getResultList();

			Iterator it = c.iterator();

			while (it.hasNext()) {
				Agstklarbeitsplan ap = (Agstklarbeitsplan) it.next();

				BigDecimal bdGesamtzeit = Helper.berechneGesamtzeitInStunden(
						ap.getLRuestzeit(), ap.getLStueckzeit(), bdMenge, 1,
						ap.getIAufspannung());

				if (bdGesamtzeit != null) {

					if (Helper.short2boolean(ap.getBNurmaschinenzeit()) == false) {

						if (kundeDto.getMwstsatzbezIId() != null) {
							VkpreisfindungDto vkpreisDto = getVkPreisfindungFac()
									.verkaufspreisfindung(
											ap.getArtikelIId(),
											agstklDto.getKundeIId(),
											bdGesamtzeit,
											new Date(agstklDto.getTBelegdatum()
													.getTime()),
											kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
											getMandantFac()
													.mwstsatzFindByMwstsatzbezIIdAktuellster(
															kundeDto.getMwstsatzbezIId(),
															theClientDto)
													.getIId(),
											agstklDto.getWaehrungCNr(),
											theClientDto);

							VerkaufspreisDto kundenVKPreisDto = Helper
									.getVkpreisBerechnet(vkpreisDto);

							if (bdMenge.doubleValue() != 0) {

								bdGesamtzeit = bdGesamtzeit.divide(bdMenge,
										BigDecimal.ROUND_HALF_UP, 4);
							}

							if (kundenVKPreisDto != null
									&& kundenVKPreisDto.nettopreis != null) {

								bdVkPreisAnhandPositionspreisPlusAufschlag = bdVkPreisAnhandPositionspreisPlusAufschlag
										.add(kundenVKPreisDto.nettopreis
												.multiply(bdGesamtzeit));

								bdVkPreisAnhandKundenpreisfindung = bdVkPreisAnhandKundenpreisfindung
										.add(kundenVKPreisDto.nettopreis
												.multiply(bdGesamtzeit));

							}
						}
					}

					if (ap.getMaschineIId() != null) {
						BigDecimal kostenVK = getZeiterfassungFac()
								.getMaschinenKostenVKZumZeitpunkt(
										ap.getMaschineIId(),
										agstklDto.getTBelegdatum());

						if (kostenVK != null) {
							bdVkPreisAnhandPositionspreisPlusAufschlag = bdVkPreisAnhandPositionspreisPlusAufschlag
									.add(kostenVK.multiply(bdGesamtzeit));
							bdVkPreisAnhandKundenpreisfindung = bdVkPreisAnhandKundenpreisfindung
									.add(kostenVK.multiply(bdGesamtzeit));

						}

					}

				}

			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return new BigDecimal[] { bdVkPreisAnhandPositionspreisPlusAufschlag,
				bdVkPreisAnhandKundenpreisfindung };

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

	public void updateAgstklmengenstaffel(
			AgstklmengenstaffelDto agstklmengenstaffelDto,
			TheClientDto theClientDto) {

		Agstklmengenstaffel agstklmengenstaffel = em.find(
				Agstklmengenstaffel.class, agstklmengenstaffelDto.getIId());

		try {
			Query query = em
					.createNamedQuery("AgstklmengenstaffelFindByAgstklIIdNMenge");
			query.setParameter(1, agstklmengenstaffelDto.getAgstklIId());
			query.setParameter(2, agstklmengenstaffelDto.getNMenge());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Agstklmengenstaffel) query
					.getSingleResult()).getIId();
			if (agstklmengenstaffelDto.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"AS_AGSTKLMENGENSTAFFEL.UK"));
			}

		} catch (NoResultException ex) {

		}
		agstklmengenstaffelDto.setPersonalIIdAendern(theClientDto
				.getIDPersonal());
		agstklmengenstaffelDto.setTAendern(new Timestamp(System
				.currentTimeMillis()));
		setAgstklmengenstaffelFromAgstklmengenstaffelDto(agstklmengenstaffel,
				agstklmengenstaffelDto);
	}

	public AufschlagDto aufschlagFindByPrimaryKey(Integer iId) {
		Aufschlag aufschlag = em.find(Aufschlag.class, iId);
		return AufschlagDtoAssembler.createDto(aufschlag);
	}

	public AgstklmengenstaffelDto agstklmengenstaffelFindByPrimaryKey(
			Integer iId) {
		Agstklmengenstaffel agstklmengenstaffel = em.find(
				Agstklmengenstaffel.class, iId);
		return AgstklmengenstaffelDtoAssembler.createDto(agstklmengenstaffel);
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

	public Integer createAgstklarbeitsplan(
			AgstklarbeitsplanDto agstklarbeitsplanDto, TheClientDto theClientDto) {

		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_AGSTKLARBEITSPLAN);
		agstklarbeitsplanDto.setIId(pk);

		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
				agstklarbeitsplanDto.getArtikelIId(), theClientDto);
		// PJ 16851
		if (Helper.short2boolean(artikelDto.getbReinemannzeit()) == false) {
			// PJ 16396
			if (agstklarbeitsplanDto.getAgartCNr() != null) {
				Query query = em
						.createNamedQuery("AgstklarbeitsplanfindByAgstklIIdIArbeitsgangnummer");
				query.setParameter(1, agstklarbeitsplanDto.getAgstklIId());
				query.setParameter(2, agstklarbeitsplanDto.getIArbeitsgang());
				Collection<?> cl = query.getResultList();
				Iterator it = cl.iterator();
				while (it.hasNext()) {
					Agstklarbeitsplan ap = (Agstklarbeitsplan) it.next();
					if (ap.getAgartCNr() == null) {
						agstklarbeitsplanDto
								.setMaschineIId(ap.getMaschineIId());
						break;
					}
				}
			}
		}

		try {
			Agstklarbeitsplan agstklarbeitsplan = new Agstklarbeitsplan(
					agstklarbeitsplanDto.getIId(),
					agstklarbeitsplanDto.getAgstklIId(),
					agstklarbeitsplanDto.getIArbeitsgang(),
					agstklarbeitsplanDto.getArtikelIId(),
					agstklarbeitsplanDto.getLStueckzeit(),
					agstklarbeitsplanDto.getLRuestzeit(),
					agstklarbeitsplanDto.getBNurmaschinenzeit());
			em.persist(agstklarbeitsplan);
			em.flush();
			setAgstklarbeitsplanFromAgstklarbeitsplanDto(agstklarbeitsplan,
					agstklarbeitsplanDto);

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

		return agstklarbeitsplanDto.getIId();
	}

	private void setAgstklarbeitsplanFromAgstklarbeitsplanDto(
			Agstklarbeitsplan agstklarbeitsplan,
			AgstklarbeitsplanDto agstklarbeitsplanDto) {
		agstklarbeitsplan.setAgstklIId(agstklarbeitsplanDto.getAgstklIId());
		agstklarbeitsplan.setIArbeitsgang(agstklarbeitsplanDto
				.getIArbeitsgang());
		agstklarbeitsplan.setArtikelIId(agstklarbeitsplanDto.getArtikelIId());
		agstklarbeitsplan.setLStueckzeit(agstklarbeitsplanDto.getLStueckzeit());
		agstklarbeitsplan.setLRuestzeit(agstklarbeitsplanDto.getLRuestzeit());
		agstklarbeitsplan.setCKommentar(agstklarbeitsplanDto.getCKommentar());
		agstklarbeitsplan.setXLangtext(agstklarbeitsplanDto.getXLangtext());
		agstklarbeitsplan.setMaschineIId(agstklarbeitsplanDto.getMaschineIId());
		agstklarbeitsplan.setIAufspannung(agstklarbeitsplanDto
				.getIAufspannung());
		agstklarbeitsplan.setAgartCNr(agstklarbeitsplanDto.getAgartCNr());
		agstklarbeitsplan.setIUnterarbeitsgang(agstklarbeitsplanDto
				.getIUnterarbeitsgang());
		agstklarbeitsplan.setBNurmaschinenzeit(agstklarbeitsplanDto
				.getBNurmaschinenzeit());

		em.merge(agstklarbeitsplan);
		em.flush();
	}

	public void updateAgstklarbeitsplan(
			AgstklarbeitsplanDto agstklarbeitsplanDto, TheClientDto theClientDto) {

		Integer iId = agstklarbeitsplanDto.getIId();
		// try {
		Agstklarbeitsplan agstklarbeitsplan = em.find(Agstklarbeitsplan.class,
				iId);
		if (agstklarbeitsplan == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}

		setAgstklarbeitsplanFromAgstklarbeitsplanDto(agstklarbeitsplan,
				agstklarbeitsplanDto);

		// PJ 16396
		if (agstklarbeitsplanDto.getMaschineIId() != null
				&& agstklarbeitsplanDto.getAgartCNr() == null
				&& agstklarbeitsplanDto.getIArbeitsgang() != 0) {

			ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(
							agstklarbeitsplanDto.getArtikelIId(), theClientDto);
			// PJ 16851
			if (Helper.short2boolean(artikelDto.getbReinemannzeit()) == false) {

				Query query = em
						.createNamedQuery("AgstklarbeitsplanfindByAgstklIIdIArbeitsgangnummer");
				query.setParameter(1, agstklarbeitsplanDto.getAgstklIId());
				query.setParameter(2, agstklarbeitsplanDto.getIArbeitsgang());
				Collection<?> cl = query.getResultList();
				Iterator it = cl.iterator();
				while (it.hasNext()) {
					Agstklarbeitsplan ap = (Agstklarbeitsplan) it.next();
					if (!ap.getIId().equals(agstklarbeitsplanDto.getIId())) {
						ArtikelDto artikelDtoPos = getArtikelFac()
								.artikelFindByPrimaryKeySmall(
										ap.getArtikelIId(), theClientDto);
						if (Helper.short2boolean(artikelDtoPos
								.getbReinemannzeit()) == false) {
							ap.setMaschineIId(agstklarbeitsplanDto
									.getMaschineIId());
						}
					}
				}
			}
		}

	}

	private AgstklarbeitsplanDto assembleAgstklarbeitsplanDto(
			Agstklarbeitsplan agstklarbeitsplan) {
		return AgstklarbeitsplanDtoAssembler.createDto(agstklarbeitsplan);
	}

	public AgstklarbeitsplanDto agstklarbeitsplanFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) {
		Agstklarbeitsplan agstklarbeitsplan = em.find(Agstklarbeitsplan.class,
				iId);
		return assembleAgstklarbeitsplanDto(agstklarbeitsplan);
	}

	public void removeAgstklarbeitsplan(
			AgstklarbeitsplanDto agstklarbeitsplanDto, TheClientDto theClientDto) {
		Agstklarbeitsplan toRemove = em.find(Agstklarbeitsplan.class,
				agstklarbeitsplanDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public Integer getNextArbeitsgang(Integer agstklIId,
			TheClientDto theClientDto) {
		if (agstklIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("stuecklisteIId == null"));
		}
		try {
			Integer i = null;
			try {
				Query querynext = em
						.createNamedQuery("AgstklarbeitsplanejbSelectNextReihung");
				querynext.setParameter(1, agstklIId);
				i = (Integer) querynext.getSingleResult();
				if (i == null) {
					return new Integer(10);
				}

				if (i != null) {
					ParametermandantDto parameter = getParameterFac()
							.getMandantparameter(
									theClientDto.getMandant(),
									ParameterFac.KATEGORIE_STUECKLISTE,
									ParameterFac.STUECKLISTE_ERHOEHUNG_ARBEITSGANG);
					Integer iErhoehung = (Integer) parameter.getCWertAsObject();
					i = new Integer(i.intValue() + iErhoehung.intValue());
				}
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
			return i;

		} catch (NoResultException e) {
			return new Integer(10);
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

	}

	@Override
	public BelegpositionDto getNewPositionDto() {
		return new EinkaufsangebotpositionDto();
	}

	@Override
	/**
	 * Erstellt ein EinkaufsangebotpositionsDto und setzt die
	 * erforderlichen Parameter.
	 * 
	 * @param spez, Spezifikation des Stklimports
	 * @param result, einzelnes Result des Stklimports
	 * @param theClientDto, der aktuelle Benutzer
	 * 
	 * @return das vorbef&uuml;llte PositionsDto
	 */
	public BelegpositionDto preparePositionDtoAusImportResult(
			BelegpositionDto posDto, StklImportSpezifikation spez,
			IStklImportResult result, TheClientDto theClientDto) {
		
		posDto.setBArtikelbezeichnunguebersteuert(Helper.getShortFalse());
		((EinkaufsangebotpositionDto)posDto).setBMitdrucken(Helper.getShortFalse());
		((EinkaufsangebotpositionDto)posDto).setCPosition(
				result.getValues().get(EinkaufsagstklImportSpezifikation.POSITION));
		((EinkaufsangebotpositionDto)posDto).setCBemerkung(Helper.cutString(
				result.getValues().get(EinkaufsagstklImportSpezifikation.REFERENZBEMERKUNG), 
				AngebotstklFac.FieldLength.EINKAUFSANGEBOTPOSITION_CBEMERKUNG));

		return posDto;
	}

	@Override
	public void createPositions(List<BelegpositionDto> posDtos, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		createEinkaufsangebotpositions(posDtos.toArray(new EinkaufsangebotpositionDto[posDtos.size()]), theClientDto);
	}

	@Override
	public Integer getKundeIIdDerStueckliste(StklImportSpezifikation spez,
			TheClientDto theClientDto) throws RemoteException {
		EinkaufsangebotDto einkaufsangebotDto = einkaufsangebotFindByPrimaryKey(spez.getStklIId());
		
		return einkaufsangebotDto == null ? null : einkaufsangebotDto.getKundeIId();
	}

	@Override
	public IImportPositionen asPositionImporter() {
		return this;
	}

	@Override
	public IImportHead asHeadImporter() {
		return this ;
	}

}
