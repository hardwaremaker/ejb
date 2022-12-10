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
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jboss.ejb3.annotation.TransactionTimeout;
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

import com.lp.server.anfrage.ejb.Anfrageposition;
import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.artikel.ejb.Artkla;
import com.lp.server.artikel.ejb.ArtklaQuery;
import com.lp.server.artikel.ejb.Lager;
import com.lp.server.artikel.ejb.LagerQuery;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelgruppe;
import com.lp.server.artikel.fastlanereader.generated.FLRLagerbewegung;
import com.lp.server.artikel.fastlanereader.generated.FLRLagerwert;
import com.lp.server.artikel.fastlanereader.generated.FLRWareneingangsreferez;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelfehlmengeDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikelreservierungDto;
import com.lp.server.artikel.service.ArtikelsperrenDto;
import com.lp.server.artikel.service.ErsatztypenDto;
import com.lp.server.artikel.service.GeometrieDto;
import com.lp.server.artikel.service.GeraetesnrDto;
import com.lp.server.artikel.service.HandlagerbewegungDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.artikel.service.LagerbewegungDto;
import com.lp.server.artikel.service.MaterialDto;
import com.lp.server.artikel.service.SeriennrChargennrAufLagerDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.artikel.service.SperrenDto;
import com.lp.server.artikel.service.WarenzugangsreferenzDto;
import com.lp.server.auftrag.ejb.Auftrag;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragposition;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragNachkalkulationDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.AuftragzeitenDto;
import com.lp.server.benutzer.service.BenutzerFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.ejb.Bestellposition;
import com.lp.server.bestellung.ejb.Bestellung;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.BestellvorschlagDto;
import com.lp.server.fertigung.ejb.Bedarfsuebernahme;
import com.lp.server.fertigung.ejb.Erledigtermaterialwert;
import com.lp.server.fertigung.ejb.Los;
import com.lp.server.fertigung.ejb.LosQuery;
import com.lp.server.fertigung.ejb.Losablieferung;
import com.lp.server.fertigung.ejb.Losgutschlecht;
import com.lp.server.fertigung.ejb.Losistmaterial;
import com.lp.server.fertigung.ejb.Loslagerentnahme;
import com.lp.server.fertigung.ejb.LoslagerentnahmeQuery;
import com.lp.server.fertigung.ejb.Loslosklasse;
import com.lp.server.fertigung.ejb.Lospruefplan;
import com.lp.server.fertigung.ejb.Lossollarbeitsplan;
import com.lp.server.fertigung.ejb.LossollarbeitsplanQuery;
import com.lp.server.fertigung.ejb.Lossollmaterial;
import com.lp.server.fertigung.ejb.LossollmaterialQuery;
import com.lp.server.fertigung.ejb.Lostechniker;
import com.lp.server.fertigung.ejb.Loszusatzstatus;
import com.lp.server.fertigung.ejb.Wiederholendelose;
import com.lp.server.fertigung.ejb.Zusatzstatus;
import com.lp.server.fertigung.ejb.ZusatzstatusQuery;
import com.lp.server.fertigung.fastlanereader.generated.FLRLos;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosReport;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosablieferung;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosgutschlecht;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosistmaterial;
import com.lp.server.fertigung.fastlanereader.generated.FLRLossollarbeitsplan;
import com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial;
import com.lp.server.fertigung.fastlanereader.generated.FLRLoszusatzstatus;
import com.lp.server.fertigung.fastlanereader.generated.FLROffeneags;
import com.lp.server.fertigung.fastlanereader.generated.FLRWiederholendelose;
import com.lp.server.fertigung.service.AblieferungByAuftragResultDto;
import com.lp.server.fertigung.service.AnzahlInFertigungDto;
import com.lp.server.fertigung.service.BedarfsuebernahmeDto;
import com.lp.server.fertigung.service.BedarfsuebernahmeDtoAssembler;
import com.lp.server.fertigung.service.BucheSerienChnrAufLosDto;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.KapazitaetsvorschauDetailDto;
import com.lp.server.fertigung.service.KapazitaetsvorschauDto;
import com.lp.server.fertigung.service.LosArbeitsplanZeitVergleichDto;
import com.lp.server.fertigung.service.LosAusAuftragDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosDtoAssembler;
import com.lp.server.fertigung.service.LosInfosFuerWerteAusUnterlosen;
import com.lp.server.fertigung.service.LosablieferungDto;
import com.lp.server.fertigung.service.LosablieferungDtoAssembler;
import com.lp.server.fertigung.service.LosablieferungResultDto;
import com.lp.server.fertigung.service.LosablieferungTerminalDto;
import com.lp.server.fertigung.service.LosgutschlechtDto;
import com.lp.server.fertigung.service.LosgutschlechtDtoAssembler;
import com.lp.server.fertigung.service.LosgutschlechtRueckmeldungDto;
import com.lp.server.fertigung.service.LosistmaterialDto;
import com.lp.server.fertigung.service.LosistmaterialDtoAssembler;
import com.lp.server.fertigung.service.LoslagerentnahmeDto;
import com.lp.server.fertigung.service.LoslagerentnahmeDtoAssembler;
import com.lp.server.fertigung.service.LoslosklasseDto;
import com.lp.server.fertigung.service.LoslosklasseDtoAssembler;
import com.lp.server.fertigung.service.LospruefplanDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDtoAssembler;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.fertigung.service.LossollmaterialDtoAssembler;
import com.lp.server.fertigung.service.LostechnikerDto;
import com.lp.server.fertigung.service.LostechnikerDtoAssembler;
import com.lp.server.fertigung.service.LoszusatzstatusDto;
import com.lp.server.fertigung.service.LoszusatzstatusDtoAssembler;
import com.lp.server.fertigung.service.RestmengeUndChargennummerDto;
import com.lp.server.fertigung.service.RueckgabeMehrereLoseAusgeben;
import com.lp.server.fertigung.service.TraceImportDto;
import com.lp.server.fertigung.service.WiederholendeloseDto;
import com.lp.server.fertigung.service.WiederholendeloseDtoAssembler;
import com.lp.server.fertigung.service.ZusatzstatusDto;
import com.lp.server.fertigung.service.ZusatzstatusDtoAssembler;
import com.lp.server.kueche.service.TageslosDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.personal.ejb.Zeitdaten;
import com.lp.server.personal.ejb.Zeitmodell;
import com.lp.server.personal.fastlanereader.generated.FLRMaschine;
import com.lp.server.personal.fastlanereader.generated.FLRMaschinengruppe;
import com.lp.server.personal.fastlanereader.generated.FLRMaschinenzeitdaten;
import com.lp.server.personal.fastlanereader.generated.FLRZeitdaten;
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.personal.service.SollverfuegbarkeitDto;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeitmodelltagDto;
import com.lp.server.personal.service.ZeitverteilungDto;
import com.lp.server.personal.service.ZeitverteilungDtoAssembler;
import com.lp.server.stueckliste.ejb.Fertigungsgruppe;
import com.lp.server.stueckliste.ejb.Stueckliste;
import com.lp.server.stueckliste.ejb.Stuecklistearbeitsplan;
import com.lp.server.stueckliste.ejb.StuecklistearbeitsplanQuery;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.stueckliste.service.PosersatzDto;
import com.lp.server.stueckliste.service.StklagerentnahmeDto;
import com.lp.server.stueckliste.service.StklpruefplanDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklisteMitStrukturDto;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.ejb.Status;
import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.BelegnummerGeneratorObj;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.pkgenerator.format.LpBelegnummer;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.EinheitDto;
import com.lp.server.system.service.HttpProxyConfig;
import com.lp.server.system.service.HttpRequestConfig;
import com.lp.server.system.service.KeyvalueDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.SystemServicesFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.ArtikelId;
import com.lp.server.util.Facade;
import com.lp.server.util.HvHttpHeader;
import com.lp.server.util.HvHttpHeaderTransformer;
import com.lp.server.util.HvOptional;
import com.lp.server.util.IHttpHeaderKeys;
import com.lp.server.util.KundeId;
import com.lp.server.util.LosId;
import com.lp.server.util.LossollmaterialId;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.logger.HvDtoLogger;
import com.lp.service.HttpStatusCodeException;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.KeyValue;
import com.lp.util.Pair;
import com.lp.util.VerwendeteSeriennummerLookup;
import com.lp.util.chart.CustomXYBarRenderer;

@Stateless
public class FertigungFacBean extends Facade implements FertigungFac, FertigungFacLocal {
	@PersistenceContext
	private EntityManager em;
	@Resource
	private SessionContext context;

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public LosDto createLoseAusAuftrag(LosDto losDto, Integer auftragIId, TheClientDto theClientDto) {

		LosDto losReturn = null;
		try {
			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(auftragIId);
			KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(), theClientDto);
			if (auftragDto.getStatusCNr().equals(LocaleFac.STATUS_ANGELEGT)
					|| auftragDto.getStatusCNr().equals(LocaleFac.STATUS_OFFEN)
					|| auftragDto.getStatusCNr().equals(LocaleFac.STATUS_TEILERLEDIGT)) {
				AuftragpositionDto[] dtos = getAuftragpositionFac().auftragpositionFindByAuftrag(auftragIId);
				for (int i = 0; i < dtos.length; i++) {
					StuecklisteDto stuecklisteDto = getStuecklisteFac()
							.stuecklisteFindByMandantCNrArtikelIIdOhneExc(dtos[i].getArtikelIId(), theClientDto);
					if (stuecklisteDto != null) {
						if (dtos[i].getNOffeneMenge() != null && dtos[i].getNOffeneMenge().doubleValue() > 0)
							losDto.setAuftragpositionIId(dtos[i].getIId());
						losDto.setNLosgroesse(dtos[i].getNOffeneMenge());

						// SP1595 Termine berechnen

						Timestamp tAuftragsliefertermin = dtos[i].getTUebersteuerbarerLiefertermin();

						Timestamp tEnde = Helper.addiereTageZuTimestamp(tAuftragsliefertermin,
								-kdDto.getILieferdauer());

						int durchlaufzeit = 0;
						if (stuecklisteDto.getNDefaultdurchlaufzeit() != null) {
							durchlaufzeit = stuecklisteDto.getNDefaultdurchlaufzeit().intValue();
						}

						Timestamp tBeginn = Helper.addiereTageZuTimestamp(tEnde, -durchlaufzeit);

						if (tBeginn.before(Helper.cutTimestamp(new Timestamp(System.currentTimeMillis())))) {

							tBeginn = Helper.cutTimestamp(new Timestamp(System.currentTimeMillis()));
							tEnde = Helper.addiereTageZuTimestamp(tBeginn, durchlaufzeit);
						}

						losDto.setTProduktionsbeginn(new java.sql.Date(tBeginn.getTime()));
						losDto.setTProduktionsende(new java.sql.Date(tEnde.getTime()));

						losDto.setStuecklisteIId(stuecklisteDto.getIId());
						losReturn = getFertigungFac().createLos(losDto, theClientDto);
					}
				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return losReturn;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ArrayList<String> importiereGeraeteseriennummernablieferung(Integer losIId, List<String[]> daten,
			TheClientDto theClientDto) {

		ArrayList<String> alFehler = new ArrayList<String>();

		try {
			LossollmaterialDto[] sollmatDtos = getFertigungFac().lossollmaterialFindByLosIIdOrderByISort(losIId);

			if (daten != null && daten.size() > 1) {

				// Es gibt keine Ueberschrift
				int iZeile = 0;
				for (int i = 0; i < daten.size(); i++) {
					String[] zeile = daten.get(i);
					iZeile++;
					if (zeile.length > 1) {
						String snrAblieferung = zeile[0];

						ArrayList<GeraetesnrDto> alGeraetesnr = new ArrayList<GeraetesnrDto>();

						int iAnzahlGsnrs = 0;

						for (int k = 0; k < sollmatDtos.length; k++) {

							ArtikelDto artikelDtoMaterial = getArtikelFac()
									.artikelFindByPrimaryKey(sollmatDtos[k].getArtikelIId(), theClientDto);

							if (artikelDtoMaterial.isSeriennrtragend()) {
								iAnzahlGsnrs++;
							}
						}

						for (int j = 1; j < zeile.length; j++) {
							String gsnr = zeile[j];
							if (gsnr != null && gsnr.length() > 0) {

								HashMap<String, Integer> hmOffeneSnrs = new HashMap<String, Integer>();

								for (int k = 0; k < sollmatDtos.length; k++) {

									ArtikelDto artikelDtoMaterial = getArtikelFac()
											.artikelFindByPrimaryKey(sollmatDtos[k].getArtikelIId(), theClientDto);

									if (artikelDtoMaterial.isSeriennrtragend()) {

										ArrayList<String> offeneSnrs = getFertigungFac()
												.getOffeneGeraeteSnrEinerSollPosition(sollmatDtos[k].getIId(),
														theClientDto);

										for (int x = 0; x < offeneSnrs.size(); x++) {
											hmOffeneSnrs.put(offeneSnrs.get(x), artikelDtoMaterial.getIId());
										}

									}

								}

								if (hmOffeneSnrs.containsKey(gsnr)) {
									GeraetesnrDto gnsrDto = new GeraetesnrDto();

									gnsrDto.setArtikelIId(hmOffeneSnrs.get(gsnr));
									gnsrDto.setCSnr(gsnr);

									alGeraetesnr.add(gnsrDto);
								} else {
									alFehler.add("Die Seriennummer '" + gsnr
											+ "' ist dem Los nicht zugeordnet bzw. in einer Losablieferung schon verwendet (Zeile "
											+ iZeile + ") ");
									continue;
								}
							}

						}

						if (iAnzahlGsnrs != alGeraetesnr.size()) {
							alFehler.add("Es wurden " + iAnzahlGsnrs + " Seriennummern erwartet, " + (zeile.length - 1)
									+ " im Import angegeben, jedoch " + alGeraetesnr.size() + " im Los gefunden (Zeile "
									+ iZeile + ") ");
						} else {

							try {
								getFertigungFac().pruefePositionenMitSollsatzgroesseUnterschreitung(losIId,
										BigDecimal.ONE, theClientDto);

							} catch (EJBExceptionLP e) {
								if (e.getCode() == EJBExceptionLP.FEHLER_FERTIGUNG_SOLLSATZGROESSE_UNTERSCHRITTEN) {

									StringBuffer fehler = (StringBuffer) e.getAlInfoForTheClient().get(0);

									alFehler.add(fehler.toString() + " (Zeile " + iZeile + ") ");
									continue;
								}
							}

							LosablieferungDto laDto = new LosablieferungDto();
							laDto.setLosIId(losIId);
							laDto.setNMenge(BigDecimal.ONE);

							List<SeriennrChargennrMitMengeDto> snr = SeriennrChargennrMitMengeDto
									.erstelleDtoAusEinerSeriennummer(snrAblieferung);
							snr.get(0).setAlGeraetesnr(alGeraetesnr);

							laDto.setSeriennrChargennrMitMenge(snr);

							try {
								getFertigungFac().createLosablieferung(laDto, theClientDto, false);
							} catch (EJBExceptionLP e) {

								alFehler.add(e.getMessage() + " (Zeile " + iZeile + ") ");

							}
						}

					} else {
						alFehler.add("Zeile " + iZeile + " muss mindesten 2 Spalten haben.");
					}

				}

			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return alFehler;
	}

	@Override
	public ArrayList<String> importiereIstMaterial(Integer losIId, List<String[]> daten,
			boolean bResultierenderLagerstand, TheClientDto theClientDto) {

		ArrayList<String> alFehler = new ArrayList<String>();

		try {
			LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);
			LossollmaterialDto[] sollmatDtos = getFertigungFac().lossollmaterialFindByLosIIdOrderByISort(losIId);
			LoslagerentnahmeDto[] loslagerDtos = getFertigungFac().loslagerentnahmeFindByLosIId(losIId);

			MontageartDto montageartDto = getStuecklisteFac().montageartFindByMandantCNr(theClientDto)[0];

			if (daten != null && daten.size() > 1) {

				// 1.Zeile ist Ueberschrift
				for (int i = 1; i < daten.size(); i++) {
					String[] zeile = daten.get(i);

					if (zeile.length > 1) {
						String artikelnummer = zeile[0];
						String menge = zeile[1];
						String snrchnr = null;
						if (zeile.length > 2) {
							snrchnr = zeile[2];
						}

						ArtikelDto artikelDtoMateiral = getArtikelFac().artikelFindByCNrOhneExc(artikelnummer,
								theClientDto);

						if (artikelDtoMateiral != null) {

							if (artikelDtoMateiral.isLagerbewirtschaftet()) {

								try {
									BigDecimal bdMenge = new BigDecimal(menge);

									BigDecimal bdAbzubuchendeMenge = BigDecimal.ZERO;

									if (bResultierenderLagerstand == true) {
										BigDecimal bdLagerstand = getLagerFac().getLagerstand(
												artikelDtoMateiral.getIId(), loslagerDtos[0].getLagerIId(),
												theClientDto);

										BigDecimal diff = bdLagerstand.subtract(bdMenge);
										if (diff.doubleValue() > 0) {
											bdAbzubuchendeMenge = diff;
										}
									} else {
										bdAbzubuchendeMenge = bdMenge;
									}

									Integer lossollmaterialIId = null;
									for (int j = 0; j < sollmatDtos.length; j++) {
										LossollmaterialDto lossollmaterialDto = sollmatDtos[j];
										// ersten gefundenen Artikel verwenden
										if (lossollmaterialDto.getArtikelIId().equals(artikelDtoMateiral.getIId())) {
											lossollmaterialIId = lossollmaterialDto.getIId();

										}
									}

									// Wenn keinen Artikel gefunden, dann
									if (lossollmaterialIId == null) {
										LossollmaterialDto lossollmaterialDto = new LossollmaterialDto();

										lossollmaterialDto.setBNachtraeglich(Helper.boolean2Short(true));
										lossollmaterialDto.setArtikelIId(artikelDtoMateiral.getIId());
										lossollmaterialDto.setEinheitCNr(artikelDtoMateiral.getEinheitCNr());
										lossollmaterialDto.setLosIId(losIId);
										lossollmaterialDto.setMontageartIId(montageartDto.getIId());

										BigDecimal bdSollpreis = getLagerFac().getGemittelterGestehungspreisEinesLagers(
												artikelDtoMateiral.getIId(), loslagerDtos[0].getLagerIId(),
												theClientDto);
										lossollmaterialDto.setNSollpreis(bdSollpreis);
										lossollmaterialDto.setNMenge(new BigDecimal(0));

										lossollmaterialIId = getFertigungFac()
												.createLossollmaterial(lossollmaterialDto, theClientDto).getIId();
									}

									// wenn noch was abzubuchen ist (Menge > 0)
									if (bdAbzubuchendeMenge.doubleValue() > 0) {
										BigDecimal bdLagerstand = getLagerFac().getLagerstand(
												artikelDtoMateiral.getIId(), loslagerDtos[0].getLagerIId(),
												theClientDto);

										// wenn ein lagerstand da ist
										if (bdLagerstand.doubleValue() > 0) {
											BigDecimal bdMengeVonLager;
											if (bdLagerstand.doubleValue() >= bdAbzubuchendeMenge.doubleValue()) {
												// wenn mehr als ausreichend auf
												// lager
												bdMengeVonLager = bdAbzubuchendeMenge;
											} else {
												// dann nur den lagerstand
												// entnehmen
												bdMengeVonLager = bdLagerstand;
											}
											LosistmaterialDto istmat = new LosistmaterialDto();
											istmat.setLagerIId(loslagerDtos[0].getLagerIId());
											istmat.setLossollmaterialIId(lossollmaterialIId);
											istmat.setNMenge(bdMengeVonLager);
											istmat.setBAbgang(Helper.boolean2Short(true));

											// Wenn eine SnrChnr angegeben, dann
											// diese verwenden, ansonsten der
											// Reihe nach abbuchen.
											if (artikelDtoMateiral.istArtikelSnrOderchargentragend()) {

												SeriennrChargennrAufLagerDto[] snrchnrDtos = getLagerFac()
														.getAllSerienChargennrAufLagerInfoDtos(
																artikelDtoMateiral.getIId(),
																loslagerDtos[0].getLagerIId(), false, null,
																theClientDto);

												if (snrchnr != null && snrchnr.length() > 0) {

													BigDecimal bdAbzubuchen = bdMengeVonLager;
													BigDecimal bdAbGebucht = BigDecimal.ZERO;
													for (int k = 0; k < snrchnrDtos.length; k++) {

														if (snrchnrDtos[k].getCSeriennrChargennr().equals(snrchnr)) {
															if (snrchnrDtos[k].getNMenge()
																	.doubleValue() >= bdMengeVonLager.doubleValue()) {
																createLosistmaterial(istmat, snrchnr, false,
																		theClientDto);
																bdAbzubuchen = bdAbzubuchen
																		.subtract(istmat.getNMenge());

																bdAbGebucht = bdAbGebucht.add(istmat.getNMenge());

																break;
															} else {

																istmat.setNMenge(snrchnrDtos[k].getNMenge());
																bdAbzubuchen = bdAbzubuchen
																		.subtract(snrchnrDtos[k].getNMenge());
																bdAbGebucht = bdAbGebucht.add(istmat.getNMenge());
																createLosistmaterial(istmat, snrchnr, false,
																		theClientDto);
																break;
															}
														}

													}

													if (bdAbzubuchen.doubleValue() > 0) {
														alFehler.add("Vom Artikel '" + artikelnummer
																+ "' konnten von der Serien/Chargennummer '" + snrchnr
																+ "' anstatt "
																+ Helper.formatZahl(
																		bdMengeVonLager, 3, theClientDto.getLocUi())
																+ " nur "
																+ Helper.formatZahl(bdAbGebucht, 3,
																		theClientDto.getLocUi())
																+ " abgebucht werden. Zeile " + i);
													}

												} else {

													// Es wurde keine SNRCHNR
													// angegeben

													BigDecimal bdNochAbzubuchen = bdMengeVonLager;
													for (int k = 0; k < snrchnrDtos.length; k++) {
														if (bdNochAbzubuchen.doubleValue() > 0) {
															BigDecimal bdMengeSerienChargennummer;
															if (snrchnrDtos[k].getNMenge()
																	.doubleValue() >= bdNochAbzubuchen.doubleValue()) {
																// wenn mehr als
																// ausreichend
																// auf
																// lager
																bdMengeSerienChargennummer = bdNochAbzubuchen;
															} else {
																// dann nur den
																// lagerstand
																// entnehmen
																bdMengeSerienChargennummer = snrchnrDtos[k].getNMenge();
															}
															istmat.setNMenge(bdMengeSerienChargennummer);

															createLosistmaterial(istmat,
																	snrchnrDtos[k].getCSeriennrChargennr(), false,
																	theClientDto);

															// menge reduzieren
															bdNochAbzubuchen = bdNochAbzubuchen
																	.subtract(bdMengeSerienChargennummer);
														}

													}

												}
											} else {
												createLosistmaterial(istmat, null, false, theClientDto);
											}

										}
									}

								} catch (NumberFormatException e) {
									alFehler.add("Menge '" + menge
											+ "' konnte nicht in BigDecimal konvertiert werden. Zeile " + i);
								}
							} else {
								alFehler.add(
										"Artikel '" + artikelnummer + "' ist nicht lagerbewirtschaftet. Zeile " + i);
							}
						} else {
							alFehler.add("Artikel '" + artikelnummer + "' konnte nicht gefunden werden. Zeile " + i);
						}

					} else {
						alFehler.add("Zeile " + i + " muss mindesten 2 Spalten haben.");
					}

				}

			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return alFehler;
	}

	@Override
	public ArrayList<String> importiereSollMaterial(Integer losIId, List<String[]> daten, TheClientDto theClientDto) {

		ArrayList<String> alFehler = new ArrayList<String>();

		try {
			LoslagerentnahmeDto[] loslagerDtos = getFertigungFac().loslagerentnahmeFindByLosIId(losIId);

			MontageartDto montageartDto = getStuecklisteFac().montageartFindByMandantCNr(theClientDto)[0];

			if (daten != null && daten.size() > 1) {

				// 1.Zeile ist Ueberschrift
				for (int i = 1; i < daten.size(); i++) {
					String[] zeile = daten.get(i);

					if (zeile.length > 1) {
						String artikelnummer = zeile[0];
						String menge = zeile[1];

						ArtikelDto artikelDtoMateiral = getArtikelFac().artikelFindByCNrOhneExc(artikelnummer,
								theClientDto);

						if (artikelDtoMateiral != null) {

							try {
								BigDecimal bdMenge = new BigDecimal(menge);

								LossollmaterialDto lossollmaterialDto = new LossollmaterialDto();
								lossollmaterialDto.setBNachtraeglich(Helper.boolean2Short(true));
								lossollmaterialDto.setArtikelIId(artikelDtoMateiral.getIId());
								lossollmaterialDto.setEinheitCNr(artikelDtoMateiral.getEinheitCNr());
								lossollmaterialDto.setLosIId(losIId);
								lossollmaterialDto.setMontageartIId(montageartDto.getIId());

								BigDecimal bdSollpreis = getLagerFac().getGemittelterGestehungspreisEinesLagers(
										artikelDtoMateiral.getIId(), loslagerDtos[0].getLagerIId(), theClientDto);
								lossollmaterialDto.setNSollpreis(bdSollpreis);
								lossollmaterialDto.setNMenge(bdMenge);

								getFertigungFac().createLossollmaterial(lossollmaterialDto, theClientDto).getIId();

							} catch (NumberFormatException e) {
								alFehler.add("Menge '" + menge
										+ "' konnte nicht in BigDecimal konvertiert werden. Zeile " + i);
							}

						} else {
							alFehler.add("Artikel '" + artikelnummer + "' konnte nicht gefunden werden. Zeile " + i);
						}

					} else {
						alFehler.add("Zeile " + i + " muss mindesten 2 Spalten haben.");
					}

				}

			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return alFehler;
	}

	public ArrayList<LosAusAuftragDto> holeAlleMoeglichenUnterloseEinerStueckliste(Integer artikelIId,
			BigDecimal zielmenge, int stdVorlaufzeit, Timestamp tLetzterBeginn, ArrayList<LosAusAuftragDto> losDtos,
			AuftragDto auftragDto, BigDecimal bdLosgroesse, TheClientDto theClientDto) {
		try {
			StuecklisteDto stuecklisteDto = getStuecklisteFac().stuecklisteFindByMandantCNrArtikelIIdOhneExc(artikelIId,
					theClientDto);

			if (stuecklisteDto != null

					&& Helper.short2boolean(stuecklisteDto.getBFremdfertigung()) == false) {
				ArrayList<?> stuecklisteAufegloest = getStuecklisteFac().getStrukturDatenEinerStueckliste(
						stuecklisteDto.getIId(), theClientDto,
						StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_ARTIKELNR, 0, null, false, true,
						zielmenge, null, true);

				Timestamp tEnde = Helper.addiereTageZuTimestamp(tLetzterBeginn, -stdVorlaufzeit);

				if (stuecklisteDto.getNDefaultdurchlaufzeit() == null) {
					stuecklisteDto.setNDefaultdurchlaufzeit(new BigDecimal(0));
				}
				Timestamp tBeginn = Helper.addiereTageZuTimestamp(tEnde,
						-stuecklisteDto.getNDefaultdurchlaufzeit().intValue());

				LosAusAuftragDto laDto = new LosAusAuftragDto();

				LosDto losDto = new LosDto();

				losDto.setTProduktionsbeginn(new java.sql.Date(tBeginn.getTime()));
				losDto.setTProduktionsende(new java.sql.Date(tEnde.getTime()));
				losDto.setStuecklisteIId(stuecklisteDto.getIId());

				// PJ18596 hole alle bereits vorhandenen Unterlose zu der
				// Stueckliste und dem Auftrag
				laDto.setBereitsVorhandeneLose(
						losFindByAuftragIIdStuecklisteIId(auftragDto.getIId(), stuecklisteDto.getIId()));

				losDto.setLagerIIdZiel(stuecklisteDto.getLagerIIdZiellager());
				losDto.setFertigungsgruppeIId(stuecklisteDto.getFertigungsgruppeIId());
				losDto.setNLosgroesse(zielmenge.multiply(bdLosgroesse));
				losDto.setKostenstelleIId(auftragDto.getKostenstelleIId());
				losDto.setCProjekt(auftragDto.getCBezProjektbezeichnung());
				laDto.setLosDto(losDto);

				laDto.setFehlmengen(getFehlmengeFac().getAnzahlFehlmengeEinesArtikels(stuecklisteDto.getArtikelIId(),
						theClientDto));
				laDto.setReservierungen(
						getReservierungFac().getAnzahlReservierungen(stuecklisteDto.getArtikelIId(), theClientDto));

				laDto.setOffeneFertigungsmenge(
						getFertigungFac().getAnzahlInFertigung(stuecklisteDto.getArtikelIId(), theClientDto));

				// PJ19886
				if (!stuecklisteDto.getStuecklisteartCNr().equals(StuecklisteFac.STUECKLISTEART_HILFSSTUECKLISTE)) {

					BigDecimal lagerstand = new BigDecimal(0);
					LagerDto[] allelaegerDtos = getLagerFac().lagerFindByMandantCNr(theClientDto.getMandant());

					for (int i = 0; i < allelaegerDtos.length; i++) {
						if (Helper.short2boolean(allelaegerDtos[i].getBInternebestellung())) {
							lagerstand = lagerstand.add(getLagerFac().getLagerstand(stuecklisteDto.getArtikelIId(),
									allelaegerDtos[i].getIId(), theClientDto));
						}

					}

					laDto.setLagerstand(lagerstand);

					losDtos.add(laDto);
				}

				for (int j = 0; j < stuecklisteAufegloest.size(); j++) {
					StuecklisteMitStrukturDto strukt = (StuecklisteMitStrukturDto) stuecklisteAufegloest.get(j);

					if (strukt.getStuecklistepositionDto() != null
							&& strukt.getStuecklistepositionDto().getArtikelIId() != null) {

						losDtos = holeAlleMoeglichenUnterloseEinerStueckliste(
								strukt.getStuecklistepositionDto().getArtikelIId(),
								Helper.rundeKaufmaennisch(strukt.getStuecklistepositionDto().getNZielmenge(zielmenge),
										4),
								stdVorlaufzeit, tBeginn, losDtos, auftragDto, bdLosgroesse, theClientDto);

					}
				}

			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return losDtos;
	}

	public String erfuellungsgradBerechnen(Integer artikelIId, Integer losIId, TheClientDto theClientDto) {
		String s = "Soll: ";
		LossollarbeitsplanDto[] dtos = lossollarbeitsplanFindByLosIIdArtikelIIdTaetigkeit(losIId, artikelIId);
		if (dtos.length > 0) {
			boolean bZuvieleZeitbuchungen = getZeiterfassungFac()
					.sindZuvieleZeitdatenEinesBelegesVorhanden(LocaleFac.BELEGART_LOS, losIId, theClientDto);

			s += Helper.formatZahl(dtos[0].getNGesamtzeit(), 1, theClientDto.getLocUi()) + " Std., Ist: ";

			if (bZuvieleZeitbuchungen == false) {

				try {
					Double d = getZeiterfassungFac().getSummeZeitenEinesBeleges(LocaleFac.BELEGART_LOS, losIId,
							dtos[0].getIId(), null, null, null, theClientDto);
					s += Helper.formatZahl(d, 1, theClientDto.getLocUi()) + " Std.";
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
	public void teilerledigteLoseerledigen(Integer iArbeitstageSeitLetzterAblieferung, TheClientDto theClientDto) {

		Calendar cHeute = Calendar.getInstance();
		cHeute.setTimeInMillis(System.currentTimeMillis());

		if (iArbeitstageSeitLetzterAblieferung != null) {
			cHeute.add(Calendar.DATE, -iArbeitstageSeitLetzterAblieferung);
		}

		int iAzahlTageSchonVersetzt = 0;

		while (iAzahlTageSchonVersetzt < 999) {
			boolean bArbeitstag = true;

			if (cHeute.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
					|| cHeute.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				bArbeitstag = false;
			}

			if (bArbeitstag == false) {
				cHeute.add(Calendar.DAY_OF_MONTH, -1);
				iAzahlTageSchonVersetzt++;
			} else {
				break;
			}

		}

		String sQueryZeiten = "SELECT a.los_i_id FROM FLRLosablieferung a WHERE a.flrlos.status_c_nr='"
				+ FertigungFac.STATUS_TEILERLEDIGT + "' AND a.flrlos.mandant_c_nr='" + theClientDto.getMandant()
				+ "'  GROUP BY a.los_i_id HAVING max(a.t_aendern)<='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(cHeute.getTime().getTime()))
				+ "' AND sum(a.n_menge) >= max(a.flrlos.n_losgroesse)";

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Query zeiten = session.createQuery(sQueryZeiten);

		List<?> resultList = zeiten.list();

		Iterator it = resultList.iterator();

		while (it.hasNext()) {
			Integer losIId = (Integer) it.next();
			try {
				getFertigungFac().manuellErledigen(losIId, true, theClientDto);
			} catch (Throwable e) {
				// Fehler z.b. in Zeitdaten, Los auslassen
			}
		}

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public int loseAusAuftragAnlegen(ArrayList<LosAusAuftragDto> losAusAuftragDto, Integer auftragIId,
			TheClientDto theClientDto) {

		int iAnzahlAngelegterLose = 0;
		try {
			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(auftragIId);
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);

			// int iUnterlose = 999;

			for (int i = 0; i < losAusAuftragDto.size(); i++) {

				if (losAusAuftragDto.get(i).getLosDto().getNLosgroesse().doubleValue() > 0) {
					LosDto losDto = losAusAuftragDto.get(i).getLosDto();
					losDto.setAuftragIId(auftragIId);

					losDto.setPartnerIIdFertigungsort(mandantDto.getPartnerIId());
					losDto.setMandantCNr(theClientDto.getMandant());

					losDto.setKostenstelleIId(auftragDto.getKostIId());
					if (losAusAuftragDto.get(i).getAuftragpositionDto() != null) {
						losDto.setAuftragpositionIId(losAusAuftragDto.get(i).getAuftragpositionDto().getIId());
					}
					/*
					 * if (losDto.getAuftragpositionIId() == null) {
					 * losDto.setISortFuerUnterlos(iUnterlose); iUnterlose = iUnterlose - 1; }
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
	public ArrayList<LosAusAuftragDto> vorschlagMitUnterlosenAusAuftragReihenUndBeginnEndeBerechnen(
			ArrayList<LosAusAuftragDto> losDtos, TheClientDto theClientDto) {

		if (losDtos.size() > 0) {
			TreeMap<String, Boolean> tmVorhandenenNummern = new TreeMap<String, Boolean>();
			int n = 1;
			while (n < 90) {

				String praefix = new java.text.DecimalFormat("00").format(n);

				tmVorhandenenNummern.put(praefix + "0", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "1", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "2", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "3", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "4", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "5", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "6", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "7", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "8", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "9", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "A", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "B", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "C", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "D", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "E", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "F", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "G", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "H", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "I", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "J", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "K", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "L", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "M", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "N", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "O", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "P", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "Q", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "R", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "S", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "T", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "U", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "V", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "W", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "X", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "Y", Boolean.FALSE);
				tmVorhandenenNummern.put(praefix + "Z", Boolean.FALSE);

				n++;
			}

			// nun 900-999

			for (int i = 900; i <= 999; i++) {
				tmVorhandenenNummern.put(i + "", Boolean.FALSE);
			}

			for (int i = 0; i < losDtos.size(); i++) {

				String reihenfolge = null;

				if (losDtos.get(i).getLosDto().getStuecklisteIId() != null) {
					StuecklisteDto stklDto = getStuecklisteFac()
							.stuecklisteFindByPrimaryKey(losDtos.get(i).getLosDto().getStuecklisteIId(), theClientDto);

					if (stklDto.getIReihenfolge() != null) {
						reihenfolge = new java.text.DecimalFormat("00").format(stklDto.getIReihenfolge()) + "0";
					}

					StuecklistearbeitsplanDto[] stkPos = getStuecklisteFac().stuecklistearbeitsplanFindByStuecklisteIId(
							losDtos.get(i).getLosDto().getStuecklisteIId(), theClientDto);

					ArrayList<StuecklistearbeitsplanDto> alStuecklistePositionen = new ArrayList<StuecklistearbeitsplanDto>();
					for (int m = 0; m < stkPos.length; m++) {
						alStuecklistePositionen.add(stkPos[m]);
					}
					losDtos.get(i).setAlStuecklistearbeitsplan(alStuecklistePositionen);
				}

				if (reihenfolge == null) {
					Iterator<String> it = tmVorhandenenNummern.descendingKeySet().iterator();
					while (it.hasNext()) {
						String nummer = it.next();
						Boolean bVerbraucht = (Boolean) tmVorhandenenNummern.get(nummer);

						if (bVerbraucht == false) {

							tmVorhandenenNummern.put(nummer, Boolean.TRUE);
							losDtos.get(i).getLosDto().setLosUnternummerReihenfolgenplanung(nummer);
							break;
						}
					}
				} else {

					boolean bVerbraucht = true;

					while (bVerbraucht == true) {
						try {
							bVerbraucht = (Boolean) tmVorhandenenNummern.get(reihenfolge);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							int t = 0;
						}

						if (bVerbraucht == false) {
							tmVorhandenenNummern.put(reihenfolge, Boolean.TRUE);
							losDtos.get(i).getLosDto().setLosUnternummerReihenfolgenplanung(reihenfolge);
							break;
						} else {
							reihenfolge = (String) tmVorhandenenNummern.higherKey(reihenfolge);
						}

					}

				}

			}

			// Absteigend sortieren

			for (int i = losDtos.size() - 1; i > 0; --i) {
				for (int j = 0; j < i; ++j) {
					LosAusAuftragDto o = losDtos.get(j);
					LosAusAuftragDto o1 = losDtos.get(j + 1);

					String sort = o.getLosDto().getLosUnternummerReihenfolgenplanung();
					String sort1 = o1.getLosDto().getLosUnternummerReihenfolgenplanung();
					if (sort.compareTo(sort1) < 0) {
						losDtos.set(j, o1);
						losDtos.set(j + 1, o);
					}
				}
			}

			// Nun den Beginntermin berechnen
			Integer iAgBeginnAutomatischErmittelnInStd = 0;
			try {
				ParametermandantDto parameterM = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_AUTOMATISCHE_ERMITTLUNG_AG_BEGINN);
				iAgBeginnAutomatischErmittelnInStd = ((Integer) parameterM.getCWertAsObject());

			} catch (RemoteException ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			}

			if (iAgBeginnAutomatischErmittelnInStd != null && iAgBeginnAutomatischErmittelnInStd.intValue() > 0) {

				// Firmenzeitmodell ermitteln

				Integer zeitmodellIId = null;

				Query queryFZ = em.createNamedQuery("ZeitmodellfindByBFirmenzeitmodellMandantCNr");
				queryFZ.setParameter(1, new Short((short) 1));
				queryFZ.setParameter(2, theClientDto.getMandant());
				// @todo getSingleResult oder getResultList ?
				Collection c = queryFZ.getResultList();
				if (c.size() > 0) {

					zeitmodellIId = ((Zeitmodell) c.iterator().next()).getIId();

				} else {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FIRMENZEITMODELL_NICHT_VORHANDEN,
							new Exception("FEHLER_FIRMENZEITMODELL_NICHT_VORHANDEN"));
				}

				Calendar cTempBeginn = Calendar.getInstance();
				cTempBeginn.setTimeInMillis(Helper.cutDate(losDtos.get(0).getLosDto().getTProduktionsende()).getTime());

				int iAnzahlSlotsBenoetigtGesamt = 0;
				for (int i = 0; i < losDtos.size(); i++) {

					for (int j = losDtos.get(i).getAlStuecklistearbeitsplan().size(); j > 0; j--) {

						StuecklistearbeitsplanDto stklAPDto = losDtos.get(i).getAlStuecklistearbeitsplan().get(j - 1);

						BigDecimal bdGesamtzeit = Helper.berechneGesamtzeitInStunden(stklAPDto.getLRuestzeit(),
								stklAPDto.getLStueckzeitDurchMitarbeitergleichzeitig(),
								losDtos.get(i).getLosDto().getNLosgroesse(), null, stklAPDto.getIAufspannung());

						int iAnzahlSlotsAufDerPosition = (int) Math
								.ceil(bdGesamtzeit.doubleValue() / iAgBeginnAutomatischErmittelnInStd);

						iAnzahlSlotsBenoetigtGesamt += iAnzahlSlotsAufDerPosition;

					}
				}
				ArrayList<java.util.Date> alSlots = new ArrayList<java.util.Date>();

				int iZahler = 0;
				while (alSlots.size() < iAnzahlSlotsBenoetigtGesamt) {
					iZahler++;

					cTempBeginn.add(Calendar.DATE, -1);

					ZeitmodelltagDto zmtagDto = getZeiterfassungFac().getZeitmodelltagFirmenzeitmodellZuDatum(
							zeitmodellIId, new Timestamp(cTempBeginn.getTimeInMillis()), theClientDto);

					if (zmtagDto != null && zmtagDto.getUSollzeit() != null) {

						double dSollzeit = Helper.time2Double(zmtagDto.getUSollzeit());

						int iAnzahlHabenPlatz = (int) Math.floor(dSollzeit / iAgBeginnAutomatischErmittelnInStd);

						for (int i = 0; i < iAnzahlHabenPlatz; i++) {
							alSlots.add(cTempBeginn.getTime());
						}

					}

					if (iZahler > 1000) {
						// Fehler kann auftreten, wenn unzureichend Sollzeit
						// definiert ist, z.b. Wenn die Slots auf 4 Stunden
						// eingestellt sind, es jedoch nur Sollzeiten unter
						// 4 Stunden gibt
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_FIRMENZEITMODELL_NICHT_AUSREICHEND_SOLLZEIT_DEFINIERT,
								new Exception("FEHLER_FIRMENZEITMODELL_NICHT_AUSREICHEND_SOLLZEIT_DEFINIERT"));

					}

				}

				java.sql.Timestamp tBeginntermin = Helper.cutTimestamp(new Timestamp(cTempBeginn.getTimeInMillis()));

				for (int m = 0; m < losDtos.size(); m++) {
					if (alSlots.size() > 0) {
						losDtos.get(m).getLosDto().setTProduktionsende(new java.sql.Date(alSlots.get(0).getTime()));

						for (int i = losDtos.get(m).getAlStuecklistearbeitsplan().size(); i > 0; i--) {

							StuecklistearbeitsplanDto stklAPDto = losDtos.get(m).getAlStuecklistearbeitsplan()
									.get(i - 1);

							BigDecimal bdGesamtzeit = Helper.berechneGesamtzeitInStunden(stklAPDto.getLRuestzeit(),
									stklAPDto.getLStueckzeitDurchMitarbeitergleichzeitig(),
									losDtos.get(m).getLosDto().getNLosgroesse(), null, stklAPDto.getIAufspannung());

							int iAnzahlSlotsAufDerPosition = (int) Math
									.ceil(bdGesamtzeit.doubleValue() / iAgBeginnAutomatischErmittelnInStd);

							for (int j = 0; j < iAnzahlSlotsAufDerPosition; j++) {

								if (alSlots.size() == 1) {
									losDtos.get(m).getLosDto()
											.setTProduktionsbeginn(new java.sql.Date(alSlots.get(0).getTime()));
								}

								alSlots.remove(0);

							}

						}
						if (alSlots.size() > 0) {
							losDtos.get(m).getLosDto()
									.setTProduktionsbeginn(new java.sql.Date(alSlots.get(0).getTime()));
						}
					} else {
						if (m > 0) {
							// Beginn und Ende des vorgangers

							losDtos.get(m).getLosDto()
									.setTProduktionsbeginn(losDtos.get(m - 1).getLosDto().getTProduktionsende());
							losDtos.get(m).getLosDto()
									.setTProduktionsende(losDtos.get(m - 1).getLosDto().getTProduktionsende());

						}
					}
				}

			}
			// Aufsteigend Sortieren

			for (int i = losDtos.size() - 1; i > 0; --i) {
				for (int j = 0; j < i; ++j) {
					LosAusAuftragDto o = losDtos.get(j);
					LosAusAuftragDto o1 = losDtos.get(j + 1);

					String sort = o.getLosDto().getLosUnternummerReihenfolgenplanung();
					String sort1 = o1.getLosDto().getLosUnternummerReihenfolgenplanung();
					if (sort.compareTo(sort1) > 0) {
						losDtos.set(j, o1);
						losDtos.set(j + 1, o);
					}
				}
			}
		}

		return losDtos;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public ArrayList<LosAusAuftragDto> vorschlagMitUnterlosenAusAuftrag(Integer auftragIId, TheClientDto theClientDto) {

		ArrayList<LosAusAuftragDto> losDtos = new ArrayList<LosAusAuftragDto>();

		try {

			ParametermandantDto parameterIndirekterPosBezug = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_INDIREKTER_AUFTRAGSPOSITIONSBEZUG);
			boolean bIndirekterABPosBezug = (Boolean) parameterIndirekterPosBezug.getCWertAsObject();

			ParametermandantDto parameterVorlaufzeit = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_DEFAULT_VORLAUFZEIT_INTERNEBESTELLUNG);
			int iVorlaufzeit = (Integer) parameterVorlaufzeit.getCWertAsObject();

			Integer stdFertigungsgruppeIId = null;
			FertigungsgruppeDto[] fertigungsgruppeDtos = getStuecklisteFac()
					.fertigungsgruppeFindByMandantCNr(theClientDto.getMandant(), theClientDto);

			if (fertigungsgruppeDtos.length > 0) {
				stdFertigungsgruppeIId = fertigungsgruppeDtos[0].getIId();
			}

			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(auftragIId);
			int iLieferdauerKunde = getKundeFac()
					.kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(), theClientDto).getILieferdauer();

			if (auftragDto.getStatusCNr().equals(LocaleFac.STATUS_ANGELEGT)
					|| auftragDto.getStatusCNr().equals(LocaleFac.STATUS_OFFEN)
					|| auftragDto.getStatusCNr().equals(LocaleFac.STATUS_TEILERLEDIGT)) {
				AuftragpositionDto[] dtos = getAuftragpositionFac().auftragpositionFindByAuftrag(auftragIId);
				for (int i = 0; i < dtos.length; i++) {

					ArrayList<LosAusAuftragDto> losDtosPosition = new ArrayList<LosAusAuftragDto>();

					LosDto[] losDtosBereitsvorhanden = losFindByAuftragpositionIId(dtos[i].getIId());

					if (dtos[i].getPositionsartCNr().equals(AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE)) {

						Timestamp tAuftragsliefertermin = dtos[i].getTUebersteuerbarerLiefertermin();
						if (tAuftragsliefertermin == null) {
							tAuftragsliefertermin = auftragDto.getDLiefertermin();
						}

						Timestamp tEnde = Helper.addiereTageZuTimestamp(tAuftragsliefertermin, -iLieferdauerKunde);

						Timestamp tBeginn = Helper.addiereTageZuTimestamp(tEnde, -iVorlaufzeit);

						LosAusAuftragDto laDto = new LosAusAuftragDto();

						laDto.setBereitsVorhandeneLose(losDtosBereitsvorhanden);

						if (tBeginn.before(Helper.cutTimestamp(new Timestamp(System.currentTimeMillis())))) {
							laDto.setBDatumVerschoben(true);
							tBeginn = Helper.cutTimestamp(new Timestamp(System.currentTimeMillis()));
							tEnde = Helper.addiereTageZuTimestamp(tBeginn, iVorlaufzeit);
						}

						LosDto losDto = new LosDto();

						losDto.setAuftragIId(auftragIId);
						losDto.setAuftragpositionIId(dtos[i].getIId());

						losDto.setTProduktionsbeginn(new java.sql.Date(tBeginn.getTime()));
						losDto.setTProduktionsende(new java.sql.Date(tEnde.getTime()));
						losDto.setLagerIIdZiel(getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId());
						losDto.setFertigungsgruppeIId(stdFertigungsgruppeIId);
						losDto.setNLosgroesse(dtos[i].getNMenge());
						losDto.setKostenstelleIId(auftragDto.getKostenstelleIId());
						losDto.setCProjekt(dtos[i].getCBez());

						laDto.setLosDto(losDto);

						laDto.setAuftragpositionDto(dtos[i]);

						laDto.setAuftragspositionsnummer(getAuftragpositionFac().getPositionNummer(dtos[i].getIId()));

						losDtos.add(laDto);

					} else {

						// SP1107 Setartikelkopf ignorieren
						Session session = null;
						SessionFactory factory = FLRSessionFactory.getFactory();
						session = factory.openSession();
						Criteria crit = session.createCriteria(FLRAuftragposition.class);
						crit.add(Restrictions.eq("position_i_id_artikelset", dtos[i].getIId()));

						int iZeilen = crit.list().size();
						session.close();
						if (iZeilen > 0) {
							continue;
						}

						StuecklisteDto stuecklisteDto = getStuecklisteFac()
								.stuecklisteFindByMandantCNrArtikelIIdOhneExc(dtos[i].getArtikelIId(), theClientDto);
						if (stuecklisteDto != null) {
							if (dtos[i].getNMenge() != null && dtos[i].getNMenge().doubleValue() > 0
									&& Helper.short2boolean(stuecklisteDto.getBFremdfertigung()) == false) {
								// Wg. PJ20772 nur die Lose rausfiltern, die auch dieselbe Stueckliste haben
								if (bIndirekterABPosBezug) {
									ArrayList alLoseVorhanden = new ArrayList();
									for (int c = 0; c < losDtosBereitsvorhanden.length; c++) {
										LosDto losDtoVorhanden = losDtosBereitsvorhanden[c];
										if (losDtoVorhanden.getStuecklisteIId() != null && losDtoVorhanden
												.getStuecklisteIId().equals(stuecklisteDto.getIId())) {
											alLoseVorhanden.add(losDtoVorhanden);
										}
									}
									losDtosBereitsvorhanden = (LosDto[]) alLoseVorhanden
											.toArray(new LosDto[alLoseVorhanden.size()]);
								}

								Timestamp tAuftragsliefertermin = dtos[i].getTUebersteuerbarerLiefertermin();
								if (tAuftragsliefertermin == null) {
									tAuftragsliefertermin = auftragDto.getDLiefertermin();
								}

								Timestamp tEnde = Helper.addiereTageZuTimestamp(tAuftragsliefertermin,
										-iLieferdauerKunde);

								Timestamp tBeginn = tEnde;

								if (stuecklisteDto.getNDefaultdurchlaufzeit() != null) {
									tBeginn = Helper.addiereTageZuTimestamp(tEnde,
											-stuecklisteDto.getNDefaultdurchlaufzeit().intValue());
								}
								LosAusAuftragDto laDto = new LosAusAuftragDto();

								laDto.setBereitsVorhandeneLose(losDtosBereitsvorhanden);

								LosDto losDto = new LosDto();

								losDto.setAuftragIId(auftragIId);
								losDto.setAuftragpositionIId(dtos[i].getIId());

								losDto.setTProduktionsbeginn(new java.sql.Date(tBeginn.getTime()));
								losDto.setTProduktionsende(new java.sql.Date(tEnde.getTime()));
								losDto.setStuecklisteIId(stuecklisteDto.getIId());
								losDto.setLagerIIdZiel(stuecklisteDto.getLagerIIdZiellager());
								losDto.setCProjekt(auftragDto.getCBezProjektbezeichnung());
								losDto.setFertigungsgruppeIId(stuecklisteDto.getFertigungsgruppeIId());

								// PJ18451
								ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(dtos[i].getArtikelIId(),
										theClientDto);
								if (aDto.getFUeberproduktion() != null) {
									dtos[i].setNMenge(dtos[i].getNMenge().add(Helper.getProzentWert(dtos[i].getNMenge(),
											new BigDecimal(aDto.getFUeberproduktion()), 4)));
								}

								// SP5806
								ArtikelsperrenDto[] artikelsperrenDtos = getArtikelFac()
										.artikelsperrenFindByArtikelIId(aDto.getIId());

								for (int j = 0; j < artikelsperrenDtos.length; j++) {

									SperrenDto sperrenDto = getArtikelFac()
											.sperrenFindByPrimaryKey(artikelsperrenDtos[j].getSperrenIId());

									if (Helper.short2boolean(sperrenDto.getBGesperrt())
											|| Helper.short2boolean(sperrenDto.getBGesperrtlos())) {
										laDto.setBGesperrt(true);
									}

								}

								// Kommentare
								ArrayList<KeyvalueDto> hinweise = getArtikelkommentarFac()
										.getArtikelhinweise(aDto.getIId(), LocaleFac.BELEGART_LOS, theClientDto);

								ArrayList<byte[]> bilder = getArtikelkommentarFac()
										.getArtikelhinweiseBild(aDto.getIId(), LocaleFac.BELEGART_LOS, theClientDto);

								if ((bilder != null && bilder.size() > 0) || hinweise.size() > 0) {
									laDto.setBKommentareVorhanden(true);
								}

								losDto.setNLosgroesse(dtos[i].getNMenge());

								// PJ20751
								if (Helper.short2boolean(stuecklisteDto.getBJahreslos())) {

									Timestamp[] tVonBis = getBuchenFac()
											.getDatumVonBisGeschaeftsjahr(getBuchenFac().findGeschaeftsjahrFuerDatum(
													new java.sql.Date(auftragDto.getTBelegdatum().getTime()),
													theClientDto.getMandant()), theClientDto);
									Session sessionSub = FLRSessionFactory.getFactory().openSession();
									String sQuerySub = "FROM FLRLosReport AS l WHERE l.stueckliste_i_id="
											+ stuecklisteDto.getIId() + " AND l.status_c_nr NOT IN('"
											+ FertigungFac.STATUS_STORNIERT + "','" + FertigungFac.STATUS_ERLEDIGT
											+ "') AND l.t_anlegen>='"
											+ Helper.formatDateWithSlashes(new java.sql.Date(tVonBis[0].getTime()))
											+ "' AND l.t_anlegen<='"
											+ Helper.formatDateWithSlashes(new java.sql.Date(tVonBis[1].getTime()))
											+ "' ";

									org.hibernate.Query hquerySub = sessionSub.createQuery(sQuerySub);
									hquerySub.setMaxResults(1);
									List<?> resultListSub = hquerySub.list();
									Iterator<?> resultListIteratorSub = resultListSub.iterator();

									if (resultListIteratorSub.hasNext()) {
										losDto.setNLosgroesse(BigDecimal.ZERO);
									}
									sessionSub.close();

								}

								losDto.setKostenstelleIId(auftragDto.getKostenstelleIId());

								laDto.setLosDto(losDto);

								laDto.setAuftragpositionDto(dtos[i]);

								laDto.setAuftragspositionsnummer(
										getAuftragpositionFac().getPositionNummer(dtos[i].getIId()));

								laDto.setFehlmengen(getFehlmengeFac()
										.getAnzahlFehlmengeEinesArtikels(stuecklisteDto.getArtikelIId(), theClientDto));

								ArtikelreservierungDto eigeneReservierungDto = getReservierungFac()
										.artikelreservierungFindByBelegartCNrBelegartPositionIIdOhneExc(
												LocaleFac.BELEGART_AUFTRAG, dtos[i].getIId());

								BigDecimal reservierungen = getReservierungFac()
										.getAnzahlReservierungen(stuecklisteDto.getArtikelIId(), theClientDto);

								if (eigeneReservierungDto != null && eigeneReservierungDto.getNMenge() != null) {
									if (reservierungen.subtract(eigeneReservierungDto.getNMenge()).doubleValue() < 0) {
										reservierungen = new BigDecimal(0);
									} else {
										reservierungen = reservierungen.subtract(eigeneReservierungDto.getNMenge());
									}
								}

								laDto.setReservierungen(reservierungen);

								BigDecimal lagerstand = new BigDecimal(0);
								LagerDto[] allelaegerDtos = getLagerFac()
										.lagerFindByMandantCNr(theClientDto.getMandant());

								for (int j = 0; j < allelaegerDtos.length; j++) {
									if (Helper.short2boolean(allelaegerDtos[j].getBInternebestellung())) {
										lagerstand = lagerstand
												.add(getLagerFac().getLagerstand(stuecklisteDto.getArtikelIId(),
														allelaegerDtos[j].getIId(), theClientDto));
									}

								}

								laDto.setLagerstand(lagerstand);
								laDto.setOffeneFertigungsmenge(getFertigungFac()
										.getAnzahlInFertigung(stuecklisteDto.getArtikelIId(), theClientDto));

								losDtosPosition.add(laDto);

								ArrayList<?> stuecklisteAufegloest = getStuecklisteFac()
										.getStrukturDatenEinerStueckliste(stuecklisteDto.getIId(), theClientDto,
												StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_ARTIKELNR, 0,
												null, false, true, dtos[i].getNMenge(), null, true);

								for (int j = 0; j < stuecklisteAufegloest.size(); j++) {
									StuecklisteMitStrukturDto strukt = (StuecklisteMitStrukturDto) stuecklisteAufegloest
											.get(j);

									if (strukt.getStuecklistepositionDto() != null
											&& strukt.getStuecklistepositionDto().getArtikelIId() != null) {
										losDtosPosition = holeAlleMoeglichenUnterloseEinerStueckliste(
												strukt.getStuecklistepositionDto().getArtikelIId(),
												strukt.getStuecklistepositionDto().getNZielmenge(), iVorlaufzeit,
												tBeginn, losDtosPosition, auftragDto, dtos[i].getNMenge(),
												theClientDto);

									}

								}

								// Wenn Termin vor Heute, dann aauf nach
								// Heute
								// verschieben
								java.sql.Date fruehesterProduktionsbeginnVorHeute = Helper
										.cutDate(new java.sql.Date(System.currentTimeMillis()));

								for (int k = 0; k < losDtosPosition.size(); k++) {

									if (losDtosPosition.get(k).getLosDto().getTProduktionsbeginn()
											.before(fruehesterProduktionsbeginnVorHeute)) {
										fruehesterProduktionsbeginnVorHeute = losDtosPosition.get(k).getLosDto()
												.getTProduktionsbeginn();
									}

								}

								int iDiffTage = Helper.getDifferenzInTagen(fruehesterProduktionsbeginnVorHeute,
										new java.sql.Date(System.currentTimeMillis()));
								if (iDiffTage > 0) {

									for (int k = 0; k < losDtosPosition.size(); k++) {

										losDtosPosition.get(k).getLosDto()
												.setTProduktionsbeginn(Helper.addiereTageZuDatum(
														losDtosPosition.get(k).getLosDto().getTProduktionsbeginn(),
														iDiffTage));
										losDtosPosition.get(k).getLosDto()
												.setTProduktionsende(Helper.addiereTageZuDatum(
														losDtosPosition.get(k).getLosDto().getTProduktionsende(),
														iDiffTage));
										losDtosPosition.get(k).setBDatumVerschoben(true);

									}
								}

								for (int k = 0; k < losDtosPosition.size(); k++) {

									if (dtos[i].getAuftragpositionstatusCNr()
											.equals(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT)) {
										losDtosPosition.get(k).setAuftragspositionIstErledigt(true);
									}

									// PJ20772
									if (bIndirekterABPosBezug == true) {
										losDtosPosition.get(k).setAuftragpositionDto(dtos[i]);

										Integer posnr = getAuftragpositionFac()
												.getPositionNummer(losDto.getAuftragpositionIId());
										String sNummer = new java.text.DecimalFormat("000").format(posnr);
										losDtosPosition.get(k).getLosDto().setCAbposnr(sNummer);
									}

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

		// SP6459 / PJ20715
		losDtos = verdichteUnterloseAufFruehestenTermin(losDtos, theClientDto);

		return losDtos;
	}

	private ArrayList<LosAusAuftragDto> verdichteUnterloseAufFruehestenTermin(ArrayList<LosAusAuftragDto> losDtos,
			TheClientDto theClientDto) {

		ArrayList<LosAusAuftragDto> listeNeu = new ArrayList<LosAusAuftragDto>();

		if (losDtos.size() > 0) {

			for (int i = 0; i < losDtos.size(); i++) {
				if (losDtos.get(i).getAuftragspositionsnummer() != null) {
					listeNeu.add(losDtos.get(i));
				} else {
					// Unterlose verdichten

					boolean bGefunden = false;
					for (int k = 0; k < listeNeu.size(); k++) {
						if (listeNeu.get(k).getAuftragspositionsnummer() == null
								&& listeNeu.get(k).getLosDto().getStuecklisteIId() != null) {
							if (listeNeu.get(k).getLosDto().getStuecklisteIId()
									.equals(losDtos.get(i).getLosDto().getStuecklisteIId())) {

								BigDecimal bdNeueLosgroesse = listeNeu.get(k).getLosDto().getNLosgroesse()
										.add(losDtos.get(i).getLosDto().getNLosgroesse());

								listeNeu.get(k).getLosDto().setNLosgroesse(bdNeueLosgroesse);
								// PJ20772
								listeNeu.get(k).getLosDto().setCAbposnr(null);
								listeNeu.get(k).setAuftragpositionDto(null);

								if (listeNeu.get(k).getLosDto().getTProduktionsbeginn()
										.after(losDtos.get(i).getLosDto().getTProduktionsbeginn())) {
									listeNeu.get(k).getLosDto()
											.setTProduktionsbeginn(losDtos.get(i).getLosDto().getTProduktionsbeginn());
								}

								bGefunden = true;
								break;

							}
						}

					}

					if (bGefunden == false) {
						listeNeu.add(losDtos.get(i));
					}

				}

			}

		}

		return listeNeu;
	}

	public Integer holeTageslos(Integer lagerIId, String mandantCNr, int iStandarddurchlaufzeit,
			boolean bSofortverbrauch, TheClientDto theClientDto) {

		Integer losIId = null;

		TageslosDto tageslosDto = null;
		try {
			tageslosDto = getKuecheFac().tageslosFindByLagerIIdBSofortverbrauch(lagerIId,
					Helper.boolean2Short(bSofortverbrauch));
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		if (tageslosDto != null) {
			// Nun das erste ausgegebene Los mit der angegebenen Kostenstelle
			// des heutigen Tages suchen
			Session session = FLRSessionFactory.getFactory().openSession();
			String sQuery3 = "FROM FLRLosReport AS l WHERE l.kostenstelle_i_id=" + tageslosDto.getKostenstelleIId()
					+ " AND l.stueckliste_i_id IS NULL AND l.status_c_nr IN('" + FertigungFac.STATUS_IN_PRODUKTION
					+ "','" + FertigungFac.STATUS_TEILERLEDIGT + "') AND l.t_produktionsbeginn='"
					+ Helper.formatDateWithSlashes(new java.sql.Date(System.currentTimeMillis())) + "' ";

			if (bSofortverbrauch) {
				sQuery3 += " AND l.flrfertigungsgruppe.c_bez='" + StuecklisteFac.FERTIGUNGSGRUPPE_SOFORTVERBRAUCH
						+ "' ";
			} else {
				sQuery3 += " AND l.flrfertigungsgruppe.c_bez NOT IN('" + StuecklisteFac.FERTIGUNGSGRUPPE_SOFORTVERBRAUCH
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
				Timestamp tHeute = Helper.cutTimestamp(new Timestamp(System.currentTimeMillis()));

				Calendar cTemp = Calendar.getInstance();
				cTemp.set(Calendar.DAY_OF_YEAR, cTemp.get(Calendar.DAY_OF_YEAR) + iStandarddurchlaufzeit);

				LosDto losDto = new LosDto();
				losDto.setTProduktionsbeginn(new java.sql.Date(tHeute.getTime()));
				losDto.setTProduktionsende(new java.sql.Date(cTemp.getTimeInMillis()));
				losDto.setKostenstelleIId(tageslosDto.getKostenstelleIId());
				losDto.setMandantCNr(mandantCNr);
				losDto.setNLosgroesse(new BigDecimal(1));
				try {
					FertigungsgruppeDto[] fgDto = getStuecklisteFac().fertigungsgruppeFindByMandantCNr(mandantCNr,
							theClientDto);

					if (fgDto != null && fgDto.length > 0) {
						losDto.setFertigungsgruppeIId(fgDto[0].getIId());
					}

					if (bSofortverbrauch) {
						Fertigungsgruppe fertigungsgruppe;
						try {
							Query query = em.createNamedQuery("FertigungsgruppefindByMandantCNrCBez");
							query.setParameter(1, mandantCNr);
							query.setParameter(2, StuecklisteFac.FERTIGUNGSGRUPPE_SOFORTVERBRAUCH);
							fertigungsgruppe = (Fertigungsgruppe) query.getSingleResult();

							losDto.setFertigungsgruppeIId(fertigungsgruppe.getIId());

						} catch (javax.persistence.NoResultException e) {
							FertigungsgruppeDto fertigungsgruppeDto = new FertigungsgruppeDto();

							fertigungsgruppeDto.setCBez(StuecklisteFac.FERTIGUNGSGRUPPE_SOFORTVERBRAUCH);
							fertigungsgruppeDto.setISort(getStuecklisteFac().getNextFertigungsgruppe(theClientDto));

							Integer i = getStuecklisteFac().createFertigungsgruppe(fertigungsgruppeDto, theClientDto);
							losDto.setFertigungsgruppeIId(i);
						}

					}

					MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(mandantCNr, theClientDto);
					losDto.setPartnerIIdFertigungsort(mandantDto.getPartnerIId());

					LagerDto lagerDtoAbbuchungslager = getLagerFac()
							.lagerFindByPrimaryKey(tageslosDto.getLagerIIdAbbuchung());

					String projekt = "Tageslos " + Helper.formatDatum(tHeute, theClientDto.getLocUi()) + " "
							+ lagerDtoAbbuchungslager.getCNr();

					losDto.setCProjekt(projekt);
					losDto.setLagerIIdZiel(getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId());

					losIId = getFertigungFac().createLos(losDto, theClientDto).getIId();

					getFertigungFac().gebeLosAus(losIId, true, false, theClientDto, null);

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}
			session.close();
		}
		return losIId;
	}

	public Integer proFirstSchachtelplanImportieren(ArrayList<String[]> alZeilen, String schachtelplannummer,
			SeriennrChargennrMitMengeDto snrchnrDto, TheClientDto theClientDto) {
		String startPartList = "StartPartList";
		String number = "Number";
		String totalArea = "TotalArea";
		String startWasteList = "StartWasteList";

		Iterator it = alZeilen.iterator();

		BigDecimal bdRest = BigDecimal.ZERO;

		boolean bWasteListeGefunden = false;

		Integer artikelIIdMaterial = null;

		while (it.hasNext()) {
			String[] zeile = (String[]) it.next();
			if (zeile != null && zeile.length > 0 && zeile[0].equals(startWasteList)) {
				bWasteListeGefunden = true;
			}

			if (zeile != null && zeile.length > 1 && zeile[0].equals(totalArea) && bWasteListeGefunden == true) {
				bdRest = new BigDecimal(zeile[1]);
				break;
			}
		}

		if (snrchnrDto != null && snrchnrDto.getNMenge().doubleValue() < bdRest.doubleValue()) {
			ArrayList al = new ArrayList();
			al.add(snrchnrDto.getCSeriennrChargennr());
			al.add(bdRest);
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PRO_FIRST_SCHACHTELPANIMPORT_RESTMENGE_GROESSER_LAGERMENGE,
					al, new Exception("FEHLER_PRO_FIRST_SCHACHTELPANIMPORT_RESTMENGE_GROESSER_LAGERMENGE"));
		}

		List<Integer> lLose = new ArrayList<Integer>();

		it = alZeilen.iterator();

		while (it.hasNext()) {
			String[] zeile = (String[]) it.next();

			if (zeile != null && zeile.length > 0 && zeile[0].equals(startPartList)) {
				zeile = (String[]) it.next();
				if (zeile != null && zeile.length > 0 && zeile[0].equals(number)) {

					do {

						zeile = (String[]) it.next();

						if (zeile.length > 10) {
							String artikelnummerProFirst = zeile[1];
							Integer menge = new Integer(zeile[6]);

							try {

								ParametermandantDto parameter = (ParametermandantDto) getParameterFac()
										.getMandantparameter(theClientDto.getMandant(),
												ParameterFac.KATEGORIE_STUECKLISTE,
												ParameterFac.PARAMETER_PRO_FIRST_DBURL);

								String dbUrl = (String) parameter.getCWertAsObject();

								parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
										theClientDto.getMandant(), ParameterFac.KATEGORIE_STUECKLISTE,
										ParameterFac.PARAMETER_PRO_FIRST_DBUSER);

								String dbUser = (String) parameter.getCWertAsObject();

								parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
										theClientDto.getMandant(), ParameterFac.KATEGORIE_STUECKLISTE,
										ParameterFac.PARAMETER_PRO_FIRST_DBPASSWORD);
								String dbPassword = (String) parameter.getCWertAsObject();
								java.sql.Connection sqlcon = null;
								Statement statement = null;
								ResultSet rs = null;
								Class.forName("net.sourceforge.jtds.jdbc.Driver");
								sqlcon = java.sql.DriverManager.getConnection(dbUrl, dbUser, dbPassword);

								statement = sqlcon.createStatement();
								String queryforFroFirst = "select PART.IDPART from PART where PART.NAME ='"
										+ artikelnummerProFirst + "'";
								statement.setMaxRows(1);
								statement.execute(queryforFroFirst);

								rs = statement.getResultSet();

								String originalnummer = null;
								if (rs.next()) {

									originalnummer = rs.getString("IDPART");
								} else {
									// FEHLER
									ArrayList al = new ArrayList();
									al.add(artikelnummerProFirst);
									throw new EJBExceptionLP(
											EJBExceptionLP.FEHLER_PRO_FIRST_SCHACHTELPANIMPORT_STKL_IN_PRO_FIRST_NICHT_VORHANDEN,
											al, new Exception(
													"FEHLER_PRO_FIRST_SCHACHTELPANIMPORT_STKL_IN_PRO_FIRST_NICHT_VORHANDEN"));
								}

								// Nun Stkl bei uns suchen
								Query query = em.createNamedQuery("StuecklistefindByCFremdsystemnrMandantCNr");
								query.setParameter(1, originalnummer);
								query.setParameter(2, theClientDto.getMandant());
								Collection c = query.getResultList();
								if (c.size() > 0) {

									Stueckliste stkl = (Stueckliste) c.iterator().next();
									ArtikelDto artikelDto = getArtikelFac()
											.artikelFindByPrimaryKeySmall(stkl.getArtikelIId(), theClientDto);
									// Nun aeltestes Los der Stueckliste anhand
									// Beginndatum holen, das Angelegt ist und
									// P1-Status hat

									Session session = FLRSessionFactory.getFactory().openSession();
									String sQuery3 = "FROM FLRLoszusatzstatus AS l WHERE l.flrlos.status_c_nr='"
											+ LocaleFac.STATUS_ANGELEGT + "' AND l.flrlos.stueckliste_i_id="
											+ stkl.getIId() + " AND l.flrzusatzstatus.c_bez='" + ZUSATZSTATUS_P1
											+ "' ORDER BY l.flrlos.t_produktionsbeginn ASC ";

									org.hibernate.Query hquery3 = session.createQuery(sQuery3);
									hquery3.setMaxResults(1);
									List<?> resultList3 = hquery3.list();
									Iterator<?> resultListIterator3 = resultList3.iterator();

									if (resultListIterator3.hasNext()) {

										FLRLoszusatzstatus zs = (FLRLoszusatzstatus) resultListIterator3.next();

										if (zs.getFlrlos().getN_losgroesse().doubleValue() < menge) {
											// FEHLER
											ArrayList al = new ArrayList();
											al.add(zs.getFlrlos().getC_nr());
											al.add(artikelDto.getCNr());
											throw new EJBExceptionLP(
													EJBExceptionLP.FEHLER_PRO_FIRST_SCHACHTELPANIMPORT_KEIN_LOS_MIT_AUSREICHENDER_MENGE_GEFUNDEN,
													al, new Exception(
															"FEHLER_PRO_FIRST_SCHACHTELPANIMPORT_KEIN_LOS_MIT_AUSREICHENDER_MENGE_GEFUNDEN"));
										}

										LosDto losDto = losFindByPrimaryKey(zs.getFlrlos().getI_id());

										LossollmaterialDto[] sollmatDtos = lossollmaterialFindByLosIId(losDto.getIId());

										if (sollmatDtos.length > 0) {

											if (snrchnrDto == null) {
												// Das erste gefundene Material
												// zurueckgeben, damit am Client
												// die
												// Chargennummer ausgewaehlt
												// werden
												// kann
												return sollmatDtos[0].getArtikelIId();
											} else {

												if (zs.getFlrlos().getN_losgroesse().doubleValue() > menge) {
													losTeilen(zs.getFlrlos().getI_id(), menge, theClientDto);
													losDto = losFindByPrimaryKey(zs.getFlrlos().getI_id());
													sollmatDtos = lossollmaterialFindByLosIId(losDto.getIId());

												}

												// Schachtelplannummer
												// hinterlgen

												losDto.setCSchachtelplan(schachtelplannummer);
												updateLos(losDto, theClientDto);

												artikelIIdMaterial = sollmatDtos[0].getArtikelIId();
												lLose.add(losDto.getIId());

											}

										} else {
											// Wenn kein Material im Los, dann
											// Fehler

											ArrayList al = new ArrayList();
											al.add(zs.getFlrlos().getC_nr());
											al.add(artikelDto.getCNr());
											throw new EJBExceptionLP(
													EJBExceptionLP.FEHLER_PRO_FIRST_SCHACHTELPANIMPORT_KEIN_MATERIAL_DEFINIERT,
													al, new Exception(
															"FEHLER_PRO_FIRST_SCHACHTELPANIMPORT_KEIN_MATERIAL_DEFINIERT"));
										}

									} else {
										// FEHLER
										ArrayList al = new ArrayList();
										al.add(artikelDto.getCNr());
										throw new EJBExceptionLP(
												EJBExceptionLP.FEHLER_PRO_FIRST_SCHACHTELPANIMPORT_KEIN_LOS_GEFUNDEN,
												al,
												new Exception("FEHLER_PRO_FIRST_SCHACHTELPANIMPORT_KEIN_LOS_GEFUNDEN"));
									}
									session.close();

								} else {
									// FEHLER
									ArrayList al = new ArrayList();
									al.add(originalnummer);

									throw new EJBExceptionLP(
											EJBExceptionLP.FEHLER_PRO_FIRST_SCHACHTELPANIMPORT_STKL_NICHT_GEFUNDEN, al,
											new Exception("FEHLER_PRO_FIRST_SCHACHTELPANIMPORT_STKL_NICHT_GEFUNDEN"));

								}

							}

							catch (RemoteException ex) {
								throwEJBExceptionLPRespectOld(ex);
							} catch (SQLException ex) {
								throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PRO_FIRST_IMPORT_DATENBANKVERBIDNUNG,
										ex);
							} catch (ClassNotFoundException ex) {
								throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PRO_FIRST_IMPORT_DATENBANKVERBIDNUNG,
										ex);
							}

						} else {
							break;
						}
					} while (it.hasNext());

					break;
				}

			}

		}

		// Nun alle Lose ausgeben und Material darauf verbuchen

		if (snrchnrDto != null && artikelIIdMaterial != null && lLose.size() > 0) {

			int iNachkommaMenge = 3;
			try {
				iNachkommaMenge = getMandantFac().getNachkommastellenMenge(theClientDto.getMandant());
			} catch (RemoteException e1) {
				throwEJBExceptionLPRespectOld(e1);
			}

			BigDecimal bdMaterialZuVerbrauchen = snrchnrDto.getNMenge().subtract(bdRest);

			BigDecimal bdGesamtMaterialLtSollmaterial = BigDecimal.ZERO;
			Iterator itLose = lLose.iterator();
			while (itLose.hasNext()) {

				Integer losIId = (Integer) itLose.next();

				LossollmaterialDto[] sollmatDtos = lossollmaterialFindByLosIId(losIId);

				if (sollmatDtos.length > 0) {
					bdGesamtMaterialLtSollmaterial = bdGesamtMaterialLtSollmaterial.add(sollmatDtos[0].getNMenge());
				}

			}

			BigDecimal bdFaktor = bdMaterialZuVerbrauchen.divide(bdGesamtMaterialLtSollmaterial, 6,
					BigDecimal.ROUND_HALF_EVEN);

			itLose = lLose.iterator();
			while (itLose.hasNext()) {

				Integer losIId = (Integer) itLose.next();

				// Das Soll der Materialposition aendern, da ansonsten
				// Fehlmengen entstehen wuerden
				LossollmaterialDto[] sollmatDtos = lossollmaterialFindByLosIId(losIId);

				if (sollmatDtos.length > 0) {

					BigDecimal bdSollNeu = Helper.rundeKaufmaennisch(sollmatDtos[0].getNMenge().multiply(bdFaktor),
							iNachkommaMenge);

					if (itLose.hasNext() == false) {
						bdSollNeu = bdMaterialZuVerbrauchen;
					}

					sollmatDtos[0].setNMenge(bdSollNeu);
					updateLossollmaterial(sollmatDtos[0], theClientDto);

					try {
						ArrayList<BucheSerienChnrAufLosDto> bucheSerienChnrAufLosDtos = new ArrayList<BucheSerienChnrAufLosDto>();

						BucheSerienChnrAufLosDto chnrDto = new BucheSerienChnrAufLosDto();
						chnrDto.setArtikelIId(artikelIIdMaterial);
						chnrDto.setCSeriennrChargennr(snrchnrDto.getCSeriennrChargennr());
						chnrDto.setNMenge(sollmatDtos[0].getNMenge());
						chnrDto.setLagerIId(getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId());
						chnrDto.setLossollmaterialIId(sollmatDtos[0].getIId());

						bucheSerienChnrAufLosDtos.add(chnrDto);

						getFertigungFac().gebeLosAus(losIId, false, false, theClientDto, bucheSerienChnrAufLosDtos);

						bdMaterialZuVerbrauchen = bdMaterialZuVerbrauchen.subtract(sollmatDtos[0].getNMenge());

					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}

			}
		}

		return null;
	}

	private void losTeilen(Integer losIId, Integer menge, TheClientDto theClientDto) {

		int iNachkommaMenge = 3;
		try {
			iNachkommaMenge = getMandantFac().getNachkommastellenMenge(theClientDto.getMandant());
		} catch (RemoteException e1) {
			throwEJBExceptionLPRespectOld(e1);
		}
		// Wenn das Los groesser ist,
		// dann teilen
		LosDto losDto = losFindByPrimaryKey(losIId);

		BigDecimal losgroesseOri = losDto.getNLosgroesse();

		BigDecimal diff = losDto.getNLosgroesse().subtract(new BigDecimal(menge));

		losDto.setIId(null);
		losDto.setCNr(null);
		losDto.setNLosgroesse(diff);
		LosDto losDtoNeu = createLos(losDto, theClientDto);

		// Vorhandenes Los Updaten
		losDto = losFindByPrimaryKey(losIId);
		losDto.setNLosgroesse(new BigDecimal(menge));
		updateLos(losDto, theClientDto);

		/*
		 * Double fFaktor = 0D;
		 * 
		 * // Material LossollmaterialDto[] sollmatDtos =
		 * lossollmaterialFindByLosIId(losDto .getIId());
		 * 
		 * Integer sollmaterialIIdNeu = null; for (int i = 0; i < sollmatDtos.length;
		 * i++) {
		 * 
		 * BigDecimal bdMengeVorhanden = sollmatDtos[i].getNMenge();
		 * 
		 * sollmatDtos[i].setNMenge(bdMengeVorhanden.divide(losgroesseOri,
		 * iNachkommaMenge, BigDecimal.ROUND_HALF_EVEN).multiply( new
		 * BigDecimal(menge))); updateLossollmaterial(sollmatDtos[i], theClientDto);
		 * 
		 * sollmatDtos[i].setIId(null); sollmatDtos[i].setLosIId(losDtoNeu.getIId());
		 * sollmatDtos[i] .setNMenge(bdMengeVorhanden.divide(losgroesseOri,
		 * iNachkommaMenge, BigDecimal.ROUND_HALF_EVEN) .multiply(diff));
		 * sollmaterialIIdNeu = createLossollmaterial(sollmatDtos[i],
		 * theClientDto).getIId(); }
		 * 
		 * // Arbeitsplan
		 * 
		 * LossollarbeitsplanDto[] sollapDtos = lossollarbeitsplanFindByLosIId(losDto
		 * .getIId());
		 * 
		 * for (int i = 0; i < sollapDtos.length; i++) { // Vorhandenen Updaten damit
		 * die Gesamtzeit stimmt updateLossollarbeitsplan(sollapDtos[i], theClientDto);
		 * 
		 * sollapDtos[i].setIId(null); sollapDtos[i].setLosIId(losDtoNeu.getIId());
		 * sollapDtos[i].setLossollmaterialIId(sollmaterialIIdNeu);
		 * createLossollarbeitsplan(sollapDtos[i], theClientDto); }
		 */

		try {
			// und Zusatzstatus kopieren

			LoszusatzstatusDto[] zsDtos = loszusatzstatusFindByLosIIdOhneExc(losDto.getIId());

			for (int i = 0; i < zsDtos.length; i++) {
				zsDtos[i].setIId(null);
				zsDtos[i].setLosIId(losDtoNeu.getIId());
				createLoszusatzstatus(zsDtos[i], theClientDto);
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

	}

	public ArrayList<String[]> getSelektierteAGsForProfirst(Object[] selectedIds, TheClientDto theClientDto) {

		Integer zusatzstatusIId = null;
		try {
			Query query = em.createNamedQuery("ZusatzstatusfindByMandantCNrCBez");
			query.setParameter(1, theClientDto.getMandant());
			query.setParameter(2, ZUSATZSTATUS_P1);
			// @todo getSingleResult oder getResultList ?
			Zusatzstatus zs = (Zusatzstatus) query.getSingleResult();

			zusatzstatusIId = zs.getIId();

		} catch (NoResultException ex) {
			ZusatzstatusDto zsDto = new ZusatzstatusDto();
			zsDto.setCBez(ZUSATZSTATUS_P1);
			zsDto.setMandantCNr(theClientDto.getMandant());
			zsDto.setISort(getNextZusatzstatus(theClientDto));

			zusatzstatusIId = createZusatzstatus(zsDto, theClientDto);
		}

		ArrayList<String[]> al = new ArrayList<String[]>();

		String in = "";

		for (int i = 0; i < selectedIds.length; i++) {
			in += selectedIds[i] + "";

			if (i != selectedIds.length - 1) {
				in += ",";
			}

		}

		// QUERY
		String sQuery = " SELECT ap FROM FLRLossollarbeitsplan ap WHERE ap.los_i_id in (" + in + ")";

		// REIHENFOLGE?

		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Query query = session.createQuery(sQuery);

		List<?> resultList = query.list();

		ArrayList<SeriennrChargennrMitMengeDto> list = new ArrayList<SeriennrChargennrMitMengeDto>();

		Iterator<?> iteratorLB = resultList.iterator();

		// Ueberschriften
		String[] zeileU = new String[6];
		zeileU[0] = "KundeKbez";
		zeileU[1] = "Losnummer";
		zeileU[2] = "StklArtikelnummerProFirst";
		zeileU[3] = "OffeneLosmenge";
		zeileU[4] = "MaterialProFirst";
		zeileU[5] = "MaterialHoehe";

		al.add(zeileU);

		//
		try {

			ParametermandantDto parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_STUECKLISTE,
					ParameterFac.PARAMETER_PRO_FIRST_DBURL);

			String dbUrl = (String) parameter.getCWertAsObject();

			parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_PRO_FIRST_DBUSER);

			String dbUser = (String) parameter.getCWertAsObject();

			parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_PRO_FIRST_DBPASSWORD);
			String dbPassword = (String) parameter.getCWertAsObject();
			java.sql.Connection sqlcon = null;
			Statement statement = null;
			ResultSet rs = null;
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			sqlcon = java.sql.DriverManager.getConnection(dbUrl, dbUser, dbPassword);

			statement = sqlcon.createStatement();

			// QUERY

			while (iteratorLB.hasNext()) {

				FLRLossollarbeitsplan ap = (FLRLossollarbeitsplan) iteratorLB.next();

				if (ap.getFlrlos().getFlrstueckliste() != null
						&& ap.getFlrlos().getFlrstueckliste().getC_fremdsystemnr() != null) {

					// Artiklnummer aus Pro-First holen
					String queryforFroFirst = "select name from PART where IDPART ="
							+ ap.getFlrlos().getFlrstueckliste().getC_fremdsystemnr();
					statement.setMaxRows(1);
					statement.execute(queryforFroFirst);

					rs = statement.getResultSet();

					String originalnummer = null;
					while (rs.next()) {

						originalnummer = rs.getString("name");
					}

					if (originalnummer != null) {

						String[] zeile = new String[6];
						// Kunde
						Integer partnerIId = null;
						if (ap.getFlrlos().getFlrkunde() != null) {
							partnerIId = ap.getFlrlos().getFlrkunde().getFlrpartner().getI_id();
						}
						if (partnerIId == null && ap.getFlrlos().getFlrauftrag() != null) {
							partnerIId = ap.getFlrlos().getFlrauftrag().getFlrkunde().getFlrpartner().getI_id();
						}

						if (partnerIId != null) {
							zeile[0] = getPartnerFac().partnerFindByPrimaryKey(partnerIId, theClientDto).getCKbez();
						}

						zeile[1] = ap.getFlrlos().getC_nr();
						zeile[2] = originalnummer;

						zeile[3] = Helper.formatZahl(
								ap.getFlrlos().getN_losgroesse().subtract(
										getFertigungFac().getErledigteMenge(ap.getFlrlos().getI_id(), theClientDto)),
								theClientDto.getLocUi());

						if (ap.getFlrsollmaterial() != null) {

							ArtikelDto aDtoMaterial = getArtikelFac().artikelFindByPrimaryKey(
									ap.getFlrsollmaterial().getFlrartikel().getI_id(), theClientDto);

							if (aDtoMaterial.getMaterialIId() != null) {

								MaterialDto mDto = getMaterialFac()
										.materialFindByPrimaryKey(aDtoMaterial.getMaterialIId(), theClientDto);
								zeile[4] = mDto.getCNr();
								zeile[5] = aDtoMaterial.getGeometrieDto().getFHoehe() + "";
							}

						}

						try {
							Query queryZS = em.createNamedQuery("LoszusatzstatusfindByLosIIdZusatzstatusIId");
							queryZS.setParameter(1, ap.getLos_i_id());
							queryZS.setParameter(2, zusatzstatusIId);
							// @todo getSingleResult oder getResultList ?
							Loszusatzstatus doppelt = (Loszusatzstatus) queryZS.getSingleResult();

							// Wenn es des Zusatzstatus schon gibt, dann
							// auslassen, d.h. es ist bereit exportiert worden
							continue;

						} catch (NoResultException ex) {
							// Zusatzstatus bei Los hinterlegen
							LoszusatzstatusDto loszusatzstatusDto = new LoszusatzstatusDto();
							loszusatzstatusDto.setZusatzstatusIId(zusatzstatusIId);
							loszusatzstatusDto.setLosIId(ap.getLos_i_id());
							createLoszusatzstatus(loszusatzstatusDto, theClientDto);
						}
						al.add(zeile);
					}
				}

			}

			ArrayList<KeyvalueDto> alDtos = new ArrayList<KeyvalueDto>();
			alDtos.add(new KeyvalueDto("ZeitpunktWEPExport", System.currentTimeMillis() + ""));

			getSystemServicesFac().replaceKeyvaluesEinerGruppe(
					SystemServicesFac.KEYVALUE_WEP_EXPORT_PRO_FIRST_LETZTER_ZEITPUNKT, alDtos);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} catch (SQLException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PRO_FIRST_IMPORT_DATENBANKVERBIDNUNG, ex);
		} catch (ClassNotFoundException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PRO_FIRST_IMPORT_DATENBANKVERBIDNUNG, ex);
		}
		return al;

	}

	public BigDecimal[] getSummeMengeSollmaterialUndDauerFuerZuProduzieren(ArrayList<Integer> losIIs) {

		BigDecimal[] bd = new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ZERO };

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT sum(sollmat.n_menge), sum(p.n_dauer_offen_personal)+sum(p.n_dauer_offen_maschine) FROM FLRProduzieren p LEFT OUTER JOIN p.flrlossollarbeitsplan AS ap LEFT OUTER JOIN ap.flrsollmaterial AS sollmat  WHERE p.los_i_id IN(";

		for (int i = 0; i < losIIs.size(); i++) {
			queryString += losIIs.get(i) + "";

			if (i != losIIs.size() - 1) {
				queryString += ",";
			}

		}

		queryString += ")";

		org.hibernate.Query query = session.createQuery(queryString);
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();
		if (resultListIterator.hasNext()) {

			Object[] o = (Object[]) resultListIterator.next();
			bd[0] = (BigDecimal) o[0];
			bd[1] = (BigDecimal) o[1];
		}

		if (bd[0] == null) {
			bd[0] = BigDecimal.ZERO;

		}
		if (bd[1] == null) {
			bd[1] = BigDecimal.ZERO;

		}

		return bd;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void alleLoseEinerStuecklisteNachkalkulieren(String artikelnummer, String sAbDatum,
			TheClientDto theClientDto) {
		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT l FROM FLRLos l WHERE l.status_c_nr ='" + LocaleFac.STATUS_ERLEDIGT
				+ "' AND l.flrstueckliste.flrartikel.c_nr='" + artikelnummer + "'";// AND l.t_erledigt>=
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

				System.out.println("Beginn der Nachkalkulation von Los " + los.getC_nr());
				myLogger.logKritisch("Beginn der Nachkalkulation von Los " + los.getC_nr());

				getFertigungFac().aktualisiereNachtraeglichPreiseAllerLosablieferungen(los.getI_id(), theClientDto,
						false);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			} catch (Throwable e2) {
				myLogger.logKritisch("Fehler bei der Nachkalkulation des Loses " + los.getC_nr()
						+ " Bitte Los manuell im Modul Fertigung nachkalkulieren, um den genauen Fehler zu erhalten");
			}

		}
	}

	public LosDto createLos(LosDto losDto, TheClientDto theClientDto) throws EJBExceptionLP {
		return createLos(losDto, false, theClientDto);
	}

	public LosDto createLos(LosDto losDto, boolean bErsatztypenAuslassen, TheClientDto theClientDto)
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
			Integer iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(losDto.getMandantCNr(), getDate());
			// Es gibt 2 Varianten der Losnummer
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_LOSNUMMER_AUFTRAGSBEZOGEN);
			// a) Auftragsbezogen
			int iLosnummerAuftragsbezogen = (Integer) parameter.getCWertAsObject();
			if (iLosnummerAuftragsbezogen >= 1 && losDto.getAuftragIId() != null) {

				PKGeneratorObj pkGen = new PKGeneratorObj();
				losDto.setIId(pkGen.getNextPrimaryKey(PKConst.PK_LOS));
				// Generierung der Losnummer im Format <AB-Nr>-<I_SORT der
				// AB-Position>
				String sBelegnummerPrefix = getAuftragFac().auftragFindByPrimaryKey(losDto.getAuftragIId()).getCNr();

				parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_STELLEN_BELEGNUMMER);
				int iStellenBelegnummer = new Integer(parameter.getCWert());

				boolean bReihenfolgenplanung = false;
				parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_REIHENFOLGENPLANUNG);
				bReihenfolgenplanung = (Boolean) parameter.getCWertAsObject();

				int iDiff = iStellenBelegnummer - 5;

				if (iDiff > 0) {

					if (iLosnummerAuftragsbezogen == 1) {

						// Laufende Nummer um 2 Stellen reduzieren
						int iNuller = sBelegnummerPrefix.indexOf("00");
						if (iNuller > 0) {
							sBelegnummerPrefix = sBelegnummerPrefix.substring(0, iNuller)
									+ sBelegnummerPrefix.substring(iNuller + 2);
						}
					}
					if (iLosnummerAuftragsbezogen == 2) {
						// Laufende Nummer um 3 Stellen reduzieren
						int iNuller = sBelegnummerPrefix.indexOf("000");
						if (iNuller > 0) {
							sBelegnummerPrefix = sBelegnummerPrefix.substring(0, iNuller)
									+ sBelegnummerPrefix.substring(iNuller + 3);
						}
					}
				}

				sBelegnummerPrefix = sBelegnummerPrefix + "-";
				Integer iSort = null;
				String belegnummerKomplett = sBelegnummerPrefix;

				// PJ18596 Nachsehen, obs schon ein Los fuer diese Position
				// gibt, wenn ja, dann muss eine neues Los mit einer
				// Unternummer <999 angelegt werden
				boolean bEsGibBereitsEinLos = false;
				Query queryAufpos = em.createNamedQuery("LosfindByAuftragpositionIId");
				queryAufpos.setParameter(1, losDto.getAuftragpositionIId());
				if (queryAufpos.getResultList().size() > 0) {
					bEsGibBereitsEinLos = true;
				}

				if (losDto.getAuftragpositionIId() != null && bEsGibBereitsEinLos == false
						&& bReihenfolgenplanung == false) {

					// SP5921
					Integer posnr = getAuftragpositionFac().getPositionNummer(losDto.getAuftragpositionIId());
					if (posnr != null) {
						iSort = posnr;
					} else {
						iSort = getAuftragpositionFac().auftragpositionFindByPrimaryKey(losDto.getAuftragpositionIId())
								.getISort();
					}
				} else {
					if (bReihenfolgenplanung) {

						belegnummerKomplett = belegnummerKomplett + losDto.getLosUnternummerReihenfolgenplanung();
						losDto.setCNr(belegnummerKomplett);

					} else {
						iSort = auftragsbezogeneBelegnummmerErzeugen(losDto, iLosnummerAuftragsbezogen,
								sBelegnummerPrefix);
					}
				}

				if (bReihenfolgenplanung == false) {
					if (iLosnummerAuftragsbezogen == 2 && losDto.getLosbereichIId() != null) {
						belegnummerKomplett = belegnummerKomplett + losDto.getLosbereichIId()
								+ new java.text.DecimalFormat("000").format(iSort);
						losDto.setCNr(belegnummerKomplett);

					} else {
						belegnummerKomplett = belegnummerKomplett + new java.text.DecimalFormat("000").format(iSort);
						losDto.setCNr(belegnummerKomplett);

					}
				}

				try {
					Query query = em.createNamedQuery("LosfindByCNrMandantCNr");
					query.setParameter(1, losDto.getCNr());
					query.setParameter(2, theClientDto.getMandant());
					Los doppelt = (Los) query.getSingleResult();
					if (doppelt != null) {

						ArrayList al = new ArrayList();
						al.add(doppelt.getCNr());
						al.add(doppelt.getStatusCNr().trim());

						if (losDto.getAuftragpositionIId() != null && bEsGibBereitsEinLos == false
								&& bReihenfolgenplanung == false) {
							// SP9068
							iSort = auftragsbezogeneBelegnummmerErzeugen(losDto, iLosnummerAuftragsbezogen,
									sBelegnummerPrefix);

							if (iLosnummerAuftragsbezogen == 2 && losDto.getLosbereichIId() != null) {
								belegnummerKomplett = sBelegnummerPrefix + losDto.getLosbereichIId()
										+ new java.text.DecimalFormat("000").format(iSort);
								losDto.setCNr(belegnummerKomplett);

							} else {
								belegnummerKomplett = sBelegnummerPrefix
										+ new java.text.DecimalFormat("000").format(iSort);
								losDto.setCNr(belegnummerKomplett);

							}

							losDto.setCNr(belegnummerKomplett);

							query = em.createNamedQuery("LosfindByCNrMandantCNr");
							query.setParameter(1, losDto.getCNr());
							query.setParameter(2, theClientDto.getMandant());
							doppelt = (Los) query.getSingleResult();
							if (doppelt != null) {

								al = new ArrayList();
								al.add(doppelt.getCNr());
								al.add(doppelt.getStatusCNr().trim());

								throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, al,
										new Exception("FERT_LOS.UK:" + doppelt.getCNr()));

							}

						} else {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, al,
									new Exception("FERT_LOS.UK:" + doppelt.getCNr()));
						}

					}
				} catch (NoResultException ex1) {

				}

			}

			// b) "normal"
			else {
				BelegnummerGeneratorObj bnGen = new BelegnummerGeneratorObj();
				LpBelegnummerFormat f = getBelegnummerGeneratorObj().getBelegnummernFormat(losDto.getMandantCNr());
				LpBelegnummer bnr = bnGen.getNextBelegNr(iGeschaeftsjahr, PKConst.PK_LOS, losDto.getMandantCNr(),
						theClientDto);
				losDto.setIId(bnr.getPrimaryKey());
				String belegNummer = f.format(bnr);
				losDto.setCNr(belegNummer);
			}
			losDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			losDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			// Auf angelegt setzen
			losDto.setStatusCNr(FertigungFac.STATUS_ANGELEGT);
			Los los = new Los(losDto.getIId(), losDto.getMandantCNr(), losDto.getCNr(), losDto.getKostenstelleIId(),
					losDto.getNLosgroesse(), losDto.getTProduktionsende(), losDto.getTProduktionsbeginn(),
					losDto.getLagerIIdZiel(), losDto.getStatusCNr(), losDto.getPersonalIIdAnlegen(),
					losDto.getPersonalIIdAendern(), losDto.getFertigungsgruppeIId());

			em.persist(los);
			em.flush();
			losDto.setTAendern(los.getTAendern());
			losDto.setTAnlegen(los.getTAnlegen());
			setLosFromLosDto(los, losDto);

			// PJ18575
			if (losDto.getStuecklisteIId() != null) {

				Stueckliste stueckliste = em.find(Stueckliste.class, losDto.getStuecklisteIId());

				if (stueckliste.getMandantCNr().equals(losDto.getMandantCNr())) {

					StklagerentnahmeDto[] stklagerentnahmeDtos = getStuecklisteFac()
							.stklagerentnahmeFindByStuecklisteIId(losDto.getStuecklisteIId());
					if (stklagerentnahmeDtos != null && stklagerentnahmeDtos.length > 0) {
						for (int i = 0; i < stklagerentnahmeDtos.length; i++) {
							LoslagerentnahmeDto loslager = new LoslagerentnahmeDto();
							loslager.setLosIId(losDto.getIId());
							loslager.setLagerIId(stklagerentnahmeDtos[i].getLagerIId());
							createLoslagerentnahme(loslager, theClientDto);
						}
					}
				}
			}

			// default materialentnahme vom Hauptlager des Mandanten
			LagerDto lagerDto = getLagerFac().getHauptlagerDesMandanten(theClientDto);

			LagerDto[] lagerDtos = getLagerFac().lagerFindByMandantCNrOrderByILoslagersort(theClientDto.getMandant());

			if (lagerDtos != null && lagerDtos.length > 0) {
				for (int i = 0; i < lagerDtos.length; i++) {
					try {
						Query query = em.createNamedQuery("LoslagerentnahmefindByLosIIdLagerIId");
						query.setParameter(1, losDto.getIId());
						query.setParameter(2, lagerDtos[i].getIId());
						Loslagerentnahme vorhanden = (Loslagerentnahme) query.getSingleResult();
					} catch (NoResultException ex1) {
						LoslagerentnahmeDto loslager = new LoslagerentnahmeDto();
						loslager.setLosIId(losDto.getIId());
						loslager.setLagerIId(lagerDtos[i].getIId());
						createLoslagerentnahme(loslager, theClientDto);
					}

				}
			} else {
				try {
					Query query = em.createNamedQuery("LoslagerentnahmefindByLosIIdLagerIId");
					query.setParameter(1, losDto.getIId());
					query.setParameter(2, lagerDto.getIId());
					query.getSingleResult();
					Loslagerentnahme vorhanden = (Loslagerentnahme) query.getSingleResult();
				} catch (NoResultException ex1) {
					LoslagerentnahmeDto loslager = new LoslagerentnahmeDto();
					loslager.setLosIId(losDto.getIId());
					loslager.setLagerIId(lagerDto.getIId());
					createLoslagerentnahme(loslager, theClientDto);
				}

			}

			if (losDto.getStuecklisteIId() != null) {
				// Material + Arbeitsplan eintragen
				aktualisiereSollMaterialAusStueckliste(losDto.getIId(), theClientDto, false, bErsatztypenAuslassen);
				aktualisiereSollArbeitsplanAusStueckliste(losDto.getIId(), theClientDto);
			} else {
				defaultArbeitszeitartikelErstellen(losDto, theClientDto);
			}

			HvDtoLogger<LosDto> hvLogger = new HvDtoLogger<LosDto>(em, losDto.getIId(), theClientDto);
			hvLogger.logInsert(losDto);

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_AUTOMATISCHE_ERMITTLUNG_LOS_ENDE);

			boolean bAutomatischeErmittlungLosEnde = (Boolean) parameter.getCWertAsObject();

			if (bAutomatischeErmittlungLosEnde) {
				berechneLosEndeAnhandMaschinenversatztage(losDto.getIId(), theClientDto);
			}

			return losDto;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	private Integer auftragsbezogeneBelegnummmerErzeugen(LosDto losDto, int iLosnummerAuftragsbezogen,
			String sBelegnummer) {
		Integer iSort;
		// PJ17595
		if (iLosnummerAuftragsbezogen == 2 && losDto.getLosbereichIId() != null) {
			String queryString = "SELECT l.c_nr FROM FLRLos l WHERE l.c_nr LIKE '" + sBelegnummer
					+ losDto.getLosbereichIId() + "%' ORDER BY l.c_nr ASC";

			Session session = FLRSessionFactory.getFactory().openSession();

			org.hibernate.Query query = session.createQuery(queryString);

			List<?> resultList = query.list();

			iSort = 0;

			Iterator<?> resultListIterator = resultList.iterator();
			while (resultListIterator.hasNext()) {
				String s = (String) resultListIterator.next();
				String sNummer = new java.text.DecimalFormat("000").format(iSort);
				if (s.endsWith(sNummer)) {
					iSort++;
				} else {
					break;
				}

			}

			session.close();

			if (iSort >= 1000) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_LOSNUMMER_NACH_BEREICH_UEBERLAUF,
						new Exception("FEHLER_FERTIGUNG_LOSNUMMER_NACH_BEREICH_UEBERLAUF"));

			}

		} else {

			// PJ 15698
			// /naechste Nummer vor 999 verwenden
			String queryString = "SELECT l.c_nr FROM FLRLos l WHERE l.c_nr LIKE '" + sBelegnummer
					+ "%' ORDER BY l.c_nr DESC";

			Session session = FLRSessionFactory.getFactory().openSession();

			org.hibernate.Query query = session.createQuery(queryString);

			List<?> resultList = query.list();

			iSort = 999;

			Iterator<?> resultListIterator = resultList.iterator();
			while (resultListIterator.hasNext()) {
				String s = (String) resultListIterator.next();
				String sNummer = new java.text.DecimalFormat("000").format(iSort);
				if (s.endsWith(sNummer)) {
					iSort--;
				} else {
					break;
				}

			}

			session.close();
		}
		return iSort;
	}

	public Integer defaultArbeitszeitartikelErstellen(LosDto losDto, TheClientDto theClientDto) {
		// 18. April 2008: lt. WH: Wenn eine Materialliste angelegt,
		// wird automatisch eine Arbeitsplanposition
		// mit der Ruestzeit 1 Sekunde angelegt:
		// Artikel=Default-AZ-Artikel
		try {
			LossollarbeitsplanDto lossollarbeitsplanDto = new LossollarbeitsplanDto();

			ParametermandantDto parameterDtoDefaultarbeitszeit = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_DEFAULT_ARBEITSZEITARTIKEL);

			if (parameterDtoDefaultarbeitszeit != null && parameterDtoDefaultarbeitszeit.getCWert() != null
					&& !parameterDtoDefaultarbeitszeit.getCWert().trim().equals("")) {
				try {

					ArtikelDto artikelDtoDefaultArbeiztszeit = getArtikelFac()
							.artikelFindByCNr(parameterDtoDefaultarbeitszeit.getCWert(), theClientDto);
					lossollarbeitsplanDto.setArtikelIIdTaetigkeit(artikelDtoDefaultArbeiztszeit.getIId());
				} catch (EJBExceptionLP ex2) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DEFAULT_ARBEITSZEITARTIKEL_NICHT_DEFINIERT,
							new Exception("FEHLER_DEFAULT_ARBEITSZEITARTIKEL_NICHT_DEFINIERT"));
				}
				lossollarbeitsplanDto.setBNachtraeglich(Helper.boolean2Short(false));
				lossollarbeitsplanDto.setIArbeitsgangnummer(10);
				lossollarbeitsplanDto.setLosIId(losDto.getIId());
				lossollarbeitsplanDto.setLRuestzeit((long) 1000);
				lossollarbeitsplanDto.setLStueckzeit((long) 0);
				lossollarbeitsplanDto.setBFertig(Helper.boolean2Short(false));
				lossollarbeitsplanDto.setBNurmaschinenzeit(Helper.boolean2Short(false));
				// SP1188
				lossollarbeitsplanDto.setBAutoendebeigeht(Helper.boolean2Short(true));
				lossollarbeitsplanDto = createLossollarbeitsplan(lossollarbeitsplanDto, theClientDto);
				return lossollarbeitsplanDto.getIId();

			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return null;
	}

	public void removeLos(LosDto losDto, TheClientDto theClientDto) throws EJBExceptionLP {

		if (losDto != null) {
			Integer iId = losDto.getIId();
			Los toRemove = em.find(Los.class, iId);
			if (toRemove == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			HvDtoLogger<LosDto> hvLogger = new HvDtoLogger<LosDto>(em, losDto.getIId(), theClientDto);
			hvLogger.logDelete(losDto);

			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
			}
		}

	}

	public void vonSollpositionMengeZuruecknehmen(Integer lossollmaterialIId, BigDecimal nMenge, Integer losIIdZiel,
			Integer lagerIIdZiel, TheClientDto theClientDto) {

		BigDecimal bdAusgegeben = getAusgegebeneMenge(lossollmaterialIId, null, theClientDto);

		BigDecimal bdMengeGesamt = new BigDecimal(nMenge.doubleValue());

		if (nMenge.doubleValue() > bdAusgegeben.doubleValue()) {
			nMenge = bdAusgegeben;
		}

		LossollmaterialDto solldto = lossollmaterialFindByPrimaryKey(lossollmaterialIId);

		if (nMenge.doubleValue() > 0) {

			LosistmaterialDto[] dtosLosist = losistmaterialFindByLossollmaterialIId(lossollmaterialIId);

			for (int j = 0; j < dtosLosist.length; j++) {
				if (dtosLosist[j].getNMenge().doubleValue() > 0 && nMenge.doubleValue() > 0) {
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

					updateLosistmaterialMenge(dtosLosist[j].getIId(), bdMengeNeu, theClientDto);

					if (lagerIIdZiel != null && diffPosition.doubleValue() > 0) {
						try {
							if (!lagerIIdZiel.equals(dtosLosist[j].getLagerIId())) {
								getLagerFac().bucheUm(solldto.getArtikelIId(), dtosLosist[j].getLagerIId(),
										solldto.getArtikelIId(), lagerIIdZiel, diffPosition, null,
										"Umbuchung Materialumlagerung",
										getLagerFac().getGemittelterGestehungspreisEinerAbgangsposition(
												LocaleFac.BELEGART_LOS, dtosLosist[j].getIId()),
										theClientDto);
							}
						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}
					}

					if (losIIdZiel != null) {

						LossollmaterialDto[] sollDtos = lossollmaterialFindyByLosIIdArtikelIId(losIIdZiel,
								solldto.getArtikelIId(), theClientDto);

						if (sollDtos != null && sollDtos.length > 0) {
							LossollmaterialDto solldtoZiel = lossollmaterialFindByPrimaryKey(sollDtos[0].getIId());
							LosistmaterialDto losistmaterialDto = new LosistmaterialDto();
							losistmaterialDto.setLagerIId(dtosLosist[j].getLagerIId());
							losistmaterialDto.setBAbgang(Helper.boolean2Short(true));
							losistmaterialDto.setNMenge(diffPosition);

							gebeMaterialNachtraeglichAus(solldtoZiel, losistmaterialDto, null, true, theClientDto);
						} else {
							LossollmaterialDto lossollmaterialDto = new LossollmaterialDto();
							lossollmaterialDto.setLosIId(losIIdZiel);
							try {
								lossollmaterialDto.setNSollpreis(getLagerFac().getGemittelterGestehungspreisEinesLagers(
										solldto.getArtikelIId(), dtosLosist[j].getLagerIId(), theClientDto));
							} catch (RemoteException e) {
								throwEJBExceptionLPRespectOld(e);
							}
							lossollmaterialDto.setNMenge(bdMengeGesamt);
							lossollmaterialDto.setMontageartIId(solldto.getMontageartIId());

							ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(solldto.getArtikelIId(),
									theClientDto);

							lossollmaterialDto.setEinheitCNr(aDto.getEinheitCNr());
							lossollmaterialDto.setArtikelIId(solldto.getArtikelIId());

							LosistmaterialDto losistmaterialDto = new LosistmaterialDto();
							losistmaterialDto.setLagerIId(dtosLosist[j].getLagerIId());
							losistmaterialDto.setBAbgang(Helper.boolean2Short(true));

							losistmaterialDto.setNMenge(diffPosition);

							gebeMaterialNachtraeglichAus(lossollmaterialDto, losistmaterialDto, null, true,
									theClientDto);
						}

					}

				}
			}
		}

	}

	@Override
	@org.jboss.ejb3.annotation.TransactionTimeout(1000)

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void aendereLosgroesseSubtransaction(Integer losId, Integer neueLosgroesse,
			boolean bUeberzaehligesMaterialZurueckgeben, TheClientDto theClientDto) {
		aendereLosgroesse(losId, neueLosgroesse, bUeberzaehligesMaterialZurueckgeben, theClientDto);
	}

	@org.jboss.ejb3.annotation.TransactionTimeout(1000)

	public void aendereLosgroesse(Integer losIId, Integer neueLosgroesse, boolean bUeberzaehligesMaterialZurueckgeben,
			TheClientDto theClientDto) {
		LosDto losDto = losFindByPrimaryKey(losIId);
		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_AUSGEGEBEN)
				|| losDto.getStatusCNr().equals(FertigungFac.STATUS_IN_PRODUKTION)
				|| losDto.getStatusCNr().equals(FertigungFac.STATUS_GESTOPPT)
				|| losDto.getStatusCNr().equals(FertigungFac.STATUS_TEILERLEDIGT)) {

			BigDecimal bdNeueLosgroesse = new BigDecimal(neueLosgroesse);
			if (!losDto.getNLosgroesse().equals(bdNeueLosgroesse)) {

				BigDecimal bdErledigte = getErledigteMenge(losDto.getIId(), theClientDto);

				if (bdErledigte.doubleValue() > neueLosgroesse.doubleValue()) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_AENDERUNG_LOGROESSE_ZUVIELEABLIEFERUNGEN,
							new Exception("bdErledigte.doubleValue()>neueLosgroesse.doubleValue()"));
				}

				Los los = em.find(Los.class, losDto.getIId());
				los.setNLosgroesse(bdNeueLosgroesse);
				em.merge(los);
				em.flush();

				LosDto losDto_Nachher = losFindByPrimaryKey(losDto.getIId());
				HvDtoLogger<LosDto> logger = new HvDtoLogger<LosDto>(em, losDto.getIId(), theClientDto);
				logger.log(losDto, losDto_Nachher);

				// Material
				LossollmaterialDto[] dtos = lossollmaterialFindByLosIId(losIId);
				for (int i = 0; i < dtos.length; i++) {
					LossollmaterialDto dto = dtos[i];
					// Sollmengen aendern

					if (Helper.short2boolean(dto.getBRuestmenge()) == false) {
						BigDecimal sollsatzgroesse = dto.getNMenge().divide(losDto.getNLosgroesse(), 10,
								BigDecimal.ROUND_HALF_EVEN);
						dto.setNMenge(
								Helper.rundeKaufmaennisch(sollsatzgroesse.multiply(new BigDecimal(neueLosgroesse)), 3));
					}

					// Aufgrund SP8893 wieder auskommentiert

					/*
					 * if (Helper.short2boolean(dto.getBRuestmenge()) == false) { // SP7277
					 * BigDecimal neueGroesse; try { neueGroesse = berechneNeueSollsatzgroesse(dto,
					 * losDto.getNLosgroesse(), bdNeueLosgroesse, theClientDto); } catch
					 * (RemoteException e) { neueGroesse = dto.getNMenge()
					 * .divide(losDto.getNLosgroesse(), 10, BigDecimal.ROUND_HALF_EVEN)
					 * .multiply(bdNeueLosgroesse); }
					 * dto.setNMenge(Helper.rundeKaufmaennisch(neueGroesse, 3)); }
					 */

					updateLossollmaterial(dto, theClientDto);

					// Wenn kleiner je nach parameter
					// bUeberzaehligesMaterialZurueckgeben material
					// zurueckbuchen
					if (neueLosgroesse.doubleValue() < losDto.getNLosgroesse().doubleValue()
							&& bUeberzaehligesMaterialZurueckgeben == true) {
						BigDecimal bdAusgegeben = getAusgegebeneMenge(dto.getIId(), null, theClientDto);
						BigDecimal diff = bdAusgegeben.subtract(dto.getNMenge());
						if (diff.doubleValue() > 0) {

							LosistmaterialDto[] dtosLosist = losistmaterialFindByLossollmaterialIId(dto.getIId());

							for (int j = 0; j < dtosLosist.length; j++) {
								if (diff.doubleValue() > 0) {
									BigDecimal istmenge = dtosLosist[j].getNMenge();

									if (istmenge.doubleValue() > 0) {
										BigDecimal bdMengeNeu = null;

										if (diff.doubleValue() > istmenge.doubleValue()) {
											bdMengeNeu = new BigDecimal(0);
											diff = diff.subtract(istmenge);
										} else {
											bdMengeNeu = istmenge.subtract(diff);
											diff = new BigDecimal(0);
										}

										updateLosistmaterialMenge(dtosLosist[j].getIId(), bdMengeNeu, theClientDto);
									}
								}
							}
						}
					}

					// Fehlmengen aktualisieren
					try {
						getFehlmengeFac().aktualisiereFehlmenge(LocaleFac.BELEGART_LOS, dto.getIId(), theClientDto);
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
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT,
						new Exception("los " + losDto.getCNr() + " ist storniert"));
			} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
						new Exception("los " + losDto.getCNr() + " ist bereits erledigt"));
			} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN,
						new Exception("los " + losDto.getCNr() + " ist noch nicht ausgegeben"));
			}
		}
	}

	private BigDecimal berechneNeueSollsatzgroesse(LossollmaterialDto dto, BigDecimal alteLosgroesse,
			BigDecimal neueLosgroesse, TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {

		ParametermandantDto parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
				theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
				ParameterFac.PARAMETER_VERSCHNITT_NACH_ABMESSUNG);
		boolean bVerschnittNachAbmessung = (Boolean) parameter.getCWertAsObject();

		String stkEinheit = dto.getEinheitCNrStklPos();
		BigDecimal stkMenge = dto.getNMengeStklPos();
		BigDecimal mengeProLos = dto.getnMengeProLos();
		if (bVerschnittNachAbmessung || (mengeProLos == null && (stkEinheit == null || stkMenge == null))) {
			// keine Info fuer StkMenge/Einheit. Alte umrechnung.
			return dto.getNMenge().divide(alteLosgroesse, 10, RoundingMode.HALF_EVEN).multiply(neueLosgroesse);
		}

		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(dto.getArtikelIId(), theClientDto);
		if (mengeProLos == null) {
			EinheitDto einheitStklDto = getSystemFac().einheitFindByPrimaryKey(stkEinheit, theClientDto);
			Integer dimension = einheitStklDto.getIDimension();
			if (dimension != null && dimension != 0) {
				// Wenn Artikel abmessungen hat, koennen wir es nicht neu berechnen,
				// weil die abmessungen nicht im los sollmaterial vorhanden sind
				return dto.getNMenge().divide(alteLosgroesse, 10, RoundingMode.HALF_EVEN).multiply(neueLosgroesse);
			}

			// Hier kann mit nur den Infos die wir haben alles neu gemacht werden:

			BigDecimal losSollMenge = getSystemFac().rechneUmInAndereEinheit(stkMenge, stkEinheit,
					artikelDto.getEinheitCNr(), null, theClientDto);
			losSollMenge = Helper.berechneMengeInklusiveVerschnitt(losSollMenge, artikelDto.getFVerschnittfaktor(),
					artikelDto.getFVerschnittbasis(), neueLosgroesse, artikelDto.getFFertigungsVpe());

			losSollMenge = losSollMenge.multiply(neueLosgroesse);

			if (losSollMenge.doubleValue() < 0.001 && losSollMenge.doubleValue() > 0.000001) {
				losSollMenge = new BigDecimal("0.001");
				losSollMenge = losSollMenge.setScale(3, BigDecimal.ROUND_HALF_EVEN);
			} else {
				losSollMenge = losSollMenge.setScale(3, BigDecimal.ROUND_HALF_EVEN);
			}

			return losSollMenge;
		} else {
			BigDecimal mitVerschnitt = Helper.berechneMengeInklusiveVerschnitt(mengeProLos,
					artikelDto.getFVerschnittfaktor(), artikelDto.getFVerschnittbasis(), neueLosgroesse,
					artikelDto.getFFertigungsVpe());
			BigDecimal gesMenge = mitVerschnitt.multiply(neueLosgroesse);
			return gesMenge;
		}
	}

	public void updateLosKommentar(Integer losIId, String cKommentar) {
		Los los = em.find(Los.class, losIId);
		if (los == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}
		los.setXText(cKommentar);
	}

	public void updateLosProduktionsinformation(Integer losIId, String cProduktionsinformation) {
		Los los = em.find(Los.class, losIId);
		if (los == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}
		los.setXProduktionsinformation(cProduktionsinformation);
		em.merge(los);
		em.flush();

	}

	public void updateLosLagerplatz(Integer losIId, Integer lagerplatzIId) {
		Los los = em.find(Los.class, losIId);
		if (los == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}
		los.setLagerplatzIId(lagerplatzIId);
		em.merge(los);
		em.flush();

	}

	public void updateBewertungKommentarImLos(LosDto losDto, TheClientDto theClientDto) throws EJBExceptionLP {
		Los los = em.find(Los.class, losDto.getIId());
		if (los == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}
		los.setFBewertung(losDto.getFBewertung());
		los.setCProjekt(losDto.getCProjekt());
	}

	public void offenAgsUmreihen(Integer lossollarbeitsplanIId, boolean bNachUntenReihen) {
		Lossollarbeitsplan sa = em.find(Lossollarbeitsplan.class, lossollarbeitsplanIId);
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

	public void offenAgsNachAGBeginnReihen(Integer maschineIId, TheClientDto theClientDto) {

		String sQuery = " from FLROffeneags flroffeneags  LEFT OUTER JOIN flroffeneags.flrartikel_taetigkeit.artikelsprset AS aspr_taetigkeit  LEFT OUTER JOIN flroffeneags.flrartikel_stueckliste.artikelsprset AS aspr_stueckliste  LEFT OUTER JOIN flroffeneags.flrartikel_sollmaterial.artikelsprset AS aspr_sollmaterial  LEFT OUTER JOIN flroffeneags.flrkunde AS flrkunde  LEFT OUTER JOIN flroffeneags.flrmaschine AS flrmaschine  LEFT OUTER JOIN flroffeneags.flrartikel_taetigkeit AS taetigkeit  LEFT OUTER JOIN flroffeneags.flrartikel_sollmaterial AS sollmaterial  LEFT OUTER JOIN flroffeneags.flrartikel_stueckliste AS stueckliste  LEFT OUTER JOIN flroffeneags.flrlossollarbeitsplan AS sollarbeitsplan  LEFT OUTER JOIN flroffeneags.flrlos AS flrlos  LEFT OUTER JOIN flrlos.flrauftrag AS flrauftrag WHERE flroffeneags.mandant_c_nr = '"
				+ theClientDto.getMandant() + "' ";

		sQuery += " AND flroffeneags.flrmaschine.i_id=" + maschineIId;

		sQuery += "  ORDER BY flrmaschine.c_identifikationsnr ASC, flroffeneags.t_agbeginn ASC, flroffeneags.i_maschinenversatz_ms ASC, flroffeneags.flrlos.c_nr ASC,  flroffeneags.i_id ";

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		org.hibernate.Query query = session.createQuery(sQuery);
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();
		int iReihung = 0;

		while (resultListIterator.hasNext()) {

			Object[] o = (Object[]) resultListIterator.next();
			FLROffeneags flrOffeneags = (FLROffeneags) o[0];
			Lossollarbeitsplan sa = em.find(Lossollarbeitsplan.class,
					flrOffeneags.getFlrlossollarbeitsplan().getI_id());
			sa.setIReihung(iReihung);
			iReihung++;

		}

	}

	@Override
	public void offenenAgAlsErstenReihen(Integer maschineIId, Integer wirdErsterAgIId, TheClientDto theClientDto) {

		String sQuery = "FROM FLROffeneags flroffeneags" +
//				" LEFT OUTER JOIN flroffeneags.flrartikel_taetigkeit.artikelsprset AS aspr_taetigkeit" +
//				" LEFT OUTER JOIN flroffeneags.flrartikel_stueckliste.artikelsprset AS aspr_stueckliste" +
//				" LEFT OUTER JOIN flroffeneags.flrartikel_sollmaterial.artikelsprset AS aspr_sollmaterial" +
//				" LEFT OUTER JOIN flroffeneags.flrkunde AS flrkunde" +
				" LEFT OUTER JOIN flroffeneags.flrmaschine AS flrmaschine" +
//				" LEFT OUTER JOIN flroffeneags.flrartikel_taetigkeit AS taetigkeit" +
//				" LEFT OUTER JOIN flroffeneags.flrartikel_sollmaterial AS sollmaterial" +
//				" LEFT OUTER JOIN flroffeneags.flrartikel_stueckliste AS stueckliste" +
				" LEFT OUTER JOIN flroffeneags.flrlossollarbeitsplan AS sollarbeitsplan"
				+ " LEFT OUTER JOIN flroffeneags.flrlos AS flrlos" +
//				" LEFT OUTER JOIN flrlos.flrauftrag AS flrauftrag" + 
				" WHERE flroffeneags.mandant_c_nr = '" + theClientDto.getMandant() + "' ";

		sQuery += " AND flroffeneags.flrmaschine.i_id=" + maschineIId;

		sQuery += "  ORDER BY flroffeneags.flrlossollarbeitsplan.i_reihung ASC," + " flroffeneags.t_agbeginn ASC,"
				+ " flroffeneags.i_maschinenversatz_ms ASC," + " flroffeneags.flrlos.c_nr ASC, flroffeneags.i_id ";

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		org.hibernate.Query query = session.createQuery(sQuery);
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();
		int iReihung = 2;

		while (resultListIterator.hasNext()) {

			Object[] o = (Object[]) resultListIterator.next();
			FLROffeneags flrOffeneags = (FLROffeneags) o[0];
			Lossollarbeitsplan sa = em.find(Lossollarbeitsplan.class,
					flrOffeneags.getFlrlossollarbeitsplan().getI_id());
			if (sa.getIId().equals(wirdErsterAgIId)) {
				sa.setIReihung(1);
			} else {
				sa.setIReihung(iReihung);
				iReihung++;
			}
		}

		session.close();
	}

	public void losSplitten(Integer losIId, BigDecimal bdLosgroesseNeuesLos, java.sql.Date bdBeginnTerminNeuesLos,
			TheClientDto theClientDto) {
		// PJ20861
		LosDto losDtoBestehend = losFindByPrimaryKey(losIId);

		int iTageDiff = Helper.ermittleTageEinesZeitraumes(losDtoBestehend.getTProduktionsbeginn(),
				losDtoBestehend.getTProduktionsende());

		BigDecimal bdMengeFuerBestehendesLos = losDtoBestehend.getNLosgroesse().subtract(bdLosgroesseNeuesLos);

		losDtoBestehend.setNLosgroesse(bdMengeFuerBestehendesLos);
		updateLos(losDtoBestehend, theClientDto);

		losDtoBestehend.setNLosgroesse(bdLosgroesseNeuesLos);

		losDtoBestehend.setTProduktionsbeginn(bdBeginnTerminNeuesLos);
		losDtoBestehend.setTProduktionsende(Helper.addiereTageZuDatum(bdBeginnTerminNeuesLos, iTageDiff));
		// Aufgrund SP7147 auskommentiert:
		// losDtoBestehend.setAuftragIId(null);
		// losDtoBestehend.setAuftragpositionIId(null);
		createLos(losDtoBestehend, theClientDto);

	}

	public LosDto updateLos(LosDto losDto, TheClientDto theClientDto) {
		return updateLos(losDto, false, theClientDto);
	}

	public LosDto updateLos(LosDto losDto, boolean bErsatztypenAuslassen, TheClientDto theClientDto)
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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
					new Exception("los " + los.getCNr() + " ist bereits erledigt"));
		} else if (los.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT,
					new Exception("los " + los.getCNr() + " ist storniert"));
		}
		// wurde die Losgroesse geaendert?
		if (los.getNLosgroesse().compareTo(losDto.getNLosgroesse()) != 0) {
			bUdateMenge = true;
		}
		// wurde die Stueckliste geaendert?
		boolean bArbeitsplanUpdaten = false;
		if (los.getStuecklisteIId() != null && !los.getStuecklisteIId().equals(losDto.getStuecklisteIId())) {
			bUdateMenge = true;
			bArbeitsplanUpdaten = true;
		}
		if (los.getStuecklisteIId() == null && losDto.getStuecklisteIId() != null) {
			bUdateMenge = true;
			bArbeitsplanUpdaten = true;
		}

		// wurde der Produktionsbeginn geaendert?
		if (los.getTProduktionsbeginn().compareTo(losDto.getTProduktionsbeginn()) != 0) {
			bUdateProduktionsbeginn = true;
		}

		if (bArbeitsplanUpdaten == true) {
			if (los.getStuecklisteIId() != null && losDto.getStuecklisteIId() == null) {
				getFertigungFac().vorhandenenArbeitsplanLoeschen(losDto.getIId());
				defaultArbeitszeitartikelErstellen(losDto, theClientDto);
				// Material entfernen

				LossollmaterialDto[] sollmatDtos = lossollmaterialFindByLosIId(losDto.getIId());
				for (int i = 0; i < sollmatDtos.length; i++) {

					// PJ 16952
					if (Helper.short2boolean(sollmatDtos[i].getBNachtraeglich()) == false) {
						removeLossollmaterial(sollmatDtos[i], theClientDto);
					}
				}
				bUdateMenge = true;
			} else {
				setLosFromLosDto(los, losDto);
				aktualisiereSollArbeitsplanAusStueckliste(losDto.getIId(), theClientDto);
			}
		}

		LosDto losDto_vorher = losFindByPrimaryKey(losDto.getIId());
		HvDtoLogger<LosDto> logger = new HvDtoLogger<LosDto>(em, losDto.getIId(), theClientDto);
		logger.log(losDto_vorher, losDto);

		setLosFromLosDto(los, losDto);
		// dauer neu berechnen

		if (bUdateMenge) {
			// Gesamtzeiten aufrollen
			updateGesamtzeiten(losDto.getIId(), theClientDto);
		}
		if (losDto.getStuecklisteIId() != null) {
			if (bUdateMenge || bUdateProduktionsbeginn) {
				// Reservierungen aktualisieren
				aktualisiereSollMaterialAusStueckliste(losDto.getIId(), theClientDto, true, bErsatztypenAuslassen);
			}
		}
		// und retour

		if (bUdateProduktionsbeginn) {
			try {
				ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_AUTOMATISCHE_ERMITTLUNG_LOS_ENDE);

				boolean bAutomatischeErmittlungLosEnde = (Boolean) parameter.getCWertAsObject();

				if (bAutomatischeErmittlungLosEnde) {
					berechneLosEndeAnhandMaschinenversatztage(losDto.getIId(), theClientDto);
				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}

		return losDto;
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, ex);
		// }
	}

	public LosDto losFindByPrimaryKey(Integer iId) throws EJBExceptionLP {

		LosDto los = losFindByPrimaryKeyOhneExc(iId);
		if (los == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
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

	public LosDto losFindByCNrMandantCNr(String cNr, String mandantCNr) throws EJBExceptionLP {
		try {
			Query query = em.createNamedQuery("LosfindByCNrMandantCNr");
			query.setParameter(1, cNr);
			query.setParameter(2, mandantCNr);
			Los los = (Los) query.getSingleResult();
			if (los == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleLosDto(los);
		} catch (NoResultException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}
	}

	public LosDto[] losFindByCProjektMandantCNr(String cProjekt, String mandantCNr) {

		Query query = em.createNamedQuery("LosfindByCProjektMandantCNr");
		query.setParameter(1, cProjekt);
		query.setParameter(2, mandantCNr);
		Collection c = query.getResultList();

		return assembleLosDtos(c);

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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}
	}

	public LosDto[] losFindByAuftragIIdMitStornierten(Integer auftragIId) {
		try {
			Query query = em.createNamedQuery("LosfindByAuftragIId");
			query.setParameter(1, auftragIId);
			Collection c = query.getResultList();

			return assembleLosDtos(c, true);
		} catch (NoResultException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}
	}

	public LosDto[] losFindByAuftragpositionIId(Integer auftragpositionIId) {
		try {
			Query query = em.createNamedQuery("LosfindByAuftragpositionIId");
			query.setParameter(1, auftragpositionIId);
			Collection c = query.getResultList();

			return assembleLosDtos(c);
		} catch (NoResultException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}
	}

	public LosDto[] losFindByAuftragIIdStuecklisteIId(Integer auftragIId, Integer stuecklisteIId) {
		try {
			Query query = em.createNamedQuery("LosfindByAuftragIIdStuecklisteIId");
			query.setParameter(1, auftragIId);
			query.setParameter(2, stuecklisteIId);
			Collection c = query.getResultList();

			return assembleLosDtos(c);
		} catch (NoResultException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
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
		los.setPersonalIIdProduktionsstop(losDto.getPersonalIIdProduktionsstop());
		los.setTLeitstandstop(losDto.getTLeitstandstop());
		los.setPersonalIIdLeitstandstop(losDto.getPersonalIIdLeitstandstop());
		los.setLagerIIdZiel(losDto.getLagerIIdZiel());
		los.setStatusCNr(losDto.getStatusCNr());
		los.setTAktualisierungstueckliste(losDto.getTAktualisierungstueckliste());
		los.setTAktualisierungarbeitszeit(losDto.getTAktualisierungarbeitszeit());
		los.setPersonalIIdAnlegen(losDto.getPersonalIIdAnlegen());
		los.setTAnlegen(losDto.getTAnlegen());
		los.setPersonalIIdAendern(losDto.getPersonalIIdAendern());
		los.setTAendern(losDto.getTAendern());
		los.setPersonalIIdManuellerledigt(losDto.getPersonalIIdManuellerledigt());
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
		los.setPersonalIIdMaterialvollstaendig(losDto.getPersonalIIdMaterialvollstaendig());
		los.setProjektIId(losDto.getProjektIId());
		los.setTVpEtikettengedruckt(losDto.getTVpEtikettengedruckt());
		los.setPersonalIIdVpEtikettengedruckt(losDto.getPersonalIIdVpEtikettengedruckt());
		los.setForecastpositionIId(losDto.getForecastpositionIId());
		los.setCSchachtelplan(losDto.getCSchachtelplan());
		los.setPersonalIIdNachtraeglichGeoeffnet(losDto.getPersonalIIdNachtraeglichGeoeffnet());
		los.setTNachtraeglichGeoeffnet(losDto.getTNachtraeglichGeoeffnet());
		los.setCAbposnr(losDto.getCAbposnr());
		los.setLagerplatzIId(losDto.getLagerplatzIId());

		em.merge(los);
		em.flush();
	}

	private LosDto assembleLosDto(Los los) {
		return LosDtoAssembler.createDto(los);
	}

	private LosDto[] assembleLosDtos(Collection<?> loss, boolean bMitStorniertenLosen) {
		List<LosDto> list = new ArrayList<LosDto>();
		if (loss != null) {
			Iterator<?> iterator = loss.iterator();
			while (iterator.hasNext()) {
				Los los = (Los) iterator.next();

				if (bMitStorniertenLosen == true) {
					list.add(assembleLosDto(los));
				} else {
					if (!los.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
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

	public LossollmaterialDto createLossollmaterialWithMaxISort(LossollmaterialDto lossollmaterialDto,
			TheClientDto theClientDto) {
		Query query = em.createNamedQuery("LossollmaterialMaxISortFindByLosIId");
		query.setParameter(1, lossollmaterialDto.getLosIId());
		Integer iMaxSort = (Integer) query.getSingleResult();
		iMaxSort = iMaxSort == null ? 0 : iMaxSort;
		lossollmaterialDto.setISort(new Integer(iMaxSort + 1));

		return createLossollmaterial(lossollmaterialDto, theClientDto);
	}

	public LossollmaterialDto createLossollmaterial(LossollmaterialDto lossollmaterialDto, TheClientDto theClientDto) {
		return createLossollmaterial(lossollmaterialDto, false, false, theClientDto);
	}

	public LossollmaterialDto createLossollmaterial(LossollmaterialDto lossollmaterialDto, boolean bErsatztype,
			boolean bErsatztypenAuslassen, TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(lossollmaterialDto);
		// begin
		LosDto losDto = losFindByPrimaryKey(lossollmaterialDto.getLosIId());
		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT, "");
		}
		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT, "");
		}

		if (losDto.getTMaterialvollstaendig() != null) {
			ArrayList al = new ArrayList();
			al.add(losDto.getCNr());
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_MATERIAL_VOLLSTAENDIG, al,
					new Exception("FEHLER_FERTIGUNG_MATERIAL_VOLLSTAENDIG" + " Los:" + losDto.getCNr()));
		}

		if (lossollmaterialDto.getIBeginnterminoffset() == null) {
			lossollmaterialDto.setIBeginnterminoffset(0);
		}

		lossollmaterialDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		// primary key
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_LOSSOLLMATERIAL);
		lossollmaterialDto.setIId(iId);

		if (lossollmaterialDto.getISort() == null) {
			Query query = em.createNamedQuery("LossollmaterialMaxISortFindByLosIId");
			query.setParameter(1, lossollmaterialDto.getLosIId());
			Integer iMaxSort = (Integer) query.getSingleResult();
			iMaxSort = iMaxSort == null ? 0 : iMaxSort;
			lossollmaterialDto.setISort(new Integer(iMaxSort + 1));
		}

		// nachtraeglich ?
		boolean bNachtraeglich = false;
		if (lossollmaterialDto.getBNachtraeglich() == null) {
			lossollmaterialDto.setBNachtraeglich(Helper.boolean2Short(true));
			bNachtraeglich = true;
		}

		if (lossollmaterialDto.getBRuestmenge() == null) {
			lossollmaterialDto.setBRuestmenge(Helper.boolean2Short(false));
		}

		if (lossollmaterialDto.getBDringend() == null) {
			lossollmaterialDto.setBDringend(Helper.boolean2Short(false));
		}

		if (getMandantFac().hatZusatzfunktionberechtigung(MandantFac.ZUSATZFUNKTION_GERAETESERIENNUMMERN,
				theClientDto)) {
			if (losDto.getStuecklisteIId() != null) {

				// PJ 16622

				Artikel artikel = em.find(Artikel.class, lossollmaterialDto.getArtikelIId());

				if (Helper.short2boolean(artikel.getBSeriennrtragend())) {

					StuecklisteDto kopfDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(),
							theClientDto);

					if (Helper.short2boolean(kopfDto.getArtikelDto().getBSeriennrtragend())) {
						BigDecimal ssg = lossollmaterialDto.getNMenge().divide(losDto.getNLosgroesse(), 4,
								BigDecimal.ROUND_HALF_UP);

						if (ssg.abs().doubleValue() != 1) {

							if (bErsatztype == true) {

								if (bErsatztypenAuslassen) {
									return null;
								} else {
									throw new EJBExceptionLP(
											EJBExceptionLP.FEHLER_POSITIONSMENGE_EINES_SNR_ARTIKELS_MUSS_1_SEIN_WENN_GERAETESNR_ERSATZTYPEN_AUSLASSEN,
											new Exception(
													"FEHLER_POSITIONSMENGE_EINES_SNR_ARTIKELS_MUSS_1_SEIN_WENN_GERAETESNR_ERSATZTYPEN_AUSLASSEN"));
								}

							} else {
								throw new EJBExceptionLP(
										EJBExceptionLP.FEHLER_POSITIONSMENGE_EINES_SNR_ARTIKELS_MUSS_1_SEIN_WENN_GERAETESNR,
										new Exception(
												"FEHLER_POSITIONSMENGE_EINES_SNR_ARTIKELS_MUSS_1_SEIN_WENN_GERAETESNR"));
							}

						}
					}
				}

			}
		}

		try {

			// SP2795

			StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByArtikelIIdMandantCNrOhneExc(
					lossollmaterialDto.getArtikelIId(), theClientDto.getMandant());

			if (stklDto != null
					&& stklDto.getStuecklisteartCNr().equals(StuecklisteFac.STUECKLISTEART_HILFSSTUECKLISTE)) {
				Artikel artikel = em.find(Artikel.class, lossollmaterialDto.getArtikelIId());
				ArrayList al = new ArrayList();
				al.add(artikel.getCNr());
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_HILFSSTUECKLISTE_DARF_KEINE_SOLLPOSITION_SEIN,
						al, new Exception("FEHLER_FERTIGUNG_HILFSSTUECKLISTE_DARF_KEINE_SOLLPOSITION_SEIN"));
			}

			// rounddto: vor dem Create
			lossollmaterialDto.round(new Integer(4),
					getMandantFac().getNachkommastellenPreisAllgemein(theClientDto.getMandant()));
			Lossollmaterial lossollmaterial = new Lossollmaterial(lossollmaterialDto.getIId(),
					lossollmaterialDto.getLosIId(), lossollmaterialDto.getArtikelIId(), lossollmaterialDto.getNMenge(),
					lossollmaterialDto.getEinheitCNr(), lossollmaterialDto.getMontageartIId(),
					lossollmaterialDto.getISort(), lossollmaterialDto.getBNachtraeglich(),
					lossollmaterialDto.getNSollpreis(), lossollmaterialDto.getPersonalIIdAendern(),
					lossollmaterialDto.getIBeginnterminoffset(), lossollmaterialDto.getBRuestmenge(),
					lossollmaterialDto.getBDringend(), lossollmaterialDto.getnMengeProLos());

			em.persist(lossollmaterial);
			em.flush();
			lossollmaterialDto.setTAendern(lossollmaterial.getTAendern());
			setLossollmaterialFromLossollmaterialDto(lossollmaterial, lossollmaterialDto, theClientDto);
			// reservierung
			if (bNachtraeglich) {
				// Reservierung anlegen
				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(lossollmaterialDto.getArtikelIId(),
						theClientDto);
				// wenn los angelegt -> reservierung
				if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
					java.sql.Date dTermin;
					if (lossollmaterialDto.getNMenge().compareTo(new BigDecimal(0)) > 0) {
						// Positive Reservierung: produktionsstart

						dTermin = losDto.getTProduktionsbeginn();
					} else {
						// Negative Reservierung: produktionsende
						dTermin = losDto.getTProduktionsende();
					}
					createReservierung(artikelDto, lossollmaterialDto.getIId(), lossollmaterialDto.getNMenge(),
							new java.sql.Timestamp(dTermin.getTime()));
				}
				// wenn ausgegeben -> fehlmenge
				else {
					getFehlmengeFac().aktualisiereFehlmenge(LocaleFac.BELEGART_LOS, lossollmaterialDto.getIId(),
							theClientDto);
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

	public void removeLossollmaterial(LossollmaterialDto lossollmaterialDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		removeLossollmaterial(lossollmaterialDto, false, theClientDto);
	}

	public void removeLossollmaterial(LossollmaterialDto lossollmaterialDto, boolean bIstMaterialLoeschen,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(lossollmaterialDto);
		LosDto losDto = losFindByPrimaryKey(lossollmaterialDto.getLosIId());
		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT, "");
		}

		if (losDto.getTMaterialvollstaendig() != null) {
			ArrayList al = new ArrayList();
			al.add(losDto.getCNr());
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_MATERIAL_VOLLSTAENDIG, al,
					new Exception("FEHLER_FERTIGUNG_MATERIAL_VOLLSTAENDIG" + " Los:" + losDto.getCNr()));
		}

		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT, "");
		}
		try {
			if (lossollmaterialDto != null) {

				// Istmaterial loeschen, wenn 0

				LosistmaterialDto[] istmatDtos = losistmaterialFindByLossollmaterialIId(lossollmaterialDto.getIId());

				for (int i = 0; i < istmatDtos.length; i++) {
					if (istmatDtos[i].getNMenge().doubleValue() == 0) {
						Losistmaterial toRemove = em.find(Losistmaterial.class, istmatDtos[i].getIId());
						em.remove(toRemove);
						em.flush();
					} else {

						if (bIstMaterialLoeschen) {
							removeLosistmaterial(istmatDtos[i], theClientDto);
						} else {

							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_ES_IST_NOCH_MATERIAL_AUSGEGEBEN,
									"");
						}
					}
				}

				Integer iId = lossollmaterialDto.getIId();
				Lossollmaterial toRemove = em.find(Lossollmaterial.class, iId);
				if (toRemove == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
				}
				try {
					em.remove(toRemove);
					em.flush();
				} catch (EntityExistsException er) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
				}
				// angelegt -> reservierung loeschen
				if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
					// Reservierung loeschen
					ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(lossollmaterialDto.getArtikelIId(),
							theClientDto);
					removeReservierung(artikelDto, lossollmaterialDto.getIId());
				}
				// ausgegeben -> fehlmenge loeschen
				else {
					getFehlmengeFac().aktualisiereFehlmenge(LocaleFac.BELEGART_LOS, lossollmaterialDto.getIId(),
							theClientDto);
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

	public LossollmaterialDto updateLossollmaterial(LossollmaterialDto lossollmaterialDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Integer iId = lossollmaterialDto.getIId();
		LosDto losDto = losFindByPrimaryKey(lossollmaterialDto.getLosIId());
		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT, "");
		}

		if (losDto.getTMaterialvollstaendig() != null) {
			ArrayList al = new ArrayList();
			al.add(losDto.getCNr());
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_MATERIAL_VOLLSTAENDIG, al,
					new Exception("FEHLER_FERTIGUNG_MATERIAL_VOLLSTAENDIG" + " Los:" + losDto.getCNr()));
		}

		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT, "");
		}
		try {
			Lossollmaterial lossollmaterial = em.find(Lossollmaterial.class, iId);

			if (!lossollmaterial.getArtikelIId().equals(lossollmaterialDto.getArtikelIId())) {
				// Wenn Update auf Artikel, dann Position loeschen und neu
				// anlegen

				// PJ22398
				Query queryAP = em.createNamedQuery("LossollarbeitsplanfindByLossollmaterialIId");
				queryAP.setParameter(1, lossollmaterial.getIId());
				Collection<?> clAP = queryAP.getResultList();

				Iterator itAP = clAP.iterator();
				while (itAP.hasNext()) {
					Lossollarbeitsplan b = (Lossollarbeitsplan) itAP.next();
					b.setLossollmaterialIId(null);
				}

				removeLossollmaterial(lossollmaterialDto, theClientDto);

				lossollmaterialDto = createLossollmaterial(lossollmaterialDto, theClientDto);

				lossollmaterial = em.find(Lossollmaterial.class, lossollmaterialDto.getIId());

				itAP = clAP.iterator();

				while (itAP.hasNext()) {
					Lossollarbeitsplan b = (Lossollarbeitsplan) itAP.next();
					b.setLossollmaterialIId(lossollmaterial.getIId());
				}

			}

			if (lossollmaterial == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
			}

			if (getMandantFac().hatZusatzfunktionberechtigung(MandantFac.ZUSATZFUNKTION_GERAETESERIENNUMMERN,
					theClientDto)) {
				if (losDto.getStuecklisteIId() != null) {

					// PJ 16622

					Artikel artikel = em.find(Artikel.class, lossollmaterialDto.getArtikelIId());
					if (Helper.short2boolean(artikel.getBSeriennrtragend())) {

						StuecklisteDto kopfDto = getStuecklisteFac()
								.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(), theClientDto);

						if (Helper.short2boolean(kopfDto.getArtikelDto().getBSeriennrtragend())) {
							BigDecimal ssg = lossollmaterialDto.getNMenge().divide(losDto.getNLosgroesse(), 4,
									BigDecimal.ROUND_HALF_UP);

							if (ssg.abs().doubleValue() != 1) {
								throw new EJBExceptionLP(
										EJBExceptionLP.FEHLER_POSITIONSMENGE_EINES_SNR_ARTIKELS_MUSS_1_SEIN_WENN_GERAETESNR,
										new Exception(
												"FEHLER_POSITIONSMENGE_EINES_SNR_ARTIKELS_MUSS_1_SEIN_WENN_GERAETESNR"));

							}
						}

					}

				}
			}
			lossollmaterialDto.setTAendern(new Timestamp(System.currentTimeMillis()));
			lossollmaterialDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			setLossollmaterialFromLossollmaterialDto(lossollmaterial, lossollmaterialDto, theClientDto);
			// angelegt -> reservierung updaten
			if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(lossollmaterial.getArtikelIId(),
						theClientDto);
				java.sql.Date dTermin;
				if (lossollmaterial.getNMenge().compareTo(new BigDecimal(0)) > 0) {
					// Positive Reservierung: produktionsstart
					dTermin = getFertigungFac().getProduktionsbeginnAnhandZugehoerigemArbeitsgang(
							losDto.getTProduktionsbeginn(), lossollmaterialDto.getIId(), theClientDto);
				} else {
					// Negative Reservierung: produktionsende
					dTermin = losDto.getTProduktionsende();
				}
				updateReservierung(artikelDto, lossollmaterialDto.getIId(), lossollmaterialDto.getNMenge(),
						new java.sql.Timestamp(dTermin.getTime()));
			}
			// ausgegeben -> fehlmenge aktualisieren
			else {
				getFehlmengeFac().aktualisiereFehlmenge(LocaleFac.BELEGART_LOS, lossollmaterialDto.getIId(),
						theClientDto);
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

	public LossollmaterialDto lossollmaterialFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		// try {
		Lossollmaterial lossollmaterial = em.find(Lossollmaterial.class, iId);
		if (lossollmaterial == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLostechnikerDto(lostechniker);

	}

	public BedarfsuebernahmeDto bedarfsuebernahmeFindByPrimaryKey(Integer iId) {
		Bedarfsuebernahme bean = em.find(Bedarfsuebernahme.class, iId);
		return BedarfsuebernahmeDtoAssembler.createDto(bean);
	}

	public LossollmaterialDto lossollmaterialFindByPrimaryKeyOhneExc(Integer iId) throws EJBExceptionLP {
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

	public LossollarbeitsplanDto lossollarbeitsplanFindByPrimaryKeyOhneExc(Integer iId) {
		Lossollarbeitsplan lossollarbeitsplan = em.find(Lossollarbeitsplan.class, iId);
		if (lossollarbeitsplan == null) {
			return null;
		}
		return assembleLossollarbeitsplanDto(lossollarbeitsplan);

	}

	public void tollgeLossollmaterialAlsDringendMarkieren(ArrayList<Integer> selectedIds, TheClientDto theClientDto) {

		for (int i = 0; i < selectedIds.size(); i++) {
			LossollmaterialDto sollmat = lossollmaterialFindByPrimaryKey(selectedIds.get(i));

			sollmat.setBDringend(Helper.boolean2Short(!Helper.short2boolean(sollmat.getBDringend())));

			updateLossollmaterial(sollmat, theClientDto);

		}

	}

	public LossollmaterialDto[] lossollmaterialFindByLosIId(Integer losIId) throws EJBExceptionLP {
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

	public LossollmaterialDto[] lossollmaterialFindByLossollmaterialIIdOriginal(Integer lossollmaterialIId) {
		Query query = em.createNamedQuery("LossollmaterialfindByLossollmaterialIIdOriginal");
		query.setParameter(1, lossollmaterialIId);
		Collection<?> cl = query.getResultList();
		return assembleLossollmaterialDtos(cl);
	}

	public LossollmaterialDto[] lossollmaterialFindByLosIIdOrderByISort(Integer losIId) throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("LossollmaterialfindByLosIIdOrderByISort");
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

	public LossollmaterialDto getErstesLossollmaterial(Integer losIId) throws EJBExceptionLP {
		LossollmaterialDto[] dtos = lossollmaterialFindByLosIIdOrderByISort(losIId);
		if (dtos.length > 0) {
			return dtos[0];
		} else {
			return null;
		}
	}

	private void setLossollmaterialFromLossollmaterialDto(Lossollmaterial lossollmaterial,
			LossollmaterialDto lossollmaterialDto, TheClientDto theClientDto) {
		try {
			// rounddto: vor dem update
			lossollmaterialDto.round(new Integer(4),
					getMandantFac().getNachkommastellenPreisAllgemein(theClientDto.getMandant()));
			lossollmaterial.setLosIId(lossollmaterialDto.getLosIId());
			lossollmaterial.setArtikelIId(lossollmaterialDto.getArtikelIId());
			lossollmaterial.setNMenge(lossollmaterialDto.getNMenge());
			lossollmaterial.setEinheitCNr(lossollmaterialDto.getEinheitCNr());
			lossollmaterial.setFDimension1(lossollmaterialDto.getFDimension1());
			lossollmaterial.setFDimension2(lossollmaterialDto.getFDimension2());
			lossollmaterial.setFDimension3(lossollmaterialDto.getFDimension3());
			lossollmaterial.setCPosition(lossollmaterialDto.getCPosition());
			lossollmaterial.setCKommentar(lossollmaterialDto.getCKommentar());
			lossollmaterial.setMontageartIId(lossollmaterialDto.getMontageartIId());
			lossollmaterial.setILfdnummer(lossollmaterialDto.getILfdnummer());
			lossollmaterial.setISort(lossollmaterialDto.getISort());
			lossollmaterial.setBNachtraeglich(lossollmaterialDto.getBNachtraeglich());
			lossollmaterial.setTAendern(lossollmaterialDto.getTAendern());
			lossollmaterial.setPersonalIIdAendern(lossollmaterialDto.getPersonalIIdAendern());
			lossollmaterial.setNSollpreis(lossollmaterialDto.getNSollpreis());
			lossollmaterial.setLossollmaterialIIdOriginal(lossollmaterialDto.getLossollmaterialIIdOriginal());
			lossollmaterial.setIBeginnterminoffset(lossollmaterialDto.getIBeginnterminoffset());
			lossollmaterial.setBRuestmenge(lossollmaterialDto.getBRuestmenge());
			lossollmaterial.setNMengeStklPos(lossollmaterialDto.getNMengeStklPos());
			lossollmaterial.setEinheitCNrStklPos(lossollmaterialDto.getEinheitCNrStklPos());
			lossollmaterial.setBDringend(lossollmaterialDto.getBDringend());
			lossollmaterial.setTExportBeginn(lossollmaterialDto.getTExportBeginn());
			lossollmaterial.setTExportEnde(lossollmaterialDto.getTExportEnde());
			lossollmaterial.setCFehlercode(lossollmaterialDto.getCFehlercode());
			lossollmaterial.setCFehlertext(lossollmaterialDto.getCFehlertext());
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		em.merge(lossollmaterial);
		em.flush();
	}

	private LossollmaterialDto assembleLossollmaterialDto(Lossollmaterial lossollmaterial) {
		return LossollmaterialDtoAssembler.createDto(lossollmaterial);
	}

	private LostechnikerDto assembleLostechnikerDto(Lostechniker lostechniker) {
		return LostechnikerDtoAssembler.createDto(lostechniker);
	}

	private List<LossollmaterialDto> assembleLossollmaterialDtosAsList(Collection<?> lossollmaterials) {
		List<LossollmaterialDto> list = new ArrayList<LossollmaterialDto>();
		if (lossollmaterials != null) {
			Iterator<?> iterator = lossollmaterials.iterator();
			while (iterator.hasNext()) {
				Lossollmaterial lossollmaterial = (Lossollmaterial) iterator.next();
				list.add(assembleLossollmaterialDto(lossollmaterial));
			}
		}
		return list;
	}

	private LossollmaterialDto[] assembleLossollmaterialDtos(Collection<?> lossollmaterials) {
		List<LossollmaterialDto> list = assembleLossollmaterialDtosAsList(lossollmaterials);
		return (LossollmaterialDto[]) list.toArray(new LossollmaterialDto[list.size()]);
	}

	public LossollarbeitsplanDto createLossollarbeitsplan(LossollarbeitsplanDto lossollarbeitsplanDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(lossollarbeitsplanDto);
		// begin
		lossollarbeitsplanDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		// primary key
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_LOSSOLLARBEITSPLAN);
		lossollarbeitsplanDto.setIId(iId);

		lossollarbeitsplanDto.setIMaschinenversatztageAusStueckliste(lossollarbeitsplanDto.getIMaschinenversatztage());

		// nachtraeglich ?
		if (lossollarbeitsplanDto.getBNachtraeglich() == null) {
			lossollarbeitsplanDto.setBNachtraeglich(Helper.boolean2Short(true));
		}

		if (lossollarbeitsplanDto.getIReihung() == null) {
			lossollarbeitsplanDto.setIReihung(0);
		}

		// Gesamtzeit berechnen
		LosDto losDto = losFindByPrimaryKey(lossollarbeitsplanDto.getLosIId());

		lossollarbeitsplanDto.setNGesamtzeit(Helper.berechneGesamtzeitInStunden(lossollarbeitsplanDto.getLRuestzeit(),
				lossollarbeitsplanDto.getLStueckzeit(), losDto.getNLosgroesse(), null,
				lossollarbeitsplanDto.getIAufspannung()));

		// PJ 16396

		ArtikelDto artikelDto = getArtikelFac()
				.artikelFindByPrimaryKeySmall(lossollarbeitsplanDto.getArtikelIIdTaetigkeit(), theClientDto);
		// PJ 16851
		if (Helper.short2boolean(artikelDto.getbReinemannzeit()) == false) {

			if (lossollarbeitsplanDto.getAgartCNr() != null) {
				Query query = em.createNamedQuery("LossollarbeitsplanfindByLosIIdIArbeitsgangnummer");
				query.setParameter(1, lossollarbeitsplanDto.getLosIId());
				query.setParameter(2, lossollarbeitsplanDto.getIArbeitsgangnummer());
				Collection<?> cl = query.getResultList();
				Iterator it = cl.iterator();
				while (it.hasNext()) {
					Lossollarbeitsplan ap = (Lossollarbeitsplan) it.next();
					if (ap.getAgartCNr() == null) {
						lossollarbeitsplanDto.setMaschineIId(ap.getMaschineIId());
						break;
					}
				}
			}
		}

		if (Helper.short2boolean(lossollarbeitsplanDto.getBFertig()) == true) {
			lossollarbeitsplanDto.setTFertig(getTimestamp());
			lossollarbeitsplanDto.setPersonalIIdFertig(theClientDto.getIDPersonal());
		}

		try {
			Lossollarbeitsplan lossollarbeitsplan = new Lossollarbeitsplan(lossollarbeitsplanDto.getIId(),
					lossollarbeitsplanDto.getLosIId(), lossollarbeitsplanDto.getArtikelIIdTaetigkeit(),
					lossollarbeitsplanDto.getLRuestzeit(), lossollarbeitsplanDto.getLStueckzeit(),
					lossollarbeitsplanDto.getNGesamtzeit(), lossollarbeitsplanDto.getIArbeitsgangnummer(),
					lossollarbeitsplanDto.getPersonalIIdAendern(), lossollarbeitsplanDto.getBNachtraeglich(),
					lossollarbeitsplanDto.getBFertig(), lossollarbeitsplanDto.getBAutoendebeigeht(),
					lossollarbeitsplanDto.getBNurmaschinenzeit(), lossollarbeitsplanDto.getIReihung());
			em.persist(lossollarbeitsplan);
			em.flush();
			lossollarbeitsplanDto.setTAendern(lossollarbeitsplan.getTAendern());
			setLossollarbeitsplanFromLossollarbeitsplanDto(lossollarbeitsplan, lossollarbeitsplanDto);
			// und retour

			// PJ20837
			if (lossollarbeitsplanDto.getLossollmaterialIId() != null) {
				updateLossollmaterial(lossollmaterialFindByPrimaryKey(lossollarbeitsplanDto.getLossollmaterialIId()),
						theClientDto);
			}

			return lossollarbeitsplanDto;
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeLossollarbeitsplan(LossollarbeitsplanDto lossollarbeitsplanDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
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
			Lossollarbeitsplan toRemove = em.find(Lossollarbeitsplan.class, iId);
			if (toRemove == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
			}
		}

		// PJ20837
		if (lossollarbeitsplanDto.getLossollmaterialIId() != null) {
			updateLossollmaterial(lossollmaterialFindByPrimaryKey(lossollarbeitsplanDto.getLossollmaterialIId()),
					theClientDto);
		}

	}

	public LossollarbeitsplanDto updateLossollarbeitsplan(LossollarbeitsplanDto lossollarbeitsplanDto,
			boolean verschiebeNachfolger, TheClientDto theClientDto) throws EJBExceptionLP {
		Integer iId = lossollarbeitsplanDto.getIId();

		Lossollarbeitsplan lossollarbeitsplan = em.find(Lossollarbeitsplan.class, iId);
		if (lossollarbeitsplan == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}

		if (Helper.short2boolean(lossollarbeitsplanDto.getBFertig()) == true
				&& Helper.short2boolean(lossollarbeitsplan.getBFertig()) == false) {
			lossollarbeitsplanDto.setTFertig(getTimestamp());
			lossollarbeitsplanDto.setPersonalIIdFertig(theClientDto.getIDPersonal());
		} else if (Helper.short2boolean(lossollarbeitsplanDto.getBFertig()) == false) {
			lossollarbeitsplanDto.setTFertig(null);
			lossollarbeitsplanDto.setPersonalIIdFertig(null);
		}

		Integer lossollmaterialIId1 = lossollarbeitsplanDto.getLossollmaterialIId();
		Integer lossollmaterialIId2 = lossollarbeitsplan.getLossollmaterialIId();

		if (verschiebeNachfolger && lossollarbeitsplanDto.getIMaschinenversatztage() != null) {
			int iVorher = 0;

			if (lossollarbeitsplan.getIMaschinenversatztage() != null) {
				iVorher = lossollarbeitsplan.getIMaschinenversatztage();
			}

			if (iVorher != lossollarbeitsplanDto.getIMaschinenversatztage()) {
				int delta = lossollarbeitsplanDto.getIMaschinenversatztage() - iVorher;

				if (delta != 0) {
					LossollarbeitsplanDto[] saDtos = lossollarbeitsplanFindByLosIId(lossollarbeitsplanDto.getLosIId());

					for (int i = 0; i < saDtos.length; i++) {
						if (lossollarbeitsplanDto.getIId().equals(saDtos[i].getIId())) {

							for (int j = i + 1; j < saDtos.length; j++) {
								Lossollarbeitsplan lossollarbeitsplanTemp = em.find(Lossollarbeitsplan.class,
										saDtos[j].getIId());

								if (lossollarbeitsplanTemp.getIMaschinenversatztage() != null) {
									lossollarbeitsplanTemp.setIMaschinenversatztage(
											lossollarbeitsplanTemp.getIMaschinenversatztage() + delta);

								} else {
									lossollarbeitsplanTemp.setIMaschinenversatztage(delta);
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

		lossollarbeitsplanDto.setTAendern(new Timestamp(System.currentTimeMillis()));

		lossollarbeitsplanDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		// Gesamtzeit berechnen
		LosDto losDto = losFindByPrimaryKey(lossollarbeitsplanDto.getLosIId());

		lossollarbeitsplanDto.setNGesamtzeit(Helper.berechneGesamtzeitInStunden(lossollarbeitsplanDto.getLRuestzeit(),
				lossollarbeitsplanDto.getLStueckzeit(), losDto.getNLosgroesse(), null,
				lossollarbeitsplanDto.getIAufspannung()));

		// speichern
		setLossollarbeitsplanFromLossollarbeitsplanDto(lossollarbeitsplan, lossollarbeitsplanDto);

		// PJ 16396
		if (lossollarbeitsplanDto.getMaschineIId() != null && lossollarbeitsplanDto.getAgartCNr() == null
				&& lossollarbeitsplanDto.getIArbeitsgangnummer() != 0) {

			ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(lossollarbeitsplanDto.getArtikelIIdTaetigkeit(), theClientDto);
			// PJ 16851
			if (Helper.short2boolean(artikelDto.getbReinemannzeit()) == false) {

				Query query = em.createNamedQuery("LossollarbeitsplanfindByLosIIdIArbeitsgangnummer");
				query.setParameter(1, lossollarbeitsplanDto.getLosIId());
				query.setParameter(2, lossollarbeitsplanDto.getIArbeitsgangnummer());
				Collection<?> cl = query.getResultList();
				Iterator it = cl.iterator();
				while (it.hasNext()) {
					Lossollarbeitsplan ap = (Lossollarbeitsplan) it.next();

					if (!ap.getIId().equals(lossollarbeitsplanDto.getIId())) {

						ArtikelDto artikelDtoPos = getArtikelFac()
								.artikelFindByPrimaryKeySmall(ap.getArtikelIIdTaetigkeit(), theClientDto);
						if (Helper.short2boolean(artikelDtoPos.getbReinemannzeit()) == false) {
							ap.setMaschineIId(lossollarbeitsplanDto.getMaschineIId());
						}
					}
				}
			}
		}

		// PJ20837
		if (lossollmaterialIId1 != null) {
			updateLossollmaterial(lossollmaterialFindByPrimaryKey(lossollmaterialIId1), theClientDto);
		}

		if (lossollmaterialIId2 != null && !lossollmaterialIId2.equals(lossollmaterialIId1)) {
			updateLossollmaterial(lossollmaterialFindByPrimaryKey(lossollmaterialIId2), theClientDto);
		}

		// SP9450
		if (lossollarbeitsplanDto.getIMaschinenversatztage() != null) {
			automatischeErmittlungBeginnterminOffset(lossollarbeitsplanDto.getLosIId(), iId, theClientDto);
		}

		return lossollarbeitsplanDto;
	}

	public LossollarbeitsplanDto updateLossollarbeitsplan(LossollarbeitsplanDto lossollarbeitsplanDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		return updateLossollarbeitsplan(lossollarbeitsplanDto, true, theClientDto);
		/*
		 * Integer iId = lossollarbeitsplanDto.getIId(); // try { Lossollarbeitsplan
		 * lossollarbeitsplan = em.find( Lossollarbeitsplan.class, iId); if
		 * (lossollarbeitsplan == null) { throw new
		 * EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, ""); } if
		 * (lossollarbeitsplanDto.getIMaschinenversatztage() != null) {
		 * 
		 * int iVorher = 0;
		 * 
		 * if (lossollarbeitsplan.getIMaschinenversatztage() != null) { iVorher =
		 * lossollarbeitsplan.getIMaschinenversatztage(); }
		 * 
		 * if (iVorher != lossollarbeitsplanDto.getIMaschinenversatztage()) {
		 * 
		 * int delta = lossollarbeitsplanDto.getIMaschinenversatztage() - iVorher;
		 * 
		 * if (delta != 0) { LossollarbeitsplanDto[] saDtos =
		 * lossollarbeitsplanFindByLosIId(lossollarbeitsplanDto .getLosIId());
		 * 
		 * for (int i = 0; i < saDtos.length; i++) { if
		 * (lossollarbeitsplanDto.getIId().equals( saDtos[i].getIId())) {
		 * 
		 * for (int j = i + 1; j < saDtos.length; j++) { Lossollarbeitsplan
		 * lossollarbeitsplanTemp = em .find(Lossollarbeitsplan.class,
		 * saDtos[j].getIId());
		 * 
		 * if (lossollarbeitsplanTemp .getIMaschinenversatztage() != null) {
		 * lossollarbeitsplanTemp .setIMaschinenversatztage(lossollarbeitsplanTemp
		 * .getIMaschinenversatztage() + delta);
		 * 
		 * } else { lossollarbeitsplanTemp .setIMaschinenversatztage(delta); }
		 * em.merge(lossollarbeitsplanTemp); em.flush(); } break; } } } }
		 * 
		 * }
		 * 
		 * lossollarbeitsplanDto.setTAendern(new Timestamp(System
		 * .currentTimeMillis()));
		 * lossollarbeitsplanDto.setPersonalIIdAendern(theClientDto .getIDPersonal());
		 * // Gesamtzeit berechnen LosDto losDto =
		 * losFindByPrimaryKey(lossollarbeitsplanDto.getLosIId());
		 * 
		 * lossollarbeitsplanDto.setNGesamtzeit(Helper .berechneGesamtzeitInStunden(
		 * lossollarbeitsplanDto.getLRuestzeit(),
		 * lossollarbeitsplanDto.getLStueckzeit(), losDto.getNLosgroesse(), null,
		 * lossollarbeitsplanDto.getIAufspannung()));
		 * 
		 * // speichern
		 * setLossollarbeitsplanFromLossollarbeitsplanDto(lossollarbeitsplan,
		 * lossollarbeitsplanDto);
		 * 
		 * // PJ 16396 if (lossollarbeitsplanDto.getMaschineIId() != null &&
		 * lossollarbeitsplanDto.getAgartCNr() == null &&
		 * lossollarbeitsplanDto.getIArbeitsgangnummer() != 0) {
		 * 
		 * ArtikelDto artikelDto = getArtikelFac() .artikelFindByPrimaryKeySmall(
		 * lossollarbeitsplanDto.getArtikelIIdTaetigkeit(), theClientDto); // PJ 16851
		 * if (Helper.short2boolean(artikelDto.getbReinemannzeit()) == false) {
		 * 
		 * Query query = em
		 * .createNamedQuery("LossollarbeitsplanfindByLosIIdIArbeitsgangnummer" );
		 * query.setParameter(1, lossollarbeitsplanDto.getLosIId());
		 * query.setParameter(2, lossollarbeitsplanDto.getIArbeitsgangnummer());
		 * Collection<?> cl = query.getResultList(); Iterator it = cl.iterator(); while
		 * (it.hasNext()) { Lossollarbeitsplan ap = (Lossollarbeitsplan) it.next();
		 * 
		 * if (!ap.getIId().equals(lossollarbeitsplanDto.getIId())) {
		 * 
		 * ArtikelDto artikelDtoPos = getArtikelFac() .artikelFindByPrimaryKeySmall(
		 * ap.getArtikelIIdTaetigkeit(), theClientDto); if
		 * (Helper.short2boolean(artikelDtoPos .getbReinemannzeit()) == false) {
		 * ap.setMaschineIId(lossollarbeitsplanDto .getMaschineIId()); } } } } }
		 * 
		 * return lossollarbeitsplanDto; // } // catch (FinderException ex) { // throw
		 * new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, ex); // }
		 */
	}

	public LossollarbeitsplanDto lossollarbeitsplanFindByPrimaryKey(Integer iId) {
		Validator.pkFieldNotNull(iId, "iId");
		LossollarbeitsplanDto dto = lossollarbeitsplanFindByPrimaryKeyOhneExc(iId);
		if (dto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "" + iId);

		}
		return dto;
	}

	public LossollarbeitsplanDto[] lossollarbeitsplanFindByLosIId(Integer losIId) throws EJBExceptionLP {
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

	public LossollarbeitsplanDto[] lossollarbeitsplanFindByLosIIdArtikelIIdTaetigkeit(Integer losIId,
			Integer artikelIIdTaetigkeit) throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("LossollarbeitsplanfindByLosIIdArtikelIIdTaetigkeit");
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

		Query query = em.createNamedQuery("LossollarbeitsplanfindByLosIIdIArbeitsgangnummerNaechsterHauptarbeitsgang");
		query.setParameter(1, losIId);
		query.setParameter(2, iArbeitsgangnummer);
		Collection<?> cl = query.getResultList();

		if (cl.size() > 0) {
			return assembleLossollarbeitsplanDto((Lossollarbeitsplan) cl.iterator().next());
		} else {
			return null;
		}

	}

	public void toggleMaterialVollstaendig(Integer losIId, TheClientDto theClientDto) {
		Los los = em.find(Los.class, losIId);
		if (los.getTMaterialvollstaendig() == null) {
			los.setTMaterialvollstaendig(new Timestamp(System.currentTimeMillis()));
			los.setPersonalIIdMaterialvollstaendig(theClientDto.getIDPersonal());

		} else {
			los.setPersonalIIdMaterialvollstaendig(null);
			los.setTMaterialvollstaendig(null);

		}
		em.merge(los);
		em.flush();
	}

	public void toggleArbeitsplanFertig(Integer lossollarbeitsplanIId, TheClientDto theClientDto) {
		Lossollarbeitsplan lossollarbeitsplan = em.find(Lossollarbeitsplan.class, lossollarbeitsplanIId);
		if (Helper.short2boolean(lossollarbeitsplan.getBFertig()) == true) {
			lossollarbeitsplan.setBFertig(Helper.boolean2Short(false));
			lossollarbeitsplan.setTFertig(null);
			lossollarbeitsplan.setPersonalIIdFertig(null);

			

		} else {
			lossollarbeitsplan.setBFertig(Helper.boolean2Short(true));
			lossollarbeitsplan.setTFertig(getTimestamp());
			lossollarbeitsplan.setPersonalIIdFertig(theClientDto.getIDPersonal());

			
			try {
				ParametermandantDto parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
						theClientDto.getMandant(), ParameterFac.KATEGORIE_FERTIGUNG,
						ParameterFac.PARAMETER_VORHERIGE_ARBEITSGAENGE_MITERLEDIGEN);

				boolean bVorherigeMiterledigen = (Boolean) parameter.getCWertAsObject();

				if (bVorherigeMiterledigen) {

					Query query = em.createNamedQuery("LossollarbeitsplanfindByLosIId");
					query.setParameter(1, lossollarbeitsplan.getLosIId());
					Collection<?> cl = query.getResultList();
					Iterator it = cl.iterator();
					while (it.hasNext()) {
						Lossollarbeitsplan lossollarbeitsplanZeile = (Lossollarbeitsplan) it.next();

						if (lossollarbeitsplan.getIId() != lossollarbeitsplanZeile.getIId()) {

							if (lossollarbeitsplanZeile.getIArbeitsgangnummer() <= lossollarbeitsplan
									.getIArbeitsgangnummer()) {

								if (lossollarbeitsplanZeile.getIArbeitsgangnummer() < lossollarbeitsplan
										.getIArbeitsgangnummer()) {
									lossollarbeitsplanZeile.setBFertig(Helper.boolean2Short(true));
								}
								
								if (lossollarbeitsplan.getIUnterarbeitsgang() == null
										&& lossollarbeitsplanZeile.getIUnterarbeitsgang() == null) {
									lossollarbeitsplanZeile.setBFertig(Helper.boolean2Short(true));
								}

								if (lossollarbeitsplan.getIUnterarbeitsgang() != null
										&& lossollarbeitsplanZeile.getIUnterarbeitsgang() == null) {
									lossollarbeitsplanZeile.setBFertig(Helper.boolean2Short(true));
								}

								if (lossollarbeitsplan.getIUnterarbeitsgang() != null
										&& lossollarbeitsplanZeile.getIUnterarbeitsgang() != null
										&& lossollarbeitsplanZeile.getIUnterarbeitsgang() <= lossollarbeitsplan
												.getIUnterarbeitsgang()) {
									lossollarbeitsplanZeile.setBFertig(Helper.boolean2Short(true));
								}

							}

						}
					}

				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
			
		}
		em.merge(lossollarbeitsplan);
		em.flush();
	}

	public void setzeVPEtikettGedruckt(Integer losIId, TheClientDto theClientDto) {
		Los los = em.find(Los.class, losIId);
		if (los.getTVpEtikettengedruckt() == null) {
			los.setTVpEtikettengedruckt(new Timestamp(System.currentTimeMillis()));
			los.setPersonalIIdVpEtikettengedruckt(theClientDto.getIDPersonal());

		}
		em.merge(los);
		em.flush();
	}

	private void setLossollarbeitsplanFromLossollarbeitsplanDto(Lossollarbeitsplan lossollarbeitsplan,
			LossollarbeitsplanDto lossollarbeitsplanDto) {
		lossollarbeitsplan.setLosIId(lossollarbeitsplanDto.getLosIId());
		lossollarbeitsplan.setArtikelIIdTaetigkeit(lossollarbeitsplanDto.getArtikelIIdTaetigkeit());
		lossollarbeitsplan.setLRuestzeit(lossollarbeitsplanDto.getLRuestzeit());
		lossollarbeitsplan.setLStueckzeit(lossollarbeitsplanDto.getLStueckzeit());
		lossollarbeitsplan.setNGesamtzeit(lossollarbeitsplanDto.getNGesamtzeit());
		lossollarbeitsplan.setIArbeitsgangnummer(lossollarbeitsplanDto.getIArbeitsgangnummer());
		lossollarbeitsplan.setPersonalIIdAendern(lossollarbeitsplanDto.getPersonalIIdAendern());
		lossollarbeitsplan.setPersonalIIdZugeordneter(lossollarbeitsplanDto.getPersonalIIdZugeordneter());
		lossollarbeitsplan.setTAendern(lossollarbeitsplanDto.getTAendern());
		lossollarbeitsplan.setXText(lossollarbeitsplanDto.getXText());
		lossollarbeitsplan.setCKomentar(lossollarbeitsplanDto.getCKomentar());
		lossollarbeitsplan.setBNachtraeglich(lossollarbeitsplanDto.getBNachtraeglich());
		lossollarbeitsplan.setMaschineIId(lossollarbeitsplanDto.getMaschineIId());
		lossollarbeitsplan.setBFertig(lossollarbeitsplanDto.getBFertig());
		lossollarbeitsplan.setIAufspannung(lossollarbeitsplanDto.getIAufspannung());
		lossollarbeitsplan.setIUnterarbeitsgang(lossollarbeitsplanDto.getIUnterarbeitsgang());
		lossollarbeitsplan.setIMaschinenversatztage(lossollarbeitsplanDto.getIMaschinenversatztage());
		lossollarbeitsplan.setBAutoendebeigeht(lossollarbeitsplanDto.getBAutoendebeigeht());
		lossollarbeitsplan.setBNurmaschinenzeit(lossollarbeitsplanDto.getBNurmaschinenzeit());
		lossollarbeitsplan.setAgartCNr(lossollarbeitsplanDto.getAgartCNr());
		lossollarbeitsplan.setLossollmaterialIId(lossollarbeitsplanDto.getLossollmaterialIId());
		lossollarbeitsplan.setIMaschinenversatzMs(lossollarbeitsplanDto.getIMaschinenversatzMs());
		lossollarbeitsplan.setFFortschritt(lossollarbeitsplanDto.getFFortschritt());
		lossollarbeitsplan.setApkommentarIId(lossollarbeitsplanDto.getApkommentarIId());
		lossollarbeitsplan.setNPpm(lossollarbeitsplanDto.getNPpm());
		lossollarbeitsplan.setPersonalIIdFertig(lossollarbeitsplanDto.getPersonalIIdFertig());
		lossollarbeitsplan.setTFertig(lossollarbeitsplanDto.getTFertig());
		lossollarbeitsplan
				.setIMaschinenversatztageAusStueckliste(lossollarbeitsplanDto.getIMaschinenversatztageAusStueckliste());
		lossollarbeitsplan.setIReihung(lossollarbeitsplanDto.getIReihung());

		lossollarbeitsplan.setTAgbeginnBerechnet(lossollarbeitsplanDto.getTAgbeginnBerechnet());

		em.merge(lossollarbeitsplan);
		em.flush();
	}

	private LossollarbeitsplanDto assembleLossollarbeitsplanDto(Lossollarbeitsplan lossollarbeitsplan) {
		return LossollarbeitsplanDtoAssembler.createDto(lossollarbeitsplan);
	}

	private List<LossollarbeitsplanDto> assembleLossollarbeitsplanDtosAsList(Collection<?> lossollarbeitsplans) {
		List<LossollarbeitsplanDto> list = new ArrayList<LossollarbeitsplanDto>();
		if (lossollarbeitsplans != null) {
			Iterator<?> iterator = lossollarbeitsplans.iterator();
			while (iterator.hasNext()) {
				Lossollarbeitsplan lossollarbeitsplan = (Lossollarbeitsplan) iterator.next();
				list.add(assembleLossollarbeitsplanDto(lossollarbeitsplan));
			}
		}
		return list;
	}

	private LossollarbeitsplanDto[] assembleLossollarbeitsplanDtos(Collection<?> lossollarbeitsplans) {
		List<LossollarbeitsplanDto> list = assembleLossollarbeitsplanDtosAsList(lossollarbeitsplans);
		return (LossollarbeitsplanDto[]) list.toArray(new LossollarbeitsplanDto[list.size()]);
	}

	public LoslagerentnahmeDto createLoslagerentnahme(LoslagerentnahmeDto loslagerentnahmeDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(loslagerentnahmeDto);
		// begin
		loslagerentnahmeDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		// primary key
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_LOSSOLLMATERIAL);
		loslagerentnahmeDto.setIId(iId);

		Integer i = null;
		try {
			Query querynext = em.createNamedQuery("LoslagerentnahmeejbSelectNextReihung");
			querynext.setParameter(1, loslagerentnahmeDto.getLosIId());
			i = (Integer) querynext.getSingleResult();

			if (i == null) {
				i = new Integer(0);
			}
			i = new Integer(i.intValue() + 1);

		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		}
		loslagerentnahmeDto.setISort(i);

		try {
			Loslagerentnahme loslagerentnahme = new Loslagerentnahme(loslagerentnahmeDto.getIId(),
					loslagerentnahmeDto.getLosIId(), loslagerentnahmeDto.getLagerIId(), loslagerentnahmeDto.getISort(),
					loslagerentnahmeDto.getPersonalIIdAendern());
			em.persist(loslagerentnahme);
			em.flush();
			loslagerentnahmeDto.setTAendern(loslagerentnahme.getTAendern());
			setLoslagerentnahmeFromLoslagerentnahmeDto(loslagerentnahme, loslagerentnahmeDto);
			return loslagerentnahmeDto;
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeLoslagerentnahme(LoslagerentnahmeDto loslagerentnahmeDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

		Query query = em.createNamedQuery("LoslagerentnahmefindByLosIId");
		query.setParameter(1, loslagerentnahmeDto.getLosIId());
		Collection<?> c = query.getResultList();

		Iterator it = c.iterator();
		int iSort = 1;
		while (it.hasNext()) {

			Loslagerentnahme losentnahme = (Loslagerentnahme) it.next();

			if (loslagerentnahmeDto != null && losentnahme.getIId().equals(loslagerentnahmeDto.getIId())) {
				em.remove(losentnahme);
				em.flush();
			} else {
				losentnahme.setISort(iSort);
				iSort++;
			}

		}

	}

	public LoslagerentnahmeDto updateLoslagerentnahme(LoslagerentnahmeDto loslagerentnahmeDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Integer iId = loslagerentnahmeDto.getIId();
		// try {
		Loslagerentnahme loslagerentnahme = em.find(Loslagerentnahme.class, iId);
		if (loslagerentnahme == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}
		setLoslagerentnahmeFromLoslagerentnahmeDto(loslagerentnahme, loslagerentnahmeDto);
		return loslagerentnahmeDto;
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, ex);
		// }
	}

	public LoslagerentnahmeDto loslagerentnahmeFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		// try {
		Loslagerentnahme loslagerentnahme = em.find(Loslagerentnahme.class, iId);
		if (loslagerentnahme == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLoslagerentnahmeDto(loslagerentnahme);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public LoslagerentnahmeDto[] loslagerentnahmeFindByLosIId(Integer losIId) throws EJBExceptionLP {
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

	private void setLoslagerentnahmeFromLoslagerentnahmeDto(Loslagerentnahme loslagerentnahme,
			LoslagerentnahmeDto loslagerentnahmeDto) {
		loslagerentnahme.setLosIId(loslagerentnahmeDto.getLosIId());
		loslagerentnahme.setLagerIId(loslagerentnahmeDto.getLagerIId());
		loslagerentnahme.setISort(loslagerentnahmeDto.getISort());
		loslagerentnahme.setTAendern(loslagerentnahmeDto.getTAendern());
		loslagerentnahme.setPersonalIIdAendern(loslagerentnahmeDto.getPersonalIIdAendern());
		em.merge(loslagerentnahme);
		em.flush();
	}

	private LoslagerentnahmeDto assembleLoslagerentnahmeDto(Loslagerentnahme loslagerentnahme) {
		return LoslagerentnahmeDtoAssembler.createDto(loslagerentnahme);
	}

	private LoslagerentnahmeDto[] assembleLoslagerentnahmeDtos(Collection<?> loslagerentnahmes) {
		List<LoslagerentnahmeDto> list = new ArrayList<LoslagerentnahmeDto>();
		if (loslagerentnahmes != null) {
			Iterator<?> iterator = loslagerentnahmes.iterator();
			while (iterator.hasNext()) {
				Loslagerentnahme loslagerentnahme = (Loslagerentnahme) iterator.next();
				list.add(assembleLoslagerentnahmeDto(loslagerentnahme));
			}
		}
		LoslagerentnahmeDto[] returnArray = new LoslagerentnahmeDto[list.size()];
		return (LoslagerentnahmeDto[]) list.toArray(returnArray);
	}

	public BigDecimal wievileTOPSArtikelWurdenBereitsZugebucht(Integer losIId, TheClientDto theClientDto) {
		LossollmaterialDto[] dtos = lossollmaterialFindByLosIId(losIId);

		LosDto losDto = losFindByPrimaryKey(losIId);

		for (int i = 0; i < dtos.length; i++) {
			Integer artklaIId = null;

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(dtos[i].getArtikelIId(), theClientDto);
			artklaIId = artikelDto.getArtklaIId();

			if (artklaIId != null) {
				boolean bTops = Helper
						.short2boolean(getArtikelFac().artklaFindByPrimaryKey(artklaIId, theClientDto).getBTops());

				if (bTops == true) {

					BigDecimal sollsatzgroesse = dtos[i].getNMenge().divide(losDto.getNLosgroesse(), 4,
							BigDecimal.ROUND_HALF_EVEN);
					BigDecimal ausgegeben = getAusgegebeneMenge(dtos[i].getIId(), null, theClientDto);
					return ausgegeben.divide(sollsatzgroesse, 4, BigDecimal.ROUND_HALF_EVEN);
				}
			}
		}

		return new BigDecimal(0);
	}

	public void verknuepfungZuBestellpositionUndArbeitsplanLoeschen(Integer lossollmaterialIId,
			boolean inklusiveVerknuepungZuOriginalartikel) {
		Query queryBestpos = em.createNamedQuery("BestellpositionfindByLossollmaterialIId");
		queryBestpos.setParameter(1, lossollmaterialIId);
		Collection<?> cl = queryBestpos.getResultList();

		Iterator itBest = cl.iterator();
		while (itBest.hasNext()) {
			Bestellposition b = (Bestellposition) itBest.next();
			b.setLossollmaterialIId(null);
		}

		Query queryAP = em.createNamedQuery("LossollarbeitsplanfindByLossollmaterialIId");
		queryAP.setParameter(1, lossollmaterialIId);
		Collection<?> clAP = queryAP.getResultList();

		Iterator itAP = clAP.iterator();
		while (itAP.hasNext()) {
			Lossollarbeitsplan b = (Lossollarbeitsplan) itAP.next();
			b.setLossollmaterialIId(null);
		}

		// SP7640
		Query queryAF = em.createNamedQuery("AnfragepositionfindByLossollmaterialIId");
		queryAF.setParameter(1, lossollmaterialIId);
		Collection<?> clAF = queryAF.getResultList();

		Iterator itAF = clAF.iterator();
		while (itAF.hasNext()) {
			Anfrageposition b = (Anfrageposition) itAF.next();
			b.setLossollmaterialIId(null);
		}

		// SP9534

		if (inklusiveVerknuepungZuOriginalartikel) {
			Query queryORI = em.createNamedQuery("LossollmaterialfindByLossollmaterialIIdOriginal");
			queryORI.setParameter(1, lossollmaterialIId);
			Collection<?> clORI = queryORI.getResultList();

			Iterator itORI = clORI.iterator();
			while (itORI.hasNext()) {
				Lossollmaterial b = (Lossollmaterial) itORI.next();
				b.setLossollmaterialIIdOriginal(null);
			}
		}

	}

	// PJ17321
	public String verknuepfungZuBestellpositionUndArbeitsplanDarstellen(Integer lossollmaterialIId,
			TheClientDto theClientDto) {
		byte[] CRLFAscii = { 13, 10 };
		String s = "";
		Query queryBestpos = em.createNamedQuery("BestellpositionfindByLossollmaterialIId");
		queryBestpos.setParameter(1, lossollmaterialIId);
		Collection<?> cl = queryBestpos.getResultList();

		Iterator itBest = cl.iterator();
		while (itBest.hasNext()) {
			Bestellposition b = (Bestellposition) itBest.next();

			Bestellung bestellung = em.find(Bestellung.class, b.getBestellungIId());

			s += LocaleFac.BELEGART_BESTELLUNG.trim() + " " + bestellung.getCNr() + ", PosNr: "
					+ getBestellpositionFac().getPositionNummer(b.getIId(), theClientDto) + new String(CRLFAscii);

		}

		Query queryAP = em.createNamedQuery("LossollarbeitsplanfindByLossollmaterialIId");
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

	private TreeMap<String, Object[]> add2TreeMap(TreeMap<String, Object[]> tm, String key, Object[] zeile) {

		if (tm.containsKey(key)) {
			Object[] zeileVorhanden = tm.get(key);
			BigDecimal bdMenge = (BigDecimal) zeileVorhanden[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_AUSGABEMENGE];
			bdMenge = bdMenge.add(
					(BigDecimal) zeile[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_AUSGABEMENGE]);
			zeileVorhanden[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_AUSGABEMENGE] = bdMenge;

			BigDecimal bdSollMenge = (BigDecimal) zeileVorhanden[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_SOLLMENGE];
			bdSollMenge = bdSollMenge.add(
					(BigDecimal) zeile[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_SOLLMENGE]);
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
	public ArrayList<Integer> erledigteLoseImZeitraumNachkalkulieren(java.sql.Date tVon, java.sql.Date tBis,
			TheClientDto theClientDto) {
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
			FLRLosablieferung los = (FLRLosablieferung) resultListIterator.next();

			if (!bereitsNachkalkuliert.containsKey(los.getLos_i_id())) {
				try {
					getFertigungFac().aktualisiereNachtraeglichPreiseAllerLosablieferungen(los.getLos_i_id(),
							theClientDto, false);
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}
			bereitsNachkalkuliert.put(los.getLos_i_id(), "");

		}

		return al;
	}

	@org.jboss.ejb3.annotation.TransactionTimeout(5000)
	public TreeMap<String, Object[]> aktualisiereLoseAusStueckliste(Integer stuecklisteIId,
			boolean mitAusgegebenUndInProduktion, TheClientDto theClientDto) {

		TreeMap<String, Object[]> tmAktualisierteLose = new TreeMap<String, Object[]>();

		ParametermandantDto parameter = null;
		try {
			parameter = getParameterFac().getMandantparameter(theClientDto.getSMandantenwaehrung(),
					ParameterFac.KATEGORIE_STUECKLISTE,
					ParameterFac.PARAMETER_BEI_LOS_AKTUALISIERUNG_MATERIAL_NACHBUCHEN);
		} catch (RemoteException e1) {
			throwEJBExceptionLPRespectOld(e1);
		}
		boolean bMaterialNachbuchen = (Boolean) parameter.getCWertAsObject();

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT l FROM FLRLosReport l WHERE l.stueckliste_i_id IS NOT NULL AND l.mandant_c_nr='"
				+ theClientDto.getMandant() + "' ";

		if (mitAusgegebenUndInProduktion) {
			queryString += "AND l.status_c_nr IN ('" + LocaleFac.STATUS_ANGELEGT + "','" + LocaleFac.STATUS_AUSGEGEBEN
					+ "','" + LocaleFac.STATUS_IN_PRODUKTION + "') ";
		} else {
			queryString += "AND l.status_c_nr IN ('" + LocaleFac.STATUS_ANGELEGT + "') ";
		}

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
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_MATERIAL_VOLLSTAENDIG, al,
						new Exception("FEHLER_FERTIGUNG_MATERIAL_VOLLSTAENDIG" + " Los:" + losDto.getCNr()));
			}

			if (los.getStatus_c_nr().equals(LocaleFac.STATUS_ANGELEGT)) {
				aktualisiereSollArbeitsplanAusStueckliste(los.getI_id(), theClientDto);
				aktualisiereSollMaterialAusStueckliste(los.getI_id(), theClientDto);

				// SP8581
				Object[] oZeileReport = new Object[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_ANZAHL_SPALTEN];
				oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_LOSNUMMER] = los.getC_nr();
				tmAktualisierteLose = add2TreeMap(tmAktualisierteLose, los.getC_nr(), oZeileReport);
			} else {

				LoslagerentnahmeDto[] laeger = loslagerentnahmeFindByLosIId(los.getI_id());

				aktualisiereSollArbeitsplanAusStueckliste(los.getI_id(), theClientDto);

				// Alle stuecklistenpositionen holen + Hilfsstueckliste und dann
				// verdichten
				HashMap<Integer, StuecklistepositionDto> hmStuecklistenposition = getFertigungFac()
						.holeAlleLossollmaterialFuerStuecklistenAktualisierung(los.getStueckliste_i_id(),
								los.getN_losgroesse(), 0, null, theClientDto);

				LossollmaterialDto[] sollmatDtos = lossollmaterialFindByLosIId(los.getI_id());

				for (int i = 0; i < sollmatDtos.length; i++) {
					try {
						if (!Helper.short2boolean(sollmatDtos[i].getBNachtraeglich())) {
							// War vor SP2000 --> &&
							// sollmatDtos[i].getNMenge().doubleValue() > 0

							LosistmaterialDto[] istmaterialDtos = losistmaterialFindByLossollmaterialIId(
									sollmatDtos[i].getIId());
							BigDecimal bdAusgegeben = getAusgegebeneMenge(sollmatDtos[i].getIId(), null, theClientDto);
							ArtikelDto artikelDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(sollmatDtos[i].getArtikelIId(), theClientDto);

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

							if (hmStuecklistenposition.containsKey(sollmatDtos[i].getArtikelIId())) {

								// Mengen abziehen
								StuecklistepositionDto stklPos = hmStuecklistenposition
										.get(sollmatDtos[i].getArtikelIId());

								sollmatDtos[i].setNMenge(stklPos.getNMenge());

								Lossollmaterial sollmatBean = em.find(Lossollmaterial.class, sollmatDtos[i].getIId());

								BigDecimal diffSollmenge = stklPos.getNMenge().subtract(sollmatBean.getNMenge());

								oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_SOLLMENGE] = diffSollmenge;

								sollmatBean.setNMenge(stklPos.getNMenge());
								sollmatBean.setIBeginnterminoffset(stklPos.getIBeginnterminoffset());

								BigDecimal diff = bdAusgegeben.subtract(sollmatDtos[i].getNMenge());

								if (diff.doubleValue() == 0 && diffSollmenge.doubleValue() != 0) {
									oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_AUSGABEMENGE] = new BigDecimal(
											0);
									tmAktualisierteLose = add2TreeMap(tmAktualisierteLose,
											los.getC_nr() + artikelDto.getCNr(), oZeileReport);

								} else {

									if (bMaterialNachbuchen == true || (bMaterialNachbuchen == false
											&& Helper.short2boolean(artikelDto.getBLagerbewirtschaftet()) == false)) {

										if (Helper.short2boolean(artikelDto.getBSeriennrtragend())
												|| Helper.short2boolean(artikelDto.getBChargennrtragend())) {
											sollmatDtos[i] = null;

											oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_AUSGABEMENGE] = new BigDecimal(
													0);
											oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_BEMERKUNG] = "Der Artikel ist SNR/CNR behaftet und wurde nicht ber\u00FCcksichtigt.";

											tmAktualisierteLose = add2TreeMap(tmAktualisierteLose,
													los.getC_nr() + artikelDto.getCNr(), oZeileReport);

											// SP3741
											hmStuecklistenposition.remove(artikelDto.getIId());

											continue;
										}

										if (diff.doubleValue() > 0) {

											for (int j = 0; j < istmaterialDtos.length; j++) {
												if (diff.doubleValue() > 0) {
													BigDecimal istmenge = istmaterialDtos[j].getNMenge();

													BigDecimal bdMengeNeu = null;

													if (diff.doubleValue() > istmenge.doubleValue()) {
														bdMengeNeu = new BigDecimal(0);
														diff = diff.subtract(istmenge);
													} else {
														bdMengeNeu = istmenge.subtract(diff);
														diff = new BigDecimal(0);
													}

													updateLosistmaterialMenge(istmaterialDtos[j].getIId(), bdMengeNeu,
															theClientDto);
												}
											}

											BigDecimal bdAusgegebenNachher = getAusgegebeneMenge(
													sollmatDtos[i].getIId(), null, theClientDto);

											oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_AUSGABEMENGE] = bdAusgegebenNachher
													.subtract(bdAusgegeben);

											tmAktualisierteLose = add2TreeMap(tmAktualisierteLose,
													los.getC_nr() + artikelDto.getCNr(), oZeileReport);
										} else {
											BigDecimal bdAbzubuchendeMenge = diff.abs();
											for (int j = 0; j < laeger.length; j++) {
												// wenn noch was abzubuchen ist
												// (Menge >
												// 0)
												if (bdAbzubuchendeMenge.compareTo(new BigDecimal(0)) == 1) {
													BigDecimal bdLagerstand = null;
													if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet())) {

														bdLagerstand = getLagerFac().getLagerstand(artikelDto.getIId(),
																laeger[j].getLagerIId(), theClientDto);

													} else {
														bdLagerstand = new BigDecimal(999999999);
													}
													// wenn ein lagerstand da
													// ist
													if (bdLagerstand.compareTo(new BigDecimal(0)) == 1) {
														BigDecimal bdMengeVonLager;
														if (bdLagerstand.compareTo(bdAbzubuchendeMenge) == 1) {
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
														istmat.setLagerIId(laeger[j].getLagerIId());
														istmat.setLossollmaterialIId(sollmatDtos[i].getIId());
														istmat.setNMenge(bdMengeVonLager);

														if (sollmatDtos[i].getNMenge().doubleValue() > 0) {
															istmat.setBAbgang(Helper.boolean2Short(true));
														} else {
															istmat.setBAbgang(Helper.boolean2Short(false));
														}

														// ist-wert anlegen und
														// lagerbuchung
														// durchfuehren
														createLosistmaterial(istmat, null, theClientDto);
														// menge reduzieren
														bdAbzubuchendeMenge = bdAbzubuchendeMenge
																.subtract(bdMengeVonLager);
														oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_AUSGABEMENGE] = bdMengeVonLager;

														tmAktualisierteLose = add2TreeMap(tmAktualisierteLose,
																los.getC_nr() + artikelDto.getCNr(), oZeileReport);

													} else {
														oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_AUSGABEMENGE] = new BigDecimal(
																0);
														tmAktualisierteLose = add2TreeMap(tmAktualisierteLose,
																los.getC_nr() + artikelDto.getCNr(), oZeileReport);
													}
												}
											}
										}
									}
								}
								hmStuecklistenposition.remove(sollmatDtos[i].getArtikelIId());
								getFehlmengeFac().aktualisiereFehlmenge(LocaleFac.BELEGART_LOS, sollmatDtos[i].getIId(),
										theClientDto);
							} else {

								// Los-Sollmaterial loeschen

								verknuepfungZuBestellpositionUndArbeitsplanLoeschen(sollmatDtos[i].getIId(), false);

								oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_SOLLMENGE] = sollmatDtos[i]
										.getNMenge().multiply(new BigDecimal(-1));

								if (bMaterialNachbuchen == true || istmaterialDtos.length == 0
										|| (bMaterialNachbuchen == false && Helper
												.short2boolean(artikelDto.getBLagerbewirtschaftet()) == false)) {
									for (int j = 0; j < istmaterialDtos.length; j++) {
										removeLosistmaterial(istmaterialDtos[j], theClientDto);
									}

									removeLossollmaterial(sollmatDtos[i], theClientDto);

									oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_AUSGABEMENGE] = bdAusgegeben
											.multiply(new BigDecimal(-1));
									tmAktualisierteLose = add2TreeMap(tmAktualisierteLose,
											los.getC_nr() + artikelDto.getCNr(), oZeileReport);

								} else {
									sollmatDtos[i].setNMenge(new BigDecimal(0));
									updateLossollmaterial(sollmatDtos[i], theClientDto);
									oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_AUSGABEMENGE] = new BigDecimal(
											0);
									tmAktualisierteLose = add2TreeMap(tmAktualisierteLose,
											los.getC_nr() + artikelDto.getCNr(), oZeileReport);
								}

								getFehlmengeFac().aktualisiereFehlmenge(LocaleFac.BELEGART_LOS, sollmatDtos[i].getIId(),
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
					if (sollmatDtos[i] != null && !Helper.short2boolean(sollmatDtos[i].getBNachtraeglich())
							&& sollmatDtos[i].getNMenge().doubleValue() == 0) {

						// Los-Sollmaterial loeschen
						LosistmaterialDto[] istmaterialDtos = losistmaterialFindByLossollmaterialIId(
								sollmatDtos[i].getIId());

						boolean bAlleIstMaterialSindNull = true;

						for (int z = 0; z < istmaterialDtos.length; z++) {
							if (istmaterialDtos[z].getNMenge().doubleValue() != 0) {
								bAlleIstMaterialSindNull = false;
							}
						}
						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(sollmatDtos[i].getArtikelIId(), theClientDto);
						if (bMaterialNachbuchen == true || bAlleIstMaterialSindNull == true
								|| (bMaterialNachbuchen == false
										&& Helper.short2boolean(artikelDto.getBLagerbewirtschaftet()) == false)) {

							BigDecimal bdAusgegeben = getAusgegebeneMenge(sollmatDtos[i].getIId(), null, theClientDto);

							for (int j = 0; j < istmaterialDtos.length; j++) {
								removeLosistmaterial(istmaterialDtos[j], theClientDto);
							}

							verknuepfungZuBestellpositionUndArbeitsplanLoeschen(sollmatDtos[i].getIId(), false);
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

								tmAktualisierteLose = add2TreeMap(tmAktualisierteLose,
										los.getC_nr() + artikelDto.getCNr(), oZeileReport);

							}
						}

					}
				}

				// Alle noch nicht verbrauchten neu eintragen

				Iterator it = hmStuecklistenposition.keySet().iterator();
				while (it.hasNext()) {

					try {
						LagerDto lagerDtoMandant = getLagerFac().getHauptlagerDesMandanten(theClientDto);
						Integer artikelIId = (Integer) it.next();

						StuecklistepositionDto stklPos = hmStuecklistenposition.get(artikelIId);
						BigDecimal bdAbzubuchendeMenge = stklPos.getNMenge();

						ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(stklPos.getArtikelIId(),
								theClientDto);

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
						losMatDto.setBNachtraeglich(Helper.boolean2Short(false));
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
						losMatDto.setNMengeStklPos(stklPos.getNMenge());
						losMatDto.setEinheitCNrStklPos(stklPos.getEinheitCNr());

						BigDecimal bdSollpreis = getLagerFac().getGemittelterGestehungspreisEinesLagers(
								stklPos.getArtikelIId(), lagerDtoMandant.getIId(), theClientDto);
						losMatDto.setNSollpreis(bdSollpreis);

						Integer sollmatIId = createLossollmaterial(losMatDto, theClientDto).getIId();
						if (bMaterialNachbuchen == true || (bMaterialNachbuchen == false
								&& Helper.short2boolean(artikelDto.getBLagerbewirtschaftet()) == false)) {

							if (Helper.short2boolean(artikelDto.getBSeriennrtragend())
									|| Helper.short2boolean(artikelDto.getBChargennrtragend())) {
								oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_BEMERKUNG] = "Der Artikel ist SNR/CNR behaftet und wurde nicht ber\u00FCcksichtigt.";

								tmAktualisierteLose.put(los.getC_nr() + artikelDto.getCNr(), oZeileReport);
								continue;
							}

							for (int j = 0; j < laeger.length; j++) {
								// wenn noch was abzubuchen ist
								// (Menge >
								// 0)
								if (bdAbzubuchendeMenge.compareTo(new BigDecimal(0)) == 1) {
									BigDecimal bdLagerstand = null;
									if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet())) {

										bdLagerstand = getLagerFac().getLagerstand(artikelDto.getIId(),
												laeger[j].getLagerIId(), theClientDto);

									} else {
										bdLagerstand = new BigDecimal(999999999);
									}
									// wenn ein lagerstand da ist
									if (bdLagerstand.compareTo(new BigDecimal(0)) == 1) {
										BigDecimal bdMengeVonLager;
										if (bdLagerstand.compareTo(bdAbzubuchendeMenge) == 1) {
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
										istmat.setLagerIId(laeger[j].getLagerIId());
										istmat.setLossollmaterialIId(sollmatIId);
										istmat.setNMenge(bdMengeVonLager);

										if (stklPos.getNMenge().doubleValue() > 0) {
											istmat.setBAbgang(Helper.boolean2Short(true));
										} else {
											istmat.setBAbgang(Helper.boolean2Short(false));
										}

										// ist-wert anlegen und
										// lagerbuchung
										// durchfuehren
										createLosistmaterial(istmat, null, theClientDto);
										// menge reduzieren
										bdAbzubuchendeMenge = bdAbzubuchendeMenge.subtract(bdMengeVonLager);

										oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_AUSGABEMENGE] = bdMengeVonLager;
										tmAktualisierteLose = add2TreeMap(tmAktualisierteLose,
												los.getC_nr() + artikelDto.getCNr(), oZeileReport);

									}
								}
							}
						} else {
							oZeileReport[StuecklisteReportFac.REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_AUSGABEMENGE] = new BigDecimal(
									0);
							tmAktualisierteLose = add2TreeMap(tmAktualisierteLose, los.getC_nr() + artikelDto.getCNr(),
									oZeileReport);
						}

						getFehlmengeFac().aktualisiereFehlmenge(LocaleFac.BELEGART_LOS, losMatDto.getIId(),
								theClientDto);

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

	public void bucheTraceImport(ArrayList<TraceImportDto> alZuBuchen, TheClientDto theClientDto) {

	}

	public void bucheTOPSArtikelAufHauptLager(Integer losIId, TheClientDto theClientDto,
			BigDecimal zuzubuchendeSatzgroesse) {
		LossollmaterialDto[] dtos = lossollmaterialFindByLosIId(losIId);
		try {
			Integer hauptlagerIId = getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId();

			LosDto losDto = losFindByPrimaryKey(losIId);

			for (int i = 0; i < dtos.length; i++) {
				Integer artklaIId = null;

				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(dtos[i].getArtikelIId(),
						theClientDto);
				artklaIId = artikelDto.getArtklaIId();

				if (artklaIId != null) {
					boolean bTops = Helper
							.short2boolean(getArtikelFac().artklaFindByPrimaryKey(artklaIId, theClientDto).getBTops());

					if (bTops == true) {

						BigDecimal sollsatzgroesse = dtos[i].getNMenge().divide(losDto.getNLosgroesse(), 4,
								BigDecimal.ROUND_HALF_EVEN);
						BigDecimal zuzubuchendeMenge = null;

						if (zuzubuchendeSatzgroesse == null) {
							zuzubuchendeMenge = dtos[i].getNMenge()
									.subtract(getAusgegebeneMenge(dtos[i].getIId(), null, theClientDto));
						} else {
							zuzubuchendeMenge = sollsatzgroesse.multiply(zuzubuchendeSatzgroesse);
						}

						if (zuzubuchendeMenge.doubleValue() > 0) {

							HandlagerbewegungDto handDto = new HandlagerbewegungDto();
							handDto.setArtikelIId(dtos[i].getArtikelIId());
							handDto.setNMenge(zuzubuchendeMenge);
							handDto.setBAbgang(Helper.boolean2Short(false));
							handDto.setCKommentar("TOPS " + losDto.getCNr());
							handDto.setLagerIId(hauptlagerIId);
							handDto.setNEinstandspreis(getLagerFac().getGemittelterGestehungspreisDesHauptlagers(
									dtos[i].getArtikelIId(), theClientDto));
							getLagerFac().createHandlagerbewegung(handDto, theClientDto);
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
							getFehlmengeFac().aktualisiereFehlmenge(LocaleFac.BELEGART_LOS, dtos[i].getIId(),
									theClientDto);

						}
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public void aktualisiereSollMaterialAusStueckliste(Integer losIId, TheClientDto theClientDto)
			throws EJBExceptionLP {

		LosDto losDto = losFindByPrimaryKey(losIId);
		if (losDto.getStuecklisteIId() != null) {

			aktualisiereSollMaterialAusStueckliste(losIId, theClientDto, true, true);
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DARF_FUER_MATERIALLISTE_NICHT_DURCHGEFUEHRT_WERDEN,
					"");
		}

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public HashMap<Integer, StuecklistepositionDto> holeAlleLossollmaterialFuerStuecklistenAktualisierung(
			Integer stuecklisteIId, BigDecimal bdLosgroesse, int iEbene,
			HashMap<Integer, StuecklistepositionDto> hmPositionen, TheClientDto theClientDto) {
		iEbene++;

		if (hmPositionen == null) {
			hmPositionen = new HashMap<Integer, StuecklistepositionDto>();
		}
		try {
			StuecklistepositionDto[] stkPos = getStuecklisteFac()
					.stuecklistepositionFindByStuecklisteIId(stuecklisteIId, theClientDto);

			StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(stuecklisteIId, theClientDto);

			for (int i = 0; i < stkPos.length; i++) {
				// alle stuecklistenpositionen ins los uebernehmen

				// Einheit umrechnen
				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(stkPos[i].getArtikelIId(),
						theClientDto);

				BigDecimal bdFaktor = getSystemFac().rechneUmInAndereEinheit(new BigDecimal(1),
						stkPos[i].getEinheitCNr(), artikelDto.getEinheitCNr(), stkPos[i].getIId(), theClientDto);

				// nun die Dimensionen
				BigDecimal bdDimProdukt = new BigDecimal(1);
				EinheitDto einheitDto = getSystemFac().einheitFindByPrimaryKey(stkPos[i].getEinheitCNr(), theClientDto);
				if (einheitDto.getIDimension().intValue() >= 1) {
					if (stkPos[i].getFDimension1() != null) {
						bdDimProdukt = bdDimProdukt.multiply(new BigDecimal(stkPos[i].getFDimension1().floatValue()));
					}
				}
				if (einheitDto.getIDimension().intValue() >= 2) {
					if (stkPos[i].getFDimension2() != null) {
						bdDimProdukt = bdDimProdukt.multiply(new BigDecimal(stkPos[i].getFDimension2().floatValue()));
					}
				}
				if (einheitDto.getIDimension().intValue() >= 3) {
					if (stkPos[i].getFDimension3() != null) {
						bdDimProdukt = bdDimProdukt.multiply(new BigDecimal(stkPos[i].getFDimension3().floatValue()));
					}
				}
				// verschnitt
				BigDecimal bdMenge = Helper.berechneMengeInklusiveVerschnitt(stkPos[i].getNMenge(),
						artikelDto.getFVerschnittfaktor(), artikelDto.getFVerschnittbasis(), bdLosgroesse,
						artikelDto.getFFertigungsVpe());

				// endgueltige Menge berechnen
				BigDecimal posMenge = null;
				if (Helper.short2boolean(stkPos[i].getBRuestmenge())) {
					posMenge = bdMenge.multiply(bdDimProdukt).multiply(bdFaktor).divide(
							new BigDecimal(stklDto.getNErfassungsfaktor().doubleValue()), 12,
							BigDecimal.ROUND_HALF_EVEN);

				} else {
					posMenge = bdMenge.multiply(bdDimProdukt).multiply(bdLosgroesse).multiply(bdFaktor).divide(
							new BigDecimal(stklDto.getNErfassungsfaktor().doubleValue()), 12,
							BigDecimal.ROUND_HALF_EVEN);

				}

				if (posMenge.doubleValue() < 0.001 && posMenge.doubleValue() > 0.000001) {
					posMenge = new BigDecimal("0.001");
					posMenge = posMenge.setScale(3, BigDecimal.ROUND_HALF_EVEN);
				} else {
					posMenge = posMenge.setScale(3, BigDecimal.ROUND_HALF_EVEN);
				}

				if (artikelDto.getNVerschnittmenge() != null) {
					posMenge = posMenge.add(artikelDto.getNVerschnittmenge());
				}

				stkPos[i].setNMenge(posMenge);

				StuecklisteDto stuecklisteDto = getStuecklisteFac()
						.stuecklisteFindByMandantCNrArtikelIIdOhneExc(stkPos[i].getArtikelIId(), theClientDto);

				if (stuecklisteDto != null && stuecklisteDto.getStuecklisteartCNr()
						.equals(StuecklisteFac.STUECKLISTEART_HILFSSTUECKLISTE)) {
					if (iEbene < 10) {
						holeAlleLossollmaterialFuerStuecklistenAktualisierung(stuecklisteDto.getIId(), posMenge, iEbene,
								hmPositionen, theClientDto);
					}

				} else {

					if (stkPos[i].getNMenge().doubleValue() > 0) {

						if (hmPositionen.containsKey(stkPos[i].getArtikelIId())) {

							StuecklistepositionDto p = hmPositionen.get(stkPos[i].getArtikelIId());
							p.setNMenge(stkPos[i].getNMenge().add(p.getNMenge()));
							hmPositionen.put(stkPos[i].getArtikelIId(), p);
						} else {
							hmPositionen.put(stkPos[i].getArtikelIId(), stkPos[i]);
						}
					}

				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

		return hmPositionen;

	}

	public void sollpreiseAllerSollmaterialpositionenNeuKalkulieren(Integer losIId, TheClientDto theClientDto) {

		try {
			LossollmaterialDto[] sollDtos = lossollmaterialFindByLosIId(losIId);

			LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);

			for (int i = 0; i < sollDtos.length; i++) {

				LossollmaterialDto sollDto = sollDtos[i];
				sollDto.setNSollpreis(BigDecimal.ZERO);
				StuecklistepositionDto[] stklPosDtos = null;
				if (losDto.getStuecklisteIId() != null) {
					stklPosDtos = getStuecklisteFac().stuecklistepositionFindByStuecklisteIIdArtikelIId(
							losDto.getStuecklisteIId(), sollDto.getArtikelIId(), theClientDto);
				}

				if (stklPosDtos != null && stklPosDtos.length > 0 && stklPosDtos[0].getNKalkpreis() != null) {
					sollDto.setNSollpreis(stklPosDtos[0].getNKalkpreis());
				} else {
					sollDto = holeSollpreisAusLief1(theClientDto, sollDto);
				}

				updateLossollmaterial(sollDto, theClientDto);

			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

	}

	private BigDecimal berechneMengeVerschnittNachAbmessung(BigDecimal bdMenge, BigDecimal nLosgroesse,
			ArtikelDto artikel, EinheitDto einheit, StuecklistepositionDto stklPosition, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		GeometrieDto geometrie = artikel.getGeometrieDto();

		if (geometrie == null)
			return bdMenge;

		int dimension = einheit.getIDimension();

		Float dimension1 = stklPosition.getFDimension1();
		Float dimension2 = stklPosition.getFDimension2();

		Double fBreite = geometrie.getFBreite();
		Double fHoehe = geometrie.getFHoehe();
		if (dimension == 1 && fBreite != null && fBreite.doubleValue() > 0 && fHoehe != null
				&& fHoehe.doubleValue() > 0) {
			BigDecimal mengeDim1Umgerechnet = getSystemFac().rechneUmInAndereEinheit(new BigDecimal(dimension1),
					artikel.getEinheitCNr(), stklPosition.getEinheitCNr(), stklPosition.getIId(), theClientDto);

			BigDecimal breiteInArtikeleinheit = getSystemFac().rechneUmInAndereEinheit(new BigDecimal(fBreite),
					SystemFac.EINHEIT_MILLIMETER, artikel.getEinheitCNr(), stklPosition.getIId(), theClientDto);

			return Helper.berechneBenoetigteMenge1D(mengeDim1Umgerechnet, breiteInArtikeleinheit, bdMenge, nLosgroesse);
		} else if (dimension == 2 && fBreite != null && fBreite.doubleValue() > 0 && fHoehe != null
				&& fHoehe.doubleValue() > 0
				&& (stklPosition.getEinheitCNr().equals(SystemFac.EINHEIT_QUADRATMETER)
						|| stklPosition.getEinheitCNr().equals(SystemFac.EINHEIT_QUADRATMILLIMETER))
				&& (artikel.getEinheitCNr().equals(SystemFac.EINHEIT_QUADRATMETER)
						|| artikel.getEinheitCNr().equals(SystemFac.EINHEIT_QUADRATMILLIMETER))) {

			BigDecimal breiteInMillimeter = new BigDecimal(dimension1);
			BigDecimal hoeheInMillimeter = new BigDecimal(dimension2);

			if (stklPosition.getEinheitCNr().equals(SystemFac.EINHEIT_QUADRATMETER)) {
				breiteInMillimeter = getSystemFac().rechneUmInAndereEinheit(breiteInMillimeter,
						SystemFac.EINHEIT_QUADRATMETER, SystemFac.EINHEIT_QUADRATMILLIMETER, stklPosition.getIId(),
						theClientDto);
				hoeheInMillimeter = getSystemFac().rechneUmInAndereEinheit(hoeheInMillimeter,
						SystemFac.EINHEIT_QUADRATMETER, SystemFac.EINHEIT_QUADRATMILLIMETER, stklPosition.getIId(),
						theClientDto);
			}

			BigDecimal artikelHoeheInMillimeter = new BigDecimal(fHoehe);
			BigDecimal artikelBreiteInMillimeter = new BigDecimal(fBreite);

			bdMenge = Helper.berechneBenoetigteMenge2D(hoeheInMillimeter, breiteInMillimeter, artikelHoeheInMillimeter,
					artikelBreiteInMillimeter, bdMenge, nLosgroesse);

			if (artikel.getEinheitCNr().equals(SystemFac.EINHEIT_QUADRATMETER)) {
				bdMenge = getSystemFac().rechneUmInAndereEinheit(bdMenge, SystemFac.EINHEIT_QUADRATMILLIMETER,
						SystemFac.EINHEIT_QUADRATMETER, stklPosition.getIId(), theClientDto);
			}

			return bdMenge;
		} else {
			switch (einheit.getIDimension()) {
			case 3:
				Float dimension3 = stklPosition.getFDimension3();
				if (dimension3 != null) {
					bdMenge = bdMenge.multiply(new BigDecimal(dimension3));
				}
				// Fall Through
			case 2:
				if (dimension2 != null) {
					bdMenge = bdMenge.multiply(new BigDecimal(dimension2));
				}
				// Fall Through
			case 1:
				if (dimension1 != null) {
					bdMenge = bdMenge.multiply(new BigDecimal(dimension1));
				}
				break;
			}

			bdMenge = getSystemFac().rechneUmInAndereEinheit(bdMenge, artikel.getEinheitCNr(), einheit.getCNr(),
					stklPosition.getIId(), theClientDto);

			return bdMenge;

		}
	}

	private void erstelleLossollmaterial(Integer losIId, Integer stuecklisteIId, BigDecimal bdPositionsMenge,
			Integer lagerIId_Hauptlager, boolean bFlachdruecken, int iEbene, boolean bErsatztypenAuslassen,boolean bErstlos,
			TheClientDto theClientDto) {
		iEbene++;
		try {
			
			
			
			
			StuecklistepositionDto[] stkPos = getStuecklisteFac()
					.stuecklistepositionFindByStuecklisteIId(stuecklisteIId, theClientDto);

			StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(stuecklisteIId, theClientDto);

			HashMap<Integer, Integer> ausStuecklistepositionIdWurdelossollmaterialIId = new HashMap<Integer, Integer>();

			for (int i = 0; i < stkPos.length; i++) {
				// alle stuecklistenpositionen ins los uebernehmen

				//PJ22552
				if(Helper.short2boolean(stkPos[i].getBInitial()) && !bErstlos) {
					continue;
				}
				
				LossollmaterialDto losMatDto = new LossollmaterialDto();
				losMatDto.setArtikelIId(stkPos[i].getArtikelIId());
				losMatDto.setBNachtraeglich(Helper.boolean2Short(false));
				losMatDto.setCKommentar(stkPos[i].getCKommentar());
				losMatDto.setCPosition(stkPos[i].getCPosition());
				losMatDto.setFDimension1(stkPos[i].getFDimension1());
				losMatDto.setFDimension2(stkPos[i].getFDimension2());
				losMatDto.setFDimension3(stkPos[i].getFDimension3());
				losMatDto.setILfdnummer(stkPos[i].getILfdnummer());
				losMatDto.setBRuestmenge(stkPos[i].getBRuestmenge());
				losMatDto.setIBeginnterminoffset(stkPos[i].getIBeginnterminoffset());

				losMatDto.setLosIId(losIId);
				losMatDto.setMontageartIId(stkPos[i].getMontageartIId());

				if (!stklDto.getMandantCNr().equals(theClientDto.getMandant())) {
					MontageartDto mDto = getStuecklisteFac().montageartFindByPrimaryKey(stkPos[i].getMontageartIId(),
							theClientDto);

					MontageartDto mDtoEigenerMandant = getStuecklisteFac()
							.montageartFindByMandantCNrCBezOhneExc(theClientDto.getMandant(), mDto.getCBez());

					if (mDtoEigenerMandant != null) {
						losMatDto.setMontageartIId(mDtoEigenerMandant.getIId());
					} else {
						MontageartDto[] mDtos = getStuecklisteFac().montageartFindByMandantCNr(theClientDto);
						if (mDtos != null && mDtos.length > 0) {
							losMatDto.setMontageartIId(mDtos[0].getIId());
						}
					}
				}

				losMatDto.setNMengeStklPos(stkPos[i].getNMenge());
				losMatDto.setEinheitCNrStklPos(stkPos[i].getEinheitCNr());
				// Einheit umrechnen
				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(stkPos[i].getArtikelIId(),
						theClientDto);
				losMatDto.setEinheitCNr(artikelDto.getEinheitCNr());
				BigDecimal bdFaktor = getSystemFac().rechneUmInAndereEinheit(new BigDecimal(1),
						artikelDto.getEinheitCNr(), stkPos[i].getEinheitCNr(), stkPos[i].getIId(), theClientDto);

				// nun die Dimensionen
				BigDecimal bdDimProdukt = new BigDecimal(1);
				EinheitDto einheitDto = getSystemFac().einheitFindByPrimaryKey(stkPos[i].getEinheitCNr(), theClientDto);

				ParametermandantDto parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
						theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
						ParameterFac.PARAMETER_VERSCHNITT_NACH_ABMESSUNG);

				boolean bVerschnittNachAbmessung = (Boolean) parameter.getCWertAsObject();

				if (einheitDto.getIDimension().intValue() >= 1) {
					if (stkPos[i].getFDimension1() != null) {
						bdDimProdukt = bdDimProdukt.multiply(new BigDecimal(stkPos[i].getFDimension1().floatValue()));
					}
				}
				if (einheitDto.getIDimension().intValue() >= 2) {
					if (stkPos[i].getFDimension2() != null) {
						bdDimProdukt = bdDimProdukt.multiply(new BigDecimal(stkPos[i].getFDimension2().floatValue()));
					}
				}
				if (einheitDto.getIDimension().intValue() >= 3) {
					if (stkPos[i].getFDimension3() != null) {
						bdDimProdukt = bdDimProdukt.multiply(new BigDecimal(stkPos[i].getFDimension3().floatValue()));
					}
				}

				boolean bRuestmenge = Helper.short2boolean(stkPos[i].getBRuestmenge());

				// SP7277
				BigDecimal bdMengeProLos = stkPos[i].getNMenge().multiply(bdDimProdukt).divide(bdFaktor, 12,
						RoundingMode.HALF_UP);

				if (!bRuestmenge && BigDecimal.ZERO.compareTo(stklDto.getNErfassungsfaktor()) != 0) {
					bdMengeProLos = bdMengeProLos.divide(stklDto.getNErfassungsfaktor(), 12, RoundingMode.DOWN);
				}
				losMatDto.setNMengeProLos(bdMengeProLos);

				// PJ21211
				if (bVerschnittNachAbmessung
						&& (einheitDto.getIDimension().intValue() == 1 || einheitDto.getIDimension().intValue() == 2)) {

					BigDecimal mengeProPosition = berechneMengeVerschnittNachAbmessung(stkPos[i].getNMenge(),
							bdPositionsMenge, artikelDto, einheitDto, stkPos[i], theClientDto);

					losMatDto.setNMenge(mengeProPosition.multiply(bdPositionsMenge));

				} else {

					// endgueltige Menge berechnen
					if (bdFaktor.doubleValue() != 0) {

						/// SP7028
						BigDecimal bdMenge = Helper.berechneMengeInklusiveVerschnitt(bdMengeProLos,
								artikelDto.getFVerschnittfaktor(), artikelDto.getFVerschnittbasis(), bdPositionsMenge,
								artikelDto.getFFertigungsVpe());

						BigDecimal losSollMenge = bdMenge;

						if (!bRuestmenge) {
							losSollMenge = losSollMenge.multiply(bdPositionsMenge);
						}

						if (losSollMenge.doubleValue() < 0.001 && losSollMenge.doubleValue() > 0.000001) {
							losSollMenge = new BigDecimal("0.001");
							losSollMenge = losSollMenge.setScale(3, BigDecimal.ROUND_HALF_EVEN);
						} else {
							losSollMenge = losSollMenge.setScale(3, BigDecimal.ROUND_HALF_EVEN);
						}

						if (artikelDto.getNVerschnittmenge() != null) {
							losSollMenge = losSollMenge.add(artikelDto.getNVerschnittmenge());
						}

						losMatDto.setNMenge(losSollMenge);

					}
				}

				losMatDto.setNSollpreis(BigDecimal.ZERO);

				// PJ18903
				if (stkPos[i].getNKalkpreis() != null) {
					losMatDto.setNSollpreis(stkPos[i].getNKalkpreis());

				} else {
					losMatDto = holeSollpreisAusLief1(theClientDto, losMatDto);
				}

				// Datensatz speichern

				// Wenn Unterstueckliste und Hilfsstueckliste:

				StuecklisteDto stuecklisteDto = getStuecklisteFac().stuecklisteFindByMandantCNrArtikelIIdOhneExc(
						stkPos[i].getArtikelIId(), stklDto.getMandantCNr());

				if (stuecklisteDto != null
						&& stuecklisteDto.getStuecklisteartCNr().equals(StuecklisteFac.STUECKLISTEART_HILFSSTUECKLISTE)
						&& bFlachdruecken == true) {
					if (iEbene < 10) {
						erstelleLossollmaterial(losIId, stuecklisteDto.getIId(), losMatDto.getNMenge(),
								lagerIId_Hauptlager, true, iEbene, bErsatztypenAuslassen, bErstlos, theClientDto);

					}

				} else {

					LossollmaterialDto lossollmaterialDto = createLossollmaterial(losMatDto, theClientDto);

					LossollmaterialDto lossollmaterialDtoNachAnlage = lossollmaterialFindByPrimaryKey(
							lossollmaterialDto.getIId());

					Integer iOriginal_IId = new Integer(lossollmaterialDto.getIId());

					ausStuecklistepositionIdWurdelossollmaterialIId.put(stkPos[i].getIId(),
							lossollmaterialDto.getIId());

					// Ersatztypen anlegen

					ArrayList<LossollmaterialDto> alErsatztypen = new ArrayList<LossollmaterialDto>();
					ArrayList<LossollmaterialDto> alErsatztypenGesamt = new ArrayList<LossollmaterialDto>();

					PosersatzDto[] posersatzDtos = getStuecklisteFac()
							.posersatzFindByStuecklistepositionIId(stkPos[i].getIId());
					if (iEbene < 10) {

						for (int k = 0; k < posersatzDtos.length; k++) {

							losMatDto.setArtikelIId(posersatzDtos[k].getArtikelIIdErsatz());
							losMatDto.setNMenge(new BigDecimal(0));
							losMatDto.setNSollpreis(new BigDecimal(0));
							losMatDto.setISort(null);
							losMatDto.setLossollmaterialIIdOriginal(iOriginal_IId);
							// SP4861
							losMatDto = holeSollpreisAusLief1(theClientDto, losMatDto);

							LossollmaterialDto smDtoNeu = createLossollmaterial(losMatDto, true, bErsatztypenAuslassen,
									theClientDto);

							if (smDtoNeu != null && smDtoNeu.getIId() != null) {
								iOriginal_IId = smDtoNeu.getIId();
								LossollmaterialDto sollmatNeu = lossollmaterialFindByPrimaryKey(smDtoNeu.getIId());

								alErsatztypenGesamt.add(sollmatNeu);

								if (losMatDto.getNSollpreis().doubleValue() != 0) {
									alErsatztypen.add(sollmatNeu);
								}
							}

						}

					}

					// PL19166

					ErsatztypenDto[] ersatztypenDtos = getArtikelFac()
							.ersatztypenFindByArtikelIId(lossollmaterialDto.getArtikelIId());
					if (iEbene < 10) {
						for (int k = 0; k < ersatztypenDtos.length; k++) {

							losMatDto.setArtikelIId(ersatztypenDtos[k].getArtikelIIdErsatz());
							losMatDto.setNMenge(new BigDecimal(0));
							losMatDto.setNSollpreis(new BigDecimal(0));
							losMatDto.setISort(null);
							losMatDto.setLossollmaterialIIdOriginal(iOriginal_IId);
							// SP4861
							losMatDto = holeSollpreisAusLief1(theClientDto, losMatDto);

							LossollmaterialDto sollmatDto = createLossollmaterial(losMatDto, true,
									bErsatztypenAuslassen, theClientDto);

							alErsatztypenGesamt.add(lossollmaterialFindByPrimaryKey(sollmatDto.getIId()));

							if (losMatDto.getNSollpreis().doubleValue() != 0) {
								alErsatztypen.add(lossollmaterialFindByPrimaryKey(sollmatDto.getIId()));
							}

						}
					}

					ParametermandantDto parameterVorlaufzeit = getParameterFac().getMandantparameter(
							theClientDto.getSMandantenwaehrung(), ParameterFac.KATEGORIE_FERTIGUNG,
							ParameterFac.PARAMETER_ERSATZTYPEN_SOLLMENGE);
					int iErsatztypenSollmenge = (Integer) parameterVorlaufzeit.getCWertAsObject();

					// PJ20134

					if (iErsatztypenSollmenge == 1) {
						if (alErsatztypen.size() > 0) {
							for (int k = alErsatztypen.size() - 1; k > 0; --k) {
								for (int j = 0; j < k; ++j) {
									LossollmaterialDto sm1 = (LossollmaterialDto) alErsatztypen.get(j);
									LossollmaterialDto sm2 = (LossollmaterialDto) alErsatztypen.get(j + 1);

									if (sm1.getNSollpreis().compareTo(sm2.getNSollpreis()) > 0) {
										alErsatztypen.set(j, sm2);
										alErsatztypen.set(j + 1, sm1);
									}

								}
							}

							LossollmaterialDto sollmatDtoHauptartikel = lossollmaterialFindByPrimaryKey(iOriginal_IId);

							// Ersatztyp auf die Sollmenge des Hauptartikels
							// setzen
							LossollmaterialDto sollmatDto = alErsatztypen.get(0);

							Lossollmaterial sollmatErsatzartikel = em.find(Lossollmaterial.class, sollmatDto.getIId());
							sollmatErsatzartikel.setNMenge(sollmatDtoHauptartikel.getNMenge());
							em.merge(sollmatErsatzartikel);
							em.flush();

							// Sollmenge des Haptartikels auf 0 setzen

							Lossollmaterial sollmatHauptartikel = em.find(Lossollmaterial.class,
									sollmatDtoHauptartikel.getIId());
							sollmatHauptartikel.setNMenge(BigDecimal.ZERO);
							em.merge(sollmatHauptartikel);
							em.flush();
						}
					} else if (iErsatztypenSollmenge == 2) {
						// PJ20379
						if (alErsatztypenGesamt.size() > 0) {
							LoslagerentnahmeDto[] loslagerDtos = getFertigungFac().loslagerentnahmeFindByLosIId(losIId);

							BigDecimal bdSollmengeOriginalArtikel = lossollmaterialDtoNachAnlage.getNMenge();

							for (int k = 0; k < alErsatztypenGesamt.size(); k++) {
								LossollmaterialDto sm = (LossollmaterialDto) alErsatztypenGesamt.get(k);

								// Lagerstand

								ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(sm.getArtikelIId(),
										theClientDto);
								if (aDto.isLagerbewirtschaftet()) {

									BigDecimal bdAufLager = BigDecimal.ZERO;
									for (int l = 0; l < loslagerDtos.length; l++) {
										bdAufLager = bdAufLager.add(getLagerFac().getLagerstand(sm.getArtikelIId(),
												loslagerDtos[l].getLagerIId(), theClientDto));
									}

									if (bdSollmengeOriginalArtikel.doubleValue() > bdAufLager.doubleValue()) {
										Lossollmaterial sollmatErsatzartikel = em.find(Lossollmaterial.class,
												sm.getIId());
										sollmatErsatzartikel.setNMenge(bdAufLager);
										em.merge(sollmatErsatzartikel);
										em.flush();
										bdSollmengeOriginalArtikel = bdSollmengeOriginalArtikel.subtract(bdAufLager);
									} else if (bdSollmengeOriginalArtikel.doubleValue() <= bdAufLager.doubleValue()) {
										Lossollmaterial sollmatErsatzartikel = em.find(Lossollmaterial.class,
												sm.getIId());
										sollmatErsatzartikel.setNMenge(bdSollmengeOriginalArtikel);
										em.merge(sollmatErsatzartikel);
										em.flush();
										bdSollmengeOriginalArtikel = BigDecimal.ZERO;
										break;
									}

								}

							}

							Lossollmaterial sollmatHauptartikel = em.find(Lossollmaterial.class,
									lossollmaterialDtoNachAnlage.getIId());
							sollmatHauptartikel.setNMenge(bdSollmengeOriginalArtikel);
							em.merge(sollmatHauptartikel);
							em.flush();

						}

					} else if (iErsatztypenSollmenge == 3) {
						// PJ21397
						if (alErsatztypenGesamt.size() > 0) {
							LoslagerentnahmeDto[] loslagerDtos = getFertigungFac().loslagerentnahmeFindByLosIId(losIId);

							BigDecimal bdSollmengeOriginalArtikel = lossollmaterialDtoNachAnlage.getNMenge();

							BigDecimal bdAufLagerOriginal = BigDecimal.ZERO;
							for (int l = 0; l < loslagerDtos.length; l++) {
								bdAufLagerOriginal = bdAufLagerOriginal
										.add(getLagerFac().getLagerstand(lossollmaterialDtoNachAnlage.getArtikelIId(),
												loslagerDtos[l].getLagerIId(), theClientDto));
							}

							if (bdSollmengeOriginalArtikel.doubleValue() > bdAufLagerOriginal.doubleValue()) {
								// Wenn vom original zuwenig auf Lager, dann erst Ersatztypen verwenden

								BigDecimal bdDiff = bdSollmengeOriginalArtikel.subtract(bdAufLagerOriginal);

								for (int k = 0; k < alErsatztypenGesamt.size(); k++) {
									LossollmaterialDto sm = (LossollmaterialDto) alErsatztypenGesamt.get(k);

									// Lagerstand

									ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(sm.getArtikelIId(),
											theClientDto);
									if (aDto.isLagerbewirtschaftet()) {

										BigDecimal bdAufLager = BigDecimal.ZERO;
										for (int l = 0; l < loslagerDtos.length; l++) {
											bdAufLager = bdAufLager.add(getLagerFac().getLagerstand(sm.getArtikelIId(),
													loslagerDtos[l].getLagerIId(), theClientDto));
										}

										if (bdDiff.doubleValue() > bdAufLager.doubleValue()) {
											Lossollmaterial sollmatErsatzartikel = em.find(Lossollmaterial.class,
													sm.getIId());
											sollmatErsatzartikel.setNMenge(bdAufLager);
											bdSollmengeOriginalArtikel = bdSollmengeOriginalArtikel
													.subtract(bdAufLager);
											em.merge(sollmatErsatzartikel);
											em.flush();
											bdDiff = bdDiff.subtract(bdAufLager);
										} else if (bdDiff.doubleValue() <= bdAufLager.doubleValue()) {
											Lossollmaterial sollmatErsatzartikel = em.find(Lossollmaterial.class,
													sm.getIId());
											sollmatErsatzartikel.setNMenge(bdDiff);
											bdSollmengeOriginalArtikel = bdSollmengeOriginalArtikel.subtract(bdDiff);
											em.merge(sollmatErsatzartikel);
											em.flush();
											bdDiff = BigDecimal.ZERO;
											break;
										}

									}

								}

								Lossollmaterial sollmatHauptartikel = em.find(Lossollmaterial.class,
										lossollmaterialDtoNachAnlage.getIId());
								sollmatHauptartikel.setNMenge(bdSollmengeOriginalArtikel);
								em.merge(sollmatHauptartikel);
								em.flush();
							}

						}

					}

				}
			}

			StklpruefplanDto[] pruefplanDtos = getStuecklisteFac().stklpruefplanFindByStuecklisteIId(stuecklisteIId);

			for (StklpruefplanDto pruefplanDto : pruefplanDtos) {

				LospruefplanDto lospruefplanDto = new LospruefplanDto();
				lospruefplanDto.setPruefartIId(pruefplanDto.getPruefartIId());

				lospruefplanDto.setPruefkombinationId(pruefplanDto.getPruefkombinationId());
				lospruefplanDto.setBDoppelanschlag(pruefplanDto.getBDoppelanschlag());
				lospruefplanDto.setISort(pruefplanDto.getISort());

				lospruefplanDto.setVerschleissteilIId(pruefplanDto.getVerschleissteilIId());
				lospruefplanDto.setLosIId(losIId);

				lospruefplanDto.setLossollmaterialIIdKontakt(ausStuecklistepositionIdWurdelossollmaterialIId
						.get(pruefplanDto.getStuecklistepositionIIdKontakt()));
				lospruefplanDto.setLossollmaterialIIdLitze(ausStuecklistepositionIdWurdelossollmaterialIId
						.get(pruefplanDto.getStuecklistepositionIIdLitze()));

				lospruefplanDto.setLossollmaterialIIdLitze2(ausStuecklistepositionIdWurdelossollmaterialIId
						.get(pruefplanDto.getStuecklistepositionIIdLitze2()));

				Integer artikelIIdKontakt = null;

				if (pruefplanDto.getStuecklistepositionIIdKontakt() != null) {
					artikelIIdKontakt = getStuecklisteFac().stuecklistepositionFindByPrimaryKey(
							pruefplanDto.getStuecklistepositionIIdKontakt(), theClientDto).getArtikelIId();
				}

				Integer artikelIIdLitze = null;
				if (pruefplanDto.getStuecklistepositionIIdLitze() != null) {
					artikelIIdLitze = getStuecklisteFac().stuecklistepositionFindByPrimaryKey(
							pruefplanDto.getStuecklistepositionIIdLitze(), theClientDto).getArtikelIId();
				}

				Integer artikelIIdLitze2 = null;
				if (pruefplanDto.getStuecklistepositionIIdLitze2() != null) {
					artikelIIdLitze2 = getStuecklisteFac().stuecklistepositionFindByPrimaryKey(
							pruefplanDto.getStuecklistepositionIIdLitze2(), theClientDto).getArtikelIId();
				}

				getStuecklisteFac().pruefeObPruefplanInPruefkombinationVorhanden(pruefplanDto.getStuecklisteId(),
						pruefplanDto.getPruefartIId(), artikelIIdKontakt, artikelIIdLitze, artikelIIdLitze2,
						pruefplanDto.getVerschleissteilIId(), pruefplanDto.getPruefkombinationId(), true, theClientDto);

				getFertigungServiceFac().createLospruefplan(lospruefplanDto, theClientDto);

			}

		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	private LossollmaterialDto holeSollpreisAusLief1(TheClientDto theClientDto, LossollmaterialDto losMatDto)
			throws RemoteException {
		ArtikellieferantDto artikellieferantDto = getArtikelFac().getArtikelEinkaufspreisDesBevorzugtenLieferanten(
				losMatDto.getArtikelIId(), losMatDto.getNMenge(), theClientDto.getSMandantenwaehrung(), theClientDto);
		if (artikellieferantDto != null && artikellieferantDto.getLief1Preis() != null) {
			losMatDto.setNSollpreis(artikellieferantDto.getLief1Preis());
		}
		return losMatDto;
	}

	private void aktualisiereSollMaterialAusStueckliste(Integer losIId, TheClientDto theClientDto,
			boolean bLoescheVorherDieReservierungen, boolean bErsatztypenAuslassen) throws EJBExceptionLP {
		try {
			LosDto losDto = losFindByPrimaryKey(losIId);

			if (losDto.getTMaterialvollstaendig() != null) {
				ArrayList<Object> al = new ArrayList<>();
				al.add(losDto.getCNr());
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_MATERIAL_VOLLSTAENDIG, al,
						new Exception("FEHLER_FERTIGUNG_MATERIAL_VOLLSTAENDIG" + " Los:" + losDto.getCNr()));
			}

			// Aktualisierung ist nur im Status angelegt erlaubt.
			if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
				// und nur fuer stuecklistenbezogene Lose
				if (losDto.getStuecklisteIId() != null) {

					// PJ19486
					Query queryPruefplan = em.createNamedQuery("LospruefplanFindByLosIId");
					queryPruefplan.setParameter(1, losDto.getIId());

					Collection<?> clPruefplan = queryPruefplan.getResultList();

					Iterator<?> iteratorPruefplan = clPruefplan.iterator();
					while (iteratorPruefplan.hasNext()) {
						Lospruefplan lospruefplanTemp = (Lospruefplan) iteratorPruefplan.next();
						em.remove(lospruefplanTemp);
						em.flush();
					}

					LagerDto lagerDtoMandant = getLagerFac().getHauptlagerDesMandanten(theClientDto);
					// alle Positionen holen
					LossollmaterialDto[] c = lossollmaterialFindByLosIId(losIId);
					// alle nicht nachtraeglichen loeschen
					for (int i = 0; i < c.length; i++) {
						if (bLoescheVorherDieReservierungen) {
							// Reservierung loeschen
							ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(c[i].getArtikelIId(),
									theClientDto);
							removeReservierung(artikelDto, c[i].getIId());
						}
						// nur die aus der stueckliste loeschen
						if (Helper.short2boolean(c[i].getBNachtraeglich()) == false) {
							Lossollmaterial toRemove = em.find(Lossollmaterial.class, c[i].getIId());

							if (toRemove == null) {
								throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
							}

							// Vorher Sollmaterialposition aus Sollarbeitsgang
							// entfernen

							Query query = em.createNamedQuery("LossollarbeitsplanfindByLossollmaterialIId");
							query.setParameter(1, toRemove.getIId());

							Collection<?> cl = query.getResultList();

							if (cl != null) {
								Iterator<?> iterator = cl.iterator();
								while (iterator.hasNext()) {
									Lossollarbeitsplan lossollarbeitsplanTemp = (Lossollarbeitsplan) iterator.next();
									lossollarbeitsplanTemp.setLossollmaterialIId(null);
									em.merge(lossollarbeitsplanTemp);
									em.flush();
								}
							}

							// SP2350 Zuerst Referenz des Ersatzartikels loschen

							Query queryOrginial = em
									.createNamedQuery("LossollmaterialfindByLossollmaterialIIdOriginal");
							queryOrginial.setParameter(1, toRemove.getIId());
							Collection<?> clOriginal = queryOrginial.getResultList();
							Iterator itOriginal = clOriginal.iterator();
							while (itOriginal.hasNext()) {
								Lossollmaterial mat_ori = (Lossollmaterial) itOriginal.next();
								mat_ori.setLossollmaterialIIdOriginal(null);
								em.merge(mat_ori);
								em.flush();
							}

							// Auch Pruefplan loeschen, da Abhaengigkeiten

							// PJ SP2012/305
							Query queryAP = em.createNamedQuery("BestellpositionfindByLossollmaterialIId");
							queryAP.setParameter(1, toRemove.getIId());
							Collection<?> clAP = queryAP.getResultList();
							if (clAP.size() > 0) {
								toRemove.setNMenge(new BigDecimal(0));
							} else {
								try {
									em.remove(toRemove);
									em.flush();
								} catch (EntityExistsException er) {
									throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
								}
							}

						}
					}
					
					
					boolean bErstlos=getFertigungReportFac().istErstlos(losDto,theClientDto);
					
					erstelleLossollmaterial(losDto.getIId(), losDto.getStuecklisteIId(), losDto.getNLosgroesse(),
							lagerDtoMandant.getIId(), true, 0, bErsatztypenAuslassen,bErstlos, theClientDto);

					// Reservierung anlegen
					// das in einer neuen schleife, dami die nachtraeglichen
					// auch dabei sind
					LossollmaterialDto[] sollmat = lossollmaterialFindByLosIId(losIId);
					for (int i = 0; i < sollmat.length; i++) {
						ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(sollmat[i].getArtikelIId(),
								theClientDto);
						if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet())) {
							java.sql.Date dTermin;
							if (sollmat[i].getNMenge().compareTo(new BigDecimal(0)) > 0) {
								// Positive Reservierung: produktionsstart
								dTermin = getFertigungFac().getProduktionsbeginnAnhandZugehoerigemArbeitsgang(
										losDto.getTProduktionsbeginn(), sollmat[i].getIId(), theClientDto);

							} else {
								// Negative Reservierung: produktionsende
								dTermin = losDto.getTProduktionsende();
							}

							// PJ17994
							dTermin = Helper.addiereTageZuDatum(dTermin, sollmat[i].getIBeginnterminoffset());
							createReservierung(artikelDto, sollmat[i].getIId(), sollmat[i].getNMenge(),
									new java.sql.Timestamp(dTermin.getTime()));
						}
					}
					// Aktualisierungszeit setzen
					Los los = em.find(Los.class, losIId);
					if (los == null) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
					}
					los.setTAktualisierungstueckliste(getTimestamp());
				}
			} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT,
						new Exception("los " + losDto.getCNr() + " ist storniert"));
			} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_AUSGEGEBEN)
					|| losDto.getStatusCNr().equals(FertigungFac.STATUS_GESTOPPT)
					|| losDto.getStatusCNr().equals(FertigungFac.STATUS_IN_PRODUKTION)
					|| losDto.getStatusCNr().equals(FertigungFac.STATUS_TEILERLEDIGT)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_AUSGEGEBEN,
						new Exception("los " + losDto.getCNr() + " ist bereits ausgegeben"));
			} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
						new Exception("los " + losDto.getCNr() + " ist bereits erledigt"));
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
	}

	public LossollarbeitsplanDto[] getAlleZusatzlichZuBuchuchendenArbeitsgaenge(Integer lossollarbeitsplanIId,
			TheClientDto theClientDto) {
		List<LossollarbeitsplanDto> list = new ArrayList<LossollarbeitsplanDto>();
		// Vorhandene AG-NR holen
		Lossollarbeitsplan lossollarbeitsplan = em.find(Lossollarbeitsplan.class, lossollarbeitsplanIId);
		if (lossollarbeitsplan.getAgartCNr() != null
				&& lossollarbeitsplan.getAgartCNr().equals(StuecklisteFac.AGART_UMSPANNZEIT)) {
			Query query = em.createNamedQuery("LossollarbeitsplanfindByLosIIdIArbeitsgangnummer");
			query.setParameter(1, lossollarbeitsplan.getLosIId());
			query.setParameter(2, lossollarbeitsplan.getIArbeitsgangnummer());
			Collection<?> cl = query.getResultList();

			if (cl != null) {
				Iterator<?> iterator = cl.iterator();
				while (iterator.hasNext()) {
					Lossollarbeitsplan lossollarbeitsplanTemp = (Lossollarbeitsplan) iterator.next();

					if (!lossollarbeitsplanTemp.getIId().equals(lossollarbeitsplanIId)) {

						if (lossollarbeitsplanTemp.getAgartCNr() != null
								&& (lossollarbeitsplanTemp.getAgartCNr().equals(StuecklisteFac.AGART_UMSPANNZEIT)
										|| lossollarbeitsplanTemp.getAgartCNr().equals(StuecklisteFac.AGART_LAUFZEIT)))
							list.add(assembleLossollarbeitsplanDto(lossollarbeitsplanTemp));
					}

				}
			}

		}
		LossollarbeitsplanDto[] returnArray = new LossollarbeitsplanDto[list.size()];
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
			Lossollarbeitsplan lossollarbeitsplan = (Lossollarbeitsplan) iterator.next();

			if (Helper.short2boolean(lossollarbeitsplan.getBNachtraeglich()) == false) {

				Query q2 = em.createNamedQuery("LosgutschlechtFindByLossollarbeitsplanIId");
				q2.setParameter(1, lossollarbeitsplan.getIId());

				Query q3 = em.createNamedQuery("MaschinenzeitdatenfindByLossollarbeitsplanIId");
				q3.setParameter(1, lossollarbeitsplan.getIId());
				

				Query q4 = em.createNamedQuery("ZeitdatenfindZeitdatenEinerBelegposition");
				q4.setParameter(1, LocaleFac.BELEGART_LOS);
				q4.setParameter(2, losIId);
				q4.setParameter(3, lossollarbeitsplan.getIId());
				q4.setMaxResults(1);
				

				if (q2.getResultList().size() > 0 || q3.getResultList().size() > 0 || q4.getResultList().size() > 0) {
					lossollarbeitsplan.setLRuestzeit((long) 0);
					lossollarbeitsplan.setLStueckzeit((long) 0);
					lossollarbeitsplan.setNGesamtzeit(new BigDecimal(0));
					em.merge(lossollarbeitsplan);
					em.flush();

				} else {
					try {
						em.remove(lossollarbeitsplan);
						em.flush();
					} catch (EntityExistsException e) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
					}
				}
			}

		}
	}

	private Integer holeLossollmaterialIdAnhandStklPosition(Integer losIId, Integer stuecklistepositionIId,
			TheClientDto theClientDto) {

		try {
			StuecklistepositionDto stuecklistepositionSto = getStuecklisteFac()
					.stuecklistepositionFindByPrimaryKey(stuecklistepositionIId, theClientDto);

			Query query = em.createNamedQuery("LossollmaterialfindByLosIIdArtikelIId");
			query.setParameter(1, losIId);
			query.setParameter(2, stuecklistepositionSto.getArtikelIId());
			Collection<?> cl = query.getResultList();

			Iterator<?> iterator = cl.iterator();
			while (iterator.hasNext()) {
				Lossollmaterial lossollmaterial = (Lossollmaterial) iterator.next();
				return lossollmaterial.getIId();
			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return null;
	}

	public LossollmaterialDto[] lossollmaterialFindyByLosIIdArtikelIId(Integer losIId, Integer artikelIId,
			TheClientDto theClientDto) {
		Query query = em.createNamedQuery("LossollmaterialfindByLosIIdArtikelIId");
		query.setParameter(1, losIId);
		query.setParameter(2, artikelIId);
		Collection<?> cl = query.getResultList();
		return assembleLossollmaterialDtos(cl);
	}

	private void berechneLosEndeAnhandMaschinenversatztage(Integer losIId, TheClientDto theClientDto) {

		LosDto losDto = losFindByPrimaryKey(losIId);

		Los los = em.find(Los.class, losIId);

		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)
				|| losDto.getStatusCNr().equals(FertigungFac.STATUS_AUSGEGEBEN)
				|| losDto.getStatusCNr().equals(FertigungFac.STATUS_IN_PRODUKTION)
				|| losDto.getStatusCNr().equals(FertigungFac.STATUS_TEILERLEDIGT)
				|| losDto.getStatusCNr().equals(FertigungFac.STATUS_GESTOPPT)) {

			Integer zeitmodellIId = null;

			Query queryFZ = em.createNamedQuery("ZeitmodellfindByBFirmenzeitmodellMandantCNr");
			queryFZ.setParameter(1, new Short((short) 1));
			queryFZ.setParameter(2, theClientDto.getMandant());
			// @todo getSingleResult oder getResultList ?
			Collection c = queryFZ.getResultList();
			if (c.size() > 0) {

				zeitmodellIId = ((Zeitmodell) c.iterator().next()).getIId();

			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FIRMENZEITMODELL_NICHT_VORHANDEN,
						new Exception("FEHLER_FIRMENZEITMODELL_NICHT_VORHANDEN"));
			}

			java.sql.Date dBeginn = losDto.getTProduktionsbeginn();

			java.sql.Date dEnde = dBeginn;

			Query query = em.createNamedQuery("LossollarbeitsplanfindByLosIId");
			query.setParameter(1, losIId);
			Collection<?> cl = query.getResultList();

			LossollarbeitsplanDto[] lossollarbeitsplanDtos = assembleLossollarbeitsplanDtos(cl);

			for (int j = 0; j < lossollarbeitsplanDtos.length; j++) {
				LossollarbeitsplanDto lossollarbeitsplanDto = lossollarbeitsplanDtos[j];

				if (lossollarbeitsplanDto.getIMaschinenversatztageAusStueckliste() != null) {

					int iZahler = 0;

					java.sql.Date dBeginnAG = dBeginn;

					int iZaehlerGesamt = 0;

					while (iZahler < lossollarbeitsplanDto.getIMaschinenversatztageAusStueckliste()) {

						iZaehlerGesamt++;

						ZeitmodelltagDto zmtagDto = getZeiterfassungFac().getZeitmodelltagFirmenzeitmodellZuDatum(
								zeitmodellIId, new Timestamp(dBeginnAG.getTime()), theClientDto);

						if (zmtagDto != null && zmtagDto.getUSollzeit() != null) {
							double dSollzeit = Helper.time2Double(zmtagDto.getUSollzeit());
							if (dSollzeit > 0) {
								iZahler++;
							}
						}

						if (iZahler < lossollarbeitsplanDto.getIMaschinenversatztageAusStueckliste()) {
							dBeginnAG = Helper.addiereTageZuDatum(dBeginnAG, 1);
						}

						if (iZaehlerGesamt > 1000) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_FIRMENZEITMODELL_NICHT_AUSREICHEND_SOLLZEIT_DEFINIERT,
									new Exception("FEHLER_FIRMENZEITMODELL_NICHT_AUSREICHEND_SOLLZEIT_DEFINIERT"));

						}

					}

					if (dBeginnAG.after(dEnde)) {
						dEnde = dBeginnAG;
					}

				}

			}

			// Zum Ende noch einen Werktag hinzufuegen

			boolean bEinenWerktagHinzugefuegt = false;

			int iZaehlerGesamt = 0;

			while (bEinenWerktagHinzugefuegt == false) {
				iZaehlerGesamt++;

				dEnde = Helper.addiereTageZuDatum(dEnde, 1);

				ZeitmodelltagDto zmtagDto = getZeiterfassungFac().getZeitmodelltagFirmenzeitmodellZuDatum(zeitmodellIId,
						new Timestamp(dEnde.getTime()), theClientDto);

				if (zmtagDto != null && zmtagDto.getUSollzeit() != null) {
					double dSollzeit = Helper.time2Double(zmtagDto.getUSollzeit());
					if (dSollzeit > 0) {
						bEinenWerktagHinzugefuegt = true;

					}
				}

				if (iZaehlerGesamt > 1000) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FIRMENZEITMODELL_NICHT_AUSREICHEND_SOLLZEIT_DEFINIERT,
							new Exception("FEHLER_FIRMENZEITMODELL_NICHT_AUSREICHEND_SOLLZEIT_DEFINIERT"));

				}

			}

			los.setTProduktionsende(dEnde);
			em.merge(los);
			em.flush();

		}

	}

	public void aktualisiereSollArbeitsplanAusStueckliste(Integer losIId, TheClientDto theClientDto)
			throws EJBExceptionLP {

		LosDto losDto = losFindByPrimaryKey(losIId);
		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)
				|| losDto.getStatusCNr().equals(FertigungFac.STATUS_AUSGEGEBEN)
				|| losDto.getStatusCNr().equals(FertigungFac.STATUS_IN_PRODUKTION)
				|| losDto.getStatusCNr().equals(FertigungFac.STATUS_TEILERLEDIGT)
				|| losDto.getStatusCNr().equals(FertigungFac.STATUS_GESTOPPT)) {

			// Darf nur fuer stuecklistenbezogene Lose durchgefuehrt werden
			if (losDto.getStuecklisteIId() != null) {

				boolean bErstlos=getFertigungReportFac().istErstlos(losDto,theClientDto);
				
				int arbeitsgaengeBeiHilfsstuecklistenVerdichten = 0;
				try {
					ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_STUECKLISTE,
							ParameterFac.PARAMETER_ARBEITSGAENGE_BEI_HILFSSTUECKLISTEN_VERDICHTEN);
					arbeitsgaengeBeiHilfsstuecklistenVerdichten = (Integer) parameter.getCWertAsObject();
				} catch (RemoteException ex) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
				}

				getFertigungFac().vorhandenenArbeitsplanLoeschen(losIId);
				// alle Positionen holen
				Query query = em.createNamedQuery("LossollarbeitsplanfindByLosIId");
				query.setParameter(1, losIId);
				Collection<?> cl = query.getResultList();

				LossollarbeitsplanDto[] lossollarbeitsplanDtos = assembleLossollarbeitsplanDtos(cl);

				ArrayList<StuecklistearbeitsplanDto> alStuecklistePositionen = new ArrayList<StuecklistearbeitsplanDto>();

				// nun den gesamten Arbeitsplan der Stueckliste ins Los
				// kopieren
				StuecklistearbeitsplanDto[] stkPos = getStuecklisteFac()
						.stuecklistearbeitsplanFindByStuecklisteIId(losDto.getStuecklisteIId(), theClientDto);
				for (int i = 0; i < stkPos.length; i++) {
					
					if(Helper.short2boolean(stkPos[i].getBInitial()) && !bErstlos) {
						continue;
					}
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
						.stuecklistepositionFindByStuecklisteIId(losDto.getStuecklisteIId(), theClientDto);
				int iArbeitsgang = 100;
				for (int j = 0; j < stklmaterialPos.length; j++) {
					StuecklisteDto unterStuecklisteDto = getStuecklisteFac()
							.stuecklisteFindByMandantCNrArtikelIIdOhneExc(stklmaterialPos[j].getArtikelIId(),
									theClientDto);

					if (unterStuecklisteDto != null
							&& unterStuecklisteDto.getStuecklisteartCNr()
									.equals(StuecklisteFac.STUECKLISTEART_HILFSSTUECKLISTE)
							&& Helper.short2boolean(unterStuecklisteDto.getBFremdfertigung()) == false) {
						// nun den gesamten Arbeitsplan der Stueckliste ins
						// Los kopieren
						StuecklistearbeitsplanDto[] unterstklPos = getStuecklisteFac()
								.stuecklistearbeitsplanFindByStuecklisteIId(unterStuecklisteDto.getIId(), theClientDto);
						for (int i = 0; i < unterstklPos.length; i++) {
							iArbeitsgang++;

							unterstklPos[i].setIArbeitsgang(iArbeitsgang);

							// SP5483
							unterstklPos[i].setLStueckzeit((long) (unterstklPos[i].getLStueckzeit()
									* stklmaterialPos[j].getNMenge().doubleValue()));

							// PJ20950
							if (arbeitsgaengeBeiHilfsstuecklistenVerdichten == 1) {
								// Suchen, ob es bereits einen Arbeitsgang mit dersleben Artikelnummer, ohne
								// zugeordnetes Material und der gleichen Maschine gibt

								boolean bGleichePositionGefunden = false;
								for (int x = 0; x < alStuecklistePositionen.size(); x++) {
									StuecklistearbeitsplanDto apVorhanden = alStuecklistePositionen.get(x);

									if (apVorhanden.getArtikelIId().equals(unterstklPos[i].getArtikelIId())) {
										if (apVorhanden.getStuecklistepositionIId() == null
												&& unterstklPos[i].getStuecklistepositionIId() == null) {

											if ((apVorhanden.getMaschineIId() == null
													&& unterstklPos[i].getMaschineIId() == null)
													|| (apVorhanden.getMaschineIId() != null
															&& apVorhanden.getMaschineIId()
																	.equals(unterstklPos[i].getMaschineIId()))) {
												bGleichePositionGefunden = true;

												apVorhanden.setLRuestzeit(
														apVorhanden.getLRuestzeit() + unterstklPos[i].getLRuestzeit());
												apVorhanden.setLStueckzeit(apVorhanden.getLStueckzeit()
														+ unterstklPos[i].getLStueckzeit());

												if (apVorhanden.getCKommentar() == null) {
													apVorhanden.setCKommentar(unterstklPos[i].getCKommentar());
												} else {

													if (unterstklPos[i].getCKommentar() != null) {
														byte[] CRLFAscii = { 13, 10 };

														// SP7763 Wenn der neue Kommentar zu lang wird, wandert er
														// ins X_KOMMENTAR
														String cKommentarNeu = apVorhanden.getCKommentar()
																+ new String(CRLFAscii)
																+ unterstklPos[i].getCKommentar();

														if (cKommentarNeu.length() > 80) {
															apVorhanden.setXLangtext(apVorhanden.getXLangtext()
																	+ new String(CRLFAscii) + cKommentarNeu);
														} else {
															apVorhanden.setCKommentar(null);
														}

													}
												}

												if (apVorhanden.getXLangtext() == null) {
													apVorhanden.setXLangtext(unterstklPos[i].getXLangtext());
												} else {

													if (unterstklPos[i].getXLangtext() != null
															&& unterstklPos[i].getXLangtext().length() > 0) {

														byte[] CRLFAscii = { 13, 10 };

														if (apVorhanden.getXLangtext() != null
																&& apVorhanden.getXLangtext().length() == 0) {
															apVorhanden.setXLangtext(unterstklPos[i].getXLangtext());
														} else {
															apVorhanden.setXLangtext(
																	apVorhanden.getXLangtext() + new String(CRLFAscii)
																			+ unterstklPos[i].getXLangtext());
														}

													}
												}

												alStuecklistePositionen.set(x, apVorhanden);

												break;
											}

										}

									}

								}

								if (bGleichePositionGefunden == false) {
									alStuecklistePositionen.add(unterstklPos[i]);
								}

							} else if (arbeitsgaengeBeiHilfsstuecklistenVerdichten == 2) {
								// PJ22260

								// Suchen, ob es bereits einen Arbeitsgang mit derselben Artikelgruppe gibt,
								// alles anderen Eigenschaften sind egal

								boolean bGleichePositionGefunden = false;
								for (int x = 0; x < alStuecklistePositionen.size(); x++) {
									StuecklistearbeitsplanDto apVorhanden = alStuecklistePositionen.get(x);

									ArtikelDto artikelDtoVorhanden = getArtikelFac()
											.artikelFindByPrimaryKeySmall(apVorhanden.getArtikelIId(), theClientDto);
									ArtikelDto artikelDtoStkl = getArtikelFac().artikelFindByPrimaryKeySmall(
											unterstklPos[i].getArtikelIId(), theClientDto);

									// PJ22338 zusaetzlich muessen auch beide zugehoerigen Materialien leer sein
									if (artikelDtoVorhanden.getArtgruIId() != null
											&& artikelDtoVorhanden.getArtgruIId().equals(artikelDtoStkl.getArtgruIId())
											&& apVorhanden.getStuecklistepositionIId() == null
											&& unterstklPos[i].getStuecklistepositionIId() == null) {

										bGleichePositionGefunden = true;

										if (artikelDtoVorhanden
												.getIExternerArbeitsgang() != ArtikelFac.EXTERNER_ARBEITSGANG_KEIN) {

											if (apVorhanden.getLStueckzeit() == 0
													&& unterstklPos[i].getLStueckzeit() == 0) {
												if (unterstklPos[i].getLRuestzeit() >= apVorhanden.getLRuestzeit()) {
													apVorhanden.setLRuestzeit(unterstklPos[i].getLRuestzeit());
													unterstklPos[i].setLRuestzeit(0L);
												}
											} else {
												apVorhanden.setLRuestzeit(
														apVorhanden.getLRuestzeit() + unterstklPos[i].getLRuestzeit());
											}
										} else {
											apVorhanden.setLRuestzeit(
													apVorhanden.getLRuestzeit() + unterstklPos[i].getLRuestzeit());
										}

										apVorhanden.setLStueckzeit(
												apVorhanden.getLStueckzeit() + unterstklPos[i].getLStueckzeit());

										if (apVorhanden.getCKommentar() == null) {
											apVorhanden.setCKommentar(unterstklPos[i].getCKommentar());
										} else {

											if (unterstklPos[i].getCKommentar() != null) {
												byte[] CRLFAscii = { 13, 10 };

												// SP7763 Wenn der neue Kommentar zu lang wird, wandert er
												// ins X_KOMMENTAR
												String cKommentarNeu = apVorhanden.getCKommentar()
														+ new String(CRLFAscii) + unterstklPos[i].getCKommentar();

												if (cKommentarNeu.length() > 80) {
													apVorhanden.setXLangtext(apVorhanden.getXLangtext()
															+ new String(CRLFAscii) + cKommentarNeu);
												} else {
													apVorhanden.setCKommentar(null);
												}

											}
										}

										if (apVorhanden.getXLangtext() == null) {
											apVorhanden.setXLangtext(unterstklPos[i].getXLangtext());
										} else {

											if (unterstklPos[i].getXLangtext() != null
													&& unterstklPos[i].getXLangtext().length() > 0) {

												byte[] CRLFAscii = { 13, 10 };

												if (apVorhanden.getXLangtext() != null
														&& apVorhanden.getXLangtext().length() == 0) {
													apVorhanden.setXLangtext(unterstklPos[i].getXLangtext());
												} else {
													apVorhanden.setXLangtext(apVorhanden.getXLangtext()
															+ new String(CRLFAscii) + unterstklPos[i].getXLangtext());
												}

											}
										}

										alStuecklistePositionen.set(x, apVorhanden);

										break;

									}
									// PJ22338

									// Mit WH besprochen: Obwohl der Wert der Parameter = 2 ist, verdichten wir erst
									// im zweiten Schritt nach Artikelgruppen

									/*
									 * if (apVorhanden.getArtikelIId().equals(unterstklPos[i].getArtikelIId())) { if
									 * (apVorhanden.getStuecklistepositionIId() != null &&
									 * unterstklPos[i].getStuecklistepositionIId() != null) {
									 * 
									 * if (artikelDtoVorhanden .getIExternerArbeitsgang() !=
									 * ArtikelFac.EXTERNER_ARBEITSGANG_KEIN) {
									 * 
									 * if (apVorhanden.getLStueckzeit() == 0 && unterstklPos[i].getLStueckzeit() ==
									 * 0) { if (unterstklPos[i].getLRuestzeit() >= apVorhanden .getLRuestzeit()) {
									 * apVorhanden.setLRuestzeit(unterstklPos[i].getLRuestzeit());
									 * unterstklPos[i].setLRuestzeit(0L); alStuecklistePositionen.set(x,
									 * apVorhanden);
									 * 
									 * } } } } }
									 */

								}

								if (bGleichePositionGefunden == false) {
									alStuecklistePositionen.add(unterstklPos[i]);
								}

							} else {
								alStuecklistePositionen.add(unterstklPos[i]);
							}

						}

					}

				}

				// Nun den Beginntermin berechnen
				Integer iAgBeginnAutomatischErmittelnInStd = 0;
				boolean bReihenfolgenplanung = false;
				try {
					ParametermandantDto parameterM = getParameterFac().getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_AUTOMATISCHE_ERMITTLUNG_AG_BEGINN);
					iAgBeginnAutomatischErmittelnInStd = ((Integer) parameterM.getCWertAsObject());

					ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_REIHENFOLGENPLANUNG);
					bReihenfolgenplanung = (Boolean) parameter.getCWertAsObject();
				} catch (RemoteException ex) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
				}

				for (int j = 0; j < lossollarbeitsplanDtos.length; j++) {
					LossollarbeitsplanDto lossollarbeitsplanDto = lossollarbeitsplanDtos[j];

					for (int i = 0; i < alStuecklistePositionen.size(); i++) {
						// Wenn die Arbeitsgangnummer und die Artikelnummer
						// zusammenstimmen, dann wird aktualisert
						if (lossollarbeitsplanDto.getIArbeitsgangnummer()
								.equals(alStuecklistePositionen.get(i).getIArbeitsgang())) {

							boolean bUnterarbeitsgangStimmtZusammen = false;

							if (lossollarbeitsplanDto.getIUnterarbeitsgang() == null
									&& alStuecklistePositionen.get(i).getIUnterarbeitsgang() == null) {
								bUnterarbeitsgangStimmtZusammen = true;
							} else {
								if (lossollarbeitsplanDto.getIUnterarbeitsgang() != null
										&& lossollarbeitsplanDto.getIUnterarbeitsgang()
												.equals(alStuecklistePositionen.get(i).getIUnterarbeitsgang())) {
									bUnterarbeitsgangStimmtZusammen = true;
								}
							}

							if (lossollarbeitsplanDto.getArtikelIIdTaetigkeit()
									.equals(alStuecklistePositionen.get(i).getArtikelIId())
									&& bUnterarbeitsgangStimmtZusammen) {

								lossollarbeitsplanDto.setCKomentar(alStuecklistePositionen.get(i).getCKommentar());

								lossollarbeitsplanDto.setLRuestzeit(alStuecklistePositionen.get(i).getLRuestzeit());
								lossollarbeitsplanDto
										.setApkommentarIId(alStuecklistePositionen.get(i).getApkommentarIId());

								lossollarbeitsplanDto.setLStueckzeit(alStuecklistePositionen.get(i).getLStueckzeit());
								lossollarbeitsplanDto.setIAufspannung(alStuecklistePositionen.get(i).getIAufspannung());
								lossollarbeitsplanDto.setMaschineIId(alStuecklistePositionen.get(i).getMaschineIId());
								lossollarbeitsplanDto.setXText(alStuecklistePositionen.get(i).getXLangtext());
								lossollarbeitsplanDto.setIAufspannung(alStuecklistePositionen.get(i).getIAufspannung());
								lossollarbeitsplanDto.setNPpm(alStuecklistePositionen.get(i).getNPpm());
								lossollarbeitsplanDto
										.setBNurmaschinenzeit(alStuecklistePositionen.get(i).getBNurmaschinenzeit());
								lossollarbeitsplanDto.setAgartCNr(alStuecklistePositionen.get(i).getAgartCNr());
								lossollarbeitsplanDto.setIMaschinenversatztage(
										alStuecklistePositionen.get(i).getIMaschinenversatztage());
								if (lossollarbeitsplanDto.getMaschineIId() != null) {
									MaschineDto mDto = getZeiterfassungFac()
											.maschineFindByPrimaryKey(lossollarbeitsplanDto.getMaschineIId());
									lossollarbeitsplanDto.setBAutoendebeigeht(mDto.getBAutoendebeigeht());

								}

								if (alStuecklistePositionen.get(i).getStuecklistepositionIId() != null) {
									lossollarbeitsplanDto.setLossollmaterialIId(holeLossollmaterialIdAnhandStklPosition(
											losIId, alStuecklistePositionen.get(i).getStuecklistepositionIId(),
											theClientDto));
								} else {
									lossollarbeitsplanDto.setLossollmaterialIId(null);
								}

								if (iAgBeginnAutomatischErmittelnInStd != null
										&& iAgBeginnAutomatischErmittelnInStd.intValue() > 0
										&& bReihenfolgenplanung == false) {
									lossollarbeitsplanDto
											.setTAgbeginnBerechnet(new Timestamp(System.currentTimeMillis()));
								}

								updateLossollarbeitsplan(lossollarbeitsplanDto, false, theClientDto);

								updateGesamtzeit(lossollarbeitsplanDto.getIId(), losDto.getNLosgroesse(), theClientDto);

								alStuecklistePositionen.remove(i);
								break;

							}
						}

					}

				}

				// SP7534 Wegen verdichten nochmals neu nach AG/UAG sortieren

				for (int k = alStuecklistePositionen.size() - 1; k > 0; --k) {
					for (int j = 0; j < k; ++j) {
						StuecklistearbeitsplanDto a1 = (StuecklistearbeitsplanDto) alStuecklistePositionen.get(j);
						StuecklistearbeitsplanDto a2 = (StuecklistearbeitsplanDto) alStuecklistePositionen.get(j + 1);
						if (a1.getIArbeitsgang() > a2.getIArbeitsgang()) {
							alStuecklistePositionen.set(j, a2);
							alStuecklistePositionen.set(j + 1, a1);
						} else if (a1.getIArbeitsgang() == a2.getIArbeitsgang()) {
							if (a1.getIUnterarbeitsgang() != null && a2.getIUnterarbeitsgang() != null
									&& a1.getIUnterarbeitsgang() > a2.getIUnterarbeitsgang()) {
								alStuecklistePositionen.set(j, a2);
								alStuecklistePositionen.set(j + 1, a1);
							}
						}
					}
				}

				if (iAgBeginnAutomatischErmittelnInStd != null && iAgBeginnAutomatischErmittelnInStd.intValue() > 0
						&& bReihenfolgenplanung == false) {

					// Firmenzeitmodell ermitteln

					Integer zeitmodellIId = null;

					Query queryFZ = em.createNamedQuery("ZeitmodellfindByBFirmenzeitmodellMandantCNr");
					queryFZ.setParameter(1, new Short((short) 1));
					queryFZ.setParameter(2, theClientDto.getMandant());
					// @todo getSingleResult oder getResultList ?
					Collection c = queryFZ.getResultList();
					if (c.size() > 0) {

						zeitmodellIId = ((Zeitmodell) c.iterator().next()).getIId();

					} else {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FIRMENZEITMODELL_NICHT_VORHANDEN,
								new Exception("FEHLER_FIRMENZEITMODELL_NICHT_VORHANDEN"));
					}

					Calendar cTempBeginn = Calendar.getInstance();
					cTempBeginn.setTimeInMillis(Helper.cutDate(losDto.getTProduktionsende()).getTime());

					int iAnzahlSlotsBenoetigtGesamt = 0;
					for (int i = alStuecklistePositionen.size(); i > 0; i--) {

						StuecklistearbeitsplanDto stklAPDto = alStuecklistePositionen.get(i - 1);

						BigDecimal bdGesamtzeit = Helper.berechneGesamtzeitInStunden(stklAPDto.getLRuestzeit(),
								stklAPDto.getLStueckzeitDurchMitarbeitergleichzeitig(), losDto.getNLosgroesse(), null,
								stklAPDto.getIAufspannung());

						int iAnzahlSlotsAufDerPosition = (int) Math
								.ceil(bdGesamtzeit.doubleValue() / iAgBeginnAutomatischErmittelnInStd);

						iAnzahlSlotsBenoetigtGesamt += iAnzahlSlotsAufDerPosition;

					}

					ArrayList<java.util.Date> alSlots = new ArrayList<java.util.Date>();

					int iZahler = 0;
					while (alSlots.size() < iAnzahlSlotsBenoetigtGesamt) {
						iZahler++;

						cTempBeginn.add(Calendar.DATE, -1);

						ZeitmodelltagDto zmtagDto = getZeiterfassungFac().getZeitmodelltagFirmenzeitmodellZuDatum(
								zeitmodellIId, new Timestamp(cTempBeginn.getTimeInMillis()), theClientDto);

						if (zmtagDto != null && zmtagDto.getUSollzeit() != null) {

							double dSollzeit = Helper.time2Double(zmtagDto.getUSollzeit());

							int iAnzahlHabenPlatz = (int) Math.floor(dSollzeit / iAgBeginnAutomatischErmittelnInStd);

							for (int i = 0; i < iAnzahlHabenPlatz; i++) {
								alSlots.add(cTempBeginn.getTime());
							}

						}

						if (iZahler > 1000) {
							// Fehler kann auftreten, wenn unzureichend Sollzeit
							// definiert ist, z.b. Wenn die Slots auf 4 Stunden
							// eingestellt sind, es jedoch nur Sollzeiten unter
							// 4 Stunden gibt
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_FIRMENZEITMODELL_NICHT_AUSREICHEND_SOLLZEIT_DEFINIERT,
									new Exception("FEHLER_FIRMENZEITMODELL_NICHT_AUSREICHEND_SOLLZEIT_DEFINIERT"));

						}

					}

					java.sql.Timestamp tBeginntermin = Helper
							.cutTimestamp(new Timestamp(cTempBeginn.getTimeInMillis()));

					losDto.setTProduktionsbeginn(new java.sql.Date(tBeginntermin.getTime()));

					updateLos(losDto, theClientDto);

					for (int i = alStuecklistePositionen.size(); i > 0; i--) {

						StuecklistearbeitsplanDto stklAPDto = alStuecklistePositionen.get(i - 1);

						BigDecimal bdGesamtzeit = Helper.berechneGesamtzeitInStunden(stklAPDto.getLRuestzeit(),
								stklAPDto.getLStueckzeitDurchMitarbeitergleichzeitig(), losDto.getNLosgroesse(), null,
								stklAPDto.getIAufspannung());

						int iAnzahlSlotsAufDerPosition = (int) Math
								.ceil(bdGesamtzeit.doubleValue() / iAgBeginnAutomatischErmittelnInStd);

						java.util.Date agBeginn = null;
						for (int j = 0; j < iAnzahlSlotsAufDerPosition; j++) {

							agBeginn = alSlots.get(0);

							alSlots.remove(0);

						}
						int iAgBeginn = 0;

						if (agBeginn != null) {
							iAgBeginn = Helper.ermittleTageEinesZeitraumes(tBeginntermin,
									new java.sql.Date(agBeginn.getTime()));
						}

						stklAPDto.setIMaschinenversatztage(iAgBeginn);

						alStuecklistePositionen.set(i - 1, stklAPDto);

					}

				}

				StuecklisteDto stklDto = null;
				if (losDto.getStuecklisteIId() != null) {
					stklDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(), theClientDto);
				}

				// Alle die nicht gefunden wurden, neu eintragen
				for (int i = 0; i < alStuecklistePositionen.size(); i++) {
					LossollarbeitsplanDto losZeitDto = new LossollarbeitsplanDto();
					losZeitDto.setArtikelIIdTaetigkeit(alStuecklistePositionen.get(i).getArtikelIId());
					losZeitDto.setBNachtraeglich(Helper.boolean2Short(false));
					losZeitDto.setCKomentar(alStuecklistePositionen.get(i).getCKommentar());
					losZeitDto.setApkommentarIId(alStuecklistePositionen.get(i).getApkommentarIId());
					losZeitDto.setIArbeitsgangnummer(alStuecklistePositionen.get(i).getIArbeitsgang());
					losZeitDto.setIUnterarbeitsgang(alStuecklistePositionen.get(i).getIUnterarbeitsgang());
					losZeitDto.setLosIId(losIId);
					losZeitDto.setLRuestzeit(alStuecklistePositionen.get(i).getLRuestzeit());

					// SP1413
					StuecklisteDto stklDtoPosition = getStuecklisteFac().stuecklisteFindByPrimaryKey(
							alStuecklistePositionen.get(i).getStuecklisteIId(), theClientDto);
					if (stklDtoPosition.getNErfassungsfaktor().doubleValue() != 0) {
						losZeitDto.setLStueckzeit((long) (alStuecklistePositionen.get(i).getLStueckzeit()
								/ stklDtoPosition.getNErfassungsfaktor().doubleValue()));
					} else {
						losZeitDto.setLStueckzeit(alStuecklistePositionen.get(i).getLStueckzeit());
					}

					losZeitDto.setBNurmaschinenzeit(alStuecklistePositionen.get(i).getBNurmaschinenzeit());
					losZeitDto.setMaschineIId(alStuecklistePositionen.get(i).getMaschineIId());

					if (stklDto != null && !stklDto.getMandantCNr().equals(theClientDto.getMandant())
							&& losZeitDto.getMaschineIId() != null) {
						// Maschine suchen
						MaschineDto mDto = getZeiterfassungFac().maschineFindByPrimaryKey(losZeitDto.getMaschineIId());

						MaschineDto mDtoEigenerMandant = getZeiterfassungFac()
								.maschinefindByCIdentifikationsnrMandantCNrOhneExc(mDto.getCIdentifikationsnr(),
										theClientDto.getMandant());

						if (mDtoEigenerMandant != null) {
							losZeitDto.setMaschineIId(mDtoEigenerMandant.getIId());
						} else {
							losZeitDto.setMaschineIId(null);
						}
					}

					losZeitDto.setXText(alStuecklistePositionen.get(i).getXLangtext());
					losZeitDto.setIAufspannung(alStuecklistePositionen.get(i).getIAufspannung());
					losZeitDto.setNPpm(alStuecklistePositionen.get(i).getNPpm());
					losZeitDto.setAgartCNr(alStuecklistePositionen.get(i).getAgartCNr());
					losZeitDto.setBFertig(Helper.boolean2Short(false));

					if (alStuecklistePositionen.get(i).getMaschineIId() != null) {
						MaschineDto mDto = getZeiterfassungFac()
								.maschineFindByPrimaryKey(alStuecklistePositionen.get(i).getMaschineIId());
						losZeitDto.setBAutoendebeigeht(mDto.getBAutoendebeigeht());

					} else {
						losZeitDto.setBAutoendebeigeht(Helper.boolean2Short(false));
					}

					if (alStuecklistePositionen.get(i).getStuecklistepositionIId() != null) {
						losZeitDto.setLossollmaterialIId(holeLossollmaterialIdAnhandStklPosition(losIId,
								alStuecklistePositionen.get(i).getStuecklistepositionIId(), theClientDto));
					}

					losZeitDto.setIMaschinenversatztage(alStuecklistePositionen.get(i).getIMaschinenversatztage());

					if (iAgBeginnAutomatischErmittelnInStd != null && iAgBeginnAutomatischErmittelnInStd.intValue() > 0
							&& bReihenfolgenplanung == false) {
						losZeitDto.setTAgbeginnBerechnet(new Timestamp(System.currentTimeMillis()));
					}

					LossollarbeitsplanDto newDto = createLossollarbeitsplan(losZeitDto, theClientDto);

					updateGesamtzeit(newDto.getIId(), losDto.getNLosgroesse(), theClientDto);

				}

				automatischeErmittlungBeginnterminOffset(losIId, null, theClientDto);

			} else {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FERTIGUNG_DARF_FUER_MATERIALLISTE_NICHT_DURCHGEFUEHRT_WERDEN,
						new Exception("Material fuer freie Materiallisten kann nicht aktualisiert werden"));
			}
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT,
					new Exception("los " + losDto.getCNr() + " ist storniert"));
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
					new Exception("los " + losDto.getCNr() + " ist bereits erledigt"));
		}

	}

	private void automatischeErmittlungBeginnterminOffset(Integer losIId, Integer lossollarbeitsplanIId,
			TheClientDto theClientDto) {
		// PJ22260
		try {
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_AUTOMATISCHE_ERMITTLUNG_BEGINNTERMINOFFSET);
			boolean bAutoErmittlungBeginnterminOffset = (Boolean) parameter.getCWertAsObject();

			if (bAutoErmittlungBeginnterminOffset) {
				HashSet hsArtikelgruppeIIdBereitsVerwendet = new HashSet<Integer>();

				LossollarbeitsplanDto[] sollapDtos = null;
				if (lossollarbeitsplanIId != null) {
					sollapDtos = new LossollarbeitsplanDto[] {
							lossollarbeitsplanFindByPrimaryKey(lossollarbeitsplanIId) };
				} else {
					sollapDtos = lossollarbeitsplanFindByLosIId(losIId);
				}

				for (int i = 0; i < sollapDtos.length; i++) {
					LossollarbeitsplanDto sapDto = sollapDtos[i];

					if (sapDto.getIMaschinenversatztage() != null && sapDto.getIMaschinenversatztage() > 0) {
						ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(sapDto.getArtikelIIdTaetigkeit(),
								theClientDto);
						if (aDto.getArtgruIId() != null) {
							if (!hsArtikelgruppeIIdBereitsVerwendet.contains(aDto.getArtgruIId())) {

								SessionFactory factory = FLRSessionFactory.getFactory();
								Session sessionMaterial = factory.openSession();
								String sQuery = "select sm FROM FLRLossollmaterial sm WHERE sm.los_i_id=" + losIId
										+ " AND sm.flrartikel.flrartikelgruppe.i_id=" + aDto.getArtgruIId();

								org.hibernate.Query queryMaterial = sessionMaterial.createQuery(sQuery);

								List<?> resultMaterial = queryMaterial.list();
								Iterator itMaterial = resultMaterial.iterator();
								while (itMaterial.hasNext()) {
									FLRLossollmaterial sm = (FLRLossollmaterial) itMaterial.next();

									Lossollmaterial lossollmaterial = em.find(Lossollmaterial.class, sm.getI_id());
									lossollmaterial.setIBeginnterminoffset(sapDto.getIMaschinenversatztage());
									em.merge(lossollmaterial);
									em.flush();
								}

								hsArtikelgruppeIIdBereitsVerwendet.add(aDto.getArtgruIId());
							}

						}
					}
				}
			}

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
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

	private void updateGesamtzeit(Integer lossollzeitenIId, BigDecimal bdLosgroesse, TheClientDto theClientDto) {
		// try {
		Lossollarbeitsplan l = em.find(Lossollarbeitsplan.class, lossollzeitenIId);
		if (l == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
		}

		BigDecimal bdGesamtzeit = Helper.berechneGesamtzeitInStunden(l.getLRuestzeit(), l.getLStueckzeit(),
				bdLosgroesse, null, l.getIAufspannung());
		l.setNGesamtzeit(bdGesamtzeit);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	public void bucheMaterialAufLos(LosDto losDto, LossollmaterialDto[] sollmat, BigDecimal menge, boolean bHandausgabe,
			boolean bNurFehlmengenAnlegenUndReservierungenLoeschen, boolean bUnterstuecklistenAbbuchen,
			TheClientDto theClientDto, ArrayList<BucheSerienChnrAufLosDto> bucheSerienChnrAufLosDtos,
			boolean throwExceptionWhenCreate) {

		try {
			LosablieferungDto[] dtos = losablieferungFindByLosIId(losDto.getIId(), false, theClientDto);
			BigDecimal bdBereitsabgeliefert = new BigDecimal(0);
			for (int i = 0; i < dtos.length; i++) {
				bdBereitsabgeliefert = bdBereitsabgeliefert.add(dtos[i].getNMenge());
			}

			// PJ18216
			boolean bNichtLagerbewSofortAusgeben = false;
			try {
				ParametermandantDto parameterM = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG,
						ParameterFac.PARAMETER_NICHT_LAGERBEWIRTSCHAFTETE_SOFORT_AUSGEBEN);
				bNichtLagerbewSofortAusgeben = ((Boolean) parameterM.getCWertAsObject()).booleanValue();

			} catch (RemoteException ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			}

			// PJ18290
			int bImmerAusreichendVerfuegbar = 0;
			try {
				ParametermandantDto parameterM = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_LAGER_IMMER_AUSREICHEND_VERFUEGBAR);
				bImmerAusreichendVerfuegbar = ((Integer) parameterM.getCWertAsObject());

			} catch (RemoteException ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			}

			// Laeger des Loses
			LoslagerentnahmeDto[] laeger = loslagerentnahmeFindByLosIId(losDto.getIId());
			// nun vom lager abbuchen
			for (int i = 0; i < sollmat.length; i++) {
				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(sollmat[i].getArtikelIId(),
						theClientDto);
				// seriennummerntragende werden jetzt gar nicht gebucht

				if (!bNurFehlmengenAnlegenUndReservierungenLoeschen) {

					// SP6205
					if (bImmerAusreichendVerfuegbar == 0) {
						if (throwExceptionWhenCreate) {

							if (sollmat[i].getNMenge().doubleValue() > 0 && artikelDto.isLagerbewirtschaftet()) {
								BigDecimal bdLagerstandGesamtAufLosLaegern = BigDecimal.ZERO;
								for (int j = 0; j < laeger.length; j++) {

									bdLagerstandGesamtAufLosLaegern = bdLagerstandGesamtAufLosLaegern.add(getLagerFac()
											.getLagerstand(artikelDto.getIId(), laeger[j].getLagerIId(), theClientDto));

								}

								if (bdLagerstandGesamtAufLosLaegern.doubleValue() < sollmat[i].getNMenge()
										.doubleValue()) {
									throw new EJBExceptionLP(
											EJBExceptionLP.FEHLER_FERTIGUNG_AUSGABE_ES_WUERDEN_FEHLMENGEN_ENTSTEHEN,
											"FEHLER_FERTIGUNG_AUSGABE_ES_WUERDEN_FEHLMENGEN_ENTSTEHEN");
								}
							}
						}
					}

					if (!bHandausgabe || (bHandausgabe == true && bNichtLagerbewSofortAusgeben == true)
							|| Helper.short2boolean(sollmat[i].getBRuestmenge())) {

						// PJ18216
						if (bHandausgabe == true && bNichtLagerbewSofortAusgeben == true
								&& Helper.short2boolean(artikelDto.getBLagerbewirtschaftet())) {
							// Dann nichts machen
						} else {

							StuecklisteDto stuecklisteDto = getStuecklisteFac()
									.stuecklisteFindByMandantCNrArtikelIIdOhneExc(artikelDto.getIId(), theClientDto);
							if (bUnterstuecklistenAbbuchen || stuecklisteDto == null) {

								BigDecimal bdAbzubuchendeMenge = BigDecimal.ZERO;
								if (menge == null) {
									bdAbzubuchendeMenge = sollmat[i].getNMenge();
								} else {
									if (losDto.getNLosgroesse().doubleValue() != 0) {
										BigDecimal sollsatzGroesse = sollmat[i].getNMenge()
												.divide(losDto.getNLosgroesse(), 10, BigDecimal.ROUND_HALF_EVEN);

										BigDecimal bdBereitsausgegeben = getAusgegebeneMenge(sollmat[i].getIId(), null,
												theClientDto);
										BigDecimal bdGesamtmenge = Helper.rundeKaufmaennisch(
												bdBereitsabgeliefert.add(menge).multiply(sollsatzGroesse), 4);

										if (Helper.short2boolean(sollmat[i].getBRuestmenge())
												&& bdGesamtmenge.doubleValue() > sollmat[i].getNMenge().doubleValue()) {
											bdGesamtmenge = sollmat[i].getNMenge();
										}
										if (bdGesamtmenge.subtract(bdBereitsausgegeben).doubleValue() > 0) {
											bdAbzubuchendeMenge = bdGesamtmenge.subtract(bdBereitsausgegeben);
										} else {
											if (bdGesamtmenge.doubleValue() < 0) {
												bdAbzubuchendeMenge = bdGesamtmenge.subtract(bdBereitsausgegeben).abs();

											}
										}

										// SP4820 Abzubuchende Menge auf 3
										// Nachkommastellen runden
										bdAbzubuchendeMenge = Helper.rundeKaufmaennisch(bdAbzubuchendeMenge, 4);

									}
								}

								// wg. PJ 15370
								if (sollmat[i].getNMenge().doubleValue() > 0) {

									if (!Helper.short2boolean(artikelDto.getBSeriennrtragend())
											&& !Helper.short2boolean(artikelDto.getBChargennrtragend())) {
										for (int j = 0; j < laeger.length; j++) {
											if (bdAbzubuchendeMenge.compareTo(BigDecimal.ZERO) <= 0) {
												// Nichts mehr abzubuchen
												break;
											}
											BigDecimal bdLagerstand = null;
											boolean lagerUnbegrenzt = false;
											if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet())
													&& bImmerAusreichendVerfuegbar == 0) {
												bdLagerstand = getLagerFac().getLagerstand(artikelDto.getIId(),
														laeger[j].getLagerIId(), theClientDto);
											} else {
												lagerUnbegrenzt = true;
											}
											// wenn ein lagerstand da ist
											BigDecimal bdMengeVonLager = lagerUnbegrenzt ? bdAbzubuchendeMenge
													: bdLagerstand.min(bdAbzubuchendeMenge);
											if (bdMengeVonLager.compareTo(BigDecimal.ZERO) > 0) {
												LosistmaterialDto istmat = new LosistmaterialDto();
												istmat.setLagerIId(laeger[j].getLagerIId());
												istmat.setLossollmaterialIId(sollmat[i].getIId());
												istmat.setNMenge(bdMengeVonLager);

												if (sollmat[i].getNMenge().doubleValue() > 0) {
													istmat.setBAbgang(Helper.boolean2Short(true));
												} else {
													istmat.setBAbgang(Helper.boolean2Short(false));
												}

												// ist-wert anlegen und
												// lagerbuchung durchfuehren
												createLosistmaterial(istmat, null, theClientDto);
												// menge reduzieren
												bdAbzubuchendeMenge = bdAbzubuchendeMenge.subtract(bdMengeVonLager);
											}
										}
										// SP6802 Ruestmenge muss bei der Ausagbe komplett auf Lager sein
										if (Helper.short2boolean(sollmat[i].getBRuestmenge())) {
											if (bdAbzubuchendeMenge.compareTo(BigDecimal.ZERO) > 0) {
												boolean bRuestmengeDarfFehlmengeBuchen = false;
												try {
													ParametermandantDto parameterRM = getParameterFac()
															.getMandantparameter(theClientDto.getMandant(),
																	ParameterFac.KATEGORIE_FERTIGUNG,
																	ParameterFac.PARAMETER_RUESTMENGE_DARF_FEHLMENGE_BUCHEN);
													bRuestmengeDarfFehlmengeBuchen = ((Boolean) parameterRM
															.getCWertAsObject()).booleanValue();

												} catch (RemoteException ex) {
													throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
												}
												if (bRuestmengeDarfFehlmengeBuchen == false) {
													ArrayList<Object> alDaten = new ArrayList<>();
													alDaten.add(sollmat[i].getNMenge());
													alDaten.add(artikelDto.getCNr());
													alDaten.add(losDto.getCNr());
													throw new EJBExceptionLP(
															EJBExceptionLP.FEHLER_LOSAUSGABE_RUESTMENGE_NICHT_AUF_LAGER,
															alDaten, new Exception(
																	"FEHLER_LOSAUSGABE_RUESTMENGE_NICHT_AUF_LAGER"));
												}
											}
										}

									} else {
										if (bucheSerienChnrAufLosDtos != null) {
											for (int j = 0; j < bucheSerienChnrAufLosDtos.size(); j++) {
												BucheSerienChnrAufLosDto dtoTemp = bucheSerienChnrAufLosDtos.get(j);

												if (dtoTemp.getLossollmaterialIId().equals(sollmat[i].getIId())) {

													LosistmaterialDto istmat = new LosistmaterialDto();
													istmat.setLagerIId(dtoTemp.getLagerIId());
													istmat.setLossollmaterialIId(dtoTemp.getLossollmaterialIId());
													istmat.setNMenge(dtoTemp.getNMenge());
													if (sollmat[i].getNMenge().doubleValue() > 0) {
														istmat.setBAbgang(Helper.boolean2Short(true));
													} else {
														istmat.setBAbgang(Helper.boolean2Short(false));
													}
													// ist-wert anlegen und
													// lagerbuchung
													// durchfuehren
													createLosistmaterial(istmat, dtoTemp.getCSeriennrChargennr(),
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
				getFehlmengeFac().aktualisiereFehlmenge(LocaleFac.BELEGART_LOS, sollmat[i].getIId(), theClientDto);
			}

		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
		catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

	}

	public void bucheMaterialAufLos(LosDto losDto, BigDecimal menge, boolean bHandausgabe,
			boolean bNurFehlmengenAnlegenUndReservierungenLoeschen, boolean bUnterstuecklistenAbbuchen,
			TheClientDto theClientDto, ArrayList<BucheSerienChnrAufLosDto> bucheSerienChnrAufLosDtos,
			boolean throwExceptionWhenCreate) {

		Query query = em.createNamedQuery("LossollmaterialfindByLosIId");
		query.setParameter(1, losDto.getIId());
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		LossollmaterialDto[] sollmat = assembleLossollmaterialDtos(cl);

		bucheMaterialAufLos(losDto, sollmat, menge, bHandausgabe, bNurFehlmengenAnlegenUndReservierungenLoeschen,
				bUnterstuecklistenAbbuchen, theClientDto, bucheSerienChnrAufLosDtos, throwExceptionWhenCreate);
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void nachtraeglichGeoeffneteLoseErledigen(TheClientDto theClientDto) {
		Session session = FLRSessionFactory.getFactory().openSession();

		Criteria crit = session.createCriteria(FLRLosReport.class);
		String[] stati = new String[4];
		stati[0] = FertigungFac.STATUS_IN_PRODUKTION;
		stati[0] = FertigungFac.STATUS_TEILERLEDIGT;
		crit.add(Restrictions.in(FertigungFac.FLR_LOS_STATUS_C_NR, stati));
		crit.add(Restrictions.isNotNull(FertigungFac.FLR_LOSREPORT_T_NACHTRAEGLICH_GEOEFFNET));
		crit.add(Restrictions.eq(FertigungFac.FLR_LOS_MANDANT_C_NR, theClientDto.getMandant()));

		List<?> resultList = crit.list();
		Iterator<?> resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {
			FLRLosReport flrLos = (FLRLosReport) resultListIterator.next();
			try {
				getFertigungFac().manuellErledigen(flrLos.getI_id(), false, theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}

		session.close();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public RueckgabeMehrereLoseAusgeben gebeMehrereLoseAus(Integer fertigungsgruppeIId,
			boolean throwExceptionWhenCreate, boolean bAufAktualisierungPruefen, TheClientDto theClientDto) {

		RueckgabeMehrereLoseAusgeben rueckgabe = new RueckgabeMehrereLoseAusgeben();

		ArrayList<Integer> ausgebebeneLose = new ArrayList<Integer>();

		Session session = FLRSessionFactory.getFactory().openSession();

		Criteria crit = session.createCriteria(FLRLosReport.class);

		String s = null;

		String[] stati = new String[4];
		stati[0] = FertigungFac.STATUS_ANGELEGT;
		crit.add(Restrictions.in(FertigungFac.FLR_LOS_STATUS_C_NR, stati));
		crit.add(Restrictions.eq(FertigungFac.FLR_LOSREPORT_FERTIGUNGSGRUPPE_I_ID, fertigungsgruppeIId));
		crit.add(Restrictions.eq(FertigungFac.FLR_LOS_MANDANT_C_NR, theClientDto.getMandant()));
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
							.stuecklisteFindByPrimaryKey(flrLos.getStueckliste_i_id(), theClientDto);
					// ist der Arbeitsplan der Stueckliste aktueller als der des
					// Loses?
					if (flrLos.getT_aktualisierungarbeitszeit() != null
							&& stklDto.getTAendernarbeitsplan().after(flrLos.getT_aktualisierungarbeitszeit())) {
						bGeandert = true;
					}

					if (flrLos.getT_aktualisierungstueckliste() != null
							&& stklDto.getTAendernposition().after(flrLos.getT_aktualisierungstueckliste())) {
						bGeandert = true;
					}

					if (bGeandert == true) {
						if (bKopfzeileEinfuegen == true) {
							bKopfzeileEinfuegen = false;
							s = "<font size='3' face='Monospaced'>";

							s += Helper.fitString2Length("Losnummer", 12, '\u00A0');
							s += Helper.fitString2Length("Stkl.Nr.", 25, '\u00A0');
							s += Helper.fitString2Length("Stkl.Bez.", 40, '\u00A0');
							s += Helper.fitString2Length("Anlagedatum", 14, '\u00A0');
							s += Helper.fitString2Length("\u00C4nd.Dat.Stkl.", 14, '\u00A0');

							s += Helper.fitString2Length("\u00C4nd.Dat.AP", 14, '\u00A0');

							s += "<br>";
							s += "<br>";

						}

						s += Helper.fitString2Length(flrLos.getC_nr(), 12, '\u00A0');

						s += Helper.fitString2Length(stklDto.getArtikelDto().getCNr(), 25, '\u00A0');
						if (stklDto.getArtikelDto().getArtikelsprDto() != null) {
							s += Helper.fitString2Length(stklDto.getArtikelDto().getArtikelsprDto().getCBez(), 40,
									'\u00A0');
						} else {
							s += Helper.fitString2Length("", 40, '\u00A0');
						}
						s += Helper.fitString2Length(Helper.formatDatum(flrLos.getT_anlegen(), theClientDto.getLocUi()),
								14, '\u00A0');
						s += Helper.fitString2Length(
								Helper.formatDatum(stklDto.getTAendernposition(), theClientDto.getLocUi()), 14,
								'\u00A0');
						s += Helper.fitString2Length(
								Helper.formatDatum(stklDto.getTAendernarbeitsplan(), theClientDto.getLocUi()), 14,
								'\u00A0');
						s += "<br>";

					}
				}

			} else {
				try {
					EJBExceptionLP returnedExc = context.getBusinessObject(FertigungFac.class)
							.gebeLosAus(flrLos.getI_id(), false, throwExceptionWhenCreate, theClientDto, null);
					if (returnedExc != null)
						rueckgabe.getLosausgabeReturnedExc().add(returnedExc);
					ausgebebeneLose.add(flrLos.getI_id());
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

		}

		rueckgabe.setMeldungZuAktualisieren(s);
		rueckgabe.setAlAusgegeben(ausgebebeneLose);
		return rueckgabe;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public RueckgabeMehrereLoseAusgeben gebeAlleLoseBisZumBeginnterminaus(java.sql.Date tBeginn,
			boolean throwExceptionWhenCreate, TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();

		Criteria crit = session.createCriteria(FLRLosReport.class);

		ArrayList<Integer> al = new ArrayList<Integer>();
		RueckgabeMehrereLoseAusgeben rueckgabe = new RueckgabeMehrereLoseAusgeben();

		String[] stati = new String[4];
		stati[0] = FertigungFac.STATUS_ANGELEGT;
		crit.add(Restrictions.in(FertigungFac.FLR_LOS_STATUS_C_NR, stati));
		crit.add(Restrictions.le(FertigungFac.FLR_LOS_T_PRODUKTIONSBEGINN, tBeginn));
		crit.add(Restrictions.eq(FertigungFac.FLR_LOS_MANDANT_C_NR, theClientDto.getMandant()));

		List<?> resultList = crit.list();
		Iterator<?> resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			FLRLosReport flrLos = (FLRLosReport) resultListIterator.next();

			try {
				EJBExceptionLP returnedExc = context.getBusinessObject(FertigungFac.class).gebeLosAus(flrLos.getI_id(),
						false, throwExceptionWhenCreate, theClientDto, null);
				if (returnedExc != null)
					rueckgabe.getLosausgabeReturnedExc().add(returnedExc);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			al.add(flrLos.getI_id());

		}

		rueckgabe.setAlAusgegeben(al);
		return rueckgabe;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public RueckgabeMehrereLoseAusgeben gebeMehrereLoseAus(Object[] losIIds, boolean throwExceptionWhenCreate,
			TheClientDto theClientDto) {

		ArrayList<Integer> al = new ArrayList<Integer>();
		RueckgabeMehrereLoseAusgeben rueckgabe = new RueckgabeMehrereLoseAusgeben();

		for (int i = 0; i < losIIds.length; i++) {

			try {

				LosDto lDto = getFertigungFac().losFindByPrimaryKey((Integer) losIIds[i]);

				if (lDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)
						&& lDto.getMandantCNr().equals(theClientDto.getMandant())) {

					EJBExceptionLP returnedExc = context.getBusinessObject(FertigungFac.class).gebeLosAus(lDto.getIId(),
							false, throwExceptionWhenCreate, theClientDto, null);
					al.add(lDto.getIId());
					if (returnedExc != null)
						rueckgabe.getLosausgabeReturnedExc().add(returnedExc);
				}
			} catch (EJBExceptionLP e1) {
				if (e1.getCode() == EJBExceptionLP.FEHLER_LOSAUSGABE_NUR_VON_KOMISSIONIERTERMINAL_MOEGLICH) {
					// lt. SP5369 aulassen
				} else {
					throw e1;
				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}
		rueckgabe.setAlAusgegeben(al);
		return rueckgabe;
	}

	@Override
	@org.jboss.ejb3.annotation.TransactionTimeout(6000)
	public AblieferungByAuftragResultDto perAuftragsnummerLoseAbliefern(Integer auftragIId, TheClientDto theClientDto) {
		AblieferungByAuftragResultDto resultDto = new AblieferungByAuftragResultDto();

		// Alle
		try {

			AuftragpositionDto[] dtos = getAuftragpositionFac().auftragpositionFindByAuftragOffeneMenge(auftragIId);
			ArrayList<Object> alGehtNicht = new ArrayList<Object>();
			for (int i = 0; i < dtos.length; i++) {
				AuftragpositionDto auftragpositionDto = dtos[i];

				if (auftragpositionDto.getArtikelIId() != null) {
					StuecklisteDto stuecklisteDto = getStuecklisteFac().stuecklisteFindByMandantCNrArtikelIIdOhneExc(
							auftragpositionDto.getArtikelIId(), theClientDto);
					if (stuecklisteDto != null
							&& Helper.short2boolean(stuecklisteDto.getBMaterialbuchungbeiablieferung()) == true) {
						ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(stuecklisteDto.getArtikelIId(),
								theClientDto);

						BigDecimal nMengeOffen = auftragpositionDto.getNOffeneMenge();

						Session session = FLRSessionFactory.getFactory().openSession();

						Criteria crit = session.createCriteria(FLRLosReport.class);

						String[] stati = new String[2];
						stati[0] = FertigungFac.STATUS_IN_PRODUKTION;
						stati[1] = FertigungFac.STATUS_TEILERLEDIGT;
						crit.add(Restrictions.in(FertigungFac.FLR_LOS_STATUS_C_NR, stati));
						crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));
						crit.add(Restrictions.eq(FertigungFac.FLR_LOS_STUECKLISTE_I_ID, stuecklisteDto.getIId()));

						crit.addOrder(Order.asc("c_nr"));

						List<?> resultList = crit.list();
						Iterator<?> resultListIterator = resultList.iterator();

						while (resultListIterator.hasNext() && nMengeOffen.doubleValue() > 0) {
							FLRLosReport flrLos = (FLRLosReport) resultListIterator.next();

							BigDecimal bdOffenLos = flrLos.getN_losgroesse()
									.subtract(getFertigungFac().getErledigteMenge(flrLos.getI_id(), theClientDto));
							if (bdOffenLos.doubleValue() > 0) {

								boolean bErledigt = false;

								LosablieferungDto losablieferungDto = new LosablieferungDto();
								losablieferungDto.setLosIId(flrLos.getI_id());

								if (bdOffenLos.doubleValue() >= nMengeOffen.doubleValue()) {
									losablieferungDto.setNMenge(nMengeOffen);
									if (bdOffenLos.doubleValue() == nMengeOffen.doubleValue()) {
										bErledigt = true;
									}
									nMengeOffen = new BigDecimal(0);

								} else {
									losablieferungDto.setNMenge(bdOffenLos);
									nMengeOffen = nMengeOffen.subtract(bdOffenLos);
									bErledigt = true;
								}
								// Materialbuchen
								bucheMaterialAufLos(losFindByPrimaryKey(flrLos.getI_id()),
										losablieferungDto.getNMenge(), false, false, true, theClientDto, null, false);

								// Sind sollsatzgroeszen unterschritten?
								// Sollsatzgroessen pruefen
								pruefePositionenMitSollsatzgroesseUnterschreitung(flrLos.getI_id(),
										losablieferungDto.getNMenge(), theClientDto);

								LosablieferungResultDto laResultDto = createLosablieferung(losablieferungDto,
										theClientDto, bErledigt);
								resultDto.getAbgelieferteLose()
										.add(new Pair<Integer, String>(flrLos.getI_id(), flrLos.getC_nr()));
								if (laResultDto.getEjbExceptionLP() != null)
									resultDto.getLoserledigungReturnedExc().add(laResultDto.getEjbExceptionLP());
							}
						}

						if (nMengeOffen.doubleValue() > 0) {
							alGehtNicht.add(artikelDto.formatArtikelbezeichnung() + " "
									+ Helper.formatZahl(nMengeOffen, 4, theClientDto.getLocUi()) + " "
									+ artikelDto.getEinheitCNr());
						}

					}
				}

			}

			if (alGehtNicht.size() > 0) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ABLIEFERN_PER_AUFTRAGSNUMMER_NICHT_MOEGLICH, alGehtNicht,
						new Exception("Zuwenig offene Lose"));

			}

			return resultDto;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return null;
	}

	// PJ18741
	public TreeSet<Integer> getLoseEinesStuecklistenbaums(Integer losIId, TheClientDto theClientDto) {

		TreeSet<Integer> ts = new TreeSet<Integer>();
		ts.add(losIId);
		ts = getLoseEinesStuecklistenbaums(losIId, ts, theClientDto);
		return ts;
	}

	private TreeSet<Integer> getLoseEinesStuecklistenbaums(Integer losIId, TreeSet<Integer> ts,
			TheClientDto theClientDto) {

		LossollmaterialDto[] sollDtos = lossollmaterialFindByLosIId(losIId);
		for (int i = 0; i < sollDtos.length; i++) {

			StuecklisteDto stklDto = getStuecklisteFac()
					.stuecklisteFindByMandantCNrArtikelIIdOhneExc(sollDtos[i].getArtikelIId(), theClientDto);
			if (stklDto != null) {

				Query query = em.createNamedQuery("LosfindByStuecklisteIId");
				query.setParameter(1, stklDto.getIId());

				Collection c = query.getResultList();

				Iterator it = c.iterator();
				while (it.hasNext()) {
					Los l = (Los) it.next();
					if (!ts.contains(l.getIId())) {
						if (l.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
							ts.add(l.getIId());
						}

						ts = getLoseEinesStuecklistenbaums(l.getIId(), ts, theClientDto);
					}
				}
			}
		}
		return ts;
	}

	@Override
	@org.jboss.ejb3.annotation.TransactionTimeout(5000)
	public EJBExceptionLP gebeLosAusOhneMaterialbuchung(Integer losIId, boolean bHandausgabe,
			boolean throwExceptionWhenCreateFehlmenge, TheClientDto theClientDto,
			ArrayList<BucheSerienChnrAufLosDto> bucheSerienChnrAufLosDtos, boolean bKommissionierterminal)
			throws EJBExceptionLP {
		gebeLosAusImpl(losIId, bHandausgabe, throwExceptionWhenCreateFehlmenge, false, theClientDto,
				bucheSerienChnrAufLosDtos, bKommissionierterminal);

		return postLosausgabeToHost(losIId, theClientDto);
	}

	@org.jboss.ejb3.annotation.TransactionTimeout(5000)
	public EJBExceptionLP gebeLosAus(Integer losIId, boolean bHandausgabe, boolean throwExceptionWhenCreateFehlmenge,
			TheClientDto theClientDto, ArrayList<BucheSerienChnrAufLosDto> bucheSerienChnrAufLosDtos)
			throws EJBExceptionLP {
		gebeLosAusImpl(losIId, bHandausgabe, throwExceptionWhenCreateFehlmenge, true, theClientDto,
				bucheSerienChnrAufLosDtos, false);

		return postLosausgabeToHost(losIId, theClientDto);
	}

	// @TransactionTimeout(5000)
	private void gebeLosAusImpl(Integer losIId, boolean bHandausgabe, boolean throwExceptionWhenCreateFehlmenge,
			boolean materialBuchungDurchfuehren, TheClientDto theClientDto,
			ArrayList<BucheSerienChnrAufLosDto> bucheSerienChnrAufLosDtos, boolean bKommissionierterminal)
			throws EJBExceptionLP {
		myLogger.logData(losIId);
		if (losIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("losIId == null"));
		}
		try {
			LosDto losDto = losFindByPrimaryKey(losIId);

			// SP5369
			String kommFtggrp = getParameterFac().getKommissionierungFertigungsgruppe(theClientDto.getMandant());
			if (kommFtggrp != null && kommFtggrp.trim().length() > 0) {

				if (bKommissionierterminal == false) {
					Fertigungsgruppe fertigungsgruppe = em.find(Fertigungsgruppe.class,
							losDto.getFertigungsgruppeIId());

					if (kommFtggrp.trim().equals(fertigungsgruppe.getCBez())) {

						ArrayList alDaten = new ArrayList();
						alDaten.add(losDto.getCNr());

						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_LOSAUSGABE_NUR_VON_KOMISSIONIERTERMINAL_MOEGLICH,
								alDaten, new Exception("FEHLER_LOSAUSGABE_NUR_VON_KOMISSIONIERTERMINAL_MOEGLICH"));
					}
				}
			}

			ParametermandantDto parameterKundeIstPflichtfeld = getParameterFac().getMandantparameter(
					theClientDto.getSMandantenwaehrung(), ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_KUNDE_IST_PFLICHTFELD);
			boolean bKundeIstPflichtfeld = (Boolean) parameterKundeIstPflichtfeld.getCWertAsObject();

			if (bKundeIstPflichtfeld == true && losDto.getKundeIId() == null && losDto.getAuftragIId() == null) {
				ArrayList alDaten = new ArrayList();
				alDaten.add(losDto.getCNr());
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_LOS_OHNE_KUNDE, alDaten,
						new Exception("FEHLER_FERTIGUNG_LOS_OHNE_KUNDE"));

			}

			boolean bMaterialbuchungBeiAblieferung = false;
			boolean bUnterstuecklisteAusgeben = false;

			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(),
						theClientDto);
				if (Helper.short2Boolean(stklDto.getBMaterialbuchungbeiablieferung()) == true) {
					bMaterialbuchungBeiAblieferung = true;

					// Auskommentiert aufgrund von SP5363
					// throwExceptionWhenCreateFehlmenge = false;
				}
				if (Helper.short2Boolean(stklDto.getBAusgabeunterstueckliste()) == true) {
					bUnterstuecklisteAusgeben = true;
				}
			}

			boolean bAusgegebenEigenerStatus = getParameterFac().getAusgabeEigenerStatus(theClientDto.getMandant());

			if (bAusgegebenEigenerStatus == true && losDto.getStatusCNr().equals(FertigungFac.STATUS_AUSGEGEBEN)) {
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

				boolean bAutomatischeMaterialausgabe = getAutomatischeMaterialbuchung(losDto, theClientDto);
				if (bAutomatischeMaterialausgabe & materialBuchungDurchfuehren) {
					bucheMaterialAufLos(losDto, null, bMaterialbuchungBeiAblieferung, bHandausgabe,
							bUnterstuecklisteAusgeben, theClientDto, bucheSerienChnrAufLosDtos,
							throwExceptionWhenCreateFehlmenge);
				} else {
					bucheMaterialAufLos(losDto, null, true, false, bUnterstuecklisteAusgeben, theClientDto,
							bucheSerienChnrAufLosDtos, throwExceptionWhenCreateFehlmenge);
				}
				//
				// parametermandantDto = getParameterFac()
				// .getMandantparameter(
				// theClientDto.getMandant(),
				// ParameterFac.KATEGORIE_FERTIGUNG,
				// ParameterFac.PARAMETER_KEINE_AUTOMATISCHE_MATERIALBUCHUNG);
				// boolean bKeineAutomatischeMaterialausgabe = false;
				// if (((java.lang.Boolean)
				// parametermandantDto.getCWertAsObject())
				// .booleanValue() == true) {
				// bKeineAutomatischeMaterialausgabe = true;
				// }
				//
				// // PJ18630 Uebersteuert von Stueckliste
				// if (losDto.getStuecklisteIId() != null) {
				// StuecklisteDto stklDto = getStuecklisteFac()
				// .stuecklisteFindByPrimaryKey(
				// losDto.getStuecklisteIId(), theClientDto);
				//
				// bKeineAutomatischeMaterialausgabe = Helper
				// .short2Boolean(stklDto
				// .getBKeineAutomatischeMaterialbuchung());
				// }

				// if (bKeineAutomatischeMaterialausgabe) {
				// bucheMaterialAufLos(losDto, null,
				// true, false,
				// bUnterstuecklisteAusgeben, theClientDto,
				// bucheSerienChnrAufLosDtos,
				// throwExceptionWhenCreateFehlmenge);
				// } else {
				// bucheMaterialAufLos(losDto, null,
				// bMaterialbuchungBeiAblieferung, bHandausgabe,
				// bUnterstuecklisteAusgeben, theClientDto,
				// bucheSerienChnrAufLosDtos,
				// throwExceptionWhenCreateFehlmenge);
				// }

			} else if (STATUS_STORNIERT.equals(losDto.getStatusCNr())) {
				throw EJBExcFactory.losIstStorniert(losDto);
			} else if (losDto.getStatusCNr().equals(STATUS_AUSGEGEBEN) || losDto.getStatusCNr().equals(STATUS_GESTOPPT)
					|| losDto.getStatusCNr().equals(STATUS_IN_PRODUKTION)
					|| losDto.getStatusCNr().equals(STATUS_TEILERLEDIGT)) {
				throw EJBExcFactory.losIstAusgegeben(losDto);
			} else if (STATUS_ERLEDIGT.equals(losDto.getStatusCNr())) {
				throw EJBExcFactory.losIstErledigt(losDto);
			}

			if (getParameterFac().getLosBeiAusgabeGestoppt(theClientDto.getMandant())
					&& bAusgegebenEigenerStatus == false) {
				stoppeProduktion(losIId, theClientDto);
			}
		}

		catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	private boolean getAutomatischeMaterialbuchung(LosDto losDto, TheClientDto theClientDto) throws RemoteException {
		ParametermandantDto paramDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
				ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_KEINE_AUTOMATISCHE_MATERIALBUCHUNG);
		boolean bAutomatischeMaterialausgabe = !paramDto.asBoolean();

		// PJ18630 Uebersteuert von Stueckliste
		if (losDto.getStuecklisteIId() != null) {
			StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(),
					theClientDto);

			bAutomatischeMaterialausgabe = !Helper.short2Boolean(stklDto.getBKeineAutomatischeMaterialbuchung());
		}

		return bAutomatischeMaterialausgabe;
	}

	public Object[] nurProduzierbareLose(Object[] losIIds, TheClientDto theClientDto) {

		Map<Integer, ArtikelDto> artikelDtoCache = new HashMap<Integer, ArtikelDto>();
		Map<Integer, BigDecimal> lagerstaende = new HashMap<Integer, BigDecimal>();

		if (losIIds != null) {
			// Heute produzierbar
			ArrayList<Integer> alProduzierbar = new ArrayList<Integer>();
			try {

				for (int j = 0; j < losIIds.length; j++) {

					LossollmaterialDto[] sollDtos = getFertigungFac().lossollmaterialFindByLosIId((Integer) losIIds[j]);

					// if (loszeit.getFlrlos().getC_nr().equals("16/100018")) {
					// int z = 0;
					// }
					Boolean bProduzierbar = true;

					for (int i = 0; i < sollDtos.length; i++) {

						if (sollDtos[i].getArtikelIId() == 354) {
							int t = 0;
						}

						ArtikelDto aDto = null;
						if (artikelDtoCache.containsKey(sollDtos[i].getArtikelIId())) {
							aDto = artikelDtoCache.get(sollDtos[i].getArtikelIId());
						} else {
							aDto = getArtikelFac().artikelFindByPrimaryKeySmall(sollDtos[i].getArtikelIId(),
									theClientDto);
							artikelDtoCache.put(sollDtos[i].getArtikelIId(), aDto);
						}

						if (aDto.isLagerbewirtschaftet()) {
							BigDecimal lagerstand = null;
							if (lagerstaende.containsKey(aDto.getIId())) {
								lagerstand = lagerstaende.get(aDto.getIId());
							} else {
								lagerstand = getLagerFac().getLagerstandAllerLagerEinesMandanten(
										sollDtos[i].getArtikelIId(), false, theClientDto);
							}

							if (lagerstand.doubleValue() < sollDtos[i].getNMenge().doubleValue()) {
								bProduzierbar = false;

							}
							lagerstand = lagerstand.subtract(sollDtos[i].getNMenge());
							if (lagerstand.doubleValue() < 0) {
								lagerstand = BigDecimal.ZERO;
							}

							lagerstaende.put(aDto.getIId(), lagerstand);

						}

					}

					if (bProduzierbar == true) {
						alProduzierbar.add((Integer) losIIds[j]);
					}
				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			Object[] returnArray = new Object[alProduzierbar.size()];
			return (Object[]) alProduzierbar.toArray(returnArray);

		}

		return losIIds;
	}

	@org.jboss.ejb3.annotation.TransactionTimeout(5000)
	public void gebeLosAusRueckgaengig(Integer losIId,
			boolean bSollmengenBeiNachtraeglichenMaterialentnahmenAktualisieren, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// loggen
		myLogger.logData(losIId);
		// parameter pruefen
		if (losIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("losIId == null"));
		}
		// Los holen
		LosDto losDto = losFindByPrimaryKey(losIId);
		// Das Ruecknehmen der Ausgabe ist fuer folgende Stati nicht erlaubt:
		// Storniert, Angelegt, Erledigt
		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN,
					new Exception("los " + losDto.getCNr() + " ist noch nicht ausgegeben"));
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT,
					new Exception("los " + losDto.getCNr() + " ist storniert"));
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
					new Exception("los " + losDto.getCNr() + " ist bereits erledigt"));
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_TEILERLEDIGT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_TEILERLEDIGT,
					new Exception("los " + losDto.getCNr() + " ist bereits teilerledigt"));
		}
		try {
			// los status
			Los los = em.find(Los.class, losIId);
			if (los == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
			}

			ParametermandantDto parametermandantDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_AUSGABE_EIGENER_STATUS);
			boolean bAusgegebenEigenerStatus = (java.lang.Boolean) parametermandantDto.getCWertAsObject();

			if (bAusgegebenEigenerStatus == true && losDto.getStatusCNr().equals(FertigungFac.STATUS_IN_PRODUKTION)) {
				los.setStatusCNr(FertigungFac.STATUS_AUSGEGEBEN);
				return;
			}

			los.setStatusCNr(FertigungFac.STATUS_ANGELEGT);
			los.setPersonalIIdAusgabe(null);
			los.setTAusgabe(null);

			// SP7321
			los.setPersonalIIdVpEtikettengedruckt(null);
			los.setTVpEtikettengedruckt(null);

			// Jetzt beginnt die Ruecknahme der Ausgaben
			Query query = em.createNamedQuery("LossollmaterialfindByLosIId");
			query.setParameter(1, losIId);
			Collection<?> c = query.getResultList();
			for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
				// fuer alle Materialpositionen im Los
				Lossollmaterial sollmat = (Lossollmaterial) iter.next();
				LosistmaterialDto[] istmat = losistmaterialFindByLossollmaterialIId(sollmat.getIId());
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
				getFehlmengeFac().aktualisiereFehlmenge(LocaleFac.BELEGART_LOS, sollmat.getIId(), theClientDto);
			}
			// stueckliste aktualisieren und Reservierungen wieder herstellen
			if (los.getStuecklisteIId() != null) {
				aktualisiereSollMaterialAusStueckliste(losIId, theClientDto, false, true);
			} else {
				LossollmaterialDto[] sollmat = lossollmaterialFindByLosIId(losIId);
				for (int i = 0; i < sollmat.length; i++) {
					ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(sollmat[i].getArtikelIId(),
							theClientDto);
					if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet())) {
						java.sql.Date dTermin;
						if (sollmat[i].getNMenge().compareTo(new BigDecimal(0)) > 0) {
							// Positive Reservierung: produktionsstart
							dTermin = getFertigungFac().getProduktionsbeginnAnhandZugehoerigemArbeitsgang(
									losDto.getTProduktionsbeginn(), sollmat[i].getIId(), theClientDto);

						} else {
							// Negative Reservierung: produktionsende
							dTermin = losDto.getTProduktionsende();
						}

						// PJ17994
						dTermin = Helper.addiereTageZuDatum(dTermin, sollmat[i].getIBeginnterminoffset());
						createReservierung(artikelDto, sollmat[i].getIId(), sollmat[i].getNMenge(),
								new java.sql.Timestamp(dTermin.getTime()));
					}
				}
			}
			// Eintrag ins Entitylog
			LosDto losDto_Nachher = losFindByPrimaryKey(losIId);
			HvDtoLogger<LosDto> logger = new HvDtoLogger<LosDto>(em, losDto.getIId(), theClientDto);
			logger.log(losDto, losDto_Nachher);

			// arbeitsplan aktualisieren
			// aktualisiereSollArbeitsplanAusStueckliste(losIId, idUser);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	public void storniereLos(Integer losIId, boolean bAuftragspositionsbezugEntfernen, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.logData(losIId);

		if (losIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("losIId == null"));
		}

		LosDto losDto = losFindByPrimaryKey(losIId);

		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT,
					new Exception("Los " + losDto.getCNr() + " ist storniert"));
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
					new Exception("Los " + losDto.getCNr() + " ist bereits erledigt"));
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_TEILERLEDIGT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_TEILERLEDIGT,
					new Exception("Los " + losDto.getCNr() + " ist bereits Teilerledigt"));
		} else {

			boolean bIstmaterialVorhanden = false;

			// die position holen
			Query query = em.createNamedQuery("LossollmaterialfindByLosIId");
			query.setParameter(1, losIId);
			Collection<?> cl = query.getResultList();
			LossollmaterialDto[] sollmat = assembleLossollmaterialDtos(cl);
			// Reservierungen loeschen
			for (int i = 0; i < sollmat.length; i++) {
				BigDecimal ausgegeben = getAusgegebeneMenge(sollmat[i].getIId(), null, theClientDto);
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
					ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(sollmat[i].getArtikelIId(),
							theClientDto);
					removeReservierung(artikelDto, sollmat[i].getIId());

					// Fehlmengen loeschen
					try {
						getFehlmengeFac().aktualisiereFehlmenge(LocaleFac.BELEGART_LOS, sollmat[i].getIId(),
								theClientDto);
					} catch (RemoteException ex1) {
						throwEJBExceptionLPRespectOld(ex1);
					}

				}
				// Eintrag ins Entitylog
				LosDto losDto_Nachher = losFindByPrimaryKey(losIId);
				HvDtoLogger<LosDto> logger = new HvDtoLogger<LosDto>(em, losDto.getIId(), theClientDto);
				logger.log(losDto, losDto_Nachher);

			} else {
				// FEHLER-IST-MATERIAL-VORHANDEN
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_AUF_DEM_LOS_IST_MATERIAL_AUSGEGEBEN,
						new Exception("Los " + losDto.getCNr() + " ist bereits Teilerledigt"));
			}

		}

	}

	public void storniereLosRueckgaengig(Integer losIId, TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(losIId);

		if (losIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("losIId == null"));
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
			aktualisiereSollMaterialAusStueckliste(losIId, theClientDto, false, true);
			// AZ aktualisieren
			// aktualisiereSollArbeitsplanAusStueckliste(losIId, idUser);
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
			// nothing here - hat wohl in der zwischenzeit wer anderer gemacht
			// ;-)
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_AUSGEGEBEN)
				|| losDto.getStatusCNr().equals(FertigungFac.STATUS_GESTOPPT)
				|| losDto.getStatusCNr().equals(FertigungFac.STATUS_IN_PRODUKTION)
				|| losDto.getStatusCNr().equals(FertigungFac.STATUS_TEILERLEDIGT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_AUSGEGEBEN,
					new Exception("los " + losDto.getCNr() + " ist bereits ausgegeben"));
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
					new Exception("los " + losDto.getCNr() + " ist bereits erledigt"));
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	public void manuellErledigenRueckgaengig(Integer losIId, boolean bAufgrundChargenWegwerfen,
			boolean negativeSollmengenZurueckbuchen, TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(losIId);

		if (losIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("losIId == null"));
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

			// PJ20280
			if (bAufgrundChargenWegwerfen) {
				los.setPersonalIIdNachtraeglichGeoeffnet(theClientDto.getIDPersonal());
				los.setTNachtraeglichGeoeffnet(getDate());
			}

			// SP7370 Negative Sollmengen wieder zurueckbuchen, wenn moeglich

			LossollmaterialDto[] losmat = lossollmaterialFindByLosIId(los.getIId());
			if (negativeSollmengenZurueckbuchen) {
				for (LossollmaterialDto sollmatDto : losmat) {
					if (sollmatDto.getNMenge().signum() < 0) {
						LosistmaterialDto[] ist = losistmaterialFindByLossollmaterialIId(sollmatDto.getIId());
						for (LosistmaterialDto istDto : ist) {
							updateLosistmaterialMenge(istDto.getIId(), BigDecimal.ZERO, theClientDto);
						}
					}
				}
			}

			// Fehlmengen wieder eintragen
			for (int i = 0; i < losmat.length; i++) {
				try {
					getFehlmengeFac().aktualisiereFehlmenge(LocaleFac.BELEGART_LOS, losmat[i].getIId(), theClientDto);
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

	public LosistmaterialDto createLosistmaterial(LosistmaterialDto losistmaterialDto, String sSerienChargennummer,
			TheClientDto theClientDto) {
		return createLosistmaterial(losistmaterialDto, sSerienChargennummer, false, theClientDto);
	}

	public LosistmaterialDto createLosistmaterial(LosistmaterialDto losistmaterialDto, String sSerienChargennummer,
			boolean bKommissionierterminal, TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			LossollmaterialDto sollmatDto = lossollmaterialFindByPrimaryKey(losistmaterialDto.getLossollmaterialIId());
			BigDecimal bdPreis = getLagerFac().getGemittelterGestehungspreisEinesLagers(sollmatDto.getArtikelIId(),
					losistmaterialDto.getLagerIId(), theClientDto);
			losistmaterialDto = createLosistmaterialImpl(losistmaterialDto, sSerienChargennummer,
					bKommissionierterminal, bdPreis, theClientDto);
			return losistmaterialDto;
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	private LosistmaterialDto createLosistmaterialImpl(LosistmaterialDto losistmaterialDto, String sSerienChargennummer,
			boolean bKommissionierterminal, BigDecimal bdPreis, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException, EntityExistsException {
		// begin
		// primary key
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_LOSISTMATERIAL);
		losistmaterialDto.setIId(iId);
		Losistmaterial losistmaterial = new Losistmaterial(losistmaterialDto.getIId(),
				losistmaterialDto.getLossollmaterialIId(), losistmaterialDto.getLagerIId(),
				losistmaterialDto.getNMenge(), losistmaterialDto.getBAbgang());
		em.persist(losistmaterial);
		em.flush();
		setLosistmaterialFromLosistmaterialDto(losistmaterial, losistmaterialDto);
		// Lagerbuchung
		LossollmaterialDto sollmatDto = lossollmaterialFindByPrimaryKey(losistmaterialDto.getLossollmaterialIId());

		if (bKommissionierterminal == false) {
			throwExceptionWhenLosFuerKommissionierterminal(losistmaterialDto, theClientDto);
		}
		// Artikel holen
		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(sollmatDto.getArtikelIId(), theClientDto);
		if (Helper.short2boolean(artikelDto.getBSeriennrtragend())) {

			if (Helper.short2boolean(losistmaterialDto.getBAbgang())) {
				getLagerFac().bucheAb(LocaleFac.BELEGART_LOS, sollmatDto.getLosIId(), losistmaterialDto.getIId(),
						sollmatDto.getArtikelIId(), new BigDecimal(1), bdPreis, losistmaterialDto.getLagerIId(),
						sSerienChargennummer, new java.sql.Timestamp(System.currentTimeMillis()), theClientDto);
			} else {
				getLagerFac().bucheZu(LocaleFac.BELEGART_LOS, sollmatDto.getLosIId(), losistmaterialDto.getIId(),
						sollmatDto.getArtikelIId(), new BigDecimal(1), bdPreis, losistmaterialDto.getLagerIId(),
						sSerienChargennummer, new java.sql.Timestamp(System.currentTimeMillis()), theClientDto, null,
						null, true);
			}
		} else {
			if (Helper.short2boolean(losistmaterialDto.getBAbgang())) {
				getLagerFac().bucheAb(LocaleFac.BELEGART_LOS, sollmatDto.getLosIId(), losistmaterialDto.getIId(),
						sollmatDto.getArtikelIId(), losistmaterialDto.getNMenge(), bdPreis,
						losistmaterialDto.getLagerIId(), sSerienChargennummer,
						new java.sql.Timestamp(System.currentTimeMillis()), theClientDto);
			} else {
				getLagerFac().bucheZu(LocaleFac.BELEGART_LOS, sollmatDto.getLosIId(), losistmaterialDto.getIId(),
						sollmatDto.getArtikelIId(), losistmaterialDto.getNMenge().abs(), bdPreis,
						losistmaterialDto.getLagerIId(), sSerienChargennummer,
						new java.sql.Timestamp(System.currentTimeMillis()), theClientDto, null, null, true);
			}
		}
		return losistmaterialDto;
	}

	public void verbuchungBedarfsuebernahmeZuruecknehmen(Integer bedarfsuebernahmeIId, TheClientDto theClientDto) {
		Bedarfsuebernahme bean = em.find(Bedarfsuebernahme.class, bedarfsuebernahmeIId);

		if (bean.getStatusCNr().equals(LocaleFac.STATUS_VERBUCHT)) {
			if (bean.getLossollmaterialIId() != null) {
				Integer lossollmaterialIId = bean.getLossollmaterialIId();
				bean.setLossollmaterialIId(null);
				removeLossollmaterial(lossollmaterialFindByPrimaryKey(lossollmaterialIId), true, theClientDto);
			}

			bean.setStatusCNr(LocaleFac.STATUS_OFFEN);
			bean.setTVerbuchtGedruckt(null);
			bean.setPersonalIIdVerbuchtGedruckt(null);
			bean.setTAendern(getTimestamp());
			bean.setPersonalIIdAendern(theClientDto.getIDPersonal());

			em.merge(bean);
		}

	}

	public void verbucheBedarfsuebernahme(BedarfsuebernahmeDto bedarfsuebernahmeDto, BigDecimal bdMengeGenehmigt,
			BigDecimal bdMengeGebucht, List<SeriennrChargennrMitMengeDto> listSnrChnr, Integer lagerIId,
			TheClientDto theClientDto) {

		try {
			Bedarfsuebernahme bean = em.find(Bedarfsuebernahme.class, bedarfsuebernahmeDto.getIId());

			bean.setArtikelIId(bedarfsuebernahmeDto.getArtikelIId());
			bean.setLosIId(bedarfsuebernahmeDto.getLosIId());

			if (bean.getStatusCNr().equals(LocaleFac.STATUS_OFFEN)) {
				MontageartDto[] mDto = getStuecklisteFac().montageartFindByMandantCNr(theClientDto);

				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(bedarfsuebernahmeDto.getArtikelIId(),
						theClientDto);

				LossollmaterialDto lossollmaterialDto = new LossollmaterialDto();
				lossollmaterialDto.setEinheitCNr(aDto.getEinheitCNr());
				lossollmaterialDto.setLosIId(bedarfsuebernahmeDto.getLosIId());
				lossollmaterialDto.setMontageartIId(mDto[0].getIId());
				lossollmaterialDto.setArtikelIId(bedarfsuebernahmeDto.getArtikelIId());

				BigDecimal bdSollpreis = getLagerFac().getGemittelterGestehungspreisEinesLagers(aDto.getIId(), lagerIId,
						theClientDto);
				lossollmaterialDto.setNSollpreis(bdSollpreis);
				lossollmaterialDto.setNMenge(bdMengeGenehmigt);

				lossollmaterialDto = getFertigungFac().createLossollmaterial(lossollmaterialDto, theClientDto);

				if (bdMengeGebucht.doubleValue() > 0) {
					LosistmaterialDto losistmaterialDto = new LosistmaterialDto();
					losistmaterialDto.setLagerIId(lagerIId);
					losistmaterialDto.setBAbgang(bedarfsuebernahmeDto.getBAbgang());
					losistmaterialDto.setNMenge(bdMengeGebucht);
					getFertigungFac().gebeMaterialNachtraeglichAus(lossollmaterialDto, losistmaterialDto, listSnrChnr,
							true, theClientDto);
				}

				bean.setStatusCNr(LocaleFac.STATUS_VERBUCHT);
				bean.setLossollmaterialIId(lossollmaterialDto.getIId());
				bean.setTAendern(getTimestamp());
				bean.setPersonalIIdAendern(theClientDto.getIDPersonal());

				em.merge(bean);
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
	}

	public Integer createBedarfsuebernahme(BedarfsuebernahmeDto bedarfsuebernahmeDto, TheClientDto theClientDto) {
		// begin
		// primary key
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_BEDARFSUEBERNAHME);
		bedarfsuebernahmeDto.setIId(iId);

		if (bedarfsuebernahmeDto.getPersonalIIdAnlegen() == null) {
			bedarfsuebernahmeDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		}
		if (bedarfsuebernahmeDto.getPersonalIIdAendern() == null) {
			bedarfsuebernahmeDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		}

		if (bedarfsuebernahmeDto.getTAnlegen() == null) {
			bedarfsuebernahmeDto.setTAnlegen(getTimestamp());
		}
		if (bedarfsuebernahmeDto.getTAendern() == null) {
			bedarfsuebernahmeDto.setTAendern(getTimestamp());
		}
		bedarfsuebernahmeDto.setStatusCNr(LocaleFac.STATUS_ANGELEGT);

		if (bedarfsuebernahmeDto.getCLosnummer() != null && bedarfsuebernahmeDto.getLosIId() == null) {
			LosDto losDto = losFindByCNrMandantCNrOhneExc(bedarfsuebernahmeDto.getCLosnummer(),
					theClientDto.getMandant());
			if (losDto != null) {
				bedarfsuebernahmeDto.setLosIId(losDto.getIId());
			}
		}

		if (bedarfsuebernahmeDto.getCArtikelnummer() != null && bedarfsuebernahmeDto.getArtikelIId() == null) {
			ArtikelDto aDto = getArtikelFac().artikelFindByCNrMandantCNrOhneExc(
					bedarfsuebernahmeDto.getCArtikelnummer(), theClientDto.getMandant());
			if (aDto != null) {
				bedarfsuebernahmeDto.setArtikelIId(aDto.getIId());
			}
		}

		try {
			Bedarfsuebernahme bedarfsuebernahme = new Bedarfsuebernahme(bedarfsuebernahmeDto.getIId(),
					bedarfsuebernahmeDto.getCLosnummer(), bedarfsuebernahmeDto.getPersonalIIdAnlegen(),
					bedarfsuebernahmeDto.getTAnlegen(), bedarfsuebernahmeDto.getPersonalIIdAendern(),
					bedarfsuebernahmeDto.getTAendern(), bedarfsuebernahmeDto.getTWunschtermin(),
					bedarfsuebernahmeDto.getNWunschmenge(), bedarfsuebernahmeDto.getBAbgang(),
					bedarfsuebernahmeDto.getBZusaetzlich(), bedarfsuebernahmeDto.getStatusCNr());
			em.persist(bedarfsuebernahme);
			em.flush();
			setBedarfsuebernahmeFromBedarfsuebernahmeDto(bedarfsuebernahme, bedarfsuebernahmeDto);

			return bedarfsuebernahmeDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	private void throwExceptionWhenLosFuerKommissionierterminal(LosistmaterialDto losistmaterialDto,
			TheClientDto theClientDto) {
		// SP5369
		String kommFtggrp = getParameterFac().getKommissionierungFertigungsgruppe(theClientDto.getMandant());
		if (kommFtggrp != null && kommFtggrp.trim().length() > 0) {
			Session session = FLRSessionFactory.getFactory().openSession();
			String sQuery = "SELECT s FROM FLRLossollmaterial s WHERE s.i_id="
					+ losistmaterialDto.getLossollmaterialIId();

			org.hibernate.Query qResult = session.createQuery(sQuery);
			qResult.setMaxResults(1);
			List<?> results = qResult.list();
			FLRLossollmaterial sollmat = (FLRLossollmaterial) results.iterator().next();

			if (kommFtggrp.trim().equals(sollmat.getFlrlos().getFlrfertigungsgruppe().getC_bez())) {

				ArrayList alDaten = new ArrayList();
				alDaten.add(sollmat.getFlrlos().getC_nr());
				alDaten.add(sollmat.getFlrartikel().getC_nr());

				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_MATERIALBUCHUNG_NUR_VON_KOMISSIONIERTERMINAL_MOEGLICH,
						alDaten, new Exception("FEHLER_MATERIALBUCHUNG_NUR_VON_KOMISSIONIERTERMINAL_MOEGLICH"));
			}

		}
	}

	public void removeLosistmaterial(LosistmaterialDto losistmaterialDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (losistmaterialDto != null) {
			// Lagerbuchung
			removeLosistmaterial(losistmaterialDto.getIId(), theClientDto);
		}

	}

	private void removeLosistmaterial(Integer losistmaterialIId, TheClientDto theClientDto) {
		getLagerFac().loescheKompletteLagerbewegungEinerBelgposition(LocaleFac.BELEGART_LOS, losistmaterialIId,
				theClientDto);
		Integer iId = losistmaterialIId;
		Losistmaterial toRemove = em.find(Losistmaterial.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public Date getProduktionsbeginnAnhandZugehoerigemArbeitsgang(Date produktionsbeginn, Integer lossollmaterialIId,
			TheClientDto theClientDto) {
		LossollarbeitsplanDto[] apDtos = getFertigungFac()
				.lossollarbeitsplanFindByLossollmaterialIId(lossollmaterialIId, theClientDto);

		Date dFruehesterBeginn = null;
		for (int i = 0; i < apDtos.length; i++) {

			Integer iMaschinenversatztage = apDtos[i].getIMaschinenversatztage();
			if (iMaschinenversatztage == null) {
				iMaschinenversatztage = 0;
			}

			if (i == 0) {
				dFruehesterBeginn = Helper.addiereTageZuDatum(produktionsbeginn, iMaschinenversatztage);
			} else {
				Date dAgBeginnZeile = Helper.addiereTageZuDatum(produktionsbeginn, iMaschinenversatztage);
				if (dAgBeginnZeile.before(dFruehesterBeginn)) {
					dFruehesterBeginn = dAgBeginnZeile;
				}
			}
		}

		if (dFruehesterBeginn != null && dFruehesterBeginn.after(produktionsbeginn)) {
			return dFruehesterBeginn;
		} else {
			return produktionsbeginn;
		}

	}

	public LossollarbeitsplanDto[] lossollarbeitsplanFindByLossollmaterialIId(Integer lossollmaterialIId,
			TheClientDto theClientDto) {

		LossollarbeitsplanDto[] sollapDtos = null;

		Query queryAP = em.createNamedQuery("LossollarbeitsplanfindByLossollmaterialIId");
		queryAP.setParameter(1, lossollmaterialIId);
		Collection<?> clAP = queryAP.getResultList();

		sollapDtos = assembleLossollarbeitsplanDtos(clAP);

		return sollapDtos;
	}

	public LosistmaterialDto losistmaterialFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		// try {
		Losistmaterial losistmaterial = em.find(Losistmaterial.class, iId);
		if (losistmaterial == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLosistmaterialDto(losistmaterial);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public LosistmaterialDto losistmaterialFindByPrimaryKeyOhneExc(Integer iId) throws EJBExceptionLP {
		// try {
		Losistmaterial losistmaterial = (Losistmaterial) em.find(Losistmaterial.class, iId);
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

	public LosistmaterialDto[] losistmaterialFindByLossollmaterialIId(Integer lossollmaterialIId)
			throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("LosistmaterialfindByLossollmaterialIId");
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

	private void setLosistmaterialFromLosistmaterialDto(Losistmaterial losistmaterial,
			LosistmaterialDto losistmaterialDto) {
		losistmaterial.setLossollmaterialIId(losistmaterialDto.getLossollmaterialIId());
		losistmaterial.setLagerIId(losistmaterialDto.getLagerIId());
		losistmaterial.setNMenge(losistmaterialDto.getNMenge());
		em.merge(losistmaterial);
		em.flush();
	}

	private void setBedarfsuebernahmeFromBedarfsuebernahmeDto(Bedarfsuebernahme bean, BedarfsuebernahmeDto dto) {
		bean.setArtikelIId(dto.getArtikelIId());
		bean.setBAbgang(dto.getBAbgang());
		bean.setBzusaetzlich(dto.getBZusaetzlich());
		bean.setCArtikelbezeichnung(dto.getCArtikelbezeichnung());
		bean.setCLosnummer(dto.getCLosnummer());
		bean.setCArtikelnummer(dto.getCArtikelnummer());
		bean.setCKommentar(dto.getcKommentar());
		bean.setLosIId(dto.getLosIId());
		bean.setLossollmaterialIId(dto.getLossollmaterialIId());

		bean.setNWunschmenge(dto.getNWunschmenge());
		bean.setOMedia(dto.getOMedia());
		bean.setPersonalIIdAendern(dto.getPersonalIIdAendern());
		bean.setPersonalIIdAnlegen(dto.getPersonalIIdAnlegen());
		bean.setPersonalIIdVerbuchtGedruckt(dto.getPersonalIIdVerbuchtGedruckt());
		bean.setTAendern(dto.getTAendern());
		bean.setTAnlegen(dto.getTAnlegen());
		bean.setTVerbuchtGedruckt(dto.getTVerbuchtGedruckt());
		bean.setTWunschtermin(dto.getTWunschtermin());
		bean.setStatusCNr(dto.getStatusCNr());
		em.merge(bean);
		em.flush();
	}

	private LosistmaterialDto assembleLosistmaterialDto(Losistmaterial losistmaterial) {
		return LosistmaterialDtoAssembler.createDto(losistmaterial);
	}

	private LosistmaterialDto[] assembleLosistmaterialDtos(Collection<?> losistmaterials) {
		List<LosistmaterialDto> list = new ArrayList<LosistmaterialDto>();
		if (losistmaterials != null) {
			Iterator<?> iterator = losistmaterials.iterator();
			while (iterator.hasNext()) {
				Losistmaterial losistmaterial = (Losistmaterial) iterator.next();
				list.add(assembleLosistmaterialDto(losistmaterial));
			}
		}
		LosistmaterialDto[] returnArray = new LosistmaterialDto[list.size()];
		return (LosistmaterialDto[]) list.toArray(returnArray);
	}

	private void createReservierung(ArtikelDto artikelDto, Integer iBelegartpositionid, BigDecimal bdMenge,
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

	private void updateReservierung(ArtikelDto artikelDto, Integer iBelegartpositionid, BigDecimal bdMenge,
			Timestamp tLiefertermin) throws EJBExceptionLP {
		try {
			if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet())) {
				if (bdMenge.compareTo(new BigDecimal(0)) != 0) {
					ArtikelreservierungDto resDto = getReservierungFac()
							.artikelreservierungFindByBelegartCNrBelegartPositionIIdOhneExc(LocaleFac.BELEGART_LOS,
									iBelegartpositionid);
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
					getReservierungFac().removeArtikelreservierung(LocaleFac.BELEGART_LOS, iBelegartpositionid);
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	private void removeReservierung(ArtikelDto artikelDto, Integer iBelegartpositionid) throws EJBExceptionLP {
		try {
			if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet())) {
				// vorher schaun ob reserviert - koennte ja menge 0 gewesen sein
				if (getReservierungFac().artikelreservierungFindByBelegartCNrBelegartPositionIIdOhneExc(
						LocaleFac.BELEGART_LOS, iBelegartpositionid) != null) {
					getReservierungFac().removeArtikelreservierung(LocaleFac.BELEGART_LOS, iBelegartpositionid);
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public String getArtikelhinweiseAllerLossollpositionen(Integer losIId, TheClientDto theClientDto) {

		LossollmaterialDto[] dtos = lossollmaterialFindByLosIId(losIId);
		byte[] CRLFAscii = { 13, 10 };
		String meldung = "";

		for (int i = 0; i < dtos.length; i++) {
			try {

				ArrayList<KeyvalueDto> hinweise = getArtikelkommentarFac().getArtikelhinweise(dtos[i].getArtikelIId(),
						LocaleFac.BELEGART_LOS, theClientDto);

				if (hinweise != null && hinweise.size() > 0) {
					ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(dtos[i].getArtikelIId(),
							theClientDto);

					meldung += artikelDto.getCNr() + ": ";

					for (int j = 0; j < hinweise.size(); j++) {

						meldung += Helper.strippHTML(hinweise.get(j).getCValue()) + new String(CRLFAscii);

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

	public BigDecimal getAusgegebeneMenge(Integer lossollmaterialIId, java.sql.Timestamp tsStichtag,
			TheClientDto theClientDto) throws EJBExceptionLP {

		BigDecimal bdMenge = new BigDecimal(0);

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		String queryStichtag = "";
		if (tsStichtag != null) {
			// Aufgrund von PJ19811 wird nun das Belegdatum anstatt der
			// Buchungszeit verwendet (SP5317 -> Uhrzeitgenau)
			queryStichtag += " AND l.t_belegdatum<='" + Helper.formatTimestampWithSlashes(tsStichtag) + "'";
		}

		String query = "SELECT li.b_abgang, (SELECT sum(l.n_menge) FROM FLRLagerbewegung l WHERE l.b_historie=0 AND l.c_belegartnr='"
				+ LocaleFac.BELEGART_LOS + "'  AND l.i_belegartpositionid=li.i_id " + queryStichtag
				+ ") as menge FROM FLRLosistmaterial li WHERE li.lossollmaterial_i_id=" + lossollmaterialIId;

		org.hibernate.Query qResult = session.createQuery(query);
		List<?> results = qResult.list();

		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {

			Object[] o = (Object[]) resultListIterator.next();
			BigDecimal bdAusgegeben = (BigDecimal) o[1];
			if (bdAusgegeben != null) {
				if (Helper.short2boolean((Short) o[0]) == true) {
					bdMenge = bdMenge.add(bdAusgegeben);
				} else {
					bdMenge = bdMenge.subtract(bdAusgegeben);
				}
			}
		}

		return bdMenge;

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BigDecimal getAusgegebeneMengePreis(Integer lossollmaterialIId, java.sql.Timestamp tStichtag,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {

			BigDecimal bdMenge = new BigDecimal(0);
			BigDecimal bdWert = new BigDecimal(0);
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = factory.openSession();

			String queryStichtag = "";
			if (tStichtag != null) {
				// Aufgrund von PJ19811 wird nun das Belegdatum anstatt der
				// Buchungszeit verwendet (SP5317 -> Uhrzeitgenau)
				queryStichtag += " AND l.t_belegdatum<='" + Helper.formatTimestampWithSlashes(tStichtag) + "'";
			}

			String query = "SELECT li.i_id,li.b_abgang, (SELECT sum(l.n_menge) FROM FLRLagerbewegung l WHERE l.b_historie=0 AND l.c_belegartnr='"
					+ LocaleFac.BELEGART_LOS + "'  AND l.i_belegartpositionid=li.i_id " + queryStichtag
					+ ") as menge FROM FLRLosistmaterial li WHERE li.lossollmaterial_i_id=" + lossollmaterialIId;

			org.hibernate.Query qResult = session.createQuery(query);
			List<?> results = qResult.list();

			Iterator<?> resultListIterator = results.iterator();
			while (resultListIterator.hasNext()) {

				Object[] o = (Object[]) resultListIterator.next();

				Integer losistmaterialIId = (Integer) o[0];
				BigDecimal bdAusgegeben = (BigDecimal) o[2];
				if (bdAusgegeben != null) {

					if (Helper.short2boolean((Short) o[1])) {

						BigDecimal bdPreis = getLagerFac().getGemittelterGestehungspreisEinerAbgangsposition(
								LocaleFac.BELEGART_LOS, losistmaterialIId);
						bdMenge = bdMenge.add(bdAusgegeben);
						bdWert = bdWert.add(bdPreis.multiply(bdAusgegeben));
					} else {
						BigDecimal bdPreis = getLagerFac().getGemittelterEinstandspreisEinerZugangsposition(
								LocaleFac.BELEGART_LOS, losistmaterialIId);
						bdMenge = bdMenge.subtract(bdAusgegeben);
						bdWert = bdWert.subtract(bdPreis.multiply(bdAusgegeben));
					}
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

	public LoslosklasseDto createLoslosklasse(LoslosklasseDto loslosklasseDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// log
		myLogger.logData(loslosklasseDto);

		// begin
		loslosklasseDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		// primary key

		try {
			Query query = em.createNamedQuery("LoslosklassefindByLosIIdLosklasseIId");
			query.setParameter(1, loslosklasseDto.getLosIId());
			query.setParameter(2, loslosklasseDto.getLosklasseIId());
			Loslosklasse doppelt = (Loslosklasse) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("FERT_LOSLOSKLASSE.UK"));
		} catch (NoResultException ex) {

		}

		Integer iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_LOSLOSKLASSE);
		loslosklasseDto.setIId(iId);
		try {
			Loslosklasse loslosklasse = new Loslosklasse(loslosklasseDto.getIId(), loslosklasseDto.getLosIId(),
					loslosklasseDto.getLosklasseIId(), loslosklasseDto.getPersonalIIdAendern());
			em.persist(loslosklasse);
			em.flush();
			loslosklasseDto.setTAendern(loslosklasse.getTAendern());
			setLoslosklasseFromLoslosklasseDto(loslosklasse, loslosklasseDto);
			return loslosklasseDto;
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeLoslosklasse(LoslosklasseDto loslosklasseDto, TheClientDto theClientDto) throws EJBExceptionLP {
		// try {
		if (loslosklasseDto != null) {
			Integer iId = loslosklasseDto.getIId();
			Loslosklasse toRemove = em.find(Loslosklasse.class, iId);
			if (toRemove == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
			}
		}
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
	}

	public LossollarbeitsplanDto[] getAlleOffenenZeitenFuerStueckrueckmeldung(Integer personalIId,
			TheClientDto theClientDto) {

		ArrayList<LossollarbeitsplanDto> alDaten = new ArrayList<LossollarbeitsplanDto>();
		HashMap ids = new HashMap();
		try {

			Integer taetigkeitIId_Kommt = getZeiterfassungFac()
					.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_KOMMT, theClientDto).getIId();
			Integer taetigkeitIId_Geht = getZeiterfassungFac()
					.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_GEHT, theClientDto).getIId();
			Integer taetigkeitIId_Ende = getZeiterfassungFac()
					.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_ENDE, theClientDto).getIId();

			Session session = FLRSessionFactory.getFactory().openSession();
			org.hibernate.Criteria liste = session.createCriteria(FLRZeitdaten.class);
			liste.add(Expression.eq(ZeiterfassungFac.FLR_ZEITDATEN_PERSONAL_I_ID, personalIId));

			ArrayList al = new ArrayList();
			al.add(taetigkeitIId_Kommt);
			al.add(taetigkeitIId_Geht);
			al.add(taetigkeitIId_Ende);

			liste.add(Restrictions.in(ZeiterfassungFac.FLR_ZEITDATEN_TAETIGKEIT_I_ID, al));

			liste.add(Expression.lt(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT, new Timestamp(System.currentTimeMillis())));

			liste.add(Expression.eq(ZeiterfassungFac.FLR_ZEITDATEN_B_AUTOMATIKBUCHUNG, Helper.boolean2Short(false)));

			liste.addOrder(Order.desc(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT));
			liste.setMaxResults(1);
			List<?> letztesKommt = liste.list();

			Iterator it = letztesKommt.iterator();

			Timestamp tVon = Helper.cutTimestamp(new Timestamp(System.currentTimeMillis()));

			if (it.hasNext()) {
				FLRZeitdaten flrZeitdaten = (FLRZeitdaten) it.next();

				tVon = new Timestamp(flrZeitdaten.getT_zeit().getTime());
			}

			boolean bTheoretischeIstZeit = false;

			boolean bEinzelarbeitsgangBuchen = false;

			try {
				ParametermandantDto parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
						theClientDto.getMandant(), ParameterFac.KATEGORIE_PERSONAL,
						ParameterFac.PARAMETER_THEORETISCHE_IST_ZEIT_RECHNUNG);

				bTheoretischeIstZeit = ((Boolean) parameter.getCWertAsObject());

				parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_EINZELARBEITSGANG_BUCHEN);

				bEinzelarbeitsgangBuchen = ((Boolean) parameter.getCWertAsObject());

			} catch (RemoteException ex5) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
			}

			// SP8716 Wenn es es seit dem letzten KOMMT offene Zeitverteilungen gibt, dann
			// diese diese anzeigen
			Query query = em.createNamedQuery("ZeitverteilungfindByPersonalIIdTZeitVonTZeitBis");
			query.setParameter(1, personalIId);

			// Von

			query.setParameter(2, tVon);
			Calendar c = Calendar.getInstance();
			query.setParameter(3, new Timestamp(System.currentTimeMillis()));
			Collection<?> cl = query.getResultList();
			ZeitverteilungDto[] zvDtos = ZeitverteilungDtoAssembler.createDtos(cl);

			if (zvDtos != null && zvDtos.length > 0) {
				for (int i = zvDtos.length - 1; i >= 0; i--) {
					if (zvDtos[i].getLossollarbeitsplanIId() != null) {
						Lossollarbeitsplan lossollarbeitsplan = em.find(Lossollarbeitsplan.class,
								zvDtos[i].getLossollarbeitsplanIId());
						if (lossollarbeitsplan != null) {
							if (!Helper.short2boolean(lossollarbeitsplan.getBNurmaschinenzeit())
									&& !Helper.short2boolean(lossollarbeitsplan.getBFertig())) {
								LossollarbeitsplanDto sollDto = assembleLossollarbeitsplanDto(lossollarbeitsplan);
								if (!ids.containsKey(sollDto.getIId())) {
									alDaten.add(sollDto);
									ids.put(sollDto.getIId(), sollDto.getIId());
								}
							}
						}
					}
				}
			} else {
				ZeitdatenDto[] dtos = getZeiterfassungFac().zeitdatenFindZeitdatenEinesTagesUndEinerPerson(personalIId,
						tVon, new Timestamp(System.currentTimeMillis()));

				boolean bRuestenSchonVorhanden = false;

				for (int i = dtos.length - 1; i >= 0; i--) {
					if (dtos[i].getCBelegartnr() != null && dtos[i].getIBelegartid() != null) {

						if (dtos[i].getCBelegartnr().equals(LocaleFac.BELEGART_LOS)
								&& dtos[i].getIBelegartpositionid() != null) {

							try {
								Lossollarbeitsplan lossollarbeitsplan = em.find(Lossollarbeitsplan.class,
										dtos[i].getIBelegartpositionid());
								if (lossollarbeitsplan != null) {
									if (!Helper.short2boolean(lossollarbeitsplan.getBNurmaschinenzeit())
											&& !Helper.short2boolean(lossollarbeitsplan.getBFertig())) {
										LossollarbeitsplanDto sollDto = assembleLossollarbeitsplanDto(
												lossollarbeitsplan);

										// PJ 16233
										if (bTheoretischeIstZeit) {
											if (!ids.containsKey(sollDto.getIId())) {
												// PJ 16035 Ruesten nur das letzte
												if (sollDto.getAgartCNr() == null) {
													if (bRuestenSchonVorhanden == false) {
														bRuestenSchonVorhanden = true;
														alDaten.add(sollDto);
														ids.put(sollDto.getIId(), sollDto.getIId());
													}
												} else {
													alDaten.add(sollDto);
													ids.put(sollDto.getIId(), sollDto.getIId());
												}

											}
										} else {

											if (!ids.containsKey(sollDto.getIId())) {
												alDaten.add(sollDto);

												ids.put(sollDto.getIId(), sollDto.getIId());

												// PJ20641
												if (bEinzelarbeitsgangBuchen == true && alDaten.size() > 0) {

													// PJ20641
													alDaten.addAll(getZeiterfassungFac()
															.getZugehoerigeSollarbeitplanDtos(dtos[i].getIId()));

													break;
												}

											}

										}
									}
								}

							} catch (NoResultException fe) {
								// keiner da
							}
						}
					}
				}
			}

			// nun nach Losnr und AG sortieren
			// Nach Fertigungsgruppe sortieren
			for (int k = alDaten.size() - 1; k > 0; --k) {
				for (int j = 0; j < k; ++j) {
					LossollarbeitsplanDto a1 = (LossollarbeitsplanDto) alDaten.get(j);
					LossollarbeitsplanDto a2 = (LossollarbeitsplanDto) alDaten.get(j + 1);

					LosDto l1 = losFindByPrimaryKey(a1.getLosIId());
					LosDto l2 = losFindByPrimaryKey(a2.getLosIId());

					String s1 = l1.getCNr();

					if (a1.getIUnterarbeitsgang() != null) {
						s1 += Helper.fitString2LengthAlignRight(a1.getIUnterarbeitsgang() + "", 10, ' ');
					} else {
						s1 += Helper.fitString2LengthAlignRight("", 10, ' ');
					}
					s1 += Helper.fitString2LengthAlignRight(a1.getIArbeitsgangnummer() + "", 10, ' ');

					String s2 = l2.getCNr();
					if (a2.getIUnterarbeitsgang() != null) {
						s2 += Helper.fitString2LengthAlignRight(a2.getIUnterarbeitsgang() + "", 10, ' ');
					} else {
						s2 += Helper.fitString2LengthAlignRight("", 10, ' ');
					}
					s2 += Helper.fitString2LengthAlignRight(a2.getIArbeitsgangnummer() + "", 10, ' ');

					if (s1.compareTo(s2) > 0) {
						alDaten.set(j, a2);
						alDaten.set(j + 1, a1);
					}
				}

			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		LossollarbeitsplanDto[] returnArray = new LossollarbeitsplanDto[alDaten.size()];
		return (LossollarbeitsplanDto[]) alDaten.toArray(returnArray);
	}

	// PJ19681
	private Integer getZugehoerigenKunden(Integer losIId, int iEbene, TheClientDto theClientDto) {

		if (iEbene > 50) {
			return null;
		}

		String sQueryLos = "SELECT  l FROM FLRLosReport l WHERE l.i_id=" + losIId;
		Session sessionLos = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Query fQueryLos = sessionLos.createQuery(sQueryLos);

		FLRLosReport flrLosReport = (FLRLosReport) fQueryLos.list().iterator().next();

		if (flrLosReport.getFlrkunde() != null) {
			return flrLosReport.getFlrkunde().getI_id();
		} else {
			if (flrLosReport.getFlrauftragposition() != null) {
				return flrLosReport.getFlrauftragposition().getFlrauftrag().getKunde_i_id_auftragsadresse();
			}
			if (flrLosReport.getFlrauftrag() != null) {
				return flrLosReport.getFlrauftrag().getKunde_i_id_auftragsadresse();
			}

			if (flrLosReport.getStueckliste_i_id() != null) {

				Integer artikelIId = flrLosReport.getFlrstueckliste().getArtikel_i_id();

				Session session = FLRSessionFactory.getFactory().openSession();
				String s = "SELECT sm FROM FLRLossollmaterial sm WHERE sm.flrlos.status_c_nr NOT IN('"
						+ LocaleFac.STATUS_ERLEDIGT + "','" + LocaleFac.STATUS_STORNIERT + "') AND sm.flrartikel.i_id="
						+ artikelIId + "ORDER BY sm.flrlos.c_nr ASC";

				org.hibernate.Query fQuery = session.createQuery(s);
				fQuery.setMaxResults(1);

				// Query ausfuehren
				List<?> fList = fQuery.list();
				Iterator<?> fListIterator = fList.iterator();
				if (fListIterator.hasNext()) {
					FLRLossollmaterial flrLossollmaterial = (FLRLossollmaterial) fListIterator.next();

					// SP4799
					iEbene = iEbene + 1;

					return getZugehoerigenKunden(flrLossollmaterial.getFlrlos().getI_id(), iEbene, theClientDto);

				}

			}

			return null;
		}

	}

	public Integer getZugehoerigenKunden(Integer losIId, TheClientDto theClientDto) {

		return getZugehoerigenKunden(losIId, 0, theClientDto);
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public Integer getJuengstenZusatzstatuseinesLoses(Integer losIId) {

		Integer zusatzstatusIId = null;

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT lz.zusatzstatus_i_id FROM FLRLoszusatzstatus as lz WHERE lz.los_i_id=" + losIId
				+ " ORDER BY lz.t_aendern DESC";
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
	public Boolean hatLosZusatzstatusP1(Integer losIId) {

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT lz.zusatzstatus_i_id FROM FLRLoszusatzstatus as lz WHERE lz.los_i_id=" + losIId
				+ " AND lz.flrzusatzstatus.c_bez='" + ZUSATZSTATUS_P1 + "'";
		org.hibernate.Query query = session.createQuery(sQuery);
		query.setMaxResults(1);

		List<?> resultList = query.list();

		if (resultList.size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public TreeMap<Integer, HashSet<Integer>> getBetroffeneLoseEinesLoses(Integer losIId,
			boolean loseHierarchischNachkalkulieren, TheClientDto theClientDto) {

		TreeMap<Integer, HashSet<Integer>> tm = getBetroffeneLoseEinesLoses(losIId, 0,
				new TreeMap<Integer, HashSet<Integer>>(), theClientDto);

		if (loseHierarchischNachkalkulieren) {
			Iterator<Integer> it = tm.descendingKeySet().iterator();
			while (it.hasNext()) {
				Integer iEbene = it.next();

				HashSet<Integer> losIIs = tm.get(iEbene);

				Iterator itLose = losIIs.iterator();
				while (itLose.hasNext()) {
					Integer losIIdZuKalkulieren = (Integer) itLose.next();
					try {
						getFertigungFac().aktualisiereNachtraeglichPreiseAllerLosablieferungen(losIIdZuKalkulieren,
								theClientDto, false);
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

				}

			}
		}

		return tm;
	}

	private TreeMap<Integer, HashSet<Integer>> getBetroffeneLoseEinesLoses(Integer losIId, int iEbene,
			TreeMap<Integer, HashSet<Integer>> tm, TheClientDto theClientDto) {

		iEbene++;

		HashSet<Integer> al = null;
		if (tm.containsKey(iEbene)) {
			al = tm.get(iEbene);
		} else {
			al = new HashSet<Integer>();
		}

		if (al.contains(losIId)) {
			return tm;
		}

		al.add(losIId);

		tm.put(iEbene, al);

		String sQuery = "SELECT wr FROM FLRWareneingangsreferez wr WHERE wr.abbuchung.c_belegartnr='"
				+ LocaleFac.BELEGART_LOS + "' AND wr.n_verbrauchtemenge > 0 AND wr.abbuchung.i_belegartid=" + losIId;
		sQuery += " AND wr.zubuchung.c_belegartnr IN ('" + LocaleFac.BELEGART_LOSABLIEFERUNG + "','"
				+ LocaleFac.BELEGART_HAND + "','" + LocaleFac.BELEGART_LSZIELLAGER + "') ";

		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Query query = session.createQuery(sQuery);
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		while (resultListIterator.hasNext()) {
			FLRWareneingangsreferez wr = (FLRWareneingangsreferez) resultListIterator.next();

			FLRLagerbewegung zugang = wr.getZubuchung();

			if (zugang.getC_belegartnr().equals(LocaleFac.BELEGART_LOSABLIEFERUNG)) {
				Integer losIIdUnterlos = zugang.getI_belegartid();

				tm = getBetroffeneLoseEinesLoses(losIIdUnterlos, iEbene, tm, theClientDto);

			} else if (zugang.getC_belegartnr().equals(LocaleFac.BELEGART_HAND)
					|| zugang.getC_belegartnr().equals(LocaleFac.BELEGART_LSZIELLAGER)) {

				List<SeriennrChargennrMitMengeDto> snrs = SeriennrChargennrMitMengeDto
						.erstelleDtoAusEinerSeriennummer(zugang.getC_seriennrchargennr());
				ArrayList<WarenzugangsreferenzDto> alZu = getLagerFac().getWareneingangsreferenz(
						zugang.getC_belegartnr(), zugang.getI_belegartpositionid(), snrs, false, theClientDto);

				for (int i = 0; i < alZu.size(); i++) {

					WarenzugangsreferenzDto wzuDto = alZu.get(i);

					if (wzuDto.getBelegart().equals(LocaleFac.BELEGART_LOSABLIEFERUNG)) {
						Integer losIIdUnterlos = wzuDto.getBelegartIId();
						tm = getBetroffeneLoseEinesLoses(losIIdUnterlos, iEbene, tm, theClientDto);
					}
				}
			}

		}
		return tm;
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

	public LossollarbeitsplanDto[] getAlleOffenenMaschinenArbeitsplan(Integer maschineIId, TheClientDto theClientDto) {

		ArrayList<LossollarbeitsplanDto> alDaten = new ArrayList<LossollarbeitsplanDto>();

		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + 2);

		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT l FROM FLRLossollarbeitsplan l WHERE l.b_fertig=0 AND l.maschine_i_id=" + maschineIId
				+ " AND l.flrlos.status_c_nr IN ('" + LocaleFac.STATUS_AUSGEGEBEN + "','"
				+ LocaleFac.STATUS_IN_PRODUKTION + "','" + LocaleFac.STATUS_TEILERLEDIGT
				+ "') AND l.flrlos.t_produktionsbeginn<'"
				+ Helper.formatDateWithSlashes(new java.sql.Date(c.getTimeInMillis())) + "' AND l.flrlos.mandant_c_nr='"
				+ theClientDto.getMandant() + "' ORDER BY l.flrlos.t_produktionsbeginn ASC , l.flrlos.c_nr ASC";

		org.hibernate.Query qResult = session.createQuery(sQuery);
		List<?> results = qResult.list();

		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			FLRLossollarbeitsplan l = (FLRLossollarbeitsplan) resultListIterator.next();
			alDaten.add(lossollarbeitsplanFindByPrimaryKey(l.getI_id()));
		}

		LossollarbeitsplanDto[] returnArray = new LossollarbeitsplanDto[alDaten.size()];
		return (LossollarbeitsplanDto[]) alDaten.toArray(returnArray);
	}

	public LoslosklasseDto updateLoslosklasse(LoslosklasseDto loslosklasseDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Integer iId = loslosklasseDto.getIId();
		// try {

		try {
			Query query = em.createNamedQuery("LoslosklassefindByLosIIdLosklasseIId");
			query.setParameter(1, loslosklasseDto.getLosIId());
			query.setParameter(2, loslosklasseDto.getLosklasseIId());
			Integer iIdVorhanden = ((Loslosklasse) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("FERT_LOSLOSKLASSE.UK"));
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

	public LoslosklasseDto loslosklasseFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		// try {
		Loslosklasse loslosklasse = em.find(Loslosklasse.class, iId);
		if (loslosklasse == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLoslosklasseDto(loslosklasse);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setLoslosklasseFromLoslosklasseDto(Loslosklasse loslosklasse, LoslosklasseDto loslosklasseDto) {
		loslosklasse.setLosIId(loslosklasseDto.getLosIId());
		loslosklasse.setLosklasseIId(loslosklasseDto.getLosklasseIId());
		loslosklasse.setTAendern(loslosklasseDto.getTAendern());
		loslosklasse.setPersonalIIdAendern(loslosklasseDto.getPersonalIIdAendern());
		em.merge(loslosklasse);
		em.flush();
	}

	private LoslosklasseDto assembleLoslosklasseDto(Loslosklasse loslosklasse) {
		return LoslosklasseDtoAssembler.createDto(loslosklasse);
	}

	public Map getAllMaschinenInOffeAGs(TheClientDto theClientDto) {

		LinkedHashMap<String, String> tmArten = new LinkedHashMap<String, String>();

		String sQuery = "select m FROM FLRMaschine m WHERE m.mandant_c_nr = '" + theClientDto.getMandant()
				+ "' AND m.i_id IN (SELECT distinct flroffeneags.maschine_i_id FROM FLROffeneags flroffeneags WHERE flroffeneags.mandant_c_nr = '"
				+ theClientDto.getMandant() + "' ) ORDER BY m.flrmaschinengruppe.c_bez, m.c_bez ASC";

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Query query = session.createQuery(sQuery);

		List<?> resultList = query.list();

		Iterator<?> resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			FLRMaschine m = (FLRMaschine) resultListIterator.next();

			if (!tmArten.containsValue(m.getFlrmaschinengruppe().getC_bez())) {
				tmArten.put("G" + m.getFlrmaschinengruppe().getI_id(), m.getFlrmaschinengruppe().getC_bez());
			}

			String cBez = "";
			if (m.getC_bez() != null) {
				cBez = m.getC_bez();
			}

			cBez = "    " + cBez;

			if (Helper.short2boolean(m.getB_versteckt())) {
				cBez = "*" + cBez;
			}

			tmArten.put("M" + m.getI_id(), cBez);

		}

		return tmArten;
	}

	public Map getAllMaschinenOhneMaschinengruppenInOffeAGs(TheClientDto theClientDto) {

		LinkedHashMap<Integer, String> tmArten = new LinkedHashMap<Integer, String>();

		String sQuery = "select m FROM FLRMaschine m WHERE m.mandant_c_nr = '" + theClientDto.getMandant()
				+ "' AND m.i_id IN (SELECT distinct flroffeneags.maschine_i_id FROM FLROffeneags flroffeneags WHERE flroffeneags.mandant_c_nr = '"
				+ theClientDto.getMandant() + "' ) ORDER BY  m.c_bez ASC";

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Query query = session.createQuery(sQuery);

		List<?> resultList = query.list();

		Iterator<?> resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			FLRMaschine m = (FLRMaschine) resultListIterator.next();

			String cBez = "";
			if (m.getC_bez() != null) {
				cBez = m.getC_bez();
			}

			if (Helper.short2boolean(m.getB_versteckt())) {
				cBez = "*" + cBez;
			}

			tmArten.put(m.getI_id(), cBez);

		}

		return tmArten;
	}

	private LoslosklasseDto[] assembleLoslosklasseDtos(Collection<?> loslosklasses) {
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

	@org.jboss.ejb3.annotation.TransactionTimeout(1000)

	public void aktualisiereNachtraeglichPreiseAllerLosablieferungen(Integer losIId, TheClientDto theClientDto,
			boolean bNurLetztenMaterialwertNeuBerechnen) throws EJBExceptionLP {
		LosDto losDto = losFindByPrimaryKey(losIId);

		try {
			ParametermandantDto parametermandantDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_ISTZEITEN_GLEICH_SOLLZEITEN);
			boolean bSollGleichIstzeiten = false;
			if (((java.lang.Boolean) parametermandantDto.getCWertAsObject()).booleanValue() == true) {
				bSollGleichIstzeiten = true;
			}

			// AZ-Werte aktualisieren
			if (bSollGleichIstzeiten) {
				aktualisiereAZAllerLosablieferungenWennSollIstGleichIst(losIId, theClientDto);
			} else {
				aktualisiereAZAllerLosablieferungen(losIId, theClientDto);
			}

			// Preise berechnen:
			// --------------------------------------------------------------
			// ---------
			// 1. der inklusive aller bisherigen ablieferungen erledigte
			// Materialwert
			if (bNurLetztenMaterialwertNeuBerechnen == false) {
				materialwertAllerLosablieferungenNeuBerechnen(losDto, theClientDto);
			}

			// PJ 14807
			boolean bAblieferpreisIstDurchschnitspreis = false;
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_ABLIEFERUNGSPREIS_IST_DURCHSCHNITTSPREIS);
			bAblieferpreisIstDurchschnitspreis = ((Boolean) parameter.getCWertAsObject());

			BigDecimal bdMaterialwertGesamt = getErledigterMaterialwertNEU(losDto, theClientDto);

			boolean bGesamteIstzeitenZaehlen = false;
			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_LOSABLIEFERUNG_GESAMTE_ISTZEITEN_ZAEHLEN);
			bGesamteIstzeitenZaehlen = ((Boolean) parameter.getCWertAsObject());

			// --------------------------------------------------------------
			// ---------
			// nun der detaillierte Arbeitszeitwert (mit Beruecksichtigung
			// der Unterlose)
			// 1. der inklusive aller bisherigen ablieferungen erledigte
			// Arbeitszeitwert
			BigDecimal bdArbeitszeitwertAusUnterlosenGesamt = getErledigterArbeitszeitwertAusUnterlosen(losDto,
					theClientDto);

			// PJ14807
			BigDecimal bdAzWertDurchschnitt = new BigDecimal(0);
			BigDecimal bdMatWertDurchschnitt = new BigDecimal(0);

			if (bAblieferpreisIstDurchschnitspreis == true) {
				BigDecimal bdGesamtkosten = new BigDecimal(0);
				// Maschinenzeiten
				AuftragzeitenDto[] zeitenMaschine = getZeiterfassungFac().getAllMaschinenzeitenEinesBeleges(losIId,
						null, null, null, theClientDto);
				for (int j = 0; j < zeitenMaschine.length; j++) {
					bdGesamtkosten = bdGesamtkosten.add(zeitenMaschine[j].getBdKosten());
				}

				// "normale" Zeiten
				AuftragzeitenDto[] zeitenMann = getZeiterfassungFac().getAllZeitenEinesBeleges(LocaleFac.BELEGART_LOS,
						losIId, null, null, null, null, ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ARTIKEL, theClientDto);
				for (int j = 0; j < zeitenMann.length; j++) {
					bdGesamtkosten = bdGesamtkosten.add(zeitenMann[j].getBdKosten());
				}

				bdGesamtkosten = bdGesamtkosten.add(bdArbeitszeitwertAusUnterlosenGesamt);

				BigDecimal bdAbgeliefertGesamt = getErledigteMenge(losDto.getIId(), theClientDto);
				if (bdAbgeliefertGesamt.doubleValue() > 0) {
					bdAzWertDurchschnitt = bdGesamtkosten.divide(bdAbgeliefertGesamt, 4, BigDecimal.ROUND_HALF_EVEN);
					bdMatWertDurchschnitt = bdMaterialwertGesamt.divide(bdAbgeliefertGesamt, 4,
							BigDecimal.ROUND_HALF_EVEN);
				}
			}

			Query query = em.createNamedQuery("LosablieferungfindByLosIId");
			query.setParameter(1, losIId);
			Collection<?> losablieferungs = query.getResultList();

			LosablieferungDto[] losabDtos = assembleLosablieferungDtosOhneSnrs(losablieferungs);

			Timestamp tAblieferzeitpunktVorherigeAblieferung = null;

			LosablieferungDto ersteLosablieferungDtoFuerGestpreisaufrollung = null;

			for (Iterator<?> iter = losablieferungs.iterator(); iter.hasNext();) {
				Losablieferung losablieferung = (Losablieferung) iter.next();

				// 2. bisher erledigte duerfen aber nicht mehr beruecksichtigt
				// werden -> subtrahieren
				// Diese werden durch die "Assemblierung" neu geladen (es
				// koennten sich Werte geaendert haben), das Find faellt aber
				// weg

				BigDecimal bdMaterialwert = bdMaterialwertGesamt;

				if (bAblieferpreisIstDurchschnitspreis == false) {

					if (bNurLetztenMaterialwertNeuBerechnen == true && iter.hasNext() == false) {
						// Ausser die betroffene Ablieferung selbst
						for (int i = 0; i < losabDtos.length; i++) {
							if (!losablieferung.getIId().equals(losabDtos[i].getIId())) {
								bdMaterialwert = bdMaterialwert
										.subtract(losabDtos[i].getNMaterialwert().multiply(losabDtos[i].getNMenge()));
							}
						}
						// 3. Nach Division durch die Menge der neuen
						// Ablieferung
						// hab
						// ich den Einzelwert
						losablieferung.setNMaterialwert(
								bdMaterialwert.divide(losablieferung.getNMenge(), 4, BigDecimal.ROUND_HALF_EVEN));
					}
					// ----------------------------------------------------------
					// ----
					// ---------
					// Gestehungspreis: ist die Summe aus Materialwert und
					// Arbeitszeitwert
					losablieferung.setNGestehungspreis(
							losablieferung.getNMaterialwert().add(losablieferung.getNArbeitszeitwert()));

					BigDecimal bdArbeitszeitwertAusUnterlosen = bdArbeitszeitwertAusUnterlosenGesamt;

					// 2. bisher erledigte duerfen aber nicht mehr
					// beruecksichtigt
					// werden -> subtrahieren
					for (int i = 0; i < losabDtos.length; i++) {
						if (!losablieferung.getIId().equals(losabDtos[i].getIId())) {
							bdArbeitszeitwertAusUnterlosen = bdArbeitszeitwertAusUnterlosen.subtract(
									losabDtos[i].getNArbeitszeitwertdetailliert().multiply(losabDtos[i].getNMenge()));
							// SP7278
							if (bdArbeitszeitwertAusUnterlosen.doubleValue() < 0) {
								bdArbeitszeitwertAusUnterlosen = BigDecimal.ZERO;
							}
						}
					}
					// 3. Nach Division durch die Menge der neuen Ablieferung
					// hab
					// ich den Einzelwert
					BigDecimal bdAZWertAusUnterlosen = bdArbeitszeitwertAusUnterlosen.divide(losablieferung.getNMenge(),
							4, BigDecimal.ROUND_HALF_EVEN);
					// 4. Dazu kommt noch der AZ-Wert aus diesem Los
					losablieferung.setNArbeitszeitwertdetailliert(
							bdAZWertAusUnterlosen.add(losablieferung.getNArbeitszeitwert()));
					// ----------------------------------------------------------
					// ----
					// ---------
					// nun der detaillierte Materialwert
					// der ist einfach die Differenz zwischen Gestehungspreis
					// und
					// Detailliertem Arbeitszeitwert
					losablieferung.setNMaterialwertdetailliert(losablieferung.getNGestehungspreis()
							.subtract(losablieferung.getNArbeitszeitwertdetailliert()));

				} else {

					if (bNurLetztenMaterialwertNeuBerechnen == false
							|| (bNurLetztenMaterialwertNeuBerechnen == true && iter.hasNext() == false)) {
						losablieferung.setNMaterialwertdetailliert(bdMatWertDurchschnitt);
						losablieferung.setNMaterialwert(bdMatWertDurchschnitt);

						if (bSollGleichIstzeiten) {
							losablieferung.setNGestehungspreis(
									bdMatWertDurchschnitt.add(losablieferung.getNArbeitszeitwert()));
						} else {
							losablieferung.setNArbeitszeitwert(bdAzWertDurchschnitt);
							losablieferung.setNArbeitszeitwertdetailliert(bdAzWertDurchschnitt);
							losablieferung.setNGestehungspreis(bdMatWertDurchschnitt.add(bdAzWertDurchschnitt));
						}

					}

				}
				// speichern

				// Nur die Zeiten dieser einen Ablieferung berechnen
				// PJ18599
				if (bGesamteIstzeitenZaehlen) {

					// Wenn Los erledigt, dann bei der letzten Ablieferung die
					// zunkuenftigen Zeiten hinzufuegen
					Timestamp tBis = losablieferung.getTAendern();
					if (losDto.getStatusCNr().equals(LocaleFac.STATUS_ERLEDIGT) && iter.hasNext() == false) {
						tBis = null;
					}

					BigDecimal bdGesamtkostenAZ = new BigDecimal(0);
					// Maschinenzeiten
					AuftragzeitenDto[] zeitenMaschine = getZeiterfassungFac().getAllMaschinenzeitenEinesBeleges(losIId,
							null, tAblieferzeitpunktVorherigeAblieferung, tBis, theClientDto);
					for (int j = 0; j < zeitenMaschine.length; j++) {
						bdGesamtkostenAZ = bdGesamtkostenAZ.add(zeitenMaschine[j].getBdKosten());
					}

					// "normale" Zeiten
					AuftragzeitenDto[] zeitenMann = getZeiterfassungFac().getAllZeitenEinesBeleges(
							LocaleFac.BELEGART_LOS, losIId, null, null, tAblieferzeitpunktVorherigeAblieferung, tBis,
							ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ARTIKEL, theClientDto);
					for (int j = 0; j < zeitenMann.length; j++) {
						bdGesamtkostenAZ = bdGesamtkostenAZ.add(zeitenMann[j].getBdKosten());
					}

					bdGesamtkostenAZ = bdGesamtkostenAZ.add(bdArbeitszeitwertAusUnterlosenGesamt);

					BigDecimal bdAbgeliefertGesamt = getErledigteMenge(losDto.getIId(), theClientDto);
					if (bdAbgeliefertGesamt.doubleValue() > 0) {
						BigDecimal bdAzWertDerLosablieferung = bdGesamtkostenAZ.divide(losablieferung.getNMenge(), 4,
								BigDecimal.ROUND_HALF_EVEN);

						losablieferung.setNArbeitszeitwertdetailliert(bdAzWertDerLosablieferung);
						losablieferung.setNArbeitszeitwert(bdAzWertDerLosablieferung);
						losablieferung
								.setNGestehungspreis(losablieferung.getNMaterialwert().add(bdAzWertDerLosablieferung));

					}
				}

				if (bNurLetztenMaterialwertNeuBerechnen == false
						|| (bNurLetztenMaterialwertNeuBerechnen == true && iter.hasNext() == false)) {

					losablieferung.setTAendern(losablieferung.getTAendern());
					losablieferung.setBGestehungspreisneuberechnen(Helper.boolean2Short(false));
					LosablieferungDto loaDot = assembleLosablieferungDto(losablieferung);
					// auch in Lagerbewegung aendern

					// Dieser Status wird beim recalcGestpreis im Lager
					// abgefragt: Wenn NOT NULL dann wird die
					// Gestpreisaufrollung uebersprungen

					if (losabDtos.length > 1) {

						theClientDto.setIStatus(99);
					}
					bucheLosAblieferungAufLager(loaDot, losDto, theClientDto);
					theClientDto.setIStatus(null);

					if (ersteLosablieferungDtoFuerGestpreisaufrollung == null) {
						ersteLosablieferungDtoFuerGestpreisaufrollung = loaDot;
					}

				}

				tAblieferzeitpunktVorherigeAblieferung = losablieferung.getTAendern();

			}

			// PJ18998 Gestreis im Lager nur bei der aeltesten Losablieferung
			// aufrollen, wenn mehr als eine Ablieferung
			if (ersteLosablieferungDtoFuerGestpreisaufrollung != null && losabDtos.length > 1) {
				bucheLosAblieferungAufLager(ersteLosablieferungDtoFuerGestpreisaufrollung, losDto, theClientDto);
			}

		} catch (RemoteException ex1) {
			theClientDto.setIStatus(null);
			throwEJBExceptionLPRespectOld(ex1);
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	private void materialwertAllerLosablieferungenNeuBerechnen_OLD(LosDto losDto, TheClientDto theClientDto) {

		Query query = em.createNamedQuery("LosablieferungfindByLosIId");
		query.setParameter(1, losDto.getIId());
		Collection<?> losablieferungs = query.getResultList();
		LossollmaterialDto[] losmat = lossollmaterialFindByLosIId(losDto.getIId());

		BigDecimal bdAbgeliefertGesamt = getErledigteMenge(losDto.getIId(), theClientDto);

		BigDecimal bdVorherigerWertDerAblieferung = new BigDecimal(0);

		boolean bErledigt = false;

		if (losDto.getStatusCNr().equals(LocaleFac.STATUS_ERLEDIGT)
				|| bdAbgeliefertGesamt.doubleValue() >= losDto.getNLosgroesse().doubleValue()) {
			bErledigt = true;
		}

		HashMap<Integer, LagerbewegungDto[]> hmLagerbewegungen = new HashMap<Integer, LagerbewegungDto[]>();

		for (int i = 0; i < losmat.length; i++) {

			BigDecimal bdPreis = getAusgegebeneMengePreis(losmat[i].getIId(), null, theClientDto);

			LosistmaterialDto[] istmatDtos = losistmaterialFindByLossollmaterialIId(losmat[i].getIId());

			ArrayList al = new ArrayList();

			for (int j = 0; j < istmatDtos.length; j++) {

				List<SeriennrChargennrMitMengeDto> snrDtos = getLagerFac()
						.getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(LocaleFac.BELEGART_LOS,
								istmatDtos[j].getIId(), false);

				for (int k = 0; k < snrDtos.size(); k++) {

					LagerbewegungDto bewDto = null;
					if (snrDtos.get(k).getLagerbewegungIIdLetzteintrag() != null) {
						try {
							bewDto = getLagerFac()
									.lagerbewegungFindByPrimaryKey(snrDtos.get(k).getLagerbewegungIIdLetzteintrag());
						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}
					} else {
						bewDto = getLagerFac().getLetzteintrag(LocaleFac.BELEGART_LOS, istmatDtos[j].getIId(),
								snrDtos.get(k).getCSeriennrChargennr());
					}

					bewDto.setNGestehungspreis(bdPreis);

					al.add(bewDto);

				}

			}

			LagerbewegungDto[] returnArray = new LagerbewegungDto[al.size()];
			hmLagerbewegungen.put(losmat[i].getIId(), (LagerbewegungDto[]) al.toArray(returnArray));

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

				BigDecimal ssg = losmat[i].getNMenge().divide(losDto.getNLosgroesse(), 4, BigDecimal.ROUND_HALF_EVEN)
						.multiply(abgeliefert);

				BigDecimal bdMengeIst = new BigDecimal(0.0000);

				BigDecimal bdWertIstProPosition = new BigDecimal(0.0000);

				if (hmLagerbewegungen.containsKey(losmat[i].getIId())) {
					LagerbewegungDto[] bewDtos = (LagerbewegungDto[]) hmLagerbewegungen.get(losmat[i].getIId());

					if (bewDtos != null) {

						for (int l = 0; l < bewDtos.length; l++) {

							if (bErledigt && iter.hasNext() == false) {

								if (Helper.short2boolean(bewDtos[l].getBAbgang())) {

									bdMengeIst = bdMengeIst.add(bewDtos[l].getNMenge());
									bdWertIstProPosition = bdWertIstProPosition
											.add(bewDtos[l].getNGestehungspreis().multiply(bewDtos[l].getNMenge()));

								} else {
									bdMengeIst = bdMengeIst.subtract(bewDtos[l].getNMenge());
									bdWertIstProPosition = bdWertIstProPosition
											.subtract(bewDtos[l].getNEinstandspreis().multiply(bewDtos[l].getNMenge()));
								}
							} else {

								// Aufgrund von PJ19811 wird nun das Belegdatum
								// anstatt der Buchungszeit verwendet
								if (losablieferung.getTAendern().getTime() >= bewDtos[l].getTBelegdatum().getTime()) {

									if (bdMengeIst.abs().add(bewDtos[l].getNMenge()).doubleValue() > ssg.abs()
											.doubleValue()) {

										BigDecimal mengeSSg = null;

										if (bdMengeIst.abs().doubleValue() < ssg.abs().doubleValue()) {
											mengeSSg = ssg.subtract(bdMengeIst);
										} else {
											mengeSSg = new BigDecimal(0);
										}

										bdMengeIst = bdMengeIst.add(bewDtos[l].getNMenge());

										if (Helper.short2boolean(bewDtos[l].getBAbgang())) {

											bdWertIstProPosition = bdWertIstProPosition
													.add(bewDtos[l].getNGestehungspreis().multiply(mengeSSg));
										} else {
											bdWertIstProPosition = bdWertIstProPosition
													.add(bewDtos[l].getNEinstandspreis().multiply(mengeSSg));
										}

									} else {

										bdMengeIst = bdMengeIst.add(bewDtos[l].getNMenge());

										if (Helper.short2boolean(bewDtos[l].getBAbgang())) {

											bdWertIstProPosition = bdWertIstProPosition.add(
													bewDtos[l].getNGestehungspreis().multiply(bewDtos[l].getNMenge()));
										} else {
											bdWertIstProPosition = bdWertIstProPosition.subtract(
													bewDtos[l].getNEinstandspreis().multiply(bewDtos[l].getNMenge()));
										}
									}

								}
							}

						}

						bdWertIstProAblieferung = bdWertIstProAblieferung.add(bdWertIstProPosition);
					}

				}
			}

			losablieferung.setNMaterialwert(bdWertIstProAblieferung.subtract(bdVorherigerWertDerAblieferung)
					.divide(losablieferung.getNMenge(), 4, BigDecimal.ROUND_HALF_EVEN));

			em.merge(losablieferung);
			em.flush();

			bdVorherigerWertDerAblieferung = bdWertIstProAblieferung;

		}

	}

	private void materialwertAllerLosablieferungenNeuBerechnen(LosDto losDto, TheClientDto theClientDto) {

		Query query = em.createNamedQuery("LosablieferungfindByLosIId");
		query.setParameter(1, losDto.getIId());
		Collection<?> losablieferungs = query.getResultList();
		LossollmaterialDto[] losmat = lossollmaterialFindByLosIId(losDto.getIId());

		BigDecimal bdAbgeliefertGesamt = getErledigteMenge(losDto.getIId(), theClientDto);

		BigDecimal bdVorherigerWertDerAblieferung = new BigDecimal(0);

		boolean bErledigt = false;

		if (losDto.getStatusCNr().equals(LocaleFac.STATUS_ERLEDIGT)
				|| bdAbgeliefertGesamt.doubleValue() >= losDto.getNLosgroesse().doubleValue()) {
			bErledigt = true;
		}

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT lw, (SELECT istmat.lossollmaterial_i_id FROM FLRLosistmaterial istmat WHERE istmat.i_id=lw.i_belegartpositionid) as lossollmaterialIId  FROM FLRLagerwert lw WHERE lw.c_belegartnr='"
				+ LocaleFac.BELEGART_LOS + "' AND lw.i_belegartid=" + losDto.getIId();

		org.hibernate.Query lagerwerte = session.createQuery(sQuery);

		List<?> resultList = lagerwerte.list();
		Iterator<?> resultListIterator = resultList.iterator();

		HashMap<Integer, ArrayList<FLRLagerwert>> hmLagerbewegungen = new HashMap<Integer, ArrayList<FLRLagerwert>>();
		while (resultListIterator.hasNext()) {

			Object[] o = (Object[]) resultListIterator.next();

			FLRLagerwert flrLagerwert = (FLRLagerwert) o[0];

			Integer lossollmaterialIId = (Integer) o[1];

			ArrayList al = new ArrayList();
			if (hmLagerbewegungen.containsKey(lossollmaterialIId)) {
				al = hmLagerbewegungen.get(lossollmaterialIId);
			}

			al.add(flrLagerwert);

			hmLagerbewegungen.put(lossollmaterialIId, al);

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

				BigDecimal ssg = losmat[i].getNMenge().divide(losDto.getNLosgroesse(), 4, BigDecimal.ROUND_HALF_EVEN)
						.multiply(abgeliefert);

				BigDecimal bdMengeIst = new BigDecimal(0.0000);

				BigDecimal bdWertIstProPosition = new BigDecimal(0.0000);

				if (hmLagerbewegungen.containsKey(losmat[i].getIId())) {
					ArrayList<FLRLagerwert> bewDtos = hmLagerbewegungen.get(losmat[i].getIId());

					if (bewDtos != null) {

						for (int l = 0; l < bewDtos.size(); l++) {

							if (bErledigt && iter.hasNext() == false) {

								if (Helper.short2boolean(bewDtos.get(l).getB_abgang())) {

									bdMengeIst = bdMengeIst.add(bewDtos.get(l).getN_menge());
									bdWertIstProPosition = bdWertIstProPosition.add(bewDtos.get(l)
											.getN_gestehungspreis().multiply(bewDtos.get(l).getN_menge()));

								} else {
									bdMengeIst = bdMengeIst.subtract(bewDtos.get(l).getN_menge());
									bdWertIstProPosition = bdWertIstProPosition.subtract(
											bewDtos.get(l).getN_einstandspreis().multiply(bewDtos.get(l).getN_menge()));
								}
							} else {

								// Aufgrund von PJ19811 wird nun das Belegdatum
								// anstatt der Buchungszeit verwendet
								if (losablieferung.getTAendern().getTime() >= bewDtos.get(l).getT_belegdatum()
										.getTime()) {

									if (bdMengeIst.abs().add(bewDtos.get(l).getN_menge()).doubleValue() > ssg.abs()
											.doubleValue()) {

										BigDecimal mengeSSg = null;

										if (bdMengeIst.abs().doubleValue() < ssg.abs().doubleValue()) {
											mengeSSg = ssg.subtract(bdMengeIst);
										} else {
											mengeSSg = new BigDecimal(0);
										}

										bdMengeIst = bdMengeIst.add(bewDtos.get(l).getN_menge());

										if (Helper.short2boolean(bewDtos.get(l).getB_abgang())) {

											bdWertIstProPosition = bdWertIstProPosition
													.add(bewDtos.get(l).getN_gestehungspreis().multiply(mengeSSg));
										} else {
											bdWertIstProPosition = bdWertIstProPosition
													.add(bewDtos.get(l).getN_gestehungspreis().multiply(mengeSSg));
										}

									} else {

										bdMengeIst = bdMengeIst.add(bewDtos.get(l).getN_menge());

										if (Helper.short2boolean(bewDtos.get(l).getB_abgang())) {

											bdWertIstProPosition = bdWertIstProPosition.add(bewDtos.get(l)
													.getN_gestehungspreis().multiply(bewDtos.get(l).getN_menge()));
										} else {
											bdWertIstProPosition = bdWertIstProPosition.subtract(bewDtos.get(l)
													.getN_gestehungspreis().multiply(bewDtos.get(l).getN_menge()));
										}
									}

								}
							}

						}

						bdWertIstProAblieferung = bdWertIstProAblieferung.add(bdWertIstProPosition);
					}

				}
			}

			losablieferung.setNMaterialwert(bdWertIstProAblieferung.subtract(bdVorherigerWertDerAblieferung)
					.divide(losablieferung.getNMenge(), 4, BigDecimal.ROUND_HALF_EVEN));

			em.merge(losablieferung);
			em.flush();

			bdVorherigerWertDerAblieferung = bdWertIstProAblieferung;

		}

	}

	private void aktualisiereAZAllerLosablieferungenWennSollIstGleichIst(Integer losIId, TheClientDto theClientDto) {
		LosDto losDto = losFindByPrimaryKey(losIId);
		try {

			BigDecimal faktorFuerIstGleichSoll = new BigDecimal(0);
			BigDecimal erlMenge = getFertigungFac().getErledigteMenge(losIId, theClientDto);

			if (erlMenge.doubleValue() > 0) {
				if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)
						|| losDto.getNLosgroesse().doubleValue() <= erlMenge.doubleValue()) {
					faktorFuerIstGleichSoll = new BigDecimal(1);
				} else {

					if (losDto.getNLosgroesse().doubleValue() > 0) {
						faktorFuerIstGleichSoll = erlMenge.divide(losDto.getNLosgroesse(), 4,
								BigDecimal.ROUND_HALF_EVEN);
					}
				}
				String sMandantWaehrung = theClientDto.getSMandantenwaehrung();
				// Sollzeiten
				LossollarbeitsplanDto[] soll = lossollarbeitsplanFindByLosIId(losDto.getIId());

				BigDecimal bdGesamtkosten = new BigDecimal(0.0000);

				// Gesamtkosten berechnen
				for (int i = 0; i < soll.length; i++) {

					// Kosten holen
					BigDecimal kosten = new BigDecimal(0.0000);
					if (soll[i].getMaschineIId() != null) {
						kosten = getZeiterfassungFac()
								.getMaschinenKostenZumZeitpunkt(soll[i].getMaschineIId(),
										Helper.cutTimestamp(new Timestamp(System.currentTimeMillis())),LocaleFac.BELEGART_LOS,soll[i].getIId()).getBdStundensatz()
								.multiply(soll[i].getNGesamtzeit()).multiply(faktorFuerIstGleichSoll);
					}

					ArtikellieferantDto artlief = getArtikelFac().getArtikelEinkaufspreisDesBevorzugtenLieferanten(
							soll[i].getArtikelIIdTaetigkeit(), new BigDecimal(1), sMandantWaehrung, theClientDto);
					if (artlief != null && artlief.getLief1Preis() != null) {
						BigDecimal bdSollpreis = artlief.getLief1Preis();
						kosten = kosten
								.add(soll[i].getNGesamtzeit().multiply(bdSollpreis).multiply(faktorFuerIstGleichSoll));
					}
					bdGesamtkosten = bdGesamtkosten.add(kosten);

				}

				
				//
				
				BigDecimal kostenProStueck = bdGesamtkosten.divide(erlMenge, 4, BigDecimal.ROUND_HALF_EVEN);
				
				Query query = em.createNamedQuery("LosablieferungfindByLosIId");
				query.setParameter(1, losIId);
				Collection<?> cl = query.getResultList();
				
				
				List<LosablieferungDto> list = new ArrayList<LosablieferungDto>();
				if (cl != null) {
					Iterator<?> iterator = cl.iterator();
					while (iterator.hasNext()) {
						Losablieferung losablieferung = (Losablieferung) iterator.next();
						losablieferung.setNArbeitszeitwert(kostenProStueck);
						losablieferung.setNArbeitszeitwertdetailliert(kostenProStueck);
					}
				}
				
			}

		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

	}

	private void aktualisiereAZAllerLosablieferungen(Integer losIId, TheClientDto theClientDto) {
		LosDto losDto = losFindByPrimaryKey(losIId);
		try {
			// Sollzeiten
			LossollarbeitsplanDto[] soll = lossollarbeitsplanFindByLosIId(losDto.getIId());
			int SOLL = 0;
			int IST = 1;
			int WERT = 2;
			// Sollzeiten nach Artikel verdichten
			HashMap<Integer, Object[]> listSollVerdichtet = new HashMap<Integer, Object[]>();
			for (int i = 0; i < soll.length; i++) {

				if (listSollVerdichtet.containsKey(soll[i].getArtikelIIdTaetigkeit())) {
					Object[] oTemp = listSollVerdichtet.get(soll[i].getArtikelIIdTaetigkeit());
					BigDecimal sollZeit = soll[i].getNGesamtzeit();
					if (soll[i].getMaschineIId() != null) {
						sollZeit = sollZeit.multiply(new BigDecimal(2));
					}

					oTemp[SOLL] = ((BigDecimal) oTemp[SOLL]).add(sollZeit);

					oTemp[IST] = new BigDecimal(0.00);
					oTemp[WERT] = new BigDecimal(0.00);

					listSollVerdichtet.put(soll[i].getArtikelIIdTaetigkeit(), oTemp);
				} else {
					Object[] oZeile = new Object[3];
					BigDecimal sollZeit = soll[i].getNGesamtzeit();
					if (soll[i].getMaschineIId() != null) {
						sollZeit = sollZeit.multiply(new BigDecimal(2));
					}
					oZeile[SOLL] = sollZeit;

					oZeile[IST] = new BigDecimal(0.00);
					oZeile[WERT] = new BigDecimal(0.00);

					listSollVerdichtet.put(soll[i].getArtikelIIdTaetigkeit(), oZeile);
				}
			}

			// Maschinenzeiten
			AuftragzeitenDto[] zeitenMaschine = getZeiterfassungFac().getAllMaschinenzeitenEinesBeleges(losIId, null,
					null, null, theClientDto);

			// "normale" Zeiten
			AuftragzeitenDto[] zeitenMann = getZeiterfassungFac().getAllZeitenEinesBeleges(LocaleFac.BELEGART_LOS,
					losIId, null, null, null, null, ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ARTIKEL, theClientDto);

			LosablieferungDto[] losablieferungDtos = losablieferungFindByLosIIdOhneNeuberechnungUndOhneSnrChnr(losIId,
					theClientDto);

			boolean bErledigtOderUeberliefert = false;
			BigDecimal bdGesamtAbgeliefert = new BigDecimal(0.00);
			for (int i = 0; i < losablieferungDtos.length; i++) {
				LosablieferungDto losablieferungDto = losablieferungDtos[i];
				bdGesamtAbgeliefert = bdGesamtAbgeliefert.add(losablieferungDto.getNMenge());
			}

			if (losDto.getStatusCNr().equals(LocaleFac.STATUS_ERLEDIGT)
					|| bdGesamtAbgeliefert.doubleValue() >= losDto.getNLosgroesse().doubleValue()) {
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
							if (zeitenMaschine[j].getTsEnde().before(losablieferungDto.getTAendern())
									|| zeitenMaschine[j].getTsEnde().after(losablieferungDtos[0].getTAendern())) {
								bdIst = bdIst.add(new BigDecimal(zeitenMaschine[j].getDdDauer().doubleValue()));
							}

						} else if (i == 0) {
							if (zeitenMaschine[j].getTsEnde().before(losablieferungDto.getTAendern())) {
								bdIst = bdIst.add(new BigDecimal(zeitenMaschine[j].getDdDauer().doubleValue()));

							}
						} else if (i == losablieferungDtos.length - 1) {
							if (zeitenMaschine[j].getTsEnde().after(losablieferungDtos[i - 1].getTAendern())) {
								bdIst = bdIst.add(new BigDecimal(zeitenMaschine[j].getDdDauer().doubleValue()));
							}
						} else {
							if (zeitenMaschine[j].getTsEnde().after(losablieferungDtos[i - 1].getTAendern())
									&& zeitenMaschine[j].getTsEnde().before(losablieferungDto.getTAendern())) {
								bdIst = bdIst.add(new BigDecimal(zeitenMaschine[j].getDdDauer().doubleValue()));
							}
						}
					}

					if (bdIst.doubleValue() > 0) {
						bdKostenGesamt = bdKostenGesamt.add(zeitenMaschine[j].getBdKosten());
						if (hmAblieferung.containsKey(zeitenMaschine[j].getArtikelIId())) {
							Object[] oZeile = hmAblieferung.get(zeitenMaschine[j].getArtikelIId());
							oZeile[IST] = ((BigDecimal) oZeile[IST]).add(bdIst);
							oZeile[WERT] = ((BigDecimal) oZeile[WERT]).add(zeitenMaschine[j].getBdKosten());
							hmAblieferung.put(zeitenMaschine[j].getArtikelIId(), oZeile);
						} else {
							Object[] oZeile = new Object[3];
							oZeile[SOLL] = new BigDecimal(0.00);
							oZeile[IST] = bdIst;
							oZeile[WERT] = zeitenMaschine[j].getBdKosten();
							hmAblieferung.put(zeitenMaschine[j].getArtikelIId(), oZeile);
						}
					}

				}

				// Zeiten Mann
				for (int j = 0; j < zeitenMann.length; j++) {
					BigDecimal bdIst = new BigDecimal(0.00);

					if (zeitenMann[j].getTsEnde() != null) {
						if (i == 0 && i == losablieferungDtos.length - 1) {
							if (zeitenMann[j].getTsEnde().before(losablieferungDto.getTAendern())
									|| zeitenMann[j].getTsEnde().after(losablieferungDtos[0].getTAendern())) {
								bdIst = bdIst.add(new BigDecimal(zeitenMann[j].getDdDauer().doubleValue()));
							}

						}

						else if (i == 0) {
							if (zeitenMann[j].getTsEnde().before(losablieferungDto.getTAendern())) {
								bdIst = bdIst.add(new BigDecimal(zeitenMann[j].getDdDauer().doubleValue()));

							}
						} else if (i == losablieferungDtos.length - 1) {
							if (zeitenMann[j].getTsEnde().after(losablieferungDtos[i - 1].getTAendern())) {
								bdIst = bdIst.add(new BigDecimal(zeitenMann[j].getDdDauer().doubleValue()));
							}
						} else {
							if (zeitenMann[j].getTsEnde().after(losablieferungDtos[i - 1].getTAendern())
									&& zeitenMann[j].getTsEnde().before(losablieferungDto.getTAendern())) {
								bdIst = bdIst.add(new BigDecimal(zeitenMann[j].getDdDauer().doubleValue()));
							}
						}
					}
					if (bdIst.doubleValue() > 0) {
						bdKostenGesamt = bdKostenGesamt.add(zeitenMann[j].getBdKosten());
						if (hmAblieferung.containsKey(zeitenMann[j].getArtikelIId())) {
							Object[] oZeile = hmAblieferung.get(zeitenMann[j].getArtikelIId());
							oZeile[IST] = ((BigDecimal) oZeile[IST]).add(bdIst);
							oZeile[WERT] = ((BigDecimal) oZeile[WERT]).add(zeitenMann[j].getBdKosten());
							hmAblieferung.put(zeitenMann[j].getArtikelIId(), oZeile);
						} else {
							Object[] oZeile = new Object[3];
							oZeile[SOLL] = new BigDecimal(0.00);
							oZeile[IST] = bdIst;
							oZeile[WERT] = zeitenMann[j].getBdKosten();
							hmAblieferung.put(zeitenMann[j].getArtikelIId(), oZeile);
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
					if (hmUeberigeZeitderVorhergehendenAblieferung.containsKey(key)) {
						bdIstAblieferung = bdIstAblieferung
								.add((BigDecimal) hmUeberigeZeitderVorhergehendenAblieferung.get(key));
					}

					BigDecimal bdSollAblieferung = (BigDecimal) oZeile[SOLL];

					BigDecimal bdKostenAblieferung = (BigDecimal) oZeile[WERT];
					if (bdUeberigeKostenderVorhergehendenAblieferung.containsKey(key)) {
						bdKostenAblieferung = bdKostenAblieferung
								.add((BigDecimal) bdUeberigeKostenderVorhergehendenAblieferung.get(key));
					}

					if (bdSollAblieferung.doubleValue() != 0) {

						// Sollsatzgroesse ermitteln

						BigDecimal sollsatzgroesse = bdSollAblieferung
								.divide(losDto.getNLosgroesse(), 4, BigDecimal.ROUND_HALF_EVEN)
								.multiply(losablieferungDto.getNMenge());

						BigDecimal maxSollsatzKosten = bdSollAblieferung.multiply(losablieferungDto.getNMenge());

						BigDecimal tatsaechlicheKosten = null;
						if (bdKostenAblieferung.doubleValue() > maxSollsatzKosten.doubleValue()) {
							tatsaechlicheKosten = maxSollsatzKosten;

						} else {
							tatsaechlicheKosten = bdKostenAblieferung;
						}

						azWertDerAblieferung = azWertDerAblieferung.add(tatsaechlicheKosten);
						if (bdKostenAblieferung.doubleValue() > azWertDerAblieferung.doubleValue()) {
							bdUeberigeKostenderVorhergehendenAblieferung.put(key,
									bdKostenAblieferung.subtract(azWertDerAblieferung));
						}

					} else {
						azWertDerAblieferung = azWertDerAblieferung.add(bdKostenAblieferung);
					}
					System.out.println(azWertDerAblieferung);
				}
				bdVerbrauchteKostenGesamt = bdVerbrauchteKostenGesamt.add(azWertDerAblieferung);

				// Wenn ERLEDIGT oder Ueberliefert //Den Rest hinzufuegen
				if (bErledigtOderUeberliefert && i == losablieferungDtos.length - 1) {
					BigDecimal restKosten = bdKostenGesamt.subtract(bdVerbrauchteKostenGesamt);
					azWertDerAblieferung = azWertDerAblieferung.add(restKosten);
				}
				if (losablieferungDto.getNMenge().doubleValue() != 0) {
					azWertDerAblieferung = azWertDerAblieferung.divide(losablieferungDto.getNMenge(), 4,
							BigDecimal.ROUND_HALF_EVEN);
				}
				Losablieferung losablieferung = em.find(Losablieferung.class, losablieferungDto.getIId());
				if (losablieferung == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
				}
				losablieferung.setNArbeitszeitwert(azWertDerAblieferung);
				losablieferung.setNArbeitszeitwertdetailliert(azWertDerAblieferung);
			}

		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

	}

//	private void bucheNegativesollmengenAufLager(LosDto losDto, BigDecimal bdGesamtAbgeliefert, boolean bStatusErledigt,
//			TheClientDto theClientDto) {
//
//		Query query = em.createNamedQuery("LossollmaterialfindByLosIId");
//		query.setParameter(1, losDto.getIId());
//		Collection<?> cl = query.getResultList();
//
//		LossollmaterialDto[] sollmat = assembleLossollmaterialDtos(cl);
//
//		Integer lagerIId = null;
//
//		ParametermandantDto parametermandantDto;
//		try {
//			parametermandantDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
//					ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_NEGATIVE_SOLLMENGEN_BUCHEN_AUF_ZIELLAGER);
//
//			if (((java.lang.Boolean) parametermandantDto.getCWertAsObject()).booleanValue() == true) {
//				lagerIId = losDto.getLagerIIdZiel();
//			} else {
//				// Laeger des Loses
//				LoslagerentnahmeDto[] laeger = loslagerentnahmeFindByLosIId(losDto.getIId());
//				lagerIId = laeger[0].getLagerIId();
//			}
//
//		} catch (RemoteException e) {
//			throwEJBExceptionLPRespectOld(e);
//		}
//
//		for (int i = 0; i < sollmat.length; i++) {
//			LossollmaterialDto sollmatZeile = sollmat[i];
//
//			if (sollmatZeile.getNMenge().doubleValue() < 0) {
//
//				// Sollsatzgroesse
//				BigDecimal ssg = sollmatZeile.getNMenge().abs().divide(losDto.getNLosgroesse(), 10,
//						BigDecimal.ROUND_HALF_EVEN);
//
//				BigDecimal soll = bdGesamtAbgeliefert.multiply(ssg);
//
//				if (Helper.short2boolean(sollmatZeile.getBRuestmenge())) {
//					if (bStatusErledigt) {
//						soll = sollmatZeile.getNMenge().abs();
//					} else {
//						// Negative Sollmengen werden bei Ruestmenge erst zurueckgebucht, wenn sich der
//						// Los-Status auf 'Erledigt' aendert
//						continue;
//					}
//				}
//
//				BigDecimal ausgegeben = getAusgegebeneMenge(sollmatZeile.getIId(), null, theClientDto).abs();
//
//				BigDecimal mengeNeu = soll.subtract(ausgegeben);
//
//				if (mengeNeu.doubleValue() > 0) {
//					LosistmaterialDto istmat = new LosistmaterialDto();
//					istmat.setLagerIId(lagerIId);
//					istmat.setLossollmaterialIId(sollmat[i].getIId());
//					istmat.setNMenge(mengeNeu.abs());
//					istmat.setBAbgang(Helper.boolean2Short(false));
//
//					createLosistmaterial(istmat, null, theClientDto);
//				}
//
//			}
//		}
//	}

	@Override
	public EJBExceptionLP manuellErledigen(Integer losIId, boolean bDatumDerLetztenAblieferungAlsErledigtDatumVerwenden,
			TheClientDto theClientDto) throws EJBExceptionLP {
		manuellErledigenImpl(losIId, bDatumDerLetztenAblieferungAlsErledigtDatumVerwenden, theClientDto);

		return postLoserledigungToHost(losIId, theClientDto);
	}

	private void manuellErledigenImpl(Integer losIId, boolean bDatumDerLetztenAblieferungAlsErledigtDatumVerwenden,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// losstatus pruefen
		Los los = null;

		los = em.find(Los.class, losIId);
		if (los == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		if (los.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT, "");
		} else if (los.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT, "");
		} else if (los.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN, "");
		} else if (los.getStatusCNr().equals(FertigungFac.STATUS_GESTOPPT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_GESTOPPT, "");
		}

		LosDto losDto = assembleLosDto(los);

		if (losDto.getStuecklisteIId() != null) {

			StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(),
					theClientDto);

			ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(stklDto.getArtikelIId(), theClientDto);

			if (Helper.short2boolean(aDto.getBDokumentenpflicht())) {

				LosablieferungDto[] losablieferungDtos = null;

				Query query = em.createNamedQuery("LosablieferungfindByLosIId");
				query.setParameter(1, losIId);
				Collection<?> cl = query.getResultList();

				losablieferungDtos = assembleLosablieferungDtos(cl);
				String textFehlermeldungDokumente = "";
				boolean bDokumenteFuerAlleLosablieferungenVorhanden = true;
				for (int i = 0; i < losablieferungDtos.length; i++) {
					PrintInfoDto piDto = getJCRDocFac().getPathAndPartnerAndTable(losablieferungDtos[i].getIId(),
							QueryParameters.UC_ID_LOSABLIEFERUNG, theClientDto);

					if (piDto != null && piDto.getDocPath() != null) {
						try {
							ArrayList<JCRDocDto> al = getJCRDocFac().getJCRDocDtoFromNodeChildren(piDto.getDocPath());
							if (al == null || al.size() == 0) {
								bDokumenteFuerAlleLosablieferungenVorhanden = false;

								textFehlermeldungDokumente += losDto.getCNr() + " " + Helper.formatDatumZeit(
										losablieferungDtos[i].getTAendern(), theClientDto.getLocUi()) + "\r\n";
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
							EJBExceptionLP.FEHLER_KEIN_DOKUMENT_BEI_DOKUMENTENPFLICHTIGEM_ARTIKEL_IM_LOS_HINTERLEGT, al,
							new Exception("FEHLER_KEIN_DOKUMENT_BEI_DOKUMENTENPFLICHTIGEM_ARTIKEL_HINTERLEGT"));
				}

			}
		}

		bucheNegativeSollmengenAufLager(new LosId(losDto.getIId()), true, theClientDto);

		los.setStatusCNr(FertigungFac.STATUS_ERLEDIGT);
		los.setPersonalIIdManuellerledigt(theClientDto.getIDPersonal());

		// PJ20280
		los.setTNachtraeglichGeoeffnet(null);
		los.setPersonalIIdNachtraeglichGeoeffnet(null);

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
				los.setTManuellerledigt(losablieferungDtos[losablieferungDtos.length - 1].getTAendern());
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
				getFehlmengeFac().aktualisiereFehlmenge(LocaleFac.BELEGART_LOS, losmat[i].getIId(), theClientDto);
			} catch (RemoteException ex1) {
				throwEJBExceptionLPRespectOld(ex1);
			}
		}
		// lt. WH (Projekt 12966)
		// zus. lt PJ19102 -> ALLE Losablieferungen anstatt nur der letzten
		// nachkalkulieren
		aktualisiereNachtraeglichPreiseAllerLosablieferungen(losIId, theClientDto, false);

	}

	public void bucheFehlmengenNach(LosId losId, LossollmaterialDto[] sollmat, boolean bKommissionierterminal,
			TheClientDto theClientDto) {
		try {

			// Laeger des Loses
			LoslagerentnahmeDto[] laeger = loslagerentnahmeFindByLosIId(losId.id());
			// nun vom lager abbuchen
			for (int i = 0; i < sollmat.length; i++) {
				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(sollmat[i].getArtikelIId(),
						theClientDto);
				// seriennummerntragende werden jetzt gar nicht gebucht

				ArtikelfehlmengeDto artikelfehlemngeDto = getFehlmengeFac()
						.artikelfehlmengeFindByBelegartCNrBelegartPositionIIdOhneExc(LocaleFac.BELEGART_LOS,
								sollmat[i].getIId());

				if (artikelfehlemngeDto != null) {

					BigDecimal bdAbzubuchendeMenge = artikelfehlemngeDto.getNMenge();
					if (!Helper.short2boolean(artikelDto.getBSeriennrtragend())) {
						if (!Helper.short2boolean(artikelDto.getBChargennrtragend())) {
							for (int j = 0; j < laeger.length; j++) {
								// wenn noch was abzubuchen ist (Menge > 0)
								if (bdAbzubuchendeMenge.compareTo(new BigDecimal(0)) == 1) {
									BigDecimal bdLagerstand = null;
									if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet())) {
										bdLagerstand = getLagerFac().getLagerstand(artikelDto.getIId(),
												laeger[j].getLagerIId(), theClientDto);

									} else {
										bdLagerstand = new BigDecimal(999999999);
									}
									// wenn ein lagerstand da ist
									if (bdLagerstand.compareTo(new BigDecimal(0)) == 1) {
										BigDecimal bdMengeVonLager;
										if (bdLagerstand.compareTo(bdAbzubuchendeMenge) == 1) {
											// wenn mehr als ausreichend auf
											// lager
											bdMengeVonLager = bdAbzubuchendeMenge;
										} else {
											// dann nur den lagerstand entnehmen
											bdMengeVonLager = bdLagerstand;
										}
										LosistmaterialDto istmat = new LosistmaterialDto();
										istmat.setLagerIId(laeger[j].getLagerIId());
										istmat.setLossollmaterialIId(sollmat[i].getIId());
										istmat.setNMenge(bdMengeVonLager);
										istmat.setBAbgang(Helper.boolean2Short(true));
										// ist-wert anlegen und lagerbuchung
										// durchfuehren
										createLosistmaterial(istmat, null, bKommissionierterminal, theClientDto);
										// menge reduzieren
										bdAbzubuchendeMenge = bdAbzubuchendeMenge.subtract(bdMengeVonLager);
									}
								}
							}
						}
					} else {
						/**
						 * @todo falls nur eine charge auf lager ist -> trotzdem buchen PJ 4220
						 */
					}

				}

			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

	}

	@org.jboss.ejb3.annotation.TransactionTimeout(1800)
	public LosablieferungResultDto createLosablieferung(LosablieferungDto losablieferungDto, TheClientDto theClientDto,
			boolean bErledigt) throws EJBExceptionLP {
//		LosablieferungResultDto losablieferungErgebnisDto = new LosablieferungResultDto(
//				createLosablieferungImpl(losablieferungDto, theClientDto, bErledigt, false));
//
//		if (!bErledigt)
//			return losablieferungErgebnisDto;
//
//		EJBExceptionLP returnedExc = postLoserledigungToHost(losablieferungDto.getLosIId(), theClientDto);
//		losablieferungErgebnisDto.setEjbExceptionLP(returnedExc);
		return createLosablieferung(losablieferungDto, theClientDto, bErledigt, false);
	}

	@Override
	@org.jboss.ejb3.annotation.TransactionTimeout(1800)
	public LosablieferungResultDto createLosablieferung(LosablieferungDto losablieferungDto, TheClientDto theClientDto,
			boolean bErledigt, boolean bKommissionierterminal) throws EJBExceptionLP {
		Validator.dtoNotNull(losablieferungDto, "losablieferungDto");
		pruefeLosstatusFuerLosablieferung(losablieferungDto.getLosIId());

		CreateLosablieferungModel model = new CreateLosablieferungModel(losablieferungDto, theClientDto);
		model.setErledigt(bErledigt);
		model.setBKommissionierterminal(bKommissionierterminal);

		LosablieferungResultDto resultDto = losAbliefern(model);
		ablieferungAktualisierePreise(model);

		return resultDto;
	}

//	private LosablieferungDto createLosablieferungImpl0(LosablieferungDto losablieferungDto, TheClientDto theClientDto,
//			boolean bErledigt, boolean bKommissionierterminal) throws EJBExceptionLP {
//		// log
//		myLogger.logData(losablieferungDto);
//
//		// losstatus pruefen
//		LosDto losDto = losFindByPrimaryKey(losablieferungDto.getLosIId());
//		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
//			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT, "");
//		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
//			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT, "");
//		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
//			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN, "");
//		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_GESTOPPT)) {
//			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_GESTOPPT, "");
//		}
//
//		// neuer losstatus - entweder erledigt oder teilerledigt
//		Los los = null;
//		// try {
//		los = em.find(Los.class, losDto.getIId());
//		if (los == null) {
//			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
//		}
//		// }
//		// catch (FinderException ex) {
//		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
//		// ex);
//		// }
//
//		try {
//
//			if (bErledigt) {
//				los.setStatusCNr(FertigungFac.STATUS_ERLEDIGT);
//				losDto.setStatusCNr(FertigungFac.STATUS_ERLEDIGT);
//				los.setPersonalIIdErledigt(theClientDto.getIDPersonal());
//				los.setTErledigt(getTimestamp());
//				// Fehlmengen loeschen
//				LossollmaterialDto[] losmat = lossollmaterialFindByLosIId(los.getIId());
//
//				//
//				ParametermandantDto parametermandantDto = getParameterFac().getMandantparameter(
//						theClientDto.getMandant(), ParameterFac.KATEGORIE_FERTIGUNG,
//						ParameterFac.PARAMETER_BEI_LOS_ERLEDIGEN_MATERIAL_NACHBUCHEN);
//				if (((java.lang.Boolean) parametermandantDto.getCWertAsObject()).booleanValue() == true) {
//					bucheFehlmengenNach(new LosId(losDto.getIId()), losmat, bKommissionierterminal, theClientDto);
//				}
//
//				for (int i = 0; i < losmat.length; i++) {
//					getFehlmengeFac().aktualisiereFehlmenge(LocaleFac.BELEGART_LOS, losmat[i].getIId(), theClientDto);
//				}
//			} else {
//				los.setStatusCNr(FertigungFac.STATUS_TEILERLEDIGT);
//				losDto.setStatusCNr(FertigungFac.STATUS_TEILERLEDIGT);
//			}
//
//			// begin
//			losablieferungDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
//			// primary key
//			Integer iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_LOSABLIEFERUNG);
//			losablieferungDto.setIId(iId);
//			// werte werden spaeter berechnet
//			losablieferungDto.setNGestehungspreis(new BigDecimal(0));
//			losablieferungDto.setNMaterialwert(new BigDecimal(0));
//			losablieferungDto.setNArbeitszeitwert(new BigDecimal(0));
//			losablieferungDto.setNMaterialwertdetailliert(new BigDecimal(0));
//			losablieferungDto.setNArbeitszeitwertdetailliert(new BigDecimal(0));
//
//			Losablieferung losablieferung = new Losablieferung(losablieferungDto.getIId(),
//					losablieferungDto.getLosIId(), losablieferungDto.getNMenge(),
//					losablieferungDto.getNGestehungspreis(), losablieferungDto.getNMaterialwert(),
//					losablieferungDto.getNArbeitszeitwert(), losablieferungDto.getPersonalIIdAendern(),
//					losablieferungDto.getNMaterialwertdetailliert(),
//					losablieferungDto.getNArbeitszeitwertdetailliert());
//			em.persist(losablieferung);
//			em.flush();
//
//			// speichern
//			losablieferungDto.setTAendern(losablieferung.getTAendern());
//			losablieferungDto.setBGestehungspreisNeuBerechnen(losablieferung.getBGestehungspreisneuberechnen());
//			setLosablieferungFromLosablieferungDto(losablieferung, losablieferungDto);
//
//			bucheLosAblieferungAufLager(losablieferungDto, losDto, theClientDto);
//
//			// SP4730
//			if (bErledigt) {
//				aktualisiereNachtraeglichPreiseAllerLosablieferungen(losDto.getIId(), theClientDto, false);
//			} else {
//				aktualisiereNachtraeglichPreiseAllerLosablieferungen(losDto.getIId(), theClientDto, true);
//			}
//
//			// PJ 14506
//			bucheNegativeSollmengenAufLager(new LosId(losDto.getIId()), bErledigt, theClientDto);
//
//			return losablieferungDto;
//		} catch (EntityNotFoundException e) {
//			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
//		} catch (RemoteException e) {
//			throwEJBExceptionLPRespectOld(e);
//			return null;
//		}
//	}
	@org.jboss.ejb3.annotation.TransactionTimeout(1800)
	public LosablieferungDto createLosablieferungFuerTerminal(LosablieferungTerminalDto losablieferungDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
		LosablieferungResultDto resultDto = getFertigungFacLocal().losAbliefernUeberTerminal(losablieferungDto,
				theClientDto);
		CreateLosablieferungModel model = new CreateLosablieferungModel(losablieferungDto, theClientDto);
		model.setErledigt(resultDto.isLosErledigt());

		try {
			getFertigungFacLocal().ablieferungAktualisierePreise(model);
		} catch (EJBExceptionLP excLP) {
			handleExcPreiseAktualisieren(model, excLP);
		}

		return resultDto.getLosablieferungDto();
	}

	private void pruefeLosstatusFuerLosablieferung(Integer losIId) {
		Los los = em.find(Los.class, losIId);
		if (los.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT, "");
		} else if (los.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT, "");
		} else if (los.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN, "");
		} else if (los.getStatusCNr().equals(FertigungFac.STATUS_GESTOPPT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_GESTOPPT, "");
		}
	}

	@org.jboss.ejb3.annotation.TransactionTimeout(1800)
	public LosablieferungDto createLosablieferungFuerTerminal(LosablieferungDto losablieferungDto,
			TheClientDto theClientDto, boolean bErledigt) {
		CreateLosablieferungModel model = new CreateLosablieferungModel(losablieferungDto, theClientDto);
		model.setErledigt(bErledigt);
		LosablieferungResultDto resultDto = getFertigungFacLocal().losAbliefern(model);

		try {
			getFertigungFacLocal().ablieferungAktualisierePreise(model);
		} catch (EJBExceptionLP excLP) {
			handleExcPreiseAktualisieren(model, excLP);
		}

		return resultDto.getLosablieferungDto();
	}

	/**
	 * @param losablieferungDto
	 * @param theClientDto
	 * @param bErledigt
	 * @return
	 */
//	private LosablieferungResultDto createLosablieferungFuerTerminalOhnePreisberechnungImpl0(
//			LosablieferungDto losablieferungDto, TheClientDto theClientDto, boolean bErledigt) {
//		LosDto losDto = losFindByPrimaryKey(losablieferungDto.getLosIId());
//
//		// neuer losstatus - entweder erledigt oder teilerledigt
//		Los los = null;
//		// try {
//		los = em.find(Los.class, losDto.getIId());
//		if (los == null) {
//			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
//		}
//		// }
//		// catch (FinderException ex) {
//		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
//		// ex);
//		// }
//
//		try {
//
//			if (bErledigt) {
//				los.setStatusCNr(FertigungFac.STATUS_ERLEDIGT);
//				losDto.setStatusCNr(FertigungFac.STATUS_ERLEDIGT);
//				los.setPersonalIIdErledigt(theClientDto.getIDPersonal());
//				los.setTErledigt(getTimestamp());
//				// Fehlmengen loeschen
//				LossollmaterialDto[] losmat = lossollmaterialFindByLosIId(los.getIId());
//
//				//
//				ParametermandantDto parametermandantDto = getParameterFac().getMandantparameter(
//						theClientDto.getMandant(), ParameterFac.KATEGORIE_FERTIGUNG,
//						ParameterFac.PARAMETER_BEI_LOS_ERLEDIGEN_MATERIAL_NACHBUCHEN);
//				if (((java.lang.Boolean) parametermandantDto.getCWertAsObject()).booleanValue() == true) {
//					bucheFehlmengenNach(new LosId(losDto.getIId()), losmat, false, theClientDto);
//				}
//
//				for (int i = 0; i < losmat.length; i++) {
//					getFehlmengeFac().aktualisiereFehlmenge(LocaleFac.BELEGART_LOS, losmat[i].getIId(), theClientDto);
//				}
//			} else {
//				los.setStatusCNr(FertigungFac.STATUS_TEILERLEDIGT);
//				losDto.setStatusCNr(FertigungFac.STATUS_TEILERLEDIGT);
//			}
//
//			// begin
//			losablieferungDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
//			// primary key
//			Integer iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_LOSABLIEFERUNG);
//			losablieferungDto.setIId(iId);
//			// werte werden spaeter berechnet
//			losablieferungDto.setNGestehungspreis(new BigDecimal(0));
//			losablieferungDto.setNMaterialwert(new BigDecimal(0));
//			losablieferungDto.setNArbeitszeitwert(new BigDecimal(0));
//			losablieferungDto.setNMaterialwertdetailliert(new BigDecimal(0));
//			losablieferungDto.setNArbeitszeitwertdetailliert(new BigDecimal(0));
//
//			Losablieferung losablieferung = new Losablieferung(losablieferungDto.getIId(),
//					losablieferungDto.getLosIId(), losablieferungDto.getNMenge(),
//					losablieferungDto.getNGestehungspreis(), losablieferungDto.getNMaterialwert(),
//					losablieferungDto.getNArbeitszeitwert(), losablieferungDto.getPersonalIIdAendern(),
//					losablieferungDto.getNMaterialwertdetailliert(),
//					losablieferungDto.getNArbeitszeitwertdetailliert());
//			em.persist(losablieferung);
//			em.flush();
//
//			// speichern
//			if (losablieferungDto.getTAendern() == null) {
//
//				losablieferungDto.setTAendern(losablieferung.getTAendern());
//			}
//			losablieferungDto.setBGestehungspreisNeuBerechnen(losablieferung.getBGestehungspreisneuberechnen());
//			setLosablieferungFromLosablieferungDto(losablieferung, losablieferungDto);
//
//			bucheLosAblieferungAufLager(losablieferungDto, losDto, theClientDto);
//
//			// PJ 14506
//			bucheNegativeSollmengenAufLager(new LosId(losDto.getIId()), bErledigt, theClientDto);
//
//			LosablieferungResultDto laResultDto = new LosablieferungResultDto(losablieferungDto);
//			if (!bErledigt)
//				return laResultDto;
//
//			EJBExceptionLP returnedExc = postLoserledigungToHost(losablieferungDto.getLosIId(), theClientDto);
//			laResultDto.setEjbExceptionLP(returnedExc);
//			return laResultDto;
//		} catch (EntityNotFoundException e) {
//			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
//		} catch (RemoteException e) {
//			throwEJBExceptionLPRespectOld(e);
//			return null;
//		}
//	}

	private void bucheLosAblieferungAufLager(LosablieferungDto losablieferungDto, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP {
		LosDto losDto = losFindByPrimaryKey(losablieferungDto.getLosIId());
		bucheLosAblieferungAufLager(losablieferungDto, losDto, theClientDto);
	}

	public void bucheLosAblieferungAufLager(LosablieferungDto losablieferungDto, LosDto losDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
		// Lagerbuchung, aber nicht fuer Materiallisten
		if (losDto.getStuecklisteIId() != null) {
			Stueckliste stueckliste = em.find(Stueckliste.class, losDto.getStuecklisteIId());

			// PJ18223
			Integer lagerIId = losDto.getLagerIIdZiel();
			if (losablieferungDto.getLagerIId() != null) {
				lagerIId = losablieferungDto.getLagerIId();
			}

			getLagerFac().bucheZu(LocaleFac.BELEGART_LOSABLIEFERUNG, losDto.getIId(), losablieferungDto.getIId(),
					stueckliste.getArtikelIId(), losablieferungDto.getNMenge(), losablieferungDto.getNGestehungspreis(),
					lagerIId, losablieferungDto.getSeriennrChargennrMitMenge(), losablieferungDto.getTAendern(),
					theClientDto);

		}
	}

	@org.jboss.ejb3.annotation.TransactionTimeout(1000)
	public void removeLosablieferung(Object[] loablieferungIIds, boolean bMaterialZurueckbuchen,
			TheClientDto theClientDto) {

		Losablieferung ersteAblieferung = em.find(Losablieferung.class, (Integer) loablieferungIIds[0]);

		Timestamp tAendern = ersteAblieferung.getTAendern();

		// losstatus pruefen
		LosDto losDto = losFindByPrimaryKey(ersteAblieferung.getLosIId());
		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT, "");
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN, "");
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_GESTOPPT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_GESTOPPT, "");
		}

		HvDtoLogger<LosablieferungDto> losablieferungLogger = new HvDtoLogger<LosablieferungDto>(em, theClientDto);

		for (int i = 0; i < loablieferungIIds.length; i++) {

			Integer losablieferungIId = (Integer) loablieferungIIds[i];

			Losablieferung toRemove = em.find(Losablieferung.class, losablieferungIId);
			losablieferungLogger.setHeadId(toRemove.getLosIId().toString());
			losablieferungLogger.logDelete(assembleLosablieferungDto(toRemove));

			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
			}

			// Lagerbuchung, aber nicht fuer Materiallisten
			if (losDto.getStuecklisteIId() != null) {

				getLagerFac().loescheKompletteLagerbewegungEinerBelgposition(LocaleFac.BELEGART_LOSABLIEFERUNG,
						losablieferungIId, theClientDto);
			}

		}

		// neuer losstatus
		// try {
		LosablieferungDto[] la = losablieferungFindByLosIId(losDto.getIId(), true, theClientDto);

		Los los = em.find(Los.class, losDto.getIId());
		if (los == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
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

		if (bMaterialZurueckbuchen) {
			ueberzaehligesMaterialZurueckgeben(theClientDto, losDto, tAendern);
		}

		try {

			if (alterStatus.equals(FertigungFac.STATUS_ERLEDIGT)) {
				LossollmaterialDto[] lossollmaterialDtos = lossollmaterialFindByLosIId(los.getIId());
				for (int k = 0; k < lossollmaterialDtos.length; k++) {
					LossollmaterialDto lossollmaterialDto = lossollmaterialDtos[k];
					if (!Helper.short2Boolean(lossollmaterialDto.getBNachtraeglich())) {
						// Fehlmengen aktualisieren
						getFehlmengeFac().aktualisiereFehlmenge(LocaleFac.BELEGART_LOS, lossollmaterialDto.getIId(),
								theClientDto);
					}
				}
				removeRestmengenBuchungen(losDto.getIId(), theClientDto);
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
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ZUGEBUCHTES_MATERIAL_BEREITS_VOM_LAGER_ENTNOMMEN,
							new Exception("FEHLER_FERTIGUNG_ABLIEFERUNG_BEREITS_VOM_LAGER_ENTNOMMEN"));
				}
			}
			throwEJBExceptionLPRespectOld(ex);
		}

	}

	public LosablieferungDto updateLosablieferung(LosablieferungDto losablieferungDto, boolean bMaterialZurueckbuchen,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// losstatus pruefen

		LosDto losDto = losFindByPrimaryKey(losablieferungDto.getLosIId());
		if (losDto.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT, "");
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT, "");
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN, "");
		} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_GESTOPPT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_GESTOPPT, "");
		}

		Losablieferung laVorher = em.find(Losablieferung.class, losablieferungDto.getIId());

		// Los updaten, wenn Mengenreduktion
		if (losablieferungDto.getNMenge().doubleValue() > 0
				&& losablieferungDto.getNMenge().doubleValue() <= laVorher.getNMenge().doubleValue()) {

			Losablieferung losablieferung = em.find(Losablieferung.class, losablieferungDto.getIId());
			HvDtoLogger<LosablieferungDto> losablieferungLogger = new HvDtoLogger<LosablieferungDto>(em,
					losablieferungDto.getLosIId(), theClientDto);
			losablieferungLogger.log(assembleLosablieferungDto(losablieferung), losablieferungDto);
			setLosablieferungFromLosablieferungDto(losablieferung, losablieferungDto);

			try {
				bucheLosAblieferungAufLager(losablieferungDto, losDto, theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			// Material zurueckbuchen
			if (bMaterialZurueckbuchen) {
				ueberzaehligesMaterialZurueckgeben(theClientDto, losDto, losablieferungDto.getTAendern());

			}

			LosablieferungDto[] dtos = losablieferungFindByLosIId(losDto.getIId(), false, theClientDto);
			// Wenn es die letzte Ablieferung ist, nur die letzte neu berechnen,
			// ansonsten alle
			if (dtos[dtos.length - 1].getIId().equals(losablieferungDto.getIId())) {
				aktualisiereNachtraeglichPreiseAllerLosablieferungen(losDto.getIId(), theClientDto, true);
			} else {
				aktualisiereNachtraeglichPreiseAllerLosablieferungen(losDto.getIId(), theClientDto, false);
			}

		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_UPDATE_LOSABLIEFERUNG_FEHLER_MENGE,
					new Exception("Menge neu muss > 0 und kleiner der alten Menge sein."));
		}

		return losablieferungDto;

	}

	private void ueberzaehligesMaterialZurueckgeben(TheClientDto theClientDto, LosDto losDto, Timestamp tBelegdatum) {
		LosablieferungDto[] dtos = losablieferungFindByLosIId(losDto.getIId(), false, theClientDto);
		BigDecimal bdGesamtAbgeliefert = new BigDecimal(0.00);

		VerwendeteSeriennummerLookup geraeteSnr = new VerwendeteSeriennummerLookup();
		for (int i = 0; i < dtos.length; i++) {
			LosablieferungDto losablieferungenDto = dtos[i];
			bdGesamtAbgeliefert = bdGesamtAbgeliefert.add(losablieferungenDto.getNMenge());

			// SP9116
			// Bei allen abgelieferten Seriennummern nachschauen, welche
			// Geraeteseriennummern verwendet wurden.
			// Die duerfen nicht zurueck gegeben werden
			for (SeriennrChargennrMitMengeDto snrChnrDto : losablieferungenDto.getSeriennrChargennrMitMenge()) {
				GeraetesnrDto[] gsnrDtos = getLagerFac().getGeraeteseriennummerEinerLagerbewegung(
						LocaleFac.BELEGART_LOSABLIEFERUNG, losablieferungenDto.getIId(),
						snrChnrDto.getCSeriennrChargennr());

				for (GeraetesnrDto dto : gsnrDtos) {
					geraeteSnr.addSeriennummer(dto.getArtikelIId(), dto.getCSnr());
				}
			}
		}

		LossollmaterialDto[] sollmatDtos = lossollmaterialFindByLosIId(losDto.getIId());
		for (int i = 0; i < sollmatDtos.length; i++) {
			LossollmaterialDto sollmatDto = sollmatDtos[i];

			if (sollmatDto.getNMenge().doubleValue() != 0) {
				BigDecimal sollsatzgroesse = sollmatDto.getNMenge()
						.divide(losDto.getNLosgroesse(), 6, BigDecimal.ROUND_HALF_EVEN).multiply(bdGesamtAbgeliefert);

				// Wenn nachtraegliche position, dann auslassen
				if (Helper.short2boolean(sollmatDto.getBNachtraeglich()) == false) {
					LosistmaterialDto[] losistmaterialDtos = losistmaterialFindByLossollmaterialIId(
							sollmatDto.getIId());

					BigDecimal istMaterialPlusNachtraeglich = new BigDecimal(0);

					for (int j = 0; j < losistmaterialDtos.length; j++) {
						LosistmaterialDto istmaterialDto = losistmaterialDtos[j];
						// wenn Lagerzugang, der wird ausgelassen
						if (Helper.short2boolean(istmaterialDto.getBAbgang())) {
							istMaterialPlusNachtraeglich = istMaterialPlusNachtraeglich.add(istmaterialDto.getNMenge());
						}

					}

					// Wenn ausgegebenes Material grosser als
					// Sollsatzgroesse
					if (istMaterialPlusNachtraeglich.doubleValue() > sollsatzgroesse.doubleValue()) {
						BigDecimal zuReduzieren = Helper
								.rundeKaufmaennisch(istMaterialPlusNachtraeglich.subtract(sollsatzgroesse), 4);

						for (int j = losistmaterialDtos.length; j > 0; j--) {
							if (zuReduzieren.doubleValue() > 0) {
								LosistmaterialDto istmaterialDto = losistmaterialDtos[j - 1];

								// wenn Lagerzugang, der wird
								// ausgelassen
								if (Helper.short2boolean(istmaterialDto.getBAbgang())) {

									List<SeriennrChargennrMitMengeDto> lospos = getLagerFac()
											.getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(
													LocaleFac.BELEGART_LOS, istmaterialDto.getIId());

									for (int k = 0; k < lospos.size(); k++) {
										// SP9116 Geraeteseriennummern, die in einer Losablieferung enthalten sind,
										// koennen nicht zurueckgegeben werden
										if (geraeteSnr.isSeriennummerVerwendet(sollmatDto.getArtikelIId(),
												lospos.get(k).getCSeriennrChargennr())) {
											continue;
										}

										LagerbewegungDto lagerbewegungDto = getLagerFac().getLetzteintrag(
												LocaleFac.BELEGART_LOS, istmaterialDto.getIId(),
												lospos.get(k).getCSeriennrChargennr());

										BigDecimal nMengeAbsolut = istmaterialDto.getNMenge().subtract(zuReduzieren);
										if (zuReduzieren.compareTo(istmaterialDto.getNMenge()) >= 0) {
											nMengeAbsolut = new BigDecimal(0);
										} else {
											zuReduzieren = new BigDecimal(0);
										}

										// Korrekturbuchung
										// Belegdatum wird nicht auf
										// heute
										// gesetzt lt. WH 20090115

										if (lospos.get(k).getCSeriennrChargennr() == null) {
											getLagerFac().bucheAb(LocaleFac.BELEGART_LOS, losDto.getIId(),
													istmaterialDto.getIId(), sollmatDto.getArtikelIId(), nMengeAbsolut,
													lagerbewegungDto.getNVerkaufspreis(), istmaterialDto.getLagerIId(),
													lospos.get(k).getCSeriennrChargennr(), tBelegdatum, theClientDto);
										}

										// Losistmaterial updaten
										updateLosistmaterialMenge(istmaterialDto.getIId(), nMengeAbsolut, theClientDto);

										zuReduzieren = zuReduzieren.subtract(istmaterialDto.getNMenge());
									}

								}
							}
						}

					}
				}
			}
		}
	}

	private void setLosablieferungFromLosablieferungDto(Losablieferung losablieferung,
			LosablieferungDto losablieferungDto) {
		losablieferung.setLosIId(losablieferungDto.getLosIId());
		losablieferung.setNMenge(losablieferungDto.getNMenge());
		losablieferung.setNGestehungspreis(losablieferungDto.getNGestehungspreis());
		losablieferung.setNMaterialwert(losablieferungDto.getNMaterialwert());
		losablieferung.setNArbeitszeitwert(losablieferungDto.getNArbeitszeitwert());
		losablieferung.setPersonalIIdAendern(losablieferungDto.getPersonalIIdAendern());
		losablieferung.setTAendern(losablieferungDto.getTAendern());
		losablieferung.setNMaterialwertdetailliert(losablieferungDto.getNMaterialwertdetailliert());
		losablieferung.setNArbeitszeitwertdetailliert(losablieferungDto.getNArbeitszeitwertdetailliert());
		losablieferung.setBGestehungspreisneuberechnen(losablieferungDto.getBGestehungspreisNeuBerechnen());
		losablieferung.setLagerIId(losablieferungDto.getLagerIId());
		em.merge(losablieferung);
		em.flush();
	}

	private LosablieferungDto assembleLosablieferungDto(Losablieferung losablieferung) {
		LosablieferungDto loaDto = LosablieferungDtoAssembler.createDto(losablieferung);
		loaDto.setSeriennrChargennrMitMenge(getLagerFac().getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(
				LocaleFac.BELEGART_LOSABLIEFERUNG, losablieferung.getIId()));

		return loaDto;
	}

	private LosablieferungDto assembleLosablieferungDtoOhneSnrs(Losablieferung losablieferung) {
		LosablieferungDto loaDto = LosablieferungDtoAssembler.createDto(losablieferung);

		return loaDto;
	}

	private LosablieferungDto[] assembleLosablieferungDtos(Collection<?> losablieferungs) {
		List<LosablieferungDto> list = new ArrayList<LosablieferungDto>();
		if (losablieferungs != null) {
			Iterator<?> iterator = losablieferungs.iterator();
			while (iterator.hasNext()) {
				Losablieferung losablieferung = (Losablieferung) iterator.next();
				list.add(assembleLosablieferungDto(losablieferung));
			}
		}
		LosablieferungDto[] returnArray = new LosablieferungDto[list.size()];
		return (LosablieferungDto[]) list.toArray(returnArray);
	}

	private LosablieferungDto[] assembleLosablieferungDtosOhneSnrs(Collection<?> losablieferungs) {
		List<LosablieferungDto> list = new ArrayList<LosablieferungDto>();
		if (losablieferungs != null) {
			Iterator<?> iterator = losablieferungs.iterator();
			while (iterator.hasNext()) {
				Losablieferung losablieferung = (Losablieferung) iterator.next();
				list.add(assembleLosablieferungDtoOhneSnrs(losablieferung));
			}
		}
		LosablieferungDto[] returnArray = new LosablieferungDto[list.size()];
		return (LosablieferungDto[]) list.toArray(returnArray);
	}

	public LossollmaterialDto getArtikelIIdOffenenSeriennummernBeiGeraeteseriennummer(Integer losIId,
			TheClientDto theClientDto) {
		LossollmaterialDto[] dtos = lossollmaterialFindByLosIId(losIId);

		LossollmaterialDto sollmaterialDto = null;
		for (int i = 0; i < dtos.length; i++) {
			LossollmaterialDto dto = dtos[i];

			if (dto.getArtikelIId() != null) {

				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(dto.getArtikelIId(), theClientDto);
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

	public ArrayList<String> getOffeneGeraeteSnrEinerSollPosition(Integer lossollmaterialIId,
			TheClientDto theClientDto) {

		LossollmaterialDto sollmatDto = lossollmaterialFindByPrimaryKey(lossollmaterialIId);

		LosistmaterialDto[] istmatDtos = losistmaterialFindByLossollmaterialIId(lossollmaterialIId);

		ArrayList<String> alAufLager = new ArrayList<String>();

		for (int i = 0; i < istmatDtos.length; i++) {

			List<SeriennrChargennrMitMengeDto> l = getLagerFac()
					.getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(LocaleFac.BELEGART_LOS,
							istmatDtos[i].getIId());

			for (int j = 0; j < l.size(); j++) {

				alAufLager.add(l.get(j).getCSeriennrChargennr());
			}

		}

		// Abgeliferte

		LosablieferungDto[] losabDtos = losablieferungFindByLosIId(sollmatDto.getLosIId(), false, theClientDto);
		for (int i = 0; i < losabDtos.length; i++) {
			GeraetesnrDto[] gsnrDtos = getLagerFac().getGeraeteseriennummerEinerLagerbewegung(
					LocaleFac.BELEGART_LOSABLIEFERUNG, losabDtos[i].getIId(),
					losabDtos[i].getSeriennrChargennrMitMenge().get(0).getCSeriennrChargennr());

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

	public String generiereChargennummer(Integer losIId, TheClientDto theClientDto) {
		LosDto losDto = losFindByPrimaryKey(losIId);

		LosablieferungDto[] laDos = losablieferungFindByLosIId(losIId, false, theClientDto);
		String lfdNr = (laDos.length + 1) + "";
		return losDto.getCNr() + "-" + Helper.fitString2LengthAlignRight(lfdNr, 3, '0');
	}

	public String getHierarchischeChargennummer(Integer losIId, TheClientDto theClientDto) {
		LosDto losDto = losFindByPrimaryKey(losIId);

		LossollmaterialDto[] sollmatDtos = lossollmaterialFindByLosIId(losIId);

		int iAnzahlChargennrtragend = 0;
		HashSet<String> hsChargennummern = new HashSet<String>();
		for (int i = 0; i < sollmatDtos.length; i++) {
			ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(sollmatDtos[i].getArtikelIId(), theClientDto);
			if (aDto.isChargennrtragend()) {
				iAnzahlChargennrtragend++;
				LosistmaterialDto[] istMatDtos = losistmaterialFindByLossollmaterialIId(sollmatDtos[i].getIId());

				for (int j = 0; j < istMatDtos.length; j++) {
					if (istMatDtos[j].getNMenge().doubleValue() > 0) {
						List<SeriennrChargennrMitMengeDto> snrchnr = getLagerFac()
								.getAllSeriennrchargennrEinerBelegartpositionOhneChargeneigenschaften(
										LocaleFac.BELEGART_LOS, istMatDtos[j].getIId());

						for (int k = 0; k < snrchnr.size(); k++) {
							hsChargennummern.add(snrchnr.get(k).getCSeriennrChargennr());
						}
					}
				}
			}
		}

		if (iAnzahlChargennrtragend == 1 && hsChargennummern.size() == 1) {

			int iLaengeHierarchischeChaergennummer = 0;
			try {
				ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_LAENGE_HIERARCHISCHE_CHARGENNUMMER);
				iLaengeHierarchischeChaergennummer = ((Integer) parameter.getCWertAsObject()).intValue();
			} catch (RemoteException ex2) {
				throwEJBExceptionLPRespectOld(ex2);
			}

			Integer iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_HIERARCHISCHECHARGENNUMMERN);

			String chnrVorhanden = hsChargennummern.iterator().next();

			return chnrVorhanden + "."
					+ Helper.fitString2LengthAlignRight(iId + "", iLaengeHierarchischeChaergennummer, '0');
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_GENERIERE_HIERARCHISCHE_CHARGENNUMMERN,
					new Exception("FEHLER_GENERIERE_HIERARCHISCHE_CHARGENNUMMERN"));
		}

	}

	public List<SeriennrChargennrMitMengeDto> getOffenenSeriennummernBeiGeraeteseriennummer(Integer losIId,
			TheClientDto theClientDto) {

		LossollmaterialDto sollmaterialDto = getArtikelIIdOffenenSeriennummernBeiGeraeteseriennummer(losIId,
				theClientDto);
		if (sollmaterialDto != null) {
			ArrayList<SeriennrChargennrMitMengeDto> al = new ArrayList<SeriennrChargennrMitMengeDto>();

			LosistmaterialDto[] istmaterialDtos = losistmaterialFindByLossollmaterialIId(sollmaterialDto.getIId());
			for (int i = 0; i < istmaterialDtos.length; i++) {
				List<SeriennrChargennrMitMengeDto> lospos = getLagerFac()
						.getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(LocaleFac.BELEGART_LOS,
								istmaterialDtos[i].getIId());
				al.addAll(lospos);
			}

			ArrayList<SeriennrChargennrMitMengeDto> abgelieferteSNrs = new ArrayList<SeriennrChargennrMitMengeDto>();

			LosablieferungDto[] ablDtos = losablieferungFindByLosIId(losIId, false, theClientDto);
			for (int i = 0; i < ablDtos.length; i++) {
				if (ablDtos[i].getSeriennrChargennrMitMenge() != null) {
					abgelieferteSNrs.addAll(ablDtos[i].getSeriennrChargennrMitMenge());
				}
			}

			ArrayList<SeriennrChargennrMitMengeDto> uebrigeSNrs = new ArrayList<SeriennrChargennrMitMengeDto>();
			for (int i = 0; i < al.size(); i++) {
				String snr = al.get(i).getCSeriennrChargennr();

				boolean bGefunden = false;
				for (int j = 0; j < abgelieferteSNrs.size(); j++) {
					if (abgelieferteSNrs.get(j).getCSeriennrChargennr().equals(snr)) {
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

	public BigDecimal getAnzahlInFertigung(Integer artikelIId, java.sql.Date tAbDatum, TheClientDto theClientDto) {
		return getAnzahlInFertigung(artikelIId, tAbDatum, null, theClientDto);
	}

	public BigDecimal getAnzahlInFertigung(Integer artikelIId, java.sql.Date tAbDatum, Integer partnerIIdStandort,
			TheClientDto theClientDto) {

		return getAnzahlInFertigung(artikelIId, tAbDatum, partnerIIdStandort, theClientDto.getMandant(), theClientDto);
	}

	public BigDecimal getAnzahlBisherVerwendet(Integer stuecklisteIId, Integer losIId, TheClientDto theClientDto) {

		BigDecimal anzahl = new BigDecimal(0);

		LosDto lDto = losFindByPrimaryKey(losIId);
		// --------------------------------------------------------------
		// ---------
		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT sum(la.n_menge) FROM FLRLosablieferung la WHERE la.flrlos.c_nr < '" + lDto.getCNr()
				+ "' AND la.flrlos.stueckliste_i_id=" + stuecklisteIId;

		org.hibernate.Query summe = session.createQuery(sQuery);

		List<?> resultList = summe.list();
		Iterator<?> resultListIterator = resultList.iterator();
		if (resultListIterator.hasNext()) {
			BigDecimal bdSumme = (BigDecimal) resultListIterator.next();
			if (bdSumme != null) {
				anzahl = bdSumme;
			}
		}

		return anzahl;
	}

	public BigDecimal getAnzahlInFertigung(Integer artikelIId, java.sql.Date tAbDatum, Integer partnerIIdStandort,
			String mandantCNr, TheClientDto theClientDto) {
		BigDecimal anzahl = new BigDecimal(0);

		ArrayList<AnzahlInFertigungDto> anzahlInFertigungDto = getAnzahlInFertigungDtos(artikelIId, tAbDatum,
				partnerIIdStandort, mandantCNr, theClientDto);
		for (AnzahlInFertigungDto dto : anzahlInFertigungDto) {
			anzahl = anzahl.add(dto.getBdMenge());
		}

		return anzahl;
	}

	public ArrayList<AnzahlInFertigungDto> getAnzahlInFertigungDtos(Integer artikelIId, TheClientDto theClientDto) {
		return getAnzahlInFertigungDtos(artikelIId, null, null, theClientDto.getMandant(), theClientDto);
	}

	private ArrayList<AnzahlInFertigungDto> getAnzahlInFertigungDtos(Integer artikelIId, java.sql.Date tAbDatum,
			Integer partnerIIdStandort, String mandantCNr, TheClientDto theClientDto) {

		ArrayList<AnzahlInFertigungDto> dtos = new ArrayList<AnzahlInFertigungDto>();
		Session session = null;

		StuecklisteDto stuecklisteDto = getStuecklisteFac().stuecklisteFindByMandantCNrArtikelIIdOhneExc(artikelIId,
				mandantCNr);
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
			crit.add(Restrictions.eq(FertigungFac.FLR_INTERNE_BESTELLUNG_STUECKLISTE_I_ID, stuecklisteDto.getIId()));
			if (tAbDatum != null) {
				crit.add(Restrictions.ge(FertigungFac.FLR_LOS_T_PRODUKTIONSENDE, tAbDatum));
			}

			List<?> resultList = crit.list();
			Iterator<?> resultListIterator = resultList.iterator();
			while (resultListIterator.hasNext()) {
				FLRLos flrLos = (FLRLos) resultListIterator.next();

				if (partnerIIdStandort != null) {
					if (!flrLos.getFlrlager().getParnter_i_id_standort().equals(partnerIIdStandort)) {
						continue;
					}
				}

				AnzahlInFertigungDto dto = new AnzahlInFertigungDto();
				dto.setTProduktionsende(new Timestamp(flrLos.getT_produktionsende().getTime()));
				dto.setBdMenge(BigDecimal.ZERO);

				if (flrLos.getStatus_c_nr().equals(FertigungFac.STATUS_TEILERLEDIGT)
						|| flrLos.getStatus_c_nr().equals(FertigungFac.STATUS_GESTOPPT)) {
					// Erledigte Menge bestimmen.
					BigDecimal bdErledigt = getErledigteMenge(flrLos.getI_id(), theClientDto);
					// Wenn noch was offen ist (es koennte auch
					// Ueberlieferungen geben).
					if (flrLos.getN_losgroesse().compareTo(bdErledigt) > 0) {
						dto.setBdMenge(flrLos.getN_losgroesse().subtract(bdErledigt));
					}
				} else {

					dto.setBdMenge(flrLos.getN_losgroesse());
				}

				dtos.add(dto);

			}
		}

		return dtos;
	}

	public BigDecimal getAnzahlInFertigung(Integer artikelIId, TheClientDto theClientDto) {
		return getAnzahlInFertigung(artikelIId, null, theClientDto);
	}

	public void loseEinesSchachteplansAbliefern(HashMap<Integer, BigDecimal> hmLose, TheClientDto theClientDto) {
		Session session = null;

		Iterator it = hmLose.keySet().iterator();
		while (it.hasNext()) {
			Integer losIId = (Integer) it.next();
			LosDto losDto = losFindByPrimaryKey(losIId);

			BigDecimal bdAusschuss = hmLose.get(losIId);

			BigDecimal bdErledigteMenge = getErledigteMenge(losIId, theClientDto);

			BigDecimal bdAbzuliefern = losDto.getNLosgroesse().subtract(bdErledigteMenge).subtract(bdAusschuss);

			if (bdAbzuliefern.doubleValue() > 0) {
				LosablieferungDto laDto = new LosablieferungDto();
				laDto.setLosIId(losIId);
				laDto.setNMenge(bdAbzuliefern);

				createLosablieferung(laDto, theClientDto, true);
			}

		}

	}

	public ArrayList<LosDto> getAusgegebeneLoseEinerSchachtelplannummer(String cSchachtelplannummer,
			TheClientDto theClientDto) {
		Session session = null;

		ArrayList<LosDto> lose = new ArrayList<LosDto>();

		try {

			if (cSchachtelplannummer != null) {
				// --------------------------------------------------------------
				// ---------
				session = FLRSessionFactory.getFactory().openSession();

				Criteria crit = session.createCriteria(FLRLos.class);

				String[] stati = new String[3];
				stati[0] = FertigungFac.STATUS_IN_PRODUKTION;
				stati[1] = FertigungFac.STATUS_AUSGEGEBEN;
				stati[2] = FertigungFac.STATUS_TEILERLEDIGT;

				crit.add(Restrictions.in(FertigungFac.FLR_LOS_STATUS_C_NR, stati));

				crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));

				crit.add(Restrictions.eq("c_schachtelplan", cSchachtelplannummer));

				crit.addOrder(Order.asc("c_nr"));

				List<?> resultList = crit.list();
				Iterator<?> resultListIterator = resultList.iterator();
				while (resultListIterator.hasNext()) {
					FLRLos flrLos = (FLRLos) resultListIterator.next();

					LosDto losDto = getFertigungFac().losFindByPrimaryKey(flrLos.getI_id());

					lose.add(losDto);

				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} finally {
			closeSession(session);
		}
		return lose;
	}

	public ArrayList<LosDto> getLoseInFertigung(Integer artikelIId, TheClientDto theClientDto) {
		Session session = null;

		ArrayList<LosDto> lose = new ArrayList<LosDto>();

		try {
			StuecklisteDto stuecklisteDto = getStuecklisteFac().stuecklisteFindByMandantCNrArtikelIIdOhneExc(artikelIId,
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

				crit.add(Restrictions.in(FertigungFac.FLR_LOS_STATUS_C_NR, stati));
				crit.add(
						Restrictions.eq(FertigungFac.FLR_INTERNE_BESTELLUNG_STUECKLISTE_I_ID, stuecklisteDto.getIId()));

				List<?> resultList = crit.list();
				Iterator<?> resultListIterator = resultList.iterator();
				while (resultListIterator.hasNext()) {
					FLRLos flrLos = (FLRLos) resultListIterator.next();

					LosDto losDto = getFertigungFac().losFindByPrimaryKey(flrLos.getI_id());

					if (flrLos.getStatus_c_nr().equals(FertigungFac.STATUS_TEILERLEDIGT)) {
						// Erledigte Menge bestimmen.
						BigDecimal bdErledigt = getErledigteMenge(flrLos.getI_id(), theClientDto);
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

	public BigDecimal getErledigteMenge(Integer losIId, TheClientDto theClientDto) throws EJBExceptionLP {

		// Optimieren
		String sQuery = "select sum(la.n_menge) from FLRLosablieferung la WHERE la.los_i_id=" + losIId;

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

	public void stoppeProduktion(Integer losIId, TheClientDto theClientDto) throws EJBExceptionLP {

		// try {
		Los los = em.find(Los.class, losIId);
		if (los == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		if (los.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT, "");
		} else if (los.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN, "");
		} else if (los.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT, "");
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

	public void stoppeProduktionRueckgaengig(Integer losIId, TheClientDto theClientDto) throws EJBExceptionLP {
		// try {
		Los los = em.find(Los.class, losIId);
		if (los == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		if (los.getStatusCNr().equals(FertigungFac.STATUS_GESTOPPT)) {
			LosablieferungDto[] la = losablieferungFindByLosIId(losIId, false, theClientDto);
			if (la.length == 0) {
				los.setStatusCNr(FertigungFac.STATUS_IN_PRODUKTION);
			} else {
				los.setStatusCNr(FertigungFac.STATUS_TEILERLEDIGT);
			}
			los.setPersonalIIdProduktionsstop(null);
			los.setTProduktionsstop(null);
		} else if (los.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT, "");
		} else if (los.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN, "");
		} else if (los.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT, "");
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_AUSGEGEBEN, "");
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	private BigDecimal getErledigterMaterialwertEinerSollpositionNEU(BigDecimal bdLosgroesse,
			BigDecimal bdErledigteMenge, TheClientDto theClientDto, LosDto losDto, BigDecimal bdAusgegeben,
			BigDecimal gesamtpreis, BigDecimal sollmenge) {

		// wenn volle menge erledigt oder sogar mehr oder Los=erledigt
		if (bdLosgroesse.compareTo(bdErledigteMenge) <= 0
				|| losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			// dann alles
			return gesamtpreis;
		} else {
			// Sollsatzgroesse berechnen
			BigDecimal bdSollsatzgroesse = sollmenge.multiply(bdErledigteMenge).divide(bdLosgroesse, 6,
					BigDecimal.ROUND_HALF_EVEN);
			// weniger oder gleich wie sollmenge ausgegeben
			if (bdAusgegeben.compareTo(bdSollsatzgroesse) <= 0) {
				// dann alles
				return gesamtpreis;
			}
			// wenn mehr ausgegeben
			else {

				BigDecimal bdEinzelpreis = new BigDecimal(0);

				if (bdAusgegeben.doubleValue() > 0) {
					bdEinzelpreis = gesamtpreis.divide(bdAusgegeben, 4, BigDecimal.ROUND_HALF_EVEN);
				}

				// dann mit sollsatzgroesse
				return bdSollsatzgroesse.multiply(bdEinzelpreis);
			}
		}
	}

	public BigDecimal getErledigterMaterialwertNEU(LosDto losDto, TheClientDto theClientDto) {

		// Neue Funktion, die Adi gebaut hat: Liefert den erledigten
		// Materialwert in einem Query

		BigDecimal bdErledigteMenge = getErledigteMenge(losDto.getIId(), theClientDto);

		// Neue Funktion, die Adi gebaut hat: Liefert den erledigten
		// Materialwert in einem Query

		Query query = em.createNamedQuery("getErledigterMaterialwert");
		query.setParameter("losiid", losDto.getIId());
		Collection<?> cl = query.getResultList();
		Iterator it = cl.iterator();

		BigDecimal bdWert = new BigDecimal(0);
		while (it.hasNext()) {
			Erledigtermaterialwert pos = (Erledigtermaterialwert) it.next();

			bdWert = bdWert.add(getErledigterMaterialwertEinerSollpositionNEU(losDto.getNLosgroesse(), bdErledigteMenge,
					theClientDto, losDto, pos.getNAusmenge(), pos.getNPreis(), pos.getNSollmenge()));
		}

		return bdWert;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int wiederholendeLoseAnlegen(TheClientDto theClientDto) {

		int iAnzahlAngelegterLose = 0;
		DateFormatSymbols symbols = new DateFormatSymbols(theClientDto.getLocUi());
		String[] defaultMonths = symbols.getMonths();
		int iStandarddurchlaufzeit = 0;
		try {
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.INTERNEBESTELLUNG_DEFAULTDURCHLAUFZEIT);
			iStandarddurchlaufzeit = ((Integer) parameter.getCWertAsObject()).intValue();
		} catch (RemoteException ex2) {
			throwEJBExceptionLPRespectOld(ex2);
		}

		Session session = FLRSessionFactory.getFactory().openSession();

		Criteria crit = session.createCriteria(FLRWiederholendelose.class);

		crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));
		crit.add(Restrictions.eq(FertigungFac.FLR_WIEDERHOLENDELOSE_B_VERSTECKT, Helper.boolean2Short(false)));

		List<?> resultList = crit.list();
		Iterator<?> resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {
			FLRWiederholendelose flrWiederholendelose = (FLRWiederholendelose) resultListIterator.next();

			// Naechster faelliger Termin nach Heute
			Calendar cBeginn = Calendar.getInstance();
			cBeginn.setTimeInMillis(flrWiederholendelose.getT_termin().getTime());

			String intervall = flrWiederholendelose.getAuftragwiederholungsintervall_c_nr();

			Timestamp tHeute = Helper.cutTimestamp(new Timestamp(System.currentTimeMillis()));

			while (cBeginn.getTimeInMillis() < tHeute.getTime()) {

				if (intervall.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_2WOECHENTLICH)) {
					cBeginn.set(Calendar.DAY_OF_MONTH, cBeginn.get(Calendar.DAY_OF_MONTH) + 14);
				} else if (intervall.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_WOECHENTLICH)) {
					cBeginn.set(Calendar.DAY_OF_MONTH, cBeginn.get(Calendar.DAY_OF_MONTH) + 7);
				}

				if (intervall.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_JAHR)) {
					cBeginn.set(Calendar.YEAR, cBeginn.get(Calendar.YEAR) + 1);
				} else if (intervall.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_2JAHR)) {
					cBeginn.set(Calendar.YEAR, cBeginn.get(Calendar.YEAR) + 2);
				} else if (intervall.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_3JAHR)) {
					cBeginn.set(Calendar.YEAR, cBeginn.get(Calendar.YEAR) + 3);
				} else if (intervall.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_4JAHR)) {
					cBeginn.set(Calendar.YEAR, cBeginn.get(Calendar.YEAR) + 4);
				} else if (intervall.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_5JAHR)) {
					cBeginn.set(Calendar.YEAR, cBeginn.get(Calendar.YEAR) + 5);
				} else if (intervall.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_MONATLICH)) {
					cBeginn.set(Calendar.MONTH, cBeginn.get(Calendar.MONTH) + 1);
				} else if (intervall.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_QUARTAL)) {
					cBeginn.set(Calendar.MONTH, cBeginn.get(Calendar.MONTH) + 3);
				}
			}

			Timestamp tBeginndatumFuerLos = new Timestamp(cBeginn.getTimeInMillis());
			// Voreilende Tage abziehen

			String monatsname = defaultMonths[cBeginn.get(Calendar.MONTH)];
			int iJahr = cBeginn.get(Calendar.YEAR);

			int iTageVoreilend = flrWiederholendelose.getI_tagevoreilend();
			cBeginn.set(Calendar.DAY_OF_MONTH, cBeginn.get(Calendar.DAY_OF_MONTH) - iTageVoreilend);

			Timestamp tAnlagedatum = new Timestamp(cBeginn.getTimeInMillis());

			if (tAnlagedatum.before(tHeute) || tAnlagedatum.equals(tHeute)) {
				// try {
				Query query = em.createNamedQuery("LosfindWiederholendeloseIIdTProduktionsbeginnMandantCNr");
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

					WiederholendeloseDto dto = wiederholendeloseFindByPrimaryKey(flrWiederholendelose.getI_id());
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
					losDto.setPartnerIIdFertigungsort(dto.getPartnerIIdFertigungsort());
					losDto.setStuecklisteIId(dto.getStuecklisteIId());
					losDto.setTProduktionsbeginn(new java.sql.Date(tBeginndatumFuerLos.getTime()));

					// Produktionsende

					try {

						int laufzeit = iStandarddurchlaufzeit;
						if (dto.getStuecklisteIId() != null) {
							StuecklisteDto stuecklisteDto = getStuecklisteFac()
									.stuecklisteFindByPrimaryKey(dto.getStuecklisteIId(), theClientDto);
							if (stuecklisteDto.getNDefaultdurchlaufzeit() != null) {
								laufzeit = stuecklisteDto.getNDefaultdurchlaufzeit().intValue();
							}
						}
						Calendar cTemp = Calendar.getInstance();
						cTemp.setTimeInMillis(tBeginndatumFuerLos.getTime());
						cTemp.set(Calendar.DAY_OF_MONTH, cTemp.get(Calendar.DAY_OF_MONTH) + laufzeit);
						losDto.setTProduktionsende(new java.sql.Date(cTemp.getTime().getTime()));
						context.getBusinessObject(FertigungFac.class).createLos(losDto, theClientDto);
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

	@org.jboss.ejb3.annotation.TransactionTimeout(6000)
	public void angelegteLoseVerdichten(ArrayList<Integer> losIIs, TheClientDto theClientDto) {
		// PJ19742
		// Lose Stuecklistenrein verdichten

		LinkedHashMap<Integer, ArrayList<Object[]>> lhm = new LinkedHashMap<Integer, ArrayList<Object[]>>();

		for (int i = 0; i < losIIs.size(); i++) {
			LosDto lDto = losFindByPrimaryKey(losIIs.get(i));

			if (lDto.getStuecklisteIId() != null && lDto.getStatusCNr().equals(LocaleFac.STATUS_ANGELEGT)) {
				boolean bEsGibtNachtraeglicheMaterialentnahmen = false;
				LossollmaterialDto[] sollDtos = lossollmaterialFindByLosIId(lDto.getIId());
				for (int j = 0; j < sollDtos.length; j++) {
					if (Helper.short2boolean(sollDtos[j].getBNachtraeglich())) {
						bEsGibtNachtraeglicheMaterialentnahmen = true;
					}
				}

				if (bEsGibtNachtraeglicheMaterialentnahmen == false) {

					ArrayList<Object[]> lose = null;
					if (lhm.containsKey(lDto.getStuecklisteIId())) {
						lose = lhm.get(lDto.getStuecklisteIId());
					} else {
						lose = new ArrayList<Object[]>();
					}

					Object[] o = new Object[3];

					o[0] = lDto.getIId();
					o[1] = lDto;

					lose.add(o);

					lhm.put(lDto.getStuecklisteIId(), lose);

				}

			}

		}

		Iterator it = lhm.keySet().iterator();
		while (it.hasNext()) {
			Integer stuecklisteIId = (Integer) it.next();

			ArrayList<Object[]> lose = lhm.get(stuecklisteIId);

			if (lose.size() > 1) {

				// Sortieren
				for (int m = lose.size() - 1; m > 0; --m) {
					for (int j = 0; j < m; ++j) {
						Object[] o = lose.get(j);
						Object[] o1 = lose.get(j + 1);

						java.sql.Date d1 = ((LosDto) o[1]).getTProduktionsbeginn();
						java.sql.Date d2 = ((LosDto) o1[1]).getTProduktionsbeginn();

						if (d1.compareTo(d2) > 0) {
							lose.set(j, o1);
							lose.set(j + 1, o);
						}
					}
				}

				// Nun auf das Los mit den aeltesten Produktionsbeginn
				// Zusammenfassen
				LosDto lDto = (LosDto) ((Object[]) lose.get(0))[1];

				BigDecimal neueLosgroesse = lDto.getNLosgroesse();

				for (int i = 1; i < lose.size(); i++) {
					LosDto lDtoZuStornieren = (LosDto) ((Object[]) lose.get(i))[1];
					neueLosgroesse = neueLosgroesse.add(lDtoZuStornieren.getNLosgroesse());

					storniereLos(lDtoZuStornieren.getIId(), true, theClientDto);

				}

				lDto.setNLosgroesse(neueLosgroesse);

				updateLos(lDto, theClientDto);

			}

		}

	}

	public BigDecimal getErledigteArbeitszeitEinerLosablieferung(Integer losablieferungIId, boolean bWertOderZeit,
			boolean bMitMaschinenZeit, TheClientDto theClientDto) throws EJBExceptionLP {
		return getErledigteArbeitszeitEinerLosablieferung(losablieferungIId, bWertOderZeit, bMitMaschinenZeit,
				theClientDto, false);
	}

	/**
	 * Ist-Arbeitszeit fuer eine Ablieferung pro abgelieferter Einheit bestimmen.
	 * 
	 * @param losablieferungIId Integer
	 * @param bWertOderZeit     boolean fuer Wert = true, fuer Zeit = false
	 * @param theClientDto      String
	 * @return BigDecimal
	 * @throws EJBExceptionLP
	 */
	private BigDecimal getErledigteArbeitszeitEinerLosablieferung(Integer losablieferungIId, boolean bWertOderZeit,
			boolean bMitMaschinenZeit, TheClientDto theClientDto, boolean bRekursiv) throws EJBExceptionLP {
		BigDecimal bdErgebnis = new BigDecimal(0);
		LosablieferungDto losablieferungDto = losablieferungFindByPrimaryKey(losablieferungIId, false, theClientDto);
		try {
			// Alle Ablieferungen auf dieses Los aufsteigend nach datum sortiert

			LosablieferungDto[] abl = losablieferungFindByLosIId(losablieferungDto.getLosIId(), false, theClientDto);
			// Rueckwaertsschleife, da die werte der vorgaenger berechnet werden
			// muessen (rekursiv)
			for (int i = abl.length - 1; i > 0; i--) {
				// wenn ich die aktuelle gefunden hab, subtrahier ich den wert
				// des vorgaengers
				if (abl[i].getIId().equals(losablieferungIId)) {
					bdErgebnis = bdErgebnis.subtract(getErledigteArbeitszeitEinerLosablieferung(abl[i - 1].getIId(),
							bWertOderZeit, bMitMaschinenZeit, theClientDto, true).multiply(abl[i - 1].getNMenge()));
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
				if (!abl[i].getTAendern().after(losablieferungDto.getTAendern())) {
					bdErledigt = bdErledigt.add(abl[i].getNMenge());
				}
			}

			LosDto losDto = losFindByPrimaryKey(losablieferungDto.getLosIId());
			// Wenn Los ueberliefert oder Erledigt, dann zaehlen alle Zeidaten
			// zum Arbeitszeitwert
			boolean bAlleZeitenZaehlenFuerArbeitswert = false;
			if (summeAblieferungen.doubleValue() >= losDto.getNLosgroesse().doubleValue()
					|| losDto.getStatusCNr().equals(LocaleFac.STATUS_ERLEDIGT)) {
				if (bRekursiv == false) {
					bAlleZeitenZaehlenFuerArbeitswert = true;
				}
			}

			BigDecimal bdFaktor = bdErledigt.divide(losDto.getNLosgroesse(), 10, BigDecimal.ROUND_HALF_EVEN);
			// ------------------------------------------------------------------
			// ----
			// Sollzeiten
			LossollarbeitsplanDto[] soll = getFertigungFac()
					.lossollarbeitsplanFindByLosIId(losablieferungDto.getLosIId());
			// Sollzeiten nach Artikel verdichten und mit Sollsatzfaktor
			// multiplizieren
			HashMap<Integer, BigDecimal> listSollVerdichtet = new HashMap<Integer, BigDecimal>();
			for (int i = 0; i < soll.length; i++) {
				BigDecimal bdBisher = listSollVerdichtet.get(soll[i].getArtikelIIdTaetigkeit());
				if (bdBisher == null) {
					listSollVerdichtet.put(soll[i].getArtikelIIdTaetigkeit(),
							soll[i].getNGesamtzeit().multiply(bdFaktor));
				} else {
					listSollVerdichtet.put(soll[i].getArtikelIIdTaetigkeit(),
							bdBisher.add(soll[i].getNGesamtzeit().multiply(bdFaktor)));
				}
			}
			// ------------------------------------------------------------------
			// ----
			// gebuchte Zeiten holen
			ParametermandantDto parametermandantDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_ISTZEITEN_GLEICH_SOLLZEITEN);
			boolean bSollGleichIstzeiten = false;
			if (((java.lang.Boolean) parametermandantDto.getCWertAsObject()).booleanValue() == true) {
				bSollGleichIstzeiten = true;
			}
			// Maschinenzeiten
			AuftragzeitenDto[] zeitenMaschine = null;
			AuftragzeitenDto[] zeiten = null;
			if (bSollGleichIstzeiten == false) {

				// Maschinenzeiten
				zeitenMaschine = getZeiterfassungFac().getAllMaschinenzeitenEinesBeleges(losablieferungDto.getLosIId(),
						null, null, null, theClientDto);
				// "normale" Zeiten
				zeiten = getZeiterfassungFac().getAllZeitenEinesBeleges(LocaleFac.BELEGART_LOS,
						losablieferungDto.getLosIId(), null, null, null, null,
						ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ARTIKEL, theClientDto);
			} else {
				zeiten = new AuftragzeitenDto[listSollVerdichtet.size()];
				zeitenMaschine = new AuftragzeitenDto[0];
				bAlleZeitenZaehlenFuerArbeitswert = true;
				int row = 0;
				for (Iterator<?> iter = listSollVerdichtet.keySet().iterator(); iter.hasNext();) {
					Integer artikelIId = (Integer) iter.next();
					BigDecimal gesamtzeit = listSollVerdichtet.get(artikelIId);
					AuftragzeitenDto az = new AuftragzeitenDto();
					az.setArtikelIId(artikelIId);
					az.setDdDauer(new Double(gesamtzeit.doubleValue()));
					BigDecimal bdPreis = getLagerFac()
							.getGemittelterGestehungspreisAllerLaegerEinesMandanten(artikelIId, theClientDto);
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
					if ((zeitenMaschine[i].getTsEnde() != null
							&& zeitenMaschine[i].getTsEnde().before(losablieferungDto.getTAendern()))
							|| bAlleZeitenZaehlenFuerArbeitswert == true) {
						BigDecimal bdBisherWert = listIstWert.get(zeitenMaschine[i].getArtikelIId());
						BigDecimal bdBisherZeit = listIstZeit.get(zeitenMaschine[i].getArtikelIId());
						if (bdBisherWert == null) {
							listIstWert.put(zeitenMaschine[i].getArtikelIId(), zeitenMaschine[i].getBdKosten());
							listIstZeit.put(zeitenMaschine[i].getArtikelIId(),
									new BigDecimal(zeitenMaschine[i].getDdDauer()));
						} else {
							listIstWert.put(zeitenMaschine[i].getArtikelIId(),
									bdBisherWert.add(zeitenMaschine[i].getBdKosten()));
							listIstZeit.put(zeitenMaschine[i].getArtikelIId(),
									bdBisherZeit.add(new BigDecimal(zeitenMaschine[i].getDdDauer())));
						}
					}
				}
			}
			// "normale" Zeiten
			for (int i = 0; i < zeiten.length; i++) {
				// Wenn vor der Ablieferung gebucht, dann addieren
				if ((zeiten[i].getTsEnde() != null && zeiten[i].getTsEnde().before(losablieferungDto.getTAendern()))
						|| bAlleZeitenZaehlenFuerArbeitswert == true) {
					BigDecimal bdBisherWert = listIstWert.get(zeiten[i].getArtikelIId());
					BigDecimal bdBisherZeit = listIstZeit.get(zeiten[i].getArtikelIId());
					if (bdBisherWert == null) {
						listIstWert.put(zeiten[i].getArtikelIId(), zeiten[i].getBdKosten());
						listIstZeit.put(zeiten[i].getArtikelIId(), new BigDecimal(zeiten[i].getDdDauer()));
					} else {
						listIstWert.put(zeiten[i].getArtikelIId(), bdBisherWert.add(zeiten[i].getBdKosten()));
						listIstZeit.put(zeiten[i].getArtikelIId(),
								bdBisherZeit.add(new BigDecimal(zeiten[i].getDdDauer())));
					}
				}
			}
			// ------------------------------------------------------------------
			// ----
			for (Iterator<?> iter = listIstZeit.keySet().iterator(); iter.hasNext();) {
				Integer artikelIId = (Integer) iter.next();
				// Wenn das Los erledigt ist, dann zaehlen alle
				if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)
						|| (bdErledigt.doubleValue() >= losDto.getNLosgroesse().doubleValue())) {
					if (bWertOderZeit) {
						bdErgebnis = bdErgebnis.add(listIstWert.get(artikelIId));
					} else {
						bdErgebnis = bdErgebnis.add(listIstZeit.get(artikelIId));
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
								bdErgebnis = bdErgebnis.add(listIstWert.get(artikelIId));
							} else {
								bdErgebnis = bdErgebnis.add(listIstZeit.get(artikelIId));
							}
						}
						// Sollsatzzeit ueberschritten
						else {
							if (bWertOderZeit) {
								bdErgebnis = bdErgebnis.add(listIstWert.get(artikelIId).multiply(bdSollZeit)
										.divide(bdIstZeit, 4, BigDecimal.ROUND_HALF_EVEN));
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

			return bdErgebnis.divide(losablieferungDto.getNMenge(), 4, BigDecimal.ROUND_HALF_EVEN);
		} else {
			return new BigDecimal(0);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public AuftragNachkalkulationDto getWerteAusUnterlosen(LosDto losDto,
			HashMap<Integer, LosInfosFuerWerteAusUnterlosen> hmLosInfos, AuftragNachkalkulationDto abNachkalkulationDto,
			TheClientDto theClientDto) {

		if (abNachkalkulationDto.hasNoResults(losDto.getIId())) {
			return abNachkalkulationDto;
		}

		Session sessionIstMat = FLRSessionFactory.getFactory().openSession();
		String sQueryIstMat = "SELECT weref.zubuchung.i_belegartid,weref.zubuchung.i_belegartpositionid, weref.n_verbrauchtemenge, (SELECT la.n_arbeitszeitwertdetailliert FROM FLRLosablieferung la WHERE la.i_id=weref.zubuchung.i_belegartpositionid) from FLRWareneingangsreferez weref WHERE weref.zubuchung.c_belegartnr='"
				+ LocaleFac.BELEGART_LOSABLIEFERUNG
				+ "' AND weref.abbuchung.n_menge > 0 AND weref.n_verbrauchtemenge > 0 AND weref.abbuchung.c_belegartnr='"
				+ LocaleFac.BELEGART_LOS + "' AND weref.abbuchung.i_belegartid=" + losDto.getIId();
		org.hibernate.Query queryIstmat = sessionIstMat.createQuery(sQueryIstMat);

		java.util.List resultListIstMat = queryIstmat.list();

		abNachkalkulationDto.add2Results(losDto.getIId(), resultListIstMat.size());

		Iterator itIstmat = resultListIstMat.iterator();
		// Caching

		if (hmLosInfos == null) {
			hmLosInfos = new HashMap<Integer, LosInfosFuerWerteAusUnterlosen>();
		}

		while (itIstmat.hasNext()) {
			Object[] o = (Object[]) itIstmat.next();

			try {
				Integer losIId = (Integer) o[0];
				Integer losablieferung_i_id = (Integer) o[1];
				BigDecimal bdVerbrauchteMenge = (BigDecimal) o[2];
				BigDecimal bdAzwertdetailliertAusLosablieferung = (BigDecimal) o[3];

				LosDto losDtoUnterlos = null;
				BigDecimal bdGesamtAbgeliefert = null;
				Double dPers = null;
				Double dMasch = null;

				LossollmaterialDto[] lossollmaterialDtosUnterlos = null;

				if (hmLosInfos.containsKey(losIId)) {
					LosInfosFuerWerteAusUnterlosen infos = hmLosInfos.get(losIId);
					losDtoUnterlos = infos.losDto;
					bdGesamtAbgeliefert = infos.bdGesamtAbgeliefert;
					dPers = infos.dPersonalzeiten;
					dMasch = infos.dMaschinenzeiten;

				} else {

					losDtoUnterlos = getFertigungFac().losFindByPrimaryKey(losIId);

					bdGesamtAbgeliefert = getFertigungFac().getErledigteMenge(losIId, theClientDto);

					dPers = getZeiterfassungFac().getSummeZeitenEinesBeleges(LocaleFac.BELEGART_LOS,
							losDtoUnterlos.getIId(), null, null, null, null, theClientDto);

					dMasch = getZeiterfassungFac().getSummeMaschinenZeitenEinesBeleges(losDtoUnterlos.getIId(), null,
							null, theClientDto);

					LosInfosFuerWerteAusUnterlosen infos = new LosInfosFuerWerteAusUnterlosen();
					infos.losDto = losDtoUnterlos;
					infos.bdGesamtAbgeliefert = bdGesamtAbgeliefert;
					infos.dPersonalzeiten = dPers;
					infos.dMaschinenzeiten = dMasch;

					hmLosInfos.put(losIId, infos);
				}

				abNachkalkulationDto.setHmLosInfos(hmLosInfos);

				// SP9787
				BigDecimal azWert = bdAzwertdetailliertAusLosablieferung.multiply(bdVerbrauchteMenge);
				

				abNachkalkulationDto.setBdGestehungswertmaterialist(
						abNachkalkulationDto.getBdGestehungswertmaterialist().subtract(azWert));
				abNachkalkulationDto
						.setBdGestehungswertarbeitist(abNachkalkulationDto.getBdGestehungswertarbeitist().add(azWert));

				// Zeit duch die Gesamtablieferungen
				// dividieren und mal bMenge
				dPers = new BigDecimal(dPers).divide(losDtoUnterlos.getNLosgroesse(), 4, BigDecimal.ROUND_HALF_EVEN)
						.multiply(bdVerbrauchteMenge).doubleValue();
				abNachkalkulationDto.setDdArbeitszeitist(abNachkalkulationDto.getDdArbeitszeitist() + dPers);

				dMasch = new BigDecimal(dMasch).divide(losDtoUnterlos.getNLosgroesse(), 4, BigDecimal.ROUND_HALF_EVEN)
						.multiply(bdVerbrauchteMenge).doubleValue();
				abNachkalkulationDto.setDdMaschinenzeitist(abNachkalkulationDto.getDdMaschinenzeitist() + dMasch);

				/*
				 * System.out.println("Los: " + losDto.getCNr() + " Stueckliste: " +
				 * stklDto.getArtikelDto().getCNr() + " UnterlosLos: " + losDtoUnterlos.getCNr()
				 * + " Artikel: " + aDto.getCNr() + " Menge:" + bdMenge);
				 */

				abNachkalkulationDto = getFertigungFac().getWerteAusUnterlosen(losDtoUnterlos, hmLosInfos,
						abNachkalkulationDto, theClientDto);

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);

			}
		}

		return abNachkalkulationDto;
	}

	private BigDecimal getErledigterArbeitszeitwertAusUnterlosen(LosDto losDto, TheClientDto theClientDto) {
		BigDecimal bdErledigteMenge = getErledigteMenge(losDto.getIId(), theClientDto);
		LossollmaterialDto[] sollmat = lossollmaterialFindByLosIId(losDto.getIId());
		BigDecimal bdWert = new BigDecimal(0);
		for (int i = 0; i < sollmat.length; i++) {
			bdWert = bdWert.add(getErledigterArbeitszeitwertEinerSollpositionAusUnterlosen(sollmat[i],
					losDto.getNLosgroesse(), bdErledigteMenge, theClientDto));
		}
		return bdWert;
	}

	public java.sql.Date getFruehesterEintrefftermin(Integer artikelIId, TheClientDto theClientDto) {
		java.sql.Date dFruehesterEintreffTermin = null;
		try {
			BestellpositionDto[] bestellpositionDtos = getBestellpositionFac()
					.bestellpositionfindByArtikelOrderByTAuftragsbestaetigungstermin(artikelIId, theClientDto);
			if (bestellpositionDtos != null) {
				for (int i = 0; i < bestellpositionDtos.length; i++) {
					if (!bestellpositionDtos[i].getBestellpositionstatusCNr()
							.equals(BestellpositionFac.BESTELLPOSITIONSTATUS_ERLEDIGT)) {

						if (bestellpositionDtos[i].getNOffeneMenge() == null
								|| bestellpositionDtos[i].getNOffeneMenge().doubleValue() > 0) {

							BestellungDto bestellungDto = getBestellungFac()
									.bestellungFindByPrimaryKey(bestellpositionDtos[i].getBestellungIId());
							// SP3130
							if (!bestellungDto.getStatusCNr().equals(BestellungFac.BESTELLSTATUS_ERLEDIGT)) {
								// SP8573
								if (!bestellungDto.getStatusCNr().equals(BestellungFac.BESTELLSTATUS_STORNIERT)) {

									// SP5288
									if (!bestellungDto.getBestellungartCNr()
											.equals(BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR)) {

										dFruehesterEintreffTermin = bestellpositionDtos[i]
												.getTAuftragsbestaetigungstermin();
										break;
									}
								}
							}

						}
					}
				}

			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		// PJ18767
		ArrayList<LosDto> alLose = getFertigungFac().getLoseInFertigung(artikelIId, theClientDto);
		for (int i = 0; i < alLose.size(); i++) {

			if (dFruehesterEintreffTermin == null) {
				dFruehesterEintreffTermin = alLose.get(i).getTProduktionsende();
			} else {
				if (dFruehesterEintreffTermin.after(alLose.get(i).getTProduktionsende())) {
					dFruehesterEintreffTermin = alLose.get(i).getTProduktionsende();
				}
			}

		}

		return dFruehesterEintreffTermin;
	}

	private BigDecimal getErledigterArbeitszeitwertEinerSollpositionAusUnterlosen(LossollmaterialDto lossollmaterialDto,
			BigDecimal bdLosgroesse, BigDecimal bdErledigteMenge, TheClientDto theClientDto) {
		// BigDecimal bdAusgegeben =
		// getAusgegebeneMenge(lossollmaterialDto.getIId(), idUser);
		// BigDecimal bdEinzelpreis =
		// getAusgegebeneMengePreis(lossollmaterialDto.getIId(),
		// idUser);
		return new BigDecimal(0);/**
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

	public Integer gebeMaterialNachtraeglichAus(LossollmaterialDto lossollmaterialDto,
			LosistmaterialDto losistmaterialDto, List<SeriennrChargennrMitMengeDto> listSnrChnr,
			boolean bReduziereFehlmenge, TheClientDto theClientDto) throws EJBExceptionLP {
		return gebeMaterialNachtraeglichAus(lossollmaterialDto, losistmaterialDto, listSnrChnr, bReduziereFehlmenge,
				false, theClientDto);
	}

	public Integer gebeMaterialNachtraeglichAus(LossollmaterialDto lossollmaterialDto,
			LosistmaterialDto losistmaterialDto, List<SeriennrChargennrMitMengeDto> listSnrChnr,
			boolean bReduziereFehlmenge, boolean bKommissionierterminal, TheClientDto theClientDto)
			throws EJBExceptionLP {

		try {
			if (lossollmaterialDto == null || losistmaterialDto == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
						new Exception("lossollmaterialDto == null || losistmaterialDto == null"));
			}

			LosDto losDto = losFindByPrimaryKey(lossollmaterialDto.getLosIId());

			if (losDto.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT, "");
			} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN, "");
			} else if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT, "");
			}

			if (lossollmaterialDto.getIId() == null || !bReduziereFehlmenge) {
				// das ist eine wirklich nachtraegliche Entnahme -> Sollmenge=0
				// iSort ermitteln (wird unten einsortiert)
				Query query = em.createNamedQuery("LossollmaterialMaxISortFindByLosIId");
				query.setParameter(1, lossollmaterialDto.getLosIId());
				Integer iMaxSort = (Integer) query.getSingleResult();
				iMaxSort = iMaxSort == null ? 0 : iMaxSort;
				lossollmaterialDto.setISort(new Integer(iMaxSort + 1));
				lossollmaterialDto = createLossollmaterial(lossollmaterialDto, theClientDto);
				losistmaterialDto.setLossollmaterialIId(lossollmaterialDto.getIId());

			}
			losistmaterialDto.setLossollmaterialIId(lossollmaterialDto.getIId());

			if (listSnrChnr != null) {
				for (int i = 0; i < listSnrChnr.size(); i++) {
					losistmaterialDto.setNMenge(listSnrChnr.get(i).getNMenge());
					createLosistmaterial(losistmaterialDto, listSnrChnr.get(i).getCSeriennrChargennr(),
							bKommissionierterminal, theClientDto);
				}
			} else {
				createLosistmaterial(losistmaterialDto, null, bKommissionierterminal, theClientDto);
			}

			if (Helper.short2boolean(losistmaterialDto.getBAbgang()) == true && bReduziereFehlmenge) {
				// aufrollung der fehlmenge sonst nicht notwendig
				getFehlmengeFac().aktualisiereFehlmenge(LocaleFac.BELEGART_LOS, lossollmaterialDto.getIId(),
						theClientDto);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return lossollmaterialDto.getIId();
	}

	public void updateLosistmaterialMenge(Integer losistmaterialIId, BigDecimal bdMengeNeu, TheClientDto theClientDto) {
		updateLosistmaterialMenge(losistmaterialIId, bdMengeNeu, new Timestamp(System.currentTimeMillis()),
				theClientDto);
	}

	public void updateLosistmaterialMenge(Integer losistmaterialIId, BigDecimal bdMengeNeu, Timestamp tBelegdatum,
			TheClientDto theClientDto) {
		updateLosistmaterialMenge(losistmaterialIId, bdMengeNeu, tBelegdatum, false, theClientDto);
	}

	public void updateLosistmaterialMenge(Integer losistmaterialIId, BigDecimal bdMengeNeu, Timestamp tBelegdatum,
			boolean bKommissionierterminal, TheClientDto theClientDto) {

		Losistmaterial losistmaterial = em.find(Losistmaterial.class, losistmaterialIId);
		if (losistmaterial == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
		}

		LossollmaterialDto lossollmaterialDto = lossollmaterialFindByPrimaryKey(losistmaterial.getLossollmaterialIId());

		// Lagerbuchung veraendern

		// je nachdem, ob ab oder Zugang

		try {
			List<SeriennrChargennrMitMengeDto> dtos = getLagerFac()
					.getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(LocaleFac.BELEGART_LOS,
							losistmaterialIId);

			if (dtos == null || dtos.size() < 1) {
				dtos = new ArrayList<SeriennrChargennrMitMengeDto>();
				SeriennrChargennrMitMengeDto s = new SeriennrChargennrMitMengeDto();
				dtos.add(s);
			}

			if (Helper.short2boolean(losistmaterial.getBAbgang())) {

				BigDecimal preis = getLagerFac().getGestehungspreisEinerAbgangspositionMitTransaktion(
						LocaleFac.BELEGART_LOS, losistmaterialIId, dtos.get(0).getCSeriennrChargennr());
				getLagerFac().bucheAb(LocaleFac.BELEGART_LOS, lossollmaterialDto.getLosIId(), losistmaterialIId,
						lossollmaterialDto.getArtikelIId(), bdMengeNeu, preis, losistmaterial.getLagerIId(),
						dtos.get(0).getCSeriennrChargennr(), tBelegdatum, theClientDto);
				// Losistmaterial updaten
				losistmaterial.setNMenge(bdMengeNeu);

				em.merge(losistmaterial);
				em.flush();

				// Fehlmengen updaten
				getFehlmengeFac().aktualisiereFehlmenge(LocaleFac.BELEGART_LOS, losistmaterial.getLossollmaterialIId(),
						theClientDto);

			} else {

				BigDecimal bdPreis = getLagerFac().getGemittelterGestehungspreisEinesLagers(
						lossollmaterialDto.getArtikelIId(), losistmaterial.getLagerIId(), theClientDto);

				getLagerFac().bucheZu(LocaleFac.BELEGART_LOS, lossollmaterialDto.getLosIId(), losistmaterialIId,
						lossollmaterialDto.getArtikelIId(), bdMengeNeu, bdPreis, losistmaterial.getLagerIId(),
						dtos.get(0).getCSeriennrChargennr(), tBelegdatum, theClientDto, null, null, true);

				// Losistmaterial updaten
				losistmaterial.setNMenge(bdMengeNeu);

				em.merge(losistmaterial);
				em.flush();
				// Fehlmengen updaten
				getFehlmengeFac().aktualisiereFehlmenge(LocaleFac.BELEGART_LOS, losistmaterial.getLossollmaterialIId(),
						theClientDto);
			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
	}

	public boolean sindZeitenOhneArbeitsplanbezugVorhanden(Integer losIId, TheClientDto theClientDto) {

		String sQueryZeiten = "SELECT z FROM FLRZeitdatenLos z WHERE z.c_belegartnr='" + LocaleFac.BELEGART_LOS
				+ "' AND z.i_belegartid=" + losIId + " AND z.i_belegartpositionid IS NULL";

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Query zeiten = session.createQuery(sQueryZeiten);

		zeiten.setMaxResults(1);

		List<?> resultList = zeiten.list();

		if (resultList.size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	public void pruefePositionenMitSollsatzgroesseUnterschreitung(Integer losIId, BigDecimal bdZuErledigendeMenge,
			TheClientDto theClientDto) throws EJBExceptionLP {
		pruefePositionenMitSollsatzgroesseUnterschreitung(losIId, bdZuErledigendeMenge, false, theClientDto);
	}

	public void pruefePositionenMitSollsatzgroesseUnterschreitung(Integer losIId, BigDecimal bdZuErledigendeMenge,
			boolean bTerminal, TheClientDto theClientDto) throws EJBExceptionLP {

		// Basis fuer die Sollsatzgroesse sind die bisher erledigte Menge und
		// die zu erledigende
		BigDecimal bdKuenftigErledigt = getErledigteMenge(losIId, theClientDto).add(bdZuErledigendeMenge);
		LosDto losDto = losFindByPrimaryKey(losIId);
		// es sollte von jeder Position ein prozentueller Anteil ausgegeben sein
		// der prozentsatz wird durch die kuenftig erl. Menge im Verhaeltnis zur
		// Losgroesse bestimmt
		BigDecimal bdFaktor = bdKuenftigErledigt.divide(losDto.getNLosgroesse(), 10, BigDecimal.ROUND_HALF_EVEN);
		// alle Positionen holen
		LossollmaterialDto[] sollmat = lossollmaterialFindByLosIId(losIId);
		LinkedList<LossollmaterialDto> listUnterschritten = new LinkedList<LossollmaterialDto>();
		// fuer jede einzelne soll- und istmenge vergleichen
		for (int i = 0; i < sollmat.length; i++) {
			// bisher ausgegebene Menge

			// Ausgegebene Menge

			BigDecimal bdAusgegeben = getAusgegebeneMenge(sollmat[i].getIId(), null, theClientDto);
			// Die soll-ausgabe menge berechnen
			BigDecimal bdSollAusgabeMenge = Helper.rundeKaufmaennisch(sollmat[i].getNMenge().multiply(bdFaktor), 4);

			if (Helper.short2boolean(sollmat[i].getBRuestmenge())
					&& bdSollAusgabeMenge.doubleValue() > sollmat[i].getNMenge().doubleValue()) {
				bdSollAusgabeMenge = sollmat[i].getNMenge();
			}

			if (bdSollAusgabeMenge.compareTo(bdAusgegeben) > 0) {
				// Wenn die soll-ausgabe-menge noch nicht erreicht ist, dann zur
				// Liste hinzufuegen

				// PJ21251
				if (bTerminal) {
					ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(sollmat[i].getArtikelIId(),
							theClientDto);
					if (artikelDto
							.getIExternerArbeitsgang() != ArtikelFac.EXTERNER_ARBEITSGANG_OHNE_SSG_PRUEFUNG_AM_TERMNIAL) {
						listUnterschritten.add(sollmat[i]);
					}

				} else {
					listUnterschritten.add(sollmat[i]);
				}

			}
		}
		// und jetzt noch ein Array aus der Liste bauen
		LossollmaterialDto[] sollmatUnterschritten = new LossollmaterialDto[listUnterschritten.size()];
		int i = 0;
		for (Iterator<?> iter = listUnterschritten.iterator(); iter.hasNext(); i++) {
			sollmatUnterschritten[i] = (LossollmaterialDto) iter.next();
		}

		if (sollmatUnterschritten.length > 0) {

			StringBuffer sText = new StringBuffer(getTextRespectUISpr("fert.sollsatzgroesseunterschritten",
					theClientDto.getMandant(), theClientDto.getLocUi()));

			String stkl = getTextRespectUISpr("lp.stueckliste", theClientDto.getMandant(), theClientDto.getLocUi());

			for (int j = 0; j < sollmatUnterschritten.length; j++) {
				sText.append("\n");

				if (losDto.getStuecklisteIId() != null) {
					sText.append(stkl);
					StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(),
							theClientDto);
					sText.append(" " + stklDto.getArtikelDto().getCNr() + ": ");
				}

				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKey(sollmatUnterschritten[j].getArtikelIId(), theClientDto);
				sText.append(artikelDto.formatArtikelbezeichnung());

			}
			ArrayList ai = new ArrayList();
			ai.add(sText);
			ai.add(losDto);
			ai.add(bdZuErledigendeMenge);

			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_SOLLSATZGROESSE_UNTERSCHRITTEN, ai,
					new Exception("Sollsatzgr\u00F6\u00DFe von Los '" + losDto.getCNr() + "' unterschritten."));

		}
	}

	public Integer getNextArbeitsgang(Integer losIId, TheClientDto theClientDto) throws EJBExceptionLP {

		if (losIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("losIId == null"));
		}
		try {
			Integer i = null;
			try {
				Query query = em.createNamedQuery("LossollarbeitsplanejbSelectNextReihung");
				query.setParameter(1, losIId);
				i = (Integer) query.getSingleResult();
				ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.STUECKLISTE_ERHOEHUNG_ARBEITSGANG);
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
	 * bereits getaetigten Ablieferungen wird das "dirty"-flag gesetzt, welches bei
	 * Bedarf eine Neuberechnung des Gestehungspreises der Alieferung ausloest.
	 * 
	 * @param losistmaterialIId Integer
	 * @param bdGestehungspreis BigDecimal
	 * @param theClientDto      String
	 * @throws EJBExceptionLP
	 */
	public void updateLosistmaterialGestehungspreis(Integer losistmaterialIId, BigDecimal bdGestehungspreis,
			TheClientDto theClientDto) throws EJBExceptionLP {
		/**
		 * @todo check wieder einbauen PJ 4229
		 */
		// super.check(idUser); // wegen aufruf mit null aus Lager derzeit
		// auskommentiert
		myLogger.logData("iid=" + losistmaterialIId + ", gestpreis=" + bdGestehungspreis);
		// try {
		Losistmaterial losistmaterial = em.find(Losistmaterial.class, losistmaterialIId);
		if (losistmaterial == null) {
			// Wenn die Position geloescht wurde, dann brauchen wir auch nichts
			// updaten
			return;
		}
		Lossollmaterial lossollmaterial = em.find(Lossollmaterial.class, losistmaterial.getLossollmaterialIId());
		// Dirty-flag fuer alle Ablieferungen setzen

		Query query = em.createQuery("UPDATE Losablieferung l SET l.bGestehungspreisneuberechnen = 1 WHERE l.losIId = "
				+ lossollmaterial.getLosIId());
		query.executeUpdate();

		/*
		 * Query query = em.createNamedQuery("LosablieferungfindByLosIId");
		 * query.setParameter(1, lossollmaterial.getLosIId()); Collection<?>
		 * losablieferungs = query.getResultList(); Iterator<?> iterator =
		 * losablieferungs.iterator(); while (iterator.hasNext()) { Losablieferung
		 * losablieferung = (Losablieferung) iterator.next();
		 * losablieferung.setBGestehungspreisneuberechnen(Helper .boolean2Short(true));
		 * em.merge(losablieferung); em.flush(); }
		 */

	}

	@org.jboss.ejb3.annotation.TransactionTimeout(1000)

	public LosablieferungDto losablieferungFindByPrimaryKey(Integer iId,
			boolean bNeuberechnungDesGestehungspreisesFallsNotwendig, TheClientDto theClientDto) throws EJBExceptionLP {
		LosablieferungDto losablieferungDto = null;
		// try {
		Losablieferung losablieferung = em.find(Losablieferung.class, iId);
		if (losablieferung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		losablieferungDto = assembleLosablieferungDto(losablieferung);

		if (bNeuberechnungDesGestehungspreisesFallsNotwendig
				&& Helper.short2Boolean(losablieferungDto.getBGestehungspreisNeuBerechnen())) {
			aktualisiereNachtraeglichPreiseAllerLosablieferungen(losablieferungDto.getLosIId(), theClientDto, false);
			// Neu laden, da sich Daten geaendert haben koennen
			Losablieferung temp = em.find(Losablieferung.class, iId);
			if (temp == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
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
			boolean bNeuberechnungDesGestehungspreisesFallsNotwendig, TheClientDto theClientDto) throws EJBExceptionLP {
		LosablieferungDto losablieferungDto = null;
		// try {
		Losablieferung losablieferung = em.find(Losablieferung.class, iId);
		if (losablieferung == null) {
			myLogger.warn("iId=" + iId);
			return null;
		}
		losablieferungDto = assembleLosablieferungDto(losablieferung);

		if (bNeuberechnungDesGestehungspreisesFallsNotwendig
				&& Helper.short2Boolean(losablieferungDto.getBGestehungspreisNeuBerechnen())) {
			aktualisiereNachtraeglichPreiseAllerLosablieferungen(losablieferungDto.getLosIId(), theClientDto, false);
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

	public LosDto[] losFindByFertigungsortPartnerIIdMandantCNr(Integer partnerIId, String cNrMandant)
			throws EJBExceptionLP, RemoteException {
		LosDto[] losDtos = null;
		// try {
		Query query = em.createNamedQuery("LosfindByFertigungsortpartnerIIdMandantCNr");
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

	public void arbeitsgaengeNeuNummerieren(Integer losIId, TheClientDto theClientDto) {

		Query query = em.createNamedQuery("LossollarbeitsplanfindByLosIId");
		query.setParameter(1, losIId);
		LossollarbeitsplanDto[] dtos = assembleLossollarbeitsplanDtos(query.getResultList());

		TreeMap<Integer, ArrayList<LossollarbeitsplanDto>> tm = new TreeMap<Integer, ArrayList<LossollarbeitsplanDto>>();
		for (int i = 0; i < dtos.length; i++) {
			Integer iArbeitsgang = dtos[i].getIArbeitsgangnummer();
			ArrayList<LossollarbeitsplanDto> list = null;

			if (tm.containsKey(iArbeitsgang)) {
				list = tm.get(iArbeitsgang);
			} else {
				list = new ArrayList<LossollarbeitsplanDto>();
			}

			list.add(dtos[i]);

			tm.put(iArbeitsgang, list);

		}

		Iterator it = tm.keySet().iterator();
		try {

			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.STUECKLISTE_ERHOEHUNG_ARBEITSGANG);
			Integer iErhoehung = (Integer) parameter.getCWertAsObject();

			// Start ist immer bei 10
			Integer iAgAktuell = 10;

			while (it.hasNext()) {
				ArrayList<LossollarbeitsplanDto> list = tm.get(it.next());
				for (int i = 0; i < list.size(); i++) {
					LossollarbeitsplanDto apDto = list.get(i);

					Lossollarbeitsplan ap = em.find(Lossollarbeitsplan.class, apDto.getIId());

					ap.setIArbeitsgangnummer(iAgAktuell);
				}

				iAgAktuell = iAgAktuell + iErhoehung;

			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

	}

	public LosDto[] losFindByFertigungsortPartnerIIdMandantCNrOhneExc(Integer partnerIId, String cNrMandant)
			throws EJBExceptionLP, RemoteException {
		LosDto[] losDtos = null;
		// try {
		Query query = em.createNamedQuery("LosfindByFertigungsortpartnerIIdMandantCNr");
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

	public BigDecimal getMengeDerJuengstenLosablieferung(Integer losIId, TheClientDto theClientDto) {
		LosablieferungDto[] dtos = losablieferungFindByLosIId(losIId, false, theClientDto);

		BigDecimal menge = new BigDecimal(0);

		if (dtos != null && dtos.length > 0) {
			menge = dtos[dtos.length - 1].getNMenge();
		}

		return menge;
	}

	private LosablieferungDto[] losablieferungFindByLosIIdOhneNeuberechnungUndOhneSnrChnr(Integer losIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		LosablieferungDto[] losablieferungDtos = null;
		// try {
		Query query = em.createNamedQuery("LosablieferungfindByLosIId");
		query.setParameter(1, losIId);
		Collection<?> cl = query.getResultList();
		losablieferungDtos = assembleLosablieferungDtosOhneSnrs(cl);
		return losablieferungDtos;
	}

	public BigDecimal getMengeDerLetztenLosablieferungEinerAuftragsposition(Integer auftragIId, Integer artikelIId) {

		String sQuery = "select la.n_menge from FLRLosablieferung la WHERE la.flrlos.flrauftrag.i_id=" + auftragIId
				+ " AND la.flrlos.flrstueckliste.flrartikel.i_id=" + artikelIId + " ORDER BY la.t_aendern DESC";

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
			boolean bNeuberechnungDesGestehungspreisesFallsNotwendig, TheClientDto theClientDto) throws EJBExceptionLP {
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
					&& Helper.short2Boolean(losablieferungDtos[i].getBGestehungspreisNeuBerechnen())) {
				bAktualisieren = true;
			}
		}
		if (bAktualisieren && losablieferungDtos.length > 0) {
			aktualisiereNachtraeglichPreiseAllerLosablieferungen(losablieferungDtos[0].getLosIId(), theClientDto,
					false);
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
	public KapazitaetsvorschauDto getKapazitaetsvorschau(TheClientDto theClientDto) throws EJBExceptionLP {
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
			final ParametermandantDto parameterAnzahlGruppen = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_FERTIGUNG_KAPAZITAETSVORSCHAU_ANZAHL_GRUPPEN);
			final int iParamAnzahlGruppen = (Integer) parameterAnzahlGruppen.getCWertAsObject();
			// Angezeigter Zeitraum = Anzahl der Spalten
			final ParametermandantDto parameterZeitraum = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_FERTIGUNG_KAPAZITAETSVORSCHAU_ZEITRAUM);
			final int iParamZeitraum = (Integer) parameterZeitraum.getCWertAsObject();

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
			Criteria cArtikelVatergruppen = session.createCriteria(FLRArtikelgruppe.class);
			cArtikelVatergruppen.add(Restrictions.isNull(ArtikelFac.FLR_ARTIKELGRUPPE_FLRARTIKELGRUPPE));
			List<?> listArtikelgruppen = cArtikelVatergruppen.list();
			// Alle Maschinen-Vatergruppen
			Criteria cMaschinengruppen = session.createCriteria(FLRMaschinengruppe.class);
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
			final int iAnzuzeigendeArtikelgruppen = Math.min(iParamAnzahlGruppen, listArtikelgruppen.size());

			final int iAnzuzeigendeMaschinengruppen = Math.min(iParamAnzahlGruppen, listMaschinengruppen.size());

			final int iAnzahlZeilen = iAnzuzeigendeArtikelgruppen + 1 + iAnzuzeigendeMaschinengruppen + 1;

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
				gc.setTimeInMillis(gc.getTimeInMillis() + 7 * 24 * 60 * 60 * 1000); // 1 Woche dazu
			}
			// ------------------------------------------------------------------
			// -----
			// Beschriftung der y-Achse ermitteln. das sind die Namen der
			// Vatergruppen bzw. 2 x "Sonstige".
			// Weiters werden die Indizes im Daten-Array fuer die jeweiligen
			// Gruppen festgelegt.
			// ------------------------------------------------------------------
			// -----
			String sSonstige = getTextRespectUISpr("lp.sonstige", theClientDto.getMandant(), theClientDto.getLocUi());

			HashMap<Integer, Integer> hmArtikelGruppenIndizes = new HashMap<Integer, Integer>();
			HashMap<Integer, Integer> hmMaschinenGruppenIndizes = new HashMap<Integer, Integer>();

			// zuerst die Artikelvatergruppen
			for (int i = 0; i < iAnzuzeigendeArtikelgruppen; i++) {
				FLRArtikelgruppe item = (FLRArtikelgruppe) listArtikelgruppen.get(i);
				kapDto.setIZeilenueberschrift(i, item.getC_nr());
				hmArtikelGruppenIndizes.put(item.getI_id(), i);
			}
			// Dann Sonstige Artikelgruppen
			final int indexSonstigeArtikelGruppen = iAnzuzeigendeArtikelgruppen;
			kapDto.setIZeilenueberschrift(indexSonstigeArtikelGruppen, sSonstige);
			// Maschinengruppen
			for (int i = 0; i < iAnzuzeigendeMaschinengruppen; i++) {
				FLRMaschinengruppe item = (FLRMaschinengruppe) listMaschinengruppen.get(i);
				int index = iAnzuzeigendeArtikelgruppen + 1 + i;
				kapDto.setIZeilenueberschrift(index, item.getC_bez());
				hmMaschinenGruppenIndizes.put(item.getI_id(), index);
			}
			// zuletzt Sonstige Maschinengruppen
			final int indexSonstigeMaschinenGruppen = iAnzuzeigendeArtikelgruppen + 1 + iAnzuzeigendeMaschinengruppen;
			kapDto.setIZeilenueberschrift(indexSonstigeMaschinenGruppen, sSonstige);

			// ------------------------------------------------------------------
			// -----
			// Lose holen
			// ------------------------------------------------------------------
			// -----
			Criteria cLose = session.createCriteria(FLRLos.class);
			// Filter nach Mandant
			cLose.add(Restrictions.eq(FertigungFac.FLR_LOS_MANDANT_C_NR, theClientDto.getMandant()));
			// Alle Stati ausser Erledigt, Gestoppt, Storniert
			Collection<String> cLoseStati = new LinkedList<String>();
			cLoseStati.add(FertigungFac.STATUS_ERLEDIGT);
			cLoseStati.add(FertigungFac.STATUS_GESTOPPT);
			cLoseStati.add(FertigungFac.STATUS_STORNIERT);
			cLose.add(Restrictions.not(Restrictions.in(FertigungFac.FLR_LOS_STATUS_C_NR, cLoseStati)));
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
			java.util.Date dMax = Helper.cutDate(new java.sql.Date(c.getTimeInMillis()));

			java.sql.Timestamp tVon = Helper.cutTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
			java.sql.Timestamp tBis = Helper.cutTimestamp(new java.sql.Timestamp(dMax.getTime()));
			// Verfuegbare Zeiten holen
			SollverfuegbarkeitDto[] oVerfuegbar = getZeiterfassungFac().getVerfuegbareSollzeit(tVon, tBis,
					theClientDto);
			// diese nun aufteilen
			for (int i = 0; i < oVerfuegbar.length; i++) {
				SollverfuegbarkeitDto svDto = oVerfuegbar[i];
				// "normale" AZ
				if (svDto.isBMannarbeitszeit()) {
					// die sind einer Artikelgruppe zugeordnet
					if (svDto.getIGruppeid() != null) {
						Integer iZeile = hmArtikelGruppenIndizes.get(svDto.getIGruppeid());
						// ist das eine sichtbare Gruppe
						if (iZeile != null) {
							for (int iSpalte = 0; iSpalte < kapDto.getDetails()[iZeile].length; iSpalte++) {
								// mal 5 Tage
								kapDto.addBdVerfuegbareStunden(iZeile, iSpalte,
										svDto.getNSollstunden().multiply(new BigDecimal(5)));
							}
						}
						// wenn nicht, dann zu den sonstigen
						else {
							for (int iSpalte = 0; iSpalte < kapDto
									.getDetails()[indexSonstigeArtikelGruppen].length; iSpalte++) {
								// mal 5 Tage
								kapDto.addBdVerfuegbareStunden(indexSonstigeArtikelGruppen, iSpalte,
										svDto.getNSollstunden().multiply(new BigDecimal(5)));
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
								kapDto.addBdVerfuegbareStunden(indexSonstigeArtikelGruppen, iIndexDerKW,
										svDto.getNSollstunden());
							} else {
								// diese KW wird nicht mehr angezeigt - brauch
								// ich nicht einrechnen
							}
						}
					}
				}
				// Maschinenzeit - die Verfuegbarkeit ist jeden Tag gleich
				else {
					Integer iZeile = hmMaschinenGruppenIndizes.get(svDto.getIGruppeid());
					// ist das eine sichtbare Gruppe
					if (iZeile != null) {
						for (int iSpalte = 0; iSpalte < kapDto.getDetails()[iZeile].length; iSpalte++) {
							// mal 5 Tage
							kapDto.addBdVerfuegbareStunden(iZeile, iSpalte,
									svDto.getNSollstunden().multiply(new BigDecimal(5)));
						}
					}
					// wenn nicht, dann zu den sonstigen
					else {
						for (int iSpalte = 0; iSpalte < kapDto
								.getDetails()[indexSonstigeMaschinenGruppen].length; iSpalte++) {
							// mal 5 Tage
							kapDto.addBdVerfuegbareStunden(indexSonstigeMaschinenGruppen, iSpalte,
									svDto.getNSollstunden().multiply(new BigDecimal(5)));
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
				for (Iterator<?> iterAblieferung = los.getAblieferungset().iterator(); iterAblieferung.hasNext();) {
					FLRLosablieferung item = (FLRLosablieferung) iterAblieferung.next();
					bdOffen = bdOffen.subtract(item.getN_menge());
				}
				// nur Lose mit tatsaechlich offener Menge>0
				if (bdOffen.compareTo(new BigDecimal(0)) > 0) {
					// Faktor zur Berechnung der offenen Zeiten = offene Menge /
					// Losgroesse. 2 Nachkommastellen sollten reichen.
					BigDecimal bdFaktor = bdOffen.divide(los.getN_losgroesse(), 2, BigDecimal.ROUND_HALF_EVEN);
					// Arbeitsplan holen
					Criteria cLosAZ = session.createCriteria(FLRLossollarbeitsplan.class);
					cLosAZ.add(Restrictions.eq(FertigungFac.FLR_LOSSOLLARBEITSPLAN_LOS_I_ID, los.getI_id()));
					List<?> listLosAZ = cLosAZ.list();
					// fuer alle Taetigkeiten
					for (Iterator<?> iterAZ = listLosAZ.iterator(); iterAZ.hasNext();) {
						FLRLossollarbeitsplan losAZ = (FLRLossollarbeitsplan) iterAZ.next();
						BigDecimal bdOffeneStunden = losAZ.getN_gesamtzeit().multiply(bdFaktor);
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
							FLRMaschinengruppe flrMaschinengruppe = flrMaschine.getFlrmaschinengruppe();
							if (flrMaschinengruppe != null) {
								// Wenn diese Maschinengruppe dargestellt wird,
								// dann kommt hier der index raus.
								Integer i = hmMaschinenGruppenIndizes.get(flrMaschinengruppe.getI_id());
								iMaschinengruppeIId = flrMaschinengruppe.getI_id();
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
							FLRArtikelgruppe flrArtikelgruppe = flrArtikel.getFlrartikelgruppe();
							if (flrArtikelgruppe != null) {
								// Wenn diese Artikelgruppe dargestellt wird,
								// dann kommt hier der index raus.
								Integer i = hmArtikelGruppenIndizes.get(flrArtikelgruppe.getI_id());
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
						java.util.Date tLosStarttermin = los.getT_produktionsbeginn();
						java.util.Date tLosEndetermin = los.getT_produktionsende();
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
							bdOffeneStundenJeWoche = bdOffeneStunden.divide(new BigDecimal(iAnzahlKW), 2,
									RoundingMode.HALF_UP);
						}

						for (int iAktuelleKW = iStartKW; iAktuelleKW <= iEndeKW; iAktuelleKW++) {
							Integer indexDerKW = hmKWIndizes.get(iAktuelleKW);
							// wird diese Woche auch angezeigt?
							if (indexDerKW != null) {
								KapazitaetsvorschauDetailDto detailDto = new KapazitaetsvorschauDetailDto();
								detailDto.setArtikelgruppeIId(iArtikelgruppeIId);
								detailDto.setArtikelIIdTaetigkeit(losAZ.getFlrartikel().getI_id());
								detailDto.setBdDauer(bdOffeneStundenJeWoche);
								detailDto.setLosIId(los.getI_id());
								detailDto.setLossollarbeitsplanIId(losAZ.getI_id());
								detailDto.setMaschinengruppeIId(iMaschinengruppeIId);
								kapDto.addDetail(iZeilenIndex, indexDerKW, detailDto);
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
				XYSeries datenZeile = new XYSeries(kapDto.getIZeilenueberschrift(iZeile));
				// Balkenfarben festlegen ( >100% = rot, sonst hellgrau)
				// fuer jede zeile und jede spalte
				Paint[][] paints = new Paint[1][kapDto.getDetails()[iZeile].length];
				for (int iSpalte = 0; iSpalte < kapDto.getDetails()[iZeile].length; iSpalte++) {
					BigDecimal bdVerfuegbar = kapDto.getBdVerfuegbareStunden()[iZeile][iSpalte];
					BigDecimal bdBenoetigt = new BigDecimal(0);
					// Benoetigte Zeit jet Gruppe je Woche ermitteln
					for (Iterator<?> iter = kapDto.getDetails()[iZeile][iSpalte].iterator(); iter.hasNext();) {
						KapazitaetsvorschauDetailDto item = (KapazitaetsvorschauDetailDto) iter.next();
						bdBenoetigt = bdBenoetigt.add(item.getBdDauer());
					}
					BigDecimal value = new BigDecimal(0);
					if (bdVerfuegbar.compareTo(new BigDecimal(0)) > 0) {
						value = (bdBenoetigt.multiply(new BigDecimal(100))).divide(bdVerfuegbar, 4,
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
					XYDataItem data = new XYDataItem(iSpalte, value.doubleValue());
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
				NumberAxis zeilenAchse = new NumberAxis(kapDto.getIZeilenueberschrift(iZeile));
				// Beschriftung der Y-Achse um 90 grad drehen
				zeilenAchse.setLabelAngle(Math.PI / 2.0);
				zeilenAchse.setAutoRange(true);
				XYPlot subplot1 = new XYPlot(xyDataset, null, zeilenAchse, renderer1);
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

	public void setzeLosablieferungenAufNeuBerechnen(Integer losIId, TheClientDto theClientDto) throws EJBExceptionLP {
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
	 * Die Zuordnung des Loses au Auftrag, Kostenstelle nachtraeglich veraendern.
	 * 
	 * @param losDto       LosDto
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 */
	public void updateLosZuordnung(LosDto losDto, TheClientDto theClientDto) throws EJBExceptionLP {

		if (losDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("losDto == null"));
		}
		Los los = em.find(Los.class, losDto.getIId());
		if (los == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
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
	 * @param theClientDto String
	 * @return ArrayList
	 * @throws EJBExceptionLP
	 */
	public ArrayList<Integer> getAllLosIIdDesMandanten(TheClientDto theClientDto) throws EJBExceptionLP {
		ArrayList<Integer> list = new ArrayList<Integer>();
		Session session = null;
		try {

			session = FLRSessionFactory.getFactory().openSession();
			Criteria crit = session.createCriteria(FLRLos.class);
			crit.add(Restrictions.eq(FertigungFac.FLR_LOS_MANDANT_C_NR, theClientDto.getMandant()));
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

	public void vonQuelllagerUmbuchenUndAnsLosAusgeben(Integer lagerIId_Quelle, Integer lossollmaterialIId,
			BigDecimal nMenge, List<SeriennrChargennrMitMengeDto> l, TheClientDto theClientDto) {
		// Zuerst umbuchen
		LossollmaterialDto lossollmaterialDto = lossollmaterialFindByPrimaryKey(lossollmaterialIId);

		LoslagerentnahmeDto[] loslagerentnahmeDtos = loslagerentnahmeFindByLosIId(lossollmaterialDto.getLosIId());

		try {

			// Zuerst umbuchen, wenn Laeger unterschiedlich

			if (!lagerIId_Quelle.equals(loslagerentnahmeDtos[0].getLagerIId())) {

				getLagerFac().bucheUm(lossollmaterialDto.getArtikelIId(), lagerIId_Quelle,
						lossollmaterialDto.getArtikelIId(), loslagerentnahmeDtos[0].getLagerIId(), nMenge, l,
						"Umbuchung Wareneingangslager", getLagerFac().getGemittelterGestehungspreisEinesLagers(
								lossollmaterialDto.getArtikelIId(), lagerIId_Quelle, theClientDto),
						theClientDto);
			}

			// dann ausgeben

			LosistmaterialDto losistmaterialDto = new LosistmaterialDto();
			losistmaterialDto.setLagerIId(loslagerentnahmeDtos[0].getLagerIId());
			losistmaterialDto.setBAbgang(Helper.boolean2Short(true));

			losistmaterialDto.setNMenge(nMenge);

			gebeMaterialNachtraeglichAus(lossollmaterialDto, losistmaterialDto, l, true, theClientDto);

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

	}

	public void vertauscheWiederholendelose(Integer iIdMontageart1I, Integer iIdMontageart2I) {
		myLogger.entry();
		// try {
		Wiederholendelose wiederholendelose1 = em.find(Wiederholendelose.class, iIdMontageart1I);
		if (wiederholendelose1 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Wiederholendelose wiederholendelose2 = em.find(Wiederholendelose.class, iIdMontageart2I);

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

	public Integer createWiederholendelose(WiederholendeloseDto wiederholendeloseDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (wiederholendeloseDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("wiederholendeloseDto == null"));
		}
		if (wiederholendeloseDto.getAuftragwiederholungsintervallCNr() == null
				|| wiederholendeloseDto.getBVersteckt() == null || wiederholendeloseDto.getFertigungsgruppeIId() == null
				|| wiederholendeloseDto.getITagevoreilend() == null
				|| wiederholendeloseDto.getKostenstelleIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"wiederholendeloseDto.getAuftragwiederholungsintervallCNr() == null || wiederholendeloseDto.getBVersteckt() == null  || wiederholendeloseDto.getFertigungsgruppeIId() == null  || wiederholendeloseDto.getITagevoreilend() == null  || wiederholendeloseDto.getKostenstelleIId() == null "));
		}
		if (wiederholendeloseDto.getLagerIIdZiel() == null || wiederholendeloseDto.getMandantCNr() == null
				|| wiederholendeloseDto.getNLosgroesse() == null
				|| wiederholendeloseDto.getPartnerIIdFertigungsort() == null
				|| wiederholendeloseDto.getTTermin() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"wiederholendeloseDto.getLagerIIdZiel() == null || wiederholendeloseDto.getMandantCNr() == null  || wiederholendeloseDto.getNLosgroesse() == null  || wiederholendeloseDto.getPartnerIIdFertigungsort() == null  || wiederholendeloseDto.getTTermin() == null"));
		}
		wiederholendeloseDto.setTTermin(Helper.cutTimestamp(wiederholendeloseDto.getTTermin()));
		try {
			Integer iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_LOSSOLLMATERIAL);
			wiederholendeloseDto.setIId(iId);
			Integer i = null;
			try {
				Query query = em.createNamedQuery("WiederholendeloseejbSelectNextReihung");
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
			wiederholendeloseDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			wiederholendeloseDto.setTAendern(new Timestamp(System.currentTimeMillis()));

			wiederholendeloseDto.setIId(iId);
			Wiederholendelose wiederholendelose = new Wiederholendelose(wiederholendeloseDto.getIId(),
					wiederholendeloseDto.getMandantCNr(), wiederholendeloseDto.getKostenstelleIId(),
					wiederholendeloseDto.getNLosgroesse(), wiederholendeloseDto.getPartnerIIdFertigungsort(),
					wiederholendeloseDto.getPersonalIIdAendern(), wiederholendeloseDto.getTTermin(),
					wiederholendeloseDto.getLagerIIdZiel(), wiederholendeloseDto.getITagevoreilend(),
					wiederholendeloseDto.getFertigungsgruppeIId(),
					wiederholendeloseDto.getAuftragwiederholungsintervallCNr(), wiederholendeloseDto.getBVersteckt(),
					wiederholendeloseDto.getISort());
			em.persist(wiederholendelose);
			em.flush();
			setWiederholendeloseFromWiederholendeloseDto(wiederholendelose, wiederholendeloseDto);
			return wiederholendeloseDto.getIId();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeWiederholendelose(WiederholendeloseDto wiederholendeloseDto) throws EJBExceptionLP {
		if (wiederholendeloseDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("wiederholendeloseDto == null"));
		}
		if (wiederholendeloseDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("wiederholendeloseDto.getIId() == null"));
		}

		// try {
		Integer iId = wiederholendeloseDto.getIId();
		Wiederholendelose toRemove = em.find(Wiederholendelose.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
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

	public void updateWiederholendelose(WiederholendeloseDto wiederholendeloseDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (wiederholendeloseDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("wiederholendeloseDto == null"));
		}
		if (wiederholendeloseDto.getIId() == null || wiederholendeloseDto.getAuftragwiederholungsintervallCNr() == null
				|| wiederholendeloseDto.getBVersteckt() == null || wiederholendeloseDto.getFertigungsgruppeIId() == null
				|| wiederholendeloseDto.getITagevoreilend() == null
				|| wiederholendeloseDto.getKostenstelleIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"wiederholendeloseDto.getIId() == null || wiederholendeloseDto.getAuftragwiederholungsintervallCNr() == null || wiederholendeloseDto.getBVersteckt() == null  || wiederholendeloseDto.getFertigungsgruppeIId() == null  || wiederholendeloseDto.getITagevoreilend() == null  || wiederholendeloseDto.getKostenstelleIId() == null "));
		}
		if (wiederholendeloseDto.getLagerIIdZiel() == null || wiederholendeloseDto.getMandantCNr() == null
				|| wiederholendeloseDto.getNLosgroesse() == null
				|| wiederholendeloseDto.getPartnerIIdFertigungsort() == null
				|| wiederholendeloseDto.getTTermin() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"wiederholendeloseDto.getLagerIIdZiel() == null || wiederholendeloseDto.getMandantCNr() == null  || wiederholendeloseDto.getNLosgroesse() == null  || wiederholendeloseDto.getPartnerIIdFertigungsort() == null  || wiederholendeloseDto.getTTermin() == null"));
		}
		wiederholendeloseDto.setTTermin(Helper.cutTimestamp(wiederholendeloseDto.getTTermin()));

		Integer iId = wiederholendeloseDto.getIId();
		wiederholendeloseDto.setMandantCNr(theClientDto.getMandant());
		wiederholendeloseDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		wiederholendeloseDto.setTAendern(new Timestamp(System.currentTimeMillis()));
		Wiederholendelose wiederholendelose = em.find(Wiederholendelose.class, iId);
		if (wiederholendelose == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setWiederholendeloseFromWiederholendeloseDto(wiederholendelose, wiederholendeloseDto);
	}

	public WiederholendeloseDto wiederholendeloseFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("iId == null"));
		}

		// try {
		Wiederholendelose wiederholendelose = em.find(Wiederholendelose.class, iId);
		if (wiederholendelose == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleWiederholendeloseDto(wiederholendelose);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public WiederholendeloseDto[] wiederholendeloseFindByPartnerIIdMandantCNr(Integer iPartnerId, String iMandantCNr)
			throws EJBExceptionLP {
		if (iPartnerId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("iPartnerId == null"));
		}
		// try {
		Query query = em.createNamedQuery("WiederholendelosefindByFertigungsortpartnerIIdMandantCNr");
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

	public WiederholendeloseDto[] wiederholendeloseFindByPartnerIIdMandantCNrOhneExc(Integer iPartnerId,
			String iMandantCNr) {
		if (iPartnerId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("iPartnerId == null"));
		}
		// try {
		Query query = em.createNamedQuery("WiederholendelosefindByFertigungsortpartnerIIdMandantCNr");
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

	private void setWiederholendeloseFromWiederholendeloseDto(Wiederholendelose wiederholendelose,
			WiederholendeloseDto wiederholendeloseDto) {
		wiederholendelose.setMandantCNr(wiederholendeloseDto.getMandantCNr());
		wiederholendelose.setKostenstelleIId(wiederholendeloseDto.getKostenstelleIId());
		wiederholendelose.setCProjekt(wiederholendeloseDto.getCProjekt());
		wiederholendelose.setStuecklisteIId(wiederholendeloseDto.getStuecklisteIId());
		wiederholendelose.setNLosgroesse(wiederholendeloseDto.getNLosgroesse());
		wiederholendelose.setPartnerIIdFertigungsort(wiederholendeloseDto.getPartnerIIdFertigungsort());
		wiederholendelose.setPersonalIIdAendern(wiederholendeloseDto.getPersonalIIdAendern());
		wiederholendelose.setTAendern(wiederholendeloseDto.getTAendern());
		wiederholendelose.setTTermin(wiederholendeloseDto.getTTermin());
		wiederholendelose.setISort(wiederholendeloseDto.getISort());
		wiederholendelose.setLagerIIdZiel(wiederholendeloseDto.getLagerIIdZiel());
		wiederholendelose.setITagevoreilend(wiederholendeloseDto.getITagevoreilend());
		wiederholendelose.setFertigungsgruppeIId(wiederholendeloseDto.getFertigungsgruppeIId());
		wiederholendelose
				.setAuftragwiederholungsintervallCNr(wiederholendeloseDto.getAuftragwiederholungsintervallCNr());
		wiederholendelose.setBVersteckt(wiederholendeloseDto.getBVersteckt());
		em.merge(wiederholendelose);
		em.flush();
	}

	private WiederholendeloseDto assembleWiederholendeloseDto(Wiederholendelose wiederholendelose) {
		return WiederholendeloseDtoAssembler.createDto(wiederholendelose);
	}

	private WiederholendeloseDto[] assembleWiederholendeloseDtos(Collection<?> wiederholendeloses) {
		List<WiederholendeloseDto> list = new ArrayList<WiederholendeloseDto>();
		if (wiederholendeloses != null) {
			Iterator<?> iterator = wiederholendeloses.iterator();
			while (iterator.hasNext()) {
				Wiederholendelose wiederholendelose = (Wiederholendelose) iterator.next();
				list.add(assembleWiederholendeloseDto(wiederholendelose));
			}
		}
		WiederholendeloseDto[] returnArray = new WiederholendeloseDto[list.size()];
		return (WiederholendeloseDto[]) list.toArray(returnArray);
	}

	public Integer createZusatzstatus(ZusatzstatusDto zusatzstatusDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (zusatzstatusDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("artikelDto == null"));
		}
		if (zusatzstatusDto.getCBez() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("zusatzstatusDto.getCBez() == null"));
		}
		zusatzstatusDto.setMandantCNr(theClientDto.getMandant());

		try {
			Query query = em.createNamedQuery("ZusatzstatusfindByMandantCNrCBez");
			query.setParameter(1, zusatzstatusDto.getMandantCNr());
			query.setParameter(2, zusatzstatusDto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Zusatzstatus doppelt = (Zusatzstatus) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("FERT_ZUSATZSTATUS.UK"));
			}
		} catch (NoResultException ex) {
			// nix
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ZUSATZSTATUS);
			zusatzstatusDto.setIId(pk);

			Zusatzstatus zusatzstatus = new Zusatzstatus(zusatzstatusDto.getIId(), zusatzstatusDto.getMandantCNr(),
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

	public void removeZusatzstatus(ZusatzstatusDto zusatzstatusDto) throws EJBExceptionLP {
		if (zusatzstatusDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("zusatzstatusDto == null"));
		}
		if (zusatzstatusDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("zusatzstatusDto.getIId() == null"));
		}

		// try {
		Zusatzstatus toRemove = em.find(Zusatzstatus.class, zusatzstatusDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
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

	public void updateZusatzstatus(ZusatzstatusDto zusatzstatusDto, TheClientDto theClientDto) throws EJBExceptionLP {

		if (zusatzstatusDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("artikelDto == null"));
		}
		if (zusatzstatusDto.getIId() == null || zusatzstatusDto.getCBez() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("zusatzstatusDto.getIId() == null || zusatzstatusDto.getCBez() == null"));
		}
		zusatzstatusDto.setMandantCNr(theClientDto.getMandant());

		Integer iId = zusatzstatusDto.getIId();

		try {
			Query query = em.createNamedQuery("ZusatzstatusfindByMandantCNrCBez");
			query.setParameter(1, zusatzstatusDto.getMandantCNr());
			query.setParameter(2, zusatzstatusDto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Zusatzstatus) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("FERT_ZUSATZSTATUS.UK"));
			}
		} catch (NoResultException ex) {
			// nix
		}

		// try {
		Zusatzstatus zusatzstatus = em.find(Zusatzstatus.class, iId);
		if (zusatzstatus == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
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

	public void vertauscheLoslagerentnahme(Integer iiDLagerentnahme1, Integer iIdLagerentnahme2) {

		Loslagerentnahme oLieferant1 = em.find(Loslagerentnahme.class, iiDLagerentnahme1);
		if (oLieferant1 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Loslagerentnahme oLieferant2 = em.find(Loslagerentnahme.class, iIdLagerentnahme2);
		if (oLieferant2 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
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

	public void vertauscheLossollarbeitsplanReihung(Integer iIdLossollarbeitsplan1, Integer iIdLossollarbeitsplan2) {

		Lossollarbeitsplan o1 = em.find(Lossollarbeitsplan.class, iIdLossollarbeitsplan1);
		if (o1 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Lossollarbeitsplan o2 = em.find(Lossollarbeitsplan.class, iIdLossollarbeitsplan2);
		if (o2 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Integer iSort1 = o1.getIReihung();
		Integer iSort2 = o2.getIReihung();
		// iSort der zweiten Loslagerentnahme auf ungueltig setzen, damit UK
		// constraint nicht verletzt wird
		o2.setIReihung(new Integer(-1));
		em.merge(o2);
		em.flush();
		o1.setIReihung(iSort2);
		o2.setIReihung(iSort1);

	}

	public ZusatzstatusDto zusatzstatusFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}

		// try {
		Zusatzstatus zusatzstatus = em.find(Zusatzstatus.class, iId);
		if (zusatzstatus == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleZusatzstatusDto(zusatzstatus);
		// }
		// catch (FinderException fe) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// fe);

		// }
	}

	public ZusatzstatusDto zusatzstatusFindByMandantCNrCBez(String mandantCNr, String cBez) throws EJBExceptionLP {
		try {
			Query query = em.createNamedQuery("ZusatzstatusfindByMandantCNrCBez");
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

	private void setZusatzstatusFromZusatzstatusDto(Zusatzstatus zusatzstatus, ZusatzstatusDto zusatzstatusDto) {
		zusatzstatus.setMandantCNr(zusatzstatusDto.getMandantCNr());
		zusatzstatus.setCBez(zusatzstatusDto.getCBez());
		zusatzstatus.setISort(zusatzstatusDto.getISort());
		em.merge(zusatzstatus);
		em.flush();
	}

	private ZusatzstatusDto assembleZusatzstatusDto(Zusatzstatus zusatzstatus) {
		return ZusatzstatusDtoAssembler.createDto(zusatzstatus);
	}

	private ZusatzstatusDto[] assembleZusatzstatusDtos(Collection<?> zusatzstatuss) {
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

	private LosgutschlechtDto[] assembleLosgutschlechtDtos(Collection<?> zusatzstatuss) {
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

	public Integer getNextZusatzstatus(TheClientDto theClientDto) throws EJBExceptionLP {

		try {
			Query query = em.createNamedQuery("ZusatzstatusejbSelectNextReihung");
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

	public BigDecimal[] getGutSchlechtInarbeit(Integer lossollarbeitsplanIId, Integer personalIId,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis, TheClientDto theClientDto) {
		LosgutschlechtDto[] dtos = losgutschlechtFindByLossollarbeitsplanIId(lossollarbeitsplanIId);
		LossollarbeitsplanDto lossollarbeitsplanDto = lossollarbeitsplanFindByPrimaryKey(lossollarbeitsplanIId);
		LosDto losDto = losFindByPrimaryKey(lossollarbeitsplanDto.getLosIId());

		BigDecimal bdGut = new BigDecimal(0);
		BigDecimal bdSchlecht = new BigDecimal(0);
		BigDecimal bdInarbeit = new BigDecimal(0);
		BigDecimal bdOffen = losDto.getNLosgroesse();

		for (int i = 0; i < dtos.length; i++) {

			if (personalIId != null && dtos[i].getZeitdatenIId() != null) {
				Zeitdaten z = em.find(Zeitdaten.class, dtos[i].getZeitdatenIId());

				if (z.getPersonalIId().equals(personalIId)) {

					if ((tVon == null || tVon != null && tVon.before(z.getTZeit()))
							&& (tBis == null || tBis != null && tBis.after(z.getTZeit()))) {
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
			ParametermandantDto parameterIstZeit = (ParametermandantDto) getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_THEORETISCHE_IST_ZEIT_RECHNUNG);

			bTheoretischeIstZeit = ((Boolean) parameterIstZeit.getCWertAsObject());

		} catch (RemoteException ex5) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
		}
		// lt. WH:Wenn TheoretischeIstZeit und "nicht ruesten", dann die
		// Gut/Schlecht Stueck der Ruestzeit des AGs abziehen

		if (bTheoretischeIstZeit == true) {

			// Wenn nicht ruesten
			if (lossollarbeitsplanDto.getAgartCNr() != null) {

				Query query = em.createNamedQuery("LossollarbeitsplanfindByLosIIdIArbeitsgangnummer");
				query.setParameter(1, lossollarbeitsplanDto.getLosIId());
				query.setParameter(2, lossollarbeitsplanDto.getIArbeitsgangnummer());
				Collection<?> cl = query.getResultList();

				if (cl != null) {
					Iterator<?> iterator = cl.iterator();
					while (iterator.hasNext()) {
						Lossollarbeitsplan lossollarbeitsplanTemp = (Lossollarbeitsplan) iterator.next();
						if (lossollarbeitsplanTemp.getAgartCNr() == null) {
							LosgutschlechtDto[] dtosRuestZeit = losgutschlechtFindByLossollarbeitsplanIId(
									lossollarbeitsplanTemp.getIId());

							for (int i = 0; i < dtosRuestZeit.length; i++) {
								bdOffen = bdOffen.subtract(dtosRuestZeit[i].getNGut())
										.subtract(dtosRuestZeit[i].getNSchlecht());
							}

						}
					}

				}
			}
		}

		return new BigDecimal[] { bdGut, bdSchlecht, bdInarbeit, bdOffen };
	}

	public BigDecimal[] getGutSchlechtInarbeit(Integer lossollarbeitsplanIId, TheClientDto theClientDto) {
		return getGutSchlechtInarbeit(lossollarbeitsplanIId, null, null, null, theClientDto);

	}

	public Integer createLoszusatzstatus(LoszusatzstatusDto loszusatzstatusDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (loszusatzstatusDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("loszusatzstatusDto == null"));
		}
		if (loszusatzstatusDto.getLosIId() == null || loszusatzstatusDto.getZusatzstatusIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"loszusatzstatusDto.getLosIId() == null || loszusatzstatusDto.getZusatzstatusIId() == null"));
		}

		try {
			Query query = em.createNamedQuery("LoszusatzstatusfindByLosIIdZusatzstatusIId");
			query.setParameter(1, loszusatzstatusDto.getLosIId());
			query.setParameter(2, loszusatzstatusDto.getZusatzstatusIId());
			// @todo getSingleResult oder getResultList ?
			Loszusatzstatus doppelt = (Loszusatzstatus) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("FERT_LOSZUSATZSTATUS.UK"));
		} catch (NoResultException ex) {

		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_LOSZUSATZSTATUS);
			loszusatzstatusDto.setIId(pk);

			loszusatzstatusDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			loszusatzstatusDto.setTAendern(new Timestamp(System.currentTimeMillis()));

			Loszusatzstatus loszusatzstatus = new Loszusatzstatus(loszusatzstatusDto.getIId(),
					loszusatzstatusDto.getLosIId(), loszusatzstatusDto.getZusatzstatusIId(),
					loszusatzstatusDto.getPersonalIIdAendern());
			em.persist(loszusatzstatus);
			em.flush();
			setLoszusatzstatusFromLoszusatzstatusDto(loszusatzstatus, loszusatzstatusDto);
			return loszusatzstatusDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

	}

	public void isLosNochErfuellbar(Integer lossollarbeitsplanIId, TheClientDto theClientDto) {
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
							.auftragpositionFindByPrimaryKey(losDto.getAuftragpositionIId());

					AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(auftragpositionDto.getBelegIId());

					if (auftragpositionDto.getNMenge() != null) {
						// Losgroesse - Schlecht-Stueck muss groesser
						// Auftragsmenge sein

						if (losDto.getNLosgroesse().subtract(mengeSchlecht).doubleValue() < auftragpositionDto
								.getNMenge().doubleValue()) {
							// Archiveintrag erstellen

							String nachricht = BenutzerFac.NA_AUFTRAG_NICHT_MEHR_ERFUELLBAR + " Auftrag "
									+ auftragDto.getCNr() + " nicht mehr erf\u00FCllbar, Losnummer: " + losDto.getCNr()
									+ ", Losgr\u00F6\u00DFe: " + losDto.getNLosgroesse() + ", Schlechtst\u00FCck: "
									+ mengeSchlecht + ", Menge Auftragsposition: " + auftragpositionDto.getNMenge();

							getBenutzerFac().sendJmsMessageMitArchiveintrag(
									BenutzerFac.NA_AUFTRAG_NICHT_MEHR_ERFUELLBAR, nachricht, theClientDto);
						}

					}

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}

		}

	}

	public LosgutschlechtRueckmeldungDto createLosgutschlecht(LosgutschlechtDto losgutschlechtDto,
			TheClientDto theClientDto) {

		if (losgutschlechtDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("losgutschlechtDto == null"));
		}
		if (losgutschlechtDto.getLossollarbeitsplanIId() == null || losgutschlechtDto.getNGut() == null
				|| losgutschlechtDto.getNSchlecht() == null || losgutschlechtDto.getNInarbeit() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"losgutschlechtDto.getLossollarbeitsplanIId() == null || losgutschlechtDto.getNGut() == null || losgutschlechtDto.getNSchlecht() == null || losgutschlechtDto.getNInarbeit() == null"));
		}

		pruefeHatZeitdaten(losgutschlechtDto, theClientDto);

		losgutschlechtDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		losgutschlechtDto.setTAnlegen(getTimestamp());

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_LOSGUTSCHLECHT);
			losgutschlechtDto.setIId(pk);

			Losgutschlecht loszusatzstatus = new Losgutschlecht(losgutschlechtDto.getIId(),
					losgutschlechtDto.getLossollarbeitsplanIId(), losgutschlechtDto.getNGut(),
					losgutschlechtDto.getNSchlecht(), losgutschlechtDto.getNInarbeit(),
					losgutschlechtDto.getTZeitpunkt(), losgutschlechtDto.getPersonalIIdAnlegen(),
					losgutschlechtDto.getTAnlegen());
			em.persist(loszusatzstatus);
			em.flush();
			setLosgutschlechtFromLosutschlechtDto(loszusatzstatus, losgutschlechtDto);

			isLosNochErfuellbar(losgutschlechtDto.getLossollarbeitsplanIId(), theClientDto);

			LosgutschlechtRueckmeldungDto rueckDto = new LosgutschlechtRueckmeldungDto();
			rueckDto.setMaterialbuchungNichtDurchgefuehrt(
					zugehoerigesMaterialNachbuchen(losgutschlechtDto, theClientDto));
			rueckDto.setLosgutschlechtIId(losgutschlechtDto.getIId());

			return rueckDto;
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

	}

	private Map<Integer, BigDecimal> zugehoerigesMaterialNachbuchen(LosgutschlechtDto losgutschlechtDto,
			TheClientDto theClientDto) {
		// PJ20204

		Map<Integer, BigDecimal> m = new LinkedHashMap<Integer, BigDecimal>();

		LossollarbeitsplanDto sollapDto = lossollarbeitsplanFindByPrimaryKey(
				losgutschlechtDto.getLossollarbeitsplanIId());
		if (sollapDto.getLossollmaterialIId() != null) {
			LossollmaterialDto sollmatDto = lossollmaterialFindByPrimaryKey(sollapDto.getLossollmaterialIId());
			if (sollmatDto.getNMenge().doubleValue() > 0) {

				LosDto losDto = losFindByPrimaryKey(sollmatDto.getLosIId());
				BigDecimal bdAusgegeben = getAusgegebeneMenge(sollmatDto.getIId(), null, theClientDto);
				BigDecimal ssg = sollmatDto.getNMenge().divide(losDto.getNLosgroesse(), 4, BigDecimal.ROUND_HALF_UP);

				LosablieferungDto[] dtos = losablieferungFindByLosIId(losDto.getIId(), false, theClientDto);
				BigDecimal bdBereitsabgeliefert = new BigDecimal(0);
				for (int i = 0; i < dtos.length; i++) {
					bdBereitsabgeliefert = bdBereitsabgeliefert.add(dtos[i].getNMenge());
				}

				BigDecimal bdSummeGut = null;
				BigDecimal bdSummeSchlecht = null;
				ArtikelDto aDto = null;
				try {
					bdSummeGut = getZeiterfassungFac().getMengeGutSchlechtEinesLosSollarbeitsplanes(
							losgutschlechtDto.getLossollarbeitsplanIId(), theClientDto, true);
					bdSummeSchlecht = getZeiterfassungFac().getMengeGutSchlechtEinesLosSollarbeitsplanes(
							losgutschlechtDto.getLossollarbeitsplanIId(), theClientDto, false);
				} catch (RemoteException e1) {
					throwEJBExceptionLPRespectOld(e1);
				}

				BigDecimal soll = bdSummeGut.add(bdSummeSchlecht).multiply(ssg);

				BigDecimal diff = soll.subtract(bdAusgegeben);

				try {

					if (diff.doubleValue() > 0) {

						aDto = getArtikelFac().artikelFindByPrimaryKey(sollmatDto.getArtikelIId(), theClientDto);

						ArrayList<BucheSerienChnrAufLosDto> bucheSerienChnrAufLosDtos = null;

						if (aDto.istArtikelSnrOderchargentragend()) {

							bucheSerienChnrAufLosDtos = new ArrayList<BucheSerienChnrAufLosDto>();

							LoslagerentnahmeDto[] laeger = loslagerentnahmeFindByLosIId(losDto.getIId());

							BigDecimal bdAbzubuchendeMenge = diff;

							for (int j = 0; j < laeger.length; j++) {

								SeriennrChargennrAufLagerDto[] snrchnrDtos = getLagerFac()
										.getAllSerienChargennrAufLagerInfoDtos(sollmatDto.getArtikelIId(),
												laeger[j].getLagerIId(), false, null, theClientDto);

								for (int k = 0; k < snrchnrDtos.length; k++) {

									if (bdAbzubuchendeMenge.doubleValue() > 0) {

										BucheSerienChnrAufLosDto buchenDto = new BucheSerienChnrAufLosDto();
										buchenDto.setArtikelIId(sollmatDto.getArtikelIId());
										buchenDto.setLagerIId(laeger[j].getLagerIId());
										buchenDto.setLossollmaterialIId(sollmatDto.getIId());
										buchenDto.setCSeriennrChargennr(snrchnrDtos[k].getCSeriennrChargennr());

										if (snrchnrDtos[k].getNMenge().doubleValue() >= bdAbzubuchendeMenge
												.doubleValue()) {
											buchenDto.setNMenge(bdAbzubuchendeMenge);
											bucheSerienChnrAufLosDtos.add(buchenDto);

											bdAbzubuchendeMenge = BigDecimal.ZERO;
											break;
										} else {
											buchenDto.setNMenge(snrchnrDtos[k].getNMenge());
											bdAbzubuchendeMenge = bdAbzubuchendeMenge
													.subtract(snrchnrDtos[k].getNMenge());
											bucheSerienChnrAufLosDtos.add(buchenDto);
										}

									}

								}
							}
							getFertigungFac().bucheMaterialAufLos(losDto, new LossollmaterialDto[] { sollmatDto },
									bdSummeGut.add(bdSummeSchlecht), false, false, false, theClientDto,
									bucheSerienChnrAufLosDtos, false);

						} else {
							// SP8264
							BigDecimal zusaetzlicheMenge = bdSummeGut.add(bdSummeSchlecht)
									.subtract(bdBereitsabgeliefert);
							if (zusaetzlicheMenge.doubleValue() > 0) {
								getFertigungFac().bucheMaterialAufLos(losDto, new LossollmaterialDto[] { sollmatDto },
										zusaetzlicheMenge, false, false, false, theClientDto, bucheSerienChnrAufLosDtos,
										false);
							}
						}

					}

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				BigDecimal bdAusgegebenNeu = getAusgegebeneMenge(sollmatDto.getIId(), null, theClientDto);
				if (soll.doubleValue() > bdAusgegebenNeu.doubleValue()) {
					m.put(aDto.getIId(), soll.subtract(bdAusgegebenNeu));
				}

			}

		}
		return m;
	}

	public ArrayList<KeyValue> getListeDerArbeitsgaenge(Integer losIId, TheClientDto theClientDto) {

		ArrayList<KeyValue> m = new ArrayList<KeyValue>();

		BestellvorschlagDto bvDto = null;

		int iNachkommastellenMenge = 2;
		try {

			iNachkommastellenMenge = getMandantFac().getNachkommastellenMenge(theClientDto.getMandant());

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		LossollarbeitsplanDto[] saDtos = lossollarbeitsplanFindByLosIId(losIId);

		// Ueberschrift

		String ueberschrift = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen("AG", 3, true);
		ueberschrift += " ";
		ueberschrift += Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(" Bezeichnung", 40);

		ueberschrift += Helper.fitString2LengthHTMLBefuelltMitLeerzeichen("Gut", 10, true);

		ueberschrift += Helper.fitString2LengthHTMLBefuelltMitLeerzeichen("Schlecht", 10, true);
		ueberschrift += Helper.fitString2LengthHTMLBefuelltMitLeerzeichen("In Arbeit", 10, true);

		ueberschrift += Helper.fitString2LengthHTMLBefuelltMitLeerzeichen("Fertig", 9, true);

		ueberschrift = "<html><body><font color=\"#000000\">" + ueberschrift + "</font></body></html>";
		KeyValue mZeile = new KeyValue(null, ueberschrift);

		m.add(mZeile);

		mZeile = new KeyValue(null,
				"-----------------------------------------------------------------------------------");
		m.add(mZeile);

		for (LossollarbeitsplanDto saDto : saDtos) {

			String ag = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(saDto.getIArbeitsgangnummer() + " ", 3, true);

			ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(saDto.getArtikelIIdTaetigkeit(), theClientDto);

			String bez = aDto.getCNr();
			if (aDto.getCBezAusSpr() != null && aDto.getCBezAusSpr().length() > 0) {
				bez = aDto.getCBezAusSpr();
			}

			String sZeile = ag + " " + Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(bez, 40, false);

			BigDecimal[] bd = getGutSchlechtInarbeit(saDto.getIId(), theClientDto);

			// Gut
			sZeile += Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(
					Helper.formatZahl(bd[0], iNachkommastellenMenge, theClientDto.getLocUi()), 10, true);
			// Schlecht
			sZeile += Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(
					Helper.formatZahl(bd[1], iNachkommastellenMenge, theClientDto.getLocUi()), 10, true);
			// In Arbeit
			sZeile += Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(
					Helper.formatZahl(bd[2], iNachkommastellenMenge, theClientDto.getLocUi()), 10, true);

			if (Helper.short2boolean(saDto.getBFertig())) {
				sZeile += Helper.fitString2LengthHTMLBefuelltMitLeerzeichen("F", 5, true);
			} else {
				sZeile += Helper.fitString2LengthHTMLBefuelltMitLeerzeichen("IA", 5, true);
			}

			String s = "<html><body><font color=\"#000000\">" + sZeile + "</font></body></html>";

			mZeile = new KeyValue(saDto, s);
			m.add(mZeile);

		}
		return m;
	}

	public LosgutschlechtRueckmeldungDto createLosgutschlechtMitMaschine(LosgutschlechtDto losgutschlechtDto,
			TheClientDto theClientDto) {

		LosgutschlechtRueckmeldungDto rueckDto = createLosgutschlecht(losgutschlechtDto, theClientDto);

		// Bei Aenderungen auch in PersonalFacBeanWS.bucheZeitMitStueckmeldung
		// aendern!!!!!

		LossollarbeitsplanDto lossollDto = lossollarbeitsplanFindByPrimaryKey(
				losgutschlechtDto.getLossollarbeitsplanIId());

		Query query = em.createNamedQuery("LossollarbeitsplanfindByLosIIdIArbeitsgangnummer");
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
				Lossollarbeitsplan lossollarbeitsplanTemp = (Lossollarbeitsplan) iterator.next();
				if (lossollarbeitsplanTemp.getAgartCNr() != null
						&& lossollarbeitsplanTemp.getAgartCNr().equals(StuecklisteFac.AGART_UMSPANNZEIT)) {

					if (lossollarbeitsplanTemp.getIId().equals(lossollDto.getIId())) {
						bIchBinDerErsteUnterarbeitsgangMitUmsapnnzeit = true;

					}
					break;
				}
			}

			if (bIchBinDerErsteUnterarbeitsgangMitUmsapnnzeit == true) {
				iterator = cl.iterator();
				while (iterator.hasNext()) {
					Lossollarbeitsplan lossollarbeitsplanTemp = (Lossollarbeitsplan) iterator.next();

					if (!lossollarbeitsplanTemp.getIId().equals(losgutschlechtDto.getLossollarbeitsplanIId())) {

						if (lossollarbeitsplanTemp.getAgartCNr() != null && (Helper
								.short2boolean(lossollarbeitsplanTemp.getBNurmaschinenzeit())
								&& lossollarbeitsplanTemp.getAgartCNr().equals(StuecklisteFac.AGART_LAUFZEIT))) {

							losgutschlechtDto.setLossollarbeitsplanIId(lossollarbeitsplanTemp.getIId());

							// Die letzte Maschinenzeit des Arbeitganges
							// verwenden
							Session session = FLRSessionFactory.getFactory().openSession();
							org.hibernate.Criteria liste = session.createCriteria(FLRMaschinenzeitdaten.class);
							if (lossollarbeitsplanTemp.getMaschineIId() != null)
								// Maschine nur filtern wenn im Arbeitsgang
								// definiert, sonst erste verwenden (bei ?? fuer
								// Maschine am Terminal)
								liste.add(Expression.eq("maschine_i_id", lossollarbeitsplanTemp.getMaschineIId()));
							liste.add(Expression.eq("lossollarbeitsplan_i_id", lossollarbeitsplanTemp.getIId()));

							liste.addOrder(Order.desc("t_von"));
							liste.setMaxResults(1);
							List<?> letztesKommt = liste.list();

							Iterator it = letztesKommt.iterator();

							if (it.hasNext()) {
								FLRMaschinenzeitdaten mz = (FLRMaschinenzeitdaten) it.next();
								losgutschlechtDto.setMaschinenzeitdatenIId(mz.getI_id());
								losgutschlechtDto.setZeitdatenIId(null);
								rueckDto.getMaterialbuchungNichtDurchgefuehrt()
										.putAll(createLosgutschlecht(losgutschlechtDto, theClientDto)
												.getMaterialbuchungNichtDurchgefuehrt());
							}

						}
					}
				}
			}

		}
		return rueckDto;
	}

	public void losLaegerAnhandStandortErstellen(Integer losIId, Integer stuecklisteIId, Integer panrtnerIIdStandort,
			TheClientDto theClientDto) {

		// Zuerst alle entfernen

		Query query = em.createNamedQuery("LoslagerentnahmefindByLosIId");
		query.setParameter(1, losIId);
		Collection<?> cl = query.getResultList();
		Iterator it = cl.iterator();
		while (it.hasNext()) {
			Loslagerentnahme l = (Loslagerentnahme) it.next();
			em.remove(l);
		}

		StklagerentnahmeDto[] stklagerentnahmeDtos = getStuecklisteFac()
				.stklagerentnahmeFindByStuecklisteIId(stuecklisteIId);
		if (stklagerentnahmeDtos != null && stklagerentnahmeDtos.length > 0) {
			for (int i = 0; i < stklagerentnahmeDtos.length; i++) {
				LoslagerentnahmeDto loslager = new LoslagerentnahmeDto();
				loslager.setLosIId(losIId);
				loslager.setLagerIId(stklagerentnahmeDtos[i].getLagerIId());
				createLoslagerentnahme(loslager, theClientDto);
			}
		} else {
			// Hauptlager anlegen
			try {
				LagerDto lagerDto = getLagerFac().getHauptlagerDesMandanten(theClientDto);

				LoslagerentnahmeDto loslager = new LoslagerentnahmeDto();
				loslager.setLosIId(losIId);
				loslager.setLagerIId(lagerDto.getIId());
				createLoslagerentnahme(loslager, theClientDto);
			} catch (EJBExceptionLP e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}

	}

	public Integer createLostechniker(LostechnikerDto lostechnikerDto, TheClientDto theClientDto) {

		if (lostechnikerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("lostechnikerDto == null"));
		}
		if (lostechnikerDto.getLosIId() == null || lostechnikerDto.getPersonalIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("lostechnikerDto.getLosIId() == null || lostechnikerDto.getPersonalIId() == null"));
		}
		try {
			Query query = em.createNamedQuery("LostechnikerfindByLosIIdPersonalIId");
			query.setParameter(1, lostechnikerDto.getLosIId());
			query.setParameter(2, lostechnikerDto.getPersonalIId());
			// @todo getSingleResult oder getResultList ?
			Lostechniker doppelt = (Lostechniker) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("FERT_LOSTECHNIKER.UK"));
		} catch (NoResultException ex) {

		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_LOSTECHNIKER);
			lostechnikerDto.setIId(pk);

			Lostechniker lostechniker = new Lostechniker(lostechnikerDto.getIId(), lostechnikerDto.getLosIId(),
					lostechnikerDto.getPersonalIId());
			em.persist(lostechniker);
			em.flush();
			setLostechnikerFromLostechnikerDto(lostechniker, lostechnikerDto);

			return lostechnikerDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

	}

	public void terminVerschieben(Integer losIId, Timestamp tsBeginnNeu, Timestamp tsEndeNeu,
			TheClientDto theClientDto) {

		Los los = em.find(Los.class, losIId);

		LosDto losDto = losFindByPrimaryKey(losIId);
		LosDto losDto_vorher = losFindByPrimaryKey(losDto.getIId());

		if (los.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)
				|| los.getStatusCNr().equals(FertigungFac.STATUS_AUSGEGEBEN)
				|| los.getStatusCNr().equals(FertigungFac.STATUS_IN_PRODUKTION)
				|| los.getStatusCNr().equals(FertigungFac.STATUS_GESTOPPT)
				|| los.getStatusCNr().equals(FertigungFac.STATUS_TEILERLEDIGT)) {

			LossollarbeitsplanDto[] saDtos = lossollarbeitsplanFindByLosIId(losIId);

			los.setTProduktionsende(new java.sql.Date(tsEndeNeu.getTime()));

			// Nun den Beginntermin berechnen
			Integer iAgBeginnAutomatischErmittelnInStd = 0;
			boolean bReihenfolgenplanung = false;
			try {
				ParametermandantDto parameterM = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_AUTOMATISCHE_ERMITTLUNG_AG_BEGINN);
				iAgBeginnAutomatischErmittelnInStd = ((Integer) parameterM.getCWertAsObject());

				ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_REIHENFOLGENPLANUNG);
				bReihenfolgenplanung = (Boolean) parameter.getCWertAsObject();
			} catch (RemoteException ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			}
			if (iAgBeginnAutomatischErmittelnInStd != null && iAgBeginnAutomatischErmittelnInStd.intValue() > 0
					&& bReihenfolgenplanung == false) {

				// nach AG/UAG sortieren

				for (int k = saDtos.length - 1; k > 0; --k) {
					for (int j = 0; j < k; ++j) {
						LossollarbeitsplanDto a1 = (LossollarbeitsplanDto) saDtos[j];
						LossollarbeitsplanDto a2 = (LossollarbeitsplanDto) saDtos[j + 1];
						if (a1.getIArbeitsgangnummer() > a2.getIArbeitsgangnummer()) {
							saDtos[j] = a2;
							saDtos[j + 1] = a1;
						} else if (a1.getIArbeitsgangnummer() == a2.getIArbeitsgangnummer()) {
							if (a1.getIUnterarbeitsgang() != null && a2.getIUnterarbeitsgang() != null
									&& a1.getIUnterarbeitsgang() > a2.getIUnterarbeitsgang()) {
								saDtos[j] = a2;
								saDtos[j + 1] = a1;
							}
						}
					}
				}

				// Firmenzeitmodell ermitteln

				Integer zeitmodellIId = null;

				Query queryFZ = em.createNamedQuery("ZeitmodellfindByBFirmenzeitmodellMandantCNr");
				queryFZ.setParameter(1, new Short((short) 1));
				queryFZ.setParameter(2, theClientDto.getMandant());
				// @todo getSingleResult oder getResultList ?
				Collection c = queryFZ.getResultList();
				if (c.size() > 0) {

					zeitmodellIId = ((Zeitmodell) c.iterator().next()).getIId();

				} else {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FIRMENZEITMODELL_NICHT_VORHANDEN,
							new Exception("FEHLER_FIRMENZEITMODELL_NICHT_VORHANDEN"));
				}

				Calendar cTempBeginn = Calendar.getInstance();
				cTempBeginn.setTimeInMillis(Helper.cutDate(tsEndeNeu).getTime());

				int iAnzahlSlotsBenoetigtGesamt = 0;
				for (int i = saDtos.length; i > 0; i--) {

					LossollarbeitsplanDto stklAPDto = saDtos[i - 1];

					BigDecimal bdGesamtzeit = stklAPDto.getNGesamtzeit();

					int iAnzahlSlotsAufDerPosition = (int) Math
							.ceil(bdGesamtzeit.doubleValue() / iAgBeginnAutomatischErmittelnInStd);

					// SP9151
					// Lt. WH muss ab sofort, auch wenn Sollzeit 0, ein Slot verbraucht werden.
					if (iAnzahlSlotsAufDerPosition == 0) {
						iAnzahlSlotsAufDerPosition = 1;
					}

					iAnzahlSlotsBenoetigtGesamt += iAnzahlSlotsAufDerPosition;

				}

				ArrayList<java.util.Date> alSlots = new ArrayList<java.util.Date>();

				int iZahler = 0;
				while (alSlots.size() < iAnzahlSlotsBenoetigtGesamt) {
					iZahler++;

					cTempBeginn.add(Calendar.DATE, -1);

					ZeitmodelltagDto zmtagDto = getZeiterfassungFac().getZeitmodelltagFirmenzeitmodellZuDatum(
							zeitmodellIId, new Timestamp(cTempBeginn.getTimeInMillis()), theClientDto);

					if (zmtagDto != null && zmtagDto.getUSollzeit() != null) {

						double dSollzeit = Helper.time2Double(zmtagDto.getUSollzeit());

						int iAnzahlHabenPlatz = (int) Math.floor(dSollzeit / iAgBeginnAutomatischErmittelnInStd);

						for (int i = 0; i < iAnzahlHabenPlatz; i++) {
							alSlots.add(cTempBeginn.getTime());
						}

					}

					if (iZahler > 1000) {
						// Fehler kann auftreten, wenn unzureichend Sollzeit
						// definiert ist, z.b. Wenn die Slots auf 4 Stunden
						// eingestellt sind, es jedoch nur Sollzeiten unter
						// 4 Stunden gibt
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_FIRMENZEITMODELL_NICHT_AUSREICHEND_SOLLZEIT_DEFINIERT,
								new Exception("FEHLER_FIRMENZEITMODELL_NICHT_AUSREICHEND_SOLLZEIT_DEFINIERT"));

					}

				}

				java.sql.Timestamp tBeginntermin = Helper.cutTimestamp(new Timestamp(cTempBeginn.getTimeInMillis()));

				los.setTProduktionsbeginn(new java.sql.Date(tBeginntermin.getTime()));

				for (int i = saDtos.length; i > 0; i--) {

					LossollarbeitsplanDto losAPDto = saDtos[i - 1];

					BigDecimal bdGesamtzeit = losAPDto.getNGesamtzeit();

					int iAnzahlSlotsAufDerPosition = (int) Math
							.ceil(bdGesamtzeit.doubleValue() / iAgBeginnAutomatischErmittelnInStd);

					// SP9151
					// Lt. WH muss ab sofort, auch wenn Sollzeit 0, ein Slot verbraucht werden.
					if (iAnzahlSlotsAufDerPosition == 0) {
						iAnzahlSlotsAufDerPosition = 1;
					}

					java.util.Date agBeginn = null;
					for (int j = 0; j < iAnzahlSlotsAufDerPosition; j++) {

						agBeginn = alSlots.get(0);

						alSlots.remove(0);

					}
					int iAgBeginn = 0;

					if (agBeginn != null) {
						iAgBeginn = Helper.ermittleTageEinesZeitraumes(tBeginntermin,
								new java.sql.Date(agBeginn.getTime()));
					}

					losAPDto.setIMaschinenversatztage(iAgBeginn);

					updateLossollarbeitsplan(losAPDto, false, theClientDto);

					saDtos[i - 1] = losAPDto;

				}

			} else {

				los.setTProduktionsbeginn(new java.sql.Date(tsBeginnNeu.getTime()));

			}
			em.merge(los);
			em.flush();

			losDto.setTProduktionsende(los.getTProduktionsende());
			losDto.setTProduktionsbeginn(los.getTProduktionsbeginn());

			try {
				ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_AUTOMATISCHE_ERMITTLUNG_LOS_ENDE);

				boolean bAutomatischeErmittlungLosEnde = (Boolean) parameter.getCWertAsObject();

				if (bAutomatischeErmittlungLosEnde) {
					berechneLosEndeAnhandMaschinenversatztage(losDto.getIId(), theClientDto);
				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			HvDtoLogger<LosDto> logger = new HvDtoLogger<LosDto>(em, losDto.getIId(), theClientDto);
			logger.log(losDto_vorher, losDto);

			// SP9450
			automatischeErmittlungBeginnterminOffset(losIId, null, theClientDto);

			LossollmaterialDto[] sollmat = lossollmaterialFindByLosIId(losIId);

			for (int i = 0; i < sollmat.length; i++) {
				try {

					getFehlmengeFac().aktualisiereFehlmenge(LocaleFac.BELEGART_LOS, sollmat[i].getIId(), theClientDto);

					ArtikelreservierungDto reservierungDto = getReservierungFac()
							.artikelreservierungFindByBelegartCNrBelegartPositionIIdOhneExc(LocaleFac.BELEGART_LOS,
									sollmat[i].getIId());
					if (reservierungDto != null) {
						if (reservierungDto != null && reservierungDto.getNMenge().doubleValue() >= 0) {
							reservierungDto.setTLiefertermin(tsBeginnNeu);
						} else {
							reservierungDto.setTLiefertermin(tsEndeNeu);
						}
						getReservierungFac().updateArtikelreservierung(reservierungDto);
					}

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}

		} else if (los.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
					new Exception("los " + los.getCNr() + " ist bereits erledigt"));

		} else if (los.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT,
					new Exception("los " + los.getCNr() + " ist bereits storniert"));

		} else if (los.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN,
					new Exception("los " + los.getCNr() + " ist noch nicht ausgegeben"));

		}

	}

	public void removeLoszusatzstatus(LoszusatzstatusDto loszusatzstatusDto) throws EJBExceptionLP {
		if (loszusatzstatusDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("loszusatzstatusDto == null"));
		}
		if (loszusatzstatusDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("loszusatzstatusDto.getIId() == null"));
		}

		// try {
		Loszusatzstatus toRemove = em.find(Loszusatzstatus.class, loszusatzstatusDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("losgutschlechtDto == null"));
		}
		if (losgutschlechtDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("losgutschlechtDto.getIId() == null"));
		}

		Losgutschlecht toRemove = em.find(Losgutschlecht.class, losgutschlechtDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("lostechnikerDto == null"));
		}
		if (lostechnikerDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("lostechnikerDto.getIId() == null"));
		}

		Lostechniker toRemove = em.find(Lostechniker.class, lostechnikerDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void updateLoszusatzstatus(LoszusatzstatusDto loszusatzstatusDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (loszusatzstatusDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("loszusatzstatusDto == null"));
		}
		if (loszusatzstatusDto.getIId() == null || loszusatzstatusDto.getLosIId() == null
				|| loszusatzstatusDto.getZusatzstatusIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"loszusatzstatusDto.getIId() == null || loszusatzstatusDto.getLosIId() == null || loszusatzstatusDto.getZusatzstatusIId() == null"));
		}

		Integer iId = loszusatzstatusDto.getIId();

		// try {
		Query query = em.createNamedQuery("LoszusatzstatusfindByLosIIdZusatzstatusIId");
		query.setParameter(1, loszusatzstatusDto.getLosIId());
		query.setParameter(2, loszusatzstatusDto.getZusatzstatusIId());
		Integer iIdVorhanden = ((Loszusatzstatus) query.getSingleResult()).getIId();
		if (iId.equals(iIdVorhanden) == false) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("FERT_LOSZUSATZSTATUS.UK"));
		}

		// }
		// catch (FinderException ex) {
		//
		// }

		// try {

		loszusatzstatusDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		loszusatzstatusDto.setTAendern(new Timestamp(System.currentTimeMillis()));

		Loszusatzstatus loszusatzstatus = em.find(Loszusatzstatus.class, iId);
		if (loszusatzstatus == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setLoszusatzstatusFromLoszusatzstatusDto(loszusatzstatus, loszusatzstatusDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public void updateLosgutschlecht(LosgutschlechtDto losgutschlechtDto, TheClientDto theClientDto) {

		if (losgutschlechtDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("losgutschlechtDto == null"));
		}
		if (losgutschlechtDto.getIId() == null || losgutschlechtDto.getLossollarbeitsplanIId() == null
				|| losgutschlechtDto.getNGut() == null || losgutschlechtDto.getNSchlecht() == null
				|| losgutschlechtDto.getNInarbeit() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"losgutschlechtDto.getIId() == null || losgutschlechtDto.getLossollarbeitsplanIId() == null || losgutschlechtDto.getNGut() == null || losgutschlechtDto.getNSchlecht() == null || losgutschlechtDto.getNInarbeit() == null"));
		}

		pruefeHatZeitdaten(losgutschlechtDto, theClientDto);

		Integer iId = losgutschlechtDto.getIId();
		Losgutschlecht losgutschlecht = em.find(Losgutschlecht.class, iId);
		if (losgutschlecht == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setLosgutschlechtFromLosutschlechtDto(losgutschlecht, losgutschlechtDto);
		isLosNochErfuellbar(losgutschlechtDto.getLossollarbeitsplanIId(), theClientDto);
	}

	/**
	 * Pruefe ob das LosgutschlechtDto Zeitdaten hat, falls erfordert und wirft eine
	 * Exception wenn nicht (die Exception kann gleich weiter geworfen werden)
	 * 
	 * @param losgutschlechtDto
	 * @param theClientDto
	 */
	private void pruefeHatZeitdaten(LosgutschlechtDto losgutschlechtDto, TheClientDto theClientDto) {
		try {
			boolean benoetigtZeitbuchung = (Boolean) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_STUECKRUECKMELDUNG_BENOETIGT_ZEITERFASSUNG)
					.getCWertAsObject();

			if (losgutschlechtDto.getZeitdatenIId() == null && losgutschlechtDto.getMaschinenzeitdatenIId() == null
					&& benoetigtZeitbuchung) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
						"losgutschlechtDto.getZeitdatenIId() == null && getMaschinenzeitdatenIId() ==null"));

			}
		} catch (RemoteException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
	}

	public void updateLostechniker(LostechnikerDto lostechnikerDto, TheClientDto theClientDto) {

		if (lostechnikerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("lostechnikerDto == null"));
		}
		if (lostechnikerDto.getIId() == null || lostechnikerDto.getLosIId() == null
				|| lostechnikerDto.getPersonalIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"lostechnikerDto.getIId() == null || lostechnikerDto.getLosIId() == null 	|| lostechnikerDto.getPersonalIId() == null"));
		}

		Integer iId = lostechnikerDto.getIId();

		Query query = em.createNamedQuery("LostechnikerfindByLosIIdPersonalIId");
		query.setParameter(1, lostechnikerDto.getLosIId());
		query.setParameter(2, lostechnikerDto.getPersonalIId());
		try {
			Lostechniker doppelt = (Lostechniker) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("FERT_LOSTECHNIKER.UK"));
		} catch (NoResultException e) {
			//
		}

		Lostechniker lostechniker = em.find(Lostechniker.class, iId);
		if (lostechniker == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setLostechnikerFromLostechnikerDto(lostechniker, lostechnikerDto);

	}

	public LoszusatzstatusDto loszusatzstatusFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}

		// try {
		Loszusatzstatus loszusatzstatus = em.find(Loszusatzstatus.class, iId);
		if (loszusatzstatus == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}
		Losgutschlecht losgutschlecht = em.find(Losgutschlecht.class, iId);
		if (losgutschlecht == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLosgutschlechtDto(losgutschlecht);
	}

	public LosgutschlechtDto[] losgutschlechtFindAllFehler(Integer losIId) {
		if (losIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("losIId == null"));

		}

		SessionFactory factory = FLRSessionFactory.getFactory();
		org.hibernate.Session session = factory.openSession();
		String queryString = "SELECT lgs FROM FLRLosgutschlecht lgs WHERE  lgs.flrlossollarbeitsplan.flrlos.i_id="
				+ losIId + " AND lgs.flrfehler IS NOT NULL ";
		queryString += " ORDER BY lgs.flrlossollarbeitsplan.i_arbeitsgangsnummer ";

		org.hibernate.Query query = session.createQuery(queryString);

		List<?> resultList = query.list();
		Iterator it = resultList.iterator();

		LosgutschlechtDto[] losgutschlechtDtos = new LosgutschlechtDto[resultList.size()];

		int i = 0;
		while (it.hasNext()) {
			FLRLosgutschlecht lgs = (FLRLosgutschlecht) it.next();

			losgutschlechtDtos[i] = losgutschlechtFindByPrimaryKey(lgs.getI_id());
			i++;
		}

		session.close();

		return losgutschlechtDtos;
	}

	public LosgutschlechtDto[] losgutschlechtFindByLossollarbeitsplanIId(Integer losIId) {
		if (losIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("losIId == null"));

		}
		LosgutschlechtDto[] losgutschlechtDtos = null;
		// try {
		Query query = em.createNamedQuery("LosgutschlechtFindByLossollarbeitsplanIId");
		query.setParameter(1, losIId);
		Collection<?> cl = query.getResultList();
		losgutschlechtDtos = assembleLosgutschlechtDtos(cl);
		return losgutschlechtDtos;
	}

	public ArrayList<Integer> getAllPersonalIIdEinesSollarbeitsplansUeberLogGutSchlecht(Integer loslollarbeitsplanIId) {

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

	public LosgutschlechtDto[] losgutschlechtFindByZeitdatenIId(Integer zeitdatenIId) {
		if (zeitdatenIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("zeitdatenIId == null"));

		}
		LosgutschlechtDto[] losgutschlechtDtos = null;
		// try {
		Query query = em.createNamedQuery("LosgutschlechtFindByZeitdatenIId");
		query.setParameter(1, zeitdatenIId);
		Collection<?> cl = query.getResultList();
		losgutschlechtDtos = assembleLosgutschlechtDtos(cl);
		return losgutschlechtDtos;
	}

	public LoszusatzstatusDto loszusatzstatusFindByLosIIdZusatzstatusIId(Integer losIId, Integer zusatzstatusIId)
			throws EJBExceptionLP {
		try {
			Query query = em.createNamedQuery("LoszusatzstatusfindByLosIIdZusatzstatusIId");
			query.setParameter(1, losIId);
			query.setParameter(2, zusatzstatusIId);
			return assembleLoszusatzstatusDto((Loszusatzstatus) query.getSingleResult());
		} catch (EJBExceptionLP e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}
	}

	public LoszusatzstatusDto loszusatzstatusFindByLosIIdZusatzstatusIIdOhneExc(Integer losIId,
			Integer zusatzstatusIId) {
		try {
			Query query = em.createNamedQuery("LoszusatzstatusfindByLosIIdZusatzstatusIId");
			query.setParameter(1, losIId);
			query.setParameter(2, zusatzstatusIId);
			return assembleLoszusatzstatusDto((Loszusatzstatus) query.getSingleResult());
		} catch (NoResultException ex) {
			//
		}
		return null;
	}

	@Override
	public LoszusatzstatusDto[] loszusatzstatusFindByLosIIdOhneExc(Integer losIId) throws RemoteException {
		try {
			Query query = em.createNamedQuery("LoszusatzstatusfindByLosIId");
			query.setParameter(1, losIId);
			return assembleLoszusatzstatusDtos(query.getResultList());
		} catch (NoResultException ex) {
		}

		return null;
	}

	private void setLoszusatzstatusFromLoszusatzstatusDto(Loszusatzstatus loszusatzstatus,
			LoszusatzstatusDto loszusatzstatusDto) {
		loszusatzstatus.setLosIId(loszusatzstatusDto.getLosIId());
		loszusatzstatus.setZusatzstatusIId(loszusatzstatusDto.getZusatzstatusIId());
		loszusatzstatus.setPersonalIIdAendern(loszusatzstatusDto.getPersonalIIdAendern());
		loszusatzstatus.setTAendern(loszusatzstatusDto.getTAendern());
		em.merge(loszusatzstatus);
		em.flush();
	}

	private void setLosgutschlechtFromLosutschlechtDto(Losgutschlecht losgutschlecht,
			LosgutschlechtDto losgutschlechtDto) {
		losgutschlecht.setLossollarbeitsplanIId(losgutschlechtDto.getLossollarbeitsplanIId());
		losgutschlecht.setZeitdatenIId(losgutschlechtDto.getZeitdatenIId());
		losgutschlecht.setNGut(losgutschlechtDto.getNGut());
		losgutschlecht.setNSchlecht(losgutschlechtDto.getNSchlecht());
		losgutschlecht.setNInarbeit(losgutschlechtDto.getNInarbeit());
		losgutschlecht.setMaschinenzeitdatenIId(losgutschlechtDto.getMaschinenzeitdatenIId());
		losgutschlecht.setCKommentar(losgutschlechtDto.getCKommentar());
		losgutschlecht.setFehlerIId(losgutschlechtDto.getFehlerIId());
		losgutschlecht.setTZeitpunkt(losgutschlechtDto.getTZeitpunkt());
		losgutschlecht.setPersonalIIdErfasst(losgutschlechtDto.getPersonalIIdErfasst());
		losgutschlecht.setPersonalIIdAnlegen(losgutschlechtDto.getPersonalIIdAnlegen());
		losgutschlecht.setTAnlegen(losgutschlechtDto.getTAnlegen());
		em.merge(losgutschlecht);
		em.flush();
	}

	private void setLostechnikerFromLostechnikerDto(Lostechniker lostechniker, LostechnikerDto lostechnikerDto) {
		lostechniker.setLosIId(lostechnikerDto.getLosIId());
		lostechniker.setPersonalIId(lostechnikerDto.getPersonalIId());

		em.merge(lostechniker);
		em.flush();
	}

	private LoszusatzstatusDto assembleLoszusatzstatusDto(Loszusatzstatus loszusatzstatus) {
		return LoszusatzstatusDtoAssembler.createDto(loszusatzstatus);
	}

	private LosgutschlechtDto assembleLosgutschlechtDto(Losgutschlecht losgutschlecht) {
		return LosgutschlechtDtoAssembler.createDto(losgutschlecht);
	}

	private LoszusatzstatusDto[] assembleLoszusatzstatusDtos(Collection<?> loszusatzstatuss) {
		List<LoszusatzstatusDto> list = new ArrayList<LoszusatzstatusDto>();
		if (loszusatzstatuss != null) {
			Iterator<?> iterator = loszusatzstatuss.iterator();
			while (iterator.hasNext()) {
				Loszusatzstatus loszusatzstatus = (Loszusatzstatus) iterator.next();
				list.add(assembleLoszusatzstatusDto(loszusatzstatus));
			}
		}
		LoszusatzstatusDto[] returnArray = new LoszusatzstatusDto[list.size()];
		return (LoszusatzstatusDto[]) list.toArray(returnArray);
	}

	@Override
	public LosDto setupDefaultLos(TheClientDto theClientDto) throws RemoteException {
		LosDto losDto = new LosDto();
		MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);

		losDto.setMandantCNr(mandantDto.getCNr());
		losDto.setNLosgroesse(BigDecimal.ONE);
		losDto.setPartnerIIdFertigungsort(mandantDto.getPartnerIId());
		losDto.setKostenstelleIId(mandantDto.getIIdKostenstelle());

		FertigungsgruppeDto[] ftgDtos = getStuecklisteFac().fertigungsgruppeFindByMandantCNr(mandantDto.getCNr(),
				theClientDto);
		losDto.setFertigungsgruppeIId(ftgDtos[0].getIId());

		losDto.setTProduktionsbeginn(getDate());
		losDto.setTProduktionsende(getDate());

		LagerDto lagerDto = getLagerFac().getHauptlagerDesMandanten(theClientDto);
		losDto.setLagerIIdZiel(lagerDto.getIId());

		return losDto;
	}

	@Override
	public LossollmaterialDto setupDefaultLossollmaterial(ArtikelDto artikelDto, Integer losIId, Integer lagerIId,
			Integer montageartIId, TheClientDto theClientDto) throws RemoteException {
		LossollmaterialDto lossollmaterialDto = new LossollmaterialDto();
		lossollmaterialDto.setBNachtraeglich(Helper.boolean2Short(true));
		lossollmaterialDto.setArtikelIId(artikelDto.getIId());
		lossollmaterialDto.setEinheitCNr(artikelDto.getEinheitCNr());
		lossollmaterialDto.setLosIId(losIId);
		lossollmaterialDto.setMontageartIId(montageartIId);
		BigDecimal sollpreis = getLagerFac().getGemittelterGestehungspreisEinesLagers(artikelDto.getIId(), lagerIId,
				theClientDto);
		lossollmaterialDto.setNSollpreis(sollpreis);
		lossollmaterialDto.setNMenge(BigDecimal.ZERO);

		return lossollmaterialDto;
	}

	public LosDto losFindByForecastpositionIIdOhneExc(Integer forecastpositionIId) {
		Los los = LosQuery.resultByForecastpositionIId(em, forecastpositionIId);

		return los != null ? assembleLosDto(los) : null;
	}

	@Override
	public List<LosDto> losFindByStuecklisteIIdFertigungsgruppeIIdStatusCnr(Integer stuecklisteIId,
			Integer fertigungsgruppeIId, List<String> stati) {
		List<Los> lose = LosQuery.listByStuecklisteIIdStatusCnr(em, stuecklisteIId, fertigungsgruppeIId, stati);

		return Arrays.asList(assembleLosDtos(lose));
	}

	private EJBExceptionLP postLosausgabeToHost(Integer losIId, TheClientDto theClientDto) {
		EJBExceptionLP ejbExc = postLosausgabeToHydraHost(new LosId(losIId), theClientDto);
		if (ejbExc != null)
			return ejbExc;
		
		return postLosausgabeToTruTopsHost(new LosId(losIId), theClientDto);
	}

	private EJBExceptionLP postLosausgabeToTruTopsHost(LosId losId, TheClientDto theClientDto) {
		if (!getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_TRUTOPS_BOOST,
				theClientDto)) {
			return null;
		}

		HttpProxyConfig hostConfig = getParameterFac().getTruTopsBoostHost(theClientDto.getMandant());
		if (!hostConfig.isDefined())
			return null;

		Los los = em.find(Los.class, losId.id());

		if (getTruTopsLossollmaterial(losId).isEmpty()) {
			return null;
		}

		if (getParameterFac().getAusgabeEigenerStatus(theClientDto.getMandant())
				&& !FertigungFac.STATUS_AUSGEGEBEN.equals(los.getStatusCNr())
				|| !FertigungFac.STATUS_IN_PRODUKTION.equals(los.getStatusCNr())) {
			return null;
		}
		
		HvOptional<KundeId> kundeIdOpt = findKundeZuLos(los);
		if (kundeIdOpt.isEmpty()) {
			// TODO throw Exc
			return postLosausgabeToTruTopsHostImpl(hostConfig, los, theClientDto);
		}
		
		getKundeFac().checkAndCreateAutoDebitorennummerZuKunden(kundeIdOpt.get(), theClientDto);
		
		return postLosausgabeToTruTopsHostImpl(hostConfig, los, theClientDto);
	}
	
	private HvOptional<KundeId> findKundeZuLos(Los los) {
		if (los.getKundeIId() != null) {
			return HvOptional.of(new KundeId(los.getKundeIId()));
		}
		
		if (los.getAuftragIId() != null) {
			Auftrag auftrag = em.find(Auftrag.class, los.getAuftragIId());
			return HvOptional.of(new KundeId(auftrag.getKundeIIdRechnungsadresse()));
		}
		
		return HvOptional.empty();
	}

	private List<Integer> getTruTopsArtklaIIds(String mandantCnr) {
		List<Artkla> truTopsKlassen = ArtklaQuery.listAllTopsByMandantCNr(em, mandantCnr);
		if (truTopsKlassen.isEmpty())
			return new ArrayList<Integer>();

		List<Integer> truTopsKlassenIds = new ArrayList<Integer>();
		truTopsKlassen.forEach(klasse -> truTopsKlassenIds.add(klasse.getIId()));
		return truTopsKlassenIds;
	}

	private List<Lossollmaterial> getTruTopsLossollmaterialImpl(LosId losId, String mandantCnr) {
		List<Integer> truTopsKlassenIds = getTruTopsArtklaIIds(mandantCnr);
		if (truTopsKlassenIds.isEmpty())
			return new ArrayList<Lossollmaterial>();

		return LossollmaterialQuery.listByLosIIdArtklaIIds(em, losId, truTopsKlassenIds);
	}

	@Override
	public List<LossollmaterialDto> getTruTopsLossollmaterial(LosId losId) {
		Validator.pkFieldNotNull(losId, "losId");

		Los los = em.find(Los.class, losId.id());
		Validator.entityFound(los, losId.id());

		List<Lossollmaterial> sollmaterial = getTruTopsLossollmaterialImpl(losId, los.getMandantCNr());
		return assembleLossollmaterialDtosAsList(sollmaterial);
	}

	private List<Lossollarbeitsplan> getTruTopsLossollarbeitsplanImpl(LosId losId, String mandantCnr) {
		List<Integer> truTopsKlassenIds = getTruTopsArtklaIIds(mandantCnr);
		if (truTopsKlassenIds.isEmpty())
			return new ArrayList<Lossollarbeitsplan>();

		return LossollarbeitsplanQuery.listByLosIIdArtklaIIds(em, losId, truTopsKlassenIds);
	}

	@Override
	public List<LossollarbeitsplanDto> getTruTopsLossollarbeitsplan(LosId losId) {
		Validator.pkFieldNotNull(losId, "losId");

		Los los = em.find(Los.class, losId.id());
		Validator.entityFound(los, losId.id());

		List<Lossollarbeitsplan> arbeitsplan = getTruTopsLossollarbeitsplanImpl(losId, los.getMandantCNr());
		return assembleLossollarbeitsplanDtosAsList(arbeitsplan);
	}

	private EJBExceptionLP postLosausgabeToHydraHost(LosId losId, TheClientDto theClientDto) {
		Los los = em.find(Los.class, losId.id());

		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_HYDRA, theClientDto)
				&& !isHydraLos(los, theClientDto.getMandant())) {
			return null;
		}

		HttpProxyConfig hostConfig = getParameterFac().getHvToHydraHost(theClientDto.getMandant());
		if (!hostConfig.isDefined())
			return null;

		if (getParameterFac().getAusgabeEigenerStatus(theClientDto.getMandant())
				&& !FertigungFac.STATUS_AUSGEGEBEN.equals(los.getStatusCNr())
				|| !FertigungFac.STATUS_IN_PRODUKTION.equals(los.getStatusCNr()))
			return null;

		return postLosausgabeToHostImpl(hostConfig, los);
	}

	private boolean isHydraLos(Los los, String mandantCnr) {
		if (!FertigungFac.STATUS_IN_PRODUKTION.equals(los.getStatusCNr()))
			return false;

		Integer hydraFertigungsruppe = getParameterFac().getHydraFertigungsgruppeIId(mandantCnr);
		if (hydraFertigungsruppe == null || !hydraFertigungsruppe.equals(los.getFertigungsgruppeIId())) {
			return false;
		}

		return true;
	}

	private EJBExceptionLP postLosausgabeToTruTopsHostImpl(HttpProxyConfig hostConfig, Los los, TheClientDto theClientDto) {

		HttpRequestConfig requestConfig = new HttpRequestConfig(hostConfig, "/production/" + los.getIId() + "/output");
		try {
			httpPost(requestConfig);
		} catch (IOException e) {
			LosDto losDto = losFindByPrimaryKeyOhneExc(los.getIId());
			throw EJBExcFactory.verbindungsfehlerBeiHttpPostLosausgabe(losDto, hostConfig, e);
		} catch (HttpStatusCodeException e) {
			LosDto losDto = losFindByPrimaryKeyOhneExc(los.getIId());
			HttpStatusCodeExceptionTranslator exTranslator = new HttpStatusCodeExceptionTranslator();
			EJBExceptionLP excLP = exTranslator.toEJBExcLP(e, theClientDto);
			throw excLP != null
					? excLP
					: EJBExcFactory.unerwarteterStatusCodeBeiHttpPostLosausgabe(losDto, e);
		}
		return null;
	}
	
	class HttpStatusCodeExceptionTranslator {
		
		public EJBExceptionLP toEJBExcLP(HttpStatusCodeException ex, TheClientDto theClientDto) {
			if (new Integer(HttpStatus.SC_EXPECTATION_FAILED).equals(ex.getStatusCode())) {
				return translateExpectationFailed(ex, theClientDto);
			}
			return null;
		}

		private EJBExceptionLP translateExpectationFailed(HttpStatusCodeException ex, TheClientDto theClientDto) {
			HvHttpHeader hvHeader = ex.getHvHeader();
			
			String entityKey = hvHeader.getErrorKey();
			if (isArtikel(entityKey)) {
				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(Integer.parseInt(hvHeader.getErrorValue()), theClientDto);
				
				if (new Integer(EJBExceptionLP.FEHLER_TOPS_FALLBACK_MEHRERE_CADFILES_PRO_ARTIKEL)
						.equals(hvHeader.getErrorCodeExtended())) {
					return EJBExcFactory.httpPostLosausgabeTopsFallbackMehrereCadfiles(hvHeader.getErrorCodeDescription(), artikelDto);
				}
				
				if (isPath(hvHeader.getAdditionalErrorKey())) {
					return EJBExcFactory.httpPostLosausgabeTops(hvHeader.getErrorCodeExtended(), 
							hvHeader.getErrorCodeDescription(), artikelDto, hvHeader.getAdditionalErrorValue());
				}
				
				if (isArtikel(hvHeader.getAdditionalErrorKey())) {
					ArtikelDto artikelDto2 = getArtikelFac().artikelFindByPrimaryKey(Integer.parseInt(hvHeader.getAdditionalErrorValue()), theClientDto);
					return EJBExcFactory.httpPostLosausgabeTops(hvHeader.getErrorCodeExtended(), 
							hvHeader.getErrorCodeDescription(), artikelDto, artikelDto2);
				}
				return EJBExcFactory.httpPostLosausgabeTops(hvHeader.getErrorCodeExtended(), 
						hvHeader.getErrorCodeDescription(), artikelDto);
				
			} else if (isLos(entityKey)) {
				LosDto losDto = losFindByPrimaryKey(Integer.parseInt(hvHeader.getErrorValue()));
				return EJBExcFactory.httpPostLosausgabeTops(hvHeader.getErrorCodeExtended(), 
						hvHeader.getErrorCodeDescription(), losDto);

			} else if (isLossollmaterial(entityKey)) {
				LossollmaterialDto sollmaterialDto = lossollmaterialFindByPrimaryKey(Integer.parseInt(hvHeader.getErrorValue()));
				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(sollmaterialDto.getArtikelIId(), theClientDto);
				return EJBExcFactory.httpPostLosausgabeTops(hvHeader.getErrorCodeExtended(), 
						hvHeader.getErrorCodeDescription(), sollmaterialDto, artikelDto);
			
			} else if (isKunde(entityKey)) {
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(Integer.parseInt(hvHeader.getErrorValue()), theClientDto);
				return EJBExcFactory.httpPostLosausgabeTops(hvHeader.getErrorCodeExtended(), 
						hvHeader.getErrorCodeDescription(), kundeDto);
			
			} else if (isLossollarbeitsplan(entityKey)) {
				LossollarbeitsplanDto agDto = lossollarbeitsplanFindByPrimaryKey(Integer.parseInt(hvHeader.getErrorValue()));
				return EJBExcFactory.httpPostLosausgabeTops(hvHeader.getErrorCodeExtended(), 
						hvHeader.getErrorCodeDescription(), agDto);
			}
			
			return EJBExcFactory.httpPostLosausgabeTopsUnbekannt(hvHeader.getErrorCodeExtended(), 
					hvHeader.getErrorCodeDescription());
		}
		
		private boolean isArtikel(String key) {
			return IHttpHeaderKeys.KEY_ARTIKELID.equals(key);
		}
		
		private boolean isLos(String key) {
			return IHttpHeaderKeys.KEY_LOSID.equals(key);
		}
		
		private boolean isLossollmaterial(String key) {
			return IHttpHeaderKeys.KEY_LOSSOLLMATERIALID.equals(key);
		}
		
		private boolean isKunde(String key) {
			return IHttpHeaderKeys.KEY_KUNDEID.equals(key);
		}
		
		private boolean isLossollarbeitsplan(String key) {
			return IHttpHeaderKeys.KEY_AGID.equals(key);
		}
		
		private boolean isPath(String key) {
			return IHttpHeaderKeys.KEY_PATH.equals(key);
		}
	}

	private EJBExceptionLP postLosausgabeToHostImpl(HttpProxyConfig hostConfig, Los los) {

		HttpRequestConfig requestConfig = new HttpRequestConfig(hostConfig, "/production/" + los.getIId() + "/output");
		try {
			httpPost(requestConfig);
		} catch (IOException e) {
			LosDto losDto = losFindByPrimaryKeyOhneExc(los.getIId());
			return EJBExcFactory.verbindungsfehlerBeiHttpPostLosausgabe(losDto, hostConfig, e);
		} catch (HttpStatusCodeException e) {
			LosDto losDto = losFindByPrimaryKeyOhneExc(los.getIId());
			return EJBExcFactory.unerwarteterStatusCodeBeiHttpPostLosausgabe(losDto, e);
		}
		return null;
	}

	private EJBExceptionLP postLoserledigungToHostImpl(HttpProxyConfig hostConfig, Los los) {
		HttpRequestConfig requestConfig = new HttpRequestConfig(hostConfig,
				"/production/" + los.getIId() + "/completion");
		try {
			httpPost(requestConfig);
		} catch (IOException e) {
			LosDto losDto = losFindByPrimaryKeyOhneExc(los.getIId());
			return EJBExcFactory.verbindungsfehlerBeiHttpPostLoserledigung(losDto, hostConfig, e);
		} catch (HttpStatusCodeException e) {
			LosDto losDto = losFindByPrimaryKeyOhneExc(los.getIId());
			return EJBExcFactory.unerwarteterStatusCodeBeiHttpPostLoserledigung(losDto, e);
		}
		return null;
	}

	private void httpPost(HttpRequestConfig requestConfig) throws IOException, HttpStatusCodeException {
		HttpClient client = new DefaultHttpClient();
		try {
			HttpPost httpPost = new HttpPost(requestConfig.getUri());
			HttpResponse response = client.execute(httpPost);
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				myLogger.info("HTTP POST to " + requestConfig.getUri() + " succeeded with status=" + status);
			} else {
				myLogger.error(
						"HTTP POST to " + requestConfig.getUri() + " did not succeed with status code '" + status);
				HvHttpHeaderTransformer headerTransformer = new HvHttpHeaderTransformer();
				HvHttpHeader hvHeader = headerTransformer.process(response);
				throw new HttpStatusCodeException(requestConfig, status, 
						response.getStatusLine().getReasonPhrase(), hvHeader);
			}
		} catch (IOException e) {
			myLogger.error("Error during HTTP POST execution to " + requestConfig.getUri(), e);
			throw e;
		} finally {
			client.getConnectionManager().shutdown();
		}
	}

	private EJBExceptionLP postLoserledigungToHost(Integer losIId, TheClientDto theClientDto) {
		Los los = em.find(Los.class, losIId);

		HttpProxyConfig hostConfig = getParameterFac().getHvToHydraHost(theClientDto.getMandant());
		if (!hostConfig.isDefined())
			return null;

		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_HYDRA, theClientDto)) {
			return null;
		}

		return postLoserledigungToHostImpl(hostConfig, los);
	}

	@Override
	public List<LosDto> losFindOffeneByTechniker(Integer personalIdTechniker, List<String> stati, Integer tageInZukunft,
			TheClientDto theClientDto) {
		Timestamp ts = Helper.cutTimestampAddDays(getTimestamp(), 1 + tageInZukunft);
		String inStati = Helper.arrayToSqlInList(stati.toArray(new String[0]));

		SessionFactory factory = FLRSessionFactory.getFactory();
		org.hibernate.Session session = factory.openSession();
		String search = "FROM FLRLos AS l " + "LEFT OUTER JOIN l.technikerset AS t " + "WHERE l.mandant_c_nr = '"
				+ theClientDto.getMandant() + "' " + "AND l.status_c_nr IN " + inStati + " AND ( "
				+ " l.personal_i_id_techniker = " + personalIdTechniker + " OR t.personal_i_id = " + personalIdTechniker
				+ ") " + " AND l.t_produktionsbeginn < '" + Helper.formatTimestampWithSlashes(ts) + "' "
				+ "ORDER BY l.t_produktionsbeginn, l.t_produktionsende";
		org.hibernate.Query query = session.createQuery(search);
		List<Object[]> resultList = query.list();
		List<LosDto> losDtos = new ArrayList<LosDto>();
		for (Object[] o : resultList) {
			FLRLos l = (FLRLos) o[0];
			LosDto losDto = losFindByPrimaryKey(l.getI_id());
			losDtos.add(losDto);
		}

		closeSession(session);
		return losDtos;
	}

	@Override
	public List<LosDto> losFindOffeneByMe(List<String> erlaubteStati, Integer tageInZukunft,
			TheClientDto theClientDto) {
		if (!getBenutzerServicesFac().hatRechtOder(theClientDto,
				new String[] { RechteFac.RECHT_FERT_LOS_R, RechteFac.RECHT_FERT_LOS_CUD })) {
			myLogger.info("Benutzer " + theClientDto.getBenutzername() + " hat kein Lese/Schreibrecht.");
			return new ArrayList<LosDto>();
		}
		// new String[] {STATUS_IN_PRODUKTION, STATUS_TEILERLEDIGT}
		return losFindOffeneByTechniker(theClientDto.getIDPersonal(), erlaubteStati, tageInZukunft, theClientDto);
	}

	@Override
	public void setzeLosInProduktion(Integer losId, TheClientDto theClientDto) {
		Validator.pkFieldNotNull(losId, "losId");
		Validator.dtoNotNull(theClientDto, "theClientDto");

		LosDto losDto = losFindByPrimaryKey(losId);

		boolean inProduktion = false;
		if (STATUS_AUSGEGEBEN.equals(losDto.getStatusCNr())) {
			boolean bAusgegebenEigenerStatus = getParameterFac().getAusgabeEigenerStatus(theClientDto.getMandant());
			if (bAusgegebenEigenerStatus) {
				Los los = em.find(Los.class, losId);
				los.setStatusCNr(STATUS_IN_PRODUKTION);
				em.merge(los);
				em.flush();

				inProduktion = true;
			}
		}

		if (!inProduktion) {
			if (Helper.isOneOf(losDto.getStatusCNr(),
					new String[] { STATUS_AUSGEGEBEN, STATUS_GESTOPPT, STATUS_IN_PRODUKTION, STATUS_TEILERLEDIGT })) {
				throw EJBExcFactory.losIstAusgegeben(losDto);
			}

			if (STATUS_STORNIERT.equals(losDto.getStatusCNr())) {
				throw EJBExcFactory.losIstStorniert(losDto);
			}

			if (STATUS_ERLEDIGT.equals(losDto.getStatusCNr())) {
				throw EJBExcFactory.losIstErledigt(losDto);
			}

			throw EJBExcFactory.losIstNichtAusgegeben(losDto);
		}
	}

	@Override
	public ZusatzstatusDto zusatzstatusFindByMandantCNrCBezOhneExc(String mandantCnr, String zusatzstatusCbez) {
		Zusatzstatus entity = ZusatzstatusQuery.resultByMandantCNrCBezNoEx(em, zusatzstatusCbez, mandantCnr);
		return entity != null ? assembleZusatzstatusDto(entity) : null;
	}

	@Override
	public List<LossollarbeitsplanDto> lossollarbeitsplanFindByLosIIdArbeitsgangnummer(Integer losIId,
			Integer arbeitsgangnummer) {
		List<Lossollarbeitsplan> list = LossollarbeitsplanQuery.listByLosIIdArbeitsgang(em, losIId, arbeitsgangnummer);
		return Arrays.asList(assembleLossollarbeitsplanDtos(list));
	}

	@Override
	public List<LossollarbeitsplanDto> lossollarbeitsplanFindByLosIIdArbeitsgangnummerUnterarbeitsgang(Integer losIId,
			Integer arbeitsgang, Integer unterarbeitsgang) {
		List<Lossollarbeitsplan> list = LossollarbeitsplanQuery.listByLosIIdArbeitsgangUnterarbeitsgang(em, losIId,
				arbeitsgang, unterarbeitsgang);
		return Arrays.asList(assembleLossollarbeitsplanDtos(list));
	}

	private void handleExcPreiseAktualisieren(CreateLosablieferungModel model, Throwable t) {
		try {
			createLoszusatzstatusPreisePruefen(model);
			notifyNachrichtenGruppePreisePruefen(model);
		} catch (Throwable t2) {
			myLogger.error("Fehler bei Behandlung der Exception waehrend Preise-Aktualisierung", t2);
		}
	}

	private void notifyNachrichtenGruppePreisePruefen(CreateLosablieferungModel model)
			throws RemoteException, EJBExceptionLP {
		if (getMandantFac().hatModulNachrichten(model.theClientDto())) {
			getNachrichtenFac().nachrichtAblieferpreisepruefenTerminal(model.losIId(), model.theClientDto());
		}
	}

	private void createLoszusatzstatusPreisePruefen(CreateLosablieferungModel model) {
		ZusatzstatusDto pruefenZusatzstatus = zusatzstatusFindByMandantCNrCBezOhneExc(model.mandantCnr(),
				ZUSATZSTATUS_ABLIEFERPREISE);
		if (pruefenZusatzstatus == null) {
			pruefenZusatzstatus = new ZusatzstatusDto();
			pruefenZusatzstatus.setMandantCNr(model.mandantCnr());
			pruefenZusatzstatus.setCBez(ZUSATZSTATUS_ABLIEFERPREISE);
			pruefenZusatzstatus.setISort(getNextZusatzstatus(model.theClientDto()));
			pruefenZusatzstatus.setIId(createZusatzstatus(pruefenZusatzstatus, model.theClientDto()));
		}

		LoszusatzstatusDto zusatzstatusDto = loszusatzstatusFindByLosIIdZusatzstatusIIdOhneExc(model.losIId(),
				pruefenZusatzstatus.getIId());
		if (zusatzstatusDto == null) {
			zusatzstatusDto = new LoszusatzstatusDto();
			zusatzstatusDto.setLosIId(model.losIId());
			zusatzstatusDto.setZusatzstatusIId(pruefenZusatzstatus.getIId());
			createLoszusatzstatus(zusatzstatusDto, model.theClientDto());
		}
	}

	private LosablieferungDto createLosablieferungImpl(CreateLosablieferungModel model) {
		myLogger.logData(model.dto());
		pruefeLosstatusFuerLosablieferung(model.losIId());

		Los los = em.find(Los.class, model.losIId());
		LosId losId = new LosId(model.losIId());

		try {
			// Vor Statusaenderung noch Restmengen zurueck buchen
			String losStatus = model.isErledigt() ? STATUS_ERLEDIGT : STATUS_TEILERLEDIGT;
			los.setStatusCNr(losStatus);

			if (model.isErledigt()) {
				los.setPersonalIIdErledigt(model.theClientDto().getIDPersonal());
				los.setTErledigt(getTimestamp());

				LossollmaterialDto[] lossollmaterialDtos = lossollmaterialFindByLosIId(model.losIId());
				if (getParameterFac().getBeiLosErledigenMaterialNachbuchen(model.mandantCnr())) {
					bucheFehlmengenNach(losId, lossollmaterialDtos, model.isKommissionierterminal(),
							model.theClientDto());
				}

				for (LossollmaterialDto losmat : lossollmaterialDtos) {
					getFehlmengeFac().aktualisiereFehlmenge(LocaleFac.BELEGART_LOS, losmat.getIId(),
							model.theClientDto());
				}
			}

			model.dto().setPersonalIIdAendern(model.theClientDto().getIDPersonal());
			// primary key
			Integer iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_LOSABLIEFERUNG);
			model.dto().setIId(iId);
			// werte werden spaeter berechnet
			model.dto().setNGestehungspreis(new BigDecimal(0));
			model.dto().setNMaterialwert(new BigDecimal(0));
			model.dto().setNArbeitszeitwert(new BigDecimal(0));
			model.dto().setNMaterialwertdetailliert(new BigDecimal(0));
			model.dto().setNArbeitszeitwertdetailliert(new BigDecimal(0));

			Losablieferung losablieferung = new Losablieferung(model.dto().getIId(), model.dto().getLosIId(),
					model.dto().getNMenge(), model.dto().getNGestehungspreis(), model.dto().getNMaterialwert(),
					model.dto().getNArbeitszeitwert(), model.dto().getPersonalIIdAendern(),
					model.dto().getNMaterialwertdetailliert(), model.dto().getNArbeitszeitwertdetailliert());
			em.persist(losablieferung);
			em.flush();

			// speichern
			model.dto().setTAendern(losablieferung.getTAendern());
			model.dto().setBGestehungspreisNeuBerechnen(losablieferung.getBGestehungspreisneuberechnen());
			setLosablieferungFromLosablieferungDto(losablieferung, model.dto());

			HvDtoLogger<LosablieferungDto> losablieferungLogger = new HvDtoLogger<LosablieferungDto>(em, model.losIId(),
					model.theClientDto());
			losablieferungLogger.logInsert(model.dto());

			bucheLosAblieferungAufLager(model.dto(), model.theClientDto());
			bucheNegativeSollmengenAufLager(losId, model.isErledigt(), model.theClientDto());

			return model.dto();
		} catch (EntityNotFoundException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
			return null;
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public Map<Integer, RestmengeUndChargennummerDto> getAllLossollmaterialMitRestmengen(Integer losIId,
			TheClientDto theClientDto) {
		Validator.pkFieldNotNull(losIId, "losIId");
		// Wenn kein Restmengenlager definiert ist, dann keine Restmengen suchen.
		// Wirft auch gleich die richtige exception, falls ein Fehler im Parameter ist
		if (!getRestmengenLager(theClientDto).isPresent()) {
			return Collections.emptyMap();
		}

		Map<Integer, RestmengeUndChargennummerDto> mengeMap = new HashMap<>();

		LosDto los = losFindByPrimaryKey(losIId);
		LossollmaterialDto[] sollmaterialDtos = lossollmaterialFindByLosIId(losIId);
		for (LossollmaterialDto sollMat : sollmaterialDtos) {
			ArtikelDto artikel = getArtikelFac().artikelFindByPrimaryKeySmall(sollMat.getArtikelIId(), theClientDto);
			if (artikel.getFVerschnittbasis() == null || artikel.getFVerschnittbasis().doubleValue() == 0.0)
				continue;
			BigDecimal gesMenge = sollMat.getNMenge();
			BigDecimal benoetigteMenge = Helper.berechneMengeInklusiveVerschnitt(sollMat.getnMengeProLos(),
					artikel.getFVerschnittfaktor(), null, los.getNLosgroesse(), null);
			benoetigteMenge = benoetigteMenge.multiply(los.getNLosgroesse());
			BigDecimal uebrigeMenge = gesMenge.subtract(benoetigteMenge);
			List<SeriennrChargennrMitMengeDto> chargennummern = Collections.emptyList();
			if (artikel.istArtikelSnrOderchargentragend()) {
				chargennummern = getGebuchteChargennummernUndMengeVonSollmaterial(sollMat);
			}
			RestmengeUndChargennummerDto chnrDto = new RestmengeUndChargennummerDto(uebrigeMenge, chargennummern);
			mengeMap.put(sollMat.getIId(), chnrDto);
		}

		return mengeMap;
	}

	private List<SeriennrChargennrMitMengeDto> getGebuchteChargennummernUndMengeVonSollmaterial(
			LossollmaterialDto sollMat) {
		List<SeriennrChargennrMitMengeDto> chargennummern;
		chargennummern = new ArrayList<>();
		Map<String, Integer> gebuchteChNr = new HashMap<>();
		LosistmaterialDto[] allIstmaterial = losistmaterialFindByLossollmaterialIId(sollMat.getIId());
		for (LosistmaterialDto istMat : allIstmaterial) {
			List<SeriennrChargennrMitMengeDto> listChnr = getLagerFac()
					.getAllSeriennrchargennrEinerBelegartpositionOhneChargeneigenschaften(LocaleFac.BELEGART_LOS,
							istMat.getIId());
			for (SeriennrChargennrMitMengeDto chnrDto : listChnr) {
				// Alle Chargennummern die nicht doppelt sind hinzufuegen
				if (gebuchteChNr.putIfAbsent(chnrDto.getCSeriennrChargennr(), chargennummern.size()) == null) {
					chargennummern.add(chnrDto);
				} else {
					Integer idx = gebuchteChNr.get(chnrDto.getCSeriennrChargennr());
					SeriennrChargennrMitMengeDto dtoAendern = chargennummern.get(idx);
					dtoAendern.setNMenge(dtoAendern.getNMenge().add(chnrDto.getNMenge()));
				}
			}
		}
		return chargennummern;
	}

	private Optional<LagerDto> getRestmengenLager(TheClientDto theClientDto) {
		try {
			ParametermandantDto parameterRestlager = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKEL_RESTMENGENLAGER);
			String restmengenLagerName = parameterRestlager.getCWert();
			if (restmengenLagerName == null || restmengenLagerName.trim().equals("")) {
				return Optional.empty();
			}
			LagerDto lager;
			try {
				lager = getLagerFac().lagerFindByCNrByMandantCNr(restmengenLagerName, theClientDto.getMandant());
			} catch (EJBExceptionLP e) {
				if (e.getCode() == EJBExceptionLP.FEHLER_BEI_FIND) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_RESTMENGENLAGER_UNGUELTIG, "",
							restmengenLagerName);
				} else {
					throw e;
				}
			}
			return Optional.of(lager);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
			return null;
		}
	}

	private void pruefeRestmengeDaten(LossollmaterialDto sollMat, RestmengeUndChargennummerDto restMenge,
			ArtikelDto artikel) {
		if (artikel.isChargennrtragend()) {
			if (restMenge.getChargenNummernMitMenge().isEmpty())
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_FEHLER_IN_RESTMENGE,
						"Artikel ist Chargennummer tragend, aber keine Restmenge Chargeninfos");
			BigDecimal gesMenge = restMenge.getGesMenge();
			Optional<BigDecimal> sum = restMenge.getChargenNummernMitMenge().stream().map(chnr -> chnr.getNMenge())
					.filter(Objects::nonNull).reduce(BigDecimal::add);
			if (!sum.isPresent() || gesMenge.compareTo(sum.get()) != 0) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_FEHLER_IN_RESTMENGE,
						"Gesamtmenge und einzelmengen stimmen nicht ueberein");
			}
		} else {
			if (!restMenge.getChargenNummernMitMenge().isEmpty()) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_FEHLER_IN_RESTMENGE,
						"Artikel hat keine Chargennummer aber Chargennummer Info bei Restmenge");
			}
		}
	}

	@TransactionTimeout(1000)
	public void bucheRestmengenAufLager(Map<Integer, RestmengeUndChargennummerDto> lossollmaterialIIdZuRestmengen,
			TheClientDto theClientDto) {
		bucheRestmengenAufLagerImpl(lossollmaterialIIdZuRestmengen, theClientDto);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private void bucheRestmengenAufLagerImpl(Map<Integer, RestmengeUndChargennummerDto> lossollmaterialIIdZuRestmengen,
			TheClientDto theClientDto) {
		if (lossollmaterialIIdZuRestmengen.isEmpty()) {
			myLogger.info("bucheRestmengenAufLager ohne Daten aufgerufen, unnoetiger Server call");
			return;
		}
		Optional<LagerDto> restmengenLager = getRestmengenLager(theClientDto);
		if (!restmengenLager.isPresent()) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER,
					"bucheRestmengenAufLager aufgerufen, aber kein Restmengenlager definiert");
		}

		for (Map.Entry<Integer, RestmengeUndChargennummerDto> entry : lossollmaterialIIdZuRestmengen.entrySet()) {
			LossollmaterialDto sollMat = lossollmaterialFindByPrimaryKey(entry.getKey());
			RestmengeUndChargennummerDto restmengeDto = entry.getValue();
			ArtikelDto artikel = getArtikelFac().artikelFindByPrimaryKey(sollMat.getArtikelIId(), theClientDto);
			pruefeRestmengeDaten(sollMat, restmengeDto, artikel);

			BigDecimal gesMenge = restmengeDto.getGesMenge();
			if (BigDecimal.ZERO.compareTo(gesMenge) == 0) {
				// Wenn keine Menge, dann ignorieren
				continue;
			}
			List<SeriennrChargennrMitMengeDto> chargennummern = restmengeDto.getChargenNummernMitMenge();
			LossollmaterialDto restmengeBuchungSollmat = new LossollmaterialDto();
			restmengeBuchungSollmat.setBNachtraeglich(Helper.getShortTrue());
			restmengeBuchungSollmat.setEinheitCNr(sollMat.getEinheitCNr());
			restmengeBuchungSollmat.setLosIId(sollMat.getLosIId());
			restmengeBuchungSollmat.setMontageartIId(sollMat.getMontageartIId());
			restmengeBuchungSollmat.setNSollpreis(sollMat.getNSollpreis());
			restmengeBuchungSollmat.setNMenge(gesMenge);
			restmengeBuchungSollmat.setArtikelIId(sollMat.getArtikelIId());
			String pos = "";
			if (sollMat.getCPosition() != null)
				pos = sollMat.getCPosition();
			pos += "_RESTMENGE_RUECKBUCHUNG";
			restmengeBuchungSollmat.setCPosition(pos);
			restmengeBuchungSollmat = createLossollmaterial(restmengeBuchungSollmat, theClientDto);
			if (chargennummern.isEmpty()) {
				// Keine Chargennummern, nur ein Istmaterial
				createRestmengeIstmaterial(gesMenge, null, restmengenLager.get(), sollMat, restmengeBuchungSollmat,
						theClientDto);
			} else {
				for (SeriennrChargennrMitMengeDto chnr : chargennummern) {
					if (chnr.getNMenge() != null && chnr.getNMenge().compareTo(BigDecimal.ZERO) != 0) {
						createRestmengeIstmaterial(chnr.getNMenge(), chnr.getCSeriennrChargennr(),
								restmengenLager.get(), sollMat, restmengeBuchungSollmat, theClientDto);
					}
				}
			}
		}
	}

	private void createRestmengeIstmaterial(BigDecimal menge, String chnr, LagerDto restmengenLager,
			LossollmaterialDto sollMat, LossollmaterialDto restmengeSollmat, TheClientDto theClientDto) {
		try {
			LosistmaterialDto istMaterial = new LosistmaterialDto();
			istMaterial.setLagerIId(restmengenLager.getIId());
			istMaterial.setBAbgang(Helper.getShortFalse());
			istMaterial.setNMenge(menge);
			istMaterial.setLossollmaterialIId(restmengeSollmat.getIId());

			BigDecimal preis = getAusgegebeneMengePreis(sollMat.getIId(), null, theClientDto);
			istMaterial = createLosistmaterialImpl(istMaterial, chnr, false, preis, theClientDto);
			updateLosistmaterialGestehungspreis(istMaterial.getIId(), preis, theClientDto);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
	}

	private void removeRestmengenBuchungen(Integer losIId, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP {
		Session sess = getCurrentSession();
		Criteria crit = sess.createCriteria(FLRLosistmaterial.class, "istmaterial");
		crit.createAlias("istmaterial.flrlossollmaterial", "sollmaterial");
		crit.add(Restrictions.eq("sollmaterial.los_i_id", losIId));
		crit.add(Restrictions.like("sollmaterial.c_position", "%_RESTMENGE_RUECKBUCHUNG"));
		crit.add(Restrictions.eq("sollmaterial.b_nachtraeglich", Helper.getShortTrue()));
		List<FLRLosistmaterial> results = crit.list();
		results.stream().map(FLRLosistmaterial::getLossollmaterial_i_id).sequential().unordered().distinct()
				.forEach(id -> {
					LossollmaterialDto dto = lossollmaterialFindByPrimaryKey(id);
					removeLossollmaterial(dto, true, theClientDto);
				});
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public LosablieferungResultDto losAbliefernUeberTerminal(LosablieferungTerminalDto losablieferungDto,
			TheClientDto theClientDto) {
		myLogger.logData(losablieferungDto);
		pruefeLosstatusFuerLosablieferung(losablieferungDto.getLosIId());

		BigDecimal abgelieferteMenge = getErledigteMenge(losablieferungDto.getLosIId(), theClientDto);

		LosDto losDto = losFindByPrimaryKey(losablieferungDto.getLosIId());
		BigDecimal aenderungLosgroesse = BigDecimal.ZERO;

		// Ablieferung Schrottmenge
		if (losablieferungDto.getMengeSchrott() != null) {
			LosablieferungResultDto losablieferungSchrottDto = terminalSchrottablieferung(losablieferungDto, losDto,
					theClientDto);
			if (Boolean.TRUE.equals(losablieferungDto.getBAendereLosgroesseUmSchrottmenge())) {
				aenderungLosgroesse = aenderungLosgroesse.add(losablieferungDto.getMengeSchrott());
			}

			if (losablieferungDto.getNMenge() == null) {
				if (BigDecimal.ZERO.compareTo(aenderungLosgroesse) < 0) {
					aendereLosgroesse(losDto.getIId(), losDto.getNLosgroesse().add(aenderungLosgroesse).intValue(),
							false, theClientDto);
				}
				return losablieferungSchrottDto;
			}
		}

		// Moegliche Ueberlieferung
		BigDecimal gesamtMenge = getErledigteMenge(losDto.getIId(), theClientDto).add(losablieferungDto.getNMenge());
		if (losDto.getNLosgroesse().add(aenderungLosgroesse).compareTo(gesamtMenge) < 0) {
			aenderungLosgroesse = gesamtMenge.subtract(losDto.getNLosgroesse());
		}

		if (aenderungLosgroesse.signum() > 0) {
			aendereLosgroesse(losDto.getIId(), losDto.getNLosgroesse().add(aenderungLosgroesse).intValue(), false,
					theClientDto);
			losDto.setNLosgroesse(losDto.getNLosgroesse().add(aenderungLosgroesse));
		}

		bucheMaterialAufLos(losDto, losablieferungDto.getGesamtmenge(), false, false, true, theClientDto, null, false);
		if (getParameterFac().getSollsatzgroessePruefen(theClientDto.getMandant())) {
			pruefePositionenMitSollsatzgroesseUnterschreitung(losDto.getIId(), losablieferungDto.getNMenge(), true,
					theClientDto);
		}

		boolean bErledigt = losDto.getNLosgroesse().compareTo(gesamtMenge) <= 0
				&& getParameterFac().getAutofertigAblieferungTerminal(theClientDto.getMandant());
		CreateLosablieferungModel model = new CreateLosablieferungModel(losablieferungDto, theClientDto);
		model.setErledigt(bErledigt);
		LosablieferungResultDto resultDto = losAbliefern(model);
		resultDto.setLosErledigt(bErledigt);
		return resultDto;
	}

	private LosablieferungResultDto terminalSchrottablieferung(LosablieferungTerminalDto losablieferungDto,
			LosDto losDto, TheClientDto theClientDto) {
		bucheMaterialAufLos(losDto, losablieferungDto.getMengeSchrott(), false, false, true, theClientDto, null, false);
		if (getParameterFac().getSollsatzgroessePruefen(theClientDto.getMandant())) {
			pruefePositionenMitSollsatzgroesseUnterschreitung(losDto.getIId(), losablieferungDto.getMengeSchrott(),
					true, theClientDto);
		}
		// Hole erstes Schrottlager, falls nicht vorhanden Los-Ziellager
		// verwenden
		List<Lager> schrottlager = LagerQuery.listByMandantCNrLagerartCNr(em, theClientDto.getMandant(),
				LagerFac.LAGERART_SCHROTT);
		int lagerIId = schrottlager.isEmpty() ? losDto.getLagerIIdZiel() : schrottlager.get(0).getIId();

		LosablieferungDto losablieferungSchrottDto = new LosablieferungDto();
		losablieferungSchrottDto.setLosIId(losablieferungDto.getLosIId());
		losablieferungSchrottDto.setLagerIId(lagerIId);
		losablieferungSchrottDto.setNMenge(losablieferungDto.getMengeSchrott());
		// PJ21261 Schrott mit Charge
		if (losablieferungDto.getSeriennrChargennrMitMenge() != null
				&& losablieferungDto.getSeriennrChargennrMitMenge().size() == 1) {
			ArrayList<SeriennrChargennrMitMengeDto> al = new ArrayList<SeriennrChargennrMitMengeDto>();
			SeriennrChargennrMitMengeDto scDto = new SeriennrChargennrMitMengeDto();
			scDto.setCSeriennrChargennr(
					losablieferungDto.getSeriennrChargennrMitMenge().get(0).getCSeriennrChargennr());
			scDto.setNMenge(losablieferungDto.getMengeSchrott());
			al.add(scDto);
			losablieferungSchrottDto.setSeriennrChargennrMitMenge(al);
		}

		CreateLosablieferungModel model = new CreateLosablieferungModel(losablieferungSchrottDto, theClientDto);
		return losAbliefern(model);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public LosablieferungResultDto losAbliefern(CreateLosablieferungModel model) {
		LosablieferungResultDto laResultDto = new LosablieferungResultDto(createLosablieferungImpl(model));
		if (model.isErledigt()) {
			EJBExceptionLP returnedExc = postLoserledigungToHost(model.losIId(), model.theClientDto());
			laResultDto.setEjbExceptionLP(returnedExc);
		}
		return laResultDto;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void ablieferungAktualisierePreise(CreateLosablieferungModel model) {
		aktualisiereNachtraeglichPreiseAllerLosablieferungen(model.losIId(), model.theClientDto(), !model.isErledigt());
	}

	private void bucheNegativeSollmengenAufLager(LosId losId, boolean bErledigt, TheClientDto theClientDto) {
		List<Lossollmaterial> sollmaterialien = LossollmaterialQuery.listByLosIId(em, losId);
		if (sollmaterialien.isEmpty())
			return;

		Los los = em.find(Los.class, losId.id());
		Boolean bucheAufZiellager = getParameterFac()
				.getNegativeSollmengenBuchenAufZiellager(theClientDto.getMandant());
		Integer lagerIId = null;
		if (Boolean.TRUE.equals(bucheAufZiellager)) {
			lagerIId = los.getLagerIIdZiel();
		} else {
			List<Loslagerentnahme> losLaeger = LoslagerentnahmeQuery.listByLosIId(em, losId);
			if (!losLaeger.isEmpty()) {
				lagerIId = losLaeger.get(0).getLagerIId();
			}
		}
		BigDecimal bdAbliefermengeGesamt = getErledigteMenge(losId.id(), theClientDto);

		for (Lossollmaterial sollmatEntity : sollmaterialien) {
			if (sollmatEntity.getNMenge().signum() >= 0)
				continue;

			// Sollsatzgroesse
			BigDecimal ssg = sollmatEntity.getNMenge().abs().divide(los.getNLosgroesse(), 10,
					BigDecimal.ROUND_HALF_EVEN);
			BigDecimal soll = bdAbliefermengeGesamt.multiply(ssg);

			if (Helper.short2boolean(sollmatEntity.getBRuestmenge())) {
				if (bErledigt) {
					soll = sollmatEntity.getNMenge().abs();
				} else {
					// Negative Sollmengen werden bei Ruestmenge erst zurueckgebucht, wenn sich der
					// Los-Status auf 'Erledigt' aendert
					continue;
				}
			}

			BigDecimal ausgegeben = getAusgegebeneMenge(sollmatEntity.getIId(), null, theClientDto).abs();
			BigDecimal mengeNeu = soll.subtract(ausgegeben);

			if (mengeNeu.signum() > 0) {
				LosistmaterialDto istmat = new LosistmaterialDto();
				istmat.setLagerIId(lagerIId);
				istmat.setLossollmaterialIId(sollmatEntity.getIId());
				istmat.setNMenge(mengeNeu.abs());
				istmat.setBAbgang(Helper.getShortFalse());

				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(sollmatEntity.getArtikelIId(),
						theClientDto);

				if (artikelDto.istArtikelSnrOderchargentragend()) {
					ArrayList al = new ArrayList();
					al.add(artikelDto.getCNr());
					al.add(sollmatEntity.getNMenge());

					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FERTIGUNG_NEGATIVE_SOLLMENGE_ARTIKEL_SNR_CNHR_BEHAFTET, al,
							new Exception("FEHLER_FERTIGUNG_NEGATIVE_SOLLMENGE_ARTIKEL_SNR_CNHR_BEHAFTET"));
				}

				createLosistmaterial(istmat, null, theClientDto);
			}
		}
	}

	@Override
	public LosablieferungDto losAbliefernUeberSoap(LosId losId, BigDecimal abliefermenge, String station,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
		LosDto losDto = getFertigungFacLocal().prepareLosAbliefernUeberSoap(losId, abliefermenge, station,
				theClientDto);

		LosablieferungDto losablieferungDto = new LosablieferungDto();
		losablieferungDto.setLosIId(losId.id());
		losablieferungDto.setNMenge(abliefermenge);

		BigDecimal bdGesamt = getErledigteMenge(losId.id(), theClientDto).add(abliefermenge);
		boolean losErledigen = getParameterFac().getAutofertigAblieferungTerminal(theClientDto.getMandant())
				&& losDto.getNLosgroesse().compareTo(bdGesamt) <= 0;
		return createLosablieferungFuerTerminal(losablieferungDto, theClientDto, losErledigen);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public LosDto prepareLosAbliefernUeberSoap(LosId losId, BigDecimal abliefermenge, String station,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
		LosDto losDto = losFindByPrimaryKey(losId.id());

		// TODO: Parameter fuer nicht "ueberbuchen"
		BigDecimal bdGesamt = getFertigungFac().getErledigteMenge(losId.id(), theClientDto).add(abliefermenge);
		if (losDto.getNLosgroesse().compareTo(bdGesamt) == -1) {
			// wenn gesamte Abliefermenge > Losmenge dann Losgroesse
			// anpassen
			aendereLosgroesseSubtransaction(losId.id(), bdGesamt.intValue(), false, theClientDto);
			losDto = losFindByPrimaryKey(losId.id());
		}

		// fehlendes Material buchen
		bucheMaterialAufLos(losDto, abliefermenge, false, false, true, theClientDto, null, false);

		// Pruefung ist abhaengig von Parameter
		if (getParameterFac().getSollsatzgroessePruefen(theClientDto.getMandant())) {
			pruefePositionenMitSollsatzgroesseUnterschreitung(losId.id(), abliefermenge, true, theClientDto);
		}

		// SP 2013/01036
		ParametermandantDto paramBucheEnde = getParameterFac().getMandantparameter(theClientDto.getMandant(),
				ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_ABLIEFERUNG_BUCHT_ENDE);

		if (paramBucheEnde.asBoolean()) {
			Integer zeitdatenIId = createEndeZeitdaten(station, theClientDto);
			// PJ17797
			if (abliefermenge.signum() == 1 && getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
					MandantFac.ZUSATZFUNKTION_STUECKRUECKMELDUNG, theClientDto)) {
				Integer lossollarbeitsplanIId = null;
				LossollarbeitsplanDto[] sollDtos = getFertigungFac().lossollarbeitsplanFindByLosIId(losDto.getIId());
				if (sollDtos.length > 0) {
					lossollarbeitsplanIId = sollDtos[sollDtos.length - 1].getIId();
				} else {
					lossollarbeitsplanIId = getFertigungFac().defaultArbeitszeitartikelErstellen(losDto, theClientDto);
				}

				createLosGut(theClientDto, abliefermenge, zeitdatenIId, lossollarbeitsplanIId);
			}
		}

		return losDto;
	}

	/**
	 * @param theClientDto
	 * @param bdMenge
	 * @param zeitdatenIId
	 * @param lossollarbeitsplanIId
	 */
	private Integer createLosGut(TheClientDto theClientDto, BigDecimal bdMenge, Integer zeitdatenIId,
			Integer lossollarbeitsplanIId) {
		LosgutschlechtDto losgutschlechtDto = new LosgutschlechtDto();
		losgutschlechtDto.setZeitdatenIId(zeitdatenIId);
		losgutschlechtDto.setLossollarbeitsplanIId(lossollarbeitsplanIId);
		losgutschlechtDto.setNGut(bdMenge);
		losgutschlechtDto.setNSchlecht(new BigDecimal(0));
		losgutschlechtDto.setNInarbeit(new BigDecimal(0));
		return createLosgutschlecht(losgutschlechtDto, theClientDto).getLosgutschlechtIId();
	}

	/**
	 * @param station
	 * @param theClientDto
	 * @param personal
	 * @throws RemoteException
	 */
	private int createEndeZeitdaten(String station, TheClientDto theClientDto) throws RemoteException {
		ZeitdatenDto zeitdatenDto = new ZeitdatenDto();
		zeitdatenDto.setPersonalIId(theClientDto.getIDPersonal());
		zeitdatenDto.setCWowurdegebucht(station);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		zeitdatenDto.setTZeit(new java.sql.Timestamp(cal.getTimeInMillis()));
		zeitdatenDto.setTAendern(zeitdatenDto.getTZeit());

		Integer taetigkeitIId_Ende = getZeiterfassungFac()
				.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_ENDE, theClientDto).getIId();
		zeitdatenDto.setTaetigkeitIId(taetigkeitIId_Ende);
		zeitdatenDto.setCBelegartnr(null);
		zeitdatenDto.setArtikelIId(null);
		zeitdatenDto.setIBelegartid(null);
		zeitdatenDto.setIBelegartpositionid(null);
		Integer zeitdatenIId = getZeiterfassungFac().createZeitdaten(zeitdatenDto, false, false, false, false,
				theClientDto);

		return zeitdatenIId;
	}

	private Optional<LosArbeitsplanZeitVergleichDto> createVergleichDto(
			Map<Integer, List<AuftragzeitenDto>> mannZeitenNachAG,
			Map<Integer, List<AuftragzeitenDto>> maschinenZeitenNachAG, LossollarbeitsplanDto dto) {
		int iArbeitsgang = dto.getIArbeitsgangnummer();
		int iLosSollarbeitsplanIId = dto.getIId();
		Optional<Integer> stuecklisteArbeitsplanIId = getStuecklisteArbeitsplanIIdZuLossollarbeitsplan(dto);
		// Wenn kein stuecklisten arbeitsplan, dann brauchen wir da nichts machen
		if (!stuecklisteArbeitsplanIId.isPresent()) {
			return Optional.empty();
		}
		List<AuftragzeitenDto> mannZeiten = mannZeitenNachAG.getOrDefault(iArbeitsgang, Collections.emptyList());
		List<AuftragzeitenDto> maschineZeiten = maschinenZeitenNachAG.getOrDefault(iArbeitsgang,
				Collections.emptyList());
		Integer maschineIId = dto.getMaschineIId();
		MaschineDto maschineDto = null;
		if (maschineIId != null) {
			maschineDto = getZeiterfassungFac().maschineFindByPrimaryKey(maschineIId);
		}
		long sollRuestzeit = dto.getLRuestzeit();

		LosDto losDto = losFindByPrimaryKey(dto.getLosIId());

		List<MaschineDto> maschinen = getAlleMaschinenFuerZeitdaten(maschineZeiten);

		return Optional.of(LosArbeitsplanZeitVergleichDto.create(iArbeitsgang, dto, Optional.ofNullable(maschineDto),
				sollRuestzeit, dto.getLStueckzeit(), losDto.getNLosgroesse(), summeZeiten(mannZeiten),
				summeZeiten(maschineZeiten), stuecklisteArbeitsplanIId.get(), maschinen));
	}

	public List<LosArbeitsplanZeitVergleichDto> getVergleichArbeitsplanIstZeitbuchungen(Integer losIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		AuftragzeitenDto[] mannZeitdaten = getZeiterfassungFac().getAllZeitenEinesBeleges(LocaleFac.BELEGART_LOS,
				losIId, null, null, null, null, ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ARTIKEL, theClientDto);
		AuftragzeitenDto[] maschinenzeiten = getZeiterfassungFac().getAllMaschinenzeitenEinesBeleges(losIId, null, null,
				null, theClientDto);
		LossollarbeitsplanDto[] sollarbeitsplanDtos = lossollarbeitsplanFindByLosIId(losIId);

		Map<Integer, List<AuftragzeitenDto>> mannZeitenNachAG = verdichteNachArbeitsgang(mannZeitdaten);
		Map<Integer, List<AuftragzeitenDto>> maschinenZeitenNachAG = verdichteNachArbeitsgang(maschinenzeiten);

		List<LosArbeitsplanZeitVergleichDto> vergleich = new ArrayList<LosArbeitsplanZeitVergleichDto>();
		for (LossollarbeitsplanDto dto : sollarbeitsplanDtos) {
			Optional<LosArbeitsplanZeitVergleichDto> neuerVgl = createVergleichDto(mannZeitenNachAG,
					maschinenZeitenNachAG, dto);
			neuerVgl.ifPresent(vergleich::add);
		}

		return vergleich;
	}

	/**
	 * Holt alle maschinen aus den Zeitdanten ohne duplikate
	 * 
	 * @param mannZeiten
	 * @param zeiten
	 */
	private List<MaschineDto> getAlleMaschinenFuerZeitdaten(List<AuftragzeitenDto> zeiten) {
		Stream<Integer> ids = zeiten.stream().map(az -> az.getIPersonalMaschinenId()).filter(Objects::nonNull)
				.distinct();
		return ids.map(getZeiterfassungFac()::maschineFindByPrimaryKey).collect(Collectors.toList());
	}

	private Optional<Integer> getStuecklisteArbeitsplanIIdZuLossollarbeitsplan(LossollarbeitsplanDto arbeitsplan) {
		Optional<Integer> losIId = Optional.of(arbeitsplan.getLosIId());
		Optional<LosDto> los = losIId.map(this::losFindByPrimaryKey);
		Optional<Integer> stuecklisteIId = los.map(LosDto::getStuecklisteIId);
		if (stuecklisteIId.isPresent()) {
			List<Stuecklistearbeitsplan> stklArbeitsplan = StuecklistearbeitsplanQuery
					.listByStuecklisteIIdArbeitsgangnummer(em, stuecklisteIId.get(),
							arbeitsplan.getIArbeitsgangnummer());
			return Helper.getFirst(stklArbeitsplan).map(Stuecklistearbeitsplan::getIId);
		}
		return Optional.empty();
	}

	private BigDecimal summeZeiten(List<AuftragzeitenDto> zeiten) {
		if (zeiten == null) {
			return BigDecimal.ZERO;
		}
		Optional<BigDecimal> sum = zeiten.stream().map(z -> new BigDecimal(z.getDdDauer())).reduce(BigDecimal::add);
		// 7 Stellen fuer ca ms aufloesung
		sum = sum.map(s -> s.setScale(7, RoundingMode.HALF_UP));
		return sum.orElse(BigDecimal.ZERO);
	}

	private Map<Integer, List<AuftragzeitenDto>> verdichteNachArbeitsgang(AuftragzeitenDto[] alle) {
		Map<Integer, List<AuftragzeitenDto>> mapNachArbeitsgang = new TreeMap<Integer, List<AuftragzeitenDto>>();
		for (AuftragzeitenDto dto : alle) {
			if (dto.getiArbeitsgang() == null) {
				continue;
			}
			mapNachArbeitsgang.putIfAbsent(dto.getiArbeitsgang(), new ArrayList<AuftragzeitenDto>());
			List<AuftragzeitenDto> list = mapNachArbeitsgang.get(dto.getiArbeitsgang());
			list.add(dto);
		}
		return mapNachArbeitsgang;
	}

	public void uebernehmeNeueArbeitsgangMaschinen(Map<LosArbeitsplanZeitVergleichDto, Integer> maschinen) {
		for (Map.Entry<LosArbeitsplanZeitVergleichDto, Integer> entry : maschinen.entrySet()) {
			int losSollAPIId = entry.getKey().getiLosSollarbeitsplanIId();
			Lossollarbeitsplan arbeitsplan = em.find(Lossollarbeitsplan.class, losSollAPIId);
			if (arbeitsplan != null) {
				arbeitsplan.setMaschineIId(entry.getValue());
			}
			int stklAPIId = entry.getKey().getStuecklisteArbeitsplanIId();
			Stuecklistearbeitsplan stklAP = em.find(Stuecklistearbeitsplan.class, stklAPIId);
			if (stklAP != null) {
				stklAP.setMaschineIId(entry.getValue());
			}
		}
	}

	public void uebernehmeNeueSollzeiten(Map<LosArbeitsplanZeitVergleichDto, BigDecimal> neueSollzeiten) {
		for (Map.Entry<LosArbeitsplanZeitVergleichDto, BigDecimal> entry : neueSollzeiten.entrySet()) {
			LosArbeitsplanZeitVergleichDto vergleich = entry.getKey();
			BigDecimal sollzeit = entry.getValue().multiply(Helper.STUNDE_IN_MS);
			long ruestzeit = vergleich.getSollRuestzeit();
			sollzeit = sollzeit.subtract(new BigDecimal(ruestzeit));
			BigDecimal stueckZeitNeuBD = sollzeit.divide(vergleich.getMenge(), 7, RoundingMode.HALF_UP);
			long stueckZeitNeu = stueckZeitNeuBD.longValue();
			int losSollAPIId = vergleich.getiLosSollarbeitsplanIId();
			Lossollarbeitsplan arbeitsplan = em.find(Lossollarbeitsplan.class, losSollAPIId);
			if (arbeitsplan != null) {
				arbeitsplan.setLStueckzeit(stueckZeitNeu);
				arbeitsplan.setLRuestzeit(ruestzeit);
				arbeitsplan.setNGesamtzeit(Helper.berechneGesamtzeitInStunden(ruestzeit, stueckZeitNeu,
						vergleich.getMenge(), null, arbeitsplan.getIAufspannung()));
			}

			int stklAPIId = vergleich.getStuecklisteArbeitsplanIId();
			Stuecklistearbeitsplan stklAP = em.find(Stuecklistearbeitsplan.class, stklAPIId);
			if (stklAP != null) {
				stklAP.setLStueckzeit(stueckZeitNeu);
				stklAP.setLRuestzeit(ruestzeit);
			}
		}
	}
	
	private void resetLossollmaterialTruTopsImpl(Lossollmaterial entity, Boolean mitArtikel) {
		if (Boolean.TRUE.equals(mitArtikel)) {
			getArtikelFac().resetArtikelTruTopsByArtikelId(new ArtikelId(entity.getArtikelIId()));
		}
		
		entity.setTExportBeginn(null);
		entity.setTExportEnde(null);
		entity.setCFehlercode(null);
		entity.setCFehlertext(null);
		
		em.merge(entity);
		em.flush();
	}
	
	@Override
	public void resetLossollmaterialTruTops(LossollmaterialId lossollmaterialId, Boolean mitArtikel) {
		Validator.notNull(lossollmaterialId, "lossollmaterialId");
		
		Lossollmaterial entity = em.find(Lossollmaterial.class, lossollmaterialId.id());
		Validator.entityFound(entity, lossollmaterialId.id());

		resetLossollmaterialTruTopsImpl(entity, mitArtikel);
	}
	
	@Override
	public void resetLossollmaterialTruTops(Collection<LossollmaterialId> lossollmaterialIds, Boolean mitArtikel) {
		Validator.notNull(lossollmaterialIds, "lossollmaterialIds");
		
		lossollmaterialIds.forEach(id -> {
			Lossollmaterial entity = em.find(Lossollmaterial.class, id.id());
			Validator.entityFound(entity, id.id());
			resetLossollmaterialTruTopsImpl(entity, mitArtikel);
		});
	}
	
	@Override
	public void resetLossollmaterialTruTops(LosId losId, Boolean mitArtikel) {
		Validator.notNull(losId, "losId");
		
		Los entity = em.find(Los.class, losId.id());
		Validator.entityFound(entity, losId.id());
		
		List<Lossollmaterial> sollmaterialien = LossollmaterialQuery.listByLosIId(em, losId);
		sollmaterialien.forEach(material -> 
			resetLossollmaterialTruTopsImpl(material, mitArtikel));
	}
	
	public void updateLossollmaterialExportBeginn(LossollmaterialId lossollmaterialId, Timestamp tExportBeginn, 
			TheClientDto theClientDto) {
		Validator.notNull(lossollmaterialId, "lossollmaterialId");
		
		Lossollmaterial entity = em.find(Lossollmaterial.class, lossollmaterialId.id());
		Validator.entityFound(entity, lossollmaterialId.id());
		
		entity.setTExportBeginn(tExportBeginn);
		entity.setTExportEnde(null);
		entity.setCFehlercode(null);
		entity.setCFehlertext(null);
		
		em.merge(entity);
		em.flush();
	}
	
	public void updateLossollmaterialExportEnde(LossollmaterialId lossollmaterialId, Timestamp tExportEnde, 
			String cFehlercode, String cFehlertext, TheClientDto theClientDto) {
		Validator.notNull(lossollmaterialId, "lossollmaterialId");
		
		Lossollmaterial entity = em.find(Lossollmaterial.class, lossollmaterialId.id());
		Validator.entityFound(entity, lossollmaterialId.id());
		
		entity.setTExportEnde(tExportEnde);
		entity.setCFehlercode(cFehlercode);
		entity.setCFehlertext(cFehlertext);
		
		em.merge(entity);
		em.flush();
	}
}
