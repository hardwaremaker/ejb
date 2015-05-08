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
package com.lp.server.fertigung.ejbfac;

import java.awt.Color;
import java.awt.Paint;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jboss.annotation.ejb.TransactionTimeout;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.artikel.ejb.Lagerbewegung;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelgruppe;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelfehlmengeDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikelreservierungDto;
import com.lp.server.artikel.service.GeraetesnrDto;
import com.lp.server.artikel.service.HandlagerbewegungDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerabgangursprungDto;
import com.lp.server.artikel.service.LagerbewegungDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragposition;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragNachkalkulationDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.AuftragzeitenDto;
import com.lp.server.benutzer.service.BenutzerFac;
import com.lp.server.bestellung.ejb.Bestellposition;
import com.lp.server.bestellung.ejb.Bestellung;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.fertigung.ejb.Erledigtermaterialwert;
import com.lp.server.fertigung.ejb.Los;
import com.lp.server.fertigung.ejb.Losablieferung;
import com.lp.server.fertigung.ejb.Losgutschlecht;
import com.lp.server.fertigung.ejb.Losistmaterial;
import com.lp.server.fertigung.ejb.Loslagerentnahme;
import com.lp.server.fertigung.ejb.Loslosklasse;
import com.lp.server.fertigung.ejb.Lossollarbeitsplan;
import com.lp.server.fertigung.ejb.Lossollmaterial;
import com.lp.server.fertigung.ejb.Lostechniker;
import com.lp.server.fertigung.ejb.Loszusatzstatus;
import com.lp.server.fertigung.ejb.Wiederholendelose;
import com.lp.server.fertigung.ejb.Zusatzstatus;
import com.lp.server.fertigung.fastlanereader.generated.FLRLos;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosReport;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosablieferung;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosgutschlecht;
import com.lp.server.fertigung.fastlanereader.generated.FLRLossollarbeitsplan;
import com.lp.server.fertigung.fastlanereader.generated.FLROffeneags;
import com.lp.server.fertigung.fastlanereader.generated.FLRWiederholendelose;
import com.lp.server.fertigung.service.BucheSerienChnrAufLosDto;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.KapazitaetsvorschauDetailDto;
import com.lp.server.fertigung.service.KapazitaetsvorschauDto;
import com.lp.server.fertigung.service.LosAusAuftragDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosDtoAssembler;
import com.lp.server.fertigung.service.LosablieferungDto;
import com.lp.server.fertigung.service.LosablieferungDtoAssembler;
import com.lp.server.fertigung.service.LosgutschlechtDto;
import com.lp.server.fertigung.service.LosgutschlechtDtoAssembler;
import com.lp.server.fertigung.service.LosistmaterialDto;
import com.lp.server.fertigung.service.LosistmaterialDtoAssembler;
import com.lp.server.fertigung.service.LoslagerentnahmeDto;
import com.lp.server.fertigung.service.LoslagerentnahmeDtoAssembler;
import com.lp.server.fertigung.service.LoslosklasseDto;
import com.lp.server.fertigung.service.LoslosklasseDtoAssembler;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDtoAssembler;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.fertigung.service.LossollmaterialDtoAssembler;
import com.lp.server.fertigung.service.LostechnikerDto;
import com.lp.server.fertigung.service.LostechnikerDtoAssembler;
import com.lp.server.fertigung.service.LoszusatzstatusDto;
import com.lp.server.fertigung.service.LoszusatzstatusDtoAssembler;
import com.lp.server.fertigung.service.WiederholendeloseDto;
import com.lp.server.fertigung.service.WiederholendeloseDtoAssembler;
import com.lp.server.fertigung.service.ZusatzstatusDto;
import com.lp.server.fertigung.service.ZusatzstatusDtoAssembler;
import com.lp.server.kueche.service.TageslosDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.personal.ejb.Zeitdaten;
import com.lp.server.personal.fastlanereader.generated.FLRMaschine;
import com.lp.server.personal.fastlanereader.generated.FLRMaschinengruppe;
import com.lp.server.personal.fastlanereader.generated.FLRMaschinenzeitdaten;
import com.lp.server.personal.fastlanereader.generated.FLRZeitdaten;
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.personal.service.SollverfuegbarkeitDto;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.stueckliste.ejb.Fertigungsgruppe;
import com.lp.server.stueckliste.ejb.Posersatz;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.PosersatzDto;
import com.lp.server.stueckliste.service.StklagerentnahmeDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklisteMitStrukturDto;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.ejb.Status;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.BelegnummerGeneratorObj;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.pkgenerator.format.LpBelegnummer;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.EinheitDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.chart.CustomXYBarRenderer;

@Stateless
public class FertigungFacBean extends Facade implements FertigungFac {
	@PersistenceContext
	private EntityManager em;
	@Resource
	private SessionContext context;

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public LosDto createLoseAusAuftrag(LosDto losDto, Integer auftragIId,
			TheClientDto theClientDto) {

		LosDto losReturn = null;
		try {
			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(
					auftragIId);
			KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(
					auftragDto.getKundeIIdAuftragsadresse(), theClientDto);
			if (auftragDto.getStatusCNr().equals(LocaleFac.STATUS_ANGELEGT)
					|| auftragDto.getStatusCNr().equals(LocaleFac.STATUS_OFFEN)
					|| auftragDto.getStatusCNr().equals(
							LocaleFac.STATUS_TEILERLEDIGT)) {
				AuftragpositionDto[] dtos = getAuftragpositionFac()
						.auftragpositionFindByAuftrag(auftragIId);
				for (int i = 0; i < dtos.length; i++) {
					StuecklisteDto stuecklisteDto = getStuecklisteFac()
							.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
									dtos[i].getArtikelIId(), theClientDto);
					if (stuecklisteDto != null) {
						if (dtos[i].getNOffeneMenge() != null
								&& dtos[i].getNOffeneMenge().doubleValue() > 0)
							losDto.setAuftragpositionIId(dtos[i].getIId());
						losDto.setNLosgroesse(dtos[i].getNOffeneMenge());

						// SP1595 Termine berechnen

						Timestamp tAuftragsliefertermin = dtos[i]
								.getTUebersteuerbarerLiefertermin();

						Timestamp tEnde = Helper
								.addiereTageZuTimestamp(tAuftragsliefertermin,
										-kdDto.getILieferdauer());

						int durchlaufzeit = 0;
						if (stuecklisteDto.getNDefaultdurchlaufzeit() != null) {
							durchlaufzeit = stuecklisteDto
									.getNDefaultdurchlaufzeit().intValue();
						}

						Timestamp tBeginn = Helper.addiereTageZuTimestamp(
								tEnde, -durchlaufzeit);

						if (tBeginn.before(Helper.cutTimestamp(new Timestamp(
								System.currentTimeMillis())))) {

							tBeginn = Helper.cutTimestamp(new Timestamp(System
									.currentTimeMillis()));
							tEnde = Helper.addiereTageZuTimestamp(tBeginn,
									durchlaufzeit);
						}

						losDto.setTProduktionsbeginn(new java.sql.Date(tBeginn
								.getTime()));
						losDto.setTProduktionsende(new java.sql.Date(tEnde
								.getTime()));

						losDto.setStuecklisteIId(stuecklisteDto.getIId());
						losReturn = getFertigungFac().createLos(losDto,
								theClientDto);
					}
				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return losReturn;
	}

	public ArrayList<LosAusAuftragDto> holeAlleMoeglichenUnterloseEinerStueckliste(
			Integer artikelIId, BigDecimal zielmenge, int stdVorlaufzeit,
			Timestamp tLetzterBeginn, ArrayList<LosAusAuftragDto> losDtos,
			AuftragDto auftragDto, BigDecimal bdLosgroesse,
			TheClientDto theClientDto) {
		try {
			StuecklisteDto stuecklisteDto = getStuecklisteFac()
					.stuecklisteFindByMandantCNrArtikelIIdOhneExc(artikelIId,
							theClientDto);

			if (stuecklisteDto != null
					&& !stuecklisteDto.getStuecklisteartCNr().equals(
							StuecklisteFac.STUECKLISTEART_HILFSSTUECKLISTE)
					&& Helper
							.short2boolean(stuecklisteDto.getBFremdfertigung()) == false) {
				ArrayList<?> stuecklisteAufegloest = getStuecklisteFac()
						.getStrukturDatenEinerStueckliste(
								stuecklisteDto.getIId(),
								theClientDto,
								StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_ARTIKELNR,
								0, null, false, true, zielmenge, null, true);

				Timestamp tEnde = Helper.addiereTageZuTimestamp(tLetzterBeginn,
						-stdVorlaufzeit);

				if (stuecklisteDto.getNDefaultdurchlaufzeit() == null) {
					stuecklisteDto.setNDefaultdurchlaufzeit(new BigDecimal(0));
				}
				Timestamp tBeginn = Helper.addiereTageZuTimestamp(tEnde,
						-stuecklisteDto.getNDefaultdurchlaufzeit().intValue());

				LosAusAuftragDto laDto = new LosAusAuftragDto();

				LosDto losDto = new LosDto();

				losDto.setTProduktionsbeginn(new java.sql.Date(tBeginn
						.getTime()));
				losDto.setTProduktionsende(new java.sql.Date(tEnde.getTime()));
				losDto.setStuecklisteIId(stuecklisteDto.getIId());

				// PJ18596 hole alle bereits vorhandenen Unterlose zu der
				// Stueckliste und dem Auftrag
				laDto.setBereitsVorhandeneLose(losFindByAuftragIIdStuecklisteIId(
						auftragDto.getIId(), stuecklisteDto.getIId()));

				losDto.setLagerIIdZiel(stuecklisteDto.getLagerIIdZiellager());
				losDto.setFertigungsgruppeIId(stuecklisteDto
						.getFertigungsgruppeIId());
				losDto.setNLosgroesse(zielmenge.multiply(bdLosgroesse));
				losDto.setKostenstelleIId(auftragDto.getKostenstelleIId());
				losDto.setCProjekt(auftragDto.getCBezProjektbezeichnung());
				laDto.setLosDto(losDto);

				laDto.setFehlmengen(getFehlmengeFac()
						.getAnzahlFehlmengeEinesArtikels(
								stuecklisteDto.getArtikelIId(), theClientDto));
				laDto.setReservierungen(getReservierungFac()
						.getAnzahlReservierungen(
								stuecklisteDto.getArtikelIId(), theClientDto));

				BigDecimal lagerstand = new BigDecimal(0);
				LagerDto[] allelaegerDtos = getLagerFac()
						.lagerFindByMandantCNr(theClientDto.getMandant());

				for (int i = 0; i < allelaegerDtos.length; i++) {
					if (Helper.short2boolean(allelaegerDtos[i]
							.getBInternebestellung())) {
						lagerstand = lagerstand.add(getLagerFac()
								.getLagerstand(stuecklisteDto.getArtikelIId(),
										allelaegerDtos[i].getIId(),
										theClientDto));
					}

				}

				laDto.setLagerstand(lagerstand);
				laDto.setOffeneFertigungsmenge(getFertigungFac()
						.getAnzahlInFertigung(stuecklisteDto.getArtikelIId(),
								theClientDto));

				losDtos.add(laDto);

				for (int j = 0; j < stuecklisteAufegloest.size(); j++) {
					StuecklisteMitStrukturDto strukt = (StuecklisteMitStrukturDto) stuecklisteAufegloest
							.get(j);

					if (strukt.getStuecklistepositionDto() != null
							&& strukt.getStuecklistepositionDto()
									.getArtikelIId() != null) {

						losDtos = holeAlleMoeglichenUnterloseEinerStueckliste(
								strukt.getStuecklistepositionDto()
										.getArtikelIId(),
								Helper.rundeKaufmaennisch(strukt
										.getStuecklistepositionDto()
										.getNZielmenge().multiply(zielmenge), 4),
								stdVorlaufzeit, tBeginn, losDtos, auftragDto,
								bdLosgroesse, theClientDto);

					}
				}

			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return losDtos;
	}

	public String erfuellungsgradBerechnen(Integer artikelIId, Integer losIId,
			TheClientDto theClientDto) {
		String s = "Soll: ";
		LossollarbeitsplanDto[] dtos = lossollarbeitsplanFindByLosIIdArtikelIIdTaetigkeit(
				losIId, artikelIId);
		if (dtos.length > 0) {
			boolean bZuvieleZeitbuchungen = getZeiterfassungFac()
					.sindZuvieleZeitdatenEinesBelegesVorhanden(
							LocaleFac.BELEGART_LOS, losIId, theClientDto);

			s += Helper.formatZahl(dtos[0].getNGesamtzeit(), 1,
					theClientDto.getLocUi())
					+ " Std., Ist: ";

			if (bZuvieleZeitbuchungen == false) {

				try {
					Double d = getZeiterfassungFac()
							.getSummeZeitenEinesBeleges(LocaleFac.BELEGART_LOS,
									losIId, dtos[0].getIId(), null, null, null,
									theClientDto);
					s += Helper.formatZahl(d, 1, theClientDto.getLocUi())
							+ " Std.";
				} catch (RemoteException e) {
					s += "ERR";
				}
			} else {
				s += "? Std.";
			}
		}

		return s;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public int loseAusAuftragAnlegen(
			ArrayList<LosAusAuftragDto> losAusAuftragDto, Integer auftragIId,
			TheClientDto theClientDto) {

		int iAnzahlAngelegterLose = 0;
		try {
			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(
					auftragIId);
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);

			// int iUnterlose = 999;

			for (int i = 0; i < losAusAuftragDto.size(); i++) {

				if (losAusAuftragDto.get(i).getLosDto().getNLosgroesse()
						.doubleValue() > 0) {
					LosDto losDto = losAusAuftragDto.get(i).getLosDto();
					losDto.setAuftragIId(auftragIId);

					losDto.setPartnerIIdFertigungsort(mandantDto
							.getPartnerIId());
					losDto.setMandantCNr(theClientDto.getMandant());

					losDto.setKostenstelleIId(auftragDto.getKostIId());
					if (losAusAuftragDto.get(i).getAuftragpositionDto() != null) {
						losDto.setAuftragpositionIId(losAusAuftragDto.get(i)
								.getAuftragpositionDto().getIId());
					}
					/*
					 * if (losDto.getAuftragpositionIId() == null) {
					 * losDto.setISortFuerUnterlos(iUnterlose); iUnterlose =
					 * iUnterlose - 1; }
					 */

					getFertigungFac().createLos(losDto, theClientDto);
					iAnzahlAngelegterLose++;

				}

			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return iAnzahlAngelegterLose;

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public ArrayList<LosAusAuftragDto> vorschlagMitUnterlosenAusAuftrag(
			Integer auftragIId, TheClientDto theClientDto) {

		ArrayList<LosAusAuftragDto> losDtos = new ArrayList<LosAusAuftragDto>();

		try {

			ParametermandantDto parameterVorlaufzeit = getParameterFac()
					.getMandantparameter(
							theClientDto.getSMandantenwaehrung(),
							ParameterFac.KATEGORIE_FERTIGUNG,
							ParameterFac.PARAMETER_DEFAULT_VORLAUFZEIT_INTERNEBESTELLUNG);
			int iVorlaufzeit = (Integer) parameterVorlaufzeit
					.getCWertAsObject();

			Integer stdFertigungsgruppeIId = null;
			FertigungsgruppeDto[] fertigungsgruppeDtos = getStuecklisteFac()
					.fertigungsgruppeFindByMandantCNr(
							theClientDto.getMandant(), theClientDto);

			if (fertigungsgruppeDtos.length > 0) {
				stdFertigungsgruppeIId = fertigungsgruppeDtos[0].getIId();
			}

			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(
					auftragIId);
			int iLieferdauerKunde = getKundeFac().kundeFindByPrimaryKey(
					auftragDto.getKundeIIdAuftragsadresse(), theClientDto)
					.getILieferdauer();

			if (auftragDto.getStatusCNr().equals(LocaleFac.STATUS_ANGELEGT)
					|| auftragDto.getStatusCNr().equals(LocaleFac.STATUS_OFFEN)
					|| auftragDto.getStatusCNr().equals(
							LocaleFac.STATUS_TEILERLEDIGT)) {
				AuftragpositionDto[] dtos = getAuftragpositionFac()
						.auftragpositionFindByAuftrag(auftragIId);
				for (int i = 0; i < dtos.length; i++) {
					ArrayList<LosAusAuftragDto> losDtosPosition = new ArrayList<LosAusAuftragDto>();

					LosDto[] losDtosBereitsvorhanden = losFindByAuftragpositionIId(dtos[i]
							.getIId());

					if (dtos[i].getPositionsartCNr().equals(
							AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE)) {

						Timestamp tAuftragsliefertermin = dtos[i]
								.getTUebersteuerbarerLiefertermin();
						if (tAuftragsliefertermin == null) {
							tAuftragsliefertermin = auftragDto
									.getDLiefertermin();
						}

						Timestamp tEnde = Helper.addiereTageZuTimestamp(
								tAuftragsliefertermin, -iLieferdauerKunde);

						Timestamp tBeginn = Helper.addiereTageZuTimestamp(
								tEnde, -iVorlaufzeit);

						LosAusAuftragDto laDto = new LosAusAuftragDto();

						laDto.setBereitsVorhandeneLose(losDtosBereitsvorhanden);

						if (tBeginn.before(Helper.cutTimestamp(new Timestamp(
								System.currentTimeMillis())))) {
							laDto.setBDatumVerschoben(true);
							tBeginn = Helper.cutTimestamp(new Timestamp(System
									.currentTimeMillis()));
							tEnde = Helper.addiereTageZuTimestamp(tBeginn,
									iVorlaufzeit);
						}

						LosDto losDto = new LosDto();

						losDto.setAuftragIId(auftragIId);
						losDto.setAuftragpositionIId(dtos[i].getIId());

						losDto.setTProduktionsbeginn(new java.sql.Date(tBeginn
								.getTime()));
						losDto.setTProduktionsende(new java.sql.Date(tEnde
								.getTime()));
						losDto.setLagerIIdZiel(getLagerFac()
								.getHauptlagerDesMandanten(theClientDto)
								.getIId());
						losDto.setFertigungsgruppeIId(stdFertigungsgruppeIId);
						losDto.setNLosgroesse(dtos[i].getNMenge());
						losDto.setKostenstelleIId(auftragDto
								.getKostenstelleIId());
						losDto.setCProjekt(dtos[i].getCBez());

						laDto.setLosDto(losDto);

						laDto.setAuftragpositionDto(dtos[i]);

						laDto.setAuftragspositionsnummer(getAuftragpositionFac()
								.getPositionNummer(dtos[i].getIId()));

						losDtos.add(laDto);

					} else {

						// SP1107 Setartikelkopf ignorieren
						Session session = null;
						SessionFactory factory = FLRSessionFactory.getFactory();
						session = factory.openSession();
						Criteria crit = session
								.createCriteria(FLRAuftragposition.class);
						crit.add(Restrictions.eq("position_i_id_artikelset",
								dtos[i].getIId()));

						int iZeilen = crit.list().size();
						session.close();
						if (iZeilen > 0) {
							continue;
						}

						StuecklisteDto stuecklisteDto = getStuecklisteFac()
								.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
										dtos[i].getArtikelIId(), theClientDto);
						if (stuecklisteDto != null) {
							if (dtos[i].getNMenge() != null
									&& dtos[i].getNMenge().doubleValue() > 0
									&& Helper.short2boolean(stuecklisteDto
											.getBFremdfertigung()) == false) {

								Timestamp tAuftragsliefertermin = dtos[i]
										.getTUebersteuerbarerLiefertermin();
								if (tAuftragsliefertermin == null) {
									tAuftragsliefertermin = auftragDto
											.getDLiefertermin();
								}

								Timestamp tEnde = Helper
										.addiereTageZuTimestamp(
												tAuftragsliefertermin,
												-iLieferdauerKunde);

								Timestamp tBeginn = tEnde;

								if (stuecklisteDto.getNDefaultdurchlaufzeit() != null) {
									tBeginn = Helper.addiereTageZuTimestamp(
											tEnde, -stuecklisteDto
													.getNDefaultdurchlaufzeit()
													.intValue());
								}
								LosAusAuftragDto laDto = new LosAusAuftragDto();

								laDto.setBereitsVorhandeneLose(losDtosBereitsvorhanden);

								LosDto losDto = new LosDto();

								losDto.setAuftragIId(auftragIId);
								losDto.setAuftragpositionIId(dtos[i].getIId());

								losDto.setTProduktionsbeginn(new java.sql.Date(
										tBeginn.getTime()));
								losDto.setTProduktionsende(new java.sql.Date(
										tEnde.getTime()));
								losDto.setStuecklisteIId(stuecklisteDto
										.getIId());
								losDto.setLagerIIdZiel(stuecklisteDto
										.getLagerIIdZiellager());
								losDto.setCProjekt(auftragDto
										.getCBezProjektbezeichnung());
								losDto.setFertigungsgruppeIId(stuecklisteDto
										.getFertigungsgruppeIId());

								// PJ18451
								ArtikelDto aDto = getArtikelFac()
										.artikelFindByPrimaryKeySmall(
												dtos[i].getArtikelIId(),
												theClientDto);
								if (aDto.getFUeberproduktion() != null) {
									dtos[i].setNMenge(dtos[i]
											.getNMenge()
											.add(Helper.getProzentWert(
													dtos[i].getNMenge(),
													new BigDecimal(
															aDto.getFUeberproduktion()),
													4)));
								}
								losDto.setNLosgroesse(dtos[i].getNMenge());

								losDto.setKostenstelleIId(auftragDto
										.getKostenstelleIId());

								laDto.setLosDto(losDto);

								laDto.setAuftragpositionDto(dtos[i]);

								laDto.setAuftragspositionsnummer(getAuftragpositionFac()
										.getPositionNummer(dtos[i].getIId()));

								laDto.setFehlmengen(getFehlmengeFac()
										.getAnzahlFehlmengeEinesArtikels(
												stuecklisteDto.getArtikelIId(),
												theClientDto));

								ArtikelreservierungDto eigeneReservierungDto = getReservierungFac()
										.artikelreservierungFindByBelegartCNrBelegartPositionIIdOhneExc(
												LocaleFac.BELEGART_AUFTRAG,
												dtos[i].getIId());

								BigDecimal reservierungen = getReservierungFac()
										.getAnzahlReservierungen(
												stuecklisteDto.getArtikelIId(),
												theClientDto);

								if (eigeneReservierungDto != null
										&& eigeneReservierungDto.getNMenge() != null) {
									if (reservierungen.subtract(
											eigeneReservierungDto.getNMenge())
											.doubleValue() < 0) {
										reservierungen = new BigDecimal(0);
									} else {
										reservierungen = reservierungen
												.subtract(eigeneReservierungDto
														.getNMenge());
									}
								}

								laDto.setReservierungen(reservierungen);

								BigDecimal lagerstand = new BigDecimal(0);
								LagerDto[] allelaegerDtos = getLagerFac()
										.lagerFindByMandantCNr(
												theClientDto.getMandant());

								for (int j = 0; j < allelaegerDtos.length; j++) {
									if (Helper.short2boolean(allelaegerDtos[j]
											.getBInternebestellung())) {
										lagerstand = lagerstand
												.add(getLagerFac()
														.getLagerstand(
																stuecklisteDto
																		.getArtikelIId(),
																allelaegerDtos[j]
																		.getIId(),
																theClientDto));
									}

								}

								laDto.setLagerstand(lagerstand);
								laDto.setOffeneFertigungsmenge(getFertigungFac()
										.getAnzahlInFertigung(
												stuecklisteDto.getArtikelIId(),
												theClientDto));

								losDtosPosition.add(laDto);

								ArrayList<?> stuecklisteAufegloest = getStuecklisteFac()
										.getStrukturDatenEinerStueckliste(
												stuecklisteDto.getIId(),
												theClientDto,
												StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_ARTIKELNR,
												0, null, false, true,
												dtos[i].getNMenge(), null, true);

								for (int j = 0; j < stuecklisteAufegloest
										.size(); j++) {
									StuecklisteMitStrukturDto strukt = (StuecklisteMitStrukturDto) stuecklisteAufegloest
											.get(j);

									if (strukt.getStuecklistepositionDto() != null
											&& strukt
													.getStuecklistepositionDto()
													.getArtikelIId() != null) {
										losDtosPosition = holeAlleMoeglichenUnterloseEinerStueckliste(
												strukt.getStuecklistepositionDto()
														.getArtikelIId(),
												strukt.getStuecklistepositionDto()
														.getNZielmenge(),
												iVorlaufzeit, tBeginn,
												losDtosPosition, auftragDto,
												dtos[i].getNMenge(),
												theClientDto);

									}

								}

								// Wenn Termin vor Heute, dann aauf nach Heute
								// verschieben
								java.sql.Date fruehesterProduktionsbeginnVorHeute = Helper
										.cutDate(new java.sql.Date(System
												.currentTimeMillis()));

								for (int k = 0; k < losDtosPosition.size(); k++) {

									if (losDtosPosition
											.get(k)
											.getLosDto()
											.getTProduktionsbeginn()
											.before(fruehesterProduktionsbeginnVorHeute)) {
										fruehesterProduktionsbeginnVorHeute = losDtosPosition
												.get(k).getLosDto()
												.getTProduktionsbeginn();
									}

								}

								int iDiffTage = Helper.getDifferenzInTagen(
										fruehesterProduktionsbeginnVorHeute,
										new java.sql.Date(System
												.currentTimeMillis()));
								if (iDiffTage > 0) {

									for (int k = 0; k < losDtosPosition.size(); k++) {

										losDtosPosition
												.get(k)
												.getLosDto()
												.setTProduktionsbeginn(
														Helper.addiereTageZuDatum(
																losDtosPosition
																		.get(k)
																		.getLosDto()
																		.getTProduktionsbeginn(),
																iDiffTage));
										losDtosPosition
												.get(k)
												.getLosDto()
												.setTProduktionsende(
														Helper.addiereTageZuDatum(
																losDtosPosition
																		.get(k)
																		.getLosDto()
																		.getTProduktionsende(),
																iDiffTage));
										losDtosPosition.get(k)
												.setBDatumVerschoben(true);

									}
								}

								for (int k = 0; k < losDtosPosition.size(); k++) {
									losDtos.add(losDtosPosition.get(k));
								}
							}
						}
					}
				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return losDtos;
	}

	public Integer holeTageslos(Integer lagerIId, String mandantCNr,
			int iStandarddurchlaufzeit, boolean bSofortverbrauch,
			TheClientDto theClientDto) {

		Integer losIId = null;

		TageslosDto tageslosDto = null;
		try {
			tageslosDto = getKuecheFac()
					.tageslosFindByLagerIIdBSofortverbrauch(lagerIId,
							Helper.boolean2Short(bSofortverbrauch));
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		if (tageslosDto != null) {
			// Nun das erste ausgegebene Los mit der angegebenen Kostenstelle
			// des heutigen Tages suchen
			Session session = FLRSessionFactory.getFactory().openSession();
			String sQuery3 = "FROM FLRLosReport AS l WHERE l.kostenstelle_i_id="
					+ tageslosDto.getKostenstelleIId()
					+ " AND l.stueckliste_i_id IS NULL AND l.status_c_nr IN('"
					+ FertigungFac.STATUS_IN_PRODUKTION
					+ "','"
					+ FertigungFac.STATUS_TEILERLEDIGT
					+ "') AND l.t_produktionsbeginn='"
					+ Helper.formatDateWithSlashes(new java.sql.Date(System
							.currentTimeMillis())) + "' ";

			if (bSofortverbrauch) {
				sQuery3 += " AND l.flrfertigungsgruppe.c_bez='"
						+ StuecklisteFac.FERTIGUNGSGRUPPE_SOFORTVERBRAUCH
						+ "' ";
			} else {
				sQuery3 += " AND l.flrfertigungsgruppe.c_bez NOT IN('"
						+ StuecklisteFac.FERTIGUNGSGRUPPE_SOFORTVERBRAUCH
						+ "') ";
			}
			sQuery3 += " ORDER BY l.t_produktionsbeginn DESC";

			org.hibernate.Query hquery3 = session.createQuery(sQuery3);
			hquery3.setMaxResults(1);
			List<?> resultList3 = hquery3.list();
			Iterator<?> resultListIterator3 = resultList3.iterator();

			if (resultListIterator3.hasNext()) {
				FLRLosReport rep = (FLRLosReport) resultListIterator3.next();
				losIId = rep.getI_id();
			} else {
				// Wenn keines gefunden wurde, dann ein neues anlegen
				Timestamp tHeute = Helper.cutTimestamp(new Timestamp(System
						.currentTimeMillis()));

				Calendar cTemp = Calendar.getInstance();
				cTemp.set(Calendar.DAY_OF_YEAR, cTemp.get(Calendar.DAY_OF_YEAR)
						+ iStandarddurchlaufzeit);

				LosDto losDto = new LosDto();
				losDto.setTProduktionsbeginn(new java.sql.Date(tHeute.getTime()));
				losDto.setTProduktionsende(new java.sql.Date(cTemp
						.getTimeInMillis()));
				losDto.setKostenstelleIId(tageslosDto.getKostenstelleIId());
				losDto.setMandantCNr(mandantCNr);
				losDto.setNLosgroesse(new BigDecimal(1));
				try {
					FertigungsgruppeDto[] fgDto = getStuecklisteFac()
							.fertigungsgruppeFindByMandantCNr(mandantCNr,
									theClientDto);

					if (fgDto != null && fgDto.length > 0) {
						losDto.setFertigungsgruppeIId(fgDto[0].getIId());
					}

					if (bSofortverbrauch) {
						Fertigungsgruppe fertigungsgruppe;
						try {
							Query query = em
									.createNamedQuery("FertigungsgruppefindByMandantCNrCBez");
							query.setParameter(1, mandantCNr);
							query.setParameter(
									2,
									StuecklisteFac.FERTIGUNGSGRUPPE_SOFORTVERBRAUCH);
							fertigungsgruppe = (Fertigungsgruppe) query
									.getSingleResult();

							losDto.setFertigungsgruppeIId(fertigungsgruppe
									.getIId());

						} catch (javax.persistence.NoResultException e) {
							FertigungsgruppeDto fertigungsgruppeDto = new FertigungsgruppeDto();

							fertigungsgruppeDto
									.setCBez(StuecklisteFac.FERTIGUNGSGRUPPE_SOFORTVERBRAUCH);
							fertigungsgruppeDto.setISort(getStuecklisteFac()
									.getNextFertigungsgruppe(theClientDto));

							Integer i = getStuecklisteFac()
									.createFertigungsgruppe(
											fertigungsgruppeDto, theClientDto);
							losDto.setFertigungsgruppeIId(i);
						}

					}

					MandantDto mandantDto = getMandantFac()
							.mandantFindByPrimaryKey(mandantCNr, theClientDto);
					losDto.setPartnerIIdFertigungsort(mandantDto
							.getPartnerIId());

					LagerDto lagerDtoAbbuchungslager = getLagerFac()
							.lagerFindByPrimaryKey(
									tageslosDto.getLagerIIdAbbuchung());

					String projekt = "Tageslos "
							+ Helper.formatDatum(tHeute,
									theClientDto.getLocUi()) + " "
							+ lagerDtoAbbuchungslager.getCNr();

					losDto.setCProjekt(projekt);
					losDto.setLagerIIdZiel(getLagerFac()
							.getHauptlagerDesMandanten(theClientDto).getIId());

					losIId = getFertigungFac().createLos(losDto, theClientDto)
							.getIId();

					getFertigungFac().gebeLosAus(losIId, true, false,
							theClientDto, null);

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}
			session.close();
		}
		return losIId;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void alleLoseEinerStuecklisteNachkalkulieren(String artikelnummer,
			String sAbDatum, TheClientDto theClientDto) {
		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT l FROM FLRLos l WHERE l.status_c_nr ='"
				+ LocaleFac.STATUS_ERLEDIGT
				+ "' AND l.flrstueckliste.flrartikel.c_nr='" + artikelnummer
				+ "'";// AND l.t_erledigt>=
		// "+Helper.formatDateWithSlashes(tsAbErledigtdatum);

		if (sAbDatum != null && sAbDatum.length() == 10) {
			queryString += " AND l.t_erledigt>=" + sAbDatum;
		}

		queryString += " ORDER BY l.c_nr";

		org.hibernate.Query query = session.createQuery(queryString);
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			FLRLos los = (FLRLos) resultListIterator.next();

			try {

				System.out.println("Beginn der Nachkalkulation von Los "
						+ los.getC_nr());
				myLogger.logKritisch("Beginn der Nachkalkulation von Los "
						+ los.getC_nr());

				getFertigungFac()
						.aktualisiereNachtraeglichPreiseAllerLosablieferungen(
								los.getI_id(), theClientDto, false);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			} catch (Throwable e2) {
				myLogger.logKritisch("Fehler bei der Nachkalkulation des Loses "
						+ los.getC_nr()
						+ " Bitte Los manuell im Modul Fertigung nachkalkulieren, um den genauen Fehler zu erhalten");
			}

		}
	}

	public LosDto createLos(LosDto losDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// log
		myLogger.logData(losDto);
		// begin
		if (losDto.getFertigungsgruppeIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("losDto.getFertigungsgruppeIId() == null"));
		}

		// ###########
		//
		// ACHTUNG: bei Aenderungen in der Nummernerstellung auch die Barcode
		// Decoder Routine anpassen !!
		//
		// ###########

		try {
			// Geschaeftsjahr berechnen
			Integer iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(
					losDto.getMandantCNr(), getDate());
			// Es gibt 2 Varianten der Losnummer
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FERTIGUNG,
							ParameterFac.PARAMETER_LOSNUMMER_AUFTRAGSBEZOGEN);
			// a) Auftragsbezogen
			int iLosnummerAuftragsbezogen = (Integer) parameter
					.getCWertAsObject();
			if (iLosnummerAuftragsbezogen >= 1
					&& losDto.getAuftragIId() != null) {

				PKGeneratorObj pkGen = new PKGeneratorObj();
				losDto.setIId(pkGen.getNextPrimaryKey(PKConst.PK_LOS));
				// Generierung der Losnummer im Format <AB-Nr>-<I_SORT der
				// AB-Position>
				String sBelegnummer = getAuftragFac().auftragFindByPrimaryKey(
						losDto.getAuftragIId()).getCNr();

				parameter = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_ALLGEMEIN,
								ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_STELLEN_BELEGNUMMER);
				int iStellenBelegnummer = new Integer(parameter.getCWert());

				int iDiff = iStellenBelegnummer - 5;

				if (iDiff > 0) {

					if (iLosnummerAuftragsbezogen == 1) {

						// Laufende Nummer um 2 Stellen reduzieren
						int iNuller = sBelegnummer.indexOf("00");
						if (iNuller > 0) {
							sBelegnummer = sBelegnummer.substring(0, iNuller)
									+ sBelegnummer.substring(iNuller + 2);
						}
					}
					if (iLosnummerAuftragsbezogen == 2) {
						// Laufende Nummer um 3 Stellen reduzieren
						int iNuller = sBelegnummer.indexOf("000");
						if (iNuller > 0) {
							sBelegnummer = sBelegnummer.substring(0, iNuller)
									+ sBelegnummer.substring(iNuller + 3);
						}
					}
				}

				sBelegnummer = sBelegnummer + "-";
				Integer iSort = null;

				// PJ18596 Nachsehen, obs schon ein Los fuer diese Position
				// gibt, wenn ja, dann muss eine neues Los mit einer
				// Unternummer <999 angelegt werden
				boolean bEsGibBereitsEinLos = false;
				Query queryAufpos = em
						.createNamedQuery("LosfindByAuftragpositionIId");
				queryAufpos.setParameter(1, losDto.getAuftragpositionIId());
				if (queryAufpos.getResultList().size() > 0) {
					bEsGibBereitsEinLos = true;
				}

				if (losDto.getAuftragpositionIId() != null
						&& bEsGibBereitsEinLos == false) {

					iSort = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(
									losDto.getAuftragpositionIId()).getISort();

				} else {
					if (losDto.getISortFuerUnterlos() != null) {
						iSort = losDto.getISortFuerUnterlos();
					} else {
						// PJ17595
						if (iLosnummerAuftragsbezogen == 2
								&& losDto.getLosbereichIId() != null) {
							String queryString = "SELECT l.c_nr FROM FLRLos l WHERE l.c_nr LIKE '"
									+ sBelegnummer
									+ losDto.getLosbereichIId()
									+ "%' ORDER BY l.c_nr ASC";

							Session session = FLRSessionFactory.getFactory()
									.openSession();

							org.hibernate.Query query = session
									.createQuery(queryString);

							List<?> resultList = query.list();

							iSort = 0;

							Iterator<?> resultListIterator = resultList
									.iterator();
							while (resultListIterator.hasNext()) {
								String s = (String) resultListIterator.next();
								String sNummer = new java.text.DecimalFormat(
										"000").format(iSort);
								if (s.endsWith(sNummer)) {
									iSort++;
								} else {
									break;
								}

							}

							session.close();

							if (iSort >= 1000) {
								throw new EJBExceptionLP(
										EJBExceptionLP.FEHLER_FERTIGUNG_LOSNUMMER_NACH_BEREICH_UEBERLAUF,
										new Exception(
												"FEHLER_FERTIGUNG_LOSNUMMER_NACH_BEREICH_UEBERLAUF"));

							}

						} else {

							// PJ 15698
							// /naechste Nummer vor 999 verwenden
							String queryString = "SELECT l.c_nr FROM FLRLos l WHERE l.c_nr LIKE '"
									+ sBelegnummer + "%' ORDER BY l.c_nr DESC";

							Session session = FLRSessionFactory.getFactory()
									.openSession();

							org.hibernate.Query query = session
									.createQuery(queryString);

							List<?> resultList = query.list();

							iSort = 999;

							Iterator<?> resultListIterator = resultList
									.iterator();
							while (resultListIterator.hasNext()) {
								String s = (String) resultListIterator.next();
								String sNummer = new java.text.DecimalFormat(
										"000").format(iSort);
								if (s.endsWith(sNummer)) {
									iSort--;
								} else {
									break;
								}

							}

							session.close();
						}
					}
				}

				if (iLosnummerAuftragsbezogen == 2
						&& losDto.getLosbereichIId() != null) {
					sBelegnummer = sBelegnummer + losDto.getLosbereichIId()
							+ new java.text.DecimalFormat("000").format(iSort);
					losDto.setCNr(sBelegnummer);

				} else {
					sBelegnummer = sBelegnummer
							+ new java.text.DecimalFormat("000").format(iSort);
					losDto.setCNr(sBelegnummer);

				}

				try {
					Query query = em.createNamedQuery("LosfindByCNrMandantCNr");
					query.setParameter(1, losDto.getCNr());
					query.setParameter(2, theClientDto.getMandant());
					Los doppelt = (Los) query.getSingleResult();
					if (doppelt != null) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
								new Exception("FERT_LOS.UK:" + doppelt.getCNr()));
					}
				} catch (NoResultException ex1) {
					// nothing here
				}

			}

			// b) "normal"
			else {
				BelegnummerGeneratorObj bnGen = new BelegnummerGeneratorObj();
				LpBelegnummerFormat f = getBelegnummerGeneratorObj()
						.getBelegnummernFormat(losDto.getMandantCNr());
				LpBelegnummer bnr = bnGen.getNextBelegNr(iGeschaeftsjahr,
						PKConst.PK_LOS, losDto.getMandantCNr(), theClientDto);
				losDto.setIId(bnr.getPrimaryKey());
				String belegNummer = f.format(bnr);
				losDto.setCNr(belegNummer);
			}
			losDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			losDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			// Auf angelegt setzen
			losDto.setStatusCNr(FertigungFac.STATUS_ANGELEGT);
			Los los = new Los(losDto.getIId(), losDto.getMandantCNr(),
					losDto.getCNr(), losDto.getKostenstelleIId(),
					losDto.getNLosgroesse(), losDto.getTProduktionsende(),
					losDto.getTProduktionsbeginn(), losDto.getLagerIIdZiel(),
					losDto.getStatusCNr(), losDto.getPersonalIIdAnlegen(),
					losDto.getPersonalIIdAendern(),
					losDto.getFertigungsgruppeIId());

			em.persist(los);
			em.flush();
			losDto.setTAendern(los.getTAendern());
			losDto.setTAnlegen(los.getTAnlegen());
			setLosFromLosDto(los, losDto);
			if (losDto.getStuecklisteIId() != null) {
				// Material + Arbeitsplan eintragen
				aktualisiereSollMaterialAusStueckliste(losDto.getIId(),
						theClientDto, false);
				aktualisiereSollArbeitsplanAusStueckliste(losDto.getIId(),
						theClientDto);
			} else {
				defaultArbeitszeitartikelErstellen(losDto, theClientDto);
			}

			// PJ18575
			if (losDto.getStuecklisteIId() != null) {

				StklagerentnahmeDto[] stklagerentnahmeDtos = getStuecklisteFac()
						.stklagerentnahmeFindByStuecklisteIId(
								losDto.getStuecklisteIId());
				if (stklagerentnahmeDtos != null
						&& stklagerentnahmeDtos.length > 0) {
					for (int i = 0; i < stklagerentnahmeDtos.length; i++) {
						LoslagerentnahmeDto loslager = new LoslagerentnahmeDto();
						loslager.setLosIId(losDto.getIId());
						loslager.setLagerIId(stklagerentnahmeDtos[i]
								.getLagerIId());
						createLoslagerentnahme(loslager, theClientDto);
					}
				}
			}

			// default materialentnahme vom Hauptlager des Mandanten
			LagerDto lagerDto = getLagerFac().getHauptlagerDesMandanten(
					theClientDto);

			LagerDto[] lagerDtos = getLagerFac()
					.lagerFindByMandantCNrOrderByILoslagersort(
							theClientDto.getMandant());

			if (lagerDtos != null && lagerDtos.length > 0) {
				for (int i = 0; i < lagerDtos.length; i++) {
					try {
						Query query = em
								.createNamedQuery("LoslagerentnahmefindByLosIIdLagerIId");
						query.setParameter(1, losDto.getIId());
						query.setParameter(2, lagerDtos[i].getIId());
						Loslagerentnahme vorhanden = (Loslagerentnahme) query
								.getSingleResult();
					} catch (NoResultException ex1) {
						LoslagerentnahmeDto loslager = new LoslagerentnahmeDto();
						loslager.setLosIId(losDto.getIId());
						loslager.setLagerIId(lagerDtos[i].getIId());
						createLoslagerentnahme(loslager, theClientDto);
					}

				}
			} else {
				try {
					Query query = em
							.createNamedQuery("LoslagerentnahmefindByLosIIdLagerIId");
					query.setParameter(1, losDto.getIId());
					query.setParameter(2, lagerDto.getIId());
					query.getSingleResult();
					Loslagerentnahme vorhanden = (Loslagerentnahme) query
							.getSingleResult();
				} catch (NoResultException ex1) {
					LoslagerentnahmeDto loslager = new LoslagerentnahmeDto();
					loslager.setLosIId(losDto.getIId());
					loslager.setLagerIId(lagerDto.getIId());
					createLoslagerentnahme(loslager, theClientDto);
				}

			}

			return losDto;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public Integer defaultArbeitszeitartikelErstellen(LosDto losDto,
			TheClientDto theClientDto) {
		// 18. April 2008: lt. WH: Wenn eine Materialliste angelegt,
		// wird automatisch eine Arbeitsplanposition
		// mit der Ruestzeit 1 Sekunde angelegt:
		// Artikel=Default-AZ-Artikel
		try {
			LossollarbeitsplanDto lossollarbeitsplanDto = new LossollarbeitsplanDto();

			ParametermandantDto parameterDtoDefaultarbeitszeit = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_DEFAULT_ARBEITSZEITARTIKEL);

			if (parameterDtoDefaultarbeitszeit != null
					&& parameterDtoDefaultarbeitszeit.getCWert() != null
					&& !parameterDtoDefaultarbeitszeit.getCWert().trim()
							.equals("")) {
				try {

					ArtikelDto artikelDtoDefaultArbeiztszeit = getArtikelFac()
							.artikelFindByCNr(
									parameterDtoDefaultarbeitszeit.getCWert(),
									theClientDto);
					lossollarbeitsplanDto
							.setArtikelIIdTaetigkeit(artikelDtoDefaultArbeiztszeit
									.getIId());
				} catch (EJBExceptionLP ex2) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DEFAULT_ARBEITSZEITARTIKEL_NICHT_DEFINIERT,
							new Exception(
									"FEHLER_DEFAULT_ARBEITSZEITARTIKEL_NICHT_DEFINIERT"));
				}
				lossollarbeitsplanDto.setBNachtraeglich(Helper
						.boolean2Short(false));
				lossollarbeitsplanDto.setIArbeitsgangnummer(10);
				lossollarbeitsplanDto.setLosIId(losDto.getIId());
				lossollarbeitsplanDto.setLRuestzeit((long) 1000);
				lossollarbeitsplanDto.setLStueckzeit((long) 0);
				lossollarbeitsplanDto.setBFertig(Helper.boolean2Short(false));
				lossollarbeitsplanDto.setBNurmaschinenzeit(Helper
						.boolean2Short(false));
				// SP1188
				lossollarbeitsplanDto.setBAutoendebeigeht(Helper
						.boolean2Short(true));
				lossollarbeitsplanDto = createLossollarbeitsplan(
						lossollarbeitsplanDto, theClientDto);
				return lossollarbeitsplanDto.getIId();

			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return null;
	}

	public void removeLos(LosDto losDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (losDto != null) {
			Integer iId = losDto.getIId();
			Los toRemove = em.find(Los.class, iId);
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						er);
			}
		}

	}

	public void vonSollpositionMengeZuruecknehmen(Integer lossollmaterialIId,
			BigDecimal nMenge, Integer losIIdZiel, Integer lagerIIdZiel,
			TheClientDto theClientDto) {

		BigDecimal bdAusgegeben = getAusgegebeneMenge(lossollmaterialIId, null,
				theClientDto);

		BigDecimal bdMengeGesamt = new BigDecimal(nMenge.doubleValue());

		if (nMenge.doubleValue() > bdAusgegeben.doubleValue()) {
			nMenge = bdAusgegeben;
		}

		LossollmaterialDto solldto = lossollmaterialFindByPrimaryKey(lossollmaterialIId);

		if (nMenge.doubleValue() > 0) {

			LosistmaterialDto[] dtosLosist = losistmaterialFindByLossollmaterialIId(lossollmaterialIId);

			for (int j = 0; j < dtosLosist.length; j++) {
				if (dtosLosist[j].getNMenge().doubleValue() > 0
						&& nMenge.doubleValue() > 0) {
					BigDecimal istmenge = dtosLosist[j].getNMenge();

					BigDecimal diffPosition = dtosLosist[j].getNMenge();

					BigDecimal bdMengeNeu = null;

					if (nMenge.doubleValue() > istmenge.doubleValue()) {
						bdMengeNeu = new BigDecimal(0);
						nMenge = nMenge.subtract(istmenge);
					} else {
						bdMengeNeu = istmenge.subtract(nMenge);
						nMenge = new BigDecimal(0);
					}

					diffPosition = diffPosition.subtract(bdMengeNeu);

					updateLosistmaterialMenge(dtosLosist[j].getIId(),
							bdMengeNeu, theClientDto);

					if (lagerIIdZiel != null && diffPosition.doubleValue() > 0) {
						try {
							if (!lagerIIdZiel.equals(dtosLosist[j]
									.getLagerIId())) {
								getLagerFac()
										.bucheUm(
												solldto.getArtikelIId(),
												dtosLosist[j].getLagerIId(),
												solldto.getArtikelIId(),
												lagerIIdZiel,
												diffPosition,
												null,
												"Umbuchung Materialumlagerung",
												getLagerFac()
														.getGemittelterGestehungspreisEinerAbgangsposition(
																LocaleFac.BELEGART_LOS,
																dtosLosist[j]
																		.getIId()),
												theClientDto);
							}
						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}
					}

					if (losIIdZiel != null) {

						LossollmaterialDto[] sollDtos = lossollmaterialFindyByLosIIdArtikelIId(
								losIIdZiel, solldto.getArtikelIId(),
								theClientDto);

						if (sollDtos != null && sollDtos.length > 0) {
							LossollmaterialDto solldtoZiel = lossollmaterialFindByPrimaryKey(sollDtos[0]
									.getIId());
							LosistmaterialDto losistmaterialDto = new LosistmaterialDto();
							losistmaterialDto.setLagerIId(dtosLosist[j]
									.getLagerIId());
							losistmaterialDto.setBAbgang(Helper
									.boolean2Short(true));
							losistmaterialDto.setNMenge(diffPosition);

							gebeMaterialNachtraeglichAus(solldtoZiel,
									losistmaterialDto, null, true, theClientDto);
						} else {
							LossollmaterialDto lossollmaterialDto = new LossollmaterialDto();
							lossollmaterialDto.setLosIId(losIIdZiel);
							try {
								lossollmaterialDto
										.setNSollpreis(getLagerFac()
												.getGemittelterGestehungspreisEinesLagers(
														solldto.getArtikelIId(),
														dtosLosist[j]
																.getLagerIId(),
														theClientDto));
							} catch (RemoteException e) {
								throwEJBExceptionLPRespectOld(e);
							}
							lossollmaterialDto.setNMenge(bdMengeGesamt);
							lossollmaterialDto.setMontageartIId(solldto
									.getMontageartIId());

							ArtikelDto aDto = getArtikelFac()
									.artikelFindByPrimaryKey(
											solldto.getArtikelIId(),
											theClientDto);

							lossollmaterialDto.setEinheitCNr(aDto
									.getEinheitCNr());
							lossollmaterialDto.setArtikelIId(solldto
									.getArtikelIId());

							LosistmaterialDto losistmaterialDto = new LosistmaterialDto();
							losistmaterialDto.setLagerIId(dtosLosist[j]
									.getLagerIId());
							losistmaterialDto.setBAbgang(Helper
									.boolean2Short(true));

							losistmaterialDto.setNMenge(diffPosition);

							gebeMaterialNachtraeglichAus(lossollmaterialDto,
									losistmaterialDto, null, true, theClientDto);
						}

					}

				}
			}
		}

	}

	public void aendereLosgroesse(Integer losIId, Integer neueLosgroesse,
			boolean bUeberzaehligesMaterialZurueckgeben,
			TheClientDto theClientDto) {
		LosDto losDto = losFindByPrimaryKey(losIId);
		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_AUSGEGEBEN)
				|| losDto.getStatusCNr().equals(
						FertigungFac.STATUS_IN_PRODUKTION)
				|| losDto.getStatusCNr().equals(FertigungFac.STATUS_GESTOPPT)
				|| losDto.getStatusCNr().equals(
						FertigungFac.STATUS_TEILERLEDIGT)) {

			if (!losDto.getNLosgroesse().equals(new BigDecimal(neueLosgroesse))) {

				BigDecimal bdErledigte = getErledigteMenge(losDto.getIId(),
						theClientDto);

				if (bdErledigte.doubleValue() > neueLosgroesse.doubleValue()) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FERTIGUNG_AENDERUNG_LOGROESSE_ZUVIELEABLIEFERUNGEN,
							new Exception(
									"bdErledigte.doubleValue()>neueLosgroesse.doubleValue()"));
				}

				Los los = em.find(Los.class, losDto.getIId());
				los.setNLosgroesse(new BigDecimal(neueLosgroesse));
				em.merge(los);
				em.flush();

				// Material
				LossollmaterialDto[] dtos = lossollmaterialFindByLosIId(losIId);
				for (int i = 0; i < dtos.length; i++) {
					LossollmaterialDto dto = dtos[i];
					// Sollmengen aendern
					BigDecimal sollsatzgroesse = dto.getNMenge().divide(
							losDto.getNLosgroesse(), 10,
							BigDecimal.ROUND_HALF_EVEN);
					dto.setNMenge(Helper.rundeKaufmaennisch(sollsatzgroesse
							.multiply(new BigDecimal(neueLosgroesse)), 3));
					updateLossollmaterial(dto, theClientDto);

					// Wenn kleiner je nach parameter
					// bUeberzaehligesMaterialZurueckgeben material
					// zurueckbuchen
					if (neueLosgroesse.doubleValue() < losDto.getNLosgroesse()
							.doubleValue()
							&& bUeberzaehligesMaterialZurueckgeben == true) {
						BigDecimal bdAusgegeben = getAusgegebeneMenge(
								dto.getIId(), null, theClientDto);
						BigDecimal diff = bdAusgegeben
								.subtract(dto.getNMenge());
						if (diff.doubleValue() > 0) {

							LosistmaterialDto[] dtosLosist = losistmaterialFindByLossollmaterialIId(dto
									.getIId());

							for (int j = 0; j < dtosLosist.length; j++) {
								if (diff.doubleValue() > 0) {
									BigDecimal istmenge = dtosLosist[j]
											.getNMenge();

									BigDecimal bdMengeNeu = null;

									if (diff.doubleValue() > istmenge
											.doubleValue()) {
										bdMengeNeu = new BigDecimal(0);
										diff = diff.subtract(istmenge);
									} else {
										bdMengeNeu = istmenge.subtract(diff);
										diff = new BigDecimal(0);
									}

									updateLosistmaterialMenge(
											dtosLosist[j].getIId(), bdMengeNeu,
											theClientDto);
								}
							}
						}
					}

					// Fehlmengen aktualisieren
					try {
						getFehlmengeFac().aktualisiereFehlmenge(
								LocaleFac.BELEGART_LOS, dto.getIId(), false,
								theClientDto);
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}

				// Arbeitsplan
				LossollarbeitsplanDto[] arbeitsplan = lossollarbeitsplanFindByLosIId(losIId);
				for (int i = 0; i < arbeitsplan.length; i++) {
					LossollarbeitsplanDto dto = arbeitsplan[i];
					// Gesamtzeit wird austomatisch aktualisiert
					updateLossollarbeitsplan(dto, theClientDto);

				}

			}

		} else {
			if (losDto.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT,
						new Exception("los " + losDto.getCNr()
								+ " ist storniert"));
			} else if (losDto.getStatusCNr().equals(
					FertigungFac.STATUS_ERLEDIGT)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
						new Exception("los " + losDto.getCNr()
								+ " ist bereits erledigt"));
			} else if (losDto.getStatusCNr().equals(
					FertigungFac.STATUS_ANGELEGT)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN,
						new Exception("los " + losDto.getCNr()
								+ " ist noch nicht ausgegeben"));
			}
		}
	}

	public void updateLosKommentar(Integer losIId, String cKommentar) {
		// try {
		Los los = em.find(Los.class, losIId);
		if (los == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}
		los.setXText(cKommentar);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, ex);
		// }
	}

	public void updateLosProduktionsinformation(Integer losIId,
			String cProduktionsinformation) {
		Los los = em.find(Los.class, losIId);
		if (los == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}
		los.setXProduktionsinformation(cProduktionsinformation);
		em.merge(los);
		em.flush();

	}

	public void updateBewertungKommentarImLos(LosDto losDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Los los = em.find(Los.class, losDto.getIId());
		if (los == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}
		los.setFBewertung(losDto.getFBewertung());
		los.setCProjekt(losDto.getCProjekt());
	}

	public void offenAgsUmreihen(Integer lossollarbeitsplanIId,
			boolean bNachUntenReihen) {
		Lossollarbeitsplan sa = em.find(Lossollarbeitsplan.class,
				lossollarbeitsplanIId);
		if (sa.getIMaschinenversatzMs() == null) {
			sa.setIMaschinenversatzMs(0);
		}
		if (bNachUntenReihen) {
			sa.setIMaschinenversatzMs(sa.getIMaschinenversatzMs() + 1);
		} else {
			if (sa.getIMaschinenversatzMs() > 0) {
				sa.setIMaschinenversatzMs(sa.getIMaschinenversatzMs() - 1);
			}
		}
	}

	public LosDto updateLos(LosDto losDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// begin
		if (losDto.getFertigungsgruppeIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("losDto.getFertigungsgruppeIId() == null"));
		}

		losDto.setTAendern(getTimestamp());
		losDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		// try {
		boolean bUdateMenge = false;
		boolean bUdateProduktionsbeginn = false;

		Los los = em.find(Los.class, losDto.getIId());
		if (los == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}
		if (los.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
					new Exception("los " + los.getCNr()
							+ " ist bereits erledigt"));
		} else if (los.getStatusCNr().equals(FertigungFac.STATUS_AUSGEGEBEN)
				|| los.getStatusCNr().equals(FertigungFac.STATUS_IN_PRODUKTION)
				|| los.getStatusCNr().equals(FertigungFac.STATUS_TEILERLEDIGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_AUSGEGEBEN,
					new Exception("los " + los.getCNr()
							+ " ist bereits ausgegeben"));
		} else if (los.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT,
					new Exception("los " + los.getCNr() + " ist storniert"));
		}
		// wurde die Losgroesse geaendert?
		if (los.getNLosgroesse().compareTo(losDto.getNLosgroesse()) != 0) {
			bUdateMenge = true;
		}
		// wurde die Stueckliste geaendert?
		boolean bArbeitsplanUpdaten = false;
		if (los.getStuecklisteIId() != null
				&& !los.getStuecklisteIId().equals(losDto.getStuecklisteIId())) {
			bUdateMenge = true;
			bArbeitsplanUpdaten = true;
		}
		if (los.getStuecklisteIId() == null
				&& losDto.getStuecklisteIId() != null) {
			bUdateMenge = true;
			bArbeitsplanUpdaten = true;
		}

		// wurde der Produktionsbeginn geaendert?
		if (los.getTProduktionsbeginn().compareTo(
				losDto.getTProduktionsbeginn()) != 0) {
			bUdateProduktionsbeginn = true;
		}

		if (bArbeitsplanUpdaten == true) {
			if (los.getStuecklisteIId() != null
					&& losDto.getStuecklisteIId() == null) {
				getFertigungFac().vorhandenenArbeitsplanLoeschen(
						losDto.getIId());
				defaultArbeitszeitartikelErstellen(losDto, theClientDto);
				// Material entfernen

				LossollmaterialDto[] sollmatDtos = lossollmaterialFindByLosIId(losDto
						.getIId());
				for (int i = 0; i < sollmatDtos.length; i++) {

					// PJ 16952
					if (Helper
							.short2boolean(sollmatDtos[i].getBNachtraeglich()) == false) {
						removeLossollmaterial(sollmatDtos[i], theClientDto);
					}
				}
				bUdateMenge = true;
			} else {
				setLosFromLosDto(los, losDto);
				aktualisiereSollArbeitsplanAusStueckliste(losDto.getIId(),
						theClientDto);
			}
		}

		setLosFromLosDto(los, losDto);
		// dauer neu berechnen

		if (bUdateMenge) {
			// Gesamtzeiten aufrollen
			updateGesamtzeiten(losDto.getIId(), theClientDto);
		}
		if (losDto.getStuecklisteIId() != null) {
			if (bUdateMenge || bUdateProduktionsbeginn) {
				// Reservierungen aktualisieren
				aktualisiereSollMaterialAusStueckliste(losDto.getIId(),
						theClientDto, true);
			}
		}
		// und retour
		return losDto;
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, ex);
		// }
	}

	public LosDto losFindByPrimaryKey(Integer iId) throws EJBExceptionLP {

		LosDto los = losFindByPrimaryKeyOhneExc(iId);
		if (los == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return los;
	}

	public LosDto losFindByPrimaryKeyOhneExc(Integer iId) {
		Los los = em.find(Los.class, iId);
		if (los == null) {
			return null;
		}
		return assembleLosDto(los);
	}

	public LosDto losFindByCNrMandantCNr(String cNr, String mandantCNr)
			throws EJBExceptionLP {
		try {
			Query query = em.createNamedQuery("LosfindByCNrMandantCNr");
			query.setParameter(1, cNr);
			query.setParameter(2, mandantCNr);
			Los los = (Los) query.getSingleResult();
			if (los == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleLosDto(los);
		} catch (NoResultException ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}
	}

	public LosDto losFindByCNrMandantCNrOhneExc(String cNr, String mandantCNr) {

		Query query = em.createNamedQuery("LosfindByCNrMandantCNr");
		query.setParameter(1, cNr);
		query.setParameter(2, mandantCNr);
		try {
			Los los = (Los) query.getSingleResult();
			return assembleLosDto(los);
		} catch (NoResultException ex) {
			return null;
		}
	}

	public LosDto[] losFindByAuftragIId(Integer auftragIId) {
		try {
			Query query = em.createNamedQuery("LosfindByAuftragIId");
			query.setParameter(1, auftragIId);
			Collection c = query.getResultList();

			return assembleLosDtos(c);
		} catch (NoResultException ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}
	}

	public LosDto[] losFindByAuftragpositionIId(Integer auftragpositionIId) {
		try {
			Query query = em.createNamedQuery("LosfindByAuftragpositionIId");
			query.setParameter(1, auftragpositionIId);
			Collection c = query.getResultList();

			return assembleLosDtos(c);
		} catch (NoResultException ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}
	}

	public LosDto[] losFindByAuftragIIdStuecklisteIId(Integer auftragIId,
			Integer stuecklisteIId) {
		try {
			Query query = em
					.createNamedQuery("LosfindByAuftragIIdStuecklisteIId");
			query.setParameter(1, auftragIId);
			query.setParameter(2, stuecklisteIId);
			Collection c = query.getResultList();

			return assembleLosDtos(c);
		} catch (NoResultException ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}
	}

	private void setLosFromLosDto(Los los, LosDto losDto) {
		los.setMandantCNr(losDto.getMandantCNr());
		los.setCNr(losDto.getCNr());
		los.setLosIIdElternlos(losDto.getLosIIdElternlos());
		los.setKostenstelleIId(losDto.getKostenstelleIId());
		los.setAuftragpositionIId(losDto.getAuftragpositionIId());
		los.setCKommentar(losDto.getCKommentar());
		los.setCProjekt(losDto.getCProjekt());
		los.setStuecklisteIId(losDto.getStuecklisteIId());
		los.setNLosgroesse(losDto.getNLosgroesse());
		los.setFertigungsgruppeIId(losDto.getFertigungsgruppeIId());
		los.setPartnerIIdFertigungsort(losDto.getPartnerIIdFertigungsort());
		los.setPersonalIIdTechniker(losDto.getPersonalIIdTechniker());
		los.setTProduktionsende(losDto.getTProduktionsende());
		los.setTProduktionsbeginn(losDto.getTProduktionsbeginn());
		los.setTAusgabe(losDto.getTAusgabe());
		los.setPersonalIIdAusgabe(losDto.getPersonalIIdAusgabe());
		los.setFBewertung(losDto.getFBewertung());
		los.setTErledigt(losDto.getTErledigt());
		los.setPersonalIIdErledigt(losDto.getPersonalIIdErledigt());
		los.setTProduktionsstop(losDto.getTProduktionsstop());
		los.setPersonalIIdProduktionsstop(losDto
				.getPersonalIIdProduktionsstop());
		los.setTLeitstandstop(losDto.getTLeitstandstop());
		los.setPersonalIIdLeitstandstop(losDto.getPersonalIIdLeitstandstop());
		los.setLagerIIdZiel(losDto.getLagerIIdZiel());
		los.setStatusCNr(losDto.getStatusCNr());
		los.setTAktualisierungstueckliste(losDto
				.getTAktualisierungstueckliste());
		los.setTAktualisierungarbeitszeit(losDto
				.getTAktualisierungarbeitszeit());
		los.setPersonalIIdAnlegen(losDto.getPersonalIIdAnlegen());
		los.setTAnlegen(losDto.getTAnlegen());
		los.setPersonalIIdAendern(losDto.getPersonalIIdAendern());
		los.setTAendern(losDto.getTAendern());
		los.setPersonalIIdManuellerledigt(losDto
				.getPersonalIIdManuellerledigt());
		los.setTManuellerledigt(losDto.getTManuellerledigt());
		los.setWiederholendeloseIId(losDto.getWiederholendeloseIId());
		los.setXText(losDto.getXText());
		los.setCZusatznummer(losDto.getCZusatznummer());
		los.setAuftragIId(losDto.getAuftragIId());
		los.setXProduktionsinformation(losDto.getXProduktionsinformation());
		los.setKundeIId(losDto.getKundeIId());
		los.setNSollmaterial(losDto.getNSollmaterial());
		los.setLosbereichIId(losDto.getLosbereichIId());
		los.setTMaterialvollstaendig(losDto.getTMaterialvollstaendig());
		los.setPersonalIIdMaterialvollstaendig(losDto
				.getPersonalIIdMaterialvollstaendig());
		los.setProjektIId(losDto.getProjektIId());

		em.merge(los);
		em.flush();
	}

	private LosDto assembleLosDto(Los los) {
		return LosDtoAssembler.createDto(los);
	}

	private LosDto[] assembleLosDtos(Collection<?> loss,
			boolean bMitStorniertenLosen) {
		List<LosDto> list = new ArrayList<LosDto>();
		if (loss != null) {
			Iterator<?> iterator = loss.iterator();
			while (iterator.hasNext()) {
				Los los = (Los) iterator.next();

				if (bMitStorniertenLosen == true) {
					list.add(assembleLosDto(los));
				} else {
					if (!los.getStatusCNr().equals(
							FertigungFac.STATUS_STORNIERT)) {
						list.add(assembleLosDto(los));
					}
				}

			}
		}
		LosDto[] returnArray = new LosDto[list.size()];
		return (LosDto[]) list.toArray(returnArray);
	}

	private LosDto[] assembleLosDtos(Collection<?> loss) {
		return assembleLosDtos(loss, false);
	}

	public LossollmaterialDto createLossollmaterial(
			LossollmaterialDto lossollmaterialDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// log
		myLogger.logData(lossollmaterialDto);
		// begin
		LosDto losDto = losFindByPrimaryKey(lossollmaterialDto.getLosIId());
		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
					"");
		}
		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT, "");
		}

		if (losDto.getTMaterialvollstaendig() != null) {
			ArrayList al = new ArrayList();
			al.add(losDto.getCNr());
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_MATERIAL_VOLLSTAENDIG, al,
					new Exception("FEHLER_FERTIGUNG_MATERIAL_VOLLSTAENDIG"
							+ " Los:" + losDto.getCNr()));
		}

		if (lossollmaterialDto.getIBeginnterminoffset() == null) {
			lossollmaterialDto.setIBeginnterminoffset(0);
		}

		lossollmaterialDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		// primary key
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(
				PKConst.PK_LOSSOLLMATERIAL);
		lossollmaterialDto.setIId(iId);
		// nachtraeglich ?
		boolean bNachtraeglich = false;
		if (lossollmaterialDto.getBNachtraeglich() == null) {
			lossollmaterialDto.setBNachtraeglich(Helper.boolean2Short(true));
			bNachtraeglich = true;
		}
		if (getMandantFac().hatZusatzfunktionberechtigung(
				MandantFac.ZUSATZFUNKTION_GERAETESERIENNUMMERN, theClientDto)) {
			if (losDto.getStuecklisteIId() != null) {

				// PJ 16622

				Artikel artikel = em.find(Artikel.class,
						lossollmaterialDto.getArtikelIId());
				if (Helper.short2boolean(artikel.getBSeriennrtragend())) {

					BigDecimal ssg = lossollmaterialDto.getNMenge().divide(
							losDto.getNLosgroesse(), 4,
							BigDecimal.ROUND_HALF_UP);

					if (ssg.doubleValue() != 1) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_POSITIONSMENGE_EINES_SNR_ARTIKELS_MUSS_1_SEIN_WENN_GERAETESNR,
								new Exception(
										"FEHLER_POSITIONSMENGE_EINES_SNR_ARTIKELS_MUSS_1_SEIN_WENN_GERAETESNR"));

					}

				}

			}
		}

		try {

			// SP2795

			StuecklisteDto stklDto = getStuecklisteFac()
					.stuecklisteFindByArtikelIIdMandantCNrOhneExc(
							lossollmaterialDto.getArtikelIId(),
							theClientDto.getMandant());

			if (stklDto != null
					&& stklDto.getStuecklisteartCNr().equals(
							StuecklisteFac.STUECKLISTEART_HILFSSTUECKLISTE)) {
				Artikel artikel = em.find(Artikel.class,
						lossollmaterialDto.getArtikelIId());
				ArrayList al = new ArrayList();
				al.add(artikel.getCNr());
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FERTIGUNG_HILFSSTUECKLISTE_DARF_KEINE_SOLLPOSITION_SEIN,
						al,
						new Exception(
								"FEHLER_FERTIGUNG_HILFSSTUECKLISTE_DARF_KEINE_SOLLPOSITION_SEIN"));
			}

			// rounddto: vor dem Create
			lossollmaterialDto.round(
					new Integer(4),
					getMandantFac().getNachkommastellenPreisAllgemein(
							theClientDto.getMandant()));
			Lossollmaterial lossollmaterial = new Lossollmaterial(
					lossollmaterialDto.getIId(),
					lossollmaterialDto.getLosIId(),
					lossollmaterialDto.getArtikelIId(),
					lossollmaterialDto.getNMenge(),
					lossollmaterialDto.getEinheitCNr(),
					lossollmaterialDto.getMontageartIId(),
					lossollmaterialDto.getISort(),
					lossollmaterialDto.getBNachtraeglich(),
					lossollmaterialDto.getNSollpreis(),
					lossollmaterialDto.getPersonalIIdAendern(),
					lossollmaterialDto.getIBeginnterminoffset());

			em.persist(lossollmaterial);
			em.flush();
			lossollmaterialDto.setTAendern(lossollmaterial.getTAendern());
			setLossollmaterialFromLossollmaterialDto(lossollmaterial,
					lossollmaterialDto, theClientDto);
			// reservierung
			if (bNachtraeglich) {
				// Reservierung anlegen
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKey(
								lossollmaterialDto.getArtikelIId(),
								theClientDto);
				// wenn los angelegt -> reservierung
				if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
					java.sql.Date dTermin;
					if (lossollmaterialDto.getNMenge().compareTo(
							new BigDecimal(0)) > 0) {
						// Positive Reservierung: produktionsstart
						dTermin = losDto.getTProduktionsbeginn();
					} else {
						// Negative Reservierung: produktionsende
						dTermin = losDto.getTProduktionsende();
					}
					createReservierung(artikelDto, lossollmaterialDto.getIId(),
							lossollmaterialDto.getNMenge(),
							new java.sql.Timestamp(dTermin.getTime()));
				}
				// wenn ausgegeben -> fehlmenge
				else {
					getFehlmengeFac().aktualisiereFehlmenge(
							LocaleFac.BELEGART_LOS,
							lossollmaterialDto.getIId(), false, theClientDto);
				}
			}
			return lossollmaterialDto;
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	public void removeLossollmaterial(LossollmaterialDto lossollmaterialDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(lossollmaterialDto);
		LosDto losDto = losFindByPrimaryKey(lossollmaterialDto.getLosIId());
		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
					"");
		}

		if (losDto.getTMaterialvollstaendig() != null) {
			ArrayList al = new ArrayList();
			al.add(losDto.getCNr());
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_MATERIAL_VOLLSTAENDIG, al,
					new Exception("FEHLER_FERTIGUNG_MATERIAL_VOLLSTAENDIG"
							+ " Los:" + losDto.getCNr()));
		}

		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT, "");
		}
		try {
			if (lossollmaterialDto != null) {

				// Istmaterial loeschen, wenn 0

				LosistmaterialDto[] istmatDtos = losistmaterialFindByLossollmaterialIId(lossollmaterialDto
						.getIId());

				for (int i = 0; i < istmatDtos.length; i++) {
					if (istmatDtos[i].getNMenge().doubleValue() == 0) {
						Losistmaterial toRemove = em.find(Losistmaterial.class,
								istmatDtos[i].getIId());
						em.remove(toRemove);
						em.flush();
					} else {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_FERTIGUNG_ES_IST_NOCH_MATERIAL_AUSGEGEBEN,
								"");
					}
				}

				Integer iId = lossollmaterialDto.getIId();
				Lossollmaterial toRemove = em.find(Lossollmaterial.class, iId);
				if (toRemove == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
				}
				try {
					em.remove(toRemove);
					em.flush();
				} catch (EntityExistsException er) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
				}
				// angelegt -> reservierung loeschen
				if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
					// Reservierung loeschen
					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(
									lossollmaterialDto.getArtikelIId(),
									theClientDto);
					removeReservierung(artikelDto, lossollmaterialDto.getIId());
				}
				// ausgegeben -> fehlmenge loeschen
				else {
					getFehlmengeFac().aktualisiereFehlmenge(
							LocaleFac.BELEGART_LOS,
							lossollmaterialDto.getIId(), false, theClientDto);
				}

			}
		}
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
		catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public LossollmaterialDto updateLossollmaterial(
			LossollmaterialDto lossollmaterialDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Integer iId = lossollmaterialDto.getIId();
		LosDto losDto = losFindByPrimaryKey(lossollmaterialDto.getLosIId());
		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
					"");
		}

		if (losDto.getTMaterialvollstaendig() != null) {
			ArrayList al = new ArrayList();
			al.add(losDto.getCNr());
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_MATERIAL_VOLLSTAENDIG, al,
					new Exception("FEHLER_FERTIGUNG_MATERIAL_VOLLSTAENDIG"
							+ " Los:" + losDto.getCNr()));
		}

		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT, "");
		}
		try {
			Lossollmaterial lossollmaterial = em.find(Lossollmaterial.class,
					iId);

			if (!lossollmaterial.getArtikelIId().equals(
					lossollmaterialDto.getArtikelIId())) {
				// Wenn Update auf Artikel, dann Position loeschen und neu
				// anlegen
				removeLossollmaterial(lossollmaterialDto, theClientDto);

				lossollmaterialDto = createLossollmaterial(lossollmaterialDto,
						theClientDto);

				lossollmaterial = em.find(Lossollmaterial.class,
						lossollmaterialDto.getIId());

			}

			if (lossollmaterial == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
			}

			if (getMandantFac().hatZusatzfunktionberechtigung(
					MandantFac.ZUSATZFUNKTION_GERAETESERIENNUMMERN,
					theClientDto)) {
				if (losDto.getStuecklisteIId() != null) {

					// PJ 16622

					Artikel artikel = em.find(Artikel.class,
							lossollmaterialDto.getArtikelIId());
					if (Helper.short2boolean(artikel.getBSeriennrtragend())) {

						BigDecimal ssg = lossollmaterialDto.getNMenge().divide(
								losDto.getNLosgroesse(), 4,
								BigDecimal.ROUND_HALF_UP);

						if (ssg.doubleValue() != 1) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_POSITIONSMENGE_EINES_SNR_ARTIKELS_MUSS_1_SEIN_WENN_GERAETESNR,
									new Exception(
											"FEHLER_POSITIONSMENGE_EINES_SNR_ARTIKELS_MUSS_1_SEIN_WENN_GERAETESNR"));

						}

					}

				}
			}
			lossollmaterialDto.setTAendern(new Timestamp(System
					.currentTimeMillis()));
			lossollmaterialDto.setPersonalIIdAendern(theClientDto
					.getIDPersonal());
			setLossollmaterialFromLossollmaterialDto(lossollmaterial,
					lossollmaterialDto, theClientDto);
			// angelegt -> reservierung updaten
			if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKey(
								lossollmaterial.getArtikelIId(), theClientDto);
				java.sql.Date dTermin;
				if (lossollmaterial.getNMenge().compareTo(new BigDecimal(0)) > 0) {
					// Positive Reservierung: produktionsstart
					dTermin = losDto.getTProduktionsbeginn();
				} else {
					// Negative Reservierung: produktionsende
					dTermin = losDto.getTProduktionsende();
				}
				updateReservierung(artikelDto, lossollmaterialDto.getIId(),
						lossollmaterialDto.getNMenge(), new java.sql.Timestamp(
								dTermin.getTime()));
			}
			// ausgegeben -> fehlmenge aktualisieren
			else {
				getFehlmengeFac().aktualisiereFehlmenge(LocaleFac.BELEGART_LOS,
						lossollmaterialDto.getIId(), false, theClientDto);
			}
			return lossollmaterialDto;
			// }
			// catch (FinderException ex) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, ex);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	public LossollmaterialDto lossollmaterialFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		// try {
		Lossollmaterial lossollmaterial = em.find(Lossollmaterial.class, iId);
		if (lossollmaterial == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLossollmaterialDto(lossollmaterial);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public LostechnikerDto lostechnikerFindByPrimaryKey(Integer iId) {

		Lostechniker lostechniker = em.find(Lostechniker.class, iId);
		if (lostechniker == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLostechnikerDto(lostechniker);

	}

	public LossollmaterialDto lossollmaterialFindByPrimaryKeyOhneExc(Integer iId)
			throws EJBExceptionLP {
		// try {
		Lossollmaterial lossollmaterial = em.find(Lossollmaterial.class, iId);
		if (lossollmaterial == null) {
			myLogger.warn("iId=" + iId);
			return null;
		}
		return assembleLossollmaterialDto(lossollmaterial);
		// }
		// catch (FinderException ex) {
		// myLogger.warn("iId=" + iId, ex);
		// return null;
		// }
	}

	public LossollarbeitsplanDto lossollarbeitsplanFindByPrimaryKeyOhneExc(
			Integer iId) {
		Lossollarbeitsplan lossollarbeitsplan = em.find(
				Lossollarbeitsplan.class, iId);
		if (lossollarbeitsplan == null) {
			return null;
		}
		return assembleLossollarbeitsplanDto(lossollarbeitsplan);

	}

	public LossollmaterialDto[] lossollmaterialFindByLosIId(Integer losIId)
			throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("LossollmaterialfindByLosIId");
		query.setParameter(1, losIId);
		// @todo getSingleResult oder getResultList ?
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		return assembleLossollmaterialDtos(cl);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	public LossollmaterialDto[] lossollmaterialFindByLossollmaterialIIdOriginal(
			Integer lossollmaterialIId) {
		Query query = em
				.createNamedQuery("LossollmaterialfindByLossollmaterialIIdOriginal");
		query.setParameter(1, lossollmaterialIId);
		Collection<?> cl = query.getResultList();
		return assembleLossollmaterialDtos(cl);
	}

	public LossollmaterialDto[] lossollmaterialFindByLosIIdOrderByISort(
			Integer losIId) throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("LossollmaterialfindByLosIIdOrderByISort");
		query.setParameter(1, losIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		return assembleLossollmaterialDtos(cl);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	public LossollmaterialDto getErstesLossollmaterial(Integer losIId)
			throws EJBExceptionLP {
		LossollmaterialDto[] dtos = lossollmaterialFindByLosIIdOrderByISort(losIId);
		if (dtos.length > 0) {
			return dtos[0];
		} else {
			return null;
		}
	}

	private void setLossollmaterialFromLossollmaterialDto(
			Lossollmaterial lossollmaterial,
			LossollmaterialDto lossollmaterialDto, TheClientDto theClientDto) {
		try {
			// rounddto: vor dem update
			lossollmaterialDto.round(
					new Integer(4),
					getMandantFac().getNachkommastellenPreisAllgemein(
							theClientDto.getMandant()));
			lossollmaterial.setLosIId(lossollmaterialDto.getLosIId());
			lossollmaterial.setArtikelIId(lossollmaterialDto.getArtikelIId());
			lossollmaterial.setNMenge(lossollmaterialDto.getNMenge());
			lossollmaterial.setEinheitCNr(lossollmaterialDto.getEinheitCNr());
			lossollmaterial.setFDimension1(lossollmaterialDto.getFDimension1());
			lossollmaterial.setFDimension2(lossollmaterialDto.getFDimension2());
			lossollmaterial.setFDimension3(lossollmaterialDto.getFDimension3());
			lossollmaterial.setCPosition(lossollmaterialDto.getCPosition());
			lossollmaterial.setCKommentar(lossollmaterialDto.getCKommentar());
			lossollmaterial.setMontageartIId(lossollmaterialDto
					.getMontageartIId());
			lossollmaterial.setILfdnummer(lossollmaterialDto.getILfdnummer());
			lossollmaterial.setISort(lossollmaterialDto.getISort());
			lossollmaterial.setBNachtraeglich(lossollmaterialDto
					.getBNachtraeglich());
			lossollmaterial.setTAendern(lossollmaterialDto.getTAendern());
			lossollmaterial.setPersonalIIdAendern(lossollmaterialDto
					.getPersonalIIdAendern());
			lossollmaterial.setNSollpreis(lossollmaterialDto.getNSollpreis());
			lossollmaterial.setLossollmaterialIIdOriginal(lossollmaterialDto
					.getLossollmaterialIIdOriginal());
			lossollmaterial.setIBeginnterminoffset(lossollmaterialDto
					.getIBeginnterminoffset());
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		em.merge(lossollmaterial);
		em.flush();
	}

	private LossollmaterialDto assembleLossollmaterialDto(
			Lossollmaterial lossollmaterial) {
		return LossollmaterialDtoAssembler.createDto(lossollmaterial);
	}

	private LostechnikerDto assembleLostechnikerDto(Lostechniker lostechniker) {
		return LostechnikerDtoAssembler.createDto(lostechniker);
	}

	private LossollmaterialDto[] assembleLossollmaterialDtos(
			Collection<?> lossollmaterials) {
		List<LossollmaterialDto> list = new ArrayList<LossollmaterialDto>();
		if (lossollmaterials != null) {
			Iterator<?> iterator = lossollmaterials.iterator();
			while (iterator.hasNext()) {
				Lossollmaterial lossollmaterial = (Lossollmaterial) iterator
						.next();
				list.add(assembleLossollmaterialDto(lossollmaterial));
			}
		}
		LossollmaterialDto[] returnArray = new LossollmaterialDto[list.size()];
		return (LossollmaterialDto[]) list.toArray(returnArray);
	}

	public LossollarbeitsplanDto createLossollarbeitsplan(
			LossollarbeitsplanDto lossollarbeitsplanDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(lossollarbeitsplanDto);
		// begin
		lossollarbeitsplanDto.setPersonalIIdAendern(theClientDto
				.getIDPersonal());
		// primary key
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(
				PKConst.PK_LOSSOLLARBEITSPLAN);
		lossollarbeitsplanDto.setIId(iId);
		// nachtraeglich ?
		if (lossollarbeitsplanDto.getBNachtraeglich() == null) {
			lossollarbeitsplanDto.setBNachtraeglich(Helper.boolean2Short(true));
		}

		// Gesamtzeit berechnen
		LosDto losDto = losFindByPrimaryKey(lossollarbeitsplanDto.getLosIId());

		lossollarbeitsplanDto.setNGesamtzeit(Helper
				.berechneGesamtzeitInStunden(
						lossollarbeitsplanDto.getLRuestzeit(),
						lossollarbeitsplanDto.getLStueckzeit(),
						losDto.getNLosgroesse(), null,
						lossollarbeitsplanDto.getIAufspannung()));

		// PJ 16396

		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
				lossollarbeitsplanDto.getArtikelIIdTaetigkeit(), theClientDto);
		// PJ 16851
		if (Helper.short2boolean(artikelDto.getbReinemannzeit()) == false) {

			if (lossollarbeitsplanDto.getAgartCNr() != null) {
				Query query = em
						.createNamedQuery("LossollarbeitsplanfindByLosIIdIArbeitsgangnummer");
				query.setParameter(1, lossollarbeitsplanDto.getLosIId());
				query.setParameter(2,
						lossollarbeitsplanDto.getIArbeitsgangnummer());
				Collection<?> cl = query.getResultList();
				Iterator it = cl.iterator();
				while (it.hasNext()) {
					Lossollarbeitsplan ap = (Lossollarbeitsplan) it.next();
					if (ap.getAgartCNr() == null) {
						lossollarbeitsplanDto.setMaschineIId(ap
								.getMaschineIId());
						break;
					}
				}
			}
		}

		try {
			Lossollarbeitsplan lossollarbeitsplan = new Lossollarbeitsplan(
					lossollarbeitsplanDto.getIId(),
					lossollarbeitsplanDto.getLosIId(),
					lossollarbeitsplanDto.getArtikelIIdTaetigkeit(),
					lossollarbeitsplanDto.getLRuestzeit(),
					lossollarbeitsplanDto.getLStueckzeit(),
					lossollarbeitsplanDto.getNGesamtzeit(),
					lossollarbeitsplanDto.getIArbeitsgangnummer(),
					lossollarbeitsplanDto.getPersonalIIdAendern(),
					lossollarbeitsplanDto.getBNachtraeglich(),
					lossollarbeitsplanDto.getBFertig(),
					lossollarbeitsplanDto.getBAutoendebeigeht(),
					lossollarbeitsplanDto.getBNurmaschinenzeit());
			em.persist(lossollarbeitsplan);
			em.flush();
			lossollarbeitsplanDto.setTAendern(lossollarbeitsplan.getTAendern());
			setLossollarbeitsplanFromLossollarbeitsplanDto(lossollarbeitsplan,
					lossollarbeitsplanDto);
			// und retour
			return lossollarbeitsplanDto;
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeLossollarbeitsplan(
			LossollarbeitsplanDto lossollarbeitsplanDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(lossollarbeitsplanDto);
		// begin
		// if (Helper.short2boolean(lossollarbeitsplanDto.getBNachtraeglich())
		// == false) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_FERTIGUNG_POSITION_AUS_ARBEITSPLAN_DARF_NICHT_GELOESCHT_WERDEN,
		// null);
		// }
		// try {
		if (lossollarbeitsplanDto != null) {
			Integer iId = lossollarbeitsplanDto.getIId();
			Lossollarbeitsplan toRemove = em
					.find(Lossollarbeitsplan.class, iId);
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						er);
			}
		}
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
	}

	public LossollarbeitsplanDto updateLossollarbeitsplan(
			LossollarbeitsplanDto lossollarbeitsplanDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Integer iId = lossollarbeitsplanDto.getIId();
		// try {
		Lossollarbeitsplan lossollarbeitsplan = em.find(
				Lossollarbeitsplan.class, iId);
		if (lossollarbeitsplan == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}
		if (lossollarbeitsplanDto.getIMaschinenversatztage() != null) {

			int iVorher = 0;

			if (lossollarbeitsplan.getIMaschinenversatztage() != null) {
				iVorher = lossollarbeitsplan.getIMaschinenversatztage();
			}

			if (iVorher != lossollarbeitsplanDto.getIMaschinenversatztage()) {

				int delta = lossollarbeitsplanDto.getIMaschinenversatztage()
						- iVorher;

				if (delta != 0) {
					LossollarbeitsplanDto[] saDtos = lossollarbeitsplanFindByLosIId(lossollarbeitsplanDto
							.getLosIId());

					for (int i = 0; i < saDtos.length; i++) {
						if (lossollarbeitsplanDto.getIId().equals(
								saDtos[i].getIId())) {

							for (int j = i + 1; j < saDtos.length; j++) {
								Lossollarbeitsplan lossollarbeitsplanTemp = em
										.find(Lossollarbeitsplan.class,
												saDtos[j].getIId());

								if (lossollarbeitsplanTemp
										.getIMaschinenversatztage() != null) {
									lossollarbeitsplanTemp
											.setIMaschinenversatztage(lossollarbeitsplanTemp
													.getIMaschinenversatztage()
													+ delta);

								} else {
									lossollarbeitsplanTemp
											.setIMaschinenversatztage(delta);
								}
								em.merge(lossollarbeitsplanTemp);
								em.flush();
							}
							break;
						}
					}
				}
			}

		}

		lossollarbeitsplanDto.setTAendern(new Timestamp(System
				.currentTimeMillis()));
		lossollarbeitsplanDto.setPersonalIIdAendern(theClientDto
				.getIDPersonal());
		// Gesamtzeit berechnen
		LosDto losDto = losFindByPrimaryKey(lossollarbeitsplanDto.getLosIId());

		lossollarbeitsplanDto.setNGesamtzeit(Helper
				.berechneGesamtzeitInStunden(
						lossollarbeitsplanDto.getLRuestzeit(),
						lossollarbeitsplanDto.getLStueckzeit(),
						losDto.getNLosgroesse(), null,
						lossollarbeitsplanDto.getIAufspannung()));

		// speichern
		setLossollarbeitsplanFromLossollarbeitsplanDto(lossollarbeitsplan,
				lossollarbeitsplanDto);

		// PJ 16396
		if (lossollarbeitsplanDto.getMaschineIId() != null
				&& lossollarbeitsplanDto.getAgartCNr() == null
				&& lossollarbeitsplanDto.getIArbeitsgangnummer() != 0) {

			ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(
							lossollarbeitsplanDto.getArtikelIIdTaetigkeit(),
							theClientDto);
			// PJ 16851
			if (Helper.short2boolean(artikelDto.getbReinemannzeit()) == false) {

				Query query = em
						.createNamedQuery("LossollarbeitsplanfindByLosIIdIArbeitsgangnummer");
				query.setParameter(1, lossollarbeitsplanDto.getLosIId());
				query.setParameter(2,
						lossollarbeitsplanDto.getIArbeitsgangnummer());
				Collection<?> cl = query.getResultList();
				Iterator it = cl.iterator();
				while (it.hasNext()) {
					Lossollarbeitsplan ap = (Lossollarbeitsplan) it.next();

					if (!ap.getIId().equals(lossollarbeitsplanDto.getIId())) {

						ArtikelDto artikelDtoPos = getArtikelFac()
								.artikelFindByPrimaryKeySmall(
										ap.getArtikelIIdTaetigkeit(),
										theClientDto);
						if (Helper.short2boolean(artikelDtoPos
								.getbReinemannzeit()) == false) {
							ap.setMaschineIId(lossollarbeitsplanDto
									.getMaschineIId());
						}
					}
				}
			}
		}

		return lossollarbeitsplanDto;
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, ex);
		// }
	}

	public LossollarbeitsplanDto lossollarbeitsplanFindByPrimaryKey(Integer iId) {
		// try {
		Lossollarbeitsplan lossollarbeitsplan = em.find(
				Lossollarbeitsplan.class, iId);
		if (lossollarbeitsplan == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		return assembleLossollarbeitsplanDto(lossollarbeitsplan);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public LossollarbeitsplanDto[] lossollarbeitsplanFindByLosIId(Integer losIId)
			throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("LossollarbeitsplanfindByLosIId");
		query.setParameter(1, losIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		return assembleLossollarbeitsplanDtos(cl);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public LossollarbeitsplanDto[] lossollarbeitsplanFindByLosIIdArtikelIIdTaetigkeit(
			Integer losIId, Integer artikelIIdTaetigkeit) throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("LossollarbeitsplanfindByLosIIdArtikelIIdTaetigkeit");
		query.setParameter(1, losIId);
		query.setParameter(2, artikelIIdTaetigkeit);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		return assembleLossollarbeitsplanDtos(cl);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public LossollarbeitsplanDto lossollarbeitsplanfindByLosIIdIArbeitsgangnummerNaechsterHauptarbeitsgang(
			Integer losIId, Integer iArbeitsgangnummer) {

		Query query = em
				.createNamedQuery("LossollarbeitsplanfindByLosIIdIArbeitsgangnummerNaechsterHauptarbeitsgang");
		query.setParameter(1, losIId);
		query.setParameter(2, iArbeitsgangnummer);
		Collection<?> cl = query.getResultList();

		if (cl.size() > 0) {
			return assembleLossollarbeitsplanDto((Lossollarbeitsplan) cl
					.iterator().next());
		} else {
			return null;
		}

	}

	public void toggleMaterialVollstaendig(Integer losIId,
			TheClientDto theClientDto) {
		Los los = em.find(Los.class, losIId);
		if (los.getTMaterialvollstaendig() == null) {
			los.setTMaterialvollstaendig(new Timestamp(System
					.currentTimeMillis()));
			los.setPersonalIIdMaterialvollstaendig(theClientDto.getIDPersonal());

		} else {
			los.setPersonalIIdMaterialvollstaendig(null);
			los.setTMaterialvollstaendig(null);

		}
		em.merge(los);
		em.flush();
	}

	private void setLossollarbeitsplanFromLossollarbeitsplanDto(
			Lossollarbeitsplan lossollarbeitsplan,
			LossollarbeitsplanDto lossollarbeitsplanDto) {
		lossollarbeitsplan.setLosIId(lossollarbeitsplanDto.getLosIId());
		lossollarbeitsplan.setArtikelIIdTaetigkeit(lossollarbeitsplanDto
				.getArtikelIIdTaetigkeit());
		lossollarbeitsplan.setLRuestzeit(lossollarbeitsplanDto.getLRuestzeit());
		lossollarbeitsplan.setLStueckzeit(lossollarbeitsplanDto
				.getLStueckzeit());
		lossollarbeitsplan.setNGesamtzeit(lossollarbeitsplanDto
				.getNGesamtzeit());
		lossollarbeitsplan.setIArbeitsgangnummer(lossollarbeitsplanDto
				.getIArbeitsgangnummer());
		lossollarbeitsplan.setPersonalIIdAendern(lossollarbeitsplanDto
				.getPersonalIIdAendern());
		lossollarbeitsplan.setPersonalIIdZugeordneter(lossollarbeitsplanDto
				.getPersonalIIdZugeordneter());
		lossollarbeitsplan.setTAendern(lossollarbeitsplanDto.getTAendern());
		lossollarbeitsplan.setXText(lossollarbeitsplanDto.getXText());
		lossollarbeitsplan.setCKomentar(lossollarbeitsplanDto.getCKomentar());
		lossollarbeitsplan.setBNachtraeglich(lossollarbeitsplanDto
				.getBNachtraeglich());
		lossollarbeitsplan.setMaschineIId(lossollarbeitsplanDto
				.getMaschineIId());
		lossollarbeitsplan.setBFertig(lossollarbeitsplanDto.getBFertig());
		lossollarbeitsplan.setIAufspannung(lossollarbeitsplanDto
				.getIAufspannung());
		lossollarbeitsplan.setIUnterarbeitsgang(lossollarbeitsplanDto
				.getIUnterarbeitsgang());
		lossollarbeitsplan.setIMaschinenversatztage(lossollarbeitsplanDto
				.getIMaschinenversatztage());
		lossollarbeitsplan.setBAutoendebeigeht(lossollarbeitsplanDto
				.getBAutoendebeigeht());
		lossollarbeitsplan.setBNurmaschinenzeit(lossollarbeitsplanDto
				.getBNurmaschinenzeit());
		lossollarbeitsplan.setAgartCNr(lossollarbeitsplanDto.getAgartCNr());
		lossollarbeitsplan.setLossollmaterialIId(lossollarbeitsplanDto
				.getLossollmaterialIId());
		lossollarbeitsplan.setIMaschinenversatzMs(lossollarbeitsplanDto
				.getIMaschinenversatzMs());
		em.merge(lossollarbeitsplan);
		em.flush();
	}

	private LossollarbeitsplanDto assembleLossollarbeitsplanDto(
			Lossollarbeitsplan lossollarbeitsplan) {
		return LossollarbeitsplanDtoAssembler.createDto(lossollarbeitsplan);
	}

	private LossollarbeitsplanDto[] assembleLossollarbeitsplanDtos(
			Collection<?> lossollarbeitsplans) {
		List<LossollarbeitsplanDto> list = new ArrayList<LossollarbeitsplanDto>();
		if (lossollarbeitsplans != null) {
			Iterator<?> iterator = lossollarbeitsplans.iterator();
			while (iterator.hasNext()) {
				Lossollarbeitsplan lossollarbeitsplan = (Lossollarbeitsplan) iterator
						.next();
				list.add(assembleLossollarbeitsplanDto(lossollarbeitsplan));
			}
		}
		LossollarbeitsplanDto[] returnArray = new LossollarbeitsplanDto[list
				.size()];
		return (LossollarbeitsplanDto[]) list.toArray(returnArray);
	}

	public LoslagerentnahmeDto createLoslagerentnahme(
			LoslagerentnahmeDto loslagerentnahmeDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.logData(loslagerentnahmeDto);
		// begin
		loslagerentnahmeDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		// primary key
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(
				PKConst.PK_LOSSOLLMATERIAL);
		loslagerentnahmeDto.setIId(iId);

		Integer i = null;
		try {
			Query querynext = em
					.createNamedQuery("LoslagerentnahmeejbSelectNextReihung");
			querynext.setParameter(1, loslagerentnahmeDto.getLosIId());
			i = (Integer) querynext.getSingleResult();

			if (i == null) {
				i = new Integer(0);
			}
			i = new Integer(i.intValue() + 1);

		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
		loslagerentnahmeDto.setISort(i);

		try {
			Loslagerentnahme loslagerentnahme = new Loslagerentnahme(
					loslagerentnahmeDto.getIId(),
					loslagerentnahmeDto.getLosIId(),
					loslagerentnahmeDto.getLagerIId(),
					loslagerentnahmeDto.getISort(),
					loslagerentnahmeDto.getPersonalIIdAendern());
			em.persist(loslagerentnahme);
			em.flush();
			loslagerentnahmeDto.setTAendern(loslagerentnahme.getTAendern());
			setLoslagerentnahmeFromLoslagerentnahmeDto(loslagerentnahme,
					loslagerentnahmeDto);
			return loslagerentnahmeDto;
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeLoslagerentnahme(LoslagerentnahmeDto loslagerentnahmeDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(loslagerentnahmeDto);
		// begin
		// try {
		Query query = em.createNamedQuery("LoslagerentnahmefindByLosIId");
		query.setParameter(1, loslagerentnahmeDto.getLosIId());
		Collection<?> c = query.getResultList();
		// if (c.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		// es muss mindestens ein lager zugeordnet sein
		if (c.size() > 1) {
			if (loslagerentnahmeDto != null) {
				Integer iId = loslagerentnahmeDto.getIId();
				Loslagerentnahme toRemove = em
						.find(Loslagerentnahme.class, iId);
				if (toRemove == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
				}
				try {
					em.remove(toRemove);
					em.flush();
				} catch (EntityExistsException er) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
				}
			}
		} else {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_LAGERENTNAHME_DARF_NICHT_GELOESCHT_WERDEN,
					"");
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
	}

	public LoslagerentnahmeDto updateLoslagerentnahme(
			LoslagerentnahmeDto loslagerentnahmeDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Integer iId = loslagerentnahmeDto.getIId();
		// try {
		Loslagerentnahme loslagerentnahme = em
				.find(Loslagerentnahme.class, iId);
		if (loslagerentnahme == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}
		setLoslagerentnahmeFromLoslagerentnahmeDto(loslagerentnahme,
				loslagerentnahmeDto);
		return loslagerentnahmeDto;
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, ex);
		// }
	}

	public LoslagerentnahmeDto loslagerentnahmeFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		// try {
		Loslagerentnahme loslagerentnahme = em
				.find(Loslagerentnahme.class, iId);
		if (loslagerentnahme == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLoslagerentnahmeDto(loslagerentnahme);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public LoslagerentnahmeDto[] loslagerentnahmeFindByLosIId(Integer losIId)
			throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("LoslagerentnahmefindByLosIId");
		query.setParameter(1, losIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,"");
		// }
		return assembleLoslagerentnahmeDtos(cl);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	private void setLoslagerentnahmeFromLoslagerentnahmeDto(
			Loslagerentnahme loslagerentnahme,
			LoslagerentnahmeDto loslagerentnahmeDto) {
		loslagerentnahme.setLosIId(loslagerentnahmeDto.getLosIId());
		loslagerentnahme.setLagerIId(loslagerentnahmeDto.getLagerIId());
		loslagerentnahme.setISort(loslagerentnahmeDto.getISort());
		loslagerentnahme.setTAendern(loslagerentnahmeDto.getTAendern());
		loslagerentnahme.setPersonalIIdAendern(loslagerentnahmeDto
				.getPersonalIIdAendern());
		em.merge(loslagerentnahme);
		em.flush();
	}

	private LoslagerentnahmeDto assembleLoslagerentnahmeDto(
			Loslagerentnahme loslagerentnahme) {
		return LoslagerentnahmeDtoAssembler.createDto(loslagerentnahme);
	}

	private LoslagerentnahmeDto[] assembleLoslagerentnahmeDtos(
			Collection<?> loslagerentnahmes) {
		List<LoslagerentnahmeDto> list = new ArrayList<LoslagerentnahmeDto>();
		if (loslagerentnahmes != null) {
			Iterator<?> iterator = loslagerentnahmes.iterator();
			while (iterator.hasNext()) {
				Loslagerentnahme loslagerentnahme = (Loslagerentnahme) iterator
						.next();
				list.add(assembleLoslagerentnahmeDto(loslagerentnahme));
			}
		}
		LoslagerentnahmeDto[] returnArray = new LoslagerentnahmeDto[list.size()];
		return (LoslagerentnahmeDto[]) list.toArray(returnArray);
	}

	public BigDecimal wievileTOPSArtikelWurdenBereitsZugebucht(Integer losIId,
			TheClientDto theClientDto) {
		LossollmaterialDto[] dtos = lossollmaterialFindByLosIId(losIId);
		try {
			LosDto losDto = losFindByPrimaryKey(losIId);

			for (int i = 0; i < dtos.length; i++) {
				Integer artklaIId = null;

				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(dtos[i].getArtikelIId(),
								theClientDto);
				artklaIId = artikelDto.getArtklaIId();

				if (artklaIId != null) {
					boolean bTops = Helper.short2boolean(getArtikelFac()
							.artklaFindByPrimaryKey(artklaIId, theClientDto)
							.getBTops());

					if (bTops == true) {

						BigDecimal sollsatzgroesse = dtos[i].getNMenge()
								.divide(losDto.getNLosgroesse(), 4,
										BigDecimal.ROUND_HALF_EVEN);
						BigDecimal ausgegeben = getAusgegebeneMenge(
								dtos[i].getIId(), null, theClientDto);
						return ausgegeben.divide(sollsatzgroesse, 4,
								BigDecimal.ROUND_HALF_EVEN);
					}
				}
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return new BigDecimal(0);
	}

	private void verknuepfungZuBestellpositionUndArbeitsplanLoeschen(
			Integer lossollmaterialIId) {
		Query queryBestpos = em
				.createNamedQuery("BestellpositionfindByLossollmaterialIId");
		queryBestpos.setParameter(1, lossollmaterialIId);
		Collection<?> cl = queryBestpos.getResultList();

		Iterator itBest = cl.iterator();
		while (itBest.hasNext()) {
			Bestellposition b = (Bestellposition) itBest.next();
			b.setLossollmaterialIId(null);
		}

		Query queryAP = em
				.createNamedQuery("LossollarbeitsplanfindByLossollmaterialIId");
		queryAP.setParameter(1, lossollmaterialIId);
		Collection<?> clAP = queryAP.getResultList();

		Iterator itAP = clAP.iterator();
		while (itAP.hasNext()) {
			Lossollarbeitsplan b = (Lossollarbeitsplan) itAP.next();
			b.setLossollmaterialIId(null);
		}

	}

	// PJ17321
	public String verknuepfungZuBestellpositionUndArbeitsplanDarstellen(
			Integer lossollmaterialIId, TheClientDto theClientDto) {
		byte[] CRLFAscii = { 13, 10 };
		String s = "";
		Query queryBestpos = em
				.createNamedQuery("BestellpositionfindByLossollmaterialIId");
		queryBestpos.setParameter(1, lossollmaterialIId);
		Collection<?> cl = queryBestpos.getResultList();

		Iterator itBest = cl.iterator();
		while (itBest.hasNext()) {
			Bestellposition b = (Bestellposition) itBest.next();

			Bestellung bestellung = em.find(Bestellung.class,
					b.getBestellungIId());

			s += LocaleFac.BELEGART_BESTELLUNG.trim()
					+ " "
					+ bestellung.getCNr()
					+ ", PosNr: "
					+ getBestellpositionFac().getPositionNummer(b.getIId(),
							theClientDto) + new String(CRLFAscii);

		}

		Query queryAP = em
				.createNamedQuery("LossollarbeitsplanfindByLossollmaterialIId");
		queryAP.setParameter(1, lossollmaterialIId);
		Collection<?> clAP = queryAP.getResultList();

		Iterator itAP = clAP.iterator();
		while (itAP.hasNext()) {
			Lossollarbeitsplan b = (Lossollarbeitsplan) itAP.next();
			Los los = em.find(Los.class, b.getLosIId());

			s += "Los " + los.getCNr() + ", AG: " + b.getIArbeitsgangnummer();

			if (b.getIUnterarbeitsgang() != null) {
				s += ", UAG: " + b.getIUnterarbeitsgang();
			}
			s += new String(CRLFAscii);
		}
		if (!s.equals("")) {
			s = new String(CRLFAscii) + s;
		}

		return s;

	}

	private TreeMap<String, Object[]> add2TreeMap(TreeMap<String, Object[]> tm,
			String key, Object[] zeile) {

		if (tm.containsKey(key)) {
			Object[] zeileVorhanden = tm.get(key);
			BigDecimal bdMenge = (BigDecimal) zeileVorhanden[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_AUSGABEMENGE];
			bdMenge = bdMenge
					.add((BigDecimal) zeile[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_AUSGABEMENGE]);
			zeileVorhanden[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_AUSGABEMENGE] = bdMenge;

			BigDecimal bdSollMenge = (BigDecimal) zeileVorhanden[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_SOLLMENGE];
			bdSollMenge = bdSollMenge
					.add((BigDecimal) zeile[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_SOLLMENGE]);
			zeileVorhanden[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_SOLLMENGE] = bdSollMenge;
			if (bdMenge.doubleValue() == 0 && bdSollMenge.doubleValue() == 0) {
				tm.remove(key);
			} else {
				tm.put(key, zeileVorhanden);
			}

		} else {
			tm.put(key, zeile);
		}

		return tm;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public ArrayList<Integer> erledigteLoseImZeitraumNachkalkulieren(
			java.sql.Date tVon, java.sql.Date tBis, TheClientDto theClientDto) {
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();
		Criteria c = session.createCriteria(FLRLosablieferung.class);

		c.add(Restrictions.ge(FertigungFac.FLR_LOSABLIEFERUNG_T_AENDERN, tVon));

		c.add(Restrictions.lt(FertigungFac.FLR_LOSABLIEFERUNG_T_AENDERN, tBis));

		List<?> results = c.list();
		ArrayList<Integer> al = new ArrayList<Integer>();

		Iterator<?> resultListIterator = results.iterator();
		HashMap bereitsNachkalkuliert = new HashMap();

		while (resultListIterator.hasNext()) {
			FLRLosablieferung los = (FLRLosablieferung) resultListIterator
					.next();

			if (!bereitsNachkalkuliert.containsKey(los.getLos_i_id())) {
				try {
					getFertigungFac()
							.aktualisiereNachtraeglichPreiseAllerLosablieferungen(
									los.getLos_i_id(), theClientDto, true);
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}
			bereitsNachkalkuliert.put(los.getLos_i_id(), "");

		}

		return al;
	}

	@TransactionTimeout(5000)
	public TreeMap<String, Object[]> aktualisiereLoseAusStueckliste(
			Integer stuecklisteIId, TheClientDto theClientDto) {

		TreeMap<String, Object[]> tmAktualisierteLose = new TreeMap<String, Object[]>();

		ParametermandantDto parameter = null;
		try {
			parameter = getParameterFac()
					.getMandantparameter(
							theClientDto.getSMandantenwaehrung(),
							ParameterFac.KATEGORIE_STUECKLISTE,
							ParameterFac.PARAMETER_BEI_LOS_AKTUALISIERUNG_MATERIAL_NACHBUCHEN);
		} catch (RemoteException e1) {
			throwEJBExceptionLPRespectOld(e1);
		}
		boolean bMaterialNachbuchen = (Boolean) parameter.getCWertAsObject();

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT l FROM FLRLosReport l WHERE l.status_c_nr IN ('"
				+ LocaleFac.STATUS_ANGELEGT
				+ "','"
				+ LocaleFac.STATUS_AUSGEGEBEN
				+ "','"
				+ LocaleFac.STATUS_IN_PRODUKTION
				+ "') AND l.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' AND l.stueckliste_i_id IS NOT NULL ";

		if (stuecklisteIId != null) {
			queryString += " AND l.stueckliste_i_id=" + stuecklisteIId;
		}

		org.hibernate.Query query = session.createQuery(queryString);
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			FLRLosReport los = (FLRLosReport) resultListIterator.next();

			LosDto losDto = losFindByPrimaryKey(los.getI_id());
			// PJ18389
			if (losDto.getTMaterialvollstaendig() != null) {
				ArrayList al = new ArrayList();
				al.add(losDto.getCNr());
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FERTIGUNG_MATERIAL_VOLLSTAENDIG,
						al, new Exception(
								"FEHLER_FERTIGUNG_MATERIAL_VOLLSTAENDIG"
										+ " Los:" + losDto.getCNr()));
			}

			if (los.getStatus_c_nr().equals(LocaleFac.STATUS_ANGELEGT)) {
				aktualisiereSollArbeitsplanAusStueckliste(los.getI_id(),
						theClientDto);
				aktualisiereSollMaterialAusStueckliste(los.getI_id(),
						theClientDto);
			} else {

				LoslagerentnahmeDto[] laeger = loslagerentnahmeFindByLosIId(los
						.getI_id());

				aktualisiereSollArbeitsplanAusStueckliste(los.getI_id(),
						theClientDto);

				// Alle stuecklistenpositionen holen + Hilfsstueckliste und dann
				// verdichten
				HashMap<Integer, StuecklistepositionDto> hmStuecklistenposition = getFertigungFac()
						.holeAlleLossollmaterialFuerStuecklistenAktualisierung(
								los.getStueckliste_i_id(),
								los.getN_losgroesse(), 0, null, theClientDto);

				LossollmaterialDto[] sollmatDtos = lossollmaterialFindByLosIId(los
						.getI_id());

				for (int i = 0; i < sollmatDtos.length; i++) {
					try {
						if (!Helper.short2boolean(sollmatDtos[i]
								.getBNachtraeglich())) {
							// War vor SP2000 --> &&
							// sollmatDtos[i].getNMenge().doubleValue() > 0

							LosistmaterialDto[] istmaterialDtos = losistmaterialFindByLossollmaterialIId(sollmatDtos[i]
									.getIId());
							BigDecimal bdAusgegeben = getAusgegebeneMenge(
									sollmatDtos[i].getIId(), null, theClientDto);
							ArtikelDto artikelDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(
											sollmatDtos[i].getArtikelIId(),
											theClientDto);

							Object[] oZeileReport = new Object[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_ANZAHL_SPALTEN];
							oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_LOSNUMMER] = los
									.getC_nr();
							oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_ARTIKELNUMMER] = artikelDto
									.getCNr();
							oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_EINHEIT] = artikelDto
									.getEinheitCNr();
							oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_SOLLMENGE] = sollmatDtos[i]
									.getNMenge();
							if (artikelDto.getArtikelsprDto() != null) {
								oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_BEZEICHNUNG] = artikelDto
										.getArtikelsprDto().getCBez();
								oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_ZUSATZBEZEICHNUNG] = artikelDto
										.getArtikelsprDto().getCZbez();
							}

							if (hmStuecklistenposition
									.containsKey(sollmatDtos[i].getArtikelIId())) {

								// Mengen abziehen
								StuecklistepositionDto stklPos = hmStuecklistenposition
										.get(sollmatDtos[i].getArtikelIId());

								sollmatDtos[i].setNMenge(stklPos.getNMenge());

								Lossollmaterial sollmatBean = em.find(
										Lossollmaterial.class,
										sollmatDtos[i].getIId());

								BigDecimal diffSollmenge = stklPos.getNMenge()
										.subtract(sollmatBean.getNMenge());

								oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_SOLLMENGE] = diffSollmenge;

								sollmatBean.setNMenge(stklPos.getNMenge());
								sollmatBean.setIBeginnterminoffset(stklPos
										.getIBeginnterminoffset());

								BigDecimal diff = bdAusgegeben
										.subtract(sollmatDtos[i].getNMenge());

								if (diff.doubleValue() == 0
										&& diffSollmenge.doubleValue() != 0) {
									oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_AUSGABEMENGE] = new BigDecimal(
											0);
									tmAktualisierteLose = add2TreeMap(
											tmAktualisierteLose, los.getC_nr()
													+ artikelDto.getCNr(),
											oZeileReport);

								} else {

									if (bMaterialNachbuchen == true
											|| (bMaterialNachbuchen == false && Helper
													.short2boolean(artikelDto
															.getBLagerbewirtschaftet()) == false)) {

										if (Helper.short2boolean(artikelDto
												.getBSeriennrtragend())
												|| Helper
														.short2boolean(artikelDto
																.getBChargennrtragend())) {
											sollmatDtos[i] = null;

											oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_AUSGABEMENGE] = new BigDecimal(
													0);
											oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_BEMERKUNG] = "Der Artikel ist SNR/CNR behaftet und wurde nicht ber\u00FCcksichtigt.";

											tmAktualisierteLose = add2TreeMap(
													tmAktualisierteLose,
													los.getC_nr()
															+ artikelDto
																	.getCNr(),
													oZeileReport);

											continue;
										}

										if (diff.doubleValue() > 0) {

											for (int j = 0; j < istmaterialDtos.length; j++) {
												if (diff.doubleValue() > 0) {
													BigDecimal istmenge = istmaterialDtos[j]
															.getNMenge();

													BigDecimal bdMengeNeu = null;

													if (diff.doubleValue() > istmenge
															.doubleValue()) {
														bdMengeNeu = new BigDecimal(
																0);
														diff = diff
																.subtract(istmenge);
													} else {
														bdMengeNeu = istmenge
																.subtract(diff);
														diff = new BigDecimal(0);
													}

													updateLosistmaterialMenge(
															istmaterialDtos[j]
																	.getIId(),
															bdMengeNeu,
															theClientDto);
												}
											}

											BigDecimal bdAusgegebenNachher = getAusgegebeneMenge(
													sollmatDtos[i].getIId(),
													null, theClientDto);

											oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_AUSGABEMENGE] = bdAusgegebenNachher
													.subtract(bdAusgegeben);

											tmAktualisierteLose = add2TreeMap(
													tmAktualisierteLose,
													los.getC_nr()
															+ artikelDto
																	.getCNr(),
													oZeileReport);
										} else {
											BigDecimal bdAbzubuchendeMenge = diff
													.abs();
											for (int j = 0; j < laeger.length; j++) {
												// wenn noch was abzubuchen ist
												// (Menge >
												// 0)
												if (bdAbzubuchendeMenge
														.compareTo(new BigDecimal(
																0)) == 1) {
													BigDecimal bdLagerstand = null;
													if (Helper
															.short2boolean(artikelDto
																	.getBLagerbewirtschaftet())) {

														bdLagerstand = getLagerFac()
																.getLagerstand(
																		artikelDto
																				.getIId(),
																		laeger[j]
																				.getLagerIId(),
																		theClientDto);

													} else {
														bdLagerstand = new BigDecimal(
																999999999);
													}
													// wenn ein lagerstand da
													// ist
													if (bdLagerstand
															.compareTo(new BigDecimal(
																	0)) == 1) {
														BigDecimal bdMengeVonLager;
														if (bdLagerstand
																.compareTo(bdAbzubuchendeMenge) == 1) {
															// wenn mehr als
															// ausreichend
															// auf
															// lager
															bdMengeVonLager = bdAbzubuchendeMenge;
														} else {
															// dann nur den
															// lagerstand
															// entnehmen
															bdMengeVonLager = bdLagerstand;
														}
														LosistmaterialDto istmat = new LosistmaterialDto();
														istmat.setLagerIId(laeger[j]
																.getLagerIId());
														istmat.setLossollmaterialIId(sollmatDtos[i]
																.getIId());
														istmat.setNMenge(bdMengeVonLager);

														if (sollmatDtos[i]
																.getNMenge()
																.doubleValue() > 0) {
															istmat.setBAbgang(Helper
																	.boolean2Short(true));
														} else {
															istmat.setBAbgang(Helper
																	.boolean2Short(false));
														}

														// ist-wert anlegen und
														// lagerbuchung
														// durchfuehren
														createLosistmaterial(
																istmat, null,
																theClientDto);
														// menge reduzieren
														bdAbzubuchendeMenge = bdAbzubuchendeMenge
																.subtract(bdMengeVonLager);
														oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_AUSGABEMENGE] = bdMengeVonLager;

														tmAktualisierteLose = add2TreeMap(
																tmAktualisierteLose,
																los.getC_nr()
																		+ artikelDto
																				.getCNr(),
																oZeileReport);

													} else {
														oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_AUSGABEMENGE] = new BigDecimal(
																0);
														tmAktualisierteLose = add2TreeMap(
																tmAktualisierteLose,
																los.getC_nr()
																		+ artikelDto
																				.getCNr(),
																oZeileReport);
													}
												}
											}
										}
									}
								}
								hmStuecklistenposition.remove(sollmatDtos[i]
										.getArtikelIId());
								getFehlmengeFac().aktualisiereFehlmenge(
										LocaleFac.BELEGART_LOS,
										sollmatDtos[i].getIId(), false,
										theClientDto);
							} else {

								// Los-Sollmaterial loeschen

								verknuepfungZuBestellpositionUndArbeitsplanLoeschen(sollmatDtos[i]
										.getIId());

								oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_SOLLMENGE] = sollmatDtos[i]
										.getNMenge().multiply(
												new BigDecimal(-1));

								if (bMaterialNachbuchen == true
										|| istmaterialDtos.length == 0
										|| (bMaterialNachbuchen == false && Helper
												.short2boolean(artikelDto
														.getBLagerbewirtschaftet()) == false)) {
									for (int j = 0; j < istmaterialDtos.length; j++) {
										removeLosistmaterial(
												istmaterialDtos[j],
												theClientDto);
									}

									removeLossollmaterial(sollmatDtos[i],
											theClientDto);

									oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_AUSGABEMENGE] = bdAusgegeben
											.multiply(new BigDecimal(-1));
									tmAktualisierteLose = add2TreeMap(
											tmAktualisierteLose, los.getC_nr()
													+ artikelDto.getCNr(),
											oZeileReport);

								} else {
									sollmatDtos[i].setNMenge(new BigDecimal(0));
									updateLossollmaterial(sollmatDtos[i],
											theClientDto);
									oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_AUSGABEMENGE] = new BigDecimal(
											0);
									tmAktualisierteLose = add2TreeMap(
											tmAktualisierteLose, los.getC_nr()
													+ artikelDto.getCNr(),
											oZeileReport);
								}

								getFehlmengeFac().aktualisiereFehlmenge(
										LocaleFac.BELEGART_LOS,
										sollmatDtos[i].getIId(), false,
										theClientDto);
								sollmatDtos[i] = null;

							}

						}
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

				}

				// Nun alle uebrigen Sollmaterial=0 loeschen
				for (int i = 0; i < sollmatDtos.length; i++) {
					if (sollmatDtos[i] != null
							&& !Helper.short2boolean(sollmatDtos[i]
									.getBNachtraeglich())
							&& sollmatDtos[i].getNMenge().doubleValue() == 0) {

						// Los-Sollmaterial loeschen
						LosistmaterialDto[] istmaterialDtos = losistmaterialFindByLossollmaterialIId(sollmatDtos[i]
								.getIId());

						boolean bAlleIstMaterialSindNull = true;

						for (int z = 0; z < istmaterialDtos.length; z++) {
							if (istmaterialDtos[z].getNMenge().doubleValue() != 0) {
								bAlleIstMaterialSindNull = false;
							}
						}
						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(
										sollmatDtos[i].getArtikelIId(),
										theClientDto);
						if (bMaterialNachbuchen == true
								|| bAlleIstMaterialSindNull == true
								|| (bMaterialNachbuchen == false && Helper
										.short2boolean(artikelDto
												.getBLagerbewirtschaftet()) == false)) {

							BigDecimal bdAusgegeben = getAusgegebeneMenge(
									sollmatDtos[i].getIId(), null, theClientDto);

							for (int j = 0; j < istmaterialDtos.length; j++) {
								removeLosistmaterial(istmaterialDtos[j],
										theClientDto);
							}

							verknuepfungZuBestellpositionUndArbeitsplanLoeschen(sollmatDtos[i]
									.getIId());
							removeLossollmaterial(sollmatDtos[i], theClientDto);

							if (bdAusgegeben.doubleValue() != 0) {

								Object[] oZeileReport = new Object[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_ANZAHL_SPALTEN];
								oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_LOSNUMMER] = los
										.getC_nr();
								oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_ARTIKELNUMMER] = artikelDto
										.getCNr();
								oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_EINHEIT] = artikelDto
										.getEinheitCNr();
								if (artikelDto.getArtikelsprDto() != null) {
									oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_BEZEICHNUNG] = artikelDto
											.getArtikelsprDto().getCBez();
									oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_ZUSATZBEZEICHNUNG] = artikelDto
											.getArtikelsprDto().getCZbez();
								}
								oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_AUSGABEMENGE] = bdAusgegeben
										.multiply(new BigDecimal(-1));

								oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_SOLLMENGE] = new BigDecimal(
										0);

								tmAktualisierteLose = add2TreeMap(
										tmAktualisierteLose, los.getC_nr()
												+ artikelDto.getCNr(),
										oZeileReport);

							}
						}

					}
				}

				// Alle noch nicht verbrauchten neu eintragen

				Iterator it = hmStuecklistenposition.keySet().iterator();
				while (it.hasNext()) {

					try {
						LagerDto lagerDtoMandant = getLagerFac()
								.getHauptlagerDesMandanten(theClientDto);
						Integer artikelIId = (Integer) it.next();

						StuecklistepositionDto stklPos = hmStuecklistenposition
								.get(artikelIId);
						BigDecimal bdAbzubuchendeMenge = stklPos.getNMenge();

						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(
										stklPos.getArtikelIId(), theClientDto);

						Object[] oZeileReport = new Object[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_ANZAHL_SPALTEN];
						oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_LOSNUMMER] = los
								.getC_nr();
						oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_ARTIKELNUMMER] = artikelDto
								.getCNr();
						oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_EINHEIT] = artikelDto
								.getEinheitCNr();
						oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_SOLLMENGE] = stklPos
								.getNMenge();

						if (artikelDto.getArtikelsprDto() != null) {
							oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_BEZEICHNUNG] = artikelDto
									.getArtikelsprDto().getCBez();
							oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_ZUSATZBEZEICHNUNG] = artikelDto
									.getArtikelsprDto().getCZbez();
						}

						LossollmaterialDto losMatDto = new LossollmaterialDto();
						losMatDto.setArtikelIId(stklPos.getArtikelIId());
						losMatDto
								.setBNachtraeglich(Helper.boolean2Short(false));
						losMatDto.setCKommentar(stklPos.getCKommentar());
						losMatDto.setCPosition(stklPos.getCPosition());
						losMatDto.setFDimension1(stklPos.getFDimension1());
						losMatDto.setFDimension2(stklPos.getFDimension2());
						losMatDto.setFDimension3(stklPos.getFDimension3());
						losMatDto.setILfdnummer(stklPos.getILfdnummer());
						losMatDto.setEinheitCNr(stklPos.getEinheitCNr());
						losMatDto.setLosIId(los.getI_id());
						losMatDto.setMontageartIId(stklPos.getMontageartIId());
						losMatDto.setNMenge(stklPos.getNMenge());
						losMatDto.setISort(new Integer(0));

						BigDecimal bdSollpreis = getLagerFac()
								.getGemittelterGestehungspreisEinesLagers(
										stklPos.getArtikelIId(),
										lagerDtoMandant.getIId(), theClientDto);
						losMatDto.setNSollpreis(bdSollpreis);

						Integer sollmatIId = createLossollmaterial(losMatDto,
								theClientDto).getIId();
						if (bMaterialNachbuchen == true
								|| (bMaterialNachbuchen == false && Helper
										.short2boolean(artikelDto
												.getBLagerbewirtschaftet()) == false)) {

							if (Helper.short2boolean(artikelDto
									.getBSeriennrtragend())
									|| Helper.short2boolean(artikelDto
											.getBChargennrtragend())) {
								oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_BEMERKUNG] = "Der Artikel ist SNR/CNR behaftet und wurde nicht ber\u00FCcksichtigt.";

								tmAktualisierteLose.put(los.getC_nr()
										+ artikelDto.getCNr(), oZeileReport);
								continue;
							}

							for (int j = 0; j < laeger.length; j++) {
								// wenn noch was abzubuchen ist
								// (Menge >
								// 0)
								if (bdAbzubuchendeMenge
										.compareTo(new BigDecimal(0)) == 1) {
									BigDecimal bdLagerstand = null;
									if (Helper.short2boolean(artikelDto
											.getBLagerbewirtschaftet())) {

										bdLagerstand = getLagerFac()
												.getLagerstand(
														artikelDto.getIId(),
														laeger[j].getLagerIId(),
														theClientDto);

									} else {
										bdLagerstand = new BigDecimal(999999999);
									}
									// wenn ein lagerstand da ist
									if (bdLagerstand
											.compareTo(new BigDecimal(0)) == 1) {
										BigDecimal bdMengeVonLager;
										if (bdLagerstand
												.compareTo(bdAbzubuchendeMenge) == 1) {
											// wenn mehr als
											// ausreichend
											// auf
											// lager
											bdMengeVonLager = bdAbzubuchendeMenge;
										} else {
											// dann nur den
											// lagerstand
											// entnehmen
											bdMengeVonLager = bdLagerstand;
										}
										LosistmaterialDto istmat = new LosistmaterialDto();
										istmat.setLagerIId(laeger[j]
												.getLagerIId());
										istmat.setLossollmaterialIId(sollmatIId);
										istmat.setNMenge(bdMengeVonLager);

										if (stklPos.getNMenge().doubleValue() > 0) {
											istmat.setBAbgang(Helper
													.boolean2Short(true));
										} else {
											istmat.setBAbgang(Helper
													.boolean2Short(false));
										}

										// ist-wert anlegen und
										// lagerbuchung
										// durchfuehren
										createLosistmaterial(istmat, null,
												theClientDto);
										// menge reduzieren
										bdAbzubuchendeMenge = bdAbzubuchendeMenge
												.subtract(bdMengeVonLager);

										oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_AUSGABEMENGE] = bdMengeVonLager;
										tmAktualisierteLose = add2TreeMap(
												tmAktualisierteLose,
												los.getC_nr()
														+ artikelDto.getCNr(),
												oZeileReport);

									}
								}
							}
						} else {
							oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_AUSGABEMENGE] = new BigDecimal(
									0);
							tmAktualisierteLose = add2TreeMap(
									tmAktualisierteLose, los.getC_nr()
											+ artikelDto.getCNr(), oZeileReport);
						}

						getFehlmengeFac().aktualisiereFehlmenge(
								LocaleFac.BELEGART_LOS, losMatDto.getIId(),
								false, theClientDto);

					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}

				// Los aktualisieren
				Los losUpd = em.find(Los.class, los.getI_id());
				losUpd.setTAktualisierungstueckliste(getTimestamp());

			}

		}

		return tmAktualisierteLose;
	}

	public void bucheTOPSArtikelAufHauptLager(Integer losIId,
			TheClientDto theClientDto, BigDecimal zuzubuchendeSatzgroesse) {
		LossollmaterialDto[] dtos = lossollmaterialFindByLosIId(losIId);
		try {
			Integer hauptlagerIId = getLagerFac().getHauptlagerDesMandanten(
					theClientDto).getIId();

			LosDto losDto = losFindByPrimaryKey(losIId);

			for (int i = 0; i < dtos.length; i++) {
				Integer artklaIId = null;

				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(dtos[i].getArtikelIId(),
								theClientDto);
				artklaIId = artikelDto.getArtklaIId();

				if (artklaIId != null) {
					boolean bTops = Helper.short2boolean(getArtikelFac()
							.artklaFindByPrimaryKey(artklaIId, theClientDto)
							.getBTops());

					if (bTops == true) {

						BigDecimal sollsatzgroesse = dtos[i].getNMenge()
								.divide(losDto.getNLosgroesse(), 4,
										BigDecimal.ROUND_HALF_EVEN);
						BigDecimal zuzubuchendeMenge = null;

						if (zuzubuchendeSatzgroesse == null) {
							zuzubuchendeMenge = dtos[i].getNMenge().subtract(
									getAusgegebeneMenge(dtos[i].getIId(), null,
											theClientDto));
						} else {
							zuzubuchendeMenge = sollsatzgroesse
									.multiply(zuzubuchendeSatzgroesse);
						}

						if (zuzubuchendeMenge.doubleValue() > 0) {

							HandlagerbewegungDto handDto = new HandlagerbewegungDto();
							handDto.setArtikelIId(dtos[i].getArtikelIId());
							handDto.setNMenge(zuzubuchendeMenge);
							handDto.setBAbgang(Helper.boolean2Short(false));
							handDto.setCKommentar("TOPS " + losDto.getCNr());
							handDto.setLagerIId(hauptlagerIId);
							handDto.setNEinstandspreis(getLagerFac()
									.getGemittelterGestehungspreisDesHauptlagers(
											dtos[i].getArtikelIId(),
											theClientDto));
							getLagerFac().createHandlagerbewegung(handDto,
									theClientDto);
							// CK:13872
							// Dann aufs Los buchen

							LosistmaterialDto istmat = new LosistmaterialDto();
							istmat.setLagerIId(hauptlagerIId);
							istmat.setLossollmaterialIId(dtos[i].getIId());
							istmat.setNMenge(zuzubuchendeMenge);
							istmat.setBAbgang(Helper.boolean2Short(true));
							// ist-wert anlegen und lagerbuchung
							// durchfuehren
							createLosistmaterial(istmat, null, theClientDto);

							// Reservierung loeschen
							removeReservierung(artikelDto, dtos[i].getIId());
							// Fehlmenge anlegen
							getFehlmengeFac().aktualisiereFehlmenge(
									LocaleFac.BELEGART_LOS, dtos[i].getIId(),
									false, theClientDto);

						}
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public void aktualisiereSollMaterialAusStueckliste(Integer losIId,
			TheClientDto theClientDto) throws EJBExceptionLP {

		LosDto losDto = losFindByPrimaryKey(losIId);
		if (losDto.getStuecklisteIId() != null) {

			aktualisiereSollMaterialAusStueckliste(losIId, theClientDto, true);
		} else {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DARF_FUER_MATERIALLISTE_NICHT_DURCHGEFUEHRT_WERDEN,
					"");
		}

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public HashMap<Integer, StuecklistepositionDto> holeAlleLossollmaterialFuerStuecklistenAktualisierung(
			Integer stuecklisteIId, BigDecimal bdLosgroesse, int iEbene,
			HashMap<Integer, StuecklistepositionDto> hmPositionen,
			TheClientDto theClientDto) {
		iEbene++;

		if (hmPositionen == null) {
			hmPositionen = new HashMap<Integer, StuecklistepositionDto>();
		}
		try {
			StuecklistepositionDto[] stkPos = getStuecklisteFac()
					.stuecklistepositionFindByStuecklisteIId(stuecklisteIId,
							theClientDto);

			StuecklisteDto stklDto = getStuecklisteFac()
					.stuecklisteFindByPrimaryKey(stuecklisteIId, theClientDto);

			for (int i = 0; i < stkPos.length; i++) {
				// alle stuecklistenpositionen ins los uebernehmen

				// W02451
				if (stkPos[i].getArtikelIId() == 6093) {
					int u = 0;
				}

				// Einheit umrechnen
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(
								stkPos[i].getArtikelIId(), theClientDto);

				BigDecimal bdFaktor = getSystemFac().rechneUmInAndereEinheit(
						new BigDecimal(1), stkPos[i].getEinheitCNr(),
						artikelDto.getEinheitCNr(), stkPos[i].getIId(),
						theClientDto);

				// nun die Dimensionen
				BigDecimal bdDimProdukt = new BigDecimal(1);
				EinheitDto einheitDto = getSystemFac().einheitFindByPrimaryKey(
						stkPos[i].getEinheitCNr(), theClientDto);
				if (einheitDto.getIDimension().intValue() >= 1) {
					if (stkPos[i].getFDimension1() != null) {
						bdDimProdukt = bdDimProdukt.multiply(new BigDecimal(
								stkPos[i].getFDimension1().floatValue()));
					}
				}
				if (einheitDto.getIDimension().intValue() >= 2) {
					if (stkPos[i].getFDimension2() != null) {
						bdDimProdukt = bdDimProdukt.multiply(new BigDecimal(
								stkPos[i].getFDimension2().floatValue()));
					}
				}
				if (einheitDto.getIDimension().intValue() >= 3) {
					if (stkPos[i].getFDimension3() != null) {
						bdDimProdukt = bdDimProdukt.multiply(new BigDecimal(
								stkPos[i].getFDimension3().floatValue()));
					}
				}
				// verschnitt
				BigDecimal bdMenge = Helper.berechneMengeInklusiveVerschnitt(
						stkPos[i].getNMenge(),
						artikelDto.getFVerschnittfaktor(),
						artikelDto.getFVerschnittbasis(), bdLosgroesse,
						artikelDto.getFFertigungsVpe());

				// endgueltige Menge berechnen
				BigDecimal posMenge = bdMenge
						.multiply(bdDimProdukt)
						.multiply(bdLosgroesse)
						.multiply(bdFaktor)
						.divide(new BigDecimal(stklDto.getIErfassungsfaktor()
								.doubleValue()), BigDecimal.ROUND_HALF_EVEN);

				if (posMenge.doubleValue() < 0.001
						&& posMenge.doubleValue() > 0.000001) {
					posMenge = new BigDecimal("0.001");
					posMenge = posMenge.setScale(3, BigDecimal.ROUND_HALF_EVEN);
				} else {
					posMenge = posMenge.setScale(3, BigDecimal.ROUND_HALF_EVEN);
				}

				stkPos[i].setNMenge(posMenge);

				StuecklisteDto stuecklisteDto = getStuecklisteFac()
						.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
								stkPos[i].getArtikelIId(), theClientDto);

				if (stuecklisteDto != null
						&& stuecklisteDto.getStuecklisteartCNr().equals(
								StuecklisteFac.STUECKLISTEART_HILFSSTUECKLISTE)) {
					if (iEbene < 10) {
						holeAlleLossollmaterialFuerStuecklistenAktualisierung(
								stuecklisteDto.getIId(), posMenge, iEbene,
								hmPositionen, theClientDto);
					}

				} else {

					if (stkPos[i].getNMenge().doubleValue() > 0) {

						if (hmPositionen.containsKey(stkPos[i].getArtikelIId())) {

							StuecklistepositionDto p = hmPositionen
									.get(stkPos[i].getArtikelIId());
							p.setNMenge(stkPos[i].getNMenge()
									.add(p.getNMenge()));
							hmPositionen.put(stkPos[i].getArtikelIId(), p);
						} else {
							hmPositionen.put(stkPos[i].getArtikelIId(),
									stkPos[i]);
						}
					}

				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

		return hmPositionen;

	}

	public void sollpreiseAllerSollmaterialpositionenNeuKalkulieren(
			Integer losIId, TheClientDto theClientDto) {

		try {
			LossollmaterialDto[] sollDtos = lossollmaterialFindByLosIId(losIId);

			LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);

			for (int i = 0; i < sollDtos.length; i++) {

				LossollmaterialDto sollDto = sollDtos[i];
				sollDto.setNSollpreis(BigDecimal.ZERO);
				StuecklistepositionDto[] stklPosDtos = null;
				if (losDto.getStuecklisteIId() != null) {
					stklPosDtos = getStuecklisteFac()
							.stuecklistepositionFindByStuecklisteIIdArtikelIId(
									losDto.getStuecklisteIId(),
									sollDto.getArtikelIId(), theClientDto);
				}

				if (stklPosDtos != null && stklPosDtos.length > 0
						&& stklPosDtos[0].getNKalkpreis() != null) {
					sollDto.setNSollpreis(stklPosDtos[0].getNKalkpreis());
				} else {
					ArtikellieferantDto artikellieferantDto = getArtikelFac()
							.getArtikelEinkaufspreis(sollDto.getArtikelIId(),
									sollDto.getNMenge(),
									theClientDto.getSMandantenwaehrung(),
									theClientDto);
					if (artikellieferantDto != null
							&& artikellieferantDto.getLief1Preis() != null) {
						sollDto.setNSollpreis(artikellieferantDto
								.getLief1Preis());
					}
				}
				
				updateLossollmaterial(sollDto, theClientDto);
				

			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

	}

	private void erstelleLossollmaterial(Integer losIId,
			Integer stuecklisteIId, BigDecimal bdPositionsMenge,
			Integer lagerIId_Hauptlager, boolean bFlachdruecken, int iEbene,
			TheClientDto theClientDto) {
		iEbene++;
		try {
			StuecklistepositionDto[] stkPos = getStuecklisteFac()
					.stuecklistepositionFindByStuecklisteIId(stuecklisteIId,
							theClientDto);

			StuecklisteDto stklDto = getStuecklisteFac()
					.stuecklisteFindByPrimaryKey(stuecklisteIId, theClientDto);

			for (int i = 0; i < stkPos.length; i++) {
				// alle stuecklistenpositionen ins los uebernehmen

				LossollmaterialDto losMatDto = new LossollmaterialDto();
				losMatDto.setArtikelIId(stkPos[i].getArtikelIId());
				losMatDto.setBNachtraeglich(Helper.boolean2Short(false));
				losMatDto.setCKommentar(stkPos[i].getCKommentar());
				losMatDto.setCPosition(stkPos[i].getCPosition());
				losMatDto.setFDimension1(stkPos[i].getFDimension1());
				losMatDto.setFDimension2(stkPos[i].getFDimension2());
				losMatDto.setFDimension3(stkPos[i].getFDimension3());
				losMatDto.setILfdnummer(stkPos[i].getILfdnummer());
				losMatDto.setIBeginnterminoffset(stkPos[i]
						.getIBeginnterminoffset());
				losMatDto.setISort(stkPos[i].getISort());
				losMatDto.setLosIId(losIId);
				losMatDto.setMontageartIId(stkPos[i].getMontageartIId());
				// Einheit umrechnen
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKey(stkPos[i].getArtikelIId(),
								theClientDto);
				losMatDto.setEinheitCNr(artikelDto.getEinheitCNr());
				BigDecimal bdFaktor = getSystemFac().rechneUmInAndereEinheit(
						new BigDecimal(1), artikelDto.getEinheitCNr(),
						stkPos[i].getEinheitCNr(), stkPos[i].getIId(),
						theClientDto);

				// nun die Dimensionen
				BigDecimal bdDimProdukt = new BigDecimal(1);
				EinheitDto einheitDto = getSystemFac().einheitFindByPrimaryKey(
						stkPos[i].getEinheitCNr(), theClientDto);
				if (einheitDto.getIDimension().intValue() >= 1) {
					if (stkPos[i].getFDimension1() != null) {
						bdDimProdukt = bdDimProdukt.multiply(new BigDecimal(
								stkPos[i].getFDimension1().floatValue()));
					}
				}
				if (einheitDto.getIDimension().intValue() >= 2) {
					if (stkPos[i].getFDimension2() != null) {
						bdDimProdukt = bdDimProdukt.multiply(new BigDecimal(
								stkPos[i].getFDimension2().floatValue()));
					}
				}
				if (einheitDto.getIDimension().intValue() >= 3) {
					if (stkPos[i].getFDimension3() != null) {
						bdDimProdukt = bdDimProdukt.multiply(new BigDecimal(
								stkPos[i].getFDimension3().floatValue()));
					}
				}
				// verschnitt
				BigDecimal bdMenge = Helper.berechneMengeInklusiveVerschnitt(
						stkPos[i].getNMenge(),
						artikelDto.getFVerschnittfaktor(),
						artikelDto.getFVerschnittbasis(), bdPositionsMenge,
						artikelDto.getFFertigungsVpe());

				// endgueltige Menge berechnen
				if (bdFaktor.doubleValue() != 0) {

					BigDecimal losSollMenge = bdMenge
							.multiply(bdDimProdukt)
							.multiply(bdPositionsMenge)
							.divide(new BigDecimal(stklDto
									.getIErfassungsfaktor().doubleValue()),
									BigDecimal.ROUND_HALF_EVEN);
					// SP2418 nachher durch den Faktor dividieren, dan ansonsten
					// eventuell die Nachkommastellen nicht ausreichen
					losSollMenge = losSollMenge.divide(bdFaktor,
							BigDecimal.ROUND_HALF_EVEN);

					if (losSollMenge.doubleValue() < 0.001
							&& losSollMenge.doubleValue() > 0.000001) {
						losSollMenge = new BigDecimal("0.001");
						losSollMenge = losSollMenge.setScale(3,
								BigDecimal.ROUND_HALF_EVEN);
					} else {
						losSollMenge = losSollMenge.setScale(3,
								BigDecimal.ROUND_HALF_EVEN);
					}

					losMatDto.setNMenge(losSollMenge);

				}

				losMatDto.setNSollpreis(BigDecimal.ZERO);

				// PJ18903
				if (stkPos[i].getNKalkpreis() != null) {
					losMatDto.setNSollpreis(stkPos[i].getNKalkpreis());

				} else {
					ArtikellieferantDto artikellieferantDto = getArtikelFac()
							.getArtikelEinkaufspreis(losMatDto.getArtikelIId(),
									losMatDto.getNMenge(),
									theClientDto.getSMandantenwaehrung(),
									theClientDto);
					if (artikellieferantDto != null
							&& artikellieferantDto.getLief1Preis() != null) {
						losMatDto.setNSollpreis(artikellieferantDto
								.getLief1Preis());
					}
				}

				// Datensatz speichern

				// Wenn Unterstueckliste und Hilfsstueckliste:

				StuecklisteDto stuecklisteDto = getStuecklisteFac()
						.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
								stkPos[i].getArtikelIId(), theClientDto);

				if (stuecklisteDto != null
						&& stuecklisteDto.getArtikelIId() == 3376) {
					int u = 0;

				}

				if (stuecklisteDto != null
						&& stuecklisteDto.getStuecklisteartCNr().equals(
								StuecklisteFac.STUECKLISTEART_HILFSSTUECKLISTE)
						&& bFlachdruecken == true) {
					if (iEbene < 10) {
						erstelleLossollmaterial(losIId,
								stuecklisteDto.getIId(), losMatDto.getNMenge(),
								lagerIId_Hauptlager, true, iEbene, theClientDto);

					}

				} else {

					LossollmaterialDto lossollmaterialDto = createLossollmaterial(
							losMatDto, theClientDto);

					Integer iOriginal_IId = new Integer(
							lossollmaterialDto.getIId());

					// Ersatztypen anlegen
					PosersatzDto[] posersatzDtos = getStuecklisteFac()
							.posersatzFindByStuecklistepositionIId(
									stkPos[i].getIId());
					if (iEbene < 10) {
						for (int k = 0; k < posersatzDtos.length; k++) {

							losMatDto.setArtikelIId(posersatzDtos[k]
									.getArtikelIIdErsatz());
							losMatDto.setNMenge(new BigDecimal(0));
							losMatDto
									.setLossollmaterialIIdOriginal(iOriginal_IId);
							createLossollmaterial(losMatDto, theClientDto);

						}
					}
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	private void aktualisiereSollMaterialAusStueckliste(Integer losIId,
			TheClientDto theClientDto, boolean bLoescheVorherDieReservierungen)
			throws EJBExceptionLP {
		try {
			LosDto losDto = losFindByPrimaryKey(losIId);

			if (losDto.getTMaterialvollstaendig() != null) {
				ArrayList al = new ArrayList();
				al.add(losDto.getCNr());
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FERTIGUNG_MATERIAL_VOLLSTAENDIG,
						al, new Exception(
								"FEHLER_FERTIGUNG_MATERIAL_VOLLSTAENDIG"
										+ " Los:" + losDto.getCNr()));
			}

			// Aktualisierung ist nur im Status angelegt erlaubt.
			if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
				// und nur fuer stuecklistenbezogene Lose
				if (losDto.getStuecklisteIId() != null) {
					LagerDto lagerDtoMandant = getLagerFac()
							.getHauptlagerDesMandanten(theClientDto);
					// alle Positionen holen
					LossollmaterialDto[] c = lossollmaterialFindByLosIId(losIId);
					// alle nicht nachtraeglichen loeschen
					for (int i = 0; i < c.length; i++) {
						if (bLoescheVorherDieReservierungen) {
							// Reservierung loeschen
							ArtikelDto artikelDto = getArtikelFac()
									.artikelFindByPrimaryKey(
											c[i].getArtikelIId(), theClientDto);
							removeReservierung(artikelDto, c[i].getIId());
						}
						// nur die aus der stueckliste loeschen
						if (Helper.short2boolean(c[i].getBNachtraeglich()) == false) {
							Lossollmaterial toRemove = em.find(
									Lossollmaterial.class, c[i].getIId());

							if (toRemove == null) {
								throw new EJBExceptionLP(
										EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
										"");
							}

							// Vorher Sollmaterialposition aus Sollarbeitsgang
							// entfernen

							Query query = em
									.createNamedQuery("LossollarbeitsplanfindByLossollmaterialIId");
							query.setParameter(1, toRemove.getIId());

							Collection<?> cl = query.getResultList();

							if (cl != null) {
								Iterator<?> iterator = cl.iterator();
								while (iterator.hasNext()) {
									Lossollarbeitsplan lossollarbeitsplanTemp = (Lossollarbeitsplan) iterator
											.next();
									lossollarbeitsplanTemp
											.setLossollmaterialIId(null);
									em.merge(lossollarbeitsplanTemp);
									em.flush();
								}
							}

							// SP2350 Zuerst Referenz des Ersatzartikels loschen

							Query queryOrginial = em
									.createNamedQuery("LossollmaterialfindByLossollmaterialIIdOriginal");
							queryOrginial.setParameter(1, toRemove.getIId());
							Collection<?> clOriginal = queryOrginial
									.getResultList();
							Iterator itOriginal = clOriginal.iterator();
							while (itOriginal.hasNext()) {
								Lossollmaterial mat_ori = (Lossollmaterial) itOriginal
										.next();
								mat_ori.setLossollmaterialIIdOriginal(null);
								em.merge(mat_ori);
								em.flush();
							}

							// PJ SP2012/305
							Query queryAP = em
									.createNamedQuery("BestellpositionfindByLossollmaterialIId");
							queryAP.setParameter(1, toRemove.getIId());
							Collection<?> clAP = queryAP.getResultList();
							if (clAP.size() > 0) {
								toRemove.setNMenge(new BigDecimal(0));
							} else {
								try {
									em.remove(toRemove);
									em.flush();
								} catch (EntityExistsException er) {
									throw new EJBExceptionLP(
											EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
											er);
								}
							}

						}
					}
					erstelleLossollmaterial(losDto.getIId(),
							losDto.getStuecklisteIId(),
							losDto.getNLosgroesse(), lagerDtoMandant.getIId(),
							true, 0, theClientDto);

					// Reservierung anlegen
					// das in einer neuen schleife, dami die nachtraeglichen
					// auch dabei sind
					LossollmaterialDto[] sollmat = lossollmaterialFindByLosIId(losIId);
					for (int i = 0; i < sollmat.length; i++) {
						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKey(
										sollmat[i].getArtikelIId(),
										theClientDto);
						if (Helper.short2boolean(artikelDto
								.getBLagerbewirtschaftet())) {
							java.sql.Date dTermin;
							if (sollmat[i].getNMenge().compareTo(
									new BigDecimal(0)) > 0) {
								// Positive Reservierung: produktionsstart
								dTermin = losDto.getTProduktionsbeginn();

							} else {
								// Negative Reservierung: produktionsende
								dTermin = losDto.getTProduktionsende();
							}

							// PJ17994
							dTermin = Helper.addiereTageZuDatum(dTermin,
									sollmat[i].getIBeginnterminoffset());
							createReservierung(artikelDto, sollmat[i].getIId(),
									sollmat[i].getNMenge(),
									new java.sql.Timestamp(dTermin.getTime()));
						}
					}
					// Aktualisierungszeit setzen
					Los los = em.find(Los.class, losIId);
					if (los == null) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_BEI_FIND, "");
					}
					los.setTAktualisierungstueckliste(getTimestamp());
				}
			} else if (losDto.getStatusCNr().equals(
					FertigungFac.STATUS_STORNIERT)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT,
						new Exception("los " + losDto.getCNr()
								+ " ist storniert"));
			} else if (losDto.getStatusCNr().equals(
					FertigungFac.STATUS_AUSGEGEBEN)
					|| losDto.getStatusCNr().equals(
							FertigungFac.STATUS_GESTOPPT)
					|| losDto.getStatusCNr().equals(
							FertigungFac.STATUS_IN_PRODUKTION)
					|| losDto.getStatusCNr().equals(
							FertigungFac.STATUS_TEILERLEDIGT)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_AUSGEGEBEN,
						new Exception("los " + losDto.getCNr()
								+ " ist bereits ausgegeben"));
			} else if (losDto.getStatusCNr().equals(
					FertigungFac.STATUS_ERLEDIGT)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
						new Exception("los " + losDto.getCNr()
								+ " ist bereits erledigt"));
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	public LossollarbeitsplanDto[] getAlleZusatzlichZuBuchuchendenArbeitsgaenge(
			Integer lossollarbeitsplanIId, TheClientDto theClientDto) {
		List<LossollarbeitsplanDto> list = new ArrayList<LossollarbeitsplanDto>();
		// Vorhandene AG-NR holen
		Lossollarbeitsplan lossollarbeitsplan = em.find(
				Lossollarbeitsplan.class, lossollarbeitsplanIId);
		if (lossollarbeitsplan.getAgartCNr() != null
				&& lossollarbeitsplan.getAgartCNr().equals(
						StuecklisteFac.AGART_UMSPANNZEIT)) {
			Query query = em
					.createNamedQuery("LossollarbeitsplanfindByLosIIdIArbeitsgangnummer");
			query.setParameter(1, lossollarbeitsplan.getLosIId());
			query.setParameter(2, lossollarbeitsplan.getIArbeitsgangnummer());
			Collection<?> cl = query.getResultList();

			if (cl != null) {
				Iterator<?> iterator = cl.iterator();
				while (iterator.hasNext()) {
					Lossollarbeitsplan lossollarbeitsplanTemp = (Lossollarbeitsplan) iterator
							.next();

					if (!lossollarbeitsplanTemp.getIId().equals(
							lossollarbeitsplanIId)) {

						if (lossollarbeitsplanTemp.getAgartCNr() != null
								&& (lossollarbeitsplanTemp
										.getAgartCNr()
										.equals(StuecklisteFac.AGART_UMSPANNZEIT) || lossollarbeitsplanTemp
										.getAgartCNr().equals(
												StuecklisteFac.AGART_LAUFZEIT)))
							list.add(assembleLossollarbeitsplanDto(lossollarbeitsplanTemp));
					}

				}
			}

		}
		LossollarbeitsplanDto[] returnArray = new LossollarbeitsplanDto[list
				.size()];
		return (LossollarbeitsplanDto[]) list.toArray(returnArray);
	}

	public void vorhandenenArbeitsplanLoeschen(Integer losIId) {
		// alle Positionen holen
		Query query = em.createNamedQuery("LossollarbeitsplanfindByLosIId");
		query.setParameter(1, losIId);
		Collection<?> cl = query.getResultList();

		// Nun versuchen alle nicht nachtraeglichen zu loeschen
		Iterator<?> iterator = cl.iterator();
		while (iterator.hasNext()) {
			Lossollarbeitsplan lossollarbeitsplan = (Lossollarbeitsplan) iterator
					.next();

			if (Helper.short2boolean(lossollarbeitsplan.getBNachtraeglich()) == false) {

				Query q2 = em
						.createNamedQuery("LosgutschlechtFindByLossollarbeitsplanIId");
				q2.setParameter(1, lossollarbeitsplan.getIId());

				Query q3 = em
						.createNamedQuery("MaschinenzeitdatenfindByLossollarbeitsplanIId");
				q3.setParameter(1, lossollarbeitsplan.getIId());

				if (q2.getResultList().size() > 0
						|| q3.getResultList().size() > 0) {
					lossollarbeitsplan.setLRuestzeit((long) 0);
					lossollarbeitsplan.setLStueckzeit((long) 0);
					lossollarbeitsplan.setNGesamtzeit(new BigDecimal(0));
					em.merge(lossollarbeitsplan);
					em.flush();

				} else {
					em.remove(lossollarbeitsplan);
					em.flush();
				}
			}

		}
	}

	private Integer holeLossollmaterialIdAnhandStklPosition(Integer losIId,
			Integer stuecklistepositionIId, TheClientDto theClientDto) {

		try {
			StuecklistepositionDto stuecklistepositionSto = getStuecklisteFac()
					.stuecklistepositionFindByPrimaryKey(
							stuecklistepositionIId, theClientDto);

			Query query = em
					.createNamedQuery("LossollmaterialfindByLosIIdArtikelIId");
			query.setParameter(1, losIId);
			query.setParameter(2, stuecklistepositionSto.getArtikelIId());
			Collection<?> cl = query.getResultList();

			Iterator<?> iterator = cl.iterator();
			while (iterator.hasNext()) {
				Lossollmaterial lossollmaterial = (Lossollmaterial) iterator
						.next();
				return lossollmaterial.getIId();
			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return null;
	}

	public LossollmaterialDto[] lossollmaterialFindyByLosIIdArtikelIId(
			Integer losIId, Integer artikelIId, TheClientDto theClientDto) {
		Query query = em
				.createNamedQuery("LossollmaterialfindByLosIIdArtikelIId");
		query.setParameter(1, losIId);
		query.setParameter(2, artikelIId);
		Collection<?> cl = query.getResultList();
		return assembleLossollmaterialDtos(cl);
	}

	public void aktualisiereSollArbeitsplanAusStueckliste(Integer losIId,
			TheClientDto theClientDto) throws EJBExceptionLP {

		LosDto losDto = losFindByPrimaryKey(losIId);
		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)
				|| losDto.getStatusCNr().equals(FertigungFac.STATUS_AUSGEGEBEN)
				|| losDto.getStatusCNr().equals(
						FertigungFac.STATUS_IN_PRODUKTION)
				|| losDto.getStatusCNr().equals(
						FertigungFac.STATUS_TEILERLEDIGT)
				|| losDto.getStatusCNr().equals(FertigungFac.STATUS_GESTOPPT)) {

			// Darf nur fuer stuecklistenbezogene Lose durchgefuehrt werden
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stklDto = getStuecklisteFac()
						.stuecklisteFindByPrimaryKey(
								losDto.getStuecklisteIId(), theClientDto);
				getFertigungFac().vorhandenenArbeitsplanLoeschen(losIId);
				// alle Positionen holen
				Query query = em
						.createNamedQuery("LossollarbeitsplanfindByLosIId");
				query.setParameter(1, losIId);
				Collection<?> cl = query.getResultList();

				LossollarbeitsplanDto[] lossollarbeitsplanDtos = assembleLossollarbeitsplanDtos(cl);

				ArrayList<StuecklistearbeitsplanDto> alStuecklistePositionen = new ArrayList<StuecklistearbeitsplanDto>();

				// nun den gesamten Arbeitsplan der Stueckliste ins Los
				// kopieren
				StuecklistearbeitsplanDto[] stkPos = getStuecklisteFac()
						.stuecklistearbeitsplanFindByStuecklisteIId(
								losDto.getStuecklisteIId(), theClientDto);
				for (int i = 0; i < stkPos.length; i++) {
					alStuecklistePositionen.add(stkPos[i]);
				}
				// Aktualisierungszeit setzen
				Los los = em.find(Los.class, losIId);
				if (los == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
				}
				los.setTAktualisierungarbeitszeit(getTimestamp());

				// Stueckliste flachdruecken

				StuecklistepositionDto[] stklmaterialPos = getStuecklisteFac()
						.stuecklistepositionFindByStuecklisteIId(
								losDto.getStuecklisteIId(), theClientDto);
				int iArbeitsgang = 100;
				for (int j = 0; j < stklmaterialPos.length; j++) {
					StuecklisteDto unterStuecklisteDto = getStuecklisteFac()
							.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
									stklmaterialPos[j].getArtikelIId(),
									theClientDto);

					if (unterStuecklisteDto != null
							&& unterStuecklisteDto
									.getStuecklisteartCNr()
									.equals(StuecklisteFac.STUECKLISTEART_HILFSSTUECKLISTE)
							&& Helper.short2boolean(unterStuecklisteDto
									.getBFremdfertigung()) == false) {
						// nun den gesamten Arbeitsplan der Stueckliste ins
						// Los kopieren
						StuecklistearbeitsplanDto[] unterstklPos = getStuecklisteFac()
								.stuecklistearbeitsplanFindByStuecklisteIId(
										unterStuecklisteDto.getIId(),
										theClientDto);
						for (int i = 0; i < unterstklPos.length; i++) {
							iArbeitsgang++;

							unterstklPos[i].setIArbeitsgang(iArbeitsgang);
							unterstklPos[i].nMengeWennHilfsstueckliste = stklmaterialPos[j]
									.getNMenge();

							alStuecklistePositionen.add(unterstklPos[i]);

						}

					}

				}

				for (int j = 0; j < lossollarbeitsplanDtos.length; j++) {
					LossollarbeitsplanDto lossollarbeitsplanDto = lossollarbeitsplanDtos[j];

					for (int i = 0; i < alStuecklistePositionen.size(); i++) {
						// Wenn die Arbeitsgangnummer und die Artikelnummer
						// zusammenstimmen, dann wird aktualisert
						if (lossollarbeitsplanDto.getIArbeitsgangnummer()
								.equals(alStuecklistePositionen.get(i)
										.getIArbeitsgang())) {

							boolean bUnterarbeitsgangStimmtZusammen = false;

							if (lossollarbeitsplanDto.getIUnterarbeitsgang() == null
									&& alStuecklistePositionen.get(i)
											.getIUnterarbeitsgang() == null) {
								bUnterarbeitsgangStimmtZusammen = true;
							} else {
								if (lossollarbeitsplanDto
										.getIUnterarbeitsgang() != null
										&& lossollarbeitsplanDto
												.getIUnterarbeitsgang()
												.equals(alStuecklistePositionen
														.get(i)
														.getIUnterarbeitsgang())) {
									bUnterarbeitsgangStimmtZusammen = true;
								}
							}

							if (lossollarbeitsplanDto.getArtikelIIdTaetigkeit()
									.equals(alStuecklistePositionen.get(i)
											.getArtikelIId())
									&& bUnterarbeitsgangStimmtZusammen) {

								lossollarbeitsplanDto
										.setCKomentar(alStuecklistePositionen
												.get(i).getCKommentar());

								lossollarbeitsplanDto
										.setLRuestzeit(alStuecklistePositionen
												.get(i).getLRuestzeit());
								lossollarbeitsplanDto
										.setLStueckzeit(alStuecklistePositionen
												.get(i).getLStueckzeit());
								lossollarbeitsplanDto
										.setIAufspannung(alStuecklistePositionen
												.get(i).getIAufspannung());
								lossollarbeitsplanDto
										.setMaschineIId(alStuecklistePositionen
												.get(i).getMaschineIId());
								lossollarbeitsplanDto
										.setXText(alStuecklistePositionen
												.get(i).getXLangtext());
								lossollarbeitsplanDto
										.setIAufspannung(alStuecklistePositionen
												.get(i).getIAufspannung());
								lossollarbeitsplanDto
										.setBNurmaschinenzeit(alStuecklistePositionen
												.get(i).getBNurmaschinenzeit());
								lossollarbeitsplanDto
										.setAgartCNr(alStuecklistePositionen
												.get(i).getAgartCNr());
								lossollarbeitsplanDto
										.setIMaschinenversatztage(alStuecklistePositionen
												.get(i)
												.getIMaschinenversatztage());
								if (lossollarbeitsplanDto.getMaschineIId() != null) {
									MaschineDto mDto = getZeiterfassungFac()
											.maschineFindByPrimaryKey(
													lossollarbeitsplanDto
															.getMaschineIId());
									lossollarbeitsplanDto
											.setBAutoendebeigeht(mDto
													.getBAutoendebeigeht());

								}

								if (alStuecklistePositionen.get(i)
										.getStuecklistepositionIId() != null) {
									lossollarbeitsplanDto
											.setLossollmaterialIId(holeLossollmaterialIdAnhandStklPosition(
													losIId,
													alStuecklistePositionen
															.get(i)
															.getStuecklistepositionIId(),
													theClientDto));
								} else {
									lossollarbeitsplanDto
											.setLossollmaterialIId(null);
								}

								updateLossollarbeitsplan(lossollarbeitsplanDto,
										theClientDto);

								if (alStuecklistePositionen.get(i).nMengeWennHilfsstueckliste == null) {
									updateGesamtzeit(
											lossollarbeitsplanDto.getIId(),
											losDto.getNLosgroesse(),
											theClientDto);
								} else {
									updateGesamtzeit(lossollarbeitsplanDto
											.getIId(), alStuecklistePositionen
											.get(i).nMengeWennHilfsstueckliste
											.multiply(losDto.getNLosgroesse()),
											theClientDto);
								}

								alStuecklistePositionen.remove(i);
								break;

							}
						}

					}

				}

				// Alle die nicht gefunden wurden, neu eintragen
				for (int i = 0; i < alStuecklistePositionen.size(); i++) {
					LossollarbeitsplanDto losZeitDto = new LossollarbeitsplanDto();
					losZeitDto.setArtikelIIdTaetigkeit(alStuecklistePositionen
							.get(i).getArtikelIId());
					losZeitDto.setBNachtraeglich(Helper.boolean2Short(false));
					losZeitDto.setCKomentar(alStuecklistePositionen.get(i)
							.getCKommentar());
					losZeitDto.setIArbeitsgangnummer(alStuecklistePositionen
							.get(i).getIArbeitsgang());
					losZeitDto.setIUnterarbeitsgang(alStuecklistePositionen
							.get(i).getIUnterarbeitsgang());
					losZeitDto.setLosIId(losIId);
					losZeitDto.setLRuestzeit(alStuecklistePositionen.get(i)
							.getLRuestzeit());

					// SP1413
					StuecklisteDto stklDtoPosition = getStuecklisteFac()
							.stuecklisteFindByPrimaryKey(
									alStuecklistePositionen.get(i)
											.getStuecklisteIId(), theClientDto);
					if (stklDtoPosition.getIErfassungsfaktor() != 0) {
						losZeitDto.setLStueckzeit(alStuecklistePositionen
								.get(i).getLStueckzeit()
								/ stklDtoPosition.getIErfassungsfaktor());
					} else {
						losZeitDto.setLStueckzeit(alStuecklistePositionen
								.get(i).getLStueckzeit());
					}

					losZeitDto.setBNurmaschinenzeit(alStuecklistePositionen
							.get(i).getBNurmaschinenzeit());
					losZeitDto.setMaschineIId(alStuecklistePositionen.get(i)
							.getMaschineIId());
					losZeitDto.setXText(alStuecklistePositionen.get(i)
							.getXLangtext());
					losZeitDto.setIAufspannung(alStuecklistePositionen.get(i)
							.getIAufspannung());
					losZeitDto.setAgartCNr(alStuecklistePositionen.get(i)
							.getAgartCNr());
					losZeitDto.setBFertig(Helper.boolean2Short(false));

					if (alStuecklistePositionen.get(i).getMaschineIId() != null) {
						MaschineDto mDto = getZeiterfassungFac()
								.maschineFindByPrimaryKey(
										alStuecklistePositionen.get(i)
												.getMaschineIId());
						losZeitDto.setBAutoendebeigeht(mDto
								.getBAutoendebeigeht());

					} else {
						losZeitDto.setBAutoendebeigeht(Helper
								.boolean2Short(false));
					}

					if (alStuecklistePositionen.get(i)
							.getStuecklistepositionIId() != null) {
						losZeitDto
								.setLossollmaterialIId(holeLossollmaterialIdAnhandStklPosition(
										losIId, alStuecklistePositionen.get(i)
												.getStuecklistepositionIId(),
										theClientDto));
					}

					losZeitDto.setIMaschinenversatztage(alStuecklistePositionen
							.get(i).getIMaschinenversatztage());

					LossollarbeitsplanDto newDto = createLossollarbeitsplan(
							losZeitDto, theClientDto);

					if (alStuecklistePositionen.get(i).nMengeWennHilfsstueckliste == null) {
						updateGesamtzeit(newDto.getIId(),
								losDto.getNLosgroesse(), theClientDto);
					} else {
						updateGesamtzeit(
								newDto.getIId(),
								alStuecklistePositionen.get(i).nMengeWennHilfsstueckliste
										.multiply(losDto.getNLosgroesse()),
								theClientDto);
					}

				}

			} else {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FERTIGUNG_DARF_FUER_MATERIALLISTE_NICHT_DURCHGEFUEHRT_WERDEN,
						new Exception(
								"Material fuer freie Materiallisten kann nicht aktualisiert werden"));
			}
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT,
					new Exception("los " + losDto.getCNr() + " ist storniert"));
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
					new Exception("los " + losDto.getCNr()
							+ " ist bereits erledigt"));
		}

	}

	private void updateGesamtzeiten(Integer losIId, TheClientDto theClientDto) {
		myLogger.logData(losIId);
		// try {
		Query query = em.createNamedQuery("LossollarbeitsplanfindByLosIId");
		query.setParameter(1, losIId);
		Collection<?> c = query.getResultList();
		// if (c.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		LosDto los = losFindByPrimaryKey(losIId);
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			Lossollarbeitsplan item = (Lossollarbeitsplan) iter.next();
			updateGesamtzeit(item.getIId(), los.getNLosgroesse(), theClientDto);
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	private void updateGesamtzeit(Integer lossollzeitenIId,
			BigDecimal bdLosgroesse, TheClientDto theClientDto) {
		// try {
		Lossollarbeitsplan l = em.find(Lossollarbeitsplan.class,
				lossollzeitenIId);
		if (l == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
		}

		BigDecimal bdGesamtzeit = Helper.berechneGesamtzeitInStunden(
				l.getLRuestzeit(), l.getLStueckzeit(), bdLosgroesse, null,
				l.getIAufspannung());
		l.setNGesamtzeit(bdGesamtzeit);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	public void bucheMaterialAufLos(LosDto losDto, BigDecimal menge,
			boolean bHandausgabe,
			boolean bNurFehlmengenAnlegenUndReservierungenLoeschen,
			boolean bUnterstuecklistenAbbuchen, TheClientDto theClientDto,
			ArrayList<BucheSerienChnrAufLosDto> bucheSerienChnrAufLosDtos,
			boolean throwExceptionWhenCreate) {
		try {

			Query query = em.createNamedQuery("LossollmaterialfindByLosIId");
			query.setParameter(1, losDto.getIId());
			Collection<?> cl = query.getResultList();
			// if (cl.isEmpty()) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
			// }
			LossollmaterialDto[] sollmat = assembleLossollmaterialDtos(cl);
			LosablieferungDto[] dtos = losablieferungFindByLosIId(
					losDto.getIId(), false, theClientDto);
			BigDecimal bdBereitsabgeliefert = new BigDecimal(0);
			for (int i = 0; i < dtos.length; i++) {
				bdBereitsabgeliefert = bdBereitsabgeliefert.add(dtos[i]
						.getNMenge());
			}

			// PJ18216
			boolean bNichtLagerbewSofortAusgeben = false;
			try {
				ParametermandantDto parameterM = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_FERTIGUNG,
								ParameterFac.PARAMETER_NICHT_LAGERBEWIRTSCHAFTETE_SOFORT_AUSGEBEN);
				bNichtLagerbewSofortAusgeben = ((Boolean) parameterM
						.getCWertAsObject()).booleanValue();

			} catch (RemoteException ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			}

			// Laeger des Loses
			LoslagerentnahmeDto[] laeger = loslagerentnahmeFindByLosIId(losDto
					.getIId());
			// nun vom lager abbuchen
			for (int i = 0; i < sollmat.length; i++) {
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKey(sollmat[i].getArtikelIId(),
								theClientDto);
				// seriennummerntragende werden jetzt gar nicht gebucht

				if (!bNurFehlmengenAnlegenUndReservierungenLoeschen) {
					if (!bHandausgabe
							|| (bHandausgabe == true && bNichtLagerbewSofortAusgeben == true)) {

						// PJ18216
						if (bHandausgabe == true
								&& bNichtLagerbewSofortAusgeben == true
								&& Helper.short2boolean(artikelDto
										.getBLagerbewirtschaftet())) {
							// Dann nichts machen
						} else {

							StuecklisteDto stuecklisteDto = getStuecklisteFac()
									.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
											artikelDto.getIId(), theClientDto);
							if (bUnterstuecklistenAbbuchen == false
									&& stuecklisteDto != null) {
							} else {

								BigDecimal bdAbzubuchendeMenge = new BigDecimal(
										0.0000);
								if (menge == null) {
									bdAbzubuchendeMenge = sollmat[i]
											.getNMenge();
								} else {
									if (losDto.getNLosgroesse().doubleValue() != 0) {
										BigDecimal sollsatzGroesse = sollmat[i]
												.getNMenge()
												.divide(losDto.getNLosgroesse(),
														10,
														BigDecimal.ROUND_HALF_EVEN);

										sollsatzGroesse = Helper
												.rundeKaufmaennisch(
														sollsatzGroesse, 3);

										BigDecimal bdBereitsausgegeben = getAusgegebeneMenge(
												sollmat[i].getIId(), null,
												theClientDto);
										BigDecimal bdGesamtmenge = Helper
												.rundeKaufmaennisch(
														bdBereitsabgeliefert
																.add(menge)
																.multiply(
																		sollsatzGroesse),
														4);
										if (bdGesamtmenge.subtract(
												bdBereitsausgegeben)
												.doubleValue() > 0) {
											bdAbzubuchendeMenge = bdGesamtmenge
													.subtract(bdBereitsausgegeben);
										} else {
											if (bdGesamtmenge.doubleValue() < 0) {
												bdAbzubuchendeMenge = bdGesamtmenge
														.subtract(
																bdBereitsausgegeben)
														.abs();

											}
										}
									}
								}

								// wg. PJ 15370
								if (sollmat[i].getNMenge().doubleValue() > 0) {

									if (!Helper.short2boolean(artikelDto
											.getBSeriennrtragend())
											&& !Helper.short2boolean(artikelDto
													.getBChargennrtragend())) {
										for (int j = 0; j < laeger.length; j++) {
											// wenn noch was abzubuchen ist
											// (Menge >
											// 0)
											if (bdAbzubuchendeMenge
													.compareTo(new BigDecimal(0)) == 1) {
												BigDecimal bdLagerstand = null;
												if (Helper
														.short2boolean(artikelDto
																.getBLagerbewirtschaftet())) {

													// PJ18290
													boolean bImmerAusreichendVerfuegbar = false;
													try {
														ParametermandantDto parameterM = getParameterFac()
																.getMandantparameter(
																		theClientDto
																				.getMandant(),
																		ParameterFac.KATEGORIE_ARTIKEL,
																		ParameterFac.PARAMETER_LAGER_IMMER_AUSREICHEND_VERFUEGBAR);
														bImmerAusreichendVerfuegbar = ((Boolean) parameterM
																.getCWertAsObject())
																.booleanValue();

													} catch (RemoteException ex) {
														throw new EJBExceptionLP(
																EJBExceptionLP.FEHLER,
																ex);
													}
													if (bImmerAusreichendVerfuegbar == true) {
														bdLagerstand = new BigDecimal(
																999999999);
													} else {
														bdLagerstand = getLagerFac()
																.getLagerstand(
																		artikelDto
																				.getIId(),
																		laeger[j]
																				.getLagerIId(),
																		theClientDto);

													}

												} else {
													bdLagerstand = new BigDecimal(
															999999999);
												}
												// wenn ein lagerstand da ist
												if (bdLagerstand
														.compareTo(new BigDecimal(
																0)) == 1) {
													BigDecimal bdMengeVonLager;
													if (bdLagerstand
															.compareTo(bdAbzubuchendeMenge) == 1) {
														// wenn mehr als
														// ausreichend
														// auf
														// lager
														bdMengeVonLager = bdAbzubuchendeMenge;
													} else {
														// dann nur den
														// lagerstand
														// entnehmen
														bdMengeVonLager = bdLagerstand;
													}
													LosistmaterialDto istmat = new LosistmaterialDto();
													istmat.setLagerIId(laeger[j]
															.getLagerIId());
													istmat.setLossollmaterialIId(sollmat[i]
															.getIId());
													istmat.setNMenge(bdMengeVonLager);

													if (sollmat[i].getNMenge()
															.doubleValue() > 0) {
														istmat.setBAbgang(Helper
																.boolean2Short(true));
													} else {
														istmat.setBAbgang(Helper
																.boolean2Short(false));
													}

													// ist-wert anlegen und
													// lagerbuchung
													// durchfuehren
													createLosistmaterial(
															istmat, null,
															theClientDto);
													// menge reduzieren
													bdAbzubuchendeMenge = bdAbzubuchendeMenge
															.subtract(bdMengeVonLager);
												}
											}
										}

									} else {
										if (bucheSerienChnrAufLosDtos != null) {
											for (int j = 0; j < bucheSerienChnrAufLosDtos
													.size(); j++) {
												BucheSerienChnrAufLosDto dtoTemp = bucheSerienChnrAufLosDtos
														.get(j);

												if (dtoTemp
														.getLossollmaterialIId()
														.equals(sollmat[i]
																.getIId())) {

													LosistmaterialDto istmat = new LosistmaterialDto();
													istmat.setLagerIId(dtoTemp
															.getLagerIId());
													istmat.setLossollmaterialIId(dtoTemp
															.getLossollmaterialIId());
													istmat.setNMenge(dtoTemp
															.getNMenge());
													if (sollmat[i].getNMenge()
															.doubleValue() > 0) {
														istmat.setBAbgang(Helper
																.boolean2Short(true));
													} else {
														istmat.setBAbgang(Helper
																.boolean2Short(false));
													}
													// ist-wert anlegen und
													// lagerbuchung
													// durchfuehren
													createLosistmaterial(
															istmat,
															dtoTemp.getCSeriennrChargennr(),
															theClientDto);

												}

											}
										}
									}
								}
							}
						}
					}
				}

				// Reservierung loeschen
				removeReservierung(artikelDto, sollmat[i].getIId());
				// Fehlmenge anlegen
				getFehlmengeFac().aktualisiereFehlmenge(LocaleFac.BELEGART_LOS,
						sollmat[i].getIId(), throwExceptionWhenCreate,
						theClientDto);
			}

		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
		catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public String gebeMehrereLoseAus(Integer fertigungsgruppeIId,
			boolean throwExceptionWhenCreate,
			boolean bAufAktualisierungPruefen, TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();

		Criteria crit = session.createCriteria(FLRLosReport.class);

		String s = null;

		String[] stati = new String[4];
		stati[0] = FertigungFac.STATUS_ANGELEGT;
		crit.add(Restrictions.in(FertigungFac.FLR_LOS_STATUS_C_NR, stati));
		crit.add(Restrictions.eq(
				FertigungFac.FLR_LOSREPORT_FERTIGUNGSGRUPPE_I_ID,
				fertigungsgruppeIId));
		crit.add(Restrictions.eq(FertigungFac.FLR_LOS_MANDANT_C_NR,
				theClientDto.getMandant()));
		byte[] CRLFAscii = { 13, 10 };
		List<?> resultList = crit.list();
		Iterator<?> resultListIterator = resultList.iterator();
		boolean bKopfzeileEinfuegen = true;
		while (resultListIterator.hasNext()) {
			FLRLosReport flrLos = (FLRLosReport) resultListIterator.next();

			if (bAufAktualisierungPruefen) {
				boolean bGeandert = false;
				if (flrLos.getStueckliste_i_id() != null) {
					StuecklisteDto stklDto = getStuecklisteFac()
							.stuecklisteFindByPrimaryKey(
									flrLos.getStueckliste_i_id(), theClientDto);
					// ist der Arbeitsplan der Stueckliste aktueller als der des
					// Loses?
					if (flrLos.getT_aktualisierungarbeitszeit() != null
							&& stklDto.getTAendernarbeitsplan().after(
									flrLos.getT_aktualisierungarbeitszeit())) {
						bGeandert = true;
					}

					if (flrLos.getT_aktualisierungstueckliste() != null
							&& stklDto.getTAendernposition().after(
									flrLos.getT_aktualisierungstueckliste())) {
						bGeandert = true;
					}

					if (bGeandert == true) {
						if (bKopfzeileEinfuegen == true) {
							bKopfzeileEinfuegen = false;
							s = "<font size='3' face='Monospaced'>";

							s += Helper.fitString2Length("Losnummer", 12,
									'\u00A0');
							s += Helper.fitString2Length("Stkl.Nr.", 25,
									'\u00A0');
							s += Helper.fitString2Length("Stkl.Bez.", 40,
									'\u00A0');
							s += Helper.fitString2Length("Anlagedatum", 14,
									'\u00A0');
							s += Helper.fitString2Length("\u00C4nd.Dat.Stkl.",
									14, '\u00A0');

							s += Helper.fitString2Length("\u00C4nd.Dat.AP", 14,
									'\u00A0');

							s += "<br>";
							s += "<br>";

						}

						s += Helper.fitString2Length(flrLos.getC_nr(), 12,
								'\u00A0');

						s += Helper.fitString2Length(stklDto.getArtikelDto()
								.getCNr(), 25, '\u00A0');
						if (stklDto.getArtikelDto().getArtikelsprDto() != null) {
							s += Helper.fitString2Length(stklDto
									.getArtikelDto().getArtikelsprDto()
									.getCBez(), 40, '\u00A0');
						} else {
							s += Helper.fitString2Length("", 40, '\u00A0');
						}
						s += Helper.fitString2Length(
								Helper.formatDatum(flrLos.getT_anlegen(),
										theClientDto.getLocUi()), 14, '\u00A0');
						s += Helper.fitString2Length(Helper.formatDatum(
								stklDto.getTAendernposition(),
								theClientDto.getLocUi()), 14, '\u00A0');
						s += Helper.fitString2Length(Helper.formatDatum(
								stklDto.getTAendernarbeitsplan(),
								theClientDto.getLocUi()), 14, '\u00A0');
						s += "<br>";

					}
				}

			} else {
				try {
					context.getBusinessObject(FertigungFac.class).gebeLosAus(
							flrLos.getI_id(), false, throwExceptionWhenCreate,
							theClientDto, null);
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

		}
		return s;
	}

	@TransactionTimeout(6000)
	public void perAuftragsnummerLoseAbliefern(Integer auftragIId,
			TheClientDto theClientDto) {
		// Alle
		try {

			AuftragpositionDto[] dtos = getAuftragpositionFac()
					.auftragpositionFindByAuftragOffeneMenge(auftragIId);
			ArrayList<Object> alGehtNicht = new ArrayList<Object>();
			for (int i = 0; i < dtos.length; i++) {
				AuftragpositionDto auftragpositionDto = dtos[i];

				if (auftragpositionDto.getArtikelIId() != null) {
					StuecklisteDto stuecklisteDto = getStuecklisteFac()
							.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
									auftragpositionDto.getArtikelIId(),
									theClientDto);
					if (stuecklisteDto != null
							&& Helper.short2boolean(stuecklisteDto
									.getBMaterialbuchungbeiablieferung()) == true) {
						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKey(
										stuecklisteDto.getArtikelIId(),
										theClientDto);

						BigDecimal nMengeOffen = auftragpositionDto
								.getNOffeneMenge();

						Session session = FLRSessionFactory.getFactory()
								.openSession();

						Criteria crit = session
								.createCriteria(FLRLosReport.class);

						String[] stati = new String[2];
						stati[0] = FertigungFac.STATUS_IN_PRODUKTION;
						stati[1] = FertigungFac.STATUS_TEILERLEDIGT;
						crit.add(Restrictions.in(
								FertigungFac.FLR_LOS_STATUS_C_NR, stati));
						crit.add(Restrictions.eq("mandant_c_nr",
								theClientDto.getMandant()));
						crit.add(Restrictions.eq(
								FertigungFac.FLR_LOS_STUECKLISTE_I_ID,
								stuecklisteDto.getIId()));

						crit.addOrder(Order.asc("c_nr"));

						List<?> resultList = crit.list();
						Iterator<?> resultListIterator = resultList.iterator();

						while (resultListIterator.hasNext()
								&& nMengeOffen.doubleValue() > 0) {
							FLRLosReport flrLos = (FLRLosReport) resultListIterator
									.next();

							BigDecimal bdOffenLos = flrLos.getN_losgroesse()
									.subtract(
											getFertigungFac()
													.getErledigteMenge(
															flrLos.getI_id(),
															theClientDto));
							if (bdOffenLos.doubleValue() > 0) {

								boolean bErledigt = false;

								LosablieferungDto losablieferungDto = new LosablieferungDto();
								losablieferungDto.setLosIId(flrLos.getI_id());

								if (bdOffenLos.doubleValue() >= nMengeOffen
										.doubleValue()) {
									losablieferungDto.setNMenge(nMengeOffen);
									if (bdOffenLos.doubleValue() == nMengeOffen
											.doubleValue()) {
										bErledigt = true;
									}
									nMengeOffen = new BigDecimal(0);

								} else {
									losablieferungDto.setNMenge(bdOffenLos);
									nMengeOffen = nMengeOffen
											.subtract(bdOffenLos);
									bErledigt = true;
								}
								// Materialbuchen
								bucheMaterialAufLos(
										losFindByPrimaryKey(flrLos.getI_id()),
										losablieferungDto.getNMenge(), false,
										false, true, theClientDto, null, false);

								// Sind sollsatzgroeszen unterschritten?
								// Sollsatzgroessen pruefen
								pruefePositionenMitSollsatzgroesseUnterschreitung(
										flrLos.getI_id(),
										losablieferungDto.getNMenge(),
										theClientDto);

								createLosablieferung(losablieferungDto,
										theClientDto, bErledigt);

							}
						}

						if (nMengeOffen.doubleValue() > 0) {
							alGehtNicht.add(artikelDto
									.formatArtikelbezeichnung()
									+ " "
									+ Helper.formatZahl(nMengeOffen, 4,
											theClientDto.getLocUi())
									+ " "
									+ artikelDto.getEinheitCNr());
						}

					}
				}

			}

			if (alGehtNicht.size() > 0) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_ABLIEFERN_PER_AUFTRAGSNUMMER_NICHT_MOEGLICH,
						alGehtNicht, new Exception("Zuwenig offene Lose"));

			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

	}

	// PJ18741
	public TreeSet<Integer> getLoseEinesStuecklistenbaums(Integer losIId,
			TheClientDto theClientDto) {

		TreeSet<Integer> ts = new TreeSet<Integer>();
		ts.add(losIId);
		ts = getLoseEinesStuecklistenbaums(losIId, ts, theClientDto);
		return ts;
	}

	private TreeSet<Integer> getLoseEinesStuecklistenbaums(Integer losIId,
			TreeSet<Integer> ts, TheClientDto theClientDto) {

		LossollmaterialDto[] sollDtos = lossollmaterialFindByLosIId(losIId);
		for (int i = 0; i < sollDtos.length; i++) {

			StuecklisteDto stklDto = getStuecklisteFac()
					.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
							sollDtos[i].getArtikelIId(), theClientDto);
			if (stklDto != null) {

				Query query = em.createNamedQuery("LosfindByStuecklisteIId");
				query.setParameter(1, stklDto.getIId());

				Collection c = query.getResultList();

				Iterator it = c.iterator();
				while (it.hasNext()) {
					Los l = (Los) it.next();
					if (!ts.contains(l.getIId())) {
						if (l.getStatusCNr().equals(
								FertigungFac.STATUS_ANGELEGT)) {
							ts.add(l.getIId());
						}

						ts = getLoseEinesStuecklistenbaums(l.getIId(), ts,
								theClientDto);
					}
				}
			}
		}
		return ts;
	}

	@TransactionTimeout(5000)
	public void gebeLosAus(Integer losIId, boolean bHandausgabe,
			boolean throwExceptionWhenCreateFehlmenge,
			TheClientDto theClientDto,
			ArrayList<BucheSerienChnrAufLosDto> bucheSerienChnrAufLosDtos)
			throws EJBExceptionLP {
		myLogger.logData(losIId);
		if (losIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("losIId == null"));
		}
		try {
			LosDto losDto = losFindByPrimaryKey(losIId);

			ParametermandantDto parameterVorlaufzeit = getParameterFac()
					.getMandantparameter(theClientDto.getSMandantenwaehrung(),
							ParameterFac.KATEGORIE_FERTIGUNG,
							ParameterFac.PARAMETER_KUNDE_IST_PFLICHTFELD);
			boolean bKundeIstPflichtfeld = (Boolean) parameterVorlaufzeit
					.getCWertAsObject();

			if (bKundeIstPflichtfeld == true && losDto.getKundeIId() == null
					&& losDto.getAuftragIId() == null) {
				ArrayList alDaten = new ArrayList();
				alDaten.add(losDto.getCNr());
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FERTIGUNG_LOS_OHNE_KUNDE,
						alDaten, new Exception(
								"FEHLER_FERTIGUNG_LOS_OHNE_KUNDE"));

			}

			boolean bMaterialbuchungBeiAblieferung = false;
			boolean bUnterstuecklisteAusgeben = false;
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stklDto = getStuecklisteFac()
						.stuecklisteFindByPrimaryKey(
								losDto.getStuecklisteIId(), theClientDto);
				if (Helper.short2Boolean(stklDto
						.getBMaterialbuchungbeiablieferung()) == true) {
					bMaterialbuchungBeiAblieferung = true;
					throwExceptionWhenCreateFehlmenge = false;
				}
				if (Helper.short2Boolean(stklDto.getBAusgabeunterstueckliste()) == true) {
					bUnterstuecklisteAusgeben = true;
				}
			}

			ParametermandantDto parametermandantDto = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FERTIGUNG,
							ParameterFac.PARAMETER_AUSGABE_EIGENER_STATUS);
			boolean bAusgegebenEigenerStatus = (java.lang.Boolean) parametermandantDto
					.getCWertAsObject();

			if (bAusgegebenEigenerStatus == true
					&& losDto.getStatusCNr().equals(
							FertigungFac.STATUS_AUSGEGEBEN)) {
				Los los = em.find(Los.class, losIId);
				los.setStatusCNr(FertigungFac.STATUS_IN_PRODUKTION);
				return;
			}

			if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
				// Status aendern
				Los los = em.find(Los.class, losIId);
				if (los == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
				}
				if (bAusgegebenEigenerStatus == true) {
					los.setStatusCNr(FertigungFac.STATUS_AUSGEGEBEN);
				} else {
					los.setStatusCNr(FertigungFac.STATUS_IN_PRODUKTION);
				}

				los.setPersonalIIdAusgabe(theClientDto.getIDPersonal());
				los.setTAusgabe(getTimestamp());
				// die position holen

				parametermandantDto = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_FERTIGUNG,
								ParameterFac.PARAMETER_KEINE_AUTOMATISCHE_MATERIALBUCHUNG);
				boolean bKeineAutomatischeMaterialausgabe = false;
				if (((java.lang.Boolean) parametermandantDto.getCWertAsObject())
						.booleanValue() == true) {
					bKeineAutomatischeMaterialausgabe = true;
				}

				// PJ18630 Uebersteuert von Stueckliste
				if (losDto.getStuecklisteIId() != null) {
					StuecklisteDto stklDto = getStuecklisteFac()
							.stuecklisteFindByPrimaryKey(
									losDto.getStuecklisteIId(), theClientDto);

					bKeineAutomatischeMaterialausgabe = Helper
							.short2Boolean(stklDto
									.getBKeineAutomatischeMaterialbuchung());
				}

				if (bKeineAutomatischeMaterialausgabe) {
					bucheMaterialAufLos(losDto, null, true, false,
							bUnterstuecklisteAusgeben, theClientDto,
							bucheSerienChnrAufLosDtos,
							throwExceptionWhenCreateFehlmenge);
				} else {
					bucheMaterialAufLos(losDto, null,
							bMaterialbuchungBeiAblieferung, bHandausgabe,
							bUnterstuecklisteAusgeben, theClientDto,
							bucheSerienChnrAufLosDtos,
							throwExceptionWhenCreateFehlmenge);
				}

			} else if (losDto.getStatusCNr().equals(
					FertigungFac.STATUS_STORNIERT)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT,
						new Exception("los " + losDto.getCNr()
								+ " ist storniert"));
			} else if (losDto.getStatusCNr().equals(
					FertigungFac.STATUS_AUSGEGEBEN)
					|| losDto.getStatusCNr().equals(
							FertigungFac.STATUS_GESTOPPT)
					|| losDto.getStatusCNr().equals(
							FertigungFac.STATUS_IN_PRODUKTION)
					|| losDto.getStatusCNr().equals(
							FertigungFac.STATUS_TEILERLEDIGT)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_AUSGEGEBEN,
						new Exception("los " + losDto.getCNr()
								+ " ist bereits ausgegeben"));
			} else if (losDto.getStatusCNr().equals(
					FertigungFac.STATUS_ERLEDIGT)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
						new Exception("los " + losDto.getCNr()
								+ " ist bereits erledigt"));
			}

			parametermandantDto = getParameterFac().getMandantparameter(
					theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_LOS_BEI_AUSGABE_GESTOPPT);

			if (((java.lang.Boolean) parametermandantDto.getCWertAsObject()) == true
					&& bAusgegebenEigenerStatus == false) {
				stoppeProduktion(losIId, theClientDto);
			}
		}

		catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	@TransactionTimeout(5000)
	public void gebeLosAusRueckgaengig(
			Integer losIId,
			boolean bSollmengenBeiNachtraeglichenMaterialentnahmenAktualisieren,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// loggen
		myLogger.logData(losIId);
		// parameter pruefen
		if (losIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("losIId == null"));
		}
		// Los holen
		LosDto losDto = losFindByPrimaryKey(losIId);
		// Das Ruecknehmen der Ausgabe ist fuer folgende Stati nicht erlaubt:
		// Storniert, Angelegt, Erledigt
		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN,
					new Exception("los " + losDto.getCNr()
							+ " ist noch nicht ausgegeben"));
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT,
					new Exception("los " + losDto.getCNr() + " ist storniert"));
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
					new Exception("los " + losDto.getCNr()
							+ " ist bereits erledigt"));
		} else if (losDto.getStatusCNr().equals(
				FertigungFac.STATUS_TEILERLEDIGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_TEILERLEDIGT,
					new Exception("los " + losDto.getCNr()
							+ " ist bereits teilerledigt"));
		}
		try {
			// los status
			Los los = em.find(Los.class, losIId);
			if (los == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
			}

			ParametermandantDto parametermandantDto = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FERTIGUNG,
							ParameterFac.PARAMETER_AUSGABE_EIGENER_STATUS);
			boolean bAusgegebenEigenerStatus = (java.lang.Boolean) parametermandantDto
					.getCWertAsObject();

			if (bAusgegebenEigenerStatus == true
					&& losDto.getStatusCNr().equals(
							FertigungFac.STATUS_IN_PRODUKTION)) {
				los.setStatusCNr(FertigungFac.STATUS_AUSGEGEBEN);
				return;
			}

			los.setStatusCNr(FertigungFac.STATUS_ANGELEGT);
			los.setPersonalIIdAusgabe(null);
			los.setTAusgabe(null);
			// Jetzt beginnt die Ruecknahme der Ausgaben
			Query query = em.createNamedQuery("LossollmaterialfindByLosIId");
			query.setParameter(1, losIId);
			Collection<?> c = query.getResultList();
			for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
				// fuer alle Materialpositionen im Los
				Lossollmaterial sollmat = (Lossollmaterial) iter.next();
				LosistmaterialDto[] istmat = losistmaterialFindByLossollmaterialIId(sollmat
						.getIId());
				// Lagerausgaben zuruecknehmen
				BigDecimal bgAusgegeben = BigDecimal.ZERO;
				for (int i = 0; i < istmat.length; i++) {
					bgAusgegeben = bgAusgegeben.add(istmat[i].getNMenge());
					removeLosistmaterial(istmat[i], theClientDto);
				}

				// SP2821
				if (bSollmengenBeiNachtraeglichenMaterialentnahmenAktualisieren) {
					sollmat.setNMenge(bgAusgegeben);
				}

				// eventuell eingetragene Fehlmenge loeschen
				getFehlmengeFac().aktualisiereFehlmenge(LocaleFac.BELEGART_LOS,
						sollmat.getIId(), false, theClientDto);
			}
			// stueckliste aktualisieren und Reservierungen wieder herstellen
			if (los.getStuecklisteIId() != null) {
				aktualisiereSollMaterialAusStueckliste(losIId, theClientDto,
						false);
			}
			// arbeitsplan aktualisieren
			// aktualisiereSollArbeitsplanAusStueckliste(losIId, idUser);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	public void storniereLos(Integer losIId,
			boolean bAuftragspositionsbezugEntfernen, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.logData(losIId);

		if (losIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("losIId == null"));
		}

		LosDto losDto = losFindByPrimaryKey(losIId);
		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT,
					new Exception("Los " + losDto.getCNr() + " ist storniert"));
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
					new Exception("Los " + losDto.getCNr()
							+ " ist bereits erledigt"));
		} else if (losDto.getStatusCNr().equals(
				FertigungFac.STATUS_TEILERLEDIGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_TEILERLEDIGT,
					new Exception("Los " + losDto.getCNr()
							+ " ist bereits Teilerledigt"));
		} else {

			boolean bIstmaterialVorhanden = false;

			// die position holen
			Query query = em.createNamedQuery("LossollmaterialfindByLosIId");
			query.setParameter(1, losIId);
			Collection<?> cl = query.getResultList();
			LossollmaterialDto[] sollmat = assembleLossollmaterialDtos(cl);
			// Reservierungen loeschen
			for (int i = 0; i < sollmat.length; i++) {
				BigDecimal ausgegeben = getAusgegebeneMenge(
						sollmat[i].getIId(), null, theClientDto);
				if (ausgegeben.doubleValue() > 0) {
					bIstmaterialVorhanden = true;
					break;
				}
			}

			if (bIstmaterialVorhanden == false) {

				Los los = em.find(Los.class, losIId);
				if (los == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
				}
				los.setStatusCNr(FertigungFac.STATUS_STORNIERT);

				if (bAuftragspositionsbezugEntfernen == true) {
					los.setAuftragpositionIId(null);
				}
				for (int i = 0; i < sollmat.length; i++) {
					// Reservierungen loeschen
					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(
									sollmat[i].getArtikelIId(), theClientDto);
					removeReservierung(artikelDto, sollmat[i].getIId());

					// Fehlmengen loeschen
					try {
						getFehlmengeFac().aktualisiereFehlmenge(
								LocaleFac.BELEGART_LOS, sollmat[i].getIId(),
								false, theClientDto);
					} catch (RemoteException ex1) {
						throwEJBExceptionLPRespectOld(ex1);
					}

				}

			} else {
				// FEHLER-IST-MATERIAL-VORHANDEN
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FERTIGUNG_AUF_DEM_LOS_IST_MATERIAL_AUSGEGEBEN,
						new Exception("Los " + losDto.getCNr()
								+ " ist bereits Teilerledigt"));
			}

		}

	}

	public void storniereLosRueckgaengig(Integer losIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(losIId);

		if (losIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("losIId == null"));
		}
		// try {
		LosDto losDto = losFindByPrimaryKey(losIId);
		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			Los los = em.find(Los.class, losIId);
			if (los == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
			}
			los.setStatusCNr(FertigungFac.STATUS_ANGELEGT);
			// Stueckliste updaten und Material reservieren
			aktualisiereSollMaterialAusStueckliste(losIId, theClientDto, false);
			// AZ aktualisieren
			// aktualisiereSollArbeitsplanAusStueckliste(losIId, idUser);
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
			// nothing here - hat wohl in der zwischenzeit wer anderer gemacht
			// ;-)
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_AUSGEGEBEN)
				|| losDto.getStatusCNr().equals(FertigungFac.STATUS_GESTOPPT)
				|| losDto.getStatusCNr().equals(
						FertigungFac.STATUS_IN_PRODUKTION)
				|| losDto.getStatusCNr().equals(
						FertigungFac.STATUS_TEILERLEDIGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_AUSGEGEBEN,
					new Exception("los " + losDto.getCNr()
							+ " ist bereits ausgegeben"));
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
					new Exception("los " + losDto.getCNr()
							+ " ist bereits erledigt"));
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	public void manuellErledigenRueckgaengig(Integer losIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(losIId);

		if (losIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("losIId == null"));
		}
		// try {
		LosDto losDto = losFindByPrimaryKey(losIId);
		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			Los los = em.find(Los.class, losIId);
			if (los == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
			}
			Query query = em.createNamedQuery("LosablieferungfindByLosIId");
			query.setParameter(1, losIId);
			Collection<?> cl = query.getResultList();
			LosablieferungDto[] dtos = assembleLosablieferungDtos(cl);

			if (dtos.length < 1) {
				los.setStatusCNr(FertigungFac.STATUS_IN_PRODUKTION);
			} else {
				los.setStatusCNr(FertigungFac.STATUS_TEILERLEDIGT);
			}
			los.setTManuellerledigt(null);
			los.setPersonalIIdManuellerledigt(null);
			los.setTErledigt(null);
			los.setPersonalIIdErledigt(null);
			// Fehlmengen wieder eintragen
			LossollmaterialDto[] losmat = lossollmaterialFindByLosIId(los
					.getIId());
			for (int i = 0; i < losmat.length; i++) {
				try {
					getFehlmengeFac().aktualisiereFehlmenge(
							LocaleFac.BELEGART_LOS, losmat[i].getIId(), false,
							theClientDto);
				} catch (RemoteException ex1) {
					throwEJBExceptionLPRespectOld(ex1);
				}
			}

		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	public LosistmaterialDto createLosistmaterial(
			LosistmaterialDto losistmaterialDto, String sSerienChargennummer,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// begin
		// primary key
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(
				PKConst.PK_LOSISTMATERIAL);
		losistmaterialDto.setIId(iId);
		try {
			Losistmaterial losistmaterial = new Losistmaterial(
					losistmaterialDto.getIId(),
					losistmaterialDto.getLossollmaterialIId(),
					losistmaterialDto.getLagerIId(),
					losistmaterialDto.getNMenge(),
					losistmaterialDto.getBAbgang());
			em.persist(losistmaterial);
			em.flush();
			setLosistmaterialFromLosistmaterialDto(losistmaterial,
					losistmaterialDto);
			// Lagerbuchung
			LossollmaterialDto sollmatDto = lossollmaterialFindByPrimaryKey(losistmaterialDto
					.getLossollmaterialIId());
			// abbuchung mit dem aktuellen gestehungspreis
			BigDecimal bdPreis = getLagerFac()
					.getGemittelterGestehungspreisEinesLagers(
							sollmatDto.getArtikelIId(),
							losistmaterialDto.getLagerIId(), theClientDto);
			// Artikel holen
			ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(sollmatDto.getArtikelIId(),
							theClientDto);
			if (Helper.short2boolean(artikelDto.getBSeriennrtragend())) {

				String[] snr = Helper.erzeugeSeriennummernArray(
						sSerienChargennummer, losistmaterialDto.getNMenge(),
						true);

				for (int i = 0; i < snr.length; i++) {
					if (Helper.short2boolean(losistmaterialDto.getBAbgang())) {
						getLagerFac().bucheAb(
								LocaleFac.BELEGART_LOS,
								sollmatDto.getLosIId(),
								losistmaterialDto.getIId(),
								sollmatDto.getArtikelIId(),
								new BigDecimal(1),
								bdPreis,
								losistmaterialDto.getLagerIId(),
								snr[i],
								new java.sql.Timestamp(System
										.currentTimeMillis()), theClientDto);
					} else {
						getLagerFac().bucheZu(
								LocaleFac.BELEGART_LOS,
								sollmatDto.getLosIId(),
								losistmaterialDto.getIId(),
								sollmatDto.getArtikelIId(),
								new BigDecimal(1),
								bdPreis,
								losistmaterialDto.getLagerIId(),
								snr[i],
								new java.sql.Timestamp(System
										.currentTimeMillis()), theClientDto,
								null, null, true);

					}
				}
			} else {
				if (Helper.short2boolean(losistmaterialDto.getBAbgang())) {
					getLagerFac().bucheAb(LocaleFac.BELEGART_LOS,
							sollmatDto.getLosIId(), losistmaterialDto.getIId(),
							sollmatDto.getArtikelIId(),
							losistmaterialDto.getNMenge(), bdPreis,
							losistmaterialDto.getLagerIId(),
							sSerienChargennummer,
							new java.sql.Timestamp(System.currentTimeMillis()),
							theClientDto);
				} else {
					getLagerFac().bucheZu(LocaleFac.BELEGART_LOS,
							sollmatDto.getLosIId(), losistmaterialDto.getIId(),
							sollmatDto.getArtikelIId(),
							losistmaterialDto.getNMenge().abs(), bdPreis,
							losistmaterialDto.getLagerIId(),
							sSerienChargennummer,
							new java.sql.Timestamp(System.currentTimeMillis()),
							theClientDto, null, null, true);
				}
			}
			return losistmaterialDto;
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	private void removeLosistmaterial(LosistmaterialDto losistmaterialDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (losistmaterialDto != null) {
			// Lagerbuchung

			getLagerFac().loescheKompletteLagerbewegungEinerBelgposition(
					LocaleFac.BELEGART_LOS, losistmaterialDto.getIId(),
					theClientDto);

			Integer iId = losistmaterialDto.getIId();
			Losistmaterial toRemove = em.find(Losistmaterial.class, iId);
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						er);
			}
		}

	}

	public LossollarbeitsplanDto[] lossollarbeitsplanFindByLossollmaterialIId(
			Integer lossollmaterialIId, TheClientDto theClientDto) {

		LossollarbeitsplanDto[] sollapDtos = null;

		Query queryAP = em
				.createNamedQuery("LossollarbeitsplanfindByLossollmaterialIId");
		queryAP.setParameter(1, lossollmaterialIId);
		Collection<?> clAP = queryAP.getResultList();

		sollapDtos = assembleLossollarbeitsplanDtos(clAP);

		return sollapDtos;
	}

	public LosistmaterialDto losistmaterialFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		// try {
		Losistmaterial losistmaterial = em.find(Losistmaterial.class, iId);
		if (losistmaterial == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLosistmaterialDto(losistmaterial);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public LosistmaterialDto losistmaterialFindByPrimaryKeyOhneExc(Integer iId)
			throws EJBExceptionLP {
		// try {
		Losistmaterial losistmaterial = (Losistmaterial) em.find(
				Losistmaterial.class, iId);
		if (losistmaterial == null) {
			myLogger.warn("iId=" + iId);
			return null;
		}
		return assembleLosistmaterialDto(losistmaterial);
		// }
		// catch (FinderException ex) {
		// myLogger.warn("iId=" + iId, ex);
		// return null;
		// }
	}

	public LosistmaterialDto[] losistmaterialFindByLossollmaterialIId(
			Integer lossollmaterialIId) throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("LosistmaterialfindByLossollmaterialIId");
		query.setParameter(1, lossollmaterialIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		return assembleLosistmaterialDtos(cl);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	private void setLosistmaterialFromLosistmaterialDto(
			Losistmaterial losistmaterial, LosistmaterialDto losistmaterialDto) {
		losistmaterial.setLossollmaterialIId(losistmaterialDto
				.getLossollmaterialIId());
		losistmaterial.setLagerIId(losistmaterialDto.getLagerIId());
		losistmaterial.setNMenge(losistmaterialDto.getNMenge());
		em.merge(losistmaterial);
		em.flush();
	}

	private LosistmaterialDto assembleLosistmaterialDto(
			Losistmaterial losistmaterial) {
		return LosistmaterialDtoAssembler.createDto(losistmaterial);
	}

	private LosistmaterialDto[] assembleLosistmaterialDtos(
			Collection<?> losistmaterials) {
		List<LosistmaterialDto> list = new ArrayList<LosistmaterialDto>();
		if (losistmaterials != null) {
			Iterator<?> iterator = losistmaterials.iterator();
			while (iterator.hasNext()) {
				Losistmaterial losistmaterial = (Losistmaterial) iterator
						.next();
				list.add(assembleLosistmaterialDto(losistmaterial));
			}
		}
		LosistmaterialDto[] returnArray = new LosistmaterialDto[list.size()];
		return (LosistmaterialDto[]) list.toArray(returnArray);
	}

	private void createReservierung(ArtikelDto artikelDto,
			Integer iBelegartpositionid, BigDecimal bdMenge,
			Timestamp tLiefertermin) throws EJBExceptionLP {
		try {
			// nur wenn die Menge > 0 ist
			if (bdMenge.compareTo(new BigDecimal(0)) != 0) {
				ArtikelreservierungDto resDto = new ArtikelreservierungDto();
				resDto.setArtikelIId(artikelDto.getIId());
				resDto.setCBelegartnr(LocaleFac.BELEGART_LOS);
				resDto.setIBelegartpositionid(iBelegartpositionid);
				resDto.setNMenge(bdMenge);
				resDto.setTLiefertermin(tLiefertermin);
				getReservierungFac().createArtikelreservierung(resDto);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	private void updateReservierung(ArtikelDto artikelDto,
			Integer iBelegartpositionid, BigDecimal bdMenge,
			Timestamp tLiefertermin) throws EJBExceptionLP {
		try {
			if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet())) {
				if (bdMenge.compareTo(new BigDecimal(0)) != 0) {
					ArtikelreservierungDto resDto = getReservierungFac()
							.artikelreservierungFindByBelegartCNrBelegartPositionIIdOhneExc(
									LocaleFac.BELEGART_LOS, iBelegartpositionid);
					if (resDto != null) {
						// falls der artikel geaendert wurde
						resDto.setArtikelIId(artikelDto.getIId());
						resDto.setNMenge(bdMenge);
						resDto.setTLiefertermin(tLiefertermin);
						getReservierungFac().updateArtikelreservierung(resDto);
					} else {
						// ansonsten neu anlegen
						resDto = new ArtikelreservierungDto();
						resDto.setCBelegartnr(LocaleFac.BELEGART_LOS);
						resDto.setIBelegartpositionid(iBelegartpositionid);
						resDto.setArtikelIId(artikelDto.getIId());
						resDto.setNMenge(bdMenge);
						resDto.setTLiefertermin(tLiefertermin);
						getReservierungFac().createArtikelreservierung(resDto);
					}

				} else {
					// wenn menge = 0 dann loeschen
					getReservierungFac().removeArtikelreservierung(
							LocaleFac.BELEGART_LOS, iBelegartpositionid);
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	private void removeReservierung(ArtikelDto artikelDto,
			Integer iBelegartpositionid) throws EJBExceptionLP {
		try {
			if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet())) {
				// vorher schaun ob reserviert - koennte ja menge 0 gewesen sein
				if (getReservierungFac()
						.artikelreservierungFindByBelegartCNrBelegartPositionIIdOhneExc(
								LocaleFac.BELEGART_LOS, iBelegartpositionid) != null) {
					getReservierungFac().removeArtikelreservierung(
							LocaleFac.BELEGART_LOS, iBelegartpositionid);
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public String getArtikelhinweiseAllerLossollpositionen(Integer losIId,
			TheClientDto theClientDto) {

		LossollmaterialDto[] dtos = lossollmaterialFindByLosIId(losIId);
		byte[] CRLFAscii = { 13, 10 };
		String meldung = "";

		for (int i = 0; i < dtos.length; i++) {
			try {

				String[] hinweise = getArtikelkommentarFac()
						.getArtikelhinweise(dtos[i].getArtikelIId(),
								LocaleFac.BELEGART_LOS, theClientDto);

				if (hinweise != null && hinweise.length > 0) {
					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(
									dtos[i].getArtikelIId(), theClientDto);

					meldung += artikelDto.getCNr() + ": ";

					for (int j = 0; j < hinweise.length; j++) {

						meldung += Helper.strippHTML(hinweise[j])
								+ new String(CRLFAscii);

					}

					meldung += new String(CRLFAscii);

				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}
		// lossollmaterialFindByLosIId(losIId)
		return meldung;
	}

	public BigDecimal getAusgegebeneMenge(Integer lossollmaterialIId,
			java.sql.Timestamp tsStichtag, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			BigDecimal bdMenge = new BigDecimal(0);

			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = factory.openSession();

			String query = "SELECT li.i_id,li.b_abgang FROM FLRLosistmaterial li WHERE li.lossollmaterial_i_id="
					+ lossollmaterialIId;

			org.hibernate.Query qResult = session.createQuery(query);
			List<?> results = qResult.list();

			Iterator<?> resultListIterator = results.iterator();
			while (resultListIterator.hasNext()) {

				Object[] o = (Object[]) resultListIterator.next();
				BigDecimal bdAusgegeben = getLagerFac()
						.getMengeEinerBelegposition(LocaleFac.BELEGART_LOS,
								(Integer) o[0], tsStichtag);
				if (Helper.short2boolean((Short) o[1]) == true) {
					bdMenge = bdMenge.add(bdAusgegeben);
				} else {
					bdMenge = bdMenge.subtract(bdAusgegeben);
				}
			}

			/*
			 * LosistmaterialDto[] losist =
			 * losistmaterialFindByLossollmaterialIId(lossollmaterialIId); for
			 * (int i = 0; i < losist.length; i++) { BigDecimal bdAusgegeben =
			 * getLagerFac() .getMengeEinerBelegposition(LocaleFac.BELEGART_LOS,
			 * losist[i].getIId(), tsStichtag); if
			 * (Helper.short2boolean(losist[i].getBAbgang()) == true) { bdMenge
			 * = bdMenge.add(bdAusgegeben); } else { bdMenge =
			 * bdMenge.subtract(bdAusgegeben); } }
			 */
			return bdMenge;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BigDecimal getAusgegebeneMengePreis(Integer lossollmaterialIId,
			java.sql.Timestamp tStichtag, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			BigDecimal bdMenge = new BigDecimal(0);
			BigDecimal bdWert = new BigDecimal(0);
			LosistmaterialDto[] losist = losistmaterialFindByLossollmaterialIId(lossollmaterialIId);
			for (int i = 0; i < losist.length; i++) {

				BigDecimal bdAusgegeben = getLagerFac()
						.getMengeEinerBelegposition(LocaleFac.BELEGART_LOS,
								losist[i].getIId(), tStichtag);

				if (Helper.short2boolean(losist[i].getBAbgang())) {

					BigDecimal bdPreis = getLagerFac()
							.getGemittelterGestehungspreisEinerAbgangsposition(
									LocaleFac.BELEGART_LOS, losist[i].getIId());
					bdMenge = bdMenge.add(bdAusgegeben);
					bdWert = bdWert.add(bdPreis.multiply(bdAusgegeben));
				} else {
					BigDecimal bdPreis = getLagerFac()
							.getGemittelterEinstandspreisEinerZugangsposition(
									LocaleFac.BELEGART_LOS, losist[i].getIId());
					bdMenge = bdMenge.subtract(bdAusgegeben);
					bdWert = bdWert.subtract(bdPreis.multiply(bdAusgegeben));
				}
			}
			if (bdMenge.doubleValue() == 0) {
				return bdMenge;
			} else {
				return bdWert.divide(bdMenge, BigDecimal.ROUND_HALF_EVEN);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	public LoslosklasseDto createLoslosklasse(LoslosklasseDto loslosklasseDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(loslosklasseDto);

		// begin
		loslosklasseDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		// primary key

		try {
			Query query = em
					.createNamedQuery("LoslosklassefindByLosIIdLosklasseIId");
			query.setParameter(1, loslosklasseDto.getLosIId());
			query.setParameter(2, loslosklasseDto.getLosklasseIId());
			Loslosklasse doppelt = (Loslosklasse) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("FERT_LOSLOSKLASSE.UK"));
		} catch (NoResultException ex) {

		}

		Integer iId = getPKGeneratorObj().getNextPrimaryKey(
				PKConst.PK_LOSLOSKLASSE);
		loslosklasseDto.setIId(iId);
		try {
			Loslosklasse loslosklasse = new Loslosklasse(
					loslosklasseDto.getIId(), loslosklasseDto.getLosIId(),
					loslosklasseDto.getLosklasseIId(),
					loslosklasseDto.getPersonalIIdAendern());
			em.persist(loslosklasse);
			em.flush();
			loslosklasseDto.setTAendern(loslosklasse.getTAendern());
			setLoslosklasseFromLoslosklasseDto(loslosklasse, loslosklasseDto);
			return loslosklasseDto;
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeLoslosklasse(LoslosklasseDto loslosklasseDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// try {
		if (loslosklasseDto != null) {
			Integer iId = loslosklasseDto.getIId();
			Loslosklasse toRemove = em.find(Loslosklasse.class, iId);
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						er);
			}
		}
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
	}

	public LossollarbeitsplanDto[] getAlleOffenenZeiternFuerStueckrueckmeldung(
			Integer personalIId, TheClientDto theClientDto) {

		ArrayList<LossollarbeitsplanDto> alDaten = new ArrayList<LossollarbeitsplanDto>();
		HashMap ids = new HashMap();
		try {

			Integer taetigkeitIId_Kommt = getZeiterfassungFac()
					.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_KOMMT,
							theClientDto).getIId();
			Integer taetigkeitIId_Geht = getZeiterfassungFac()
					.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_GEHT,
							theClientDto).getIId();

			Session session = FLRSessionFactory.getFactory().openSession();
			org.hibernate.Criteria liste = session
					.createCriteria(FLRZeitdaten.class);
			liste.add(Expression.eq(
					ZeiterfassungFac.FLR_ZEITDATEN_PERSONAL_I_ID, personalIId));

			liste.add(Restrictions.or(Expression.eq(
					ZeiterfassungFac.FLR_ZEITDATEN_TAETIGKEIT_I_ID,
					taetigkeitIId_Kommt), Expression.eq(
					ZeiterfassungFac.FLR_ZEITDATEN_TAETIGKEIT_I_ID,
					taetigkeitIId_Geht)));
			liste.add(Expression.lt(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT,
					new Timestamp(System.currentTimeMillis())));

			liste.add(Expression.eq(
					ZeiterfassungFac.FLR_ZEITDATEN_B_AUTOMATIKBUCHUNG,
					Helper.boolean2Short(false)));

			liste.addOrder(Order.desc(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT));
			liste.setMaxResults(1);
			List<?> letztesKommt = liste.list();

			Iterator it = letztesKommt.iterator();

			Timestamp tVon = Helper.cutTimestamp(new Timestamp(System
					.currentTimeMillis()));

			if (it.hasNext()) {
				tVon = new Timestamp(((FLRZeitdaten) it.next()).getT_zeit()
						.getTime());
			}

			boolean bTheoretischeIstZeit = false;

			try {
				ParametermandantDto parameterIstZeit = (ParametermandantDto) getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_PERSONAL,
								ParameterFac.PARAMETER_THEORETISCHE_IST_ZEIT_RECHNUNG);

				bTheoretischeIstZeit = ((Boolean) parameterIstZeit
						.getCWertAsObject());

			} catch (RemoteException ex5) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
			}

			ZeitdatenDto[] dtos = getZeiterfassungFac()
					.zeitdatenFindZeitdatenEinesTagesUndEinerPerson(
							personalIId, tVon,
							new Timestamp(System.currentTimeMillis()));

			boolean bRuestenSchonVorhanden = false;

			for (int i = dtos.length - 1; i >= 0; i--) {
				if (dtos[i].getCBelegartnr() != null
						&& dtos[i].getIBelegartid() != null) {

					if (dtos[i].getCBelegartnr().equals(LocaleFac.BELEGART_LOS)
							&& dtos[i].getIBelegartpositionid() != null) {

						try {
							Lossollarbeitsplan lossollarbeitsplan = em.find(
									Lossollarbeitsplan.class,
									dtos[i].getIBelegartpositionid());
							if (lossollarbeitsplan != null) {
								if (!Helper.short2boolean(lossollarbeitsplan
										.getBNurmaschinenzeit())
										&& !Helper
												.short2boolean(lossollarbeitsplan
														.getBFertig())) {
									LossollarbeitsplanDto sollDto = assembleLossollarbeitsplanDto(lossollarbeitsplan);

									// PJ 16233
									if (bTheoretischeIstZeit) {
										if (!ids.containsKey(sollDto.getIId())) {
											// PJ 16035 Ruesten nur das letzte
											if (sollDto.getAgartCNr() == null) {
												if (bRuestenSchonVorhanden == false) {
													bRuestenSchonVorhanden = true;
													alDaten.add(sollDto);
													ids.put(sollDto.getIId(),
															sollDto.getIId());
												}
											} else {
												alDaten.add(sollDto);
												ids.put(sollDto.getIId(),
														sollDto.getIId());
											}

										}
									} else {
										alDaten.add(sollDto);
									}
								}
							}

						} catch (NoResultException fe) {
							// keiner da
						}
					}
				}
			}

			// nun nach Losnr und AG sortieren
			// Nach Fertigungsgruppe sortieren
			for (int k = alDaten.size() - 1; k > 0; --k) {
				for (int j = 0; j < k; ++j) {
					LossollarbeitsplanDto a1 = (LossollarbeitsplanDto) alDaten
							.get(j);
					LossollarbeitsplanDto a2 = (LossollarbeitsplanDto) alDaten
							.get(j + 1);

					LosDto l1 = losFindByPrimaryKey(a1.getLosIId());
					LosDto l2 = losFindByPrimaryKey(a2.getLosIId());

					String s1 = l1.getCNr();

					if (a1.getIUnterarbeitsgang() != null) {
						s1 += Helper.fitString2LengthAlignRight(
								a1.getIUnterarbeitsgang() + "", 10, ' ');
					} else {
						s1 += Helper.fitString2LengthAlignRight("", 10, ' ');
					}
					s1 += Helper.fitString2LengthAlignRight(
							a1.getIArbeitsgangnummer() + "", 10, ' ');

					String s2 = l2.getCNr();
					if (a2.getIUnterarbeitsgang() != null) {
						s2 += Helper.fitString2LengthAlignRight(
								a2.getIUnterarbeitsgang() + "", 10, ' ');
					} else {
						s2 += Helper.fitString2LengthAlignRight("", 10, ' ');
					}
					s2 += Helper.fitString2LengthAlignRight(
							a2.getIArbeitsgangnummer() + "", 10, ' ');

					if (s1.compareTo(s2) > 0) {
						alDaten.set(j, a2);
						alDaten.set(j + 1, a1);
					}
				}

			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		LossollarbeitsplanDto[] returnArray = new LossollarbeitsplanDto[alDaten
				.size()];
		return (LossollarbeitsplanDto[]) alDaten.toArray(returnArray);
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public Integer getJuengstenZusatzstatuseinesLoses(Integer losIId) {

		Integer zusatzstatusIId = null;

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT lz.zusatzstatus_i_id FROM FLRLoszusatzstatus as lz WHERE lz.los_i_id="
				+ losIId + " ORDER BY lz.t_aendern DESC";
		org.hibernate.Query query = session.createQuery(sQuery);
		query.setMaxResults(1);

		List<?> resultList = query.list();

		Iterator<?> resultListIterator = resultList.iterator();
		if (resultListIterator.hasNext()) {
			zusatzstatusIId = (Integer) resultListIterator.next();
		}
		session.close();
		return zusatzstatusIId;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public Map<Integer, String> getAllZusatzstatus(TheClientDto theClientDto) {

		TreeMap<Integer, String> tmArten = new TreeMap<Integer, String>();

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT z.i_id,z.c_bez FROM FLRZusatzstatus as z";
		org.hibernate.Query query = session.createQuery(sQuery);

		List<?> resultList = query.list();

		Iterator<?> resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {
			Object o[] = (Object[]) resultListIterator.next();
			Integer id = (Integer) o[0];
			String value = (String) o[1];

			tmArten.put(id, value);
		}
		session.close();
		return tmArten;
	}

	public LossollarbeitsplanDto[] getAlleOffenenMaschinenArbeitsplan(
			Integer maschineIId, TheClientDto theClientDto) {

		ArrayList<LossollarbeitsplanDto> alDaten = new ArrayList<LossollarbeitsplanDto>();

		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + 2);

		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT l FROM FLRLossollarbeitsplan l WHERE l.b_fertig=0 AND l.maschine_i_id="
				+ maschineIId
				+ " AND l.flrlos.status_c_nr IN ('"
				+ LocaleFac.STATUS_AUSGEGEBEN
				+ "','"
				+ LocaleFac.STATUS_IN_PRODUKTION
				+ "','"
				+ LocaleFac.STATUS_TEILERLEDIGT
				+ "') AND l.flrlos.t_produktionsbeginn<'"
				+ Helper.formatDateWithSlashes(new java.sql.Date(c
						.getTimeInMillis()))
				+ "' AND l.flrlos.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' ORDER BY l.flrlos.t_produktionsbeginn ASC , l.flrlos.c_nr ASC";

		org.hibernate.Query qResult = session.createQuery(sQuery);
		List<?> results = qResult.list();

		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			FLRLossollarbeitsplan l = (FLRLossollarbeitsplan) resultListIterator
					.next();
			alDaten.add(lossollarbeitsplanFindByPrimaryKey(l.getI_id()));
		}

		LossollarbeitsplanDto[] returnArray = new LossollarbeitsplanDto[alDaten
				.size()];
		return (LossollarbeitsplanDto[]) alDaten.toArray(returnArray);
	}

	public LoslosklasseDto updateLoslosklasse(LoslosklasseDto loslosklasseDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Integer iId = loslosklasseDto.getIId();
		// try {

		try {
			Query query = em
					.createNamedQuery("LoslosklassefindByLosIIdLosklasseIId");
			query.setParameter(1, loslosklasseDto.getLosIId());
			query.setParameter(2, loslosklasseDto.getLosklasseIId());
			Integer iIdVorhanden = ((Loslosklasse) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"FERT_LOSLOSKLASSE.UK"));
			}
		} catch (NoResultException ex) {

		}

		Loslosklasse loslosklasse = em.find(Loslosklasse.class, iId);
		if (loslosklasse == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}
		setLoslosklasseFromLoslosklasseDto(loslosklasse, loslosklasseDto);
		return loslosklasseDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
		// }
	}

	public LoslosklasseDto loslosklasseFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		// try {
		Loslosklasse loslosklasse = em.find(Loslosklasse.class, iId);
		if (loslosklasse == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLoslosklasseDto(loslosklasse);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setLoslosklasseFromLoslosklasseDto(Loslosklasse loslosklasse,
			LoslosklasseDto loslosklasseDto) {
		loslosklasse.setLosIId(loslosklasseDto.getLosIId());
		loslosklasse.setLosklasseIId(loslosklasseDto.getLosklasseIId());
		loslosklasse.setTAendern(loslosklasseDto.getTAendern());
		loslosklasse.setPersonalIIdAendern(loslosklasseDto
				.getPersonalIIdAendern());
		em.merge(loslosklasse);
		em.flush();
	}

	private LoslosklasseDto assembleLoslosklasseDto(Loslosklasse loslosklasse) {
		return LoslosklasseDtoAssembler.createDto(loslosklasse);
	}

	private LoslosklasseDto[] assembleLoslosklasseDtos(
			Collection<?> loslosklasses) {
		List<LoslosklasseDto> list = new ArrayList<LoslosklasseDto>();
		if (loslosklasses != null) {
			Iterator<?> iterator = loslosklasses.iterator();
			while (iterator.hasNext()) {
				Loslosklasse loslosklasse = (Loslosklasse) iterator.next();
				list.add(assembleLoslosklasseDto(loslosklasse));
			}
		}
		LoslosklasseDto[] returnArray = new LoslosklasseDto[list.size()];
		return (LoslosklasseDto[]) list.toArray(returnArray);
	}

	@TransactionTimeout(1000)
	public void aktualisiereNachtraeglichPreiseAllerLosablieferungen(
			Integer losIId, TheClientDto theClientDto,
			boolean bNurLetztenMaterialwertNeuBerechnen) throws EJBExceptionLP {
		LosDto losDto = losFindByPrimaryKey(losIId);

		try {
			ParametermandantDto parametermandantDto = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FERTIGUNG,
							ParameterFac.PARAMETER_ISTZEITEN_GLEICH_SOLLZEITEN);
			boolean bSollGleichIstzeiten = false;
			if (((java.lang.Boolean) parametermandantDto.getCWertAsObject())
					.booleanValue() == true) {
				bSollGleichIstzeiten = true;
			}

			// AZ-Werte aktualisieren
			if (bSollGleichIstzeiten) {
				aktualisiereAZAllerLosablieferungenWennSollIstGleichIst(losIId,
						theClientDto);
			} else {
				aktualisiereAZAllerLosablieferungen(losIId, theClientDto);
			}

			// Preise berechnen:
			// --------------------------------------------------------------
			// ---------
			// 1. der inklusive aller bisherigen ablieferungen erledigte
			// Materialwert
			if (bNurLetztenMaterialwertNeuBerechnen == false) {
				materialwertAllerLosablieferungenNeuBerechnen(losDto,
						theClientDto);
			}

			// PJ 14807
			boolean bAblieferpreisIstDurchschnitspreis = false;
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FERTIGUNG,
							ParameterFac.PARAMETER_ABLIEFERUNGSPREIS_IST_DURCHSCHNITTSPREIS);
			bAblieferpreisIstDurchschnitspreis = ((Boolean) parameter
					.getCWertAsObject());

			BigDecimal bdMaterialwertGesamt = getErledigterMaterialwertNEU(
					losDto, theClientDto);

			boolean bGesamteIstzeitenZaehlen = false;
			parameter = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FERTIGUNG,
							ParameterFac.PARAMETER_LOSABLIEFERUNG_GESAMTE_ISTZEITEN_ZAEHLEN);
			bGesamteIstzeitenZaehlen = ((Boolean) parameter.getCWertAsObject());

			// --------------------------------------------------------------
			// ---------
			// nun der detaillierte Arbeitszeitwert (mit Beruecksichtigung
			// der Unterlose)
			// 1. der inklusive aller bisherigen ablieferungen erledigte
			// Arbeitszeitwert
			BigDecimal bdArbeitszeitwertAusUnterlosenGesamt = getErledigterArbeitszeitwertAusUnterlosen(
					losDto, theClientDto);

			// PJ14807
			BigDecimal bdAzWertDurchschnitt = new BigDecimal(0);
			BigDecimal bdMatWertDurchschnitt = new BigDecimal(0);

			if (bAblieferpreisIstDurchschnitspreis == true) {
				BigDecimal bdGesamtkosten = new BigDecimal(0);
				// Maschinenzeiten
				AuftragzeitenDto[] zeitenMaschine = getZeiterfassungFac()
						.getAllMaschinenzeitenEinesBeleges(losIId, null, null,
								null, theClientDto);
				for (int j = 0; j < zeitenMaschine.length; j++) {
					bdGesamtkosten = bdGesamtkosten.add(zeitenMaschine[j]
							.getBdKosten());
				}

				// "normale" Zeiten
				AuftragzeitenDto[] zeitenMann = getZeiterfassungFac()
						.getAllZeitenEinesBeleges(LocaleFac.BELEGART_LOS,
								losIId, null, null, null, null, false, false,
								theClientDto);
				for (int j = 0; j < zeitenMann.length; j++) {
					bdGesamtkosten = bdGesamtkosten.add(zeitenMann[j]
							.getBdKosten());
				}

				bdGesamtkosten = bdGesamtkosten
						.add(bdArbeitszeitwertAusUnterlosenGesamt);

				BigDecimal bdAbgeliefertGesamt = getErledigteMenge(
						losDto.getIId(), theClientDto);
				if (bdAbgeliefertGesamt.doubleValue() > 0) {
					bdAzWertDurchschnitt = bdGesamtkosten.divide(
							bdAbgeliefertGesamt, 4, BigDecimal.ROUND_HALF_EVEN);
					bdMatWertDurchschnitt = bdMaterialwertGesamt.divide(
							bdAbgeliefertGesamt, 4, BigDecimal.ROUND_HALF_EVEN);
				}
			}

			Query query = em.createNamedQuery("LosablieferungfindByLosIId");
			query.setParameter(1, losIId);
			Collection<?> losablieferungs = query.getResultList();

			LosablieferungDto[] losabDtos = assembleLosablieferungDtosOhneSnrs(losablieferungs);

			Timestamp tAblieferzeitpunktVorherigeAblieferung = null;

			for (Iterator<?> iter = losablieferungs.iterator(); iter.hasNext();) {
				Losablieferung losablieferung = (Losablieferung) iter.next();

				// 2. bisher erledigte duerfen aber nicht mehr beruecksichtigt
				// werden -> subtrahieren
				// Diese werden durch die "Assemblierung" neu geladen (es
				// koennten sich Werte geaendert haben), das Find faellt aber
				// weg

				BigDecimal bdMaterialwert = bdMaterialwertGesamt;

				if (bAblieferpreisIstDurchschnitspreis == false) {

					if (bNurLetztenMaterialwertNeuBerechnen == true
							&& iter.hasNext() == false) {
						// Ausser die betroffene Ablieferung selbst
						for (int i = 0; i < losabDtos.length; i++) {
							if (!losablieferung.getIId().equals(
									losabDtos[i].getIId())) {
								bdMaterialwert = bdMaterialwert
										.subtract(losabDtos[i]
												.getNMaterialwert().multiply(
														losabDtos[i]
																.getNMenge()));
							}
						}
						// 3. Nach Division durch die Menge der neuen
						// Ablieferung
						// hab
						// ich den Einzelwert
						losablieferung.setNMaterialwert(bdMaterialwert.divide(
								losablieferung.getNMenge(), 4,
								BigDecimal.ROUND_HALF_EVEN));
					}
					// ----------------------------------------------------------
					// ----
					// ---------
					// Gestehungspreis: ist die Summe aus Materialwert und
					// Arbeitszeitwert
					losablieferung.setNGestehungspreis(losablieferung
							.getNMaterialwert().add(
									losablieferung.getNArbeitszeitwert()));

					BigDecimal bdArbeitszeitwertAusUnterlosen = bdArbeitszeitwertAusUnterlosenGesamt;

					// 2. bisher erledigte duerfen aber nicht mehr
					// beruecksichtigt
					// werden -> subtrahieren
					for (int i = 0; i < losabDtos.length; i++) {
						if (!losablieferung.getIId().equals(
								losabDtos[i].getIId())) {
							bdArbeitszeitwertAusUnterlosen = bdArbeitszeitwertAusUnterlosen
									.subtract(losabDtos[i]
											.getNArbeitszeitwertdetailliert()
											.multiply(losabDtos[i].getNMenge()));
						}
					}
					// 3. Nach Division durch die Menge der neuen Ablieferung
					// hab
					// ich den Einzelwert
					BigDecimal bdAZWertAusUnterlosen = bdArbeitszeitwertAusUnterlosen
							.divide(losablieferung.getNMenge(), 4,
									BigDecimal.ROUND_HALF_EVEN);
					// 4. Dazu kommt noch der AZ-Wert aus diesem Los
					losablieferung
							.setNArbeitszeitwertdetailliert(bdAZWertAusUnterlosen
									.add(losablieferung.getNArbeitszeitwert()));
					// ----------------------------------------------------------
					// ----
					// ---------
					// nun der detaillierte Materialwert
					// der ist einfach die Differenz zwischen Gestehungspreis
					// und
					// Detailliertem Arbeitszeitwert
					losablieferung.setNMaterialwertdetailliert(losablieferung
							.getNGestehungspreis().subtract(
									losablieferung
											.getNArbeitszeitwertdetailliert()));

				} else {
					losablieferung
							.setNMaterialwertdetailliert(bdMatWertDurchschnitt);
					losablieferung.setNMaterialwert(bdMatWertDurchschnitt);
					losablieferung
							.setNArbeitszeitwertdetailliert(bdAzWertDurchschnitt);
					losablieferung.setNArbeitszeitwert(bdAzWertDurchschnitt);
					losablieferung.setNGestehungspreis(bdMatWertDurchschnitt
							.add(bdAzWertDurchschnitt));
				}
				// speichern

				// Nur die Zeiten dieser einen Ablieferung berechnen
				// PJ18599
				if (bGesamteIstzeitenZaehlen) {

					// Wenn Los erledigt, dann bei der letzten Ablieferung die
					// zunkuenftigen Zeiten hinzufuegen
					Timestamp tBis = losablieferung.getTAendern();
					if (losDto.getStatusCNr().equals(LocaleFac.STATUS_ERLEDIGT)
							&& iter.hasNext() == false) {
						tBis = null;
					}

					BigDecimal bdGesamtkostenAZ = new BigDecimal(0);
					// Maschinenzeiten
					AuftragzeitenDto[] zeitenMaschine = getZeiterfassungFac()
							.getAllMaschinenzeitenEinesBeleges(losIId, null,
									tAblieferzeitpunktVorherigeAblieferung,
									tBis, theClientDto);
					for (int j = 0; j < zeitenMaschine.length; j++) {
						bdGesamtkostenAZ = bdGesamtkostenAZ
								.add(zeitenMaschine[j].getBdKosten());
					}

					// "normale" Zeiten
					AuftragzeitenDto[] zeitenMann = getZeiterfassungFac()
							.getAllZeitenEinesBeleges(LocaleFac.BELEGART_LOS,
									losIId, null, null,
									tAblieferzeitpunktVorherigeAblieferung,
									tBis, false, false, theClientDto);
					for (int j = 0; j < zeitenMann.length; j++) {
						bdGesamtkostenAZ = bdGesamtkostenAZ.add(zeitenMann[j]
								.getBdKosten());
					}

					bdGesamtkostenAZ = bdGesamtkostenAZ
							.add(bdArbeitszeitwertAusUnterlosenGesamt);

					BigDecimal bdAbgeliefertGesamt = getErledigteMenge(
							losDto.getIId(), theClientDto);
					if (bdAbgeliefertGesamt.doubleValue() > 0) {
						BigDecimal bdAzWertDerLosablieferung = bdGesamtkostenAZ
								.divide(losablieferung.getNMenge(), 4,
										BigDecimal.ROUND_HALF_EVEN);

						losablieferung
								.setNArbeitszeitwertdetailliert(bdAzWertDerLosablieferung);
						losablieferung
								.setNArbeitszeitwert(bdAzWertDerLosablieferung);
						losablieferung.setNGestehungspreis(losablieferung
								.getNMaterialwert().add(
										bdAzWertDerLosablieferung));

					}
				}

				if (bNurLetztenMaterialwertNeuBerechnen == false
						|| (bNurLetztenMaterialwertNeuBerechnen == true && iter
								.hasNext() == false)) {

					losablieferung.setTAendern(losablieferung.getTAendern());
					losablieferung.setBGestehungspreisneuberechnen(Helper
							.boolean2Short(false));
					LosablieferungDto loaDot = assembleLosablieferungDto(losablieferung);
					// auch in Lagerbewegung aendern
					bucheLosAblieferungAufLager(loaDot, losDto, theClientDto);
				}

				tAblieferzeitpunktVorherigeAblieferung = losablieferung
						.getTAendern();

			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	private void materialwertAllerLosablieferungenNeuBerechnen(LosDto losDto,
			TheClientDto theClientDto) {

		Query query = em.createNamedQuery("LosablieferungfindByLosIId");
		query.setParameter(1, losDto.getIId());
		Collection<?> losablieferungs = query.getResultList();
		LossollmaterialDto[] losmat = lossollmaterialFindByLosIId(losDto
				.getIId());

		BigDecimal bdAbgeliefertGesamt = getErledigteMenge(losDto.getIId(),
				theClientDto);

		BigDecimal bdVorherigerWertDerAblieferung = new BigDecimal(0);

		boolean bErledigt = false;

		if (losDto.getStatusCNr().equals(LocaleFac.STATUS_ERLEDIGT)
				|| bdAbgeliefertGesamt.doubleValue() >= losDto.getNLosgroesse()
						.doubleValue()) {
			bErledigt = true;
		}

		HashMap<Integer, LagerbewegungDto[]> hmLagerbewegungen = new HashMap<Integer, LagerbewegungDto[]>();

		for (int i = 0; i < losmat.length; i++) {

			BigDecimal bdPreis = getAusgegebeneMengePreis(losmat[i].getIId(),
					null, theClientDto);

			LosistmaterialDto[] istmatDtos = losistmaterialFindByLossollmaterialIId(losmat[i]
					.getIId());

			ArrayList al = new ArrayList();

			for (int j = 0; j < istmatDtos.length; j++) {

				List<SeriennrChargennrMitMengeDto> snrDtos = getLagerFac()
						.getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(
								LocaleFac.BELEGART_LOS, istmatDtos[j].getIId());

				for (int k = 0; k < snrDtos.size(); k++) {

					LagerbewegungDto bewDto = getLagerFac().getLetzteintrag(
							LocaleFac.BELEGART_LOS, istmatDtos[j].getIId(),
							snrDtos.get(k).getCSeriennrChargennr());

					bewDto.setNGestehungspreis(bdPreis);

					al.add(bewDto);

				}

			}

			LagerbewegungDto[] returnArray = new LagerbewegungDto[al.size()];
			hmLagerbewegungen.put(losmat[i].getIId(),
					(LagerbewegungDto[]) al.toArray(returnArray));

		}

		BigDecimal abgeliefert = new BigDecimal(0);

		int iAblieferung = 0;

		for (Iterator<?> iter = losablieferungs.iterator(); iter.hasNext();) {
			Losablieferung losablieferung = (Losablieferung) iter.next();
			iAblieferung++;
			abgeliefert = abgeliefert.add(losablieferung.getNMenge());

			BigDecimal bdWertIstProAblieferung = new BigDecimal(0.0000);

			for (int i = 0; i < losmat.length; i++) {

				// Sollsatzgroesse der Position

				BigDecimal ssg = losmat[i]
						.getNMenge()
						.divide(losDto.getNLosgroesse(), 4,
								BigDecimal.ROUND_HALF_EVEN)
						.multiply(abgeliefert);

				BigDecimal bdMengeIst = new BigDecimal(0.0000);

				BigDecimal bdWertIstProPosition = new BigDecimal(0.0000);

				if (hmLagerbewegungen.containsKey(losmat[i].getIId())) {
					LagerbewegungDto[] bewDtos = (LagerbewegungDto[]) hmLagerbewegungen
							.get(losmat[i].getIId());

					if (bewDtos != null) {

						for (int l = 0; l < bewDtos.length; l++) {

							if (bErledigt && iter.hasNext() == false) {

								if (Helper.short2boolean(bewDtos[l]
										.getBAbgang())) {

									bdMengeIst = bdMengeIst.add(bewDtos[l]
											.getNMenge());
									bdWertIstProPosition = bdWertIstProPosition
											.add(bewDtos[l]
													.getNGestehungspreis()
													.multiply(
															bewDtos[l]
																	.getNMenge()));

								} else {
									bdMengeIst = bdMengeIst.subtract(bewDtos[l]
											.getNMenge());
									bdWertIstProPosition = bdWertIstProPosition
											.subtract(bewDtos[l]
													.getNEinstandspreis()
													.multiply(
															bewDtos[l]
																	.getNMenge()));
								}
							} else {
								if (losablieferung.getTAendern().getTime() >= bewDtos[l]
										.getTBuchungszeit().getTime()) {

									if (bdMengeIst.abs()
											.add(bewDtos[l].getNMenge())
											.doubleValue() > ssg.abs()
											.doubleValue()) {

										BigDecimal mengeSSg = null;

										if (bdMengeIst.abs().doubleValue() < ssg
												.abs().doubleValue()) {
											mengeSSg = ssg.subtract(bdMengeIst);
										} else {
											mengeSSg = new BigDecimal(0);
										}

										bdMengeIst = bdMengeIst.add(bewDtos[l]
												.getNMenge());

										if (Helper.short2boolean(bewDtos[l]
												.getBAbgang())) {

											bdWertIstProPosition = bdWertIstProPosition
													.add(bewDtos[l]
															.getNGestehungspreis()
															.multiply(mengeSSg));
										} else {
											bdWertIstProPosition = bdWertIstProPosition
													.add(bewDtos[l]
															.getNEinstandspreis()
															.multiply(mengeSSg));
										}

									} else {

										bdMengeIst = bdMengeIst.add(bewDtos[l]
												.getNMenge());

										if (Helper.short2boolean(bewDtos[l]
												.getBAbgang())) {

											bdWertIstProPosition = bdWertIstProPosition
													.add(bewDtos[l]
															.getNGestehungspreis()
															.multiply(
																	bewDtos[l]
																			.getNMenge()));
										} else {
											bdWertIstProPosition = bdWertIstProPosition
													.subtract(bewDtos[l]
															.getNEinstandspreis()
															.multiply(
																	bewDtos[l]
																			.getNMenge()));
										}
									}

								}
							}

							System.out.println(bewDtos[l].getIIdBuchung() + ":"
									+ bdWertIstProPosition);
						}

						System.out.println(losmat[i].getArtikelIId() + ":"
								+ bdWertIstProPosition);

						bdWertIstProAblieferung = bdWertIstProAblieferung
								.add(bdWertIstProPosition);
					}

				}
			}

			losablieferung.setNMaterialwert(bdWertIstProAblieferung.subtract(
					bdVorherigerWertDerAblieferung).divide(
					losablieferung.getNMenge(), 4, BigDecimal.ROUND_HALF_EVEN));

			em.merge(losablieferung);
			em.flush();

			bdVorherigerWertDerAblieferung = bdWertIstProAblieferung;

		}

	}

	private void aktualisiereAZAllerLosablieferungenWennSollIstGleichIst(
			Integer losIId, TheClientDto theClientDto) {
		LosDto losDto = losFindByPrimaryKey(losIId);
		try {

			BigDecimal faktorFuerIstGleichSoll = new BigDecimal(0);
			BigDecimal erlMenge = getFertigungFac().getErledigteMenge(losIId,
					theClientDto);

			if (erlMenge.doubleValue() > 0) {
				if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)
						|| losDto.getNLosgroesse().doubleValue() <= erlMenge
								.doubleValue()) {
					faktorFuerIstGleichSoll = new BigDecimal(1);
				} else {

					if (losDto.getNLosgroesse().doubleValue() > 0) {
						faktorFuerIstGleichSoll = erlMenge.divide(
								losDto.getNLosgroesse(), 4,
								BigDecimal.ROUND_HALF_EVEN);
					}
				}
				String sMandantWaehrung = theClientDto.getSMandantenwaehrung();
				// Sollzeiten
				LossollarbeitsplanDto[] soll = lossollarbeitsplanFindByLosIId(losDto
						.getIId());

				BigDecimal bdGesamtkosten = new BigDecimal(0.0000);

				// Gesamtkosten berechnen
				for (int i = 0; i < soll.length; i++) {

					// Kosten holen
					BigDecimal kosten = new BigDecimal(0.0000);
					if (soll[i].getMaschineIId() != null) {
						kosten = getZeiterfassungFac()
								.getMaschinenKostenZumZeitpunkt(
										soll[i].getMaschineIId(),
										Helper.cutTimestamp(new Timestamp(
												System.currentTimeMillis())))
								.multiply(soll[i].getNGesamtzeit())
								.multiply(faktorFuerIstGleichSoll);
					}

					ArtikellieferantDto artlief = getArtikelFac()
							.getArtikelEinkaufspreis(
									soll[i].getArtikelIIdTaetigkeit(),
									new BigDecimal(1), sMandantWaehrung,
									theClientDto);
					if (artlief != null && artlief.getLief1Preis() != null) {
						BigDecimal bdSollpreis = artlief.getLief1Preis();
						kosten = kosten.add(soll[i].getNGesamtzeit()
								.multiply(bdSollpreis)
								.multiply(faktorFuerIstGleichSoll));
					}
					bdGesamtkosten = bdGesamtkosten.add(kosten);

				}

				LosablieferungDto[] losablieferungDtos = losablieferungFindByLosIId(
						losIId, false, theClientDto);

				BigDecimal kostenProStueck = bdGesamtkosten.divide(erlMenge, 4,
						BigDecimal.ROUND_HALF_EVEN);

				for (int i = 0; i < losablieferungDtos.length; i++) {
					LosablieferungDto losablieferungDto = losablieferungDtos[i];

					// try {

					Losablieferung losablieferung = em.find(
							Losablieferung.class, losablieferungDto.getIId());
					if (losablieferung == null) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
					}
					losablieferung.setNArbeitszeitwert(kostenProStueck);
					losablieferung
							.setNArbeitszeitwertdetailliert(kostenProStueck);
					// }
					// catch (FinderException ex) {
					// throw new
					// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					// ex);
					// }

				}
			}

		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

	}

	private void aktualisiereAZAllerLosablieferungen(Integer losIId,
			TheClientDto theClientDto) {
		LosDto losDto = losFindByPrimaryKey(losIId);
		try {
			// Sollzeiten
			LossollarbeitsplanDto[] soll = lossollarbeitsplanFindByLosIId(losDto
					.getIId());
			int SOLL = 0;
			int IST = 1;
			int WERT = 2;
			// Sollzeiten nach Artikel verdichten
			HashMap<Integer, Object[]> listSollVerdichtet = new HashMap<Integer, Object[]>();
			for (int i = 0; i < soll.length; i++) {

				if (listSollVerdichtet.containsKey(soll[i]
						.getArtikelIIdTaetigkeit())) {
					Object[] oTemp = listSollVerdichtet.get(soll[i]
							.getArtikelIIdTaetigkeit());
					BigDecimal sollZeit = soll[i].getNGesamtzeit();
					if (soll[i].getMaschineIId() != null) {
						sollZeit = sollZeit.multiply(new BigDecimal(2));
					}

					oTemp[SOLL] = ((BigDecimal) oTemp[SOLL]).add(sollZeit);

					oTemp[IST] = new BigDecimal(0.00);
					oTemp[WERT] = new BigDecimal(0.00);

					listSollVerdichtet.put(soll[i].getArtikelIIdTaetigkeit(),
							oTemp);
				} else {
					Object[] oZeile = new Object[3];
					BigDecimal sollZeit = soll[i].getNGesamtzeit();
					if (soll[i].getMaschineIId() != null) {
						sollZeit = sollZeit.multiply(new BigDecimal(2));
					}
					oZeile[SOLL] = sollZeit;

					oZeile[IST] = new BigDecimal(0.00);
					oZeile[WERT] = new BigDecimal(0.00);

					listSollVerdichtet.put(soll[i].getArtikelIIdTaetigkeit(),
							oZeile);
				}
			}

			// Maschinenzeiten
			AuftragzeitenDto[] zeitenMaschine = getZeiterfassungFac()
					.getAllMaschinenzeitenEinesBeleges(losIId, null, null,
							null, theClientDto);

			// "normale" Zeiten
			AuftragzeitenDto[] zeitenMann = getZeiterfassungFac()
					.getAllZeitenEinesBeleges(LocaleFac.BELEGART_LOS, losIId,
							null, null, null, null, false, false, theClientDto);

			LosablieferungDto[] losablieferungDtos = losablieferungFindByLosIIdOhneNeuberechnungUndOhneSnrChnr(
					losIId, theClientDto);

			boolean bErledigtOderUeberliefert = false;
			BigDecimal bdGesamtAbgeliefert = new BigDecimal(0.00);
			for (int i = 0; i < losablieferungDtos.length; i++) {
				LosablieferungDto losablieferungDto = losablieferungDtos[i];
				bdGesamtAbgeliefert = bdGesamtAbgeliefert.add(losablieferungDto
						.getNMenge());
			}

			if (losDto.getStatusCNr().equals(LocaleFac.STATUS_ERLEDIGT)
					|| bdGesamtAbgeliefert.doubleValue() >= losDto
							.getNLosgroesse().doubleValue()) {
				bErledigtOderUeberliefert = true;
			}

			BigDecimal bdKostenGesamt = new BigDecimal(0.00);
			BigDecimal bdVerbrauchteKostenGesamt = new BigDecimal(0.00);
			HashMap<Integer, BigDecimal> hmUeberigeZeitderVorhergehendenAblieferung = new HashMap();
			HashMap bdUeberigeKostenderVorhergehendenAblieferung = new HashMap<Object, BigDecimal>();

			for (int i = 0; i < losablieferungDtos.length; i++) {
				LosablieferungDto losablieferungDto = losablieferungDtos[i];

				HashMap<Integer, Object[]> hmAblieferung = new HashMap<Integer, Object[]>();
				Iterator<?> it = listSollVerdichtet.keySet().iterator();
				while (it.hasNext()) {
					Integer key = (Integer) it.next();
					Object[] oNew = new Object[3];
					Object[] oVorhanden = listSollVerdichtet.get(key);
					oNew[SOLL] = oVorhanden[SOLL];
					oNew[IST] = oVorhanden[IST];
					oNew[WERT] = oVorhanden[WERT];
					hmAblieferung.put(key, oNew);
				}

				for (int j = 0; j < zeitenMaschine.length; j++) {
					BigDecimal bdIst = new BigDecimal(0.00);

					if (zeitenMaschine[j].getTsEnde() != null) {
						if (i == 0 && i == losablieferungDtos.length - 1) {
							if (zeitenMaschine[j].getTsEnde().before(
									losablieferungDto.getTAendern())
									|| zeitenMaschine[j].getTsEnde()
											.after(losablieferungDtos[0]
													.getTAendern())) {
								bdIst = bdIst.add(new BigDecimal(
										zeitenMaschine[j].getDdDauer()
												.doubleValue()));
							}

						} else if (i == 0) {
							if (zeitenMaschine[j].getTsEnde().before(
									losablieferungDto.getTAendern())) {
								bdIst = bdIst.add(new BigDecimal(
										zeitenMaschine[j].getDdDauer()
												.doubleValue()));

							}
						} else if (i == losablieferungDtos.length - 1) {
							if (zeitenMaschine[j].getTsEnde().after(
									losablieferungDtos[i - 1].getTAendern())) {
								bdIst = bdIst.add(new BigDecimal(
										zeitenMaschine[j].getDdDauer()
												.doubleValue()));
							}
						} else {
							if (zeitenMaschine[j].getTsEnde().after(
									losablieferungDtos[i - 1].getTAendern())
									&& zeitenMaschine[j].getTsEnde().before(
											losablieferungDto.getTAendern())) {
								bdIst = bdIst.add(new BigDecimal(
										zeitenMaschine[j].getDdDauer()
												.doubleValue()));
							}
						}
					}

					if (bdIst.doubleValue() > 0) {
						bdKostenGesamt = bdKostenGesamt.add(zeitenMaschine[j]
								.getBdKosten());
						if (hmAblieferung.containsKey(zeitenMaschine[j]
								.getArtikelIId())) {
							Object[] oZeile = hmAblieferung
									.get(zeitenMaschine[j].getArtikelIId());
							oZeile[IST] = ((BigDecimal) oZeile[IST]).add(bdIst);
							oZeile[WERT] = ((BigDecimal) oZeile[WERT])
									.add(zeitenMaschine[j].getBdKosten());
							hmAblieferung.put(
									zeitenMaschine[j].getArtikelIId(), oZeile);
						} else {
							Object[] oZeile = new Object[3];
							oZeile[SOLL] = new BigDecimal(0.00);
							oZeile[IST] = bdIst;
							oZeile[WERT] = zeitenMaschine[j].getBdKosten();
							hmAblieferung.put(
									zeitenMaschine[j].getArtikelIId(), oZeile);
						}
					}

				}

				// Zeiten Mann
				for (int j = 0; j < zeitenMann.length; j++) {
					BigDecimal bdIst = new BigDecimal(0.00);

					if (zeitenMann[j].getTsEnde() != null) {
						if (i == 0 && i == losablieferungDtos.length - 1) {
							if (zeitenMann[j].getTsEnde().before(
									losablieferungDto.getTAendern())
									|| zeitenMann[j].getTsEnde()
											.after(losablieferungDtos[0]
													.getTAendern())) {
								bdIst = bdIst.add(new BigDecimal(zeitenMann[j]
										.getDdDauer().doubleValue()));
							}

						}

						else if (i == 0) {
							if (zeitenMann[j].getTsEnde().before(
									losablieferungDto.getTAendern())) {
								bdIst = bdIst.add(new BigDecimal(zeitenMann[j]
										.getDdDauer().doubleValue()));

							}
						} else if (i == losablieferungDtos.length - 1) {
							if (zeitenMann[j].getTsEnde().after(
									losablieferungDtos[i - 1].getTAendern())) {
								bdIst = bdIst.add(new BigDecimal(zeitenMann[j]
										.getDdDauer().doubleValue()));
							}
						} else {
							if (zeitenMann[j].getTsEnde().after(
									losablieferungDtos[i - 1].getTAendern())
									&& zeitenMann[j].getTsEnde().before(
											losablieferungDto.getTAendern())) {
								bdIst = bdIst.add(new BigDecimal(zeitenMann[j]
										.getDdDauer().doubleValue()));
							}
						}
					}
					if (bdIst.doubleValue() > 0) {
						bdKostenGesamt = bdKostenGesamt.add(zeitenMann[j]
								.getBdKosten());
						if (hmAblieferung.containsKey(zeitenMann[j]
								.getArtikelIId())) {
							Object[] oZeile = hmAblieferung.get(zeitenMann[j]
									.getArtikelIId());
							oZeile[IST] = ((BigDecimal) oZeile[IST]).add(bdIst);
							oZeile[WERT] = ((BigDecimal) oZeile[WERT])
									.add(zeitenMann[j].getBdKosten());
							hmAblieferung.put(zeitenMann[j].getArtikelIId(),
									oZeile);
						} else {
							Object[] oZeile = new Object[3];
							oZeile[SOLL] = new BigDecimal(0.00);
							oZeile[IST] = bdIst;
							oZeile[WERT] = zeitenMann[j].getBdKosten();
							hmAblieferung.put(zeitenMann[j].getArtikelIId(),
									oZeile);
						}
					}
				}

				Iterator<?> itTemp = hmAblieferung.keySet().iterator();
				BigDecimal azWertDerAblieferung = new BigDecimal(0.00);
				while (itTemp.hasNext()) {
					Integer key = (Integer) itTemp.next();

					Object[] oZeile = hmAblieferung.get(key);

					// Sollsatzgroesze ermitteln
					BigDecimal bdIstAblieferung = (BigDecimal) oZeile[IST];

					// Vorherige uebrige Zeit dazuzaehlen
					if (hmUeberigeZeitderVorhergehendenAblieferung
							.containsKey(key)) {
						bdIstAblieferung = bdIstAblieferung
								.add((BigDecimal) hmUeberigeZeitderVorhergehendenAblieferung
										.get(key));
					}

					BigDecimal bdSollAblieferung = (BigDecimal) oZeile[SOLL];

					BigDecimal bdKostenAblieferung = (BigDecimal) oZeile[WERT];
					if (bdUeberigeKostenderVorhergehendenAblieferung
							.containsKey(key)) {
						bdKostenAblieferung = bdKostenAblieferung
								.add((BigDecimal) bdUeberigeKostenderVorhergehendenAblieferung
										.get(key));
					}

					if (bdSollAblieferung.doubleValue() != 0) {

						// Sollsatzgroesse ermitteln

						BigDecimal sollsatzgroesse = bdSollAblieferung.divide(
								losDto.getNLosgroesse(), 4,
								BigDecimal.ROUND_HALF_EVEN).multiply(
								losablieferungDto.getNMenge());

						BigDecimal maxSollsatzKosten = bdSollAblieferung
								.multiply(losablieferungDto.getNMenge());

						BigDecimal tatsaechlicheKosten = null;
						if (bdKostenAblieferung.doubleValue() > maxSollsatzKosten
								.doubleValue()) {
							tatsaechlicheKosten = maxSollsatzKosten;

						} else {
							tatsaechlicheKosten = bdKostenAblieferung;
						}

						azWertDerAblieferung = azWertDerAblieferung
								.add(tatsaechlicheKosten);
						if (bdKostenAblieferung.doubleValue() > azWertDerAblieferung
								.doubleValue()) {
							bdUeberigeKostenderVorhergehendenAblieferung.put(
									key, bdKostenAblieferung
											.subtract(azWertDerAblieferung));
						}

					} else {
						azWertDerAblieferung = azWertDerAblieferung
								.add(bdKostenAblieferung);
					}
					System.out.println(azWertDerAblieferung);
				}
				bdVerbrauchteKostenGesamt = bdVerbrauchteKostenGesamt
						.add(azWertDerAblieferung);

				// Wenn ERLEDIGT oder Ueberliefert //Den Rest hinzufuegen
				if (bErledigtOderUeberliefert
						&& i == losablieferungDtos.length - 1) {
					BigDecimal restKosten = bdKostenGesamt
							.subtract(bdVerbrauchteKostenGesamt);
					azWertDerAblieferung = azWertDerAblieferung.add(restKosten);
				}
				if (losablieferungDto.getNMenge().doubleValue() != 0) {
					azWertDerAblieferung = azWertDerAblieferung.divide(
							losablieferungDto.getNMenge(), 4,
							BigDecimal.ROUND_HALF_EVEN);
				}
				Losablieferung losablieferung = em.find(Losablieferung.class,
						losablieferungDto.getIId());
				if (losablieferung == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
				}
				losablieferung.setNArbeitszeitwert(azWertDerAblieferung);
				losablieferung
						.setNArbeitszeitwertdetailliert(azWertDerAblieferung);
			}

		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

	}

	private void bucheNegativesollmengenAufLager(LosDto losDto,
			BigDecimal bdGesamtAbgeliefert, TheClientDto theClientDto) {

		Query query = em.createNamedQuery("LossollmaterialfindByLosIId");
		query.setParameter(1, losDto.getIId());
		Collection<?> cl = query.getResultList();

		LossollmaterialDto[] sollmat = assembleLossollmaterialDtos(cl);

		Integer lagerIId = null;

		ParametermandantDto parametermandantDto;
		try {
			parametermandantDto = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FERTIGUNG,
							ParameterFac.PARAMETER_NEGATIVE_SOLLMENGEN_BUCHEN_AUF_ZIELLAGER);

			if (((java.lang.Boolean) parametermandantDto.getCWertAsObject())
					.booleanValue() == true) {
				lagerIId = losDto.getLagerIIdZiel();
			} else {
				// Laeger des Loses
				LoslagerentnahmeDto[] laeger = loslagerentnahmeFindByLosIId(losDto
						.getIId());
				lagerIId = laeger[0].getLagerIId();
			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		for (int i = 0; i < sollmat.length; i++) {
			LossollmaterialDto sollmatZeile = sollmat[i];

			if (sollmatZeile.getNMenge().doubleValue() < 0) {

				// Sollsatzgroesse
				BigDecimal ssg = sollmatZeile
						.getNMenge()
						.abs()
						.divide(losDto.getNLosgroesse(), 10,
								BigDecimal.ROUND_HALF_EVEN);

				BigDecimal soll = bdGesamtAbgeliefert.multiply(ssg);

				BigDecimal ausgegeben = getAusgegebeneMenge(
						sollmatZeile.getIId(), null, theClientDto).abs();

				BigDecimal mengeNeu = soll.subtract(ausgegeben);

				if (mengeNeu.doubleValue() > 0) {
					LosistmaterialDto istmat = new LosistmaterialDto();
					istmat.setLagerIId(lagerIId);
					istmat.setLossollmaterialIId(sollmat[i].getIId());
					istmat.setNMenge(mengeNeu.abs());
					istmat.setBAbgang(Helper.boolean2Short(false));

					createLosistmaterial(istmat, null, theClientDto);
				}

			}
		}
	}

	public void manuellErledigen(Integer losIId,
			boolean bDatumDerLetztenAblieferungAlsErledigtDatumVerwenden,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// losstatus pruefen
		Los los = null;

		los = em.find(Los.class, losIId);
		if (los == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		if (los.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT, "");
		} else if (los.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
					"");
		} else if (los.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN,
					"");
		} else if (los.getStatusCNr().equals(FertigungFac.STATUS_GESTOPPT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_GESTOPPT, "");
		}

		LosDto losDto = assembleLosDto(los);

		if (losDto.getStuecklisteIId() != null) {

			StuecklisteDto stklDto = getStuecklisteFac()
					.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(),
							theClientDto);

			ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
					stklDto.getArtikelIId(), theClientDto);

			if (Helper.short2boolean(aDto.getBDokumentenpflicht())) {

				LosablieferungDto[] losablieferungDtos = null;

				Query query = em.createNamedQuery("LosablieferungfindByLosIId");
				query.setParameter(1, losIId);
				Collection<?> cl = query.getResultList();

				losablieferungDtos = assembleLosablieferungDtos(cl);
				String textFehlermeldungDokumente = "";
				boolean bDokumenteFuerAlleLosablieferungenVorhanden = true;
				for (int i = 0; i < losablieferungDtos.length; i++) {
					PrintInfoDto piDto = getJCRDocFac()
							.getPathAndPartnerAndTable(
									losablieferungDtos[i].getIId(),
									QueryParameters.UC_ID_LOSABLIEFERUNG,
									theClientDto);

					if (piDto != null && piDto.getDocPath() != null) {
						try {
							ArrayList<JCRDocDto> al = getJCRDocFac()
									.getJCRDocDtoFromNodeChildren(
											piDto.getDocPath());
							if (al == null || al.size() == 0) {
								bDokumenteFuerAlleLosablieferungenVorhanden = false;

								textFehlermeldungDokumente += losDto.getCNr()
										+ " "
										+ Helper.formatDatumZeit(
												losablieferungDtos[i]
														.getTAendern(),
												theClientDto.getLocUi())
										+ "\r\n";
							}

						} catch (Exception t) {
							t.printStackTrace();
							bDokumenteFuerAlleLosablieferungenVorhanden = false;
						}
					} else {
						bDokumenteFuerAlleLosablieferungenVorhanden = false;
					}

				}

				if (bDokumenteFuerAlleLosablieferungenVorhanden == false) {
					ArrayList al = new ArrayList();
					al.add(textFehlermeldungDokumente);

					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_KEIN_DOKUMENT_BEI_DOKUMENTENPFLICHTIGEM_ARTIKEL_IM_LOS_HINTERLEGT,
							al,
							new Exception(
									"FEHLER_KEIN_DOKUMENT_BEI_DOKUMENTENPFLICHTIGEM_ARTIKEL_HINTERLEGT"));
				}

			}
		}

		bucheNegativesollmengenAufLager(losDto,
				getErledigteMenge(losDto.getIId(), theClientDto), theClientDto);

		los.setStatusCNr(FertigungFac.STATUS_ERLEDIGT);
		los.setPersonalIIdManuellerledigt(theClientDto.getIDPersonal());
		if (bDatumDerLetztenAblieferungAlsErledigtDatumVerwenden == true) {

			LosablieferungDto[] losablieferungDtos = null;
			// try {
			Query query = em.createNamedQuery("LosablieferungfindByLosIId");
			query.setParameter(1, losIId);
			Collection<?> cl = query.getResultList();
			// if (cl.isEmpty()) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
			// }
			losablieferungDtos = assembleLosablieferungDtos(cl);

			if (losablieferungDtos != null && losablieferungDtos.length > 0) {
				los.setTManuellerledigt(losablieferungDtos[losablieferungDtos.length - 1]
						.getTAendern());
			} else {
				los.setTManuellerledigt(getTimestamp());
			}

		} else {
			los.setTManuellerledigt(getTimestamp());
		}

		// Fehlmengen loeschen
		LossollmaterialDto[] losmat = lossollmaterialFindByLosIId(los.getIId());
		for (int i = 0; i < losmat.length; i++) {
			try {
				getFehlmengeFac().aktualisiereFehlmenge(LocaleFac.BELEGART_LOS,
						losmat[i].getIId(), false, theClientDto);
			} catch (RemoteException ex1) {
				throwEJBExceptionLPRespectOld(ex1);
			}
		}
		// lt. WH (Projekt 12966)
		aktualisiereNachtraeglichPreiseAllerLosablieferungen(losIId,
				theClientDto, true);

	}

	public void bucheFehlmengenNach(LosDto losDto,
			LossollmaterialDto[] sollmat, TheClientDto theClientDto) {
		try {

			// Laeger des Loses
			LoslagerentnahmeDto[] laeger = loslagerentnahmeFindByLosIId(losDto
					.getIId());
			// nun vom lager abbuchen
			for (int i = 0; i < sollmat.length; i++) {
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKey(sollmat[i].getArtikelIId(),
								theClientDto);
				// seriennummerntragende werden jetzt gar nicht gebucht

				ArtikelfehlmengeDto artikelfehlemngeDto = getFehlmengeFac()
						.artikelfehlmengeFindByBelegartCNrBelegartPositionIIdOhneExc(
								LocaleFac.BELEGART_LOS, sollmat[i].getIId());

				if (artikelfehlemngeDto != null) {

					BigDecimal bdAbzubuchendeMenge = artikelfehlemngeDto
							.getNMenge();
					if (!Helper.short2boolean(artikelDto.getBSeriennrtragend())) {
						if (!Helper.short2boolean(artikelDto
								.getBChargennrtragend())) {
							for (int j = 0; j < laeger.length; j++) {
								// wenn noch was abzubuchen ist (Menge > 0)
								if (bdAbzubuchendeMenge
										.compareTo(new BigDecimal(0)) == 1) {
									BigDecimal bdLagerstand = null;
									if (Helper.short2boolean(artikelDto
											.getBLagerbewirtschaftet())) {
										bdLagerstand = getLagerFac()
												.getLagerstand(
														artikelDto.getIId(),
														laeger[j].getLagerIId(),
														theClientDto);

									} else {
										bdLagerstand = new BigDecimal(999999999);
									}
									// wenn ein lagerstand da ist
									if (bdLagerstand
											.compareTo(new BigDecimal(0)) == 1) {
										BigDecimal bdMengeVonLager;
										if (bdLagerstand
												.compareTo(bdAbzubuchendeMenge) == 1) {
											// wenn mehr als ausreichend auf
											// lager
											bdMengeVonLager = bdAbzubuchendeMenge;
										} else {
											// dann nur den lagerstand entnehmen
											bdMengeVonLager = bdLagerstand;
										}
										LosistmaterialDto istmat = new LosistmaterialDto();
										istmat.setLagerIId(laeger[j]
												.getLagerIId());
										istmat.setLossollmaterialIId(sollmat[i]
												.getIId());
										istmat.setNMenge(bdMengeVonLager);
										istmat.setBAbgang(Helper
												.boolean2Short(true));
										// ist-wert anlegen und lagerbuchung
										// durchfuehren
										createLosistmaterial(istmat, null,
												theClientDto);
										// menge reduzieren
										bdAbzubuchendeMenge = bdAbzubuchendeMenge
												.subtract(bdMengeVonLager);
									}
								}
							}
						}
					} else {
						/**
						 * @todo falls nur eine charge auf lager ist -> trotzdem
						 *       buchen PJ 4220
						 */
					}

				}

			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

	}

	@TransactionTimeout(1800)
	public LosablieferungDto createLosablieferung(
			LosablieferungDto losablieferungDto, TheClientDto theClientDto,
			boolean bErledigt) throws EJBExceptionLP {
		// log
		myLogger.logData(losablieferungDto);

		// losstatus pruefen
		LosDto losDto = losFindByPrimaryKey(losablieferungDto.getLosIId());
		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT, "");
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
					"");
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN,
					"");
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_GESTOPPT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_GESTOPPT, "");
		}

		// neuer losstatus - entweder erledigt oder teilerledigt
		Los los = null;
		// try {
		los = em.find(Los.class, losDto.getIId());
		if (los == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		try {

			if (bErledigt) {
				los.setStatusCNr(FertigungFac.STATUS_ERLEDIGT);
				losDto.setStatusCNr(FertigungFac.STATUS_ERLEDIGT);
				los.setPersonalIIdErledigt(theClientDto.getIDPersonal());
				los.setTErledigt(getTimestamp());
				// Fehlmengen loeschen
				LossollmaterialDto[] losmat = lossollmaterialFindByLosIId(los
						.getIId());

				//
				ParametermandantDto parametermandantDto = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_FERTIGUNG,
								ParameterFac.PARAMETER_BEI_LOS_ERLEDIGEN_MATERIAL_NACHBUCHEN);
				if (((java.lang.Boolean) parametermandantDto.getCWertAsObject())
						.booleanValue() == true) {
					bucheFehlmengenNach(losDto, losmat, theClientDto);
				}

				for (int i = 0; i < losmat.length; i++) {
					getFehlmengeFac().aktualisiereFehlmenge(
							LocaleFac.BELEGART_LOS, losmat[i].getIId(), false,
							theClientDto);
				}
			} else {
				los.setStatusCNr(FertigungFac.STATUS_TEILERLEDIGT);
				losDto.setStatusCNr(FertigungFac.STATUS_TEILERLEDIGT);
			}

			// begin
			losablieferungDto.setPersonalIIdAendern(theClientDto
					.getIDPersonal());
			// primary key
			Integer iId = getPKGeneratorObj().getNextPrimaryKey(
					PKConst.PK_LOSABLIEFERUNG);
			losablieferungDto.setIId(iId);
			// werte werden spaeter berechnet
			losablieferungDto.setNGestehungspreis(new BigDecimal(0));
			losablieferungDto.setNMaterialwert(new BigDecimal(0));
			losablieferungDto.setNArbeitszeitwert(new BigDecimal(0));
			losablieferungDto.setNMaterialwertdetailliert(new BigDecimal(0));
			losablieferungDto.setNArbeitszeitwertdetailliert(new BigDecimal(0));

			Losablieferung losablieferung = new Losablieferung(
					losablieferungDto.getIId(), losablieferungDto.getLosIId(),
					losablieferungDto.getNMenge(),
					losablieferungDto.getNGestehungspreis(),
					losablieferungDto.getNMaterialwert(),
					losablieferungDto.getNArbeitszeitwert(),
					losablieferungDto.getPersonalIIdAendern(),
					losablieferungDto.getNMaterialwertdetailliert(),
					losablieferungDto.getNArbeitszeitwertdetailliert());
			em.persist(losablieferung);
			em.flush();

			// speichern
			losablieferungDto.setTAendern(losablieferung.getTAendern());
			losablieferungDto.setBGestehungspreisNeuBerechnen(losablieferung
					.getBGestehungspreisneuberechnen());
			setLosablieferungFromLosablieferungDto(losablieferung,
					losablieferungDto);

			bucheLosAblieferungAufLager(losablieferungDto, losDto, theClientDto);
			aktualisiereNachtraeglichPreiseAllerLosablieferungen(
					losDto.getIId(), theClientDto, true);

			// PJ 14506
			BigDecimal bdAbgeliefertGesamt = getErledigteMenge(losDto.getIId(),
					theClientDto);
			bucheNegativesollmengenAufLager(losDto, bdAbgeliefertGesamt,
					theClientDto);

			return losablieferungDto;
		} catch (EntityNotFoundException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
			return null;
		}
	}

	@TransactionTimeout(1800)
	public LosablieferungDto createLosablieferungFuerTerminalOhnePreisberechnung(
			LosablieferungDto losablieferungDto, TheClientDto theClientDto,
			boolean bErledigt) {
		// log
		myLogger.logData(losablieferungDto);

		// losstatus pruefen
		LosDto losDto = losFindByPrimaryKey(losablieferungDto.getLosIId());
		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT, "");
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
					"");
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN,
					"");
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_GESTOPPT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_GESTOPPT, "");
		}

		// neuer losstatus - entweder erledigt oder teilerledigt
		Los los = null;
		// try {
		los = em.find(Los.class, losDto.getIId());
		if (los == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		try {

			if (bErledigt) {
				los.setStatusCNr(FertigungFac.STATUS_ERLEDIGT);
				losDto.setStatusCNr(FertigungFac.STATUS_ERLEDIGT);
				los.setPersonalIIdErledigt(theClientDto.getIDPersonal());
				los.setTErledigt(getTimestamp());
				// Fehlmengen loeschen
				LossollmaterialDto[] losmat = lossollmaterialFindByLosIId(los
						.getIId());

				//
				ParametermandantDto parametermandantDto = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_FERTIGUNG,
								ParameterFac.PARAMETER_BEI_LOS_ERLEDIGEN_MATERIAL_NACHBUCHEN);
				if (((java.lang.Boolean) parametermandantDto.getCWertAsObject())
						.booleanValue() == true) {
					bucheFehlmengenNach(losDto, losmat, theClientDto);
				}

				for (int i = 0; i < losmat.length; i++) {
					getFehlmengeFac().aktualisiereFehlmenge(
							LocaleFac.BELEGART_LOS, losmat[i].getIId(), false,
							theClientDto);
				}
			} else {
				los.setStatusCNr(FertigungFac.STATUS_TEILERLEDIGT);
				losDto.setStatusCNr(FertigungFac.STATUS_TEILERLEDIGT);
			}

			// begin
			losablieferungDto.setPersonalIIdAendern(theClientDto
					.getIDPersonal());
			// primary key
			Integer iId = getPKGeneratorObj().getNextPrimaryKey(
					PKConst.PK_LOSABLIEFERUNG);
			losablieferungDto.setIId(iId);
			// werte werden spaeter berechnet
			losablieferungDto.setNGestehungspreis(new BigDecimal(0));
			losablieferungDto.setNMaterialwert(new BigDecimal(0));
			losablieferungDto.setNArbeitszeitwert(new BigDecimal(0));
			losablieferungDto.setNMaterialwertdetailliert(new BigDecimal(0));
			losablieferungDto.setNArbeitszeitwertdetailliert(new BigDecimal(0));

			Losablieferung losablieferung = new Losablieferung(
					losablieferungDto.getIId(), losablieferungDto.getLosIId(),
					losablieferungDto.getNMenge(),
					losablieferungDto.getNGestehungspreis(),
					losablieferungDto.getNMaterialwert(),
					losablieferungDto.getNArbeitszeitwert(),
					losablieferungDto.getPersonalIIdAendern(),
					losablieferungDto.getNMaterialwertdetailliert(),
					losablieferungDto.getNArbeitszeitwertdetailliert());
			em.persist(losablieferung);
			em.flush();

			// speichern
			if (losablieferungDto.getTAendern() == null) {

				losablieferungDto.setTAendern(losablieferung.getTAendern());
			}
			losablieferungDto.setBGestehungspreisNeuBerechnen(losablieferung
					.getBGestehungspreisneuberechnen());
			setLosablieferungFromLosablieferungDto(losablieferung,
					losablieferungDto);

			bucheLosAblieferungAufLager(losablieferungDto, losDto, theClientDto);

			// PJ 14506
			BigDecimal bdAbgeliefertGesamt = getErledigteMenge(losDto.getIId(),
					theClientDto);
			bucheNegativesollmengenAufLager(losDto, bdAbgeliefertGesamt,
					theClientDto);

			return losablieferungDto;
		} catch (EntityNotFoundException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
			return null;
		}
	}

	public void bucheLosAblieferungAufLager(
			LosablieferungDto losablieferungDto, LosDto losDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
		// Lagerbuchung, aber nicht fuer Materiallisten
		if (losDto.getStuecklisteIId() != null) {
			StuecklisteDto stuecklisteDto = getStuecklisteFac()
					.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(),
							theClientDto);

			getLagerFac().bucheZu(LocaleFac.BELEGART_LOSABLIEFERUNG,
					losDto.getIId(), losablieferungDto.getIId(),
					stuecklisteDto.getArtikelIId(),
					losablieferungDto.getNMenge(),
					losablieferungDto.getNGestehungspreis(),
					losDto.getLagerIIdZiel(),
					losablieferungDto.getSeriennrChargennrMitMenge(),
					losablieferungDto.getTAendern(), theClientDto);

		}
	}

	@TransactionTimeout(1000)
	public void removeLosablieferung(Object[] loablieferungIIds,
			TheClientDto theClientDto) {

		Losablieferung ersteAblieferung = em.find(Losablieferung.class,
				(Integer) loablieferungIIds[0]);

		// losstatus pruefen
		LosDto losDto = losFindByPrimaryKey(ersteAblieferung.getLosIId());
		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT, "");
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN,
					"");
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_GESTOPPT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_GESTOPPT, "");
		}

		for (int i = 0; i < loablieferungIIds.length; i++) {

			Integer losablieferungIId = (Integer) loablieferungIIds[i];

			Losablieferung toRemove = em.find(Losablieferung.class,
					losablieferungIId);

			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						er);
			}

			// Lagerbuchung, aber nicht fuer Materiallisten
			if (losDto.getStuecklisteIId() != null) {

				getLagerFac().loescheKompletteLagerbewegungEinerBelgposition(
						LocaleFac.BELEGART_LOSABLIEFERUNG, losablieferungIId,
						theClientDto);
			}

		}

		// neuer losstatus
		// try {
		LosablieferungDto[] la = losablieferungFindByLosIId(losDto.getIId(),
				true, theClientDto);

		Los los = em.find(Los.class, losDto.getIId());
		if (los == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		String alterStatus = los.getStatusCNr();

		if (la.length == 0) {
			los.setStatusCNr(FertigungFac.STATUS_IN_PRODUKTION);
			los.setPersonalIIdErledigt(null);
			los.setTErledigt(null);
		} else {
			los.setStatusCNr(FertigungFac.STATUS_TEILERLEDIGT);
			los.setPersonalIIdErledigt(null);
			los.setTErledigt(null);
		}
		// CK: Wenn Status ERLEDIGT zurueckgenommen wurde, dann
		// muessen
		// die Fehlmengen wiederhergestellt werden

		try {

			if (alterStatus.equals(FertigungFac.STATUS_ERLEDIGT)) {
				LossollmaterialDto[] lossollmaterialDtos = lossollmaterialFindByLosIId(los
						.getIId());

				for (int k = 0; k < lossollmaterialDtos.length; k++) {
					LossollmaterialDto lossollmaterialDto = lossollmaterialDtos[k];
					if (!Helper.short2Boolean(lossollmaterialDto
							.getBNachtraeglich())) {
						// Fehlmengen aktualisieren
						getFehlmengeFac().aktualisiereFehlmenge(
								LocaleFac.BELEGART_LOS,
								lossollmaterialDto.getIId(), false,
								theClientDto);
					}
				}
			}
		}
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
		// ex);
		// }
		catch (RemoteException ex) {
			// falls die zugebuchte menge schon verbraucht wurde, muss ich
			// das
			// extra behandeln
			Throwable eCause = ex.getCause();
			if (eCause instanceof EJBExceptionLP) {
				EJBExceptionLP e = (EJBExceptionLP) eCause;
				if (e.getCode() == EJBExceptionLP.FEHLER_MENGENREDUZIERUNG_NICHT_MOEGLICH) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_ZUGEBUCHTES_MATERIAL_BEREITS_VOM_LAGER_ENTNOMMEN,
							new Exception(
									"FEHLER_FERTIGUNG_ABLIEFERUNG_BEREITS_VOM_LAGER_ENTNOMMEN"));
				}
			}
			throwEJBExceptionLPRespectOld(ex);
		}

	}

	public LosablieferungDto updateLosablieferung(
			LosablieferungDto losablieferungDto,
			boolean bMaterialZurueckbuchen, TheClientDto theClientDto)
			throws EJBExceptionLP {

		// losstatus pruefen

		LosDto losDto = losFindByPrimaryKey(losablieferungDto.getLosIId());
		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT, "");
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
					"");
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN,
					"");
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_GESTOPPT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_GESTOPPT, "");
		}

		Losablieferung laVorher = em.find(Losablieferung.class,
				losablieferungDto.getIId());

		// Los updaten, wenn Mengenreduktion
		if (losablieferungDto.getNMenge().doubleValue() > 0
				&& losablieferungDto.getNMenge().doubleValue() <= laVorher
						.getNMenge().doubleValue()) {

			Losablieferung losablieferung = em.find(Losablieferung.class,
					losablieferungDto.getIId());
			setLosablieferungFromLosablieferungDto(losablieferung,
					losablieferungDto);

			try {
				bucheLosAblieferungAufLager(losablieferungDto, losDto,
						theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			// Material zurueckbuchen
			if (bMaterialZurueckbuchen) {
				LosablieferungDto[] dtos = losablieferungFindByLosIId(
						losDto.getIId(), false, theClientDto);
				BigDecimal bdGesamtAbgeliefert = new BigDecimal(0.00);
				for (int i = 0; i < dtos.length; i++) {
					LosablieferungDto losablieferungenDto = dtos[i];
					bdGesamtAbgeliefert = bdGesamtAbgeliefert
							.add(losablieferungenDto.getNMenge());
				}

				LossollmaterialDto[] sollmatDtos = lossollmaterialFindByLosIId(losDto
						.getIId());
				for (int i = 0; i < sollmatDtos.length; i++) {
					LossollmaterialDto sollmatDto = sollmatDtos[i];

					if (sollmatDto.getNMenge().doubleValue() != 0) {
						BigDecimal sollsatzgroesse = sollmatDto
								.getNMenge()
								.divide(sollmatDto.getNMenge(), 6,
										BigDecimal.ROUND_HALF_EVEN)
								.multiply(bdGesamtAbgeliefert);

						// Wenn nachtraegliche position, dann auslassen
						if (Helper
								.short2boolean(sollmatDto.getBNachtraeglich()) == false) {
							LosistmaterialDto[] losistmaterialDtos = losistmaterialFindByLossollmaterialIId(sollmatDto
									.getIId());

							BigDecimal istMaterialPlusNachtraeglich = new BigDecimal(
									0);

							for (int j = 0; j < losistmaterialDtos.length; j++) {
								LosistmaterialDto istmaterialDto = losistmaterialDtos[j];
								// wenn Lagerzugang, der wird ausgelassen
								if (Helper.short2boolean(istmaterialDto
										.getBAbgang())) {
									istMaterialPlusNachtraeglich = istMaterialPlusNachtraeglich
											.add(istmaterialDto.getNMenge());
								}

							}

							// Wenn ausgegebenes Material grosser als
							// Sollsatzgroesse
							if (istMaterialPlusNachtraeglich.doubleValue() > sollsatzgroesse
									.doubleValue()) {
								BigDecimal zuReduzieren = Helper
										.rundeKaufmaennisch(
												istMaterialPlusNachtraeglich
														.subtract(sollsatzgroesse),
												4);

								for (int j = losistmaterialDtos.length; j > 0; j--) {
									if (zuReduzieren.doubleValue() > 0) {
										LosistmaterialDto istmaterialDto = losistmaterialDtos[j - 1];

										// wenn Lagerzugang, der wird
										// ausgelassen
										if (Helper.short2boolean(istmaterialDto
												.getBAbgang())) {

											LagerbewegungDto lagerbewegungDto = getLagerFac()
													.getLetzteintrag(
															LocaleFac.BELEGART_LOS,
															istmaterialDto
																	.getIId(),
															null);

											BigDecimal nMengeAbsolut = new BigDecimal(
													0);

											if (zuReduzieren.doubleValue() >= istmaterialDto
													.getNMenge().doubleValue()) {
												nMengeAbsolut = istmaterialDto
														.getNMenge().subtract(
																zuReduzieren);

												nMengeAbsolut = new BigDecimal(
														0);
											} else {
												nMengeAbsolut = istmaterialDto
														.getNMenge().subtract(
																zuReduzieren);
												zuReduzieren = new BigDecimal(0);

											}

											// Korrekturbuchung
											// Belegdatum wird nicht auf
											// heute
											// gesetzt lt. WH 20090115
											getLagerFac()
													.bucheAb(
															LocaleFac.BELEGART_LOS,
															losDto.getIId(),
															istmaterialDto
																	.getIId(),
															sollmatDto
																	.getArtikelIId(),
															nMengeAbsolut,
															lagerbewegungDto
																	.getNVerkaufspreis(),
															istmaterialDto
																	.getLagerIId(),
															(String) null,
															losablieferungDto
																	.getTAendern(),
															theClientDto);

											// Losistmaterial updaten
											updateLosistmaterialMenge(
													istmaterialDto.getIId(),
													nMengeAbsolut, theClientDto);

											zuReduzieren = zuReduzieren
													.subtract(istmaterialDto
															.getNMenge());

										}
									}
								}

							}
						}
					}
				}

			}

			LosablieferungDto[] dtos = losablieferungFindByLosIId(
					losDto.getIId(), false, theClientDto);
			// Wenn es die letzte Ablieferung ist, nur die letzte neu berechnen,
			// ansonsten alle
			if (dtos[dtos.length - 1].getIId().equals(
					losablieferungDto.getIId())) {
				aktualisiereNachtraeglichPreiseAllerLosablieferungen(
						losDto.getIId(), theClientDto, true);
			} else {
				aktualisiereNachtraeglichPreiseAllerLosablieferungen(
						losDto.getIId(), theClientDto, false);
			}

		} else {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_UPDATE_LOSABLIEFERUNG_FEHLER_MENGE,
					new Exception(
							"Menge neu muss > 0 und kleiner der alten Menge sein."));
		}

		return losablieferungDto;

	}

	private void setLosablieferungFromLosablieferungDto(
			Losablieferung losablieferung, LosablieferungDto losablieferungDto) {
		losablieferung.setLosIId(losablieferungDto.getLosIId());
		losablieferung.setNMenge(losablieferungDto.getNMenge());
		losablieferung.setNGestehungspreis(losablieferungDto
				.getNGestehungspreis());
		losablieferung.setNMaterialwert(losablieferungDto.getNMaterialwert());
		losablieferung.setNArbeitszeitwert(losablieferungDto
				.getNArbeitszeitwert());
		losablieferung.setPersonalIIdAendern(losablieferungDto
				.getPersonalIIdAendern());
		losablieferung.setTAendern(losablieferungDto.getTAendern());
		losablieferung.setNMaterialwertdetailliert(losablieferungDto
				.getNMaterialwertdetailliert());
		losablieferung.setNArbeitszeitwertdetailliert(losablieferungDto
				.getNArbeitszeitwertdetailliert());
		losablieferung.setBGestehungspreisneuberechnen(losablieferungDto
				.getBGestehungspreisNeuBerechnen());
		em.merge(losablieferung);
		em.flush();
	}

	private LosablieferungDto assembleLosablieferungDto(
			Losablieferung losablieferung) {
		LosablieferungDto loaDto = LosablieferungDtoAssembler
				.createDto(losablieferung);
		loaDto.setSeriennrChargennrMitMenge(getLagerFac()
				.getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(
						LocaleFac.BELEGART_LOSABLIEFERUNG,
						losablieferung.getIId()));

		return loaDto;
	}

	private LosablieferungDto assembleLosablieferungDtoOhneSnrs(
			Losablieferung losablieferung) {
		LosablieferungDto loaDto = LosablieferungDtoAssembler
				.createDto(losablieferung);

		return loaDto;
	}

	private LosablieferungDto[] assembleLosablieferungDtos(
			Collection<?> losablieferungs) {
		List<LosablieferungDto> list = new ArrayList<LosablieferungDto>();
		if (losablieferungs != null) {
			Iterator<?> iterator = losablieferungs.iterator();
			while (iterator.hasNext()) {
				Losablieferung losablieferung = (Losablieferung) iterator
						.next();
				list.add(assembleLosablieferungDto(losablieferung));
			}
		}
		LosablieferungDto[] returnArray = new LosablieferungDto[list.size()];
		return (LosablieferungDto[]) list.toArray(returnArray);
	}

	private LosablieferungDto[] assembleLosablieferungDtosOhneSnrs(
			Collection<?> losablieferungs) {
		List<LosablieferungDto> list = new ArrayList<LosablieferungDto>();
		if (losablieferungs != null) {
			Iterator<?> iterator = losablieferungs.iterator();
			while (iterator.hasNext()) {
				Losablieferung losablieferung = (Losablieferung) iterator
						.next();
				list.add(assembleLosablieferungDtoOhneSnrs(losablieferung));
			}
		}
		LosablieferungDto[] returnArray = new LosablieferungDto[list.size()];
		return (LosablieferungDto[]) list.toArray(returnArray);
	}

	public LossollmaterialDto getArtikelIIdOffenenSeriennummernBeiGeraeteseriennummer(
			Integer losIId, TheClientDto theClientDto) {
		LossollmaterialDto[] dtos = lossollmaterialFindByLosIId(losIId);

		LossollmaterialDto sollmaterialDto = null;
		for (int i = 0; i < dtos.length; i++) {
			LossollmaterialDto dto = dtos[i];

			if (dto.getArtikelIId() != null) {

				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
						dto.getArtikelIId(), theClientDto);
				if (Helper.short2boolean(aDto.getBSeriennrtragend())) {
					if (sollmaterialDto == null) {
						sollmaterialDto = dto;
					} else {
						return null;
					}
				}

			}

		}
		return sollmaterialDto;
	}

	public ArrayList<String> getOffeneGeraeteSnrEinerSollPosition(
			Integer lossollmaterialIId, TheClientDto theClientDto) {

		LossollmaterialDto sollmatDto = lossollmaterialFindByPrimaryKey(lossollmaterialIId);

		LosistmaterialDto[] istmatDtos = losistmaterialFindByLossollmaterialIId(lossollmaterialIId);

		ArrayList<String> alAufLager = new ArrayList<String>();

		for (int i = 0; i < istmatDtos.length; i++) {

			List<SeriennrChargennrMitMengeDto> l = getLagerFac()
					.getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(
							LocaleFac.BELEGART_LOS, istmatDtos[i].getIId());

			for (int j = 0; j < l.size(); j++) {

				alAufLager.add(l.get(j).getCSeriennrChargennr());
			}

		}

		// Abgeliferte

		LosablieferungDto[] losabDtos = losablieferungFindByLosIId(
				sollmatDto.getLosIId(), false, theClientDto);
		for (int i = 0; i < losabDtos.length; i++) {
			GeraetesnrDto[] gsnrDtos = getLagerFac()
					.getGeraeteseriennummerEinerLagerbewegung(
							LocaleFac.BELEGART_LOSABLIEFERUNG,
							losabDtos[i].getIId(),
							losabDtos[i].getSeriennrChargennrMitMenge().get(0)
									.getCSeriennrChargennr());

			for (int j = 0; j < gsnrDtos.length; j++) {
				GeraetesnrDto snrDto = gsnrDtos[j];

				if (snrDto.getArtikelIId().equals(sollmatDto.getArtikelIId())) {

					// SNR aus Vorschlag entfernen
					for (int u = 0; u < alAufLager.size(); u++) {
						if (alAufLager.get(u).equals(snrDto.getCSnr())) {
							alAufLager.remove(u);
							break;
						}

					}

				}
			}

		}

		return alAufLager;
	}

	public List<SeriennrChargennrMitMengeDto> getOffenenSeriennummernBeiGeraeteseriennummer(
			Integer losIId, TheClientDto theClientDto) {

		LossollmaterialDto sollmaterialDto = getArtikelIIdOffenenSeriennummernBeiGeraeteseriennummer(
				losIId, theClientDto);
		if (sollmaterialDto != null) {
			ArrayList<SeriennrChargennrMitMengeDto> al = new ArrayList<SeriennrChargennrMitMengeDto>();

			LosistmaterialDto[] istmaterialDtos = losistmaterialFindByLossollmaterialIId(sollmaterialDto
					.getIId());
			for (int i = 0; i < istmaterialDtos.length; i++) {
				List<SeriennrChargennrMitMengeDto> lospos = getLagerFac()
						.getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(
								LocaleFac.BELEGART_LOS,
								istmaterialDtos[i].getIId());
				al.addAll(lospos);
			}

			ArrayList<SeriennrChargennrMitMengeDto> abgelieferteSNrs = new ArrayList<SeriennrChargennrMitMengeDto>();

			LosablieferungDto[] ablDtos = losablieferungFindByLosIId(losIId,
					false, theClientDto);
			for (int i = 0; i < ablDtos.length; i++) {
				if (ablDtos[i].getSeriennrChargennrMitMenge() != null) {
					abgelieferteSNrs.addAll(ablDtos[i]
							.getSeriennrChargennrMitMenge());
				}
			}

			ArrayList<SeriennrChargennrMitMengeDto> uebrigeSNrs = new ArrayList<SeriennrChargennrMitMengeDto>();
			for (int i = 0; i < al.size(); i++) {
				String snr = al.get(i).getCSeriennrChargennr();

				boolean bGefunden = false;
				for (int j = 0; j < abgelieferteSNrs.size(); j++) {
					if (abgelieferteSNrs.get(j).getCSeriennrChargennr()
							.equals(snr)) {
						bGefunden = true;
						break;
					}
				}
				if (bGefunden == false) {
					uebrigeSNrs.add(al.get(i));
				}
			}

			return uebrigeSNrs;
		} else {
			return null;
		}

	}

	public BigDecimal getAnzahlInFertigung(Integer artikelIId,
			java.sql.Date tAbDatum, TheClientDto theClientDto) {

		BigDecimal anzahl = new BigDecimal(0);
		Session session = null;

		StuecklisteDto stuecklisteDto = getStuecklisteFac()
				.stuecklisteFindByMandantCNrArtikelIIdOhneExc(artikelIId,
						theClientDto);
		if (stuecklisteDto != null) {
			// --------------------------------------------------------------
			// ---------
			session = FLRSessionFactory.getFactory().openSession();

			Criteria crit = session.createCriteria(FLRLos.class);

			String[] stati = new String[5];
			stati[0] = FertigungFac.STATUS_ANGELEGT;
			stati[1] = FertigungFac.STATUS_IN_PRODUKTION;
			stati[2] = FertigungFac.STATUS_AUSGEGEBEN;
			stati[3] = FertigungFac.STATUS_TEILERLEDIGT;
			stati[4] = FertigungFac.STATUS_GESTOPPT;

			crit.add(Restrictions.in(FertigungFac.FLR_LOS_STATUS_C_NR, stati));
			crit.add(Restrictions.eq(
					FertigungFac.FLR_INTERNE_BESTELLUNG_STUECKLISTE_I_ID,
					stuecklisteDto.getIId()));
			if (tAbDatum != null) {
				crit.add(Restrictions.ge(
						FertigungFac.FLR_LOS_T_PRODUKTIONSENDE, tAbDatum));
			}

			List<?> resultList = crit.list();
			Iterator<?> resultListIterator = resultList.iterator();
			while (resultListIterator.hasNext()) {
				FLRLos flrLos = (FLRLos) resultListIterator.next();
				if (flrLos.getStatus_c_nr().equals(
						FertigungFac.STATUS_TEILERLEDIGT)) {
					// Erledigte Menge bestimmen.
					BigDecimal bdErledigt = getErledigteMenge(flrLos.getI_id(),
							theClientDto);
					// Wenn noch was offen ist (es koennte auch
					// Ueberlieferungen geben).
					if (flrLos.getN_losgroesse().compareTo(bdErledigt) > 0) {
						anzahl = anzahl.add(flrLos.getN_losgroesse().subtract(
								bdErledigt));
					}
				} else {
					anzahl = anzahl.add(flrLos.getN_losgroesse());
				}
			}
		}

		return anzahl;
	}

	public BigDecimal getAnzahlInFertigung(Integer artikelIId,
			TheClientDto theClientDto) {
		return getAnzahlInFertigung(artikelIId, null, theClientDto);
	}

	public ArrayList<LosDto> getLoseInFertigung(Integer artikelIId,
			TheClientDto theClientDto) {
		Session session = null;

		ArrayList<LosDto> lose = new ArrayList<LosDto>();

		try {
			StuecklisteDto stuecklisteDto = getStuecklisteFac()
					.stuecklisteFindByMandantCNrArtikelIIdOhneExc(artikelIId,
							theClientDto);
			if (stuecklisteDto != null) {
				// --------------------------------------------------------------
				// ---------
				session = FLRSessionFactory.getFactory().openSession();

				Criteria crit = session.createCriteria(FLRLos.class);

				String[] stati = new String[4];
				stati[0] = FertigungFac.STATUS_ANGELEGT;
				stati[1] = FertigungFac.STATUS_IN_PRODUKTION;
				stati[2] = FertigungFac.STATUS_AUSGEGEBEN;
				stati[3] = FertigungFac.STATUS_TEILERLEDIGT;

				crit.add(Restrictions.in(FertigungFac.FLR_LOS_STATUS_C_NR,
						stati));
				crit.add(Restrictions.eq(
						FertigungFac.FLR_INTERNE_BESTELLUNG_STUECKLISTE_I_ID,
						stuecklisteDto.getIId()));

				List<?> resultList = crit.list();
				Iterator<?> resultListIterator = resultList.iterator();
				while (resultListIterator.hasNext()) {
					FLRLos flrLos = (FLRLos) resultListIterator.next();

					LosDto losDto = getFertigungFac().losFindByPrimaryKey(
							flrLos.getI_id());

					if (flrLos.getStatus_c_nr().equals(
							FertigungFac.STATUS_TEILERLEDIGT)) {
						// Erledigte Menge bestimmen.
						BigDecimal bdErledigt = getErledigteMenge(
								flrLos.getI_id(), theClientDto);
						// Wenn noch was offen ist (es koennte auch
						// Ueberlieferungen geben).
						if (flrLos.getN_losgroesse().compareTo(bdErledigt) > 0) {

							lose.add(losDto);
						}
					} else {
						lose.add(losDto);
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} finally {
			closeSession(session);
		}
		return lose;
	}

	public BigDecimal getErledigteMenge(Integer losIId,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// Optimieren
		String sQuery = "select sum(la.n_menge) from FLRLosablieferung la WHERE la.los_i_id="
				+ losIId;

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		org.hibernate.Query summe = session.createQuery(sQuery);
		List<?> resultList = summe.list();
		Iterator<?> resultListIterator = resultList.iterator();
		BigDecimal bdErledigt = (BigDecimal) resultListIterator.next();
		session.close();

		if (bdErledigt != null) {
			return bdErledigt;
		} else {
			return new BigDecimal(0);
		}

	}

	public void stoppeProduktion(Integer losIId, TheClientDto theClientDto)
			throws EJBExceptionLP {

		// try {
		Los los = em.find(Los.class, losIId);
		if (los == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		if (los.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT, "");
		} else if (los.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN,
					"");
		} else if (los.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
					"");
		} else if (los.getStatusCNr().equals(FertigungFac.STATUS_GESTOPPT)) {
			return;
		} else {
			los.setStatusCNr(FertigungFac.STATUS_GESTOPPT);
			los.setPersonalIIdProduktionsstop(theClientDto.getIDPersonal());
			los.setTProduktionsstop(new Timestamp(System.currentTimeMillis()));
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public void stoppeProduktionRueckgaengig(Integer losIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// try {
		Los los = em.find(Los.class, losIId);
		if (los == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		if (los.getStatusCNr().equals(FertigungFac.STATUS_GESTOPPT)) {
			LosablieferungDto[] la = losablieferungFindByLosIId(losIId, false,
					theClientDto);
			if (la.length == 0) {
				los.setStatusCNr(FertigungFac.STATUS_IN_PRODUKTION);
			} else {
				los.setStatusCNr(FertigungFac.STATUS_TEILERLEDIGT);
			}
			los.setPersonalIIdProduktionsstop(null);
			los.setTProduktionsstop(null);
		} else if (los.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT, "");
		} else if (los.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN,
					"");
		} else if (los.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
					"");
		} else {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_AUSGEGEBEN,
					"");
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	private BigDecimal getErledigterMaterialwertEinerSollpositionNEU(
			BigDecimal bdLosgroesse, BigDecimal bdErledigteMenge,
			TheClientDto theClientDto, LosDto losDto, BigDecimal bdAusgegeben,
			BigDecimal gesamtpreis, BigDecimal sollmenge) {

		// wenn volle menge erledigt oder sogar mehr oder Los=erledigt
		if (bdLosgroesse.compareTo(bdErledigteMenge) <= 0
				|| losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			// dann alles
			return gesamtpreis;
		} else {
			// Sollsatzgroesse berechnen
			BigDecimal bdSollsatzgroesse = sollmenge.multiply(bdErledigteMenge)
					.divide(bdLosgroesse, 6, BigDecimal.ROUND_HALF_EVEN);
			// weniger oder gleich wie sollmenge ausgegeben
			if (bdAusgegeben.compareTo(bdSollsatzgroesse) <= 0) {
				// dann alles
				return gesamtpreis;
			}
			// wenn mehr ausgegeben
			else {

				BigDecimal bdEinzelpreis = new BigDecimal(0);

				if (bdAusgegeben.doubleValue() > 0) {
					bdEinzelpreis = gesamtpreis.divide(bdAusgegeben, 4,
							BigDecimal.ROUND_HALF_EVEN);
				}

				// dann mit sollsatzgroesse
				return bdSollsatzgroesse.multiply(bdEinzelpreis);
			}
		}
	}

	public BigDecimal getErledigterMaterialwertNEU(LosDto losDto,
			TheClientDto theClientDto) {

		// Neue Funktion, die Adi gebaut hat: Liefert den erledigten
		// Materialwert in einem Query

		BigDecimal bdErledigteMenge = getErledigteMenge(losDto.getIId(),
				theClientDto);

		// Neue Funktion, die Adi gebaut hat: Liefert den erledigten
		// Materialwert in einem Query

		Query query = em.createNamedQuery("getErledigterMaterialwert");
		query.setParameter("losiid", losDto.getIId());
		Collection<?> cl = query.getResultList();
		Iterator it = cl.iterator();

		BigDecimal bdWert = new BigDecimal(0);
		while (it.hasNext()) {
			Erledigtermaterialwert pos = (Erledigtermaterialwert) it.next();

			bdWert = bdWert.add(getErledigterMaterialwertEinerSollpositionNEU(
					losDto.getNLosgroesse(), bdErledigteMenge, theClientDto,
					losDto, pos.getNAusmenge(), pos.getNPreis(),
					pos.getNSollmenge()));
		}

		return bdWert;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int wiederholendeLoseAnlegen(TheClientDto theClientDto) {

		int iAnzahlAngelegterLose = 0;
		DateFormatSymbols symbols = new DateFormatSymbols(
				theClientDto.getLocUi());
		String[] defaultMonths = symbols.getMonths();
		int iStandarddurchlaufzeit = 0;
		try {
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FERTIGUNG,
							ParameterFac.INTERNEBESTELLUNG_DEFAULTDURCHLAUFZEIT);
			iStandarddurchlaufzeit = ((Integer) parameter.getCWertAsObject())
					.intValue();
		} catch (RemoteException ex2) {
			throwEJBExceptionLPRespectOld(ex2);
		}

		Session session = FLRSessionFactory.getFactory().openSession();

		Criteria crit = session.createCriteria(FLRWiederholendelose.class);

		crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));
		crit.add(Restrictions.eq(
				FertigungFac.FLR_WIEDERHOLENDELOSE_B_VERSTECKT,
				Helper.boolean2Short(false)));

		List<?> resultList = crit.list();
		Iterator<?> resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {
			FLRWiederholendelose flrWiederholendelose = (FLRWiederholendelose) resultListIterator
					.next();

			// Naechster faelliger Termin nach Heute
			Calendar cBeginn = Calendar.getInstance();
			cBeginn.setTimeInMillis(flrWiederholendelose.getT_termin()
					.getTime());

			String intervall = flrWiederholendelose
					.getAuftragwiederholungsintervall_c_nr();

			Timestamp tHeute = Helper.cutTimestamp(new Timestamp(System
					.currentTimeMillis()));

			while (cBeginn.getTimeInMillis() < tHeute.getTime()) {

				if (intervall
						.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_2WOECHENTLICH)) {
					cBeginn.set(Calendar.DAY_OF_MONTH,
							cBeginn.get(Calendar.DAY_OF_MONTH) + 14);
				} else if (intervall
						.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_WOECHENTLICH)) {
					cBeginn.set(Calendar.DAY_OF_MONTH,
							cBeginn.get(Calendar.DAY_OF_MONTH) + 7);
				}

				if (intervall
						.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_JAHR)) {
					cBeginn.set(Calendar.YEAR, cBeginn.get(Calendar.YEAR) + 1);
				} else if (intervall
						.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_2JAHR)) {
					cBeginn.set(Calendar.YEAR, cBeginn.get(Calendar.YEAR) + 2);
				} else if (intervall
						.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_3JAHR)) {
					cBeginn.set(Calendar.YEAR, cBeginn.get(Calendar.YEAR) + 3);
				} else if (intervall
						.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_4JAHR)) {
					cBeginn.set(Calendar.YEAR, cBeginn.get(Calendar.YEAR) + 4);
				} else if (intervall
						.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_5JAHR)) {
					cBeginn.set(Calendar.YEAR, cBeginn.get(Calendar.YEAR) + 5);
				} else if (intervall
						.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_MONATLICH)) {
					cBeginn.set(Calendar.MONTH, cBeginn.get(Calendar.MONTH) + 1);
				} else if (intervall
						.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_QUARTAL)) {
					cBeginn.set(Calendar.MONTH, cBeginn.get(Calendar.MONTH) + 3);
				}
			}

			Timestamp tBeginndatumFuerLos = new Timestamp(
					cBeginn.getTimeInMillis());
			// Voreilende Tage abziehen

			String monatsname = defaultMonths[cBeginn.get(Calendar.MONTH)];
			int iJahr = cBeginn.get(Calendar.YEAR);

			int iTageVoreilend = flrWiederholendelose.getI_tagevoreilend();
			cBeginn.set(Calendar.DAY_OF_MONTH,
					cBeginn.get(Calendar.DAY_OF_MONTH) - iTageVoreilend);

			Timestamp tAnlagedatum = new Timestamp(cBeginn.getTimeInMillis());

			if (tAnlagedatum.before(tHeute) || tAnlagedatum.equals(tHeute)) {
				// try {
				Query query = em
						.createNamedQuery("LosfindWiederholendeloseIIdTProduktionsbeginnMandantCNr");
				query.setParameter(1, flrWiederholendelose.getI_id());
				query.setParameter(2, tBeginndatumFuerLos);
				query.setParameter(3, theClientDto.getMandant());
				Collection<?> cl = query.getResultList();
				// if (cl.isEmpty()) {
				// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, null);
				// }
				LosDto[] lose = assembleLosDtos(cl);

				// Wenn noch nicht angelegt
				if (lose.length == 0) {

					WiederholendeloseDto dto = wiederholendeloseFindByPrimaryKey(flrWiederholendelose
							.getI_id());
					String projektname = "";
					if (dto.getCProjekt() != null) {
						projektname += dto.getCProjekt() + " ";
					}
					projektname += monatsname + " " + iJahr;

					if (projektname.length() > 50) {
						projektname = projektname.substring(0, 49);
					}

					LosDto losDto = new LosDto();
					losDto.setWiederholendeloseIId(dto.getIId());
					losDto.setCProjekt(projektname);
					losDto.setFertigungsgruppeIId(dto.getFertigungsgruppeIId());
					losDto.setKostenstelleIId(dto.getKostenstelleIId());
					losDto.setLagerIIdZiel(dto.getLagerIIdZiel());
					losDto.setMandantCNr(theClientDto.getMandant());
					losDto.setNLosgroesse(dto.getNLosgroesse());
					losDto.setPartnerIIdFertigungsort(dto
							.getPartnerIIdFertigungsort());
					losDto.setStuecklisteIId(dto.getStuecklisteIId());
					losDto.setTProduktionsbeginn(new java.sql.Date(
							tBeginndatumFuerLos.getTime()));

					// Produktionsende

					try {

						int laufzeit = iStandarddurchlaufzeit;
						if (dto.getStuecklisteIId() != null) {
							StuecklisteDto stuecklisteDto = getStuecklisteFac()
									.stuecklisteFindByPrimaryKey(
											dto.getStuecklisteIId(),
											theClientDto);
							if (stuecklisteDto.getNDefaultdurchlaufzeit() != null) {
								laufzeit = stuecklisteDto
										.getNDefaultdurchlaufzeit().intValue();
							}
						}
						Calendar cTemp = Calendar.getInstance();
						cTemp.setTimeInMillis(tBeginndatumFuerLos.getTime());
						cTemp.set(Calendar.DAY_OF_MONTH,
								cTemp.get(Calendar.DAY_OF_MONTH) + laufzeit);
						losDto.setTProduktionsende(new java.sql.Date(cTemp
								.getTime().getTime()));
						context.getBusinessObject(FertigungFac.class)
								.createLos(losDto, theClientDto);
					} catch (RemoteException ex1) {
						throwEJBExceptionLPRespectOld(ex1);
					}

					iAnzahlAngelegterLose++;
				}
				// }
				// catch (FinderException ex) {
				// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
				// }

			}

		}

		return iAnzahlAngelegterLose;
	}

	public BigDecimal getErledigteArbeitszeitEinerLosablieferung(
			Integer losablieferungIId, boolean bWertOderZeit,
			boolean bMitMaschinenZeit, TheClientDto theClientDto)
			throws EJBExceptionLP {
		return getErledigteArbeitszeitEinerLosablieferung(losablieferungIId,
				bWertOderZeit, bMitMaschinenZeit, theClientDto, false);
	}

	/**
	 * Ist-Arbeitszeit fuer eine Ablieferung pro abgelieferter Einheit
	 * bestimmen.
	 * 
	 * @param losablieferungIId
	 *            Integer
	 * @param bWertOderZeit
	 *            boolean fuer Wert = true, fuer Zeit = false
	 * @param theClientDto
	 *            String
	 * @return BigDecimal
	 * @throws EJBExceptionLP
	 */
	private BigDecimal getErledigteArbeitszeitEinerLosablieferung(
			Integer losablieferungIId, boolean bWertOderZeit,
			boolean bMitMaschinenZeit, TheClientDto theClientDto,
			boolean bRekursiv) throws EJBExceptionLP {
		BigDecimal bdErgebnis = new BigDecimal(0);
		LosablieferungDto losablieferungDto = losablieferungFindByPrimaryKey(
				losablieferungIId, false, theClientDto);
		try {
			// Alle Ablieferungen auf dieses Los aufsteigend nach datum sortiert

			LosablieferungDto[] abl = losablieferungFindByLosIId(
					losablieferungDto.getLosIId(), false, theClientDto);
			// Rueckwaertsschleife, da die werte der vorgaenger berechnet werden
			// muessen (rekursiv)
			for (int i = abl.length - 1; i > 0; i--) {
				// wenn ich die aktuelle gefunden hab, subtrahier ich den wert
				// des vorgaengers
				if (abl[i].getIId().equals(losablieferungIId)) {
					bdErgebnis = bdErgebnis
							.subtract(getErledigteArbeitszeitEinerLosablieferung(
									abl[i - 1].getIId(), bWertOderZeit,
									bMitMaschinenZeit, theClientDto, true)
									.multiply(abl[i - 1].getNMenge()));
					break;
				}
			}
			// ------------------------------------------------------------------
			// ----

			BigDecimal summeAblieferungen = new BigDecimal(0);

			// Sollsatzfaktor bestimmen
			BigDecimal bdErledigt = new BigDecimal(0);
			for (int i = 0; i < abl.length; i++) {
				summeAblieferungen = summeAblieferungen.add(abl[i].getNMenge());
				if (!abl[i].getTAendern()
						.after(losablieferungDto.getTAendern())) {
					bdErledigt = bdErledigt.add(abl[i].getNMenge());
				}
			}

			LosDto losDto = losFindByPrimaryKey(losablieferungDto.getLosIId());
			// Wenn Los ueberliefert oder Erledigt, dann zaehlen alle Zeidaten
			// zum Arbeitszeitwert
			boolean bAlleZeitenZaehlenFuerArbeitswert = false;
			if (summeAblieferungen.doubleValue() >= losDto.getNLosgroesse()
					.doubleValue()
					|| losDto.getStatusCNr().equals(LocaleFac.STATUS_ERLEDIGT)) {
				if (bRekursiv == false) {
					bAlleZeitenZaehlenFuerArbeitswert = true;
				}
			}

			BigDecimal bdFaktor = bdErledigt.divide(losDto.getNLosgroesse(),
					10, BigDecimal.ROUND_HALF_EVEN);
			// ------------------------------------------------------------------
			// ----
			// Sollzeiten
			LossollarbeitsplanDto[] soll = getFertigungFac()
					.lossollarbeitsplanFindByLosIId(
							losablieferungDto.getLosIId());
			// Sollzeiten nach Artikel verdichten und mit Sollsatzfaktor
			// multiplizieren
			HashMap<Integer, BigDecimal> listSollVerdichtet = new HashMap<Integer, BigDecimal>();
			for (int i = 0; i < soll.length; i++) {
				BigDecimal bdBisher = listSollVerdichtet.get(soll[i]
						.getArtikelIIdTaetigkeit());
				if (bdBisher == null) {
					listSollVerdichtet.put(soll[i].getArtikelIIdTaetigkeit(),
							soll[i].getNGesamtzeit().multiply(bdFaktor));
				} else {
					listSollVerdichtet.put(
							soll[i].getArtikelIIdTaetigkeit(),
							bdBisher.add(soll[i].getNGesamtzeit().multiply(
									bdFaktor)));
				}
			}
			// ------------------------------------------------------------------
			// ----
			// gebuchte Zeiten holen
			ParametermandantDto parametermandantDto = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FERTIGUNG,
							ParameterFac.PARAMETER_ISTZEITEN_GLEICH_SOLLZEITEN);
			boolean bSollGleichIstzeiten = false;
			if (((java.lang.Boolean) parametermandantDto.getCWertAsObject())
					.booleanValue() == true) {
				bSollGleichIstzeiten = true;
			}
			// Maschinenzeiten
			AuftragzeitenDto[] zeitenMaschine = null;
			AuftragzeitenDto[] zeiten = null;
			if (bSollGleichIstzeiten == false) {

				// Maschinenzeiten
				zeitenMaschine = getZeiterfassungFac()
						.getAllMaschinenzeitenEinesBeleges(
								losablieferungDto.getLosIId(), null, null,
								null, theClientDto);
				// "normale" Zeiten
				zeiten = getZeiterfassungFac().getAllZeitenEinesBeleges(
						LocaleFac.BELEGART_LOS, losablieferungDto.getLosIId(),
						null, null, null, null, false, false, theClientDto);
			} else {
				zeiten = new AuftragzeitenDto[listSollVerdichtet.size()];
				zeitenMaschine = new AuftragzeitenDto[0];
				bAlleZeitenZaehlenFuerArbeitswert = true;
				int row = 0;
				for (Iterator<?> iter = listSollVerdichtet.keySet().iterator(); iter
						.hasNext();) {
					Integer artikelIId = (Integer) iter.next();
					BigDecimal gesamtzeit = listSollVerdichtet.get(artikelIId);
					AuftragzeitenDto az = new AuftragzeitenDto();
					az.setArtikelIId(artikelIId);
					az.setDdDauer(new Double(gesamtzeit.doubleValue()));
					BigDecimal bdPreis = getLagerFac()
							.getGemittelterGestehungspreisAllerLaegerEinesMandanten(
									artikelIId, theClientDto);
					az.setBdKosten(gesamtzeit.multiply(bdPreis));
					zeiten[row] = az;
					row++;
				}
			}
			// ------------------------------------------------------------------
			// ----
			// relevante Ist-Zeiten nach Ident verdichten
			HashMap<Integer, BigDecimal> listIstWert = new HashMap<Integer, BigDecimal>();
			HashMap<Integer, BigDecimal> listIstZeit = new HashMap<Integer, BigDecimal>();
			if (bMitMaschinenZeit) {
				for (int i = 0; i < zeitenMaschine.length; i++) {
					// Wenn vor der Ablieferung gebucht, dann addieren
					if ((zeitenMaschine[i].getTsEnde() != null && zeitenMaschine[i]
							.getTsEnde()
							.before(losablieferungDto.getTAendern()))
							|| bAlleZeitenZaehlenFuerArbeitswert == true) {
						BigDecimal bdBisherWert = listIstWert
								.get(zeitenMaschine[i].getArtikelIId());
						BigDecimal bdBisherZeit = listIstZeit
								.get(zeitenMaschine[i].getArtikelIId());
						if (bdBisherWert == null) {
							listIstWert.put(zeitenMaschine[i].getArtikelIId(),
									zeitenMaschine[i].getBdKosten());
							listIstZeit.put(
									zeitenMaschine[i].getArtikelIId(),
									new BigDecimal(zeitenMaschine[i]
											.getDdDauer()));
						} else {
							listIstWert.put(zeitenMaschine[i].getArtikelIId(),
									bdBisherWert.add(zeitenMaschine[i]
											.getBdKosten()));
							listIstZeit.put(zeitenMaschine[i].getArtikelIId(),
									bdBisherZeit.add(new BigDecimal(
											zeitenMaschine[i].getDdDauer())));
						}
					}
				}
			}
			// "normale" Zeiten
			for (int i = 0; i < zeiten.length; i++) {
				// Wenn vor der Ablieferung gebucht, dann addieren
				if ((zeiten[i].getTsEnde() != null && zeiten[i].getTsEnde()
						.before(losablieferungDto.getTAendern()))
						|| bAlleZeitenZaehlenFuerArbeitswert == true) {
					BigDecimal bdBisherWert = listIstWert.get(zeiten[i]
							.getArtikelIId());
					BigDecimal bdBisherZeit = listIstZeit.get(zeiten[i]
							.getArtikelIId());
					if (bdBisherWert == null) {
						listIstWert.put(zeiten[i].getArtikelIId(),
								zeiten[i].getBdKosten());
						listIstZeit.put(zeiten[i].getArtikelIId(),
								new BigDecimal(zeiten[i].getDdDauer()));
					} else {
						listIstWert.put(zeiten[i].getArtikelIId(),
								bdBisherWert.add(zeiten[i].getBdKosten()));
						listIstZeit.put(zeiten[i].getArtikelIId(), bdBisherZeit
								.add(new BigDecimal(zeiten[i].getDdDauer())));
					}
				}
			}
			// ------------------------------------------------------------------
			// ----
			for (Iterator<?> iter = listIstZeit.keySet().iterator(); iter
					.hasNext();) {
				Integer artikelIId = (Integer) iter.next();
				// Wenn das Los erledigt ist, dann zaehlen alle
				if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)
						|| (bdErledigt.doubleValue() >= losDto.getNLosgroesse()
								.doubleValue())) {
					if (bWertOderZeit) {
						bdErgebnis = bdErgebnis
								.add(listIstWert.get(artikelIId));
					} else {
						bdErgebnis = bdErgebnis
								.add(listIstZeit.get(artikelIId));
					}
				}
				// nicht vollstaendig erledigte Lose kriegen hoechstens die
				// anhand sollsatzgroesse errechnete zeit
				else {
					BigDecimal bdIstZeit = listIstZeit.get(artikelIId);
					BigDecimal bdSollZeit = listSollVerdichtet.get(artikelIId);
					if (bdSollZeit == null) {
						// nothing here
						// Taetigkeiten, die im Sollarbeitsplan nicht enthalten
						// waren, werden hier noch nicht gezaehlt
					} else {
						// Ist liegt unter Soll -> ganzer Wert bzw. Zeit
						if (bdSollZeit.compareTo(bdIstZeit) >= 0) {
							if (bWertOderZeit) {
								bdErgebnis = bdErgebnis.add(listIstWert
										.get(artikelIId));
							} else {
								bdErgebnis = bdErgebnis.add(listIstZeit
										.get(artikelIId));
							}
						}
						// Sollsatzzeit ueberschritten
						else {
							if (bWertOderZeit) {
								bdErgebnis = bdErgebnis.add(listIstWert
										.get(artikelIId)
										.multiply(bdSollZeit)
										.divide(bdIstZeit, 4,
												BigDecimal.ROUND_HALF_EVEN));
							} else {
								bdErgebnis = bdErgebnis.add(bdSollZeit);
							}
						}
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		if (losablieferungDto.getNMenge().doubleValue() != 0) {

			return bdErgebnis.divide(losablieferungDto.getNMenge(), 4,
					BigDecimal.ROUND_HALF_EVEN);
		} else {
			return new BigDecimal(0);
		}
	}

	public AuftragNachkalkulationDto getWerteAusUnterlosen(LosDto losDto,
			BigDecimal bdMenge, AuftragNachkalkulationDto abNachkalkulationDto,
			TheClientDto theClientDto) {

		LossollmaterialDto[] sollmat = lossollmaterialFindByLosIId(losDto
				.getIId());

		try {

			BigDecimal bdWert = new BigDecimal(0);
			for (int i = 0; i < sollmat.length; i++) {
				LossollmaterialDto lossollmaterialDto = sollmat[i];
				LosistmaterialDto[] istMatDtos = losistmaterialFindByLossollmaterialIId(lossollmaterialDto
						.getIId());

				for (int j = 0; j < istMatDtos.length; j++) {
					LosistmaterialDto istMatDto = istMatDtos[j];
					Query query = em
							.createNamedQuery("LagerbewegungfindByBelegartCNrBelegartPositionIId");
					query.setParameter(1, LocaleFac.BELEGART_LOS);
					query.setParameter(2, istMatDto.getIId());
					Collection cl = query.getResultList();

					Iterator it = cl.iterator();

					while (it.hasNext()) {
						Lagerbewegung l = (Lagerbewegung) it.next();

						LagerabgangursprungDto[] dtos = getLagerFac()
								.lagerabgangursprungFindByLagerbewegungIIdBuchung(
										l.getIIdBuchung());
						if (l.getNMenge().doubleValue() > 0) {
							for (int m = 0; m < dtos.length; m++) {
								// nun die Ursprungsbuchungen suchen
								Session session2 = FLRSessionFactory
										.getFactory().openSession();
								String sQuery2 = "from FLRLagerbewegung lagerbewegung WHERE lagerbewegung.i_id_buchung="
										+ dtos[m].getILagerbewegungidursprung()
										+ " order by lagerbewegung.t_buchungszeit DESC";
								org.hibernate.Query ursrungsbuchung = session2
										.createQuery(sQuery2);
								ursrungsbuchung.setMaxResults(1);

								java.util.List resultList2 = ursrungsbuchung
										.list();

								Iterator itUrsprung = resultList2.iterator();

								while (itUrsprung.hasNext()) {
									com.lp.server.artikel.fastlanereader.generated.FLRLagerbewegung lagerbewegung_ursprung = (com.lp.server.artikel.fastlanereader.generated.FLRLagerbewegung) itUrsprung
											.next();

									// Wenn diese aus einer Losablieferung kommt
									if (lagerbewegung_ursprung
											.getC_belegartnr()
											.equals(LocaleFac.BELEGART_LOSABLIEFERUNG)) {

										LosablieferungDto laDto = losablieferungFindByPrimaryKey(
												lagerbewegung_ursprung
														.getI_belegartpositionid(),
												true, theClientDto);

										LosDto losDtoUnterlos = losFindByPrimaryKey(laDto
												.getLosIId());
										BigDecimal sollsatzgroesse = lossollmaterialDto
												.getNMenge()
												.divide(losDto.getNLosgroesse(),
														4,
														BigDecimal.ROUND_HALF_EVEN);

										BigDecimal azWert = laDto
												.getNArbeitszeitwertdetailliert()
												.multiply(
														dtos[m].getNVerbrauchtemenge());

										abNachkalkulationDto
												.setBdGestehungswertmaterialist(abNachkalkulationDto
														.getBdGestehungswertmaterialist()
														.subtract(azWert));
										abNachkalkulationDto
												.setBdGestehungswertarbeitist(abNachkalkulationDto
														.getBdGestehungswertarbeitist()
														.add(azWert));

										System.out
												.println("Losablieferung Unterlos  :\t"
														+ losDtoUnterlos
																.getCNr()
														+ "\t AZ-Wert:\t"
														+ Helper.fitString2LengthAlignRight(
																Helper.rundeKaufmaennisch(
																		azWert,
																		2)
																		+ "",
																15, ' '));

										BigDecimal bdGesamtAbgeliefert = getErledigteMenge(
												laDto.getLosIId(), theClientDto);
										Double dPers = getZeiterfassungFac()
												.getSummeZeitenEinesBeleges(
														LocaleFac.BELEGART_LOS,
														losDtoUnterlos.getIId(),
														null, null, null, null,
														theClientDto);

										BigDecimal arbeitszeitsoll = new BigDecimal(
												0);

										BigDecimal maschinenzeitsoll = new BigDecimal(
												0);
										LossollarbeitsplanDto[] sollarbeitsplanDtos = getFertigungFac()
												.lossollarbeitsplanFindByLosIId(
														losDtoUnterlos.getIId());

										for (int u = 0; u < sollarbeitsplanDtos.length; u++) {
											LossollarbeitsplanDto sollarbeitsplanDto = sollarbeitsplanDtos[u];

											BigDecimal menge = sollarbeitsplanDto
													.getNGesamtzeit()
													.divide(losDtoUnterlos
															.getNLosgroesse(),
															4,
															BigDecimal.ROUND_HALF_EVEN)
													.multiply(bdMenge);
											/*
											 * ArtikelDto artikelDto =
											 * getArtikelFac()
											 * .artikelFindByPrimaryKeySmall(
											 * sollarbeitsplanDto
											 * .getArtikelIIdTaetigkeit(),
											 * theClientDto);
											 * 
											 * myLogger.warn("Los:" +
											 * losDto.getCNr() + " Unterlos:" +
											 * losDtoUnterlos.getCNr() + " AZ:"
											 * + artikelDto.getCNr() + " Zeit:"
											 * + Helper.rundeKaufmaennisch(
											 * menge, 4));
											 */

											if (sollarbeitsplanDto
													.getMaschineIId() == null) {
												arbeitszeitsoll = arbeitszeitsoll
														.add(menge);
											} else {
												maschinenzeitsoll = maschinenzeitsoll
														.add(menge);
												if (!Helper
														.short2boolean(sollarbeitsplanDto
																.getBNurmaschinenzeit())) {
													arbeitszeitsoll = arbeitszeitsoll
															.add(menge);
												}

											}
										}

										abNachkalkulationDto
												.setDdArbeitszeitsoll(abNachkalkulationDto
														.getDdArbeitszeitsoll()
														+ arbeitszeitsoll
																.doubleValue());
										abNachkalkulationDto
												.setDdMaschinenzeitsoll(abNachkalkulationDto
														.getDdMaschinenzeitsoll()
														+ maschinenzeitsoll
																.doubleValue());

										// Zeit duch die Gesamtablieferungen
										// dividieren und mal bMenge
										dPers = dPers
												/ bdGesamtAbgeliefert
														.doubleValue()
												* bdMenge.doubleValue();
										abNachkalkulationDto
												.setDdArbeitszeitist(abNachkalkulationDto
														.getDdArbeitszeitist()
														+ dPers);

										Double dMasch = getZeiterfassungFac()
												.getSummeMaschinenZeitenEinesBeleges(
														losDtoUnterlos.getIId(),
														null, null,
														theClientDto);

										dMasch = dMasch
												/ bdGesamtAbgeliefert
														.doubleValue()
												* bdMenge.doubleValue();
										abNachkalkulationDto
												.setDdMaschinenzeitist(abNachkalkulationDto
														.getDdMaschinenzeitist()
														+ dMasch);

										/*
										 * System.out.println("Los: " +
										 * losDto.getCNr() + " Stueckliste: " +
										 * stklDto.getArtikelDto().getCNr() +
										 * " UnterlosLos: " +
										 * losDtoUnterlos.getCNr() +
										 * " Artikel: " + aDto.getCNr() +
										 * " Menge:" + bdMenge);
										 */

										abNachkalkulationDto = getWerteAusUnterlosen(
												losDtoUnterlos,
												sollsatzgroesse.multiply(dtos[m]
														.getNVerbrauchtemenge()),
												abNachkalkulationDto,
												theClientDto);

									}

								}

								session2.close();
							}
						}
					}

				}

			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);

		}
		return abNachkalkulationDto;
	}

	private BigDecimal getErledigterArbeitszeitwertAusUnterlosen(LosDto losDto,
			TheClientDto theClientDto) {
		BigDecimal bdErledigteMenge = getErledigteMenge(losDto.getIId(),
				theClientDto);
		LossollmaterialDto[] sollmat = lossollmaterialFindByLosIId(losDto
				.getIId());
		BigDecimal bdWert = new BigDecimal(0);
		for (int i = 0; i < sollmat.length; i++) {
			bdWert = bdWert
					.add(getErledigterArbeitszeitwertEinerSollpositionAusUnterlosen(
							sollmat[i], losDto.getNLosgroesse(),
							bdErledigteMenge, theClientDto));
		}
		return bdWert;
	}

	public java.sql.Date getFruehesterEintrefftermin(Integer artikelIId,
			TheClientDto theClientDto) {
		java.sql.Date dFruehesterEintreffTermin = null;
		try {
			BestellpositionDto[] bestellpositionDtos = getBestellpositionFac()
					.bestellpositionfindByArtikelOrderByTAuftragsbestaetigungstermin(
							artikelIId, theClientDto);
			if (bestellpositionDtos != null) {
				for (int i = 0; i < bestellpositionDtos.length; i++) {
					if (!bestellpositionDtos[i]
							.getBestellpositionstatusCNr()
							.equals(BestellpositionFac.BESTELLPOSITIONSTATUS_ERLEDIGT)) {

						if (bestellpositionDtos[i].getNOffeneMenge() == null
								|| bestellpositionDtos[i].getNOffeneMenge()
										.doubleValue() > 0) {

							BestellungDto bestellungDto = getBestellungFac()
									.bestellungFindByPrimaryKey(
											bestellpositionDtos[i]
													.getBestellungIId());
							// SP3130
							if (!bestellungDto.getStatusCNr().equals(
									BestellungFac.BESTELLSTATUS_ERLEDIGT)) {

								dFruehesterEintreffTermin = bestellpositionDtos[i]
										.getTAuftragsbestaetigungstermin();
								break;
							}

						}
					}
				}

			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		// PJ18767
		ArrayList<LosDto> alLose = getFertigungFac().getLoseInFertigung(
				artikelIId, theClientDto);
		for (int i = 0; i < alLose.size(); i++) {

			if (dFruehesterEintreffTermin == null) {
				dFruehesterEintreffTermin = alLose.get(i).getTProduktionsende();
			} else {
				if (dFruehesterEintreffTermin.after(alLose.get(i)
						.getTProduktionsende())) {
					dFruehesterEintreffTermin = alLose.get(i)
							.getTProduktionsende();
				}
			}

		}

		return dFruehesterEintreffTermin;
	}

	private BigDecimal getErledigterArbeitszeitwertEinerSollpositionAusUnterlosen(
			LossollmaterialDto lossollmaterialDto, BigDecimal bdLosgroesse,
			BigDecimal bdErledigteMenge, TheClientDto theClientDto) {
		// BigDecimal bdAusgegeben =
		// getAusgegebeneMenge(lossollmaterialDto.getIId(), idUser);
		// BigDecimal bdEinzelpreis =
		// getAusgegebeneMengePreis(lossollmaterialDto.getIId(),
		// idUser);
		return new BigDecimal(0);
		/**
		 * @todo nochmal pruefen, das liefert noch falsche Werte, daher zzt.
		 *       auskommentiert PJ 4228
		 */

		// // nun die Ursprungsbuchungen suchen
		// Session session2 = FLRSessionFactory.getFactory().openSession();
		// String sQuery2 =
		// "from FLRLagerbewegung lagerbewegung WHERE lagerbewegung.i_id_buchung="
		// +
		// dto.
		// getILagerbewegungidursprung() +
		// " order by lagerbewegung.t_buchungszeit DESC";
		// Query ursrungsbuchung = session2.createQuery(sQuery2);
		// ursrungsbuchung.setMaxResults(1);
		//
		// java.util.List resultList2 = ursrungsbuchung.list();
		//
		// com.lp.server.artikel.fastlanereader.generated.FLRLagerbewegung
		// lagerbewegung_ursprung = (com.lp.server.artikel.fastlanereader.
		// generated.
		// FLRLagerbewegung) resultList2.iterator().next();
		//
		//
		//
		//
		//
		//
		// // wenn volle menge erledigt oder sogar mehr
		// if (bdLosgroesse.compareTo(bdErledigteMenge) <= 0) {
		// // dann alles
		// return bdAusgegeben.multiply(bdEinzelpreis);
		// }
		// else {
		// // Sollsatzgroesse berechnen
		// BigDecimal bdSollsatzgroesse =
		// lossollmaterialDto.getNMenge().multiply(
		// bdErledigteMenge).divide(bdLosgroesse, BigDecimal.ROUND_HALF_UP);
		// // weniger oder gleich wie sollmenge ausgegeben
		// if (bdAusgegeben.compareTo(bdSollsatzgroesse) <= 0) {
		// // dann alles
		// return bdAusgegeben.multiply(bdEinzelpreis);
		// }
		// // wenn mehr ausgegeben
		// else {
		// // dann mit sollsatzgroesse
		// return bdSollsatzgroesse.multiply(bdEinzelpreis);
		// }
		// }
	}

	public void gebeMaterialNachtraeglichAus(
			LossollmaterialDto lossollmaterialDto,
			LosistmaterialDto losistmaterialDto,
			List<SeriennrChargennrMitMengeDto> listSnrChnr,
			boolean bReduziereFehlmenge, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			if (lossollmaterialDto == null || losistmaterialDto == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
						new Exception(
								"lossollmaterialDto == null || losistmaterialDto == null"));
			}
			LosDto losDto = losFindByPrimaryKey(lossollmaterialDto.getLosIId());

			if (losDto.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT,
						"");
			} else if (losDto.getStatusCNr().equals(
					FertigungFac.STATUS_ANGELEGT)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN,
						"");
			} else if (losDto.getStatusCNr().equals(
					FertigungFac.STATUS_ERLEDIGT)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
						"");
			}

			if (lossollmaterialDto.getIId() == null || !bReduziereFehlmenge) {
				// das ist eine wirklich nachtraegliche Entnahme -> Sollmenge=0
				// iSort ermitteln (wird unten einsortiert)
				LossollmaterialDto[] sollmat = lossollmaterialFindByLosIId(lossollmaterialDto
						.getLosIId());
				int iMaxSort = 0;
				for (int i = 0; i < sollmat.length; i++) {
					if (sollmat[i].getISort().intValue() > iMaxSort) {
						iMaxSort = sollmat[i].getISort().intValue();
					}
				}
				lossollmaterialDto.setISort(new Integer(iMaxSort + 1));
				lossollmaterialDto = createLossollmaterial(lossollmaterialDto,
						theClientDto);
				losistmaterialDto.setLossollmaterialIId(lossollmaterialDto
						.getIId());
			}
			losistmaterialDto
					.setLossollmaterialIId(lossollmaterialDto.getIId());

			if (listSnrChnr != null) {
				for (int i = 0; i < listSnrChnr.size(); i++) {
					losistmaterialDto.setNMenge(listSnrChnr.get(i).getNMenge());
					createLosistmaterial(losistmaterialDto, listSnrChnr.get(i)
							.getCSeriennrChargennr(), theClientDto);
				}
			} else {
				createLosistmaterial(losistmaterialDto, null, theClientDto);
			}

			if (Helper.short2boolean(losistmaterialDto.getBAbgang()) == true
					&& bReduziereFehlmenge) {
				// aufrollung der fehlmenge sonst nicht notwendig
				getFehlmengeFac().aktualisiereFehlmenge(LocaleFac.BELEGART_LOS,
						lossollmaterialDto.getIId(), false, theClientDto);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public void updateLosistmaterialMenge(Integer losistmaterialIId,
			BigDecimal bdMengeNeu, TheClientDto theClientDto) {

		Losistmaterial losistmaterial = em.find(Losistmaterial.class,
				losistmaterialIId);
		if (losistmaterial == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
		}

		LossollmaterialDto lossollmaterialDto = lossollmaterialFindByPrimaryKey(losistmaterial
				.getLossollmaterialIId());

		// Lagerbuchung veraendern

		// je nachdem, ob ab oder Zugang

		try {
			List<SeriennrChargennrMitMengeDto> dtos = getLagerFac()
					.getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(
							LocaleFac.BELEGART_LOS, losistmaterialIId);

			if (dtos == null || dtos.size() < 1) {
				dtos = new ArrayList<SeriennrChargennrMitMengeDto>();
				SeriennrChargennrMitMengeDto s = new SeriennrChargennrMitMengeDto();
				dtos.add(s);
			}

			if (Helper.short2boolean(losistmaterial.getBAbgang())) {

				BigDecimal preis = getLagerFac()
						.getGestehungspreisEinerAbgangspositionMitTransaktion(
								LocaleFac.BELEGART_LOS, losistmaterialIId,
								dtos.get(0).getCSeriennrChargennr());
				getLagerFac()
						.bucheAb(LocaleFac.BELEGART_LOS,
								lossollmaterialDto.getLosIId(),
								losistmaterialIId,
								lossollmaterialDto.getArtikelIId(), bdMengeNeu,
								preis, losistmaterial.getLagerIId(),
								dtos.get(0).getCSeriennrChargennr(),
								new Timestamp(System.currentTimeMillis()),
								theClientDto);
				// Losistmaterial updaten
				losistmaterial.setNMenge(bdMengeNeu);

				em.merge(losistmaterial);
				em.flush();

				// Fehlmengen updaten
				getFehlmengeFac().aktualisiereFehlmenge(LocaleFac.BELEGART_LOS,
						losistmaterial.getLossollmaterialIId(), false,
						theClientDto);

			} else {

				BigDecimal bdPreis = getLagerFac()
						.getGemittelterGestehungspreisEinesLagers(
								lossollmaterialDto.getArtikelIId(),
								losistmaterial.getLagerIId(), theClientDto);

				getLagerFac().bucheZu(LocaleFac.BELEGART_LOS,
						lossollmaterialDto.getLosIId(), losistmaterialIId,
						lossollmaterialDto.getArtikelIId(), bdMengeNeu,
						bdPreis, losistmaterial.getLagerIId(),
						dtos.get(0).getCSeriennrChargennr(),
						new Timestamp(System.currentTimeMillis()),
						theClientDto, null, null, true);

				// Losistmaterial updaten
				losistmaterial.setNMenge(bdMengeNeu);

				em.merge(losistmaterial);
				em.flush();
				// Fehlmengen updaten
				getFehlmengeFac().aktualisiereFehlmenge(LocaleFac.BELEGART_LOS,
						losistmaterial.getLossollmaterialIId(), false,
						theClientDto);
			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
	}

	public void pruefePositionenMitSollsatzgroesseUnterschreitung(
			Integer losIId, BigDecimal bdZuErledigendeMenge,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// Basis fuer die Sollsatzgroesse sind die bisher erledigte Menge und
		// die zu erledigende
		BigDecimal bdKuenftigErledigt = getErledigteMenge(losIId, theClientDto)
				.add(bdZuErledigendeMenge);
		LosDto losDto = losFindByPrimaryKey(losIId);
		// es sollte von jeder Position ein prozentueller Anteil ausgegeben sein
		// der prozentsatz wird durch die kuenftig erl. Menge im Verhaeltnis zur
		// Losgroesse bestimmt
		BigDecimal bdFaktor = bdKuenftigErledigt.divide(
				losDto.getNLosgroesse(), 10, BigDecimal.ROUND_HALF_EVEN);
		// alle Positionen holen
		LossollmaterialDto[] sollmat = lossollmaterialFindByLosIId(losIId);
		LinkedList<LossollmaterialDto> listUnterschritten = new LinkedList<LossollmaterialDto>();
		// fuer jede einzelne soll- und istmenge vergleichen
		for (int i = 0; i < sollmat.length; i++) {
			// bisher ausgegebene Menge
			BigDecimal bdAusgegeben = getAusgegebeneMenge(sollmat[i].getIId(),
					null, theClientDto);
			// Die soll-ausgabe menge berechnen
			BigDecimal bdSollAusgabeMenge = Helper.rundeKaufmaennisch(
					sollmat[i].getNMenge().multiply(bdFaktor), 3);
			if (bdSollAusgabeMenge.compareTo(bdAusgegeben) > 0) {
				// Wenn die soll-ausgabe-menge noch nicht erreicht ist, dann zur
				// Liste hinzufuegen
				listUnterschritten.add(sollmat[i]);
			}
		}
		// und jetzt noch ein Array aus der Liste bauen
		LossollmaterialDto[] sollmatUnterschritten = new LossollmaterialDto[listUnterschritten
				.size()];
		int i = 0;
		for (Iterator<?> iter = listUnterschritten.iterator(); iter.hasNext(); i++) {
			sollmatUnterschritten[i] = (LossollmaterialDto) iter.next();
		}

		if (sollmatUnterschritten.length > 0) {

			StringBuffer sText = new StringBuffer(getTextRespectUISpr(
					"fert.sollsatzgroesseunterschritten",
					theClientDto.getMandant(), theClientDto.getLocUi()));

			String stkl = getTextRespectUISpr("lp.stueckliste",
					theClientDto.getMandant(), theClientDto.getLocUi());

			for (int j = 0; j < sollmatUnterschritten.length; j++) {
				sText.append("\n");

				if (losDto.getStuecklisteIId() != null) {
					sText.append(stkl);
					StuecklisteDto stklDto = getStuecklisteFac()
							.stuecklisteFindByPrimaryKey(
									losDto.getStuecklisteIId(), theClientDto);
					sText.append(" " + stklDto.getArtikelDto().getCNr() + ": ");
				}

				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKey(
								sollmatUnterschritten[j].getArtikelIId(),
								theClientDto);
				sText.append(artikelDto.formatArtikelbezeichnung());

			}
			ArrayList ai = new ArrayList();
			ai.add(sText);
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_SOLLSATZGROESSE_UNTERSCHRITTEN,
					ai, new Exception("losIId == null"));

		}
	}

	public Integer getNextArbeitsgang(Integer losIId, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (losIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("losIId == null"));
		}
		try {
			Integer i = null;
			try {
				Query query = em
						.createNamedQuery("LossollarbeitsplanejbSelectNextReihung");
				query.setParameter(1, losIId);
				i = (Integer) query.getSingleResult();
				ParametermandantDto parameter = getParameterFac()
						.getMandantparameter(theClientDto.getMandant(),
								ParameterFac.KATEGORIE_STUECKLISTE,
								ParameterFac.STUECKLISTE_ERHOEHUNG_ARBEITSGANG);
				Integer iErhoehung = (Integer) parameter.getCWertAsObject();
				if (i == null) {
					i = new Integer(0);
				}
				i = new Integer(i.intValue() + iErhoehung.intValue());
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
			return i;
		} catch (NoResultException e) {
			return new Integer(10);
		}
	}

	/**
	 * Update des Gestehungspreises von im Los verwendetem Material. Auf alle
	 * bereits getaetigten Ablieferungen wird das "dirty"-flag gesetzt, welches
	 * bei Bedarf eine Neuberechnung des Gestehungspreises der Alieferung
	 * ausloest.
	 * 
	 * @param losistmaterialIId
	 *            Integer
	 * @param bdGestehungspreis
	 *            BigDecimal
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void updateLosistmaterialGestehungspreis(Integer losistmaterialIId,
			BigDecimal bdGestehungspreis, TheClientDto theClientDto)
			throws EJBExceptionLP {
		/**
		 * @todo check wieder einbauen PJ 4229
		 */
		// super.check(idUser); // wegen aufruf mit null aus Lager derzeit
		// auskommentiert
		myLogger.logData("iid=" + losistmaterialIId + ", gestpreis="
				+ bdGestehungspreis);
		// try {
		Losistmaterial losistmaterial = em.find(Losistmaterial.class,
				losistmaterialIId);
		if (losistmaterial == null) {
			// Wenn die Position geloescht wurde, dann brauchen wir auch nichts
			// updaten
			return;
		}
		Lossollmaterial lossollmaterial = em.find(Lossollmaterial.class,
				losistmaterial.getLossollmaterialIId());
		// Dirty-flag fuer alle Ablieferungen setzen
		Query query = em.createNamedQuery("LosablieferungfindByLosIId");
		query.setParameter(1, lossollmaterial.getLosIId());
		Collection<?> losablieferungs = query.getResultList();
		Iterator<?> iterator = losablieferungs.iterator();
		while (iterator.hasNext()) {
			Losablieferung losablieferung = (Losablieferung) iterator.next();
			losablieferung.setBGestehungspreisneuberechnen(Helper
					.boolean2Short(true));
			em.merge(losablieferung);
			em.flush();
		}
		// }
		// catch (ObjectNotFoundException ex) {
		// koennte zb bei inkonsistenten Lagerdaten passieren
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
	}

	public LosablieferungDto losablieferungFindByPrimaryKey(Integer iId,
			boolean bNeuberechnungDesGestehungspreisesFallsNotwendig,
			TheClientDto theClientDto) throws EJBExceptionLP {
		LosablieferungDto losablieferungDto = null;
		// try {
		Losablieferung losablieferung = em.find(Losablieferung.class, iId);
		if (losablieferung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		losablieferungDto = assembleLosablieferungDto(losablieferung);

		if (bNeuberechnungDesGestehungspreisesFallsNotwendig
				&& Helper.short2Boolean(losablieferungDto
						.getBGestehungspreisNeuBerechnen())) {
			aktualisiereNachtraeglichPreiseAllerLosablieferungen(
					losablieferungDto.getLosIId(), theClientDto, false);
			// Neu laden, da sich Daten geaendert haben koennen
			Losablieferung temp = em.find(Losablieferung.class, iId);
			if (temp == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			losablieferungDto = assembleLosablieferungDto(temp);
		}
		// }
		// catch (ObjectNotFoundException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		return losablieferungDto;
	}

	public LosablieferungDto losablieferungFindByPrimaryKeyOhneExc(Integer iId) {
		Losablieferung losablieferung = em.find(Losablieferung.class, iId);
		if (losablieferung == null) {
			return null;
		}
		return assembleLosablieferungDto(losablieferung);
	}

	public LosablieferungDto losablieferungFindByPrimaryKeyOhneExc(Integer iId,
			boolean bNeuberechnungDesGestehungspreisesFallsNotwendig,
			TheClientDto theClientDto) throws EJBExceptionLP {
		LosablieferungDto losablieferungDto = null;
		// try {
		Losablieferung losablieferung = em.find(Losablieferung.class, iId);
		if (losablieferung == null) {
			myLogger.warn("iId=" + iId);
			return null;
		}
		losablieferungDto = assembleLosablieferungDto(losablieferung);

		if (bNeuberechnungDesGestehungspreisesFallsNotwendig
				&& Helper.short2Boolean(losablieferungDto
						.getBGestehungspreisNeuBerechnen())) {
			aktualisiereNachtraeglichPreiseAllerLosablieferungen(
					losablieferungDto.getLosIId(), theClientDto, false);
			// Neu laden, da sich Daten geaendert haben koennen
			Losablieferung temp = em.find(Losablieferung.class, iId);
			losablieferungDto = assembleLosablieferungDto(temp);
			if (temp == null) {
				myLogger.warn("iId=" + iId);
				return null;
			}
		}
		// }
		// catch (ObjectNotFoundException ex) {
		// myLogger.warn("iId=" + iId, ex);
		// return null;
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		return losablieferungDto;
	}

	public LosDto[] losFindByFertigungsortPartnerIIdMandantCNr(
			Integer partnerIId, String cNrMandant) throws EJBExceptionLP,
			RemoteException {
		LosDto[] losDtos = null;
		// try {
		Query query = em
				.createNamedQuery("LosfindByFertigungsortpartnerIIdMandantCNr");
		query.setParameter(1, partnerIId);
		query.setParameter(2, cNrMandant);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, null);
		// }
		// @todo getSingleResult oder getResultList ?
		losDtos = assembleLosDtos(cl);
		// }
		// catch (ObjectNotFoundException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		return losDtos;
	}

	public LosDto[] losFindByFertigungsortPartnerIIdMandantCNrOhneExc(
			Integer partnerIId, String cNrMandant) throws EJBExceptionLP,
			RemoteException {
		LosDto[] losDtos = null;
		// try {
		Query query = em
				.createNamedQuery("LosfindByFertigungsortpartnerIIdMandantCNr");
		query.setParameter(1, partnerIId);
		query.setParameter(2, cNrMandant);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// myLogger.warn("partnerIId=" + partnerIId);
		// return null;
		// }
		losDtos = assembleLosDtos(cl);
		// }
		// catch (ObjectNotFoundException ex) {
		// myLogger.warn("partnerIId=" + partnerIId, ex);
		// return null;
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		return losDtos;
	}

	public BigDecimal getMengeDerJuengstenLosablieferung(Integer losIId,
			TheClientDto theClientDto) {
		LosablieferungDto[] dtos = losablieferungFindByLosIId(losIId, false,
				theClientDto);

		BigDecimal menge = new BigDecimal(0);

		if (dtos != null && dtos.length > 0) {
			menge = dtos[dtos.length - 1].getNMenge();
		}

		return menge;
	}

	private LosablieferungDto[] losablieferungFindByLosIIdOhneNeuberechnungUndOhneSnrChnr(
			Integer losIId, TheClientDto theClientDto) throws EJBExceptionLP {
		LosablieferungDto[] losablieferungDtos = null;
		// try {
		Query query = em.createNamedQuery("LosablieferungfindByLosIId");
		query.setParameter(1, losIId);
		Collection<?> cl = query.getResultList();
		losablieferungDtos = assembleLosablieferungDtosOhneSnrs(cl);
		return losablieferungDtos;
	}

	public BigDecimal getMengeDerLetztenLosablieferungEinerAuftragsposition(
			Integer auftragIId, Integer artikelIId) {

		String sQuery = "select la.n_menge from FLRLosablieferung la WHERE la.flrlos.flrauftrag.i_id="
				+ auftragIId
				+ " AND la.flrlos.flrstueckliste.flrartikel.i_id="
				+ artikelIId + " ORDER BY la.t_aendern DESC";

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		org.hibernate.Query letzes = session.createQuery(sQuery);
		letzes.setMaxResults(1);
		List<?> resultList = letzes.list();

		if (resultList.size() > 0) {
			Iterator<?> resultListIterator = resultList.iterator();
			return (BigDecimal) resultListIterator.next();
		} else {
			return null;
		}

	}

	public LosablieferungDto[] losablieferungFindByLosIId(Integer losIId,
			boolean bNeuberechnungDesGestehungspreisesFallsNotwendig,
			TheClientDto theClientDto) throws EJBExceptionLP {
		LosablieferungDto[] losablieferungDtos = null;
		// try {
		Query query = em.createNamedQuery("LosablieferungfindByLosIId");
		query.setParameter(1, losIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		losablieferungDtos = assembleLosablieferungDtos(cl);
		// Alle Ablieferungen pruefen
		boolean bAktualisieren = false;
		for (int i = 0; i < losablieferungDtos.length; i++) {
			if (bNeuberechnungDesGestehungspreisesFallsNotwendig
					&& Helper.short2Boolean(losablieferungDtos[i]
							.getBGestehungspreisNeuBerechnen())) {
				bAktualisieren = true;
			}
		}
		if (bAktualisieren && losablieferungDtos.length > 0) {
			aktualisiereNachtraeglichPreiseAllerLosablieferungen(
					losablieferungDtos[0].getLosIId(), theClientDto, false);
			// Da sich Daten geaendert haben koennen, muss nochmal neu geladen
			// werden
			Query query1 = em.createNamedQuery("LosablieferungfindByLosIId");
			query1.setParameter(1, losIId);
			Collection<?> cl1 = query1.getResultList();
			// if (cl1.isEmpty()) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
			// }
			losablieferungDtos = assembleLosablieferungDtos(cl1);
		}
		// }
		// catch (ObjectNotFoundException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		return losablieferungDtos;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public KapazitaetsvorschauDto getKapazitaetsvorschau(
			TheClientDto theClientDto) throws EJBExceptionLP {
		KapazitaetsvorschauDto kapDto = null;
		Session session = null;
		try {

			// ------------------------------------------------------------------
			// -----
			// Benoetigte Parameter holen
			// ------------------------------------------------------------------
			// -----
			// Default Sicht nach Wochen
			// final int iSicht = FertigungFac.KAPAZITAETSVORSCHAU_NACH_WOCHEN;
			// Anzahl der angezeigten Vatergruppen. Alles andere wird nach
			// "Sonstige" verdichtet
			final ParametermandantDto parameterAnzahlGruppen = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FERTIGUNG,
							ParameterFac.PARAMETER_FERTIGUNG_KAPAZITAETSVORSCHAU_ANZAHL_GRUPPEN);
			final int iParamAnzahlGruppen = (Integer) parameterAnzahlGruppen
					.getCWertAsObject();
			// Angezeigter Zeitraum = Anzahl der Spalten
			final ParametermandantDto parameterZeitraum = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FERTIGUNG,
							ParameterFac.PARAMETER_FERTIGUNG_KAPAZITAETSVORSCHAU_ZEITRAUM);
			final int iParamZeitraum = (Integer) parameterZeitraum
					.getCWertAsObject();

			// ------------------------------------------------------------------
			// -----
			// Hibernate-Session erstellen
			// ------------------------------------------------------------------
			// -----
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			// ------------------------------------------------------------------
			// -----
			// Artikel- / Maschinen-Vatergruppen holen
			// ------------------------------------------------------------------
			// -----
			// Alle Artikel-Vatergruppen
			Criteria cArtikelVatergruppen = session
					.createCriteria(FLRArtikelgruppe.class);
			cArtikelVatergruppen.add(Restrictions
					.isNull(ArtikelFac.FLR_ARTIKELGRUPPE_FLRARTIKELGRUPPE));
			List<?> listArtikelgruppen = cArtikelVatergruppen.list();
			// Alle Maschinen-Vatergruppen
			Criteria cMaschinengruppen = session
					.createCriteria(FLRMaschinengruppe.class);
			List<?> listMaschinengruppen = cMaschinengruppen.list();
			// ------------------------------------------------------------------
			// -----
			// Anzahl der sub-Diagramme bestimmen
			// das ist grundsaetzlich (iParamAnzahlGruppen + 1) x 2 ...
			// (Anzahl d. anzuzeigenden Vatergruppen + Sonstige) f. AZ und
			// Maschinen
			// wenn es weniger Vatergruppen als anzuzeigende gibt, reduziert
			// sich das aber
			// Gibt es keine Vatergruppe, wird daher alles nach "Sonstige"
			// verdichtet
			// ------------------------------------------------------------------
			// -----
			final int iAnzuzeigendeArtikelgruppen = Math.min(
					iParamAnzahlGruppen, listArtikelgruppen.size());

			final int iAnzuzeigendeMaschinengruppen = Math.min(
					iParamAnzahlGruppen, listMaschinengruppen.size());

			final int iAnzahlZeilen = iAnzuzeigendeArtikelgruppen + 1
					+ iAnzuzeigendeMaschinengruppen + 1;

			// ------------------------------------------------------------------
			// -----
			// Dto initialisieren
			// ------------------------------------------------------------------
			// -----
			kapDto = new KapazitaetsvorschauDto(iAnzahlZeilen, iParamZeitraum);
			// ------------------------------------------------------------------
			// -----
			// Beschriftung der x-Achse ermitteln. das sind die Kalenderwochen
			// ------------------------------------------------------------------
			// -----
			HashMap<Integer, Integer> hmKWIndizes = new HashMap<Integer, Integer>();

			String[] kw = new String[iParamZeitraum];
			GregorianCalendar gc = new GregorianCalendar();
			for (int i = 0; i < kw.length; i++) {
				int iKw = gc.get(GregorianCalendar.WEEK_OF_YEAR);
				kw[i] = "" + iKw;
				kapDto.setISpaltenueberschrift(i, kw[i]);
				hmKWIndizes.put(gc.get(GregorianCalendar.WEEK_OF_YEAR), i);
				gc.setTimeInMillis(gc.getTimeInMillis() + 7 * 24 * 60 * 60
						* 1000); // 1 Woche dazu
			}
			// ------------------------------------------------------------------
			// -----
			// Beschriftung der y-Achse ermitteln. das sind die Namen der
			// Vatergruppen bzw. 2 x "Sonstige".
			// Weiters werden die Indizes im Daten-Array fuer die jeweiligen
			// Gruppen festgelegt.
			// ------------------------------------------------------------------
			// -----
			String sSonstige = getTextRespectUISpr("lp.sonstige",
					theClientDto.getMandant(), theClientDto.getLocUi());

			HashMap<Integer, Integer> hmArtikelGruppenIndizes = new HashMap<Integer, Integer>();
			HashMap<Integer, Integer> hmMaschinenGruppenIndizes = new HashMap<Integer, Integer>();

			// zuerst die Artikelvatergruppen
			for (int i = 0; i < iAnzuzeigendeArtikelgruppen; i++) {
				FLRArtikelgruppe item = (FLRArtikelgruppe) listArtikelgruppen
						.get(i);
				kapDto.setIZeilenueberschrift(i, item.getC_nr());
				hmArtikelGruppenIndizes.put(item.getI_id(), i);
			}
			// Dann Sonstige Artikelgruppen
			final int indexSonstigeArtikelGruppen = iAnzuzeigendeArtikelgruppen;
			kapDto.setIZeilenueberschrift(indexSonstigeArtikelGruppen,
					sSonstige);
			// Maschinengruppen
			for (int i = 0; i < iAnzuzeigendeMaschinengruppen; i++) {
				FLRMaschinengruppe item = (FLRMaschinengruppe) listMaschinengruppen
						.get(i);
				int index = iAnzuzeigendeArtikelgruppen + 1 + i;
				kapDto.setIZeilenueberschrift(index, item.getC_bez());
				hmMaschinenGruppenIndizes.put(item.getI_id(), index);
			}
			// zuletzt Sonstige Maschinengruppen
			final int indexSonstigeMaschinenGruppen = iAnzuzeigendeArtikelgruppen
					+ 1 + iAnzuzeigendeMaschinengruppen;
			kapDto.setIZeilenueberschrift(indexSonstigeMaschinenGruppen,
					sSonstige);

			// ------------------------------------------------------------------
			// -----
			// Lose holen
			// ------------------------------------------------------------------
			// -----
			Criteria cLose = session.createCriteria(FLRLos.class);
			// Filter nach Mandant
			cLose.add(Restrictions.eq(FertigungFac.FLR_LOS_MANDANT_C_NR,
					theClientDto.getMandant()));
			// Alle Stati ausser Erledigt, Gestoppt, Storniert
			Collection<String> cLoseStati = new LinkedList<String>();
			cLoseStati.add(FertigungFac.STATUS_ERLEDIGT);
			cLoseStati.add(FertigungFac.STATUS_GESTOPPT);
			cLoseStati.add(FertigungFac.STATUS_STORNIERT);
			cLose.add(Restrictions.not(Restrictions.in(
					FertigungFac.FLR_LOS_STATUS_C_NR, cLoseStati)));
			List<?> listLose = cLose.list();
			// ------------------------------------------------------------------
			// -----
			// Auswertungszeitraum und verfuegbare Kapazitaeten ermitteln
			// ------------------------------------------------------------------
			// -----

			// 3 Monate in die zukunft
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(System.currentTimeMillis());
			c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 3);
			java.util.Date dMax = Helper.cutDate(new java.sql.Date(c
					.getTimeInMillis()));

			java.sql.Timestamp tVon = Helper
					.cutTimestamp(new java.sql.Timestamp(System
							.currentTimeMillis()));
			java.sql.Timestamp tBis = Helper
					.cutTimestamp(new java.sql.Timestamp(dMax.getTime()));
			// Verfuegbare Zeiten holen
			SollverfuegbarkeitDto[] oVerfuegbar = getZeiterfassungFac()
					.getVerfuegbareSollzeit(tVon, tBis, theClientDto);
			// diese nun aufteilen
			for (int i = 0; i < oVerfuegbar.length; i++) {
				SollverfuegbarkeitDto svDto = oVerfuegbar[i];
				// "normale" AZ
				if (svDto.isBMannarbeitszeit()) {
					// die sind einer Artikelgruppe zugeordnet
					if (svDto.getIGruppeid() != null) {
						Integer iZeile = hmArtikelGruppenIndizes.get(svDto
								.getIGruppeid());
						// ist das eine sichtbare Gruppe
						if (iZeile != null) {
							for (int iSpalte = 0; iSpalte < kapDto.getDetails()[iZeile].length; iSpalte++) {
								// mal 5 Tage
								kapDto.addBdVerfuegbareStunden(
										iZeile,
										iSpalte,
										svDto.getNSollstunden().multiply(
												new BigDecimal(5)));
							}
						}
						// wenn nicht, dann zu den sonstigen
						else {
							for (int iSpalte = 0; iSpalte < kapDto.getDetails()[indexSonstigeArtikelGruppen].length; iSpalte++) {
								// mal 5 Tage
								kapDto.addBdVerfuegbareStunden(
										indexSonstigeArtikelGruppen,
										iSpalte,
										svDto.getNSollstunden().multiply(
												new BigDecimal(5)));
							}
						}
					}
					// Rest = Sonstige Artikelgruppen
					else {
						if (svDto.getTDatum() != null) {
							// Die KW dieses Datums ermitteln, damit das
							// zugeordnet werden kann
							GregorianCalendar gcSV = new GregorianCalendar();
							gcSV.setTime(svDto.getTDatum());
							int kwSV = gcSV.get(Calendar.WEEK_OF_YEAR);
							Integer iIndexDerKW = hmKWIndizes.get(kwSV);
							if (iIndexDerKW != null) {
								// Hier nicht mit 5 multiplizieren, da es fuer
								// jeden Tag einen eigenen Eintrag gibt
								kapDto.addBdVerfuegbareStunden(
										indexSonstigeArtikelGruppen,
										iIndexDerKW, svDto.getNSollstunden());
							} else {
								// diese KW wird nicht mehr angezeigt - brauch
								// ich nicht einrechnen
							}
						}
					}
				}
				// Maschinenzeit - die Verfuegbarkeit ist jeden Tag gleich
				else {
					Integer iZeile = hmMaschinenGruppenIndizes.get(svDto
							.getIGruppeid());
					// ist das eine sichtbare Gruppe
					if (iZeile != null) {
						for (int iSpalte = 0; iSpalte < kapDto.getDetails()[iZeile].length; iSpalte++) {
							// mal 5 Tage
							kapDto.addBdVerfuegbareStunden(
									iZeile,
									iSpalte,
									svDto.getNSollstunden().multiply(
											new BigDecimal(5)));
						}
					}
					// wenn nicht, dann zu den sonstigen
					else {
						for (int iSpalte = 0; iSpalte < kapDto.getDetails()[indexSonstigeMaschinenGruppen].length; iSpalte++) {
							// mal 5 Tage
							kapDto.addBdVerfuegbareStunden(
									indexSonstigeMaschinenGruppen,
									iSpalte,
									svDto.getNSollstunden().multiply(
											new BigDecimal(5)));
						}
					}
				}
			}
			// ------------------------------------------------------------------
			// -----
			// Offene Zeiten ermitteln
			// ------------------------------------------------------------------
			// -----
			for (Iterator<?> iter = listLose.iterator(); iter.hasNext();) {
				FLRLos los = (FLRLos) iter.next();
				// Offene Menge ermitteln
				BigDecimal bdOffen = los.getN_losgroesse();
				for (Iterator<?> iterAblieferung = los.getAblieferungset()
						.iterator(); iterAblieferung.hasNext();) {
					FLRLosablieferung item = (FLRLosablieferung) iterAblieferung
							.next();
					bdOffen = bdOffen.subtract(item.getN_menge());
				}
				// nur Lose mit tatsaechlich offener Menge>0
				if (bdOffen.compareTo(new BigDecimal(0)) > 0) {
					// Faktor zur Berechnung der offenen Zeiten = offene Menge /
					// Losgroesse. 2 Nachkommastellen sollten reichen.
					BigDecimal bdFaktor = bdOffen.divide(los.getN_losgroesse(),
							2, BigDecimal.ROUND_HALF_EVEN);
					// Arbeitsplan holen
					Criteria cLosAZ = session
							.createCriteria(FLRLossollarbeitsplan.class);
					cLosAZ.add(Restrictions.eq(
							FertigungFac.FLR_LOSSOLLARBEITSPLAN_LOS_I_ID,
							los.getI_id()));
					List<?> listLosAZ = cLosAZ.list();
					// fuer alle Taetigkeiten
					for (Iterator<?> iterAZ = listLosAZ.iterator(); iterAZ
							.hasNext();) {
						FLRLossollarbeitsplan losAZ = (FLRLossollarbeitsplan) iterAZ
								.next();
						BigDecimal bdOffeneStunden = losAZ.getN_gesamtzeit()
								.multiply(bdFaktor);
						// ------------------------------------------------------
						// -----------------
						// Index der Gruppe bestimmen, der ich das zuordnen muss
						// ------------------------------------------------------
						// -----------------
						int iZeilenIndex;
						// 1. nach Maschinengruppe
						// 2. nach Artikelgruppe der Taetigkeit
						FLRMaschine flrMaschine = losAZ.getFlrmaschine();
						Integer iMaschinengruppeIId = null;
						Integer iArtikelgruppeIId = null;
						if (flrMaschine != null) {
							FLRMaschinengruppe flrMaschinengruppe = flrMaschine
									.getFlrmaschinengruppe();
							if (flrMaschinengruppe != null) {
								// Wenn diese Maschinengruppe dargestellt wird,
								// dann kommt hier der index raus.
								Integer i = hmMaschinenGruppenIndizes
										.get(flrMaschinengruppe.getI_id());
								iMaschinengruppeIId = flrMaschinengruppe
										.getI_id();
								if (i != null) {
									iZeilenIndex = i;
								}
								// wenn nicht -> sonstige.
								else {
									iZeilenIndex = indexSonstigeMaschinenGruppen;
								}
							}
							// Maschinen ohne Maschinengruppe werden nach
							// "Sonstige" verdichtet.
							else {
								iZeilenIndex = indexSonstigeMaschinenGruppen;
							}
						} else {
							FLRArtikel flrArtikel = losAZ.getFlrartikel();
							FLRArtikelgruppe flrArtikelgruppe = flrArtikel
									.getFlrartikelgruppe();
							if (flrArtikelgruppe != null) {
								// Wenn diese Artikelgruppe dargestellt wird,
								// dann kommt hier der index raus.
								Integer i = hmArtikelGruppenIndizes
										.get(flrArtikelgruppe.getI_id());
								iArtikelgruppeIId = flrArtikelgruppe.getI_id();
								if (i != null) {
									iZeilenIndex = i;
								}
								// wenn nicht -> sonstige.
								else {
									iZeilenIndex = indexSonstigeArtikelGruppen;
								}
							}
							// Taetigkeiten ohne Artikelgruppe werden nach
							// "Sonstige" verdichtet.
							else {
								iZeilenIndex = indexSonstigeArtikelGruppen;
							}
						}
						// ------------------------------------------------------
						// -----------------
						// Jetzt hab ich die Gruppe, der ich das zuordnen muss
						// nun muss die Zeit aufgeteilt werden
						// ------------------------------------------------------
						// -----------------
						java.util.Date tLosStarttermin = los
								.getT_produktionsbeginn();
						java.util.Date tLosEndetermin = los
								.getT_produktionsende();
						// beide Termine duerfen nicht vor heute liegen
						if (tLosStarttermin.before(getDate())) {
							tLosStarttermin = getDate();
						}
						if (tLosEndetermin.before(getDate())) {
							tLosEndetermin = getDate();
						}
						// Anzahl der betroffenen Kalenderwochen bestimmen
						GregorianCalendar gcStart = new GregorianCalendar();
						gcStart.setTime(tLosStarttermin);
						GregorianCalendar gcEnde = new GregorianCalendar();
						gcEnde.setTime(tLosEndetermin);
						int iStartKW = gcStart.get(Calendar.WEEK_OF_YEAR);
						int iEndeKW = gcEnde.get(Calendar.WEEK_OF_YEAR);
						int iAnzahlKW = 1 + iEndeKW - iStartKW;
						// nun auf die Wochen aufteilen
						BigDecimal bdOffeneStundenJeWoche = bdOffeneStunden;

						if (iAnzahlKW > 0) {
							bdOffeneStundenJeWoche = bdOffeneStunden.divide(
									new BigDecimal(iAnzahlKW), 2,
									RoundingMode.HALF_UP);
						}

						for (int iAktuelleKW = iStartKW; iAktuelleKW <= iEndeKW; iAktuelleKW++) {
							Integer indexDerKW = hmKWIndizes.get(iAktuelleKW);
							// wird diese Woche auch angezeigt?
							if (indexDerKW != null) {
								KapazitaetsvorschauDetailDto detailDto = new KapazitaetsvorschauDetailDto();
								detailDto
										.setArtikelgruppeIId(iArtikelgruppeIId);
								detailDto.setArtikelIIdTaetigkeit(losAZ
										.getFlrartikel().getI_id());
								detailDto.setBdDauer(bdOffeneStundenJeWoche);
								detailDto.setLosIId(los.getI_id());
								detailDto.setLossollarbeitsplanIId(losAZ
										.getI_id());
								detailDto
										.setMaschinengruppeIId(iMaschinengruppeIId);
								kapDto.addDetail(iZeilenIndex, indexDerKW,
										detailDto);
							}
						}
					}
				}
			}

			// ------------------------------------------------------------------
			// -----
			// Diagramm aus den Daten erstellen
			// ------------------------------------------------------------------
			// -----
			SymbolAxis xAchse = new SymbolAxis("KW", kw);
			CombinedDomainXYPlot plot = new CombinedDomainXYPlot(xAchse);
			for (int iZeile = 0; iZeile < kapDto.getDetails().length; iZeile++) {
				XYSeries datenZeile = new XYSeries(
						kapDto.getIZeilenueberschrift(iZeile));
				// Balkenfarben festlegen ( >100% = rot, sonst hellgrau)
				// fuer jede zeile und jede spalte
				Paint[][] paints = new Paint[1][kapDto.getDetails()[iZeile].length];
				for (int iSpalte = 0; iSpalte < kapDto.getDetails()[iZeile].length; iSpalte++) {
					BigDecimal bdVerfuegbar = kapDto.getBdVerfuegbareStunden()[iZeile][iSpalte];
					BigDecimal bdBenoetigt = new BigDecimal(0);
					// Benoetigte Zeit jet Gruppe je Woche ermitteln
					for (Iterator<?> iter = kapDto.getDetails()[iZeile][iSpalte]
							.iterator(); iter.hasNext();) {
						KapazitaetsvorschauDetailDto item = (KapazitaetsvorschauDetailDto) iter
								.next();
						bdBenoetigt = bdBenoetigt.add(item.getBdDauer());
					}
					BigDecimal value = new BigDecimal(0);
					if (bdVerfuegbar.compareTo(new BigDecimal(0)) > 0) {
						value = (bdBenoetigt.multiply(new BigDecimal(100)))
								.divide(bdVerfuegbar, 4,
										BigDecimal.ROUND_HALF_EVEN);
						if (value.doubleValue() > 100.0) {
							paints[0][iSpalte] = Color.red;
						} else {
							paints[0][iSpalte] = Color.lightGray;
						}
					}
					// tage ohne Verfuegbarkeit mach ich 100% und weisz
					else {
						value = new BigDecimal(100.0);
						// Wochen ohne Kapazitaet aber mit geplanter Zeit
						if (bdBenoetigt.compareTo(new BigDecimal(0)) > 0) {
							paints[0][iSpalte] = Color.MAGENTA;
						}
						// Wenn nichts verfuegbar aber auch nichts benoetigt ->
						// weiss
						else {
							paints[0][iSpalte] = Color.white;
						}
					}
					XYDataItem data = new XYDataItem(iSpalte,
							value.doubleValue());
					datenZeile.add(data);
				}
				// Zur Collection
				XYSeriesCollection xyDataset = new XYSeriesCollection();
				xyDataset.addSeries(datenZeile);

				// subplot erstellen
				XYItemRenderer renderer1 = new CustomXYBarRenderer(paints);

				// renderer1.setItemLabelsVisible(true);
				// Legende nicht anzeigen
				renderer1.setBaseSeriesVisibleInLegend(false);
				NumberAxis zeilenAchse = new NumberAxis(
						kapDto.getIZeilenueberschrift(iZeile));
				// Beschriftung der Y-Achse um 90 grad drehen
				zeilenAchse.setLabelAngle(Math.PI / 2.0);
				zeilenAchse.setAutoRange(true);
				XYPlot subplot1 = new XYPlot(xyDataset, null, zeilenAchse,
						renderer1);
				subplot1.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
				// Markierung bei 100%
				final Marker target = new ValueMarker(100.0);
				target.setPaint(Color.darkGray);
				// target.setLabel("100 %"); // Label
				// target.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
				// target.setLabelTextAnchor(TextAnchor.BOTTOM_RIGHT);
				subplot1.addRangeMarker(target);

				plot.add(subplot1); // plot.add(subplot1, 1);
			}
			JFreeChart lStackedBarChart = new JFreeChart(plot);

			kapDto.setJfcKapazitaetsvorschau(lStackedBarChart);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return kapDto;
	}

	public void setzeLosablieferungenAufNeuBerechnen(Integer losIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("LosablieferungfindByLosIId");
		query.setParameter(1, losIId);
		Collection<?> c = query.getResultList();
		// if (!c.isEmpty()) {

		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			Losablieferung item = (Losablieferung) iter.next();
			item.setBGestehungspreisneuberechnen(Helper.boolean2Short(true));
		}
		// }
		// }
		// catch (ObjectNotFoundException ex) {
		// // nothing here;
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	/**
	 * Die Zuordnung des Loses au Auftrag, Kostenstelle nachtraeglich
	 * veraendern.
	 * 
	 * @param losDto
	 *            LosDto
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void updateLosZuordnung(LosDto losDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (losDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("losDto == null"));
		}
		Los los = em.find(Los.class, losDto.getIId());
		if (los == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		los.setAuftragIId(losDto.getAuftragIId());
		los.setAuftragpositionIId(losDto.getAuftragpositionIId());
		los.setLosIIdElternlos(losDto.getLosIIdElternlos());
		los.setKostenstelleIId(losDto.getKostenstelleIId());
		los.setPersonalIIdAendern(theClientDto.getIDPersonal());
		los.setTAendern(getTimestamp());
	}

	/**
	 * Die I_ID's aller Lose des Mandanten ermitteln. Um clientseitige
	 * Serientransaktionen durchfuerhen zu koennen.
	 * 
	 * @param theClientDto
	 *            String
	 * @return ArrayList
	 * @throws EJBExceptionLP
	 */
	public ArrayList<Integer> getAllLosIIdDesMandanten(TheClientDto theClientDto)
			throws EJBExceptionLP {
		ArrayList<Integer> list = new ArrayList<Integer>();
		Session session = null;
		try {

			session = FLRSessionFactory.getFactory().openSession();
			Criteria crit = session.createCriteria(FLRLos.class);
			crit.add(Restrictions.eq(FertigungFac.FLR_LOS_MANDANT_C_NR,
					theClientDto.getMandant()));
			List<?> resultList = crit.list();
			Iterator<?> resultListIterator = resultList.iterator();
			while (resultListIterator.hasNext()) {
				FLRLos flrLos = (FLRLos) resultListIterator.next();
				list.add(flrLos.getI_id());
			}
		} finally {
			closeSession(session);
		}
		return list;
	}

	public void vonQuelllagerUmbuchenUndAnsLosAusgeben(Integer lagerIId_Quelle,
			Integer lossollmaterialIId, BigDecimal nMenge,
			List<SeriennrChargennrMitMengeDto> l, TheClientDto theClientDto) {
		// Zuerst umbuchen
		LossollmaterialDto lossollmaterialDto = lossollmaterialFindByPrimaryKey(lossollmaterialIId);

		LoslagerentnahmeDto[] loslagerentnahmeDtos = loslagerentnahmeFindByLosIId(lossollmaterialDto
				.getLosIId());

		try {

			// Zuerst umbuchen, wenn Laeger unterschiedlich

			if (!lagerIId_Quelle.equals(loslagerentnahmeDtos[0].getLagerIId())) {

				getLagerFac().bucheUm(
						lossollmaterialDto.getArtikelIId(),
						lagerIId_Quelle,
						lossollmaterialDto.getArtikelIId(),
						loslagerentnahmeDtos[0].getLagerIId(),
						nMenge,
						l,
						"Umbuchung Wareneingangslager",
						getLagerFac().getGemittelterGestehungspreisEinesLagers(
								lossollmaterialDto.getArtikelIId(),
								lagerIId_Quelle, theClientDto), theClientDto);
			}

			// dann ausgeben

			LosistmaterialDto losistmaterialDto = new LosistmaterialDto();
			losistmaterialDto
					.setLagerIId(loslagerentnahmeDtos[0].getLagerIId());
			losistmaterialDto.setBAbgang(Helper.boolean2Short(true));

			losistmaterialDto.setNMenge(nMenge);

			gebeMaterialNachtraeglichAus(lossollmaterialDto, losistmaterialDto,
					l, true, theClientDto);

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

	}

	public void vertauscheWiederholendelose(Integer iIdMontageart1I,
			Integer iIdMontageart2I) {
		myLogger.entry();
		// try {
		Wiederholendelose wiederholendelose1 = em.find(Wiederholendelose.class,
				iIdMontageart1I);
		if (wiederholendelose1 == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Wiederholendelose wiederholendelose2 = em.find(Wiederholendelose.class,
				iIdMontageart2I);

		Integer iSort1 = wiederholendelose1.getISort();
		Integer iSort2 = wiederholendelose2.getISort();

		wiederholendelose2.setISort(new Integer(-1));

		wiederholendelose1.setISort(iSort2);
		wiederholendelose2.setISort(iSort1);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public Integer createWiederholendelose(
			WiederholendeloseDto wiederholendeloseDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (wiederholendeloseDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("wiederholendeloseDto == null"));
		}
		if (wiederholendeloseDto.getAuftragwiederholungsintervallCNr() == null
				|| wiederholendeloseDto.getBVersteckt() == null
				|| wiederholendeloseDto.getFertigungsgruppeIId() == null
				|| wiederholendeloseDto.getITagevoreilend() == null
				|| wiederholendeloseDto.getKostenstelleIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"wiederholendeloseDto.getAuftragwiederholungsintervallCNr() == null || wiederholendeloseDto.getBVersteckt() == null  || wiederholendeloseDto.getFertigungsgruppeIId() == null  || wiederholendeloseDto.getITagevoreilend() == null  || wiederholendeloseDto.getKostenstelleIId() == null "));
		}
		if (wiederholendeloseDto.getLagerIIdZiel() == null
				|| wiederholendeloseDto.getMandantCNr() == null
				|| wiederholendeloseDto.getNLosgroesse() == null
				|| wiederholendeloseDto.getPartnerIIdFertigungsort() == null
				|| wiederholendeloseDto.getTTermin() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"wiederholendeloseDto.getLagerIIdZiel() == null || wiederholendeloseDto.getMandantCNr() == null  || wiederholendeloseDto.getNLosgroesse() == null  || wiederholendeloseDto.getPartnerIIdFertigungsort() == null  || wiederholendeloseDto.getTTermin() == null"));
		}
		wiederholendeloseDto.setTTermin(Helper
				.cutTimestamp(wiederholendeloseDto.getTTermin()));
		try {
			Integer iId = getPKGeneratorObj().getNextPrimaryKey(
					PKConst.PK_LOSSOLLMATERIAL);
			wiederholendeloseDto.setIId(iId);
			Integer i = null;
			try {
				Query query = em
						.createNamedQuery("WiederholendeloseejbSelectNextReihung");
				query.setParameter(1, theClientDto.getMandant());
				i = (Integer) query.getSingleResult();
			} catch (NoResultException ex) {
				// nothing here
			}
			if (i == null) {
				i = new Integer(0);
			}
			i = new Integer(i.intValue() + 1);
			wiederholendeloseDto.setISort(i);

			wiederholendeloseDto.setMandantCNr(theClientDto.getMandant());
			wiederholendeloseDto.setPersonalIIdAendern(theClientDto
					.getIDPersonal());
			wiederholendeloseDto.setTAendern(new Timestamp(System
					.currentTimeMillis()));

			wiederholendeloseDto.setIId(iId);
			Wiederholendelose wiederholendelose = new Wiederholendelose(
					wiederholendeloseDto.getIId(),
					wiederholendeloseDto.getMandantCNr(),
					wiederholendeloseDto.getKostenstelleIId(),
					wiederholendeloseDto.getNLosgroesse(),
					wiederholendeloseDto.getPartnerIIdFertigungsort(),
					wiederholendeloseDto.getPersonalIIdAendern(),
					wiederholendeloseDto.getTTermin(),
					wiederholendeloseDto.getLagerIIdZiel(),
					wiederholendeloseDto.getITagevoreilend(),
					wiederholendeloseDto.getFertigungsgruppeIId(),
					wiederholendeloseDto.getAuftragwiederholungsintervallCNr(),
					wiederholendeloseDto.getBVersteckt(),
					wiederholendeloseDto.getISort());
			em.persist(wiederholendelose);
			em.flush();
			setWiederholendeloseFromWiederholendeloseDto(wiederholendelose,
					wiederholendeloseDto);
			return wiederholendeloseDto.getIId();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeWiederholendelose(
			WiederholendeloseDto wiederholendeloseDto) throws EJBExceptionLP {
		if (wiederholendeloseDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("wiederholendeloseDto == null"));
		}
		if (wiederholendeloseDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("wiederholendeloseDto.getIId() == null"));
		}

		// try {
		Integer iId = wiederholendeloseDto.getIId();
		Wiederholendelose toRemove = em.find(Wiederholendelose.class, iId);
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
		// }
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN, e);
		// }

	}

	public void updateWiederholendelose(
			WiederholendeloseDto wiederholendeloseDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (wiederholendeloseDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("wiederholendeloseDto == null"));
		}
		if (wiederholendeloseDto.getIId() == null
				|| wiederholendeloseDto.getAuftragwiederholungsintervallCNr() == null
				|| wiederholendeloseDto.getBVersteckt() == null
				|| wiederholendeloseDto.getFertigungsgruppeIId() == null
				|| wiederholendeloseDto.getITagevoreilend() == null
				|| wiederholendeloseDto.getKostenstelleIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"wiederholendeloseDto.getIId() == null || wiederholendeloseDto.getAuftragwiederholungsintervallCNr() == null || wiederholendeloseDto.getBVersteckt() == null  || wiederholendeloseDto.getFertigungsgruppeIId() == null  || wiederholendeloseDto.getITagevoreilend() == null  || wiederholendeloseDto.getKostenstelleIId() == null "));
		}
		if (wiederholendeloseDto.getLagerIIdZiel() == null
				|| wiederholendeloseDto.getMandantCNr() == null
				|| wiederholendeloseDto.getNLosgroesse() == null
				|| wiederholendeloseDto.getPartnerIIdFertigungsort() == null
				|| wiederholendeloseDto.getTTermin() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"wiederholendeloseDto.getLagerIIdZiel() == null || wiederholendeloseDto.getMandantCNr() == null  || wiederholendeloseDto.getNLosgroesse() == null  || wiederholendeloseDto.getPartnerIIdFertigungsort() == null  || wiederholendeloseDto.getTTermin() == null"));
		}
		wiederholendeloseDto.setTTermin(Helper
				.cutTimestamp(wiederholendeloseDto.getTTermin()));

		Integer iId = wiederholendeloseDto.getIId();
		wiederholendeloseDto.setMandantCNr(theClientDto.getMandant());
		wiederholendeloseDto
				.setPersonalIIdAendern(theClientDto.getIDPersonal());
		wiederholendeloseDto.setTAendern(new Timestamp(System
				.currentTimeMillis()));
		Wiederholendelose wiederholendelose = em.find(Wiederholendelose.class,
				iId);
		if (wiederholendelose == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setWiederholendeloseFromWiederholendeloseDto(wiederholendelose,
				wiederholendeloseDto);
	}

	public WiederholendeloseDto wiederholendeloseFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Wiederholendelose wiederholendelose = em.find(Wiederholendelose.class,
				iId);
		if (wiederholendelose == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleWiederholendeloseDto(wiederholendelose);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public WiederholendeloseDto[] wiederholendeloseFindByPartnerIIdMandantCNr(
			Integer iPartnerId, String iMandantCNr) throws EJBExceptionLP {
		if (iPartnerId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("iPartnerId == null"));
		}
		// try {
		Query query = em
				.createNamedQuery("WiederholendelosefindByFertigungsortpartnerIIdMandantCNr");
		query.setParameter(1, iPartnerId);
		query.setParameter(2, iMandantCNr);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		return assembleWiederholendeloseDtos(cl);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		// }
	}

	public WiederholendeloseDto[] wiederholendeloseFindByPartnerIIdMandantCNrOhneExc(
			Integer iPartnerId, String iMandantCNr) {
		if (iPartnerId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("iPartnerId == null"));
		}
		// try {
		Query query = em
				.createNamedQuery("WiederholendelosefindByFertigungsortpartnerIIdMandantCNr");
		query.setParameter(1, iPartnerId);
		query.setParameter(2, iMandantCNr);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// return null;
		// }
		return assembleWiederholendeloseDtos(cl);
		// }
		// catch (ObjectNotFoundException ex) {
		// return null;
		// }
		// catch (Throwable e) {
		// return null;
		// }
	}

	private void setWiederholendeloseFromWiederholendeloseDto(
			Wiederholendelose wiederholendelose,
			WiederholendeloseDto wiederholendeloseDto) {
		wiederholendelose.setMandantCNr(wiederholendeloseDto.getMandantCNr());
		wiederholendelose.setKostenstelleIId(wiederholendeloseDto
				.getKostenstelleIId());
		wiederholendelose.setCProjekt(wiederholendeloseDto.getCProjekt());
		wiederholendelose.setStuecklisteIId(wiederholendeloseDto
				.getStuecklisteIId());
		wiederholendelose.setNLosgroesse(wiederholendeloseDto.getNLosgroesse());
		wiederholendelose.setPartnerIIdFertigungsort(wiederholendeloseDto
				.getPartnerIIdFertigungsort());
		wiederholendelose.setPersonalIIdAendern(wiederholendeloseDto
				.getPersonalIIdAendern());
		wiederholendelose.setTAendern(wiederholendeloseDto.getTAendern());
		wiederholendelose.setTTermin(wiederholendeloseDto.getTTermin());
		wiederholendelose.setISort(wiederholendeloseDto.getISort());
		wiederholendelose.setLagerIIdZiel(wiederholendeloseDto
				.getLagerIIdZiel());
		wiederholendelose.setITagevoreilend(wiederholendeloseDto
				.getITagevoreilend());
		wiederholendelose.setFertigungsgruppeIId(wiederholendeloseDto
				.getFertigungsgruppeIId());
		wiederholendelose
				.setAuftragwiederholungsintervallCNr(wiederholendeloseDto
						.getAuftragwiederholungsintervallCNr());
		wiederholendelose.setBVersteckt(wiederholendeloseDto.getBVersteckt());
		em.merge(wiederholendelose);
		em.flush();
	}

	private WiederholendeloseDto assembleWiederholendeloseDto(
			Wiederholendelose wiederholendelose) {
		return WiederholendeloseDtoAssembler.createDto(wiederholendelose);
	}

	private WiederholendeloseDto[] assembleWiederholendeloseDtos(
			Collection<?> wiederholendeloses) {
		List<WiederholendeloseDto> list = new ArrayList<WiederholendeloseDto>();
		if (wiederholendeloses != null) {
			Iterator<?> iterator = wiederholendeloses.iterator();
			while (iterator.hasNext()) {
				Wiederholendelose wiederholendelose = (Wiederholendelose) iterator
						.next();
				list.add(assembleWiederholendeloseDto(wiederholendelose));
			}
		}
		WiederholendeloseDto[] returnArray = new WiederholendeloseDto[list
				.size()];
		return (WiederholendeloseDto[]) list.toArray(returnArray);
	}

	public Integer createZusatzstatus(ZusatzstatusDto zusatzstatusDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (zusatzstatusDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelDto == null"));
		}
		if (zusatzstatusDto.getCBez() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("zusatzstatusDto.getCBez() == null"));
		}
		zusatzstatusDto.setMandantCNr(theClientDto.getMandant());

		try {
			Query query = em
					.createNamedQuery("ZusatzstatusfindByMandantCNrCBez");
			query.setParameter(1, zusatzstatusDto.getMandantCNr());
			query.setParameter(2, zusatzstatusDto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Zusatzstatus doppelt = (Zusatzstatus) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"FERT_ZUSATZSTATUS.UK"));
			}
		} catch (NoResultException ex) {
			// nix
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ZUSATZSTATUS);
			zusatzstatusDto.setIId(pk);

			Zusatzstatus zusatzstatus = new Zusatzstatus(
					zusatzstatusDto.getIId(), zusatzstatusDto.getMandantCNr(),
					zusatzstatusDto.getCBez(), zusatzstatusDto.getISort());
			em.persist(zusatzstatus);
			em.flush();
			setZusatzstatusFromZusatzstatusDto(zusatzstatus, zusatzstatusDto);

			Status status = em.find(Status.class, zusatzstatusDto.getCBez());
			if (status == null) {
				status = new Status(zusatzstatusDto.getCBez());
				try {
					em.persist(status);
				} catch (EntityExistsException e) {
					// Bereits vorhanden
				}
			}

			return zusatzstatusDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeZusatzstatus(ZusatzstatusDto zusatzstatusDto)
			throws EJBExceptionLP {
		if (zusatzstatusDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zusatzstatusDto == null"));
		}
		if (zusatzstatusDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("zusatzstatusDto.getIId() == null"));
		}

		// try {
		Zusatzstatus toRemove = em.find(Zusatzstatus.class,
				zusatzstatusDto.getIId());
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
		// }
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN, e);
		// }

	}

	public void updateZusatzstatus(ZusatzstatusDto zusatzstatusDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (zusatzstatusDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelDto == null"));
		}
		if (zusatzstatusDto.getIId() == null
				|| zusatzstatusDto.getCBez() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"zusatzstatusDto.getIId() == null || zusatzstatusDto.getCBez() == null"));
		}
		zusatzstatusDto.setMandantCNr(theClientDto.getMandant());

		Integer iId = zusatzstatusDto.getIId();

		try {
			Query query = em
					.createNamedQuery("ZusatzstatusfindByMandantCNrCBez");
			query.setParameter(1, zusatzstatusDto.getMandantCNr());
			query.setParameter(2, zusatzstatusDto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Zusatzstatus) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"FERT_ZUSATZSTATUS.UK"));
			}
		} catch (NoResultException ex) {
			// nix
		}

		// try {
		Zusatzstatus zusatzstatus = em.find(Zusatzstatus.class, iId);
		if (zusatzstatus == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setZusatzstatusFromZusatzstatusDto(zusatzstatus, zusatzstatusDto);

		Status status = em.find(Status.class, zusatzstatusDto.getCBez());
		if (status == null) {
			status = new Status(zusatzstatusDto.getCBez());
			try {
				em.persist(status);
			} catch (EntityExistsException e) {
				// Bereits vorhanden
			}
		}

	}

	public void vertauscheLoslagerentnahme(Integer iiDLagerentnahme1,
			Integer iIdLagerentnahme2) {

		Loslagerentnahme oLieferant1 = em.find(Loslagerentnahme.class,
				iiDLagerentnahme1);
		if (oLieferant1 == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Loslagerentnahme oLieferant2 = em.find(Loslagerentnahme.class,
				iIdLagerentnahme2);
		if (oLieferant2 == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Integer iSort1 = oLieferant1.getISort();
		Integer iSort2 = oLieferant2.getISort();
		// iSort der zweiten Loslagerentnahme auf ungueltig setzen, damit UK
		// constraint nicht verletzt wird
		oLieferant2.setISort(new Integer(-1));
		em.merge(oLieferant2);
		em.flush();
		oLieferant1.setISort(iSort2);
		oLieferant2.setISort(iSort1);

	}

	public ZusatzstatusDto zusatzstatusFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Zusatzstatus zusatzstatus = em.find(Zusatzstatus.class, iId);
		if (zusatzstatus == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleZusatzstatusDto(zusatzstatus);
		// }
		// catch (FinderException fe) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// fe);

		// }
	}

	public ZusatzstatusDto zusatzstatusFindByMandantCNrCBez(String mandantCNr,
			String cBez) throws EJBExceptionLP {
		try {
			Query query = em
					.createNamedQuery("ZusatzstatusfindByMandantCNrCBez");
			query.setParameter(1, mandantCNr);
			query.setParameter(2, cBez);
			// @todo getSingleResult oder getResultList ?
			Zusatzstatus zusatzstatus = (Zusatzstatus) query.getSingleResult();
			return assembleZusatzstatusDto(zusatzstatus);
		} catch (NoResultException fe) {
			throw fe;
		}
		// catch (Exception e) {
		// throw new EJBException(e.getMessage());
		// }
	}

	private void setZusatzstatusFromZusatzstatusDto(Zusatzstatus zusatzstatus,
			ZusatzstatusDto zusatzstatusDto) {
		zusatzstatus.setMandantCNr(zusatzstatusDto.getMandantCNr());
		zusatzstatus.setCBez(zusatzstatusDto.getCBez());
		zusatzstatus.setISort(zusatzstatusDto.getISort());
		em.merge(zusatzstatus);
		em.flush();
	}

	private ZusatzstatusDto assembleZusatzstatusDto(Zusatzstatus zusatzstatus) {
		return ZusatzstatusDtoAssembler.createDto(zusatzstatus);
	}

	private ZusatzstatusDto[] assembleZusatzstatusDtos(
			Collection<?> zusatzstatuss) {
		List<ZusatzstatusDto> list = new ArrayList<ZusatzstatusDto>();
		if (zusatzstatuss != null) {
			Iterator<?> iterator = zusatzstatuss.iterator();
			while (iterator.hasNext()) {
				Zusatzstatus zusatzstatus = (Zusatzstatus) iterator.next();
				list.add(assembleZusatzstatusDto(zusatzstatus));
			}
		}
		ZusatzstatusDto[] returnArray = new ZusatzstatusDto[list.size()];
		return (ZusatzstatusDto[]) list.toArray(returnArray);
	}

	private LosgutschlechtDto[] assembleLosgutschlechtDtos(
			Collection<?> zusatzstatuss) {
		List<LosgutschlechtDto> list = new ArrayList<LosgutschlechtDto>();
		if (zusatzstatuss != null) {
			Iterator<?> iterator = zusatzstatuss.iterator();
			while (iterator.hasNext()) {
				Losgutschlecht zusatzstatus = (Losgutschlecht) iterator.next();
				list.add(assembleLosgutschlechtDto(zusatzstatus));
			}
		}
		LosgutschlechtDto[] returnArray = new LosgutschlechtDto[list.size()];
		return (LosgutschlechtDto[]) list.toArray(returnArray);
	}

	public Integer getNextZusatzstatus(TheClientDto theClientDto)
			throws EJBExceptionLP {

		try {
			Query query = em
					.createNamedQuery("ZusatzstatusejbSelectNextReihung");
			query.setParameter(1, theClientDto.getMandant());
			Integer i = (Integer) query.getSingleResult();
			if (i != null) {
				i = 1 + 1;
			} else {
				i = 1;
			}
			return i;
		} catch (NoResultException e) {
			return new Integer(1);
		}

	}

	public BigDecimal[] getGutSchlechtInarbeit(Integer lossollarbeitsplanIId,
			Integer personalIId, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, TheClientDto theClientDto) {
		LosgutschlechtDto[] dtos = losgutschlechtFindByLossollarbeitsplanIId(lossollarbeitsplanIId);
		LossollarbeitsplanDto lossollarbeitsplanDto = lossollarbeitsplanFindByPrimaryKey(lossollarbeitsplanIId);
		LosDto losDto = losFindByPrimaryKey(lossollarbeitsplanDto.getLosIId());

		BigDecimal bdGut = new BigDecimal(0);
		BigDecimal bdSchlecht = new BigDecimal(0);
		BigDecimal bdInarbeit = new BigDecimal(0);
		BigDecimal bdOffen = losDto.getNLosgroesse();

		for (int i = 0; i < dtos.length; i++) {

			if (personalIId != null && dtos[i].getZeitdatenIId() != null) {
				Zeitdaten z = em.find(Zeitdaten.class,
						dtos[i].getZeitdatenIId());

				if (z.getPersonalIId().equals(personalIId)) {

					if ((tVon == null || tVon != null
							&& tVon.before(z.getTZeit()))
							&& (tBis == null || tBis != null
									&& tBis.after(z.getTZeit()))) {
						bdGut = bdGut.add(dtos[i].getNGut());
						bdSchlecht = bdSchlecht.add(dtos[i].getNSchlecht());
						bdInarbeit = bdInarbeit.add(dtos[i].getNInarbeit());
					}

				}

			} else {

				bdGut = bdGut.add(dtos[i].getNGut());
				bdSchlecht = bdSchlecht.add(dtos[i].getNSchlecht());
				bdInarbeit = bdInarbeit.add(dtos[i].getNInarbeit());
			}

		}

		bdOffen = bdOffen.subtract(bdGut).subtract(bdSchlecht);

		boolean bTheoretischeIstZeit = false;

		try {
			ParametermandantDto parameterIstZeit = (ParametermandantDto) getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_PERSONAL,
							ParameterFac.PARAMETER_THEORETISCHE_IST_ZEIT_RECHNUNG);

			bTheoretischeIstZeit = ((Boolean) parameterIstZeit
					.getCWertAsObject());

		} catch (RemoteException ex5) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
		}
		// lt. WH:Wenn TheoretischeIstZeit und "nicht ruesten", dann die
		// Gut/Schlecht Stueck der Ruestzeit des AGs abziehen

		if (bTheoretischeIstZeit == true) {

			// Wenn nicht ruesten
			if (lossollarbeitsplanDto.getAgartCNr() != null) {

				Query query = em
						.createNamedQuery("LossollarbeitsplanfindByLosIIdIArbeitsgangnummer");
				query.setParameter(1, lossollarbeitsplanDto.getLosIId());
				query.setParameter(2,
						lossollarbeitsplanDto.getIArbeitsgangnummer());
				Collection<?> cl = query.getResultList();

				if (cl != null) {
					Iterator<?> iterator = cl.iterator();
					while (iterator.hasNext()) {
						Lossollarbeitsplan lossollarbeitsplanTemp = (Lossollarbeitsplan) iterator
								.next();
						if (lossollarbeitsplanTemp.getAgartCNr() == null) {
							LosgutschlechtDto[] dtosRuestZeit = losgutschlechtFindByLossollarbeitsplanIId(lossollarbeitsplanTemp
									.getIId());

							for (int i = 0; i < dtosRuestZeit.length; i++) {
								bdOffen = bdOffen.subtract(
										dtosRuestZeit[i].getNGut()).subtract(
										dtosRuestZeit[i].getNSchlecht());
							}

						}
					}

				}
			}
		}

		return new BigDecimal[] { bdGut, bdSchlecht, bdInarbeit, bdOffen };
	}

	public BigDecimal[] getGutSchlechtInarbeit(Integer lossollarbeitsplanIId,
			TheClientDto theClientDto) {
		return getGutSchlechtInarbeit(lossollarbeitsplanIId, null, null, null,
				theClientDto);

	}

	public Integer createLoszusatzstatus(LoszusatzstatusDto loszusatzstatusDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (loszusatzstatusDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("loszusatzstatusDto == null"));
		}
		if (loszusatzstatusDto.getLosIId() == null
				|| loszusatzstatusDto.getZusatzstatusIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"loszusatzstatusDto.getLosIId() == null || loszusatzstatusDto.getZusatzstatusIId() == null"));
		}

		try {
			Query query = em
					.createNamedQuery("LoszusatzstatusfindByLosIIdZusatzstatusIId");
			query.setParameter(1, loszusatzstatusDto.getLosIId());
			query.setParameter(2, loszusatzstatusDto.getZusatzstatusIId());
			// @todo getSingleResult oder getResultList ?
			Loszusatzstatus doppelt = (Loszusatzstatus) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("FERT_LOSZUSATZSTATUS.UK"));
		} catch (NoResultException ex) {

		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_LOSZUSATZSTATUS);
			loszusatzstatusDto.setIId(pk);

			loszusatzstatusDto.setPersonalIIdAendern(theClientDto
					.getIDPersonal());
			loszusatzstatusDto.setTAendern(new Timestamp(System
					.currentTimeMillis()));

			Loszusatzstatus loszusatzstatus = new Loszusatzstatus(
					loszusatzstatusDto.getIId(),
					loszusatzstatusDto.getLosIId(),
					loszusatzstatusDto.getZusatzstatusIId(),
					loszusatzstatusDto.getPersonalIIdAendern());
			em.persist(loszusatzstatus);
			em.flush();
			setLoszusatzstatusFromLoszusatzstatusDto(loszusatzstatus,
					loszusatzstatusDto);
			return loszusatzstatusDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

	}

	public void isLosNochErfuellbar(Integer lossollarbeitsplanIId,
			TheClientDto theClientDto) {
		if (lossollarbeitsplanIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("lossollarbeitsplanIId == null"));
		}

		LossollarbeitsplanDto lossollarbeitsplanDto = lossollarbeitsplanFindByPrimaryKey(lossollarbeitsplanIId);

		LosDto losDto = losFindByPrimaryKey(lossollarbeitsplanDto.getLosIId());

		if (losDto.getAuftragpositionIId() != null) {

			String sQuery = "select sum(zeitdaten.n_schlecht) from FLRLosgutschlecht zeitdaten WHERE zeitdaten.flrlossollarbeitsplan.flrlos.i_id="
					+ lossollarbeitsplanDto.getLosIId();

			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = factory.openSession();

			org.hibernate.Query summe = session.createQuery(sQuery);
			List<?> resultList = summe.list();
			Iterator<?> resultListIterator = resultList.iterator();
			BigDecimal mengeSchlecht = (BigDecimal) resultListIterator.next();

			if (mengeSchlecht != null) {

				try {
					AuftragpositionDto auftragpositionDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(
									losDto.getAuftragpositionIId());

					AuftragDto auftragDto = getAuftragFac()
							.auftragFindByPrimaryKey(
									auftragpositionDto.getBelegIId());

					if (auftragpositionDto.getNMenge() != null) {
						// Losgroesse - Schlecht-Stueck muss groesser
						// Auftragsmenge sein

						if (losDto.getNLosgroesse().subtract(mengeSchlecht)
								.doubleValue() < auftragpositionDto.getNMenge()
								.doubleValue()) {
							// Archiveintrag erstellen

							String nachricht = BenutzerFac.NA_AUFTRAG_NICHT_MEHR_ERFUELLBAR
									+ " Auftrag "
									+ auftragDto.getCNr()
									+ " nicht mehr erf\u00FCllbar, Losnummer: "
									+ losDto.getCNr()
									+ ", Losgr\u00F6\u00DFe: "
									+ losDto.getNLosgroesse()
									+ ", Schlechtst\u00FCck: "
									+ mengeSchlecht
									+ ", Menge Auftragsposition: "
									+ auftragpositionDto.getNMenge();

							getBenutzerFac()
									.sendJmsMessageMitArchiveintrag(
											BenutzerFac.NA_AUFTRAG_NICHT_MEHR_ERFUELLBAR,
											nachricht, theClientDto);
						}

					}

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}

		}

	}

	public Integer createLosgutschlecht(LosgutschlechtDto losgutschlechtDto,
			TheClientDto theClientDto) {

		if (losgutschlechtDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("losgutschlechtDto == null"));
		}
		if (losgutschlechtDto.getLossollarbeitsplanIId() == null
				|| losgutschlechtDto.getNGut() == null
				|| losgutschlechtDto.getNSchlecht() == null
				|| losgutschlechtDto.getNInarbeit() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"losgutschlechtDto.getLossollarbeitsplanIId() == null || losgutschlechtDto.getNGut() == null || losgutschlechtDto.getNSchlecht() == null || losgutschlechtDto.getNInarbeit() == null"));
		}

		if (losgutschlechtDto.getZeitdatenIId() == null
				&& losgutschlechtDto.getMaschinenzeitdatenIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"losgutschlechtDto.getZeitdatenIId() == null && getMaschinenzeitdatenIId() ==null"));

		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_LOSGUTSCHLECHT);
			losgutschlechtDto.setIId(pk);

			Losgutschlecht loszusatzstatus = new Losgutschlecht(
					losgutschlechtDto.getIId(),
					losgutschlechtDto.getLossollarbeitsplanIId(),
					losgutschlechtDto.getNGut(),
					losgutschlechtDto.getNSchlecht(),
					losgutschlechtDto.getNInarbeit());
			em.persist(loszusatzstatus);
			em.flush();
			setLosgutschlechtFromLosutschlechtDto(loszusatzstatus,
					losgutschlechtDto);

			isLosNochErfuellbar(losgutschlechtDto.getLossollarbeitsplanIId(),
					theClientDto);

			return losgutschlechtDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

	}

	public void createLosgutschlechtMitMaschine(
			LosgutschlechtDto losgutschlechtDto, TheClientDto theClientDto) {

		createLosgutschlecht(losgutschlechtDto, theClientDto);

		// Bei Aenderungen auch in PersonalFacBeanWS.bucheZeitMitStueckmeldung
		// aendern!!!!!

		LossollarbeitsplanDto lossollDto = lossollarbeitsplanFindByPrimaryKey(losgutschlechtDto
				.getLossollarbeitsplanIId());

		Query query = em
				.createNamedQuery("LossollarbeitsplanfindByLosIIdIArbeitsgangnummer");
		query.setParameter(1, lossollDto.getLosIId());
		query.setParameter(2, lossollDto.getIArbeitsgangnummer());
		Collection<?> cl = query.getResultList();

		// Beschreibung lt. WH: Es muessen zusaetzlich alle Sollpositionen
		// desselben AG welche die AG-Art Laufzeit haben und nur
		// Maschinenzeit
		// sind, ebenfalls dieselben Gut-Schlecht-Stueck gebucht werden

		// WH: Wenn der Ausloeser der niedrigste UAG im AG ist und
		// Umspannzeit ist, dann Gut-Schlecht-Stueck auf Maschinenzeit
		// buchen

		boolean bIchBinDerErsteUnterarbeitsgangMitUmsapnnzeit = false;

		if (cl != null) {
			Iterator<?> iterator = cl.iterator();
			while (iterator.hasNext()) {
				Lossollarbeitsplan lossollarbeitsplanTemp = (Lossollarbeitsplan) iterator
						.next();
				if (lossollarbeitsplanTemp.getAgartCNr() != null
						&& lossollarbeitsplanTemp.getAgartCNr().equals(
								StuecklisteFac.AGART_UMSPANNZEIT)) {

					if (lossollarbeitsplanTemp.getIId().equals(
							lossollDto.getIId())) {
						bIchBinDerErsteUnterarbeitsgangMitUmsapnnzeit = true;

					}
					break;
				}
			}

			if (bIchBinDerErsteUnterarbeitsgangMitUmsapnnzeit == true) {
				iterator = cl.iterator();
				while (iterator.hasNext()) {
					Lossollarbeitsplan lossollarbeitsplanTemp = (Lossollarbeitsplan) iterator
							.next();

					if (!lossollarbeitsplanTemp.getIId().equals(
							losgutschlechtDto.getLossollarbeitsplanIId())) {

						if (lossollarbeitsplanTemp.getAgartCNr() != null
								&& (Helper.short2boolean(lossollarbeitsplanTemp
										.getBNurmaschinenzeit()) && lossollarbeitsplanTemp
										.getAgartCNr().equals(
												StuecklisteFac.AGART_LAUFZEIT))) {

							losgutschlechtDto
									.setLossollarbeitsplanIId(lossollarbeitsplanTemp
											.getIId());

							// Die letzte Maschinenzeit des Arbeitganges
							// verwenden
							Session session = FLRSessionFactory.getFactory()
									.openSession();
							org.hibernate.Criteria liste = session
									.createCriteria(FLRMaschinenzeitdaten.class);
							if (lossollarbeitsplanTemp.getMaschineIId() != null)
								// Maschine nur filtern wenn im Arbeitsgang
								// definiert, sonst erste verwenden (bei ?? fuer
								// Maschine am Terminal)
								liste.add(Expression
										.eq("maschine_i_id",
												lossollarbeitsplanTemp
														.getMaschineIId()));
							liste.add(Expression.eq("lossollarbeitsplan_i_id",
									lossollarbeitsplanTemp.getIId()));

							liste.addOrder(Order.desc("t_von"));
							liste.setMaxResults(1);
							List<?> letztesKommt = liste.list();

							Iterator it = letztesKommt.iterator();

							if (it.hasNext()) {
								FLRMaschinenzeitdaten mz = (FLRMaschinenzeitdaten) it
										.next();
								losgutschlechtDto.setMaschinenzeitdatenIId(mz
										.getI_id());
								losgutschlechtDto.setZeitdatenIId(null);
								createLosgutschlecht(losgutschlechtDto,
										theClientDto);
							}

						}
					}
				}
			}

		}

	}

	public Integer createLostechniker(LostechnikerDto lostechnikerDto,
			TheClientDto theClientDto) {

		if (lostechnikerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("lostechnikerDto == null"));
		}
		if (lostechnikerDto.getLosIId() == null
				|| lostechnikerDto.getPersonalIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"lostechnikerDto.getLosIId() == null || lostechnikerDto.getPersonalIId() == null"));
		}
		try {
			Query query = em
					.createNamedQuery("LostechnikerfindByLosIIdPersonalIId");
			query.setParameter(1, lostechnikerDto.getLosIId());
			query.setParameter(2, lostechnikerDto.getPersonalIId());
			// @todo getSingleResult oder getResultList ?
			Lostechniker doppelt = (Lostechniker) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("FERT_LOSTECHNIKER.UK"));
		} catch (NoResultException ex) {

		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_LOSTECHNIKER);
			lostechnikerDto.setIId(pk);

			Lostechniker lostechniker = new Lostechniker(
					lostechnikerDto.getIId(), lostechnikerDto.getLosIId(),
					lostechnikerDto.getPersonalIId());
			em.persist(lostechniker);
			em.flush();
			setLostechnikerFromLostechnikerDto(lostechniker, lostechnikerDto);

			return lostechnikerDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

	}

	public void terminVerschieben(Integer losIId, Timestamp tsBeginnNeu,
			Timestamp tsEndeNeu, TheClientDto theClientDto) {

		Los los = em.find(Los.class, losIId);

		if (los.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)
				|| los.getStatusCNr().equals(FertigungFac.STATUS_AUSGEGEBEN)
				|| los.getStatusCNr().equals(FertigungFac.STATUS_IN_PRODUKTION)
				|| los.getStatusCNr().equals(FertigungFac.STATUS_GESTOPPT)
				|| los.getStatusCNr().equals(FertigungFac.STATUS_TEILERLEDIGT)) {

			los.setTProduktionsbeginn(new java.sql.Date(tsBeginnNeu.getTime()));
			los.setTProduktionsende(new java.sql.Date(tsEndeNeu.getTime()));

			em.merge(los);
			em.flush();

			LossollmaterialDto[] sollmat = lossollmaterialFindByLosIId(losIId);

			for (int i = 0; i < sollmat.length; i++) {
				try {

					getFehlmengeFac().aktualisiereFehlmenge(
							LocaleFac.BELEGART_LOS, sollmat[i].getIId(), false,
							theClientDto);

					ArtikelreservierungDto reservierungDto = getReservierungFac()
							.artikelreservierungFindByBelegartCNrBelegartPositionIIdOhneExc(
									LocaleFac.BELEGART_LOS, sollmat[i].getIId());
					if (reservierungDto != null) {
						if (reservierungDto != null
								&& reservierungDto.getNMenge().doubleValue() >= 0) {
							reservierungDto.setTLiefertermin(tsBeginnNeu);
						} else {
							reservierungDto.setTLiefertermin(tsEndeNeu);
						}
						getReservierungFac().updateArtikelreservierung(
								reservierungDto);
					}

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}

		} else if (los.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
					new Exception("los " + los.getCNr()
							+ " ist bereits erledigt"));

		} else if (los.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT,
					new Exception("los " + los.getCNr()
							+ " ist bereits storniert"));

		} else if (los.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN,
					new Exception("los " + los.getCNr()
							+ " ist noch nicht ausgegeben"));

		}

	}

	public void removeLoszusatzstatus(LoszusatzstatusDto loszusatzstatusDto)
			throws EJBExceptionLP {
		if (loszusatzstatusDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("loszusatzstatusDto == null"));
		}
		if (loszusatzstatusDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("loszusatzstatusDto.getIId() == null"));
		}

		// try {
		Loszusatzstatus toRemove = em.find(Loszusatzstatus.class,
				loszusatzstatusDto.getIId());
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
		// }
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN, e);
		// }

	}

	public void removeLosgutschlecht(LosgutschlechtDto losgutschlechtDto) {
		if (losgutschlechtDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("losgutschlechtDto == null"));
		}
		if (losgutschlechtDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("losgutschlechtDto.getIId() == null"));
		}

		Losgutschlecht toRemove = em.find(Losgutschlecht.class,
				losgutschlechtDto.getIId());
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

	public void removeLostechniker(LostechnikerDto lostechnikerDto) {
		if (lostechnikerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("lostechnikerDto == null"));
		}
		if (lostechnikerDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("lostechnikerDto.getIId() == null"));
		}

		Lostechniker toRemove = em.find(Lostechniker.class,
				lostechnikerDto.getIId());
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

	public void updateLoszusatzstatus(LoszusatzstatusDto loszusatzstatusDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (loszusatzstatusDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("loszusatzstatusDto == null"));
		}
		if (loszusatzstatusDto.getIId() == null
				|| loszusatzstatusDto.getLosIId() == null
				|| loszusatzstatusDto.getZusatzstatusIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"loszusatzstatusDto.getIId() == null || loszusatzstatusDto.getLosIId() == null || loszusatzstatusDto.getZusatzstatusIId() == null"));
		}

		Integer iId = loszusatzstatusDto.getIId();

		// try {
		Query query = em
				.createNamedQuery("LoszusatzstatusfindByLosIIdZusatzstatusIId");
		query.setParameter(1, loszusatzstatusDto.getLosIId());
		query.setParameter(2, loszusatzstatusDto.getZusatzstatusIId());
		Integer iIdVorhanden = ((Loszusatzstatus) query.getSingleResult())
				.getIId();
		if (iId.equals(iIdVorhanden) == false) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("FERT_LOSZUSATZSTATUS.UK"));
		}

		// }
		// catch (FinderException ex) {
		//
		// }

		// try {

		loszusatzstatusDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		loszusatzstatusDto
				.setTAendern(new Timestamp(System.currentTimeMillis()));

		Loszusatzstatus loszusatzstatus = em.find(Loszusatzstatus.class, iId);
		if (loszusatzstatus == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setLoszusatzstatusFromLoszusatzstatusDto(loszusatzstatus,
				loszusatzstatusDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public void updateLosgutschlecht(LosgutschlechtDto losgutschlechtDto,
			TheClientDto theClientDto) {

		if (losgutschlechtDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("losgutschlechtDto == null"));
		}
		if (losgutschlechtDto.getIId() == null
				|| losgutschlechtDto.getLossollarbeitsplanIId() == null
				|| losgutschlechtDto.getNGut() == null
				|| losgutschlechtDto.getNSchlecht() == null
				|| losgutschlechtDto.getNInarbeit() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"losgutschlechtDto.getIId() == null || losgutschlechtDto.getLossollarbeitsplanIId() == null || losgutschlechtDto.getNGut() == null || losgutschlechtDto.getNSchlecht() == null || losgutschlechtDto.getNInarbeit() == null"));
		}

		if (losgutschlechtDto.getZeitdatenIId() == null
				&& losgutschlechtDto.getMaschinenzeitdatenIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"losgutschlechtDto.getZeitdatenIId() == null && getMaschinenzeitdatenIId() ==null"));

		}

		Integer iId = losgutschlechtDto.getIId();
		Losgutschlecht losgutschlecht = em.find(Losgutschlecht.class, iId);
		if (losgutschlecht == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setLosgutschlechtFromLosutschlechtDto(losgutschlecht, losgutschlechtDto);
		isLosNochErfuellbar(losgutschlechtDto.getLossollarbeitsplanIId(),
				theClientDto);
	}

	public void updateLostechniker(LostechnikerDto lostechnikerDto,
			TheClientDto theClientDto) {

		if (lostechnikerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("lostechnikerDto == null"));
		}
		if (lostechnikerDto.getIId() == null
				|| lostechnikerDto.getLosIId() == null
				|| lostechnikerDto.getPersonalIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"lostechnikerDto.getIId() == null || lostechnikerDto.getLosIId() == null 	|| lostechnikerDto.getPersonalIId() == null"));
		}

		Integer iId = lostechnikerDto.getIId();

		Query query = em
				.createNamedQuery("LostechnikerfindByLosIIdPersonalIId");
		query.setParameter(1, lostechnikerDto.getLosIId());
		query.setParameter(2, lostechnikerDto.getPersonalIId());
		try {
			Lostechniker doppelt = (Lostechniker) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("FERT_LOSTECHNIKER.UK"));
		} catch (NoResultException e) {
			//
		}

		Lostechniker lostechniker = em.find(Lostechniker.class, iId);
		if (lostechniker == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setLostechnikerFromLostechnikerDto(lostechniker, lostechnikerDto);

	}

	public LoszusatzstatusDto loszusatzstatusFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Loszusatzstatus loszusatzstatus = em.find(Loszusatzstatus.class, iId);
		if (loszusatzstatus == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}

		return assembleLoszusatzstatusDto(loszusatzstatus);
		// }
		// catch (FinderException fe) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// fe);

		// }

	}

	public LosgutschlechtDto losgutschlechtFindByPrimaryKey(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		Losgutschlecht losgutschlecht = em.find(Losgutschlecht.class, iId);
		if (losgutschlecht == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLosgutschlechtDto(losgutschlecht);
	}

	public LosgutschlechtDto[] losgutschlechtFindAllFehler(Integer losIId) {
		if (losIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("losIId == null"));

		}

		SessionFactory factory = FLRSessionFactory.getFactory();
		org.hibernate.Session session = factory.openSession();
		String queryString = "SELECT lgs FROM FLRLosgutschlecht lgs WHERE  lgs.flrlossollarbeitsplan.flrlos.i_id="
				+ losIId + " AND lgs.flrfehler IS NOT NULL ";
		queryString += " ORDER BY lgs.flrlossollarbeitsplan.i_arbeitsgangsnummer ";

		org.hibernate.Query query = session.createQuery(queryString);

		List<?> resultList = query.list();
		Iterator it = resultList.iterator();

		LosgutschlechtDto[] losgutschlechtDtos = new LosgutschlechtDto[resultList
				.size()];

		int i = 0;
		while (it.hasNext()) {
			FLRLosgutschlecht lgs = (FLRLosgutschlecht) it.next();

			losgutschlechtDtos[i] = losgutschlechtFindByPrimaryKey(lgs
					.getI_id());
			i++;
		}

		session.close();

		return losgutschlechtDtos;
	}

	public LosgutschlechtDto[] losgutschlechtFindByLossollarbeitsplanIId(
			Integer losIId) {
		if (losIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("losIId == null"));

		}
		LosgutschlechtDto[] losgutschlechtDtos = null;
		// try {
		Query query = em
				.createNamedQuery("LosgutschlechtFindByLossollarbeitsplanIId");
		query.setParameter(1, losIId);
		Collection<?> cl = query.getResultList();
		losgutschlechtDtos = assembleLosgutschlechtDtos(cl);
		return losgutschlechtDtos;
	}

	public ArrayList<Integer> getAllPersonalIIdEinesSollarbeitsplansUeberLogGutSchlecht(
			Integer loslollarbeitsplanIId) {

		ArrayList alPersonalIIds = new ArrayList();
		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT distinct l.flrzeitdaten.personal_i_id FROM FLRLosgutschlecht l WHERE l.flrzeitdaten.personal_i_id IS NOT NULL AND l.flrlossollarbeitsplan.i_id="
				+ loslollarbeitsplanIId;

		org.hibernate.Query query = session.createQuery(queryString);
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			Integer personal_i_id = (Integer) resultListIterator.next();
			alPersonalIIds.add(personal_i_id);
		}

		return alPersonalIIds;
	}

	public LosgutschlechtDto[] losgutschlechtFindByZeitdatenIId(
			Integer zeitdatenIId) {
		if (zeitdatenIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("zeitdatenIId == null"));

		}
		LosgutschlechtDto[] losgutschlechtDtos = null;
		// try {
		Query query = em.createNamedQuery("LosgutschlechtFindByZeitdatenIId");
		query.setParameter(1, zeitdatenIId);
		Collection<?> cl = query.getResultList();
		losgutschlechtDtos = assembleLosgutschlechtDtos(cl);
		return losgutschlechtDtos;
	}

	public LoszusatzstatusDto loszusatzstatusFindByLosIIdZusatzstatusIId(
			Integer losIId, Integer zusatzstatusIId) throws EJBExceptionLP {
		try {
			Query query = em
					.createNamedQuery("LoszusatzstatusfindByLosIIdZusatzstatusIId");
			query.setParameter(1, losIId);
			query.setParameter(2, zusatzstatusIId);
			return assembleLoszusatzstatusDto((Loszusatzstatus) query
					.getSingleResult());
		} catch (EJBExceptionLP e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}
	}

	public LoszusatzstatusDto loszusatzstatusFindByLosIIdZusatzstatusIIdOhneExc(
			Integer losIId, Integer zusatzstatusIId) {
		try {
			Query query = em
					.createNamedQuery("LoszusatzstatusfindByLosIIdZusatzstatusIId");
			query.setParameter(1, losIId);
			query.setParameter(2, zusatzstatusIId);
			return assembleLoszusatzstatusDto((Loszusatzstatus) query
					.getSingleResult());
		} catch (NoResultException ex) {
			//
		}
		return null;
	}

	private void setLoszusatzstatusFromLoszusatzstatusDto(
			Loszusatzstatus loszusatzstatus,
			LoszusatzstatusDto loszusatzstatusDto) {
		loszusatzstatus.setLosIId(loszusatzstatusDto.getLosIId());
		loszusatzstatus.setZusatzstatusIId(loszusatzstatusDto
				.getZusatzstatusIId());
		loszusatzstatus.setPersonalIIdAendern(loszusatzstatusDto
				.getPersonalIIdAendern());
		loszusatzstatus.setTAendern(loszusatzstatusDto.getTAendern());
		em.merge(loszusatzstatus);
		em.flush();
	}

	private void setLosgutschlechtFromLosutschlechtDto(
			Losgutschlecht losgutschlecht, LosgutschlechtDto losgutschlechtDto) {
		losgutschlecht.setLossollarbeitsplanIId(losgutschlechtDto
				.getLossollarbeitsplanIId());
		losgutschlecht.setZeitdatenIId(losgutschlechtDto.getZeitdatenIId());
		losgutschlecht.setNGut(losgutschlechtDto.getNGut());
		losgutschlecht.setNSchlecht(losgutschlechtDto.getNSchlecht());
		losgutschlecht.setNInarbeit(losgutschlechtDto.getNInarbeit());
		losgutschlecht.setMaschinenzeitdatenIId(losgutschlechtDto
				.getMaschinenzeitdatenIId());
		losgutschlecht.setCKommentar(losgutschlechtDto.getCKommentar());
		losgutschlecht.setFehlerIId(losgutschlechtDto.getFehlerIId());
		em.merge(losgutschlecht);
		em.flush();
	}

	private void setLostechnikerFromLostechnikerDto(Lostechniker lostechniker,
			LostechnikerDto lostechnikerDto) {
		lostechniker.setLosIId(lostechnikerDto.getLosIId());
		lostechniker.setPersonalIId(lostechnikerDto.getPersonalIId());

		em.merge(lostechniker);
		em.flush();
	}

	private LoszusatzstatusDto assembleLoszusatzstatusDto(
			Loszusatzstatus loszusatzstatus) {
		return LoszusatzstatusDtoAssembler.createDto(loszusatzstatus);
	}

	private LosgutschlechtDto assembleLosgutschlechtDto(
			Losgutschlecht losgutschlecht) {
		return LosgutschlechtDtoAssembler.createDto(losgutschlecht);
	}

	private LoszusatzstatusDto[] assembleLoszusatzstatusDtos(
			Collection<?> loszusatzstatuss) {
		List<LoszusatzstatusDto> list = new ArrayList<LoszusatzstatusDto>();
		if (loszusatzstatuss != null) {
			Iterator<?> iterator = loszusatzstatuss.iterator();
			while (iterator.hasNext()) {
				Loszusatzstatus loszusatzstatus = (Loszusatzstatus) iterator
						.next();
				list.add(assembleLoszusatzstatusDto(loszusatzstatus));
			}
		}
		LoszusatzstatusDto[] returnArray = new LoszusatzstatusDto[list.size()];
		return (LoszusatzstatusDto[]) list.toArray(returnArray);
	}
}
